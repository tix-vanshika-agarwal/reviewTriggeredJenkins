package com.tiket.analytics.app.test.platform;

import com.tiket.analytics.BaseAnalyticsTest;
import com.tiket.analytics.assertions.BaseAssertions;
import com.tiket.analytics.app.home.AnalyticsHomePage;
import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.model.Session;
import com.tiket.page.home.HomePage;
import com.tiket.page.login.LoginPage;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.testbase.Configs;
import org.hamcrest.Matchers;
import org.hamcrest.core.Is;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import java.util.Map;

import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;

public class DiscoveryPageTest extends BaseAnalyticsTest {

    @Author(names = {"Naufal"})
    @Module(name = "HomePageWeb")
    @Tribe(name = "Discovery")
    @Vertical(name = "Core")
    @Test
    public void testHomePageVisitGuest(){
        Map<String, String> testProperties = getParam("testHomePageVisitGuest");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String event = testProperties.get("event");
        String eventAction = testProperties.get("eventAction");
        String screenName = testProperties.get("screenName");
        String isFirstPage = testProperties.get("isFirstPage");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Open app as guest");
        loginPage.loginAsGuest();

        sleep(40);

        BaseAssertions platformPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("PageVisit event is found", platformPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, screenName), is(1));
        Assertion.assertThat("eventAction is correct", platformPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), Is.is(true));
        Assertion.assertThat("screenName is correct", platformPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), Is.is(true));
        Assertion.assertThat("isFirstPage is correct", platformPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "isFirstPage", isFirstPage), Is.is(true));
    }

    @Author(names = {"Naufal"})
    @Module(name = "HomePageWeb")
    @Tribe(name = "Discovery")
    @Vertical(name = "Core")
    @Test
    public void testHomePageVisitLogin(){
        Map<String, String> testProperties = getParam("testHomePageVisitLogin");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String event = testProperties.get("event");
        String eventAction = testProperties.get("eventAction");
        String screenName = testProperties.get("screenName");
        String utmPage = testProperties.get("utmPage");
        String isFirstPage = testProperties.get("isFirstPage");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        AnalyticsHomePage<WebDriver> analyticsHomePage = AnalyticsHomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);

        step("Wait for app to launch");
        homePage.gotoHome();

        String loginType = testProperties.get("loginType");
        String email = testProperties.get("email");
        String password = testProperties.get("password");

        step("Input credential on login page");
        loginPage.loginWith(loginType, email, password);

        step("sleep for 30s to ensure there will be no left over amplitude api calls after clearing the messages");
        sleep(30);
        Configs.MESSAGES.clear();
        analyticsHomePage.restartApp();

        sleep(40);

        BaseAssertions platformPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("PageVisit event is found", platformPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, screenName), is(1));
        Assertion.assertThat("eventAction is correct", platformPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), Is.is(true));
        Assertion.assertThat("utmPage is correct", platformPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "utmPage", utmPage), Is.is(true));
        Assertion.assertThat("screenName is correct", platformPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), Is.is(true));
        Assertion.assertThat("isFirstPage is correct", platformPageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "isFirstPage", isFirstPage), Is.is(true));
    }

    @Author(names = {"Naufal"})
    @Module(name = "HomePageWeb")
    @Tribe(name = "Discovery")
    @Vertical(name = "Core")
    @Test
    public void testLaunchTiketGuest(){
        Map<String, String> testProperties = getParam("testLaunchTiketGuest");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String event = testProperties.get("event");
        String eventAction = testProperties.get("eventAction");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        AnalyticsHomePage<WebDriver> analyticsHomePage = AnalyticsHomePage.getInstance(session);

        step("Wait for app to launch");
        homePage.gotoHome();
        analyticsHomePage.restartApp();

        step("Open app as guest");
        loginPage.loginAsGuest();

        sleep(40);

        BaseAssertions platformPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("launchTiket event is found", platformPageAssertion.verifyEvent(Configs.MESSAGES, event), is(1));
        Assertion.assertThat("eventAction is correct", platformPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "eventAction", eventAction), Is.is(true));
    }

    @Author(names = {"Naufal"})
    @Module(name = "HomePageWeb")
    @Tribe(name = "Discovery")
    @Vertical(name = "Core")
    @Test
    public void testLaunchTiketLogin(){
        Map<String, String> testProperties = getParam("testLaunchTiketLogin");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String event = testProperties.get("event");
        String eventAction = testProperties.get("eventAction");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        AnalyticsHomePage<WebDriver> analyticsHomePage = AnalyticsHomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);

        step("Wait for app to launch");
        homePage.gotoHome();

        String loginType = testProperties.get("loginType");
        String email = testProperties.get("email");
        String password = testProperties.get("password");

        step("Input credential on login page");
        loginPage.loginWith(loginType, email, password);

        step("sleep for 30s to ensure there will be no left over amplitude api calls after clearing the messages");
        sleep(30);
        Configs.MESSAGES.clear();
        analyticsHomePage.restartApp();

        sleep(40);

        BaseAssertions platformPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("launchTiket event is found", platformPageAssertion.verifyEvent(Configs.MESSAGES, event), is(1));
        Assertion.assertThat("eventAction is correct", platformPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "eventAction", eventAction), Is.is(true));
    }

    @Author(names = {"Naufal"})
    @Module(name = "HomePageWeb")
    @Tribe(name = "Discovery")
    @Vertical(name = "Core")
    @Test
    public void testClaimCenterEnterVerticalGuest(){
        Map<String, String> testProperties = getParam("testClaimCenterEnterVerticalGuest");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String event = testProperties.get("event");
        String eventAction = testProperties.get("eventAction");
        String screenName = testProperties.get("screenName");
        String eventLabel = testProperties.get("eventLabel");
        String vertical = testProperties.get("vertical");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Open app as guest");
        loginPage.loginAsGuest();

        step("Change currency");
        homePage.changeCurrency("IDR", "");

        step("Open claim center vertical");
        homePage.openVerticalPage("Claim Center");

        sleep(40);

        BaseAssertions platformPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("enterVertical event is found", platformPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, vertical), is(1));
        Assertion.assertThat("eventAction is correct", platformPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "eventAction", eventAction), Is.is(true));
        Assertion.assertThat("screenName is correct", platformPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "screenName", screenName), Is.is(true));
        Assertion.assertThat("eventLabel is correct", platformPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "eventLabel", eventLabel), Is.is(true));
        Assertion.assertThat("vertical is correct", platformPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "vertical", vertical), Is.is(true));
    }

    @Author(names = {"Naufal"})
    @Module(name = "HomePageWeb")
    @Tribe(name = "Discovery")
    @Vertical(name = "Core")
    @Test
    public void testBlibliEnterVerticalGuest(){
        Map<String, String> testProperties = getParam("testBlibliEnterVerticalGuest");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String event = testProperties.get("event");
        String eventAction = testProperties.get("eventAction");
        String screenName = testProperties.get("screenName");
        String eventLabel = testProperties.get("eventLabel");
        String vertical = testProperties.get("vertical");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Open app as guest");
        loginPage.loginAsGuest();

        step("Change currency");
        homePage.changeCurrency("IDR", "");

        step("Open Blibli vertical");
        homePage.openVerticalPage("Blibli");

        sleep(40);

        BaseAssertions platformPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("enterVertical event is found", platformPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, vertical), is(1));
        Assertion.assertThat("eventAction is correct", platformPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "eventAction", eventAction), Is.is(true));
        Assertion.assertThat("screenName is correct", platformPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "screenName", screenName), Is.is(true));
        Assertion.assertThat("eventLabel is correct", platformPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "eventLabel", eventLabel), Is.is(true));
        Assertion.assertThat("vertical is correct", platformPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "vertical", vertical), Is.is(true));
    }

    @Author(names = {"Naufal"})
    @Module(name = "HomePageWeb")
    @Tribe(name = "Discovery")
    @Vertical(name = "Core")
    @Test
    public void testTiketPlusEnterVerticalGuest(){
        Map<String, String> testProperties = getParam("testTiketPlusEnterVerticalGuest");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String event = testProperties.get("event");
        String eventAction = testProperties.get("eventAction");
        String screenName = testProperties.get("screenName");
        String eventLabel = testProperties.get("eventLabel");
        String vertical = testProperties.get("vertical");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Open app as guest");
        loginPage.loginAsGuest();

        step("Change currency");
        homePage.changeCurrency("IDR", "");

        step("Open Tiket+ vertical");
        homePage.openVerticalPage("tiket+");

        sleep(40);

        BaseAssertions platformPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("enterVertical event is found", platformPageAssertion.verifyEvent(Configs.MESSAGES, event), is(1));
        Assertion.assertThat("eventAction is correct", platformPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "eventAction", eventAction), Is.is(true));
        Assertion.assertThat("screenName is correct", platformPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "screenName", screenName), Is.is(true));
        Assertion.assertThat("eventLabel is correct", platformPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "eventLabel", eventLabel), Is.is(true));
        Assertion.assertThat("vertical is correct", platformPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "vertical", vertical), Is.is(true));
    }

    @Author(names = {"Naufal"})
    @Module(name = "HomePageWeb")
    @Tribe(name = "Discovery")
    @Vertical(name = "Core")
    @Test
    public void testReferralEnterVerticalLogin(){
        Map<String, String> testProperties = getParam("testReferralEnterVerticalLogin");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String event = testProperties.get("event");
        String eventAction = testProperties.get("eventAction");
        String screenName = testProperties.get("screenName");
        String eventLabel = testProperties.get("eventLabel");
        String vertical = testProperties.get("vertical");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);

        step("Wait for app to launch");
        homePage.gotoHome();

        String loginType = testProperties.get("loginType");
        String email = testProperties.get("email");
        String password = testProperties.get("password");

        step("Input credential on login page");
        loginPage.loginWith(loginType, email, password);

        step("Change currency");
        homePage.changeCurrency("IDR", email);

        step("Open Referral vertical");
        homePage.openVerticalPage("referral");

        sleep(40);

        BaseAssertions platformPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("enterVertical event is found", platformPageAssertion.verifyEvent(Configs.MESSAGES, event), is(1));
        Assertion.assertThat("eventAction is correct", platformPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "eventAction", eventAction), Is.is(true));
        Assertion.assertThat("screenName is correct", platformPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "screenName", screenName), Is.is(true));
        Assertion.assertThat("eventLabel is correct", platformPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "eventLabel", eventLabel), Is.is(true));
        Assertion.assertThat("vertical is correct", platformPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "vertical", vertical), Is.is(true));
    }

    @Author(names = {"Naufal"})
    @Module(name = "HomePageWeb")
    @Tribe(name = "Discovery")
    @Vertical(name = "Core")
    @Test
    public void testClaimCenterEnterVerticalLogin(){
        Map<String, String> testProperties = getParam("testClaimCenterEnterVerticalLogin");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String event = testProperties.get("event");
        String eventAction = testProperties.get("eventAction");
        String screenName = testProperties.get("screenName");
        String eventLabel = testProperties.get("eventLabel");
        String vertical = testProperties.get("vertical");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);

        step("Wait for app to launch");
        homePage.gotoHome();

        String loginType = testProperties.get("loginType");
        String email = testProperties.get("email");
        String password = testProperties.get("password");

        step("Input credential on login page");
        loginPage.loginWith(loginType, email, password);

        step("Change currency");
        homePage.changeCurrency("IDR", email);

        step("Open Claim Center vertical");
        homePage.openVerticalPage("Claim Center");

        sleep(40);

        BaseAssertions platformPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("enterVertical event is found", platformPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, vertical), is(1));
        Assertion.assertThat("eventAction is correct", platformPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "eventAction", eventAction), Is.is(true));
        Assertion.assertThat("screenName is correct", platformPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "screenName", screenName), Is.is(true));
        Assertion.assertThat("eventLabel is correct", platformPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "eventLabel", eventLabel), Is.is(true));
        Assertion.assertThat("vertical is correct", platformPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "vertical", vertical), Is.is(true));
    }

    @Author(names = {"Naufal"})
    @Module(name = "HomePageWeb")
    @Tribe(name = "Discovery")
    @Vertical(name = "Core")
    @Test
    public void testBlibliEnterVerticalLogin(){
        Map<String, String> testProperties = getParam("testBlibliEnterVerticalLogin");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String event = testProperties.get("event");
        String eventAction = testProperties.get("eventAction");
        String screenName = testProperties.get("screenName");
        String eventLabel = testProperties.get("eventLabel");
        String vertical = testProperties.get("vertical");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);

        step("Wait for app to launch");
        homePage.gotoHome();

        String loginType = testProperties.get("loginType");
        String email = testProperties.get("email");
        String password = testProperties.get("password");

        step("Input credential on login page");
        loginPage.loginWith(loginType, email, password);

        step("Change currency");
        homePage.changeCurrency("IDR", email);

        step("Open Blibli vertical");
        homePage.openVerticalPage("Blibli");

        sleep(40);

        BaseAssertions platformPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("enterVertical event is found", platformPageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, vertical), is(1));
        Assertion.assertThat("eventAction is correct", platformPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "eventAction", eventAction), Is.is(true));
        Assertion.assertThat("screenName is correct", platformPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "screenName", screenName), Is.is(true));
        Assertion.assertThat("eventLabel is correct", platformPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "eventLabel", eventLabel), Is.is(true));
        Assertion.assertThat("vertical is correct", platformPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "vertical", vertical), Is.is(true));
    }

    @Author(names = {"Naufal"})
    @Module(name = "HomePageWeb")
    @Tribe(name = "Discovery")
    @Vertical(name = "Core")
    @Test
    public void testTiketPlusEnterVerticalLogin(){
        Map<String, String> testProperties = getParam("testTiketPlusEnterVerticalLogin");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String event = testProperties.get("event");
        String eventAction = testProperties.get("eventAction");
        String screenName = testProperties.get("screenName");
        String eventLabel = testProperties.get("eventLabel");
        String vertical = testProperties.get("vertical");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);

        step("Wait for app to launch");
        homePage.gotoHome();

        String loginType = testProperties.get("loginType");
        String email = testProperties.get("email");
        String password = testProperties.get("password");

        step("Input credential on login page");
        loginPage.loginWith(loginType, email, password);

        step("Change currency");
        homePage.changeCurrency("IDR", email);

        step("Open Tiket+ vertical");
        homePage.openVerticalPage("tiket+");

        sleep(40);

        BaseAssertions platformPageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("enterVertical event is found", platformPageAssertion.verifyEvent(Configs.MESSAGES, event), is(1));
        Assertion.assertThat("eventAction is correct", platformPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "eventAction", eventAction), Is.is(true));
        Assertion.assertThat("screenName is correct", platformPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "screenName", screenName), Is.is(true));
        Assertion.assertThat("eventLabel is correct", platformPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "eventLabel", eventLabel), Is.is(true));
        Assertion.assertThat("vertical is correct", platformPageAssertion.haveCorrectEventProperty(Configs.MESSAGES, event, "vertical", vertical), Is.is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "HomePageWeb")
    @Tribe(name = "PRO")
    @Vertical(name = "Core")
    @Test
    public void testHomePageVisitDeeplinkUtmGuest() {
        Map<String, String> testProperties = getParam("testHomePageVisitDeeplinkUtmGuest");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String deeplink = "tiket://m.tiket.com/?";

        String event = testProperties.get("event");
        String eventAction = testProperties.get("eventAction");
        String screenName = testProperties.get("screenName");
        String utmPage = testProperties.get("utmPage");
        String isFirstPage = testProperties.get("isFirstPage");

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

        step("Open home page via deeplink");
        String utmProperties = analyticsHomePage.getUtmProperties(utmCampaign, utmContent, utmId, utmMedium, utmSource, utmTerm);
        analyticsHomePage.openDeeplink(deeplink + utmProperties);

        sleep(40);

        BaseAssertions homePageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("utmAttributedAt is not null", homePageAssertion.verifyUserProperty(Configs.MESSAGES, "utmAttributedAt"), is(true));
        Assertion.assertThat("utmCampaign is correct", homePageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmCampaign", utmCampaign), is(true));
        Assertion.assertThat("utmContent is correct", homePageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmContent", utmContent), is(true));
        Assertion.assertThat("utmExternal is correct", homePageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmExternal", utmExternal), is(true));
        Assertion.assertThat("utmId is correct", homePageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmId", utmId), is(true));
        Assertion.assertThat("utmMedium is correct", homePageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmMedium", utmMedium), is(true));
        Assertion.assertThat("utmSource is correct", homePageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmSource", utmSource), is(true));
        Assertion.assertThat("utmTerm is correct", homePageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmTerm", utmTerm), is(true));
        Assertion.assertThat("userId is null", homePageAssertion.verifyUserProperty(Configs.MESSAGES, "userId"), Matchers.is(false));

        Assertion.assertThat("pageVisit event is found", homePageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, screenName), greaterThan(0));
        Assertion.assertThat("eventAction is correct", homePageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), is(true));
        Assertion.assertThat("screenName is correct", homePageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), is(true));
        Assertion.assertThat("utmPage is correct", homePageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "utmPage", utmPage), is(true));
        Assertion.assertThat("isFirstPage is correct", homePageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "isFirstPage", isFirstPage), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "PromoMainPage")
    @Tribe(name = "PRO")
    @Vertical(name = "Core")
    @Test
    public void testHomePageVisitDeeplinkUtmLogin() {
        Map<String, String> testProperties = getParam("testHomePageVisitDeeplinkUtmLogin");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String loginType = testProperties.get("loginType");
        String email = testProperties.get("email");
        String password = testProperties.get("password");
        String deeplink = "tiket://m.tiket.com/?";

        String event = testProperties.get("event");
        String eventAction = testProperties.get("eventAction");
        String screenName = testProperties.get("screenName");
        String utmPage = testProperties.get("utmPage");
        String isFirstPage = testProperties.get("isFirstPage");

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

        step("Open home page via deeplink");
        String utmProperties = analyticsHomePage.getUtmProperties(utmCampaign, utmContent, utmId, utmMedium, utmSource, utmTerm);
        analyticsHomePage.openDeeplink(deeplink + utmProperties);

        sleep(40);

        BaseAssertions homePageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("utmAttributedAt is not null", homePageAssertion.verifyUserProperty(Configs.MESSAGES, "utmAttributedAt"), is(true));
        Assertion.assertThat("utmCampaign is correct", homePageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmCampaign", utmCampaign), is(true));
        Assertion.assertThat("utmContent is correct", homePageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmContent", utmContent), is(true));
        Assertion.assertThat("utmExternal is correct", homePageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmExternal", utmExternal), is(true));
        Assertion.assertThat("utmId is correct", homePageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmId", utmId), is(true));
        Assertion.assertThat("utmMedium is correct", homePageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmMedium", utmMedium), is(true));
        Assertion.assertThat("utmSource is correct", homePageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmSource", utmSource), is(true));
        Assertion.assertThat("utmTerm is correct", homePageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmTerm", utmTerm), is(true));
        Assertion.assertThat("userId is not null", homePageAssertion.verifyUserProperty(Configs.MESSAGES, "userId"), Matchers.is(true));

        Assertion.assertThat("pageVisit event is found", homePageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, screenName), greaterThan(0));
        Assertion.assertThat("eventAction is correct", homePageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), is(true));
        Assertion.assertThat("screenName is correct", homePageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), is(true));
        Assertion.assertThat("utmPage is correct", homePageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "utmPage", utmPage), is(true));
        Assertion.assertThat("isFirstPage is correct", homePageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "isFirstPage", isFirstPage), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "HomePageWeb")
    @Tribe(name = "PRO")
    @Vertical(name = "Core")
    @Test
    public void testHomePageVisitDeeplinkNonUtmGuest() {
        Map<String, String> testProperties = getParam("testHomePageVisitDeeplinkNonUtmGuest");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String deeplink = "tiket://m.tiket.com";

        String event = testProperties.get("event");
        String eventAction = testProperties.get("eventAction");
        String screenName = testProperties.get("screenName");
        String utmPage = testProperties.get("utmPage");
        String isFirstPage = testProperties.get("isFirstPage");

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

        step("Open home page via deeplink");
        analyticsHomePage.openDeeplink(deeplink);

        sleep(40);

        BaseAssertions homePageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("utmAttributedAt is not null", homePageAssertion.verifyUserProperty(Configs.MESSAGES, "utmAttributedAt"), is(true));
        Assertion.assertThat("utmCampaign is correct", homePageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmCampaign", utmCampaign), is(true));
        Assertion.assertThat("utmContent is correct", homePageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmContent", utmContent), is(true));
        Assertion.assertThat("utmExternal is correct", homePageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmExternal", utmExternal), is(true));
        Assertion.assertThat("utmId is correct", homePageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmId", utmId), is(true));
        Assertion.assertThat("utmMedium is correct", homePageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmMedium", utmMedium), is(true));
        Assertion.assertThat("utmSource is correct", homePageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmSource", utmSource), is(true));
        Assertion.assertThat("utmTerm is correct", homePageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmTerm", utmTerm), is(true));
        Assertion.assertThat("userId is null", homePageAssertion.verifyUserProperty(Configs.MESSAGES, "userId"), is(false));

        Assertion.assertThat("pageVisit event is found", homePageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, screenName), greaterThan(0));
        Assertion.assertThat("eventAction is correct", homePageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), is(true));
        Assertion.assertThat("screenName is correct", homePageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), is(true));
        Assertion.assertThat("utmPage is correct", homePageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "utmPage", utmPage), is(true));
        Assertion.assertThat("isFirstPage is correct", homePageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "isFirstPage", isFirstPage), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "PromoMainPage")
    @Tribe(name = "PRO")
    @Vertical(name = "Core")
    @Test
    public void testHomePageVisitDeeplinkNonUtmLogin() {
        Map<String, String> testProperties = getParam("testHomePageVisitDeeplinkNonUtmLogin");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String loginType = testProperties.get("loginType");
        String email = testProperties.get("email");
        String password = testProperties.get("password");
        String deeplink = "tiket://m.tiket.com";

        String event = testProperties.get("event");
        String eventAction = testProperties.get("eventAction");
        String screenName = testProperties.get("screenName");
        String utmPage = testProperties.get("utmPage");
        String isFirstPage = testProperties.get("isFirstPage");

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

        step("Open home page via deeplink");
        analyticsHomePage.openDeeplink(deeplink);

        sleep(40);

        BaseAssertions homePageAssertion = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("utmAttributedAt is not null", homePageAssertion.verifyUserProperty(Configs.MESSAGES, "utmAttributedAt"), is(true));
        Assertion.assertThat("utmCampaign is correct", homePageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmCampaign", utmCampaign), is(true));
        Assertion.assertThat("utmContent is correct", homePageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmContent", utmContent), is(true));
        Assertion.assertThat("utmExternal is correct", homePageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmExternal", utmExternal), is(true));
        Assertion.assertThat("utmId is correct", homePageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmId", utmId), is(true));
        Assertion.assertThat("utmMedium is correct", homePageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmMedium", utmMedium), is(true));
        Assertion.assertThat("utmSource is correct", homePageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmSource", utmSource), is(true));
        Assertion.assertThat("utmTerm is correct", homePageAssertion.haveCorrectUserProperty(Configs.MESSAGES, "utmTerm", utmTerm), is(true));
        Assertion.assertThat("userId is not null", homePageAssertion.verifyUserProperty(Configs.MESSAGES, "userId"), is(true));

        Assertion.assertThat("pageVisit event is found", homePageAssertion.verifyFilteredEvent(Configs.MESSAGES, event, screenName), greaterThan(0));
        Assertion.assertThat("eventAction is correct", homePageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), is(true));
        Assertion.assertThat("screenName is correct", homePageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), is(true));
        Assertion.assertThat("utmPage is correct", homePageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "utmPage", utmPage), is(true));
        Assertion.assertThat("isFirstPage is correct", homePageAssertion.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "isFirstPage", isFirstPage), is(true));
    }
}