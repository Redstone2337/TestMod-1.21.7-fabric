package net.redstone233.test.core.until;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class ModKeys {
    // 默认绑定为 I 键，分类为 "Freeze Sword"
    public static KeyBinding CHARGE_KEY = new KeyBinding(
            "key.freezesword.charge",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_I,
            "category.freezesword"
    );

    public static KeyBinding ANNOUNCEMENT_KEY = new KeyBinding(
            "key.mtc.announcement",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_H,
            "category.freezesword"
    );

    public static void register() {
        // 注册键位绑定
        KeyBindingHelper.registerKeyBinding(CHARGE_KEY);
        KeyBindingHelper.registerKeyBinding(ANNOUNCEMENT_KEY);
    }

    // 检查按键是否按下（客户端调用）
    public static boolean isChargeKeyPressed() {
        return CHARGE_KEY.isPressed();
    }

    public static boolean isAnnouncementKeyPressed() {
        return ANNOUNCEMENT_KEY.isPressed();
    }
}
