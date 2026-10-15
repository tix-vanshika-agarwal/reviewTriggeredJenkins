package com.tiket.web.dashboard.page.affiliate;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class AffiliatePWAPage<T extends WebDriver> extends BasePage<T> {

    public AffiliatePWAPage(Session<T> session) {
        super(session);
    }

    private static final Locator PARTNER_NAME_FILTER_FIELDS = new Locator("PARTNER_NAME_FILTER_FIELDS", By.xpath("//input[@id='partner-name']"));
    private static final Locator APPLY_FILTER = new Locator("APPLY_FILTER", By.xpath("//button[contains(@class,'Button_variant_secondary')]"));
    private static final Locator ADD_NEW_BUTTON = new Locator("ADD_NEW_BUTTON", By.xpath("//button[normalize-space(text())='Add New']"));
    private static final Locator SEARCH_BY_PARTNER_NAME_IDS_INPUT = new Locator("SEARCH_BY_PARTNER_NAME_IDS_INPUT", By.id("search-by-partners-nameids"));
    private static final Locator FIRST_PARTNER_OPTION = new Locator("FIRST_PARTNER_OPTION", By.xpath("(//ul[contains(@class,'InputSearch_dropdown')]/li)[1]"));
    private static final Locator CREATE_BUTTON = new Locator("CREATE_BUTTON", By.xpath("//button[normalize-space(text())='Create']"));
    private static final Locator TOGGLE_CHECKBOX = new Locator("TOGGLE_CHECKBOX", By.xpath("//div[contains(@class, 'Toggle_toggle')]"));
    private static final Locator PARTNER_LOGO_URL_INPUT = new Locator("PARTNER_LOGO_URL_INPUT", By.id("partner-logo-url"));
    private static final Locator HEADER_TEXT_INPUT = new Locator("HEADER_TEXT_INPUT", By.id("header-text"));
    private static final Locator NEW_PRODUCT_NAME_FIELD = new Locator("NEW_PRODUCT_NAME_FIELD", By.xpath("(//input[@id='product-name'])[last()]"));
    private static final Locator NEW_PRODUCT_ICON_URL_FIELD = new Locator("NEW_PRODUCT_ICON_URL_FIELD", By.xpath("(//input[@id='product-icon-url'])[last()]"));
    private static final Locator NEW_PRODUCT_URL_FIELD = new Locator("NEW_PRODUCT_URL_FIELD", By.xpath("(//input[@id='product-url'])[last()]"));
    private static final Locator URL_DESCRIPTION_FIELD = new Locator("URL_DESCRIPTION_FIELD", By.id("url-description"));
    private static final Locator URL_TEXT_FIELD = new Locator("URL_TEXT_FIELD", By.id("url-text"));
    private static final Locator URL_FIELD = new Locator("URL_FIELD", By.id("url"));
    private static final Locator SAVE_BUTTON = new Locator("SAVE_BUTTON", By.xpath("//button[contains(@class,'Button_variant_primary') and text()='Save']"));
    private static final Locator PWA_SUCCESS_POPUP = new Locator("PWA_SUCCESS_POPUP", By.xpath("//div[contains(@class,'bg-[#E7F2FF]') and .//span[contains(text(),'PWA successfully modified')]]"));

    public void goToPage() {
        switch (Configs.ENVIRONMENT) {
            case GK -> driver.get("https://gatotkaca.tiket.com/admin/affiliate/product/pwa");
            case PREPROD -> driver.get("https://preprod.tiket.com/admin/affiliate/product/pwa");
            case PROD -> driver.get("https://www.tiket.com/admin/affiliate/product/pwa");
        }
    }

    public void applyFilter(String partnerName, String status) throws InterruptedException {
        if (!partnerName.isEmpty()) {
            driverTools.clearInput(PARTNER_NAME_FILTER_FIELDS);
            driverTools.sendKeys(PARTNER_NAME_FILTER_FIELDS, partnerName);
        }

        if (!status.isEmpty()) {
            //TODO: Need to implement if needed
        }

        driverTools.click(APPLY_FILTER);
        Thread.sleep(500);
    }

    public void clickEditButton(String partnerName) {
        driverTools.click(getEditButtonLocatorByPartnerName(partnerName));
    }

    private Locator getEditButtonLocatorByPartnerName(String partnerName) {
        return new Locator("EDIT_BUTTON_" + partnerName, By.xpath("//tr[./td[2 and text()='"+partnerName+"']]/td[7]/div"));
    }

    public boolean checkProductOffered(String partnerName, String productName) {
        driverTools.waitForElementToAppear(getEditButtonLocatorByPartnerName(partnerName));

        return driverTools.getText(getProductOfferedLocatorByName(partnerName)).get().toLowerCase().contains(productName.toLowerCase());
    }

    private Locator getProductOfferedLocatorByName(String partnerName) {
        return new Locator("PRODUCT_OFFERED_" + partnerName, By.xpath("//tr[./td[2 and text()='"+partnerName+"']]/td[3]"));
    }

    public void selectPartnerName(String partnerName) {
        driverTools.waitForElementToAppear(ADD_NEW_BUTTON);
        driverTools.click(ADD_NEW_BUTTON);
        driverTools.waitForElementToAppear(SEARCH_BY_PARTNER_NAME_IDS_INPUT);
        driverTools.click(SEARCH_BY_PARTNER_NAME_IDS_INPUT);
        driverTools.sendKeys(SEARCH_BY_PARTNER_NAME_IDS_INPUT, partnerName);
        driverTools.waitForElementToAppear(FIRST_PARTNER_OPTION);
        driverTools.scrollToElement(FIRST_PARTNER_OPTION);
        driverTools.click(FIRST_PARTNER_OPTION);
        driverTools.click(CREATE_BUTTON);
    }

    public void createNewPWA(String partnerLogoURL, String headerText,
                             String productName, String productIconURL, String productURL,
                             String urlDescription, String urlText, String url) {
        driverTools.waitForElementToAppear(TOGGLE_CHECKBOX);
        driverTools.click(TOGGLE_CHECKBOX);
        fillContentSettings(partnerLogoURL, headerText);
        fillProductOffered(productName, productIconURL, productURL);
        fillTermsAndConditions(urlDescription, urlText, url);
        driverTools.scrollToElement(SAVE_BUTTON);
        driverTools.click(SAVE_BUTTON);
    }

    public void fillContentSettings(String partnerLogoURL, String headerText) {
        driverTools.waitForElementToAppear(PARTNER_LOGO_URL_INPUT);
        driverTools.sendKeys(PARTNER_LOGO_URL_INPUT, "https://" + partnerLogoURL);
        driverTools.sendKeys(HEADER_TEXT_INPUT, headerText);
    }

    public void fillProductOffered(String productName, String productIconURL, String productURL) {
        driverTools.waitForElementToAppear(NEW_PRODUCT_NAME_FIELD);
        driverTools.scrollToElement(NEW_PRODUCT_NAME_FIELD);
        driverTools.sendKeys(NEW_PRODUCT_NAME_FIELD, productName);
        driverTools.sendKeys(NEW_PRODUCT_ICON_URL_FIELD, "https://" + productIconURL);
        driverTools.sendKeys(NEW_PRODUCT_URL_FIELD, "https://" + productURL);
    }

    public void fillTermsAndConditions(String urlDescription, String urlText, String url) {
        driverTools.scrollToElement(URL_DESCRIPTION_FIELD);
        driverTools.sendKeys(URL_DESCRIPTION_FIELD, urlDescription);
        driverTools.sendKeys(URL_TEXT_FIELD, urlText);
        driverTools.sendKeys(URL_FIELD, "https://" + url);
    }

    public boolean isPwaSuccessPopupDisplayed() {
        driverTools.waitForElementToAppear(PWA_SUCCESS_POPUP);
        return driverTools.isElementDisplayed(PWA_SUCCESS_POPUP);
    }
}
