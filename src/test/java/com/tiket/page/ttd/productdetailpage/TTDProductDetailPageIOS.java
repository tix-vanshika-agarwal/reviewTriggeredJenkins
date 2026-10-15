package com.tiket.page.ttd.productdetailpage;

import com.tiket.appium.Swipe;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class TTDProductDetailPageIOS<T extends WebDriver> extends TTDProductDetailPage<T> {

    //Note : please update and make sure use the latest locator ID when it's deployed by IOS dev team.
    public static final Locator POPULAR_PARTNER_PAGE = new Locator("POPULAR_PARTNER_PAGE", AppiumBy.id("TTDPartner"));
    private static final Locator PARTNER_NAME_ON_DETAIL_PAGE = new Locator("PARTNER_NAME_ON_DETAIL_PAGE", AppiumBy.accessibilityId("ttd_partner_name"));
    private static final Locator VISIBLE_TAB_BUTTON = new Locator("VISIBLE_TAB_BUTTON", AppiumBy.xpath("(//*[@name='TTDProductDetail']//XCUIElementTypeScrollView)[1]//XCUIElementTypeOther[@name and @visible='true']"));
    private static final Locator VIEW_MAP_LINK = new Locator("VIEW_MAP_LINK", AppiumBy.xpath("//XCUIElementTypeLink[@name='View Map' or @label='View Map' or @name='Lihat Peta' or @label='Lihat Peta']"));

    public TTDProductDetailPageIOS(Session<T> session) {
        super(session);
    }

    @Override
    public String getPartnerName() {
        WebElement partnerName = driverTools.waitForElementToAppear(PARTNER_NAME_ON_DETAIL_PAGE, 5).orElseThrow();
        return driverTools.getText(partnerName);
    }

    @Override
    public String getProductLocation(String location) {
        return driverTools.element(getProductLocationLocator(location)).orElseThrow().getAttribute("name");
    }

    private Locator getProductLocationLocator(String location) {
        return new Locator("PRODUCT_LOCATION", AppiumBy.xpath("(//*[@name='"+location+"'])[last()]"));
    }

    @Override
    public boolean isPopularPartnerPageValid(String name) {
        boolean partnerPageIsDisplayed = driverTools.waitForElementToAppear(POPULAR_PARTNER_PAGE).orElseThrow().isDisplayed();
        boolean partnerNameIsValid = driverTools.waitForElementToAppear(getPopularTitleLocator(name)).orElseThrow().isDisplayed();

        if (!partnerPageIsDisplayed) {
            logger.warn("Partner page is not loaded");
        }
        if (!partnerNameIsValid) {
            logger.warn("Partner name is wrong or not displayed");
        }

        return partnerPageIsDisplayed && partnerNameIsValid;
    }

    private Locator getPopularTitleLocator(String name) {
        return new Locator("PARTNER_NAME",AppiumBy.xpath("(//*[contains(@name,'"+name+"')])[last()]"));
    }

    @Override
    public void swipeUntilTabVisible() {
        int loop = 0;
        while (driverTools.isElementDisplayed(VISIBLE_TAB_BUTTON)) {
            driverTools.swipeUp();
            if (loop == 10) break;
            loop++;
        }
    }

    @Override
    public boolean checkProductNameNative(String productName) {
        return driverTools.waitForElementToPresence(getProductNameLocator(productName)).isPresent();
    }

    private static Locator getProductNameLocator(String productName) {
        return new Locator("PRODUCT_NAME_"+productName, AppiumBy.xpath("//XCUIElementTypeStaticText[@label='"+productName+"' and @visible='true']"));
    }

    @Override
    public void clickViewMap() {
        driverTools.swipeToElement(VIEW_MAP_LINK, Swipe.DOWN, 10).orElseThrow();
        driverTools.click(VIEW_MAP_LINK);
    }
}