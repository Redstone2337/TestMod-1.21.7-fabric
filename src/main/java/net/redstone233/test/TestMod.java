package net.redstone233.test;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.redstone233.test.blocks.ModBlockFamilies;
import net.redstone233.test.blocks.ModBlocks;
import net.redstone233.test.core.commands.AcquireModItemStackCommand;
import net.redstone233.test.core.commands.InformationCommand;
import net.redstone233.test.core.commands.SetValueCountCommand;
import net.redstone233.test.core.component.ModDataComponentTypes;
import net.redstone233.test.core.food.ModConsumableComponents;
import net.redstone233.test.core.food.ModFoodComponents;
import net.redstone233.test.core.loader.BrewingRecipeLoader;
import net.redstone233.test.core.loot.FreezeSwordLoot;
import net.redstone233.test.core.potion.ModStatusEffects;
import net.redstone233.test.core.tags.ModBlockTags;
import net.redstone233.test.core.tags.ModItemTags;
import net.redstone233.test.core.transaction.CustomTrades;
import net.redstone233.test.core.until.ModToolMaterial;
import net.redstone233.test.core.world.gen.ModWorldGeneration;
import net.redstone233.test.items.ModItemGroups;
import net.redstone233.test.items.ModItems;
import net.redstone233.test.potion.ModPotions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestMod implements ModInitializer {
	public static final String MOD_ID = "mtc";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {

		ModBlocks.init();
		ModItems.init();
		ModItemGroups.registerModItemGroups();
		ModBlockFamilies.init();
		ModBlockTags.init();
		ModItemTags.init();
		ModConsumableComponents.init();
		ModFoodComponents.init();
		ModToolMaterial.register();
		ModWorldGeneration.generateModWorldGen();
		FreezeSwordLoot.init();
		BrewingRecipeLoader.register();
		ModPotions.init();
		ModStatusEffects.init();
		CustomTrades.init();
        ModDataComponentTypes.init();
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		CommandRegistrationCallback.EVENT.register(
				(commandDispatcher, commandRegistryAccess, registrationEnvironment) -> {
					commandDispatcher.register(SetValueCountCommand.register());
					commandDispatcher.register(AcquireModItemStackCommand.register());
                    commandDispatcher.register(InformationCommand.register());
				}
		);

    }
}