package dev.obscuria.lootjournal.client.themes.styles.panels;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import dev.obscuria.lootjournal.client.renderer.PickupRenderer;
import net.minecraft.client.gui.GuiGraphics;

public record NonePanel() implements PickupPanel {

    public static final NonePanel DEFAULT = new NonePanel();

    public static final MapCodec<NonePanel> MAP_CODEC = MapCodec.unit(NonePanel::new);
    public static final Codec<NonePanel> CODEC = MAP_CODEC.codec();

    @Override
    public MapCodec<? extends PickupPanel> codec() {
        return MAP_CODEC;
    }

    @Override
    public void render(GuiGraphics graphics, PickupRenderer pickup) {
    }
}