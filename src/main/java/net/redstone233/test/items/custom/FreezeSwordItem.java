// FreezeSwordItem.java
package net.redstone233.test.items.custom;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.redstone233.test.core.component.FreezingSwordComponent;
import net.redstone233.test.core.component.type.ModDataComponentTypes;
import net.redstone233.test.core.until.FreezeHelper;
import org.jetbrains.annotations.Nullable;

public class FreezeSwordItem extends Item {
    public static final int CHARGE_TIME = 40; // 2秒蓄力

    public FreezeSwordItem(ToolMaterial material, float attackDamage, float attackSpeed, Item.Settings settings) {
        super(settings.sword(material, attackDamage, attackSpeed).component(ModDataComponentTypes.FREEZING_SWORD, FreezingSwordComponent.DEFAULT));
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        stack.set(ModDataComponentTypes.FREEZING_SWORD, new FreezingSwordComponent(0, true));

        if (!world.isClient()) {
            user.sendMessage(Text.translatable("item.freeze.freezing_sword.charging"), true);
        }
        return ActionResult.SUCCESS;
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
                player.sendMessage(Text.translatable("item.freeze.freezing_sword.charged"), true);
            }
        }

        if (newProgress != component.chargeProgress() || shouldCharge != component.isCharging()) {
            stack.set(ModDataComponentTypes.FREEZING_SWORD, new FreezingSwordComponent(newProgress, shouldCharge));
        }
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        if (hand != Hand.MAIN_HAND) return ActionResult.PASS;

        FreezingSwordComponent component = stack.get(ModDataComponentTypes.FREEZING_SWORD);
        if (component != null && component.chargeProgress() >= CHARGE_TIME) {
            entity.damage((ServerWorld) user.getWorld(),user.getDamageSources().playerAttack(user), 20.0F);
            FreezeHelper.freezeEntity(entity, 200); // 10秒冻结

            stack.set(ModDataComponentTypes.FREEZING_SWORD, new FreezingSwordComponent(0, false));
            if (!user.getWorld().isClient()) {
                user.sendMessage(Text.translatable("item.freeze.freezing_sword.power_attack"), true);
            }
            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }

    @Override
    public void postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        FreezeHelper.freezeEntity(target, 60); // 3秒冻结
        super.postHit(stack, target, attacker);
    }
}