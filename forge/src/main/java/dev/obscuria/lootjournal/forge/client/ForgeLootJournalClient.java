package dev.obscuria.lootjournal.forge.client;

import dev.obscuria.lootjournal.LootJournal;
import dev.obscuria.lootjournal.client.registry.PickupResourceManager;
import dev.obscuria.lootjournal.config.ConfigBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.fml.ModLoadingContext;

public final class ForgeLootJournalClient {

    public static void init() {
        LootJournal.clientInit();
        registerConfigScreen();
        registerResourceListener();
    }

    private static void registerConfigScreen() {
        ModLoadingContext.get().registerExtensionPoint(
                ConfigScreenHandler.ConfigScreenFactory.class,
                () -> new ConfigScreenHandler.ConfigScreenFactory(
                        (client, parent) -> ConfigBuilder.createConfigScreen(parent)));
    }

    private static void registerResourceListener() {
        try {
            if (!(Minecraft.getInstance().getResourceManager() instanceof ReloadableResourceManager manager)) return;
            manager.registerReloadListener(PickupResourceManager.SHARED);
        } catch (Exception exception) {
            LootJournal.LOGGER.error("Failed to register resource listener", exception);
        }
    }
}
