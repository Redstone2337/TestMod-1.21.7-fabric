package net.redstone233.test.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.redstone233.test.core.until.ModKeys;
import net.redstone233.test.longpress.LongPressManager;

@Environment(EnvType.CLIENT)
public class ClientLongPressHandler {

    public static void init() {
        // 注册客户端tick事件
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player != null) {
                // 更新长按状态
                LongPressManager.update();

                // 检查按键释放状态
                checkKeyRelease();
            }
        });
    }

    private static void checkKeyRelease() {
        PlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) return;

        LongPressManager.PressData data = LongPressManager.getPressData(player);
        if (data != null) {
            // 检查按键是否释放
            if (!ModKeys.isChargeKeyPressed()) {
                LongPressManager.interruptLongPress(player);
            }
        }
    }

    public static void handleKeyInput(PlayerEntity player, String keyName, boolean isPressed, ItemStack stack) {
        LongPressManager.handleKeyInput(player, keyName, isPressed, stack);
    }
}