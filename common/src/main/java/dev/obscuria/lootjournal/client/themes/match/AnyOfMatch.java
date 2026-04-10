package dev.obscuria.lootjournal.client.themes.match;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.obscuria.lootjournal.client.events.PickupEvent;

import java.util.List;

public record AnyOfMatch(List<PickupMatch> terms) implements PickupMatch {

    public static final String NAME = "any_of";
    public static final Codec<AnyOfMatch> CODEC;

    @Override
    public Codec<AnyOfMatch> codec() {
        return CODEC;
    }

    @Override
    public boolean matches(PickupEvent pickupEvent) {
        for (var term : terms) {
            if (!term.matches(pickupEvent)) continue;
            return true;
        }
        return false;
    }

    static {
        CODEC = RecordCodecBuilder.create(codec -> codec.group(
                PickupMatch.CODEC.listOf().fieldOf(NAME).forGetter(AnyOfMatch::terms)
        ).apply(codec, AnyOfMatch::new));
    }
}
