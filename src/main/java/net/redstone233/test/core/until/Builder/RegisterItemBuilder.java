package net.redstone233.test.core.until.Builder;

import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.redstone233.test.TestMod;

import java.util.function.Function;

public class RegisterItemBuilder {
    private final String id;
    private Function<Item.Settings, Item> factory = Item::new;
    private Item.Settings settings;

    private RegisterItemBuilder(String id) {
        this.id = id;
    }

    public static RegisterItemBuilder create(String id) {
        return new RegisterItemBuilder(id);
    }

    public RegisterItemBuilder factory(Function<Item.Settings, Item> factory) {
        this.factory = factory;
        return this;
    }

    public RegisterItemBuilder settings(Item.Settings settings) {
        this.settings = settings;
        return this;
    }

    public Item register() {
        final RegistryKey<Item> registryKey = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(TestMod.MOD_ID, id));
        return net.minecraft.item.Items.register(registryKey, factory, settings);
    }
}
