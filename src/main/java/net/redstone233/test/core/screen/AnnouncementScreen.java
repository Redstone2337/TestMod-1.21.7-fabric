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

public class AnnouncementScreen extends Screen {
    private final ModConfig config;
    private ScrollableTextWidget scrollableText;
    private int tickCount = 0;

    public AnnouncementScreen() {
        super(Text.literal("Announcement Screen"));
        this.config = TestModClient.CONFIG;
    }

    @Override
    protected void init() {
        super.init();

        int centerX = this.width / 2;
        int buttonWidth = 100;
        int buttonHeight = 20;
        int buttonY = this.height - 30;

        // 大标题 - 直接使用整数颜色
        Text mainTitle = Text.literal(config.mainTitle)
                .formatted(Formatting.BOLD)
                .withColor(config.mainTitleColor);

        TextWidget titleWidget = new TextWidget(centerX, 30, 200, 20, mainTitle, textRenderer);
        titleWidget.alignCenter();
        addDrawableChild(titleWidget);

        // 小标题 - 直接使用整数颜色
        Text subTitle = Text.literal(config.subTitle)
                .withColor(config.subTitleColor);

        TextWidget subtitleWidget = new TextWidget(centerX, 55, 200, 20, subTitle, textRenderer);
        subtitleWidget.alignCenter();
        addDrawableChild(subtitleWidget);

        // 滚动公告区域 - 直接使用整数颜色
        StringBuilder content = new StringBuilder();
        for (String line : config.announcementContent) {
            content.append(line).append("\n");
        }
        Text contentText = Text.literal(content.toString().trim())
                .withColor(config.contentColor);

        scrollableText = new ScrollableTextWidget(
                centerX - 150, 80, 300, this.height - 150,
                contentText, client
        );
        addDrawableChild(scrollableText);

        // 确定按钮
        addDrawableChild(ButtonWidget.builder(Text.literal("确定"), button -> {
            this.close();
        }).dimensions(centerX - buttonWidth - 5, buttonY, buttonWidth, buttonHeight).build());

        // 前往投递按钮
        addDrawableChild(ButtonWidget.builder(Text.literal(config.buttonText), button -> {
            try {
                Util.getOperatingSystem().open(new URI(config.buttonLink));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).dimensions(centerX + 5, buttonY, buttonWidth, buttonHeight).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context,mouseX,mouseY,20);
        super.render(context, mouseX, mouseY, delta);

        // 自动滚动
        if (tickCount % 2 == 0) {
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
