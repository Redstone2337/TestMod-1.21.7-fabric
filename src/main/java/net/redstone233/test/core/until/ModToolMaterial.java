package net.redstone233.test.core.until;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.component.type.ToolComponent;
import net.minecraft.component.type.WeaponComponent;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.Item;
import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;
import net.redstone233.test.TestMod;
import net.redstone233.test.core.tags.ModBlockTags;
import net.redstone233.test.core.tags.ModItemTags;

import java.util.List;

public record ModToolMaterial(
        TagKey<Block> incorrectBlocksForDrops, int durability, float speed, float attackDamageBonus, int enchantmentValue, TagKey<Item> repairItems
) {
    public static final ToolMaterial SILICON = new ToolMaterial(ModBlockTags.INCORRECT_FOR_SILICON_TOOL,4000,8.0F,10.0F,15,ModItemTags.SILICON_TOOL_MATERIALS);

public ModToolMaterial(TagKey<Block> incorrectBlocksForDrops, int durability, float speed, float attackDamageBonus, int enchantmentValue, TagKey<Item> repairItems
) {
    this.incorrectBlocksForDrops = incorrectBlocksForDrops;
    this.durability = durability;
    this.speed = speed;
    this.attackDamageBonus = attackDamageBonus;
    this.enchantmentValue = enchantmentValue;
    this.repairItems = repairItems;
}

    private Item.Settings applyBaseSettings(Item.Settings settings) {
        return settings.maxDamage(this.durability).repairable(this.repairItems).enchantable(this.enchantmentValue);
    }

    public Item.Settings applyToolSettings(
            Item.Settings settings, TagKey<Block> effectiveBlocks, float attackDamage, float attackSpeed, float disableBlockingForSeconds
    ) {
        RegistryEntryLookup<Block> registryEntryLookup = Registries.createEntryLookup(Registries.BLOCK);
        return this.applyBaseSettings(settings)
                .component(
                        DataComponentTypes.TOOL,
                        new ToolComponent(
                                List.of(
                                        ToolComponent.Rule.ofNeverDropping(registryEntryLookup.getOrThrow(this.incorrectBlocksForDrops)),
                                        ToolComponent.Rule.ofAlwaysDropping(registryEntryLookup.getOrThrow(effectiveBlocks), this.speed)
                                ),
                                1.0F,
                                1,
                                true
                        )
                )
                .attributeModifiers(this.createToolAttributeModifiers(attackDamage, attackSpeed))
                .component(DataComponentTypes.WEAPON, new WeaponComponent(2, disableBlockingForSeconds));
    }

    private AttributeModifiersComponent createToolAttributeModifiers(float attackDamage, float attackSpeed) {
        return AttributeModifiersComponent.builder()
                .add(
                        EntityAttributes.ATTACK_DAMAGE,
                        new EntityAttributeModifier(Item.BASE_ATTACK_DAMAGE_MODIFIER_ID, attackDamage + this.attackDamageBonus, EntityAttributeModifier.Operation.ADD_VALUE),
                        AttributeModifierSlot.MAINHAND
                )
                .add(
                        EntityAttributes.ATTACK_SPEED,
                        new EntityAttributeModifier(Item.BASE_ATTACK_SPEED_MODIFIER_ID, attackSpeed, EntityAttributeModifier.Operation.ADD_VALUE),
                        AttributeModifierSlot.MAINHAND
                )
                .build();
    }

    public Item.Settings applySwordSettings(Item.Settings settings, float attackDamage, float attackSpeed) {
        RegistryEntryLookup<Block> registryEntryLookup = Registries.createEntryLookup(Registries.BLOCK);
        return this.applyBaseSettings(settings)
                .component(
                        DataComponentTypes.TOOL,
                        new ToolComponent(
                                List.of(
                                        ToolComponent.Rule.ofAlwaysDropping(RegistryEntryList.of(Blocks.COBWEB.getRegistryEntry()), 15.0F),
                                        ToolComponent.Rule.of(registryEntryLookup.getOrThrow(BlockTags.SWORD_INSTANTLY_MINES), Float.MAX_VALUE),
                                        ToolComponent.Rule.of(registryEntryLookup.getOrThrow(BlockTags.SWORD_EFFICIENT), 1.5F)
                                ),
                                1.0F,
                                2,
                                false
                        )
                )
                .attributeModifiers(this.createSwordAttributeModifiers(attackDamage, attackSpeed))
                .component(DataComponentTypes.WEAPON, new WeaponComponent(1));
    }

    private AttributeModifiersComponent createSwordAttributeModifiers(float attackDamage, float attackSpeed) {
        return AttributeModifiersComponent.builder()
                .add(
                        EntityAttributes.ATTACK_DAMAGE,
                        new EntityAttributeModifier(Item.BASE_ATTACK_DAMAGE_MODIFIER_ID, attackDamage + this.attackDamageBonus, EntityAttributeModifier.Operation.ADD_VALUE),
                        AttributeModifierSlot.MAINHAND
                )
                .add(
                        EntityAttributes.ATTACK_SPEED,
                        new EntityAttributeModifier(Item.BASE_ATTACK_SPEED_MODIFIER_ID, attackSpeed, EntityAttributeModifier.Operation.ADD_VALUE),
                        AttributeModifierSlot.MAINHAND
                )
                .build();
    }

    public static void register() {
        TestMod.LOGGER.info("自定义属性完成！");
    }
}
