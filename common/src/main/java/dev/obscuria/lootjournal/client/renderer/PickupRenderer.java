package dev.obscuria.lootjournal.client.renderer;

import dev.obscuria.fragmentum.content.util.color.ARGB;
import dev.obscuria.fragmentum.content.util.color.Colors;
import dev.obscuria.lootjournal.client.events.PickupEvent;
import dev.obscuria.lootjournal.client.renderer.layout.LayoutResult;
import dev.obscuria.lootjournal.client.renderer.layout.PickupLayout;
import dev.obscuria.lootjournal.client.themes.BakedTheme;
import dev.obscuria.lootjournal.client.themes.styles.PickupStyle;
import dev.obscuria.lootjournal.config.ConfigCache;
import net.minecraft.util.Util;

import java.util.ArrayDeque;
import java.util.Deque;

public final class PickupRenderer {

    private static final ARGB WHITE = Colors.argbOf(1, 1, 1, 1);
    private final Deque<ARGB> colorStack = new ArrayDeque<>();
    private final PickupComponent.PickupContainer container;
    private final float progress;
    private final float pulse;
    private ARGB modulate = WHITE;

    public PickupRenderer(PickupComponent.PickupContainer container, float progress, float pulse) {
        this.container = container;
        this.progress = progress;
        this.pulse = pulse;
    }

    public PickupEvent event() {
        return container.event;
    }

    public BakedTheme theme() {
        return container.theme;
    }

    public PickupStyle style() {
        return container.style;
    }

    public LayoutResult layout() {
        return container.layout;
    }

    public float progress() {
        return progress;
    }

    public float pulse() {
        return pulse;
    }

    public boolean isMirrored() {
        return ConfigCache.anchor.isMirrored();
    }

    public LayoutResult measureLayout(PickupLayout layout) {
        return layout.measure(event(), style(), isMirrored());
    }

    public int width() {
        return layout().width() + theme().entryPaddingLeft() + theme().entryPaddingRight();
    }

    public int height() {
        return layout().height() + theme().entryPaddingTop() + theme().entryPaddingBottom();
    }

    public int paddingLeft() {
        return theme().entryPaddingLeft();
    }

    public int paddingRight() {
        return theme().entryPaddingRight();
    }

    public int paddingTop() {
        return theme().entryPaddingTop();
    }

    public int paddingBottom() {
        return theme().entryPaddingBottom();
    }

    public int paddingEdge() {
        return isMirrored() ? theme().entryPaddingRight() : theme().entryPaddingLeft();
    }

    public double originOffset() {
        final var offset = -220.0 + 220.0 * (1 - Math.pow(progress - 1, 2));
        return (isMirrored() ? offset : -offset) - (isMirrored() ? 0 : width());
    }

    public void pushModulate(float a) {
        pushModulate(a, 1f, 1f, 1f);
    }

    public void pushModulate(float r, float g, float b) {
        pushModulate(1f, r, g, b);
    }

    public void pushModulate(float a, float r, float g, float b) {
        pushModulate(Colors.argbOf(a, r, g, b));
    }

    public void pushModulate(ARGB modulate) {
        this.colorStack.push(this.modulate);
        this.modulate = Colors.argbOf(
                this.modulate.alpha() * modulate.alpha(),
                this.modulate.red() * modulate.red(),
                this.modulate.green() * modulate.green(),
                this.modulate.blue() * modulate.blue());
    }

    public void popModulate() {
        if (colorStack.isEmpty()) {
            throw new IllegalStateException("Color stack underflow");
        }
        modulate = colorStack.pop();
    }

    public int modulatedWhite() {
        return modulateColor(0xFFFFFFFF);
    }

    public int modulateColor(ARGB color) {
        return modulateColor(color.decimal());
    }

    public int modulateColor(int color) {
        int a = (color >>> 24) & 0xFF;
        int r = (color >>> 16) & 0xFF;
        int g = (color >>> 8) & 0xFF;
        int b = color & 0xFF;

        int ma = Math.round(a * modulate.alpha());
        int mr = Math.round(r * modulate.red());
        int mg = Math.round(g * modulate.green());
        int mb = Math.round(b * modulate.blue());

        ma = Math.max(0, Math.min(255, ma));
        mr = Math.max(0, Math.min(255, mr));
        mg = Math.max(0, Math.min(255, mg));
        mb = Math.max(0, Math.min(255, mb));

        return (ma << 24) | (mr << 16) | (mg << 8) | mb;
    }

    public float timeInSeconds() {
        return (Util.getMillis() - container.startTime) * 0.001f;
    }
}
