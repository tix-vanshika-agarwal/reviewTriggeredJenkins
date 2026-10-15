package com.tiket.SmartUI.page.platform;

import com.tiket.appium.Swipe;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebDriver;

public class PlatformSmartPageIOS<T extends WebDriver> extends PlatformSmartPage<T> {
    public static final Locator LOCATION_BANNER = new Locator("LOCATION_BANNER", AppiumBy.name("banner_location"));
    public static final Locator BACK_BUTTON = new Locator("BACK_BUTTON",AppiumBy.xpath("(//XCUIElementTypeButton[@name='back'])[2]"));
    public static final Locator BACK_BUTTON_SRP = new Locator("BACK_BUTTON",AppiumBy.xpath("(//XCUIElementTypeButton[@name='back'])"));
    public static final Locator BACK_BUTTON_LANDING_PAGE = new Locator("BACK_BUTTON_LANDING_PAGE",AppiumBy.accessibilityId("tds_ic_chevron_left"));
    public static final Locator SEE_THE_PREVIOUS_PAGE = new Locator("SEE_THE_PREVIOUS_PAGE",AppiumBy.xpath("//*[contains(@label,'See the previous page') or contains(@label,'Lihat halaman sebelumnya')]"));
    public static final Locator CLICK_ON_CROSS_BTN = new Locator("CLICK_ON_CROSS_BTN",AppiumBy.accessibilityId("ic cancel grey"));
    public static final Locator HOME_PAGE_GLOBAL_SEARCH = new Locator("HOME_PAGE_GLOBAL_SEARCH", AppiumBy.accessibilityId("gs_search"));

    public PlatformSmartPageIOS(Session<T> session) {
        super(session);
    }

    @Override
    public boolean verifyLocationBanner() {
        driverTools.swipeToElement(LOCATION_BANNER, Swipe.UP, 30);
        boolean bannerPresent = driverTools.isElementDisplayed(LOCATION_BANNER);
        driverTools.swipeUp(1);
        return bannerPresent;
    }
}