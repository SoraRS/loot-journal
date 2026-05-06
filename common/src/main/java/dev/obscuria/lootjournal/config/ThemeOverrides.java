package dev.obscuria.lootjournal.config;

import com.google.gson.*;
import net.minecraft.client.Minecraft;
import dev.obscuria.lootjournal.LootJournal;
import dev.obscuria.lootjournal.client.themes.Theme;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public final class ThemeOverrides {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final Map<String, JsonElement> overrides = new HashMap<>();

    public Optional<Integer> getInt(String key) {
        return getSafely(key, JsonElement::getAsInt);
    }

    public Optional<Double> getDouble(String key) {
        return getSafely(key, JsonElement::getAsDouble);
    }

    public Optional<Boolean> getBoolean(String key) {
        return getSafely(key, JsonElement::getAsBoolean);
    }

    public Optional<String> getString(String key) {
        return getSafely(key, JsonElement::getAsString);
    }

    public void setInt(String key, int value) {
        overrides.put(key, new JsonPrimitive(value));
    }

    public void setDouble(String key, double value) {
        overrides.put(key, new JsonPrimitive(value));
    }

    public void setBoolean(String key, boolean value) {
        overrides.put(key, new JsonPrimitive(value));
    }

    public void setString(String key, String value) {
        overrides.put(key, new JsonPrimitive(value));
    }

    public void load(Theme theme) {
        overrides.clear();
        var configPath = getConfigPath(theme);
        if (!Files.exists(configPath)) return;

        try (var reader = Files.newBufferedReader(configPath)) {
            var root = JsonParser.parseReader(reader);
            if (!root.isJsonObject()) return;

            for (var entry : root.getAsJsonObject().entrySet()) {
                overrides.put(entry.getKey(), entry.getValue());
            }
        } catch (Exception exception) {
            LootJournal.LOGGER.error("Failed to load theme overrides: {}", configPath, exception);
        }
    }

    public void save(Theme theme) {
        var configPath = getConfigPath(theme);

        try {
            Files.createDirectories(configPath.getParent());
            var rootObject = new JsonObject();
            overrides.forEach(rootObject::add);

            try (var writer = Files.newBufferedWriter(configPath)) {
                GSON.toJson(rootObject, writer);
            }
        } catch (Exception exception) {
            LootJournal.LOGGER.error("Failed to save theme overrides: {}", configPath, exception);
        }
    }

    private <T> Optional<T> getSafely(String key, Function<JsonElement, @Nullable T> mapper) {
        try {
            return Optional.ofNullable(mapper.apply(overrides.get(key)));
        } catch (Exception ignored) {
            return Optional.empty();
        }
    }

    private Path getConfigPath(Theme theme) {
        return Minecraft.getInstance().gameDirectory.toPath()
                .resolve("config")
                .resolve("obscuria")
                .resolve("loot_journal-themes")
                .resolve(theme.displayName() + ".json");
    }
}
