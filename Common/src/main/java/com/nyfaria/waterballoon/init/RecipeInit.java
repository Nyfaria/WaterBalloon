package com.nyfaria.waterballoon.init;

import com.nyfaria.grinnersents.registration.RegistrationProvider;
import com.nyfaria.grinnersents.registration.RegistryObject;
import com.nyfaria.waterballoon.Constants;
import com.nyfaria.waterballoon.recipe.BalloonRecipe;
import net.minecraft.core.Registry;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class RecipeInit {
    public static final RegistrationProvider<RecipeSerializer<?>> RECIPE_SERIALIZERS = RegistrationProvider.get(Registry.RECIPE_SERIALIZER, Constants.MODID);
    public static final RegistryObject<RecipeSerializer<BalloonRecipe>> BALLOON_RECIPE = RECIPE_SERIALIZERS.register("balloon", BalloonRecipe.Serializer::new);

    public static void loadClass() {
    }
}
