package dev.obscuria.lootjournal.client.themes.styles.icons.effects;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.math.Axis;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.obscuria.fragmentum.util.color.ARGB;
import dev.obscuria.fragmentum.util.easing.Easing;
import dev.obscuria.lootjournal.LootJournal;
import dev.obscuria.lootjournal.client.themes.styles.vars.Var;
import dev.obscuria.lootjournal.client.renderer.PickupRenderer;
import dev.obscuria.lootjournal.config.Config;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public record RayGlowEffect(
        Var<ARGB> primaryColor,
        Var<ARGB> secondaryColor,
        Var<Float> scale
) implements IconEffect {

    public static final ResourceLocation TEXTURE;
    public static final Codec<RayGlowEffect> CODEC;

    @Override
    public Codec<RayGlowEffect> codec() {
        return CODEC;
    }

    @Override
    public void render(GuiGraphics graphics, PickupRenderer pickup) {

        if (!Config.RAY_GLOW_ENABLED.get()) return;

        var primaryColor = this.primaryColor.get();
        var secondaryColor = this.secondaryColor.get();

        final var time = pickup.timeInSeconds();
        final var baseScale = Mth.clamp(Easing.EASE_OUT_CUBIC.compute(time / 0.5f), 0f, 1f) * 2.0f;
        final var animatedScale = baseScale + Math.max(0f, Easing.EASE_IN_CUBIC.mergeOut(Easing.EASE_OUT_CUBIC, 0.35f).compute(time));

        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(
                GlStateManager.SourceFactor.SRC_ALPHA,
                GlStateManager.DestFactor.ONE,
                GlStateManager.SourceFactor.ONE,
                GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.disableDepthTest();
        RenderSystem.disableCull();

        graphics.pose().pushPose();
        graphics.pose().scale(scale.get(), scale.get(), 1);

        pickup.pushModulate(primaryColor);
        renderSegment(graphics, animatedScale, 0.5f, time);
        pickup.popModulate();

        pickup.pushModulate(primaryColor.lerp(secondaryColor, 0.5f));
        renderSegment(graphics, animatedScale * 0.75f, -0.33f, time);
        pickup.popModulate();

        pickup.pushModulate(secondaryColor);
        renderSegment(graphics, animatedScale * 0.5f, 0.25f, time);
        pickup.popModulate();

        graphics.pose().popPose();

        RenderSystem.enableCull();
        RenderSystem.enableDepthTest();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();
    }

    private void renderSegment(GuiGraphics graphics, float scale, float rotDelta, float timer) {
        graphics.pose().pushPose();
        graphics.pose().scale(scale, scale, scale);
        graphics.pose().mulPose(Axis.ZP.rotation(rotDelta * 3f + rotDelta * timer));
        graphics.blit(TEXTURE, -32, -32, 0f, 0f, 64, 64, 64, 64);
        graphics.pose().popPose();
    }

    static {
        TEXTURE = LootJournal.identifier("textures/gui/effect_ray_glow.png");
        CODEC = RecordCodecBuilder.create(codec -> codec.group(
                Var.ARGB.fieldOf("primary_color").forGetter(RayGlowEffect::primaryColor),
                Var.ARGB.fieldOf("secondary_color").forGetter(RayGlowEffect::secondaryColor),
                Var.FLOAT.fieldOf("scale").forGetter(RayGlowEffect::scale)
        ).apply(codec, RayGlowEffect::new));
    }
}
