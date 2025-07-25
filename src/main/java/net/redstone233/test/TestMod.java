package net.redstone233.test;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.ResourceType;
import net.redstone233.test.blocks.ModBlockFamilies;
import net.redstone233.test.blocks.ModBlocks;
import net.redstone233.test.core.commands.SetValueCountCommand;
import net.redstone233.test.core.tags.ModBlockTags;
import net.redstone233.test.core.tags.ModItemTags;
import net.redstone233.test.core.tags.ModTagReloader;
import net.redstone233.test.core.until.ModToolMaterial;
import net.redstone233.test.core.world.gen.ModWorldGeneration;
import net.redstone233.test.items.ModItemGroups;
import net.redstone233.test.items.ModItems;
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
		ModToolMaterial.register();
		ModWorldGeneration.generateModWorldGen();
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		CommandRegistrationCallback.EVENT.register(
				(commandDispatcher, commandRegistryAccess, registrationEnvironment) -> {
					commandDispatcher.register(SetValueCountCommand.register());
				}
		);

		ResourceManagerHelper.get(ResourceType.SERVER_DATA)
				.registerReloadListener(ModTagReloader.INSTANCE);

		LOGGER.info("Hello Fabric world!");
	}
}