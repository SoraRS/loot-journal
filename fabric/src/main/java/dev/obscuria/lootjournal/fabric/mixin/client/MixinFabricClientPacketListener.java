package dev.obscuria.lootjournal.fabric.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.obscuria.lootjournal.LootJournalHelper;
import dev.obscuria.lootjournal.config.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundTakeItemEntityPacket;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public abstract class MixinFabricClientPacketListener {

    @Shadow
    private ClientLevel level;

    @Inject(
            method = "handleTakeItemEntity",
            at = @At("HEAD")
    )
    private void lootJournal$onTakeItemEntity(ClientboundTakeItemEntityPacket packet, CallbackInfo info) {
        if (!Minecraft.getInstance().isSameThread()) return;
        LootJournalHelper.handlePickupPacket(level, packet);
    }

    @WrapOperation(
            method = "handleTakeItemEntity",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/multiplayer/ClientLevel;playLocalSound(DDDLnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FFZ)V"
            ),
            require = 0
    )
    private void lootJournal$modifyPickupSound(
            ClientLevel client,
            double x,
            double y,
            double z,
            SoundEvent sound,
            SoundSource category,
            float volume,
            float pitch,
            boolean distanceDelay,
            Operation<Void> original
    ) {
        if (sound == SoundEvents.ITEM_PICKUP) {
            if (!Config.ENABLE_SOUNDS.get()) return;

            @Nullable var soundId = Identifier.tryParse(Config.SOUND_ID.get());
            var actualSound = soundId == null ? sound : SoundEvent.createVariableRangeEvent(soundId);
            var actualVolume = Config.SOUND_VOLUME.get().floatValue();
            var actualPitch = Config.SOUND_PITCH.get().floatValue();

            original.call(client, x, y, z, actualSound, category, actualVolume, actualPitch, distanceDelay);
        } else {
            original.call(client, x, y, z, sound, category, volume, pitch, distanceDelay);
        }
    }
}
