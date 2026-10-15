package com.tiket.web.page.nft.airport_transfer.pdp;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import io.appium.java_client.AppiumBy;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public abstract class AirportTransferPDPWeb<T extends WebDriver> extends WebPage<T> {

    public AirportTransferPDPWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> AirportTransferPDPWeb<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new AirportTransferPDPDWeb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new AirportTransferPDPMWeb<>(session);
            }
            default -> throw new NotImplementedException("Unsupported platform: " + Configs.PLATFORM);
        }
    }

    public static final Locator SUBMIT_BUTTON = new Locator("SUBMIT_BUTTON", AppiumBy.cssSelector("*[class*='SubmissionFooter_submit_button']"));
    private static final Locator PRICE_BOTTOM_SHEET_PDP = new Locator("PRICE_BOTTOM_SHEET_PDP", new By.ByXPath("(//*[contains(@class, 'SubmissionFooter_final_price')])"));
    private static final Locator AIRPORT_LOCATION_NAME_BOOKING_FORM = new Locator("AIRPORT_LOCATION_NAME_BOOKING_FORM", new By.ByXPath("(//p[contains(@class,'Text_text') and contains(@class,'Text_size_b2') and contains(@class,'Text_weight_bold')])[1]"));
    private static final Locator DESTINATION_LOCATION_NAME_BOOKING_FORM = new Locator("DESTINATION_LOCATION_NAME_BOOKING_FORM", new By.ByXPath("//section[contains(@class,'PriceInfoBottomSheet_trip_route')]//div[contains(@class,'TripRoute_trip_route_destination')]//div[contains(@class,'TripRoute_trip_route_name')]/p[contains(@class,'Text_weight_bold')]"));
    private static final Locator TOTAL_PRICE_UNDER_BOTTOM_SHEET = new Locator("TOTAL_PRICE_UNDER_BOTTOM_SHEET", new By.ByXPath("(//*[contains(@class, 'PriceInfoBottomSheet_total_price_section')])//span[2]"));
    private static final Locator CLOSE_BOTTOM_SHEET = new Locator("CLOSE_BOTTOM_SHEET", new By.ByXPath("(//*[contains(@class, 'BottomSheet_close_button')])"));
    public static final Locator PRICE_VALUE = new Locator("PRICE_VALUE",AppiumBy.xpath("//span[contains(@class, 'Text_variant_price') and contains(text(), 'IDR')]"));

    public void clickSubmitCar() {
        driverTools.click(SUBMIT_BUTTON);
    }
    public void proceedToBookingPage(){
        driverTools.waitForElementToAppear(SUBMIT_BUTTON);
        driverTools.click(SUBMIT_BUTTON);
        driverTools.waitForElementToDisappear(SUBMIT_BUTTON);
    }

    public void openPriceBottomSheet(){
        driverTools.click(PRICE_BOTTOM_SHEET_PDP);
    }

    public String checkForAirportLocationName(){
        driverTools.waitForElementToAppear(AIRPORT_LOCATION_NAME_BOOKING_FORM).orElseThrow();
        return driverTools.getText(AIRPORT_LOCATION_NAME_BOOKING_FORM).orElseThrow();
    }
    public String checkForDestinationLocationName(){
        driverTools.waitForElementToAppear(DESTINATION_LOCATION_NAME_BOOKING_FORM).orElseThrow();
        return driverTools.getText(DESTINATION_LOCATION_NAME_BOOKING_FORM).orElseThrow();
    }

    public String checkForAirportLocationNameTransferOut(){
        driverTools.waitForElementToAppear(DESTINATION_LOCATION_NAME_BOOKING_FORM).orElseThrow();
        return driverTools.getText(DESTINATION_LOCATION_NAME_BOOKING_FORM).orElseThrow();
    }
    public String checkForDestinationLocationNameTransferOut(){
        driverTools.waitForElementToAppear(AIRPORT_LOCATION_NAME_BOOKING_FORM).orElseThrow();
        return driverTools.getText(AIRPORT_LOCATION_NAME_BOOKING_FORM).orElseThrow();
    }

    public String getTotalPrice(){
        driverTools.waitForElementToAppear(TOTAL_PRICE_UNDER_BOTTOM_SHEET).orElseThrow();
        return driverTools.getText(TOTAL_PRICE_UNDER_BOTTOM_SHEET).orElseThrow();
    }

    public void closePriceBottomSheet(){
        driverTools.back();
    }

    public String getPrice(){
        WebElement element = driverTools.waitForElementToAppear(PRICE_VALUE).orElseThrow();
        String price = driverTools.getText(element);
        logger.log("PRICE: " + price);
        return price;
    }
}
