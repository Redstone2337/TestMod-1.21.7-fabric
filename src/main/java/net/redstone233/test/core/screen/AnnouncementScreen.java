package net.redstone233.test.core.screen;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.redstone233.test.TestModClient;
import net.redstone233.test.core.button.ScrollableTextWidget;
import net.redstone233.test.core.config.ConfigInitializer;
import net.redstone233.test.core.config.ModConfig;

import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AnnouncementScreen extends Screen {
    private final ModConfig config;
    private ScrollableTextWidget scrollableText;
    private int tickCount = 0;
    private Identifier iconTexture;
    private Identifier backgroundTexture;

    private TextWidget titleWidget;
    private TextWidget subtitleWidget;

    public AnnouncementScreen() {
        super(Text.literal("Announcement Screen"));
        this.config = getSafeConfig();
    }

    private ModConfig getSafeConfig() {
        // 确保配置已初始化
        if (!ConfigInitializer.isInitialized()) {
            ConfigInitializer.initialize();
        }

        // 返回有效配置
        if (TestModClient.CONFIG != null) {
            return TestModClient.CONFIG;
        }

        TestModClient.LOGGER.warn("使用默认配置作为回退");
        return new ModConfig();
    }

    @Override
    protected void init() {
        super.init();

        // 创建滚动文本部件
        int contentColor = config.useCustomRGB ? config.contentColor : 0xFFFFFFFF; // 默认使用白色

        int centerX = 0;
        MutableText contentText = null;
        scrollableText = new ScrollableTextWidget(
                centerX - 150, 80, 300, this.height - 150,
                contentText, textRenderer, client,
                contentColor
        );

        centerX = this.width / 2;
        int buttonWidth = 100;
        int buttonHeight = 20;
        int buttonY = this.height - 30;

        // 版本兼容性检查
        if (!isVersionCompatible()) {
            addDrawableChild(new TextWidget(centerX, 20, 300, 20,
                    Text.literal("此模组不兼容1.21.5及以下版本").formatted(Formatting.RED), textRenderer));
        }

        // 加载图标纹理
        if (config.showIcon && config.iconPath != null && !config.iconPath.isEmpty()) {
            try {
                iconTexture = Identifier.of(config.iconPath);
                // 检查纹理是否存在
                if (client != null && client.getResourceManager().getResource(iconTexture).isEmpty()) {
                    TestModClient.LOGGER.warn("图标纹理不存在: {}", config.iconPath);
                    iconTexture = null;
                }
            } catch (Exception e) {
                TestModClient.LOGGER.warn("无法加载图标纹理: {}", config.iconPath, e);
                iconTexture = null;
            }
        }

        // 加载背景纹理（如果启用自定义背景）
        if (config.useCustomAnnouncementBackground && config.announcementBackgroundPath != null && !config.announcementBackgroundPath.isEmpty()) {
            try {
                backgroundTexture = Identifier.of(config.announcementBackgroundPath);
                // 检查纹理是否存在
                if (client != null && client.getResourceManager().getResource(backgroundTexture).isEmpty()) {
                    TestModClient.LOGGER.warn("公告背景纹理不存在: {}", config.announcementBackgroundPath);
                    backgroundTexture = null;
                }
            } catch (Exception e) {
                TestModClient.LOGGER.warn("无法加载公告背景纹理: {}", config.announcementBackgroundPath, e);
                backgroundTexture = null;
            }
        }

        // 计算标题位置（考虑图标显示）
        int titleX = centerX;
        if (config.showIcon && iconTexture != null) {
            // 如果有图标，标题向右移动
            int iconAreaWidth = config.iconWidth + config.iconTextSpacing;
            titleX = centerX + iconAreaWidth / 2;
        }

        // 安全访问配置字段
        String mainTitleText = config.mainTitle != null ? config.mainTitle : "服务器公告";

        // 使用 MutableText 创建丰富的主标题
        MutableText mainTitle = Text.literal(mainTitleText);
        if (config.useCustomRGB) {
            mainTitle = mainTitle.withColor(config.mainTitleColor);
        } else {
            Formatting formatting = getFormattingFromColor(config.mainTitleColor);
            mainTitle = mainTitle.formatted(formatting);
        }
        mainTitle = mainTitle.formatted(Formatting.BOLD);

        titleWidget = new TextWidget(titleX, 30, 200, 20, mainTitle, textRenderer);
        titleWidget.alignCenter();
        addDrawableChild(titleWidget);

        String subTitleText = config.subTitle != null ? config.subTitle : "最新通知";

        // 使用 MutableText 创建丰富的副标题
        MutableText subTitle = Text.literal(subTitleText);
        if (config.useCustomRGB) {
            subTitle = subTitle.withColor(config.subTitleColor);
        } else {
            Formatting formatting = getFormattingFromColor(config.subTitleColor);
            subTitle = subTitle.formatted(formatting);
        }

        subtitleWidget = new TextWidget(titleX, 55, 200, 20, subTitle, textRenderer);
        subtitleWidget.alignCenter();
        addDrawableChild(subtitleWidget);

        // 创建公告内容
        contentText = createAnnouncementContent();

        // 创建滚动文本部件
        // 简化颜色对比度检查
        if ((contentColor & 0xFFFFFF) == (0xB4303030 & 0xFFFFFF)) {
            contentColor = 0xFFFFFFFF; // 如果颜色太相似，使用白色
        }

        // 调试日志
        if (TestModClient.DEBUG_MODE) {
            TestModClient.LOGGER.info("公告内容: {}", contentText.getString());
            TestModClient.LOGGER.info("内容颜色: 0x{}", Integer.toHexString(contentColor));
        }

        scrollableText = new ScrollableTextWidget(
                centerX - 150, 80, 300, this.height - 150,
                contentText, textRenderer, client,
                contentColor
        );
        addDrawableChild(scrollableText);

        // 创建按钮
        createButtons(centerX, buttonWidth, buttonHeight, buttonY);
    }

    /*
    private MutableText createAnnouncementContent() {
        MutableText contentText = Text.empty();

        List<String> content = getStrings();

        for (int i = 0; i < content.size(); i++) {
            String line = content.get(i);

            // 解析包含Formatting代码的字符串
            MutableText lineText = parseFormattingCodes(line, config.contentColor, config.useCustomRGB);

            contentText.append(lineText);

            // 如果不是最后一行，添加换行符
            if (i < content.size() - 1) {
                contentText.append(Text.literal("\n"));
            }
        }

        if (TestModClient.DEBUG_MODE) {
            TestModClient.LOGGER.info("公告内容构建完成: {}", contentText.getString());
            TestModClient.LOGGER.info("公告内容行数: {}", content.size());
        }

        return contentText;
    }
    */

    /**
      * 把整段公告字符串 → MutableText
      * 支持：
      *  - §a §b … 原版颜色码
      *  - &#RRGGBB  自定义 RGB 颜色码
      *  - 未写颜色 → 使用 config.contentColor
     */
				private MutableText createAnnouncementContent() {
    MutableText root = Text.empty();
    List<String> src = getStrings();
    
    for (String line : src) {
        // 跳过空行
        if (line.trim().isEmpty()) {
            root.append(Text.literal("\n"));
            continue;
        }
        
        MutableText lineText = Text.empty();
        Pattern pattern = Pattern.compile("(&#[0-9a-fA-F]{6}|§[0-9a-fk-or]|[^&§]+)");
        Matcher m = pattern.matcher(line);
        Style currentStyle = Style.EMPTY;
        
        while (m.find()) {
            String segment = m.group();
            
            if (segment.startsWith("&#")) {
                // 处理自定义RGB颜色
                try {
                    int rgb = Integer.parseInt(segment.substring(2), 16);
                    currentStyle = currentStyle.withColor(rgb);
                } catch (NumberFormatException e) {
                    TestModClient.LOGGER.warn("无效的RGB颜色代码: {}", segment);
                }
            } else if (segment.startsWith("§")) {
                // 处理原版格式代码
                Formatting formatting = Formatting.byCode(segment.charAt(1));
                if (formatting != null) {
                    if (formatting == Formatting.RESET) {
                        currentStyle = Style.EMPTY;
                    } else if (formatting.isColor()) {
                        currentStyle = currentStyle.withColor(formatting);
                    } else {
                        currentStyle = currentStyle.withFormatting(formatting);
                    }
                }
            } else {
                // 普通文本，应用当前样式
                MutableText textSegment = Text.literal(segment).setStyle(currentStyle);
                lineText.append(textSegment);
            }
        }
        
        root.append(lineText).append(Text.literal("\n"));
    }
    
    return root;
}
/*
    private MutableText createAnnouncementContent() {
        MutableText root = Text.empty();
        List<String> src = getStrings();

        Pattern pattern = Pattern.compile("(&#[0-9a-fA-F]{6}|§[0-9a-fk-or]|[^&§\n]+|\n)");
        for (String raw : src) {
            Matcher m = pattern.matcher(raw);
            MutableText line = Text.empty();

            while (m.find()) {
                String seg = m.group();
                if (seg.startsWith("&#")) {
                    int rgb = Integer.parseInt(seg.substring(2), 16);
                    line.append(Text.literal("").setStyle(Style.EMPTY.withColor(rgb)));
                } else if (seg.startsWith("§")) {
                    Formatting fmt = Formatting.byCode(seg.charAt(1));
                    if (fmt != null) line.append(Text.literal("").setStyle(Style.EMPTY.withFormatting(fmt)));
                } else if (seg.equals("\n")) {
                    // 换行符已在根文本里处理
                } else {
                    line.append(Text.literal(seg));
                }
            }
            root.append(line).append(Text.literal("\n"));
        }
        return root;
    }
*/

//    /**
//     * 解析单行富文本
//     */
//    private MutableText parseRichText(String raw, int defaultCol, boolean useRgb) {
//        MutableText result = Text.empty();
//        // 1) 先按 &#RRGGBB 或 §x 分段
//        Pattern p = Pattern.compile("(&#[0-9a-fA-F]{6}|§[0-9a-fk-or]|[^&§]+)");
//        Matcher m = p.matcher(raw);
//        Style active = Style.EMPTY;
//
//        while (m.find()) {
//            String seg = m.group();
//            if (seg.startsWith("&#")) {                       // RGB 写法
//                int rgb = Integer.parseInt(seg.substring(2), 16);
//                active = Style.EMPTY.withColor(rgb);
//            } else if (seg.startsWith("§")) {                 // 原版颜色码
//                Formatting fmt = Formatting.byCode(seg.charAt(1));
//                if (fmt != null) active = Style.EMPTY.withFormatting(fmt);
//            } else {                                          // 普通文字
//                MutableText t = Text.literal(seg);
//                if (active == Style.EMPTY && !useRgb) {
//                    // 未指定颜色且不用 RGB → 使用默认 Formatting
//                    Formatting fmt = getFormattingFromColor(defaultCol);
//                    t = t.formatted(fmt);
//                } else if (active == Style.EMPTY && useRgb) {
//                    // 未指定颜色且启用 RGB → 使用默认 RGB
//                    t = t.withColor(defaultCol);
//                } else {
//                    t = t.setStyle(active);
//                }
//                result.append(t);
//            }
//        }
//        return result;
//    }




    private List<String> getStrings() {
        List<String> defaultContent = List.of(
                "§a欢迎游玩，我们团队做的模组！",
                " ",
                "§e一些提醒：",
                "§f1. 模组仅限于1.21.7~1.21.8fabric",
                "§f2. 模组目前是半成品",
                "§f3. 后面会继续更新",
                " ",
                "§b模组随缘更新",
                "§c若发现bug可以向模组作者或者仓库反馈！"
        );

        return config.announcementContent != null && !config.announcementContent.isEmpty() ?
                config.announcementContent : defaultContent;
    }

//    /**
//     * 解析包含Minecraft Formatting代码的字符串
//     * 支持 § 符号后跟颜色代码（如 §a 表示绿色）
//     *
//     * @param text 要解析的文本
//     * @param defaultColor 默认颜色值（十六进制整数格式，如0xFFFFFF）
//     * @param useCustomRGB 是否使用自定义RGB颜色
//     * @return 解析后的MutableText
//     */
//    private MutableText parseFormattingCodes(String text, int defaultColor, boolean useCustomRGB) {
//        MutableText result = Text.empty();
//        StringBuilder currentText = new StringBuilder();
//        Formatting currentFormatting = null;
//
//        for (int i = 0; i < text.length(); i++) {
//            char c = text.charAt(i);
//
//            if (c == '§' && i + 1 < text.length()) {
//                // 找到Formatting代码
//                char codeChar = text.charAt(i + 1);
//                Formatting formatting = Formatting.byCode(codeChar);
//
//                if (formatting != null) {
//                    // 添加当前文本（如果有）
//                    if (!currentText.isEmpty()) {
//                        MutableText segment = Text.literal(currentText.toString());
//                        if (currentFormatting != null) {
//                            segment = segment.formatted(currentFormatting);
//                        } else if (useCustomRGB) {
//                            segment = segment.withColor(defaultColor);
//                        } else {
//                            Formatting defaultFormatting = getFormattingFromColor(defaultColor);
//                            segment = segment.formatted(defaultFormatting);
//                        }
//                        result.append(segment);
//                        currentText.setLength(0);
//                    }
//
//                    currentFormatting = formatting.isColor() ? formatting : currentFormatting;
//                    i++; // 跳过格式代码
//                } else {
//                    // 无效的格式代码，当作普通文本处理
//                    currentText.append(c);
//                }
//            } else {
//                currentText.append(c);
//            }
//        }
//
//        // 添加剩余的文本
//        if (!currentText.isEmpty()) {
//            MutableText segment = Text.literal(currentText.toString());
//            if (currentFormatting != null) {
//                segment = segment.formatted(currentFormatting);
//            } else if (useCustomRGB) {
//                segment = segment.withColor(defaultColor);
//            } else {
//                Formatting defaultFormatting = getFormattingFromColor(defaultColor);
//                segment = segment.formatted(defaultFormatting);
//            }
//            result.append(segment);
//        }
//
//        return result;
//    }

    private void createButtons(int centerX, int buttonWidth, int buttonHeight, int buttonY) {
        // 使用 MutableText 创建按钮文本
        // 确定按钮使用配置的文本
        String confirmText = config.confirmButtonText != null ? config.confirmButtonText : "确定";
        MutableText confirmButtonText = Text.literal(confirmText);
        if (config.useCustomRGB) {
            confirmButtonText = confirmButtonText.withColor(0xFFFFFF); // 按钮文本使用白色
        } else {
            confirmButtonText = confirmButtonText.formatted(Formatting.WHITE);
        }

        // 确定按钮
        addDrawableChild(ButtonWidget.builder(confirmButtonText, button -> {
            if (this.client != null) {
                this.close();
            }
        }).dimensions(centerX - buttonWidth - 5, buttonY, buttonWidth, buttonHeight).build());

        // 前往投递按钮使用配置的文本
        String submitText = config.submitButtonText != null ? config.submitButtonText : "前往投递";
        MutableText submitButtonText = Text.literal(submitText);
        if (config.useCustomRGB) {
            submitButtonText = submitButtonText.withColor(0xFFFFFF); // 按钮文本使用白色
        } else {
            submitButtonText = submitButtonText.formatted(Formatting.WHITE);
        }

        String buttonLink = Objects.requireNonNullElse(config.buttonLink, "https://example.com");

        addDrawableChild(ButtonWidget.builder(submitButtonText, button -> {
            try {
                String url = buttonLink;
                if (!url.startsWith("http://") && !url.startsWith("https://")) {
                    url = "https://" + url;
                }
                Util.getOperatingSystem().open(new URI(url));
            } catch (Exception e) {
                if (this.client != null && this.client.player != null) {
                    this.client.player.sendMessage(Text.literal("无法打开链接: " + e.getMessage()), false);
                }
            }
        }).dimensions(centerX + 5, buttonY, buttonWidth, buttonHeight).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // 渲染背景
        if (config.useCustomAnnouncementBackground && backgroundTexture != null) {
            // 使用自定义背景图
            context.drawTexturedQuad(backgroundTexture, 0, 0, 0, 0, this.width, this.height, this.width, this.height);
        } else {
            // 使用与原版UI相似的颜色 #B4303030
            context.fill(0, 0, this.width, this.height, 0xB4303030);
        }

        // 绘制图标（如果有）
        if (config.showIcon && iconTexture != null) {
            int iconX = (this.width / 2) - 150 - config.iconWidth - config.iconTextSpacing;
            int iconY = 30;

            try {
                // 使用正确的DrawContext API绘制纹理
                context.drawTexturedQuad(
                        iconTexture,
                        iconX,
                        iconY,
                        0,
                        0,
                        config.iconWidth,
                        config.iconHeight,
                        config.iconWidth,
                        config.iconHeight
                );
            } catch (Exception e) {
                TestModClient.LOGGER.warn("无法绘制图标", e);
            }
        }

        // 先渲染标题
        renderTitle(context, mouseX, mouseY, delta);

        // 然后渲染其他部件（包括滚动文本）
        super.render(context, mouseX, mouseY, delta);

        // 滚动文本
        if (scrollableText != null && tickCount % 2 == 0) {
            double maxScroll = scrollableText.getTotalHeight() - scrollableText.getHeight();
            if (maxScroll > 0) {
                double scrollAmount = scrollableText.getScrollAmount() + (config.scrollSpeed / 20.0);
                if (scrollAmount > maxScroll) scrollAmount = 0;
                scrollableText.setScrollAmount(Math.min(scrollAmount, maxScroll));
            }
        }

        // 调试模式：绘制ScrollableTextWidget的边框
        if (TestModClient.DEBUG_MODE && scrollableText != null) {
            context.fill(
                    scrollableText.getX() - 1,
                    scrollableText.getY() - 1,
                    scrollableText.getX() + scrollableText.getWidth() + 1,
                    scrollableText.getY() + scrollableText.getHeight() + 1,
                    0x40FF00FF
            );
        }
    }

    private boolean isVersionCompatible() {
        try {
            Optional<ModContainer> minecraftContainer =
                    FabricLoader.getInstance().getModContainer("minecraft");

            if (minecraftContainer.isEmpty()) {
                TestModClient.LOGGER.warn("无法找到 Minecraft mod 容器");
                return true;
            }

            String version = minecraftContainer.get().getMetadata().getVersion().getFriendlyString();
            TestModClient.LOGGER.info("检测到 Minecraft 版本: {}", version);

            return isVersionAtLeast(version, 1, 21, 6);
        } catch (Exception e) {
            TestModClient.LOGGER.warn("无法确定 Minecraft 版本", e);
            return true;
        }
    }

    private boolean isVersionAtLeast(String versionString, int minMajor, int minMinor, int minPatch) {
        try {
            String[] parts = versionString.split("\\.");
            if (parts.length < 3) {
                return false;
            }

            int major = Integer.parseInt(parts[0]);
            int minor = Integer.parseInt(parts[1]);

            // 处理可能的版本后缀 (如 "1.21.8-fabric")
            String patchPart = parts[2].replaceAll("[^0-9].*", "");
            int patch = Integer.parseInt(patchPart);

            if (major > minMajor) return true;
            if (major < minMajor) return false;

            if (minor > minMinor) return true;
            if (minor < minMinor) return false;

            return patch >= minPatch;
        } catch (NumberFormatException e) {
            TestModClient.LOGGER.warn("无法解析版本号: {}", versionString, e);
            return false;
        }
    }

    private void renderTitle(DrawContext context, int mouseX, int mouseY, float delta) {
        if (TestModClient.DEBUG_MODE) {
            if (titleWidget != null) {
                drawDebugOutline(context, titleWidget, 0x40FF0000);
            }
            if (subtitleWidget != null) {
                drawDebugOutline(context, subtitleWidget, 0x4000FF00);
            }

            // 绘制图标区域调试框
            if (config.showIcon && iconTexture != null) {
                int iconX = (this.width / 2) - 150 - config.iconWidth - config.iconTextSpacing;
                int iconY = 30;

                context.fill(
                        iconX - 1,
                        iconY - 1,
                        iconX + config.iconWidth + 1,
                        iconY + config.iconHeight + 1,
                        0x400000FF
                );
            }
        }

        if (titleWidget != null) titleWidget.render(context, mouseX, mouseY, delta);
        if (subtitleWidget != null) subtitleWidget.render(context, mouseX, mouseY, delta);
    }

    private void drawDebugOutline(DrawContext context, ClickableWidget widget, int color) {
        context.fill(
                widget.getX() - 1,
                widget.getY() - 1,
                widget.getX() + widget.getWidth() + 1,
                widget.getY() + widget.getHeight() + 1,
                color
        );
    }

    @Override
    public void tick() {
        super.tick();
        tickCount++;
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return super.shouldCloseOnEsc();
    }

    // 辅助方法：从颜色值获取Formatting枚举
    private Formatting getFormattingFromColor(int color) {
        // 移除alpha通道，只比较RGB值
        int rgbColor = color & 0xFFFFFF;

        for (Formatting formatting : Formatting.values()) {
            if (formatting.isColor() && formatting.getColorValue() != null) {
                int formattingColor = formatting.getColorValue();
                if (formattingColor == rgbColor) {
                    return formatting;
                }
            }
        }

        // 如果没有找到匹配的颜色，返回默认值
        TestModClient.LOGGER.warn("未找到匹配的Formatting枚举，颜色: 0x{}", Integer.toHexString(color));
        return Formatting.WHITE;
    }

    // 辅助方法：从Formatting枚举获取颜色值
    private int getColorFromFormatting(Formatting formatting) {
        return formatting.getColorValue() != null ? formatting.getColorValue() : 0xFFFFFF;
    }
}