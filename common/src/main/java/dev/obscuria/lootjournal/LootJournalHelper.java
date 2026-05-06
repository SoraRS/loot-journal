package dev.obscuria.lootjournal;

import dev.obscuria.lootjournal.client.events.ItemPickupEvent;
import dev.obscuria.lootjournal.client.events.XpPickupEvent;
import dev.obscuria.lootjournal.client.renderer.PickupComponent;
import dev.obscuria.lootjournal.config.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.network.protocol.game.ClientboundTakeItemEntityPacket;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public final class LootJournalHelper {

    public static void handlePickupPacket(Level level, ClientboundTakeItemEntityPacket packet) {
        if (!Config.ENABLE_LOOT_JOURNAL.get()) return;
        @Nullable var owner = level.getEntity(packet.getPlayerId());
        if (!(owner instanceof AbstractClientPlayer player)) return;
        @Nullable var entity = level.getEntity(packet.getItemId());
        if (entity instanceof ItemEntity itemEntity) {
            pickupItem(player, itemEntity.getItem().copyWithCount(packet.getAmount()));
        } else if (entity instanceof ExperienceOrb experienceOrb) {
            pickupXp(player, experienceOrb.getValue());
        }
    }

    public static void pickupItem(AbstractClientPlayer player, ItemStack stack) {
        if (stack.isEmpty()) return;
        if (isSelf(player)) {
            if (!Config.SHOW_ITEM_PICKUPS.get()) return;
            if (!LootJournal.isAllowed(player, stack)) return;
            PickupComponent.handleEvent(new ItemPickupEvent(player, stack));
        } else {
            if (!Config.TRACK_ITEM_PICKUPS.get()) return;
            if (!isTrackable(player)) return;
            if (!LootJournal.isAllowed(player, stack)) return;
            PickupComponent.handleEvent(new ItemPickupEvent(player, stack));
        }
    }

    public static void pickupXp(AbstractClientPlayer player, int amount) {
        if (isSelf(player)) {
            if (!Config.SHOW_XP_PICKUPS.get()) return;
            PickupComponent.handleEvent(new XpPickupEvent(player, amount));
        } else {
            if (!Config.TRACK_XP_PICKUPS.get()) return;
            if (!isTrackable(player)) return;
            PickupComponent.handleEvent(new XpPickupEvent(player, amount));
        }
    }

    public static boolean isSelf(Player player) {
        return isSamePlayer(Minecraft.getInstance().player, player);
    }

    public static boolean isTrackable(Player player) {
        if (player.hasEffect(MobEffects.INVISIBILITY)) return false;
        if (player.getItemBySlot(EquipmentSlot.HEAD).is(Items.CARVED_PUMPKIN)) return false;
        if (!Config.ENABLE_PLAYER_FILTERING.get()) return true;
        return Config.PLAYER_WHITELIST.get().contains(player.getGameProfile().name());
    }

    public static boolean isSamePlayer(@Nullable Player first, @Nullable Player second) {
        if (first == null || second == null) return false;
        return first.getGameProfile().id().equals(second.getGameProfile().id());
    }
}
