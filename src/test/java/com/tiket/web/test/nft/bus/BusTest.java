package com.tiket.web.test.nft.bus;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Environment;
import com.tiket.model.Platform;
import com.tiket.model.Session;
import com.tiket.pojo.bus.BusSrpOneWayData;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.testbase.Configs;
import com.tiket.web.WebTest;
import com.tiket.web.page.common.calendar.CalendarPageWeb;
import com.tiket.web.page.nft.bus.bookingForm_Page.BusBookingFormPageWeb;
import com.tiket.web.page.nft.bus.productDetail_Page.BusPdpPageWeb;
import com.tiket.web.page.nft.bus.searchResult_Page.BusSearchResultPageWeb;
import com.tiket.web.page.order.OrderDetail.OrderDetailWeb;
import com.tiket.web.page.order.OrderList.OrderListWeb;
import com.tiket.web.dWeb.page.payment.PaymentPage;
import com.tiket.web.page.common.home.HomePageWeb;
import com.tiket.web.page.common.home.LoginPageWeb;
import com.tiket.web.page.nft.bus.search_Page.BusSearchPageWeb;
import org.testng.annotations.Test;

import javax.security.auth.login.FailedLoginException;
import java.util.Map;

import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.core.Is.is;

public class BusTest extends WebTest {
    @Author(names = "Okta")
    @Module(name = "HomePageWeb")
    @Tribe(name = "Bus")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyBusIsDisplayedNonIDRGuestWeb() {
        Map<String, String> param = getParam("testVerifyBusIsDisplayedNonIDRGuestWeb");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String currency = param.get("currency");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as guest");
        loginPage.loginAsGuest();

        step("User select " + currency + " currency");
        homePage.selectCurrency(currency);

        step("User is redirected back to home page");
        Assertion.assertThat("Verify bus is displayed on home page", homePage.verifyVerticalIsDisplayed("Bus"), is(true));
    }

    @Author(names = "Okta")
    @Module(name = "HomePageWeb")
    @Tribe(name = "Bus")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyBusIsDisplayedNonIDRLoginWeb() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testVerifyBusIsDisplayedNonIDRLoginWeb");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String currency = param.get("currency");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(username, password, otp, retryCounter);

        step("User select " + currency + " currency");
        homePage.selectCurrency(currency);

        step("User is redirected back to home page");
        Assertion.assertThat("Verify bus is displayed on home page", homePage.verifyVerticalIsDisplayed("Bus"), is(true));
    }

    @Author(names = "Okta")
    @Module(name = "SearchPage")
    @Tribe(name = "Bus")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyBusOriginLocationGuest() {
        Map<String, String> param = getParam("testVerifyBusOriginLocationGuest");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String origin = param.get("origin");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        BusSearchPageWeb<?> busSearchPage = BusSearchPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as guest");
        loginPage.loginAsGuest();

        step("User select IDR currency");
        homePage.selectCurrency("IDR");

        step("User open bus vertical");
        homePage.openVerticalPageViaURL("bus-travel");

        step("User click skip button");
        busSearchPage.clickSkipButton();

        step("User select origin location");
        busSearchPage.clickOriginLocationInput();
        busSearchPage.selectLocation(origin);
        Assertion.assertThat("Verify origin location is selected", busSearchPage.isLocationSelected(origin), is(true));
    }

    @Author(names = {"Okta", "Eka"})
    @Module(name = "SearchPage")
    @Tribe(name = "Bus")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyBusOriginLocationLoginWeb() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testVerifyBusOriginLocationLoginWeb");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String origin = param.get("origin");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        BusSearchPageWeb<?> busSearchPage = BusSearchPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(username, password, otp, retryCounter);

        step("User open bus vertical");
        homePage.openVerticalPageViaURL("bus-travel");

        step("User click skip button");
        busSearchPage.clickSkipButton();

        step("User select origin location");
        busSearchPage.clickOriginLocationInput();
        busSearchPage.selectLocation(origin);
        Assertion.assertThat("Verify origin location is selected", busSearchPage.isLocationSelected(origin), is(true));
    }

    @Author(names = {"Okta", "Eka"})
    @Module(name = "SearchPage")
    @Tribe(name = "Bus")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyBusDestinationLocationGuest() throws InterruptedException {
        Map<String, String> param = getParam("testVerifyBusDestinationLocationGuest");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String destination = param.get("destination");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        BusSearchPageWeb<?> busSearchPage = BusSearchPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as guest");
        loginPage.loginAsGuest();

        step("User open bus vertical");
        homePage.openVerticalPageViaURL("bus-travel");

        step("User click skip button");
        busSearchPage.clickSkipButton();

        step("User select destination location");
        busSearchPage.clickDestinationLocationInput();
        busSearchPage.selectLocation(destination);
        Assertion.assertThat("Verify destination location is selected", busSearchPage.isLocationSelected(destination), is(true));
    }

    @Author(names = "Okta")
    @Module(name = "SearchPage")
    @Tribe(name = "Bus")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyBusDestinationLocationLoginWeb() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testVerifyBusDestinationLocationLoginWeb");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String destination = param.get("destination");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        BusSearchPageWeb<?> busSearchPage = BusSearchPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(username, password, otp, retryCounter);

        step("User select IDR currency");
        homePage.selectCurrency("IDR");

        step("User open bus vertical");
        homePage.openVerticalPageViaURL("bus-travel");

        step("User click skip button");
        busSearchPage.clickSkipButton();

        step("User select destination location");
        busSearchPage.clickDestinationLocationInput();
        busSearchPage.selectLocation(destination);
        Assertion.assertThat("Verify destination location is selected", busSearchPage.isLocationSelected(destination), is(true));
    }

    @Author(names = "Okta")
    @Module(name = "SRP")
    @Tribe(name = "Bus")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyBusSearchOnewayGuest() throws InterruptedException {
        Map<String, String> param = getParam("testVerifyBusSearchOnewayGuest");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String origin = param.get("origin");
        String destination = param.get("destination");
        int daysToDeparture = Integer.parseInt(param.get("daysToDeparture"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDeparture);
        int totalPassenger = Integer.parseInt(param.get("totalPassenger"));

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        BusSearchPageWeb<?> busSearchPage = BusSearchPageWeb.getInstance(session);
        BusSearchResultPageWeb<?> busSearchResultPage = BusSearchResultPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as guest");
        loginPage.loginAsGuest();
        String language = homePage.getLanguage();

        step("User select IDR currency");
        homePage.selectCurrency("IDR");

        step("User open bus vertical");
        homePage.openVerticalPageViaURL("bus-travel");

        step("User click skip button");
        busSearchPage.clickSkipButton();

        step("User select oneway");
        busSearchPage.selectType("oneway");

        step("User select origin location");
        busSearchPage.clickOriginLocationInput();
        busSearchPage.selectLocation(origin);

        step("User select destination location");
        busSearchPage.clickDestinationLocationInput();
        busSearchPage.selectLocation(destination);

        step("User select date");
        busSearchPage.selectDate(departureDate);

        step("User select passenger");
        busSearchPage.clickPassengerInput();
        busSearchPage.setPassenger(totalPassenger);

        step("User search bus");
        busSearchPage.searchBus();

        step("User skip coachmark on SRP");
        busSearchPage.clickSkipButton();

        step("User is directed to SRP");
        busSearchResultPage.verifySearchResultIsCorrect(origin, destination, departureDate, null, totalPassenger, language);
    }

    @Author(names = "Okta")
    @Module(name = "SRP")
    @Tribe(name = "Bus")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyBusSearchOnewayLoginWeb() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testVerifyBusSearchOnewayLoginWeb");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));

        String origin = param.get("origin");
        String destination = param.get("destination");
        int daysToDeparture = Integer.parseInt(param.get("daysToDeparture"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDeparture);
        int totalPassenger = Integer.parseInt(param.get("totalPassenger"));

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        BusSearchPageWeb<?> busSearchPage = BusSearchPageWeb.getInstance(session);
        BusSearchResultPageWeb<?> busSearchResultPage = BusSearchResultPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(username, password, otp, retryCounter);
        String language = homePage.getLanguage();

        step("User select IDR currency");
        homePage.selectCurrency("IDR");

        step("User open bus vertical");
        homePage.openVerticalPageViaURL("bus-travel");

        step("User click skip button");
        busSearchPage.clickSkipButton();

        step("User select oneway");
        busSearchPage.selectType("oneway");

        step("User select origin location");
        busSearchPage.clickOriginLocationInput();
        busSearchPage.selectLocation(origin);

        step("User select destination location");
        busSearchPage.clickDestinationLocationInput();
        busSearchPage.selectLocation(destination);

        step("User select date");
        busSearchPage.selectDate(departureDate);

        step("User select passenger");
        busSearchPage.clickPassengerInput();
        busSearchPage.setPassenger(totalPassenger);

        step("User search bus");
        busSearchPage.searchBus();

        step("User click skip button");
        busSearchPage.clickSkipButton();

        step("User is directed to SRP");
        busSearchResultPage.verifySearchResultIsCorrect(origin, destination, departureDate, null, totalPassenger, language);
    }

    @Author(names = "Okta")
    @Module(name = "SRP")
    @Tribe(name = "Bus")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyBusSearchRoundtripGuestWeb() throws InterruptedException {
        Map<String, String> param = getParam("testVerifyBusSearchRoundtripGuestWeb");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String origin = param.get("origin");
        String destination = param.get("destination");
        int daysToDeparture = Integer.parseInt(param.get("daysToDeparture"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDeparture);
        int duration = Integer.parseInt(param.get("duration"));
        String returnDate = CoreFunctions.addToLongDate(departureDate, duration);
        int totalPassenger = Integer.parseInt(param.get("totalPassenger"));

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        BusSearchPageWeb<?> busSearchPage = BusSearchPageWeb.getInstance(session);
        BusSearchResultPageWeb<?> busSearchResultPage = BusSearchResultPageWeb.getInstance(session);
        BusPdpPageWeb<?> busPdpPage = BusPdpPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as guest");
        loginPage.loginAsGuest();
        String language = homePage.getLanguage();

        step("User select IDR currency");
        homePage.selectCurrency("IDR");

        step("User open bus vertical");
        homePage.openVerticalPageViaURL("bus-travel");

        step("User click skip button");
        busSearchPage.clickSkipButton();

        step("User select roundtrip");
        busSearchPage.selectType("roundtrip");

        step("User select origin location");
        busSearchPage.clickOriginLocationInput();
        busSearchPage.selectLocation(origin);

        step("User select destination location");
        busSearchPage.clickDestinationLocationInput();
        busSearchPage.selectLocation(destination);

        step("User select date");
        busSearchPage.selectDate(departureDate, returnDate);

        step("User select passenger");
        busSearchPage.clickPassengerInput();
        busSearchPage.setPassenger(totalPassenger);

        step("User search bus");
        busSearchPage.searchBus();

        step("User click skip button");
        busSearchPage.clickSkipButton();

        step("User is directed to departure SRP");
        busSearchResultPage.verifySearchResultIsCorrect(origin, destination, departureDate, returnDate, totalPassenger, language);

        step("User select first bus card");
        busSearchResultPage.selectFirstSrpCard();

        step("User select seat for departure bus");
        busPdpPage.selectSeat();

        step("User is directed to return SRP");
        if (Configs.PLATFORM == Platform.DWEB) {
            busSearchResultPage.verifySearchResultIsCorrect(origin, destination, departureDate, returnDate, totalPassenger, language);
        } else {
            busSearchResultPage.verifySearchResultIsCorrect(destination, origin, departureDate, returnDate, totalPassenger, language);
        }
    }

    @Author(names = "Okta")
    @Module(name = "SRP")
    @Tribe(name = "Bus")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyBusSearchRoundtripLoginWeb() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testVerifyBusSearchRoundtripLoginWeb");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));

        String origin = param.get("origin");
        String destination = param.get("destination");
        int daysToDeparture = Integer.parseInt(param.get("daysToDeparture"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDeparture);
        int duration = Integer.parseInt(param.get("duration"));
        String returnDate = CoreFunctions.addToLongDate(departureDate, duration);
        int totalPassenger = Integer.parseInt(param.get("totalPassenger"));

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        BusSearchPageWeb<?> busSearchPage = BusSearchPageWeb.getInstance(session);
        BusSearchResultPageWeb<?> busSearchResultPage = BusSearchResultPageWeb.getInstance(session);
        BusPdpPageWeb<?> busPdpPage = BusPdpPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(username, password, otp, retryCounter);
        String language = homePage.getLanguage();

        step("User select IDR currency");
        homePage.selectCurrency("IDR");

        step("User open bus vertical");
        homePage.openVerticalPageViaURL("bus-travel");

        step("User click skip button");
        busSearchPage.clickSkipButton();

        step("User select roundtrip");
        busSearchPage.selectType("roundtrip");

        step("User select origin location");
        busSearchPage.clickOriginLocationInput();
        busSearchPage.selectLocation(origin);

        step("User select destination location");
        busSearchPage.clickDestinationLocationInput();
        busSearchPage.selectLocation(destination);

        step("User select date");
        busSearchPage.selectDate(departureDate, returnDate);

        step("User select passenger");
        busSearchPage.clickPassengerInput();
        busSearchPage.setPassenger(totalPassenger);

        step("User search bus");
        busSearchPage.searchBus();

        step("User click skip button");
        busSearchPage.clickSkipButton();

        step("User is directed to departure SRP");
        busSearchResultPage.verifySearchResultIsCorrect(origin, destination, departureDate, returnDate, totalPassenger, language);

        step("User select first bus card");
        busSearchResultPage.selectFirstSrpCard();

        step("User select seat for departure bus");
        busPdpPage.selectSeat();

        step("User is directed to return SRP");
        if (Configs.PLATFORM == Platform.DWEB) {
            busSearchResultPage.verifySearchResultIsCorrect(origin, destination, departureDate, returnDate, totalPassenger, language);
        } else {
            busSearchResultPage.verifySearchResultIsCorrect(destination, origin, departureDate, returnDate, totalPassenger, language);
        }
    }

    @Author(names = "Okta")
    @Module(name = "BookingForm")
    @Tribe(name = "Bus")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyBusPriceDetailBookingFormGuestWeb() throws InterruptedException {
        Map<String, String> param = getParam("testVerifyBusPriceDetailBookingFormGuestWeb");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String origin = param.get("origin");
        String destination = param.get("destination");
        int daysToDeparture = Integer.parseInt(param.get("daysToDeparture"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDeparture);
        int totalPassenger = Integer.parseInt(param.get("totalPassenger"));

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        BusSearchPageWeb<?> busSearchPage = BusSearchPageWeb.getInstance(session);
        BusSearchResultPageWeb<?> busSearchResultPage = BusSearchResultPageWeb.getInstance(session);
        BusPdpPageWeb<?> busPdpPage = BusPdpPageWeb.getInstance(session);
        BusBookingFormPageWeb<?> busBookingFormPage = BusBookingFormPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as guest");
        loginPage.loginAsGuest();

        step("User select IDR currency");
        homePage.selectCurrency("IDR");

        step("User open bus vertical");
        homePage.openVerticalPageViaURL("bus-travel");

        step("User click skip button");
        busSearchPage.clickSkipButton();

        step("User select oneway");
        busSearchPage.selectType("oneway");

        step("User select origin location");
        busSearchPage.clickOriginLocationInput();
        busSearchPage.selectLocation(origin);

        step("User select destination location");
        busSearchPage.clickDestinationLocationInput();
        busSearchPage.selectLocation(destination);

        step("User select date");
        busSearchPage.selectDate(departureDate);

        step("User select passenger");
        busSearchPage.clickPassengerInput();
        busSearchPage.setPassenger(totalPassenger);

        step("User search bus");
        busSearchPage.searchBus();

        step("User select first bus card");
        busSearchResultPage.selectFirstSrpCard();

        step("User select seat");
        busPdpPage.selectSeat();

        step("User proceed to booking form");
        Assertion.assertThat("Verify price detail is displayed correctly", busBookingFormPage.verifyPriceDetail(), is(true));
}

    @Author(names = {"Okta"})
    @Module(name = "BookingForm")
    @Tribe(name = "Bus")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyBusPriceDetailBookingFormLoginWeb() throws InterruptedException, FailedLoginException {
        Map<String, String> param = getParam("testVerifyBusPriceDetailBookingFormLoginWeb");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));

        String origin = param.get("origin");
        String destination = param.get("destination");
        int daysToDeparture = Integer.parseInt(param.get("daysToDeparture"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDeparture);
        int totalPassenger = Integer.parseInt(param.get("totalPassenger"));

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        BusSearchPageWeb<?> busSearchPage = BusSearchPageWeb.getInstance(session);
        BusSearchResultPageWeb<?> busSearchResultPage = BusSearchResultPageWeb.getInstance(session);
        BusPdpPageWeb<?> busPdpPage = BusPdpPageWeb.getInstance(session);
        BusBookingFormPageWeb<?> busBookingFormPage = BusBookingFormPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(username, password, otp, retryCounter);

        step("User select IDR currency");
        homePage.selectCurrency("IDR");

        step("User open bus vertical");
        homePage.openVerticalPageViaURL("bus-travel");

        step("User click skip button");
        busSearchPage.clickSkipButton();

        step("User select oneway");
        busSearchPage.selectType("oneway");

        step("User select origin location");
        busSearchPage.clickOriginLocationInput();
        busSearchPage.selectLocation(origin);

        step("User select destination location");
        busSearchPage.clickDestinationLocationInput();
        busSearchPage.selectLocation(destination);

        step("User select date");
        busSearchPage.selectDate(departureDate);

        step("User select passenger");
        busSearchPage.clickPassengerInput();
        busSearchPage.setPassenger(totalPassenger);

        step("User search bus");
        busSearchPage.searchBus();

        step("User select first bus card");
        busSearchResultPage.selectFirstSrpCard();

        step("User select seat");
        busPdpPage.selectSeat();

        step("User proceed to booking form");
        Assertion.assertThat("Verify price detail is displayed correctly", busBookingFormPage.verifyPriceDetail(), is(true));
    }

    @Author(names = {"Okta", "Eka"})
    @Module(name = "BookingForm")
    @Tribe(name = "Bus")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyBusOrderSummaryBookingFormGuest() throws InterruptedException {
        Map<String, String> param = getParam("testVerifyBusOrderSummaryBookingFormGuest");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String origin = param.get("origin");
        String destination = param.get("destination");
        int daysToDeparture = Integer.parseInt(param.get("daysToDeparture"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDeparture);
        int totalPassenger = Integer.parseInt(param.get("totalPassenger"));

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        BusSearchPageWeb<?> busSearchPage = BusSearchPageWeb.getInstance(session);
        BusSearchResultPageWeb<?> busSearchResultPage = BusSearchResultPageWeb.getInstance(session);
        BusPdpPageWeb<?> busPdpPage = BusPdpPageWeb.getInstance(session);
        BusBookingFormPageWeb<?> busBookingFormPage = BusBookingFormPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as guest");
        loginPage.loginAsGuest();
        String language = homePage.getLanguage();

        step("User open bus vertical");
        homePage.openVerticalPageViaURL("bus-travel");

        step("User click skip button");
        busSearchPage.clickSkipButton();

        step("User select oneway");
        busSearchPage.selectType("oneway");

        step("User select origin location");
        busSearchPage.clickOriginLocationInput();
        busSearchPage.selectLocation(origin);

        step("User select destination location");
        busSearchPage.clickDestinationLocationInput();
        busSearchPage.selectLocation(destination);

        step("User select date");
        busSearchPage.selectDate(departureDate);

        step("User select passenger");
        busSearchPage.clickPassengerInput();
        busSearchPage.setPassenger(totalPassenger);

        step("User search bus");
        busSearchPage.searchBus();
        BusSrpOneWayData busSrpData = busSearchResultPage.getFirstBusData(departureDate, language);

        step("User select first bus card");
        busSearchResultPage.selectFirstSrpCard();

        step("User select seat");
        busPdpPage.selectSeat();

        step("User click order detail");
        busBookingFormPage.clickDetails();

        step("User proceed to booking form");
        busBookingFormPage.verifyOrderSummary(busSrpData);
    }

    @Author(names = {"Okta", "Eka"})
    @Module(name = "BookingForm")
    @Tribe(name = "Bus")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyBusOrderSummaryBookingFormLogin() throws InterruptedException, FailedLoginException {
        Map<String, String> param = getParam("testVerifyBusOrderSummaryBookingFormLogin");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));

        String origin = param.get("origin");
        String destination = param.get("destination");
        int daysToDeparture = Integer.parseInt(param.get("daysToDeparture"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDeparture);
        int totalPassenger = Integer.parseInt(param.get("totalPassenger"));

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        BusSearchPageWeb<?> busSearchPage = BusSearchPageWeb.getInstance(session);
        BusSearchResultPageWeb<?> busSearchResultPage = BusSearchResultPageWeb.getInstance(session);
        BusPdpPageWeb<?> busPdpPage = BusPdpPageWeb.getInstance(session);
        BusBookingFormPageWeb<?> busBookingFormPage = BusBookingFormPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(username, password, otp, retryCounter);
        String language = homePage.getLanguage();

        step("User open bus vertical");
        homePage.openVerticalPageViaURL("bus-travel");

        step("User click skip button");
        busSearchPage.clickSkipButton();

        step("User select oneway");
        busSearchPage.selectType("oneway");

        step("User select origin location");
        busSearchPage.clickOriginLocationInput();
        busSearchPage.selectLocation(origin);

        step("User select destination location");
        busSearchPage.clickDestinationLocationInput();
        busSearchPage.selectLocation(destination);

        step("User select date");
        busSearchPage.selectDate(departureDate);

        step("User select passenger");
        busSearchPage.clickPassengerInput();
        busSearchPage.setPassenger(totalPassenger);

        step("User search bus");
        busSearchPage.searchBus();
        BusSrpOneWayData busSrpData = busSearchResultPage.getFirstBusData(departureDate, language);

        step("User select first bus card");
        busSearchResultPage.selectFirstSrpCard();

        step("User select seat");
        busPdpPage.selectSeat();

        step("User click order detail");
        busBookingFormPage.clickDetails();

        step("User proceed to booking form");
        busBookingFormPage.verifyOrderSummary(busSrpData);
    }

    @Author(names = {"Okta", "Clement"})
    @Module(name = "CompleteBooking")
    @Tribe(name = "Bus")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyBusBookingOnewayGuest() throws InterruptedException {
        Map<String, String> param = getParam("testVerifyBusBookingOnewayGuest");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String origin = param.get("origin");
        String destination = param.get("destination");
        int daysToDeparture = Integer.parseInt(param.get("daysToDeparture"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDeparture);
        int totalPassenger = 1;

        String fullName = CoreFunctions.randomNameWithoutTest();
        String phoneNumber = param.get("phoneNumber");
        String email = param.get("email");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        BusSearchPageWeb<?> busSearchPage = BusSearchPageWeb.getInstance(session);
        BusSearchResultPageWeb<?> busSearchResultPage = BusSearchResultPageWeb.getInstance(session);
        BusPdpPageWeb<?> busPdpPage = BusPdpPageWeb.getInstance(session);
        BusBookingFormPageWeb<?> busBookingFormPage = BusBookingFormPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as guest");
        loginPage.loginAsGuest();

        step("User open bus vertical");
        homePage.openVerticalPageViaURL("bus-travel");

        step("User click skip button");
        busSearchPage.clickSkipButton();

        step("User select oneway");
        busSearchPage.selectType("oneway");

        step("User select origin location");
        busSearchPage.clickOriginLocationInput();
        busSearchPage.selectLocation(origin);

        step("User select destination location");
        busSearchPage.clickDestinationLocationInput();
        busSearchPage.selectLocation(destination);

        step("User select date");
        busSearchPage.selectDate(departureDate);

        step("User select passenger");
        busSearchPage.clickPassengerInput();
        busSearchPage.setPassenger(totalPassenger);

        step("User search bus");
        busSearchPage.searchBus();

        step("User select first bus card");
        busSearchResultPage.selectFirstSrpCard();

        step("User select seat");
        busPdpPage.selectSeat();

        step("User input contact detail");
        busBookingFormPage.fillContactDetail(fullName, phoneNumber, email);

        step("User input passenger detail");
        busBookingFormPage.fillPassengerDetail(totalPassenger);

        step("User check terms and conditions");
        busBookingFormPage.clickTncCheckBox();

        step("User continue to payment page");
        busBookingFormPage.continueToPaymentPage();
        String orderID = paymentPage.getOrderId();
        Assertion.assertThat("Verify order ID is displayed in payment page", !orderID.isEmpty(), is(true));
    }

    @Author(names = {"Okta", "Clement"})
    @Module(name = "CompleteBooking")
    @Tribe(name = "Bus")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyBusBookingOnewayLogin() throws InterruptedException, FailedLoginException {
        Map<String, String> param = getParam("testVerifyBusBookingOnewayLogin");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));

        String origin = param.get("origin");
        String destination = param.get("destination");
        int daysToDeparture = Integer.parseInt(param.get("daysToDeparture"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDeparture);
        int totalPassenger = 1;

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        BusSearchPageWeb<?> busSearchPage = BusSearchPageWeb.getInstance(session);
        BusSearchResultPageWeb<?> busSearchResultPage = BusSearchResultPageWeb.getInstance(session);
        BusPdpPageWeb<?> busPdpPage = BusPdpPageWeb.getInstance(session);
        BusBookingFormPageWeb<?> busBookingFormPage = BusBookingFormPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);

        step("User open home page");
        homePage.goToHomePage();

        step("Login as " + username + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(username, password);

        step("User open bus vertical");
        homePage.openVerticalPageViaURL("bus-travel");

        step("User click skip button");
        busSearchPage.clickSkipButton();

        step("User select oneway");
        busSearchPage.selectType("oneway");

        step("User select origin location");
        busSearchPage.clickOriginLocationInput();
        busSearchPage.selectLocation(origin);

        step("User select destination location");
        busSearchPage.clickDestinationLocationInput();
        busSearchPage.selectLocation(destination);

        step("User select date");
        busSearchPage.selectDate(departureDate);

        step("User select passenger");
        busSearchPage.clickPassengerInput();
        busSearchPage.setPassenger(totalPassenger);

        step("User search bus");
        busSearchPage.searchBus();

        step("User select first bus card");
        busSearchResultPage.selectFirstSrpCard();

        step("User select seat");
        busPdpPage.selectSeat();

        step("User input passenger detail");
        busBookingFormPage.fillPassengerDetail(totalPassenger);

        step("Extract the Payment Price from the Booking form");
        Map<String, String> summary = busBookingFormPage.capturePriceSummaryDetails();

        step("User check terms and conditions");
        busBookingFormPage.clickTncCheckBox();

        step("User continue to payment page");
        busBookingFormPage.continueToPaymentPage();
        String orderID = paymentPage.getOrderId();
        Assertion.assertThat("Verify order ID is displayed in payment page", !orderID.isEmpty(), is(true));

        step("Verify the Payment Price details match the Price Details modal");
        paymentPage.verifyBottomPriceDetailsMatchSummary(summary);
    }

    @Author(names = "Okta")
    @Module(name = "CompleteBooking")
    @Tribe(name = "Bus")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyBusBookingRoundtripGuest() throws InterruptedException {
        Map<String, String> param = getParam("testVerifyBusBookingRoundtripGuest");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String origin = param.get("origin");
        String destination = param.get("destination");
        int daysToDeparture = Integer.parseInt(param.get("daysToDeparture"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDeparture);
        int duration = Integer.parseInt(param.get("duration"));
        String returnDate = CoreFunctions.addToLongDate(departureDate, duration);
        int totalPassenger = Integer.parseInt(param.get("totalPassenger"));

        String fullName = CoreFunctions.randomNameWithoutTest();
        String phoneNumber = param.get("phoneNumber");
        String email = param.get("email");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        BusSearchPageWeb<?> busSearchPage = BusSearchPageWeb.getInstance(session);
        BusSearchResultPageWeb<?> busSearchResultPage = BusSearchResultPageWeb.getInstance(session);
        BusPdpPageWeb<?> busPdpPage = BusPdpPageWeb.getInstance(session);
        BusBookingFormPageWeb<?> busBookingFormPage = BusBookingFormPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as guest");
        loginPage.loginAsGuest();

        step("User open bus vertical");
        homePage.openVerticalPageViaURL("bus-travel");

        step("User click skip button");
        busSearchPage.clickSkipButton();

        step("User select roundtrip");
        busSearchPage.selectType("roundtrip");

        step("User select origin location");
        busSearchPage.clickOriginLocationInput();
        busSearchPage.selectLocation(origin);

        step("User select destination location");
        busSearchPage.clickDestinationLocationInput();
        busSearchPage.selectLocation(destination);

        step("User select date");
        busSearchPage.selectDate(departureDate, returnDate);

        step("User select passenger");
        busSearchPage.clickPassengerInput();
        busSearchPage.setPassenger(totalPassenger);

        step("User search bus");
        busSearchPage.searchBus();

        step("User select first bus card");
        busSearchResultPage.selectFirstSrpCard();

        step("User select seat for departure bus");
        busPdpPage.selectSeat();

        step("User select first bus card");
        busSearchResultPage.selectFirstSrpCard();

        step("User select seat for return bus");
        busPdpPage.selectSeat();

        step("User input contact detail");
        busBookingFormPage.fillContactDetail(fullName, phoneNumber, email);

        step("User input passenger detail");
        busBookingFormPage.fillPassengerDetail(totalPassenger);

        step("User check terms and conditions");
        busBookingFormPage.clickTncCheckBox();

        step("User continue to payment page");
        busBookingFormPage.continueToPaymentPage();
        String orderID = paymentPage.getOrderId();
        Assertion.assertThat("Verify order ID is displayed in payment page", !orderID.isEmpty(), is(true));
    }

    @Author(names = "Okta")
    @Module(name = "Bus")
    @Tribe(name = "NFT")
    @Vertical(name = "NFT")
    @Test
    public void Bus_testVerifyBusBookingRoundtripLogin() throws InterruptedException, FailedLoginException {
        Map<String, String> param = getParam("Bus_testVerifyBusBookingRoundtripLogin");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));

        String origin = param.get("origin");
        String destination = param.get("destination");
        int daysToDeparture = Integer.parseInt(param.get("daysToDeparture"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDeparture);
        int duration = Integer.parseInt(param.get("duration"));
        String returnDate = CoreFunctions.addToLongDate(departureDate, duration);
        int totalPassenger = Integer.parseInt(param.get("totalPassenger"));

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        BusSearchPageWeb<?> busSearchPage = BusSearchPageWeb.getInstance(session);
        BusSearchResultPageWeb<?> busSearchResultPage = BusSearchResultPageWeb.getInstance(session);
        BusPdpPageWeb<?> busPdpPage = BusPdpPageWeb.getInstance(session);
        BusBookingFormPageWeb<?> busBookingFormPage = BusBookingFormPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        CalendarPageWeb<?> calendarPage = CalendarPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("Login as " + username + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(username, password);

        step("User open bus vertical");
        homePage.openVerticalPageViaURL("bus-travel");

        step("User click skip button");
        busSearchPage.clickSkipButton();

        step("User select roundtrip");
        busSearchPage.selectType("roundtrip");

        step("User select origin location");
        busSearchPage.clickOriginLocationInput();
        busSearchPage.selectLocation(origin);

        step("User select destination location");
        busSearchPage.clickDestinationLocationInput();
        busSearchPage.selectLocation(destination);

        step("User select date");
        busSearchPage.clickOnCalendarWidgetToOpen();
        calendarPage.selectDate(departureDate, returnDate);

        step("User select passenger");
        busSearchPage.clickPassengerInput();
        busSearchPage.setPassenger(totalPassenger);

        step("Fetching date value");
        String date1 = busSearchPage.getDateText();

        step("User search bus");
        busSearchPage.searchBus();

        step("User returns to the bus search form to verify that the travel date has not changed");
        homePage.openVerticalPageViaURL("bus-travel");
        String date2 = busSearchPage.getDateText();
        Assertion.assertThat("Verify travel date not changed", date2, containsString(date1));

        step("User search bus");
        busSearchPage.searchBus();

        step("User select first bus card");
        busSearchResultPage.selectFirstSrpCard();

        step("User select seat for departure bus");
        busPdpPage.selectSeat();

        step("User select first bus card");
        busSearchResultPage.selectFirstSrpCard();

        step("User select seat for return bus");
        busPdpPage.selectSeat();

        step("User input passenger detail");
        busBookingFormPage.fillPassengerDetail(totalPassenger);

        step("User check terms and conditions");
        busBookingFormPage.clickTncCheckBox();

        step("User continue to payment page");
        busBookingFormPage.continueToPaymentPage();
        busBookingFormPage.priceChangePopUp();
        String orderID = paymentPage.getOrderId();
        Assertion.assertThat("Verify order ID is displayed in payment page", !orderID.isEmpty(), is(true));
    }

    @Author(names = "Okta")
    @Module(name = "CompleteBooking")
    @Tribe(name = "Bus")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyBusPriceWithCurrencyNonIDRGuest() throws InterruptedException {
        Map<String, String> param = getParam("testVerifyBusPriceWithCurrencyNonIDRGuest");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String currency = param.get("currency");
        String origin = param.get("origin");
        String destination = param.get("destination");
        int daysToDeparture = Integer.parseInt(param.get("daysToDeparture"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDeparture);
        int totalPassenger = Integer.parseInt(param.get("totalPassenger"));

        String fullName = CoreFunctions.randomNameWithoutTest();
        String phoneNumber = param.get("phoneNumber");
        String email = param.get("email");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        BusSearchPageWeb<?> busSearchPage = BusSearchPageWeb.getInstance(session);
        BusSearchResultPageWeb<?> busSearchResultPage = BusSearchResultPageWeb.getInstance(session);
        BusPdpPageWeb<?> busPdpPage = BusPdpPageWeb.getInstance(session);
        BusBookingFormPageWeb<?> busBookingFormPage = BusBookingFormPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as guest");
        loginPage.loginAsGuest();

        step("User select " + currency + " currency");
        homePage.selectCurrency(currency);

        step("User open bus vertical via deeplink");
        homePage.openVerticalPageViaURL("bus-travel");

        step("User click skip button");
        busSearchPage.clickSkipButton();
        if (Configs.PLATFORM == Platform.DWEB) {
            String searchPageCurrency = homePage.getCurrentCurrency();
            Assertion.assertThat("Verify currency in search page is set to " + currency,
                    searchPageCurrency.equals(currency), is(true));
        }

        step("User select oneway");
        busSearchPage.selectType("oneway");

        step("User select origin location");
        busSearchPage.clickOriginLocationInput();
        busSearchPage.selectLocation(origin);

        step("User select destination location");
        busSearchPage.clickDestinationLocationInput();
        busSearchPage.selectLocation(destination);

        step("User select date");
        busSearchPage.selectDate(departureDate);

        step("User select passenger");
        busSearchPage.clickPassengerInput();
        busSearchPage.setPassenger(totalPassenger);

        step("User search bus");
        busSearchPage.searchBus();
        busSearchResultPage.verifySrpPriceInIDR();

        step("User select first bus card");
        busSearchResultPage.selectFirstSrpCard();
        busPdpPage.verifyPdpPriceInIDR();

        step("User select seat");
        busPdpPage.selectSeat();
        Assertion.assertThat("Verify prices in booking form are in IDR", busBookingFormPage.verifyBookingPriceInIDR(), is(true));

        step("User input contact detail");
        busBookingFormPage.fillContactDetail(fullName, phoneNumber, email);

        step("User input passenger detail");
        busBookingFormPage.fillPassengerDetail(totalPassenger);

        step("User check terms and conditions");
        busBookingFormPage.clickTncCheckBox();

        step("User continue to payment page");
        busBookingFormPage.continueToPaymentPage();
        String orderID = paymentPage.getOrderId();
        Assertion.assertThat("Verify order ID is displayed in payment page", !orderID.isEmpty(), is(true));
        Assertion.assertThat("Verify price in payment page is in IDR", paymentPage.verifyTotalPaymentPriceInIDR(), is(true));
    }

    @Author(names = "Okta")
    @Module(name = "CompleteBooking")
    @Tribe(name = "Bus")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyBusPriceWithCurrencyNonIDRLogin() throws InterruptedException, FailedLoginException {
        Map<String, String> param = getParam("testVerifyBusPriceWithCurrencyNonIDRLogin");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));

        String currency = param.get("currency");
        String origin = param.get("origin");
        String destination = param.get("destination");
        int daysToDeparture = Integer.parseInt(param.get("daysToDeparture"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDeparture);
        int totalPassenger = Integer.parseInt(param.get("totalPassenger"));

        String fullName = CoreFunctions.randomNameWithoutTest();
        String phoneNumber = param.get("phoneNumber");
        String email = param.get("email");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        BusSearchPageWeb<?> busSearchPage = BusSearchPageWeb.getInstance(session);
        BusSearchResultPageWeb<?> busSearchResultPage = BusSearchResultPageWeb.getInstance(session);
        BusPdpPageWeb<?> busPdpPage = BusPdpPageWeb.getInstance(session);
        BusBookingFormPageWeb<?> busBookingFormPage = BusBookingFormPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        CalendarPageWeb<?> calendarPage = CalendarPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("Login as " + username + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(username, password);

        step("User select " + currency + " currency");
        homePage.selectCurrency(currency);

        step("User open bus vertical via deeplink with " + currency + " currency");
        homePage.openVerticalPageViaURL("bus-travel", currency);

        step("User click skip button");
        busSearchPage.clickSkipButton();
        if (Configs.PLATFORM == Platform.DWEB) {
            String searchPageCurrency = homePage.getCurrentCurrency();
            Assertion.assertThat("Verify currency in search page is set to " + "IDR",
                    searchPageCurrency.contains("IDR"), is(true));
        }

        step("User select oneway");
        busSearchPage.selectType("oneway");

        step("User select origin location");
        busSearchPage.clickOriginLocationInput();
        busSearchPage.selectLocation(origin);

        step("User select destination location");
        busSearchPage.clickDestinationLocationInput();
        busSearchPage.selectLocation(destination);

        step("User select date");
        busSearchPage.clickOnCalendarWidgetToOpen();
        calendarPage.selectDate(departureDate);

        step("User select passenger");
        busSearchPage.clickPassengerInput();
        busSearchPage.setPassenger(totalPassenger);

        step("User search bus");
        busSearchPage.searchBus();
        busSearchResultPage.verifySrpPriceInIDR();

        step("User select first bus card");
        busSearchResultPage.selectFirstSrpCard();
        busPdpPage.verifyPdpPriceInIDR();

        step("User select seat");
        busPdpPage.selectSeat(); 
        Assertion.assertThat("Verify prices in booking form are in IDR", busBookingFormPage.verifyBookingPriceInIDR(), is(true));

        step("User input passenger detail");
        busBookingFormPage.fillPassengerDetail(totalPassenger);

        step("User check terms and conditions");
        busBookingFormPage.clickTncCheckBox();

        step("User continue to payment page");
        busBookingFormPage.continueToPaymentPage();
        String orderID = paymentPage.getOrderId();
        Assertion.assertThat("Verify order ID is displayed in payment page", !orderID.isEmpty(), is(true));
        Assertion.assertThat("Verify price in payment page is in IDR", paymentPage.verifyTotalPaymentPriceInIDR(), is(true));
    }

    @Author(names = {"Okta", "Clement"})
    @Module(name = "CompleteBooking")
    @Tribe(name = "Bus")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyBusPaymentDetailGuest() throws InterruptedException {
        Map<String, String> param = getParam("testVerifyBusPaymentDetailGuest");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String origin = param.get("origin");
        String destination = param.get("destination");
        int daysToDeparture = Integer.parseInt(param.get("daysToDeparture"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDeparture);
        int totalPassenger = Integer.parseInt(param.get("totalPassenger"));

        String fullName = CoreFunctions.randomNameWithoutTest();
        String phoneNumber = param.get("phoneNumber");
        String email = param.get("email");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        BusSearchPageWeb<?> busSearchPage = BusSearchPageWeb.getInstance(session);
        BusSearchResultPageWeb<?> busSearchResultPage = BusSearchResultPageWeb.getInstance(session);
        BusPdpPageWeb<?> busPdpPage = BusPdpPageWeb.getInstance(session);
        BusBookingFormPageWeb<?> busBookingFormPage = BusBookingFormPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as guest");
        loginPage.loginAsGuest();

        step("User select IDR currency");
        homePage.selectCurrency("IDR");

        step("User open bus vertical");
        homePage.openVerticalPageViaURL("bus-travel");

        step("User click skip button");
        busSearchPage.clickSkipButton();

        step("User select oneway");
        busSearchPage.selectType("oneway");

        step("User select origin location");
        busSearchPage.clickOriginLocationInput();
        busSearchPage.selectLocation(origin);

        step("User select destination location");
        busSearchPage.clickDestinationLocationInput();
        busSearchPage.selectLocation(destination);

        step("User select date");
        busSearchPage.selectDate(departureDate);

        step("User select passenger");
        busSearchPage.clickPassengerInput();
        busSearchPage.setPassenger(totalPassenger);

        step("User search bus");
        busSearchPage.searchBus();

        step("User select first bus card");
        busSearchResultPage.selectFirstSrpCard();

        step("User select seat");
        busPdpPage.selectSeat();

        step("User input contact detail");
        busBookingFormPage.fillContactDetail(fullName, phoneNumber, email);

        step("User input passenger detail");
        busBookingFormPage.fillPassengerDetail(totalPassenger);

        step("User check terms and conditions");
        busBookingFormPage.clickTncCheckBox();
        String bookingPrice = busBookingFormPage.getTotalPriceFooter();

        step("User continue to payment page");
        busBookingFormPage.continueToPaymentPage();

        String orderID = paymentPage.getOrderId();
        Assertion.assertThat("Verify order ID is displayed in payment page", !orderID.isEmpty(), is(true));

        String paymentPrice;

        if (Configs.PLATFORM == Platform.DWEB) {
            paymentPrice = paymentPage.getTotalPriceDetail();
        } else {
            paymentPrice = paymentPage.getTotalPriceDetailMweb();
        }

        Assertion.assertThat("Verify price in payment detail is the same as in booking form", paymentPrice, is(bookingPrice));
    }

    @Author(names = {"Okta", "Clement"})
    @Module(name = "CompleteBooking")
    @Tribe(name = "Bus")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyBusPaymentDetailLogin() throws InterruptedException, FailedLoginException {
        Map<String, String> param = getParam("testVerifyBusPaymentDetailLogin");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));

        String origin = param.get("origin");
        String destination = param.get("destination");
        int daysToDeparture = Integer.parseInt(param.get("daysToDeparture"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDeparture);
        int totalPassenger = Integer.parseInt(param.get("totalPassenger"));

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        BusSearchPageWeb<?> busSearchPage = BusSearchPageWeb.getInstance(session);
        BusSearchResultPageWeb<?> busSearchResultPage = BusSearchResultPageWeb.getInstance(session);
        BusPdpPageWeb<?> busPdpPage = BusPdpPageWeb.getInstance(session);
        BusBookingFormPageWeb<?> busBookingFormPage = BusBookingFormPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(username, password, otp, retryCounter);

        step("User select IDR currency");
        homePage.selectCurrency("IDR");

        step("User open bus vertical");
        homePage.openVerticalPageViaURL("bus-travel");

        step("User click skip button");
        busSearchPage.clickSkipButton();

        step("User select oneway");
        busSearchPage.selectType("oneway");

        step("User select origin location");
        busSearchPage.clickOriginLocationInput();
        busSearchPage.selectLocation(origin);

        step("User select destination location");
        busSearchPage.clickDestinationLocationInput();
        busSearchPage.selectLocation(destination);

        step("User select date");
        busSearchPage.selectDate(departureDate);

        step("User select passenger");
        busSearchPage.clickPassengerInput();
        busSearchPage.setPassenger(totalPassenger);

        step("User search bus");
        busSearchPage.searchBus();

        step("User select first bus card");
        busSearchResultPage.selectFirstSrpCard();

        step("User select seat");
        busPdpPage.selectSeat();

        step("User input passenger detail");
        busBookingFormPage.fillPassengerDetail(totalPassenger);

        step("User check terms and conditions");
        busBookingFormPage.clickTncCheckBox();
        String bookingPrice = busBookingFormPage.getTotalPriceFooter();

        step("User continue to payment page");
        busBookingFormPage.continueToPaymentPage();

        String orderID = paymentPage.getOrderId();
        Assertion.assertThat("Verify order ID is displayed in payment page", !orderID.isEmpty(), is(true));

        String paymentPrice;

        if (Configs.PLATFORM == Platform.DWEB) {
            paymentPrice = paymentPage.getTotalPriceDetail();
        } else {
            paymentPrice = paymentPage.getTotalPriceDetailMweb();
        }

        Assertion.assertThat("Verify price in payment detail is the same as in booking form", paymentPrice, is(bookingPrice));
    }

    @Author(names = {"Okta", "Clement"})
    @Module(name = "CompleteBooking")
    @Tribe(name = "Bus")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyBusOrderDetailGuest() throws InterruptedException {
        Map<String, String> param = getParam("testVerifyBusOrderDetailGuest");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String origin = param.get("origin");
        String destination = param.get("destination");
        int daysToDeparture = Integer.parseInt(param.get("daysToDeparture"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDeparture);
        int totalPassenger = Integer.parseInt(param.get("totalPassenger"));

        String fullName = CoreFunctions.randomNameWithoutTest();
        String phoneNumber = param.get("phoneNumber");
        String email = param.get("email");
        String paymentMethod = param.get("paymentMethod");
        String paymentType = param.get("paymentType");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        BusSearchPageWeb<?> busSearchPage = BusSearchPageWeb.getInstance(session);
        BusSearchResultPageWeb<?> busSearchResultPage = BusSearchResultPageWeb.getInstance(session);
        BusPdpPageWeb<?> busPdpPage = BusPdpPageWeb.getInstance(session);
        BusBookingFormPageWeb<?> busBookingFormPage = BusBookingFormPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as guest");
        loginPage.loginAsGuest();

        step("User select IDR currency");
        homePage.selectCurrency("IDR");

        step("User open bus vertical");
        homePage.openVerticalPageViaURL("bus-travel");

        step("User click skip button");
        busSearchPage.clickSkipButton();

        step("User select oneway");
        busSearchPage.selectType("oneway");

        step("User select origin location");
        busSearchPage.clickOriginLocationInput();
        busSearchPage.selectLocation(origin);

        step("User select destination location");
        busSearchPage.clickDestinationLocationInput();
        busSearchPage.selectLocation(destination);

        step("User select date");
        busSearchPage.selectDate(departureDate);

        step("User select passenger");
        busSearchPage.clickPassengerInput();
        busSearchPage.setPassenger(totalPassenger);

        step("User search bus");
        busSearchPage.searchBus();

        step("User select first bus card");
        busSearchResultPage.selectFirstSrpCard();

        step("User select seat");
        busPdpPage.selectSeat();

        step("User input contact detail");
        busBookingFormPage.fillContactDetail(fullName, phoneNumber, email);

        step("User input passenger detail");
        busBookingFormPage.fillPassengerDetail(totalPassenger);

        step("User check terms and conditions");
        busBookingFormPage.clickTncCheckBox();

        step("User continue to payment page");
        busBookingFormPage.continueToPaymentPage();

        String orderID = paymentPage.getOrderId();
        Assertion.assertThat("Verify order ID is displayed in payment page", !orderID.isEmpty(), is(true));

        step("User select payment method ");
        paymentPage.clickOnSelectPaymentMethod();
        if (Configs.PLATFORM == Platform.DWEB) {
            paymentPage.selectPaymentMethod(paymentMethod);
        } else {
            paymentPage.selectPaymentMethodMWeb(paymentMethod, paymentType);
        }

        step("User complete payment");
        paymentPage.clickOnPaymentButton();

        if (Configs.ENVIRONMENT == Environment.PROD) {
            step("User open order list page");
            paymentPage.clickSeeOrderListButton();
        }

        if (Configs.ENVIRONMENT == Environment.GK|| Configs.ENVIRONMENT == Environment.PREPROD) {
            step("User open order detail page");
            orderListPage.openActiveTicketViaURL(orderID);
        }

        Assertion.assertThat("Verify web page will redirect to login page for guest user", loginPage.isLoginPageOpened(), is(true));
    }

    @Author(names = "Okta")
    @Module(name = "Bus")
    @Tribe(name = "NFT")
    @Vertical(name = "NFT")
    @Test
    public void Bus_testVerifyBusOrderDetailLogin() throws InterruptedException, FailedLoginException {
        Map<String, String> param = getParam("Bus_testVerifyBusOrderDetailLogin");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));

        String origin = param.get("origin");
        String destination = param.get("destination");
        int daysToDeparture = Integer.parseInt(param.get("daysToDeparture"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDeparture);
        int totalPassenger = Integer.parseInt(param.get("totalPassenger"));
        String paymentMethod = param.get("paymentMethod");
        String paymentType = param.get("paymentType");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        BusSearchPageWeb<?> busSearchPage = BusSearchPageWeb.getInstance(session);
        BusSearchResultPageWeb<?> busSearchResultPage = BusSearchResultPageWeb.getInstance(session);
        BusPdpPageWeb<?> busPdpPage = BusPdpPageWeb.getInstance(session);
        BusBookingFormPageWeb<?> busBookingFormPage = BusBookingFormPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginWithSpecificAccountViaCookie(username, password);
        String language = homePage.getLanguage();

        step("User select IDR currency");
        homePage.selectCurrency("IDR");

        step("User open bus vertical");
        homePage.openVerticalPageViaURL("bus-travel");

        step("User click skip button");
        busSearchPage.clickSkipButton();

        step("User select oneway");
        busSearchPage.selectType("oneway");

        step("User select origin location");
        busSearchPage.clickOriginLocationInput();
        busSearchPage.selectLocation(origin);

        step("User select destination location");
        busSearchPage.clickDestinationLocationInput();
        busSearchPage.selectLocation(destination);

        step("User select date");
        busSearchPage.selectDate(departureDate);

        step("User select passenger");
        busSearchPage.clickPassengerInput();
        busSearchPage.setPassenger(totalPassenger);

        step("User search bus");
        busSearchPage.searchBus();
        BusSrpOneWayData busSrpData = busSearchResultPage.getFirstBusData(departureDate, language);

        step("User select first bus card");
        busSearchResultPage.selectFirstSrpCard();

        step("User select seat");
        busPdpPage.selectSeat();

        step("User input passenger detail");
        busBookingFormPage.fillPassengerDetail(totalPassenger);

        step("User check terms and conditions");
        busBookingFormPage.clickTncCheckBox();

        step("User continue to payment page");
        busBookingFormPage.continueToPaymentPage();

        String orderID = paymentPage.getOrderId();
        Assertion.assertThat("Verify order ID is displayed in payment page", !orderID.isEmpty(), is(true));

        step("User select payment method");
        paymentPage.clickOnSelectPaymentMethod();
        if (Configs.PLATFORM == Platform.DWEB) {
            paymentPage.selectPaymentMethod(paymentMethod);
        } else {
            paymentPage.selectPaymentMethodMWeb(paymentMethod, paymentType);
        }

        step("User complete payment");
        paymentPage.clickOnPaymentButton();

        if (Configs.ENVIRONMENT == Environment.PROD) {
            step("User open order list page");
            paymentPage.clickSeeOrderListButton();

            step("User open order detail page");
            orderListPage.openOrderDetailPage(orderID);
        }

        else if (Configs.ENVIRONMENT == Environment.GK|| Configs.ENVIRONMENT == Environment.PREPROD) {
            step("User open order detail page");
            orderListPage.openActiveTicketViaURL(orderID);
        }

        step("Verify bus order detail in order detail page");
        orderDetailPage.verifyBusOrderDetail(busSrpData);
    }

    @Author(names = "Okta")
    @Module(name = "SRP")
    @Tribe(name = "Bus")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyBusSortGuestWeb() throws InterruptedException {
        Map<String, String> param = getParam("testVerifyBusSortGuestWeb");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String origin = param.get("origin");
        String destination = param.get("destination");
        int daysToDeparture = Integer.parseInt(param.get("daysToDeparture"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDeparture);
        int totalPassenger = Integer.parseInt(param.get("totalPassenger"));

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        BusSearchPageWeb<?> busSearchPage = BusSearchPageWeb.getInstance(session);
        BusSearchResultPageWeb<?> busSearchResultPage = BusSearchResultPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as guest");
        loginPage.loginAsGuest();

        step("User select IDR currency");
        homePage.selectCurrency("IDR");

        step("User open bus vertical");
        homePage.openVerticalPageViaURL("bus-travel");

        step("User click skip button");
        busSearchPage.clickSkipButton();

        step("User select oneway");
        busSearchPage.selectType("oneway");

        step("User select origin location");
        busSearchPage.clickOriginLocationInput();
        busSearchPage.selectLocation(origin);

        step("User select destination location");
        busSearchPage.clickDestinationLocationInput();
        busSearchPage.selectLocation(destination);

        step("User select date");
        busSearchPage.selectDate(departureDate);

        step("User select passenger");
        busSearchPage.clickPassengerInput();
        busSearchPage.setPassenger(totalPassenger);

        step("User search bus");
        busSearchPage.searchBus();

        step("User click skip button");
        busSearchPage.clickSkipButton();

        step("User open filter and sort in SRP");
        busSearchResultPage.openFilterSort();

        step("User select sort by lowest price");
        Assertion.assertThat("Verify SRP is sorted by lowest price", busSearchResultPage.sortByLowestPrice(), is(true));
    }

    @Author(names = "Okta")
    @Module(name = "SRP")
    @Tribe(name = "Bus")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyBusSortLoginWeb() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testVerifyBusSortLoginWeb");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));

        String origin = param.get("origin");
        String destination = param.get("destination");
        int daysToDeparture = Integer.parseInt(param.get("daysToDeparture"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDeparture);
        int totalPassenger = Integer.parseInt(param.get("totalPassenger"));

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        BusSearchPageWeb<?> busSearchPage = BusSearchPageWeb.getInstance(session);
        BusSearchResultPageWeb<?> busSearchResultPage = BusSearchResultPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(username, password, otp, retryCounter);

        step("User select IDR currency");
        homePage.selectCurrency("IDR");

        step("User open bus vertical");
        homePage.openVerticalPageViaURL("bus-travel");

        step("User click skip button");
        busSearchPage.clickSkipButton();

        step("User select oneway");
        busSearchPage.selectType("oneway");

        step("User select origin location");
        busSearchPage.clickOriginLocationInput();
        busSearchPage.selectLocation(origin);

        step("User select destination location");
        busSearchPage.clickDestinationLocationInput();
        busSearchPage.selectLocation(destination);

        step("User select date");
        busSearchPage.selectDate(departureDate);

        step("User select passenger");
        busSearchPage.clickPassengerInput();
        busSearchPage.setPassenger(totalPassenger);

        step("User search bus");
        busSearchPage.searchBus();

        step("User click skip button");
        busSearchPage.clickSkipButton();

        step("User open filter and sort in SRP");
        busSearchResultPage.openFilterSort();

        step("User select sort by lowest price");
        Assertion.assertThat("Verify SRP is sorted by lowest price", busSearchResultPage.sortByLowestPrice(), is(true));
    }

    @Author(names = "Okta")
    @Module(name = "SRP")
    @Tribe(name = "Bus")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyBusFilterGuestWeb() throws InterruptedException {
        Map<String, String> param = getParam("testVerifyBusFilterGuestWeb");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String origin = param.get("origin");
        String destination = param.get("destination");
        int daysToDeparture = Integer.parseInt(param.get("daysToDeparture"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDeparture);
        int totalPassenger = Integer.parseInt(param.get("totalPassenger"));

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        BusSearchPageWeb<?> busSearchPage = BusSearchPageWeb.getInstance(session);
        BusSearchResultPageWeb<?> busSearchResultPage = BusSearchResultPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as guest");
        loginPage.loginAsGuest();

        step("User select IDR currency");
        homePage.selectCurrency("IDR");

        step("User open bus vertical");
        homePage.openVerticalPageViaURL("bus-travel");

        step("User click skip button");
        busSearchPage.clickSkipButton();

        step("User select oneway");
        busSearchPage.selectType("oneway");

        step("User select origin location");
        busSearchPage.clickOriginLocationInput();
        busSearchPage.selectLocation(origin);

        step("User select destination location");
        busSearchPage.clickDestinationLocationInput();
        busSearchPage.selectLocation(destination);

        step("User select date");
        busSearchPage.selectDate(departureDate);

        step("User select passenger");
        busSearchPage.clickPassengerInput();
        busSearchPage.setPassenger(totalPassenger);

        step("User search bus");
        busSearchPage.searchBus();

        step("User skip coachmark on SRP");
        busSearchPage.clickSkipButton();

        step("User open filter and sort in SRP");
        busSearchResultPage.openFilterSort();

        step("User select filter by executive class");
        busSearchResultPage.selectFilterByExecutiveClass();

        step("User select filter by executive class");
        Assertion.assertThat("Verify SRP is filtered by class", busSearchResultPage.verifyFilterByExecutiveClass(), is(true));
    }

    @Author(names = "Okta")
    @Module(name = "SRP")
    @Tribe(name = "Bus")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyBusFilterLoginWeb() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testVerifyBusFilterLoginWeb");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));

        String origin = param.get("origin");
        String destination = param.get("destination");
        int daysToDeparture = Integer.parseInt(param.get("daysToDeparture"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDeparture);
        int totalPassenger = Integer.parseInt(param.get("totalPassenger"));

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        BusSearchPageWeb<?> busSearchPage = BusSearchPageWeb.getInstance(session);
        BusSearchResultPageWeb<?> busSearchResultPage = BusSearchResultPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(username, password, otp, retryCounter);

        step("User select IDR currency");
        homePage.selectCurrency("IDR");

        step("User open bus vertical");
        homePage.openVerticalPageViaURL("bus-travel");

        step("User click skip button");
        busSearchPage.clickSkipButton();

        step("User select oneway");
        busSearchPage.selectType("oneway");

        step("User select origin location");
        busSearchPage.clickOriginLocationInput();
        busSearchPage.selectLocation(origin);

        step("User select destination location");
        busSearchPage.clickDestinationLocationInput();
        busSearchPage.selectLocation(destination);

        step("User select date");
        busSearchPage.selectDate(departureDate);

        step("User select passenger");
        busSearchPage.clickPassengerInput();
        busSearchPage.setPassenger(totalPassenger);

        step("User search bus");
        busSearchPage.searchBus();

        step("User skip coachmark on SRP");
        busSearchPage.clickSkipButton();

        step("User open filter and sort in SRP");
        busSearchResultPage.openFilterSort();

        step("User select filter by executive class");
        busSearchResultPage.selectFilterByExecutiveClass();

        step("User select filter by executive class");
        Assertion.assertThat("Verify SRP is filtered by class", busSearchResultPage.verifyFilterByExecutiveClass(), is(true));
    }

    @Author(names = "Areeb")
    @Module(name = "SearchPage")
    @Tribe(name = "Bus")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyBusDestinationLocationGuestWeb() throws InterruptedException {
        Map<String, String> param = getParam("testVerifyBusDestinationLocationGuestWeb");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String destination = param.get("destination");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        BusSearchPageWeb<?> busSearchPage = BusSearchPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as guest");
        loginPage.loginAsGuest();

        step("User select IDR currency");
        homePage.selectCurrency("IDR");

        step("User open bus vertical");
        homePage.openVerticalPageViaURL("bus-travel");

        step("User click skip button");
        busSearchPage.clickSkipButton();

        step("User select destination location");
        busSearchPage.clickDestinationLocationInput();
        busSearchPage.selectLocation(destination);
        Assertion.assertThat("Verify destination location is selected", busSearchPage.isLocationSelected(destination), is(true));
    }

    @Author(names = "Vipul Kumar")
    @Module(name = "SearchPage")
    @Tribe(name = "Bus")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyBusOriginLocationGuestWeb() {
        Map<String, String> param = getParam("testVerifyBusOriginLocationGuestWeb");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String origin = param.get("origin");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        BusSearchPageWeb<?> busSearchPage = BusSearchPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as guest");
        loginPage.loginAsGuest();

        step("User select IDR currency");
        homePage.selectCurrency("IDR");

        step("User open bus vertical");
        homePage.openVerticalPageViaURL("bus-travel");

        step("User click skip button");
        busSearchPage.clickSkipButton();

        step("User select origin location");
        busSearchPage.clickOriginLocationInput();
        busSearchPage.selectLocation(origin);
        Assertion.assertThat("Verify origin location is selected", busSearchPage.isLocationSelected(origin), is(true));
    }

    @Author(names = "Hafizh")
    @Module(name = "SearchResultPage")
    @Tribe(name = "Bus")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyBusSearchOnewayGuestWeb() throws InterruptedException {
        Map<String, String> param = getParam("testVerifyBusSearchOnewayGuestWeb");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String type = param.get("type");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);
        String origin = param.get("origin");
        String destination = param.get("destination");
        int passenger = Integer.parseInt(param.get("passenger"));
        String queryParam = param.get("queryParam");
        String passengerSrp = param.get("passengerSrp");
        int passengerSrpInt = Integer.parseInt(param.get("passengerSrp"));

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        BusSearchPageWeb<?> busSearchPage = BusSearchPageWeb.getInstance(session);
        BusSearchResultPageWeb<?> busSearchResultPage = BusSearchResultPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as guest");
        loginPage.loginAsGuest();

        step("User select IDR currency");
        homePage.selectCurrency("IDR");

        step("User open bus vertical");
        homePage.openVerticalPageViaURL("bus-travel");

        step("User click skip button");
        busSearchPage.clickSkipButton();

        step("User select one way trip type");
        busSearchPage.selectType(type);

        step("User select travel date");
        busSearchPage.selectDate(departureDate);

        step("User select origin location");
        busSearchPage.clickOriginLocationInput();
        busSearchPage.selectLocation(origin);

        step("User select destination location");
        busSearchPage.clickDestinationLocationInput();
        busSearchPage.selectLocation(destination);

        step("User select passenger amount");
        busSearchPage.clickPassengerInput();
        busSearchPage.setPassenger(passenger);

        step("User search bus");
        busSearchPage.searchBus();
        busSearchResultPage.editPassengerQueryParam(queryParam, passengerSrp);

        step("User skip coachmark on SRP");
        busSearchPage.clickSkipButton();

        step("User is directed to SRP");
        busSearchResultPage.verifySearchResultIsCorrect(origin, destination, departureDate, null, passengerSrpInt, null);

        step("Verify route has allotment");
        Assertion.assertThat("Route has allotment", busSearchResultPage.isScheduleAvailable(), is(true));
    }

    @Author(names = "Mridul Gupta")
    @Module(name = "CompleteBooking")
    @Tribe(name = "Bus")
    @Vertical(name = "NFT")
    @Test
    public void testCompleteBusBookingOneWayWithRBTestOperatorLoginWeb() throws InterruptedException, FailedLoginException {
        Map<String, String> param = getParam("testCompleteBusBookingOneWayWithRBTestOperatorLoginWeb");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String email = param.get("email");
        String password = param.get("password");
        String currency = param.get("currency");

        String tripType = param.get("tripType");
        String origin = param.get("origin");
        String destination = param.get("destination");
        int daysToDeparture = Integer.parseInt(param.get("daysToDeparture"));
        int totalPassenger = Integer.parseInt(param.get("totalPassenger"));
        String operator = param.get("operator");
        String preferredBusType = param.get("preferredBusType");
        String fallbackBusType = param.get("fallbackBusType");

        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDeparture);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        BusSearchPageWeb<?> busSearchPage = BusSearchPageWeb.getInstance(session);
        BusSearchResultPageWeb<?> busSearchResultPage = BusSearchResultPageWeb.getInstance(session);
        BusPdpPageWeb<?> busPdpPage = BusPdpPageWeb.getInstance(session);
        BusBookingFormPageWeb<?> busBookingFormPage = BusBookingFormPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        CalendarPageWeb<?> calendarPage = CalendarPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open home page as logged in user via cookie");
        loginPage.loginWithSpecificAccountViaCookie(email, password);

        step("User select " + currency + " currency");
        homePage.selectCurrency(currency);

        step("User open Bus & Shuttle landing page via deeplink");
        homePage.openVerticalPageViaURL("bus-travel");

        step("User click skip button");
        busSearchPage.clickSkipButton();

        step("User select " + tripType + " on trip tab");
        busSearchPage.selectType(tripType);

        step("User select departure location: " + origin);
        busSearchPage.clickOriginLocationInput();
        busSearchPage.selectLocation(origin);

        step("User select destination location: " + destination);
        busSearchPage.clickDestinationLocationInput();
        busSearchPage.selectLocation(destination);

        step("User select DoT (next " + daysToDeparture + " days)");
        busSearchPage.clickOnCalendarWidgetToOpen();
        calendarPage.selectDate(departureDate);

        step("User set passenger seat to " + totalPassenger + " pax");
        busSearchPage.clickPassengerInput();
        busSearchPage.setPassenger(totalPassenger);

        step("User tap search button");
        busSearchPage.searchBus();

        step("User choose bus schedule for operator '" + operator + "' (prefer '" + preferredBusType + "', fallback '" + fallbackBusType + "')");
        Assertion.assertThat("Route has allotment", busSearchResultPage.isScheduleAvailable(), is(true));
        busSearchResultPage.selectScheduleByOperatorAndType(operator, preferredBusType);
        busSearchResultPage.selectFirstSrpCard();

        step("User select seat (default) and tap save");
        busPdpPage.selectSeat();

        step("User complete booking form");
        busBookingFormPage.fillPassengerDetail(totalPassenger);
        busBookingFormPage.clickTncCheckBox();

        step("User continue to payment page");
        busBookingFormPage.continueToPaymentPage();

        step("Verify user is redirected to payment page and orderId is generated");
        String orderID = paymentPage.getOrderId();
        Assertion.assertThat("Verify order ID is displayed in payment page", !orderID.isEmpty(), is(true));
    }

    @Author(names = "Mridul Gupta")
    @Module(name = "CompleteBooking")
    @Tribe(name = "Bus")
    @Vertical(name = "NFT")
    @Test
    public void testCompleteBusBookingOneWayWithRBTestOperatorGuestWeb() throws InterruptedException {
        Map<String, String> param = getParam("testCompleteBusBookingOneWayWithRBTestOperatorGuestWeb");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String currency = param.get("currency");
        String tripType = param.get("tripType");
        String origin = param.get("origin");
        String destination = param.get("destination");
        int daysToDeparture = Integer.parseInt(param.get("daysToDeparture"));
        int totalPassenger = Integer.parseInt(param.get("totalPassenger"));
        String operator = param.get("operator");

        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDeparture);

        String fullName = CoreFunctions.randomNameWithoutTest();
        String phoneNumber = param.get("phoneNumber");
        String guestEmail = param.get("email");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        BusSearchPageWeb<?> busSearchPage = BusSearchPageWeb.getInstance(session);
        BusSearchResultPageWeb<?> busSearchResultPage = BusSearchResultPageWeb.getInstance(session);
        BusPdpPageWeb<?> busPdpPage = BusPdpPageWeb.getInstance(session);
        BusBookingFormPageWeb<?> busBookingFormPage = BusBookingFormPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        CalendarPageWeb<?> calendarPage = CalendarPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open home page as guest (non-login)");
        loginPage.loginAsGuest();

        step("User select " + currency + " currency");
        homePage.selectCurrency(currency);

        step("User open Bus & Shuttle landing page via deeplink");
        homePage.openVerticalPageViaURL("bus-travel");

        step("User click skip button");
        busSearchPage.clickSkipButton();

        step("User select " + tripType + " on trip tab");
        busSearchPage.selectType(tripType);

        step("User select departure location: " + origin);
        busSearchPage.clickOriginLocationInput();
        busSearchPage.selectLocation(origin);

        step("User select destination location: " + destination);
        busSearchPage.clickDestinationLocationInput();
        busSearchPage.selectLocation(destination);

        step("User select DoT (next " + daysToDeparture + " days)");
        busSearchPage.clickOnCalendarWidgetToOpen();
        calendarPage.selectDate(departureDate);

        step("User set passenger seat to " + totalPassenger + " pax");
        busSearchPage.clickPassengerInput();
        busSearchPage.setPassenger(totalPassenger);

        step("User tap search button");
        busSearchPage.searchBus();

        step("User choose available bus schedule for operator '" + operator + "'");
        Assertion.assertThat("Route has allotment", busSearchResultPage.isScheduleAvailable(), is(true));
        if (!busSearchResultPage.selectScheduleByOperator(operator)) {
            busSearchResultPage.selectFirstSrpCard();
        }

        step("User select seat (default) and tap save");
        busPdpPage.selectSeat();

        step("User complete booking form (contact + passenger details)");
        busBookingFormPage.fillContactDetail(fullName, phoneNumber, guestEmail);
        busBookingFormPage.fillPassengerDetail(totalPassenger);
        busBookingFormPage.clickTncCheckBox();

        step("User continue to payment page");
        busBookingFormPage.continueToPaymentPage();

        step("Verify user is redirected to payment page and orderId is generated");
        String orderID = paymentPage.getOrderId();
        Assertion.assertThat("Verify order ID is displayed in payment page", !orderID.isEmpty(), is(true));
    }

    @Author(names = "Mridul Gupta")
    @Module(name = "CompleteBooking")
    @Tribe(name = "Bus")
    @Vertical(name = "NFT")
    @Test
    public void testCompleteBusBookingRoundTripWithRBTestOperatorLoginWeb() throws InterruptedException, FailedLoginException {
        Map<String, String> param = getParam("testCompleteBusBookingRoundTripWithRBTestOperatorLoginWeb");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String email = param.get("email");
        String password = param.get("password");
        String currency = param.get("currency");

        String tripType = param.get("tripType");
        String origin = param.get("origin");
        String destination = param.get("destination");
        int daysToDeparture = Integer.parseInt(param.get("daysToDeparture"));
        int duration = Integer.parseInt(param.get("duration"));
        int totalPassenger = Integer.parseInt(param.get("totalPassenger"));
        String operator = param.get("operator");
        String preferredBusType = param.get("preferredBusType");
        String fallbackBusType = param.get("fallbackBusType");

        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDeparture);
        String returnDate = CoreFunctions.addToLongDate(departureDate, duration);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        BusSearchPageWeb<?> busSearchPage = BusSearchPageWeb.getInstance(session);
        BusSearchResultPageWeb<?> busSearchResultPage = BusSearchResultPageWeb.getInstance(session);
        BusPdpPageWeb<?> busPdpPage = BusPdpPageWeb.getInstance(session);
        BusBookingFormPageWeb<?> busBookingFormPage = BusBookingFormPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        CalendarPageWeb<?> calendarPage = CalendarPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open home page as logged in user via cookie");
        loginPage.loginWithSpecificAccountViaCookie(email, password);

        step("User select " + currency + " currency");
        homePage.selectCurrency(currency);

        step("User open Bus & Shuttle landing page via deeplink");
        homePage.openVerticalPageViaURL("bus-travel");

        step("User click skip button");
        busSearchPage.clickSkipButton();

        step("User select " + tripType + " on trip tab");
        busSearchPage.selectType(tripType);

        step("User select departure location: " + origin);
        busSearchPage.clickOriginLocationInput();
        busSearchPage.selectLocation(origin);

        step("User select destination location: " + destination);
        busSearchPage.clickDestinationLocationInput();
        busSearchPage.selectLocation(destination);

        step("User select DoT (depart in " + daysToDeparture + " days, return after " + duration + " day)");
        busSearchPage.clickOnCalendarWidgetToOpen();
        calendarPage.selectDate(departureDate, returnDate);

        step("User set passenger seat to " + totalPassenger + " pax");
        busSearchPage.clickPassengerInput();
        busSearchPage.setPassenger(totalPassenger);

        step("User tap search button");
        busSearchPage.searchBus();

        step("User choose departure bus schedule for operator '" + operator + "' (prefer '" + preferredBusType + "', fallback '" + fallbackBusType + "')");
        Assertion.assertThat("Departure route has allotment", busSearchResultPage.isScheduleAvailable(), is(true));
        if (!busSearchResultPage.selectScheduleByOperatorAndType(operator, preferredBusType)
                && !busSearchResultPage.selectScheduleByOperatorAndType(operator, fallbackBusType)
                && !busSearchResultPage.selectScheduleByOperator(operator)) {
            busSearchResultPage.selectFirstSrpCard();
        }

        step("User select seat for departure bus");
        busPdpPage.selectSeat();

        step("User choose return bus schedule for operator '" + operator + "' (prefer '" + preferredBusType + "', fallback '" + fallbackBusType + "')");
        Assertion.assertThat("Return route has allotment", busSearchResultPage.isScheduleAvailable(), is(true));
        if (!busSearchResultPage.selectScheduleByOperatorAndType(operator, preferredBusType)
                && !busSearchResultPage.selectScheduleByOperatorAndType(operator, fallbackBusType)
                && !busSearchResultPage.selectScheduleByOperator(operator)) {
            busSearchResultPage.selectFirstSrpCard();
        }

        step("User select seat for return bus");
        busPdpPage.selectSeat();

        step("User complete booking form");
        busBookingFormPage.fillPassengerDetail(totalPassenger);
        busBookingFormPage.clickTncCheckBox();

        step("User continue to payment page");
        busBookingFormPage.continueToPaymentPage();

        step("Verify user is redirected to payment page and orderId is generated");
        String orderID = paymentPage.getOrderId();
        Assertion.assertThat("Verify order ID is displayed in payment page", !orderID.isEmpty(), is(true));
    }
}