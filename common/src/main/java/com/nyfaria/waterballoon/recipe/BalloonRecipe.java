package com.nyfaria.waterballoon.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.nyfaria.waterballoon.init.RecipeInit;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.minecraft.world.level.Level;

import java.util.List;

public class BalloonRecipe implements CraftingRecipe {
    final ShapedRecipePattern pattern;
    final ItemStack result;
    final String group;
    final CraftingBookCategory category;
    final boolean showNotification;

    public BalloonRecipe(String group, CraftingBookCategory category, ShapedRecipePattern pattern, ItemStack result, boolean showNotification) {
        this.group = group;
        this.category = category;
        this.pattern = pattern;
        this.result = result;
        this.showNotification = showNotification;
    }

    public BalloonRecipe(String group, CraftingBookCategory category, ShapedRecipePattern pattern, ItemStack result) {
        this(group, category, pattern, result, true);
    }

    public RecipeSerializer<?> getSerializer() {
        return RecipeInit.BALLOON_RECIPE.get();
    }

    public String getGroup() {
        return this.group;
    }

    public CraftingBookCategory category() {
        return this.category;
    }

    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return this.result;
    }

    public NonNullList<Ingredient> getIngredients() {
        return this.pattern.ingredients();
    }

    public boolean showNotification() {
        return this.showNotification;
    }

    public boolean canCraftInDimensions(int width, int height) {
        return width >= this.pattern.width() && height >= this.pattern.height();
    }

    public boolean matches(CraftingContainer inv, Level level) {
        return this.pattern.matches(inv);
    }

    public ItemStack assemble(CraftingContainer craftingContainer, HolderLookup.Provider registries) {
        ItemStack itemStack = this.getResultItem(registries).copy();
        return DyedItemColor.applyDyes(itemStack, List.of(((DyeItem) craftingContainer.getItem(1).getItem())));
    }

    public int getWidth() {
        return this.pattern.width();
    }

    public int getHeight() {
        return this.pattern.height();
    }

    public boolean isIncomplete() {
        NonNullList<Ingredient> nonNullList = this.getIngredients();
        return nonNullList.isEmpty() || nonNullList.stream().filter((ingredient) -> {
            return !ingredient.isEmpty();
        }).anyMatch((ingredient) -> {
            return ingredient.getItems().length == 0;
        });
    }

    public static class Serializer implements RecipeSerializer<BalloonRecipe> {
        public static final MapCodec<BalloonRecipe> CODEC = RecordCodecBuilder.mapCodec((instance) -> {
            return instance.group(Codec.STRING.optionalFieldOf("group", "").forGetter((shapedRecipe) -> {
                return shapedRecipe.group;
            }), CraftingBookCategory.CODEC.fieldOf("category").orElse(CraftingBookCategory.MISC).forGetter((shapedRecipe) -> {
                return shapedRecipe.category;
            }), ShapedRecipePattern.MAP_CODEC.forGetter((shapedRecipe) -> {
                return shapedRecipe.pattern;
            }), ItemStack.STRICT_CODEC.fieldOf("result").forGetter((shapedRecipe) -> {
                return shapedRecipe.result;
            }), Codec.BOOL.optionalFieldOf("show_notification", true).forGetter((shapedRecipe) -> {
                return shapedRecipe.showNotification;
            })).apply(instance, BalloonRecipe::new);
        });
        public static final StreamCodec<RegistryFriendlyByteBuf, BalloonRecipe> STREAM_CODEC = StreamCodec.of(BalloonRecipe.Serializer::toNetwork, BalloonRecipe.Serializer::fromNetwork);

        public Serializer() {
        }

        public MapCodec<BalloonRecipe> codec() {
            return CODEC;
        }

        public StreamCodec<RegistryFriendlyByteBuf, BalloonRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        private static BalloonRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
            String string = buffer.readUtf();
            CraftingBookCategory craftingBookCategory = (CraftingBookCategory)buffer.readEnum(CraftingBookCategory.class);
            ShapedRecipePattern shapedRecipePattern = (ShapedRecipePattern)ShapedRecipePattern.STREAM_CODEC.decode(buffer);
            ItemStack itemStack = (ItemStack)ItemStack.STREAM_CODEC.decode(buffer);
            boolean bl = buffer.readBoolean();
            return new BalloonRecipe(string, craftingBookCategory, shapedRecipePattern, itemStack, bl);
        }

        private static void toNetwork(RegistryFriendlyByteBuf buffer, BalloonRecipe recipe) {
            buffer.writeUtf(recipe.group);
            buffer.writeEnum(recipe.category);
            ShapedRecipePattern.STREAM_CODEC.encode(buffer, recipe.pattern);
            ItemStack.STREAM_CODEC.encode(buffer, recipe.result);
            buffer.writeBoolean(recipe.showNotification);
        }
    }
}
