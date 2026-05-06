package dev.obscuria.lootjournal.client.themes.styles.icons.effects;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import dev.obscuria.fragmentum.content.registry.BootstrapContext;
import dev.obscuria.lootjournal.client.registry.LootJournalRegistries;
import dev.obscuria.lootjournal.client.renderer.PickupRenderer;
import net.minecraft.client.gui.GuiGraphics;

import java.util.function.Function;

public interface IconEffect {

    Codec<IconEffect> CODEC = LootJournalRegistries
            .ICON_EFFECT_TYPE.byNameCodec()
            .dispatch(IconEffect::codec, Function.identity());

    MapCodec<? extends IconEffect> codec();

    void render(GuiGraphics graphics, PickupRenderer pickup);

    static void bootstrap(BootstrapContext<MapCodec<? extends IconEffect>> context) {
        context.register("none", () -> NoneEffect.MAP_CODEC);
        context.register("ray_glow", () -> RayGlowEffect.MAP_CODEC);
    }
}