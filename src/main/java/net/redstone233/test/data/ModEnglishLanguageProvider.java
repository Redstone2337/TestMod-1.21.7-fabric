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
        translationBuilder.add(ModItems.HE_QI_ZHENG, "Chinese Herb Tea");
        translationBuilder.add(ModItems.DELICIOUS_BLACK_GARLIC, "\"Delicious\" Black Garlic");
        translationBuilder.add(ModItems.INFO_ITEM, "Screen Info");
        translationBuilder.add(ModItems.BLUE_CHEESE, "Blue Cheese");
        translationBuilder.add(ModItems.FABRIC_BOW, "Fabric Bow");
        translationBuilder.add(ModItems.FORGE_MACE, "Forge Mace");
        translationBuilder.add(ModItems.NEOFORGE_ITEM, "NeoForge");
        translationBuilder.add(ModItems.QUILT_ITEM, "Quilt");

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

        translationBuilder.add("effect.mtc.test_status_effect","Herbal Tea Effect");
        translationBuilder.add("item.minecraft.potion.effect.herbal_tea","Potion of Herbal Tea");
        translationBuilder.add("item.minecraft.lingering_potion.effect.herbal_tea", "Lingering Potion of Herbal Tea");
        translationBuilder.add("item.minecraft.splash_potion.effect.herbal_tea", "Splash Potion of Herbal Tea");

        translationBuilder.add("item.minecraft.potion.effect.long_herbal_tea","Potion of Herbal Tea Ⅱ");
        translationBuilder.add("item.minecraft.lingering_potion.effect.long_herbal_tea", "Lingering Potion of Herbal Tea Ⅱ");
        translationBuilder.add("item.minecraft.splash_potion.effect.long_herbal_tea", "Splash Potion of Herbal Tea Ⅱ");


        // Creative Tabs
        translationBuilder.add("itemGroup.mtc.silicon_items", "Silicon | Items");
        translationBuilder.add("itemGroup.mtc.silicon_build", "Silicon | Build Blocks");
        translationBuilder.add("itemGroup.mtc.silicon_futures", "Silicon | Futures Blocks");

        // Freeze Sword Tooltips
        translationBuilder.add("tooltip.freezing_sword.desc", "A legendary sword that freezes enemies with each hit.");
        translationBuilder.add("tooltip.freezing_sword.charge", "Freeze Charge:");
        translationBuilder.add("tooltip.freezing_sword.charge_instruction", "Press %s to charge, then left-click for massive damage!");
        translationBuilder.add("tooltip.freezing_sword.charging", "Charging: %d%%");
        translationBuilder.add("tooltip.freezing_sword.ready", "READY! Left-click to unleash power!");
        translationBuilder.add("tooltip.freezing_sword.damage.normal", "Base Damage: §a+%.1f");
        translationBuilder.add("tooltip.freezing_sword.damage.charged", "Charged vs Bosses: §c+%.1f");
        translationBuilder.add("tooltip.freezing_sword.damage.non_boss", "Charged vs Others: §d+%.1f");
        translationBuilder.add("tooltip.freezing_sword.loot_only", "There is a chance to get it in the relic treasure chest.");
        translationBuilder.add("tooltip.freezing_sword.max_charges", "MAX CHARGES REACHED!");

        // Freeze Sword In-Game Messages
        translationBuilder.add("msg.freezesword.start_charging", "§b[Freeze Sword]§r Charging started! Hold [%s] to continue...");
        translationBuilder.add("msg.freezesword.charge_canceled", "§7[Freeze Sword]§r Charge canceled (released [%s])");
        translationBuilder.add("msg.freezesword.charging_progress", "§3[Freeze Sword]§r Charge Progress: %s%% (%s seconds remaining)");
        translationBuilder.add("msg.freezesword.fully_charged", "§a[Freeze Sword]§r Fully Charged!");
        translationBuilder.add("msg.freezesword.boss_hit", "§6[Freeze Sword]§r Critical hit on Boss! Charges[%d/%d] Damage +%.0f");
        translationBuilder.add("msg.freezesword.invalid_target", "§c[Freeze Sword]§r Invalid target! Charges[%d/%d] Damage +%.0f");
        translationBuilder.add("msg.freezesword.charge_added", "§b[Freeze Sword]§r Charge added! Current: %d/%d");
        translationBuilder.add("msg.freezesword.max_charges", "§a[Freeze Sword]§r MAX CHARGES REACHED!");
        translationBuilder.add("msg.freezesword.hit_with_charges", "§e[Freeze Sword]§r Attack with %d charges!");

        translationBuilder.add("msg.freezesword.charge_start", "§b[Freeze Sword]§r Charging started!");

        translationBuilder.add("msg.freezesword.charge_progress", "§b[Freeze Sword]§r%s");

        translationBuilder.add("msg.freezesword.max_charges_hud", "MAX CHARGES READY!");

        translationBuilder.add("msg.freezesword.boss_target","§6[Freeze Sword]§r Found boss target damage increase!");

        // HUD Messages
        translationBuilder.add("msg.freezesword.hud_charge", "Charge: [%s]");
        translationBuilder.add("msg.freezesword.progress_value", "Progress[");
        translationBuilder.add("msg.freezesword.charges_value", "] Charges[");
        translationBuilder.add("msg.freezesword.damage_value", "] Damage ");
        translationBuilder.add("msg.freezesword.damage_plus", "Damage +");

        // Item Status
        translationBuilder.add("item.freeze.freezing_sword.charging", "Charging: %s%%");
        translationBuilder.add("tooltip.freezing_sword.charges", "Charging: %s%%");
        translationBuilder.add("item.freeze.freezing_sword.charged", "Freezing energy fully charged!");
        translationBuilder.add("item.freeze.freezing_sword.power_attack", "Unleashed frozen fury!");

        translationBuilder.add("narration.scrollable_text","Scrollable Text：%s");
        translationBuilder.add("narration.scrollable_text.usage","Usage Scrollable Text");

        // Screen Info
        translationBuilder.add("gui.playermod.player_info","Player Information");
        translationBuilder.add("gui.playermod.level", "Level: %d");
        translationBuilder.add("gui.playermod.experience", "Experience: %d/%d");
        translationBuilder.add("gui.playermod.vip_status", "VIP Member");
        translationBuilder.add("gui.playermod.svip_status", "Super VIP Member");
        translationBuilder.add("gui.playermod.non_vip_status", "Regular Player");
        translationBuilder.add("gui.playermod.tooltip.exp_info", "Experience Information");
        translationBuilder.add("gui.playermod.tooltip.exp_remaining", "Remaining for next level: %d");
        translationBuilder.add("gui.playermod.tooltip.exp_multiplier", "Experience multiplier: %sx");
        translationBuilder.add("gui.playermod.tooltip.base_exp", "Base experience required: %d");
        translationBuilder.add("gui.playermod.next_level_exp", "Experience needed for next level: %d");
        translationBuilder.add("gui.close", "Close");

        // Key Bindings
        translationBuilder.add("key.freezesword.charge", "Activate Freeze Sword Charge");
        translationBuilder.add("key.mtc.announcement","Display the announcement.");
        translationBuilder.add("key.mtc.debug","Display debugging.");
        translationBuilder.add("key.mtc.reload", "Reload the configuration.");
        translationBuilder.add("category.freezesword", "Debug Craft Mod");
        translationBuilder.add("key.mtc.open_player_info","Open Player Info Screen");
    }
}
