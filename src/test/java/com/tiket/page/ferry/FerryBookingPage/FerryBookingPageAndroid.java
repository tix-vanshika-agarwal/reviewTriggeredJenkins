package com.tiket.page.ferry.FerryBookingPage;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.Optional;


public class FerryBookingPageAndroid  <T extends WebDriver> extends FerryBookingPage<T>{
    private static final Locator ACCEPT_LOCATION_PERMISSION = new Locator("ACCEPT_LOCATION_PERMISSION", AppiumBy.xpath("//*[contains(@resource-id,'tds_main_info_container_view')]//*[contains(@resource-id,'tds_btn_primary_info_view')]"));
    private static final Locator ALLOW_LOCATION_ONE_TIME = new Locator("ALLOW_LOCATION_ONE_TIME", AppiumBy.xpath("//*[@resource-id='com.android.permissioncontroller:id/permission_allow_one_time_button']"));
    private static final Locator ALLOW_LOCATION_NATIVE_IF_LOCATION_OFF = new Locator("ALLOW_LOCATION_NATIVE_IF_LOCATION_OFF", AppiumBy.id("//*[contains(@resource-id,'button1') and contains(@text,'Turn on')]"));
    public FerryBookingPageAndroid(Session<T> session) {
        super(session);
    }

    @Override
    public void setLocationService(){
        driverTools.switchToNative();
        Optional<WebElement> acceptLocationPermission = driverTools.waitForElementToAppear(ACCEPT_LOCATION_PERMISSION,3);
        if(acceptLocationPermission.isPresent()){
            driverTools.click(acceptLocationPermission.orElseThrow());
            driverTools.click(ALLOW_LOCATION_ONE_TIME);
        }else {
            driverTools.waitForElementToPresence(ALLOW_LOCATION_NATIVE_IF_LOCATION_OFF).ifPresent(driverTools::click);
        }
    }
}