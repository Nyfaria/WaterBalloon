package com.nyfaria.nyfsmultiloader.init;

import com.nyfaria.nyfsmultiloader.Constants;
import com.nyfaria.nyfsmultiloader.registration.RegistrationProvider;
import com.nyfaria.nyfsmultiloader.registration.RegistryObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Function;
import java.util.function.Supplier;

public class BlockInit {
    public static final RegistrationProvider<Block> BLOCKS = RegistrationProvider.get(Registries.BLOCK, Constants.MODID);
    public static final RegistrationProvider<BlockEntityType<?>> BLOCK_ENTITIES = RegistrationProvider.get(Registries.BLOCK_ENTITY_TYPE, Constants.MODID);


    public static <T extends Block> RegistryObject<Block,T> registerBlock(String name, Supplier<T> block) {
        return registerBlock(name, block, b -> () -> new BlockItem(b.get(), ItemInit.getItemProperties()));
    }

    protected static <T extends Block> RegistryObject<Block,T> registerBlock(String name, Supplier<T> block, Function<RegistryObject<Block,T>, Supplier<? extends BlockItem>> item) {
        RegistryObject<Block,T> reg = BLOCKS.register(name, block);
        ItemInit.ITEMS.register(name, () -> item.apply(reg).get());
        return reg;
    }

    public static <T extends Block> RegistryObject<Block,T> registerBlockWithoutItem(String name, Supplier<T> block) {
        var reg = BLOCKS.register(name, block);
        return reg;
    }


    public static void loadClass() {
    }
}
