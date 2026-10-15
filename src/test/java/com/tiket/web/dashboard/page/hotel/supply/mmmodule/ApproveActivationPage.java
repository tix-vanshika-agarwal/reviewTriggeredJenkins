package com.tiket.web.dashboard.page.hotel.supply.mmmodule;

import com.tiket.model.Environment;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ApproveActivationPage<T extends WebDriver> extends BasePage<T> {
    public ApproveActivationPage(Session<T> session) {
        super(session);
    }

    private static final String APPROVE_ACTIVATION_URL_GK = "https://gatotkaca.tiket.com/admin/mm-dashboard/hotel-native/registration/approve-activation";
    private static final String APPROVE_ACTIVATION_URL_PREPROD = "https://preprod.tiket.com/admin/mm-dashboard/hotel-native/registration/approve-activation";
    private static final Locator HOTEL_ID_INPUT = new Locator("HOTEL_ID_INPUT", new By.ByXPath("//input[@name='hotelId' and @type='text']"));

    private static Locator hotelRowLink(String hotelId) {
        return new Locator("HOTEL_ROW_LINK", new By.ByXPath(String.format("//tr[td[2]='%s']//td[3]//a", hotelId)));
    }

    public void open() {
        driverTools.getWithDynamicTimeout(Configs.ENVIRONMENT == Environment.GK ? APPROVE_ACTIVATION_URL_GK : APPROVE_ACTIVATION_URL_PREPROD, 30);
    }

    public void searchByHotelId(String hotelId) {
        WebElement input = driverTools.waitForElementToBeClickable(HOTEL_ID_INPUT).orElseThrow();
        driverTools.sendKeys(input, hotelId);
        // ENTER can fire before the typed value registers. The value is already in the field, so only (re)submit
        // it — never re-type, which would append (e.g. "123123") and never match — until the result row appears.
        for (int attempt = 1; attempt <= 3; attempt++) {
            driverTools.waitForElementToBeClickable(HOTEL_ID_INPUT).orElseThrow().sendKeys(Keys.ENTER);
            if (driverTools.waitForElementToAppear(hotelRowLink(hotelId)).isPresent()) {
                return;
            }
            logger.log("Hotel row not found after search, retrying: " + hotelId + " (attempt " + attempt + "/3)");
        }
        throw new java.util.NoSuchElementException("Hotel row not found after searching hotel id: " + hotelId);
    }

    /** Clicks the matched hotel row link; Extranet may open in a new tab (switch) or redirect in the same tab. */
    public void openHotelAndSwitchToExtranet(String hotelId) {
        int handlesBeforeClick = driverTools.getWindowHandles().size();
        WebElement link = driverTools.waitForElementToBeClickable(hotelRowLink(hotelId)).orElseThrow();
        driverTools.click(link);
        try {
            driverTools.waitForNewTabAndSwitchToLatest(handlesBeforeClick);
        } catch (Exception sameTabRedirect) {
            logger.log("Extranet opened in the same tab (no new tab detected)");
        }
    }
}
