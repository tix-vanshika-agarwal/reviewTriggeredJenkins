package com.tiket.analytics.app.test.flight;

import com.tiket.analytics.BaseAnalyticsTest;
import com.tiket.analytics.assertions.BaseAssertions;
import com.tiket.analytics.app.home.AnalyticsHomePage;
import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Session;
import com.tiket.page.flight.FlightPage;
import com.tiket.page.flight.bookingform.FlightBookingFormPage;
import com.tiket.page.flight.prp.FlightProductReviewPage;
import com.tiket.page.flight.searchpage.FlightSearchPage;
import com.tiket.page.flight.searchresultpage.FlightSearchResultPage;
import com.tiket.page.home.HomePage;
import com.tiket.page.login.LoginPage;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.testbase.Configs;
import org.hamcrest.Matchers;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import java.util.Map;

import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.core.Is.is;

public class FlightPageTest extends BaseAnalyticsTest {

    @Author(names = {"Okta"})
    @Module(name = "FlightMainPage")
    @Tribe(name = "Flight")
    @Vertical(name = "Core")
    @Test
    public void testFlightMainPageVisitGuest() {
        Map<String, String> testProperties = getParam("testFlightMainPageVisitGuest");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String event = testProperties.get("event");
        String eventAction = testProperties.get("eventAction");
        String screenName = testProperties.get("screenName");
        String utmPage = testProperties.get("utmPage");
        String isFirstPage = testProperties.get("isFirstPage");
        String vertical = testProperties.get("vertical");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Open app as guest");
        loginPage.loginAsGuest();

        step("Open flights vertical");
        homePage.openVerticalPage("Flights");

        sleep(40);

        BaseAssertions flightPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("pageVisit event is found", flightPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, screenName), is(1));
        Assertion.assertThat("eventAction is correct", flightPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), is(true));
        Assertion.assertThat("screenName is correct", flightPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), is(true));
        Assertion.assertThat("utmPage is correct", flightPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "utmPage", utmPage), is(true));
        Assertion.assertThat("isFirstPage is correct", flightPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "isFirstPage", isFirstPage), is(true));
        Assertion.assertThat("vertical is correct", flightPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "vertical", vertical), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "FlightPDP")
    @Tribe(name = "Flight")
    @Vertical(name = "Core")
    @Test
    public void testFlightPDPPageVisitGuest() {
        Map<String, String> testProperties = getParam("testFlightPDPPageVisitGuest");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String tripType = testProperties.get("tripType");
        String originAirport = testProperties.get("originAirport");
        String destinationAirport = testProperties.get("destinationAirport");
        int daysToDepartureDate = Integer.parseInt(testProperties.get("daysToDepartureDate"));

        String event = testProperties.get("event");
        String eventAction = testProperties.get("eventAction");
        String screenName = testProperties.get("screenName");
        String utmPage = testProperties.get("utmPage");
        String isFirstPage = testProperties.get("isFirstPage");
        String vertical = testProperties.get("vertical");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightPage<WebDriver> flightPage = FlightPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Find dynamic data");
        flightPage.setDataOneWay(daysToDepartureDate, "");
        String departureDate = CoreFunctions.convertToLongDate(flightPage.getDate("departureDate"));

        step("Open app as guest");
        loginPage.loginAsGuest();

        step("Open flights page");
        homePage.openVerticalPage("Flights");
        flightSearchPage.closePopup();

        step("Toggle trip type");
        flightSearchPage.toggleTriptype(tripType);

        step("Select origin airport");
        flightSearchPage.selectOrigin(originAirport);
        flightSearchPage.closePopup();

        step("Select destination airport");
        flightSearchPage.selectDestination(destinationAirport);

        step("Select date");
        flightSearchPage.selectDate(departureDate);

        step("Search flight");
        flightSearchPage.searchFlight();

        step("Click flight detail chevron");
        flightSearchResultPage.selectFlight();

        sleep(40);

        BaseAssertions flightPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("pageVisit event is found", flightPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, screenName), is(1));
        Assertion.assertThat("eventAction is correct", flightPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), is(true));
        Assertion.assertThat("screenName is correct", flightPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), is(true));
        Assertion.assertThat("utmPage is correct", flightPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "utmPage", utmPage), is(true));
        Assertion.assertThat("isFirstPage is correct", flightPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "isFirstPage", isFirstPage), is(true));
        Assertion.assertThat("vertical is correct", flightPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "vertical", vertical), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "FlightBookingPage")
    @Tribe(name = "Flight")
    @Vertical(name = "Core")
    @Test
    public void testFlightBookingPageVisitGuest() {
        Map<String, String> testProperties = getParam("testFlightBookingPageVisitGuest");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String tripType = testProperties.get("tripType");
        String originAirport = testProperties.get("originAirport");
        String destinationAirport = testProperties.get("destinationAirport");
        int daysToDepartureDate = Integer.parseInt(testProperties.get("daysToDepartureDate"));

        String event = testProperties.get("event");
        String eventAction = testProperties.get("eventAction");
        String screenName = testProperties.get("screenName");
        String utmPage = testProperties.get("utmPage");
        String isFirstPage = testProperties.get("isFirstPage");
        String vertical = testProperties.get("vertical");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightPage<WebDriver> flightPage = FlightPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightProductReviewPage<WebDriver> flightProductReviewPage = FlightProductReviewPage.getInstance(session);

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Find dynamic data");
        flightPage.setDataOneWay(daysToDepartureDate, "");
        String departureDate = CoreFunctions.convertToLongDate(flightPage.getDate("departureDate"));

        step("Open app as guest");
        loginPage.loginAsGuest();

        step("Open flights page");
        homePage.openVerticalPage("Flights");
        flightSearchPage.closePopup();

        step("Toggle trip type");
        flightSearchPage.toggleTriptype(tripType);

        step("Select origin airport");
        flightSearchPage.selectOrigin(originAirport);
        flightSearchPage.closePopup();

        step("Select destination airport");
        flightSearchPage.selectDestination(destinationAirport);

        step("Select date");
        flightSearchPage.selectDate(departureDate);

        step("Search flight");
        flightSearchPage.searchFlight();

        step("Click flight detail chevron");
        flightSearchResultPage.selectFlight();

        step("Continue to booking form");
        flightProductReviewPage.continueToBookingForm(tripType);

        sleep(40);

        BaseAssertions flightPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("pageVisit event is found", flightPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, screenName), is(1));
        Assertion.assertThat("eventAction is correct", flightPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), is(true));
        Assertion.assertThat("screenName is correct", flightPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), is(true));
        Assertion.assertThat("utmPage is correct", flightPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "utmPage", utmPage), is(true));
        Assertion.assertThat("isFirstPage is correct", flightPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "isFirstPage", isFirstPage), is(true));
        Assertion.assertThat("vertical is correct", flightPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "vertical", vertical), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "FlightSRP")
    @Tribe(name = "Flight")
    @Vertical(name = "Core")
    @Test
    public void testFlightSRPPageVisitGuest() {
        Map<String, String> testProperties = getParam("testFlightSRPPageVisitGuest");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String tripType = testProperties.get("tripType");
        String originAirport = testProperties.get("originAirport");
        String destinationAirport = testProperties.get("destinationAirport");
        int daysToDepartureDate = Integer.parseInt(testProperties.get("daysToDepartureDate"));

        String event = testProperties.get("event");
        String eventAction = testProperties.get("eventAction");
        String screenName = testProperties.get("screenName");
        String utmPage = testProperties.get("utmPage");
        String isFirstPage = testProperties.get("isFirstPage");
        String vertical = testProperties.get("vertical");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightPage<WebDriver> flightPage = FlightPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Find dynamic data");
        flightPage.setDataOneWay(daysToDepartureDate, "");
        String departureDate = CoreFunctions.convertToLongDate(flightPage.getDate("departureDate"));

        step("Open app as guest");
        loginPage.loginAsGuest();

        step("Open flights page");
        homePage.openVerticalPage("Flights");
        flightSearchPage.closePopup();

        step("Toggle trip type");
        flightSearchPage.toggleTriptype(tripType);

        step("Select origin airport");
        flightSearchPage.selectOrigin(originAirport);
        flightSearchPage.closePopup();

        step("Select destination airport");
        flightSearchPage.selectDestination(destinationAirport);

        step("Select date");
        flightSearchPage.selectDate(departureDate);

        step("Search flight");
        flightSearchPage.searchFlight();

        sleep(40);

        BaseAssertions flightPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("pageVisit event is found", flightPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, screenName), is(1));
        Assertion.assertThat("eventAction is correct", flightPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), is(true));
        Assertion.assertThat("screenName is correct", flightPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), is(true));
        Assertion.assertThat("utmPage is correct", flightPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "utmPage", utmPage), is(true));
        Assertion.assertThat("isFirstPage is correct", flightPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "isFirstPage", isFirstPage), is(true));
        Assertion.assertThat("vertical is correct", flightPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "vertical", vertical), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "FlightMainPage")
    @Tribe(name = "Flight")
    @Vertical(name = "Core")
    @Test
    public void testFlightEnterVerticalGuest() {
        Map<String, String> testProperties = getParam("testFlightEnterVerticalGuest");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String event = testProperties.get("event");
        String screenName = testProperties.get("screenName");
        String vertical = testProperties.get("vertical");
        String eventAction = testProperties.get("eventAction");
        String eventLabel = testProperties.get("eventLabel");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Open app as guest");
        loginPage.loginAsGuest();

        step("Open flights vertical");
        homePage.openVerticalPage("Flights");

        sleep(40);

        BaseAssertions flightPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("enterVertical event is found", flightPageAssertion.verifyEvent(Configs.MESSAGES, event), is(1));
        Assertion.assertThat("screenName is correct", flightPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "screenName", screenName), is(true));
        Assertion.assertThat("vertical is correct", flightPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "vertical", vertical), is(true));
        Assertion.assertThat("eventAction is correct", flightPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "eventAction", eventAction), is(true));
        Assertion.assertThat("eventLabel is correct", flightPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "eventLabel", eventLabel), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "FlightSRP")
    @Tribe(name = "Flight")
    @Vertical(name = "Core")
    @Test
    public void testFlightSearchLoadedGuest() {
        Map<String, String> testProperties = getParam("testFlightSearchLoadedGuest");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String tripType = testProperties.get("tripType");
        String originAirport = testProperties.get("originAirport");
        String destinationAirport = testProperties.get("destinationAirport");
        int daysToDepartureDate = Integer.parseInt(testProperties.get("daysToDepartureDate"));

        String event = testProperties.get("event");
        String screenName = testProperties.get("screenName");
        String vertical = testProperties.get("vertical");
        String eventAction = testProperties.get("eventAction");
        String eventLabel = testProperties.get("eventLabel");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightPage<WebDriver> flightPage = FlightPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Find dynamic data");
        flightPage.setDataOneWay(daysToDepartureDate, "");
        String departureDate = CoreFunctions.convertToLongDate(flightPage.getDate("departureDate"));

        step("Open app as guest");
        loginPage.loginAsGuest();

        step("Open flights page");
        homePage.openVerticalPage("Flights");
        flightSearchPage.closePopup();

        step("Toggle trip type");
        flightSearchPage.toggleTriptype(tripType);

        step("Select origin airport");
        flightSearchPage.selectOrigin(originAirport);
        flightSearchPage.closePopup();

        step("Select destination airport");
        flightSearchPage.selectDestination(destinationAirport);

        step("Select date");
        flightSearchPage.selectDate(departureDate);

        step("Search flight");
        flightSearchPage.searchFlight();

        sleep(40);

        BaseAssertions flightPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("searchLoaded event is found", flightPageAssertion.verifyEvent(Configs.MESSAGES, event), is(1));
        Assertion.assertThat("screenName is correct", flightPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "screenName", screenName), is(true));
        Assertion.assertThat("vertical is correct", flightPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "vertical", vertical), is(true));
        Assertion.assertThat("eventAction is correct", flightPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "eventAction", eventAction), is(true));
        Assertion.assertThat("eventLabel is correct", flightPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "eventLabel", eventLabel), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "FlightSRP")
    @Tribe(name = "Flight")
    @Vertical(name = "Core")
    @Test
    public void testFlightSearchProductGuest() {
        Map<String, String> testProperties = getParam("testFlightSearchProductGuest");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String tripType = testProperties.get("tripType");
        String originAirport = testProperties.get("originAirport");
        String destinationAirport = testProperties.get("destinationAirport");
        int daysToDepartureDate = Integer.parseInt(testProperties.get("daysToDepartureDate"));

        String event = testProperties.get("event");
        String screenName = testProperties.get("screenName");
        String vertical = testProperties.get("vertical");
        String eventAction = testProperties.get("eventAction");
        String eventLabel = testProperties.get("eventLabel");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightPage<WebDriver> flightPage = FlightPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Find dynamic data");
        flightPage.setDataOneWay(daysToDepartureDate, "");
        String departureDate = CoreFunctions.convertToLongDate(flightPage.getDate("departureDate"));

        step("Open app as guest");
        loginPage.loginAsGuest();

        step("Open flights page");
        homePage.openVerticalPage("Flights");
        flightSearchPage.closePopup();

        step("Toggle trip type");
        flightSearchPage.toggleTriptype(tripType);

        step("Select origin airport");
        flightSearchPage.selectOrigin(originAirport);
        flightSearchPage.closePopup();

        step("Select destination airport");
        flightSearchPage.selectDestination(destinationAirport);

        step("Select date");
        flightSearchPage.selectDate(departureDate);

        step("Search flight");
        flightSearchPage.searchFlight();

        sleep(40);

        BaseAssertions flightPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("searchProduct event is found", flightPageAssertion.verifyEvent(Configs.MESSAGES, event), is(1));
        Assertion.assertThat("screenName is correct", flightPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "screenName", screenName), is(true));
        Assertion.assertThat("vertical is correct", flightPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "vertical", vertical), is(true));
        Assertion.assertThat("eventAction is correct", flightPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "eventAction", eventAction), is(true));
        Assertion.assertThat("eventLabel is correct", flightPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "eventLabel", eventLabel), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "FlightBookingPage")
    @Tribe(name = "Flight")
    @Vertical(name = "Core")
    @Test
    public void testFlightBookProductGuest() {
        Map<String, String> testProperties = getParam("testFlightBookProductGuest");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String tripType = testProperties.get("tripType");
        String originAirport = testProperties.get("originAirport");
        String destinationAirport = testProperties.get("destinationAirport");
        int daysToDepartureDate = Integer.parseInt(testProperties.get("daysToDepartureDate"));
        int adultPassengers = Integer.parseInt(testProperties.get("adult"));
        int childPassengers = Integer.parseInt(testProperties.get("child"));
        int infantPassengers = Integer.parseInt(testProperties.get("infant"));
        String email = testProperties.get("email");
        String phoneNumber = testProperties.get("phoneNumber");

        String event = testProperties.get("event");
        String screenName = testProperties.get("screenName");
        String vertical = testProperties.get("vertical");
        String eventAction = testProperties.get("eventAction");
        String eventDescription = testProperties.get("eventDescription");
        String eventLabel = testProperties.get("eventLabel");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightPage<WebDriver> flightPage = FlightPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightProductReviewPage<WebDriver> flightProductReviewPage = FlightProductReviewPage.getInstance(session);
        FlightBookingFormPage<WebDriver> flightBookingFormPage = FlightBookingFormPage.getInstance(session);

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Find dynamic data");
        flightPage.setDataOneWay(daysToDepartureDate, "");
        String departureDate = CoreFunctions.convertToLongDate(flightPage.getDate("departureDate"));

        step("Open app as guest");
        loginPage.loginAsGuest();

        step("Open flights page");
        homePage.openVerticalPage("Flights");
        flightSearchPage.closePopup();

        step("Toggle trip type");
        flightSearchPage.toggleTriptype(tripType);

        step("Select origin airport");
        flightSearchPage.selectOrigin(originAirport);
        flightSearchPage.closePopup();

        step("Select destination airport");
        flightSearchPage.selectDestination(destinationAirport);

        step("Select date");
        flightSearchPage.selectDate(departureDate);

        step("Search flight");
        flightSearchPage.searchFlight();

        step("Click flight detail chevron");
        flightSearchResultPage.selectFlight();

        step("Continue to booking form");
        flightProductReviewPage.continueToBookingForm(tripType);

        step("Fill contact detail");
        flightBookingFormPage.fillContactDetail(phoneNumber, email, "guest", "");

        step("Fill passenger detail");
        flightBookingFormPage.fillPassengerDetail(adultPassengers, childPassengers, infantPassengers, "domestic", "");

        step("Continue to second half booking form");
        flightBookingFormPage.continueToSecondHalf();

        step("Continue to payment");
        flightBookingFormPage.continueToPayment();

        sleep(40);

        BaseAssertions flightPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("bookProduct event is found", flightPageAssertion.verifyEvent(Configs.MESSAGES, event), greaterThan(0));
        Assertion.assertThat("screenName is correct", flightPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "screenName", screenName), is(true));
        Assertion.assertThat("vertical is correct", flightPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "vertical", vertical), is(true));
        Assertion.assertThat("eventAction is correct", flightPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "eventAction", eventAction), is(true));
        Assertion.assertThat("eventDescription is correct", flightPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "eventDescription", eventDescription), is(true));
        Assertion.assertThat("eventLabel is correct", flightPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "eventLabel", eventLabel), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "FlightBookingPage")
    @Tribe(name = "Flight")
    @Vertical(name = "Core")
    @Test
    public void testFlightBookingProductGuest() {
        Map<String, String> testProperties = getParam("testFlightBookingProductGuest");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String tripType = testProperties.get("tripType");
        String originAirport = testProperties.get("originAirport");
        String destinationAirport = testProperties.get("destinationAirport");
        int daysToDepartureDate = Integer.parseInt(testProperties.get("daysToDepartureDate"));

        String event = testProperties.get("event");
        String screenName = testProperties.get("screenName");
        String vertical = testProperties.get("vertical");
        String eventAction = testProperties.get("eventAction");
        String eventLabel = testProperties.get("eventLabel");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightPage<WebDriver> flightPage = FlightPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightProductReviewPage<WebDriver> flightProductReviewPage = FlightProductReviewPage.getInstance(session);

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Find dynamic data");
        flightPage.setDataOneWay(daysToDepartureDate, "");
        String departureDate = CoreFunctions.convertToLongDate(flightPage.getDate("departureDate"));

        step("Open app as guest");
        loginPage.loginAsGuest();

        step("Open flights page");
        homePage.openVerticalPage("Flights");
        flightSearchPage.closePopup();

        step("Toggle trip type");
        flightSearchPage.toggleTriptype(tripType);

        step("Select origin airport");
        flightSearchPage.selectOrigin(originAirport);
        flightSearchPage.closePopup();

        step("Select destination airport");
        flightSearchPage.selectDestination(destinationAirport);

        step("Select date");
        flightSearchPage.selectDate(departureDate);

        step("Search flight");
        flightSearchPage.searchFlight();

        step("Click flight detail chevron");
        flightSearchResultPage.selectFlight();

        step("Continue to booking form");
        flightProductReviewPage.continueToBookingForm(tripType);

        sleep(40);

        BaseAssertions flightPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("bookingProduct event is found", flightPageAssertion.verifyEvent(Configs.MESSAGES, event), is(1));
        Assertion.assertThat("screenName is correct", flightPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "screenName", screenName), is(true));
        Assertion.assertThat("vertical is correct", flightPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "vertical", vertical), is(true));
        Assertion.assertThat("eventAction is correct", flightPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "eventAction", eventAction), is(true));
        Assertion.assertThat("eventLabel is correct", flightPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "eventLabel", eventLabel), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "FlightMainPage")
    @Tribe(name = "Flight")
    @Vertical(name = "Core")
    @Test
    public void testFlightMainPageVisitLogin() {
        Map<String, String> testProperties = getParam("testFlightMainPageVisitLogin");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String loginType = testProperties.get("loginType");
        String email = testProperties.get("email");
        String password = testProperties.get("password");

        String event = testProperties.get("event");
        String eventAction = testProperties.get("eventAction");
        String screenName = testProperties.get("screenName");
        String utmPage = testProperties.get("utmPage");
        String isFirstPage = testProperties.get("isFirstPage");
        String vertical = testProperties.get("vertical");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Open app as login user");
        loginPage.loginWith(loginType, email, password);

        step("Open flights vertical");
        homePage.openVerticalPage("Flights");

        sleep(40);

        BaseAssertions flightPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("pageVisit event is found", flightPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, screenName), is(1));
        Assertion.assertThat("eventAction is correct", flightPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), is(true));
        Assertion.assertThat("screenName is correct", flightPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), is(true));
        Assertion.assertThat("utmPage is correct", flightPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "utmPage", utmPage), is(true));
        Assertion.assertThat("isFirstPage is correct", flightPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "isFirstPage", isFirstPage), is(true));
        Assertion.assertThat("vertical is correct", flightPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "vertical", vertical), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "FlightPDP")
    @Tribe(name = "Flight")
    @Vertical(name = "Core")
    @Test
    public void testFlightPDPPageVisitLogin() {
        Map<String, String> testProperties = getParam("testFlightPDPPageVisitLogin");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String loginType = testProperties.get("loginType");
        String email = testProperties.get("email");
        String password = testProperties.get("password");

        String tripType = testProperties.get("tripType");
        String originAirport = testProperties.get("originAirport");
        String destinationAirport = testProperties.get("destinationAirport");
        int daysToDepartureDate = Integer.parseInt(testProperties.get("daysToDepartureDate"));

        String event = testProperties.get("event");
        String eventAction = testProperties.get("eventAction");
        String screenName = testProperties.get("screenName");
        String utmPage = testProperties.get("utmPage");
        String isFirstPage = testProperties.get("isFirstPage");
        String vertical = testProperties.get("vertical");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightPage<WebDriver> flightPage = FlightPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Find dynamic data");
        flightPage.setDataOneWay(daysToDepartureDate, "");
        String departureDate = CoreFunctions.convertToLongDate(flightPage.getDate("departureDate"));

        step("Open app as login user");
        loginPage.loginWith(loginType, email, password);

        step("Open flights page");
        homePage.openVerticalPage("Flights");
        flightSearchPage.closePopup();

        step("Toggle trip type");
        flightSearchPage.toggleTriptype(tripType);

        step("Select origin airport");
        flightSearchPage.selectOrigin(originAirport);
        flightSearchPage.closePopup();

        step("Select destination airport");
        flightSearchPage.selectDestination(destinationAirport);

        step("Select date");
        flightSearchPage.selectDate(departureDate);

        step("Search flight");
        flightSearchPage.searchFlight();

        step("Click flight detail chevron");
        flightSearchResultPage.selectFlight();

        sleep(40);

        BaseAssertions flightPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("pageVisit event is found", flightPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, screenName), is(1));
        Assertion.assertThat("eventAction is correct", flightPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), is(true));
        Assertion.assertThat("screenName is correct", flightPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), is(true));
        Assertion.assertThat("utmPage is correct", flightPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "utmPage", utmPage), is(true));
        Assertion.assertThat("isFirstPage is correct", flightPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "isFirstPage", isFirstPage), is(true));
        Assertion.assertThat("vertical is correct", flightPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "vertical", vertical), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "FlightBookingPage")
    @Tribe(name = "Flight")
    @Vertical(name = "Core")
    @Test
    public void testFlightBookingPageVisitLogin() {
        Map<String, String> testProperties = getParam("testFlightBookingPageVisitLogin");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String loginType = testProperties.get("loginType");
        String email = testProperties.get("email");
        String password = testProperties.get("password");

        String tripType = testProperties.get("tripType");
        String originAirport = testProperties.get("originAirport");
        String destinationAirport = testProperties.get("destinationAirport");
        int daysToDepartureDate = Integer.parseInt(testProperties.get("daysToDepartureDate"));

        String event = testProperties.get("event");
        String eventAction = testProperties.get("eventAction");
        String screenName = testProperties.get("screenName");
        String utmPage = testProperties.get("utmPage");
        String isFirstPage = testProperties.get("isFirstPage");
        String vertical = testProperties.get("vertical");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightPage<WebDriver> flightPage = FlightPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightProductReviewPage<WebDriver> flightProductReviewPage = FlightProductReviewPage.getInstance(session);

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Find dynamic data");
        flightPage.setDataOneWay(daysToDepartureDate, "");
        String departureDate = CoreFunctions.convertToLongDate(flightPage.getDate("departureDate"));

        step("Open app as login user");
        loginPage.loginWith(loginType, email, password);

        step("Open flights page");
        homePage.openVerticalPage("Flights");
        flightSearchPage.closePopup();

        step("Toggle trip type");
        flightSearchPage.toggleTriptype(tripType);

        step("Select origin airport");
        flightSearchPage.selectOrigin(originAirport);
        flightSearchPage.closePopup();

        step("Select destination airport");
        flightSearchPage.selectDestination(destinationAirport);

        step("Select date");
        flightSearchPage.selectDate(departureDate);

        step("Search flight");
        flightSearchPage.searchFlight();

        step("Click flight detail chevron");
        flightSearchResultPage.selectFlight();

        step("Continue to booking form");
        flightProductReviewPage.continueToBookingForm(tripType);

        sleep(40);

        BaseAssertions flightPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("pageVisit event is found", flightPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, screenName), is(1));
        Assertion.assertThat("eventAction is correct", flightPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), is(true));
        Assertion.assertThat("screenName is correct", flightPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), is(true));
        Assertion.assertThat("utmPage is correct", flightPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "utmPage", utmPage), is(true));
        Assertion.assertThat("isFirstPage is correct", flightPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "isFirstPage", isFirstPage), is(true));
        Assertion.assertThat("vertical is correct", flightPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "vertical", vertical), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "FlightSRP")
    @Tribe(name = "Flight")
    @Vertical(name = "Core")
    @Test
    public void testFlightSRPPageVisitLogin() {
        Map<String, String> testProperties = getParam("testFlightSRPPageVisitLogin");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String loginType = testProperties.get("loginType");
        String email = testProperties.get("email");
        String password = testProperties.get("password");

        String tripType = testProperties.get("tripType");
        String originAirport = testProperties.get("originAirport");
        String destinationAirport = testProperties.get("destinationAirport");
        int daysToDepartureDate = Integer.parseInt(testProperties.get("daysToDepartureDate"));

        String event = testProperties.get("event");
        String eventAction = testProperties.get("eventAction");
        String screenName = testProperties.get("screenName");
        String utmPage = testProperties.get("utmPage");
        String isFirstPage = testProperties.get("isFirstPage");
        String vertical = testProperties.get("vertical");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightPage<WebDriver> flightPage = FlightPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Find dynamic data");
        flightPage.setDataOneWay(daysToDepartureDate, "");
        String departureDate = CoreFunctions.convertToLongDate(flightPage.getDate("departureDate"));

        step("Open app as login user");
        loginPage.loginWith(loginType, email, password);

        step("Open flights page");
        homePage.openVerticalPage("Flights");
        flightSearchPage.closePopup();

        step("Toggle trip type");
        flightSearchPage.toggleTriptype(tripType);

        step("Select origin airport");
        flightSearchPage.selectOrigin(originAirport);
        flightSearchPage.closePopup();

        step("Select destination airport");
        flightSearchPage.selectDestination(destinationAirport);

        step("Select date");
        flightSearchPage.selectDate(departureDate);

        step("Search flight");
        flightSearchPage.searchFlight();

        sleep(40);

        BaseAssertions flightPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("pageVisit event is found", flightPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, screenName), is(1));
        Assertion.assertThat("eventAction is correct", flightPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), is(true));
        Assertion.assertThat("screenName is correct", flightPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), is(true));
        Assertion.assertThat("utmPage is correct", flightPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "utmPage", utmPage), is(true));
        Assertion.assertThat("isFirstPage is correct", flightPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "isFirstPage", isFirstPage), is(true));
        Assertion.assertThat("vertical is correct", flightPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "vertical", vertical), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "FlightMainPage")
    @Tribe(name = "Flight")
    @Vertical(name = "Core")
    @Test
    public void testFlightEnterVerticalLogin() {
        Map<String, String> testProperties = getParam("testFlightEnterVerticalLogin");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String loginType = testProperties.get("loginType");
        String email = testProperties.get("email");
        String password = testProperties.get("password");

        String event = testProperties.get("event");
        String screenName = testProperties.get("screenName");
        String vertical = testProperties.get("vertical");
        String eventAction = testProperties.get("eventAction");
        String eventLabel = testProperties.get("eventLabel");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Open app as login user");
        loginPage.loginWith(loginType, email, password);

        step("Open flights vertical");
        homePage.openVerticalPage("Flights");

        sleep(40);

        BaseAssertions flightPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("enterVertical event is found", flightPageAssertion.verifyEvent(Configs.MESSAGES, event), is(1));
        Assertion.assertThat("screenName is correct", flightPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "screenName", screenName), is(true));
        Assertion.assertThat("vertical is correct", flightPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "vertical", vertical), is(true));
        Assertion.assertThat("eventAction is correct", flightPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "eventAction", eventAction), is(true));
        Assertion.assertThat("eventLabel is correct", flightPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "eventLabel", eventLabel), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "FlightSRP")
    @Tribe(name = "Flight")
    @Vertical(name = "Core")
    @Test
    public void testFlightSearchLoadedLogin() {
        Map<String, String> testProperties = getParam("testFlightSearchLoadedLogin");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String loginType = testProperties.get("loginType");
        String email = testProperties.get("email");
        String password = testProperties.get("password");

        String tripType = testProperties.get("tripType");
        String originAirport = testProperties.get("originAirport");
        String destinationAirport = testProperties.get("destinationAirport");
        int daysToDepartureDate = Integer.parseInt(testProperties.get("daysToDepartureDate"));

        String event = testProperties.get("event");
        String screenName = testProperties.get("screenName");
        String vertical = testProperties.get("vertical");
        String eventAction = testProperties.get("eventAction");
        String eventLabel = testProperties.get("eventLabel");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightPage<WebDriver> flightPage = FlightPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Find dynamic data");
        flightPage.setDataOneWay(daysToDepartureDate, "");
        String departureDate = CoreFunctions.convertToLongDate(flightPage.getDate("departureDate"));

        step("Open app as login user");
        loginPage.loginWith(loginType, email, password);

        step("Open flights page");
        homePage.openVerticalPage("Flights");
        flightSearchPage.closePopup();

        step("Toggle trip type");
        flightSearchPage.toggleTriptype(tripType);

        step("Select origin airport");
        flightSearchPage.selectOrigin(originAirport);
        flightSearchPage.closePopup();

        step("Select destination airport");
        flightSearchPage.selectDestination(destinationAirport);

        step("Select date");
        flightSearchPage.selectDate(departureDate);

        step("Search flight");
        flightSearchPage.searchFlight();

        sleep(40);

        BaseAssertions flightPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("searchLoaded event is found", flightPageAssertion.verifyEvent(Configs.MESSAGES, event), is(1));
        Assertion.assertThat("screenName is correct", flightPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "screenName", screenName), is(true));
        Assertion.assertThat("vertical is correct", flightPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "vertical", vertical), is(true));
        Assertion.assertThat("eventAction is correct", flightPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "eventAction", eventAction), is(true));
        Assertion.assertThat("eventLabel is correct", flightPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "eventLabel", eventLabel), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "FlightSRP")
    @Tribe(name = "Flight")
    @Vertical(name = "Core")
    @Test
    public void testFlightSearchProductLogin() {
        Map<String, String> testProperties = getParam("testFlightSearchProductLogin");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String loginType = testProperties.get("loginType");
        String email = testProperties.get("email");
        String password = testProperties.get("password");

        String tripType = testProperties.get("tripType");
        String originAirport = testProperties.get("originAirport");
        String destinationAirport = testProperties.get("destinationAirport");
        int daysToDepartureDate = Integer.parseInt(testProperties.get("daysToDepartureDate"));

        String event = testProperties.get("event");
        String screenName = testProperties.get("screenName");
        String vertical = testProperties.get("vertical");
        String eventAction = testProperties.get("eventAction");
        String eventLabel = testProperties.get("eventLabel");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightPage<WebDriver> flightPage = FlightPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Find dynamic data");
        flightPage.setDataOneWay(daysToDepartureDate, "");
        String departureDate = CoreFunctions.convertToLongDate(flightPage.getDate("departureDate"));

        step("Open app as login user");
        loginPage.loginWith(loginType, email, password);

        step("Open flights page");
        homePage.openVerticalPage("Flights");
        flightSearchPage.closePopup();

        step("Toggle trip type");
        flightSearchPage.toggleTriptype(tripType);

        step("Select origin airport");
        flightSearchPage.selectOrigin(originAirport);
        flightSearchPage.closePopup();

        step("Select destination airport");
        flightSearchPage.selectDestination(destinationAirport);

        step("Select date");
        flightSearchPage.selectDate(departureDate);

        step("Search flight");
        flightSearchPage.searchFlight();

        sleep(40);

        BaseAssertions flightPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("searchProduct event is found", flightPageAssertion.verifyEvent(Configs.MESSAGES, event), is(1));
        Assertion.assertThat("screenName is correct", flightPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "screenName", screenName), is(true));
        Assertion.assertThat("vertical is correct", flightPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "vertical", vertical), is(true));
        Assertion.assertThat("eventAction is correct", flightPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "eventAction", eventAction), is(true));
        Assertion.assertThat("eventLabel is correct", flightPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "eventLabel", eventLabel), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "FlightBookingPage")
    @Tribe(name = "Flight")
    @Vertical(name = "Core")
    @Test
    public void testFlightBookProductLogin() {
        Map<String, String> testProperties = getParam("testFlightBookProductLogin");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String loginType = testProperties.get("loginType");
        String email = testProperties.get("email");
        String password = testProperties.get("password");

        String tripType = testProperties.get("tripType");
        String originAirport = testProperties.get("originAirport");
        String destinationAirport = testProperties.get("destinationAirport");
        int daysToDepartureDate = Integer.parseInt(testProperties.get("daysToDepartureDate"));
        int adultPassengers = Integer.parseInt(testProperties.get("adult"));
        int childPassengers = Integer.parseInt(testProperties.get("child"));
        int infantPassengers = Integer.parseInt(testProperties.get("infant"));

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightPage<WebDriver> flightPage = FlightPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightProductReviewPage<WebDriver> flightProductReviewPage = FlightProductReviewPage.getInstance(session);
        FlightBookingFormPage<WebDriver> flightBookingFormPage = FlightBookingFormPage.getInstance(session);

        String event = testProperties.get("event");
        String screenName = testProperties.get("screenName");
        String vertical = testProperties.get("vertical");
        String eventAction = testProperties.get("eventAction");
        String eventDescription = testProperties.get("eventDescription");
        String eventLabel = testProperties.get("eventLabel");

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Find dynamic data");
        flightPage.setDataOneWay(daysToDepartureDate, "");
        String departureDate = CoreFunctions.convertToLongDate(flightPage.getDate("departureDate"));

        step("Open app as login user");
        loginPage.loginWith(loginType, email, password);

        step("Open flights page");
        homePage.openVerticalPage("Flights");
        flightSearchPage.closePopup();

        step("Toggle trip type");
        flightSearchPage.toggleTriptype(tripType);

        step("Select origin airport");
        flightSearchPage.selectOrigin(originAirport);
        flightSearchPage.closePopup();

        step("Select destination airport");
        flightSearchPage.selectDestination(destinationAirport);

        step("Select date");
        flightSearchPage.selectDate(departureDate);

        step("Search flight");
        flightSearchPage.searchFlight();

        step("Click flight detail chevron");
        flightSearchResultPage.selectFlight();

        step("Continue to booking form");
        flightProductReviewPage.continueToBookingForm(tripType);

        step("Fill passenger detail");
        flightBookingFormPage.fillPassengerDetail(adultPassengers, childPassengers, infantPassengers, "domestic", "");

        step("Continue to second half booking form");
        flightBookingFormPage.continueToSecondHalf();

        step("Continue to payment");
        flightBookingFormPage.continueToPayment();

        sleep(40);

        BaseAssertions flightPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("bookProduct event is found", flightPageAssertion.verifyEvent(Configs.MESSAGES, event), greaterThan(0));
        Assertion.assertThat("screenName is correct", flightPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "screenName", screenName), is(true));
        Assertion.assertThat("vertical is correct", flightPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "vertical", vertical), is(true));
        Assertion.assertThat("eventAction is correct", flightPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "eventAction", eventAction), is(true));
        Assertion.assertThat("eventDescription is correct", flightPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "eventDescription", eventDescription), is(true));
        Assertion.assertThat("eventLabel is correct", flightPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "eventLabel", eventLabel), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "FlightBookingPage")
    @Tribe(name = "Flight")
    @Vertical(name = "Core")
    @Test
    public void testFlightBookingProductLogin() {
        Map<String, String> testProperties = getParam("testFlightBookingProductLogin");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String loginType = testProperties.get("loginType");
        String email = testProperties.get("email");
        String password = testProperties.get("password");

        String tripType = testProperties.get("tripType");
        String originAirport = testProperties.get("originAirport");
        String destinationAirport = testProperties.get("destinationAirport");
        int daysToDepartureDate = Integer.parseInt(testProperties.get("daysToDepartureDate"));

        String event = testProperties.get("event");
        String screenName = testProperties.get("screenName");
        String vertical = testProperties.get("vertical");
        String eventAction = testProperties.get("eventAction");
        String eventLabel = testProperties.get("eventLabel");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightPage<WebDriver> flightPage = FlightPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightProductReviewPage<WebDriver> flightProductReviewPage = FlightProductReviewPage.getInstance(session);

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Find dynamic data");
        flightPage.setDataOneWay(daysToDepartureDate, "");
        String departureDate = CoreFunctions.convertToLongDate(flightPage.getDate("departureDate"));

        step("Open app as login user");
        loginPage.loginWith(loginType, email, password);

        step("Open flights page");
        homePage.openVerticalPage("Flights");
        flightSearchPage.closePopup();

        step("Toggle trip type");
        flightSearchPage.toggleTriptype(tripType);

        step("Select origin airport");
        flightSearchPage.selectOrigin(originAirport);
        flightSearchPage.closePopup();

        step("Select destination airport");
        flightSearchPage.selectDestination(destinationAirport);

        step("Select date");
        flightSearchPage.selectDate(departureDate);

        step("Search flight");
        flightSearchPage.searchFlight();

        step("Click flight detail chevron");
        flightSearchResultPage.selectFlight();

        step("Continue to booking form");
        flightProductReviewPage.continueToBookingForm(tripType);

        sleep(40);

        BaseAssertions flightPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("bookingProduct event is found", flightPageAssertion.verifyEvent(Configs.MESSAGES, event), is(1));
        Assertion.assertThat("screenName is correct", flightPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "screenName", screenName), is(true));
        Assertion.assertThat("vertical is correct", flightPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "vertical", vertical), is(true));
        Assertion.assertThat("eventAction is correct", flightPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "eventAction", eventAction), is(true));
        Assertion.assertThat("eventLabel is correct", flightPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "eventLabel", eventLabel), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "FlightMainPage")
    @Tribe(name = "Flight")
    @Vertical(name = "Core")
    @Test
    public void testFlightMainPageVisitDeeplinkUtmGuest() {
        Map<String, String> testProperties = getParam("testFlightMainPageVisitDeeplinkUtmGuest");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String deeplink = "tiket://m.tiket.com/pesawat/?";

        String event = testProperties.get("event");
        String eventAction = testProperties.get("eventAction");
        String screenName = testProperties.get("screenName");
        String utmPage = testProperties.get("utmPage");
        String isFirstPage = testProperties.get("isFirstPage");
        String vertical = testProperties.get("vertical");

        String utmCampaign = testProperties.get("utmCampaign");
        String utmContent = testProperties.get("utmContent");
        String utmExternal = "nonOrganic";
        String utmId = testProperties.get("utmId");
        String utmMedium = testProperties.get("utmMedium");
        String utmSource = testProperties.get("utmSource");
        String utmTerm = testProperties.get("utmTerm");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        AnalyticsHomePage<WebDriver> analyticsHomePage = AnalyticsHomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Open app as guest");
        loginPage.loginAsGuest();

        step("Open flight vertical via deeplink");
        String utmProperties = analyticsHomePage.getUtmProperties(utmCampaign, utmContent, utmId, utmMedium, utmSource, utmTerm);
        analyticsHomePage.openDeeplink(deeplink + utmProperties);

        sleep(40);

        BaseAssertions flightPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("utmAttributedAt is not null", flightPageAssertion.verifyUserProperty(Configs.MESSAGES, "utmAttributedAt"), is(true));
        Assertion.assertThat("utmCampaign is correct", flightPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmCampaign", utmCampaign), is(true));
        Assertion.assertThat("utmContent is correct", flightPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmContent", utmContent), is(true));
        Assertion.assertThat("utmExternal is correct", flightPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmExternal", utmExternal), is(true));
        Assertion.assertThat("utmId is correct", flightPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmId", utmId), is(true));
        Assertion.assertThat("utmMedium is correct", flightPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmMedium", utmMedium), is(true));
        Assertion.assertThat("utmSource is correct", flightPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmSource", utmSource), is(true));
        Assertion.assertThat("utmTerm is correct", flightPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmTerm", utmTerm), is(true));
        Assertion.assertThat("userId is null", flightPageAssertion.verifyUserProperty(Configs.MESSAGES, "userId"), Matchers.is(false));

        Assertion.assertThat("pageVisit event is found", flightPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, screenName), is(1));
        Assertion.assertThat("eventAction is correct", flightPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), is(true));
        Assertion.assertThat("screenName is correct", flightPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), is(true));
        Assertion.assertThat("utmPage is correct", flightPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "utmPage", utmPage), is(true));
        Assertion.assertThat("isFirstPage is correct", flightPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "isFirstPage", isFirstPage), is(true));
        Assertion.assertThat("vertical is correct", flightPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "vertical", vertical), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "FlightMainPage")
    @Tribe(name = "Flight")
    @Vertical(name = "Core")
    @Test
    public void testFlightMainPageVisitDeeplinkUtmLogin() {
        Map<String, String> testProperties = getParam("testFlightMainPageVisitDeeplinkUtmLogin");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String loginType = testProperties.get("loginType");
        String email = testProperties.get("email");
        String password = testProperties.get("password");
        String deeplink = "tiket://m.tiket.com/pesawat/?";

        String event = testProperties.get("event");
        String eventAction = testProperties.get("eventAction");
        String screenName = testProperties.get("screenName");
        String utmPage = testProperties.get("utmPage");
        String isFirstPage = testProperties.get("isFirstPage");
        String vertical = testProperties.get("vertical");

        String utmCampaign = testProperties.get("utmCampaign");
        String utmContent = testProperties.get("utmContent");
        String utmExternal = "nonOrganic";
        String utmId = testProperties.get("utmId");
        String utmMedium = testProperties.get("utmMedium");
        String utmSource = testProperties.get("utmSource");
        String utmTerm = testProperties.get("utmTerm");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        AnalyticsHomePage<WebDriver> analyticsHomePage = AnalyticsHomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Open app as login user");
        loginPage.loginWith(loginType, email, password);

        step("Open flight vertical via deeplink");
        String utmProperties = analyticsHomePage.getUtmProperties(utmCampaign, utmContent, utmId, utmMedium, utmSource, utmTerm);
        analyticsHomePage.openDeeplink(deeplink + utmProperties);

        sleep(40);

        BaseAssertions flightPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("utmAttributedAt is not null", flightPageAssertion.verifyUserProperty(Configs.MESSAGES, "utmAttributedAt"), is(true));
        Assertion.assertThat("utmCampaign is correct", flightPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmCampaign", utmCampaign), is(true));
        Assertion.assertThat("utmContent is correct", flightPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmContent", utmContent), is(true));
        Assertion.assertThat("utmExternal is correct", flightPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmExternal", utmExternal), is(true));
        Assertion.assertThat("utmId is correct", flightPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmId", utmId), is(true));
        Assertion.assertThat("utmMedium is correct", flightPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmMedium", utmMedium), is(true));
        Assertion.assertThat("utmSource is correct", flightPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmSource", utmSource), is(true));
        Assertion.assertThat("utmTerm is correct", flightPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmTerm", utmTerm), is(true));
        Assertion.assertThat("userId is not null", flightPageAssertion.verifyUserProperty(Configs.MESSAGES, "userId"), Matchers.is(true));

        Assertion.assertThat("pageVisit event is found", flightPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, screenName), is(1));
        Assertion.assertThat("eventAction is correct", flightPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), is(true));
        Assertion.assertThat("screenName is correct", flightPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), is(true));
        Assertion.assertThat("utmPage is correct", flightPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "utmPage", utmPage), is(true));
        Assertion.assertThat("isFirstPage is correct", flightPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "isFirstPage", isFirstPage), is(true));
        Assertion.assertThat("vertical is correct", flightPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "vertical", vertical), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "FlightSRP")
    @Tribe(name = "Flight")
    @Vertical(name = "Core")
    @Test
    public void testFlightSRPPageVisitDeeplinkUtmGuest() {
        Map<String, String> testProperties = getParam("testFlightSRPPageVisitDeeplinkUtmGuest");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        int daysToDepartureDate = Integer.parseInt(testProperties.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentDateYYYY_MM_DD();
        String departureDate = CoreFunctions.addToDateYYYY_MM_DD(currentDate, daysToDepartureDate);
        String deeplink = "tiket://m.tiket.com/pesawat/search?d=JKTC&a=SUBC&date=" + departureDate + "&adult=1&child=0&infant=0&class=economy&dType=CITY&aType=CITY&dLabel=Jakarta&aLabel=Surabaya&type=depart&flexiFare=true&";

        String event = testProperties.get("event");
        String eventAction = testProperties.get("eventAction");
        String screenName = testProperties.get("screenName");
        String utmPage = testProperties.get("utmPage");
        String isFirstPage = testProperties.get("isFirstPage");
        String vertical = testProperties.get("vertical");

        String utmCampaign = testProperties.get("utmCampaign");
        String utmContent = testProperties.get("utmContent");
        String utmExternal = "nonOrganic";
        String utmId = testProperties.get("utmId");
        String utmMedium = testProperties.get("utmMedium");
        String utmSource = testProperties.get("utmSource");
        String utmTerm = testProperties.get("utmTerm");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        AnalyticsHomePage<WebDriver> analyticsHomePage = AnalyticsHomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Open app as guest");
        loginPage.loginAsGuest();

        step("Open flight SRP via deeplink");
        String utmProperties = analyticsHomePage.getUtmProperties(utmCampaign, utmContent, utmId, utmMedium, utmSource, utmTerm);
        analyticsHomePage.openDeeplink(deeplink + utmProperties);

        sleep(40);

        BaseAssertions flightPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("utmAttributedAt is not null", flightPageAssertion.verifyUserProperty(Configs.MESSAGES, "utmAttributedAt"), is(true));
        Assertion.assertThat("utmCampaign is correct", flightPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmCampaign", utmCampaign), is(true));
        Assertion.assertThat("utmContent is correct", flightPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmContent", utmContent), is(true));
        Assertion.assertThat("utmExternal is correct", flightPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmExternal", utmExternal), is(true));
        Assertion.assertThat("utmId is correct", flightPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmId", utmId), is(true));
        Assertion.assertThat("utmMedium is correct", flightPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmMedium", utmMedium), is(true));
        Assertion.assertThat("utmSource is correct", flightPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmSource", utmSource), is(true));
        Assertion.assertThat("utmTerm is correct", flightPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmTerm", utmTerm), is(true));
        Assertion.assertThat("userId is null", flightPageAssertion.verifyUserProperty(Configs.MESSAGES, "userId"), Matchers.is(false));

        Assertion.assertThat("pageVisit event is found", flightPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, screenName), is(1));
        Assertion.assertThat("eventAction is correct", flightPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), is(true));
        Assertion.assertThat("screenName is correct", flightPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), is(true));
        Assertion.assertThat("utmPage is correct", flightPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "utmPage", utmPage), is(true));
        Assertion.assertThat("isFirstPage is correct", flightPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "isFirstPage", isFirstPage), is(true));
        Assertion.assertThat("vertical is correct", flightPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "vertical", vertical), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "FlightSRP")
    @Tribe(name = "Flight")
    @Vertical(name = "Core")
    @Test
    public void testFlightSRPPageVisitDeeplinkUtmLogin() {
        Map<String, String> testProperties = getParam("testFlightSRPPageVisitDeeplinkUtmLogin");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String loginType = testProperties.get("loginType");
        String email = testProperties.get("email");
        String password = testProperties.get("password");

        int daysToDepartureDate = Integer.parseInt(testProperties.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentDateYYYY_MM_DD();
        String departureDate = CoreFunctions.addToDateYYYY_MM_DD(currentDate, daysToDepartureDate);
        String deeplink = "tiket://m.tiket.com/pesawat/search?d=JKTC&a=SUBC&date=" + departureDate + "&adult=1&child=0&infant=0&class=economy&dType=CITY&aType=CITY&dLabel=Jakarta&aLabel=Surabaya&type=depart&flexiFare=true&";

        String event = testProperties.get("event");
        String eventAction = testProperties.get("eventAction");
        String screenName = testProperties.get("screenName");
        String utmPage = testProperties.get("utmPage");
        String isFirstPage = testProperties.get("isFirstPage");
        String vertical = testProperties.get("vertical");

        String utmCampaign = testProperties.get("utmCampaign");
        String utmContent = testProperties.get("utmContent");
        String utmExternal = "nonOrganic";
        String utmId = testProperties.get("utmId");
        String utmMedium = testProperties.get("utmMedium");
        String utmSource = testProperties.get("utmSource");
        String utmTerm = testProperties.get("utmTerm");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        AnalyticsHomePage<WebDriver> analyticsHomePage = AnalyticsHomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Open app as login user");
        loginPage.loginWith(loginType, email, password);

        step("Open flight SRP via deeplink");
        String utmProperties = analyticsHomePage.getUtmProperties(utmCampaign, utmContent, utmId, utmMedium, utmSource, utmTerm);
        analyticsHomePage.openDeeplink(deeplink + utmProperties);

        sleep(40);

        BaseAssertions flightPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("utmAttributedAt is not null", flightPageAssertion.verifyUserProperty(Configs.MESSAGES, "utmAttributedAt"), is(true));
        Assertion.assertThat("utmCampaign is correct", flightPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmCampaign", utmCampaign), is(true));
        Assertion.assertThat("utmContent is correct", flightPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmContent", utmContent), is(true));
        Assertion.assertThat("utmExternal is correct", flightPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmExternal", utmExternal), is(true));
        Assertion.assertThat("utmId is correct", flightPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmId", utmId), is(true));
        Assertion.assertThat("utmMedium is correct", flightPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmMedium", utmMedium), is(true));
        Assertion.assertThat("utmSource is correct", flightPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmSource", utmSource), is(true));
        Assertion.assertThat("utmTerm is correct", flightPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmTerm", utmTerm), is(true));
        Assertion.assertThat("userId is not null", flightPageAssertion.verifyUserProperty(Configs.MESSAGES, "userId"), Matchers.is(true));

        Assertion.assertThat("pageVisit event is found", flightPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, screenName), is(1));
        Assertion.assertThat("eventAction is correct", flightPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), is(true));
        Assertion.assertThat("screenName is correct", flightPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), is(true));
        Assertion.assertThat("utmPage is correct", flightPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "utmPage", utmPage), is(true));
        Assertion.assertThat("isFirstPage is correct", flightPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "isFirstPage", isFirstPage), is(true));
        Assertion.assertThat("vertical is correct", flightPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "vertical", vertical), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "FlightMainPage")
    @Tribe(name = "Flight")
    @Vertical(name = "Core")
    @Test
    public void testFlightMainPageVisitDeeplinkNonUtmGuest() {
        Map<String, String> testProperties = getParam("testFlightMainPageVisitDeeplinkNonUtmGuest");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String deeplink = "tiket://m.tiket.com/pesawat";

        String event = testProperties.get("event");
        String eventAction = testProperties.get("eventAction");
        String screenName = testProperties.get("screenName");
        String utmPage = testProperties.get("utmPage");
        String isFirstPage = testProperties.get("isFirstPage");
        String vertical = testProperties.get("vertical");

        String utmCampaign, utmContent, utmExternal, utmId, utmMedium, utmSource, utmTerm;
        utmCampaign = utmContent = utmId = utmMedium = utmSource = utmTerm = "none";
        utmExternal = "organic";

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        AnalyticsHomePage<WebDriver> analyticsHomePage = AnalyticsHomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Open app as guest");
        loginPage.loginAsGuest();

        step("Open flight vertical via deeplink");
        analyticsHomePage.openDeeplink(deeplink);

        sleep(40);

        BaseAssertions flightPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("utmAttributedAt is not null", flightPageAssertion.verifyUserProperty(Configs.MESSAGES, "utmAttributedAt"), is(true));
        Assertion.assertThat("utmCampaign is correct", flightPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmCampaign", utmCampaign), is(true));
        Assertion.assertThat("utmContent is correct", flightPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmContent", utmContent), is(true));
        Assertion.assertThat("utmExternal is correct", flightPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmExternal", utmExternal), is(true));
        Assertion.assertThat("utmId is correct", flightPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmId", utmId), is(true));
        Assertion.assertThat("utmMedium is correct", flightPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmMedium", utmMedium), is(true));
        Assertion.assertThat("utmSource is correct", flightPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmSource", utmSource), is(true));
        Assertion.assertThat("utmTerm is correct", flightPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmTerm", utmTerm), is(true));
        Assertion.assertThat("userId is null", flightPageAssertion.verifyUserProperty(Configs.MESSAGES, "userId"), is(false));

        Assertion.assertThat("pageVisit event is found", flightPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, screenName), is(1));
        Assertion.assertThat("eventAction is correct", flightPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), is(true));
        Assertion.assertThat("screenName is correct", flightPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), is(true));
        Assertion.assertThat("utmPage is correct", flightPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "utmPage", utmPage), is(true));
        Assertion.assertThat("isFirstPage is correct", flightPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "isFirstPage", isFirstPage), is(true));
        Assertion.assertThat("vertical is correct", flightPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "vertical", vertical), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "FlightMainPage")
    @Tribe(name = "Flight")
    @Vertical(name = "Core")
    @Test
    public void testFlightMainPageVisitDeeplinkNonUtmLogin() {
        Map<String, String> testProperties = getParam("testFlightMainPageVisitDeeplinkNonUtmLogin");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String loginType = testProperties.get("loginType");
        String email = testProperties.get("email");
        String password = testProperties.get("password");
        String deeplink = "tiket://m.tiket.com/pesawat";

        String event = testProperties.get("event");
        String eventAction = testProperties.get("eventAction");
        String screenName = testProperties.get("screenName");
        String utmPage = testProperties.get("utmPage");
        String isFirstPage = testProperties.get("isFirstPage");
        String vertical = testProperties.get("vertical");

        String utmCampaign, utmContent, utmExternal, utmId, utmMedium, utmSource, utmTerm;
        utmCampaign = utmContent = utmId = utmMedium = utmSource = utmTerm = "none";
        utmExternal = "organic";

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        AnalyticsHomePage<WebDriver> analyticsHomePage = AnalyticsHomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Open app as login user");
        loginPage.loginWith(loginType, email, password);

        step("Open flight vertical via deeplink");
        analyticsHomePage.openDeeplink(deeplink);

        sleep(40);

        BaseAssertions flightPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("utmAttributedAt is not null", flightPageAssertion.verifyUserProperty(Configs.MESSAGES, "utmAttributedAt"), is(true));
        Assertion.assertThat("utmCampaign is correct", flightPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmCampaign", utmCampaign), is(true));
        Assertion.assertThat("utmContent is correct", flightPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmContent", utmContent), is(true));
        Assertion.assertThat("utmExternal is correct", flightPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmExternal", utmExternal), is(true));
        Assertion.assertThat("utmId is correct", flightPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmId", utmId), is(true));
        Assertion.assertThat("utmMedium is correct", flightPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmMedium", utmMedium), is(true));
        Assertion.assertThat("utmSource is correct", flightPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmSource", utmSource), is(true));
        Assertion.assertThat("utmTerm is correct", flightPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmTerm", utmTerm), is(true));
        Assertion.assertThat("userId is not null", flightPageAssertion.verifyUserProperty(Configs.MESSAGES, "userId"), is(true));

        Assertion.assertThat("pageVisit event is found", flightPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, screenName), is(1));
        Assertion.assertThat("eventAction is correct", flightPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), is(true));
        Assertion.assertThat("screenName is correct", flightPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), is(true));
        Assertion.assertThat("utmPage is correct", flightPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "utmPage", utmPage), is(true));
        Assertion.assertThat("isFirstPage is correct", flightPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "isFirstPage", isFirstPage), is(true));
        Assertion.assertThat("vertical is correct", flightPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "vertical", vertical), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "FlightSRP")
    @Tribe(name = "Flight")
    @Vertical(name = "Core")
    @Test
    public void testFlightSRPPageVisitDeeplinkNonUtmGuest() {
        Map<String, String> testProperties = getParam("testFlightSRPPageVisitDeeplinkNonUtmGuest");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        int daysToDepartureDate = Integer.parseInt(testProperties.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentDateYYYY_MM_DD();
        String departureDate = CoreFunctions.addToDateYYYY_MM_DD(currentDate, daysToDepartureDate);
        String deeplink = "tiket://m.tiket.com/pesawat/search?d=JKTC&a=SUBC&date=" + departureDate + "&adult=1&child=0&infant=0&class=economy&dType=CITY&aType=CITY&dLabel=Jakarta&aLabel=Surabaya&type=depart&flexiFare=true";

        String event = testProperties.get("event");
        String eventAction = testProperties.get("eventAction");
        String screenName = testProperties.get("screenName");
        String utmPage = testProperties.get("utmPage");
        String isFirstPage = testProperties.get("isFirstPage");
        String vertical = testProperties.get("vertical");

        String utmCampaign, utmContent, utmExternal, utmId, utmMedium, utmSource, utmTerm;
        utmCampaign = utmContent = utmId = utmMedium = utmSource = utmTerm = "none";
        utmExternal = "organic";

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        AnalyticsHomePage<WebDriver> analyticsHomePage = AnalyticsHomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Open app as guest");
        loginPage.loginAsGuest();

        step("Open flight SRP via deeplink");
        analyticsHomePage.openDeeplink(deeplink);

        sleep(40);

        BaseAssertions flightPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("utmAttributedAt is not null", flightPageAssertion.verifyUserProperty(Configs.MESSAGES, "utmAttributedAt"), is(true));
        Assertion.assertThat("utmCampaign is correct", flightPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmCampaign", utmCampaign), is(true));
        Assertion.assertThat("utmContent is correct", flightPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmContent", utmContent), is(true));
        Assertion.assertThat("utmExternal is correct", flightPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmExternal", utmExternal), is(true));
        Assertion.assertThat("utmId is correct", flightPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmId", utmId), is(true));
        Assertion.assertThat("utmMedium is correct", flightPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmMedium", utmMedium), is(true));
        Assertion.assertThat("utmSource is correct", flightPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmSource", utmSource), is(true));
        Assertion.assertThat("utmTerm is correct", flightPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmTerm", utmTerm), is(true));
        Assertion.assertThat("userId is null", flightPageAssertion.verifyUserProperty(Configs.MESSAGES, "userId"), is(false));

        Assertion.assertThat("pageVisit event is found", flightPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, screenName), is(1));
        Assertion.assertThat("eventAction is correct", flightPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), is(true));
        Assertion.assertThat("screenName is correct", flightPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), is(true));
        Assertion.assertThat("utmPage is correct", flightPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "utmPage", utmPage), is(true));
        Assertion.assertThat("isFirstPage is correct", flightPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "isFirstPage", isFirstPage), is(true));
        Assertion.assertThat("vertical is correct", flightPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "vertical", vertical), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "FlightSRP")
    @Tribe(name = "Flight")
    @Vertical(name = "Core")
    @Test
    public void testFlightSRPPageVisitDeeplinkNonUtmLogin() {
        Map<String, String> testProperties = getParam("testFlightSRPPageVisitDeeplinkNonUtmLogin");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String loginType = testProperties.get("loginType");
        String email = testProperties.get("email");
        String password = testProperties.get("password");

        int daysToDepartureDate = Integer.parseInt(testProperties.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentDateYYYY_MM_DD();
        String departureDate = CoreFunctions.addToDateYYYY_MM_DD(currentDate, daysToDepartureDate);
        String deeplink = "tiket://m.tiket.com/pesawat/search?d=JKTC&a=SUBC&date=" + departureDate + "&adult=1&child=0&infant=0&class=economy&dType=CITY&aType=CITY&dLabel=Jakarta&aLabel=Surabaya&type=depart&flexiFare=true";

        String event = testProperties.get("event");
        String eventAction = testProperties.get("eventAction");
        String screenName = testProperties.get("screenName");
        String utmPage = testProperties.get("utmPage");
        String isFirstPage = testProperties.get("isFirstPage");
        String vertical = testProperties.get("vertical");

        String utmCampaign, utmContent, utmExternal, utmId, utmMedium, utmSource, utmTerm;
        utmCampaign = utmContent = utmId = utmMedium = utmSource = utmTerm = "none";
        utmExternal = "organic";

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        AnalyticsHomePage<WebDriver> analyticsHomePage = AnalyticsHomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Open app as login user");
        loginPage.loginWith(loginType, email, password);

        step("Open flight SRP via deeplink");
        analyticsHomePage.openDeeplink(deeplink);

        sleep(40);

        BaseAssertions flightPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("utmAttributedAt is not null", flightPageAssertion.verifyUserProperty(Configs.MESSAGES, "utmAttributedAt"), is(true));
        Assertion.assertThat("utmCampaign is correct", flightPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmCampaign", utmCampaign), is(true));
        Assertion.assertThat("utmContent is correct", flightPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmContent", utmContent), is(true));
        Assertion.assertThat("utmExternal is correct", flightPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmExternal", utmExternal), is(true));
        Assertion.assertThat("utmId is correct", flightPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmId", utmId), is(true));
        Assertion.assertThat("utmMedium is correct", flightPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmMedium", utmMedium), is(true));
        Assertion.assertThat("utmSource is correct", flightPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmSource", utmSource), is(true));
        Assertion.assertThat("utmTerm is correct", flightPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmTerm", utmTerm), is(true));
        Assertion.assertThat("userId is not null", flightPageAssertion.verifyUserProperty(Configs.MESSAGES, "userId"), is(true));

        Assertion.assertThat("pageVisit event is found", flightPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, screenName), is(1));
        Assertion.assertThat("eventAction is correct", flightPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), is(true));
        Assertion.assertThat("screenName is correct", flightPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), is(true));
        Assertion.assertThat("utmPage is correct", flightPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "utmPage", utmPage), is(true));
        Assertion.assertThat("isFirstPage is correct", flightPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "isFirstPage", isFirstPage), is(true));
        Assertion.assertThat("vertical is correct", flightPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "vertical", vertical), is(true));
    }
}