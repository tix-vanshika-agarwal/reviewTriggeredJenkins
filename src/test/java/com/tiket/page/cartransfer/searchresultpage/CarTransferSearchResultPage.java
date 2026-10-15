package com.tiket.page.cartransfer.searchresultpage;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import com.tiket.web.page.nft.airport_transfer.pdp.AirportTransferPDPWeb;
import com.tiket.web.page.nft.airport_transfer.srp.AirportTransferSRPWeb;
import io.appium.java_client.AppiumBy;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

import static org.hamcrest.Matchers.containsString;

public abstract class CarTransferSearchResultPage <T extends WebDriver> extends BasePage<T> {

    public CarTransferSearchResultPage(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> CarTransferSearchResultPage<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case ANDROID -> {return new CarTransferSearchResultPageAndroid <>(session);}
            case IOS -> {return new CarTransferSearchResultPageIOS <>(session);}
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    private static final Locator CAR_PRICE = new Locator("CAR_PRICE", AppiumBy.xpath("//span[contains(@class, 'Text_variant_alert')]"));
    private static final Locator CAR_VENDOR_PRICE = new Locator("CAR_VENDOR_PRICE", AppiumBy.xpath("//span[contains(@class,'FleetList_text_wrap') and contains(@class,'Text_variant_alert')]"));
    private static final Locator PDP_PRICE = new Locator("PDP_PRICE", AppiumBy.xpath("//span[contains(@class, 'Text_variant_price')]"));
    private static final Locator TIKET_POINTS_PRICE_DETAIL = new Locator("TIKET_POINTS_PRICE_DETAIL", AppiumBy.xpath("//span[contains(text(), 'points')]"));
    private static final Locator CHANGE = new Locator("CHANGE", AppiumBy.xpath("//button[.//*[text()='Change' or text()='Ubah']]"));
    private static final Locator DATE_TIME_SRP = new Locator("DATE_TIME_SRP", AppiumBy.xpath("//div[contains(@class, 'ChangeSearchHeader_cabin_class_standard')]"));

    public void selectCarAndVendor(){
        AirportTransferSRPWeb<?> airportTransferSearchPageWeb = AirportTransferSRPWeb.getInstance(session);
        airportTransferSearchPageWeb.selectFirstSRPCard();
    }
    public abstract String getDateSearchResultPage(String time);


    public boolean verifyCarVendorPricesInIDR() {
        List<WebElement> carVendorPrices = driverTools.elements(CAR_VENDOR_PRICE).orElseThrow();
        carVendorPrices.stream().forEach( priceElement -> Assertion.assertThat("Verify car vendor prices in srp is in IDR", driverTools.getText(priceElement), containsString("IDR")));
        return true;
    }

    public boolean verifyCarsOptionPriceInIDR() {
        List<WebElement> carVendorPrices = driverTools.elements(CAR_PRICE).orElseThrow();
        carVendorPrices.stream().forEach( priceElement -> Assertion.assertThat("Verify car vendor prices in srp is in IDR", driverTools.getText(priceElement), containsString("IDR")));
        return true;
    }

    public void selectVendor() {
        AirportTransferSRPWeb<?> airportTransferSearchPageWeb = AirportTransferSRPWeb.getInstance(session);
        airportTransferSearchPageWeb.selectVendor();
    }

    public void selectCar() {
        AirportTransferSRPWeb<?> airportTransferSearchPageWeb = AirportTransferSRPWeb.getInstance(session);
        airportTransferSearchPageWeb.selectCar();
    }

    public boolean verifyPricePdpInIDR() {
        WebElement pdpPriceElement = driverTools.waitForElementToAppear(PDP_PRICE).orElseThrow();
        Assertion.assertThat("Verify price in PDP is in IDR", driverTools.getText(pdpPriceElement), containsString("IDR"));
        driverTools.click(PDP_PRICE);
        return true;
    }

    public void continueToBookingPage() {
        AirportTransferPDPWeb<?> airportTransferProductPageWeb = AirportTransferPDPWeb.getInstance(session);
        airportTransferProductPageWeb.proceedToBookingPage();
    }

    public String getCarType(){
        AirportTransferSRPWeb<?> airportTransferSearchPageWeb = AirportTransferSRPWeb.getInstance(session);
        return airportTransferSearchPageWeb.getCarType();
    }

    public void openPriceDetail(){
        AirportTransferPDPWeb<?> airportTransferProductPageWeb = AirportTransferPDPWeb.getInstance(session);
        airportTransferProductPageWeb.openPriceBottomSheet();
    }


    public boolean isCarFarePresent(String currency){
        AirportTransferSRPWeb<?> airportTransferSearchPageWeb = AirportTransferSRPWeb.getInstance(session);
        return airportTransferSearchPageWeb.isCarFarePresent(currency);
    }

    public void closePriceDetail() {
        try {
            AirportTransferPDPWeb<?> airportTransferProductPageWeb = AirportTransferPDPWeb.getInstance(session);
            airportTransferProductPageWeb.closePriceBottomSheet();
        } catch (Exception e) {
            logger.log("Unable to close price detail: " + e.getMessage());
            driverTools.back();
        }
    }

    public boolean isTiketPointsPresent(){
        driverTools.waitForElementToAppear(TIKET_POINTS_PRICE_DETAIL, 2);
        return driverTools.isElementDisplayed(TIKET_POINTS_PRICE_DETAIL);
    }

    public String getPrice(){
       AirportTransferPDPWeb<?> airportTransferProductPageWeb = AirportTransferPDPWeb.getInstance(session);
       return airportTransferProductPageWeb.getPrice();
    }

    public boolean areWeOnSRP(){
        WebElement isFleetTitlesDisplayElement = driverTools.waitForElementToAppear(CHANGE).orElseThrow();
        return driverTools.isElementDisplayed(isFleetTitlesDisplayElement);
    }

    public void clickBackBtn() throws InterruptedException {
        driverTools.back();
    }

    public String getDateSearchResult(){
        WebElement dateTime = driverTools.waitForElementToAppear(DATE_TIME_SRP).orElseThrow();
        String date = driverTools.getText(dateTime);
        String[] parts = date.split("\\s+");
        if (parts.length >= 2) {
            String day = parts[1].replaceFirst("^0+", "");
            String month = parts[2];
            String dateOnly = day + " " + month;
            logger.log("DATE: " + dateOnly);
            return dateOnly;
        } else {
            throw new IllegalStateException("Unable to extract date from: " + date);
        }
    }

    public String verifyOrigin(String origin) {
        AirportTransferPDPWeb<?> airportTransferProductPageWeb = AirportTransferPDPWeb.getInstance(session);
        return airportTransferProductPageWeb.checkForAirportLocationName();
    }


    public String verifyDestination(String destination) {
        AirportTransferPDPWeb<?> airportTransferProductPageWeb = AirportTransferPDPWeb.getInstance(session);
        return airportTransferProductPageWeb.checkForDestinationLocationName();
    }
}
