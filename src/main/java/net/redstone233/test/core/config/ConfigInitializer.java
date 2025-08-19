package net.redstone233.test.core.config;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.redstone233.test.TestModClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigInitializer {
    private static final Logger LOGGER = LoggerFactory.getLogger("ConfigInitializer");
    private static ConfigHolder<ModConfig> configHolder;
    private static boolean initialized = false;
    private static long initializationTime = 0;

    /**
     * 初始化配置系统
     */
    public static synchronized void initialize() {
        if (initialized) {
            return;
        }

        LOGGER.info("开始初始化配置系统...");
        long startTime = System.currentTimeMillis();

        try {
            // 注册配置
            configHolder = AutoConfig.register(ModConfig.class, Toml4jConfigSerializer::new);

            // 获取配置实例
            TestModClient.CONFIG = configHolder.getConfig();

            // 确保新配置选项有默认值
            ensureNewConfigDefaults();

            initialized = true;
            initializationTime = System.currentTimeMillis() - startTime;

            LOGGER.info("配置初始化成功，耗时 {}ms", initializationTime);

        } catch (Exception e) {
            LOGGER.error("配置初始化失败，使用默认配置", e);
            TestModClient.CONFIG = new ModConfig();
            ensureNewConfigDefaults(); // 确保默认配置也有新选项
            initialized = true; // 仍然标记为已初始化，但使用默认配置
            initializationTime = System.currentTimeMillis() - startTime;
        }
    }

    /**
     * 确保新配置选项有默认值
     */
    private static void ensureNewConfigDefaults() {
        if (TestModClient.CONFIG.iconPath == null || TestModClient.CONFIG.iconPath.isEmpty()) {
            TestModClient.CONFIG.iconPath = "testmod:textures/gui/announcement_icon.png";
        }

        // 确保图标尺寸在有效范围内
        if (TestModClient.CONFIG.iconWidth < 16) TestModClient.CONFIG.iconWidth = 16;
        if (TestModClient.CONFIG.iconWidth > 1024) TestModClient.CONFIG.iconWidth = 1024;
        if (TestModClient.CONFIG.iconHeight < 16) TestModClient.CONFIG.iconHeight = 16;
        if (TestModClient.CONFIG.iconHeight > 1024) TestModClient.CONFIG.iconHeight = 1024;
        if (TestModClient.CONFIG.iconTextSpacing < 0) TestModClient.CONFIG.iconTextSpacing = 10;
    }

    /**
     * 获取配置持有者
     */
    public static ConfigHolder<ModConfig> getConfigHolder() {
        ensureInitialized();
        return configHolder;
    }

    /**
     * 刷新配置
     */
    public static void refreshConfig() {
        ensureInitialized();
        if (configHolder != null) {
            TestModClient.CONFIG = configHolder.getConfig();
            ensureNewConfigDefaults(); // 确保刷新后新配置选项也有默认值
            LOGGER.debug("配置已刷新");
        }
    }

    /**
     * 保存配置
     */
    public static void saveConfig() {
        ensureInitialized();
        if (configHolder != null) {
            try {
                configHolder.save();
                LOGGER.debug("配置已保存");
            } catch (Exception e) {
                LOGGER.error("保存配置失败", e);
            }
        } else {
            LOGGER.warn("无法保存配置: config holder is null");
        }
    }

    /**
     * 检查配置是否可用
     */
    public static boolean isConfigAvailable() {
        return initialized && TestModClient.CONFIG != null;
    }

    /**
     * 确保配置已初始化
     */
    private static void ensureInitialized() {
        if (!initialized) {
            LOGGER.warn("配置尚未初始化，正在延迟初始化...");
            initialize();
        }
    }

    /**
     * 获取初始化状态
     */
    public static boolean isInitialized() {
        return initialized;
    }

    /**
     * 获取初始化耗时
     */
    public static long getInitializationTime() {
        return initializationTime;
    }
}