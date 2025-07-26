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
import net.redstone233.test.core.component.FreezingSwordComponent;
import net.redstone233.test.core.component.type.ModDataComponentTypes;
import net.redstone233.test.items.custom.FreezeSwordItem;

@Environment(EnvType.CLIENT)
public class FreezeSwordHud {
    private static final Identifier CHARGE_INDICATOR = Identifier.of("textures/gui/icons.png");
    private static final int BAR_WIDTH = 100;
    private static final int BAR_HEIGHT = 10;
    private static final int HUD_OFFSET_Y = 60;
    
    // 用于跟踪是否已经完成5次蓄力
    private static boolean hasMaxCharges = false;

    public static void render(DrawContext context) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || client.player == null) return;

        PlayerEntity player = client.player;
        ItemStack mainHandStack = player.getMainHandStack();

        // 检查是否持有寒冰剑
        if (!(mainHandStack.getItem() instanceof FreezeSwordItem)) return;

        var component = mainHandStack.get(ModDataComponentTypes.FREEZING_SWORD);
        if (component == null) return;

        // 只要正在蓄力或有蓄力次数就显示HUD
        if (component.isCharging() || component.charges() > 0) {
            float chargePercent = component.isCharging() ? component.getChargePercent() : 1.0f;
            int windowWidth = client.getWindow().getScaledWidth();
            int x = (windowWidth - BAR_WIDTH) / 2;
            int y = client.getWindow().getScaledHeight() - HUD_OFFSET_Y;

            renderChargeBar(context, x, y, chargePercent);
            renderChargeText(context, windowWidth, y, component);

            // 蓄满时闪烁效果
            if (component.charges() >= FreezeSwordItem.MAX_CHARGES && 
                (System.currentTimeMillis() / 200) % 2 == 0) {
                context.fill(x, y, x + BAR_WIDTH, y + BAR_HEIGHT, 0x20FFFFFF);
            }
        }
    }

    private static void renderChargeBar(DrawContext context, int x, int y, float chargePercent) {
        // 背景
        context.fill(x, y, x + BAR_WIDTH, y + BAR_HEIGHT, 0x80000000);

        // 渐变进度条
        int filledWidth = (int)(BAR_WIDTH * chargePercent);
        for (int i = 0; i < filledWidth; i++) {
            float hue = 0.66f * (1 - (float)i/BAR_WIDTH); // 颜色从红到蓝渐变
            int color = java.awt.Color.HSBtoRGB(hue, 1.0f, 1.0f) | 0xFF000000;
            context.fill(x + i, y, x + i + 1, y + BAR_HEIGHT, color);
        }

        // 边框
        context.drawBorder(x, y, BAR_WIDTH, BAR_HEIGHT, 0xFFFFFFFF);
    }

    private static void renderChargeText(DrawContext context, int windowWidth, int y, FreezingSwordComponent component) {
        MinecraftClient client = MinecraftClient.getInstance();
        Text text;
        
        if (component == null) {
            text = Text.literal("Charges: 0/" + FreezeSwordItem.MAX_CHARGES);
        } else if (component.charges() >= FreezeSwordItem.MAX_CHARGES) {
            text = Text.translatable("msg.freezesword.max_charges_hud")
                    .formatted(Formatting.AQUA, Formatting.BOLD);
        } else {
            text = FreezeSwordItem.buildHudText(component);
        }

        context.drawCenteredTextWithShadow(
                client.textRenderer,
                text,
                windowWidth / 2,
                y - 12,
                (component != null && component.charges() >= FreezeSwordItem.MAX_CHARGES) ? 0xFF00FFFF : 0xFFFFFF
        );
    }
    
    // 当攻击完成后调用此方法重置HUD状态
    public static void resetMaxChargesState() {
        hasMaxCharges = false;
    }
}
