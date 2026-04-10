package dev.obscuria.lootjournal.forge.mixin;

import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;

// Because of the well-designed Forge
@Mixin(value = MinecraftForge.class, remap = false)
public abstract class MixinDummy {}
