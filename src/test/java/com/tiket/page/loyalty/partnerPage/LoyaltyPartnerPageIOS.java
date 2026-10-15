package com.tiket.page.loyalty.partnerPage;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebDriver;

public class LoyaltyPartnerPageIOS<T extends WebDriver> extends LoyaltyPartnerPage<T> {
    private static final Locator HEADER = new Locator("HEADER", AppiumBy.xpath("//XCUIElementTypeStaticText[@name='Loyalty Program']"));
    private static final Locator BACK_BUTTON = new Locator("BACK_BUTTON", AppiumBy.name("leftBarButtonItem"));
    private static final Locator OUR_PARTNERS = new Locator("OUR_PARTNERS", AppiumBy.xpath("//XCUIElementTypeStaticText[@name='Our Partners']"));
    private static final Locator CONNECT_BUTTON = new Locator("CONNECT_BUTTON", AppiumBy.xpath("//XCUIElementTypeButton[@name='CONNECT']"));
    private static final Locator ECLIPSES = new Locator("ECLIPSES", AppiumBy.name("icElipsesWhite"));
    private static final Locator PARTNER_IMAGE = new Locator("PARTNER_IMAGE", AppiumBy.xpath("(//XCUIElementTypeImage[@name='icElipsesWhite']/preceding-sibling::XCUIElementTypeImage)[2]"));

    public LoyaltyPartnerPageIOS(Session<T> session) {
        super(session);
    }
    @Override
    public Boolean isLoyaltyPartnerPageOpen() {
        boolean isHeaderPresent = driverTools.isElementDisplayed(HEADER);
        boolean isBackButtonPresent = driverTools.isElementDisplayed(BACK_BUTTON);
        boolean isOurPartnersPresent = driverTools.isElementDisplayed(OUR_PARTNERS);
        logger.log("is header present: " + isHeaderPresent);
        logger.log("is back button present: " + isBackButtonPresent);
        logger.log("is our partners present: " + isOurPartnersPresent);
        return isHeaderPresent && isBackButtonPresent && isOurPartnersPresent ;
    }
}