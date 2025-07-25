package net.redstone233.test.mixin;


import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.redstone233.test.core.commands.SetValueCountCommand;
import net.redstone233.test.core.tags.ModItemTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class ItemStackMaxCountMixin {

    @Inject(method = "getMaxCount", at = @At("RETURN"), cancellable = true)
    private void modifyItemStackSize(CallbackInfoReturnable<Integer> cir) {
        ItemStack item = (ItemStack) (Object)this;
        if (item.isIn(ModItemTags.MOD_TAGS)) {
            cir.setReturnValue(SetValueCountCommand.getCustomMaxSize());
        }
    }
}
