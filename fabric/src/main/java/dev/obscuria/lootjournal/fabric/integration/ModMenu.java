package dev.obscuria.lootjournal.fabric.integration;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.obscuria.lootjournal.config.ConfigBuilder;

public final class ModMenu implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return ConfigBuilder::createConfigScreen;
    }
}