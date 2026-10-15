package com.tiket.web.test.nft.whoosh;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Environment;
import com.tiket.model.Platform;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.testbase.Configs;
import com.tiket.web.WebTest;
import com.tiket.web.page.common.calendar.CalendarPageWeb;
import com.tiket.web.page.common.home.HomePageWeb;
import com.tiket.web.page.common.home.LoginPageWeb;
import com.tiket.web.page.nft.whoosh.booking_page.WhooshBookingPageWeb;
import com.tiket.web.page.nft.whoosh.searchResult_page.WhooshSearchResultPageWeb;
import com.tiket.web.page.nft.whoosh.search_page.WhooshSearchPageWeb;
import com.tiket.web.page.order.OrderDetail.OrderDetailWeb;
import com.tiket.web.page.order.OrderList.OrderListWeb;
import com.tiket.web.page.platform.payment.PaymentPageWeb;
import org.testng.annotations.Test;

import javax.security.auth.login.FailedLoginException;
import java.util.List;
import java.util.Map;

import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.core.Is.is;

public class WhooshTest extends WebTest {

    @Author(names = "Akshat")
    @Module(name = "CompleteBooking")
    @Tribe(name = "Whoosh")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyWhooshBookingGuestUser() throws InterruptedException {
        Map<String, String> param = getParam("testVerifyWhooshBookingGuestUser");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String originStation = param.get("originStation");
        String destinationStation = param.get("destinationStation");
        String tripType = param.get("tripType");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);
        int daysToReturnDate = Integer.parseInt(param.get("daysToReturnDate"));
        String returnDate = CoreFunctions.addToLongDate(departureDate, daysToReturnDate);
        int adultCount = Integer.parseInt(param.get("adultCount"));
        int infantCount = Integer.parseInt(param.get("infantCount"));
        String fullName = CoreFunctions.randomName();
        String phoneNumber = "91" + CoreFunctions.randomInt(8);
        String emailID = CoreFunctions.randomEmail();
        String paymentMethod = param.get("paymentMethod");
        String paymentGroup = param.get("paymentGroup");


        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        WhooshSearchPageWeb<?> whooshSearchPageWeb = WhooshSearchPageWeb.getInstance(session);
        WhooshSearchResultPageWeb<?> whooshSearchResultPageWeb = WhooshSearchResultPageWeb.getInstance(session);
        WhooshBookingPageWeb<?> whooshBookingPageWeb = WhooshBookingPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as guest");
        loginPage.loginAsGuest();

        step("User open whoosh vertical");
        homePage.openVerticalPageViaURL("whoosh");

        step("User skip pop-up");
        whooshSearchPageWeb.clickSkipPopup();

        step("User select origin station");
        whooshSearchPageWeb.selectOrigin(originStation);

        step("User select destination station");
        whooshSearchPageWeb.selectDestination(destinationStation);

        step("User toggle one way");
        whooshSearchPageWeb.toggleTripType(tripType);

        step("User selects departure and return date");
        whooshSearchPageWeb.selectDate(departureDate, returnDate);

        step("User select passenger");
        whooshSearchPageWeb.selectPassenger(adultCount, infantCount);

        step("User saves the passengers");
        whooshSearchPageWeb.savePassengers();

        step("User search whoosh");
        whooshSearchPageWeb.searchWhoosh();

        step("User verifies if he is on SRP or not");
        Assertion.assertThat("Verify if the user is on SRP or not", whooshSearchResultPageWeb.checkSRP(), is(true));

        step("User skips the filter option");
        whooshSearchResultPageWeb.clickSkip();

        step("User select first Whoosh");
        String departureWhoosh = whooshSearchResultPageWeb.clickFirstWhoosh();

        step("User verifies whether he can select return whoosh or not");
        Assertion.assertThat("Verifies whether user can select return whoosh or not", whooshSearchResultPageWeb.checkReturn(), is(true));

        step("User selects return Whoosh");
        String returnWhoosh = whooshSearchResultPageWeb.clickFirstWhoosh();

        step("User enter contact details for refunds");
        whooshBookingPageWeb.fillContactDetails(fullName, phoneNumber, emailID);

        step("User fill adult details");
        whooshBookingPageWeb.fillPassengerDetailAdult(adultCount);

        step("User fill infant details");
        whooshBookingPageWeb.fillPassengerDetailInfant(adultCount, infantCount);

        step("User continues booking after filling details");
        whooshBookingPageWeb.clickContinue();

        step("User changes and saves the seat");
        whooshBookingPageWeb.saveSeat();

        step("User verifies the departure whoosh");
        Assertion.assertThat("verifies departure whoosh", whooshBookingPageWeb.verifiesDepartureWhoosh(tripType), is(departureWhoosh));

        step("User verifies the return whoosh");
        Assertion.assertThat("verifies return whoosh", whooshBookingPageWeb.verifiesReturnWhoosh(), is(returnWhoosh));

        step("User verifies departure origin station");
        Assertion.assertThat("verifies departure origin station", whooshBookingPageWeb.verifyDepartureOrigin(tripType), is(originStation));

        step("User verifies departure destination station");
        Assertion.assertThat("verifies departure destination station", whooshBookingPageWeb.verifyDepartureDestination(tripType), is(destinationStation));

        step("User verifies return origin station");
        Assertion.assertThat("verifies return origin station", whooshBookingPageWeb.verifyReturnOrigin(), is(destinationStation));

        step("User verifies return destination station");
        Assertion.assertThat("verifies return destination station", whooshBookingPageWeb.verifyReturnDestination(), is(originStation));

        step("User adds insurance");
        whooshBookingPageWeb.addInsurance();

        step("User continues for payment");
        whooshBookingPageWeb.continueToPay();

        step("User Go to Payment Page");
        whooshBookingPageWeb.goToPaymentPage();
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

    @Author(names = {"Akshat", "Vipul Kumar"})
    @Module(name = "Whoosh")
    @Tribe(name = "NFT")
    @Vertical(name = "NFT")
    @Test
    public void Whoosh_testVerifyWhooshBookingAsLoggedInUser() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("Whoosh_testVerifyWhooshBookingAsLoggedInUser");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String currency = param.get("currency");
        String originStation = param.get("originStation");
        String destinationStation = param.get("destinationStation");
        String tripType = param.get("tripType");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);
        int daysToReturnDate = Integer.parseInt(param.get("daysToReturnDate"));
        String returnDate = CoreFunctions.addToLongDate(departureDate, daysToReturnDate);
        int adultCount = Integer.parseInt(param.get("adultCount"));
        int infantCount = Integer.parseInt(param.get("infantCount"));
        String paymentMethod = param.get("paymentMethod");
        String paymentGroup = param.get("paymentGroup");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        WhooshSearchPageWeb<?> whooshSearchPageWeb = WhooshSearchPageWeb.getInstance(session);
        WhooshSearchResultPageWeb<?> whooshSearchResultPageWeb = WhooshSearchResultPageWeb.getInstance(session);
        WhooshBookingPageWeb<?> whooshBookingPageWeb = WhooshBookingPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        CalendarPageWeb<?> calendarPage = CalendarPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("Login as " + email + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(email, password);

        step("User select IDR currency");
        homePage.selectCurrency(currency);

        step("User select English language");
        homePage.selectEnglishLanguage();

        step("User open whoosh vertical");
        homePage.openVerticalPageViaURL("Whoosh");

        step("User skip pop-up");
        whooshSearchPageWeb.clickSkipPopup();

        step("User select origin station");
        whooshSearchPageWeb.selectOrigin(originStation);

        step("User select destination station");
        whooshSearchPageWeb.selectDestination(destinationStation);

        step("User toggle one way");
        whooshSearchPageWeb.toggleTripType(tripType);

        step("User selects departure and return date");
        whooshSearchPageWeb.clickOnCalendarWidgetToOpen();
        calendarPage.selectDate(departureDate, returnDate);

        step("User select passenger");
        whooshSearchPageWeb.selectPassenger(adultCount, infantCount);

        step("User saves the passengers");
        whooshSearchPageWeb.savePassengers();

        step("User search whoosh");
        whooshSearchPageWeb.searchWhoosh();

        step("User verifies if he is on SRP or not");
        Assertion.assertThat("Verify if the user is on SRP or not", whooshSearchResultPageWeb.checkSRP(), is(true));

        step("User skips the filter option");
        whooshSearchResultPageWeb.clickSkip();

        step("User select first Whoosh");
        String departureWhoosh = whooshSearchResultPageWeb.clickFirstWhoosh();

        step("User verifies whether he can select return whoosh or not");
        Assertion.assertThat("Verifies whether user can select return whoosh or not", whooshSearchResultPageWeb.checkReturn(), is(true));

        step("User selects return Whoosh");
        String returnWhoosh = whooshSearchResultPageWeb.clickFirstWhoosh();

        step("User fill adult details");
        whooshBookingPageWeb.fillPassengerDetailAdult(adultCount);

        step("User fill infant details");
        whooshBookingPageWeb.fillPassengerDetailInfant(adultCount, infantCount);

        step("User continues booking after filling details");
        whooshBookingPageWeb.clickContinue();

        step("User changes and saves the seat");
        whooshBookingPageWeb.saveSeat();

        step("User verifies the departure whoosh");
        Assertion.assertThat("verifies departure whoosh", whooshBookingPageWeb.verifiesDepartureWhoosh(tripType), is(departureWhoosh));

        step("User verifies the return whoosh");
        Assertion.assertThat("verifies return whoosh", whooshBookingPageWeb.verifiesReturnWhoosh(), is(returnWhoosh));

        step("User verifies departure origin station");
        Assertion.assertThat("verifies departure origin station", whooshBookingPageWeb.verifyDepartureOrigin(tripType), is(originStation));

        step("User verifies departure destination station");
        Assertion.assertThat("verifies departure destination station", whooshBookingPageWeb.verifyDepartureDestination(tripType), containsString(destinationStation));

        step("User verifies return origin station");
        Assertion.assertThat("verifies return origin station", whooshBookingPageWeb.verifyReturnOrigin(), is(destinationStation));

        step("User verifies return destination station");
        Assertion.assertThat("verifies return destination station", whooshBookingPageWeb.verifyReturnDestination(), is(originStation));

        step("User adds insurance");
        whooshBookingPageWeb.addInsurance();

        step("User continues for payment");
        whooshBookingPageWeb.continueToPay();

        step("User Go to Payment Page");
        whooshBookingPageWeb.goToPaymentPage();
        String orderID = paymentPage.getOrderId();
        Assertion.assertThat("Verify order ID is displayed in payment page", !orderID.isEmpty(), is(true));

        if (Configs.ENVIRONMENT == Environment.GK || Configs.ENVIRONMENT == Environment.PREPROD) {
        step("User select payment method ");
        paymentPage.clickOnSelectPaymentMethod();
        paymentPage.goToPaymentMethodsGroup(paymentGroup);
        paymentPage.selectPaymentMethod(paymentMethod);

        step("User complete payment");
        paymentPage.clickOnPaymentButton();
        Assertion.assertThat("Payment successful page is visible", paymentPage.isPaymentSuccessfulVisible(), is(true));

        step("User try to open see order list");
        paymentPage.clickSeeOrderListButton();

        step("User open order detail page");
        orderListPage.openActiveTicketViaURL(orderID, "depart");
        orderDetailPage.refreshTheWebpage();
        orderDetailPage.skipCoachmark();

        step("User verifies e-ticket is displayed or not");
        Assertion.assertThat("Verifies whether view e-ticket is displayed or not", orderDetailPage.isEticketDisplayed(), is(true));

        step("User verifies departure whoosh details");
        orderDetailPage.checkDepartureTrainDetails(departureWhoosh, originStation, destinationStation);

        step("User verifies return whoosh details");
        orderDetailPage.checkReturnTrainDetails(returnWhoosh, originStation, destinationStation);

        Assertion.assertThat("Verify Manage order container", orderDetailPage.verifyManageOrderContainer(), is(true));
        }
    }

    @Author(names = {"Akshat", "Vipul Kumar"})
    @Module(name = "CompleteBooking")
    @Tribe(name = "Whoosh")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyCompleteWhooshBookingOneWayAsLoggedInUser() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testVerifyCompleteWhooshBookingOneWayAsLoggedInUser");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String currency = param.get("currency");
        String originStation = param.get("originStation");
        String destinationStation = param.get("destinationStation");
        String tripType = param.get("tripType");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);
        int adultCount = Integer.parseInt(param.get("adultCount"));
        int infantCount = Integer.parseInt(param.get("infantCount"));
        String paymentMethod = param.get("paymentMethod");
        String paymentGroup = param.get("paymentGroup");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        WhooshSearchPageWeb<?> whooshSearchPageWeb = WhooshSearchPageWeb.getInstance(session);
        WhooshSearchResultPageWeb<?> whooshSearchResultPageWeb = WhooshSearchResultPageWeb.getInstance(session);
        WhooshBookingPageWeb<?> whooshBookingPageWeb = WhooshBookingPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open home page as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User select IDR currency");
        homePage.selectCurrency(currency);

        step("User open whoosh vertical");
        homePage.openVerticalPageViaURL("whoosh");

        step("User skip pop-up");
        whooshSearchPageWeb.clickSkipPopup();

        step("User select origin station");
        whooshSearchPageWeb.selectOrigin(originStation);

        step("User select destination station");
        whooshSearchPageWeb.selectDestination(destinationStation);

        step("User toggle one way");
        whooshSearchPageWeb.toggleTripType(tripType);

        step("User selects departure date");
        whooshSearchPageWeb.searchDepDate(departureDate);

        step("User select passenger");
        whooshSearchPageWeb.selectPassenger(adultCount, infantCount);

        step("User saves the passengers");
        whooshSearchPageWeb.savePassengers();

        step("User search whoosh");
        whooshSearchPageWeb.searchWhoosh();

        step("User verifies if he is on SRP or not");
        Assertion.assertThat("Verify if the user is on SRP or not", whooshSearchResultPageWeb.checkSRP(), is(true));

        step("User skips the filter option");
        whooshSearchResultPageWeb.clickSkip();

        step("User select first Whoosh");
        String departureWhoosh = whooshSearchResultPageWeb.clickFirstWhoosh();

        step("User fill adult details");
        whooshBookingPageWeb.fillPassengerDetailAdult(adultCount);

        step("User fill infant details");
        whooshBookingPageWeb.fillPassengerDetailInfant(adultCount, infantCount);

        step("User continues booking after filling details");
        whooshBookingPageWeb.clickContinue();

        step("User verifies the departure whoosh");
        Assertion.assertThat("verifies departure whoosh", whooshBookingPageWeb.verifiesDepartureWhoosh(tripType), is(departureWhoosh));

        step("User verifies departure origin station");
        Assertion.assertThat("verifies departure origin station", whooshBookingPageWeb.verifyDepartureOrigin(tripType), is(originStation));

        step("User verifies departure destination station");
        Assertion.assertThat("verifies departure destination station", whooshBookingPageWeb.verifyDepartureDestination(tripType), is(destinationStation));

        step("User continues for payment");
        whooshBookingPageWeb.continueToPay();

        step("User Go to Payment Page");
        whooshBookingPageWeb.goToPaymentPage();
        String orderID = paymentPage.getOrderId();
        Assertion.assertThat("Verify order ID is displayed in payment page", !orderID.isEmpty(), is(true));

        step("User select payment method ");
        paymentPage.clickOnSelectPaymentMethod();
        paymentPage.goToPaymentMethodsGroup(paymentGroup);
        paymentPage.selectPaymentMethod(paymentMethod);

        step("User complete payment");
        paymentPage.clickOnPaymentButton();
        paymentPage.isPaymentSuccessfulVisible();

        step("User open order detail page");
        orderListPage.openActiveTicketViaURL(orderID);
        orderDetailPage.refreshTheWebpage();
        orderDetailPage.skipCoachmark();

        step("User verifies e-ticket is displayed or not");
        Assertion.assertThat("Verifies whether view e-ticket is displayed or not", orderDetailPage.isEticketDisplayed(), is(true));

        step("User verifies departure whoosh details");
        orderDetailPage.checkDepartureTrainDetails(departureWhoosh, originStation, destinationStation);

        Assertion.assertThat("Verify Manage order container", orderDetailPage.verifyManageOrderContainer(), is(true));
    }

    @Author(names = "Mridul Gupta")
    @Module(name = "onlineCheckIn")
    @Tribe(name = "Whoosh")
    @Vertical(name = "NFT")
    @Test
    public void testWhooshSeeQRCodeOnIssuedOrderLoginWeb() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testWhooshSeeQRCodeOnIssuedOrderLoginWeb");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String email = param.get("email");
        String password = param.get("password");

        String currency = param.get("currency");
        String originStation = param.get("originStation");
        String destinationStation = param.get("destinationStation");
        String tripType = param.get("tripType");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int adultCount = Integer.parseInt(param.get("adultCount"));
        int infantCount = Integer.parseInt(param.get("infantCount"));
        String paymentMethod = param.get("paymentMethod");
        String paymentGroup = param.get("paymentGroup");

        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        WhooshSearchPageWeb<?> whooshSearchPageWeb = WhooshSearchPageWeb.getInstance(session);
        WhooshSearchResultPageWeb<?> whooshSearchResultPageWeb = WhooshSearchResultPageWeb.getInstance(session);
        WhooshBookingPageWeb<?> whooshBookingPageWeb = WhooshBookingPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        CalendarPageWeb<?> calendarPage = CalendarPageWeb.getInstance(session);

        // === PRECONDITION: create an issued active Whoosh order for the login user ===

        step("User open home page");
        homePage.goToHomePage();

        step("User open home page as logged in user via cookie");
        loginPage.loginWithSpecificAccountViaCookie(email, password);

        step("User select " + currency + " currency");
        homePage.selectCurrency(currency);

        step("User open Whoosh landing page via deeplink");
        homePage.openVerticalPageViaURL("whoosh");

        step("User skip pop-up");
        whooshSearchPageWeb.clickSkipPopup();

        step("User select origin station: " + originStation);
        whooshSearchPageWeb.selectOrigin(originStation);

        step("User select destination station: " + destinationStation);
        whooshSearchPageWeb.selectDestination(destinationStation);

        step("User toggle " + tripType + " trip type");
        whooshSearchPageWeb.toggleTripType(tripType);

        step("User selects departure date (next " + daysToDepartureDate + " days)");
        whooshSearchPageWeb.clickOnCalendarWidgetToOpen();
        calendarPage.selectDate(departureDate);

        step("User select " + adultCount + " adult(s) and " + infantCount + " infant(s)");
        whooshSearchPageWeb.selectPassenger(adultCount, infantCount);

        step("User saves the passengers");
        whooshSearchPageWeb.savePassengers();

        step("User search whoosh");
        whooshSearchPageWeb.searchWhoosh();

        step("User is on SRP");
        Assertion.assertThat("Verify SRP is displayed", whooshSearchResultPageWeb.checkSRP(), is(true));

        step("User skips the filter option");
        whooshSearchResultPageWeb.clickSkip();

        step("User select first Whoosh");
        whooshSearchResultPageWeb.clickFirstWhoosh();

        step("User fill adult passenger details");
        whooshBookingPageWeb.fillPassengerDetailAdult(adultCount);

        step("User fill infant passenger details");
        whooshBookingPageWeb.fillPassengerDetailInfant(adultCount, infantCount);

        step("User continues booking after filling details");
        whooshBookingPageWeb.clickContinue();

        step("User continues for payment");
        whooshBookingPageWeb.continueToPay();

        step("User Go to Payment Page");
        whooshBookingPageWeb.goToPaymentPage();
        String orderID = paymentPage.getOrderId();
        Assertion.assertThat("Verify order ID is displayed in payment page", !orderID.isEmpty(), is(true));

        step("User select " + paymentMethod + " payment method");
        paymentPage.clickOnSelectPaymentMethod();
        paymentPage.goToPaymentMethodsGroup(paymentGroup);
        paymentPage.selectPaymentMethod(paymentMethod);

        step("User complete payment");
        paymentPage.clickOnPaymentButton();
        Assertion.assertThat("Payment successful page is visible", paymentPage.isPaymentSuccessfulVisible(), is(true));
        paymentPage.clickSeeOrderListButton();

        // === ACTUAL TEST: navigate to issued order detail and verify QR codes ===

        step("User open issued active Whoosh order detail page");
        orderListPage.openIssuedTicketViaURL(orderID);
        orderDetailPage.refreshTheWebpage();
        orderDetailPage.skipCoachmark();
        Assertion.assertThat("Verify e-ticket is displayed on issued order detail page", orderDetailPage.isEticketDisplayed(), is(true));

        step("User click on See QR code");
        orderDetailPage.clickSeeQRCodeIfPresent();

        step("Verify user is able to access online check-in page and see QR codes for adult passenger(s) — infants excluded");
        orderDetailPage.verifyQRCodesForPassengers(adultCount);
    }

    @Author(names = "Mridul Gupta")
    @Module(name = "CompleteBooking")
    @Tribe(name = "Whoosh")
    @Vertical(name = "NFT")
    @Test
    public void testCompleteWhooshBookingOneWayWithInsuranceForThreeAdultsLoginWeb() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testCompleteWhooshBookingOneWayWithInsuranceForThreeAdultsLoginWeb");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String email = param.get("email");
        String password = param.get("password");

        String currency = param.get("currency");
        String originStation = param.get("originStation");
        String destinationStation = param.get("destinationStation");
        String tripType = param.get("tripType");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int adultCount = Integer.parseInt(param.get("adultCount"));
        int infantCount = Integer.parseInt(param.get("infantCount"));

        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        WhooshSearchPageWeb<?> whooshSearchPageWeb = WhooshSearchPageWeb.getInstance(session);
        WhooshSearchResultPageWeb<?> whooshSearchResultPageWeb = WhooshSearchResultPageWeb.getInstance(session);
        WhooshBookingPageWeb<?> whooshBookingPageWeb = WhooshBookingPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);
        CalendarPageWeb<?> calendarPage = CalendarPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open home page as logged in user via cookie");
        loginPage.loginWithSpecificAccountViaCookie(email, password);

        step("User select " + currency + " currency");
        homePage.selectCurrency(currency);

        step("User go to Whoosh landing page via deeplink (round-trip toggle off by default)");
        homePage.openVerticalPageViaURL("whoosh");

        step("User skip pop-up");
        whooshSearchPageWeb.clickSkipPopup();

        step("User select origin station: " + originStation);
        whooshSearchPageWeb.selectOrigin(originStation);

        step("User select destination station: " + destinationStation);
        whooshSearchPageWeb.selectDestination(destinationStation);

        step("User select " + tripType + " trip type (round-trip toggle stays off)");
        whooshSearchPageWeb.toggleTripType(tripType);

        step("User set passenger count to " + adultCount + " adult(s)");
        whooshSearchPageWeb.selectPassenger(adultCount, infantCount);

        step("User saves the passengers");
        whooshSearchPageWeb.savePassengers();

        step("User select DoT (next " + daysToDepartureDate + " days)");
        whooshSearchPageWeb.clickOnCalendarWidgetToOpen();
        calendarPage.selectDate(departureDate);

        step("User tap search button");
        whooshSearchPageWeb.searchWhoosh();

        step("User is on SRP");
        Assertion.assertThat("Verify SRP is displayed", whooshSearchResultPageWeb.checkSRP(), is(true));

        step("User skips the filter option");
        whooshSearchResultPageWeb.clickSkip();

        step("User select available Whoosh schedule on SRP");
        String departureWhoosh = whooshSearchResultPageWeb.clickFirstWhoosh();

        step("User fill adult passenger details");
        whooshBookingPageWeb.fillPassengerDetailAdult(adultCount);

        step("User fill infant passenger details");
        whooshBookingPageWeb.fillPassengerDetailInfant(adultCount, infantCount);

        step("User continues booking after filling details");
        whooshBookingPageWeb.clickContinue();

        step("Verify selected departure Whoosh is reflected on booking form");
        Assertion.assertThat("Verify departure whoosh on booking form", whooshBookingPageWeb.verifiesDepartureWhoosh(tripType), is(departureWhoosh));
        Assertion.assertThat("Verify departure origin contains " + originStation,
                whooshBookingPageWeb.verifyDepartureOrigin(tripType), containsString(originStation));
        Assertion.assertThat("Verify departure destination contains " + destinationStation,
                whooshBookingPageWeb.verifyDepartureDestination(tripType), containsString(destinationStation));

        step("User apply insurance add-ons");
        whooshBookingPageWeb.addInsurance();

        step("User continue to payment page");
        whooshBookingPageWeb.continueToPay();
        whooshBookingPageWeb.goToPaymentPage();

        step("Verify user is redirected to payment page and orderId is generated");
        String orderID = paymentPage.getOrderId();
        Assertion.assertThat("Verify order ID is displayed in payment page", !orderID.isEmpty(), is(true));
    }

    @Author(names = "Mridul Gupta")
    @Module(name = "CompleteBooking")
    @Tribe(name = "Whoosh")
    @Vertical(name = "NFT")
    @Test
    public void testCompleteWhooshBookingOneWayWithAdultAndInfantLoginWeb() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testCompleteWhooshBookingOneWayWithAdultAndInfantLoginWeb");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String email = param.get("email");
        String password = param.get("password");

        String currency = param.get("currency");
        String originStation = param.get("originStation");
        String destinationStation = param.get("destinationStation");
        String tripType = param.get("tripType");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int adultCount = Integer.parseInt(param.get("adultCount"));
        int infantCount = Integer.parseInt(param.get("infantCount"));

        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        WhooshSearchPageWeb<?> whooshSearchPageWeb = WhooshSearchPageWeb.getInstance(session);
        WhooshSearchResultPageWeb<?> whooshSearchResultPageWeb = WhooshSearchResultPageWeb.getInstance(session);
        WhooshBookingPageWeb<?> whooshBookingPageWeb = WhooshBookingPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);
        CalendarPageWeb<?> calendarPage = CalendarPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open home page as logged in user via cookie");
        loginPage.loginWithSpecificAccountViaCookie(email, password);

        step("User select " + currency + " currency");
        homePage.selectCurrency(currency);

        step("User go to Whoosh landing page via deeplink (round-trip toggle off by default)");
        homePage.openVerticalPageViaURL("whoosh");

        step("User skip pop-up");
        whooshSearchPageWeb.clickSkipPopup();

        step("User select origin station: " + originStation);
        whooshSearchPageWeb.selectOrigin(originStation);

        step("User select destination station: " + destinationStation);
        whooshSearchPageWeb.selectDestination(destinationStation);

        step("User select " + tripType + " trip type (round-trip toggle stays off)");
        whooshSearchPageWeb.toggleTripType(tripType);

        step("User set passenger count to " + adultCount + " adult(s) and " + infantCount + " infant(s)");
        whooshSearchPageWeb.selectPassenger(adultCount, infantCount);

        step("User saves the passengers");
        whooshSearchPageWeb.savePassengers();

        step("User select DoT (next " + daysToDepartureDate + " days)");
        whooshSearchPageWeb.clickOnCalendarWidgetToOpen();
        calendarPage.selectDate(departureDate);

        step("User tap search button");
        whooshSearchPageWeb.searchWhoosh();

        step("User is on SRP");
        Assertion.assertThat("Verify SRP is displayed", whooshSearchResultPageWeb.checkSRP(), is(true));

        step("User skips the filter option");
        whooshSearchResultPageWeb.clickSkip();

        step("User select available Whoosh schedule on SRP");
        String departureWhoosh = whooshSearchResultPageWeb.clickFirstWhoosh();

        step("User fill adult passenger details");
        whooshBookingPageWeb.fillPassengerDetailAdult(adultCount);

        step("User fill infant passenger details");
        whooshBookingPageWeb.fillPassengerDetailInfant(adultCount, infantCount);

        step("User continues booking after filling details");
        whooshBookingPageWeb.clickContinue();

        step("Verify selected departure Whoosh is reflected on booking form");
        Assertion.assertThat("Verify departure whoosh on booking form", whooshBookingPageWeb.verifiesDepartureWhoosh(tripType), is(departureWhoosh));
        Assertion.assertThat("Verify departure origin contains " + originStation,
                whooshBookingPageWeb.verifyDepartureOrigin(tripType), containsString(originStation));
        Assertion.assertThat("Verify departure destination contains " + destinationStation,
                whooshBookingPageWeb.verifyDepartureDestination(tripType), containsString(destinationStation));

        step("User continue to payment page");
        whooshBookingPageWeb.continueToPay();
        whooshBookingPageWeb.goToPaymentPage();

        step("Verify user is redirected to payment page and orderId is generated");
        String orderID = paymentPage.getOrderId();
        Assertion.assertThat("Verify order ID is displayed in payment page", !orderID.isEmpty(), is(true));
    }

    @Author(names = "Mridul Gupta")
    @Module(name = "CompleteBooking")
    @Tribe(name = "Whoosh")
    @Vertical(name = "NFT")
    @Test
    public void testCompleteWhooshBookingOneWayForOneAdultLoginWeb() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testCompleteWhooshBookingOneWayForOneAdultLoginWeb");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String email = param.get("email");
        String password = param.get("password");

        String currency = param.get("currency");
        String originStation = param.get("originStation");
        String destinationStation = param.get("destinationStation");
        String tripType = param.get("tripType");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int adultCount = Integer.parseInt(param.get("adultCount"));
        int infantCount = Integer.parseInt(param.get("infantCount"));

        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        WhooshSearchPageWeb<?> whooshSearchPageWeb = WhooshSearchPageWeb.getInstance(session);
        WhooshSearchResultPageWeb<?> whooshSearchResultPageWeb = WhooshSearchResultPageWeb.getInstance(session);
        WhooshBookingPageWeb<?> whooshBookingPageWeb = WhooshBookingPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);
        CalendarPageWeb<?> calendarPage = CalendarPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open home page as logged in user via cookie");
        loginPage.loginWithSpecificAccountViaCookie(email, password);

        step("User select " + currency + " currency");
        homePage.selectCurrency(currency);

        step("User go to Whoosh landing page via deeplink (round-trip toggle off by default)");
        homePage.openVerticalPageViaURL("whoosh");

        step("User skip pop-up");
        whooshSearchPageWeb.clickSkipPopup();

        step("User select origin station: " + originStation);
        whooshSearchPageWeb.selectOrigin(originStation);

        step("User select destination station: " + destinationStation);
        whooshSearchPageWeb.selectDestination(destinationStation);

        step("User select " + tripType + " trip type (round-trip toggle stays off)");
        whooshSearchPageWeb.toggleTripType(tripType);

        step("User set passenger count to " + adultCount + " adult(s)");
        whooshSearchPageWeb.selectPassenger(adultCount, infantCount);

        step("User saves the passengers");
        whooshSearchPageWeb.savePassengers();

        step("User select DoT (next " + daysToDepartureDate + " days)");
        whooshSearchPageWeb.clickOnCalendarWidgetToOpen();
        calendarPage.selectDate(departureDate);

        step("User tap search button");
        whooshSearchPageWeb.searchWhoosh();

        step("User is on SRP");
        Assertion.assertThat("Verify SRP is displayed", whooshSearchResultPageWeb.checkSRP(), is(true));

        step("User skips the filter option");
        whooshSearchResultPageWeb.clickSkip();

        step("User select available Whoosh schedule on SRP");
        String departureWhoosh = whooshSearchResultPageWeb.clickFirstWhoosh();

        step("User fill adult passenger details");
        whooshBookingPageWeb.fillPassengerDetailAdult(adultCount);

        step("User fill infant passenger details");
        whooshBookingPageWeb.fillPassengerDetailInfant(adultCount, infantCount);

        step("User continues booking after filling details");
        whooshBookingPageWeb.clickContinue();

        step("Verify selected departure Whoosh is reflected on booking form");
        Assertion.assertThat("Verify departure whoosh on booking form", whooshBookingPageWeb.verifiesDepartureWhoosh(tripType), is(departureWhoosh));
        Assertion.assertThat("Verify departure origin contains " + originStation,
                whooshBookingPageWeb.verifyDepartureOrigin(tripType), containsString(originStation));
        Assertion.assertThat("Verify departure destination contains " + destinationStation,
                whooshBookingPageWeb.verifyDepartureDestination(tripType), containsString(destinationStation));

        step("User continue to payment page");
        whooshBookingPageWeb.continueToPay();
        whooshBookingPageWeb.goToPaymentPage();

        step("Verify user is redirected to payment page and orderId is generated");
        String orderID = paymentPage.getOrderId();
        Assertion.assertThat("Verify order ID is displayed in payment page", !orderID.isEmpty(), is(true));
    }

    @Author(names = {"Akshat", "Vipul Kumar"})
    @Module(name = "CompleteBooking")
    @Tribe(name = "Whoosh")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyCompleteWhooshBookingWithInsuranceOneWayAsLoggedInUser() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testVerifyCompleteWhooshBookingWithInsuranceOneWayAsLoggedInUser");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String currency = param.get("currency");
        String originStation = param.get("originStation");
        String destinationStation = param.get("destinationStation");
        String tripType = param.get("tripType");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);
        int adultCount = Integer.parseInt(param.get("adultCount"));
        int infantCount = Integer.parseInt(param.get("infantCount"));
        String paymentMethod = param.get("paymentMethod");
        String paymentGroup = param.get("paymentGroup");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        WhooshSearchPageWeb<?> whooshSearchPageWeb = WhooshSearchPageWeb.getInstance(session);
        WhooshSearchResultPageWeb<?> whooshSearchResultPageWeb = WhooshSearchResultPageWeb.getInstance(session);
        WhooshBookingPageWeb<?> whooshBookingPageWeb = WhooshBookingPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open home page as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User select IDR currency");
        homePage.selectCurrency(currency);

        step("User open whoosh vertical");
        homePage.openVerticalPageViaURL("whoosh");

        step("User skip pop-up");
        whooshSearchPageWeb.clickSkipPopup();

        step("User selects whoosh");
        whooshSearchPageWeb.selectWhoosh();

        step("User select origin station");
        whooshSearchPageWeb.selectOrigin(originStation);

        step("User select destination station");
        whooshSearchPageWeb.selectDestination(destinationStation);

        step("User toggle one way");
        whooshSearchPageWeb.toggleTripType(tripType);

        step("User selects departure date");
        whooshSearchPageWeb.searchDepDate(departureDate);

        step("User select passenger");
        whooshSearchPageWeb.selectPassenger(adultCount, infantCount);

        step("User saves the passengers");
        whooshSearchPageWeb.savePassengers();

        step("User search whoosh");
        whooshSearchPageWeb.searchWhoosh();

        step("User verifies if he is on SRP or not");
        Assertion.assertThat("Verify if the user is on SRP or not", whooshSearchResultPageWeb.checkSRP(), is(true));

        step("User skips the filter option");
        whooshSearchResultPageWeb.clickSkip();

        step("User select first Whoosh");
        String departureWhoosh = whooshSearchResultPageWeb.clickFirstWhoosh();

        step("User fill adult details");
        whooshBookingPageWeb.fillPassengerDetailAdult(adultCount);

        step("User fill infant details");
        whooshBookingPageWeb.fillPassengerDetailInfant(adultCount, infantCount);

        step("User continues booking after filling details");
        whooshBookingPageWeb.clickContinue();

        step("User verifies the departure whoosh");
        Assertion.assertThat("verifies departure whoosh", whooshBookingPageWeb.verifiesDepartureWhoosh(tripType), is(departureWhoosh));

        step("User verifies departure origin station");
        Assertion.assertThat("verifies departure origin station", whooshBookingPageWeb.verifyDepartureOrigin(tripType), is(originStation));

        step("User verifies departure destination station");
        Assertion.assertThat("verifies departure destination station", whooshBookingPageWeb.verifyDepartureDestination(tripType), is(destinationStation));

        step("User adds insurance");
        whooshBookingPageWeb.addInsurance();

        step("User continues for payment");
        whooshBookingPageWeb.continueToPay();

        step("User Go to Payment Page");
        whooshBookingPageWeb.goToPaymentPage();
        String orderID = paymentPage.getOrderId();
        Assertion.assertThat("Verify order ID is displayed in payment page", !orderID.isEmpty(), is(true));

        step("User select payment method ");
        paymentPage.clickOnSelectPaymentMethod();
        paymentPage.goToPaymentMethodsGroup(paymentGroup);
        paymentPage.selectPaymentMethod(paymentMethod);

        step("User complete payment");
        paymentPage.clickOnPaymentButton();
        paymentPage.isPaymentSuccessfulVisible();

        step("User open order detail page");
        orderListPage.openActiveTicketViaURL(orderID);
        orderDetailPage.refreshTheWebpage();
        orderDetailPage.skipCoachmark();

        step("User verifies e-ticket is displayed or not");
        Assertion.assertThat("Verifies whether view e-ticket is displayed or not", orderDetailPage.isEticketDisplayed(), is(true));

        step("User verifies departure whoosh details");
        orderDetailPage.checkDepartureTrainDetails(departureWhoosh, originStation, destinationStation);

        Assertion.assertThat("Verify Manage order container", orderDetailPage.verifyManageOrderContainer(), is(true));
    }

    @Author(names = {"Akshat", "Vipul Kumar"})
    @Module(name = "SRP")
    @Tribe(name = "Whoosh")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyRecentlySearchedAsGuestUser() {
        Map<String, String> param = getParam("testVerifyRecentlySearchedAsGuestUser");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String currency = param.get("currency");
        String originStation = param.get("originStation");
        String destinationStation = param.get("destinationStation");
        String tripType = param.get("tripType");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);
        int adultCount = Integer.parseInt(param.get("adultCount"));
        int infantCount = Integer.parseInt(param.get("infantCount"));

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        WhooshSearchPageWeb<?> whooshSearchPageWeb = WhooshSearchPageWeb.getInstance(session);
        WhooshSearchResultPageWeb<?> whooshSearchResultPageWeb = WhooshSearchResultPageWeb.getInstance(session);
        CalendarPageWeb<?> calendarPage = CalendarPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as guest");
        loginPage.loginAsGuest();

        step("User select IDR currency");
        homePage.selectCurrency(currency);

        step("User open whoosh vertical");
        homePage.openVerticalPageViaURL("Whoosh");

        step("User skip pop-up");
        whooshSearchPageWeb.clickSkipPopup();

        step("Clear recently searched if present");
        whooshSearchPageWeb.clearRecentlySearch();

        step("User select origin station");
        whooshSearchPageWeb.selectOrigin(originStation);

        step("User select destination station");
        whooshSearchPageWeb.selectDestination(destinationStation);

        step("User toggle one way");
        whooshSearchPageWeb.toggleTripType(tripType);

        step("User selects departure date");
        whooshSearchPageWeb.clickOnCalendarWidgetToOpen();
        calendarPage.selectDate(departureDate);

        step("User select passenger");
        whooshSearchPageWeb.selectPassenger(adultCount, infantCount);

        step("User saves the passengers");
        whooshSearchPageWeb.savePassengers();

        step("User search whoosh");
        whooshSearchPageWeb.searchWhoosh();

        step("User verifies if he is on SRP or not");
        Assertion.assertThat("Verify if the user is on SRP or not", whooshSearchResultPageWeb.checkSRP(), is(true));

        step("User skips the filter option");
        whooshSearchResultPageWeb.clickSkip();

        step("User gets back to Search page");
        whooshSearchResultPageWeb.getBack();

        step("User verifies whether he is on search page or not");
        Assertion.assertThat("Verifies if the user is on search page or not", whooshSearchPageWeb.verifiesSearchPage(), is(true));

        step("User checks if there is recently search box appears or not");
        Assertion.assertThat("Verifies whether recently search box appears or not", whooshSearchPageWeb.verifiesRecentlySearch(), is(true));

        String recentlySearchedText = whooshSearchPageWeb.getRecentlySearchedText();
        Assertion.assertThat("Verify origin station is present in recently searched box", recentlySearchedText.toLowerCase(), containsString(originStation.toLowerCase()));
        Assertion.assertThat("Verify destination station is present in recently searched box", recentlySearchedText.toLowerCase(), containsString(destinationStation.toLowerCase()));

        step("User clicks on that recently searched box");
        whooshSearchPageWeb.clickRecentlySearchBox();

        step("User verifies whether he is on SRP or not");
        Assertion.assertThat("verifies whether user is on SRP or not", whooshSearchResultPageWeb.checkSRP(), is(true));
    }

    @Author(names = {"Akshat", "Vipul Kumar"})
    @Module(name = "SRP")
    @Tribe(name = "Whoosh")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyRecentlySearchedAsLoggedInUser() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testVerifyRecentlySearchedAsLoggedInUser");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String currency = param.get("currency");
        String originStation = param.get("originStation");
        String destinationStation = param.get("destinationStation");
        String tripType = param.get("tripType");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);
        int adultCount = Integer.parseInt(param.get("adultCount"));
        int infantCount = Integer.parseInt(param.get("infantCount"));

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        WhooshSearchPageWeb<?> whooshSearchPageWeb = WhooshSearchPageWeb.getInstance(session);
        WhooshSearchResultPageWeb<?> whooshSearchResultPageWeb = WhooshSearchResultPageWeb.getInstance(session);
        CalendarPageWeb<?> calendarPage = CalendarPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open home page as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User select IDR currency");
        homePage.selectCurrency(currency);

        step("User open whoosh vertical");
        homePage.openVerticalPageViaURL("Whoosh");

        step("User skip pop-up");
        whooshSearchPageWeb.clickSkipPopup();

        step("Clear recently searched if present");
        whooshSearchPageWeb.clearRecentlySearch();

        step("User select origin station");
        whooshSearchPageWeb.selectOrigin(originStation);

        step("User select destination station");
        whooshSearchPageWeb.selectDestination(destinationStation);

        step("User toggle one way");
        whooshSearchPageWeb.toggleTripType(tripType);

        step("User selects departure date");
        whooshSearchPageWeb.clickOnCalendarWidgetToOpen();
        calendarPage.selectDate(departureDate);

        step("User select passenger");
        whooshSearchPageWeb.selectPassenger(adultCount, infantCount);

        step("User saves the passengers");
        whooshSearchPageWeb.savePassengers();

        step("User search whoosh");
        whooshSearchPageWeb.searchWhoosh();

        step("User verifies if he is on SRP or not");
        Assertion.assertThat("Verify if the user is on SRP or not", whooshSearchResultPageWeb.checkSRP(), is(true));

        step("User skips the filter option");
        whooshSearchResultPageWeb.clickSkip();

        step("User gets back to Search page");
        whooshSearchResultPageWeb.getBack();

        step("User verifies whether he is on search page or not");
        Assertion.assertThat("Verifies if the user is on search page or not", whooshSearchPageWeb.verifiesSearchPage(), is(true));

        step("User checks if there is recently search box appears or not");
        Assertion.assertThat("Verifies whether recently search box appears or not", whooshSearchPageWeb.verifiesRecentlySearch(), is(true));

        String recentlySearchedText = whooshSearchPageWeb.getRecentlySearchedText();
        Assertion.assertThat("Verify origin station is present in recently searched box", recentlySearchedText.toLowerCase(), containsString(originStation.toLowerCase()));
        Assertion.assertThat("Verify destination station is present in recently searched box", recentlySearchedText.toLowerCase(), containsString(destinationStation.toLowerCase()));

        step("User clicks on that recently searched box");
        whooshSearchPageWeb.clickRecentlySearchBox();

        step("User verifies whether he is on SRP or not");
        Assertion.assertThat("verifies whether user is on SRP or not", whooshSearchResultPageWeb.checkSRP(), is(true));
    }

    @Author(names = {"Akshat", "Vipul Kumar"})
    @Module(name = "SRP")
    @Tribe(name = "Whoosh")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyClassFilterAsGuestUser() {
        Map<String, String> param = getParam("testVerifyClassFilterAsGuestUser");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String currency = param.get("currency");
        String originStation = param.get("originStation");
        String destinationStation = param.get("destinationStation");
        String tripType = param.get("tripType");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);
        int adultCount = Integer.parseInt(param.get("adultCount"));
        int infantCount = Integer.parseInt(param.get("infantCount"));

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        WhooshSearchPageWeb<?> whooshSearchPageWeb = WhooshSearchPageWeb.getInstance(session);
        WhooshSearchResultPageWeb<?> whooshSearchResultPageWeb = WhooshSearchResultPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as guest");
        loginPage.loginAsGuest();

        step("User select IDR currency");
        homePage.selectCurrency(currency);

        step("User open whoosh vertical");
        homePage.openVerticalPageViaURL("Whoosh");

        step("User skip pop-up");
        whooshSearchPageWeb.clickSkipPopup();

        step("User select origin station");
        whooshSearchPageWeb.selectOrigin(originStation);

        step("User select destination station");
        whooshSearchPageWeb.selectDestination(destinationStation);

        step("User select one way");
        whooshSearchPageWeb.toggleTripType(tripType);

        step("User selects departure date");
        whooshSearchPageWeb.searchDepDate(departureDate);

        step("User select passenger");
        whooshSearchPageWeb.selectPassenger(adultCount, infantCount);

        step("User saves the passengers");
        whooshSearchPageWeb.savePassengers();

        step("User search whoosh");
        whooshSearchPageWeb.searchWhoosh();

        step("User verifies if he is on SRP or not");
        Assertion.assertThat("Verify if the user is on SRP or not", whooshSearchResultPageWeb.checkSRP(), is(true));

        step("User skips the filter option");
        whooshSearchResultPageWeb.clickSkip();

        step("User clicks the filter button");
        whooshSearchResultPageWeb.clickFilterBtn();

        step("User clicks the class filter and select the first appearing class option");
        String classType = whooshSearchResultPageWeb.clickFirstClassType();

        step("User saves the filter");
        whooshSearchResultPageWeb.clickSave();

        sleep(5);
        // Introducing a delay to ensure the filter is fully applied before content selection.
        // Without this sleep, selection occurs before the filter takes effect, leading to incorrect results.

        step("User verifies whether the filter applied or not");
        Assertion.assertThat("Verifies whether filter applied or not", whooshSearchResultPageWeb.checkFilter(), is(classType));
    }

    @Author(names = {"Akshat", "Vipul Kumar"})
    @Module(name = "SRP")
    @Tribe(name = "Whoosh")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyClassFilterAsLoggedInUser() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testVerifyClassFilterAsLoggedInUser");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String currency = param.get("currency");
        String originStation = param.get("originStation");
        String destinationStation = param.get("destinationStation");
        String tripType = param.get("tripType");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);
        int adultCount = Integer.parseInt(param.get("adultCount"));
        int infantCount = Integer.parseInt(param.get("infantCount"));

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        WhooshSearchPageWeb<?> whooshSearchPageWeb = WhooshSearchPageWeb.getInstance(session);
        WhooshSearchResultPageWeb<?> whooshSearchResultPageWeb = WhooshSearchResultPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open home page as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User select IDR currency");
        homePage.selectCurrency(currency);

        step("User open whoosh vertical");
        homePage.openVerticalPageViaURL("Whoosh");

        step("User skip pop-up");
        whooshSearchPageWeb.clickSkipPopup();

        step("User select origin station");
        whooshSearchPageWeb.selectOrigin(originStation);

        step("User select destination station");
        whooshSearchPageWeb.selectDestination(destinationStation);

        step("User select one way");
        whooshSearchPageWeb.toggleTripType(tripType);

        step("User selects departure date");
        whooshSearchPageWeb.searchDepDate(departureDate);

        step("User select passenger");
        whooshSearchPageWeb.selectPassenger(adultCount, infantCount);

        step("User saves the passengers");
        whooshSearchPageWeb.savePassengers();

        step("User search whoosh");
        whooshSearchPageWeb.searchWhoosh();

        step("User verifies if he is on SRP or not");
        Assertion.assertThat("Verify if the user is on SRP or not", whooshSearchResultPageWeb.checkSRP(), is(true));

        step("User skips the filter option");
        whooshSearchResultPageWeb.clickSkip();

        step("User clicks the filter button");
        whooshSearchResultPageWeb.clickFilterBtn();

        step("User clicks the class filter and select the first appearing class option");
        String classType = whooshSearchResultPageWeb.clickFirstClassType();

        step("User saves the filter");
        whooshSearchResultPageWeb.clickSave();

        sleep(5);
        // Introducing a delay to ensure the filter is fully applied before content selection.
        // Without this sleep, selection occurs before the filter takes effect, leading to incorrect results.

        step("User verifies whether the filter applied or not");
        Assertion.assertThat("Verifies whether filter applied or not", whooshSearchResultPageWeb.checkFilter(), is(classType));
    }

    @Author(names = {"Akshat", "Vipul Kumar"})
    @Module(name = "SRP")
    @Tribe(name = "Whoosh")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyWhooshNameFilterAsLoggedInUser() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testVerifyWhooshNameFilterAsLoggedInUser");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String currency = param.get("currency");
        String originStation = param.get("originStation");
        String destinationStation = param.get("destinationStation");
        String tripType = param.get("tripType");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);
        int adultCount = Integer.parseInt(param.get("adultCount"));
        int infantCount = Integer.parseInt(param.get("infantCount"));

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        WhooshSearchPageWeb<?> whooshSearchPageWeb = WhooshSearchPageWeb.getInstance(session);
        WhooshSearchResultPageWeb<?> whooshSearchResultPageWeb = WhooshSearchResultPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open home page as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User select IDR currency");
        homePage.selectCurrency(currency);

        step("User open whoosh vertical");
        homePage.openVerticalPageViaURL("Whoosh");

        step("User skip pop-up");
        whooshSearchPageWeb.clickSkipPopup();

        step("User select origin station");
        whooshSearchPageWeb.selectOrigin(originStation);

        step("User select destination station");
        whooshSearchPageWeb.selectDestination(destinationStation);

        step("User select one way");
        whooshSearchPageWeb.toggleTripType(tripType);

        step("User selects departure date");
        whooshSearchPageWeb.searchDepDate(departureDate);

        step("User select passenger");
        whooshSearchPageWeb.selectPassenger(adultCount, infantCount);

        step("User saves the passengers");
        whooshSearchPageWeb.savePassengers();

        step("User search whoosh");
        whooshSearchPageWeb.searchWhoosh();

        step("User verifies if he is on SRP or not");
        Assertion.assertThat("Verify if the user is on SRP or not", whooshSearchResultPageWeb.checkSRP(), is(true));

        step("User skips the filter option");
        whooshSearchResultPageWeb.clickSkip();

        step("User clicks the filter button");
        whooshSearchResultPageWeb.clickFilterBtn();

        step("User clicks the train name filter and select the first appearing class option");
        whooshSearchResultPageWeb.clickTrainNameFilter();

        step("User saves the filter");
        whooshSearchResultPageWeb.clickSave();

        sleep(5);
        // Introducing a delay to ensure the filter is fully applied before content selection.
        // Without this sleep, selection occurs before the filter takes effect, leading to incorrect results.

        step("User verifies whether the filter applied or not");
        Assertion.assertThat("Verifies whether filter applied or not", whooshSearchResultPageWeb.checkFilteredWhooshName(), containsString("Whoosh"));
    }

    @Author(names = {"Akshat", "Vipul Kumar"})
    @Module(name = "SRP")
    @Tribe(name = "Whoosh")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyWhooshNameFilterAsGuestUser() {
        Map<String, String> param = getParam("testVerifyWhooshNameFilterAsGuestUser");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String currency = param.get("currency");
        String originStation = param.get("originStation");
        String destinationStation = param.get("destinationStation");
        String tripType = param.get("tripType");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);
        int adultCount = Integer.parseInt(param.get("adultCount"));
        int infantCount = Integer.parseInt(param.get("infantCount"));

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        WhooshSearchPageWeb<?> whooshSearchPageWeb = WhooshSearchPageWeb.getInstance(session);
        WhooshSearchResultPageWeb<?> whooshSearchResultPageWeb = WhooshSearchResultPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as guest");
        loginPage.loginAsGuest();

        step("User select IDR currency");
        homePage.selectCurrency(currency);

        step("User open whoosh vertical");
        homePage.openVerticalPageViaURL("Whoosh");

        step("User skip pop-up");
        whooshSearchPageWeb.clickSkipPopup();

        step("User select origin station");
        whooshSearchPageWeb.selectOrigin(originStation);

        step("User select destination station");
        whooshSearchPageWeb.selectDestination(destinationStation);

        step("User select one way");
        whooshSearchPageWeb.toggleTripType(tripType);

        step("User selects departure date");
        whooshSearchPageWeb.searchDepDate(departureDate);

        step("User select passenger");
        whooshSearchPageWeb.selectPassenger(adultCount, infantCount);

        step("User saves the passengers");
        whooshSearchPageWeb.savePassengers();

        step("User search whoosh");
        whooshSearchPageWeb.searchWhoosh();

        step("User verifies if he is on SRP or not");
        Assertion.assertThat("Verify if the user is on SRP or not", whooshSearchResultPageWeb.checkSRP(), is(true));

        step("User skips the filter option");
        whooshSearchResultPageWeb.clickSkip();

        step("User clicks the filter button");
        whooshSearchResultPageWeb.clickFilterBtn();

        step("User clicks the train name filter and select the first appearing class option");
        whooshSearchResultPageWeb.clickTrainNameFilter();

        step("User saves the filter");
        whooshSearchResultPageWeb.clickSave();

        sleep(5);
        /* Introducing a delay to ensure the filter is fully applied before content selection.
         Without this sleep, selection occurs before the filter takes effect, leading to incorrect results. */

        step("User verifies whether the filter applied or not");
        Assertion.assertThat("Verifies whether filter applied or not ", whooshSearchResultPageWeb.checkFilteredWhooshName(), containsString("Whoosh"));
    }

    @Author(names = {"Akshat", "Vipul Kumar"})
    @Module(name = "BookingPage")
    @Tribe(name = "Whoosh")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyPriceDetailsGuestUser() throws InterruptedException {
        Map<String, String> param = getParam("testVerifyPriceDetailsGuestUser");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String originStation = param.get("originStation");
        String destinationStation = param.get("destinationStation");
        String tripType = param.get("tripType");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);
        int adultCount = Integer.parseInt(param.get("adultCount"));
        int infantCount = Integer.parseInt(param.get("infantCount"));
        String fullName = CoreFunctions.randomName();
        String phoneNumber = "91" + CoreFunctions.randomInt(8);
        String emailID = CoreFunctions.randomEmail();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        WhooshSearchPageWeb<?> whooshSearchPageWeb = WhooshSearchPageWeb.getInstance(session);
        WhooshSearchResultPageWeb<?> whooshSearchResultPageWeb = WhooshSearchResultPageWeb.getInstance(session);
        WhooshBookingPageWeb<?> whooshBookingPageWeb = WhooshBookingPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as guest");
        loginPage.loginAsGuest();

        step("User selects English language");
        homePage.selectEnglishLanguageOnly();

        step("User open whoosh vertical");
        homePage.openVerticalPageViaURL("whoosh");

        step("User skip pop-up");
        whooshSearchPageWeb.clickSkipPopup();

        step("User select origin station");
        whooshSearchPageWeb.selectOrigin(originStation);

        step("User select destination station");
        whooshSearchPageWeb.selectDestination(destinationStation);

        step("User selects one way");
        whooshSearchPageWeb.toggleTripType(tripType);

        step("User selects departure date");
        whooshSearchPageWeb.searchDepDate(departureDate);

        step("User select passenger");
        whooshSearchPageWeb.selectPassenger(adultCount, infantCount);

        step("User saves the passengers");
        whooshSearchPageWeb.savePassengers();

        step("User search whoosh");
        whooshSearchPageWeb.searchWhoosh();

        step("User verifies if he is on SRP or not");
        Assertion.assertThat("Verify if the user is on SRP or not", whooshSearchResultPageWeb.checkSRP(), is(true));

        step("User skips the filter option");
        whooshSearchResultPageWeb.clickSkip();

        step("User gets the price of first whoosh");
        String price = whooshSearchResultPageWeb.getPrice();

        step("User select first Whoosh");
        whooshSearchResultPageWeb.clickFirstWhoosh();

        step("User enter contact details for refunds");
        whooshBookingPageWeb.fillContactDetails(fullName, phoneNumber, emailID);

        step("User fill adult details");
        whooshBookingPageWeb.fillPassengerDetailAdult(adultCount);

        step("User fill infant details");
        whooshBookingPageWeb.fillPassengerDetailInfant(adultCount, infantCount);

        if (Configs.PLATFORM.equals(Platform.DWEB)) {
            step("User clicks Total price details button");
            whooshBookingPageWeb.checkPriceBtn();

            step("User verifies whether price details page opens or not");
            Assertion.assertThat("Verifies whether price details page appears or not", whooshBookingPageWeb.isPriceDetailsDisplayed(), is(true));

            step("User verifies the price details on booking page");
            Assertion.assertThat("Verifies the price on booking page", whooshBookingPageWeb.checkPrice(adultCount), is(price));
        } else {
            step("User continues booking after filling details");
            whooshBookingPageWeb.clickContinue();
            whooshBookingPageWeb.ClickAnimatedChevronIcon_icon_chevron();

            step("User verifies the price details on booking page");
            Assertion.assertThat("Verifies the price on booking page", whooshBookingPageWeb.checkPrice(adultCount), is(price));
        }
    }

    @Author(names = {"Akshat", "Vipul Kumar"})
    @Module(name = "BookingPage")
    @Tribe(name = "Whoosh")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyPriceDetailsAsLoggedInUser() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testVerifyPriceDetailsAsLoggedInUser");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String currency = param.get("currency");
        String originStation = param.get("originStation");
        String destinationStation = param.get("destinationStation");
        String tripType = param.get("tripType");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);
        int adultCount = Integer.parseInt(param.get("adultCount"));
        int infantCount = Integer.parseInt(param.get("infantCount"));


        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        WhooshSearchPageWeb<?> whooshSearchPageWeb = WhooshSearchPageWeb.getInstance(session);
        WhooshSearchResultPageWeb<?> whooshSearchResultPageWeb = WhooshSearchResultPageWeb.getInstance(session);
        WhooshBookingPageWeb<?> whooshBookingPageWeb = WhooshBookingPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open home page as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User select IDR currency");
        homePage.selectCurrency(currency);

        step("User open whoosh vertical");
        homePage.openVerticalPageViaURL("Whoosh");

        step("User skip pop-up");
        whooshSearchPageWeb.clickSkipPopup();

        step("User select origin station");
        whooshSearchPageWeb.selectOrigin(originStation);

        step("User select destination station");
        whooshSearchPageWeb.selectDestination(destinationStation);

        step("User selects one way");
        whooshSearchPageWeb.toggleTripType(tripType);

        step("User selects departure and return date");
        whooshSearchPageWeb.searchDepDate(departureDate);

        step("User select passenger");
        whooshSearchPageWeb.selectPassenger(adultCount, infantCount);

        step("User saves the passengers");
        whooshSearchPageWeb.savePassengers();

        step("User search whoosh");
        whooshSearchPageWeb.searchWhoosh();

        step("User verifies if he is on SRP or not");
        Assertion.assertThat("Verify if the user is on SRP or not", whooshSearchResultPageWeb.checkSRP(), is(true));

        step("User skips the filter option");
        whooshSearchResultPageWeb.clickSkip();

        step("User gets the price of first whoosh");
        String price = whooshSearchResultPageWeb.getPrice();

        step("User select first Whoosh");
        whooshSearchResultPageWeb.clickFirstWhoosh();

        step("User fill adult details");
        whooshBookingPageWeb.fillPassengerDetailAdult(adultCount);

        step("User fill infant details");
        whooshBookingPageWeb.fillPassengerDetailInfant(adultCount, infantCount);

        if (Configs.PLATFORM.equals(Platform.DWEB)) {
            step("User clicks Total price details button");
            whooshBookingPageWeb.checkPriceBtn();

            step("User verifies whether price details page opens or not");
            Assertion.assertThat("Verifies whether price details page appears or not", whooshBookingPageWeb.isPriceDetailsDisplayed(), is(true));

            step("User verifies the price details on booking page");
            Assertion.assertThat("Verifies the price on booking page", whooshBookingPageWeb.checkPrice(adultCount), is(price));
        } else {
            step("User continues booking after filling details");
            whooshBookingPageWeb.clickContinue();
            whooshBookingPageWeb.ClickAnimatedChevronIcon_icon_chevron();

            step("User verifies the price details on booking page");
            Assertion.assertThat("Verifies the price on booking page", whooshBookingPageWeb.checkPrice(adultCount), is(price));
        }
    }

    @Author(names = {"Akshat", "Vipul Kumar"})
    @Module(name = "HomePage")
    @Tribe(name = "Whoosh")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyWhooshIsStillDisplayedNonIDRLoginWeb() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testVerifyWhooshIsStillDisplayedNonIDRLoginWeb");
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

        step("User change to " + currency + " currency");
        homePage.selectCurrency(currency);

        step("User is redirected back to home page");
        if (Configs.PLATFORM.equals(Platform.DWEB)) {
            Assertion.assertThat("Verify Whoosh is still displayed on home page", homePage.verifyVerticalIsDisplayed("kcic"), is(true));
        } else {
            Assertion.assertThat("Verify Whoosh is still displayed on home page", homePage.verifyVerticalIsDisplayed("Whoosh"), is(true));
        }
    }

    @Author(names = {"Akshat", "Vipul Kumar"})
    @Module(name = "HomePage")
    @Tribe(name = "Whoosh")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyWhooshIsStillDisplayedNonIDRGuestUserWeb() {
        Map<String, String> param = getParam("testVerifyWhooshIsStillDisplayedNonIDRGuestUserWeb");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String currency = param.get("currency");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as guest");
        loginPage.loginAsGuest();

        step("User change to " + currency + " currency");
        homePage.selectCurrency(currency);

        step("User is redirected back to home page");
        if (Configs.PLATFORM.equals(Platform.DWEB)) {
            Assertion.assertThat("Verify Whoosh is still displayed on home page", homePage.verifyVerticalIsDisplayed("kcic"), is(true));
        } else {
            Assertion.assertThat("Verify Whoosh is still displayed on home page", homePage.verifyVerticalIsDisplayed("Whoosh"), is(true));
        }
    }

    @Author(names = {"Akshat", "Vipul Kumar"})
    @Module(name = "Whoosh")
    @Tribe(name = "NFT")
    @Vertical(name = "NFT")
    @Test
    public void Whoosh_testVerifyPaymentDetailsAsLoggedInUserWeb() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("Whoosh_testVerifyPaymentDetailsAsLoggedInUserWeb");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String currency = param.get("currency");
        String originStation = param.get("originStation");
        String destinationStation = param.get("destinationStation");
        String tripType = param.get("tripType");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);
        int adultCount = Integer.parseInt(param.get("adultCount"));
        int infantCount = Integer.parseInt(param.get("infantCount"));

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        WhooshSearchPageWeb<?> whooshSearchPageWeb = WhooshSearchPageWeb.getInstance(session);
        WhooshSearchResultPageWeb<?> whooshSearchResultPageWeb = WhooshSearchResultPageWeb.getInstance(session);
        WhooshBookingPageWeb<?> whooshBookingPageWeb = WhooshBookingPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open home page as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User select IDR currency");
        homePage.selectCurrency(currency);

        step("User open whoosh vertical");
        homePage.openVerticalPageViaURL("whoosh");

        step("User skip pop-up");
        whooshSearchPageWeb.clickSkipPopup();

        step("User select origin station");
        whooshSearchPageWeb.selectOrigin(originStation);

        step("User select destination station");
        whooshSearchPageWeb.selectDestination(destinationStation);

        step("User toggle one way");
        whooshSearchPageWeb.toggleTripType(tripType);

        step("User selects departure date");
        whooshSearchPageWeb.searchDepDate(departureDate);

        step("User select passenger");
        whooshSearchPageWeb.selectPassenger(adultCount, infantCount);

        step("User saves the passengers");
        whooshSearchPageWeb.savePassengers();

        step("User search whoosh");
        whooshSearchPageWeb.searchWhoosh();

        step("User verifies if he is on SRP or not");
        Assertion.assertThat("Verify if the user is on SRP or not", whooshSearchResultPageWeb.checkSRP(), is(true));

        step("User skips the filter option");
        whooshSearchResultPageWeb.clickSkip();

        step("User select first Whoosh");
        String departureWhoosh = whooshSearchResultPageWeb.clickFirstWhoosh();

        step("User fill adult details");
        whooshBookingPageWeb.fillPassengerDetailAdult(adultCount);

        step("User fill infant details");
        whooshBookingPageWeb.fillPassengerDetailInfant(adultCount, infantCount);

        step("User continues booking after filling details");
        whooshBookingPageWeb.clickContinue();

        step("User verifies the departure whoosh");
        Assertion.assertThat("verifies departure whoosh", whooshBookingPageWeb.verifiesDepartureWhoosh(tripType), is(departureWhoosh));

        step("User verifies departure origin station");
        Assertion.assertThat("verifies departure origin station", whooshBookingPageWeb.verifyDepartureOrigin(tripType), is(originStation));

        step("User verifies departure destination station");
        Assertion.assertThat("verifies departure destination station", whooshBookingPageWeb.verifyDepartureDestination(tripType), is(destinationStation));
        String originalPrice = whooshBookingPageWeb.getTotalToPayAfterTaxPrice();

        step("User continues for payment");
        whooshBookingPageWeb.continueToPay();

        step("User Go to Payment Page");
        whooshBookingPageWeb.goToPaymentPage();
        String orderID = paymentPage.getOrderId();
        Assertion.assertThat("Verify order ID is displayed in payment page", !orderID.isEmpty(), is(true));

        step("User clicks on check details button");
        whooshBookingPageWeb.clickPriceBtn();

        step("User verify origin station");
        Assertion.assertThat("Verify origin station", whooshBookingPageWeb.verifyOrigin(), is(originStation));

        step("User verify destination station");
        Assertion.assertThat("Verify destination station", whooshBookingPageWeb.verifyDestination(), is(destinationStation));

        step("User verifies the base price");
        Assertion.assertThat("Verifies the base price", whooshBookingPageWeb.checkPriceOnPaymentPage(adultCount), is(originalPrice));

        step("User verifies the Convenience fees");
        Assertion.assertThat("verifies the convenience fees", whooshBookingPageWeb.checkConvenienceFee(), is(true));

        step("User verifies the service fee");
        Assertion.assertThat("verifies the service fees", whooshBookingPageWeb.checkServiceFee(), is(true));

        step("User gets the total price");
        String totalPrice = whooshBookingPageWeb.getTotalPrice();

        step("User verifies the total price");
        Assertion.assertThat("verifies the total price", whooshBookingPageWeb.getOverallPrice(originalPrice), is(totalPrice));
    }

    @Author(names = {"Akshat", "Vipul Kumar"})
    @Module(name = "BookingPage")
    @Tribe(name = "Whoosh")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyPaymentDetailsAsGuestUserWeb() throws InterruptedException {
        Map<String, String> param = getParam("testVerifyPaymentDetailsAsGuestUserWeb");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String originStation = param.get("originStation");
        String destinationStation = param.get("destinationStation");
        String tripType = param.get("tripType");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);
        int adultCount = Integer.parseInt(param.get("adultCount"));
        int infantCount = Integer.parseInt(param.get("infantCount"));
        String fullName = CoreFunctions.randomName();
        String phoneNumber = "91" + CoreFunctions.randomInt(8);
        String emailID = CoreFunctions.randomEmail();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        WhooshSearchPageWeb<?> whooshSearchPageWeb = WhooshSearchPageWeb.getInstance(session);
        WhooshSearchResultPageWeb<?> whooshSearchResultPageWeb = WhooshSearchResultPageWeb.getInstance(session);
        WhooshBookingPageWeb<?> whooshBookingPageWeb = WhooshBookingPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as guest");
        loginPage.loginAsGuest();

        step("User open whoosh vertical");
        homePage.openVerticalPageViaURL("whoosh");

        step("User skip pop-up");
        whooshSearchPageWeb.clickSkipPopup();

        step("User select origin station");
        whooshSearchPageWeb.selectOrigin(originStation);

        step("User select destination station");
        whooshSearchPageWeb.selectDestination(destinationStation);

        step("User toggle one way");
        whooshSearchPageWeb.toggleTripType(tripType);

        step("User selects departure date");
        whooshSearchPageWeb.searchDepDate(departureDate);

        step("User select passenger");
        whooshSearchPageWeb.selectPassenger(adultCount, infantCount);

        step("User saves the passengers");
        whooshSearchPageWeb.savePassengers();

        step("User search whoosh");
        whooshSearchPageWeb.searchWhoosh();

        step("User verifies if he is on SRP or not");
        Assertion.assertThat("Verify if the user is on SRP or not", whooshSearchResultPageWeb.checkSRP(), is(true));

        step("User skips the filter option");
        whooshSearchResultPageWeb.clickSkip();

        step("User select first Whoosh");
        String originalPrice = whooshSearchResultPageWeb.getPrice();
        String departureWhoosh = whooshSearchResultPageWeb.clickFirstWhoosh();

        step("User enter contact details for refunds");
        whooshBookingPageWeb.fillContactDetails(fullName, phoneNumber, emailID);

        step("User fill adult details");
        whooshBookingPageWeb.fillPassengerDetailAdult(adultCount);

        step("User fill infant details");
        whooshBookingPageWeb.fillPassengerDetailInfant(adultCount, infantCount);

        step("User continues booking after filling details");
        whooshBookingPageWeb.clickContinue();

        step("User verifies the departure whoosh");
        Assertion.assertThat("verifies departure whoosh", whooshBookingPageWeb.verifiesDepartureWhoosh(tripType), is(departureWhoosh));

        step("User verifies departure origin station");
        Assertion.assertThat("verifies departure origin station", whooshBookingPageWeb.verifyDepartureOrigin(tripType), is(originStation));

        step("User verifies departure destination station");
        Assertion.assertThat("verifies departure destination station", whooshBookingPageWeb.verifyDepartureDestination(tripType), is(destinationStation));

        step("User continues for payment");
        whooshBookingPageWeb.continueToPay();

        step("User Go to Payment Page");
        whooshBookingPageWeb.goToPaymentPage();
        String orderID = paymentPage.getOrderId();
        Assertion.assertThat("Verify order ID is displayed in payment page", !orderID.isEmpty(), is(true));

        step("User clicks on check details button");
        whooshBookingPageWeb.clickPriceBtn();

        step("User verify origin station");
        Assertion.assertThat("Verify origin station", whooshBookingPageWeb.verifyOrigin(), is(originStation));

        step("User verify destination station");
        Assertion.assertThat("Verify destination station", whooshBookingPageWeb.verifyDestination(), is(destinationStation));

        step("User verifies the base price");
        Assertion.assertThat("Verifies the base price", whooshBookingPageWeb.checkPriceOnPaymentPage(adultCount), is(originalPrice));

        step("User verifies the Convenience fees");
        Assertion.assertThat("verifies the convenience fees", whooshBookingPageWeb.checkConvenienceFee(), is(true));

        step("User verifies the service fee");
        Assertion.assertThat("verifies the service fees", whooshBookingPageWeb.checkServiceFee(), is(true));

        step("User gets the total price");
        String totalPrice = whooshBookingPageWeb.getTotalPrice();

        step("User verifies the total price");
        Assertion.assertThat("verifies the total price", whooshBookingPageWeb.getOverallPrice(originalPrice), is(totalPrice));
    }

    @Author(names = {"Akshat", "Vipul Kumar"})
    @Module(name = "BookingPage")
    @Tribe(name = "Whoosh")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyWhetherUserAbleToBookViaDeeplinkWithNonIDRCurrencyLoginWeb() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testVerifyWhetherUserAbleToBookViaDeeplinkWithNonIDRCurrencyLoginWeb");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String currency = param.get("currency");
        String originStation = param.get("originStation");
        String destinationStation = param.get("destinationStation");
        String tripType = param.get("tripType");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);
        int adultCount = Integer.parseInt(param.get("adultCount"));
        int infantCount = Integer.parseInt(param.get("infantCount"));

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        WhooshSearchPageWeb<?> whooshSearchPageWeb = WhooshSearchPageWeb.getInstance(session);
        WhooshSearchResultPageWeb<?> whooshSearchResultPageWeb = WhooshSearchResultPageWeb.getInstance(session);
        WhooshBookingPageWeb<?> whooshBookingPageWeb = WhooshBookingPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open home page as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User select non-IDR currency");
        homePage.selectCurrency(currency);

        step("User open whoosh vertical");
        homePage.openVerticalPageViaURL("whoosh");

        step("User skip pop-up");
        whooshSearchPageWeb.clickSkipPopup();

        step("User select origin station");
        whooshSearchPageWeb.selectOrigin(originStation);

        step("User select destination station");
        whooshSearchPageWeb.selectDestination(destinationStation);

        step("User toggle one way");
        whooshSearchPageWeb.toggleTripType(tripType);

        step("User selects departure date");
        whooshSearchPageWeb.searchDepDate(departureDate);

        step("User select passenger");
        whooshSearchPageWeb.selectPassenger(adultCount, infantCount);

        step("User saves the passengers");
        whooshSearchPageWeb.savePassengers();

        step("User search whoosh");
        whooshSearchPageWeb.searchWhoosh();

        step("User verifies if he is on SRP or not");
        Assertion.assertThat("Verify if the user is on SRP or not", whooshSearchResultPageWeb.checkSRP(), is(true));

        step("User skips the filter option");
        whooshSearchResultPageWeb.clickSkip();

        step("User select first Whoosh");
        String departureWhoosh = whooshSearchResultPageWeb.clickFirstWhoosh();

        step("User fill adult details");
        whooshBookingPageWeb.fillPassengerDetailAdult(adultCount);

        step("User fill infant details");
        whooshBookingPageWeb.fillPassengerDetailInfant(adultCount, infantCount);

        step("User continues booking after filling details");
        whooshBookingPageWeb.clickContinue();

        step("User verifies the departure whoosh");
        Assertion.assertThat("verifies departure whoosh", whooshBookingPageWeb.verifiesDepartureWhoosh(tripType), is(departureWhoosh));

        step("User verifies departure origin station");
        Assertion.assertThat("verifies departure origin station", whooshBookingPageWeb.verifyDepartureOrigin(tripType), is(originStation));

        step("User verifies departure destination station");
        Assertion.assertThat("verifies departure destination station", whooshBookingPageWeb.verifyDepartureDestination(tripType), is(destinationStation));

        step("User continues for payment");
        whooshBookingPageWeb.continueToPay();

        step("User Go to Payment Page");
        whooshBookingPageWeb.goToPaymentPage();
        String orderID = paymentPage.getOrderId();
        Assertion.assertThat("Verify order ID is displayed in payment page", !orderID.isEmpty(), is(true));
    }

    @Author(names = {"Akshat", "Vipul Kumar"})
    @Module(name = "BookingPage")
    @Tribe(name = "Whoosh")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyWhetherUserAbleToBookViaDeeplinkWithNonIDRCurrencyGuestWeb() throws InterruptedException {
        Map<String, String> param = getParam("testVerifyWhetherUserAbleToBookViaDeeplinkWithNonIDRCurrencyGuestWeb");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String originStation = param.get("originStation");
        String destinationStation = param.get("destinationStation");
        String tripType = param.get("tripType");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);
        int adultCount = Integer.parseInt(param.get("adultCount"));
        int infantCount = Integer.parseInt(param.get("infantCount"));
        String fullName = CoreFunctions.randomName();
        String phoneNumber = "91" + CoreFunctions.randomInt(8);
        String emailID = CoreFunctions.randomEmail();
        String currency = param.get("currency");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        WhooshSearchPageWeb<?> whooshSearchPageWeb = WhooshSearchPageWeb.getInstance(session);
        WhooshSearchResultPageWeb<?> whooshSearchResultPageWeb = WhooshSearchResultPageWeb.getInstance(session);
        WhooshBookingPageWeb<?> whooshBookingPageWeb = WhooshBookingPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as guest");
        loginPage.loginAsGuest();

        step("User select non-IDR currency");
        homePage.selectCurrency(currency);

        step("User open whoosh vertical");
        homePage.openVerticalPageViaURL("whoosh");

        step("User skip pop-up");
        whooshSearchPageWeb.clickSkipPopup();

        step("User select origin station");
        whooshSearchPageWeb.selectOrigin(originStation);

        step("User select destination station");
        whooshSearchPageWeb.selectDestination(destinationStation);

        step("User toggle one way");
        whooshSearchPageWeb.toggleTripType(tripType);

        step("User selects departure date");
        whooshSearchPageWeb.searchDepDate(departureDate);

        step("User select passenger");
        whooshSearchPageWeb.selectPassenger(adultCount, infantCount);

        step("User saves the passengers");
        whooshSearchPageWeb.savePassengers();

        step("User search whoosh");
        whooshSearchPageWeb.searchWhoosh();

        step("User verifies if he is on SRP or not");
        Assertion.assertThat("Verify if the user is on SRP or not", whooshSearchResultPageWeb.checkSRP(), is(true));

        step("User skips the filter option");
        whooshSearchResultPageWeb.clickSkip();

        step("User select first Whoosh");
        String departureWhoosh = whooshSearchResultPageWeb.clickFirstWhoosh();

        step("User enter contact details for refunds");
        whooshBookingPageWeb.fillContactDetails(fullName, phoneNumber, emailID);

        step("User fill adult details");
        whooshBookingPageWeb.fillPassengerDetailAdult(adultCount);

        step("User fill infant details");
        whooshBookingPageWeb.fillPassengerDetailInfant(adultCount, infantCount);

        step("User continues booking after filling details");
        whooshBookingPageWeb.clickContinue();

        step("User verifies the departure whoosh");
        Assertion.assertThat("verifies departure whoosh", whooshBookingPageWeb.verifiesDepartureWhoosh(tripType), is(departureWhoosh));

        step("User verifies departure origin station");
        Assertion.assertThat("verifies departure origin station", whooshBookingPageWeb.verifyDepartureOrigin(tripType), is(originStation));

        step("User verifies departure destination station");
        Assertion.assertThat("verifies departure destination station", whooshBookingPageWeb.verifyDepartureDestination(tripType), is(destinationStation));

        step("User continues for payment");
        whooshBookingPageWeb.continueToPay();

        step("User Go to Payment Page");
        whooshBookingPageWeb.goToPaymentPage();
        String orderID = paymentPage.getOrderId();
        Assertion.assertThat("Verify order ID is displayed in payment page", !orderID.isEmpty(), is(true));
    }

    @Author(names = {"Akshat", "Vipul Kumar"})
    @Module(name = "SRP")
    @Tribe(name = "Whoosh")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyCrossSellToBusWithNoRouteGuestWeb() {
        Map<String, String> param = getParam("testVerifyCrossSellToBusWithNoRouteGuestWeb");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String originStation = param.get("originStation");
        String destinationStation = param.get("destinationStation");
        String tripType = param.get("tripType");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);
        int adultCount = Integer.parseInt(param.get("adultCount"));
        int infantCount = Integer.parseInt(param.get("infantCount"));

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        WhooshSearchPageWeb<?> whooshSearchPageWeb = WhooshSearchPageWeb.getInstance(session);
        WhooshSearchResultPageWeb<?> whooshSearchResultPageWeb = WhooshSearchResultPageWeb.getInstance(session);
        CalendarPageWeb<?> calendarPage = CalendarPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as guest");
        loginPage.loginAsGuest();

        step("User open whoosh vertical");
        homePage.openVerticalPageViaURL("whoosh");

        step("User skip pop-up");
        whooshSearchPageWeb.clickSkipPopup();

        step("User select origin station");
        whooshSearchPageWeb.selectOrigin(originStation);

        step("User select destination station");
        whooshSearchPageWeb.selectDestination(destinationStation);

        step("User toggle one way");
        whooshSearchPageWeb.toggleTripType(tripType);

        step("User selects departure date");
        whooshSearchPageWeb.clickOnCalendarWidgetToOpen();
        calendarPage.selectDate(departureDate);

        step("User select passenger");
        whooshSearchPageWeb.selectPassenger(adultCount, infantCount);

        step("User saves the passengers");
        whooshSearchPageWeb.savePassengers();

        step("User search whoosh");
        whooshSearchPageWeb.searchWhoosh();

        step("User verifies whether there is any whoosh or not");
        Assertion.assertThat(("Verifies if there is any whoosh or not"), whooshSearchResultPageWeb.checkWhoosh(), is(true));

        step("User search for bus and shuttles");
        whooshSearchResultPageWeb.clickBusBtn();

        step("User verifies whether the bus and shuttle page is displayed or not");
        Assertion.assertThat("verifies the bus and shuttle page", whooshSearchResultPageWeb.verifyBusPage(), is(true));
    }

    @Author(names = {"Akshat", "Vipul Kumar"})
    @Module(name = "SRP")
    @Tribe(name = "Whoosh")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyCrossSellToBusWithNoRouteLoginWeb() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testVerifyCrossSellToBusWithNoRouteLoginWeb");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String currency = param.get("currency");
        String originStation = param.get("originStation");
        String destinationStation = param.get("destinationStation");
        String tripType = param.get("tripType");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);
        int adultCount = Integer.parseInt(param.get("adultCount"));
        int infantCount = Integer.parseInt(param.get("infantCount"));

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        WhooshSearchPageWeb<?> whooshSearchPageWeb = WhooshSearchPageWeb.getInstance(session);
        WhooshSearchResultPageWeb<?> whooshSearchResultPageWeb = WhooshSearchResultPageWeb.getInstance(session);
        CalendarPageWeb<?> calendarPage = CalendarPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("Login as " + email + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(email, password);

        step("User select IDR currency");
        homePage.selectCurrency(currency);

        step("User open whoosh vertical");
        homePage.openVerticalPageViaURL("whoosh");

        step("User skip pop-up");
        whooshSearchPageWeb.clickSkipPopup();

        step("User select origin station");
        whooshSearchPageWeb.selectOrigin(originStation);

        step("User select destination station");
        whooshSearchPageWeb.selectDestination(destinationStation);

        step("User toggle one way");
        whooshSearchPageWeb.toggleTripType(tripType);

        step("User selects departure date");
        whooshSearchPageWeb.clickOnCalendarWidgetToOpen();
        calendarPage.selectDate(departureDate);

        step("User select passenger");
        whooshSearchPageWeb.selectPassenger(adultCount, infantCount);

        step("User saves the passengers");
        whooshSearchPageWeb.savePassengers();

        step("User search whoosh");
        whooshSearchPageWeb.searchWhoosh();

        step("User verifies whether there is any whoosh or not");
        Assertion.assertThat(("Verifies if there is any whoosh or not"), whooshSearchResultPageWeb.checkWhoosh(), is(true));

        step("User search for bus and shuttles");
        whooshSearchResultPageWeb.clickBusBtn();

        step("User verifies whether the bus and shuttle page is displayed or not");
        Assertion.assertThat("verifies the bus and shuttle page", whooshSearchResultPageWeb.verifyBusPage(), is(true));
    }

    @Author(names = {"Akshat", "Vipul Kumar"})
    @Module(name = "SRP")
    @Tribe(name = "Whoosh")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyCrossSellToBusWithPartialRouteGuestWeb() {
        Map<String, String> param = getParam("testVerifyCrossSellToBusWithPartialRouteGuestWeb");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String originStation = param.get("originStation");
        String destinationStation = param.get("destinationStation");
        String tripType = param.get("tripType");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);
        int adultCount = Integer.parseInt(param.get("adultCount"));
        int infantCount = Integer.parseInt(param.get("infantCount"));

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        WhooshSearchPageWeb<?> whooshSearchPageWeb = WhooshSearchPageWeb.getInstance(session);
        WhooshSearchResultPageWeb<?> whooshSearchResultPageWeb = WhooshSearchResultPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as guest");
        loginPage.loginAsGuest();

        step("User open whoosh vertical");
        homePage.openVerticalPageViaURL("whoosh");

        step("User skip pop-up");
        whooshSearchPageWeb.clickSkipPopup();

        step("User select origin station");
        whooshSearchPageWeb.selectOrigin(originStation);

        step("User select destination station");
        whooshSearchPageWeb.selectDestination(destinationStation);

        step("User toggle one way");
        whooshSearchPageWeb.toggleTripType(tripType);

        step("User selects departure date");
        whooshSearchPageWeb.searchDepDate(departureDate);

        step("User select passenger");
        whooshSearchPageWeb.selectPassenger(adultCount, infantCount);

        step("User saves the passengers");
        whooshSearchPageWeb.savePassengers();

        step("User search whoosh");
        whooshSearchPageWeb.searchWhoosh();

        step("User verifies if he is on SRP or not");
        Assertion.assertThat("Verify if the user is on SRP or not", whooshSearchResultPageWeb.checkSRP(), is(true));

        step("User skips the filter option");
        whooshSearchResultPageWeb.clickSkip();

        step("User search for bus and shuttles");
        whooshSearchResultPageWeb.clickBusBtn();

        step("User skips the filter option");
        whooshSearchResultPageWeb.clickSkip();

        step("User verifies whether the bus and shuttle page is displayed or not");
        Assertion.assertThat("verifies the bus and shuttle page", whooshSearchResultPageWeb.verifyBusPage(), is(true));

        if (Configs.PLATFORM.equals(Platform.DWEB)) {
            step("User verifies the origin station on bus search result page");
            Assertion.assertThat("verifies the origin station on bus SRP", whooshSearchResultPageWeb.checkOriginOnBusSRP(), containsString(originStation));

            step("User verifies the destination station on bus search result page");
            Assertion.assertThat("verifies the destination station on bus SRP", whooshSearchResultPageWeb.checkDestinationOnBusSRP(), containsString(destinationStation));
        }
    }

    @Author(names = {"Akshat", "Vipul Kumar"})
    @Module(name = "SRP")
    @Tribe(name = "Whoosh")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyCrossSellToBusWithPartialRouteLoginWeb() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testVerifyCrossSellToBusWithPartialRouteLoginWeb");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String currency = param.get("currency");
        String originStation = param.get("originStation");
        String destinationStation = param.get("destinationStation");
        String tripType = param.get("tripType");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);
        int adultCount = Integer.parseInt(param.get("adultCount"));
        int infantCount = Integer.parseInt(param.get("infantCount"));

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        CalendarPageWeb<?> calendarPage = CalendarPageWeb.getInstance(session);
        WhooshSearchPageWeb<?> whooshSearchPageWeb = WhooshSearchPageWeb.getInstance(session);
        WhooshSearchResultPageWeb<?> whooshSearchResultPageWeb = WhooshSearchResultPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("Login as " + email + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(email, password);

        step("User select IDR currency");
        homePage.selectCurrency(currency);

        step("User open whoosh vertical");
        homePage.openVerticalPageViaURL("whoosh");

        step("User skip pop-up");
        whooshSearchPageWeb.clickSkipPopup();

        step("User select origin station");
        whooshSearchPageWeb.selectOrigin(originStation);

        step("User select destination station");
        whooshSearchPageWeb.selectDestination(destinationStation);

        step("User toggle one way");
        whooshSearchPageWeb.toggleTripType(tripType);

        step("User selects departure date");
        whooshSearchPageWeb.clickOnCalendarWidgetToOpen();
        calendarPage.selectDate(departureDate);

        step("User select passenger");
        whooshSearchPageWeb.selectPassenger(adultCount, infantCount);

        step("User saves the passengers");
        whooshSearchPageWeb.savePassengers();

        step("User search whoosh");
        whooshSearchPageWeb.searchWhoosh();

        step("User verifies if he is on SRP or not");
        Assertion.assertThat("Verify if the user is on SRP or not", whooshSearchResultPageWeb.checkSRP(), is(true));

        step("User skips the filter option");
        whooshSearchResultPageWeb.clickSkip();

        step("User search for bus and shuttles");
        whooshSearchResultPageWeb.clickBusBtn();

        step("User skips the filter option");
        whooshSearchResultPageWeb.clickSkip();

        step("User verifies whether the bus and shuttle page is displayed or not");
        Assertion.assertThat("verifies the bus and shuttle page", whooshSearchResultPageWeb.verifyBusPage(), is(true));

        if (Configs.PLATFORM.equals(Platform.DWEB)) {
            step("User verifies the origin station on bus search result page");
            Assertion.assertThat("verifies the origin station on bus SRP", whooshSearchResultPageWeb.checkOriginOnBusSRP(), containsString(originStation));

            step("User verifies the destination station on bus search result page");
            Assertion.assertThat("verifies the destination station on bus SRP", whooshSearchResultPageWeb.checkDestinationOnBusSRP(), containsString(destinationStation));
        }
    }

    @Author(names = "Akshat")
    @Module(name = "CompleteBooking")
    @Tribe(name = "Whoosh")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyCompleteWhooshBookingOneWayAsGuestUser() throws InterruptedException {
        Map<String, String> param = getParam("testVerifyCompleteWhooshBookingOneWayAsGuestUser");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String currency = param.get("currency");
        String originStation = param.get("originStation");
        String destinationStation = param.get("destinationStation");
        String tripType = param.get("tripType");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);
        int adultCount = Integer.parseInt(param.get("adultCount"));
        int infantCount = Integer.parseInt(param.get("infantCount"));
        String fullName = CoreFunctions.randomName();
        String phoneNumber = "91" + CoreFunctions.randomInt(8);
        String emailID = CoreFunctions.randomEmail();
        String paymentMethod = param.get("paymentMethod");
        String paymentGroup = param.get("paymentGroup");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        WhooshSearchPageWeb<?> whooshSearchPageWeb = WhooshSearchPageWeb.getInstance(session);
        WhooshSearchResultPageWeb<?> whooshSearchResultPageWeb = WhooshSearchResultPageWeb.getInstance(session);
        WhooshBookingPageWeb<?> whooshBookingPageWeb = WhooshBookingPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open home page as logged in user");
        loginPage.loginAsGuest();

        step("User select IDR currency");
        homePage.selectCurrency(currency);

        step("User open whoosh vertical");
        homePage.openVerticalPageViaURL("whoosh");

        step("User skip pop-up");
        whooshSearchPageWeb.clickSkipPopup();

        step("User select origin station");
        whooshSearchPageWeb.selectOrigin(originStation);

        step("User select destination station");
        whooshSearchPageWeb.selectDestination(destinationStation);

        step("User toggle one way");
        whooshSearchPageWeb.toggleTripType(tripType);

        step("User selects departure date");
        whooshSearchPageWeb.searchDepDate(departureDate);

        step("User select passenger");
        whooshSearchPageWeb.selectPassenger(adultCount, infantCount);

        step("User saves the passengers");
        whooshSearchPageWeb.savePassengers();

        step("User search whoosh");
        whooshSearchPageWeb.searchWhoosh();

        step("User verifies if he is on SRP or not");
        Assertion.assertThat("Verify if the user is on SRP or not", whooshSearchResultPageWeb.checkSRP(), is(true));

        step("User skips the filter option");
        whooshSearchResultPageWeb.clickSkip();

        step("User select first Whoosh");
        String departureWhoosh = whooshSearchResultPageWeb.clickFirstWhoosh();

        step("User enter contact details for refunds");
        whooshBookingPageWeb.fillContactDetails(fullName, phoneNumber, emailID);

        step("User fill adult details");
        whooshBookingPageWeb.fillPassengerDetailAdult(adultCount);

        step("User fill infant details");
        whooshBookingPageWeb.fillPassengerDetailInfant(adultCount, infantCount);

        step("User continues booking after filling details");
        whooshBookingPageWeb.clickContinue();

        step("User verifies the departure whoosh");
        Assertion.assertThat("verifies departure whoosh", whooshBookingPageWeb.verifiesDepartureWhoosh(tripType), is(departureWhoosh));

        step("User verifies departure origin station");
        Assertion.assertThat("verifies departure origin station", whooshBookingPageWeb.verifyDepartureOrigin(tripType), is(originStation));

        step("User verifies departure destination station");
        Assertion.assertThat("verifies departure destination station", whooshBookingPageWeb.verifyDepartureDestination(tripType), is(destinationStation));

        step("User continues for payment");
        whooshBookingPageWeb.continueToPay();

        step("User Go to Payment Page");
        whooshBookingPageWeb.goToPaymentPage();
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

    @Author(names = "Akshat")
    @Module(name = "CompleteBooking")
    @Tribe(name = "Whoosh")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyCompleteWhooshBookingWithInsuranceOneWayAsGuestUser() throws InterruptedException {
        Map<String, String> param = getParam("testVerifyCompleteWhooshBookingWithInsuranceOneWayAsGuestUser");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String currency = param.get("currency");
        String originStation = param.get("originStation");
        String destinationStation = param.get("destinationStation");
        String tripType = param.get("tripType");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);
        int adultCount = Integer.parseInt(param.get("adultCount"));
        int infantCount = Integer.parseInt(param.get("infantCount"));
        String fullName = CoreFunctions.randomName();
        String phoneNumber = "91" + CoreFunctions.randomInt(8);
        String emailID = CoreFunctions.randomEmail();
        String paymentMethod = param.get("paymentMethod");
        String paymentGroup = param.get("paymentGroup");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        WhooshSearchPageWeb<?> whooshSearchPageWeb = WhooshSearchPageWeb.getInstance(session);
        WhooshSearchResultPageWeb<?> whooshSearchResultPageWeb = WhooshSearchResultPageWeb.getInstance(session);
        WhooshBookingPageWeb<?> whooshBookingPageWeb = WhooshBookingPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open home page as logged in user");
        loginPage.loginAsGuest();

        step("User select IDR currency");
        homePage.selectCurrency(currency);

        step("User open whoosh vertical");
        homePage.openVerticalPageViaURL("whoosh");

        step("User skip pop-up");
        whooshSearchPageWeb.clickSkipPopup();

        step("User selects whoosh");
        whooshSearchPageWeb.selectWhoosh();

        step("User select origin station");
        whooshSearchPageWeb.selectOrigin(originStation);

        step("User select destination station");
        whooshSearchPageWeb.selectDestination(destinationStation);

        step("User toggle one way");
        whooshSearchPageWeb.toggleTripType(tripType);

        step("User selects departure date");
        whooshSearchPageWeb.searchDepDate(departureDate);

        step("User select passenger");
        whooshSearchPageWeb.selectPassenger(adultCount, infantCount);

        step("User saves the passengers");
        whooshSearchPageWeb.savePassengers();

        step("User search whoosh");
        whooshSearchPageWeb.searchWhoosh();

        step("User verifies if he is on SRP or not");
        Assertion.assertThat("Verify if the user is on SRP or not", whooshSearchResultPageWeb.checkSRP(), is(true));

        step("User skips the filter option");
        whooshSearchResultPageWeb.clickSkip();

        step("User select first Whoosh");
        String departureWhoosh = whooshSearchResultPageWeb.clickFirstWhoosh();

        step("User enter contact details for refunds");
        whooshBookingPageWeb.fillContactDetails(fullName, phoneNumber, emailID);

        step("User fill adult details");
        whooshBookingPageWeb.fillPassengerDetailAdult(adultCount);

        step("User fill infant details");
        whooshBookingPageWeb.fillPassengerDetailInfant(adultCount, infantCount);

        step("User continues booking after filling details");
        whooshBookingPageWeb.clickContinue();

        step("User verifies the departure whoosh");
        Assertion.assertThat("verifies departure whoosh", whooshBookingPageWeb.verifiesDepartureWhoosh(tripType), is(departureWhoosh));

        step("User verifies departure origin station");
        Assertion.assertThat("verifies departure origin station", whooshBookingPageWeb.verifyDepartureOrigin(tripType), is(originStation));

        step("User verifies departure destination station");
        Assertion.assertThat("verifies departure destination station", whooshBookingPageWeb.verifyDepartureDestination(tripType), is(destinationStation));

        step("User adds insurance");
        whooshBookingPageWeb.addInsurance();

        step("User continues for payment");
        whooshBookingPageWeb.continueToPay();

        step("User Go to Payment Page");
        whooshBookingPageWeb.goToPaymentPage();
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

    @Author(names = "Akshat")
    @Module(name = "CompleteBooking")
    @Tribe(name = "Whoosh")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyIssuedOrderOneWayAsLoggedInUser() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testVerifyIssuedOrderOneWayAsLoggedInUser");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String currency = param.get("currency");
        String originStation = param.get("originStation");
        String destinationStation = param.get("destinationStation");
        String tripType = param.get("tripType");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);
        int adultCount = Integer.parseInt(param.get("adultCount"));
        int infantCount = Integer.parseInt(param.get("infantCount"));
        String paymentMethod = param.get("paymentMethod");
        String paymentGroup = param.get("paymentGroup");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        WhooshSearchPageWeb<?> whooshSearchPageWeb = WhooshSearchPageWeb.getInstance(session);
        WhooshSearchResultPageWeb<?> whooshSearchResultPageWeb = WhooshSearchResultPageWeb.getInstance(session);
        WhooshBookingPageWeb<?> whooshBookingPageWeb = WhooshBookingPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open home page as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User select IDR currency");
        homePage.selectCurrency(currency);

        step("User open whoosh vertical");
        homePage.openVerticalPageViaURL("whoosh");

        step("User skip pop-up");
        whooshSearchPageWeb.clickSkipPopup();

        step("User select origin station");
        whooshSearchPageWeb.selectOrigin(originStation);

        step("User select destination station");
        whooshSearchPageWeb.selectDestination(destinationStation);

        step("User toggle one way");
        whooshSearchPageWeb.toggleTripType(tripType);

        step("User selects departure date");
        String depDate = whooshSearchPageWeb.searchDepDate(departureDate);

        step("User select passenger");
        whooshSearchPageWeb.selectPassenger(adultCount, infantCount);

        step("User saves the passengers");
        whooshSearchPageWeb.savePassengers();

        step("User search whoosh");
        whooshSearchPageWeb.searchWhoosh();

        step("User verifies if he is on SRP or not");
        Assertion.assertThat("Verify if the user is on SRP or not", whooshSearchResultPageWeb.checkSRP(), is(true));

        step("User skips the filter option");
        whooshSearchResultPageWeb.clickSkip();

        step("User select first Whoosh");
        String departureWhoosh = whooshSearchResultPageWeb.clickFirstWhoosh();

        step("User fill adult details");
        List<String> namesAdult = whooshBookingPageWeb.fillPassengerDetailAdult(adultCount);

        step("User fill infant details");
        List<String> namesInfant = whooshBookingPageWeb.fillPassengerDetailInfant(adultCount, infantCount);

        step("Gets the id of passengers");
        List<String> passengerIDs = whooshBookingPageWeb.getPassengerIDs(adultCount, infantCount);

        step("User continues booking after filling details");
        whooshBookingPageWeb.clickContinue();

        step("User verifies the departure whoosh");
        Assertion.assertThat("verifies departure whoosh", whooshBookingPageWeb.verifiesDepartureWhoosh(tripType), is(departureWhoosh));

        step("User verifies departure origin station");
        Assertion.assertThat("verifies departure origin station", whooshBookingPageWeb.verifyDepartureOrigin(tripType), is(originStation));

        step("User verifies departure destination station");
        Assertion.assertThat("verifies departure destination station", whooshBookingPageWeb.verifyDepartureDestination(tripType), is(destinationStation));

        step("User continues for payment");
        whooshBookingPageWeb.continueToPay();

        step("User Go to Payment Page");
        whooshBookingPageWeb.goToPaymentPage();
        String orderID = paymentPage.getOrderId();
        Assertion.assertThat("Verify order ID is displayed in payment page", !orderID.isEmpty(), is(true));

        step("User select payment method ");
        paymentPage.clickOnSelectPaymentMethod();
        paymentPage.goToPaymentMethodsGroup(paymentGroup);
        paymentPage.selectPaymentMethod(paymentMethod);

        step("User complete payment");
        paymentPage.clickOnPaymentButton();
        paymentPage.isPaymentSuccessfulVisible();

        step("User open order detail page");
        orderListPage.openActiveTicketViaURL(orderID);
        orderDetailPage.refreshTheWebpage();
        orderDetailPage.skipCoachmark();

        step("User verifies e-ticket is displayed or not");
        Assertion.assertThat("Verifies whether view e-ticket is displayed or not", orderDetailPage.isEticketDisplayed(), is(true));

        step("User verifies departure whoosh details");
        orderDetailPage.checkDepartureTrainDetails(departureWhoosh, originStation, destinationStation);

        step("User verifies departure date");
        Assertion.assertThat("verify departure date", orderDetailPage.verifyDepartureDate(), containsString(depDate));

        step("User verifies the name of adults");
        Assertion.assertThat("verifies contact details of adults", orderDetailPage.verifyAdultName(adultCount, infantCount), is(namesAdult));

        step("User verifies the name of infants");
        Assertion.assertThat("verifies contact details of infants", orderDetailPage.verifyInfantName(adultCount, infantCount), is(namesInfant));

        step("User verifies the ids of adults");
        Assertion.assertThat("verifies ids of adults", orderDetailPage.verifyPassengerIDs(adultCount, infantCount), is(passengerIDs));

        Assertion.assertThat("Verify Manage order container", orderDetailPage.verifyManageOrderContainer(), is(true));
    }

    @Author(names = "Akshat")
    @Module(name = "CompleteBooking")
    @Tribe(name = "Whoosh")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyWhooshBookingForThreeAdultsGuestUser() throws InterruptedException {
        Map<String, String> param = getParam("testVerifyWhooshBookingForThreeAdultsGuestUser");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String originStation = param.get("originStation");
        String destinationStation = param.get("destinationStation");
        String tripType = param.get("tripType");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);
        int daysToReturnDate = Integer.parseInt(param.get("daysToReturnDate"));
        String returnDate = CoreFunctions.addToLongDate(departureDate, daysToReturnDate);
        int adultCount = Integer.parseInt(param.get("adultCount"));
        int infantCount = Integer.parseInt(param.get("infantCount"));
        String fullName = CoreFunctions.randomName();
        String phoneNumber = "91" + CoreFunctions.randomInt(8);
        String emailID = CoreFunctions.randomEmail();
        String paymentMethod = param.get("paymentMethod");
        String paymentGroup = param.get("paymentGroup");


        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        WhooshSearchPageWeb<?> whooshSearchPageWeb = WhooshSearchPageWeb.getInstance(session);
        WhooshSearchResultPageWeb<?> whooshSearchResultPageWeb = WhooshSearchResultPageWeb.getInstance(session);
        WhooshBookingPageWeb<?> whooshBookingPageWeb = WhooshBookingPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as guest");
        loginPage.loginAsGuest();

        step("User open whoosh vertical");
        homePage.openVerticalPageViaURL("whoosh");

        step("User skip pop-up");
        whooshSearchPageWeb.clickSkipPopup();

        step("User select origin station");
        whooshSearchPageWeb.selectOrigin(originStation);

        step("User select destination station");
        whooshSearchPageWeb.selectDestination(destinationStation);

        step("User toggle one way");
        whooshSearchPageWeb.toggleTripType(tripType);

        step("User selects departure and return date");
        whooshSearchPageWeb.selectDate(departureDate, returnDate);

        step("User select passenger");
        whooshSearchPageWeb.selectPassenger(adultCount, infantCount);

        step("User saves the passengers");
        whooshSearchPageWeb.savePassengers();

        step("User search whoosh");
        whooshSearchPageWeb.searchWhoosh();

        step("User verifies if he is on SRP or not");
        Assertion.assertThat("Verify if the user is on SRP or not", whooshSearchResultPageWeb.checkSRP(), is(true));

        step("User skips the filter option");
        whooshSearchResultPageWeb.clickSkip();

        step("User select first Whoosh");
        String departureWhoosh = whooshSearchResultPageWeb.clickFirstWhoosh();

        step("User verifies whether he can select return whoosh or not");
        Assertion.assertThat("Verifies whether user can select return whoosh or not", whooshSearchResultPageWeb.checkReturn(), is(true));

        step("User selects return Whoosh");
        String returnWhoosh = whooshSearchResultPageWeb.clickFirstWhoosh();

        step("User enter contact details for refunds");
        whooshBookingPageWeb.fillContactDetails(fullName, phoneNumber, emailID);

        step("User fill adult details");
        whooshBookingPageWeb.fillPassengerDetailAdult(adultCount);

        step("User fill infant details");
        whooshBookingPageWeb.fillPassengerDetailInfant(adultCount, infantCount);

        step("User continues booking after filling details");
        whooshBookingPageWeb.clickContinue();

        step("User changes and saves the seat");
        whooshBookingPageWeb.saveSeat();

        step("User verifies the departure whoosh");
        Assertion.assertThat("verifies departure whoosh", whooshBookingPageWeb.verifiesDepartureWhoosh(tripType), is(departureWhoosh));

        step("User verifies the return whoosh");
        Assertion.assertThat("verifies return whoosh", whooshBookingPageWeb.verifiesReturnWhoosh(), is(returnWhoosh));

        step("User verifies departure origin station");
        Assertion.assertThat("verifies departure origin station", whooshBookingPageWeb.verifyDepartureOrigin(tripType), is(originStation));

        step("User verifies departure destination station");
        Assertion.assertThat("verifies departure destination station", whooshBookingPageWeb.verifyDepartureDestination(tripType), is(destinationStation));

        step("User verifies return origin station");
        Assertion.assertThat("verifies return origin station", whooshBookingPageWeb.verifyReturnOrigin(), is(destinationStation));

        step("User verifies return destination station");
        Assertion.assertThat("verifies return destination station", whooshBookingPageWeb.verifyReturnDestination(), is(originStation));

        step("User adds insurance");
        whooshBookingPageWeb.addInsurance();

        step("User continues for payment");
        whooshBookingPageWeb.continueToPay();

        step("User Go to Payment Page");
        whooshBookingPageWeb.goToPaymentPage();
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

    @Author(names = {"Akshat", "Vipul Kumar"})
    @Module(name = "CompleteBooking")
    @Tribe(name = "Whoosh")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyWhooshBookingForThreeAdultsAsLoggedInUser() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testVerifyWhooshBookingForThreeAdultsAsLoggedInUser");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String currency = param.get("currency");
        String originStation = param.get("originStation");
        String destinationStation = param.get("destinationStation");
        String tripType = param.get("tripType");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);
        int daysToReturnDate = Integer.parseInt(param.get("daysToReturnDate"));
        String returnDate = CoreFunctions.addToLongDate(departureDate, daysToReturnDate);
        int adultCount = Integer.parseInt(param.get("adultCount"));
        int infantCount = Integer.parseInt(param.get("infantCount"));
        String paymentMethod = param.get("paymentMethod");
        String paymentGroup = param.get("paymentGroup");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        WhooshSearchPageWeb<?> whooshSearchPageWeb = WhooshSearchPageWeb.getInstance(session);
        WhooshSearchResultPageWeb<?> whooshSearchResultPageWeb = WhooshSearchResultPageWeb.getInstance(session);
        WhooshBookingPageWeb<?> whooshBookingPageWeb = WhooshBookingPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open home page as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User select IDR currency");
        homePage.selectCurrency(currency);

        step("User open whoosh vertical");
        homePage.openVerticalPageViaURL("Whoosh");

        step("User skip pop-up");
        whooshSearchPageWeb.clickSkipPopup();

        step("User select origin station");
        whooshSearchPageWeb.selectOrigin(originStation);

        step("User select destination station");
        whooshSearchPageWeb.selectDestination(destinationStation);

        step("User toggle one way");
        whooshSearchPageWeb.toggleTripType(tripType);

        step("User selects departure and return date");
        whooshSearchPageWeb.selectDate(departureDate, returnDate);

        step("User select passenger");
        whooshSearchPageWeb.selectPassenger(adultCount, infantCount);

        step("User saves the passengers");
        whooshSearchPageWeb.savePassengers();

        step("User search whoosh");
        whooshSearchPageWeb.searchWhoosh();

        step("User verifies if he is on SRP or not");
        Assertion.assertThat("Verify if the user is on SRP or not", whooshSearchResultPageWeb.checkSRP(), is(true));

        step("User skips the filter option");
        whooshSearchResultPageWeb.clickSkip();

        step("User select first Whoosh");
        String departureWhoosh = whooshSearchResultPageWeb.clickFirstWhoosh();

        step("User verifies whether he can select return whoosh or not");
        Assertion.assertThat("Verifies whether user can select return whoosh or not", whooshSearchResultPageWeb.checkReturn(), is(true));

        step("User selects return Whoosh");
        String returnWhoosh = whooshSearchResultPageWeb.clickFirstWhoosh();

        step("User fill adult details");
        whooshBookingPageWeb.fillPassengerDetailAdult(adultCount);

        step("User fill infant details");
        whooshBookingPageWeb.fillPassengerDetailInfant(adultCount, infantCount);

        step("User continues booking after filling details");
        whooshBookingPageWeb.clickContinue();

        step("User changes and saves the seat");
        whooshBookingPageWeb.saveSeat();

        step("User verifies the departure whoosh");
        Assertion.assertThat("verifies departure whoosh", whooshBookingPageWeb.verifiesDepartureWhoosh(tripType), is(departureWhoosh));

        step("User verifies the return whoosh");
        Assertion.assertThat("verifies return whoosh", whooshBookingPageWeb.verifiesReturnWhoosh(), is(returnWhoosh));

        step("User verifies departure origin station");
        Assertion.assertThat("verifies departure origin station", whooshBookingPageWeb.verifyDepartureOrigin(tripType), is(originStation));

        step("User verifies departure destination station");
        Assertion.assertThat("verifies departure destination station", whooshBookingPageWeb.verifyDepartureDestination(tripType), is(destinationStation));

        step("User verifies return origin station");
        Assertion.assertThat("verifies return origin station", whooshBookingPageWeb.verifyReturnOrigin(), is(destinationStation));

        step("User verifies return destination station");
        Assertion.assertThat("verifies return destination station", whooshBookingPageWeb.verifyReturnDestination(), is(originStation));

        step("User adds insurance");
        whooshBookingPageWeb.addInsurance();

        step("User continues for payment");
        whooshBookingPageWeb.continueToPay();

        step("User Go to Payment Page");
        whooshBookingPageWeb.goToPaymentPage();
        String orderID = paymentPage.getOrderId();
        Assertion.assertThat("Verify order ID is displayed in payment page", !orderID.isEmpty(), is(true));

        step("User select payment method ");
        paymentPage.clickOnSelectPaymentMethod();
        paymentPage.goToPaymentMethodsGroup(paymentGroup);
        paymentPage.selectPaymentMethod(paymentMethod);

        step("User complete payment");
        paymentPage.clickOnPaymentButton();
        Assertion.assertThat("Payment successful page is visible", paymentPage.isPaymentSuccessfulVisible(), is(true));

        step("User try to open see order list");
        paymentPage.clickSeeOrderListButton();

        step("User open order detail page");
        orderListPage.openActiveTicketViaURL(orderID, "depart");
        orderDetailPage.refreshTheWebpage();
        orderDetailPage.skipCoachmark();

        step("User verifies e-ticket is displayed or not");
        Assertion.assertThat("Verifies whether view e-ticket is displayed or not", orderDetailPage.isEticketDisplayed(), is(true));

        step("User verifies departure whoosh details");
        orderDetailPage.checkDepartureTrainDetails(departureWhoosh, originStation, destinationStation);

        step("User verifies return whoosh details");
        orderDetailPage.checkReturnTrainDetails(returnWhoosh, originStation, destinationStation);

        Assertion.assertThat("Verify Manage order container", orderDetailPage.verifyManageOrderContainer(), is(true));
    }
}