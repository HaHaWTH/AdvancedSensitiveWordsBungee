package io.wdsj.aswb.listener;

import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientChatCommand;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientChatMessage;
import io.wdsj.aswb.AdvancedSensitiveWords;
import io.wdsj.aswb.setting.PluginMessages;
import io.wdsj.aswb.setting.PluginSettings;
import io.wdsj.aswb.util.ContextUtils;
import io.wdsj.aswb.util.Utils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.List;
import java.util.Queue;

import static io.wdsj.aswb.AdvancedSensitiveWords.*;
import static io.wdsj.aswb.util.TimingUtils.addProcessStatistic;
import static io.wdsj.aswb.util.Utils.*;

public class ASWPacketListener extends PacketListenerAbstract {
    public ASWPacketListener() {
        super(PacketListenerPriority.LOW);
    }

    public void onPacketReceive(PacketReceiveEvent event) {
        PacketTypeCommon packetType = event.getPacketType();
        User user = event.getUser();
        ProxiedPlayer player = (ProxiedPlayer) event.getPlayer();
        String userName = user.getName();
        if (packetType == PacketType.Play.Client.CHAT_MESSAGE) {
            WrapperPlayClientChatMessage wrapperPlayClientChatMessage = new WrapperPlayClientChatMessage(event);
            String originalMessage = wrapperPlayClientChatMessage.getMessage();
            if (shouldNotProcess(player, originalMessage)) return;
            long startTime = System.currentTimeMillis();
            // Word check
            List<String> censoredWords = AdvancedSensitiveWords.sensitiveWordBs.findAll(originalMessage);
            if (!censoredWords.isEmpty()) {
                messagesFilteredNum.getAndIncrement();
                String processedMessage = AdvancedSensitiveWords.sensitiveWordBs.replace(originalMessage);
                if (settingsManager.getProperty(PluginSettings.CHAT_METHOD).equalsIgnoreCase("cancel")) {
                    event.setCancelled(true);
                    if (settingsManager.getProperty(PluginSettings.CHAT_FAKE_MESSAGE_ON_CANCEL) && isNotCommand(originalMessage)) {
                        String fakeMessage = messagesManager.getProperty(PluginMessages.CHAT_FAKE_MESSAGE).replace("%integrated_player%", userName).replace("%integrated_message%", originalMessage);
                        user.sendMessage(ChatColor.translateAlternateColorCodes('&', fakeMessage));
                    }
                } else {
                    int maxLength = 256;
                    if (processedMessage.length() > maxLength) {
                        wrapperPlayClientChatMessage.setMessage(processedMessage.substring(0, maxLength));
                    } else {
                        wrapperPlayClientChatMessage.setMessage(processedMessage);
                    }
                }
                if (settingsManager.getProperty(PluginSettings.CHAT_SEND_MESSAGE)) {
                    user.sendMessage(ChatColor.translateAlternateColorCodes('&', messagesManager.getProperty(PluginMessages.MESSAGE_ON_CHAT).replace("%integrated_player%", userName).replace("%integrated_message%", originalMessage)));
                }

                if (settingsManager.getProperty(PluginSettings.LOG_VIOLATION)) {
                    Utils.logViolation(userName + "(IP: " + user.getAddress().getAddress().getHostAddress() + ")(Chat)", originalMessage + censoredWords);
                }
                long endTime = System.currentTimeMillis();
                addProcessStatistic(endTime, startTime);
                return;
            }

            // Context check
            if (settingsManager.getProperty(PluginSettings.CHAT_CONTEXT_CHECK) && isNotCommand(originalMessage)) {
                ContextUtils.addMessage(player, originalMessage);
                Queue<String> queue = ContextUtils.getHistory(player);
                String originalContext = String.join("", queue);
                List<String> censoredContextList = sensitiveWordBs.findAll(originalContext);
                if (!censoredContextList.isEmpty()) {
                    ContextUtils.clearPlayerContext(player);
                    messagesFilteredNum.getAndIncrement();
                    event.setCancelled(true);
                    if (settingsManager.getProperty(PluginSettings.CHAT_FAKE_MESSAGE_ON_CANCEL) && isNotCommand(originalMessage)) {
                        String fakeMessage = messagesManager.getProperty(PluginMessages.CHAT_FAKE_MESSAGE).replace("%integrated_player%", userName).replace("%integrated_message%", originalMessage);
                        user.sendMessage(ChatColor.translateAlternateColorCodes('&', fakeMessage));
                    }
                    if (settingsManager.getProperty(PluginSettings.CHAT_SEND_MESSAGE)) {
                        user.sendMessage(ChatColor.translateAlternateColorCodes('&', messagesManager.getProperty(PluginMessages.MESSAGE_ON_CHAT).replace("%integrated_player%", userName).replace("%integrated_message%", originalMessage)));
                    }
                    if (settingsManager.getProperty(PluginSettings.LOG_VIOLATION)) {
                        Utils.logViolation(userName + "(IP: " + user.getAddress().getAddress().getHostAddress() + ")(Chat)(Context)", originalContext + censoredContextList);
                    }
                    long endTime = System.currentTimeMillis();
                    addProcessStatistic(endTime, startTime);
                }
            }
        } else if (packetType == PacketType.Play.Client.CHAT_COMMAND) {
            WrapperPlayClientChatCommand wrapperPlayClientChatCommand = new WrapperPlayClientChatCommand(event);
            String originalCommand = wrapperPlayClientChatCommand.getCommand();
            if (shouldNotProcess(player, "/" + originalCommand)) return;
            long startTime = System.currentTimeMillis();
            List<String> censoredWords = AdvancedSensitiveWords.sensitiveWordBs.findAll(originalCommand);
            if (!censoredWords.isEmpty()) {
                messagesFilteredNum.getAndIncrement();
                String processedCommand = AdvancedSensitiveWords.sensitiveWordBs.replace(originalCommand);
                if (settingsManager.getProperty(PluginSettings.CHAT_METHOD).equalsIgnoreCase("cancel")) {
                    event.setCancelled(true);
                } else {
                    int commandMaxLength = 255; // because there is a slash before the command, so we should minus 1
                    if (processedCommand.length() > commandMaxLength) {
                        wrapperPlayClientChatCommand.setCommand(processedCommand.substring(0, commandMaxLength));
                    } else {
                        wrapperPlayClientChatCommand.setCommand(processedCommand);
                    }
                }
                if (settingsManager.getProperty(PluginSettings.CHAT_SEND_MESSAGE)) {
                    user.sendMessage(ChatColor.translateAlternateColorCodes('&', messagesManager.getProperty(PluginMessages.MESSAGE_ON_CHAT).replace("%integrated_player%", userName).replace("%integrated_message%", originalCommand)));
                }
                if (settingsManager.getProperty(PluginSettings.LOG_VIOLATION)) {
                    Utils.logViolation(userName + "(IP: " + user.getAddress().getAddress().getHostAddress() + ")(Chat)", "/" + originalCommand + censoredWords);
                }
                long endTime = System.currentTimeMillis();
                addProcessStatistic(endTime, startTime);
            }
        }
    }
    private boolean shouldNotProcess(ProxiedPlayer player, String message) {
        if (isInitialized && !player.hasPermission("advancedsensitivewords.bypass") && !isCommandAndWhiteListed(message)) {
            return false;
        }
        return true;
    }
}
