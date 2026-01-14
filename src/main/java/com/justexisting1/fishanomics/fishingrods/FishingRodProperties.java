package com.justexisting1.fishanomics.fishingrods;

import com.justexisting1.fishanomics.Fishanomics;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;

import static com.justexisting1.fishanomics.component.FishanomicDataComponents.FISHING_ROD_DATA;

/**
 * Record to create fishing rods from
 *
 * @param fishingRod Fishing Rod name?
 * @param durability Durability of the fishing rod
 */
public record FishingRodProperties(
        ResourceLocation fishingRod,
        ResourceLocation lootTable,
        int durability,
        int lure

) {
    /**
     * Codec for json -> class/record
     */
    public static final Codec<FishingRodProperties> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ResourceLocation.CODEC.fieldOf("fishing_rod").forGetter(FishingRodProperties::fishingRod),
                    ResourceLocation.CODEC.optionalFieldOf("loot_table", BuiltInLootTables.FISHING.location()).forGetter(FishingRodProperties::lootTable),
                    Codec.INT.fieldOf("durability").forGetter(FishingRodProperties::durability),
                    Codec.INT.fieldOf("lure").forGetter(FishingRodProperties::lure)

            ).apply(instance, FishingRodProperties::new)
    );

    // StreamCodec is required for network syncing //IDK DO I NEED THIS? YES FOR SERVERS
//    public static final StreamCodec<ByteBuf, FishingRodProperties> STREAM_CODEC = StreamCodec.composite(
//            ResourceLocation.STREAM_CODEC, FishingRodProperties::fishingRod,
//            ByteBufCodecs.VAR_INT, FishingRodProperties::durability,
//            ByteBufCodecs.VAR_INT, FishingRodProperties::lure,
//            FishingRodProperties::new
//    );


    /**
     * Gets the data components for a fishing rod that contains FishingRodProperties as a data component
     *
     * @param item  The fishing rod item
     * @param level The world level
     * @return FishingRodProperties or null if not found
     */
    public static FishingRodProperties getProperties(ItemStack item, Level level) {
        // 1. Get the ID from the component
        ResourceLocation id = item.get(FISHING_ROD_DATA);

        if (id == null) return null;    //TODO: Possibly change null to a defaulted FishingRodProperties

        try {
            // 2. Access the registry or crash if the registry itself wasn't registered
            var registry = level.registryAccess().registryOrThrow(FishingRodProperties.FISHING_ROD_REGISTRY_KEY);

            // 3. Convert ResourceLocation to ResourceKey for the specific entry
            ResourceKey<FishingRodProperties> entryKey = ResourceKey.create(
                    FishingRodProperties.FISHING_ROD_REGISTRY_KEY,
                    id
            );

            // 4. Get the data or crash if 'id' (e.g., fishanomics:obi) is missing from the registry
            return registry.getOrThrow(entryKey);
        } catch (Exception ex) {
            System.out.printf("Failed to find rod data for ID: %s$1. Error: %s$2", id, ex.getMessage());
            return null;
        }
    }

    /**
     * Registry Key for fishing rods
     */
    public static final ResourceKey<Registry<FishingRodProperties>> FISHING_ROD_REGISTRY_KEY =
            ResourceKey.createRegistryKey(
                    ResourceLocation.fromNamespaceAndPath(Fishanomics.MOD_ID, "fishingrod")
            );
}
