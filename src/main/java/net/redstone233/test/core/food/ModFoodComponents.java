package net.redstone233.test.core.food;

import net.minecraft.component.type.FoodComponent;
import net.redstone233.test.TestMod;

public class ModFoodComponents {

    public static final FoodComponent HE_QI_ZHENG = new FoodComponent.Builder().nutrition(6).saturationModifier(0.1F).alwaysEdible().build();

    public static void init() {
        TestMod.LOGGER.info("食品注册成功！");
    }

}
