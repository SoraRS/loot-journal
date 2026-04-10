package dev.obscuria.lootjournal.client.themes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record ThemeLayout(
        int screenMargin,
        int paddingLeft,
        int paddingRight,
        int paddingTop,
        int paddingBottom
) {

    public static final Codec<ThemeLayout> CODEC;
    public static final ThemeLayout DEFAULT;

    static {
        CODEC = RecordCodecBuilder.create(codec -> codec.group(
                Codec.INT.fieldOf("screen_margin").forGetter(ThemeLayout::screenMargin),
                Codec.INT.fieldOf("padding_left").forGetter(ThemeLayout::paddingLeft),
                Codec.INT.fieldOf("padding_right").forGetter(ThemeLayout::paddingRight),
                Codec.INT.fieldOf("padding_top").forGetter(ThemeLayout::paddingTop),
                Codec.INT.fieldOf("padding_bottom").forGetter(ThemeLayout::paddingBottom)
        ).apply(codec, ThemeLayout::new));
        DEFAULT = new ThemeLayout(0, 0, 0, 0, 0);
    }
}
