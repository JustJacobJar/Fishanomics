package com.justexisting1.fishanomics.entity;

import com.justexisting1.fishanomics.item.FishanomicFishingRodItem;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.neoforged.neoforge.common.ItemAbilities;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

public class FishanomicFishingBobberEntity extends FishingHook {
    /**
     * Fishing rod luck level
     */
    private final int luck;

    /**
     * Fishing rod as Item Stack
     */
    private final ItemStack mc_fishingRodItemStack;
    //I need to get the data component from the item which contains the loot table locations, luck...
    //Or is this just on the fishingrod item i pass in???
    //Im pretty sure by this point in the code, the ItemStack and Fishing rod I pass in are the same Item

    /**
     * Fishing rod item (Fishanomics Extended version)
     */
    private final FishanomicFishingRodItem fishingRod;

    /**
     * Constructor for fishing bobber
     * @param player Player the bobber belongs to
     * @param level MC world level?
     * @param luck  Fishing rods Luck level
     * @param lureSpeed Fishing rods Lure Speed
     * @param fishingRod Fishing rod item
     * @param rod Fishing rod item as ItemStack
     */
    public FishanomicFishingBobberEntity(Player player, Level level, int luck, int lureSpeed, FishanomicFishingRodItem fishingRod, @Nonnull ItemStack rod) {
        super(player, level, luck, lureSpeed);
        this.luck = luck;
        player.fishing = this;
        this.mc_fishingRodItemStack = rod;
        this.fishingRod = fishingRod;
    }


    /**
     * Pull interaction on fishing rod.
     * Pulls itemLoot/player/entity/nothing and takes durability damage for that action
     * @apiNote Changed the way a loot item is retrieved from loot table.
     * @param stack The fishing rod? -> comes from item in held hand
     * @return Amount of damage item should take
     */
    @Override
    public int retrieve(@Nonnull ItemStack stack) {
        Player player = this.getPlayerOwner();
        Level level = this.level();
        if (!this.level().isClientSide && player != null && !this.shouldStopFishing(player)) {
            int i = 0;  //durability damage to take if not hooking fish loot
            net.neoforged.neoforge.event.entity.player.ItemFishedEvent event = null;
            if (this.getHookedIn() != null) {
                this.pullEntity(this.getHookedIn());
                CriteriaTriggers.FISHING_ROD_HOOKED.trigger((ServerPlayer) player, stack, this, Collections.emptyList());
                this.level().broadcastEntityEvent(this, (byte) 31);
                i = this.getHookedIn() instanceof ItemEntity ? 3 : 5;   //Think it makes it take more durability dmg for player/block
            } else if (this.nibble > 0 && level instanceof ServerLevel serverLevel) {

                LootParams lootparams = new LootParams.Builder((ServerLevel) this.level())
                        .withParameter(LootContextParams.ORIGIN, this.position())
                        .withParameter(LootContextParams.TOOL, stack)
                        .withParameter(LootContextParams.THIS_ENTITY, this)
                        .withParameter(LootContextParams.ATTACKING_ENTITY, this.getOwner())
                        .withLuck((float) this.luck + player.getLuck()) //Player luck assumed, try before, in the case of machines
                        .create(LootContextParamSets.FISHING);
                //Vanilla Looting
                //LootTable loottable = this.level().getServer().reloadableRegistries().getLootTable(BuiltInLootTables.FISHING);
                //List<ItemStack> list = loottable.getRandomItems(lootparams);

                //Fishanomics Looting
                List<ItemStack> list = getLoot(lootparams, serverLevel, stack); //set in custom catching fish

                //Rod durability damage set in this event
                //I think this.onGround() checks if the item you hooked is on the ground? Eg, fished an item dropped on the floor
                event = new net.neoforged.neoforge.event.entity.player.ItemFishedEvent(list, this.onGround() ? 2 : 1, this);
                net.neoforged.neoforge.common.NeoForge.EVENT_BUS.post(event);
                if (event.isCanceled()) {
                    this.discard();
                    return event.getRodDamage();
                }
                CriteriaTriggers.FISHING_ROD_HOOKED.trigger((ServerPlayer) player, stack, this, list);

                //For items fished, give xp and spwan item
                //ItemStack holds the amount of an item
                for (ItemStack itemstack : list) {
                    ItemEntity itementity = new ItemEntity(this.level(), this.getX(), this.getY(), this.getZ(), itemstack);
                    double d0 = player.getX() - this.getX();
                    double d1 = player.getY() - this.getY();
                    double d2 = player.getZ() - this.getZ();
                    double d3 = 0.1;
                    itementity.setDeltaMovement(d0 * 0.1, d1 * 0.1 + Math.sqrt(Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2)) * 0.08, d2 * 0.1);
                    this.level().addFreshEntity(itementity);
                    player.level()
                            .addFreshEntity(new ExperienceOrb(player.level(), player.getX(), player.getY() + 0.5, player.getZ() + 0.5, this.random.nextInt(6) + 1));
                    if (itemstack.is(ItemTags.FISHES)) {
                        player.awardStat(Stats.FISH_CAUGHT, 1);
                    }
                }

                i = 1;  //Hooked nothing, but used item, so take 1 durability dmg
            }

            if (this.onGround()) {
                i = 2;  //If you hook a block on ground?
            }

            this.discard();
            if (event != null) return event.getRodDamage(); //Do durabilty damage
            return i;   //Damage to take (none fishing loot)
        } else {
            return 0;
        }
    }


    /**
     * Used to get the loot item from the correct loot table
     * @param lootParams Minecraft loot params, used to roll luck
     * @param serverLevel Minecraft server level, used to get Registries of loot tables
     * @return Item Stack rolled on loot table
     */
    private List<ItemStack> getLoot(LootParams lootParams, ServerLevel serverLevel, ItemStack item){
//        ResourceKey<LootTable> lootTableLocation;
//        //I will need to register some loot tables
//        lootTableLocation = fishingRod.getFishingLootTable(item);
//        LootTable lootTable = serverLevel.getServer().reloadableRegistries().getLootTable(lootTableLocation);
        LootTable lootTable = fishingRod.getLootTable(item, serverLevel);   //get the table from data component
        return lootTable.getRandomItems(lootParams);
    }

    //region === No use as of yet ===

    @Override
    protected boolean shouldStopFishing(Player player) {
        ItemStack mainHand = player.getMainHandItem();
        ItemStack offHand = player.getOffhandItem();
        boolean isMainHandRod = mainHand.canPerformAction(ItemAbilities.FISHING_ROD_CAST);
        boolean isOffHandRod = offHand.canPerformAction(ItemAbilities.FISHING_ROD_CAST);
        if (!player.isRemoved() && player.isAlive() && (isMainHandRod || isOffHandRod) && !(this.distanceToSqr(player) > 1024.0D)) {
            return false;
        } else {
            this.discard();
            return true;
        }
    }

    @Override
    protected void catchingFish(BlockPos pos) {
        ServerLevel serverlevel = (ServerLevel)this.level();
        int i = 1;
        BlockPos blockpos = pos.above();
        if (this.random.nextFloat() < 0.25F && this.level().isRainingAt(blockpos)) {
            i++;
        }

        if (this.random.nextFloat() < 0.5F && !this.level().canSeeSky(blockpos)) {
            i--;
        }

        if (this.nibble > 0) {
            this.nibble--;
            if (this.nibble <= 0) {
                this.timeUntilLured = 0;
                this.timeUntilHooked = 0;
                this.getEntityData().set(DATA_BITING, false);
            }
        } else if (this.timeUntilHooked > 0) {
            this.timeUntilHooked -= i;
            if (this.timeUntilHooked > 0) {
                this.fishAngle = this.fishAngle + (float)this.random.triangle(0.0, 9.188);
                float f = this.fishAngle * (float) (Math.PI / 180.0);
                float f1 = Mth.sin(f);
                float f2 = Mth.cos(f);
                double d0 = this.getX() + (double)(f1 * (float)this.timeUntilHooked * 0.1F);
                double d1 = (double)((float)Mth.floor(this.getY()) + 1.0F);
                double d2 = this.getZ() + (double)(f2 * (float)this.timeUntilHooked * 0.1F);
                BlockState blockstate = serverlevel.getBlockState(BlockPos.containing(d0, d1 - 1.0, d2));
                if (blockstate.is(Blocks.WATER)) {
                    if (this.random.nextFloat() < 0.15F) {
                        serverlevel.sendParticles(ParticleTypes.BUBBLE, d0, d1 - 0.1F, d2, 1, (double)f1, 0.1, (double)f2, 0.0);
                    }

                    float f3 = f1 * 0.04F;
                    float f4 = f2 * 0.04F;
                    serverlevel.sendParticles(ParticleTypes.FISHING, d0, d1, d2, 0, (double)f4, 0.01, (double)(-f3), 1.0);
                    serverlevel.sendParticles(ParticleTypes.FISHING, d0, d1, d2, 0, (double)(-f4), 0.01, (double)f3, 1.0);
                }
            } else {
                this.playSound(SoundEvents.FISHING_BOBBER_SPLASH, 0.25F, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.4F);
                double d3 = this.getY() + 0.5;
                serverlevel.sendParticles(
                        ParticleTypes.BUBBLE,
                        this.getX(),
                        d3,
                        this.getZ(),
                        (int)(1.0F + this.getBbWidth() * 20.0F),
                        (double)this.getBbWidth(),
                        0.0,
                        (double)this.getBbWidth(),
                        0.2F
                );
                serverlevel.sendParticles(
                        ParticleTypes.FISHING,
                        this.getX(),
                        d3,
                        this.getZ(),
                        (int)(1.0F + this.getBbWidth() * 20.0F),
                        (double)this.getBbWidth(),
                        0.0,
                        (double)this.getBbWidth(),
                        0.2F
                );
                this.nibble = Mth.nextInt(this.random, 20, 40);
                this.getEntityData().set(DATA_BITING, true);
            }
        } else if (this.timeUntilLured > 0) {
            this.timeUntilLured -= i;
            float f5 = 0.15F;
            if (this.timeUntilLured < 20) {
                f5 += (float)(20 - this.timeUntilLured) * 0.05F;
            } else if (this.timeUntilLured < 40) {
                f5 += (float)(40 - this.timeUntilLured) * 0.02F;
            } else if (this.timeUntilLured < 60) {
                f5 += (float)(60 - this.timeUntilLured) * 0.01F;
            }

            if (this.random.nextFloat() < f5) {
                float f6 = Mth.nextFloat(this.random, 0.0F, 360.0F) * (float) (Math.PI / 180.0);
                float f7 = Mth.nextFloat(this.random, 25.0F, 60.0F);
                double d4 = this.getX() + (double)(Mth.sin(f6) * f7) * 0.1;
                double d5 = (double)((float)Mth.floor(this.getY()) + 1.0F);
                double d6 = this.getZ() + (double)(Mth.cos(f6) * f7) * 0.1;
                BlockState blockstate1 = serverlevel.getBlockState(BlockPos.containing(d4, d5 - 1.0, d6));
                if (blockstate1.is(Blocks.WATER)) {
                    serverlevel.sendParticles(ParticleTypes.SPLASH, d4, d5, d6, 2 + this.random.nextInt(2), 0.1F, 0.0, 0.1F, 0.0);
                }
            }

            if (this.timeUntilLured <= 0) {
                this.fishAngle = Mth.nextFloat(this.random, 0.0F, 360.0F);
                this.timeUntilHooked = Mth.nextInt(this.random, 20, 80);
            }
        } else {
            this.timeUntilLured = Mth.nextInt(this.random, 100, 600);
            this.timeUntilLured = this.timeUntilLured - this.lureSpeed;
        }
    }

    //endregion

}
