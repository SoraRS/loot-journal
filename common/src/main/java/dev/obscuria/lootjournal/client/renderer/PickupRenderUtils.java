package dev.obscuria.lootjournal.client.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.obscuria.lootjournal.config.ConfigCache;
import net.minecraft.client.gui.GuiGraphics;
import org.jetbrains.annotations.Nullable;

public final class PickupRenderUtils {

    public static final int PICKUP_HEIGHT = 14;

    public static void render(GuiGraphics graphics, PickupRenderer renderer) {

        RenderSystem.enableBlend();
        renderer.pushModulate(renderer.progress());

        graphics.pose().pushPose();
        graphics.pose().translate(renderer.originOffset(), 0, 80);
        renderer.style().panel().render(graphics, renderer);
        graphics.pose().translate(renderer.paddingEdge(), renderer.paddingTop(), 0);
        @Nullable var iconPosition = renderer.layout().findFirst("ICON");
        if (iconPosition != null) {
            graphics.pose().pushPose();
            graphics.pose().translate(iconPosition.centerX(), PICKUP_HEIGHT * 0.5, 0);
            renderer.style().banner().render(graphics, renderer);
            graphics.pose().popPose();
        }
        ConfigCache.layout.render(graphics, renderer);
        graphics.pose().popPose();

        renderer.popModulate();
        RenderSystem.disableBlend();
    }
}
