package net.redstone233.test.core.loot;

import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.util.Identifier;
import net.redstone233.test.items.ModItems;

public class FreezeSwordLoot {
    private static final Identifier JUNGLE_TEMPLATE =
            Identifier.ofVanilla("chests/jungle_template");

    private static final Identifier DESERT_PYRAMID =
            Identifier.ofVanilla("chests/desert_pyramid");

    private static final Identifier END_CITY =
            Identifier.ofVanilla("chests/end_city_treasure");

    private static final Identifier BASTION_REMNANT =
            Identifier.ofVanilla("chests/bastion_treasure");

    private static final Identifier MANSION =
            Identifier.ofVanilla("chests/woodland_mansion");

    private static final Identifier NETHER_BRIDGE =
            Identifier.ofVanilla("chests/nether_bridge");

    private static final Identifier WEAPONSMITH_VILLAGE =
            Identifier.ofVanilla("chests/village/village_weaponsmith");

    private static final Identifier ARMORER_VILLAGE =
            Identifier.ofVanilla("chests/village/village_armorer");

    private static final Identifier TOOLSMITH_VILLAGE =
            Identifier.ofVanilla("chests/village/village_toolsmith");

    private static final Identifier FISHER_VILLAGE =
            Identifier.ofVanilla("chests/village/village_fisher");

    private static final Identifier FLETCHER_VILLAGE =
            Identifier.ofVanilla("chests/village/village_fletcher");

        public static void init() {

            LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
                if (JUNGLE_TEMPLATE.equals(key.getValue())) {
                    LootPool.Builder pool = LootPool.builder()
                           .bonusRolls(UniformLootNumberProvider.create(1, 3))
                            .with(ItemEntry.builder(ModItems.FREEZE_SWORD)
                                    .weight(3)
                                    .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1,2)))
                            );
                    tableBuilder.pool(pool);
                }
            });

            LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
                if (DESERT_PYRAMID.equals(key.getValue())) {
                    LootPool.Builder pool = LootPool.builder()
                            .bonusRolls(UniformLootNumberProvider.create(1, 3))
                            .with(ItemEntry.builder(ModItems.FREEZE_SWORD)
                                    .weight(3)
                                    .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1,2)))
                            );
                    tableBuilder.pool(pool);
                }
            });

            LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
                if (END_CITY.equals(key.getValue())) {
                    LootPool.Builder pool = LootPool.builder()
                            .bonusRolls(UniformLootNumberProvider.create(1, 3))
                            .with(ItemEntry.builder(ModItems.FREEZE_SWORD)
                                    .weight(3)
                                    .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1,2)))
                            );
                    tableBuilder.pool(pool);
                }
            });

            LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
                if (WEAPONSMITH_VILLAGE.equals(key.getValue())) {
                    LootPool.Builder pool = LootPool.builder()
                            .bonusRolls(UniformLootNumberProvider.create(1, 3))
                            .with(ItemEntry.builder(ModItems.SILICON_INGOT)
                                    .weight(3)
                                    .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(3,5)))
                            );
                    tableBuilder.pool(pool);
                }
            });

            LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
                if (BASTION_REMNANT.equals(key.getValue())) {
                    LootPool.Builder pool = LootPool.builder()
                            .bonusRolls(UniformLootNumberProvider.create(1, 3))
                            .with(ItemEntry.builder(ModItems.FREEZE_SWORD)
                                    .weight(3)
                                    .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1,2)))
                            )
                            .with(ItemEntry.builder(ModItems.SILICON_INGOT)
                                    .weight(4)
                                    .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(7,9)))
                            )
                            .with(ItemEntry.builder(ModItems.SILICON)
                                    .weight(5)
                                    .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(7,9)))
                            );
                    tableBuilder.pool(pool);
                }
            });

            LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
                if (MANSION.equals(key.getValue())) {
                    LootPool.Builder pool = LootPool.builder()
                            .bonusRolls(UniformLootNumberProvider.create(1, 3))
                            .with(ItemEntry.builder(ModItems.FREEZE_SWORD)
                                    .weight(3)
                                    .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1,2)))
                            );
                    tableBuilder.pool(pool);
                }
            });

            LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
                if (NETHER_BRIDGE.equals(key.getValue())) {
                    LootPool.Builder pool = LootPool.builder()
                            .bonusRolls(UniformLootNumberProvider.create(1, 3))
                            .with(ItemEntry.builder(ModItems.FREEZE_SWORD)
                                    .weight(3)
                                    .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1,2)))
                            );
                    tableBuilder.pool(pool);
                }
            });

            LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
                if (ARMORER_VILLAGE.equals(key.getValue())) {
                    LootPool.Builder pool = LootPool.builder()
                            .bonusRolls(UniformLootNumberProvider.create(1, 3))
                            .with(ItemEntry.builder(ModItems.SILICON_INGOT)
                                    .weight(3)
                                    .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(3,5)))
                            );
                    tableBuilder.pool(pool);
                }
            });

            LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
                if (TOOLSMITH_VILLAGE.equals(key.getValue())) {
                    LootPool.Builder pool = LootPool.builder()
                            .bonusRolls(UniformLootNumberProvider.create(1, 3))
                            .with(ItemEntry.builder(ModItems.SILICON_INGOT)
                                    .weight(3)
                                    .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(3,5)))
                            );
                    tableBuilder.pool(pool);
                }
            });

            LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
                if (FISHER_VILLAGE.equals(key.getValue())) {
                    LootPool.Builder pool = LootPool.builder()
                            .bonusRolls(UniformLootNumberProvider.create(1, 3))
                            .with(ItemEntry.builder(ModItems.SILICON_INGOT)
                                    .weight(3)
                                    .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(3,5)))
                            )
                            .with(ItemEntry.builder(ModItems.SILICON)
                                    .weight(3)
                                    .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(3,5)))
                            );
                    tableBuilder.pool(pool);
                }
            });

            LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
                if (FLETCHER_VILLAGE.equals(key.getValue())) {
                    LootPool.Builder pool = LootPool.builder()
                            .bonusRolls(UniformLootNumberProvider.create(1, 3))
                            .with(ItemEntry.builder(ModItems.SILICON_INGOT)
                                    .weight(3)
                                    .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(3,5)))
                            )
                            .with(ItemEntry.builder(ModItems.SILICON)
                                    .weight(3)
                                    .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(3,5)))
                            );
                    tableBuilder.pool(pool);
                }
            });
        }
}

