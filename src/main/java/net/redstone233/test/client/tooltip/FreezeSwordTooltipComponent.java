package net.redstone233.test.client.tooltip;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipData;
import net.minecraft.util.Identifier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.redstone233.test.core.component.ModDataComponentTypes;

import java.util.Optional;

@Environment(EnvType.CLIENT)
public class FreezeSwordTooltipComponent implements TooltipComponent {
    private static final Identifier CHARGE_BAR = Identifier.of("textures/gui/bars.png");
    private final float chargeProgress;

    public FreezeSwordTooltipComponent(ItemStack stack) {
        var component = stack.get(ModDataComponentTypes.FREEZING_SWORD);
        this.chargeProgress = component != null ? component.getChargePercent() : 0;
    }

    @Override
    public int getHeight(TextRenderer textRenderer) {
        return 20;
    }

    @Override
    public int getWidth(TextRenderer textRenderer) {
        return 100;
    }

    @Override
    public void drawItems(TextRenderer textRenderer, int x, int y, int width, int height, DrawContext context) {
        // 背景
        context.fill(x, y, x + 100, y + 10, 0x80000000);

        // 渐变进度条
        int filledWidth = (int)(100 * chargeProgress);
        for (int i = 0; i < filledWidth; i++) {
            float hue = 0.66f * (1 - (float)i/100);
            int color = java.awt.Color.HSBtoRGB(hue, 1.0f, 1.0f) | 0xFF000000;
            context.fill(x + i, y, x + i + 1, y + 10, color);
        }

        // 边框
        context.drawBorder(x, y, 100, 10, 0xFFFFFFFF);

        // 百分比文本
        String text = String.format("%.0f%%", chargeProgress * 100);
        int textX = x + 50 - textRenderer.getWidth(text) / 2;
        int textY = y - 2;
        int textColor = chargeProgress >= 1.0f ? 0xFF00FFFF : 0xFFFFFF;
        context.drawText(textRenderer, text, textX, textY, textColor, false);

        // 蓄满闪烁效果
        if (chargeProgress >= 1.0f && (System.currentTimeMillis() / 200) % 2 == 0) {
            context.fill(x, y, x + 100, y + 10, 0x20FFFFFF);
        }
        TooltipComponent.super.drawItems(textRenderer, x, y, width, height, context);
    }

    public static Optional<TooltipData> of(ItemStack stack) {
        return Optional.of(new Data(stack));
    }

    public record Data(ItemStack stack) implements TooltipData {}
}