package com.tiket.SmartUI.page.common;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebDriver;

public class CommonSmartPageIOS<T extends WebDriver> extends CommonSmartPage<T> {
    public static final Locator PROMO_BUTTON = new Locator("PROMO_BUTTON", AppiumBy.name("tds_ic_discount_ribbon"));

    public static final Locator VOUCHER_BOX_BUTTON = new Locator("VOUCHER_BOX_BUTTON", AppiumBy.name("Voucher Box"));
    public static final Locator HOTEL_LOGIN_BANNER = new Locator("HOTEL_LOGIN_BANNER", AppiumBy.name("tds ic contact details"));
    public CommonSmartPageIOS(Session<T> session) {
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