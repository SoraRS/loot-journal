package dev.obscuria.lootjournal.client.themes.styles.panels;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import dev.obscuria.fragmentum.content.registry.BootstrapContext;
import dev.obscuria.lootjournal.client.registry.LootJournalRegistries;
import dev.obscuria.lootjournal.client.renderer.PickupRenderer;
import net.minecraft.client.gui.GuiGraphics;

import java.util.function.Function;

public interface PickupPanel {

    Codec<PickupPanel> CODEC = LootJournalRegistries
            .PICKUP_PANEL_TYPE.byNameCodec()
            .dispatch(PickupPanel::codec, Function.identity());

    MapCodec<? extends PickupPanel> codec();

    void render(GuiGraphics graphics, PickupRenderer pickup);

    static void bootstrap(BootstrapContext<MapCodec<? extends PickupPanel>> context) {
        context.register("none", () -> NonePanel.MAP_CODEC);
        context.register("fill", () -> FillPanel.MAP_CODEC);
        context.register("nine_sliced", () -> NineSlicedPanel.MAP_CODEC);
    }
}