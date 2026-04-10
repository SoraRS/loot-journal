package dev.obscuria.lootjournal.client.themes.styles.banners;

import com.mojang.serialization.Codec;
import dev.obscuria.lootjournal.client.renderer.PickupRenderer;
import net.minecraft.client.gui.GuiGraphics;

public record NoneBanner() implements PickupBanner {

    public static final Codec<NoneBanner> CODEC = Codec.unit(NoneBanner::new);
    public static final NoneBanner DEFAULT = new NoneBanner();

    @Override
    public Codec<NoneBanner> codec() {
        return CODEC;
    }

    @Override
    public void render(GuiGraphics graphics, PickupRenderer pickup) {}
}
