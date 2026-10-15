package com.tiket.web.page.nft.train.search_Page;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public abstract class TrainSearchPageWeb<T extends WebDriver> extends WebPage<T> {
    public TrainSearchPageWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> TrainSearchPageWeb<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new TrainSearchPageDWeb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new TrainSearchPageMWeb<>(session);
            }
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    private static final Locator SKIP_BUTTON = new Locator("SKIP_BUTTON", new By.ByXPath("//*[contains(@class, 'skip_btn')]"));
    private static final Locator TRIP_TOGGLE = new Locator("TRIP_TOGGLE", new By.ByXPath("//*[contains(@class, 'toggle_small')]"));
    private static final Locator RETURN_DATE_CALENDAR = new Locator("RETURN_DATE_CALENDAR", new By.ByXPath("(//span[contains(@class, 'Date')])[2]"));
    private static final Locator SEARCH_BUTTON = new Locator("SEARCH_BUTTON", new By.ByXPath("(//*[contains(@class, 'Button_variant')])[2]"));
    private static final Locator PASSENGER_BTN = new Locator("PASSENGER_BTN", new By.ByXPath("//div[contains(@class, 'PassengerInput_input_text')]//span"));
    private static final Locator ADULT_CNT = new Locator("ADULT_CNT", new By.ByXPath("(//input[contains(@class, 'QuantityEditor_quantity_input')])[1]"));
    private static final Locator INFANT_CNT = new Locator("INFANT_CNT", new By.ByXPath("(//input[contains(@class, 'QuantityEditor_quantity_input')])[2]"));
    private static final Locator ADULT_DEC = new Locator("ADULT_DEC", new By.ByXPath("(//button[contains(@class, 'QuantityEditor_operation_button')])[1]"));
    private static final Locator ADULT_INC = new Locator("ADULT_INC", new By.ByXPath("(//button[contains(@class, 'QuantityEditor_operation_button')])[2]"));
    private static final Locator INFANT_DEC = new Locator("INFANT_DEC", new By.ByXPath("(//button[contains(@class, 'QuantityEditor_operation_button')])[3]"));
    private static final Locator INFANT_INC = new Locator("INFANT_INC", new By.ByXPath("(//button[contains(@class, 'QuantityEditor_operation_button')])[4]"));
    private static final Locator SAVE_BTN = new Locator("SAVE BTN", new By.ByXPath("(//button[contains(@class, 'Button_button') and contains(@class, 'Button_variant_primary')])[3]"));
    private static final Locator DEPART_DATE_CALENDAR = new Locator("DEPART_DATE_CALENDAR", new By.ByXPath("//span[contains(@class, 'Date')]"));

    public void clickSkipPopup() {
        driverTools.waitForElementToBeClickable(SKIP_BUTTON, 5).ifPresent(driverTools::clickWithJavascriptExecutor);
    }

    public abstract void selectOrigin(String stationName);

    public abstract void selectDestination(String stationName);

    public void toggleTripType(String tripType) {
        boolean current_trip_type = driverTools.waitForElementToAppear(RETURN_DATE_CALENDAR, 5).isPresent();
        switch (tripType) {
            case "oneway" -> {if(current_trip_type) driverTools.clickWithJavascriptExecutor(TRIP_TOGGLE);}
            case "roundtrip" -> {if(!current_trip_type) driverTools.clickWithJavascriptExecutor(TRIP_TOGGLE);}
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    public abstract void selectDate(String departureDate);

    public void clickOnCalendarWidgetToOpen() {
        logger.log("Opening calendar widget...");
        driverTools.clickWithJavascriptExecutor(DEPART_DATE_CALENDAR);
    }

    public void searchTrain() {
        driverTools.clickWithJavascriptExecutor(SEARCH_BUTTON);
    }

    public void selectPassenger(int adultCount, int infantCount) {
        driverTools.waitForElementToBeClickable(PASSENGER_BTN, 20).ifPresent(driverTools::clickWithJavascriptExecutor);

        driverTools.waitForElementToAppear(ADULT_CNT);
        driverTools.waitForElementToAppear(INFANT_CNT);
        int currentAdultCount = Integer.parseInt(
                driverTools.getAttribute(ADULT_CNT.by(), "value")
        );

        int currentInfantCount = Integer.parseInt(
                driverTools.getAttribute(INFANT_CNT.by(), "value")
        );

        if (adultCount < 1 || adultCount >= 4) {
            throw new IllegalArgumentException("Adult count must be between 1 and 4");
        }

        if (infantCount < 0 || infantCount >= 4) {
            throw new IllegalArgumentException("Infant count must be between 0 and 4");
        }

        while (currentAdultCount < adultCount) {
            driverTools.clickWithJavascriptExecutor(ADULT_INC);
            currentAdultCount++;
        }
        while (currentAdultCount > adultCount) {
            driverTools.clickWithJavascriptExecutor(ADULT_DEC);
            currentAdultCount--;
        }

        while (currentInfantCount < infantCount) {
            driverTools.clickWithJavascriptExecutor(INFANT_INC);
            currentInfantCount++;
        }
        while (currentInfantCount > infantCount) {
            driverTools.clickWithJavascriptExecutor(INFANT_DEC);
            currentInfantCount--;
        }
    }

    public void savePassengers() {
        driverTools.waitForElementToBeClickable(SAVE_BTN);
        driverTools.click(SAVE_BTN);
    }

    public void selectDate(String departureDate, String returnDate) {
        driverTools.click(DEPART_DATE_CALENDAR);
        String yearDep = CoreFunctions.getSectionOfString(departureDate, " ", 2);
        String monthEngDep = CoreFunctions.getSectionOfString(departureDate, " ", 1);
        String monthIdDep = CoreFunctions.monthEngToId(monthEngDep);
        String dayDep = CoreFunctions.getSectionOfString(departureDate, " ", 0);
        Locator dateLocatorDep = dateLocatorDep(dayDep, monthEngDep, monthIdDep, yearDep);
        driverTools.click(dateLocatorDep);
        String yearRet = CoreFunctions.getSectionOfString(returnDate, " ", 2);
        String monthEngRet = CoreFunctions.getSectionOfString(returnDate, " ", 1);
        String monthIdRet = CoreFunctions.monthEngToId(monthEngRet);
        String dayRet = CoreFunctions.getSectionOfString(returnDate, " ", 0);
        Locator dateLocatorRet = dateLocatorRet(dayRet, monthEngRet, monthIdRet, yearRet);
        driverTools.waitForElementToAppear(dateLocatorRet);
        driverTools.click(dateLocatorRet);
    }

    private Locator dateLocatorDep(String day, String monthEng, String monthId, String year) {
        return new Locator("DATE_LOCATOR", new By.ByXPath("(//span[contains(@aria-label, '" + day + " " + monthEng + " " + year + "') or contains(@aria-label, '" + day + " " + monthId + " " + year + "')])[1]"));
    }

    private Locator dateLocatorRet(String day, String monthEng, String monthId, String year) {
        return new Locator("DATE_LOCATOR", new By.ByXPath("(//span[contains(@aria-label, '" + day + " " + monthEng + " " + year + "') or contains(@aria-label, '" + day + " " + monthId + " " + year + "')])[1]"));
    }
}