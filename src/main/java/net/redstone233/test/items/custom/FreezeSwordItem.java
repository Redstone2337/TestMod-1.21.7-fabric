package net.redstone233.test.items.custom;

import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.mob.WardenEntity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import net.redstone233.test.core.component.FreezingSwordComponent;
import net.redstone233.test.core.component.type.ModDataComponentTypes;
import net.redstone233.test.core.until.FreezeHelper;
import net.redstone233.test.core.until.ModKeys;
import net.redstone233.test.core.until.ModToolMaterial;
import org.jetbrains.annotations.Nullable;

public class FreezeSwordItem extends Item {
    public static final int CHARGE_TIME = 140; // 7秒蓄力
    public static final float BASE_DAMAGE = ModToolMaterial.SILICON.attackDamageBonus();// 基础伤害
    public static final float BOSS_DAMAGE = BASE_DAMAGE * 10; // 对Boss的伤害（10倍）
    public static final float NON_BOSS_DAMAGE = BASE_DAMAGE * 3; // 非法目标伤害（2倍）

    public FreezeSwordItem(ToolMaterial material, float attackDamage, float attackSpeed, Settings settings) {
        super(settings
                .sword(material, attackDamage, attackSpeed)
                .component(ModDataComponentTypes.FREEZING_SWORD, FreezingSwordComponent.DEFAULT)
        );
    }

    public static AttributeModifiersComponent createAttributeModifiers() {
        return AttributeModifiersComponent.builder()
                .add(
                        EntityAttributes.ATTACK_DAMAGE,
                        new EntityAttributeModifier(BASE_ATTACK_DAMAGE_MODIFIER_ID, BASE_DAMAGE, EntityAttributeModifier.Operation.ADD_VALUE),
                        AttributeModifierSlot.MAINHAND
                )
                .add(
                        EntityAttributes.ATTACK_SPEED,
                        new EntityAttributeModifier(BASE_ATTACK_SPEED_MODIFIER_ID, 3.5F, EntityAttributeModifier.Operation.ADD_VALUE),
                        AttributeModifierSlot.MAINHAND
                )
                .build();
    }

    // 修改后的长按检测逻辑
    public static void handleKeyInput(PlayerEntity player) {
        if (player.getWorld().isClient) {
            ItemStack stack = player.getMainHandStack();
            if (stack.getItem() instanceof FreezeSwordItem) {
                boolean isKeyPressed = ModKeys.isChargeKeyPressed();
                FreezingSwordComponent component = stack.get(ModDataComponentTypes.FREEZING_SWORD);
                boolean isCharging = component != null && component.isCharging();

                if (isKeyPressed != isCharging) {
                    stack.set(ModDataComponentTypes.FREEZING_SWORD, new FreezingSwordComponent(0, isKeyPressed));
                    if (isKeyPressed) {
                        // 获取绑定的按键名称
                        Text keyName = ModKeys.CHARGE_KEY.getBoundKeyLocalizedText();
                        player.sendMessage(
                                Text.literal("\n")
                                        .append(Text.translatable("msg.freezesword.start_charging", keyName)
                                                .formatted(Formatting.AQUA, Formatting.BOLD))
                                        .append("\n"),
                                true
                        );
                    } else {
                        // 获取绑定的按键名称
                        Text keyName = ModKeys.CHARGE_KEY.getBoundKeyLocalizedText();
                        player.sendMessage(
                                Text.literal("\n")
                                        .append(Text.translatable("msg.freezesword.charge_canceled", keyName)
                                                .formatted(Formatting.GRAY))
                                        .append("\n"),
                                true
                        );
                    }
                }
            }
        }
    }

    @Override
    public void inventoryTick(ItemStack stack, ServerWorld world, Entity entity, @Nullable EquipmentSlot slot) {
        if (!(entity instanceof PlayerEntity player)) return;

        FreezingSwordComponent component = stack.getOrDefault(ModDataComponentTypes.FREEZING_SWORD, FreezingSwordComponent.DEFAULT);
        boolean isCharging = component.isCharging();
        int newProgress = isCharging ? component.chargeProgress() + 1 : 0;

        if (newProgress >= CHARGE_TIME) {
            newProgress = CHARGE_TIME;
            // 每2秒（40 ticks）提醒一次蓄力进度
            if (world.getTime() % 40 == 0 && isCharging) {
                int secondsLeft = (CHARGE_TIME - component.chargeProgress()) / 20;
                player.sendMessage(
                    Text.literal("\n")
                        .append(Text.translatable("msg.freezesword.charging_progress", 
                            String.format("%.1f", component.getChargePercent() * 100),
                            secondsLeft)
                            .formatted(Formatting.BLUE))
                        .append("\n"),
                    true
                );
            }
            
            // 蓄力完成时发送特殊消息
            if (newProgress == CHARGE_TIME && component.chargeProgress() < CHARGE_TIME) {
                player.sendMessage(
                    Text.literal("\n")
                        .append(Text.translatable("msg.freezesword.fully_charged")
                        .formatted(Formatting.GREEN, Formatting.BOLD)
                        .append("\n")
                        ), true);
            }
        }

        if (newProgress != component.chargeProgress() || isCharging != component.isCharging()) {
            stack.set(ModDataComponentTypes.FREEZING_SWORD, new FreezingSwordComponent(newProgress, isCharging));
        }
        super.inventoryTick(stack, world, entity, slot);
    }

    // 其余方法保持不变...
    @Override
    public void postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        FreezingSwordComponent component = stack.get(ModDataComponentTypes.FREEZING_SWORD);
        boolean isCharged = component != null && component.chargeProgress() >= CHARGE_TIME;

        if (isCharged && !attacker.getWorld().isClient() && attacker instanceof PlayerEntity player) {
            boolean isBoss = target instanceof WardenEntity || target instanceof EnderDragonEntity || target instanceof WitherEntity;
            float damage = isBoss ? BOSS_DAMAGE : NON_BOSS_DAMAGE;
            int freezeTime = isBoss ? 200 : 60;
            World world = player.getWorld();

            if (world instanceof ServerWorld serverWorld) {
                target.damage(serverWorld, attacker.getDamageSources().playerAttack(player), damage);
                FreezeHelper.freezeEntity(target, freezeTime);
            }

            if (isBoss) {
                player.sendMessage(
                        Text.literal("\n")
                                .append(Text.translatable("msg.freezesword.boss_hit").formatted(Formatting.RED, Formatting.BOLD))
                                .append("\n"),
                        true
                );
            } else {
                player.sendMessage(
                        Text.literal("\n")
                                .append(Text.translatable("msg.freezesword.invalid_target").formatted(Formatting.YELLOW))
                                .append("\n"),
                        true
                );
            }

            stack.set(ModDataComponentTypes.FREEZING_SWORD, new FreezingSwordComponent(0, false));
        } else {
            World world = attacker.getWorld();
            if (world instanceof ServerWorld serverWorld) {
                target.damage(serverWorld, attacker.getDamageSources().playerAttack(attacker.getAttackingPlayer()), BASE_DAMAGE);
            }
            FreezeHelper.freezeEntity(target, 60);
        }
        super.postHit(stack, target, attacker);
    }

    @Override
    public void postDamageEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.damage(1, attacker, EquipmentSlot.MAINHAND);
        super.postDamageEntity(stack, target, attacker);
    }
}