package net.redstone233.test.items.custom;

import net.fabricmc.fabric.api.item.v1.ComponentTooltipAppenderRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ToolComponent;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.tooltip.TooltipAppender;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.redstone233.test.TestMod;
import net.redstone233.test.core.component.FreezingSwordComponent;
import net.redstone233.test.core.component.ModComponentTypes;
import net.redstone233.test.core.until.FreezeHelper;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FreezeSwordItem extends Item {
    private static final int CHARGE_TIME = 40; // 2秒蓄力 (20 ticks/秒)
    private static final Identifier CHARGE_INDICATOR = Identifier.of(TestMod.MOD_ID, "textures/gui/charge_indicator.png");
    private int chargeProgress = 0;
    private boolean isCharging = false;


    public FreezeSwordItem(ToolMaterial material, float attackDamage, float attackSpeed, Item.Settings settings) {
        super(settings.sword(material, attackDamage, attackSpeed));
    }

    @Override
    public void postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        FreezeHelper.freezeEntity(target, 60);
        super.postHit(stack, target, attacker);
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient() && hand == Hand.MAIN_HAND) {
            ItemStack stack = user.getStackInHand(hand);
            stack.set(FreezingSwordComponent.TYPE,
                    new FreezingSwordComponent(0, true));
            user.sendMessage(Text.translatable("item.freeze.freezing_sword.charging"), true);
        }
        return super.use(world, user, hand);
    }

    @Override
    public void inventoryTick(ItemStack stack, ServerWorld world, Entity entity, @Nullable EquipmentSlot slot) {
        if (world instanceof ServerWorld && entity instanceof PlayerEntity player) {
            FreezingSwordComponent component = stack.getOrDefault(FreezingSwordComponent.TYPE,
                    new FreezingSwordComponent(0, false));

            boolean shouldCharge = slot == EquipmentSlot.MAINHAND && component.isCharging();
            int newProgress = shouldCharge ? component.chargeProgress() + 1 : 0;

            if (newProgress >= CHARGE_TIME) {
                newProgress = CHARGE_TIME;
                if (world.getTime() % 10 == 0) {
                    player.sendMessage(Text.translatable("item.freeze.freezing_sword.charged"), true);
                }
            }

            stack.set(FreezingSwordComponent.TYPE,
                    new FreezingSwordComponent(newProgress, shouldCharge));
        }
        super.inventoryTick(stack, world, entity, slot);
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        if (!user.getWorld().isClient() && hand == Hand.MAIN_HAND) {
            FreezingSwordComponent component = stack.get(FreezingSwordComponent.TYPE);
            if (component != null && component.chargeProgress() >= CHARGE_TIME) {
                entity.damage((ServerWorld) user.getWorld(), user.getDamageSources().playerAttack(user), 20.0F);
                FreezeHelper.freezeEntity(entity, 200);

                stack.set(FreezingSwordComponent.TYPE,
                        new FreezingSwordComponent(0, false));
                user.sendMessage(Text.translatable("item.freeze.freezing_sword.power_attack"), true);

                return ActionResult.SUCCESS;
            }
        }
        return ActionResult.PASS;
    }


    // 客户端渲染方法 - 在物品栏上方显示进度条
    public static void renderChargeIndicator(DrawContext context, PlayerEntity player) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null) return;

        ItemStack mainHandStack = player.getMainHandStack();
        if (mainHandStack.getItem() instanceof FreezeSwordItem sword) {
            if (sword.isCharging) {
                int width = 100;
                int height = 10;
                int x = (client.getWindow().getScaledWidth() - width) / 2;
                int y = client.getWindow().getScaledHeight() - 60;

                float chargePercent = MathHelper.clamp((float) sword.chargeProgress / CHARGE_TIME, 0, 1);
                int filledWidth = (int) (width * chargePercent);

                // 绘制背景
                context.fill(x, y, x + width, y + height, 0x80000000);
                // 绘制进度条
                context.fill(x, y, x + filledWidth, y + height, 0x8000FF00);
                // 绘制边框
                context.drawBorder(x, y, width, height, 0xFFFFFFFF);

                // 绘制文本
                String text = String.format("Charging: %.0f%%", chargePercent * 100);
                context.drawCenteredTextWithShadow(
                        client.textRenderer,
                        Text.literal(text),
                        client.getWindow().getScaledWidth() / 2,
                        y - 12,
                        0xFFFFFF
                );
            }
        }
    }

    // 注册组件
    public static void registerComponent() {
//        Registry.register(Registries.DATA_COMPONENT_TYPE,
//                Identifier.of(TestMod.MOD_ID, "freezing_sword"),
//                FreezingSwordComponent.TYPE);

        ComponentTooltipAppenderRegistry.addAfter(
                DataComponentTypes.DAMAGE,
                FreezingSwordComponent.TYPE
        );
    }
}


