package com.justexisting1.fishanomics.screen;

import com.justexisting1.fishanomics.Fishanomics;
import com.justexisting1.fishanomics.screen.custom.FishFurnaceMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.network.IContainerFactory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class FishanomicsMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(Registries.MENU, Fishanomics.MOD_ID);


    public static final DeferredHolder<MenuType<?>, MenuType<FishFurnaceMenu>> FISH_FURNACE_MENU =
            registerMenuType("fish_furnace_menu", FishFurnaceMenu::new);



    private static <T extends AbstractContainerMenu>DeferredHolder<MenuType<?>, MenuType<T>> registerMenuType(String name, IContainerFactory<T> factory){
        return MENUS.register(name, () -> IMenuTypeExtension.create(factory));
    }

    public static void register(IEventBus eventBus){
        MENUS.register(eventBus);
    }
}
