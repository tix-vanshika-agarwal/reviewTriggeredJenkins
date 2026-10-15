package com.tiket.web.dashboard.page.hotel.extranet;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class BookingPage<T extends WebDriver> extends BasePage<T> {
    public BookingPage(Session<T> session) {
            super(session);
        }

    private static final Locator BOOKING_PAGE_CONTAINER = new Locator("BOOKING_PAGE_CONTAINER", new By.ByXPath("//div[@class='the-search-bookings']//label[text()='Booking List' or text()='Daftar Pemesanan']"));
    private static final Locator DATE_TYPE_DROPDOWN = new Locator("DATE_TYPE_DROPDOWN", new By.ByXPath("//div[@id='dateType']//input[@name='dateType']"));
    private static final Locator BOOKING_DATE_OPTION = new Locator("BOOKING_DATE_OPTION", new By.ByXPath("//div[@id='dateType']//span[contains(@class,'popoverlist-list')]//label[normalize-space()='Booking']"));
    private static final Locator QUERY_INPUT = new Locator("QUERY_INPUT", new By.ByXPath("//div[@id='query']//input[@name='query']"));
    private static final Locator SEARCH_BUTTON = new Locator("SEARCH_BUTTON", new By.ByXPath("//button[text()='Search' or text()='Cari']"));
    private static final Locator FIRST_ROW_ITIN_ID = new Locator("FIRST_ROW_ITIN_ID", new By.ByXPath("//tr[@id='row-0']//div[@class='itin-id']"));
    private static final Locator FIRST_ROW_GUEST_NAME = new Locator("FIRST_ROW_GUEST_NAME", new By.ByXPath("//tr[@id='row-0']/td[3]"));
    private static final Locator FIRST_ROW_CHECK_IN_DATE = new Locator("FIRST_ROW_CHECK_IN_DATE", new By.ByXPath("//tr[@id='row-0']/td[6]"));
    private static final Locator FIRST_ROW_STATUS = new Locator("FIRST_ROW_STATUS", new By.ByXPath("//tr[@id='row-0']//div[@class='booking-status']"));

    public boolean isBookingListPageDisplayed() {
        return driverTools.waitForElementToAppear(BOOKING_PAGE_CONTAINER, 5).isPresent();
    }

    public void selectBookingDateType() {
        driverTools.waitForElementToAppear(DATE_TYPE_DROPDOWN);
        driverTools.click(DATE_TYPE_DROPDOWN);
        WebElement bookingOption = driverTools.waitForElementToBeClickable(BOOKING_DATE_OPTION)
                .orElseThrow(() -> new AssertionError("Dropdown option 'booking' was not clickable."));
        driverTools.click(bookingOption);
        WebElement dropdownInput = driverTools.waitForElementToAppear(DATE_TYPE_DROPDOWN)
                .orElseThrow(() -> new AssertionError("Dropdown input field could not be located for validation."));
        String actualSelectedValue = dropdownInput.getAttribute("value");
        Assertion.assertThat("Verify 'booking' date type is successfully selected", actualSelectedValue, equalTo("Booking"));
    }

    public void searchAndVerifyBookingRecord(String itineraryId, String guestFullName, String startDate) {
        WebElement queryInput = driverTools.waitForElementToAppear(QUERY_INPUT)
                .orElseThrow(() -> new AssertionError("Itinerary search input box could not be located."));
        queryInput.clear();
        queryInput.sendKeys(itineraryId);

        driverTools.waitForElementToAppear(SEARCH_BUTTON);
        driverTools.click(SEARCH_BUTTON);

        String actualItinId = driverTools.waitForElementToAppear(FIRST_ROW_ITIN_ID)
                .orElseThrow(() -> new AssertionError("Search results did not yield data entries within timeout."))
                .getText().trim();
        String actualGuestName = driverTools.waitForElementToAppear(FIRST_ROW_GUEST_NAME).orElseThrow().getText().trim();
        String actualCheckIn = driverTools.waitForElementToAppear(FIRST_ROW_CHECK_IN_DATE).orElseThrow().getText().trim();
        String actualStatus = driverTools.waitForElementToAppear(FIRST_ROW_STATUS).orElseThrow().getText().trim();

        Assertion.assertThat("Verify itinerary ID in Extranet matches Book API Expected = "+itineraryId+" Actual = "+actualItinId, actualItinId, equalTo(itineraryId));
        Assertion.assertThat("Verify guest name in Extranet matches registration entry Expected = "+guestFullName.toLowerCase().trim()+ " Actual = "+actualGuestName.toLowerCase(), actualGuestName.toLowerCase(), equalTo(guestFullName.toLowerCase().trim()));
        Assertion.assertThat("Verify booking status is issued, Status = "+actualStatus.toLowerCase(), actualStatus.toLowerCase(), equalTo("issued"));

        String[] splitDates = startDate.split("-");
        String shortYear   = splitDates[0].substring(2);
        String monthNumber = splitDates[1];
        String dayNumber   = splitDates[2];

        String[] monthVariants = switch (monthNumber) {
            case "01" -> new String[]{"Jan"};
            case "02" -> new String[]{"Feb"};
            case "03" -> new String[]{"Mar"};
            case "04" -> new String[]{"Apr"};
            case "05" -> new String[]{"May", "Mei"};
            case "06" -> new String[]{"Jun"};
            case "07" -> new String[]{"Jul"};
            case "08" -> new String[]{"Aug", "Agt"};
            case "09" -> new String[]{"Sep"};
            case "10" -> new String[]{"Oct", "Okt"};
            case "11" -> new String[]{"Nov"};
            case "12" -> new String[]{"Dec", "Des"};
            default -> throw new IllegalArgumentException("Invalid month parsed from start date: " + monthNumber);
        };

        boolean isDateVerified = false;
        List<String> expectedOptions = new ArrayList<>();
        for (String monthAbbr : monthVariants) {
            String expectedDateSnippet = dayNumber + " " + monthAbbr + ", " + shortYear;
            expectedOptions.add(expectedDateSnippet);
            if (actualCheckIn.contains(expectedDateSnippet)) {
                Assertion.assertThat("CheckIn Date must match with Date at booking page Expected = "+expectedDateSnippet+" Actual = "+actualCheckIn, actualCheckIn, containsString(expectedDateSnippet));
                isDateVerified = true;
                break;
            }
        }
        Assertion.assertThat("Verify Check-In Date | Expected Options: " + expectedOptions + " | Actual UI Value: [" + actualCheckIn + "]", isDateVerified, equalTo(true));
    }
}
