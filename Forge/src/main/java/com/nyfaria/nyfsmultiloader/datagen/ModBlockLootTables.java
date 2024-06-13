package com.nyfaria.nyfsmultiloader.datagen;

import com.nyfaria.grinnersents.registration.RegistryObject;
import com.nyfaria.nyfsmultiloader.init.BlockInit;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DropExperienceBlock;

import java.util.stream.Stream;

public class ModBlockLootTables extends BlockLoot {
    @Override
    protected void addTables() {
        this.getBlockStream().filter(this::shouldDropSelf).forEach(this::dropSelf);
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return this.getBlockStream().filter(this::shouldGenerateLoot).toList();
    }

    protected Stream<Block> getBlockStream() {
        return BlockInit.BLOCKS.getEntries().stream().map(RegistryObject::get);
    }

    protected boolean shouldDropSelf(Block block) {
        return shouldGenerateLoot(block);
    }

    protected boolean shouldGenerateLoot(Block block) {
        return block.asItem() != Items.AIR && !(block instanceof DropExperienceBlock);
    }

}
