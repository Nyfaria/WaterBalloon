package com.nyfaria.waterballoon;


import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(Constants.MODID)
public class WaterBalloon {

    public WaterBalloon(IEventBus eventBus) {
        Constants.LOG.info("Hello NeoForge world!");
        CommonClass.init();
    }
}