package com.justexisting1.fishanomics.block.entity;

import com.justexisting1.fishanomics.Fishanomics;
import com.justexisting1.fishanomics.block.FishanomicsBlocks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class FishanomicBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, Fishanomics.MOD_ID);

    public static final Supplier<BlockEntityType<FishFurnaceBlockEntity>> FISH_FURNACE_BE =
            BLOCK_ENTITIES.register("fish_furnace_be", () -> BlockEntityType.Builder.of(
                    FishFurnaceBlockEntity::new, FishanomicsBlocks.FISH_FURNACE.get()).build(null));


    public static void register(IEventBus eventBus){
        BLOCK_ENTITIES.register(eventBus);
    }
}
