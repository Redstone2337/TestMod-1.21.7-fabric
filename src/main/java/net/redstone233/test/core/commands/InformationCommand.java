package net.redstone233.test.core.commands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.redstone233.test.core.api.PlayerDataProvider;
import net.redstone233.test.core.api.impl.CodecPlayerDataProvider;
import net.redstone233.test.core.until.PlayerDataFactory;

import java.util.Optional;

public class InformationCommand {
    public static LiteralArgumentBuilder<ServerCommandSource> register() {
        return CommandManager.literal("tmd")
                .requires(source -> source.hasPermissionLevel(2))
                .then(CommandManager.literal("setlevel")
                        .then(CommandManager.argument("level", IntegerArgumentType.integer(1))
                                .executes(context -> setLevel(context.getSource(), context.getSource().getPlayer(), IntegerArgumentType.getInteger(context, "level")))))
                .then(CommandManager.literal("setexp")
                        .then(CommandManager.argument("exp", IntegerArgumentType.integer(0))
                                .executes(context -> setExp(context.getSource(), context.getSource().getPlayer(), IntegerArgumentType.getInteger(context, "exp")))))
                .then(CommandManager.literal("addexp")
                        .then(CommandManager.argument("amount", IntegerArgumentType.integer(1))
                                .executes(context -> addExp(context.getSource(), context.getSource().getPlayer(), IntegerArgumentType.getInteger(context, "amount")))))
                .then(CommandManager.literal("setvip")
                        .then(CommandManager.argument("vip", BoolArgumentType.bool())
                                .executes(context -> setVip(context.getSource(), context.getSource().getPlayer(), BoolArgumentType.getBool(context, "vip")))))
                .then(CommandManager.literal("setsvip")
                        .then(CommandManager.argument("svip", BoolArgumentType.bool())
                                .executes(context -> setSvip(context.getSource(), context.getSource().getPlayer(), BoolArgumentType.getBool(context, "svip")))))
                .then(CommandManager.literal("setmultiplier")
                        .then(CommandManager.argument("multiplier", DoubleArgumentType.doubleArg(1.0, 10.0))
                                .executes(context -> setMultiplier(context.getSource(), context.getSource().getPlayer(), DoubleArgumentType.getDouble(context, "multiplier")))))
                .then(CommandManager.literal("info")
                        .executes(context -> showInfo(context.getSource(), context.getSource().getPlayer())))
                .then(CommandManager.literal("setmultiplier")
                        .then(CommandManager.argument("multiplier", DoubleArgumentType.doubleArg(1.0, 10.0))
                                .executes(context -> setMultiplierViaFactory(context.getSource(), context.getSource().getPlayer(), DoubleArgumentType.getDouble(context, "multiplier")))))
                .then(CommandManager.literal("reload")
                        .executes(context -> reloadData(context.getSource(), context.getSource().getPlayer())))
                .then(CommandManager.literal("info")
                        .executes(context -> showInfoViaFactory(context.getSource(), context.getSource().getPlayer())));
    }

    private static int setLevel(ServerCommandSource source, ServerPlayerEntity player, int level) {
        if (source instanceof ServerCommandSource && player != null) {
            CodecPlayerDataProvider provider = PlayerDataFactory.getCodecProvider(player);
            provider.setLevel(level);
            source.sendMessage(Text.literal("设置等级为: " + level));
            source.sendFeedback(() -> Text.literal("通过数据提供器设置玩家数据成功！").formatted(Formatting.GREEN, Formatting.BOLD), true);
            return 1;
        } else {
            source.sendError(Text.literal("执行失败，未检测到玩家！").formatted(Formatting.RED, Formatting.BOLD));
            return 0;
        }
    }

    private static int setExp(ServerCommandSource source, ServerPlayerEntity player, int exp) {
        if (source instanceof ServerCommandSource && player != null) {
            CodecPlayerDataProvider provider = PlayerDataFactory.getCodecProvider(player);
            provider.setExperience(exp);
            source.sendMessage(Text.literal("设置经验为: " + exp));
            source.sendFeedback(() -> Text.literal("通过数据提供器设置玩家经验成功！").formatted(Formatting.GREEN, Formatting.BOLD), true);
            return 1;
        } else {
            source.sendError(Text.literal("执行失败，未检测到玩家！").formatted(Formatting.RED, Formatting.BOLD));
            return 0;
        }
    }

    private static int addExp(ServerCommandSource source, ServerPlayerEntity player, int amount) {
        if (source instanceof ServerCommandSource && player != null) {
            CodecPlayerDataProvider provider = PlayerDataFactory.getCodecProvider(player);
            provider.addExperience(amount);
            source.sendMessage(Text.literal("添加经验: " + amount));
            source.sendFeedback(() -> Text.literal("通过数据提供器添加玩家经验成功！").formatted(Formatting.GREEN, Formatting.BOLD), true);
            return 1;
        } else {
            source.sendError(Text.literal("执行失败，未检测到玩家！").formatted(Formatting.RED, Formatting.BOLD));
            return 0;
        }
    }

    private static int setVip(ServerCommandSource source, ServerPlayerEntity player, boolean vip) {
        if (source instanceof ServerCommandSource && player != null) {
            CodecPlayerDataProvider provider = PlayerDataFactory.getCodecProvider(player);
            provider.setVip(vip);
            source.sendMessage(Text.literal("设置VIP状态为: " + vip));
            source.sendFeedback(() -> Text.literal("通过数据提供器设置玩家数据成功！").formatted(Formatting.GREEN, Formatting.BOLD), true);
            return 1;
        } else {
            source.sendError(Text.literal("执行失败，未检测到玩家！").formatted(Formatting.RED, Formatting.BOLD));
            return 0;
        }
    }

    private static int setSvip(ServerCommandSource source, ServerPlayerEntity player, boolean svip) {
        if (source instanceof ServerCommandSource && player != null) {
            CodecPlayerDataProvider provider = PlayerDataFactory.getCodecProvider(player);
            provider.setSvip(svip);
            source.sendMessage(Text.literal("设置SVIP状态为: " + svip));
            source.sendFeedback(() -> Text.literal("通过数据提供器设置玩家数据成功！").formatted(Formatting.GREEN, Formatting.BOLD), true);
            return 1;
        } else {
            source.sendError(Text.literal("执行失败，未检测到玩家！").formatted(Formatting.RED, Formatting.BOLD));
            return 0;
        }
    }

    private static int setMultiplier(ServerCommandSource source, ServerPlayerEntity player, double multiplier) {
        if (source instanceof ServerCommandSource && player != null) {
            CodecPlayerDataProvider provider = PlayerDataFactory.getCodecProvider(player);
            provider.setExpMultiplier(multiplier);
            source.sendMessage(Text.literal("设置经验倍率为: " + multiplier));
            source.sendFeedback(() -> Text.literal("通过数据提供器设置玩家数据成功！").formatted(Formatting.GREEN, Formatting.BOLD), true);
            return 1;
        } else {
            source.sendError(Text.literal("执行失败，未检测到玩家！").formatted(Formatting.RED, Formatting.BOLD));
            return 0;
        }
    }

    private static int showInfo(ServerCommandSource source, ServerPlayerEntity player) {
        if (source instanceof ServerCommandSource && player != null) {
            PlayerDataProvider provider = PlayerDataFactory.getProvider(player);
            source.sendMessage(Text.literal("=== 玩家信息 ==="));
            source.sendMessage(Text.literal("等级: " + provider.getLevel()));
            source.sendMessage(Text.literal("经验: " + provider.getExperience() + "/" + provider.getTotalExpForNextLevel()));
            source.sendMessage(Text.literal("经验进度: " + String.format("%.1f", provider.getExpProgressPercentage()) + "%"));
            source.sendMessage(Text.literal("升级还需: " + provider.getRemainingExpForNextLevel() + " 经验"));
            source.sendMessage(Text.literal("经验倍率: " + provider.getExpMultiplier()));
            source.sendMessage(Text.literal("基础经验需求: " + provider.getBaseExpForNextLevel()));
            source.sendMessage(Text.literal("实际经验需求: " + provider.getTotalExpForNextLevel()));
            source.sendMessage(Text.literal("VIP: " + provider.isVip()));
            source.sendMessage(Text.literal("SVIP: " + provider.isSVip()));
            source.sendFeedback(() -> Text.literal("通过数据提供器获取玩家数据成功！").formatted(Formatting.GREEN, Formatting.BOLD), true);
            return 1;
        } else {
            source.sendError(Text.literal("执行失败，未检测到玩家！").formatted(Formatting.RED, Formatting.BOLD));
            return 0;
        }
    }

    private static int setMultiplierViaFactory(ServerCommandSource source, ServerPlayerEntity player, double multiplier) {
        if (source instanceof ServerCommandSource && player != null) {
            PlayerDataFactory.setExpMultiplier(player, multiplier);
            source.sendMessage(Text.literal("设置经验倍率为: " + multiplier));
            source.sendFeedback(() -> Text.literal("通过工厂类设置玩家数据成功！").formatted(Formatting.GREEN, Formatting.BOLD), true);
            return 1;
        } else {
            source.sendError(Text.literal("执行失败，未检测到玩家！").formatted(Formatting.RED, Formatting.BOLD));
            return 0;
        }
    }

    private static int reloadData(ServerCommandSource source, ServerPlayerEntity player) {
        if (source instanceof ServerCommandSource && player != null) {
            PlayerDataFactory.reloadProvider(player);
            source.sendMessage(Text.literal("已重新加载玩家数据"));
            source.sendFeedback(() -> Text.literal("通过工厂类重新加载玩家数据成功！").formatted(Formatting.GREEN, Formatting.BOLD), true);
            return 1;
        } else {
            source.sendError(Text.literal("执行失败，未检测到玩家！").formatted(Formatting.RED, Formatting.BOLD));
            return 0;
        }
    }

    private static int showInfoViaFactory(ServerCommandSource source, ServerPlayerEntity player) {
        if (source instanceof ServerCommandSource && player != null) {
            source.sendMessage(Text.literal("=== 玩家信息 ==="));
            source.sendMessage(Text.literal("等级: " + PlayerDataFactory.getLevel(player)));
            source.sendMessage(Text.literal("经验: " + PlayerDataFactory.getExperience(player) + "/" + PlayerDataFactory.getExpForNextLevel(player)));
            source.sendMessage(Text.literal("经验进度: " + String.format("%.1f", PlayerDataFactory.getExpProgressPercentage(player)) + "%"));
            source.sendMessage(Text.literal("升级还需: " + PlayerDataFactory.getRemainingExp(player) + " 经验"));
            source.sendMessage(Text.literal("经验倍率: " + PlayerDataFactory.getExpMultiplier(player)));
            source.sendMessage(Text.literal("VIP: " + PlayerDataFactory.isVip(player)));
            source.sendMessage(Text.literal("SVIP: " + PlayerDataFactory.isSVip(player)));

            Optional<String> dataFilePath = PlayerDataFactory.getDataFilePath(player);
            dataFilePath.ifPresent(path ->
                    source.sendMessage(Text.literal("数据文件: " + path))
            );
            source.sendFeedback(() -> Text.literal("通过工厂类获取玩家数据成功！").formatted(Formatting.GREEN, Formatting.BOLD), true);
            return 1;
        } else {
            source.sendError(Text.literal("执行失败，未检测到玩家！").formatted(Formatting.RED, Formatting.BOLD));
            return 0;
        }
    }
}