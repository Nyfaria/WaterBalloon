package com.nyfaria.waterballoon;

import com.nyfaria.waterballoon.init.*;
import net.fabricmc.api.*;
import net.fabricmc.fabric.api.client.rendering.v1.*;
import net.minecraft.client.color.item.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.resources.*;

public class WaterBalloonClient implements ClientModInitializer {
    
    @Override
    public void onInitializeClient() {

        EntityRendererRegistry.register(EntityInit.THROWN_BALLOON.get(), ThrownItemRenderer::new);
        ItemTintSources.ID_MAPPER.put(Identifier.fromNamespaceAndPath(Constants.MODID,"waterballoon"),WaterBallonTintSource.MAP_CODEC);


    }
}
