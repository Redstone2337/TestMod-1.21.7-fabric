package net.redstone233.test.core.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Config(name = "announcementscreen")
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
    @Comment("主标题颜色 (RGB十六进制)")
    public int mainTitleColor = 0xFFD700;

    @ConfigEntry.Gui.Tooltip
    @Comment("副标题颜色 (RGB十六进制)")
    public int subTitleColor = 0xFFFFFF;

    @ConfigEntry.Gui.Tooltip
    @Comment("公告内容颜色 (RGB十六进制)")
    public int contentColor = 0xCCCCCC;

    // 向后兼容处理
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

        // 确保颜色值有效
        if (mainTitleColor == 0) mainTitleColor = 0xFFD700;
        if (subTitleColor == 0) subTitleColor = 0xFFFFFF;
        if (contentColor == 0) contentColor = 0xCCCCCC;

        if (oldMainTitleColor != null && !oldMainTitleColor.isEmpty()) {
            try {
                mainTitleColor = (int) Long.parseLong(oldMainTitleColor.replace("#", ""), 16);
            } catch (Exception e) {
                // 转换失败，保持默认值
            }
        }

        if (oldSubTitleColor != null && !oldSubTitleColor.isEmpty()) {
            try {
                subTitleColor = (int) Long.parseLong(oldSubTitleColor.replace("#", ""), 16);
            } catch (Exception e) {
                // 转换失败，保持默认值
            }
        }

        if (oldContentColor != null && !oldContentColor.isEmpty()) {
            try {
                contentColor = (int) Long.parseLong(oldContentColor.replace("#", ""), 16);
            } catch (Exception e) {
                // 转换失败，保持默认值
            }
        }
    }
}
