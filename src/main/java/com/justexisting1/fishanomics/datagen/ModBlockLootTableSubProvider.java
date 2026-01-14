package com.justexisting1.fishanomics.datagen;

import com.justexisting1.fishanomics.block.FishanomicsBlocks;
import com.justexisting1.fishanomics.item.FishanomicItems;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;

import java.util.Set;

public class ModBlockLootTableSubProvider extends BlockLootSubProvider {

    protected ModBlockLootTableSubProvider(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected void generate() {
        //Blocks that drop self
        dropSelf(FishanomicsBlocks.FISH_FURNACE.get());
        dropSelf(FishanomicsBlocks.FISH_TANK.get());

        //Blocks that have specific drops
        //Block -> Block, Drop
        add(FishanomicsBlocks.FISH_TANK.get(),
                block -> createOreDrop(FishanomicsBlocks.FISH_TANK.get(), FishanomicItems.WOODEN_FISHING_ROD.get()));   //ore loot table

    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return FishanomicsBlocks.BLOCKS.getEntries().stream().map(Holder::value)::iterator;
    }
}
