package com.tiket.analytics.app.test.train;

import com.tiket.analytics.BaseAnalyticsTest;
import com.tiket.analytics.assertions.BaseAssertions;
import com.tiket.analytics.app.home.AnalyticsHomePage;
import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Session;
import com.tiket.page.home.HomePage;
import com.tiket.page.login.LoginPage;
import com.tiket.page.train.bookingform.TrainBookingPage;
import com.tiket.page.train.searchpage.TrainSearchPage;
import com.tiket.page.train.searchresultpage.TrainSearchResultPage;
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

public class TrainPageTest extends BaseAnalyticsTest {

    @Author(names = {"Okta"})
    @Module(name = "TrainMainPage")
    @Tribe(name = "Train")
    @Vertical(name = "Core")
    @Test
    public void testTrainMainPageVisitGuest() {
        Map<String, String> testProperties = getParam("testTrainMainPageVisitGuest");
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

        step("Open train vertical");
        homePage.openVerticalPage("Trains");

        sleep(40);

        BaseAssertions trainPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("pageVisit event is found", trainPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, screenName), is(1));
        Assertion.assertThat("eventAction is correct", trainPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), is(true));
        Assertion.assertThat("screenName is correct", trainPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), is(true));
        Assertion.assertThat("utmPage is correct", trainPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "utmPage", utmPage), is(true));
        Assertion.assertThat("isFirstPage is correct", trainPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "isFirstPage", isFirstPage), is(true));
        Assertion.assertThat("vertical is correct", trainPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "vertical", vertical), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "TrainSRP")
    @Tribe(name = "Train")
    @Vertical(name = "Core")
    @Test
    public void testTrainSRPPageVisitGuest() {
        Map<String, String> testProperties = getParam("testTrainSRPPageVisitGuest");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String tripType = testProperties.get("tripType");
        String originStation = testProperties.get("originStation");
        String destinationStation = testProperties.get("destinationStation");

        String event = testProperties.get("event");
        String eventAction = testProperties.get("eventAction");
        String screenName = testProperties.get("screenName");
        String utmPage = testProperties.get("utmPage");
        String isFirstPage = testProperties.get("isFirstPage");
        String vertical = testProperties.get("vertical");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TrainSearchPage<WebDriver> trainSearchPage = TrainSearchPage.getInstance(session);

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Open app as guest");
        loginPage.loginAsGuest();

        step("Open train vertical");
        homePage.openVerticalPage("Trains");

        step("Select origin station");
        trainSearchPage.selectOrigin(originStation);

        step("Select destination station");
        trainSearchPage.selectDestination(destinationStation);

        step("Toggle one way trip");
        trainSearchPage.toggleTripType(tripType);

        step("Search train");
        trainSearchPage.searchTrain();

        step("Skip pop up");
        trainSearchPage.skipPopup();

        sleep(40);

        BaseAssertions trainPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("pageVisit event is found", trainPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, screenName), is(1));
        Assertion.assertThat("eventAction is correct", trainPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), is(true));
        Assertion.assertThat("screenName is correct", trainPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), is(true));
        Assertion.assertThat("utmPage is correct", trainPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "utmPage", utmPage), is(true));
        Assertion.assertThat("isFirstPage is correct", trainPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "isFirstPage", isFirstPage), is(true));
        Assertion.assertThat("vertical is correct", trainPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "vertical", vertical), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "TrainBookingPage")
    @Tribe(name = "Train")
    @Vertical(name = "Core")
    @Test
    public void testTrainBookingPageVisitGuest() {
        Map<String, String> testProperties = getParam("testTrainBookingPageVisitGuest");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String tripType = testProperties.get("tripType");
        String originStation = testProperties.get("originStation");
        String destinationStation = testProperties.get("destinationStation");

        String event = testProperties.get("event");
        String eventAction = testProperties.get("eventAction");
        String screenName = testProperties.get("screenName");
        String utmPage = testProperties.get("utmPage");
        String isFirstPage = testProperties.get("isFirstPage");
        String vertical = testProperties.get("vertical");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TrainSearchPage<WebDriver> trainSearchPage = TrainSearchPage.getInstance(session);
        TrainSearchResultPage<WebDriver> trainSearchResultPage = TrainSearchResultPage.getInstance(session);

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Open app as guest");
        loginPage.loginAsGuest();

        step("Open train vertical");
        homePage.openVerticalPage("Trains");

        step("Select origin station");
        trainSearchPage.selectOrigin(originStation);

        step("Select destination station");
        trainSearchPage.selectDestination(destinationStation);

        step("Toggle one way trip");
        trainSearchPage.toggleTripType(tripType);

        step("Search train");
        trainSearchPage.searchTrain();

        step("Skip pop up");
        trainSearchPage.skipPopup();

        step("Select train");
        trainSearchResultPage.selectTrain();

        sleep(40);

        BaseAssertions trainPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("pageVisit event is found", trainPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, screenName), is(1));
        Assertion.assertThat("eventAction is correct", trainPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), is(true));
        Assertion.assertThat("screenName is correct", trainPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), is(true));
        Assertion.assertThat("utmPage is correct", trainPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "utmPage", utmPage), is(true));
        Assertion.assertThat("isFirstPage is correct", trainPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "isFirstPage", isFirstPage), is(true));
        Assertion.assertThat("vertical is correct", trainPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "vertical", vertical), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "TrainMainPage")
    @Tribe(name = "Train")
    @Vertical(name = "Core")
    @Test
    public void testTrainEnterVerticalGuest() {
        Map<String, String> testProperties = getParam("testTrainEnterVerticalGuest");
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

        step("Open train vertical");
        homePage.openVerticalPage("Trains");

        sleep(40);

        BaseAssertions trainPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("enterVertical event is found", trainPageAssertion.verifyEvent(Configs.MESSAGES, event), is(1));
        Assertion.assertThat("screenName is correct", trainPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "screenName", screenName), is(true));
        Assertion.assertThat("vertical is correct", trainPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "vertical", vertical), is(true));
        Assertion.assertThat("eventAction is correct", trainPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "eventAction", eventAction), is(true));
        Assertion.assertThat("eventLabel is correct", trainPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "eventLabel", eventLabel), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "TrainSRP")
    @Tribe(name = "Train")
    @Vertical(name = "Core")
    @Test
    public void testTrainSearchLoadedGuest() {
        Map<String, String> testProperties = getParam("testTrainSearchLoadedGuest");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String tripType = testProperties.get("tripType");
        String originStation = testProperties.get("originStation");
        String destinationStation = testProperties.get("destinationStation");

        String event = testProperties.get("event");
        String screenName = testProperties.get("screenName");
        String vertical = testProperties.get("vertical");
        String eventAction = testProperties.get("eventAction");
        String eventLabel = testProperties.get("eventLabel");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TrainSearchPage<WebDriver> trainSearchPage = TrainSearchPage.getInstance(session);

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Open app as guest");
        loginPage.loginAsGuest();

        step("Open train vertical");
        homePage.openVerticalPage("Trains");

        step("Select origin station");
        trainSearchPage.selectOrigin(originStation);

        step("Select destination station");
        trainSearchPage.selectDestination(destinationStation);

        step("Toggle one way trip");
        trainSearchPage.toggleTripType(tripType);

        step("Search train");
        trainSearchPage.searchTrain();

        step("Skip pop up");
        trainSearchPage.skipPopup();

        sleep(40);

        BaseAssertions trainPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("searchLoaded event is found", trainPageAssertion.verifyEvent(Configs.MESSAGES, event), is(1));
        Assertion.assertThat("screenName is correct", trainPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "screenName", screenName), is(true));
        Assertion.assertThat("vertical is correct", trainPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "vertical", vertical), is(true));
        Assertion.assertThat("eventAction is correct", trainPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "eventAction", eventAction), is(true));
        Assertion.assertThat("eventLabel is correct", trainPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "eventLabel", eventLabel), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "TrainSRP")
    @Tribe(name = "Train")
    @Vertical(name = "Core")
    @Test
    public void testTrainSearchProductGuest() {
        Map<String, String> testProperties = getParam("testTrainSearchProductGuest");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String tripType = testProperties.get("tripType");
        String originStation = testProperties.get("originStation");
        String destinationStation = testProperties.get("destinationStation");

        String event = testProperties.get("event");
        String screenName = testProperties.get("screenName");
        String vertical = testProperties.get("vertical");
        String eventAction = testProperties.get("eventAction");
        String eventDescription = testProperties.get("eventDescription");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TrainSearchPage<WebDriver> trainSearchPage = TrainSearchPage.getInstance(session);

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Open app as guest");
        loginPage.loginAsGuest();

        step("Open train vertical");
        homePage.openVerticalPage("Trains");

        step("Select origin station");
        trainSearchPage.selectOrigin(originStation);

        step("Select destination station");
        trainSearchPage.selectDestination(destinationStation);

        step("Toggle one way trip");
        trainSearchPage.toggleTripType(tripType);

        step("Search train");
        trainSearchPage.searchTrain();

        step("Skip pop up");
        trainSearchPage.skipPopup();

        sleep(40);

        BaseAssertions trainPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("searchProduct event is found", trainPageAssertion.verifyEvent(Configs.MESSAGES, event), is(1));
        Assertion.assertThat("screenName is correct", trainPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "screenName", screenName), is(true));
        Assertion.assertThat("vertical is correct", trainPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "vertical", vertical), is(true));
        Assertion.assertThat("eventAction is correct", trainPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "eventAction", eventAction), is(true));
        Assertion.assertThat("eventDescription is correct", trainPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "eventDescription", eventDescription), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "TrainBookingPage")
    @Tribe(name = "Train")
    @Vertical(name = "Core")
    @Test
    public void testTrainBookProductGuest() {
        Map<String, String> testProperties = getParam("testTrainBookProductGuest");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String tripType = testProperties.get("tripType");
        String originStation = testProperties.get("originStation");
        String destinationStation = testProperties.get("destinationStation");

        int adultPassengers = Integer.parseInt(testProperties.get("adult"));
        int infantPassengers = Integer.parseInt(testProperties.get("infant"));
        String email = testProperties.get("email");

        String event = testProperties.get("event");
        String screenName = testProperties.get("screenName");
        String vertical = testProperties.get("vertical");
        String eventAction = testProperties.get("eventAction");
        String eventLabel = testProperties.get("eventLabel");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TrainSearchPage<WebDriver> trainSearchPage = TrainSearchPage.getInstance(session);
        TrainSearchResultPage<WebDriver> trainSearchResultPage = TrainSearchResultPage.getInstance(session);
        TrainBookingPage<WebDriver> trainBookingPage = TrainBookingPage.getInstance(session);

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Open app as guest");
        loginPage.loginAsGuest();

        step("Open train vertical");
        homePage.openVerticalPage("Trains");

        step("Select origin station");
        trainSearchPage.selectOrigin(originStation);

        step("Select destination station");
        trainSearchPage.selectDestination(destinationStation);

        step("Toggle one way trip");
        trainSearchPage.toggleTripType(tripType);

        step("Add passenger");
        trainSearchPage.addPassenger(adultPassengers, infantPassengers);

        step("Search train");
        trainSearchPage.searchTrain();

        step("Skip pop up");
        trainSearchPage.skipPopup();

        step("Select train");
        trainSearchResultPage.selectTrain();

        step("Fill contact detail");
        trainBookingPage.fillGuestDetail(email);

        step("Fill passenger detail");
        trainBookingPage.fillPassengerDetail(adultPassengers, infantPassengers);

        step("Continue to second half booking form");
        trainBookingPage.continueTo2ndHalf();

        step("Continue to payment");
        trainBookingPage.continueToPayment();

        sleep(40);

        BaseAssertions trainPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("bookProduct event is found", trainPageAssertion.verifyEvent(Configs.MESSAGES, event), greaterThan(0));
        Assertion.assertThat("screenName is correct", trainPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "screenName", screenName), is(true));
        Assertion.assertThat("vertical is correct", trainPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "vertical", vertical), is(true));
        Assertion.assertThat("eventAction is correct", trainPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "eventAction", eventAction), is(true));
        Assertion.assertThat("eventLabel is correct", trainPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "eventLabel", eventLabel), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "TrainMainPage")
    @Tribe(name = "Train")
    @Vertical(name = "Core")
    @Test
    public void testTrainMainPageVisitLogin() {
        Map<String, String> testProperties = getParam("testTrainMainPageVisitLogin");
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

        step("Open train vertical");
        homePage.openVerticalPage("Trains");

        sleep(40);

        BaseAssertions trainPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("pageVisit event is found", trainPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, screenName), is(1));
        Assertion.assertThat("eventAction is correct", trainPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), is(true));
        Assertion.assertThat("screenName is correct", trainPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), is(true));
        Assertion.assertThat("utmPage is correct", trainPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "utmPage", utmPage), is(true));
        Assertion.assertThat("isFirstPage is correct", trainPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "isFirstPage", isFirstPage), is(true));
        Assertion.assertThat("vertical is correct", trainPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "vertical", vertical), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "TrainSRP")
    @Tribe(name = "Train")
    @Vertical(name = "Core")
    @Test
    public void testTrainSRPPageVisitLogin() {
        Map<String, String> testProperties = getParam("testTrainSRPPageVisitLogin");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String loginType = testProperties.get("loginType");
        String email = testProperties.get("email");
        String password = testProperties.get("password");

        String tripType = testProperties.get("tripType");
        String originStation = testProperties.get("originStation");
        String destinationStation = testProperties.get("destinationStation");

        String event = testProperties.get("event");
        String eventAction = testProperties.get("eventAction");
        String screenName = testProperties.get("screenName");
        String utmPage = testProperties.get("utmPage");
        String isFirstPage = testProperties.get("isFirstPage");
        String vertical = testProperties.get("vertical");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TrainSearchPage<WebDriver> trainSearchPage = TrainSearchPage.getInstance(session);

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Open app as login user");
        loginPage.loginWith(loginType, email, password);

        step("Open train vertical");
        homePage.openVerticalPage("Trains");

        step("Select origin station");
        trainSearchPage.selectOrigin(originStation);

        step("Select destination station");
        trainSearchPage.selectDestination(destinationStation);

        step("Toggle one way trip");
        trainSearchPage.toggleTripType(tripType);

        step("Search train");
        trainSearchPage.searchTrain();

        step("Skip pop up");
        trainSearchPage.skipPopup();

        sleep(40);

        BaseAssertions trainPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("pageVisit event is found", trainPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, screenName), is(1));
        Assertion.assertThat("eventAction is correct", trainPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), is(true));
        Assertion.assertThat("screenName is correct", trainPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), is(true));
        Assertion.assertThat("utmPage is correct", trainPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "utmPage", utmPage), is(true));
        Assertion.assertThat("isFirstPage is correct", trainPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "isFirstPage", isFirstPage), is(true));
        Assertion.assertThat("vertical is correct", trainPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "vertical", vertical), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "TrainBookingPage")
    @Tribe(name = "Train")
    @Vertical(name = "Core")
    @Test
    public void testTrainBookingPageVisitLogin() {
        Map<String, String> testProperties = getParam("testTrainBookingPageVisitLogin");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String loginType = testProperties.get("loginType");
        String email = testProperties.get("email");
        String password = testProperties.get("password");

        String tripType = testProperties.get("tripType");
        String originStation = testProperties.get("originStation");
        String destinationStation = testProperties.get("destinationStation");

        String event = testProperties.get("event");
        String eventAction = testProperties.get("eventAction");
        String screenName = testProperties.get("screenName");
        String utmPage = testProperties.get("utmPage");
        String isFirstPage = testProperties.get("isFirstPage");
        String vertical = testProperties.get("vertical");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TrainSearchPage<WebDriver> trainSearchPage = TrainSearchPage.getInstance(session);
        TrainSearchResultPage<WebDriver> trainSearchResultPage = TrainSearchResultPage.getInstance(session);

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Open app as login user");
        loginPage.loginWith(loginType, email, password);

        step("Open train vertical");
        homePage.openVerticalPage("Trains");

        step("Select origin station");
        trainSearchPage.selectOrigin(originStation);

        step("Select destination station");
        trainSearchPage.selectDestination(destinationStation);

        step("Toggle one way trip");
        trainSearchPage.toggleTripType(tripType);

        step("Search train");
        trainSearchPage.searchTrain();

        step("Skip pop up");
        trainSearchPage.skipPopup();

        step("Select train");
        trainSearchResultPage.selectTrain();

        sleep(40);

        BaseAssertions trainPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("pageVisit event is found", trainPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, screenName), is(1));
        Assertion.assertThat("eventAction is correct", trainPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), is(true));
        Assertion.assertThat("screenName is correct", trainPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), is(true));
        Assertion.assertThat("utmPage is correct", trainPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "utmPage", utmPage), is(true));
        Assertion.assertThat("isFirstPage is correct", trainPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "isFirstPage", isFirstPage), is(true));
        Assertion.assertThat("vertical is correct", trainPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "vertical", vertical), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "TrainMainPage")
    @Tribe(name = "Train")
    @Vertical(name = "Core")
    @Test
    public void testTrainEnterVerticalLogin() {
        Map<String, String> testProperties = getParam("testTrainEnterVerticalLogin");
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

        step("Open train vertical");
        homePage.openVerticalPage("Trains");

        sleep(40);

        BaseAssertions trainPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("enterVertical event is found", trainPageAssertion.verifyEvent(Configs.MESSAGES, event), is(1));
        Assertion.assertThat("screenName is correct", trainPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "screenName", screenName), is(true));
        Assertion.assertThat("vertical is correct", trainPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "vertical", vertical), is(true));
        Assertion.assertThat("eventAction is correct", trainPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "eventAction", eventAction), is(true));
        Assertion.assertThat("eventLabel is correct", trainPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "eventLabel", eventLabel), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "TrainSRP")
    @Tribe(name = "Train")
    @Vertical(name = "Core")
    @Test
    public void testTrainSearchLoadedLogin() {
        Map<String, String> testProperties = getParam("testTrainSearchLoadedLogin");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String loginType = testProperties.get("loginType");
        String email = testProperties.get("email");
        String password = testProperties.get("password");

        String tripType = testProperties.get("tripType");
        String originStation = testProperties.get("originStation");
        String destinationStation = testProperties.get("destinationStation");

        String event = testProperties.get("event");
        String screenName = testProperties.get("screenName");
        String vertical = testProperties.get("vertical");
        String eventAction = testProperties.get("eventAction");
        String eventLabel = testProperties.get("eventLabel");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TrainSearchPage<WebDriver> trainSearchPage = TrainSearchPage.getInstance(session);

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Open app as login user");
        loginPage.loginWith(loginType, email, password);

        step("Open train vertical");
        homePage.openVerticalPage("Trains");

        step("Select origin station");
        trainSearchPage.selectOrigin(originStation);

        step("Select destination station");
        trainSearchPage.selectDestination(destinationStation);

        step("Toggle one way trip");
        trainSearchPage.toggleTripType(tripType);

        step("Search train");
        trainSearchPage.searchTrain();

        step("Skip pop up");
        trainSearchPage.skipPopup();

        sleep(40);

        BaseAssertions trainPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("searchLoaded event is found", trainPageAssertion.verifyEvent(Configs.MESSAGES, event), is(1));
        Assertion.assertThat("screenName is correct", trainPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "screenName", screenName), is(true));
        Assertion.assertThat("vertical is correct", trainPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "vertical", vertical), is(true));
        Assertion.assertThat("eventAction is correct", trainPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "eventAction", eventAction), is(true));
        Assertion.assertThat("eventLabel is correct", trainPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "eventLabel", eventLabel), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "TrainSRP")
    @Tribe(name = "Train")
    @Vertical(name = "Core")
    @Test
    public void testTrainSearchProductLogin() {
        Map<String, String> testProperties = getParam("testTrainSearchProductLogin");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String loginType = testProperties.get("loginType");
        String email = testProperties.get("email");
        String password = testProperties.get("password");

        String tripType = testProperties.get("tripType");
        String originStation = testProperties.get("originStation");
        String destinationStation = testProperties.get("destinationStation");

        String event = testProperties.get("event");
        String screenName = testProperties.get("screenName");
        String vertical = testProperties.get("vertical");
        String eventAction = testProperties.get("eventAction");
        String eventDescription = testProperties.get("eventDescription");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TrainSearchPage<WebDriver> trainSearchPage = TrainSearchPage.getInstance(session);

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Open app as login user");
        loginPage.loginWith(loginType, email, password);

        step("Open train vertical");
        homePage.openVerticalPage("Trains");

        step("Select origin station");
        trainSearchPage.selectOrigin(originStation);

        step("Select destination station");
        trainSearchPage.selectDestination(destinationStation);

        step("Toggle one way trip");
        trainSearchPage.toggleTripType(tripType);

        step("Search train");
        trainSearchPage.searchTrain();

        step("Skip pop up");
        trainSearchPage.skipPopup();

        sleep(40);

        BaseAssertions trainPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("searchProduct event is found", trainPageAssertion.verifyEvent(Configs.MESSAGES, event), is(1));
        Assertion.assertThat("screenName is correct", trainPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "screenName", screenName), is(true));
        Assertion.assertThat("vertical is correct", trainPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "vertical", vertical), is(true));
        Assertion.assertThat("eventAction is correct", trainPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "eventAction", eventAction), is(true));
        Assertion.assertThat("eventDescription is correct", trainPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "eventDescription", eventDescription), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "TrainBookingPage")
    @Tribe(name = "Train")
    @Vertical(name = "Core")
    @Test
    public void testTrainBookProductLogin() {
        Map<String, String> testProperties = getParam("testTrainBookProductLogin");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String loginType = testProperties.get("loginType");
        String email = testProperties.get("email");
        String password = testProperties.get("password");

        String tripType = testProperties.get("tripType");
        String originStation = testProperties.get("originStation");
        String destinationStation = testProperties.get("destinationStation");

        int adultPassengers = Integer.parseInt(testProperties.get("adult"));
        int infantPassengers = Integer.parseInt(testProperties.get("infant"));

        String event = testProperties.get("event");
        String screenName = testProperties.get("screenName");
        String vertical = testProperties.get("vertical");
        String eventAction = testProperties.get("eventAction");
        String eventLabel = testProperties.get("eventLabel");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TrainSearchPage<WebDriver> trainSearchPage = TrainSearchPage.getInstance(session);
        TrainSearchResultPage<WebDriver> trainSearchResultPage = TrainSearchResultPage.getInstance(session);
        TrainBookingPage<WebDriver> trainBookingPage = TrainBookingPage.getInstance(session);

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Open app as login user");
        loginPage.loginWith(loginType, email, password);

        step("Open train vertical");
        homePage.openVerticalPage("Trains");

        step("Select origin station");
        trainSearchPage.selectOrigin(originStation);

        step("Select destination station");
        trainSearchPage.selectDestination(destinationStation);

        step("Toggle one way trip");
        trainSearchPage.toggleTripType(tripType);

        step("Add passenger");
        trainSearchPage.addPassenger(adultPassengers, infantPassengers);

        step("Search train");
        trainSearchPage.searchTrain();

        step("Skip pop up");
        trainSearchPage.skipPopup();

        step("Select train");
        trainSearchResultPage.selectTrain();

        step("Fill passenger detail");
        trainBookingPage.fillPassengerDetail(adultPassengers, infantPassengers);

        step("Continue to second half booking form");
        trainBookingPage.continueTo2ndHalf();

        step("Continue to payment");
        trainBookingPage.continueToPayment();

        sleep(40);

        BaseAssertions trainPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("bookProduct event is found", trainPageAssertion.verifyEvent(Configs.MESSAGES, event), greaterThan(0));
        Assertion.assertThat("screenName is correct", trainPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "screenName", screenName), is(true));
        Assertion.assertThat("vertical is correct", trainPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "vertical", vertical), is(true));
        Assertion.assertThat("eventAction is correct", trainPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "eventAction", eventAction), is(true));
        Assertion.assertThat("eventLabel is correct", trainPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "eventLabel", eventLabel), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "TrainMainPage")
    @Tribe(name = "Train")
    @Vertical(name = "Core")
    @Test
    public void testTrainMainPageVisitDeeplinkUtmGuest() {
        Map<String, String> testProperties = getParam("testTrainMainPageVisitDeeplinkUtmGuest");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String deeplink = "tiket://m.tiket.com/kereta-api/?";

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

        step("Open train vertical via deeplink");
        String utmProperties = analyticsHomePage.getUtmProperties(utmCampaign, utmContent, utmId, utmMedium, utmSource, utmTerm);
        analyticsHomePage.openDeeplink(deeplink + utmProperties);

        sleep(40);

        BaseAssertions trainPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("utmAttributedAt is not null", trainPageAssertion.verifyUserProperty(Configs.MESSAGES, "utmAttributedAt"), is(true));
        Assertion.assertThat("utmCampaign is correct", trainPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmCampaign", utmCampaign), is(true));
        Assertion.assertThat("utmContent is correct", trainPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmContent", utmContent), is(true));
        Assertion.assertThat("utmExternal is correct", trainPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmExternal", utmExternal), is(true));
        Assertion.assertThat("utmId is correct", trainPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmId", utmId), is(true));
        Assertion.assertThat("utmMedium is correct", trainPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmMedium", utmMedium), is(true));
        Assertion.assertThat("utmSource is correct", trainPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmSource", utmSource), is(true));
        Assertion.assertThat("utmTerm is correct", trainPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmTerm", utmTerm), is(true));
        Assertion.assertThat("userId is null", trainPageAssertion.verifyUserProperty(Configs.MESSAGES, "userId"), Matchers.is(false));

        Assertion.assertThat("pageVisit event is found", trainPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, screenName), is(1));
        Assertion.assertThat("eventAction is correct", trainPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), is(true));
        Assertion.assertThat("screenName is correct", trainPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), is(true));
        Assertion.assertThat("utmPage is correct", trainPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "utmPage", utmPage), is(true));
        Assertion.assertThat("isFirstPage is correct", trainPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "isFirstPage", isFirstPage), is(true));
        Assertion.assertThat("vertical is correct", trainPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "vertical", vertical), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "TrainMainPage")
    @Tribe(name = "Train")
    @Vertical(name = "Core")
    @Test
    public void testTrainMainPageVisitDeeplinkUtmLogin() {
        Map<String, String> testProperties = getParam("testTrainMainPageVisitDeeplinkUtmLogin");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String loginType = testProperties.get("loginType");
        String email = testProperties.get("email");
        String password = testProperties.get("password");
        String deeplink = "tiket://m.tiket.com/kereta-api/?";

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

        step("Open train vertical via deeplink");
        String utmProperties = analyticsHomePage.getUtmProperties(utmCampaign, utmContent, utmId, utmMedium, utmSource, utmTerm);
        analyticsHomePage.openDeeplink(deeplink + utmProperties);

        sleep(40);

        BaseAssertions trainPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("utmAttributedAt is not null", trainPageAssertion.verifyUserProperty(Configs.MESSAGES, "utmAttributedAt"), is(true));
        Assertion.assertThat("utmCampaign is correct", trainPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmCampaign", utmCampaign), is(true));
        Assertion.assertThat("utmContent is correct", trainPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmContent", utmContent), is(true));
        Assertion.assertThat("utmExternal is correct", trainPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmExternal", utmExternal), is(true));
        Assertion.assertThat("utmId is correct", trainPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmId", utmId), is(true));
        Assertion.assertThat("utmMedium is correct", trainPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmMedium", utmMedium), is(true));
        Assertion.assertThat("utmSource is correct", trainPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmSource", utmSource), is(true));
        Assertion.assertThat("utmTerm is correct", trainPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmTerm", utmTerm), is(true));
        Assertion.assertThat("userId is not null", trainPageAssertion.verifyUserProperty(Configs.MESSAGES, "userId"), Matchers.is(true));

        Assertion.assertThat("pageVisit event is found", trainPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, screenName), is(1));
        Assertion.assertThat("eventAction is correct", trainPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), is(true));
        Assertion.assertThat("screenName is correct", trainPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), is(true));
        Assertion.assertThat("utmPage is correct", trainPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "utmPage", utmPage), is(true));
        Assertion.assertThat("isFirstPage is correct", trainPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "isFirstPage", isFirstPage), is(true));
        Assertion.assertThat("vertical is correct", trainPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "vertical", vertical), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "TrainSRP")
    @Tribe(name = "Train")
    @Vertical(name = "Core")
    @Test
    public void testTrainSRPPageVisitDeeplinkUtmGuest() {
        Map<String, String> testProperties = getParam("testTrainSRPPageVisitDeeplinkUtmGuest");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        int daysToDepartureDate = Integer.parseInt(testProperties.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentDateYYYY_MM_DD();
        String departureDate = CoreFunctions.addToDateYYYY_MM_DD(currentDate, daysToDepartureDate);
        String deeplink = "tiket://m.tiket.com/kereta-api/cari?d=BD&dlabel=Bandung+%28BD%29&dt=STATION&a=GMR&alabel=Gambir+%28GMR%29&at=STATION&date=" + departureDate + "&adult=1&infant=0&productType=train&";

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
        TrainSearchPage<WebDriver> trainSearchPage = TrainSearchPage.getInstance(session);

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Open app as guest");
        loginPage.loginAsGuest();

        step("Open train SRP via deeplink");
        String utmProperties = analyticsHomePage.getUtmProperties(utmCampaign, utmContent, utmId, utmMedium, utmSource, utmTerm);
        analyticsHomePage.openDeeplink(deeplink + utmProperties);

        step("Skip pop up");
        trainSearchPage.skipPopup();

        sleep(40);

        BaseAssertions trainPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("utmAttributedAt is not null", trainPageAssertion.verifyUserProperty(Configs.MESSAGES, "utmAttributedAt"), is(true));
        Assertion.assertThat("utmCampaign is correct", trainPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmCampaign", utmCampaign), is(true));
        Assertion.assertThat("utmContent is correct", trainPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmContent", utmContent), is(true));
        Assertion.assertThat("utmExternal is correct", trainPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmExternal", utmExternal), is(true));
        Assertion.assertThat("utmId is correct", trainPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmId", utmId), is(true));
        Assertion.assertThat("utmMedium is correct", trainPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmMedium", utmMedium), is(true));
        Assertion.assertThat("utmSource is correct", trainPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmSource", utmSource), is(true));
        Assertion.assertThat("utmTerm is correct", trainPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmTerm", utmTerm), is(true));
        Assertion.assertThat("userId is null", trainPageAssertion.verifyUserProperty(Configs.MESSAGES, "userId"), Matchers.is(false));

        Assertion.assertThat("pageVisit event is found", trainPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, screenName), is(1));
        Assertion.assertThat("eventAction is correct", trainPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), is(true));
        Assertion.assertThat("screenName is correct", trainPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), is(true));
        Assertion.assertThat("utmPage is correct", trainPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "utmPage", utmPage), is(true));
        Assertion.assertThat("isFirstPage is correct", trainPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "isFirstPage", isFirstPage), is(true));
        Assertion.assertThat("vertical is correct", trainPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "vertical", vertical), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "TrainSRP")
    @Tribe(name = "Train")
    @Vertical(name = "Core")
    @Test
    public void testTrainSRPPageVisitDeeplinkUtmLogin() {
        Map<String, String> testProperties = getParam("testTrainSRPPageVisitDeeplinkUtmLogin");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String loginType = testProperties.get("loginType");
        String email = testProperties.get("email");
        String password = testProperties.get("password");

        int daysToDepartureDate = Integer.parseInt(testProperties.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentDateYYYY_MM_DD();
        String departureDate = CoreFunctions.addToDateYYYY_MM_DD(currentDate, daysToDepartureDate);
        String deeplink = "tiket://m.tiket.com/kereta-api/cari?d=BD&dlabel=Bandung+%28BD%29&dt=STATION&a=GMR&alabel=Gambir+%28GMR%29&at=STATION&date=" + departureDate + "&adult=1&infant=0&productType=train&";

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
        TrainSearchPage<WebDriver> trainSearchPage = TrainSearchPage.getInstance(session);

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Open app as login user");
        loginPage.loginWith(loginType, email, password);

        step("Open train SRP via deeplink");
        String utmProperties = analyticsHomePage.getUtmProperties(utmCampaign, utmContent, utmId, utmMedium, utmSource, utmTerm);
        analyticsHomePage.openDeeplink(deeplink + utmProperties);

        step("Skip pop up");
        trainSearchPage.skipPopup();

        sleep(40);

        BaseAssertions trainPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("utmAttributedAt is not null", trainPageAssertion.verifyUserProperty(Configs.MESSAGES, "utmAttributedAt"), is(true));
        Assertion.assertThat("utmCampaign is correct", trainPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmCampaign", utmCampaign), is(true));
        Assertion.assertThat("utmContent is correct", trainPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmContent", utmContent), is(true));
        Assertion.assertThat("utmExternal is correct", trainPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmExternal", utmExternal), is(true));
        Assertion.assertThat("utmId is correct", trainPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmId", utmId), is(true));
        Assertion.assertThat("utmMedium is correct", trainPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmMedium", utmMedium), is(true));
        Assertion.assertThat("utmSource is correct", trainPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmSource", utmSource), is(true));
        Assertion.assertThat("utmTerm is correct", trainPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmTerm", utmTerm), is(true));
        Assertion.assertThat("userId is not null", trainPageAssertion.verifyUserProperty(Configs.MESSAGES, "userId"), Matchers.is(true));

        Assertion.assertThat("pageVisit event is found", trainPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, screenName), is(1));
        Assertion.assertThat("eventAction is correct", trainPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), is(true));
        Assertion.assertThat("screenName is correct", trainPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), is(true));
        Assertion.assertThat("utmPage is correct", trainPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "utmPage", utmPage), is(true));
        Assertion.assertThat("isFirstPage is correct", trainPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "isFirstPage", isFirstPage), is(true));
        Assertion.assertThat("vertical is correct", trainPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "vertical", vertical), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "TrainMainPage")
    @Tribe(name = "Train")
    @Vertical(name = "Core")
    @Test
    public void testTrainMainPageVisitDeeplinkNonUtmGuest() {
        Map<String, String> testProperties = getParam("testTrainMainPageVisitDeeplinkNonUtmGuest");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String deeplink = "tiket://m.tiket.com/kereta-api";

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

        step("Open train vertical via deeplink");
        analyticsHomePage.openDeeplink(deeplink);

        sleep(40);

        BaseAssertions trainPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("utmAttributedAt is not null", trainPageAssertion.verifyUserProperty(Configs.MESSAGES, "utmAttributedAt"), is(true));
        Assertion.assertThat("utmCampaign is correct", trainPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmCampaign", utmCampaign), is(true));
        Assertion.assertThat("utmContent is correct", trainPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmContent", utmContent), is(true));
        Assertion.assertThat("utmExternal is correct", trainPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmExternal", utmExternal), is(true));
        Assertion.assertThat("utmId is correct", trainPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmId", utmId), is(true));
        Assertion.assertThat("utmMedium is correct", trainPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmMedium", utmMedium), is(true));
        Assertion.assertThat("utmSource is correct", trainPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmSource", utmSource), is(true));
        Assertion.assertThat("utmTerm is correct", trainPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmTerm", utmTerm), is(true));
        Assertion.assertThat("userId is null", trainPageAssertion.verifyUserProperty(Configs.MESSAGES, "userId"), is(false));

        Assertion.assertThat("pageVisit event is found", trainPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, screenName), is(1));
        Assertion.assertThat("eventAction is correct", trainPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), is(true));
        Assertion.assertThat("screenName is correct", trainPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), is(true));
        Assertion.assertThat("utmPage is correct", trainPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "utmPage", utmPage), is(true));
        Assertion.assertThat("isFirstPage is correct", trainPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "isFirstPage", isFirstPage), is(true));
        Assertion.assertThat("vertical is correct", trainPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "vertical", vertical), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "TrainMainPage")
    @Tribe(name = "Train")
    @Vertical(name = "Core")
    @Test
    public void testTrainMainPageVisitDeeplinkNonUtmLogin() {
        Map<String, String> testProperties = getParam("testTrainMainPageVisitDeeplinkNonUtmLogin");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String loginType = testProperties.get("loginType");
        String email = testProperties.get("email");
        String password = testProperties.get("password");
        String deeplink = "tiket://m.tiket.com/kereta-api";

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

        step("Open train vertical via deeplink");
        analyticsHomePage.openDeeplink(deeplink);

        sleep(40);

        BaseAssertions trainPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("utmAttributedAt is not null", trainPageAssertion.verifyUserProperty(Configs.MESSAGES, "utmAttributedAt"), is(true));
        Assertion.assertThat("utmCampaign is correct", trainPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmCampaign", utmCampaign), is(true));
        Assertion.assertThat("utmContent is correct", trainPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmContent", utmContent), is(true));
        Assertion.assertThat("utmExternal is correct", trainPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmExternal", utmExternal), is(true));
        Assertion.assertThat("utmId is correct", trainPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmId", utmId), is(true));
        Assertion.assertThat("utmMedium is correct", trainPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmMedium", utmMedium), is(true));
        Assertion.assertThat("utmSource is correct", trainPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmSource", utmSource), is(true));
        Assertion.assertThat("utmTerm is correct", trainPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmTerm", utmTerm), is(true));
        Assertion.assertThat("userId is not null", trainPageAssertion.verifyUserProperty(Configs.MESSAGES, "userId"), is(true));

        Assertion.assertThat("pageVisit event is found", trainPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, screenName), is(1));
        Assertion.assertThat("eventAction is correct", trainPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), is(true));
        Assertion.assertThat("screenName is correct", trainPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), is(true));
        Assertion.assertThat("utmPage is correct", trainPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "utmPage", utmPage), is(true));
        Assertion.assertThat("isFirstPage is correct", trainPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "isFirstPage", isFirstPage), is(true));
        Assertion.assertThat("vertical is correct", trainPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "vertical", vertical), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "TrainSRP")
    @Tribe(name = "Train")
    @Vertical(name = "Core")
    @Test
    public void testTrainSRPPageVisitDeeplinkNonUtmGuest() {
        Map<String, String> testProperties = getParam("testTrainSRPPageVisitDeeplinkNonUtmGuest");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        int daysToDepartureDate = Integer.parseInt(testProperties.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentDateYYYY_MM_DD();
        String departureDate = CoreFunctions.addToDateYYYY_MM_DD(currentDate, daysToDepartureDate);
        String deeplink = "tiket://m.tiket.com/kereta-api/cari?d=BD&dlabel=Bandung+%28BD%29&dt=STATION&a=GMR&alabel=Gambir+%28GMR%29&at=STATION&date=" + departureDate + "&adult=1&infant=0&productType=train";

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
        TrainSearchPage<WebDriver> trainSearchPage = TrainSearchPage.getInstance(session);

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Open app as guest");
        loginPage.loginAsGuest();

        step("Open train SRP via deeplink");
        analyticsHomePage.openDeeplink(deeplink);

        step("Skip pop up");
        trainSearchPage.skipPopup();

        sleep(40);

        BaseAssertions trainPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("utmAttributedAt is not null", trainPageAssertion.verifyUserProperty(Configs.MESSAGES, "utmAttributedAt"), is(true));
        Assertion.assertThat("utmCampaign is correct", trainPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmCampaign", utmCampaign), is(true));
        Assertion.assertThat("utmContent is correct", trainPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmContent", utmContent), is(true));
        Assertion.assertThat("utmExternal is correct", trainPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmExternal", utmExternal), is(true));
        Assertion.assertThat("utmId is correct", trainPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmId", utmId), is(true));
        Assertion.assertThat("utmMedium is correct", trainPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmMedium", utmMedium), is(true));
        Assertion.assertThat("utmSource is correct", trainPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmSource", utmSource), is(true));
        Assertion.assertThat("utmTerm is correct", trainPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmTerm", utmTerm), is(true));
        Assertion.assertThat("userId is null", trainPageAssertion.verifyUserProperty(Configs.MESSAGES, "userId"), is(false));

        Assertion.assertThat("pageVisit event is found", trainPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, screenName), is(1));
        Assertion.assertThat("eventAction is correct", trainPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), is(true));
        Assertion.assertThat("screenName is correct", trainPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), is(true));
        Assertion.assertThat("utmPage is correct", trainPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "utmPage", utmPage), is(true));
        Assertion.assertThat("isFirstPage is correct", trainPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "isFirstPage", isFirstPage), is(true));
        Assertion.assertThat("vertical is correct", trainPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "vertical", vertical), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "TrainSRP")
    @Tribe(name = "Train")
    @Vertical(name = "Core")
    @Test
    public void testTrainSRPPageVisitDeeplinkNonUtmLogin() {
        Map<String, String> testProperties = getParam("testTrainSRPPageVisitDeeplinkNonUtmLogin");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String loginType = testProperties.get("loginType");
        String email = testProperties.get("email");
        String password = testProperties.get("password");

        int daysToDepartureDate = Integer.parseInt(testProperties.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentDateYYYY_MM_DD();
        String departureDate = CoreFunctions.addToDateYYYY_MM_DD(currentDate, daysToDepartureDate);
        String deeplink = "tiket://m.tiket.com/kereta-api/cari?d=BD&dlabel=Bandung+%28BD%29&dt=STATION&a=GMR&alabel=Gambir+%28GMR%29&at=STATION&date=" + departureDate + "&adult=1&infant=0&productType=train";

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
        TrainSearchPage<WebDriver> trainSearchPage = TrainSearchPage.getInstance(session);

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Open app as login user");
        loginPage.loginWith(loginType, email, password);

        step("Open train SRP via deeplink");
        analyticsHomePage.openDeeplink(deeplink);

        step("Skip pop up");
        trainSearchPage.skipPopup();

        sleep(40);

        BaseAssertions trainPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("utmAttributedAt is not null", trainPageAssertion.verifyUserProperty(Configs.MESSAGES, "utmAttributedAt"), is(true));
        Assertion.assertThat("utmCampaign is correct", trainPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmCampaign", utmCampaign), is(true));
        Assertion.assertThat("utmContent is correct", trainPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmContent", utmContent), is(true));
        Assertion.assertThat("utmExternal is correct", trainPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmExternal", utmExternal), is(true));
        Assertion.assertThat("utmId is correct", trainPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmId", utmId), is(true));
        Assertion.assertThat("utmMedium is correct", trainPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmMedium", utmMedium), is(true));
        Assertion.assertThat("utmSource is correct", trainPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmSource", utmSource), is(true));
        Assertion.assertThat("utmTerm is correct", trainPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmTerm", utmTerm), is(true));
        Assertion.assertThat("userId is not null", trainPageAssertion.verifyUserProperty(Configs.MESSAGES, "userId"), is(true));

        Assertion.assertThat("pageVisit event is found", trainPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, screenName), is(1));
        Assertion.assertThat("eventAction is correct", trainPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), is(true));
        Assertion.assertThat("screenName is correct", trainPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), is(true));
        Assertion.assertThat("utmPage is correct", trainPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "utmPage", utmPage), is(true));
        Assertion.assertThat("isFirstPage is correct", trainPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "isFirstPage", isFirstPage), is(true));
        Assertion.assertThat("vertical is correct", trainPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "vertical", vertical), is(true));
    }
}