package com.nyfaria.waterballoon.events;

import com.nyfaria.waterballoon.init.EntityInit;
import com.nyfaria.waterballoon.init.ItemInit;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
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
            float r;
            float g;
            float b;
            int i1 = 25;
            int i =  (int)(Minecraft.getInstance().level.getDayTime() / 25);
            int j = DyeColor.values().length;
            int k = i % j;
            int l = (i + 1) % j;
            float f3 = ((float)(Minecraft.getInstance().level.getDayTime() % 25) + Minecraft.getInstance().getPartialTick()) / 25.0F;
            float[] afloat1 = Sheep.getColorArray(DyeColor.byId(k));
            float[] afloat2 = Sheep.getColorArray(DyeColor.byId(l));
            float a = 1f;
            r = afloat1[0] * (1.0F - f3) + afloat2[0] * f3;
            g = afloat1[1] * (1.0F - f3) + afloat2[1] * f3;
            b = afloat1[2] * (1.0F - f3) + afloat2[2] * f3;
            int argb = (int)(a * 255) << 24 | (int)(r * 255) << 16 | (int)(g * 255) << 8 | (int)(b * 255);
            return argb;
        }, ItemInit.WATER_BALLOON.get(), ItemInit.SLING_SHOT.get());
    }
    @SubscribeEvent
    public static void onFMLClient(FMLClientSetupEvent event){
        ItemProperties.register(ItemInit.SLING_SHOT.get(), new ResourceLocation("pulling"),
                (stack, world, entity, i) -> {
                    if (entity == null) {
                        return 0.0F;
                    } else {
                        return entity.getUseItem() != stack ? 0.0F : (float)(stack.getUseDuration() - entity.getUseItemRemainingTicks()) / 20.0F;
                    }
                });
        ItemProperties.register(ItemInit.SLING_SHOT.get(), new ResourceLocation("pull"),
                (stack, world, entity, i) -> {
                    return entity == null ? 0.0F : (entity.getUseItem() == stack && entity.getUseItemRemainingTicks() > 0) ? 1.0F : 0.0F;
                });
    }


}
