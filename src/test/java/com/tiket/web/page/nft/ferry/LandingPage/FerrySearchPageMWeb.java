package com.tiket.web.page.nft.ferry.LandingPage;

import com.tiket.model.Locator;
import com.tiket.appium.Swipe;
import com.tiket.model.Session;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class FerrySearchPageMWeb <T extends WebDriver> extends FerrySearchPageWeb<T> {

    public FerrySearchPageMWeb(Session<T> session) {
        super(session);
    }

    private static final Locator SAVE_TIME = new Locator("SAVE_TIME", new By.ByXPath("(//button[contains(@class, 'Button_button') and contains(@class, 'Button_size_large')])[2]"));

    @Override
    public void selectCheckInTime(String checkInTime) {
        driverTools.clickWithJavascriptExecutor(SAVE_TIME);
    }
}
