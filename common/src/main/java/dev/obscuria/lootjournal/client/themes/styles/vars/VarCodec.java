package dev.obscuria.lootjournal.client.themes.styles.vars;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;

public record VarCodec<A>(VarType<A> type) implements Codec<Var<A>> {

    @Override
    public <T> DataResult<Pair<Var<A>, T>> decode(DynamicOps<T> ops, T input) {
        var variable = ops.getStringValue(input).flatMap(this::asVariable);
        if (variable.result().isPresent()) return variable.map(name -> Pair.of(new Var.ThemeVar<>(type, name), input));
        return type.read(ops, input).map(value -> Pair.of(new Var.DirectVar<>(value), input));
    }

    @Override
    public <T> DataResult<T> encode(Var<A> input, DynamicOps<T> ops, T prefix) {
        throw new UnsupportedOperationException();
    }

    private DataResult<String> asVariable(String value) {
        return value.startsWith("var(") && value.endsWith(")")
                ? DataResult.success(value.substring(4, value.length() - 1))
                : DataResult.error(() -> "Invalid variable: " + value);
    }
}
