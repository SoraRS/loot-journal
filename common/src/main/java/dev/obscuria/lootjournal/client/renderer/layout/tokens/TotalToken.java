package dev.obscuria.lootjournal.client.renderer.layout.tokens;

import dev.obscuria.lootjournal.LootJournal;
import dev.obscuria.lootjournal.LootJournalHelper;
import dev.obscuria.lootjournal.client.events.PickupEvent;
import dev.obscuria.lootjournal.client.renderer.PickupRenderer;
import dev.obscuria.lootjournal.client.themes.styles.PickupStyle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.PlayerFaceRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import org.jetbrains.annotations.Nullable;

public record TotalToken() implements LayoutToken {

    public static final TotalToken SHARED = new TotalToken();

    @Override
    public int measureWidth(PickupEvent event, PickupStyle style) {
        if (LootJournalHelper.isSelf(event.player())) {
            if (!event.supportsTotalCount()) return 0;
            return Minecraft.getInstance().font.width(format(event.total(), null));
        } else return 12;
    }

    @Override
    public void render(GuiGraphics graphics, PickupRenderer renderer, int x) {
        if (LootJournalHelper.isSelf(renderer.event().player())) {
            if (!renderer.event().supportsTotalCount()) return;

            var color = renderer.style().text().totalCountColor().get();

            graphics.drawString(
                    Minecraft.getInstance().font,
                    format(renderer.event().total(), renderer.style()),
                    x,
                    3,
                    renderer.modulateColor(color),
                    renderer.style().text().dropShadow().get()
            );
        } else {
            var skin = renderer.event().player().getSkin();
            PlayerFaceRenderer.draw(graphics, skin, x, 1, 12);
        }
    }

    @Override
    public String id() {
        return "TOTAL";
    }

    private Component format(int total, @Nullable PickupStyle style) {
        var color = style == null ? 0xffffff : style.text().totalCountColor().get().toRGB().decimal();
        var textStyle = Style.EMPTY.withColor(color);
        return Component.literal(LootJournal.abbreviate(total)).withStyle(textStyle);
    }
}
