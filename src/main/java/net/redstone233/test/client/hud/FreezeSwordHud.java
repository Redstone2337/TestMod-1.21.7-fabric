package net.redstone233.test.client.hud;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.util.math.MathHelper;
import net.redstone233.test.core.component.FreezingSwordComponent;
import net.redstone233.test.core.component.type.ModDataComponentTypes;
import net.redstone233.test.items.custom.FreezeSwordItem;

import java.awt.Color;

@Environment(EnvType.CLIENT)
public class FreezeSwordHud {
    private static final int BAR_WIDTH = 102;
    private static final int BAR_HEIGHT = 14;
    private static final int HUD_OFFSET_Y = 50;
    private static final int TEXT_OFFSET_Y = 18;

    // 使用volatile保证多线程可见性
    private static volatile int charges = 0;
    private static volatile float progress = 0;
    private static volatile boolean isCharging = false;
    private static volatile long lastUpdateTime = 0;

    public static void register() {
        // 注册客户端tick事件
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;

            // 主手物品检测
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

        // 注册HUD渲染
        HudRenderCallback.EVENT.register((context, tickDelta) -> {
            if (shouldRender()) {
                render(context);
            }
        });

        // 注册网络包监听
        ClientPlayNetworking.registerGlobalReceiver(
            new Identifier("test", "hud_update"),
            (client, handler, buf, responseSender) -> {
                int newCharges = buf.readInt();
                float newProgress = buf.readFloat();
                boolean charging = buf.readBoolean();
                
                client.execute(() -> {
                    updateState(newCharges, newProgress, charging);
                });
            });
    }

    public static void updateState(int newCharges, float newProgress, boolean charging) {
        charges = newCharges;
        progress = newProgress;
        isCharging = charging;
        lastUpdateTime = System.currentTimeMillis();
    }

    private static boolean shouldRender() {
        return (isCharging || charges > 0) && 
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
        // 背景（带透明度渐变）
        context.fillGradient(x, y, x + BAR_WIDTH, y + BAR_HEIGHT, 0x80000000, 0x90000000);
        
        // 平滑进度计算
        float smoothProgress = MathHelper.lerp(0.2f, 
            progress / FreezeSwordItem.CHARGE_TIME, 
            charges / (float)FreezeSwordItem.MAX_CHARGES
        );
        int filledWidth = (int)(BAR_WIDTH * smoothProgress);

        // 分段渲染（优化性能）
        int segmentSize = Math.max(2, BAR_WIDTH / 50);
        for (int i = 0; i < filledWidth; i += segmentSize) {
            int segmentEnd = Math.min(i + segmentSize, filledWidth);
            float segmentRatio = (i + segmentSize/2f) / BAR_WIDTH;
            int color = calculateGradientColor(segmentRatio);
            context.fill(x + i, y + 1, x + segmentEnd, y + BAR_HEIGHT - 1, color);
        }

        // 边框效果
        context.drawBorder(x - 1, y - 1, BAR_WIDTH + 2, BAR_HEIGHT + 2, 0xAAFFFFFF);
    }

    private static int calculateGradientColor(float ratio) {
        // HSV色彩空间实现蓝->青->绿->黄->红渐变
        float hue = 0.66f * (1 - ratio); // 从蓝色(0.66)到红色(0.0)
        return Color.HSVToRGB(hue, 0.9f, 1.0f) | 0xFF000000;
    }

    private static void renderChargeText(DrawContext context, int screenWidth, int y) {
        MinecraftClient client = MinecraftClient.getInstance();
        
        Text text;
        if (charges >= FreezeSwordItem.MAX_CHARGES) {
            // 满蓄力闪烁效果
            float pulse = (float)Math.sin(System.currentTimeMillis() / 200f) * 0.2f + 0.8f;
            int gold = (int)(255 * pulse) << 16 | 0xA5 << 8 | 0x00;
            text = Text.literal("MAX POWER!").styled(style -> 
                style.withColor(gold).withBold(true));
        } else {
            text = Text.translatable("hud.test.charges", charges, FreezeSwordItem.MAX_CHARGES)
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
