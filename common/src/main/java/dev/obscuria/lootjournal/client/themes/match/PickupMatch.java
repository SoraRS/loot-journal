package dev.obscuria.lootjournal.client.themes.match;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import dev.obscuria.fragmentum.content.registry.BootstrapContext;
import dev.obscuria.lootjournal.LootJournal;
import dev.obscuria.lootjournal.client.events.PickupEvent;
import dev.obscuria.lootjournal.client.registry.LootJournalRegistries;
import dev.obscuria.lootjournal.client.registry.SmartDispatchCodec;
import net.minecraft.resources.Identifier;

import java.util.function.Function;

public interface PickupMatch {

    Codec<Codec<? extends PickupMatch>> TYPE_CODEC = Codec.STRING.comapFlatMap(name -> {
        var id = Identifier.tryParse(name);

        if (id == null) {
            return DataResult.error(() -> "Invalid pickup match type: " + name);
        }

        /*
         * Loot Journal's bundled theme JSON uses compact Fragmentum-style match syntax:
         *
         *   "match": { "always": true }
         *   "match": { "rarity": "rare" }
         *   "match": { "is_xp": true }
         *
         * In modern Minecraft, bare identifiers are decoded as minecraft:*.
         * These match types are registered under loot_journal:*, so bare keys need to
         * resolve to the Loot Journal namespace for backwards-compatible resource data.
         */
        if ("minecraft".equals(id.getNamespace())) {
            id = LootJournal.identifier(id.getPath());
        }

        var resolvedId = id;

        return LootJournalRegistries.PICKUP_MATCH_TYPE.get(resolvedId)
                .map(reference -> DataResult.success(reference.value()))
                .orElseGet(() -> DataResult.error(() -> "Unknown pickup match type: " + resolvedId));
    }, codec -> "unknown");

    Codec<PickupMatch> CODEC = SmartDispatchCodec.create(
            TYPE_CODEC,
            PickupMatch::codec,
            Function.identity()
    );

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
