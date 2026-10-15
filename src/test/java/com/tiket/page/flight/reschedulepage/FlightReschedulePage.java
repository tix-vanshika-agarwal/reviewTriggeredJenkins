package com.tiket.page.flight.reschedulepage;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import io.appium.java_client.AppiumBy;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public abstract class FlightReschedulePage <T extends WebDriver> extends BasePage<T> {
    public FlightReschedulePage(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> FlightReschedulePage<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case IOS -> {
                return new FlightReschedulePageIOS<>(session);
            }
            case ANDROID -> {
                return new FlightReschedulePageAndroid<>(session);
            }
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    private static final Locator PASSENGER_CHECKBOX = new Locator("PASSENGER_CHECKBOX", AppiumBy.xpath("//*[contains(@class,'Check_custom_checkbox')]"));
    private static final Locator FIND_RESCHEDULE_BTN = new Locator("FIND_RESCHEDULE_BTN", AppiumBy.xpath("//button[contains(@class,'Footer_footer_button')]"));

    public void clickPassengerCheckbox() {
        driverTools.waitForElementToAppear(PASSENGER_CHECKBOX);
        driverTools.switchToWebViewUrl("reschedule");
        List<WebElement> passengersCheckBox = driverTools.elements(PASSENGER_CHECKBOX).orElseThrow();
        logger.log("Passenger checkbox size: " + passengersCheckBox.size());
        for(WebElement passengerCheckBox : passengersCheckBox) {
            logger.log("Clicking passenger checkbox");
            driverTools.clickWithJavascriptExecutor(passengerCheckBox);
        }
        driverTools.switchToNative();
    }

    public void clickFindRescheduleButton() {
        driverTools.switchToWebViewUrl("reschedule");
        driverTools.click(FIND_RESCHEDULE_BTN);
        driverTools.switchToNative();
    }
}
