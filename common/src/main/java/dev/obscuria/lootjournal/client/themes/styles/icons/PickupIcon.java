package dev.obscuria.lootjournal.client.themes.styles.icons;

import com.mojang.serialization.Codec;
import dev.obscuria.fragmentum.registry.BootstrapContext;
import dev.obscuria.lootjournal.client.themes.styles.vars.Var;
import dev.obscuria.lootjournal.client.registry.LootJournalRegistries;
import dev.obscuria.lootjournal.client.renderer.PickupRenderer;
import net.minecraft.client.gui.GuiGraphics;

import java.util.function.Function;

public interface PickupIcon {

    Codec<PickupIcon> CODEC = LootJournalRegistries
            .PICKUP_ICON_TYPE.byNameCodec()
            .dispatch(PickupIcon::codec, Function.identity());

    Codec<? extends PickupIcon> codec();

    void render(GuiGraphics graphics, PickupRenderer renderer);

    Var<Integer> paddingLeft();

    Var<Integer> paddingRight();

    static void bootstrap(BootstrapContext<Codec<? extends PickupIcon>> context) {
        context.register("simple", () -> SimpleIcon.CODEC);
    }
}
