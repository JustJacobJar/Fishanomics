package com.justexisting1.fishanomics.block.entity;

import com.justexisting1.fishanomics.item.FishanomicItems;
import com.justexisting1.fishanomics.screen.custom.FishFurnaceMenu;
import com.justexisting1.fishanomics.util.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FishFurnaceBlockEntity extends BlockEntity implements MenuProvider {

    //Machine Inventory
    public final ItemStackHandler itemHandler = new ItemStackHandler(2) //How many slots are in the machine
    {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if (!level.isClientSide()) {
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }
    };

    //Index of the slots
    public static final int INPUT_SLOT = 0;
    public static final int OUTPUT_SLOT = 1;

    //Lets us sync the data with server to client + UI
    protected final ContainerData data;

    //Crafting progress
    private int progress = 0;
    private int maxProgress = 72;


    public FishFurnaceBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.FISH_FURNACE_BE.get(), pos, blockState);
        //Sync client/server for UI
        data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> FishFurnaceBlockEntity.this.progress;
                    case 1 -> FishFurnaceBlockEntity.this.maxProgress;
                    default -> 0;
                };

            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    //If doesnt work, use : switch case
                    case 0: FishFurnaceBlockEntity.this.progress = value;
                    case 1: FishFurnaceBlockEntity.this.maxProgress = value;
                };
            }

            //Number of vars to save
            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("block.fishanomics.fish_furnace");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new FishFurnaceMenu(containerId, playerInventory, this, this.data);
    }

    /**
     * Drops the inventory of the block
     */
    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    /**
     * Runs logic every tick
     */
    public void tick(Level level, BlockPos blockPos, BlockState blockState) {


        ItemStack inputItem = itemHandler.getStackInSlot(INPUT_SLOT);
        //Temp for now, this will be what the item makes, checks input slot, checks if
        ItemStack output = new ItemStack(Items.IRON_INGOT.asItem(), 2);


        if (hasRecipe(inputItem) && canOutputItem(output)) {
            increaseCraftingProgress();
            //Sync stuff
            setChanged(level, blockPos, blockState);

            if (hasCraftingFinished()) {
                craftItem(output);
                resetProgress();
            }

        } else {
            resetProgress();
        }
    }

    private void craftItem(ItemStack outputItem) {
        itemHandler.extractItem(INPUT_SLOT, 1, false);
        //If the output is buggy, there is another method. But this was said to be best
        itemHandler.setStackInSlot(OUTPUT_SLOT,
                new ItemStack(outputItem.getItem(),
                        itemHandler.getStackInSlot(OUTPUT_SLOT).getCount() + outputItem.getCount()));
    }

    private void resetProgress() {
        progress = 0;
        maxProgress = 72;   //idk maybe will look at doing this a diff way
    }


    /**
     * Checks if crafting progress full
     *
     * @return True if progress >= maxProgress
     */
    private boolean hasCraftingFinished() {
        return this.progress >= this.maxProgress;
    }

    /**
     * Increases crafting progress by 1
     */
    private void increaseCraftingProgress() {
        progress++;
    }

    /**
     * Checks if the machine has a recipe for the item
     *
     * @return True if the item can be processed in this machine
     */
    private boolean hasRecipe(ItemStack item) {
        //Will update to be dynamic later
        return item.is(ModTags.Items.SMELTABLE_ITEMS);
    }

    /**
     * Checks if the item can be inserted into output slot
     *
     * @param outputItem The output item
     * @return True if output item can be inserted into output and there is stack space
     */
    private boolean canOutputItem(ItemStack outputItem) {
        return canInsertItemIntoOutputSlot(outputItem) && canInsertAmountIntoOutputSlot(outputItem.getCount());
    }

    /**
     * Checks if an item can be inserted into output slot
     *
     * @param outputItem Item to insert
     * @return True if item slot is empty/same as input
     */
    private boolean canInsertItemIntoOutputSlot(ItemStack outputItem) {
        return itemHandler.getStackInSlot(OUTPUT_SLOT).isEmpty() ||
                itemHandler.getStackInSlot(OUTPUT_SLOT).getItem() == outputItem.getItem();
    }

    /**
     * Checks if an item can be inserted into an output slot, regarding stack size
     *
     * @param count Amount of item
     * @return True if there is stack size space for the item
     */
    private boolean canInsertAmountIntoOutputSlot(int count) {
        int maxCount = itemHandler.getStackInSlot(OUTPUT_SLOT).isEmpty() ? 64 : itemHandler.getStackInSlot(OUTPUT_SLOT).getMaxStackSize();
        int currCount = itemHandler.getStackInSlot(OUTPUT_SLOT).getCount();

        return maxCount >= currCount + count;
    }


    //region Saving and loading of data

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        tag.put("inventory", itemHandler.serializeNBT(registries));
        tag.putInt("fish_furnace.progress", progress);
        tag.putInt("fish_furnace.max_progress", maxProgress);

        super.saveAdditional(tag, registries);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);

        itemHandler.deserializeNBT(registries, tag.getCompound("inventory"));
        progress = tag.getInt("fish_furnace.progress");
        maxProgress = tag.getInt("fish_furnace.max_progress");
    }

    //endregion

    //region Sync server and client stuff

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveWithoutMetadata(registries);
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    //endregion

}
