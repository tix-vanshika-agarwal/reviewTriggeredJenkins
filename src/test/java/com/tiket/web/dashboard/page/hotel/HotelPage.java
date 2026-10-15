package com.tiket.web.dashboard.page.hotel;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HotelPage <T extends WebDriver> extends BasePage<T> {

    private static final Locator TIKET_LOGO = new Locator("TIKET_LOGO", new By.ByClassName("rr___DesktopLogo-module__logo___ywcCf"));
    public HotelPage(Session<T> session) {
        super(session);
    }

    public void gotoHotelLandingPage() {
        driverTools.get("https://tiket.com/hotel");
    }

    public boolean isHotelPageLoaded() {
        return driverTools.getTitle().contains("Hotel") && driverTools.isElementDisplayed(TIKET_LOGO);
    }

    public void clickOnTiketLogo() {
        driverTools.click(TIKET_LOGO);
    }
}
