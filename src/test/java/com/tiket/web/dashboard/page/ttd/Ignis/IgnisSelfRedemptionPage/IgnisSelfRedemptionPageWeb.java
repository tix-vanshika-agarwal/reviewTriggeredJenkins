package com.tiket.web.dashboard.page.ttd.Ignis.IgnisSelfRedemptionPage;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class IgnisSelfRedemptionPageWeb<T extends WebDriver> extends WebPage<T> {
    public IgnisSelfRedemptionPageWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> IgnisSelfRedemptionPageWeb<?> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new IgnisSelfRedemptionPageDweb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new IgnisSelfRedemptionPageMweb<>(session);
            }
            default -> throw new NotImplementedException("Unsupported platform: " + Configs.PLATFORM);
        }
    }

    private static final Locator QR_CODE = new Locator("QR_CODE", By.xpath("//img[@class='preview__qr-code']"));

    public boolean isQRVisible() {
        driverTools.dummyWait(1);
        return driverTools.waitForElementToAppear(QR_CODE).isPresent();
    }
}
