package net.redstone233.test.core.potion;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.redstone233.test.TestMod;
import net.redstone233.test.core.potion.status.TestStatusEffect;

public class ModStatusEffects {

    public static final RegistryEntry<StatusEffect> TEST_STATUS_EFFECT = register("test_status_effect",
            new TestStatusEffect(StatusEffectCategory.BENEFICIAL, 0x696969)
    );

    private static RegistryEntry<StatusEffect> register(String id, StatusEffect statusEffect) {
        return Registry.registerReference(Registries.STATUS_EFFECT, Identifier.of(TestMod.MOD_ID,id), statusEffect);
    }

    public static void init() {
        TestMod.LOGGER.info("注册状态效果成功");
    }
}
