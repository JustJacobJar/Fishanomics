package com.justexisting1.fishanomics.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 * Wrapper to pass to the generator
 */
public class ModLootProvider {
    public static LootTableProvider create(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        return new LootTableProvider(
                output,
                Set.of(), // We don't need to specify "required" tables here
                List.of(
                        new LootTableProvider.SubProviderEntry(ModBlockLootTableSubProvider::new, LootContextParamSets.BLOCK),
                        new LootTableProvider.SubProviderEntry(ModFishingLootSubProvider::new,LootContextParamSets.FISHING // Tells MC these tables use Fishing context (tool, player, etc)
                )),
                registries
        );
    }
}
