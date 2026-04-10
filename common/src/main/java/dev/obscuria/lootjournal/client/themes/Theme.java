package dev.obscuria.lootjournal.client.themes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.obscuria.lootjournal.client.themes.variables.Variable;

import java.util.List;

public record Theme(
        String displayName,
        String description,
        ThemeLayout layout,
        List<Variable<?>> variables,
        List<ThemeBinding> bindings
) {

    public static final Codec<Theme> DIRECT_CODEC;
    public static final Theme DEFAULT;

    public BakedTheme bake() {
        return new BakedTheme(this);
    }

    static {
        DIRECT_CODEC = RecordCodecBuilder.create(codec -> codec.group(
                Codec.STRING.fieldOf("display_name").forGetter(Theme::displayName),
                Codec.STRING.fieldOf("description").forGetter(Theme::description),
                ThemeLayout.CODEC.fieldOf("layout").forGetter(Theme::layout),
                Variable.CODEC.listOf().fieldOf("variables").forGetter(Theme::variables),
                ThemeBinding.CODEC.listOf().fieldOf("bindings").forGetter(Theme::bindings)
        ).apply(codec, Theme::new));
        DEFAULT = new Theme(
                "Fallback Theme",
                "Fallback theme in use due to an error.",
                ThemeLayout.DEFAULT, List.of(), List.of(ThemeBinding.DEFAULT));
    }
}
