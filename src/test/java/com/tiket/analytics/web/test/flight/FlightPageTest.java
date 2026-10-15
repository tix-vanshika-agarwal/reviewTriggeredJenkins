package com.tiket.analytics.web.test.flight;

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
import com.tiket.web.page.flight.landingpage.FlightLandingPageWeb;
import com.tiket.web.page.flight.searchresultpage.FlightSearchResultPageWeb;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import javax.security.auth.login.FailedLoginException;
import java.util.Map;

import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.core.Is.is;

public class FlightPageTest extends BaseAnalyticsTest {

    private static final String UTM_VALUE_NONE = "none";
    private static final String UTM_VALUE_ORGANIC = "organic";
    private static final String UTM_VALUE_NON_ORGANIC = "nonOrganic";
    private static final String LANGUAGE_EN = "EN";
    private static final String FLIGHT_LANDING_PAGE_ENDPOINT = "pesawat";
    private static final String FLIGHT_LANDING_PAGE_EN_ENDPOINT = "en-sg/flights";
    
    @Author(names = {"Okta"})
    @Module(name = "FlightMainPage")
    @Tribe(name = "Flight")
    @Vertical(name = "Core")
    @Test
    public void testFlightMainPageEnNonUtmGuest() {
        Map<String, String> testProperties = getParam("testFlightMainPageEnNonUtmGuest");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String utmCampaign, utmContent, utmExternal, utmId, utmMedium, utmSource, utmTerm;
        utmCampaign = utmContent = utmId = utmMedium = utmSource = utmTerm = UTM_VALUE_NONE;
        utmExternal = UTM_VALUE_ORGANIC;
        String language = LANGUAGE_EN;

        String path = FLIGHT_LANDING_PAGE_ENDPOINT;

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        AnalyticsUrlPageWeb<?> analyticsUrlPage = AnalyticsUrlPageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as guest");
        loginPage.loginAsGuest();

        step("Select english language if not in english");
        homePage.selectEnglishLanguageOnly();

        step("Open flight main page EN");
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
    @Module(name = "FlightMainPage")
    @Tribe(name = "Flight")
    @Vertical(name = "Core")
    @Test
    public void testFlightMainPageEnNonUtmLogin() throws FailedLoginException, InterruptedException {
        Map<String, String> testProperties = getParam("testFlightMainPageEnNonUtmLogin");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String username = testProperties.get("username");
        String password = testProperties.get("password");
        String utmCampaign, utmContent, utmExternal, utmId, utmMedium, utmSource, utmTerm;
        utmCampaign = utmContent = utmId = utmMedium = utmSource = utmTerm = UTM_VALUE_NONE;
        utmExternal = UTM_VALUE_ORGANIC;
        String language = LANGUAGE_EN;

        String path = FLIGHT_LANDING_PAGE_ENDPOINT;

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        AnalyticsUrlPageWeb<?> analyticsUrlPage = AnalyticsUrlPageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("Login as " + username + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(username, password);

        step("Select english language if not in english");
        homePage.selectEnglishLanguageOnly();

        step("Open flight main page EN");
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
    @Module(name = "FlightMainPage")
    @Tribe(name = "Flight")
    @Vertical(name = "Core")
    @Test
    public void testFlightMainPageEnUtmGuest() {
        Map<String, String> testProperties = getParam("testFlightMainPageEnUtmGuest");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String utmCampaign = testProperties.get("utmCampaign");
        String utmContent = testProperties.get("utmContent");
        String utmExternal = UTM_VALUE_NON_ORGANIC;
        String utmId = testProperties.get("utmId");
        String utmMedium = testProperties.get("utmMedium");
        String utmSource = testProperties.get("utmSource");
        String utmTerm = testProperties.get("utmTerm");
        String language = LANGUAGE_EN;

        String path = FLIGHT_LANDING_PAGE_ENDPOINT + "?";

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        AnalyticsUrlPageWeb<?> analyticsUrlPage = AnalyticsUrlPageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as guest");
        loginPage.loginAsGuest();

        step("Select english language if not in english");
        homePage.selectEnglishLanguageOnly();

        step("Open flight main page EN");
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
    @Module(name = "FlightMainPage")
    @Tribe(name = "Flight")
    @Vertical(name = "Core")
    @Test
    public void testFlightMainPageEnUtmLogin() throws FailedLoginException, InterruptedException {
        Map<String, String> testProperties = getParam("testFlightMainPageEnUtmLogin");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String username = testProperties.get("username");
        String password = testProperties.get("password");
        String utmCampaign = testProperties.get("utmCampaign");
        String utmContent = testProperties.get("utmContent");
        String utmExternal = UTM_VALUE_NON_ORGANIC;
        String utmId = testProperties.get("utmId");
        String utmMedium = testProperties.get("utmMedium");
        String utmSource = testProperties.get("utmSource");
        String utmTerm = testProperties.get("utmTerm");
        String language = LANGUAGE_EN;

        String path = FLIGHT_LANDING_PAGE_ENDPOINT + "?";

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        AnalyticsUrlPageWeb<?> analyticsUrlPage = AnalyticsUrlPageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("Login as " + username + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(username, password);

        step("Select english language if not in english");
        homePage.selectEnglishLanguageOnly();

        step("Open flight main page EN");
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

    @Author(names = {"Ashish"})
    @Module(name = "FlightSRP")
    @Tribe(name = "Flight")
    @Vertical(name = "Core")
    @Test
    public void verifySearchLoadedEventPropertiesOnSrp() {
        Map<String, String> testProperties = getParam("verifySearchLoadedEventPropertiesOnSrp");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String tripType = testProperties.get("tripType");
        String originAirport = testProperties.get("originAirport");
        String destinationAirport = testProperties.get("destinationAirport");
        String originCity = testProperties.get("originCity");
        String destinationCity = testProperties.get("destinationCity");
        int daysToDepartureDate = Integer.parseInt(testProperties.get("daysToDepartureDate"));
        int adult = Integer.parseInt(testProperties.get("adult"));
        int child = Integer.parseInt(testProperties.get("child"));
        int infant = Integer.parseInt(testProperties.get("infant"));
        String seatClass = testProperties.get("seatClass");
        String passenger = testProperties.get("passenger");
        String roundTrip = testProperties.get("roundTrip");
        String type = testProperties.get("type");
        String journey = testProperties.get("journey");

        String event = testProperties.get("event");
        String eventCategory = testProperties.get("eventCategory");
        String eventLabel = testProperties.get("eventLabel");
        String screenName = testProperties.get("screenName");
        String vertical = testProperties.get("vertical");
        String eventAction = testProperties.get("eventAction");

        // Use Jakarta date as the single source for calendar selection and expected Mixpanel startDate
        String startDate = CoreFunctions.getJakartaDateYYYY_MM_DD(daysToDepartureDate);
        String departureDate = CoreFunctions.convertToLongDate(startDate);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        AnalyticsUrlPageWeb<?> analyticsUrlPage = AnalyticsUrlPageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPage = FlightLandingPageWeb.getInstance(session);

        step("User open English home page");
        analyticsUrlPage.openHomePageEn();
        homePage.waitForWebPageToLoad();

        step("User open as guest");
        loginPage.loginAsGuest();

        step("Open flight landing page");
        analyticsUrlPage.openUrl(FLIGHT_LANDING_PAGE_EN_ENDPOINT);
        homePage.waitForWebPageToLoad();
        flightLandingPage.handleGotItPopup();
        Assertion.assertThat("Flight Landing Page is Opened", flightLandingPage.isOnFlightPage(), is(true));

        step("Search for flight");
        flightLandingPage.toggleTripType(tripType);
        flightLandingPage.selectOrigin(originAirport);
        flightLandingPage.handleGotItPopup();
        flightLandingPage.selectDestination(destinationAirport);
        flightLandingPage.handleGotItPopup();
        flightLandingPage.selectDate(departureDate);
        flightLandingPage.addPassenger(adult, child, infant);
        flightLandingPage.selectFlightClass(seatClass);
        flightLandingPage.searchFlightAndWaitForSrp();

        step("User redirected to SRP and search load completes");
        String currentUrl = session.driver().getCurrentUrl();
        Assertion.assertThat("Search Result Page URL is loaded",
                currentUrl.contains("/flights/search") || currentUrl.contains("/pesawat/search"), is(true));
        FlightSearchResultPageWeb<?> searchResultPage = FlightSearchResultPageWeb.getInstance(session);
        Assertion.assertThat("SRP has flight results", !searchResultPage.hasNoFlightCards(), is(true));

        sleep(ANALYTICS_WAIT_SECONDS);

        BaseAssertions analyticsAssertions = new BaseAssertions(mainLogger.get());

        step("Verify searchLoaded event tracker properties");
        Assertion.assertThat("searchLoaded event is found", analyticsAssertions.verifyFilteredEvent(Configs.MESSAGES, event, screenName), is(1));
        Assertion.assertThat("eventCategory is correct", analyticsAssertions.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventCategory", eventCategory), is(true));
        Assertion.assertThat("eventLabel is correct", analyticsAssertions.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventLabel", eventLabel), is(true));
        Assertion.assertThat("screenName is correct", analyticsAssertions.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), is(true));
        Assertion.assertThat("vertical is correct", analyticsAssertions.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "vertical", vertical), is(true));
        Assertion.assertThat("startDate is correct", analyticsAssertions.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "startDate", startDate), is(true));
        Assertion.assertThat("originAirport is correct", analyticsAssertions.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "originAirport", originAirport), is(true));
        Assertion.assertThat("destinationAirport is correct", analyticsAssertions.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "destinationAirport", destinationAirport), is(true));
        Assertion.assertThat("originCity is correct", analyticsAssertions.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "originCity", originCity), is(true));
        Assertion.assertThat("destinationCity is correct", analyticsAssertions.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "destinationCity", destinationCity), is(true));
        Assertion.assertThat("roundTrip is correct", analyticsAssertions.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "roundTrip", roundTrip), is(true));
        Assertion.assertThat("passenger is correct", analyticsAssertions.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "passenger", passenger), is(true));
        Assertion.assertThat("infant is correct", analyticsAssertions.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "infant", String.valueOf(infant)), is(true));
        Assertion.assertThat("child is correct", analyticsAssertions.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "child", String.valueOf(child)), is(true));
        Assertion.assertThat("adult is correct", analyticsAssertions.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "adult", String.valueOf(adult)), is(true));
        Assertion.assertThat("seatClass is correct", analyticsAssertions.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "seatClass", seatClass), is(true));
        Assertion.assertThat("type is correct", analyticsAssertions.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "type", type), is(true));
        Assertion.assertThat("journey is correct", analyticsAssertions.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "journey", journey), is(true));
        Assertion.assertThat("eventAction is correct", analyticsAssertions.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), is(true));
    }

    @Author(names = {"Ashish"})
    @Module(name = "FlightSRP")
    @Tribe(name = "Flight")
    @Vertical(name = "Core")
    @Test
    public void verifyFlightErrorNoResultEventPropertiesOnSrp() {
        Map<String, String> testProperties = getParam("verifyFlightErrorNoResultEventPropertiesOnSrp");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String tripType = testProperties.get("tripType");
        String originAirport = testProperties.get("originAirport");
        String destinationAirport = testProperties.get("destinationAirport");
        String originCity = testProperties.get("originCity");
        String destinationCity = testProperties.get("destinationCity");
        int daysToDepartureDate = Integer.parseInt(testProperties.get("daysToDepartureDate"));
        int adult = Integer.parseInt(testProperties.get("adult"));
        int child = Integer.parseInt(testProperties.get("child"));
        int infant = Integer.parseInt(testProperties.get("infant"));
        String seatClass = testProperties.get("seatClass");
        String passenger = testProperties.get("passenger");
        String roundTrip = testProperties.get("roundTrip");
        String journey = testProperties.get("journey");
        String originCitySearch = testProperties.get("originCitySearch");
        String destinationCitySearch = testProperties.get("destinationCitySearch");
        String destinationKeyword = testProperties.get("destinationKeyword");

        String event = testProperties.get("event");
        String eventCategory = testProperties.get("eventCategory");
        String eventLabel = testProperties.get("eventLabel");
        String screenName = testProperties.get("screenName");
        String vertical = testProperties.get("vertical");
        String eventAction = testProperties.get("eventAction");

        // Use Jakarta date as the single source for calendar selection and expected Mixpanel startDate
        String startDate = CoreFunctions.getJakartaDateYYYY_MM_DD(daysToDepartureDate);
        String departureDate = CoreFunctions.convertToLongDate(startDate);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        AnalyticsUrlPageWeb<?> analyticsUrlPage = AnalyticsUrlPageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPage = FlightLandingPageWeb.getInstance(session);
        FlightSearchResultPageWeb<?> searchResultPage = FlightSearchResultPageWeb.getInstance(session);

        step("User open English home page");
        analyticsUrlPage.openHomePageEn();
        homePage.waitForWebPageToLoad();

        step("User open as guest");
        loginPage.loginAsGuest();

        step("Open flight landing page");
        analyticsUrlPage.openUrl(FLIGHT_LANDING_PAGE_EN_ENDPOINT);
        homePage.waitForWebPageToLoad();
        flightLandingPage.handleGotItPopup();
        Assertion.assertThat("Flight Landing Page is Opened", flightLandingPage.isOnFlightPage(), is(true));

        step("Search route that most likely returns empty SRP");
        flightLandingPage.toggleTripType(tripType);
        flightLandingPage.selectOrigin(originAirport);
        flightLandingPage.handleGotItPopup();
        flightLandingPage.selectDestination(destinationAirport);
        flightLandingPage.handleGotItPopup();
        flightLandingPage.selectDate(departureDate);
        flightLandingPage.addPassenger(adult, child, infant);
        flightLandingPage.selectFlightClass(seatClass);
        flightLandingPage.searchFlightAndWaitForEmptySrp();

        step("User arrives on SRP with empty response");
        String currentUrl = session.driver().getCurrentUrl();
        Assertion.assertThat("Search Result Page URL is loaded",
                currentUrl.contains("/flights/search") || currentUrl.contains("/pesawat/search"), is(true));
        Assertion.assertThat("SRP has empty flight results", searchResultPage.hasNoFlightCards(), is(true));

        sleep(ANALYTICS_WAIT_SECONDS);

        BaseAssertions analyticsAssertions = new BaseAssertions(mainLogger.get());

        step("Verify flightError noResult tracker properties");
        Assertion.assertThat("flightError event is found", analyticsAssertions.verifyFilteredEvent(Configs.MESSAGES, event, screenName), is(1));
        Assertion.assertThat("eventCategory is correct", analyticsAssertions.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventCategory", eventCategory), is(true));
        Assertion.assertThat("eventLabel is correct", analyticsAssertions.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventLabel", eventLabel), is(true));
        // FromProdBug: screenName was empty/missing on dWeb & mWeb — must be flightSearchResult
        Assertion.assertThat("screenName is correct", analyticsAssertions.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "screenName", screenName), is(true));
        // noResult description varies (flightNotFound vs supplierLatency) — assert present
        Assertion.assertThat("eventDescription is present", analyticsAssertions.verifyEventProperty(Configs.MESSAGES, event, screenName, "eventDescription"), is(true));
        Assertion.assertThat("vertical is correct", analyticsAssertions.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "vertical", vertical), is(true));
        Assertion.assertThat("eventAction is correct", analyticsAssertions.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "eventAction", eventAction), is(true));
        Assertion.assertThat("startDate is correct", analyticsAssertions.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "startDate", startDate), is(true));
        Assertion.assertThat("originAirport is correct", analyticsAssertions.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "originAirport", originAirport), is(true));
        Assertion.assertThat("destinationAirport is correct", analyticsAssertions.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "destinationAirport", destinationAirport), is(true));
        Assertion.assertThat("originCity is correct", analyticsAssertions.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "originCity", originCity), is(true));
        Assertion.assertThat("destinationCity is correct", analyticsAssertions.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "destinationCity", destinationCity), is(true));
        Assertion.assertThat("roundTrip is correct", analyticsAssertions.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "roundTrip", roundTrip), is(true));
        Assertion.assertThat("passenger is correct", analyticsAssertions.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "passenger", passenger), is(true));
        Assertion.assertThat("infant is correct", analyticsAssertions.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "infant", String.valueOf(infant)), is(true));
        Assertion.assertThat("child is correct", analyticsAssertions.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "child", String.valueOf(child)), is(true));
        Assertion.assertThat("adult is correct", analyticsAssertions.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "adult", String.valueOf(adult)), is(true));
        Assertion.assertThat("seatClass is correct", analyticsAssertions.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "seatClass", seatClass), is(true));
        Assertion.assertThat("journey is correct", analyticsAssertions.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "journey", journey), is(true));
        Assertion.assertThat("originCitySearch is correct", analyticsAssertions.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "originCitySearch", originCitySearch), is(true));
        Assertion.assertThat("destinationCitySearch is correct", analyticsAssertions.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "destinationCitySearch", destinationCitySearch), is(true));
        Assertion.assertThat("departKeyword is present", analyticsAssertions.verifyEventProperty(Configs.MESSAGES, event, screenName, "departKeyword"), is(true));
        Assertion.assertThat("destinationKeyword is correct", analyticsAssertions.filteredEventHaveCorrectEventProperty(Configs.MESSAGES, event, screenName, "destinationKeyword", destinationKeyword), is(true));
        // empty noResult payload typically only keeps calendar price; other sample price/id fields are omitted
        Assertion.assertThat("departCalendarPrice is present", analyticsAssertions.verifyEventProperty(Configs.MESSAGES, event, screenName, "departCalendarPrice"), is(true));
    }
}