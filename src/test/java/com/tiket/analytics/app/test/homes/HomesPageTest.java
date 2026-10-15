package com.tiket.analytics.app.test.homes;

import com.tiket.analytics.BaseAnalyticsTest;
import com.tiket.analytics.assertions.BaseAssertions;
import com.tiket.analytics.app.home.AnalyticsHomePage;
import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Session;
import com.tiket.page.accommodation.homes.homeslandingpage.HomesLandingPage;
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

public class HomesPageTest extends BaseAnalyticsTest {

    @Author(names = {"Okta"})
    @Module(name = "HomesMainPage")
    @Tribe(name = "Homes")
    @Vertical(name = "Core")
    @Test
    public void testHomesMainPageVisitGuest() {
        Map<String, String> testProperties = getParam("testHomesMainPageVisitGuest");
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

        step("Open homes vertical");
        homePage.openVerticalPage("Homes");

        sleep(40);

        BaseAssertions homesPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("pageVisit event is found", homesPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, screenName), is(1));
        Assertion.assertThat("eventAction is correct", homesPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), is(true));
        Assertion.assertThat("screenName is correct", homesPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), is(true));
        Assertion.assertThat("utmPage is correct", homesPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "utmPage", utmPage), is(true));
        Assertion.assertThat("isFirstPage is correct", homesPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "isFirstPage", isFirstPage), is(true));
        Assertion.assertThat("vertical is correct", homesPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "vertical", vertical), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "HomesPDP")
    @Tribe(name = "Homes")
    @Vertical(name = "Core")
    @Test
    public void testHomesPDPPageVisitGuest() {
        Map<String, String> testProperties = getParam("testHomesPDPPageVisitGuest");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();
        String location = testProperties.get("location");

        String event = testProperties.get("event");
        String eventAction = testProperties.get("eventAction");
        String screenName = testProperties.get("screenName");
        String utmPage = testProperties.get("utmPage");
        String isFirstPage = testProperties.get("isFirstPage");
        String vertical = testProperties.get("vertical");

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> hotelLandingPage = LandingPage.getInstance(session);
        HomesLandingPage<WebDriver> homesLandingPage = HomesLandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);

        step("Go to home");
        homePage.gotoHome();

        step("Open app as guest");
        loginPage.loginAsGuest();

        step("Open homes landing page");
        homePage.openVerticalPage("Homes");

        step("Input homes location");
        homesLandingPage.searchStayLocation(location);

        step("Search homes");
        hotelLandingPage.clickSubmit();
        hotelLandingPage.fillNightCheckInForm();

        step("Select homes");
        searchResultPage.selectFirstHotelCard();

        sleep(40);

        BaseAssertions homesPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("pageVisit event is found", homesPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, screenName), is(1));
        Assertion.assertThat("eventAction is correct", homesPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), is(true));
        Assertion.assertThat("screenName is correct", homesPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), is(true));
        Assertion.assertThat("utmPage is correct", homesPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "utmPage", utmPage), is(true));
        Assertion.assertThat("isFirstPage is correct", homesPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "isFirstPage", isFirstPage), is(true));
        Assertion.assertThat("vertical is correct", homesPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "vertical", vertical), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "HomesBookingPage")
    @Tribe(name = "Homes")
    @Vertical(name = "Core")
    @Test
    public void testHomesBookingPageVisitGuest() {
        Map<String, String> testProperties = getParam("testHomesBookingPageVisitGuest");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();
        String location = testProperties.get("location");

        String event = testProperties.get("event");
        String eventAction = testProperties.get("eventAction");
        String screenName = testProperties.get("screenName");
        String utmPage = testProperties.get("utmPage");
        String isFirstPage = testProperties.get("isFirstPage");
        String vertical = testProperties.get("vertical");

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> hotelLandingPage = LandingPage.getInstance(session);
        HomesLandingPage<WebDriver> homesLandingPage = HomesLandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);

        step("Go to home");
        homePage.gotoHome();

        step("Open app as guest");
        loginPage.loginAsGuest();

        step("Open homes landing page");
        homePage.openVerticalPage("Homes");

        step("Input homes location");
        homesLandingPage.searchStayLocation(location);

        step("Search homes");
        hotelLandingPage.clickSubmit();
        hotelLandingPage.fillNightCheckInForm();

        step("Select homes");
        searchResultPage.selectFirstHotelCard();

        step("Scroll down to room list");
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();

        step("Open homes booking page");
        roomListPage.getTheFirstHotelRoomPrice();
        roomListPage.clickBookRoom();

        sleep(40);

        BaseAssertions homesPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("pageVisit event is found", homesPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, screenName), is(1));
        Assertion.assertThat("eventAction is correct", homesPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), is(true));
        Assertion.assertThat("screenName is correct", homesPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), is(true));
        Assertion.assertThat("utmPage is correct", homesPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "utmPage", utmPage), is(true));
        Assertion.assertThat("isFirstPage is correct", homesPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "isFirstPage", isFirstPage), is(true));
        Assertion.assertThat("vertical is correct", homesPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "vertical", vertical), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "HomesSRP")
    @Tribe(name = "Homes")
    @Vertical(name = "Core")
    @Test
    public void testHomesSRPPageVisitGuest() {
        Map<String, String> testProperties = getParam("testHomesSRPPageVisitGuest");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();
        String location = testProperties.get("location");

        String event = testProperties.get("event");
        String eventAction = testProperties.get("eventAction");
        String screenName = testProperties.get("screenName");
        String utmPage = testProperties.get("utmPage");
        String isFirstPage = testProperties.get("isFirstPage");
        String vertical = testProperties.get("vertical");

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> hotelLandingPage = LandingPage.getInstance(session);
        HomesLandingPage<WebDriver> homesLandingPage = HomesLandingPage.getInstance(session);

        step("Go to home");
        homePage.gotoHome();

        step("Open app as guest");
        loginPage.loginAsGuest();

        step("Open homes landing page");
        homePage.openVerticalPage("Homes");

        step("Input homes location");
        homesLandingPage.searchStayLocation(location);

        step("Search homes");
        hotelLandingPage.clickSubmit();
        hotelLandingPage.fillNightCheckInForm();

        sleep(40);

        BaseAssertions homesPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("pageVisit event is found", homesPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, screenName), is(1));
        Assertion.assertThat("eventAction is correct", homesPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), is(true));
        Assertion.assertThat("screenName is correct", homesPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), is(true));
        Assertion.assertThat("utmPage is correct", homesPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "utmPage", utmPage), is(true));
        Assertion.assertThat("isFirstPage is correct", homesPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "isFirstPage", isFirstPage), is(true));
        Assertion.assertThat("vertical is correct", homesPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "vertical", vertical), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "HomesMainPage")
    @Tribe(name = "Homes")
    @Vertical(name = "Core")
    @Test
    public void testHomesEnterVerticalGuest() {
        Map<String, String> testProperties = getParam("testHomesEnterVerticalGuest");
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

        step("Open homes vertical");
        homePage.openVerticalPage("Homes");

        sleep(40);

        BaseAssertions homesPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("enterVertical event is found", homesPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, vertical), is(1));
        Assertion.assertThat("screenName is correct", homesPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "screenName", screenName), is(true));
        Assertion.assertThat("vertical is correct", homesPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "vertical", vertical), is(true));
        Assertion.assertThat("eventAction is correct", homesPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "eventAction", eventAction), is(true));
        Assertion.assertThat("eventLabel is correct", homesPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "eventLabel", eventLabel), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "HomesSRP")
    @Tribe(name = "Homes")
    @Vertical(name = "Core")
    @Test
    public void testHomesSearchLoadedGuest() {
        Map<String, String> testProperties = getParam("testHomesSearchLoadedGuest");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();
        String location = testProperties.get("location");

        String event = testProperties.get("event");
        String screenName = testProperties.get("screenName");
        String vertical = testProperties.get("vertical");
        String eventAction = testProperties.get("eventAction");
        String eventLabel = testProperties.get("eventLabel");

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> hotelLandingPage = LandingPage.getInstance(session);
        HomesLandingPage<WebDriver> homesLandingPage = HomesLandingPage.getInstance(session);

        step("Go to home");
        homePage.gotoHome();

        step("Open app as guest");
        loginPage.loginAsGuest();

        step("Open homes landing page");
        homePage.openVerticalPage("Homes");

        step("Input homes location");
        homesLandingPage.searchStayLocation(location);

        step("Search homes");
        hotelLandingPage.clickSubmit();
        hotelLandingPage.fillNightCheckInForm();

        sleep(40);

        BaseAssertions homesPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("searchLoaded event is found", homesPageAssertion.verifyEvent(Configs.MESSAGES, event), is(1));
        Assertion.assertThat("screenName is correct", homesPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "screenName", screenName), is(true));
        Assertion.assertThat("vertical is correct", homesPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "vertical", vertical), is(true));
        Assertion.assertThat("eventAction is correct", homesPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "eventAction", eventAction), is(true));
        Assertion.assertThat("eventLabel is correct", homesPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "eventLabel", eventLabel), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "HomesSRP")
    @Tribe(name = "Homes")
    @Vertical(name = "Core")
    @Test
    public void testHomesSearchProductGuest() {
        Map<String, String> testProperties = getParam("testHomesSearchProductGuest");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();
        String location = testProperties.get("location");

        String event = testProperties.get("event");
        String screenName = testProperties.get("screenName");
        String vertical = testProperties.get("vertical");
        String eventAction = testProperties.get("eventAction");
        String eventLabel = testProperties.get("eventLabel");

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> hotelLandingPage = LandingPage.getInstance(session);
        HomesLandingPage<WebDriver> homesLandingPage = HomesLandingPage.getInstance(session);

        step("Go to home");
        homePage.gotoHome();

        step("Open app as guest");
        loginPage.loginAsGuest();

        step("Open homes landing page");
        homePage.openVerticalPage("Homes");

        step("Input homes location");
        homesLandingPage.searchStayLocation(location);

        step("Search homes");
        hotelLandingPage.clickSubmit();
        hotelLandingPage.fillNightCheckInForm();

        sleep(40);

        BaseAssertions homesPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("searchProduct event is found", homesPageAssertion.verifyEvent(Configs.MESSAGES, event), is(1));
        Assertion.assertThat("screenName is correct", homesPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "screenName", screenName), is(true));
        Assertion.assertThat("vertical is correct", homesPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "vertical", vertical), is(true));
        Assertion.assertThat("eventAction is correct", homesPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "eventAction", eventAction), is(true));
        Assertion.assertThat("eventLabel is correct", homesPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "eventLabel", eventLabel), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "HomesBookingPage")
    @Tribe(name = "Homes")
    @Vertical(name = "Core")
    @Test
    public void testHomesBookProductGuest() {
        Map<String, String> testProperties = getParam("testHomesBookProductGuest");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();
        String location = testProperties.get("location");

        String event = testProperties.get("event");
        String screenName = testProperties.get("screenName");
        String vertical = testProperties.get("vertical");
        String eventAction = testProperties.get("eventAction");
        String eventDescription = testProperties.get("eventDescription");
        String eventLabel = testProperties.get("eventLabel");

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> hotelLandingPage = LandingPage.getInstance(session);
        HomesLandingPage<WebDriver> homesLandingPage = HomesLandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);

        step("Go to home");
        homePage.gotoHome();

        step("Open app as guest");
        loginPage.loginAsGuest();

        step("Open homes landing page");
        homePage.openVerticalPage("Homes");

        step("Input homes location");
        homesLandingPage.searchStayLocation(location);

        step("Search homes");
        hotelLandingPage.clickSubmit();
        hotelLandingPage.fillNightCheckInForm();

        step("Select homes");
        searchResultPage.selectFirstHotelCard();

        step("Scroll down to room list");
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();

        step("Open homes booking page");
        roomListPage.getTheFirstHotelRoomPrice();
        roomListPage.clickBookRoom();

        step("Fill booking detail");
        bookingFormPage.fillContactOrderDetail();
        bookingFormPage.fillGuestDetail(false);

        step("Submit booking");
        bookingFormPage.continueToPaymentPage();

        sleep(40);

        BaseAssertions homesPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("bookProduct event is found", homesPageAssertion.verifyEvent(Configs.MESSAGES, event), greaterThan(0));
        Assertion.assertThat("screenName is correct", homesPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "screenName", screenName), is(true));
        Assertion.assertThat("vertical is correct", homesPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "vertical", vertical), is(true));
        Assertion.assertThat("eventAction is correct", homesPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "eventAction", eventAction), is(true));
        Assertion.assertThat("eventDescription is correct", homesPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "eventDescription", eventDescription), is(true));
        Assertion.assertThat("eventLabel is correct", homesPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "eventLabel", eventLabel), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "HomesMainPage")
    @Tribe(name = "Homes")
    @Vertical(name = "Core")
    @Test
    public void testHomesMainPageVisitLogin() {
        Map<String, String> testProperties = getParam("testHomesMainPageVisitLogin");
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

        step("Open homes vertical");
        homePage.openVerticalPage("Homes");

        sleep(40);

        BaseAssertions homesPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("pageVisit event is found", homesPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, screenName), is(1));
        Assertion.assertThat("eventAction is correct", homesPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), is(true));
        Assertion.assertThat("screenName is correct", homesPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), is(true));
        Assertion.assertThat("utmPage is correct", homesPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "utmPage", utmPage), is(true));
        Assertion.assertThat("isFirstPage is correct", homesPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "isFirstPage", isFirstPage), is(true));
        Assertion.assertThat("vertical is correct", homesPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "vertical", vertical), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "HomesPDP")
    @Tribe(name = "Homes")
    @Vertical(name = "Core")
    @Test
    public void testHomesPDPPageVisitLogin() {
        Map<String, String> testProperties = getParam("testHomesPDPPageVisitLogin");
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

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> hotelLandingPage = LandingPage.getInstance(session);
        HomesLandingPage<WebDriver> homesLandingPage = HomesLandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);

        step("Go to home");
        homePage.gotoHome();

        step("Open app as login user");
        loginPage.loginWith(loginType, email, password);

        step("Open homes landing page");
        homePage.openVerticalPage("Homes");

        step("Input homes location");
        homesLandingPage.searchStayLocation(location);

        step("Search homes");
        hotelLandingPage.clickSubmit();
        hotelLandingPage.fillNightCheckInForm();

        step("Select homes");
        searchResultPage.selectFirstHotelCard();

        sleep(40);

        BaseAssertions homesPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("pageVisit event is found", homesPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, screenName), is(1));
        Assertion.assertThat("eventAction is correct", homesPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), is(true));
        Assertion.assertThat("screenName is correct", homesPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), is(true));
        Assertion.assertThat("utmPage is correct", homesPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "utmPage", utmPage), is(true));
        Assertion.assertThat("isFirstPage is correct", homesPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "isFirstPage", isFirstPage), is(true));
        Assertion.assertThat("vertical is correct", homesPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "vertical", vertical), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "HomesBookingPage")
    @Tribe(name = "Homes")
    @Vertical(name = "Core")
    @Test
    public void testHomesBookingPageVisitLogin() {
        Map<String, String> testProperties = getParam("testHomesBookingPageVisitLogin");
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

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> hotelLandingPage = LandingPage.getInstance(session);
        HomesLandingPage<WebDriver> homesLandingPage = HomesLandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);

        step("Go to home");
        homePage.gotoHome();

        step("Open app as login user");
        loginPage.loginWith(loginType, email, password);

        step("Open homes landing page");
        homePage.openVerticalPage("Homes");

        step("Input homes location");
        homesLandingPage.searchStayLocation(location);

        step("Search homes");
        hotelLandingPage.clickSubmit();
        hotelLandingPage.fillNightCheckInForm();

        step("Select homes");
        searchResultPage.selectFirstHotelCard();

        step("Scroll down to room list");
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();

        step("Open homes booking page");
        roomListPage.getTheFirstHotelRoomPrice();
        roomListPage.clickBookRoom();

        sleep(40);

        BaseAssertions homesPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("pageVisit event is found", homesPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, screenName), is(1));
        Assertion.assertThat("eventAction is correct", homesPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), is(true));
        Assertion.assertThat("screenName is correct", homesPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), is(true));
        Assertion.assertThat("utmPage is correct", homesPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "utmPage", utmPage), is(true));
        Assertion.assertThat("isFirstPage is correct", homesPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "isFirstPage", isFirstPage), is(true));
        Assertion.assertThat("vertical is correct", homesPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "vertical", vertical), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "HomesSRP")
    @Tribe(name = "Homes")
    @Vertical(name = "Core")
    @Test
    public void testHomesSRPPageVisitLogin() {
        Map<String, String> testProperties = getParam("testHomesSRPPageVisitLogin");
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

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> hotelLandingPage = LandingPage.getInstance(session);
        HomesLandingPage<WebDriver> homesLandingPage = HomesLandingPage.getInstance(session);

        step("Go to home");
        homePage.gotoHome();

        step("Open app as login user");
        loginPage.loginWith(loginType, email, password);

        step("Open homes landing page");
        homePage.openVerticalPage("Homes");

        step("Input homes location");
        homesLandingPage.searchStayLocation(location);

        step("Search homes");
        hotelLandingPage.clickSubmit();
        hotelLandingPage.fillNightCheckInForm();

        sleep(40);

        BaseAssertions homesPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("pageVisit event is found", homesPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, screenName), is(1));
        Assertion.assertThat("eventAction is correct", homesPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), is(true));
        Assertion.assertThat("screenName is correct", homesPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), is(true));
        Assertion.assertThat("utmPage is correct", homesPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "utmPage", utmPage), is(true));
        Assertion.assertThat("isFirstPage is correct", homesPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "isFirstPage", isFirstPage), is(true));
        Assertion.assertThat("vertical is correct", homesPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "vertical", vertical), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "HomesMainPage")
    @Tribe(name = "Homes")
    @Vertical(name = "Core")
    @Test
    public void testHomesEnterVerticalLogin() {
        Map<String, String> testProperties = getParam("testHomesEnterVerticalLogin");
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

        step("Open homes vertical");
        homePage.openVerticalPage("Homes");

        sleep(40);

        BaseAssertions homesPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("enterVertical event is found", homesPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, vertical), is(1));
        Assertion.assertThat("screenName is correct", homesPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "screenName", screenName), is(true));
        Assertion.assertThat("vertical is correct", homesPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "vertical", vertical), is(true));
        Assertion.assertThat("eventAction is correct", homesPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "eventAction", eventAction), is(true));
        Assertion.assertThat("eventLabel is correct", homesPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "eventLabel", eventLabel), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "HomesSRP")
    @Tribe(name = "Homes")
    @Vertical(name = "Core")
    @Test
    public void testHomesSearchLoadedLogin() {
        Map<String, String> testProperties = getParam("testHomesSearchLoadedLogin");
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

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> hotelLandingPage = LandingPage.getInstance(session);
        HomesLandingPage<WebDriver> homesLandingPage = HomesLandingPage.getInstance(session);

        step("Go to home");
        homePage.gotoHome();

        step("Open app as login user");
        loginPage.loginWith(loginType, email, password);

        step("Open homes landing page");
        homePage.openVerticalPage("Homes");

        step("Input homes location");
        homesLandingPage.searchStayLocation(location);

        step("Search homes");
        hotelLandingPage.clickSubmit();
        hotelLandingPage.fillNightCheckInForm();

        sleep(40);

        BaseAssertions homesPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("searchLoaded event is found", homesPageAssertion.verifyEvent(Configs.MESSAGES, event), is(1));
        Assertion.assertThat("screenName is correct", homesPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "screenName", screenName), is(true));
        Assertion.assertThat("vertical is correct", homesPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "vertical", vertical), is(true));
        Assertion.assertThat("eventAction is correct", homesPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "eventAction", eventAction), is(true));
        Assertion.assertThat("eventLabel is correct", homesPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "eventLabel", eventLabel), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "HomesSRP")
    @Tribe(name = "Homes")
    @Vertical(name = "Core")
    @Test
    public void testHomesSearchProductLogin() {
        Map<String, String> testProperties = getParam("testHomesSearchProductLogin");
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

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> hotelLandingPage = LandingPage.getInstance(session);
        HomesLandingPage<WebDriver> homesLandingPage = HomesLandingPage.getInstance(session);

        step("Go to home");
        homePage.gotoHome();

        step("Open app as login user");
        loginPage.loginWith(loginType, email, password);

        step("Open homes landing page");
        homePage.openVerticalPage("Homes");

        step("Input homes location");
        homesLandingPage.searchStayLocation(location);

        step("Search homes");
        hotelLandingPage.clickSubmit();
        hotelLandingPage.fillNightCheckInForm();

        sleep(60);

        BaseAssertions homesPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("searchProduct event is found", homesPageAssertion.verifyEvent(Configs.MESSAGES, event), is(1));
        Assertion.assertThat("screenName is correct", homesPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "screenName", screenName), is(true));
        Assertion.assertThat("vertical is correct", homesPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "vertical", vertical), is(true));
        Assertion.assertThat("eventAction is correct", homesPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "eventAction", eventAction), is(true));
        Assertion.assertThat("eventLabel is correct", homesPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "eventLabel", eventLabel), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "HomesBookingPage")
    @Tribe(name = "Homes")
    @Vertical(name = "Core")
    @Test
    public void testHomesBookProductLogin() {
        Map<String, String> testProperties = getParam("testHomesBookProductLogin");
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

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> hotelLandingPage = LandingPage.getInstance(session);
        HomesLandingPage<WebDriver> homesLandingPage = HomesLandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);

        step("Go to home");
        homePage.gotoHome();

        step("Open app as login user");
        loginPage.loginWith(loginType, email, password);

        step("Open homes landing page");
        homePage.openVerticalPage("Homes");

        step("Input homes location");
        homesLandingPage.searchStayLocation(location);

        step("Search homes");
        hotelLandingPage.clickSubmit();
        hotelLandingPage.fillNightCheckInForm();

        step("Select homes");
        searchResultPage.selectFirstHotelCard();

        step("Scroll down to room list");
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();

        step("Open homes booking page");
        roomListPage.getTheFirstHotelRoomPrice();
        roomListPage.clickBookRoom();

        step("Submit booking");
        bookingFormPage.continueToPaymentPage();

        sleep(40);

        BaseAssertions homesPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("bookProduct event is found", homesPageAssertion.verifyEvent(Configs.MESSAGES, event), greaterThan(0));
        Assertion.assertThat("screenName is correct", homesPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "screenName", screenName), is(true));
        Assertion.assertThat("vertical is correct", homesPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "vertical", vertical), is(true));
        Assertion.assertThat("eventAction is correct", homesPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "eventAction", eventAction), is(true));
        Assertion.assertThat("eventDescription is correct", homesPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "eventDescription", eventDescription), is(true));
        Assertion.assertThat("eventLabel is correct", homesPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "eventLabel", eventLabel), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "HomesMainPage")
    @Tribe(name = "Homes")
    @Vertical(name = "Core")
    @Test
    public void testHomesMainPageVisitDeeplinkUtmGuest() {
        Map<String, String> testProperties = getParam("testHomesMainPageVisitDeeplinkUtmGuest");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String deeplink = "tiket://m.tiket.com/homes/?";

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

        step("Open homes vertical via deeplink");
        String utmProperties = analyticsHomePage.getUtmProperties(utmCampaign, utmContent, utmId, utmMedium, utmSource, utmTerm);
        analyticsHomePage.openDeeplink(deeplink + utmProperties);

        sleep(40);

        BaseAssertions homesPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("utmAttributedAt is not null", homesPageAssertion.verifyUserProperty(Configs.MESSAGES, "utmAttributedAt"), is(true));
        Assertion.assertThat("utmCampaign is correct", homesPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmCampaign", utmCampaign), is(true));
        Assertion.assertThat("utmContent is correct", homesPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmContent", utmContent), is(true));
        Assertion.assertThat("utmExternal is correct", homesPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmExternal", utmExternal), is(true));
        Assertion.assertThat("utmId is correct", homesPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmId", utmId), is(true));
        Assertion.assertThat("utmMedium is correct", homesPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmMedium", utmMedium), is(true));
        Assertion.assertThat("utmSource is correct", homesPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmSource", utmSource), is(true));
        Assertion.assertThat("utmTerm is correct", homesPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmTerm", utmTerm), is(true));
        Assertion.assertThat("userId is null", homesPageAssertion.verifyUserProperty(Configs.MESSAGES, "userId"), is(false));

        Assertion.assertThat("pageVisit event is found", homesPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, screenName), is(1));
        Assertion.assertThat("eventAction is correct", homesPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), is(true));
        Assertion.assertThat("screenName is correct", homesPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), is(true));
        Assertion.assertThat("utmPage is correct", homesPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "utmPage", utmPage), is(true));
        Assertion.assertThat("isFirstPage is correct", homesPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "isFirstPage", isFirstPage), is(true));
        Assertion.assertThat("vertical is correct", homesPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "vertical", vertical), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "HomesMainPage")
    @Tribe(name = "Homes")
    @Vertical(name = "Core")
    @Test
    public void testHomesMainPageVisitDeeplinkUtmLogin() {
        Map<String, String> testProperties = getParam("testHomesMainPageVisitDeeplinkUtmLogin");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String loginType = testProperties.get("loginType");
        String email = testProperties.get("email");
        String password = testProperties.get("password");
        String deeplink = "tiket://m.tiket.com/homes/?";

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

        step("Open homes vertical via deeplink");
        String utmProperties = analyticsHomePage.getUtmProperties(utmCampaign, utmContent, utmId, utmMedium, utmSource, utmTerm);
        analyticsHomePage.openDeeplink(deeplink + utmProperties);

        sleep(40);

        BaseAssertions homesPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("utmAttributedAt is not null", homesPageAssertion.verifyUserProperty(Configs.MESSAGES, "utmAttributedAt"), is(true));
        Assertion.assertThat("utmCampaign is correct", homesPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmCampaign", utmCampaign), is(true));
        Assertion.assertThat("utmContent is correct", homesPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmContent", utmContent), is(true));
        Assertion.assertThat("utmExternal is correct", homesPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmExternal", utmExternal), is(true));
        Assertion.assertThat("utmId is correct", homesPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmId", utmId), is(true));
        Assertion.assertThat("utmMedium is correct", homesPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmMedium", utmMedium), is(true));
        Assertion.assertThat("utmSource is correct", homesPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmSource", utmSource), is(true));
        Assertion.assertThat("utmTerm is correct", homesPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmTerm", utmTerm), is(true));
        Assertion.assertThat("userId is not null", homesPageAssertion.verifyUserProperty(Configs.MESSAGES, "userId"), is(true));

        Assertion.assertThat("pageVisit event is found", homesPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, screenName), is(1));
        Assertion.assertThat("eventAction is correct", homesPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), is(true));
        Assertion.assertThat("screenName is correct", homesPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), is(true));
        Assertion.assertThat("utmPage is correct", homesPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "utmPage", utmPage), is(true));
        Assertion.assertThat("isFirstPage is correct", homesPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "isFirstPage", isFirstPage), is(true));
        Assertion.assertThat("vertical is correct", homesPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "vertical", vertical), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "HomesSRP")
    @Tribe(name = "Homes")
    @Vertical(name = "Core")
    @Test
    public void testHomesSRPPageVisitDeeplinkUtmGuest() {
        Map<String, String> testProperties = getParam("testHomesSRPPageVisitDeeplinkUtmGuest");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        int daysToCheckOutDate = Integer.parseInt(testProperties.get("daysToCheckOutDate"));
        String checkInDate = CoreFunctions.getCurrentDateYYYY_MM_DD();
        String checkOutDate = CoreFunctions.addToDateYYYY_MM_DD(checkInDate, daysToCheckOutDate);
        String deeplink = "tiket://m.tiket.com/homes/search?room=1&adult=1&id=bali-108001534490276212&type=REGION&q=Bali&checkin=" + checkInDate + "&checkout=" + checkOutDate + "&";

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

        step("Open homes SRP via deeplink");
        String utmProperties = analyticsHomePage.getUtmProperties(utmCampaign, utmContent, utmId, utmMedium, utmSource, utmTerm);
        analyticsHomePage.openDeeplink(deeplink + utmProperties);

        sleep(40);

        BaseAssertions homesPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("utmAttributedAt is not null", homesPageAssertion.verifyUserProperty(Configs.MESSAGES, "utmAttributedAt"), is(true));
        Assertion.assertThat("utmCampaign is correct", homesPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmCampaign", utmCampaign), is(true));
        Assertion.assertThat("utmContent is correct", homesPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmContent", utmContent), is(true));
        Assertion.assertThat("utmExternal is correct", homesPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmExternal", utmExternal), is(true));
        Assertion.assertThat("utmId is correct", homesPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmId", utmId), is(true));
        Assertion.assertThat("utmMedium is correct", homesPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmMedium", utmMedium), is(true));
        Assertion.assertThat("utmSource is correct", homesPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmSource", utmSource), is(true));
        Assertion.assertThat("utmTerm is correct", homesPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmTerm", utmTerm), is(true));
        Assertion.assertThat("userId is null", homesPageAssertion.verifyUserProperty(Configs.MESSAGES, "userId"), is(false));

        Assertion.assertThat("pageVisit event is found", homesPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, screenName), is(1));
        Assertion.assertThat("eventAction is correct", homesPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), is(true));
        Assertion.assertThat("screenName is correct", homesPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), is(true));
        Assertion.assertThat("utmPage is correct", homesPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "utmPage", utmPage), is(true));
        Assertion.assertThat("isFirstPage is correct", homesPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "isFirstPage", isFirstPage), is(true));
        Assertion.assertThat("vertical is correct", homesPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "vertical", vertical), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "HomesSRP")
    @Tribe(name = "Homes")
    @Vertical(name = "Core")
    @Test
    public void testHomesSRPPageVisitDeeplinkUtmLogin() {
        Map<String, String> testProperties = getParam("testHomesSRPPageVisitDeeplinkUtmLogin");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String loginType = testProperties.get("loginType");
        String email = testProperties.get("email");
        String password = testProperties.get("password");

        int daysToCheckOutDate = Integer.parseInt(testProperties.get("daysToCheckOutDate"));
        String checkInDate = CoreFunctions.getCurrentDateYYYY_MM_DD();
        String checkOutDate = CoreFunctions.addToDateYYYY_MM_DD(checkInDate, daysToCheckOutDate);
        String deeplink = "tiket://m.tiket.com/homes/search?room=1&adult=1&id=bali-108001534490276212&type=REGION&q=Bali&checkin=" + checkInDate + "&checkout=" + checkOutDate + "&";

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

        step("Open homes SRP via deeplink");
        String utmProperties = analyticsHomePage.getUtmProperties(utmCampaign, utmContent, utmId, utmMedium, utmSource, utmTerm);
        analyticsHomePage.openDeeplink(deeplink + utmProperties);

        sleep(40);

        BaseAssertions homesPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("utmAttributedAt is not null", homesPageAssertion.verifyUserProperty(Configs.MESSAGES, "utmAttributedAt"), is(true));
        Assertion.assertThat("utmCampaign is correct", homesPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmCampaign", utmCampaign), is(true));
        Assertion.assertThat("utmContent is correct", homesPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmContent", utmContent), is(true));
        Assertion.assertThat("utmExternal is correct", homesPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmExternal", utmExternal), is(true));
        Assertion.assertThat("utmId is correct", homesPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmId", utmId), is(true));
        Assertion.assertThat("utmMedium is correct", homesPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmMedium", utmMedium), is(true));
        Assertion.assertThat("utmSource is correct", homesPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmSource", utmSource), is(true));
        Assertion.assertThat("utmTerm is correct", homesPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmTerm", utmTerm), is(true));
        Assertion.assertThat("userId is not null", homesPageAssertion.verifyUserProperty(Configs.MESSAGES, "userId"), is(true));

        Assertion.assertThat("pageVisit event is found", homesPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, screenName), is(1));
        Assertion.assertThat("eventAction is correct", homesPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), is(true));
        Assertion.assertThat("screenName is correct", homesPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), is(true));
        Assertion.assertThat("utmPage is correct", homesPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "utmPage", utmPage), is(true));
        Assertion.assertThat("isFirstPage is correct", homesPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "isFirstPage", isFirstPage), is(true));
        Assertion.assertThat("vertical is correct", homesPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "vertical", vertical), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "HomesPDP")
    @Tribe(name = "Homes")
    @Vertical(name = "Core")
    @Test
    public void testHomesPDPPageVisitDeeplinkUtmGuest() {
        Map<String, String> testProperties = getParam("testHomesPDPPageVisitDeeplinkUtmGuest");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        int daysToCheckOutDate = Integer.parseInt(testProperties.get("daysToCheckOutDate"));
        String checkInDate = CoreFunctions.getCurrentDateYYYY_MM_DD();
        String checkOutDate = CoreFunctions.addToDateYYYY_MM_DD(checkInDate, daysToCheckOutDate);
        String deeplink = "tiket://m.tiket.com/homes/indonesia/villa-kayu-raja-108001534490279170?room=1&adult=1&checkin=" + checkInDate + "&checkout=" + checkOutDate + "&";

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

        step("Open homes PDP via deeplink");
        String utmProperties = analyticsHomePage.getUtmProperties(utmCampaign, utmContent, utmId, utmMedium, utmSource, utmTerm);
        analyticsHomePage.openDeeplink(deeplink + utmProperties);

        sleep(40);

        BaseAssertions homesPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("utmAttributedAt is not null", homesPageAssertion.verifyUserProperty(Configs.MESSAGES, "utmAttributedAt"), is(true));
        Assertion.assertThat("utmCampaign is correct", homesPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmCampaign", utmCampaign), is(true));
        Assertion.assertThat("utmContent is correct", homesPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmContent", utmContent), is(true));
        Assertion.assertThat("utmExternal is correct", homesPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmExternal", utmExternal), is(true));
        Assertion.assertThat("utmId is correct", homesPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmId", utmId), is(true));
        Assertion.assertThat("utmMedium is correct", homesPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmMedium", utmMedium), is(true));
        Assertion.assertThat("utmSource is correct", homesPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmSource", utmSource), is(true));
        Assertion.assertThat("utmTerm is correct", homesPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmTerm", utmTerm), is(true));
        Assertion.assertThat("userId is null", homesPageAssertion.verifyUserProperty(Configs.MESSAGES, "userId"), is(false));

        Assertion.assertThat("pageVisit event is found", homesPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, screenName), is(1));
        Assertion.assertThat("eventAction is correct", homesPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), is(true));
        Assertion.assertThat("screenName is correct", homesPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), is(true));
        Assertion.assertThat("utmPage is correct", homesPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "utmPage", utmPage), is(true));
        Assertion.assertThat("isFirstPage is correct", homesPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "isFirstPage", isFirstPage), is(true));
        Assertion.assertThat("vertical is correct", homesPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "vertical", vertical), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "HomesPDP")
    @Tribe(name = "Homes")
    @Vertical(name = "Core")
    @Test
    public void testHomesPDPPageVisitDeeplinkUtmLogin() {
        Map<String, String> testProperties = getParam("testHomesPDPPageVisitDeeplinkUtmLogin");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String loginType = testProperties.get("loginType");
        String email = testProperties.get("email");
        String password = testProperties.get("password");

        int daysToCheckOutDate = Integer.parseInt(testProperties.get("daysToCheckOutDate"));
        String checkInDate = CoreFunctions.getCurrentDateYYYY_MM_DD();
        String checkOutDate = CoreFunctions.addToDateYYYY_MM_DD(checkInDate, daysToCheckOutDate);
        String deeplink = "tiket://m.tiket.com/homes/indonesia/villa-kayu-raja-108001534490279170?room=1&adult=1&checkin=" + checkInDate + "&checkout=" + checkOutDate + "&";

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

        step("Open homes PDP via deeplink");
        String utmProperties = analyticsHomePage.getUtmProperties(utmCampaign, utmContent, utmId, utmMedium, utmSource, utmTerm);
        analyticsHomePage.openDeeplink(deeplink + utmProperties);

        sleep(40);

        BaseAssertions homesPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("utmAttributedAt is not null", homesPageAssertion.verifyUserProperty(Configs.MESSAGES, "utmAttributedAt"), is(true));
        Assertion.assertThat("utmCampaign is correct", homesPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmCampaign", utmCampaign), is(true));
        Assertion.assertThat("utmContent is correct", homesPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmContent", utmContent), is(true));
        Assertion.assertThat("utmExternal is correct", homesPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmExternal", utmExternal), is(true));
        Assertion.assertThat("utmId is correct", homesPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmId", utmId), is(true));
        Assertion.assertThat("utmMedium is correct", homesPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmMedium", utmMedium), is(true));
        Assertion.assertThat("utmSource is correct", homesPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmSource", utmSource), is(true));
        Assertion.assertThat("utmTerm is correct", homesPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmTerm", utmTerm), is(true));
        Assertion.assertThat("userId is not null", homesPageAssertion.verifyUserProperty(Configs.MESSAGES, "userId"), is(true));

        Assertion.assertThat("pageVisit event is found", homesPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, screenName), is(1));
        Assertion.assertThat("eventAction is correct", homesPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), is(true));
        Assertion.assertThat("screenName is correct", homesPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), is(true));
        Assertion.assertThat("utmPage is correct", homesPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "utmPage", utmPage), is(true));
        Assertion.assertThat("isFirstPage is correct", homesPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "isFirstPage", isFirstPage), is(true));
        Assertion.assertThat("vertical is correct", homesPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "vertical", vertical), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "HomesMainPage")
    @Tribe(name = "Homes")
    @Vertical(name = "Core")
    @Test
    public void testHomesMainPageVisitDeeplinkNonUtmGuest() {
        Map<String, String> testProperties = getParam("testHomesMainPageVisitDeeplinkNonUtmGuest");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String deeplink = "tiket://m.tiket.com/homes";

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

        step("Open homes vertical via deeplink");
        analyticsHomePage.openDeeplink(deeplink);

        sleep(40);

        BaseAssertions homesPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("utmAttributedAt is not null", homesPageAssertion.verifyUserProperty(Configs.MESSAGES, "utmAttributedAt"), is(true));
        Assertion.assertThat("utmCampaign is correct", homesPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmCampaign", utmCampaign), is(true));
        Assertion.assertThat("utmContent is correct", homesPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmContent", utmContent), is(true));
        Assertion.assertThat("utmExternal is correct", homesPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmExternal", utmExternal), is(true));
        Assertion.assertThat("utmId is correct", homesPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmId", utmId), is(true));
        Assertion.assertThat("utmMedium is correct", homesPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmMedium", utmMedium), is(true));
        Assertion.assertThat("utmSource is correct", homesPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmSource", utmSource), is(true));
        Assertion.assertThat("utmTerm is correct", homesPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmTerm", utmTerm), is(true));
        Assertion.assertThat("userId is null", homesPageAssertion.verifyUserProperty(Configs.MESSAGES, "userId"), is(false));

        Assertion.assertThat("pageVisit event is found", homesPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, screenName), is(1));
        Assertion.assertThat("eventAction is correct", homesPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), is(true));
        Assertion.assertThat("screenName is correct", homesPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), is(true));
        Assertion.assertThat("utmPage is correct", homesPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "utmPage", utmPage), is(true));
        Assertion.assertThat("isFirstPage is correct", homesPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "isFirstPage", isFirstPage), is(true));
        Assertion.assertThat("vertical is correct", homesPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "vertical", vertical), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "HomesMainPage")
    @Tribe(name = "Homes")
    @Vertical(name = "Core")
    @Test
    public void testHomesMainPageVisitDeeplinkNonUtmLogin() {
        Map<String, String> testProperties = getParam("testHomesMainPageVisitDeeplinkNonUtmLogin");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String loginType = testProperties.get("loginType");
        String email = testProperties.get("email");
        String password = testProperties.get("password");
        String deeplink = "tiket://m.tiket.com/homes";

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

        step("Open homes vertical via deeplink");
        analyticsHomePage.openDeeplink(deeplink);

        sleep(40);

        BaseAssertions homesPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("utmAttributedAt is not null", homesPageAssertion.verifyUserProperty(Configs.MESSAGES, "utmAttributedAt"), is(true));
        Assertion.assertThat("utmCampaign is correct", homesPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmCampaign", utmCampaign), is(true));
        Assertion.assertThat("utmContent is correct", homesPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmContent", utmContent), is(true));
        Assertion.assertThat("utmExternal is correct", homesPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmExternal", utmExternal), is(true));
        Assertion.assertThat("utmId is correct", homesPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmId", utmId), is(true));
        Assertion.assertThat("utmMedium is correct", homesPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmMedium", utmMedium), is(true));
        Assertion.assertThat("utmSource is correct", homesPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmSource", utmSource), is(true));
        Assertion.assertThat("utmTerm is correct", homesPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmTerm", utmTerm), is(true));
        Assertion.assertThat("userId is not null", homesPageAssertion.verifyUserProperty(Configs.MESSAGES, "userId"), is(true));

        Assertion.assertThat("pageVisit event is found", homesPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, screenName), is(1));
        Assertion.assertThat("eventAction is correct", homesPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), is(true));
        Assertion.assertThat("screenName is correct", homesPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), is(true));
        Assertion.assertThat("utmPage is correct", homesPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "utmPage", utmPage), is(true));
        Assertion.assertThat("isFirstPage is correct", homesPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "isFirstPage", isFirstPage), is(true));
        Assertion.assertThat("vertical is correct", homesPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "vertical", vertical), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "HomesSRP")
    @Tribe(name = "Homes")
    @Vertical(name = "Core")
    @Test
    public void testHomesSRPPageVisitDeeplinkNonUtmGuest() {
        Map<String, String> testProperties = getParam("testHomesSRPPageVisitDeeplinkNonUtmGuest");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        int daysToCheckOutDate = Integer.parseInt(testProperties.get("daysToCheckOutDate"));
        String checkInDate = CoreFunctions.getCurrentDateYYYY_MM_DD();
        String checkOutDate = CoreFunctions.addToDateYYYY_MM_DD(checkInDate, daysToCheckOutDate);
        String deeplink = "tiket://m.tiket.com/homes/search?room=1&adult=1&id=bali-108001534490276212&type=REGION&q=Bali&checkin=" + checkInDate + "&checkout=" + checkOutDate;

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

        step("Open homes SRP via deeplink");
        analyticsHomePage.openDeeplink(deeplink);

        sleep(40);

        BaseAssertions homesPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("utmAttributedAt is not null", homesPageAssertion.verifyUserProperty(Configs.MESSAGES, "utmAttributedAt"), is(true));
        Assertion.assertThat("utmCampaign is correct", homesPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmCampaign", utmCampaign), is(true));
        Assertion.assertThat("utmContent is correct", homesPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmContent", utmContent), is(true));
        Assertion.assertThat("utmExternal is correct", homesPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmExternal", utmExternal), is(true));
        Assertion.assertThat("utmId is correct", homesPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmId", utmId), is(true));
        Assertion.assertThat("utmMedium is correct", homesPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmMedium", utmMedium), is(true));
        Assertion.assertThat("utmSource is correct", homesPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmSource", utmSource), is(true));
        Assertion.assertThat("utmTerm is correct", homesPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmTerm", utmTerm), is(true));
        Assertion.assertThat("userId is null", homesPageAssertion.verifyUserProperty(Configs.MESSAGES, "userId"), is(false));

        Assertion.assertThat("pageVisit event is found", homesPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, screenName), is(1));
        Assertion.assertThat("eventAction is correct", homesPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), is(true));
        Assertion.assertThat("screenName is correct", homesPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), is(true));
        Assertion.assertThat("utmPage is correct", homesPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "utmPage", utmPage), is(true));
        Assertion.assertThat("isFirstPage is correct", homesPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "isFirstPage", isFirstPage), is(true));
        Assertion.assertThat("vertical is correct", homesPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "vertical", vertical), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "HomesSRP")
    @Tribe(name = "Homes")
    @Vertical(name = "Core")
    @Test
    public void testHomesSRPPageVisitDeeplinkNonUtmLogin() {
        Map<String, String> testProperties = getParam("testHomesSRPPageVisitDeeplinkNonUtmLogin");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String loginType = testProperties.get("loginType");
        String email = testProperties.get("email");
        String password = testProperties.get("password");

        int daysToCheckOutDate = Integer.parseInt(testProperties.get("daysToCheckOutDate"));
        String checkInDate = CoreFunctions.getCurrentDateYYYY_MM_DD();
        String checkOutDate = CoreFunctions.addToDateYYYY_MM_DD(checkInDate, daysToCheckOutDate);
        String deeplink = "tiket://m.tiket.com/homes/search?room=1&adult=1&id=bali-108001534490276212&type=REGION&q=Bali&checkin=" + checkInDate + "&checkout=" + checkOutDate;

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

        step("Open homes SRP via deeplink");
        analyticsHomePage.openDeeplink(deeplink);

        sleep(40);

        BaseAssertions homesPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("utmAttributedAt is not null", homesPageAssertion.verifyUserProperty(Configs.MESSAGES, "utmAttributedAt"), is(true));
        Assertion.assertThat("utmCampaign is correct", homesPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmCampaign", utmCampaign), is(true));
        Assertion.assertThat("utmContent is correct", homesPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmContent", utmContent), is(true));
        Assertion.assertThat("utmExternal is correct", homesPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmExternal", utmExternal), is(true));
        Assertion.assertThat("utmId is correct", homesPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmId", utmId), is(true));
        Assertion.assertThat("utmMedium is correct", homesPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmMedium", utmMedium), is(true));
        Assertion.assertThat("utmSource is correct", homesPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmSource", utmSource), is(true));
        Assertion.assertThat("utmTerm is correct", homesPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmTerm", utmTerm), is(true));
        Assertion.assertThat("userId is not null", homesPageAssertion.verifyUserProperty(Configs.MESSAGES, "userId"), is(true));

        Assertion.assertThat("pageVisit event is found", homesPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, screenName), is(1));
        Assertion.assertThat("eventAction is correct", homesPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), is(true));
        Assertion.assertThat("screenName is correct", homesPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), is(true));
        Assertion.assertThat("utmPage is correct", homesPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "utmPage", utmPage), is(true));
        Assertion.assertThat("isFirstPage is correct", homesPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "isFirstPage", isFirstPage), is(true));
        Assertion.assertThat("vertical is correct", homesPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "vertical", vertical), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "HomesPDP")
    @Tribe(name = "Homes")
    @Vertical(name = "Core")
    @Test
    public void testHomesPDPPageVisitDeeplinkNonUtmGuest() {
        Map<String, String> testProperties = getParam("testHomesPDPPageVisitDeeplinkNonUtmGuest");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        int daysToCheckOutDate = Integer.parseInt(testProperties.get("daysToCheckOutDate"));
        String checkInDate = CoreFunctions.getCurrentDateYYYY_MM_DD();
        String checkOutDate = CoreFunctions.addToDateYYYY_MM_DD(checkInDate, daysToCheckOutDate);
        String deeplink = "tiket://m.tiket.com/homes/indonesia/villa-kayu-raja-108001534490279170?room=1&adult=1&checkin=" + checkInDate + "&checkout=" + checkOutDate;

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

        step("Open homes PDP via deeplink");
        analyticsHomePage.openDeeplink(deeplink);

        sleep(40);

        BaseAssertions homesPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("utmAttributedAt is not null", homesPageAssertion.verifyUserProperty(Configs.MESSAGES, "utmAttributedAt"), is(true));
        Assertion.assertThat("utmCampaign is correct", homesPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmCampaign", utmCampaign), is(true));
        Assertion.assertThat("utmContent is correct", homesPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmContent", utmContent), is(true));
        Assertion.assertThat("utmExternal is correct", homesPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmExternal", utmExternal), is(true));
        Assertion.assertThat("utmId is correct", homesPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmId", utmId), is(true));
        Assertion.assertThat("utmMedium is correct", homesPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmMedium", utmMedium), is(true));
        Assertion.assertThat("utmSource is correct", homesPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmSource", utmSource), is(true));
        Assertion.assertThat("utmTerm is correct", homesPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmTerm", utmTerm), is(true));
        Assertion.assertThat("userId is null", homesPageAssertion.verifyUserProperty(Configs.MESSAGES, "userId"), is(false));

        Assertion.assertThat("pageVisit event is found", homesPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, screenName), is(1));
        Assertion.assertThat("eventAction is correct", homesPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), is(true));
        Assertion.assertThat("screenName is correct", homesPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), is(true));
        Assertion.assertThat("utmPage is correct", homesPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "utmPage", utmPage), is(true));
        Assertion.assertThat("isFirstPage is correct", homesPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "isFirstPage", isFirstPage), is(true));
        Assertion.assertThat("vertical is correct", homesPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "vertical", vertical), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "HomesPDP")
    @Tribe(name = "Homes")
    @Vertical(name = "Core")
    @Test
    public void testHomesPDPPageVisitDeeplinkNonUtmLogin() {
        Map<String, String> testProperties = getParam("testHomesPDPPageVisitDeeplinkNonUtmLogin");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String loginType = testProperties.get("loginType");
        String email = testProperties.get("email");
        String password = testProperties.get("password");

        int daysToCheckOutDate = Integer.parseInt(testProperties.get("daysToCheckOutDate"));
        String checkInDate = CoreFunctions.getCurrentDateYYYY_MM_DD();
        String checkOutDate = CoreFunctions.addToDateYYYY_MM_DD(checkInDate, daysToCheckOutDate);
        String deeplink = "tiket://m.tiket.com/homes/indonesia/villa-kayu-raja-108001534490279170?room=1&adult=1&checkin=" + checkInDate + "&checkout=" + checkOutDate;

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

        step("Open homes PDP via deeplink");
        analyticsHomePage.openDeeplink(deeplink);

        sleep(40);

        BaseAssertions homesPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("utmAttributedAt is not null", homesPageAssertion.verifyUserProperty(Configs.MESSAGES, "utmAttributedAt"), is(true));
        Assertion.assertThat("utmCampaign is correct", homesPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmCampaign", utmCampaign), is(true));
        Assertion.assertThat("utmContent is correct", homesPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmContent", utmContent), is(true));
        Assertion.assertThat("utmExternal is correct", homesPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmExternal", utmExternal), is(true));
        Assertion.assertThat("utmId is correct", homesPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmId", utmId), is(true));
        Assertion.assertThat("utmMedium is correct", homesPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmMedium", utmMedium), is(true));
        Assertion.assertThat("utmSource is correct", homesPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmSource", utmSource), is(true));
        Assertion.assertThat("utmTerm is correct", homesPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmTerm", utmTerm), is(true));
        Assertion.assertThat("userId is not null", homesPageAssertion.verifyUserProperty(Configs.MESSAGES, "userId"), is(true));

        Assertion.assertThat("pageVisit event is found", homesPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, screenName), is(1));
        Assertion.assertThat("eventAction is correct", homesPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), is(true));
        Assertion.assertThat("screenName is correct", homesPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), is(true));
        Assertion.assertThat("utmPage is correct", homesPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "utmPage", utmPage), is(true));
        Assertion.assertThat("isFirstPage is correct", homesPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "isFirstPage", isFirstPage), is(true));
        Assertion.assertThat("vertical is correct", homesPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "vertical", vertical), is(true));
    }
}