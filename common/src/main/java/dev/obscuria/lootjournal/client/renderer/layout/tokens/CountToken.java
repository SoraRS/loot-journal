package dev.obscuria.lootjournal.client.renderer.layout.tokens;

import dev.obscuria.lootjournal.LootJournal;
import dev.obscuria.lootjournal.client.events.PickupEvent;
import dev.obscuria.lootjournal.client.renderer.PickupRenderer;
import dev.obscuria.lootjournal.client.themes.styles.PickupStyle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import org.jetbrains.annotations.Nullable;

public record CountToken() implements LayoutToken {

    public static final CountToken SHARED = new CountToken();

    @Override
    public int measureWidth(PickupEvent event, PickupStyle style) {
        var count = event.count();
        if (count == 0) return 0;
        return Minecraft.getInstance().font.width(format(count, null));
    }

    @Override
    public void render(GuiGraphics graphics, PickupRenderer renderer, int x) {
        var count = renderer.event().count();
        if (count == 0) return;
        graphics.drawString(
                Minecraft.getInstance().font,
                format(count, renderer.style()), x, 3, 0xFFFFFF,
                renderer.style().text().dropShadow().get());
    }

    @Override
    public String id() {
        return "COUNT";
    }

    private Component format(int count, @Nullable PickupStyle style) {
        var color = style == null ? 0xffffff : style.text().pickupCountColor().get().toRGB().decimal();
        var textStyle = Style.EMPTY.withColor(color);
        return Component.literal("x" + LootJournal.abbreviate(count)).withStyle(textStyle);
    }
}
