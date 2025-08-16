package net.redstone233.test.core.tags;

import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.redstone233.test.TestMod;

public class ModBlockTags {

    public static final TagKey<Block> BUILDING_BLICKS = of("building_blocks");
    public static final TagKey<Block> INCORRECT_FOR_SILICON_TOOL = of("incorrect_for_silicon_tool");
    public static final TagKey<Block> FEATURE_BLOCKS = of("feature_blocks");


    private static TagKey<Block> of(String id) {
        return TagKey.of(RegistryKeys.BLOCK, Identifier.of(TestMod.MOD_ID,id));
    }

    public static void init() {
        TestMod.LOGGER.info("方块标签注册成功！");
    }
}
