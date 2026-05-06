package dev.obscuria.lootjournal.fabric;

import dev.obscuria.lootjournal.LootJournal;
import dev.obscuria.lootjournal.client.registry.PickupResourceManager;
import dev.obscuria.lootjournal.client.renderer.PickupComponent;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;

public final class FabricLootJournal implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        LootJournal.clientInit();
        ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(new SimpleSynchronousResourceReloadListener() {
            @Override
            public Identifier getFabricId() {
                return LootJournal.identifier("pickup_resources");
            }

            @Override
            public void onResourceManagerReload(ResourceManager manager) {
                PickupResourceManager.SHARED.onResourceManagerReload(manager);
            }
        });
        HudElementRegistry.attachElementAfter(
                VanillaHudElements.SUBTITLES,
                LootJournal.identifier("pickup_component"),
                (graphics, tickCounter) -> PickupComponent.render(graphics));
    }
}
