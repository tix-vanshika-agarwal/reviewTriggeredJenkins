package com.tiket.web.dashboard.page.platform.liberty;

import com.tiket.model.Environment;
import com.tiket.model.Session;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.WebDriver;

public class LibertyDashboardHomePage<T extends WebDriver> extends WebPage<T> {
    public LibertyDashboardHomePage(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> LibertyDashboardHomePage<?> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new LibertyDashboardHomePageDweb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new LibertyDashboardHomePageMweb<>(session);
            }
            default -> throw new NotImplementedException("Unsupported platform: " + Configs.PLATFORM);
        }
    }

    public void goToMessageDashboardPage() {
        if (Configs.ENVIRONMENT == Environment.GK) {
            driver.get("https://gatotkaca.tiket.com/admin/message");
        } else if (Configs.ENVIRONMENT == Environment.PREPROD) {
            driver.get("https://preprod.tiket.com/admin/message");
        } else if (Configs.ENVIRONMENT == Environment.PROD) {
            driver.get("https://tiket.com/admin/message");
        }
    }
}