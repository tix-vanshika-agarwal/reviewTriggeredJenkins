package com.tiket.web.dashboard.page.affiliate;

import com.tiket.model.Environment;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class AffiliatePartnerListPage<T extends WebDriver> extends BasePage<T> {

    public AffiliatePartnerListPage(Session<T> session) {
        super(session);
    }

    private static final Locator SEARCH_PARTNER_ID = new Locator("SEARCH_PARTNER_ID", By.id("partner-id"));
    private static final Locator SEARCH_PARTNER_NAME = new Locator("SEARCH_PARTNER_NAME", By.id("partner-name"));
    private static final Locator SEARCH_BUTTON = new Locator("SEARCH_BUTTON", By.xpath("//*[text()='Search']"));
    private static final Locator RESET_FILTER_BUTTON = new Locator("RESET_FILTER_BUTTON", By.xpath("//*[text()='Reset Filter']"));
    private static final Locator STATUS_ACTIVE = new Locator("STATUS_ACTIVE", By.xpath("(//div[text()='Active'])[1]"));
    private static final Locator STATUS_INACTIVE = new Locator("STATUS_INACTIVE", By.xpath("(//div[text()='Inactive'])[1]"));
    private static final Locator STATUS_TOGGLE = new Locator("STATUS_TOGGLE", By.xpath("(//div[contains(@class,'Toggle_toggle_small')])[1]"));
    private static final Locator STATUS_YES_BTN = new Locator("STATUS_YES_BTN", By.xpath("//button[text()='Yes']"));
    private static final Locator ADD_NEW_BTN = new Locator("ADD_NEW_BTN", By.xpath("//a[text()='Add New']"));
    private static final Locator SETTINGS_BTN = new Locator("SETTINGS_BTN", By.xpath("(//div[contains(@class,'DropdownFormDisplay_icon_container')])[1]"));
    private static final Locator MANAGE_PRODUCT_BTN = new Locator("MANAGE_PRODUCT_BTN", By.xpath("//span[text()='Manage Product']"));
    private static final Locator POP_UP_MANAGE_PRODUCT = new Locator("POP_UP_MANAGE_PRODUCT", By.xpath("//div[contains(@class,'Dialog_pop_up')]"));
    private static final Locator ADD_MORE_PRODUCT_BTN = new Locator("ADD_MORE_PRODUCT_BTN", By.xpath("//span[contains(@class,'text-lg')]"));
    private static final Locator PRODUCT_DROPDOWN = new Locator("PRODUCT_DROPDOWN", By.xpath("//button[contains(@class,'styles_clickable_icon')]"));
    private static final Locator PRODUCT_ITEM = new Locator("PRODUCT_ITEM", By.xpath("//ul[contains(@class, 'InputDropdown_dropdown')]/li[1]"));
    private static final Locator CREATE_PRODUCT_BTN = new Locator("CREATE_PRODUCT_BTN", By.xpath("//button[text()='Create']"));
    private static final Locator PRODUCT_NAME = new Locator("PRODUCT_NAME", By.xpath("(//div[contains(@class,'flex justify-between items-center')])[1]/p"));
    private static final Locator CONFIGURE_BTN = new Locator("CONFIGURE_BTN", By.xpath("//button[contains(text(),'Configure')]"));
    private static final Locator EDIT_BUTTON = new Locator("EDIT_BUTTON", By.xpath("(//td//div[text()='Edit' and contains(@class, 'cursor-pointer')])[1]"));


    public void goToPartnerListPage() {

        if (Configs.ENVIRONMENT == Environment.GK) {
            driver.get("https://gatotkaca.tiket.com/admin/affiliate/partner/list");
        } else if (Configs.ENVIRONMENT == Environment.PREPROD) {
            driver.get("https://preprod.tiket.com/admin/affiliate/partner/list");
        } else if (Configs.ENVIRONMENT == Environment.PROD) {
            driver.get("https://www.tiket.com/admin/affiliate/partner/list");
        }
    }

    public void clickAddNewPartnerButton() {
        driverTools.waitForElementToAppear(ADD_NEW_BTN, 5);
        driverTools.click(ADD_NEW_BTN);
    }

    public void searchPartnerById(String partnerId) {
        driverTools.waitForElementToAppear(SEARCH_PARTNER_ID, 5);
        driverTools.sendKeys(SEARCH_PARTNER_ID, partnerId);
        driverTools.click(SEARCH_BUTTON);
    }

    public void searchPartnerByName(String partnerName) {
        driverTools.waitForElementToBeClickable(SEARCH_PARTNER_NAME, 5);
        driverTools.sendKeys(SEARCH_PARTNER_NAME, partnerName);
        driverTools.click(SEARCH_BUTTON);
    }

    public Locator getSearchedItem(String searchedItem) {
        return new Locator("SEARCHED_ITEM", By.xpath("//*[text()='" + searchedItem + "']"));
    }

    public boolean isPartnerListItemPresent(String searchString) {
        driverTools.waitForElementToAppear(getSearchedItem(searchString), 5);
        return driverTools.isElementDisplayed(getSearchedItem(searchString));
    }

    public void clickFilterResetButton() {
        driverTools.waitForElementToAppear(RESET_FILTER_BUTTON, 5);
        driverTools.click(RESET_FILTER_BUTTON);
    }

    public boolean isSearchFieldEmpty() {
        String partnerName = driverTools.getText(SEARCH_PARTNER_NAME).orElseThrow();
        String partnerId = driverTools.getText(SEARCH_PARTNER_ID).orElseThrow();
        return partnerName.isEmpty() && partnerId.isEmpty();
    }

    public boolean isStatusActive() {
        return driverTools.isElementDisplayed(STATUS_ACTIVE);
    }

    public boolean isStatusInactive() {
        driverTools.waitForElementToAppear(STATUS_INACTIVE);
        return driverTools.isElementDisplayed(STATUS_INACTIVE);
    }

    public void toggleStatusActive() {
        if (isStatusInactive()) {
            driverTools.waitForElementToAppear(STATUS_TOGGLE, 5);
            driverTools.click(STATUS_TOGGLE);
            driverTools.waitForElementToAppear(STATUS_YES_BTN, 5);
            driverTools.click(STATUS_YES_BTN);
        }
    }

    public void toggleStatusInactive() {
        if (isStatusActive()) {
            driverTools.waitForElementToAppear(STATUS_TOGGLE, 5);
            driverTools.click(STATUS_TOGGLE);
            driverTools.waitForElementToAppear(STATUS_YES_BTN, 5);
            driverTools.click(STATUS_YES_BTN);
        }
    }
    
    public void openManageProductPopUp() {
        driverTools.waitForElementToAppear(SETTINGS_BTN, 5);
        driverTools.click(SETTINGS_BTN);
        driverTools.waitForElementToAppear(MANAGE_PRODUCT_BTN, 5);
        driverTools.click(MANAGE_PRODUCT_BTN);
    }

    public boolean isManageProductPopUpDisplayed() {
        driverTools.waitForElementToAppear(POP_UP_MANAGE_PRODUCT, 5);
        return driverTools.isElementDisplayed(POP_UP_MANAGE_PRODUCT);
    }

    public String clickAddMoreProductsButton() {
        driverTools.waitForElementToAppear(ADD_MORE_PRODUCT_BTN, 5);
        driverTools.click(ADD_MORE_PRODUCT_BTN);

        driverTools.waitForElementToAppear(PRODUCT_DROPDOWN, 5);
        driverTools.click(PRODUCT_DROPDOWN);

        driverTools.waitForElementToAppear(PRODUCT_ITEM, 5);
        String productName = driverTools.getText(PRODUCT_ITEM).orElseThrow();
        logger.log("Selected product: " + productName);
        driverTools.click(PRODUCT_ITEM);

        driverTools.waitForElementToAppear(CREATE_PRODUCT_BTN, 5);
        driverTools.click(CREATE_PRODUCT_BTN);

        return productName;
    }

    public boolean isOnProductAddPage(String productName) {
        Locator productNameLocator = new Locator("PRODUCT_NAME", By.xpath("//span[contains(text(),'" + productName + "')]"));
        driverTools.waitForElementToAppear(productNameLocator, 5);
        return driverTools.isElementDisplayed(productNameLocator);
    }

    public String clickConfigureButton() {
        driverTools.waitForElementToAppear(PRODUCT_NAME, 5);
        String productName = driverTools.getText(PRODUCT_NAME).orElseThrow();
        logger.log("Selected product: " + productName);
        driverTools.click(CONFIGURE_BTN);
        return productName;
    }

    public boolean isOnProductEditPage(String productName) {
        Locator productNameLocator = new Locator("PRODUCT_NAME", By.xpath("//span[contains(text(),'" + productName + "')]"));
        driverTools.waitForElementToAppear(productNameLocator, 5);
        return driverTools.isElementDisplayed(productNameLocator);
    }

    public void goToPartnerProductPwa() {
        try {
            String url = switch (Configs.ENVIRONMENT) {
                case GK -> "https://gatotkaca.tiket.com/admin/affiliate/product/pwa";
                case PREPROD -> "https://preprod.tiket.com/admin/affiliate/product/pwa";
                case PROD -> "https://www.tiket.com/admin/affiliate/product/pwa";
                default -> throw new IllegalStateException("Unexpected environment: " + Configs.ENVIRONMENT);
            };
            driverTools.get(url);
            logger.log("Navigated to Partner Product PWA Page.");
        } catch (Exception e) {
            logger.fail("Failed to navigate to Partner Product PWA Page.");
            throw e;
        }
    }

    public void editPWA(){
        driverTools.click(EDIT_BUTTON);
    }

    public boolean isOnEditProductPwaPage(){
        driverTools.waitForElementToAppear(PRODUCT_NAME,2);
        return driverTools.getCurrentURL().contains("/admin/affiliate/product/pwa/edit/");
    }
}