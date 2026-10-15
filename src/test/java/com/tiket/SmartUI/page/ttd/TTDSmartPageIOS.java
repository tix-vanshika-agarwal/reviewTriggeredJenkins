package com.tiket.SmartUI.page.ttd;

import com.tiket.appium.Swipe;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class TTDSmartPageIOS<T extends WebDriver> extends TTDSmartPage<T> {
    public static final Locator SEARCH_BAR = new Locator("SEARCH_BAR", AppiumBy.xpath("//*[@type='XCUIElementTypeButton' and ./parent::*[@type='XCUIElementTypeOther' and ./preceding-sibling::*[@name='tds ic back']]]"));

    public static final Locator PRODUCT_PRICE_LANDING_PAGE = new Locator("PRODUCT_PRICE_LANDING_PAGE", By.xpath("(//*[contains(@label,'tiket FLEXI') or contains(@name,'tiket FLEXI') or contains(@value,'tiket FLEXI')])[1]"));
    public TTDSmartPageIOS(Session<T> session) {
        super(session);
    }

    @Override
    public void scrollToProductCardLandingPage() {
        driverTools.swipeToElement(PRODUCT_PRICE_LANDING_PAGE, Swipe.UP, 30);
    }

    @Override
    public void clickSearchBar() {
        driverTools.click(SEARCH_BAR);
    }


}