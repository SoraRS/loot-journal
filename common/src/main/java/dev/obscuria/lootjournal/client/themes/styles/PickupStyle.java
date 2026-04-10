package dev.obscuria.lootjournal.client.themes.styles;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.obscuria.lootjournal.client.registry.LootJournalRegistries;
import dev.obscuria.lootjournal.client.themes.styles.banners.NoneBanner;
import dev.obscuria.lootjournal.client.themes.styles.banners.PickupBanner;
import dev.obscuria.lootjournal.client.themes.styles.icons.PickupIcon;
import dev.obscuria.lootjournal.client.themes.styles.icons.SimpleIcon;
import dev.obscuria.lootjournal.client.themes.styles.panels.NonePanel;
import dev.obscuria.lootjournal.client.themes.styles.panels.PickupPanel;

public record PickupStyle(
        PickupPanel panel,
        PickupBanner banner,
        PickupIcon icon,
        TextStyle text
) {

    public static final Codec<PickupStyle> DIRECT_CODEC;
    public static final Codec<PickupStyle> CODEC;
    public static final PickupStyle DEFAULT;

    static {
        DIRECT_CODEC = RecordCodecBuilder.create(codec -> codec.group(
                PickupPanel.CODEC.fieldOf("panel").forGetter(PickupStyle::panel),
                PickupBanner.CODEC.fieldOf("banner").forGetter(PickupStyle::banner),
                PickupIcon.CODEC.fieldOf("icon").forGetter(PickupStyle::icon),
                TextStyle.CODEC.fieldOf("text").forGetter(PickupStyle::text)
        ).apply(codec, PickupStyle::new));
        CODEC = LootJournalRegistries.Resource.PICKUP_STYLE.byNameCodec();
        DEFAULT = new PickupStyle(NonePanel.DEFAULT, NoneBanner.DEFAULT, SimpleIcon.DEFAULT, TextStyle.DEFAULT);
    }
}
