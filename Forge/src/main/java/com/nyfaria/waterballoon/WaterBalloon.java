package com.nyfaria.waterballoon;

import com.nyfaria.waterballoon.datagen.ModBlockStateProvider;
import com.nyfaria.waterballoon.datagen.ModItemModelProvider;
import com.nyfaria.waterballoon.datagen.ModLangProvider;
import com.nyfaria.waterballoon.datagen.ModLootTableProvider;
import com.nyfaria.waterballoon.datagen.ModRecipeProvider;
import com.nyfaria.waterballoon.datagen.ModSoundProvider;
import com.nyfaria.waterballoon.datagen.ModTagProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
@Mod(Constants.MODID)
public class WaterBalloon {
    
    public WaterBalloon() {
    
        // This method is invoked by the Forge mod loader when it is ready
        // to load your mod. You can access Forge and Common code in this
        // project.
    
        // Use Forge to bootstrap the Common mod.
        Constants.LOG.info("Hello Forge world!");
        CommonClass.init();
        
    }

    @SubscribeEvent
    public static void onGatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        boolean includeServer = event.includeServer();
        boolean includeClient = event.includeClient();

        generator.addProvider(includeServer, new ModRecipeProvider(generator));
        generator.addProvider(includeServer, new ModLootTableProvider(generator));
        generator.addProvider(includeServer, new ModSoundProvider(generator, existingFileHelper));
        generator.addProvider(includeServer, new ModTagProvider.Blocks(generator, existingFileHelper));
        generator.addProvider(includeServer, new ModTagProvider.Items(generator, existingFileHelper));
        generator.addProvider(includeClient, new ModItemModelProvider(generator, existingFileHelper));
        generator.addProvider(includeClient, new ModBlockStateProvider(generator, existingFileHelper));
        generator.addProvider(includeClient, new ModLangProvider(generator));
    }
}