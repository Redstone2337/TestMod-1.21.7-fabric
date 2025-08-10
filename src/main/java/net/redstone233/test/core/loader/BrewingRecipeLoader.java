/*
package net.redstone233.test.core.loader;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.serialization.JsonOps;
import net.fabricmc.fabric.api.registry.FabricBrewingRecipeRegistryBuilder;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.recipe.BrewingRecipeRegistry;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.util.Identifier;
import net.minecraft.item.Items;
import net.minecraft.item.Item;
import net.minecraft.recipe.Ingredient;
import net.minecraft.potion.Potions;
import net.redstone233.test.TestMod;
import net.redstone233.test.items.ModItems;
import net.redstone233.test.core.recipe.BrewingItemRecipe;
import net.redstone233.test.core.recipe.CustomBrewingRecipe;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class BrewingRecipeLoader implements SimpleSynchronousResourceReloadListener {
    private static final String PATH = "brewing";
    private static FeatureSet enabledFeatures;
    public static final Map<Identifier, CustomBrewingRecipe> RECIPES = new HashMap<>();
    public static final Map<Identifier, BrewingItemRecipe> ITEM_RECIPES = new HashMap<>();
    private static int loadedCount = 0;
    private static int loadedCount1 = 0;
    private final BrewingRecipeRegistry.Builder builder = new BrewingRecipeRegistry.Builder(enabledFeatures);
//	private final BrewingRecipeRegistry.Builder builder = create(enabledFeatures).Builder;

    @Override
    public void reload(ResourceManager manager) {
        RECIPES.clear();
        //registerDefaults(builder);
        
        // 清除现有配方（Fabric API没有提供直接清除方法，需要重新构建）
        FabricBrewingRecipeRegistryBuilder.BUILD.register(builder -> {
            // 清空构建器（实际Fabric API会自动合并而不是覆盖）
            // 这里依赖Fabric API的内部实现，可能需要根据版本调整
        });

        // 修改后的reload方法片段
        for (Identifier resourceId : manager.findResources(PATH, path -> path.getPath().endsWith(".json")).keySet()) {
            Optional<Resource> resourceOpt = manager.getResource(resourceId);
            if (resourceOpt.isEmpty()) {
                TestMod.LOGGER.error("Resource not found: {}", resourceId);
                continue;
            }

            
            try (InputStream stream = resourceOpt.get().getInputStream();
                 Reader reader = new InputStreamReader(stream)) {

                JsonElement json = JsonParser.parseReader(reader);
                CustomBrewingRecipe recipe = CustomBrewingRecipe.CODEC.parse(JsonOps.INSTANCE, json)
                        .getOrThrow();

                BrewingItemRecipe recipe1 = BrewingItemRecipe.CODEC.parse(JsonOps.INSTANCE, json)
                        .getOrThrow();

                if ("cmr:brewing".equals(recipe.type()) && "potion".equals(recipe.group())) {
                    Identifier recipeId = Identifier.of(
                            resourceId.getNamespace(),
                            resourceId.getPath().replace(PATH + "/", "").replace(".json", "")
                    );
                    RECIPES.put(recipeId, recipe);
                    registerRecipe(recipe, builder);
                    loadedCount++;
                }

                if ("item".equals(recipe1.group())) {
                    Identifier recipeId = Identifier.of(
                            resourceId.getNamespace(),
                            resourceId.getPath().replace(PATH + "/", "").replace(".json", "")
                    );
                    ITEM_RECIPES.put(recipeId, recipe1);
                    registerRecipe(recipe1, builder);
                    loadedCount1++;
                }
            } catch (Exception e) {
                TestMod.LOGGER.error("Failed to load recipe {}: {}", resourceId, e.getMessage());
            }
            
        }

        TestMod.LOGGER.info("Loaded {} brewing recipes", loadedCount);
    }

    private void registerRecipe(CustomBrewingRecipe recipe, BrewingRecipeRegistry.Builder builder) {
            builder.registerPotionRecipe(
                    recipe.input(),
                    recipe.addition(),
                    recipe.output()
            );
    }

    private void registerRecipe(BrewingItemRecipe recipe, BrewingRecipeRegistry.Builder builder) {
            builder.registerItemRecipe(
                    recipe.input().value(),
                    recipe.addition(),
                    recipe.output().value()
            );
    }

    public static void registerDefaults(BrewingRecipeRegistry.Builder builder) {
        builder.registerItemRecipe(
                    (Item)Potions.WATER,
                    Ingredient.ofItem(ModItems.SILICON_INGOT),
                    ModItems.HE_QI_ZHENG
            );
    }

    @Override
    public Identifier getFabricId() {
        return Identifier.of(TestMod.MOD_ID, "brewing_recipe_loader");
    }

    public static void register() {
        ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new BrewingRecipeLoader());
        //create(enabledFeatures)
    }

public static BrewingRecipeRegistry create(FeatureSet enabledFeatures) {
	BrewingRecipeRegistry.Builder builder = new BrewingRecipeRegistry.Builder(enabledFeatures);
	registerDefaults(builder);
	return builder.build();
}

}*/

package net.redstone233.test.core.loader;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.serialization.JsonOps;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import net.redstone233.test.TestMod;
import net.redstone233.test.core.recipe.BrewingItemRecipe;
import net.redstone233.test.core.recipe.CustomBrewingRecipe;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class BrewingRecipeLoader implements SimpleSynchronousResourceReloadListener {
    private static final String PATH = "brewing";
    public static final Map<Identifier, CustomBrewingRecipe> RECIPES = new HashMap<>();
    public static final Map<Identifier, BrewingItemRecipe> ITEM_RECIPES = new HashMap<>();

    @Override
    public void reload(ResourceManager manager) {
        RECIPES.clear();
        ITEM_RECIPES.clear();

        for (Identifier resourceId : manager.findResources(PATH, path -> path.getPath().endsWith(".json")).keySet()) {
            Optional<Resource> resourceOpt = manager.getResource(resourceId);
            if (resourceOpt.isEmpty()) {
                TestMod.LOGGER.error("Resource not found: {}", resourceId);
                continue;
            }

            try (InputStream stream = resourceOpt.get().getInputStream();
                 Reader reader = new InputStreamReader(stream)) {

                JsonElement json = JsonParser.parseReader(reader);
                CustomBrewingRecipe recipe = CustomBrewingRecipe.CODEC.parse(JsonOps.INSTANCE, json)
                        .getOrThrow();

                BrewingItemRecipe recipe1 = BrewingItemRecipe.CODEC.parse(JsonOps.INSTANCE, json)
                        .getOrThrow();

                String replace = resourceId.getPath().replace(PATH + "/", "").replace(".json", "");
                if ("cmr:brewing".equals(recipe.type()) && "potion".equals(recipe.group())) {
                    Identifier recipeId = Identifier.of(
                            resourceId.getNamespace(),
                            replace
                    );
                    RECIPES.put(recipeId, recipe);
                }

                if ("item".equals(recipe1.group())) {
                    Identifier recipeId = Identifier.of(
                            resourceId.getNamespace(),
                            replace
                    );
                    ITEM_RECIPES.put(recipeId, recipe1);
                }
            } catch (Exception e) {
                TestMod.LOGGER.error("Failed to load recipe {}: {}", resourceId, e.getMessage());
            }
        }

        TestMod.LOGGER.info("Loaded {} brewing recipes", RECIPES.size() + ITEM_RECIPES.size());
    }

    @Override
    public Identifier getFabricId() {
        return Identifier.of(TestMod.MOD_ID, "brewing_recipe_loader");
    }

    public static void register() {
        ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new BrewingRecipeLoader());
    }
}
