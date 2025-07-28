package net.redstone233.test.client.hud;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Formatting;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.redstone233.test.core.component.FreezingSwordComponent;
import net.redstone233.test.core.component.type.ModDataComponentTypes;
import net.redstone233.test.items.custom.FreezeSwordItem;

@Environment(EnvType.CLIENT)
public class FreezeSwordHud {
    private static final int BAR_WIDTH = 100;
    private static final int BAR_HEIGHT = 12;
    private static final int HUD_OFFSET_Y = 50;
    private static final int TEXT_OFFSET_Y = 5;

    // 客户端状态缓存
    private static int charges = 0;
    private static float progress = 0;
    private static boolean isCharging = false;

    public static void updateState(int charges, float progress, boolean isCharging) {
        FreezeSwordHud.charges = charges;
        FreezeSwordHud.progress = progress;
        FreezeSwordHud.isCharging = isCharging;
    }

    public static void render(DrawContext context) {
        if (!shouldRender()) return;

        MinecraftClient client = MinecraftClient.getInstance();
        int screenWidth = client.getWindow().getScaledWidth();
        int x = (screenWidth - BAR_WIDTH) / 2;
        int y = client.getWindow().getScaledHeight() - HUD_OFFSET_Y;

        renderGradientChargeBar(context, x, y);
        renderChargeText(context, screenWidth, y - TEXT_OFFSET_Y);
    }

    private static boolean shouldRender() {
        MinecraftClient client = MinecraftClient.getInstance();
        return client != null && client.player != null && 
              (isCharging || charges > 0);
    }

    private static void renderGradientChargeBar(DrawContext context, int x, int y) {
        // 背景
        context.fill(x, y, x + BAR_WIDTH, y + BAR_HEIGHT, 0x80000000);

        // 渐变进度条（蓝->绿->红）
        float chargePercent = progress / (float)FreezeSwordItem.CHARGE_TIME;
        int filledWidth = (int)(BAR_WIDTH * chargePercent);
        
        for (int i = 0; i < filledWidth; i++) {
            float positionRatio = (float)i / BAR_WIDTH;
            int color = calculateGradientColor(positionRatio);
            context.fill(x + i, y, x + i + 1, y + BAR_HEIGHT, color);
        }

        // 边框
        context.drawBorder(x - 1, y - 1, BAR_WIDTH + 2, BAR_HEIGHT + 2, 0xAAFFFFFF);
    }

    private static int calculateGradientColor(float ratio) {
        // 蓝(0%) -> 绿(50%) -> 红(100%)
        if (ratio < 0.5f) {
            // 蓝到绿过渡
            int r = (int)(0 * 255);
            int g = (int)(ratio * 2 * 255);
            int b = (int)((1 - ratio * 2) * 255);
            return (0xFF << 24) | (r << 16) | (g << 8) | b;
        } else {
            // 绿到红过渡
            int r = (int)((ratio - 0.5f) * 2 * 255);
            int g = (int)((1 - (ratio - 0.5f) * 2) * 255);
            int b = 0;
            return (0xFF << 24) | (r << 16) | (g << 8) | b;
        }
    }

    private static void renderChargeText(DrawContext context, int screenWidth, int y) {
        MinecraftClient client = MinecraftClient.getInstance();
        Text text;

        if (charges >= FreezeSwordItem.MAX_CHARGES) {
            text = Text.translatable("msg.freezesword.max_charges_hud")
                    .formatted(Formatting.GOLD, Formatting.BOLD);
        } else {
            text = Text.translatable("item.freezesword.charges", 
                    charges, FreezeSwordItem.MAX_CHARGES)
                    .formatted(Formatting.AQUA);
        }

        context.drawCenteredTextWithShadow(
            client.textRenderer,
            text,
            screenWidth / 2,
            y,
            0xFFFFFF
        );
    }

    public static void resetMaxChargesState() {
        charges = 0;
        progress = 0;
        isCharging = false;
    }
}