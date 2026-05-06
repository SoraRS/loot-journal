package dev.obscuria.lootjournal.client;

import dev.obscuria.lootjournal.config.ConfigCache;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public enum DefaultFilterRule {
    ALLOW_ALL,
    DENY_ALL;

    @SuppressWarnings("deprecation")
    public boolean isAllowed(ItemStack stack) {

        var item = stack.getItem();
        var namespace = item.builtInRegistryHolder().key().identifier().getNamespace();

        if (this == ALLOW_ALL) {
            return !ConfigCache.modBlacklist.contains(namespace)
                    && !ConfigCache.itemBlacklist.contains(item)
                    && !any(ConfigCache.tagBlacklist, stack);
        } else {
            return ConfigCache.modWhitelist.contains(namespace)
                    || ConfigCache.itemWhitelist.contains(item)
                    || any(ConfigCache.tagWhitelist, stack);
        }
    }

    private static boolean any(List<TagKey<Item>> tags, ItemStack stack) {
        for (var tag : tags) {
            if (!stack.is(tag)) continue;
            return true;
        }
        return false;
    }
}