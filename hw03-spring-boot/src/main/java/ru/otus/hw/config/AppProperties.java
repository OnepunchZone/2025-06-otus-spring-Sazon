package ru.otus.hw.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Locale;
import java.util.Map;

@ConfigurationProperties(prefix = "test")
public class AppProperties implements TestConfig, TestFileNameProvider, LocaleConfig {

    @Getter
    private int rightAnswersCountToPass;

    @Getter
    private Locale locale;

    private Map<String, String> fileNameByLocaleTag;

    public void setLocale(String locale) {
        this.locale = Locale.forLanguageTag(locale);
    }

    public void setRightAnswersCountToPass(int rightAnswersCountToPass) {
        this.rightAnswersCountToPass = rightAnswersCountToPass;
    }

    public void setFileNameByLocaleTag(Map<String, String> fileNameByLocaleTag) {
        this.fileNameByLocaleTag = fileNameByLocaleTag;
    }

    @Override
    public String getTestFileName() {
        return fileNameByLocaleTag.get(locale.toLanguageTag());
    }
}
