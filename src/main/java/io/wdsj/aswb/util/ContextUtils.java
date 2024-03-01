package io.wdsj.aswb.util;

import io.wdsj.aswb.setting.PluginSettings;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

import static io.wdsj.aswb.AdvancedSensitiveWords.settingsManager;

public class ContextUtils {
    private static final ConcurrentHashMap<ProxiedPlayer, Queue<String>> chatHistory = new ConcurrentHashMap<>();
    /**
     * Add player message to history
     */
    public static void addMessage(ProxiedPlayer player, String message) {
        chatHistory.computeIfAbsent(player, k -> new LinkedList<>());
        Queue<String> history = chatHistory.get(player);
        while (history.size() >= settingsManager.getProperty(PluginSettings.CHAT_CONTEXT_MAX_SIZE)) {
            history.poll();
        }
        history.offer(message.trim());
    }

    public static Queue<String> getHistory(ProxiedPlayer player) {
        return chatHistory.getOrDefault(player, new LinkedList<>());
    }

    public static void clearPlayerContext(ProxiedPlayer player) {
        if (chatHistory.get(player) == null) return;
        chatHistory.remove(player);
    }
    public static void forceClearContext() {
        chatHistory.clear();
    }

    private ContextUtils() {}
}
