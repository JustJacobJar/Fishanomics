package com.justexisting1.fishanomics.datagen;

import com.justexisting1.fishanomics.Fishanomics;
import com.justexisting1.fishanomics.item.FishanomicItems;
import com.justexisting1.fishanomics.util.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends ItemTagsProvider {
    public ModItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTags, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags, Fishanomics.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(ModTags.Items.SMELTABLE_ITEMS)
                .add(FishanomicItems.COAL_FISH.get())
                .add(FishanomicItems.IRON_FISH.get());

    }
}
