package net.redstone233.test.items.custom;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.passive.FoxEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class NeoForgeItem extends Item {
    public NeoForgeItem(Settings settings) {
        super(settings);
    }

    public static AttributeModifiersComponent createAttributeModifiers() {
        return AttributeModifiersComponent.builder()
                .add(
                        EntityAttributes.ATTACK_DAMAGE,
                        new EntityAttributeModifier(BASE_ATTACK_DAMAGE_MODIFIER_ID, 40, EntityAttributeModifier.Operation.ADD_VALUE),
                        AttributeModifierSlot.MAINHAND
                )
                .add(
                        EntityAttributes.ATTACK_SPEED,
                        new EntityAttributeModifier(BASE_ATTACK_SPEED_MODIFIER_ID, 10.0f, EntityAttributeModifier.Operation.ADD_VALUE),
                        AttributeModifierSlot.MAINHAND
                )
                .build();
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        if (user instanceof ServerPlayerEntity player) {
            if (player.getStackInHand(hand).getItem() == this && !world.isClient) {
                Vec3d playerPos = player.getPos();
                if (Screen.hasShiftDown()) {
                    Box schoolBox = new Box(
                            playerPos.x - 10, playerPos.y - 10, playerPos.z - 10,
                            playerPos.x + 10, playerPos.y + 10, playerPos.z + 10
                    );

                    List<ChickenEntity> chickenEntitiesInRange = world.getEntitiesByClass(ChickenEntity.class, schoolBox, chicken -> true);
                    for (ChickenEntity chicken : chickenEntitiesInRange) {
                        chicken.teleport(playerPos.x, playerPos.y, playerPos.z, false);
                    }

                    List<PlayerEntity> playerEntitiesInRange = world.getEntitiesByClass(PlayerEntity.class, schoolBox, p -> p == player);
                    for (PlayerEntity otherPlayer : playerEntitiesInRange) {
//                        otherPlayer.teleport(playerPos.x, playerPos.y, playerPos.z, false);
                        otherPlayer.addStatusEffect(
                                new StatusEffectInstance(
                                        StatusEffects.RESISTANCE,
                                        400,
                                        1,
                                        false,
                                        true
                                )
                        );
                    }

                    List<MobEntity> mobEntitiesInRange = world.getEntitiesByClass(MobEntity.class, schoolBox, mob -> mob instanceof Monster);
                    for (MobEntity mob : mobEntitiesInRange) {
                        mob.setOnFireFor(200);
                        mob.setFireTicks(200);
                        mob.setOnFire(true);
                    }

                } else {
                    Box schoolBox = new Box(
                            playerPos.x - 5, playerPos.y - 5, playerPos.z - 5,
                            playerPos.x + 5, playerPos.y + 5, playerPos.z + 5
                    );

                    List<ChickenEntity> chickenEntitiesInRange = world.getEntitiesByClass(ChickenEntity.class, schoolBox, chicken -> true);
                    for (ChickenEntity chicken : chickenEntitiesInRange) {
                        chicken.teleport(playerPos.x, playerPos.y, playerPos.z, false);
                    }

                    List<PlayerEntity> playerEntitiesInRange = world.getEntitiesByClass(PlayerEntity.class, schoolBox, p -> p == player);
                    for (PlayerEntity otherPlayer : playerEntitiesInRange) {
                        otherPlayer.addStatusEffect(
                                new StatusEffectInstance(
                                        StatusEffects.RESISTANCE,
                                        200,
                                        1,
                                        false,
                                        true
                                )
                        );
                    }

                    List<MobEntity> mobEntitiesInRange = world.getEntitiesByClass(MobEntity.class, schoolBox, mob -> mob instanceof Monster);
                    for (MobEntity mob : mobEntitiesInRange) {
                        mob.setOnFireFor(100);
                        mob.setFireTicks(100);
                        mob.setOnFire(true);
                    }
                }

            }
        }
        return ActionResult.SUCCESS;
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        if (user instanceof ServerPlayerEntity player) {
            World world = player.getWorld();
            FoxEntity fox = EntityType.FOX.create(world, SpawnReason.SPAWN_ITEM_USE);
            if (stack.getItem() == this && !world.isClient) {
                world.spawnEntity(fox);
            }
        }
        return ActionResult.SUCCESS;
    }
}
