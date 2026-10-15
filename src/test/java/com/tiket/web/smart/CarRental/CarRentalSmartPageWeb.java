package com.tiket.web.smart.CarRental;

import com.tiket.appium.Swipe;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public abstract class CarRentalSmartPageWeb<T extends WebDriver> extends WebPage<T> {
    private static final Locator LOCATION_INPUT_TEXT = new Locator("LOCATION_INPUT_TEXT", new By.ByXPath("//*[contains(@class, 'LocationInput_input')]"));
    private static final Locator BOTTOMSHEET_CLOSE = new Locator("BOTTOMSHEET_CLOSE", new By.ByXPath("//*[contains(@class, 'BottomSheet_close_button')]"));
    private static final Locator LOCATION_INPUT_BOX = new Locator("LOCATION_INPUT_BOX", new By.ByXPath("//*[contains(@class, 'SearchBox_placeholder')]//input"));
    private static final Locator FILTER_MENU = new Locator("FILTER_MENU", new By.ByXPath("//*[contains(@class,'Chip_chip')][1]"));
    private static final Locator MANUAL_TRANSMISSION = new Locator("MANUAL_TRANSMISSION", new By.ByXPath("//button[.//span[contains(normalize-space(.),'Manual')]]"));
    private static final Locator AUTOMATIC_TRANSMISSION = new Locator("AUTOMATIC_TRANSMISSION", new By.ByXPath("//button[.//span[contains(normalize-space(.),'Automatic') or contains(normalize-space(.),'Otomatis')]]"));
    private static final Locator BOTTOMSHEET_SAVE_BUTTON = new Locator("BOTTOMSHEET_SAVE_BUTTON", new By.ByXPath("//button[contains(normalize-space(.),'Simpan') or contains(normalize-space(.),'Save')]"));
    private static final Locator FIRST_CAR_CARD = new Locator("FIRST_CAR_CARD", new By.ByXPath("(//*[contains(@class, 'FleetCard_container')])[1]"));
    private static final Locator FIRST_CAR_VENDOR_CARD = new Locator("FIRST_CAR_VENDOR_CARD", new By.ByXPath("(//*[contains(@class, 'VendorCard_content')])[1]"));
    private static final Locator WAIT_FOR_BOOKING_FORM = new Locator("WAIT_FOR_BOOKING_FORM", new By.ByXPath("(//*[contains(@class, 'TotalPrice')])[1]"));
    private static final Locator ORDER_ID = new Locator("ORDER_ID", new By.ByXPath("//p[contains(normalize-space(.),'Order ID')]"));
    private static final Locator PAYMENT_BACK_BUTTON = new Locator("PAYMENT_BACK_BUTTON", new By.ByXPath("//*[contains(@id,'back')]"));
    private static final Locator CR_BACK_BUTTON = new Locator("CR_BACK_BUTTON", new By.ByXPath("//*[contains(@class, 'back_button')]"));
    private static final Locator CONFIRM_BACK_BUTTON = new Locator("CONFIRM_BACK_BUTTON", new By.ByXPath("//*[contains(@class, 'Figure_secondary')]"));
    private static final Locator LANDING_PAGE_BACK_BUTTON = new Locator("LANDING_PAGE_BACK_BUTTON", new By.ByXPath("//*[contains(@class, 'HeroImage_button')]"));

    public CarRentalSmartPageWeb(Session<T> session) {
        super(session);
    }
    public static <T extends WebDriver> CarRentalSmartPageWeb<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new CarRentalSmartPageDWeb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new CarRentalSmartPageMWeb<>(session);
            }
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    public void chooseLocation(String location) {
        driverTools.clickWithJavascriptExecutor(LOCATION_INPUT_TEXT);
        driverTools.waitForElementToAppear(LOCATION_INPUT_BOX);
        driverTools.sendKeys(LOCATION_INPUT_BOX, location);
    }
    public void closeBottomsheet(){
        driverTools.click(BOTTOMSHEET_CLOSE);
    }

    public void applyTransmissionFilter(String transmission){
        driverTools.waitForElementToPresence(FILTER_MENU);
        driverTools.clickWithJavascriptExecutor(FILTER_MENU);
        if(transmission.equals("automatic")){
            driverTools.swipeToElement(AUTOMATIC_TRANSMISSION, Swipe.UP,5);
            driverTools.clickWithJavascriptExecutor(AUTOMATIC_TRANSMISSION);
        }else{
            driverTools.swipeToElement(MANUAL_TRANSMISSION, Swipe.UP,5);
            driverTools.clickWithJavascriptExecutor(MANUAL_TRANSMISSION);
        }
        driverTools.clickWithJavascriptExecutor(BOTTOMSHEET_SAVE_BUTTON);

    }
    public void chooseCar(){
        driverTools.clickWithJavascriptExecutor(FIRST_CAR_CARD);
    }
    public void chooseVendor(){
        driverTools.clickWithJavascriptExecutor(FIRST_CAR_VENDOR_CARD);
    }
    public void waitForElement(String page) {
        if(page == "booking"){
            driverTools.waitForElementToAppear(WAIT_FOR_BOOKING_FORM, 30);
        }else{
            driverTools.waitForElementToAppear(ORDER_ID, 30);
        }
    }
}
