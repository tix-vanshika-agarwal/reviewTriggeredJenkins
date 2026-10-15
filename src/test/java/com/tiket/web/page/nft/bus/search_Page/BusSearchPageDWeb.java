package com.tiket.web.page.nft.bus.search_Page;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class BusSearchPageDWeb <T extends WebDriver> extends BusSearchPageWeb<T> {

    public BusSearchPageDWeb(Session<T> session) {
        super(session);
    }

    private static final Locator PASSENGER_COUNT = new Locator("PASSENGER_COUNT", new By.ByXPath("//*[contains(@class, 'QuantityEditor_quantity_input')]"));
    private static final Locator PASSENGER_INCREMENT = new Locator("PASSENGER_INCREMENT", new By.ByXPath("//*[contains(@class, 'QuantityEditor_plus_icon')]/ancestor::button"));
    private static final Locator PASSENGER_DECREMENT = new Locator("PASSENGER_DECREMENT", new By.ByXPath("//*[contains(@class, 'QuantityEditor_minus_icon')]/ancestor::button"));

    public void setPassenger(int passengerCount) {
        int currentCount = Integer.parseInt(driverTools.getValueAttribute(PASSENGER_COUNT, "value"));
        if (currentCount < passengerCount) {
            clickPassengerCountIncrementLocator(passengerCount - currentCount);
        } else if (currentCount > passengerCount) {
            clickPassengerCountDecrementLocator(currentCount - passengerCount);
        }
        driverTools.clickWithJavascriptExecutor(PASSENGER_SAVE_BUTTON);
    }

    private void clickPassengerCountIncrementLocator(int i) {
        for (int j = 0; j < i; j++) {
            driverTools.clickWithJavascriptExecutor(PASSENGER_INCREMENT);
        }
    }

    private void clickPassengerCountDecrementLocator(int i) {
        for (int j = 0; j < i; j++) {
            driverTools.clickWithJavascriptExecutor(PASSENGER_DECREMENT);
        }
    }
}
