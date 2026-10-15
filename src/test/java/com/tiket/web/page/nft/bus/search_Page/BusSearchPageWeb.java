package com.tiket.web.page.nft.bus.search_Page;

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

public abstract class BusSearchPageWeb<T extends WebDriver> extends WebPage<T> {

    public BusSearchPageWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> BusSearchPageWeb<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new BusSearchPageDWeb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new BusSearchPageMWeb<>(session);
            }
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    public abstract void setPassenger(int passengerCount);

    private static final Locator SKIP_BUTTON = new Locator("SKIP_BUTTON", new By.ByXPath("//*[contains(@class, 'Coachmark_coachmark_skip_btn')]"));
    private static final Locator ORIGIN_LOCATION_INPUT = new Locator("ORIGIN_LOCATION_INPUT", new By.ByXPath("(//*[contains(@class, 'LocationInput_text')])[1]"));
    private static final Locator DESTINATION_LOCATION_INPUT = new Locator("DESTINATION_LOCATION_INPUT", new By.ByXPath("(//*[contains(@class, 'LocationInput_text')])[2]"));
    private static final Locator LOCATION_INPUT_BOX = new Locator("LOCATION_INPUT_BOX", new By.ByXPath("//*[contains(@class, 'SearchBox_placeholder')]//input"));
    public static final Locator DATE_SECTION = new Locator("DATE_SECTION", new By.ByXPath("//*[contains(@class, 'DateInput') and contains(@class, 'Text')]"));
    private static final Locator PASSENGER_INPUT = new Locator("PASSENGER_INPUT", new By.ByXPath("//*[contains(@class, 'PassengerInput_input_text')]"));
    public static final Locator PASSENGER_SAVE_BUTTON = new Locator("PASSENGER_SAVE_BUTTON", new By.ByXPath("(//*[contains(@class, 'Button_full_width')])[last()]"));
    public static final Locator SEARCH_BUTTON = new Locator("SEARCH_BUTTON", AppiumBy.xpath("//*[contains(@class, 'Button_variant_primary') and contains(@class, 'Button_size_large')]"));
    public static final Locator SAVE_DATE_BUTTON = new Locator("SAVE_DATE_BUTTON", new By.ByXPath("//button[normalize-space()='Save' or normalize-space()='Simpan']"));
    private static final Locator DEPART_DATE_CALENDAR = new Locator("DEPART_DATE_CALENDAR", new By.ByXPath("//span[contains(@class, 'Date')]"));
    public static final Locator DATE_TEXT = new Locator("DATE_TEXT", AppiumBy.xpath("//*[contains(@class, 'DateInput_text')]"));


    public void clickSkipButton() {
        driverTools.waitForElementToAppear(SKIP_BUTTON,5);
        if (driverTools.isElementDisplayed(SKIP_BUTTON)) {
            driverTools.clickWithJavascriptExecutor(SKIP_BUTTON);
        }
    }

    public void selectType(String type) {
        Locator TYPE_TAB = getTypeTabLocator(type);
        driverTools.clickWithJavascriptExecutor(TYPE_TAB);
    }

    private Locator getTypeTabLocator(String type) {
        String idText, enText;
        if (type.equals("oneway")) {
            idText = "Sekali jalan";
            enText = "One way";
        }
        else {
            idText = "Pulang pergi";
            enText = "Round trip";
        }
        return new Locator("TYPE_TAB", new By.ByXPath("//button//span[text()='" + idText + "' or text()='" + enText + "']"));
    }

    public void clickOriginLocationInput() {
        driverTools.clickWithJavascriptExecutor(ORIGIN_LOCATION_INPUT);
    }

    public void clickDestinationLocationInput() throws InterruptedException {
        driverTools.clickWithJavascriptExecutor(DESTINATION_LOCATION_INPUT);
        Thread.sleep(1000);
    }

    public void clickOnCalendarWidgetToOpen() {
        logger.log("Opening calendar widget...");
        driverTools.clickWithJavascriptExecutor(DEPART_DATE_CALENDAR);
    }

    public void selectLocation(String location) {
        WebElement locationInput = driverTools.waitForElementToAppear(LOCATION_INPUT_BOX).orElseThrow();
        driverTools.sendKeys(locationInput, location);

        Locator LOCATION_TEXT = getLocationTextLocator(location);
        driverTools.waitForElementToAppear(LOCATION_TEXT);
        driverTools.clickWithJavascriptExecutor(LOCATION_TEXT);
    }

    private Locator getLocationTextLocator(String location) {
        return new Locator("LOCATION_TEXT", new By.ByXPath("//*[@id='modal-root']//div[@role='button'][.//*[contains(normalize-space(),'" + location + "')]]"));
    }

    public boolean isLocationSelected(String location) {
        Locator SELECTED_LOCATION = getSelectedLocationTextLocator(location);
        driverTools.waitForElementToAppear(SELECTED_LOCATION);
        return driverTools.isElementDisplayed(SELECTED_LOCATION);
    }

    private Locator getSelectedLocationTextLocator(String location) {
        return new Locator("SELECTED_LOCATION", new By.ByXPath("//*[contains(@class, 'LocationInput_text') and text()='" + location + "']"));
    }

    public void selectDate(String departureDate) {
        driverTools.clickWithJavascriptExecutor(DATE_SECTION);
        driverTools.clickWithJavascriptExecutor(getDateLocator(departureDate));
        driverTools.waitForElementToAppear(SAVE_DATE_BUTTON).ifPresent(driverTools::clickWithJavascriptExecutor);
    }

    public void selectDate(String departureDate, String returnDate) {
        driverTools.clickWithJavascriptExecutor(DATE_SECTION);
        driverTools.waitForElementToAppear(getDateLocator(departureDate));
        driverTools.clickWithJavascriptExecutor(getDateLocator(departureDate));
        driverTools.clickWithJavascriptExecutor(getDateLocator(returnDate));
        driverTools.waitForElementToAppear(SAVE_DATE_BUTTON).ifPresent(driverTools::click);
    }

    public Locator getDateLocator(String date) {
        String year = CoreFunctions.getSectionOfString(date," ", 2);
        String month = CoreFunctions.getSectionOfString(date," ", 1);
        String day = CoreFunctions.getSectionOfString(date," ", 0);
        String monthId = CoreFunctions.monthEngToId(month);
        String locatorTextEng = day + " " + month + " " + year;
        String locatorTextId = day + " " + monthId + " " + year;
        return new Locator(locatorTextEng, AppiumBy.xpath("//*[contains(@aria-label, '" + locatorTextEng + "') or contains(@aria-label, '" + locatorTextId + "')]"));
    }

    public void clickPassengerInput() {
        driverTools.clickWithJavascriptExecutor(PASSENGER_INPUT);
    }

    public void searchBus() {
        driverTools.clickWithJavascriptExecutor(SEARCH_BUTTON);
        driverTools.waitForElementToAppear(SKIP_BUTTON, 2).ifPresent(driverTools::click);
    }

    public String getDateText() {
        String dateText = driverTools.waitForElementToAppear(DATE_TEXT).orElseThrow().getText();
        logger.log("Date = " + dateText);
        return dateText;
    }
}