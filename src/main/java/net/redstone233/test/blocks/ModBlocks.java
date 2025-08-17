package net.redstone233.test.blocks;

import net.minecraft.block.*;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.redstone233.test.TestMod;

import java.util.function.Function;

public class ModBlocks {

    public static final Block SILICON_BLOCK = register("silicon_block", AbstractBlock.Settings.copy(Blocks./*IRON_BLOCK*/AMETHYST_BLOCK));
    public static final Block RAW_SILICON_BLOCK = register("raw_silicon_block",
            AbstractBlock.Settings.copy(Blocks.RAW_IRON_BLOCK)
            .sounds(BlockSoundGroup.AMETHYST_BLOCK));
    public static final Block SILICON_ORE = register(
            "silicon_ore",
            settings -> new ExperienceDroppingBlock(ConstantIntProvider.create(0),settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.WHITE_GRAY)
                    .instrument(NoteBlockInstrument.BIT)
                    .requiresTool()
                    .strength(2.0f,3.5f)
    );
    public static final Block DEEPSLATE_SILICON_ORE = register(
            "deepslate_silicon_ore",
            settings -> new ExperienceDroppingBlock(ConstantIntProvider.create(0),settings),
            AbstractBlock.Settings.copy(Blocks.IRON_ORE)
                    .mapColor(MapColor.DEEPSLATE_GRAY)
                    .strength(3.0F,4.0f)
    );
    //建筑方块
    public static final Block SILICON_BLOCK_STAIRS = register(
            "silicon_block_stairs",
            settings -> new StairsBlock(SILICON_BLOCK.getDefaultState(), settings),
            AbstractBlock.Settings.copy(SILICON_BLOCK.getDefaultState().getBlock())
                    .mapColor(MapColor.WHITE)
                    .instrument(NoteBlockInstrument.BIT)
                    .requiresTool()
                    .strength(2.0F,3.5F)
                    .sounds(BlockSoundGroup.AMETHYST_BLOCK)
    );

    public static final Block SILICON_BLOCK_SLAB = register(
            "silicon_block_slab",
            SlabBlock::new,
            AbstractBlock.Settings.copy(SILICON_BLOCK.getDefaultState().getBlock())
                    .mapColor(MapColor.WHITE)
                    .instrument(NoteBlockInstrument.BIT)
                    .requiresTool()
                    .strength(2.0F,3.5F)
                    .sounds(BlockSoundGroup.AMETHYST_BLOCK)
    );

    public static final Block SILICON_BUTTON = register(
            "silicon_button",
            settings -> new ButtonBlock(BlockSetType.IRON,20, settings),
            AbstractBlock.Settings.copy(SILICON_BLOCK.getDefaultState().getBlock())
                    .mapColor(MapColor.WHITE)
                    .instrument(NoteBlockInstrument.BIT)
                    .requiresTool()
                    .strength(2.0F,3.5F)
                    .sounds(BlockSoundGroup.AMETHYST_BLOCK)
    );

    public static final Block SILICON_PRESSURE_PLATE = register(
            "silicon_pressure_plate",
            settings -> new PressurePlateBlock(BlockSetType.IRON, settings),
            AbstractBlock.Settings.copy(SILICON_BLOCK.getDefaultState().getBlock())
                    .mapColor(MapColor.WHITE)
                    .instrument(NoteBlockInstrument.BIT)
                    .requiresTool()
                    .strength(2.0F,3.5F)
                    .sounds(BlockSoundGroup.AMETHYST_BLOCK)
    );

    public static final Block SILICON_FENCE = register(
            "silicon_fence",
            FenceBlock::new,
            AbstractBlock.Settings.copy(SILICON_BLOCK.getDefaultState().getBlock())
                    .mapColor(MapColor.WHITE)
                    .instrument(NoteBlockInstrument.BIT)
                    .requiresTool()
                    .strength(2.0F,3.5F)
                    .sounds(BlockSoundGroup.AMETHYST_BLOCK)
    );

    public static final Block SILICON_FENCE_GATE = register(
            "silicon_fence_gate",
            settings -> new FenceGateBlock(WoodType.ACACIA,settings),
            AbstractBlock.Settings.copy(SILICON_BLOCK.getDefaultState().getBlock())
                    .mapColor(MapColor.WHITE)
                    .instrument(NoteBlockInstrument.BIT)
                    .requiresTool()
                    .strength(2.0F,3.5F)
                    .sounds(BlockSoundGroup.AMETHYST_BLOCK)
    );

    public static final Block SILICON_WALL = register(
            "silicon_wall",
            WallBlock::new,
            AbstractBlock.Settings.copy(SILICON_BLOCK.getDefaultState().getBlock())
                    .mapColor(MapColor.WHITE)
                    .instrument(NoteBlockInstrument.BIT)
                    .requiresTool()
                    .strength(2.0F,3.5F)
                    .sounds(BlockSoundGroup.AMETHYST_BLOCK)
    );

    public static final Block SILICON_DOOR = register(
            "silicon_door",
            settings -> new DoorBlock(BlockSetType.IRON, settings),
            AbstractBlock.Settings.copy(SILICON_BLOCK.getDefaultState().getBlock())
                    .mapColor(MapColor.WHITE)
                    .instrument(NoteBlockInstrument.BIT)
                    .requiresTool()
                    .strength(2.0F,3.5F)
                    .sounds(BlockSoundGroup.AMETHYST_BLOCK)
    );

    public static final Block SILICON_TRAPDOOR = register(
            "silicon_trapdoor",
            settings -> new TrapdoorBlock(BlockSetType.IRON, settings),
            AbstractBlock.Settings.copy(SILICON_BLOCK.getDefaultState().getBlock())
                    .mapColor(MapColor.WHITE)
                    .instrument(NoteBlockInstrument.BIT)
                    .requiresTool()
                    .strength(2.0F,3.5F)
                    .sounds(BlockSoundGroup.AMETHYST_BLOCK)
    );

    private static Block register(String id, Function<AbstractBlock.Settings, Block> factory, AbstractBlock.Settings settings) {
        final RegistryKey<Block> registryKey = RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(TestMod.MOD_ID,id));
        final Block block = Blocks.register(registryKey, factory, settings);
        Items.register(block);
        return block;
    }

    private static Block register(String id, AbstractBlock.Settings settings) {
        final RegistryKey<Block> registryKey = RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(TestMod.MOD_ID,id));
        final Block block = Blocks.register(registryKey, Block::new, settings);
        Items.register(block);
        return block;
    }

    private static Block register(String id,
                                  Function<AbstractBlock.Settings, Block> factory,
                                  AbstractBlock.Settings settings,
                                  boolean registerItems) {
//        final Identifier identifier = Identifier.of("tutorial", path);
        final RegistryKey<Block> registryKey = RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(TestMod.MOD_ID,id));
        final Block block = Blocks.register(registryKey, factory, settings);
        if (registerItems) {
            Items.register(block);
        }
        return block;
    }

    private static Block register(String id, AbstractBlock.Settings settings,boolean registerItems) {
        final RegistryKey<Block> registryKey = RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(TestMod.MOD_ID,id));
        final Block block = Blocks.register(registryKey, Block::new, settings);
        if (registerItems) {
            Items.register(block);
        }
        return block;
    }

    public static void init() {
        TestMod.LOGGER.info("方块注册成功！");
    }
}
