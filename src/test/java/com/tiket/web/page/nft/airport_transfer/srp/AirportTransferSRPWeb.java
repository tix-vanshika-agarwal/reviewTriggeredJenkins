package com.tiket.web.page.nft.airport_transfer.srp;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import io.appium.java_client.AppiumBy;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

import static com.tiket.testbase.Assertion.assertThat;
import static org.hamcrest.Matchers.containsString;

public abstract class AirportTransferSRPWeb<T extends WebDriver> extends WebPage<T> {

    public AirportTransferSRPWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> AirportTransferSRPWeb<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new SRPDWebAirportTransfer<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new SRPMWebAirportTransfer<>(session);
            }
            default -> throw new NotImplementedException("Unsupported platform: " + Configs.PLATFORM);
        }
    }

    private static final Locator CAR_NAME = new Locator("CAR_NAME", AppiumBy.xpath("//*[contains(@class,'CarCardList_card')]//*//following-sibling::*[contains(@class,'Text_weight_bold')]"));
    private static final Locator CAR_VENDOR = new Locator("CAR_VENDOR", AppiumBy.cssSelector("*[class*='FleetList_vendor_card_information']"));
    private static final Locator SKIP_TEXT = new Locator("SKIP_TEXT", new By.ByXPath("//span[normalize-space(text())='Skip' or normalize-space(text())='Lewati']"));
    private static final Locator SEARCH_CARS_BUTTON = new Locator("SEARCH_CARS_BUTTON", new By.ByXPath("//button//span[contains(text(), 'Search cars') or contains(text(), 'Cari mobil')]"));
    private static final Locator OK_BTN = new Locator("OK_BTN", new By.ByXPath("//span[contains(text(), 'OK') or contains(text(), 'Lanjut')]"));
    private static final Locator BACK_TO_LANDING_PAGE = new Locator("BACK_TO_LANDING_PAGE", new By.ByXPath("//div[contains(@class, 'ChangeSearchHeader_change_search')]//*[name()='svg']"));
    private static final Locator RECENTLY_SEARCH_TITLE = new Locator("RECENTLY_SEARCH_TITLE", new By.ByXPath("(//div[contains(@class, 'RecentSearchSection_title')])"));
    private static final Locator RECENTLY_SEARCH_CARDS = new Locator("RECENTLY_SEARCH_CARDS", new By.ByXPath("(//div[contains(@class, 'RecentSearchSection_location')])"));
    private static final Locator FIRST_CAR_ON_SRP = new Locator("FIRST_CAR_ON_SRP", new By.ByXPath("(//div[contains(@class, 'CarCardList_card_body')])"));
    private static final Locator CARD_PRICE = new Locator("CARD_PRICE", new By.ByXPath("(//span[contains(@class, 'Text_variant_price')])"));
    private static final Locator FIRST_CAR_VENDOR_CARD = new Locator("FIRST_CAR_VENDOR_CARD", new By.ByXPath("(//*[contains(@class, 'FleetList_vendor_card_information')])"));
    private static final Locator SAVE_ICON = new Locator("SAVE_ICON", new By.ByXPath("//div[contains(@data-hide-skip-button,'true')]//button//span"));
    public static final Locator CAR_TYPE = new Locator("CAR_TYPE",AppiumBy.xpath("//span[contains(@class, 'Header_title__') and contains(@class, 'Text_text__') and contains(@class, 'Text_size_h3__')]"));
    public static final Locator PRICE_VALUE = new Locator("PRICE_VALUE",AppiumBy.xpath("//span[contains(@class, 'Text_variant_price') and contains(text(), 'IDR')]"));


    public String chooseCar() {
        String carName = driverTools.getText(CAR_NAME).orElseThrow();
        driverTools.click(CAR_NAME);
        return carName;
    }

    public String getCarType(){
        WebElement carTypeNameElement = driverTools.waitForElementToAppear(CAR_TYPE).orElseThrow();
        String s = driverTools.getText(carTypeNameElement);
        logger.log("Car type on SRP: " + s);
        return s;
    }

    public String chooseVendor() {
        String vendorName = driverTools.getText(CAR_VENDOR).orElseThrow();
        driverTools.click(CAR_VENDOR);
        return vendorName;
    }

    public void ClickSkipBtn(){
        driverTools.waitForElementToAppear(SKIP_TEXT,1);
        driverTools.click(SKIP_TEXT);
    }

    public void clickSubmitBtn(){
        driverTools.waitForElementToAppear(SEARCH_CARS_BUTTON);
        driverTools.click(SEARCH_CARS_BUTTON);
    }

    public void clickGoItBtn(){
            driverTools.waitForElementToAppear(OK_BTN,3);
            if(driverTools.isElementDisplayed(OK_BTN)){
                driverTools.click(OK_BTN);
            }

            driverTools.waitForElementToAppear(SAVE_ICON,3);
            if(driverTools.isElementDisplayed(SAVE_ICON)){
                driverTools.clickWithJavascriptExecutor(SAVE_ICON);
            }
    }



    public void goBackToLandingPage(){
        driverTools.click(BACK_TO_LANDING_PAGE);
    }

    public boolean recentlySearched(){
        driverTools.waitForElementToAppear(RECENTLY_SEARCH_TITLE);
        boolean recentlySearchedDisplayed = driverTools.isElementDisplayed(RECENTLY_SEARCH_TITLE);
        boolean recentlySearchCardDisplayed = driverTools.isElementDisplayed(RECENTLY_SEARCH_CARDS);
        return recentlySearchedDisplayed && recentlySearchCardDisplayed;
    }

    public void selectRecentlySearchedCard(){
        driverTools.click(RECENTLY_SEARCH_CARDS);
        driverTools.isElementDisplayed(FIRST_CAR_ON_SRP);
    }

    public boolean checkCarRentalCard(){
        driverTools.waitForElementToAppear(FIRST_CAR_ON_SRP);
        boolean isFirstCarCardDisplayed = driverTools.isElementDisplayed(FIRST_CAR_ON_SRP);
        boolean isBackToSRPDisplayed = driverTools.isElementDisplayed(BACK_TO_LANDING_PAGE);
        return isFirstCarCardDisplayed && isBackToSRPDisplayed;
    }

    public void verifySRPPriceInIDR() {
        List<WebElement> priceList = driverTools.elements(CARD_PRICE).orElseThrow();
        for (WebElement priceElement : priceList) {
            String priceText = driverTools.getText(priceElement);
            assertThat("Verify prices in SRP card are in 'IDR'", priceText, containsString("IDR"));
        }
    }

    public void selectFirstSRPCard() {
        driverTools.waitForElementToAppear(FIRST_CAR_ON_SRP);
        driverTools.clickWithJavascriptExecutor(FIRST_CAR_ON_SRP);
        //If Car have multiple Vendor then pick first vendor else skip
        if(driverTools.waitForElementToAppear(FIRST_CAR_VENDOR_CARD,5).isPresent()) {
            driverTools.click(FIRST_CAR_VENDOR_CARD);
        }
    }

    public void selectCar(){
        driverTools.waitForElementToAppear(FIRST_CAR_ON_SRP);
        driverTools.clickWithJavascriptExecutor(FIRST_CAR_ON_SRP);
    }

    public void selectVendor(){
        if(driverTools.waitForElementToAppear(FIRST_CAR_VENDOR_CARD,5).isPresent()) {
            driverTools.click(FIRST_CAR_VENDOR_CARD);
            driverTools.waitForElementToDisappear(FIRST_CAR_VENDOR_CARD);
        }
    }

    public boolean isCarFarePresent(String currency){
        WebElement carFareElement = driverTools.waitForElementToAppear(getCarFarePriceLocator(currency)).orElseThrow();
        boolean res = driverTools.isElementDisplayed(carFareElement);
        logger.log("Car fare price is displayed: " + res);
        return res;
    }

    private Locator getCarFarePriceLocator(String currency){
        String xpath = "//span[contains(@class, 'Text_text') and contains(text(), 'Total')]";
        return new Locator("TOTAL_LABEL", By.xpath(xpath));
    }
}
