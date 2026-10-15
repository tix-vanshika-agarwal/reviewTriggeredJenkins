package com.tiket.page.loyalty.partnerPage;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebDriver;

public class LoyaltyPartnerPageAndroid<T extends WebDriver> extends LoyaltyPartnerPage<T> {

    private static final Locator HEADER = new Locator("HEADER", AppiumBy.xpath("//android.widget.TextView[@text='Loyalty Program']"));
    private static final Locator BACK_BUTTON = new Locator("BACK_BUTTON", AppiumBy.className("android.widget.ImageButton"));
    private static final Locator OUR_PARTNERS = new Locator("OUR_PARTNERS", AppiumBy.id("tv_partners"));
    private static final Locator PARTNER_IMAGE = new Locator("PARTNER_IMAGE", AppiumBy.id("cl_card"));

    public LoyaltyPartnerPageAndroid(Session<T> session) {
        super(session);
    }

    @Override
    public Boolean isLoyaltyPartnerPageOpen() {
        boolean isHeaderPresent = driverTools.isElementDisplayed(HEADER);
        boolean isBackButtonPresent = driverTools.isElementDisplayed(BACK_BUTTON);
        boolean isOurPartnersPresent = driverTools.isElementDisplayed(OUR_PARTNERS);
        boolean isPartnerImagePresent = driverTools.isElementDisplayed(PARTNER_IMAGE);
        logger.log("is header present: " + isHeaderPresent);
        logger.log("is back button present: " + isBackButtonPresent);
        logger.log("is our partners present: " + isOurPartnersPresent);
        logger.log("is partner image present: " + isPartnerImagePresent);
        return isHeaderPresent && isBackButtonPresent && isOurPartnersPresent && isPartnerImagePresent;
    }
}
