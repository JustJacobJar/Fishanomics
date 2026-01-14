package com.justexisting1.fishanomics.item;

import com.justexisting1.fishanomics.Fishanomics;
import com.justexisting1.fishanomics.component.FishanomicDataComponents;
import com.justexisting1.fishanomics.init.FishanomicLootTables;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tiers;
import net.neoforged.neoforge.common.extensions.IItemExtension;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Supplier;


public class FishanomicItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Fishanomics.MOD_ID);
    public static final Collection<DeferredItem<Item>> ITEMS_FOR_TABS = new ArrayList<>();

    //TODO: make collection for fishing rods/fish/items and all that to automate model creation

    //Fish
    public static final DeferredItem<Item> COAL_FISH = registerWithTab(SimpleItem::new, "coal_fish");
    public static final DeferredItem<Item> COPPER_FISH = registerWithTab(SimpleItem::new, "copper_fish");
    public static final DeferredItem<Item> IRON_FISH = registerWithTab(SimpleItem::new, "iron_fish");
    public static final DeferredItem<Item> GOLD_FISH = registerWithTab(SimpleItem::new, "gold_fish");
    public static final DeferredItem<Item> REDSTONE_FISH = registerWithTab(SimpleItem::new, "redstone_fish");
    public static final DeferredItem<Item> LAPIS_FISH = registerWithTab(SimpleItem::new, "lapis_fish");
    public static final DeferredItem<Item> DIAMOND_FISH = registerWithTab(SimpleItem::new, "diamond_fish");

    //TODO: Possibly make it so that modpack creators can create their own variants of these to use to craft custom rods
    //Simple Items
    public static final DeferredItem<Item> WOODEN_HOOK = registerWithTab(SimpleItem::new, "wooden_hook");
    public static final DeferredItem<Item> STONE_HOOK = registerWithTab(SimpleItem::new, "stone_hook");
    public static final DeferredItem<Item> IRON_HOOK = registerWithTab(SimpleItem::new, "iron_hook");
    public static final DeferredItem<Item> GOLD_HOOK = registerWithTab(SimpleItem::new, "gold_hook");
    public static final DeferredItem<Item> DIAMOND_HOOK = registerWithTab(SimpleItem::new, "diamond_hook");

    //TODO: Change the loot tables that each rod points to
    //Fishing Rods
    public static final DeferredItem<Item> WOODEN_FISHING_ROD = registerWithTab(() -> new FishanomicFishingRodItem(new Item.Properties().durability(59).component(FishanomicDataComponents.FISHING_ROD_DATA.get(), ResourceLocation.fromNamespaceAndPath(Fishanomics.MOD_ID, "wooden_fishing_rod"))), "wooden_fishing_rod");
    public static final DeferredItem<Item> STONE_FISHING_ROD = registerWithTab(() -> new FishanomicFishingRodItem(Tiers.STONE, FishanomicLootTables.STONE_ROD, 40, new Item.Properties().durability(131)), "stone_fishing_rod");
    public static final DeferredItem<Item> IRON_FISHING_ROD = registerWithTab(() -> new FishanomicFishingRodItem(Tiers.IRON, FishanomicLootTables.IRON_ROD, 60, new Item.Properties().durability(250)), "iron_fishing_rod");
    public static final DeferredItem<Item> GOLD_FISHING_ROD = registerWithTab(() -> new FishanomicFishingRodItem(Tiers.GOLD, FishanomicLootTables.WOODEN_ROD, 120, new Item.Properties().durability(32)), "gold_fishing_rod");
    public static final DeferredItem<Item> DIAMOND_FISHING_ROD = registerWithTab(() -> new FishanomicFishingRodItem(Tiers.DIAMOND, FishanomicLootTables.WOODEN_ROD, 80, new Item.Properties().durability(1561)), "diamond_fishing_rod");
    public static final DeferredItem<Item> NETHERITE_FISHING_ROD = registerWithTab(() -> new FishanomicFishingRodItem(Tiers.NETHERITE, FishanomicLootTables.WOODEN_ROD, 90, new Item.Properties().durability(2031)), "netherite_fishing_rod");

    //Custom Rods
    public static final DeferredItem<Item> CUSTOM_FISHING_ROD  = registerWithTab(() -> new FishanomicFishingRodItem(new Item.Properties()), "custom_fishing_rod");

    public static DeferredItem<Item> register(Supplier<Item> initialiser, String name){
        return ITEMS.register(name, initialiser);
    }

    public static DeferredItem<Item> registerWithTab(Supplier<Item> initialiser, String name){
        DeferredItem<Item> itemToRegister = register(initialiser, name);
        ITEMS_FOR_TABS.add(itemToRegister);
        return itemToRegister;
    }
}
