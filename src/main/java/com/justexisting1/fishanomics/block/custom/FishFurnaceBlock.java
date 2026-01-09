package com.justexisting1.fishanomics.block.custom;

import com.justexisting1.fishanomics.block.entity.FishFurnaceBlockEntity;
import com.justexisting1.fishanomics.block.entity.ModBlockEntities;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class FishFurnaceBlock extends BaseEntityBlock {

    public static final MapCodec<FishFurnaceBlock> CODEC = simpleCodec(FishFurnaceBlock::new);


    public FishFurnaceBlock(Properties properties) {
        super(properties);
    }

    /**
     * Reads in and out block class
     */
    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new FishFurnaceBlockEntity(pos, state);
    }

    /**
     * Makes the block NOT invisible
     */
    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        //If block broken, checks this block is a type, drops items
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof FishFurnaceBlockEntity fishFurnaceBlockEntity) {
                fishFurnaceBlockEntity.drops();
            }
        }

        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {

        if (!level.isClientSide()) {
            BlockEntity entity = level.getBlockEntity(pos);
            if (entity instanceof FishFurnaceBlockEntity fishFurnaceBlockEntity) {
                ((ServerPlayer) player).openMenu(new SimpleMenuProvider(fishFurnaceBlockEntity, Component.literal("Fish Furnace")), pos);
            } else {
                throw new IllegalStateException("Our container provide3r is missing!");
            }
        }

        return ItemInteractionResult.sidedSuccess(level.isClientSide());
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {

        //dont do on client
        if(level.isClientSide()){
            return null;
        }

        //Tick entity in block entity will get called every tick
        return createTickerHelper(blockEntityType, ModBlockEntities.FISH_FURNACE_BE.get(),
                (nLevel, blockPos, blockState, blockEntity) -> blockEntity.tick(nLevel, blockPos, blockState));
    }
}
