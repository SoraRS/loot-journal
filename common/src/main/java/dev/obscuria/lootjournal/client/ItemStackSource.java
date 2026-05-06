package dev.obscuria.lootjournal.client;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import java.util.function.Consumer;

public enum ItemStackSource {
    INVENTORY {
        @Override
        public void forEach(Player player, Consumer<ItemStack> consumer) {
            var inventory = player.getInventory();
            for (int slot = 0; slot < inventory.getContainerSize(); slot++) {
                consumer.accept(inventory.getItem(slot));
            }
        }
    },
    CURIOS {
        @Override
        public void forEach(Player player, Consumer<ItemStack> consumer) {
        }
    };
    public abstract void forEach(Player player, Consumer<ItemStack> consumer);
}