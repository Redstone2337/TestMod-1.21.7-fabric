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


/*
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
}*/

@Environment(EnvType.CLIENT)
public class FreezeSwordHud {
    private static final int BAR_WIDTH = 102;
    private static final int BAR_HEIGHT = 14;
    private static final int HUD_OFFSET_Y = 50;
    private static final int TEXT_OFFSET_Y = 18;

    // 使用Atomic保证线程安全
    private static final AtomicInteger charges = new AtomicInteger(0);
    private static final AtomicFloat progress = new AtomicFloat(0);
    private static final AtomicBoolean isCharging = new AtomicBoolean(false);
    private static long lastUpdateTime = 0;

    // 通过事件总线注册即时更新
    public static void register() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
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
                }
            }
        });

        HudRenderCallback.EVENT.register((context, tickDelta) -> {
            if (shouldRender()) {
                render(context);
            }
        });
    }

    public static void updateState(int newCharges, float newProgress, boolean charging) {
        charges.set(newCharges);
        progress.set(newProgress);
        isCharging.set(charging);
        lastUpdateTime = System.currentTimeMillis();
    }

    private static boolean shouldRender() {
        // 300ms的渲染余量，避免突然消失
        return (isCharging.get() || charges.get() > 0) && 
               (System.currentTimeMillis() - lastUpdateTime < 300);
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
        // 背景（带圆角效果）
        context.fillGradient(x, y, x + BAR_WIDTH, y + BAR_HEIGHT, 0x80000000, 0x90000000);
        
        // 计算平滑进度（避免帧间跳跃）
        float smoothProgress = MathHelper.lerp(0.2f, 
            progress.get() / (float)FreezeSwordItem.CHARGE_TIME, 
            charges.get() / (float)FreezeSwordItem.MAX_CHARGES
        );
        int filledWidth = (int)(BAR_WIDTH * smoothProgress);

        // 分段渲染提升性能
        int segmentSize = Math.max(2, BAR_WIDTH / 50);
        for (int i = 0; i < filledWidth; i += segmentSize) {
            int segmentEnd = Math.min(i + segmentSize, filledWidth);
            float segmentRatio = (i + segmentSize/2f) / (float)BAR_WIDTH;
            int color = calculateGradientColor(segmentRatio);
            context.fill(x + i, y + 1, x + segmentEnd, y + BAR_HEIGHT - 1, color);
        }

        // 高光边框
        context.drawBorder(x - 1, y - 1, BAR_WIDTH + 2, BAR_HEIGHT + 2, 0xAAFFFFFF);
        context.fill(x, y, x + BAR_WIDTH, y + 1, 0x66FFFFFF); // 顶部高光
    }

    private static int calculateGradientColor(float ratio) {
        // HSV色彩空间实现更平滑的蓝->青->绿->黄->红渐变
        float hue = 0.66f * (1 - ratio); // 蓝(0.66)到红(0.0)
        return Color.HSVToRGB(hue, 0.9f, 1.0f) | 0xFF000000;
    }

    private static void renderChargeText(DrawContext context, int screenWidth, int y) {
        MinecraftClient client = MinecraftClient.getInstance();
        int currentCharges = charges.get();
        
        // 动态文字效果
        Text text;
        if (currentCharges >= FreezeSwordItem.MAX_CHARGES) {
            float pulse = (float)Math.sin(System.currentTimeMillis() / 200f) * 0.2f + 0.8f;
            int gold = (int)(255 * pulse) << 16 | 0xA5 << 8 | 0x00;
            text = Text.literal("MAX!").styled(style -> 
                style.withColor(gold).withBold(true));
        } else {
            text = Text.translatable("hud.freezesword.charges", 
                currentCharges, FreezeSwordItem.MAX_CHARGES)
                .formatted(Formatting.AQUA);
        }

        // 文字阴影效果
        context.drawCenteredTextWithShadow(
            client.textRenderer,
            text,
            screenWidth / 2,
            y,
            0xFFFFFF
        );
    }

    public static void resetMaxChargesState() {
        charges.set(0);
        progress.set(0);
        isCharging.set(false);
    }
}