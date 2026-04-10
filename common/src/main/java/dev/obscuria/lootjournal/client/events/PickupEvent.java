package dev.obscuria.lootjournal.client.events;

import dev.obscuria.lootjournal.client.renderer.PickupRenderer;
import dev.obscuria.lootjournal.client.themes.styles.PickupStyle;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.network.chat.Component;

public interface PickupEvent {

    void bind(PickupStyle style);

    void renderIcon(GuiGraphics graphics, PickupRenderer renderer);

    boolean maybeMerge(PickupEvent event);

    boolean supportsTotalCount();

    Component displayName();

    AbstractClientPlayer player();

    int count();

    int total();
}