package dev.obscuria.lootjournal.client.themes.styles.panels;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.obscuria.lootjournal.client.renderer.PickupRenderer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;

public record NineSlicedPanel(
        Identifier texture,
        int textureWidth,
        int textureHeight,
        int uOffset,
        int vOffset,
        int uWidth,
        int vHeight,
        int uSlice,
        int vSlice
) implements PickupPanel {

        public static final MapCodec<NineSlicedPanel> MAP_CODEC = RecordCodecBuilder.mapCodec(codec -> codec.group(
                Identifier.CODEC.fieldOf("texture").forGetter(NineSlicedPanel::texture),
                Codec.INT.fieldOf("texture_width").forGetter(NineSlicedPanel::textureWidth),
                Codec.INT.fieldOf("texture_height").forGetter(NineSlicedPanel::textureHeight),
                Codec.INT.fieldOf("u_offset").forGetter(NineSlicedPanel::uOffset),
                Codec.INT.fieldOf("v_offset").forGetter(NineSlicedPanel::vOffset),
                Codec.INT.fieldOf("u_width").forGetter(NineSlicedPanel::uWidth),
                Codec.INT.fieldOf("v_height").forGetter(NineSlicedPanel::vHeight),
                Codec.INT.fieldOf("u_slice").forGetter(NineSlicedPanel::uSlice),
                Codec.INT.fieldOf("v_slice").forGetter(NineSlicedPanel::vSlice)
        ).apply(codec, NineSlicedPanel::new));

        public static final Codec<NineSlicedPanel> CODEC = MAP_CODEC.codec();

        @Override
        public MapCodec<? extends PickupPanel> codec() {
                return MAP_CODEC;
        }

        @Override
        public void render(GuiGraphics graphics, PickupRenderer pickup) {

        var targetWidth = pickup.width();
        var targetHeight = pickup.height();
        var mirrored = pickup.isMirrored();

        int left = uSlice;
        int right = uSlice;
        int top = vSlice;
        int bottom = vSlice;

        int centerW = targetWidth - left - right;
        int centerH = targetHeight - top - bottom;

        int texCenterW = uWidth - left - right;
        int texCenterH = vHeight - top - bottom;

        int uLeft = uOffset;
        int uRight = uOffset + uWidth - right;
        int uCenter = uOffset + left;

        int vTop = vOffset;
        int vBottom = vOffset + vHeight - bottom;
        int vCenter = vOffset + top;

        blitSlice(pickup, graphics, 0, 0, left, top,
                mirrored ? uRight : uLeft, vTop,
                left, top, mirrored);

        blitSlice(pickup, graphics, targetWidth - right, 0, right, top,
                mirrored ? uLeft : uRight, vTop,
                right, top, mirrored);

        blitSlice(pickup, graphics, 0, targetHeight - bottom, left, bottom,
                mirrored ? uRight : uLeft, vBottom,
                left, bottom, mirrored);

        blitSlice(pickup, graphics, targetWidth - right, targetHeight - bottom, right, bottom,
                mirrored ? uLeft : uRight, vBottom,
                right, bottom, mirrored);

        blitSlice(pickup, graphics, left, 0, centerW, top,
                uCenter, vTop,
                texCenterW, top, mirrored);

        blitSlice(pickup, graphics, left, targetHeight - bottom, centerW, bottom,
                uCenter, vBottom,
                texCenterW, bottom, mirrored);

        blitSlice(pickup, graphics, 0, top, left, centerH,
                mirrored ? uRight : uLeft, vCenter,
                left, texCenterH, mirrored);

        blitSlice(pickup, graphics, targetWidth - right, top, right, centerH,
                mirrored ? uLeft : uRight, vCenter,
                right, texCenterH, mirrored);

        blitSlice(pickup, graphics, left, top, centerW, centerH,
                uCenter, vCenter,
                texCenterW, texCenterH, mirrored);
        }

        private void blitSlice(
                PickupRenderer pickup,
                GuiGraphics graphics,
                int x, int y,
                int w, int h,
                int u, int v,
                int texW, int texH,
                boolean mirrored
        ) {
        if (w <= 0 || h <= 0 || texW <= 0 || texH <= 0) return;

        if (mirrored) {
                graphics.pose().pushMatrix();
                graphics.pose().translate((float) (x + w), (float) y);
                graphics.pose().scale(-1f, 1f);
                graphics.blit(
                        RenderPipelines.GUI_TEXTURED,
                        texture,
                        0,
                        0,
                        (float) u,
                        (float) v,
                        w,
                        h,
                        texW,
                        texH,
                        textureWidth,
                        textureHeight,
                        pickup.modulatedWhite()
                );
                graphics.pose().popMatrix();
        } else {
                graphics.blit(
                        RenderPipelines.GUI_TEXTURED,
                        texture,
                        x,
                        y,
                        (float) u,
                        (float) v,
                        w,
                        h,
                        texW,
                        texH,
                        textureWidth,
                        textureHeight,
                        pickup.modulatedWhite()
                        );
                }
        }
}
