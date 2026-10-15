package com.tiket.web.page.ttd.TtdDestinationPage;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public abstract class TtdDestinationPageWeb<T extends WebDriver> extends WebPage<T> {

    public TtdDestinationPageWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> TtdDestinationPageWeb<?> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new TtdDestinationPageDweb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new TtdDestinationPageMweb<>(session);
            }
            default -> throw new NotImplementedException("Unsupported platform: " + Configs.PLATFORM);
        }
    }

    public static final Locator DESTINATION_NAME = new Locator("DESTINATION_NAME", new By.ByXPath("//*[contains(@class, 'DestinationBanner_location_name')]"));

    public String getDestinationName() {
        driverTools.waitForElementToPresence(DESTINATION_NAME);
        return driverTools.getText(DESTINATION_NAME).orElseThrow();
    }
}
