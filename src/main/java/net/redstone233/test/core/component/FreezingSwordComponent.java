package net.redstone233.test.core.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.component.ComponentsAccess;
import net.minecraft.item.Item;
import net.minecraft.item.tooltip.TooltipAppender;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;
import net.redstone233.test.core.until.ModKeys;
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

    MutableText builder = Text.literal("");
    builder.append(Text.literal("[").formatted(Formatting.GRAY));

    for (int i = 0; i < segments; i++) {
        if (i < filledSegments) {
            // 颜色渐变：从白色 → 红色 → 蓝色 → 绿色 → 金色
            float position = (float) i / segments;
            Formatting color;
            
            if (position < 0.25f) {
                // 白色到红色过渡
                color = Formatting.WHITE;
            } else if (position < 0.5f) {
                // 红色到蓝色过渡
                color = Formatting.RED;
            } else if (position < 0.75f) {
                // 蓝色到绿色过渡
                color = Formatting.BLUE;
            } else {
                // 绿色到金色过渡（最后25%渐变为金色）
                if (position > 0.9f) {
                    color = Formatting.GOLD; // 最后10%纯金色
                } else {
                    // 绿色到金色渐变过渡
                    color = Formatting.GREEN;
                }
            }
            
            // 使用■字符并应用颜色
            builder.append(Text.literal("■").formatted(color));
        } else {
            // 未填充部分使用□字符
            builder.append(Text.literal("□").formatted(Formatting.DARK_GRAY));
        }
    }

    builder.append(Text.literal("]").formatted(Formatting.GRAY));
    textConsumer.accept(builder);
}

    @Override
    public void appendTooltip(Item.TooltipContext context, Consumer<Text> textConsumer, TooltipType type, ComponentsAccess components) {
        float chargePercent = getChargePercent();

        // 基础描述
        textConsumer.accept(Text.translatable("tooltip.freezing_sword.desc")
                .formatted(Formatting.GRAY));
        textConsumer.accept(Text.empty());

        // 蓄力状态
        if (isCharging) {
            textConsumer.accept(Text.translatable("tooltip.freezing_sword.charging",
                            (int)(chargePercent * 100))
                    .formatted(Formatting.BLUE));
        } else {
            textConsumer.accept(Text.translatable("tooltip.freezing_sword.charge_instruction",
                            Text.keybind(ModKeys.CHARGE_KEY.getBoundKeyLocalizedText().getString())
                                    .formatted(Formatting.GOLD))
                    .formatted(Formatting.YELLOW));
        }

        // 进度条
        addChargeProgressBar(textConsumer, chargePercent);

        // 伤害信息
        textConsumer.accept(Text.translatable("tooltip.freezing_sword.damage.normal",
                        FreezeSwordItem.BASE_DAMAGE)
                .formatted(Formatting.GREEN));
        textConsumer.accept(Text.translatable("tooltip.freezing_sword.damage.charged",
                        FreezeSwordItem.BOSS_DAMAGE)
                .formatted(Formatting.RED));
        textConsumer.accept(Text.translatable("tooltip.freezing_sword.damage.non_boss",
                        FreezeSwordItem.NON_BOSS_DAMAGE)
                .formatted(Formatting.LIGHT_PURPLE));

        // 蓄满提示
        if (chargePercent >= 1.0f) {
            textConsumer.accept(Text.translatable("tooltip.freezing_sword.ready")
                    .formatted(Formatting.AQUA, Formatting.BOLD));
        }

        // 来源提示
        textConsumer.accept(Text.translatable("tooltip.freezing_sword.loot_only")
                .formatted(Formatting.DARK_GRAY, Formatting.ITALIC));
    }
}