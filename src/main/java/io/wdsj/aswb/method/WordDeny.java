package io.wdsj.aswb.method;

import com.github.houbb.sensitive.word.api.IWordDeny;
import io.wdsj.aswb.setting.PluginSettings;

import java.util.List;

import static io.wdsj.aswb.AdvancedSensitiveWords.settingsManager;

public class WordDeny implements IWordDeny {
    @Override
    public List<String> deny() {
        return settingsManager.getProperty(PluginSettings.BLACK_LIST);
    }
}
