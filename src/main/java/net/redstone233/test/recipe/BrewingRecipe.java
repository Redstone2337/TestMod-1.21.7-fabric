package net.redstone233.test.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.*;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.redstone233.test.recipe.input.BrewingRecipeInput;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class BrewingRecipe implements Recipe<BrewingRecipeInput> {
    private final String group;
    private final ItemStack input;
    private final ItemStack addition;
    private final ItemStack output;
    private Identifier id;
    @Nullable
    private IngredientPlacement ingredientPlacement;

    public BrewingRecipe(String group, ItemStack input, ItemStack addition, ItemStack output) {
        this.group = group;
        this.input = input;
        this.addition = addition;
        this.output = output;
    }

    @Override
    public boolean matches(BrewingRecipeInput inventory, World world) {
        ItemStack inputStack = inventory.getStackInSlot(0);
        ItemStack additionStack = inventory.getStackInSlot(1);

        return ItemStack.areItemsAndComponentsEqual(input, inputStack) &&
                ItemStack.areItemsAndComponentsEqual(addition, additionStack);
    }

    @Override
    public ItemStack craft(BrewingRecipeInput inventory, RegistryWrapper.WrapperLookup registryLookup) {
        ItemStack result = output.copy();
        result.setCount(inventory.getStackInSlot(0).getCount());
        return result;
    }

//    @Override
//    public boolean fits(int width, int height) {
//        return width * height >= 2;
//    }
//
//    @Override
//    public ItemStack getResult(RegistryWrapper.WrapperLookup registryLookup) {
//        return output;
//    }

    @Override
    public String getGroup() {
        return group;
    }

    @Override
    public RecipeSerializer<? extends Recipe<BrewingRecipeInput>> getSerializer() {
        return null;
    }

    @Override
    public RecipeType<? extends Recipe<BrewingRecipeInput>> getType() {
        return null;
    }

//    @Override
//    public Identifier getId() {
//        return id;
//    }

    public void setId(Identifier id) {
        this.id = id;
    }

    public ItemStack getInput() {
        return input;
    }

    public ItemStack getAddition() {
        return addition;
    }

    public ItemStack getOutput() {
        return output;
    }

//    @Override
//    public RecipeSerializer<?> getSerializer() {
//        return BrewingRecipeSerializer.INSTANCE;
//    }
//
//    @Override
//    public RecipeType<?> getType() {
//        return BrewingRecipeType.INSTANCE;
//    }

    @Override
    public IngredientPlacement getIngredientPlacement() {
        if (this.ingredientPlacement == null) {
            // 修正为使用Ingredient.ofItems()
            this.ingredientPlacement = IngredientPlacement.forMultipleSlots(
                    List.of(
                            Optional.of(Ingredient.ofItems(input.getItem())),
                            Optional.of(Ingredient.ofItems(addition.getItem()))
                    )
            );
        }
        return this.ingredientPlacement;
    }

    @Override
    public RecipeBookCategory getRecipeBookCategory() {
        return null;
    }
}