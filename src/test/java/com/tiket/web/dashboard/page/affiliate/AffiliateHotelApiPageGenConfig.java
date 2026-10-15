package com.tiket.web.dashboard.page.affiliate;

import com.tiket.model.Environment;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;


public class AffiliateHotelApiPageGenConfig<T extends WebDriver> extends BasePage<T> {
    public AffiliateHotelApiPageGenConfig(Session<T> session) {
        super(session);}
    private static final Locator API_PRODUCTS = new Locator("API_PRODUCTS", By.xpath("//p[text()='API Products']"));
    private static final Locator FIRST_ROW_PARTNER_DATA = new Locator("FIRST ROW_PARTNER_DATA", By.xpath("//div[contains(@class, 'styles_input_field_wrapper')]"));
    private static final Locator HOTELS_SECTION = new Locator("HOTELS_SECTION", By.xpath("(//div[text()='Hotels'])[1]"));
    private static final Locator PARTNER_ID_GEN_CONFIG = new Locator("PARTNER_ID_GEN_CONFIG", By.xpath("//input[@id='partner-id']"));
    private static final Locator PARTNER_NAME_GEN_CONFIG = new Locator("PARTNER_NAME_GEN_CONFIG", By.xpath("//input[@id='partner-name']"));
    private static final Locator GEN_CONFIG_HOTEL = new Locator("GEN_CONFIG_HOTEL", By.xpath("//span[normalize-space()='General Config']"));
    private static final Locator SETTINGS = new Locator("SETTINGS", By.xpath("//input[@id='settings']"));
    private static final Locator SETTINGS_DROPDOWN = new Locator("SETTINGS_DROPDOWN", By.xpath("(//input[@id='settings'])[1]"));
    private static final Locator MAX_PROPERTY_IDS_INPUT = new Locator("MAX_PROPERTY_IDS_INPUT", By.id("max-number-of-property-ids-in-a-search-request"));
    private static final Locator AMOUNT_IN_IDR_INPUT = new Locator("AMOUNT_IN_IDR_INPUT", By.xpath("//input[@name='refundCharge' and @placeholder='Amount in Reseller Preferred Currency']"));
    private static final Locator SAVE_BUTTON = new Locator("SAVE_BUTTON", By.xpath("//button[@type='submit' and (normalize-space(text())='Save' or normalize-space(text())='Simpan')]"));
    private static final Locator SUCCESS_SNACKBAR_MESSAGE = new Locator("SUCCESS_SNACKBAR_MESSAGE", By.xpath("//div[contains(@class, 'Snackbar_text_content') and (normalize-space(text())='The changes to the data have been saved.' or normalize-space(text())='Perubahan data telah disimpan.')]"));
    private static final Locator MAX_PROPERTY_ERR_MSG = new Locator("MAX_PROPERTY_ERR_MSG", By.xpath("//*[normalize-space(text())='Must be between 1-100']"));
    private static final Locator CHECK_IN_GUIDE_EMAIL = new Locator("CHECK_IN_GUIDE_EMAIL", By.xpath("//input[@name='nhaEmail' and @placeholder='Email Recipient']"));

    public void goToHotelAPIPage() {
        if (Configs.ENVIRONMENT == Environment.GK) {
            driver.get("https://gatotkaca.tiket.com/admin/affiliate/hotel");
        } else if (Configs.ENVIRONMENT == Environment.PREPROD) {
            driver.get("https://preprod.tiket.com/admin/affiliate/hotel");
        } else if (Configs.ENVIRONMENT == Environment.PROD) {
            driver.get("https://tiket.com/admin/affiliate/hotel");
        }
    }

    public void clickHotelApiSection(){
        driverTools.waitForElementToAppear(HOTELS_SECTION, 5);
        driverTools.click(HOTELS_SECTION);
        driverTools.waitForElementToAppear(FIRST_ROW_PARTNER_DATA, 5);
        driverTools.waitForElementToBeClickable(SETTINGS,5);
        driverTools.click(SETTINGS);
    }

    public void clickGenConfigHotelAPI() {
        driverTools.waitForElementToAppear(GEN_CONFIG_HOTEL,5);
        driverTools.click(GEN_CONFIG_HOTEL);
    }

    public boolean checkPartnerIdIsDisabled() {
        driverTools.waitForElementToAppear(PARTNER_ID_GEN_CONFIG);
        return driverTools.isElementSelected(PARTNER_ID_GEN_CONFIG);

    }

    public boolean checkPartnerNameIsDisabled() {
        driverTools.waitForElementToAppear(PARTNER_NAME_GEN_CONFIG);
        return driverTools.isElementSelected(PARTNER_NAME_GEN_CONFIG);

    }

    public void setMaxPropertyIdsInput(String value) {
        driverTools.waitForElementToAppear(MAX_PROPERTY_IDS_INPUT);
        driverTools.clearInput(MAX_PROPERTY_IDS_INPUT);
        driverTools.sendKeys(MAX_PROPERTY_IDS_INPUT, value);
    }

    public void setMaxPropertyToZero(String value) {
        driverTools.waitForElementToAppear(MAX_PROPERTY_IDS_INPUT);
        driverTools.clearInput(MAX_PROPERTY_IDS_INPUT);
        driverTools.sendKeys(MAX_PROPERTY_IDS_INPUT, value);
    }

    public void setAmountInIdrInput(String value) {
        driverTools.waitForElementToAppear(AMOUNT_IN_IDR_INPUT);
        driverTools.clearInput(AMOUNT_IN_IDR_INPUT);
        driverTools.sendKeys(AMOUNT_IN_IDR_INPUT, value);
    }

    public void generateConfigHotelApi(String maxPropertyIds, String amountInIdr) {
        driverTools.waitForElementToAppear(PARTNER_NAME_GEN_CONFIG);
        setMaxPropertyIdsInput(maxPropertyIds);
        setAmountInIdrInput(amountInIdr);
        clickSaveButton();
    }

    public void clickSaveButton() {
        driverTools.waitForElementToBeClickable(SAVE_BUTTON);
        driverTools.scrollToElement(SAVE_BUTTON);
        driverTools.click(SAVE_BUTTON);
    }

    public boolean isSaveSuccess() {
        driverTools.waitForElementToAppear(SUCCESS_SNACKBAR_MESSAGE, 5);
        return driverTools.isElementDisplayed(SUCCESS_SNACKBAR_MESSAGE);
    }

    public void goToGenerateConfigHotel(){
        driverTools.waitForElementToAppear(HOTELS_SECTION, 5);
        driverTools.click(HOTELS_SECTION);
        driverTools.waitForElementToBeClickable(SETTINGS_DROPDOWN, 2);
        driverTools.click(SETTINGS_DROPDOWN);
        driverTools.waitForElementToAppear(GEN_CONFIG_HOTEL);
        driverTools.click(GEN_CONFIG_HOTEL);
    }

    public boolean isPartnerDataDisplayed() {
        driverTools.waitForElementToAppear(PARTNER_ID_GEN_CONFIG);
        return driverTools.isElementDisplayed(PARTNER_ID_GEN_CONFIG) && driverTools.isElementDisplayed(PARTNER_NAME_GEN_CONFIG);
    }

    public boolean isMaxPropertyErrMsgDisplayed() {
        driverTools.waitForElementToAppear(MAX_PROPERTY_ERR_MSG);
        return driverTools.isElementDisplayed(MAX_PROPERTY_ERR_MSG);
    }

    public boolean isMaxPropertyIdsInputDisplayed() {
        driverTools.waitForElementToAppear(MAX_PROPERTY_IDS_INPUT);
        return driverTools.isElementDisplayed(MAX_PROPERTY_IDS_INPUT);
    }
}
