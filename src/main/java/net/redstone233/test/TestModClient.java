package net.redstone233.test;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.TooltipComponentCallback;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.fabricmc.fabric.api.item.v1.ComponentTooltipAppenderRegistry;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.redstone233.test.client.hud.FreezeSwordHud;
import net.redstone233.test.client.tooltip.FreezeSwordTooltipComponent;
import net.redstone233.test.core.component.FreezingSwordComponent;
import net.redstone233.test.core.component.type.ModDataComponentTypes;
import net.redstone233.test.core.until.ModKeys;
import net.redstone233.test.items.custom.FreezeSwordItem;

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

        //ComponentTooltipAppenderRegistry.addLast(ModDataComponentTypes.FREEZING_SWORD);
        ComponentTooltipAppenderRegistry.addAfter(DataComponentTypes.LORE, ModDataComponentTypes.FREEZING_SWORD);

        // 注册HUD渲染
/*        HudElementRegistry.addLast(Identifier.of(TestMod.MOD_ID, "freeze_hud"), (context, tickCounter) -> {
            FreezeSwordHud.render(context);
        });
*/

/*
HudElementRegistry.attachElementBefore(VanillaHudElements.MISC_OVERLAYS, Identifier.of(TestMod.MOD_ID, "freeze_hud"), (context, tickCounter) -> { 	
			FreezeSwordHud.render(context);
 });
*/

/*
HudElementRegistry.attachElementBefore(VanillaHudElements.SUBTITLES, Identifier.of(TestMod.MOD_ID, "freeze_hud"), (context, tickCounter) -> { 	
			FreezeSwordHud.render(context);
 });
*/


/*
HudElementRegistry.attachElementBefore(VanillaHudElements.HOTBAR, Identifier.of(TestMod.MOD_ID, "freeze_hud"), (context, tickCounter) -> { 	
			FreezeSwordHud.render(context);
 });
*/

	    /*
HudElementRegistry.attachElementBefore(VanillaHudElements.HELD_ITEM_TOOLTIP, Identifier.of(TestMod.MOD_ID, "freeze_hud"), (context, tickCounter) -> {         
                        FreezeSwordHud.render(context);
 });
	    */

	    
//HudElementRegistry.attachElementBefore/*attachElementAfter*/(VanillaHudElements.SLEEP, Identifier.of(TestMod.MOD_ID, "freeze_hud"), (context, tickCounter) -> { 	
			//FreezeSwordHud.render(context);
 //});

	FreezeSwordHud.register();

ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;
            
            // 只检测主手物品
            ItemStack mainHand = client.player.getMainHandStack();
            if (mainHand.getItem() instanceof FreezeSwordItem) {
                FreezingSwordComponent component = mainHand.get(ModDataComponentTypes.FREEZING_SWORD);
                if (component != null) {
                    FreezeSwordHud.updateState(
                        component.charges(),
                        component.chargeProgress(),
                        component.isCharging()
                    );
                }
            }
        });

/*
HudElementRegistry.attachElementBefore(VanillaHudElements.TITLE_AND_SUBTITLE, Identifier.of(TestMod.MOD_ID, "freeze_hud"), (context, tickCounter) -> { 	
			FreezeSwordHud.render(context);
 });
*/

        ModKeys.register(); // 注册键位
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player != null) {
                FreezeSwordItem.handleKeyInput(client.player);
            }
        });
    }
}
