package dev.obscuria.lootjournal.client.themes.styles.icons.effects;

import com.mojang.serialization.Codec;
import dev.obscuria.lootjournal.client.renderer.PickupRenderer;
import net.minecraft.client.gui.GuiGraphics;

public record NoneEffect() implements IconEffect {

    public static final NoneEffect SHARED = new NoneEffect();
    public static final Codec<NoneEffect> CODEC = Codec.unit(SHARED);

    @Override
    public Codec<NoneEffect> codec() {
        return CODEC;
    }

    @Override
    public void render(GuiGraphics graphics, PickupRenderer pickup) {}
}
