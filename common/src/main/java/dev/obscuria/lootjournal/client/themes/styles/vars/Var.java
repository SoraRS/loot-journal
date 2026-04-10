package dev.obscuria.lootjournal.client.themes.styles.vars;

import com.mojang.serialization.Codec;
import dev.obscuria.fragmentum.util.color.ARGB;
import dev.obscuria.lootjournal.client.registry.ThemeRegistry;

public interface Var<T> {

    Codec<Var<Integer>> INT = VarType.INT.codec();
    Codec<Var<Float>> FLOAT = VarType.FLOAT.codec();
    Codec<Var<Boolean>> BOOL = VarType.BOOL.codec();
    Codec<Var<String>> STRING = VarType.STRING.codec();
    Codec<Var<ARGB>> ARGB = VarType.COLOR_ARGB.codec();

    T get();

    record DirectVar<T>(T value) implements Var<T> {

        @Override
        public T get() {
            return value;
        }
    }

    record ThemeVar<T>(VarType<T> type, String name) implements Var<T> {

        @Override
        public T get() {
            return type.cached(ThemeRegistry.activeTheme(), name);
        }
    }
}


