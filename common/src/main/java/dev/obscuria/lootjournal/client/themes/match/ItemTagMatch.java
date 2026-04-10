package dev.obscuria.lootjournal.client.themes.match;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public record ItemTagMatch(TagKey<Item> value) implements ItemStackMatch {

    public static final String NAME = "item_tag";
    public static final Codec<ItemTagMatch> CODEC;

    @Override
    public Codec<ItemTagMatch> codec() {
        return CODEC;
    }

    @Override
    public boolean matches(ItemStack stack) {
        return stack.is(value);
    }

    static {
        CODEC = RecordCodecBuilder.create(codec -> codec.group(
                TagKey.codec(Registries.ITEM).fieldOf(NAME).forGetter(ItemTagMatch::value)
        ).apply(codec, ItemTagMatch::new));
    }
}
