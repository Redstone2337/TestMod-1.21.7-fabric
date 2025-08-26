package net.redstone233.test.core.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.redstone233.test.TestModClient;
import net.redstone233.test.core.api.PlayerDataProvider;
import net.redstone233.test.core.button.ScrollableTextWidget;

import java.util.ArrayList;
import java.util.List;

public class EnhancedPlayerInfoScreen extends Screen {
    private final PlayerEntity player;
    private final PlayerDataProvider dataProvider;
    private Identifier backgroundTexture;

    // 动画相关变量
    private int lastLevel;
    private int animationTicks;
    private float levelScale = 1.0f;
    private boolean isAnimating = false;

    // 鼠标悬停检测
    private int hoveredExpBarX;
    private int hoveredExpBarY;
    private int hoveredExpBarWidth;
    private boolean isExpBarHovered = false;

    private TextWidget subtitleWidget;
    private ScrollableTextWidget contentWidget;
    private int tickCount = 0;

    public EnhancedPlayerInfoScreen(PlayerEntity player, PlayerDataProvider dataProvider) {
        super(Text.literal(""));
        this.player = player;
        this.dataProvider = dataProvider;
        this.lastLevel = dataProvider.getLevel(); // 初始化记录当前等级

        // 加载背景纹理（如果启用自定义背景）
        if (TestModClient.CONFIG != null && TestModClient.CONFIG.useCustomPlayerInfoBackground &&
                TestModClient.CONFIG.playerInfoBackgroundPath != null && !TestModClient.CONFIG.playerInfoBackgroundPath.isEmpty()) {
            try {
                backgroundTexture = Identifier.of(TestModClient.CONFIG.playerInfoBackgroundPath);
                // 检查纹理是否存在
                MinecraftClient client = MinecraftClient.getInstance();
                if (client != null && client.getResourceManager().getResource(backgroundTexture).isEmpty()) {
                    TestModClient.LOGGER.warn("玩家信息背景纹理不存在: {}", TestModClient.CONFIG.playerInfoBackgroundPath);
                    backgroundTexture = null;
                }
            } catch (Exception e) {
                TestModClient.LOGGER.warn("无法加载玩家信息背景纹理: {}", TestModClient.CONFIG.playerInfoBackgroundPath, e);
                backgroundTexture = null;
            }
        }
    }

    @Override
    protected void init() {
        super.init();

        int centerX = this.width / 2;

        // 创建标题（玩家名称）
        MutableText playerName = player.getName().copy();
        if (dataProvider.isSVip()) {
            // 如果同时有SVIP和VIP，优先显示SVIP颜色（金色）
            playerName = playerName.formatted(Formatting.GOLD, Formatting.BOLD);
        } else if (dataProvider.isVip()) {
            playerName = playerName.formatted(Formatting.AQUA, Formatting.BOLD);
        } else {
            playerName = playerName.formatted(Formatting.WHITE, Formatting.BOLD);
        }

        // 类似于AnnouncementScreen的UI元素
        TextWidget titleWidget = new TextWidget(centerX, 30, 200, 20, playerName, textRenderer);
        titleWidget.alignCenter();
        addDrawableChild(titleWidget);

        // 创建副标题（玩家等级）
        MutableText levelText = Text.translatable("gui.playermod.level", dataProvider.getLevel())
                .formatted(Formatting.YELLOW);
        subtitleWidget = new TextWidget(centerX, 55, 200, 20, levelText, textRenderer);
        subtitleWidget.alignCenter();
        addDrawableChild(subtitleWidget);

        // 创建内容区域
        MutableText contentText = createContentText();
        contentWidget = new ScrollableTextWidget(
                centerX - 150, 80, 300, this.height - 150,
                contentText, textRenderer, client,
                0xFFFFFFFF // 白色文本
        );
        addDrawableChild(contentWidget);

        // 添加关闭按钮
        this.addDrawableChild(ButtonWidget.builder(Text.translatable("gui.close"), button -> this.close())
                .dimensions(this.width / 2 - 50, this.height - 30, 100, 20)
                .build());
    }

    private MutableText createContentText() {
        MutableText content = Text.empty();

        // 检查会员状态
        boolean isSVip = dataProvider.isSVip();
        boolean isVip = dataProvider.isVip();

        // VIP状态显示
        if (isSVip) {
            content.append(Text.translatable("gui.playermod.svip_status")
                    .formatted(Formatting.GOLD, Formatting.BOLD));

            // 如果同时有VIP，在SVIP下面添加VIP状态
            if (isVip) {
                content.append(Text.literal("\n"));
                content.append(Text.translatable("gui.playermod.vip_status")
                        .formatted(Formatting.AQUA, Formatting.BOLD));
            }
        } else if (isVip) {
            content.append(Text.translatable("gui.playermod.vip_status")
                    .formatted(Formatting.AQUA, Formatting.BOLD));
        } else {
            content.append(Text.translatable("gui.playermod.non_vip_status")
                    .formatted(Formatting.GRAY));
        }

        content.append(Text.literal("\n\n"));

        // 经验信息
        content.append(Text.translatable("gui.playermod.experience",
                        dataProvider.getExperience(), dataProvider.getTotalExpForNextLevel())
                .formatted(Formatting.GREEN));

        content.append(Text.literal("\n\n"));

        // 下一级所需经验
        content.append(Text.translatable("gui.playermod.next_level_exp",
                        dataProvider.getRemainingExpForNextLevel())
                .formatted(Formatting.BLUE));

        return content;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // 渲染背景 - 使用与AnnouncementScreen相同的样式
        if (TestModClient.CONFIG != null && TestModClient.CONFIG.useCustomPlayerInfoBackground && backgroundTexture != null) {
            // 使用自定义背景图
            context.drawTexturedQuad(backgroundTexture, 0, 0, 0, 0, this.width, this.height, this.width, this.height);
        } else {
            // 使用与原版UI相似的颜色 #B4303030
            context.fill(0, 0, this.width, this.height, 0xB4303030);
        }

        // 检查等级是否提升
        checkLevelUp();

        // 更新动画
        updateAnimation();

        // 检查鼠标悬停
        checkHover(mouseX, mouseY);

        // 更新副标题（等级）的动画效果
        if (subtitleWidget != null) {
            MutableText levelText = Text.translatable("gui.playermod.level", dataProvider.getLevel())
                    .formatted(Formatting.YELLOW);

            // 应用动画缩放
            context.getMatrices().pushMatrix();
            context.getMatrices().translate(subtitleWidget.getX() + (float) subtitleWidget.getWidth() / 2,
                    subtitleWidget.getY() + (float) subtitleWidget.getHeight() / 2);
            context.getMatrices().scale(levelScale, levelScale);

            // 绘制带动画的等级文本
            int textWidth = textRenderer.getWidth(levelText);
            context.drawText(textRenderer, levelText, -textWidth / 2, -5, 0xFFFFFF, true);
            context.getMatrices().popMatrix();
        }

        // 绘制经验条（在内容区域下方）
        int centerX = this.width / 2;
        int expBarY = this.height - 80;

        // 绘制经验条
        int expBarWidth = 150;
        int expBarX = centerX - expBarWidth / 2;
        hoveredExpBarX = expBarX;
        hoveredExpBarY = expBarY;
        hoveredExpBarWidth = expBarWidth;

        // 经验条背景
        context.fill(expBarX, expBarY, expBarX + expBarWidth, expBarY + 10, 0xFF555555);

        // 经验条进度
        float progress = dataProvider.getExperienceProgress();
        int progressWidth = (int)(expBarWidth * progress);
        if (progressWidth > 0) {
            // 根据进度使用不同颜色
            int color = progress >= 0.9f ? 0xFFFFFF00 : // 接近满时黄色
                    progress >= 0.7f ? 0xFF00FF00 : // 高进度绿色
                            0xFF00AA00; // 普通进度深绿色
            context.fill(expBarX, expBarY, expBarX + progressWidth, expBarY + 10, color);
        }

        // 经验条边框
        context.drawBorder(expBarX, expBarY, expBarWidth, 10, 0xFF000000);

        // 绘制经验百分比
        String percentText = String.format("%.1f%%", progress * 100);
        context.drawCenteredTextWithShadow(textRenderer, Text.literal(percentText),
                centerX, expBarY + 12, 0xDDDDDD);

        // 绘制鼠标悬停提示
        if (isExpBarHovered) {
            drawExpTooltip(context, mouseX, mouseY, textRenderer);
        }

        // 渲染其他UI元素
        super.render(context, mouseX, mouseY, delta);

        // 自动滚动内容
        if (contentWidget != null && tickCount % 2 == 0) {
            double maxScroll = contentWidget.getTotalHeight() - contentWidget.getHeight();
            if (maxScroll > 0) {
                double scrollAmount = contentWidget.getScrollAmount() + (0.5 / 20.0);
                if (scrollAmount > maxScroll) scrollAmount = 0;
                contentWidget.setScrollAmount(Math.min(scrollAmount, maxScroll));
            }
        }
    }

    // 检查等级是否提升
    private void checkLevelUp() {
        int currentLevel = dataProvider.getLevel();
        if (currentLevel > lastLevel) {
            // 等级提升，触发动画
            isAnimating = true;
            animationTicks = 0;
            lastLevel = currentLevel;
        }
    }

    // 更新动画
    private void updateAnimation() {
        if (isAnimating) {
            animationTicks++;

            // 简单的缩放动画：先放大再缩小
            if (animationTicks < 10) {
                // 放大阶段
                levelScale = 1.0f + (animationTicks / 10.0f) * 0.5f;
            } else if (animationTicks < 20) {
                // 缩小阶段
                levelScale = 1.5f - ((animationTicks - 10) / 10.0f) * 0.5f;
            } else {
                // 动画结束
                levelScale = 1.0f;
                isAnimating = false;
            }
        }
    }

    // 检查鼠标悬停
    private void checkHover(int mouseX, int mouseY) {
        isExpBarHovered = mouseX >= hoveredExpBarX &&
                mouseX <= hoveredExpBarX + hoveredExpBarWidth &&
                mouseY >= hoveredExpBarY &&
                mouseY <= hoveredExpBarY + 10;
    }

    // 绘制经验条提示信息
    private void drawExpTooltip(DrawContext context, int mouseX, int mouseY, TextRenderer textRenderer) {
        List<Text> tooltip = new ArrayList<>();

        // 添加基本信息
        tooltip.add(Text.translatable("gui.playermod.tooltip.exp_info")
                .formatted(Formatting.GRAY));

        // 添加下一级所需经验
        int remainingExp = dataProvider.getRemainingExpForNextLevel();
        tooltip.add(Text.translatable("gui.playermod.tooltip.exp_remaining", remainingExp)
                .formatted(Formatting.YELLOW));

        // 添加经验倍率
        double multiplier = dataProvider.getExpMultiplier();
        tooltip.add(Text.translatable("gui.playermod.tooltip.exp_multiplier", String.format("%.2f", multiplier))
                .formatted(Formatting.GREEN));

        // 添加基础经验需求
        int baseExp = dataProvider.getBaseExpForNextLevel();
        tooltip.add(Text.translatable("gui.playermod.tooltip.base_exp", baseExp)
                .formatted(Formatting.BLUE));

        // 绘制工具提示
        context.drawTooltip(textRenderer, tooltip, mouseX, mouseY);
    }

    @Override
    public void tick() {
        super.tick();
        tickCount++;
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}