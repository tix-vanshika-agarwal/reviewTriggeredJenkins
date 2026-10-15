package com.tiket.web.page.nft.ferry.LandingPage;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class FerrySearchPageDWeb <T extends WebDriver> extends FerrySearchPageWeb<T> {

    public FerrySearchPageDWeb(Session<T> session) {
        super(session);
    }

    @Override
    public void selectCheckInTime(String checkInTime) {
        Locator CHECK_IN_TIME = getCheckInTimeLocator(checkInTime);
        WebElement element = driverTools.waitForElementToAppear(CHECK_IN_TIME).orElseThrow();
        driverTools.scrollIntoWebView(element);
        driverTools.clickWithJavascriptExecutor(CHECK_IN_TIME);
    }

    private Locator getCheckInTimeLocator(String checkInTime) {
        return new Locator("CHECK_IN_TIME", new By.ByXPath("//*[contains(@class, 'TimePickerList') and text()='" + checkInTime + "']"));
    }
}
