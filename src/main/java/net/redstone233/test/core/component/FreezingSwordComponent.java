package net.redstone233.test.core.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.component.ComponentType;
import net.minecraft.component.ComponentsAccess;
import net.minecraft.item.Item;
import net.minecraft.item.tooltip.TooltipAppender;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Util;

import java.util.function.Consumer;

public record FreezingSwordComponent(int chargeProgress, boolean isCharging) implements TooltipAppender {
    private static final int CHARGE_TIME = 40;

    @Override
    public void appendTooltip(Item.TooltipContext context, Consumer<Text> textConsumer, TooltipType type, ComponentsAccess components) {
        // 基础提示文本
        textConsumer.accept(Text.translatable("item.mtc.freezing_sword.tooltip1"));
        textConsumer.accept(Text.translatable("item.mtc.freezing_sword.tooltip2"));

        // 蓄力状态提示
        if (isCharging) {
            float chargePercent = (float) chargeProgress / CHARGE_TIME;

            // 动画效果
            String animChar = switch ((int)(Util.getMeasuringTimeMs() / 200 % 4)) {
                case 0 -> "⣾";
                case 1 -> "⣽";
                case 2 -> "⣻";
                case 3 -> "⢿";
                default -> "⣿";
            };

            // 添加动画和百分比
            textConsumer.accept(Text.literal(String.format("§a%s Charging: §e%.0f%%",
                    animChar, chargePercent * 100)));

            // 进度条
            int bars = 10;
            int filled = (int) (bars * chargePercent);
            textConsumer.accept(Text.literal("§8[" + "§a" + "|".repeat(filled) +
                    "§7" + "|".repeat(bars - filled) + "§8]"));
        }
    }

    public static final ComponentType<FreezingSwordComponent> TYPE =
            ComponentType.<FreezingSwordComponent>builder()
                    .codec(RecordCodecBuilder.create(instance -> instance.group(
                            Codec.INT.fieldOf("charge_progress").forGetter(FreezingSwordComponent::chargeProgress),
                            Codec.BOOL.fieldOf("is_charging").forGetter(FreezingSwordComponent::isCharging)
                    ).apply(instance, FreezingSwordComponent::new)))
                    .build();
}