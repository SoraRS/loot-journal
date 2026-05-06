package dev.obscuria.lootjournal.neoforge;

import dev.obscuria.lootjournal.LootJournal;
import dev.obscuria.lootjournal.client.registry.PickupResourceManager;
import dev.obscuria.lootjournal.client.renderer.PickupComponent;
import dev.obscuria.lootjournal.config.ConfigBuilder;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.AddClientReloadListenersEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

@Mod(value = LootJournal.MODID, dist = Dist.CLIENT)
public final class NeoForgeLootJournal {

    public NeoForgeLootJournal(IEventBus modBus, ModContainer container) {
        LootJournal.clientInit();

        IConfigScreenFactory configScreenFactory =
                (minecraft, parent) -> ConfigBuilder.createConfigScreen(parent);

        container.registerExtensionPoint(IConfigScreenFactory.class, configScreenFactory);

        modBus.addListener(NeoForgeLootJournal::addClientReloadListeners);
        modBus.addListener(NeoForgeLootJournal::registerGuiLayers);
    }

    private static void addClientReloadListeners(AddClientReloadListenersEvent event) {
        event.addListener(
                LootJournal.identifier("pickup_resource_manager"),
                PickupResourceManager.SHARED
        );
    }

    private static void registerGuiLayers(RegisterGuiLayersEvent event) {
        event.registerAbove(
                VanillaGuiLayers.SUBTITLE_OVERLAY,
                LootJournal.identifier("pickup_component"),
                (graphics, tickCounter) -> PickupComponent.render(graphics)
        );
    }
}