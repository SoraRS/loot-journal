package dev.obscuria.lootjournal.config;

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.*;
import dev.obscuria.fragmentum.config.ConfigValue;
import dev.obscuria.fragmentum.content.util.easing.Easing;
import dev.obscuria.lootjournal.client.DefaultFilterRule;
import dev.obscuria.lootjournal.client.registry.ThemeRegistry;
import dev.obscuria.lootjournal.client.renderer.*;
import dev.obscuria.lootjournal.client.themes.BakedTheme;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.tags.TagKey;

import java.util.function.Supplier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public final class ConfigBuilder {

    public static Screen createConfigScreen(Screen parent) {

        return YetAnotherConfigLib.createBuilder()
                .title(Component.literal("Loot Journal Options"))
                .category(cat("general")
                        .option(Opts.bool(Config.ENABLE_LOOT_JOURNAL))
                        .option(Opts.bool(Config.SHOW_ITEM_PICKUPS))
                        .option(Opts.bool(Config.SHOW_XP_PICKUPS))
                        .option(Opts.bool(Config.SHOW_OVERFLOW_PICKUPS))
                        .option(Opts.bool(Config.ABBREVIATE_NUMBERS))
                        .group(gr("display")
                                .option(Opts.enumCycle(Config.MERGE_MODE, MergeMode.class))
                                .option(Opts.enumCycle(Config.STACKING_MODE, StackingMode.class))
                                .option(Opts.intSlider(Config.MAX_NAME_WIDTH, 0, 500, 5, Format.PIXELS))
                                .option(Opts.doubleSlider(Config.DISPLAY_TIME, 1, 60, 0.5, Format.SECONDS))
                                .option(Opts.intSlider(Config.DISPLAY_CAPACITY, 1, 64, 1, Format.ENTRIES))
                                .option(Opts.intSlider(Config.QUEUE_SIZE, 0, 256, 1, Format.ENTRIES))
                                .build())
                        .build())
                .category(createThemesCategory())
                .category(cat("layout")
                        .option(Opts.enumCycle(Config.SCREEN_ANCHOR, ScreenAnchor.class))
                        .option(Opts.enumCycle(Config.GROWTH_DIRECTION, GrowthDirection.class))
                        .option(Opts.intSlider(Config.ANCHOR_X_OFFSET, -256, 256, 1, Format.PIXELS))
                        .option(Opts.intSlider(Config.ANCHOR_Y_OFFSET, -256, 256, 1, Format.PIXELS))
                        .option(Opts.intSlider(Config.SEPARATION, 0, 16, 1, Format.PIXELS))
                        .option(Opts.doubleSlider(Config.SCALE, 0.1, 3.0, 0.1, Format.PERCENT))
                        .group(gr("elements")
                                .option(Opts.string(Config.ELEMENT_ORDER))
                                .option(Opts.intSlider(Config.ELEMENT_PADDING_LEFT, 0, 32, 1, Format.PIXELS))
                                .option(Opts.intSlider(Config.ELEMENT_PADDING_RIGHT, 0, 32, 1, Format.PIXELS))
                                .option(Opts.intSlider(Config.ELEMENT_PADDING_TOP, 0, 16, 1, Format.PIXELS))
                                .option(Opts.intSlider(Config.ELEMENT_PADDING_BOTTOM, 0, 16, 1, Format.PIXELS))
                                .build())
                        .build())
                .category(cat("effects")
                        .option(Opts.doubleSlider(Config.FADE_IN_TIME, 0, 5, 0.1, Format.SECONDS))
                        .option(Opts.doubleSlider(Config.FADE_OUT_TIME, 0, 5, 0.1, Format.SECONDS))
                        .option(Opts.easing(Config.FADE_IN_EASING))
                        .option(Opts.easing(Config.FADE_OUT_EASING))
                        .group(gr("pulse")
                                .option(Opts.doubleSlider(Config.PULSE_STRENGTH, 0, 10, 0.1, Format.PERCENT))
                                .option(Opts.doubleSlider(Config.PULSE_TIME, 0, 5, 0.1, Format.SECONDS))
                                .option(Opts.doubleSlider(Config.PULSE_PEAK, 0, 1, 0.05, Format.RAW))
                                .option(Opts.easing(Config.PULSE_EASE_IN))
                                .option(Opts.easing(Config.PULSE_EASE_OUT))
                                .build())
                        .group(gr("specialEffects")
                                .option(Opts.bool(Config.RAY_GLOW_ENABLED))
                                .build())
                        .build())
                .category(cat("tracking")
                        .option(LabelOption.create(Component
                                .translatable("config.loot_journal.option.tracking.label")
                                .withStyle(Style.EMPTY.withColor(ChatFormatting.GRAY))))
                        .option(Opts.bool(Config.TRACK_ITEM_PICKUPS))
                        .option(Opts.bool(Config.TRACK_XP_PICKUPS))
                        .option(Opts.bool(Config.ENABLE_PLAYER_FILTERING))
                        .group(Opts.list(Config.PLAYER_WHITELIST, "Nickname"))
                        .build())
                .category(cat("sounds")
                        .option(Opts.bool(Config.ENABLE_SOUNDS))
                        .option(Opts.sound(Config.SOUND_ID))
                        .option(Opts.doubleSlider(Config.SOUND_VOLUME, 0, 1, 0.05, Format.PERCENT))
                        .option(Opts.doubleSlider(Config.SOUND_PITCH, 0, 2, 0.05, Format.RAW))
                        .build())
                .category(cat("filtering")
                        .option(Opts.bool(Config.ENABLE_FILTERING))
                        .option(Opts.bool(Config.HIDE_YOUR_COMMON_ITEMS))
                        .option(Opts.bool(Config.HIDE_OTHER_COMMON_ITEMS))
                        .option(Opts.enumCycle(Config.DEFAULT_FILTER_RULE, DefaultFilterRule.class))
                        .option(Opts.openScreenButton("itemIdFilters", ConfigBuilder::createItemIdFiltersScreen))
                        .option(Opts.openScreenButton("itemTagFilters", ConfigBuilder::createItemTagFiltersScreen))
                        .option(Opts.openScreenButton("modIdFilters", ConfigBuilder::createModIdFiltersScreen))
                        .build())
                .save(ConfigBuilder::onSave)
                .build().generateScreen(parent);
    }

    private static ConfigCategory.Builder cat(String key) {
        return ConfigCategory.createBuilder()
                .name(tr("category." + key));
    }

    private static OptionGroup.Builder gr(String key) {
        return OptionGroup.createBuilder()
                .name(tr("group." + key))
                .description(Opts.description(tr("group." + key + ".desc")));
    }

    private static Component tr(String key) {
        return Component.translatable("config.loot_journal." + key);
    }

    private static Component tr(String key, Object... args) {
        return Component.translatable("config.loot_journal." + key, args);
    }

    private static ConfigCategory createThemesCategory() {
        var builder = cat("themes")
                .option(Opts.base(Config.THEME)
                        .controller(option -> DropdownStringControllerBuilder.create(option)
                                .values(ThemeRegistry.listThemeNames())
                                .allowAnyValue(false)
                                .allowEmptyValue(false))
                        .build())
                .option(LabelOption.create(Component
                        .translatable("config.loot_journal.option.activeTheme.label")
                        .withStyle(Style.EMPTY.withItalic(true).withColor(ChatFormatting.GRAY))));
        ThemeRegistry.stream().forEach(theme -> builder.group(createThemeOptions(theme)));
        return builder.build();
    }

    private static OptionGroup createThemeOptions(BakedTheme theme) {
        var builder = OptionGroup.createBuilder()
                .name(ThemeRegistry.isBuiltin(theme)
                        ? Component.literal(theme.displayName())
                        : Component.literal(theme.displayName())
                        .append(CommonComponents.SPACE)
                        .append(Component.literal("Custom").withStyle(ChatFormatting.DARK_GRAY)))
                .description(OptionDescription.of(
                        Component.literal(theme.description()).withStyle(ChatFormatting.GRAY),
                        CommonComponents.EMPTY,
                        Component.literal("ID: ").append(Component.literal(ThemeRegistry.getId(theme).toString()).withStyle(ChatFormatting.GRAY))));
        theme.theme.variables().forEach(variable -> builder.option(variable.createOption(theme)));
        return builder.collapsed(true).build();
    }

        private static Screen createItemIdFiltersScreen(Screen parent) {
                return createFilterListScreen(
                        parent,
                        "itemIdFilters",
                        Opts.itemIdList(Config.ITEM_ID_BLACKLIST),
                        Opts.itemIdList(Config.ITEM_ID_WHITELIST)
                );
        }

        private static Screen createItemTagFiltersScreen(Screen parent) {
                return createFilterListScreen(
                        parent,
                        "itemTagFilters",
                        Opts.itemTagList(Config.ITEM_TAG_BLACKLIST),
                        Opts.itemTagList(Config.ITEM_TAG_WHITELIST)
                );
        }

        private static Screen createModIdFiltersScreen(Screen parent) {
                return createFilterListScreen(
                        parent,
                        "modIdFilters",
                        Opts.modIdList(Config.MOD_ID_BLACKLIST),
                        Opts.modIdList(Config.MOD_ID_WHITELIST)
                );
        }

        private static Screen createFilterListScreen(
                Screen parent,
                String categoryKey,
                ListOption<String> blacklist,
                ListOption<String> whitelist
        ) {
                return YetAnotherConfigLib.createBuilder()
                        .title(tr("category." + categoryKey))
                        .category(cat(categoryKey)
                                .group(blacklist)
                                .group(whitelist)
                                .build())
                        .save(ConfigBuilder::onSave)
                        .build()
                        .generateScreen(parent);
        }

    private static void onSave() {
        Config.VALUES.forEach(ConfigValue::save);
        ConfigCache.refresh();
        ThemeRegistry.clearCache();
        ThemeRegistry.updateActiveTheme();
        ThemeRegistry.stream().forEach(BakedTheme::saveOverrides);
    }

    public final static class Opts {

        public static OptionDescription description(Component description) {
            return OptionDescription.of(description.copy().withStyle(ChatFormatting.GRAY));
        }

        private static ButtonOption openScreenButton(String key, Function<Screen, Screen> screenFactory) {
                return ButtonOption.createBuilder()
                        .name(tr("option." + key))
                        .description(description(tr("option." + key + ".desc")))
                        .action((screen, option) -> Minecraft.getInstance().setScreen(screenFactory.apply(screen)))
                        .build();
        }

        private static ListOption<String> itemIdList(ConfigValue<List<? extends String>> v) {
                return listDropdown(v, "minecraft:apple", () -> BuiltInRegistries.ITEM.keySet().stream()
                        .map(Identifier::toString)
                        .sorted()
                        .toList());
        }

        private static ListOption<String> itemTagList(ConfigValue<List<? extends String>> v) {
                return listDropdown(v, "minecraft:music_discs", () -> BuiltInRegistries.ITEM.getTags()
                        .map(named -> named.key().location())
                        .map(Identifier::toString)
                        .sorted()
                        .toList());
        }

        private static ListOption<String> modIdList(ConfigValue<List<? extends String>> v) {
                return listDropdown(v, "minecraft", () -> BuiltInRegistries.ITEM.keySet().stream()
                        .map(Identifier::getNamespace)
                        .distinct()
                        .sorted()
                        .toList());
        }

        private static ListOption<String> listDropdown(
                ConfigValue<List<? extends String>> v,
                String initial,
                Supplier<List<String>> values
        ) {
                return ListOption.<String>createBuilder()
                        .name(tr("option." + optionKey(v)))
                        .description(description(tr("option." + optionKey(v) + ".desc")))
                        .binding(bindList(v))
                        .controller(option -> DropdownStringControllerBuilder.create(option)
                                .values(values.get())
                                .allowAnyValue(false)
                                .allowEmptyValue(false))
                        .initial(initial)
                        .build();
        }

        private static String optionKey(ConfigValue<?> value) {
                if (value == Config.ENABLE_LOOT_JOURNAL) return "enableLootJournal";
                if (value == Config.SHOW_ITEM_PICKUPS) return "showItemPickups";
                if (value == Config.SHOW_XP_PICKUPS) return "showXpPickups";
                if (value == Config.SHOW_OVERFLOW_PICKUPS) return "showOverflowPickups";
                if (value == Config.ABBREVIATE_NUMBERS) return "abbreviateNumbers";
                if (value == Config.MERGE_MODE) return "mergeMode";
                if (value == Config.STACKING_MODE) return "stackingMode";
                if (value == Config.MAX_NAME_WIDTH) return "maxNameWidth";
                if (value == Config.DISPLAY_TIME) return "displayTime";
                if (value == Config.DISPLAY_CAPACITY) return "displayCapacity";
                if (value == Config.QUEUE_SIZE) return "queueSize";

                if (value == Config.THEME) return "activeTheme";
                if (value == Config.SCREEN_ANCHOR) return "screenAnchor";
                if (value == Config.GROWTH_DIRECTION) return "growthDirection";
                if (value == Config.ANCHOR_X_OFFSET) return "anchorXOffset";
                if (value == Config.ANCHOR_Y_OFFSET) return "anchorYOffset";
                if (value == Config.SEPARATION) return "separation";
                if (value == Config.SCALE) return "scale";
                if (value == Config.ELEMENT_ORDER) return "elementOrder";
                if (value == Config.ELEMENT_PADDING_LEFT) return "elementPaddingLeft";
                if (value == Config.ELEMENT_PADDING_RIGHT) return "elementPaddingRight";
                if (value == Config.ELEMENT_PADDING_TOP) return "elementPaddingTop";
                if (value == Config.ELEMENT_PADDING_BOTTOM) return "elementPaddingBottom";

                if (value == Config.FADE_IN_TIME) return "fadeInTime";
                if (value == Config.FADE_OUT_TIME) return "fadeOutTime";
                if (value == Config.FADE_IN_EASING) return "fadeInEasing";
                if (value == Config.FADE_OUT_EASING) return "fadeOutEasing";
                if (value == Config.PULSE_STRENGTH) return "pulseStrength";
                if (value == Config.PULSE_TIME) return "pulseTime";
                if (value == Config.PULSE_PEAK) return "pulsePeak";
                if (value == Config.PULSE_EASE_IN) return "pulseEaseIn";
                if (value == Config.PULSE_EASE_OUT) return "pulseEaseOut";
                if (value == Config.RAY_GLOW_ENABLED) return "enableRayGlow";

                if (value == Config.TRACK_ITEM_PICKUPS) return "trackItemPickups";
                if (value == Config.TRACK_XP_PICKUPS) return "trackXpPickups";
                if (value == Config.ENABLE_PLAYER_FILTERING) return "enablePlayerFiltering";
                if (value == Config.PLAYER_WHITELIST) return "playerWhitelist";

                if (value == Config.ENABLE_SOUNDS) return "enableSounds";
                if (value == Config.SOUND_ID) return "soundId";
                if (value == Config.SOUND_VOLUME) return "soundVolume";
                if (value == Config.SOUND_PITCH) return "soundPitch";

                if (value == Config.ENABLE_FILTERING) return "enableFiltering";
                if (value == Config.HIDE_YOUR_COMMON_ITEMS) return "hideYourCommonItems";
                if (value == Config.HIDE_OTHER_COMMON_ITEMS) return "hideOtherCommonItems";
                if (value == Config.DEFAULT_FILTER_RULE) return "defaultFilterRule";
                if (value == Config.ITEM_ID_BLACKLIST) return "itemIdBlacklist";
                if (value == Config.ITEM_ID_WHITELIST) return "itemIdWhitelist";
                if (value == Config.ITEM_TAG_BLACKLIST) return "itemTagBlacklist";
                if (value == Config.ITEM_TAG_WHITELIST) return "itemTagWhitelist";
                if (value == Config.MOD_ID_BLACKLIST) return "modIdBlacklist";
                if (value == Config.MOD_ID_WHITELIST) return "modIdWhitelist";

                return "unknown";
        }

        private static <T> Option.Builder<T> base(ConfigValue<T> v) {
            return Option.<T>createBuilder()
                    .name(tr("option." + optionKey(v)))
                    .description(description(tr("option." + optionKey(v) + ".desc")))
                    .binding(bind(v));
        }

        private static Option<Boolean> bool(ConfigValue<Boolean> v) {
            return base(v).controller(TickBoxControllerBuilder::create).build();
        }

        private static <T extends Enum<T>> Option<T> enumCycle(ConfigValue<T> v, Class<T> cls) {
            return base(v).controller(o -> EnumControllerBuilder.create(o).enumClass(cls)).build();
        }

        private static Option<Double> doubleSlider(ConfigValue<Double> v, double min, double max, double step, Format f) {
            return base(v).controller(o -> DoubleSliderControllerBuilder.create(o)
                    .range(min, max)
                    .step(step)
                    .formatValue(f.formatter)).build();
        }

        private static Option<Integer> intSlider(ConfigValue<Integer> v, int min, int max, int step, Format f) {
            return base(v).controller(o -> IntegerSliderControllerBuilder.create(o)
                    .range(min, max)
                    .step(step)
                    .formatValue(val -> f.formatter.format(val.doubleValue()))).build();
        }

        private static Option<String> string(ConfigValue<String> v) {
            return base(v).controller(StringControllerBuilder::create).build();
        }

        private static Option<String> easing(ConfigValue<Easing> v) {
            return Option.<String>createBuilder()
                    .name(tr("option." + optionKey(v)))
                    .description(description(tr("option." + optionKey(v) + ".desc")))
                    .binding(bind(v, Easing::name, Easing::valueOf))
                    .controller(o -> DropdownStringControllerBuilder.create(o)
                            .values(Arrays.stream(Easing.values()).map(Easing::name).toList()))
                    .build();
        }

        private static Option<String> sound(ConfigValue<String> v) {
            return base(v).controller(o -> DropdownStringControllerBuilder.create(o)
                    .values(BuiltInRegistries.SOUND_EVENT.stream()
                            .map(SoundEvent::location)
                            .map(Identifier::toString)
                            .toList())).build();
        }

        private static ListOption<String> list(ConfigValue<List<? extends String>> v, String initial) {
            return ListOption.<String>createBuilder()
                    .name(tr("option." + optionKey(v)))
                    .description(description(tr("option." + optionKey(v) + ".desc")))
                    .binding(bindList(v))
                    .controller(StringControllerBuilder::create)
                    .initial(initial)
                    .build();
        }

        private static <T> Binding<T> bind(ConfigValue<T> v) {
            return Binding.generic(v.getDefault(), v::get, v::set);
        }

        private static <T, V> Binding<V> bind(ConfigValue<T> v, Function<T, V> in, Function<V, T> out) {
            return Binding.generic(in.apply(v.getDefault()),
                    () -> in.apply(v.get()),
                    x -> v.set(out.apply(x)));
        }

        private static <T> Binding<List<T>> bindList(ConfigValue<List<? extends T>> v) {
            return Binding.generic(
                    new ArrayList<>(v.getDefault()),
                    () -> new ArrayList<>(v.get()),
                    x -> v.set(new ArrayList<>(x))
            );
        }
    }

    private enum Format {
        RAW(v -> Component.literal("%.1f".formatted(v))),
        SECONDS(v -> tr("format.seconds", "%.1f".formatted(v))),
        PERCENT(v -> tr("format.percent", "%.0f".formatted(v * 100))),
        PIXELS(v -> tr("format.pixels", v.intValue())),
        ENTRIES(v -> tr("format.entries", v.intValue()));

        private final ValueFormatter<Double> formatter;

        Format(ValueFormatter<Double> formatter) {
            this.formatter = formatter;
        }
    }
}