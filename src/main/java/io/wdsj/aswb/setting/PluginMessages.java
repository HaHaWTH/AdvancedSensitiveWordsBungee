package io.wdsj.aswb.setting;

import ch.jalu.configme.Comment;
import ch.jalu.configme.SettingsHolder;
import ch.jalu.configme.configurationdata.CommentsConfiguration;
import ch.jalu.configme.properties.Property;

import static ch.jalu.configme.properties.PropertyInitializer.newProperty;

public class PluginMessages implements SettingsHolder {
    @Comment({"玩家发送敏感消息时候回复的假消息(只有玩家本人能看见)",
            "内置变量%integrated_player% %integrated_message% (支持PlaceHolderAPI)"
    })
    public static final Property<String> CHAT_FAKE_MESSAGE = newProperty("Chat.fakeMessage", "<%integrated_player%> %integrated_message%");
    @Comment("玩家发送敏感消息时候的提示")
    public static final Property<String> MESSAGE_ON_CHAT = newProperty("Chat.messageOnChat", "&c请勿在聊天中发送敏感词汇.");
    @Comment("插件重载消息")
    public static final Property<String> MESSAGE_ON_COMMAND_RELOAD = newProperty("Plugin.messageOnCommandReload", "&aAdvancedSensitiveWords has been reloaded.");
    @Comment("插件帮助菜单")
    public static final Property<String> MESSAGE_ON_COMMAND_HELP = newProperty("Plugin.messageOnCommandHelp", "&bAdvancedSensitiveWordsBungee&r---&b帮助菜单\n   &7/aswb reload&7: &a重新加载过滤词库和插件配置\n   &7/aswb status&7: &a显示插件状态菜单\n   &7/aswb test <待测消息>: &a运行敏感词测试\n   &7/aswb help&7: &a显示帮助信息");
    @Comment("插件状态菜单")
    public static final Property<String> MESSAGE_ON_COMMAND_STATUS = newProperty("Plugin.messageOnCommandStatus", "&bAdvancedSensitiveWordsBungee&r---&b插件状态(%VERSION%)\n   &7系统信息: &b%PLATFORM% %BIT% (Java %JAVA_VERSION% -- %JAVA_VENDOR%)\n   &7初始化: %INIT%\n   &7已过滤消息数: &a%NUM%\n   &7近20次处理平均耗时: %MS%");
    @Comment("敏感词测试返回")
    public static final Property<String> MESSAGE_ON_COMMAND_TEST = newProperty("Plugin.commandTest.testResultTrue", "&b一眼丁真, 鉴定为敏感词(鉴定报告)\n   &7原消息: &c%ORIGINAL_MSG%\n   &7过滤后消息: &a%PROCESSED_MSG%\n   &7敏感词列表: &b%CENSORED_LIST%");
    @Comment("敏感词测试通过")
    public static final Property<String> MESSAGE_ON_COMMAND_TEST_PASS = newProperty("Plugin.commandTest.testResultPass", "&a待测消息中没有敏感词喵~");
    @Comment("敏感词测试参数不足")
    public static final Property<String> MESSAGE_ON_COMMAND_TEST_NOT_ENOUGH = newProperty("Plugin.commandTest.testArgNotEnough", "&c参数不足, 请使用 &7/aswb test <待测消息>");
    @Comment("敏感词测试未初始化")
    public static final Property<String> MESSAGE_ON_COMMAND_TEST_NOT_INIT = newProperty("Plugin.commandTest.testNotInit", "&c插件还没有初始化完毕喵");
    @Comment("没有权限执行该指令")
    public static final Property<String> NO_PERMISSION = newProperty("Plugin.noPermission", "&c你没有权限执行该指令.");

    @Override
    public void registerComments(CommentsConfiguration conf) {
        conf.setComment("", "AdvancedSensitiveWords 插件消息配置");
        conf.setComment("Plugin", "插件消息");
        conf.setComment("Plugin.commandTest", "敏感词测试消息(不计入已过滤消息)");
        conf.setComment("Chat", "聊天检测消息");
    }

    private PluginMessages() {
    }
}
