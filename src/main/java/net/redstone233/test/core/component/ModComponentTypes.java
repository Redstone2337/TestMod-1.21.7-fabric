package net.redstone233.test.core.component;

import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.redstone233.test.TestMod;

public class ModComponentTypes {

    public static final ComponentType<FreezingSwordComponent> FREEZING_SWORD_COMPONENT_COMPONENT_TYPE = Registry.register(Registries.DATA_COMPONENT_TYPE,
            Identifier.of(TestMod.MOD_ID, "freezing_sword"),
            FreezingSwordComponent.TYPE);

    public static void init() {
        TestMod.LOGGER.info("组件注册成功！");
    }
}
