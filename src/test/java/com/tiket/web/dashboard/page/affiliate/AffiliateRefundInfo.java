package com.tiket.web.dashboard.page.affiliate;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class AffiliateRefundInfo<T extends WebDriver> extends BasePage<T> {
    public AffiliateRefundInfo(Session<T> session) {
        super(session);
    }

    private static final Locator HOTEL_REFUND_DETAILS_HEADING = new Locator("HOTEL_REFUND_DETAILS_HEADING", By.xpath("//*[normalize-space()='Hotel Refund Details']"));

    public String getRefundDetailPageUrl() {
        driverTools.waitForElementToAppear(HOTEL_REFUND_DETAILS_HEADING, 10);
        return driver.getCurrentUrl();
    }

    public String getPageHeading() {
        driverTools.waitForElementToAppear(HOTEL_REFUND_DETAILS_HEADING, 10);
        return driverTools.getText(HOTEL_REFUND_DETAILS_HEADING).orElseThrow();
    }
}
