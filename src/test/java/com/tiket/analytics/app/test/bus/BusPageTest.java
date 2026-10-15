package com.tiket.analytics.app.test.bus;

import com.tiket.analytics.BaseAnalyticsTest;
import com.tiket.analytics.assertions.BaseAssertions;
import com.tiket.analytics.app.home.AnalyticsHomePage;
import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Session;
import com.tiket.page.bus.bookingform.BusBookingFormPage;
import com.tiket.page.bus.pdp.BuspdpPage;
import com.tiket.page.bus.search.BusSearchPage;
import com.tiket.page.bus.searchresultpage.BusSearchResultPage;
import com.tiket.page.home.HomePage;
import com.tiket.page.login.LoginPage;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.testbase.Configs;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import java.util.Map;

import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.core.Is.is;

public class BusPageTest extends BaseAnalyticsTest {

    private static final int ANALYTICS_WAIT_SECONDS = 40;

    @Author(names = {"Okta"})
    @Module(name = "BusMainPage")
    @Tribe(name = "Bus")
    @Vertical(name = "Core")
    @Test
    public void testBusMainPageVisitGuest(){
        Map<String, String> testProperties = getParam("testBusMainPageVisitGuest");
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
        BusSearchPage<WebDriver> busSearchPage = BusSearchPage.getInstance(session);

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Open app as guest");
        loginPage.loginAsGuest();

        step("Open bus vertical");
        homePage.openVerticalPage("Bus");

        step("Skip coach mark in landing page");
        busSearchPage.skipCoachMark();

        sleep(ANALYTICS_WAIT_SECONDS);

        BaseAssertions busPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("pageVisit event is found", busPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, screenName), is(1));
        Assertion.assertThat("eventAction is correct", busPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), is(true));
        Assertion.assertThat("screenName is correct", busPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), is(true));
        Assertion.assertThat("utmPage is correct", busPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "utmPage", utmPage), is(true));
        Assertion.assertThat("isFirstPage is correct", busPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "isFirstPage", isFirstPage), is(true));
        Assertion.assertThat("vertical is correct", busPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "vertical", vertical), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "BusPDP")
    @Tribe(name = "Bus")
    @Vertical(name = "Core")
    @Test
    public void testBusPDPPageVisitGuest(){
        Map<String, String> testProperties = getParam("testBusPDPPageVisitGuest");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String tripType = testProperties.get("tripType");
        String origin = testProperties.get("origin");
        String destination = testProperties.get("destination");
        int daysToDeparture = Integer.parseInt(testProperties.get("daysToDeparture"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String dateDeparture = CoreFunctions.addToLongDate(currentDate, daysToDeparture);
        int passenger = Integer.parseInt(testProperties.get("passenger"));

        String event = testProperties.get("event");
        String eventAction = testProperties.get("eventAction");
        String screenName = testProperties.get("screenName");
        String utmPage = testProperties.get("utmPage");
        String isFirstPage = testProperties.get("isFirstPage");
        String vertical = testProperties.get("vertical");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        BusSearchPage<WebDriver> busSearchPage = BusSearchPage.getInstance(session);
        BusSearchResultPage<WebDriver> busSearchResultPage = BusSearchResultPage.getInstance(session);

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Open app as guest");
        loginPage.loginAsGuest();

        step("Open bus vertical with deeplink");
        busSearchPage.openBusSearchViaDeepLink();

        step("Skip coach mark in landing page");
        busSearchPage.skipCoachMark();

        step("Select one way trip type");
        busSearchPage.switchTripType(tripType);

        step("Input origin location");
        busSearchPage.inputOriginLocation(origin);

        step("Input destination location");
        busSearchPage.inputDestinationLocation(destination);

        step("Select date");
        busSearchPage.selectDate(dateDeparture);

        step("Select pax amount");
        busSearchPage.selectPassenger(passenger);

        step("Search bus");
        busSearchPage.clickSearchBus();

        step("Skip coach mark in SRP");
        busSearchResultPage.skipCoachMark();

        step("Select bus");
        busSearchResultPage.selectFirstBus();

        sleep(ANALYTICS_WAIT_SECONDS);

        BaseAssertions busPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("pageVisit event is found", busPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, screenName), is(1));
        Assertion.assertThat("eventAction is correct", busPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), is(true));
        Assertion.assertThat("screenName is correct", busPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), is(true));
        Assertion.assertThat("utmPage is correct", busPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "utmPage", utmPage), is(true));
        Assertion.assertThat("isFirstPage is correct", busPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "isFirstPage", isFirstPage), is(true));
        Assertion.assertThat("vertical is correct", busPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "vertical", vertical), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "BusMainPage")
    @Tribe(name = "Bus")
    @Vertical(name = "Core")
    @Test
    public void testBusEnterVerticalGuest(){
        Map<String, String> testProperties = getParam("testBusEnterVerticalGuest");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String event = testProperties.get("event");
        String screenName = testProperties.get("screenName");
        String vertical = testProperties.get("vertical");
        String eventAction = testProperties.get("eventAction");
        String eventLabel = testProperties.get("eventLabel");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        BusSearchPage<WebDriver> busSearchPage = BusSearchPage.getInstance(session);

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Open app as guest");
        loginPage.loginAsGuest();

        step("Open bus vertical");
        homePage.openVerticalPage("Bus");

        step("Skip coach mark in landing page");
        busSearchPage.skipCoachMark();

        sleep(ANALYTICS_WAIT_SECONDS);

        BaseAssertions busPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("enterVertical event is found", busPageAssertion.verifyEvent(Configs.MESSAGES, event), is(1));
        Assertion.assertThat("screenName is correct", busPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "screenName", screenName), is(true));
        Assertion.assertThat("vertical is correct", busPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "vertical", vertical), is(true));
        Assertion.assertThat("eventAction is correct", busPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "eventAction", eventAction), is(true));
        Assertion.assertThat("eventLabel is correct", busPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "eventLabel", eventLabel), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "BusSRP")
    @Tribe(name = "Bus")
    @Vertical(name = "Core")
    @Test
    public void testBusSearchProductGuest(){
        Map<String, String> testProperties = getParam("testBusSearchProductGuest");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String tripType = testProperties.get("tripType");
        String origin = testProperties.get("origin");
        String destination = testProperties.get("destination");
        int daysToDeparture = Integer.parseInt(testProperties.get("daysToDeparture"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String dateDeparture = CoreFunctions.addToLongDate(currentDate, daysToDeparture);
        int passenger = Integer.parseInt(testProperties.get("passenger"));

        String event = testProperties.get("event");
        String screenName = testProperties.get("screenName");
        String vertical = testProperties.get("vertical");
        String eventAction = testProperties.get("eventAction");
        String eventLabel = testProperties.get("eventLabel").replace("-", ";");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        BusSearchPage<WebDriver> busSearchPage = BusSearchPage.getInstance(session);
        BusSearchResultPage<WebDriver> busSearchResultPage = BusSearchResultPage.getInstance(session);

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Open app as guest");
        loginPage.loginAsGuest();

        step("Open bus vertical with deeplink");
        busSearchPage.openBusSearchViaDeepLink();

        step("Skip coach mark in landing page");
        busSearchPage.skipCoachMark();

        step("Select one way trip type");
        busSearchPage.switchTripType(tripType);

        step("Input origin location");
        busSearchPage.inputOriginLocation(origin);

        step("Input destination location");
        busSearchPage.inputDestinationLocation(destination);

        step("Select date");
        busSearchPage.selectDate(dateDeparture);

        step("Select pax amount");
        busSearchPage.selectPassenger(passenger);

        step("Search bus");
        busSearchPage.clickSearchBus();

        step("Skip coach mark in SRP");
        busSearchResultPage.skipCoachMark();

        sleep(ANALYTICS_WAIT_SECONDS);

        BaseAssertions busPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("searchProduct event is found", busPageAssertion.verifyEvent(Configs.MESSAGES, event), greaterThan(0));
        Assertion.assertThat("screenName is correct", busPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "screenName", screenName), is(true));
        Assertion.assertThat("vertical is correct", busPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "vertical", vertical), is(true));
        Assertion.assertThat("eventAction is correct", busPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "eventAction", eventAction), is(true));
        Assertion.assertThat("eventLabel is correct", busPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "eventLabel", eventLabel), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "BusSRP")
    @Tribe(name = "Bus")
    @Vertical(name = "Core")
    @Test
    public void testBusSRPPageVisitGuest(){
        Map<String, String> testProperties = getParam("testBusSRPPageVisitGuest");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String tripType = testProperties.get("tripType");
        String origin = testProperties.get("origin");
        String destination = testProperties.get("destination");
        int daysToDeparture = Integer.parseInt(testProperties.get("daysToDeparture"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String dateDeparture = CoreFunctions.addToLongDate(currentDate, daysToDeparture);
        int passenger = Integer.parseInt(testProperties.get("passenger"));

        String event = testProperties.get("event");
        String eventAction = testProperties.get("eventAction");
        String screenName = testProperties.get("screenName");
        String utmPage = testProperties.get("utmPage");
        String isFirstPage = testProperties.get("isFirstPage");
        String vertical = testProperties.get("vertical");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        BusSearchPage<WebDriver> busSearchPage = BusSearchPage.getInstance(session);
        BusSearchResultPage<WebDriver> busSearchResultPage = BusSearchResultPage.getInstance(session);

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Open app as guest");
        loginPage.loginAsGuest();

        step("Open bus vertical with deeplink");
        busSearchPage.openBusSearchViaDeepLink();

        step("Skip coach mark in landing page");
        busSearchPage.skipCoachMark();

        step("Select one way trip type");
        busSearchPage.switchTripType(tripType);

        step("Input origin location");
        busSearchPage.inputOriginLocation(origin);

        step("Input destination location");
        busSearchPage.inputDestinationLocation(destination);

        step("Select date");
        busSearchPage.selectDate(dateDeparture);

        step("Select pax amount");
        busSearchPage.selectPassenger(passenger);

        step("Search bus");
        busSearchPage.clickSearchBus();

        step("Skip coach mark in SRP");
        busSearchResultPage.skipCoachMark();

        sleep(ANALYTICS_WAIT_SECONDS);

        BaseAssertions busPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("pageVisit event is found", busPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, screenName), is(1));
        Assertion.assertThat("eventAction is correct", busPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), is(true));
        Assertion.assertThat("screenName is correct", busPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), is(true));
        Assertion.assertThat("utmPage is correct", busPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "utmPage", utmPage), is(true));
        Assertion.assertThat("isFirstPage is correct", busPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "isFirstPage", isFirstPage), is(true));
        Assertion.assertThat("vertical is correct", busPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "vertical", vertical), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "BusBookingPage")
    @Tribe(name = "Bus")
    @Vertical(name = "Core")
    @Test
    public void testBusBookProductGuest(){
        Map<String, String> testProperties = getParam("testBusBookProductGuest");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String tripType = testProperties.get("tripType");
        String origin = testProperties.get("origin");
        String destination = testProperties.get("destination");
        int daysToDeparture = Integer.parseInt(testProperties.get("daysToDeparture"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String dateDeparture = CoreFunctions.addToLongDate(currentDate, daysToDeparture);
        int passenger = Integer.parseInt(testProperties.get("passenger"));

        String fullName = CoreFunctions.randomName();
        String phoneNumber = CoreFunctions.randomPhone();
        String emailAddress = CoreFunctions.randomEmail();

        String event = testProperties.get("event");
        String screenName = testProperties.get("screenName");
        String vertical = testProperties.get("vertical");
        String eventAction = testProperties.get("eventAction");
        String eventLabel = testProperties.get("eventLabel").replace("-", ";");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        BusSearchPage<WebDriver> busSearchPage = BusSearchPage.getInstance(session);
        BusSearchResultPage<WebDriver> busSearchResultPage = BusSearchResultPage.getInstance(session);
        BuspdpPage<WebDriver> busPdpPage = BuspdpPage.getInstance(session);
        BusBookingFormPage<WebDriver> busBookingFormPage = BusBookingFormPage.getInstance(session);

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Open app as guest");
        loginPage.loginAsGuest();

        step("Open bus vertical with deeplink");
        busSearchPage.openBusSearchViaDeepLink();

        step("Skip coach mark in landing page");
        busSearchPage.skipCoachMark();

        step("Select one way trip type");
        busSearchPage.switchTripType(tripType);

        step("Input origin location");
        busSearchPage.inputOriginLocation(origin);

        step("Input destination location");
        busSearchPage.inputDestinationLocation(destination);

        step("Select date");
        busSearchPage.selectDate(dateDeparture);

        step("Select pax amount");
        busSearchPage.selectPassenger(passenger);

        step("Search bus");
        busSearchPage.clickSearchBus();

        step("Skip coach mark in SRP");
        busSearchResultPage.skipCoachMark();

        step("Select bus");
        busSearchResultPage.selectFirstBus();

        step("Select seat in PDP");
        busPdpPage.openSeat();
        busPdpPage.clickSelectSeatButton();

        step("Input guest contact detail");
        busBookingFormPage.inputGuestContactDetail(fullName, phoneNumber, emailAddress);
        busBookingFormPage.inputPassengerGuestDetail();
        busBookingFormPage.fillPassengerData(passenger);

        step("Continue to payment page");
        busBookingFormPage.continueToPaymentPage();

        sleep(ANALYTICS_WAIT_SECONDS);

        BaseAssertions busPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("bookProduct event is found", busPageAssertion.verifyEvent(Configs.MESSAGES, event), greaterThan(0));
        Assertion.assertThat("screenName is correct", busPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "screenName", screenName), is(true));
        Assertion.assertThat("vertical is correct", busPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "vertical", vertical), is(true));
        Assertion.assertThat("eventAction is correct", busPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "eventAction", eventAction), is(true));
        Assertion.assertThat("eventLabel is correct", busPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "eventLabel", eventLabel), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "BusMainPage")
    @Tribe(name = "Bus")
    @Vertical(name = "Core")
    @Test
    public void testBusMainPageVisitLogin(){
        Map<String, String> testProperties = getParam("testBusMainPageVisitLogin");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String loginMethod = testProperties.get("loginType");
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
        BusSearchPage<WebDriver> busSearchPage = BusSearchPage.getInstance(session);

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Open app as login user");
        loginPage.loginWith(loginMethod, email, password);

        step("Open bus vertical");
        homePage.openVerticalPage("Bus");

        step("Skip coach mark in landing page");
        busSearchPage.skipCoachMark();

        sleep(ANALYTICS_WAIT_SECONDS);

        BaseAssertions busPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("pageVisit event is found", busPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, screenName), is(1));
        Assertion.assertThat("eventAction is correct", busPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), is(true));
        Assertion.assertThat("screenName is correct", busPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), is(true));
        Assertion.assertThat("utmPage is correct", busPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "utmPage", utmPage), is(true));
        Assertion.assertThat("isFirstPage is correct", busPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "isFirstPage", isFirstPage), is(true));
        Assertion.assertThat("vertical is correct", busPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "vertical", vertical), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "BusPDP")
    @Tribe(name = "Bus")
    @Vertical(name = "Core")
    @Test
    public void testBusPDPPageVisitLogin(){
        Map<String, String> testProperties = getParam("testBusPDPPageVisitLogin");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String loginMethod = testProperties.get("loginType");
        String email = testProperties.get("email");
        String password = testProperties.get("password");

        String tripType = testProperties.get("tripType");
        String origin = testProperties.get("origin");
        String destination = testProperties.get("destination");
        int daysToDeparture = Integer.parseInt(testProperties.get("daysToDeparture"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String dateDeparture = CoreFunctions.addToLongDate(currentDate, daysToDeparture);
        int passenger = Integer.parseInt(testProperties.get("passenger"));

        String event = testProperties.get("event");
        String eventAction = testProperties.get("eventAction");
        String screenName = testProperties.get("screenName");
        String utmPage = testProperties.get("utmPage");
        String isFirstPage = testProperties.get("isFirstPage");
        String vertical = testProperties.get("vertical");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        BusSearchPage<WebDriver> busSearchPage = BusSearchPage.getInstance(session);
        BusSearchResultPage<WebDriver> busSearchResultPage = BusSearchResultPage.getInstance(session);

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Open app as login user");
        loginPage.loginWith(loginMethod, email, password);

        step("Open bus vertical with deeplink");
        busSearchPage.openBusSearchViaDeepLink();

        step("Skip coach mark in landing page");
        busSearchPage.skipCoachMark();

        step("Select one way trip type");
        busSearchPage.switchTripType(tripType);

        step("Input origin location");
        busSearchPage.inputOriginLocation(origin);

        step("Input destination location");
        busSearchPage.inputDestinationLocation(destination);

        step("Select date");
        busSearchPage.selectDate(dateDeparture);

        step("Select pax amount");
        busSearchPage.selectPassenger(passenger);

        step("Search bus");
        busSearchPage.clickSearchBus();

        step("Skip coach mark in SRP");
        busSearchResultPage.skipCoachMark();

        step("Select bus");
        busSearchResultPage.selectFirstBus();

        sleep(ANALYTICS_WAIT_SECONDS);

        BaseAssertions busPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("pageVisit event is found", busPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, screenName), is(1));
        Assertion.assertThat("eventAction is correct", busPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), is(true));
        Assertion.assertThat("screenName is correct", busPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), is(true));
        Assertion.assertThat("utmPage is correct", busPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "utmPage", utmPage), is(true));
        Assertion.assertThat("isFirstPage is correct", busPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "isFirstPage", isFirstPage), is(true));
        Assertion.assertThat("vertical is correct", busPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "vertical", vertical), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "BusMainPage")
    @Tribe(name = "Bus")
    @Vertical(name = "Core")
    @Test
    public void testBusEnterVerticalLogin(){
        Map<String, String> testProperties = getParam("testBusEnterVerticalLogin");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String loginMethod = testProperties.get("loginType");
        String email = testProperties.get("email");
        String password = testProperties.get("password");

        String event = testProperties.get("event");
        String screenName = testProperties.get("screenName");
        String vertical = testProperties.get("vertical");
        String eventAction = testProperties.get("eventAction");
        String eventLabel = testProperties.get("eventLabel");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        BusSearchPage<WebDriver> busSearchPage = BusSearchPage.getInstance(session);

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Open app as login user");
        loginPage.loginWith(loginMethod, email, password);

        step("Open bus vertical");
        homePage.openVerticalPage("Bus");

        step("Skip coach mark in landing page");
        busSearchPage.skipCoachMark();

        sleep(ANALYTICS_WAIT_SECONDS);

        BaseAssertions busPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("enterVertical event is found", busPageAssertion.verifyEvent(Configs.MESSAGES, event), is(1));
        Assertion.assertThat("screenName is correct", busPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "screenName", screenName), is(true));
        Assertion.assertThat("vertical is correct", busPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "vertical", vertical), is(true));
        Assertion.assertThat("eventAction is correct", busPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "eventAction", eventAction), is(true));
        Assertion.assertThat("eventLabel is correct", busPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "eventLabel", eventLabel), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "BusSRP")
    @Tribe(name = "Bus")
    @Vertical(name = "Core")
    @Test
    public void testBusSearchProductLogin(){
        Map<String, String> testProperties = getParam("testBusSearchProductLogin");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String loginMethod = testProperties.get("loginType");
        String email = testProperties.get("email");
        String password = testProperties.get("password");

        String tripType = testProperties.get("tripType");
        String origin = testProperties.get("origin");
        String destination = testProperties.get("destination");
        int daysToDeparture = Integer.parseInt(testProperties.get("daysToDeparture"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String dateDeparture = CoreFunctions.addToLongDate(currentDate, daysToDeparture);
        int passenger = Integer.parseInt(testProperties.get("passenger"));

        String event = testProperties.get("event");
        String screenName = testProperties.get("screenName");
        String vertical = testProperties.get("vertical");
        String eventAction = testProperties.get("eventAction");
        String eventLabel = testProperties.get("eventLabel").replace("-", ";");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        BusSearchPage<WebDriver> busSearchPage = BusSearchPage.getInstance(session);
        BusSearchResultPage<WebDriver> busSearchResultPage = BusSearchResultPage.getInstance(session);

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Open app as login user");
        loginPage.loginWith(loginMethod, email, password);

        step("Open bus vertical with deeplink");
        busSearchPage.openBusSearchViaDeepLink();

        step("Skip coach mark in landing page");
        busSearchPage.skipCoachMark();

        step("Select one way trip type");
        busSearchPage.switchTripType(tripType);

        step("Input origin location");
        busSearchPage.inputOriginLocation(origin);

        step("Input destination location");
        busSearchPage.inputDestinationLocation(destination);

        step("Select date");
        busSearchPage.selectDate(dateDeparture);

        step("Select pax amount");
        busSearchPage.selectPassenger(passenger);

        step("Search bus");
        busSearchPage.clickSearchBus();

        step("Skip coach mark in SRP");
        busSearchResultPage.skipCoachMark();

        sleep(ANALYTICS_WAIT_SECONDS);

        BaseAssertions busPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("searchProduct event is found", busPageAssertion.verifyEvent(Configs.MESSAGES, event), greaterThan(0));
        Assertion.assertThat("screenName is correct", busPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "screenName", screenName), is(true));
        Assertion.assertThat("vertical is correct", busPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "vertical", vertical), is(true));
        Assertion.assertThat("eventAction is correct", busPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "eventAction", eventAction), is(true));
        Assertion.assertThat("eventLabel is correct", busPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "eventLabel", eventLabel), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "BusSRP")
    @Tribe(name = "Bus")
    @Vertical(name = "Core")
    @Test
    public void testBusSRPPageVisitLogin(){
        Map<String, String> testProperties = getParam("testBusSRPPageVisitLogin");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String loginMethod = testProperties.get("loginType");
        String email = testProperties.get("email");
        String password = testProperties.get("password");

        String tripType = testProperties.get("tripType");
        String origin = testProperties.get("origin");
        String destination = testProperties.get("destination");
        int daysToDeparture = Integer.parseInt(testProperties.get("daysToDeparture"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String dateDeparture = CoreFunctions.addToLongDate(currentDate, daysToDeparture);
        int passenger = Integer.parseInt(testProperties.get("passenger"));

        String event = testProperties.get("event");
        String eventAction = testProperties.get("eventAction");
        String screenName = testProperties.get("screenName");
        String utmPage = testProperties.get("utmPage");
        String isFirstPage = testProperties.get("isFirstPage");
        String vertical = testProperties.get("vertical");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        BusSearchPage<WebDriver> busSearchPage = BusSearchPage.getInstance(session);
        BusSearchResultPage<WebDriver> busSearchResultPage = BusSearchResultPage.getInstance(session);

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Open app as login user");
        loginPage.loginWith(loginMethod, email, password);

        step("Open bus vertical with deeplink");
        busSearchPage.openBusSearchViaDeepLink();

        step("Skip coach mark in landing page");
        busSearchPage.skipCoachMark();

        step("Select one way trip type");
        busSearchPage.switchTripType(tripType);

        step("Input origin location");
        busSearchPage.inputOriginLocation(origin);

        step("Input destination location");
        busSearchPage.inputDestinationLocation(destination);

        step("Select date");
        busSearchPage.selectDate(dateDeparture);

        step("Select pax amount");
        busSearchPage.selectPassenger(passenger);

        step("Search bus");
        busSearchPage.clickSearchBus();

        step("Skip coach mark in SRP");
        busSearchResultPage.skipCoachMark();

        sleep(ANALYTICS_WAIT_SECONDS);

        BaseAssertions busPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("pageVisit event is found", busPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, screenName), is(1));
        Assertion.assertThat("eventAction is correct", busPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), is(true));
        Assertion.assertThat("screenName is correct", busPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), is(true));
        Assertion.assertThat("utmPage is correct", busPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "utmPage", utmPage), is(true));
        Assertion.assertThat("isFirstPage is correct", busPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "isFirstPage", isFirstPage), is(true));
        Assertion.assertThat("vertical is correct", busPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "vertical", vertical), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "BusBookingPage")
    @Tribe(name = "Bus")
    @Vertical(name = "Core")
    @Test
    public void testBusBookProductLogin(){
        Map<String, String> testProperties = getParam("testBusBookProductLogin");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String loginMethod = testProperties.get("loginType");
        String email = testProperties.get("email");
        String password = testProperties.get("password");

        String tripType = testProperties.get("tripType");
        String origin = testProperties.get("origin");
        String destination = testProperties.get("destination");
        int daysToDeparture = Integer.parseInt(testProperties.get("daysToDeparture"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String dateDeparture = CoreFunctions.addToLongDate(currentDate, daysToDeparture);
        int passenger = Integer.parseInt(testProperties.get("passenger"));

        String event = testProperties.get("event");
        String screenName = testProperties.get("screenName");
        String vertical = testProperties.get("vertical");
        String eventAction = testProperties.get("eventAction");
        String eventLabel = testProperties.get("eventLabel").replace("-", ";");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        BusSearchPage<WebDriver> busSearchPage = BusSearchPage.getInstance(session);
        BusSearchResultPage<WebDriver> busSearchResultPage = BusSearchResultPage.getInstance(session);
        BuspdpPage<WebDriver> busPdpPage = BuspdpPage.getInstance(session);
        BusBookingFormPage<WebDriver> busBookingFormPage = BusBookingFormPage.getInstance(session);

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Open app as login user");
        loginPage.loginWith(loginMethod, email, password);

        step("Open bus vertical with deeplink");
        busSearchPage.openBusSearchViaDeepLink();

        step("Skip coach mark in landing page");
        busSearchPage.skipCoachMark();

        step("Select one way trip type");
        busSearchPage.switchTripType(tripType);

        step("Input origin location");
        busSearchPage.inputOriginLocation(origin);

        step("Input destination location");
        busSearchPage.inputDestinationLocation(destination);

        step("Select date");
        busSearchPage.selectDate(dateDeparture);

        step("Select pax amount");
        busSearchPage.selectPassenger(passenger);

        step("Search bus");
        busSearchPage.clickSearchBus();

        step("Skip coach mark in SRP");
        busSearchResultPage.skipCoachMark();

        step("Select bus");
        busSearchResultPage.selectFirstBus();

        step("Select seat in PDP");
        busPdpPage.openSeat();
        busPdpPage.clickSelectSeatButton();

        step("Input contact detail in booking page");
        busBookingFormPage.togglePassengerSameAsContact();
        busBookingFormPage.fillPassengerData(passenger);

        step("Continue to payment page");
        busBookingFormPage.continueToPaymentPage();

        sleep(ANALYTICS_WAIT_SECONDS);

        BaseAssertions busPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("bookProduct event is found", busPageAssertion.verifyEvent(Configs.MESSAGES, event), greaterThan(0));
        Assertion.assertThat("screenName is correct", busPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "screenName", screenName), is(true));
        Assertion.assertThat("vertical is correct", busPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "vertical", vertical), is(true));
        Assertion.assertThat("eventAction is correct", busPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "eventAction", eventAction), is(true));
        Assertion.assertThat("eventLabel is correct", busPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "eventLabel", eventLabel), is(true));
    }
    
    @Author(names = {"Okta"})
    @Module(name = "BusMainPage")
    @Tribe(name = "Bus")
    @Vertical(name = "Core")
    @Test
    public void testBusMainPageVisitDeeplinkUtmGuest() {
        Map<String, String> testProperties = getParam("testBusMainPageVisitDeeplinkUtmGuest");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String deeplink = "tiket://m.tiket.com/bus-travel/?";

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
        BusSearchPage<WebDriver> busSearchPage = BusSearchPage.getInstance(session);

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Open app as guest");
        loginPage.loginAsGuest();

        step("Open bus vertical via deeplink");
        String utmProperties = analyticsHomePage.getUtmProperties(utmCampaign, utmContent, utmId, utmMedium, utmSource, utmTerm);
        analyticsHomePage.openDeeplink(deeplink + utmProperties);
        busSearchPage.skipCoachMark();

        sleep(ANALYTICS_WAIT_SECONDS);

        BaseAssertions busPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("utmAttributedAt is not null", busPageAssertion.verifyUserProperty(Configs.MESSAGES, "utmAttributedAt"), is(true));
        Assertion.assertThat("utmCampaign is correct", busPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmCampaign", utmCampaign), is(true));
        Assertion.assertThat("utmContent is correct", busPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmContent", utmContent), is(true));
        Assertion.assertThat("utmExternal is correct", busPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmExternal", utmExternal), is(true));
        Assertion.assertThat("utmId is correct", busPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmId", utmId), is(true));
        Assertion.assertThat("utmMedium is correct", busPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmMedium", utmMedium), is(true));
        Assertion.assertThat("utmSource is correct", busPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmSource", utmSource), is(true));
        Assertion.assertThat("utmTerm is correct", busPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmTerm", utmTerm), is(true));
        Assertion.assertThat("userId is null", busPageAssertion.verifyUserProperty(Configs.MESSAGES, "userId"), is(false));

        Assertion.assertThat("pageVisit event is found", busPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, screenName), is(1));
        Assertion.assertThat("eventAction is correct", busPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), is(true));
        Assertion.assertThat("screenName is correct", busPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), is(true));
        Assertion.assertThat("utmPage is correct", busPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "utmPage", utmPage), is(true));
        Assertion.assertThat("isFirstPage is correct", busPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "isFirstPage", isFirstPage), is(true));
        Assertion.assertThat("vertical is correct", busPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "vertical", vertical), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "BusMainPage")
    @Tribe(name = "Bus")
    @Vertical(name = "Core")
    @Test
    public void testBusMainPageVisitDeeplinkUtmLogin() {
        Map<String, String> testProperties = getParam("testBusMainPageVisitDeeplinkUtmLogin");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String loginType = testProperties.get("loginType");
        String email = testProperties.get("email");
        String password = testProperties.get("password");
        String deeplink = "tiket://m.tiket.com/bus-travel/?";

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
        BusSearchPage<WebDriver> busSearchPage = BusSearchPage.getInstance(session);

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Open app as login user");
        loginPage.loginWith(loginType, email, password);

        step("Open bus vertical via deeplink");
        String utmProperties = analyticsHomePage.getUtmProperties(utmCampaign, utmContent, utmId, utmMedium, utmSource, utmTerm);
        analyticsHomePage.openDeeplink(deeplink + utmProperties);
        busSearchPage.skipCoachMark();

        sleep(ANALYTICS_WAIT_SECONDS);

        BaseAssertions busPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("utmAttributedAt is not null", busPageAssertion.verifyUserProperty(Configs.MESSAGES, "utmAttributedAt"), is(true));
        Assertion.assertThat("utmCampaign is correct", busPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmCampaign", utmCampaign), is(true));
        Assertion.assertThat("utmContent is correct", busPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmContent", utmContent), is(true));
        Assertion.assertThat("utmExternal is correct", busPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmExternal", utmExternal), is(true));
        Assertion.assertThat("utmId is correct", busPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmId", utmId), is(true));
        Assertion.assertThat("utmMedium is correct", busPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmMedium", utmMedium), is(true));
        Assertion.assertThat("utmSource is correct", busPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmSource", utmSource), is(true));
        Assertion.assertThat("utmTerm is correct", busPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmTerm", utmTerm), is(true));
        Assertion.assertThat("userId is not null", busPageAssertion.verifyUserProperty(Configs.MESSAGES, "userId"), is(true));

        Assertion.assertThat("pageVisit event is found", busPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, screenName), is(1));
        Assertion.assertThat("eventAction is correct", busPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), is(true));
        Assertion.assertThat("screenName is correct", busPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), is(true));
        Assertion.assertThat("utmPage is correct", busPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "utmPage", utmPage), is(true));
        Assertion.assertThat("isFirstPage is correct", busPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "isFirstPage", isFirstPage), is(true));
        Assertion.assertThat("vertical is correct", busPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "vertical", vertical), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "BusSRP")
    @Tribe(name = "Bus")
    @Vertical(name = "Core")
    @Test
    public void testBusSRPPageVisitDeeplinkUtmGuest() {
        Map<String, String> testProperties = getParam("testBusSRPPageVisitDeeplinkUtmGuest");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String originId = testProperties.get("originId");
        String destinationId = testProperties.get("destinationId");
        int daysToDepartureDate = Integer.parseInt(testProperties.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentDateYYYY_MM_DD();
        String departureDate = CoreFunctions.addToDateYYYY_MM_DD(currentDate, daysToDepartureDate);
        String deeplink = "tiket://m.tiket.com/bus-travel/search?origin=" + originId + "&destination=" + destinationId + "&tripType=oneway&journeyType=depart&departureDate=" + departureDate + "&returnDate=" + departureDate + "&adult=1&";

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

        step("Open bus SRP via deeplink");
        String utmProperties = analyticsHomePage.getUtmProperties(utmCampaign, utmContent, utmId, utmMedium, utmSource, utmTerm);
        analyticsHomePage.openDeeplink(deeplink + utmProperties);

        sleep(ANALYTICS_WAIT_SECONDS);

        BaseAssertions busPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("utmAttributedAt is not null", busPageAssertion.verifyUserProperty(Configs.MESSAGES, "utmAttributedAt"), is(true));
        Assertion.assertThat("utmCampaign is correct", busPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmCampaign", utmCampaign), is(true));
        Assertion.assertThat("utmContent is correct", busPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmContent", utmContent), is(true));
        Assertion.assertThat("utmExternal is correct", busPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmExternal", utmExternal), is(true));
        Assertion.assertThat("utmId is correct", busPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmId", utmId), is(true));
        Assertion.assertThat("utmMedium is correct", busPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmMedium", utmMedium), is(true));
        Assertion.assertThat("utmSource is correct", busPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmSource", utmSource), is(true));
        Assertion.assertThat("utmTerm is correct", busPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmTerm", utmTerm), is(true));
        Assertion.assertThat("userId is null", busPageAssertion.verifyUserProperty(Configs.MESSAGES, "userId"), is(false));

        Assertion.assertThat("pageVisit event is found", busPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, screenName), is(1));
        Assertion.assertThat("eventAction is correct", busPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), is(true));
        Assertion.assertThat("screenName is correct", busPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), is(true));
        Assertion.assertThat("utmPage is not null", busPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "utmPage", utmPage), is(true));
        Assertion.assertThat("isFirstPage is correct", busPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "isFirstPage", isFirstPage), is(true));
        Assertion.assertThat("vertical is correct", busPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "vertical", vertical), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "BusSRP")
    @Tribe(name = "Bus")
    @Vertical(name = "Core")
    @Test
    public void testBusSRPPageVisitDeeplinkUtmLogin() {
        Map<String, String> testProperties = getParam("testBusSRPPageVisitDeeplinkUtmLogin");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String loginType = testProperties.get("loginType");
        String email = testProperties.get("email");
        String password = testProperties.get("password");

        String originId = testProperties.get("originId");
        String destinationId = testProperties.get("destinationId");
        int daysToDepartureDate = Integer.parseInt(testProperties.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentDateYYYY_MM_DD();
        String departureDate = CoreFunctions.addToDateYYYY_MM_DD(currentDate, daysToDepartureDate);
        String deeplink = "tiket://m.tiket.com/bus-travel/search?origin=" + originId + "&destination=" + destinationId + "&tripType=oneway&journeyType=depart&departureDate=" + departureDate + "&returnDate=" + departureDate + "&adult=1&";

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

        step("Open bus SRP via deeplink");
        String utmProperties = analyticsHomePage.getUtmProperties(utmCampaign, utmContent, utmId, utmMedium, utmSource, utmTerm);
        analyticsHomePage.openDeeplink(deeplink + utmProperties);

        sleep(ANALYTICS_WAIT_SECONDS);

        BaseAssertions busPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("utmAttributedAt is not null", busPageAssertion.verifyUserProperty(Configs.MESSAGES, "utmAttributedAt"), is(true));
        Assertion.assertThat("utmCampaign is correct", busPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmCampaign", utmCampaign), is(true));
        Assertion.assertThat("utmContent is correct", busPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmContent", utmContent), is(true));
        Assertion.assertThat("utmExternal is correct", busPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmExternal", utmExternal), is(true));
        Assertion.assertThat("utmId is correct", busPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmId", utmId), is(true));
        Assertion.assertThat("utmMedium is correct", busPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmMedium", utmMedium), is(true));
        Assertion.assertThat("utmSource is correct", busPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmSource", utmSource), is(true));
        Assertion.assertThat("utmTerm is correct", busPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmTerm", utmTerm), is(true));
        Assertion.assertThat("userId is not null", busPageAssertion.verifyUserProperty(Configs.MESSAGES, "userId"), is(true));

        Assertion.assertThat("pageVisit event is found", busPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, screenName), is(1));
        Assertion.assertThat("eventAction is correct", busPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), is(true));
        Assertion.assertThat("screenName is correct", busPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), is(true));
        Assertion.assertThat("utmPage is not null", busPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "utmPage", utmPage), is(true));
        Assertion.assertThat("isFirstPage is correct", busPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "isFirstPage", isFirstPage), is(true));
        Assertion.assertThat("vertical is correct", busPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "vertical", vertical), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "BusMainPage")
    @Tribe(name = "Bus")
    @Vertical(name = "Core")
    @Test
    public void testBusMainPageVisitDeeplinkNonUtmGuest() {
        Map<String, String> testProperties = getParam("testBusMainPageVisitDeeplinkNonUtmGuest");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String deeplink = "tiket://m.tiket.com/bus-travel";

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
        BusSearchPage<WebDriver> busSearchPage = BusSearchPage.getInstance(session);

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Open app as guest");
        loginPage.loginAsGuest();

        step("Open bus vertical via deeplink");
        analyticsHomePage.openDeeplink(deeplink);
        busSearchPage.skipCoachMark();

        sleep(ANALYTICS_WAIT_SECONDS);

        BaseAssertions busPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("utmAttributedAt is not null", busPageAssertion.verifyUserProperty(Configs.MESSAGES, "utmAttributedAt"), is(true));
        Assertion.assertThat("utmCampaign is correct", busPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmCampaign", utmCampaign), is(true));
        Assertion.assertThat("utmContent is correct", busPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmContent", utmContent), is(true));
        Assertion.assertThat("utmExternal is correct", busPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmExternal", utmExternal), is(true));
        Assertion.assertThat("utmId is correct", busPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmId", utmId), is(true));
        Assertion.assertThat("utmMedium is correct", busPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmMedium", utmMedium), is(true));
        Assertion.assertThat("utmSource is correct", busPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmSource", utmSource), is(true));
        Assertion.assertThat("utmTerm is correct", busPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmTerm", utmTerm), is(true));
        Assertion.assertThat("userId is null", busPageAssertion.verifyUserProperty(Configs.MESSAGES, "userId"), is(false));

        Assertion.assertThat("pageVisit event is found", busPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, screenName), is(1));
        Assertion.assertThat("eventAction is correct", busPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), is(true));
        Assertion.assertThat("screenName is correct", busPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), is(true));
        Assertion.assertThat("utmPage is correct", busPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "utmPage", utmPage), is(true));
        Assertion.assertThat("isFirstPage is correct", busPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "isFirstPage", isFirstPage), is(true));
        Assertion.assertThat("vertical is correct", busPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "vertical", vertical), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "BusMainPage")
    @Tribe(name = "Bus")
    @Vertical(name = "Core")
    @Test
    public void testBusMainPageVisitDeeplinkNonUtmLogin() {
        Map<String, String> testProperties = getParam("testBusMainPageVisitDeeplinkNonUtmLogin");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String loginType = testProperties.get("loginType");
        String email = testProperties.get("email");
        String password = testProperties.get("password");
        String deeplink = "tiket://m.tiket.com/bus-travel";

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
        BusSearchPage<WebDriver> busSearchPage = BusSearchPage.getInstance(session);

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Open app as login user");
        loginPage.loginWith(loginType, email, password);

        step("Open bus vertical via deeplink");
        analyticsHomePage.openDeeplink(deeplink);
        busSearchPage.skipCoachMark();

        sleep(ANALYTICS_WAIT_SECONDS);

        BaseAssertions busPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("utmAttributedAt is not null", busPageAssertion.verifyUserProperty(Configs.MESSAGES, "utmAttributedAt"), is(true));
        Assertion.assertThat("utmCampaign is correct", busPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmCampaign", utmCampaign), is(true));
        Assertion.assertThat("utmContent is correct", busPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmContent", utmContent), is(true));
        Assertion.assertThat("utmExternal is correct", busPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmExternal", utmExternal), is(true));
        Assertion.assertThat("utmId is correct", busPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmId", utmId), is(true));
        Assertion.assertThat("utmMedium is correct", busPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmMedium", utmMedium), is(true));
        Assertion.assertThat("utmSource is correct", busPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmSource", utmSource), is(true));
        Assertion.assertThat("utmTerm is correct", busPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmTerm", utmTerm), is(true));
        Assertion.assertThat("userId is not null", busPageAssertion.verifyUserProperty(Configs.MESSAGES, "userId"), is(true));

        Assertion.assertThat("pageVisit event is found", busPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, screenName), is(1));
        Assertion.assertThat("eventAction is correct", busPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), is(true));
        Assertion.assertThat("screenName is correct", busPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), is(true));
        Assertion.assertThat("utmPage is correct", busPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "utmPage", utmPage), is(true));
        Assertion.assertThat("isFirstPage is correct", busPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "isFirstPage", isFirstPage), is(true));
        Assertion.assertThat("vertical is correct", busPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "vertical", vertical), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "BusSRP")
    @Tribe(name = "Bus")
    @Vertical(name = "Core")
    @Test
    public void testBusSRPPageVisitDeeplinkNonUtmGuest() {
        Map<String, String> testProperties = getParam("testBusSRPPageVisitDeeplinkNonUtmGuest");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String originId = testProperties.get("originId");
        String destinationId = testProperties.get("destinationId");
        int daysToDepartureDate = Integer.parseInt(testProperties.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentDateYYYY_MM_DD();
        String departureDate = CoreFunctions.addToDateYYYY_MM_DD(currentDate, daysToDepartureDate);
        String deeplink = "tiket://m.tiket.com/bus-travel/search?origin=" + originId + "&destination=" + destinationId + "&tripType=oneway&journeyType=depart&departureDate=" + departureDate + "&returnDate=" + departureDate + "&adult=1&";

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

        step("Open bus SRP via deeplink");
        analyticsHomePage.openDeeplink(deeplink);

        sleep(ANALYTICS_WAIT_SECONDS);

        BaseAssertions busPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("utmAttributedAt is not null", busPageAssertion.verifyUserProperty(Configs.MESSAGES, "utmAttributedAt"), is(true));
        Assertion.assertThat("utmCampaign is correct", busPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmCampaign", utmCampaign), is(true));
        Assertion.assertThat("utmContent is correct", busPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmContent", utmContent), is(true));
        Assertion.assertThat("utmExternal is correct", busPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmExternal", utmExternal), is(true));
        Assertion.assertThat("utmId is correct", busPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmId", utmId), is(true));
        Assertion.assertThat("utmMedium is correct", busPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmMedium", utmMedium), is(true));
        Assertion.assertThat("utmSource is correct", busPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmSource", utmSource), is(true));
        Assertion.assertThat("utmTerm is correct", busPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmTerm", utmTerm), is(true));
        Assertion.assertThat("userId is null", busPageAssertion.verifyUserProperty(Configs.MESSAGES, "userId"), is(false));

        Assertion.assertThat("pageVisit event is found", busPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, screenName), is(1));
        Assertion.assertThat("eventAction is correct", busPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), is(true));
        Assertion.assertThat("screenName is correct", busPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), is(true));
        Assertion.assertThat("utmPage is not null", busPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "utmPage", utmPage), is(true));
        Assertion.assertThat("isFirstPage is correct", busPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "isFirstPage", isFirstPage), is(true));
        Assertion.assertThat("vertical is correct", busPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "vertical", vertical), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "BusSRP")
    @Tribe(name = "Bus")
    @Vertical(name = "Core")
    @Test
    public void testBusSRPPageVisitDeeplinkNonUtmLogin() {
        Map<String, String> testProperties = getParam("testBusSRPPageVisitDeeplinkNonUtmLogin");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String loginType = testProperties.get("loginType");
        String email = testProperties.get("email");
        String password = testProperties.get("password");

        String originId = testProperties.get("originId");
        String destinationId = testProperties.get("destinationId");
        int daysToDepartureDate = Integer.parseInt(testProperties.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentDateYYYY_MM_DD();
        String departureDate = CoreFunctions.addToDateYYYY_MM_DD(currentDate, daysToDepartureDate);
        String deeplink = "tiket://m.tiket.com/bus-travel/search?origin=" + originId + "&destination=" + destinationId + "&tripType=oneway&journeyType=depart&departureDate=" + departureDate + "&returnDate=" + departureDate + "&adult=1&";

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

        step("Open bus SRP via deeplink");
        analyticsHomePage.openDeeplink(deeplink);

        sleep(ANALYTICS_WAIT_SECONDS);

        BaseAssertions busPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("utmAttributedAt is not null", busPageAssertion.verifyUserProperty(Configs.MESSAGES, "utmAttributedAt"), is(true));
        Assertion.assertThat("utmCampaign is correct", busPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmCampaign", utmCampaign), is(true));
        Assertion.assertThat("utmContent is correct", busPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmContent", utmContent), is(true));
        Assertion.assertThat("utmExternal is correct", busPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmExternal", utmExternal), is(true));
        Assertion.assertThat("utmId is correct", busPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmId", utmId), is(true));
        Assertion.assertThat("utmMedium is correct", busPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmMedium", utmMedium), is(true));
        Assertion.assertThat("utmSource is correct", busPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmSource", utmSource), is(true));
        Assertion.assertThat("utmTerm is correct", busPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmTerm", utmTerm), is(true));
        Assertion.assertThat("userId is not null", busPageAssertion.verifyUserProperty(Configs.MESSAGES, "userId"), is(true));

        Assertion.assertThat("pageVisit event is found", busPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, screenName), is(1));
        Assertion.assertThat("eventAction is correct", busPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), is(true));
        Assertion.assertThat("screenName is correct", busPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), is(true));
        Assertion.assertThat("utmPage is not null", busPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "utmPage", utmPage), is(true));
        Assertion.assertThat("isFirstPage is correct", busPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "isFirstPage", isFirstPage), is(true));
        Assertion.assertThat("vertical is correct", busPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "vertical", vertical), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "BusPDP")
    @Tribe(name = "Bus")
    @Vertical(name = "Core")
    @Test
    public void testBusPDPPageVisitDeeplinkUtmGuest() {
        Map<String, String> testProperties = getParam("testBusPDPPageVisitDeeplinkUtmGuest");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String originId = testProperties.get("originId");
        String destinationId = testProperties.get("destinationId");
        int daysToDepartureDate = Integer.parseInt(testProperties.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentDateYYYY_MM_DD();
        String departureDate = CoreFunctions.addToDateYYYY_MM_DD(currentDate, daysToDepartureDate);
        String deeplink = "tiket://m.tiket.com/bus-travel/detail?origin=" + originId + "&destination=" + destinationId + "&tripType=oneway&journeyType=depart&departureDate=" + departureDate + "&returnDate=" + departureDate + "&adult=1&";

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

        step("Open bus PDP via deeplink");
        String utmProperties = analyticsHomePage.getUtmProperties(utmCampaign, utmContent, utmId, utmMedium, utmSource, utmTerm);
        analyticsHomePage.openDeeplink(deeplink + utmProperties);

        sleep(ANALYTICS_WAIT_SECONDS);

        BaseAssertions busPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("utmAttributedAt is not null", busPageAssertion.verifyUserProperty(Configs.MESSAGES, "utmAttributedAt"), is(true));
        Assertion.assertThat("utmCampaign is correct", busPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmCampaign", utmCampaign), is(true));
        Assertion.assertThat("utmContent is correct", busPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmContent", utmContent), is(true));
        Assertion.assertThat("utmExternal is correct", busPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmExternal", utmExternal), is(true));
        Assertion.assertThat("utmId is correct", busPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmId", utmId), is(true));
        Assertion.assertThat("utmMedium is correct", busPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmMedium", utmMedium), is(true));
        Assertion.assertThat("utmSource is correct", busPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmSource", utmSource), is(true));
        Assertion.assertThat("utmTerm is correct", busPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmTerm", utmTerm), is(true));
        Assertion.assertThat("userId is null", busPageAssertion.verifyUserProperty(Configs.MESSAGES, "userId"), is(false));

        Assertion.assertThat("pageVisit event is found", busPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, screenName), is(1));
        Assertion.assertThat("eventAction is correct", busPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), is(true));
        Assertion.assertThat("screenName is correct", busPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), is(true));
        Assertion.assertThat("utmPage is not null", busPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "utmPage", utmPage), is(true));
        Assertion.assertThat("isFirstPage is correct", busPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "isFirstPage", isFirstPage), is(true));
        Assertion.assertThat("vertical is correct", busPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "vertical", vertical), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "BusPDP")
    @Tribe(name = "Bus")
    @Vertical(name = "Core")
    @Test
    public void testBusPDPPageVisitDeeplinkUtmLogin() {
        Map<String, String> testProperties = getParam("testBusPDPPageVisitDeeplinkUtmLogin");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String loginType = testProperties.get("loginType");
        String email = testProperties.get("email");
        String password = testProperties.get("password");

        String originId = testProperties.get("originId");
        String destinationId = testProperties.get("destinationId");
        int daysToDepartureDate = Integer.parseInt(testProperties.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentDateYYYY_MM_DD();
        String departureDate = CoreFunctions.addToDateYYYY_MM_DD(currentDate, daysToDepartureDate);
        String deeplink = "tiket://m.tiket.com/bus-travel/detail?origin=" + originId + "&destination=" + destinationId + "&tripType=oneway&journeyType=depart&departureDate=" + departureDate + "&returnDate=" + departureDate + "&adult=1&";

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

        step("Open bus PDP via deeplink");
        String utmProperties = analyticsHomePage.getUtmProperties(utmCampaign, utmContent, utmId, utmMedium, utmSource, utmTerm);
        analyticsHomePage.openDeeplink(deeplink + utmProperties);

        sleep(ANALYTICS_WAIT_SECONDS);

        BaseAssertions busPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("utmAttributedAt is not null", busPageAssertion.verifyUserProperty(Configs.MESSAGES, "utmAttributedAt"), is(true));
        Assertion.assertThat("utmCampaign is correct", busPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmCampaign", utmCampaign), is(true));
        Assertion.assertThat("utmContent is correct", busPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmContent", utmContent), is(true));
        Assertion.assertThat("utmExternal is correct", busPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmExternal", utmExternal), is(true));
        Assertion.assertThat("utmId is correct", busPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmId", utmId), is(true));
        Assertion.assertThat("utmMedium is correct", busPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmMedium", utmMedium), is(true));
        Assertion.assertThat("utmSource is correct", busPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmSource", utmSource), is(true));
        Assertion.assertThat("utmTerm is correct", busPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmTerm", utmTerm), is(true));
        Assertion.assertThat("userId is not null", busPageAssertion.verifyUserProperty(Configs.MESSAGES, "userId"), is(true));

        Assertion.assertThat("pageVisit event is found", busPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, screenName), is(1));
        Assertion.assertThat("eventAction is correct", busPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), is(true));
        Assertion.assertThat("screenName is correct", busPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), is(true));
        Assertion.assertThat("utmPage is not null", busPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "utmPage", utmPage), is(true));
        Assertion.assertThat("isFirstPage is correct", busPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "isFirstPage", isFirstPage), is(true));
        Assertion.assertThat("vertical is correct", busPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "vertical", vertical), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "BusPDP")
    @Tribe(name = "Bus")
    @Vertical(name = "Core")
    @Test
    public void testBusPDPPageVisitDeeplinkNonUtmGuest() {
        Map<String, String> testProperties = getParam("testBusPDPPageVisitDeeplinkNonUtmGuest");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String originId = testProperties.get("originId");
        String destinationId = testProperties.get("destinationId");
        int daysToDepartureDate = Integer.parseInt(testProperties.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentDateYYYY_MM_DD();
        String departureDate = CoreFunctions.addToDateYYYY_MM_DD(currentDate, daysToDepartureDate);
        String deeplink = "tiket://m.tiket.com/bus-travel/detail?origin=" + originId + "&destination=" + destinationId + "&tripType=oneway&journeyType=depart&departureDate=" + departureDate + "&returnDate=" + departureDate + "&adult=1&";

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

        step("Open bus PDP via deeplink");
        analyticsHomePage.openDeeplink(deeplink);

        sleep(ANALYTICS_WAIT_SECONDS);

        BaseAssertions busPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("utmAttributedAt is not null", busPageAssertion.verifyUserProperty(Configs.MESSAGES, "utmAttributedAt"), is(true));
        Assertion.assertThat("utmCampaign is correct", busPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmCampaign", utmCampaign), is(true));
        Assertion.assertThat("utmContent is correct", busPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmContent", utmContent), is(true));
        Assertion.assertThat("utmExternal is correct", busPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmExternal", utmExternal), is(true));
        Assertion.assertThat("utmId is correct", busPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmId", utmId), is(true));
        Assertion.assertThat("utmMedium is correct", busPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmMedium", utmMedium), is(true));
        Assertion.assertThat("utmSource is correct", busPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmSource", utmSource), is(true));
        Assertion.assertThat("utmTerm is correct", busPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmTerm", utmTerm), is(true));
        Assertion.assertThat("userId is null", busPageAssertion.verifyUserProperty(Configs.MESSAGES, "userId"), is(false));

        Assertion.assertThat("pageVisit event is found", busPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, screenName), is(1));
        Assertion.assertThat("eventAction is correct", busPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), is(true));
        Assertion.assertThat("screenName is correct", busPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), is(true));
        Assertion.assertThat("utmPage is not null", busPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "utmPage", utmPage), is(true));
        Assertion.assertThat("isFirstPage is correct", busPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "isFirstPage", isFirstPage), is(true));
        Assertion.assertThat("vertical is correct", busPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "vertical", vertical), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "BusPDP")
    @Tribe(name = "Bus")
    @Vertical(name = "Core")
    @Test
    public void testBusPDPPageVisitDeeplinkNonUtmLogin() {
        Map<String, String> testProperties = getParam("testBusPDPPageVisitDeeplinkNonUtmLogin");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String loginType = testProperties.get("loginType");
        String email = testProperties.get("email");
        String password = testProperties.get("password");

        String originId = testProperties.get("originId");
        String destinationId = testProperties.get("destinationId");
        int daysToDepartureDate = Integer.parseInt(testProperties.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentDateYYYY_MM_DD();
        String departureDate = CoreFunctions.addToDateYYYY_MM_DD(currentDate, daysToDepartureDate);
        String deeplink = "tiket://m.tiket.com/bus-travel/detail?origin=" + originId + "&destination=" + destinationId + "&tripType=oneway&journeyType=depart&departureDate=" + departureDate + "&returnDate=" + departureDate + "&adult=1&";

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

        step("Open bus PDP via deeplink");
        analyticsHomePage.openDeeplink(deeplink);

        sleep(ANALYTICS_WAIT_SECONDS);

        BaseAssertions busPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("utmAttributedAt is not null", busPageAssertion.verifyUserProperty(Configs.MESSAGES, "utmAttributedAt"), is(true));
        Assertion.assertThat("utmCampaign is correct", busPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmCampaign", utmCampaign), is(true));
        Assertion.assertThat("utmContent is correct", busPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmContent", utmContent), is(true));
        Assertion.assertThat("utmExternal is correct", busPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmExternal", utmExternal), is(true));
        Assertion.assertThat("utmId is correct", busPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmId", utmId), is(true));
        Assertion.assertThat("utmMedium is correct", busPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmMedium", utmMedium), is(true));
        Assertion.assertThat("utmSource is correct", busPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmSource", utmSource), is(true));
        Assertion.assertThat("utmTerm is correct", busPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmTerm", utmTerm), is(true));
        Assertion.assertThat("userId is not null", busPageAssertion.verifyUserProperty(Configs.MESSAGES, "userId"), is(true));

        Assertion.assertThat("pageVisit event is found", busPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, screenName), is(1));
        Assertion.assertThat("eventAction is correct", busPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), is(true));
        Assertion.assertThat("screenName is correct", busPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), is(true));
        Assertion.assertThat("utmPage is not null", busPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "utmPage", utmPage), is(true));
        Assertion.assertThat("isFirstPage is correct", busPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "isFirstPage", isFirstPage), is(true));
        Assertion.assertThat("vertical is correct", busPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "vertical", vertical), is(true));
    }
}