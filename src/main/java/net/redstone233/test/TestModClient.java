package net.redstone233.test;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.TooltipComponentCallback;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.item.v1.ComponentTooltipAppenderRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.redstone233.test.client.ClientLongPressHandler;
import net.redstone233.test.client.hud.FreezeSwordHud;
import net.redstone233.test.client.tooltip.FreezeSwordTooltipComponent;
import net.redstone233.test.core.api.PlayerDataProvider;
import net.redstone233.test.core.component.ModDataComponentTypes;
import net.redstone233.test.core.config.ConfigInitializer;
import net.redstone233.test.core.config.ModConfig;
import net.redstone233.test.core.screen.AnnouncementScreen;
import net.redstone233.test.core.screen.EnhancedPlayerInfoScreen;
import net.redstone233.test.core.until.ModKeys;
import net.redstone233.test.core.until.PlayerDataFactory;
import net.redstone233.test.items.custom.FreezeSwordItem;
import net.redstone233.test.longpress.FreezeSwordLongPressAction;
import net.redstone233.test.longpress.LongPressManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class TestModClient implements ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger(TestMod.MOD_ID);
    public static ModConfig CONFIG;
    public static boolean DEBUG_MODE = false;
    public static boolean SHOW_ICON = true; // 新增：图标显示状态
    // 添加静态变量来跟踪是否已显示公告
    private static boolean hasAnnouncementBeenShown = false;

    public static boolean isHasAnnouncementBeenShown() {
        return hasAnnouncementBeenShown;
    }

    public static void setHasAnnouncementBeenShown(boolean hasAnnouncementBeenShown) {
        TestModClient.hasAnnouncementBeenShown = hasAnnouncementBeenShown;
    }


    @Override
    public void onInitializeClient() {
        LOGGER.info("开始初始化测试模组客户端...");
        long startTime = System.currentTimeMillis();

        // 第一步：初始化配置系统
        ConfigInitializer.initialize();

        // 第二步：初始化其他组件
        initializeTooltips();
        initializeLongPress();
        initializeHud();
        initializeKeyBindings();

        // 添加世界进入事件监听
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            if (client.world != null && client.player != null) {
                showAnnouncementIfNeeded(client);
            }
        });

        LOGGER.info("客户端初始化完成，总耗时 {}ms", System.currentTimeMillis() - startTime);
    }

    private void initializeTooltips() {
        LOGGER.debug("初始化工具提示系统...");

        TooltipComponentCallback.EVENT.register(data -> {
            if (data instanceof FreezeSwordTooltipComponent.Data(ItemStack stack)) {
                return new FreezeSwordTooltipComponent(stack);
            }
            return null;
        });

        ComponentTooltipAppenderRegistry.addBefore(DataComponentTypes.LORE, ModDataComponentTypes.FREEZING_SWORD);
        ComponentTooltipAppenderRegistry.addBefore(DataComponentTypes.LORE, ModDataComponentTypes.HE_QI_ZHENG);
        ComponentTooltipAppenderRegistry.addBefore(DataComponentTypes.LORE, ModDataComponentTypes.DELICIOUS_BLACK_GARLIC);
        ComponentTooltipAppenderRegistry.addBefore(DataComponentTypes.LORE, ModDataComponentTypes.INFO_ITEM);
        ComponentTooltipAppenderRegistry.addBefore(DataComponentTypes.LORE, ModDataComponentTypes.NEO_FORGE_ITEM);
    }

    private void showAnnouncementIfNeeded(MinecraftClient client) {
        // 确保配置已加载
        if (!ConfigInitializer.isInitialized()) {
            ConfigInitializer.initialize();
        }

        if (!CONFIG.showOnWorldEnter || hasAnnouncementBeenShown) {
            TestModClient.LOGGER.info("不显示公告: showOnWorldEnter={}, hasAnnouncementBeenShown={}",
                    CONFIG.showOnWorldEnter, hasAnnouncementBeenShown);
            return;
        }

        // 计算当前公告内容的哈希值
        String currentHash = calculateAnnouncementHash();
        TestModClient.LOGGER.info("公告哈希比较: 当前={}, 上次={}", currentHash, CONFIG.lastDisplayedHash);

        // 如果公告内容已更改或从未显示过
        if (!currentHash.equals(CONFIG.lastDisplayedHash)) {
            client.execute(() -> {
                if (client.currentScreen == null) {
                    TestModClient.LOGGER.info("显示公告屏幕");
                    client.setScreen(new AnnouncementScreen());
                    hasAnnouncementBeenShown = true;

                    // 更新配置中的哈希值
                    CONFIG.lastDisplayedHash = currentHash;
                    saveConfig();
                } else {
                    TestModClient.LOGGER.info("已有其他屏幕打开，延迟显示公告");
                    // 延迟显示公告
                    client.execute(() -> {
                        if (client.currentScreen == null) {
                            client.setScreen(new AnnouncementScreen());
                            hasAnnouncementBeenShown = true;
                            CONFIG.lastDisplayedHash = currentHash;
                            saveConfig();
                        }
                    });
                }
            });
        } else {
            TestModClient.LOGGER.info("公告内容未更改，不显示");
        }
    }

    private String calculateAnnouncementHash() {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            StringBuilder contentBuilder = new StringBuilder();

            if (CONFIG.announcementContent != null) {
                for (String line : CONFIG.announcementContent) {
                    contentBuilder.append(line);
                }
            }

            byte[] hash = digest.digest(contentBuilder.toString().getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error("无法计算公告哈希值", e);
            return "";
        }
    }

    private void initializeLongPress() {
        LOGGER.debug("初始化长按系统...");

        ClientLongPressHandler.init();
        LongPressManager.registerAction("freeze_sword_charge", new FreezeSwordLongPressAction());
        LongPressManager.bindKeyToAction("charge_key", "freeze_sword_charge");
    }

    private void initializeHud() {
        LOGGER.debug("初始化HUD系统...");

        HudElementRegistry.addFirst(Identifier.of(TestMod.MOD_ID,"freeze_hud"),
                (drawContext, renderTickCounter) -> {
                    FreezeSwordHud.render(drawContext);
                });
    }

    private void initializeKeyBindings() {
        LOGGER.debug("初始化按键绑定...");

        ModKeys.register();

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            // 处理冻结剑输入
            if (client.player != null) {
                FreezeSwordItem.handleKeyInput(client.player);
            }

            // 处理公告按键
            handleAnnouncementKey(client);

            // 处理玩家信息按键
            handlePlayerInfoKey(client);

            // 处理调试按键
            handleDebugKeys();

            // 处理配置重载按键
            handleConfigReloadKey();
        });
    }

    private void handleAnnouncementKey(MinecraftClient client) {
        while (ModKeys.isAnnouncementKeyPressed()) {
            if (client.player != null && client.currentScreen == null) {
                ConfigInitializer.refreshConfig();
                client.setScreen(new AnnouncementScreen());
                break;
            }
        }
    }

    private void handleDebugKeys() {
        while (ModKeys.isDebugModPressed()) {
            DEBUG_MODE = !DEBUG_MODE;
            LOGGER.info("调试模式 {}", DEBUG_MODE ? "已开启" : "已关闭");
        }
    }

    private void handleConfigReloadKey() {
        while (ModKeys.isReloadPressed()) {
            LOGGER.info("手动重新加载配置...");
            ConfigInitializer.refreshConfig();
            DEBUG_MODE = CONFIG.debugMode;
            SHOW_ICON = CONFIG.showIcon; // 新增：同步图标显示状态
            LOGGER.info("配置已重新加载，调试模式: {}, 显示图标: {}", DEBUG_MODE, SHOW_ICON);
        }
    }

    private void handlePlayerInfoKey(MinecraftClient client) {
        while (ModKeys.isPlayerInfoPressed()) {
            if (client.player != null && client.currentScreen == null) {
                PlayerDataProvider dataProvider = PlayerDataFactory.getProvider(client.player);
                // 打开玩家信息界面
                client.setScreen(new EnhancedPlayerInfoScreen(client.player, dataProvider)); // 替换为实际的玩家信息屏幕
                break;
            }
        }
    }

    public static void saveConfig() {
        ConfigInitializer.saveConfig();
    }

    // TestModClient.java
    public static void setAnnouncementShown(boolean shown) {
        hasAnnouncementBeenShown = shown;
    }

    public static void resetAnnouncementShown() {
        hasAnnouncementBeenShown = false;
    }

    public static void refreshConfig() {
        ConfigInitializer.refreshConfig();
        // 新增：刷新配置后同步状态
        if (CONFIG != null) {
            DEBUG_MODE = CONFIG.debugMode;
            SHOW_ICON = CONFIG.showIcon;
        }
    }

    public static boolean isConfigAvailable() {
        return ConfigInitializer.isConfigAvailable();
    }
}