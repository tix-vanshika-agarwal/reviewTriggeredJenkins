package com.tiket.page.order.reservation;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebDriver;

public class ManagePassesPage<T extends WebDriver> extends BasePage<T> {

    public ManagePassesPage(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> ManagePassesPage<T> getInstance(Session<T> session) {
        return new ManagePassesPage<>(session);
    }

    private static final Locator FIRST_ACTIVITY_CARD = new Locator("FIRST_ACTIVITY_CARD", AppiumBy.xpath("//div[contains(@class, 'activity_selection')]"));

    public void clickFirstActivity() {
        driverTools.switchToWebViewUrl("/upcoming-visit");
        driverTools.waitForElementToAppear(FIRST_ACTIVITY_CARD);
        driverTools.click(FIRST_ACTIVITY_CARD);
        driverTools.switchToNative();
    }
}
