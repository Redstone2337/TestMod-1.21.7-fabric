package net.redstone233.test.core.component.type;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.component.ComponentsAccess;
import net.minecraft.item.Item;
import net.minecraft.item.tooltip.TooltipAppender;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.function.Consumer;

public record DeliciousBlackGarlicComponent(boolean display) implements TooltipAppender {
    public static final DeliciousBlackGarlicComponent DEFAULT = new DeliciousBlackGarlicComponent(true);

    public static final Codec<DeliciousBlackGarlicComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.BOOL.fieldOf("isDisplay").forGetter(DeliciousBlackGarlicComponent::display)
    ).apply(instance,DeliciousBlackGarlicComponent::new));

    @Override
    public boolean display() {
        return display;
    }

    @Override
    public void appendTooltip(Item.TooltipContext context, Consumer<Text> textConsumer, TooltipType type, ComponentsAccess components) {
        if (display) {
            textConsumer.accept(Text.literal("---吃一口会发生什么神奇的情况呢？").formatted(Formatting.AQUA,Formatting.BOLD));

            textConsumer.accept(Text.literal("  >要吃一口吗？").formatted(Formatting.GREEN,Formatting.BOLD));
            textConsumer.accept(Text.literal("  >非常“美味”。").formatted(Formatting.YELLOW,Formatting.BOLD));

            textConsumer.accept(Text.literal(" 可通过村民交易获取").formatted(Formatting.GRAY,Formatting.BOLD));
        }
    }
}
