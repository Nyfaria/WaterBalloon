package com.nyfaria.waterballoon.events;

import com.nyfaria.waterballoon.init.EntityInit;
import com.nyfaria.waterballoon.init.ItemInit;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.*;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.component.DyedItemColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEvents {


    @SubscribeEvent
    public static void renderers(EntityRenderersEvent.RegisterRenderers e) {
        e.registerEntityRenderer(EntityInit.THROWN_BALLOON.get(), ThrownItemRenderer::new);
    }
    @SubscribeEvent
    public static void itemColors(RegisterColorHandlersEvent.Item e) {
        e.register((stack, tintIndex) -> {
            if(DyedItemColor.getOrDefault(stack, -1) != -1){
                return DyedItemColor.getOrDefault(stack, -1);
            }
            int i;
            int k = (int)(Minecraft.getInstance().level.getDayTime()) / 25;
            int l = DyeColor.values().length;
            int i1 = k % l;
            int j1 = (k + 1) % l;
            float f = ((float)(Minecraft.getInstance().level.getDayTime() % 25) + Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(true)) / 25.0F;
            int k1 = Sheep.getColor(DyeColor.byId(i1));
            int l1 = Sheep.getColor(DyeColor.byId(j1));
            i = FastColor.ARGB32.lerp(f, k1, l1);
            return i;
        }, ItemInit.WATER_BALLOON.get(), ItemInit.SLING_SHOT.get());
    }
    @SubscribeEvent
    public static void onFMLClient(FMLClientSetupEvent event){
        ItemProperties.register(ItemInit.SLING_SHOT.get(), ResourceLocation.withDefaultNamespace("pulling"),
                (stack, world, entity, i) -> {
                    if (entity == null) {
                        return 0.0F;
                    } else {
                        return entity.getUseItem() != stack ? 0.0F : (float)(stack.getUseDuration(entity) - entity.getUseItemRemainingTicks()) / 20.0F;
                    }
                });
        ItemProperties.register(ItemInit.SLING_SHOT.get(), ResourceLocation.withDefaultNamespace("pull"),
                (stack, world, entity, i) -> {
                    return entity == null ? 0.0F : (entity.getUseItem() == stack && entity.getUseItemRemainingTicks() > 0) ? 1.0F : 0.0F;
                });
    }


}
