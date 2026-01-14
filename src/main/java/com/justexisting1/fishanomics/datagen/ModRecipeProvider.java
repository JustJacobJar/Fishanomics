package com.justexisting1.fishanomics.datagen;

import com.justexisting1.fishanomics.Fishanomics;
import com.justexisting1.fishanomics.block.FishanomicsBlocks;
import com.justexisting1.fishanomics.component.FishanomicDataComponents;
import com.justexisting1.fishanomics.item.FishanomicItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {

//        Items that smelt into iron
        List<ItemLike> IRON_SMELTABLES = List.of(FishanomicItems.COAL_FISH, FishanomicItems.IRON_FISH);

        fishingRodRecipies(recipeOutput);
        fishingHookRecipies(recipeOutput);

        //Shaped Recipe
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, FishanomicsBlocks.FISH_FURNACE.get())
                .pattern("ccc")
                .pattern("cfc")
                .pattern("ccc")
                .define('c', ItemTags.STONE_CRAFTING_MATERIALS)
                .define('f', FishanomicItems.COAL_FISH.get())
                .unlockedBy("has_coal_fish", has(FishanomicItems.COAL_FISH))
                .save(recipeOutput);

        //Shapeless Recipe
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, FishanomicsBlocks.FISH_TANK, 9)
                .requires(FishanomicItems.COAL_FISH)
                .unlockedBy("has_coal_fish", has(FishanomicItems.COAL_FISH))
                //This is used to prevent conflicting, DEFAULTS to output item name
                .save(recipeOutput, "fishanomics:fish_tank_from_coal_fish");

        oreSmelting(recipeOutput, IRON_SMELTABLES, RecipeCategory.MISC, Items.IRON_INGOT, 0.25f, 200, "iron");
        oreBlasting(recipeOutput, IRON_SMELTABLES, RecipeCategory.MISC, Items.IRON_INGOT, 0.25f, 100, "iron");
    }


    private void fishingRodRecipies(RecipeOutput recipeOutput){

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, FishanomicItems.WOODEN_FISHING_ROD.get())
                .pattern("  s")
                .pattern(" st")
                .pattern("sht")
                .define('s', Items.STICK)
                .define('t', Items.STRING)
                .define('h', FishanomicItems.WOODEN_HOOK)
                .unlockedBy("has_wooden_hook", has(FishanomicItems.WOODEN_HOOK))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, FishanomicItems.STONE_FISHING_ROD.get())
                .pattern("  s")
                .pattern(" st")
                .pattern("sht")
                .define('s', Items.STICK)
                .define('t', Items.STRING)
                .define('h', FishanomicItems.STONE_HOOK)
                .unlockedBy("has_stone_hook", has(FishanomicItems.STONE_HOOK))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, FishanomicItems.IRON_FISHING_ROD.get())
                .pattern("  s")
                .pattern(" st")
                .pattern("sht")
                .define('s', Items.STICK)
                .define('t', Items.STRING)
                .define('h', FishanomicItems.IRON_HOOK)
                .unlockedBy("has_iron_hook", has(FishanomicItems.IRON_HOOK))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, FishanomicItems.GOLD_FISHING_ROD.get())
                .pattern("  s")
                .pattern(" st")
                .pattern("sht")
                .define('s', Items.STICK)
                .define('t', Items.STRING)
                .define('h', FishanomicItems.GOLD_HOOK)
                .unlockedBy("has_gold_hook", has(FishanomicItems.GOLD_HOOK))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, FishanomicItems.DIAMOND_FISHING_ROD.get())
                .pattern("  s")
                .pattern(" st")
                .pattern("sht")
                .define('s', Items.STICK)
                .define('t', Items.STRING)
                .define('h', FishanomicItems.DIAMOND_HOOK)
                .unlockedBy("has_diamond_hook", has(FishanomicItems.DIAMOND_HOOK))
                .save(recipeOutput);

        netheriteSmithing(recipeOutput, FishanomicItems.DIAMOND_FISHING_ROD.get(), RecipeCategory.TOOLS, FishanomicItems.NETHERITE_FISHING_ROD.get());

        //Data driven rod
        //1. Define result
        ItemStack obsidianRodResult = new ItemStack(FishanomicItems.CUSTOM_FISHING_ROD.get());
        //2. Set custom data component
        obsidianRodResult.set(FishanomicDataComponents.FISHING_ROD_DATA.get(),
                ResourceLocation.fromNamespaceAndPath(Fishanomics.MOD_ID, "obi"));
        //3. Set up recipe
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, obsidianRodResult)
                .pattern("  O")
                .pattern(" O#")
                .pattern("S #")
                .define('O', Items.OBSIDIAN)
                .define('S', Items.STICK)
                .define('#', Items.STRING)
                .unlockedBy("has_obsidian", has(Items.OBSIDIAN))
                // This attaches the component pointing to our registry ID
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(Fishanomics.MOD_ID, "obsidian_rod_recipe"));
    }



    private void fishingHookRecipies(RecipeOutput recipeOutput){

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, FishanomicItems.WOODEN_HOOK.get())
                .pattern("  w")
                .pattern("s s")
                .pattern(" s ")
                .define('s', Items.STICK)
                .define('w', ItemTags.PLANKS)
                .unlockedBy("has_planks", has(ItemTags.PLANKS))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, FishanomicItems.STONE_HOOK.get())
                .pattern("  s")
                .pattern("c c")
                .pattern(" c ")
                .define('c', ItemTags.STONE_CRAFTING_MATERIALS)
                .define('s', Items.STONE)
                .unlockedBy("has_stone", has(Items.STONE))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, FishanomicItems.IRON_HOOK.get())
                .pattern("  b")
                .pattern("i i")
                .pattern(" i ")
                .define('i', Items.IRON_INGOT)
                .define('b', Items.IRON_BLOCK)
                .unlockedBy("has_iron_ingot", has(Items.IRON_INGOT))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, FishanomicItems.GOLD_HOOK.get())
                .pattern("  b")
                .pattern("i i")
                .pattern(" i ")
                .define('i', Items.GOLD_INGOT)
                .define('b', Items.GOLD_BLOCK)
                .unlockedBy("has_gold_ingot", has(Items.GOLD_INGOT))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, FishanomicItems.DIAMOND_HOOK.get())
                .pattern("  b")
                .pattern("i i")
                .pattern(" i ")
                .define('i', Items.DIAMOND)
                .define('b', Items.DIAMOND_BLOCK)
                .unlockedBy("has_diamond", has(Items.DIAMOND))
                .save(recipeOutput);
    }

    //region Helper methods to add under my mod

    protected static void oreSmelting(RecipeOutput recipeOutput, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult,
                                      float pExperience, int pCookingTIme, String pGroup) {
        oreCooking(recipeOutput, RecipeSerializer.SMELTING_RECIPE, SmeltingRecipe::new, pIngredients, pCategory, pResult,
                pExperience, pCookingTIme, pGroup, "_from_smelting");
    }

    protected static void oreBlasting(RecipeOutput recipeOutput, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult,
                                      float pExperience, int pCookingTime, String pGroup) {
        oreCooking(recipeOutput, RecipeSerializer.BLASTING_RECIPE, BlastingRecipe::new, pIngredients, pCategory, pResult,
                pExperience, pCookingTime, pGroup, "_from_blasting");
    }

    protected static <T extends AbstractCookingRecipe> void oreCooking(RecipeOutput recipeOutput, RecipeSerializer<T> pCookingSerializer, AbstractCookingRecipe.Factory<T> factory,
                                                                       List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTime, String pGroup, String pRecipeName) {
        for(ItemLike itemlike : pIngredients) {
            SimpleCookingRecipeBuilder.generic(Ingredient.of(itemlike), pCategory, pResult, pExperience, pCookingTime, pCookingSerializer, factory).group(pGroup).unlockedBy(getHasName(itemlike), has(itemlike))
                    .save(recipeOutput, Fishanomics.MOD_ID + ":" + getItemName(pResult) + pRecipeName + "_" + getItemName(itemlike));
        }
    }

    //endregion
}
