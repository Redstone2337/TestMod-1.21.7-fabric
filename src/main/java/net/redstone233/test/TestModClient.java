package net.redstone233.test;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.TooltipComponentCallback;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.fabricmc.fabric.api.item.v1.ComponentTooltipAppenderRegistry;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.client.MinecraftClient;
import net.redstone233.test.client.ClientLongPressHandler;
import net.redstone233.test.client.hud.FreezeSwordHud;
import net.redstone233.test.client.tooltip.FreezeSwordTooltipComponent;
import net.redstone233.test.core.component.ModDataComponentTypes;
import net.redstone233.test.core.config.ModConfig;
import net.redstone233.test.core.screen.AnnouncementScreen;
import net.redstone233.test.core.until.ModKeys;
import net.redstone233.test.items.custom.FreezeSwordItem;
import net.redstone233.test.longpress.FreezeSwordLongPressAction;
import net.redstone233.test.longpress.LongPressManager;

public class TestModClient implements ClientModInitializer {
    public static ModConfig CONFIG;
    private static ConfigHolder<ModConfig> configHolder;
    public static boolean DEBUG_MODE = false;


    @Override
    public void onInitializeClient() {

        AutoConfig.register(ModConfig.class, Toml4jConfigSerializer::new);

        refreshConfig();

        // 注册Tooltip组件
        TooltipComponentCallback.EVENT.register(data -> {
            if (data instanceof FreezeSwordTooltipComponent.Data(ItemStack stack)) {
                return new FreezeSwordTooltipComponent(stack);
            }
            return null;
        });

        //ComponentTooltipAppenderRegistry.addLast(ModDataComponentTypes.FREEZING_SWORD);
        ComponentTooltipAppenderRegistry.addBefore(DataComponentTypes.LORE, ModDataComponentTypes.FREEZING_SWORD);
        ComponentTooltipAppenderRegistry.addBefore(DataComponentTypes.LORE, ModDataComponentTypes.HE_QI_ZHENG);
        ComponentTooltipAppenderRegistry.addBefore(DataComponentTypes.LORE, ModDataComponentTypes.DELICIOUS_BLACK_GARLIC);


        // 初始化长按处理器
        ClientLongPressHandler.init();

        // 注册冻结剑的长按动作
        LongPressManager.registerAction("freeze_sword_charge", new FreezeSwordLongPressAction());

        // 绑定按键到动作
        LongPressManager.bindKeyToAction("charge_key", "freeze_sword_charge");

        HudElementRegistry.addFirst(
                Identifier.of(TestMod.MOD_ID, "freeze_hud"),
                (context, tickCounter) -> {
                    FreezeSwordHud.render(context);
                });


        ModKeys.register(); // 注册键位
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player != null) {
                FreezeSwordItem.handleKeyInput(client.player);
            }

           while (ModKeys.isAnnouncementKeyPressed()) {
               if (client.player != null) {
                   refreshConfig();
                   client.setScreen(new AnnouncementScreen());
               }
           }

            while (ModKeys.isDebugModPressed()) {
                DEBUG_MODE = !DEBUG_MODE;
                syncDebugMode();
                TestMod.LOGGER.info("调试模式 {}", DEBUG_MODE ? "已开启" : "已关闭");
            }
        });
    }

    public static void syncDebugMode() {
        if (configHolder != null) {
            CONFIG.debugMode = DEBUG_MODE;
            saveConfig();
        }
    }


    // 安全获取配置的方法
    public static void refreshConfig() {
        try {
            if (configHolder != null) {
                CONFIG = configHolder.getConfig();
            } else {
                TestMod.LOGGER.error("Config holder is null, using default config");
                CONFIG = new ModConfig();
            }
        } catch (Exception e) {
            TestMod.LOGGER.error("Failed to load config, using default", e);
            CONFIG = new ModConfig();
        }
    }

    public static void saveConfig() {
        AutoConfig.getConfigHolder(ModConfig.class).save();
    }
}
