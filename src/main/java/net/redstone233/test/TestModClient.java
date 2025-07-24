package net.redstone233.test;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.TooltipComponentCallback;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.redstone233.test.client.hud.FreezeSwordHud;
import net.redstone233.test.client.tooltip.FreezeSwordTooltipComponent;

public class TestModClient implements ClientModInitializer {


    @Override
    public void onInitializeClient() {
        // 注册Tooltip组件
        TooltipComponentCallback.EVENT.register(data -> {
            if (data instanceof FreezeSwordTooltipComponent.Data(ItemStack stack)) {
                return new FreezeSwordTooltipComponent(stack);
            }
            return null;
        });

        // 注册HUD渲染
        HudElementRegistry.addLast(Identifier.of(TestMod.MOD_ID, "freeze_hud"), (context, tickCounter) -> {
            FreezeSwordHud.render(context);
        });
    }
}
