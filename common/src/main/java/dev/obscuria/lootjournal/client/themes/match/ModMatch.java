package dev.obscuria.lootjournal.client.themes.match;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.ItemStack;

public record ModMatch(String value) implements ItemStackMatch {

    public static final String NAME = "mod";
    public static final Codec<ModMatch> CODEC;

    @Override
    public Codec<ModMatch> codec() {
        return CODEC;
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean matches(ItemStack stack) {
        return stack.getItem().builtInRegistryHolder().key().identifier().getNamespace().equals(value);
    }

    static {
        CODEC = RecordCodecBuilder.create(codec -> codec.group(
                Codec.STRING.fieldOf(NAME).forGetter(ModMatch::value)
        ).apply(codec, ModMatch::new));
    }
}
