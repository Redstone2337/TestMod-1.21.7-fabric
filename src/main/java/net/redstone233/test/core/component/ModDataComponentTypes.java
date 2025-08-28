package net.redstone233.test.core.component;

import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.redstone233.test.TestMod;
import net.redstone233.test.core.component.type.*;

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

    public static final ComponentType<DeliciousBlackGarlicComponent> DELICIOUS_BLACK_GARLIC = register(
            "black_garlic",
            deliciousBlackGarlicComponentBuilder -> deliciousBlackGarlicComponentBuilder.codec(DeliciousBlackGarlicComponent.CODEC)
    );

    public static final ComponentType<InfoItemComponent> INFO_ITEM = register(
            "info_item",
            infoItemComponentBuilder -> infoItemComponentBuilder.codec(InfoItemComponent.CODEC)
    );

    public static final ComponentType<NeoForgeItemComponent> NEO_FORGE_ITEM = register(
            "neo_forge_item",
            neoForgeItemComponentBuilder -> neoForgeItemComponentBuilder.codec(NeoForgeItemComponent.CODEC)
    );

    public static void init() {
        TestMod.LOGGER.info("数据组件类型注册成功！");
    }

    private static <T> ComponentType<T> register(String id, UnaryOperator<ComponentType.Builder<T>> builderOperator) {
        return Registry.register(Registries.DATA_COMPONENT_TYPE, Identifier.of(TestMod.MOD_ID,id), ((ComponentType.Builder)builderOperator.apply(ComponentType.builder())).build());
    }
}
