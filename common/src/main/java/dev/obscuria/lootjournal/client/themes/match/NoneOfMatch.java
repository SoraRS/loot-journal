package dev.obscuria.lootjournal.client.themes.match;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.obscuria.lootjournal.client.events.PickupEvent;

import java.util.List;

public record NoneOfMatch(List<PickupMatch> terms) implements PickupMatch {

    public static final String NAME = "none_of";
    public static final Codec<NoneOfMatch> CODEC;

    @Override
    public Codec<NoneOfMatch> codec() {
        return CODEC;
    }

    @Override
    public boolean matches(PickupEvent pickupEvent) {
        for (var term : terms) {
            if (!term.matches(pickupEvent)) continue;
            return false;
        }
        return true;
    }

    static {
        CODEC = RecordCodecBuilder.create(codec -> codec.group(
                PickupMatch.CODEC.listOf().fieldOf(NAME).forGetter(NoneOfMatch::terms)
        ).apply(codec, NoneOfMatch::new));
    }
}
