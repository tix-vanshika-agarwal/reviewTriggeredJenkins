package com.tiket.test.nft.bus;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Environment;
import com.tiket.model.Session;
import com.tiket.page.bus.bookingform.BusBookingFormPage;
import com.tiket.page.bus.pdp.BuspdpPage;
import com.tiket.page.bus.search.BusSearchPage;
import com.tiket.page.bus.searchresultpage.BusSearchResultPage;
import com.tiket.page.home.HomePage;
import com.tiket.page.login.LoginPage;
import com.tiket.page.order.orderdetail.OrderDetailPage;
import com.tiket.page.order.orderlist.OrderListPage;
import com.tiket.page.payment.PaymentPage;
import com.tiket.pojo.bus.BusBookingPageData;
import com.tiket.pojo.bus.BusSrpOneWayData;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.testbase.Configs;
import com.tiket.web.page.order.OrderList.OrderListWeb;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Map;

import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;

public class BusTest extends BaseTest{

    @Author(names = {"Dendi"})
    @Module(name = "BookBusShuttleViaDeepLink")
    @Tribe(name = "Bus")
    @Vertical(name = "GroundTransport")
    @Test
    public void testBookBusViaDeeplinkLogin(){
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> testProperties = getParam("testBookBusViaDeeplinkLogin");
        BaseTest.paramsThreadLocal.set(testProperties);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        BusSearchPage<WebDriver> busSearchPage = BusSearchPage.getInstance(session);
        BusSearchResultPage<WebDriver> busSearchResultPage = BusSearchResultPage.getInstance(session);
        BuspdpPage<WebDriver> buspdpPage = BuspdpPage.getInstance(session);
        BusBookingFormPage<WebDriver> busBookingFormPage = BusBookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);

        String loginMethod = testProperties.get("loginType");
        String username = testProperties.get("username");
        String password = testProperties.get("password");
        String currency = testProperties.get("currency");
        String originLocation = testProperties.get("originLocation");
        String destinationLocation = testProperties.get("destinationLocation");


        step("User open app");
        homePage.gotoHome();

        step("User input credential");
        loginPage.loginWith(loginMethod, username, password);

        step("Change currency");
        homePage.changeCurrency(currency,username);

        step("Open Bus search page using deeplink");
        busSearchPage.openBusSearchViaDeepLink();

        step("Skip coachmark");
        busSearchPage.skipCoachMark();

        step("Select oneway");
        busSearchPage.switchTripType("oneway");

        step("Input origin location");
        busSearchPage.inputOriginLocation(originLocation);

        step("Input destination location");
        busSearchPage.inputDestinationLocation(destinationLocation);

        step("Choose tomorrow date");
        busSearchPage.chooseTomorrowDate();

        step("Select pax amount");
        busSearchPage.selectPassenger(1);

        step("Click search bus");
        busSearchPage.clickSearchBus();

        step("Skip bus srp coachmark");
        busSearchResultPage.skipCoachMark();

        step("Verify price in srp are in IDR");
        Assertion.assertThat("Verify price in srp are in idr", busSearchResultPage.verifyPricesAreInIdrOnly(), is(true));

        step("Select bus on srp");
        busSearchResultPage.selectFirstBus();

        step("Verify price in pdp is equal to idr");
        Assertion.assertThat("Verify final price is in idr", buspdpPage.getFinalPriceCurrency(), equalTo("IDR"));

        step("Continue to booking form");
        buspdpPage.openSeat();
        Assertion.assertThat("Verify seat price is in idr", buspdpPage.getSeatPriceCurrency(), equalTo("IDR"));
        buspdpPage.clickSelectSeatButton();

        step("Toggle same as contact");
        busBookingFormPage.inputPassengerGuestDetail();

        step("Verify price in booking form is in idr");
        String busBookingPagePrice = busBookingFormPage.getBookingPagePrice();
        Assertion.assertThat("Verify price in booking form is in IDR",  busBookingFormPage.getBookingPagePriceCurrency(), equalTo("IDR"));

        step("Continue to payment page");
        busBookingFormPage.continueToPaymentPage();
        Assertion.assertThat("Verify user get orderId", !paymentPage.checkOrderID().isBlank(), is(true));

        step("Verify total price is in IDR");
        String totalPricePayment =  paymentPage.getTotalPricePayment();
        Assertion.assertThat("Verify Price on booking form is equal with payment page",busBookingPagePrice.equals(totalPricePayment), is(true));
        Assertion.assertThat("Verify total price is in IDR", paymentPage.getTotalPriceCurrency(), equalTo("IDR"));
    }

    @Author(names = {"Dendi"})
    @Module(name = "BookBusShuttleViaDeepLink")
    @Tribe(name = "Bus")
    @Vertical(name = "GroundTransport")
    @Test
    public void testBookBusViaDeeplinkGuest(){
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> testProperties = getParam("testBookBusViaDeeplinkGuest");
        BaseTest.paramsThreadLocal.set(testProperties);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        BusSearchPage<WebDriver> busSearchPage = BusSearchPage.getInstance(session);
        BusSearchResultPage<WebDriver> busSearchResultPage = BusSearchResultPage.getInstance(session);
        BuspdpPage<WebDriver> buspdpPage = BuspdpPage.getInstance(session);
        BusBookingFormPage<WebDriver> busBookingFormPage = BusBookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);

        String currency = testProperties.get("currency");
        String originLocation = testProperties.get("originLocation");
        String destinationLocation = testProperties.get("destinationLocation");
        String fullName = CoreFunctions.randomName();
        String phoneNumber =String.valueOf("8"+CoreFunctions.randomInt(10));
        String emailAddress = testProperties.get("emailAdddress");

        step("User open app");
        homePage.gotoHome();

        step("User login as guest");
        loginPage.loginAsGuest();

        step("Change currency");
        homePage.changeCurrency(currency,"");

        step("Open Bus search page using deeplink");
        busSearchPage.openBusSearchViaDeepLink();

        step("Skip coachmark");
        busSearchPage.skipCoachMark();

        step("Select oneway");
        busSearchPage.switchTripType("oneway");

        step("Input origin location");
        busSearchPage.inputOriginLocation(originLocation);

        step("Input destination location");
        busSearchPage.inputDestinationLocation(destinationLocation);

        step("Choose tomorrow date");
        busSearchPage.chooseTomorrowDate();

        step("Select pax amount");
        busSearchPage.selectPassenger(1);

        step("Click search bus");
        busSearchPage.clickSearchBus();

        step("Skip bus srp coachmark");
        busSearchResultPage.skipCoachMark();

        step("Verify price in srp are in IDR");
        Assertion.assertThat("Verify price in srp are in idr", busSearchResultPage.verifyPricesAreInIdrOnly(), is(true));

        step("Select bus on srp");
        busSearchResultPage.selectFirstBus();

        step("Verify price in pdp is equal to idr");
        Assertion.assertThat("Verify final price is in idr",buspdpPage.getFinalPriceCurrency(), containsString("IDR"));

        step("Continue to booking form");
        buspdpPage.openSeat();
        buspdpPage.clickSelectSeatButton();

        step("Toggle same as contact");
        busBookingFormPage.inputGuestContactDetail(fullName,phoneNumber,emailAddress);
        busBookingFormPage.inputPassengerGuestDetail();

        step("Verify price in booking form is in idr");
        String busBookingPagePrice = busBookingFormPage.getBookingPagePrice();
        Assertion.assertThat("Verify price in booking form is in IDR",  busBookingFormPage.getBookingPagePriceCurrency(), equalTo("IDR"));

        step("Continue to payment page");
        busBookingFormPage.continueToPaymentPage();
        Assertion.assertThat("Verify user get orderId", !paymentPage.checkOrderID().isBlank(), is(true));

        step("Verify total price is in IDR");
        String totalPricePayment =  paymentPage.getTotalPricePayment();
        Assertion.assertThat("Verify Price on booking form is equal with payment page",busBookingPagePrice.equals(totalPricePayment), is(true));
        Assertion.assertThat("Verify total price is in IDR", paymentPage.getTotalPriceCurrency(), equalTo("IDR"));
    }

    @Author(names = {"Kartik"})
    @Module(name = "CompleteBooking")
    @Tribe(name = "Bus")
    @Vertical(name = "GroundTransport")
    @Test
    public void testBookBusRoundtripGuest(){
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> testProperties = getParam("testBookBusRoundtripGuest");
        BaseTest.paramsThreadLocal.set(testProperties);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        BusSearchPage<WebDriver> busSearchPage = BusSearchPage.getInstance(session);
        BusSearchResultPage<WebDriver> busSearchResultPage = BusSearchResultPage.getInstance(session);
        BuspdpPage<WebDriver> buspdpPage = BuspdpPage.getInstance(session);
        BusBookingFormPage<WebDriver> busBookingFormPage = BusBookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);

        String tripType = testProperties.get("tripType");
        String originLocation = testProperties.get("originLocation");
        String destinationLocation = testProperties.get("destinationLocation");
        int daysToDeparture = Integer.parseInt(testProperties.get("daysToDeparture"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String dateDeparture = CoreFunctions.addToLongDate(currentDate,daysToDeparture);
        int duration = Integer.parseInt(testProperties.get("duration"));
        String dateReturn = CoreFunctions.addToLongDate(dateDeparture,duration);
        int passenger = Integer.parseInt(testProperties.get("passenger"));

        String fullName = CoreFunctions.randomName();
        String phoneNumber =CoreFunctions.randomPhone();
        String emailAddress = CoreFunctions.randomEmail();

        step("User open app");
        homePage.gotoHome();

        step("Logout via deeplink");
        homePage.logoutViaDeeplink();

        if(Configs.ENVIRONMENT == Environment.PROD) {
            step("Change currency to 'IDR'");
            homePage.changeCurrency("IDR","");
        }

        step("Get App Language");
        String language = homePage.getLanguageApp();

        step("Open Bus Landing Page");
        homePage.openVerticalPageDeeplink("Bus", language, "IDR");

        step("Skip coachmark");
        busSearchPage.skipCoachMark();

        step("Switch trip type to roundtrip");
        busSearchPage.switchTripType(tripType);

        step("Input origin location");
        busSearchPage.inputOriginLocation(originLocation);

        step("Input destination location");
        busSearchPage.inputDestinationLocation(destinationLocation);

        step("Select date");
        busSearchPage.selectDate(dateDeparture,dateReturn);

        step("Select pax amount");
        busSearchPage.selectPassenger(passenger);

        step("Verify search form value");
        Assertion.assertThat("is origin selected",busSearchPage.isDepartureSelected(originLocation),is(true));
        Assertion.assertThat("is destination selected",busSearchPage.isDestinationSelected(destinationLocation),is(true));
        Assertion.assertThat("is calendar selected",busSearchPage.isDateSelected(dateDeparture,dateReturn),is(true));
        Assertion.assertThat("is correct pax selected",busSearchPage.isPaxSelected(passenger),is(true));

        step("Click search bus");
        busSearchPage.clickSearchBus();

        step("Skip bus srp coachmark");
        busSearchResultPage.skipCoachMark();

        step("Select bus departure on srp");
        busSearchResultPage.selectFirstBus();

        step("select seat departure");
        buspdpPage.openSeat();
        buspdpPage.clickSelectSeatButton();

        step("Select bus return on srp");
        busSearchResultPage.selectFirstBus();

        step("select seat return");
        buspdpPage.openSeat();
        buspdpPage.clickSelectSeatButton();

        step("Input guest contact detail");
        busBookingFormPage.inputGuestContactDetail(fullName,phoneNumber,emailAddress);
        busBookingFormPage.fillPassengerData(passenger);

        step("Continue to payment page");
        busBookingFormPage.continueToPaymentPage();
        Assertion.assertThat("Verify user get orderId", !paymentPage.checkOrderID().isBlank(), is(true));
    }

    @Author(names = {"kartik"})
    @Module(name = "Bus")
    @Tribe(name = "NFT")
    @Vertical(name = "GroundTransport")
    @Test
    public void Bus_testBookBusRoundtripLogin(){
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> testProperties = getParam("Bus_testBookBusRoundtripLogin");
        BaseTest.paramsThreadLocal.set(testProperties);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        BusSearchPage<WebDriver> busSearchPage = BusSearchPage.getInstance(session);
        BusSearchResultPage<WebDriver> busSearchResultPage = BusSearchResultPage.getInstance(session);
        BuspdpPage<WebDriver> buspdpPage = BuspdpPage.getInstance(session);
        BusBookingFormPage<WebDriver> busBookingFormPage = BusBookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);

        String loginMethod = testProperties.get("loginType");
        String username = testProperties.get("username");
        String password = testProperties.get("password");

        String tripType = testProperties.get("tripType");
        String originLocation = testProperties.get("originLocation");
        String destinationLocation = testProperties.get("destinationLocation");
        int daysToDeparture = Integer.parseInt(testProperties.get("daysToDeparture"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String dateDeparture = CoreFunctions.addToLongDate(currentDate,daysToDeparture);
        int duration = Integer.parseInt(testProperties.get("duration"));
        String dateReturn = CoreFunctions.addToLongDate(dateDeparture,duration);
        int passenger = Integer.parseInt(testProperties.get("passenger"));

        step("User open app");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink (if needed) and change currency to IDR");
        homePage.loginIfLoggedOutAndChangeCurrencyViaDeeplink(username, password, "IDR");

        step("Get App Language");
        String language = homePage.getLanguageApp();

        step("Open Bus Landing Page");
        homePage.openVerticalPageDeeplink("Bus", language, "IDR");

        step("Skip coachmark");
        busSearchPage.skipCoachMark();

        step("Switch trip type to roundtrip");
        busSearchPage.switchTripType(tripType);

        step("Input origin location");
        busSearchPage.inputOriginLocation(originLocation);

        step("Input destination location");
        busSearchPage.inputDestinationLocation(destinationLocation);

        step("Select date");
        busSearchPage.selectDate(dateDeparture,dateReturn);

        step("Select pax amount");
        busSearchPage.selectPassenger(passenger);

        step("Verify search form value");
        Assertion.assertThat("is origin selected",busSearchPage.isDepartureSelected(originLocation),is(true));
        Assertion.assertThat("is destination selected",busSearchPage.isDestinationSelected(destinationLocation),is(true));
        Assertion.assertThat("is calendar selected",busSearchPage.isDateSelected(dateDeparture,dateReturn),is(true));
        Assertion.assertThat("is correct pax selected",busSearchPage.isPaxSelected(passenger),is(true));

        step("Click search bus");
        busSearchPage.clickSearchBus();

        step("Skip bus srp coachmark");
        busSearchResultPage.skipCoachMark();

        step("Select bus departure on srp");
        busSearchResultPage.selectFirstBus();

        step("select seat departure");
        buspdpPage.openSeat();
        buspdpPage.clickSelectSeatButton();

        step("Select bus return on srp");
        busSearchResultPage.selectFirstBus();

        step("select seat return");
        buspdpPage.openSeat();
        buspdpPage.clickSelectSeatButton();

        step("Input contact detail");
        busBookingFormPage.fillPassengerData(passenger);

        step("Continue to payment page");
        busBookingFormPage.continueToPaymentPage();
        Assertion.assertThat("Verify user get orderId", !paymentPage.checkOrderID().isBlank(), is(true));
    }

    @Author(names = {"Zia"})
    @Module(name = "CompleteBooking")
    @Tribe(name = "Bus")
    @Vertical(name = "GroundTransport")
    @Test
    public void testBookBusOnewayGuest(){
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> testProperties = getParam("testBookBusOnewayGuest");
        BaseTest.paramsThreadLocal.set(testProperties);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        BusSearchPage<WebDriver> busSearchPage = BusSearchPage.getInstance(session);
        BusSearchResultPage<WebDriver> busSearchResultPage = BusSearchResultPage.getInstance(session);
        BuspdpPage<WebDriver> buspdpPage = BuspdpPage.getInstance(session);
        BusBookingFormPage<WebDriver> busBookingFormPage = BusBookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);

        String tripType = testProperties.get("tripType");
        String originLocation = testProperties.get("originLocation");
        String destinationLocation = testProperties.get("destinationLocation");
        int daysToDeparture = Integer.parseInt(testProperties.get("daysToDeparture"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String dateDeparture = CoreFunctions.addToLongDate(currentDate,daysToDeparture);
        int passenger = Integer.parseInt(testProperties.get("passenger"));

        String fullName = CoreFunctions.randomName();
        String phoneNumber =CoreFunctions.randomPhone();
        String emailAddress = CoreFunctions.randomEmail();

        step("User open app");
        homePage.gotoHome();

        step("User login as guest");
        loginPage.loginAsGuest();

        if(Configs.ENVIRONMENT == Environment.PROD) {
            step("Change currency to 'IDR'");
            homePage.changeCurrency("IDR","");
        }

        step("Get App Language");
        String language = homePage.getLanguageApp();

        step("Open Bus Landing Page");
        homePage.openVerticalPageDeeplink("Bus", language, "IDR");

        step("Skip coachmark");
        busSearchPage.skipCoachMark();

        step("Switch trip type to oneway");
        busSearchPage.switchTripType(tripType);

        step("Input origin location");
        busSearchPage.inputOriginLocation(originLocation);

        step("Input destination location");
        busSearchPage.inputDestinationLocation(destinationLocation);

        step("Select date");
        busSearchPage.selectDate(dateDeparture);

        step("Select pax amount");
        busSearchPage.selectPassenger(passenger);

        step("Verify search form value");
        Assertion.assertThat("is origin selected",busSearchPage.isDepartureSelected(originLocation),is(true));
        Assertion.assertThat("is destination selected",busSearchPage.isDestinationSelected(destinationLocation),is(true));
        Assertion.assertThat("is calendar selected",busSearchPage.isDateSelected(dateDeparture),is(true));
        Assertion.assertThat("is correct pax selected",busSearchPage.isPaxSelected(passenger),is(true));

        step("Click search bus");
        busSearchPage.clickSearchBus();

        step("Skip bus srp coachmark");
        busSearchResultPage.skipCoachMark();

        step("Select bus departure on srp");
        busSearchResultPage.selectFirstBus();

        step("select seat departure");
        buspdpPage.openSeat();
        buspdpPage.clickSelectSeatButton();

        step("Input guest contact detail");
        busBookingFormPage.inputGuestContactDetail(fullName,phoneNumber,emailAddress);
        busBookingFormPage.inputPassengerGuestDetail();

        step("Continue to payment page");
        busBookingFormPage.continueToPaymentPage();
        Assertion.assertThat("Verify user get orderId", !paymentPage.checkOrderID().isBlank(), is(true));
    }

    @Author(names = {"Zia"})
    @Module(name = "CompleteBooking")
    @Tribe(name = "Bus")
    @Vertical(name = "GroundTransport")
    @Test
    public void testBookBusOnewayLogin(){
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> testProperties = getParam("testBookBusOnewayLogin");
        BaseTest.paramsThreadLocal.set(testProperties);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        BusSearchPage<WebDriver> busSearchPage = BusSearchPage.getInstance(session);
        BusSearchResultPage<WebDriver> busSearchResultPage = BusSearchResultPage.getInstance(session);
        BuspdpPage<WebDriver> buspdpPage = BuspdpPage.getInstance(session);
        BusBookingFormPage<WebDriver> busBookingFormPage = BusBookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);

        String loginMethod = testProperties.get("loginType");
        String username = testProperties.get("username");
        String password = testProperties.get("password");

        String tripType = testProperties.get("tripType");
        String originLocation = testProperties.get("originLocation");
        String destinationLocation = testProperties.get("destinationLocation");
        int daysToDeparture = Integer.parseInt(testProperties.get("daysToDeparture"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String dateDeparture = CoreFunctions.addToLongDate(currentDate,daysToDeparture);
        int passenger = Integer.parseInt(testProperties.get("passenger"));

        step("User open app");
        homePage.gotoHome();

        step("User login");
        loginPage.loginWith(loginMethod, username, password);

        if(Configs.ENVIRONMENT == Environment.PROD) {
            step("Change currency to 'IDR'");
            homePage.changeCurrency("IDR",username);
        }

        step("Get App Language");
        String language = homePage.getLanguageApp();

        step("Open Bus Landing Page");
        homePage.openVerticalPageDeeplink("Bus", language, "IDR");

        step("Skip coachmark");
        busSearchPage.skipCoachMark();

        step("Switch trip type to oneway");
        busSearchPage.switchTripType(tripType);

        step("Input origin location");
        busSearchPage.inputOriginLocation(originLocation);

        step("Input destination location");
        busSearchPage.inputDestinationLocation(destinationLocation);

        step("Select date");
        busSearchPage.selectDate(dateDeparture);

        step("Select pax amount");
        busSearchPage.selectPassenger(passenger);

        step("Verify search form value");
        Assertion.assertThat("is origin selected",busSearchPage.isDepartureSelected(originLocation),is(true));
        Assertion.assertThat("is destination selected",busSearchPage.isDestinationSelected(destinationLocation),is(true));
        Assertion.assertThat("is calendar selected",busSearchPage.isDateSelected(dateDeparture),is(true));
        Assertion.assertThat("is correct pax selected",busSearchPage.isPaxSelected(passenger),is(true));

        step("Click search bus");
        busSearchPage.clickSearchBus();

        step("Skip bus srp coachmark");
        busSearchResultPage.skipCoachMark();

        step("Select bus departure on srp");
        busSearchResultPage.selectFirstBus();

        step("select seat departure");
        buspdpPage.openSeat();
        buspdpPage.clickSelectSeatButton();

        step("Input contact detail");
        busBookingFormPage.togglePassengerSameAsContact();
        busBookingFormPage.fillPassengerData(passenger);

        step("Continue to payment page");
        busBookingFormPage.continueToPaymentPage();
        Assertion.assertThat("Verify user get orderId", !paymentPage.checkOrderID().isBlank(), is(true));
    }

    @Author(names = {"Ashish"})
    @Module(name = "CheckDepartureLocation")
    @Tribe(name = "Bus")
    @Vertical(name = "GroundTransport")
    @Test
    public void testToCheckDepartureLocationLogin(){
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> testProperties = getParam("testToCheckDepartureLocationLogin");
        BaseTest.paramsThreadLocal.set(testProperties);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        BusSearchPage<WebDriver> busSearchPage = BusSearchPage.getInstance(session);

        String loginMethod = testProperties.get("loginType");
        String username = testProperties.get("username");
        String password = testProperties.get("password");
        String currency = testProperties.get("currency");
        String originLocation = testProperties.get("originLocation");

        step("User open app");
        homePage.gotoHome();

        step("User input credential");
        loginPage.loginWith(loginMethod, username, password);

        if(Configs.ENVIRONMENT == Environment.PROD) {
            step("Change currency to 'IDR'");
            homePage.changeCurrency("IDR",username);
        }

        step("Get App Language");
        String language = homePage.getLanguageApp();

        step("Open Bus Landing Page");
        homePage.openVerticalPageDeeplink("Bus & Shuttle", language, currency);

        step("Skip coach mark");
        busSearchPage.skipCoachMark();

        step("Input origin location");
        busSearchPage.inputOriginLocation(originLocation);
        Assertion.assertThat("is origin selected",busSearchPage.isDepartureSelected(originLocation),is(true));
    }

    @Author(names = {"Ashish"})
    @Module(name = "CheckDepartureLocation")
    @Tribe(name = "Bus")
    @Vertical(name = "GroundTransport")
    @Test
    public void testToCheckDepartureLocationGuest(){
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> testProperties = getParam("testToCheckDepartureLocationGuest");
        BaseTest.paramsThreadLocal.set(testProperties);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        BusSearchPage<WebDriver> busSearchPage = BusSearchPage.getInstance(session);

        String currency = testProperties.get("currency");
        String originLocation = testProperties.get("originLocation");

        step("User open app");
        homePage.gotoHome();

        step("User input credential");
        loginPage.loginAsGuest();

        if(Configs.ENVIRONMENT == Environment.PROD) {
            step("Change currency to 'IDR'");
            homePage.changeCurrency("IDR","");
        }

        step("Get App Language");
        String language = homePage.getLanguageApp();

        step("Open Bus Landing Page");
        homePage.openVerticalPageDeeplink("Bus", language, currency);

        step("Skip coach mark");
        busSearchPage.skipCoachMark();

        step("Input origin location");
        busSearchPage.inputOriginLocation(originLocation);
        Assertion.assertThat("is origin selected",busSearchPage.isDepartureSelected(originLocation),is(true));
    }

    @Author(names = {"Prerna"})
    @Module(name = "homePageWithoutBus&Shuttle")
    @Tribe(name = "Bus")
    @Vertical(name = "GroundTransport")
    @Test
    public void testVerifyHomePageWithoutBusAndShuttleNonIDRGuest() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testVerifyHomePageWithoutBusAndShuttleNonIDRGuest");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);

        String currency = param.get("currency");

        step("user open app");
        homePage.gotoHome();

        step("user login as guest");
        loginPage.loginAsGuest();

        step("change currency to default IDR");
        homePage.changeCurrency("IDR","");

        step("Change currency");
        homePage.changeCurrency(currency,"");

        step("Find Bus and Shuttle Vertical");
        Assertion.assertThat("is Bus & Shuttle not displayed in non-IDR currency {"+currency+" }",homePage.checkVerticalPresence("Bus & Shuttle"),is(false));
    }

    @Author(names = {"Prerna"})
    @Module(name = "homePageWithoutBus&Shuttle")
    @Tribe(name = "Bus")
    @Vertical(name = "GroundTransport")
    @Test
    public void testVerifyHomePageWithoutBusAndShuttleNonIDRLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testVerifyHomePageWithoutBusAndShuttleNonIDRLogin");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);

        String currency = param.get("currency");
        String loginMethod = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");

        step("user open app");
        homePage.gotoHome();

        step("User input credential");
        loginPage.loginWith(loginMethod, username, password);

        step("change currency to default IDR");
        homePage.changeCurrency("IDR",username);

        step("Change currency");
        homePage.changeCurrency(currency,username);

        step("Find Bus and Shuttle Vertical");
        Assertion.assertThat("is Bus & Shuttle not displayed in non-IDR currency {"+currency+" }",homePage.checkVerticalPresence("Bus & Shuttle"),is(false));
    }

    @Author(names = {"Dendi"})
    @Module(name = "FilteAndSort")
    @Tribe(name = "Bus")
    @Vertical(name = "GroundTransport")
    @Test
    public void testFilterAndSortGuest(){
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> testProperties = getParam("testFilterAndSortGuest");
        BaseTest.paramsThreadLocal.set(testProperties);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        BusSearchPage<WebDriver> busSearchPage = BusSearchPage.getInstance(session);
        BusSearchResultPage<WebDriver> busSearchResultPage = BusSearchResultPage.getInstance(session);

        String currency = testProperties.get("currency");
        String originLocation = testProperties.get("originLocation");
        String destinationLocation = testProperties.get("destinationLocation");
        String busOperator = testProperties.get("busOperator");

        step("User open app");
        homePage.gotoHome();

        step("Logout via deeplink");
        homePage.logoutViaDeeplink();

        if(Configs.ENVIRONMENT == Environment.PROD) {
            step("Change currency to 'IDR'");
            homePage.changeCurrency("IDR","");
        }

        step("Get App Language");
        String language = homePage.getLanguageApp();

        step("Open Bus Landing Page");
        homePage.openVerticalPageDeeplink("Bus", language, currency);

        step("Skip coach mark");
        busSearchPage.skipCoachMark();

        step("Switch to oneway");
        busSearchPage.switchTripType("oneway");

        step("Input origin location");
        busSearchPage.inputOriginLocation(originLocation);

        step("Input destination location");
        busSearchPage.inputDestinationLocation(destinationLocation);

        step("Choose tomorrow date");
        busSearchPage.chooseTomorrowDate();

        step("Click search bus");
        busSearchPage.clickSearchBus();

        step("open skip srp coachmark");
        busSearchResultPage.skipCoachMark();

        step("Open filter and sort");
        busSearchResultPage.openFilterAndSort();

        step("Select bus operator");
        String selectedOperator = busSearchResultPage.selectFilterByOperator(busOperator);
        busSearchResultPage.applyFilter();

        step("Assert that all bus operator is "+busOperator);
        Assertion.assertThat("verify all bus operator filter apply", busSearchResultPage.verifyAllCardsAreFromSelectedOperator(selectedOperator), is(true));
    }

    @Author(names = {"Dendi"})
    @Module(name = "FilteAndSort")
    @Tribe(name = "Bus")
    @Vertical(name = "GroundTransport")
    @Test
    public void testFilterAndSortLogin(){
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> testProperties = getParam("testFilterAndSortLogin");
        BaseTest.paramsThreadLocal.set(testProperties);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        BusSearchPage<WebDriver> busSearchPage = BusSearchPage.getInstance(session);
        BusSearchResultPage<WebDriver> busSearchResultPage = BusSearchResultPage.getInstance(session);

        String currency = testProperties.get("currency");
        String originLocation = testProperties.get("originLocation");
        String destinationLocation = testProperties.get("destinationLocation");
        String username = testProperties.get("username");
        String password = testProperties.get("password");
        String loginMethod = testProperties.get("loginType");
        String busOperator = testProperties.get("busOperator");

        step("User open app");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink (if needed) and change currency to " + currency);
        homePage.loginIfLoggedOutAndChangeCurrencyViaDeeplink(username, password, currency);

        step("Get App Language");
        String language = homePage.getLanguageApp();

        step("Open Bus Landing Page");
        homePage.openVerticalPageDeeplink("Bus & Shuttle", language, currency);

        step("Skip coach mark");
        busSearchPage.skipCoachMark();

        step("Switch to oneway");
        busSearchPage.switchTripType("oneway");

        step("Input origin location");
        busSearchPage.inputOriginLocation(originLocation);

        step("Input destination location");
        busSearchPage.inputDestinationLocation(destinationLocation);

        step("Choose tomorrow date");
        busSearchPage.chooseTomorrowDate();

        step("Click search bus");
        busSearchPage.clickSearchBus();

        step("open skip srp coachmark");
        busSearchResultPage.skipCoachMark();

        step("Open filter and sort");
        busSearchResultPage.openFilterAndSort();

        step("Select bus operator");
        String selectedOperator = busSearchResultPage.selectFilterByOperator(busOperator);
        busSearchResultPage.applyFilter();

        step("Assert that all bus operator is "+busOperator);
        Assertion.assertThat("verify all bus operator filter apply", busSearchResultPage.verifyAllCardsAreFromSelectedOperator(selectedOperator), is(true));
    }

    @Author(names = {"Ashish"})
    @Module(name = "searchBusOneWay")
    @Tribe(name = "Bus")
    @Vertical(name = "GroundTransport")
    @Test
    public void testToCheckSearchBusOneWayLogin(){
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> testProperties = getParam("testToCheckSearchBusOneWayLogin");
        BaseTest.paramsThreadLocal.set(testProperties);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        BusSearchPage<WebDriver> busSearchPage = BusSearchPage.getInstance(session);
        BusSearchResultPage<WebDriver> busSearchResultPage = BusSearchResultPage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);

        String loginMethod = testProperties.get("loginType");
        String username = testProperties.get("username");
        String password = testProperties.get("password");

        String tripType = testProperties.get("tripType");
        String originLocation = testProperties.get("originLocation");
        String destinationLocation = testProperties.get("destinationLocation");
        int daysToDeparture = Integer.parseInt(testProperties.get("daysToDeparture"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String dateDeparture = CoreFunctions.addToLongDate(currentDate,daysToDeparture);
        int passenger = Integer.parseInt(testProperties.get("passenger"));

        step("User open app");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink (if needed) and change currency to IDR");
        homePage.loginIfLoggedOutAndChangeCurrencyViaDeeplink(username, password, "IDR");

        step("Get App Language");
        String language = homePage.getLanguageApp();

        step("Open Bus Landing Page");
        homePage.openVerticalPageDeeplink("Bus & Shuttle", language, "IDR");

        step("Skip coachmark");
        busSearchPage.skipCoachMark();

        step("Switch trip type to oneway");
        busSearchPage.switchTripType(tripType);

        step("Input origin location");
        busSearchPage.inputOriginLocation(originLocation);
        Assertion.assertThat("is origin selected",busSearchPage.isDepartureSelected(originLocation),is(true));

        step("Input destination location");
        busSearchPage.inputDestinationLocation(destinationLocation);
        Assertion.assertThat("is origin selected",busSearchPage.isDestinationSelected(destinationLocation),is(true));

        step("Select date");
        busSearchPage.selectDate(dateDeparture);
        Assertion.assertThat("is calendar selected",busSearchPage.isDateSelected(dateDeparture),is(true));

        step("Select pax amount");
        busSearchPage.selectPassenger(passenger);
        Assertion.assertThat("is correct pax selected",busSearchPage.isPaxSelected(passenger),is(true));

        step("Click search bus");
        busSearchPage.clickSearchBus();

        step("Skip bus srp coach mark");
        busSearchResultPage.skipCoachMark();

        step("Verify origin, destination, and travel date on srp");
        Assertion.assertThat("is on srp ",busSearchResultPage.isOnSrp(),is(true));
        Assertion.assertThat("is search correct",busSearchResultPage.verifySearchedDetail(originLocation,destinationLocation,dateDeparture),is(true));
    }

    @Author(names = {"Ashish"})
    @Module(name = "searchBusOneWay")
    @Tribe(name = "Bus")
    @Vertical(name = "GroundTransport")
    @Test
    public void testToCheckSearchBusOneWayGuest(){
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> testProperties = getParam("testToCheckSearchBusOneWayGuest");
        BaseTest.paramsThreadLocal.set(testProperties);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        BusSearchPage<WebDriver> busSearchPage = BusSearchPage.getInstance(session);
        BusSearchResultPage<WebDriver> busSearchResultPage = BusSearchResultPage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);

        String tripType = testProperties.get("tripType");
        String originLocation = testProperties.get("originLocation");
        String destinationLocation = testProperties.get("destinationLocation");
        int daysToDeparture = Integer.parseInt(testProperties.get("daysToDeparture"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String dateDeparture = CoreFunctions.addToLongDate(currentDate,daysToDeparture);
        int passenger = Integer.parseInt(testProperties.get("passenger"));

        step("User open app");
        homePage.gotoHome();

        step("Logout via deeplink");
        homePage.logoutViaDeeplink();

        if(Configs.ENVIRONMENT == Environment.PROD) {
            step("Change currency to 'IDR'");
            homePage.changeCurrency("IDR","");
        }

        step("Get App Language");
        String language = homePage.getLanguageApp();

        step("Open Bus Landing Page");
        homePage.openVerticalPageDeeplink("Bus", language, "IDR");

        step("Skip coachmark");
        busSearchPage.skipCoachMark();

        step("Switch trip type to oneway");
        busSearchPage.switchTripType(tripType);

        step("Input origin location");
        busSearchPage.inputOriginLocation(originLocation);
        Assertion.assertThat("is origin selected",busSearchPage.isDepartureSelected(originLocation),is(true));

        step("Input destination location");
        busSearchPage.inputDestinationLocation(destinationLocation);
        Assertion.assertThat("is origin selected",busSearchPage.isDestinationSelected(destinationLocation),is(true));

        step("Select date");
        busSearchPage.selectDate(dateDeparture);
        Assertion.assertThat("is calendar selected",busSearchPage.isDateSelected(dateDeparture),is(true));

        step("Select pax amount");
        busSearchPage.selectPassenger(passenger);
        Assertion.assertThat("is correct pax selected",busSearchPage.isPaxSelected(passenger),is(true));

        step("Click search bus");
        busSearchPage.clickSearchBus();

        step("Skip bus srp coach mark");
        busSearchResultPage.skipCoachMark();

        step("Verify origin, destination, and travel date on srp");
        Assertion.assertThat("is on srp ",busSearchResultPage.isOnSrp(),is(true));
        Assertion.assertThat("is search correct",busSearchResultPage.verifySearchedDetail(originLocation,destinationLocation,dateDeparture),is(true));
    }

    @Author(names = {"Kartik"})
    @Module(name = "Bus")
    @Tribe(name = "NFT")
    @Vertical(name = "GroundTransport")
    @Test
    public void Bus_testBookBusOrderDetailLogin(){
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> testProperties = getParam("Bus_testBookBusOrderDetailLogin");
        BaseTest.paramsThreadLocal.set(testProperties);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        BusSearchPage<WebDriver> busSearchPage = BusSearchPage.getInstance(session);
        BusSearchResultPage<WebDriver> busSearchResultPage = BusSearchResultPage.getInstance(session);
        BuspdpPage<WebDriver> buspdpPage = BuspdpPage.getInstance(session);
        BusBookingFormPage<WebDriver> busBookingFormPage = BusBookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        OrderListWeb<?> orderListPageWeb = OrderListWeb.getInstance(session);

        String loginMethod = testProperties.get("loginType");
        String username = testProperties.get("username");
        String password = testProperties.get("password");

        String tripType = testProperties.get("tripType");
        String originLocation = testProperties.get("originLocation");
        String destinationLocation = testProperties.get("destinationLocation");
        int daysToDeparture = Integer.parseInt(testProperties.get("daysToDeparture"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String dateDeparture = CoreFunctions.addToLongDate(currentDate,daysToDeparture);
        int duration = Integer.parseInt(testProperties.get("duration"));
        String dateReturn = CoreFunctions.addToLongDate(dateDeparture,duration);
        int passenger = Integer.parseInt(testProperties.get("passenger"));

        step("User open app");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink (if needed) and change currency to IDR");
        homePage.loginIfLoggedOutAndChangeCurrencyViaDeeplink(username, password, "IDR");

        step("Get App Language");
        String language = homePage.getLanguageApp();

        step("Open Bus Landing Page");
        homePage.openVerticalPageDeeplink("Bus & Shuttle", language, "IDR");

        step("Skip coachmark");
        busSearchPage.skipCoachMark();

        step("Switch trip type to roundtrip");
        busSearchPage.switchTripType(tripType);

        step("Input origin location");
        busSearchPage.inputOriginLocation(originLocation);

        step("Input destination location");
        busSearchPage.inputDestinationLocation(destinationLocation);

        step("Select date");
        busSearchPage.selectDate(dateDeparture,dateReturn);

        step("Select pax amount");
        busSearchPage.selectPassenger(passenger);

        step("Click search bus");
        busSearchPage.clickSearchBus();

        step("Skip bus srp coachmark");
        busSearchResultPage.skipCoachMark();

        step("Select bus departure on srp");
        busSearchResultPage.selectFirstBus();

        step("select seat departure");
        buspdpPage.openSeat();
        buspdpPage.clickSelectSeatButton();

        step("Select bus return on srp");
        busSearchResultPage.selectFirstBus();

        step("select seat return");
        buspdpPage.openSeat();
        buspdpPage.clickSelectSeatButton();

        step("Input contact detail");
        busBookingFormPage.togglePassengerSameAsContact();
        busBookingFormPage.fillPassengerData(passenger);

        step("Get data from Booking Form Page");
        String[] busPassengerDetailArray = busBookingFormPage.getBusPassengerNameDetail().split(" ");
        busPassengerDetailArray = Arrays.copyOfRange(busPassengerDetailArray,1,busPassengerDetailArray.length);

        String busPassengerDetail = String.join(" ", busPassengerDetailArray);
        String getSeatNoDetail = busBookingFormPage.getSeatNoDetail();
        String getDepartureDate = busBookingFormPage.getDepartureDate();
        String getReturnDate = busBookingFormPage.getReturnDate();
        String totalPriceBookingForm = busBookingFormPage.getTotalPrice();

        step("Continue to payment page");
        busBookingFormPage.continueToPaymentPage();

        step("Verify user get orderId");
        String orderID = paymentPage.checkOrderID();
        String totalPrice = paymentPage.getTotalPrice();
        Assertion.assertThat("verify total price on booking form page and payment page",totalPrice,is(totalPriceBookingForm));

        step("Make success Payment");
        paymentPage.openPaymentMethod();
        paymentPage.selectPaymentMethod("Sakuku");
        paymentPage.clickPayButton();

        step("Verify payment success");
        Assertion.assertThat("Payment should be successful for the Bus booking",
                paymentPage.isThankYouPageShown(), is(true));

        step("Open order detail");
        OrderListWeb.BusRoundTripOrderDetailIds busTripIds = orderListPageWeb.getBusRoundTripOrderDetailIdsByBusTrip(orderID);
        String orderHashId = busTripIds.orderHash;
        String orderDetailIdDeparture = busTripIds.departureOrderDetailId;
        String orderDetailIdReturn = busTripIds.returnOrderDetailId;
        Assertion.assertThat("Bus DEPART orderDetailId resolved from internal order", orderDetailIdDeparture, is(notNullValue()));
        Assertion.assertThat("Bus RETURN orderDetailId resolved from internal order", orderDetailIdReturn, is(notNullValue()));

        step("Departure Order Detail Page");
        Assertion.assertThat("Verify user get orderId", !orderID.isBlank(), is(true));
        orderListPage.openActiveTicketViaDeeplink(orderID, orderHashId, orderDetailIdDeparture);
        String busDepartureDetail =  orderDetailPage.getBusDepartureDate(orderID,orderHashId,orderDetailIdDeparture);
        Assertion.assertThat("Verify Departure date on Order Detail Page",busDepartureDetail, containsStringIgnoringCase(getDepartureDate));
        orderDetailPage.hidePromoBannerIfPresent();
        String passengerDetailOrderPage1 = orderDetailPage.getBusPassengerDetails();
        String busSeatNoDetail1 = orderDetailPage.getBusSeatNoDetail();

        step("Return Order Detail Page");
        Assertion.assertThat("Verify user get orderId", !orderID.isBlank(), is(true));
        orderListPage.openActiveTicketViaDeeplink(orderID, orderHashId, orderDetailIdReturn);
        String busReturnDetail =  orderDetailPage.getBusReturnDate(orderID,orderHashId,orderDetailIdReturn);
        Assertion.assertThat("Verify Return date on Order Detail Page",busReturnDetail, containsStringIgnoringCase(getReturnDate));
        orderDetailPage.hidePromoBannerIfPresent();
        String passengerDetailOrderPage2 = orderDetailPage.getBusPassengerDetails();
        String busSeatNoDetail2 = orderDetailPage.getBusSeatNoDetail();

        String busSeatNoDetail = String.join(", ", new String[] {busSeatNoDetail1,busSeatNoDetail2});
        String passengerDetailOrderPage = String.join(", ", new String[] {passengerDetailOrderPage1,passengerDetailOrderPage2});
        Assertion.assertThat("Bus seat number details on booking form page and order detail page is same",busSeatNoDetail,is(getSeatNoDetail));
        Assertion.assertThat("Verify Passenger detail on Order Detail Page",passengerDetailOrderPage,containsString(busPassengerDetail));
    }

    @Author(names = {"Kartik"})
    @Module(name = "CompleteBooking")
    @Tribe(name = "Bus")
    @Vertical(name = "GroundTransport")
    @Test
    public void testBookBusOrderDetailGuest(){
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> testProperties = getParam("testBookBusOrderDetailGuest");
        BaseTest.paramsThreadLocal.set(testProperties);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        BusSearchPage<WebDriver> busSearchPage = BusSearchPage.getInstance(session);
        BusSearchResultPage<WebDriver> busSearchResultPage = BusSearchResultPage.getInstance(session);
        BuspdpPage<WebDriver> buspdpPage = BuspdpPage.getInstance(session);
        BusBookingFormPage<WebDriver> busBookingFormPage = BusBookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        OrderListWeb<?> orderListPageWeb = OrderListWeb.getInstance(session);

        String tripType = testProperties.get("tripType");
        String originLocation = testProperties.get("originLocation");
        String destinationLocation = testProperties.get("destinationLocation");
        int daysToDeparture = Integer.parseInt(testProperties.get("daysToDeparture"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String dateDeparture = CoreFunctions.addToLongDate(currentDate,daysToDeparture);
        int duration = Integer.parseInt(testProperties.get("duration"));
        String dateReturn = CoreFunctions.addToLongDate(dateDeparture,duration);
        int passenger = Integer.parseInt(testProperties.get("passenger"));

        String fullName = CoreFunctions.randomName();
        String phoneNumber =CoreFunctions.randomPhone();
        String emailAddress = CoreFunctions.randomEmail();

        step("User open app");
        homePage.gotoHome();

        step("Logout via deeplink");
        homePage.logoutViaDeeplink();

        if(Configs.ENVIRONMENT == Environment.PROD) {
            step("Change currency to 'IDR'");
            homePage.changeCurrency("IDR","");
        }

        step("Get App Language");
        String language = homePage.getLanguageApp();

        step("Open Bus Landing Page");
        homePage.openVerticalPageDeeplink("Bus", language, "IDR");

        step("Skip coachmark");
        busSearchPage.skipCoachMark();

        step("Switch trip type to round trip");
        busSearchPage.switchTripType(tripType);

        step("Input origin location");
        busSearchPage.inputOriginLocation(originLocation);

        step("Input destination location");
        busSearchPage.inputDestinationLocation(destinationLocation);

        step("Select date");
        busSearchPage.selectDate(dateDeparture,dateReturn);

        step("Select pax amount");
        busSearchPage.selectPassenger(passenger);

        step("Click search bus");
        busSearchPage.clickSearchBus();

        step("Skip bus srp coachmark");
        busSearchResultPage.skipCoachMark();

        step("Select bus departure on srp");
        busSearchResultPage.selectFirstBus();

        step("select seat departure");
        buspdpPage.openSeat();
        buspdpPage.clickReturnSeatButton();

        step("Select bus return on srp");
        busSearchResultPage.selectFirstBus();

        step("select seat return");
        buspdpPage.openSeat();
        buspdpPage.clickSelectSeatButton();

        step("Input guest contact detail");
        busBookingFormPage.inputGuestContactDetail(fullName,phoneNumber,emailAddress);
        busBookingFormPage.inputPassengerGuestDetail();
        busBookingFormPage.fillPassengerData(passenger);

        step("Get data from Booking Form Page");
        String getSeatNoDetail = busBookingFormPage.getSeatNoDetail();
        String getDepartureDate = busBookingFormPage.getDepartureDate();
        String getReturnDate = busBookingFormPage.getReturnDate();
        String totalPriceBookingForm = busBookingFormPage.getTotalPrice();

        step("Continue to payment page");
        busBookingFormPage.continueToPaymentPage();

        step("Verify user get orderId");
        String orderID = paymentPage.checkOrderID();
        String totalPrice = paymentPage.getTotalPrice();

        step("Make success Payment");
        paymentPage.openPaymentMethod();
        paymentPage.selectPaymentMethod("BCA Virtual Account");
        paymentPage.clickPayButton();

        step("Open order detail on order list page");
        paymentPage.seeOrderList();
        orderListPage.openOrderDetailWaitingForPayment(orderID);
        Assertion.assertThat("verify total price on booking form page and payment page",totalPrice,is(totalPriceBookingForm));
        Assertion.assertThat("Verify user get orderId", !orderID.isBlank(), is(true));

        step("Open order detail");
        OrderListWeb.BusRoundTripOrderDetailIds busTripIds = orderListPageWeb.getBusRoundTripOrderDetailIdsByBusTrip(orderID);
        String orderHashId = busTripIds.orderHash;
        String orderDetailIdDeparture = busTripIds.departureOrderDetailId;
        String orderDetailIdReturn = busTripIds.returnOrderDetailId;
        Assertion.assertThat("Bus DEPART orderDetailId resolved from internal order", orderDetailIdDeparture, is(notNullValue()));
        Assertion.assertThat("Bus RETURN orderDetailId resolved from internal order", orderDetailIdReturn, is(notNullValue()));

        step("Departure Order Detail Page");
        Assertion.assertThat("Verify user get orderId", !orderID.isBlank(), is(true));
        orderListPage.openActiveTicketViaDeeplink(orderID, orderHashId, orderDetailIdDeparture);
        String busDepartureDetail =  orderDetailPage.getBusDepartureDate(orderID,orderHashId,orderDetailIdDeparture);
        Assertion.assertThat("Verify Departure date on Order Detail Page",busDepartureDetail, containsStringIgnoringCase(getDepartureDate));
        orderDetailPage.hidePromoBannerIfPresent();
        String PassengerDetailOrderPage1 = orderDetailPage.getBusPassengerDetails();
        String busSeatNoDetail1 = orderDetailPage.getBusSeatNoDetail();

        step("Return Order Detail Page");
        Assertion.assertThat("Verify user get orderId", !orderID.isBlank(), is(true));
        orderListPage.openActiveTicketViaDeeplink(orderID, orderHashId, orderDetailIdReturn);
        String busReturnDetail =  orderDetailPage.getBusReturnDate(orderID,orderHashId,orderDetailIdReturn);
        Assertion.assertThat("Verify Return date on Order Detail Page",busReturnDetail, containsStringIgnoringCase(getReturnDate));
        orderDetailPage.hidePromoBannerIfPresent();
        String PassengerDetailOrderPage2 = orderDetailPage.getBusPassengerDetails();
        String busSeatNoDetail2 = orderDetailPage.getBusSeatNoDetail();

        String busSeatNoDetail = String.join(", ", new String[] {busSeatNoDetail1,busSeatNoDetail2});
        String PassengerDetailOrderPage = String.join(", ", new String[] {PassengerDetailOrderPage1,PassengerDetailOrderPage2});
        Assertion.assertThat("Bus seat number details on booking form page and order detail page is same",busSeatNoDetail,is(getSeatNoDetail));
        Assertion.assertThat("Verify Passenger detail on Order Detail Page",PassengerDetailOrderPage,containsString(fullName));
    }

    @Author(names = {"Ashish"})
    @Module(name = "searchBusRoundTrip")
    @Tribe(name = "Bus")
    @Vertical(name = "GroundTransport")
    @Test
    public void testToCheckSearchBusRoundTripLogin(){
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> testProperties = getParam("testToCheckSearchBusRoundTripLogin");
        BaseTest.paramsThreadLocal.set(testProperties);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        BusSearchPage<WebDriver> busSearchPage = BusSearchPage.getInstance(session);
        BusSearchResultPage<WebDriver> busSearchResultPage = BusSearchResultPage.getInstance(session);
        BuspdpPage<WebDriver> buspdpPage = BuspdpPage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);

        String loginMethod = testProperties.get("loginType");
        String username = testProperties.get("username");
        String password = testProperties.get("password");

        String tripType = testProperties.get("tripType");
        String originLocation = testProperties.get("originLocation");
        String destinationLocation = testProperties.get("destinationLocation");
        int daysToDeparture = Integer.parseInt(testProperties.get("daysToDeparture"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String dateDeparture = CoreFunctions.addToLongDate(currentDate,daysToDeparture);
        int duration = Integer.parseInt(testProperties.get("duration"));
        String dateReturn = CoreFunctions.addToLongDate(dateDeparture,duration);
        int passenger = Integer.parseInt(testProperties.get("passenger"));

        step("User open app");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink (if needed) and change currency to IDR");
        homePage.loginIfLoggedOutAndChangeCurrencyViaDeeplink(username, password, "IDR");

        step("Get App Language");
        String language = homePage.getLanguageApp();

        step("Open Bus Landing Page");
        homePage.openVerticalPageDeeplink("Bus", language, "IDR");

        step("Skip coachmark");
        busSearchPage.skipCoachMark();

        step("Switch trip type to round trip");
        busSearchPage.switchTripType(tripType);

        step("Input origin location");
        busSearchPage.inputOriginLocation(originLocation);
        Assertion.assertThat("is origin selected",busSearchPage.isDepartureSelected(originLocation),is(true));

        step("Input destination location");
        busSearchPage.inputDestinationLocation(destinationLocation);
        Assertion.assertThat("is origin selected",busSearchPage.isDestinationSelected(destinationLocation),is(true));

        step("Select date");
        busSearchPage.selectDate(dateDeparture,dateReturn);
        Assertion.assertThat("is calendar selected departure date",busSearchPage.isDateSelected(dateDeparture),is(true));
        Assertion.assertThat("is calendar selected return date",busSearchPage.isDateSelected(dateReturn),is(true));

        step("Select pax amount");
        busSearchPage.selectPassenger(passenger);
        Assertion.assertThat("is correct pax selected",busSearchPage.isPaxSelected(passenger),is(true));

        step("Click search bus");
        busSearchPage.clickSearchBus();

        step("Skip bus srp coach mark");
        busSearchResultPage.skipCoachMark();

        step("Verify origin, destination, and travel date on srp");
        Assertion.assertThat("is on srp ",busSearchResultPage.isOnSrp(),is(true));
        Assertion.assertThat("is search correct",busSearchResultPage.verifySearchedDetail(originLocation,destinationLocation,dateDeparture),is(true));

        step("Select bus departure on srp");
        busSearchResultPage.selectFirstBus();

        step("select seat departure");
        buspdpPage.openSeat();
        buspdpPage.clickSelectSeatButton();

        step("Verify origin, destination, and travel date on srp");
        Assertion.assertThat("is on srp ",busSearchResultPage.isOnSrp(),is(true));
        Assertion.assertThat("is search correct",busSearchResultPage.verifySearchedDetail(destinationLocation,originLocation,dateReturn),is(true));
    }

    @Author(names = {"Ashish"})
    @Module(name = "searchBusRoundTrip")
    @Tribe(name = "Bus")
    @Vertical(name = "GroundTransport")
    @Test
    public void testToCheckSearchBusRoundTripGuest(){
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> testProperties = getParam("testToCheckSearchBusRoundTripGuest");
        BaseTest.paramsThreadLocal.set(testProperties);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        BusSearchPage<WebDriver> busSearchPage = BusSearchPage.getInstance(session);
        BusSearchResultPage<WebDriver> busSearchResultPage = BusSearchResultPage.getInstance(session);
        BuspdpPage<WebDriver> buspdpPage = BuspdpPage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);

        String tripType = testProperties.get("tripType");
        String originLocation = testProperties.get("originLocation");
        String destinationLocation = testProperties.get("destinationLocation");
        int daysToDeparture = Integer.parseInt(testProperties.get("daysToDeparture"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String dateDeparture = CoreFunctions.addToLongDate(currentDate,daysToDeparture);
        int duration = Integer.parseInt(testProperties.get("duration"));
        String dateReturn = CoreFunctions.addToLongDate(dateDeparture,duration);
        int passenger = Integer.parseInt(testProperties.get("passenger"));

        step("User open app");
        homePage.gotoHome();

        step("Logout via deeplink");
        homePage.logoutViaDeeplink();

        if(Configs.ENVIRONMENT == Environment.PROD) {
            step("Change currency to 'IDR'");
            homePage.changeCurrency("IDR","");
        }

        step("Get App Language");
        String language = homePage.getLanguageApp();

        step("Open Bus Landing Page");
        homePage.openVerticalPageDeeplink("Bus", language, "IDR");

        step("Skip coachmark");
        busSearchPage.skipCoachMark();

        step("Switch trip type to round trip");
        busSearchPage.switchTripType(tripType);

        step("Input origin location");
        busSearchPage.inputOriginLocation(originLocation);
        Assertion.assertThat("is origin selected",busSearchPage.isDepartureSelected(originLocation),is(true));

        step("Input destination location");
        busSearchPage.inputDestinationLocation(destinationLocation);
        Assertion.assertThat("is origin selected",busSearchPage.isDestinationSelected(destinationLocation),is(true));

        step("Select date");
        busSearchPage.selectDate(dateDeparture,dateReturn);
        Assertion.assertThat("is calendar selected departure date",busSearchPage.isDateSelected(dateDeparture),is(true));
        Assertion.assertThat("is calendar selected return date",busSearchPage.isDateSelected(dateReturn),is(true));

        step("Select pax amount");
        busSearchPage.selectPassenger(passenger);
        Assertion.assertThat("is correct pax selected",busSearchPage.isPaxSelected(passenger),is(true));

        step("Click search bus");
        busSearchPage.clickSearchBus();

        step("Skip bus srp coach mark");
        busSearchResultPage.skipCoachMark();

        step("Verify origin, destination, and travel date on srp departure");
        Assertion.assertThat("is on srp departure",busSearchResultPage.isOnSrp(),is(true));
        Assertion.assertThat("is search correct departure",busSearchResultPage.verifySearchedDetail(originLocation,destinationLocation,dateDeparture),is(true));

        step("Select bus departure on srp");
        busSearchResultPage.selectFirstBus();

        step("select seat departure");
        buspdpPage.openSeat();
        buspdpPage.clickSelectSeatButton();

        step("Verify origin, destination, and travel date on srp return");
        Assertion.assertThat("is on srp return",busSearchResultPage.isOnSrp(),is(true));
        Assertion.assertThat("is search correct return",busSearchResultPage.verifySearchedDetail(destinationLocation,originLocation,dateReturn),is(true));
    }

    @Author(names = {"Dendi"})
    @Module(name = "BusBookingDetail")
    @Tribe(name = "Bus")
    @Vertical(name = "GroundTransport")
    @Test
    public void testGuestBusBookingFormSummary(){
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> testProperties = getParam("testGuestBusBookingFormSummary");
        BaseTest.paramsThreadLocal.set(testProperties);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        BusSearchPage<WebDriver> busSearchPage = BusSearchPage.getInstance(session);
        BusSearchResultPage<WebDriver> busSearchResultPage = BusSearchResultPage.getInstance(session);
        BuspdpPage<WebDriver> buspdpPage = BuspdpPage.getInstance(session);
        BusBookingFormPage<WebDriver> busBookingFormPage = BusBookingFormPage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        
        String tripType = testProperties.get("tripType");
        String originLocation = testProperties.get("originLocation");
        String destinationLocation = testProperties.get("destinationLocation");
        int daysToDeparture = Integer.parseInt(testProperties.get("daysToDeparture"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String dateDeparture = CoreFunctions.addToLongDate(currentDate,daysToDeparture);
        int duration = Integer.parseInt(testProperties.get("duration"));
        String dateReturn = CoreFunctions.addToLongDate(dateDeparture,duration);
        int passenger = Integer.parseInt(testProperties.get("passenger"));

        step("User open app");
        homePage.gotoHome();

        step("Logout via deeplink");
        homePage.logoutViaDeeplink();

        if(Configs.ENVIRONMENT == Environment.PROD) {
            step("Change currency to 'IDR'");
            homePage.changeCurrency("IDR","");
        }

        step("Get App Language");
        String language = homePage.getLanguageApp();

        step("Open Bus Landing Page");
        homePage.openVerticalPageDeeplink("Bus", language, "IDR");

        step("Skip coachmark");
        busSearchPage.skipCoachMark();

        step("Switch trip type to one way");
        busSearchPage.switchTripType(tripType);

        step("Input origin location");
        busSearchPage.inputOriginLocation(originLocation);

        step("Input destination location");
        busSearchPage.inputDestinationLocation(destinationLocation);

        step("Select date");
        busSearchPage.selectDate(dateDeparture,dateReturn);

        step("Select pax amount");
        busSearchPage.selectPassenger(passenger);

        step("Click search bus");
        busSearchPage.clickSearchBus();

        step("Skip bus srp coachmark");
        busSearchResultPage.skipCoachMark();

        step("Select bus departure on srp");
        BusSrpOneWayData busSrpData = busSearchResultPage.getFirstBusData();
        busSearchResultPage.selectFirstBus();

        step("select seat departure");
        buspdpPage.openSeat();
        buspdpPage.clickSelectSeatButton();

        step("Verify booking form summary");
        Assertion.assertThat("verify booking form", busBookingFormPage.verifyBookingFormSummary(busSrpData), is(true));
    }

    @Author(names = {"Dendi"})
    @Module(name = "BusBookingDetail")
    @Tribe(name = "Bus")
    @Vertical(name = "GroundTransport")
    @Test
    public void testLoginBusBookingFormSummary(){
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> testProperties = getParam("testLoginBusBookingFormSummary");
        BaseTest.paramsThreadLocal.set(testProperties);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        BusSearchPage<WebDriver> busSearchPage = BusSearchPage.getInstance(session);
        BusSearchResultPage<WebDriver> busSearchResultPage = BusSearchResultPage.getInstance(session);
        BuspdpPage<WebDriver> buspdpPage = BuspdpPage.getInstance(session);
        BusBookingFormPage<WebDriver> busBookingFormPage = BusBookingFormPage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);

        String loginMethod = testProperties.get("loginType");
        String username = testProperties.get("username");
        String password = testProperties.get("password");
        
        String tripType = testProperties.get("tripType");
        String originLocation = testProperties.get("originLocation");
        String destinationLocation = testProperties.get("destinationLocation");
        int daysToDeparture = Integer.parseInt(testProperties.get("daysToDeparture"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String dateDeparture = CoreFunctions.addToLongDate(currentDate,daysToDeparture);
        int duration = Integer.parseInt(testProperties.get("duration"));
        String dateReturn = CoreFunctions.addToLongDate(dateDeparture,duration);
        int passenger = Integer.parseInt(testProperties.get("passenger"));

        step("User open app");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink (if needed) and change currency to IDR");
        homePage.loginIfLoggedOutAndChangeCurrencyViaDeeplink(username, password, "IDR");

        step("Get App Language");
        String language = homePage.getLanguageApp();

        step("Open Bus Landing Page");
        homePage.openVerticalPageDeeplink("Bus & Shuttle", language, "IDR");

        step("Skip coachmark");
        busSearchPage.skipCoachMark();

        step("Switch trip type to one way");
        busSearchPage.switchTripType(tripType);

        step("Input origin location");
        busSearchPage.inputOriginLocation(originLocation);

        step("Input destination location");
        busSearchPage.inputDestinationLocation(destinationLocation);

        step("Select date");
        busSearchPage.selectDate(dateDeparture,dateReturn);

        step("Select pax amount");
        busSearchPage.selectPassenger(passenger);

        step("Click search bus");
        busSearchPage.clickSearchBus();

        step("Skip bus srp coachmark");
        busSearchResultPage.skipCoachMark();

        step("Select bus departure on srp");
        BusSrpOneWayData busSrpData = busSearchResultPage.getFirstBusData();
        busSearchResultPage.selectFirstBus();

        step("select seat departure");
        buspdpPage.openSeat();
        buspdpPage.clickSelectSeatButton();

        step("Verify booking form summary");
        Assertion.assertThat("verify booking form", busBookingFormPage.verifyBookingFormSummary(busSrpData), is(true));
    }

    @Author(names = {"Ashish"})
    @Module(name = "CheckDestinationLocation")
    @Tribe(name = "Bus")
    @Vertical(name = "GroundTransport")
    @Test
    public void testToCheckDestinationLocationLogin(){
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> testProperties = getParam("testToCheckDestinationLocationLogin");
        BaseTest.paramsThreadLocal.set(testProperties);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        BusSearchPage<WebDriver> busSearchPage = BusSearchPage.getInstance(session);

        String loginMethod = testProperties.get("loginType");
        String username = testProperties.get("username");
        String password = testProperties.get("password");
        String currency = testProperties.get("currency");
        String originLocation = testProperties.get("originLocation");
        String destinationLocation = testProperties.get("destinationLocation");

        step("User open app");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink (if needed) and change currency to " + currency);
        homePage.loginIfLoggedOutAndChangeCurrencyViaDeeplink(username, password, currency);

        step("Get App Language");
        String language = homePage.getLanguageApp();

        step("Open Bus Landing Page");
        homePage.openVerticalPageDeeplink("Bus & Shuttle", language, currency);

        step("Skip coach mark");
        busSearchPage.skipCoachMark();

        step("Input origin location");
        busSearchPage.inputOriginLocation(originLocation);

        step("Input destination location");
        busSearchPage.inputDestinationLocation(destinationLocation);
        Assertion.assertThat("is destination selected",busSearchPage.isDestinationSelected(destinationLocation),is(true));
    }

    @Author(names = {"Ashish"})
    @Module(name = "CheckDestinationLocation")
    @Tribe(name = "Bus")
    @Vertical(name = "GroundTransport")
    @Test
    public void testToCheckDestinationLocationGuest(){
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> testProperties = getParam("testToCheckDestinationLocationGuest");
        BaseTest.paramsThreadLocal.set(testProperties);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        BusSearchPage<WebDriver> busSearchPage = BusSearchPage.getInstance(session);

        String currency = testProperties.get("currency");
        String originLocation = testProperties.get("originLocation");
        String destinationLocation = testProperties.get("destinationLocation");

        step("User open app");
        homePage.gotoHome();

        step("Logout via deeplink");
        homePage.logoutViaDeeplink();

        if(Configs.ENVIRONMENT == Environment.PROD) {
            step("Change currency to 'IDR'");
            homePage.changeCurrency("IDR","");
        }

        step("Get App Language");
        String language = homePage.getLanguageApp();

        step("Open Bus Landing Page");
        homePage.openVerticalPageDeeplink("Bus", language, currency);

        step("Skip coach mark");
        busSearchPage.skipCoachMark();

        step("Input origin location");
        busSearchPage.inputOriginLocation(originLocation);

        step("Input destination location");
        busSearchPage.inputDestinationLocation(destinationLocation);
        Assertion.assertThat("is destination selected",busSearchPage.isDestinationSelected(destinationLocation),is(true));
    }

    @Author(names = {"kartik"})
    @Module(name = "CheckPaymentDetailOnPaymentListPage")
    @Tribe(name = "Bus")
    @Vertical(name = "GroundTransport")
    @Test
    public void testToCheckPaymentDetailOnPaymentListPageGuest(){
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> testProperties = getParam("testToCheckPaymentDetailOnPaymentListPageGuest");
        BaseTest.paramsThreadLocal.set(testProperties);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        BusSearchPage<WebDriver> busSearchPage = BusSearchPage.getInstance(session);
        BusSearchResultPage<WebDriver> busSearchResultPage = BusSearchResultPage.getInstance(session);
        BuspdpPage<WebDriver> buspdpPage = BuspdpPage.getInstance(session);
        BusBookingFormPage<WebDriver> busBookingFormPage = BusBookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);

        String tripType = testProperties.get("tripType");
        String originLocation = testProperties.get("originLocation");
        String destinationLocation = testProperties.get("destinationLocation");
        int daysToDeparture = Integer.parseInt(testProperties.get("daysToDeparture"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String dateDeparture = CoreFunctions.addToLongDate(currentDate,daysToDeparture);
        int passenger = Integer.parseInt(testProperties.get("passenger"));

        String fullName = CoreFunctions.randomName();
        String phoneNumber =CoreFunctions.randomPhone();
        String emailAddress = CoreFunctions.randomEmail();

        step("User open app");
        homePage.gotoHome();

        step("User login as guest");
        loginPage.loginAsGuest();

        if(Configs.ENVIRONMENT == Environment.PROD) {
            step("Change currency to 'IDR'");
            homePage.changeCurrency("IDR","");
        }

        step("Get App Language");
        String language = homePage.getLanguageApp();

        step("Open Bus Landing Page");
        homePage.openVerticalPageDeeplink("Bus", language, "IDR");

        step("Skip coachmark");
        busSearchPage.skipCoachMark();

        step("Switch trip type to oneway");
        busSearchPage.switchTripType(tripType);

        step("Input origin location");
        busSearchPage.inputOriginLocation(originLocation);

        step("Input destination location");
        busSearchPage.inputDestinationLocation(destinationLocation);

        step("Select date");
        busSearchPage.selectDate(dateDeparture);

        step("Select pax amount");
        busSearchPage.selectPassenger(passenger);

        step("Verify search form value");
        Assertion.assertThat("is origin selected",busSearchPage.isDepartureSelected(originLocation),is(true));
        Assertion.assertThat("is destination selected",busSearchPage.isDestinationSelected(destinationLocation),is(true));
        Assertion.assertThat("is calendar selected",busSearchPage.isDateSelected(dateDeparture),is(true));
        Assertion.assertThat("is correct pax selected",busSearchPage.isPaxSelected(passenger),is(true));

        step("Click search bus");
        busSearchPage.clickSearchBus();

        step("Skip bus srp coachmark");
        busSearchResultPage.skipCoachMark();

        step("Select bus departure on srp");
        busSearchResultPage.selectFirstBus();

        step("select seat departure");
        buspdpPage.openSeat();
        buspdpPage.clickSelectSeatButton();

        step("Input guest contact detail");
        busBookingFormPage.inputGuestContactDetail(fullName,phoneNumber,emailAddress);
        busBookingFormPage.fillPassengerData(passenger);

        step("Get data to verify on payment page");
        BusBookingPageData busBookingPageData = new BusBookingPageData();
        busBookingPageData.seatDetail = busBookingFormPage.getSeatNoDetail();
        busBookingPageData.departDate = busBookingFormPage.getDepartureDate();
        busBookingPageData.totalPrice = busBookingFormPage.getTotalPrice();
        busBookingPageData.passengerName = busBookingFormPage.getPassengerName();
        busBookingPageData.busClass = busBookingFormPage.getBusClassName();
        busBookingPageData.operatorName = busBookingFormPage.getBusOperatorName();

        step("Continue to payment page");
        String changedPrice = busBookingFormPage.continueToPaymentPageAndGetPriceChange();
        busBookingPageData.totalPrice = changedPrice != null ? changedPrice : busBookingPageData.totalPrice;
        Assertion.assertThat("Verify user get orderId", !paymentPage.checkOrderID().isBlank(), is(true));
        paymentPage.verifyPaymentPageForBus(busBookingPageData);
    }

    @Author(names = {"kartik"})
    @Module(name = "CheckPaymentDetailOnPaymentListPage")
    @Tribe(name = "Bus")
    @Vertical(name = "GroundTransport")
    @Test
    public void testToCheckPaymentDetailOnPaymentListPageLogin(){
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> testProperties = getParam("testToCheckPaymentDetailOnPaymentListPageLogin");
        BaseTest.paramsThreadLocal.set(testProperties);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        BusSearchPage<WebDriver> busSearchPage = BusSearchPage.getInstance(session);
        BusSearchResultPage<WebDriver> busSearchResultPage = BusSearchResultPage.getInstance(session);
        BuspdpPage<WebDriver> buspdpPage = BuspdpPage.getInstance(session);
        BusBookingFormPage<WebDriver> busBookingFormPage = BusBookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);

        String loginMethod = testProperties.get("loginType");
        String username = testProperties.get("username");
        String password = testProperties.get("password");

        String tripType = testProperties.get("tripType");
        String originLocation = testProperties.get("originLocation");
        String destinationLocation = testProperties.get("destinationLocation");
        int daysToDeparture = Integer.parseInt(testProperties.get("daysToDeparture"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String dateDeparture = CoreFunctions.addToLongDate(currentDate,daysToDeparture);
        int passenger = Integer.parseInt(testProperties.get("passenger"));

        step("User open app");
        homePage.gotoHome();

        step("User login");
        loginPage.loginAsGuest();
        loginPage.loginWith(loginMethod,username,password);

        step("Get App Language");
        String language = homePage.getLanguageApp();

        step("Open Bus Landing Page");
        homePage.openVerticalPageDeeplink("Bus & Shuttle", language, "IDR");

        step("Skip coachmark");
        busSearchPage.skipCoachMark();

        step("Switch trip type to oneway");
        busSearchPage.switchTripType(tripType);

        step("Input origin location");
        busSearchPage.inputOriginLocation(originLocation);

        step("Input destination location");
        busSearchPage.inputDestinationLocation(destinationLocation);

        step("Select date");
        busSearchPage.selectDate(dateDeparture);

        step("Select pax amount");
        busSearchPage.selectPassenger(passenger);

        step("Verify search form value");
        Assertion.assertThat("is origin selected",busSearchPage.isDepartureSelected(originLocation),is(true));
        Assertion.assertThat("is destination selected",busSearchPage.isDestinationSelected(destinationLocation),is(true));
        Assertion.assertThat("is calendar selected",busSearchPage.isDateSelected(dateDeparture),is(true));
        Assertion.assertThat("is correct pax selected",busSearchPage.isPaxSelected(passenger),is(true));

        step("Click search bus");
        busSearchPage.clickSearchBus();

        step("Skip bus srp coachmark");
        busSearchResultPage.skipCoachMark();

        step("Select bus departure on srp");
        busSearchResultPage.selectFirstBus();

        step("select seat departure");
        buspdpPage.openSeat();
        buspdpPage.clickSelectSeatButton();

        step("Input guest contact detail");
        busBookingFormPage.fillPassengerData(passenger);

        step("Get data to verify on payment page");
        BusBookingPageData busBookingPageData = new BusBookingPageData();
        busBookingPageData.seatDetail = busBookingFormPage.getSeatNoDetail();
        busBookingPageData.departDate = busBookingFormPage.getDepartureDate();
        busBookingPageData.totalPrice = busBookingFormPage.getTotalPrice();
        busBookingPageData.passengerName = busBookingFormPage.getPassengerName();
        busBookingPageData.busClass = busBookingFormPage.getBusClassName();
        busBookingPageData.operatorName = busBookingFormPage.getBusOperatorName();

        step("Continue to payment page");
        String changedPrice = busBookingFormPage.continueToPaymentPageAndGetPriceChange();
        busBookingPageData.totalPrice = changedPrice != null ? changedPrice : busBookingPageData.totalPrice;
        Assertion.assertThat("Verify user get orderId", !paymentPage.checkOrderID().isBlank(), is(true));
        paymentPage.verifyPaymentPageForBus(busBookingPageData);
    }

    @Author(names = {"Dendi"})
    @Module(name = "CheckPriceDetailBookingForm")
    @Tribe(name = "Bus")
    @Vertical(name = "GroundTransport")
    @Test
    public void testBusPriceDetailBookingFormLogin(){
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> testProperties = getParam("testBusPriceDetailBookingFormLogin");
        BaseTest.paramsThreadLocal.set(testProperties);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        BusSearchPage<WebDriver> busSearchPage = BusSearchPage.getInstance(session);
        BusSearchResultPage<WebDriver> busSearchResultPage = BusSearchResultPage.getInstance(session);
        BuspdpPage<WebDriver> buspdpPage = BuspdpPage.getInstance(session);
        BusBookingFormPage<WebDriver> busBookingFormPage = BusBookingFormPage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);

        String loginMethod = testProperties.get("loginType");
        String username = testProperties.get("username");
        String password = testProperties.get("password");

        String tripType = testProperties.get("tripType");
        String originLocation = testProperties.get("originLocation");
        String destinationLocation = testProperties.get("destinationLocation");
        int daysToDeparture = Integer.parseInt(testProperties.get("daysToDeparture"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String dateDeparture = CoreFunctions.addToLongDate(currentDate,daysToDeparture);
        int passenger = Integer.parseInt(testProperties.get("passenger"));

        step("User open app");
        homePage.gotoHome();

        step("User login as guest");
        loginPage.loginWith(loginMethod,username,password);

        step("Get App Language");
        String language = homePage.getLanguageApp();

        step("Open Bus Landing Page");
        homePage.openVerticalPageDeeplink("Bus & Shuttle", language, "IDR");

        step("Skip coachmark");
        busSearchPage.skipCoachMark();

        step("Switch trip type to oneway");
        busSearchPage.switchTripType(tripType);

        step("Input origin location");
        busSearchPage.inputOriginLocation(originLocation);

        step("Input destination location");
        busSearchPage.inputDestinationLocation(destinationLocation);

        step("Select date");
        busSearchPage.selectDate(dateDeparture);

        step("Select pax amount");
        busSearchPage.selectPassenger(passenger);

        step("Click search bus");
        busSearchPage.clickSearchBus();

        step("Skip bus srp coachmark");
        busSearchResultPage.skipCoachMark();

        step("Select bus departure on srp");
        busSearchResultPage.selectFirstBus();

        step("select seat departure");
        buspdpPage.openSeat();
        buspdpPage.clickSelectSeatButton();

        step("Check price total in booking page");
        String priceTotalBookingPage = busBookingFormPage.getBookingPagePrice();

        step("User verify price detail booking page");
        busBookingFormPage.openPriceDetail();

        step("User verify price detail booking page");
        Assertion.assertThat("Verify price detail booking page is correct", busBookingFormPage.verifyBookingFormPriceDetail(priceTotalBookingPage), is(true));
    }

    @Author(names = {"Dendi"})
    @Module(name = "CheckPriceDetailBookingForm")
    @Tribe(name = "Bus")
    @Vertical(name = "GroundTransport")
    @Test
    public void testBusPriceDetailBookingFormGuest(){
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> testProperties = getParam("testBusPriceDetailBookingFormGuest");
        BaseTest.paramsThreadLocal.set(testProperties);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        BusSearchPage<WebDriver> busSearchPage = BusSearchPage.getInstance(session);
        BusSearchResultPage<WebDriver> busSearchResultPage = BusSearchResultPage.getInstance(session);
        BuspdpPage<WebDriver> buspdpPage = BuspdpPage.getInstance(session);
        BusBookingFormPage<WebDriver> busBookingFormPage = BusBookingFormPage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);

        String tripType = testProperties.get("tripType");
        String originLocation = testProperties.get("originLocation");
        String destinationLocation = testProperties.get("destinationLocation");
        int daysToDeparture = Integer.parseInt(testProperties.get("daysToDeparture"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String dateDeparture = CoreFunctions.addToLongDate(currentDate,daysToDeparture);
        int passenger = Integer.parseInt(testProperties.get("passenger"));

        step("User open app");
        homePage.gotoHome();

        step("User login as guest");
        loginPage.loginAsGuest();

        if(Configs.ENVIRONMENT == Environment.PROD) {
            step("Change currency to 'IDR'");
            homePage.changeCurrency("IDR","");
        }

        step("Get App Language");
        String language = homePage.getLanguageApp();

        step("Open Bus Landing Page");
        homePage.openVerticalPageDeeplink("Bus", language, "IDR");

        step("Skip coachmark");
        busSearchPage.skipCoachMark();

        step("Switch trip type to oneway");
        busSearchPage.switchTripType(tripType);

        step("Input origin location");
        busSearchPage.inputOriginLocation(originLocation);

        step("Input destination location");
        busSearchPage.inputDestinationLocation(destinationLocation);

        step("Select date");
        busSearchPage.selectDate(dateDeparture);

        step("Select pax amount");
        busSearchPage.selectPassenger(passenger);

        step("Click search bus");
        busSearchPage.clickSearchBus();

        step("Skip bus srp coachmark");
        busSearchResultPage.skipCoachMark();

        step("Select bus departure on srp");
        busSearchResultPage.selectFirstBus();

        step("select seat departure");
        buspdpPage.openSeat();
        buspdpPage.clickSelectSeatButton();

        step("Check price total in booking page");
        String priceTotalBookingPage = busBookingFormPage.getBookingPagePrice();

        step("User verify price detail booking page");
        busBookingFormPage.openPriceDetail();

        step("User verify price detail booking page");
        Assertion.assertThat("Verify price detail booking page is correct", busBookingFormPage.verifyBookingFormPriceDetail(priceTotalBookingPage), is(true));
    }

}
