package io.wdsj.aswb;

import ch.jalu.configme.SettingsManager;
import ch.jalu.configme.SettingsManagerBuilder;
import com.github.houbb.sensitive.word.api.IWordAllow;
import com.github.houbb.sensitive.word.api.IWordDeny;
import com.github.houbb.sensitive.word.bs.SensitiveWordBs;
import com.github.houbb.sensitive.word.support.allow.WordAllows;
import com.github.houbb.sensitive.word.support.deny.WordDenys;
import com.github.houbb.sensitive.word.support.resultcondition.WordResultConditions;
import com.github.houbb.sensitive.word.support.tag.WordTags;
import com.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.bungee.factory.BungeePacketEventsBuilder;
import io.wdsj.aswb.command.ConstructCommand;
import io.wdsj.aswb.listener.ASWPacketListener;
import io.wdsj.aswb.method.*;
import io.wdsj.aswb.setting.PluginMessages;
import io.wdsj.aswb.setting.PluginSettings;
import io.wdsj.aswb.util.ContextUtils;
import io.wdsj.aswb.util.TimingUtils;
import net.md_5.bungee.api.plugin.Plugin;
import org.bstats.bungeecord.Metrics;
import org.bstats.charts.SimplePie;

import java.io.File;
import java.net.ProxySelector;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import static io.wdsj.aswb.util.TimingUtils.cleanStatisticCache;
import static io.wdsj.aswb.util.Utils.purgeLog;

public final class AdvancedSensitiveWords extends Plugin {
    public static boolean isInitialized = false;
    public static SensitiveWordBs sensitiveWordBs;
    private final File CONFIG_FILE = new File(getDataFolder(), "config.yml");
    private final File MESSAGE_FILE = new File(getDataFolder(), "messages.yml");
    public static SettingsManager settingsManager;
    public static SettingsManager messagesManager;
    private static AdvancedSensitiveWords instance;

    public static AdvancedSensitiveWords getInstance() {
        return instance;
    }
    @Override
    public void onLoad() {
        PacketEvents.setAPI(BungeePacketEventsBuilder.build(this));
        PacketEvents.getAPI().getSettings().reEncodeByDefault(true).checkForUpdates(false).bStats(false);
        PacketEvents.getAPI().load();
    }

    @Override
    public void onEnable() {
        getLogger().info("Initializing DFA dict...");
        long startTime = System.currentTimeMillis();
        instance = this;
        cleanStatisticCache();
        settingsManager = SettingsManagerBuilder
                .withYamlFile(CONFIG_FILE)
                .configurationData(PluginSettings.class)
                .useDefaultMigrationService()
                .create();
        messagesManager = SettingsManagerBuilder
                .withYamlFile(MESSAGE_FILE)
                .configurationData(PluginMessages.class)
                .useDefaultMigrationService()
                .create();
        doInitTasks();
        if (settingsManager.getProperty(PluginSettings.PURGE_LOG_FILE)) purgeLog();
        PacketEvents.getAPI().getEventManager().registerListener(new ASWPacketListener());
        PacketEvents.getAPI().init();
        int pluginId = 21183;
        Metrics metrics = new Metrics(this, pluginId);
        metrics.addCustomChart(new SimplePie("default_list", () -> String.valueOf(settingsManager.getProperty(PluginSettings.ENABLE_DEFAULT_WORDS))));
        metrics.addCustomChart(new SimplePie("java_vendor", TimingUtils::getJvmVendor));
        getProxy().getPluginManager().registerCommand(this, new ConstructCommand());
        long endTime = System.currentTimeMillis();
        getLogger().info("AdvancedSensitiveWords is enabled!(took " + (endTime - startTime) + "ms)");
        // bro, don't bytecode this, you can just disable it in the config TAT
        if (Math.random() < 0.1 && !settingsManager.getProperty(PluginSettings.DISABLE_DONATION)) {
            getLogger().info("This plugin takes over 600 hours to develop and optimize, if you think it's nice, consider" +
                    " support: https://afdian.net/a/114514woxiuyuan/");
        }
    }


    public void doInitTasks() {
        IWordAllow wA = WordAllows.chains(WordAllows.defaults(), new WordAllow());
        AtomicReference<IWordDeny> wD = new AtomicReference<>();
        isInitialized = false;
        ProxySelector.setDefault(null);
        getProxy().getScheduler().runAsync( this, () -> {
            if (settingsManager.getProperty(PluginSettings.ENABLE_DEFAULT_WORDS) && settingsManager.getProperty(PluginSettings.ENABLE_ONLINE_WORDS)) {
                wD.set(WordDenys.chains(WordDenys.defaults(), new WordDeny(), new OnlineWordDeny()));
            } else if (settingsManager.getProperty(PluginSettings.ENABLE_DEFAULT_WORDS)) {
                wD.set(WordDenys.chains(new WordDeny(), WordDenys.defaults()));
            } else if (settingsManager.getProperty(PluginSettings.ENABLE_ONLINE_WORDS)) {
                wD.set(WordDenys.chains(new OnlineWordDeny(), new WordDeny()));
            } else {
                wD.set(new WordDeny());
            }
            // Full async reload
            sensitiveWordBs = SensitiveWordBs.newInstance().ignoreCase(settingsManager.getProperty(PluginSettings.IGNORE_CASE)).ignoreWidth(settingsManager.getProperty(PluginSettings.IGNORE_WIDTH)).ignoreNumStyle(settingsManager.getProperty(PluginSettings.IGNORE_NUM_STYLE)).ignoreChineseStyle(settingsManager.getProperty(PluginSettings.IGNORE_CHINESE_STYLE)).ignoreEnglishStyle(settingsManager.getProperty(PluginSettings.IGNORE_ENGLISH_STYLE)).ignoreRepeat(settingsManager.getProperty(PluginSettings.IGNORE_REPEAT)).enableNumCheck(settingsManager.getProperty(PluginSettings.ENABLE_NUM_CHECK)).enableEmailCheck(settingsManager.getProperty(PluginSettings.ENABLE_EMAIL_CHECK)).enableUrlCheck(settingsManager.getProperty(PluginSettings.ENABLE_URL_CHECK)).enableWordCheck(settingsManager.getProperty(PluginSettings.ENABLE_WORD_CHECK)).wordResultCondition(settingsManager.getProperty(PluginSettings.FORCE_ENGLISH_FULL_MATCH) ? WordResultConditions.englishWordMatch() : WordResultConditions.alwaysTrue()).wordDeny(wD.get()).wordAllow(wA).numCheckLen(settingsManager.getProperty(PluginSettings.NUM_CHECK_LEN)).wordReplace(new WordReplace()).wordTag(WordTags.none()).charIgnore(new CharIgnore()).init();
            isInitialized = true;
        });
    }

    @Override
    public void onDisable() {
        PacketEvents.getAPI().terminate();
        TimingUtils.cleanStatisticCache();
        ContextUtils.forceClearContext();
        getLogger().info("AdvancedSensitiveWords is disabled!");
    }
}
