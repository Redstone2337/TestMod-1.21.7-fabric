package net.redstone233.test.longpress;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class LongPressManager {
    // 使用弱引用防止内存泄漏
    private static final Map<PlayerEntity, PressData> pressingPlayers = Collections.synchronizedMap(new WeakHashMap<>());

    // 冷却时间记录 <玩家ID, 冷却结束时间>
    private static final Map<Identifier, Long> cooldowns = new ConcurrentHashMap<>();

    // 注册的动作 <动作名称, 动作实例>
    private static final Map<String, LongPressAction> registeredActions = new HashMap<>();

    // 按键绑定 <按键名称, 动作名称>
    private static final Map<String, String> keyBindings = new HashMap<>();

    public static class PressData {
        public final LongPressAction action;
        public final long startTime;
        public final ItemStack stack;

        public PressData(LongPressAction action, long startTime, ItemStack stack) {
            this.action = action;
            this.startTime = startTime;
            this.stack = stack;
        }
    }

    public static void registerAction(String actionName, LongPressAction action) {
        registeredActions.put(actionName, action);
    }

    public static void bindKeyToAction(String keyName, String actionName) {
        keyBindings.put(keyName, actionName);
    }

    public static boolean startLongPress(PlayerEntity player, String actionName, ItemStack stack) {
        if (player == null) return false;

        LongPressAction action = registeredActions.get(actionName);
        if (action == null) return false;

        // 检查冷却
        if (isOnCooldown(player, action)) {
            return false;
        }

        // 检查是否可用
        if (!action.canUse(player, stack)) {
            return false;
        }

        PressData data = new PressData(action, System.currentTimeMillis(), stack);
        pressingPlayers.put(player, data);
        action.onPressStart(player, stack, System.currentTimeMillis());
        return true;
    }

    public static void interruptLongPress(PlayerEntity player) {
        PressData data = pressingPlayers.remove(player);
        if (data != null) {
            long pressedTime = System.currentTimeMillis() - data.startTime;
            data.action.onPressInterrupted(player, data.stack, pressedTime);
        }
    }

    public static PressData getPressData(PlayerEntity player) {
        return pressingPlayers.get(player);
    }

    public static void update() {
        Iterator<Map.Entry<PlayerEntity, PressData>> iterator = pressingPlayers.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<PlayerEntity, PressData> entry = iterator.next();
            PlayerEntity player = entry.getKey();
            PressData data = entry.getValue();

            if (player.isRemoved() || !player.isAlive()) {
                iterator.remove();
                continue;
            }

            long pressedTime = System.currentTimeMillis() - data.startTime;

            // 调用持续按压处理
            if (!data.action.onPressing(player, data.stack, pressedTime)) {
                iterator.remove();
                continue;
            }

            // 检查是否完成长按
            if (pressedTime >= data.action.getRequiredPressTime()) {
                data.action.onLongPressComplete(player, data.stack);
                iterator.remove();

                // 设置冷却时间
                long cooldown = data.action.getCooldown();
                if (cooldown > 0) {
                    setCooldown(player, data.action, cooldown);
                }
            }
        }

        // 更新冷却时间
        updateCooldowns();
    }

    public static void handleKeyInput(PlayerEntity player, String keyName, boolean isPressed, ItemStack stack) {
        String actionName = keyBindings.get(keyName);
        if (actionName == null) return;

        PressData data = getPressData(player);
        boolean isPressing = data != null && data.action.getName().equals(actionName);

        if (isPressed && !isPressing) {
            startLongPress(player, actionName, stack);
        } else if (!isPressed && isPressing) {
            interruptLongPress(player);
        }
    }

    public static boolean isOnCooldown(PlayerEntity player, LongPressAction action) {
        Identifier id = getCooldownId(player, action);
        Long endTime = cooldowns.get(id);
        return endTime != null && endTime > System.currentTimeMillis();
    }

    public static long getRemainingCooldown(PlayerEntity player, LongPressAction action) {
        Identifier id = getCooldownId(player, action);
        Long endTime = cooldowns.get(id);
        if (endTime == null) return 0;
        return Math.max(0, endTime - System.currentTimeMillis());
    }

    public static void setCooldown(PlayerEntity player, LongPressAction action, long duration) {
        Identifier id = getCooldownId(player, action);
        cooldowns.put(id, System.currentTimeMillis() + duration);
    }

    private static Identifier getCooldownId(PlayerEntity player, LongPressAction action) {
        return Identifier.of(player.getUuidAsString() + ":" + action.getName());
    }

    private static void updateCooldowns() {
        long currentTime = System.currentTimeMillis();
        cooldowns.values().removeIf(endTime -> endTime <= currentTime);
    }

    public static LongPressAction getAction(String actionName) {
        return registeredActions.get(actionName);
    }
}
