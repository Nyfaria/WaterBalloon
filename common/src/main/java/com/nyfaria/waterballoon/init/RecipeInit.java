package com.nyfaria.waterballoon.init;

import com.nyfaria.waterballoon.Constants;
import com.nyfaria.waterballoon.recipe.BalloonRecipe;
import com.nyfaria.waterballoon.registration.RegistrationProvider;
import com.nyfaria.waterballoon.registration.RegistryObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class RecipeInit {
    public static final RegistrationProvider<RecipeSerializer<?>> RECIPE_SERIALIZERS = RegistrationProvider.get(BuiltInRegistries.RECIPE_SERIALIZER, Constants.MODID);
    public static final RegistryObject<RecipeSerializer<?>,RecipeSerializer<BalloonRecipe>> BALLOON_RECIPE = RECIPE_SERIALIZERS.register("balloon", BalloonRecipe.Serializer::new);

    public static void loadClass() {
    }
}
