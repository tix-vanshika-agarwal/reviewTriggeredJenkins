package com.tiket.web.dashboard.page.affiliate;

import com.tiket.model.Environment;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;


public class AffiliateTTDOrderTablePage<T extends WebDriver> extends BasePage<T> {
    public AffiliateTTDOrderTablePage(Session<T> session) {
        super(session);
    }

    private static final Locator ORDER_TABLE = new Locator("ORDER_TABLE", By.xpath("//p[text()='Order Table']"));
    private static final Locator TTD_MENU = new Locator("TTD_MENU", By.xpath("//button[.//span[text()='Things To Do']]"));
    private static final Locator ORDER_ID_CHIPS = new Locator("ORDER_ID_CHIPS", By.xpath("//button[.//span[text()='Order ID']]"));
    private static final Locator AFF_REF_ID_CHIPS = new Locator("AFF_REF_ID_CHIPS", By.xpath("//button[normalize-space()='Affiliate and Reference ID']"));
    private static final Locator TTD_ORDER_ID_INPUT = new Locator("TTD_ORDER_ID_INPUT", By.xpath("//input[@id='order-id']"));
    private static final Locator TTD_PARTNER_ID_INPUT = new Locator("TTD_PARTNER_ID_INPUT", By.xpath("//input[@id='search-partner']"));
    private static final Locator TTD_PARTNER_ID_DROPDOWN = new Locator("TTD_PARTNER_ID_DROPDOWN", By.xpath("//ul[contains(@class, 'InputSearch_dropdown')]/li[1]/p/span"));
    private static final Locator AFF_REF_ID_INPUT = new Locator("AFF_REF_ID_INPUT", By.xpath("//input[@id='affiliate-ref-id']"));
    private static final Locator SEARCH_BTN = new Locator("SEARCH_BTN", By.xpath("//button[normalize-space()='Search']"));
    private static final Locator RESET_BTN = new Locator("RESET_BTN", By.xpath("//button[normalize-space()='Reset Filter']"));
    private Locator getTTDOrderIdLocator(String orderId) {
        String xpathTemplate = "//p[text()='Order ID']/following-sibling::div/p[normalize-space()='%s']";
        String finalXpath = String.format(xpathTemplate, orderId);
        return new Locator("TTD Order ID with value: " + orderId, By.xpath(finalXpath));
    }
    private Locator getTTDAffRefIDLocator(String affRefID) {
        String xpathTemplate = "//p[text()='Affiliate Ref ID']/following-sibling::div/p[normalize-space()='%s']";
        String finalXpath = String.format(xpathTemplate, affRefID);
        return new Locator("TTD Affiliate Reference ID with value: " + affRefID, By.xpath(finalXpath));
    }

    public void goToAffiliateLandingPageMenu() {

        if (Configs.ENVIRONMENT == Environment.GK) {
            driver.get("https://gatotkaca.tiket.com/admin/affiliate");
        } else if (Configs.ENVIRONMENT == Environment.PREPROD) {
            driver.get("https://preprod.tiket.com/admin/affiliate");
        } else if (Configs.ENVIRONMENT == Environment.PROD) {
            driver.get("https://www.tiket.com/admin/affiliate");
        }
    }

    public void goToTTDOrderTableMenu() {
        driverTools.waitForElementToAppear(ORDER_TABLE, 5);
        driverTools.click(ORDER_TABLE);
        driverTools.waitForElementToAppear(TTD_MENU, 5);
        driverTools.click(TTD_MENU);
    }

    public void searchTTDOrderByOrderId(String orderId) {
        driverTools.waitForElementToAppear(ORDER_ID_CHIPS, 1);
        driverTools.click(ORDER_ID_CHIPS);
        driverTools.waitForElementToAppear(TTD_ORDER_ID_INPUT, 1);
        driverTools.click(TTD_ORDER_ID_INPUT);
        driverTools.sendKeys(TTD_ORDER_ID_INPUT, orderId);
        driverTools.clear(TTD_ORDER_ID_INPUT);
    }

    public void searchTTDOrderByPartnerID(String partnerId) throws InterruptedException {
        driverTools.waitForElementToAppear(AFF_REF_ID_CHIPS, 10);
        driverTools.click(AFF_REF_ID_CHIPS);
        driverTools.waitForElementToAppear(TTD_PARTNER_ID_INPUT,1);
        driverTools.click(TTD_PARTNER_ID_INPUT);
        driverTools.sendKeys(TTD_PARTNER_ID_INPUT, partnerId);
        Thread.sleep(500);
        driverTools.click(TTD_PARTNER_ID_DROPDOWN);
    }

    public void searchTTDOrderByAffRefID(String affRefID) {
        driverTools.waitForElementToAppear(AFF_REF_ID_INPUT,1);
        driverTools.click(AFF_REF_ID_INPUT);
        driverTools.sendKeys(AFF_REF_ID_INPUT, affRefID);
    }

    public void clickTTDOrderSearchBTN() {
        driverTools.click(SEARCH_BTN);
    }

    public void clickTTDOrderResetBTN(){
        driverTools.waitForElementToBeClickable(RESET_BTN);
        driverTools.click(RESET_BTN);
    }

    public String getTTDOrderIdResult(String orderId) {
        Locator firstOrderData = getTTDOrderIdLocator(orderId);
        driverTools.waitForElementToAppear(firstOrderData, 4).orElseThrow();
        return driverTools.getText(firstOrderData).orElseThrow();
    }

    public String getTTDAffRefIDResult(String affRefID) {
        Locator firstOrderData = getTTDAffRefIDLocator(affRefID);
        driverTools.waitForElementToAppear(firstOrderData, 4).orElseThrow();
        return driverTools.getText(firstOrderData).orElseThrow();
    }

    public String affRefIDFieldIsEmpty(){
        return driverTools.getText(AFF_REF_ID_INPUT).orElseThrow();
    }
}
