package com.nyfaria.waterballoon.datagen;

import com.nyfaria.waterballoon.init.ItemInit;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;

import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> recipeSaver) {
        BalloonRecipeBuilder.shaped(ItemInit.WATER_BALLOON.get(), 3)
                .pattern("#D#")
                .pattern("#B#")
                .pattern("#S#")
                .define('#', Items.PAPER)
                .define('D', Tags.Items.DYES)
                .define('B', Items.WATER_BUCKET)
                .define('S', Items.STRING)
                .unlockedBy("has_water_bucket", has(Items.WATER_BUCKET))
                .save(recipeSaver);
    }
}
