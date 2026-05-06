package dev.obscuria.lootjournal.client.registry;

import dev.obscuria.lootjournal.LootJournal;
import dev.obscuria.lootjournal.client.themes.BakedTheme;
import dev.obscuria.lootjournal.client.themes.Theme;
import dev.obscuria.lootjournal.config.Config;
import net.minecraft.resources.Identifier;

import java.util.*;
import java.util.stream.Stream;

public final class ThemeRegistry {

    private static final HashMap<Identifier, BakedTheme> THEMES = new HashMap<>();
    private static final List<Identifier> BUILTIN_ORDER = List.of(
            LootJournal.identifier("classic"),
            LootJournal.identifier("minimal"),
            LootJournal.identifier("tooltip"),
            LootJournal.identifier("contrast"),
            LootJournal.identifier("java"),
            LootJournal.identifier("bedrock"),
            LootJournal.identifier("dungeons"));

    private static BakedTheme activeTheme = BakedTheme.DEFAULT;

    public static BakedTheme activeTheme() {
        return activeTheme;
    }

    public static Stream<BakedTheme> stream() {
        var builtIn = BUILTIN_ORDER.stream()
                .map(THEMES::get)
                .filter(Objects::nonNull);

        var others = THEMES.entrySet().stream()
                .filter(entry -> !BUILTIN_ORDER.contains(entry.getKey()))
                .sorted(Map.Entry.comparingByKey())
                .map(Map.Entry::getValue);

        return Stream.concat(builtIn, others);
    }

    public static void add(Identifier id, Theme theme) {
        add(id, theme.bake());
    }

    public static void add(Identifier id, BakedTheme theme) {
        THEMES.put(id, theme);
    }

    public static Identifier getId(BakedTheme theme) {
        if (theme == BakedTheme.DEFAULT) return LootJournal.identifier("fallback");
        return THEMES.entrySet().stream()
                .filter(entry -> entry.getValue().equals(theme))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElseThrow();
    }

    public static boolean isBuiltin(BakedTheme theme) {
        return BUILTIN_ORDER.contains(getId(theme));
    }

    public static void updateActiveTheme() {
        var activeThemeName = Config.THEME.get();
        activeTheme = THEMES.values().stream()
                .filter(theme -> theme.displayName().equals(activeThemeName))
                .findFirst().orElse(BakedTheme.DEFAULT);
    }

    public static void clearCache() {
        THEMES.values().forEach(BakedTheme::clearCache);
    }

    public static void clear() {
        THEMES.clear();
    }

    public static List<String> listThemeNames() {
        return stream().map(BakedTheme::displayName).toList();
    }
}
