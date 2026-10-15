package com.tiket.web.dashboard.page.member_dashboard;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class MemberPage <T extends WebDriver> extends BasePage<T> {

    public MemberPage(Session<T> session) {
        super(session);
    }

    private static final String PRICING_START_PAGE_URL = "https://gatotkaca.tiket.com/admin/pricing-dashboard/price-anomaly-detection/vertical/default";
    private static final String PRICING_PREPROD_START_PAGE_URL = "https://preprod.tiket.com/admin/pricing-dashboard/price-anomaly-detection/vertical/default";
    private static final String PRICING_PROD_START_PAGE_URL = "https://prod.tiket.com/admin/pricing-dashboard/price-anomaly-detection/vertical/default";
    private static final String INTOOLS_PAGE_URL = "https://gatotkaca.tiket.com/admin/intools/commission-dashboard/ground-transport?page=1&limit=10&vertical=ground-transport";
    private static final Locator MEMBER_DASHBOARD_LOGO = new Locator("MEMBER_DASHBOARD_LOGO", new By.ByXPath("//div[contains(@class,'styles_logo_area__')]"));
    private static final Locator DASHBOARD_NAME = new Locator("DASHBOARD_NAME", new By.ByXPath("//div[contains(text(),'Pricing Dashboard')]"));
    private static final Locator PRICING_DASHBOARD_LOGO = new Locator("PRICING_DASHBOARD_LOGO", new By.ByXPath("//span[contains(text(),'Pricing Dashboard')]"));
    private static final Locator FINANCE_DASHBOARD_LOGO = new Locator("PRICING_DASHBOARD_LOGO", new By.ByXPath("//span[contains(text(),'Finance Dashboard')]"));

    public boolean isMemberDashboardLoaded() {
        driverTools.waitForElementToAppear(MEMBER_DASHBOARD_LOGO,30);
        return driverTools.getTitle().contains("Member") && driverTools.isElementDisplayed(MEMBER_DASHBOARD_LOGO);
    }

    public void goToPricingDashboard(String username, String password) {
        String logoElement;
        switch (Configs.ENVIRONMENT) {
            case GK -> logoElement = PRICING_START_PAGE_URL;
            case PREPROD -> logoElement = PRICING_PREPROD_START_PAGE_URL;
            case PROD -> logoElement = PRICING_PROD_START_PAGE_URL;
            default -> throw new NotImplementedException(Configs.ENVIRONMENT + " is not implemented for pricing page login.");
        }
        driverTools.get(logoElement);
        if (!isPricingPageLoggedIn()) {
            MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
            memberLoginPage.inputLogin(username, password);
            memberLoginPage.clickOnButton();
        }
    }

    public boolean isPricingPageLoggedIn() {
        driverTools.waitForElementToAppear(PRICING_DASHBOARD_LOGO, 3);
        return (driverTools.isElementDisplayed(PRICING_DASHBOARD_LOGO));
    }


    public void goToFinanceDashboard(String username, String password) {
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        memberLoginPage.adminLoginViaCookie(username, password);
        driverTools.get(INTOOLS_PAGE_URL);
    }

    public boolean isFinancePageLoggedIn() {
        driverTools.waitForElementToAppear(FINANCE_DASHBOARD_LOGO, 3);
        return (driverTools.isElementDisplayed(FINANCE_DASHBOARD_LOGO));
    }
}