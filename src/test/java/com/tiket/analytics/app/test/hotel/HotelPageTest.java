package com.tiket.analytics.app.test.hotel;

import com.tiket.analytics.BaseAnalyticsTest;
import com.tiket.analytics.assertions.BaseAssertions;
import com.tiket.analytics.app.home.AnalyticsHomePage;
import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Session;
import com.tiket.page.accommodation.hotel.bookingformpage.BookingFormPage;
import com.tiket.page.accommodation.hotel.detailpage.DetailPage;
import com.tiket.page.accommodation.hotel.landingpage.LandingPage;
import com.tiket.page.accommodation.hotel.roomlistpage.RoomListPage;
import com.tiket.page.accommodation.hotel.searchresultpage.SearchResultPage;
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

public class HotelPageTest extends BaseAnalyticsTest {

    @Author(names = {"Okta"})
    @Module(name = "HotelMainPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Core")
    @Test
    public void testHotelMainPageVisitGuest() {
        Map<String, String> testProperties = getParam("testHotelMainPageVisitGuest");
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

        step("Open hotels vertical");
        homePage.openVerticalPage("Hotel");

        sleep(40);

        BaseAssertions hotelPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("pageVisit event is found", hotelPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, screenName), is(1));
        Assertion.assertThat("eventAction is correct", hotelPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), is(true));
        Assertion.assertThat("screenName is correct", hotelPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), is(true));
        Assertion.assertThat("utmPage is correct", hotelPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "utmPage", utmPage), is(true));
        Assertion.assertThat("isFirstPage is correct", hotelPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "isFirstPage", isFirstPage), is(true));
        Assertion.assertThat("vertical is correct", hotelPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "vertical", vertical), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "HotelPDP")
    @Tribe(name = "Hotel")
    @Vertical(name = "Core")
    @Test
    public void testHotelPDPPageVisitGuest() {
        Map<String, String> testProperties = getParam("testHotelPDPPageVisitGuest");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();
        String location = testProperties.get("location");

        String event = testProperties.get("event");
        String eventAction = testProperties.get("eventAction");
        String screenName = testProperties.get("screenName");
        String utmPage = testProperties.get("utmPage");
        String isFirstPage = testProperties.get("isFirstPage");
        String vertical = testProperties.get("vertical");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Open app as guest");
        loginPage.loginAsGuest();

        step("Open hotels vertical");
        homePage.openVerticalPage("Hotel");

        step("Input hotel location");
        landingPage.searchStayLocation(location);

        step("Search hotel");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        step("Open hotel PDP");
        searchResultPage.selectFirstHotelCard();

        sleep(40);

        BaseAssertions hotelPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("pageVisit event is found", hotelPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, screenName), is(1));
        Assertion.assertThat("eventAction is correct", hotelPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), is(true));
        Assertion.assertThat("screenName is correct", hotelPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), is(true));
        Assertion.assertThat("utmPage is correct", hotelPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "utmPage", utmPage), is(true));
        Assertion.assertThat("isFirstPage is correct", hotelPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "isFirstPage", isFirstPage), is(true));
        Assertion.assertThat("vertical is correct", hotelPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "vertical", vertical), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "HotelBookingPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Core")
    @Test
    public void testHotelBookingPageVisitGuest() {
        Map<String, String> testProperties = getParam("testHotelBookingPageVisitGuest");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();
        String location = testProperties.get("location");

        String event = testProperties.get("event");
        String eventAction = testProperties.get("eventAction");
        String screenName = testProperties.get("screenName");
        String utmPage = testProperties.get("utmPage");
        String isFirstPage = testProperties.get("isFirstPage");
        String vertical = testProperties.get("vertical");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Open app as guest");
        loginPage.loginAsGuest();

        step("Open hotels vertical");
        homePage.openVerticalPage("Hotel");

        step("Input hotel location");
        landingPage.searchStayLocation(location);

        step("Search hotel");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        step("Open hotel PDP");
        searchResultPage.selectFirstHotelCard();

        step("Scroll down to room list");
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();

        step("Open hotel booking page");
        roomListPage.getTheFirstHotelRoomPrice();
        roomListPage.clickBookRoom();

        sleep(40);

        BaseAssertions hotelPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("pageVisit event is found", hotelPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, screenName), is(1));
        Assertion.assertThat("eventAction is correct", hotelPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), is(true));
        Assertion.assertThat("screenName is correct", hotelPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), is(true));
        Assertion.assertThat("utmPage is correct", hotelPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "utmPage", utmPage), is(true));
        Assertion.assertThat("isFirstPage is correct", hotelPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "isFirstPage", isFirstPage), is(true));
        Assertion.assertThat("vertical is correct", hotelPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "vertical", vertical), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "HotelSRP")
    @Tribe(name = "Hotel")
    @Vertical(name = "Core")
    @Test
    public void testHotelSRPPageVisitGuest() {
        Map<String, String> testProperties = getParam("testHotelSRPPageVisitGuest");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();
        String location = testProperties.get("location");

        String event = testProperties.get("event");
        String eventAction = testProperties.get("eventAction");
        String screenName = testProperties.get("screenName");
        String utmPage = testProperties.get("utmPage");
        String isFirstPage = testProperties.get("isFirstPage");
        String vertical = testProperties.get("vertical");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Open app as guest");
        loginPage.loginAsGuest();

        step("Open hotels vertical");
        homePage.openVerticalPage("Hotel");

        step("Input hotel location");
        landingPage.searchStayLocation(location);

        step("Search hotel");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        sleep(40);

        BaseAssertions hotelPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("pageVisit event is found", hotelPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, screenName), is(1));
        Assertion.assertThat("eventAction is correct", hotelPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), is(true));
        Assertion.assertThat("screenName is correct", hotelPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), is(true));
        Assertion.assertThat("utmPage is correct", hotelPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "utmPage", utmPage), is(true));
        Assertion.assertThat("isFirstPage is correct", hotelPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "isFirstPage", isFirstPage), is(true));
        Assertion.assertThat("vertical is correct", hotelPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "vertical", vertical), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "HotelMainPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Core")
    @Test
    public void testHotelEnterVerticalGuest() {
        Map<String, String> testProperties = getParam("testHotelEnterVerticalGuest");
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

        step("Open hotels vertical");
        homePage.openVerticalPage("Hotel");

        sleep(40);

        BaseAssertions hotelPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("enterVertical event is found", hotelPageAssertion.verifyEvent(Configs.MESSAGES, event), is(1));
        Assertion.assertThat("screenName is correct", hotelPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "screenName", screenName), is(true));
        Assertion.assertThat("vertical is correct", hotelPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "vertical", vertical), is(true));
        Assertion.assertThat("eventAction is correct", hotelPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "eventAction", eventAction), is(true));
        Assertion.assertThat("eventLabel is correct", hotelPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "eventLabel", eventLabel), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "HotelSRP")
    @Tribe(name = "Hotel")
    @Vertical(name = "Core")
    @Test
    public void testHotelSearchLoadedGuest() {
        Map<String, String> testProperties = getParam("testHotelSearchLoadedGuest");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();
        String location = testProperties.get("location");

        String event = testProperties.get("event");
        String screenName = testProperties.get("screenName");
        String vertical = testProperties.get("vertical");
        String eventAction = testProperties.get("eventAction");
        String eventLabel = testProperties.get("eventLabel");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Open app as guest");
        loginPage.loginAsGuest();

        step("Open hotels vertical");
        homePage.openVerticalPage("Hotel");

        step("Input hotel location");
        landingPage.searchStayLocation(location);

        step("Search hotel");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        sleep(40);

        BaseAssertions hotelPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("searchLoaded event is found", hotelPageAssertion.verifyEvent(Configs.MESSAGES, event), is(1));
        Assertion.assertThat("screenName is correct", hotelPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "screenName", screenName), is(true));
        Assertion.assertThat("vertical is correct", hotelPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "vertical", vertical), is(true));
        Assertion.assertThat("eventAction is correct", hotelPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "eventAction", eventAction), is(true));
        Assertion.assertThat("eventLabel is correct", hotelPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "eventLabel", eventLabel), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "HotelSRP")
    @Tribe(name = "Hotel")
    @Vertical(name = "Core")
    @Test
    public void testHotelSearchProductGuest() {
        Map<String, String> testProperties = getParam("testHotelSearchProductGuest");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();
        String location = testProperties.get("location");

        String event = testProperties.get("event");
        String screenName = testProperties.get("screenName");
        String vertical = testProperties.get("vertical");
        String eventAction = testProperties.get("eventAction");
        String eventLabel = testProperties.get("eventLabel");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Open app as guest");
        loginPage.loginAsGuest();

        step("Open hotels vertical");
        homePage.openVerticalPage("Hotel");

        step("Input hotel location");
        landingPage.searchStayLocation(location);

        step("Search hotel");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        sleep(40);

        BaseAssertions hotelPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("searchProduct event is found", hotelPageAssertion.verifyEvent(Configs.MESSAGES, event), is(1));
        Assertion.assertThat("screenName is correct", hotelPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "screenName", screenName), is(true));
        Assertion.assertThat("vertical is correct", hotelPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "vertical", vertical), is(true));
        Assertion.assertThat("eventAction is correct", hotelPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "eventAction", eventAction), is(true));
        Assertion.assertThat("eventLabel is correct", hotelPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "eventLabel", eventLabel), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "HotelBookingPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Core")
    @Test
    public void testHotelBookProductGuest() {
        Map<String, String> testProperties = getParam("testHotelBookProductGuest");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();
        String location = testProperties.get("location");

        String event = testProperties.get("event");
        String screenName = testProperties.get("screenName");
        String vertical = testProperties.get("vertical");
        String eventAction = testProperties.get("eventAction");
        String eventDescription = testProperties.get("eventDescription");
        String eventLabel = testProperties.get("eventLabel");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Open app as guest");
        loginPage.loginAsGuest();

        step("Open hotels vertical");
        homePage.openVerticalPage("Hotel");

        step("Input hotel location");
        landingPage.searchStayLocation(location);

        step("Search hotel");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        step("Open hotel PDP");
        searchResultPage.selectFirstHotelCard();

        step("Scroll down to room list");
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();

        step("Open hotel booking page");
        roomListPage.getTheFirstHotelRoomPrice();
        roomListPage.clickBookRoom();

        step("Fill booking detail");
        bookingFormPage.fillContactOrderDetail();
        bookingFormPage.fillGuestDetail(false);

        step("Submit booking");
        bookingFormPage.continueToPaymentPage();

        sleep(40);

        BaseAssertions hotelPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("bookProduct event is found", hotelPageAssertion.verifyEvent(Configs.MESSAGES, event), greaterThan(0));
        Assertion.assertThat("screenName is correct", hotelPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "screenName", screenName), is(true));
        Assertion.assertThat("vertical is correct", hotelPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "vertical", vertical), is(true));
        Assertion.assertThat("eventAction is correct", hotelPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "eventAction", eventAction), is(true));
        Assertion.assertThat("eventDescription is correct", hotelPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "eventDescription", eventDescription), is(true));
        Assertion.assertThat("eventLabel is correct", hotelPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "eventLabel", eventLabel), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "HotelMainPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Core")
    @Test
    public void testHotelMainPageVisitLogin() {
        Map<String, String> testProperties = getParam("testHotelMainPageVisitLogin");
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

        step("Open hotels vertical");
        homePage.openVerticalPage("Hotel");

        sleep(40);

        BaseAssertions hotelPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("pageVisit event is found", hotelPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, screenName), is(1));
        Assertion.assertThat("eventAction is correct", hotelPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), is(true));
        Assertion.assertThat("screenName is correct", hotelPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), is(true));
        Assertion.assertThat("utmPage is correct", hotelPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "utmPage", utmPage), is(true));
        Assertion.assertThat("isFirstPage is correct", hotelPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "isFirstPage", isFirstPage), is(true));
        Assertion.assertThat("vertical is correct", hotelPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "vertical", vertical), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "HotelPDP")
    @Tribe(name = "Hotel")
    @Vertical(name = "Core")
    @Test
    public void testHotelPDPPageVisitLogin() {
        Map<String, String> testProperties = getParam("testHotelPDPPageVisitLogin");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String loginType = testProperties.get("loginType");
        String email = testProperties.get("email");
        String password = testProperties.get("password");
        String location = testProperties.get("location");

        String event = testProperties.get("event");
        String eventAction = testProperties.get("eventAction");
        String screenName = testProperties.get("screenName");
        String utmPage = testProperties.get("utmPage");
        String isFirstPage = testProperties.get("isFirstPage");
        String vertical = testProperties.get("vertical");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Open app as login user");
        loginPage.loginWith(loginType, email, password);

        step("Open hotels vertical");
        homePage.openVerticalPage("Hotel");

        step("Input hotel location");
        landingPage.searchStayLocation(location);

        step("Search hotel");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        step("Open hotel PDP");
        searchResultPage.selectFirstHotelCard();

        sleep(40);

        BaseAssertions hotelPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("pageVisit event is found", hotelPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, screenName), is(1));
        Assertion.assertThat("eventAction is correct", hotelPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), is(true));
        Assertion.assertThat("screenName is correct", hotelPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), is(true));
        Assertion.assertThat("utmPage is correct", hotelPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "utmPage", utmPage), is(true));
        Assertion.assertThat("isFirstPage is correct", hotelPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "isFirstPage", isFirstPage), is(true));
        Assertion.assertThat("vertical is correct", hotelPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "vertical", vertical), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "HotelBookingPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Core")
    @Test
    public void testHotelBookingPageVisitLogin() {
        Map<String, String> testProperties = getParam("testHotelBookingPageVisitLogin");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String loginType = testProperties.get("loginType");
        String email = testProperties.get("email");
        String password = testProperties.get("password");
        String location = testProperties.get("location");

        String event = testProperties.get("event");
        String eventAction = testProperties.get("eventAction");
        String screenName = testProperties.get("screenName");
        String utmPage = testProperties.get("utmPage");
        String isFirstPage = testProperties.get("isFirstPage");
        String vertical = testProperties.get("vertical");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Open app as login user");
        loginPage.loginWith(loginType, email, password);

        step("Open hotels vertical");
        homePage.openVerticalPage("Hotel");

        step("Input hotel location");
        landingPage.searchStayLocation(location);

        step("Search hotel");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        step("Open hotel PDP");
        searchResultPage.selectFirstHotelCard();

        step("Scroll down to room list");
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();

        step("Open hotel booking page");
        roomListPage.getTheFirstHotelRoomPrice();
        roomListPage.clickBookRoom();

        sleep(40);

        BaseAssertions hotelPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("pageVisit event is found", hotelPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, screenName), is(1));
        Assertion.assertThat("eventAction is correct", hotelPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), is(true));
        Assertion.assertThat("screenName is correct", hotelPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), is(true));
        Assertion.assertThat("utmPage is correct", hotelPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "utmPage", utmPage), is(true));
        Assertion.assertThat("isFirstPage is correct", hotelPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "isFirstPage", isFirstPage), is(true));
        Assertion.assertThat("vertical is correct", hotelPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "vertical", vertical), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "HotelSRP")
    @Tribe(name = "Hotel")
    @Vertical(name = "Core")
    @Test
    public void testHotelSRPPageVisitLogin() {
        Map<String, String> testProperties = getParam("testHotelSRPPageVisitLogin");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String loginType = testProperties.get("loginType");
        String email = testProperties.get("email");
        String password = testProperties.get("password");
        String location = testProperties.get("location");

        String event = testProperties.get("event");
        String eventAction = testProperties.get("eventAction");
        String screenName = testProperties.get("screenName");
        String utmPage = testProperties.get("utmPage");
        String isFirstPage = testProperties.get("isFirstPage");
        String vertical = testProperties.get("vertical");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Open app as login user");
        loginPage.loginWith(loginType, email, password);

        step("Open hotels vertical");
        homePage.openVerticalPage("Hotel");

        step("Input hotel location");
        landingPage.searchStayLocation(location);

        step("Search hotel");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        sleep(40);

        BaseAssertions hotelPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("pageVisit event is found", hotelPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, screenName), is(1));
        Assertion.assertThat("eventAction is correct", hotelPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), is(true));
        Assertion.assertThat("screenName is correct", hotelPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), is(true));
        Assertion.assertThat("utmPage is correct", hotelPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "utmPage", utmPage), is(true));
        Assertion.assertThat("isFirstPage is correct", hotelPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "isFirstPage", isFirstPage), is(true));
        Assertion.assertThat("vertical is correct", hotelPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "vertical", vertical), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "HotelMainPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Core")
    @Test
    public void testHotelEnterVerticalLogin() {
        Map<String, String> testProperties = getParam("testHotelEnterVerticalLogin");
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

        step("Open hotels vertical");
        homePage.openVerticalPage("Hotel");

        sleep(40);

        BaseAssertions hotelPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("enterVertical event is found", hotelPageAssertion.verifyEvent(Configs.MESSAGES, event), is(1));
        Assertion.assertThat("screenName is correct", hotelPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "screenName", screenName), is(true));
        Assertion.assertThat("vertical is correct", hotelPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "vertical", vertical), is(true));
        Assertion.assertThat("eventAction is correct", hotelPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "eventAction", eventAction), is(true));
        Assertion.assertThat("eventLabel is correct", hotelPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "eventLabel", eventLabel), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "HotelSRP")
    @Tribe(name = "Hotel")
    @Vertical(name = "Core")
    @Test
    public void testHotelSearchLoadedLogin() {
        Map<String, String> testProperties = getParam("testHotelSearchLoadedLogin");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String loginType = testProperties.get("loginType");
        String email = testProperties.get("email");
        String password = testProperties.get("password");
        String location = testProperties.get("location");

        String event = testProperties.get("event");
        String screenName = testProperties.get("screenName");
        String vertical = testProperties.get("vertical");
        String eventAction = testProperties.get("eventAction");
        String eventLabel = testProperties.get("eventLabel");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Open app as login user");
        loginPage.loginWith(loginType, email, password);

        step("Open hotels vertical");
        homePage.openVerticalPage("Hotel");

        step("Input hotel location");
        landingPage.searchStayLocation(location);

        step("Search hotel");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        sleep(40);

        BaseAssertions hotelPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("searchLoaded event is found", hotelPageAssertion.verifyEvent(Configs.MESSAGES, event), is(1));
        Assertion.assertThat("screenName is correct", hotelPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "screenName", screenName), is(true));
        Assertion.assertThat("vertical is correct", hotelPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "vertical", vertical), is(true));
        Assertion.assertThat("eventAction is correct", hotelPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "eventAction", eventAction), is(true));
        Assertion.assertThat("eventLabel is correct", hotelPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "eventLabel", eventLabel), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "HotelSRP")
    @Tribe(name = "Hotel")
    @Vertical(name = "Core")
    @Test
    public void testHotelSearchProductLogin() {
        Map<String, String> testProperties = getParam("testHotelSearchProductLogin");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String loginType = testProperties.get("loginType");
        String email = testProperties.get("email");
        String password = testProperties.get("password");
        String location = testProperties.get("location");

        String event = testProperties.get("event");
        String screenName = testProperties.get("screenName");
        String vertical = testProperties.get("vertical");
        String eventAction = testProperties.get("eventAction");
        String eventLabel = testProperties.get("eventLabel");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Open app as login user");
        loginPage.loginWith(loginType, email, password);

        step("Open hotels vertical");
        homePage.openVerticalPage("Hotel");

        step("Input hotel location");
        landingPage.searchStayLocation(location);

        step("Search hotel");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        sleep(40);

        BaseAssertions hotelPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("searchProduct event is found", hotelPageAssertion.verifyEvent(Configs.MESSAGES, event), is(1));
        Assertion.assertThat("screenName is correct", hotelPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "screenName", screenName), is(true));
        Assertion.assertThat("vertical is correct", hotelPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "vertical", vertical), is(true));
        Assertion.assertThat("eventAction is correct", hotelPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "eventAction", eventAction), is(true));
        Assertion.assertThat("eventLabel is correct", hotelPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "eventLabel", eventLabel), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "HotelBookingPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Core")
    @Test
    public void testHotelBookProductLogin() {
        Map<String, String> testProperties = getParam("testHotelBookProductLogin");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String loginType = testProperties.get("loginType");
        String email = testProperties.get("email");
        String password = testProperties.get("password");
        String location = testProperties.get("location");

        String event = testProperties.get("event");
        String screenName = testProperties.get("screenName");
        String vertical = testProperties.get("vertical");
        String eventAction = testProperties.get("eventAction");
        String eventDescription = testProperties.get("eventDescription");
        String eventLabel = testProperties.get("eventLabel");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Open app as login user");
        loginPage.loginWith(loginType, email, password);

        step("Open hotels vertical");
        homePage.openVerticalPage("Hotel");

        step("Input hotel location");
        landingPage.searchStayLocation(location);

        step("Search hotel");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        step("Open hotel PDP");
        searchResultPage.selectFirstHotelCard();

        step("Scroll down to room list");
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();

        step("Open hotel booking page");
        roomListPage.getTheFirstHotelRoomPrice();
        roomListPage.clickBookRoom();

        step("Submit booking");
        bookingFormPage.continueToPaymentPage();

        sleep(40);

        BaseAssertions hotelPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("bookProduct event is found", hotelPageAssertion.verifyEvent(Configs.MESSAGES, event), greaterThan(0));
        Assertion.assertThat("screenName is correct", hotelPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "screenName", screenName), is(true));
        Assertion.assertThat("vertical is correct", hotelPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "vertical", vertical), is(true));
        Assertion.assertThat("eventAction is correct", hotelPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "eventAction", eventAction), is(true));
        Assertion.assertThat("eventDescription is correct", hotelPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "eventDescription", eventDescription), is(true));
        Assertion.assertThat("eventLabel is correct", hotelPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "eventLabel", eventLabel), is(true));
    }
    
    @Author(names = {"Okta"})
    @Module(name = "HotelMainPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Core")
    @Test
    public void testHotelMainPageVisitDeeplinkUtmGuest() {
        Map<String, String> testProperties = getParam("testHotelMainPageVisitDeeplinkUtmGuest");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String deeplink = "tiket://m.tiket.com/hotel/?";

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

        step("Open hotel vertical via deeplink");
        String utmProperties = analyticsHomePage.getUtmProperties(utmCampaign, utmContent, utmId, utmMedium, utmSource, utmTerm);
        analyticsHomePage.openDeeplink(deeplink + utmProperties);

        sleep(40);

        BaseAssertions hotelPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("utmAttributedAt is not null", hotelPageAssertion.verifyUserProperty(Configs.MESSAGES, "utmAttributedAt"), is(true));
        Assertion.assertThat("utmCampaign is correct", hotelPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmCampaign", utmCampaign), is(true));
        Assertion.assertThat("utmContent is correct", hotelPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmContent", utmContent), is(true));
        Assertion.assertThat("utmExternal is correct", hotelPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmExternal", utmExternal), is(true));
        Assertion.assertThat("utmId is correct", hotelPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmId", utmId), is(true));
        Assertion.assertThat("utmMedium is correct", hotelPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmMedium", utmMedium), is(true));
        Assertion.assertThat("utmSource is correct", hotelPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmSource", utmSource), is(true));
        Assertion.assertThat("utmTerm is correct", hotelPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmTerm", utmTerm), is(true));
        Assertion.assertThat("userId is null", hotelPageAssertion.verifyUserProperty(Configs.MESSAGES, "userId"), is(false));

        Assertion.assertThat("pageVisit event is found", hotelPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, screenName), is(1));
        Assertion.assertThat("eventAction is correct", hotelPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), is(true));
        Assertion.assertThat("screenName is correct", hotelPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), is(true));
        Assertion.assertThat("utmPage is correct", hotelPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "utmPage", utmPage), is(true));
        Assertion.assertThat("isFirstPage is correct", hotelPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "isFirstPage", isFirstPage), is(true));
        Assertion.assertThat("vertical is correct", hotelPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "vertical", vertical), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "HotelMainPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Core")
    @Test
    public void testHotelMainPageVisitDeeplinkUtmLogin() {
        Map<String, String> testProperties = getParam("testHotelMainPageVisitDeeplinkUtmLogin");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String loginType = testProperties.get("loginType");
        String email = testProperties.get("email");
        String password = testProperties.get("password");
        String deeplink = "tiket://m.tiket.com/hotel/?";

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

        step("Open hotel vertical via deeplink");
        String utmProperties = analyticsHomePage.getUtmProperties(utmCampaign, utmContent, utmId, utmMedium, utmSource, utmTerm);
        analyticsHomePage.openDeeplink(deeplink + utmProperties);

        sleep(40);

        BaseAssertions hotelPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("utmAttributedAt is not null", hotelPageAssertion.verifyUserProperty(Configs.MESSAGES, "utmAttributedAt"), is(true));
        Assertion.assertThat("utmCampaign is correct", hotelPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmCampaign", utmCampaign), is(true));
        Assertion.assertThat("utmContent is correct", hotelPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmContent", utmContent), is(true));
        Assertion.assertThat("utmExternal is correct", hotelPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmExternal", utmExternal), is(true));
        Assertion.assertThat("utmId is correct", hotelPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmId", utmId), is(true));
        Assertion.assertThat("utmMedium is correct", hotelPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmMedium", utmMedium), is(true));
        Assertion.assertThat("utmSource is correct", hotelPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmSource", utmSource), is(true));
        Assertion.assertThat("utmTerm is correct", hotelPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmTerm", utmTerm), is(true));
        Assertion.assertThat("userId is not null", hotelPageAssertion.verifyUserProperty(Configs.MESSAGES, "userId"), is(true));

        Assertion.assertThat("pageVisit event is found", hotelPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, screenName), is(1));
        Assertion.assertThat("eventAction is correct", hotelPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), is(true));
        Assertion.assertThat("screenName is correct", hotelPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), is(true));
        Assertion.assertThat("utmPage is correct", hotelPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "utmPage", utmPage), is(true));
        Assertion.assertThat("isFirstPage is correct", hotelPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "isFirstPage", isFirstPage), is(true));
        Assertion.assertThat("vertical is correct", hotelPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "vertical", vertical), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "HotelSRP")
    @Tribe(name = "Hotel")
    @Vertical(name = "Core")
    @Test
    public void testHotelSRPPageVisitDeeplinkUtmGuest() {
        Map<String, String> testProperties = getParam("testHotelSRPPageVisitDeeplinkUtmGuest");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        int daysToCheckOutDate = Integer.parseInt(testProperties.get("daysToCheckOutDate"));
        String checkInDate = CoreFunctions.getCurrentDateYYYY_MM_DD();
        String checkOutDate = CoreFunctions.addToDateYYYY_MM_DD(checkInDate, daysToCheckOutDate);
        String deeplink = "tiket://m.tiket.com/hotel/search?room=1&adult=1&id=bali-108001534490276212&type=REGION&q=Bali&checkin=" + checkInDate + "&checkout=" + checkOutDate + "&";

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

        step("Open hotel SRP via deeplink");
        String utmProperties = analyticsHomePage.getUtmProperties(utmCampaign, utmContent, utmId, utmMedium, utmSource, utmTerm);
        analyticsHomePage.openDeeplink(deeplink + utmProperties);

        sleep(40);

        BaseAssertions hotelPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("utmAttributedAt is not null", hotelPageAssertion.verifyUserProperty(Configs.MESSAGES, "utmAttributedAt"), is(true));
        Assertion.assertThat("utmCampaign is correct", hotelPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmCampaign", utmCampaign), is(true));
        Assertion.assertThat("utmContent is correct", hotelPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmContent", utmContent), is(true));
        Assertion.assertThat("utmExternal is correct", hotelPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmExternal", utmExternal), is(true));
        Assertion.assertThat("utmId is correct", hotelPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmId", utmId), is(true));
        Assertion.assertThat("utmMedium is correct", hotelPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmMedium", utmMedium), is(true));
        Assertion.assertThat("utmSource is correct", hotelPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmSource", utmSource), is(true));
        Assertion.assertThat("utmTerm is correct", hotelPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmTerm", utmTerm), is(true));
        Assertion.assertThat("userId is null", hotelPageAssertion.verifyUserProperty(Configs.MESSAGES, "userId"), is(false));

        Assertion.assertThat("pageVisit event is found", hotelPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, screenName), is(1));
        Assertion.assertThat("eventAction is correct", hotelPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), is(true));
        Assertion.assertThat("screenName is correct", hotelPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), is(true));
        Assertion.assertThat("utmPage is correct", hotelPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "utmPage", utmPage), is(true));
        Assertion.assertThat("isFirstPage is correct", hotelPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "isFirstPage", isFirstPage), is(true));
        Assertion.assertThat("vertical is correct", hotelPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "vertical", vertical), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "HotelSRP")
    @Tribe(name = "Hotel")
    @Vertical(name = "Core")
    @Test
    public void testHotelSRPPageVisitDeeplinkUtmLogin() {
        Map<String, String> testProperties = getParam("testHotelSRPPageVisitDeeplinkUtmLogin");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String loginType = testProperties.get("loginType");
        String email = testProperties.get("email");
        String password = testProperties.get("password");

        int daysToCheckOutDate = Integer.parseInt(testProperties.get("daysToCheckOutDate"));
        String checkInDate = CoreFunctions.getCurrentDateYYYY_MM_DD();
        String checkOutDate = CoreFunctions.addToDateYYYY_MM_DD(checkInDate, daysToCheckOutDate);
        String deeplink = "tiket://m.tiket.com/hotel/search?room=1&adult=1&id=bali-108001534490276212&type=REGION&q=Bali&checkin=" + checkInDate + "&checkout=" + checkOutDate + "&";

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

        step("Open hotel SRP via deeplink");
        String utmProperties = analyticsHomePage.getUtmProperties(utmCampaign, utmContent, utmId, utmMedium, utmSource, utmTerm);
        analyticsHomePage.openDeeplink(deeplink + utmProperties);

        sleep(40);

        BaseAssertions hotelPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("utmAttributedAt is not null", hotelPageAssertion.verifyUserProperty(Configs.MESSAGES, "utmAttributedAt"), is(true));
        Assertion.assertThat("utmCampaign is correct", hotelPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmCampaign", utmCampaign), is(true));
        Assertion.assertThat("utmContent is correct", hotelPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmContent", utmContent), is(true));
        Assertion.assertThat("utmExternal is correct", hotelPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmExternal", utmExternal), is(true));
        Assertion.assertThat("utmId is correct", hotelPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmId", utmId), is(true));
        Assertion.assertThat("utmMedium is correct", hotelPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmMedium", utmMedium), is(true));
        Assertion.assertThat("utmSource is correct", hotelPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmSource", utmSource), is(true));
        Assertion.assertThat("utmTerm is correct", hotelPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmTerm", utmTerm), is(true));
        Assertion.assertThat("userId is not null", hotelPageAssertion.verifyUserProperty(Configs.MESSAGES, "userId"), is(true));

        Assertion.assertThat("pageVisit event is found", hotelPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, screenName), is(1));
        Assertion.assertThat("eventAction is correct", hotelPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), is(true));
        Assertion.assertThat("screenName is correct", hotelPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), is(true));
        Assertion.assertThat("utmPage is correct", hotelPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "utmPage", utmPage), is(true));
        Assertion.assertThat("isFirstPage is correct", hotelPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "isFirstPage", isFirstPage), is(true));
        Assertion.assertThat("vertical is correct", hotelPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "vertical", vertical), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "HotelPDP")
    @Tribe(name = "Hotel")
    @Vertical(name = "Core")
    @Test
    public void testHotelPDPPageVisitDeeplinkUtmGuest() {
        Map<String, String> testProperties = getParam("testHotelPDPPageVisitDeeplinkUtmGuest");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        int daysToCheckOutDate = Integer.parseInt(testProperties.get("daysToCheckOutDate"));
        String checkInDate = CoreFunctions.getCurrentDateYYYY_MM_DD();
        String checkOutDate = CoreFunctions.addToDateYYYY_MM_DD(checkInDate, daysToCheckOutDate);
        String deeplink = "tiket://m.tiket.com/hotel/indonesia/infinity8-bali-506001655965152937?room=1&adult=1&checkin=" + checkInDate + "&checkout=" + checkOutDate + "&";

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

        step("Open hotel PDP via deeplink");
        String utmProperties = analyticsHomePage.getUtmProperties(utmCampaign, utmContent, utmId, utmMedium, utmSource, utmTerm);
        analyticsHomePage.openDeeplink(deeplink + utmProperties);

        sleep(40);

        BaseAssertions hotelPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("utmAttributedAt is not null", hotelPageAssertion.verifyUserProperty(Configs.MESSAGES, "utmAttributedAt"), is(true));
        Assertion.assertThat("utmCampaign is correct", hotelPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmCampaign", utmCampaign), is(true));
        Assertion.assertThat("utmContent is correct", hotelPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmContent", utmContent), is(true));
        Assertion.assertThat("utmExternal is correct", hotelPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmExternal", utmExternal), is(true));
        Assertion.assertThat("utmId is correct", hotelPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmId", utmId), is(true));
        Assertion.assertThat("utmMedium is correct", hotelPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmMedium", utmMedium), is(true));
        Assertion.assertThat("utmSource is correct", hotelPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmSource", utmSource), is(true));
        Assertion.assertThat("utmTerm is correct", hotelPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmTerm", utmTerm), is(true));
        Assertion.assertThat("userId is null", hotelPageAssertion.verifyUserProperty(Configs.MESSAGES, "userId"), is(false));

        Assertion.assertThat("pageVisit event is found", hotelPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, screenName), is(1));
        Assertion.assertThat("eventAction is correct", hotelPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), is(true));
        Assertion.assertThat("screenName is correct", hotelPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), is(true));
        Assertion.assertThat("utmPage is correct", hotelPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "utmPage", utmPage), is(true));
        Assertion.assertThat("isFirstPage is correct", hotelPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "isFirstPage", isFirstPage), is(true));
        Assertion.assertThat("vertical is correct", hotelPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "vertical", vertical), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "HotelPDP")
    @Tribe(name = "Hotel")
    @Vertical(name = "Core")
    @Test
    public void testHotelPDPPageVisitDeeplinkUtmLogin() {
        Map<String, String> testProperties = getParam("testHotelPDPPageVisitDeeplinkUtmLogin");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String loginType = testProperties.get("loginType");
        String email = testProperties.get("email");
        String password = testProperties.get("password");

        int daysToCheckOutDate = Integer.parseInt(testProperties.get("daysToCheckOutDate"));
        String checkInDate = CoreFunctions.getCurrentDateYYYY_MM_DD();
        String checkOutDate = CoreFunctions.addToDateYYYY_MM_DD(checkInDate, daysToCheckOutDate);
        String deeplink = "tiket://m.tiket.com/hotel/indonesia/infinity8-bali-506001655965152937?room=1&adult=1&checkin=" + checkInDate + "&checkout=" + checkOutDate + "&";

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

        step("Open hotel PDP via deeplink");
        String utmProperties = analyticsHomePage.getUtmProperties(utmCampaign, utmContent, utmId, utmMedium, utmSource, utmTerm);
        analyticsHomePage.openDeeplink(deeplink + utmProperties);

        sleep(40);

        BaseAssertions hotelPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("utmAttributedAt is not null", hotelPageAssertion.verifyUserProperty(Configs.MESSAGES, "utmAttributedAt"), is(true));
        Assertion.assertThat("utmCampaign is correct", hotelPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmCampaign", utmCampaign), is(true));
        Assertion.assertThat("utmContent is correct", hotelPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmContent", utmContent), is(true));
        Assertion.assertThat("utmExternal is correct", hotelPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmExternal", utmExternal), is(true));
        Assertion.assertThat("utmId is correct", hotelPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmId", utmId), is(true));
        Assertion.assertThat("utmMedium is correct", hotelPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmMedium", utmMedium), is(true));
        Assertion.assertThat("utmSource is correct", hotelPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmSource", utmSource), is(true));
        Assertion.assertThat("utmTerm is correct", hotelPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmTerm", utmTerm), is(true));
        Assertion.assertThat("userId is not null", hotelPageAssertion.verifyUserProperty(Configs.MESSAGES, "userId"), is(true));

        Assertion.assertThat("pageVisit event is found", hotelPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, screenName), is(1));
        Assertion.assertThat("eventAction is correct", hotelPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), is(true));
        Assertion.assertThat("screenName is correct", hotelPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), is(true));
        Assertion.assertThat("utmPage is correct", hotelPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "utmPage", utmPage), is(true));
        Assertion.assertThat("isFirstPage is correct", hotelPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "isFirstPage", isFirstPage), is(true));
        Assertion.assertThat("vertical is correct", hotelPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "vertical", vertical), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "HotelMainPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Core")
    @Test
    public void testHotelMainPageVisitDeeplinkNonUtmGuest() {
        Map<String, String> testProperties = getParam("testHotelMainPageVisitDeeplinkNonUtmGuest");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String deeplink = "tiket://m.tiket.com/hotel";

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

        step("Open hotel vertical via deeplink");
        analyticsHomePage.openDeeplink(deeplink);

        sleep(40);

        BaseAssertions hotelPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("utmAttributedAt is not null", hotelPageAssertion.verifyUserProperty(Configs.MESSAGES, "utmAttributedAt"), is(true));
        Assertion.assertThat("utmCampaign is correct", hotelPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmCampaign", utmCampaign), is(true));
        Assertion.assertThat("utmContent is correct", hotelPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmContent", utmContent), is(true));
        Assertion.assertThat("utmExternal is correct", hotelPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmExternal", utmExternal), is(true));
        Assertion.assertThat("utmId is correct", hotelPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmId", utmId), is(true));
        Assertion.assertThat("utmMedium is correct", hotelPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmMedium", utmMedium), is(true));
        Assertion.assertThat("utmSource is correct", hotelPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmSource", utmSource), is(true));
        Assertion.assertThat("utmTerm is correct", hotelPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmTerm", utmTerm), is(true));
        Assertion.assertThat("userId is null", hotelPageAssertion.verifyUserProperty(Configs.MESSAGES, "userId"), is(false));

        Assertion.assertThat("pageVisit event is found", hotelPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, screenName), is(1));
        Assertion.assertThat("eventAction is correct", hotelPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), is(true));
        Assertion.assertThat("screenName is correct", hotelPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), is(true));
        Assertion.assertThat("utmPage is correct", hotelPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "utmPage", utmPage), is(true));
        Assertion.assertThat("isFirstPage is correct", hotelPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "isFirstPage", isFirstPage), is(true));
        Assertion.assertThat("vertical is correct", hotelPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "vertical", vertical), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "HotelMainPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Core")
    @Test
    public void testHotelMainPageVisitDeeplinkNonUtmLogin() {
        Map<String, String> testProperties = getParam("testHotelMainPageVisitDeeplinkNonUtmLogin");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String loginType = testProperties.get("loginType");
        String email = testProperties.get("email");
        String password = testProperties.get("password");
        String deeplink = "tiket://m.tiket.com/hotel";

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

        step("Open hotel vertical via deeplink");
        analyticsHomePage.openDeeplink(deeplink);

        sleep(40);

        BaseAssertions hotelPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("utmAttributedAt is not null", hotelPageAssertion.verifyUserProperty(Configs.MESSAGES, "utmAttributedAt"), is(true));
        Assertion.assertThat("utmCampaign is correct", hotelPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmCampaign", utmCampaign), is(true));
        Assertion.assertThat("utmContent is correct", hotelPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmContent", utmContent), is(true));
        Assertion.assertThat("utmExternal is correct", hotelPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmExternal", utmExternal), is(true));
        Assertion.assertThat("utmId is correct", hotelPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmId", utmId), is(true));
        Assertion.assertThat("utmMedium is correct", hotelPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmMedium", utmMedium), is(true));
        Assertion.assertThat("utmSource is correct", hotelPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmSource", utmSource), is(true));
        Assertion.assertThat("utmTerm is correct", hotelPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmTerm", utmTerm), is(true));
        Assertion.assertThat("userId is not null", hotelPageAssertion.verifyUserProperty(Configs.MESSAGES, "userId"), is(true));

        Assertion.assertThat("pageVisit event is found", hotelPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, screenName), is(1));
        Assertion.assertThat("eventAction is correct", hotelPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), is(true));
        Assertion.assertThat("screenName is correct", hotelPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), is(true));
        Assertion.assertThat("utmPage is correct", hotelPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "utmPage", utmPage), is(true));
        Assertion.assertThat("isFirstPage is correct", hotelPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "isFirstPage", isFirstPage), is(true));
        Assertion.assertThat("vertical is correct", hotelPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "vertical", vertical), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "HotelSRP")
    @Tribe(name = "Hotel")
    @Vertical(name = "Core")
    @Test
    public void testHotelSRPPageVisitDeeplinkNonUtmGuest() {
        Map<String, String> testProperties = getParam("testHotelSRPPageVisitDeeplinkNonUtmGuest");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        int daysToCheckOutDate = Integer.parseInt(testProperties.get("daysToCheckOutDate"));
        String checkInDate = CoreFunctions.getCurrentDateYYYY_MM_DD();
        String checkOutDate = CoreFunctions.addToDateYYYY_MM_DD(checkInDate, daysToCheckOutDate);
        String deeplink = "tiket://m.tiket.com/hotel/search?room=1&adult=1&id=bali-108001534490276212&type=REGION&q=Bali&checkin=" + checkInDate + "&checkout=" + checkOutDate;

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

        step("Open hotel SRP via deeplink");
        analyticsHomePage.openDeeplink(deeplink);

        sleep(40);

        BaseAssertions hotelPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("utmAttributedAt is not null", hotelPageAssertion.verifyUserProperty(Configs.MESSAGES, "utmAttributedAt"), is(true));
        Assertion.assertThat("utmCampaign is correct", hotelPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmCampaign", utmCampaign), is(true));
        Assertion.assertThat("utmContent is correct", hotelPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmContent", utmContent), is(true));
        Assertion.assertThat("utmExternal is correct", hotelPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmExternal", utmExternal), is(true));
        Assertion.assertThat("utmId is correct", hotelPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmId", utmId), is(true));
        Assertion.assertThat("utmMedium is correct", hotelPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmMedium", utmMedium), is(true));
        Assertion.assertThat("utmSource is correct", hotelPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmSource", utmSource), is(true));
        Assertion.assertThat("utmTerm is correct", hotelPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmTerm", utmTerm), is(true));
        Assertion.assertThat("userId is null", hotelPageAssertion.verifyUserProperty(Configs.MESSAGES, "userId"), is(false));

        Assertion.assertThat("pageVisit event is found", hotelPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, screenName), is(1));
        Assertion.assertThat("eventAction is correct", hotelPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), is(true));
        Assertion.assertThat("screenName is correct", hotelPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), is(true));
        Assertion.assertThat("utmPage is correct", hotelPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "utmPage", utmPage), is(true));
        Assertion.assertThat("isFirstPage is correct", hotelPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "isFirstPage", isFirstPage), is(true));
        Assertion.assertThat("vertical is correct", hotelPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "vertical", vertical), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "HotelSRP")
    @Tribe(name = "Hotel")
    @Vertical(name = "Core")
    @Test
    public void testHotelSRPPageVisitDeeplinkNonUtmLogin() {
        Map<String, String> testProperties = getParam("testHotelSRPPageVisitDeeplinkNonUtmLogin");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String loginType = testProperties.get("loginType");
        String email = testProperties.get("email");
        String password = testProperties.get("password");

        int daysToCheckOutDate = Integer.parseInt(testProperties.get("daysToCheckOutDate"));
        String checkInDate = CoreFunctions.getCurrentDateYYYY_MM_DD();
        String checkOutDate = CoreFunctions.addToDateYYYY_MM_DD(checkInDate, daysToCheckOutDate);
        String deeplink = "tiket://m.tiket.com/hotel/search?room=1&adult=1&id=bali-108001534490276212&type=REGION&q=Bali&checkin=" + checkInDate + "&checkout=" + checkOutDate;

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

        step("Open hotel SRP via deeplink");
        analyticsHomePage.openDeeplink(deeplink);

        sleep(40);

        BaseAssertions hotelPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("utmAttributedAt is not null", hotelPageAssertion.verifyUserProperty(Configs.MESSAGES, "utmAttributedAt"), is(true));
        Assertion.assertThat("utmCampaign is correct", hotelPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmCampaign", utmCampaign), is(true));
        Assertion.assertThat("utmContent is correct", hotelPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmContent", utmContent), is(true));
        Assertion.assertThat("utmExternal is correct", hotelPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmExternal", utmExternal), is(true));
        Assertion.assertThat("utmId is correct", hotelPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmId", utmId), is(true));
        Assertion.assertThat("utmMedium is correct", hotelPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmMedium", utmMedium), is(true));
        Assertion.assertThat("utmSource is correct", hotelPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmSource", utmSource), is(true));
        Assertion.assertThat("utmTerm is correct", hotelPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmTerm", utmTerm), is(true));
        Assertion.assertThat("userId is not null", hotelPageAssertion.verifyUserProperty(Configs.MESSAGES, "userId"), is(true));

        Assertion.assertThat("pageVisit event is found", hotelPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, screenName), is(1));
        Assertion.assertThat("eventAction is correct", hotelPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), is(true));
        Assertion.assertThat("screenName is correct", hotelPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), is(true));
        Assertion.assertThat("utmPage is correct", hotelPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "utmPage", utmPage), is(true));
        Assertion.assertThat("isFirstPage is correct", hotelPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "isFirstPage", isFirstPage), is(true));
        Assertion.assertThat("vertical is correct", hotelPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "vertical", vertical), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "HotelPDP")
    @Tribe(name = "Hotel")
    @Vertical(name = "Core")
    @Test
    public void testHotelPDPPageVisitDeeplinkNonUtmGuest() {
        Map<String, String> testProperties = getParam("testHotelPDPPageVisitDeeplinkNonUtmGuest");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        int daysToCheckOutDate = Integer.parseInt(testProperties.get("daysToCheckOutDate"));
        String checkInDate = CoreFunctions.getCurrentDateYYYY_MM_DD();
        String checkOutDate = CoreFunctions.addToDateYYYY_MM_DD(checkInDate, daysToCheckOutDate);
        String deeplink = "tiket://m.tiket.com/hotel/indonesia/infinity8-bali-506001655965152937?room=1&adult=1&checkin=" + checkInDate + "&checkout=" + checkOutDate;

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

        step("Open hotel PDP via deeplink");
        analyticsHomePage.openDeeplink(deeplink);

        sleep(40);

        BaseAssertions hotelPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("utmAttributedAt is not null", hotelPageAssertion.verifyUserProperty(Configs.MESSAGES, "utmAttributedAt"), is(true));
        Assertion.assertThat("utmCampaign is correct", hotelPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmCampaign", utmCampaign), is(true));
        Assertion.assertThat("utmContent is correct", hotelPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmContent", utmContent), is(true));
        Assertion.assertThat("utmExternal is correct", hotelPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmExternal", utmExternal), is(true));
        Assertion.assertThat("utmId is correct", hotelPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmId", utmId), is(true));
        Assertion.assertThat("utmMedium is correct", hotelPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmMedium", utmMedium), is(true));
        Assertion.assertThat("utmSource is correct", hotelPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmSource", utmSource), is(true));
        Assertion.assertThat("utmTerm is correct", hotelPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmTerm", utmTerm), is(true));
        Assertion.assertThat("userId is null", hotelPageAssertion.verifyUserProperty(Configs.MESSAGES, "userId"), is(false));

        Assertion.assertThat("pageVisit event is found", hotelPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, screenName), is(1));
        Assertion.assertThat("eventAction is correct", hotelPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), is(true));
        Assertion.assertThat("screenName is correct", hotelPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), is(true));
        Assertion.assertThat("utmPage is correct", hotelPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "utmPage", utmPage), is(true));
        Assertion.assertThat("isFirstPage is correct", hotelPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "isFirstPage", isFirstPage), is(true));
        Assertion.assertThat("vertical is correct", hotelPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "vertical", vertical), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "HotelPDP")
    @Tribe(name = "Hotel")
    @Vertical(name = "Core")
    @Test
    public void testHotelPDPPageVisitDeeplinkNonUtmLogin() {
        Map<String, String> testProperties = getParam("testHotelPDPPageVisitDeeplinkNonUtmLogin");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String loginType = testProperties.get("loginType");
        String email = testProperties.get("email");
        String password = testProperties.get("password");

        int daysToCheckOutDate = Integer.parseInt(testProperties.get("daysToCheckOutDate"));
        String checkInDate = CoreFunctions.getCurrentDateYYYY_MM_DD();
        String checkOutDate = CoreFunctions.addToDateYYYY_MM_DD(checkInDate, daysToCheckOutDate);
        String deeplink = "tiket://m.tiket.com/hotel/indonesia/infinity8-bali-506001655965152937?room=1&adult=1&checkin=" + checkInDate + "&checkout=" + checkOutDate;

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

        step("Open hotel PDP via deeplink");
        analyticsHomePage.openDeeplink(deeplink);

        sleep(40);

        BaseAssertions hotelPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("utmAttributedAt is not null", hotelPageAssertion.verifyUserProperty(Configs.MESSAGES, "utmAttributedAt"), is(true));
        Assertion.assertThat("utmCampaign is correct", hotelPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmCampaign", utmCampaign), is(true));
        Assertion.assertThat("utmContent is correct", hotelPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmContent", utmContent), is(true));
        Assertion.assertThat("utmExternal is correct", hotelPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmExternal", utmExternal), is(true));
        Assertion.assertThat("utmId is correct", hotelPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmId", utmId), is(true));
        Assertion.assertThat("utmMedium is correct", hotelPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmMedium", utmMedium), is(true));
        Assertion.assertThat("utmSource is correct", hotelPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmSource", utmSource), is(true));
        Assertion.assertThat("utmTerm is correct", hotelPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmTerm", utmTerm), is(true));
        Assertion.assertThat("userId is not null", hotelPageAssertion.verifyUserProperty(Configs.MESSAGES, "userId"), is(true));

        Assertion.assertThat("pageVisit event is found", hotelPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, screenName), is(1));
        Assertion.assertThat("eventAction is correct", hotelPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), is(true));
        Assertion.assertThat("screenName is correct", hotelPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), is(true));
        Assertion.assertThat("utmPage is correct", hotelPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "utmPage", utmPage), is(true));
        Assertion.assertThat("isFirstPage is correct", hotelPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "isFirstPage", isFirstPage), is(true));
        Assertion.assertThat("vertical is correct", hotelPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "vertical", vertical), is(true));
    }
}