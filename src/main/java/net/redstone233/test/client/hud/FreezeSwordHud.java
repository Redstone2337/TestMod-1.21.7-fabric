/*
package net.redstone233.test.client.hud;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.redstone233.test.core.component.FreezingSwordComponent;
import net.redstone233.test.core.component.type.ModDataComponentTypes;
import net.redstone233.test.items.custom.FreezeSwordItem;

@Environment(EnvType.CLIENT)
public class FreezeSwordHud {
    // 尺寸配置
    private static final int BAR_WIDTH = 80;
    private static final int BAR_HEIGHT = 5;
    private static final int HUD_OFFSET_Y = 50;
    private static final int TEXT_OFFSET_Y = 10;

    // 四段式渐变颜色定义
    private static final int GREEN = ColorHelper.getArgb(255, 0, 255, 0);     // 纯绿
    private static final int BLUE = ColorHelper.getArgb(255, 0, 150, 255);     // 亮蓝
    private static final int YELLOW = ColorHelper.getArgb(255, 255, 255, 0);  // 纯黄
    private static final int RED = ColorHelper.getArgb(255, 255, 0, 0);        // 纯红

    // 渲染状态
    private static int charges = 0;
    private static int progress = 0;
    private static boolean isCharging = false;
    private static boolean hasFreezeSword = false;

    public static void checkAndUpdateState(ItemStack stack) {
        if (stack.getItem() instanceof FreezeSwordItem) {
            FreezingSwordComponent component = stack.get(ModDataComponentTypes.FREEZING_SWORD);
            if (component != null) {
                updateState(
                    component.charges(),
                    component.chargeProgress(),
                    component.isCharging()
                );
            }
        }
    }

    public static void updateState(int newCharges, int newProgress, boolean charging) {
        charges = newCharges;
        progress = newProgress;
        isCharging = charging;
    }

    public static void updatePlayerItems(PlayerEntity player) {
        if (player == null) {
            hasFreezeSword = false;
            return;
        }

        ItemStack mainHand = player.getMainHandStack();
        ItemStack offHand = player.getOffHandStack();

        hasFreezeSword = mainHand.getItem() instanceof FreezeSwordItem || 
                         offHand.getItem() instanceof FreezeSwordItem;

        if (hasFreezeSword) {
            checkAndUpdateState(mainHand);
            if (charges == 0 && !isCharging) {
                checkAndUpdateState(offHand);
            }
        }
    }

    private static boolean shouldRender() {
        return hasFreezeSword && (isCharging || charges > 0);
    }

    public static void render(DrawContext context) {
        if (!shouldRender()) return;
        
        MinecraftClient client = MinecraftClient.getInstance();
        int screenWidth = client.getWindow().getScaledWidth();
        int x = (screenWidth - BAR_WIDTH) / 2;
        int y = client.getWindow().getScaledHeight() - HUD_OFFSET_Y;

        renderProgressBar(context, x, y);
        renderChargeText(context, screenWidth, y - TEXT_OFFSET_Y);
    }

    private static void renderProgressBar(DrawContext context, int x, int y) {
        // 背景（带透明度）
        context.fill(x, y, x + BAR_WIDTH, y + BAR_HEIGHT, 0x80000000);

        // 计算进度比例
        float progressRatio = Math.min((float)progress / FreezeSwordItem.CHARGE_TIME, 1.0f);
        int filledWidth = (int)(BAR_WIDTH * progressRatio);

        // 四段式渐变进度条
        for (int i = 0; i < filledWidth; i++) {
            float ratio = i / (float)BAR_WIDTH;
            int color = calculateMultiGradientColor(ratio);
            context.fill(x + i, y, x + i + 1, y + BAR_HEIGHT, color);
        }

        // 边框
        context.drawBorder(x - 1, y - 1, BAR_WIDTH + 2, BAR_HEIGHT + 2, 0xAAFFFFFF);
    }

    private static int calculateMultiGradientColor(float ratio) {
        // 四段式渐变逻辑
        if (ratio < 0.33f) {
            // 绿→蓝 (0%-33%)
            return ColorHelper.lerp(ratio / 0.33f, GREEN, BLUE);
        } else if (ratio < 0.66f) {
            // 蓝→黄 (33%-66%)
            return ColorHelper.lerp((ratio - 0.33f) / 0.33f, BLUE, YELLOW);
        } else {
            // 黄→红 (66%-100%)
            return ColorHelper.lerp((ratio - 0.66f) / 0.34f, YELLOW, RED);
        }
    }

    private static void renderChargeText(DrawContext context, int screenWidth, int y) {
        MinecraftClient client = MinecraftClient.getInstance();
        float progressRatio = Math.min((float)progress / FreezeSwordItem.CHARGE_TIME, 1.0f);

        Text text;
        if (charges >= FreezeSwordItem.MAX_CHARGES) {
            text = Text.literal("MAX").formatted(Formatting.GOLD, Formatting.BOLD);
        } else if (isCharging) {
            text = Text.literal((int)(progressRatio * 100) + "%")
                   .formatted(Formatting.AQUA);
        } else {
            text = Text.literal(charges + "/" + FreezeSwordItem.MAX_CHARGES)
                   .formatted(Formatting.BLUE);
        }

        context.drawCenteredTextWithShadow(
            client.textRenderer,
            text,
            screenWidth / 2,
            y,
            0xFFFFFF
        );
    }

    public static void resetState() {
        charges = 0;
        progress = 0;
        isCharging = false;
        hasFreezeSword = false;
    }
}*/

package net.redstone233.test.client.hud;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.redstone233.test.core.component.FreezingSwordComponent;
import net.redstone233.test.core.component.type.ModDataComponentTypes;
import net.redstone233.test.items.custom.FreezeSwordItem;

@Environment(EnvType.CLIENT)
public class FreezeSwordHud {
    // 尺寸配置
    private static final int BAR_WIDTH = 80;
    private static final int BAR_HEIGHT = 5;
    private static final int HUD_OFFSET_Y = 50;
    private static final int TEXT_OFFSET_Y = 10;

    // 四段式渐变颜色定义
    private static final int GREEN = ColorHelper.getArgb(255, 0, 255, 0);     // 纯绿
    private static final int BLUE = ColorHelper.getArgb(255, 0, 150, 255);    // 亮蓝
    private static final int YELLOW = ColorHelper.getArgb(255, 255, 255, 0);  // 纯黄
    private static final int RED = ColorHelper.getArgb(255, 255, 0, 0);       // 纯红

    // 渲染状态
    private static int charges = 0;
    private static int progress = 0;
    private static boolean isCharging = false;
    private static boolean hasFreezeSword = false;
    private static boolean keyPressed = false;
    private static long lastChargeTime = 0;

    public static void updateKeyState(boolean pressed) {
        keyPressed = pressed;
    }

    public static void checkAndUpdateState(ItemStack stack) {
        if (stack.getItem() instanceof FreezeSwordItem) {
            FreezingSwordComponent component = stack.get(ModDataComponentTypes.FREEZING_SWORD);
            if (component != null) {
                updateState(
                    component.charges(),
                    component.chargeProgress(),
                    component.isCharging()
                );
                if (component.isCharging()) {
                    lastChargeTime = System.currentTimeMillis();
                }
            }
        }
    }

    public static void updateState(int newCharges, int newProgress, boolean charging) {
        charges = newCharges;
        progress = newProgress;
        isCharging = charging;
    }

    public static void updatePlayerItems(PlayerEntity player) {
        if (player == null) {
            hasFreezeSword = false;
            return;
        }

        ItemStack mainHand = player.getMainHandStack();
        ItemStack offHand = player.getOffHandStack();

        hasFreezeSword = mainHand.getItem() instanceof FreezeSwordItem || 
                         offHand.getItem() instanceof FreezeSwordItem;

        if (hasFreezeSword) {
            checkAndUpdateState(mainHand);
            if (charges == 0 && !isCharging) {
                checkAndUpdateState(offHand);
            }
        }
    }

    private static boolean shouldRender() {
        // 显示条件：按下I键、正在充能、或充能完成3秒内
        return hasFreezeSword && (keyPressed || isCharging || 
               (System.currentTimeMillis() - lastChargeTime < 3000 && charges > 0));
    }

    public static void render(DrawContext context) {
        if (!shouldRender()) return;

        MinecraftClient client = MinecraftClient.getInstance();
        int screenWidth = client.getWindow().getScaledWidth();
        int x = (screenWidth - BAR_WIDTH) / 2;
        int y = client.getWindow().getScaledHeight() - HUD_OFFSET_Y;

        renderProgressBar(context, x, y);
        renderChargeText(context, screenWidth, y - TEXT_OFFSET_Y);
    }

    private static void renderProgressBar(DrawContext context, int x, int y) {
        // 背景（带透明度）
        context.fill(x, y, x + BAR_WIDTH, y + BAR_HEIGHT, 0x80000000);

        float progressRatio = Math.min((float)progress / FreezeSwordItem.CHARGE_TIME, 1.0f);
        int filledWidth = (int)(BAR_WIDTH * progressRatio);

        // 四段式渐变进度条
        for (int i = 0; i < filledWidth; i++) {
            float ratio = i / (float)BAR_WIDTH;
            int color = calculateMultiGradientColor(ratio);
            context.fill(x + i, y, x + i + 1, y + BAR_HEIGHT, color);
        }

        // 边框
        context.drawBorder(x - 1, y - 1, BAR_WIDTH + 2, BAR_HEIGHT + 2, 0xAAFFFFFF);
    }

    private static int calculateMultiGradientColor(float ratio) {
        if (ratio < 0.33f) {
            return ColorHelper.lerp(ratio / 0.33f, GREEN, BLUE);
        } else if (ratio < 0.66f) {
            return ColorHelper.lerp((ratio - 0.33f) / 0.33f, BLUE, YELLOW);
        } else {
            return ColorHelper.lerp((ratio - 0.66f) / 0.34f, YELLOW, RED);
        }
    }

    private static void renderChargeText(DrawContext context, int screenWidth, int y) {
        MinecraftClient client = MinecraftClient.getInstance();
        float progressRatio = Math.min((float)progress / FreezeSwordItem.CHARGE_TIME, 1.0f);

        Text text;
        if (charges >= FreezeSwordItem.MAX_CHARGES) {
            text = Text.literal("MAX POWER!").formatted(Formatting.GOLD, Formatting.BOLD);
        } else if (isCharging) {
            text = Text.literal((int)(progressRatio * 100) + "%")
                   .formatted(Formatting.AQUA);
        } else {
            text = Text.literal(charges + "/" + FreezeSwordItem.MAX_CHARGES)
                   .formatted(Formatting.BLUE);
        }

        context.drawCenteredTextWithShadow(
            client.textRenderer,
            text,
            screenWidth / 2,
            y,
            0xFFFFFF
        );
    }

    public static void resetState() {
        charges = 0;
        progress = 0;
        isCharging = false;
        hasFreezeSword = false;
        lastChargeTime = 0;
    }
}