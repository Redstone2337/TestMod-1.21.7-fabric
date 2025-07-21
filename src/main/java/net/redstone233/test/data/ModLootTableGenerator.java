package net.redstone233.test.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.registry.RegistryWrapper;
import net.redstone233.test.blocks.ModBlocks;
import net.redstone233.test.items.ModItems;

import java.util.concurrent.CompletableFuture;

public class ModLootTableGenerator extends FabricBlockLootTableProvider {
    public ModLootTableGenerator(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generate() {
        addDrop(ModBlocks.SILICON_ORE, block -> oreDrops(block, ModItems.RAW_SILICON));
        addDrop(ModBlocks.DEEPSLATE_SILICON_ORE, block -> oreDrops(block,ModItems.RAW_SILICON));
        addDrop(ModBlocks.SILICON_BLOCK);
        addDrop(ModBlocks.RAW_SILICON_BLOCK);
        addDrop(ModBlocks.SILICON_TRAPDOOR);
        addDrop(ModBlocks.SILICON_DOOR, block -> doorDrops(ModBlocks.SILICON_DOOR));
        addDrop(ModBlocks.SILICON_BLOCK_STAIRS);
        addDrop(ModBlocks.SILICON_BLOCK_SLAB);
        addDrop(ModBlocks.SILICON_BUTTON);
        addDrop(ModBlocks.SILICON_FENCE);
        addDrop(ModBlocks.SILICON_FENCE_GATE);
        addDrop(ModBlocks.SILICON_WALL);
    }

//    public LootTable.Builder oreDrops(Block withSilkTouch, Item withoutSilkTouch) {
//        RegistryWrapper.Impl<Enchantment> impl = this.registries.getOrThrow(RegistryKeys.ENCHANTMENT);
//        return this.dropsWithSilkTouch(
//                withSilkTouch,
//                (LootPoolEntry.Builder<?>)this.applyExplosionDecay(
//                        withSilkTouch, ItemEntry.builder(withoutSilkTouch).apply(ApplyBonusLootFunction.oreDrops(impl.getOrThrow(Enchantments.FORTUNE)))
//                )
//        );
//    }
}
