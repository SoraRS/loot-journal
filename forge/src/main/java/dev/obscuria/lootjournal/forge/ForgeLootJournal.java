package dev.obscuria.lootjournal.forge;

import dev.obscuria.lootjournal.LootJournal;
import dev.obscuria.lootjournal.forge.client.ForgeLootJournalClient;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;

@Mod(LootJournal.MODID)
public final class ForgeLootJournal {

    public ForgeLootJournal() {
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ForgeLootJournalClient::init);
    }
}