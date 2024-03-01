package io.wdsj.aswb.util;

import com.github.houbb.heaven.util.io.FileUtil;
import com.github.houbb.heaven.util.lang.StringUtil;
import com.github.retrooper.packetevents.protocol.player.User;
import io.wdsj.aswb.AdvancedSensitiveWords;
import io.wdsj.aswb.setting.PluginSettings;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import static io.wdsj.aswb.AdvancedSensitiveWords.settingsManager;

public class Utils {
    public static AtomicLong messagesFilteredNum = new AtomicLong(0);

    public static String getPlayerIp(User user) {
        InetSocketAddress address = user.getAddress();
        if (address != null && address.getAddress() != null) return address.getAddress().getHostAddress();
        return "null";
    }

    public static boolean isClassLoaded(String className) {
        try {
            Class.forName(className);
            return true;
        } catch (ClassNotFoundException ignored) {
            return false;
        }
    }

    public static void logViolation(String playerName, String violationReason) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss");
        String formattedDate = dateFormat.format(new Date());
        String logMessage = "[" + formattedDate + "] " + playerName + " " + violationReason;
        File logFile = new File(AdvancedSensitiveWords.getInstance().getDataFolder(), "violations.log");
        if (!logFile.exists()) {
            try {
                logFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
        try {
            FileWriter writer = new FileWriter(logFile, true);

            try {
                writer.write(logMessage + System.lineSeparator());
            } catch (Throwable th) {
                try {
                    writer.close();
                } catch (Throwable t) {
                    th.addSuppressed(t);
                }
                throw th;
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void purgeLog() {
        File logFile = new File(AdvancedSensitiveWords.getInstance().getDataFolder(), "violations.log");
        if (!logFile.exists()) return;
        FileUtil.deleteFile(logFile);
        AdvancedSensitiveWords.getInstance().getLogger().info("Successfully purged violations");
    }
    public static boolean isCommand(String command) {
        return command.startsWith("/");
    }
    public static String getSplitCommandArgs(String command) {
        String[] splitCommand = command.split(" ");
        if (splitCommand.length <= 1) return "";
        return String.join(" ", Arrays.copyOfRange(splitCommand, 1, splitCommand.length));
    }

    public static boolean isCommandAndWhiteListed(String command) {
    if (!command.startsWith("/")) return false;
    List<String> whitelist = settingsManager.getProperty(PluginSettings.CHAT_COMMAND_WHITE_LIST);
    String[] splitCommand = command.split(" ");
    for (String s : whitelist) {
        if (splitCommand[0].equalsIgnoreCase(s)) {
            return !settingsManager.getProperty(PluginSettings.CHAT_INVERT_WHITELIST);
        }
    }
    return settingsManager.getProperty(PluginSettings.CHAT_INVERT_WHITELIST);
}

    public static String getMinecraftVersion() {
        return AdvancedSensitiveWords.getInstance().getProxy().getVersion();
    }

    public static boolean isNotCommand(String command) {
        return !command.startsWith("/");
    }

    private Utils() {
    }
}
