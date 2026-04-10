package dev.obscuria.lootjournal.client.themes.styles.panels;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.obscuria.lootjournal.client.renderer.PickupRenderer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public record NineSlicedPanel(
        ResourceLocation texture,
        int textureWidth,
        int textureHeight,
        int uOffset,
        int vOffset,
        int uWidth,
        int vHeight,
        int uSlice,
        int vSlice
) implements PickupPanel {

    public static final Codec<NineSlicedPanel> CODEC;

    @Override
    public Codec<NineSlicedPanel> codec() {
        return CODEC;
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

        blitSlice(graphics, 0, 0, left, top,
                mirrored ? uRight : uLeft, vTop,
                left, top, mirrored);

        blitSlice(graphics, targetWidth - right, 0, right, top,
                mirrored ? uLeft : uRight, vTop,
                right, top, mirrored);

        blitSlice(graphics, 0, targetHeight - bottom, left, bottom,
                mirrored ? uRight : uLeft, vBottom,
                left, bottom, mirrored);

        blitSlice(graphics, targetWidth - right, targetHeight - bottom, right, bottom,
                mirrored ? uLeft : uRight, vBottom,
                right, bottom, mirrored);

        blitSlice(graphics, left, 0, centerW, top,
                uCenter, vTop,
                texCenterW, top, mirrored);

        blitSlice(graphics, left, targetHeight - bottom, centerW, bottom,
                uCenter, vBottom,
                texCenterW, bottom, mirrored);

        blitSlice(graphics, 0, top, left, centerH,
                mirrored ? uRight : uLeft, vCenter,
                left, texCenterH, mirrored);

        blitSlice(graphics, targetWidth - right, top, right, centerH,
                mirrored ? uLeft : uRight, vCenter,
                right, texCenterH, mirrored);

        blitSlice(graphics, left, top, centerW, centerH,
                uCenter, vCenter,
                texCenterW, texCenterH, mirrored);
    }

    private void blitSlice(
            GuiGraphics graphics,
            int x, int y,
            int w, int h,
            int u, int v,
            int texW, int texH,
            boolean mirrored
    ) {
        if (mirrored) {
            u += texW;
            texW = -texW;
        }

        graphics.blit(texture, x, y, w, h,
                u, v, texW, texH,
                textureWidth, textureHeight);
    }

    static {
        CODEC = RecordCodecBuilder.create(codec -> codec.group(
                ResourceLocation.CODEC.fieldOf("texture").forGetter(NineSlicedPanel::texture),
                Codec.INT.fieldOf("texture_width").forGetter(NineSlicedPanel::textureWidth),
                Codec.INT.fieldOf("texture_height").forGetter(NineSlicedPanel::textureHeight),
                Codec.INT.fieldOf("u_offset").forGetter(NineSlicedPanel::uOffset),
                Codec.INT.fieldOf("v_offset").forGetter(NineSlicedPanel::vOffset),
                Codec.INT.fieldOf("u_width").forGetter(NineSlicedPanel::uWidth),
                Codec.INT.fieldOf("v_height").forGetter(NineSlicedPanel::vHeight),
                Codec.INT.fieldOf("u_slice").forGetter(NineSlicedPanel::uSlice),
                Codec.INT.fieldOf("v_slice").forGetter(NineSlicedPanel::vSlice)
        ).apply(codec, NineSlicedPanel::new));
    }
}
