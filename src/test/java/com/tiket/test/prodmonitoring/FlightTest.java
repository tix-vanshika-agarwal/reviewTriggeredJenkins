package com.tiket.test.prodmonitoring;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.apiPage.FlightApi;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Session;
import com.tiket.page.flight.bookingform.FlightBookingFormPage;
import com.tiket.page.flight.prp.FlightProductReviewPage;
import com.tiket.page.flight.searchpage.FlightSearchPage;
import com.tiket.page.flight.searchresultpage.FlightSearchResultPage;
import com.tiket.page.home.HomePage;
import com.tiket.page.login.LoginPage;
import com.tiket.page.payment.PaymentPage;
import com.tiket.pojo.flight.FlightDynamicData;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import static org.hamcrest.Matchers.*;

import java.util.ArrayList;
import java.util.Map;

import static com.tiket.testdata.TestData.getParam;

public class FlightTest extends BaseTest {

    @Author(names = {"Ashish"})
    @Module(name = "FlightDomesticOneway")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void testDomesticOnewayDirectLoginProdMonitoringFlight() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testDomesticOnewayDirectLoginProdMonitoringFlight");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightProductReviewPage<WebDriver> flightProductReviewPage = FlightProductReviewPage.getInstance(session);
        FlightBookingFormPage<WebDriver> flightBookingFormPage = FlightBookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);

        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        ArrayList<String> airlines = CoreFunctions.makeAirlineArrayList(param.get("airlines"));

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String currency = param.get("currency");
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, CoreFunctions.generateRandomInt(daysToDepartureDate,daysToDepartureDate+10));

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Login page");
        loginPage.loginWith(loginType, username, password);

        step("Home page");
        homePage.changeCurrency(currency,username);
        homePage.openVerticalPageDeeplink("Flights","",currency);

        step("Landing page");
        flightSearchPage.closePopup();
        flightSearchPage.changeDateViaDeeplink(daysToDepartureDate,tripType);
        flightSearchPage.toggleTriptype(tripType);
        flightSearchPage.selectOrigin(originAirport);
        flightSearchPage.closePopup();
        flightSearchPage.selectDestination(destinationAirport);
        flightSearchPage.selectDate(departureDate);
        flightSearchPage.addPassenger(adultPassengers,childPassengers,infantPassengers);
        flightSearchPage.searchFlight();

        step("Search result page");
        flightSearchResultPage.openFilter();
        String airlineSelected = flightSearchResultPage.filterFirstFromListAirline(airlines);
        flightSearchResultPage.saveFilter();
        flightSearchResultPage.selectFlight();

        step("Product review page");
        flightProductReviewPage.continueToBookingForm(tripType);
        flightBookingFormPage.skipFullProtectionPrp();

        step("Booking form page");
        flightBookingFormPage.fillPassengerDetail(adultPassengers,childPassengers,infantPassengers, "domestic",airlineSelected);
        flightBookingFormPage.continueToSecondHalf();
        flightBookingFormPage.continueToPayment();
        flightBookingFormPage.otherOrderStillWaitingPopUp();
        flightBookingFormPage.skipFullProtection();
        flightBookingFormPage.baggageAllowancePopup();

        step("Payment page");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id", orderID, allOf(
                not(nullValue()),
                not(equalTo(""))
        ));
    }
}
