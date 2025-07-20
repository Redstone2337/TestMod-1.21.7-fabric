package net.redstone233.test.items;

import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.redstone233.test.TestMod;

import java.util.function.Function;

public class ModItems {

    public static final Item SILICON = register("silicon", new Item.Settings().maxCount(64));
    public static final Item RAW_SILICON = register("raw_silicon",new Item.Settings().maxCount(64));
    public static final Item SILICON_INGOT = register("silicon_ingot", new Item.Settings().maxCount(99));




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
