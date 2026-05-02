package dev.obscuria.lootjournal;

import dev.obscuria.lootjournal.client.registry.LootJournalRegistries;
import dev.obscuria.lootjournal.config.Config;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class LootJournal {

    public static final String MODID = "loot_journal";
    public static final String DISPLAY_NAME = "Loot Journal";
    public static final Logger LOGGER = LoggerFactory.getLogger(DISPLAY_NAME);

    public static ResourceLocation identifier(String name) {
        return ResourceLocation.fromNamespaceAndPath(MODID, name);
    }

    public static boolean isAllowed(Player player, ItemStack stack) {
        if (!Config.ENABLE_FILTERING.get()) return true;
        var isSelf = LootJournalHelper.isSelf(player);
        var isCommon = stack.getRarity() == Rarity.COMMON;
        if (isCommon) {
            if (isSelf && Config.HIDE_YOUR_COMMON_ITEMS.get()) return false;
            if (!isSelf && Config.HIDE_OTHER_COMMON_ITEMS.get()) return false;
        }
        return Config.DEFAULT_FILTER_RULE.get().isAllowed(stack);
    }

    public static String abbreviate(double value) {
        if (!Config.ABBREVIATE_NUMBERS.get()) return String.valueOf((int) value);
        double abs = Math.abs(value);

        if (abs >= 1_000_000_000) {
            return abbreviate(value / 1_000_000_000, "B");
        } else if (abs >= 1_000_000) {
            return abbreviate(value / 1_000_000, "M");
        } else if (abs >= 1_000) {
            return abbreviate(value / 1_000, "K");
        } else {
            return String.valueOf((int) value);
        }
    }

    private static String abbreviate(double value, String suffix) {
        if (value >= 100) {
            return String.format("%.0f%s", value, suffix);
        } else if (value >= 10) {
            return String.format("%.1f%s", value, suffix);
        } else {
            return String.format("%.1f%s", value, suffix);
        }
    }

    public static void clientInit() {
        LootJournalRegistries.init();
        Config.init();
    }
}
