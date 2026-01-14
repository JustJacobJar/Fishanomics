package com.justexisting1.fishanomics.datagen;

import com.justexisting1.fishanomics.Fishanomics;
import com.justexisting1.fishanomics.block.FishanomicsBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends BlockTagsProvider {
    public ModBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, Fishanomics.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {

        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(FishanomicsBlocks.FISH_FURNACE.get())
                .add(FishanomicsBlocks.FISH_TANK.get());


        tag(BlockTags.NEEDS_STONE_TOOL)
                .add(FishanomicsBlocks.FISH_FURNACE.get());

    }
}
