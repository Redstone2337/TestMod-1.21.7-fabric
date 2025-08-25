package net.redstone233.test.core.api.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.redstone233.test.TestMod;
import net.redstone233.test.core.api.PlayerDataProvider;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class CodecPlayerDataProvider implements PlayerDataProvider {
    private final PlayerEntity player;
    private final Path dataFilePath;
    private PlayerData cachedData;

    // 用于JSON格式化的Gson实例
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    // 默认经验倍率
    private static final double DEFAULT_EXP_MULTIPLIER = 1.5;

    // 定义数据记录的Codec
    private static final Codec<PlayerData> DATA_CODEC = RecordCodecBuilder.create(dataInstance ->
            dataInstance.group(
                    Codec.INT.fieldOf("level").forGetter(PlayerData::getLevel),
                    Codec.INT.fieldOf("experience").forGetter(PlayerData::getExperience),
                    Codec.BOOL.fieldOf("vip").forGetter(PlayerData::isVip),
                    Codec.BOOL.fieldOf("svip").forGetter(PlayerData::isSvip),
                    Codec.DOUBLE.optionalFieldOf("expMultiplier", DEFAULT_EXP_MULTIPLIER).forGetter(PlayerData::getExpMultiplier)
            ).apply(dataInstance, PlayerData::new)
    );

    public CodecPlayerDataProvider(PlayerEntity player) {
        this.player = player;

        // 确定数据文件路径 - 使用配置目录而不是游戏目录
        String playerUuid = player.getUuidAsString();
        this.dataFilePath = FabricLoader.getInstance().getConfigDir() // 获取配置目录
                .resolve(TestMod.MOD_ID) // 创建以mod id命名的子目录
                .resolve("player_data_" + playerUuid + ".json"); // 最终的文件名

        // 确保目录存在
        try {
            Files.createDirectories(dataFilePath.getParent());
        } catch (IOException e) {
            TestMod.LOGGER.error("Failed to create data directory", e);
        }

        // 加载或初始化数据
        loadOrInitializeData();
    }

    private void loadOrInitializeData() {
        if (Files.exists(dataFilePath)) {
            try {
                // 读取文件内容为字符串
                String content = Files.readString(dataFilePath);

                if (content == null || content.trim().isEmpty()) {
                    TestMod.LOGGER.warn("Player data file is empty, creating default data");
                    createDefaultData();
                    saveData();
                    return;
                }

                // 解析JSON字符串为JsonElement
                JsonElement jsonElement = JsonParser.parseString(content);

                // 使用Codec解析JSON
                DataResult<PlayerData> result = DATA_CODEC.parse(JsonOps.INSTANCE, jsonElement);

                result.resultOrPartial(error -> {
                    TestMod.LOGGER.error("Failed to parse player data: {}", error);
                }).ifPresent(data -> {
                    cachedData = data;
                    TestMod.LOGGER.info("Loaded player data from file: {}", dataFilePath);
                });

                // 如果解析失败，使用默认数据
                if (cachedData == null) {
                    TestMod.LOGGER.warn("Failed to parse player data, using default values");
                    createDefaultData();
                    saveData();
                }

            } catch (IOException e) {
                TestMod.LOGGER.error("Failed to load player data from {}", dataFilePath, e);
                createDefaultData();
                saveData();
            } catch (Exception e) {
                TestMod.LOGGER.error("Unexpected error while loading player data from {}", dataFilePath, e);
                createDefaultData();
                saveData();
            }
        } else {
            TestMod.LOGGER.info("Player data file does not exist, creating default data at {}", dataFilePath);
            createDefaultData();
            saveData();
        }
    }

    private void createDefaultData() {
        // 硬编码的默认值
        cachedData = new PlayerData(1, 0, false, false, DEFAULT_EXP_MULTIPLIER);
    }

    private void saveData() {
        try {
            // 使用Codec将数据编码为JSON
            DataResult<JsonElement> result = DATA_CODEC.encodeStart(JsonOps.INSTANCE, cachedData);

            result.resultOrPartial(error -> {
                TestMod.LOGGER.error("Failed to encode player data: {}", error);
            }).ifPresent(jsonElement -> {
                try {
                    // 将JsonElement转换为格式化的JSON字符串
                    String jsonContent = GSON.toJson(jsonElement);
                    Files.writeString(dataFilePath, jsonContent);
                    TestMod.LOGGER.info("Saved player data to file: {}", dataFilePath);
                } catch (IOException e) {
                    TestMod.LOGGER.error("Failed to save player data to {}", dataFilePath, e);
                }
            });
        } catch (Exception e) {
            TestMod.LOGGER.error("Unexpected error during data save", e);
        }
    }

    // 其余的方法保持不变...
    @Override
    public int getLevel() {
        return cachedData.getLevel();
    }

    @Override
    public int getExperience() {
        return cachedData.getExperience();
    }

    @Override
    public boolean isVip() {
        return cachedData.isVip();
    }

    @Override
    public boolean isSVip() {
        return cachedData.isSvip();
    }

    @Override
    public double getExpMultiplier() {
        return cachedData.getExpMultiplier();
    }

    @Override
    public int getBaseExpForCurrentLevel() {
        int level = getLevel();
        // 基础经验公式：100 * (level)^1.5
        return (int) (100 * Math.pow(level, 1.5));
    }

    @Override
    public int getBaseExpForNextLevel() {
        int nextLevel = getLevel() + 1;
        // 基础经验公式：100 * (level)^1.5
        return (int) (100 * Math.pow(nextLevel, 1.5));
    }

    @Override
    public int getExperienceForNextLevel() {
        int baseExp = getBaseExpForNextLevel();
        double multiplier = getExpMultiplier();
        return (int) (baseExp * multiplier);
    }

    @Override
    public float getExperienceProgress() {
        int current = getExperience();
        int nextLevelExp = getExperienceForNextLevel();
        return nextLevelExp > 0 ? MathHelper.clamp((float) current / nextLevelExp, 0.0f, 1.0f) : 0.0f;
    }

    // 数据修改方法
    public void setLevel(int level) {
        cachedData = new PlayerData(
                level,
                cachedData.getExperience(),
                cachedData.isVip(),
                cachedData.isSvip(),
                cachedData.getExpMultiplier()
        );
        saveData();
    }

    public void setExperience(int experience) {
        cachedData = new PlayerData(
                cachedData.getLevel(),
                experience,
                cachedData.isVip(),
                cachedData.isSvip(),
                cachedData.getExpMultiplier()
        );
        saveData();
    }

    public void setVip(boolean vip) {
        cachedData = new PlayerData(
                cachedData.getLevel(),
                cachedData.getExperience(),
                vip,
                cachedData.isSvip(),
                cachedData.getExpMultiplier()
        );
        saveData();
    }

    public void setSvip(boolean svip) {
        cachedData = new PlayerData(
                cachedData.getLevel(),
                cachedData.getExperience(),
                cachedData.isVip(),
                svip,
                cachedData.getExpMultiplier()
        );
        saveData();
    }

    public void setExpMultiplier(double multiplier) {
        cachedData = new PlayerData(
                cachedData.getLevel(),
                cachedData.getExperience(),
                cachedData.isVip(),
                cachedData.isSvip(),
                multiplier
        );
        saveData();
    }

    public void addExperience(int amount) {
        int newExp = cachedData.getExperience() + amount;
        int newLevel = cachedData.getLevel();

        // 检查是否升级
        while (newExp >= getExperienceForNextLevel()) {
            newExp -= getExperienceForNextLevel();
            newLevel++;

            // 更新缓存数据以便下一轮计算使用新等级
            cachedData = new PlayerData(
                    newLevel,
                    newExp,
                    cachedData.isVip(),
                    cachedData.isSvip(),
                    cachedData.getExpMultiplier()
            );
        }

        // 保存最终数据
        cachedData = new PlayerData(
                newLevel,
                newExp,
                cachedData.isVip(),
                cachedData.isSvip(),
                cachedData.getExpMultiplier()
        );
        saveData();
    }

    /**
     * 获取数据文件路径（用于调试或管理）
     */
    public String getDataFilePath() {
        return dataFilePath.toString();
    }

    /**
     * 强制保存数据（用于手动保存）
     */
    public void forceSave() {
        saveData();
    }

    /**
     * 重新加载数据（用于手动重新加载）
     */
    public void reloadData() {
        loadOrInitializeData();
    }

    public PlayerEntity getPlayer() {
        return player;
    }

    // 不可变数据记录
    public record PlayerData(
            int level,
            int experience,
            boolean vip,
            boolean svip,
            double expMultiplier
    ) {
        public int getLevel() { return level; }
        public int getExperience() { return experience; }
        public boolean isVip() { return vip; }
        public boolean isSvip() { return svip; }
        public double getExpMultiplier() { return expMultiplier; }
    }
}