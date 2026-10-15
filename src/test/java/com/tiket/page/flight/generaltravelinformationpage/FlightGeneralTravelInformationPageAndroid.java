package com.tiket.page.flight.generaltravelinformationpage;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebDriver;

public class FlightGeneralTravelInformationPageAndroid <T extends WebDriver> extends FlightGeneralTravelInformationPage<T> {
    public FlightGeneralTravelInformationPageAndroid(Session<T> session) {
        super(session);
    }
    public static final Locator BACK_BTN = new Locator("BACK_BTN", AppiumBy.className("android.widget.ImageButton"));

    public void clickBackBtn(){
        driverTools.click(BACK_BTN);
    }
}
