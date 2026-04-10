package dev.obscuria.lootjournal.client.renderer;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import dev.obscuria.lootjournal.LootJournalHelper;
import dev.obscuria.lootjournal.client.events.ItemPickupEvent;
import dev.obscuria.lootjournal.client.events.OverflowPickupEvent;
import dev.obscuria.lootjournal.client.events.PickupEvent;
import dev.obscuria.lootjournal.client.registry.ThemeRegistry;
import dev.obscuria.lootjournal.client.renderer.layout.LayoutResult;
import dev.obscuria.lootjournal.client.themes.BakedTheme;
import dev.obscuria.lootjournal.client.themes.styles.PickupStyle;
import dev.obscuria.lootjournal.config.Config;
import dev.obscuria.lootjournal.config.ConfigCache;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

import java.util.HashMap;
import java.util.List;

public final class PickupComponent {

    private static final HashMap<Integer, PickupContainer> slots = Maps.newHashMap();
    private static final List<PickupContainer> activeContainers = Lists.newArrayList();
    private static final List<PickupEvent> queuedPickups = Lists.newArrayList();

    public static void handleEvent(PickupEvent event) {
        if (maybeMerge(event)) return;
        pickupInternal(mapNextEvent(event));
    }

    public static void render(GuiGraphics graphics) {
        graphics.pose().pushPose();
        ConfigCache.anchor.transform(graphics);

        updateLayout();

        for (int i = 0; i < activeContainers.size(); ) {
            var container = activeContainers.get(i);
            if (container.render(graphics)) {
                slots.remove(container.index);
                activeContainers.remove(i);
            } else {
                i++;
            }
        }

        graphics.pose().popPose();

        if (queuedPickups.isEmpty() || isAllSlotsOccupied()) return;

        for (int i = 0; i < queuedPickups.size(); ) {
            var pickup = queuedPickups.get(i);
            var index = findFreeSlot();
            if (index <= -1) break;
            var instance = new PickupContainer(pickup, index);
            addContainer(instance);
            slots.put(index, instance);
            queuedPickups.remove(i);
        }
    }

    private static void updateLayout() {
        var mode = Config.STACKING_MODE.get();
        var total = activeContainers.size();

        for (var i = 0; i < total; i++) {
            var container = activeContainers.get(i);
            var logicalIndex = mode == StackingMode.FIXED_SLOTS ? container.index : i;
            container.targetY = computeTargetY(logicalIndex);

            if (mode == StackingMode.FIXED_SLOTS) {
                container.currentY = container.targetY;
            } else {
                var diff = container.targetY - container.currentY;
                if (Math.abs(diff) < 0.5f) {
                    container.currentY = container.targetY;
                } else {
                    var speed = 10f;
                    container.currentY += diff * Math.min(1f, (float) (container.delta * speed));
                }
            }
        }
    }

    private static float computeTargetY(int index) {
        var step = ThemeRegistry.activeTheme().entryHeight() + Config.SEPARATION.get();
        return ConfigCache.growthDirection.calculate(index, step);
    }

    private static PickupEvent mapNextEvent(PickupEvent event) {
        if (!(event instanceof ItemPickupEvent itemEvent)) return event;
        return shouldCombineNext() ? new OverflowPickupEvent(itemEvent) : event;
    }

    private static void pickupInternal(PickupEvent event) {
        if (maybeMerge(event)) return;
        var index = findFreeSlot();
        if (index > -1) {
            final var container = new PickupContainer(event, index);
            addContainer(container);
            slots.put(index, container);
        } else if (shouldEnqueue(event)) {
            queuedPickups.add(event);
        }
    }

    private static void addContainer(PickupContainer instance) {
        if (Config.STACKING_MODE.get() == StackingMode.SMOOTH_FLOW) {
            activeContainers.add(0, instance);
        } else {
            activeContainers.add(instance);
        }
    }

    private static boolean maybeMerge(PickupEvent pickupEvent) {
        for (var activeEntry : activeContainers)
            if (activeEntry.maybeMerge(pickupEvent))
                return true;
        for (var queuedEntry : queuedPickups)
            if (queuedEntry.maybeMerge(pickupEvent))
                return true;
        return false;
    }

    private static boolean shouldEnqueue(PickupEvent pickupEvent) {
        return pickupEvent instanceof OverflowPickupEvent || queuedPickups.size() < Config.QUEUE_SIZE.get();
    }

    private static boolean shouldCombineNext() {
        return isAllSlotsOccupied() && queuedPickups.size() >= Config.QUEUE_SIZE.get() - 1;
    }

    private static int findFreeSlot() {
        for (var i = 0; i < Config.DISPLAY_CAPACITY.get(); i++)
            if (!slots.containsKey(i))
                return i;
        return -1;
    }

    private static boolean isAllSlotsOccupied() {
        return activeContainers.size() >= Config.DISPLAY_CAPACITY.get();
    }

    public static final class PickupContainer {

        public final long startTime;
        public final PickupEvent event;
        public final BakedTheme theme;
        public final PickupStyle style;
        public LayoutResult layout;

        private long virtualStartTime = -1L;
        private long lastTime;
        private long pulseStart = -1L;

        private double delta;
        private double pulse;

        private double targetProgress;
        private double progress;

        public float currentY;
        public float targetY;

        public int index;

        public PickupContainer(PickupEvent event, int index) {
            this.event = event;
            this.index = index;
            this.startTime = Util.getMillis();
            this.theme = ThemeRegistry.activeTheme();
            this.style = theme.findStyle(event);
            this.event.bind(style);
            this.layout = measureLayout();
        }

        public boolean render(GuiGraphics graphics) {
            var currentTime = Util.getMillis();

            if (virtualStartTime < 0L) {
                virtualStartTime = currentTime;
                lastTime = currentTime;
            }

            this.delta = (currentTime - lastTime) / 1000.0;
            this.lastTime = currentTime;

            var time = currentTime - virtualStartTime;

            updatePulse(currentTime);
            updateTargetProgress(time);
            updateProgress();

            if (!Minecraft.getInstance().options.hideGui) {
                graphics.pose().pushPose();
                graphics.pose().translate(0, currentY, 0);
                var actualPulse = ConfigCache.pulseEasing.compute((float) pulse) * Config.PULSE_STRENGTH.get().floatValue();
                var pickupGraphics = new PickupRenderer(this, (float) progress, actualPulse);
                PickupRenderUtils.render(graphics, pickupGraphics);
                graphics.pose().popPose();
            }

            return time > getDisplayTime();
        }

        public boolean maybeMerge(PickupEvent other) {

            if (!event.maybeMerge(other)) return false;
            this.layout = measureLayout();
            var now = Util.getMillis();

            if (virtualStartTime > 0L) {
                var fadeInSec = Config.FADE_IN_TIME.get();
                var fadeInMs = (long) (fadeInSec * 1000.0);
                var elapsed = now - virtualStartTime;
                if (elapsed > fadeInMs) {
                    virtualStartTime = now - fadeInMs;
                }
            }

            pulseStart = now;
            return true;
        }

        private void updateTargetProgress(long timeMs) {

            var fadeIn = Config.FADE_IN_TIME.get();
            var fadeOut = Config.FADE_OUT_TIME.get();
            var lifetime = Config.DISPLAY_TIME.get();

            var time = timeMs / 1000.0;

            var fadeInEnd = fadeIn;
            var holdEnd = fadeIn + lifetime;
            var fadeOutEnd = fadeIn + lifetime + fadeOut;

            if (time < fadeInEnd) {
                var t = time / fadeInEnd;
                this.targetProgress = Config.FADE_IN_EASING.get().compute((float) t);
            } else if (time < holdEnd) {
                this.targetProgress = 1.0;
            } else if (time < fadeOutEnd) {
                var t = (time - holdEnd) / fadeOut;
                this.targetProgress = 1.0 - Config.FADE_OUT_EASING.get().compute((float) t);
            } else {
                this.targetProgress = 0.0;
            }
        }

        private void updateProgress() {
            var speed = 12.0;
            this.progress += (targetProgress - progress) * (1.0 - Math.exp(-speed * delta));
        }

        private void updatePulse(long currentTime) {
            if (pulseStart >= 0) {
                var elapsed = (currentTime - pulseStart) / 1000.0;

                if (elapsed < Config.PULSE_TIME.get()) {
                    this.pulse = elapsed / Config.PULSE_TIME.get();
                } else {
                    this.pulse = 0.0;
                    this.pulseStart = -1L;
                }
            }
        }

        private long getDisplayTime() {
            var fadeIn = Config.FADE_IN_TIME.get();
            var fadeOut = Config.FADE_OUT_TIME.get();
            var lifetime = Config.DISPLAY_TIME.get();
            return (long) ((fadeIn + lifetime + fadeOut) * 1000.0);
        }

        private LayoutResult measureLayout() {
            return ConfigCache.layout.measure(event, style, ConfigCache.anchor.isMirrored());
        }
    }
}