package dev.obscuria.lootjournal.client.themes.match;

import dev.obscuria.lootjournal.client.events.PickupEvent;
import dev.obscuria.lootjournal.client.events.ItemPickupEvent;
import net.minecraft.world.item.ItemStack;

public interface ItemStackMatch extends PickupMatch {

    boolean matches(ItemStack stack);

    @Override
    default boolean matches(PickupEvent pickupEvent) {
        if (!(pickupEvent instanceof ItemPickupEvent itemPickup)) return false;
        return matches(itemPickup.stack());
    }
}
