package com.tiket.web.test.flight;

import com.tiket.apiPage.FlightApi;
import com.tiket.apiPage.FlightIssuedOrderFactory;
import com.tiket.apiPage.Unm;
import com.tiket.model.Environment;
import com.tiket.model.Platform;
import com.tiket.page.login.LoginPage;
import com.tiket.pojo.flight.FlightDynamicData;
import com.tiket.pojo.member.RegisterNewAccountData;
import com.tiket.testbase.Configs;
import com.tiket.web.dWeb.page.flight.FlightPage;
import com.tiket.pojo.common.InsuranceGeneralData;
import com.tiket.pojo.flight.FlightOrderDetailResponse;
import com.tiket.pojo.flight.FlightPaxDetail;
import com.tiket.pojo.flight.PriceDetail;
import com.tiket.testbase.BaseTest;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.web.WebTest;
import com.tiket.web.dWeb.page.flight.FlightProductReviewPage;
import com.tiket.web.dWeb.page.payment.PaymentPage;
import com.tiket.web.page.common.calendar.CalendarPageWeb;
import com.tiket.web.page.common.home.HomePageWeb;
import com.tiket.web.page.common.home.InfoPageWeb;
import com.tiket.web.page.common.home.LoginPageWeb;
import com.tiket.web.page.corporate.travel_plan.TravelPlanWeb;
import com.tiket.web.page.flight.accountcenter.AccountCenterPageWeb;
import com.tiket.web.page.flight.bookingForm.FlightBookingFormPageWeb;
import com.tiket.web.page.flight.descriptionPage.FlightDescriptionPageWeb;
import com.tiket.web.page.flight.flightStatusPage.FlightStatusPageWeb;
import com.tiket.web.page.flight.landingpage.FlightLandingPageWeb;
import com.tiket.web.page.flight.productReview.FlightProductReviewPageWeb;
import com.tiket.web.page.flight.reschedule.FlightReschedulePageWeb;
import com.tiket.web.page.flight.rescheduleReview.FlightRescheduleReviewPageWeb;
import com.tiket.web.page.flight.rescheduleSearchPage.FlightRescheduleSearchPageWeb;
import com.tiket.web.page.flight.searchresultpage.FlightSearchResultPageWeb;
import com.tiket.web.page.platform.grand.PromoPageWeb;
import com.tiket.web.page.platform.payment.PaymentPageWeb;
import com.tiket.web.page.order.OrderList.OrderListWeb;
import com.tiket.web.page.order.OrderDetail.OrderDetailWeb;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.security.auth.login.FailedLoginException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import static com.tiket.core.CoreFunctions.*;
import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import com.tiket.web.page.flight.bookingForm.FlightBookingFormPageMWeb;

public class FlightTest extends WebTest {

    @BeforeMethod
    public void cleanBrowserStateBeforeTest() {
        Session<WebDriver> session = sessionThreadLocal.get();
        if (session != null) {
            WebDriver driver = session.driver();
            try {
                // 1. Close all extra tabs/windows, keep only one tab with about:blank
                if (driver.getWindowHandles().size() > 1) {
                    // Get current window handle
                    String currentHandle = driver.getWindowHandle();
                    
                    // Close all windows except the first one
                    for (String windowHandle : driver.getWindowHandles()) {
                        if (!windowHandle.equals(currentHandle)) {
                            try {
                                driver.switchTo().window(windowHandle);
                                driver.close();
                                if (mainLogger.get() != null) {
                                    mainLogger.get().log("Closed extra window/tab: " + windowHandle);
                                }
                            } catch (Exception e) {
                                if (mainLogger.get() != null) {
                                    mainLogger.get().log("Could not close window: " + e.getMessage());
                                }
                            }
                        }
                    }
                    
                    // Switch back to the remaining window
                    driver.switchTo().window(currentHandle);
                }
                
                // 2. Finally navigate to about:blank for clean slate
                driver.navigate().to("about:blank");
                
                if (mainLogger.get() != null) {
                    mainLogger.get().log("Browser cleaned: " + (driver.getWindowHandles().size()) + " tab(s)");
                }
            } catch (Exception e) {
                if (mainLogger.get() != null) {
                    mainLogger.get().exception(e);
                }
            }
        }
    }

    @Author(names = "Rohan")
    @Module(name = "Booking")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void PelitaBookingOnewayDomesticSinglePassenger() throws InterruptedException, FailedLoginException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("PelitaBookingOnewayDomesticSinglePassenger");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPage = FlightLandingPageWeb.getInstance(session);
        FlightSearchResultPageWeb<?> flightSearchResultPageWeb = FlightSearchResultPageWeb.getInstance(session);
        FlightProductReviewPageWeb<?> flightProductReviewPageWeb = FlightProductReviewPageWeb.getInstance(session);
        FlightBookingFormPageWeb<?> flightBookingFormPageWeb = FlightBookingFormPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPageWeb = PaymentPageWeb.getInstance(session);

        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");
        String tripType = param.get("tripType");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));

        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);
        int duration = Integer.parseInt(param.get("duration"));
        String returnDate = CoreFunctions.addToLongDate(departureDate, duration);
        String username = param.get("username");
        String otp = param.get("otp");
        String passengerName = param.get("passengerName");
        String password = param.get("password");
        String airlineName = param.get("airlineName");

        step("Logging in using {" + username + "}");
        loginPage.loginIfNotWithSameEmail(username, password, otp, 1);

        step("User open Flight Landing Page");
        homePage.openVerticalPageViaURL("Flights");

        step("Toggle trip type");
        flightLandingPage.toggleTripType(tripType);

        step("Select origin airport");
        flightLandingPage.selectOrigin(originAirport);

        step("Select destination airport");
        flightLandingPage.selectDestination(destinationAirport);

        step("Select date");
        flightLandingPage.selectDate(departureDate);

        step("Toggle trip type");
        flightLandingPage.toggleTripType(tripType);

        step("search flight");
        flightLandingPage.searchFlight();

        step("filter airline {" + airlineName + " }");
        flightSearchResultPageWeb.filterAirline(airlineName);

        step("select flight for departure");
        flightSearchResultPageWeb.selectFlight();

        step("select the SELECT Button");
        flightProductReviewPageWeb.clickSelectBtnRegular();

        step("close Reschedule popUp");
        flightBookingFormPageWeb.clickNoThanksOnFlightMayReschedulePopup();

        step("enter the passenger Name");
        flightBookingFormPageWeb.fillPassengerDetails();

        step("going to paymentPage");
        flightBookingFormPageWeb.goToPaymentPageFromBookingFormPage();
        flightBookingFormPageWeb.clickNoThanksOnFlightMayReschedulePopup();

        step("click I don't want it");
        flightBookingFormPageWeb.clickOkayGotIt();

        step("Payment page should be displayed");
        Assertion.assertThat("PaymentPageLoaded", paymentPageWeb.isPaymentPagepresent(), is(true));
        String orderID = paymentPageWeb.getOrderId();
        orderID = String.valueOf(getOrderId(orderID));
        Assertion.assertThat("OrderIDVisible " + orderID, !orderID.isEmpty(), is(true));
    }

    @Author(names = {"Jacky"})
    @Module(name = "Transnusa")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void transnusaBookingOnewayDomesticSinglePassengerwithoutaddons() throws InterruptedException, FailedLoginException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("transnusaBookingOnewayDomesticSinglePassengerwithoutaddons");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPage = FlightLandingPageWeb.getInstance(session);
        FlightSearchResultPageWeb<?> searchResultPage = FlightSearchResultPageWeb.getInstance(session);
        FlightProductReviewPageWeb<?> flightProductReviewPageWeb = FlightProductReviewPageWeb.getInstance(session);
        FlightBookingFormPageWeb<?> flightBookingFormPageWeb = FlightBookingFormPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPageWeb = PaymentPageWeb.getInstance(session);

        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));

        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);
        String tripType = param.get("tripType");
        String username = param.get("username");
        String otp = param.get("otp");
        String passengerName = param.get("passengerName");
        String password = param.get("password");
        String airlineName = param.get("airlineName");
        int duration = Integer.parseInt(param.get("duration"));
        String returnDate = CoreFunctions.addToLongDate(departureDate, duration);

        step("Login as " + username + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(username, password);

        step("User open Flight Landing Page");
        homePage.openVerticalPageViaURL("Flights");
        Assertion.assertThat("Flight Landing Page is Opened", flightLandingPage.isOnFlightPage(), is(true));

        step("Toggle trip type");
        flightLandingPage.toggleTripType(tripType);

        step("Select origin airport");
        flightLandingPage.selectOrigin(originAirport);

        step("Select destination airport");
        flightLandingPage.selectDestination(destinationAirport);

        step("Select date");
        flightLandingPage.selectDate(departureDate);

        step("Toggle trip type");
        flightLandingPage.toggleTripType(tripType);

        step("search flight");
        flightLandingPage.searchFlight();
        Assertion.assertThat("Search Result Page is loaded", searchResultPage.isSearchResultPagePresent(), is(true));

        step("filter airline {" + airlineName + " }");
        searchResultPage.filterAirline(airlineName);

        step("select flight for departure");
        searchResultPage.selectFlight();

        step("select the SELECT Button");
        flightProductReviewPageWeb.clickSelectBtnRegular();

        step("close Reschedule popUp");
        flightBookingFormPageWeb.clickNoThanksOnFlightMayReschedulePopup();

        step("enter the passenger Name");
        flightBookingFormPageWeb.fillPassengerDetailsLogin(passengerName);

        step("going to paymentPage");
        flightBookingFormPageWeb.goToPaymentPageFromBookingFormPage();
        flightBookingFormPageWeb.clickNoThanksOnFlightMayReschedulePopup();

        step("click I don't want it");
        flightBookingFormPageWeb.clickOkayGotIt();

        step("Payment page should be displayed");
        Assertion.assertThat("PaymentPageLoaded", paymentPageWeb.isPaymentPagepresent(), is(true));
        String orderID = paymentPageWeb.getOrderId();
        orderID = String.valueOf(getOrderId(orderID));
        Assertion.assertThat("OrderIDVisible " + orderID, !orderID.isEmpty(), is(true));
    }

    @Author(names = "Harshita")
    @Module(name = "Booking")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void testTROneWayInternational() throws FailedLoginException, InterruptedException {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        Map<String, String> param = getParam("testTROneWayInternational");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPage = FlightLandingPageWeb.getInstance(session);
        FlightSearchResultPageWeb<?> searchResultPage = FlightSearchResultPageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        FlightProductReviewPageWeb<?> productReviewPage = FlightProductReviewPageWeb.getInstance(session);
        FlightBookingFormPageWeb<?> flightBookingFormPage = FlightBookingFormPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);


        String travelEmail = param.get("travelEmail");
        String travelerPassword = param.get("travelerPassword");
        String otp = param.get("otp");
        int i = Integer.parseInt(param.get("i"));
        String origin = param.get("origin");
        String destination = param.get("destination");
        int adultPassengers = Integer.parseInt(param.get("adultCount"));
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);
        String name = param.get("name");
        int duration = Integer.parseInt(param.get("duration"));
        String returnDate = CoreFunctions.addToLongDate(departureDate, duration);
        String tripType = param.get("tripType");
        
        step("Login as " + travelEmail + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(travelEmail, travelerPassword);

        step("User open Flight Landing Page");
        homePage.openVerticalPageViaURL("Flights");
        Assertion.assertThat("Flight Landing Page is Opened", flightLandingPage.isOnFlightPage(), is(true));

        step("Toggle trip type");
        flightLandingPage.toggleTripType(tripType);

        step("User select origin");
        flightLandingPage.selectOrigin(origin);

        step("User select destination");
        flightLandingPage.selectDestination(destination);

        step("Select date");
        flightLandingPage.selectDate(departureDate);

        step("Toggle trip type");
        flightLandingPage.toggleTripType(tripType);

        step("add passenger");
        flightLandingPage.addPassenger(adultPassengers, 0, 0);

        step("User click search button");
        flightLandingPage.searchFlight();
        Assertion.assertThat("Search Result Page is loaded", searchResultPage.isSearchResultPagePresent(), is(true));

        step("Wait for Search Result page to be fully loaded.");
        searchResultPage.isSRPLoaded();

        step("User Selects Flight Jetstar");
        searchResultPage.filterAirline("Scoot");

        step("User Selects Any Flight");
        searchResultPage.selectFlight();
        Assertion.assertThat("Product Review Page is loaded", productReviewPage.isPresent(), is(true));

        step("User Selects SELECT button in ECONOMY section");
        productReviewPage.clickSelectBtnRegular();

        step("Continue to booking form");
        productReviewPage.continueToBookingForm();
        flightBookingFormPage.clickNoThanksOnFlightMayReschedulePopup();

        step("Click toggle to fill passengers details and save ");
        flightBookingFormPage.fillPassengerDetailsLogin(name);

        step("Go to payment page");
        flightBookingFormPage.goToPaymentPageFromBookingFormPage();
        flightBookingFormPage.clickNoThanksOnFlightMayReschedulePopup();
        flightBookingFormPage.clickOkayGotIt();


        step("Payment page should be displayed");
        Assertion.assertThat("PaymentPageLoaded", paymentPage.isPaymentPagepresent(), is(true));
        String orderID = paymentPage.getOrderId();
        orderID = String.valueOf(getOrderId(orderID));
        Assertion.assertThat("OrderIDVisible " + orderID, !orderID.isEmpty(), is(true));
    }

    @Author(names = "Harshita")
    @Module(name = "Booking")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void testJetStarOneWayInternational() throws FailedLoginException, InterruptedException {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        Map<String, String> param = getParam("testJetStarOneWayInternational");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPage = FlightLandingPageWeb.getInstance(session);
        FlightSearchResultPageWeb<?> searchResultPage = FlightSearchResultPageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        FlightProductReviewPageWeb<?> productReviewPage = FlightProductReviewPageWeb.getInstance(session);
        FlightBookingFormPageWeb<?> flightBookingFormPage = FlightBookingFormPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);


        String travelEmail = param.get("travelEmail");
        String travelerPassword = param.get("travelerPassword");
        String otp = param.get("otp");
        int i = Integer.parseInt(param.get("i"));
        String origin = param.get("origin");
        String destination = param.get("destination");
        int adultPassengers = Integer.parseInt(param.get("adultCount"));
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);
        String name = param.get("name");
        int duration = Integer.parseInt(param.get("duration"));
        String returnDate = CoreFunctions.addToLongDate(departureDate, duration);
        String tripType = param.get("tripType");
        
        step("Login as " + travelEmail + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(travelEmail, travelerPassword);

        step("User open Flight Landing Page");
        homePage.openVerticalPageViaURL("Flights");
        Assertion.assertThat("Flight Landing Page is Opened", flightLandingPage.isOnFlightPage(), is(true));

        step("User select origin");
        flightLandingPage.selectOrigin(origin);
        flightBookingFormPage.clickOkayGotIt();

        step("User select destination");
        flightLandingPage.selectDestination(destination);
        flightBookingFormPage.clickOkayGotIt();

        step("Select date");
        flightLandingPage.selectDate(departureDate);

        step("Toggle trip type");
        flightLandingPage.toggleTripType(tripType);

        step("add passenger");
        flightLandingPage.addPassenger(adultPassengers, 0, 0);

        step("User click search button");
        flightLandingPage.searchFlight();
        Assertion.assertThat("Search Result Page is loaded", searchResultPage.isSearchResultPagePresent(), is(true));

        step("Wait for Search Result page to be fully loaded.");
        searchResultPage.isSRPLoaded();

        step("User Selects Flight Jetstar");
        searchResultPage.filterAirline("JETSTAR AIRWAYS");

        step("User Selects Any Flight");
        searchResultPage.selectFlight();
        Assertion.assertThat("Product Review Page is loaded", productReviewPage.isPresent(), is(true));

        step("User Selects SELECT button in ECONOMY section");
        productReviewPage.clickSelectBtnRegular();

        step("Continue to booking form");
        productReviewPage.continueToBookingForm();
        flightBookingFormPage.clickNoThanksOnFlightMayReschedulePopup();

        step("Click toggle to fill passengers details and save ");
        flightBookingFormPage.fillPassengerDetailsLogin(name);

        step("Go to payment page");
        flightBookingFormPage.goToPaymentPageFromBookingFormPage();
        flightBookingFormPage.clickNoThanksOnFlightMayReschedulePopup();
        flightBookingFormPage.clickOkayGotIt();


        step("Payment page should be displayed");
        Assertion.assertThat("PaymentPageLoaded", paymentPage.isPaymentPagepresent(), is(true));
        String orderID = paymentPage.getOrderId();
        orderID = String.valueOf(getOrderId(orderID));
        Assertion.assertThat("OrderIDVisible " + orderID, !orderID.isEmpty(), is(true));
    }

    @Author(names = "Harshita")
    @Module(name = "Booking")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void testIssuedMalindoOneWayInternational() throws FailedLoginException, InterruptedException {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        Map<String, String> param = getParam("testIssuedMalindoOneWayInternational");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPageWeb = FlightLandingPageWeb.getInstance(session);
        FlightSearchResultPageWeb<?> flightSearchResultPage = FlightSearchResultPageWeb.getInstance(session);
        FlightBookingFormPageWeb<?> flightBookingFormPage = FlightBookingFormPageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        FlightProductReviewPageWeb<?> productReviewPage = FlightProductReviewPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);

        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
        int loginAttemptCount = Integer.parseInt(param.get("loginAttemptCount"));
        String origin = param.get("origin");
        String destination = param.get("destination");
        int adultPassengers = Integer.parseInt(param.get("adultCount"));
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);
        String airlineName = param.get("airlineName");
        int duration = Integer.parseInt(param.get("duration"));
        String returnDate = CoreFunctions.addToLongDate(departureDate, duration);
        String tripType = param.get("tripType");

        step("Login as " + username + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(username, password);

        step("User open Flight Landing Page");
        homePage.openVerticalPageViaURL("Flights");
        Assertion.assertThat("Flight Landing Page is Opened", flightLandingPageWeb.isOnFlightPage(), is(true));

        step("Toggle trip type");
        flightLandingPageWeb.toggleTripType(tripType);

        step("User select origin");
        flightLandingPageWeb.selectOrigin(origin);

        step("User select destination");
        flightLandingPageWeb.selectDestination(destination);

        step("Select date");
        flightLandingPageWeb.selectDate(departureDate);

        step("Add passenger");
        flightLandingPageWeb.addPassenger(adultPassengers, 0, 0);

        step("User click search button");
        flightLandingPageWeb.searchFlight();
        Assertion.assertThat("Search Result Page is loaded", flightSearchResultPage.isSearchResultPagePresent(), is(true));

        step("Wait for Search Result page to be fully loaded.");
        flightSearchResultPage.isSRPLoaded();

        step("User Selects Flight Malindo");
        flightSearchResultPage.filterAirline(airlineName);

        step("User Selects Any Flight");
        flightSearchResultPage.selectFlight();
        Assertion.assertThat("Product Review Page is loaded", productReviewPage.isPresent(), is(true));

        step("User Selects SELECT button in ECONOMY section");
        productReviewPage.clickSelectBtnRegular();

        step("Continue to booking form");
        productReviewPage.continueToBookingForm();
        flightBookingFormPage.clickNoThanksOnFlightMayReschedulePopup();

        step("Click toggle to fill passengers details and save ");
        flightBookingFormPage.fillPassengerDetails();

        step("Go to payment page");
        flightBookingFormPage.goToPaymentPageFromBookingFormPage();
        flightBookingFormPage.clickNoThanksOnFlightMayReschedulePopup();
        flightBookingFormPage.clickOkayGotIt();

        step("Payment page should be displayed");
        Assertion.assertThat("PaymentPageLoaded", paymentPage.isPaymentPagepresent(), is(true));
        String orderID = String.valueOf(paymentPage.getOrderId());
        Assertion.assertThat("OrderIDVisible " + orderID, !orderID.isEmpty(), is(true));
    }

    @Author(names = "Harshita")
    @Module(name = "Booking")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void testIssuedAmadeusOneWayInternational() throws FailedLoginException, InterruptedException {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        Map<String, String> param = getParam("testIssuedAmadeusOneWayInternational");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPageWeb = FlightLandingPageWeb.getInstance(session);
        FlightSearchResultPageWeb<?> flightSearchResultPage = FlightSearchResultPageWeb.getInstance(session);
        FlightBookingFormPageWeb<?> flightBookingFormPage = FlightBookingFormPageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        FlightProductReviewPageWeb<?> productReviewPage = FlightProductReviewPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);

        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
        int loginAttemptCount = Integer.parseInt(param.get("loginAttemptCount"));
        String origin = param.get("origin");
        String destination = param.get("destination");
        int adultPassengers = Integer.parseInt(param.get("adultCount"));
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);
        String airlineName = param.get("airlineName");
        String tripType = param.get("tripType");

        step("Login as " + username + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(username, password);

        step("User open Flight Landing Page");
        homePage.openVerticalPageViaURL("Flights");
        Assertion.assertThat("Flight Landing Page is Opened", flightLandingPageWeb.isOnFlightPage(), is(true));

        step("Toggle trip type");
        flightLandingPageWeb.toggleTripType(tripType);

        step("User select origin");
        flightLandingPageWeb.selectOrigin(origin);

        step("User select destination");
        flightLandingPageWeb.selectDestination(destination);

        step("Select date");
        flightLandingPageWeb.selectDate(departureDate);

        step("Add passenger");
        flightLandingPageWeb.addPassenger(adultPassengers, 0, 0);

        step("User click search button");
        flightLandingPageWeb.searchFlight();
        Assertion.assertThat("Search Result Page is loaded", flightSearchResultPage.isSearchResultPagePresent(), is(true));

        step("Wait for Search Result page to be fully loaded.");
        flightSearchResultPage.isSRPLoaded();

        step("User Selects Flight Amadeus(SQ)");
        flightSearchResultPage.filterAirline(airlineName);

        step("User Selects Any Flight");
        flightSearchResultPage.selectFlight();
        Assertion.assertThat("Product Review Page is loaded", productReviewPage.isPresent(), is(true));

        step("User Selects SELECT button in ECONOMY section");
        productReviewPage.clickSelectBtnRegular();

        step("Continue to booking form");
        productReviewPage.continueToBookingForm();
        flightBookingFormPage.clickNoThanksOnFlightMayReschedulePopup();

        step("Click toggle to fill passengers details and save ");
        flightBookingFormPage.fillPassengerDetails();

        step("Go to payment page");
        flightBookingFormPage.goToPaymentPageFromBookingFormPage();
        flightBookingFormPage.clickNoThanksOnFlightMayReschedulePopup();
        flightBookingFormPage.clickOkayGotIt();

        step("Payment page should be displayed");
        Assertion.assertThat("PaymentPageLoaded", paymentPage.isPaymentPagepresent(), is(true));
        String orderID = String.valueOf(paymentPage.getOrderId());
        Assertion.assertThat("OrderIDVisible " + orderID, !orderID.isEmpty(), is(true));

    }

    @Author(names = "Harshita")
    @Module(name = "Booking")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void testIssuedAirasiaAKOneWayInternational() throws FailedLoginException, InterruptedException {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        Map<String, String> param = getParam("testIssuedAirasiaAKOneWayInternational");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPageWeb = FlightLandingPageWeb.getInstance(session);
        FlightSearchResultPageWeb<?> flightSearchResultPage = FlightSearchResultPageWeb.getInstance(session);
        FlightBookingFormPageWeb<?> flightBookingFormPage = FlightBookingFormPageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        FlightProductReviewPageWeb<?> productReviewPage = FlightProductReviewPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);

        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
        int loginAttemptCount = Integer.parseInt(param.get("loginAttemptCount"));
        String origin = param.get("origin");
        String destination = param.get("destination");
        int adultPassengers = Integer.parseInt(param.get("adultCount"));
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);
        String airlineName = param.get("airlineName");
        String tripType = param.get("tripType");

        step("Login as " + username + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(username, password);

        step("User open Flight Landing Page");
        homePage.openVerticalPageViaURL("Flights");
        Assertion.assertThat("Flight Landing Page is Opened", flightLandingPageWeb.isOnFlightPage(), is(true));

        step("User select origin");
        flightLandingPageWeb.selectOrigin(origin);
        flightBookingFormPage.clickOkayGotIt();

        step("User select destination");
        flightLandingPageWeb.selectDestination(destination);
        flightBookingFormPage.clickOkayGotIt();

        step("Toggle trip type");
        flightLandingPageWeb.toggleTripType(tripType);
        
        step("Select date");
        flightLandingPageWeb.selectDate(departureDate);

        step("Toggle trip type");
        flightLandingPageWeb.toggleTripType(tripType);

        step("Add passenger");
        flightLandingPageWeb.addPassenger(adultPassengers, 0, 0);

        step("User click search button");
        flightLandingPageWeb.searchFlight();
        Assertion.assertThat("Search Result Page is loaded", flightSearchResultPage.isSearchResultPagePresent(), is(true));

        step("Wait for Search Result page to be fully loaded.");
        flightSearchResultPage.isSRPLoaded();

        step("User Selects Flight Airasia (AK)");
        flightSearchResultPage.filterAirline(airlineName);

        step("User Selects Any Flight");
        flightSearchResultPage.selectFlight();
        Assertion.assertThat("Product Review Page is loaded", productReviewPage.isPresent(), is(true));

        step("User Selects SELECT button in ECONOMY section");
        productReviewPage.clickSelectBtnRegular();

        step("Continue to booking form");
        productReviewPage.continueToBookingForm();
        flightBookingFormPage.clickNoThanksOnFlightMayReschedulePopup();

        step("Click toggle to fill passengers details and save ");
        flightBookingFormPage.fillPassengerDetails();

        step("Go to payment page");
        flightBookingFormPage.goToPaymentPageFromBookingFormPage();
        flightBookingFormPage.clickNoThanksOnFlightMayReschedulePopup();
        flightBookingFormPage.clickOkayGotIt();

        step("Payment page should be displayed");
        Assertion.assertThat("PaymentPageLoaded", paymentPage.isPaymentPagepresent(), is(true));
        String orderID = String.valueOf(paymentPage.getOrderId());
        Assertion.assertThat("OrderIDVisible " + orderID, !orderID.isEmpty(), is(true));
    }

    @Author(names = "Harshita")
    @Module(name = "Booking")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void testIssuedSabreOneWayInternational() throws FailedLoginException, InterruptedException {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        Map<String, String> param = getParam("testIssuedSabreOneWayInternational");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPageWeb = FlightLandingPageWeb.getInstance(session);
        FlightSearchResultPageWeb<?> flightSearchResultPage = FlightSearchResultPageWeb.getInstance(session);
        FlightBookingFormPageWeb<?> flightBookingFormPage = FlightBookingFormPageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        FlightProductReviewPageWeb<?> productReviewPage = FlightProductReviewPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);

        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
        int loginAttemptCount = Integer.parseInt(param.get("loginAttemptCount"));
        String origin = param.get("origin");
        String destination = param.get("destination");
        int adultPassengers = Integer.parseInt(param.get("adultCount"));
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate,daysToDepartureDate);
        String airlineName = param.get("airlineName");
        String tripType = param.get("tripType");

        step("Login as " + username + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(username, password);

        step("User open Flight Landing Page");
        homePage.openVerticalPageViaURL("Flights");
        Assertion.assertThat("Flight Landing Page is Opened", flightLandingPageWeb.isOnFlightPage(), is(true));

        step("Toggle trip type");
        flightLandingPageWeb.toggleTripType(tripType);

        step("User select origin");
        flightLandingPageWeb.selectOrigin(origin);

        step("User select destination");
        flightLandingPageWeb.selectDestination(destination);

        step("Select date");
        flightLandingPageWeb.selectDate(departureDate);

        step("Add passenger");
        flightLandingPageWeb.addPassenger(adultPassengers,0,0);

        step("User click search button");
        flightLandingPageWeb.searchFlight();
        Assertion.assertThat("Search Result Page is loaded", flightSearchResultPage.isSearchResultPagePresent(), is(true));

        step("Wait for Search Result page to be fully loaded.");
        flightSearchResultPage.isSRPLoaded();

        step("User Selects Flight Amadeus(SQ)");
        flightSearchResultPage.filterAirline(airlineName);

        step("User Selects Any Flight");
        flightSearchResultPage.selectFlight();
        Assertion.assertThat("Product Review Page is loaded", productReviewPage.isPresent(), is(true));

        step("User Selects SELECT button in ECONOMY section");
        productReviewPage.clickSelectBtnRegular();

        step("Continue to booking form");
        productReviewPage.continueToBookingForm();
        flightBookingFormPage.clickNoThanksOnFlightMayReschedulePopup();

        step("Click toggle to fill passengers details and save ");
        flightBookingFormPage.fillPassengerDetailsLogin(adultPassengers, 0, 0, "international", airlineName);

        step("Go to payment page");
        flightBookingFormPage.goToPaymentPageFromBookingFormPage();

        step("verify Review Passenger's Data");
        flightBookingFormPage.clickItsCorrectOnReviewPassengerDataPopup();

        step("Payment page should be displayed");
        Assertion.assertThat("PaymentPageLoaded", paymentPage.isPaymentPagepresent(), is(true));
        String orderID = String.valueOf(paymentPage.getOrderId());
        Assertion.assertThat("OrderIDVisible "+ orderID, !orderID.isEmpty(), is(true));
    }

    @Author(names = "Harshita")
    @Module(name = "Booking")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void testIssuedIUOneWayDomestic() throws FailedLoginException, InterruptedException {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        Map<String, String> param = getParam("testIssuedIUOneWayDomestic");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPageWeb = FlightLandingPageWeb.getInstance(session);
        FlightSearchResultPageWeb<?> flightSearchResultPage = FlightSearchResultPageWeb.getInstance(session);
        FlightBookingFormPageWeb<?> flightBookingFormPage = FlightBookingFormPageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        FlightProductReviewPageWeb<?> productReviewPage = FlightProductReviewPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);

        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
        int loginAttemptCount = Integer.parseInt(param.get("loginAttemptCount"));
        String origin = param.get("origin");
        String destination = param.get("destination");
        int adultPassengers = Integer.parseInt(param.get("adultCount"));
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate,daysToDepartureDate);
        String airlineName = param.get("airlineName");
        String tripType = param.get("tripType");

        step("Login as " + username + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(username, password);

        step("User open Flight Landing Page");
        homePage.openVerticalPageViaURL("Flights");
        Assertion.assertThat("Flight Landing Page is Opened", flightLandingPageWeb.isOnFlightPage(), is(true));

        step("Toggle trip type");
        flightLandingPageWeb.toggleTripType(tripType);

        step("User select origin");
        flightLandingPageWeb.selectOrigin(origin);
        flightBookingFormPage.clickOkayGotIt();

        step("User select destination");
        flightLandingPageWeb.selectDestination(destination);
        flightBookingFormPage.clickOkayGotIt();

        step("Select date");
        flightLandingPageWeb.selectDate(departureDate);

        step("Toggle trip type");
        flightLandingPageWeb.toggleTripType(tripType);

        step("Add passenger");
        flightLandingPageWeb.addPassenger(adultPassengers,0,0);

        step("User click search button");
        flightLandingPageWeb.searchFlight();
        Assertion.assertThat("Search Result Page is loaded", flightSearchResultPage.isSearchResultPagePresent(), is(true));

        step("Wait for Search Result page to be fully loaded.");
        flightSearchResultPage.isSRPLoaded();

        step("User Selects Flight Lion Vedelon(IU)");
        flightSearchResultPage.filterAirline(airlineName);

        step("User Selects Any Flight");
        flightSearchResultPage.selectFlight();
        Assertion.assertThat("Product Review Page is loaded", productReviewPage.isPresent(), is(true));

        step("User Selects SELECT button in ECONOMY section");
        productReviewPage.clickSelectBtnRegular();

        step("Continue to booking form");
        productReviewPage.continueToBookingForm();
        flightBookingFormPage.clickNoThanksOnFlightMayReschedulePopup();

        step("Click toggle to fill passengers details and save ");
        flightBookingFormPage.fillPassengerDetailsLogin(adultPassengers, 0, 0, "domestic", airlineName);

        step("Go to payment page");
        flightBookingFormPage.goToPaymentPageFromBookingFormPage();

        step("verify Review Passenger's Data");
        flightBookingFormPage.clickItsCorrectOnReviewPassengerDataPopup();
        flightBookingFormPage.clickNoThanksOnFlightMayReschedulePopup();

        step("If any extra popup occurs");
        flightBookingFormPage.clickOkayGotIt();

        step("Payment page should be displayed");
        Assertion.assertThat("PaymentPageLoaded", paymentPage.isPaymentPagepresent(), is(true));
        String orderID = String.valueOf(paymentPage.getOrderId());
        Assertion.assertThat("OrderIDVisible "+ orderID, !orderID.isEmpty(), is(true));
    }

    @Author(names = "Harshita")
    @Module(name = "Booking")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void testBookingOneWayOnePaxAddOnsThroughBaggage() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testBookingOneWayOnePaxAddOnsThroughBaggage");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPage = FlightLandingPageWeb.getInstance(session);
        FlightSearchResultPageWeb<?> searchResultPage = FlightSearchResultPageWeb.getInstance(session);
        FlightDescriptionPageWeb<?> flightDescriptionPage = FlightDescriptionPageWeb.getInstance(session);
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
        String FullName = param.get("fullName");
        String salutation = randomSalutation();
        String phoneNo = randomPhone();
        String email = randomEmail();
        String year = param.get("year");
        String month = param.get("month");
        String day = param.get("day");
        String tripType = param.get("tripType");
        String airlineName = param.get("departureAirlineName");

        step("User open Home Page");
        homePage.goToHomePage();

        step("Logout via URL");
        loginPage.logoutViaUrl();

        step("User open Flight Landing Page");
        homePage.openVerticalPageViaURL("Flights");
        Assertion.assertThat("Flight Landing Page is Opened", flightLandingPage.isOnFlightPage(), is(true));

        step("Toggle trip type");
        flightLandingPage.toggleTripType(tripType);

        step("User select origin");
        flightLandingPage.selectOrigin(origin);
        bookingFormPage.clickOkayGotIt();

        step("User select destination");
        flightLandingPage.selectDestination(destination);
        bookingFormPage.clickOkayGotIt();

        step("Select date");
        flightLandingPage.selectDate(departureDate);

        step("add passenger");
        flightLandingPage.addPassenger(adultPassengers, childPassengers, infantPassengers);

        step("User click search button");
        flightLandingPage.searchFlight();
        Assertion.assertThat("Search Result Page is loaded", searchResultPage.isSearchResultPagePresent(), is(true));

        step("Wait for Search Result page to be fully loaded.");
        searchResultPage.isSRPLoaded();

        step("User choose filter Transit");
        searchResultPage.CheckTransit();

        step("Apply Filter " + airlineName);
        searchResultPage.filterAirline(airlineName);

        step("select flight departure");
        searchResultPage.selectFlight();

        step("User is redirected to Product Review Page");
        Assertion.assertThat("Product Review page is visible", productReviewPage.isPresent(), is(true));

        step("User can see the schedule selection by user and can see the details");
        Assertion.assertThat("Departure schedule is displayed", productReviewPage.isDepartureScheduleVisible(), is(true));

        step("User clicks on 'See Other Facilities' hyperlink in flight details");
        productReviewPage.clickSeeOtherFacilities();

        step("User can see the popup with Flight Facilities Section");
        Assertion.assertThat("Facilities popup is displayed.", productReviewPage.isFacilitiesPopupVisible(), is(true));

        step("User closes the facilities popup");
        productReviewPage.closePopUp();

        step("User scrolls to Economy Card and clicks 'See Details' hyperlink");
        productReviewPage.clickSeeDetailsUnderEconomyCard();

        step("User is on Penalty Info popup in Product Review Page");
        Assertion.assertThat("Penalty Info popup is visible", productReviewPage.isPenaltyInfoVisible(), is(true));

        step("User selects the 'Reschedule' tab in the popup");
        productReviewPage.clickPenaltyInfoTabReschedule();

        step("User selects the 'PRICE DETAIL' tab in the popup");
        productReviewPage.clickPenaltyInfoTabPriceDetail();

        step("User clicks the close button on penalty info popup");
        productReviewPage.closePopUp();

        step("User clicks 'Select' button on 'Economy' card");
        productReviewPage.clickSelectBtnRegular();

        step("Verify user is directed to Booking Form page");
        bookingFormPage.waitForBookingPageToLoad();
        Assertion.assertThat("Booking Form page is visible", bookingFormPage.isBookingFormVisible(), is(true));

        step("Verify flight details are displayed");
        Assertion.assertThat("Flight details are displayed", bookingFormPage.isFlightDetailsVisible(), is(true));

        step("User navigates to Flight Details section");
        bookingFormPage.goOnFlightDetail();

        step("Verify Flight Schedule tab is visible");
        Assertion.assertThat("Flight Schedule tab is displayed", bookingFormPage.isFlightScheduleTabVisible(), is(true));

        step("User go on tiket Policies");
        bookingFormPage.closePopUp();

        step("Fill Passenger Details");
        bookingFormPage.inputContactDetailsNonLogin(salutation, FullName, phoneNo, email);
        bookingFormPage.clickSameContactToggle();
        bookingFormPage.fillPassengerDetailsLogin(adultPassengers, childPassengers, infantPassengers, "domestic", "");

        step("User is on Booking Form page at Addons section");
        bookingFormPage.scrollToBaggageSection();

        step("User Select Baggages for Passenger");
        bookingFormPage.addBaggageFromBookingForm();

        step("User taps on chevron icon on price section to view Price Details");
        bookingFormPage.openPriceDetailSection();

        step("User is redirected to Price Detail page");
        Assertion.assertThat("Price Detail Page is displayed", bookingFormPage.isPriceDetailPageVisible(), is(true));

        step("Verify Total Price Displayed");
        Assertion.assertThat("Total price is displayed", bookingFormPage.isTotalPriceDisplayed(), is(true));

        step("User closes the price detail page");
        bookingFormPage.closePopUp();

        step("User is redirected back to Booking Form");
        Assertion.assertThat("Booking Form page is visible again", bookingFormPage.isBookingFormVisible(), is(true));

        step("going to paymentPage");
        bookingFormPage.goToPaymentPageFromBookingFormPage();

        step("verify Review Passenger's Data");
        bookingFormPage.clickItsCorrectOnReviewPassengerDataPopup();
        bookingFormPage.clickNoThanksOnFlightMayReschedulePopup();

        step("Payment page should be displayed");
        Assertion.assertThat("PaymentPageLoaded", paymentPageWeb.isPaymentPagepresent(), is(true));
        String orderID = paymentPageWeb.getOrderId();
        orderID = String.valueOf(getOrderId(orderID));
        Assertion.assertThat("OrderIDVisible " + orderID, !orderID.isEmpty(), is(true));
    }

    @Author(names = {"Ashish"})
    @Module(name = "Test")
    @Tribe(name = "Test")
    @Vertical(name = "Test")
    @Test
    public void testFlightFillMultiPaxDweb() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testFlightFillMultiPaxDweb");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPageWeb = FlightLandingPageWeb.getInstance(session);
        FlightSearchResultPageWeb<?> flightSearchResultPage = FlightSearchResultPageWeb.getInstance(session);
        FlightProductReviewPage<?> flightProductReviewPage = new FlightProductReviewPage<>(session);
        FlightBookingFormPageWeb<?> flightBookingFormPage = FlightBookingFormPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");
        String airline = param.get("airline");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String departureDate = getRandomLongDateOnRange(daysToDepartureDate, 30);

        int adultPassengers = Integer.parseInt(param.get("adultPassengers"));
        int childPassengers = Integer.parseInt(param.get("childPassengers"));
        int infantPassengers = Integer.parseInt(param.get("infantPassengers"));

        String username = param.get("username");
        String password = param.get("password");
        String currency = param.get("currency");

        step("login in using { " + username + " }");
        loginPage.loginIfNotWithSameEmail(username, password, "123456", 1);

        step("User select { " + currency + " } currency");
        homePage.selectCurrency(currency);

        step("User open Flight vertical");
        homePage.openVerticalPageViaURL("Flights");

        step("Toggle trip type");
        flightLandingPageWeb.toggleTripType(tripType);

        step("Select origin airport");
        flightLandingPageWeb.selectOrigin(originAirport);

        step("Select destination airport");
        flightLandingPageWeb.selectDestination(destinationAirport);

        step("Select date");
        flightLandingPageWeb.selectDate(departureDate);

        step("add passenger");
        flightLandingPageWeb.addPassenger(adultPassengers, childPassengers, infantPassengers);

        step("search flight");
        flightLandingPageWeb.searchFlight();

        step("Apply Filter " + airline);
        flightSearchResultPage.filterAirline(airline);

        step("select flight");
        flightSearchResultPage.selectFlightByAirline(airline);

        step("Continue to booking form");
        flightProductReviewPage.continueToBookingForm();
        flightBookingFormPage.clickNoThanksOnFlightMayReschedulePopupPRP();

        step("Click toggle to fill passengers details and save ");
        FlightPaxDetail flightPaxDetail = flightBookingFormPage.fillPassengerDetailsLogin(adultPassengers, childPassengers, infantPassengers, "domestic", airline);

        step("Go to payment page");
        flightBookingFormPage.goToPaymentPageFromBookingFormPage();

        step("Skip Flight may rescheduled page by clicking no thanks");
        flightBookingFormPage.clickNoThanksOnFlightMayReschedulePopup();

        step("Payment page should be displayed");
        String orderID = paymentPage.getOrderId();
        orderID = String.valueOf(getOrderId(orderID));
        Assertion.assertThat("OrderIDVisible " + orderID, !orderID.isEmpty(), is(true));
    }

    @Author(names = "Rohan")
    @Module(name = "Booking")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void onewayInternationalSinglePaxFirefly() throws FailedLoginException, InterruptedException {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        Map<String, String> param = getParam("onewayInternationalSinglePaxFirefly");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPage =  FlightLandingPageWeb.getInstance(session);
        FlightSearchResultPageWeb<?> searchResultPage =  FlightSearchResultPageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        FlightProductReviewPageWeb<?> productReviewPage = FlightProductReviewPageWeb.getInstance(session);
        FlightBookingFormPageWeb<?> flightBookingFormPage = FlightBookingFormPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);


        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");

        int adultPassengers = Integer.parseInt(param.get("adultCount"));
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));

        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate,daysToDepartureDate);
        int duration = Integer.parseInt(param.get("duration"));
        String returnDate = CoreFunctions.addToLongDate(departureDate, duration);
        String username = param.get("username");
        String otp = param.get("otp");
        String password = param.get("password");
        String tripType = param.get("tripType");

        step("Login as " + username + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(username, password);

        step("User open Flight Landing Page");
        homePage.openVerticalPageViaURL("Flights");
        Assertion.assertThat("Flight Landing Page is Opened", flightLandingPage.isOnFlightPage(), is(true));

        step("Toggle trip type");
        flightLandingPage.toggleTripType(tripType);

        step("User select origin");
        flightLandingPage.selectOrigin(originAirport);

        step("User select destination");
        flightLandingPage.selectDestination(destinationAirport);

        step("Select date");
        flightLandingPage.selectDate(departureDate);

        step("Toggle trip type");
        flightLandingPage.toggleTripType(tripType);

        step("add passenger");
        flightLandingPage.addPassenger(adultPassengers,0,0);

        step("User click search button");
        flightLandingPage.searchFlight();
        Assertion.assertThat("Search Result Page is loaded", searchResultPage.isSearchResultPagePresent(), is(true));

        step("Wait for Search Result page to be fully loaded.");
        searchResultPage.isSRPLoaded();

        step("User Selects Flight Firefly");
        searchResultPage.filterAirline("Firefly");

        step("User Selects Any Flight");
        searchResultPage.selectFlight();

        step("User Selects SELECT button in ECONOMY section");
        productReviewPage.clickSelectBtnRegular();

        step("Continue to booking form");
        productReviewPage.continueToBookingForm();
        flightBookingFormPage.clickNoThanksOnFlightMayReschedulePopup();

        step("Click toggle to fill passengers details and save ");
        flightBookingFormPage.fillPassengerDetails();

        step("Go to payment page");
        flightBookingFormPage.goToPaymentPageFromBookingFormPage();
        flightBookingFormPage.clickNoThanksOnFlightMayReschedulePopup();
        flightBookingFormPage.clickOkayGotIt();

        step("Payment page should be displayed");
        Assertion.assertThat("Payment Page loaded", paymentPage.isPaymentPagepresent(), is(true));
        String orderID = paymentPage.getOrderId();
        orderID = String.valueOf(getOrderId(orderID));
        Assertion.assertThat("OrderID is visible "+ orderID, !orderID.isEmpty(), is(true));
    }

    @Author(names = "Rohan")
    @Module(name = "Booking")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void onewayInternationalSinglePaxAeroDili() throws FailedLoginException, InterruptedException {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        Map<String, String> param = getParam("onewayInternationalSinglePaxAeroDili");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPage =  FlightLandingPageWeb.getInstance(session);
        FlightSearchResultPageWeb<?> searchResultPage =  FlightSearchResultPageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        FlightProductReviewPageWeb<?> productReviewPage = FlightProductReviewPageWeb.getInstance(session);
        FlightBookingFormPageWeb<?> flightBookingFormPage = FlightBookingFormPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);

        String airlineName = param.get("airlineName");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");
        int adultPassengers = Integer.parseInt(param.get("adultCount"));
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));

        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate,daysToDepartureDate);
        String tripType = param.get("tripType");
        int duration = Integer.parseInt(param.get("duration"));
        String returnDate = CoreFunctions.addToLongDate(departureDate, duration);
        String username = param.get("username");
        String otp = param.get("otp");
        String password = param.get("password");

        step("Login as " + username + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(username, password);

        step("User open Flight Landing Page");
        homePage.openVerticalPageViaURL("Flights");
        Assertion.assertThat("Flight Landing Page is Opened", flightLandingPage.isOnFlightPage(), is(true));

        step("Toggle trip type to oneWay");
        flightLandingPage.toggleTripType(tripType);

        step("User select origin");
        flightLandingPage.selectOrigin(originAirport);

        step("User select destination");
        flightLandingPage.selectDestination(destinationAirport);

        step("Select date");
        flightLandingPage.selectDate(departureDate);

        step("Toggle trip type to oneWay");
        flightLandingPage.toggleTripType(tripType);

        step("add passenger");
        flightLandingPage.addPassenger(adultPassengers,0,0);

        step("User click search button");
        flightLandingPage.searchFlight();
        Assertion.assertThat("Search Result Page is loaded", searchResultPage.isSearchResultPagePresent(), is(true));

        step("Wait for Search Result page to be fully loaded.");
        searchResultPage.isSRPLoaded();

        step("User Selects Flight Aero Dili");
        searchResultPage.filterAirline(airlineName);

        step("User Selects Any Flight");
        searchResultPage.selectFlight();

        step("User Selects SELECT button in ECONOMY section");
        productReviewPage.clickSelectBtnRegular();

        step("Continue to booking form");
        productReviewPage.continueToBookingForm();
        flightBookingFormPage.clickNoThanksOnFlightMayReschedulePopup();

        step("Click toggle to fill passengers details and save ");
        flightBookingFormPage.fillPassengerDetailsLogin(adultPassengers, 0, 0, "international", airlineName);

        step("Go to payment page");
        flightBookingFormPage.goToPaymentPageFromBookingFormPage();
        flightBookingFormPage.clickNoThanksOnFlightMayReschedulePopup();
        flightBookingFormPage.clickOkayGotIt();

        step("Payment page should be displayed");
        Assertion.assertThat("Payment Page Loaded", paymentPage.isPaymentPagepresent(), is(true));
        String orderID = paymentPage.getOrderId();
        orderID = String.valueOf(getOrderId(orderID));
        Assertion.assertThat("OrderID is Visible "+ orderID, !orderID.isEmpty(), is(true));
    }

    @Author(names = "Rohan")
    @Module(name = "Booking")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void oneWayDomesticSinglePAXBookingwithoutAddONsAirAsia() throws FailedLoginException, InterruptedException {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        Map<String, String> param = getParam("oneWayDomesticSinglePAXBookingwithoutAddONsAirAsia");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPage = FlightLandingPageWeb.getInstance(session);
        FlightSearchResultPageWeb<?> searchResultPage = FlightSearchResultPageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        FlightProductReviewPageWeb<?> productReviewPage = FlightProductReviewPageWeb.getInstance(session);
        FlightBookingFormPageWeb<?> flightBookingFormPage = FlightBookingFormPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);


        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");
        int adultPassengers = Integer.parseInt(param.get("adultCount"));
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String airlineName = param.get("airlineName");
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);
        int duration = Integer.parseInt(param.get("duration"));
        String returnDate = CoreFunctions.addToLongDate(departureDate, duration);
        String tripType = param.get("tripType");
        String username = param.get("username");
        String otp = param.get("otp");
        String password = param.get("password");

        step("Login as " + username + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(username, password);

        step("Change Currency to IDR");
        homePage.selectCurrency("IDR");

        step("User open Flight Landing Page");
        homePage.openVerticalPageViaURL("Flights");
        Assertion.assertThat("Flight Landing Page is Opened", flightLandingPage.isOnFlightPage(), is(true));

        step("Toggle trip type");
        flightLandingPage.toggleTripType(tripType);

        step("User select origin");
        flightLandingPage.selectOrigin(originAirport);

        step("User select destination");
        flightLandingPage.selectDestination(destinationAirport);

        step("Select date");
        flightLandingPage.selectDate(departureDate);

        step("Toggle trip type");
        flightLandingPage.toggleTripType(tripType);

        step("add passenger");
        flightLandingPage.addPassenger(adultPassengers, 0, 0);

        step("User click search button");
        flightLandingPage.searchFlight();
        Assertion.assertThat("Search Result Page is loaded", searchResultPage.isSearchResultPagePresent(), is(true));

        step("Wait for Search Result page to be fully loaded.");
        searchResultPage.isSRPLoaded();

        step("User Selects Flight AirAsia Indonesia");
        searchResultPage.filterAirline(airlineName);

        step("User Selects Any Flight");
        searchResultPage.selectFlight();

        step("User Selects SELECT button in ECONOMY section");
        productReviewPage.clickSelectBtnRegular();

        step("Continue to booking form");
        productReviewPage.continueToBookingForm();
        flightBookingFormPage.clickNoThanksOnFlightMayReschedulePopup();

        step("Click toggle to fill passengers details and save ");
        flightBookingFormPage.fillPassengerDetails();

        step("going to paymentPage");
        flightBookingFormPage.goToPaymentPageFromBookingFormPage();

        step("verify Review Passenger's Data");
        flightBookingFormPage.clickItsCorrectOnReviewPassengerDataPopup();
        flightBookingFormPage.clickNoThanksOnFlightMayReschedulePopup();

        step("click I don't want it");
        flightBookingFormPage.clickOkayGotIt();

        step("Payment page should be displayed");
        Assertion.assertThat("PaymentPageLoaded", paymentPage.isPaymentPagepresent(), is(true));
        String orderID = paymentPage.getOrderId();
        orderID = String.valueOf(getOrderId(orderID));
        Assertion.assertThat("OrderIDVisible " + orderID, !orderID.isEmpty(), is(true));
    }

    @Author(names = "Priyanka")
    @Module(name = "Booking")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void GarudaBookingOnewayDomesticSinglePassenger() throws InterruptedException, FailedLoginException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("GarudaBookingOnewayDomesticSinglePassenger");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPage = FlightLandingPageWeb.getInstance(session);
        FlightSearchResultPageWeb<?> flightSearchResultPageWeb = FlightSearchResultPageWeb.getInstance(session);
        FlightProductReviewPageWeb<?> flightProductReviewPageWeb = FlightProductReviewPageWeb.getInstance(session);
        FlightBookingFormPageWeb<?> flightBookingFormPageWeb = FlightBookingFormPageWeb.getInstance(session);
        PaymentPageWeb<?>paymentPageWeb=PaymentPageWeb.getInstance(session);

        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));

        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate,daysToDepartureDate);
        int duration = Integer.parseInt(param.get("duration"));
        String returnDate = CoreFunctions.addToLongDate(departureDate, duration);
        String tripType = param.get("tripType");
        String username = param.get("username");
        String otp = param.get("otp");
        String password = param.get("password");
        String airlineName = param.get("airlineName");

        step("Login as " + username + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(username, password);

        step("Change Currency to IDR");
        homePage.selectCurrency("IDR");

        step("User open Flight Landing Page");
        homePage.openVerticalPageViaURL("Flights");

        step("Toggle trip type");
        flightLandingPage.toggleTripType(tripType);

        step("Select origin airport");
        flightLandingPage.selectOrigin(originAirport);

        step("Select destination airport");
        flightLandingPage.selectDestination(destinationAirport);

        step("Select date");
        flightLandingPage.selectDate(departureDate);

        step("Toggle trip type");
        flightLandingPage.toggleTripType(tripType);

        step("search flight");
        flightLandingPage.searchFlight();

        step("filter airline {" + airlineName + " }" );
        flightSearchResultPageWeb.filterAirline(airlineName);

        step("select flight for departure");
        flightSearchResultPageWeb.selectFlight();

        step("select the SELECT Button");
        flightProductReviewPageWeb.clickSelectBtnRegular();

        step("close Reschedule popUp");
        flightBookingFormPageWeb.clickNoThanksOnFlightMayReschedulePopup();

        step("enter the passenger Name");
        flightBookingFormPageWeb.fillPassengerDetails();

        step("going to paymentPage");
        flightBookingFormPageWeb.goToPaymentPageFromBookingFormPage();
        flightBookingFormPageWeb.clickNoThanksOnFlightMayReschedulePopup();

        step("click I don't want it");
        flightBookingFormPageWeb.clickOkayGotIt();

        step("Payment page should be displayed");
        Assertion.assertThat("PaymentPageLoaded", paymentPageWeb.isPaymentPagepresent(), is(true));
        String orderID = paymentPageWeb.getOrderId();
        orderID = String.valueOf(getOrderId(orderID));
        Assertion.assertThat("OrderIDVisible "+ orderID, !orderID.isEmpty(), is(true));
    }

    @Author(names = {"Rohan"})
    @Module(name = "Booking")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void LionVedaleonRTSIntlMultiPaxAddons() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("LionVedaleonRTSIntlMultiPaxAddons");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPageWeb = FlightLandingPageWeb.getInstance(session);
        FlightSearchResultPageWeb<?> flightSearchResultPage = FlightSearchResultPageWeb.getInstance(session);
        FlightProductReviewPage<?> flightProductReviewPage = new FlightProductReviewPage<>(session);
        FlightBookingFormPageWeb<?> flightBookingFormPage = FlightBookingFormPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        String tripType = param.get("tripType");
        String originAirport = param.get("origin");
        String destinationAirport = param.get("destination");
        String airlineNameDepart = param.get("airlineNameDepart");
        String airlineNameReturn = param.get("airlineNameReturn");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int duration = Integer.parseInt(param.get("duration"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate,daysToDepartureDate);
        String returnDate = CoreFunctions.addToLongDate(departureDate, duration);


        int adultPassengers = Integer.parseInt(param.get("adultPassengers"));
        int childPassengers = Integer.parseInt(param.get("childPassengers"));
        int infantPassengers = Integer.parseInt(param.get("infantPassengers"));

        String username = param.get("username");
        String password = param.get("password");
        String currency = param.get("currency");

        step("Login as " + username + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(username, password);

        step("User open Flight vertical");
        homePage.openVerticalPageViaURL("Flights");

        step("Toggle trip type");
        flightLandingPageWeb.toggleTripType(tripType);

        step("Select origin airport");
        flightLandingPageWeb.selectOrigin(originAirport);

        step("Select destination airport");
        flightLandingPageWeb.selectDestination(destinationAirport);

        step("Select date");
        flightLandingPageWeb.selectDate(departureDate,returnDate);

        step("add passenger");
        flightLandingPageWeb.addPassenger(adultPassengers,childPassengers,infantPassengers);

        step("search flight");
        flightLandingPageWeb.searchFlight();
        Assertion.assertThat("Search Result Page is loaded", flightSearchResultPage.isSearchResultPagePresent(), is(true));

        step("Apply Filter " + airlineNameDepart);
        flightSearchResultPage.filterAirline(airlineNameDepart);

        step("select flight");
        flightSearchResultPage.selectFlightByAirline(airlineNameDepart);
        Assertion.assertThat("Search Result Page is loaded", flightSearchResultPage.isSearchResultPagePresent(), is(true));

        step("Apply Filter " + airlineNameReturn);
        flightSearchResultPage.filterAirline(airlineNameReturn);

        step("select flight");
        flightSearchResultPage.selectFlightByAirline(airlineNameReturn);

        step("Continue to booking form");
        flightProductReviewPage.continueToBookingForm();
        flightBookingFormPage.clickNoThanksOnFlightMayReschedulePopupPRP();

        step("Click toggle to fill passengers details and save ");
        flightBookingFormPage.fillPassengerDetailsLogin(adultPassengers,childPassengers,infantPassengers,"international",airlineNameDepart);

        step("User is on Booking Form page at Addons section");
        flightBookingFormPage.scrollToBaggageSection();

        step("User Select Baggages for Passenger");
        flightBookingFormPage.selectAddonsBaggageMultiPAX(adultPassengers,childPassengers);

        step("Select Insurance");
        flightBookingFormPage.selectInsurance(currency);

        step("Go to payment page");
        flightBookingFormPage.goToPaymentPageFromBookingFormPage();

        step("verify Review Passenger's Data");
        flightBookingFormPage.clickItsCorrectOnReviewPassengerDataPopup();

        step("Payment page should be displayed");
        String orderID = paymentPage.getOrderId();
        orderID = String.valueOf(getOrderId(orderID));
        Assertion.assertThat("OrderIDVisible " + orderID, !orderID.isEmpty(), is(true));
    }

    @Author(names = "Harshita")
    @Module(name = "Booking")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void testBookingSmartTripSabreMultiPax()  throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testBookingSmartTripSabreMultiPax");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPage = FlightLandingPageWeb.getInstance(session);
        FlightSearchResultPageWeb<?> searchResultPage = FlightSearchResultPageWeb.getInstance(session);
        FlightProductReviewPageWeb<?> productReviewPage = FlightProductReviewPageWeb.getInstance(session);
        FlightBookingFormPageWeb<?> bookingFormPage =  FlightBookingFormPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPageWeb=PaymentPageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        String origin = param.get("origin");
        String destination = param.get("destination");
        String airline = param.get("airline");
        int adultPassengers = Integer.parseInt(param.get("adultCount"));
        int childPassengers = Integer.parseInt(param.get("childCount"));
        int infantPassengers = Integer.parseInt(param.get("infantCount"));
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        int duration = Integer.parseInt(param.get("duration"));
        String departureDate = CoreFunctions.addToLongDate(currentDate,daysToDepartureDate);
        String returnDate = CoreFunctions.addToLongDate(departureDate, duration);
        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
        String tripType = param.get("tripType");

        int loginAttemptCount = Integer.parseInt(param.get("loginAttemptCount"));

        step("Login as " + username + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(username, password);

        step("User open Flight Landing Page");
        homePage.openVerticalPageViaURL("Flights");
        Assertion.assertThat("Flight Landing Page is Opened", flightLandingPage.isOnFlightPage(), is(true));

        step("Toggle trip type");
        flightLandingPage.toggleTripType(tripType);

        step("User select origin");
        flightLandingPage.selectOrigin(origin);
        bookingFormPage.clickOkayGotIt();

        step("User select destination");
        flightLandingPage.selectDestination(destination);
        bookingFormPage.clickOkayGotIt();

        step("Select date");
        flightLandingPage.selectDate(departureDate,returnDate);

        step("add passenger");
        flightLandingPage.addPassenger(adultPassengers,childPassengers,infantPassengers);

        step("User click search button");
        flightLandingPage.searchFlight();
        Assertion.assertThat("Search Result Page is loaded", searchResultPage.isSearchResultPagePresent(), is(true));

        step("Wait for Search Result page to be fully loaded.");
        searchResultPage.isSRPLoaded();

        step("User Click on roundtrip deals button");
        searchResultPage.roundTripDealsFilter();

        step("User choose filter Flight");
        searchResultPage.filterAirline(airline);

        step("select flight departure");
        searchResultPage.selectFlight();
        Assertion.assertThat("Search Result Page is loaded", searchResultPage.isSearchResultPagePresent(), is(true));

        step("User choose filter Flight for depart");
        searchResultPage.filterAirline(airline);

        step("select flight return");
        searchResultPage.selectFlight();

        step("User is redirected to Product Review Page");
        Assertion.assertThat("Product Review page is visible", productReviewPage.isPresent(), is(true));

        step("User can see the schedule selection by user and can see the details");
        Assertion.assertThat("Departure schedule is displayed", productReviewPage.isDepartureScheduleVisible(), is(true));

        step("User clicks on 'See Other Facilities' hyperlink in flight details");
        productReviewPage.clickSeeOtherFacilities();

        step("User can see the popup with Flight Facilities Section");
        Assertion.assertThat("Facilities popup is displayed.", productReviewPage.isFacilitiesPopupVisible(), is(true));

        step("User selects the 'Reschedule' tab in the popup");
        productReviewPage.clickPenaltyInfoTabReschedule();

        step("User selects the 'PRICE DETAIL' tab in the popup");
        productReviewPage.clickPenaltyInfoTabPriceDetail();

        step("User clicks the close button on penalty info popup");
        productReviewPage.closePopUp();

        step("User clicks 'Select' button on 'Economy' card");
        productReviewPage.clickSelectBtnRegular();

        step("Verify user is directed to Booking Form page");
        bookingFormPage.clickOkayGotIt();
        bookingFormPage.waitForBookingPageToLoad();
        Assertion.assertThat("Booking Form page is visible", bookingFormPage.isBookingFormVisible(), is(true));

        step("Verify flight details are displayed");
        Assertion.assertThat("Flight details are displayed", bookingFormPage.isFlightDetailsVisible(), is(true));

        step("User navigates to Flight Details section");
        bookingFormPage.goOnFlightDetail();

        step("Verify Flight Schedule tab is visible");
        Assertion.assertThat("Flight Schedule tab is displayed", bookingFormPage.isFlightScheduleTabVisible(), is(true));

        step("User Click see details to see refund reschedule info");
        bookingFormPage.clickOnSeeDetails();

        step("User can see refund reschedule info");
        Assertion.assertThat("Flight Facility button is visible", bookingFormPage.isRRPopUpAppear(), is(true));
        bookingFormPage.closeRRPopUp();

        step("User go on tiket Policies");
        bookingFormPage.closePopUp();

        step("Click toggle to fill passengers details and save ");
        bookingFormPage.fillPassengerDetailsLogin(adultPassengers, childPassengers, infantPassengers, "international", airline);

        step("User taps on chevron icon on price section to view Price Details");
        bookingFormPage.openPriceDetailSection();

        step("User is redirected to Price Detail page");
        Assertion.assertThat("Price Detail Page is displayed", bookingFormPage.isPriceDetailPageVisible(), is(true));

        step("Verify Total Price Displayed");
        Assertion.assertThat("Total price is displayed", bookingFormPage.isTotalPriceDisplayed(), is(true));

        step("User closes the price detail page");
        bookingFormPage.closePopUp();

        step("User is redirected back to Booking Form");
        Assertion.assertThat("Booking Form page is visible again", bookingFormPage.isBookingFormVisible(), is(true));

        step("going to paymentPage");
        bookingFormPage.goToPaymentPageFromBookingFormPage();

        step("verify Review Passenger's Data");
        bookingFormPage.clickItsCorrectOnReviewPassengerDataPopup();

        step("Payment page should be displayed");
        Assertion.assertThat("PaymentPageLoaded", paymentPageWeb.isPaymentPagepresent(), is(true));
        String orderID = paymentPageWeb.getOrderId();
        orderID = String.valueOf(getOrderId(orderID));
        Assertion.assertThat("OrderIDVisible "+ orderID, !orderID.isEmpty(), is(true));
    }

    @Author(names = {"Rohan"})
    @Module(name = "Citilink")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void CitilinkBookingOnewayDomesticSinglePassengerwithoutaddons() throws InterruptedException, FailedLoginException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("CitilinkBookingOnewayDomesticSinglePassengerwithoutaddons");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPage =  FlightLandingPageWeb.getInstance(session);
        FlightSearchResultPageWeb<?> searchResultPage =  FlightSearchResultPageWeb.getInstance(session);
        FlightProductReviewPageWeb<?> flightProductReviewPageWeb = FlightProductReviewPageWeb.getInstance(session);
        FlightBookingFormPageWeb<?> flightBookingFormPageWeb = FlightBookingFormPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPageWeb=PaymentPageWeb.getInstance(session);

        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate,daysToDepartureDate);
        String tripType = param.get("tripType");
        String username = param.get("username");
        String passengerName = param.get("passengerName");
        String password = param.get("password");
        String airlineName = param.get("airlineName");

        step("Login as " + username + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(username, password);

        step("Change Currency to IDR");
        homePage.selectCurrency("IDR");

        step("User open Flight Landing Page");
        homePage.openVerticalPageViaURL("Flights");
        Assertion.assertThat("Flight Landing Page is Opened", flightLandingPage.isOnFlightPage(), is(true));

        step("reset to single passenger");
        flightLandingPage.addPassenger(1,0,0);

        step("Toggle trip type");
        flightLandingPage.toggleTripType(tripType);

        step("Select origin airport");
        flightLandingPage.selectOrigin(originAirport);

        step("Select destination airport");
        flightLandingPage.selectDestination(destinationAirport);

        step("Select date");
        flightLandingPage.selectDate(departureDate);

        step("Toggle trip type");
        flightLandingPage.toggleTripType(tripType);

        step("search flight");
        flightLandingPage.searchFlight();
        Assertion.assertThat("Search Result Page is loaded", searchResultPage.isSearchResultPagePresent(), is(true));

        step("filter airline {" + airlineName + " }" );
        searchResultPage.filterAirline(airlineName);

        step("select flight for departure");
        searchResultPage.selectFlightByAirline(airlineName);

        step("select the SELECT Button");
        flightProductReviewPageWeb.clickSelectBtnRegular();

        step("close Reschedule popUp");
        flightBookingFormPageWeb.clickNoThanksOnFlightMayReschedulePopup();

        step("enter the passenger Name");
        flightBookingFormPageWeb.fillPassengerDetailsLogin(passengerName);

        step("going to paymentPage");
        flightBookingFormPageWeb.goToPaymentPageFromBookingFormPage();

        step("verify Review Passenger's Data");
        flightBookingFormPageWeb.clickItsCorrectOnReviewPassengerDataPopup();
        flightBookingFormPageWeb.clickNoThanksOnFlightMayReschedulePopup();

        step("Payment page should be displayed");
        Assertion.assertThat("PaymentPageLoaded", paymentPageWeb.isPaymentPagepresent(), is(true));
        String orderID = paymentPageWeb.getOrderId();
        orderID = String.valueOf(getOrderId(orderID));
        Assertion.assertThat("OrderIDVisible "+ orderID, !orderID.isEmpty(), is(true));
    }

    @Author(names = "Harshita")
    @Module(name = "Booking")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void testBookingSmartTripAmadeusMultiPax()  throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testBookingSmartTripAmadeusMultiPax");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPage = FlightLandingPageWeb.getInstance(session);
        FlightSearchResultPageWeb<?> searchResultPage = FlightSearchResultPageWeb.getInstance(session);
        FlightProductReviewPageWeb<?> productReviewPage = FlightProductReviewPageWeb.getInstance(session);
        FlightBookingFormPageWeb<?> bookingFormPage =  FlightBookingFormPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPageWeb=PaymentPageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        String origin = param.get("origin");
        String destination = param.get("destination");
        String airline = param.get("airline");
        int adultPassengers = Integer.parseInt(param.get("adultCount"));
        int childPassengers = Integer.parseInt(param.get("childCount"));
        int infantPassengers = Integer.parseInt(param.get("infantCount"));
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        int duration = Integer.parseInt(param.get("duration"));
        String departureDate = CoreFunctions.addToLongDate(currentDate,daysToDepartureDate);
        String returnDate = CoreFunctions.addToLongDate(departureDate, duration);
        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
        String tripType = param.get("tripType");

        int loginAttemptCount = Integer.parseInt(param.get("loginAttemptCount"));

        step("Login as " + username + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(username, password);

        step("User open Flight Landing Page");
        homePage.openVerticalPageViaURL("Flights");
        Assertion.assertThat("Flight Landing Page is Opened", flightLandingPage.isOnFlightPage(), is(true));

        step("Toggle trip type");
        flightLandingPage.toggleTripType(tripType);

        step("User select origin");
        flightLandingPage.selectOrigin(origin);
        bookingFormPage.clickOkayGotIt();

        step("User select destination");
        flightLandingPage.selectDestination(destination);
        bookingFormPage.clickOkayGotIt();

        step("Select date");
        flightLandingPage.selectDate(departureDate,returnDate);

        step("add passenger");
        flightLandingPage.addPassenger(adultPassengers,childPassengers,infantPassengers);

        step("User click search button");
        flightLandingPage.searchFlight();
        Assertion.assertThat("Search Result Page is loaded", searchResultPage.isSearchResultPagePresent(), is(true));

        step("Wait for Search Result page to be fully loaded.");
        searchResultPage.isSRPLoaded();

        step("User Click on roundTripDealsFilter");
        searchResultPage.roundTripDealsFilter();

        step("User choose filter Flight");
        searchResultPage.filterAirline(airline);

        step("select flight departure");
        searchResultPage.selectFlight();

        step("User choose filter Flight for depart");
        searchResultPage.filterAirline(airline);

        step("select flight return");
        searchResultPage.selectFlight();

        step("User is redirected to Product Review Page");
        Assertion.assertThat("Product Review page is visible", productReviewPage.isPresent(), is(true));

        step("User can see the schedule selection by user and can see the details");
        Assertion.assertThat("Departure schedule is displayed", productReviewPage.isDepartureScheduleVisible(), is(true));

        step("User clicks on 'See Other Facilities' hyperlink in flight details");
        productReviewPage.clickSeeOtherFacilities();

        step("User can see the popup with Flight Facilities Section");
        Assertion.assertThat("Facilities popup is displayed.", productReviewPage.isFacilitiesPopupVisible(), is(true));

        step("User selects the 'Reschedule' tab in the popup");
        productReviewPage.clickPenaltyInfoTabReschedule();

        step("User selects the 'PRICE DETAIL' tab in the popup");
        productReviewPage.clickPenaltyInfoTabPriceDetail();

        step("User clicks the close button on penalty info popup");
        productReviewPage.closePopUp();

        step("User clicks 'Select' button on 'Economy' card");
        productReviewPage.clickSelectBtnRegular();

        step("Verify user is directed to Booking Form page");
        bookingFormPage.clickOkayGotIt();
        bookingFormPage.waitForBookingPageToLoad();
        Assertion.assertThat("Booking Form page is visible", bookingFormPage.isBookingFormVisible(), is(true));

        step("Verify flight details are displayed");
        Assertion.assertThat("Flight details are displayed", bookingFormPage.isFlightDetailsVisible(), is(true));

        step("User navigates to Flight Details section");
        bookingFormPage.goOnFlightDetail();

        step("Verify Flight Schedule tab is visible");
        Assertion.assertThat("Flight Schedule tab is displayed", bookingFormPage.isFlightScheduleTabVisible(), is(true));

        step("User Selects Details of Flight Facility");
        bookingFormPage.clickOnFacilitiesDetail();

        step("User can see Flight Facilities");
        Assertion.assertThat("Flight Facility button is visible", bookingFormPage.isFlightFacilityVisible(), is(true));
        bookingFormPage.closeSecondPopup();

        step("User Click see details to see refund reschedule info");
        bookingFormPage.clickOnSeeDetails();

        step("User can see refund reschedule info");
        Assertion.assertThat("Flight Facility button is visible", bookingFormPage.isRRPopUpAppear(), is(true));
        bookingFormPage.closeRRPopUp();

        step("User go on tiket Policies");
        bookingFormPage.closePopUp();

        step("Click toggle to fill passengers details and save ");
        bookingFormPage.fillPassengerDetailsLogin(adultPassengers, childPassengers, infantPassengers, "international", airline);

        step("User taps on chevron icon on price section to view Price Details");
        bookingFormPage.openPriceDetailSection();

        step("User is redirected to Price Detail page");
        Assertion.assertThat("Price Detail Page is displayed", bookingFormPage.isPriceDetailPageVisible(), is(true));

        step("Verify Total Price Displayed");
        Assertion.assertThat("Total price is displayed", bookingFormPage.isTotalPriceDisplayed(), is(true));

        step("User closes the price detail page");
        bookingFormPage.closePopUp();

        step("User is redirected back to Booking Form");
        Assertion.assertThat("Booking Form page is visible again", bookingFormPage.isBookingFormVisible(), is(true));

        step("going to paymentPage");
        bookingFormPage.goToPaymentPageFromBookingFormPage();
        bookingFormPage.clickNoThanksOnFlightMayReschedulePopup();

        step("click I don't want it");
        bookingFormPage.clickOkayGotIt();

        step("Payment page should be displayed");
        Assertion.assertThat("PaymentPageLoaded", paymentPageWeb.isPaymentPagepresent(), is(true));
        String orderID = paymentPageWeb.getOrderId();
        orderID = String.valueOf(getOrderId(orderID));
        Assertion.assertThat("OrderIDVisible "+ orderID, !orderID.isEmpty(), is(true));
    }

    @Author(names = "Gavin")
    @Module(name = "Booking")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void testBookingOneWaySinglePaxReceivePopUpBaggageTouchPoint() throws Exception {
        final int RETRY_LOGIN_COUNTER = 3;

        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testBookingOneWaySinglePaxReceivePopUpBaggageTouchPoint");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPage = FlightLandingPageWeb.getInstance(session);
        FlightSearchResultPageWeb<?> flightSearchResultPageWeb = FlightSearchResultPageWeb.getInstance(session);
        FlightProductReviewPageWeb<?> flightProductReviewPageWeb = FlightProductReviewPageWeb.getInstance(session);
        FlightBookingFormPageWeb<?> flightBookingFormPageWeb = FlightBookingFormPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPageWeb = PaymentPageWeb.getInstance(session);

        String origin = param.get("origin");
        String destination = param.get("destination");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));

        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);

        String username = param.get("username");
        String otp = param.get("otp");
        String password = param.get("password");
        String airlineNameDepart = param.get("airlineNameDepart");
        String tripType = param.get("tripType");

        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));

        step("Login as " + username + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(username, password);

        step("Change Currency to IDR");
        homePage.selectCurrency("IDR");

        step("User open Flight Landing Page");
        homePage.openVerticalPageViaURL("Flights");

        step("Toggle trip type");
        flightLandingPage.toggleTripType(tripType);

        step("Select origin airport");
        flightLandingPage.selectOrigin(origin);

        step("Select destination airport");
        flightLandingPage.selectDestination(destination);

        step("Select date");
        flightLandingPage.selectDate(departureDate);

        step("Add passenger");
        flightLandingPage.addPassenger(adultPassengers, childPassengers, infantPassengers);

        step("Search flight");
        flightLandingPage.searchFlight();
        Assertion.assertThat("Search Result Page is loaded", flightSearchResultPageWeb.isSearchResultPagePresent(), is(true));

        step("Filter airline {" + airlineNameDepart + " }");
        flightSearchResultPageWeb.filterAirline(airlineNameDepart);

        step("Select flight for departure");
        flightSearchResultPageWeb.selectFlightByAirlineWithoutInclusiveBaggage(airlineNameDepart);

        step("Select the SELECT Button");
        flightProductReviewPageWeb.clickSelectBtnRegular();

        step("Enter the passenger Name");
        flightBookingFormPageWeb.fillPassengerDetailsLogin(adultPassengers, childPassengers, infantPassengers, "international", airlineNameDepart);

        step("Select continue to Payment");
        flightBookingFormPageWeb.goToPaymentPageWithoutSkippingPopup();

        step("User able to see 2 buttons 'Check passenger & baggage details' and 'Continue to payment'");
        Assertion.assertThat("Verify 'Check passenger & baggage details' label",
                flightBookingFormPageWeb.getNonPaymentConfirmationDialogPopupText(),
                is("Check passenger & baggage details"));
        Assertion.assertThat("Verify 'Continue to payment' label",
                flightBookingFormPageWeb.getPaymentConfirmationDialogPopupText(),
                is("Continue to Payment"));

        step("User able to see baggage pop up message");
        Assertion.assertThat("Warning message is valid",
                flightBookingFormPageWeb.getContinueToPaymentPopupWarningMessage(),
                is("Make sure you have enough baggage"));

        step("Click 'Check passenger & baggage details'");
        flightBookingFormPageWeb.goBackToBookingFormFromPaymentPopUp();

        step("User select baggage addon for all pax");
        flightBookingFormPageWeb.selectAddonsBaggage();

        step("Click go to payment page");
        flightBookingFormPageWeb.goToPaymentPageFromBookingFormPage();

        step("verify Review Passenger's Data");
        flightBookingFormPageWeb.clickItsCorrectOnReviewPassengerDataPopup();

        step("Payment page should be displayed");
        Assertion.assertThat("PaymentPageLoaded", paymentPageWeb.isPaymentPagepresent(), is(true));
        String orderID = paymentPageWeb.getOrderId();
        orderID = String.valueOf(getOrderId(orderID));
        Assertion.assertThat("OrderIDVisible "+ orderID, !orderID.isEmpty(), is(true));
    }

    @Author(names = "Rohan")
    @Module(name = "Booking")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void oneWayDomesticSinglePAXBookingwithoutAddONsSriwijaya() throws FailedLoginException, InterruptedException {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        Map<String, String> param = getParam("oneWayDomesticSinglePAXBookingwithoutAddONsSriwijaya");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPage = FlightLandingPageWeb.getInstance(session);
        FlightSearchResultPageWeb<?> searchResultPage = FlightSearchResultPageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        FlightProductReviewPageWeb<?> productReviewPage = FlightProductReviewPageWeb.getInstance(session);
        FlightBookingFormPageWeb<?> flightBookingFormPage = FlightBookingFormPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);

        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");
        int adultPassengers = Integer.parseInt(param.get("adultCount"));
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String airlineName = param.get("airlineName");
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);

        String username = param.get("username");
        String otp = param.get("otp");
        String password = param.get("password");
        String tripType = param.get("tripType");

        step("Login as " + username + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(username, password);

        step("Change Currency to IDR");
        homePage.selectCurrency("IDR");

        step("User open Flight Landing Page");
        homePage.openVerticalPageViaURL("Flights");
        Assertion.assertThat("Flight Landing Page is Opened", flightLandingPage.isOnFlightPage(), is(true));

        step("User select origin");
        flightLandingPage.selectOrigin(originAirport);

        step("User select destination");
        flightLandingPage.selectDestination(destinationAirport);

        step("Toggle trip type");
        flightLandingPage.toggleTripType(tripType);

        step("Select date");
        flightLandingPage.selectDate(departureDate);

        step("Toggle trip type");
        flightLandingPage.toggleTripType(tripType);

        step("add passenger");
        flightLandingPage.addPassenger(adultPassengers, 0, 0);

        step("User click search button");
        flightLandingPage.searchFlight();
        Assertion.assertThat("Search Result Page is loaded", searchResultPage.isSearchResultPagePresent(), is(true));

        step("Wait for Search Result page to be fully loaded.");
        searchResultPage.isSRPLoaded();

        step("Apply Filter " + airlineName);
        searchResultPage.filterAirline(airlineName);

        step("User Selects Flight Sriwijaya");
        searchResultPage.selectFlightByAirline(airlineName);

        step("User Selects SELECT button in ECONOMY section");
        productReviewPage.clickSelectBtnRegular();

        step("Continue to booking form");
        productReviewPage.continueToBookingForm();
        flightBookingFormPage.clickNoThanksOnFlightMayReschedulePopup();

        step("Click toggle to fill passengers details and save ");
        flightBookingFormPage.fillPassengerDetails();

        step("going to paymentPage");
        flightBookingFormPage.goToPaymentPageFromBookingFormPage();
        flightBookingFormPage.clickNoThanksOnFlightMayReschedulePopup();

        step("click I don't want it");
        flightBookingFormPage.clickOkayGotIt();

        step("Payment page should be displayed");
        Assertion.assertThat("PaymentPageLoaded", paymentPage.isPaymentPagepresent(), is(true));
        String orderID = paymentPage.getOrderId();
        orderID = String.valueOf(getOrderId(orderID));
        Assertion.assertThat("OrderIDVisible " + orderID, !orderID.isEmpty(), is(true));
    }

    @Author(names = "Priyanka")
    @Module(name = "Booking")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void IntlRT2BookingcodeSinglePassenger() throws InterruptedException, FailedLoginException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("IntlRT2BookingcodeSinglePassenger");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPage = FlightLandingPageWeb.getInstance(session);
        FlightSearchResultPageWeb<?> flightSearchResultPageWeb = FlightSearchResultPageWeb.getInstance(session);
        FlightProductReviewPageWeb<?> flightProductReviewPageWeb = FlightProductReviewPageWeb.getInstance(session);
        FlightBookingFormPageWeb<?> flightBookingFormPageWeb = FlightBookingFormPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPageWeb = PaymentPageWeb.getInstance(session);

        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int duration = Integer.parseInt(param.get("duration"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate,daysToDepartureDate);
        String returnDate = CoreFunctions.addToLongDate(departureDate, duration);

        String username = param.get("username");
        String otp = param.get("otp");
        String passengerName = param.get("passengerName");
        String password = param.get("password");
        String airlineNameDepart = param.get("airlineNameDepart");
        String airlineNameReturn = param.get("airlineNameReturn");
        String tripType = param.get("tripType");

        int adultPassengers = Integer.parseInt(param.get("adultCount"));
        int childPassengers = Integer.parseInt(param.get("childCount"));
        int infantPassengers = Integer.parseInt(param.get("infantCount"));

        step("Login as " + username + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(username, password);

        step("Change Currency to IDR");
        homePage.selectCurrency("IDR");
        
        step("User open Flight Landing Page");
        homePage.openVerticalPageViaURL("Flights");

        step("Select origin airport");
        flightLandingPage.selectOrigin(originAirport);

        step("Select destination airport");
        flightLandingPage.selectDestination(destinationAirport);

        step("Toggle trip type");
        flightLandingPage.toggleTripType(tripType);

        step("Select date");
        flightLandingPage.selectDate(departureDate,returnDate);

        step("Add passenger");
        flightLandingPage.addPassenger(adultPassengers, childPassengers, infantPassengers);

        step("search flight");
        flightLandingPage.searchFlight();
        Assertion.assertThat("Search Result Page is loaded", flightSearchResultPageWeb.isSearchResultPagePresent(), is(true));

        step("filter airline Depart {" + airlineNameDepart + " }");
        flightSearchResultPageWeb.filterAirline(airlineNameDepart);

        step("select flight for departure");
        flightSearchResultPageWeb.selectFlightByAirline(airlineNameDepart);
        Assertion.assertThat("Search Result Page is loaded", flightSearchResultPageWeb.isSearchResultPagePresent(), is(true));

        step("filter airline Return {" + airlineNameReturn + " }");
        flightSearchResultPageWeb.filterAirline(airlineNameReturn);

        step("select flight for Return");
        flightSearchResultPageWeb.selectFlightByAirline(airlineNameReturn);

        step("select the SELECT Button");
        flightProductReviewPageWeb.clickSelectBtnRegular();

        step("close Reschedule popUp");
        flightBookingFormPageWeb.clickNoThanksOnFlightMayReschedulePopup();

        step("Click toggle to fill passengers details and save ");
        flightBookingFormPageWeb.fillPassengerDetailsLogin(adultPassengers, childPassengers, infantPassengers, "international", airlineNameDepart);

        step("going to paymentPage");
        flightBookingFormPageWeb.goToPaymentPageFromBookingFormPage();

        step("verify Review Passenger's Data");
        flightBookingFormPageWeb.clickItsCorrectOnReviewPassengerDataPopup();

        step("Payment page should be displayed");
        Assertion.assertThat("PaymentPageLoaded", paymentPageWeb.isPaymentPagepresent(), is(true));
        String orderID = paymentPageWeb.getOrderId();
        orderID = String.valueOf(getOrderId(orderID));
        Assertion.assertThat("OrderIDVisible " + orderID, !orderID.isEmpty(), is(true));
    }

    @Author(names = {"Rohan"})
    @Module(name = "Booking")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void AeroDiliRTSIntlMultiPaxIntl() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("AeroDiliRTSIntlMultiPaxIntl");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPageWeb = FlightLandingPageWeb.getInstance(session);
        FlightSearchResultPageWeb<?> flightSearchResultPage = FlightSearchResultPageWeb.getInstance(session);
        FlightProductReviewPage<?> flightProductReviewPage = new FlightProductReviewPage<>(session);
        FlightBookingFormPageWeb<?> flightBookingFormPage = FlightBookingFormPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        String tripType = param.get("tripType");
        String originAirport = param.get("origin");
        String destinationAirport = param.get("destination");
        String airlineNameDepart = param.get("airlineNameDepart");
        String airlineNameReturn = param.get("airlineNameReturn");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int duration = Integer.parseInt(param.get("duration"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate,daysToDepartureDate);
        String returnDate = CoreFunctions.addToLongDate(departureDate, duration);

        int adultPassengers = Integer.parseInt(param.get("adultPassengers"));
        int childPassengers = Integer.parseInt(param.get("childPassengers"));
        int infantPassengers = Integer.parseInt(param.get("infantPassengers"));

        String username = param.get("username");
        String password = param.get("password");
        String currency = param.get("currency");
        String otp = param.get("otp");

        step("Login as " + username + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(username, password);

        step("User open Flight vertical");
        homePage.openVerticalPageViaURL("Flights");

        step("Toggle trip type");
        flightLandingPageWeb.toggleTripType(tripType);

        step("Select origin airport");
        flightLandingPageWeb.selectOrigin(originAirport);

        step("Select destination airport");
        flightLandingPageWeb.selectDestination(destinationAirport);

        step("Select date");
        flightLandingPageWeb.selectDate(departureDate,returnDate);

        step("add passenger");
        flightLandingPageWeb.addPassenger(adultPassengers,childPassengers,infantPassengers);

        step("search flight");
        flightLandingPageWeb.searchFlight();

        step("Apply Filter " + airlineNameDepart);
        flightSearchResultPage.filterAirline(airlineNameDepart);

        step("select flight");
        flightSearchResultPage.selectFlightByAirline(airlineNameDepart);

        step("Apply Filter " + airlineNameReturn);
        flightSearchResultPage.filterAirline(airlineNameReturn);

        step("select flight");
        flightSearchResultPage.selectFlightByAirline(airlineNameReturn);

        step("Continue to booking form");
        flightProductReviewPage.continueToBookingForm();
        flightBookingFormPage.clickNoThanksOnFlightMayReschedulePopupPRP();

        step("Click toggle to fill passengers details and save ");
        flightBookingFormPage.fillPassengerDetailsLogin(adultPassengers,childPassengers,infantPassengers,"international",airlineNameDepart);

        step("Go to payment page");
        flightBookingFormPage.goToPaymentPageFromBookingFormPage();

        step("Skip Flight may rescheduled page by clicking no thanks");
        flightBookingFormPage.clickNoThanksOnFlightMayReschedulePopup();

        step("click I don't want it");
        flightBookingFormPage.clickOkayGotIt();
        flightBookingFormPage.clickGotItInBaggagePopup();

        step("Payment page should be displayed");
        String orderID = paymentPage.getOrderId();
        orderID = String.valueOf(getOrderId(orderID));
        Assertion.assertThat("OrderIDVisible " + orderID, !orderID.isEmpty(), is(true));
    }

    @Author(names = "Gavin")
    @Module(name = "flightStatus")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void testFlightStatusLiveActivityScheduledState() throws Exception {
        final int RETRY_LOGIN_COUNTER = 3;
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testFlightStatusLiveActivityScheduledState");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        FlightPage<?> flightPage = new FlightPage<>(session);
        FlightStatusPageWeb<?> flightStatusPageWeb = FlightStatusPageWeb.getInstance(session);
        FlightApi<?> flightApi = new FlightApi<>(session);

        String origin = param.get("origin");
        String destination = param.get("destination");

        int daysToDepartureDate = ThreadLocalRandom.current().nextInt(3, 30);

        String currentDate = CoreFunctions.getCurrentDateYYYY_MM_DD();
        String departureDate = CoreFunctions.addToDateYYYY_MM_DD(currentDate, daysToDepartureDate);

        String username = param.get("username");
        String otp = param.get("otp");
        String password = param.get("password");
        String phoneNumber = param.get("phoneNumber");
        String airlineNameDepart = param.get("airlineNameDepart");
        String tripType = param.get("searchType");

        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));

        String[] passengerNames = {CoreFunctions.randomNameWithoutTest() + " Testing"};

        step("Create Booking Order via API");
        String orderId = flightApi.createOrderFlightB2CDomestic(
                username,
                password,
                origin,
                destination,
                departureDate,
                tripType,
                adultPassengers,
                childPassengers,
                infantPassengers,
                airlineNameDepart,
                passengerNames,
                phoneNumber
        );
        Assertion.assertThat("Verify order is created", orderId, not(""));

        step("Get Order Details from Order Id");
        FlightOrderDetailResponse orderDetail = flightApi.getOrderDetail(orderId);

        String bookingCode = orderDetail.data.bookResponses.get(0).bookDetail.bookingCode;

        step("Set Ticket Number on flight dashboard and e-voucher will be issued");
        Boolean isTicketIssued = flightApi.flightEticketIssuedRequest(orderId, bookingCode, username, passengerNames[0], 20);
        Assertion.assertThat("Verify Eticket Issued", isTicketIssued, is(true));


        step("Get Flight Live Activity");
        Assertion.assertThat("Verify live activity is empty",
                flightApi.checkFlightLiveActivity(
                        orderDetail.data.origin,
                        orderDetail.data.destination,
                        orderDetail.data.bookResponses.get(0).integratorBookRequest.flightSelect,
                        orderDetail.data.departureDate,
                        "CREATE"
                ), is(true));

        step("Hit variflight callback with status 'schedule'");
        boolean callbackStatus = flightApi.updateSpecificFlightStatusViaCallback(
                orderDetail.data.origin,
                orderDetail.data.destination,
                orderDetail.data.bookResponses.get(0).integratorBookRequest.flightSelect,
                orderDetail.data.departureDate,
                orderDetail.data.bookResponses.get(0).bookDetail.itineraries.get(0).schedules.get(0).arrivalDate,
                "schedule"
        );
        Assertion.assertThat("Verify callback endpoint hit is success", callbackStatus, is(true));

        step("User open home page");
        homePage.goToHomePage();

        step("Logging in using {" + username + "}");
        loginPage.loginIfNotWithSameEmail(username, password, otp, RETRY_LOGIN_COUNTER);

        step("User open Flight Status Page and verify status");
        homePage.openFlightStatusOrderPage(orderId);
        Assertion.assertThat("Verify flight status is 'On Schedule'",
                flightStatusPageWeb.getFlightStatusOnSchedule(), is(true));
    }

    @Author(names = "Jacky")
    @Module(name = "Booking")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void verifyPassportBannerandPassportConfirmationPopup() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyPassportBannerandPassportConfirmationPopup");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPage =  FlightLandingPageWeb.getInstance(session);
        FlightSearchResultPageWeb<?> searchResultPage =  FlightSearchResultPageWeb.getInstance(session);
        FlightProductReviewPageWeb<?> flightProductReviewPageWeb = FlightProductReviewPageWeb.getInstance(session);
        FlightBookingFormPageWeb<?> flightBookingFormPageWeb = FlightBookingFormPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPageWeb=PaymentPageWeb.getInstance(session);

        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));

        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate,daysToDepartureDate);
        int duration = Integer.parseInt(param.get("duration"));
        int adult = Integer.parseInt(param.get("adult"));
        String returnDate = CoreFunctions.addToLongDate(departureDate, duration);
        String username = param.get("username");
        String otp = param.get("otp");
        String password = param.get("password");
        String airlineName = param.get("airlineName");
        String tripType = param.get("tripType");

        step("Login as " + username + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(username, password);

        step("User open Flight Landing Page");
        homePage.openVerticalPageViaURL("Flights");
        Assertion.assertThat("Flight Landing Page is Opened", flightLandingPage.isOnFlightPage(), is(true));

        step("Toggle trip type");
        flightLandingPage.toggleTripType(tripType);

        step("Select origin airport");
        flightLandingPage.selectOrigin(originAirport);

        step("Select destination airport");
        flightLandingPage.selectDestination(destinationAirport);

        step("Select date");
        flightLandingPage.selectDate(departureDate);

        step("Toggle trip type");
        flightLandingPage.toggleTripType(tripType);

        step("Set passenger count");
        flightLandingPage.addPassenger(adult, 0, 0);

        step("search flight");
        flightLandingPage.searchFlight();
        Assertion.assertThat("Search Result Page is loaded", searchResultPage.isSearchResultPagePresent(), is(true));

        step("filter airline {" + airlineName + " }" );
        searchResultPage.filterAirline(airlineName);

        step("select flight for departure");
        searchResultPage.selectFlightByAirline(airlineName);

        step("select the SELECT Button");
        flightProductReviewPageWeb.clickSelectBtnRegular();

        step("close Reschedule popUp");
        flightBookingFormPageWeb.clickNoThanksOnFlightMayReschedulePopup();

        step("Fill passenger details for all passengers");
        flightBookingFormPageWeb.fillPassengerDetailsLogin(adult, 0, 0, "international", airlineName);
        Assertion.assertThat("Passenger form should have no validation errors before continuing",
                flightBookingFormPageWeb.hasPassengerFormValidationErrors(), is(false));

        step("Validate Passport banner is showing");
        Assertion.assertThat("Validate Passport Banner is showing", flightBookingFormPageWeb.isPassportBannerVisibled(), is(true));

        step("Go to payment page");
        flightBookingFormPageWeb.showingBookingPopupConfirmation();
        Assertion.assertThat("Passport confirmation popup is showing", flightBookingFormPageWeb.isPassportConfirmationPopupVisibled(), is(true));
        flightBookingFormPageWeb.goToPaymentPageFromBookingFormPageForOthersPopup();
        flightBookingFormPageWeb.clickNoThanksOnFlightMayReschedulePopup();
        flightBookingFormPageWeb.clickOkayGotIt();

        step("Payment page should be displayed");
        Assertion.assertThat("PaymentPageLoaded", paymentPageWeb.isPaymentPagepresent(), is(true));
        String orderID = paymentPageWeb.getOrderId();
        orderID = String.valueOf(getOrderId(orderID));
        Assertion.assertThat("OrderIDVisible " + orderID, !orderID.isEmpty(), is(true));
    }

    @Author(names = "Harshita")
    @Module(name = "Booking")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void testIntlRTSMultiPaxJQwithAddons()  throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testIntlRTSMultiPaxJQwithAddons");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPage = FlightLandingPageWeb.getInstance(session);
        FlightSearchResultPageWeb<?> searchResultPage = FlightSearchResultPageWeb.getInstance(session);
        FlightProductReviewPageWeb<?> productReviewPage = FlightProductReviewPageWeb.getInstance(session);
        FlightBookingFormPageWeb<?> bookingFormPage =  FlightBookingFormPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPageWeb=PaymentPageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        String origin = param.get("origin");
        String destination = param.get("destination");
        String airline = param.get("airline");
        int adultPassengers = Integer.parseInt(param.get("adultCount"));
        int childPassengers = Integer.parseInt(param.get("childCount"));
        int infantPassengers = Integer.parseInt(param.get("infantCount"));
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        int duration = Integer.parseInt(param.get("duration"));
        String departureDate = CoreFunctions.addToLongDate(currentDate,daysToDepartureDate);
        String returnDate = CoreFunctions.addToLongDate(departureDate, duration);
        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
        String tripType = param.get("tripType");

        int loginAttemptCount = Integer.parseInt(param.get("loginAttemptCount"));

        step("Login as " + username + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(username, password);

        step("User open Flight Landing Page");
        homePage.openVerticalPageViaURL("Flights");
        Assertion.assertThat("Flight Landing Page is Opened", flightLandingPage.isOnFlightPage(), is(true));

        step("Toggle trip type");
        flightLandingPage.toggleTripType(tripType);

        step("User select origin");
        flightLandingPage.selectOrigin(origin);
        bookingFormPage.clickOkayGotIt();

        step("User select destination");
        flightLandingPage.selectDestination(destination);
        bookingFormPage.clickOkayGotIt();

        step("Select date");
        flightLandingPage.selectDate(departureDate,returnDate);

        step("add passenger");
        flightLandingPage.addPassenger(adultPassengers,childPassengers,infantPassengers);

        step("User click search button");
        flightLandingPage.searchFlight();
        Assertion.assertThat("Search Result Page is loaded", searchResultPage.isSearchResultPagePresent(), is(true));

        step("Wait for Search Result page to be fully loaded.");
        searchResultPage.isSRPLoaded();

        step("User choose filter Flight");
        searchResultPage.filterAirline(airline);

        step("select flight departure");
        searchResultPage.selectFlight();

        step("User choose filter Flight for depart");
        searchResultPage.filterAirline(airline);

        step("select flight return");
        searchResultPage.selectFlight();

        step("User is redirected to Product Review Page");
        Assertion.assertThat("Product Review page is visible", productReviewPage.isPresent(), is(true));

        step("User can see the schedule selection by user and can see the details");
        Assertion.assertThat("Departure schedule is displayed", productReviewPage.isDepartureScheduleVisible(), is(true));

        step("User clicks on 'See Other Facilities' hyperlink in flight details");
        productReviewPage.clickSeeOtherFacilities();

        step("User can see the popup with Flight Facilities Section");
        Assertion.assertThat("Facilities popup is displayed.", productReviewPage.isFacilitiesPopupVisible(), is(true));

        step("User selects the 'Reschedule' tab in the popup");
        productReviewPage.clickPenaltyInfoTabReschedule();

        step("User selects the 'PRICE DETAIL' tab in the popup");
        productReviewPage.clickPenaltyInfoTabPriceDetail();

        step("User clicks the close button on penalty info popup");
        productReviewPage.closePopUp();

        step("User clicks 'Select' button on 'Economy' card");
        productReviewPage.clickSelectBtnRegular();

        step("Verify user is directed to Booking Form page");
        bookingFormPage.clickOkayGotIt();
        bookingFormPage.waitForBookingPageToLoad();
        Assertion.assertThat("Booking Form page is visible", bookingFormPage.isBookingFormVisible(), is(true));

        step("Verify flight details are displayed");
        Assertion.assertThat("Flight details are displayed", bookingFormPage.isFlightDetailsVisible(), is(true));

        step("User navigates to Flight Details section");
        bookingFormPage.goOnFlightDetail();

        step("Verify Flight Schedule tab is visible");
        Assertion.assertThat("Flight Schedule tab is displayed", bookingFormPage.isFlightScheduleTabVisible(), is(true));

        step("User Selects Details of Flight Facility");
        bookingFormPage.clickOnFacilitiesDetail();

        step("User can see Flight Facilities");
        Assertion.assertThat("Flight Facility button is visible", bookingFormPage.isFlightFacilityVisible(), is(true));
        bookingFormPage.closeSecondPopup();

        step("User Click see details to see refund reschedule info");
        bookingFormPage.clickOnSeeDetails();

        step("User can see refund reschedule info");
        Assertion.assertThat("Flight Facility button is visible", bookingFormPage.isRRPopUpAppear(), is(true));
        bookingFormPage.closeRRPopUp();

        step("User go on tiket Policies");
        bookingFormPage.closePopUp();

        step("Click toggle to fill passengers details and save ");
        bookingFormPage.fillPassengerDetailsLogin(adultPassengers, childPassengers, infantPassengers, "international", airline);

        step("Add all addons");
        bookingFormPage.scrollToBaggageSection();
        bookingFormPage.selectAddonsBaggageMultiPAX(adultPassengers,childPassengers);
        bookingFormPage.selectSeatAddons();

        step("User taps on chevron icon on price section to view Price Details");
        bookingFormPage.openPriceDetailSection();

        step("User is redirected to Price Detail page");
        Assertion.assertThat("Price Detail Page is displayed", bookingFormPage.isPriceDetailPageVisible(), is(true));

        step("Verify Total Price Displayed");
        Assertion.assertThat("Total price is displayed", bookingFormPage.isTotalPriceDisplayed(), is(true));

        step("User closes the price detail page");
        bookingFormPage.closePopUp();

        step("User is redirected back to Booking Form Page");
        Assertion.assertThat("Booking Form page is visible again", bookingFormPage.isBookingFormVisible(), is(true));

        step("going to paymentPage");
        bookingFormPage.goToPaymentPageFromBookingFormPage();
        bookingFormPage.clickNoThanksOnFlightMayReschedulePopup();

        step("click I don't want it");
        bookingFormPage.clickOkayGotIt();

        step("Payment page should be displayed");
        Assertion.assertThat("PaymentPageLoaded", paymentPageWeb.isPaymentPagepresent(), is(true));
        String orderID = paymentPageWeb.getOrderId();
        orderID = String.valueOf(getOrderId(orderID));
        Assertion.assertThat("OrderIDVisible "+ orderID, !orderID.isEmpty(), is(true));
    }

    @Author(names = {"Rohan"})
    @Module(name = "Booking")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void BatikAirMalaysiaRTSIntlMultiPaxIntl() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("BatikAirMalaysiaRTSIntlMultiPaxIntl");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPageWeb = FlightLandingPageWeb.getInstance(session);
        FlightSearchResultPageWeb<?> flightSearchResultPage = FlightSearchResultPageWeb.getInstance(session);
        FlightProductReviewPage<?> flightProductReviewPage = new FlightProductReviewPage<>(session);
        FlightBookingFormPageWeb<?> flightBookingFormPage = FlightBookingFormPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        String tripType = param.get("tripType");
        String originAirport = param.get("origin");
        String destinationAirport = param.get("destination");
        String airlineNameDepart = param.get("airlineNameDepart");
        String airlineNameReturn = param.get("airlineNameReturn");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int duration = Integer.parseInt(param.get("duration"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate,daysToDepartureDate);
        String returnDate = CoreFunctions.addToLongDate(departureDate, duration);


        int adultPassengers = Integer.parseInt(param.get("adultPassengers"));
        int childPassengers = Integer.parseInt(param.get("childPassengers"));
        int infantPassengers = Integer.parseInt(param.get("infantPassengers"));

        String username = param.get("username");
        String password = param.get("password");
        String currency = param.get("currency");
        String otp = param.get("otp");

        step("Login as " + username + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(username, password);

        step("User open Flight vertical");
        homePage.openVerticalPageViaURL("Flights");

        step("Toggle trip type");
        flightLandingPageWeb.toggleTripType(tripType);

        step("Select origin airport");
        flightLandingPageWeb.selectOrigin(originAirport);

        step("Select destination airport");
        flightLandingPageWeb.selectDestination(destinationAirport);

        step("Select date");
        flightLandingPageWeb.selectDate(departureDate,returnDate);

        step("add passenger");
        flightLandingPageWeb.addPassenger(adultPassengers,childPassengers,infantPassengers);

        step("search flight");
        flightLandingPageWeb.searchFlight();

        step("Apply Filter " + airlineNameDepart);
        flightSearchResultPage.filterAirline(airlineNameDepart);

        step("select flight");
        flightSearchResultPage.selectFlightByAirline(airlineNameDepart);

        step("Apply Filter " + airlineNameReturn);
        flightSearchResultPage.filterAirline(airlineNameReturn);

        step("select flight");
        flightSearchResultPage.selectFlightByAirline(airlineNameReturn);

        step("Continue to booking form");
        flightProductReviewPage.continueToBookingForm();
        flightBookingFormPage.clickNoThanksOnFlightMayReschedulePopupPRP();

        step("Click toggle to fill passengers details and save ");
        flightBookingFormPage.fillPassengerDetailsLogin(adultPassengers,childPassengers,infantPassengers,"international",airlineNameDepart);

        step("Go to payment page");
        flightBookingFormPage.goToPaymentPageFromBookingFormPage();

        step("Skip Flight may rescheduled page by clicking no thanks");
        flightBookingFormPage.clickNoThanksOnFlightMayReschedulePopup();

        step("click I don't want it");
        flightBookingFormPage.clickOkayGotIt();
        flightBookingFormPage.clickGotItInBaggagePopup();

        step("Payment page should be displayed");
        String orderID = paymentPage.getOrderId();
        orderID = String.valueOf(getOrderId(orderID));
        Assertion.assertThat("OrderIDVisible " + orderID, !orderID.isEmpty(), is(true));
    }

    @Author(names = {"Jacky"})
    @Module(name = "Booking")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void FireflyRTSIntlMultiPaxAddons() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("FireflyRTSIntlMultiPaxAddons");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPageWeb = FlightLandingPageWeb.getInstance(session);
        FlightSearchResultPageWeb<?> flightSearchResultPage = FlightSearchResultPageWeb.getInstance(session);
        FlightProductReviewPage<?> flightProductReviewPage = new FlightProductReviewPage<>(session);
        FlightBookingFormPageWeb<?> flightBookingFormPage = FlightBookingFormPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        String tripType = param.get("tripType");
        String originAirport = param.get("origin");
        String destinationAirport = param.get("destination");
        String airlineNameDepart = param.get("airlineNameDepart");
        String airlineNameReturn = param.get("airlineNameReturn");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int duration = Integer.parseInt(param.get("duration"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate,daysToDepartureDate);
        String returnDate = CoreFunctions.addToLongDate(departureDate, duration);


        int adultPassengers = Integer.parseInt(param.get("adultPassengers"));
        int childPassengers = Integer.parseInt(param.get("childPassengers"));
        int infantPassengers = Integer.parseInt(param.get("infantPassengers"));

        String username = param.get("username");
        String password = param.get("password");
        String currency = param.get("currency");

        step("Login as " + username + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(username, password);

        step("Change Currency to { " + currency + " }");
        homePage.selectCurrency(currency);

        step("User open Flight vertical");
        homePage.openVerticalPageViaURL("Flights");

        step("Toggle trip type");
        flightLandingPageWeb.toggleTripType(tripType);

        step("Select origin airport");
        flightLandingPageWeb.selectOrigin(originAirport);

        step("Select destination airport");
        flightLandingPageWeb.selectDestination(destinationAirport);

        step("Select date");
        flightLandingPageWeb.selectDate(departureDate,returnDate);

        step("add passenger");
        flightLandingPageWeb.addPassenger(adultPassengers,childPassengers,infantPassengers);

        step("search flight");
        flightLandingPageWeb.searchFlight();

        step("Apply Filter " + airlineNameDepart);
        flightSearchResultPage.filterAirline(airlineNameDepart);

        step("select flight");
        flightSearchResultPage.selectFlightByAirline(airlineNameDepart);

        step("Apply Filter " + airlineNameReturn);
        flightSearchResultPage.filterAirline(airlineNameReturn);

        step("select flight");
        flightSearchResultPage.selectFlightByAirline(airlineNameReturn);

        step("Continue to booking form");
        flightProductReviewPage.continueToBookingForm();
        flightBookingFormPage.clickNoThanksOnFlightMayReschedulePopupPRP();

        step("Click toggle to fill passengers details and save ");
        flightBookingFormPage.fillPassengerDetailsLogin(adultPassengers,childPassengers,infantPassengers,"international",airlineNameDepart);

        step("User is on Booking Form page at Addons section");
        flightBookingFormPage.scrollToBaggageSection();

        step("User Select Baggages for Passenger");
        flightBookingFormPage.selectAddonsBaggageMultiPAX(adultPassengers,childPassengers);

        step("Select Insurance");
        flightBookingFormPage.insuranceSelector();

        step("Go to payment page");
        flightBookingFormPage.goToPaymentPageFromBookingFormPage();

        step("Skip Flight may rescheduled page by clicking no thanks");
        flightBookingFormPage.clickNoThanksOnFlightMayReschedulePopup();

        step("click I don't want it");
        flightBookingFormPage.clickOkayGotIt();
        flightBookingFormPage.clickGotItInBaggagePopup();

        step("Payment page should be displayed");
        String orderID = paymentPage.getOrderId();
        orderID = String.valueOf(getOrderId(orderID));
        Assertion.assertThat("OrderIDVisible " + orderID, !orderID.isEmpty(), is(true));
    }

    @Author(names = "Gavin")
    @Module(name = "Booking")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void testLoginBookingDirectRTMultiPaxInternationalWithExtraProtection() throws Exception {
        final int RETRY_LOGIN_COUNTER = 3;
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testLoginBookingDirectRTMultiPaxInternationalWithExtraProtection");
        BaseTest.paramsThreadLocal.set(param);

        FlightApi<?> flightApi = new FlightApi<>(session);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPage = FlightLandingPageWeb.getInstance(session);
        FlightSearchResultPageWeb<?> flightSearchResultPageWeb = FlightSearchResultPageWeb.getInstance(session);
        FlightProductReviewPageWeb<?> flightProductReviewPageWeb = FlightProductReviewPageWeb.getInstance(session);
        FlightBookingFormPageWeb<?> flightBookingFormPageWeb = FlightBookingFormPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPageWeb = PaymentPageWeb.getInstance(session);

        int daysToDepartureDate = ThreadLocalRandom.current().nextInt(30, 60);
        int duration = Integer.parseInt(param.get("duration"));

        String origin = param.get("origin");
        String destination = param.get("destination");
        String originFull = param.get("originFull");
        String destinationFull = param.get("destinationFull");

        String currency = param.get("currency");

        String username = param.get("username");
        String otp = param.get("otp");
        String password = param.get("password");

        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));

        String airline = param.get("airline");

        step("Find dynamic international round-trip data with Round-trip Deals and 100% Refund");
        FlightDynamicData flightDynamicData = flightApi.findDateRT(
                username, password,
                origin, destination, daysToDepartureDate, duration,
                adultPassengers, childPassengers, infantPassengers,
                "roundtrip", airline, airline, "", "",
                "", "", currency
        );

        String departureDate = CoreFunctions.convertToLongDate(flightDynamicData.departureDate);
        String returnDate = CoreFunctions.convertToLongDate(flightDynamicData.returnDate);

        step("Login as " + username + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(username, password);

        step("Change Currency to { " + currency + " }");
        homePage.selectCurrency(currency);

        step("User open Flight Landing Page");
        homePage.openVerticalPageViaURL("Flights");
        Assertion.assertThat("Flight Landing Page is Opened", flightLandingPage.isOnFlightPage(), is(true));

        step("Toggle trip type");
        flightLandingPage.toggleTripType("roundTrip");

        step("User select origin");
        flightLandingPage.selectOrigin(origin);
        flightBookingFormPageWeb.clickOkayGotIt();

        step("User select destination");
        flightLandingPage.selectDestination(destination);
        flightBookingFormPageWeb.clickOkayGotIt();

        step("Select date");
        flightLandingPage.selectDate(departureDate,returnDate);

        step("add passenger");
        flightLandingPage.addPassenger(adultPassengers,childPassengers,infantPassengers);

        step("User click search button");
        flightLandingPage.searchFlight();
        Assertion.assertThat("Search Result Page is loaded", flightSearchResultPageWeb.isSearchResultPagePresent(), is(true));

        step("Wait for Search Result page to be fully loaded.");
        flightSearchResultPageWeb.isSRPLoaded();

        step("User Click on Round-trip Deals button");
        flightSearchResultPageWeb.roundTripDealsFilter();

        step("Store departure and arrival time for depart flight");
        String departFlightDepartureTime = flightSearchResultPageWeb.getDepartureOrArrivalTime("departure");
        String departFlightArrivalTime = flightSearchResultPageWeb.getDepartureOrArrivalTime("arrival");

        step("select flight departure");
        flightSearchResultPageWeb.selectFlight();
        Assertion.assertThat("Search Result Page is loaded", flightSearchResultPageWeb.isSearchResultPagePresent(), is(true));

        step("User Click on Round-trip Deals button");
        flightSearchResultPageWeb.roundTripDealsFilter();

        step("Store departure and arrival time for return flight");
        String returnFlightDepartureTime = flightSearchResultPageWeb.getDepartureOrArrivalTime("departure");
        String returnFlightArrivalTime = flightSearchResultPageWeb.getDepartureOrArrivalTime("arrival");

        step("select flight return");
        flightSearchResultPageWeb.selectFlight();

        step("User is redirected to Product Review Page");
        Assertion.assertThat("Product Review page is visible", flightProductReviewPageWeb.isPresent(), is(true));

        step("User can see depart and return airports in PRP");
        Map<String, String> airport = flightProductReviewPageWeb.getDepartureOrArrivalAirport();
        Assertion.assertThat("Verify departure airport", airport.get("departure"), is(originFull));
        Assertion.assertThat("Verify arrival airport", airport.get("arrival"), is(destinationFull));

        step("User can see departure and arrival time of flight in PRP");
        Map<String, List<String>> flightSchedule = flightProductReviewPageWeb.getFlightSchedule();
        Assertion.assertThat("Verify departure depart time", flightSchedule.get("departure").get(0), is(departFlightDepartureTime));
        Assertion.assertThat("Verify departure arrival time", flightSchedule.get("arrival").get(0), is(departFlightArrivalTime));

        Assertion.assertThat("Verify return depart time", flightSchedule.get("departure").get(1), is(returnFlightDepartureTime));
        Assertion.assertThat("Verify return arrival time", flightSchedule.get("arrival").get(1), is(returnFlightArrivalTime));

        step("User can see the schedule selection by user and can see the details");
        Assertion.assertThat("Departure schedule is displayed", flightProductReviewPageWeb.isDepartureScheduleVisible(), is(true));

        step("User clicks on 'See Other Facilities' hyperlink in flight details");
        flightProductReviewPageWeb.clickSeeOtherFacilities();

        step("User can see the popup with Flight Facilities Section");
        Assertion.assertThat("Facilities popup is displayed.", flightProductReviewPageWeb.isFacilitiesPopupVisible(), is(true));

        step("User selects the 'Reschedule' tab in the popup");
        flightProductReviewPageWeb.clickPenaltyInfoTabReschedule();

        step("User selects the 'PRICE DETAIL' tab in the popup");
        flightProductReviewPageWeb.clickPenaltyInfoTabPriceDetail();

        step("User clicks the close button on penalty info popup");
        flightProductReviewPageWeb.closePopUp();

        step("User clicks 'Select' button on 'Economy' card");
        flightProductReviewPageWeb.clickSelectBtnRegular();

        step("Verify user is directed to Booking Form page");
        flightBookingFormPageWeb.clickOkayGotIt();
        flightBookingFormPageWeb.waitForBookingPageToLoad();
        Assertion.assertThat("Booking Form page is visible", flightBookingFormPageWeb.isBookingFormVisible(), is(true));

        step("Verify flight details are displayed");
        Assertion.assertThat("Flight details are displayed", flightBookingFormPageWeb.isFlightDetailsVisible(), is(true));

        step("User navigates to Flight Details section");
        flightBookingFormPageWeb.goOnFlightDetail();

        step("Verify Flight Schedule tab is visible");
        Assertion.assertThat("Flight Schedule tab is displayed", flightBookingFormPageWeb.isFlightScheduleTabVisible(), is(true));

        step("User Click see details to see refund reschedule info");
        flightBookingFormPageWeb.clickOnSeeDetails();

        step("User can see refund reschedule info");
        Assertion.assertThat("Flight Facility button is visible", flightBookingFormPageWeb.isRRPopUpAppear(), is(true));
        flightBookingFormPageWeb.closeRRPopUp();

        step("User go on tiket Policies");
        flightBookingFormPageWeb.closePopUp();

        step("Click toggle to fill passengers details and save ");
        flightBookingFormPageWeb.fillPassengerDetailsLogin(adultPassengers, childPassengers, infantPassengers, "international", airline);

        step("Select Insurance");
        flightBookingFormPageWeb.selectInsurance(currency);

        step("User taps on chevron icon on price section to view Price Details");
        flightBookingFormPageWeb.openPriceDetailSection();

        step("User is redirected to Price Detail page");
        Assertion.assertThat("Price Detail Page is displayed", flightBookingFormPageWeb.isPriceDetailPageVisible(), is(true));

        step("Verify Total Price Displayed");
        Assertion.assertThat("Total price is displayed", flightBookingFormPageWeb.isTotalPriceDisplayed(), is(true));

        step("User closes the price detail page");
        flightBookingFormPageWeb.closePopUp();

        step("User is redirected back to Booking Form");
        Assertion.assertThat("Booking Form page is visible again", flightBookingFormPageWeb.isBookingFormVisible(), is(true));

        step("going to paymentPage");
        flightBookingFormPageWeb.goToPaymentPageFromBookingFormPage();

        step("verify Review Passenger's Data");
        flightBookingFormPageWeb.clickItsCorrectOnReviewPassengerDataPopup();

        step("Payment page should be displayed");
        Assertion.assertThat("PaymentPageLoaded", paymentPageWeb.isPaymentPagepresent(), is(true));
        String orderID = paymentPageWeb.getOrderId();
        orderID = String.valueOf(getOrderId(orderID));
        Assertion.assertThat("OrderID is Visible "+ orderID, !orderID.isEmpty(), is(true));
    }

    @Author(names = {"Rohan"})
    @Module(name = "Booking")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void TransnusaRTSIntlMultiPaxAddons() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("TransnusaRTSIntlMultiPaxAddons");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPageWeb = FlightLandingPageWeb.getInstance(session);
        FlightSearchResultPageWeb<?> flightSearchResultPage = FlightSearchResultPageWeb.getInstance(session);
        FlightProductReviewPage<?> flightProductReviewPage = new FlightProductReviewPage<>(session);
        FlightBookingFormPageWeb<?> flightBookingFormPage = FlightBookingFormPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        String tripType = param.get("tripType");
        String originAirport = param.get("origin");
        String destinationAirport = param.get("destination");
        String airlineNameDepart = param.get("airlineNameDepart");
        String airlineNameReturn = param.get("airlineNameReturn");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int duration = Integer.parseInt(param.get("duration"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate,daysToDepartureDate);
        String returnDate = CoreFunctions.addToLongDate(departureDate, duration);
        int adultPassengers = Integer.parseInt(param.get("adultPassengers"));
        int childPassengers = Integer.parseInt(param.get("childPassengers"));
        int infantPassengers = Integer.parseInt(param.get("infantPassengers"));

        String username = param.get("username");
        String password = param.get("password");

        step("Login as " + username + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(username, password);

        step("User open Flight vertical");
        homePage.openVerticalPageViaURL("Flights");

        step("Toggle trip type");
        flightLandingPageWeb.toggleTripType(tripType);

        step("Select origin airport");
        flightLandingPageWeb.selectOrigin(originAirport);

        step("Select destination airport");
        flightLandingPageWeb.selectDestination(destinationAirport);

        step("Select date");
        flightLandingPageWeb.selectDate(departureDate,returnDate);

        step("add passenger");
        flightLandingPageWeb.addPassenger(adultPassengers,childPassengers,infantPassengers);

        step("search flight");
        flightLandingPageWeb.searchFlight();

        step("Apply Filter " + airlineNameDepart);
        flightSearchResultPage.filterAirline(airlineNameDepart);

        step("select flight");
        flightSearchResultPage.selectFlightByAirline(airlineNameDepart);

        step("Apply Filter " + airlineNameReturn);
        flightSearchResultPage.filterAirline(airlineNameReturn);

        step("select flight");
        flightSearchResultPage.selectFlightByAirline(airlineNameReturn);

        step("Continue to booking form");
        flightProductReviewPage.continueToBookingForm();
        flightBookingFormPage.clickNoThanksOnFlightMayReschedulePopupPRP();

        step("Click toggle to fill passengers details and save ");
        flightBookingFormPage.fillPassengerDetailsLogin(adultPassengers,childPassengers,infantPassengers,"international",airlineNameDepart);

        step("User is on Booking Form page at Addons section");
        flightBookingFormPage.scrollToBaggageSection();

        step("User Select Baggages for Passenger");
        flightBookingFormPage.selectAddonsBaggageMultiPAX(adultPassengers,childPassengers);

        step("Select Insurance");
        flightBookingFormPage.insuranceSelector();

        step("Go to payment page");
        flightBookingFormPage.goToPaymentPageFromBookingFormPage();

        step("Skip Flight may rescheduled page by clicking no thanks");
        flightBookingFormPage.clickNoThanksOnFlightMayReschedulePopup();

        step("click I don't want it");
        flightBookingFormPage.clickOkayGotIt();
        flightBookingFormPage.clickGotItInBaggagePopup();

        step("Payment page should be displayed");
        String orderID = paymentPage.getOrderId();
        orderID = String.valueOf(getOrderId(orderID));
        Assertion.assertThat("OrderIDVisible " + orderID, !orderID.isEmpty(), is(true));
    }

    @Author(names = {"Harshita"})
    @Module(name = "Booking")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void testIntlRTSMultiPaxCitilinkwithAddons()  throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testIntlRTSMultiPaxCitilinkwithAddons");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPage = FlightLandingPageWeb.getInstance(session);
        FlightSearchResultPageWeb<?> searchResultPage = FlightSearchResultPageWeb.getInstance(session);
        FlightProductReviewPageWeb<?> productReviewPage = FlightProductReviewPageWeb.getInstance(session);
        FlightBookingFormPageWeb<?> bookingFormPage =  FlightBookingFormPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPageWeb=PaymentPageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        String origin = param.get("origin");
        String destination = param.get("destination");
        String airline = param.get("airline");
        int adultPassengers = Integer.parseInt(param.get("adultCount"));
        int childPassengers = Integer.parseInt(param.get("childCount"));
        int infantPassengers = Integer.parseInt(param.get("infantCount"));
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        int duration = Integer.parseInt(param.get("duration"));
        String departureDate = CoreFunctions.addToLongDate(currentDate,daysToDepartureDate);
        String returnDate = CoreFunctions.addToLongDate(departureDate, duration);
        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
        String tripType = param.get("tripType");

        int loginAttemptCount = Integer.parseInt(param.get("loginAttemptCount"));

        step("Login as " + username + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(username, password);

        step("Change Currency to IDR");
        homePage.selectCurrency("IDR");

        step("User open Flight Landing Page");
        homePage.openVerticalPageViaURL("Flights");
        Assertion.assertThat("Flight Landing Page is Opened", flightLandingPage.isOnFlightPage(), is(true));

        step("Toggle trip type");
        flightLandingPage.toggleTripType(tripType);

        step("User select origin");
        flightLandingPage.selectOrigin(origin);
        bookingFormPage.clickOkayGotIt();

        step("User select destination");
        flightLandingPage.selectDestination(destination);
        bookingFormPage.clickOkayGotIt();

        step("Select date");
        flightLandingPage.selectDate(departureDate,returnDate);

        step("add passenger");
        flightLandingPage.addPassenger(adultPassengers,childPassengers,infantPassengers);

        step("User click search button");
        flightLandingPage.searchFlight();
        Assertion.assertThat("Search Result Page is loaded", searchResultPage.isSearchResultPagePresent(), is(true));

        step("Wait for Search Result page to be fully loaded.");
        searchResultPage.isSRPLoaded();

        step("User choose filter Flight");
        searchResultPage.filterAirline(airline);

        step("select flight departure");
        searchResultPage.selectFlight();
        Assertion.assertThat("Search Result Page is loaded", searchResultPage.isSearchResultPagePresent(), is(true));

        step("User choose filter Flight for depart");
        searchResultPage.filterAirline(airline);

        step("select flight return");
        searchResultPage.selectFlight();

        step("User is redirected to Product Review Page");
        Assertion.assertThat("Product Review page is visible", productReviewPage.isPresent(), is(true));

        step("User can see the schedule selection by user and can see the details");
        Assertion.assertThat("Departure schedule is displayed", productReviewPage.isDepartureScheduleVisible(), is(true));

        step("User clicks 'Select' button on 'Economy' card");
        productReviewPage.clickSelectBtnRegular();

        step("Verify user is directed to Booking Form page");
        bookingFormPage.clickOkayGotIt();
        bookingFormPage.waitForBookingPageToLoad();
        Assertion.assertThat("Booking Form page is visible", bookingFormPage.isBookingFormVisible(), is(true));

        step("Verify flight details are displayed");
        Assertion.assertThat("Flight details are displayed", bookingFormPage.isFlightDetailsVisible(), is(true));

        step("Click toggle to fill passengers details and save ");
        bookingFormPage.fillPassengerDetailsLogin(adultPassengers, childPassengers, infantPassengers, "international", airline);

        step("Add all addons");
        bookingFormPage.scrollToBaggageSection();
        bookingFormPage.selectAddonsBaggageMultiPAX(adultPassengers,childPassengers);
        bookingFormPage.selectSeatAddons();

        step("User taps on chevron icon on price section to view Price Details");
        bookingFormPage.openPriceDetailSection();

        step("User is redirected to Price Detail page");
        Assertion.assertThat("Price Detail Page is displayed", bookingFormPage.isPriceDetailPageVisible(), is(true));

        step("Verify Total Price Displayed");
        Assertion.assertThat("Total price is displayed", bookingFormPage.isTotalPriceDisplayed(), is(true));

        step("User closes the price detail page");
        bookingFormPage.closePopUp();

        step("User is redirected back to Booking Form");
        Assertion.assertThat("Booking Form page is visible again", bookingFormPage.isBookingFormVisible(), is(true));

        step("going to paymentPage");
        bookingFormPage.goToPaymentPageFromBookingFormPage();

        step("verify Review Passenger's Data");
        bookingFormPage.clickItsCorrectOnReviewPassengerDataPopup();
        bookingFormPage.clickNoThanksOnFlightMayReschedulePopup();

        step("Payment page should be displayed");
        Assertion.assertThat("PaymentPageLoaded", paymentPageWeb.isPaymentPagepresent(), is(true));
        String orderID = paymentPageWeb.getOrderId();
        orderID = String.valueOf(getOrderId(orderID));
        Assertion.assertThat("OrderIDVisible "+ orderID, !orderID.isEmpty(), is(true));
    }

    @Author(names = {"Valonia"})
    @Module(name = "Scoot")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void ScootRTSIntlMultiPaxIntl() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("ScootRTSIntlMultiPaxIntl");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPageWeb = FlightLandingPageWeb.getInstance(session);
        FlightSearchResultPageWeb<?> flightSearchResultPage = FlightSearchResultPageWeb.getInstance(session);
        FlightProductReviewPage<?> flightProductReviewPage = new FlightProductReviewPage<>(session);
        FlightBookingFormPageWeb<?> flightBookingFormPage = FlightBookingFormPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        String tripType = param.get("tripType");
        String originAirport = param.get("origin");
        String destinationAirport = param.get("destination");
        String airlineNameDepart = param.get("airlineNameDepart");
        String airlineNameReturn = param.get("airlineNameReturn");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int duration = Integer.parseInt(param.get("duration"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate,daysToDepartureDate);
        String returnDate = CoreFunctions.addToLongDate(departureDate, duration);

        int adultPassengers = Integer.parseInt(param.get("adultPassengers"));
        int childPassengers = Integer.parseInt(param.get("childPassengers"));
        int infantPassengers = Integer.parseInt(param.get("infantPassengers"));

        String username = param.get("username");
        String password = param.get("password");
        String currency = param.get("currency");
        String otp = param.get("otp");

        step("Login as " + username + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(username, password);

        step("User open Flight vertical");
        homePage.openVerticalPageViaURL("Flights");

        step("Toggle trip type");
        flightLandingPageWeb.toggleTripType(tripType);

        step("Select origin airport");
        flightLandingPageWeb.selectOrigin(originAirport);

        step("Select destination airport");
        flightLandingPageWeb.selectDestination(destinationAirport);

        step("Select date");
        flightLandingPageWeb.selectDate(departureDate,returnDate);

        step("add passenger");
        flightLandingPageWeb.addPassenger(adultPassengers,childPassengers,infantPassengers);

        step("search flight");
        flightLandingPageWeb.searchFlight();

        step("Apply Filter " + airlineNameDepart);
        flightSearchResultPage.filterAirline(airlineNameDepart);

        step("select flight");
        flightSearchResultPage.selectFlightByAirline(airlineNameDepart);

        step("Apply Filter " + airlineNameReturn);
        flightSearchResultPage.filterAirline(airlineNameReturn);

        step("select flight");
        flightSearchResultPage.selectFlightByAirline(airlineNameReturn);

        step("Continue to booking form");
        flightProductReviewPage.continueToBookingForm();
        flightBookingFormPage.clickNoThanksOnFlightMayReschedulePopupPRP();

        step("Click toggle to fill passengers details and save ");
        flightBookingFormPage.fillPassengerDetailsLogin(adultPassengers,childPassengers,infantPassengers,"international",airlineNameDepart);

        step("User is on Booking Form page at Addons section");
        flightBookingFormPage.scrollToBaggageSection();

        step("User Select Baggages for Passenger");
        flightBookingFormPage.selectAddonsBaggageMultiPAX(adultPassengers,childPassengers);

        step("Go to payment page");
        flightBookingFormPage.goToPaymentPageFromBookingFormPage();

        step("Skip Flight may rescheduled page by clicking no thanks");
        flightBookingFormPage.clickNoThanksOnFlightMayReschedulePopup();

        step("click I don't want it");
        flightBookingFormPage.clickOkayGotIt();
        flightBookingFormPage.clickGotItInBaggagePopup();

        step("Payment page should be displayed");
        String orderID = paymentPage.getOrderId();
        orderID = String.valueOf(getOrderId(orderID));
        Assertion.assertThat("OrderIDVisible " + orderID, !orderID.isEmpty(), is(true));
    }

    @Author(names = {"Rohan"})
    @Module(name = "Garuda")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void GarudaRTSMultiPaxDomestic() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("GarudaRTSMultiPaxDomestic");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPageWeb = FlightLandingPageWeb.getInstance(session);
        FlightSearchResultPageWeb<?> flightSearchResultPage = FlightSearchResultPageWeb.getInstance(session);
        FlightProductReviewPage<?> flightProductReviewPage = new FlightProductReviewPage<>(session);
        FlightBookingFormPageWeb<?> flightBookingFormPage = FlightBookingFormPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        String tripType = param.get("tripType");
        String originAirport = param.get("origin");
        String destinationAirport = param.get("destination");
        String airlineNameDepart = param.get("airlineNameDepart");
        String airlineNameReturn = param.get("airlineNameReturn");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int duration = Integer.parseInt(param.get("duration"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate,daysToDepartureDate);
        String returnDate = CoreFunctions.addToLongDate(departureDate, duration);
        String otp = param.get("otp");

        int adultPassengers = Integer.parseInt(param.get("adultPassengers"));
        int childPassengers = Integer.parseInt(param.get("childPassengers"));
        int infantPassengers = Integer.parseInt(param.get("infantPassengers"));

        String username = param.get("username");
        String password = param.get("password");

        step("Login as " + username + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(username, password);

        step("Change Currency to IDR");
        homePage.selectCurrency("IDR");

        step("User open Flight vertical");
        homePage.openVerticalPageViaURL("Flights");

        step("Toggle trip type");
        flightLandingPageWeb.toggleTripType(tripType);

        step("Select origin airport");
        flightLandingPageWeb.selectOrigin(originAirport);

        step("Select destination airport");
        flightLandingPageWeb.selectDestination(destinationAirport);

        step("Select date");
        flightLandingPageWeb.selectDate(departureDate,returnDate);

        step("add passenger");
        flightLandingPageWeb.addPassenger(adultPassengers,childPassengers,infantPassengers);

        step("search flight");
        flightLandingPageWeb.searchFlight();

        step("Apply Filter " + airlineNameDepart);
        flightSearchResultPage.filterAirline(airlineNameDepart);

        step("select flight");
        flightSearchResultPage.selectFlightByAirline(airlineNameDepart);

        step("Apply Filter " + airlineNameReturn);
        flightSearchResultPage.filterAirline(airlineNameReturn);

        step("select flight");
        flightSearchResultPage.selectFlightByAirline(airlineNameReturn);

        step("Continue to booking form");
        flightProductReviewPage.continueToBookingForm();
        flightBookingFormPage.clickNoThanksOnFlightMayReschedulePopupPRP();

        step("Click toggle to fill passengers details and save ");
        flightBookingFormPage.fillPassengerDetailsLogin(adultPassengers,childPassengers,infantPassengers,"domestic",airlineNameDepart);

        step("Go to payment page");
        flightBookingFormPage.goToPaymentPageFromBookingFormPage();

        step("Skip Flight may rescheduled page by clicking no thanks");
        flightBookingFormPage.clickNoThanksOnFlightMayReschedulePopup();

        step("click I don't want it");
        flightBookingFormPage.clickOkayGotIt();
        flightBookingFormPage.clickGotItInBaggagePopup();

        step("Payment page should be displayed");
        String orderID = paymentPage.getOrderId();
        orderID = String.valueOf(getOrderId(orderID));
        Assertion.assertThat("OrderIDVisible " + orderID, !orderID.isEmpty(), is(true));
    }

    @Author(names = "Rohan")
    @Module(name = "Booking")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void testNonLoginBookingDirectRTMultiPaxIntlWithExtraProtection() throws Exception {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testNonLoginBookingDirectRTMultiPaxIntlWithExtraProtection");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPage = FlightLandingPageWeb.getInstance(session);
        FlightSearchResultPageWeb<?> flightSearchResultPageWeb = FlightSearchResultPageWeb.getInstance(session);
        FlightProductReviewPageWeb<?> flightProductReviewPageWeb = FlightProductReviewPageWeb.getInstance(session);
        FlightBookingFormPageWeb<?> flightBookingFormPageWeb = FlightBookingFormPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPageWeb = PaymentPageWeb.getInstance(session);

        int daysToDepartureDate = ThreadLocalRandom.current().nextInt(3, 30);
        int daysToReturnDate = ThreadLocalRandom.current().nextInt(3, 10);

        String origin = param.get("origin");
        String destination = param.get("destination");

        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate,daysToDepartureDate);
        String returnDate = CoreFunctions.addToLongDate(departureDate, daysToReturnDate);
        String currency = param.get("currency");
        String FullName = param.get("fullName");
        String salutation = randomSalutation();
        String phoneNo = randomPhone();
        String email = randomEmail();

        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));

        step("User open Home Page");
        homePage.goToHomePage();

        step("Logout via URL");
        loginPage.logoutViaUrl();

        step("User select IDR currency");
        homePage.selectCurrency(currency);

        step("User open Flight Landing Page");
        homePage.openVerticalPageViaURL("Flights");
        Assertion.assertThat("Flight Landing Page is Opened", flightLandingPage.isOnFlightPage(), is(true));

        step("Toggle trip type");
        flightLandingPage.toggleTripType("roundTrip");

        step("User select origin");
        flightLandingPage.selectOrigin(origin);
        flightBookingFormPageWeb.clickOkayGotIt();

        step("User select destination");
        flightLandingPage.selectDestination(destination);
        flightBookingFormPageWeb.clickOkayGotIt();

        step("Select date");
        flightLandingPage.selectDate(departureDate,returnDate);

        step("add passenger");
        flightLandingPage.addPassenger(adultPassengers,childPassengers,infantPassengers);

        step("User click search button");
        flightLandingPage.searchFlight();
        Assertion.assertThat("Search Result Page is loaded", flightSearchResultPageWeb.isSearchResultPagePresent(), is(true));

        step("Wait for Search Result page to be fully loaded.");
        flightSearchResultPageWeb.isSRPLoaded();

        step("User click Direct Flight filter");
        flightSearchResultPageWeb.selectDirectFlightFilter();

        step("User Click on Round-trip Deals button");
        flightSearchResultPageWeb.roundTripDealsFilter();

        step("Store departure and arrival time for depart flight");
        String departFlightDepartureTime = flightSearchResultPageWeb.getDepartureOrArrivalTime("departure");
        String departFlightArrivalTime = flightSearchResultPageWeb.getDepartureOrArrivalTime("arrival");

        step("select flight departure");
        flightSearchResultPageWeb.selectFlight();

        step("User click Direct Flight filter");
        flightSearchResultPageWeb.selectDirectFlightFilter();

        step("User Click on Round-trip Deals button");
        flightSearchResultPageWeb.roundTripDealsFilter();

        step("Store departure and arrival time for return flight");
        String returnFlightDepartureTime = flightSearchResultPageWeb.getDepartureOrArrivalTime("departure");
        String returnFlightArrivalTime = flightSearchResultPageWeb.getDepartureOrArrivalTime("arrival");

        step("select flight return");
        flightSearchResultPageWeb.selectFlight();

        step("User is redirected to Product Review Page");
        Assertion.assertThat("Product Review page is visible", flightProductReviewPageWeb.isPresent(), is(true));

        step("User can see depart and return airports in PRP");
        Map<String, String> airport = flightProductReviewPageWeb.getDepartureOrArrivalAirport();
        Assertion.assertThat("Verify departure airport", airport.get("departure"), is(origin));
        Assertion.assertThat("Verify arrival airport", airport.get("arrival"), is(destination));

        step("User can see departure and arrival time of flight in PRP");
        Map<String, List<String>> flightSchedule = flightProductReviewPageWeb.getFlightSchedule();
        Assertion.assertThat("Verify departure depart time", flightSchedule.get("departure").get(0), is(departFlightDepartureTime));
        Assertion.assertThat("Verify departure arrival time", flightSchedule.get("arrival").get(0), is(departFlightArrivalTime));

        Assertion.assertThat("Verify return depart time", flightSchedule.get("departure").get(1), is(returnFlightDepartureTime));
        Assertion.assertThat("Verify return arrival time", flightSchedule.get("arrival").get(1), is(returnFlightArrivalTime));

        step("User can see the schedule selection by user and can see the details");
        Assertion.assertThat("Departure schedule is displayed", flightProductReviewPageWeb.isDepartureScheduleVisible(), is(true));

        step("User clicks on 'See Other Facilities' hyperlink in flight details");
        flightProductReviewPageWeb.clickSeeOtherFacilities();

        step("User can see the popup with Flight Facilities Section");
        Assertion.assertThat("Facilities popup is displayed.", flightProductReviewPageWeb.isFacilitiesPopupVisible(), is(true));

        step("User selects the 'Reschedule' tab in the popup");
        flightProductReviewPageWeb.clickPenaltyInfoTabReschedule();

        step("User selects the 'PRICE DETAIL' tab in the popup");
        flightProductReviewPageWeb.clickPenaltyInfoTabPriceDetail();

        step("User clicks the close button on penalty info popup");
        flightProductReviewPageWeb.closePopUp();

        step("User clicks 'Select' button on 'Economy' card");
        flightProductReviewPageWeb.clickSelectBtnRegular();

        step("Verify user is directed to Booking Form page");
        flightBookingFormPageWeb.clickOkayGotIt();
        flightBookingFormPageWeb.waitForBookingPageToLoad();
        Assertion.assertThat("Booking Form page is visible", flightBookingFormPageWeb.isBookingFormVisible(), is(true));

        step("Verify flight details are displayed");
        Assertion.assertThat("Flight details are displayed", flightBookingFormPageWeb.isFlightDetailsVisible(), is(true));

        step("User navigates to Flight Details section");
        flightBookingFormPageWeb.goOnFlightDetail();

        step("Verify Flight Schedule tab is visible");
        Assertion.assertThat("Flight Schedule tab is displayed", flightBookingFormPageWeb.isFlightScheduleTabVisible(), is(true));

        step("User Selects Details of Flight Facility");
        flightBookingFormPageWeb.clickOnFacilitiesDetail();

        step("User can see Flight Facilities");
        Assertion.assertThat("Flight Facility button is visible", flightBookingFormPageWeb.isFlightFacilityVisible(), is(true));
        flightBookingFormPageWeb.closeSecondPopup();

        step("User Click see details to see refund reschedule info");
        flightBookingFormPageWeb.clickOnSeeDetails();

        step("User can see refund reschedule info");
        Assertion.assertThat("Flight Facility button is visible", flightBookingFormPageWeb.isRRPopUpAppear(), is(true));
        flightBookingFormPageWeb.closeRRPopUp();

        step("User go on tiket Policies");
        flightBookingFormPageWeb.closePopUp();

        flightBookingFormPageWeb.inputContactDetailsNonLogin(salutation, FullName, phoneNo, email);
        step("Click toggle to fill passengers details and save ");
        flightBookingFormPageWeb.fillPassengerDetailsLogin(adultPassengers, childPassengers, infantPassengers, "international", "");

        step("Select Insurance");
        flightBookingFormPageWeb.insuranceSelector();

        step("User taps on chevron icon on price section to view Price Details");
        flightBookingFormPageWeb.openPriceDetailSection();

        step("User is redirected to Price Detail page");
        Assertion.assertThat("Price Detail Page is displayed", flightBookingFormPageWeb.isPriceDetailPageVisible(), is(true));

        step("Verify Total Price Displayed");
        Assertion.assertThat("Total price is displayed", flightBookingFormPageWeb.isTotalPriceDisplayed(), is(true));

        step("User closes the price detail page");
        flightBookingFormPageWeb.closePopUp();

        step("User is redirected back to Booking Form");
        Assertion.assertThat("Booking Form page is visible again", flightBookingFormPageWeb.isBookingFormVisible(), is(true));

        step("going to paymentPage");
        flightBookingFormPageWeb.goToPaymentPageFromBookingFormPage();
        flightBookingFormPageWeb.clickNoThanksOnFlightMayReschedulePopup();

        step("click I don't want it");
        flightBookingFormPageWeb.clickOkayGotIt();

        step("Payment page should be displayed");
        Assertion.assertThat("PaymentPageLoaded", paymentPageWeb.isPaymentPagepresent(), is(true));
        String orderID = paymentPageWeb.getOrderId();
        orderID = String.valueOf(getOrderId(orderID));
        Assertion.assertThat("OrderIDVisible "+ orderID, !orderID.isEmpty(), is(true));
    }

    @Author(names = {"Gavin"})
    @Module(name = "Booking")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void testSriwijayaLoginBookingDomesticRTSMultiPaxWithAllAddons() throws Exception {
        final int RETRY_LOGIN_COUNTER = 3;
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testSriwijayaLoginBookingDomesticRTSMultiPaxWithAllAddons");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPage = FlightLandingPageWeb.getInstance(session);
        FlightSearchResultPageWeb<?> flightSearchResultPageWeb = FlightSearchResultPageWeb.getInstance(session);
        FlightProductReviewPageWeb<?> flightProductReviewPageWeb = FlightProductReviewPageWeb.getInstance(session);
        FlightBookingFormPageWeb<?> flightBookingFormPageWeb = FlightBookingFormPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPageWeb = PaymentPageWeb.getInstance(session);

        int daysToDepartureDate = ThreadLocalRandom.current().nextInt(60, 90);
        int daysToReturnDate = ThreadLocalRandom.current().nextInt(3, 10);

        String origin = param.get("origin");
        String destination = param.get("destination");

        String airlineNameDepart = param.get("airlineNameDepart");
        String airlineNameReturn = param.get("airlineNameReturn");

        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate,daysToDepartureDate);
        String returnDate = CoreFunctions.addToLongDate(departureDate, daysToReturnDate);

        String username = param.get("username");
        String otp = param.get("otp");
        String password = param.get("password");

        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));

        step("Login as " + username + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(username, password);

        step("Change Currency to IDR");
        homePage.selectCurrency("IDR");

        step("User open Flight Landing Page");
        homePage.openVerticalPageViaURL("Flights");
        Assertion.assertThat("Flight Landing Page is Opened", flightLandingPage.isOnFlightPage(), is(true));

        step("Toggle trip type");
        flightLandingPage.toggleTripType("roundTrip");

        step("User select origin");
        flightLandingPage.selectOrigin(origin);
        flightBookingFormPageWeb.clickOkayGotIt();

        step("User select destination");
        flightLandingPage.selectDestination(destination);
        flightBookingFormPageWeb.clickOkayGotIt();

        step("Select date");
        flightLandingPage.selectDate(departureDate,returnDate);

        step("add passenger");
        flightLandingPage.addPassenger(adultPassengers,childPassengers,infantPassengers);

        step("User click search button");
        flightLandingPage.searchFlight();
        Assertion.assertThat("Search Result Page is loaded", flightSearchResultPageWeb.isSearchResultPagePresent(), is(true));

        step("Wait for Search Result page to be fully loaded.");
        flightSearchResultPageWeb.isSRPLoaded();

        step("Filter airline {" + airlineNameDepart + " }");
        flightSearchResultPageWeb.filterAirline(airlineNameDepart);

        step("Store departure and arrival time for depart flight");
        String departFlightDepartureTime = flightSearchResultPageWeb.getDepartureOrArrivalTime("departure");
        String departFlightArrivalTime = flightSearchResultPageWeb.getDepartureOrArrivalTime("arrival");

        step("select flight departure");
        flightSearchResultPageWeb.selectFlightByAirline(airlineNameDepart);

        step("Filter airline {" + airlineNameReturn + " }");
        flightSearchResultPageWeb.filterAirline(airlineNameReturn);

        step("Store departure and arrival time for return flight");
        String returnFlightDepartureTime = flightSearchResultPageWeb.getDepartureOrArrivalTime("departure");
        String returnFlightArrivalTime = flightSearchResultPageWeb.getDepartureOrArrivalTime("arrival");

        step("select flight return");
        flightSearchResultPageWeb.selectFlightByAirline(airlineNameReturn);

        step("User is redirected to Product Review Page");
        Assertion.assertThat("Product Review page is visible", flightProductReviewPageWeb.isPresent(), is(true));

        step("User can see depart and return airports in PRP");
        Map<String, String> airport = flightProductReviewPageWeb.getDepartureOrArrivalAirport();
        Assertion.assertThat("Verify departure airport", airport.get("departure"), is(origin));
        Assertion.assertThat("Verify arrival airport", airport.get("arrival"), is(destination));

        step("User can see departure and arrival time of flight in PRP");
        Map<String, List<String>> flightSchedule = flightProductReviewPageWeb.getFlightSchedule();
        Assertion.assertThat("Verify departure depart time", flightSchedule.get("departure").get(0), is(departFlightDepartureTime));
        Assertion.assertThat("Verify departure arrival time", flightSchedule.get("arrival").get(0), is(departFlightArrivalTime));

        Assertion.assertThat("Verify return depart time", flightSchedule.get("departure").get(1), is(returnFlightDepartureTime));
        Assertion.assertThat("Verify return arrival time", flightSchedule.get("arrival").get(1), is(returnFlightArrivalTime));

        step("User can see the schedule selection by user and can see the details");
        Assertion.assertThat("Departure schedule is displayed", flightProductReviewPageWeb.isDepartureScheduleVisible(), is(true));

        step("User clicks on 'See Other Facilities' hyperlink in flight details");
        flightProductReviewPageWeb.clickSeeOtherFacilities();

        step("User can see the popup with Flight Facilities Section");
        Assertion.assertThat("Facilities popup is displayed.", flightProductReviewPageWeb.isFacilitiesPopupVisible(), is(true));

        step("User selects the 'Reschedule' tab in the popup");
        flightProductReviewPageWeb.clickPenaltyInfoTabReschedule();

        step("User selects the 'PRICE DETAIL' tab in the popup");
        flightProductReviewPageWeb.clickPenaltyInfoTabPriceDetail();

        step("User clicks the close button on penalty info popup");
        flightProductReviewPageWeb.closePopUp();

        step("User clicks 'Select' button on 'Economy' card");
        flightProductReviewPageWeb.clickSelectBtnRegular();

        step("Verify user is directed to Booking Form page");
        flightBookingFormPageWeb.clickOkayGotIt();
        flightBookingFormPageWeb.waitForBookingPageToLoad();
        Assertion.assertThat("Booking Form page is visible", flightBookingFormPageWeb.isBookingFormVisible(), is(true));

        step("Verify flight details are displayed");
        Assertion.assertThat("Flight details are displayed", flightBookingFormPageWeb.isFlightDetailsVisible(), is(true));

        step("User navigates to Flight Details section");
        flightBookingFormPageWeb.goOnFlightDetail();

        step("Verify Flight Schedule tab is visible");
        Assertion.assertThat("Flight Schedule tab is displayed", flightBookingFormPageWeb.isFlightScheduleTabVisible(), is(true));

        step("User Selects Details of Flight Facility");
        flightBookingFormPageWeb.clickOnFacilitiesDetail();

        step("User can see Flight Facilities");
        Assertion.assertThat("Flight Facility button is visible", flightBookingFormPageWeb.isFlightFacilityVisible(), is(true));
        flightBookingFormPageWeb.closeSecondPopup();

        step("User Click see details to see refund reschedule info");
        flightBookingFormPageWeb.clickOnSeeDetails();

        step("User can see refund reschedule info");
        Assertion.assertThat("Flight Facility button is visible", flightBookingFormPageWeb.isRRPopUpAppear(), is(true));
        flightBookingFormPageWeb.closeRRPopUp();

        step("User go on tiket Policies");
        flightBookingFormPageWeb.closePopUp();

        step("Click toggle to fill passengers details and save ");
        flightBookingFormPageWeb.fillPassengerDetailsLogin(adultPassengers, childPassengers, infantPassengers, "domestic", airlineNameDepart);

        step("Select Baggage Addon");
        flightBookingFormPageWeb.scrollToBaggageSection();
        flightBookingFormPageWeb.selectAddonsBaggage();

        step("Select Seat Addon");
        flightBookingFormPageWeb.selectSeatAddons();

        step("User taps on chevron icon on price section to view Price Details");
        flightBookingFormPageWeb.openPriceDetailSection();

        step("User is redirected to Price Detail page");
        Assertion.assertThat("Price Detail Page is displayed", flightBookingFormPageWeb.isPriceDetailPageVisible(), is(true));

        step("Verify Total Price Displayed");
        Assertion.assertThat("Total price is displayed", flightBookingFormPageWeb.isTotalPriceDisplayed(), is(true));

        step("User closes the price detail page");
        flightBookingFormPageWeb.closePopUp();

        step("User is redirected back to Booking Form");
        Assertion.assertThat("Booking Form page is visible again", flightBookingFormPageWeb.isBookingFormVisible(), is(true));

        step("Going to paymentPage");
        flightBookingFormPageWeb.goToPaymentPageFromBookingFormPage();
        flightBookingFormPageWeb.clickNoThanksOnFlightMayReschedulePopup();

        step("click I don't want it");
        flightBookingFormPageWeb.clickOkayGotIt();

        step("Payment page should be displayed");
        Assertion.assertThat("PaymentPageLoaded", paymentPageWeb.isPaymentPagepresent(), is(true));
        String orderID = paymentPageWeb.getOrderId();
        orderID = String.valueOf(getOrderId(orderID));
        Assertion.assertThat("OrderIDVisible "+ orderID, !orderID.isEmpty(), is(true));
    }

    @Author(names = {"Gavin", "Alden"})
    @Module(name = "Booking")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void testLoginBookingTransitOneWayMultiPaxInternationalWithAddonSeatAndNonIdrCurrency() throws Exception {
        final int RETRY_LOGIN_COUNTER = 3;
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testLoginBookingTransitOneWayMultiPaxInternationalWithAddonSeatAndNonIdrCurrency");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPage = FlightLandingPageWeb.getInstance(session);
        FlightSearchResultPageWeb<?> flightSearchResultPageWeb = FlightSearchResultPageWeb.getInstance(session);
        FlightProductReviewPageWeb<?> flightProductReviewPageWeb = FlightProductReviewPageWeb.getInstance(session);
        FlightBookingFormPageWeb<?> flightBookingFormPageWeb = FlightBookingFormPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPageWeb = PaymentPageWeb.getInstance(session);
        FlightApi<?> flightApi = new FlightApi<>(session);

        String origin = param.get("origin");
        String destination = param.get("destination");
        String originFull = param.get("originFull");
        String destinationFull = param.get("destinationFull");
        String airlineNameDepart = param.get("airlineNameDepart");

        String username = param.get("username");
        String otp = param.get("otp");
        String password = param.get("password");
        String tripType = param.get("tripType");
        String currency = param.get("currency");
        String optionalLocation = param.get("optionalLocation") != null ? param.get("optionalLocation") : "";
        String optionalAirline = param.get("optionalAirline") != null ? param.get("optionalAirline") : "";

        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));

        step("Find dynamic data");
        FlightDynamicData flightDynamicData = flightApi.findDate(
                username, password,
                origin, destination, daysToDepartureDate,
                adultPassengers, childPassengers, infantPassengers,
                tripType, airlineNameDepart, "", optionalLocation, optionalAirline, currency
        );

        String departureDate = CoreFunctions.convertToLongDate(flightDynamicData.departureDate);

        step("Login as " + username + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(username, password);

        step("Change Currency to non IDR");
        homePage.selectCurrency(currency);

        step("User open Flight Landing Page");
        homePage.openVerticalPageViaURL("Flights");
        Assertion.assertThat("Flight Landing Page is Opened", flightLandingPage.isOnFlightPage(), is(true));

        step("Toggle trip type");
        flightLandingPage.toggleTripType(tripType);

        step("User select origin");
        flightLandingPage.selectOrigin(origin);
        flightBookingFormPageWeb.clickOkayGotIt();

        step("User select destination");
        flightLandingPage.selectDestination(destination);
        flightBookingFormPageWeb.clickOkayGotIt();

        step("Select date");
        flightLandingPage.selectDate(departureDate);

        step("add passenger");
        flightLandingPage.addPassenger(adultPassengers,childPassengers,infantPassengers);

        step("User click search button");
        flightLandingPage.searchFlight();
        Assertion.assertThat("Search Result Page is loaded", flightSearchResultPageWeb.isSearchResultPagePresent(), is(true));

        step("Wait for Search Result page to be fully loaded.");
        flightSearchResultPageWeb.isSRPLoaded();

        step("User click Transit Flight filter");
        flightSearchResultPageWeb.selectFlightWithSingleTransit();

        step("Filter airline {" + airlineNameDepart + " }");
        flightSearchResultPageWeb.filterAirline(airlineNameDepart);

        step("Select flight for departure");
        flightSearchResultPageWeb.selectFlightByAirline(airlineNameDepart);

        step("User is redirected to Product Review Page");
        Assertion.assertThat("Product Review page is visible", flightProductReviewPageWeb.isPresent(), is(true));

        step("User can see depart and return airports in PRP");
        Map<String, String> airport = flightProductReviewPageWeb.getDepartureOrArrivalAirport();
        Assertion.assertThat("Verify departure airport", airport.get("departure"), is(originFull));
        Assertion.assertThat("Verify arrival airport", airport.get("arrival"), is(destinationFull));

        step("User can see the schedule selection by user and can see the details");
        Assertion.assertThat("Departure schedule is displayed", flightProductReviewPageWeb.isDepartureScheduleVisible(), is(true));

        step("Given user click See Detail on one PRP fare card");
        String price = flightProductReviewPageWeb.getPriceOnFareCard();
        flightProductReviewPageWeb.clickSeeDetailsUnderEconomyCard();
        Assertion.assertThat("Fare card price matches adult price in price detail", flightProductReviewPageWeb.getAdultPriceBreakdown(), equalTo(price));
        String totalPrice = flightProductReviewPageWeb.getTotalPriceOnFareCard();
        Assertion.assertThat("Total price matches the sum of all the prices", flightProductReviewPageWeb.checkTotalPriceBreakdown(), equalTo(totalPrice));

        step("User clicks on 'See Other Facilities' hyperlink in flight details");
        flightProductReviewPageWeb.clickSeeOtherFacilities();

        step("User can see the popup with Flight Facilities Section");
        Assertion.assertThat("Facilities popup is displayed.", flightProductReviewPageWeb.isFacilitiesPopupVisible(), is(true));

        step("User selects the 'Reschedule' tab in the popup");
        flightProductReviewPageWeb.clickPenaltyInfoTabReschedule();

        step("User selects the 'PRICE DETAIL' tab in the popup");
        flightProductReviewPageWeb.clickPenaltyInfoTabPriceDetail();

        step("User clicks the close button on penalty info popup");
        flightProductReviewPageWeb.closePopUp();

        step("User clicks 'Select' button on 'Economy' card");
        flightProductReviewPageWeb.clickSelectBtnRegular();

        step("Verify user is directed to Booking Form page");
        flightBookingFormPageWeb.clickOkayGotIt();
        flightBookingFormPageWeb.waitForBookingPageToLoad();
        Assertion.assertThat("Booking Form page is visible", flightBookingFormPageWeb.isBookingFormVisible(), is(true));

        step("Verify flight details are displayed");
        Assertion.assertThat("Flight details are displayed", flightBookingFormPageWeb.isFlightDetailsVisible(), is(true));

        step("User navigates to Flight Details section");
        flightBookingFormPageWeb.goOnFlightDetail();

        step("Verify Flight Schedule tab is visible");
        Assertion.assertThat("Flight Schedule tab is displayed", flightBookingFormPageWeb.isFlightScheduleTabVisible(), is(true));

        step("User Click see details to see refund reschedule info");
        flightBookingFormPageWeb.clickOnSeeDetails();

        step("User can see refund reschedule info");
        Assertion.assertThat("Flight Facility button is visible", flightBookingFormPageWeb.isRRPopUpAppear(), is(true));
        flightBookingFormPageWeb.closeRRPopUp();

        step("User go on tiket Policies");
        flightBookingFormPageWeb.closePopUp();

        step("Click toggle to fill passengers details and save ");
        flightBookingFormPageWeb.fillPassengerDetailsLogin(adultPassengers, childPassengers, infantPassengers, "international", airlineNameDepart);
        if (Configs.PLATFORM == Platform.AMWEB || Configs.PLATFORM == Platform.IMWEB) {
            flightBookingFormPageWeb.clickContinue();
        }

        step("Select Seat Addon");
        flightBookingFormPageWeb.selectSeatAddons();

        step("User taps on chevron icon on price section to view Price Details");
        flightBookingFormPageWeb.openPriceDetailSection();

        step("User is redirected to Price Detail page");
        Assertion.assertThat("Price Detail Page is displayed", flightBookingFormPageWeb.isPriceDetailPageVisible(), is(true));

        step("Verify Total Price Displayed");
        Assertion.assertThat("Total price is displayed", flightBookingFormPageWeb.isTotalPriceDisplayed(), is(true));

        step("User closes the price detail page");
        flightBookingFormPageWeb.closePopUp();

        step("User is redirected back to Booking Form");
        Assertion.assertThat("Booking Form page is visible again", flightBookingFormPageWeb.isBookingFormVisible(), is(true));

        step("going to paymentPage");
        flightBookingFormPageWeb.goToPaymentPageFromAddonsPage();
        flightBookingFormPageWeb.clickNoThanksOnFlightMayReschedulePopup();

        step("click I don't want it");
        flightBookingFormPageWeb.clickOkayGotIt();

        step("Payment page should be displayed");
        Assertion.assertThat("PaymentPageLoaded", paymentPageWeb.isPaymentPagepresent(), is(true));
        String orderID = paymentPageWeb.getOrderId();
        orderID = String.valueOf(getOrderId(orderID));
        Assertion.assertThat("OrderID is Visible "+ orderID, !orderID.isEmpty(), is(true));
    }

    @Author(names = "Jacky")
    @Module(name = "Booking")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void testNonLoginBookingTransitOneWayMultiPaxInternationalWithAddonSeatAndNonIdrCurrency() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testNonLoginBookingTransitOneWayMultiPaxInternationalWithAddonSeatAndNonIdrCurrency");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPage = FlightLandingPageWeb.getInstance(session);
        FlightSearchResultPageWeb<?> flightSearchResultPageWeb = FlightSearchResultPageWeb.getInstance(session);
        FlightProductReviewPageWeb<?> flightProductReviewPageWeb = FlightProductReviewPageWeb.getInstance(session);
        FlightBookingFormPageWeb<?> flightBookingFormPageWeb = FlightBookingFormPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPageWeb = PaymentPageWeb.getInstance(session);

        int daysToDepartureDate = ThreadLocalRandom.current().nextInt(3, 30);

        String origin = param.get("origin");
        String destination = param.get("destination");
        String originFull = param.get("originFull");
        String destinationFull = param.get("destinationFull");
        String airlineNameDepart = param.get("airlineNameDepart");

        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate,daysToDepartureDate);

        String currency = param.get("currency");
        String FullName = param.get("fullName");
        String salutation = randomSalutation();
        String phoneNo = randomPhone();
        String email = randomEmail();

        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));

        step("User open Home Page");
        homePage.goToHomePage();

        step("Logout via URL");
        loginPage.loginAsGuest();

        step("Change Currency to non IDR");
        homePage.selectCurrency(currency);

        step("User open Flight Landing Page");
        homePage.openVerticalPageViaURL("Flights");
        Assertion.assertThat("Flight Landing Page is Opened", flightLandingPage.isOnFlightPage(), is(true));

        step("Toggle trip type");
        flightLandingPage.toggleTripType("oneWay");

        step("User select origin");
        flightLandingPage.selectOrigin(origin);
        flightBookingFormPageWeb.clickOkayGotIt();

        step("User select destination");
        flightLandingPage.selectDestination(destination);
        flightBookingFormPageWeb.clickOkayGotIt();

        step("Select date");
        flightLandingPage.selectDate(departureDate);

        step("Toggle trip type");
        flightLandingPage.toggleTripType("oneWay");

        step("add passenger");
        flightLandingPage.addPassenger(adultPassengers,childPassengers,infantPassengers);

        step("User click search button");
        flightLandingPage.searchFlight();
        Assertion.assertThat("Search Result Page is loaded", flightSearchResultPageWeb.isSearchResultPagePresent(), is(true));

        step("Wait for Search Result page to be fully loaded.");
        flightSearchResultPageWeb.isSRPLoaded();

        step("User click Transit Flight filter");
        flightSearchResultPageWeb.selectFlightWithSingleTransit();

        step("Filter airline {" + airlineNameDepart + " }");
        flightSearchResultPageWeb.filterAirline(airlineNameDepart);

        step("Select flight for departure");
        flightSearchResultPageWeb.selectFlightByAirline(airlineNameDepart);

        step("User is redirected to Product Review Page");
        Assertion.assertThat("Product Review page is visible", flightProductReviewPageWeb.isPresent(), is(true));

        step("User can see depart and return airports in PRP");
        Map<String, String> airport = flightProductReviewPageWeb.getDepartureOrArrivalAirport();
        Assertion.assertThat("Verify departure airport", airport.get("departure"), is(originFull));
        Assertion.assertThat("Verify arrival airport", airport.get("arrival"), is(destinationFull));

        step("User can see the schedule selection by user and can see the details");
        Assertion.assertThat("Departure schedule is displayed", flightProductReviewPageWeb.isDepartureScheduleVisible(), is(true));

        step("User clicks on 'See Other Facilities' hyperlink in flight details");
        flightProductReviewPageWeb.clickSeeOtherFacilities();

        step("User can see the popup with Flight Facilities Section");
        Assertion.assertThat("Facilities popup is displayed.", flightProductReviewPageWeb.isFacilitiesPopupVisible(), is(true));

        step("User selects the 'Reschedule' tab in the popup");
        flightProductReviewPageWeb.clickPenaltyInfoTabReschedule();

        step("User selects the 'PRICE DETAIL' tab in the popup");
        flightProductReviewPageWeb.clickPenaltyInfoTabPriceDetail();

        step("User clicks the close button on penalty info popup");
        flightProductReviewPageWeb.closePopUp();

        step("User clicks 'Select' button on 'Economy' card");
        flightProductReviewPageWeb.clickSelectBtnRegular();

        step("Verify user is directed to Booking Form page");
        flightBookingFormPageWeb.clickOkayGotIt();
        flightBookingFormPageWeb.waitForBookingPageToLoad();
        Assertion.assertThat("Booking Form page is visible", flightBookingFormPageWeb.isBookingFormVisible(), is(true));

        step("Verify flight details are displayed");
        Assertion.assertThat("Flight details are displayed", flightBookingFormPageWeb.isFlightDetailsVisible(), is(true));

        step("User navigates to Flight Details section");
        flightBookingFormPageWeb.goOnFlightDetail();

        step("Verify Flight Schedule tab is visible");
        Assertion.assertThat("Flight Schedule tab is displayed", flightBookingFormPageWeb.isFlightScheduleTabVisible(), is(true));

        step("User Click see details to see refund reschedule info");
        flightBookingFormPageWeb.clickOnSeeDetails();

        step("User can see refund reschedule info");
        Assertion.assertThat("Flight Facility button is visible", flightBookingFormPageWeb.isRRPopUpAppear(), is(true));
        flightBookingFormPageWeb.closeRRPopUp();

        step("User go on tiket Policies");
        flightBookingFormPageWeb.closePopUp();

        step("Input Contact Name");
        flightBookingFormPageWeb.inputContactDetailsNonLogin(salutation, FullName, phoneNo, email);

        step("Click toggle to fill passengers details and save ");
        flightBookingFormPageWeb.fillPassengerDetailsLogin(adultPassengers, childPassengers, infantPassengers, "international", airlineNameDepart);
        if (Configs.PLATFORM == Platform.AMWEB || Configs.PLATFORM == Platform.IMWEB) {
            flightBookingFormPageWeb.clickContinue();
        }

        step("Select Seat Addon");
        flightBookingFormPageWeb.selectSeatAddons();

        step("User taps on chevron icon on price section to view Price Details");
        flightBookingFormPageWeb.openPriceDetailSection();

        step("User is redirected to Price Detail page");
        Assertion.assertThat("Price Detail Page is displayed", flightBookingFormPageWeb.isPriceDetailPageVisible(), is(true));

        step("Verify Total Price Displayed");
        Assertion.assertThat("Total price is displayed", flightBookingFormPageWeb.isTotalPriceDisplayed(), is(true));

        step("User closes the price detail page");
        flightBookingFormPageWeb.closePopUp();

        step("User is redirected back to Booking Form");
        Assertion.assertThat("Booking Form page is visible again", flightBookingFormPageWeb.isBookingFormVisible(), is(true));

        step("going to paymentPage");
        flightBookingFormPageWeb.goToPaymentPageFromAddonsPage();
        flightBookingFormPageWeb.clickNoThanksOnFlightMayReschedulePopup();

        step("click I don't want it");
        flightBookingFormPageWeb.clickOkayGotIt();

        step("Payment page should be displayed");
        Assertion.assertThat("PaymentPageLoaded", paymentPageWeb.isPaymentPagepresent(), is(true));
        String orderID = paymentPageWeb.getOrderId();
        orderID = String.valueOf(getOrderId(orderID));
        Assertion.assertThat("OrderIDVisible "+ orderID, !orderID.isEmpty(), is(true));
    }

    @Author(names = {"Jacky"})
    @Module(name = "AirasiaBerhad")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void AirasiaBerhadRTSIntlMultiPaxAddons() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("AirasiaBerhadRTSIntlMultiPaxAddons");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPageWeb = FlightLandingPageWeb.getInstance(session);
        FlightSearchResultPageWeb<?> flightSearchResultPage = FlightSearchResultPageWeb.getInstance(session);
        FlightProductReviewPage<?> flightProductReviewPage = new FlightProductReviewPage<>(session);
        FlightBookingFormPageWeb<?> flightBookingFormPage = FlightBookingFormPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        String tripType = param.get("tripType");
        String originAirport = param.get("origin");
        String destinationAirport = param.get("destination");
        String airlineNameDepart = param.get("airlineNameDepart");
        String airlineNameReturn = param.get("airlineNameReturn");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int duration = Integer.parseInt(param.get("duration"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate,daysToDepartureDate);
        String returnDate = CoreFunctions.addToLongDate(departureDate, duration);

        int adultPassengers = Integer.parseInt(param.get("adultPassengers"));
        int childPassengers = Integer.parseInt(param.get("childPassengers"));
        int infantPassengers = Integer.parseInt(param.get("infantPassengers"));

        String username = param.get("username");
        String password = param.get("password");
        String currency = param.get("currency");

        step("Login as " + username + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(username, password);

        step("Change Currency to { " + currency + " }");
        homePage.selectCurrency(currency);

        step("User open Flight vertical");
        homePage.openVerticalPageViaURL("Flights");

        step("Toggle trip type");
        flightLandingPageWeb.toggleTripType(tripType);

        step("Select origin airport");
        flightLandingPageWeb.selectOrigin(originAirport);

        step("Select destination airport");
        flightLandingPageWeb.selectDestination(destinationAirport);

        step("Select date");
        flightLandingPageWeb.selectDate(departureDate,returnDate);

        step("add passenger");
        flightLandingPageWeb.addPassenger(adultPassengers,childPassengers,infantPassengers);

        step("search flight");
        flightLandingPageWeb.searchFlight();
        Assertion.assertThat("Search Result Page is loaded", flightSearchResultPage.isSearchResultPagePresent(), is(true));

        step("Apply Filter " + airlineNameDepart);
        flightSearchResultPage.filterAirline(airlineNameDepart);

        step("select flight");
        flightSearchResultPage.selectFlightByAirline(airlineNameDepart);
        Assertion.assertThat("Search Result Page is loaded", flightSearchResultPage.isSearchResultPagePresent(), is(true));

        step("Apply Filter " + airlineNameReturn);
        flightSearchResultPage.filterAirline(airlineNameReturn);

        step("select flight");
        flightSearchResultPage.selectFlightByAirline(airlineNameReturn);

        step("Continue to booking form");
        flightProductReviewPage.continueToBookingForm();
        flightBookingFormPage.clickNoThanksOnFlightMayReschedulePopupPRP();

        step("Click toggle to fill passengers details and save ");
        flightBookingFormPage.fillPassengerDetailsLogin(adultPassengers,childPassengers,infantPassengers,"international",airlineNameDepart);

        step("Add Baggage addons");
        flightBookingFormPage.scrollToBaggageSection();
        flightBookingFormPage.selectAddonsBaggageMultiPAX(adultPassengers,childPassengers);

        step("Select Insurance");
        flightBookingFormPage.selectInsurance(currency);

        step("Go to payment page");
        flightBookingFormPage.goToPaymentPageFromBookingFormPage();

        step("Skip Flight may rescheduled page by clicking no thanks");
        flightBookingFormPage.clickNoThanksOnFlightMayReschedulePopup();

        step("click I don't want it");
        flightBookingFormPage.clickOkayGotIt();
        flightBookingFormPage.clickGotItInBaggagePopup();

        step("Payment page should be displayed");
        String orderID = paymentPage.getOrderId();
        orderID = String.valueOf(getOrderId(orderID));
        Assertion.assertThat("OrderIDVisible " + orderID, !orderID.isEmpty(), is(true));
    }

    @Author(names = "Harshita")
    @Module(name = "Booking")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void testDomRTSMultiPaxIPwithAddons()  throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testDomRTSMultiPaxIPwithAddons");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPage = FlightLandingPageWeb.getInstance(session);
        FlightSearchResultPageWeb<?> searchResultPage = FlightSearchResultPageWeb.getInstance(session);
        FlightProductReviewPageWeb<?> productReviewPage = FlightProductReviewPageWeb.getInstance(session);
        FlightBookingFormPageWeb<?> bookingFormPage =  FlightBookingFormPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPageWeb=PaymentPageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        String origin = param.get("origin");
        String destination = param.get("destination");
        String airline = param.get("airline");
        int adultPassengers = Integer.parseInt(param.get("adultCount"));
        int childPassengers = Integer.parseInt(param.get("childCount"));
        int infantPassengers = Integer.parseInt(param.get("infantCount"));
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        int duration = Integer.parseInt(param.get("duration"));
        String departureDate = CoreFunctions.addToLongDate(currentDate,daysToDepartureDate);
        String returnDate = CoreFunctions.addToLongDate(departureDate, duration);
        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
        String tripType = param.get("tripType");

        int loginAttemptCount = Integer.parseInt(param.get("loginAttemptCount"));

        step("Login as " + username + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(username, password);

        step("User open Flight Landing Page");
        homePage.openVerticalPageViaURL("Flights");
        Assertion.assertThat("Flight Landing Page is Opened", flightLandingPage.isOnFlightPage(), is(true));

        step("Toggle trip type");
        flightLandingPage.toggleTripType(tripType);

        step("User select origin");
        flightLandingPage.selectOrigin(origin);
        bookingFormPage.clickOkayGotIt();

        step("User select destination");
        flightLandingPage.selectDestination(destination);
        bookingFormPage.clickOkayGotIt();

        step("Select date");
        flightLandingPage.selectDate(departureDate,returnDate);

        step("add passenger");
        flightLandingPage.addPassenger(adultPassengers,childPassengers,infantPassengers);

        step("User click search button");
        flightLandingPage.searchFlight();
        Assertion.assertThat("Search Result Page is loaded", searchResultPage.isSearchResultPagePresent(), is(true));

        step("Wait for Search Result page to be fully loaded.");
        searchResultPage.isSRPLoaded();

        step("User choose filter Flight");
        searchResultPage.filterAirline(airline);

        step("select flight departure");
        searchResultPage.selectFlight();

        step("User choose filter Flight for depart");
        searchResultPage.filterAirline(airline);

        step("select flight return");
        searchResultPage.selectFlight();

        step("User is redirected to Product Review Page");
        Assertion.assertThat("Product Review page is visible", productReviewPage.isPresent(), is(true));

        step("User can see the schedule selection by user and can see the details");
        Assertion.assertThat("Departure schedule is displayed", productReviewPage.isDepartureScheduleVisible(), is(true));

        step("User clicks 'Select' button on 'Economy' card");
        productReviewPage.clickSelectBtnRegular();

        step("Verify user is directed to Booking Form page");
        bookingFormPage.clickOkayGotIt();
        bookingFormPage.waitForBookingPageToLoad();
        Assertion.assertThat("Booking Form page is visible", bookingFormPage.isBookingFormVisible(), is(true));

        step("Verify flight details are displayed");
        Assertion.assertThat("Flight details are displayed", bookingFormPage.isFlightDetailsVisible(), is(true));

        step("Click toggle to fill passengers details and save ");
        bookingFormPage.fillPassengerDetailsLogin(adultPassengers, childPassengers, infantPassengers, "domestic", airline);

        step("Add all addons");
        bookingFormPage.scrollToBaggageSection();
        bookingFormPage.selectAddonsBaggageMultiPAX(adultPassengers,childPassengers);
        bookingFormPage.selectSeatAddons();

        step("User taps on chevron icon on price section to view Price Details");
        bookingFormPage.openPriceDetailSection();

        step("User is redirected to Price Detail page");
        Assertion.assertThat("Price Detail Page is displayed", bookingFormPage.isPriceDetailPageVisible(), is(true));

        step("Verify Total Price Displayed");
        Assertion.assertThat("Total price is displayed", bookingFormPage.isTotalPriceDisplayed(), is(true));

        step("User closes the price detail page");
        bookingFormPage.closePopUp();

        step("User is redirected back to Booking Form");
        Assertion.assertThat("Booking Form page is visible again", bookingFormPage.isBookingFormVisible(), is(true));

        step("going to paymentPage");
        bookingFormPage.goToPaymentPageFromBookingFormPage();
        bookingFormPage.clickNoThanksOnFlightMayReschedulePopup();

        step("click I don't want it");
        bookingFormPage.clickOkayGotIt();

        step("Payment page should be displayed");
        Assertion.assertThat("PaymentPageLoaded", paymentPageWeb.isPaymentPagepresent(), is(true));
        String orderID = paymentPageWeb.getOrderId();
        orderID = String.valueOf(getOrderId(orderID));
        Assertion.assertThat("OrderIDVisible "+ orderID, !orderID.isEmpty(), is(true));
    }

    @Author(names = {"Jacky"})
    @Module(name = "Airasia Indonesia")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void AirasiaIndonesiaRTIntlMultiPaxAddons() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("AirasiaIndonesiaRTIntlMultiPaxAddons");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPageWeb = FlightLandingPageWeb.getInstance(session);
        FlightSearchResultPageWeb<?> flightSearchResultPage = FlightSearchResultPageWeb.getInstance(session);
        FlightProductReviewPage<?> flightProductReviewPage = new FlightProductReviewPage<>(session);
        FlightBookingFormPageWeb<?> flightBookingFormPage = FlightBookingFormPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        String tripType = param.get("tripType");
        String originAirport = param.get("origin");
        String destinationAirport = param.get("destination");
        String airlineNameDepart = param.get("airlineNameDepart");
        String airlineNameReturn = param.get("airlineNameReturn");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int duration = Integer.parseInt(param.get("duration"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate,daysToDepartureDate);
        String returnDate = CoreFunctions.addToLongDate(departureDate, duration);


        int adultPassengers = Integer.parseInt(param.get("adultPassengers"));
        int childPassengers = Integer.parseInt(param.get("childPassengers"));
        int infantPassengers = Integer.parseInt(param.get("infantPassengers"));

        String username = param.get("username");
        String password = param.get("password");
        String currency = param.get("currency");

        step("Login as " + username + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(username, password);

        step("Change Currency to { " + currency + " }");
        homePage.selectCurrency(currency);

        step("User open Flight vertical");
        homePage.openVerticalPageViaURL("Flights");

        step("Toggle trip type");
        flightLandingPageWeb.toggleTripType(tripType);

        step("Select origin airport");
        flightLandingPageWeb.selectOrigin(originAirport);

        step("Select destination airport");
        flightLandingPageWeb.selectDestination(destinationAirport);

        step("Select date");
        flightLandingPageWeb.selectDate(departureDate,returnDate);

        step("add passenger");
        flightLandingPageWeb.addPassenger(adultPassengers,childPassengers,infantPassengers);

        step("search flight");
        flightLandingPageWeb.searchFlight();
        Assertion.assertThat("Search Result Page is loaded", flightSearchResultPage.isSearchResultPagePresent(), is(true));

        step("Apply Filter " + airlineNameDepart);
        flightSearchResultPage.filterAirline(airlineNameDepart);

        step("select flight");
        flightSearchResultPage.selectFlightByAirline(airlineNameDepart);
        Assertion.assertThat("Search Result Page is loaded", flightSearchResultPage.isSearchResultPagePresent(), is(true));

        step("Apply Filter " + airlineNameReturn);
        flightSearchResultPage.filterAirline(airlineNameReturn);

        step("select flight");
        flightSearchResultPage.selectFlightByAirline(airlineNameReturn);

        step("Continue to booking form");
        flightProductReviewPage.continueToBookingForm();
        flightBookingFormPage.clickNoThanksOnFlightMayReschedulePopupPRP();

        step("Click toggle to fill passengers details and save ");
        flightBookingFormPage.fillPassengerDetailsLogin(adultPassengers,childPassengers,infantPassengers,"international",airlineNameDepart);

        step("Add Baggage addons");
        flightBookingFormPage.selectAddonsBaggage();

        step("Add Seat addons");
        flightBookingFormPage.selectSeatAddons();

        step("Select Insurance");
        flightBookingFormPage.selectInsurance(currency);

        step("Go to payment page");
        flightBookingFormPage.goToPaymentPageFromBookingFormPage();

        step("Skip Flight may rescheduled page by clicking no thanks");
        flightBookingFormPage.clickNoThanksOnFlightMayReschedulePopup();

        step("click I don't want it");
        flightBookingFormPage.clickOkayGotIt();
        flightBookingFormPage.clickGotItInBaggagePopup();

        step("Payment page should be displayed");
        String orderID = paymentPage.getOrderId();
        orderID = String.valueOf(getOrderId(orderID));
        Assertion.assertThat("OrderIDVisible " + orderID, !orderID.isEmpty(), is(true));
    }

    @Author(names = "Harshita")
    @Module(name = "Flight")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void FLG_testBookingOneWayOnePaxLowestPriceGuarantee()  throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("FLG_testBookingOneWayOnePaxLowestPriceGuarantee");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPage = FlightLandingPageWeb.getInstance(session);
        FlightSearchResultPageWeb<?> searchResultPage = FlightSearchResultPageWeb.getInstance(session);
        FlightProductReviewPageWeb<?> productReviewPage = FlightProductReviewPageWeb.getInstance(session);
        FlightBookingFormPageWeb<?> bookingFormPage =  FlightBookingFormPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPageWeb=PaymentPageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        FlightApi<?> flightApi = new FlightApi<>(session);

        String origin = param.get("origin");
        String destination = param.get("destination");
        int adultPassengers = Integer.parseInt(param.get("adultCount"));
        int childPassengers = Integer.parseInt(param.get("childCount"));
        int infantPassengers = Integer.parseInt(param.get("infantCount"));
        String username = param.get("username");
        String password = param.get("password");
        String airlineName = param.get("airlineName");
        String tripType = param.get("tripType");
        String paymentGroup = param.get("paymentGroup");
        String paymentMethod = param.get("paymentMethod");
        int daysToDepartureDate = ThreadLocalRandom.current().nextInt(30, 60);
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate,daysToDepartureDate);

        step("Find dynamic data");
        FlightDynamicData flightDynamicData = flightApi.findDate(
                username,password,
                origin,destination,daysToDepartureDate,
                adultPassengers,childPassengers,infantPassengers,
                "oneway",airlineName,"","","",""
        );
        origin = flightDynamicData.originAirport;
        destination = flightDynamicData.destinationAirport;
        departureDate = CoreFunctions.convertToLongDate(flightDynamicData.departureDate);
        airlineName = flightDynamicData.airlineDepart;

        step("User open Home Page");
        homePage.goToHomePage();

        step("Go to account Page");
        loginPage.goToAccountPage();

        step("User login");
        loginPage.loginWithSpecificAccountViaCookie(username, password);
        homePage.selectCurrency("IDR");

        step("User open Flight Landing Page");
        homePage.openVerticalPageViaURL("Flights");
        Assertion.assertThat("Flight Landing Page is Opened", flightLandingPage.isOnFlightPage(), is(true));

        step("Toggle trip type "+ tripType);
        flightLandingPage.toggleTripType(tripType);

        step("User select origin");
        flightLandingPage.selectOrigin(origin);

        step("User select destination");
        flightLandingPage.selectDestination(destination);

        step("Select date");
        flightLandingPage.selectDate(departureDate);

        step("add passenger");
        flightLandingPage.addPassenger(adultPassengers,childPassengers,infantPassengers);

        step("User click search button");
        flightLandingPage.searchFlight();
        Assertion.assertThat("Search Result Page is loaded", searchResultPage.isSearchResultPagePresent(), is(true));

        step("Wait for Search Result page to be fully loaded.");
        searchResultPage.isSRPLoaded();

        step("User Click on lowest price Guarantee button");
        searchResultPage.clickLowestPriceGuarantee();

        step("User choose filter Flight");
        searchResultPage.filterAirline(airlineName);

        step("select flight departure");
        searchResultPage.selectFlight();

        step("User is redirected to Product Review Page");
        Assertion.assertThat("Product Review page is visible", productReviewPage.isPresent(), is(true));

        step("User can see the schedule selection by user and can see the details");
        Assertion.assertThat("Departure schedule is displayed", productReviewPage.isDepartureScheduleVisible(), is(true));

        step("User clicks on 'See Other Facilities' hyperlink in flight details");
        productReviewPage.clickSeeOtherFacilitiesRR();

        step("User can see the popup with Flight Facilities Section");
        Assertion.assertThat("Facilities popup is displayed.", productReviewPage.isFacilitiesPopupVisible(), is(true));

        step("User selects the 'Reschedule' tab in the popup");
        productReviewPage.clickPenaltyInfoTabReschedule();

        step("User selects the 'PRICE DETAIL' tab in the popup");
        productReviewPage.clickPenaltyInfoTabPriceDetail();

        step("User clicks the close button on penalty info popup");
        productReviewPage.closePopUp();

        step("User clicks 'Select' button on 'Economy' card");
        productReviewPage.clickSelectBtnRegular();

        step("Verify user is directed to Booking Form page");
        bookingFormPage.clickOkayGotIt();
        bookingFormPage.waitForBookingPageToLoad();
        Assertion.assertThat("Booking Form page is visible", bookingFormPage.isBookingFormVisible(), is(true));

        step("Verify flight details are displayed");
        Assertion.assertThat("Flight details are displayed", bookingFormPage.isFlightDetailsVisible(), is(true));

        step("User navigates to Flight Details section");
        bookingFormPage.goOnFlightDetail();

        step("Verify Flight Schedule tab is visible");
        Assertion.assertThat("Flight Schedule tab is displayed", bookingFormPage.isFlightScheduleTabVisible(), is(true));

        step("User Click see details to see refund reschedule info");
        bookingFormPage.clickOnSeeDetails();

        step("User can see refund reschedule info");
        Assertion.assertThat("Flight Facility button is visible", bookingFormPage.isRRPopUpAppear(), is(true));
        bookingFormPage.closeRRPopUp();

        step("User go on tiket Policies");
        bookingFormPage.closePopUp();

        step("Fill Passenger Details");
        bookingFormPage.fillPassengerDetails();

        step("User delete a character in a middle of contact person's name");
        bookingFormPage.deleteMiddleChar();

        step("User is on Booking Form page at Addons section");
        bookingFormPage.scrollToBaggageSection();

        step("User Select Baggages for Passenger");
        bookingFormPage.addBaggageFromBookingForm();

        step("User taps on chevron icon on price section to view Price Details");
        bookingFormPage.openPriceDetailSection();

        step("User is redirected to Price Detail page");
        Assertion.assertThat("Price Detail Page is displayed", bookingFormPage.isPriceDetailPageVisible(), is(true));

        step("Verify Total Price Displayed");
        Assertion.assertThat("Total price is displayed", bookingFormPage.isTotalPriceDisplayed(), is(true));

        step("User closes the price detail page");
        bookingFormPage.closePopUp();

        step("User is redirected back to Booking Form");
        Assertion.assertThat("Booking Form page is visible again", bookingFormPage.isBookingFormVisible(), is(true));

        step("going to paymentPage");
        bookingFormPage.goToPaymentPageFromBookingFormPage();

        step("verify Review Passenger's Data");
        bookingFormPage.clickItsCorrectOnReviewPassengerDataPopup();
        bookingFormPage.clickNoThanksOnFlightMayReschedulePopupPRP();

        step("Payment page should be displayed");
        Assertion.assertThat("PaymentPageLoaded", paymentPageWeb.isPaymentPagepresent(), is(true));
        String orderID = paymentPageWeb.getOrderId();
        orderID = String.valueOf(getOrderId(orderID));
        Assertion.assertThat("OrderIDVisible "+ orderID, !orderID.isEmpty(), is(true));

        step("Validate all suppliers are mock issued by airline for departure and return, skipped if real issued supplier exist");
        boolean isDepartSuppliersMocked = flightApi.areAllSuppliersMockIssuedByAirline(flightDynamicData.airlineCodeDepart, flightDynamicData.originAirport, flightDynamicData.destinationAirport);

        if (Configs.ENVIRONMENT != Environment.PROD && isDepartSuppliersMocked) {
            step("User select payment method ");
            paymentPageWeb.clickOnSelectPaymentMethod();
            paymentPageWeb.goToPaymentMethodsGroup(paymentGroup);
            paymentPageWeb.selectPaymentMethod(paymentMethod);

            step("Click on Pay button");
            paymentPageWeb.clickOnPaymentButton();

            step("Verify the payment successful page is visible or not");
            Assertion.assertThat("Verify the payment successful page is visible", paymentPageWeb.isPaymentSuccessfulVisible(), is(true));

            step("User try to open see order list");
            paymentPageWeb.clickSeeOrderListButton();

            step("Find the issued order");
            String orderStatus = orderListPage.getOrderStatus(orderID);
            orderListPage.openActiveTicketViaURL(orderID);
            orderDetailPage.skipCoachmark();

            step("Verify flight order details");
            Assertion.assertThat("orderID should be visible and should be same as from payment page", orderDetailPage.getOrderId(), is(orderID));

            Assertion.assertThat("Order status should be ETICKET_ISSUED or PROCESSING_ETICKET",
                    orderStatus.equalsIgnoreCase("ETICKET_ISSUED") || orderStatus.equalsIgnoreCase("PROCESSING_ETICKET"), is(true));
            orderDetailPage.closeEticketPopUp();
        }
    }

    @Author(names = "Jacky")
    @Module(name = "MWeb Booking")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void testMWebBookingOneWayOnePaxLowestPriceGuarantee()  throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testMWebBookingOneWayOnePaxLowestPriceGuarantee");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPage = FlightLandingPageWeb.getInstance(session);
        FlightSearchResultPageWeb<?> flightSearchResultPage = FlightSearchResultPageWeb.getInstance(session);
        FlightProductReviewPageWeb<?> flightProductReviewPage = FlightProductReviewPageWeb.getInstance(session);
        FlightBookingFormPageWeb<?> flightBookingFormPage =  FlightBookingFormPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        String tripType = param.get("tripType");
        String originAirport = param.get("origin");
        String destinationAirport = param.get("destination");
        String airlineNameDepart = param.get("airlineNameDepart");
        String airlineNameReturn = param.get("airlineNameReturn");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int duration = Integer.parseInt(param.get("duration"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate,daysToDepartureDate);
        String returnDate = CoreFunctions.addToLongDate(departureDate, duration);

        int adultPassengers = Integer.parseInt(param.get("adultPassengers"));
        int childPassengers = Integer.parseInt(param.get("childPassengers"));
        int infantPassengers = Integer.parseInt(param.get("infantPassengers"));

        String username = param.get("username");
        String password = param.get("password");
        String currency = param.get("currency");

        step("login in using { " + username + " }");
        loginPage.loginIfNotWithSameEmail(username, password, "123456", 1);

        step("User open Flight vertical");
        homePage.openVerticalPageViaURL("Flights");

        step("Toggle trip type");
        flightLandingPage.toggleTripType(tripType);

        step("Select origin airport");
        flightLandingPage.selectOrigin(originAirport);

        step("Select destination airport");
        flightLandingPage.selectDestination(destinationAirport);

        step("Select date");
        flightLandingPage.selectDate(departureDate);

        step("add passenger");
        flightLandingPage.addPassenger(adultPassengers,childPassengers,infantPassengers);

        step("search flight");
        flightLandingPage.searchFlight();

        step("Wait for Search Result page to be fully loaded.");
        flightSearchResultPage.isSRPLoaded();

        step("User Click on lowest price Guarantee button and Validate lowest Price Guarantee tag showing on schedules");
        flightSearchResultPage.clickLowestPriceGuarantee();

        step("User choose filter Flight");
        flightSearchResultPage.filterAirline(airlineNameDepart);

        step("select flight departure");
        Assertion.assertThat("Lowest Price Guarantee Tag visible", flightSearchResultPage.verifyLowestPriceGuaranteeTag(), is(true));
        flightSearchResultPage.selectFlight();

        step("Continue to booking form");
        flightProductReviewPage.continueToBookingForm();
        flightBookingFormPage.clickNoThanksOnFlightMayReschedulePopupPRP();

        step("Click toggle to fill passengers details and save");
        flightBookingFormPage.clickSameContactToggle();

        step("Go to payment page");
        flightBookingFormPage.goToPaymentPageFromBookingFormPage();

        step("Skip Flight may rescheduled page by clicking no thanks");
        flightBookingFormPage.clickNoThanksOnFlightMayReschedulePopup();

        step("click I don't want it");
        flightBookingFormPage.clickOkayGotIt();
        flightBookingFormPage.clickGotItInBaggagePopup();

        step("Payment page should be displayed");
        String orderID = paymentPage.getOrderId();
        orderID = String.valueOf(getOrderId(orderID));
        Assertion.assertThat("OrderIDVisible " + orderID, !orderID.isEmpty(), is(true));
    }

    @Author(names = "Alden")
    @Module(name = "SRP")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void validateSearchRoundtripSameDay()  throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("validateSearchRoundtripSameDay");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPage = FlightLandingPageWeb.getInstance(session);
        FlightSearchResultPageWeb<?> searchResultPage = FlightSearchResultPageWeb.getInstance(session);
        FlightBookingFormPageWeb<?> bookingFormPage =  FlightBookingFormPageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        String origin = param.get("origin");
        String destination = param.get("destination");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        int duration = Integer.parseInt(param.get("duration"));
        String departureDate = CoreFunctions.addToLongDate(currentDate,daysToDepartureDate);
        String returnDate = CoreFunctions.addToLongDate(departureDate, duration);
        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
        String tripType = param.get("tripType");

        int loginAttemptCount = Integer.parseInt(param.get("loginAttemptCount"));

        step("User login");
        loginPage.loginIfNotWithSameEmail(username, password, otp, loginAttemptCount);

        step("User open Flight Landing Page");
        homePage.openVerticalPageViaURL("Flights");
        Assertion.assertThat("Flight Landing Page is Opened", flightLandingPage.isOnFlightPage(), is(true));

        step("Toggle trip type");
        flightLandingPage.toggleTripType(tripType);

        step("User select origin");
        flightLandingPage.selectOrigin(origin);
        bookingFormPage.clickOkayGotIt();

        step("User select destination");
        flightLandingPage.selectDestination(destination);
        bookingFormPage.clickOkayGotIt();

        step("Select date");
        flightLandingPage.selectDate(departureDate,returnDate);

        step("User click search button");
        flightLandingPage.searchFlight();
        Assertion.assertThat("Search Result Page is loaded", searchResultPage.isSearchResultPagePresent(), is(true));
        String departDateUI = searchResultPage.getActiveDate();

        step("Choose depart flight");
        searchResultPage.selectFlight();
        String returnDateUI = searchResultPage.getActiveDate();

        step("Verify dates");
        Assertion.assertThat("Depart date and return date is the same", departDateUI, is(returnDateUI));
    }

    @Author(names = "Alden")
    @Module(name = "SRP")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void validateSearchToday()  throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("validateSearchToday");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPage = FlightLandingPageWeb.getInstance(session);
        FlightSearchResultPageWeb<?> searchResultPage = FlightSearchResultPageWeb.getInstance(session);
        FlightBookingFormPageWeb<?> bookingFormPage =  FlightBookingFormPageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        String origin = param.get("origin");
        String destination = param.get("destination");
        String currentDate = CoreFunctions.getCurrentLongDate();
        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");

        int loginAttemptCount = Integer.parseInt(param.get("loginAttemptCount"));

        step("User login");
        loginPage.loginIfNotWithSameEmail(username, password, otp, loginAttemptCount);

        step("User open Flight Landing Page");
        homePage.openVerticalPageViaURL("Flights");
        Assertion.assertThat("Flight Landing Page is Opened", flightLandingPage.isOnFlightPage(), is(true));

        step("User select origin");
        flightLandingPage.selectOrigin(origin);
        bookingFormPage.clickOkayGotIt();

        step("User select destination");
        flightLandingPage.selectDestination(destination);
        bookingFormPage.clickOkayGotIt();

        step("Select date");
        flightLandingPage.selectDate(currentDate);

        step("User click search button");
        flightLandingPage.searchFlight();
        Assertion.assertThat("Search Result Page is loaded", searchResultPage.isSearchResultPagePresent(), is(true));

        step("Verify search date");
        Assertion.assertThat("Search date is correct", CoreFunctions.convertToLongDate(searchResultPage.getActiveDate()), is(currentDate));
    }

    @Author(names = "Alden")
    @Module(name = "SRP")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void validateSearchTopResults()  throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("validateSearchTopResults");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPage = FlightLandingPageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        String origin = param.get("origin");
        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
        int loginAttemptCount = Integer.parseInt(param.get("loginAttemptCount"));

        step("User login");
        loginPage.loginIfNotWithSameEmail(username, password, otp, loginAttemptCount);

        step("User open Flight Landing Page");
        homePage.openVerticalPageViaURL("Flights");
        Assertion.assertThat("Flight Landing Page is Opened", flightLandingPage.isOnFlightPage(), is(true));

        step("Search and verify search result");
        Assertion.assertThat("Search Result is Showing", flightLandingPage.isSearchResultPresent(origin), is(true));
        Assertion.assertThat("Top search result is " + origin, flightLandingPage.getFirstHighlightedResult(), is(origin));
    }

    @Author(names = "Rohan")
    @Module(name = "Booking")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void testLoginBookingDirectSRTMultiPaxInternationalWithExtraProtection() throws Exception {
        final int RETRY_LOGIN_COUNTER = 3;
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testLoginBookingDirectSRTMultiPaxInternationalWithExtraProtection");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPage = FlightLandingPageWeb.getInstance(session);
        FlightSearchResultPageWeb<?> flightSearchResultPageWeb = FlightSearchResultPageWeb.getInstance(session);
        FlightProductReviewPageWeb<?> flightProductReviewPageWeb = FlightProductReviewPageWeb.getInstance(session);
        FlightBookingFormPageWeb<?> flightBookingFormPageWeb = FlightBookingFormPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPageWeb = PaymentPageWeb.getInstance(session);

        int daysToDepartureDate = ThreadLocalRandom.current().nextInt(3, 30);

        String origin = param.get("origin");
        String destination = param.get("destination");
        String airlineName = param.get("airlineNameDepart");
        int duration = Integer.parseInt(param.get("duration"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate,daysToDepartureDate);
        String returnDate = CoreFunctions.addToLongDate(departureDate, duration);
        String username = param.get("username");
        String otp = param.get("otp");
        String password = param.get("password");
        String FullName = param.get("fullName");
        String salutation = randomSalutation();
        String phoneNo = randomPhone();
        String email = randomEmail();
        String currency = param.get("currency");

        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));

        step("User open Home Page");
        homePage.goToHomePage();

        step("Change Currency to non IDR");
        homePage.selectCurrency(currency);

        step("User open Flight Landing Page");
        homePage.openVerticalPageViaURL("Flights");
        Assertion.assertThat("Flight Landing Page is Opened", flightLandingPage.isOnFlightPage(), is(true));

        step("Toggle trip type");
        flightLandingPage.toggleTripType("roundTrip");

        step("User select origin");
        flightLandingPage.selectOrigin(origin);
        flightBookingFormPageWeb.clickOkayGotIt();

        step("User select destination");
        flightLandingPage.selectDestination(destination);
        flightBookingFormPageWeb.clickOkayGotIt();

        step("Select date");
        flightLandingPage.selectDate(departureDate,returnDate);

        step("add passenger");
        flightLandingPage.addPassenger(adultPassengers,childPassengers,infantPassengers);

        step("User click search button");
        flightLandingPage.searchFlight();
        Assertion.assertThat("Search Result Page is loaded", flightSearchResultPageWeb.isSearchResultPagePresent(), is(true));

        step("Wait for Search Result page to be fully loaded.");
        flightSearchResultPageWeb.isSRPLoaded();

        step("User click direct Flight filter");
        flightSearchResultPageWeb.selectDirectFlightFilter();
        flightSearchResultPageWeb.roundTripDealsFilter();

        step("Filter airline {" + airlineName + " }");
        flightSearchResultPageWeb.filterAirline(airlineName);

        step("Select flight for departure");
        flightSearchResultPageWeb.selectFlightByAirline(airlineName);

        step("User click Transit Flight filter");
        flightSearchResultPageWeb.selectDirectFlightFilter();
        flightSearchResultPageWeb.roundTripDealsFilter();

        step("Filter airline {" + airlineName + " }");
        flightSearchResultPageWeb.filterAirline(airlineName);

        step("Select flight for departure");
        flightSearchResultPageWeb.selectFlightByAirline(airlineName);

        step("User is redirected to Product Review Page");
        Assertion.assertThat("Product Review page is visible", flightProductReviewPageWeb.isPresent(), is(true));

        step("User clicks 'Select' button on 'Economy' card");
        flightProductReviewPageWeb.clickSelectBtnRegular();

        step("Verify user is directed to Booking Form page");
        flightBookingFormPageWeb.clickOkayGotIt();

        step("Click toggle to fill passengers details and save ");
        flightBookingFormPageWeb.inputContactDetailsNonLogin(salutation, FullName, phoneNo, email);
        flightBookingFormPageWeb.fillPassengerDetailsLogin(adultPassengers, childPassengers, infantPassengers, "international", "");

        step("Select Insurance");
        flightBookingFormPageWeb.insuranceSelector();

        step("going to paymentPage");
        flightBookingFormPageWeb.goToPaymentPageFromBookingFormPage();
        flightBookingFormPageWeb.clickNoThanksOnFlightMayReschedulePopup();

        step("click I don't want it");
        flightBookingFormPageWeb.clickOkayGotIt();
        flightBookingFormPageWeb.clickGotItInBaggagePopup();
        step("Payment page should be displayed");
        String orderID = paymentPageWeb.getOrderId();
        orderID = String.valueOf(getOrderId(orderID));
        Assertion.assertThat("OrderIDVisible "+ orderID, !orderID.isEmpty(), is(true));

        step("User open Home Page");
        homePage.goToHomePage();

        step("Revert Currency to IDR");
        homePage.selectCurrency("IDR");
    }

    @Author(names = "Aditi")
    @Module(name = "Booking")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void testBookingOneWayAddOnsThroughBaggage() {
        Map<String, String> param = getParam("testBookingOneWayAddOnsThroughBaggage");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPage = FlightLandingPageWeb.getInstance(session);
        FlightSearchResultPageWeb<?> searchResultPage = FlightSearchResultPageWeb.getInstance(session);
        FlightDescriptionPageWeb<?> flightDescriptionPage = FlightDescriptionPageWeb.getInstance(session);
        FlightProductReviewPageWeb<?> productReviewPage = FlightProductReviewPageWeb.getInstance(session);
        FlightBookingFormPageWeb<?> bookingFormPage = FlightBookingFormPageWeb.getInstance(session);
        FlightBookingFormPageMWeb<?> bookingFormPageMweb = (FlightBookingFormPageMWeb<?>) FlightBookingFormPageMWeb.getInstance(session);
        PaymentPageWeb<?> paymentPageWeb = PaymentPageWeb.getInstance(session);

        String origin = param.get("origin");
        String destination = param.get("destination");
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        String currency = param.get("currency");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);
        String FullName = CoreFunctions.randomName();
        String salutation = randomSalutation();
        String phoneNo = randomPhone();
        String email = randomEmail();

        PriceDetail priceDetail = new PriceDetail();
        priceDetail.tripType = "oneway";
        priceDetail.adult = adultPassengers;
        priceDetail.child = childPassengers;
        priceDetail.infant = infantPassengers;

        step("User open Home Page");
        homePage.goToHomePage();

        step("Change currency to " + currency);
        homePage.selectCurrency(currency);

        step("User open Flight Landing Page");
        homePage.openVerticalPageViaURL("Flights");
        Assertion.assertThat("Flight Landing Page is Opened", flightLandingPage.isOnFlightPage(), is(true));

        step("select trip type One Way");
        flightLandingPage.toggleTripType("oneway");

        step("User select origin");
        flightLandingPage.selectOrigin(origin);
        bookingFormPage.clickOkayGotIt();

        step("User select destination");
        flightLandingPage.selectDestination(destination);
        bookingFormPage.clickOkayGotIt();

        step("Select date");
        flightLandingPage.selectDate(departureDate);

        step("add passenger");
        flightLandingPage.addPassenger(adultPassengers, childPassengers, infantPassengers);

        step("User click search button");
        flightLandingPage.searchFlight();
        Assertion.assertThat("Search Result Page is loaded", searchResultPage.isSearchResultPagePresent(), is(true));

        step("Wait for Search Result page to be fully loaded.");
        searchResultPage.isSRPLoaded();
        String url = searchResultPage.fetchURL();

        step("Click on floating promo button");
        searchResultPage.clickFloatingPromoButton();

        step("Verify promo page is displayed");
        Assertion.assertThat("Promo page should be displayed", searchResultPage.isPromoPageDisplayed(), is(true));

        step("Close promo page");
        searchResultPage.closePromoPage(url);

        step("User choose filter Transit");
        searchResultPage.CheckTransit();

        step("Apply baggage filter");
        searchResultPage.selectBaggageFilter();

        step("select flight departure");
        searchResultPage.selectFlight();

        step("User is redirected to Product Review Page");
        Assertion.assertThat("Product Review page is visible", productReviewPage.isPresent(), is(true));

        step("User can see the schedule selection by user and can see the details");
        Assertion.assertThat("Departure schedule is displayed", productReviewPage.isDepartureScheduleVisible(), is(true));

        step("User clicks on flight route collapse icon to expand details");
        productReviewPage.expandFlightRouteDetails();

        step("User clicks on first 'See details' button in travel route");
        productReviewPage.clickFirstTravelRouteSeeDetails();

        step("Verify Flight Facilities bottom sheet is displayed for first flight");
        Assertion.assertThat("Flight Facilities bottom sheet should be displayed", productReviewPage.isFlightFacilitiesBottomSheetDisplayed(), is(true));

        step("User closes the Flight Facilities bottom sheet");
        productReviewPage.closeBottomSheet();

        step("User clicks on second 'See details' button for transit flight");
        productReviewPage.clickSecondTravelRouteSeeDetails();

        step("Verify Flight Facilities bottom sheet is displayed for second flight");
        Assertion.assertThat("Flight Facilities bottom sheet should be displayed for transit flight", productReviewPage.isFlightFacilitiesBottomSheetDisplayed(), is(true));

        step("User closes the Flight Facilities bottom sheet");
        productReviewPage.closeBottomSheet();

        step("User scrolls to Economy Card and clicks 'See Details' hyperlink");
        productReviewPage.clickSeeDetailsUnderEconomyCard();

        step("User selects the 'Refund' tab in the popup");
        productReviewPage.clickPenaltyInfoTabRefund();

        step("Scroll and verify 'Other Important Info' section is visible for Refund");
        Assertion.assertThat("Other Important Info section should be visible", productReviewPage.isOtherImportantInfoSectionVisible(), is(true));

        step("User selects the 'Reschedule' tab in the popup");
        productReviewPage.clickPenaltyInfoTabReschedule();

        step("Scroll and verify 'Other Important Info' section is visible for Reschedule");
        Assertion.assertThat("Other Important Info section should be visible for Reschedule", productReviewPage.isOtherImportantInfoSectionVisible(), is(true));

        step("User selects the 'PRICE DETAIL' tab in the popup");
        productReviewPage.clickPenaltyInfoTabPriceDetail();

        step("Validate price breakdown and total calculation");
        Assertion.assertThat("Price Details section should be visible", productReviewPage.isPriceDetailsSectionVisible(), is(true));
        productReviewPage.validatePriceBreakdownAndTotal(priceDetail,currency);

        step("User clicks the close button on price detail bottom sheet");
        productReviewPage.closeBottomSheet();

        step("User is redirected to Product Review Page");
        Assertion.assertThat("Product Review page is visible again", productReviewPage.isPresent(), is(true));

        step("User clicks 'Select' button on 'Economy' card");
        productReviewPage.clickSelectBtnRegular();
        productReviewPage.clickNoThanksBtn();

        step("Verify user is directed to Booking Form page");
        bookingFormPage.waitForBookingPageToLoad();
        Assertion.assertThat("Booking Form page is visible", bookingFormPage.isBookingFormVisible(), is(true));

        step("Verify flight details are displayed");
        Assertion.assertThat("Flight details are displayed", bookingFormPage.isFlightDetailsVisible(), is(true));

        step("User navigates to Flight Details section");
        bookingFormPage.goOnFlightDetail();

        step("Verify Flight Schedule tab is visible");
        Assertion.assertThat("Flight Schedule tab is displayed", bookingFormPage.isFlightScheduleTabVisible(), is(true));

        step("User Selects Details of Flight Facility");
        bookingFormPage.clickOnFacilitiesDetail(1);

        step("Verify Flight Facilities bottom sheet is displayed");
        Assertion.assertThat("Flight Facilities bottom sheet should be displayed", bookingFormPage.isFlightFacilitiesBottomSheetDisplayed(), is(true));

        step("User closes Flight Facilities bottom sheet");
        bookingFormPage.closeFlightFacilitiesBottomSheet();

        step("User Selects Details of Flight Facility again");
        bookingFormPage.goOnFlightDetail();
        bookingFormPage.clickOnFacilitiesDetail(2);

        step("Verify Flight Facilities bottom sheet is displayed");
        Assertion.assertThat("Flight Facilities bottom sheet should be displayed", bookingFormPage.isFlightFacilitiesBottomSheetDisplayed(), is(true));

        step("User closes Flight Facilities bottom sheet");
        bookingFormPage.closeFlightFacilitiesBottomSheet();

        step("Fill Passenger Details");
        bookingFormPage.inputContactDetailsNonLogin(salutation, FullName, phoneNo, email);
        FlightPaxDetail flightPaxDetail = bookingFormPageMweb.fillPassengerDetailsGuest(adultPassengers, childPassengers, infantPassengers, "domestic", "");
        FullName = flightPaxDetail.data.get(1).paxFirstName + " " + flightPaxDetail.data.get(1).paxLastName;

        step("User is on Booking Form page at Addons section");
        bookingFormPage.scrollToBaggageSection();

        step("User clicks Add button on baggage addon");
        bookingFormPage.clickBaggageAddButton();

        step("User clicks Select button on baggage addon");
        bookingFormPage.clickBaggageSelectButton();

        step("Verify baggage subtotal is zero before selecting baggage");
        bookingFormPage.validateBaggageSubtotalIsZero("IDR");

        step("User Select Baggage for Passenger");
        bookingFormPage.selectBaggage();

        step("User opens baggage price details dropdown");
        bookingFormPage.clickBaggagePriceToggler();

        step("Verify baggage price details bottom sheet is displayed");
        Assertion.assertThat("Baggage price details should be displayed", bookingFormPage.isBaggagePriceDetailsBottomSheetDisplayed(), is(true));

        step("Verify baggage is assigned to correct passenger");
        bookingFormPage.validateBaggagePassengerName(FullName);

        step("User closes baggage price details popup");
        bookingFormPage.closeBaggagePriceDetailsBottomSheet();

        step("Save added baggage");
        bookingFormPage.saveBaggage();

        step("User taps on chevron icon on price section to view Price Details");
        bookingFormPage.openPriceDetailSection();

        step("User is redirected to Price Detail page");
        Assertion.assertThat("Price Detail Page is displayed", bookingFormPage.isPriceDetailPageVisible(), is(true));

        step("Verify Total Price Displayed");
        Assertion.assertThat("Total price is displayed", bookingFormPage.isTotalPriceDisplayed(), is(true));

        step("User closes the price detail page");
        bookingFormPage.closePopUp();

        step("User is redirected back to Booking Form");
        Assertion.assertThat("Booking Form page is visible again", bookingFormPage.isBookingFormVisible(), is(true));

        step("going to paymentPage");
        bookingFormPage.clickContinueToPaymentButtons();
        bookingFormPage.clickNoThanksOnFlightMayReschedulePopup();

        step("click I don't want it");
        bookingFormPage.clickOkayGotIt();

        step("Payment page should be displayed");
        Assertion.assertThat("PaymentPageLoaded", paymentPageWeb.isPaymentPagepresent(), is(true));
        String orderID = paymentPageWeb.getOrderId();
        orderID = String.valueOf(getOrderId(orderID));
        Assertion.assertThat("OrderID is Visible " + orderID, !orderID.isEmpty(), is(true));
    }

    @Author(names = "Aditi")
    @Module(name = "Booking")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void testBaggageAddonAddRemoveAndReaddPriceBreakdown() {
        Map<String, String> param = getParam("testBaggageAddonAddRemoveAndReaddPriceBreakdown");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPage = FlightLandingPageWeb.getInstance(session);
        FlightSearchResultPageWeb<?> searchResultPage = FlightSearchResultPageWeb.getInstance(session);
        FlightProductReviewPageWeb<?> productReviewPage = FlightProductReviewPageWeb.getInstance(session);
        FlightBookingFormPageWeb<?> bookingFormPage = FlightBookingFormPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPageWeb = PaymentPageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        String origin = param.get("origin");
        String destination = param.get("destination");
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        String currency = param.get("currency");
        int daysToDepartureDate = ThreadLocalRandom.current().nextInt(30, 60);
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);
        String fullName = CoreFunctions.randomName();
        String salutation = randomSalutation();
        String phoneNo = randomPhone();
        String email = randomEmail();

        step("User open Home Page");
        homePage.goToHomePage();

        step("Logout via URL");
        loginPage.loginAsGuest();

        step("User select IDR currency");
        homePage.selectCurrency(currency);

        step("Go to flight landing page");
        homePage.openVerticalPageViaURL("Flights");
        Assertion.assertThat("Flight landing page is opened", flightLandingPage.isOnFlightPage(), is(true));

        flightLandingPage.toggleTripType("oneway");

        step("Enter origin, destination and other details");
        flightLandingPage.selectOrigin(origin);
        bookingFormPage.clickOkayGotIt();
        flightLandingPage.selectDestination(destination);
        bookingFormPage.clickOkayGotIt();
        flightLandingPage.selectDate(departureDate);
        flightLandingPage.addPassenger(adultPassengers, childPassengers, infantPassengers);

        step("Search for flight");
        flightLandingPage.searchFlight();
        Assertion.assertThat("Search result page is loaded", searchResultPage.isSearchResultPagePresent(), is(true));
        searchResultPage.isSRPLoaded();

        step("Choose baggage filter from SRP");
        searchResultPage.selectBaggageFilter();

        step("Choose any flight which provides baggage addons");
        searchResultPage.selectFlight();
        Assertion.assertThat("Product review page is visible", productReviewPage.isPresent(), is(true));

        productReviewPage.clickSelectBtnRegular();
        productReviewPage.clickNoThanksBtn();
        bookingFormPage.waitForBookingPageToLoad();
        Assertion.assertThat("Booking form page is visible", bookingFormPage.isBookingFormVisible(), is(true));

        step("Fill passenger details");
        bookingFormPage.inputContactDetailsNonLogin(salutation, fullName, phoneNo, email);
        bookingFormPage.fillPassengerDetailsLogin(adultPassengers, childPassengers, infantPassengers, "domestic", "");

        step("Verify no baggage is pre-selected on booking form");
        bookingFormPage.scrollToBaggageSection();
        bookingFormPage.verifyBaggageNotPreSelected();

        // add baggage 1st time
        step("In booking form, add baggage");
        FlightBookingFormPageWeb.SelectedBaggageDetail selectedBaggage = bookingFormPage.addBaggageFromBookingForm();

        step("User taps on chevron icon on price section to view Price Details");
        bookingFormPage.openPriceDetailSection();

        step("Verify price breakdown shows selected baggage details");
        bookingFormPage.verifyBookingPriceDetailWithSelectedBaggage(selectedBaggage);
        bookingFormPage.closePopUp();
        Assertion.assertThat("Booking form page is visible", bookingFormPage.isBookingFormVisible(), is(true));

        // removing the added baggage
        step("Going to addons baggage and remove baggage addons");
        bookingFormPage.scrollToBaggageSection();
        bookingFormPage.removeBaggageAddon();

        step("User taps on chevron icon on price section to view Price Details");
        bookingFormPage.openPriceDetailSection();

        step("Verify price breakdown no longer shows baggage details");
        bookingFormPage.verifyBookingPriceDetailWithoutBaggage();
        bookingFormPage.closePopUp();
        Assertion.assertThat("Booking form page is visible", bookingFormPage.isBookingFormVisible(), is(true));

        // re-adding  baggage
        step("In booking form, re-add baggage");
        selectedBaggage = bookingFormPage.addBaggageFromBookingForm();

        step("User taps on chevron icon on price section to view Price Details");
        bookingFormPage.openPriceDetailSection();

        step("Verify price breakdown shows selected baggage details");
        bookingFormPage.verifyBookingPriceDetailWithSelectedBaggage(selectedBaggage);
        bookingFormPage.closePopUp();
        Assertion.assertThat("Booking form page is visible", bookingFormPage.isBookingFormVisible(), is(true));

        step("Proceed to payment page");
        bookingFormPage.goToPaymentPageFromBookingFormPage();

        step("verify Review Passenger's Data");
        bookingFormPage.clickItsCorrectOnReviewPassengerDataPopup();
        bookingFormPage.clickNoThanksOnFlightMayReschedulePopup();

        step("Payment page should be displayed");
        Assertion.assertThat("Payment page is loaded", paymentPageWeb.isPaymentPagepresent(), is(true));
        String orderID = paymentPageWeb.getOrderId();
        orderID = String.valueOf(getOrderId(orderID));
        Assertion.assertThat("OrderID is visible " + orderID, !orderID.isEmpty(), is(true));

    }

    @Author(names = {"Ashish"})
    @Module(name = "Insurance")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void testCheckDomesticRoundDirectExtraProtectionGuest() throws IOException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testCheckDomesticRoundDirectExtraProtectionGuest");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPage = FlightLandingPageWeb.getInstance(session);
        FlightSearchResultPageWeb<?> flightSearchResultPageWeb = FlightSearchResultPageWeb.getInstance(session);
        FlightProductReviewPageWeb<?> flightProductReviewPageWeb = FlightProductReviewPageWeb.getInstance(session);
        FlightBookingFormPageWeb<?> flightBookingFormPageWeb = FlightBookingFormPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPageWeb = PaymentPageWeb.getInstance(session);
        PromoPageWeb<?> promoPageWeb = PromoPageWeb.getInstance(session);

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int duration = Integer.parseInt(param.get("duration"));

        String origin = param.get("origin");
        String destination = param.get("destination");
        String tripType = param.get("tripType");
        String airlineNameDeparture = param.get("airlineNameDeparture");
        String airlineNameReturn = param.get("airlineNameReturn");
        String currentDate = CoreFunctions.getCurrentLongDate();
        String currency = param.get("currency");
        String FullName = randomName();
        String salutation = randomSalutation();
        String phoneNo = randomPhone();
        String email = randomEmail();

        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        int dayToDepartRandom = CoreFunctions.generateRandomInt(daysToDepartureDate,10);
        String departureDate = CoreFunctions.addToLongDate(currentDate,dayToDepartRandom);
        String returnDate = CoreFunctions.addToLongDate(departureDate,duration);

        PriceDetail priceDetail = new PriceDetail();
        priceDetail.tripType = tripType;
        priceDetail.adult = adultPassengers;
        priceDetail.child = childPassengers;
        priceDetail.infant = infantPassengers;

        step("User open Home Page");
        homePage.goToHomePage();

        step("User is in Guest mode");
        loginPage.loginAsGuest();

        step("Change Currency to " + currency);
        homePage.selectCurrency(currency);

        step("User open Flight Landing Page");
        homePage.openVerticalPageViaURL("Flights");

        step("User select origin and destination");
        flightLandingPage.selectOrigin(origin);
        flightLandingPage.selectDestination(destination);

        step("User update trip type to Round Trip");
        flightLandingPage.toggleTripType(tripType);

        step("User select date");
        flightLandingPage.selectDate(departureDate,returnDate);

        step("User Add Passenger");
        flightLandingPage.addPassenger(adultPassengers,childPassengers,infantPassengers);

        step("User click search button");
        flightLandingPage.searchFlight();

        step("Verify Search Result Page is loaded");
        Assertion.assertThat("Search Result Page is loaded", flightSearchResultPageWeb.isSearchResultPagePresent(), is(true));

        step("User click the Promo Icon");
        flightSearchResultPageWeb.getCurrentUrlSRP();
        flightSearchResultPageWeb.clickPromoIcon();

        step("Verify Promo page is displayed");
        Assertion.assertThat("Promo Page is displayed", promoPageWeb.isPromoPageTitleAppear(), is(true));

        step("Click back button to go back to SRP");
        promoPageWeb.clickBackBtn();
        flightSearchResultPageWeb.isSRPLoaded();

        step("Select direct flight");
        flightSearchResultPageWeb.selectTransitFilter("direct");

        step("filter airline for departure flight");
        flightSearchResultPageWeb.filterAirline(airlineNameDeparture);

        step("Select flight for departure with airline: " + airlineNameDeparture);
        flightSearchResultPageWeb.selectFlightByAirline(airlineNameDeparture);

        step("Select direct flight for return");
        flightSearchResultPageWeb.selectTransitFilter("direct");

        step("filter flight for return with airline: " + airlineNameReturn);
        flightSearchResultPageWeb.filterAirline(airlineNameReturn);

        step("Select random airline for return flight (different from " + airlineNameReturn + ")");
        flightSearchResultPageWeb.selectFlightByAirline(airlineNameReturn);

        step("User is redirected to Product Review Page");
        Assertion.assertThat("Product Review page is visible", flightProductReviewPageWeb.isPresent(), is(true));

        step("User can see the schedule selection by user and can see the details");
        Assertion.assertThat("Departure schedule is displayed", flightProductReviewPageWeb.isDepartureScheduleVisible(), is(true));

        //============================flight details verification============================//

        step("User clicks on flight route collapse icon to expand details");
        flightProductReviewPageWeb.openFlightFacilitiesSectionViaChevron(1);

        step("Verify Flight Facilities bottom sheet is displayed for first flight");
        Assertion.assertThat("Flight Facilities bottom sheet should be displayed", flightProductReviewPageWeb.isFlightFacilitiesBottomSheetDisplayed(), is(true));

        step("User closes the Flight Facilities bottom sheet");
        flightProductReviewPageWeb.closeBottomSheet();

        step("User clicks on second 'See details' button for transit flight");
        flightProductReviewPageWeb.openFlightFacilitiesSectionViaChevron(2);

        step("Verify Flight Facilities bottom sheet is displayed for second flight");
        Assertion.assertThat("Flight Facilities bottom sheet should be displayed for transit flight", flightProductReviewPageWeb.isFlightFacilitiesBottomSheetDisplayed(), is(true));

        step("User closes the Flight Facilities bottom sheet");
        flightProductReviewPageWeb.closeBottomSheet();

        step("User scrolls to Economy Card and clicks 'See Details' hyperlink");
        flightProductReviewPageWeb.clickSeeDetailsUnderEconomyCard();

        step("User selects the 'Refund' tab in the popup");
        flightProductReviewPageWeb.clickPenaltyInfoTabRefund();

        step("Scroll and verify 'Refund', 'FAQ' and 'Other Important Info' sections are visible for Refund Tab");
        Assertion.assertThat("Refund Tab should display Refund, FAQ and Other Important Info sections", flightBookingFormPageWeb.verifyRefundTabSection(), is(true));

        step("User selects the 'Reschedule' tab in the popup");
        flightProductReviewPageWeb.clickPenaltyInfoTabReschedule();

        step("Scroll and verify 'Reschedule' and 'Other Important Info' sections are visible for Reschedule Tab");
        Assertion.assertThat("Reschedule Tab should display Reschedule and Other Important Info sections", flightBookingFormPageWeb.verifyRescheduleTabSection(), is(true));

        step("User selects the 'PRICE DETAIL' tab in the popup");
        flightProductReviewPageWeb.clickPenaltyInfoTabPriceDetail();

        step("Validate price breakdown and total calculation");
        Assertion.assertThat("Price Details section should be visible", flightProductReviewPageWeb.isPriceDetailsSectionVisible(), is(true));
        flightProductReviewPageWeb.validatePriceBreakdownAndTotal(priceDetail,currency);

        step("User clicks the close button on price detail bottom sheet");
        flightProductReviewPageWeb.closeBottomSheet();

        step("User is redirected to Product Review Page");
        Assertion.assertThat("Product Review page is visible again", flightProductReviewPageWeb.isPresent(), is(true));

        step("User clicks 'Select' button on 'Economy' card");
        flightProductReviewPageWeb.clickSelectBtnRegular();
        flightProductReviewPageWeb.clickNoThanksBtn();

        //============================booking form verification============================//

        step("Verify user is directed to Booking Form page");
        flightBookingFormPageWeb.waitForBookingPageToLoad();
        Assertion.assertThat("Booking Form page is visible", flightBookingFormPageWeb.isBookingFormVisible(), is(true));

        step("Verify flight details are displayed");
        Assertion.assertThat("Flight details are displayed", flightBookingFormPageWeb.isFlightDetailsVisible(), is(true));

        //============================departure flight details verification============================//
        step("User navigates to Flight Details section Departure Flight");
        flightBookingFormPageWeb.openFlightSummery(1);

        step("Verify Flight Schedule tab is visible");
        Assertion.assertThat("Flight Schedule tab is displayed", flightBookingFormPageWeb.isFlightScheduleTabVisible(), is(true));

        step("User Selects Details of Flight Facility");
        flightBookingFormPageWeb.clickSeeDetailRefundAndReschedule();

        step("Scroll and verify 'FAQ' and 'Other Important Info' sections are visible for Refund Tab");
        Assertion.assertThat("Refund Tab should display Refund Amount, FAQ and Other Important Info sections", flightBookingFormPageWeb.verifyRefundTabSection(), is(true));

        step("User clicks on 'Reschedule' chip button");
        flightBookingFormPageWeb.clickRescheduleTab();

        step("Scroll and verify 'Other Important Info' sections are visible for Reschedule Tab");
        Assertion.assertThat("Reschedule Tab should display Reschedule Fee and Other Important Info sections", flightBookingFormPageWeb.verifyRescheduleTabSection(), is(true));

        step("closes the Flight Facilities bottom sheet");
        flightBookingFormPageWeb.closeFlightFacilitiesBottomSheet();

        step("Reopen Departure flight details section");
        flightBookingFormPageWeb.openFlightSummery(1);

        step("open flight facilities section via chevron");
        flightBookingFormPageWeb.clickSeeFlightFacilities();

        step("Verify Flight Facilities bottom sheet is displayed");
        Assertion.assertThat("Flight Facilities bottom sheet should be displayed", flightBookingFormPageWeb.isFlightFacilitiesBottomSheetDisplayed(), is(true));

        step("User closes Flight Facilities bottom sheet");
        flightBookingFormPageWeb.closeFlightFacilitiesBottomSheet();

        //============================return flight details verification============================//

        step("User navigates to Flight Details section Return");
        flightBookingFormPageWeb.openFlightSummery(2);

        step("Verify Flight Schedule tab is visible");
        Assertion.assertThat("Flight Schedule tab is displayed", flightBookingFormPageWeb.isFlightScheduleTabVisible(), is(true));

        step("User Selects Details of Flight Facility");
        flightBookingFormPageWeb.clickSeeDetailRefundAndReschedule();

        step("Scroll 'FAQ' and verify 'Other Important Info' sections are visible for Refund Tab");
        Assertion.assertThat("Refund Tab should display Refund Amount, FAQ and Other Important Info sections", flightBookingFormPageWeb.verifyRefundTabSection(), is(true));

        step("User clicks on 'Reschedule' chip button");
        flightBookingFormPageWeb.clickRescheduleTab();

        step("Scroll and verify 'Other Important Info' sections are visible for Reschedule Tab");
        Assertion.assertThat("Reschedule Tab should display Reschedule Fee and Other Important Info sections", flightBookingFormPageWeb.verifyRescheduleTabSection(), is(true));

        step("closes the Flight Facilities bottom sheet");
        flightBookingFormPageWeb.closeFlightFacilitiesBottomSheet();

        step("Reopen return flight details section");
        flightBookingFormPageWeb.openFlightSummery(2);

        step("open flight facilities section via chevron");
        flightBookingFormPageWeb.clickSeeFlightFacilities();

        step("Verify Flight Facilities bottom sheet is displayed");
        Assertion.assertThat("Flight Facilities bottom sheet should be displayed", flightBookingFormPageWeb.isFlightFacilitiesBottomSheetDisplayed(), is(true));

        step("User closes Flight Facilities bottom sheet");
        flightBookingFormPageWeb.closeFlightFacilitiesBottomSheet();

        //============================fill contact and passenger details============================//

        step("User fill contact details and passenger details");
        flightBookingFormPageWeb.inputContactDetailsNonLogin(salutation,FullName,phoneNo,email);
        flightBookingFormPageWeb.fillPassengerDetailsLogin(adultPassengers, childPassengers, infantPassengers, "domestic", airlineNameDeparture);
        if (Configs.PLATFORM == Platform.AMWEB || Configs.PLATFORM == Platform.IMWEB) {
            flightBookingFormPageWeb.clickContinue();
        }
        flightBookingFormPageWeb.clickContinue();
        step("Verify user can see the Booking Form 2nd half");
        Assertion.assertThat("Booking Form 2nd half is visible", flightBookingFormPageWeb.isBookingFormVisible(), is(true));

        step("User choose More Info on Full Protection");
        flightBookingFormPageWeb.clickMoreInfoFullProtection();

        step("User choose to see more Terms and Condition section by hyperlink");
        flightBookingFormPageWeb.clickTermsAndConditionsLink();

        step("verify insurance pdf");
        flightBookingFormPageWeb.verifyInsurancePdfByEnvironment();

        step("switch to booking tab");
        flightBookingFormPageWeb.switchToChromeOrSafari();

        step("close more info popup");
        flightBookingFormPageWeb.closeExtraProtectionBottomSheet();

        step("select insurance and get insurance data");
        InsuranceGeneralData insuranceGeneralData = flightBookingFormPageWeb.selectInsurance(currency);

        step("Verify price detail");
        priceDetail.extraProtectionTitle=insuranceGeneralData.insuranceTitle;
        priceDetail.extraProtectionPrice=insuranceGeneralData.insurancePrice;
        flightBookingFormPageWeb.isPriceBreakdownCorrect(priceDetail, currency);
        flightBookingFormPageWeb.closePopUp();

        step("Verify user is on Payment Page after click Continue to Payment button");
        flightBookingFormPageWeb.clickGotItInBaggagePopup();
        flightBookingFormPageWeb.goToPaymentPageFromAddonsPage();

        step("Go to the Order Confirmation page and verify the order is successful");
        Assertion.assertThat("PaymentPageLoaded", paymentPageWeb.isPaymentPagepresent(), is(true));
        String orderID = paymentPageWeb.getOrderId();
        Assertion.assertThat("user get order id", orderID, allOf(
                not(nullValue()),
                not(equalTo(""))
        ));

    }

    @Author(names = "Mridul Gupta")
    @Module(name = "Booking")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void checkDomesticRoundTransitMealNonIDRLogin()  throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("checkDomesticRoundTransitMealNonIDRLogin");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPage = FlightLandingPageWeb.getInstance(session);
        FlightSearchResultPageWeb<?> flightSearchResultPageWeb = FlightSearchResultPageWeb.getInstance(session);
        FlightProductReviewPageWeb<?> flightProductReviewPageWeb = FlightProductReviewPageWeb.getInstance(session);
        FlightBookingFormPageWeb<?> flightBookingFormPageWeb = FlightBookingFormPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPageWeb = PaymentPageWeb.getInstance(session);
        PromoPageWeb<?> promoPageWeb = PromoPageWeb.getInstance(session);
        InfoPageWeb<?> infoPageWeb = InfoPageWeb.getInstance(session);

        int daysToDepartureDate = ThreadLocalRandom.current().nextInt(60, 90);
        int daysToReturnDate = ThreadLocalRandom.current().nextInt(1, 5);

        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
        String origin = param.get("origin");
        String destination = param.get("destination");
        String tripType = param.get("tripType");
        String airlineName = param.get("airlineName");
        String filterType = param.get("filterType");
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate,daysToDepartureDate);
        String returnDate = CoreFunctions.addToLongDate(departureDate, daysToReturnDate);
        String currency = param.get("currency");

        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));

        step("User Login ");
        loginPage.loginIfNotWithSameEmail(username, password, otp, 1);

        step("Change Currency to " + currency);
        homePage.selectCurrency(currency);

        step("User open Flight Landing Page");
        homePage.openVerticalPageViaURL("Flights");
        Assertion.assertThat("Flight Landing Page is Opened", flightLandingPage.isOnFlightPage(), is(true));

        step("Verify Lowest Price Guarantee Tag is displayed on Flight Landing Page");
        Assertion.assertThat("Lowest Price Guarantee Tag is displayed", flightLandingPage.isLowestPriceGuaranteeTagDisplayed(), is(true));

        step("Click Lowest Price Guarantee Tag and Verify the info popup is displayed");
        flightLandingPage.clickLowestPriceGuaranteeTag();

        step("Verify Lowest Price Guarantee Page is displayed on SRP");
        Assertion.assertThat("Lowest Price Guarantee Page is displayed", infoPageWeb.verifyTitleInfopage(), is(true));

        step("Click back button to go back to SRP");
        infoPageWeb.clickBackButton();
        flightSearchResultPageWeb.isSRPLoaded();

        step("User select origin and destination");
        flightLandingPage.selectOrigin(origin);
        flightLandingPage.selectDestination(destination);

        step("User update trip type to Round Trip");
        flightLandingPage.toggleTripType(tripType);

        step("User select date");
        flightLandingPage.selectDate(departureDate,returnDate);

        step("User Add Passenger");
        flightLandingPage.addPassenger(adultPassengers,childPassengers,infantPassengers);

        step("User click search button");
        flightLandingPage.searchFlight();

        step("Verify Search Result Page is loaded");
        flightSearchResultPageWeb.isSRPLoaded();
        Assertion.assertThat("Search Result Page is loaded", flightSearchResultPageWeb.isSearchResultPagePresent(), is(true));

        step("Verify Transit Flight is displayed");
        int totalFlights = flightSearchResultPageWeb.getTotalFlights();
        int totalFlightsWithTransit = flightSearchResultPageWeb.getTotalFlightsWithTransit();
        Assertion.assertThat("All flights have transit", totalFlights, is(totalFlightsWithTransit));

        step("User click the Promo Icon");
        flightSearchResultPageWeb.clickPromoIcon();

        step("Verify Promo page is displayed");
        Assertion.assertThat("Promo Page is displayed", promoPageWeb.isPromoPageTitleAppear(), is(true));

        step("Click back button to go back to SRP");
        promoPageWeb.clickBackBtn();
        flightSearchResultPageWeb.isSRPLoaded();

        step("User Select Meal Filter on SRP");
        flightSearchResultPageWeb.otherFilters(filterType);

        step("Verify Meal Filter is applied");
        Assertion.assertThat("Meal Filter is applied", flightSearchResultPageWeb.isFilterApplied(filterType), is(true));

        step("Verify User can see total/pax and tiket point granted on each flight card");
        Assertion.assertThat("User can see total/pax and tiket point granted on each flight card", flightSearchResultPageWeb.isTotalPaxAndTiketPointDisplayed(), is(true));

        step("Select Flight for Departure");
        flightSearchResultPageWeb.selectFlight();
        flightSearchResultPageWeb.isSRPLoaded();

        step("Apply Filter for Return Flight");
        flightSearchResultPageWeb.otherFilters(filterType);

        step("Verify Filter is applied for Return Flight");
        Assertion.assertThat("Filter is applied for Return Flight", flightSearchResultPageWeb.isFilterApplied(filterType), is(true));

        step("Verify Transit Flight is displayed for Return");
        totalFlights = flightSearchResultPageWeb.getTotalFlights();
        totalFlightsWithTransit = flightSearchResultPageWeb.getTotalFlightsWithTransit();
        Assertion.assertThat("All flights have transit", totalFlights, is(totalFlightsWithTransit));

        step("Verify User can see total/pax and tiket point granted on each flight card for Return");
        Assertion.assertThat("User can see total/pax and tiket point granted on each flight card for Return", flightSearchResultPageWeb.isTotalPaxAndTiketPointDisplayed(), is(true));

        step("Select Flight for Return");
        flightSearchResultPageWeb.selectFlight();

        step("Verify user can see the schedule selection by user and can see the details");
        Assertion.assertThat("User can see the schedule selection by user", flightProductReviewPageWeb.isDepartureScheduleVisible(), is(true));

        step("User click on \"See Other Facilities\" Hyperlink on the flight detail");
        flightProductReviewPageWeb.openFlightFacilities();

        step("Verify Flight Facilities Section");
        Assertion.assertThat("Flight Facilities Section is displayed", flightProductReviewPageWeb.isFlightFacilitiesSectionVisible(), is(true));

        step("User click close button on the popup");
        flightProductReviewPageWeb.closeFlightFacilitiesSection();

        step("User click See Details to see refund info");
        flightProductReviewPageWeb.clickSeeDetailsUnderEconomyCard();
        flightProductReviewPageWeb.clickPenaltyInfoTabRefund();

        step("Verify Refund section");
        Assertion.assertThat("Refund section is displayed", flightProductReviewPageWeb.isRefundSectionVisible(), is(true));

        step("Verify Refund section FAQ Section");
        flightProductReviewPageWeb.verifyFAQSection();

        step("User selects the 'Reschedule' tab in the popup");
        flightProductReviewPageWeb.clickPenaltyInfoTabReschedule();

        step("Verify Reschedule section");
        Assertion.assertThat("Reschedule section is displayed", flightProductReviewPageWeb.isRescheduleSectionVisible(), is(true));

        step("Verify Reschedule section FAQ Section");
        flightProductReviewPageWeb.verifyFAQSection();

        step("User choose tab \"Price Detail\"");
        flightProductReviewPageWeb.clickPenaltyInfoTabPriceDetail();

        step("Verify Price Breakdown section");
        Assertion.assertThat("Price Breakdown section is displayed", flightProductReviewPageWeb.isPriceDetailInfoDisplayed(), is(true));

        step("User close the pop up");
        flightProductReviewPageWeb.closeFlightFacilitiesSection();

        step("User clicks 'Select' button on 'Economy' card");
        flightProductReviewPageWeb.clickSelectBtnRegular();

        step("Verify user see Flight details, login/register button, contact details, passenger Details, price details and Continue button");
        Assertion.assertThat("Booking Form page is visible", flightBookingFormPageWeb.isBookingFormVisible(), is(true));

        step("User click See other Details link");
        flightBookingFormPageWeb.openFlightFacilitySection();

        step("Verify user can see the details of facilities flight");
        Assertion.assertThat("Flight Facility button is visible", flightBookingFormPageWeb.isFlightFacilityVisible(), is(true));

        step("Close Flight Facility section");
        flightBookingFormPageWeb.closeFlightFacilitySection();

        step("User choose to see details refund/reschedule on booking 1st half form");
        flightBookingFormPageWeb.goOnFlightDetail();
        flightBookingFormPageWeb.clickOnSeeDetails();

        step("Verify user can see the Refund/Reschedule information");
        Assertion.assertThat("User can see the Refund/Reschedule information", flightBookingFormPageWeb.isRefundRescheduleVisible(), is(true));
        flightBookingFormPageWeb.closePopUp();

        step("Verify user can see the Booking Form 2nd half");
        Assertion.assertThat("Booking Form 2nd half is visible", flightBookingFormPageWeb.isBookingFormVisible(), is(true));

        step("User fill contact details and passenger details");
        flightBookingFormPageWeb.fillPassengerDetailsLogin(adultPassengers, childPassengers, infantPassengers, "domestic", "");
        if (Configs.PLATFORM == Platform.AMWEB || Configs.PLATFORM == Platform.IMWEB) {
            flightBookingFormPageWeb.clickContinue();
        }

        step("User choose to add on Meals section on Booking Form");
        flightBookingFormPageWeb.selectMultiMealAddons(adultPassengers+childPassengers+infantPassengers,true);

        step("Verify multiple meals up to 2 meals for each passengers and for each segment");
        Assertion.assertThat("Multiple meals up to 2 meals for each passengers and for each segment are visible", flightBookingFormPageWeb.isMealSelectionVisible(), is(true));

        step("User tap on chevron button on the price section");
        flightBookingFormPageWeb.openPriceDetailSection();

        step("Verify Price Detail page which contain breakdown price include total price");
        Assertion.assertThat("Price Detail page is visible", flightBookingFormPageWeb.isPriceDetailPageVisible(), is(true));
        Assertion.assertThat("Total price is displayed", flightBookingFormPageWeb.isTotalPriceDisplayed(), is(true));
        flightBookingFormPageWeb.closePopUp();

        step("Verify user is on Payment Page after click Continue to Payment button");
        flightBookingFormPageWeb.clickGotItInBaggagePopup();
        flightBookingFormPageWeb.goToPaymentPageFromAddonsPage();
        flightBookingFormPageWeb.clickOkayGotIt();

        step("Go to the Order Confirmation page and verify the order is successful");
        Assertion.assertThat("PaymentPageLoaded", paymentPageWeb.isPaymentPageLoaded(), is(true));

    }

    @Author(names = "Mridul Gupta")
    @Module(name = "Booking")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void checkDomesticRoundTransitMealNonIDR()  throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("checkDomesticRoundTransitMealNonIDR");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPage = FlightLandingPageWeb.getInstance(session);
        FlightSearchResultPageWeb<?> flightSearchResultPageWeb = FlightSearchResultPageWeb.getInstance(session);
        FlightProductReviewPageWeb<?> flightProductReviewPageWeb = FlightProductReviewPageWeb.getInstance(session);
        FlightBookingFormPageWeb<?> flightBookingFormPageWeb = FlightBookingFormPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPageWeb = PaymentPageWeb.getInstance(session);
        PromoPageWeb<?> promoPageWeb = PromoPageWeb.getInstance(session);

        int daysToDepartureDate = ThreadLocalRandom.current().nextInt(60, 90);
        int daysToReturnDate = ThreadLocalRandom.current().nextInt(1, 5);

        String origin = param.get("origin");
        String destination = param.get("destination");
        String tripType = param.get("tripType");
        String airlineName = param.get("airlineName");
        String filterType = param.get("filterType");
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate,daysToDepartureDate);
        String returnDate = CoreFunctions.addToLongDate(departureDate, daysToReturnDate);
        String currency = param.get("currency");
        String FullName = randomName();
        String salutation = randomSalutation();
        String phoneNo = randomPhone();
        String email = randomEmail();

        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));

        step("User open Home Page");
        homePage.goToHomePage();

        step("User is in Guest mode");
        loginPage.loginAsGuest();

        step("Change Currency to " + currency);
        homePage.selectCurrency(currency);

        step("User open Flight Landing Page");
        homePage.openVerticalPageViaURL("Flights");

        step("User select origin and destination");
        flightLandingPage.selectOrigin(origin);
        flightLandingPage.selectDestination(destination);

        step("User update trip type to Round Trip");
        flightLandingPage.toggleTripType(tripType);

        step("User select date");
        flightLandingPage.selectDate(departureDate,returnDate);

        step("User Add Passenger");
        flightLandingPage.addPassenger(adultPassengers,childPassengers,infantPassengers);

        step("User click search button");
        flightLandingPage.searchFlight();

        step("Verify Search Result Page is loaded");
        Assertion.assertThat("Search Result Page is loaded", flightSearchResultPageWeb.isSearchResultPagePresent(), is(true));

        step("Verify Transit Flight is displayed");
        int totalFlights = flightSearchResultPageWeb.getTotalFlights();
        int totalFlightsWithTransit = flightSearchResultPageWeb.getTotalFlightsWithTransit();
        Assertion.assertThat("All flights have transit", totalFlights, is(totalFlightsWithTransit));

        step("User click the Promo Icon");
        flightSearchResultPageWeb.clickPromoIcon();

        step("Verify Promo page is displayed");
        Assertion.assertThat("Promo Page is displayed", promoPageWeb.isPromoPageTitleAppear(), is(true));

        step("Click back button to go back to SRP");
        promoPageWeb.clickBackBtn();
        flightSearchResultPageWeb.isSRPLoaded();

        step("User Select Meal Filter on SRP");
        flightSearchResultPageWeb.otherFilters(filterType);

        step("Verify Meal Filter is applied");
        Assertion.assertThat("Meal Filter is applied", flightSearchResultPageWeb.isFilterApplied(filterType), is(true));

        step("Verify User can see total/pax and tiket point granted on each flight card");
        Assertion.assertThat("User can see total/pax and tiket point granted on each flight card", flightSearchResultPageWeb.isTotalPaxAndTiketPointDisplayed(), is(true));

        step("Select Flight for Departure");
        flightSearchResultPageWeb.selectFlight();

        step("Apply Filter for Return Flight");
        flightSearchResultPageWeb.otherFilters(filterType);

        step("Verify Filter is applied for Return Flight");
        Assertion.assertThat("Filter is applied for Return Flight", flightSearchResultPageWeb.isFilterApplied(filterType), is(true));

        step("Verify Transit Flight is displayed for Return");
        totalFlights = flightSearchResultPageWeb.getTotalFlights();
        totalFlightsWithTransit = flightSearchResultPageWeb.getTotalFlightsWithTransit();
        Assertion.assertThat("All flights have transit", totalFlights, is(totalFlightsWithTransit));

        step("Verify User can see total/pax and tiket point granted on each flight card for Return");
        Assertion.assertThat("User can see total/pax and tiket point granted on each flight card for Return", flightSearchResultPageWeb.isTotalPaxAndTiketPointDisplayed(), is(true));

        step("Select Flight for Return");
        flightSearchResultPageWeb.selectFlight();

        step("Verify user can see the schedule selection by user and can see the details");
        Assertion.assertThat("User can see the schedule selection by user", flightProductReviewPageWeb.isDepartureScheduleVisible(), is(true));

        step("User click on \"See Other Facilities\" Hyperlink on the flight detail");
        flightProductReviewPageWeb.openFlightFacilities();

        step("Verify Flight Facilities Section");
        Assertion.assertThat("Flight Facilities Section is displayed", flightProductReviewPageWeb.isFlightFacilitiesSectionVisible(), is(true));

        step("User click close button on the popup");
        flightProductReviewPageWeb.closeFlightFacilitiesSection();

        step("User click See Details to see refund info");
        flightProductReviewPageWeb.clickSeeDetailsUnderEconomyCard();
        flightProductReviewPageWeb.clickPenaltyInfoTabRefund();

        step("Verify Refund section");
        Assertion.assertThat("Refund section is displayed", flightProductReviewPageWeb.isRefundSectionVisible(), is(true));

        step("Verify Refund section FAQ Section");
        flightProductReviewPageWeb.verifyFAQSection();

        step("User selects the 'Reschedule' tab in the popup");
        flightProductReviewPageWeb.clickPenaltyInfoTabReschedule();

        step("Verify Reschedule section");
        Assertion.assertThat("Reschedule section is displayed", flightProductReviewPageWeb.isRescheduleSectionVisible(), is(true));

        step("Verify Reschedule section FAQ Section");
        flightProductReviewPageWeb.verifyFAQSection();

        step("User choose tab \"Price Detail\"");
        flightProductReviewPageWeb.clickPenaltyInfoTabPriceDetail();

        step("Verify Price Breakdown section");
        Assertion.assertThat("Price Breakdown section is displayed", flightProductReviewPageWeb.isPriceDetailInfoDisplayed(), is(true));

        step("User close the pop up");
        flightProductReviewPageWeb.closeFlightFacilitiesSection();

        step("User clicks 'Select' button on 'Economy' card");
        flightProductReviewPageWeb.clickSelectBtnRegular();

        step("Verify user see Flight details, login/register button, contact details, passenger Details, price details and Continue button");
        Assertion.assertThat("Booking Form page is visible", flightBookingFormPageWeb.isBookingFormVisible(), is(true));

        step("User click See other Details link");
        flightBookingFormPageWeb.openFlightFacilitySection();

        step("Verify user can see the details of facilities flight");
        Assertion.assertThat("Flight Facility button is visible", flightBookingFormPageWeb.isFlightFacilityVisible(), is(true));

        step("Close Flight Facility section");
        flightBookingFormPageWeb.closeFlightFacilitySection();

        step("User choose to see details refund/reschedule on booking 1st half form");
        flightBookingFormPageWeb.goOnFlightDetail();
        flightBookingFormPageWeb.clickOnSeeDetails();

        step("Verify user can see the Refund/Reschedule information");
        Assertion.assertThat("User can see the Refund/Reschedule information", flightBookingFormPageWeb.isRefundRescheduleVisible(), is(true));
        flightBookingFormPageWeb.closePopUp();

        step("Verify user can see the Booking Form 2nd half");
        Assertion.assertThat("Booking Form 2nd half is visible", flightBookingFormPageWeb.isBookingFormVisible(), is(true));

        step("User fill contact details and passenger details");
        flightBookingFormPageWeb.inputContactDetailsNonLogin(salutation,FullName,phoneNo,email);
        flightBookingFormPageWeb.fillPassengerDetailsLogin(adultPassengers, childPassengers, infantPassengers, "domestic", "");
        if (Configs.PLATFORM == Platform.AMWEB || Configs.PLATFORM == Platform.IMWEB) {
            flightBookingFormPageWeb.clickContinue();
        }
        step("User choose to add on Meals section on Booking Form");
        flightBookingFormPageWeb.selectMultiMealAddons(adultPassengers+childPassengers+infantPassengers,true);

        step("Verify multiple meals up to 2 meals for each passengers and for each segment");
        Assertion.assertThat("Multiple meals up to 2 meals for each passengers and for each segment are visible", flightBookingFormPageWeb.isMealSelectionVisible(), is(true));

        step("User tap on chevron button on the price section");
        flightBookingFormPageWeb.openPriceDetailSection();

        step("Verify Price Detail page which contain breakdown price include total price");
        Assertion.assertThat("Price Detail page is visible", flightBookingFormPageWeb.isPriceDetailPageVisible(), is(true));
        Assertion.assertThat("Total price is displayed", flightBookingFormPageWeb.isTotalPriceDisplayed(), is(true));
        flightBookingFormPageWeb.closePopUp();

        step("Verify user is on Payment Page after click Continue to Payment button");
        flightBookingFormPageWeb.clickGotItInBaggagePopup();
        flightBookingFormPageWeb.goToPaymentPageFromAddonsPage();
        flightBookingFormPageWeb.clickOkayGotIt();

        step("Go to the Order Confirmation page and verify the order is successful");
        Assertion.assertThat("PaymentPageLoaded", paymentPageWeb.isPaymentPageLoaded(), is(true));

    }

    @Author(names = "Aditi")
    @Module(name = "Booking")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void testBookingRTAddOnsExtraProtectionsLogin() throws FailedLoginException, InterruptedException, IOException {
        Map<String, String> param = getParam("testBookingRTAddOnsExtraProtectionsLogin");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPage = FlightLandingPageWeb.getInstance(session);
        FlightSearchResultPageWeb<?> searchResultPage = FlightSearchResultPageWeb.getInstance(session);
        FlightDescriptionPageWeb<?> flightDescriptionPage = FlightDescriptionPageWeb.getInstance(session);
        FlightProductReviewPageWeb<?> productReviewPage = FlightProductReviewPageWeb.getInstance(session);
        FlightBookingFormPageWeb<?> bookingFormPage = FlightBookingFormPageWeb.getInstance(session);
        FlightBookingFormPageMWeb<?> bookingFormPageMweb = (FlightBookingFormPageMWeb<?>) FlightBookingFormPageMWeb.getInstance(session);
        PaymentPageWeb<?> paymentPageWeb = PaymentPageWeb.getInstance(session);

        String email = param.get("email");
        String otp = param.get("otp");
        String password = param.get("password");
        String origin = param.get("origin");
        String destination = param.get("destination");
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        String currency = param.get("currency");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);
        String tripType = param.get("tripType");

        PriceDetail priceDetail = new PriceDetail();
        priceDetail.tripType = "roundtrip";
        priceDetail.adult = adultPassengers;
        priceDetail.child = childPassengers;
        priceDetail.infant = infantPassengers;

        step("User login");
        loginPage.loginIfNotWithSameEmail(email, password, otp, 3);

        step("Change currency to " + currency);
        homePage.selectCurrency(currency);

        step("User open Flight Landing Page");
        homePage.openVerticalPageViaURL("Flights");
        Assertion.assertThat("Flight Landing Page is Opened", flightLandingPage.isOnFlightPage(), is(true));

        step("User select origin");
        flightLandingPage.selectOrigin(origin);
        bookingFormPage.clickOkayGotIt();

        step("User select destination");
        flightLandingPage.selectDestination(destination);
        bookingFormPage.clickOkayGotIt();

        step("Select date");
        flightLandingPage.selectDate(departureDate);

        step("User update trip type to Round Trip");
        flightLandingPage.toggleTripType(tripType);

        step("add passenger");
        flightLandingPage.addPassenger(adultPassengers, childPassengers, infantPassengers);

        step("User click search button");
        flightLandingPage.searchFlight();
        Assertion.assertThat("Search Result Page is loaded", searchResultPage.isSearchResultPagePresent(), is(true));

        step("Wait for Search Result page to be fully loaded.");
        searchResultPage.isSRPLoaded();
        String url = searchResultPage.fetchURL();

        step("Click on floating promo button");
        searchResultPage.clickFloatingPromoButton();

        step("Verify promo page is displayed");
        Assertion.assertThat("Promo page should be displayed", searchResultPage.isPromoPageDisplayed(), is(true));

        step("Close promo page");
        searchResultPage.closePromoPage(url);

        step("select flight departure");
        searchResultPage.selectFlight();

        step("select flight return");
        searchResultPage.selectFlight();

        step("User is redirected to Product Review Page");
        Assertion.assertThat("Product Review page is visible", productReviewPage.isPresent(), is(true));

        step("User can see the schedule selection by user and can see the details");
        Assertion.assertThat("Departure schedule is displayed", productReviewPage.isDepartureScheduleVisible(), is(true));

        step("User scrolls to Economy Card and clicks 'See Details' hyperlink");
        productReviewPage.clickSeeDetailsUnderEconomyCard();

        step("User selects the 'Refund' tab in the popup");
        productReviewPage.clickPenaltyInfoTabRefund();

        step("Scroll and verify 'Refund', 'FAQ' and 'Other Important Info' sections are visible for Refund Tab");
        Assertion.assertThat("Refund Tab should display Refund, FAQ and Other Important Info sections", productReviewPage.verifyRefundTabSection(), is(true));

        step("User selects the 'Reschedule' tab in the popup");
        productReviewPage.clickPenaltyInfoTabReschedule();

        step("Scroll and verify 'Reschedule' and 'Other Important Info' sections are visible for Reschedule Tab");
        Assertion.assertThat("Reschedule Tab should display Reschedule and Other Important Info sections", productReviewPage.verifyRescheduleTabSection(), is(true));

        step("User selects the 'PRICE DETAIL' tab in the popup");
        productReviewPage.clickPenaltyInfoTabPriceDetail();

        step("Validate price breakdown and total calculation");
        Assertion.assertThat("Price Details section should be visible", productReviewPage.isPriceDetailsSectionVisible(), is(true));
        productReviewPage.validatePriceBreakdownAndTotal(priceDetail, currency);

        step("User clicks the close button on price detail bottom sheet");
        productReviewPage.closeBottomSheet();

        step("User is redirected to Product Review Page");
        Assertion.assertThat("Product Review page is visible again", productReviewPage.isPresent(), is(true));

        step("User clicks 'Select' button on 'Economy' card");
        productReviewPage.clickSelectBtnRegular();
        productReviewPage.clickNoThanksBtn();

        step("Verify user is directed to Booking Form page");
        bookingFormPage.waitForBookingPageToLoad();
        Assertion.assertThat("Booking Form page is visible", bookingFormPage.isBookingFormVisible(), is(true));

        step("Verify flight details are displayed");
        Assertion.assertThat("Flight details are displayed", bookingFormPage.isFlightDetailsVisible(), is(true));

        step("Fill Passenger Details");
        bookingFormPage.fillPassengerDetailsLogin(adultPassengers, childPassengers, infantPassengers, "domestic", "");

        bookingFormPage.clickContinue();
        step("Verify user can see the Booking Form 2nd half");
        Assertion.assertThat("Booking Form 2nd half is visible", bookingFormPage.isBookingFormVisible(), is(true));

        step("User choose More Info on Full Protection");
        bookingFormPage.clickMoreInfoFullProtection();

        step("User choose to see more Terms and Condition section by hyperlink");
        bookingFormPage.clickTermsAndConditionsLink();

        step("verify insurance pdf");
        bookingFormPage.verifyInsurancePdfByEnvironment();

        step("switch to booking tab");
        bookingFormPage.switchToChromeOrSafari();

        step("close more info popup");
        bookingFormPage.closeExtraProtectionBottomSheet();

        step("select insurance and get insurance data");
        InsuranceGeneralData insuranceGeneralData = bookingFormPage.selectInsurance(currency);

        step("Verify price detail");
        priceDetail.extraProtectionTitle=insuranceGeneralData.insuranceTitle;
        priceDetail.extraProtectionPrice=insuranceGeneralData.insurancePrice;
        bookingFormPage.isPriceBreakdownCorrect(priceDetail, currency);
        bookingFormPage.closePopUp();

        step("Verify user is on Payment Page after click Continue to Payment button");
        bookingFormPage.clickGotItInBaggagePopup();
        bookingFormPage.goToPaymentPageFromAddonsPage();

        step("Verify orderID on Payment Page");
        Assertion.assertThat("Payment page is loaded", paymentPageWeb.isPaymentPagepresent(), is(true));
        String orderID = paymentPageWeb.getOrderId();
        Assertion.assertThat("user get order id", orderID, allOf(not(nullValue()), not(equalTo(""))));

    }

    @Author(names = "Harshita")
    @Module(name = "Booking")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void testRTLoginultipaxTransitAddonsMealsMultiCurrency()  throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testRTLoginultipaxTransitAddonsMealsMultiCurrency");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPage = FlightLandingPageWeb.getInstance(session);
        FlightSearchResultPageWeb<?> searchResultPage = FlightSearchResultPageWeb.getInstance(session);
        FlightProductReviewPageWeb<?> productReviewPage = FlightProductReviewPageWeb.getInstance(session);
        FlightBookingFormPageWeb<?> bookingFormPage =  FlightBookingFormPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage=PaymentPageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        String origin = param.get("origin");
        String destination = param.get("destination");
        String airline = param.get("airline");
        int adultPassengers = Integer.parseInt(param.get("adultCount"));
        int childPassengers = Integer.parseInt(param.get("childCount"));
        int infantPassengers = Integer.parseInt(param.get("infantCount"));
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        int duration = Integer.parseInt(param.get("duration"));
        String departureDate = CoreFunctions.addToLongDate(currentDate,daysToDepartureDate);
        String returnDate = CoreFunctions.addToLongDate(departureDate, duration);
        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
        String tripType = param.get("tripType");
        String currency = param.get("currency");

        int loginAttemptCount = Integer.parseInt(param.get("loginAttemptCount"));

        PriceDetail priceDetail = new PriceDetail();
        priceDetail.tripType = tripType;
        priceDetail.adult = adultPassengers;
        priceDetail.child = childPassengers;
        priceDetail.infant = infantPassengers;

        step("Login as " + username + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(username, password);

        step("User select { " + currency + " } currency");
        homePage.selectCurrency(currency);

        step("User open Flight Landing Page");
        homePage.openVerticalPageViaURL("Flights");

        step("Toggle trip type");
        flightLandingPage.toggleTripType(tripType);

        step("User select origin");
        flightLandingPage.selectOrigin(origin);

        step("User select destination");
        flightLandingPage.selectDestination(destination);

        step("Select date");
        flightLandingPage.selectDate(departureDate,returnDate);

        step("add passenger");
        flightLandingPage.addPassenger(adultPassengers,childPassengers,infantPassengers);

        step("User click search button");
        flightLandingPage.searchFlight();
        Assertion.assertThat("Search Result Page is loaded", searchResultPage.isSearchResultPagePresent(), is(true));

        step("Wait for Search Result page to be fully loaded.");
        searchResultPage.isSRPLoaded();

        step("The return date displayed must be the last search");
        searchResultPage.goBackOnLandingPage();
        Assertion.assertThat("Flight Landing Page is Opened", flightLandingPage.isOnFlightPage(), is(true));
        Assertion.assertThat("Departure and return dates match the last search", flightLandingPage.isSelectedDatesSame(departureDate, returnDate), is(true));

        step("User click search button");
        flightLandingPage.searchFlight();
        Assertion.assertThat("Search Result Page is loaded", searchResultPage.isSearchResultPagePresent(), is(true));

        step("Wait for Search Result page to be fully loaded.");
        searchResultPage.isSRPLoaded();

        step("User click Transit Flight filter");
        searchResultPage.selectFlightWithSingleTransit();

        step("User choose filter Flight");
        searchResultPage.filterAirline(airline);

        step("select flight departure");
        searchResultPage.selectFlight();
        Assertion.assertThat("Search Result Page is loaded", searchResultPage.isSearchResultPagePresent(), is(true));

        step("User choose filter Flight for depart");
        searchResultPage.filterAirline(airline);

        step("select flight return");
        searchResultPage.selectFlight();

        step("User can see the schedule selection by user and can see the details");
        Assertion.assertThat("Departure schedule is displayed", productReviewPage.isDepartureScheduleVisible(), is(true));

        step("User clicks on 'See Other Facilities' hyperlink in flight details");
        productReviewPage.clickSeeOtherFacilities();

        step("User can see the popup with Flight Facilities Section");
        Assertion.assertThat("Facilities popup is displayed.", productReviewPage.isFacilitiesPopupVisible(), is(true));

        step("User selects the 'Refund' tab in the popup");
        productReviewPage.clickPenaltyInfoTabRefund();

        step("Verify Refund section");
        Assertion.assertThat("Refund section is displayed", productReviewPage.isRefundSectionVisible(), is(true));

        step("Verify Refund section FAQ Section");
        productReviewPage.verifyFAQSection();

        step("User selects the 'Reschedule' tab in the popup");
        productReviewPage.clickPenaltyInfoTabReschedule();

        step("Verify Reschedule section");
        Assertion.assertThat("Reschedule section is displayed", productReviewPage.isRescheduleSectionVisible(), is(true));

        step("Verify Reschedule section FAQ Section");
        productReviewPage.verifyFAQSection();

        step("User selects the 'PRICE DETAIL' tab in the popup");
        productReviewPage.clickPenaltyInfoTabPriceDetail();

        step("User clicks the close button on penalty info popup");
        productReviewPage.closePopUp();

        step("User clicks 'Select' button on 'Economy' card");
        productReviewPage.clickSelectBtnRegular();

        step("Verify user is directed to Booking Form page");
        bookingFormPage.clickOkayGotIt();
        bookingFormPage.waitForBookingPageToLoad();
        Assertion.assertThat("Booking Form page is visible", bookingFormPage.isBookingFormVisible(), is(true));

        step("User navigates to Flight Details section");
        bookingFormPage.goOnFlightDetail();

        step("Verify Flight Schedule tab is visible");
        Assertion.assertThat("Flight Schedule tab is displayed", bookingFormPage.isFlightScheduleTabVisible(), is(true));

        step("User Click see details to see refund reschedule info");
        bookingFormPage.clickOnSeeDetails();

        step("User can see refund reschedule info");
        Assertion.assertThat("Flight Facility button is visible", bookingFormPage.isRRPopUpAppear(), is(true));
        bookingFormPage.closeRRPopUp();

        step("User go on tiket Policies");
        bookingFormPage.closePopUp();

        step("Fill passenger details");
        bookingFormPage.fillPassengerDetailsLogin(adultPassengers, childPassengers, infantPassengers, "international", airline);
        bookingFormPage.reloadPage();

        step("User choose to add on Meals section on Booking Form");
        bookingFormPage.selectMultiMealAddons(adultPassengers+childPassengers+infantPassengers,false);

        step("Verify multiple meals up to 2 meals for each passengers and for each segment");
        Assertion.assertThat("Multiple meals up to 2 meals for each passengers and for each segment are visible", bookingFormPage.isMealSelectionVisible(), is(true));

        step("User tap on chevron button on the price section");
        bookingFormPage.openPriceDetailSection();

        step("Verify Price Detail page which contain breakdown price include total price");
        Assertion.assertThat("Price Detail page is visible", bookingFormPage.isPriceDetailPageVisible(), is(true));
        Assertion.assertThat("Total price is displayed", bookingFormPage.isTotalPriceDisplayed(), is(true));
        bookingFormPage.closePopUp();

        step("Go to payment page");
        bookingFormPage.goToPaymentPageFromBookingFormPage();

        step("verify Review Passenger's Data");
        bookingFormPage.clickItsCorrectOnReviewPassengerDataPopup();
        bookingFormPage.clickNoThanksOnFlightMayReschedulePopup();

        step("Payment page should be displayed");
        Assertion.assertThat("PaymentPageLoaded", paymentPage.isPaymentPagepresent(), is(true));
        String orderID = paymentPage.getOrderId();
        orderID = String.valueOf(getOrderId(orderID));
        Assertion.assertThat("OrderID is Visible " + orderID, !orderID.isEmpty(), is(true));
    }

    @Author(names = "Rohan")
    @Module(name = "Booking")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void testLoginBookingDirectRT2MultiPaxDomesticWithExtraProtection() throws Exception {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testLoginBookingDirectRT2MultiPaxDomesticWithExtraProtection");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPage = FlightLandingPageWeb.getInstance(session);
        FlightSearchResultPageWeb<?> flightSearchResultPageWeb = FlightSearchResultPageWeb.getInstance(session);
        FlightProductReviewPageWeb<?> flightProductReviewPageWeb = FlightProductReviewPageWeb.getInstance(session);
        FlightBookingFormPageWeb<?> flightBookingFormPageWeb = FlightBookingFormPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPageWeb = PaymentPageWeb.getInstance(session);
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int duration = Integer.parseInt(param.get("duration"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String origin = param.get("origin");
        String destination = param.get("destination");
        String tripType = param.get("tripType");
        String departureDate = CoreFunctions.addToLongDate(currentDate,daysToDepartureDate);
        String returnDate = CoreFunctions.addToLongDate(departureDate, duration);
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        String username = param.get("username");
        String password = param.get("password");
        String currency = param.get("currency");
        String airlineNameDepart = param.get("airlineNameDepart");
        String airlineNameReturn = param.get("airlineNameReturn");

        step("Login as " + username + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(username, password);

        step("Change Currency to { " + currency + " }");
        homePage.selectCurrency(currency);

        step("User open Flight Landing Page");
        homePage.openVerticalPageViaURL("Flights");

        step("Toggle trip type");
        flightLandingPage.toggleTripType(tripType);

        step("User select origin");
        flightLandingPage.selectOrigin(origin);
        flightBookingFormPageWeb.clickOkayGotIt();

        step("User select destination");
        flightLandingPage.selectDestination(destination);
        flightBookingFormPageWeb.clickOkayGotIt();

        step("Select date");
        flightLandingPage.selectDate(departureDate,returnDate);

        step("add passenger");
        flightLandingPage.addPassenger(adultPassengers,childPassengers,infantPassengers);

        step("User click search button");
        flightLandingPage.searchFlight();
        Assertion.assertThat("Search Result Page is loaded", flightSearchResultPageWeb.isSearchResultPagePresent(), is(true));

        step("Wait for Search Result page to be fully loaded.");
        flightSearchResultPageWeb.isSRPLoaded();

        step("User click Direct Flight filter");
        flightSearchResultPageWeb.selectDirectFlightFilter();

        step("Apply Filter " + airlineNameDepart);
        flightSearchResultPageWeb.filterAirline(airlineNameDepart);

        step("select flight");
        flightSearchResultPageWeb.selectFlightByAirline(airlineNameDepart);
        Assertion.assertThat("Search Result Page is loaded", flightSearchResultPageWeb.isSearchResultPagePresent(), is(true));

        step("User click Direct Flight filter");
        flightSearchResultPageWeb.selectDirectFlightFilter();

        step("Apply Filter " + airlineNameReturn);
        flightSearchResultPageWeb.filterAirline(airlineNameReturn);

        step("select flight");
        flightSearchResultPageWeb.selectFlightByAirline(airlineNameReturn);

        step("User can see the schedule selection by user and can see the details");
        Assertion.assertThat("Departure schedule is displayed", flightProductReviewPageWeb.isDepartureScheduleVisible(), is(true));

        step("User clicks on 'See Other Facilities' hyperlink in flight details");
        flightProductReviewPageWeb.clickSeeOtherFacilitiesRR();

        step("User can see the popup with Flight Facilities Section");
        Assertion.assertThat("Facilities popup is displayed.", flightProductReviewPageWeb.isFacilitiesPopupVisible(), is(true));

        step("User selects the 'Reschedule' tab in the popup");
        flightProductReviewPageWeb.clickPenaltyInfoTabReschedule();

        step("User selects the 'PRICE DETAIL' tab in the popup");
        flightProductReviewPageWeb.clickPenaltyInfoTabPriceDetail();

        step("User clicks the close button on penalty info popup");
        flightProductReviewPageWeb.closePopUp();

        step("User clicks 'Select' button on 'Economy' card");
        flightProductReviewPageWeb.clickSelectBtnRegular();

        step("Verify user is directed to Booking Form page");
        flightBookingFormPageWeb.clickOkayGotIt();
        flightBookingFormPageWeb.waitForBookingPageToLoad();
        Assertion.assertThat("Booking Form page is visible", flightBookingFormPageWeb.isBookingFormVisible(), is(true));

        step("Click toggle to fill passengers details and save ");
        flightBookingFormPageWeb.fillPassengerDetailsLogin(adultPassengers,childPassengers,infantPassengers,"domestic","");

        step("Select Insurance");
        flightBookingFormPageWeb.selectInsurance(currency);

        step("going to paymentPage");
        flightBookingFormPageWeb.goToPaymentPageFromBookingFormPage();

        step("verify Review Passenger's Data");
        flightBookingFormPageWeb.clickItsCorrectOnReviewPassengerDataPopup();
        flightBookingFormPageWeb.clickNoThanksOnFlightMayReschedulePopupPRP();

        step("Payment page should be displayed");
        Assertion.assertThat("PaymentPageLoaded", paymentPageWeb.isPaymentPagepresent(), is(true));
        String orderID = paymentPageWeb.getOrderId();
        orderID = String.valueOf(getOrderId(orderID));
        Assertion.assertThat("OrderIDVisible "+ orderID, !orderID.isEmpty(), is(true));
    }

    @Author(names = "Harshita")
    @Module(name = "Booking")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void testBookingOnePaxIntlRT2BookingCode() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testBookingOnePaxIntlRT2BookingCode");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPage = FlightLandingPageWeb.getInstance(session);
        FlightSearchResultPageWeb<?> flightSearchResultPageWeb = FlightSearchResultPageWeb.getInstance(session);
        FlightDescriptionPageWeb<?> flightDescriptionPage = FlightDescriptionPageWeb.getInstance(session);
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
        int duration = Integer.parseInt(param.get("duration"));
        String departureDate = CoreFunctions.addToLongDate(currentDate,daysToDepartureDate);
        String returnDate = CoreFunctions.addToLongDate(departureDate, duration);
        String FullName = param.get("fullName");
        String salutation = randomSalutation();
        String phoneNo = randomPhone();
        String email = randomEmail();
        String year = param.get("year");
        String month = param.get("month");
        String day = param.get("day");
        String airlineNameDepart = param.get("airlineNameDepart");
        String airlineNameReturn = param.get("airlineNameReturn");

        step("User open Home Page");
        homePage.goToHomePage();

        step("User open Flight Landing Page");
        homePage.openVerticalPageViaURL("Flights");
        Assertion.assertThat("Flight Landing Page is Opened", flightLandingPage.isOnFlightPage(), is(true));

        step("User select origin");
        flightLandingPage.selectOrigin(origin);

        step("Select destination airport");
        flightLandingPage.selectDestination(destination);

        step("Toggle trip type");
        flightLandingPage.toggleTripType("roundtrip");

        step("Select date");
        flightLandingPage.selectDate(departureDate,returnDate);

        step("Add passenger");
        flightLandingPage.addPassenger(adultPassengers, childPassengers, infantPassengers);

        step("search flight");
        flightLandingPage.searchFlight();

        step("filter airline Depart {" + airlineNameDepart + " }");
        flightSearchResultPageWeb.filterAirline(airlineNameDepart);

        step("select flight for departure");
        flightSearchResultPageWeb.selectFlightByAirline(airlineNameDepart);

        step("filter airline Return {" + airlineNameReturn + " }");
        flightSearchResultPageWeb.filterAirline(airlineNameReturn);

        step("select flight for Return");
        flightSearchResultPageWeb.selectFlightByAirline(airlineNameReturn);

        step("User is redirected to Product Review Page");
        Assertion.assertThat("Product Review page is visible", productReviewPage.isPresent(), is(true));

        step("User can see the schedule selection by user and can see the details");
        Assertion.assertThat("Departure schedule is displayed", productReviewPage.isDepartureScheduleVisible(), is(true));

        step("User clicks on 'See Other Facilities' hyperlink in flight details");
        productReviewPage.clickSeeOtherFacilities();

        step("User can see the popup with Flight Facilities Section");
        Assertion.assertThat("Facilities popup is displayed.", productReviewPage.isFacilitiesPopupVisible(), is(true));

        step("User closes the facilities popup");
        productReviewPage.closePopUp();

        step("User scrolls to Economy Card and clicks 'See Details' hyperlink");
        productReviewPage.clickSeeDetailsUnderEconomyCard();

        step("User is on Penalty Info popup in Product Review Page");
        Assertion.assertThat("Penalty Info popup is visible", productReviewPage.isPenaltyInfoVisible(), is(true));

        step("User selects the 'Reschedule' tab in the popup");
        productReviewPage.clickPenaltyInfoTabReschedule();

        step("User selects the 'PRICE DETAIL' tab in the popup");
        productReviewPage.clickPenaltyInfoTabPriceDetail();

        step("User clicks the close button on penalty info popup");
        productReviewPage.closePopUp();

        step("User clicks 'Select' button on 'Economy' card");
        productReviewPage.clickSelectBtnRegular();

        step("User clicks 'Select' button on 'Economy' card Return");
        productReviewPage.clickSelectBtnRegularReturn();

        step("Verify user is directed to Booking Form page");
        bookingFormPage.waitForBookingPageToLoad();
        Assertion.assertThat("Booking Form page is visible", bookingFormPage.isBookingFormVisible(), is(true));

        step("Verify flight details are displayed");
        Assertion.assertThat("Flight details are displayed", bookingFormPage.isFlightDetailsVisible(), is(true));

        step("User navigates to Flight Details section");
        bookingFormPage.goOnFlightDetail();

        step("Verify Flight Schedule tab is visible");
        Assertion.assertThat("Flight Schedule tab is displayed", bookingFormPage.isFlightScheduleTabVisible(), is(true));

        step("User Selects Details of Flight Facility");
        bookingFormPage.clickOnFacilitiesDetail();

        step("User can see Flight Facilities");
        Assertion.assertThat("Flight Facility button is visible", bookingFormPage.isFlightFacilityVisible(), is(true));
        bookingFormPage.closeSecondPopup();
        step("User go on tiket Policies");
        bookingFormPage.closePopUp();

        step("Fill Passenger Details");
        bookingFormPage.inputContactDetailsNonLogin(salutation, FullName, phoneNo, email);
        bookingFormPage.fillPassengerDetailsLogin(adultPassengers, childPassengers, infantPassengers, "international", airlineNameDepart);

        step("User taps on chevron icon on price section to view Price Details");
        bookingFormPage.openPriceDetailSection();

        step("User is redirected to Price Detail page");
        Assertion.assertThat("Price Detail Page is displayed", bookingFormPage.isPriceDetailPageVisible(), is(true));

        step("Verify Total Price Displayed");
        Assertion.assertThat("Total price is displayed", bookingFormPage.isTotalPriceDisplayed(), is(true));

        step("User closes the price detail page");
        bookingFormPage.closePopUp();

        step("User is redirected back to Booking Form");
        Assertion.assertThat("Booking Form page is visible again", bookingFormPage.isBookingFormVisible(), is(true));

        step("going to paymentPage");
        bookingFormPage.goToPaymentPageFromBookingFormPage();
        bookingFormPage.clickNoThanksOnFlightMayReschedulePopup();

        step("click I don't want it");
        bookingFormPage.clickOkayGotIt();

        step("Payment page should be displayed");
        Assertion.assertThat("PaymentPageLoaded", paymentPageWeb.isPaymentPagepresent(), is(true));
        String orderID = paymentPageWeb.getOrderId();
        orderID = String.valueOf(getOrderId(orderID));
        Assertion.assertThat("OrderIDVisible " + orderID, !orderID.isEmpty(), is(true));
    }

    @Author(names = "Priyanka")
    @Module(name = "Booking")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void testNonLoginBookingDirectRTMultiPaxWithExtraProtection() throws Exception {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testNonLoginBookingDirectRTMultiPaxWithExtraProtection");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPage = FlightLandingPageWeb.getInstance(session);
        FlightSearchResultPageWeb<?> flightSearchResultPageWeb = FlightSearchResultPageWeb.getInstance(session);
        FlightProductReviewPageWeb<?> flightProductReviewPageWeb = FlightProductReviewPageWeb.getInstance(session);
        FlightBookingFormPageWeb<?> flightBookingFormPageWeb = FlightBookingFormPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPageWeb = PaymentPageWeb.getInstance(session);
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int duration = Integer.parseInt(param.get("duration"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String origin = param.get("origin");
        String destination = param.get("destination");
        String tripType = param.get("tripType");
        String airlineNameDepart = param.get("airlineNameDepart");
        String airlineNameReturn = param.get("airlineNameReturn");
        String departureDate = CoreFunctions.addToLongDate(currentDate,daysToDepartureDate);
        String returnDate = CoreFunctions.addToLongDate(departureDate, duration);
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        String currency = param.get("currency");
        String FullName = param.get("fullName");
        String salutation = randomSalutation();
        String phoneNo = randomPhone();
        String email = randomEmail();

        step("User open Home Page");
        homePage.goToHomePage();

        step("Logout via URL");
        loginPage.logoutViaUrl();

        step("User select IDR currency");
        homePage.selectCurrency(currency);

        step("User open Flight Landing Page");
        homePage.openVerticalPageViaURL("Flights");

        step("Toggle trip type");
        flightLandingPage.toggleTripType(tripType);

        step("User select origin");
        flightLandingPage.selectOrigin(origin);
        flightBookingFormPageWeb.clickOkayGotIt();

        step("User select destination");
        flightLandingPage.selectDestination(destination);
        flightBookingFormPageWeb.clickOkayGotIt();

        step("Select date");
        flightLandingPage.selectDate(departureDate,returnDate);

        step("add passenger");
        flightLandingPage.addPassenger(adultPassengers,childPassengers,infantPassengers);

        step("User click search button");
        flightLandingPage.searchFlight();
        Assertion.assertThat("Search Result Page is loaded", flightSearchResultPageWeb.isSearchResultPagePresent(), is(true));

        step("Wait for Search Result page to be fully loaded.");
        flightSearchResultPageWeb.isSRPLoaded();

        step("Apply Filter " + airlineNameDepart);
        flightSearchResultPageWeb.filterAirline(airlineNameDepart);

        step("select flight departure");
        flightSearchResultPageWeb.selectFlightByAirline(airlineNameDepart);


        step("Apply Filter " + airlineNameReturn);
        flightSearchResultPageWeb.filterAirline(airlineNameReturn);

        step("User click Direct Flight filter");
        flightSearchResultPageWeb.selectDirectFlightFilter();

        step("select flight");
        flightSearchResultPageWeb.selectFlightByAirline(airlineNameReturn);

        step("User is redirected to Product Review Page");

        step("User can see the schedule selection by user and can see the details");
        Assertion.assertThat("Departure schedule is displayed", flightProductReviewPageWeb.isDepartureScheduleVisible(), is(true));

        step("User clicks on 'See Other Facilities' hyperlink in flight details");
        flightProductReviewPageWeb.clickSeeOtherFacilities();

        step("User can see the popup with Flight Facilities Section");
        Assertion.assertThat("Facilities popup is displayed.", flightProductReviewPageWeb.isFacilitiesPopupVisible(), is(true));

        step("User selects the 'Reschedule' tab in the popup");
        flightProductReviewPageWeb.clickPenaltyInfoTabReschedule();

        step("User selects the 'PRICE DETAIL' tab in the popup");
        flightProductReviewPageWeb.clickPenaltyInfoTabPriceDetail();

        step("User clicks the close button on penalty info popup");
        flightProductReviewPageWeb.closePopUp();

        step("User clicks 'Select' button on 'Economy' card");
        flightProductReviewPageWeb.clickSelectBtnRegular();

        step("Verify user is directed to Booking Form page");
        flightBookingFormPageWeb.clickOkayGotIt();
        flightBookingFormPageWeb.waitForBookingPageToLoad();
        Assertion.assertThat("Booking Form page is visible", flightBookingFormPageWeb.isBookingFormVisible(), is(true));

        step("Verify flight details are displayed");
        Assertion.assertThat("Flight details are displayed", flightBookingFormPageWeb.isFlightDetailsVisible(), is(true));

        step("User navigates to Flight Details section");
        flightBookingFormPageWeb.goOnFlightDetail();

        step("Verify Flight Schedule tab is visible");
        Assertion.assertThat("Flight Schedule tab is displayed", flightBookingFormPageWeb.isFlightScheduleTabVisible(), is(true));

        step("User Selects Details of Flight Facility");
        flightBookingFormPageWeb.clickOnFacilitiesDetail();

        step("User can see Flight Facilities");
        Assertion.assertThat("Flight Facility button is visible", flightBookingFormPageWeb.isFlightFacilityVisible(), is(true));
        flightBookingFormPageWeb.closeSecondPopup();

        step("User Click see details to see refund reschedule info");
        flightBookingFormPageWeb.clickOnSeeDetails();

        step("User can see refund reschedule info");
        Assertion.assertThat("Flight Facility button is visible", flightBookingFormPageWeb.isRRPopUpAppear(), is(true));
        flightBookingFormPageWeb.closeRRPopUp();

        step("User go on tiket Policies");
        flightBookingFormPageWeb.closePopUp();

        step("Input Contact Name");
        flightBookingFormPageWeb.inputContactDetailsNonLogin(salutation, FullName, phoneNo, email);

        step("Click toggle to fill passengers details and save ");
        flightBookingFormPageWeb.fillPassengerDetailsLogin(adultPassengers, childPassengers, infantPassengers, "domestic", "");

        step("Select Insurance");
        flightBookingFormPageWeb.insuranceSelector();

        step("User taps on chevron icon on price section to view Price Details");
        flightBookingFormPageWeb.openPriceDetailSection();

        step("User is redirected to Price Detail page");
        Assertion.assertThat("Price Detail Page is displayed", flightBookingFormPageWeb.isPriceDetailPageVisible(), is(true));

        step("Verify Total Price Displayed");
        Assertion.assertThat("Total price is displayed", flightBookingFormPageWeb.isTotalPriceDisplayed(), is(true));

        step("User closes the price detail page");
        flightBookingFormPageWeb.closePopUp();

        step("User is redirected back to Booking Form");
        Assertion.assertThat("Booking Form page is visible again", flightBookingFormPageWeb.isBookingFormVisible(), is(true));

        step("going to paymentPage");
        flightBookingFormPageWeb.goToPaymentPageFromBookingFormPage();
        flightBookingFormPageWeb.clickNoThanksOnFlightMayReschedulePopup();

        step("click I don't want it");
        flightBookingFormPageWeb.clickOkayGotIt();

        step("Payment page should be displayed");
        Assertion.assertThat("PaymentPageLoaded", paymentPageWeb.isPaymentPagepresent(), is(true));
        String orderID = paymentPageWeb.getOrderId();
        orderID = String.valueOf(getOrderId(orderID));
        Assertion.assertThat("OrderIDVisible "+ orderID, !orderID.isEmpty(), is(true));
    }

    @Author(names = "Priyanka Rana")
    @Module(name = "Booking")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void checkDomesticRoundTransitMealNonIDRNonLogin()  throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("checkDomesticRoundTransitMealNonIDRNonLogin");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPage = FlightLandingPageWeb.getInstance(session);
        FlightSearchResultPageWeb<?> flightSearchResultPageWeb = FlightSearchResultPageWeb.getInstance(session);
        FlightProductReviewPageWeb<?> flightProductReviewPageWeb = FlightProductReviewPageWeb.getInstance(session);
        FlightBookingFormPageWeb<?> flightBookingFormPageWeb = FlightBookingFormPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPageWeb = PaymentPageWeb.getInstance(session);
        PromoPageWeb<?> promoPageWeb = PromoPageWeb.getInstance(session);

        int daysToDepartureDate = ThreadLocalRandom.current().nextInt(60, 90);
        int daysToReturnDate = ThreadLocalRandom.current().nextInt(1, 5);

        String origin = param.get("origin");
        String destination = param.get("destination");
        String tripType = param.get("tripType");
        String airlineName = param.get("airlineName");
        String filterType = param.get("filterType");
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate,daysToDepartureDate);
        String returnDate = CoreFunctions.addToLongDate(departureDate, daysToReturnDate);
        String currency = param.get("currency");
        String FullName = randomName();
        String salutation = randomSalutation();
        String phoneNo = randomPhone();
        String email = randomEmail();

        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));

        step("User open Home Page");
        homePage.goToHomePage();

        step("Change Currency to non IDR");
        homePage.selectCurrency(currency);

        step("User open Flight Landing Page");
        homePage.openVerticalPageViaURL("Flights");
        Assertion.assertThat("Flight Landing Page is Opened", flightLandingPage.isOnFlightPage(), is(true));

        step("User select origin and destination");
        flightLandingPage.selectOrigin(origin);
        flightLandingPage.selectDestination(destination);

        step("User update trip type to Round Trip");
        flightLandingPage.toggleTripType(tripType);

        step("User select date");
        flightLandingPage.selectDate(departureDate,returnDate);

        step("User Add Passenger");
        flightLandingPage.addPassenger(adultPassengers,childPassengers,infantPassengers);

        step("User click search button");
        flightLandingPage.searchFlight();

        step("Verify Search Result Page is loaded");
        Assertion.assertThat("Search Result Page is loaded", flightSearchResultPageWeb.isSearchResultPagePresent(), is(true));

        step("filter airline {" + airlineName + " }" );
        flightSearchResultPageWeb.filterAirline(airlineName);

        step("User Select Meal Filter on SRP");
        flightSearchResultPageWeb.otherFilters(filterType);

        step("select flight for departure");
        flightSearchResultPageWeb.selectFlightByAirline(airlineName);

        step("Apply Filter for Return Flight");
        flightSearchResultPageWeb.selectFlightByAirline(airlineName);

        step("User is redirected to Product Review Page");
        Assertion.assertThat("Product Review page is visible", flightProductReviewPageWeb.isPresent(), is(true));

        step("User clicks 'Select' button on 'Economy' card");
        flightProductReviewPageWeb.clickSelectBtnRegular();

        step("Verify user is directed to Booking Form page");
        flightBookingFormPageWeb.clickOkayGotIt();

        step("User fill contact details and passenger details");
        flightBookingFormPageWeb.inputContactDetailsNonLogin(salutation,FullName,phoneNo,email);
        flightBookingFormPageWeb.fillPassengerDetailsLogin(adultPassengers, childPassengers, infantPassengers, "domestic", "");

        step("User choose to add on Meals section on Booking Form");
        flightBookingFormPageWeb.selectMultiMealAddons(adultPassengers + childPassengers + infantPassengers, false);

        step("User tap on chevron button on the price section");
        flightBookingFormPageWeb.openPriceDetailSection();

        step("Verify Price Detail page which contain breakdown price include total price");
        Assertion.assertThat("Price Detail page is visible", flightBookingFormPageWeb.isPriceDetailPageVisible(), is(true));
        Assertion.assertThat("Total price is displayed", flightBookingFormPageWeb.isTotalPriceDisplayed(), is(true));
        flightBookingFormPageWeb.closePopUp();

        step("Verify user is on Payment Page after click Continue to Payment button");
        flightBookingFormPageWeb.clickGotItInBaggagePopup();
        flightBookingFormPageWeb.goToPaymentPageFromAddonsPage();
        flightBookingFormPageWeb.clickOkayGotIt();

        step("Go to the Order Confirmation page and verify the order is successful");
        Assertion.assertThat("PaymentPageLoaded", paymentPageWeb.isPaymentPageLoaded(), is(true));

    }

    @Author(names = "Rohan")
    @Module(name = "Booking")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void testLoginBookingDirectRT2BookingCodeSinglePaxIntl() throws Exception {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testLoginBookingDirectRT2BookingCodeSinglePaxIntl");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPage = FlightLandingPageWeb.getInstance(session);
        FlightSearchResultPageWeb<?> flightSearchResultPageWeb = FlightSearchResultPageWeb.getInstance(session);
        FlightProductReviewPageWeb<?> flightProductReviewPageWeb = FlightProductReviewPageWeb.getInstance(session);
        FlightBookingFormPageWeb<?> flightBookingFormPageWeb = FlightBookingFormPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPageWeb = PaymentPageWeb.getInstance(session);

        int daysToDepartureDate = ThreadLocalRandom.current().nextInt(3, 30);

        String origin = param.get("origin");
        String destination = param.get("destination");
        String airlineNameDepart = param.get("airlineNameDepart");
        String airlineNameReturn = param.get("airlineNameReturn");
        int duration = Integer.parseInt(param.get("duration"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate,daysToDepartureDate);
        String returnDate = CoreFunctions.addToLongDate(departureDate, duration);
        String username = param.get("username");
        String otp = param.get("otp");
        String password = param.get("password");

        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));

        step("User login");
        loginPage.loginIfNotWithSameEmail(username, password, otp, 1);

        step("User open Flight Landing Page");
        homePage.openVerticalPageViaURL("Flights");
        Assertion.assertThat("Flight Landing Page is Opened", flightLandingPage.isOnFlightPage(), is(true));

        step("Toggle trip type");
        flightLandingPage.toggleTripType("roundTrip");

        step("User select origin");
        flightLandingPage.selectOrigin(origin);
        flightBookingFormPageWeb.clickOkayGotIt();

        step("User select destination");
        flightLandingPage.selectDestination(destination);
        flightBookingFormPageWeb.clickOkayGotIt();

        step("Select date");
        flightLandingPage.selectDate(departureDate,returnDate);

        step("add passenger");
        flightLandingPage.addPassenger(adultPassengers,childPassengers,infantPassengers);

        step("User click search button");
        flightLandingPage.searchFlight();
        Assertion.assertThat("Search Result Page is loaded", flightSearchResultPageWeb.isSearchResultPagePresent(), is(true));

        step("Wait for Search Result page to be fully loaded.");
        flightSearchResultPageWeb.isSRPLoaded();

        step("User click direct Flight filter");
        flightSearchResultPageWeb.selectDirectFlightFilter();

        step("Filter airline {" + airlineNameDepart + " }");
        flightSearchResultPageWeb.filterAirline(airlineNameDepart);

        step("Select flight for departure");
        flightSearchResultPageWeb.selectFlightByAirline(airlineNameDepart);

        step("User click Direct Flight filter for return flight");
        flightSearchResultPageWeb.selectDirectFlightFilter();

        step("Filter airline {" + airlineNameReturn + " }");
        flightSearchResultPageWeb.filterAirline(airlineNameReturn);

        step("Select flight for departure");
        flightSearchResultPageWeb.selectFlightByAirline(airlineNameReturn);

        step("User is redirected to Product Review Page");
        Assertion.assertThat("Product Review page is visible", flightProductReviewPageWeb.isPresent(), is(true));

        step("User clicks 'Select' button on 'Economy' card");
        flightProductReviewPageWeb.clickSelectBtnRegular();

        step("Verify user is directed to Booking Form page");
        flightBookingFormPageWeb.clickOkayGotIt();

        step("Click same contact toggle for passenger details");
        flightBookingFormPageWeb.clickSameContactToggle();

        step("going to paymentPage");
        flightBookingFormPageWeb.goToPaymentPageFromBookingFormPage();
        flightBookingFormPageWeb.clickNoThanksOnFlightMayReschedulePopup();

        step("click I don't want it");
        flightBookingFormPageWeb.clickOkayGotIt();
        step("Handle optional baggage popup if present");
        flightBookingFormPageWeb.clickGotItInBaggagePopup();
        step("Payment page should be displayed");
        String orderID = paymentPageWeb.getOrderId();
        orderID = String.valueOf(getOrderId(orderID));
        Assertion.assertThat("OrderIDVisible "+ orderID, !orderID.isEmpty(), is(true));
    }

    @Author(names = "Rohan")
    @Module(name = "Booking")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void testCompleteBookingWithBestSuitedPromoDweb() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testCompleteBookingWithBestSuitedPromoDweb");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPage = FlightLandingPageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        FlightSearchResultPageWeb<?> flightSearchResultPageWeb = FlightSearchResultPageWeb.getInstance(session);
        FlightProductReviewPageWeb<?> flightProductReviewPageWeb = FlightProductReviewPageWeb.getInstance(session);
        FlightBookingFormPageWeb<?> flightBookingFormPageWeb = FlightBookingFormPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPageWeb = PaymentPageWeb.getInstance(session);
        String otp = param.get("otp");
        String origin = param.get("originAirport");
        String destination = param.get("destinationAirport");
        String currency = param.get("currency");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int duration = Integer.parseInt(param.get("duration"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);
        String returnDate = CoreFunctions.addToLongDate(departureDate, duration);
        String username = param.get("username");
        String password = param.get("password");

        step("User Login ");
        loginPage.loginIfNotWithSameEmail(username, password, otp, 1);

        step("User select { " + currency + " } currency");
        homePage.selectCurrency(currency);

        step("User open Flight Landing Page");
        homePage.openVerticalPageViaURL("Flights");
        Assertion.assertThat("Flight Landing Page is Opened", flightLandingPage.isOnFlightPage(), is(true));

        step("User select origin");
        flightLandingPage.selectOrigin(origin);

        step("Select destination airport");
        flightLandingPage.selectDestination(destination);

        step("Select date");
        flightLandingPage.selectDate(departureDate,returnDate);

        step("Toggle trip type");
        flightLandingPage.toggleTripType("oneway");

        step("search flight");
        flightLandingPage.searchFlight();

        step("Wait for Search Result page to be fully loaded.");
        flightSearchResultPageWeb.isSRPLoaded();

        step("Wait for Infinity promo to be visible.");
        Assertion.assertThat("Infinity promo should be visible", flightSearchResultPageWeb.isInfinityPromoVisible(), is(true));

        step("Apply Infinity promo.");
        flightSearchResultPageWeb.ApplyInfinityPromo();

        step("User Selects Any Flight");
        flightSearchResultPageWeb.selectFlight();

        step("User Selects SELECT button in ECONOMY section");
        flightProductReviewPageWeb.clickSelectBtnRegular();

        step("Continue to booking form");
        flightProductReviewPageWeb.continueToBookingForm();
        flightBookingFormPageWeb.clickNoThanksOnFlightMayReschedulePopup();

        step("Click toggle to fill passengers details and save ");
        flightBookingFormPageWeb.fillPassengerDetails();

        step("Go to payment page");
        flightBookingFormPageWeb.goToPaymentPageFromBookingFormPage();
        flightBookingFormPageWeb.clickNoThanksOnFlightMayReschedulePopup();
        flightBookingFormPageWeb.clickOkayGotIt();

        step("Payment page should be displayed");
        Assertion.assertThat("PaymentPageLoaded", paymentPageWeb.isPaymentPagepresent(), is(true));
        String orderID = String.valueOf(paymentPageWeb.getOrderId());
        Assertion.assertThat("OrderIDVisible " + orderID, !orderID.isEmpty(), is(true));
    }

    @Author(names = "Rohan")
    @Module(name = "Booking")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void nonLoginOneWayIntlMultiPaxQuietZoneD7()  throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("nonLoginOneWayIntlMultiPaxQuietZoneD7");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPage = FlightLandingPageWeb.getInstance(session);
        FlightSearchResultPageWeb<?> searchResultPage = FlightSearchResultPageWeb.getInstance(session);
        FlightProductReviewPageWeb<?> productReviewPage = FlightProductReviewPageWeb.getInstance(session);
        FlightBookingFormPageWeb<?> bookingFormPage =  FlightBookingFormPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPageWeb=PaymentPageWeb.getInstance(session);

        String origin = param.get("origin");
        String destination = param.get("destination");
        int adultPassengers = Integer.parseInt(param.get("adultCount"));
        int childPassengers = Integer.parseInt(param.get("childCount"));
        int infantPassengers = Integer.parseInt(param.get("infantCount"));
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate,daysToDepartureDate);

        // Removed hardcoded passenger details (FullName, year, month, day)
        String salutation = randomSalutation();
        String phoneNo = randomPhone();
        String email = randomEmail();
        String airline = param.get("airlineName");

        step("User open Home Page");
        homePage.goToHomePage();

        step("User open Flight Landing Page");
        homePage.openVerticalPageViaURL("Flights");
        Assertion.assertThat("Flight Landing Page is Opened", flightLandingPage.isOnFlightPage(), is(true));

        step("User select origin");
        flightLandingPage.selectOrigin(origin);
        bookingFormPage.clickOkayGotIt();

        step("User select destination");
        flightLandingPage.selectDestination(destination);
        bookingFormPage.clickOkayGotIt();

        step("Select date");
        flightLandingPage.selectDate(departureDate);

        step("add passenger");
        flightLandingPage.addPassenger(adultPassengers,childPassengers,infantPassengers);

        step("User click search button");
        flightLandingPage.searchFlight();
        Assertion.assertThat("Search Result Page is loaded", searchResultPage.isSearchResultPagePresent(), is(true));

        step("Wait for Search Result page to be fully loaded.");
        searchResultPage.isSRPLoaded();

        step("Apply Filter " + airline);
        searchResultPage.filterAirline(airline);

        step("select flight");
        searchResultPage.selectFlightByAirline(airline);

        step("User scrolls to Economy Card and clicks 'See Details' hyperlink");
        productReviewPage.clickSeeDetailsUnderEconomyCard();

        step("User clicks the close button on penalty info popup");
        productReviewPage.closePopUp();

        step("User clicks 'Select' button on 'Economy' card");
        productReviewPage.clickSelectBtnRegular();

        step("Verify user is directed to Booking Form page");
        bookingFormPage.waitForBookingPageToLoad();
        Assertion.assertThat("Booking Form page is visible", bookingFormPage.isBookingFormVisible(), is(true));

        step("Fill Contact Details");
        String contactFullName = "Automation " + CoreFunctions.generateRandomString(5);
        bookingFormPage.inputContactDetailsNonLogin(salutation, contactFullName, phoneNo, email);

        step("Fill Passenger Details");
        bookingFormPage.fillPassengerDetailsLogin(adultPassengers,childPassengers,infantPassengers,"international", airline);

        step("User Select Seats for Passenger");
        bookingFormPage.selectQuietZoneSeat();

        step("User taps on chevron icon on price section to view Price Details");
        bookingFormPage.openPriceDetailSection();

        step("User is redirected to Price Detail page");
        Assertion.assertThat("Price Detail Page is displayed", bookingFormPage.isPriceDetailPageVisible(), is(true));

        step("Verify Total Price Displayed");
        Assertion.assertThat("Total price is displayed", bookingFormPage.isTotalPriceDisplayed(), is(true));

        step("User closes the price detail page");
        bookingFormPage.closePopUp();

        step("User is redirected back to Booking Form");
        Assertion.assertThat("Booking Form page is visible again", bookingFormPage.isBookingFormVisible(), is(true));

        step("going to paymentPage");
        bookingFormPage.goToPaymentPageFromBookingFormPage();
        bookingFormPage.clickNoThanksOnFlightMayReschedulePopup();

        step("click I don't want it");
        bookingFormPage.clickOkayGotIt();
        bookingFormPage.clickGotItInBaggagePopup();

        step("Payment page should be displayed");
        Assertion.assertThat("PaymentPageLoaded", paymentPageWeb.isPaymentPagepresent(), is(true));
        String orderID = paymentPageWeb.getOrderId();
        orderID = String.valueOf(getOrderId(orderID));
        Assertion.assertThat("OrderIDVisible "+ orderID, !orderID.isEmpty(), is(true));
    }

    @Author(names = {"Ram Pratap Singh", "Abhishek Sharma"})
    @Module(name = "Booking")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void FLG_testBookingOneWayDomesticSinglePaxBuisnessCritical() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("FLG_testBookingOneWayDomesticSinglePaxBuisnessCritical");
        BaseTest.paramsThreadLocal.set(param);

        Session<WebDriver> session = sessionThreadLocal.get();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPage = FlightLandingPageWeb.getInstance(session);
        FlightSearchResultPageWeb<?> flightSearchResultPageWeb = FlightSearchResultPageWeb.getInstance(session);
        FlightProductReviewPageWeb<?> flightProductReviewPageWeb = FlightProductReviewPageWeb.getInstance(session);
        FlightBookingFormPageWeb<?> flightBookingFormPageWeb = FlightBookingFormPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPageWeb = PaymentPageWeb.getInstance(session);
        FlightApi<?> flightApi = new FlightApi<>(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);


        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);
        String tripType = param.get("tripType");
        String username = param.get("username");
        String otp = param.get("otp");
        String password = param.get("password");
        String airlineName = param.get("airlineName");
        String currency = "IDR";
        int adultPassengers = Integer.parseInt(param.get("adultPassengers"));
        int childPassengers = Integer.parseInt(param.get("childPassengers"));
        int infantPassengers = Integer.parseInt(param.get("infantPassengers"));
        String optionalLocation = param.get("optionalLocation");
        String optionalAirline = param.get("optionalAirline");
        String paymentGroup = param.get("paymentGroup");
        String paymentMethod = param.get("paymentMethod");

        step("Find dynamic data");
        FlightDynamicData flightDynamicData = flightApi.findDate(
                username,password,
                originAirport,destinationAirport,daysToDepartureDate,
                adultPassengers,childPassengers,infantPassengers,
                "oneway","","",optionalLocation,optionalAirline,currency
        );
        originAirport = flightDynamicData.originAirport;
        destinationAirport = flightDynamicData.destinationAirport;
        departureDate = CoreFunctions.convertToLongDate(flightDynamicData.departureDate);
        airlineName = flightDynamicData.airlineDepart;

        step("User open home page");
        homePage.goToHomePage();

        step("Logging in using {" + username + "}");
        loginPage.loginWithSpecificAccountViaCookie(username, password);

        homePage.selectCurrency(currency);

        step("User open Flight Landing Page");
        homePage.openVerticalPageViaURL("Flights");

        step("Toggle trip type");
        flightLandingPage.toggleTripType(tripType);

        step("Select origin airport");
        flightLandingPage.selectOrigin(originAirport);

        step("Select destination airport");
        flightLandingPage.selectDestination(destinationAirport);

        step("Select date");
        flightLandingPage.selectDate(departureDate);

        step("Add passenger");
        flightLandingPage.addPassenger(adultPassengers, childPassengers, infantPassengers);

        step("search flight");
        flightLandingPage.searchFlight();

        step("filter airline {" + airlineName + " }");
        flightSearchResultPageWeb.filterAirline(airlineName);

        step("select flight for departure");
        flightSearchResultPageWeb.selectFlight();

        step("select the SELECT Button");
        flightProductReviewPageWeb.clickSelectBtnRegular();

        step("close Reschedule popUp");
        flightBookingFormPageWeb.clickNoThanksOnFlightMayReschedulePopup();

        step("enter the passenger Name");
        flightBookingFormPageWeb.fillPassengerDetails();

        step("Open Price Detail section and verify breakdown including add-ons price");
        flightBookingFormPageWeb.openPriceDetailSection();
        Assertion.assertThat("Price Detail Page is displayed", flightBookingFormPageWeb.isPriceDetailPageVisible(), is(true));
        Assertion.assertThat("Total price is displayed", flightBookingFormPageWeb.isTotalPriceDisplayed(), is(true));

        step("Close Price Detail page");
        flightBookingFormPageWeb.closePopUp();

        step("going to paymentPage");
        flightBookingFormPageWeb.goToPaymentPageFromBookingFormPage();
        flightBookingFormPageWeb.clickContinueToPaymentIfVisible();
        flightBookingFormPageWeb.clickNoThanksOnFlightMayReschedulePopup();
        flightBookingFormPageWeb.clickContinueToPaymentIfVisible();

        step("click I don't want it");
        flightBookingFormPageWeb.clickOkayGotIt();

        step("Payment page should be displayed");
        Assertion.assertThat("PaymentPageLoaded", paymentPageWeb.isPaymentPagepresent(), is(true));
        String orderID = paymentPageWeb.getOrderId();
        orderID = String.valueOf(getOrderId(orderID));
        Assertion.assertThat("OrderIDVisible " + orderID, !orderID.isEmpty(), is(true));

        step("Validate all suppliers are mock issued by airline, skipped if real issued supplier exist");
        Boolean result = flightApi.areAllSuppliersMockIssuedByAirline(flightDynamicData.airlineCodeDepart, flightDynamicData.originAirport, flightDynamicData.destinationAirport);

        if(Configs.ENVIRONMENT != Environment.PROD && result) {
            step("Open all payment method");
            paymentPage.clickOnSelectPaymentMethod();

            step("Select the payment Method");
            paymentPage.goToPaymentMethodsGroup(paymentGroup);
            paymentPage.selectPaymentMethod(paymentMethod);

            step("Submit the payment method");
            paymentPage.clickOnPaymentButton();
            Assertion.assertThat("Payment successful page is visible", paymentPage.isPaymentSuccessfulVisible(), is(true));

            step("Open order list and go to order detail page");
            paymentPageWeb.clickSeeOrderListButton();
            OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
            OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
            String orderStatus = orderListPage.getOrderStatus(orderID);
            Assertion.assertThat("Order status should be ETICKET_ISSUED or PROCESSING_ETICKET",
                    orderStatus.equalsIgnoreCase("ETICKET_ISSUED") || orderStatus.equalsIgnoreCase("PROCESSING_ETICKET"), is(true));
            orderListPage.openActiveTicketViaURL(orderID);
            orderDetailPage.closeEticketPopUp();
        }
    }

    @Author(names = {"Ram Pratap Singh"})
    @Module(name = "Flight")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void FLG_testBookingOneWayInternationalSinglePaxNonIdrBuisnessCritical() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("FLG_testBookingOneWayInternationalSinglePaxNonIdrBuisnessCritical");
        BaseTest.paramsThreadLocal.set(param);

        Session<WebDriver> session = sessionThreadLocal.get();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPage = FlightLandingPageWeb.getInstance(session);
        FlightSearchResultPageWeb<?> flightSearchResultPageWeb = FlightSearchResultPageWeb.getInstance(session);
        FlightProductReviewPageWeb<?> flightProductReviewPageWeb = FlightProductReviewPageWeb.getInstance(session);
        FlightBookingFormPageWeb<?> flightBookingFormPageWeb = FlightBookingFormPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPageWeb = PaymentPageWeb.getInstance(session);
        FlightApi<?> flightApi = new FlightApi<>(session);

        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);
        String tripType = param.get("tripType");
        String username = param.get("username");
        String otp = param.get("otp");
        String password = param.get("password");
        String airlineName = param.get("airlineName");
        String currency = "THB";
        int adultPassengers = Integer.parseInt(param.get("adultPassengers"));
        int childPassengers = Integer.parseInt(param.get("childPassengers"));
        int infantPassengers = Integer.parseInt(param.get("infantPassengers"));
        String optionalLocation = param.get("optionalLocation");
        String optionalAirline = param.get("optionalAirline");
        String cardNumber = param.get("cardNumber");
        String cardMonth = param.get("cardMonth");
        String cvv = param.get("cvv");
        String cardHolderName = param.get("cardHolderName");

        step("Find dynamic data");
        FlightDynamicData flightDynamicData = flightApi.findDate(
                username,password,
                originAirport,destinationAirport,daysToDepartureDate,
                adultPassengers,childPassengers,infantPassengers,
                "oneway",airlineName,"",optionalLocation,optionalAirline,currency
        );
        originAirport = flightDynamicData.originAirport;
        destinationAirport = flightDynamicData.destinationAirport;
        departureDate = CoreFunctions.convertToLongDate(flightDynamicData.departureDate);
        airlineName = flightDynamicData.airlineDepart;

        step("User open home page");
        homePage.goToHomePage();

        step("Logging in using {" + username + "}");
        loginPage.loginWithSpecificAccountViaCookie(username, password);

        step("User select { " + currency + " } currency");
        homePage.selectCurrency(currency);

        step("User open Flight Landing Page");
        homePage.openVerticalPageViaURL("Flights");

        step("Toggle trip type");
        flightLandingPage.toggleTripType(tripType);

        step("Select origin airport");
        flightLandingPage.selectOrigin(originAirport);

        step("Select destination airport");
        flightLandingPage.selectDestination(destinationAirport);

        step("Select date");
        flightLandingPage.selectDate(departureDate);

        step("Add passenger");
        flightLandingPage.addPassenger(adultPassengers, childPassengers, infantPassengers);

        step("search flight");
        flightLandingPage.searchFlight();
        flightLandingPage.handleGotItPopup();

        step("filter airline {" + airlineName + " }");
        flightSearchResultPageWeb.filterAirline(airlineName);

        step("select flight for departure");
        flightSearchResultPageWeb.selectFlight();

        step("select the SELECT Button");
        flightProductReviewPageWeb.clickSelectBtnRegular();

        step("close Reschedule popUp");
        flightBookingFormPageWeb.clickNoThanksOnFlightMayReschedulePopup();

        step("enter the passenger Name");
        flightBookingFormPageWeb.fillPassengerDetails();

        step("Open Price Detail section and verify breakdown including add-ons price");
        flightBookingFormPageWeb.openPriceDetailSection();
        Assertion.assertThat("Price Detail Page is displayed", flightBookingFormPageWeb.isPriceDetailPageVisible(), is(true));
        Assertion.assertThat("Total price is displayed", flightBookingFormPageWeb.isTotalPriceDisplayed(), is(true));

        step("Close Price Detail page");
        flightBookingFormPageWeb.closePopUp();

        step("going to paymentPage");
        flightBookingFormPageWeb.goToPaymentPageFromBookingFormPage();
        flightBookingFormPageWeb.clickContinueToPaymentIfVisible();
        flightBookingFormPageWeb.clickNoThanksOnFlightMayReschedulePopup();
        flightBookingFormPageWeb.clickContinueToPaymentIfVisible();

        step("click I don't want it");
        flightBookingFormPageWeb.clickOkayGotIt();

        step("Payment page should be displayed");
        Assertion.assertThat("PaymentPageLoaded", paymentPageWeb.isPaymentPagepresent(), is(true));
        String orderID = paymentPageWeb.getOrderId();
        orderID = String.valueOf(getOrderId(orderID));
        Assertion.assertThat("OrderID Visible " + orderID, !orderID.isEmpty(), is(true));

        step("Input credit card details");
        paymentPageWeb.inputCardNumber(cardNumber);
        paymentPageWeb.inputCardMonth(cardMonth);
        paymentPageWeb.inputCardCvv(cvv);
        paymentPageWeb.inputCardHolderName(cardHolderName);

        step("Validate all suppliers are mock issued by airline, skipped if real issued supplier exist");
        boolean isDepartSuppliersMocked = flightApi.areAllSuppliersMockIssuedByAirline(flightDynamicData.airlineCodeDepart, flightDynamicData.originAirport, flightDynamicData.destinationAirport);

        step("only submit payment if all suppliers are mocked { isDepartSuppliersMocked: " + isDepartSuppliersMocked +" }");
        if (Configs.ENVIRONMENT != Environment.PROD && isDepartSuppliersMocked) {

            step("Submit payment");
            paymentPageWeb.clickOnPaymentButton();
            Assertion.assertThat("Payment successful page is visible", paymentPageWeb.isPaymentSuccessfulVisible(), is(true));

            step("Open order list and go to order detail page");
            paymentPageWeb.clickSeeOrderListButton();
            OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
            OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
            String orderStatus = orderListPage.getOrderStatus(orderID);
            Assertion.assertThat("Order status should be ETICKET_ISSUED or PROCESSING_ETICKET",
                    orderStatus.equalsIgnoreCase("ETICKET_ISSUED") || orderStatus.equalsIgnoreCase("PROCESSING_ETICKET"), is(true));
            orderListPage.openActiveTicketViaURL(orderID);
            orderDetailPage.closeEticketPopUp();
        }

    }

    @Author(names = {"Ram Pratap Singh"})
    @Module(name = "Flight")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void FLG_testBookingOneWayMultiPaxInternationalWithAddonsBuisnessCritical() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("FLG_testBookingOneWayMultiPaxInternationalWithAddonsBuisnessCritical");
        BaseTest.paramsThreadLocal.set(param);

        Session<WebDriver> session = sessionThreadLocal.get();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPage = FlightLandingPageWeb.getInstance(session);
        FlightSearchResultPageWeb<?> flightSearchResultPageWeb = FlightSearchResultPageWeb.getInstance(session);
        FlightProductReviewPageWeb<?> flightProductReviewPageWeb = FlightProductReviewPageWeb.getInstance(session);
        FlightBookingFormPageWeb<?> flightBookingFormPageWeb = FlightBookingFormPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPageWeb = PaymentPageWeb.getInstance(session);
        FlightApi<?> flightApi = new FlightApi<>(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);


        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);
        String tripType = param.get("tripType");
        String username = param.get("username");
        String otp = param.get("otp");
        String password = param.get("password");
        String airlineName = param.get("airlineName");
        String currency = "IDR";
        int adultPassengers = Integer.parseInt(param.get("adultPassengers"));
        int childPassengers = Integer.parseInt(param.get("childPassengers"));
        int infantPassengers = Integer.parseInt(param.get("infantPassengers"));
        String optionalLocation = param.get("optionalLocation");
        String optionalAirline = param.get("optionalAirline");
        String paymentGroup = param.get("paymentGroup");
        String paymentMethod = param.get("paymentMethod");

        step("Find dynamic data");
        FlightDynamicData flightDynamicData = flightApi.findDate(
                username,password,
                originAirport,destinationAirport,daysToDepartureDate,
                adultPassengers,childPassengers,infantPassengers,
                "oneway",airlineName,"",optionalLocation,optionalAirline,currency
        );
        originAirport = flightDynamicData.originAirport;
        destinationAirport = flightDynamicData.destinationAirport;
        departureDate = CoreFunctions.convertToLongDate(flightDynamicData.departureDate);
        airlineName = flightDynamicData.airlineDepart;

        step("Logging in using {" + username + "}");
        loginPage.loginWithSpecificAccountViaCookie(username, password);

        step("User select { " + currency + " } currency");
        homePage.selectCurrency(currency);

        step("User open Flight Landing Page");
        homePage.openVerticalPageViaURL("Flights");

        step("Toggle trip type");
        flightLandingPage.toggleTripType(tripType);

        step("Select origin airport");
        flightLandingPage.selectOrigin(originAirport);

        step("Select destination airport");
        flightLandingPage.selectDestination(destinationAirport);

        step("Select date");
        flightLandingPage.selectDate(departureDate);

        step("Add passengers {Adult: " + adultPassengers + ", Child: " + childPassengers + ", Infant: " + infantPassengers + "}");
        flightLandingPage.addPassenger(adultPassengers, childPassengers, infantPassengers);

        step("Search flight");
        flightLandingPage.searchFlight();

        step("Filter airline {" + airlineName + "}");
        flightSearchResultPageWeb.filterAirline(airlineName);

        step("Select flight for departure");
        flightSearchResultPageWeb.selectFlight();

        step("Select the SELECT button on Product Review Page");
        flightProductReviewPageWeb.clickSelectBtnRegular();

        step("Close Reschedule popUp");
        flightBookingFormPageWeb.clickNoThanksOnFlightMayReschedulePopup();

        step("Enter passenger details");
        flightBookingFormPageWeb.fillPassengerDetailsLogin(adultPassengers, childPassengers, infantPassengers, "international", airlineName);

        step("Select baggage addons");
        flightBookingFormPageWeb.selectAddonsBaggageMultiPAX(adultPassengers, childPassengers);

        step("Open Price Detail section and verify breakdown");
        flightBookingFormPageWeb.openPriceDetailSection();
        Assertion.assertThat("Price Detail Page is displayed", flightBookingFormPageWeb.isPriceDetailPageVisible(), is(true));
        Assertion.assertThat("Total price is displayed", flightBookingFormPageWeb.isTotalPriceDisplayed(), is(true));

        step("Close Price Detail page");
        flightBookingFormPageWeb.closePopUp();

        step("Going to payment page");
        flightBookingFormPageWeb.goToPaymentPageFromAddonsPage();
        flightBookingFormPageWeb.clickContinueToPaymentIfVisible();
        flightBookingFormPageWeb.clickNoThanksOnFlightMayReschedulePopup();
        flightBookingFormPageWeb.clickContinueToPaymentIfVisible();

        step("Click I don't want it");
        flightBookingFormPageWeb.clickOkayGotIt();

        step("Payment page should be displayed");
        Assertion.assertThat("PaymentPageLoaded", paymentPageWeb.isPaymentPagepresent(), is(true));
        String orderID = paymentPageWeb.getOrderId();
        orderID = String.valueOf(getOrderId(orderID));
        Assertion.assertThat("OrderIDVisible " + orderID, !orderID.isEmpty(), is(true));

        step("Validate all suppliers are mock issued by airline, skipped if real issued supplier exist");
        boolean isDepartSuppliersMocked = flightApi.areAllSuppliersMockIssuedByAirline(flightDynamicData.airlineCodeDepart, flightDynamicData.originAirport, flightDynamicData.destinationAirport);

        step("only submit payment if all suppliers are mocked { isDepartSuppliersMocked: " + isDepartSuppliersMocked +" }");
        if (Configs.ENVIRONMENT != Environment.PROD && isDepartSuppliersMocked) {

            step("Open all payment method");
            paymentPage.clickOnSelectPaymentMethod();

            step("Select the payment Method");
            paymentPage.goToPaymentMethodsGroup(paymentGroup);
            paymentPage.selectPaymentMethod(paymentMethod);

            step("Submit the payment method");
            paymentPage.clickOnPaymentButton();
            Assertion.assertThat("Payment successful page is visible", paymentPage.isPaymentSuccessfulVisible(), is(true));

            step("Open order list and go to order detail page");
            paymentPageWeb.clickSeeOrderListButton();
            OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
            OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
            String orderStatus = orderListPage.getOrderStatus(orderID);
            Assertion.assertThat("Order status should be ETICKET_ISSUED or PROCESSING_ETICKET",
                    orderStatus.equalsIgnoreCase("ETICKET_ISSUED") || orderStatus.equalsIgnoreCase("PROCESSING_ETICKET"), is(true));
            orderListPage.openActiveTicketViaURL(orderID);
            orderDetailPage.closeEticketPopUp();
        }

    }

    @Author(names = {"Ram Pratap Singh"})
    @Module(name = "Flight")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void FLG_testBookingRoundTripTransitSinglePaxInternationalBusinessCritical() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("FLG_testBookingRoundTripTransitSinglePaxInternationalBusinessCritical");
        BaseTest.paramsThreadLocal.set(param);

        Session<WebDriver> session = sessionThreadLocal.get();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPage = FlightLandingPageWeb.getInstance(session);
        FlightSearchResultPageWeb<?> flightSearchResultPageWeb = FlightSearchResultPageWeb.getInstance(session);
        FlightProductReviewPageWeb<?> flightProductReviewPageWeb = FlightProductReviewPageWeb.getInstance(session);
        FlightBookingFormPageWeb<?> flightBookingFormPageWeb = FlightBookingFormPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPageWeb = PaymentPageWeb.getInstance(session);
        FlightApi<?> flightApi = new FlightApi<>(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);


        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int duration = Integer.parseInt(param.get("duration"));
        String username = param.get("username");
        String otp = param.get("otp");
        String password = param.get("password");
        String airlineName = param.get("airlineName");
        String currency = "IDR";
        int adultPassengers = Integer.parseInt(param.get("adultPassengers"));
        int childPassengers = Integer.parseInt(param.get("childPassengers"));
        int infantPassengers = Integer.parseInt(param.get("infantPassengers"));
        String optionalLocation = param.get("optionalLocation");
        String optionalAirline = param.get("optionalAirline");
        String paymentGroup = param.get("paymentGroup");
        String paymentMethod = param.get("paymentMethod");

        step("Find dynamic data for round trip");
        FlightDynamicData flightDynamicData = flightApi.findDateRT(
                username,password,
                originAirport,destinationAirport,daysToDepartureDate,duration,
                adultPassengers,childPassengers,infantPassengers,
                "roundtrip","","","Round-trip Deals","",optionalLocation,optionalAirline,currency
        );
        originAirport = flightDynamicData.originAirport;
        destinationAirport = flightDynamicData.destinationAirport;
        String departureDate = CoreFunctions.convertToLongDate(flightDynamicData.departureDate);
        String returnDate = CoreFunctions.convertToLongDate(flightDynamicData.returnDate);
        airlineName = flightDynamicData.airlineDepart;
        String airlineReturn = flightDynamicData.airlineReturn;

        step("User open home page");
        homePage.goToHomePage();

        step("Logging in using {" + username + "}");
        loginPage.loginWithSpecificAccountViaCookie(username, password);

        homePage.selectCurrency(currency);

        step("User open Flight Landing Page");
        homePage.openVerticalPageViaURL("Flights");

        step("Select origin airport");
        flightLandingPage.selectOrigin(originAirport);

        step("Select destination airport");
        flightLandingPage.selectDestination(destinationAirport);

        step("Select departure and return date");
        flightLandingPage.selectDate(departureDate, returnDate);

        step("Add passenger");
        flightLandingPage.addPassenger(adultPassengers, childPassengers, infantPassengers);

        step("Search flight");
        flightLandingPage.searchFlight();

        step("Filter transit flights for departure");
        flightSearchResultPageWeb.selectFlightWithSingleTransit();

        step("Click on Round-trip Deals filter for departure");
        flightSearchResultPageWeb.roundTripDealsFilter();

        step("Filter airline {" + airlineName + " } for departure");
        flightSearchResultPageWeb.filterAirline(airlineName);

        step("Select flight for departure");
        flightSearchResultPageWeb.selectFlight();

        step("Filter transit flights for return");
        flightSearchResultPageWeb.selectFlightWithSingleTransit();

        step("Click on Round-trip Deals filter for return");
        flightSearchResultPageWeb.roundTripDealsFilter();

        step("Filter airline {" + airlineReturn + " } for return trip");
        flightSearchResultPageWeb.filterAirline(airlineReturn);

        step("Select flight for return");
        flightSearchResultPageWeb.selectFlight();

        step("Select the SELECT button on Product Review Page");
        flightProductReviewPageWeb.clickSelectBtnRegular();

        step("Close Reschedule popUp");
        flightBookingFormPageWeb.clickNoThanksOnFlightMayReschedulePopup();

        step("Enter passenger details");
        flightBookingFormPageWeb.fillPassengerDetails();

        step("Open Price Detail section and verify breakdown");
        flightBookingFormPageWeb.openPriceDetailSection();
        Assertion.assertThat("Price Detail Page is displayed", flightBookingFormPageWeb.isPriceDetailPageVisible(), is(true));
        Assertion.assertThat("Total price is displayed", flightBookingFormPageWeb.isTotalPriceDisplayed(), is(true));

        step("Close Price Detail page");
        flightBookingFormPageWeb.closePopUp();

        step("Going to payment page");
        flightBookingFormPageWeb.goToPaymentPageFromBookingFormPage();
        flightBookingFormPageWeb.clickContinueToPaymentIfVisible();
        flightBookingFormPageWeb.clickNoThanksOnFlightMayReschedulePopup();
        flightBookingFormPageWeb.clickContinueToPaymentIfVisible();

        step("Click I don't want it");
        flightBookingFormPageWeb.clickOkayGotIt();
        flightBookingFormPageWeb.clickGotItInBaggagePopup();

        step("Payment page should be displayed");
        Assertion.assertThat("PaymentPageLoaded", paymentPageWeb.isPaymentPagepresent(), is(true));
        String orderID = paymentPageWeb.getOrderId();
        orderID = String.valueOf(getOrderId(orderID));
        Assertion.assertThat("OrderIDVisible " + orderID, !orderID.isEmpty(), is(true));

        step("Validate all suppliers are mock issued by airline, skipped if real issued supplier exist");
        boolean isDepartSuppliersMocked = flightApi.areAllSuppliersMockIssuedByAirline(flightDynamicData.airlineCodeDepart, flightDynamicData.originAirport, flightDynamicData.destinationAirport);
        boolean isReturnSuppliersMocked = flightApi.areAllSuppliersMockIssuedByAirline(flightDynamicData.airlineCodeReturn, flightDynamicData.destinationAirport, flightDynamicData.originAirport);

        if(Configs.ENVIRONMENT != Environment.PROD && isDepartSuppliersMocked && isReturnSuppliersMocked) {
            step("Open all payment method");
            paymentPage.clickOnSelectPaymentMethod();

            step("Select the payment Method");
            paymentPage.goToPaymentMethodsGroup(paymentGroup);
            paymentPage.selectPaymentMethod(paymentMethod);

            step("Submit the payment method");
            paymentPage.clickOnPaymentButton();
            Assertion.assertThat("Payment successful page is visible", paymentPage.isPaymentSuccessfulVisible(), is(true));


            step("Open order list and go to order detail page");
            OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
            OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
            homePage.goToYourOrderUrl();
            String orderStatus = orderListPage.getOrderStatus(orderID);
            Assertion.assertThat("Order list (Your Orders) is displayed", orderListPage.isOrderListPageDisplayed(), is(true));


            Assertion.assertThat("Order status should be ETICKET_ISSUED or PROCESSING_ETICKET",
                    orderStatus.equalsIgnoreCase("ETICKET_ISSUED") || orderStatus.equalsIgnoreCase("PROCESSING_ETICKET"), is(true));
            orderListPage.openActiveTicketViaURL(orderID);
            orderDetailPage.closeEticketPopUp();
        }

    }


@Author(names = {"Harshita","Priyanka"})
@Module(name = "Flight")
@Tribe(name = "Flight")
@Vertical(name = "Flight")
@Test
public void FLG_testBookingOneWayTransitConnectingFlightProtectionVI() throws FailedLoginException, InterruptedException {
    Map<String, String> param = getParam("FLG_testBookingOneWayTransitConnectingFlightProtectionVI");
    BaseTest.paramsThreadLocal.set(param);

    Session<WebDriver> session = BaseTest.sessionThreadLocal.get();
    HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
    FlightLandingPageWeb<?> flightLandingPage = FlightLandingPageWeb.getInstance(session);
    FlightSearchResultPageWeb<?> searchResultPage = FlightSearchResultPageWeb.getInstance(session);
    FlightProductReviewPageWeb<?> productReviewPage = FlightProductReviewPageWeb.getInstance(session);
    FlightBookingFormPageWeb<?> bookingFormPage =  FlightBookingFormPageWeb.getInstance(session);
    PaymentPageWeb<?> paymentPageWeb=PaymentPageWeb.getInstance(session);
    FlightApi<WebDriver> flightApi = new FlightApi<>(session);
    LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

    String location1 = param.get("location1");
    String location2 = param.get("location2");
    String location3 = param.get("location3");

    int adultPassengers = Integer.parseInt(param.get("adultCount"));
    int childPassengers = Integer.parseInt(param.get("childCount"));
    int infantPassengers = Integer.parseInt(param.get("infantCount"));
    int daysToDepartureDate = ThreadLocalRandom.current().nextInt(1, 21) + 90;
    String username = param.get("username");
    String password = param.get("password");

    String currentDate = CoreFunctions.getCurrentLongDate();
    String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);
    String currency = param.get("currency");

    step("Set the scheduler config to true");
    Assertion.assertThat("Config is set to true", flightApi.toggleScheduleIngestion(true), is(true));

    step("User open Home Page");
    homePage.goToHomePage();

    step("User login");
    loginPage.loginWithSpecificAccountViaCookie(username, password);

    step("User select { " + currency + " } currency");
    homePage.selectCurrency(currency);

    step("User search route CGK - SUB to cache schedule");
    homePage.openVerticalPageViaURL("Flights");

    step("Toggle trip type to One Way");
    flightLandingPage.toggleTripType("oneway");

    step("Select date");
    flightLandingPage.selectDate(departureDate);

    step("Add passenger");
    flightLandingPage.addPassenger(adultPassengers, childPassengers, infantPassengers);

    step("select location1 { " + location1 +" } and location2 { " + location2 +" }");
    flightLandingPage.selectOrigin(location1);
    flightLandingPage.selectDestination(location2);

    step("Search flight origin: " + location1 + " destination: " + location2);
    flightLandingPage.searchFlight();
    Assertion.assertThat("Search Result Page CGK-SUB is loaded", searchResultPage.isSearchResultPagePresent(), is(true));
    searchResultPage.isSRPLoaded();

    step("select location2 { " + location2 +" } and location3 { " + location3 +" }");
    searchResultPage.selectOriginOnSearchResults(location2);
    searchResultPage.selectDestinationOnSearchResults(location3);

    step("Search flight location2 { " + location2 +" } and location3 { " + location3 +" }");
    searchResultPage.searchFlightSRP();
    Assertion.assertThat("Search Result Page SUB-DPS is loaded", searchResultPage.isSearchResultPagePresent(), is(true));
    searchResultPage.isSRPLoaded();

    step("select location1 { " + location1 +" } and location3 { " + location3 +" }");
    searchResultPage.selectOriginOnSearchResults(location1);
    searchResultPage.selectDestinationOnSearchResults(location3);

    step("User click search button");
    searchResultPage.searchFlightSRP();
    Assertion.assertThat("Search Result Page CGK-DPS is loaded", searchResultPage.isSearchResultPagePresent(), is(true));
    searchResultPage.isSRPLoaded();

    // Re-selecting the same route to ensure the schedule is ingested and available for booking
    step("select location1 { " + location1 +" } and location3 { " + location3 +" }");
    searchResultPage.selectOriginOnSearchResults(location1);
    searchResultPage.selectDestinationOnSearchResults(location3);

    step("User click search button");
    searchResultPage.searchFlightSRP();
    Assertion.assertThat("Search Result Page CGK-DPS is loaded", searchResultPage.isSearchResultPagePresent(), is(true));
    searchResultPage.isSRPLoaded();

    step("Filter airline by self transfer");
    searchResultPage.selectSelfTransferFlightFilter();

    step("Select VI transit flight (may have different transit supplier e.g. Lion Air + Citilink)");
    searchResultPage.selectFlight();

    step("User is redirected to Product Review Page");
    Assertion.assertThat("Product Review page is visible", productReviewPage.isPresent(), is(true));

    step("User clicks on flight route collapse icon to expand transit route details");
    productReviewPage.expandFlightRouteDetails();

    step("Verify transit route details show 'Check-in again' information");
    Assertion.assertThat("Transit route details should show 'Connecting Flight Protection Info' information for VI route", productReviewPage.isCheckInAgainInfoVisible(), is(true));

    step("Close VI Connecting Flight details pop up");
    productReviewPage.closePopUp();

    step("User expands first flight route and reads airline code for mock validation");
    String airlineCode1 = productReviewPage.expandFirstFlightRouteAndGetAirlineCode();

    step("User expands second flight route and reads airline code for mock validation");
    String airlineCode2 = productReviewPage.expandSecondFlightRouteAndGetAirlineCode();

    step("User clicks 'Select' button on 'Economy' card");
    productReviewPage.clickSelectBtnRegular();

    step("Verify user is directed to Booking Form page");
    bookingFormPage.waitForBookingPageToLoad();
    Assertion.assertThat("Booking Form page is visible", bookingFormPage.isBookingFormVisible(), is(true));

    step("Verify Flight details visible");
    Assertion.assertThat("Flight details are displayed", bookingFormPage.isFlightDetailsVisible(), is(true));

    step("Go to flight detail");
    bookingFormPage.goOnFlightDetail();

    step("User clicks on flight route collapse icon to expand transit route details");
    bookingFormPage.expandFlightRouteDetails();

    step("Verify transit route details show 'Check-in again' information");
    Assertion.assertThat("Transit route details should show 'Connecting Flight Protection Info' information for VI route", bookingFormPage.isCheckInAgainInfoVisible(), is(true));

    step("Close VI Connecting Flight details pop up");
    bookingFormPage.closePopUp();

    step("User fill passenger details");
    bookingFormPage.fillPassengerDetailsLogin(adultPassengers, childPassengers, infantPassengers, "domestic", "");

    step("Open Price Details section");
    bookingFormPage.openPriceDetailSection();

    step("Close Price Detail section");
    bookingFormPage.closePopUp();

    step("Proceed to payment page");
    bookingFormPage.goToPaymentPageFromBookingFormPage();
    bookingFormPage.clickItsCorrectOnReviewPassengerDataPopup();
    bookingFormPage.clickNoThanksOnFlightMayReschedulePopup();
    bookingFormPage.clickOkayGotIt();

    step("Payment page should be displayed with final price");
    Assertion.assertThat("PaymentPageLoaded", paymentPageWeb.isPaymentPagepresent(), is(true));
    String orderID = paymentPageWeb.getOrderId();
    orderID = String.valueOf(getOrderId(orderID));
    Assertion.assertThat("OrderIDVisible " + orderID, !orderID.isEmpty(), is(true));

    step("Mock validation: all suppliers mock-issued for first segment airline " + airlineCode1
            + " on booked route origin: " + location1 + " destination: " + location3);
    boolean res1 = flightApi.areAllSuppliersMockIssuedByAirline(airlineCode1, location1, location3);

    step("Mock validation: all suppliers mock-issued for second segment airline " + airlineCode2
            + " on booked route origin: " + location1 + " destination: " + location3);
    boolean res2 = flightApi.areAllSuppliersMockIssuedByAirline(airlineCode2, location1, location3);

    if (Configs.ENVIRONMENT != Environment.PROD && (res1 && res2))
    {
        step("User selects Sakuku payment method");
        paymentPageWeb.selectSakukuPaymentMethod();

        step("Submit the payment method");
        paymentPageWeb.clickOnPaymentButton();
        Assertion.assertThat("Payment successful page is visible", paymentPageWeb.isPaymentSuccessfulVisible(), is(true));

        step("Open order list and go to order detail page");
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        homePage.goToYourOrderUrl();
        String orderStatus = orderListPage.getOrderStatus(orderID);
        Assertion.assertThat("Order list (Your Orders) is displayed", orderListPage.isOrderListPageDisplayed(), is(true));


        Assertion.assertThat("Order status should be ETICKET_ISSUED or PROCESSING_ETICKET",
                orderStatus.equalsIgnoreCase("ETICKET_ISSUED") || orderStatus.equalsIgnoreCase("PROCESSING_ETICKET"), is(true));
        orderListPage.openActiveTicketViaURL(orderID);
        orderDetailPage.closeEticketPopUp();
    }
}

    @Author(names = "Abhishek Sharma, Ram Pratap Singh")
    @Module(name = "Flight")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void FLG_testBookRoundTripDifferentAirlineSinglePassengerDomesticRoute() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("FLG_testBookRoundTripDifferentAirlineSinglePassengerDomesticRoute");
        BaseTest.paramsThreadLocal.set(param);

        FlightApi<?> flightApi = new FlightApi<>(session);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPage = FlightLandingPageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        FlightSearchResultPageWeb<?> flightSearchResultPageWeb = FlightSearchResultPageWeb.getInstance(session);
        FlightProductReviewPageWeb<?> flightProductReviewPageWeb = FlightProductReviewPageWeb.getInstance(session);
        FlightBookingFormPageWeb<?> flightBookingFormPageWeb = FlightBookingFormPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPageWeb = PaymentPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);

        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String origin = param.get("originAirport");
        String destination = param.get("destinationAirport");
        String currency = param.get("currency");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int duration = Integer.parseInt(param.get("duration"));
        String username = param.get("username");
        String password = param.get("password");
        int adult = Integer.parseInt(param.get("adult"));
        int children = Integer.parseInt(param.get("children"));
        int infant = Integer.parseInt(param.get("infant"));
        String tripType = param.get("tripType");
        String paymentGroup = param.get("paymentGroup");
        String paymentMethod = param.get("paymentMethod");

        step("Find dynamic data");
        FlightDynamicData flightDynamicData = flightApi.findDateRTWithMockIssuedAirline(
                username, password,
                origin, destination, daysToDepartureDate, duration,
                adult, children, infant,
                tripType, "", "", "100% refund", "100% refund", "", "", currency
        );
        origin = flightDynamicData.originAirport;
        destination = flightDynamicData.destinationAirport;
        String departureDate = CoreFunctions.convertToLongDate(flightDynamicData.departureDate);
        String returnDate = CoreFunctions.convertToLongDate(flightDynamicData.returnDate);
        String airline = flightDynamicData.airlineDepart;
        String airlineReturn = flightDynamicData.airlineReturn;

        step("User open home page");
        homePage.goToHomePage();

        step("User Login ");
        loginPage.loginWithSpecificAccountViaCookie(username, password);

        step("User select { " + currency + " } currency");
        homePage.selectCurrency(currency);

        step("User open Flight Landing Page");
        homePage.openVerticalPageViaURL("Flights");
        Assertion.assertThat("Flight Landing Page is Opened", flightLandingPage.isOnFlightPage(), is(true));

        step("User select origin");
        flightLandingPage.selectOrigin(origin);

        step("Select destination airport");
        flightLandingPage.selectDestination(destination);

        step("Select date");
        flightLandingPage.selectDate(departureDate, returnDate);

        step("Toggle trip type");
        flightLandingPage.toggleTripType(tripType);

        step("Add Number of adults, children and infant passengers");
        flightLandingPage.addPassenger(adult, children, infant);

        step("search flight");
        flightLandingPage.searchFlight();

        step("Wait for Search Result page to be fully loaded.");
        flightSearchResultPageWeb.isSRPLoaded();

        step("User Selects Flight departure airline " + airline);
        flightSearchResultPageWeb.selectFlightByAirline(airline);

        step("User Selects Flight return airline " + airlineReturn);
        flightSearchResultPageWeb.selectFlightByAirline(airlineReturn);

        step("User checks schedule");
        Assertion.assertThat("Check Departure Schedule visible", flightProductReviewPageWeb.isDepartureScheduleVisible(), is(true));
        Assertion.assertThat("Check Return Schedule visible", flightProductReviewPageWeb.isReturnScheduleVisible(), is(true));

        step("User goes to product review page");
        flightProductReviewPageWeb.clickSelectBtnRegular();

        step("Continue to booking form");
        flightProductReviewPageWeb.continueToBookingForm();
        flightBookingFormPageWeb.clickNoThanksOnFlightMayReschedulePopup();

        int initialAmount = Integer.parseInt(flightBookingFormPageWeb.getPriceText().replaceAll("[^0-9]", ""));

        step("Click toggle to fill passengers details and save ");
        flightBookingFormPageWeb.fillPassengerDetails();

        step("Verify Flight details visible");
        flightBookingFormPageWeb.isFlightDetailsVisible();

        step("Go to flight detail");
        flightBookingFormPageWeb.goOnFlightDetail();

        step("See Refund details");
        flightBookingFormPageWeb.clickSeeDetailRefundAndReschedule();

        step("Verify Refund visible or not");
        boolean isRefundRescheduleVisible = flightBookingFormPageWeb.isRefundRescheduleVisible();
        Assertion.assertThat("Verify Refund visible or not", isRefundRescheduleVisible, is(true));

        step("Close refund detail popup");
        flightBookingFormPageWeb.closeSecondPopup();

        step("Close flight detail popup");
        flightBookingFormPageWeb.closePopUp();

        step("Click More Info in Extra protection");
        flightBookingFormPageWeb.clickMoreInfoFullProtection();

        step("Verify Terms and conditions link");
        flightBookingFormPageWeb.clickTermsAndConditionsLink();

        step("close more info popup");
        flightBookingFormPageWeb.closePopUp();

        step("Add insurance");
        InsuranceGeneralData insuranceGeneralData = flightBookingFormPageWeb.selectInsurance(currency);
        int insurancePrice = Integer.parseInt(insuranceGeneralData.insurancePrice.replaceAll("[^0-9]", ""));
        int finalPrice = Integer.parseInt(flightBookingFormPageWeb.getPriceText().replaceAll("[^0-9]", ""));
        Assertion.assertThat("Initial + insurance price = final price", initialAmount + insurancePrice, is(finalPrice));

        step("Go to payment page");
        flightBookingFormPageWeb.goToPaymentPageFromBookingFormPage();
        flightBookingFormPageWeb.clickNoThanksOnFlightMayReschedulePopup();
        flightBookingFormPageWeb.clickOkayGotIt();

        step("Payment page should be displayed");
        Assertion.assertThat("PaymentPageLoaded", paymentPageWeb.isPaymentPagepresent(), is(true));
        String orderID = String.valueOf(paymentPageWeb.getOrderId());
        Assertion.assertThat("OrderIDVisible " + orderID, !orderID.isEmpty(), is(true));

        step("Validate all suppliers are mock issued by airline for departure and return, skipped if real issued supplier exist");
        boolean isDepartSuppliersMocked = flightApi.areAllSuppliersMockIssuedByAirline(flightDynamicData.airlineCodeDepart, flightDynamicData.originAirport, flightDynamicData.destinationAirport);
        boolean isReturnSuppliersMocked = flightApi.areAllSuppliersMockIssuedByAirline(flightDynamicData.airlineCodeReturn, flightDynamicData.destinationAirport, flightDynamicData.originAirport);

        step("Only Submit payment if all suppliers are mocked { isDepartSuppliersMocked: " + isDepartSuppliersMocked + ", isReturnSuppliersMocked: " + isReturnSuppliersMocked + " }");
        if (Configs.ENVIRONMENT != Environment.PROD && isDepartSuppliersMocked && isReturnSuppliersMocked) {
            step("User select payment method ");
            paymentPageWeb.clickOnSelectPaymentMethod();
            paymentPageWeb.goToPaymentMethodsGroup(paymentGroup);
            paymentPageWeb.selectPaymentMethod(paymentMethod);

            step("Click on Pay button");
            paymentPageWeb.clickOnPaymentButton();

            step("Verify the payment successful page is visible or not");
            Assertion.assertThat("Verify the payment successful page is visible", paymentPageWeb.isPaymentSuccessfulVisible(), is(true));

            step("User try to open see order list");
            paymentPageWeb.clickSeeOrderListButton();

            step("Find the issued order");
            orderListPage.openActiveTicketViaURL(orderID);
            orderDetailPage.skipCoachmark();

            step("Open order list and go to order detail page");
            homePage.goToYourOrderUrl();
            String orderStatus = orderListPage.getOrderStatus(orderID);
            Assertion.assertThat("Order list (Your Orders) is displayed", orderListPage.isOrderListPageDisplayed(), is(true));


            Assertion.assertThat("Order status should be ETICKET_ISSUED or PROCESSING_ETICKET",
                    orderStatus.equalsIgnoreCase("ETICKET_ISSUED") || orderStatus.equalsIgnoreCase("PROCESSING_ETICKET"), is(true));
            orderListPage.openActiveTicketViaURL(orderID);
            orderDetailPage.closeEticketPopUp();
        }
    }

    @Author(names = "Manan Kumar")
    @Module(name = "Flight")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void FLG_testUserCanBook100PRefundAndInsuranceBookingFormOptions()  throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("FLG_testUserCanBook100PRefundAndInsuranceBookingFormOptions");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPage = FlightLandingPageWeb.getInstance(session);
        FlightSearchResultPageWeb<?> searchResultPage = FlightSearchResultPageWeb.getInstance(session);
        FlightProductReviewPageWeb<?> productReviewPage = FlightProductReviewPageWeb.getInstance(session);
        FlightBookingFormPageWeb<?> bookingFormPage =  FlightBookingFormPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPageWeb=PaymentPageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        FlightProductReviewPageWeb<?> flightProductReviewPageWeb = FlightProductReviewPageWeb.getInstance(session);
        FlightApi<?> flightApi = new FlightApi<>(session);
        FlightSearchResultPageWeb<?> flightSearchResultPageWeb = FlightSearchResultPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);

        String origin = param.get("origin");
        String destination = param.get("destination");
        int adultPassengers = Integer.parseInt(param.get("adultCount"));
        int childPassengers = Integer.parseInt(param.get("childCount"));
        int infantPassengers = Integer.parseInt(param.get("infantCount"));
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate,daysToDepartureDate);
        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
        int loginAttemptCount = Integer.parseInt(param.get("loginAttemptCount"));
        String airlineName = param.get("airlineName");
        String currency = param.get("currency");

        String paymentGroup = param.get("paymentGroup");
        String paymentMethod = param.get("paymentMethod");

        step("Find dynamic data");
        FlightDynamicData flightDynamicData = flightApi.findDate(
                username,password,
                origin,destination,daysToDepartureDate,
                adultPassengers,childPassengers,infantPassengers,
                "oneway",airlineName,"","","",currency
        );
        origin = flightDynamicData.originAirport;
        destination = flightDynamicData.destinationAirport;
        departureDate = CoreFunctions.convertToLongDate(flightDynamicData.departureDate);
        airlineName = flightDynamicData.airlineDepart;

        step("User open Home Page");
        homePage.goToHomePage();

        step("Go to account Page");
        loginPage.goToAccountPage();

        step("User login");
        loginPage.loginWithSpecificAccountViaCookie(username, password);

        step("User select { " + currency + " } currency");
        homePage.selectCurrency(currency);

        step("User open Flight Landing Page");
        homePage.openVerticalPageViaURL("Flights");
        Assertion.assertThat("Flight Landing Page is Opened", flightLandingPage.isOnFlightPage(), is(true));

        step("User select origin");
        flightLandingPage.selectOrigin(origin);

        step("User select destination");
        flightLandingPage.selectDestination(destination);

        step("Toggle trip type");
        flightLandingPage.toggleTripType("oneway");

        step("Select date");
        flightLandingPage.selectDate(departureDate);

        step("add passenger");
        flightLandingPage.addPassenger(adultPassengers,childPassengers,infantPassengers);

        step("User click search button");
        flightLandingPage.searchFlight();
        Assertion.assertThat("Search Result Page is loaded", searchResultPage.isSearchResultPagePresent(), is(true));

        step("Wait for Search Result page to be fully loaded.");
        searchResultPage.isSRPLoaded();

        step("Click Tiket 100% Refund quick filter on search results");
        searchResultPage.clickTiket100PercentRefundQuickFilter();

        step("filter airline {" + airlineName + " }");
        flightSearchResultPageWeb.filterAirline(airlineName);

        step("select flight departure");
        searchResultPage.selectFlight();

        step("User is redirected to Product Review Page");
        Assertion.assertThat("Product Review page is visible", productReviewPage.isPresent(), is(true));

        step("User expands first flight route and reads airline code for mock validation");
        String airlineCode = productReviewPage.expandFirstFlightRouteAndGetAirlineCode();

        step("select the SELECT Button");
        flightProductReviewPageWeb.clickDownPaySelectBtn();

        step("User clicks No thanks button");
        flightProductReviewPageWeb.clickNoThanksBtn();

        step("Enable same as contact details for first passenger");
        bookingFormPage.clickSameContactToggle();

        step("User fill passenger details");
        bookingFormPage.fillPassengerDetailsLogin(adultPassengers, childPassengers, infantPassengers, "domestic", "");

        step("User clicks extra protection for Baggage");
        bookingFormPage.clickExtraProtectionAddButtonForBaggage();

        step("Verify Booking benefits section shows 100% Refund");
        Assertion.assertThat("100% Refund is present under Booking benefits", bookingFormPage.isBookingBenefits100PercentRefundDisplayed(), is(true));

        step("User continues to payment");
        bookingFormPage.goToPaymentPageFromBookingFormPage();
        bookingFormPage.clickNoThanksOnFlightMayReschedulePopup();

        step("Payment page should be displayed");
        Assertion.assertThat("PaymentPageLoaded", paymentPageWeb.isPaymentPagepresent(), is(true));

        step("User Submit payment method");
        String orderId = paymentPageWeb.getOrderId();
        Assertion.assertThat("Order Id successfully generated", orderId, notNullValue());

        step("Validate all suppliers are mock issued by airline for departure and return, skipped if real issued supplier exist");
        boolean isDepartSuppliersMocked = flightApi.areAllSuppliersMockIssuedByAirline(flightDynamicData.airlineCodeDepart, flightDynamicData.originAirport, flightDynamicData.destinationAirport);

        step("Only Submit payment if all suppliers are mocked { isDepartSuppliersMocked: " + isDepartSuppliersMocked + "}");
        if (Configs.ENVIRONMENT != Environment.PROD && isDepartSuppliersMocked) {

            step("User select payment method ");
            paymentPageWeb.clickOnSelectPaymentMethod();
            paymentPageWeb.goToPaymentMethodsGroup(paymentGroup);
            paymentPageWeb.selectPaymentMethod(paymentMethod);

            step("Click on Pay button");
            paymentPageWeb.clickOnPaymentButton();

            step("Verify the payment successful page is visible or not");
            Assertion.assertThat("Verify the payment successful page is visible", paymentPageWeb.isPaymentSuccessfulVisible(), is(true));

            step("User try to open see order list");
            paymentPageWeb.clickSeeOrderListButton();

            step("Find the issued order");
            orderListPage.openActiveTicketViaURL(orderId);
            orderDetailPage.skipCoachmark();

            step("Open order list and go to order detail page");
            homePage.goToYourOrderUrl();
            String orderStatus = orderListPage.getOrderStatus(orderId);
            Assertion.assertThat("Order list (Your Orders) is displayed", orderListPage.isOrderListPageDisplayed(), is(true));


            Assertion.assertThat("Order status should be ETICKET_ISSUED or PROCESSING_ETICKET",
                    orderStatus.equalsIgnoreCase("ETICKET_ISSUED") || orderStatus.equalsIgnoreCase("PROCESSING_ETICKET"), is(true));
            orderListPage.openActiveTicketViaURL(orderId);
            orderDetailPage.closeEticketPopUp();
        }
    }

    @Author(names = "Manan Kumar")
    @Module(name = "Flight")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void FLG_testSortByCheapestPriceOnDepartAndReturn()  throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("FLG_testSortByCheapestPriceOnDepartAndReturn");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPage = FlightLandingPageWeb.getInstance(session);
        FlightSearchResultPageWeb<?> searchResultPage = FlightSearchResultPageWeb.getInstance(session);

        String origin = param.get("origin");
        String destination = param.get("destination");
        int daysToDepartureDate = ThreadLocalRandom.current().nextInt(10, 20);
        int daysToReturnDate = ThreadLocalRandom.current().nextInt(3, 10);

        int adultPassengers = Integer.parseInt(param.get("adultCount"));
        int childPassengers = Integer.parseInt(param.get("childCount"));
        int infantPassengers = Integer.parseInt(param.get("infantCount"));

        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate,daysToDepartureDate);
        String returnDate = CoreFunctions.addToLongDate(departureDate, daysToReturnDate);
        String currency = param.get("currency");

        step("User open Home Page");
        homePage.goToHomePage();

        step("User select { " + currency + " } currency");
        homePage.selectCurrency(currency);

        step("User open Flight Landing Page");
        homePage.openVerticalPageViaURL("Flights");
        Assertion.assertThat("Flight Landing Page is Opened", flightLandingPage.isOnFlightPage(), is(true));

        step("User select origin");
        flightLandingPage.selectOrigin(origin);

        step("User select destination");
        flightLandingPage.selectDestination(destination);

        step("Toggle trip type");
        flightLandingPage.toggleTripType("roundTrip");

        step("Select date");
        flightLandingPage.selectDate(departureDate,returnDate);

        step("add passenger");
        flightLandingPage.addPassenger(adultPassengers,childPassengers,infantPassengers);

        step("User click search button");
        flightLandingPage.searchFlight();
        Assertion.assertThat("Search Result Page is loaded", searchResultPage.isSearchResultPagePresent(), is(true));

        step("Wait for Search Result page to be fully loaded.");
        searchResultPage.isSRPLoaded();

        step("Open sort filter and select lowest price.");
        searchResultPage.openSortFilter();
        searchResultPage.selectSortByLowestPrice();

        step("Assert active departure date picker price matches first flight card per-pax price.");
        searchResultPage.assertSelectedDatePriceMatchesFirstCardPerPaxPrice();

        step("Select Round-trip Deals quick filter.");
        searchResultPage.roundTripDealsFilter();

        step("select flight departure");
        searchResultPage.selectFlight();
        Assertion.assertThat("Search Result Page is loaded", searchResultPage.isSearchResultPagePresent(), is(true));

        step("Open sort filter and select lowest price.");
        searchResultPage.openSortFilter();
        searchResultPage.selectSortByLowestPrice();

        step("Verify Round-trip Deals departure delta block is displayed on SRP (Departure flight + delta + disclaimer).");
        searchResultPage.assertRoundTripDealsDepartureDeltaBlockVisible();

        step("Assert active return date picker price matches first schedule after Round-trip Deals delta disclaimer.");
        searchResultPage.assertReturnDateStripMatchesRtdCardBodyPerPaxPrice();
    }

    @Author(names = "Manan Kumar")
    @Module(name = "Flight")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void FLG_testCancelFlightFromYourOrders()  throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("FLG_testCancelFlightFromYourOrders");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        FlightApi<?> flightApi = new FlightApi<>(session);

        String origin = param.get("origin");
        String destination = param.get("destination");
        int adultPassengers = Integer.parseInt(param.get("adultCount"));
        int childPassengers = Integer.parseInt(param.get("childCount"));
        int infantPassengers = Integer.parseInt(param.get("infantCount"));
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String username = param.get("username");
        String password = param.get("password");
        String airlineName = param.get("airlineName");
        String currency = param.get("currency");

        step("Find dynamic data");
        FlightDynamicData flightDynamicData = flightApi.findDate(
                username, password,
                origin, destination, daysToDepartureDate,
                adultPassengers, childPassengers, infantPassengers,
                "oneway", airlineName, "", "", "", currency
        );

        step("Create waiting payment order via API");
        String orderId = flightApi.createWaitForPaymentFlightB2CDomestic(
                username, password,
                flightDynamicData.originAirport, flightDynamicData.destinationAirport,
                flightDynamicData.departureDate, "ONE_WAY",
                adultPassengers, childPassengers, infantPassengers,
                flightDynamicData.airlineDepart,
                "Preprod", "Automation"
        );
        Assertion.assertThat("Order Id successfully generated", orderId, notNullValue());

        step("User open Home Page");
        homePage.goToHomePage();

        step("Go to account Page");
        loginPage.goToAccountPage();

        step("User login");
        loginPage.loginWithSpecificAccountViaCookie(username, password);

        step("User select { " + currency + " } currency");
        homePage.selectCurrency(currency);

        step("Navigate to My Orders and verify unpaid order status");
        homePage.goToYourOrderUrl();
        Assertion.assertThat("Order list (Your Orders) is displayed", orderListPage.isOrderListPageDisplayed(), is(true));
        String orderStatusUnpaid = orderListPage.getOrderStatus(orderId);
        Assertion.assertThat("Order should be unpaid (WAITING_PAYMENT or SELECT_PAYMENT_METHOD) on My Orders", orderStatusUnpaid.equalsIgnoreCase("WAITING_PAYMENT") || orderStatusUnpaid.equalsIgnoreCase("SELECT_PAYMENT_METHOD"), is(true));

        step("Go to Order Detail Page");
        orderListPage.openActiveTicketViaURL(orderId);

        step("Go to Order Detail Page");
        orderListPage.reloadPage();
        orderListPage.openActiveTicketViaURL(orderId);

        step("Cancel order via Refund/Reschedule modal");
        orderDetailPage.cancelOrderViaRefundRescheduleModal();

        step("Verify canceled order notification banner is shown");
        Assertion.assertThat("Order detail shows You have canceled this order (or ID equivalent)", orderDetailPage.isCanceledOrderNotificationBannerDisplayed(orderId, 10), is(true));

        step("User clicks Delete order and confirms if prompted");
        orderDetailPage.clickDeleteOrderOnCanceledOrderDetail();

    }

    @Author(names = "Manan Kumar")
    @Module(name = "Flight")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void FLG_testBookRoundTripDifferentAirlineSinglePassengerInternationalRoute() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("FLG_testBookRoundTripDifferentAirlineSinglePassengerInternationalRoute");
        BaseTest.paramsThreadLocal.set(param);

        FlightApi<?> flightApi = new FlightApi<>(session);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPage = FlightLandingPageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        FlightSearchResultPageWeb<?> flightSearchResultPageWeb = FlightSearchResultPageWeb.getInstance(session);
        FlightProductReviewPageWeb<?> flightProductReviewPageWeb = FlightProductReviewPageWeb.getInstance(session);
        FlightBookingFormPageWeb<?> flightBookingFormPageWeb = FlightBookingFormPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPageWeb = PaymentPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        CalendarPageWeb<?> calendarPage = CalendarPageWeb.getInstance(session);

        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String origin = param.get("originAirport");
        String destination = param.get("destinationAirport");
        String currency = param.get("currency");
        int daysToDepartureDate = ThreadLocalRandom.current().nextInt(30, 60);
        int duration = Integer.parseInt(param.get("duration"));
        String username = param.get("username");
        String password = param.get("password");
        int adult = Integer.parseInt(param.get("adult"));
        int children = Integer.parseInt(param.get("children"));
        int infant = Integer.parseInt(param.get("infant"));
        String tripType = param.get("tripType");
        String paymentGroup = param.get("paymentGroup");
        String paymentMethod = param.get("paymentMethod");
        String airline = param.get("airline");
        String airlineReturn = param.get("airlineReturn");

        step("Find dynamic data");
        FlightDynamicData flightDynamicData = flightApi.findDateRT(
                username, password,
                origin, destination, daysToDepartureDate, duration,
                adult, children, infant,
                tripType, "", "", "", "", "", "", currency
        );
        String departureDate = CoreFunctions.convertToLongDate(flightDynamicData.departureDate);
        String returnDate = CoreFunctions.convertToLongDate(flightDynamicData.returnDate);
        airline = flightDynamicData.airlineDepart;
        airlineReturn = flightDynamicData.airlineReturn;

        step("User open home page");
        homePage.goToHomePage();

        step("User Login ");
        loginPage.loginWithSpecificAccountViaCookie(username, password);

        step("User select { " + currency + " } currency");
        homePage.selectCurrency(currency);

        step("User open Flight Landing Page");
        homePage.openVerticalPageViaURL("Flights");
        Assertion.assertThat("Flight Landing Page is Opened", flightLandingPage.isOnFlightPage(), is(true));

        step("User select origin");
        flightLandingPage.selectOrigin(origin);

        step("Select destination airport");
        flightLandingPage.selectDestination(destination);

        step("Select date");
        flightLandingPage.clickOnCalendarWidgetToOpen();
        calendarPage.selectDate(departureDate, returnDate);

        step("Toggle trip type");
        flightLandingPage.toggleTripType(tripType);

        step("Add Number of adults, children and infant passengers");
        flightLandingPage.addPassenger(adult, children, infant);

        step("search flight");
        flightLandingPage.searchFlight();
        Assertion.assertThat("Search Result Page is loaded", flightSearchResultPageWeb.isSearchResultPagePresent(), is(true));

        step("Wait for Search Result page to be fully loaded.");
        flightSearchResultPageWeb.isSRPLoaded();

        step("User Selects Flight departure airline " + airline);
        flightSearchResultPageWeb.selectFlightByAirline(airline);
        Assertion.assertThat("Search Result Page is loaded", flightSearchResultPageWeb.isSearchResultPagePresent(), is(true));

        step("User Selects Flight return airline " + airlineReturn);
        flightSearchResultPageWeb.selectFlightByAirline(airlineReturn);

        step("User checks schedule");
        Assertion.assertThat("Check Departure Schedule visible", flightProductReviewPageWeb.isDepartureScheduleVisible(), is(true));
        Assertion.assertThat("Check Return Schedule visible", flightProductReviewPageWeb.isReturnScheduleVisible(), is(true));

        step("User goes to product review page");
        flightProductReviewPageWeb.clickSelectBtnRegular();

        step("Continue to booking form");
        flightProductReviewPageWeb.continueToBookingForm();
        flightBookingFormPageWeb.clickNoThanksOnFlightMayReschedulePopup();

        int initialAmount = Integer.parseInt(flightBookingFormPageWeb.getPriceText().replaceAll("[^0-9]", ""));

        step("Click toggle to fill passengers details and save ");
        flightBookingFormPageWeb.fillPassengerDetailsLogin(adult, children, infant, "international", airline);

        step("Verify Flight details visible");
        flightBookingFormPageWeb.isFlightDetailsVisible();

        step("Go to flight detail");
        flightBookingFormPageWeb.goOnFlightDetail();
        flightBookingFormPageWeb.closePopUp();

        step("See Refund details");
        flightBookingFormPageWeb.clickSeeDetailRefundAndReschedule();

        step("Verify Refund visible or not");
        boolean isRefundRescheduleVisible = flightBookingFormPageWeb.isRefundRescheduleVisible();
        Assertion.assertThat("Verify Refund visible or not", isRefundRescheduleVisible, is(true));

        step("Close refund detail popup");
        flightBookingFormPageWeb.closeSecondPopup();

        step("Click More Info in Extra protection");
        flightBookingFormPageWeb.clickMoreInfoFullProtection();

        step("Verify Terms and conditions link");
        flightBookingFormPageWeb.clickTermsAndConditionsLink();

        step("close more info popup");
        flightBookingFormPageWeb.closePopUp();

        step("Add insurance");
        InsuranceGeneralData insuranceGeneralData = flightBookingFormPageWeb.selectInsurance(currency);
        int insurancePrice = Integer.parseInt(insuranceGeneralData.insurancePrice.replaceAll("[^0-9]", ""));
        int finalPrice = Integer.parseInt(flightBookingFormPageWeb.getPriceText().replaceAll("[^0-9]", ""));
        Assertion.assertThat("Initial + insurance price = final price", initialAmount + insurancePrice, is(finalPrice));

        step("Go to payment page");
        flightBookingFormPageWeb.goToPaymentPageFromBookingFormPage();

        step("verify Review Passenger's Data");
        flightBookingFormPageWeb.clickItsCorrectOnReviewPassengerDataPopup();
        flightBookingFormPageWeb.clickNoThanksOnFlightMayReschedulePopup();

        step("Payment page should be displayed");
        Assertion.assertThat("PaymentPageLoaded", paymentPageWeb.isPaymentPagepresent(), is(true));
        String orderID = String.valueOf(paymentPageWeb.getOrderId());
        Assertion.assertThat("OrderIDVisible " + orderID, !orderID.isEmpty(), is(true));

        step("Validate all suppliers are mock issued by airline for departure and return, skipped if real issued supplier exist");
        boolean isDepartSuppliersMocked = flightApi.areAllSuppliersMockIssuedByAirline(flightDynamicData.airlineCodeDepart, flightDynamicData.originAirport, flightDynamicData.destinationAirport);
        boolean isReturnSuppliersMocked = flightApi.areAllSuppliersMockIssuedByAirline(flightDynamicData.airlineCodeReturn, flightDynamicData.destinationAirport, flightDynamicData.originAirport);

        step("Only Submit payment if all suppliers are mocked { isDepartSuppliersMocked: " + isDepartSuppliersMocked + ", isReturnSuppliersMocked: " + isReturnSuppliersMocked + " }");
        if (Configs.ENVIRONMENT != Environment.PROD && isDepartSuppliersMocked && isReturnSuppliersMocked) {
            step("User select payment method ");
            paymentPageWeb.clickOnSelectPaymentMethod();
            paymentPageWeb.goToPaymentMethodsGroup(paymentGroup);
            paymentPageWeb.selectPaymentMethod(paymentMethod);

            step("Click on Pay button");
            paymentPageWeb.clickOnPaymentButton();

            step("Verify the payment successful page is visible or not");
            Assertion.assertThat("Verify the payment successful page is visible", paymentPageWeb.isPaymentSuccessfulVisible(), is(true));

            step("User try to open see order list");
            paymentPageWeb.clickSeeOrderListButton();

            step("Find the issued order");
            String orderStatus = orderListPage.getOrderStatus(orderID);
            orderListPage.openActiveTicketViaURL(orderID);
            orderDetailPage.skipCoachmark();

            step("Verify flight order details");
            Assertion.assertThat("orderID should be visible and should be same as from payment page", orderDetailPage.getOrderId(), is(orderID));

            Assertion.assertThat("Order status should be ETICKET_ISSUED or PROCESSING_ETICKET",
                    orderStatus.equalsIgnoreCase("ETICKET_ISSUED") || orderStatus.equalsIgnoreCase("PROCESSING_ETICKET"), is(true));
            orderDetailPage.closeEticketPopUp();

        }
    }

    @Author(names = "Ram Pratap Singh")
    @Module(name = "Flight")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void FLG_testValidateAirlineFilterDepartAndReturnRoundTrip() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("FLG_testValidateAirlineFilterDepartAndReturnRoundTrip");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPageWeb = FlightLandingPageWeb.getInstance(session);
        FlightSearchResultPageWeb<?> flightSearchResultPageWeb = FlightSearchResultPageWeb.getInstance(session);
        FlightApi<?> flightApi = new FlightApi<>(session);

        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");
        String airlineNameDepart = param.get("airlineNameDepart");
        String airlineNameReturn = param.get("airlineNameReturn");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int duration = Integer.parseInt(param.get("duration"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);
        String returnDate = CoreFunctions.addToLongDate(departureDate, duration);

        step("Find dynamic data for round trip");
        FlightDynamicData flightDynamicData = flightApi.findDateRT(
                "","",
                originAirport,destinationAirport,daysToDepartureDate,duration,
                1,0,0,
                "roundtrip","","","","","","",""
        );
        originAirport = flightDynamicData.originAirport;
        destinationAirport = flightDynamicData.destinationAirport;
        departureDate = CoreFunctions.convertToLongDate(flightDynamicData.departureDate);
        returnDate = CoreFunctions.convertToLongDate(flightDynamicData.returnDate);
        airlineNameDepart = flightDynamicData.airlineDepart;
        airlineNameReturn = flightDynamicData.airlineReturn;

        step("User open home page");
        homePage.goToHomePage();

        step("User open Flight Landing Page");
        homePage.openVerticalPageViaURL("Flights");
        Assertion.assertThat("Flight Landing Page is Opened", flightLandingPageWeb.isOnFlightPage(), is(true));

        step("Toggle trip type to round trip");
        flightLandingPageWeb.toggleTripType("roundTrip");

        step("Select origin airport");
        flightLandingPageWeb.selectOrigin(originAirport);

        step("Select destination airport");
        flightLandingPageWeb.selectDestination(destinationAirport);

        step("Select departure and return date");
        flightLandingPageWeb.selectDate(departureDate, returnDate);

        step("Search flight");
        flightLandingPageWeb.searchFlight();
        Assertion.assertThat("Search Result Page is loaded", flightSearchResultPageWeb.isSearchResultPagePresent(), is(true));

        step("Wait for Search Result page to be fully loaded");
        flightSearchResultPageWeb.isSRPLoaded();

        step("Filter by airline {" + airlineNameDepart + "} on departure SRP");
        flightSearchResultPageWeb.filterAirline(airlineNameDepart);
        flightSearchResultPageWeb.isSRPLoaded();

        step("Validate departure SRP shows only filtered airline");
        List<String> departAirlines = flightSearchResultPageWeb.getAirlineNames(airlineNameDepart, airlineNameDepart);
        Assertion.assertThat("Departure SRP should have flights", departAirlines.size(), is(greaterThan(0)));
        Assertion.assertThat("All departure flights should contain filtered airline only",
                departAirlines, everyItem(containsStringIgnoringCase(airlineNameDepart == null ? "" : airlineNameDepart.trim())));

        step("Select a departure flight");
        flightSearchResultPageWeb.selectFlightByAirline(airlineNameDepart);

        step("Filter by airline {" + airlineNameReturn + "} on return SRP");
        Assertion.assertThat("Search Result Page is loaded", flightSearchResultPageWeb.isSearchResultPagePresent(), is(true));
        flightSearchResultPageWeb.filterAirline(airlineNameReturn);
        flightSearchResultPageWeb.isSRPLoaded();

        step("Validate return SRP shows only filtered airline");
        List<String> returnAirlines = flightSearchResultPageWeb.getAirlineNames(airlineNameReturn, airlineNameReturn);
        Assertion.assertThat("Return SRP should have flights", returnAirlines.size(), is(greaterThan(0)));
        Assertion.assertThat("All return flights should contain filtered airline only",
                returnAirlines, everyItem(containsStringIgnoringCase(airlineNameReturn == null ? "" : airlineNameReturn.trim())));
    }

    @Author(names = "Aditi")
    @Module(name = "LandingPage")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void testRoundTripToggleRemainsOffAfterSelectingDepartureDate() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testRoundTripToggleRemainsOffAfterSelectingDepartureDate");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPage = FlightLandingPageWeb.getInstance(session);
        FlightSearchResultPageWeb<?> searchResultPage = FlightSearchResultPageWeb.getInstance(session);

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");
        String departureDate = CoreFunctions.addToLongDate(CoreFunctions.getCurrentLongDate(), daysToDepartureDate);

        step("Access flight landing page for the first time");
        homePage.goToHomePage();
        homePage.openVerticalPageViaURL("Flights");
        Assertion.assertThat("Flight landing page is opened", flightLandingPage.isOnFlightPage(), is(true));

        step("Turn off round trip toggle");
        flightLandingPage.toggleTripType(tripType);

        step("Choose departure date");
        flightLandingPage.selectDate(departureDate);

        step("Verify round trip toggle remains OFF");
        Assertion.assertThat("Round trip toggle remains OFF after selecting departure date",
                flightLandingPage.isRoundTripToggleOn(), is(false));

        step("Select origin and destination airports");
        flightLandingPage.selectOrigin(originAirport);
        flightLandingPage.selectDestination(destinationAirport);

        step("Search flight");
        flightLandingPage.searchFlight();
        Assertion.assertThat("Search result page is loaded", searchResultPage.isSearchResultPagePresent(), is(true));
        searchResultPage.isSRPLoaded();

        step("Verify SRP search widget shows same origin and destination");
        searchResultPage.verifySrpSearchWidgetLocations(originAirport, destinationAirport);

        step("Verify SRP search widget shows single one-way departure date only");
        searchResultPage.verifySrpOneWayDateMatches(departureDate);

        step("Verify SRP URL reflects one-way search parameters");
        searchResultPage.verifySrpOneWayUrlParams(originAirport, destinationAirport, departureDate);
    }

    @Author(names = {"Akbar","Priyanka"})
    @Module(name = "Multifare")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void testBookingFreeMealsFreeSeatsMultifareSupplier()  throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testBookingFreeMealsFreeSeatsMultifareSupplier");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPage = FlightLandingPageWeb.getInstance(session);
        FlightSearchResultPageWeb<?> searchResultPage = FlightSearchResultPageWeb.getInstance(session);
        FlightProductReviewPageWeb<?> flightProductReviewPageWeb = FlightProductReviewPageWeb.getInstance(session);
        FlightBookingFormPageWeb<?> flightBookingFormPageWeb = FlightBookingFormPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPageWeb = PaymentPageWeb.getInstance(session);

        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));

        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);
        String tripType = param.get("tripType");
        String username = param.get("username");
        String password = param.get("password");
        String airlineName = param.get("airlineName");
        String fareCriteria = param.get("fareCriteria");
        int adultPassengers = Integer.parseInt(param.get("adultPassengers"));


        step("Logging in using {" + username + "}");
        loginPage.loginWithSpecificAccountViaCookie(username, password);

        step("User open Flight Landing Page");
        homePage.openVerticalPageViaURL("Flights");
        Assertion.assertThat("Flight Landing Page is Opened", flightLandingPage.isOnFlightPage(), is(true));

        step("Change Currency to Non-IDR");
        homePage.selectCurrency("SGD");

        step("Toggle trip type");
        flightLandingPage.toggleTripType(tripType);

        step("Select origin airport");
        flightLandingPage.selectOrigin(originAirport);

        step("Select destination airport");
        flightLandingPage.selectDestination(destinationAirport);

        step("Select date");
        flightLandingPage.selectDate(departureDate);

        step("Toggle trip type");
        flightLandingPage.toggleTripType(tripType);

        step("enter pax");
        flightLandingPage.addPassenger(adultPassengers, 0, 0);

        step("search flight");
        flightLandingPage.searchFlight();
        Assertion.assertThat("Search Result Page is loaded", searchResultPage.isSearchResultPagePresent(), is(true));

        step("filter airline {" + airlineName + " }");
        searchResultPage.filterAirline(airlineName);

        step("select flight for departure");
        searchResultPage.selectFlight();

        step("select the fare that has free meals and free seat");
        flightProductReviewPageWeb.selectFare(fareCriteria);

        step("close Reschedule popUp");
        flightBookingFormPageWeb.clickNoThanksOnFlightMayReschedulePopup();

        step("enter the passenger Name");
        flightBookingFormPageWeb.fillPassengerDetailsLogin(adultPassengers, 0, 0, "international", airlineName);

        step("Check Price Booking Form - Before Add Free Addons");
        Double priceBeforeAddFreeAddons = CoreFunctions.convertPriceSafely(flightBookingFormPageWeb.getPriceText());

        step("Select Meal addons");
        flightBookingFormPageWeb.selectMultiMealAddons(adultPassengers,false);
        Double bookingFormMealsAddonPrice = flightBookingFormPageWeb.getTotalMealAddonPrice();

        step("Select Seat Addon");
        flightBookingFormPageWeb.selectSeatAddons();
        Double bookingFormSeatsAddonPrice = flightBookingFormPageWeb.getTotalSeatAddonPrice();

        step("Check Price Booking Form - After Add Free Addons");
        Double priceAfterAddFreeAddons = CoreFunctions.convertPriceSafely(flightBookingFormPageWeb.getPriceText());

        step("going to paymentPage");
        flightBookingFormPageWeb.goToPaymentPageFromBookingFormPage();

        step("verify Review Passenger's Data");
        flightBookingFormPageWeb.clickItsCorrectOnReviewPassengerDataPopup();

        step("click I don't want it");
        flightBookingFormPageWeb.clickOkayGotIt();
        flightBookingFormPageWeb.clickNoThanksOnFlightMayReschedulePopup();

        step("Payment page should be displayed");
        Assertion.assertThat("PaymentPageLoaded", paymentPageWeb.isPaymentPagepresent(), is(true));

        String orderID = paymentPageWeb.getOrderId();
        orderID = String.valueOf(getOrderId(orderID));

        step("Assertion");
        Assertion.assertThat("Booking form total price is not changing before and after adding free add on", priceBeforeAddFreeAddons, is(priceAfterAddFreeAddons));
        Assertion.assertThat("Meals add on price is 0 on Booking Form", bookingFormMealsAddonPrice, is(0.0));
        Assertion.assertThat("Seats add on price is 0 on Booking Form", bookingFormSeatsAddonPrice, is(0.0));
        Assertion.assertThat("OrderID is visible " + orderID, !orderID.isEmpty(), is(true));
    }

    @Author(names = "Aditi")
    @Module(name = "refund100pLogin")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void verify100pRefundInsurancePriceMatchesPrpAndBookingFormWeb() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verify100pRefundInsurancePriceMatchesPrpAndBookingFormWeb");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPage = FlightLandingPageWeb.getInstance(session);
        FlightSearchResultPageWeb<?> flightSearchResultPageWeb = FlightSearchResultPageWeb.getInstance(session);
        FlightProductReviewPageWeb<?> flightProductReviewPageWeb = FlightProductReviewPageWeb.getInstance(session);
        FlightBookingFormPageWeb<?> flightBookingFormPageWeb = FlightBookingFormPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPageWeb = PaymentPageWeb.getInstance(session);
        FlightApi<?> flightApi = new FlightApi<>(session);

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int duration = Integer.parseInt(param.get("duration"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        int chargeablePax = adultPassengers + childPassengers;

        String origin = param.get("origin");
        String destination = param.get("destination");
        String originFull = param.get("originFull");
        String destinationFull = param.get("destinationFull");
        String currency = param.get("currency");
        String username = param.get("username");
        String password = param.get("password");
        String airline = param.get("airline");
        String airlineReturn = param.get("airlineNameReturn") != null ? param.get("airlineNameReturn") : airline;
        String optionalLocation = param.get("optionalLocation") != null ? param.get("optionalLocation") : "";
        String optionalAirline = param.get("optionalAirline") != null ? param.get("optionalAirline") : "";

        step("Find dynamic data for round trip with 100% Refund");
        FlightDynamicData flightDynamicData = flightApi.findDateRT(username, password,
                origin, destination, daysToDepartureDate, duration,
                adultPassengers, childPassengers, infantPassengers,
                "roundtrip", airline, airlineReturn, "100% refund", "100% refund",
                optionalLocation, optionalAirline, currency
        );
        origin = flightDynamicData.originAirport;
        destination = flightDynamicData.destinationAirport;
        String departureDate = CoreFunctions.convertToLongDate(flightDynamicData.departureDate);
        String returnDate = CoreFunctions.convertToLongDate(flightDynamicData.returnDate);
        airline = flightDynamicData.airlineDepart;
        airlineReturn = flightDynamicData.airlineReturn;

        step("Login as " + username + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(username, password);

        step("Change Currency to { " + currency + " }");
        homePage.selectCurrency(currency);

        step("User open Flight Landing Page");
        homePage.openVerticalPageViaURL("Flights");
        Assertion.assertThat("Flight Landing Page is Opened", flightLandingPage.isOnFlightPage(), is(true));

        step("Toggle trip type to round trip");
        flightLandingPage.toggleTripType("roundTrip");

        step("User select origin");
        flightLandingPage.selectOrigin(origin);
        flightBookingFormPageWeb.clickOkayGotIt();

        step("User select destination");
        flightLandingPage.selectDestination(destination);
        flightBookingFormPageWeb.clickOkayGotIt();

        step("Select departure and return date");
        flightLandingPage.selectDate(departureDate, returnDate);

        step("Add passengers {" + adultPassengers + "-" + childPassengers + "-" + infantPassengers + "}");
        flightLandingPage.addPassenger(adultPassengers, childPassengers, infantPassengers);

        step("Search flight");
        flightLandingPage.searchFlight();
        Assertion.assertThat("Search Result Page is loaded", flightSearchResultPageWeb.isSearchResultPagePresent(), is(true));
        flightSearchResultPageWeb.isSRPLoaded();

        step("Apply Tiket 100% Refund filter and select departure flight by airline {" + airline + "}");
        flightSearchResultPageWeb.clickTiket100PercentRefundQuickFilter();
        flightSearchResultPageWeb.filterAirline(airline);
        flightSearchResultPageWeb.isSRPLoaded();
        flightSearchResultPageWeb.selectFlightByAirline(airline);
        Assertion.assertThat("Return Search Result Page is loaded", flightSearchResultPageWeb.isSearchResultPagePresent(), is(true));
        flightSearchResultPageWeb.isSRPLoaded();

        step("Apply Tiket 100% Refund filter and select return flight by airline {" + airlineReturn + "}");
        flightSearchResultPageWeb.clickTiket100PercentRefundQuickFilter();
        flightSearchResultPageWeb.filterAirline(airlineReturn);
        flightSearchResultPageWeb.isSRPLoaded();
        flightSearchResultPageWeb.selectFlightByAirline(airlineReturn);

        step("User is redirected to Product Review Page");
        Assertion.assertThat("Product Review page is visible", flightProductReviewPageWeb.isPresent(), is(true));

        step("Verify departure and return airports on PRP");
        Map<String, String> airport = flightProductReviewPageWeb.getDepartureOrArrivalAirport();
        Assertion.assertThat("Verify departure airport", airport.get("departure"), is(originFull));
        Assertion.assertThat("Verify arrival airport", airport.get("arrival"), is(destinationFull));

        step("Verify 100% Refund '+ XXX get full refund' wording on PRP fare card and price breakdown before selecting fare");
        Map<String, Double> prpInsurancePrices = flightProductReviewPageWeb.get100pRefundInsurancePricesFromPrp(chargeablePax, currency);
        double prpBreakdownTotal = prpInsurancePrices.get("prpBreakdownTotal");
        double prpFareCardAddonPerPerson = prpInsurancePrices.get("prpFareCardAddonPerPerson");

        step("Select 100% Refund fare card and continue to booking form");
        flightProductReviewPageWeb.clickSelectTicketOn100pRefundFareCardPopup();
        flightProductReviewPageWeb.clickNoThanksBtn();
        flightBookingFormPageWeb.clickOkayGotIt();
        flightBookingFormPageWeb.waitForBookingPageToLoad();
        Assertion.assertThat("Booking Form page is visible", flightBookingFormPageWeb.isBookingFormVisible(), is(true));

        step("Fill passenger details");
        flightBookingFormPageWeb.clickSameContactToggle();
        flightBookingFormPageWeb.fillPassengerDetailsLogin(adultPassengers, childPassengers, infantPassengers, "domestic", airline);

        step("Verify 100% Refund insurance price on booking form breakdown");
        flightBookingFormPageWeb.openPriceDetailSection();
        Assertion.assertThat("100% Refund is present under Booking benefits",
                flightBookingFormPageWeb.isBookingBenefits100PercentRefundDisplayed(), is(true));
        String booking100p = flightBookingFormPageWeb.getBookingPriceDetailRefundAddonPrice();
        String cleanBooking100p = booking100p.replaceAll("[^\\d.]", "");
        double bookingBreakdownTotal = Double.parseDouble(cleanBooking100p.isEmpty() ? "0" : cleanBooking100p);
        flightBookingFormPageWeb.closePopUp();

        step("Verify insurance price matches between PRP fare card wording, PRP breakdown, and booking form");
        long prpAddonTotal = Math.round(prpBreakdownTotal);
        long bookingAddonTotal = Math.round(bookingBreakdownTotal);
        long minBookingAddonTotal = Math.round(prpAddonTotal * 0.9);
        long maxBookingAddonTotal = Math.round(prpAddonTotal * 1.1);
        boolean bookingAddonExactMatch = bookingAddonTotal == prpAddonTotal;
        boolean bookingAddonWithinTolerance = bookingAddonTotal >= minBookingAddonTotal
                && bookingAddonTotal <= maxBookingAddonTotal;
        Assertion.assertThat("PRP fare card addon per-person x pax matches PRP breakdown total",
                (int) Math.round(prpBreakdownTotal), is((int) Math.round(prpFareCardAddonPerPerson
                        * prpInsurancePrices.getOrDefault("addonPaxCount", (double) chargeablePax))));
        Assertion.assertThat("Booking form addon total matches PRP addon total exactly or is within -10% to +10% (PRP="
                        + prpAddonTotal + ", booking=" + bookingAddonTotal + ", exactMatch=" + bookingAddonExactMatch
                        + ", toleranceRange=" + minBookingAddonTotal + "-" + maxBookingAddonTotal + ")",
                bookingAddonExactMatch || bookingAddonWithinTolerance, is(true));

        step("Continue to payment page");
        flightBookingFormPageWeb.goToPaymentPageFromBookingFormPage();

        step("verify Review Passenger's Data");
        flightBookingFormPageWeb.clickItsCorrectOnReviewPassengerDataPopup();
        flightBookingFormPageWeb.clickNoThanksOnFlightMayReschedulePopup();

        step("Payment page should be displayed");
        Assertion.assertThat("PaymentPageLoaded", paymentPageWeb.isPaymentPagepresent(), is(true));
        String orderID = String.valueOf(paymentPageWeb.getOrderId());
        Assertion.assertThat("OrderIDVisible " + orderID, !orderID.isEmpty(), is(true));
    }

    @Author(names = "Aditi")
    @Module(name = "SRP")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void verifyRoundTripDealsDepartureDeltaPriceMatchesLatestStreamingResponseWeb() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("verifyRoundTripDealsDepartureDeltaPriceMatchesLatestStreamingResponseWeb");
        BaseTest.paramsThreadLocal.set(param);

        Session<WebDriver> session = sessionThreadLocal.get();
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        FlightApi<?> flightApi = new FlightApi<>(session);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPage = FlightLandingPageWeb.getInstance(session);
        FlightSearchResultPageWeb<?> searchResultPage = FlightSearchResultPageWeb.getInstance(session);

        String origin = param.get("origin");
        String destination = param.get("destination");
        String username = param.get("username");
        String password = param.get("password");
        String currency = param.get("currency");
        String optionalLocation = param.get("optionalLocation");
        String optionalAirline = param.get("optionalAirline");
        int adult = Integer.parseInt(param.get("adult"));
        int child = Integer.parseInt(param.get("child"));
        int infant = Integer.parseInt(param.get("infant"));
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int duration = Integer.parseInt(param.get("duration"));

        step("Find dynamic international round-trip data with Round-trip Deals (2-2-2 pax, avoid cached route)");
        FlightDynamicData flightDynamicData = flightApi.findDateRT(
                username, password,
                origin, destination, daysToDepartureDate, duration,
                adult, child, infant,
                "roundtrip", "", "", "Round-trip Deals", "Round-trip Deals",
                optionalLocation, optionalAirline, currency
        );

        String departureDate = CoreFunctions.convertToLongDate(flightDynamicData.departureDate);
        String returnDate = CoreFunctions.convertToLongDate(flightDynamicData.returnDate);

        step("Open home page");
        homePage.goToHomePage();

        step("User login");
        loginPage.loginWithSpecificAccountViaCookie(username, password);

        step("Enable flight streaming API capture (CDP + in-page hooks for all navigations)");
        searchResultPage.enableFlightStreamingCapture();

        step("User select { " + currency + " } currency");
        homePage.selectCurrency(currency);

        step("User open Flight Landing Page");
        homePage.openVerticalPageViaURL("Flights");
        Assertion.assertThat("Flight Landing Page is Opened", flightLandingPage.isOnFlightPage(), is(true));

        step("User select origin");
        flightLandingPage.selectOrigin(origin);

        step("User select destination");
        flightLandingPage.selectDestination(destination);

        step("Toggle trip type to round trip");
        flightLandingPage.toggleTripType("roundTrip");

        step("Select departure and return date");
        flightLandingPage.selectDate(departureDate, returnDate);

        step("Add passengers");
        flightLandingPage.addPassenger(adult, child, infant);

        step("Search flight");
        flightLandingPage.searchFlight();
        Assertion.assertThat("Search Result Page is loaded", searchResultPage.isSearchResultPagePresent(), is(true));

        step("Re-enable flight streaming capture on departure SRP");
        searchResultPage.enableFlightStreamingCapture();

        step("Wait for departure SRP to be fully loaded");
        searchResultPage.isSRPLoaded();

        step("Clear streaming captures before selecting departure flight (return SRP streamings only)");
        searchResultPage.clearFlightStreamingCaptures();

        step("Apply Round-trip Deals quick filter and select departure flight");
        searchResultPage.selectFirstFlightWithRoundTripDealsLabel();
        Assertion.assertThat("Return Search Result Page is loaded", searchResultPage.isSearchResultPagePresent(), is(true));

        step("Wait for return SRP streaming to settle (including multiple streaming waves)");
        searchResultPage.isSRPLoadedAndPollStreamingCaptures();

        step("Verify Round-trip Deals departure delta block is displayed on return SRP");
        searchResultPage.assertRoundTripDealsDepartureDeltaBlockVisible();

        step("Compare UI delta price with RT_RETURN_SMART_TRIP streaming responses");
        long uiDeltaPrice = searchResultPage.getRoundTripDealsDepartureDeltaPriceFromUi();
        long apiDeltaPrice = searchResultPage.findMatchingDepartureFlightDeltaPriceFromReturnStreamingResponses(uiDeltaPrice);
        Assertion.assertThat(
                "Departure delta price on return SRP should match departureFlightDeltaPrice from the latest RT_RETURN_SMART_TRIP streaming response",
                uiDeltaPrice, is(apiDeltaPrice));
    }

    @Author(names = {"Akbar"})
    @Module(name = "Reschedule")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void verifyRescheduleSrpRoundTripDealsPriceMatchesPrpTotalWeb() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("verifyRescheduleSrpRoundTripDealsPriceMatchesPrpTotalWeb");
        BaseTest.paramsThreadLocal.set(param);

        Session<WebDriver> session = sessionThreadLocal.get();
        FlightApi<?> flightApi = new FlightApi<>(session);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        FlightReschedulePageWeb<?> reschedulePage = FlightReschedulePageWeb.getInstance(session);
        FlightRescheduleSearchPageWeb<?> rescheduleSearchPage = FlightRescheduleSearchPageWeb.getInstance(session);
        FlightRescheduleReviewPageWeb<?> rescheduleReviewPage = FlightRescheduleReviewPageWeb.getInstance(session);
        FlightSearchResultPageWeb<?> searchResultPage = FlightSearchResultPageWeb.getInstance(session);

        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));

        String origin = param.get("origin");
        String destination = param.get("destination");
        String rescheduleDestination = param.get("rescheduleDestination");
        String tripType = param.get("tripType");
        String airline = param.get("airline");
        String currency = param.get("currency");
        String phoneNumber = param.get("phoneNumber");
        String optionalLocation = param.get("optionalLocation");
        String optionalAirline = param.get("optionalAirline");
        String supplierCode = param.get("supplierCode");
        int adult = Integer.parseInt(param.get("adult"));
        int child = Integer.parseInt(param.get("child"));
        int infant = Integer.parseInt(param.get("infant"));
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int duration = Integer.parseInt(param.get("duration"));

        step("Resolve a reschedulable round-trip on " + airline);
        FlightDynamicData flightDynamicData = flightApi.findDateRT(
                username, password,
                origin, destination, daysToDepartureDate, duration,
                adult, child, infant,
                tripType, airline, airline, "reschedule", "reschedule",
                optionalLocation, optionalAirline, currency
        );
        origin = flightDynamicData.originAirport;
        destination = flightDynamicData.destinationAirport;
        airline = flightDynamicData.airlineDepart;
        String departureDate = flightDynamicData.departureDate;
        String returnDate = flightDynamicData.returnDate;

        step("Open home page");
        homePage.goToHomePage();
        homePage.selectEnglishLanguage();

        step("Check login state and log in");
        loginPage.loginIfNotWithSameEmail(username, password, otp, retryCounter);

        step("Create an issued round-trip order on a single booking code, which is what this case "
                + "needs to reschedule");
        FlightIssuedOrderFactory.IssuedOrder issuedOrder =
                new FlightIssuedOrderFactory<>(session).createIssuedRoundTrip(
                        FlightIssuedOrderFactory.Inputs.singleBookingCode(
                                username, password, origin, destination, departureDate, returnDate,
                                tripType, adult, child, infant, airline,
                                flightDynamicData.airlineCodeDepart, flightDynamicData.airlineCodeReturn,
                                phoneNumber, supplierCode,
                                new FlightIssuedOrderFactory.Card(param.get("cardNumber"),
                                        param.get("cardMonth"), param.get("cvv"),
                                        param.get("cardHolderName"))));
        String orderId = issuedOrder.orderId();

        step("Open the issued order, order id " + orderId);
        orderListPage.openActiveTicketViaURL(orderId);

        step("Go to Reschedule page");
        orderDetailPage.clickRescheduleButton();

        step("Pick cancel and rebook as the reschedule method, the only one that allows a route change");
        reschedulePage.selectCancelAndRebookMethodIfPresent();

        step("Pick pax that will be rescheduled");
        reschedulePage.selectAllPassengers();

        step("Reschedule to the international route " + rescheduleDestination);
        reschedulePage.changeDestinationAirport(rescheduleDestination);

        step("Search the new schedule");
        reschedulePage.pressFindNewScheduleButton();

        Assertion.assertThat("Reschedule Search Result Page is loaded",
                searchResultPage.isSearchResultPagePresent(), is(true));

        step("Filter Round-trip Deals on the reschedule departure SRP and take the first deal");
        searchResultPage.roundTripDealsFilter();
        long departurePrice = rescheduleSearchPage.selectFirstFlightAndGetPrice();

        step("Read what the return SRP asks for a flight outside Round-trip Deals: the amount breaking "
                + "the pair adds on top of the departure price, plus the price on the return card");
        long departurePriceChange = searchResultPage.getRoundTripDealsDepartureDeltaPriceFromUi();
        long returnPrice = rescheduleSearchPage.selectFirstFlightWithoutDealsAndGetPrice();

        step("Open the PRP of the picked flights");
        Assertion.assertThat("Cheapest fare on the PRP matches the prices the reschedule SRP showed",
                rescheduleReviewPage.getCheapestFarePrice(), is(departurePrice + departurePriceChange + returnPrice));
    }

    @Author(names = {"Priyanka"})
    @Module(name = "ValidateEmailOnBookingFormAfterChangeEmail")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void testEmailOnBookingFormAfterChangeEmail() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testEmailOnBookingFormAfterChangeEmail");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPage = FlightLandingPageWeb.getInstance(session);
        FlightSearchResultPageWeb<?> flightSearchResultPageWeb = FlightSearchResultPageWeb.getInstance(session);
        FlightProductReviewPageWeb<?> flightProductReviewPageWeb = FlightProductReviewPageWeb.getInstance(session);
        FlightBookingFormPageWeb<?> flightBookingFormPageWeb = FlightBookingFormPageWeb.getInstance(session);
        AccountCenterPageWeb<?> accountCenterPage = AccountCenterPageWeb.getInstance(session);
        Unm<?> unm = new Unm<>(session);

        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");
        String tripType = param.get("tripType");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));

        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);
        int duration = Integer.parseInt(param.get("duration"));
        String returnDate = CoreFunctions.addToLongDate(departureDate, duration);
        String otp = param.get("otp");
        String passengerName = param.get("passengerName");
        String airlineName = param.get("airlineName");

        step("Register new account");
        RegisterNewAccountData accountData = unm.registerNewAccountViaApiUNM();
        String email = accountData.email;
        String password = accountData.password;

        step("Logging in using {" + email + "}");
        loginPage.loginIfNotWithSameEmail(email, password, otp, 1);

        step("User open Home Page");
        homePage.goToHomePage();

        step("Go to account Page");
        loginPage.goToAccountPage();

        step("Open Account Center page");
        accountCenterPage.clickToAccountCenter();

        step("Update Email");
        String newEmail = accountCenterPage.updateEmail(email);

        step("User again open Home Page");
        homePage.goToHomePage();

        step("User open Flight Landing Page");
        homePage.openVerticalPageViaURL("Flights");

        step("Toggle trip type");
        flightLandingPage.toggleTripType(tripType);

        step("Select origin airport");
        flightLandingPage.selectOrigin(originAirport);

        step("Select destination airport");
        flightLandingPage.selectDestination(destinationAirport);

        step("Select date");
        flightLandingPage.selectDate(departureDate);

        step("Toggle trip type");
        flightLandingPage.toggleTripType(tripType);

        step("search flight");
        flightLandingPage.searchFlight();

        step("filter airline {" + airlineName + " }");
        flightSearchResultPageWeb.filterAirline(airlineName);

        step("select flight for departure");
        flightSearchResultPageWeb.selectFlight();

        step("select the SELECT Button");
        flightProductReviewPageWeb.clickSelectBtnRegular();

        step("close Reschedule popUp");
        flightBookingFormPageWeb.clickNoThanksOnFlightMayReschedulePopup();

        step("check changed email");
        boolean isEmailCorrect = flightBookingFormPageWeb.verifyChangeEmail(newEmail);
        Assertion.assertThat("Is new email present", isEmailCorrect, is(true));

        step("delete account");
        unm.deleteAccountViaApi(accountData.unmData);
    }

    @Author(names = "Vanshika Agarwal")
    @Module(name = "Booking")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void testBookingFormDoesNotUpdateLoginStatus() throws FailedLoginException, InterruptedException {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        Map<String, String> param = getParam("testBookingFormDoesNotUpdateLoginStatus");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPageWeb = FlightLandingPageWeb.getInstance(session);
        FlightSearchResultPageWeb<?> flightSearchResultPage = FlightSearchResultPageWeb.getInstance(session);
        FlightBookingFormPageWeb<?> flightBookingFormPage = FlightBookingFormPageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        FlightProductReviewPageWeb<?> productReviewPage = FlightProductReviewPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);

        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
        String origin = param.get("origin");
        String destination = param.get("destination");
        int adultPassengers = Integer.parseInt(param.get("adultCount"));
        int daysToDepartureDate = ThreadLocalRandom.current().nextInt(30, 60);
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate,daysToDepartureDate);
        String airlineName = param.get("airlineName");
        String tripType = param.get("tripType");
        String contactSalutation = param.get("salutation");
        String contactFullName = param.get("name");
        String contactPhone = param.get("phone");
        String paxDob = param.get("dob");
        String paxNationality = param.get("nationality");

        step("User open Home Page");
        homePage.goToHomePage();

        step("Logout via URL");
        loginPage.logoutViaUrl();

        step("User open Flight Landing Page");
        homePage.openVerticalPageViaURL("Flights","IDR");
        Assertion.assertThat("Flight Landing Page is Opened", flightLandingPageWeb.isOnFlightPage(), is(true));

        step("Toggle trip type");
        flightLandingPageWeb.toggleTripType(tripType);

        step("User select origin");
        flightLandingPageWeb.selectOrigin(origin);
        flightBookingFormPage.clickOkayGotIt();

        step("User select destination");
        flightLandingPageWeb.selectDestination(destination);
        flightBookingFormPage.clickOkayGotIt();

        step("Select date");
        flightLandingPageWeb.selectDate(departureDate);

        step("Toggle trip type");
        flightLandingPageWeb.toggleTripType(tripType);

        step("Add passenger");
        flightLandingPageWeb.addPassenger(adultPassengers,0,0);

        step("User click search button");
        flightLandingPageWeb.searchFlight();
        Assertion.assertThat("Search Result Page is loaded", flightSearchResultPage.isSearchResultPagePresent(), is(true));

        step("Wait for Search Result page to be fully loaded.");
        flightSearchResultPage.isSRPLoaded();

        step("User Selects Flight AirAsia Indonesia");
        flightSearchResultPage.filterAirline(airlineName);

        step("User Selects Any Flight");
        flightSearchResultPage.selectFlight();
        Assertion.assertThat("Product Review Page is loaded", productReviewPage.isPresent(), is(true));

        step("User Selects SELECT button in ECONOMY section");
        productReviewPage.clickSelectBtnRegular();

        step("Continue to booking form");
        productReviewPage.continueToBookingForm();
        flightBookingFormPage.clickNoThanksOnFlightMayReschedulePopup();

        step("Verify Login banner is visible on booking form page before login");
        Assertion.assertThat("Login banner should be visible on Booking Form", flightBookingFormPage.isLoginBannerVisible(), is(true));

        step("Fill booking form with contact information email same as we want to login on payment page");
        flightBookingFormPage.fillBookingDetailsSameAsLogin(contactSalutation, contactFullName, contactPhone, username, paxDob, paxNationality,1);

        step("Go to payment page");
        flightBookingFormPage.goToPaymentPageFromBookingFormPage();

        step("verify Review Passenger's Data");
        flightBookingFormPage.clickItsCorrectOnReviewPassengerDataPopup();
        flightBookingFormPage.clickNoThanksOnFlightMayReschedulePopup();

        step("If any extra popup occurs");
        flightBookingFormPage.clickOkayGotIt();

        step("Payment page should be displayed");
        Assertion.assertThat("PaymentPageLoaded", paymentPage.isPaymentPagepresent(), is(true));
        step("Verify Login banner is visible on payment page");
        Assertion.assertThat("Login banner should be visible on Payment Page", paymentPage.isLoginBannerVisible(), is(true));

        step("Click login banner and login on payment page");
        paymentPage.clickLoginBanner();
        loginPage.loginIntoWebFromDifferentPage(username, password,otp,3);

        step("Verify Login banner is NO LONGER visible on payment page");
        Assertion.assertThat("Login banner should disappear after login on Payment Page", paymentPage.isLoginBannerVisible(), is(false));

        step("Navigate back to Booking Form Page");
        flightBookingFormPage.navigateBackToBookingForm();
        flightBookingFormPage.waitForBookingPageToLoad();

        step("Verify Login banner is NO LONGER visible on Booking Form page");
        Assertion.assertThat("Login banner should disappear after navigating back to Booking Form", flightBookingFormPage.isLoginBannerVisible(), is(false));

        step("Verify the email field is disabled/read-only");
        Assertion.assertThat("Email field should be disabled after session state updates", flightBookingFormPage.isEmailFieldDisabled(), is(true));
    }

    @Author(names = "Vanshika Agarwal")
    @Module(name = "Booking")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void verifyInterimIdPassedAfterRetryBooking() throws InterruptedException, FailedLoginException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyInterimIdPassedAfterRetryBooking");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPage = FlightLandingPageWeb.getInstance(session);
        FlightSearchResultPageWeb<?> searchResultPage = FlightSearchResultPageWeb.getInstance(session);
        FlightProductReviewPageWeb<?> productReviewPage = FlightProductReviewPageWeb.getInstance(session);
        FlightBookingFormPageWeb<?> flightBookingFormPage = FlightBookingFormPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);
        TravelPlanWeb<?> travelPlanPage = TravelPlanWeb.getInstance(session);
        FlightApi<?> flightApi = new FlightApi<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");
        String tripType = param.get("tripType");
        String airlineName = param.get("airlineName");
        String action = param.get("action");
        String passengerName = param.get("passengerName");
        String currency = param.get("currency");
        int daysToDepartureDate = ThreadLocalRandom.current().nextInt(15,45);
        String departureDate = CoreFunctions.addToLongDate(CoreFunctions.getCurrentLongDate(), daysToDepartureDate);
        param.put("departureDate", CoreFunctions.convertDateFormat(departureDate, "d MMMM yyyy", "yyyy-MM-dd"));

        step("Login to B2B Corporate");
        homePage.goToHomePage();
        loginPage.goToAccountPage();
        loginPage.loginIfNotWithSameEmail(username, password, otp, 1);
        homePage.goToHomePage();
        homePage.selectCurrency(currency);

        step("Select One Way Flight Single Pax");
        homePage.openVerticalPageViaURL("Flights");
        Assertion.assertThat("Flight Landing Page is Opened", flightLandingPage.isOnFlightPage(), is(true));
        flightLandingPage.toggleTripType(tripType);
        flightLandingPage.selectOrigin(originAirport);
        flightLandingPage.selectDestination(destinationAirport);
        flightLandingPage.selectDate(departureDate);
        flightLandingPage.addPassenger(1, 0, 0);
        flightLandingPage.searchFlight();
        Assertion.assertThat("Search Result Page is loaded", searchResultPage.isSearchResultPagePresent(), is(true));
        searchResultPage.isSRPLoaded();
        searchResultPage.filterAirline(airlineName);
        productReviewPage.enablePrpFlightDetailCapture();
        searchResultPage.selectFlightByAirline(airlineName);

        step("At PRP select cheapest fare");
        Assertion.assertThat("Product Review Page is loaded", productReviewPage.isPresent(), is(true));

        step("Resolve supplierCode from PRP prp/flightDetail (config id lookup uses this supplier)");
        String body = productReviewPage.getCapturedPrpFlightDetailBody();
        String encodedSupplierId = flightApi.extractEncodedSupplierIdFromPrpFlightDetail(body);
        if (encodedSupplierId.isBlank()) {
            encodedSupplierId = productReviewPage.getEncodedSupplierIdFromPrpUrl();
        }
        String supplierCode = flightApi.decodePrpSupplierId(encodedSupplierId);
        Assertion.assertThat("SupplierCode resolved from PRP", supplierCode, not(emptyOrNullString()));

        productReviewPage.clickSelectBtnRegular();
        productReviewPage.clickYesContinue();
        flightBookingFormPage.clickNoThanksOnFlightMayReschedulePopup();
        flightBookingFormPage.waitForBookingPageToLoad();
        Assertion.assertThat("Booking form is visible", flightBookingFormPage.isBookingFormVisible(), is(true));

        boolean isIntegratorUrlUpdated = false;
        try {
            step("Update integrator config of selected flight supplierCode BOOKING endpoint to incorrect value");
            boolean mocked = flightApi.updateBookingIntegratorUrl("-MOCKFAILED", action, supplierCode);
            Assertion.assertThat("Integrator config-v2 URL updated to incorrect value", mocked, is(true));
            isIntegratorUrlUpdated = mocked;

            step("After inputting all pax details, continue to payment page");
            flightBookingFormPage.fillPassengerDetailsLogin(passengerName);
            flightBookingFormPage.enableV7BookingRequestCapture();
            flightBookingFormPage.goToPaymentPageFromBookingFormPage();
            flightBookingFormPage.clickItsCorrectOnReviewPassengerDataPopup();
            flightBookingFormPage.clickNoThanksOnFlightMayReschedulePopup();
            travelPlanPage.skipTravelPlan();

            step("Check booking failed");
            Assertion.assertThat("Booking failed and user remains on booking form",
                    flightBookingFormPage.isBookingFailed(), either(containsStringIgnoringCase("Still can't connect with the airline")).or(containsStringIgnoringCase("Belum berhasil terhubung dengan maskapai")));

            step("Revert integrator config selected flight supplierCode BOOKING endpoint to actual integrator URL");
            boolean reverted = flightApi.updateBookingIntegratorUrl("", action, supplierCode);
            Assertion.assertThat("Integrator config-v2 URL reverted", reverted, is(true));
            if (reverted) {
                isIntegratorUrlUpdated = false;
            }

            step("Retry continue to payment page");
            flightBookingFormPage.enableV7BookingRequestCapture();
            flightBookingFormPage.clickOnTryAgain();
            flightBookingFormPage.clickItsCorrectOnReviewPassengerDataPopup();
            flightBookingFormPage.clickNoThanksOnFlightMayReschedulePopup();
            travelPlanPage.skipTravelPlanIfPresent();

            step("Success redirect to payment page");
            Assertion.assertThat("Payment page is displayed", paymentPage.isPaymentPagepresent(), is(true));

            step("Check Booking payload and ensure interimId is passed in v7/booking under corporateDataRequest.corporateInterimId");
            Assertion.assertThat("corporateInterimId is passed in v7/booking",
                    flightBookingFormPage.getCapturedCorporateInterimId(), not(emptyOrNullString()));

            step("OrderId successfully created");
            String orderID = String.valueOf(getOrderId(paymentPage.getOrderId()));
            Assertion.assertThat("OrderID visible " + orderID, orderID, not(emptyOrNullString()));
        } finally {
            if (isIntegratorUrlUpdated) {
                step("Reverting the name back even if try fails");
                flightApi.updateBookingIntegratorUrl("", action, supplierCode);
            }
        }
    }

    @Author(names = {"Vanshika Agarwal"})
    @Module(name = "PRP")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void verifyRefundAmountBreakdownForAllFareCards() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyRefundAmountBreakdownForAllFareCards");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPageWeb = FlightLandingPageWeb.getInstance(session);
        FlightSearchResultPageWeb<?> flightSearchResultPage = FlightSearchResultPageWeb.getInstance(session);
        FlightProductReviewPage<?> flightProductReviewPage = new FlightProductReviewPage<>(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        String tripType = param.get("tripType");
        String originAirport = param.get("origin");
        String destinationAirport = param.get("destination");
        int daysToDepartureDate = ThreadLocalRandom.current().nextInt(150, 190);
        String departureDate = CoreFunctions.addToLongDate(CoreFunctions.getCurrentLongDate(), daysToDepartureDate);
        int adultPassengers = Integer.parseInt(param.get("adultPassengers"));
        int childPassengers = Integer.parseInt(param.get("childPassengers"));
        int infantPassengers = Integer.parseInt(param.get("infantPassengers"));
        String username = param.get("username");
        String password = param.get("password");
        String currency = param.get("currency");

        step("Login as " + username + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(username, password);

        step("Change Currency to { " + currency + " }");
        homePage.selectCurrency(currency);

        step("User open Flight vertical");
        homePage.openVerticalPageViaURL("Flights");

        step("Toggle trip type");
        flightLandingPageWeb.toggleTripType(tripType);

        step("Select origin airport");
        flightLandingPageWeb.selectOrigin(originAirport);

        step("Select destination airport");
        flightLandingPageWeb.selectDestination(destinationAirport);

        step("Select date");
        flightLandingPageWeb.selectDate(departureDate);

        step("add passenger");
        flightLandingPageWeb.addPassenger(adultPassengers, childPassengers, infantPassengers);

        step("search flight");
        flightLandingPageWeb.searchFlight();
        Assertion.assertThat("Search Result Page is loaded", flightSearchResultPage.isSearchResultPagePresent(), is(true));

        step("Select flight with price greater than 10mil");
        long selectedFlightPrice = flightSearchResultPage.selectFlightWithPriceGreaterThanTenMillion();
        Assertion.assertThat("Selected flight price should be greater than IDR 10,000,000", selectedFlightPrice, greaterThanOrEqualTo(10_000_000L));

        step("Verify PRP is loaded");
        Assertion.assertThat("Product Review Page is loaded", flightProductReviewPage.isPresent(), is(true));

        step("Verify refund percentage breakdown for all fare cards");
        flightProductReviewPage.verifyRefundPercentageBreakdownForAllFareCards();

    }

    @Author(names = {"Siddharth Sheoran"})
    @Module(name = "Reschedule")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void VerifyDobIsNotEditableOnBookingFormInRescheduleFlow() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("VerifyDobIsNotEditableOnBookingFormInRescheduleFlow");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPage = FlightLandingPageWeb.getInstance(session);
        FlightSearchResultPageWeb<?> searchResultPage = FlightSearchResultPageWeb.getInstance(session);
        FlightProductReviewPageWeb<?> productReviewPage = FlightProductReviewPageWeb.getInstance(session);
        FlightBookingFormPageWeb<?> bookingFormPage =  FlightBookingFormPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPageWeb=PaymentPageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        FlightApi<?> flightApi = new FlightApi<>(session);
        FlightReschedulePageWeb<?> flightReschedulePageWeb=FlightReschedulePageWeb.getInstance(session);

        String origin = param.get("origin");
        String destination = param.get("destination");
        int adultPassengers = Integer.parseInt(param.get("adultCount"));
        int childPassengers = Integer.parseInt(param.get("childCount"));
        int infantPassengers = Integer.parseInt(param.get("infantCount"));
        String username = param.get("username");
        String password = param.get("password");
        String airlineName = param.get("airlineName");
        String tripType = param.get("tripType");
        String paymentGroup = param.get("paymentGroup");
        String paymentMethod = param.get("paymentMethod");
        String rescheduleDestination = param.get("rescheduleDestination");
        int daysToDepartureDate = ThreadLocalRandom.current().nextInt(30, 60);

        String currentDate = CoreFunctions.getCurrentDateYYYY_MM_DD();

        String departureDate = CoreFunctions.addToDateYYYY_MM_DD(currentDate, daysToDepartureDate);
        String phoneNumber = param.get("phone");

        step("Find dynamic data");
        FlightDynamicData flightDynamicData = flightApi.findDate(
                username, password,
                origin, destination, daysToDepartureDate,
                adultPassengers, childPassengers, infantPassengers,
                "ONE_WAY", airlineName, "", "", "", ""
        );
        origin = flightDynamicData.originAirport;
        destination = flightDynamicData.destinationAirport;
        departureDate = flightDynamicData.departureDate;
        airlineName = flightDynamicData.airlineDepart;

        String[] passengerNames = {CoreFunctions.randomNameWithoutTest() + " Testing"};

        step("Create Booking Order via API");
        String orderId = flightApi.createOrderFlightB2CDomestic(
                username,
                password,
                origin,
                destination,
                departureDate,
                tripType,
                adultPassengers,
                childPassengers,
                infantPassengers,
                airlineName,
                passengerNames,
                phoneNumber
        );
        Assertion.assertThat("Verify order is created", orderId, not(""));

        step("User open Home Page");
        homePage.goToHomePage();

        step("Go to account Page");
        loginPage.goToAccountPage();

        step("User login");
        loginPage.loginWithSpecificAccountViaCookie(username, password);
        homePage.selectCurrency("IDR");

        step("Find the issued order");
        String orderStatus = orderListPage.getOrderStatus(orderId);
        orderListPage.openActiveTicketViaURL(orderId);
        orderDetailPage.skipCoachmark();

        step("Verify flight order details");
        Assertion.assertThat("orderID should be visible and should be same as from payment page", orderDetailPage.getOrderId(), is(orderId));

        Assertion.assertThat("Order status should be ETICKET_ISSUED or PROCESSING_ETICKET",
                orderStatus.equalsIgnoreCase("ETICKET_ISSUED") || orderStatus.equalsIgnoreCase("PROCESSING_ETICKET"), is(true));
        orderDetailPage.closeEticketPopUp();

        step("Go to Flight Reschedule page");
        orderDetailPage.clickRescheduleButton();

        step("Select all passengers applicable in the reschedulling option");
        flightReschedulePageWeb.selectAllPassengers();

        step("select new destination airport");
        flightReschedulePageWeb.changeDestinationAirport(rescheduleDestination);

        step("click on find new schedule button");
        flightReschedulePageWeb.pressFindNewScheduleButton();

        step("select flight departure");
        searchResultPage.selectFlight();

        step("User is redirected to Product Review Page");
        Assertion.assertThat("Product Review page is visible", productReviewPage.isPresent(), is(true));

        step("user select a flight");
        productReviewPage.clickSelectBtnRegular();

        step("Verify user is directed to Booking Form page");
        bookingFormPage.clickOkayGotIt();
        bookingFormPage.waitForBookingPageToLoad();
        Assertion.assertThat("Booking Form page is visible", bookingFormPage.isBookingFormVisible(), is(true));

        step("verify dob is not editable in the booking form page");
        Assertion.assertThat("Dob is not editable in booking form", flightReschedulePageWeb.clickDateOfBirth(), is(false));
    }
    @Author(names = {"Vanshika Agarwal"})
    @Module(name = "Booking")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void verifyPaymentNudgesFor100p() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyPaymentNudgesFor100p");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPageWeb = FlightLandingPageWeb.getInstance(session);
        FlightSearchResultPageWeb<?> flightSearchResultPage = FlightSearchResultPageWeb.getInstance(session);
        FlightProductReviewPageWeb<?> flightProductReviewPageWeb = FlightProductReviewPageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        FlightBookingFormPageWeb<?> flightBookingFormPage = FlightBookingFormPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPageWeb = PaymentPageWeb.getInstance(session);

        String tripType = param.get("tripType");
        String origin = param.get("origin");
        String destination = param.get("destination");
        int daysToDepartureDate = ThreadLocalRandom.current().nextInt(30, 60);
        int adultPassengers = Integer.parseInt(param.get("adultPassengers"));
        int childPassengers = Integer.parseInt(param.get("childPassengers"));
        int infantPassengers = Integer.parseInt(param.get("infantPassengers"));
        String username = param.get("username");
        String password = param.get("password");
        String currency = param.get("currency");
        String airlineName = param.get("airlineName");
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);

        step("Login as " + username + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(username, password);

        step("Change Currency to { " + currency + " }");
        homePage.selectCurrency(currency);

        step("User open Flight vertical");
        homePage.openVerticalPageViaURL("Flights");

        step("Toggle trip type");
        flightLandingPageWeb.toggleTripType(tripType);

        step("Select origin airport");
        flightLandingPageWeb.selectOrigin(origin);

        step("Select destination airport");
        flightLandingPageWeb.selectDestination(destination);

        step("Select date");
        flightLandingPageWeb.selectDate(departureDate);

        step("add passenger");
        flightLandingPageWeb.addPassenger(adultPassengers, childPassengers, infantPassengers);

        step("search flight");
        flightLandingPageWeb.searchFlight();
        Assertion.assertThat("Search Result Page is loaded", flightSearchResultPage.isSearchResultPagePresent(), is(true));

        step("Filter airline {" + airlineName + " }");
        flightSearchResultPage.clickTiket100PercentRefundQuickFilter();
        flightSearchResultPage.filterAirline(airlineName);

        step("Select flight for departure");
        flightSearchResultPage.selectFlightByAirline(airlineName);

        step("Verify PRP is loaded");
        Assertion.assertThat("Product Review Page is loaded", flightProductReviewPageWeb.isPresent(), is(true));

        step("User Select fare card on prp page");
        flightProductReviewPageWeb.clickSelect100pRefundFareCard();
        flightBookingFormPage.clickNoThanksOnFlightMayReschedulePopup();

        step("Click toggle to fill passengers details and save ");
        flightBookingFormPage.fillPassengerDetails();

        step("Go to payment page");
        flightBookingFormPage.goToPaymentPageFromBookingFormPage();
        flightBookingFormPage.clickItsCorrectOnReviewPassengerDataPopup();
        flightBookingFormPage.clickNoThanksOnFlightMayReschedulePopup();
        flightBookingFormPage.clickOkayGotIt();

        step("Payment page should be displayed");
        Assertion.assertThat("PaymentPageLoaded", paymentPageWeb.isPaymentPagepresent(), is(true));

        step("Verify Refund and Reschedule policies match between Booking and Payment page");
        Assertion.assertThat("Refund and reschedule nudge should be displayed on payment page", paymentPageWeb.getRefundRescheduleInfo(), containsStringIgnoringCase("Free refund"));
        paymentPageWeb.openRRPolicies();

        step("Verify that the Refund & Reschedule Info modal is successfully opened");
        paymentPageWeb.verifyRefundRescheduleModalOpened();

        step("Verify that a 100% refund is mentioned inside the policy box");
        Assertion.assertThat("100% refund policy should be displayed in Refund and Reschedule policies", paymentPageWeb.verifyRefund100PercentPresent(), anyOf(containsStringIgnoringCase("refund 100%"), containsStringIgnoringCase("100% refund")));
        paymentPageWeb.closeOrderDetails();
    }

    @Author(names = {"Vanshika Agarwal"})
    @Module(name = "Booking")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void verifyPaymentNudgesDateIsCorrect() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyPaymentNudgesDateIsCorrect");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPageWeb = FlightLandingPageWeb.getInstance(session);
        FlightSearchResultPageWeb<?> flightSearchResultPage = FlightSearchResultPageWeb.getInstance(session);
        FlightProductReviewPage<?> flightProductReviewPage = new FlightProductReviewPage<>(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        FlightBookingFormPageWeb<?> flightBookingFormPage = FlightBookingFormPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPageWeb = PaymentPageWeb.getInstance(session);


        String tripType = param.get("tripType");
        String origin = param.get("origin");
        String destination = param.get("destination");
        int daysToDepartureDate = ThreadLocalRandom.current().nextInt(30, 60);
        int adultPassengers = Integer.parseInt(param.get("adultPassengers"));
        int childPassengers = Integer.parseInt(param.get("childPassengers"));
        int infantPassengers = Integer.parseInt(param.get("infantPassengers"));
        String username = param.get("username");
        String password = param.get("password");
        String currency = param.get("currency");
        String airlineName = param.get("airlineName");
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);

        step("Login as " + username + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(username, password);

        step("Change Currency to { " + currency + " }");
        homePage.selectCurrency(currency);

        step("User open Flight vertical");
        homePage.openVerticalPageViaURL("Flights");

        step("Toggle trip type");
        flightLandingPageWeb.toggleTripType(tripType);

        step("Select origin airport");
        flightLandingPageWeb.selectOrigin(origin);

        step("Select destination airport");
        flightLandingPageWeb.selectDestination(destination);

        step("Select date");
        flightLandingPageWeb.selectDate(departureDate);

        step("add passenger");
        flightLandingPageWeb.addPassenger(adultPassengers, childPassengers, infantPassengers);

        step("search flight");
        flightLandingPageWeb.searchFlight();
        Assertion.assertThat("Search Result Page is loaded", flightSearchResultPage.isSearchResultPagePresent(), is(true));

        step("Filter airline {" + airlineName + " }");
        flightSearchResultPage.filterAirline(airlineName);

        step("Select flight for departure");
        flightSearchResultPage.selectFlightByAirline(airlineName);

        step("Verify PRP is loaded");
        Assertion.assertThat("Product Review Page is loaded", flightProductReviewPage.isPresent(), is(true));

        step("User Select fare card on prp page");
        flightProductReviewPage.clickSelectBtnRegular();
        flightBookingFormPage.clickNoThanksOnFlightMayReschedulePopup();

        step("Click toggle to fill passengers details and save ");
        flightBookingFormPage.fillPassengerDetails();

        step("Fetch the dates for refund and reschedule on booking page");
        flightBookingFormPage.openRefundAndRescheduleDetails();
        List<String> expectedRefundKeywords = flightBookingFormPage.refundDatesUnderPolicies();
        List<String> expectedRescheduleKeywords = flightBookingFormPage.rescheduleDatesUnderPolicies();
        flightBookingFormPage.closePopUp();

        step("Go to payment page");
        flightBookingFormPage.goToPaymentPageFromBookingFormPage();
        flightBookingFormPage.clickItsCorrectOnReviewPassengerDataPopup();
        flightBookingFormPage.clickNoThanksOnFlightMayReschedulePopup();
        flightBookingFormPage.clickOkayGotIt();

        step("Payment page should be displayed");
        Assertion.assertThat("PaymentPageLoaded", paymentPageWeb.isPaymentPagepresent(), is(true));
        paymentPageWeb.openRRPolicies();

        step("Verify that the Refund & Reschedule Info modal is successfully opened");
        paymentPageWeb.verifyRefundRescheduleModalOpened();

        step("Verify Refund and Reschedule policies match between Booking and Payment page");
        boolean refundMatch = paymentPageWeb.verifyRefundPolicyMatches(expectedRefundKeywords);
        boolean rescheduleMatch = paymentPageWeb.verifyReschedulePolicyMatches(expectedRescheduleKeywords);

        Assertion.assertThat("Refund percentages and amounts are present on Payment page", refundMatch, is(true));
        Assertion.assertThat("Reschedule percentages and amounts are present on Payment page", rescheduleMatch, is(true));
        paymentPageWeb.closeOrderDetails();
    }

    @Author(names = {"Nasha Zahira"})
    @Module(name = "SRP")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void verifySmartTripFarePrioritizedOverCheaperFareOnSrp() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifySmartTripFarePrioritizedOverCheaperFareOnSrp");
        BaseTest.paramsThreadLocal.set(param);

        FlightSearchResultPageWeb<?> flightSearchResultPage = FlightSearchResultPageWeb.getInstance(session);

        flightSearchResultPage.maximizeWindow();

        step("Start recording streaming API responses and clear captures from any previous test");
        flightSearchResultPage.enableFlightStreamingCapture();
        flightSearchResultPage.clearFlightStreamingCaptures();

        String searchUrl = flightSearchResultPage.buildFlightSearchUrl(param);
        step("Open flight SRP directly: " + searchUrl);
        flightSearchResultPage.openSRPFromUrl(searchUrl);

        step("Poll streaming responses every second until they stop arriving");
        flightSearchResultPage.waitForSrpAndCaptureStreaming(120);

        step("Wait until SRP finished loading");
        flightSearchResultPage.isSRPLoaded();

        step("Find a flightIdGeneral returned both with and without the SMART_TRIP tag whose schedule is shown on SRP");
        FlightSearchResultPageWeb.SmartTripFarePair pair = flightSearchResultPage.requireDisplayedSmartTripAndPlainFarePair();

        step("Read the last SRP card rendered for flightIdGeneral " + pair.flightIdGeneral());
        String displayedPrice = flightSearchResultPage.getDisplayedPriceForPair(pair);

        step("Verify the SRP card shows the Smart Trip fare");
        Assertion.assertThat("SRP card for flightIdGeneral " + pair.flightIdGeneral() + " should display the SMART_TRIP fare " + pair.smartTrip().mainPriceText() + " (rounding tolerance +/-" + FlightSearchResultPageWeb.PRICE_ROUNDING_TOLERANCE + ")", flightSearchResultPage.displayedPriceMatchesSmartTripFare(pair, displayedPrice), is(true));

    }


    @Author(names = {"Vanshika Agarwal"})
    @Module(name = "SRP")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void verifySRPShowsSearchResultForSEO() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifySRPShowsSearchResultForSEO");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        FlightSearchResultPageWeb<?> flightSearchResultPage = FlightSearchResultPageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        String username = param.get("username");
        String password = param.get("password");
        String currency = param.get("currency");
        String seoLandingUrl = param.get("url");

        step("Login as " + username + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(username, password);

        step("Change Currency to { " + currency + " }");
        homePage.selectCurrency(currency);

        step("User open Flight vertical");
        homePage.openVerticalPageViaURL("Flights");

        step("Navigate directly to Flight SEO route page: " + seoLandingUrl);
        flightSearchResultPage.openSRPFromUrl(seoLandingUrl);

        step("Verify price table opens");
        Assertion.assertThat("Price table opens", flightSearchResultPage.isPriceTableVisible(), is(true));

        step("Verify that none of the SEO cards have today's date");
        flightSearchResultPage.verifyNoSeoCardsHaveTodaysDate();

        step("Extract route and date details from the first SEO card");
        String expectedOrigin = flightSearchResultPage.getFirstSeoCardOrigin();
        String expectedDestination = flightSearchResultPage.getFirstSeoCardDestination();
        String expectedDate = flightSearchResultPage.getFirstSeoCardDate();

        step("Click 'Book now' on the first SEO card");
        flightSearchResultPage.clickBookNowOnFirstSeoCard();

        step("Verify Search Results Page (SRP) loads");
        flightSearchResultPage.isSRPLoaded();

        step("Verify route and date details on SRP search widget match the SEO card");
        flightSearchResultPage.verifySrpWidgetRouteAndDate(expectedOrigin, expectedDestination, expectedDate);
    }


    @Author(names = {"Vanshika Agarwal"})
    @Module(name = "Booking")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void validateUserWillNotGetPopUpInsuranceAfterFullProtectionInsurance () throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("validateUserWillNotGetPopUpInsuranceAfterFullProtectionInsurance");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPageWeb = FlightLandingPageWeb.getInstance(session);
        FlightSearchResultPageWeb<?> flightSearchResultPage = FlightSearchResultPageWeb.getInstance(session);
        FlightProductReviewPage<?> flightProductReviewPage = new FlightProductReviewPage<>(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        FlightBookingFormPageWeb<?> flightBookingFormPage = FlightBookingFormPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPageWeb = PaymentPageWeb.getInstance(session);


        String tripType = param.get("tripType");
        String origin = param.get("origin");
        String destination = param.get("destination");
        int daysToDepartureDate = ThreadLocalRandom.current().nextInt(30, 60);
        int adultPassengers = Integer.parseInt(param.get("adultPassengers"));
        int childPassengers = Integer.parseInt(param.get("childPassengers"));
        int infantPassengers = Integer.parseInt(param.get("infantPassengers"));
        String username = param.get("username");
        String password = param.get("password");
        String currency = param.get("currency");
        String airlineName = param.get("airlineName");
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);

        step("Login as " + username + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(username, password);

        step("Change Currency to { " + currency + " }");
        homePage.selectCurrency(currency);

        step("User open Flight vertical");
        homePage.openVerticalPageViaURL("Flights");

        step("Toggle trip type");
        flightLandingPageWeb.toggleTripType(tripType);

        step("Select origin airport");
        flightLandingPageWeb.selectOrigin(origin);

        step("Select destination airport");
        flightLandingPageWeb.selectDestination(destination);

        step("Select date");
        flightLandingPageWeb.selectDate(departureDate);

        step("add passenger");
        flightLandingPageWeb.addPassenger(adultPassengers, childPassengers, infantPassengers);

        step("search flight");
        flightLandingPageWeb.searchFlight();
        Assertion.assertThat("Search Result Page is loaded", flightSearchResultPage.isSearchResultPagePresent(), is(true));

        step("Filter airline {" + airlineName + " }");
        flightSearchResultPage.filterAirline(airlineName);

        step("Select flight for departure");
        flightSearchResultPage.selectFlightByAirline(airlineName);

        step("Verify PRP is loaded");
        Assertion.assertThat("Product Review Page is loaded", flightProductReviewPage.isPresent(), is(true));

        step("User Select fare card on prp page");
        flightProductReviewPage.clickSelectBtnRegular();
        flightBookingFormPage.clickNoThanksOnFlightMayReschedulePopup();

        step("Click toggle to fill passengers details and save ");
        flightBookingFormPage.fillPassengerDetails();

        step("Check full insurance protection and verify checkbox state");
        flightBookingFormPage.ensureOnlyFullProtectionIsSelected();

        step("Assert insurance is present in the price detail box");
        flightBookingFormPage.verifyFullProtectionInPriceBreakdown();

        step("Go to payment page");
        flightBookingFormPage.goToPaymentPageFromBookingFormPage();
        flightBookingFormPage.clickItsCorrectOnReviewPassengerDataPopup();
        flightBookingFormPage.checkIfNoThanksPopUpCame();

        step("Payment page should be displayed without any insurance pop up in between");
        Assertion.assertThat("Payment Page Loaded without any insurance popup", paymentPageWeb.isPaymentPagepresent(), is(true));
    }

    @Author(names = {"Nasha Zahira"})
    @Module(name = "SEO")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void verifyFlightAirlineSeoPagePriceMatchesSrp() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyFlightAirlineSeoPagePriceMatchesSrp");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        FlightSearchResultPageWeb<?> flightSearchResultPage = FlightSearchResultPageWeb.getInstance(session);

        String currency = param.get("currency");
        String seoUrl = param.get("url");

        step("User open home page");
        homePage.goToHomePage();

        step("Change currency to { " + currency + " }");
        homePage.selectCurrency(currency);

        step("Navigate to the airline SEO page: " + seoUrl);
        flightSearchResultPage.openSRPFromUrl(seoUrl);

        step("Verify the SEO page loaded with its price table");
        Assertion.assertThat("Price table is shown on the airline SEO page", flightSearchResultPage.isPriceTableVisible(), is(true));
        Assertion.assertThat("SEO price cards are available to book", flightSearchResultPage.getSeoPriceCardCount(), greaterThan(0));

        step("Read the price of the first SEO card");
        long seoCardPrice = flightSearchResultPage.getFirstSeoCardPrice();

        step("Click 'Book now' on the first SEO card");
        flightSearchResultPage.clickBookNowOnFirstSeoCard();

        step("Verify Search Results Page (SRP) loads");
        flightSearchResultPage.isSRPLoaded();

        step("Sort the SRP by lowest price");
        flightSearchResultPage.openSortFilter();
        flightSearchResultPage.selectSortByLowestPrice();

        step("Verify the cheapest SRP flight price matches the SEO card price");
        Assertion.assertThat("Cheapest SRP flight price matches the price shown on the airline SEO card (IDR " + seoCardPrice + ")", flightSearchResultPage.getFirstStandardSectionFlightPrice(), is(seoCardPrice));
    }

    @Author(names = {"Nasha Zahira"})
    @Module(name = "SEO")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void verifyFlightPopularDestinationSeoPagePriceMatchesSrp() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyFlightPopularDestinationSeoPagePriceMatchesSrp");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        FlightSearchResultPageWeb<?> flightSearchResultPage = FlightSearchResultPageWeb.getInstance(session);

        String currency = param.get("currency");
        String seoUrl = param.get("url");

        step("User open home page");
        homePage.goToHomePage();

        step("Change currency to { " + currency + " }");
        homePage.selectCurrency(currency);

        step("Navigate to the popular destination SEO page: " + seoUrl);
        flightSearchResultPage.openSRPFromUrl(seoUrl);

        step("Verify the SEO page loaded with its price table");
        Assertion.assertThat("Price table is shown on the popular destination SEO page", flightSearchResultPage.isPriceTableVisible(), is(true));
        Assertion.assertThat("SEO price cards are available to book", flightSearchResultPage.getSeoPriceCardCount(), greaterThan(0));

        step("Read the airline and price of the first SEO card");
        String seoCardAirline = flightSearchResultPage.getFirstSeoCardAirline();
        long seoCardPrice = flightSearchResultPage.getFirstSeoCardPrice();

        step("Click 'Book now' on the first SEO card");
        flightSearchResultPage.clickBookNowOnFirstSeoCard();

        step("Verify Search Results Page (SRP) loads");
        flightSearchResultPage.isSRPLoaded();

        step("Filter the SRP by airline { " + seoCardAirline + " }");
        flightSearchResultPage.filterAirline(seoCardAirline);

        step("Sort the SRP by lowest price");
        flightSearchResultPage.openSortFilter();
        flightSearchResultPage.selectSortByLowestPrice();

        step("Verify the cheapest SRP flight price matches the SEO card price");
        Assertion.assertThat("Cheapest SRP flight price matches the price shown on the popular destination SEO card (IDR " + seoCardPrice + ")", flightSearchResultPage.getFirstStandardSectionFlightPrice(), is(seoCardPrice));
    }

    @Author(names = {"Nasha Zahira"})
    @Module(name = "SEO")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void verifyFlightPopularRouteSeoPagePriceMatchesSrp() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyFlightPopularRouteSeoPagePriceMatchesSrp");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        FlightSearchResultPageWeb<?> flightSearchResultPage = FlightSearchResultPageWeb.getInstance(session);

        String currency = param.get("currency");
        String seoUrl = param.get("url");

        step("User open home page");
        homePage.goToHomePage();

        step("Change currency to { " + currency + " }");
        homePage.selectCurrency(currency);

        step("Navigate to the popular route SEO page: " + seoUrl);
        flightSearchResultPage.openSRPFromUrl(seoUrl);

        step("Verify the SEO page loaded with its price table");
        Assertion.assertThat("Price table is shown on the popular route SEO page", flightSearchResultPage.isPriceTableVisible(), is(true));
        Assertion.assertThat("SEO price cards are available to book", flightSearchResultPage.getSeoPriceCardCount(), greaterThan(0));

        step("Read the airline and price of the first SEO card");
        String seoCardAirline = flightSearchResultPage.getFirstSeoCardAirline();
        long seoCardPrice = flightSearchResultPage.getFirstSeoCardPrice();

        step("Click 'Book now' on the first SEO card");
        flightSearchResultPage.clickBookNowOnFirstSeoCard();

        step("Verify Search Results Page (SRP) loads");
        flightSearchResultPage.isSRPLoaded();

        step("Filter the SRP by airline { " + seoCardAirline + " }");
        flightSearchResultPage.filterAirline(seoCardAirline);

        step("Sort the SRP by lowest price");
        flightSearchResultPage.openSortFilter();
        flightSearchResultPage.selectSortByLowestPrice();

        step("Verify the cheapest SRP flight price matches the SEO card price");
        Assertion.assertThat("Cheapest SRP flight price matches the price shown on the popular route SEO card (IDR " + seoCardPrice + ")", flightSearchResultPage.getFirstStandardSectionFlightPrice(), is(seoCardPrice));
    }

}
