package com.nyfaria.nyfsmultiloader.init;

import com.nyfaria.grinnersents.registration.RegistrationProvider;
import com.nyfaria.nyfsmultiloader.Constants;
import net.minecraft.core.Registry;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class BlockInit {
    public static final RegistrationProvider<Block> BLOCKS = RegistrationProvider.get(Registry.BLOCK, Constants.MODID);
    public static final RegistrationProvider<BlockEntityType<?>> BLOCK_ENTITIES = RegistrationProvider.get(Registry.BLOCK_ENTITY_TYPE, Constants.MODID);


    public static void loadClass() {
    }
}
