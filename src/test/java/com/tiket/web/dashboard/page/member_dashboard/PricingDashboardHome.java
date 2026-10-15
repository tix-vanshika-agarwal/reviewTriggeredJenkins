package com.tiket.web.dashboard.page.member_dashboard;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class PricingDashboardHome<T extends WebDriver> extends BasePage<T> {

    public PricingDashboardHome(Session<T> session) { super(session); }

    private static final Locator PRICING_DASHBOARD_LOGO = new Locator("PRICING_DASHBOARD_LOGO", new By.ByXPath("//span[contains(text(),'Pricing Dashboard')]"));

    public boolean isPricingDashboardLoaded() {
        driverTools.waitForElementToAppear(PRICING_DASHBOARD_LOGO,3);
        return driverTools.getTitle().contains("Pricing") && driverTools.isElementDisplayed(PRICING_DASHBOARD_LOGO);
    }
}