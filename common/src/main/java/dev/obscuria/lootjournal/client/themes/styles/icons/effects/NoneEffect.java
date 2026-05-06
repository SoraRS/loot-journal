package dev.obscuria.lootjournal.client.themes.styles.icons.effects;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import dev.obscuria.lootjournal.client.renderer.PickupRenderer;
import net.minecraft.client.gui.GuiGraphics;

public record NoneEffect() implements IconEffect {

    public static final NoneEffect SHARED = new NoneEffect();

    public static final MapCodec<NoneEffect> MAP_CODEC = MapCodec.unit(SHARED);
    public static final Codec<NoneEffect> CODEC = MAP_CODEC.codec();

    @Override
    public MapCodec<? extends IconEffect> codec() {
        return MAP_CODEC;
    }

    @Override
    public void render(GuiGraphics graphics, PickupRenderer pickup) {
    }
}