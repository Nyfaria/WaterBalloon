package com.nyfaria.nyfsmultiloader.datagen;

import com.nyfaria.nyfsmultiloader.Constants;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber(modid = Constants.MODID, bus = EventBusSubscriber.Bus.MOD)
public class DataGenEntrypoint {
    @SubscribeEvent
    public static void onGatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        boolean includeServer = event.includeServer();
        boolean includeClient = event.includeClient();

        generator.addProvider(includeServer, new ModRecipeProvider(packOutput, event.getLookupProvider()));
        generator.addProvider(includeServer, new ModLootTableProvider(packOutput, event.getLookupProvider()));
        generator.addProvider(includeServer, new ModSoundProvider(packOutput, existingFileHelper));
        generator.addProvider(includeServer, new ModTagProvider.ModBlockTags(packOutput, event.getLookupProvider(), existingFileHelper));
        generator.addProvider(includeServer, new ModTagProvider.ModItemTags(packOutput, event.getLookupProvider(), existingFileHelper));
        generator.addProvider(includeClient, new ModItemModelProvider(packOutput, existingFileHelper));
        generator.addProvider(includeClient, new ModBlockStateProvider(packOutput, existingFileHelper));
        generator.addProvider(includeClient, new ModLangProvider(packOutput));
        generator.addProvider(includeServer, new ModDamageTypeProvider(packOutput, event.getLookupProvider()));
    }
}
