package dev.obscuria.lootjournal.client.events;

import dev.obscuria.lootjournal.LootJournal;
import dev.obscuria.lootjournal.LootJournalHelper;
import dev.obscuria.lootjournal.client.renderer.PickupRenderer;
import dev.obscuria.lootjournal.client.themes.styles.PickupStyle;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.Nullable;

public final class XpPickupEvent implements PickupEvent {

    private static final Identifier ICON = LootJournal.identifier("textures/gui/experience.png");
    private final AbstractClientPlayer player;
    private PickupStyle style = PickupStyle.DEFAULT;
    private int count;

    public XpPickupEvent(AbstractClientPlayer player, int count) {
        this.player = player;
        this.count = count;
    }

    @Override
    public void bind(PickupStyle style) {
        this.style = style;
    }

    @Override
    public void renderIcon(GuiGraphics graphics, PickupRenderer renderer) {
        graphics.blit(ICON, -8, -8, 0, 0, 16, 16, 16, 32);
        renderer.pushModulate(0.5f + 0.5f * (float) Math.cos(renderer.timeInSeconds() * 10f));
        graphics.blit(ICON, -8, -8, 0, 16, 16, 16, 16, 32);
        renderer.popModulate();
    }

    @Override
    public boolean maybeMerge(PickupEvent event) {
        if (!LootJournalHelper.isSamePlayer(player(), event.player())) return false;
        if (!(event instanceof XpPickupEvent other)) return false;
        this.count += other.count;
        return true;
    }

    @Override
    public boolean supportsTotalCount() {
        return true;
    }

    @Override
    public Component displayName() {
        return style.text().ignoreFormatting().get()
                ? Component.translatable("pickup.loot_journal.xp")
                : Component.translatable("pickup.loot_journal.xp")
                .withStyle(ChatFormatting.GREEN);
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
        @Nullable var player = Minecraft.getInstance().player;
        return player != null ? player.totalExperience : 0;
    }
}
