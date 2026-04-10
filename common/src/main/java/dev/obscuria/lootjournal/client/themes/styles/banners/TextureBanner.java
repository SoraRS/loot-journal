package dev.obscuria.lootjournal.client.themes.styles.banners;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.obscuria.lootjournal.client.renderer.PickupRenderer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public record TextureBanner(
        ResourceLocation texture,
        int textureWidth,
        int textureHeight,
        int uOffset,
        int vOffset,
        int uWidth,
        int vHeight,
        int pivotX,
        int pivotY
) implements PickupBanner {

    public static final Codec<TextureBanner> CODEC;

    @Override
    public Codec<TextureBanner> codec() {
        return CODEC;
    }

    @Override
    public void render(GuiGraphics graphics, PickupRenderer pickup) {

        int x;
        int u;
        int w;

        if (pickup.isMirrored()) {
            x = -(uWidth - pivotX);
            u = uOffset + uWidth;
            w = -uWidth;
        } else {
            x = -pivotX;
            u = uOffset;
            w = uWidth;
        }

        int y = -pivotY;
        graphics.blit(texture, x, y, uWidth, vHeight, u, vOffset, w, vHeight, textureWidth, textureHeight);
    }

    static {
        CODEC = RecordCodecBuilder.create(codec -> codec.group(
                ResourceLocation.CODEC.fieldOf("texture").forGetter(TextureBanner::texture),
                Codec.INT.fieldOf("texture_width").forGetter(TextureBanner::textureWidth),
                Codec.INT.fieldOf("texture_height").forGetter(TextureBanner::textureHeight),
                Codec.INT.fieldOf("u_offset").forGetter(TextureBanner::uOffset),
                Codec.INT.fieldOf("v_offset").forGetter(TextureBanner::vOffset),
                Codec.INT.fieldOf("u_width").forGetter(TextureBanner::uWidth),
                Codec.INT.fieldOf("v_height").forGetter(TextureBanner::vHeight),
                Codec.INT.fieldOf("pivot_x").forGetter(TextureBanner::pivotX),
                Codec.INT.fieldOf("pivot_y").forGetter(TextureBanner::pivotY)
        ).apply(codec, TextureBanner::new));
    }
}
