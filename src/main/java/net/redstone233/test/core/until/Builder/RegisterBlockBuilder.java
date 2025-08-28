package net.redstone233.test.core.until.Builder;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.redstone233.test.TestMod;

import java.util.function.Function;

public class RegisterBlockBuilder {
    private final String id;
    private Function<AbstractBlock.Settings, Block> factory = Block::new;
    private AbstractBlock.Settings settings;
    private boolean registerItem = true;

    private RegisterBlockBuilder(String id) {
        this.id = id;
    }

    public static RegisterBlockBuilder create(String id) {
        return new RegisterBlockBuilder(id);
    }

    public RegisterBlockBuilder factory(Function<AbstractBlock.Settings, Block> factory) {
        this.factory = factory;
        return this;
    }

    public RegisterBlockBuilder settings(AbstractBlock.Settings settings) {
        this.settings = settings;
        return this;
    }

    public RegisterBlockBuilder registerItem(boolean registerItem) {
        this.registerItem = registerItem;
        return this;
    }

    public Block register() {
        final RegistryKey<Block> registryKey = RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(TestMod.MOD_ID, id));
        final Block block = net.minecraft.block.Blocks.register(registryKey, factory, settings);

        if (registerItem) {
            Items.register(block);
        }

        return block;
    }
}
