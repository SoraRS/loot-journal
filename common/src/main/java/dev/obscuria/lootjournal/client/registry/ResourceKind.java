package dev.obscuria.lootjournal.client.registry;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import dev.obscuria.lootjournal.LootJournal;
import dev.obscuria.lootjournal.client.themes.Theme;
import dev.obscuria.lootjournal.client.themes.styles.PickupStyle;
import net.minecraft.resources.Identifier;

public enum ResourceKind {

    STYLE(new Loader<>("style", "styles", PickupStyle.DIRECT_CODEC, LootJournalRegistries.Resource.PICKUP_STYLE)),
    THEME(new Loader<>("theme", "themes", Theme.DIRECT_CODEC, LootJournalRegistries.Resource.THEME));

    public final Loader<?> loader;

    ResourceKind(Loader<?> loader) {
        this.loader = loader;
    }

    public record Loader<T>(
            String name,
            String directory,
            Codec<T> codec,
            ResourceRegistry<T> registry) {

        public String resourceDir() {
            return "pickups/" + directory;
        }

        public void onReloadStart() {
            registry.onReloadStart();
        }

        public void load(Identifier key, JsonElement element) {
            final var result = codec.decode(JsonOps.INSTANCE, element);
            result.result().ifPresent(it -> registry.register(key, it.getFirst()));
            result.error().ifPresent(it -> LootJournal.LOGGER.error("Failed to register {} with key {}: {}", name, key, it.message()));
        }

        public void onReloadEnd() {
            registry.onReloadEnd();
            LootJournal.LOGGER.info("Loaded {} resources from {}", registry.total(), resourceDir());
        }
    }
}
