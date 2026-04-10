package dev.obscuria.lootjournal.client.themes;

import dev.obscuria.fragmentum.util.color.ARGB;
import dev.obscuria.fragmentum.util.color.Colors;
import dev.obscuria.lootjournal.LootJournal;
import dev.obscuria.lootjournal.client.renderer.PickupRenderUtils;
import dev.obscuria.lootjournal.client.events.PickupEvent;
import dev.obscuria.lootjournal.client.themes.styles.PickupStyle;
import dev.obscuria.lootjournal.client.themes.variables.Variable;
import dev.obscuria.lootjournal.config.Config;
import dev.obscuria.lootjournal.config.ThemeOverrides;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BakedTheme {

    public static final BakedTheme DEFAULT = Theme.DEFAULT.bake();

    public final Theme theme;
    public final Map<String, Variable<?>> variables;
    public final List<ThemeBinding> sortedBindings;
    public final ThemeOverrides overrides;
    public final Map<String, Object> cache;

    public BakedTheme(Theme theme) {
        this.theme = theme;
        this.variables = theme.variables().stream().collect(Collectors.toMap(Variable::key, Function.identity()));
        this.sortedBindings = theme.bindings().stream().sorted().toList();
        this.overrides = new ThemeOverrides();
        this.cache = new HashMap<>();
        this.loadOverrides();
    }

    public PickupStyle findStyle(PickupEvent pickupEvent) {
        for (var binding : sortedBindings) {
            if (!binding.matches(pickupEvent)) continue;
            return binding.style();
        }
        return PickupStyle.DEFAULT;
    }

    public String displayName() {
        return theme.displayName();
    }

    public String description() {
        return theme.description();
    }

    public int screenMargin() {
        return theme.layout().screenMargin() + Config.ANCHOR_X_OFFSET.get();
    }

    public int entryPaddingLeft() {
        return theme.layout().paddingLeft() + Config.ELEMENT_PADDING_LEFT.get();
    }

    public int entryPaddingRight() {
        return theme.layout().paddingRight() + Config.ELEMENT_PADDING_RIGHT.get();
    }

    public int entryPaddingTop() {
        return theme.layout().paddingTop() + Config.ELEMENT_PADDING_TOP.get();
    }

    public int entryPaddingBottom() {
        return theme.layout().paddingBottom() + Config.ELEMENT_PADDING_BOTTOM.get();
    }

    public int entryHeight() {
        return PickupRenderUtils.PICKUP_HEIGHT
                + entryPaddingTop()
                + entryPaddingBottom();
    }

    public boolean getCachedBoolean(String name) {
        return (boolean) cache.computeIfAbsent(name, this::getAsBoolean);
    }

    public int getCachedInt(String name) {
        return (int) cache.computeIfAbsent(name, this::getAsInt);
    }

    public ARGB getCachedARGB(String name) {
        return (ARGB) cache.computeIfAbsent(name, this::getAsARGB);
    }

    public boolean getAsBoolean(String optionKey) {
        try {
            return (boolean) variables.get(optionKey).actualValue(this);
        } catch (Exception exception) {
            LootJournal.LOGGER.warn("Failed to get defaultValue for option {}", optionKey, exception);
            return false;
        }
    }

    public int getAsInt(String optionKey) {
        try {
            return (int) variables.get(optionKey).actualValue(this);
        } catch (Exception exception) {
            LootJournal.LOGGER.warn("Failed to get defaultValue for option {}", optionKey, exception);
            return 0;
        }
    }

    public ARGB getAsARGB(String optionKey) {
        try {
            return (ARGB) variables.get(optionKey).actualValue(this);
        } catch (Exception exception) {
            LootJournal.LOGGER.warn("Failed to get defaultValue for option {}", optionKey, exception);
            return Colors.argbOf("#ffffffff");
        }
    }

    public void override(String optionKey, int value) {
        overrides.setInt(optionKey, value);
    }

    public void clearCache() {
        cache.clear();
    }

    public void loadOverrides() {
        this.overrides.load(theme);
    }

    public void saveOverrides() {
        this.overrides.save(theme);
    }
}
