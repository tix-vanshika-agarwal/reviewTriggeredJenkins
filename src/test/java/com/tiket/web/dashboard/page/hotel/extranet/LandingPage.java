package com.tiket.web.dashboard.page.hotel.extranet;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LandingPage <T extends WebDriver> extends BasePage<T> {
    public LandingPage(Session<T> session) {
        super(session);
    }

    private static final Locator LANDING_PAGE = new Locator("LANDING_PAGE", new By.ByXPath("//div[contains(@class,'app landing-page')]"));

    public boolean isExtranetLandingPageLoaded() {
        return driverTools.waitForElementToAppear(LANDING_PAGE, 3).isPresent();
    }
}