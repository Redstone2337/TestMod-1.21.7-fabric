package net.redstone233.test.items;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.BowItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.redstone233.test.TestMod;
import net.redstone233.test.client.tooltip.FreezeSwordTooltipComponent;
import net.redstone233.test.core.component.ModDataComponentTypes;
import net.redstone233.test.core.component.type.*;
import net.redstone233.test.core.food.ModConsumableComponents;
import net.redstone233.test.core.food.ModFoodComponents;
import net.redstone233.test.core.until.Builder.RegisterItemBuilder;
import net.redstone233.test.core.until.ModToolMaterial;
import net.redstone233.test.items.custom.*;
import net.redstone233.test.core.commands.SetValueCountCommand;

import java.util.function.Function;

public class ModItems {

private static final int CUSTOM_MAX_COUNT = SetValueCountCommand.getCustomMaxSize();

public static final float ATTACK_DAMAGE = 10.0f;

    public static final Item SILICON = register("silicon", new Item.Settings().maxCount(CUSTOM_MAX_COUNT));
    public static final Item RAW_SILICON = register("raw_silicon",new Item.Settings().maxCount(CUSTOM_MAX_COUNT));
    public static final Item SILICON_INGOT = register("silicon_ingot", new Item.Settings().maxCount(CUSTOM_MAX_COUNT));
    public static final Item FREEZE_SWORD = register("freeze_sword",
            settings -> new FreezeSwordItem(ModToolMaterial.SILICON,ATTACK_DAMAGE, 2.5f ,settings),
            new Item.Settings()
                    .maxDamage(300000)
                    .rarity(Rarity.RARE)
    );
    public static final Item HE_QI_ZHENG = register(
            "herbal_tea",
            HerbalTeaItem::new,
            new Item.Settings()
                    .recipeRemainder(Items.GLASS_BOTTLE)
                    .food(ModFoodComponents.HE_QI_ZHENG, ModConsumableComponents.HE_QI_ZHENG)
                    .useRemainder(Items.GLASS_BOTTLE)
                    .maxCount(12)
                    .component(ModDataComponentTypes.HE_QI_ZHENG, HerbalTeaComponent.DEFAULT)
                    .component(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, true)
                    .attributeModifiers(HerbalTeaItem.createAttributeModifiers())
    );

    public static final Item DELICIOUS_BLACK_GARLIC = register(
      "black_garlic",
      BlackGarlicItem::new,
      new Item.Settings()
              .food(ModFoodComponents.DELICIOUS_BLACK_GARLIC, ModConsumableComponents.DELICIOUS_BLACK_GARLIC)
              .component(ModDataComponentTypes.DELICIOUS_BLACK_GARLIC, DeliciousBlackGarlicComponent.DEFAULT)
              .component(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE,true)
              .attributeModifiers(BlackGarlicItem.createAttributeModifiers())
              .maxCount(64)
    );

    public static final Item INFO_ITEM = register(
            "info_item",
            InfoItem::new,
            new Item.Settings()
                    .attributeModifiers(InfoItem.createAttributeModifiers())
                    .component(ModDataComponentTypes.INFO_ITEM,InfoItemComponent.DEFAULT)
                    .maxCount(64)
    );

    public static final Item BLUE_CHEESE = register(
            "blue_cheese",
            BlueCheeseItem::new,
            new Item.Settings()
                    .attributeModifiers(BlueCheeseItem.createAttributeModifiers())
                    .food(ModFoodComponents.BLUE_CHEESE)
                    .maxCount(16)
    );

    public static final Item FABRIC_BOW = RegisterItemBuilder.create("fabric")
            .factory(FabricBowItem::new)
            .settings(new Item.Settings()
                    .maxDamage(30000)
                    .enchantable(1)
                    .maxCount(1)
            )
            .register();

    public static final Item FORGE_MACE = RegisterItemBuilder.create("forge")
            .factory(ForgeItem::new)
            .settings(new Item.Settings()
                    .rarity(Rarity.EPIC)
                    .component(DataComponentTypes.TOOL, ForgeItem.createToolComponent())
                    .maxDamage(5000)
                    .enchantable(15)
                    .attributeModifiers(ForgeItem.createAttributeModifiers())
                    .repairable(SILICON_INGOT)
                    .maxCount(1)
            )
            .register();

    public static final Item NEOFORGE_ITEM = RegisterItemBuilder.create("neoforged")
            .factory(NeoForgeItem::new)
            .settings(new Item.Settings()
                    .component(ModDataComponentTypes.NEO_FORGE_ITEM, NeoForgeItemComponent.DEFAULT)
                    .maxDamage(10000)
                    .enchantable(20)
                    .attributeModifiers(NeoForgeItem.createAttributeModifiers())
                    .maxCount(1)
            )
            .register();

    public static final Item QUILT_ITEM = RegisterItemBuilder.create("quilt")
            .factory(QuiltItem::new)
            .settings(new Item.Settings()
                    .maxDamage(8000)
                    .enchantable(10)
                    .attributeModifiers(QuiltItem.createAttributeModifiers())
                    .maxCount(1)
            )
            .register();



    private static Item register(String id, Function<Item.Settings, Item> factory, Item.Settings settings) {
        final RegistryKey<Item> registryKey = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(TestMod.MOD_ID, id));
        return Items.register(registryKey, factory, settings);
    }

    private static Item register(String id, Item.Settings settings) {
        final RegistryKey<Item> registryKey = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(TestMod.MOD_ID, id));
        return Items.register(registryKey, Item::new, settings);
    }

    public static void init() {
        TestMod.LOGGER.info("物品注册成功！");
    }
}
