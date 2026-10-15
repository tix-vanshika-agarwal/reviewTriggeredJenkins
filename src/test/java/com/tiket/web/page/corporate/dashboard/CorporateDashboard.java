package com.tiket.web.page.corporate.dashboard;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Deeplink;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import com.tiket.web.page.common.home.HomePageWeb;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CorporateDashboard <T extends WebDriver> extends BasePage<T> {
    public CorporateDashboard(Session<T> session) {
        super(session);
    }

    private static final Locator CORPORATE_DASHBOARD_TITLE = new Locator("CORPORATE_DASHBOARD_TITLE", By.xpath("//span[normalize-space()='Welcome to Corporate Dashboard']"));
    private static final Locator PURCHASE_LIST = new Locator("PURCHASE_LIST", new By.ByXPath("//span[text()='Purchase List']"));
    private static final Locator GO_TO_TIKET_HOME_PAGE = new Locator("GO_TO_TIKET_HOME_PAGE", new By.ByXPath("//button[contains(@class,'Button_variant_secondary') and contains(text(),'Go')]"));
    private static final Locator CORPORATE_BILLING_BALANCE = new Locator("CORPORATE_BILLING_BALANCE", By.xpath(
            "//*[contains(normalize-space(.),'IDR') and (normalize-space(.)='IDR 0' or normalize-space(.)='IDR0')]"));
    private static final Deeplink PURCHASE_LIST_DEEPLINK = new Deeplink(
            "CORPORATE_PURCHASE_LIST",
            "https://corporate-gatotkaca.tiket.com/purchase-list",
            "https://corporate.preprod.tiket.com/purchase-list",
            "https://corporate.tiket.com/purchase-list");

    public boolean isOnCorporateDashboard(){
        driverTools.waitForElementsToAppear(CORPORATE_DASHBOARD_TITLE);
        return driverTools.isElementDisplayed(CORPORATE_DASHBOARD_TITLE) && driverTools.getCurrentURL().contains("corporate");
    }

    public void openPurchaseListViaDeeplink() {
        navigateToPurchaseListViaDeeplink();
        driverTools.waitForElementToAppear(PURCHASE_LIST, 15).orElseThrow();
        logger.log("Opened corporate purchase list via deeplink: " + driverTools.getCurrentURL());
    }

    public void navigateToPurchaseListViaDeeplink() {
        switch (Configs.ENVIRONMENT) {
            case PROD -> driverTools.get(PURCHASE_LIST_DEEPLINK.prodDeeplink());
            case PREPROD -> driverTools.get(PURCHASE_LIST_DEEPLINK.preprodDeeplink());
            case GK -> driverTools.get(PURCHASE_LIST_DEEPLINK.gkDeeplink());
            default -> driverTools.get(CoreFunctions.getCorporateDashboardBaseUrl() + "/purchase-list");
        }
    }

    public boolean isCorporatePurchaseListAccessible() {
        return driverTools.getCurrentURL().contains("corporate")
                && driverTools.waitForElementToAppear(PURCHASE_LIST, 15).isPresent();
    }

    /**
     * Inactive or removed employees cannot open the corporate purchase list and remain on consumer home.
     */
    public boolean isEmployeeInactiveOrRemoved(HomePageWeb<?> homePage) {
        navigateToPurchaseListViaDeeplink();
        if (isCorporatePurchaseListAccessible()) {
            logger.log("Corporate purchase list is accessible; employee is active");
            return false;
        }
        homePage.waitForHomePageToOpen();
        boolean onConsumerHome = homePage.isHomePageOpened() && !driverTools.getCurrentURL().contains("corporate");
        logger.log("Employee inactive or removed check -> onConsumerHome=" + onConsumerHome
                + ", currentUrl=" + driverTools.getCurrentURL());
        return onConsumerHome;
    }

    public String getCorporateBillingBalanceText() {
        driverTools.waitForElementToAppear(CORPORATE_BILLING_BALANCE, 15).orElseThrow();
        String balanceText = driverTools.getText(CORPORATE_BILLING_BALANCE).orElseThrow().trim();
        logger.log("Corporate billing balance text on purchase list: " + balanceText);
        return balanceText;
    }

    public boolean isCorporateBillingBalanceEmpty() {
        return getCorporateBillingBalanceText().replaceAll("[\\s,]", "").equalsIgnoreCase("IDR0");
    }

    public void goBackToTiketHomePage(){
        driverTools.waitForElementToPresence(GO_TO_TIKET_HOME_PAGE);
        driverTools.clickWithJavascriptExecutor(GO_TO_TIKET_HOME_PAGE);
        driverTools.tabSwitch("tiket.com");
    }
}
