package net.redstone233.test.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.registry.RegistryWrapper;
import net.redstone233.test.blocks.ModBlocks;
import net.redstone233.test.items.ModItems;

import java.util.concurrent.CompletableFuture;

public class ModEnglishLanguageProvider extends FabricLanguageProvider {
    public ModEnglishLanguageProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generateTranslations(RegistryWrapper.WrapperLookup wrapperLookup, TranslationBuilder translationBuilder) {
        // Items
        translationBuilder.add(ModItems.RAW_SILICON, "Raw Silicon");
        translationBuilder.add(ModItems.SILICON, "Silicon");
        translationBuilder.add(ModItems.SILICON_INGOT, "Silicon Ingot");
        translationBuilder.add(ModItems.FREEZE_SWORD, "Freeze Sword");

        // Blocks
        translationBuilder.add(ModBlocks.SILICON_BLOCK, "Block of Silicon");
        translationBuilder.add(ModBlocks.RAW_SILICON_BLOCK, "Block of Raw Silicon");
        translationBuilder.add(ModBlocks.SILICON_ORE, "Silicon Ore");
        translationBuilder.add(ModBlocks.DEEPSLATE_SILICON_ORE, "Deepslate Silicon Ore");

        // Building Blocks
        translationBuilder.add(ModBlocks.SILICON_BLOCK_STAIRS, "Silicon Block Stairs");
        translationBuilder.add(ModBlocks.SILICON_BLOCK_SLAB, "Silicon Block Slab");
        translationBuilder.add(ModBlocks.SILICON_BUTTON, "Silicon Block Button");
        translationBuilder.add(ModBlocks.SILICON_DOOR, "Silicon Block Door");
        translationBuilder.add(ModBlocks.SILICON_FENCE, "Silicon Block Fence");
        translationBuilder.add(ModBlocks.SILICON_FENCE_GATE, "Silicon Block Fence Gate");
        translationBuilder.add(ModBlocks.SILICON_PRESSURE_PLATE, "Silicon Block Pressure Plate");
        translationBuilder.add(ModBlocks.SILICON_TRAPDOOR, "Silicon Block Trapdoor");
        translationBuilder.add(ModBlocks.SILICON_WALL, "Silicon Block Wall");

        // Creative Tabs
        translationBuilder.add("itemGroup.mtc.silicon_items", "Silicon | Items");
        translationBuilder.add("itemGroup.mtc.silicon_build", "Silicon | Build Blocks");

        // Freeze Sword Tooltips
        translationBuilder.add("tooltip.freezing_sword.desc", "A legendary sword that freezes enemies with each hit.");
        translationBuilder.add("tooltip.freezing_sword.charge", "Freeze Charge:");
        translationBuilder.add("tooltip.freezing_sword.charge_instruction", "Press %s to charge, then left-click for massive damage!");
        translationBuilder.add("tooltip.freezing_sword.charging", "Charging: %d%%");
        translationBuilder.add("tooltip.freezing_sword.ready", "READY! Left-click to unleash power!");
        translationBuilder.add("tooltip.freezing_sword.damage.normal", "Base Damage: §a+%f");
        translationBuilder.add("tooltip.freezing_sword.damage.charged", "Charged vs Bosses: §c+%f");
        translationBuilder.add("tooltip.freezing_sword.damage.non_boss", "Charged vs Others: §d+%f");
        translationBuilder.add("tooltip.freezing_sword.loot_only", "Can be synthesized by Recipe's formula.");

        // Freeze Sword In-Game Messages
        translationBuilder.add("item.freeze.freezing_sword.charging", "Charging freezing energy...");
        translationBuilder.add("item.freeze.freezing_sword.charged", "Freezing energy fully charged!");
        translationBuilder.add("item.freeze.freezing_sword.power_attack", "Unleashed frozen fury!");
//        translationBuilder.add("msg.freezesword.start_charging", "§b[Freeze Sword]§r Charging started...");
        translationBuilder.add("msg.freezesword.charged", "§b[Freeze Sword]§r Fully charged!");
        translationBuilder.add("msg.freezesword.charging_progress", "§3[Freeze Sword]§r Charge Progress: %s%% (%s seconds remaining)");
        translationBuilder.add("msg.freezesword.fully_charged", "§a[Freeze Sword]§r Fully Charged!");
//        translationBuilder.add("msg.freezesword.charge_canceled", "§7[Freeze Sword]§r Charge canceled");
        translationBuilder.add("msg.freezesword.boss_hit", "§6[Freeze Sword]§r Critical hit on Boss!");
        translationBuilder.add("msg.freezesword.invalid_target", "§c[Freeze Sword]§r Invalid target, damage reduced!");
        translationBuilder.add("msg.freezesword.start_charging", "§b[Freeze Sword]§r Charging started! Hold [%s] to continue...");
        translationBuilder.add("msg.freezesword.charge_canceled", "§7[Freeze Sword]§r Charge canceled (released [%s])");

        // Key Bindings
        translationBuilder.add("key.freezesword.charge", "Activate Freeze Sword Charge");
        translationBuilder.add("category.freezesword", "Freeze Sword Mod");
    }
}