package net.redstone233.test;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.redstone233.test.items.custom.FreezeSwordItem;

public class TestModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HudElementRegistry.addLast(Identifier.of(TestMod.MOD_ID, "freeze_hud"), (context, tickCounter) -> {
            PlayerEntity player = MinecraftClient.getInstance().player;
            if (player == null) {
                FreezeSwordItem.renderChargeIndicator(context,player);
            }
        });
    }
}
