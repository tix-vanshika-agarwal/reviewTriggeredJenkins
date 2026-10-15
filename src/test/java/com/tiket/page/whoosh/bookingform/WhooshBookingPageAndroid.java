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

public class WhooshBookingPageAndroid<T extends WebDriver> extends WhooshBookingPage<T> {
    public WhooshBookingPageAndroid(Session<T> session) {
        super(session);
    }
    public void selectTravelPlan(String planName){
        //fixme: not implemented yet
    }
}
