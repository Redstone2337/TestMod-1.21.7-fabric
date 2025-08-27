// ModConfigScreen.java
package net.redstone233.test.core.screen;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.impl.builders.DropdownMenuBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.redstone233.test.TestModClient;
import net.redstone233.test.core.config.ModConfig;

import java.util.Arrays;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class ModConfigScreen {
    public static Screen getScreen(Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Text.literal("公告屏幕配置"))
                .setSavingRunnable(TestModClient::saveConfig);

        ConfigEntryBuilder entryBuilder = builder.entryBuilder();
        ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();

        // 获取所有颜色Formatting的集合
        Set<Formatting> colorFormattings = Arrays.stream(Formatting.values())
                .filter(Formatting::isColor)
                .collect(Collectors.toSet());

        // 颜色模式设置
        builder.getOrCreateCategory(Text.literal("颜色模式"))
                .addEntry(entryBuilder.startBooleanToggle(Text.literal("使用自定义RGB颜色"), config.useCustomRGB)
                        .setDefaultValue(false)
                        .setTooltip(Text.literal("启用后将隐藏Formatting枚举颜色选择，使用自定义RGB颜色"))
                        .setSaveConsumer(newValue -> config.useCustomRGB = newValue)
                        .build());

        // 创建颜色保存消费者
        Consumer<String> mainTitleColorConsumer = createColorConsumer(
                newValue -> config.mainTitleColor = newValue,
                0xFFD700,
                "主标题颜色"
        );

        Consumer<String> subTitleColorConsumer = createColorConsumer(
                newValue -> config.subTitleColor = newValue,
                0xFFFFFF,
                "副标题颜色"
        );

        Consumer<String> contentColorConsumer = createColorConsumer(
                newValue -> config.contentColor = newValue,
                0x0610EA,
                "内容颜色"
        );

        // 标题设置
        builder.getOrCreateCategory(Text.literal("标题设置"))
                .addEntry(entryBuilder.startStrField(Text.literal("主标题"), config.mainTitle)
                        .setDefaultValue("服务器公告")
                        .setTooltip(Text.literal("显示在公告屏幕顶部的大标题"))
                        .setSaveConsumer(newValue -> config.mainTitle = newValue)
                        .build())
                .addEntry(entryBuilder.startStrField(Text.literal("主标题颜色"), String.format("0x%06X", config.mainTitleColor))
                        .setDefaultValue("0xFFD700")
                        .setTooltip(Text.literal("主标题的文本颜色 (十六进制格式，如0xFFD700表示金色)"))
                        .setSaveConsumer(mainTitleColorConsumer)
                        .setRequirement(() -> config.useCustomRGB) // 仅在自定义RGB模式下显示
                        .build())
                .addEntry(entryBuilder.startDropdownMenu(Text.literal("主标题颜色"),
                                DropdownMenuBuilder.TopCellElementBuilder.of(
                                        getFormattingFromColor(config.mainTitleColor),
                                        str -> {
                                            try {
                                                return Formatting.valueOf(str.toUpperCase());
                                            } catch (IllegalArgumentException e) {
                                                return null;
                                            }
                                        },
                                        formatting -> Text.literal(formatting != null ? formatting.getName() : "WHITE")
                                ),
                                DropdownMenuBuilder.CellCreatorBuilder.of(
                                        formatting -> Text.literal(formatting != null ? formatting.getName() : "WHITE")
                                ))
                        .setDefaultValue(getFormattingFromColor(config.mainTitleColor))
                        .setSelections(colorFormattings)
                        .setSaveConsumer(newValue -> config.mainTitleColor = getColorFromFormatting((Formatting) newValue))
                        .setRequirement(() -> !config.useCustomRGB) // 仅在非自定义RGB模式下显示
                        .build())
                .addEntry(entryBuilder.startStrField(Text.literal("副标题"), config.subTitle)
                        .setDefaultValue("最新通知")
                        .setTooltip(Text.literal("显示在主标题下方的小标题"))
                        .setSaveConsumer(newValue -> config.subTitle = newValue)
                        .build())
                .addEntry(entryBuilder.startStrField(Text.literal("副标题颜色"), String.format("0x%06X", config.subTitleColor))
                        .setDefaultValue("0xFFFFFF")
                        .setTooltip(Text.literal("副标题的文本颜色 (十六进制格式)"))
                        .setSaveConsumer(subTitleColorConsumer)
                        .setRequirement(() -> config.useCustomRGB) // 仅在自定义RGB模式下显示
                        .build())
                .addEntry(entryBuilder.startDropdownMenu(Text.literal("副标题颜色"),
                                DropdownMenuBuilder.TopCellElementBuilder.of(
                                        getFormattingFromColor(config.subTitleColor),
                                        str -> {
                                            try {
                                                return Formatting.valueOf(str.toUpperCase());
                                            } catch (IllegalArgumentException e) {
                                                return null;
                                            }
                                        },
                                        formatting -> Text.literal(formatting != null ? formatting.getName() : "WHITE")
                                ),
                                DropdownMenuBuilder.CellCreatorBuilder.of(
                                        formatting -> Text.literal(formatting != null ? formatting.getName() : "WHITE")
                                ))
                        .setDefaultValue(getFormattingFromColor(config.subTitleColor))
                        .setSelections(colorFormattings)
                        .setSaveConsumer(newValue -> config.subTitleColor = getColorFromFormatting((Formatting) newValue))
                        .setRequirement(() -> !config.useCustomRGB) // 仅在非自定义RGB模式下显示
                        .build());

        // 按钮设置
        builder.getOrCreateCategory(Text.literal("按钮设置"))
                .addEntry(entryBuilder.startStrField(Text.literal("确定按钮文本"), config.confirmButtonText)
                        .setDefaultValue("确定")
                        .setTooltip(Text.literal("确定按钮上显示的文本"))
                        .setSaveConsumer(newValue -> config.confirmButtonText = newValue)
                        .build())
                .addEntry(entryBuilder.startStrField(Text.literal("前往投递按钮文本"), config.submitButtonText)
                        .setDefaultValue("前往投递")
                        .setTooltip(Text.literal("前往投递按钮上显示的文本"))
                        .setSaveConsumer(newValue -> config.submitButtonText = newValue)
                        .build())
                .addEntry(entryBuilder.startStrField(Text.literal("按钮链接"), config.buttonLink)
                        .setDefaultValue("https://example.com/submit")
                        .setTooltip(Text.literal("点击按钮后打开的网页链接"))
                        .setSaveConsumer(newValue -> config.buttonLink = newValue)
                        .build());

        // 图标设置
        builder.getOrCreateCategory(Text.literal("图标设置"))
                .addEntry(entryBuilder.startBooleanToggle(Text.literal("显示图标"), config.showIcon)
                        .setDefaultValue(true)
                        .setTooltip(Text.literal("是否在标题左侧显示图标"))
                        .setSaveConsumer(newValue -> config.showIcon = newValue)
                        .build())
                .addEntry(entryBuilder.startStrField(Text.literal("图标路径"), config.iconPath)
                        .setDefaultValue("mtc:textures/gui/announcement_icon.png")
                        .setTooltip(Text.literal("图标资源路径 (例如: testmod:textures/gui/announcement_icon.png)"))
                        .setSaveConsumer(newValue -> config.iconPath = newValue)
                        .build())
                .addEntry(entryBuilder.startIntField(Text.literal("图标宽度"), config.iconWidth)
                        .setDefaultValue(32)
                        .setMin(16)
                        .setMax(1024)
                        .setTooltip(Text.literal("图标的显示宽度 (像素)"))
                        .setSaveConsumer(newValue -> config.iconWidth = newValue)
                        .build())
                .addEntry(entryBuilder.startIntField(Text.literal("图标高度"), config.iconHeight)
                        .setDefaultValue(32)
                        .setMin(16)
                        .setMax(1024)
                        .setTooltip(Text.literal("图标的显示高度 (像素)"))
                        .setSaveConsumer(newValue -> config.iconHeight = newValue)
                        .build())
                .addEntry(entryBuilder.startIntField(Text.literal("图标文本间距"), config.iconTextSpacing)
                        .setDefaultValue(10)
                        .setMin(0)
                        .setMax(50)
                        .setTooltip(Text.literal("图标与文本之间的间距 (像素)"))
                        .setSaveConsumer(newValue -> config.iconTextSpacing = newValue)
                        .build());

        // 背景设置
        builder.getOrCreateCategory(Text.literal("背景设置"))
                .addEntry(entryBuilder.startBooleanToggle(Text.literal("使用自定义公告背景图"), config.useCustomAnnouncementBackground)
                        .setDefaultValue(false)
                        .setTooltip(Text.literal("是否使用自定义公告背景图"))
                        .setSaveConsumer(newValue -> config.useCustomAnnouncementBackground = newValue)
                        .build())
                .addEntry(entryBuilder.startStrField(Text.literal("公告背景图路径"), config.announcementBackgroundPath)
                        .setDefaultValue("testmod:textures/gui/announcement_background.png")
                        .setTooltip(Text.literal("公告背景图资源路径 (例如: testmod:textures/gui/announcement_background.png)"))
                        .setSaveConsumer(newValue -> config.announcementBackgroundPath = newValue)
                        .setRequirement(() -> config.useCustomAnnouncementBackground) // 仅在启用自定义公告背景时显示
                        .build())
                .addEntry(entryBuilder.startBooleanToggle(Text.literal("使用自定义玩家信息背景图"), config.useCustomPlayerInfoBackground)
                        .setDefaultValue(false)
                        .setTooltip(Text.literal("是否使用自定义玩家信息背景图"))
                        .setSaveConsumer(newValue -> config.useCustomPlayerInfoBackground = newValue)
                        .build())
                .addEntry(entryBuilder.startStrField(Text.literal("玩家信息背景图路径"), config.playerInfoBackgroundPath)
                        .setDefaultValue("testmod:textures/gui/player_info_background.png")
                        .setTooltip(Text.literal("玩家信息背景图资源路径 (例如: testmod:textures/gui/player_info_background.png)"))
                        .setSaveConsumer(newValue -> config.playerInfoBackgroundPath = newValue)
                        .setRequirement(() -> config.useCustomPlayerInfoBackground) // 仅在启用自定义玩家信息背景时显示
                        .build());

        // 公告内容
        builder.getOrCreateCategory(Text.literal("公告内容"))
                .addEntry(entryBuilder.startTextDescription(Text.literal("每行一条公告，支持多行").formatted(Formatting.GRAY))
                        .build())
                .addEntry(entryBuilder.startStrList(Text.literal("公告内容"), config.announcementContent)
                        .setDefaultValue(Arrays.asList(
                                "§a欢迎游玩，我们团队做的模组！",
                                " ",
                                "§e一些提醒：",
                                "§f1. 模组仅限于1.21.7~1.21.8fabric",
                                "§f2. 模组目前是半成品",
                                "§f3. 后面会继续更新",
                                " ",
                                "§b模组随缘更新",
                                "§c若发现bug可以向模组作者或者仓库反馈！"
                        ))
                        .setTooltip(Text.literal("公告内容列表，每行一条。支持Minecraft格式代码（如§a表示绿色）"))
                        .setSaveConsumer(newValue -> config.announcementContent = newValue)
                        .build())
                .addEntry(entryBuilder.startStrField(Text.literal("内容颜色"), String.format("0x%06X", config.contentColor))
                        .setDefaultValue("0x0610EA")
                        .setTooltip(Text.literal("公告内容的默认文本颜色 (十六进制格式)"))
                        .setSaveConsumer(contentColorConsumer)
                        .setRequirement(() -> config.useCustomRGB) // 仅在自定义RGB模式下显示
                        .build())
                .addEntry(entryBuilder.startDropdownMenu(Text.literal("内容颜色"),
                                DropdownMenuBuilder.TopCellElementBuilder.of(
                                        getFormattingFromColor(config.contentColor),
                                        str -> {
                                            try {
                                                return Formatting.valueOf(str.toUpperCase());
                                            } catch (IllegalArgumentException e) {
                                                return null;
                                            }
                                        },
                                        formatting -> Text.literal(formatting != null ? formatting.getName() : "WHITE")
                                ),
                                DropdownMenuBuilder.CellCreatorBuilder.of(
                                        formatting -> Text.literal(formatting != null ? formatting.getName() : "WHITE")
                                ))
                        .setDefaultValue(getFormattingFromColor(config.contentColor))
                        .setSelections(colorFormattings)
                        .setSaveConsumer(newValue -> config.contentColor = getColorFromFormatting((Formatting) newValue))
                        .setRequirement(() -> !config.useCustomRGB) // 仅在非自定义RGB模式下显示
                        .build())
                .addEntry(entryBuilder.startTextDescription(Text.literal("提示：在公告内容中使用 § 符号后跟颜色代码可以设置特定文本的颜色").formatted(Formatting.GRAY))
                        .build())
                .addEntry(entryBuilder.startTextDescription(Text.literal("例如：§a绿色文本 §b蓝色文本 §c红色文本 §e黄色文本").formatted(Formatting.GRAY))
                        .build())
                .addEntry(entryBuilder.startIntSlider(Text.literal("滚动速度"), config.scrollSpeed, 10, 100)
                        .setDefaultValue(30)
                        .setTooltip(Text.literal("公告内容滚动速度 (像素/秒)"))
                        .setSaveConsumer(newValue -> config.scrollSpeed = newValue)
                        .build());

        // 调试设置
        builder.getOrCreateCategory(Text.literal("调试设置"))
                .addEntry(entryBuilder.startBooleanToggle(Text.literal("调试模式"), config.debugMode)
                        .setDefaultValue(false)
                        .setTooltip(Text.literal("启用调试模式（显示UI边界等辅助信息）"))
                        .setSaveConsumer(newValue -> {
                            config.debugMode = newValue;
                            TestModClient.DEBUG_MODE = newValue;
                        })
                        .build());

        // 显示设置
        builder.getOrCreateCategory(Text.literal("显示设置"))
                .addEntry(entryBuilder.startBooleanToggle(Text.literal("进入世界时显示公告"), config.showOnWorldEnter)
                        .setDefaultValue(true)
                        .setTooltip(Text.literal("是否在玩家进入世界时显示公告屏幕"))
                        .setSaveConsumer(newValue -> config.showOnWorldEnter = newValue)
                        .build());

        return builder.build();
    }

    /**
     * 创建颜色保存消费者，用于处理十六进制颜色值的解析和验证
     *
     * @param colorSetter 颜色设置器，用于设置配置中的颜色值
     * @param defaultValue 默认颜色值（当输入无效时使用）
     * @param colorName 颜色名称（用于日志记录）
     * @return 配置保存消费者
     */
    private static Consumer<String> createColorConsumer(Consumer<Integer> colorSetter, int defaultValue, String colorName) {
        return newValue -> {
            try {
                // 移除可能的#前缀和空格
                String cleanValue = newValue.replace("#", "").replace(" ", "").trim();

                // 处理0x前缀或没有前缀的情况
                int colorValue;
                if (cleanValue.startsWith("0x")) {
                    colorValue = (int) Long.parseLong(cleanValue.substring(2), 16);
                } else {
                    colorValue = (int) Long.parseLong(cleanValue, 16);
                }

                // 确保只保留RGB部分（移除Alpha通道）
                colorValue = colorValue & 0xFFFFFF;
                colorSetter.accept(colorValue);

            } catch (NumberFormatException e) {
                TestModClient.LOGGER.warn("无效的{}值: {}, 使用默认值", colorName, newValue);
                colorSetter.accept(defaultValue);
            }
        };
    }

    // 辅助方法：从颜色值获取Formatting枚举
    private static Formatting getFormattingFromColor(int color) {
        // 移除alpha通道，只比较RGB值
        int rgbColor = color & 0xFFFFFF;

        for (Formatting formatting : Formatting.values()) {
            if (formatting.isColor() && formatting.getColorValue() != null) {
                int formattingColor = formatting.getColorValue() & 0xFFFFFF;
                if (formattingColor == rgbColor) {
                    return formatting;
                }
            }
        }
        return Formatting.WHITE; // 默认值
    }

    // 辅助方法：从Formatting枚举获取颜色值
    private static int getColorFromFormatting(Formatting formatting) {
        return formatting.getColorValue() != null ? formatting.getColorValue() : 0xFFFFFF;
    }
}