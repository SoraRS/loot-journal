package dev.obscuria.lootjournal.client.renderer.layout.tokens;

import dev.obscuria.lootjournal.client.events.PickupEvent;
import dev.obscuria.lootjournal.client.renderer.PickupRenderer;
import dev.obscuria.lootjournal.client.themes.styles.PickupStyle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;

public record NameToken() implements LayoutToken {

    public static final NameToken SHARED = new NameToken();
    private static final String ELLIPSIS = "...";

    @Override
    public int measureWidth(PickupEvent event, PickupStyle style) {
        return Minecraft.getInstance().font.width(event.displayName());
    }

    @Override
    public void render(GuiGraphics graphics, PickupRenderer renderer, int x) {
        var color = renderer.style().text().nameColor().get().toRGB().decimal();
        var text = Component.empty()
                .append(renderer.event().displayName())
                .withStyle(Style.EMPTY.withColor(color));
        graphics.drawString(
                Minecraft.getInstance().font,
                text, x, 3, 0xFFFFFF,
                renderer.style().text().dropShadow().get());
    }

    private static String trimToWidth(Font font, String text, int maxWidth) {
        if (font.width(text) <= maxWidth) {
            return text;
        }

        int ellipsisWidth = font.width(ELLIPSIS);

        // если даже "..." не влезает
        if (ellipsisWidth > maxWidth) {
            return "";
        }

        String cut = font.plainSubstrByWidth(text, maxWidth - ellipsisWidth);
        return cut + ELLIPSIS;
    }

    @Override
    public String id() {
        return "NAME";
    }
}
