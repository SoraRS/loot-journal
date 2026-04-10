package dev.obscuria.lootjournal.client.themes.match;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.obscuria.lootjournal.client.events.PickupEvent;

public record AlwaysMatch(boolean value) implements PickupMatch {

    public static final String NAME = "always";
    public static final Codec<AlwaysMatch> CODEC;

    @Override
    public Codec<AlwaysMatch> codec() {
        return CODEC;
    }

    @Override
    public boolean matches(PickupEvent pickupEvent) {
        return value;
    }

    static {
        CODEC = RecordCodecBuilder.create(codec -> codec.group(
                Codec.BOOL.fieldOf(NAME).forGetter(AlwaysMatch::value)
        ).apply(codec, AlwaysMatch::new));
    }
}
