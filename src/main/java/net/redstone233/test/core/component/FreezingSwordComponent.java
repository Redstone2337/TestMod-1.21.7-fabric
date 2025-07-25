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
                // 颜色渐变：从白色(透明)到黑色，经过红蓝绿渐变
                float position = (float) i / segments;
                int r, g, b;

                if (position < 0.33f) {
                    // 红色到蓝色过渡
                    float phase = position / 0.33f;
                    r = (int) (255 * (1 - phase));
                    g = 0;
                    b = (int) (255 * phase);
                } else if (position < 0.66f) {
                    // 蓝色到绿色过渡
                    float phase = (position - 0.33f) / 0.33f;
                    r = 0;
                    g = (int) (255 * phase);
                    b = (int) (255 * (1 - phase));
                } else {
                    // 绿色到黑色过渡
                    float phase = (position - 0.66f) / 0.34f;
                    r = 0;
                    g = (int) (255 * (1 - phase));
                    b = 0;
                }

                // 使用■字符并应用渐变颜色
                builder.append(Text.literal("■").styled(style ->
                        style.withColor(r << 16 | g << 8 | b)
                ));
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