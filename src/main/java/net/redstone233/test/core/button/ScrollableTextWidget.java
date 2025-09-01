package net.redstone233.test.core.button;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.MutableText;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.Formatting;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.screen.narration.NarrationPart;
import net.minecraft.client.font.TextRenderer;
import net.redstone233.test.TestModClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ScrollableTextWidget extends ClickableWidget {
    private final MinecraftClient client;
    private final TextRenderer textRenderer;
    private final List<Text> textLines;
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
        this.color = color;
        this.textLines = new ArrayList<>(); // 改为存储Text对象
        this.scrollAmount = 0;
        this.totalHeight = 0;
        updateTextLines(); // 改为更新文本行
    }




    /*
    public void updateTextLines() {
        textLines.clear();

        if (getMessage() == null || getMessage().getString().isEmpty()) {
            textLines.add(Text.literal("暂无公告内容"));
            totalHeight = textRenderer.fontHeight;
            return;
        }

        try {
            // 按换行符分割文本，并保留格式
            String fullText = getMessage().getString();
            String[] lines = fullText.split("\n");

            // 解析每行的格式代码
            for (String line : lines) {
                textLines.add(parseFormattingCodes(line));
            }
            totalHeight = textLines.size() * textRenderer.fontHeight;

            if (TestModClient.DEBUG_MODE) {
                TestModClient.LOGGER.info("处理了 {} 行文本，总高度: {}", textLines.size(), totalHeight);
                for (int i = 0; i < textLines.size(); i++) {
                    TestModClient.LOGGER.info("行 {}: {}", i, textLines.get(i).getString());
                }
            }
        } catch (Exception e) {
            TestModClient.LOGGER.error("文本处理失败", e);
            textLines.add(Text.literal("文本渲染错误"));
            totalHeight = textRenderer.fontHeight;
        }
    }*/

    public void updateTextLines() {
        textLines.clear();

        try {
            if (getMessage() == null) {
                textLines.add(Text.literal("暂无公告内容"));
            } else {
                // 直接按换行符切分，保留 Text 对象本身（已含颜色）
                getMessage().visit((style, text) -> {
                    for (String line : text.split("\n")) {
                        textLines.add(Text.literal(line).setStyle(style));
                    }
                    return Optional.empty();
                }, Style.EMPTY);
            }

            if (TestModClient.DEBUG_MODE) {
                TestModClient.LOGGER.info("处理了 {} 行文本，总高度: {}", textLines.size(), totalHeight);
                for (int i = 0; i < textLines.size(); i++) {
                    TestModClient.LOGGER.info("行 {}: {}", i, textLines.get(i).getString());
                }
            }

            totalHeight = textLines.size() * textRenderer.fontHeight;
        } catch (Exception e) {
            TestModClient.LOGGER.error("文本处理失败", e);
            textLines.add(Text.literal("文本渲染错误"));
            totalHeight = textRenderer.fontHeight;
        }
    }

    /**
     * 解析包含Minecraft Formatting代码的字符串
     * 支持 § 符号后跟颜色代码（如 §a 表示绿色）
     *
     * @param text 要解析的文本
     * @return 解析后的Text对象
     */
    private Text parseFormattingCodes(String text) {
        MutableText result = Text.empty();
        StringBuilder currentText = new StringBuilder();
        net.minecraft.util.Formatting currentFormatting = null;

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);

            if (c == '§' && i + 1 < text.length()) {
                // 找到Formatting代码
                char codeChar = text.charAt(i + 1);
                Formatting formatting = Formatting.byCode(codeChar);

                if (formatting != null) {
                    // 添加当前文本（如果有）
                    if (!currentText.isEmpty()) {
                        MutableText segment = Text.literal(currentText.toString());
                        if (currentFormatting != null) {
                            segment = segment.formatted(currentFormatting);
                        }
                        result.append(segment);
                        currentText.setLength(0);
                    }

                    currentFormatting = formatting.isColor() ? formatting : currentFormatting;
                    i++; // 跳过格式代码
                } else {
                    // 无效的格式代码，当作普通文本处理
                    currentText.append(c);
                }
            } else {
                currentText.append(c);
            }
        }

        // 添加剩余的文本
        if (!currentText.isEmpty()) {
            MutableText segment = Text.literal(currentText.toString());
            if (currentFormatting != null) {
                segment = segment.formatted(currentFormatting);
            }
            result.append(segment);
        }

        return result;
    }

    @Override
    public void setMessage(Text message) {
        super.setMessage(message);
        updateTextLines();
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        // 绘制背景 - 改为与原版UI相似的半透明深色
        context.fill(getX(), getY(), getX() + width, getY() + height, 0x80404040);

        // 绘制边框
        context.fill(getX(), getY(), getX() + width, getY() + 1, 0xFF808080);
        context.fill(getX(), getY() + height - 1, getX() + width, getY() + height, 0xFF808080);
        context.fill(getX(), getY(), getX() + 1, getY() + height, 0xFF808080);
        context.fill(getX() + width - 1, getY(), getX() + width, getY() + height, 0xFF808080);

        // 启用裁剪
        int clipX = getX() + scrollbarPadding;
        int clipY = getY() + scrollbarPadding;
        int clipWidth = width - scrollbarWidth - scrollbarPadding * 2;
        int clipHeight = height - scrollbarPadding * 2;

        context.enableScissor(clipX, clipY, clipX + clipWidth, clipY + clipHeight);

        // 绘制文本 - 使用Text对象自身的颜色
        int yOffset = getY() + scrollbarPadding - (int) scrollAmount;
        for (Text line : textLines) {
            if (yOffset + textRenderer.fontHeight >= getY() && yOffset <= getY() + height) {
                // 使用Text对象自身的颜色，而不是固定颜色
                //context.drawText(textRenderer, line, getX() + scrollbarPadding, yOffset, -1, false);

										// 在renderWidget方法中，修改文本渲染部分
						context.drawText(textRenderer, line, getX() + scrollbarPadding, yOffset, 
                 line.getStyle().getColor() != null ? line.getStyle().getColor().getRgb() : color, 
                 false);
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