package net.redstone233.test;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.redstone233.test.data.*;

public class TestModDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

		pack.addProvider(ModItemTagsProvider::new);
		pack.addProvider(ModBlockTagsProvider::new);
		pack.addProvider(ModModelsProvider::new);
		pack.addProvider(ModEnglishLanguageProvider::new);
		pack.addProvider(ModLootTableGenerator::new);
		pack.addProvider(ModRecipesProvider::new);
	}
}
