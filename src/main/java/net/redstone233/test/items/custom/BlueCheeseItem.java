package net.redstone233.test.items.custom;

import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import java.util.UUID;

public class BlueCheeseItem extends Item {
    // 指定要检查的数字
    private static final char TARGET_NUMBER = '7';


    public BlueCheeseItem(Settings settings) {
        super(settings);
    }

    public static AttributeModifiersComponent createAttributeModifiers() {
        return AttributeModifiersComponent.builder()
                .add(EntityAttributes.ATTACK_DAMAGE,
                        new EntityAttributeModifier(BASE_ATTACK_DAMAGE_MODIFIER_ID, 30.0F,
                                EntityAttributeModifier.Operation.ADD_VALUE),
                        AttributeModifierSlot.MAINHAND)
                .add(EntityAttributes.ATTACK_SPEED,
                        new EntityAttributeModifier(BASE_ATTACK_SPEED_MODIFIER_ID, 15.0F,
                                EntityAttributeModifier.Operation.ADD_VALUE),
                        AttributeModifierSlot.MAINHAND)
                .build();
    }


    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (user instanceof PlayerEntity player) {
            if (!world.isClient) {
                applyEffects(player);
            }

        player.incrementStat(Stats.USED.getOrCreateStat(this));
            if (player instanceof ServerPlayerEntity) {
                Criteria.CONSUME_ITEM.trigger((ServerPlayerEntity)player, stack);
            }

        }
        return stack.isEmpty() ? ItemStack.EMPTY : new ItemStack(stack.getItem());
    }

    private void applyEffects(PlayerEntity player) {
        UUID uuid = player.getUuid();
        boolean hasTargetNumber = checkUuidForNumber(uuid, TARGET_NUMBER);

        if (hasTargetNumber) {
            // 给予正面效果
            player.addStatusEffect(new StatusEffectInstance(
                    StatusEffects.REGENERATION,  // 生命恢复
                    200,  // 持续时间(ticks): 10秒
                    1     // 效果等级
            ));
            player.addStatusEffect(new StatusEffectInstance(
                    StatusEffects.SATURATION,    // 饱和
                    100,  // 持续时间(ticks): 5秒
                    0     // 效果等级
            ));
            player.addStatusEffect(new StatusEffectInstance(
                    StatusEffects.STRENGTH,      // 力量
                    200,  // 持续时间(ticks): 10秒
                    0     // 效果等级
            ));
            player.sendMessage(Text.literal("体质已达成！")
                    .formatted(Formatting.WHITE)
                    .append(Text.literal("[蓝纹奶酪享受者]")
                            .formatted(Formatting.AQUA)),
                    false);
        } else {
            // 给予负面效果
            player.addStatusEffect(new StatusEffectInstance(
                    StatusEffects.POISON,        // 中毒
                    200,  // 持续时间(ticks): 10秒
                    1     // 效果等级
            ));
            player.addStatusEffect(new StatusEffectInstance(
                    StatusEffects.NAUSEA,        // 反胃
                    200,  // 持续时间(ticks): 10秒
                    0     // 效果等级
            ));

            player.sendMessage(Text.literal("体质已达成！")
                    .formatted(Formatting.WHITE)
                            .append(Text.literal("[蓝纹奶酪受害者]")
                                    .formatted(Formatting.AQUA)),
                    false);
        }
    }

    /**
     * 检查UUID中是否包含指定数字的算法模块
     * @param uuid 玩家的UUID
     * @param targetNumber 要查找的数字
     * @return 如果UUID中包含指定数字返回true，否则返回false
     */
    private boolean checkUuidForNumber(UUID uuid, char targetNumber) {
        // 将UUID转换为字符串（移除连字符）
        String uuidStr = uuid.toString().replace("-", "");

        // 检查字符串中是否包含目标数字
        return uuidStr.indexOf(targetNumber) != -1;
    }
}
