package dev.obscuria.lootjournal.client.themes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.obscuria.lootjournal.client.events.PickupEvent;
import dev.obscuria.lootjournal.client.themes.match.RootMatch;
import dev.obscuria.lootjournal.client.themes.styles.PickupStyle;

public record ThemeBinding(
        int priority,
        PickupStyle style,
        RootMatch match
) implements Comparable<ThemeBinding> {

    public static final Codec<ThemeBinding> CODEC;
    public static final ThemeBinding DEFAULT;

    public boolean matches(PickupEvent pickupEvent) {
        return match.matches(pickupEvent);
    }

    @Override
    public int compareTo(ThemeBinding other) {
        return Integer.compare(other.priority, priority);
    }

    static {
        CODEC = RecordCodecBuilder.create(codec -> codec.group(
                Codec.INT.fieldOf("priority").forGetter(ThemeBinding::priority),
                PickupStyle.CODEC.fieldOf("style").forGetter(ThemeBinding::style),
                RootMatch.CODEC.fieldOf("match").forGetter(ThemeBinding::match)
        ).apply(codec, ThemeBinding::new));
        DEFAULT = new ThemeBinding(0, PickupStyle.DEFAULT, RootMatch.DEFAULT);
    }
}
