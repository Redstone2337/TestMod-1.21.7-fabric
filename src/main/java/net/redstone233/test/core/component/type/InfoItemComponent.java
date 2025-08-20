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

public record InfoItemComponent(boolean Display) implements TooltipAppender {

    public static final InfoItemComponent DEFAULT = new InfoItemComponent(true);

    public static final Codec<InfoItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.BOOL.fieldOf("isDisplay").forGetter(InfoItemComponent::Display)
    ).apply(instance,InfoItemComponent::new));

    @Override
    public boolean Display() {
        return Display;
    }

    @Override
    public void appendTooltip(Item.TooltipContext context, Consumer<Text> textConsumer, TooltipType type, ComponentsAccess components) {
        textConsumer.accept(Text.literal("会爆炸的物品！对地面右键触发").formatted(Formatting.AQUA,Formatting.BOLD));
    }
}
