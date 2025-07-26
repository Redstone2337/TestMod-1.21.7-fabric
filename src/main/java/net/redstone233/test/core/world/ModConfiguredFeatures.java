package net.redstone233.test.core.world;

import net.minecraft.block.Blocks;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.structure.rule.BlockMatchRuleTest;
import net.minecraft.structure.rule.RuleTest;
import net.minecraft.structure.rule.TagMatchRuleTest;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.redstone233.test.TestMod;
import net.redstone233.test.blocks.ModBlocks;

import java.util.List;

public class ModConfiguredFeatures {

    // 注册矿石
    public static final RegistryKey<ConfiguredFeature<?, ?>> SILICON_ORE_KEY = of("silicon_ore");
    public static final RegistryKey<ConfiguredFeature<?, ?>> NETHER_SILICON_ORE_KEY = of("nether_silicon_ore");
    public static final RegistryKey<ConfiguredFeature<?, ?>> END_SILICON_ORE_KEY = of("end_silicon_ore");
    // bootstrap方法，用于数据生成
    public static void bootstrap(Registerable<ConfiguredFeature<?,?>> featureRegisterable) {
        // 为三个维度的矿石添加可置换方块
        RuleTest stoneReplace = new TagMatchRuleTest(BlockTags.STONE_ORE_REPLACEABLES);
        RuleTest deepslateReplace = new TagMatchRuleTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);
        RuleTest netherReplace = new TagMatchRuleTest(BlockTags.BASE_STONE_NETHER);
        RuleTest endReplace = new BlockMatchRuleTest(Blocks.END_STONE);

        List<OreFeatureConfig.Target> overWorldIceEtherOres =
                List.of(OreFeatureConfig.createTarget(stoneReplace, ModBlocks.SILICON_ORE.getDefaultState()),
                        OreFeatureConfig.createTarget(deepslateReplace, ModBlocks.SILICON_ORE.getDefaultState()));
        List<OreFeatureConfig.Target> netherIceEtherOres =
                List.of(OreFeatureConfig.createTarget(netherReplace, ModBlocks.SILICON_ORE.getDefaultState()));
        List<OreFeatureConfig.Target> endIceEtherOres =
                List.of(OreFeatureConfig.createTarget(endReplace, ModBlocks.SILICON_ORE.getDefaultState()));

        register(featureRegisterable, SILICON_ORE_KEY, Feature.ORE, new OreFeatureConfig(overWorldIceEtherOres, 8));
        register(featureRegisterable, NETHER_SILICON_ORE_KEY, Feature.ORE, new OreFeatureConfig(netherIceEtherOres, 8));
        register(featureRegisterable, END_SILICON_ORE_KEY, Feature.ORE, new OreFeatureConfig(endIceEtherOres, 8));
    }



        // 注册方法，记得改id
    public static RegistryKey<ConfiguredFeature<?, ?>> of(String id) {
        return RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, Identifier.of(TestMod.MOD_ID, id));
    }
    // 注册方法
    public static <FC extends FeatureConfig, F extends Feature<FC>> void register(Registerable<ConfiguredFeature<?, ?>> registerable, RegistryKey<ConfiguredFeature<?, ?>> key, F feature, FC config) {
        registerable.register(key, new ConfiguredFeature<FC, F>(feature, config));
    }
}
