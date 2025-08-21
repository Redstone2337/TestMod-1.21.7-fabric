package net.redstone233.test.core.button;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.screen.narration.NarrationPart;
import net.minecraft.text.OrderedText;
import net.minecraft.client.font.TextRenderer;

import java.util.ArrayList;
import java.util.List;

public class ScrollableTextWidget extends ClickableWidget {
    private final MinecraftClient client;
    private final TextRenderer textRenderer;
    private final List<OrderedText> wrappedLines;
    private double scrollAmount;
    private boolean scrolling;
    private int totalHeight;
    private final int scrollbarWidth = 6;
    private final int scrollbarPadding = 2;
    private final int color; // 颜色字段

    public ScrollableTextWidget(int x, int y, int width, int height, Text message, TextRenderer textRenderer, MinecraftClient client, int color) {
        super(x, y, width, height, message);
        this.client = client;
        this.textRenderer = textRenderer;
        this.color = color; // 初始化颜色
        this.wrappedLines = new ArrayList<>();
        this.scrollAmount = 0;
        this.totalHeight = 0;
        updateWrappedLines();
    }

    public void updateWrappedLines() {
        wrappedLines.clear();

        // 检查消息是否为空
        if (getMessage() == null || getMessage().getString().isEmpty()) {
            // 添加默认文本
            wrappedLines.add(Text.literal("暂无公告内容").asOrderedText());
            totalHeight = textRenderer.fontHeight;
            return;
        }

        // 直接包装整个文本，保留样式
        wrappedLines.addAll(textRenderer.wrapLines(getMessage(), width - scrollbarWidth - scrollbarPadding * 2));
        totalHeight = wrappedLines.size() * textRenderer.fontHeight;
    }

    @Override
    public void setMessage(Text message) {
        super.setMessage(message);
        updateWrappedLines();
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {
        builder.put(NarrationPart.TITLE, Text.translatable("narration.scrollable_text", this.getMessage()));
        builder.put(NarrationPart.USAGE, Text.translatable("narration.scrollable_text.usage"));
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (this.visible && this.active && scrolling && totalHeight > height) {
            double maxScroll = totalHeight - height;
            double relativeY = mouseY - getY();
            scrollAmount = (relativeY / height) * maxScroll;
            scrollAmount = MathHelper.clamp(scrollAmount, 0, maxScroll);
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (this.visible && this.active && totalHeight > height) {
            double maxScroll = totalHeight - height;
            scrollAmount = MathHelper.clamp(scrollAmount - verticalAmount * 20, 0, maxScroll);
            return true;
        }
        return false;
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        // 绘制背景 - 改为灰色半透明
        context.fill(getX(), getY(), getX() + width, getY() + height, 0x80A0A0A0);

        // 启用裁剪 - 修正裁剪区域
        context.enableScissor(getX(), getY(), getX() + width - scrollbarWidth, getY() + height);

        // 绘制文本
        int yOffset = getY() - (int) scrollAmount;
        for (OrderedText line : wrappedLines) {
            // 检查行是否在可见区域内
            if (yOffset + textRenderer.fontHeight >= getY() && yOffset <= getY() + height) {
                context.drawText(textRenderer, line, getX() + 5, yOffset, color, false);
            }
            yOffset += textRenderer.fontHeight;
        }

        // 禁用裁剪
        context.disableScissor();

        // 绘制滚动条（如果需要）
        drawScrollbar(context);
    }

    private void drawScrollbar(DrawContext context) {
        if (totalHeight > height) {
            int scrollbarHeight = (int) ((float) height * height / totalHeight);
            scrollbarHeight = Math.max(scrollbarHeight, 20);

            int scrollbarY = (int) (getY() + scrollAmount * (height - scrollbarHeight) / (totalHeight - height));
            scrollbarY = MathHelper.clamp(scrollbarY, getY(), getY() + height - scrollbarHeight);

            // 滚动条背景
            context.fill(getX() + width - scrollbarWidth, getY(),
                    getX() + width, getY() + height,
                    0x55AAAAAA);

            // 滚动条滑块
            context.fill(getX() + width - scrollbarWidth + 1, scrollbarY,
                    getX() + width - 1, scrollbarY + scrollbarHeight,
                    0xFF888888);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.visible && this.active && button == 0) {
            // 检查是否点击了滚动条区域
            if (mouseX >= getX() + width - scrollbarWidth && mouseX <= getX() + width) {
                scrolling = true;
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        scrolling = false;
        return super.mouseReleased(mouseX, mouseY, button);
    }

    public double getScrollAmount() {
        return scrollAmount;
    }

    public void setScrollAmount(double scrollAmount) {
        this.scrollAmount = scrollAmount;
    }

    public int getTotalHeight() {
        return totalHeight;
    }

    public int getHeight() {
        return super.getHeight();
    }

    public MinecraftClient getClient() {
        return client;
    }

    public int getColor() {
        return color;
    }

    public int getScrollbarPadding() {
        return scrollbarPadding;
    }
}