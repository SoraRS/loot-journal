package dev.obscuria.lootjournal.client.themes.styles.icons.effects;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.obscuria.fragmentum.content.util.color.ARGB;
import dev.obscuria.fragmentum.content.util.easing.Easing;
import dev.obscuria.lootjournal.LootJournal;
import dev.obscuria.lootjournal.client.renderer.PickupRenderer;
import dev.obscuria.lootjournal.client.themes.styles.vars.Var;
import dev.obscuria.lootjournal.config.Config;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;

public record RayGlowEffect(
        Var<ARGB> primaryColor,
        Var<ARGB> secondaryColor,
        Var<Float> scale
) implements IconEffect {

    public static final Identifier TEXTURE = LootJournal.identifier("textures/gui/effect_ray_glow.png");

    public static final MapCodec<RayGlowEffect> MAP_CODEC = RecordCodecBuilder.mapCodec(codec -> codec.group(
            Var.ARGB.fieldOf("primary_color").forGetter(RayGlowEffect::primaryColor),
            Var.ARGB.fieldOf("secondary_color").forGetter(RayGlowEffect::secondaryColor),
            Var.FLOAT.fieldOf("scale").forGetter(RayGlowEffect::scale)
    ).apply(codec, RayGlowEffect::new));

    public static final Codec<RayGlowEffect> CODEC = MAP_CODEC.codec();

    @Override
    public MapCodec<? extends IconEffect> codec() {
        return MAP_CODEC;
    }

    @Override
    public void render(GuiGraphics graphics, PickupRenderer pickup) {
        if (!Config.RAY_GLOW_ENABLED.get()) return;

        var primaryColor = this.primaryColor.get();
        var secondaryColor = this.secondaryColor.get();

        final var time = pickup.timeInSeconds();
        final var baseScale = Mth.clamp(Easing.EASE_OUT_CUBIC.compute(time / 0.5f), 0f, 1f) * 1.0f;
        final var animatedScale = baseScale + Math.max(0f, Easing.EASE_IN_CUBIC
                .mergeOut(Easing.EASE_OUT_CUBIC, 0.35f)
                .compute(time));

        graphics.pose().pushMatrix();
        graphics.pose().scale(scale.get(), scale.get());

        pickup.pushModulate(primaryColor);
        renderSegment(graphics, pickup, animatedScale, 0.5f, time);
        pickup.popModulate();

        pickup.pushModulate(primaryColor.lerp(secondaryColor, 0.5f));
        renderSegment(graphics, pickup, animatedScale * 0.75f, -0.33f, time);
        pickup.popModulate();

        pickup.pushModulate(secondaryColor);
        renderSegment(graphics, pickup, animatedScale * 0.5f, 0.25f, time);
        pickup.popModulate();

        graphics.pose().popMatrix();
    }

    private void renderSegment(GuiGraphics graphics, PickupRenderer pickup, float scale, float rotDelta, float timer) {
        graphics.pose().pushMatrix();
        graphics.pose().scale(scale, scale);
        graphics.pose().rotate(rotDelta * 3f + rotDelta * timer);

        graphics.blit(
            RenderPipelines.GUI_TEXTURED,
            TEXTURE,
            -32,
            -32,
            0f,
            0f,
            64,
            64,
            64,
            64,
            64,
            64,
            pickup.modulatedWhite()
        );

        graphics.pose().popMatrix();
    }
}
