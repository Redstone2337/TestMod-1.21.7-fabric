package net.redstone233.test.core.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.component.ComponentType;
import net.minecraft.component.ComponentsAccess;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipAppender;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;
import net.redstone233.test.items.custom.FreezeSwordItem;

import java.util.function.Consumer;

public record FreezingSwordComponent(int chargeProgress, boolean isCharging) implements TooltipAppender {
    public static final FreezingSwordComponent DEFAULT = new FreezingSwordComponent(0, false);
    public static final Codec<FreezingSwordComponent> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.INT.fieldOf("charge").forGetter(FreezingSwordComponent::chargeProgress),
                    Codec.BOOL.fieldOf("charging").forGetter(FreezingSwordComponent::isCharging)
            ).apply(instance, FreezingSwordComponent::new)
    );

    public static final PacketCodec<RegistryByteBuf, FreezingSwordComponent> PACKET_CODEC = PacketCodec.tuple(
            PacketCodecs.INTEGER, FreezingSwordComponent::chargeProgress,
            PacketCodecs.BOOLEAN, FreezingSwordComponent::isCharging,
            FreezingSwordComponent::new
    );

    public float getChargePercent() {
        return MathHelper.clamp((float) chargeProgress / FreezeSwordItem.CHARGE_TIME, 0, 1);
    }

    private void addChargeProgressBar(Consumer<Text> textConsumer, float chargePercent) {
        int segments = 10;
        int filledSegments = (int) (chargePercent * segments);

        StringBuilder builder = new StringBuilder();
        builder.append(Formatting.GRAY + "[");

        for (int i = 0; i < segments; i++) {
            if (i < filledSegments) {
                // 颜色渐变：从红色到蓝色
                float hue = 0.66f * (1 - (float) i / segments);
                int rgb = java.awt.Color.HSBtoRGB(hue, 1.0f, 1.0f);
                builder.append(String.format("§x§%02X§%02X§%02X|",
                        (rgb >> 16) & 0xFF,
                        (rgb >> 8) & 0xFF,
                        rgb & 0xFF));
            } else {
                builder.append(Formatting.DARK_GRAY + "|");
            }
        }

        builder.append(Formatting.GRAY + "]");
        textConsumer.accept(Text.literal(builder.toString()));
    }

    @Override
    public void appendTooltip(Item.TooltipContext context, Consumer<Text> textConsumer, TooltipType type, ComponentsAccess components) {
        float chargePercent = getChargePercent();

        // 添加蓄力标题
        textConsumer.accept(Text.translatable("tooltip.freezing_sword.charge")
                .formatted(Formatting.GRAY));

        // 添加进度条
        addChargeProgressBar(textConsumer, chargePercent);

        // 蓄满提示
        if (chargePercent >= 1.0f) {
            textConsumer.accept(Text.translatable("tooltip.freezing_sword.ready")
                    .formatted(Formatting.AQUA, Formatting.BOLD));
        }
    }
}