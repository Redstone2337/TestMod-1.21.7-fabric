// LongPressAction.java
package net.redstone233.test.longpress;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

/**
 * 定义长按动作的接口
 */
public interface LongPressAction {
    /**
     * 当长按开始时调用
     * @param player 触发长按的玩家
     * @param stack 玩家当前手持的物品（可能为null）
     * @param startTime 长按开始时间戳（毫秒）
     */
    default void onPressStart(PlayerEntity player, ItemStack stack, long startTime) {}
    
    /**
     * 长按过程中每帧调用
     * @param player 玩家
     * @param stack 玩家当前手持的物品
     * @param pressedTime 已按下时间（毫秒）
     * @return 是否继续执行（返回false可提前结束）
     */
    default boolean onPressing(PlayerEntity player, ItemStack stack, long pressedTime) {
        return true;
    }
    
    /**
     * 当长按成功完成时调用
     * @param player 玩家
     * @param stack 玩家当前手持的物品
     */
    default void onLongPressComplete(PlayerEntity player, ItemStack stack) {}
    
    /**
     * 当长按中断时调用
     * @param player 玩家
     * @param stack 玩家当前手持的物品
     * @param pressedTime 已按下时间（毫秒）
     */
    default void onPressInterrupted(PlayerEntity player, ItemStack stack, long pressedTime) {}
    
    /**
     * 获取长按所需时间（毫秒）
     * @return 长按持续时间要求
     */
    long getRequiredPressTime();
    
    /**
     * 获取动作名称（用于标识和调试）
     */
    String getName();
    
    /**
     * 检查玩家是否可以使用此动作
     * @param player 玩家
     * @param stack 玩家当前手持的物品（可能为null）
     * @return 是否可用
     */
    default boolean canUse(PlayerEntity player, ItemStack stack) {
        return true;
    }
    
    /**
     * 获取冷却时间（毫秒）
     * @return 冷却时间
     */
    default long getCooldown() {
        return 0;
    }
}