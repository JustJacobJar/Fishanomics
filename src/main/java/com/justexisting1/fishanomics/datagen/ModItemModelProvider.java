package com.justexisting1.fishanomics.datagen;

import com.justexisting1.fishanomics.Fishanomics;
import com.justexisting1.fishanomics.item.FishanomicItems;
import net.minecraft.client.renderer.block.model.ItemModelGenerator;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.ModelProvider;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ModItemModelProvider extends ItemModelProvider {

    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Fishanomics.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        //Fishing Rods

        fishingRod(FishanomicItems.WOODEN_FISHING_ROD.get());
        fishingRod(FishanomicItems.STONE_FISHING_ROD.get());
        fishingRod(FishanomicItems.IRON_FISHING_ROD.get());
        fishingRod(FishanomicItems.GOLD_FISHING_ROD.get());
        fishingRod(FishanomicItems.DIAMOND_FISHING_ROD.get());
        fishingRod(FishanomicItems.NETHERITE_FISHING_ROD.get());

        //Fish Ores
        basicItem(FishanomicItems.COAL_FISH.get());
        basicItem(FishanomicItems.COPPER_FISH.get());
        basicItem(FishanomicItems.IRON_FISH.get());
        basicItem(FishanomicItems.GOLD_FISH.get());
        basicItem(FishanomicItems.LAPIS_FISH.get());
        basicItem(FishanomicItems.REDSTONE_FISH.get());
        basicItem(FishanomicItems.DIAMOND_FISH.get());
    }

    private void fishingRod(Item item) {
        String name = BuiltInRegistries.ITEM.getKey(item).getPath();

        // Cast model
        ModelFile castModel = withExistingParent(
                name + "_cast",
                mcLoc("item/handheld_rod")
        ).texture("layer0", modLoc("item/" + name + "_cast"));

        // Base (uncast) model
        withExistingParent(
                name,
                mcLoc("item/handheld_rod")
        )
                .texture("layer0", modLoc("item/" + name + "_uncast"))
                .override()
                .predicate(mcLoc("cast"), 1.0f)
                .model(castModel)
                .end();
    }


//    protected void simpleOverrideItem(
//            Item item,
//            String parent,
//            String predicate,
//            float value,
//            String suffix
//    ) {
//        String name = BuiltInRegistries.ITEM.getKey(item).getPath();
//
//        ModelFile overrideModel = withExistingParent(
//                name + suffix,
//                mcLoc(parent)
//        ).texture("layer0", modLoc("item/" + name + suffix));
//
//        withExistingParent(name, mcLoc(parent))
//                .texture("layer0", modLoc("item/" + name))
//                .override()
//                .predicate(mcLoc(predicate), value)
//                .model(overrideModel)
//                .end();
//    }

// Usage
//    simpleOverrideItem(
//            ModItems.MY_FISHING_ROD.get(),
//    "item/handheld_rod",
//            "cast",
//            1.0f,
//            "_cast"
//            );
}

