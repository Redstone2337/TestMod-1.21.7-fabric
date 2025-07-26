// FreezeSwordItem.java
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
import net.redstone233.test.core.component.FreezingSwordComponent;
import net.redstone233.test.core.component.type.ModDataComponentTypes;
import net.redstone233.test.core.until.FreezeHelper;
import net.redstone233.test.core.until.ModKeys;
import net.redstone233.test.core.until.ModToolMaterial;
import net.redstone233.test.client.hud.FreezeSwordHud;
import net.redstone233.test.items.ModItems;
import org.jetbrains.annotations.Nullable;

public class FreezeSwordItem extends Item {
    public static final int CHARGE_TIME = 140;
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


public static void handleKeyInput(PlayerEntity player) {
    if (player.getWorld().isClient) {
        ItemStack stack = player.getMainHandStack();
        if (stack.getItem() instanceof FreezeSwordItem) {
            boolean isKeyPressed = ModKeys.isChargeKeyPressed();
            FreezingSwordComponent component = stack.get(ModDataComponentTypes.FREEZING_SWORD);
            boolean isCharging = component != null && component.isCharging();
            int charges = component != null ? component.charges() : 0;

            // 按键状态变化处理
            if (isKeyPressed != isCharging) {
                stack.set(ModDataComponentTypes.FREEZING_SWORD, 
                    new FreezingSwordComponent(
                        isKeyPressed ? 0 : component.chargeProgress(),
                        isKeyPressed,
                        charges
                    ));
                
                if (isKeyPressed) {
                    player.sendMessage(
                        Text.translatable("msg.freezesword.charge_start")
                            .formatted(Formatting.AQUA),
                        true);
                }
            }
        }
    }
}

@Override
public void inventoryTick(ItemStack stack, ServerWorld world, Entity entity, @Nullable EquipmentSlot slot) {
    if (!(entity instanceof PlayerEntity player)) return;

    FreezingSwordComponent component = stack.getOrDefault(ModDataComponentTypes.FREEZING_SWORD,
            FreezingSwordComponent.DEFAULT);
    
    // 只在蓄力状态且未达最大蓄力时增加进度
    if (component.isCharging() && component.charges() < MAX_CHARGES) {
        int newProgress = component.chargeProgress() + 1;
        int charges = component.charges();
        boolean isCharging = component.isCharging();

        // 完成一次蓄力
        if (newProgress >= CHARGE_TIME) {
            newProgress = 0;
            charges = Math.min(charges + 1, MAX_CHARGES);
            
            player.sendMessage(buildChargeMessage(charges), true);
            
            if (charges >= MAX_CHARGES) {
                isCharging = false;
                player.sendMessage(
                    Text.translatable("msg.freezesword.max_charges")
                        .formatted(Formatting.GREEN, Formatting.BOLD),
                    true);
            }
        }

        // 更新组件状态
        stack.set(ModDataComponentTypes.FREEZING_SWORD,
            new FreezingSwordComponent(newProgress, isCharging, charges));
    }
    super.inventoryTick(stack, world, entity, slot);
}public static void handleKeyInput(PlayerEntity player) {
    if (player.getWorld().isClient) {
        ItemStack stack = player.getMainHandStack();
        if (stack.getItem() instanceof FreezeSwordItem) {
            boolean isKeyPressed = ModKeys.isChargeKeyPressed();
            FreezingSwordComponent component = stack.get(ModDataComponentTypes.FREEZING_SWORD);
            boolean isCharging = component != null && component.isCharging();
            int charges = component != null ? component.charges() : 0;

            // 按键状态变化处理
            if (isKeyPressed != isCharging) {
                stack.set(ModDataComponentTypes.FREEZING_SWORD, 
                    new FreezingSwordComponent(
                        isKeyPressed ? 0 : component.chargeProgress(),
                        isKeyPressed,
                        charges
                    ));
                
                if (isKeyPressed) {
                    player.sendMessage(
                        Text.translatable("msg.freezesword.charge_start")
                            .formatted(Formatting.AQUA),
                        true);
                }
            }
        }
    }
}

@Override
public void inventoryTick(ItemStack stack, ServerWorld world, Entity entity, @Nullable EquipmentSlot slot) {
    if (!(entity instanceof PlayerEntity player)) return;

    FreezingSwordComponent component = stack.getOrDefault(ModDataComponentTypes.FREEZING_SWORD,
            FreezingSwordComponent.DEFAULT);
    
    // 只在蓄力状态且未达最大蓄力时增加进度
    if (component.isCharging() && component.charges() < MAX_CHARGES) {
        int newProgress = component.chargeProgress() + 1;
        int charges = component.charges();
        boolean isCharging = component.isCharging();

        // 完成一次蓄力
        if (newProgress >= CHARGE_TIME) {
            newProgress = 0;
            charges = Math.min(charges + 1, MAX_CHARGES);
            
            player.sendMessage(buildChargeMessage(charges), true);
            
            if (charges >= MAX_CHARGES) {
                isCharging = false;
                player.sendMessage(
                    Text.translatable("msg.freezesword.max_charges")
                        .formatted(Formatting.GREEN, Formatting.BOLD),
                    true);
            }
        }

        // 更新组件状态
        stack.set(ModDataComponentTypes.FREEZING_SWORD,
            new FreezingSwordComponent(newProgress, isCharging, charges));
    }
    super.inventoryTick(stack, world, entity, slot);
}
    
@Override
public void postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
    FreezingSwordComponent component = stack.get(ModDataComponentTypes.FREEZING_SWORD);
    int charges = component != null ? component.charges() : 0;

    if (!attacker.getWorld().isClient() && attacker instanceof PlayerEntity player) {
        boolean isBoss = target instanceof WardenEntity ||
                target instanceof EnderDragonEntity ||
                target instanceof WitherEntity;
        float damage = calculateDamage(charges, isBoss);
        float damageBonus = damage - BASE_DAMAGE;

        if (attacker.getWorld() instanceof ServerWorld serverWorld) {
            target.damage(serverWorld, attacker.getDamageSources().playerAttack(player), damage);
            FreezeHelper.freezeEntity(target, isBoss ? 200 : 60 * (charges + 1));
        }

        if (charges > 0) {
            player.sendMessage(buildHitMessage(stack, isBoss, charges, damageBonus), true);
            // 攻击后重置蓄力状态（包括charges）

            FreezeSwordHud.resetMaxChargesState();
            stack.set(ModDataComponentTypes.FREEZING_SWORD,
                    new FreezingSwordComponent(0, false, 0));
        }
    } else {
        World world = attacker.getWorld();
        if (world instanceof ServerWorld serverWorld) {
            target.damage(serverWorld,
                    attacker.getDamageSources().playerAttack(attacker.getAttackingPlayer()),
                    BASE_DAMAGE);
        }
        FreezeHelper.freezeEntity(target, 60);
    }
    super.postHit(stack, target, attacker);
}

    public static Text buildChargeMessage(int charges) {
    float damage = calculateDamage(charges, false);
    return Text.translatable("msg.freezesword.charging_progress",
        String.format("%.0f", 100f), // 进度百分比
        String.format("%.0f", damage) // 伤害值
    ).formatted(Formatting.AQUA);
    }

    public static Text buildHudText(FreezingSwordComponent component) {
    return Text.translatable("item.freeze.freezing_sword.charging",
        String.format("%.0f%%", component.getChargePercent() * 100)
    ).formatted(Formatting.AQUA);
    }

    private Text buildHitMessage(ItemStack stack, boolean isBoss, int charges, float damageBonus) {
        Text swordName = Text.translatable(stack.getItem().getTranslationKey()).formatted(Formatting.AQUA);

        return Text.empty()
                .append(Text.literal("[").formatted(Formatting.AQUA))
                .append(swordName)
                .append(Text.literal("] ").formatted(Formatting.AQUA))
                .append(Text.translatable(isBoss ? "msg.freezesword.boss_target" : "msg.freezesword.invalid_target")
                        .formatted(Formatting.RED))
                .append(" ")
                .append(Text.translatable("msg.freezesword.damage_plus")
                        .formatted(Formatting.GRAY)
                        .append(String.format("%.0f", damageBonus))
                        .formatted(Formatting.GOLD))
                .append(" ")
                .append(Text.translatable("msg.freezesword.charges_value")
                        .formatted(Formatting.GRAY)
                        .append(charges + "/" + MAX_CHARGES)
                        .formatted(Formatting.BLUE));
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
