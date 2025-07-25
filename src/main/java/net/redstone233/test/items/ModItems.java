package net.redstone233.test.items;

import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.redstone233.test.TestMod;
import net.redstone233.test.core.until.ModToolMaterial;
import net.redstone233.test.items.custom.FreezeSwordItem;
import net.redstone233.test.core.commands.SetValueCountCommand;

import java.util.function.Function;

public class ModItems {

private static final int CUSTOM_MAX_COUNT = SetValueCountCommand.getCustomMaxSize();

    public static final Item SILICON = register("silicon", new Item.Settings().maxCount(CUSTOM_MAX_COUNT));
    public static final Item RAW_SILICON = register("raw_silicon",new Item.Settings().maxCount(CUSTOM_MAX_COUNT));
    public static final Item SILICON_INGOT = register("silicon_ingot", new Item.Settings().maxCount(CUSTOM_MAX_COUNT));
    public static final Item FREEZE_SWORD = register("freeze_sword",
            settings -> new FreezeSwordItem(ModToolMaterial.SILICON,10.5f, 2.5f,settings),
            new Item.Settings().maxDamage(300000)
                    .attributeModifiers(FreezeSwordItem.createAttributeModifiers())
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
