package dev.obscuria.lootjournal.client.renderer;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.component.DataComponents;
import java.util.function.BiPredicate;

public enum MergeMode {
    TYPE_NAMED(MergeMode::isSameItemNamed),
    TYPE(ItemStack::isSameItem),
    STRICT(ItemStack::isSameItemSameComponents),
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
                && !first.has(DataComponents.CUSTOM_NAME)
                && !second.has(DataComponents.CUSTOM_NAME);
    }

    private static boolean alwaysFalse(ItemStack first, ItemStack second) {
        return false;
    }
}
