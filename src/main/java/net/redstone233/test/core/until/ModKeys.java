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

    public static KeyBinding DEBUG_MODE_KEY = new KeyBinding(
            "key.mtc.debug",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_F12,
            "category.freezesword"
    );

    public static KeyBinding RELOAD_KEY = new KeyBinding(
            "key.mtc.reload",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_RIGHT_CONTROL,
            "category.freezesword"
    );

    public static KeyBinding PLAYER_INFO_KEY = new KeyBinding(
            "key.mtc.open_player_info",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_P,
            "category.freezesword"
    );

    public static void register() {
        // 注册键位绑定
        KeyBindingHelper.registerKeyBinding(CHARGE_KEY);
        KeyBindingHelper.registerKeyBinding(ANNOUNCEMENT_KEY);
        KeyBindingHelper.registerKeyBinding(DEBUG_MODE_KEY);
        KeyBindingHelper.registerKeyBinding(RELOAD_KEY);
        KeyBindingHelper.registerKeyBinding(PLAYER_INFO_KEY);
    }

    // 检查按键是否按下（客户端调用）
    public static boolean isChargeKeyPressed() {
        return CHARGE_KEY.isPressed();
    }

    public static boolean isAnnouncementKeyPressed() {
        return ANNOUNCEMENT_KEY.isPressed();
    }

    public static boolean isDebugModPressed() {
        return DEBUG_MODE_KEY.wasPressed();
    }

    public static boolean isReloadPressed() {
        return RELOAD_KEY.wasPressed();
    }

    public static boolean isPlayerInfoPressed() {
        return PLAYER_INFO_KEY.wasPressed();
    }
}
