package net.redstone233.test.core.food;

import net.minecraft.component.type.FoodComponent;
import net.redstone233.test.TestMod;

public class ModFoodComponents {

    public static final FoodComponent HE_QI_ZHENG = new FoodComponent.Builder().nutrition(6).saturationModifier(0.6F).alwaysEdible().build();
    public static final FoodComponent DELICIOUS_BLACK_GARLIC = new FoodComponent.Builder().alwaysEdible().nutrition(4).saturationModifier(0.4f).build();

    // 蓝纹奶酪，食用后根据玩家UUID是否包含数字7给予不同效果
    public static final FoodComponent BLUE_CHEESE = new FoodComponent.Builder().alwaysEdible().nutrition(2).saturationModifier(0.3f).build();


    public static void init() {
        TestMod.LOGGER.info("食品注册成功！");
    }

}
