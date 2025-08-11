package net.redstone233.test.potion;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.potion.Potion;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.redstone233.test.TestMod;
import net.redstone233.test.core.potion.ModStatusEffects;

public class ModPotions {

    public static final RegistryEntry<Potion> HE_QI_ZHENG = register(
            "herbal_tea" , new Potion("herbal_tea", new StatusEffectInstance(ModStatusEffects.TEST_STATUS_EFFECT, 300))
    );

    public static final RegistryEntry<Potion> LONG_HE_QI_ZHENG = register(
            "long_herbal_tea" , new Potion("herbal_tea", new StatusEffectInstance(ModStatusEffects.TEST_STATUS_EFFECT, 600, 1, false, true, false))
    );

    private static RegistryEntry<Potion> register(String name, Potion potion) {
        return Registry.registerReference(Registries.POTION, Identifier.of(TestMod.MOD_ID,name), potion);
    }

    public static void init() {
        TestMod.LOGGER.info("药水注册成功");
    }
}
