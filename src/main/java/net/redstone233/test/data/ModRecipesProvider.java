package net.redstone233.test.data;

import com.google.common.collect.ImmutableList;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.impl.resource.loader.ModNioResourcePack;
import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.data.recipe.RecipeGenerator;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;
import net.redstone233.test.TestMod;
import net.redstone233.test.blocks.ModBlocks;
import net.redstone233.test.core.tags.ModItemTags;
import net.redstone233.test.items.ModItems;
import org.joml.Matrix3d;

import java.util.concurrent.CompletableFuture;

public class ModRecipesProvider extends FabricRecipeProvider {
    public ModRecipesProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    private static final ImmutableList<ItemConvertible> SILICON_ORES = ImmutableList.of(ModBlocks.SILICON_ORE, ModBlocks.DEEPSLATE_SILICON_ORE);

    @Override
    protected RecipeGenerator getRecipeGenerator(RegistryWrapper.WrapperLookup wrapperLookup, RecipeExporter recipeExporter) {
        return new RecipeGenerator(wrapperLookup,recipeExporter) {
            @Override
            public void generate() {
                createShaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.SILICON_BLOCK)
                        .pattern("###")
                        .pattern("###")
                        .pattern("###")
                        .input('#', ModItems.SILICON_INGOT)
                        .criterion("has_silicon", conditionsFromItem(ModItems.SILICON))
                        .offerTo(recipeExporter);

                createShaped(RecipeCategory.FOOD, ModItems.BLUE_CHEESE)
                        .pattern("AAA")
                        .pattern("BCB")
                        .pattern("DDD")
                                .input('A', Items.MILK_BUCKET)
                                .input('B', Items.SUGAR)
                                .input('C', Items.BLUE_DYE)
                                .input('D', Items.WHEAT)
                        .criterion("has_sugar", conditionsFromItem(Items.SUGAR))
                        .offerTo(recipeExporter);

                createShapeless(RecipeCategory.FOOD, ModItems.HE_QI_ZHENG)
                        .input(Ingredient.ofItem(Items.GLASS_BOTTLE))
                        .input(Ingredient.ofItem(ModItems.SILICON))
                        .criterion("has_glass",conditionsFromItem(Items.GLASS))
                        .offerTo(recipeExporter);

//                createShaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.RAW_SILICON_BLOCK)
//                        .pattern("###")
//                        .pattern("###")
//                        .pattern("###")
//                        .input('#', ModItems.RAW_SILICON)
//                        .criterion("has_raw_silicon", conditionsFromItem(ModItems.RAW_SILICON))
//                        .offerTo(recipeExporter);

//                createShaped(RecipeCategory.COMBAT,ModItems.FREEZE_SWORD)
//                        .pattern(" A ")
//                        .pattern("BAB")
//                        .pattern(" C ")
//                        .input('A', Items.PACKED_ICE)
//                        .input('B', Items.BLUE_ICE)
//                        .input('C', Items.STICK)
//                        .criterion("has_packed_ice", conditionsFromItem(Items.PACKED_ICE))
//                        .criterion("has_blue_ice", conditionsFromItem(Items.BLUE_ICE))
//                        .offerTo(recipeExporter);

                createShapeless(RecipeCategory.MISC, ModItems.SILICON_INGOT, 9)
                        .input(Ingredient.ofItem(ModBlocks.SILICON_BLOCK))
                        .criterion("has_silicon_block", conditionsFromItem(ModBlocks.SILICON_BLOCK))
                        .offerTo(recipeExporter);

                createStairsRecipe(ModBlocks.SILICON_BLOCK_STAIRS, Ingredient.ofItem(ModBlocks.SILICON_BLOCK))
                        .criterion("has_silicon_block", conditionsFromItem(ModBlocks.SILICON_BLOCK))
                        .offerTo(recipeExporter);

                createSlabRecipe(RecipeCategory.BUILDING_BLOCKS,ModBlocks.SILICON_BLOCK_SLAB,Ingredient.ofItem(ModBlocks.SILICON_BLOCK))
                        .criterion("has_silicon_block", conditionsFromItem(ModBlocks.SILICON_BLOCK))
                        .offerTo(recipeExporter);

                createButtonRecipe(ModBlocks.SILICON_BUTTON, Ingredient.ofItem(ModItems.SILICON_INGOT))
                        .criterion("has_silicon_ingot", conditionsFromItem(ModItems.SILICON_INGOT))
                        .offerTo(recipeExporter);

                createPressurePlateRecipe(RecipeCategory.REDSTONE,ModBlocks.SILICON_PRESSURE_PLATE,Ingredient.ofItem(ModItems.SILICON_INGOT))
                        .criterion("has_silicon_ingot", conditionsFromItem(ModItems.SILICON_INGOT))
                                .offerTo(recipeExporter);

                createDoorRecipe(ModBlocks.SILICON_DOOR, Ingredient.ofItem(ModBlocks.SILICON_BLOCK))
                        .criterion("has_silicon_block", conditionsFromItem(ModBlocks.SILICON_BLOCK))
                        .offerTo(recipeExporter);

                createTrapdoorRecipe(ModBlocks.SILICON_TRAPDOOR, Ingredient.ofItem(ModBlocks.SILICON_BLOCK))
                        .criterion("has_silicon_block", conditionsFromItem(ModBlocks.SILICON_BLOCK))
                        .offerTo(recipeExporter);

                createFenceRecipe(ModBlocks.SILICON_FENCE, Ingredient.ofItem(ModBlocks.SILICON_BLOCK))
                        .criterion("has_silicon_block", conditionsFromItem(ModBlocks.SILICON_BLOCK))
                        .offerTo(recipeExporter);

                createFenceGateRecipe(ModBlocks.SILICON_FENCE_GATE, Ingredient.ofItem(ModBlocks.SILICON_BLOCK))
                        .criterion("has_silicon_block", conditionsFromItem(ModBlocks.SILICON_BLOCK))
                        .offerTo(recipeExporter);

                offerSmelting(SILICON_ORES, RecipeCategory.MISC, ModItems.SILICON,200,300,"silicon");

                offerBlasting(SILICON_ORES, RecipeCategory.MISC, ModItems.SILICON, 200, 150, "silicon");

                offerReversibleCompactingRecipes(RecipeCategory.MISC, ModItems.RAW_SILICON, RecipeCategory.BUILDING_BLOCKS, ModBlocks.RAW_SILICON_BLOCK);

            }
        };
    }

    @Override
    public String getName() {
        return TestMod.MOD_ID;
    }
}
