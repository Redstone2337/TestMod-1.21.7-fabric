package net.redstone233.test.recipe.input;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.input.RecipeInput;

public record BrewingRecipeInput(ItemStack input, ItemStack addition) implements RecipeInput {

    @Override
    public ItemStack getStackInSlot(int slot) {
        return switch (slot) {
            case 0 -> this.input;
            case 1 -> this.addition;
            default -> throw new IllegalArgumentException("Invalid slot for brewing recipe: " + slot);
        };
    }

    @Override
    public int size() {
        return 2;
    }

    @Override
    public boolean isEmpty() {
        return this.input.isEmpty() && this.addition.isEmpty();
    }
}