package dev.obscuria.lootjournal.client.themes.variables;

import com.mojang.serialization.MapCodec;
import com.mojang.datafixers.Products;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.isxander.yacl3.api.Option;
import dev.obscuria.fragmentum.content.registry.BootstrapContext;
import dev.obscuria.lootjournal.client.registry.LootJournalRegistries;
import dev.obscuria.lootjournal.client.themes.BakedTheme;
import dev.obscuria.lootjournal.config.ConfigBuilder;
import net.minecraft.network.chat.Component;
import java.util.function.Function;

public interface Variable<T> {

    Codec<Variable<?>> CODEC = LootJournalRegistries
            .THEME_VARIABLE_TYPE.byNameCodec()
            .dispatch(Variable::codec, Function.identity());

    MapCodec<? extends Variable<?>> codec();

    String key();

    String displayName();

    String description();

    T defaultValue();

    T actualValue(BakedTheme theme);

    Option<?> createOption(BakedTheme theme);

    static <V extends Variable<?>> Products.P3<RecordCodecBuilder.Mu<V>, String, String, String> baseFields(
            RecordCodecBuilder.Instance<V> instance
    ) {
        return instance.group(
                Codec.STRING.fieldOf("key").forGetter(V::key),
                Codec.STRING.fieldOf("display_name").forGetter(V::displayName),
                Codec.STRING.fieldOf("description").forGetter(V::description));
    }

    static <T> Option.Builder<T> createOption(Variable<?> variable) {
        return Option.<T>createBuilder()
                .name(Component.translatable(variable.displayName()))
                .description(ConfigBuilder.Opts.description(Component.translatable(variable.description())));
    }

    static void bootstrap(BootstrapContext<MapCodec<? extends Variable<?>>> context) {
        context.register("boolean", () -> BooleanVariable.MAP_CODEC);
        context.register("color", () -> ColorVariable.MAP_CODEC);
    }
}
