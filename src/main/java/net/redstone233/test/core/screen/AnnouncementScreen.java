package net.redstone233.test.core.screen;


import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Util;
import net.redstone233.test.TestModClient;
import net.redstone233.test.core.button.ScrollableTextWidget;
import net.redstone233.test.core.config.ModConfig;

import java.net.URI;
import java.util.Objects;

public class AnnouncementScreen extends Screen {
    private final ModConfig config;
    private ScrollableTextWidget scrollableText;
    private int tickCount = 0;

    public AnnouncementScreen() {
        super(Text.literal("Announcement Screen"));
        // 安全获取配置，如果为空则使用默认值
        // 创建默认配置作为回退
        this.config = Objects.requireNonNullElseGet(TestModClient.CONFIG, ModConfig::new);
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

        TextWidget titleWidget = new TextWidget(centerX, 30, 200, 20, mainTitle, textRenderer);
        titleWidget.alignCenter();
        addDrawableChild(titleWidget);

        // 副标题 - 添加空检查
        String subTitleText = config.subTitle != null ? config.subTitle : "最新通知";
        int subTitleColor = config.subTitleColor;

        Text subTitle = Text.literal(subTitleText)
                .withColor(subTitleColor);

        TextWidget subtitleWidget = new TextWidget(centerX, 55, 200, 20, subTitle, textRenderer);
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

        // 确定按钮
        addDrawableChild(ButtonWidget.builder(Text.literal("确定"), button -> {
            this.close();
        }).dimensions(centerX - buttonWidth - 5, buttonY, buttonWidth, buttonHeight).build());

        // 前往投递按钮 - 添加空检查
        String buttonText = config.buttonText != null ? config.buttonText : "前往投递";
        String buttonLink = config.buttonLink != null ? config.buttonLink : "https://example.com";

        addDrawableChild(ButtonWidget.builder(Text.literal(buttonText), button -> {
            try {
                Util.getOperatingSystem().open(new URI(buttonLink));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).dimensions(centerX + 5, buttonY, buttonWidth, buttonHeight).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // 修复背景渲染方法调用
        renderBackground(context,mouseX,mouseY,20); // 移除多余的参数
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

    @Override
    public void tick() {
        super.tick();
        tickCount++;
    }
}
