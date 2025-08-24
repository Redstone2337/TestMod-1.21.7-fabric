package net.redstone233.test.core.commands;


import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.redstone233.test.core.api.PlayerDataProvider;
import net.redstone233.test.core.api.impl.CodecPlayerDataProvider;
import net.redstone233.test.core.until.PlayerDataFactory;

import java.util.Optional;

public class InformationCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("playermod")
                .requires(source -> source.hasPermissionLevel(2))
                .then(CommandManager.literal("setlevel")
                        .then(CommandManager.argument("level", IntegerArgumentType.integer(1))
                                .executes(context -> {
                                    ServerPlayerEntity player = context.getSource().getPlayer();
                                    int level = IntegerArgumentType.getInteger(context, "level");

                                    CodecPlayerDataProvider provider = PlayerDataFactory.getCodecProvider(player);
                                    provider.setLevel(level);

                                    context.getSource().sendMessage(Text.literal("设置等级为: " + level));
                                    return 1;
                                })))
                .then(CommandManager.literal("setexp")
                        .then(CommandManager.argument("exp", IntegerArgumentType.integer(0))
                                .executes(context -> {
                                    ServerPlayerEntity player = context.getSource().getPlayer();
                                    int exp = IntegerArgumentType.getInteger(context, "exp");

                                    CodecPlayerDataProvider provider = PlayerDataFactory.getCodecProvider(player);
                                    provider.setExperience(exp);

                                    context.getSource().sendMessage(Text.literal("设置经验为: " + exp));
                                    return 1;
                                })))
                .then(CommandManager.literal("addexp")
                        .then(CommandManager.argument("amount", IntegerArgumentType.integer(1))
                                .executes(context -> {
                                    ServerPlayerEntity player = context.getSource().getPlayer();
                                    int amount = IntegerArgumentType.getInteger(context, "amount");

                                    CodecPlayerDataProvider provider = PlayerDataFactory.getCodecProvider(player);
                                    provider.addExperience(amount);

                                    context.getSource().sendMessage(Text.literal("添加经验: " + amount));
                                    return 1;
                                })))
                .then(CommandManager.literal("setvip")
                        .then(CommandManager.argument("vip", BoolArgumentType.bool())
                                .executes(context -> {
                                    ServerPlayerEntity player = context.getSource().getPlayer();
                                    boolean vip = BoolArgumentType.getBool(context, "vip");

                                    CodecPlayerDataProvider provider = PlayerDataFactory.getCodecProvider(player);
                                    provider.setVip(vip);

                                    context.getSource().sendMessage(Text.literal("设置VIP状态为: " + vip));
                                    return 1;
                                })))
                .then(CommandManager.literal("setsvip")
                        .then(CommandManager.argument("svip", BoolArgumentType.bool())
                                .executes(context -> {
                                    ServerPlayerEntity player = context.getSource().getPlayer();
                                    boolean svip = BoolArgumentType.getBool(context, "svip");

                                    CodecPlayerDataProvider provider = PlayerDataFactory.getCodecProvider(player);
                                    provider.setSvip(svip);

                                    context.getSource().sendMessage(Text.literal("设置SVIP状态为: " + svip));
                                    return 1;
                                })))
                .then(CommandManager.literal("setmultiplier")
                        .then(CommandManager.argument("multiplier", DoubleArgumentType.doubleArg(1.0, 10.0))
                                .executes(context -> {
                                    ServerPlayerEntity player = context.getSource().getPlayer();
                                    double multiplier = DoubleArgumentType.getDouble(context, "multiplier");

                                    CodecPlayerDataProvider provider = PlayerDataFactory.getCodecProvider(player);
                                    provider.setExpMultiplier(multiplier);

                                    context.getSource().sendMessage(Text.literal("设置经验倍率为: " + multiplier));
                                    return 1;
                                })))
                .then(CommandManager.literal("info")
                        .executes(context -> {
                            ServerPlayerEntity player = context.getSource().getPlayer();
                            PlayerDataProvider provider = PlayerDataFactory.getProvider(player);

                            context.getSource().sendMessage(Text.literal("=== 玩家信息 ==="));
                            context.getSource().sendMessage(Text.literal("等级: " + provider.getLevel()));
                            context.getSource().sendMessage(Text.literal("经验: " + provider.getExperience() + "/" + provider.getTotalExpForNextLevel()));
                            context.getSource().sendMessage(Text.literal("经验进度: " + String.format("%.1f", provider.getExpProgressPercentage()) + "%"));
                            context.getSource().sendMessage(Text.literal("升级还需: " + provider.getRemainingExpForNextLevel() + " 经验"));
                            context.getSource().sendMessage(Text.literal("经验倍率: " + provider.getExpMultiplier()));
                            context.getSource().sendMessage(Text.literal("基础经验需求: " + provider.getBaseExpForNextLevel()));
                            context.getSource().sendMessage(Text.literal("实际经验需求: " + provider.getTotalExpForNextLevel()));
                            context.getSource().sendMessage(Text.literal("VIP: " + provider.isVip()));
                            context.getSource().sendMessage(Text.literal("SVIP: " + provider.isSVip()));

                            return 1;
                        }))
                .then(CommandManager.literal("setmultiplier")
                        .then(CommandManager.argument("multiplier", DoubleArgumentType.doubleArg(1.0, 10.0))
                                .executes(context -> {
                                    ServerPlayerEntity player = context.getSource().getPlayer();
                                    double multiplier = DoubleArgumentType.getDouble(context, "multiplier");

                                    // 使用工厂方法而不是直接获取提供者
                                    PlayerDataFactory.setExpMultiplier(player, multiplier);

                                    context.getSource().sendMessage(Text.literal("设置经验倍率为: " + multiplier));
                                    return 1;
                                })))

                .then(CommandManager.literal("reload")
                        .executes(context -> {
                            ServerPlayerEntity player = context.getSource().getPlayer();

                            // 使用工厂方法重新加载数据
                            PlayerDataFactory.reloadProvider(player);

                            context.getSource().sendMessage(Text.literal("已重新加载玩家数据"));
                            return 1;
                        }))

                .then(CommandManager.literal("info")
                        .executes(context -> {
                            ServerPlayerEntity player = context.getSource().getPlayer();

                            // 使用工厂方法获取数据
                            context.getSource().sendMessage(Text.literal("=== 玩家信息 ==="));
                            context.getSource().sendMessage(Text.literal("等级: " + PlayerDataFactory.getLevel(player)));
                            context.getSource().sendMessage(Text.literal("经验: " + PlayerDataFactory.getExperience(player) + "/" + PlayerDataFactory.getExpForNextLevel(player)));
                            context.getSource().sendMessage(Text.literal("经验进度: " + String.format("%.1f", PlayerDataFactory.getExpProgressPercentage(player)) + "%"));
                            context.getSource().sendMessage(Text.literal("升级还需: " + PlayerDataFactory.getRemainingExp(player) + " 经验"));
                            context.getSource().sendMessage(Text.literal("经验倍率: " + PlayerDataFactory.getExpMultiplier(player)));
                            context.getSource().sendMessage(Text.literal("VIP: " + PlayerDataFactory.isVip(player)));
                            context.getSource().sendMessage(Text.literal("SVIP: " + PlayerDataFactory.isSVip(player)));

                            // 显示数据文件路径（如果有）
                            Optional<String> dataFilePath = PlayerDataFactory.getDataFilePath(player);
                            dataFilePath.ifPresent(path ->
                                    context.getSource().sendMessage(Text.literal("数据文件: " + path))
                            );

                            return 1;
                        }))
        );
    }
}