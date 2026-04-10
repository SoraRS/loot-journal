package dev.obscuria.lootjournal.client.registry;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.*;

import java.util.function.Function;
import java.util.stream.Stream;

public final class SmartDispatchCodec<K, V> extends MapCodec<V> {

    private final Codec<K> keyCodec;
    private final Function<? super V, ? extends DataResult<? extends K>> type;
    private final Function<? super K, ? extends DataResult<? extends Decoder<? extends V>>> decoder;
    private final Function<? super V, ? extends DataResult<? extends Encoder<V>>> encoder;

    public static <K, V> Codec<V> create(
            Codec<K> keyCodec,
            Function<? super V, ? extends K> type,
            Function<? super K, ? extends Codec<? extends V>> codec) {
        return partialDispatch(keyCodec,
                type.andThen(DataResult::success),
                codec.andThen(DataResult::success));
    }

    public static <K, V> Codec<V> partialDispatch(
            Codec<K> keyCodec,
            Function<? super V, ? extends DataResult<? extends K>> type,
            Function<? super K, ? extends DataResult<? extends Codec<? extends V>>> codec
    ) {
        return new SmartDispatchCodec<>(keyCodec, type, codec).codec();
    }

    private SmartDispatchCodec(
            Codec<K> keyCodec,
            Function<? super V, ? extends DataResult<? extends K>> type,
            Function<? super K, ? extends DataResult<? extends Codec<? extends V>>> codec
    ) {
        this.keyCodec = keyCodec;
        this.type = type;
        this.decoder = codec;
        this.encoder = v -> getCodec(type, codec, v);
    }

    @Override
    public <T> DataResult<V> decode(DynamicOps<T> ops, MapLike<T> input) {
        var firstEntry = input.entries().findFirst();
        if (firstEntry.isEmpty()) {
            return DataResult.error(() -> "Empty object, cannot determine type");
        }

        var keyElement = firstEntry.get().getFirst();
        return keyCodec.decode(ops, keyElement).flatMap(typePair -> {
            var type = typePair.getFirst();
            var elementDecoder = decoder.apply(type);

            return elementDecoder.flatMap(dec -> {
                var value = firstEntry.get().getSecond();
                if (dec instanceof MapCodec.MapCodecCodec mapCodecCodec) {
                    return mapCodecCodec.codec().decode(ops, input).map(Function.identity());
                }
                return dec.decode(ops, value).map(Pair::getFirst);
            });
        });
    }

    @Override
    public <T> RecordBuilder<T> encode(V input, DynamicOps<T> ops, RecordBuilder<T> prefix) {

        DataResult<? extends Encoder<V>> elementEncoder = encoder.apply(input);

        if (elementEncoder.result().isEmpty()) {
            return prefix.withErrorsFrom(elementEncoder);
        }

        Encoder<V> enc = elementEncoder.result().get();

        DataResult<T> keyResult = encoder.apply(input).flatMap(
                e -> DataResult.error(() -> "Type extraction not implemented"));

        return keyResult.flatMap(key -> {
            DataResult<T> valueResult = enc.encodeStart(ops, input);

            return valueResult.map(value -> {
                prefix.add(key, value);
                return prefix;
            });
        }).result().orElse(prefix);
    }

    @Override
    public <T> Stream<T> keys(DynamicOps<T> ops) {
        return Stream.empty();
    }

    private static <K, V> DataResult<? extends Encoder<V>> getCodec(
            Function<? super V, ? extends DataResult<? extends K>> type,
            Function<? super K, ? extends DataResult<? extends Encoder<? extends V>>> encoder,
            V input) {
        return type.apply(input).flatMap((k) -> (DataResult) encoder.apply(k).map(Function.identity())).map((c) -> c);
    }
}
