package net.redstone233.test.core.recipe;

import net.minecraft.recipe.RecipeType;
import net.redstone233.test.recipe.BrewingRecipe;

public class BrewingRecipeType implements RecipeType<BrewingRecipe> {
    public static final BrewingRecipeType INSTANCE = new BrewingRecipeType();
    public static final String ID = "mtc:brewing";
}