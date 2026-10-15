package com.tiket.SmartUI.page.platform;

import com.tiket.appium.Swipe;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebDriver;

public class PlatformSmartPageAndroid<T extends WebDriver> extends PlatformSmartPage<T> {
    public static final Locator LOCATION_BANNER = new Locator("LOCATION_BANNER",AppiumBy.xpath("//*[contains(@text,'Beri Izin Akses Lokasi')or contains(@text,'Allow Location Access')]"));
    public static final Locator BACK_BUTTON = new Locator("BACK_BUTTON",AppiumBy.xpath("//*[contains(@resource-id, 'tds_icon_navigation_toolbar')]"));
    public static final Locator SEE_THE_PREVIOUS_PAGE = new Locator("SEE_THE_PREVIOUS_PAGE",AppiumBy.xpath("//*[contains(@text,'See the previous page') or contains(@text,'Lihat halaman sebelumnya')]"));
    public static final Locator CLICK_ON_CROSS_BTN = new Locator("CLICK_ON_CROSS_BTN",AppiumBy.xpath("//*[contains(@resource-id, 'iv_close')]"));
    public static final Locator BACK_BUTTON_LANDING_PAGE = new Locator("BACK_BUTTON_LANDING_PAGE",AppiumBy.xpath("//*[contains(@resource-id, 'button_icon_container')]/android.widget.ImageView"));
    private static final Locator HOME_PAGE_SEARCH_BAR = new Locator("HOME_PAGE_SEARCH_BAR", AppiumBy.id("tds_search_et"));

    public PlatformSmartPageAndroid(Session<T> session) {
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