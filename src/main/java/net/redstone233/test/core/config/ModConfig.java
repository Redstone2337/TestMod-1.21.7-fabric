package net.redstone233.test.core.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;
import net.redstone233.test.TestModClient;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Config(name = "testmod")
public class ModConfig implements ConfigData {
    @ConfigEntry.Gui.Tooltip(count = 2)
    @Comment("主标题文本")
    public String mainTitle = "模组公告";

    @ConfigEntry.Gui.Tooltip
    @Comment("副标题文本")
    public String subTitle = "请仔细观看";

    @ConfigEntry.Gui.Tooltip
    @Comment("公告内容（每行一条，支持多行）")
    public List<String> announcementContent = Arrays.asList(
            "欢迎游玩，我们团队做的模组！",
            " ",
            "一些提醒：",
            "1. 模组仅限于1.21.7~1.21.8fabric",
            "2. 模组目前是半成品",
            "3. 后面会继续更新",
            " ",
            "模组随缘更新",
            "若发现bug可以向模组作者或者仓库反馈！"
    );

    @ConfigEntry.Gui.Tooltip
    @Comment("按钮文本")
    public String buttonText = "前往投递";

    @ConfigEntry.Gui.Tooltip
    @Comment("按钮点击后打开的链接")
    public String buttonLink = "https://github.com/Redstone2337/TestMod-1.21.7-fabric/issues";

    @ConfigEntry.Gui.Tooltip
    @Comment("文本滚动速度 (像素/秒)")
    public int scrollSpeed = 30;

    @ConfigEntry.Gui.Tooltip
    @Comment("主标题颜色 (RGB整数)")
    public int mainTitleColor = 0xFFD700;

    @ConfigEntry.Gui.Tooltip
    @Comment("副标题颜色 (RGB整数)")
    public int subTitleColor = 0xFFFFFF;

    @ConfigEntry.Gui.Tooltip
    @Comment("公告内容颜色 (RGB整数)")
    public int contentColor = 0x0610EA;

    @ConfigEntry.Gui.Tooltip
    @Comment("启用调试模式（显示UI边界等辅助信息）")
    public boolean debugMode = false;

    // 新增配置选项
    // 修改图标显示默认值为false
    @ConfigEntry.Gui.Tooltip
    @Comment("是否显示公告图标")
    public boolean showIcon = false;

    @ConfigEntry.Gui.Tooltip
    @Comment("图标资源路径 (例如: testmod:textures/gui/icon.png)")
    public String iconPath = "testmod:textures/gui/announcement_icon.png";

    @ConfigEntry.Gui.Tooltip
    @Comment("图标宽度 (像素)")
    public int iconWidth = 32;

    @ConfigEntry.Gui.Tooltip
    @Comment("图标高度 (像素)")
    public int iconHeight = 32;

    @ConfigEntry.Gui.Tooltip
    @Comment("图标与文本的间距 (像素)")
    public int iconTextSpacing = 10;

    // 向后兼容字段
    @Deprecated
    public String oldMainTitleColor;
    @Deprecated
    public String oldSubTitleColor;
    @Deprecated
    public String oldContentColor;

    @Override
    public void validatePostLoad() throws ValidationException {
        // 确保字段不为null
        if (mainTitle == null) mainTitle = "服务器公告";
        if (subTitle == null) subTitle = "最新通知";
        if (announcementContent == null) announcementContent = Collections.singletonList("默认公告内容");
        if (buttonText == null) buttonText = "前往投递";
        if (buttonLink == null) buttonLink = "https://example.com";
        if (iconPath == null) iconPath = "testmod:textures/gui/announcement_icon.png";

        // 确保颜色值有效
        if (mainTitleColor == 0) mainTitleColor = 0xFFD700;
        if (subTitleColor == 0) subTitleColor = 0xFFFFFF;
        if (contentColor == 0) contentColor = 0xCCCCCC;

        // 确保图标尺寸有效
        if (iconWidth < 16) iconWidth = 16;
        if (iconWidth > 1024) iconWidth = 1024;
        if (iconHeight < 16) iconHeight = 16;
        if (iconHeight > 1024) iconHeight = 1024;
        if (iconTextSpacing < 0) iconTextSpacing = 10;

        // 迁移旧版配置
        migrateOldConfig();

        // 同步调试模式
        TestModClient.DEBUG_MODE = this.debugMode;
    }

    private void migrateOldConfig() {
        try {
            if (oldMainTitleColor != null && !oldMainTitleColor.isEmpty()) {
                mainTitleColor = parseColor(oldMainTitleColor, 0xFFD700);
                oldMainTitleColor = null;
            }

            if (oldSubTitleColor != null && !oldSubTitleColor.isEmpty()) {
                subTitleColor = parseColor(oldSubTitleColor, 0xFFFFFF);
                oldSubTitleColor = null;
            }

            if (oldContentColor != null && !oldContentColor.isEmpty()) {
                contentColor = parseColor(oldContentColor, 0xCCCCCC);
                oldContentColor = null;
            }
        } catch (Exception e) {
            TestModClient.LOGGER.warn("配置迁移失败", e);
        }
    }

    private int parseColor(String colorString, int defaultValue) {
        try {
            String cleanString = colorString.replace("#", "").trim();
            return (int) Long.parseLong(cleanString, 16);
        } catch (Exception e) {
            return defaultValue;
        }
    }
}