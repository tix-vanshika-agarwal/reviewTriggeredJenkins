package com.tiket.SmartUI.page.common;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebDriver;

public class CommonSmartPageAndroid<T extends WebDriver> extends CommonSmartPage<T> {
    public static final Locator PROMO_BUTTON = new Locator("PROMO_BUTTON", AppiumBy.xpath("(//android.view.View[@resource-id='com.tiket.gits:id/app_bar_item_container'])[2]"));

    public static final Locator VOUCHER_BOX_BUTTON = new Locator("VOUCHER_BOX_BUTTON", AppiumBy.id("container_voucher_entry"));
    public static final Locator HOTEL_LOGIN_BANNER = new Locator("HOTEL_LOGIN_BANNER", AppiumBy.id("tds_content_notification_banner"));

    public CommonSmartPageAndroid(Session<T> session) {
        super(session);
    }

    @Override
    public void openVoucherBox() {
        driverTools.click(PROMO_BUTTON);
        driverTools.click(VOUCHER_BOX_BUTTON);
    }

    @Override
    public void waitForHotelLoginBanner() {
        driverTools.waitForElementToAppear(HOTEL_LOGIN_BANNER);
    }
}