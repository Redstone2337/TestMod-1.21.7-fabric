package net.redstone233.test.longpress;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;
import net.redstone233.test.client.hud.FreezeSwordHud;
import net.redstone233.test.core.component.type.FreezingSwordComponent;
import net.redstone233.test.items.custom.FreezeSwordItem;
import net.redstone233.test.core.component.ModDataComponentTypes;

public class FreezeSwordLongPressAction implements LongPressAction {
    private static final long PRESS_TIME = 5000; // 5秒

    @Override
    public void onPressStart(PlayerEntity player, ItemStack stack, long startTime) {
        if (stack == null || !(stack.getItem() instanceof FreezeSwordItem)) return;

        // 获取当前组件状态
        var component = stack.get(ModDataComponentTypes.FREEZING_SWORD);
        int charges = component != null ? component.charges() : 0;

        // 更新组件状态为蓄力中
        stack.set(ModDataComponentTypes.FREEZING_SWORD,
                new FreezingSwordComponent(0, true, charges));

        // 只在服务端发送消息
        if (player.getWorld() instanceof ServerWorld) {
            player.sendMessage(
                    Text.translatable("msg.freezesword.charge_start")
                            .formatted(Formatting.AQUA),
                    true);
        }
    }

    @Override
    public boolean onPressing(PlayerEntity player, ItemStack stack, long pressedTime) {
        if (stack == null || !(stack.getItem() instanceof FreezeSwordItem)) return false;

        // 添加蓄力粒子效果（客户端）
        if (player.getWorld().isClient()) {
            spawnChargingParticles(player, pressedTime);
        }

        // 更新客户端HUD
        updateHudProgress(player, pressedTime);

        return true;
    }

    @Override
    public void onLongPressComplete(PlayerEntity player, ItemStack stack) {
        if (stack == null || !(stack.getItem() instanceof FreezeSwordItem)) return;

        // 获取当前组件状态
        var component = stack.get(ModDataComponentTypes.FREEZING_SWORD);
        if (component == null) return;

        int newCharges = Math.min(component.charges() + 1, FreezeSwordItem.MAX_CHARGES);

        // 更新组件状态
        stack.set(ModDataComponentTypes.FREEZING_SWORD,
                new FreezingSwordComponent(0, false, newCharges));

        // 只在服务端发送消息
        if (player.getWorld() instanceof ServerWorld) {
            player.sendMessage(
                    Text.translatable("msg.freezesword.charge_complete", newCharges)
                            .formatted(Formatting.GREEN),
                    true);
        }
    }

    @Override
    public void onPressInterrupted(PlayerEntity player, ItemStack stack, long pressedTime) {
        if (stack == null || !(stack.getItem() instanceof FreezeSwordItem)) return;

        // 获取当前组件状态
        var component = stack.get(ModDataComponentTypes.FREEZING_SWORD);
        if (component == null) return;

        // 更新组件状态为停止蓄力
        stack.set(ModDataComponentTypes.FREEZING_SWORD,
                new FreezingSwordComponent(component.chargeProgress(), false, component.charges()));

        // 只在服务端发送消息
        if (pressedTime > 500 && player.getWorld() instanceof ServerWorld) {
            player.sendMessage(
                    Text.translatable("msg.freezesword.charge_interrupted")
                            .formatted(Formatting.RED),
                    true);
        }
    }

    @Override
    public long getRequiredPressTime() {
        return PRESS_TIME;
    }

    @Override
    public String getName() {
        return "freeze_sword_charge";
    }

    @Override
    public boolean canUse(PlayerEntity player, ItemStack stack) {
        // 只有手持冻结剑且未满充能时可用
        if (stack == null || !(stack.getItem() instanceof FreezeSwordItem)) return false;

        var component = stack.get(ModDataComponentTypes.FREEZING_SWORD);
        return component != null && !component.isMaxCharges();
    }

    private void spawnChargingParticles(PlayerEntity player, long pressedTime) {
        double progress = MathHelper.clamp((double) pressedTime / PRESS_TIME, 0, 1);
        int particles = (int) (10 * progress);

        for (int i = 0; i < particles; i++) {
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

    private void updateHudProgress(PlayerEntity player, long pressedTime) {
        if (player.getWorld().isClient()) {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client != null && client.player == player) {
                // 更新HUD进度
                float progress = MathHelper.clamp((float) pressedTime / PRESS_TIME, 0, 1);
                FreezeSwordHud.updateProgress(progress);
            }
        }
    }
}