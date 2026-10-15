package com.tiket.web.dashboard.page.affiliate;

import com.tiket.model.Environment;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class AffiliateTTDDashboardPage<T extends WebDriver> extends BasePage<T> {
    public AffiliateTTDDashboardPage(Session<T> session) {
        super(session);
    }

    private static final Locator FIRST_DATA_ROW = new Locator("FIRST_DATA_ROW", By.xpath("(//tr[contains(@class,'odd:bg-f4f7fe') or contains(@class,'even:bg-white')])[1]"));
    private static final Locator COMPANY_NAME = new Locator("COMPANY_NAME", By.name("partnerName"));
    private static final Locator STATUS_ACTIVE = new Locator("STATUS_ACTIVE", By.xpath("(//div[text()='Active'])[1]"));
    private static final Locator STATUS_INACTIVE = new Locator("STATUS_INACTIVE", By.xpath("(//div[text()='Inactive'])[1]"));
    private static final Locator STATUS_TOGGLE = new Locator("STATUS_TOGGLE", By.xpath("(//div[contains(@class,'Toggle_toggle_small')])[1]"));
    private static final Locator STATUS_YES_BTN = new Locator("STATUS_YES_BTN", By.xpath("//button[text()='Yes']"));
    private static final Locator SETTINGS_DROPDOWN = new Locator("SETTINGS_DROPDOWN", By.xpath("//input[@id='settings']"));
    private static final Locator EDIT_PARTNER_VOUCHER = new Locator("EDIT_PARTNER_VOUCHER", By.xpath("//span[text()='Edit Partner Voucher']"));
    private static final Locator EDIT_WEBHOOK = new Locator("EDIT_WEBHOOK", By.xpath("//span[text()='Edit Webhook']"));
    private static final Locator EDIT_PARTNER_LOGO_TITLE = new Locator("EDIT_PARTNER_LOGO_TITLE", By.xpath("//span[normalize-space(text())='Edit Partner Logo']"));
    private static final Locator SAVE_BUTTON = new Locator("SAVE_BUTTON", By.xpath("//button[normalize-space(text())='Save' and @type='submit']"));
    private static final Locator ADD_NEW_BUTTON = new Locator("ADD_NEW_BUTTON", By.xpath("//button[normalize-space(text())='Add New']"));
    private static final Locator OPEN_SELECT_PARTNER_DROPDOWN = new Locator("OPEN_SELECT_PARTNER_DROPDOWN", By.xpath("//input[@id='partner-name-or-id']"));
    private static final Locator FIRST_PARTNER_OPTION = new Locator("FIRST_PARTNER_OPTION", By.xpath("(//ul[contains(@class,'InputSearch_dropdown')]//li)[1]"));
    private static final Locator CREATE_BUTTON = new Locator("CREATE_BUTTON", By.xpath("//button[normalize-space(text())='Create']"));
    private static final Locator NEXT_BUTTON = new Locator("NEXT_BUTTON", By.xpath("//button[normalize-space(text())='Next']"));
    private static final Locator ORDER_WEBHOOK_INPUT = new Locator("ORDER_WEBHOOK_INPUT", By.xpath("//input[@id='order-webhook' and @name='orderWebhookUrl']"));
    private static final Locator VOUCHER_WEBHOOK_INPUT = new Locator("VOUCHER_WEBHOOK_INPUT", By.xpath("//input[@id='voucher-webhook' and @name='voucherWebhookUrl']"));
    private static final Locator ACCOUNT_CREATED_SUCCESSFULLY_TEXT = new Locator("ACCOUNT_CREATED_SUCCESSFULLY_TEXT", By.xpath("//span[normalize-space(text())='You have successfully created a new TTD API partner']"));
    private static final Locator SNACKBAR_PARTNER_VOUCHER_SAVED = new Locator("SNACKBAR_PARTNER_VOUCHER_SAVED", By.xpath("//div[contains(@class, 'Snackbar_text_content') and normalize-space(text())='Partner Voucher successfully saved']"));
    private static final Locator EDITED_WEBHOOK_SUCCESSFULLY = new Locator("EDITED_WEBHOOK_SUCCESSFULLY", By.xpath("//div[contains(@class, 'Snackbar_text_content') and normalize-space(text())='Web hooks successfully saved']"));

    public void clickTTD() {
        try {
            if (Configs.ENVIRONMENT == Environment.GK) {
                driverTools.get("https://gatotkaca.tiket.com/admin/affiliate/ttd");
            } else if (Configs.ENVIRONMENT == Environment.PREPROD) {
                driverTools.get("https://preprod.tiket.com/admin/affiliate/ttd");
            } else if (Configs.ENVIRONMENT == Environment.PROD) {
                driverTools.get("https://www.tiket.com/admin/affiliate/ttd");
            }
            logger.log("Navigated to TTD Page.");
        } catch (Exception e) {
            logger.fail("Failed to navigate to TTD Page.");
            throw e;
        }
    }

    public boolean checkTTDSearch(String searchValue) {
        driverTools.waitForElementToAppear(FIRST_DATA_ROW, 4).orElseThrow();
        String searchData = driverTools.getText(FIRST_DATA_ROW).orElseThrow();
        return searchData.contains(searchValue);
    }

    public boolean isStatusActive() {
        driverTools.waitForElementToPresence(STATUS_ACTIVE, 3);
        return driverTools.isElementDisplayed(STATUS_ACTIVE);
    }

    public boolean isStatusInactive() {
        driverTools.waitForElementToPresence(STATUS_INACTIVE, 3);
        return driverTools.isElementDisplayed(STATUS_INACTIVE);
    }

    public void toggleStatusActive() {
        toggleStatus(isStatusActive());
    }

    public void toggleStatusInactive() {
        toggleStatus(isStatusInactive());
    }

    private void toggleStatus(boolean shouldToggle) {
        if (shouldToggle) {
            driverTools.click(STATUS_TOGGLE);
            driverTools.waitForElementToAppear(STATUS_YES_BTN, 3).orElseThrow();
            driverTools.click(STATUS_YES_BTN);
            logger.log("Status toggled to Inactive.");
        } else {
            driverTools.click(STATUS_TOGGLE);
            driverTools.waitForElementToAppear(STATUS_YES_BTN, 3).orElseThrow();
            driverTools.click(STATUS_YES_BTN);
            logger.log("Status toggled to Active.");
        }
    }


    public void clickSettings(){
        driverTools.swipeLeft();
        driverTools.click(SETTINGS_DROPDOWN);
    }

    public void clickEditPartnerVoucher() {
        driverTools.click(EDIT_PARTNER_VOUCHER);
    }


    public boolean isNavigatedToPartnerVoucherPage() {
        driverTools.waitForElementToAppear(EDIT_PARTNER_LOGO_TITLE, 3).orElseThrow();
        String currentUrl = driver.getCurrentUrl();
        logger.log("Current URL: " + currentUrl);
        return currentUrl.contains("/admin/affiliate/ttd/edit/partner-voucher");
    }

    private void clickSaveButton() {
        driverTools.click(SAVE_BUTTON);
    }

    public boolean isPartnerVoucherUpdated() {
        clickSaveButton();
        driverTools.waitForElementToAppear(SNACKBAR_PARTNER_VOUCHER_SAVED, 5);
        String snackbarText = driverTools.getText(SNACKBAR_PARTNER_VOUCHER_SAVED).orElseThrow();
        logger.log("Snackbar text: " + snackbarText);
        return snackbarText.contains("Partner Voucher successfully saved");
    }

    public void addNewPartner(String orderWebhookUrl, String voucherWebhookUrl) {
        driverTools.waitForElementToAppear(ADD_NEW_BUTTON, 1).orElseThrow();
        driverTools.click(ADD_NEW_BUTTON);
        driverTools.waitForElementToAppear(OPEN_SELECT_PARTNER_DROPDOWN, 2).orElseThrow();
        driverTools.click(OPEN_SELECT_PARTNER_DROPDOWN);
        driverTools.waitForElementToAppear(FIRST_PARTNER_OPTION, 2).orElseThrow();
        driverTools.click(FIRST_PARTNER_OPTION);
        driverTools.waitForElementToAppear(CREATE_BUTTON, 1).orElseThrow();
        driverTools.click(CREATE_BUTTON);
        driverTools.click(NEXT_BUTTON);
        driverTools.waitForElementToAppear(SAVE_BUTTON, 2).orElseThrow();
        clickSaveButton();
        driverTools.waitForElementToAppear(ORDER_WEBHOOK_INPUT, 2);
        driverTools.sendKeys(ORDER_WEBHOOK_INPUT, orderWebhookUrl);
        driverTools.sendKeys(VOUCHER_WEBHOOK_INPUT, voucherWebhookUrl);
        clickSaveButton();
    }

    public boolean isNewPartnerAdded(String expectedMessage) {
        driverTools.waitForElementToAppear(ACCOUNT_CREATED_SUCCESSFULLY_TEXT, 5);
        String successMessage = driverTools.getText(ACCOUNT_CREATED_SUCCESSFULLY_TEXT).orElseThrow();
        logger.log("Success message: " + successMessage);
        return successMessage.contains(expectedMessage);
    }

    public void clickEditWebhookButton() {
        driverTools.waitForElementToAppear(EDIT_WEBHOOK, 2).orElseThrow();
        driverTools.click(EDIT_WEBHOOK);
        driverTools.waitForElementToAppear(ORDER_WEBHOOK_INPUT, 2).orElseThrow();
        driverTools.waitForElementToAppear(VOUCHER_WEBHOOK_INPUT, 2).orElseThrow();
    }

    public void editWebhookUrls(String updatedOrderWebhook, String updatedVoucherWebhook) {
        WebElement orderWebhookInput = driverTools.waitForElementToAppear(ORDER_WEBHOOK_INPUT).orElseThrow();
        WebElement voucherWebhookInput = driverTools.waitForElementToAppear(VOUCHER_WEBHOOK_INPUT).orElseThrow();
        driverTools.clearInput(orderWebhookInput);
        driverTools.sendKeys(orderWebhookInput, updatedOrderWebhook);
        driverTools.clearInput(voucherWebhookInput);
        driverTools.sendKeys(voucherWebhookInput, updatedVoucherWebhook);
        clickSaveButton();
    }

    public boolean editedWebHookSuccessfully(String expectedMessage) {
        driverTools.waitForElementToAppear(EDITED_WEBHOOK_SUCCESSFULLY, 5);
        String successMessage = driverTools.getText(EDITED_WEBHOOK_SUCCESSFULLY).orElseThrow();
        logger.log("Success message for updated webhook: " + successMessage);
        return successMessage.contains(expectedMessage);
    }

}