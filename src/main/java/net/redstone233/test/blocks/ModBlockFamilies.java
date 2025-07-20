package net.redstone233.test.blocks;

import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.data.family.BlockFamily;
import net.minecraft.registry.Registries;
import net.redstone233.test.TestMod;

import java.util.Map;
import java.util.stream.Stream;

public class ModBlockFamilies {
    private static final Map<Block, BlockFamily> BASE_BLOCKS_TO_FAMILIES = Maps.<Block, BlockFamily>newHashMap();

    public static final BlockFamily SILICON_FAMILY = register(ModBlocks.SILICON_BLOCK)
            .stairs(ModBlocks.SILICON_BLOCK_STAIRS)
            .slab(ModBlocks.SILICON_BLOCK_SLAB)
            .button(ModBlocks.SILICON_BUTTON)
            .pressurePlate(ModBlocks.SILICON_PRESSURE_PLATE)
            .fence(ModBlocks.SILICON_FENCE)
            .fenceGate(ModBlocks.SILICON_FENCE_GATE)
            .wall(ModBlocks.SILICON_WALL)
            .door(ModBlocks.SILICON_DOOR)
            .trapdoor(ModBlocks.SILICON_TRAPDOOR)
            .unlockCriterionName("has_silicon_block")
            .build();


    public static BlockFamily.Builder register(Block baseBlock) {
        BlockFamily.Builder builder = new BlockFamily.Builder(baseBlock);
        BlockFamily blockFamily = (BlockFamily)BASE_BLOCKS_TO_FAMILIES.put(baseBlock, builder.build());
        if (blockFamily != null) {
            throw new IllegalStateException("Duplicate family definition for " + Registries.BLOCK.getId(baseBlock));
        } else {
            return builder;
        }
    }

    public static Stream<BlockFamily> getFamilies() {
        return BASE_BLOCKS_TO_FAMILIES.values().stream();
    }

    public static void init() {
        TestMod.LOGGER.info("注册建筑方块成功！");
    }
}
