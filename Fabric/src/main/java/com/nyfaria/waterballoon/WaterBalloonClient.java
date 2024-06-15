package com.nyfaria.waterballoon;

import com.nyfaria.waterballoon.init.EntityInit;
import com.nyfaria.waterballoon.init.ItemInit;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.item.DyeColor;

public class WaterBalloonClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {

        EntityRendererRegistry.register(EntityInit.THROWN_BALLOON.get(), ThrownItemRenderer::new);
        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> {
            if (stack.hasTag() && stack.getTag().contains("color")) {
                return stack.getTag().getInt("color");
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
            r = afloat1[0] * (1.0F - f3) + afloat2[0] * f3;
            g = afloat1[1] * (1.0F - f3) + afloat2[1] * f3;
            b = afloat1[2] * (1.0F - f3) + afloat2[2] * f3;
            int rgb = (int) (r * 255) << 16 | (int) (g * 255) << 8 | (int) (b * 255);
            return rgb;
        }, ItemInit.WATER_BALLOON.get());

    }
}
