package net.redstone233.test.core.until;

import net.minecraft.item.ToolMaterial;
import net.redstone233.test.TestMod;
import net.redstone233.test.core.tags.ModBlockTags;
import net.redstone233.test.core.tags.ModItemTags;

public class ModToolMaterial {
    public static final ToolMaterial SILICON = new ToolMaterial(ModBlockTags.INCORRECT_FOR_SILICON_TOOL,4000,8.0F,10.0F,15, ModItemTags.SILICON_TOOL_MATERIALS);

    public static void register() {
        TestMod.LOGGER.info("自定义属性完成！");
    }
}
