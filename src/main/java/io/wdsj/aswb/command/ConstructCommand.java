package io.wdsj.aswb.command;

import com.github.houbb.heaven.util.util.OsUtil;
import io.wdsj.aswb.AdvancedSensitiveWords;
import io.wdsj.aswb.setting.PluginMessages;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

import java.util.List;

import static com.github.houbb.heaven.util.util.OsUtil.is64;
import static com.github.houbb.heaven.util.util.OsUtil.isUnix;
import static io.wdsj.aswb.AdvancedSensitiveWords.*;
import static io.wdsj.aswb.util.TimingUtils.*;
import static io.wdsj.aswb.util.Utils.getMinecraftVersion;
import static io.wdsj.aswb.util.Utils.messagesFilteredNum;

public class ConstructCommand extends Command {
    public ConstructCommand() {
        super("advancedsensitivewordsbungee", null, "aswb");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("reload") && (sender.hasPermission("advancedsensitivewords.reload"))) {
                settingsManager.reload();
                messagesManager.reload();
                AdvancedSensitiveWords.getInstance().doInitTasks();
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', messagesManager.getProperty(PluginMessages.MESSAGE_ON_COMMAND_RELOAD)));
                return;
            }
            if (args[0].equalsIgnoreCase("reload")) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', messagesManager.getProperty(PluginMessages.NO_PERMISSION)));
                return;
            }
            if (args[0].equalsIgnoreCase("status") && (sender.hasPermission("advancedsensitivewords.status"))) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', messagesManager.getProperty(PluginMessages.MESSAGE_ON_COMMAND_STATUS).replace("%NUM%", "&a" + messagesFilteredNum.get()).replace("%INIT%", isInitialized ? "&a已初始化" : "&c未初始化").replace("%MS%", getProcessAverage() >= 120 ? getProcessAverage() >= 300 ? "&c" + getProcessAverage() + "ms" : "&e" + getProcessAverage() + "ms" : "&a" + getProcessAverage() + "ms").replace("%VERSION%", AdvancedSensitiveWords.getInstance().getDescription().getVersion()).replace("%PLATFORM%", OsUtil.isWindows() ? "Windows" : (OsUtil.isMac() ? "Mac" : isUnix() ? "Linux" : "Unknown")).replace("%BIT%", is64() ? "64bit" : "32bit").replace("%JAVA_VERSION%", getJvmVersion()).replace("%JAVA_VENDOR%", getJvmVendor())));
                return;
            }
            if (args[0].equalsIgnoreCase("status")) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', messagesManager.getProperty(PluginMessages.NO_PERMISSION)));
                return;
            }
        }
        if (args.length >= 1) {
            if (args[0].equalsIgnoreCase("test") && (sender.hasPermission("advancedsensitivewords.test"))) {
                if (args.length >= 2) {
                    if (isInitialized) {
                        StringBuilder sb = new StringBuilder();
                        for (int i = 1; i <= args.length - 1; i++) {
                            sb.append(args[i]).append(" ");
                        }
                        String testArgs = sb.toString();
                        List<String> censoredWordList = sensitiveWordBs.findAll(testArgs);
                        if (!censoredWordList.isEmpty()) {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', messagesManager.getProperty(PluginMessages.MESSAGE_ON_COMMAND_TEST).replace("%ORIGINAL_MSG%", testArgs).replace("%PROCESSED_MSG%", sensitiveWordBs.replace(testArgs)).replace("%CENSORED_LIST%", censoredWordList.toString())));
                        } else {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', messagesManager.getProperty(PluginMessages.MESSAGE_ON_COMMAND_TEST_PASS)));
                        }
                    } else {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', messagesManager.getProperty(PluginMessages.MESSAGE_ON_COMMAND_TEST_NOT_INIT)));
                    }
                } else {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', messagesManager.getProperty(PluginMessages.MESSAGE_ON_COMMAND_TEST_NOT_ENOUGH)));
                }
                return;
            }
            if (args[0].equalsIgnoreCase("test") && args.length == 1) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', messagesManager.getProperty(PluginMessages.NO_PERMISSION)));
            }
        }
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', messagesManager.getProperty(PluginMessages.MESSAGE_ON_COMMAND_HELP)));
    }
}
