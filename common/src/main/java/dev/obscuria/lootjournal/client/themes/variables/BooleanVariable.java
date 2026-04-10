package dev.obscuria.lootjournal.client.themes.variables;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.isxander.yacl3.api.Binding;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import dev.obscuria.lootjournal.client.themes.BakedTheme;

public record BooleanVariable(
        Boolean defaultValue,
        String key,
        String displayName,
        String description
) implements Variable<Boolean> {

    public static final Codec<BooleanVariable> CODEC;

    @Override
    public Codec<BooleanVariable> codec() {
        return CODEC;
    }

    @Override
    public Boolean actualValue(BakedTheme theme) {
        return theme.overrides.getBoolean(key).orElse(defaultValue);
    }

    @Override
    public Option<?> createOption(BakedTheme theme) {
        return Variable.<Boolean>createOption(this)
                .binding(Binding.generic(defaultValue,
                        () -> theme.getAsBoolean(key),
                        value -> theme.overrides.setBoolean(key, value)))
                .controller(TickBoxControllerBuilder::create)
                .build();
    }

    static {
        CODEC = RecordCodecBuilder.create(codec -> codec.group(
                Codec.BOOL.fieldOf("default").forGetter(BooleanVariable::defaultValue)
        ).and(Variable.baseFields(codec)).apply(codec, BooleanVariable::new));
    }
}
