package net.redstone233.test.core.api;

/**
 * 提供玩家相关数据的接口，包括等级、经验、特权状态及经验计算等功能。
 * 实现此接口的类应提供获取玩家基础信息和经验相关数据的具体实现。
 *
 * @author Redstone233
 * @version 1.0.0
 */
public interface PlayerDataProvider {

    /**
     * 获取玩家当前等级。
     *
     * @return 玩家当前的等级，非负整数
     */
    int getLevel();

    /**
     * 获取玩家当前经验值。
     *
     * @return 玩家当前的经验值，非负整数
     */
    int getExperience();

    /**
     * 检查玩家是否为VIP。
     *
     * @return 如果是VIP返回 {@code true}，否则返回 {@code false}
     */
    boolean isVip();

    /**
     * 检查玩家是否为超级VIP（SVip）。
     *
     * @return 如果是SVip返回 {@code true}，否则返回 {@code false}
     */
    boolean isSVip();

    /**
     * 获取升至下一级所需的总经验值（考虑经验倍率）。
     *
     * @return 升级到下一级所需的总经验值，非负整数
     */
    int getExperienceForNextLevel();

    /**
     * 获取当前等级的经验进度（0.0 到 1.0 之间的浮点数）。
     *
     * @return 当前等级的经验进度百分比（小数形式）
     */
    float getExperienceProgress();

    /**
     * 获取玩家当前的经验倍率。
     *
     * @return 经验倍率，通常大于等于 1.0
     */
    double getExpMultiplier();

    /**
     * 获取当前等级的基础经验需求（不考虑倍率）。
     *
     * @return 当前等级的基础经验值，非负整数
     */
    int getBaseExpForCurrentLevel();

    /**
     * 获取下一等级的基础经验需求（不考虑倍率）。
     *
     * @return 下一等级的基础经验值，非负整数
     */
    int getBaseExpForNextLevel();

    /**
     * 计算考虑倍率后升级所需的总经验值。
     * 默认实现基于 {@link #getBaseExpForNextLevel()} 和 {@link #getExpMultiplier()} 计算。
     *
     * @return 考虑倍率后的升级所需总经验值
     */
    default int getTotalExpForNextLevel() {
        int baseExp = getBaseExpForNextLevel();
        double multiplier = getExpMultiplier();
        return (int) (baseExp * multiplier);
    }

    /**
     * 计算升级所需的剩余经验值。
     * 默认实现基于 {@link #getTotalExpForNextLevel()} 和 {@link #getExperience()} 计算。
     *
     * @return 升级还需的经验值，非负整数
     */
    default int getRemainingExpForNextLevel() {
        int totalExpNeeded = getTotalExpForNextLevel();
        int currentExp = getExperience();
        return Math.max(0, totalExpNeeded - currentExp);
    }

    /**
     * 获取经验进度百分比（0.0 到 100.0 之间的浮点数）。
     * 默认实现基于当前经验值和升级所需总经验计算。
     *
     * @return 经验进度百分比（0.0 - 100.0）
     */
    default float getExpProgressPercentage() {
        int totalExpNeeded = getTotalExpForNextLevel();
        if (totalExpNeeded <= 0) return 0.0f;
        return (float) getExperience() / totalExpNeeded * 100.0f;
    }
}