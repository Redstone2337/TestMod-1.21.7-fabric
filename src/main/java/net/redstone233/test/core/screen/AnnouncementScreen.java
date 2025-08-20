// AnnouncementScreen.java
package net.redstone233.test.core.screen;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.redstone233.test.TestModClient;
import net.redstone233.test.core.button.ScrollableTextWidget;
import net.redstone233.test.core.config.ConfigInitializer;
import net.redstone233.test.core.config.ModConfig;

import java.net.URI;
import java.util.Objects;
import java.util.Optional;

public class AnnouncementScreen extends Screen {
    private final ModConfig config;
    private ScrollableTextWidget scrollableText;
    private int tickCount = 0;
    private Identifier iconTexture;

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

        int centerX = this.width / 2;
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
            } catch (Exception e) {
                TestModClient.LOGGER.warn("无法加载图标纹理: " + config.iconPath, e);
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
        int mainTitleColor = config.mainTitleColor;

        Text mainTitle = Text.literal(mainTitleText)
                .formatted(Formatting.BOLD)
                .withColor(mainTitleColor);

        titleWidget = new TextWidget(titleX, 30, 200, 20, mainTitle, textRenderer);
        titleWidget.alignCenter();
        addDrawableChild(titleWidget);

        String subTitleText = config.subTitle != null ? config.subTitle : "最新通知";
        int subTitleColor = config.subTitleColor;

        Text subTitle = Text.literal(subTitleText)
                .withColor(subTitleColor);

        subtitleWidget = new TextWidget(titleX, 55, 200, 20, subTitle, textRenderer);
        subtitleWidget.alignCenter();
        addDrawableChild(subtitleWidget);

        // 滚动公告内容
        StringBuilder content = new StringBuilder();
        if (config.announcementContent != null) {
            for (String line : config.announcementContent) {
                content.append(line).append("\n");
            }
        } else {
            content.append("欢迎来到服务器！").append("\n");
        }

        int contentColor = config.contentColor;
        Text contentText = Text.literal(content.toString().trim())
                .withColor(contentColor);

        scrollableText = new ScrollableTextWidget(
                centerX - 150, 80, 300, this.height - 150,
                contentText, textRenderer, client, contentColor  // 传递颜色参数
        );
        addDrawableChild(scrollableText);

        // 确定按钮
        addDrawableChild(ButtonWidget.builder(Text.literal("确定"), button -> {
            if (this.client != null) {
                this.close();
            }
        }).dimensions(centerX - buttonWidth - 5, buttonY, buttonWidth, buttonHeight).build());

        // 前往投递按钮
        String buttonText = config.buttonText != null ? config.buttonText : "前往投递";
        String buttonLink = Objects.requireNonNullElse(config.buttonLink, "https://example.com");

        addDrawableChild(ButtonWidget.builder(Text.literal(buttonText), button -> {
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
        this.renderInGameBackground(context);

        // 绘制图标（如果有）
        if (config.showIcon && iconTexture != null) {
            int iconX = (this.width / 2) - 150 - config.iconWidth - config.iconTextSpacing;
            int iconY = 30;

            // 使用正确的DrawContext API绘制纹理
            context.drawTexture(
                    RenderPipelines.GUI_TEXTURED,
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
        }

        this.renderTitle(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);

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
}