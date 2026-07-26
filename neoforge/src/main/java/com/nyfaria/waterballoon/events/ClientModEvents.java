package com.nyfaria.waterballoon.events;

import com.nyfaria.waterballoon.*;
import com.nyfaria.waterballoon.init.EntityInit;
import com.nyfaria.waterballoon.init.ItemInit;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.*;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.resources.*;
import net.minecraft.util.*;
import net.minecraft.world.entity.animal.sheep.*;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.component.DyedItemColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;

@EventBusSubscriber(value = Dist.CLIENT)
public class ClientModEvents {


    @SubscribeEvent
    public static void renderers(EntityRenderersEvent.RegisterRenderers e) {
        e.registerEntityRenderer(EntityInit.THROWN_BALLOON.get(), ThrownItemRenderer::new);
    }
    @SubscribeEvent
    public static void itemColors(RegisterColorHandlersEvent.ItemTintSources e) {
        e.register(Identifier.fromNamespaceAndPath(Constants.MODID,"dyed_color"), WaterBallonTintSource.MAP_CODEC);
    }



}
