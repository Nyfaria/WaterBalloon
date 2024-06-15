package com.nyfaria.waterballoon;

import com.nyfaria.waterballoon.init.ItemInit;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.world.item.CreativeModeTabs;

public class WaterBalloon implements ModInitializer {
    
    @Override
    public void onInitialize() {
        CommonClass.init();
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.TOOLS_AND_UTILITIES)
                .register((itemStacks) -> {
                    itemStacks.accept(ItemInit.WATER_BALLOON.get());
                    itemStacks.accept(ItemInit.BAZOOKA.get());
                    itemStacks.accept(ItemInit.SLING_SHOT.get());
                });
    }
}
