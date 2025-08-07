package net.redstone233.test.core.recipe;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.redstone233.test.TestMod;
import net.redstone233.test.recipe.BrewingRecipe;
import net.redstone233.test.recipe.BrewingRecipeRegistry;

import java.io.InputStreamReader;
import java.util.*;

public class BrewingRecipeLoader implements SimpleSynchronousResourceReloadListener {
    private static final Gson GSON = new Gson();
    private static final List<String> RECIPE_PATHS = List.of(
            "brewing_recipes",  // 自定义路径优先
            "recipes"           // 原版路径次之
    );

    public static final Map<Identifier, BrewingRecipe> RECIPES = new HashMap<>();

    @Override
    public Identifier getFabricId() {
        return Identifier.of(TestMod.MOD_ID, "brewing_recipe_loader");
    }

    @Override
    public void reload(ResourceManager manager) {
        RECIPES.clear();
        Map<String, Integer> pathCounts = new HashMap<>();

        // 按优先级顺序加载路径
        for (String path : RECIPE_PATHS) {
            int count = 0;
            for (Map.Entry<Identifier, Resource> entry : findRecipesInPath(manager, path)) {
                try {
                    Identifier recipeId = createRecipeId(entry.getKey(), path);
                    if (loadRecipe(entry.getValue(), recipeId)) {
                        count++;
                    }
                } catch (Exception e) {
                    TestMod.LOGGER.error("Error loading recipe from {}", entry.getKey(), e);
                }
            }
            pathCounts.put(path, count);
        }

        // 打印加载统计
        pathCounts.forEach((path, count) ->
                TestMod.LOGGER.info("Loaded {} recipes from '{}' path", count, path));
        TestMod.LOGGER.info("Total brewing recipes loaded: {}", RECIPES.size());

        // 注册到酿造系统
        BrewingRecipeRegistry.registerAll();
    }

    private Collection<Map.Entry<Identifier, Resource>> findRecipesInPath(ResourceManager manager, String path) {
        return manager.findResources(path, id ->
                id.getNamespace().equals(TestMod.MOD_ID) &&
                        id.getPath().endsWith(".json")).entrySet();
    }

    private Identifier createRecipeId(Identifier resourceId, String path) {
        String pathPrefix = path + "/";
        String recipePath = resourceId.getPath();
        recipePath = recipePath.substring(recipePath.indexOf(pathPrefix) + pathPrefix.length());
        return Identifier.of(
                resourceId.getNamespace(),
                recipePath.substring(0, recipePath.length() - 5) // 移除.json
        );
    }

    private boolean loadRecipe(Resource resource, Identifier recipeId) throws Exception {
        try (InputStreamReader reader = new InputStreamReader(resource.getInputStream())) {
            JsonElement json = GSON.fromJson(reader, JsonElement.class);

            if (!isBrewingRecipe(json)) {
                return false;
            }

            BrewingRecipe recipe = BrewingRecipe.CODEC.parse(JsonOps.INSTANCE, json)
                    .getOrThrow();

            // 自定义路径的配方会覆盖原版路径的同名配方
            RECIPES.put(recipeId, recipe);
            return true;
        }
    }

    private boolean isBrewingRecipe(JsonElement json) {
        if (!json.isJsonObject()) return false;
        JsonObject obj = json.getAsJsonObject();
        return obj.has("type") && obj.get("type").getAsString().equals("mtc:brewing");
    }
}
