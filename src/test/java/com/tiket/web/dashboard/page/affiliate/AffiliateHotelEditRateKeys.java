package com.tiket.web.dashboard.page.affiliate;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.BaseTest;
import com.tiket.web.WebPage;
import org.openqa.selenium.*;

import java.util.Optional;

public class AffiliateHotelEditRateKeys<T extends WebDriver> extends WebPage<T> {
    public AffiliateHotelEditRateKeys(Session<T> session) {
        super(session);
    }

    private static final Locator PARTNER_ID = new Locator("PARTNER_ID", By.id("partner-id"));
    private static final Locator PARTNER_NAME = new Locator("PARTNER_NAME", By.xpath("//input[@id='partner-name' and @disabled]"));
    private static final Locator NEW_MAPPING_10_CHECKBOX_RATE_KEY = new Locator("NEW_MAPPING_10_CHECKBOX_RATE_KEY", By.xpath("//*[contains(@class,'Check_children') and contains(text(),'new mapping 10')]"));
    private static final Locator COBA_TEST_CHECKBOX_PROMOS = new Locator("COBA_TEST_CHECKBOX_PROMOS", By.xpath("//*[contains(@class,'Check_children') and contains(text(),'Coba Test')]"));
    private static final Locator BREAKFAST_CHECKBOX_RATE_PLANS = new Locator("BREAKFAST_CHECKBOX_RATE_PLANS", By.xpath("//*[contains(@class,'Check_children') and contains(text(),'Breakfast')]"));
    private static final Locator SAVE_BTN = new Locator("SAVE_BTN", By.xpath("//button[text()='Save']"));
    private static final Locator CONFIRM_SAVE_BTN = new Locator("CONFIRM_SAVE_BTN", By.xpath("(//button[text()='Save'])[2]"));
    private static final Locator SNACKBAR = new Locator("SNACKBAR", By.xpath("//div[contains(@class, 'Snackbar_text_content')]"));
    private static final Locator EDIT_RATEKEYS = new Locator("EDIT_RATEKEYS", By.xpath("//span[text()='Edit Rate Keys']"));

    public boolean checkPartnerIdData(String partnerIdInput, String partnerNameInput) {
        driverTools.waitForElementToAppear(PARTNER_ID, 5);
        String partnerId = driverTools.getValueAttribute(PARTNER_ID, "value");
        driverTools.waitForElementToAppear(PARTNER_NAME, 5);
        String partnerName = driverTools.getValueAttribute(PARTNER_NAME, "value");
        logger.log("partnerName -> " + partnerName + " partnerNameInput -> " + partnerNameInput);
        logger.log(" partnerId -> " + partnerId + "partnerIdInput -> " + partnerIdInput);
        return partnerId.contains(partnerIdInput) && partnerName.contains(partnerNameInput);
    }

    public void clickEditRatekey(){
        driverTools.waitForElementToAppear(EDIT_RATEKEYS, 5);
        driverTools.click(EDIT_RATEKEYS);
    }

    public boolean checkNewMapping10CheckboxRateKey() {
        try {
            driverTools.waitForElementToAppear(NEW_MAPPING_10_CHECKBOX_RATE_KEY, 5);
            driverTools.clickWithJavascriptExecutor(NEW_MAPPING_10_CHECKBOX_RATE_KEY);
            return true;
        } catch (Exception e) {
            logger.log("Failed to click NEW_MAPPING_10_CHECKBOX_RATE_KEY: " + e.getMessage());
            return false;
        }
    }

    public boolean checkCobaTestCheckboxPromos() {
        try {
            driverTools.waitForElementToAppear(COBA_TEST_CHECKBOX_PROMOS, 5);
            driverTools.scrollToElement(COBA_TEST_CHECKBOX_PROMOS);
            driverTools.clickWithJavascriptExecutor(COBA_TEST_CHECKBOX_PROMOS);
            return true;
        } catch (Exception e) {
            logger.log("Failed to click COBA_TEST_CHECKBOX_PROMOS: " + e.getMessage());
            return false;
        }
    }

    public boolean checkBreakfastCheckboxRatePlans() {
        try {
            driverTools.waitForElementToAppear(BREAKFAST_CHECKBOX_RATE_PLANS, 5);
            driverTools.scrollToElement(BREAKFAST_CHECKBOX_RATE_PLANS);
            driverTools.clickWithJavascriptExecutor(BREAKFAST_CHECKBOX_RATE_PLANS);
            return true;
        } catch (Exception e) {
            logger.log("Failed to click BREAKFAST_CHECKBOX_RATE_PLANS: " + e.getMessage());
            return false;
        }
    }

    public boolean updateRateKeys() {
        boolean newMapping10Success = checkNewMapping10CheckboxRateKey();
        boolean cobaTestSuccess = checkCobaTestCheckboxPromos();
        boolean breakfastSuccess = checkBreakfastCheckboxRatePlans();
        return newMapping10Success && cobaTestSuccess && breakfastSuccess;
    }

    public void saveUpdatedRateKeyDetails() {
        driverTools.scrollToElement(SAVE_BTN);
        driverTools.waitForElementToAppear(SAVE_BTN, 5);
        driverTools.click(SAVE_BTN);

        driverTools.waitForElementToAppear(CONFIRM_SAVE_BTN, 5);
        driverTools.click(CONFIRM_SAVE_BTN);
    }

    public boolean checkSnackbarText(String expectedSnackbarText) {
        driverTools.waitForElementToAppear(SNACKBAR, 2).orElseThrow();
        String snackbarText = driverTools.getText(SNACKBAR).orElseThrow();
        return driverTools.isElementPresent(SNACKBAR) && snackbarText.contains(expectedSnackbarText);
    }
}