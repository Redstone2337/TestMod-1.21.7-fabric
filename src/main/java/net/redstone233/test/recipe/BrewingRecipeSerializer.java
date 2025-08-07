package net.redstone233.test.recipe;


/*
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
}*/
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import net.redstone233.test.TestMod;

public class BrewingRecipeSerializer implements RecipeSerializer<BrewingRecipe> {
    public static final BrewingRecipeSerializer INSTANCE = new BrewingRecipeSerializer();

    private static final MapCodec<BrewingRecipe> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                            Codec.STRING.optionalFieldOf("group", "").forGetter(BrewingRecipe::getGroup),
                            ItemStack.CODEC.fieldOf("input").forGetter(BrewingRecipe::getInput),
                            ItemStack.CODEC.fieldOf("addition").forGetter(BrewingRecipe::getAddition),
                            ItemStack.CODEC.fieldOf("output").forGetter(BrewingRecipe::getOutput)
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
        try {
            String group = buf.readString();
            ItemStack input = ItemStack.PACKET_CODEC.decode(buf);
            ItemStack addition = ItemStack.PACKET_CODEC.decode(buf);
            ItemStack output = ItemStack.PACKET_CODEC.decode(buf);

            if (input == null || addition == null || output == null) {
                throw new IllegalStateException("Recipe components cannot be null");
            }

            return new BrewingRecipe(group, input, addition, output);
        } catch (Exception e) {
            TestMod.LOGGER.error("Failed to read brewing recipe from network", e);
            throw e;
        }
    }

    private static void write(RegistryByteBuf buf, BrewingRecipe recipe) {
        try {
            buf.writeString(recipe.getGroup());
            ItemStack.PACKET_CODEC.encode(buf, recipe.getInput());
            ItemStack.PACKET_CODEC.encode(buf, recipe.getAddition());
            ItemStack.PACKET_CODEC.encode(buf, recipe.getOutput());
        } catch (Exception e) {
            TestMod.LOGGER.error("Failed to write brewing recipe to network", e);
            throw e;
        }
    }
}