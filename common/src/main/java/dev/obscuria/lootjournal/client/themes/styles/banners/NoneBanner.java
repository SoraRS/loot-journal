package dev.obscuria.lootjournal.client.themes.styles.banners;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import dev.obscuria.lootjournal.client.renderer.PickupRenderer;
import net.minecraft.client.gui.GuiGraphics;

public record NoneBanner() implements PickupBanner {

    public static final NoneBanner DEFAULT = new NoneBanner();

    public static final MapCodec<NoneBanner> MAP_CODEC = MapCodec.unit(NoneBanner::new);
    public static final Codec<NoneBanner> CODEC = MAP_CODEC.codec();

    @Override
    public MapCodec<? extends PickupBanner> codec() {
        return MAP_CODEC;
    }

    @Override
    public void render(GuiGraphics graphics, PickupRenderer pickup) {
    }
}