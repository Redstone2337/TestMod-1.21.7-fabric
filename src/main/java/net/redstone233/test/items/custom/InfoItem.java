package net.redstone233.test.items.custom;

import net.minecraft.client.MinecraftClient;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.redstone233.test.core.screen.AnnouncementScreen;
import net.redstone233.test.items.ModItems;

public class InfoItem extends Item {


    public InfoItem(Settings settings) {
        super(settings);
    }

    public static AttributeModifiersComponent createAttributeModifiers() {
        return AttributeModifiersComponent.builder()
                .add(EntityAttributes.ATTACK_DAMAGE,
                        new EntityAttributeModifier(BASE_ATTACK_DAMAGE_MODIFIER_ID, 6.0f,
                                EntityAttributeModifier.Operation.ADD_VALUE),
                        AttributeModifierSlot.MAINHAND)
                .add(EntityAttributes.ATTACK_SPEED,
                        new EntityAttributeModifier(BASE_ATTACK_SPEED_MODIFIER_ID, 11.0F,
                                EntityAttributeModifier.Operation.ADD_VALUE),
                        AttributeModifierSlot.MAINHAND)
                .build();
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (world.isClient) {
            client.setScreen(new AnnouncementScreen());
        }
        return ActionResult.SUCCESS;
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        PlayerEntity player = context.getPlayer();
        if (!world.isClient) {
            world.createExplosion(player,
                    context.getBlockPos().getX(),
                    context.getBlockPos().getY(),
                    context.getBlockPos().getZ(),
                    5.0f,
                    true,
                    World.ExplosionSourceType.TNT
            );
           spawnChargingParticles(player, 300);
        }


        return ActionResult.SUCCESS;
    }
    private void spawnChargingParticles(PlayerEntity player, int time) {
        for (int i = 0; i < time; i++) {
            double offsetX = player.getRandom().nextGaussian() * 0.5;
            double offsetY = player.getRandom().nextGaussian() * 0.5 + 1;
            double offsetZ = player.getRandom().nextGaussian() * 0.5;

            player.getWorld().addParticleClient(
                    ParticleTypes.SNOWFLAKE,
                    player.getX() + offsetX,
                    player.getY() + offsetY,
                    player.getZ() + offsetZ,
                    0, 0.1, 0
            );
        }
    }
}
