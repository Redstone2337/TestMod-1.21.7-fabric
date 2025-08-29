package net.redstone233.test.core.component.type;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.component.ComponentsAccess;
import net.minecraft.item.Item;
import net.minecraft.item.tooltip.TooltipAppender;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.function.Consumer;

public record NeoForgeItemComponent(boolean display) implements TooltipAppender {
    public static final NeoForgeItemComponent DEFAULT = new NeoForgeItemComponent(true);

    public static final Codec<NeoForgeItemComponent> CODEC = RecordCodecBuilder.create(neoForgeItemComponentInstance -> neoForgeItemComponentInstance.group(
            Codec.BOOL.fieldOf("display").forGetter(NeoForgeItemComponent::display)
    ).apply(neoForgeItemComponentInstance, NeoForgeItemComponent::new));


    @Override
    public void appendTooltip(Item.TooltipContext context, Consumer<Text> textConsumer, TooltipType type, ComponentsAccess components) {
        textConsumer.accept(Text.literal("按住").formatted(Formatting.WHITE)
                .append(Text.literal("[ Shift ]"))
                .formatted(Formatting.GOLD)
                .append(Text.literal("查看详细信息").formatted(Formatting.WHITE))
        );
        textConsumer.accept(Text.literal(" "));
        textConsumer.accept(Text.literal("默认效果：").formatted(Formatting.YELLOW));
        textConsumer.accept(Text.literal("  - 将玩家周围五格以内的鸡传送至玩家附近").formatted(Formatting.GRAY));
        textConsumer.accept(Text.literal("  - 范围内的怪物将被引燃").formatted(Formatting.GRAY));
        textConsumer.accept(Text.literal("  - 范围内的玩家获得抗性效果").formatted(Formatting.GRAY));
        if (Screen.hasShiftDown()) {
            textConsumer.accept(Text.literal("Shift效果：").formatted(Formatting.YELLOW));
            textConsumer.accept(Text.literal("  - 将玩家周围十格以内的鸡传送至玩家附近").formatted(Formatting.GRAY));
            textConsumer.accept(Text.literal("  - 范围内的怪物将被引燃更长时间").formatted(Formatting.GRAY));
            textConsumer.accept(Text.literal("  - 范围内的玩家获得更长时间的抗性效果").formatted(Formatting.GRAY));
        }
    }

    @Override
    public boolean display() {
        return display;
    }
}
