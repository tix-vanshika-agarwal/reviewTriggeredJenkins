package com.tiket.web.dashboard.page.ttd.CMS.CMSPromoAndLoyaltyBundlePage;

import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;

public class CMSPromoAndLoyaltyBundlePageWeb<T extends WebDriver> extends WebPage<T> {

    public CMSPromoAndLoyaltyBundlePageWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> CMSPromoAndLoyaltyBundlePageWeb<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new CMSPromoAndLoyaltyBundlePageDweb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new CMSPromoAndLoyaltyBundlePageMweb<>(session);
            }
            default -> throw new NotImplementedException("Unsupported platform: " + Configs.PLATFORM);
        }
    }

    private static final Locator BUNDLE_PAGE = new Locator("BUNDLE_PAGE", By.xpath("//a[@class='sidebar__nav-submenu-item' and text()='Bundle']"));
    private static final Locator CREATE_BUNDLE_BUTTON = new Locator("CREATE_BUNDLE_BUTTON", By.xpath("//button[normalize-space()='Create new Bundle']"));
    private static final Locator RADIO_ADDONS = new Locator("RADIO_ADDONS", By.xpath("//label[@class='radio-button-wrapper']//span[normalize-space()='Add-ons']"));
    private static final Locator SUBMIT_BUNDLE_TYPE_BUTTON = new Locator("SUBMIT_BUNDLE_TYPE_BUTTON", By.xpath("//button[contains(text(), 'Submit')]"));
    private static final Locator ADDONS_NAME_ID_INPUT = new Locator("ADDONS_NAME_ID_INPUT", By.xpath("//input[@name='name.ID']"));
    private static final Locator ADDONS_NAME_EN_INPUT = new Locator("ADDONS_NAME_EN_INPUT", By.xpath("//input[@name='name.EN']"));
    private static final Locator SEARCH_PRODUCT_INPUT = new Locator("SEARCH_PRODUCT_INPUT", By.xpath("//div[text()='Search']/following-sibling::div[@class='input-wrapper']/input"));
    private static final Locator MAIN_PRODUCT_CONFIGURATION_ADD_BUTTON = new Locator("MAIN_PRODUCT_CONFIGURATION_ADD_BUTTON", By.xpath("//div[contains(@class, 'bundle-name__configuration') and .//p[text()='Main Product Configuration']]//div[contains(@class, 'action-add-component')]"));
    private static final Locator ADDONS_PRODUCT_CONFIGURATION_ADD_BUTTON = new Locator("ADDONS_PRODUCT_CONFIGURATION_ADD_BUTTON", By.xpath("//div[contains(@class, 'bundle-name__configuration') and .//p[text()='Add-ons Product Configuration']]//div[contains(@class, 'action-add-component')]"));
    private static final Locator THINGS_TO_DO_LIST_FIRST_ITEM_BUTTON = new Locator("THINGS_TO_DO_LIST_FIRST_ITEM_BUTTON", By.xpath("(//button[contains(@class, 'dialog-product-add__product-item')])[1]"));
    private static final Locator ADD_THINGS_TO_DO_BUTTON = new Locator("ADD_THINGS_TO_DO_BUTTON", By.xpath("//button[contains(., 'Add Things To Do')]"));
    private static final Locator DATE_PICKER_TODAY = new Locator("DATE_PICKER_TODAY", By.xpath("//div[contains(@class, 'DayPicker-Day--today')]"));
    private static final Locator DATE_PICKER_TOMORROW = new Locator("DATE_PICKER_TOMORROW", By.xpath("//div[contains(@class, 'DayPicker-Day--today')]/following::div[contains(@class, 'DayPicker-Day')][1]"));
    private static final Locator START_DATE = new Locator("START_DATE", By.xpath("//input[@name='startDate']"));
    private static final Locator END_DATE = new Locator("END_DATE", By.xpath("//input[@name='endDate']"));
    private static final Locator CHECKED_PACKAGE = new Locator("CHECKED_PACKAGE", By.xpath("//label[contains(@class, 'dialog-add-product-bundle-package-item') and .//input[@checked]]"));
    private static final Locator DELETE_ACTION = new Locator("DELETE_ACTION", By.xpath("//li[contains(@class,'MuiMenuItem-root') and .//span[text()='Delete']]"));
    private static final Locator CONFIRM_DELETE_BUTTON = new Locator("CONFIRM_DELETE_BUTTON", By.xpath("//button[contains(., 'Yes, Delete Promo')]"));
    private static final Locator SIDEBAR_PROMO_LOYALTY = new Locator("SIDEBAR_PROMO_LOYALTY", By.xpath("//div[@title='Promo & Loyalty']"));
    private static final Locator SEARCH_BUTTON_V2 = new Locator("SEARCH_BUTTON_V2", By.xpath("//button[normalize-space(text())='Search']"));
    private static final Locator TIKET_SUBSIDY_INPUT = new Locator("TIKET_SUBSIDY_INPUT", By.xpath("//div[contains(@class, 'bundle-row_add-ons')]//div[contains(@class, 'mt-4')]//div[div[@class='input-name' and text()='tiket subsidy (%)']]//input"));
    private static final Locator PARTNER_SUBSIDY_INPUT = new Locator("PARTNER_SUBSIDY_INPUT", By.xpath("//div[contains(@class, 'bundle-row_add-ons')]//div[contains(@class, 'mt-4')]//div[div[@class='input-name' and text()='partner subsidy (%)']]//input"));
    private static final Locator SUBMIT_BUTTON = new Locator("SUBMIT_BUTTON", By.xpath("//button[@type='button' and contains(text(), 'Submit')]"));

    public void goToPage() {
        switch (Configs.ENVIRONMENT) {
            case GK -> driver.get("https://gatotkaca.tiket.com/admin/event-ms/to-do");
            case PREPROD -> driver.get("https://preprod.tiket.com/admin/event-ms/to-do");
            case PROD -> driver.get("https://www.tiket.com/admin/event-ms/to-do");
        }
        driverTools.waitForWebToLoad();
    }

    public static Locator getPackageByName(String packageName) {
        return new Locator("PACKAGE_BY_NAME_" + packageName.toUpperCase().replace(" ", "_"), By.xpath("//label[contains(@class, 'dialog-add-product-bundle-package-item') and .//p[text()='"+ packageName + "']]"));
    }

    public void clickPromoAndLoyaltySidebar() {
        driverTools.waitForElementToAppear(SIDEBAR_PROMO_LOYALTY, 10);
        driverTools.click(SIDEBAR_PROMO_LOYALTY);
    }

    public void goToBundlePage() {
        driverTools.waitForElementToAppear(BUNDLE_PAGE, 10);
        driverTools.click(BUNDLE_PAGE);
    }

    public void clickCreateNewBundle() {
        driverTools.waitForElementToAppear(CREATE_BUNDLE_BUTTON, 10);
        driverTools.click(CREATE_BUNDLE_BUTTON);
    }

    public void fillBundleGeneralDetails(String bundleName) {
        driverTools.waitForElementToAppear(RADIO_ADDONS, 10);
        driverTools.click(RADIO_ADDONS);
        driverTools.click(SUBMIT_BUNDLE_TYPE_BUTTON);

        driverTools.waitForElementToAppear(ADDONS_NAME_ID_INPUT, 10);
        driverTools.sendKeys(ADDONS_NAME_ID_INPUT, bundleName);
        driverTools.sendKeys(ADDONS_NAME_EN_INPUT, bundleName);

        driverTools.clickWithJavascriptExecutor(START_DATE);
        driverTools.clickWithJavascriptExecutor(DATE_PICKER_TODAY);
        driverTools.clickWithJavascriptExecutor(END_DATE);
        driverTools.clickWithJavascriptExecutor(DATE_PICKER_TOMORROW);
    }

    public void addMainProductConfiguration(String productName, String packageName) {
        driverTools.clickWithJavascriptExecutor(MAIN_PRODUCT_CONFIGURATION_ADD_BUTTON);
        driverTools.waitForElementToAppear(SEARCH_PRODUCT_INPUT, 10);
        driverTools.sendKeys(SEARCH_PRODUCT_INPUT, productName);
        driverTools.click(SEARCH_BUTTON_V2);
        driverTools.waitForElementToAppear(THINGS_TO_DO_LIST_FIRST_ITEM_BUTTON, 10);
        driverTools.click(THINGS_TO_DO_LIST_FIRST_ITEM_BUTTON);

        if (driverTools.isElementDisplayed(CHECKED_PACKAGE)) {
            driverTools.clickWithJavascriptExecutor(CHECKED_PACKAGE);
            driverTools.dummyWait(1);
        }

        Locator targetPackage = getPackageByName(packageName);
        driverTools.scrollToElement(targetPackage);
        driverTools.waitForElementToAppear(targetPackage, 10);
        driverTools.clickWithJavascriptExecutor(targetPackage);
        driverTools.click(ADD_THINGS_TO_DO_BUTTON);
        driverTools.waitForElementToAbsence(ADD_THINGS_TO_DO_BUTTON, 10);
    }

    public void addAddonsProductConfiguration(String productName, String packageName) {
        driverTools.clickWithJavascriptExecutor(ADDONS_PRODUCT_CONFIGURATION_ADD_BUTTON);
        driverTools.waitForElementToAppear(SEARCH_PRODUCT_INPUT, 10);
        driverTools.sendKeys(SEARCH_PRODUCT_INPUT, productName);
        driverTools.click(SEARCH_BUTTON_V2);
        driverTools.waitForElementToAppear(THINGS_TO_DO_LIST_FIRST_ITEM_BUTTON, 10);
        driverTools.click(THINGS_TO_DO_LIST_FIRST_ITEM_BUTTON);

        if (driverTools.isElementDisplayed(CHECKED_PACKAGE)) {
            driverTools.clickWithJavascriptExecutor(CHECKED_PACKAGE);
            driverTools.dummyWait(1);
        }

        Locator targetPackage = getPackageByName(packageName);
        driverTools.scrollToElement(targetPackage);
        driverTools.waitForElementToAppear(targetPackage, 10);
        driverTools.clickWithJavascriptExecutor(targetPackage);
        driverTools.click(ADD_THINGS_TO_DO_BUTTON);
        driverTools.waitForElementToAbsence(ADD_THINGS_TO_DO_BUTTON, 10);
    }

    public void submitBundle() {
        driverTools.clickWithJavascriptExecutor(SUBMIT_BUTTON);
        driverTools.dummyWait(5);
    }

    public boolean isBundleCreated(String productName, String packageNameMain) {
        Locator BUNDLE = new Locator("BUNDLE", By.xpath("//tr[td[contains(., '" + productName + "')] and td[contains(., '" + packageNameMain + "')]]"));
        driverTools.waitForElementToPresence(BUNDLE, 10);
        return driverTools.isElementPresent(BUNDLE);
    }

    public void deleteBundle(String identifier1, String identifier2) {
        Locator ACTIONS_BTN = new Locator("ACTIONS_BTN", By.xpath("//tr[td[contains(., '" + identifier1 + "')] and td[contains(., '" + identifier2 + "')]]//button[contains(@class, 'btn-action-table')]"));
        driverTools.waitForElementToAppear(ACTIONS_BTN, 10);
        driverTools.scrollToElement(ACTIONS_BTN);
        driverTools.clickWithJavascriptExecutor(ACTIONS_BTN);
        driverTools.waitForElementToAppear(DELETE_ACTION, 5);
        driverTools.clickWithJavascriptExecutor(DELETE_ACTION);
        driverTools.waitForElementToAppear(CONFIRM_DELETE_BUTTON, 5);
        driverTools.clickWithJavascriptExecutor(CONFIRM_DELETE_BUTTON);
    }

    public void inputTiketSubsidy(String value) {
        driverTools.waitForElementToAppear(TIKET_SUBSIDY_INPUT, 5);
        driverTools.clear(TIKET_SUBSIDY_INPUT);
        driverTools.sendKeys(TIKET_SUBSIDY_INPUT, value);
    }

    public void inputPartnerSubsidy(String value) {
        driverTools.waitForElementToAppear(PARTNER_SUBSIDY_INPUT, 5);
        driverTools.clear(PARTNER_SUBSIDY_INPUT);
        driverTools.sendKeys(PARTNER_SUBSIDY_INPUT, value);
    }
}
