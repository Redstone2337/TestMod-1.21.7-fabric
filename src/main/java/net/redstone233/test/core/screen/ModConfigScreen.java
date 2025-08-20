package net.redstone233.test.core.screen;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.redstone233.test.TestModClient;
import net.redstone233.test.core.config.ModConfig;

import java.util.Arrays;

public class ModConfigScreen {
    public static Screen getScreen(Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Text.literal("公告屏幕配置"))
                .setSavingRunnable(TestModClient::saveConfig);

        ConfigEntryBuilder entryBuilder = builder.entryBuilder();
        ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();

        // 颜色模式设置
        builder.getOrCreateCategory(Text.literal("颜色模式"))
                .addEntry(entryBuilder.startBooleanToggle(Text.literal("使用自定义RGB颜色"), config.useCustomRGB)
                        .setDefaultValue(false)
                        .setTooltip(Text.literal("启用后将隐藏Formatting枚举颜色选择，使用自定义RGB颜色"))
                        .setSaveConsumer(newValue -> config.useCustomRGB = newValue)
                        .build());

        // 标题设置
        builder.getOrCreateCategory(Text.literal("标题设置"))
                .addEntry(entryBuilder.startStrField(Text.literal("主标题"), config.mainTitle)
                        .setDefaultValue("服务器公告")
                        .setTooltip(Text.literal("显示在公告屏幕顶部的大标题"))
                        .setSaveConsumer(newValue -> config.mainTitle = newValue)
                        .build())
                .addEntry(entryBuilder.startIntField(Text.literal("主标题颜色"), config.mainTitleColor)
                        .setDefaultValue(0xFFD700)
                        .setTooltip(Text.literal("主标题的文本颜色 (RGB整数格式，如0xFFD700表示金色)"))
                        .setSaveConsumer(newValue -> config.mainTitleColor = newValue)
                        .setRequirement(() -> config.useCustomRGB) // 仅在自定义RGB模式下显示
                        .build())
                .addEntry(entryBuilder.startEnumSelector(Text.literal("主标题颜色"), Formatting.class,
                                getFormattingFromColor(config.mainTitleColor))
                        .setDefaultValue(Formatting.GOLD)
                        .setTooltip(Text.literal("主标题的文本颜色 (Formatting枚举)"))
                        .setSaveConsumer(newValue -> config.mainTitleColor = getColorFromFormatting(newValue))
                        .setRequirement(() -> !config.useCustomRGB) // 仅在非自定义RGB模式下显示
                        .build())
                .addEntry(entryBuilder.startStrField(Text.literal("副标题"), config.subTitle)
                        .setDefaultValue("最新通知")
                        .setTooltip(Text.literal("显示在主标题下方的小标题"))
                        .setSaveConsumer(newValue -> config.subTitle = newValue)
                        .build())
                .addEntry(entryBuilder.startIntField(Text.literal("副标题颜色"), config.subTitleColor)
                        .setDefaultValue(0xFFFFFF)
                        .setTooltip(Text.literal("副标题的文本颜色 (RGB整数格式)"))
                        .setSaveConsumer(newValue -> config.subTitleColor = newValue)
                        .setRequirement(() -> config.useCustomRGB) // 仅在自定义RGB模式下显示
                        .build())
                .addEntry(entryBuilder.startEnumSelector(Text.literal("副标题颜色"), Formatting.class,
                                getFormattingFromColor(config.subTitleColor))
                        .setDefaultValue(Formatting.WHITE)
                        .setTooltip(Text.literal("副标题的文本颜色 (Formatting枚举)"))
                        .setSaveConsumer(newValue -> config.subTitleColor = getColorFromFormatting(newValue))
                        .setRequirement(() -> !config.useCustomRGB) // 仅在非自定义RGB模式下显示
                        .build());

        // 图标设置
        builder.getOrCreateCategory(Text.literal("图标设置"))
                .addEntry(entryBuilder.startBooleanToggle(Text.literal("显示图标"), config.showIcon)
                        .setDefaultValue(true)
                        .setTooltip(Text.literal("是否在标题左侧显示图标"))
                        .setSaveConsumer(newValue -> config.showIcon = newValue)
                        .build())
                .addEntry(entryBuilder.startStrField(Text.literal("图标路径"), config.iconPath)
                        .setDefaultValue("testmod:textures/gui/announcement_icon.png")
                        .setTooltip(Text.literal("图标资源路径 (例如: testmod:textures/gui/icon.png)"))
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

        // 公告内容
        builder.getOrCreateCategory(Text.literal("公告内容"))
                .addEntry(entryBuilder.startTextDescription(Text.literal("每行一条公告，支持多行").formatted(Formatting.GRAY))
                        .build())
                .addEntry(entryBuilder.startStrList(Text.literal("公告内容"), config.announcementContent)
                        .setDefaultValue(Arrays.asList(
                                "欢迎游玩，我们团队做的模组！",
                                " ",
                                "一些提醒：",
                                "1. 模组仅限于1.21.7~1.21.8fabric",
                                "2. 模组目前是半成品",
                                "3. 后面会继续更新",
                                " ",
                                "模组随缘更新",
                                "若发现bug可以向模组作者或者仓库反馈！"
                        ))
                        .setTooltip(Text.literal("公告内容列表，每行一条"))
                        .setSaveConsumer(newValue -> config.announcementContent = newValue)
                        .build())
                .addEntry(entryBuilder.startIntField(Text.literal("内容颜色"), config.contentColor)
                        .setDefaultValue(0x0610EA)
                        .setTooltip(Text.literal("公告内容的文本颜色 (RGB整数格式)"))
                        .setSaveConsumer(newValue -> config.contentColor = newValue)
                        .setRequirement(() -> config.useCustomRGB) // 仅在自定义RGB模式下显示
                        .build())
                .addEntry(entryBuilder.startEnumSelector(Text.literal("内容颜色"), Formatting.class,
                                getFormattingFromColor(config.contentColor))
                        .setDefaultValue(Formatting.BLUE)
                        .setTooltip(Text.literal("公告内容的文本颜色 (Formatting枚举)"))
                        .setSaveConsumer(newValue -> config.contentColor = getColorFromFormatting(newValue))
                        .setRequirement(() -> !config.useCustomRGB) // 仅在非自定义RGB模式下显示
                        .build())
                .addEntry(entryBuilder.startIntSlider(Text.literal("滚动速度"), config.scrollSpeed, 10, 100)
                        .setDefaultValue(30)
                        .setTooltip(Text.literal("公告内容滚动速度 (像素/秒)"))
                        .setSaveConsumer(newValue -> config.scrollSpeed = newValue)
                        .build());

        // 按钮设置
        builder.getOrCreateCategory(Text.literal("按钮设置"))
                .addEntry(entryBuilder.startStrField(Text.literal("按钮文本"), config.buttonText)
                        .setDefaultValue("前往投递")
                        .setTooltip(Text.literal("按钮上显示的文本"))
                        .setSaveConsumer(newValue -> config.buttonText = newValue)
                        .build())
                .addEntry(entryBuilder.startStrField(Text.literal("按钮链接"), config.buttonLink)
                        .setDefaultValue("https://example.com/submit")
                        .setTooltip(Text.literal("点击按钮后打开的网页链接"))
                        .setSaveConsumer(newValue -> config.buttonLink = newValue)
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

    // 辅助方法：从颜色值获取Formatting枚举
    private static Formatting getFormattingFromColor(int color) {
        for (Formatting formatting : Formatting.values()) {
            if (formatting.isColor() && formatting.getColorValue() != null &&
                    formatting.getColorValue() == color) {
                return formatting;
            }
        }
        return Formatting.WHITE; // 默认值
    }

    // 辅助方法：从Formatting枚举获取颜色值
    private static int getColorFromFormatting(Formatting formatting) {
        return formatting.getColorValue() != null ? formatting.getColorValue() : 0xFFFFFF;
    }
}