package dev.obscuria.lootjournal.client.themes.styles.panels;

import com.mojang.serialization.Codec;
import dev.obscuria.fragmentum.registry.BootstrapContext;
import dev.obscuria.lootjournal.client.registry.LootJournalRegistries;
import dev.obscuria.lootjournal.client.renderer.PickupRenderer;
import net.minecraft.client.gui.GuiGraphics;

import java.util.function.Function;

public interface PickupPanel {

    Codec<PickupPanel> CODEC = LootJournalRegistries
            .PICKUP_PANEL_TYPE.byNameCodec()
            .dispatch(PickupPanel::codec, Function.identity());

    Codec<? extends PickupPanel> codec();

    void render(GuiGraphics graphics, PickupRenderer pickup);

    static void bootstrap(BootstrapContext<Codec<? extends PickupPanel>> context) {
        context.register("none", () -> NonePanel.CODEC);
        context.register("fill", () -> FillPanel.CODEC);
        context.register("nine_sliced", () -> NineSlicedPanel.CODEC);
    }
}
