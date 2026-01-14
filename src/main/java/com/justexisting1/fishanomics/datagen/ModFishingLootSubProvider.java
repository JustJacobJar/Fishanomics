package com.justexisting1.fishanomics.datagen;

import com.justexisting1.fishanomics.init.FishanomicLootTables;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.entries.NestedLootTable;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.function.BiConsumer;

public class ModFishingLootSubProvider implements LootTableSubProvider {

    private final HolderLookup.Provider registries;

    public ModFishingLootSubProvider(HolderLookup.Provider registries) {
        this.registries = registries;
    }


    @Override
    public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {

        var enchantmentLookup = registries.lookupOrThrow(Registries.ENCHANTMENT);
        // 1. Generate standard fish pool
        //Pass in resource key (location) + loot table builder
        output.accept(FishanomicLootTables.FISH,
                LootTable.lootTable().withPool(
                        LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1))
                                .add(LootItem.lootTableItem(Items.COD).setWeight(60))
                                .add(LootItem.lootTableItem(Items.SALMON).setWeight(30))
                                .add(LootItem.lootTableItem(Items.PUFFERFISH).setWeight(5))
                                .add(LootItem.lootTableItem(Items.TROPICAL_FISH).setWeight(5))
                )
        );

        output.accept(FishanomicLootTables.WOODEN_ROD,
                LootTable.lootTable().withPool(
                        LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1))
                                .apply(ApplyBonusCount.addOreBonusCount(
                                        enchantmentLookup.getOrThrow(Enchantments.FORTUNE)
                                ))
                                .add(basicEntry(Items.ACACIA_PLANKS, 25, 0))
                                .add(detailedEntry(Items.ACACIA_LOG, 25, 0, 1, 3))
                                .add(NestedLootTable.lootTableReference(FishanomicLootTables.FISH).setWeight(25).setWeight(-3))
                ));

        output.accept(FishanomicLootTables.IRON_ROD,
                LootTable.lootTable().withPool(
                        LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1))
                                .apply(ApplyBonusCount.addOreBonusCount(
                                        enchantmentLookup.getOrThrow(Enchantments.FORTUNE)
                                ))
                                .add(basicEntry(Items.REDSTONE_BLOCK, 25, 0))
                                .add(detailedEntry(Items.REDSTONE_LAMP, 25, 0, 1, 3))
                                .add(NestedLootTable.lootTableReference(FishanomicLootTables.FISH).setWeight(50).setWeight(-3))
                ));
    }

    private LootPoolEntryContainer.Builder<?> basicEntry(Item item, int weight, int quality) {
        return LootItem.lootTableItem(item)
                .setWeight(weight)
                .setQuality(quality);
    }

    private LootPoolEntryContainer.Builder<?> detailedEntry(Item item, int weight, int quality, int min, int max) {
        return LootItem.lootTableItem(item)
                .setWeight(weight)
                .setQuality(quality)
                // This generates the "minecraft:set_count" with "minecraft:uniform"
                .apply(SetItemCountFunction.setCount(UniformGenerator.between(min, max)));
    }
}
