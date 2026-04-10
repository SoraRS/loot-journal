package dev.obscuria.lootjournal.client.themes.variables;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.isxander.yacl3.api.Binding;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.controller.ColorControllerBuilder;
import dev.obscuria.fragmentum.util.color.ARGB;
import dev.obscuria.fragmentum.util.color.Colors;
import dev.obscuria.lootjournal.client.themes.BakedTheme;

import java.awt.*;

public record ColorVariable(
        ARGB defaultValue,
        String key,
        String displayName,
        String description
) implements Variable<ARGB> {

    public static final Codec<ColorVariable> CODEC;

    @Override
    public Codec<ColorVariable> codec() {
        return CODEC;
    }

    @Override
    public ARGB actualValue(BakedTheme theme) {
        return theme.overrides.getString(key).map(Colors::argbOf).orElse(defaultValue);
    }

    @Override
    public Option<?> createOption(BakedTheme theme) {
        return Variable.<Color>createOption(this)
                .binding(Binding.generic(argbToColor(defaultValue),
                        () -> argbToColor(theme.getAsARGB(key)),
                        value -> theme.overrides.setString(key, colorToARGB(value).hexadecimal())))
                .controller(it -> ColorControllerBuilder.create(it).allowAlpha(true))
                .build();
    }

    private static Color argbToColor(ARGB argb) {
        return new Color(argb.red(), argb.green(), argb.blue(), argb.alpha());
    }

    private static ARGB colorToARGB(Color color) {
        return Colors.argbOf(
                color.getAlpha() / 255f,
                color.getRed() / 255f,
                color.getGreen() / 255f,
                color.getBlue() / 255f);
    }

    static {
        CODEC = RecordCodecBuilder.create(codec -> codec.group(
                ARGB.CODEC.fieldOf("default").forGetter(ColorVariable::defaultValue)
        ).and(Variable.baseFields(codec)).apply(codec, ColorVariable::new));
    }
}
