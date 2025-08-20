package net.redstone233.test.items.custom;

import net.minecraft.client.MinecraftClient;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.redstone233.test.core.screen.AnnouncementScreen;
import net.redstone233.test.items.ModItems;

public class InfoItem extends Item {
    private static MinecraftClient client;


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
        if (user.canEquip(ModItems.INFO_ITEM.getDefaultStack(), EquipmentSlot.MAINHAND)) {
            if (client != null) {
                client.setScreen(new AnnouncementScreen());
            }
        }
        return super.use(world, user, hand);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        PlayerEntity player = context.getPlayer();
        double x = context.getBlockPos().getX();
        double y = context.getBlockPos().getY();
        double z = context.getBlockPos().getX();
        if (!world.isClient) {
            world.createExplosion(player,x,y,z,5.0f,true, World.ExplosionSourceType.MOB);
            if (player != null) {
                world.addParticleClient(
                        DustParticleEffect.DEFAULT,
                        player.getX(),
                        player.getY(),
                        player.getZ(),
                        player.getVelocity().getX(),
                        player.getVelocity().getY(),
                        player.getVelocity().getZ()
                );
            }
        }
        return ActionResult.SUCCESS;
    }
}
