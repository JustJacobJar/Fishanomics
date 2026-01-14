package com.justexisting1.fishanomics;

import com.justexisting1.fishanomics.block.FishanomicsBlocks;
import com.justexisting1.fishanomics.block.entity.FishanomicBlockEntities;
import com.justexisting1.fishanomics.client.ClientHandler;
import com.justexisting1.fishanomics.component.FishanomicDataComponents;
import com.justexisting1.fishanomics.fishingrods.FishingRodProperties;
import com.justexisting1.fishanomics.item.FishanomicCreativeModeTabs;
import com.justexisting1.fishanomics.item.FishanomicItems;

import com.justexisting1.fishanomics.screen.FishanomicsMenuTypes;
import com.justexisting1.fishanomics.screen.custom.FishFurnaceScreen;
import com.mojang.logging.LogUtils;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(Fishanomics.MOD_ID)
public class Fishanomics {
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "fishanomics";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public Fishanomics(IEventBus modEventBus, ModContainer modContainer) {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::setupClient);
        this.registerDeferredRegistries(modEventBus);
        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (ExampleMod) to respond directly to events.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
        NeoForge.EVENT_BUS.register(this);


//        modEventBus.addListener(this::addCreative);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {

    }

    private void setupClient(FMLClientSetupEvent event) {
        event.enqueueWork(ClientHandler::setupClient);
    }

    public void registerDeferredRegistries(IEventBus eventBus) {
        //Pulls list of mod items -> registers them
        FishanomicItems.ITEMS.register(eventBus);
        FishanomicsBlocks.register(eventBus);
        FishanomicBlockEntities.register(eventBus);
        FishanomicsMenuTypes.register(eventBus);
        FishanomicDataComponents.register(eventBus);

        // Register the item to a creative tab
        FishanomicCreativeModeTabs.register(eventBus);
    }

    // Add the example block item to the building blocks tab
//    private void addCreative(BuildCreativeModeTabContentsEvent event) {
//        if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
//            event.accept(ModItems.IRON_FISH);
//        }
//
//        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
//            event.accept(ModBlocks.FISH_TANK);
//        }
//    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

    }

    // REMOVE value = Dist.CLIENT so this runs on Servers too!
    @EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.MOD)
    public static class ModEvents {

        @SubscribeEvent
        public static void addRegistries(DataPackRegistryEvent.NewRegistry event) {
            event.dataPackRegistry(
                    FishingRodProperties.FISHING_ROD_REGISTRY_KEY,
                    FishingRodProperties.CODEC,
                    FishingRodProperties.CODEC // Use the StreamCodec for syncing!
            );
        }

//        @SubscribeEvent
//        public static void addRegistries(DataPackRegistryEvent.NewRegistry event){
//            event.dataPackRegistry(
//                    FishingRodProperties.FISHING_ROD_REGISTRY_KEY,
//                    FishingRodProperties.CODEC,
//                    // The network codec of the registry contents. Often identical to the normal codec.
//                    // May be a reduced variant of the normal codec that omits data that is not needed on the client.
//                    // May be null. If null, registry entries will not be synced to the client at all.
//                    // May be omitted, which is functionally identical to passing null (a method overload
//                    // with two parameters is called that passes null to the normal three parameter method).
//                    FishingRodProperties.STREAM_CODEC
//            );
//        }
    }

    @EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {




        @SubscribeEvent
        public static void registerMenuScreens(RegisterMenuScreensEvent event) {
            event.register(FishanomicsMenuTypes.FISH_FURNACE_MENU.get(), FishFurnaceScreen::new);
        }
    }
}
