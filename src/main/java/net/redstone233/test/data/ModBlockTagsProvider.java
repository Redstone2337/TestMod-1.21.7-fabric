package net.redstone233.test.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryWrapper;
import net.redstone233.test.blocks.ModBlocks;
import net.redstone233.test.core.tags.ModBlockTags;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagsProvider extends FabricTagProvider.BlockTagProvider {
    public ModBlockTagsProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        valueLookupBuilder(ModBlockTags.BUILDING_BLICKS)
                .add(
                        ModBlocks.SILICON_BLOCK,
                        ModBlocks.RAW_SILICON_BLOCK,
                        ModBlocks.SILICON_BLOCK_STAIRS,
                        ModBlocks.SILICON_BLOCK_SLAB,
                        ModBlocks.SILICON_FENCE,
                        ModBlocks.SILICON_FENCE_GATE,
                        ModBlocks.SILICON_WALL,
                        ModBlocks.SILICON_BUTTON,
                        ModBlocks.SILICON_DOOR,
                        ModBlocks.SILICON_TRAPDOOR
                );
    }
}
