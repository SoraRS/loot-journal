package dev.obscuria.lootjournal.client;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;

public enum ItemCountResolver {

    SELF {
        @Override
        public int resolve(ItemStack target, ItemStack stack) {
            return ItemStack.isSameItemSameComponents(target, stack) ? stack.getCount() : 0;
        }
    },

    SHULKER_BOX {
        @Override
        public int resolve(ItemStack target, ItemStack stack) {
            return 0;
        }
    },

    GENERIC_CONTAINER {
        @Override
        public int resolve(ItemStack target, ItemStack stack) {
            var contents = stack.get(DataComponents.CONTAINER);
            if (contents == null || contents == ItemContainerContents.EMPTY) return 0;

            var total = 0;
            for (var containedStack : contents.nonEmptyItems()) {
                total += resolveRecursive(target, containedStack);
            }
            return total;
        }
    };

    public abstract int resolve(ItemStack target, ItemStack stack);

    public static int resolveRecursive(ItemStack target, ItemStack stack) {
        var total = 0;
        for (var resolver : values()) {
            total += resolver.resolve(target, stack);
        }
        return total;
    }
}