package dev.obscuria.lootjournal.client.renderer.layout.tokens;

import dev.obscuria.lootjournal.client.events.PickupEvent;
import dev.obscuria.lootjournal.client.renderer.PickupRenderer;
import dev.obscuria.lootjournal.client.themes.styles.PickupStyle;
import net.minecraft.client.gui.GuiGraphics;

public record GapToken(int size) implements LayoutToken {

    @Override
    public int measureWidth(PickupEvent event, PickupStyle style) {
        return size;
    }

    @Override
    public void render(GuiGraphics graphics, PickupRenderer renderer, int x) {}

    @Override
    public String id() {
        return "GAP";
    }
}
