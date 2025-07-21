package net.redstone233.test.mixin;


import net.minecraft.item.Item;
import net.redstone233.test.core.commands.SetValueCountCommand;
import net.redstone233.test.core.tags.ModItemTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public abstract class ItemMaxCountMixin {

    @Inject(method = "getMaxCount", at = @At("HEAD"), cancellable = true)
    private void modifyItemStackSize(CallbackInfoReturnable<Integer> cir) {
        Item item = (Item)(Object)this;
        if (item.getDefaultStack().isIn(ModItemTags.MOD_TAGS)) {
            cir.setReturnValue(SetValueCountCommand.getCustomMaxSize());
        }
    }
}
