package com.tiket.analytics.web.page;

import com.tiket.model.Session;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.WebDriver;

import java.util.Map;
import java.util.stream.Collectors;

public abstract class AnalyticsUrlPageWeb<T extends WebDriver> extends WebPage<T> {

    public AnalyticsUrlPageWeb(Session<T> session) {
        super(session);
    }

    public static AnalyticsUrlPageWeb<?> getInstance(Session<WebDriver> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new AnalyticsUrlPageDWeb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new AnalyticsUrlPageMWeb<>(session);
            }
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    public abstract void openHomePageEn();
    public abstract void openUrl(String path);

    public String getUtmProperties(String utmCampaign, String utmContent, String utmId, String utmMedium, String utmSource, String utmTerm) {
        String properties;
        Map<String, String> utmParameters = Map.of(
                "utm_campaign", utmCampaign,
                "utm_content", utmContent,
                "utm_id", utmId,
                "utm_medium", utmMedium,
                "utm_source", utmSource,
                "utm_term", utmTerm
        );
        properties = utmParameters.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining("&"));
        return properties;
    }
}