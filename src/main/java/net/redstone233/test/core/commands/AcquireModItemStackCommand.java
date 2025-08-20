package net.redstone233.test.core.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.redstone233.test.blocks.ModBlocks;
import net.redstone233.test.items.ModItems;

import java.util.Objects;

public class AcquireModItemStackCommand {
    public static LiteralArgumentBuilder<ServerCommandSource> register() {
        return CommandManager.literal("acquire")
                .requires(source -> source.hasPermissionLevel(2))
                .then(CommandManager.literal("modItems")
                        .executes(run -> giveModItems(
                                        run.getSource()
                                )
                        )
                )
            .then(CommandManager.literal("modBlocks")
                                .executes(run -> giveModBlocks(
                                    run.getSource()
                                )
                            )
                        );
    }

    private static int giveModItems(ServerCommandSource source) {
        PlayerEntity player = source.getPlayer();
        if (player != null && !(player.getInventory().isEmpty()) && source instanceof ServerCommandSource) {
            player.getInventory().insertStack(new ItemStack(ModItems.SILICON));
            player.getInventory().insertStack(new ItemStack(ModItems.SILICON_INGOT));
            player.getInventory().insertStack(new ItemStack(ModItems.RAW_SILICON));
            player.getInventory().insertStack(new ItemStack(ModItems.HE_QI_ZHENG));
            player.getInventory().insertStack(new ItemStack(ModItems.FREEZE_SWORD));
            player.getInventory().insertStack(new ItemStack(ModItems.DELICIOUS_BLACK_GARLIC));
            player.getInventory().insertStack(new ItemStack(ModItems.INFO_ITEM));
            source.sendFeedback(() -> Text.literal("执行成功，已将模组物品给予玩家:").formatted(Formatting.GREEN, Formatting.BOLD)
                    .append(player.getName()).formatted(Formatting.BLUE, Formatting.BOLD), true);
            return 1;
        } else {
            source.sendError(Text.literal("执行失败，未检测到玩家！").formatted(Formatting.RED,Formatting.BOLD));
            return 0;
        }
    }

    private static int giveModBlocks(ServerCommandSource source) {
        PlayerEntity player = source.getPlayer();
        if (player != null && !(player.getInventory().isEmpty()) && source instanceof ServerCommandSource) {
            player.getInventory().insertStack(new ItemStack(ModBlocks.SILICON_BLOCK));
            player.getInventory().insertStack(new ItemStack(ModBlocks.RAW_SILICON_BLOCK));
            player.getInventory().insertStack(new ItemStack(ModBlocks.SILICON_WALL));
            player.getInventory().insertStack(new ItemStack(ModBlocks.SILICON_PRESSURE_PLATE));
            player.getInventory().insertStack(new ItemStack(ModBlocks.SILICON_BLOCK_SLAB));
            player.getInventory().insertStack(new ItemStack(ModBlocks.SILICON_BLOCK_STAIRS));
            player.getInventory().insertStack(new ItemStack(ModBlocks.SILICON_BUTTON));
            player.getInventory().insertStack(new ItemStack(ModBlocks.SILICON_FENCE));
            player.getInventory().insertStack(new ItemStack(ModBlocks.SILICON_FENCE_GATE));
            player.getInventory().insertStack(new ItemStack(ModBlocks.SILICON_ORE));
            player.getInventory().insertStack(new ItemStack(ModBlocks.SILICON_DOOR));
            player.getInventory().insertStack(new ItemStack(ModBlocks.SILICON_TRAPDOOR));
            player.getInventory().insertStack(new ItemStack(ModBlocks.DEEPSLATE_SILICON_ORE));
            source.sendFeedback(() -> Text.literal("执行成功，已将模组方块给予玩家:").formatted(Formatting.GREEN, Formatting.BOLD)
                    .append(player.getName()).formatted(Formatting.BLUE, Formatting.BOLD), true);
            return 1;
        } else {
            source.sendError(Text.literal("执行失败，未检测到玩家！").formatted(Formatting.RED,Formatting.BOLD));
            return 0;
        }
    }
}
