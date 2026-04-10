package dev.obscuria.lootjournal.client.themes.match;

import com.mojang.serialization.Codec;
import dev.obscuria.lootjournal.client.events.PickupEvent;

public record RootMatch(PickupMatch match) {

    public static final Codec<RootMatch> CODEC;
    public static final RootMatch DEFAULT;

    public boolean matches(PickupEvent pickupEvent) {
        return match.matches(pickupEvent);
    }

    static {
        CODEC = PickupMatch.CODEC.xmap(RootMatch::new, RootMatch::match);
        DEFAULT = new RootMatch(new AlwaysMatch(true));
    }
}
