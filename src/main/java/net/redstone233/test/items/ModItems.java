package net.redstone233.test.items;

import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.redstone233.test.TestMod;
import net.redstone233.test.core.component.ModDataComponentTypes;
import net.redstone233.test.core.component.type.HerbalTeaComponent;
import net.redstone233.test.core.food.ModConsumableComponents;
import net.redstone233.test.core.food.ModFoodComponents;
import net.redstone233.test.core.until.ModToolMaterial;
import net.redstone233.test.items.custom.FreezeSwordItem;
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
            new Item.Settings().maxDamage(300000).rarity(Rarity.RARE)
                    /*.attributeModifiers(FreezeSwordItem.createAttributeModifiers())*/
    );
    public static final Item HE_QI_ZHENG = register(
            "herbal_tea",
            new Item.Settings()
                    .recipeRemainder(Items.GLASS_BOTTLE)
                    .food(ModFoodComponents.HE_QI_ZHENG, ModConsumableComponents.HE_QI_ZHENG)
                    .useRemainder(Items.GLASS_BOTTLE)
                    .maxCount(12)
                    .component(ModDataComponentTypes.HE_QI_ZHENG, HerbalTeaComponent.DEFAULT)
    );




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
