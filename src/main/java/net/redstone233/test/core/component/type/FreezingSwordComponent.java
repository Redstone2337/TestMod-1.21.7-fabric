/*
package net.redstone233.test.core.component.type;

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

public record FreezingSwordComponent(int chargeProgress, boolean isCharging, int charges) implements TooltipAppender {
    public static final FreezingSwordComponent DEFAULT = new FreezingSwordComponent(0, false, 0);

    public FreezingSwordComponent(int chargeProgress, boolean isCharging) {
        this(chargeProgress, isCharging, 0);
    }

    public static final Codec<FreezingSwordComponent> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.INT.fieldOf("charge").forGetter(FreezingSwordComponent::chargeProgress),
                    Codec.BOOL.fieldOf("charging").forGetter(FreezingSwordComponent::isCharging),
                    Codec.INT.fieldOf("charges").forGetter(FreezingSwordComponent::charges)
            ).apply(instance, FreezingSwordComponent::new)
    );

    public static final PacketCodec<RegistryByteBuf, FreezingSwordComponent> PACKET_CODEC = PacketCodec.tuple(
            PacketCodecs.INTEGER, FreezingSwordComponent::chargeProgress,
            PacketCodecs.BOOLEAN, FreezingSwordComponent::isCharging,
            PacketCodecs.INTEGER, FreezingSwordComponent::charges,
            FreezingSwordComponent::new
    );

    public float getChargePercent() {
        return MathHelper.clamp((float) chargeProgress / FreezeSwordItem.CHARGE_TIME, 0, 1);
    }

    public boolean isMaxCharges() {
        return charges >= FreezeSwordItem.MAX_CHARGES;
    }

    public boolean canCharge() {
        return !isMaxCharges() && isCharging;
    }

    private void addChargeProgressBar(Consumer<Text> textConsumer, float chargePercent) {
        int segments = 10;
        int filledSegments = (int) (chargePercent * segments);

        MutableText builder = Text.literal("");
        builder.append(Text.literal("[").formatted(Formatting.GRAY));

        for (int i = 0; i < segments; i++) {
            if (i < filledSegments) {
                float position = (float) i / segments;
                Formatting color = getColorForPosition(position);
                builder.append(Text.literal("■").formatted(color));
            } else {
                builder.append(Text.literal("□").formatted(Formatting.DARK_GRAY));
            }
        }

        builder.append(Text.literal("]").formatted(Formatting.GRAY));
        textConsumer.accept(builder);
    }

    private Formatting getColorForPosition(float position) {
        if (position < 0.25f) return Formatting.WHITE;
        if (position < 0.5f) return Formatting.RED;
        if (position < 0.75f) return Formatting.BLUE;
        return position > 0.9f ? Formatting.GOLD : Formatting.GREEN;
    }

    @Override
    public void appendTooltip(Item.TooltipContext context, Consumer<Text> textConsumer, TooltipType type, ComponentsAccess components) {
        float chargePercent = getChargePercent();

        // 基础描述
        textConsumer.accept(Text.translatable("tooltip.freezing_sword.desc")
                .formatted(Formatting.GRAY));
        textConsumer.accept(Text.empty());

        // 蓄力状态
        if (isCharging && !isMaxCharges()) {
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
                FreezeSwordItem.calculateDamage(charges, true))
            .formatted(Formatting.RED));
            
        textConsumer.accept(Text.translatable("tooltip.freezing_sword.damage.non_boss",
                FreezeSwordItem.calculateDamage(charges, false))
            .formatted(Formatting.LIGHT_PURPLE));

        // 蓄力状态提示
        if (isMaxCharges()) {
            textConsumer.accept(Text.translatable("tooltip.freezing_sword.ready")
                .formatted(Formatting.AQUA, Formatting.BOLD));
        } else if (charges > 0) {
            textConsumer.accept(Text.translatable("tooltip.freezing_sword.charges",
                    charges, FreezeSwordItem.MAX_CHARGES)
                .formatted(Formatting.BLUE));
        }

        // 来源提示
        textConsumer.accept(Text.translatable("tooltip.freezing_sword.loot_only")
            .formatted(Formatting.DARK_GRAY, Formatting.ITALIC));
    }
}*/
package net.redstone233.test.core.component.type;

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

public record FreezingSwordComponent(int chargeProgress, boolean isCharging, int charges) implements TooltipAppender {
    public static final FreezingSwordComponent DEFAULT = new FreezingSwordComponent(0, false, 0);

    public FreezingSwordComponent(int chargeProgress, boolean isCharging) {
        this(chargeProgress, isCharging, 0);
    }

    public static final Codec<FreezingSwordComponent> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.INT.fieldOf("charge").forGetter(FreezingSwordComponent::chargeProgress),
                    Codec.BOOL.fieldOf("charging").forGetter(FreezingSwordComponent::isCharging),
                    Codec.INT.fieldOf("charges").forGetter(FreezingSwordComponent::charges)
            ).apply(instance, FreezingSwordComponent::new)
    );

    public static final PacketCodec<RegistryByteBuf, FreezingSwordComponent> PACKET_CODEC = PacketCodec.tuple(
            PacketCodecs.INTEGER, FreezingSwordComponent::chargeProgress,
            PacketCodecs.BOOLEAN, FreezingSwordComponent::isCharging,
            PacketCodecs.INTEGER, FreezingSwordComponent::charges,
            FreezingSwordComponent::new
    );

    public float getChargePercent() {
        return MathHelper.clamp((float) chargeProgress / FreezeSwordItem.CHARGE_TIME, 0, 1);
    }

    public boolean isMaxCharges() {
        return charges >= FreezeSwordItem.MAX_CHARGES;
    }

    public boolean canCharge() {
        return !isMaxCharges() && isCharging;
    }

    private void addChargeProgressBar(Consumer<Text> textConsumer, float chargePercent) {
        int segments = 10;
        int filledSegments = (int) (chargePercent * segments);

        MutableText builder = Text.literal("");
        builder.append(Text.literal("[").formatted(Formatting.GRAY));

        for (int i = 0; i < segments; i++) {
            if (i < filledSegments) {
                float position = (float) i / segments;
                Formatting color = getColorForPosition(position);
                builder.append(Text.literal("■").formatted(color));
            } else {
                builder.append(Text.literal("□").formatted(Formatting.DARK_GRAY));
            }
        }

        builder.append(Text.literal("]").formatted(Formatting.GRAY));
        textConsumer.accept(builder);
    }

    private Formatting getColorForPosition(float position) {
        if (position < 0.25f) return Formatting.WHITE;
        if (position < 0.5f) return Formatting.RED;
        if (position < 0.75f) return Formatting.BLUE;
        return position > 0.9f ? Formatting.GOLD : Formatting.GREEN;
    }

    @Override
    public void appendTooltip(Item.TooltipContext context, Consumer<Text> textConsumer, TooltipType type, ComponentsAccess components) {
        float chargePercent = getChargePercent();

        // 基础描述
        textConsumer.accept(Text.translatable("tooltip.freezing_sword.desc")
                .formatted(Formatting.GRAY));
        textConsumer.accept(Text.empty());

        // 蓄力状态
        if (isCharging && !isMaxCharges()) {
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
                        FreezeSwordItem.calculateDamage(charges, true))
                .formatted(Formatting.RED));

        textConsumer.accept(Text.translatable("tooltip.freezing_sword.damage.non_boss",
                        FreezeSwordItem.calculateDamage(charges, false))
                .formatted(Formatting.LIGHT_PURPLE));

        // 蓄力状态提示
        if (isMaxCharges()) {
            textConsumer.accept(Text.translatable("tooltip.freezing_sword.ready")
                    .formatted(Formatting.AQUA, Formatting.BOLD));
        } else if (charges > 0) {
            textConsumer.accept(Text.translatable("tooltip.freezing_sword.charges",
                            charges, FreezeSwordItem.MAX_CHARGES)
                    .formatted(Formatting.BLUE));
        }

        // 来源提示
        textConsumer.accept(Text.translatable("tooltip.freezing_sword.loot_only")
                .formatted(Formatting.DARK_GRAY, Formatting.ITALIC));
    }

    // 添加访问器方法
    public int chargeProgress() {
        return chargeProgress;
    }

    public boolean isCharging() {
        return isCharging;
    }

    public int charges() {
        return charges;
    }
}
