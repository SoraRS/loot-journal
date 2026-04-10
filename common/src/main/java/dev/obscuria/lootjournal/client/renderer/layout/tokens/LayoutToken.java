package dev.obscuria.lootjournal.client.renderer.layout.tokens;

import dev.obscuria.lootjournal.client.events.PickupEvent;
import dev.obscuria.lootjournal.client.renderer.PickupRenderer;
import dev.obscuria.lootjournal.client.themes.styles.PickupStyle;
import net.minecraft.client.gui.GuiGraphics;

public interface LayoutToken {

    int measureWidth(PickupEvent event, PickupStyle style);

    void render(GuiGraphics graphics, PickupRenderer renderer, int x);

    String id();
}
