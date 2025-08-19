package net.redstone233.test.core.screen;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Util;
import net.redstone233.test.TestModClient;
import net.redstone233.test.core.button.ScrollableTextWidget;
import net.redstone233.test.core.config.ModConfig;

import java.net.URI;
import java.util.Collections;

public class AnnouncementScreen extends Screen {
    private final ModConfig config;
    private ScrollableTextWidget scrollableText;
    private int tickCount = 0;

    // 添加文本组件引用
    private TextWidget titleWidget;
    private TextWidget subtitleWidget;

    public AnnouncementScreen() {
        super(Text.literal("Announcement Screen"));
        // 安全获取配置，如果为空则使用默认值
        if (TestModClient.CONFIG != null) {
            this.config = TestModClient.CONFIG;
        } else {
            // 创建默认配置作为回退
            this.config = new ModConfig();
        }
    }

    @Override
    protected void init() {
        super.init();

        int centerX = this.width / 2;
        int buttonWidth = 100;
        int buttonHeight = 20;
        int buttonY = this.height - 30;

        // 安全访问配置字段，提供默认值
        String mainTitleText = config.mainTitle != null ? config.mainTitle : "服务器公告";
        int mainTitleColor = config.mainTitleColor;

        Text mainTitle = Text.literal(mainTitleText)
                .formatted(Formatting.BOLD)
                .withColor(mainTitleColor);

        // 使用类变量存储文本组件
        titleWidget = new TextWidget(centerX, 30, 200, 20, mainTitle, textRenderer);
        titleWidget.alignCenter();
        addDrawableChild(titleWidget);

        // 副标题 - 添加空检查
        String subTitleText = config.subTitle != null ? config.subTitle : "最新通知";
        int subTitleColor = config.subTitleColor;

        Text subTitle = Text.literal(subTitleText)
                .withColor(subTitleColor);

        // 使用类变量存储文本组件
        subtitleWidget = new TextWidget(centerX, 55, 200, 20, subTitle, textRenderer);
        subtitleWidget.alignCenter();
        addDrawableChild(subtitleWidget);

        // 滚动公告区域 - 添加空检查
        StringBuilder content = new StringBuilder();
        if (config.announcementContent != null) {
            for (String line : config.announcementContent) {
                content.append(line).append("\n");
            }
        } else {
            // 默认内容
            content.append("欢迎来到服务器！").append("\n");
        }

        int contentColor = config.contentColor;
        Text contentText = Text.literal(content.toString().trim())
                .withColor(contentColor);

        scrollableText = new ScrollableTextWidget(
                centerX - 150, 80, 300, this.height - 150,
                contentText, client
        );
        addDrawableChild(scrollableText);

        // 确定按钮 - 修复关闭功能
        addDrawableChild(ButtonWidget.builder(Text.literal("确定"), button -> {
            if (this.client != null && this.client.player != null) {
                this.client.player.closeScreen();
            }
            this.close();
        }).dimensions(centerX - buttonWidth - 5, buttonY, buttonWidth, buttonHeight).build());

        // 前往投递按钮 - 修复链接打开功能
        String buttonText = config.buttonText != null ? config.buttonText : "前往投递";
        String buttonLink = config.buttonLink != null ? config.buttonLink : "https://example.com";

        addDrawableChild(ButtonWidget.builder(Text.literal(buttonText), button -> {
            try {
                // 确保链接格式正确
                String url = buttonLink;
                if (!url.startsWith("http://") && !url.startsWith("https://")) {
                    url = "https://" + url;
                }
                Util.getOperatingSystem().open(new URI(url));
            } catch (Exception e) {
                // 打印错误信息到控制台
                System.err.println("无法打开链接: " + buttonLink);
                e.printStackTrace();

                // 在游戏中显示错误消息
                if (this.client != null && this.client.player != null) {
                    this.client.player.sendMessage(Text.literal("无法打开链接: " + e.getMessage()), false);
                }
            }
        }).dimensions(centerX + 5, buttonY, buttonWidth, buttonHeight).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // 修复背景渲染
        this.renderInGameBackground(context);

        // 手动渲染标题以确保显示
        this.renderTitle(context, mouseX, mouseY, delta);

        super.render(context, mouseX, mouseY, delta);

        // 自动滚动 - 添加空检查
        if (scrollableText != null && tickCount % 2 == 0) {
            double maxScroll = scrollableText.totalHeight - scrollableText.getHeight();
            if (maxScroll > 0) {
                double scrollAmount = scrollableText.scrollAmount + (config.scrollSpeed / 20.0);
                if (scrollAmount > maxScroll) scrollAmount = 0;
                scrollableText.scrollAmount = Math.min(scrollAmount, maxScroll);
            }
        }
    }

    // 修复渲染标题方法
    private void renderTitle(DrawContext context, int mouseX, int mouseY, float delta) {
        if (TestModClient.DEBUG_MODE) {
            // 绘制组件边界
            if (titleWidget != null) {
                drawDebugOutline(context, titleWidget, 0x40FF0000); // 红色半透明
            }
            if (subtitleWidget != null) {
                drawDebugOutline(context, subtitleWidget, 0x4000FF00); // 绿色半透明
            }
        }

        // 正常渲染
        if (titleWidget != null) titleWidget.render(context, mouseX, mouseY, delta);
        if (subtitleWidget != null) subtitleWidget.render(context, mouseX, mouseY, delta);
    }

    // 新增调试轮廓绘制方法
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
        return true; // 允许按ESC键关闭屏幕
    }
}