package dev.obscuria.lootjournal.client.themes.styles.banners;

import com.mojang.serialization.Codec;
import dev.obscuria.fragmentum.registry.BootstrapContext;
import dev.obscuria.lootjournal.client.registry.LootJournalRegistries;
import dev.obscuria.lootjournal.client.renderer.PickupRenderer;
import net.minecraft.client.gui.GuiGraphics;

import java.util.function.Function;

public interface PickupBanner {

    Codec<PickupBanner> CODEC = LootJournalRegistries
            .PICKUP_BANNER_TYPE.byNameCodec()
            .dispatch(PickupBanner::codec, Function.identity());

    Codec<? extends PickupBanner> codec();

    void render(GuiGraphics graphics, PickupRenderer pickup);

    static void bootstrap(BootstrapContext<Codec<? extends PickupBanner>> context) {
        context.register("none", () -> NoneBanner.CODEC);
        context.register("texture", () -> TextureBanner.CODEC);
    }
}
