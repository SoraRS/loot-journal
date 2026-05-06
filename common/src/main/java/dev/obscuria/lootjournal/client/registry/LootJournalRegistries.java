package dev.obscuria.lootjournal.client.registry;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.Codec;
import dev.obscuria.fragmentum.content.registry.BootstrapContext;
import dev.obscuria.fragmentum.content.registry.FragmentumRegistry;
import dev.obscuria.fragmentum.content.registry.Registrar;
import dev.obscuria.lootjournal.LootJournal;
import dev.obscuria.lootjournal.client.themes.Theme;
import dev.obscuria.lootjournal.client.themes.match.PickupMatch;
import dev.obscuria.lootjournal.client.themes.styles.PickupStyle;
import dev.obscuria.lootjournal.client.themes.styles.banners.PickupBanner;
import dev.obscuria.lootjournal.client.themes.styles.icons.PickupIcon;
import dev.obscuria.lootjournal.client.themes.styles.icons.effects.IconEffect;
import dev.obscuria.lootjournal.client.themes.styles.panels.PickupPanel;
import dev.obscuria.lootjournal.client.themes.variables.Variable;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public interface LootJournalRegistries {
    Registrar REGISTRAR = FragmentumRegistry.registrar(LootJournal.MODID);

    Registry<MapCodec<? extends Variable<?>>> THEME_VARIABLE_TYPE = REGISTRAR.createRegistry(Key.THEME_VARIABLE_TYPE);
    Registry<MapCodec<? extends PickupPanel>> PICKUP_PANEL_TYPE = REGISTRAR.createRegistry(Key.PICKUP_PANEL_TYPE);
    Registry<MapCodec<? extends PickupBanner>> PICKUP_BANNER_TYPE = REGISTRAR.createRegistry(Key.PICKUP_BANNER_TYPE);
    Registry<MapCodec<? extends PickupIcon>> PICKUP_ICON_TYPE = REGISTRAR.createRegistry(Key.PICKUP_ICON_TYPE);
    Registry<Codec<? extends PickupMatch>> PICKUP_MATCH_TYPE = REGISTRAR.createRegistry(Key.PICKUP_MATCH_TYPE);
    Registry<MapCodec<? extends IconEffect>> ICON_EFFECT_TYPE = REGISTRAR.createRegistry(Key.ICON_EFFECT_TYPE);

    interface Resource {
        ResourceRegistry<PickupStyle> PICKUP_STYLE = new ResourceRegistry<>("style");
        ResourceRegistry<Theme> THEME = new ResourceRegistry<>("theme");
    }

    interface Key {
        ResourceKey<Registry<MapCodec<? extends Variable<?>>>> THEME_VARIABLE_TYPE = create("theme_variable_type");
        ResourceKey<Registry<MapCodec<? extends PickupPanel>>> PICKUP_PANEL_TYPE = create("pickup_panel_type");
        ResourceKey<Registry<MapCodec<? extends PickupBanner>>> PICKUP_BANNER_TYPE = create("pickup_banner_type");
        ResourceKey<Registry<MapCodec<? extends PickupIcon>>> PICKUP_ICON_TYPE = create("pickup_icon_type");
        ResourceKey<Registry<Codec<? extends PickupMatch>>> PICKUP_MATCH_TYPE = create("pickup_match_type");
        ResourceKey<Registry<MapCodec<? extends IconEffect>>> ICON_EFFECT_TYPE = create("icon_effect_type");

        private static <T> ResourceKey<Registry<T>> create(String name) {
            return ResourceKey.createRegistryKey(LootJournal.identifier(name));
        }
    }

    static void init() {
        Variable.bootstrap(BootstrapContext.create(REGISTRAR, Key.THEME_VARIABLE_TYPE, LootJournal::identifier));
        PickupPanel.bootstrap(BootstrapContext.create(REGISTRAR, Key.PICKUP_PANEL_TYPE, LootJournal::identifier));
        PickupBanner.bootstrap(BootstrapContext.create(REGISTRAR, Key.PICKUP_BANNER_TYPE, LootJournal::identifier));
        PickupIcon.bootstrap(BootstrapContext.create(REGISTRAR, Key.PICKUP_ICON_TYPE, LootJournal::identifier));
        PickupMatch.bootstrap(BootstrapContext.create(REGISTRAR, Key.PICKUP_MATCH_TYPE, LootJournal::identifier));
        IconEffect.bootstrap(BootstrapContext.create(REGISTRAR, Key.ICON_EFFECT_TYPE, LootJournal::identifier));
    }
}
