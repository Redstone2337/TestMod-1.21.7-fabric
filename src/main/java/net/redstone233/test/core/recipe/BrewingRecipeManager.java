package net.redstone233.test.core.recipe;

/*
import net.fabricmc.fabric.api.registry.FabricBrewingRecipeRegistryBuilder;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.recipe.*;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.redstone233.test.recipe.BrewingRecipe;
import net.redstone233.test.recipe.input.BrewingRecipeInput;

import java.util.*;

public class BrewingRecipeManager {
    public static void registerRecipes(MinecraftServer server) {
//        ServerRecipeManager recipeManager = server.getRecipeManager();
//
//        // 获取所有配方的最可靠方法
//        Collection<RecipeEntry<?>> allRecipes = recipeManager.values();
//        List<RecipeEntry<BrewingRecipe>> brewingRecipes = new ArrayList<>();
//
//        // 过滤出酿造配方
//        for (RecipeEntry<?> entry : allRecipes) {
//            if (entry.value().getType() == BrewingRecipeType.INSTANCE) {
//                brewingRecipes.add((RecipeEntry<BrewingRecipe>) entry);
//            }
//        }
//
//        // 注册找到的配方
//        for (RecipeEntry<BrewingRecipe> entry : brewingRecipes) {
//            registerBrewingRecipe(entry.value());
//        }

        ServerRecipeManager recipeManager = server.getRecipeManager();

        // 获取所有配方
        Collection<RecipeEntry<?>> allRecipes = recipeManager.values();

        // 安全过滤酿造配方
        List<BrewingRecipe> brewingRecipes = allRecipes.stream()
                .filter(entry -> entry.value().getType() == BrewingRecipeType.INSTANCE)
                .map(entry -> safeCastToBrewingRecipe(entry.value()))
                .filter(Objects::nonNull)
                .toList();

        // 注册找到的配方
        brewingRecipes.forEach(BrewingRecipeManager::registerBrewingRecipe);
    }

    private static BrewingRecipe safeCastToBrewingRecipe(Recipe<?> recipe) {
        if (recipe instanceof BrewingRecipe) {
            return (BrewingRecipe) recipe;
        }
        return null;
    }

    private static void registerBrewingRecipe(BrewingRecipe recipe) {
        ItemStack input = recipe.getInput();
        ItemStack addition = recipe.getAddition();
        ItemStack output = recipe.getOutput();

        Optional<RegistryEntry<Potion>> inputPotion = getPotionEntry(input);
        Optional<RegistryEntry<Potion>> outputPotion = getPotionEntry(output);

        FabricBrewingRecipeRegistryBuilder.BUILD.register(builder -> {
            if (inputPotion.isPresent() && outputPotion.isPresent() && addition.getItem() != null) {
                builder.registerPotionRecipe(
                        inputPotion.get(),
                        addition.getItem(),
                        outputPotion.get()
                );
            } else if (input.getItem() != null && addition.getItem() != null && output.getItem() != null) {
                builder.registerItemRecipe(
                        input.getItem(),
                        addition.getItem(),
                        output.getItem()
                );
            }
        });
    }

    private static Optional<RegistryEntry<Potion>> getPotionEntry(ItemStack stack) {
        if (stack.contains(DataComponentTypes.POTION_CONTENTS)) {
            PotionContentsComponent potionContents = stack.get(DataComponentTypes.POTION_CONTENTS);
            if (potionContents != null) {
                return potionContents.potion();
            }
        }
        return Optional.empty();
    }

    public static BrewingRecipe findMatchingRecipe(BrewingRecipeInput input, World world) {
        if (world.isClient) {
            return null;
        }

        MinecraftServer server = world.getServer();
        if (server == null) return null;

        return server.getRecipeManager()
                .getFirstMatch(BrewingRecipeType.INSTANCE, input, world)
                .map(RecipeEntry::value)
                .orElse(null);
    }
}*/
import net.fabricmc.fabric.api.registry.FabricBrewingRecipeRegistryBuilder;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.ServerRecipeManager;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.redstone233.test.TestMod;
import net.redstone233.test.recipe.BrewingRecipe;
import net.redstone233.test.recipe.input.BrewingRecipeInput;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class BrewingRecipeManager {
    public static void registerRecipes(MinecraftServer server) {
        try {
            ServerRecipeManager recipeManager = server.getRecipeManager();
            Collection<RecipeEntry<?>> allRecipes = recipeManager.values();
            List<BrewingRecipe> validRecipes = new ArrayList<>();

            for (RecipeEntry<?> entry : allRecipes) {
                try {
                    Recipe<?> recipe = entry.value();
                    if (recipe.getType() == BrewingRecipeType.INSTANCE) {
                        if (entry.id() == null) {
                            TestMod.LOGGER.error("Recipe has null ID: {}", recipe);
                            continue;
                        }

                        if (!(recipe instanceof BrewingRecipe brewingRecipe)) {
                            TestMod.LOGGER.error("Expected BrewingRecipe but got {} for ID {}",
                                    recipe.getClass(), entry.id());
                            continue;
                        }

                        brewingRecipe.setId(entry.id().getRegistry());
                        validRecipes.add(brewingRecipe);
                    }
                } catch (Exception e) {
                    TestMod.LOGGER.error("Error processing recipe entry", e);
                }
            }

            TestMod.LOGGER.info("Registering {} brewing recipes", validRecipes.size());
            validRecipes.forEach(BrewingRecipeManager::registerBrewingRecipe);
        } catch (Exception e) {
            TestMod.LOGGER.error("Failed to register brewing recipes", e);
            throw e;
        }
    }

    private static void registerBrewingRecipe(BrewingRecipe recipe) {
        try {
            ItemStack input = recipe.getInput();
            ItemStack addition = recipe.getAddition();
            ItemStack output = recipe.getOutput();

            if (input.isEmpty() || addition.isEmpty() || output.isEmpty()) {
                TestMod.LOGGER.error("Empty stack in brewing recipe {}: input={}, addition={}, output={}",
                        recipe.getId(), input, addition, output);
                return;
            }

            Optional<RegistryEntry<Potion>> inputPotion = getPotionEntry(input);
            Optional<RegistryEntry<Potion>> outputPotion = getPotionEntry(output);

            FabricBrewingRecipeRegistryBuilder.BUILD.register(builder -> {
                try {
                    if (inputPotion.isPresent() && outputPotion.isPresent()) {
                        builder.registerPotionRecipe(
                                inputPotion.get(),
                                addition.getItem(),
                                outputPotion.get()
                        );
                        TestMod.LOGGER.debug("Registered potion recipe: {}", recipe.getId());
                    } else {
                        builder.registerItemRecipe(
                                input.getItem(),
                                addition.getItem(),
                                output.getItem()
                        );
                        TestMod.LOGGER.debug("Registered item recipe: {}", recipe.getId());
                    }
                } catch (Exception e) {
                    TestMod.LOGGER.error("Failed to register recipe {}", recipe.getId(), e);
                }
            });
        } catch (Exception e) {
            TestMod.LOGGER.error("Failed to process brewing recipe {}", recipe.getId(), e);
        }
    }

    private static Optional<RegistryEntry<Potion>> getPotionEntry(ItemStack stack) {
        try {
            if (stack.contains(DataComponentTypes.POTION_CONTENTS)) {
                PotionContentsComponent potionContents = stack.get(DataComponentTypes.POTION_CONTENTS);
                if (potionContents != null) {
                    return potionContents.potion();
                }
            }
            return Optional.empty();
        } catch (Exception e) {
            TestMod.LOGGER.error("Error getting potion entry from stack", e);
            return Optional.empty();
        }
    }

    public static BrewingRecipe findMatchingRecipe(BrewingRecipeInput input, World world) {
        try {
            if (world.isClient) {
                return null;
            }

            MinecraftServer server = world.getServer();
            if (server == null) return null;

            return server.getRecipeManager()
                    .getFirstMatch(BrewingRecipeType.INSTANCE, input, world)
                    .map(RecipeEntry::value)
                    .orElse(null);
        } catch (Exception e) {
            TestMod.LOGGER.error("Error finding matching recipe", e);
            return null;
        }
    }}
