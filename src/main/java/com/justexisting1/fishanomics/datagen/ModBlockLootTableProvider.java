package com.justexisting1.fishanomics.datagen;

import com.justexisting1.fishanomics.block.ModBlocks;
import com.justexisting1.fishanomics.item.FishanomicItems;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;

import java.util.Set;

public class ModBlockLootTableProvider extends BlockLootSubProvider {

    protected ModBlockLootTableProvider(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected void generate() {
        //Blocks that drop self
        dropSelf(ModBlocks.FISH_FURNACE.get());
        dropSelf(ModBlocks.FISH_TANK.get());

        //Blocks that have specific drops
        //Block -> Block, Drop
        add(ModBlocks.FISH_TANK.get(),
                block -> createOreDrop(ModBlocks.FISH_TANK.get(), FishanomicItems.WOODEN_FISHING_ROD.get()));   //ore loot table

    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(Holder::value)::iterator;
    }
}
