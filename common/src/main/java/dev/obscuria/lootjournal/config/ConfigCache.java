package dev.obscuria.lootjournal.config;

import dev.obscuria.fragmentum.content.util.easing.Easing;
import dev.obscuria.fragmentum.content.util.easing.EasingFunction;
import dev.obscuria.lootjournal.LootJournal;
import dev.obscuria.lootjournal.client.renderer.GrowthDirection;
import dev.obscuria.lootjournal.client.renderer.ScreenAnchor;
import dev.obscuria.lootjournal.client.renderer.layout.LayoutParser;
import dev.obscuria.lootjournal.client.renderer.layout.PickupLayout;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class ConfigCache {

    public static ScreenAnchor anchor = ScreenAnchor.BOTTOM_LEFT;
    public static GrowthDirection growthDirection = GrowthDirection.UP;
    public static EasingFunction pulseEasing = Easing.LINEAR;
    public static PickupLayout layout = PickupLayout.DEFAULT;

    public static final List<Item> itemBlacklist = new ArrayList<>();
    public static final List<Item> itemWhitelist = new ArrayList<>();
    public static final List<TagKey<Item>> tagBlacklist = new ArrayList<>();
    public static final List<TagKey<Item>> tagWhitelist = new ArrayList<>();
    public static final List<String> modBlacklist = new ArrayList<>();
    public static final List<String> modWhitelist = new ArrayList<>();

    public static void refresh() {

        anchor = Config.SCREEN_ANCHOR.get();
        growthDirection = Config.GROWTH_DIRECTION.get();
        pulseEasing = Config.PULSE_EASE_IN.get().mergeOut(
                Config.PULSE_EASE_OUT.get(),
                Config.PULSE_PEAK.get().floatValue());

        try {
            layout = LayoutParser.parse(Config.ELEMENT_ORDER.get());
        } catch (Exception exception) {
            LootJournal.LOGGER.error("Failed to parse element order: {}", exception.getMessage());
            layout = PickupLayout.DEFAULT;
        }

        itemBlacklist.clear();
        itemWhitelist.clear();
        tagBlacklist.clear();
        tagWhitelist.clear();
        modBlacklist.clear();
        modWhitelist.clear();

        try {
            Config.ITEM_ID_BLACKLIST.get().stream()
                    .map(Identifier::tryParse)
                    .filter(Objects::nonNull)
                    .flatMap(id -> BuiltInRegistries.ITEM.get(id).stream())
                    .map(reference -> reference.value())
                    .forEach(itemBlacklist::add);
            Config.ITEM_ID_WHITELIST.get().stream()
                    .map(Identifier::tryParse)
                    .filter(Objects::nonNull)
                    .flatMap(id -> BuiltInRegistries.ITEM.get(id).stream())
                    .map(reference -> reference.value())
                    .forEach(itemWhitelist::add);
            Config.ITEM_TAG_BLACKLIST.get().stream()
                    .map(Identifier::tryParse)
                    .filter(Objects::nonNull)
                    .map(it -> TagKey.create(Registries.ITEM, it))
                    .forEach(tagBlacklist::add);
            Config.ITEM_TAG_WHITELIST.get().stream()
                    .map(Identifier::tryParse)
                    .filter(Objects::nonNull)
                    .map(it -> TagKey.create(Registries.ITEM, it))
                    .forEach(tagWhitelist::add);
            modBlacklist.addAll(Config.MOD_ID_BLACKLIST.get());
            modWhitelist.addAll(Config.MOD_ID_WHITELIST.get());
        } catch (Exception ignored) {}
    }
}
