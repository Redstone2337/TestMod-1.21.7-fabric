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

public record HerbalTeaComponent(boolean isDisplay, Formatting color, Formatting font) implements TooltipAppender {
    public static final HerbalTeaComponent DEFAULT = new HerbalTeaComponent(true, Formatting.BOLD, Formatting.AQUA);

    public static final Codec<HerbalTeaComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.BOOL.fieldOf("isDisplay").forGetter(HerbalTeaComponent::isDisplay),
            Formatting.COLOR_CODEC.fieldOf("color").forGetter(HerbalTeaComponent::color),
            Formatting.CODEC.fieldOf("font").forGetter(HerbalTeaComponent::font)
    ).apply(instance,HerbalTeaComponent::new));

    @Override
    public Formatting color() {
        return color;
    }

    @Override
    public Formatting font() {
        return font;
    }

    @Override
    public boolean isDisplay() {
        return isDisplay;
    }

    @Override
    public void appendTooltip(Item.TooltipContext context, Consumer<Text> textConsumer, TooltipType type, ComponentsAccess components) {
        if (isDisplay) {
            textConsumer.accept(Text.literal("---喝一口会是什么情况呢？").formatted(color,font));
            
            textConsumer.accept(Text.literal("  >要喝一口吗？").formatted(Formatting.GREEN,Formatting.BOLD));
            textConsumer.accept(Text.literal("  >非常美味。").formatted(Formatting.YELLOW,Formatting.BOLD));
            textConsumer.accept(Text.literal(" 可通过配方直接合成").formatted(Formatting.GRAY,Formatting.BOLD));
        }
    }
}
