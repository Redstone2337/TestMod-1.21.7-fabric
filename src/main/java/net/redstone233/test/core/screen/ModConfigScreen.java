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
                        .build())
                .addEntry(entryBuilder.startStrField(Text.literal("副标题"), config.subTitle)
                        .setDefaultValue("最新通知与更新")
                        .setTooltip(Text.literal("显示在主标题下方的小标题"))
                        .setSaveConsumer(newValue -> config.subTitle = newValue)
                        .build())
                .addEntry(entryBuilder.startIntField(Text.literal("副标题颜色"), config.subTitleColor)
                        .setDefaultValue(0xFFFFFF)
                        .setTooltip(Text.literal("副标题的文本颜色 (RGB整数格式)"))
                        .setSaveConsumer(newValue -> config.subTitleColor = newValue)
                        .build());

        // 公告内容
        builder.getOrCreateCategory(Text.literal("公告内容"))
                .addEntry(entryBuilder.startTextDescription(Text.literal("每行一条公告，支持多行").formatted(Formatting.GRAY))
                        .build())
                .addEntry(entryBuilder.startStrList(Text.literal("公告内容"), config.announcementContent)
                        .setDefaultValue(Arrays.asList(
                                "欢迎来到我们的服务器！",
                                "请遵守服务器规则",
                                "感谢您的支持！"
                        ))
                        .setTooltip(Text.literal("公告内容列表，每行一条"))
                        .setSaveConsumer(newValue -> config.announcementContent = newValue)
                        .build())
                .addEntry(entryBuilder.startIntField(Text.literal("内容颜色"), config.contentColor)
                        .setDefaultValue(0xCCCCCC)
                        .setTooltip(Text.literal("公告内容的文本颜色 (RGB整数格式)"))
                        .setSaveConsumer(newValue -> config.contentColor = newValue)
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

        return builder.build();
    }
}
/*
package com.example.announcementscreen.config;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class ModConfigScreen {
    public static Screen getScreen(Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create()
            .setParentScreen(parent)
            .setTitle(Text.literal("公告屏幕配置"))
            .setSavingRunnable(AnnouncementMod::saveConfig);

        ConfigEntryBuilder entryBuilder = builder.entryBuilder();
        ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();

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
                .build())
            .addEntry(entryBuilder.startStrField(Text.literal("副标题"), config.subTitle)
                .setDefaultValue("最新通知与更新")
                .setTooltip(Text.literal("显示在主标题下方的小标题"))
                .setSaveConsumer(newValue -> config.subTitle = newValue)
                .build())
            .addEntry(entryBuilder.startIntField(Text.literal("副标题颜色"), config.subTitleColor)
                .setDefaultValue(0xFFFFFF)
                .setTooltip(Text.literal("副标题的文本颜色 (RGB整数格式)"))
                .setSaveConsumer(newValue -> config.subTitleColor = newValue)
                .build());

        // 公告内容
        builder.getOrCreateCategory(Text.literal("公告内容"))
            .addEntry(entryBuilder.startTextDescription(Text.literal("每行一条公告，支持多行").formatted(Formatting.GRAY))
                .build())
            .addEntry(entryBuilder.startStrList(Text.literal("公告内容"), config.announcementContent)
                .setDefaultValue(Arrays.asList(
                    "欢迎来到我们的服务器！",
                    "请遵守服务器规则",
                    "感谢您的支持！"
                ))
                .setTooltip(Text.literal("公告内容列表，每行一条"))
                .setSaveConsumer(newValue -> config.announcementContent = newValue)
                .build())
            .addEntry(entryBuilder.startIntField(Text.literal("内容颜色"), config.contentColor)
                .setDefaultValue(0xCCCCCC)
                .setTooltip(Text.literal("公告内容的文本颜色 (RGB整数格式)"))
                .setSaveConsumer(newValue -> config.contentColor = newValue)
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

        return builder.build();
    }
}
 */
