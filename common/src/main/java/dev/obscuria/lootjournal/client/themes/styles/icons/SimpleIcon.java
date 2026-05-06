package dev.obscuria.lootjournal.client.themes.styles.icons;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.obscuria.lootjournal.client.renderer.PickupRenderer;
import dev.obscuria.lootjournal.client.themes.styles.icons.effects.IconEffect;
import dev.obscuria.lootjournal.client.themes.styles.icons.effects.NoneEffect;
import dev.obscuria.lootjournal.client.themes.styles.vars.Var;
import net.minecraft.client.gui.GuiGraphics;

public record SimpleIcon(
        Var<Integer> xOffset,
        Var<Integer> yOffset,
        Var<Integer> paddingLeft,
        Var<Integer> paddingRight,
        IconEffect effect
) implements PickupIcon {

    public static final MapCodec<SimpleIcon> MAP_CODEC = RecordCodecBuilder.mapCodec(codec -> codec.group(
            Var.INT.fieldOf("x_offset").forGetter(SimpleIcon::xOffset),
            Var.INT.fieldOf("y_offset").forGetter(SimpleIcon::yOffset),
            Var.INT.fieldOf("padding_left").forGetter(SimpleIcon::paddingLeft),
            Var.INT.fieldOf("padding_right").forGetter(SimpleIcon::paddingRight),
            IconEffect.CODEC.optionalFieldOf("effect", NoneEffect.SHARED).forGetter(SimpleIcon::effect)
    ).apply(codec, SimpleIcon::new));

    public static final Codec<SimpleIcon> CODEC = MAP_CODEC.codec();

    public static final SimpleIcon DEFAULT;

    static {
        var zero = new Var.DirectVar<>(0);
        DEFAULT = new SimpleIcon(zero, zero, zero, zero, NoneEffect.SHARED);
    }

    @Override
    public MapCodec<? extends PickupIcon> codec() {
        return MAP_CODEC;
    }

    @Override
    public void render(GuiGraphics graphics, PickupRenderer renderer) {
        effect.render(graphics, renderer);
        renderer.event().renderIcon(graphics, renderer);
    }
}