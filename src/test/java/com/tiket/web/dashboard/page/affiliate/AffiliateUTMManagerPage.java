package com.tiket.web.dashboard.page.affiliate;

import com.tiket.model.Environment;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class AffiliateUTMManagerPage<T extends WebDriver> extends BasePage<T> {

    public AffiliateUTMManagerPage(Session<T> session) {
        super(session);
    }

    private static final Locator AFFILIATE_BOX = new Locator("AFFILIATE_BOX", By.id("affiliate-name-or-id"));
    private static final Locator PRODUCT_TYPE = new Locator("PRODUCT_TYPE", By.id("product-type"));
    private static final Locator SEARCH_BTN = new Locator("SEARCH_BTN", By.xpath("//button[normalize-space()='Search']"));
    private static final Locator FIRST_DATA_ROW = new Locator("FIRST_DATA_ROW", By.xpath("//tbody/tr[1]"));
    private static final Locator URL_NAME_INPUT = new Locator("URL_NAME_INPUT", By.xpath("//input[@id='url-name']"));
    private static final Locator RESET_FILTER_BUTTON = new Locator("RESET_FILTER_BUTTON", By.xpath("//button[normalize-space()='Reset Filter']"));

    public void gotoUTMManagerPage() {
        if (Configs.ENVIRONMENT == Environment.GK) {
            driver.get("https://gatotkaca.tiket.com/admin/affiliate/partner/utm-manager");
        } else if (Configs.ENVIRONMENT == Environment.PREPROD) {
            driver.get("https://preprod.tiket.com/admin/affiliate/partner/utm-manager");
        } else if (Configs.ENVIRONMENT == Environment.PROD) {
            driver.get("https://www.tiket.com/admin/affiliate/partner/utm-manager");
        }
    }

    public void selectAffiliateById(String affiliateName) {
        driverTools.waitForElementToAppear(AFFILIATE_BOX);
        driverTools.click(AFFILIATE_BOX);
        driverTools.sendKeys(AFFILIATE_BOX, affiliateName);
        Locator DYNAMIC_AFFILIATE_ID = new Locator("DYNAMIC_AFFILIATE_ID", By.xpath("//li[contains(.,'" + affiliateName + "')][1]"));
        driverTools.waitForElementToAppear(DYNAMIC_AFFILIATE_ID);
        driverTools.click(DYNAMIC_AFFILIATE_ID);
    }

    public void selectProductType(String productType) {
        driverTools.waitForElementToAppear(PRODUCT_TYPE);
        driverTools.click(PRODUCT_TYPE);
        Locator DYNAMIC_PRODUCT_TYPE = new Locator("DYNAMIC_PRODUCT_TYPE", By.xpath("//li[normalize-space()='" + productType + "']"));
        driverTools.waitForElementToAppear(DYNAMIC_PRODUCT_TYPE, 10);
        driverTools.waitForElementToBeClickable(DYNAMIC_PRODUCT_TYPE, 10);
        driverTools.click(DYNAMIC_PRODUCT_TYPE);
    }

    public void clickSearchButton() {
        driverTools.waitForElementToAppear(SEARCH_BTN);
        driverTools.click(SEARCH_BTN);
    }

    public boolean isPartnerVisible(String partnerId) {
        driverTools.waitForElementToAppear(FIRST_DATA_ROW, 4).orElseThrow();
        String searchData = driverTools.getText(FIRST_DATA_ROW).orElseThrow();
        return searchData.contains(partnerId);
    }

    public String getFirstRowProductType() {
        driverTools.waitForElementToAppear(FIRST_DATA_ROW, 4).orElseThrow();
        return driverTools.getText(FIRST_DATA_ROW).orElseThrow();
    }

    public void enterURLName(String urlName) {
        driverTools.waitForElementToAppear(URL_NAME_INPUT);
        driverTools.sendKeys(URL_NAME_INPUT, urlName);
    }

    public boolean isURLNameVisible(String urlName) {
        driverTools.waitForElementToAppear(URL_NAME_INPUT, 4).orElseThrow();
        String searchData = driverTools.getText(FIRST_DATA_ROW).orElseThrow();
        return searchData.contains(urlName);
    }

    public void clickResetFilterButton() {
        driverTools.waitForElementToAppear(RESET_FILTER_BUTTON);
        driverTools.click(RESET_FILTER_BUTTON);
    }

    public boolean areAllFilterFieldsEmpty() {
        boolean isUrlNameEmpty = driverTools.getAttribute(URL_NAME_INPUT.by(), "value").trim().isEmpty();
        boolean isAffiliateFieldEmpty = driverTools.getText(AFFILIATE_BOX).orElse("").trim().isEmpty();
        boolean isProductTypeEmpty = driverTools.getText(PRODUCT_TYPE).orElse("").trim().isEmpty();

        return isUrlNameEmpty && isAffiliateFieldEmpty && isProductTypeEmpty;
    }
}
