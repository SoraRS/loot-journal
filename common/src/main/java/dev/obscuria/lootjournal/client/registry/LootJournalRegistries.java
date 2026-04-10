package dev.obscuria.lootjournal.client.registry;

import com.mojang.serialization.Codec;
import dev.obscuria.fragmentum.registry.BootstrapContext;
import dev.obscuria.fragmentum.registry.DelegatedRegistry;
import dev.obscuria.fragmentum.registry.FragmentumRegistry;
import dev.obscuria.fragmentum.registry.Registrar;
import dev.obscuria.lootjournal.LootJournal;
import dev.obscuria.lootjournal.client.themes.match.PickupMatch;
import dev.obscuria.lootjournal.client.themes.Theme;
import dev.obscuria.lootjournal.client.themes.styles.PickupStyle;
import dev.obscuria.lootjournal.client.themes.styles.banners.PickupBanner;
import dev.obscuria.lootjournal.client.themes.styles.icons.PickupIcon;
import dev.obscuria.lootjournal.client.themes.styles.icons.effects.IconEffect;
import dev.obscuria.lootjournal.client.themes.styles.panels.PickupPanel;
import dev.obscuria.lootjournal.client.themes.variables.Variable;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public interface LootJournalRegistries {

    Registrar REGISTRAR = FragmentumRegistry.registrar(LootJournal.MODID);

    DelegatedRegistry<Codec<? extends Variable<?>>> THEME_VARIABLE_TYPE = REGISTRAR.createRegistry(Key.THEME_VARIABLE_TYPE);
    DelegatedRegistry<Codec<? extends PickupPanel>> PICKUP_PANEL_TYPE = REGISTRAR.createRegistry(Key.PICKUP_PANEL_TYPE);
    DelegatedRegistry<Codec<? extends PickupBanner>> PICKUP_BANNER_TYPE = REGISTRAR.createRegistry(Key.PICKUP_BANNER_TYPE);
    DelegatedRegistry<Codec<? extends PickupIcon>> PICKUP_ICON_TYPE = REGISTRAR.createRegistry(Key.PICKUP_ICON_TYPE);
    DelegatedRegistry<Codec<? extends PickupMatch>> PICKUP_MATCH_TYPE = REGISTRAR.createRegistry(Key.PICKUP_MATCH_TYPE);
    DelegatedRegistry<Codec<? extends IconEffect>> ICON_EFFECT_TYPE = REGISTRAR.createRegistry(Key.ICON_EFFECT_TYPE);

    interface Resource {

        ResourceRegistry<PickupStyle> PICKUP_STYLE = new ResourceRegistry<>("style");
        ResourceRegistry<Theme> THEME = new ResourceRegistry<>("theme");
    }

    interface Key {

        ResourceKey<Registry<Codec<? extends Variable<?>>>> THEME_VARIABLE_TYPE = create("theme_variable_type");
        ResourceKey<Registry<Codec<? extends PickupPanel>>> PICKUP_PANEL_TYPE = create("pickup_panel_type");
        ResourceKey<Registry<Codec<? extends PickupBanner>>> PICKUP_BANNER_TYPE = create("pickup_banner_type");
        ResourceKey<Registry<Codec<? extends PickupIcon>>> PICKUP_ICON_TYPE = create("pickup_icon_type");
        ResourceKey<Registry<Codec<? extends PickupMatch>>> PICKUP_MATCH_TYPE = create("pickup_match_type");
        ResourceKey<Registry<Codec<? extends IconEffect>>> ICON_EFFECT_TYPE = create("icon_effect_type");

        private static <T> ResourceKey<Registry<T>> create(String name) {
            return ResourceKey.createRegistryKey(LootJournal.identifier(name));
        }
    }

    static void init() {

        Variable.bootstrap(BootstrapContext.create(REGISTRAR, Key.THEME_VARIABLE_TYPE, ResourceLocation::new));
        PickupPanel.bootstrap(BootstrapContext.create(REGISTRAR, Key.PICKUP_PANEL_TYPE, ResourceLocation::new));
        PickupBanner.bootstrap(BootstrapContext.create(REGISTRAR, Key.PICKUP_BANNER_TYPE, ResourceLocation::new));
        PickupIcon.bootstrap(BootstrapContext.create(REGISTRAR, Key.PICKUP_ICON_TYPE, ResourceLocation::new));
        PickupMatch.bootstrap(BootstrapContext.create(REGISTRAR, Key.PICKUP_MATCH_TYPE, ResourceLocation::new));
        IconEffect.bootstrap(BootstrapContext.create(REGISTRAR, Key.ICON_EFFECT_TYPE, ResourceLocation::new));
    }
}
