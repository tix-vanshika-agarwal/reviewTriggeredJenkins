package com.tiket.web.page.platform.eiffel.membership;

import com.tiket.core.CoreFunctions;
import com.tiket.dbclient.TiketMysqlDBManager;
import com.tiket.model.Locator;
import com.tiket.model.Platform;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import com.tiket.web.page.accommodation.hotels.BookingForm.HotelBookingFormPageWeb;
import com.tiket.web.dWeb.page.accommodation.hotels.HotelDetailPage;
import com.tiket.web.page.flight.bookingForm.FlightBookingFormPageWeb;
import com.tiket.web.page.accommodation.hotels.hotelLandingPage.HotelLandingPageWeb;
import com.tiket.web.page.flight.searchresultpage.FlightSearchResultPageWeb;
import com.tiket.web.page.flight.landingpage.FlightLandingPageWeb;
import com.tiket.web.page.common.home.HomePageWeb;
import com.tiket.web.page.nft.car_rental.bookingForm_Page.CarRentalBookingPageWeb;
import com.tiket.web.page.nft.car_rental.searchResult_Page.CarRentalSearchResultPageWeb;
import com.tiket.web.page.nft.car_rental.search_Page.CarRentalSearchPageWeb;
import com.tiket.web.page.ttd.TtdBookingFormPage.TtdBookingFormPageWeb;
import com.tiket.web.page.ttd.TtdLandingPage.TtdLandingPageWeb;
import com.tiket.web.page.ttd.TtdProductDetailPage.TtdProductDetailPageWeb;
import lombok.Getter;
import lombok.Setter;
import org.openqa.selenium.*;
import org.testng.SkipException;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;


import javax.sql.rowset.CachedRowSet;

public abstract class GuestBookingWeb<T extends WebDriver> extends WebPage<T> {
    public GuestBookingWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> GuestBookingWeb<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new GuestBookingDWeb<>(session);
            }
            case AMWEB, IMWEB -> {
                return new GuestBookingMWeb<>(session);
            }
            default -> throw new UnsupportedOperationException("Platform not supported");
        }
    }

    private String vertical;
    private static final Locator LOADING_TO_PAYMENT = new Locator("LOADING_TO_PAYMENT", By.xpath("//div[contains(@class,'LoadingWithIllustration_content')]"));
    private static final Locator NEW_ORDER_SUMMARY_SECTION = new Locator("NEW_ORDER_SUMMARY_SECTION", By.xpath("//div[contains(@class,'NewOrderSummarySection_container')]"));

    public void goToVerticalPage() {
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        this.vertical = randomVertical(this.verticals);
        logger.log("Going to vertical page: " + vertical);
        hideGoogleOneTap();
        homePage.openVerticalPageViaURL(vertical);
    }

    public void continueToBookingForm() {
        try {
            switch (vertical.toLowerCase()) {
                case "flights" -> bookFlight();
                case "hotel" -> bookHotel();
                case "event", "events" -> bookEvent();
                case "sewa-mobil" -> bookCar();
                default -> throw new UnsupportedOperationException("Vertical not supported: " + vertical);
            }
        } catch (Exception e) {
            throw new SkipException("Failed to book product for vertical: " + vertical + ". \n Error: " + e.getMessage() + "\n stacktrace: " + Arrays.toString(e.getStackTrace()));
        }
    }

    private void bookFlight() {
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, 3);
        logger.log("Departure date: " + departureDate);
        FlightLandingPageWeb<?> flightFlightLandingPageWeb = FlightLandingPageWeb.getInstance(session);
        flightFlightLandingPageWeb.selectOrigin(sheetData.get("flightOrigin"));
        flightFlightLandingPageWeb.selectDestination(sheetData.get("flightDestination"));
        flightFlightLandingPageWeb.selectDate(departureDate);
        flightFlightLandingPageWeb.searchFlight();

        FlightSearchResultPageWeb<?> flightSearchResultPage = FlightSearchResultPageWeb.getInstance(session);
        flightSearchResultPage.selectFlightByAirline(sheetData.get("flightAirline"));
        flightSearchResultPage.selectTicket(1);
    }

    private void bookHotel() {
        HotelLandingPageWeb<?> hotelLandingPage = HotelLandingPageWeb.getInstance(session);
        hotelLandingPage.searchStayLocation(sheetData.get("hotelLocation"));
        hotelLandingPage.setStayDate("add date", "3-1");
        hotelLandingPage.clickSubmit();

        HotelDetailPage<?> hotelDetailPage = new HotelDetailPage<>(session);
        if (!hotelDetailPage.isRoomsAvailable()) {
            Assertion.skip("No rooms available for the selected hotel");
        }
        hotelDetailPage.selectHotelRoom(Integer.parseInt(sheetData.get("hotelRoomIndex")));
    }

    private void bookEvent() {
        TtdLandingPageWeb<?> ttdLandingPage = TtdLandingPageWeb.getInstance(session);
        ttdLandingPage.inputSearchKeyword(sheetData.get("activityAttraction"));
        ttdLandingPage.selectFirstSearchResult();

        TtdProductDetailPageWeb<?> productDetailPage = TtdProductDetailPageWeb.getInstance(session);
        productDetailPage.chooseTicket(sheetData.get("packageName"));
        productDetailPage.setTicketQty(1, 0);
        productDetailPage.clickBookButton();
    }

    private void bookCar() {
        CarRentalSearchPageWeb<?> carRentalSearchPage = CarRentalSearchPageWeb.getInstance(session);
        carRentalSearchPage.clickSkipButton();
        carRentalSearchPage.selectLocationByRecommendation("Jakarta");
        carRentalSearchPage.searchCarRental();

        CarRentalSearchResultPageWeb<?> carSearchResultPage = CarRentalSearchResultPageWeb.getInstance(session);
        carSearchResultPage.selectFirstCarRentalCard();
        carSearchResultPage.proceedToBookingFormFromReviewPage();
    }

    private void createDummyContactDetails() {
        String firstName = CoreFunctions.randomNameWithoutTest();
        String lastName = CoreFunctions.randomLastName();
        String fullName = firstName + " " + lastName;
        String email = CoreFunctions.randomEmail();
        String phoneNumber = "857" + CoreFunctions.randomInt(9);
        String salutation = CoreFunctions.randomSalutation().toLowerCase();

        sheetData.put("firstName", firstName.toLowerCase());
        sheetData.put("lastName", lastName.toLowerCase());
        sheetData.put("fullName", fullName.toLowerCase());
        sheetData.put("email", email.toLowerCase());
        sheetData.put("phoneNumber", phoneNumber.toLowerCase());
        sheetData.put("salutation", salutation);
    }

    public void inputContactDetails() {
        try {
            createDummyContactDetails();
            switch (vertical.toLowerCase()) {
                case "flights" -> inputFlightContactDetails();
                case "hotel" -> inputHotelContactDetails();
                case "event", "events" -> inputEventContactDetails();
                case "sewa-mobil" -> inputCarContactDetails();
                default -> throw new UnsupportedOperationException("Vertical not supported: " + vertical);
            }
        } catch (Exception e) {
            throw new SkipException("Failed to input contact details for vertical: " + vertical + ". \n Error: " + e.getMessage() + "\n stacktrace: " + Arrays.toString(e.getStackTrace()));
        }
    }

    private void inputFlightContactDetails() {
        FlightBookingFormPageWeb<?> flightBookingFormPage = FlightBookingFormPageWeb.getInstance(session);
        flightBookingFormPage.waitForBookingPageToLoad();
        flightBookingFormPage.inputContactDetailsNonLogin(sheetData.get("salutation"), sheetData.get("fullName"), sheetData.get("phoneNumber"), sheetData.get("email"));
        flightBookingFormPage.clickSameContactToggle();
        flightBookingFormPage.goToPaymentPageFromBookingFormPage();
        driverTools.waitForElementToDisappear(LOADING_TO_PAYMENT, 60);
        flightBookingFormPage.clickNoThanksOnFlightMayReschedulePopup();
        driverTools.waitForElementToAppear(NEW_ORDER_SUMMARY_SECTION);
    }

    private void inputHotelContactDetails() {
        HotelBookingFormPageWeb<?> hotelBookingFormPage = HotelBookingFormPageWeb.getInstance(session);
        hotelBookingFormPage.fillContactDetail(sheetData.get("fullName"), sheetData.get("phoneNumber"), sheetData.get("email"));
        hotelBookingFormPage.fillGuestDetailsCombinedRandom(1);
        hotelBookingFormPage.continueToNextPage();
        if (hotelBookingFormPage.isInsuranceOrAddonPageVisible()) {
            hotelBookingFormPage.continueToPaymentPage();
            driverTools.waitForElementToDisappear(LOADING_TO_PAYMENT, 60);
        } else {
            driverTools.waitForElementToDisappear(LOADING_TO_PAYMENT, 60);
        }
        if (hotelBookingFormPage.isFullyBookedNotifPresent()) {
            Assertion.skip("Hotel is fully booked, cannot proceed with booking. Please verify manually.");
        }
    }

    private void inputEventContactDetails() {
        TtdBookingFormPageWeb<?> ttdBookingFormPage = TtdBookingFormPageWeb.getInstance(session);
        ttdBookingFormPage.fillContactDetailsNonLogin(sheetData.get("salutation"), sheetData.get("fullName"), sheetData.get("phoneNumber"), sheetData.get("email"));
        ttdBookingFormPage.continueToPaymentPage();

        waitUntilPaymentPageLoaded();
    }

    private void inputCarContactDetails() {
        CarRentalBookingPageWeb<?> carRentalBookingPage = CarRentalBookingPageWeb.getInstance(session);
        carRentalBookingPage.setPickUpLocation("Jakarta");
        carRentalBookingPage.setSetDropOffLocation("Jakarta");
        carRentalBookingPage.continueBookFormMWEB();

        carRentalBookingPage.fillContactDetail(sheetData.get("fullName"), sheetData.get("phoneNumber"), sheetData.get("email"));
        carRentalBookingPage.selectPayment();
        waitUntilPaymentPageLoaded();
    }

    public Map<String, Object> getGuestEmailDataFromDB() throws Exception {
        String query = "SELECT mabc.accountId, mpbc.accountFirstName, mpbc.accountLastName, mabc.accountUsername, mabc.guest " +
                "FROM tix_member_core.members_account_b2c as mabc " +
                "JOIN tix_profile_core.members_profile_b2c as mpbc " +
                "ON mabc.accountId = mpbc.accountId " +
                "WHERE mabc.accountUsername = '" + sheetData.get("email") + "' " +
                "AND mpbc.isPrimary = 1";
        var db = TiketMysqlDBManager.getInstance("member-2");
        return getDBDataToMap(db.executeQuery(query), 1);
    }

    public Map<String, Object> getDBDataToMap(CachedRowSet rowSet, Integer expectedTotalData) {
        Map<String, Object> resultData = new HashMap<>();
        try {
            if (!rowSet.next()) {
                throw new RuntimeException("No data found in db. Please check the query or the database connection.");
            }

            if (expectedTotalData != null && rowSet.size() > expectedTotalData) {
                throw new RuntimeException("Total data size is " + rowSet.size() + ", but expected size is " + expectedTotalData);
            }

            ResultSetMetaData metaData = rowSet.getMetaData();
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                resultData.put(metaData.getColumnName(i), rowSet.getObject(i));
            }
        } catch (SQLException e) {
            logger.log("SQL Execution Error: " + e.getMessage() + "\n" + e);
            throw new RuntimeException("SQL Execution Error: " + e.getMessage(), e);
        }
        return resultData;
    }

    private String randomVertical(String[] verticals) {
        int[] verticalWeights = sheetData.get("verticalWeights") != null ? Arrays.stream(sheetData.get("verticalWeights").split("#")).mapToInt(Integer::parseInt).toArray() : null;
        if (verticalWeights != null && verticalWeights.length > 0) {
            int totalWeight = Arrays.stream(verticalWeights).sum();
            int randomWeight = (int) (Math.random() * totalWeight);
            for (int i = 0; i < verticalWeights.length; i++) {
                if (randomWeight < verticalWeights[i]) {
                    return verticals[i];
                }
                randomWeight -= verticalWeights[i];
            }
        }
        if (verticals == null || verticals.length == 0) {
            throw new IllegalStateException("Verticals array is not set or empty");
        }
        return verticals[0];
    }

    private void hideGoogleOneTap() {
        if (Configs.PLATFORM == Platform.AMWEB || Configs.PLATFORM == Platform.IMWEB) {
            try {
                JavascriptExecutor js = (JavascriptExecutor) driver;
                js.executeScript("document.querySelector('iframe[src*=\"accounts.google.com\"]').style.display='none';");
            } catch (Exception ignored) {
            }

        }
    }

    private void waitUntilPaymentPageLoaded() {
        driverTools.waitForElementToAppear(LOADING_TO_PAYMENT, 5);
        if (driverTools.isElementDisplayed(LOADING_TO_PAYMENT)) {
            driverTools.waitForElementToDisappear(LOADING_TO_PAYMENT, 30);
        }
        driverTools.waitForElementToAppear(NEW_ORDER_SUMMARY_SECTION, 5);
    }

    @Setter
    String[] verticals = null;
    @Setter
    @Getter
    Map<String, String> sheetData = new HashMap<>();
}
