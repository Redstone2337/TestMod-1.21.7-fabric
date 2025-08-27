// ModConfig.java
package net.redstone233.test.core.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;
import net.redstone233.test.TestModClient;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Config(name = "announcement")
public class ModConfig implements ConfigData {
    @ConfigEntry.Gui.Tooltip
    @Comment("上次显示公告的哈希值（用于检测公告是否已修改）")
    public String lastDisplayedHash = "";

    @ConfigEntry.Gui.Tooltip
    @Comment("是否在进入世界时显示公告")
    public boolean showOnWorldEnter = true;

    @ConfigEntry.Gui.Tooltip(count = 2)
    @Comment("主标题文本")
    public String mainTitle = "模组公告";

    @ConfigEntry.Gui.Tooltip
    @Comment("副标题文本")
    public String subTitle = "请仔细观看";

    @ConfigEntry.Gui.Tooltip
    @Comment("公告内容（每行一条，支持多行）")
    public List<String> announcementContent = Arrays.asList(
            "§a欢迎游玩，我们团队做的模组！",
            " ",
            "§e一些提醒：",
            "§f1. 模组仅限于1.21.7~1.21.8fabric",
            "§f2. 模组目前是半成品",
            "§f3. 后面会继续更新",
            " ",
            "§b模组随缘更新",
            "§c若发现bug可以向模组作者或者仓库反馈！"
    );

    @ConfigEntry.Gui.Tooltip
    @Comment("确定按钮文本")
    public String confirmButtonText = "确定";

    @ConfigEntry.Gui.Tooltip
    @Comment("前往投递按钮文本")
    public String submitButtonText = "前往投递";

    @ConfigEntry.Gui.Tooltip
    @Comment("按钮点击后打开的链接")
    public String buttonLink = "https://github.com/Redstone2337/TestMod-1.21.7-fabric/issues";

    @ConfigEntry.Gui.Tooltip
    @Comment("文本滚动速度 (像素/秒)")
    public int scrollSpeed = 30;

    @ConfigEntry.Gui.Tooltip
    @Comment("主标题颜色 (RGB整数，十六进制格式如0xFFD700)")
    public int mainTitleColor = 0xFFD700;

    @ConfigEntry.Gui.Tooltip
    @Comment("副标题颜色 (RGB整数，十六进制格式如0xFFFFFF)")
    public int subTitleColor = 0xFFFFFF;

    @ConfigEntry.Gui.Tooltip
    @Comment("公告内容颜色 (RGB整数，十六进制格式如0x0610EA)")
    public int contentColor = 0x0610EA;

    @ConfigEntry.Gui.Tooltip
    @Comment("启用调试模式（显示UI边界等辅助信息）")
    public boolean debugMode = false;

    @ConfigEntry.Gui.Tooltip
    @Comment("是否显示公告图标")
    public boolean showIcon = false;

    @ConfigEntry.Gui.Tooltip
    @Comment("图标资源路径 (例如: testmod:textures/gui/announcement_icon.png)")
    public String iconPath = "mtc:textures/gui/announcement_icon.png";

    @ConfigEntry.Gui.Tooltip
    @Comment("图标宽度 (像素)")
    public int iconWidth = 32;

    @ConfigEntry.Gui.Tooltip
    @Comment("图标高度 (像素)")
    public int iconHeight = 32;

    @ConfigEntry.Gui.Tooltip
    @Comment("图标与文本的间距 (像素)")
    public int iconTextSpacing = 10;

    @ConfigEntry.Gui.Tooltip
    @Comment("是否使用自定义RGB颜色（启用后将隐藏Formatting枚举颜色选择）")
    public boolean useCustomRGB = false;

    @ConfigEntry.Gui.Tooltip
    @Comment("是否使用自定义公告背景图")
    public boolean useCustomAnnouncementBackground = false;

    @ConfigEntry.Gui.Tooltip
    @Comment("公告背景图路径 (例如: testmod:textures/gui/announcement_background.png)")
    public String announcementBackgroundPath = "testmod:textures/gui/announcement_background.png";

    @ConfigEntry.Gui.Tooltip
    @Comment("是否使用自定义玩家信息背景图")
    public boolean useCustomPlayerInfoBackground = false;

    @ConfigEntry.Gui.Tooltip
    @Comment("玩家信息背景图路径 (例如: testmod:textures/gui/player_info_background.png)")
    public String playerInfoBackgroundPath = "testmod:textures/gui/player_info_background.png";

    @Override
    public void validatePostLoad() throws ValidationException {
        // 确保字段不为null
        if (mainTitle == null) mainTitle = "服务器公告";
        if (subTitle == null) subTitle = "最新通知";
        if (announcementContent == null) announcementContent = Collections.singletonList("默认公告内容");
        if (confirmButtonText == null) confirmButtonText = "确定";
        if (submitButtonText == null) submitButtonText = "前往投递";
        if (buttonLink == null) buttonLink = "https://github.com/Redstone2337/TestMod-1.21.7-fabric/issues";
        if (iconPath == null) iconPath = "mtc:textures/gui/announcement_icon.png";
        if (announcementBackgroundPath == null) announcementBackgroundPath = "testmod:textures/gui/announcement_background.png";
        if (playerInfoBackgroundPath == null) playerInfoBackgroundPath = "testmod:textures/gui/player_info_background.png";

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

        // 同步调试模式
        TestModClient.DEBUG_MODE = this.debugMode;
    }
}