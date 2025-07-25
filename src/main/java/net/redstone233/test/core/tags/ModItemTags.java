package net.redstone233.test.core.tags;

import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.redstone233.test.TestMod;

public class ModItemTags {

    public static final TagKey<Item> MOD_TAGS = of("mod_tags");
    public static final TagKey<Item> SILICON_TOOL_MATERIALS = of("silicon_tool_materials");

    private static TagKey<Item> of(String id) {
        return TagKey.of(RegistryKeys.ITEM, Identifier.of(TestMod.MOD_ID,id));
    }

    public static void init() {
        TestMod.LOGGER.info("物品标签注册成功！");
    }
}
