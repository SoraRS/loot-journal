package dev.obscuria.lootjournal.client.themes.styles.vars;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import dev.obscuria.fragmentum.util.color.ARGB;
import dev.obscuria.fragmentum.util.color.Colors;
import dev.obscuria.lootjournal.client.themes.BakedTheme;

import java.util.Locale;

public interface VarType<V> {

    VarType<Integer> INT = new VarType<>() {

        public <T> DataResult<Integer> read(DynamicOps<T> ops, T input) {
            return ops.getNumberValue(input).map(Number::intValue);
        }

        @Override
        public Integer bakeConfigValue(Object value) {
            return (Integer) value;
        }

        @Override
        public Integer defaultValue() {
            return 0;
        }

        @Override
        public Integer cached(BakedTheme theme, String name) {
            return theme.getCachedInt(name);
        }

        public String name() {
            return "int";
        }
    };

    VarType<Float> FLOAT = new VarType<>() {

        public <T> DataResult<Float> read(DynamicOps<T> ops, T input) {
            return ops.getNumberValue(input).map(Number::floatValue);
        }

        @Override
        public Float bakeConfigValue(Object value) {
            return (Float) value;
        }

        @Override
        public Float defaultValue() {
            return 0f;
        }

        @Override
        public Float cached(BakedTheme theme, String name) {
            return (float) theme.getCachedInt(name);
        }

        public String name() {
            return "float";
        }
    };

    VarType<Boolean> BOOL = new VarType<>() {

        public <T> DataResult<Boolean> read(DynamicOps<T> ops, T input) {
            return ops.getBooleanValue(input);
        }

        @Override
        public Boolean bakeConfigValue(Object value) {
            return (Boolean) value;
        }

        @Override
        public Boolean defaultValue() {
            return false;
        }

        @Override
        public Boolean cached(BakedTheme theme, String name) {
            return theme.getCachedBoolean(name);
        }

        public String name() {
            return "bool";
        }
    };

    VarType<String> STRING = new VarType<>() {

        public <T> DataResult<String> read(DynamicOps<T> ops, T input) {
            return ops.getStringValue(input);
        }

        @Override
        public String bakeConfigValue(Object value) {
            return (String) value;
        }

        @Override
        public String defaultValue() {
            return "null";
        }

        @Override
        public String cached(BakedTheme theme, String name) {
            return "";
        }

        public String name() {
            return "string";
        }
    };

    VarType<ARGB> COLOR_ARGB = new VarType<>() {

        private static final ARGB WHITE = Colors.argbOf("#ffffffff");

        @Override
        public <T> DataResult<ARGB> read(DynamicOps<T> ops, T input) {
            return ARGB.CODEC.parse(ops, input);
        }

        @Override
        public ARGB bakeConfigValue(Object value) {
            return Colors.argbOf(String.valueOf(value));
        }

        @Override
        public ARGB defaultValue() {
            return WHITE;
        }

        @Override
        public ARGB cached(BakedTheme theme, String name) {
            return theme.getCachedARGB(name);
        }

        @Override
        public String name() {
            return "modulate(argb)";
        }
    };

    static <E extends Enum<E>> VarType<E> enumType(Class<E> clazz) {

        return new VarType<>() {

            public <T> DataResult<E> read(DynamicOps<T> ops, T input) {
                return ops.getStringValue(input).flatMap(name -> {
                    try {
                        return DataResult.success(Enum.valueOf(clazz, name.toUpperCase(Locale.ROOT)));
                    } catch (Exception e) {
                        return DataResult.error(() -> "Invalid enum defaultValue: " + name);
                    }
                });
            }

            @Override
            public E bakeConfigValue(Object value) {
                return (E) value;
            }

            @Override
            public E defaultValue() {
                return null;
            }

            @Override
            public E cached(BakedTheme theme, String name) {
                return null;
            }

            public String name() {return "enum(" + clazz.getSimpleName() + ")";}
        };
    }

    <T> DataResult<V> read(DynamicOps<T> ops, T input);

    V bakeConfigValue(Object value);

    V defaultValue();

    V cached(BakedTheme theme, String name);

    String name();

    default VarCodec<V> codec() {
        return new VarCodec<>(this);
    }
}