package dev.obscuria.lootjournal.client.themes.styles.banners;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.obscuria.lootjournal.client.renderer.PickupRenderer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;

public record TextureBanner(
        Identifier texture,
        int textureWidth,
        int textureHeight,
        int uOffset,
        int vOffset,
        int uWidth,
        int vHeight,
        int pivotX,
        int pivotY
) implements PickupBanner {

    public static final MapCodec<TextureBanner> MAP_CODEC = RecordCodecBuilder.mapCodec(codec -> codec.group(
            Identifier.CODEC.fieldOf("texture").forGetter(TextureBanner::texture),
            Codec.INT.fieldOf("texture_width").forGetter(TextureBanner::textureWidth),
            Codec.INT.fieldOf("texture_height").forGetter(TextureBanner::textureHeight),
            Codec.INT.fieldOf("u_offset").forGetter(TextureBanner::uOffset),
            Codec.INT.fieldOf("v_offset").forGetter(TextureBanner::vOffset),
            Codec.INT.fieldOf("u_width").forGetter(TextureBanner::uWidth),
            Codec.INT.fieldOf("v_height").forGetter(TextureBanner::vHeight),
            Codec.INT.fieldOf("pivot_x").forGetter(TextureBanner::pivotX),
            Codec.INT.fieldOf("pivot_y").forGetter(TextureBanner::pivotY)
    ).apply(codec, TextureBanner::new));

    public static final Codec<TextureBanner> CODEC = MAP_CODEC.codec();

    @Override
    public MapCodec<? extends PickupBanner> codec() {
        return MAP_CODEC;
    }

    @Override
    public void render(GuiGraphics graphics, PickupRenderer pickup) {

        int x;
        int u;

        if (pickup.isMirrored()) {
            x = -(uWidth - pivotX);
            u = uOffset;
        } else {
            x = -pivotX;
            u = uOffset;
        }

        int y = -pivotY;

        if (pickup.isMirrored()) {
            graphics.pose().pushMatrix();
            graphics.pose().translate((float) (x + uWidth), (float) y);
            graphics.pose().scale(-1f, 1f);
            graphics.blit(
                    RenderPipelines.GUI_TEXTURED,
                    texture,
                    0,
                    0,
                    (float) u,
                    (float) vOffset,
                    uWidth,
                    vHeight,
                    uWidth,
                    vHeight,
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
                    (float) vOffset,
                    uWidth,
                    vHeight,
                    uWidth,
                    vHeight,
                    textureWidth,
                    textureHeight,
                    pickup.modulatedWhite()
            );
        }
    }
}
