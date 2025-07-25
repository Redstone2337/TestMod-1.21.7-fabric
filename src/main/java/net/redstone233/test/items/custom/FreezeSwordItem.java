package net.redstone233.test.items.custom;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
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
import net.minecraft.world.World;
import net.redstone233.test.core.component.FreezingSwordComponent;
import net.redstone233.test.core.component.type.ModDataComponentTypes;
import net.redstone233.test.core.until.FreezeHelper;
import net.redstone233.test.core.until.ModKeys;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

public class FreezeSwordItem extends Item {
    public static final int CHARGE_TIME = 40; // 2秒蓄力
    public static final float BASE_DAMAGE = 11.5F; // 基础伤害
    public static final float BOSS_DAMAGE = 115.0F; // 对Boss的伤害（10倍）
    public static final float NON_BOSS_DAMAGE = BASE_DAMAGE * 2; // 非法目标伤害（2倍）

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

    // 客户端按键监听
    // 替换原有的 handleKeyInput 方法
    public static void handleKeyInput(PlayerEntity player) {
        if (player.getWorld().isClient && ModKeys.isChargeKeyPressed()) {
            ItemStack stack = player.getMainHandStack();
            if (stack.getItem() instanceof FreezeSwordItem) {
                stack.set(ModDataComponentTypes.FREEZING_SWORD, new FreezingSwordComponent(0, true));
                player.sendMessage(Text.translatable("msg.freezesword.start_charging"), true);
            }
        }
    }

    @Override
    public void inventoryTick(ItemStack stack, ServerWorld world, Entity entity, @Nullable EquipmentSlot slot) {
        if (!(entity instanceof PlayerEntity player)) return;

        FreezingSwordComponent component = stack.getOrDefault(ModDataComponentTypes.FREEZING_SWORD, FreezingSwordComponent.DEFAULT);
        boolean shouldCharge = slot == EquipmentSlot.MAINHAND && component.isCharging();
        int newProgress = shouldCharge ? component.chargeProgress() + 1 : 0;

        if (newProgress >= CHARGE_TIME) {
            newProgress = CHARGE_TIME;
            if (world.getTime() % 10 == 0) {
                player.sendMessage(Text.translatable("msg.freezesword.charged"), true);
            }
        }

        if (newProgress != component.chargeProgress() || shouldCharge != component.isCharging()) {
            stack.set(ModDataComponentTypes.FREEZING_SWORD, new FreezingSwordComponent(newProgress, shouldCharge));
        }
        super.inventoryTick(stack, world, entity, slot);
    }

    /*
    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, @Nullable EquipmentSlot slot) {
        if (!(entity instanceof PlayerEntity player)) return;

        FreezingSwordComponent component = stack.getOrDefault(ModDataComponentTypes.FREEZING_SWORD, FreezingSwordComponent.DEFAULT);
        boolean shouldCharge = slot == EquipmentSlot.MAINHAND && component.isCharging();
        int newProgress = shouldCharge ? component.chargeProgress() + 1 : 0;

        if (newProgress >= CHARGE_TIME) {
            newProgress = CHARGE_TIME;
            if (world.getTime() % 10 == 0) {
                player.sendMessage(Text.translatable("msg.freezesword.charged"), true);
            }
        }

        if (newProgress != component.chargeProgress() || shouldCharge != component.isCharging()) {
            stack.set(ModDataComponentTypes.FREEZING_SWORD, new FreezingSwordComponent(newProgress, shouldCharge));
        }
    }*/

    @Override
    public void postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        FreezingSwordComponent component = stack.get(ModDataComponentTypes.FREEZING_SWORD);
        boolean isCharged = component != null && component.chargeProgress() >= CHARGE_TIME;
        World world = attacker.getWorld();

        if (isCharged && !attacker.getWorld().isClient() && attacker instanceof PlayerEntity player) {
            boolean isBoss = target instanceof WardenEntity || target instanceof EnderDragonEntity || target instanceof WitherEntity;
            float damage = isBoss ? BOSS_DAMAGE : NON_BOSS_DAMAGE;
            int freezeTime = isBoss ? 200 : 60;
            world = player.getWorld();

            // 伤害与冰冻
            target.damage((ServerWorld) world, attacker.getDamageSources().playerAttack(player), damage);
            FreezeHelper.freezeEntity(target, freezeTime);

            // 提示信息
            if (isBoss) {
                player.sendMessage(Text.translatable("msg.freezesword.boss_hit"), true);
            } else {
                player.sendMessage(Text.translatable("msg.freezesword.invalid_target"), true);
            }

            stack.set(ModDataComponentTypes.FREEZING_SWORD, new FreezingSwordComponent(0, false)); // 重置蓄力
        } else {
            // 普通攻击逻辑
            if (attacker instanceof PlayerEntity) {
                target.damage((ServerWorld) world, attacker.getDamageSources().playerAttack((PlayerEntity) attacker), BASE_DAMAGE);
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