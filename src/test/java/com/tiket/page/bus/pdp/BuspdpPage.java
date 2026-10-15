package com.tiket.page.bus.pdp;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import io.appium.java_client.AppiumBy;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public abstract class BuspdpPage <T extends WebDriver> extends BasePage<T>{
    private static final Locator TOTAL_PRICE_PDP = new Locator("TOTAL_PRICE_PDP", AppiumBy.xpath("(//div[contains(@class,'Footer_price')]//span)[1]"));
    private static final Locator SELECT_SEAT_BTN = new Locator("SELECT_SEAT_BTN", AppiumBy.xpath("//button[contains(@class,'Footer_button')]"));
    private static final Locator SEATS_ITEMS = new Locator("SEATS_ITEMS", AppiumBy.xpath("//button[not(contains(@class,'SeatSwitch_unavailable_seat')) and contains(@class,'seat_item')]"));
    private static final Locator CONTINUE_TO_BOOKING_FORM = new Locator("CONTINUE_TO_BOOKING_FORM", AppiumBy.xpath("//div[contains(@class,'Footer_button')]//button"));
    private static final Locator SELECT_RETURN_SEAT = new Locator("SELECT_RETURN_SEAT", AppiumBy.xpath("(//button[@data-testid='seat-submit-button'])"));

    private static final Locator TOTAL_PRICE_IN_SEAT_SECTION = new Locator("TOTAL_PRICE_IN_SEAT_SECTION", AppiumBy.xpath("(//section[contains(@class,'Footer_submit')]//span)[1]"));

    public BuspdpPage(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> BuspdpPage<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case ANDROID -> {return new BuspdpPageAndroid<>(session);}
            case IOS -> {return new BuspdpPageIOS<>(session);}
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    public String getFinalPriceCurrency(){
        driverTools.switchToWebViewUrl("bus-travel/detail");
        driverTools.waitForElementToAppear(TOTAL_PRICE_PDP,10);
        String finalPrice = driverTools.getText(TOTAL_PRICE_PDP).orElseThrow();
        String[] finalPriceSplited = finalPrice.split(" ");
        driverTools.switchToNative();
        return finalPriceSplited[0];
    }

    public String getSeatPriceCurrency(){
        driverTools.switchToWebView();
        if(driverTools.waitForElementToAppear(TOTAL_PRICE_IN_SEAT_SECTION,10).isPresent()) {
            String finalPrice = driverTools.getText(TOTAL_PRICE_IN_SEAT_SECTION).orElseThrow();
            String[] finalPriceSplited = finalPrice.split(" ");
            driverTools.switchToNative();
            return finalPriceSplited[0];
        }

        logger.log("There is no seat price in the page");
        return "IDR";
    }

    public void openSeat(){
        driverTools.switchToWebViewUrl("bus-travel/detail");
        driverTools.click(SELECT_SEAT_BTN);
        driverTools.switchToNative();
    }

    public void clickReturnSeatButton() {
        driverTools.switchToWebView();
        if (driverTools.isElementDisplayed(SELECT_RETURN_SEAT)) {
            driverTools.clickWithJavascriptExecutor(SELECT_RETURN_SEAT);
            driverTools.waitForElementToDisappear(SELECT_RETURN_SEAT, 20);
        }
    }

    public void clickSelectSeatButton(){
        driverTools.switchToWebViewUrl("bus-travel/detail");
        driverTools.waitForElementToAppear(CONTINUE_TO_BOOKING_FORM);
        if(driverTools.isElementDisplayed(CONTINUE_TO_BOOKING_FORM)) {
            driverTools.clickWithJavascriptExecutor(CONTINUE_TO_BOOKING_FORM);
            driverTools.waitForElementToDisappear(CONTINUE_TO_BOOKING_FORM, 20);
        }
        driverTools.switchToNative();
    }
    
    public void changeSeat(){
        openSeat();
        driverTools.switchToWebView();
        driverTools.waitForElementToAppear(SEATS_ITEMS);
        List<WebElement> seatItems = driverTools.elements(SEATS_ITEMS).orElseThrow();
        int random = CoreFunctions.generateRandomInt(0, seatItems.size()-1);
        driverTools.click(seatItems.get(random));
        driverTools.switchToNative();
        clickSelectSeatButton();
    }
}
