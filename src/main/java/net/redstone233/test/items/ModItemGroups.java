package net.redstone233.test.items;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.redstone233.test.TestMod;
import net.redstone233.test.blocks.ModBlocks;

public class ModItemGroups {

    public static final RegistryKey<ItemGroup> SILICON_BUILDINGS = register("silicon_buildings");
    public static final RegistryKey<ItemGroup> SILICON_ITEMS = register("silicon_items");
    public static final RegistryKey<ItemGroup> SILICON_FUTURES = register("silicon_futures");

    private static RegistryKey<ItemGroup> register(String id) {
        return RegistryKey.of(RegistryKeys.ITEM_GROUP, Identifier.of(TestMod.MOD_ID,id));
    }

    public static void registerModItemGroups() {
        Registry.register(Registries.ITEM_GROUP,ModItemGroups.SILICON_ITEMS,
                ItemGroup.create(
                                ItemGroup.Row.TOP, 0)
                        .displayName(Text.translatable("itemGroup.mtc.silicon_items"))
                        .icon(() -> new ItemStack(ModItems.SILICON)).entries((displayContext, entries) -> {
                            entries.add(ModItems.RAW_SILICON);
                            entries.add(ModItems.SILICON);
                            entries.add(ModBlocks.SILICON_BLOCK);
                            entries.add(ModBlocks.SILICON_ORE);
                            entries.add(ModBlocks.DEEPSLATE_SILICON_ORE);
                            entries.add(ModItems.SILICON_INGOT);
                            entries.add(ModItems.FREEZE_SWORD);
                            entries.add(ModItems.HE_QI_ZHENG);
                            entries.add(ModItems.DELICIOUS_BLACK_GARLIC);
                            entries.add(ModItems.INFO_ITEM);
                            entries.add(ModItems.BLUE_CHEESE);
                            entries.add(ModItems.FABRIC_BOW);
                            entries.add(ModItems.NEOFORGE_ITEM);
                            entries.add(ModItems.FORGE_MACE);
                            entries.add(ModItems.QUILT_ITEM);
                        })
                        .build()
        );

        Registry.register(Registries.ITEM_GROUP,ModItemGroups.SILICON_FUTURES,
                ItemGroup.create(
                                ItemGroup.Row.TOP, 0)
                        .displayName(Text.translatable("itemGroup.mtc.silicon_futures"))
                        .icon(() -> new ItemStack(ModBlocks.SILICON_ORE)).entries((displayContext, entries) -> {
//                            entries.add(ModItems.RAW_SILICON);
//                            entries.add(ModItems.SILICON);
//                            entries.add(ModBlocks.SILICON_BLOCK);
                            entries.add(ModBlocks.SILICON_ORE);
                            entries.add(ModBlocks.DEEPSLATE_SILICON_ORE);
//                            entries.add(ModItems.SILICON_INGOT);
//                            entries.add(ModItems.FREEZE_SWORD);
                        })
                        .build()
        );



        Registry.register(Registries.ITEM_GROUP,ModItemGroups.SILICON_BUILDINGS,
                ItemGroup.create(
                                ItemGroup.Row.TOP, 0)
                        .displayName(Text.translatable("itemGroup.mtc.silicon_build"))
                        .icon(() -> new ItemStack(ModBlocks.SILICON_BLOCK)).entries((displayContext, entries) -> {
                            entries.add(ModBlocks.SILICON_BLOCK);
                            entries.add(ModBlocks.RAW_SILICON_BLOCK);
                            entries.add(ModBlocks.SILICON_BLOCK_STAIRS);
                            entries.add(ModBlocks.SILICON_BLOCK_SLAB);
                            entries.add(ModBlocks.SILICON_BUTTON);
                            entries.add(ModBlocks.SILICON_FENCE);
                            entries.add(ModBlocks.SILICON_FENCE_GATE);
                            entries.add(ModBlocks.SILICON_PRESSURE_PLATE);
                            entries.add(ModBlocks.SILICON_DOOR);
                            entries.add(ModBlocks.SILICON_TRAPDOOR);
                            entries.add(ModBlocks.SILICON_WALL);
                        })
                        .build()
        );
    }
}
