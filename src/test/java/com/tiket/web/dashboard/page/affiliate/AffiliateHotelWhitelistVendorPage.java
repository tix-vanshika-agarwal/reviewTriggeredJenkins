package com.tiket.web.dashboard.page.affiliate;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.web.WebPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class AffiliateHotelWhitelistVendorPage<T extends WebDriver> extends WebPage<T> {
    public AffiliateHotelWhitelistVendorPage(Session<T> session) {
        super(session);
    }
    private static final Locator VENDOR_TOGGLE_1 = new Locator("VENDOR_TOGGLE_1", By.xpath("(//div[contains(@class, 'Toggle_toggle_small')])[1]"));
    private static final Locator YES_BUTTON = new Locator("YES_BUTTON", By.xpath("//button[normalize-space()='Yes']"));
    private static final Locator EXPEDIA_WHITELIST_TOGGLE_SNACKBAR = new Locator("EXPEDIA_WHITELIST_TOGGLE_SNACKBAR", By.xpath("//div[contains(@class, 'Snackbar_text_content') and (contains(text(), 'EXPEDIA successfully added') or contains(text(), 'EXPEDIA successfully removed'))]"));

    public void clickVendorToggle1() {
        driverTools.waitForElementToAppear(VENDOR_TOGGLE_1, 5);
        driverTools.click(VENDOR_TOGGLE_1);
    }

    public void clickYesButton() {
        driverTools.waitForElementToAppear(YES_BUTTON, 5);
        driverTools.click(YES_BUTTON);
    }

    public boolean isExpediaWhitelistSnackbarToggleDisplayed() {
        driverTools.waitForElementToAppear(EXPEDIA_WHITELIST_TOGGLE_SNACKBAR, 2);
        return driverTools.isElementPresent(EXPEDIA_WHITELIST_TOGGLE_SNACKBAR);
    }
}