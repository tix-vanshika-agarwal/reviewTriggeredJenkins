package com.tiket.web.dashboard.page.affiliate;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class AffiliateFlightDashboardGeneralConfigPageWeb<T extends WebDriver> extends WebPage<T> {
    public AffiliateFlightDashboardGeneralConfigPageWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> AffiliateFlightDashboardGeneralConfigPageWeb<?> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new AffiliateFlightDashboardGeneralConfigPageDWeb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new AffiliateFlightDashboardGeneralConfigPageMWeb<>(session);
            }
            default -> throw new NotImplementedException("Unsupported platform: " + Configs.PLATFORM);
        }
    }

    private static final Locator REFUND_CHARGE_INPUT = new Locator("REFUND_CHARGE_INPUT", By.xpath("//input[@name='refundCharge']"));
    private static final Locator SAVE_BUTTON = new Locator("SAVE_BUTTON", By.xpath("//button[@type='submit']"));

    public void inputRefundCharge(String amount) {
        driverTools.waitForElementToAppear(REFUND_CHARGE_INPUT, 5);
        driverTools.clearInput(REFUND_CHARGE_INPUT);
        driverTools.sendKeys(REFUND_CHARGE_INPUT, amount);
    }

    public void clickSaveButton() {
        driverTools.scrollElementToCenter(SAVE_BUTTON);
        driverTools.click(SAVE_BUTTON);
    }

    public String getRefundChargeValue() {
        driverTools.waitForElementToAppear(REFUND_CHARGE_INPUT, 5);
        return driverTools.getValue(REFUND_CHARGE_INPUT);
    }
}
