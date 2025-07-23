package net.redstone233.test.core.until;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

public class FreezeHelper {

    public static void freezeEntity(LivingEntity entity, int duration) {
        entity.addStatusEffect(new StatusEffectInstance(
                StatusEffects.SLOWNESS,
                duration,
                255,
                false,
                true,
                true
        ));
        entity.addStatusEffect(new StatusEffectInstance(
                StatusEffects.MINING_FATIGUE,
                duration,
                255,
                false,
                true,
                true
        ));
        entity.addStatusEffect(new StatusEffectInstance(
                StatusEffects.RESISTANCE,
                duration,
                255,
                false,
                true,
                true
        ));
    }
}
