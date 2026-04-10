package dev.obscuria.lootjournal.client.themes.match;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.obscuria.lootjournal.client.events.ItemPickupEvent;
import dev.obscuria.lootjournal.client.events.PickupEvent;

public record IsItemMatch(boolean value) implements PickupMatch {

    public static final String NAME = "is_item";
    public static final Codec<IsItemMatch> CODEC;

    @Override
    public Codec<IsItemMatch> codec() {
        return CODEC;
    }

    @Override
    public boolean matches(PickupEvent pickupEvent) {
        return pickupEvent instanceof ItemPickupEvent;
    }

    static {
        CODEC = RecordCodecBuilder.create(codec -> codec.group(
                Codec.BOOL.fieldOf(NAME).forGetter(IsItemMatch::value)
        ).apply(codec, IsItemMatch::new));
    }
}
