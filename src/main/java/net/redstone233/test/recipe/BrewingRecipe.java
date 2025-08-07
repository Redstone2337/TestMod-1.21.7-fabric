package net.redstone233.test.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.registry.entry.RegistryEntry;

import java.util.Optional;

public record BrewingRecipe(
        String group,
        ItemStack input,
        ItemStack addition,
        ItemStack output
) {
    public static final Codec<BrewingRecipe> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.STRING.optionalFieldOf("group", "").forGetter(BrewingRecipe::group),
                    ItemStack.CODEC.fieldOf("input").forGetter(BrewingRecipe::input),
                    ItemStack.CODEC.fieldOf("addition").forGetter(BrewingRecipe::addition),
                    ItemStack.CODEC.fieldOf("output").forGetter(BrewingRecipe::output)
            ).apply(instance, BrewingRecipe::new)
    );

    public Optional<RegistryEntry<Potion>> getInputPotion() {
        return getPotionFromStack(input);
    }

    public Optional<RegistryEntry<Potion>> getOutputPotion() {
        return getPotionFromStack(output);
    }

    private static Optional<RegistryEntry<Potion>> getPotionFromStack(ItemStack stack) {
        PotionContentsComponent potionContents = stack.get(DataComponentTypes.POTION_CONTENTS);
        return potionContents != null ? potionContents.potion() : Optional.empty();
    }
}
