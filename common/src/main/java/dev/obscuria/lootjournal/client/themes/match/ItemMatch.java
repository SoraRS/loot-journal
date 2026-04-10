package dev.obscuria.lootjournal.client.themes.match;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public record ItemMatch(Item value) implements ItemStackMatch {

    public static final String NAME = "item";
    public static final Codec<ItemMatch> CODEC;

    @Override
    public Codec<ItemMatch> codec() {
        return CODEC;
    }

    @Override
    public boolean matches(ItemStack stack) {
        return stack.getItem() == value;
    }

    static {
        CODEC = RecordCodecBuilder.create(codec -> codec.group(
                BuiltInRegistries.ITEM.byNameCodec().fieldOf(NAME).forGetter(ItemMatch::value)
        ).apply(codec, ItemMatch::new));
    }
}
