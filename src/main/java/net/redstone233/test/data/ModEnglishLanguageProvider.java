package net.redstone233.test.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.registry.RegistryWrapper;
import net.redstone233.test.blocks.ModBlocks;
import net.redstone233.test.items.ModItems;

import java.util.concurrent.CompletableFuture;

public class ModEnglishLanguageProvider extends FabricLanguageProvider {
    public ModEnglishLanguageProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generateTranslations(RegistryWrapper.WrapperLookup wrapperLookup, TranslationBuilder translationBuilder) {
        translationBuilder.add(ModItems.RAW_SILICON, "Raw Silicon");
        translationBuilder.add(ModItems.SILICON, "Silicon");
        translationBuilder.add(ModItems.SILICON_INGOT, "Silicon Ingot");

        translationBuilder.add(ModBlocks.SILICON_BLOCK, "Block of Silicon");
        translationBuilder.add(ModBlocks.RAW_SILICON_BLOCK, "Block of Raw Silicon");
        translationBuilder.add(ModBlocks.SILICON_ORE, "Silicon Ore");
        translationBuilder.add(ModBlocks.DEEPSLATE_SILICON_ORE, "Deepslate Silicon Ore");

        translationBuilder.add(ModBlocks.SILICON_BLOCK_STAIRS, "Silicon Block Stairs");
        translationBuilder.add(ModBlocks.SILICON_BLOCK_SLAB, "Silicon Block Slab");
        translationBuilder.add(ModBlocks.SILICON_BUTTON, "Silicon Block Button");
        translationBuilder.add(ModBlocks.SILICON_DOOR, "Silicon Block Door");
        translationBuilder.add(ModBlocks.SILICON_FENCE, "Silicon Block Fence");
        translationBuilder.add(ModBlocks.SILICON_FENCE_GATE, "Silicon Block Fence Gate");
        translationBuilder.add(ModBlocks.SILICON_PRESSURE_PLATE, "Silicon Block Pressure Plate");
        translationBuilder.add(ModBlocks.SILICON_TRAPDOOR, "Silicon Block Trapdoor");
        translationBuilder.add(ModBlocks.SILICON_WALL, "Silicon Block Wall");

        translationBuilder.add("itemGroup.mtc.silicon_items", "Silicon Mod|Items");
        translationBuilder.add("itemGroup.mtc.silicon_build", "Silicon|Build Blocks");
    }
}
