package com.tiket.web.page.nft.bus.search_Page;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class BusSearchPageMWeb <T extends WebDriver> extends BusSearchPageWeb<T> {

    public BusSearchPageMWeb(Session<T> session) {
        super(session);
    }

    public Locator getPassengerTextLocator(int passenger) {
        return new Locator("PASSENGER_PICKER_WHEEL_TEXT",By.xpath("//div[contains(@class,'WheelPicker_slider_item') and normalize-space(text())='" + passenger + "']"));
        }

    public void setPassenger(int passenger) {
        Locator PASSENGER_PICKER_WHEEL_TEXT = getPassengerTextLocator(passenger);
        WebElement passengerText = driverTools.waitForElementToAppear(PASSENGER_PICKER_WHEEL_TEXT).orElseThrow();
        driverTools.scrollIntoWebView(passengerText);
        driverTools.clickWithJavascriptExecutor(PASSENGER_PICKER_WHEEL_TEXT);
        driverTools.waitForElementToAppear(SAVE_DATE_BUTTON).ifPresent(driverTools::click);
    }
}
