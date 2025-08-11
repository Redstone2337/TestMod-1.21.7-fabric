package net.redstone233.test.mixin;

import net.minecraft.item.Items;
import net.minecraft.potion.Potions;
import net.minecraft.recipe.BrewingRecipeRegistry;
import net.minecraft.recipe.Ingredient;
import net.redstone233.test.items.ModItems;
import net.redstone233.test.core.loader.BrewingRecipeLoader;
import net.redstone233.test.potion.ModPotions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BrewingRecipeRegistry.class)
public class BrewingRecipeRegistryMixin {

    @Inject(method = "registerDefaults", at = @At("RETURN"))
    private static void registerDefaults(BrewingRecipeRegistry.Builder builder, CallbackInfo ci) {
        // 注册默认配方

        builder.registerPotionRecipe(
                Potions.AWKWARD,
                Ingredient.ofItem(ModItems.HE_QI_ZHENG),
                ModPotions.HE_QI_ZHENG
        );

        builder.registerPotionRecipe(
                Potions.MUNDANE,
                Ingredient.ofItem(ModItems.SILICON_INGOT),
                ModPotions.HE_QI_ZHENG
        );

        builder.registerPotionRecipe(
                Potions.THICK,
                Ingredient.ofItem(ModItems.SILICON),
                ModPotions.HE_QI_ZHENG
        );

        builder.registerPotionRecipe(
                ModPotions.HE_QI_ZHENG,
                Ingredient.ofItem(Items.REDSTONE),
                ModPotions.LONG_HE_QI_ZHENG
        );

       // builder.registerPotionType(ModItems.HE_QI_ZHENG);
//        builder.registerPotionRecipe(
//            Potions.AWKWARD,
//            Ingredient.ofItems(Items.REDSTONE),
//            (RegistryEntry<Potion>) ModItems.HE_QI_ZHENG
//        );

        // 加载动态配方（从资源包）
        BrewingRecipeLoader.RECIPES.forEach((id, recipe) -> {
            builder.registerPotionRecipe(
                recipe.input(),
                recipe.addition(),
                recipe.output()
            );
        });

        BrewingRecipeLoader.ITEM_RECIPES.forEach((id, recipe) -> {
            builder.registerItemRecipe(
                recipe.input().value(),
                recipe.addition(),
                recipe.output().value()
            );
        });

        // 其他硬编码配方（如果需要）
        // builder.registerPotionRecipe(Potions.AWKWARD, ModItems.FLYDRAGON, ModPotions.FIRE_POTION);
        // ...
    }
}
