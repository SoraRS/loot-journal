package dev.obscuria.lootjournal.client.themes.styles.panels;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.obscuria.fragmentum.util.color.ARGB;
import dev.obscuria.lootjournal.client.themes.styles.vars.Var;
import dev.obscuria.lootjournal.client.renderer.PickupRenderer;
import net.minecraft.client.gui.GuiGraphics;

public record FillPanel(
        Var<ARGB> color
) implements PickupPanel {

    public static final Codec<FillPanel> CODEC;

    @Override
    public Codec<FillPanel> codec() {
        return CODEC;
    }

    @Override
    public void render(GuiGraphics graphics, PickupRenderer pickup) {
        graphics.fill(0, 0, pickup.width(), pickup.height(), color.get().decimal());
    }

    static {
        CODEC = RecordCodecBuilder.create(codec -> codec.group(
                Var.ARGB.fieldOf("color").forGetter(FillPanel::color)
        ).apply(codec, FillPanel::new));
    }
}
