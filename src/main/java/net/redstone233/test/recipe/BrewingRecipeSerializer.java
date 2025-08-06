package net.redstone233.test.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.recipe.RecipeSerializer;

public class BrewingRecipeSerializer implements RecipeSerializer<BrewingRecipe> {
    public static final BrewingRecipeSerializer INSTANCE = new BrewingRecipeSerializer();

    private static final MapCodec<BrewingRecipe> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                            Codec.STRING.optionalFieldOf("group", "").forGetter(BrewingRecipe::getGroup),
                            ItemStack.UNCOUNTED_CODEC.fieldOf("input").forGetter(BrewingRecipe::getInput),
                            ItemStack.UNCOUNTED_CODEC.fieldOf("addition").forGetter(BrewingRecipe::getAddition),
                            ItemStack.UNCOUNTED_CODEC.fieldOf("output").forGetter(BrewingRecipe::getOutput)
                    )
                    .apply(instance, BrewingRecipe::new)
    );

    public static final PacketCodec<RegistryByteBuf, BrewingRecipe> PACKET_CODEC = PacketCodec.ofStatic(
            BrewingRecipeSerializer::write, BrewingRecipeSerializer::read
    );

    @Override
    public MapCodec<BrewingRecipe> codec() {
        return CODEC;
    }

    @Override
    public PacketCodec<RegistryByteBuf, BrewingRecipe> packetCodec() {
        return PACKET_CODEC;
    }

    private static BrewingRecipe read(RegistryByteBuf buf) {
        String group = buf.readString();
        ItemStack input = ItemStack.PACKET_CODEC.decode(buf);
        ItemStack addition = ItemStack.PACKET_CODEC.decode(buf);
        ItemStack output = ItemStack.PACKET_CODEC.decode(buf);
        return new BrewingRecipe(group, input, addition, output);
    }

    private static void write(RegistryByteBuf buf, BrewingRecipe recipe) {
        buf.writeString(recipe.getGroup());
        ItemStack.PACKET_CODEC.encode(buf, recipe.getInput());
        ItemStack.PACKET_CODEC.encode(buf, recipe.getAddition());
        ItemStack.PACKET_CODEC.encode(buf, recipe.getOutput());
    }
}