package com.tiket.analytics.web.test.bus;

import com.tiket.analytics.BaseAnalyticsTest;
import com.tiket.analytics.assertions.BaseAssertions;
import com.tiket.analytics.web.page.AnalyticsUrlPageWeb;
import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.testbase.Configs;
import com.tiket.web.page.common.home.HomePageWeb;
import com.tiket.web.page.common.home.LoginPageWeb;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import javax.security.auth.login.FailedLoginException;
import java.util.Map;

import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.core.Is.is;

public class BusPageTest extends BaseAnalyticsTest {

    @Author(names = {"Okta"})
    @Module(name = "BusPDP")
    @Tribe(name = "Bus")
    @Vertical(name = "Core")
    @Test
    public void testBusPDPEnNonUtmGuest() {
        Map<String, String> testProperties = getParam("testBusPDPEnNonUtmGuest");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String origin = testProperties.get("origin");
        String destination = testProperties.get("destination");
        int daysToDepartureDate = Integer.parseInt(testProperties.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentDateYYYY_MM_DD();
        String departureDate = CoreFunctions.addToDateYYYY_MM_DD(currentDate, daysToDepartureDate);

        String utmCampaign, utmContent, utmExternal, utmId, utmMedium, utmSource, utmTerm;
        utmCampaign = utmContent = utmId = utmMedium = utmSource = utmTerm = "none";
        utmExternal = "organic";
        String language = "EN";

        String path = "bus-travel/detail?origin=" + origin + "&destination=" + destination + "&departureDate=" + departureDate + "&returnDate=" + departureDate + "&adult=1&tripType=oneway&journeyType=depart";

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        AnalyticsUrlPageWeb<?> analyticsUrlPage = AnalyticsUrlPageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as guest");
        loginPage.loginAsGuest();

        step("Select english language if not in english");
        homePage.selectEnglishLanguageOnly();

        step("Open bus PDP EN");
        analyticsUrlPage.openUrl(path);
        homePage.waitForWebPageToLoad();

        sleep(ANALYTICS_WAIT_SECONDS);

        BaseAssertions analyticsAssertions = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("utmAttributedAt is not null", analyticsAssertions.verifyUserProperty(Configs.MESSAGES, "utmAttributedAt"), is(true));
        Assertion.assertThat("utmSource is none", analyticsAssertions.haveCorrectUserProperty(Configs.MESSAGES, "utmSource", utmSource), is(true));
        Assertion.assertThat("utmMedium is none", analyticsAssertions.haveCorrectUserProperty(Configs.MESSAGES, "utmMedium", utmMedium), is(true));
        Assertion.assertThat("utmCampaign is none", analyticsAssertions.haveCorrectUserProperty(Configs.MESSAGES, "utmCampaign", utmCampaign), is(true));
        Assertion.assertThat("utmId is none", analyticsAssertions.haveCorrectUserProperty(Configs.MESSAGES, "utmId", utmId), is(true));
        Assertion.assertThat("utmTerm is none", analyticsAssertions.haveCorrectUserProperty(Configs.MESSAGES, "utmTerm", utmTerm), is(true));
        Assertion.assertThat("utmContent is none", analyticsAssertions.haveCorrectUserProperty(Configs.MESSAGES, "utmContent", utmContent), is(true));
        Assertion.assertThat("utmExternal is organic", analyticsAssertions.haveCorrectUserProperty(Configs.MESSAGES, "utmExternal", utmExternal), is(true));
        Assertion.assertThat("userId is null", analyticsAssertions.verifyUserProperty(Configs.MESSAGES, "userId"), is(false));
        Assertion.assertThat("language is en", analyticsAssertions.haveCorrectUserProperty(Configs.MESSAGES, "language", language), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "BusPDP")
    @Tribe(name = "Bus")
    @Vertical(name = "Core")
    @Test
    public void testBusPDPEnNonUtmLogin() throws FailedLoginException, InterruptedException {
        Map<String, String> testProperties = getParam("testBusPDPEnNonUtmLogin");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String username = testProperties.get("username");
        String password = testProperties.get("password");
        String origin = testProperties.get("origin");
        String destination = testProperties.get("destination");
        int daysToDepartureDate = Integer.parseInt(testProperties.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentDateYYYY_MM_DD();
        String departureDate = CoreFunctions.addToDateYYYY_MM_DD(currentDate, daysToDepartureDate);

        String utmCampaign, utmContent, utmExternal, utmId, utmMedium, utmSource, utmTerm;
        utmCampaign = utmContent = utmId = utmMedium = utmSource = utmTerm = "none";
        utmExternal = "organic";
        String language = "EN";

        String path = "bus-travel/detail?origin=" + origin + "&destination=" + destination + "&departureDate=" + departureDate + "&returnDate=" + departureDate + "&adult=1&tripType=oneway&journeyType=depart";

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        AnalyticsUrlPageWeb<?> analyticsUrlPage = AnalyticsUrlPageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("Login as " + username + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(username, password);

        step("Select english language if not in english");
        homePage.selectEnglishLanguageOnly();

        step("Open bus PDP EN");
        analyticsUrlPage.openUrl(path);
        homePage.waitForWebPageToLoad();

        sleep(ANALYTICS_WAIT_SECONDS);

        BaseAssertions analyticsAssertions = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("utmAttributedAt is not null", analyticsAssertions.verifyUserProperty(Configs.MESSAGES, "utmAttributedAt"), is(true));
        Assertion.assertThat("utmSource is none", analyticsAssertions.haveCorrectUserProperty(Configs.MESSAGES, "utmSource", utmSource), is(true));
        Assertion.assertThat("utmMedium is none", analyticsAssertions.haveCorrectUserProperty(Configs.MESSAGES, "utmMedium", utmMedium), is(true));
        Assertion.assertThat("utmCampaign is none", analyticsAssertions.haveCorrectUserProperty(Configs.MESSAGES, "utmCampaign", utmCampaign), is(true));
        Assertion.assertThat("utmId is none", analyticsAssertions.haveCorrectUserProperty(Configs.MESSAGES, "utmId", utmId), is(true));
        Assertion.assertThat("utmTerm is none", analyticsAssertions.haveCorrectUserProperty(Configs.MESSAGES, "utmTerm", utmTerm), is(true));
        Assertion.assertThat("utmContent is none", analyticsAssertions.haveCorrectUserProperty(Configs.MESSAGES, "utmContent", utmContent), is(true));
        Assertion.assertThat("utmExternal is organic", analyticsAssertions.haveCorrectUserProperty(Configs.MESSAGES, "utmExternal", utmExternal), is(true));
        Assertion.assertThat("userId is not null", analyticsAssertions.verifyUserProperty(Configs.MESSAGES, "userId"), is(true));
        Assertion.assertThat("language is en", analyticsAssertions.haveCorrectUserProperty(Configs.MESSAGES, "language", language), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "BusPDP")
    @Tribe(name = "Bus")
    @Vertical(name = "Core")
    @Test
    public void testBusPDPEnUtmGuest() {
        Map<String, String> testProperties = getParam("testBusPDPEnUtmGuest");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String origin = testProperties.get("origin");
        String destination = testProperties.get("destination");
        int daysToDepartureDate = Integer.parseInt(testProperties.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentDateYYYY_MM_DD();
        String departureDate = CoreFunctions.addToDateYYYY_MM_DD(currentDate, daysToDepartureDate);

        String utmCampaign = testProperties.get("utmCampaign");
        String utmContent = testProperties.get("utmContent");
        String utmExternal = "nonOrganic";
        String utmId = testProperties.get("utmId");
        String utmMedium = testProperties.get("utmMedium");
        String utmSource = testProperties.get("utmSource");
        String utmTerm = testProperties.get("utmTerm");
        String language = "EN";

        String path = "bus-travel/detail?origin=" + origin + "&destination=" + destination + "&departureDate=" + departureDate + "&returnDate=" + departureDate + "&adult=1&tripType=oneway&journeyType=depart&";

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        AnalyticsUrlPageWeb<?> analyticsUrlPage = AnalyticsUrlPageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as guest");
        loginPage.loginAsGuest();

        step("Select english language if not in english");
        homePage.selectEnglishLanguageOnly();

        step("Open bus PDP EN");
        String utmProperties = analyticsUrlPage.getUtmProperties(utmCampaign, utmContent, utmId, utmMedium, utmSource, utmTerm);
        analyticsUrlPage.openUrl(path + utmProperties);
        homePage.waitForWebPageToLoad();

        sleep(ANALYTICS_WAIT_SECONDS);

        BaseAssertions analyticsAssertions = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("utmAttributedAt is not null", analyticsAssertions.verifyUserProperty(Configs.MESSAGES, "utmAttributedAt"), is(true));
        Assertion.assertThat("utmCampaign is correct", analyticsAssertions.haveCorrectUserProperty(Configs.MESSAGES, "utmCampaign", utmCampaign), is(true));
        Assertion.assertThat("utmContent is correct", analyticsAssertions.haveCorrectUserProperty(Configs.MESSAGES, "utmContent", utmContent), is(true));
        Assertion.assertThat("utmExternal is correct", analyticsAssertions.haveCorrectUserProperty(Configs.MESSAGES, "utmExternal", utmExternal), is(true));
        Assertion.assertThat("utmId is correct", analyticsAssertions.haveCorrectUserProperty(Configs.MESSAGES, "utmId", utmId), is(true));
        Assertion.assertThat("utmMedium is correct", analyticsAssertions.haveCorrectUserProperty(Configs.MESSAGES, "utmMedium", utmMedium), is(true));
        Assertion.assertThat("utmSource is correct", analyticsAssertions.haveCorrectUserProperty(Configs.MESSAGES, "utmSource", utmSource), is(true));
        Assertion.assertThat("utmTerm is correct", analyticsAssertions.haveCorrectUserProperty(Configs.MESSAGES, "utmTerm", utmTerm), is(true));
        Assertion.assertThat("userId is null", analyticsAssertions.verifyUserProperty(Configs.MESSAGES, "userId"), is(false));
        Assertion.assertThat("language is en", analyticsAssertions.haveCorrectUserProperty(Configs.MESSAGES, "language", language), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "BusPDP")
    @Tribe(name = "Bus")
    @Vertical(name = "Core")
    @Test
    public void testBusPDPEnUtmLogin() throws FailedLoginException, InterruptedException {
        Map<String, String> testProperties = getParam("testBusPDPEnUtmLogin");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String username = testProperties.get("username");
        String password = testProperties.get("password");
        String origin = testProperties.get("origin");
        String destination = testProperties.get("destination");
        int daysToDepartureDate = Integer.parseInt(testProperties.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentDateYYYY_MM_DD();
        String departureDate = CoreFunctions.addToDateYYYY_MM_DD(currentDate, daysToDepartureDate);

        String utmCampaign = testProperties.get("utmCampaign");
        String utmContent = testProperties.get("utmContent");
        String utmExternal = "nonOrganic";
        String utmId = testProperties.get("utmId");
        String utmMedium = testProperties.get("utmMedium");
        String utmSource = testProperties.get("utmSource");
        String utmTerm = testProperties.get("utmTerm");
        String language = "EN";

        String path = "bus-travel/detail?origin=" + origin + "&destination=" + destination + "&departureDate=" + departureDate + "&returnDate=" + departureDate + "&adult=1&tripType=oneway&journeyType=depart&";

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        AnalyticsUrlPageWeb<?> analyticsUrlPage = AnalyticsUrlPageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("Login as " + username + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(username, password);

        step("Select english language if not in english");
        homePage.selectEnglishLanguageOnly();

        step("Open bus PDP EN");
        String utmProperties = analyticsUrlPage.getUtmProperties(utmCampaign, utmContent, utmId, utmMedium, utmSource, utmTerm);
        analyticsUrlPage.openUrl(path + utmProperties);
        homePage.waitForWebPageToLoad();

        sleep(ANALYTICS_WAIT_SECONDS);

        BaseAssertions analyticsAssertions = new BaseAssertions(mainLogger.get());

        Assertion.assertThat("utmAttributedAt is not null", analyticsAssertions.verifyUserProperty(Configs.MESSAGES, "utmAttributedAt"), is(true));
        Assertion.assertThat("utmCampaign is correct", analyticsAssertions.haveCorrectUserProperty(Configs.MESSAGES, "utmCampaign", utmCampaign), is(true));
        Assertion.assertThat("utmContent is correct", analyticsAssertions.haveCorrectUserProperty(Configs.MESSAGES, "utmContent", utmContent), is(true));
        Assertion.assertThat("utmExternal is correct", analyticsAssertions.haveCorrectUserProperty(Configs.MESSAGES, "utmExternal", utmExternal), is(true));
        Assertion.assertThat("utmId is correct", analyticsAssertions.haveCorrectUserProperty(Configs.MESSAGES, "utmId", utmId), is(true));
        Assertion.assertThat("utmMedium is correct", analyticsAssertions.haveCorrectUserProperty(Configs.MESSAGES, "utmMedium", utmMedium), is(true));
        Assertion.assertThat("utmSource is correct", analyticsAssertions.haveCorrectUserProperty(Configs.MESSAGES, "utmSource", utmSource), is(true));
        Assertion.assertThat("utmTerm is correct", analyticsAssertions.haveCorrectUserProperty(Configs.MESSAGES, "utmTerm", utmTerm), is(true));
        Assertion.assertThat("userId is not null", analyticsAssertions.verifyUserProperty(Configs.MESSAGES, "userId"), is(true));
        Assertion.assertThat("language is en", analyticsAssertions.haveCorrectUserProperty(Configs.MESSAGES, "language", language), is(true));
    }
}