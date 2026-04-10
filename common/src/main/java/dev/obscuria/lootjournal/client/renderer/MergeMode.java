package dev.obscuria.lootjournal.client.renderer;

import net.minecraft.world.item.ItemStack;

import java.util.function.BiPredicate;

public enum MergeMode {
    TYPE_NAMED(MergeMode::isSameItemNamed),
    TYPE(ItemStack::isSameItem),
    STRICT(ItemStack::isSameItemSameTags),
    NONE(MergeMode::alwaysFalse);

    private final BiPredicate<ItemStack, ItemStack> predicate;

    MergeMode(BiPredicate<ItemStack, ItemStack> predicate) {
        this.predicate = predicate;
    }

    public boolean canMerge(ItemStack first, ItemStack second) {
        return predicate.test(first, second);
    }

    private static boolean isSameItemNamed(ItemStack first, ItemStack second) {
        return ItemStack.isSameItem(first, second)
                && !first.hasCustomHoverName()
                && !second.hasCustomHoverName();
    }

    private static boolean alwaysFalse(ItemStack first, ItemStack second) {
        return false;
    }
}
