package com.tiket.SmartUI.test;

import com.tiket.SmartUI.SmartUtils;
import com.tiket.SmartUI.page.flight.FlightSmartPage;
import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.apiPage.FlightApi;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Session;
import com.tiket.page.flight.FlightPage;
import com.tiket.page.flight.bookingform.FlightBookingFormPage;
import com.tiket.page.flight.prp.FlightProductReviewPage;
import com.tiket.page.flight.searchpage.FlightSearchPage;
import com.tiket.page.flight.searchresultpage.FlightSearchResultPage;
import com.tiket.page.home.HomePage;
import com.tiket.page.login.LoginPage;
import com.tiket.pojo.flight.FlightDynamicData;
import com.tiket.testbase.BaseTest;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import java.util.Map;

import static com.tiket.testdata.TestData.getParam;

public class FlightSmartTest extends BaseTest {
    @Author(names = {"Naufal"})
    @Module(name = "FlightGuestSmart")
    @Tribe(name = "Flight")
    @Vertical(name = "Core")
    @Test
    public void testFlightSmartGuest(){
        Map<String, String> testProperties = getParam("testFlightSmartGuest");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String tripType = testProperties.get("tripType");
        String originAirport = testProperties.get("originAirport");
        String destinationAirport = testProperties.get("destinationAirport");
        int daysToDepartureDate = Integer.parseInt(testProperties.get("daysToDepartureDate"));
        int adultPassengers = Integer.parseInt(testProperties.get("adult"));
        int childPassengers = Integer.parseInt(testProperties.get("child"));
        int infantPassengers = Integer.parseInt(testProperties.get("infant"));
        String airline = testProperties.get("airline");
        String email = testProperties.get("email");
        String phoneNumber = testProperties.get("phoneNumber");
        String currency = testProperties.get("currency");
        int baggageAmount = Integer.parseInt(testProperties.get("baggage"));
        int duration = Integer.parseInt(testProperties.get("duration"));

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightProductReviewPage<WebDriver> flightProductReviewPage = FlightProductReviewPage.getInstance(session);
        FlightSmartPage<WebDriver> flightSmartPage = FlightSmartPage.getInstance(session);
        FlightBookingFormPage<WebDriver> flightBookingFormPage = FlightBookingFormPage.getInstance(session);
        FlightApi<?> flightApi = new FlightApi<>(session);

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Find dynamic data");
        FlightDynamicData flightDynamicData = flightApi.findDateRT(
                "","",
                originAirport,destinationAirport,daysToDepartureDate,0,
                1,0,0,
                tripType,airline,airline, "", "","","",currency
        );
        String departureDate = CoreFunctions.convertToLongDate(flightDynamicData.departureDate);
        String returnDate = CoreFunctions.convertToLongDate(flightDynamicData.returnDate);
        airline = flightDynamicData.airlineDepart;

        step("Open app as guest");
        loginPage.loginAsGuest();

        step("Change currency to 'IDR'");
        homePage.changeCurrency("IDR", "");
        String language = homePage.getLanguageApp();

        step("Open flights page");
        homePage.openVerticalPageDeeplink("Flights", language, currency);
        flightSearchPage.closePopup();

        step("change date using deeplink - roundtrip");
        flightSearchPage.changeDateViaDeeplink(flightDynamicData, "roundtrip");

        step("Toggle trip type - roundtrip");
        flightSearchPage.toggleTriptype("roundtrip");
        SmartUtils.takeSmartScreenshot(session.driver(), "Flight_[Guest] Toggle Roundtrip");

        step("Verify roundtrip calendar");
        flightSmartPage.openAndSetCalendar(departureDate, returnDate);
        SmartUtils.takeSmartScreenshot(session.driver(), "Flight_[Guest] Verify Calendar - roundtrip");
        flightSmartPage.closeCalendar();

        step("Toggle trip type - oneway");
        flightSearchPage.toggleTriptype(tripType);
        SmartUtils.takeSmartScreenshot(session.driver(), "Flight_[Guest] Toggle Oneway");

        step("Verify oneway calendar");
        flightSmartPage.openAndSetCalendar(departureDate, null);
        SmartUtils.takeSmartScreenshot(session.driver(), "Flight_[Guest] Verify Calendar - oneway");
        flightSmartPage.closeCalendar();

        step("Select origin airport");
        flightSearchPage.selectOrigin(originAirport);
        flightSearchPage.closePopup();

        step("Select destination airport");
        flightSearchPage.selectDestination(destinationAirport);

        step("Select date");
        flightSearchPage.selectDate(departureDate);

        step("Search flight");
        flightSearchPage.searchFlight();

        step("Click change date");
        flightSmartPage.pressChangeDateButton();
        SmartUtils.takeSmartScreenshot(session.driver(), "Flight_[Guest] Verify Transparent App - Open Change Date Bottomsheet on SRP");

        step("Close bottomsheet");
        flightSmartPage.closeBottomSheet();

        step("Open filter");
        flightSearchResultPage.openFilter();

        step("Filter airline");
        flightSearchResultPage.filterAirline(airline);

        step("Save filter");
        flightSearchResultPage.saveFilter();
        SmartUtils.takeSmartScreenshot(session.driver(), "Flight_[Guest] After Filter");

        step("Click  Flight_detail chevron");
        flightSearchResultPage.selectFlight();
        SmartUtils.takeSmartScreenshot(session.driver(), "Flight_[Guest] Flight PRP");

        step("Continue to booking form");
        flightProductReviewPage.continueToBookingForm(tripType);
        SmartUtils.takeSmartScreenshot(session.driver(), "Flight_[Guest] Verify banner on booking page (Non login) - [Login] Banner");

        step("Skip full protection if visible");
        flightBookingFormPage.skipFullProtectionPrp();

        step("Fill contact detail");
        flightBookingFormPage.fillContactDetail(phoneNumber,email,"guest",airline);

        step("Fill passenger detail");
        flightBookingFormPage.fillPassengerDetail(adultPassengers,childPassengers,infantPassengers,"international",airline);

        step("Continue to second half booking form");
        flightBookingFormPage.continueToSecondHalf();

        step("Add baggage");
        SmartUtils.takeSmartScreenshot(session.driver(),"Flight_[Guest] Verify card view on travel add ons for international  Flight_- Baggage card");
        baggageAmount = flightBookingFormPage.addBaggage();
        flightBookingFormPage.getBaggagePrice(baggageAmount);
        SmartUtils.takeSmartScreenshot(session.driver(),"Flight_[Guest] Verify card view on travel add ons for international  Flight_- Add Baggage");
        flightBookingFormPage.saveAddBaggage();
        SmartUtils.takeSmartScreenshot(session.driver(),"Flight_[Guest] Verify card view on travel add ons for international  Flight_- Finished Adding Baggage");

        step("Add Card Compound");
        flightSmartPage.addCardCompound();
        SmartUtils.takeSmartScreenshot(session.driver(), "Flight_[Guest] Verify card compound  in extra protection - Add");

        step("Cancel Card Compound");
        flightSmartPage.cancelCardCompound();
        SmartUtils.takeSmartScreenshot(session.driver(), "Flight_[Guest] Verify card compound  in extra protection - Cancel");

        step("Press back button on Booking Form");
        flightSmartPage.pressBackButtonBookingForm();
        SmartUtils.takeSmartScreenshot(session.driver(), "Flight_[Guest] Verify Transparent App - Press Back Button on booking form");

        step("Close Dialog");
        flightSmartPage.checkOtherFlights();
        SmartUtils.takeSmartScreenshot(session.driver(), "Flight_[Guest] Verify Transparent App - SRP after back from Booking Form");

        step("Press back button on SRP");
        flightSmartPage.pressBackButtonSRP();
        SmartUtils.takeSmartScreenshot(session.driver(), "Flight_[Guest] Verify Transparent App - LP after back from SRP");
    }

    @Author(names = {"Naufal"})
    @Module(name = "FlightGuestSmart")
    @Tribe(name = "Flight")
    @Vertical(name = "Core")
    @Test
    public void testFlightSmartLogin(){
        Map<String, String> testProperties = getParam("testFlightSmartLogin");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String tripType = testProperties.get("tripType");
        String originAirport = testProperties.get("originAirport");
        String destinationAirport = testProperties.get("destinationAirport");
        int daysToDepartureDate = Integer.parseInt(testProperties.get("daysToDepartureDate"));
        int adultPassengers = Integer.parseInt(testProperties.get("adult"));
        int childPassengers = Integer.parseInt(testProperties.get("child"));
        int infantPassengers = Integer.parseInt(testProperties.get("infant"));
        String airline = testProperties.get("airline");
        String email = testProperties.get("email");
        String phoneNumber = testProperties.get("phoneNumber");
        String currency = testProperties.get("currency");
        int baggageAmount = Integer.parseInt(testProperties.get("baggage"));
        int duration = Integer.parseInt(testProperties.get("duration"));

        String loginType = testProperties.get("loginType");
        String username = testProperties.get("username");
        String password = testProperties.get("password");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightPage<WebDriver> flightPage = FlightPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightProductReviewPage<WebDriver> flightProductReviewPage = FlightProductReviewPage.getInstance(session);
        FlightSmartPage<WebDriver> flightSmartPage = FlightSmartPage.getInstance(session);
        FlightBookingFormPage<WebDriver> flightBookingFormPage = FlightBookingFormPage.getInstance(session);
        FlightApi<?> flightApi = new FlightApi<>(session);

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Find dynamic data");
        FlightDynamicData flightDynamicData = flightApi.findDateRT(
                username,password,
                originAirport,destinationAirport,daysToDepartureDate,duration,
                adultPassengers,childPassengers,infantPassengers,
                tripType,airline,airline,"","","","",currency
        );
        String departureDate = CoreFunctions.convertToLongDate(flightDynamicData.departureDate);
        String returnDate = CoreFunctions.convertToLongDate(flightDynamicData.returnDate);
        airline = flightDynamicData.airlineDepart;

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Change currency to 'IDR'");
        homePage.changeCurrency("IDR", "");
        String language = homePage.getLanguageApp();

        step("Open flights page");
        homePage.openVerticalPageDeeplink("Flights", language, currency);
        flightSearchPage.closePopup();

        step("change date using deeplink - roundtrip");
        flightSearchPage.changeDateViaDeeplink(flightDynamicData, "roundtrip");

        step("Toggle trip type - roundtrip");
        flightSearchPage.toggleTriptype("roundtrip");
        SmartUtils.takeSmartScreenshot(session.driver(), "Flight_[Guest] Toggle Roundtrip");

        step("Verify roundtrip calendar");
        flightSmartPage.openAndSetCalendar(departureDate, returnDate);
        SmartUtils.takeSmartScreenshot(session.driver(), "Flight_[Guest] Verify Calendar - roundtrip");
        flightSmartPage.closeCalendar();

        step("Toggle trip type - oneway");
        flightSearchPage.toggleTriptype(tripType);
        SmartUtils.takeSmartScreenshot(session.driver(), "Flight_[Guest] Toggle Oneway");

        step("Verify oneway calendar");
        flightSmartPage.openAndSetCalendar(departureDate, null);
        SmartUtils.takeSmartScreenshot(session.driver(), "Flight_[Guest] Verify Calendar - oneway");
        flightSmartPage.closeCalendar();

        step("Select origin airport");
        flightSearchPage.selectOrigin(originAirport);
        flightSearchPage.closePopup();

        step("Select destination airport");
        flightSearchPage.selectDestination(destinationAirport);

        step("Select date");
        flightSearchPage.selectDate(departureDate);

        step("Search flight");
        flightSearchPage.searchFlight();

        step("Click change date");
        flightSmartPage.pressChangeDateButton();
        SmartUtils.takeSmartScreenshot(session.driver(), "Flight_[Guest] Verify Transparent App - Open Change Date Bottomsheet on SRP");

        step("Close bottomsheet");
        flightSmartPage.closeBottomSheet();

        step("Open filter");
        flightSearchResultPage.openFilter();

        step("Filter airline");
        flightSearchResultPage.filterAirline(airline);

        step("Save filter");
        flightSearchResultPage.saveFilter();
        SmartUtils.takeSmartScreenshot(session.driver(), "Flight_[Guest] After Filter");

        step("Click  Flight_detail chevron");
        flightSearchResultPage.selectFlight();
        SmartUtils.takeSmartScreenshot(session.driver(), "Flight_[Guest] Flight PRP");

        step("Continue to booking form");
        flightProductReviewPage.continueToBookingForm(tripType);
        SmartUtils.takeSmartScreenshot(session.driver(), "Flight_[Guest] Verify banner on booking page (Non login) - [Login] Banner");

        step("Skip full protection if visible");
        flightBookingFormPage.skipFullProtectionPrp();

        step("Fill passenger detail");
        flightBookingFormPage.fillPassengerDetail(adultPassengers,childPassengers,infantPassengers,"international",airline);

        step("Continue to second half booking form");
        flightBookingFormPage.continueToSecondHalf();

        step("Add baggage");
        SmartUtils.takeSmartScreenshot(session.driver(),"Flight_[Guest] Verify card view on travel add ons for international  Flight_- Baggage card");
        baggageAmount = flightBookingFormPage.addBaggage();
        SmartUtils.takeSmartScreenshot(session.driver(),"Flight_[Guest] Verify card view on travel add ons for international  Flight_- Add Baggage");
        flightBookingFormPage.getBaggagePrice(baggageAmount);
        flightBookingFormPage.saveAddBaggage();
        SmartUtils.takeSmartScreenshot(session.driver(),"Flight_[Guest] Verify card view on travel add ons for international  Flight_- Finished Adding Baggage");

        step("Add Card Compound");
        flightSmartPage.addCardCompound();
        SmartUtils.takeSmartScreenshot(session.driver(), "Flight_[Guest] Verify card compound  in extra protection - Add");

        step("Cancel Card Compound");
        flightSmartPage.cancelCardCompound();
        SmartUtils.takeSmartScreenshot(session.driver(), "Flight_[Guest] Verify card compound  in extra protection - Cancel");

        step("Press back button on Booking Form");
        flightSmartPage.pressBackButtonBookingForm();
        SmartUtils.takeSmartScreenshot(session.driver(), "Flight_[Guest] Verify Transparent App - Press Back Button on booking form");

        step("Close Dialog");
        flightSmartPage.checkOtherFlights();
        SmartUtils.takeSmartScreenshot(session.driver(), "Flight_[Guest] Verify Transparent App - SRP after back from Booking Form");

        step("Press back button on SRP");
        flightSmartPage.pressBackButtonSRP();
        SmartUtils.takeSmartScreenshot(session.driver(), "Flight_[Guest] Verify Transparent App - LP after back from SRP");
    }
}
