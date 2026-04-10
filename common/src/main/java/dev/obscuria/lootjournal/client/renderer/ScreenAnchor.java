package dev.obscuria.lootjournal.client.renderer;

import com.mojang.blaze3d.platform.Window;
import dev.obscuria.lootjournal.client.registry.ThemeRegistry;
import dev.obscuria.lootjournal.config.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

import java.util.function.BiFunction;

@SuppressWarnings("unused")
public enum ScreenAnchor {
    TOP_LEFT(GrowthDirection.DOWN,
            (window, offset) -> offset,
            (window, offset) -> offset,
            true, false),
    TOP_RIGHT(GrowthDirection.DOWN,
            (window, offset) -> window.getGuiScaledWidth() - offset,
            (window, offset) -> offset,
            false, false),
    BOTTOM_LEFT(GrowthDirection.UP,
            (window, offset) -> offset,
            (window, offset) -> window.getGuiScaledHeight() - offset - entryHeight(),
            true, true),
    BOTTOM_RIGHT(GrowthDirection.UP,
            ((window, offset) -> window.getGuiScaledWidth() - offset),
            (window, offset) -> window.getGuiScaledHeight() - offset - entryHeight(),
            false, true);

    private final GrowthDirection direction;
    private final BiFunction<Window, Integer, Integer> xFunc;
    private final BiFunction<Window, Integer, Integer> yFunc;
    private final boolean mirrored;
    private final boolean bottom;

    ScreenAnchor(GrowthDirection direction,
                 BiFunction<Window, Integer, Integer> xFunc,
                 BiFunction<Window, Integer, Integer> yFunc,
                 boolean mirrored,
                 boolean bottom) {

        this.direction = direction;
        this.xFunc = xFunc;
        this.yFunc = yFunc;
        this.mirrored = mirrored;
        this.bottom = bottom;
    }

    public void transform(GuiGraphics graphics) {
        var window = Minecraft.getInstance().getWindow();
        graphics.pose().translate(computeX(window), computeY(window), 0);
        var scale = Config.SCALE.get().floatValue();
        graphics.pose().scale(scale, scale, scale);
    }

    public int computeX(Window window) {
        return xFunc.apply(window, ThemeRegistry.activeTheme().screenMargin());
    }

    public int computeY(Window window) {
        return yFunc.apply(window, Config.ANCHOR_Y_OFFSET.get());
    }

    public GrowthDirection direction() {
        return direction;
    }

    public boolean isMirrored() {
        return mirrored;
    }

    public boolean isBottom() {
        return bottom;
    }

    private static int entryHeight() {
        return ThemeRegistry.activeTheme().entryHeight();
    }
}
