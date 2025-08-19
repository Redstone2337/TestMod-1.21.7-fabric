package net.redstone233.test.core.button;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;

import java.util.ArrayList;
import java.util.List;

public class ScrollableTextWidget extends ClickableWidget {
    private final MinecraftClient client;
    private final List<OrderedText> wrappedLines;
    public double scrollAmount;
    private boolean scrolling;
    public int totalHeight;
    private final int scrollbarWidth = 6;
    private final int scrollbarPadding = 2;

    public ScrollableTextWidget(int x, int y, int width, int height, Text message, MinecraftClient client) {
        super(x, y, width, height, message);
        this.client = client;
        this.wrappedLines = new ArrayList<>();
        this.scrollAmount = 0;
        this.totalHeight = 0;
        updateWrappedLines();
    }

    public void updateWrappedLines() {
        wrappedLines.clear();
        List<Text> lines = new ArrayList<>();
        for (String line : getMessage().getString().split("\n")) {
            lines.add(Text.literal(line));
        }

        for (Text line : lines) {
            wrappedLines.addAll(client.textRenderer.wrapLines(line, width - scrollbarWidth - scrollbarPadding * 2));
        }

        totalHeight = wrappedLines.size() * client.textRenderer.fontHeight;
    }

    @Override
    public void setMessage(Text message) {
        super.setMessage(message);
        updateWrappedLines();
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {

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
        // 绘制背景
        context.fill(getX(), getY(), getX() + width, getY() + height, 0x80000000);

        // 启用裁剪
        context.enableScissor(getX() + 1, getY() + 1, getX() + width - scrollbarWidth - 1, getY() + height - 1);

        // 绘制文本
        int yOffset = getY() + 5 - (int) scrollAmount;
        for (OrderedText line : wrappedLines) {
            if (yOffset + client.textRenderer.fontHeight >= getY() && yOffset <= getY() + height) {
                context.drawText(client.textRenderer, line, getX() + 5, yOffset, 0xFFFFFF, false);
            }
            yOffset += client.textRenderer.fontHeight;
        }

        // 禁用裁剪
        context.disableScissor();

        // 绘制滚动条
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
}
