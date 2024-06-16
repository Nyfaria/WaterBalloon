package com.nyfaria.waterballoon.datagen;

import com.nyfaria.waterballoon.init.ItemInit;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.Tags;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(PackOutput p_248933_, CompletableFuture<HolderLookup.Provider> p_323846_) {
        super(p_248933_, p_323846_);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeSaver) {
        BalloonRecipeBuilder.shaped(RecipeCategory.MISC, ItemInit.WATER_BALLOON.get(), 16)
                .pattern("#D#")
                .pattern("#B#")
                .pattern("#S#")
                .define('#', Items.PAPER)
                .define('D', Tags.Items.DYES)
                .define('B', Items.WATER_BUCKET)
                .define('S', Items.STRING)
                .unlockedBy("has_water_bucket", has(Items.WATER_BUCKET))
                .save(recipeSaver);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,ItemInit.BAZOOKA.get())
                .pattern("BBB")
                .pattern("SI ")
                .define('B', Items.IRON_BLOCK)
                .define('I', Items.IRON_INGOT)
                .define('S', Items.STICK)
                .unlockedBy("has_iron_ingot", has(Items.IRON_INGOT))
                .save(recipeSaver);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,ItemInit.SLING_SHOT.get())
                .pattern("SDS")
                .pattern(" S ")
                .pattern(" S ")
                .define('S', Items.STICK)
                .define('D', Items.STRING)
                .unlockedBy("has_string", has(Items.STRING))
                .save(recipeSaver);
    }
}
