package com.tiket.web.page.nft.ferry.LandingPage;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import com.tiket.web.page.nft.ferry.SearchResultPage.FerrySearchResultPageDWeb;
import com.tiket.web.page.nft.ferry.SearchResultPage.FerrySearchResultPageMWeb;
import com.tiket.web.page.nft.ferry.SearchResultPage.FerrySearchResultPageWeb;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.time.LocalDateTime;
import java.util.Map;

public abstract class FerrySearchPageWeb<T extends WebDriver> extends WebPage<T> {

    public static <T extends WebDriver> FerrySearchPageWeb<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new FerrySearchPageDWeb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new FerrySearchPageMWeb<>(session);
            }
            default -> throw new NotImplementedException("Not implemented");
        }
    }


    private static final Locator SKIP_BUTTON = new Locator("SKIP_BUTTON", new By.ByXPath("//*[contains(@class, 'Button_variant_tertiary')]"));
    private static final Locator DEPARTURE_PORT_INPUT = new Locator("DEPARTURE_PORT_INPUT", new By.ByXPath("(//*[contains(@class, 'PortSegment_container')])[1]"));
    private static final Locator DESTINATION_PORT_INPUT = new Locator("DESTINATION_PORT_INPUT", new By.ByXPath("(//*[contains(@class, 'PortSegment_container')])[2]"));
    private static final Locator DATE_INPUT = new Locator("DATE_INPUT", new By.ByXPath("//div[contains(@class, 'DateTimeInput_container_date') and span]"));
    private static final Locator CHECK_IN_TIME_INPUT = new Locator("CHECK_IN_TIME_INPUT", new By.ByXPath("//*[contains(@class, 'DateTimeInput_container_time')]"));
    private static final Locator TICKET_TYPE_INPUT = new Locator("TICKET_TYPE_INPUT", new By.ByXPath("//*[contains(@class, 'VehicleInput_input_text')]"));
    private static final Locator CLOSE_TICKET_TYPE_POP_UP = new Locator("CLOSE_TICKET_TYPE_POP_UP", new By.ByXPath("//*[contains(@class, 'Modal_modal_close')]"));
    private static final Locator PASSENGER_INPUT = new Locator("PASSENGER_INPUT", new By.ByXPath("//*[contains(@class, 'PassengerInput_input_text')]"));
    private static final Locator PASSENGER_SAVE_BUTTON = new Locator("PASSENGER_SAVE_BUTTON", new By.ByXPath("(//*[contains(@class, 'Button_full_width')])[last()]"));
    private static final Locator SEARCH_BUTTON = new Locator("SEARCH_BUTTON", new By.ByXPath("//*[contains(@class, 'SearchForm_submit_button')]"));
    private static final Locator COACH_MARK_BUTTON = new Locator("COACH_MARK_BUTTON", new By.ByXPath("//button[text()='OK' or text()='Oke']"));
    private static final Locator CALENDAR_MODAL = new Locator("CALENDAR_MODAL", new By.ByXPath("//section[contains(@class, 'Modal_modal_popup')]"));
    private static final Locator SAVE_DATE = new Locator("SAVE_DATE", new By.ByXPath("(//button[contains(@class, 'Button_button') and contains(@class, 'Button_size_large')])[2]"));


    public FerrySearchPageWeb(Session<T> session) {
        super(session);
    }

    public void clickSkipButton() {
        if (driverTools.isElementDisplayed(SKIP_BUTTON)) {
            driverTools.click(SKIP_BUTTON);
        }
    }

    public void clickDeparturePortInput() {
        driverTools.clickWithJavascriptExecutor(DEPARTURE_PORT_INPUT);
    }

    public void clickDestinationPortInput() {
        driverTools.clickWithJavascriptExecutor(DESTINATION_PORT_INPUT);
    }

    public void selectPort(String port) {
        Locator PORT_TEXT = getPortTextLocator(port);
        driverTools.waitForElementToAppear(PORT_TEXT).orElseThrow();
        driverTools.clickWithJavascriptExecutor(PORT_TEXT);
    }

    private Locator getPortTextLocator(String port) {
        return new Locator("PORT_TEXT", new By.ByXPath("(//*[contains(@class, 'Text_weight_bold') and text()='" + port + "'])[last()]"));
    }

    public void clickDateInput() {
        driverTools.click(DATE_INPUT);
        driverTools.waitForElementToAppear(CALENDAR_MODAL);
    }

    /**
     * In ferry, schedule comes from 3rd party and they provide schedule only on month end.
     * So in test data we always pass "30" as date.
     * If given date exceeds current month's last date (ex: 30 in February),
     * it will automatically pick the last available date of that month.
     *
     * @param departureDate Day from test data (example: "30")
     */
    public void selectDate(String departureDate) {
        String targetDateDeparture = "";

        LocalDateTime today = LocalDateTime.now();
        int lastDayOfMonth = today.toLocalDate().lengthOfMonth();
        int expectedDateFromTestData = Integer.parseInt(departureDate);
        int finalDateToPick = Math.min(expectedDateFromTestData, lastDayOfMonth);

        targetDateDeparture += finalDateToPick + " "
                + CoreFunctions.getMonthName(today.getMonthValue(), "en") + " "
                + today.getYear();

        driverTools.clickWithJavascriptExecutor(getDateLocator(targetDateDeparture));
        driverTools.waitForElementToAppear(SAVE_DATE).ifPresent(driverTools::click);
    }

    private Locator getDateLocator(String date) {
        String year = CoreFunctions.getSectionOfString(date, " ", 2);
        String month = CoreFunctions.getSectionOfString(date, " ", 1);
        String day = CoreFunctions.getSectionOfString(date, " ", 0);
        String monthId = CoreFunctions.monthEngToId(month);
        String locatorTextEng = day + " " + month + " " + year;
        String locatorTextId = day + " " + monthId + " " + year;
        return new Locator(locatorTextEng, new By.ByXPath("//*[contains(@aria-label, '" + locatorTextEng + "') or contains(@aria-label, '" + locatorTextId + "')]"));
    }

    public void clickCheckInTimeInput() {
        driverTools.click(CHECK_IN_TIME_INPUT);
    }

    public abstract void selectCheckInTime(String checkInTime);

    public void clickTicketTypeInput() {
        driverTools.click(TICKET_TYPE_INPUT);
    }

    public void selectTicketType(String ticketType) {
        Locator TICKET_TYPE = getTicketTypeLocator(ticketType);
        WebElement element = driverTools.waitForElementToAppear(TICKET_TYPE).orElseThrow();
        driverTools.scrollIntoWebView(element);
        driverTools.clickWithJavascriptExecutor(TICKET_TYPE);
        driverTools.waitForElementToDisappear(CLOSE_TICKET_TYPE_POP_UP);
        if (driverTools.isElementDisplayed(CLOSE_TICKET_TYPE_POP_UP)) {
            driverTools.clickWithJavascriptExecutor(CLOSE_TICKET_TYPE_POP_UP);
        }
    }

    private Locator getTicketTypeLocator(String ticketType) {
        String ticketTypeEn = ferryTicketTypeIdToEn.get(ticketType) == null ? ticketType : ferryTicketTypeIdToEn.get(ticketType);
        String ticketTypeId = ferryTicketTypeEnToId.get(ticketType) == null ? ticketType : ferryTicketTypeEnToId.get(ticketType);
        return new Locator("TICKET_TYPE", new By.ByXPath("//*[contains(@class, 'VehiclePopup_title') and (text()='" + ticketTypeEn + "' or text()='" + ticketTypeId + "')]"));
    }

    public static Map<String, String> ferryTicketTypeIdToEn = Map.of(
            "Mobil Penumpang", "Passenger Car",
            "Pejalan Kaki", "Pedestrian",
            "Sepeda", "Bicycle",
            "Truk Pickup", "Pickup Truck"
    );

    public static Map<String, String> ferryTicketTypeEnToId = Map.of(
            "Passenger Car", "Mobil Penumpang",
            "Pedestrian", "Pejalan Kaki",
            "Bicycle", "Sepeda",
            "Pickup Truck", "Truk Pickup"
    );

    public void clickPassengerInput() {
        driverTools.clickWithJavascriptExecutor(PASSENGER_INPUT);
    }

    public void setPassenger(String passengerType, int passengerCount) {
        int type = 0;
        switch (passengerType) {
            case "adult" -> type += 1;
            case "child" -> type += 2;
            case "infant" -> type += 3;
            case "elder" -> type += 4;
            default -> logger.fail("There is no passenger type: '" + passengerType + "'");
        }

        int currentCount = Integer.parseInt(driverTools.getValueAttribute(getPassengerCountLocator(type), "value"));
        if (currentCount < passengerCount) {
            clickPassengerCountIncrementLocator(type, passengerCount - currentCount);
        } else if (currentCount > passengerCount) {
            clickPassengerCountDecrementLocator(type, currentCount - passengerCount);
        }
    }

    private Locator getPassengerCountLocator(int type) {
        return new Locator("PASSENGER_INPUT_" + type + "CURRENT", new By.ByXPath("//*[contains(@class, 'PassengerPopup_counter')][" + type + "]//*[contains(@class, 'QuantityEditor_quantity_input')]"));
    }

    private void clickPassengerCountIncrementLocator(int type, int i) {
        for (int j = 0; j < i; j++) {
            driverTools.clickWithJavascriptExecutor(getPassengerIncrementLocator(type));
        }
    }

    private Locator getPassengerIncrementLocator(int type) {
        return new Locator("INCREMENT_PASSENGER_INPUT_" + type, new By.ByXPath("//*[contains(@class, 'PassengerPopup_counter')][" + type + "]//*[contains(@class, 'QuantityEditor_operation_button')][2]"));
    }

    private void clickPassengerCountDecrementLocator(int type, int i) {
        for (int j = 0; j < i; j++) {
            driverTools.clickWithJavascriptExecutor(getPassengerDecrementLocator(type));
        }
    }

    private Locator getPassengerDecrementLocator(int type) {
        return new Locator("DECREMENT_PASSENGER_INPUT_" + type, new By.ByXPath("//*[contains(@class, 'PassengerPopup_counter')][" + type + "]//*[contains(@class, 'QuantityEditor_operation_button')][1]"));
    }

    public void savePassenger() {
        driverTools.clickWithJavascriptExecutor(PASSENGER_SAVE_BUTTON);
    }

    public void searchFerry() {
        driverTools.waitForElementToAppear(SEARCH_BUTTON, 5).ifPresent(driverTools::clickWithJavascriptExecutor);
        driverTools.waitForElementToAppear(COACH_MARK_BUTTON, 5).ifPresent(driverTools::clickWithJavascriptExecutor);
    }
}