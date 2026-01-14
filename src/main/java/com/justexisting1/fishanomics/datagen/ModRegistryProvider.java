package com.justexisting1.fishanomics.datagen;

import com.justexisting1.fishanomics.Fishanomics;
import com.justexisting1.fishanomics.fishingrods.FishingRodProperties;
import com.justexisting1.fishanomics.init.FishanomicLootTables;
import com.justexisting1.fishanomics.item.FishanomicItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.PackOutput;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.LootTable;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.registries.DeferredItem;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 * This is where Data Components are registered.
 * Currently used for custom fishing rod data
 */
public class ModRegistryProvider extends DatapackBuiltinEntriesProvider {

    public ModRegistryProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(Fishanomics.MOD_ID));
    }

    /**
     * Registers data component for fishing rods
     * @param context the builder context
     * @param item The deferred Item
     * @param lootTable The loot table resource key
     * @param durability undecided, also pass into constructor, but could be here only
     * @param lure see above
     */
    private static void registerRod(BootstrapContext<FishingRodProperties> context,
                                    DeferredItem<Item> item,
                                    ResourceKey<LootTable> lootTable,
                                    int durability,
                                    int lure) {
        // 1. Get the path string from the DeferredItem (e.g., "wooden_fishing_rod")
        String name = item.getId().getPath();

        // 2. Create the Key for the registry entry using that path
        ResourceKey<FishingRodProperties> key = ResourceKey.create(
                FishingRodProperties.FISHING_ROD_REGISTRY_KEY,
                ResourceLocation.fromNamespaceAndPath(Fishanomics.MOD_ID, name)
        );

        // 3. Register the properties
        context.register(key, new FishingRodProperties(
                ResourceLocation.fromNamespaceAndPath(Fishanomics.MOD_ID, name),
                lootTable.location(),
                durability,
                lure
        ));
    }

    //TODO: Consider where to pass in durability and lure

    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(FishingRodProperties.FISHING_ROD_REGISTRY_KEY, context -> {
                // Register all your default rods here
                registerRod(context, FishanomicItems.WOODEN_FISHING_ROD, FishanomicLootTables.WOODEN_ROD, 59, 100);

                // Register your "Obsidian" rod here
                //TODO: When registering new items that My mod will have by default, I should instead define
                //the resouce keys inside the item class, and reference them via that.
                //This may initially cause some issues with my data gen as my items will then all be types of a single custom item
                //This will cause errors with the models and states and all that stuff, so will need to workout adding
                //the model path here, and then making that generated in a different way too
                context.register(
                        ResourceKey.create(FishingRodProperties.FISHING_ROD_REGISTRY_KEY,
                                ResourceLocation.fromNamespaceAndPath(Fishanomics.MOD_ID, "obi")),
                        new FishingRodProperties(
                                ResourceLocation.fromNamespaceAndPath(Fishanomics.MOD_ID, "obi"), //fishingRod
                                FishanomicLootTables.IRON_ROD.location(), //Loot table
                                5,   // Durability
                                120  // Lure
                        )
                );
            });
}
