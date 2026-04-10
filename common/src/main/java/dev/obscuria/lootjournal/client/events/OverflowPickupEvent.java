package dev.obscuria.lootjournal.client.events;

import dev.obscuria.lootjournal.LootJournalHelper;
import dev.obscuria.lootjournal.client.renderer.PickupRenderer;
import dev.obscuria.lootjournal.client.themes.styles.PickupStyle;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.compress.utils.Lists;

import java.util.List;

public final class OverflowPickupEvent implements PickupEvent {

    private static final Component DISPLAY_NAME = Component.translatable("pickup.loot_journal.overflow");
    private final List<ItemStack> stacks = Lists.newArrayList();
    private final AbstractClientPlayer player;
    private int count;

    public OverflowPickupEvent(ItemPickupEvent event) {
        this(event.player(), event.stack());
    }

    public OverflowPickupEvent(AbstractClientPlayer player, ItemStack stack) {
        this.stacks.add(stack);
        this.player = player;
        this.count = stack.getCount();
    }

    @Override
    public void bind(PickupStyle style) {}

    @Override
    public void renderIcon(GuiGraphics graphics, PickupRenderer renderer) {
        if (stacks.isEmpty()) return;
        var interval = Math.max(0.2, 1.0 - 0.05 * stacks.size());
        var stack = stacks.get((int) (renderer.timeInSeconds() / interval % stacks.size()));
        graphics.renderFakeItem(stack, -8, -8);
    }

    @Override
    public boolean maybeMerge(PickupEvent event) {
        if (!LootJournalHelper.isSamePlayer(player(), event.player())) return false;
        if (!(event instanceof OverflowPickupEvent other)) return false;
        this.stacks.addAll(other.stacks);
        this.count += other.count;
        return true;
    }

    @Override
    public boolean supportsTotalCount() {
        return false;
    }

    @Override
    public Component displayName() {
        return DISPLAY_NAME;
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
        return 0;
    }
}