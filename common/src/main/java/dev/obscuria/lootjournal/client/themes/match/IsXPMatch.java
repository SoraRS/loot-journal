package dev.obscuria.lootjournal.client.themes.match;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.obscuria.lootjournal.client.events.PickupEvent;
import dev.obscuria.lootjournal.client.events.XpPickupEvent;

public record IsXPMatch(boolean value) implements PickupMatch {

    public static final String NAME = "is_xp";
    public static final Codec<IsXPMatch> CODEC;

    @Override
    public Codec<IsXPMatch> codec() {
        return CODEC;
    }

    @Override
    public boolean matches(PickupEvent pickupEvent) {
        return pickupEvent instanceof XpPickupEvent;
    }

    static {
        CODEC = RecordCodecBuilder.create(codec -> codec.group(
                Codec.BOOL.fieldOf(NAME).forGetter(IsXPMatch::value)
        ).apply(codec, IsXPMatch::new));
    }
}
