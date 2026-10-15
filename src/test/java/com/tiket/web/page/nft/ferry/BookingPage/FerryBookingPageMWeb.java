package com.tiket.web.page.nft.ferry.BookingPage;

import com.tiket.model.Locator;
import com.tiket.model.Platform;
import com.tiket.model.Session;
import com.tiket.testbase.Configs;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.MobileBy;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class FerryBookingPageMWeb <T extends WebDriver> extends FerryBookingPageWeb<T> {

    private static final Locator SAVE_PASSENGER_DETAIL_BUTTON = new Locator("SAVE_PASSENGER_DETAIL_BUTTON", new By.ByXPath("//button[contains(@class,'Button_button') and (text()='Save' or text()='Simpan')]"));
    private static final Locator ANDROID_ALLOW_THE_SITE = new Locator("ALLOW_THE_SITE", AppiumBy.accessibilityId("Allow while visiting the site"));
    private static final Locator IOS_ALLOW = new Locator("IOS_ALLOW", AppiumBy.accessibilityId("Allow"));

    public FerryBookingPageMWeb(Session<T> session) {
        super(session);
    }

    @Override
    public void clickPassengerContainer(Locator passengerContainerLocator, int passengerIndex) {
        WebElement passengerContainer = driverTools.waitForElementToAppear(passengerContainerLocator, 5).orElseThrow();
        driverTools.scrollIntoWebView(passengerContainer);
        driverTools.clickWithJavascriptExecutor(passengerContainer);
    }

    @Override
    public void clickSavePassengerDetail() {
        driverTools.clickWithJavascriptExecutor(SAVE_PASSENGER_DETAIL_BUTTON);
    }

    @Override
    public void clickAllowLocation() {
        try {
            if(Configs.PLATFORM == Platform.AMWEB) {
                driverTools.switchToNative();
                driverTools.waitForElementToAppear(ANDROID_ALLOW_THE_SITE, 5).ifPresent(driverTools::click);
                driver.findElement(AppiumBy.androidUIAutomator("new UiSelector().textContains(\"While using the app\")")).click();
                driverTools.waitForWebview();
                driverTools.switchToWebView();
            }
            else if (Configs.PLATFORM == Platform.IMWEB) {
                driverTools.switchToNative();
                driverTools.waitForElementToPresence(IOS_ALLOW, 5).ifPresent(driverTools::click);
                driverTools.switchToWebView();
            }
        } catch (Exception e) {
        }
    }

    @Override
    public Locator getPassengerSalutationInput(int index) {
        index = 1;
        return new Locator("PASSENGER_SALUTATION_" + index, new By.ByXPath("((//*[contains(@class, 'FormFieldSwitch_radio')])[" + index + "]//*[contains(@class, 'Radio_checkbox')])[1]"));
    }

    @Override
    public Locator getPassengerFullNameInput(int index) {
        return new Locator("PASSENGER_FULL_NAME_INPUT_" + index, new By.ByXPath("//input[@name='fullName" + index + "']"));
    }

    @Override
    public Locator getPassengerAgeInput(int index) {
        return new Locator( "PASSENGER_AGE_INPUT_" + index, new By.ByXPath("//input[@name='age" + index + "']"));
    }
    
    @Override
    public Locator getPassengerCityInput(int index) {
        index = 1;
        return new Locator("PASSENGER_CITY_INPUT_" + index, new By.ByXPath("(//*[@id='select-origin-city' or @id='pilih-kota-domisili'])[" + index + "]"));
    }

    @Override
    public Locator getPassengerIdNumberInput(int index) {
        return new Locator("ID_NUMBER_INPUT_" + index, new By.ByXPath("//input[@name='identityNumber" + index + "']"));
    }

    @Override
    public Locator getPassengerDateOfBirthInput(int index) {
        index = 1;
        return new Locator("PASSENGER_DOB_INPUT_" + index, new By.ByXPath("(//input[@id='date-input' and (@placeholder='Date of Birth' or @placeholder='Tanggal Lahir')])[" + index + "]"));
    }
}
