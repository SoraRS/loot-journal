package dev.obscuria.lootjournal.client.renderer.layout;

import com.google.common.collect.Lists;
import dev.obscuria.lootjournal.client.events.PickupEvent;
import dev.obscuria.lootjournal.client.renderer.PickupRenderer;
import dev.obscuria.lootjournal.client.renderer.layout.tokens.LayoutToken;
import dev.obscuria.lootjournal.client.renderer.layout.tokens.NameToken;
import dev.obscuria.lootjournal.client.themes.styles.PickupStyle;
import net.minecraft.client.gui.GuiGraphics;

import java.util.List;

public record PickupLayout(List<LayoutToken> tokens) {

    public static final PickupLayout DEFAULT = new PickupLayout(List.of(NameToken.SHARED));

    public LayoutResult measure(PickupEvent entry, PickupStyle style, boolean mirrored) {

        LayoutResult result = new LayoutResult();

        List<LayoutToken> list = mirrored ? Lists.reverse(tokens) : tokens;

        int cursor = 0;

        for (LayoutToken token : list) {

            int w = token.measureWidth(entry, style);

            int x;

            x = cursor;
            cursor += w;

            result.add(new LayoutEntry(token, x, w));
        }

        return result;
    }

    public int measureTotalWidth(PickupEvent entry, PickupStyle style) {
        int total = 0;
        for (LayoutToken t : tokens) {
            total += t.measureWidth(entry, style);
        }
        return total;
    }

    public void render(GuiGraphics graphics, PickupRenderer pickup) {

        for (var entry : pickup.layout().getAll()) {
            entry.token().render(graphics, pickup, entry.x());
        }
    }
}