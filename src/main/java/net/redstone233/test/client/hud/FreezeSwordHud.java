package net.redstone233.test.client.hud;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.ColorHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.redstone233.test.core.component.FreezingSwordComponent;
import net.redstone233.test.core.component.type.ModDataComponentTypes;
import net.redstone233.test.items.custom.FreezeSwordItem;

@Environment(EnvType.CLIENT)
public class FreezeSwordHud {
    // 尺寸配置
    private static final int BAR_WIDTH = 102;
    private static final int BAR_HEIGHT = 14;
    private static final int HUD_OFFSET_Y = 50;
    private static final int TEXT_OFFSET_Y = 18;

    // 颜色定义（ARGB格式）
    private static final int BLUE = 0xFF3366FF;
    private static final int GREEN = 0xFF33FF66;
    private static final int YELLOW = 0xFFFFCC33;
    private static final int RED = 0xFFFF3333;

    // 渲染状态
    private static int charges = 0;
    private static float progress = 0;
    private static boolean isCharging = false;
    private static long lastRenderTime = 0;

    public static void register() {
        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            if (client.player == null) return;

            ItemStack mainHand = client.player.getMainHandStack();
            if (mainHand.getItem() instanceof FreezeSwordItem) {
                FreezingSwordComponent component = mainHand.get(ModDataComponentTypes.FREEZING_SWORD);
                if (component != null) {
                    updateState(
                        component.charges(),
                        component.chargeProgress(),
                        component.isCharging()
                    );
                    return;
                }
            }
            resetState();
        });

        HudRenderCallback.EVENT.register((context, tickDelta) -> {
            if (shouldRender()) {
                render(context);
            }
        });
    }

    private static void updateState(int newCharges, float newProgress, boolean charging) {
        charges = newCharges;
        progress = newProgress;
        isCharging = charging;
        lastRenderTime = System.currentTimeMillis();
    }

    private static void resetState() {
        charges = 0;
        progress = 0;
        isCharging = false;
    }

    private static boolean shouldRender() {
        return (isCharging || charges > 0) && 
               (System.currentTimeMillis() - lastRenderTime < 300);
    }

    private static void render(DrawContext context) {
        MinecraftClient client = MinecraftClient.getInstance();
        int screenWidth = client.getWindow().getScaledWidth();
        int x = (screenWidth - BAR_WIDTH) / 2;
        int y = client.getWindow().getScaledHeight() - HUD_OFFSET_Y;

        renderGradientBar(context, x, y);
        renderChargeText(context, screenWidth, y - TEXT_OFFSET_Y);
    }

    private static void renderGradientBar(DrawContext context, int x, int y) {
        // 背景
        context.fill(x, y, x + BAR_WIDTH, y + BAR_HEIGHT, 0x80000000);
        
        // 计算当前进度
        float smoothProgress = MathHelper.lerp(0.3f,
            progress / FreezeSwordItem.CHARGE_TIME,
            charges / (float)FreezeSwordItem.MAX_CHARGES
        );
        int filledWidth = (int)(BAR_WIDTH * smoothProgress);

        // 分段颜色过渡
        int segmentCount = 10;
        for (int i = 0; i < filledWidth; i += BAR_WIDTH / segmentCount) {
            int segmentEnd = Math.min(i + BAR_WIDTH / segmentCount, filledWidth);
            float segmentRatio = (i + (BAR_WIDTH / segmentCount)/2f) / BAR_WIDTH;
            int color = getSegmentColor(segmentRatio);
            context.fill(x + i, y + 1, x + segmentEnd, y + BAR_HEIGHT - 1, color);
        }

        // 边框
        context.drawBorder(x - 1, y - 1, BAR_WIDTH + 2, BAR_HEIGHT + 2, 0xAAFFFFFF);
    }

    private static int getSegmentColor(float ratio) {
        // 四色渐变：蓝 -> 绿 -> 黄 -> 红
        if (ratio < 0.33f) {
            return blendColors(BLUE, GREEN, ratio / 0.33f);
        } else if (ratio < 0.66f) {
            return blendColors(GREEN, YELLOW, (ratio - 0.33f) / 0.33f);
        } else {
            return blendColors(YELLOW, RED, (ratio - 0.66f) / 0.34f);
        }
    }

    private static int blendColors(int color1, int color2, float progress) {
        int r = (int)(ColorHelper.Argb.getRed(color1) * (1 - progress) + ColorHelper.Argb.getRed(color2) * progress);
        int g = (int)(ColorHelper.Argb.getGreen(color1) * (1 - progress) + ColorHelper.Argb.getGreen(color2) * progress);
        int b = (int)(ColorHelper.Argb.getBlue(color1) * (1 - progress) + ColorHelper.Argb.getBlue(color2) * progress);
        return ColorHelper.Argb.getArgb(255, r, g, b);
    }

    private static void renderChargeText(DrawContext context, int screenWidth, int y) {
        MinecraftClient client = MinecraftClient.getInstance();
        
        Text text;
        if (charges >= FreezeSwordItem.MAX_CHARGES) {
            // 动态金色闪烁
            float pulse = (float)Math.sin(System.currentTimeMillis() / 200f) * 0.25f + 0.75f;
            int gold = ColorHelper.Argb.getArgb(255, 
                (int)(255 * pulse), 
                (int)(200 * pulse), 
                0);
            text = Text.literal("MAX!").styled(style -> 
                style.withColor(gold).withBold(true));
        } else {
            text = Text.translatable("hud.test.charges", 
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
}
