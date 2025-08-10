package net.redstone233.test.core.loot;

import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.redstone233.test.items.ModItems;

public class FreezeSwordLoot {
        public static void init() {
            LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
                if (key.equals(LootTables.getAll())) {
                    LootPool.Builder pool = LootPool.builder()
                            .bonusRolls(UniformLootNumberProvider.create(0, 1))
                            .with(ItemEntry.builder(ModItems.FREEZE_SWORD)
                                    .weight(3)
                            ).getThisFunctionConsumingBuilder();
                    tableBuilder.pool(pool);
                } else if (key.equals(LootTables.VILLAGE_FISHER_CHEST)) {
                    LootPool.Builder pool = LootPool.builder()
                            .bonusRolls(UniformLootNumberProvider.create(1, 3))
                            .with(ItemEntry.builder(ModItems.HE_QI_ZHENG)
                                    .weight(3)
                                    .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(3,5)))
                            ).getThisFunctionConsumingBuilder();
                    tableBuilder.pool(pool);
                } else if (key.equals(LootTables.DESERT_PYRAMID_ARCHAEOLOGY)) {
                    LootPool.Builder pool = LootPool.builder()
                            .bonusRolls(UniformLootNumberProvider.create(1, 3))
                            .with(ItemEntry.builder(ModItems.SILICON_INGOT)
                                    .weight(3)
                                    .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(3,5)))
                            ).getThisFunctionConsumingBuilder();
                    tableBuilder.pool(pool);
                } else if (key.equals(LootTables.WOODLAND_MANSION_CHEST)) {
                    LootPool.Builder pool = LootPool.builder()
                            .bonusRolls(UniformLootNumberProvider.create(1, 3))
                            .with(ItemEntry.builder(ModItems.SILICON_INGOT)
                                    .weight(3)
                                    .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(3,5)))
                            ).getThisFunctionConsumingBuilder();
                    tableBuilder.pool(pool);
                }
            });
        }
}

