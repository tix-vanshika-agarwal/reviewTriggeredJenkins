package com.tiket.web.test.nft.airport_transfer;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Environment;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.testbase.Configs;
import com.tiket.web.WebTest;
import com.tiket.web.page.common.calendar.CalendarPageWeb;
import com.tiket.web.page.nft.airport_transfer.booking_page.AirportTransferBookingPageWeb;
import com.tiket.web.page.nft.airport_transfer.landing_page.AirportTransferLandingPageWeb;
import com.tiket.web.page.nft.airport_transfer.pdp.AirportTransferPDPWeb;
import com.tiket.web.page.nft.airport_transfer.srp.AirportTransferSRPWeb;
import com.tiket.web.page.order.OrderDetail.OrderDetailWeb;
import com.tiket.web.page.order.OrderList.OrderListWeb;
import com.tiket.web.page.common.home.HomePageWeb;
import com.tiket.web.page.common.home.LoginPageWeb;
import com.tiket.web.page.platform.payment.PaymentPageWeb;
import org.testng.annotations.Test;

import javax.security.auth.login.FailedLoginException;
import java.util.Map;

import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.core.Is.is;

public class AirportTransferTest extends WebTest {
    @Author(names = "Vipul Kumar")
    @Module(name = "HomePageWeb")
    @Tribe(name = "AirportTransfer")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyAirportTransferIsStillDisplayedNonIDRGuestWeb() {
        Map<String, String> param = getParam("testVerifyAirportTransferIsStillDisplayedNonIDRGuestWeb");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String currency = param.get("currency");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open home page as guest");
        loginPage.loginAsGuest();

        step("User change to NON-IDR currency");
        homePage.selectCurrency(currency);

        step("User is redirected back to home page");
        Assertion.assertThat("Verify Airport Pickups is still displayed on home page", homePage.verifyVerticalIsDisplayed("Airport"), is(true));
    }

    @Author(names = "Vipul Kumar")
    @Module(name = "HomePageWeb")
    @Tribe(name = "AirportTransfer")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyAirportTransferIsStillDisplayedNonIDRLoginWeb() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testVerifyAirportTransferIsStillDisplayedNonIDRLoginWeb");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String currency = param.get("currency");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open home page as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User change to NON-IDR currency");
        homePage.selectCurrency(currency);

        step("User is redirected back to home page");
        Assertion.assertThat("Verify Airport Pickups is still displayed on home page", homePage.verifyVerticalIsDisplayed("Airport"), is(true));
    }

    @Author(names = "Vipul Kumar")
    @Module(name = "SRP")
    @Tribe(name = "AirportTransfer")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyAirportTransferRecentlySearchedGuestWeb() throws InterruptedException {
        Map<String, String> param = getParam("testVerifyAirportTransferRecentlySearchedGuestWeb");
        BaseTest.paramsThreadLocal.set(param);

        String location = param.get("location");
        String destination = param.get("destination");
        String currency = param.get("currency");
        String type = param.get("type");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);
        String selectTime = param.get("selectTime");
        String passengerCount = param.get("passengerCount");

        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        AirportTransferLandingPageWeb<?> airportTransferLandingPageWeb = AirportTransferLandingPageWeb.getInstance(session);
        AirportTransferSRPWeb<?> airportTransferSearchPageWeb = AirportTransferSRPWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open home page as guest");
        loginPage.loginAsGuest();

        step("User change to " + currency + " currency");
        homePage.selectCurrency(currency);

        step("User open Airport Pickups vertical");
        homePage.openVerticalPageViaURL("airport-transfer");

        step("User click on Skip pop Up");
        airportTransferLandingPageWeb.closePopup();

        step("User selects Airport transfer type: " + type);
        airportTransferLandingPageWeb.selectTransferType(type);

        step("User fill the Airport and destination locations based on transfer type dynamically");
        airportTransferLandingPageWeb.fillTransferFields(type, location, destination);

        step("User select date and Time");
        airportTransferLandingPageWeb.selectDate(departureDate);
        airportTransferLandingPageWeb.selectTime(selectTime);

        step("User select Passenger count");
        airportTransferLandingPageWeb.selectPassenger(passengerCount);

        step("User searches the cars");
        airportTransferLandingPageWeb.seeCarListOnSRP();
        Assertion.assertThat("Verify the car rental card is displayed or not", airportTransferSearchPageWeb.checkCarRentalCard(), is(true));

        step("User Go Back to Airport SRP");
        airportTransferSearchPageWeb.goBackToLandingPage();
        Assertion.assertThat("Verify the recently searched is present", airportTransferSearchPageWeb.recentlySearched(), is(true));

        step("User select recently searched card to again see the SRP page & check car rental card is displayed or not");
        airportTransferSearchPageWeb.selectRecentlySearchedCard();
        Assertion.assertThat("Verify the car rental card is displayed or not", airportTransferSearchPageWeb.checkCarRentalCard(), is(true));
    }

    @Author(names = "Vipul Kumar")
    @Module(name = "SRP")
    @Tribe(name = "AirportTransfer")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyAirportTransferRecentlySearchedLoginWeb() throws InterruptedException, FailedLoginException {
        Map<String, String> param = getParam("testVerifyAirportTransferRecentlySearchedLoginWeb");
        BaseTest.paramsThreadLocal.set(param);

        String location = param.get("location");
        String destination = param.get("destination");
        String currency = param.get("currency");
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String type = param.get("type");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);
        String selectTime = param.get("selectTime");
        String passengerCount = param.get("passengerCount");

        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        AirportTransferLandingPageWeb<?> airportTransferLandingPageWeb = AirportTransferLandingPageWeb.getInstance(session);
        AirportTransferSRPWeb<?> airportTransferSearchPageWeb = AirportTransferSRPWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open home page as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User change to " + currency + " currency");
        homePage.selectCurrency(currency);

        step("User open Airport Pickups vertical");
        homePage.openVerticalPageViaURL("airport-transfer");

        step("User click on Skip pop Up");
        airportTransferLandingPageWeb.closePopup();

        step("User selects Airport transfer type: " + type);
        airportTransferLandingPageWeb.selectTransferType(type);

        step("User fill the Airport and destination locations based on transfer type dynamically");
        airportTransferLandingPageWeb.fillTransferFields(type, location, destination);

        step("User select date and Time");
        airportTransferLandingPageWeb.selectDate(departureDate);
        airportTransferLandingPageWeb.selectTime(selectTime);

        step("User select Passenger count");
        airportTransferLandingPageWeb.selectPassenger(passengerCount);

        step("User searches the cars");
        airportTransferLandingPageWeb.seeCarListOnSRP();
        Assertion.assertThat("Verify the car rental card is displayed or not", airportTransferSearchPageWeb.checkCarRentalCard(), is(true));

        step("User Go Back to Airport landing Search page");
        airportTransferSearchPageWeb.goBackToLandingPage();
        Assertion.assertThat("Verify the recently searched is present", airportTransferSearchPageWeb.recentlySearched(), is(true));

        step("User select recently searched card to again see the SRP page & check car rental card is displayed or not");
        airportTransferSearchPageWeb.selectRecentlySearchedCard();
        Assertion.assertThat("Verify the car rental card is displayed or not", airportTransferSearchPageWeb.checkCarRentalCard(), is(true));
    }

    @Author(names = {"Vipul Kumar", "Eka"})
    @Module(name = "SearchLandingPage")
    @Tribe(name = "AirportTransfer")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyAirportTransferCheckAirportGuestWeb() {
        Map<String, String> param = getParam("testVerifyAirportTransferCheckAirportGuestWeb");
        BaseTest.paramsThreadLocal.set(param);

        String currency = param.get("currency");
        String type = param.get("type");

        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        AirportTransferLandingPageWeb<?> airportTransferLandingPageWeb = AirportTransferLandingPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open home page as guest");
        loginPage.loginAsGuest();

        step("User change to " + currency + " currency");
        homePage.selectCurrency(currency);

        step("User open Airport Pickups vertical");
        homePage.openVerticalPageViaURL("airport-transfer");

        step("User click on Skip pop Up");
        airportTransferLandingPageWeb.closePopup();

        step("User selects Airport transfer type: " + type);
        airportTransferLandingPageWeb.selectTransferType(type);

        step("User select airport to set");
        airportTransferLandingPageWeb.setSelectAirport(type);
        Assertion.assertThat("Verify popular airport lists are displayed or not", airportTransferLandingPageWeb.checkPopularAirportList(), is(true));
    }

    @Author(names = {"Vipul Kumar", "Eka"})
    @Module(name = "SearchLandingPage")
    @Tribe(name = "AirportTransfer")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyAirportTransferCheckAirportLoginWeb() throws InterruptedException, FailedLoginException {
        Map<String, String> param = getParam("testVerifyAirportTransferCheckAirportLoginWeb");
        BaseTest.paramsThreadLocal.set(param);

        String currency = param.get("currency");
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));

        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        AirportTransferLandingPageWeb<?> airportTransferLandingPageWeb = AirportTransferLandingPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open home page as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User change to " + currency + " currency");
        homePage.selectCurrency(currency);

        step("User open Airport Pickups vertical");
        homePage.openVerticalPageViaURL("airport-transfer");

        step("User click on Skip pop Up");
        airportTransferLandingPageWeb.closePopup();

        step("User select airport to set");
        airportTransferLandingPageWeb.setSelectAirport("transfer_out");
        Assertion.assertThat("Verify popular airport lists are displayed or not", airportTransferLandingPageWeb.checkPopularAirportList(), is(true));
    }

    @Author(names = {"Vipul Kumar", "Eka"})
    @Module(name = "SearchLandingPage")
    @Tribe(name = "AirportTransfer")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyAirportTransferCheckDestinationGuestWeb() {
        Map<String, String> param = getParam("testVerifyAirportTransferCheckDestinationGuestWeb");
        BaseTest.paramsThreadLocal.set(param);

        String location = param.get("location");
        String currency = param.get("currency");
        String type = param.get("type");

        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        AirportTransferLandingPageWeb<?> airportTransferLandingPageWeb = AirportTransferLandingPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open home page as guest");
        loginPage.loginAsGuest();

        step("User change to " + currency + " currency");
        homePage.selectCurrency(currency);

        step("User open Airport Pickups vertical");
        homePage.openVerticalPageViaURL("airport-transfer");

        step("User click on Skip pop Up");
        airportTransferLandingPageWeb.closePopup();

        step("User selects Airport transfer type: " + type);
        airportTransferLandingPageWeb.selectTransferType(type);

        step("User set the Airport and destination locations");
        airportTransferLandingPageWeb.setSelectAirport(type);
        airportTransferLandingPageWeb.setInputAirport(location);
        airportTransferLandingPageWeb.setSelectDestination(type);
        Assertion.assertThat("Verify popular destination cities lists are displayed or not", airportTransferLandingPageWeb.checkPopularDestinationList(), is(true));
    }

    @Author(names = {"Vipul Kumar", "Eka"})
    @Module(name = "SearchLandingPage")
    @Tribe(name = "AirportTransfer")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyAirportTransferCheckDestinationLoginWeb() throws InterruptedException, FailedLoginException {
        Map<String, String> param = getParam("testVerifyAirportTransferCheckDestinationLoginWeb");
        BaseTest.paramsThreadLocal.set(param);

        String location = param.get("location");
        String currency = param.get("currency");
        String type = param.get("type");
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));

        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        AirportTransferLandingPageWeb<?> airportTransferLandingPageWeb = AirportTransferLandingPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open home page as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User change to " + currency + " currency");
        homePage.selectCurrency(currency);

        step("User open Airport Pickups vertical");
        homePage.openVerticalPageViaURL("airport-transfer");

        step("User click on Skip pop Up");
        airportTransferLandingPageWeb.closePopup();

        step("User selects Airport transfer type: " + type);
        airportTransferLandingPageWeb.selectTransferType(type);

        step("User set the Airport and destination locations");
        airportTransferLandingPageWeb.setSelectAirport(type);
        airportTransferLandingPageWeb.setInputAirport(location);
        airportTransferLandingPageWeb.setSelectDestination(type);
        Assertion.assertThat("Verify popular destination cities lists are displayed or not", airportTransferLandingPageWeb.checkPopularDestinationList(), is(true));
    }

    @Author(names = {"Vipul Kumar", "Eka"})
    @Module(name = "CompleteBooking")
    @Tribe(name = "AirportTransfer")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyAirportTransferPriceWithCurrencyNonIDRGuestWeb() {
        Map<String, String> param = getParam("testVerifyAirportTransferPriceWithCurrencyNonIDRGuestWeb");
        BaseTest.paramsThreadLocal.set(param);

        String location = param.get("location");
        String destination = param.get("destination");
        String currency = param.get("currency");
        String type = param.get("type");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);
        String selectTime = param.get("selectTime");
        String passengerCount = param.get("passengerCount");

        String fullName = CoreFunctions.randomName();
        String phoneNumber = "91" + CoreFunctions.randomInt(8);
        String email = CoreFunctions.randomEmail();

        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        AirportTransferLandingPageWeb<?> airportTransferLandingPageWeb = AirportTransferLandingPageWeb.getInstance(session);
        AirportTransferSRPWeb<?> airportTransferSearchPageWeb = AirportTransferSRPWeb.getInstance(session);
        AirportTransferPDPWeb<?> airportTransferProductPageWeb = AirportTransferPDPWeb.getInstance(session);
        AirportTransferBookingPageWeb<?> airportTransferBookingPageWeb = AirportTransferBookingPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open home page as guest");
        loginPage.loginAsGuest();

        step("User change to " + currency + " currency");
        homePage.selectCurrency(currency);

        step("User open Airport Pickups vertical via deeplink");
        homePage.openVerticalPageViaURL("airport-transfer");

        step("User click on Skip pop Up");
        airportTransferLandingPageWeb.closePopup();

        step("User selects Airport transfer type: " + type);
        airportTransferLandingPageWeb.selectTransferType(type);

        step("User fill the Airport and destination locations based on transfer type dynamically");
        airportTransferLandingPageWeb.fillTransferFields(type, location, destination);

        step("User select date and Time");
        airportTransferLandingPageWeb.selectDate(departureDate);
        airportTransferLandingPageWeb.selectTime(selectTime);

        step("User select Passenger count");
        airportTransferLandingPageWeb.selectPassenger(passengerCount);

        step("User searches the cars");
        airportTransferLandingPageWeb.seeCarListOnSRP();
        Assertion.assertThat("Verify the car rental card is displayed or not", airportTransferSearchPageWeb.checkCarRentalCard(), is(true));
        airportTransferSearchPageWeb.verifySRPPriceInIDR();
        step("User select first SRP card & go to PDP");
        airportTransferSearchPageWeb.selectFirstSRPCard();

        step("User Go to Booking Page");
        airportTransferProductPageWeb.proceedToBookingPage();

        step("User input contact detail");
        airportTransferBookingPageWeb.fillContactDetail(fullName, phoneNumber, email);

        step("User Go to Payment Page");
        airportTransferBookingPageWeb.checkBookingContactDetailsCard();
        airportTransferBookingPageWeb.goToPaymentPage();
        String orderID = paymentPage.getOrderId();
        Assertion.assertThat("Verify order ID is displayed in payment page", !orderID.isEmpty(), is(true));
    }

    @Author(names = {"Vipul Kumar", "Eka"})
    @Module(name = "CompleteBooking")
    @Tribe(name = "AirportTransfer")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyAirportTransferPriceWithCurrencyNonIDRLoginWeb() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testVerifyAirportTransferPriceWithCurrencyNonIDRLoginWeb");
        BaseTest.paramsThreadLocal.set(param);

        String location = param.get("location");
        String destination = param.get("destination");
        String currency = param.get("currency");
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String type = param.get("type");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);
        String selectTime = param.get("selectTime");
        String passengerCount = param.get("passengerCount");

        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        AirportTransferLandingPageWeb<?> airportTransferLandingPageWeb = AirportTransferLandingPageWeb.getInstance(session);
        AirportTransferSRPWeb<?> airportTransferSearchPageWeb = AirportTransferSRPWeb.getInstance(session);
        AirportTransferPDPWeb<?> airportTransferProductPageWeb = AirportTransferPDPWeb.getInstance(session);
        AirportTransferBookingPageWeb<?> airportTransferBookingPageWeb = AirportTransferBookingPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open home page as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User change to " + currency + " currency");
        homePage.selectCurrency(currency);

        step("User open Airport Pickups vertical via deeplink");
        homePage.openVerticalPageViaURL("airport-transfer");

        step("User click on Skip pop Up");
        airportTransferLandingPageWeb.closePopup();

        step("User selects Airport transfer type: " + type);
        airportTransferLandingPageWeb.selectTransferType(type);

        step("User fill the Airport and destination locations based on transfer type dynamically");
        airportTransferLandingPageWeb.fillTransferFields(type, location, destination);

        step("User select date and Time");
        airportTransferLandingPageWeb.selectDate(departureDate);
        airportTransferLandingPageWeb.selectTime(selectTime);

        step("User select Passenger count");
        airportTransferLandingPageWeb.selectPassenger(passengerCount);

        step("User searches the cars");
        airportTransferLandingPageWeb.seeCarListOnSRP();
        Assertion.assertThat("Verify the car rental card is displayed or not", airportTransferSearchPageWeb.checkCarRentalCard(), is(true));
        airportTransferSearchPageWeb.verifySRPPriceInIDR();

        step("User select first SRP card & go to PDP");
        airportTransferSearchPageWeb.selectFirstSRPCard();

        step("User Go to Booking Page");
        airportTransferProductPageWeb.proceedToBookingPage();

        step("User Go to Payment Page");
        airportTransferBookingPageWeb.checkBookingContactDetailsCard();
        airportTransferBookingPageWeb.goToPaymentPage();
        String orderID = paymentPage.getOrderId();
        Assertion.assertThat("Verify order ID is displayed in payment page", !orderID.isEmpty(), is(true));
    }

    @Author(names = {"Vipul Kumar", "Eka"})
    @Module(name = "BookingForm")
    @Tribe(name = "AirportTransfer")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyAirportTransferTripDetailOnBookingFormGuestWeb() throws InterruptedException {
        Map<String, String> param = getParam("testVerifyAirportTransferTripDetailOnBookingFormGuestWeb");
        BaseTest.paramsThreadLocal.set(param);

        String location = param.get("location");
        String destination = param.get("destination");
        String currency = param.get("currency");
        String type = param.get("type");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);
        String selectTime = param.get("selectTime");
        String passengerCount = param.get("passengerCount");

        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        AirportTransferLandingPageWeb<?> airportTransferLandingPageWeb = AirportTransferLandingPageWeb.getInstance(session);
        AirportTransferSRPWeb<?> airportTransferSearchPageWeb = AirportTransferSRPWeb.getInstance(session);
        AirportTransferPDPWeb<?> airportTransferProductPageWeb = AirportTransferPDPWeb.getInstance(session);
        AirportTransferBookingPageWeb<?> airportTransferBookingPageWeb = AirportTransferBookingPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open home page as guest");
        loginPage.loginAsGuest();

        step("User change to " + currency + " currency");
        homePage.selectCurrency(currency);

        step("User open Airport Pickups vertical");
        homePage.openVerticalPageViaURL("airport-transfer");

        step("User click on Skip pop Up");
        airportTransferLandingPageWeb.closePopup();

        step("User selects Airport transfer type: " + type);
        airportTransferLandingPageWeb.selectTransferType(type);

        step("User fill the Airport and destination locations based on transfer type");
        Map<String, String> locationMap = airportTransferLandingPageWeb.fillTransferFields(type, location, destination);
        String airportLocationName = locationMap.get("airport");
        String DestinationLocationName = locationMap.get("destination");

        step("User select date and Time");
        airportTransferLandingPageWeb.selectDate(departureDate);
        airportTransferLandingPageWeb.selectTime(selectTime);

        step("User select Passenger count");
        airportTransferLandingPageWeb.selectPassenger(passengerCount);

        step("User searches the cars");
        airportTransferLandingPageWeb.seeCarListOnSRP();
        Assertion.assertThat("Verify the car rental card is displayed or not", airportTransferSearchPageWeb.checkCarRentalCard(), is(true));
        airportTransferSearchPageWeb.verifySRPPriceInIDR();
        step("User select first SRP card & go to PDP");
        airportTransferSearchPageWeb.selectFirstSRPCard();

        step("User Go to Booking Page");
        airportTransferProductPageWeb.proceedToBookingPage();
        airportTransferBookingPageWeb.checkBookingContactDetailsCard();
        airportTransferBookingPageWeb.openPriceBottomSheet();
        Assertion.assertThat("Verify Airport Location Name is same as Landing search page", airportTransferBookingPageWeb.checkForAirportLocationName(), containsString(airportLocationName));
        Assertion.assertThat("Verify Destination Location Name is same as Landing search page", airportTransferBookingPageWeb.checkForDestinationLocationName(), containsString(DestinationLocationName));
    }

    @Author(names = {"Vipul Kumar", "Eka"})
    @Module(name = "BookingForm")
    @Tribe(name = "AirportTransfer")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyAirportTransferTripDetailOnBookingFormLoginWeb() throws InterruptedException, FailedLoginException {
        Map<String, String> param = getParam("testVerifyAirportTransferTripDetailOnBookingFormLoginWeb");
        BaseTest.paramsThreadLocal.set(param);

        String location = param.get("location");
        String destination = param.get("destination");
        String currency = param.get("currency");
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String type = param.get("type");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);
        String selectTime = param.get("selectTime");
        String passengerCount = param.get("passengerCount");

        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        AirportTransferLandingPageWeb<?> airportTransferLandingPageWeb = AirportTransferLandingPageWeb.getInstance(session);
        AirportTransferSRPWeb<?> airportTransferSearchPageWeb = AirportTransferSRPWeb.getInstance(session);
        AirportTransferPDPWeb<?> airportTransferProductPageWeb = AirportTransferPDPWeb.getInstance(session);
        AirportTransferBookingPageWeb<?> airportTransferBookingPageWeb = AirportTransferBookingPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open home page as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User change to " + currency + " currency");
        homePage.selectCurrency(currency);

        step("User open Airport Pickups vertical");
        homePage.openVerticalPageViaURL("airport-transfer");

        step("User click on Skip pop Up");
        airportTransferLandingPageWeb.closePopup();

        step("User selects Airport transfer type: " + type);
        airportTransferLandingPageWeb.selectTransferType(type);

        step("User fill the Airport and destination locations based on transfer type");
        Map<String, String> locationMap = airportTransferLandingPageWeb.fillTransferFields(type, location, destination);
        String airportLocationName = locationMap.get("airport");
        String DestinationLocationName = locationMap.get("destination");

        step("User select date and Time");
        airportTransferLandingPageWeb.selectDate(departureDate);
        airportTransferLandingPageWeb.selectTime(selectTime);

        step("User select Passenger count");
        airportTransferLandingPageWeb.selectPassenger(passengerCount);

        step("User searches the cars");
        airportTransferLandingPageWeb.seeCarListOnSRP();
        Assertion.assertThat("Verify the car rental card is displayed or not", airportTransferSearchPageWeb.checkCarRentalCard(), is(true));
        airportTransferSearchPageWeb.verifySRPPriceInIDR();

        step("User select first SRP card & go to PDP");
        airportTransferSearchPageWeb.selectFirstSRPCard();

        step("User Go to Booking Page");
        airportTransferProductPageWeb.proceedToBookingPage();
        airportTransferBookingPageWeb.checkBookingContactDetailsCard();
        airportTransferBookingPageWeb.openPriceBottomSheet();
        Assertion.assertThat("Verify Airport Location Name is same as Landing search page", airportTransferBookingPageWeb.checkForAirportLocationName(), containsString(airportLocationName));
        Assertion.assertThat("Verify Destination Location Name is same as Landing search page", airportTransferBookingPageWeb.checkForDestinationLocationName(), containsString(DestinationLocationName));
    }

    @Author(names = {"Vipul Kumar", "Eka"})
    @Module(name = "BookingForm")
    @Tribe(name = "AirportTransfer")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyAirportTransferPriceDetailOnBookingFormGuestWeb() throws InterruptedException {
        Map<String, String> param = getParam("testVerifyAirportTransferPriceDetailOnBookingFormGuestWeb");
        BaseTest.paramsThreadLocal.set(param);

        String location = param.get("location");
        String destination = param.get("destination");
        String currency = param.get("currency");
        String type = param.get("type");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);
        String selectTime = param.get("selectTime");
        String passengerCount = param.get("passengerCount");

        String fullName = CoreFunctions.randomName();
        String phoneNumber = "91" + CoreFunctions.randomInt(8);
        String email = CoreFunctions.randomEmail();

        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        AirportTransferLandingPageWeb<?> airportTransferLandingPageWeb = AirportTransferLandingPageWeb.getInstance(session);
        AirportTransferSRPWeb<?> airportTransferSearchPageWeb = AirportTransferSRPWeb.getInstance(session);
        AirportTransferPDPWeb<?> airportTransferProductPageWeb = AirportTransferPDPWeb.getInstance(session);
        AirportTransferBookingPageWeb<?> airportTransferBookingPageWeb = AirportTransferBookingPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open home page as guest");
        loginPage.loginAsGuest();

        step("User change to " + currency + " currency");
        homePage.selectCurrency(currency);

        step("User open Airport Pickups vertical");
        homePage.openVerticalPageViaURL("airport-transfer");

        step("User click on Skip pop Up");
        airportTransferLandingPageWeb.closePopup();

        step("User selects Airport transfer type: " + type);
        airportTransferLandingPageWeb.selectTransferType(type);

        step("User fill the Airport and destination locations based on transfer type");
        Map<String, String> locationMap = airportTransferLandingPageWeb.fillTransferFields(type, location, destination);
        String airportLocationName = locationMap.get("airport");
        String DestinationLocationName = locationMap.get("destination");

        step("User select date and Time");
        airportTransferLandingPageWeb.selectDate(departureDate);
        airportTransferLandingPageWeb.selectTime(selectTime);

        step("User select Passenger count");
        airportTransferLandingPageWeb.selectPassenger(passengerCount);

        step("User searches the cars");
        airportTransferLandingPageWeb.seeCarListOnSRP();
        Assertion.assertThat("Verify the car rental card is displayed or not", airportTransferSearchPageWeb.checkCarRentalCard(), is(true));
        airportTransferSearchPageWeb.verifySRPPriceInIDR();

        step("User select first SRP card & go to PDP");
        airportTransferSearchPageWeb.selectFirstSRPCard();

        step("User open price bottom sheet on PDP");
        airportTransferProductPageWeb.openPriceBottomSheet();
        Assertion.assertThat("Verify Airport Location Name On PDP is same as Landing search page", airportTransferProductPageWeb.checkForAirportLocationName(), containsString(airportLocationName));
        Assertion.assertThat("Verify Destination Location Name On PDP is same as Landing search page", airportTransferProductPageWeb.checkForDestinationLocationName(), containsString(DestinationLocationName));
        String ToTalPriceOnPDP = airportTransferProductPageWeb.getTotalPrice();
        step("User Close the Bottom sheet of Total Price On PDP");
        airportTransferProductPageWeb.closePriceBottomSheet();

        step("User Go to Booking Page");
        airportTransferProductPageWeb.proceedToBookingPage();

        step("User input contact detail");
        airportTransferBookingPageWeb.fillContactDetail(fullName, phoneNumber, email);

        step("User open price bottom sheet on Booking Page");
        airportTransferBookingPageWeb.openPriceBottomSheet();
        Assertion.assertThat("Verify Airport Location Name On Booking Page is same as Landing search page", airportTransferBookingPageWeb.checkForAirportLocationName(), containsString(airportLocationName));
        Assertion.assertThat("Verify Destination Location Name On Booking Page is same as Landing search page", airportTransferBookingPageWeb.checkForDestinationLocationName(), containsString(DestinationLocationName));
        Assertion.assertThat("Verify total price on Booking Page is same as on PDP", airportTransferBookingPageWeb.getTotalPrice(), is(ToTalPriceOnPDP));
        airportTransferBookingPageWeb.closePriceBottomSheet();
    }

    @Author(names = {"Vipul Kumar", "Eka"})
    @Module(name = "BookingForm")
    @Tribe(name = "AirportTransfer")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyAirportTransferPriceDetailOnBookingFormLoginWeb() throws InterruptedException, FailedLoginException {
        Map<String, String> param = getParam("testVerifyAirportTransferPriceDetailOnBookingFormLoginWeb");
        BaseTest.paramsThreadLocal.set(param);

        String location = param.get("location");
        String destination = param.get("destination");
        String currency = param.get("currency");
        String type = param.get("type");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);
        String selectTime = param.get("selectTime");
        String passengerCount = param.get("passengerCount");

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));

        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        AirportTransferLandingPageWeb<?> airportTransferLandingPageWeb = AirportTransferLandingPageWeb.getInstance(session);
        AirportTransferSRPWeb<?> airportTransferSearchPageWeb = AirportTransferSRPWeb.getInstance(session);
        AirportTransferPDPWeb<?> airportTransferProductPageWeb = AirportTransferPDPWeb.getInstance(session);
        AirportTransferBookingPageWeb<?> airportTransferBookingPageWeb = AirportTransferBookingPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open home page as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User change to " + currency + " currency");
        homePage.selectCurrency(currency);

        step("User open Airport Pickups vertical");
        homePage.openVerticalPageViaURL("airport-transfer");

        step("User click on Skip pop Up");
        airportTransferLandingPageWeb.closePopup();

        step("User selects Airport transfer type: " + type);
        airportTransferLandingPageWeb.selectTransferType(type);

        step("User fill the Airport and destination locations based on transfer type");
        Map<String, String> locationMap = airportTransferLandingPageWeb.fillTransferFields(type, location, destination);
        String airportLocationName = locationMap.get("airport");
        String DestinationLocationName = locationMap.get("destination");

        step("User select date and Time");
        airportTransferLandingPageWeb.selectDate(departureDate);
        airportTransferLandingPageWeb.selectTime(selectTime);

        step("User select Passenger count");
        airportTransferLandingPageWeb.selectPassenger(passengerCount);

        step("User searches the cars");
        airportTransferLandingPageWeb.seeCarListOnSRP();
        Assertion.assertThat("Verify the car rental card is displayed or not", airportTransferSearchPageWeb.checkCarRentalCard(), is(true));
        airportTransferSearchPageWeb.verifySRPPriceInIDR();

        step("User select first SRP card & go to PDP");
        airportTransferSearchPageWeb.selectFirstSRPCard();

        step("User open price bottom sheet on PDP");
        airportTransferProductPageWeb.openPriceBottomSheet();
        Assertion.assertThat("Verify Airport Location Name On PDP is same as Landing search page", airportTransferProductPageWeb.checkForAirportLocationName(), containsString(airportLocationName));
        Assertion.assertThat("Verify Destination Location Name On PDP is same as Landing search page", airportTransferProductPageWeb.checkForDestinationLocationName(), containsString(DestinationLocationName));
        String ToTalPriceOnPDP = airportTransferProductPageWeb.getTotalPrice();
        step("User Close the Bottom sheet of Total Price On PDP");
        airportTransferProductPageWeb.closePriceBottomSheet();

        step("User Go to Booking Page");
        airportTransferProductPageWeb.proceedToBookingPage();

        step("User open price bottom sheet on Booking Page");
        airportTransferBookingPageWeb.checkBookingContactDetailsCard();
        airportTransferBookingPageWeb.openPriceBottomSheet();
        Assertion.assertThat("Verify Airport Location Name On Booking Page is same as Landing search page", airportTransferBookingPageWeb.checkForAirportLocationName(), containsString(airportLocationName));
        Assertion.assertThat("Verify Destination Location Name On Booking Page is same as Landing search page", airportTransferBookingPageWeb.checkForDestinationLocationName(), containsString(DestinationLocationName));
        Assertion.assertThat("Verify total price on Booking Page is same as on PDP", airportTransferBookingPageWeb.getTotalPrice(), is(ToTalPriceOnPDP));
        airportTransferBookingPageWeb.closePriceBottomSheet();
    }

    @Author(names = {"Vipul Kumar", "Eka"})
    @Module(name = "CompleteBooking")
    @Tribe(name = "AirportTransfer")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyAirportTransferCompleteBookingTransferOutGuestWeb() throws InterruptedException {
        Map<String, String> param = getParam("testVerifyAirportTransferCompleteBookingTransferOutGuestWeb");
        BaseTest.paramsThreadLocal.set(param);

        String location = param.get("location");
        String destination = param.get("destination");
        String currency = param.get("currency");
        String type = param.get("type");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);
        String selectTime = param.get("selectTime");
        String passengerCount = param.get("passengerCount");

        String fullName = CoreFunctions.randomName();
        String phoneNumber = "91" + CoreFunctions.randomInt(8);
        String email = CoreFunctions.randomEmail();
        String paymentMethod = param.get("paymentMethod");
        String paymentGroup = param.get("paymentGroup");

        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        AirportTransferLandingPageWeb<?> airportTransferLandingPageWeb = AirportTransferLandingPageWeb.getInstance(session);
        AirportTransferSRPWeb<?> airportTransferSearchPageWeb = AirportTransferSRPWeb.getInstance(session);
        AirportTransferPDPWeb<?> airportTransferProductPageWeb = AirportTransferPDPWeb.getInstance(session);
        AirportTransferBookingPageWeb<?> airportTransferBookingPageWeb = AirportTransferBookingPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        CalendarPageWeb<?> calendarPage = CalendarPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open home page as guest (non-login)");
        loginPage.loginAsGuest();

        step("User change to " + currency + " currency");
        homePage.selectCurrency(currency);

        step("User open Airport Pickups vertical");
        homePage.openVerticalPageViaURL("airport-transfer");

        step("User click on Skip pop Up");
        airportTransferLandingPageWeb.closePopup();

        step("User selects Airport transfer type: " + type);
        airportTransferLandingPageWeb.selectTransferType(type);

        step("User fill the Airport and destination locations based on transfer type");
        Map<String, String> locationMap = airportTransferLandingPageWeb.fillTransferFields(type, location, destination);
        String airportLocationName = locationMap.get("airport");
        String DestinationLocationName = locationMap.get("destination");

        step("User select date and Time");
        airportTransferLandingPageWeb.clickOnCalendarWidgetToOpen();
        calendarPage.selectDate(departureDate);
        airportTransferLandingPageWeb.selectTime(selectTime);

        step("User select Passenger count");
        airportTransferLandingPageWeb.selectPassenger(passengerCount);

        step("User searches the cars");
        airportTransferLandingPageWeb.seeCarListOnSRP();
        Assertion.assertThat("Verify the car rental card is displayed or not", airportTransferSearchPageWeb.checkCarRentalCard(), is(true));
        airportTransferSearchPageWeb.verifySRPPriceInIDR();

        step("User select first SRP card & go to PDP");
        airportTransferSearchPageWeb.selectFirstSRPCard();

        step("User open price bottom sheet on PDP");
        airportTransferProductPageWeb.openPriceBottomSheet();
        Assertion.assertThat("Verify Airport Location Name On PDP is same as Landing search page", airportTransferProductPageWeb.checkForAirportLocationName(), containsString(airportLocationName));
        Assertion.assertThat("Verify Destination Location Name On PDP is same as Landing search page", airportTransferProductPageWeb.checkForDestinationLocationName(), containsString(DestinationLocationName));
        String ToTalPriceOnPDP = airportTransferProductPageWeb.getTotalPrice();
        step("User Close the Bottom sheet of Total Price On PDP");
        airportTransferProductPageWeb.closePriceBottomSheet();

        step("User Go to Booking Page");
        airportTransferProductPageWeb.proceedToBookingPage();

        step("User input contact detail");
        airportTransferBookingPageWeb.fillContactDetail(fullName, phoneNumber, email);

        step("User open price bottom sheet on Booking Page");
        airportTransferBookingPageWeb.openPriceBottomSheet();
        Assertion.assertThat("Verify Airport Location Name On Booking Page is same as Landing search page", airportTransferBookingPageWeb.checkForAirportLocationName(), containsString(airportLocationName));
        Assertion.assertThat("Verify Destination Location Name On Booking Page is same as Landing search page", airportTransferBookingPageWeb.checkForDestinationLocationName(), containsString(DestinationLocationName));
        Assertion.assertThat("Verify total price on Booking Page is same as on PDP", airportTransferBookingPageWeb.getTotalPrice(), is(ToTalPriceOnPDP));
        airportTransferBookingPageWeb.closePriceBottomSheet();

        step("User Go to Payment Page");
        airportTransferBookingPageWeb.goToPaymentPage();
        String orderID = paymentPage.getOrderId();
        Assertion.assertThat("Verify order ID is displayed in payment page", !orderID.isEmpty(), is(true));

        if (Configs.ENVIRONMENT == Environment.PROD) {
            step("User select payment method ");
            paymentPage.clickOnSelectPaymentMethod();
            paymentPage.goToPaymentMethodsGroup(paymentGroup);
            paymentPage.selectPaymentMethod(paymentMethod);

            step("User complete payment");
            paymentPage.clickOnPaymentButton();

            step("User open order list page");
            paymentPage.clickSeeOrderListButton();
        }

        if (Configs.ENVIRONMENT == Environment.GK || Configs.ENVIRONMENT == Environment.PREPROD) {
            step("User select payment method ");
            paymentPage.clickOnSelectPaymentMethod();
            paymentPage.goToPaymentMethodsGroup(paymentGroup);
            paymentPage.selectPaymentMethod(paymentMethod);

            step("User complete payment");
            paymentPage.clickOnPaymentButton();

            step("User open order detail page");
            orderListPage.openActiveTicketViaURL(orderID);
        }

        Assertion.assertThat("Verify web page will redirect to login page for guest user", loginPage.isLoginPageOpened(), is(true));
    }

    @Author(names = "Mridul Gupta")
    @Module(name = "CompleteBooking")
    @Tribe(name = "AirportTransfer")
    @Vertical(name = "NFT")
    @Test
    public void testCompleteAirportTransferTransferOutFromDPSToPantaiKutaWithMinimumPickupTimeGuestWeb() {
        Map<String, String> param = getParam("testCompleteAirportTransferTransferOutFromDPSToPantaiKutaWithMinimumPickupTimeGuestWeb");
        BaseTest.paramsThreadLocal.set(param);

        String currency = param.get("currency");
        String type = param.get("type");
        String location = param.get("location");
        String destination = param.get("destination");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String selectTime = param.get("selectTime");
        String passengerCount = param.get("passengerCount");

        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);

        String fullName = CoreFunctions.randomName();
        String phoneNumber = "91" + CoreFunctions.randomInt(8);
        String email = CoreFunctions.randomEmail();

        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        AirportTransferLandingPageWeb<?> airportTransferLandingPageWeb = AirportTransferLandingPageWeb.getInstance(session);
        AirportTransferSRPWeb<?> airportTransferSearchPageWeb = AirportTransferSRPWeb.getInstance(session);
        AirportTransferPDPWeb<?> airportTransferProductPageWeb = AirportTransferPDPWeb.getInstance(session);
        AirportTransferBookingPageWeb<?> airportTransferBookingPageWeb = AirportTransferBookingPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);
        CalendarPageWeb<?> calendarPage = CalendarPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open home page as guest (non-login)");
        loginPage.loginAsGuest();

        step("User change to " + currency + " currency");
        homePage.selectCurrency(currency);

        step("User open Airport Transfer vertical");
        homePage.openVerticalPageViaURL("airport-transfer");

        step("User close pop-up if any");
        airportTransferLandingPageWeb.closePopup();

        step("User selects Airport transfer type: " + type);
        airportTransferLandingPageWeb.selectTransferType(type);

        step("User tap landmark field for pickup");
        airportTransferLandingPageWeb.setSelectAirport(type);
        Assertion.assertThat("Verify popular airport list is displayed when pickup field is tapped", airportTransferLandingPageWeb.checkPopularAirportList(), is(true));

        step("User input keyword airport: " + location);
        String airportLocationName = airportTransferLandingPageWeb.setInputAirport(location);
        Assertion.assertThat("Verify selected airport contains keyword " + location, airportLocationName.toLowerCase(), containsString(location.toLowerCase()));

        step("User tap destination field for drop off");
        airportTransferLandingPageWeb.setSelectDestination(type);
        Assertion.assertThat("Verify popular destination/landmark list is displayed", airportTransferLandingPageWeb.checkPopularDestinationList(), is(true));

        step("User select destination from popular landmark list: " + destination);
        String destinationLocationName = airportTransferLandingPageWeb.selectPopularDestination(destination);

        step("User select date as today (D+" + daysToDepartureDate + ")");
        airportTransferLandingPageWeb.clickOnCalendarWidgetToOpen();
        calendarPage.selectDate(departureDate);

        step("User select minimum pickup time (default minimum)");
        airportTransferLandingPageWeb.selectTime(selectTime);

        step("User select passenger count: " + passengerCount);
        airportTransferLandingPageWeb.selectPassenger(passengerCount);

        step("User taps search car button");
        airportTransferLandingPageWeb.seeCarListOnSRP();
        Assertion.assertThat("Verify car rental cards are displayed on SRP", airportTransferSearchPageWeb.checkCarRentalCard(), is(true));
        airportTransferSearchPageWeb.verifySRPPriceInIDR();

        step("User selects first SRP card to go to PDP");
        airportTransferSearchPageWeb.selectFirstSRPCard();

        step("User tap price detail on PDP and verify origin, destination, and total price");
        airportTransferProductPageWeb.openPriceBottomSheet();
        Assertion.assertThat("Verify Airport Location Name On PDP is same as Landing search page", airportTransferProductPageWeb.checkForAirportLocationName(), containsString(airportLocationName));
        Assertion.assertThat("Verify Destination Location Name On PDP is same as Landing search page", airportTransferProductPageWeb.checkForDestinationLocationName(), containsString(destinationLocationName.split(" ")[1]));
        String totalPriceOnPDP = airportTransferProductPageWeb.getTotalPrice();
        Assertion.assertThat("Verify total price on PDP is shown and not empty", totalPriceOnPDP != null && !totalPriceOnPDP.trim().isEmpty(), is(true));
        airportTransferProductPageWeb.closePriceBottomSheet();

        step("User tap Continue on PDP to go to booking page");
        airportTransferProductPageWeb.proceedToBookingPage();

        step("User fills contact details on booking form");
        airportTransferBookingPageWeb.fillContactDetail(fullName, phoneNumber, email);

        step("Verify contact details on booking form match user input");
        Assertion.assertThat("Verify full name matches user input", airportTransferBookingPageWeb.getFilledFullName(), is(fullName));
        Assertion.assertThat("Verify phone number matches user input", airportTransferBookingPageWeb.getFilledPhoneNumber(), is(phoneNumber));
        Assertion.assertThat("Verify email matches user input", airportTransferBookingPageWeb.getFilledEmail(), is(email));

        step("User tap price detail on booking form and verify origin, destination, and total price");
        airportTransferBookingPageWeb.openPriceBottomSheet();
        Assertion.assertThat("Verify airport (origin) on booking form price detail contains " + airportLocationName, airportTransferBookingPageWeb.checkForAirportLocationName(), containsString(airportLocationName));
        Assertion.assertThat("Verify destination on booking form price detail contains " + destinationLocationName, airportTransferBookingPageWeb.checkForDestinationLocationName(), containsString(destinationLocationName.split(" ")[1]));
        Assertion.assertThat("Verify total price on booking form matches PDP", airportTransferBookingPageWeb.getTotalPrice(), is(totalPriceOnPDP));
        airportTransferBookingPageWeb.closePriceBottomSheet();

        step("User tap Continue to payment");
        airportTransferBookingPageWeb.goToPaymentPage();

        step("Verify user is redirected to payment page and orderId is generated");
        String orderID = paymentPage.getOrderId();
        Assertion.assertThat("Verify order ID is displayed in payment page", !orderID.isEmpty(), is(true));
    }

    @Author(names = {"Vipul Kumar", "Eka"})
    @Module(name = "CompleteBooking")
    @Tribe(name = "AirportTransfer")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyTransferInAirportTransferCompleteBookingGuestWeb() {
        Map<String, String> param = getParam("testVerifyTransferInAirportTransferCompleteBookingGuestWeb");
        BaseTest.paramsThreadLocal.set(param);

        String location = param.get("location");
        String destination = param.get("destination");
        String currency = param.get("currency");
        String type = param.get("type");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);
        String selectTime = param.get("selectTime");
        String passengerCount = param.get("passengerCount");

        String fullName = CoreFunctions.randomName();
        String phoneNumber = "91" + CoreFunctions.randomInt(8);
        String email = CoreFunctions.randomEmail();
        String paymentMethod = param.get("paymentMethod");
        String paymentGroup = param.get("paymentGroup");

        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        AirportTransferLandingPageWeb<?> airportTransferLandingPageWeb = AirportTransferLandingPageWeb.getInstance(session);
        AirportTransferSRPWeb<?> airportTransferSearchPageWeb = AirportTransferSRPWeb.getInstance(session);
        AirportTransferPDPWeb<?> airportTransferProductPageWeb = AirportTransferPDPWeb.getInstance(session);
        AirportTransferBookingPageWeb<?> airportTransferBookingPageWeb = AirportTransferBookingPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        CalendarPageWeb<?> calendarPage = CalendarPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open home page as guest");
        loginPage.loginAsGuest();

        step("User change to " + currency + " currency");
        homePage.selectCurrency(currency);

        step("User open Airport Pickups vertical");
        homePage.openVerticalPageViaURL("airport-transfer");

        step("User click on Skip pop Up");
        airportTransferLandingPageWeb.closePopup();

        step("User selects Airport transfer type: " + type);
        airportTransferLandingPageWeb.selectTransferType(type);

        step("User fill the Airport and destination locations based on transfer type");
        Map<String, String> locationMap = airportTransferLandingPageWeb.fillTransferFields(type, location, destination);
        String airportLocationName = locationMap.get("airport");
        String DestinationLocationName = locationMap.get("destination");

        step("User select date and Time");
        airportTransferLandingPageWeb.clickOnCalendarWidgetToOpen();
        calendarPage.selectDate(departureDate);
        airportTransferLandingPageWeb.selectTime(selectTime);

        step("User select Passenger count");
        airportTransferLandingPageWeb.selectPassenger(passengerCount);

        step("User searches the cars");
        airportTransferLandingPageWeb.seeCarListOnSRP();
        Assertion.assertThat("Verify the car rental card is displayed or not", airportTransferSearchPageWeb.checkCarRentalCard(), is(true));
        airportTransferSearchPageWeb.verifySRPPriceInIDR();

        step("User select first SRP card & go to PDP");
        airportTransferSearchPageWeb.selectFirstSRPCard();

        step("User open price bottom sheet on PDP");
        airportTransferProductPageWeb.openPriceBottomSheet();
        Assertion.assertThat("Verify Destination Location Name On PDP is same as Landing search page", airportTransferProductPageWeb.checkForAirportLocationName(), containsString(DestinationLocationName));
        Assertion.assertThat("Verify Airport Location Name On PDP is same as Landing search page", airportTransferProductPageWeb.checkForDestinationLocationName(), containsString(airportLocationName));
        String ToTalPriceOnPDP = airportTransferProductPageWeb.getTotalPrice();
        step("User Close the Bottom sheet of Total Price On PDP");
        airportTransferProductPageWeb.closePriceBottomSheet();

        step("User Go to Booking Page");
        airportTransferProductPageWeb.proceedToBookingPage();

        step("User input contact detail");
        airportTransferBookingPageWeb.fillContactDetail(fullName, phoneNumber, email);

        step("User open price bottom sheet on Booking Page");
        airportTransferBookingPageWeb.openPriceBottomSheet();
        Assertion.assertThat("Verify total price on Booking Page is same as on PDP", airportTransferBookingPageWeb.getTotalPrice(), is(ToTalPriceOnPDP));
        airportTransferBookingPageWeb.closePriceBottomSheet();

        step("User Go to Payment Page");
        airportTransferBookingPageWeb.goToPaymentPage();
        String orderID = paymentPage.getOrderId();
        Assertion.assertThat("Verify order ID is displayed in payment page", !orderID.isEmpty(), is(true));

        if (Configs.ENVIRONMENT == Environment.PROD) {
            step("User select payment method ");
            paymentPage.clickOnSelectPaymentMethod();
            paymentPage.goToPaymentMethodsGroup(paymentGroup);
            paymentPage.selectPaymentMethod(paymentMethod);

            step("User complete payment");
            paymentPage.clickOnPaymentButton();

            step("User open order list page");
            paymentPage.clickSeeOrderListButton();
        }

        if (Configs.ENVIRONMENT == Environment.GK || Configs.ENVIRONMENT == Environment.PREPROD) {
            step("User select payment method ");
            paymentPage.clickOnSelectPaymentMethod();
            paymentPage.goToPaymentMethodsGroup(paymentGroup);
            paymentPage.selectPaymentMethod(paymentMethod);

            step("User complete payment");
            paymentPage.clickOnPaymentButton();

            step("User open order detail page");
            orderListPage.openActiveTicketViaURL(orderID);
        }
        Assertion.assertThat("Verify web page will redirect to login page for guest user", loginPage.isLoginPageOpened(), is(true));
    }

    @Author(names = {"Vipul Kumar", "Eka"})
    @Module(name = "AT")
    @Tribe(name = "NFT")
    @Vertical(name = "NFT")
    @Test
    public void AT_testVerifyAirportTransferCompleteBookingTransferOutLoginWeb() throws InterruptedException, FailedLoginException {
        Map<String, String> param = getParam("AT_testVerifyAirportTransferCompleteBookingTransferOutLoginWeb");
        BaseTest.paramsThreadLocal.set(param);

        String location = param.get("location");
        String destination = param.get("destination");
        String currency = param.get("currency");
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String type = param.get("type");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);
        String selectTime = param.get("selectTime");
        String passengerCount = param.get("passengerCount");

        String paymentMethod = param.get("paymentMethod");
        String paymentGroup = param.get("paymentGroup");

        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        AirportTransferLandingPageWeb<?> airportTransferLandingPageWeb = AirportTransferLandingPageWeb.getInstance(session);
        AirportTransferSRPWeb<?> airportTransferSearchPageWeb = AirportTransferSRPWeb.getInstance(session);
        AirportTransferPDPWeb<?> airportTransferProductPageWeb = AirportTransferPDPWeb.getInstance(session);
        AirportTransferBookingPageWeb<?> airportTransferBookingPageWeb = AirportTransferBookingPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        CalendarPageWeb<?> calendarPage = CalendarPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("Login as " + email + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(email, password);

        step("Verify user is logged in and home page is opened");
        homePage.waitForHomePageToOpen();
        Assertion.assertThat("Verify the home page is opened", homePage.isHomePageOpened(), is(true));
        Assertion.assertThat("Verify the user logged in", loginPage.checkIfLoggedIn(), is(true));

        step("User change to " + currency + " currency");
        homePage.selectCurrency(currency);

        step("User open Airport Pickups vertical");
        homePage.openVerticalPageViaURL("airport-transfer");

        step("User click on Skip pop Up");
        airportTransferLandingPageWeb.closePopup();

        step("User selects Airport transfer type: " + type);
        airportTransferLandingPageWeb.selectTransferType(type);

        step("User fill the Airport and destination locations based on transfer type");
        Map<String, String> locationMap = airportTransferLandingPageWeb.fillTransferFields(type, location, destination);
        String airportLocationName = locationMap.get("airport");
        String DestinationLocationName = locationMap.get("destination");

        step("User select date and Time");
        airportTransferLandingPageWeb.clickOnCalendarWidgetToOpen();
        calendarPage.selectDate(departureDate);
        airportTransferLandingPageWeb.selectTime(selectTime);

        step("User select Passenger count");
        airportTransferLandingPageWeb.selectPassenger(passengerCount);

        step("User searches the cars");
        airportTransferLandingPageWeb.seeCarListOnSRP();
        Assertion.assertThat("Verify the car rental card is displayed or not", airportTransferSearchPageWeb.checkCarRentalCard(), is(true));
        airportTransferSearchPageWeb.verifySRPPriceInIDR();

        step("User select first SRP card & go to PDP");
        airportTransferSearchPageWeb.selectFirstSRPCard();

        step("User open price bottom sheet on PDP");
        airportTransferProductPageWeb.openPriceBottomSheet();
        Assertion.assertThat("Verify Airport Location Name On PDP is same as Landing search page", airportTransferProductPageWeb.checkForAirportLocationName(), containsString(airportLocationName));
        Assertion.assertThat("Verify Destination Location Name On PDP is same as Landing search page", airportTransferProductPageWeb.checkForDestinationLocationName(), containsString(DestinationLocationName));
        String ToTalPriceOnPDP = airportTransferProductPageWeb.getTotalPrice();

        step("User Close the Bottom sheet of Total Price On PDP");
        airportTransferProductPageWeb.closePriceBottomSheet();

        step("User Go to Booking Page");
        airportTransferProductPageWeb.proceedToBookingPage();
        airportTransferBookingPageWeb.checkBookingContactDetailsCard();

        step("User open price bottom sheet on Booking Page");
        airportTransferBookingPageWeb.openPriceBottomSheet();
        Assertion.assertThat("Verify Airport Location Name On Booking Page is same as Landing search page", airportTransferBookingPageWeb.checkForAirportLocationName(), containsString(airportLocationName));
        Assertion.assertThat("Verify Destination Location Name On Booking Page is same as Landing search page", airportTransferBookingPageWeb.checkForDestinationLocationName(), containsString(DestinationLocationName));
        Assertion.assertThat("Verify total price on Booking Page is same as on PDP", airportTransferBookingPageWeb.getTotalPrice(), is(ToTalPriceOnPDP));
        airportTransferBookingPageWeb.closePriceBottomSheet();

        step("User Go to Payment Page");
        airportTransferBookingPageWeb.goToPaymentPage();
        String orderID = paymentPage.getOrderId();
        Assertion.assertThat("Verify order ID is displayed in payment page", !orderID.isEmpty(), is(true));

        if (Configs.ENVIRONMENT == Environment.PROD) {
            step("User select payment method ");
            paymentPage.clickOnSelectPaymentMethod();
            paymentPage.goToPaymentMethodsGroup(paymentGroup);
            paymentPage.selectPaymentMethod(paymentMethod);

            step("User complete payment");
            paymentPage.clickOnPaymentButton();

            step("User open order list page");
            paymentPage.clickSeeOrderListButton();
        }

        if (Configs.ENVIRONMENT == Environment.GK || Configs.ENVIRONMENT == Environment.PREPROD) {
            step("User select payment method ");
            paymentPage.clickOnSelectPaymentMethod();
            paymentPage.goToPaymentMethodsGroup(paymentGroup);
            paymentPage.selectPaymentMethod(paymentMethod);

            step("User complete payment");
            paymentPage.clickOnPaymentButton();
            boolean paymentSuccess = paymentPage.isPaymentSuccessfulVisible();

            step("User open order detail page");
            orderListPage.openActiveTicketViaURL(orderID);
            orderDetailPage.refreshTheWebpage();
            orderDetailPage.skipCoachmark();

            if (paymentSuccess) {
                step("User check e-tiket details");
                Assertion.assertThat("Verify e-ticket card must be displayed", orderDetailPage.isETicketCardDisplayed(), is(true));
                Assertion.assertThat("Verify Airport Location Name On order detail page is same as Landing search page", orderDetailPage.checkForAirportLocationNameTransferOut(), containsString(airportLocationName));
                Assertion.assertThat("Verify Destination Location Name On order detail page is same as Landing search page", orderDetailPage.checkForDestinationLocationNameTransferOut(), containsString(DestinationLocationName));
            }
        }
    }

    @Author(names = {"Vipul Kumar", "Eka"})
    @Module(name = "AT")
    @Tribe(name = "NFT")
    @Vertical(name = "NFT")
    @Test
    public void AT_testVerifyTransferInAirportTransferCompleteBookingLoginWeb() throws InterruptedException, FailedLoginException {
        Map<String, String> param = getParam("AT_testVerifyTransferInAirportTransferCompleteBookingLoginWeb");
        BaseTest.paramsThreadLocal.set(param);

        String location = param.get("location");
        String destination = param.get("destination");
        String currency = param.get("currency");
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String type = param.get("type");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);
        String selectTime = param.get("selectTime");
        String passengerCount = param.get("passengerCount");

        String paymentMethod = param.get("paymentMethod");
        String paymentGroup = param.get("paymentGroup");

        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        AirportTransferLandingPageWeb<?> airportTransferLandingPageWeb = AirportTransferLandingPageWeb.getInstance(session);
        AirportTransferSRPWeb<?> airportTransferSearchPageWeb = AirportTransferSRPWeb.getInstance(session);
        AirportTransferPDPWeb<?> airportTransferProductPageWeb = AirportTransferPDPWeb.getInstance(session);
        AirportTransferBookingPageWeb<?> airportTransferBookingPageWeb = AirportTransferBookingPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        CalendarPageWeb<?> calendarPage = CalendarPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("Login as " + email + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(email, password);

        step("Verify user is logged in and home page is opened");
        homePage.waitForHomePageToOpen();
        Assertion.assertThat("Verify the home page is opened", homePage.isHomePageOpened(), is(true));
        Assertion.assertThat("Verify the user logged in", loginPage.checkIfLoggedIn(), is(true));

        step("User change to " + currency + " currency");
        homePage.selectCurrency(currency);

        step("User open Airport Pickups vertical");
        homePage.openVerticalPageViaURL("airport-transfer");

        step("User click on Skip pop Up");
        airportTransferLandingPageWeb.closePopup();

        step("User selects Airport transfer type: " + type);
        airportTransferLandingPageWeb.selectTransferType(type);

        step("User fill the Airport and destination locations based on transfer type");
        Map<String, String> locationMap = airportTransferLandingPageWeb.fillTransferFields(type, location, destination);
        String airportLocationName = locationMap.get("airport");
        String DestinationLocationName = locationMap.get("destination");

        step("User select date and Time");
        airportTransferLandingPageWeb.clickOnCalendarWidgetToOpen();
        calendarPage.selectDate(departureDate);
        airportTransferLandingPageWeb.selectTime(selectTime);

        step("User select Passenger count");
        airportTransferLandingPageWeb.selectPassenger(passengerCount);

        step("User searches the cars");
        airportTransferLandingPageWeb.seeCarListOnSRP();
        Assertion.assertThat("Verify the car rental card is displayed or not", airportTransferSearchPageWeb.checkCarRentalCard(), is(true));
        airportTransferSearchPageWeb.verifySRPPriceInIDR();

        step("User select first SRP card & go to PDP");
        airportTransferSearchPageWeb.selectFirstSRPCard();

        step("User open price bottom sheet on PDP");
        airportTransferProductPageWeb.openPriceBottomSheet();
        Assertion.assertThat("Verify Destination Location Name On PDP is same as Landing search page", airportTransferProductPageWeb.checkForAirportLocationNameTransferOut(), containsString(airportLocationName));
        Assertion.assertThat("Verify Airport Location Name On PDP is same as Landing search page", airportTransferProductPageWeb.checkForDestinationLocationNameTransferOut(), containsString(DestinationLocationName));
        String ToTalPriceOnPDP = airportTransferProductPageWeb.getTotalPrice();

        step("User Close the Bottom sheet of Total Price On PDP");
        airportTransferProductPageWeb.closePriceBottomSheet();

        step("User Go to Booking Page");
        airportTransferProductPageWeb.proceedToBookingPage();

        step("User open price bottom sheet on Booking Page");
        airportTransferBookingPageWeb.checkBookingContactDetailsCard();
        airportTransferBookingPageWeb.openPriceBottomSheet();
        Assertion.assertThat("Verify total price on Booking Page is same as on PDP", airportTransferBookingPageWeb.getTotalPrice(), is(ToTalPriceOnPDP));
        airportTransferBookingPageWeb.closePriceBottomSheet();

        step("User Go to Payment Page");
        airportTransferBookingPageWeb.goToPaymentPage();
        String orderID = paymentPage.getOrderId();
        Assertion.assertThat("Verify order ID is displayed in payment page", !orderID.isEmpty(), is(true));

        if (Configs.ENVIRONMENT == Environment.GK || Configs.ENVIRONMENT == Environment.PREPROD) {
            step("User select payment method ");
            paymentPage.clickOnSelectPaymentMethod();
            paymentPage.goToPaymentMethodsGroup(paymentGroup);
            paymentPage.selectPaymentMethod(paymentMethod);

            step("User complete payment");
            paymentPage.clickOnPaymentButton();
            boolean paymentSuccess = paymentPage.isPaymentSuccessfulVisible();

            step("User open order detail page");
            orderListPage.openActiveTicketViaURL(orderID);
            orderDetailPage.refreshTheWebpage();
            orderDetailPage.skipCoachmark();

            if (paymentSuccess) {
                step("User check e-tiket details");
                Assertion.assertThat("Verify e-ticket card must be displayed", orderDetailPage.isETicketCardDisplayed(), is(true));
                Assertion.assertThat("Verify Airport Location Name On order detail page is same as Landing search page", orderDetailPage.checkForAirportLocationName(), containsString(airportLocationName));
                Assertion.assertThat("Verify Destination Location Name On order detail page is same as Landing search page", orderDetailPage.checkForDestinationLocationName(), containsString(DestinationLocationName));
            }
        }
    }
}