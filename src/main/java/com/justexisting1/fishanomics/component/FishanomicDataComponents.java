package com.justexisting1.fishanomics.component;

import com.justexisting1.fishanomics.Fishanomics;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.bus.api.IEventBus;


import java.util.function.UnaryOperator;

public class FishanomicDataComponents {
    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPES =
            DeferredRegister.createDataComponents(Fishanomics.MOD_ID);

//    public static final DeferredHolder<DataComponentType<?>, DataComponentType<FishingRodProperties>> FISHING_ROD_DATA = register("fishing_rod_data",
//            builder -> builder.persistent(FishingRodProperties.CODEC));

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ResourceLocation>> FISHING_ROD_DATA =
            register("fishing_rod_data", builder -> builder
                    .persistent(ResourceLocation.CODEC) // We only save the ID (e.g. "fishanomics:obsidian")
                    .networkSynchronized(ResourceLocation.STREAM_CODEC)
            );

    /**
     * Helper function to register datatypes
     */
    private static <T>DeferredHolder<DataComponentType<?>, DataComponentType<T>> register(String name,
                                                                                          UnaryOperator<DataComponentType.Builder<T>> builderOperator) {
        return DATA_COMPONENT_TYPES.register(name, () -> builderOperator.apply(DataComponentType.builder()).build());
    }

    public static void register(IEventBus eventBus) {
        DATA_COMPONENT_TYPES.register(eventBus);
    }
}
