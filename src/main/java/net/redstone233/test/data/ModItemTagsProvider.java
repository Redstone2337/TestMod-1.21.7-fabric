package net.redstone233.test.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.block.Block;
import net.minecraft.data.tag.ProvidedTagBuilder;
import net.minecraft.data.tag.vanilla.VanillaBlockItemTags;
import net.minecraft.data.tag.vanilla.VanillaItemTagProvider;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.TagKey;
import net.redstone233.test.core.tags.ModBlockTags;
import net.redstone233.test.core.tags.ModItemTags;
import net.redstone233.test.items.ModItems;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModItemTagsProvider extends FabricTagProvider.ItemTagProvider {
    public ModItemTagsProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        valueLookupBuilder(ModItemTags.MOD_TAGS)
                .add(
                        ModItems.RAW_SILICON,
                        ModItems.SILICON,
                        ModItems.SILICON_INGOT,
                        ModItems.HE_QI_ZHENG,
                        ModItems.DELICIOUS_BLACK_GARLIC,
                        ModItems.INFO_ITEM,
                        ModItems.BLUE_CHEESE
                );
        valueLookupBuilder(ModItemTags.SILICON_TOOL_MATERIALS)
                .add(ModItems.SILICON_INGOT);
    }
}
