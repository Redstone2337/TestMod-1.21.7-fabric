package net.redstone233.test.client.hud;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.ColorHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.redstone233.test.core.component.FreezingSwordComponent;
import net.redstone233.test.core.component.type.ModDataComponentTypes;
import net.redstone233.test.items.custom.FreezeSwordItem;
import net.redstone233.test.TestMod;

@Environment(EnvType.CLIENT)
public class FreezeSwordHud {
    // 尺寸配置
    private static final int BAR_WIDTH = 102;
    private static final int BAR_HEIGHT = 14;
    private static final int HUD_OFFSET_Y = 50;
    private static final int TEXT_OFFSET_Y = 18;

    // 使用ColorHelper定义颜色（ARGB格式）
    private static final int BLUE = ColorHelper.getArgb(255, 51, 102, 255);   // 0xFF3366FF
    private static final int CYAN = ColorHelper.getArgb(255, 51, 255, 255);   // 0xFF33FFFF
    private static final int GREEN = ColorHelper.getArgb(255, 51, 255, 102);  // 0xFF33FF66
    private static final int YELLOW = ColorHelper.getArgb(255, 255, 204, 51); // 0xFFFFCC33
    private static final int RED = ColorHelper.getArgb(255, 255, 51, 51);     // 0xFFFF3333

    // 渲染状态
    private static int charges = 0;
    private static float progress = 0;
    private static boolean isCharging = false;
    private static long lastRenderTime = 0;

    public static void register() {
        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            if (client.player == null) return;

            ItemStack mainHand = client.player.getMainHandStack();
            ItemStack offHand = client.player.getOffHandStack();
            
            checkAndUpdateState(mainHand);
            if (charges == 0 && !isCharging) {
                checkAndUpdateState(offHand);
            }
        });

        HudRenderCallback.EVENT.register((context, tickDelta) -> {
            if (shouldRender()) render(context);
        });

        HudElementRegistry.attachElementBefore(
            VanillaHudElements.SLEEP, 
            Identifier.of(TestMod.MOD_ID, "freeze_hud"),
            (context, tickCounter) -> render(context)
        );
    }

    private static void checkAndUpdateState(ItemStack stack) {
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

    public static void updateState(int newCharges, float newProgress, boolean charging) {
        charges = newCharges;
        progress = newProgress;
        isCharging = charging;
        lastRenderTime = System.currentTimeMillis();
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
        // 背景（带透明度）
        context.fill(x, y, x + BAR_WIDTH, y + BAR_HEIGHT, 0x80000000);
        
        // 计算平滑进度（0.0-1.0）
        float smoothProgress = MathHelper.lerp(0.3f,
            progress / FreezeSwordItem.CHARGE_TIME,
            charges / (float)FreezeSwordItem.MAX_CHARGES
        );
        int filledWidth = (int)(BAR_WIDTH * smoothProgress);

        // 五段式渐变渲染（蓝→青→绿→黄→红）
        int segmentSize = Math.max(5, BAR_WIDTH / 20); // 每段5像素
        for (int i = 0; i < filledWidth; i += segmentSize) {
            int segmentEnd = Math.min(i + segmentSize, filledWidth);
            float segmentRatio = (i + segmentSize/2f) / BAR_WIDTH;
            int color = calculateGradientColor(segmentRatio);
            context.fill(x + i, y + 1, x + segmentEnd, y + BAR_HEIGHT - 1, color);
        }

        // 高光边框
        context.drawBorder(x - 1, y - 1, BAR_WIDTH + 2, BAR_HEIGHT + 2, 0xAAFFFFFF);
    }

    private static int calculateGradientColor(float ratio) {
        // 五段式渐变
        if (ratio < 0.25f) {
            return ColorHelper.lerp(ratio / 0.25f, BLUE, CYAN);
        } else if (ratio < 0.5f) {
            return ColorHelper.lerp((ratio - 0.25f) / 0.25f, CYAN, GREEN);
        } else if (ratio < 0.75f) {
            return ColorHelper.lerp((ratio - 0.5f) / 0.25f, GREEN, YELLOW);
        } else {
            return ColorHelper.lerp((ratio - 0.75f) / 0.25f, YELLOW, RED);
        }
    }

    private static void renderChargeText(DrawContext context, int screenWidth, int y) {
        MinecraftClient client = MinecraftClient.getInstance();
        
        Text text;
        if (charges >= FreezeSwordItem.MAX_CHARGES) {
            // 动态金色脉冲效果
            float pulse = (float)Math.sin(System.currentTimeMillis() / 200f) * 0.25f + 0.75f;
            int gold = ColorHelper.Argb.getArgb(255, 
                (int)(255 * pulse), 
                (int)(215 * pulse), 
                0);
            text = Text.literal("✦ MAX POWER ✦").styled(style -> 
                style.withColor(gold).withBold(true));
        } else if (isCharging) {
            text = Text.translatable("hud.test.charging", 
                    (int)(progress * 100f / FreezeSwordItem.CHARGE_TIME))
                .formatted(Formatting.AQUA);
        } else {
            text = Text.translatable("hud.test.charges", charges, FreezeSwordItem.MAX_CHARGES)
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
    }
}
