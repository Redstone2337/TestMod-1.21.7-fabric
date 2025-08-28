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

public record NeoForgeItemComponent(boolean display) implements TooltipAppender {
    public static final NeoForgeItemComponent DEFAULT = new NeoForgeItemComponent(true);

    public static final Codec<NeoForgeItemComponent> CODEC = RecordCodecBuilder.create(neoForgeItemComponentInstance -> {
        return neoForgeItemComponentInstance.group(
                Codec.BOOL.fieldOf("display").forGetter(NeoForgeItemComponent::display)
        ).apply(neoForgeItemComponentInstance, NeoForgeItemComponent::new);
    });


    @Override
    public void appendTooltip(Item.TooltipContext context, Consumer<Text> textConsumer, TooltipType type, ComponentsAccess components) {
        textConsumer.accept(Text.literal("按住")
                .append(Text.literal("[Shift]"))
                .formatted(Formatting.GOLD)
                .append(Text.literal("将范围更新至10格"))
        );
    }

    @Override
    public boolean display() {
        return display;
    }
}
