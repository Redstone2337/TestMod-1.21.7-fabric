package net.redstone233.test.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;
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
        valueLookupBuilder(ModBlockTags.FEATURE_BLOCKS)
                .add(
                        ModBlocks.SILICON_ORE,
                        ModBlocks.DEEPSLATE_SILICON_ORE
                );
        valueLookupBuilder(ModBlockTags.INCORRECT_FOR_SILICON_TOOL);
        valueLookupBuilder(BlockTags.PICKAXE_MINEABLE)
                .addTag(ModBlockTags.BUILDING_BLICKS)
                .addTag(ModBlockTags.FEATURE_BLOCKS);
        valueLookupBuilder(BlockTags.AXE_MINEABLE)
                .add(ModBlocks.SILICON_FENCE_GATE);

// 台阶
valueLookupBuilder(BlockTags.SLABS)
			.add(ModBlocks.SILICON_BLOCK_SLAB);
// 楼梯
valueLookupBuilder(BlockTags.STAIRS)
			.add(ModBlocks.SILICON_BLOCK_STAIRS);
// 门
valueLookupBuilder(BlockTags.DOORS)
			.add(ModBlocks.SILICON_DOOR);
// 活板门
valueLookupBuilder(BlockTags.TRAPDOORS)
			.add(ModBlocks.SILICON_TRAPDOOR);
// 墙壁
valueLookupBuilder(BlockTags.WALLS)
			.add(ModBlocks.SILICON_WALL);
// 栅栏
valueLookupBuilder(BlockTags.FENCES)
			.add(ModBlocks.SILICON_FENCE);
// 栅栏门
valueLookupBuilder(BlockTags.FENCE_GATES)
			.add(ModBlocks.SILICON_FENCE_GATE);

valueLookupBuilder(BlockTags.NEEDS_IRON_TOOL)
        .add(
                ModBlocks.SILICON_ORE,
                ModBlocks.DEEPSLATE_SILICON_ORE
        );
    }
}
