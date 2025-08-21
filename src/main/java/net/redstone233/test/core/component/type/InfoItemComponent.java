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
        textConsumer.accept(Text.literal("§b基础介绍：").formatted(Formatting.AQUA,Formatting.BOLD));
        textConsumer.accept(Text.literal("§a这是一个能力武器，可以在物品栏中查看其属性。")
                .formatted(Formatting.GRAY, Formatting.ITALIC).append(" "));

        textConsumer.accept(Text.literal("§b使用方法：").formatted(Formatting.BLUE, Formatting.BOLD));
        textConsumer.accept(Text.literal("§e右键会显示屏幕。").formatted(Formatting.YELLOW, Formatting.ITALIC));
        textConsumer.accept(Text.literal("§e对地面右键会爆炸").formatted(Formatting.DARK_BLUE, Formatting.ITALIC).append(" "));

        textConsumer.accept(Text.literal("§b请注意：").formatted(Formatting.BLUE, Formatting.BOLD));
        textConsumer.accept(Text.literal("§c注意：如果你不小心丢失了这个物品，你将无法再获得它。")
                .formatted(Formatting.RED, Formatting.ITALIC));
        textConsumer.accept(Text.literal("§b请妥善保管。").formatted(Formatting.GREEN, Formatting.ITALIC));
    }
}
