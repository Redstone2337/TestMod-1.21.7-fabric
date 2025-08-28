package net.redstone233.test.data;

import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.data.BlockStateModelGenerator;
import net.minecraft.client.data.ItemModelGenerator;
import net.minecraft.client.data.Models;
import net.minecraft.data.family.BlockFamily;
import net.redstone233.test.blocks.ModBlockFamilies;
import net.redstone233.test.blocks.ModBlocks;
import net.redstone233.test.items.ModItems;

public class ModModelsProvider extends FabricModelProvider {
    public ModModelsProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        ModBlockFamilies.getFamilies()
                        .filter(BlockFamily::shouldGenerateModels).forEach(
                                blockFamily -> blockStateModelGenerator.registerCubeAllModelTexturePool(blockFamily.getBaseBlock())
                                        .family(blockFamily)
                );

//        blockStateModelGenerator.registerSimpleState(ModBlocks.SILICON_BLOCK);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.RAW_SILICON_BLOCK);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.SILICON_ORE);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.DEEPSLATE_SILICON_ORE);
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(ModItems.SILICON, Models.GENERATED);
        itemModelGenerator.register(ModItems.RAW_SILICON, Models.GENERATED);
        itemModelGenerator.register(ModItems.SILICON_INGOT ,Models.GENERATED);
        itemModelGenerator.register(ModItems.FREEZE_SWORD, Models.HANDHELD);
        itemModelGenerator.register(ModItems.HE_QI_ZHENG, Models.GENERATED);
        itemModelGenerator.register(ModItems.DELICIOUS_BLACK_GARLIC,Models.GENERATED);
        itemModelGenerator.register(ModItems.INFO_ITEM, Models.GENERATED);
        itemModelGenerator.register(ModItems.BLUE_CHEESE, Models.GENERATED);
        itemModelGenerator.registerBow(ModItems.FABRIC_BOW);
        itemModelGenerator.register(ModItems.FORGE_MACE, Models.GENERATED);
        itemModelGenerator.register(ModItems.NEOFORGE_ITEM, Models.GENERATED);
        itemModelGenerator.register(ModItems.QUILT_ITEM, Models.GENERATED);
    }
}
