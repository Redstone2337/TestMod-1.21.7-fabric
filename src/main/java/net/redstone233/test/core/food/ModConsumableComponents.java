package net.redstone233.test.core.food;

import net.minecraft.component.type.ConsumableComponent;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.consume.ApplyEffectsConsumeEffect;
import net.minecraft.item.consume.ConsumeEffect;
import net.minecraft.item.consume.UseAction;
import net.minecraft.scoreboard.Team;
import net.minecraft.sound.SoundEvents;
import net.redstone233.test.TestMod;

import java.util.List;

public class ModConsumableComponents {

    public static final ConsumableComponent HE_QI_ZHENG = drink()
            .consumeSeconds(2.0f)
            .consumeEffect(
                            new ApplyEffectsConsumeEffect(
                            List.of(
                                    new StatusEffectInstance(StatusEffects.LUCK,600,3,false,true),
                                    new StatusEffectInstance(StatusEffects.RESISTANCE,600,3,false,true),
                                    new StatusEffectInstance(StatusEffects.NAUSEA,600,3,false,true)
                                )
                            )
                        )
            .consumeEffect(
                    new ApplyEffectsConsumeEffect(new StatusEffectInstance(
                            StatusEffects.HERO_OF_THE_VILLAGE,
                            3600,
                            4,
                            true,
                            false
                    ),0.5F)
            )
            .build();

    public static final ConsumableComponent DELICIOUS_BLACK_GARLIC = food()
            .consumeSeconds(3.0f)
            .consumeEffect(
                    new ApplyEffectsConsumeEffect(
                           new StatusEffectInstance(
                                   StatusEffects.NAUSEA,
                                   1200,
                                   2,
                                   true,
                                   false
                           )
                    )
            )
            .consumeEffect(new ApplyEffectsConsumeEffect(
                    new StatusEffectInstance(
                            StatusEffects.SPEED,
                            1200,
                            2,
                            true,
                            false
                    ),
                    0.3f
            ))
            .consumeEffect(
                    new ApplyEffectsConsumeEffect(
                    new StatusEffectInstance(
                            StatusEffects.JUMP_BOOST,
                            1200,
                            2,
                            true,
                            false
                    ),
                    0.4f
            ))
            .consumeEffect(new ApplyEffectsConsumeEffect(
                    new StatusEffectInstance(
                            StatusEffects.HEALTH_BOOST,
                            1200,
                            9,
                            true,
                            false
                    ),
                    0.2f
            ))
            .build();

    public static void init() {
        TestMod.LOGGER.info("饮品食品组件注册成功");
    }

    public static ConsumableComponent.Builder food() {
        return ConsumableComponent.builder().consumeSeconds(1.6F).useAction(UseAction.EAT).sound(SoundEvents.ENTITY_GENERIC_EAT).consumeParticles(true);
    }

    public static ConsumableComponent.Builder drink() {
        return ConsumableComponent.builder().consumeSeconds(1.6F).useAction(UseAction.DRINK).sound(SoundEvents.ENTITY_GENERIC_DRINK).consumeParticles(false);
    }
}
