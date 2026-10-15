package com.tiket.web.page.nft.car_rental.search_Page;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Deeplink;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.pojo.carrental.CarRentalLandingPageData;
import com.tiket.web.WebPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import com.tiket.testbase.Configs;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.WebElement;

public abstract class CarRentalSearchPageWeb<T extends WebDriver> extends WebPage<T> {

    public CarRentalSearchPageWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> CarRentalSearchPageWeb<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new CarRentalSearchPageDWeb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new CarRentalSearchPageMWeb<>(session);
            }
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    public abstract Locator getDurationTextLocator(String duration);
    public abstract void selectDuration(String duration);
    public abstract String clickFaqLink();

    private static final Locator SKIP_BUTTON = new Locator("SKIP_BUTTON", new By.ByXPath("//*[contains(@class, 'skip_btn')]"));
    private static final Locator LOCATION_INPUT_TEXT = new Locator("LOCATION_INPUT_TEXT", new By.ByXPath("//*[contains(@class, 'LocationInput_input')]"));
    private static final Locator LOCATION_INPUT_BOX = new Locator("LOCATION_INPUT_BOX", new By.ByXPath("//*[contains(@class, 'SearchBox_placeholder')]//input"));
    private static final Locator SET_PICKUP_LOCATION_BUTTON = new Locator("SET_PICKUP_LOCATION_BUTTON", new By.ByXPath("(//*[contains(@class, 'Button_size_large')])[2]"));
    public static final Locator DURATION_INPUT_TEXT = new Locator("DURATION_INPUT_TEXT", new By.ByXPath("//*[contains(@class, 'DurationInput_duration_input')]"));
    private static final Locator SEARCH_BUTTON = new Locator("SEARCH_BUTTON", new By.ByXPath("//*[contains(@class, 'search_button')]"));
    private static final Locator COACH_MARK_BUTTON = new Locator("COACH_MARK_BUTTON", new By.ByXPath("//*[contains(@class, 'coachmark_done')]"));
    public static final Locator DATE_SECTION = new Locator("DATE_SECTION", new By.ByXPath("//*[contains(@class, 'DateInput') and contains(@class, 'Text')]"));
    public static final Locator SAVE_BUTTON = new Locator("SAVE_BUTTON", new By.ByXPath("//button[normalize-space()='Save' or normalize-space()='Simpan']"));
    private static final Locator PLAY_BUTTON = new Locator("PLAY_BUTTON", new By.ByXPath("//*[contains(@class, 'VideoContent_circle_bg')]"));
    private static final Locator HOW_TO_RENT_BANNER = new Locator("HOW_TO_RENT_BANNER", new By.ByXPath("//*[text()='How to Rent a Car' or text()='Cara Menyewa Mobil']"));
    private static final Locator VIDEO_PLAYED = new Locator("VIDEO_PLAYED", new By.ByXPath("//*[contains(@id, 'youtube-iframe')]"));
    private static final Locator SELECTED_DRIVER_TYPE_TEXT = new Locator("SELECTED_DRIVER_TYPE_TEXT", new By.ByXPath("//*[contains(@class,'TripTypeTogglerTab_active')]//*[contains(@class,'Text_text')]"));
    private static final Locator SELECTED_LOCATION_TEXT = new Locator("SELECTED_LOCATION_TEXT", new By.ByXPath("//*[contains(@class,'LocationInput_text')]"));
    private static final Locator SELECTED_DATE_TEXT = new Locator("SELECTED_LOCATION_TEXT", new By.ByXPath("//*[contains(@class,'DateInput_text')]"));
    private static final Locator SELECTED_TIME_TEXT = new Locator("SELECTED_TIME_TEXT", new By.ByXPath("//*[contains(@class,'DateInput_input_time')]//*[contains(@class,'Text_text')]"));
    private static final Locator TIME_DURATION_12_OR_24_HOUR_FORMAT = new Locator("TIME_DURATION_12_OR_24_HOUR_FORMAT", new By.ByXPath("//*[contains(@class,'SearchFormDisplay_auxiliary_container')]//span"));

    public void clickSkipButton() {
        driverTools.waitForElementToAppear(SKIP_BUTTON);
        if (driverTools.isElementDisplayed(SKIP_BUTTON)) {
            driverTools.clickWithJavascriptExecutor(SKIP_BUTTON);
        }
    }

    public void selectDriverType(String type) {
        Locator DRIVER_TYPE_TAB = getDriverTypeTabLocator(type);
        driverTools.waitForElementToAppear(DRIVER_TYPE_TAB);
        driverTools.clickWithJavascriptExecutor(DRIVER_TYPE_TAB);
    }

    private Locator getDriverTypeTabLocator(String type) {
        String idText, enText;
        if (type.trim().equalsIgnoreCase("with-driver") || type.trim().equalsIgnoreCase("with_driver")) {
            idText = "Dengan Sopir";
            enText = "With Driver";
        }
        else {
            idText = "Lepas Kunci";
            enText = "Self Drive";
        }
        return new Locator("DRIVER_TYPE_TAB", new By.ByXPath("//button//span[text()='" + idText + "' or text()='" + enText + "']"));
    }

    public void selectLocationByRecommendation(String location) {
        driverTools.click(LOCATION_INPUT_TEXT);
        Locator LOCATION_TEXT = getLocationTextLocator(location);
        driverTools.waitForElementToAppear(LOCATION_TEXT);
        driverTools.clickWithJavascriptExecutor(LOCATION_TEXT);
        if (driverTools.isElementDisplayed(SET_PICKUP_LOCATION_BUTTON)) {
            driverTools.click(SET_PICKUP_LOCATION_BUTTON);
        }
    }

    public void selectCarRentalLocationBySearch(String location) {
        driverTools.clickWithJavascriptExecutor(LOCATION_INPUT_TEXT);
        driverTools.clickWithJavascriptExecutor(LOCATION_INPUT_BOX);
        driverTools.sendKeys(LOCATION_INPUT_BOX, location);
        Locator LOCATION_TEXT = getLocationTextLocatorBySearch(location);
        driverTools.waitForElementToAppear(LOCATION_TEXT);
        driverTools.clickWithJavascriptExecutor(LOCATION_TEXT);
        driverTools.waitForElementToAppear(SET_PICKUP_LOCATION_BUTTON).ifPresent(driverTools::clickWithJavascriptExecutor);
    }

    private Locator getLocationTextLocator(String location) {
        return new Locator("LOCATION_TEXT", new By.ByXPath("//*[contains(@class, 'List_title') and text()='" + location + "']"));
    }

    private Locator getLocationTextLocatorBySearch(String location) {
        return new Locator("LOCATION_TEXT", new By.ByXPath("(//*[contains(@class, 'highlighted') and text()='" + location + "'])[1]"));
    }

    public Locator getDateLocator(String date) {
        String year = CoreFunctions.getSectionOfString(date," ", 2);
        String month = CoreFunctions.getSectionOfString(date," ", 1);
        String day = CoreFunctions.getSectionOfString(date," ", 0);
        String monthId = CoreFunctions.monthEngToId(month);
        String locatorTextEng = day + " " + month + " " + year;
        String locatorTextId = day + " " + monthId + " " + year;
        return new Locator(locatorTextEng, new By.ByXPath("//*[contains(@aria-label, '" + locatorTextEng + "') or contains(@aria-label, '" + locatorTextId + "')]"));
    }

    public void selectDate(String departureDate) {
        driverTools.clickWithJavascriptExecutor(DATE_SECTION);
        WebElement dateElement = driverTools.waitForElementToPresence(getDateLocator(departureDate)).orElseThrow();
        driverTools.clickWithJavascriptExecutor(dateElement);
        driverTools.waitForElementToPresence(SAVE_BUTTON, 5)
                .ifPresentOrElse(
                        element -> driverTools.clickWithJavascriptExecutor(SAVE_BUTTON),
                        () -> logger.log("Save button not found - continuing test")
                );
    }

    public void searchCarRental() {
        driverTools.waitForElementToAppear(SEARCH_BUTTON, 5);
        driverTools.clickWithJavascriptExecutor(SEARCH_BUTTON);
        if (!driverTools.waitForElementToDisappear(SEARCH_BUTTON, 5)) {
            driverTools.click(SEARCH_BUTTON);
        }
        driverTools.waitForElementToAppear(COACH_MARK_BUTTON, 5).ifPresent(driverTools::clickWithJavascriptExecutor);
    }

    public abstract String getFormValue(String type, String page);

    public boolean playVideo() {
        WebElement playBtn = driverTools.waitForElementToAppear(PLAY_BUTTON).orElseThrow();
        driverTools.scrollIntoWebView(playBtn);
        driverTools.clickWithJavascriptExecutor(PLAY_BUTTON);
        driverTools.waitForElementToAppear(VIDEO_PLAYED);
        return driverTools.isElementDisplayed(VIDEO_PLAYED);
    }

    public String clickHowToRentBanner() {
        WebElement banner = driverTools.waitForElementToAppear(HOW_TO_RENT_BANNER).orElseThrow();
        driverTools.scrollIntoWebView(banner);
        driverTools.clickWithJavascriptExecutor(HOW_TO_RENT_BANNER);
        String currentUrl = driver.getCurrentUrl();
        logger.log("current url:" + currentUrl);
        return currentUrl;
    }

    public CarRentalLandingPageData getSelectedData(){
        CarRentalLandingPageData carRentalLandingPageData = new CarRentalLandingPageData();
        WebElement driverType = driverTools.waitForElementToAppear(SELECTED_DRIVER_TYPE_TEXT).orElseThrow();
        carRentalLandingPageData.setTripType(driverTools.getText(driverType));
        WebElement location = driverTools.waitForElementToAppear(SELECTED_LOCATION_TEXT).orElseThrow();
        carRentalLandingPageData.setLocationSelected(driverTools.getText(location));
        WebElement date = driverTools.waitForElementToAppear(SELECTED_DATE_TEXT).orElseThrow();
        carRentalLandingPageData.setDateSelected(driverTools.getText(date).split(",")[1].trim());
        WebElement time = driverTools.waitForElementToAppear(SELECTED_TIME_TEXT).orElseThrow();
        carRentalLandingPageData.setTimeSelected(driverTools.getText(time));
        WebElement timeDuration12Or24HourFormat = driverTools.waitForElementToAppear(TIME_DURATION_12_OR_24_HOUR_FORMAT).orElseThrow();
        carRentalLandingPageData.setTimeDurationFormat(driverTools.getText(timeDuration12Or24HourFormat));

        logger.log("data: " + carRentalLandingPageData.toString());
        return carRentalLandingPageData;
    }
}