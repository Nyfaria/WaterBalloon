package com.nyfaria.waterballoon;

import com.mojang.serialization.Codec;
import com.nyfaria.waterballoon.init.EntityInit;
import com.nyfaria.waterballoon.init.ItemInit;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.*;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.component.DyedItemColor;

public class WaterBalloonClient implements ClientModInitializer {
    
    @Override
    public void onInitializeClient() {

        EntityRendererRegistry.register(EntityInit.THROWN_BALLOON.get(), ThrownItemRenderer::new);
        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> {
            if (DyedItemColor.getOrDefault(stack, -1) != -1) {
                return DyedItemColor.getOrDefault(stack, -1);
            }
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
        ItemProperties.register(ItemInit.SLING_SHOT.get(), ResourceLocation.withDefaultNamespace("pulling"),
                (stack, world, entity, i) -> {
                    if (entity == null) {
                        return 0.0F;
                    } else {
                        return entity.getUseItem() != stack ? 0.0F : (float) (stack.getUseDuration(entity) - entity.getUseItemRemainingTicks()) / 20.0F;
                    }
                });
        ItemProperties.register(ItemInit.SLING_SHOT.get(), ResourceLocation.withDefaultNamespace("pull"),
                (stack, world, entity, i) -> {
                    if (entity == null) {
                        return 0.0F;
                    } else {
                        return entity.getUseItem() == stack && entity.getUseItem().getUseAnimation() == UseAnim.BOW ? 1.0F : 0.0F;
                    }
                });

    }
}
