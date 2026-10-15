package com.tiket.web.dashboard.page.affiliate;

import com.tiket.appium.Swipe;
import com.tiket.model.Environment;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class AffiliateFlightDashboardPage<T extends WebDriver> extends BasePage<T> {

    public AffiliateFlightDashboardPage(Session<T> session) {
        super(session);
    }

    private static final Locator FIRST_DATA_ROW = new Locator("FIRST_DATA_ROW", By.xpath("(//tr[contains(@class,'odd:bg-f4f7fe') or contains(@class,'even:bg-white')])[1]"));
    private static final Locator STATUS_ACTIVE = new Locator("STATUS_ACTIVE", By.xpath("(//div[text()='Active'])[1]"));
    private static final Locator STATUS_INACTIVE = new Locator("STATUS_INACTIVE", By.xpath("(//div[text()='Inactive'])[1]"));
    private static final Locator STATUS_TOGGLE = new Locator("STATUS_TOGGLE", By.xpath("(//div[contains(@class,'Toggle_toggle_small')])[1]"));
    private static final Locator STATUS_YES_BTN = new Locator("STATUS_YES_BTN", By.xpath("//button[text()='Yes']"));
    private static final Locator SETTINGS_DROPDOWN = new Locator("SETTINGS_DROPDOWN", By.xpath("(//input[@id='settings'])[1]"));
    private static final Locator GENERAL_CONFIG = new Locator("GENERAL_CONFIG", By.xpath("//span[text()='General Config']"));
    private static final Locator EDIT_FINANCIAL_DETAILS = new Locator("EDIT_FINANCIAL_DETAILS", By.xpath("//span[text()='Edit Finance Detail']"));
    private static final Locator SETUP_COMMISSION_BUTTON = new Locator("SETUP_COMMISSION_BUTTON", By.xpath("//span[normalize-space(text())='Setup Commission']"));
    private static final Locator AIRLINES_EXCLUSION = new Locator("AIRLINES_EXCLUSION", By.xpath("//span[normalize-space(text())='Airlines Exclusion']"));
    private static final Locator MANAGE_CREDENTIALS= new Locator("MANAGE_CREDENTIALS", By.xpath("//span[normalize-space(text())='Manage Credentials']"));
    public static final Locator RATE_TYPE_DROPDOWN = new Locator("RATE_TYPE_DROPDOWN", By.xpath("//div[contains(@class,'styles_right_slot_wrapper')]/button"));
    public static final Locator RATE_TYPE_SELECTED_VALUE = new Locator("RATE_TYPE_SELECTED_VALUE", By.xpath("//input[@aria-label='rateType Dropdown' and @readonly]"));
    public static final Locator OPTION_NET_PRICE = new Locator("OPTION_NET_PRICE", By.xpath("//li[@data-value='NET' and contains(text(), 'Net Price')]"));
    public static final Locator OPTION_SELL_PRICE = new Locator("OPTION_SELL_PRICE", By.xpath("//li[@data-value='SELL' and contains(text(), 'Sell Price')]"));
    private static final Locator FLIGHTS_PARTNER_LIST_BREADCRUMB = new Locator("FLIGHTS_PARTNER_LIST_BREADCRUMB", By.xpath("//a[contains(@href, '/admin/affiliate/flight')]//span[normalize-space(text())='Flights API Partner List']"));
    private static final Locator PARTNER_ID_SEARCH_INPUT = new Locator("PARTNER_ID_SEARCH_INPUT", By.xpath("//input[@id='partner-id']"));
    private static final Locator SEARCH_BUTTON = new Locator("SEARCH_BUTTON", By.xpath("//button[text()='Search']"));

    public void clickFlight(){
        try {
            if (Configs.ENVIRONMENT == Environment.GK) {
                driverTools.get("https://gatotkaca.tiket.com/admin/affiliate/flight");
            } else if (Configs.ENVIRONMENT == Environment.PREPROD) {
                driverTools.get("https://preprod.tiket.com/admin/affiliate/flight");
            } else if (Configs.ENVIRONMENT == Environment.PROD) {
                driverTools.get("https://www.tiket.com/admin/affiliate/flight");
            }
            logger.log("Navigated to Flight Page.");
        } catch (Exception e) {
            logger.fail("Failed to Navigated to Flight Page.");
            throw e;
        }
    }

    public boolean checkFlightSearch(String searchValue){
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

    public void toggleStatusActive(){
        if(isStatusInactive()){
            driverTools.waitForElementToAppear(STATUS_TOGGLE, 5);
            driverTools.click(STATUS_TOGGLE);
            driverTools.waitForElementToAppear(STATUS_YES_BTN, 5);
            driverTools.click(STATUS_YES_BTN);
        }
    }

    public void toggleStatusInactive(){
        if(isStatusActive()){
            driverTools.waitForElementToAppear(STATUS_TOGGLE, 5);
            driverTools.click(STATUS_TOGGLE);
            driverTools.waitForElementToAppear(STATUS_YES_BTN, 5);
            driverTools.click(STATUS_YES_BTN);
        }
    }

    public String getCurrentRateType() {
        driverTools.click(RATE_TYPE_DROPDOWN);
        return driverTools.getAttribute(RATE_TYPE_SELECTED_VALUE.by(), "value").trim();
    }

    public void toggleRateTypeBasedOnCurrent(String currentRateType) {
        driverTools.click(RATE_TYPE_DROPDOWN);

        if (currentRateType.equalsIgnoreCase("Sell Price")) {
            driverTools.click(OPTION_NET_PRICE);
        } else {
            driverTools.click(OPTION_SELL_PRICE);
        }

    }

    public boolean isRateTypeUpdated(String currentRateType , String updatedRateType) {
        return !currentRateType.equalsIgnoreCase(updatedRateType);
    }

    public void clickSettings(){
        driverTools.click(SETTINGS_DROPDOWN);
    }

    public void clickEditFinancialDetails(){
        driverTools.click(EDIT_FINANCIAL_DETAILS);
    }

    public void clickGeneralConfig() {
        driverTools.click(GENERAL_CONFIG);
    }

    public void clickSetupCommission(){
        try {
            driverTools.waitForElementToAppear(SETUP_COMMISSION_BUTTON, 5).orElseThrow();
            driverTools.click(SETUP_COMMISSION_BUTTON);
            logger.log("Clicked on Setup Commission button.");
        } catch (Exception e) {
            logger.fail("Failed to click on Setup Commission button.");
            throw e;
        };
    }

    public void clickAirlinesExclusion(){
        driverTools.click(AIRLINES_EXCLUSION);
    }

    public void clickManageCredentials(){
        driverTools.swipeToElement(MANAGE_CREDENTIALS, Swipe.DOWN , 5);
        driverTools.click(MANAGE_CREDENTIALS);
    }

    public boolean isNavigatedToCredentialPage() {
        driverTools.waitForElementToAppear(FLIGHTS_PARTNER_LIST_BREADCRUMB, 3).orElseThrow();
        String currentUrl = driver.getCurrentUrl();
        logger.log("Current URL: " + currentUrl);
        return currentUrl.contains("/admin/affiliate/flight/credential/");
    }

    public void searchByPartnerId(String partnerId) {
        driverTools.waitForElementToAppear(PARTNER_ID_SEARCH_INPUT, 5);
        driverTools.sendKeys(PARTNER_ID_SEARCH_INPUT, partnerId);
        driverTools.click(SEARCH_BUTTON);
    }

}