package dev.obscuria.lootjournal.fabric;

import dev.obscuria.lootjournal.LootJournal;
import net.fabricmc.api.ClientModInitializer;

public final class FabricLootJournal implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        LootJournal.clientInit();
    }
}