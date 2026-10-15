package com.tiket.web.page.nft.whoosh.search_page;

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

import java.util.Locale;
import java.util.Objects;

public abstract class WhooshSearchPageWeb<T extends WebDriver> extends WebPage<T> {
    public WhooshSearchPageWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> WhooshSearchPageWeb<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new WhooshSearchPageDWeb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new WhooshSearchPageMWeb<>(session);
            }
            default -> throw new NotImplementedException("Not implemented");
        }
    }
    public static final Locator DATE_SECTION = new Locator("DATE_SECTION", new By.ByXPath("//*[contains(@class, 'DateInput') and contains(@class, 'Text')]"));
    public static final Locator SAVE_DATE_BUTTON = new Locator("SAVE_DATE_BUTTON", new By.ByXPath("//button[normalize-space()='Save' or normalize-space()='Simpan']"));
    private static final Locator SKIP_BUTTON = new Locator("SKIP_BUTTON", new By.ByXPath("//*[contains(@class, 'skip_btn')]"));
    private static final Locator TRIP_TOGGLE = new Locator("TRIP_TOGGLE", new By.ByXPath("//*[contains(@class, 'toggle_small')]"));
    private static final Locator WHOOSH_BTN = new Locator("WHOOSH_BTN", new By.ByXPath("(//button[contains(@class, 'Chip_chip') and contains(@class, 'Chip_size_default')])[2]"));
    private static final Locator SEARCH_BUTTON = new Locator("SEARCH_BUTTON", new By.ByXPath("(//*[contains(@class, 'Button_variant')])[2]"));
    private static final Locator SEARCH_PAGE_CONTAINER = new Locator("SEARCH_PAGE_CONTAINER", new By.ByXPath("//div[contains(@class, 'SearchForm_search_form')]"));
    private static final Locator RECENTLY_SEARCHED = new Locator("RECENTLY_SEARCHED", new By.ByXPath("//div[contains(@class, 'SliderRecentSearch_card') and contains(@class, 'Card_card_container')]"));
    private static final Locator DEPART_DATE_CALENDAR = new Locator("DEPART_DATE_CALENDAR", new By.ByXPath("//span[contains(@class, 'Date')]"));
    private static final Locator SAVE_DATE_BTN = new Locator("SAVE_DATE_BTN", new By.ByXPath("//button[normalize-space(text())='Save' or normalize-space(text())='Simpan']"));
    public static final Locator ORIGIN = new Locator("ORIGIN", new By.ByXPath("(//span[contains(@class, 'Location')])[1]"));
    public static final Locator FIRST_STATION = new Locator("FIRST_STATION", new By.ByXPath("//div[contains(@class, 'List_center_side')]"));
    public static final Locator DESTINATION = new Locator("DESTINATION", new By.ByXPath("(//span[contains(@class, 'Location')])[2]"));
    public static final Locator PASSENGER_BTN = new Locator("PASSENGER_BTN", new By.ByXPath("//div[contains(@class, 'PassengerInput_input_text')]//span"));
    public static final Locator ADULT_CNT = new Locator("ADULT_CNT", new By.ByXPath("(//input[contains(@class, 'QuantityEditor_quantity_input')])[1]"));
    public static final Locator INFANT_CNT = new Locator("INFANT_CNT", new By.ByXPath("(//input[contains(@class, 'QuantityEditor_quantity_input')])[2]"));
    public static final Locator ADULT_DEC = new Locator("ADULT_DEC", new By.ByXPath("(//button[contains(@class, 'QuantityEditor_operation_button')])[1]"));
    public static final Locator ADULT_INC = new Locator("ADULT_INC", new By.ByXPath("(//button[contains(@class, 'QuantityEditor_operation_button')])[2]"));
    public static final Locator INFANT_DEC = new Locator("INFANT_DEC", new By.ByXPath("(//button[contains(@class, 'QuantityEditor_operation_button')])[3]"));
    public static final Locator INFANT_INC = new Locator("INFANT_INC", new By.ByXPath("(//button[contains(@class, 'QuantityEditor_operation_button')])[4]"));
    public static final Locator RECENTLY_SEARCH_BOX = new Locator("RECENTLY_SEARCH_BOX", new By.ByXPath("//*[contains(@class,'SliderRecentSearch_title')]"));
    public static final Locator CLEAR_RECENTLY_SEARCH = new Locator("CLEAR_RECENTLY_SEARCH", new By.ByXPath("//*[contains(@class,'SliderRecentSearch_clear')]"));
    private static final Locator RETURN_DATE_CALENDAR = new Locator("RETURN_DATE_CALENDAR", new By.ByXPath("(//span[contains(@class, 'Date')])[2]"));

    public void clickSkipPopup() {
        driverTools.waitForElementToAppear(SKIP_BUTTON);
        if (driverTools.isElementDisplayed(SKIP_BUTTON)) {
            driverTools.click(SKIP_BUTTON);
        }
    }

    public void selectWhoosh() {
        driverTools.waitForElementToBeClickable(WHOOSH_BTN, 20).ifPresent(driverTools::click);
    }


    public void toggleTripType(String tripType) {
        boolean current_trip_type = driverTools.waitForElementToAppear(RETURN_DATE_CALENDAR, 5).isPresent();
        switch (tripType) {
            case "oneway" -> {if(current_trip_type) driverTools.clickWithJavascriptExecutor(TRIP_TOGGLE);}
            case "roundtrip" -> {if(!current_trip_type) driverTools.clickWithJavascriptExecutor(TRIP_TOGGLE);}
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    public void selectDate(String departureDate) {
        driverTools.clickWithJavascriptExecutor(DATE_SECTION);
        driverTools.clickWithJavascriptExecutor(getDateLocator(departureDate));
        driverTools.waitForElementToAppear(SAVE_DATE_BUTTON).ifPresent(driverTools::clickWithJavascriptExecutor);
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

    public abstract void savePassengers() ;

    public void searchWhoosh() {
        driverTools.clickWithJavascriptExecutor(SEARCH_BUTTON);
    }

    public boolean verifiesSearchPage() {
        driverTools.waitForElementToAppear(SEARCH_PAGE_CONTAINER);
        return driverTools.isElementPresent(SEARCH_PAGE_CONTAINER);
    }

    public boolean verifiesRecentlySearch() {
        driverTools.scrollIntoWebView(RECENTLY_SEARCHED);
        driverTools.waitForElementToAppear(RECENTLY_SEARCHED);
        return driverTools.isElementPresent(RECENTLY_SEARCHED);
    }

    public void clickRecentlySearchBox() {
        driverTools.waitForElementToAppear(RECENTLY_SEARCHED);
        driverTools.clickWithJavascriptExecutor(RECENTLY_SEARCHED);
    }

    public abstract void selectOrigin(String stationName);

    public abstract void selectDestination(String stationName);

    public abstract void selectPassenger(int adultCount, int infantCount);

    public void selectDate(String departureDate, String returnDate) {
        driverTools.clickWithJavascriptExecutor(DEPART_DATE_CALENDAR);
        String yearDep = CoreFunctions.getSectionOfString(departureDate, " ", 2);
        String monthEngDep = CoreFunctions.getSectionOfString(departureDate, " ", 1);
        String monthIdDep = CoreFunctions.monthEngToId(monthEngDep);
        String dayDep = CoreFunctions.getSectionOfString(departureDate, " ", 0);
        Locator dateLocatorDep = dateLocatorDep(dayDep, monthEngDep, monthIdDep, yearDep);
        driverTools.clickWithJavascriptExecutor(dateLocatorDep);
        String yearRet = CoreFunctions.getSectionOfString(returnDate, " ", 2);
        String monthEngRet = CoreFunctions.getSectionOfString(returnDate, " ", 1);
        String monthIdRet = CoreFunctions.monthEngToId(monthEngRet);
        String dayRet = CoreFunctions.getSectionOfString(returnDate, " ", 0);
        Locator dateLocatorRet = dateLocatorRet(dayRet, monthEngRet, monthIdRet, yearRet);
        driverTools.waitForElementToAppear(dateLocatorRet);
        driverTools.clickWithJavascriptExecutor(dateLocatorRet);
        if ( driverTools.isElementDisplayed(SAVE_DATE_BTN)) {
            driverTools.click(SAVE_DATE_BTN);
        }
    }

    public void clickOnCalendarWidgetToOpen() {
        logger.log("Opening calendar widget...");
        driverTools.clickWithJavascriptExecutor(DEPART_DATE_CALENDAR);
    }

    public String searchDepDate(String departureDate) {
        driverTools.clickWithJavascriptExecutor(DEPART_DATE_CALENDAR);
        String yearDep = CoreFunctions.getSectionOfString(departureDate, " ", 2);
        String monthEngDep = CoreFunctions.getSectionOfString(departureDate, " ", 1);
        String monthIdDep = CoreFunctions.monthEngToId(monthEngDep);
        String dayDep = CoreFunctions.getSectionOfString(departureDate, " ", 0);
        Locator dateLocatorDep = dateLocatorDep(dayDep, monthEngDep, monthIdDep, yearDep);
        driverTools.clickWithJavascriptExecutor(dateLocatorDep);
        if ( driverTools.isElementDisplayed(SAVE_DATE_BTN)) {
            driverTools.click(SAVE_DATE_BTN);
        }
        return yearDep;
    }

    private Locator dateLocatorDep(String day, String monthEng, String monthId, String year) {
        return new Locator("DATE_LOCATOR", new By.ByXPath("(//span[contains(@aria-label, '" + day + " " + monthEng + " " + year + "') or contains(@aria-label, '" + day + " " + monthId + " " + year + "')])[1]"));
    }

    private Locator dateLocatorRet(String day, String monthEng, String monthId, String year) {
        return new Locator("DATE_LOCATOR", new By.ByXPath("(//span[contains(@aria-label, '" + day + " " + monthEng + " " + year + "') or contains(@aria-label, '" + day + " " + monthId + " " + year + "')])[1]"));
    }

    public Locale getAppLocale() {
        String lang = System.getProperty("app.lang");
        if ("id".equalsIgnoreCase(lang)) {
            return new Locale("in", "ID");
        } else {
            return Locale.ENGLISH;
        }
    }

    public String getRecentlySearchedText() {
        WebElement recentlySearchedElement = driverTools.waitForElementToAppear(RECENTLY_SEARCH_BOX).orElseThrow();
        String recentlySearchedText = driverTools.getText(recentlySearchedElement);
        return recentlySearchedText.trim();
    }

    public void clearRecentlySearch(){
        driverTools.waitForElementToAppear(CLEAR_RECENTLY_SEARCH).ifPresent(driverTools::click);
    }
}