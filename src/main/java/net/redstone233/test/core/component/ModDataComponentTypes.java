package net.redstone233.test.core.component;

import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.redstone233.test.TestMod;
import net.redstone233.test.core.component.type.FreezingSwordComponent;
import net.redstone233.test.core.component.type.HerbalTeaComponent;

import java.util.function.UnaryOperator;

public class ModDataComponentTypes {

    // 在DataComponentTypes.java中添加
    public static final ComponentType<FreezingSwordComponent> FREEZING_SWORD = register(
            "freezing_sword",
            builder -> builder.codec(FreezingSwordComponent.CODEC)
                    .packetCodec(FreezingSwordComponent.PACKET_CODEC)
    );

    public static final ComponentType<HerbalTeaComponent> HE_QI_ZHENG = register(
            "herbal_tea",
            herbalTeaComponentBuilder -> herbalTeaComponentBuilder.codec(HerbalTeaComponent.CODEC)
    );

    private static <T> ComponentType<T> register(String id, UnaryOperator<ComponentType.Builder<T>> builderOperator) {
        return Registry.register(Registries.DATA_COMPONENT_TYPE, Identifier.of(TestMod.MOD_ID,id), ((ComponentType.Builder)builderOperator.apply(ComponentType.builder())).build());
    }
}
