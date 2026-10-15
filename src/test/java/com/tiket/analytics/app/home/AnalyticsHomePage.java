package com.tiket.analytics.app.home;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Deeplink;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.WebDriver;

import java.util.Map;
import java.util.stream.Collectors;

public abstract class AnalyticsHomePage <T extends WebDriver> extends BasePage<T> {
    public AnalyticsHomePage(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> AnalyticsHomePage<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case ANDROID -> {
                return new AnalyticsHomePageAndroid<>(session);
            }
            case IOS -> {
                return new AnalyticsHomePageIOS<>(session);
            }
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    public abstract void restartApp();

    public abstract void openVerticalPage(String vertical);

    protected String translateVerticalNameToBhs(String vertical) {
        String translatedVerticalName = CoreFunctions.verticalEngToId(vertical);
        return translatedVerticalName == null ? vertical : translatedVerticalName;
    }

    public void openDeeplink(String deeplink) {
        Deeplink deepLink = new Deeplink("OPEN_DEEPLINK", deeplink, deeplink);
        driverTools.deeplink(deepLink);
    }

    public String getUtmProperties(String utmCampaign, String utmContent, String utmId, String utmMedium, String utmSource, String utmTerm) {
        String utmProperties;
        Map<String, String> utmParameters = Map.of(
                "utm_campaign", utmCampaign,
                "utm_content", utmContent,
                "utm_id", utmId,
                "utm_medium", utmMedium,
                "utm_source", utmSource,
                "utm_term", utmTerm
        );
        utmProperties = utmParameters.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining("&", "&", ""));
        return utmProperties;
    }
}