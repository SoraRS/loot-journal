package dev.obscuria.lootjournal.client;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.ItemStack;

public enum ItemCountResolver {

    SELF {
        @Override
        public int resolve(ItemStack target, ItemStack stack) {
            return ItemStack.isSameItem(target, stack) ? stack.getCount() : 0;
        }
    },
    SHULKER_BOX {
        @Override
        public int resolve(ItemStack target, ItemStack stack) {
            if (!stack.hasTag()) return 0;
            if (!stack.getOrCreateTag().contains(TAG_BLOCK, ListTag.TAG_COMPOUND)) return 0;
            if (!(stack.getOrCreateTag().getCompound(TAG_BLOCK).get(TAG_ITEMS) instanceof ListTag list)) return 0;
            return resolvePackedList(target, list);
        }
    },
    GENERIC_CONTAINER {
        @Override
        public int resolve(ItemStack target, ItemStack stack) {
            if (!stack.hasTag()) return 0;
            if (!(stack.getOrCreateTag().get(TAG_ITEMS) instanceof ListTag list)) return 0;
            return resolvePackedList(target, list);
        }
    };

    private static final String TAG_BLOCK = "BlockEntityTag";
    private static final String TAG_ITEMS = "Items";

    public abstract int resolve(ItemStack target, ItemStack stack);

    public static int resolveRecursive(ItemStack target, ItemStack stack) {
        var total = 0;
        for (var resolver : values()) {
            total += resolver.resolve(target, stack);
        }
        return total;
    }

    private static int resolvePackedList(ItemStack target, ListTag list) {
        var total = 0;
        for (var element : list) {
            if (!(element instanceof CompoundTag packedItem)) continue;
            total += resolveRecursive(target, ItemStack.of(packedItem));
        }
        return total;
    }
}
