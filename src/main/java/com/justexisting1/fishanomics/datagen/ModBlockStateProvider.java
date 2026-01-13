package com.justexisting1.fishanomics.datagen;

import com.justexisting1.fishanomics.Fishanomics;
import com.justexisting1.fishanomics.block.ModBlocks;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, Fishanomics.MOD_ID, exFileHelper);
    }

    /**
     * Generates
     * BlockState.json
     * BlockModel.json
     * ItemModel.json
     */
    @Override
    protected void registerStatesAndModels() {
        //Add models here
        blockWithItem(ModBlocks.FISH_FURNACE);
        blockWithItem(ModBlocks.FISH_TANK);
    }

    /**
     * Makes a block with all faces the same
     */
    private void blockWithItem(DeferredBlock<?> deferredBlock){
        simpleBlockWithItem(deferredBlock.get(), cubeAll(deferredBlock.get()));
    }
}
