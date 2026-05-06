package dev.obscuria.lootjournal.client.registry;

import com.google.gson.JsonParser;
import dev.obscuria.lootjournal.LootJournal;
import dev.obscuria.lootjournal.config.ConfigCache;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import org.apache.commons.lang3.StringUtils;

public class PickupResourceManager implements ResourceManagerReloadListener {

    public static final PickupResourceManager SHARED = new PickupResourceManager();

    private PickupResourceManager() {}

    @Override
    public void onResourceManagerReload(ResourceManager manager) {
        ThemeRegistry.clear();
        for (var kind : ResourceKind.values()) {
            kind.loader.onReloadStart();
            final var resources = manager.listResources(kind.loader.resourceDir(), this::isValidResource);
            resources.forEach((path, resource) -> loadResource(kind, path, resource));
            kind.loader.onReloadEnd();
        }
        LootJournalRegistries.Resource.THEME.keyToElement.forEach(ThemeRegistry::add);
        ThemeRegistry.updateActiveTheme();
        ConfigCache.refresh();
    }

    private boolean isValidResource(Identifier path) {
        return path.toString().endsWith(".json");
    }

    private void loadResource(ResourceKind kind, Identifier path, Resource resource) {
        try {
            kind.loader.load(extractKey(kind, path), JsonParser.parseReader(resource.openAsReader()));
        } catch (Exception exception) {
            LootJournal.LOGGER.error("Failed to load resource {}: {}", path, exception.getMessage());
        }
    }

    private Identifier extractKey(ResourceKind kind, Identifier path) {
        return path.withPath(it -> {
            var result = StringUtils.removeStart(it, kind.loader.resourceDir() + "/");
            result = StringUtils.removeEnd(result, ".json");
            return result;
        });
    }
}
