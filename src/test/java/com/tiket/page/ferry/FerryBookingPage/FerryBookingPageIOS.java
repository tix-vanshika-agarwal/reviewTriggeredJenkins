package com.tiket.page.ferry.FerryBookingPage;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.Optional;

public class FerryBookingPageIOS  <T extends WebDriver> extends FerryBookingPage<T>{
    public static final Locator ALLOW_ACCESS_BUTTON = new Locator("ALLOW_ACCESS_BUTTON", AppiumBy.xpath("//XCUIElementTypeButton[@label=\"Allow While Using App\"]\n"));
    public static final Locator ALLOW_LOCATION_BTN = new Locator("ALLOW_LOCATION_BTN", AppiumBy.xpath("//XCUIElementTypeButton[@label='Allow']"));

    public FerryBookingPageIOS(Session<T> session) {
        super(session);
    }

    @Override
    public void setLocationService(){
        driverTools.switchToNative();
        Optional<WebElement> acceptLocationPermission = driverTools.waitForElementToAppear(ALLOW_ACCESS_BUTTON);
        if(acceptLocationPermission.isPresent()){
            driverTools.click(acceptLocationPermission.orElseThrow());
            driverTools.click(ALLOW_LOCATION_BTN);
        } else
            driverTools.waitForElementToPresence(ALLOW_LOCATION_BTN).ifPresent(driverTools::click);
    }
}