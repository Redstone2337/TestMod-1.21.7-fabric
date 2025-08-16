package net.redstone233.test.core.world;

import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.placementmodifier.HeightRangePlacementModifier;
import net.minecraft.world.gen.placementmodifier.PlacementModifier;
import net.redstone233.test.TestMod;

import java.util.List;

public class ModPlacedFeatures {

    // 注册矿石的放置的key（三个维度）
    public static final RegistryKey<PlacedFeature> SILICON_ORE_PLACED_KEY = of("silicon_ore_placed");
    public static final RegistryKey<PlacedFeature> NETHER_SILICON_ORE_PLACED_KEY = of("nether_silicon_ore_placed");
    public static final RegistryKey<PlacedFeature> END_SILICON_ORE_PLACED_KEY = of("end_silicon_ore_placed");



    public static void boostrap(Registerable<PlacedFeature> featureRegisterable) {
        RegistryEntryLookup<ConfiguredFeature<?, ?>> registryEntryLookup = featureRegisterable.getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE);
        // 为三个维度的矿石注册放置特征
        // modifiersWithCount的参数分别是每个区块生成的数量、高度范围
        // HeightRangePlacementModifier.uniform参数是高度范围，另有trapezoid方法（梯形生成法）
        register(featureRegisterable, SILICON_ORE_PLACED_KEY, registryEntryLookup.getOrThrow(ModConfiguredFeatures.SILICON_ORE_KEY),
                ModOrePlacements.modifiersWithCount(12,
                        HeightRangePlacementModifier.uniform(YOffset.fixed(-60), YOffset.fixed(30))));
        register(featureRegisterable, NETHER_SILICON_ORE_PLACED_KEY, registryEntryLookup.getOrThrow(ModConfiguredFeatures.NETHER_SILICON_ORE_KEY),
                ModOrePlacements.modifiersWithCount(12,
                        HeightRangePlacementModifier.uniform(YOffset.fixed(-80), YOffset.fixed(80))));
        register(featureRegisterable, END_SILICON_ORE_PLACED_KEY, registryEntryLookup.getOrThrow(ModConfiguredFeatures.END_SILICON_ORE_KEY),
                ModOrePlacements.modifiersWithCount(12,
                        HeightRangePlacementModifier.uniform(YOffset.fixed(-80), YOffset.fixed(80))));
    }


    // 注册方法
    public static RegistryKey<PlacedFeature> of(String id) {
        return RegistryKey.of(RegistryKeys.PLACED_FEATURE, Identifier.of(TestMod.MOD_ID, id));
    }
    public static void register(Registerable<PlacedFeature> featureRegisterable, RegistryKey<PlacedFeature> key, RegistryEntry<ConfiguredFeature<?, ?>> feature, PlacementModifier... modifiers) {
        register(featureRegisterable, key, feature, List.of(modifiers));
    }
    public static void register(Registerable<PlacedFeature> featureRegisterable, RegistryKey<PlacedFeature> key, RegistryEntry<ConfiguredFeature<?, ?>> feature, List<PlacementModifier> modifiers) {
        featureRegisterable.register(key, new PlacedFeature(feature, List.copyOf(modifiers)));
    }
}
