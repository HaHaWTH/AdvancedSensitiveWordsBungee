package io.wdsj.aswb.setting;

import ch.jalu.configme.Comment;
import ch.jalu.configme.SettingsHolder;
import ch.jalu.configme.configurationdata.CommentsConfiguration;
import ch.jalu.configme.properties.Property;

import java.util.List;

import static ch.jalu.configme.properties.PropertyInitializer.newListProperty;
import static ch.jalu.configme.properties.PropertyInitializer.newProperty;

public class PluginSettings implements SettingsHolder {
    @Comment("是否启用默认词库(6w+)(强烈建议开启)")
    public static final Property<Boolean> ENABLE_DEFAULT_WORDS = newProperty("Plugin.enableDefaultWords", true);
    @Comment("是否启用在线词库")
    public static final Property<Boolean> ENABLE_ONLINE_WORDS = newProperty("Plugin.enableOnlineWords", false);
    @Comment("在线词库地址")
    public static final Property<String> ONLINE_WORDS_URL = newProperty("Plugin.onlineWordsUrl", "https://raw.githubusercontent.com/HaHaWTH/ASW-OnlineWordList/main/lists.txt");
    @Comment("在线文本字符编码(UTF-8/GBK)")
    public static final Property<String> ONLINE_WORDS_ENCODING = newProperty("Plugin.onlineWordsEncoding", "UTF-8");
    @Comment("是否记录违规消息(存储在violations.log中)")
    public static final Property<Boolean> LOG_VIOLATION = newProperty("Plugin.logViolation", true);
    @Comment("是否在插件启动时自动清除旧的日志文件")
    public static final Property<Boolean> PURGE_LOG_FILE = newProperty("Plugin.purgeLogFile", false);
    @Comment("敏感词替换符号")
    public static final Property<String> REPLACEMENT = newProperty("Plugin.replacement", "*");
    @Comment("预定义替换(需要先加入blackList)(如包含相同敏感词,长的放前面)(用|来分隔敏感词和替换词)")
    public static final Property<List<String>> DEFINED_REPLACEMENT = newListProperty("Plugin.definedReplacement", "失业|灵活就业");
    @Comment("*是否启用告示牌检测")
    public static final Property<Boolean> ENABLE_SIGN_EDIT_CHECK = newProperty("Plugin.enableSignEditCheck", true);
    @Comment("*是否启用铁砧重命名检测")
    public static final Property<Boolean> ENABLE_ANVIL_EDIT_CHECK = newProperty("Plugin.enableAnvilEditCheck", true);
    @Comment("*是否启用书检测")
    public static final Property<Boolean> ENABLE_BOOK_EDIT_CHECK = newProperty("Plugin.enableBookEditCheck", true);
    @Comment("*是否启用玩家名称检测(推荐支持中文名的服务器开启)")
    public static final Property<Boolean> ENABLE_PLAYER_NAME_CHECK = newProperty("Plugin.enablePlayerNameCheck", false);
    @Comment("默认跳过字符")
    public static final Property<String> IGNORE_CHAR = newProperty("Plugin.ignoreChar", "`-—=~～!！@#$%^&*()_+[]{}\\|;:'\"“”,，.。、（）<>?？¥【】《》 ");
    @Comment("检测大小写")
    public static final Property<Boolean> IGNORE_CASE = newProperty("Plugin.ignoreCase", true);
    @Comment("检测半角&全角字符")
    public static final Property<Boolean> IGNORE_WIDTH = newProperty("Plugin.ignoreWidth", true);
    @Comment("检测变着花样发送的数字(例:1贰叁④)")
    public static final Property<Boolean> IGNORE_NUM_STYLE = newProperty("Plugin.ignoreNumStyle", true);
    @Comment("检测变着花样发送的中文(简繁)")
    public static final Property<Boolean> IGNORE_CHINESE_STYLE = newProperty("Plugin.ignoreChineseStyle", true);
    @Comment("检测变着花样发送的英文(例：Ⓕⓤc⒦)")
    public static final Property<Boolean> IGNORE_ENGLISH_STYLE = newProperty("Plugin.ignoreEnglishStyle", true);
    @Comment({"是否强制英文全词匹配",
            "(例:屏蔽了av但又不想让have被屏蔽"})
    public static final Property<Boolean> FORCE_ENGLISH_FULL_MATCH = newProperty("Plugin.forceEnglishFullMatch", false);
    @Comment("检测重复字符(例：SSSSSBBBBB)")
    public static final Property<Boolean> IGNORE_REPEAT = newProperty("Plugin.ignoreRepeat", true);
    @Comment("启用连续数字检测(通常用于过滤群号&身份证号&手机号等)")
    public static final Property<Boolean> ENABLE_NUM_CHECK = newProperty("Plugin.enableNumCheck", true);
    @Comment("判定为连续数字的长度")
    public static final Property<Integer> NUM_CHECK_LEN = newProperty("Plugin.numCheckLen", 9);
    @Comment("启用邮箱检测(xxx@xxx.xxx)")
    public static final Property<Boolean> ENABLE_EMAIL_CHECK = newProperty("Plugin.enableEmailCheck", false);
    @Comment("启用网址检测(http://xxx.xxx)")
    public static final Property<Boolean> ENABLE_URL_CHECK = newProperty("Plugin.enableUrlCheck", true);
    @Comment("启用敏感英文单词检测")
    public static final Property<Boolean> ENABLE_WORD_CHECK = newProperty("Plugin.enableWordCheck", true);
    @Comment("自定义敏感词列表")
    public static final Property<List<String>> BLACK_LIST = newListProperty("Plugin.blackList", "失业");
    @Comment("敏感词白名单")
    public static final Property<List<String>> WHITE_LIST = newListProperty("Plugin.whiteList", "3p");
    @Comment({"是否关闭插件启动时的求赞助消息:(",
            "赞助链接: https://afdian.net/a/114514woxiuyuan/"})
    public static final Property<Boolean> DISABLE_DONATION = newProperty("Plugin.disableDonation", false);
    @Comment("替换还是取消(replace/cancel)")
    public static final Property<String> CHAT_METHOD = newProperty("Chat.method", "replace");
    @Comment({"取消后是否发送假消息(仅取消模式可用)(支持PAPI)(Inspired by Bilibili Avalon System)",
            "如果命令被取消则不会发送假消息. 避免隐私泄露"
    })
    public static final Property<Boolean> CHAT_FAKE_MESSAGE_ON_CANCEL = newProperty("Chat.fakeMessageOnCancel", false);
    @Comment("是否发送消息提醒(和假消息冲突)")
    public static final Property<Boolean> CHAT_SEND_MESSAGE = newProperty("Chat.sendMessage", true);
    @Comment("是否开启聊天上下文检测(仅提供取消和假消息模式,配置跟随聊天检测)")
    public static final Property<Boolean> CHAT_CONTEXT_CHECK = newProperty("Chat.contextCheck", false);
    @Comment("最大检测上下文大小")
    public static final Property<Integer> CHAT_CONTEXT_MAX_SIZE = newProperty("Chat.contextMaxSize", 4);
    @Comment("是否反转指令白名单为黑名单")
    public static final Property<Boolean> CHAT_INVERT_WHITELIST = newProperty("Chat.invertCommandWhiteList", false);
    @Comment("指令白名单(白名单的指令如含敏感词不会被检测)")
    public static final Property<List<String>> CHAT_COMMAND_WHITE_LIST = newListProperty("Chat.commandWhiteList", "/asw", "/reload", "/help", "/ban",
            "/mute", "/unmute", "/kick", "/unban", "/res", "/sethome", "/home", "/l", "/tp", "/tpa", "/login", "/log", "/register", "/reg");
    @Override
    public void registerComments(CommentsConfiguration conf) {
        conf.setComment("", "AdvancedSensitiveWords 配置文件", "所有配置项均支持重载(标*的配置项仅支持重载关闭)");
        conf.setComment("Plugin", "插件总配置");
        conf.setComment("Chat", "聊天检测配置");
    }

    // Do not instantiate.
    private PluginSettings() {
    }
}
