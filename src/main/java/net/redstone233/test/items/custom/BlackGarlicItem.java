package net.redstone233.test.items.custom;

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
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.world.World;

public class BlackGarlicItem extends Item {
    public BlackGarlicItem(Settings settings) {
        super(settings);
    }

    public static AttributeModifiersComponent createAttributeModifiers() {
        return AttributeModifiersComponent.builder()
                .add(EntityAttributes.ATTACK_DAMAGE,
                        new EntityAttributeModifier(BASE_ATTACK_DAMAGE_MODIFIER_ID, 5.5f,
                                EntityAttributeModifier.Operation.ADD_VALUE),
                        AttributeModifierSlot.MAINHAND)
                .add(EntityAttributes.ATTACK_SPEED,
                        new EntityAttributeModifier(BASE_ATTACK_SPEED_MODIFIER_ID, 10.5F,
                                EntityAttributeModifier.Operation.ADD_VALUE),
                        AttributeModifierSlot.MAINHAND)
                .build();
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        if (!world.isClient) {
            world.createExplosion(context.getPlayer(),
                    context.getBlockPos().getX(),
                    context.getBlockPos().getY(),
                    context.getBlockPos().getZ(),
                    5.0f,
                    true,
                    World.ExplosionSourceType.TNT
            );
        }
        return ActionResult.SUCCESS;
    }

    @Override
    public void postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (attacker instanceof PlayerEntity player ) {
            player.addStatusEffect(new StatusEffectInstance(
                    StatusEffects.SPEED,
                    200,
                    3
            ));
        }
        if (target instanceof LivingEntity livingEntity) {
            livingEntity.setOnFireFor(300);
            livingEntity.setOnFire(true);
            livingEntity.setOnFireForTicks(300);
        }
        super.postHit(stack, target, attacker);
    }
}
