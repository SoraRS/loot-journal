package dev.obscuria.lootjournal.client.themes.styles.icons;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.obscuria.lootjournal.client.themes.styles.icons.effects.NoneEffect;
import dev.obscuria.lootjournal.client.themes.styles.vars.Var;
import dev.obscuria.lootjournal.client.renderer.PickupRenderer;
import dev.obscuria.lootjournal.client.themes.styles.icons.effects.IconEffect;
import net.minecraft.client.gui.GuiGraphics;

public record SimpleIcon(
        Var<Integer> xOffset,
        Var<Integer> yOffset,
        Var<Integer> paddingLeft,
        Var<Integer> paddingRight,
        IconEffect effect
) implements PickupIcon {

    public static final Codec<SimpleIcon> CODEC;
    public static final SimpleIcon DEFAULT;

    @Override
    public Codec<SimpleIcon> codec() {
        return CODEC;
    }

    @Override
    public void render(GuiGraphics graphics, PickupRenderer renderer) {
        effect.render(graphics, renderer);
        renderer.event().renderIcon(graphics, renderer);
    }

    static {
        CODEC = RecordCodecBuilder.create(codec -> codec.group(
                Var.INT.fieldOf("x_offset").forGetter(SimpleIcon::xOffset),
                Var.INT.fieldOf("y_offset").forGetter(SimpleIcon::yOffset),
                Var.INT.fieldOf("padding_left").forGetter(SimpleIcon::paddingLeft),
                Var.INT.fieldOf("padding_right").forGetter(SimpleIcon::paddingRight),
                IconEffect.CODEC.optionalFieldOf("effect", NoneEffect.SHARED).forGetter(SimpleIcon::effect)
        ).apply(codec, SimpleIcon::new));
        var zero = new Var.DirectVar<>(0);
        DEFAULT = new SimpleIcon(zero, zero, zero, zero, NoneEffect.SHARED);
    }
}

