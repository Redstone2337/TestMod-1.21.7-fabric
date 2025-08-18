package net.redstone233.test.items.custom;

import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.mob.WardenEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import net.redstone233.test.core.component.type.FreezingSwordComponent;
import net.redstone233.test.core.component.ModDataComponentTypes;
import net.redstone233.test.core.until.FreezeHelper;
import net.redstone233.test.core.until.ModKeys;
import net.redstone233.test.core.until.ModToolMaterial;
import net.redstone233.test.client.hud.FreezeSwordHud;
import net.redstone233.test.items.ModItems;
import net.redstone233.test.longpress.LongPressManager;
import org.jetbrains.annotations.Nullable;

public class FreezeSwordItem extends Item {
    public static final int CHARGE_TIME = 100; // 5秒 (20 ticks/秒)
    public static final int MAX_CHARGES = 5;
    public static final float BASE_DAMAGE = ModToolMaterial.SILICON.attackDamageBonus() + ModItems.ATTACK_DAMAGE + 1;
    public static final float BOSS_DAMAGE = BASE_DAMAGE * 10;
    public static final float NON_BOSS_DAMAGE = BASE_DAMAGE * 3;
    public static final float CHARGE_DAMAGE_MULTIPLIER = 2.0f;

    public FreezeSwordItem(ToolMaterial material, float attackDamage, float attackSpeed, Settings settings) {
        super(settings
                .sword(material, attackDamage, attackSpeed)
                .component(ModDataComponentTypes.FREEZING_SWORD, FreezingSwordComponent.DEFAULT)
        );
    }

    public static AttributeModifiersComponent createAttributeModifiers() {
        return AttributeModifiersComponent.builder()
                .add(EntityAttributes.ATTACK_DAMAGE,
                        new EntityAttributeModifier(BASE_ATTACK_DAMAGE_MODIFIER_ID, BASE_DAMAGE,
                                EntityAttributeModifier.Operation.ADD_VALUE),
                        AttributeModifierSlot.MAINHAND)
                .add(EntityAttributes.ATTACK_SPEED,
                        new EntityAttributeModifier(BASE_ATTACK_SPEED_MODIFIER_ID, 3.5F,
                                EntityAttributeModifier.Operation.ADD_VALUE),
                        AttributeModifierSlot.MAINHAND)
                .build();
    }

    // 使用长按接口的按键处理方法
    public static void handleKeyInput(PlayerEntity player) {
        if (player.getWorld().isClient()) {
            ItemStack stack = player.getMainHandStack();
            boolean isKeyPressed = ModKeys.isChargeKeyPressed();

            // 通过长按管理器处理按键
            LongPressManager.handleKeyInput(
                    player,
                    "charge_key",
                    isKeyPressed,
                    stack
            );

            // 更新HUD按键状态
            FreezeSwordHud.updateKeyState(isKeyPressed);
        }
    }

    // 适配新版本的 inventoryTick 方法
    @Override
    public void inventoryTick(ItemStack stack, ServerWorld world, Entity entity, @Nullable EquipmentSlot slot) {
        super.inventoryTick(stack, world, entity, slot);

        // 确保实体是玩家且物品在手上
        if (!(entity instanceof PlayerEntity player)) return;
        if (slot == null || (!slot.equals(EquipmentSlot.MAINHAND) && !slot.equals(EquipmentSlot.OFFHAND))) return;

        FreezingSwordComponent component = stack.getOrDefault(ModDataComponentTypes.FREEZING_SWORD,
                FreezingSwordComponent.DEFAULT);

        // 检查长按管理器状态
        LongPressManager.PressData pressData = LongPressManager.getPressData(player);
        boolean isLongPressing = pressData != null &&
                pressData.action.getName().equals("freeze_sword_charge");

        // 同步组件状态
        if (isLongPressing && !component.isCharging()) {
            // 开始蓄力
            stack.set(ModDataComponentTypes.FREEZING_SWORD,
                    new FreezingSwordComponent(component.chargeProgress(), true, component.charges()));
        } else if (!isLongPressing && component.isCharging()) {
            // 停止蓄力
            stack.set(ModDataComponentTypes.FREEZING_SWORD,
                    new FreezingSwordComponent(component.chargeProgress(), false, component.charges()));
        }

        // 如果正在蓄力，更新进度
        if (component.isCharging()) {
            int newProgress = Math.min(component.chargeProgress() + 1, CHARGE_TIME);
            int currentCharges = component.charges();

            // 更新进度
            stack.set(ModDataComponentTypes.FREEZING_SWORD,
                    new FreezingSwordComponent(newProgress, true, currentCharges));

            // 当进度完成时增加充能点
            if (newProgress == CHARGE_TIME) {
                int newCharges = Math.min(currentCharges + 1, MAX_CHARGES);

                // 创建新的组件状态
                stack.set(ModDataComponentTypes.FREEZING_SWORD,
                        new FreezingSwordComponent(0, true, newCharges));

                // 发送充能消息
                player.sendMessage(buildChargeMessage(newCharges), true);

                // 检查是否达到最大充能
                if (newCharges >= MAX_CHARGES) {
                    player.sendMessage(
                            Text.translatable("msg.freezesword.max_charges")
                                    .formatted(Formatting.GREEN, Formatting.BOLD),
                            true);

                    // 达到最大充能时停止蓄力
                    stack.set(ModDataComponentTypes.FREEZING_SWORD,
                            new FreezingSwordComponent(0, false, newCharges));
                }
            }
        }
    }

    @Override
    public void postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        World world = target.getWorld();
        // 获取组件状态
        FreezingSwordComponent component = stack.get(ModDataComponentTypes.FREEZING_SWORD);
        int charges = component != null ? component.charges() : 0;

        // 确保在服务端执行且攻击者是玩家
        if (!attacker.getWorld().isClient() && attacker instanceof PlayerEntity player) {
            // 检查目标是否是Boss
            boolean isBoss = target instanceof WardenEntity ||
                    target instanceof EnderDragonEntity ||
                    target instanceof WitherEntity;

            // 计算伤害
            float damage = calculateDamage(charges, isBoss);
            float damageBonus = damage - BASE_DAMAGE;

            // 应用伤害和冰冻效果
            if (world instanceof ServerWorld serverWorld) {
                target.damage(serverWorld,attacker.getDamageSources().playerAttack(player), damage);
            }
            FreezeHelper.freezeEntity(target, isBoss ? 200 : 60 * (charges + 1));

            // 如果有充能点，消耗它们
            if (charges > 0) {
                player.sendMessage(buildHitMessage(stack, isBoss, charges, damageBonus), true);
                stack.set(ModDataComponentTypes.FREEZING_SWORD,
                        new FreezingSwordComponent(0, false, 0));
                FreezeSwordHud.resetState();
            }
        } else {
            // 普通攻击
            if (world instanceof ServerWorld serverWorld) {
                target.damage(serverWorld,attacker.getDamageSources().playerAttack(attacker.getAttackingPlayer()),
                        BASE_DAMAGE);
            }
            FreezeHelper.freezeEntity(target, 60);
        }
        super.postHit(stack, target, attacker);
    }

    public static Text buildHudText(FreezingSwordComponent component) {
        return Text.literal((int)(component.getChargePercent() * 100) + "%")
                .formatted(Formatting.AQUA);
    }

    private Text buildHitMessage(ItemStack stack, boolean isBoss, int charges, float damageBonus) {
        Text swordName = Text.translatable(stack.getItem().getTranslationKey()).formatted(Formatting.AQUA);
        MutableText message = Text.empty()
                .append(Text.literal("[").formatted(Formatting.AQUA))
                .append(swordName)
                .append(Text.literal("] ").formatted(Formatting.AQUA));

        if (isBoss) {
            message.append(Text.translatable("msg.freezesword.boss_target"));
        } else {
            message.append(Text.translatable("msg.freezesword.invalid_target"));
        }

        message.append(" ")
                .append(Text.translatable("msg.freezesword.damage_plus")
                        .append(String.format("%.0f", damageBonus))
                        .append(" ")
                        .append(Text.translatable("msg.freezesword.charges_value")
                                .append(charges + "/" + MAX_CHARGES)));

        return message;
    }

    public static Text buildChargeMessage(int charges) {
        float damage = calculateDamage(charges, false);
        return Text.translatable("msg.freezesword.charging_progress",
                String.format("%.0f", 100f),
                String.format("%.0f", damage)
        ).formatted(Formatting.AQUA);
    }

    public static float calculateDamage(int charges, boolean isBoss) {
        float damageMultiplier = 1.0f + (CHARGE_DAMAGE_MULTIPLIER * charges);
        return isBoss ? BOSS_DAMAGE * damageMultiplier : NON_BOSS_DAMAGE * damageMultiplier;
    }

    @Override
    public void postDamageEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.damage(1, attacker, EquipmentSlot.MAINHAND);
        super.postDamageEntity(stack, target, attacker);
    }
}