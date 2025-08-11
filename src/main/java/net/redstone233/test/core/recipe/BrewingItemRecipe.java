package net.redstone233.test.core.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.entry.RegistryEntry;

public record BrewingItemRecipe(
        String type,      // 固定为 "mtc:brewing_item"
        String group,     // 配方分组
        RegistryEntry<Item> input,
        Ingredient addition,
        RegistryEntry<Item> output
) {
    public static final Codec<BrewingItemRecipe> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.STRING.fieldOf("type").forGetter(BrewingItemRecipe::type),
                    Codec.STRING.fieldOf("group").forGetter(BrewingItemRecipe::group),
                    Item.ENTRY_CODEC.fieldOf("input_item").forGetter(BrewingItemRecipe::input),
                    Ingredient.CODEC.fieldOf("addition").forGetter(BrewingItemRecipe::addition),
                    Item.ENTRY_CODEC.fieldOf("output_item").forGetter(BrewingItemRecipe::output)
            ).apply(instance, BrewingItemRecipe::new)
    );

    public ItemStack createInputStack() {
        return new ItemStack(input.value());
    }

    public ItemStack createOutputStack() {
        return new ItemStack(output.value());
    }

    public boolean isValid() {
        return "mtc:brewing_item".equals(type);
    }

    @Override
    public String type() {
        return type;
    }

    @Override
    public String group() {
        return group;
    }

    @Override
    public Ingredient addition() {
        return addition;
    }

    @Override
    public RegistryEntry<Item> input() {
        return input;
    }

    @Override
    public RegistryEntry<Item> output() {
        return output;
    }
}
