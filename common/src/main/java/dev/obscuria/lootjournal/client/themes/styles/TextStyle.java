package dev.obscuria.lootjournal.client.themes.styles;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.obscuria.fragmentum.content.util.color.ARGB;
import dev.obscuria.fragmentum.content.util.color.Colors;
import dev.obscuria.lootjournal.client.themes.styles.vars.Var;
import net.minecraft.resources.Identifier;

import java.util.Optional;

public record TextStyle(
        Optional<Identifier> font,
        Var<ARGB> nameColor,
        Var<ARGB> pickupCountColor,
        Var<ARGB> totalCountColor,
        Var<Boolean> dropShadow,
        Var<Boolean> ignoreFormatting
) {

    public static final Codec<TextStyle> CODEC;
    public static final TextStyle DEFAULT;

    static {
        CODEC = RecordCodecBuilder.create(codec -> codec.group(
                Identifier.CODEC.optionalFieldOf("font").forGetter(TextStyle::font),
                Var.ARGB.fieldOf("name_color").forGetter(TextStyle::nameColor),
                Var.ARGB.fieldOf("pickup_count_color").forGetter(TextStyle::pickupCountColor),
                Var.ARGB.fieldOf("total_count_color").forGetter(TextStyle::totalCountColor),
                Var.BOOL.fieldOf("drop_shadow").forGetter(TextStyle::dropShadow),
                Var.BOOL.fieldOf("ignore_formatting").forGetter(TextStyle::ignoreFormatting)
        ).apply(codec, TextStyle::new));
        DEFAULT = new TextStyle(
                Optional.empty(),
                new Var.DirectVar<>(Colors.argbOf("#FFFFFFFF")),
                new Var.DirectVar<>(Colors.argbOf("#FFFFFFFF")),
                new Var.DirectVar<>(Colors.argbOf("#FFFFFFFF")),
                new Var.DirectVar<>(true),
                new Var.DirectVar<>(false));
    }
}
