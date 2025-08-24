package net.redstone233.test.core.until;

import net.minecraft.entity.player.PlayerEntity;
import net.redstone233.test.core.api.PlayerDataProvider;
import net.redstone233.test.core.api.impl.CodecPlayerDataProvider;

import java.util.WeakHashMap;
import java.util.Map;
import java.util.Optional;

public class PlayerDataFactory {
    private static final Map<PlayerEntity, PlayerDataProvider> providers = new WeakHashMap<>();

    /**
     * 获取玩家的数据提供者（接口类型）
     */
    public static PlayerDataProvider getProvider(PlayerEntity player) {
        return providers.computeIfAbsent(player, CodecPlayerDataProvider::new);
    }

    /**
     * 获取Codec实现的数据提供者（具体类型）
     */
    public static CodecPlayerDataProvider getCodecProvider(PlayerEntity player) {
        PlayerDataProvider provider = getProvider(player);
        if (provider instanceof CodecPlayerDataProvider) {
            return (CodecPlayerDataProvider) provider;
        }
        // 如果不是CodecPlayerDataProvider，创建一个新的并替换
        CodecPlayerDataProvider codecProvider = new CodecPlayerDataProvider(player);
        providers.put(player, codecProvider);
        return codecProvider;
    }

    /**
     * 检查玩家是否有数据提供者
     */
    public static boolean hasProvider(PlayerEntity player) {
        return providers.containsKey(player);
    }

    /**
     * 移除玩家的数据提供者（用于重新加载数据）
     */
    public static void removeProvider(PlayerEntity player) {
        providers.remove(player);
    }

    /**
     * 重新加载玩家的数据
     */
    public static void reloadProvider(PlayerEntity player) {
        removeProvider(player);
        getProvider(player); // 这会重新创建提供者并加载数据
    }

    /**
     * 获取所有活跃的数据提供者数量（用于调试）
     */
    public static int getActiveProvidersCount() {
        return providers.size();
    }

    /**
     * 获取玩家的经验倍率
     */
    public static double getExpMultiplier(PlayerEntity player) {
        return getProvider(player).getExpMultiplier();
    }

    /**
     * 设置玩家的经验倍率
     */
    public static void setExpMultiplier(PlayerEntity player, double multiplier) {
        getCodecProvider(player).setExpMultiplier(multiplier);
    }

    /**
     * 获取玩家的下一级所需经验
     */
    public static int getExpForNextLevel(PlayerEntity player) {
        return getProvider(player).getExperienceForNextLevel();
    }

    /**
     * 获取玩家的经验进度
     */
    public static float getExpProgress(PlayerEntity player) {
        return getProvider(player).getExperienceProgress();
    }

    /**
     * 获取玩家的经验进度百分比
     */
    public static float getExpProgressPercentage(PlayerEntity player) {
        return getProvider(player).getExpProgressPercentage();
    }

    /**
     * 获取玩家升级所需剩余经验
     */
    public static int getRemainingExp(PlayerEntity player) {
        return getProvider(player).getRemainingExpForNextLevel();
    }

    /**
     * 为玩家添加经验
     */
    public static void addExperience(PlayerEntity player, int amount) {
        getCodecProvider(player).addExperience(amount);
    }

    /**
     * 设置玩家的VIP状态
     */
    public static void setVipStatus(PlayerEntity player, boolean isVip) {
        getCodecProvider(player).setVip(isVip);
    }

    /**
     * 设置玩家的SVIP状态
     */
    public static void setSVipStatus(PlayerEntity player, boolean isSVip) {
        getCodecProvider(player).setSvip(isSVip);
    }

    /**
     * 获取玩家的VIP状态
     */
    public static boolean isVip(PlayerEntity player) {
        return getProvider(player).isVip();
    }

    /**
     * 获取玩家的SVIP状态
     */
    public static boolean isSVip(PlayerEntity player) {
        return getProvider(player).isSVip();
    }

    /**
     * 获取玩家的等级
     */
    public static int getLevel(PlayerEntity player) {
        return getProvider(player).getLevel();
    }

    /**
     * 设置玩家的等级
     */
    public static void setLevel(PlayerEntity player, int level) {
        getCodecProvider(player).setLevel(level);
    }

    /**
     * 获取玩家的经验值
     */
    public static int getExperience(PlayerEntity player) {
        return getProvider(player).getExperience();
    }

    /**
     * 设置玩家的经验值
     */
    public static void setExperience(PlayerEntity player, int experience) {
        getCodecProvider(player).setExperience(experience);
    }

    /**
     * 获取玩家数据文件的路径（用于调试或管理）
     */
    public static Optional<String> getDataFilePath(PlayerEntity player) {
        PlayerDataProvider provider = getProvider(player);
        if (provider instanceof CodecPlayerDataProvider) {
            return Optional.of(((CodecPlayerDataProvider) provider).getDataFilePath());
        }
        return Optional.empty();
    }
}
