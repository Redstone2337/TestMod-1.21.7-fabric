package net.redstone233.test.core.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class SetValueCountCommand {

    private static final int DefaultValue = Item.DEFAULT_MAX_COUNT;
    private static final int DefaultMaxValue = Item.MAX_MAX_COUNT;
    private static final int MAX_SIZE = 300;
    private static int CustomMaxSize = 99;


    public static LiteralArgumentBuilder<ServerCommandSource> register() {
        return CommandManager.literal("itemstack")
                .requires(src -> src.hasPermissionLevel(1))
                .then(CommandManager.literal("get")
                        .then(CommandManager.literal("default")
                                .executes(run -> getDefaultSize(run.getSource(),run.getSource().getPlayer()))
                        )
                        .then(CommandManager.literal("defaultMax")
                                .executes(run -> getDefaultMaxSize(run.getSource(),run.getSource().getPlayer()))
                        )
                        .then(CommandManager.literal("defaultCustomMaxCount")
                                .executes(run -> getDefaultCustomMaxSize(run.getSource(), run.getSource().getPlayer()))
                        )
                )
                .then(CommandManager.literal("set")
//                        .then(CommandManager.literal("customDefaultValue")
//                                .then(CommandManager.argument("value", IntegerArgumentType.integer(1,getDefaultMaxValue()))
//                                        .executes(null)
//                                )
//
//                        )
                        .then(CommandManager.literal("customMaxValue")
                                .then(CommandManager.argument("value", IntegerArgumentType.integer(1,MAX_SIZE))
                                        .executes(run -> setCustomValue(
                                                run.getSource(),
                                                IntegerArgumentType.getInteger(run,"value")
                                        ))
                                )

                        )
                        .then(CommandManager.literal("default")
                                .executes(run -> setDefaultValues(run.getSource(),run.getSource().getPlayer()))
                        )
                        .then(CommandManager.literal("defaultMax")
                                .executes(run -> setDefaultMwxValues(run.getSource(),run.getSource().getPlayer()))
                        )
                );
    }

    public static int getDefaultValue() {
        return DefaultValue;
    }


    public static int getDefaultMaxValue() {
        return DefaultMaxValue;
    }

    public static int getCustomMaxSize() {
        return CustomMaxSize;
    }

    public static void setCustomMaxSize(int customMaxSize) {
        CustomMaxSize = customMaxSize;
    }

    private static int getDefaultSize(ServerCommandSource source, PlayerEntity player) throws CommandSyntaxException {
        if (player != null) {
            source.sendFeedback(() -> Text.literal("默认值为：" + getDefaultValue()), true);
        } else {
            source.sendError(Text.literal("执行失败！未发现玩家。"));
            return 0;
        }
        return Command.SINGLE_SUCCESS;
    }

    private static int getDefaultMaxSize(ServerCommandSource source, PlayerEntity player) throws CommandSyntaxException {
        if (player != null) {
            source.sendFeedback(() -> Text.literal("默认最大值为：" + getDefaultMaxValue()), true);
        } else {
            source.sendError(Text.literal("执行失败！未发现玩家。"));
            return 0;
        }
        return Command.SINGLE_SUCCESS;
    }

    private static int getDefaultCustomMaxSize(ServerCommandSource source, PlayerEntity player) throws CommandSyntaxException {
        if (player != null) {
            source.sendFeedback(() -> Text.literal("默认最大值为：" + getCustomMaxSize()), true);
        } else {
            source.sendError(Text.literal("执行失败！未发现玩家。"));
            return 0;
        }
        return Command.SINGLE_SUCCESS;
    }

    private static int setCustomValue(ServerCommandSource source, int value) {
        if (value <= MAX_SIZE) {
            setCustomMaxSize(value);
            source.sendFeedback(() -> Text.literal("设置成功！已经将最值设置为：" + getCustomMaxSize() ), true);
        } else {
            source.sendError(Text.literal("数值过大，应该在300以内！"));
            return 0;
        }
        return Command.SINGLE_SUCCESS;
    }

    private static int setDefaultValues(ServerCommandSource source, PlayerEntity player) {
        if (player != null) {
            setCustomMaxSize(getDefaultValue());
            source.sendFeedback(() -> Text.literal("执行成功，已经将值设置为：" + getCustomMaxSize()),true);
        } else {
            source.sendError(Text.literal("执行失败！为未发现目标。"));
            return  0;
        }
        return Command.SINGLE_SUCCESS;
    }

    private static int setDefaultMwxValues(ServerCommandSource source, PlayerEntity player) {
        if (player != null) {
            setCustomMaxSize(getDefaultMaxValue());
            source.sendFeedback(() -> Text.literal("执行成功，已经将值设置为：" + getCustomMaxSize()),true);
        } else {
            source.sendError(Text.literal("执行失败！未发现目标。"));
            return  0;
        }
        return Command.SINGLE_SUCCESS;
    }
}
