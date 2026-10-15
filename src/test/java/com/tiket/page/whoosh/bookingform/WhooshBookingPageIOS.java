package com.tiket.page.whoosh.bookingform;

import com.tiket.appium.Swipe;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.page.whoosh.bookingform.WhooshBookingPage;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class WhooshBookingPageIOS <T extends WebDriver> extends WhooshBookingPage<T> {
    public static final Locator CONTINUE_TO_PAY = new Locator("CONTINUE_TO_PAY",AppiumBy.xpath("//*[@name=\"Continue to payment\" and @type=\"XCUIElementTypeButton\"]"));
    public static final Locator SELECT_TRAVEL_PLAN = new Locator("SELECT_TRAVEL_PLAN",AppiumBy.xpath("//*[@type=\"XCUIElementTypeOther\" and ./*[@name=\"travelPlan\"]]"));

    public WhooshBookingPageIOS(Session<T> session) {
        super(session);
    }

    @Override
    public void selectTravelPlan(String planName){
        driverTools.waitForElementToPresence(SELECT_TRAVEL_PLAN);
        driverTools.click(SELECT_TRAVEL_PLAN);
        if(!driverTools.waitForElementToPresence(locatorForPLanViaName(planName)).isPresent()){
            driverTools.scrollToElement(locatorForPLanViaName(planName));
        }
        driverTools.click(locatorForPLanViaName(planName));
        driverTools.waitForElementToPresence(CONTINUE_TO_PAY);
        driverTools.click(CONTINUE_TO_PAY);
    }

    private Locator locatorForPLanViaName(String planName){
        return new Locator("PLAN_NAME_SELECT",AppiumBy.xpath("//*[@name=\""+planName+"\"]"));
    }
}
