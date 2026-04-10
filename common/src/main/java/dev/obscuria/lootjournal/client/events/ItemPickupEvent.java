package dev.obscuria.lootjournal.client.events;

import dev.obscuria.lootjournal.LootJournalHelper;
import dev.obscuria.lootjournal.client.ItemCountResolver;
import dev.obscuria.lootjournal.client.ItemStackSource;
import dev.obscuria.lootjournal.client.renderer.PickupRenderer;
import dev.obscuria.lootjournal.client.themes.styles.PickupStyle;
import dev.obscuria.lootjournal.config.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.ItemStack;

public final class ItemPickupEvent implements PickupEvent {

    private static final String ELLIPSIS = "...";

    private final AbstractClientPlayer player;
    private final ItemStack stack;
    private PickupStyle style;
    private Component displayName;
    private int count;
    private int total;

    public ItemPickupEvent(AbstractClientPlayer player, ItemStack stack) {
        this.player = player;
        this.stack = stack;
        this.count = stack.getCount();
        this.style = PickupStyle.DEFAULT;
        this.displayName = Component.empty();
        this.recomputeTotalCount(count);
    }

    public ItemStack stack() {
        return stack;
    }

    @Override
    public void bind(PickupStyle style) {
        this.style = style;
        this.updateDisplayName();
    }

    @Override
    public void renderIcon(GuiGraphics graphics, PickupRenderer renderer) {
        graphics.renderFakeItem(stack, -8, -8);
    }

    @Override
    public boolean maybeMerge(PickupEvent event) {
        if (!LootJournalHelper.isSamePlayer(player(), event.player())) return false;
        if (!(event instanceof ItemPickupEvent other)) return false;
        if (!Config.MERGE_MODE.get().canMerge(stack, other.stack)) return false;
        this.count += other.count;
        this.updateDisplayName();
        this.recomputeTotalCount(other.count);
        return true;
    }

    @Override
    public boolean supportsTotalCount() {
        return LootJournalHelper.isSelf(player);
    }

    @Override
    public Component displayName() {
        return displayName;
    }

    @Override
    public AbstractClientPlayer player() {
        return player;
    }

    @Override
    public int count() {
        return count;
    }

    @Override
    public int total() {
        return total;
    }

    private void updateDisplayName() {
        var name = Component.translatable("pickup.loot_journal.item", stack.getHoverName()).getString();
        var trimmedName = trimToWidth(Minecraft.getInstance().font, name, Config.MAX_NAME_WIDTH.get());
        this.displayName = Component.literal(trimmedName).withStyle(getNameStyle());
    }

    private Style getNameStyle() {
        if (style.text().ignoreFormatting().get()) return Style.EMPTY;
        var nameStyle = stack.getDisplayName().getStyle();
        return stack.hasCustomHoverName()
                ? nameStyle.withItalic(true)
                : nameStyle;
    }

    private void recomputeTotalCount(int baseCount) {
        if (!supportsTotalCount()) return;
        var client = Minecraft.getInstance();
        this.total = baseCount;
        if (client.player == null) return;
        for (var source : ItemStackSource.values()) {
            source.forEach(client.player, this::accumulateFromStack);
        }
    }

    private void accumulateFromStack(ItemStack stack) {
        this.total += ItemCountResolver.resolveRecursive(this.stack, stack);
    }

    private static String trimToWidth(Font font, String text, int maxWidth) {
        if (font.width(text) <= maxWidth) {
            return text;
        }

        int ellipsisWidth = font.width(ELLIPSIS);

        if (ellipsisWidth > maxWidth) {
            return "";
        }

        String cut = font.plainSubstrByWidth(text, maxWidth - ellipsisWidth);
        return cut + ELLIPSIS;
    }
}