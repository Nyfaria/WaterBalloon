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
            float r;
            float g;
            float b;
            int i1 = 25;
            int i = (int) (Minecraft.getInstance().level.getDayTime() / 25);
            int j = DyeColor.values().length;
            int k = i % j;
            int l = (i + 1) % j;
            float f3 = ((float) (Minecraft.getInstance().level.getDayTime() % 25) + Minecraft.getInstance().getFrameTime()) / 25.0F;
            float[] afloat1 = Sheep.getColorArray(DyeColor.byId(k));
            float[] afloat2 = Sheep.getColorArray(DyeColor.byId(l));
            float a = 1f;
            r = afloat1[0] * (1.0F - f3) + afloat2[0] * f3;
            g = afloat1[1] * (1.0F - f3) + afloat2[1] * f3;
            b = afloat1[2] * (1.0F - f3) + afloat2[2] * f3;
            int argb = (int) (a * 255) << 24 | (int) (r * 255) << 16 | (int) (g * 255) << 8 | (int) (b * 255);
            return argb;
        }, ItemInit.WATER_BALLOON.get(), ItemInit.SLING_SHOT.get());
        ItemProperties.register(ItemInit.SLING_SHOT.get(), ResourceLocation.withDefaultNamespace("pulling"),
                (stack, world, entity, i) -> {
                    if (entity == null) {
                        return 0.0F;
                    } else {
                        return entity.getUseItem() != stack ? 0.0F : (float) (stack.getUseDuration(player) - entity.getUseItemRemainingTicks()) / 20.0F;
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
