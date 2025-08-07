package net.redstone233.test.recipe;

import net.fabricmc.fabric.api.registry.FabricBrewingRecipeRegistryBuilder;
import net.minecraft.potion.Potion;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.entry.RegistryEntry;
import net.redstone233.test.TestMod;
import net.redstone233.test.core.recipe.BrewingRecipeLoader;

import java.util.Optional;

public class BrewingRecipeRegistry {
    public static void registerAll() {
        if (BrewingRecipeLoader.RECIPES.isEmpty()) {
            TestMod.LOGGER.warn("No brewing recipes found to register");
            return;
        }

        FabricBrewingRecipeRegistryBuilder.BUILD.register(builder -> {
            int potionRecipes = 0;
            int itemRecipes = 0;

            for (var entry : BrewingRecipeLoader.RECIPES.entrySet()) {
                try {
                    BrewingRecipe recipe = entry.getValue();
                    boolean isPotionRecipe = registerRecipe(builder, recipe);

                    if (isPotionRecipe) {
                        potionRecipes++;
                    } else {
                        itemRecipes++;
                    }
                } catch (Exception e) {
                    TestMod.LOGGER.error("Failed to register recipe {}", entry.getKey(), e);
                }
            }

            TestMod.LOGGER.info("Registered {} potion recipes and {} item recipes",
                    potionRecipes, itemRecipes);
        });
    }

    private static boolean registerRecipe(FabricBrewingRecipeRegistryBuilder builder, BrewingRecipe recipe) {
        Optional<RegistryEntry<Potion>> inputPotion = recipe.getInputPotion();
        Optional<RegistryEntry<Potion>> outputPotion = recipe.getOutputPotion();

        if (inputPotion.isPresent() && outputPotion.isPresent()) {
            builder.registerPotionRecipe(
                    inputPotion.get(),
                    Ingredient.ofItem(recipe.addition().getItem()),
                    outputPotion.get()
            );
            return true;
        } else {
            builder.registerItemRecipe(
                    recipe.input().getItem(),
                    Ingredient.ofItem(recipe.addition().getItem()),
                    recipe.output().getItem()
            );
            return false;
        }
    }
}
