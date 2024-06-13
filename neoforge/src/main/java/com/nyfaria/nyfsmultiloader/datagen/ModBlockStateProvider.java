package com.nyfaria.nyfsmultiloader.datagen;

import com.nyfaria.nyfsmultiloader.Constants;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput generator, ExistingFileHelper existingFileHelper) {
        super(generator, Constants.MODID, existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {

        // Stream.of(
        //
        //         )
        //         .map(Supplier::get)
        //         .forEach(this::simpleCubeBottomTopBlockState);
        //
        // Stream.of(
        //
        // ).map(Supplier::get)
        //         .forEach(this::simpleBlock);

    }

    protected void simpleCubeBottomTopBlockState(Block block) {
        simpleBlock(block, blockCubeTopModel(block));
    }

    protected BlockModelBuilder blockCubeTopModel(Block block) {
        String name = getName(block);
        return models().cubeBottomTop(name, modLoc("block/" + name + "_side"), modLoc("block/" + name + "_bottom"), modLoc("block/" + name + "_top"));
    }

    protected String getName(Block item) {
        return BuiltInRegistries.BLOCK.getKey(item).getPath();
    }
}
