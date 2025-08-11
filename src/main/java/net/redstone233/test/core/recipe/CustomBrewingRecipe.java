package net.redstone233.test.core.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.potion.Potion;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.entry.RegistryEntry;

public record CustomBrewingRecipe(
        String type,
        String group,
        RegistryEntry<Potion> input,
        Ingredient addition,
        RegistryEntry<Potion> output
) {
    public static final Codec<CustomBrewingRecipe> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.STRING.optionalFieldOf("group", "").forGetter(CustomBrewingRecipe::group),
                    Codec.STRING.fieldOf("type").forGetter(CustomBrewingRecipe::type),
                    Potion.CODEC.fieldOf("input").forGetter(CustomBrewingRecipe::input),
                    Ingredient.CODEC.fieldOf("addition").forGetter(CustomBrewingRecipe::addition),
                    Potion.CODEC.fieldOf("output").forGetter(CustomBrewingRecipe::output)
            ).apply(instance, CustomBrewingRecipe::new)
    );

    @Override
    public RegistryEntry<Potion> input() {
        return input;
    }

    @Override
    public String group() {
        return group;
    }

    @Override
    public String type() {
        return type;
    }

    @Override
    public Ingredient addition() {
        return addition;
    }

    @Override
    public RegistryEntry<Potion> output() {
        return output;
    }
}
