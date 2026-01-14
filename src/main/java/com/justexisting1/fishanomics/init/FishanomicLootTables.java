package com.justexisting1.fishanomics.init;

import com.justexisting1.fishanomics.Fishanomics;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.entries.NestedLootTable;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.LootTableLoadEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

public class FishanomicLootTables {

    //Why these exist?
    //These are here as hardcoded paths that get used in my generators to actually make the table.
    //The only difference between hand writing these paths and not, is that these are stored against a variable.
    //This allows me to use these as default values for my default fishing rods that I will similarly make
    public static final ResourceKey<LootTable> FISH = register("fish");
    public static final ResourceKey<LootTable> RESOURCE_BLOCKS = register("resource_blocks");
    public static final ResourceKey<LootTable> WOODEN_ROD = registerRod("wooden_rod");
    public static final ResourceKey<LootTable> STONE_ROD = registerRod("stone_rod");
    public static final ResourceKey<LootTable> IRON_ROD = registerRod("iron_rod");

    private static final String BASE_PATH = "fishing/";
    private static final String BASE_PATH_ROD = "fishing/rod_tables/";

    private static ResourceKey<LootTable> register(String path){
        return ResourceKey.create(Registries.LOOT_TABLE,
                ResourceLocation.fromNamespaceAndPath(Fishanomics.MOD_ID,  BASE_PATH + path));
    }

    private static ResourceKey<LootTable> registerRod(String path){
        return ResourceKey.create(Registries.LOOT_TABLE,
                ResourceLocation.fromNamespaceAndPath(Fishanomics.MOD_ID,  BASE_PATH_ROD + path));
    }

}
