package com.tiket.web.test.prodmonitoring;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.web.WebTest;
import com.tiket.web.page.common.home.HomePageWeb;
import com.tiket.web.page.common.home.LoginPageWeb;
import com.tiket.web.page.flight.bookingForm.FlightBookingFormPageWeb;
import com.tiket.web.page.flight.landingpage.FlightLandingPageWeb;
import com.tiket.web.page.flight.productReview.FlightProductReviewPageWeb;
import com.tiket.web.page.flight.searchresultpage.FlightSearchResultPageWeb;
import com.tiket.web.page.platform.payment.PaymentPageWeb;
import org.testng.annotations.Test;

import javax.security.auth.login.FailedLoginException;
import java.util.ArrayList;
import java.util.Map;

import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.Matchers.*;

public class FlightTest extends WebTest {

    @Author(names = "Harshita")
    @Module(name = "Booking")
    @Tribe(name = "Flight")
    @Vertical(name = "Travel")
    @Test
    public void testDomesticOnewayDirectLoginProdMonitoringFlightWeb() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testDomesticOnewayDirectLoginProdMonitoringFlightWeb");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPage = FlightLandingPageWeb.getInstance(session);
        FlightSearchResultPageWeb<?> searchResultPage = FlightSearchResultPageWeb.getInstance(session);
        FlightProductReviewPageWeb<?> productReviewPage = FlightProductReviewPageWeb.getInstance(session);
        FlightBookingFormPageWeb<?> bookingFormPage = FlightBookingFormPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPageWeb = PaymentPageWeb.getInstance(session);

        String origin = param.get("origin");
        String destination = param.get("destination");
        int adultPassengers = Integer.parseInt(param.get("adultCount"));
        int childPassengers = Integer.parseInt(param.get("childCount"));
        int infantPassengers = Integer.parseInt(param.get("infantCount"));
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);
        String username = param.get("username");
        String password = param.get("password");
        ArrayList<String> airline = CoreFunctions.makeAirlineArrayList(param.get("airline"));

        step("User open Home Page");
        homePage.goToHomePage();

        step("Login page");
        loginPage.loginIfNotWithSameEmail(username, password, "123456", 1);

        step("Home page");
        homePage.openVerticalPageViaURL("Flights");

        step("Landing page");
        Assertion.assertThat("Flight Landing Page is Opened", flightLandingPage.isOnFlightPage(), is(true));
        flightLandingPage.toggleTripType("OneWay");
        flightLandingPage.selectOrigin(origin);
        bookingFormPage.clickOkayGotIt();
        flightLandingPage.selectDestination(destination);
        bookingFormPage.clickOkayGotIt();
        flightLandingPage.selectDate(departureDate);
        flightLandingPage.addPassenger(adultPassengers, childPassengers, infantPassengers);
        flightLandingPage.searchFlight();

        step("Search Result Page");
        bookingFormPage.clickOkayGotIt();
        Assertion.assertThat("Search Result Page is loaded", searchResultPage.isSearchResultPagePresent(), is(true));
        searchResultPage.filterAirline(airline);
        String selectedAirline = searchResultPage.selectFlightByAirlineAndGetName(airline);

        step("Product Review Page");
        Assertion.assertThat("Product Review page is visible", productReviewPage.isPresent(), is(true));
        productReviewPage.clickSelectBtnRegular();
        bookingFormPage.clickNoThanksOnFlightMayReschedulePopupPRP();

        step("Booking page");
        bookingFormPage.waitForBookingPageToLoad();
        Assertion.assertThat("Booking Form page is visible", bookingFormPage.isBookingFormVisible(), is(true));
        bookingFormPage.fillPassengerDetailsLogin(adultPassengers, childPassengers, infantPassengers, "domestic", selectedAirline);
        bookingFormPage.goToPaymentPageFromBookingFormPage();
        bookingFormPage.clickItsCorrectOnReviewPassengerDataPopup();
        bookingFormPage.clickNoThanksOnFlightMayReschedulePopup();

        step("Payment page");
        Assertion.assertThat("PaymentPageLoaded", paymentPageWeb.isPaymentPagepresent(), is(true));
        String orderID = paymentPageWeb.getOrderId();
        Assertion.assertThat("user get order id", orderID, allOf(
                not(nullValue()),
                not(equalTo(""))
        ));
    }
}
