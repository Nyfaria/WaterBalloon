package com.nyfaria.waterballoon;

import com.mojang.serialization.*;
import net.minecraft.client.color.*;
import net.minecraft.client.color.item.*;
import net.minecraft.client.multiplayer.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.*;
import org.jspecify.annotations.*;

public class WaterBallonTintSource implements ItemTintSource {
    public static final WaterBallonTintSource INSTANCE = new WaterBallonTintSource();
    public static final MapCodec<WaterBallonTintSource> MAP_CODEC = MapCodec.unit(INSTANCE);

    public WaterBallonTintSource() {
        super();
    }

    @Override
    public int calculate(ItemStack itemStack, @Nullable ClientLevel clientLevel, @Nullable LivingEntity livingEntity) {
        if(DyedItemColor.getOrDefault(itemStack, -1) != -1){
            return DyedItemColor.getOrDefault(itemStack, -1);
        }
        return ColorLerper.getLerpedColor(ColorLerper.Type.SHEEP, 1);
    }

    @Override
    public MapCodec<? extends ItemTintSource> type() {
        return MAP_CODEC;
    }
}
