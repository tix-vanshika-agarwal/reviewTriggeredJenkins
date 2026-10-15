package com.tiket.analytics.app.test.ttd;

import com.tiket.analytics.BaseAnalyticsTest;
import com.tiket.analytics.assertions.BaseAssertions;
import com.tiket.analytics.app.home.AnalyticsHomePage;
import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.model.Session;
import com.tiket.page.common.webview.WebViewPage;
import com.tiket.page.home.HomePage;
import com.tiket.page.login.LoginPage;
import com.tiket.page.ttd.bookingformpage.TTDBookingFormPage;
import com.tiket.page.ttd.landingpage.TTDLandingPage;
import com.tiket.page.ttd.productdetailpage.TTDProductDetailPage;
import com.tiket.page.ttd.searchpage.TTDSearchPage;
import com.tiket.page.ttd.searchresultpage.TTDSearchResultPage;
import com.tiket.page.ttd.selectedpackagedetailpage.TTDSelectedPackageDetailPage;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.testbase.Configs;
import com.tiket.web.page.common.home.InfoPageWeb;
import com.tiket.web.page.ttd.TtdBookingFormPage.TtdBookingFormPageWeb;
import com.tiket.web.page.ttd.TtdPackageDetailPage.TtdPackageDetailPageWeb;
import com.tiket.web.page.ttd.TtdProductDetailPage.TtdProductDetailPageWeb;
import com.tiket.web.page.ttd.TtdReviewPage.TtdReviewPageWeb;
import com.tiket.web.page.ttd.TtdSelectedPackageDetailPage.TtdSelectedPackageDetailPageWeb;
import org.hamcrest.core.Is;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import java.util.Map;

import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.Matchers.is;

public class TTDPageTest extends BaseAnalyticsTest {

    @Author(names = {"Naufal"})
    @Module(name = "TTDMainPage")
    @Tribe(name = "TTD")
    @Vertical(name = "Core")
    @Test
    public void testTTDMainPageVisitGuest(){
        Map<String, String> testProperties = getParam("testTTDMainPageVisitGuest");
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

        step("Open TTD vertical");
        homePage.openVerticalPage("To Do");

        sleep(40);

        BaseAssertions ttdPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("pageVisit event is found", ttdPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, screenName), is(1));
        Assertion.assertThat("eventAction is correct", ttdPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), Is.is(true));
        Assertion.assertThat("screenName is correct", ttdPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), Is.is(true));
        Assertion.assertThat("utmPage is correct", ttdPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "utmPage", utmPage), Is.is(true));
        Assertion.assertThat("isFirstPage is correct", ttdPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "isFirstPage", isFirstPage), Is.is(true));
        Assertion.assertThat("vertical is correct", ttdPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "vertical", vertical), Is.is(true));
    }

    @Author(names = {"Naufal"})
    @Module(name = "TTDMainPage")
    @Tribe(name = "TTD")
    @Vertical(name = "Core")
    @Test
    public void testTTDMainPageVisitLogin(){
        Map<String, String> testProperties = getParam("testTTDMainPageVisitLogin");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String event = testProperties.get("event");
        String eventAction = testProperties.get("eventAction");
        String screenName = testProperties.get("screenName");
        String utmPage = testProperties.get("utmPage");
        String isFirstPage = testProperties.get("isFirstPage");
        String vertical = testProperties.get("vertical");

        String loginType = testProperties.get("loginType");
        String email = testProperties.get("email");
        String password = testProperties.get("password");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, email, password);

        step("Open TTD vertical");
        homePage.openVerticalPage("To Do");

        sleep(40);

        BaseAssertions ttdPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("pageVisit event is found", ttdPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, screenName), is(1));
        Assertion.assertThat("eventAction is correct", ttdPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), Is.is(true));
        Assertion.assertThat("screenName is correct", ttdPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), Is.is(true));
        Assertion.assertThat("utmPage is correct", ttdPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "utmPage", utmPage), Is.is(true));
        Assertion.assertThat("isFirstPage is correct", ttdPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "isFirstPage", isFirstPage), Is.is(true));
        Assertion.assertThat("vertical is correct", ttdPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "vertical", vertical), Is.is(true));
    }

    @Author(names = {"Naufal"})
    @Module(name = "TTDMainPage")
    @Tribe(name = "TTD")
    @Vertical(name = "Core")
    @Test
    public void testTTDEnterVerticalGuest(){
        Map<String, String> testProperties = getParam("testTTDEnterVerticalGuest");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String event = testProperties.get("event");
        String eventAction = testProperties.get("eventAction");
        String eventLabel = testProperties.get("eventLabel");
        String screenName = testProperties.get("screenName");
        String vertical = testProperties.get("vertical");
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Open app as guest");
        loginPage.loginAsGuest();

        step("Open TTD vertical");
        homePage.openVerticalPage("To Do");

        sleep(40);

        BaseAssertions ttdPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("enterVertical event is found", ttdPageAssertion.verifyEvent(Configs.MESSAGES, event), is(1));
        Assertion.assertThat("screenName is correct", ttdPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event,"screenName", screenName), Is.is(true));
        Assertion.assertThat("eventAction is correct", ttdPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "eventAction", eventAction), Is.is(true));
        Assertion.assertThat("eventLabel is correct", ttdPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event,"eventLabel", eventLabel), Is.is(true));
        Assertion.assertThat("vertical is correct", ttdPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "vertical", vertical), Is.is(true));
    }

    @Author(names = {"Naufal"})
    @Module(name = "TTDMainPage")
    @Tribe(name = "TTD")
    @Vertical(name = "Core")
    @Test
    public void testTTDEnterVerticalLogin(){
        Map<String, String> testProperties = getParam("testTTDEnterVerticalLogin");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String event = testProperties.get("event");
        String eventAction = testProperties.get("eventAction");
        String eventLabel = testProperties.get("eventLabel");
        String screenName = testProperties.get("screenName");
        String vertical = testProperties.get("vertical");

        String loginType = testProperties.get("loginType");
        String email = testProperties.get("email");
        String password = testProperties.get("password");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, email, password);

        step("Open TTD vertical");
        homePage.openVerticalPage("To Do");

        sleep(40);

        BaseAssertions ttdPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("enterVertical event is found", ttdPageAssertion.verifyEvent(Configs.MESSAGES, event), is(1));
        Assertion.assertThat("screenName is correct", ttdPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event,"screenName", screenName), Is.is(true));
        Assertion.assertThat("eventAction is correct", ttdPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "eventAction", eventAction), Is.is(true));
        Assertion.assertThat("eventLabel is correct", ttdPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event,"eventLabel", eventLabel), Is.is(true));
        Assertion.assertThat("vertical is correct", ttdPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "vertical", vertical), Is.is(true));
    }
    @Author(names = {"Naufal"})
    @Module(name = "TTDMainPage")
    @Tribe(name = "TTD")
    @Vertical(name = "Core")
    @Test
    public void testAttractionsEnterVerticalGuest(){
        Map<String, String> testProperties = getParam("testAttractionsEnterVerticalGuest");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String event = testProperties.get("event");
        String eventAction = testProperties.get("eventAction");
        String eventLabel = testProperties.get("eventLabel");
        String screenName = testProperties.get("screenName");
        String vertical = testProperties.get("vertical");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Open app as guest");
        loginPage.loginAsGuest();

        step("Open Attractions vertical");
        homePage.openVerticalPage("Attractions");

        sleep(40);

        BaseAssertions ttdPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("enterVertical event is found", ttdPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, vertical), is(1));
        Assertion.assertThat("screenName is correct", ttdPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event,"screenName", screenName), Is.is(true));
        Assertion.assertThat("eventAction is correct", ttdPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "eventAction", eventAction), Is.is(true));
        Assertion.assertThat("eventLabel is correct", ttdPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event,"eventLabel", eventLabel), Is.is(true));
        Assertion.assertThat("vertical is correct", ttdPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "vertical", vertical), Is.is(true));
    }

    @Author(names = {"Naufal"})
    @Module(name = "TTDMainPage")
    @Tribe(name = "TTD")
    @Vertical(name = "Core")
    @Test
    public void testAttractionsEnterVerticalLogin(){
        Map<String, String> testProperties = getParam("testAttractionsEnterVerticalLogin");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String event = testProperties.get("event");
        String eventAction = testProperties.get("eventAction");
        String eventLabel = testProperties.get("eventLabel");
        String screenName = testProperties.get("screenName");
        String vertical = testProperties.get("vertical");

        String loginType = testProperties.get("loginType");
        String email = testProperties.get("email");
        String password = testProperties.get("password");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, email, password);

        step("Open Attractions vertical");
        homePage.openVerticalPage("Attractions");

        sleep(40);

        BaseAssertions ttdPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("enterVertical event is found", ttdPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, vertical), is(1));
        Assertion.assertThat("screenName is correct", ttdPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event,"screenName", screenName), Is.is(true));
        Assertion.assertThat("eventAction is correct", ttdPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "eventAction", eventAction), Is.is(true));
        Assertion.assertThat("eventLabel is correct", ttdPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event,"eventLabel", eventLabel), Is.is(true));
        Assertion.assertThat("vertical is correct", ttdPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "vertical", vertical), Is.is(true));
    }

    @Author(names = {"Naufal"})
    @Module(name = "TTDSRP")
    @Tribe(name = "TTD")
    @Vertical(name = "Core")
    @Test
    public void testTTDSRPPageVisitGuest(){
        Map<String, String> testProperties = getParam("testTTDSRPPageVisitGuest");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);

        String event = testProperties.get("event");
        String eventAction = testProperties.get("eventAction");
        String screenName = testProperties.get("screenName");
        String utmPage = testProperties.get("utmPage");
        String isFirstPage = testProperties.get("isFirstPage");
        String category = testProperties.get("category");
        String vertical = testProperties.get("vertical");

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Open app as guest");
        loginPage.loginAsGuest();

        step("Open TTD vertical");
        homePage.openVerticalPage("To Do");

        step("Open SRP Category '"+category+"'");
        ttdLandingPage.openProductByCategory(category);

        sleep(40);

        BaseAssertions ttdPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("SRP event is found", ttdPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, screenName), Is.is(1));
        Assertion.assertThat("eventAction is correct", ttdPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), Is.is(true));
        Assertion.assertThat("vertical is correct", ttdPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "vertical", vertical), Is.is(true));
        Assertion.assertThat("screenName is correct", ttdPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), Is.is(true));
        Assertion.assertThat("utmPage is correct", ttdPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "utmPage", utmPage), Is.is(true));
        Assertion.assertThat("isFirstPage is correct", ttdPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "isFirstPage", isFirstPage), Is.is(true));
    }

    @Author(names = {"Naufal"})
    @Module(name = "TTDSRP")
    @Tribe(name = "TTD")
    @Vertical(name = "Core")
    @Test
    public void testTTDSRPPageVisitLogin(){
        Map<String, String> testProperties = getParam("testTTDSRPPageVisitLogin");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);

        String event = testProperties.get("event");
        String eventAction = testProperties.get("eventAction");
        String screenName = testProperties.get("screenName");
        String utmPage = testProperties.get("utmPage");
        String isFirstPage = testProperties.get("isFirstPage");
        String category = testProperties.get("category");
        String vertical = testProperties.get("vertical");

        String loginType = testProperties.get("loginType");
        String email = testProperties.get("email");
        String password = testProperties.get("password");

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, email, password);

        step("Open TTD vertical");
        homePage.openVerticalPage("To Do");

        step("Open SRP Category '"+category+"'");
        ttdLandingPage.openProductByCategory(category);

        sleep(40);

        BaseAssertions ttdPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("SRP event is found", ttdPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, screenName), Is.is(1));
        Assertion.assertThat("eventAction is correct", ttdPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), Is.is(true));
        Assertion.assertThat("vertical is correct", ttdPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "vertical", vertical), Is.is(true));
        Assertion.assertThat("screenName is correct", ttdPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), Is.is(true));
        Assertion.assertThat("utmPage is correct", ttdPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "utmPage", utmPage), Is.is(true));
        Assertion.assertThat("isFirstPage is correct", ttdPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "isFirstPage", isFirstPage), Is.is(true));
    }

    @Author(names = {"Naufal"})
    @Module(name = "TTDSRP")
    @Tribe(name = "TTD")
    @Vertical(name = "Core")
    @Test
    public void testTTDSearchLoadedGuest(){
        Map<String, String> testProperties = getParam("testTTDSearchLoadedGuest");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);

        String event = testProperties.get("event");
        String eventAction = testProperties.get("eventAction");
        String screenName = testProperties.get("screenName");
        String category = testProperties.get("category");
        String eventLabel = testProperties.get("eventLabel");
        String vertical = testProperties.get("vertical");

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Open app as guest");
        loginPage.loginAsGuest();

        step("Open TTD vertical");
        homePage.openVerticalPage("To Do");

        step("Open SRP Category '"+category+"'");
        ttdLandingPage.openProductByCategory(category);

        sleep(40);

        BaseAssertions ttdPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("searchLoaded event is found", ttdPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, screenName), Is.is(1));
        Assertion.assertThat("eventAction is correct", ttdPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), Is.is(true));
        Assertion.assertThat("eventLabel is correct", ttdPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventLabel", eventLabel), Is.is(true));
        Assertion.assertThat("vertical is correct", ttdPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "vertical", vertical), Is.is(true));
        Assertion.assertThat("screenName is correct", ttdPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), Is.is(true));
    }

    @Author(names = {"Naufal"})
    @Module(name = "TTDSRP")
    @Tribe(name = "TTD")
    @Vertical(name = "Core")
    @Test
    public void testTTDSearchLoadedLogin(){
        Map<String, String> testProperties = getParam("testTTDSearchLoadedLogin");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);

        String event = testProperties.get("event");
        String eventAction = testProperties.get("eventAction");
        String screenName = testProperties.get("screenName");
        String category = testProperties.get("category");
        String eventLabel = testProperties.get("eventLabel");
        String vertical = testProperties.get("vertical");

        String loginType = testProperties.get("loginType");
        String email = testProperties.get("email");
        String password = testProperties.get("password");

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, email, password);

        step("Open TTD vertical");
        homePage.openVerticalPage("To Do");

        step("Open SRP Category '"+category+"'");
        ttdLandingPage.openProductByCategory(category);

        sleep(40);

        BaseAssertions ttdPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("searchLoaded event is found", ttdPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, screenName), Is.is(1));
        Assertion.assertThat("eventAction is correct", ttdPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), Is.is(true));
        Assertion.assertThat("eventLabel is correct", ttdPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventLabel", eventLabel), Is.is(true));
        Assertion.assertThat("vertical is correct", ttdPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "vertical", vertical), Is.is(true));
        Assertion.assertThat("screenName is correct", ttdPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), Is.is(true));
    }

    @Author(names = {"Naufal"})
    @Module(name = "TTDSRP")
    @Tribe(name = "TTD")
    @Vertical(name = "Core")
    @Test
    public void testTTDSearchProductGuest(){
        Map<String, String> testProperties = getParam("testTTDSearchProductGuest");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);
        TTDSearchResultPage<WebDriver> ttdSearchResultPage = TTDSearchResultPage.getInstance(session);

        String event = testProperties.get("event");
        String eventAction = testProperties.get("eventAction");
        String screenName = testProperties.get("screenName");
        String eventLabel = testProperties.get("eventLabel");
        String vertical = testProperties.get("vertical");

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Open app as guest");
        loginPage.loginAsGuest();

        step("Open TTD vertical");
        homePage.openVerticalPage("To Do");

        step("Open search bar");
        ttdLandingPage.clickSearchBar();

        step("Click Check Nearby Activity");
        ttdSearchPage.clickNearbyActivity();
        ttdSearchResultPage.setAppPermissionAllow();

        sleep(40);

        BaseAssertions ttdPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("searchProduct event is found", ttdPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, screenName), Is.is(1));
        Assertion.assertThat("eventAction is correct", ttdPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), Is.is(true));
        Assertion.assertThat("eventLabel is correct", ttdPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventLabel", eventLabel), Is.is(true));
        Assertion.assertThat("vertical is correct", ttdPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "vertical", vertical), Is.is(true));
        Assertion.assertThat("screenName is correct", ttdPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), Is.is(true));
    }

    @Author(names = {"Naufal"})
    @Module(name = "TTDSRP")
    @Tribe(name = "TTD")
    @Vertical(name = "Core")
    @Test
    public void testTTDSearchProductLogin(){
        Map<String, String> testProperties = getParam("testTTDSearchProductLogin");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);
        TTDSearchResultPage<WebDriver> ttdSearchResultPage = TTDSearchResultPage.getInstance(session);

        String event = testProperties.get("event");
        String eventAction = testProperties.get("eventAction");
        String screenName = testProperties.get("screenName");
        String eventLabel = testProperties.get("eventLabel");
        String vertical = testProperties.get("vertical");

        String loginType = testProperties.get("loginType");
        String email = testProperties.get("email");
        String password = testProperties.get("password");

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, email, password);

        step("Open TTD vertical");
        homePage.openVerticalPage("To Do");

        step("Open search bar");
        ttdLandingPage.clickSearchBar();

        step("Click Check Nearby Activity");
        ttdSearchPage.clickNearbyActivity();
        ttdSearchResultPage.setAppPermissionAllow();

        sleep(40);

        BaseAssertions ttdPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("searchProduct event is found", ttdPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, screenName), Is.is(1));
        Assertion.assertThat("eventAction is correct", ttdPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), Is.is(true));
        Assertion.assertThat("eventLabel is correct", ttdPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventLabel", eventLabel), Is.is(true));
        Assertion.assertThat("vertical is correct", ttdPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "vertical", vertical), Is.is(true));
        Assertion.assertThat("screenName is correct", ttdPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), Is.is(true));
    }

    @Author(names = {"Naufal"})
    @Module(name = "TTDBookingPage")
    @Tribe(name = "TTD")
    @Vertical(name = "Core")
    @Test
    public void testTTDBookProductSubmitGuest(){
        Map<String, String> testProperties = getParam("testTTDBookProductSubmitGuest");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);
        TtdSelectedPackageDetailPageWeb<?> ttdSelectedPackageDetailPageWeb = TtdSelectedPackageDetailPageWeb.getInstance(session);
        TtdBookingFormPageWeb<?> ttdBookingFormPageWeb = TtdBookingFormPageWeb.getInstance(session);

        String event = testProperties.get("event");
        String eventAction = testProperties.get("eventAction");
        String screenName = testProperties.get("screenName");
        String vertical = testProperties.get("vertical");
        String eventLabel = testProperties.get("eventLabel");
        String product = testProperties.get("product");
        int packageNumber = Integer.parseInt(testProperties.get("packageNumber"));
        int ticketQty = Integer.parseInt(testProperties.get("ticket_qty"));

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Open app as guest");
        loginPage.loginAsGuest();

        step("Open TTD vertical");
        homePage.openVerticalPage("To Do");

        step("Search the product");
        ttdLandingPage.clickSearchBar();
        ttdSearchPage.inputSearchKeyword(product);

        step("select the activity");
        ttdSearchPage.selectRecommendedActivity(product);

        step("Wait for the PDP to be fully loaded");
        webViewPage.switchToWebViewUrl("to-do");
        ttdProductDetailPageWeb.getProductName();

        step("Select ticket");
        ttdProductDetailPageWeb.goToPackagesList();
        ttdProductDetailPageWeb.chooseTicket(packageNumber);

        step("Submit package detail");
        ttdSelectedPackageDetailPageWeb.setTicketQty(ticketQty);
        ttdSelectedPackageDetailPageWeb.clickBookButton();
        webViewPage.switchToNative();

        step("Completing booking form data");
        webViewPage.switchToWebViewUrl("to-do");
        ttdBookingFormPageWeb.fillContactOrderDetail(false);
        ttdBookingFormPageWeb.fillGuestDetail();

        step("Submit booking");
        ttdBookingFormPageWeb.continueToPaymentPage();
        webViewPage.switchToNative();

        sleep(40);

        BaseAssertions ttdPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("BookProduct Submit event is found", ttdPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, screenName), Is.is(1));
        Assertion.assertThat("eventAction is correct", ttdPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), Is.is(true));
        Assertion.assertThat("vertical is correct", ttdPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "vertical", vertical), Is.is(true));
        Assertion.assertThat("screenName is correct", ttdPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), Is.is(true));
        Assertion.assertThat("eventLabel is correct", ttdPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventLabel", eventLabel), Is.is(true));
    }

    @Author(names = {"Naufal"})
    @Module(name = "TTDBookingPage")
    @Tribe(name = "TTD")
    @Vertical(name = "Core")
    @Test
    public void testTTDBookProductSubmitLogin(){
        Map<String, String> testProperties = getParam("testTTDBookProductSubmitLogin");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);
        TtdPackageDetailPageWeb<?> ttdPackageDetailPageWeb = TtdPackageDetailPageWeb.getInstance(session);
        TtdSelectedPackageDetailPageWeb<?> ttdSelectedPackageDetailPageWeb = TtdSelectedPackageDetailPageWeb.getInstance(session);
        TtdBookingFormPageWeb<?> ttdBookingFormPageWeb = TtdBookingFormPageWeb.getInstance(session);

        String event = testProperties.get("event");
        String eventAction = testProperties.get("eventAction");
        String screenName = testProperties.get("screenName");
        String vertical = testProperties.get("vertical");
        String eventLabel = testProperties.get("eventLabel");
        String product = testProperties.get("product");
        int packageNumber = Integer.parseInt(testProperties.get("packageNumber"));
        int ticketQty = Integer.parseInt(testProperties.get("ticket_qty"));

        String loginType = testProperties.get("loginType");
        String email = testProperties.get("email");
        String password = testProperties.get("password");

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, email, password);

        step("Open TTD vertical");
        homePage.openVerticalPage("To Do");

        step("Search the product");
        ttdLandingPage.clickSearchBar();
        ttdSearchPage.inputSearchKeyword(product);

        step("select the activity");
        ttdSearchPage.selectRecommendedActivity(product);

        step("Wait for the PDP to be fully loaded");
        webViewPage.switchToWebViewUrl("to-do");
        ttdProductDetailPageWeb.getProductName();

        step("Select ticket");
        ttdProductDetailPageWeb.goToPackagesList();
        ttdProductDetailPageWeb.chooseTicket(packageNumber);

        step("Submit package detail");
        ttdSelectedPackageDetailPageWeb.setTicketQty(ticketQty);
        ttdSelectedPackageDetailPageWeb.clickBookButton();
        webViewPage.switchToNative();

        step("Completing booking form data");
        webViewPage.switchToWebViewUrl("to-do");
        ttdBookingFormPageWeb.fillContactOrderDetail(true);
        ttdBookingFormPageWeb.fillGuestDetail();

        step("Submit booking");
        ttdBookingFormPageWeb.continueToPaymentPage();
        webViewPage.switchToNative();

        sleep(40);

        BaseAssertions ttdPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("BookProduct Submit event is found", ttdPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, screenName), Is.is(1));
        Assertion.assertThat("eventAction is correct", ttdPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), Is.is(true));
        Assertion.assertThat("vertical is correct", ttdPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "vertical", vertical), Is.is(true));
        Assertion.assertThat("screenName is correct", ttdPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), Is.is(true));
        Assertion.assertThat("eventLabel is correct", ttdPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventLabel", eventLabel), Is.is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "TTDMainPage")
    @Tribe(name = "TTD")
    @Vertical(name = "Core")
    @Test
    public void testTTDMainPageVisitDeeplinkUtmGuest() {
        Map<String, String> testProperties = getParam("testTTDMainPageVisitDeeplinkUtmGuest");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String deeplink = "tiket://m.tiket.com/to-do/?";

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

        step("Open TTD vertical via deeplink");
        String utmProperties = analyticsHomePage.getUtmProperties(utmCampaign, utmContent, utmId, utmMedium, utmSource, utmTerm);
        analyticsHomePage.openDeeplink(deeplink + utmProperties);

        sleep(50);

        BaseAssertions ttdPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("utmAttributedAt is not null", ttdPageAssertion.verifyUserProperty(Configs.MESSAGES, "utmAttributedAt"), is(true));
        Assertion.assertThat("utmCampaign is correct", ttdPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmCampaign", utmCampaign), is(true));
        Assertion.assertThat("utmContent is correct", ttdPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmContent", utmContent), is(true));
        Assertion.assertThat("utmExternal is correct", ttdPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmExternal", utmExternal), is(true));
        Assertion.assertThat("utmId is correct", ttdPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmId", utmId), is(true));
        Assertion.assertThat("utmMedium is correct", ttdPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmMedium", utmMedium), is(true));
        Assertion.assertThat("utmSource is correct", ttdPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmSource", utmSource), is(true));
        Assertion.assertThat("utmTerm is correct", ttdPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmTerm", utmTerm), is(true));
        Assertion.assertThat("userId is null", ttdPageAssertion.verifyUserProperty(Configs.MESSAGES, "userId"), is(false));

        Assertion.assertThat("pageVisit event is found", ttdPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, screenName), is(1));
        Assertion.assertThat("eventAction is correct", ttdPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), is(true));
        Assertion.assertThat("screenName is correct", ttdPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), is(true));
        Assertion.assertThat("utmPage is correct", ttdPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "utmPage", utmPage), is(true));
        Assertion.assertThat("isFirstPage is correct", ttdPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "isFirstPage", isFirstPage), is(true));
        Assertion.assertThat("vertical is correct", ttdPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "vertical", vertical), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "TTDMainPage")
    @Tribe(name = "TTD")
    @Vertical(name = "Core")
    @Test
    public void testTTDMainPageVisitDeeplinkUtmLogin() {
        Map<String, String> testProperties = getParam("testTTDMainPageVisitDeeplinkUtmLogin");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String loginType = testProperties.get("loginType");
        String email = testProperties.get("email");
        String password = testProperties.get("password");
        String deeplink = "tiket://m.tiket.com/to-do/?";

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

        step("Open TTD vertical via deeplink");
        String utmProperties = analyticsHomePage.getUtmProperties(utmCampaign, utmContent, utmId, utmMedium, utmSource, utmTerm);
        analyticsHomePage.openDeeplink(deeplink + utmProperties);

        sleep(50);

        BaseAssertions ttdPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("utmAttributedAt is not null", ttdPageAssertion.verifyUserProperty(Configs.MESSAGES, "utmAttributedAt"), is(true));
        Assertion.assertThat("utmCampaign is correct", ttdPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmCampaign", utmCampaign), is(true));
        Assertion.assertThat("utmContent is correct", ttdPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmContent", utmContent), is(true));
        Assertion.assertThat("utmExternal is correct", ttdPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmExternal", utmExternal), is(true));
        Assertion.assertThat("utmId is correct", ttdPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmId", utmId), is(true));
        Assertion.assertThat("utmMedium is correct", ttdPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmMedium", utmMedium), is(true));
        Assertion.assertThat("utmSource is correct", ttdPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmSource", utmSource), is(true));
        Assertion.assertThat("utmTerm is correct", ttdPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmTerm", utmTerm), is(true));
        Assertion.assertThat("userId is not null", ttdPageAssertion.verifyUserProperty(Configs.MESSAGES, "userId"), is(true));

        Assertion.assertThat("pageVisit event is found", ttdPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, screenName), is(1));
        Assertion.assertThat("eventAction is correct", ttdPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), is(true));
        Assertion.assertThat("screenName is correct", ttdPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), is(true));
        Assertion.assertThat("utmPage is correct", ttdPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "utmPage", utmPage), is(true));
        Assertion.assertThat("isFirstPage is correct", ttdPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "isFirstPage", isFirstPage), is(true));
        Assertion.assertThat("vertical is correct", ttdPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "vertical", vertical), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "TTDSRP")
    @Tribe(name = "TTD")
    @Vertical(name = "Core")
    @Test
    public void testTTDSRPPageVisitDeeplinkUtmGuest() {
        Map<String, String> testProperties = getParam("testTTDSRPPageVisitDeeplinkUtmGuest");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String deeplink = "tiket://m.tiket.com/to-do/search?";

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

        step("Open TTD SRP via deeplink");
        String utmProperties = analyticsHomePage.getUtmProperties(utmCampaign, utmContent, utmId, utmMedium, utmSource, utmTerm);
        analyticsHomePage.openDeeplink(deeplink + utmProperties);

        sleep(50);

        BaseAssertions ttdPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("utmAttributedAt is not null", ttdPageAssertion.verifyUserProperty(Configs.MESSAGES, "utmAttributedAt"), is(true));
        Assertion.assertThat("utmCampaign is correct", ttdPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmCampaign", utmCampaign), is(true));
        Assertion.assertThat("utmContent is correct", ttdPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmContent", utmContent), is(true));
        Assertion.assertThat("utmExternal is correct", ttdPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmExternal", utmExternal), is(true));
        Assertion.assertThat("utmId is correct", ttdPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmId", utmId), is(true));
        Assertion.assertThat("utmMedium is correct", ttdPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmMedium", utmMedium), is(true));
        Assertion.assertThat("utmSource is correct", ttdPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmSource", utmSource), is(true));
        Assertion.assertThat("utmTerm is correct", ttdPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmTerm", utmTerm), is(true));
        Assertion.assertThat("userId is null", ttdPageAssertion.verifyUserProperty(Configs.MESSAGES, "userId"), is(false));

        Assertion.assertThat("pageVisit event is found", ttdPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, screenName), is(1));
        Assertion.assertThat("eventAction is correct", ttdPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), is(true));
        Assertion.assertThat("screenName is correct", ttdPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), is(true));
        Assertion.assertThat("utmPage is correct", ttdPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "utmPage", utmPage), is(true));
        Assertion.assertThat("isFirstPage is correct", ttdPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "isFirstPage", isFirstPage), is(true));
        Assertion.assertThat("vertical is correct", ttdPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "vertical", vertical), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "TTDSRP")
    @Tribe(name = "TTD")
    @Vertical(name = "Core")
    @Test
    public void testTTDSRPPageVisitDeeplinkUtmLogin() {
        Map<String, String> testProperties = getParam("testTTDSRPPageVisitDeeplinkUtmLogin");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String loginType = testProperties.get("loginType");
        String email = testProperties.get("email");
        String password = testProperties.get("password");
        String deeplink = "tiket://m.tiket.com/to-do/search?";

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

        step("Open TTD SRP via deeplink");
        String utmProperties = analyticsHomePage.getUtmProperties(utmCampaign, utmContent, utmId, utmMedium, utmSource, utmTerm);
        analyticsHomePage.openDeeplink(deeplink + utmProperties);

        sleep(50);

        BaseAssertions ttdPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("utmAttributedAt is not null", ttdPageAssertion.verifyUserProperty(Configs.MESSAGES, "utmAttributedAt"), is(true));
        Assertion.assertThat("utmCampaign is correct", ttdPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmCampaign", utmCampaign), is(true));
        Assertion.assertThat("utmContent is correct", ttdPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmContent", utmContent), is(true));
        Assertion.assertThat("utmExternal is correct", ttdPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmExternal", utmExternal), is(true));
        Assertion.assertThat("utmId is correct", ttdPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmId", utmId), is(true));
        Assertion.assertThat("utmMedium is correct", ttdPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmMedium", utmMedium), is(true));
        Assertion.assertThat("utmSource is correct", ttdPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmSource", utmSource), is(true));
        Assertion.assertThat("utmTerm is correct", ttdPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmTerm", utmTerm), is(true));
        Assertion.assertThat("userId is not null", ttdPageAssertion.verifyUserProperty(Configs.MESSAGES, "userId"), is(true));

        Assertion.assertThat("pageVisit event is found", ttdPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, screenName), is(1));
        Assertion.assertThat("eventAction is correct", ttdPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), is(true));
        Assertion.assertThat("screenName is correct", ttdPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), is(true));
        Assertion.assertThat("utmPage is correct", ttdPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "utmPage", utmPage), is(true));
        Assertion.assertThat("isFirstPage is correct", ttdPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "isFirstPage", isFirstPage), is(true));
        Assertion.assertThat("vertical is correct", ttdPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "vertical", vertical), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "TTDPDP")
    @Tribe(name = "TTD")
    @Vertical(name = "Core")
    @Test
    public void testTTDPDPPageVisitDeeplinkUtmGuest() {
        Map<String, String> testProperties = getParam("testTTDPDPPageVisitDeeplinkUtmGuest");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String deeplink = "tiket://m.tiket.com/to-do/jatim-park-3?";

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

        step("Open TTD PDP via deeplink");
        String utmProperties = analyticsHomePage.getUtmProperties(utmCampaign, utmContent, utmId, utmMedium, utmSource, utmTerm);
        analyticsHomePage.openDeeplink(deeplink + utmProperties);

        sleep(50);

        BaseAssertions ttdPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("utmAttributedAt is not null", ttdPageAssertion.verifyUserProperty(Configs.MESSAGES, "utmAttributedAt"), is(true));
        Assertion.assertThat("utmCampaign is correct", ttdPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmCampaign", utmCampaign), is(true));
        Assertion.assertThat("utmContent is correct", ttdPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmContent", utmContent), is(true));
        Assertion.assertThat("utmExternal is correct", ttdPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmExternal", utmExternal), is(true));
        Assertion.assertThat("utmId is correct", ttdPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmId", utmId), is(true));
        Assertion.assertThat("utmMedium is correct", ttdPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmMedium", utmMedium), is(true));
        Assertion.assertThat("utmSource is correct", ttdPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmSource", utmSource), is(true));
        Assertion.assertThat("utmTerm is correct", ttdPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmTerm", utmTerm), is(true));
        Assertion.assertThat("userId is null", ttdPageAssertion.verifyUserProperty(Configs.MESSAGES, "userId"), is(false));

        Assertion.assertThat("pageVisit event is found", ttdPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, screenName), is(1));
        Assertion.assertThat("eventAction is correct", ttdPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), is(true));
        Assertion.assertThat("screenName is correct", ttdPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), is(true));
        Assertion.assertThat("utmPage is correct", ttdPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "utmPage", utmPage), is(true));
        Assertion.assertThat("isFirstPage is correct", ttdPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "isFirstPage", isFirstPage), is(true));
        Assertion.assertThat("vertical is correct", ttdPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "vertical", vertical), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "TTDPDP")
    @Tribe(name = "TTD")
    @Vertical(name = "Core")
    @Test
    public void testTTDPDPPageVisitDeeplinkUtmLogin() {
        Map<String, String> testProperties = getParam("testTTDPDPPageVisitDeeplinkUtmLogin");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String loginType = testProperties.get("loginType");
        String email = testProperties.get("email");
        String password = testProperties.get("password");
        String deeplink = "tiket://m.tiket.com/to-do/jatim-park-3?";

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

        step("Open TTD PDP via deeplink");
        String utmProperties = analyticsHomePage.getUtmProperties(utmCampaign, utmContent, utmId, utmMedium, utmSource, utmTerm);
        analyticsHomePage.openDeeplink(deeplink + utmProperties);

        sleep(50);

        BaseAssertions ttdPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("utmAttributedAt is not null", ttdPageAssertion.verifyUserProperty(Configs.MESSAGES, "utmAttributedAt"), is(true));
        Assertion.assertThat("utmCampaign is correct", ttdPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmCampaign", utmCampaign), is(true));
        Assertion.assertThat("utmContent is correct", ttdPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmContent", utmContent), is(true));
        Assertion.assertThat("utmExternal is correct", ttdPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmExternal", utmExternal), is(true));
        Assertion.assertThat("utmId is correct", ttdPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmId", utmId), is(true));
        Assertion.assertThat("utmMedium is correct", ttdPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmMedium", utmMedium), is(true));
        Assertion.assertThat("utmSource is correct", ttdPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmSource", utmSource), is(true));
        Assertion.assertThat("utmTerm is correct", ttdPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmTerm", utmTerm), is(true));
        Assertion.assertThat("userId is not null", ttdPageAssertion.verifyUserProperty(Configs.MESSAGES, "userId"), is(true));

        Assertion.assertThat("pageVisit event is found", ttdPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, screenName), is(1));
        Assertion.assertThat("eventAction is correct", ttdPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), is(true));
        Assertion.assertThat("screenName is correct", ttdPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), is(true));
        Assertion.assertThat("utmPage is correct", ttdPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "utmPage", utmPage), is(true));
        Assertion.assertThat("isFirstPage is correct", ttdPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "isFirstPage", isFirstPage), is(true));
        Assertion.assertThat("vertical is correct", ttdPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "vertical", vertical), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "TTDMainPage")
    @Tribe(name = "TTD")
    @Vertical(name = "Core")
    @Test
    public void testTTDMainPageVisitDeeplinkNonUtmGuest() {
        Map<String, String> testProperties = getParam("testTTDMainPageVisitDeeplinkNonUtmGuest");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String deeplink = "tiket://m.tiket.com/to-do";

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

        step("Open TTD vertical via deeplink");
        analyticsHomePage.openDeeplink(deeplink);

        sleep(50);

        BaseAssertions ttdPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("utmAttributedAt is not null", ttdPageAssertion.verifyUserProperty(Configs.MESSAGES, "utmAttributedAt"), is(true));
        Assertion.assertThat("utmCampaign is correct", ttdPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmCampaign", utmCampaign), is(true));
        Assertion.assertThat("utmContent is correct", ttdPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmContent", utmContent), is(true));
        Assertion.assertThat("utmExternal is correct", ttdPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmExternal", utmExternal), is(true));
        Assertion.assertThat("utmId is correct", ttdPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmId", utmId), is(true));
        Assertion.assertThat("utmMedium is correct", ttdPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmMedium", utmMedium), is(true));
        Assertion.assertThat("utmSource is correct", ttdPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmSource", utmSource), is(true));
        Assertion.assertThat("utmTerm is correct", ttdPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmTerm", utmTerm), is(true));
        Assertion.assertThat("userId is null", ttdPageAssertion.verifyUserProperty(Configs.MESSAGES, "userId"), is(false));

        Assertion.assertThat("pageVisit event is found", ttdPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, screenName), is(1));
        Assertion.assertThat("eventAction is correct", ttdPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), is(true));
        Assertion.assertThat("screenName is correct", ttdPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), is(true));
        Assertion.assertThat("utmPage is correct", ttdPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "utmPage", utmPage), is(true));
        Assertion.assertThat("isFirstPage is correct", ttdPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "isFirstPage", isFirstPage), is(true));
        Assertion.assertThat("vertical is correct", ttdPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "vertical", vertical), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "TTDMainPage")
    @Tribe(name = "TTD")
    @Vertical(name = "Core")
    @Test
    public void testTTDMainPageVisitDeeplinkNonUtmLogin() {
        Map<String, String> testProperties = getParam("testTTDMainPageVisitDeeplinkNonUtmLogin");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String loginType = testProperties.get("loginType");
        String email = testProperties.get("email");
        String password = testProperties.get("password");
        String deeplink = "tiket://m.tiket.com/to-do";

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

        step("Open TTD vertical via deeplink");
        analyticsHomePage.openDeeplink(deeplink);

        sleep(50);

        BaseAssertions ttdPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("utmAttributedAt is not null", ttdPageAssertion.verifyUserProperty(Configs.MESSAGES, "utmAttributedAt"), is(true));
        Assertion.assertThat("utmCampaign is correct", ttdPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmCampaign", utmCampaign), is(true));
        Assertion.assertThat("utmContent is correct", ttdPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmContent", utmContent), is(true));
        Assertion.assertThat("utmExternal is correct", ttdPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmExternal", utmExternal), is(true));
        Assertion.assertThat("utmId is correct", ttdPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmId", utmId), is(true));
        Assertion.assertThat("utmMedium is correct", ttdPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmMedium", utmMedium), is(true));
        Assertion.assertThat("utmSource is correct", ttdPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmSource", utmSource), is(true));
        Assertion.assertThat("utmTerm is correct", ttdPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmTerm", utmTerm), is(true));
        Assertion.assertThat("userId is not null", ttdPageAssertion.verifyUserProperty(Configs.MESSAGES, "userId"), is(true));

        Assertion.assertThat("pageVisit event is found", ttdPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, screenName), is(1));
        Assertion.assertThat("eventAction is correct", ttdPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), is(true));
        Assertion.assertThat("screenName is correct", ttdPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), is(true));
        Assertion.assertThat("utmPage is correct", ttdPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "utmPage", utmPage), is(true));
        Assertion.assertThat("isFirstPage is correct", ttdPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "isFirstPage", isFirstPage), is(true));
        Assertion.assertThat("vertical is correct", ttdPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "vertical", vertical), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "TTDSRP")
    @Tribe(name = "TTD")
    @Vertical(name = "Core")
    @Test
    public void testTTDSRPPageVisitDeeplinkNonUtmGuest() {
        Map<String, String> testProperties = getParam("testTTDSRPPageVisitDeeplinkNonUtmGuest");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String deeplink = "tiket://m.tiket.com/to-do/search";

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

        step("Open TTD SRP via deeplink");
        analyticsHomePage.openDeeplink(deeplink);

        sleep(50);

        BaseAssertions ttdPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("utmAttributedAt is not null", ttdPageAssertion.verifyUserProperty(Configs.MESSAGES, "utmAttributedAt"), is(true));
        Assertion.assertThat("utmCampaign is correct", ttdPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmCampaign", utmCampaign), is(true));
        Assertion.assertThat("utmContent is correct", ttdPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmContent", utmContent), is(true));
        Assertion.assertThat("utmExternal is correct", ttdPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmExternal", utmExternal), is(true));
        Assertion.assertThat("utmId is correct", ttdPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmId", utmId), is(true));
        Assertion.assertThat("utmMedium is correct", ttdPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmMedium", utmMedium), is(true));
        Assertion.assertThat("utmSource is correct", ttdPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmSource", utmSource), is(true));
        Assertion.assertThat("utmTerm is correct", ttdPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmTerm", utmTerm), is(true));
        Assertion.assertThat("userId is null", ttdPageAssertion.verifyUserProperty(Configs.MESSAGES, "userId"), is(false));

        Assertion.assertThat("pageVisit event is found", ttdPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, screenName), is(1));
        Assertion.assertThat("eventAction is correct", ttdPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), is(true));
        Assertion.assertThat("screenName is correct", ttdPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), is(true));
        Assertion.assertThat("utmPage is correct", ttdPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "utmPage", utmPage), is(true));
        Assertion.assertThat("isFirstPage is correct", ttdPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "isFirstPage", isFirstPage), is(true));
        Assertion.assertThat("vertical is correct", ttdPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "vertical", vertical), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "TTDSRP")
    @Tribe(name = "TTD")
    @Vertical(name = "Core")
    @Test
    public void testTTDSRPPageVisitDeeplinkNonUtmLogin() {
        Map<String, String> testProperties = getParam("testTTDSRPPageVisitDeeplinkNonUtmLogin");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String loginType = testProperties.get("loginType");
        String email = testProperties.get("email");
        String password = testProperties.get("password");
        String deeplink = "tiket://m.tiket.com/to-do/search";

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

        step("Open TTD SRP via deeplink");
        analyticsHomePage.openDeeplink(deeplink);

        sleep(50);

        BaseAssertions ttdPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("utmAttributedAt is not null", ttdPageAssertion.verifyUserProperty(Configs.MESSAGES, "utmAttributedAt"), is(true));
        Assertion.assertThat("utmCampaign is correct", ttdPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmCampaign", utmCampaign), is(true));
        Assertion.assertThat("utmContent is correct", ttdPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmContent", utmContent), is(true));
        Assertion.assertThat("utmExternal is correct", ttdPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmExternal", utmExternal), is(true));
        Assertion.assertThat("utmId is correct", ttdPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmId", utmId), is(true));
        Assertion.assertThat("utmMedium is correct", ttdPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmMedium", utmMedium), is(true));
        Assertion.assertThat("utmSource is correct", ttdPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmSource", utmSource), is(true));
        Assertion.assertThat("utmTerm is correct", ttdPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmTerm", utmTerm), is(true));
        Assertion.assertThat("userId is not null", ttdPageAssertion.verifyUserProperty(Configs.MESSAGES, "userId"), is(true));

        Assertion.assertThat("pageVisit event is found", ttdPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, screenName), is(1));
        Assertion.assertThat("eventAction is correct", ttdPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), is(true));
        Assertion.assertThat("screenName is correct", ttdPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), is(true));
        Assertion.assertThat("utmPage is correct", ttdPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "utmPage", utmPage), is(true));
        Assertion.assertThat("isFirstPage is correct", ttdPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "isFirstPage", isFirstPage), is(true));
        Assertion.assertThat("vertical is correct", ttdPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "vertical", vertical), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "TTDPDP")
    @Tribe(name = "TTD")
    @Vertical(name = "Core")
    @Test
    public void testTTDPDPPageVisitDeeplinkNonUtmGuest() {
        Map<String, String> testProperties = getParam("testTTDPDPPageVisitDeeplinkNonUtmGuest");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String deeplink = "tiket://m.tiket.com/to-do/jatim-park-3";

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

        step("Open TTD PDP via deeplink");
        analyticsHomePage.openDeeplink(deeplink);

        sleep(50);

        BaseAssertions ttdPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("utmAttributedAt is not null", ttdPageAssertion.verifyUserProperty(Configs.MESSAGES, "utmAttributedAt"), is(true));
        Assertion.assertThat("utmCampaign is correct", ttdPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmCampaign", utmCampaign), is(true));
        Assertion.assertThat("utmContent is correct", ttdPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmContent", utmContent), is(true));
        Assertion.assertThat("utmExternal is correct", ttdPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmExternal", utmExternal), is(true));
        Assertion.assertThat("utmId is correct", ttdPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmId", utmId), is(true));
        Assertion.assertThat("utmMedium is correct", ttdPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmMedium", utmMedium), is(true));
        Assertion.assertThat("utmSource is correct", ttdPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmSource", utmSource), is(true));
        Assertion.assertThat("utmTerm is correct", ttdPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmTerm", utmTerm), is(true));
        Assertion.assertThat("userId is null", ttdPageAssertion.verifyUserProperty(Configs.MESSAGES, "userId"), is(false));

        Assertion.assertThat("pageVisit event is found", ttdPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, screenName), is(1));
        Assertion.assertThat("eventAction is correct", ttdPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), is(true));
        Assertion.assertThat("screenName is correct", ttdPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), is(true));
        Assertion.assertThat("utmPage is correct", ttdPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "utmPage", utmPage), is(true));
        Assertion.assertThat("isFirstPage is correct", ttdPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "isFirstPage", isFirstPage), is(true));
        Assertion.assertThat("vertical is correct", ttdPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "vertical", vertical), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "TTDPDP")
    @Tribe(name = "TTD")
    @Vertical(name = "Core")
    @Test
    public void testTTDPDPPageVisitDeeplinkNonUtmLogin() {
        Map<String, String> testProperties = getParam("testTTDPDPPageVisitDeeplinkNonUtmLogin");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String loginType = testProperties.get("loginType");
        String email = testProperties.get("email");
        String password = testProperties.get("password");
        String deeplink = "tiket://m.tiket.com/to-do/jatim-park-3";

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

        step("Open TTD PDP via deeplink");
        analyticsHomePage.openDeeplink(deeplink);

        sleep(50);

        BaseAssertions ttdPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("utmAttributedAt is not null", ttdPageAssertion.verifyUserProperty(Configs.MESSAGES, "utmAttributedAt"), is(true));
        Assertion.assertThat("utmCampaign is correct", ttdPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmCampaign", utmCampaign), is(true));
        Assertion.assertThat("utmContent is correct", ttdPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmContent", utmContent), is(true));
        Assertion.assertThat("utmExternal is correct", ttdPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmExternal", utmExternal), is(true));
        Assertion.assertThat("utmId is correct", ttdPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmId", utmId), is(true));
        Assertion.assertThat("utmMedium is correct", ttdPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmMedium", utmMedium), is(true));
        Assertion.assertThat("utmSource is correct", ttdPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmSource", utmSource), is(true));
        Assertion.assertThat("utmTerm is correct", ttdPageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmTerm", utmTerm), is(true));
        Assertion.assertThat("userId is not null", ttdPageAssertion.verifyUserProperty(Configs.MESSAGES, "userId"), is(true));

        Assertion.assertThat("pageVisit event is found", ttdPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, screenName), is(1));
        Assertion.assertThat("eventAction is correct", ttdPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), is(true));
        Assertion.assertThat("screenName is correct", ttdPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), is(true));
        Assertion.assertThat("utmPage is correct", ttdPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "utmPage", utmPage), is(true));
        Assertion.assertThat("isFirstPage is correct", ttdPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "isFirstPage", isFirstPage), is(true));
        Assertion.assertThat("vertical is correct", ttdPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "vertical", vertical), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "TTDPDP")
    @Tribe(name = "TTD")
    @Vertical(name = "Core")
    @Test
    public void testTTDPDPPageVisitGuest(){
        Map<String, String> testProperties = getParam("testTTDPDPPageVisitGuest");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);

        String event = testProperties.get("event");
        String eventAction = testProperties.get("eventAction");
        String screenName = testProperties.get("screenName");
        String utmPage = testProperties.get("utmPage");
        String isFirstPage = testProperties.get("isFirstPage");
        String vertical = testProperties.get("vertical");
        String product = testProperties.get("product");

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Open app as guest");
        loginPage.loginAsGuest();

        step("Open TTD vertical");
        homePage.openVerticalPage("To Do");

        step("Search the product");
        ttdLandingPage.clickSearchBar();
        ttdSearchPage.inputSearchKeyword(product);

        step("select the activity");
        ttdSearchPage.selectRecommendedActivity(product);

        sleep(40);

        BaseAssertions ttdPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("pageVisit event is found", ttdPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, screenName), is(1));
        Assertion.assertThat("eventAction is correct", ttdPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), is(true));
        Assertion.assertThat("screenName is correct", ttdPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), is(true));
        Assertion.assertThat("utmPage is correct", ttdPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "utmPage", utmPage), is(true));
        Assertion.assertThat("isFirstPage is correct", ttdPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "isFirstPage", isFirstPage), is(true));
        Assertion.assertThat("vertical is correct", ttdPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "vertical", vertical), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "TTDBookingPage")
    @Tribe(name = "TTD")
    @Vertical(name = "Core")
    @Test
    public void testTTDPDPPageVisitLogin(){
        Map<String, String> testProperties = getParam("testTTDPDPPageVisitLogin");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);

        String event = testProperties.get("event");
        String eventAction = testProperties.get("eventAction");
        String screenName = testProperties.get("screenName");
        String utmPage = testProperties.get("utmPage");
        String isFirstPage = testProperties.get("isFirstPage");
        String vertical = testProperties.get("vertical");
        String product = testProperties.get("product");

        String loginType = testProperties.get("loginType");
        String email = testProperties.get("email");
        String password = testProperties.get("password");

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, email, password);

        step("Open TTD vertical");
        homePage.openVerticalPage("To Do");

        step("Search the product");
        ttdLandingPage.clickSearchBar();
        ttdSearchPage.inputSearchKeyword(product);

        step("select the activity");
        ttdSearchPage.selectRecommendedActivity(product);

        sleep(40);

        BaseAssertions ttdPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("pageVisit event is found", ttdPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, screenName), is(1));
        Assertion.assertThat("eventAction is correct", ttdPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), is(true));
        Assertion.assertThat("screenName is correct", ttdPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), is(true));
        Assertion.assertThat("utmPage is correct", ttdPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "utmPage", utmPage), is(true));
        Assertion.assertThat("isFirstPage is correct", ttdPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "isFirstPage", isFirstPage), is(true));
        Assertion.assertThat("vertical is correct", ttdPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "vertical", vertical), is(true));
    }
}