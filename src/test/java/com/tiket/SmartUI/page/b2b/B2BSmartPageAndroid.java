package com.tiket.SmartUI.page.b2b;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebDriver;

public class B2BSmartPageAndroid<T extends WebDriver> extends B2BSmartPage<T> {
    public static final Locator TRAVEL_PLAN_CHEVRON_ICON = new Locator("TRAVEL_PLAN_CHEVRON_ICON", AppiumBy.id("text_input_end_icon"));
    public static final Locator TRAVEL_PLAN_RADIO_BUTTON = new Locator("TRAVEL_PLAN_RADIO_BUTTON", AppiumBy.id("tds_selection_control_list"));
    public static final Locator CONTINUE_TO_PAYMENT_BUTTON = new Locator("CONTINUE_TO_PAYMENT_BUTTON", AppiumBy.id("btn_continue_to_payment"));

    public B2BSmartPageAndroid(Session<T> session) {
        super(session);
    }

    @Override
    public void openTravelPlan() {
        driverTools.click(TRAVEL_PLAN_CHEVRON_ICON);
    }

    @Override
    public void chooseTravelPlan() {
        driverTools.click(TRAVEL_PLAN_RADIO_BUTTON);
    }

    @Override
    public void waitForTravelPlanToBeOpened() {
        driverTools.waitForElementToAppear(TRAVEL_PLAN_RADIO_BUTTON);
    }

    @Override
    public void continueToNextPage() {
        driverTools.click(CONTINUE_TO_PAYMENT_BUTTON);
    }

}