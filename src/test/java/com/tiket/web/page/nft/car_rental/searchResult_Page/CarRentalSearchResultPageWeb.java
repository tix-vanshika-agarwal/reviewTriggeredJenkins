package com.tiket.web.page.nft.car_rental.searchResult_Page;

import com.tiket.core.CoreFunctions;
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
import java.util.Objects;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

public abstract class CarRentalSearchResultPageWeb<T extends WebDriver> extends WebPage<T> {

    public abstract void editTotalDaysQueryParam(String queryParam, String newValue);

    private static final Locator FIRST_CAR_NAME = new Locator("FIRST_CAR_NAME", new By.ByXPath("(//*[contains(@class, 'FleetCard_title')])[1]"));
    private static final Locator FIRST_CAR_RENTAL_CARD = new Locator("FIRST_CAR_RENTAL_CARD", new By.ByXPath("(//*[contains(@class, 'FleetCard_container')])[1]"));
    private static final Locator FIRST_CAR_VENDOR_CARD = new Locator("FIRST_CAR_VENDOR_CARD", new By.ByXPath("(//*[contains(@class, 'VendorCard_content')])[1]"));
    private static final Locator FIRST_CAR_RENTAL_PRICE = new Locator("FIRST_CAR_RENTAL_PRICE",new By.ByXPath("(//span[contains(@class, 'Text_text') and contains(@class, 'Text_weight_bold') and contains(text(),'IDR')])[1]"));
    public static final Locator LOCATION_FORM = new Locator("LOCATION_FORM", AppiumBy.xpath("//span[contains(@class,'LocationInput_text')]"));
    public static final Locator DATE_FORM = new Locator("DATE_FORM", AppiumBy.xpath("//span[contains(@class,'DateInput_text')]"));
    private static final Locator CAR_BOOK_BUTTON = new Locator("CAR_BOOK_BUTTON", By.xpath("//button[contains(@class,'Button_variant_primary') and (contains(text(),'Booking Form') or contains(text(),'Form Pemesanan'))]"));
    private static final Locator CAR_BOOKING_CONTAINER = new Locator("CAR_BOOKING_CONTAINER", By.xpath("//div[contains(@class,'booking_booking_content_container')]"));
    private static final Locator CAR_MODEL_SRP = new Locator("CAR_MODEL_SRP", By.xpath("(//div[contains(@class, 'VendorPageContent_header')]//span[contains(@class, 'Text_text')])[1]"));
    private static final Locator VENDOR_NAME_SRP = new Locator("VENDOR_NAME_SRP", By.xpath("(//div[@data-testid='vendor-content-wrapper']//span[contains(@class, 'Text_text') and contains(@class, 'Text_variant_highEmphasis')])[1]"));
    private static final Locator CLOSE_MODAL = new Locator("CLOSE_MODAL", By.xpath("//button[@data-testid='modal-close-button' or contains(@class, 'BottomSheet_close_button')]"));

    private static final Locator FILTER_AND_SORT_BTN = new Locator("FILTER_AND_SORT_BTN", new By.ByXPath("(//span[normalize-space(text())='Filter & Sort' or normalize-space(text())='Filter & Urutkan'])[last()]"));
    private static final Locator TRANSMISSION_FILTER = new Locator("TRANSMISSION_FILTER", new By.ByXPath("//span[normalize-space(text())='Transmission' or normalize-space(text())='Transmisi']"));
    private static final Locator AUTOMATIC_BTN = new Locator("AUTOMATIC_BTN", new By.ByXPath("(//span[normalize-space(text())='Automatic' or normalize-space(text())='Otomatis'])[last()]"));
    private static final Locator MANUAL_BTN = new Locator("MANUAL_BTN", By.xpath("(//span[normalize-space(text())='Manual' or normalize-space(text())='Manual'])[last()]"));
    private static final Locator FIRST_CAR_TRANSMISSION = new Locator("FIRST_CAR_TRANSMISSION",By.xpath("(//*[contains(@class, 'FleetCard_container')])[1]//span[contains(@class, 'Text_text') and (contains(text(),'Automatic') or contains(text(),'Manual') or contains(text(),'Otomatis') or contains(text(),'Manual'))]"));
    private static final Locator SAVE_FILTER = new Locator("SAVE_FILTER", new By.ByXPath("(//button[contains(@class, 'Button_variant_primary') and contains(@class, 'Button_size_large')])[1]"));
    private static final Locator FILTER_CNT = new Locator("FILTER_CNT", new By.ByXPath("(//span[contains(@class, 'Notif_notif')])[last()]"));
    private static final Locator LIST_COUNT = new Locator("LIST_COUNT", new By.ByXPath("//div[contains(@class, 'FleetCard_card_wrapper')]"));
    private static final Locator FILTERED_CAR = new Locator("FILTERED_CAR", new By.ByXPath("(//span[contains(@class, 'Text_text') and contains(@class, 'Text_size_b3')])[3]"));
    private static final Locator VENDORS_FILTER = new Locator("VENDORS_FILTER", new By.ByXPath("//span[(normalize-space(text())='Vendors' or normalize-space(text())='Penyedia') and contains(@class, 'Text_text')]"));
    private static final Locator FIRST_VENDOR = new Locator("FIRST_VENDOR", new By.ByXPath("(//div[contains(@class,'FilterSortForm_checkbox_list')])[3]//div[contains(@class,'Text_size_b1')]"));
    private static final Locator FILTERED_VENDOR = new Locator("FILTERED_VENDOR", new By.ByXPath("//div[contains(@class, 'VendorCard_body')]//span[contains(@class, 'Text_variant_highEmphasis') and contains(@class, 'Text_weight_bold')]"));
    private static final Locator PRICE_OF_CAR = new Locator("PRICE_OF_CAR", new By.ByXPath("//span[contains(@class, 'Text_variant_alert') and contains(., '0')]"));
    private static final Locator POPUP_NEARBY_SKIP = new Locator("POPUP_NEARBY_SKIP", new By.ByXPath("//*[contains(text(), 'Oke, mengerti') or contains(text(), 'OK, got it') ]"));
    String filter;

    public CarRentalSearchResultPageWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> CarRentalSearchResultPageWeb<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new CarRentalSearchResultPageDWeb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new CarRentalSearchResultPageMWeb<>(session);
            }
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    public String getFirstCarName() {
        return driverTools.waitForElementToAppear(FIRST_CAR_NAME).orElseThrow().getText();
    }

    public void waitForFirstCar() {
        //need to wait for page to load properly
        driverTools.dummyWait(4);
        driverTools.waitForElementToPresence(FIRST_CAR_NAME).orElseThrow();
    }

    public String getCarModel(){
        return driverTools.waitForElementToAppear(CAR_MODEL_SRP).orElseThrow().getText();
    }
    public String getVendorName(){
        return driverTools.waitForElementToAppear(VENDOR_NAME_SRP).orElseThrow().getText();
    }

    public void selectFirstCarRentalCard() {
        driverTools.waitForElementToPresence(FIRST_CAR_RENTAL_CARD);
        driverTools.clickWithJavascriptExecutor(FIRST_CAR_RENTAL_CARD);
        driverTools.waitForElementToPresence(FIRST_CAR_VENDOR_CARD).orElseThrow();
        driverTools.clickWithJavascriptExecutor(FIRST_CAR_VENDOR_CARD);
    }

    public void selectOnlyFirstCarRentalCard() {
        driverTools.clickWithJavascriptExecutor(FIRST_CAR_RENTAL_CARD);
    }

    public void selectFirstCarVendorCard() {
        driverTools.waitForElementToPresence(FIRST_CAR_VENDOR_CARD).orElseThrow();
        driverTools.clickWithJavascriptExecutor(FIRST_CAR_VENDOR_CARD);
        driverTools.waitForElementToDisappear(FIRST_CAR_VENDOR_CARD);
    }

    public void openFirstCarRentalCard() {
        driverTools.waitForElementToAppear(FIRST_CAR_VENDOR_CARD,5);
        driverTools.clickWithJavascriptExecutor(FIRST_CAR_RENTAL_CARD);
    }

    public void closeFirstCarRentalCard() {
        driverTools.clickWithJavascriptExecutor(CLOSE_MODAL);
    }

    public abstract void editSearchResult(String location, String newDate);

    public abstract void changeDriverType(String driverType, String location, String newDate);

    public Locator getDateLocator(String date){
        String year = CoreFunctions.getSectionOfString(date," ", 2);
        String month = CoreFunctions.getSectionOfString(date," ", 1);
        String day = CoreFunctions.getSectionOfString(date," ", 0);
        String monthId = CoreFunctions.monthEngToId(month);
        String locatorTextEng = day + " " + month +" "+ year;
        String locatorTextId = day + " " + monthId +" "+ year;

        logger.log("date locator : " + locatorTextEng);
        return new Locator(locatorTextEng, AppiumBy.xpath("//*[contains(@aria-label,'" + locatorTextEng + "') or contains(@aria-label,'" + locatorTextId + "')]"));
    }

    public Locator getLocationTextLocator(String location) {
        return new Locator("LOCATION_TEXT", new By.ByXPath("//span[contains(@class, 'Text_text') and contains(normalize-space(text()), '" + location + "')]"));
    }

    public void proceedToBookingFormFromReviewPage() {
        driverTools.waitForElementToAppear(CAR_BOOK_BUTTON);
        driverTools.scrollElementToCenter(CAR_BOOK_BUTTON);
        driverTools.click(CAR_BOOK_BUTTON);
        driverTools.waitForElementToAppear(CAR_BOOKING_CONTAINER);
    }

    public void clickFilterBtn() {
        driverTools.waitForElementToBeClickable(FILTER_AND_SORT_BTN);
        driverTools.click(FILTER_AND_SORT_BTN);
    }

    public void clickTransmissionFilter(){
        driverTools.waitForElementToBeClickable(TRANSMISSION_FILTER);
        driverTools.clickWithJavascriptExecutor(TRANSMISSION_FILTER);
    }

    public void selectAMT() {
        driverTools.waitForElementToBeClickable(AUTOMATIC_BTN);
        driverTools.clickWithJavascriptExecutor(AUTOMATIC_BTN);
        filter = String.valueOf(driverTools.getText(AUTOMATIC_BTN));
    }

    public void selectManual() {
        driverTools.waitForElementToBeClickable(MANUAL_BTN);
        driverTools.clickWithJavascriptExecutor(MANUAL_BTN);
        filter = String.valueOf(driverTools.getText(MANUAL_BTN));
    }

    public String getTransmissionType() {
        driverTools.waitForElementToAppear(FIRST_CAR_TRANSMISSION);
        return driverTools.getText(FIRST_CAR_TRANSMISSION).orElseThrow();
    }

    public void clickSave() {
        driverTools.waitForElementToBeClickable(SAVE_FILTER);
        driverTools.clickWithJavascriptExecutor(SAVE_FILTER);
        skipNearbyLocationPopup();
    }

    public boolean isElementDisplayingText() {
        driverTools.waitForElementToAppear(FILTER_CNT);
        return driverTools.isElementDisplayed(FILTER_CNT);
    }

    public boolean checkList() {
        List<WebElement> carList = driverTools.elements(LIST_COUNT).orElseThrow();
        return !carList.isEmpty();
    }

    public boolean checkName() {
        String filtered_name = String.valueOf(driverTools.getText(FILTERED_CAR));
        return Objects.equals(filter, filtered_name);
    }

    public void clickVendorFilter(){
        driverTools.waitForElementToBeClickable(VENDORS_FILTER);
        driverTools.clickWithJavascriptExecutor(VENDORS_FILTER);
    }

    public String selectVendor(){
        driverTools.waitForElementToBeClickable(FIRST_VENDOR);
        String str = driverTools.getText(FIRST_VENDOR).orElseThrow();
        driverTools.clickWithJavascriptExecutor(FIRST_VENDOR);
        return str;
    }

    public String checkNameOfVendor() {
        driverTools.waitForElementToAppear(FILTERED_VENDOR,3);
        return driverTools.getText(FILTERED_VENDOR).orElseThrow();
    }

    public void selectFirstCarRentalCardForVendor() {
        driverTools.clickWithJavascriptExecutor(FIRST_CAR_RENTAL_CARD);
    }

    public String getFirstCarRentalPrice() {
        driverTools.waitForElementToAppear(FIRST_CAR_RENTAL_PRICE);
        return driverTools.getText(FIRST_CAR_RENTAL_PRICE).orElseThrow();
    }

    public String getPriceOnSRP(){
        driverTools.waitForElementToAppear(PRICE_OF_CAR);
        String priceOfCar = driverTools.getText(PRICE_OF_CAR).orElseThrow();
        Optional<Integer> priceOnSrp = CoreFunctions.getIntFromString(priceOfCar);
        int price = priceOnSrp.orElseThrow();
        return String.valueOf(price);
    }

    private static final Locator BACK_BTN = new Locator("BACK_BTN", new By.ByXPath("//*[contains(@class,'RPHeaderMobile_back_button')]"));

    public void selectDurationUsingDeeplink(String duration){
        //change duration using deeplink
        String currentUrl = driver.getCurrentUrl();
        String newUrl = CoreFunctions.updateQueryParameter(currentUrl, "duration", duration);
        logger.log("new url:" + newUrl);
        driverTools.get(newUrl);
        driverTools.waitForWebToLoad();
        driverTools.waitForElementToPresence(BACK_BTN);
    }

    public void skipNearbyLocationPopup(){
        driverTools.waitForElementToAppear(POPUP_NEARBY_SKIP,3).ifPresent(webElement -> {
            driverTools.clickWithJavascriptExecutor(POPUP_NEARBY_SKIP);
            logger.log("Nearby location popup found and skipped");
        });
    }

}