package com.tiket.web.dashboard.page.home;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HomePage <T extends WebDriver> extends BasePage<T> {

    private static final Locator TIKET_LOGO = new Locator("TIKET_LOGO", new By.ByClassName("rr___DesktopLogo-module__logo___ywcCf"));
    public HomePage(Session<T> session) {
        super(session);
    }

    public void gotoHome() {
        driverTools.get("https://tiket.com");
    }

    public boolean isHomePageLoaded() {
        return driverTools.getTitle().contains("tiket") && driverTools.isElementDisplayed(TIKET_LOGO);
    }

    public void clickOnTiketLogo() {
        driverTools.click(TIKET_LOGO);
    }
}
