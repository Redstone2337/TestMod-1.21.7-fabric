package net.redstone233.test.items.custom;

import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class QuiltItem extends Item {
    public QuiltItem(Settings settings) {
        super(settings);
    }

    public static AttributeModifiersComponent createAttributeModifiers() {
        return AttributeModifiersComponent.builder()
                .add(
                        EntityAttributes.ATTACK_DAMAGE,
                        new EntityAttributeModifier(BASE_ATTACK_DAMAGE_MODIFIER_ID, 20, EntityAttributeModifier.Operation.ADD_VALUE),
                        AttributeModifierSlot.MAINHAND
                )
                .add(
                        EntityAttributes.ATTACK_SPEED,
                        new EntityAttributeModifier(BASE_ATTACK_SPEED_MODIFIER_ID, 7.0f, EntityAttributeModifier.Operation.ADD_VALUE),
                        AttributeModifierSlot.MAINHAND
                )
                .build();
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        if (user instanceof PlayerEntity player) {
            if (!world.isClient) {
                player.heal(20.0F);
                applyEffects(player);
            }

        }
        return ActionResult.SUCCESS;
    }

    private static void applyEffects(PlayerEntity player) {
        // 这里可以添加你想要的效果
        player.addStatusEffect(
                new StatusEffectInstance(
                        StatusEffects.REGENERATION,
                        600,
                        4
                )
        );
        player.addStatusEffect(
                new StatusEffectInstance(
                        StatusEffects.RESISTANCE,
                        600,
                        4
                )
        );
        player.addStatusEffect(
                new StatusEffectInstance(
                        StatusEffects.FIRE_RESISTANCE,
                        600,
                        0
                )
        );
        player.addStatusEffect(
                new StatusEffectInstance(
                        StatusEffects.SPEED,
                        600,
                        4
                )
        );
    }
}
