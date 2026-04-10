package dev.obscuria.lootjournal.client.themes.match;

import com.mojang.serialization.Codec;
import dev.obscuria.fragmentum.registry.BootstrapContext;
import dev.obscuria.lootjournal.client.events.PickupEvent;
import dev.obscuria.lootjournal.client.registry.LootJournalRegistries;
import dev.obscuria.lootjournal.client.registry.SmartDispatchCodec;

import java.util.function.Function;

public interface PickupMatch {

    Codec<PickupMatch> CODEC = SmartDispatchCodec.create(
            LootJournalRegistries.PICKUP_MATCH_TYPE.byNameCodec(),
            PickupMatch::codec, Function.identity());

    Codec<? extends PickupMatch> codec();

    boolean matches(PickupEvent pickupEvent);

    static void bootstrap(BootstrapContext<Codec<? extends PickupMatch>> context) {
        context.register(AlwaysMatch.NAME, () -> AlwaysMatch.CODEC);
        context.register(AllOfMatch.NAME, () -> AllOfMatch.CODEC);
        context.register(AnyOfMatch.NAME, () -> AnyOfMatch.CODEC);
        context.register(NoneOfMatch.NAME, () -> NoneOfMatch.CODEC);
        context.register(IsItemMatch.NAME, () -> IsItemMatch.CODEC);
        context.register(IsXPMatch.NAME, () -> IsXPMatch.CODEC);
        context.register(ItemMatch.NAME, () -> ItemMatch.CODEC);
        context.register(ItemTagMatch.NAME, () -> ItemTagMatch.CODEC);
        context.register(ModMatch.NAME, () -> ModMatch.CODEC);
        context.register(RarityMatch.NAME, () -> RarityMatch.CODEC);
    }
}
