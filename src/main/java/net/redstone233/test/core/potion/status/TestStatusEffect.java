package net.redstone233.test.core.potion.status;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.redstone233.test.core.until.RandomNumber;

public class TestStatusEffect extends StatusEffect {
//    private final int random = RandomNumber.nextInt(-1, 10);

    public TestStatusEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean applyUpdateEffect(ServerWorld world, LivingEntity entity, int amplifier) {
        int min = -1;
        int max = 10;
        if (entity instanceof PlayerEntity player) {
//            int random = RandomNumber.nextInt(-1,10);
            int random = MathHelper.nextBetween(world.getRandom(), min, max);
            player.sendMessage(Text.literal("当前随机数：" + random)
                    .formatted(Formatting.GRAY,Formatting.BOLD)
                    .append(Text.literal(" (范围: " + min + " ~ " + max + ")")
                            .formatted(Formatting.BLUE,Formatting.BOLD))
                    , false);

            if (amplifier == 2) {
                applyDefaultEffect(player);
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.MINING_FATIGUE,1200,4,false,true,true));
                player.sendMessage(Text.literal("[GeneralMessage]")
                                .formatted(Formatting.GRAY,Formatting.BOLD)
                                .append(Text.literal("变化不大，只是多了一个药水而已")
                                        .formatted(Formatting.BLUE,Formatting.BOLD))
                        , true);
            }


            applyDefaultEffect(player);


           switch (random) {
               case 1 -> player.sendMessage(Text.literal("[GeneralMessage]")
                       .formatted(Formatting.GRAY,Formatting.BOLD)
                               .append(Text.literal("什么都没发生。")
                                       .formatted(Formatting.BLUE,Formatting.BOLD))
                       , true);
               case 5 -> applyTestEffect(player);
               case 10 -> applyStatusEffect(player);
               default -> applyDefaultEffect(player);

           }

           applyRandomEffect(player,random);

            return true;
        } else if (entity instanceof LivingEntity livingEntity) {
//            livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS,600, 3));
            switch (amplifier) {
                case 1 -> livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS,600, 3));
                case 2 -> livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS,600, 4));
                default -> livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING,300, 3));
            }
            return true;
        } else {
            return false;
        }
    }

    private static void applyStatusEffect(PlayerEntity player) {
        player.sendMessage(Text.literal("[LuckyMessage]")
                .formatted(Formatting.GOLD,Formatting.BOLD)
                .append(Text.literal("这次的你很幸运，获得了村庄英雄药水效果！")
                        .formatted(Formatting.BLUE,Formatting.BOLD)
                ),true);
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.HERO_OF_THE_VILLAGE, 1200, 5));
    }

    private static void applyDefaultEffect(PlayerEntity player) {
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 600, 1));
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.LUCK, 300, 5));
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 300, 6));
    }

    private static void applyTestEffect(PlayerEntity player) {
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE,600,4));
        player.sendMessage(Text.literal("[LuckyMessage]")
                .formatted(Formatting.GOLD,Formatting.BOLD)
                .append(Text.literal("这次的你很幸运，获得了火焰抗性药水效果！")
                        .formatted(Formatting.BLUE,Formatting.BOLD)
                ), true
        );
    }

    private static void applyRandomEffect(PlayerEntity player, int random) {
        switch (random) {
            case 7:
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.HEALTH_BOOST,600,7));
                player.sendMessage(Text.literal("[LuckyMessage]")
                        .formatted(Formatting.GOLD,Formatting.BOLD)
                        .append(Text.literal("这次的你很幸运，获得了生命提升药水效果！")
                                .formatted(Formatting.BLUE,Formatting.BOLD)
                        ), true
                );
                break;
            case 9:
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED,600,4));
                player.sendMessage(Text.literal("[LuckyMessage]")
                        .formatted(Formatting.GOLD,Formatting.BOLD)
                        .append(Text.literal("这次的你很幸运，获得了速度提升药水效果！")
                                .formatted(Formatting.BLUE,Formatting.BOLD)
                        ), true
                );
                break;
            case -1:
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.BAD_OMEN,600,1));
                player.sendMessage(Text.literal("[UnluckyMessage]")
                        .formatted(Formatting.GOLD,Formatting.BOLD)
                        .append(Text.literal("这次运气很差，获得了不祥之兆药水效果！")
                                .formatted(Formatting.BLUE,Formatting.BOLD)
                        ), true
                );
                break;
            default:
                applyDefaultEffect(player);
                break;
        }
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }
}
