package com.nyfaria.waterballoon.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.nyfaria.waterballoon.init.RecipeInit;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.crafting.display.*;
import net.minecraft.world.level.Level;

import java.util.List;

public class BalloonRecipe extends NormalCraftingRecipe {
    public static final MapCodec<BalloonRecipe> MAP_CODEC = RecordCodecBuilder.mapCodec((i) -> i.group(CommonInfo.MAP_CODEC.forGetter((o) -> o.commonInfo), CraftingBookInfo.MAP_CODEC.forGetter((o) -> o.bookInfo), ShapedRecipePattern.MAP_CODEC.forGetter((o) -> o.pattern), ItemStackTemplate.CODEC.fieldOf("result").forGetter((o) -> o.result)).apply(i, BalloonRecipe::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, BalloonRecipe> STREAM_CODEC = StreamCodec.composite(CommonInfo.STREAM_CODEC, (o) -> o.commonInfo, CraftingBookInfo.STREAM_CODEC, (o) -> o.bookInfo, ShapedRecipePattern.STREAM_CODEC, (o) -> o.pattern, ItemStackTemplate.STREAM_CODEC, (o) -> o.result, BalloonRecipe::new);
    private final ShapedRecipePattern pattern;
    private final ItemStackTemplate result;

    public BalloonRecipe(Recipe.CommonInfo commonInfo, CraftingRecipe.CraftingBookInfo bookInfo, ShapedRecipePattern pattern, ItemStackTemplate result) {
        super(commonInfo, bookInfo);
        this.pattern = pattern;
        this.result = result;
    }


    public RecipeSerializer<BalloonRecipe> getSerializer() {
        return RecipeInit.BALLOON_RECIPE.get();
    }

    @Override
    protected PlacementInfo createPlacementInfo() {
        return PlacementInfo.createFromOptionals(this.pattern.ingredients());
    }


    public boolean canCraftInDimensions(int width, int height) {
        return width >= this.pattern.width() && height >= this.pattern.height();
    }

    public boolean matches(CraftingInput inv, Level level) {
        return this.pattern.matches(inv);
    }



    public ItemStack assemble(CraftingInput craftingContainer) {
        ItemStack itemStack = this.result.create();
        for (int i = 0; i < craftingContainer.size(); i++) {
            ItemStack stack = craftingContainer.getItem(i);
            if (stack.has(DataComponents.DYE)) {
                return DyedItemColor.applyDyes(itemStack, List.of(stack.getOrDefault(DataComponents.DYE, DyeColor.WHITE)));
            }
        }
        return itemStack;
    }

    public int getWidth() {
        return this.pattern.width();
    }

    public int getHeight() {
        return this.pattern.height();
    }

    public List<RecipeDisplay> display() {
        return List.of(new ShapedCraftingRecipeDisplay(this.pattern.width(), this.pattern.height(), this.pattern.ingredients().stream().map((e) -> (SlotDisplay)e.map(Ingredient::display).orElse(SlotDisplay.Empty.INSTANCE)).toList(), new SlotDisplay.ItemStackSlotDisplay(this.result), new SlotDisplay.ItemSlotDisplay(Items.CRAFTING_TABLE)));
    }

}
