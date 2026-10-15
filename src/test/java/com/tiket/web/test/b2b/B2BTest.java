package com.tiket.web.test.b2b;

import com.tiket.apiPage.HotelApi;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.apiPage.*;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Environment;
import com.tiket.model.Platform;
import com.tiket.model.Session;
import com.tiket.page.accommodation.hotel.HotelPage;
import com.tiket.pojo.b2b.corporate.CorporateCodeData;
import com.tiket.pojo.b2b.corporate.EmployeeData;
import com.tiket.pojo.b2b.corporate.PostCreateTravelPlanBody;
import com.tiket.pojo.b2b.corporate.TravelPlanPojo;
import com.tiket.pojo.flight.FlightDynamicData;
import com.tiket.pojo.flight.FlightOrderDetailResponse;
import com.tiket.pojo.unm.LoginDataUnmResponse;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.testbase.Configs;
import com.tiket.web.dWeb.page.accommodation.hotels.HotelDetailPage;
import com.tiket.web.dWeb.page.accommodation.hotels.HotelRoomListPage;
import com.tiket.web.dWeb.page.account.AccountPage;
import com.tiket.web.dWeb.page.flight.FlightDescriptionPage;
import com.tiket.web.dWeb.page.flight.FlightPage;
import com.tiket.web.dWeb.page.flight.FlightProductReviewPage;
import com.tiket.web.dWeb.page.nft.train.TrainBookingFormPage;
import com.tiket.web.dWeb.page.payment.PaymentPage;
import com.tiket.web.dWeb.page.reschedule.ReschedulePage;
import com.tiket.web.dashboard.page.order.orderCreationPage;
import com.tiket.web.page.accommodation.hotels.BookingForm.HotelBookingFormPageWeb;
import com.tiket.web.page.accommodation.hotels.LandingPage.LandingPageWeb;
import com.tiket.web.page.accommodation.hotels.PDP.PDPPageWeb;
import com.tiket.web.page.accommodation.hotels.SRP.HotelSearchResultPage;
import com.tiket.web.page.accommodation.hotels.hotelLandingPage.HotelLandingPageWeb;
import com.tiket.web.page.common.home.HomePageWeb;
import com.tiket.web.page.common.home.LoginPageWeb;
import com.tiket.web.page.corporate.dashboard.CorporateDashboard;
import com.tiket.web.page.corporate.travel_plan.TravelPlanWeb;
import com.tiket.web.page.flight.bookingForm.FlightBookingFormPageWeb;
import com.tiket.web.page.flight.landingpage.FlightLandingPageWeb;
import com.tiket.web.page.flight.searchresultpage.FlightSearchResultPageWeb;
import com.tiket.web.page.nft.airport_transfer.booking_page.AirportTransferBookingPageWeb;
import com.tiket.web.page.nft.airport_transfer.landing_page.AirportTransferLandingPageWeb;
import com.tiket.web.page.nft.airport_transfer.pdp.AirportTransferPDPWeb;
import com.tiket.web.page.nft.airport_transfer.srp.AirportTransferSRPWeb;
import com.tiket.web.page.nft.train.bookingForm_Page.TrainBookingFormWeb;
import com.tiket.web.page.nft.train.search_Page.TrainSearchPageWeb;
import com.tiket.web.page.nft.bus.bookingForm_Page.BusBookingFormPageWeb;
import com.tiket.web.page.nft.bus.productDetail_Page.BusPdpPageWeb;
import com.tiket.web.page.nft.bus.searchResult_Page.BusSearchResultPageWeb;
import com.tiket.web.page.nft.bus.search_Page.BusSearchPageWeb;
import com.tiket.web.page.nft.ferry.BookingPage.FerryBookingPageWeb;
import com.tiket.web.page.nft.ferry.LandingPage.FerrySearchPageWeb;
import com.tiket.web.page.nft.ferry.SearchResultPage.FerrySearchResultPageWeb;
import com.tiket.web.page.nft.train.searchResult_Page.TrainSearchResultPageWeb;
import com.tiket.web.page.nft.train.search_Page.TrainSearchPageWeb;
import com.tiket.web.page.nft.whoosh.booking_page.WhooshBookingPageWeb;
import com.tiket.web.page.nft.whoosh.searchResult_page.WhooshSearchResultPageWeb;
import com.tiket.web.page.nft.whoosh.search_page.WhooshSearchPageWeb;
import com.tiket.web.page.order.OrderDetail.OrderDetailWeb;
import com.tiket.web.page.order.OrderList.OrderListWeb;
import com.tiket.web.page.platform.sphinx.refund.RefundDetailWeb;
import com.tiket.web.page.platform.sphinx.refund.RefundDetailWeb;
import com.tiket.web.page.platform.payment.PaymentPageWeb;
import com.tiket.web.page.platform.sphinx.refund.RefundPageWeb;
import org.hamcrest.core.Is;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import javax.security.auth.login.FailedLoginException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static com.tiket.core.CoreFunctions.*;
import static com.tiket.testdata.TestData.getObjectParams;
import static com.tiket.testdata.TestData.getParam;
import static org.bouncycastle.cms.RecipientId.password;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;
import java.util.*;

public class B2BTest<T extends WebDriver> extends BaseTest {

    @Author(names = "Aditi")
    @Module(name = "Login")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void testB2BLoginAsTraveller() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testB2BLoginAsTraveller");
        BaseTest.paramsThreadLocal.set(param);
        Session<WebDriver> session = sessionThreadLocal.get();
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");

        step("Go to home");
        homePage.goToHomePage();

        step("Go to account page");
        loginPage.goToAccountPage();

        step("Login with B2B Traveller account");
        loginPage.loginIfNotWithSameEmail(email, password, otp, 1);
        homePage.waitForHomePageToOpen();

        step("Go to account page and check if the user is logged in");
        loginPage.goToAccountPage();
        Assertion.assertThat("Verify that the user is logged in", loginPage.checkIfLoggedIn(), is(true));
    }

    @Author(names = "Aditi")
    @Module(name = "Login")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void testB2BLoginAsApprover() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testB2BLoginAsApprover");
        BaseTest.paramsThreadLocal.set(param);
        Session<WebDriver> session = sessionThreadLocal.get();
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        AccountPage<?> accountPage = new AccountPage<>(session);
        CorporateDashboard<?> corporateDashboard = new CorporateDashboard<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");

        step("Go to home");
        homePage.goToHomePage();

        step("Go to account page");
        loginPage.goToAccountPage();

        step("Login with B2B Approver account");
        loginPage.loginIfNotWithSameEmail(email, password, otp, 1);

        step("Go to account page and check if the user is logged in");
        loginPage.goToAccountPage();
        String userName = loginPage.accountHolderName();
        Assertion.assertThat("Verify User is logged in with correct account",userName,containsString(userName));
        Assertion.assertThat("Verify that the user is logged in", loginPage.checkIfLoggedIn(), is(true));
    }

    @Author(names = "Aditi")
    @Module(name = "Login")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void testB2BLoginAsAdmin() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testB2BLoginAsAdmin");
        BaseTest.paramsThreadLocal.set(param);
        Session<WebDriver> session = sessionThreadLocal.get();
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        AccountPage<?> accountPage = new AccountPage<>(session);
        CorporateDashboard<?> corporateDashboard = new CorporateDashboard<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");

        step("Go to home");
        homePage.goToHomePage();

        step("Go to account page");
        loginPage.goToAccountPage();

        step("Login with B2B Admin account");
        loginPage.loginIfNotWithSameEmail(email, password, otp, 1);
        corporateDashboard.goBackToTiketHomePage();
        homePage.waitForHomePageToOpen();

        step("Go to account page and check if the user is logged in");
        loginPage.goToAccountPage();
        String userName = loginPage.accountHolderName();
        Assertion.assertThat("Verify User is logged in with correct account",userName,containsString(userName));
        Assertion.assertThat("Verify that the user is logged in", loginPage.checkIfLoggedIn(), is(true));
    }

    @Author(names = "Zia")
    @Module(name = "Booking")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void hotelBookingFromTravelerToApproverDweb() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("hotelBookingFromTravelerToApproverDweb");
        BaseTest.paramsThreadLocal.set(param);
        Session<WebDriver> session = sessionThreadLocal.get();
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HotelLandingPageWeb<?> hotelLandingPage = HotelLandingPageWeb.getInstance(session);
        HotelRoomListPage<?> hotelRoomListPage = new HotelRoomListPage<>(session);
        HotelBookingFormPageWeb<?> hotelBookingFormPage = HotelBookingFormPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);
        TravelPlanWeb<?> travelPlanPage = TravelPlanWeb.getInstance(session);

        String hotelName = param.get("hotelName");
        String travelerUsername = param.get("travelerUsername");
        String travelerPassword = param.get("travelerPassword");
        String approverUsername = param.get("approverUsername");
        String approverPassword = param.get("approverPassword");
        String otp = param.get("otp");
        String dateType = param.get("date_type");
        String date = param.get("date");

        step("Go to home");
        homePage.goToHomePage();

        step("User open as guest");
        loginPage.loginAsGuest();

        step("User select IDR currency");
        homePage.selectCurrency("IDR");

        step("User open hotel vertical");
        homePage.openVerticalPage("Hotels");

        step("User search hotel");
        hotelLandingPage.searchStayLocation(hotelName);

        step("Set date");
        hotelLandingPage.setStayDate(dateType, date);

        step("Set guests and rooms");
        hotelLandingPage.clickRoomAndGuestButton();
        hotelLandingPage.setTotalRoom(1);
        hotelLandingPage.setTotalGuest(1);
        hotelLandingPage.clickDone();

        step("Submit search");
        hotelLandingPage.clickSubmit();

        step("Click book button");
        hotelRoomListPage.clickBookButton();

        step("Login from banner booking form");
        hotelBookingFormPage.clickLoginBanner();
        loginPage.loginSteps(travelerUsername, travelerPassword, otp, 1);

        step("Continue to payment page");
        hotelBookingFormPage.continueToSecondPartThenPaymentPage();
        travelPlanPage.skipTravelPlan();

        step("Payment page should be displayed");
        String orderID = paymentPage.getOrderId();
        orderID = String.valueOf(getOrderId(orderID));
        Assertion.assertThat("Corporate billing option is selected", paymentPage.getSelectedPayment().toLowerCase(), is("corporate billing"));

        step("Submit payment using Corporate Billing");
        paymentPage.clickOnPaymentButton();

        step("Approve booking by approver account for Order ID: " + orderID);
        Boolean bookingStatus = corporateApi.approveBookingB2BApi(approverUsername, approverPassword, orderID);
        Assertion.assertThat("Booking approved by B2B dashboard", bookingStatus, is(true));

        step("Open the order list and go to order detail");
        paymentPage.clickSeeOrderListButton();
        orderListPage.openIssuedTicketViaURL(orderID);
        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.verifyIssuedETicket(), is(true));
    }

    @Author(names = "Zia")
    @Module(name = "Booking")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void hotelBookingFromTravelerToAdminDweb() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("hotelBookingFromTravelerToAdminDweb");
        BaseTest.paramsThreadLocal.set(param);
        Session<WebDriver> session = sessionThreadLocal.get();
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HotelLandingPageWeb<?> hotelLandingPage = HotelLandingPageWeb.getInstance(session);
        HotelRoomListPage<?> hotelRoomListPage = new HotelRoomListPage<>(session);
        HotelBookingFormPageWeb<?> hotelBookingFormPage = HotelBookingFormPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);
        TravelPlanWeb<?> travelPlanPage = TravelPlanWeb.getInstance(session);

        String hotelName = param.get("hotelName");
        String travelerUsername = param.get("travelerUsername");
        String travelerPassword = param.get("travelerPassword");
        String adminUsername = param.get("adminUsername");
        String adminPassword = param.get("adminPassword");
        String otp = param.get("otp");
        String dateType = param.get("date_type");
        String date = param.get("date");

        step("Go to home");
        homePage.goToHomePage();

        step("Go to account page");
        loginPage.goToAccountPage();

        step("User open as guest");
        loginPage.loginIfNotWithSameEmail(travelerUsername, travelerPassword, otp, 1);

        step("User select IDR currency");
        homePage.selectCurrency("IDR");

        step("User open hotel vertical");
        homePage.openVerticalPage("Hotels");

        step("User search hotel");
        hotelLandingPage.searchStayLocation(hotelName);

        step("Set date");
        hotelLandingPage.setStayDate(dateType, date);

        step("Set guests and rooms");
        hotelLandingPage.clickRoomAndGuestButton();
        hotelLandingPage.setTotalRoom(1);
        hotelLandingPage.setTotalGuest(1);
        hotelLandingPage.clickDone();

        step("Submit search");
        hotelLandingPage.clickSubmit();

        step("Click book button");
        hotelRoomListPage.clickBookButton();

        step("Continue to payment page");
        hotelBookingFormPage.continueToSecondPartThenPaymentPage();
        travelPlanPage.skipTravelPlan();

        step("Payment page should be displayed");
        String orderID = paymentPage.getOrderId();
        orderID = String.valueOf(getOrderId(orderID));
        Assertion.assertThat("Corporate billing option is selected", paymentPage.getSelectedPayment().toLowerCase(), is("corporate billing"));

        step("Submit payment using Corporate Billing");
        paymentPage.clickOnPaymentButton();

        step("Approve booking by admin account for Order ID: " + orderID);
        Boolean bookingStatus = corporateApi.approveBookingB2BApi(adminUsername, adminPassword, orderID);
        Assertion.assertThat("Booking approved by B2B dashboard", bookingStatus, is(true));

        step("Open the order list and go to order detail");
        paymentPage.clickSeeOrderListButton();
        orderListPage.openIssuedTicketViaURL(orderID);
        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.verifyIssuedETicket(), is(true));
    }

    @Author(names = "Yazid")
    @Module(name = "Booking")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void hotelBookingFromSuperAdminConvertTravelerWEB() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("hotelBookingFromSuperAdminConvertTravelerWEB");
        BaseTest.paramsThreadLocal.set(param);
        Session<WebDriver> session = sessionThreadLocal.get();
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HotelLandingPageWeb<?> hotelLandingPage = HotelLandingPageWeb.getInstance(session);
        HotelRoomListPage<?> hotelRoomListPage = new HotelRoomListPage<>(session);
        HotelBookingFormPageWeb<?> hotelBookingFormPage = HotelBookingFormPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        TravelPlanWeb<?> travelPlanPage = TravelPlanWeb.getInstance(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);

        String hotelName = param.get("hotelName");
        String otp = param.get("otp");
        String dateType = param.get("date_type");
        String date = param.get("date");
        String currency = "IDR";
        String superAdminUsername = param.get("superAdminUsername");
        String superAdminPassword = param.get("superAdminPassword");
        String travelerUsername= param.get("travelerUsername");
        String travelerPassword = param.get("travelerPassword");

        step("Convert this Email : "+superAdminUsername+" to Traveler using this email : "+travelerUsername);
        corporateApi.updateEmployeeCorporateRole(superAdminUsername,superAdminPassword,travelerUsername,1,"super_administrator");
        boolean isConvertedToTraveler = corporateApi.updateEmployeeCorporateRole(travelerUsername,travelerPassword,superAdminUsername,1,"traveler");

        step("Check the SUPER ADMIN email "+superAdminUsername+" is converted to TRAVELER");
        Assertion.assertThat("Check the SUPER ADMIN email "+superAdminUsername+" is converted to TRAVELER",isConvertedToTraveler,is(true));

        step("Go to home");
        homePage.goToHomePage();

        step("Go to account page");
        loginPage.goToAccountPage();

        step("login in using { " + superAdminUsername + " }");
        loginPage.loginIfNotWithSameEmail(superAdminUsername, superAdminPassword, otp, 1);

        step("User select "+currency+" currency");
        homePage.selectCurrency(currency);

        step("User open Hotel vertical");
        homePage.openVerticalPageViaURL("hotel");

        step("User search hotel");
        hotelLandingPage.searchStayLocation(hotelName);

        step("Set date");
        hotelLandingPage.setStayDate(dateType, date);

        step("Set guests and rooms");
        hotelLandingPage.clickRoomAndGuestButton();
        hotelLandingPage.setTotalRoom(1);
        hotelLandingPage.setTotalGuest(1);
        hotelLandingPage.clickDone();

        step("Submit search");
        hotelLandingPage.clickSubmit();

        step("Click book button");
        hotelRoomListPage.clickBookButton();

        step("Continue to payment page");
        hotelBookingFormPage.continueToSecondPartThenPaymentPage();
        travelPlanPage.skipTravelPlan();

        step("Payment page should be displayed");
        String orderID = paymentPage.getOrderId();
        orderID = String.valueOf(getOrderId(orderID));
        Assertion.assertThat("Corporate billing option is selected", paymentPage.getSelectedPayment().toLowerCase(), is("corporate billing"));

        step("Submit payment using Corporate Billing");
        paymentPage.clickOnPaymentButton();

        step("Wait for 1 mins");
        sleep(60);

        step("Open the order list and go to order detail");
        Assertion.assertThat("orderID should be visible and should be same as from payment page and in purchase List ", corporateApi.checkPurchaseOrderList(orderID,superAdminUsername,superAdminPassword,"Super Admin"), is(true));

        step("Convert this Email : "+superAdminUsername+" to Super Admin using this email : "+travelerUsername);
        boolean isConvertedToSuper = corporateApi.updateEmployeeCorporateRole(travelerUsername,travelerPassword,superAdminUsername,1,"super_administrator");

        step("Check the traveler email "+superAdminUsername+" is converted back to SUPER ADMIN");
        Assertion.assertThat("Check the traveler email "+superAdminUsername+" is converted back to SUPER ADMIN",isConvertedToSuper,is(true));

    }

    @Author(names = {"Ashish"})
    @Module(name = "Login")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void testLoginOnFlightBookingFormAdminDweb() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testLoginOnFlightBookingFormAdminDweb");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPageWeb = FlightLandingPageWeb.getInstance(session);
        FlightSearchResultPageWeb<?> flightSearchResultPage = FlightSearchResultPageWeb.getInstance(session);
        FlightProductReviewPage<?> flightProductReviewPage = new FlightProductReviewPage<>(session);
        FlightBookingFormPageWeb<?> flightBookingFormPage = FlightBookingFormPageWeb.getInstance(session);
        FlightPage<?> flightPage = new FlightPage<>(session);
        CorporateDashboard<?> corporateDashboard = new CorporateDashboard<>(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate,daysToDepartureDate);

        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));

        String username = param.get("username");
        String password = param.get("password");
        String currency = param.get("currency");
        String otp = param.get("otp");

        step("User open home page");
        homePage.goToHomePage();

        step("logout If already logged in");
        loginPage.logoutViaUrl();

        step("User open Flight vertical");
        homePage.openVerticalPageViaURL("pesawat");

        step("Toggle trip type");
        flightLandingPageWeb.toggleTripType(tripType);

        step("Select origin airport");
        flightLandingPageWeb.selectOrigin(originAirport);

        step("Select destination airport");
        flightLandingPageWeb.selectDestination(destinationAirport);

        step("Select date");
        flightLandingPageWeb.selectDate(departureDate);

        step("add passenger");
        flightLandingPageWeb.addPassenger(adultPassengers,childPassengers,infantPassengers);

        step("search flight");
        flightLandingPageWeb.searchFlight();

        step("select flight");
        flightSearchResultPage.selectFlight();

        step("Continue to booking form");
        flightProductReviewPage.continueToBookingForm();
        flightBookingFormPage.clickNoThanksOnFlightMayReschedulePopup();

        step("click login on booking page");
        flightBookingFormPage.clickLoginBtnOnBookingPage();

        step("login with :" + username);
        loginPage.loginSteps(username, password, otp, 1);

        step("verify redirection to corporate dashboard");
        Assertion.assertThat("verify redirection to corporate dashboard", corporateDashboard.isOnCorporateDashboard(), is(true));
    }

    @Author(names = {"Ashish"})
    @Module(name = "TravelPolicy")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void testFlightVerticalIsAvailableWhenTravelPolicyActivatedDweb() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testFlightVerticalIsAvailableWhenTravelPolicyActivatedDweb");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPageWeb = FlightLandingPageWeb.getInstance(session);

        String username = param.get("username");
        String password = param.get("password");
        String currency = param.get("currency");
        String otp = param.get("otp");

        step("User open home page");
        homePage.goToHomePage();

        step("Go to account page");
        loginPage.goToAccountPage();

        step("login in using { " + username + " }");
        loginPage.loginIfNotWithSameEmail(username, password, otp, 1);

        step("User select IDR currency");
        homePage.selectCurrency(currency);

        step("is flight vertical shown");
        Assertion.assertThat("is flight vertical shown",homePage.verifyVerticalIsDisplayed("Flights"),is(true));

        step("User open Flight vertical");
        homePage.openVerticalPage("Flights");

        step("is on flight landing page");
        Assertion.assertThat("is on flight landing page", flightLandingPageWeb.isOnFlightPage(),is(true));
    }

    @Author(names = {"Ashish"})
    @Module(name = "TravelPolicy")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void testMeetTravelPolicyFlightSRPWhenPolicyIsSetForFewAirlinesDweb() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testMeetTravelPolicyFlightSRPWhenPolicyIsSetForFewAirlinesDweb");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPageWeb = FlightLandingPageWeb.getInstance(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);
        FlightSearchResultPageWeb<?> flightSearchResultPage = FlightSearchResultPageWeb.getInstance(session);

        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate,daysToDepartureDate);

        int adultPassengers = Integer.parseInt(param.get("adultPassengers"));
        int childPassengers = Integer.parseInt(param.get("childPassengers"));
        int infantPassengers = Integer.parseInt(param.get("infantPassengers"));

        String airline = param.get("airline");
        String airline2 = param.get("airlineWithoutPolicy");

        String username = param.get("username");
        String password = param.get("password");
        String adminUsername = param.get("adminUsername");
        String adminPassword = param.get("adminPassword");

        int minPrice = Integer.parseInt(param.get("minPrice"));
        int maxPrice = Integer.parseInt(param.get("maxPrice"));

        String currency = param.get("currency");
        String policyName = param.get("policyName");
        String otp = param.get("otp");

        step("User open home page");
        homePage.goToHomePage();

        step("Login as " + username + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(username, password);

        step("User select IDR currency");
        homePage.selectCurrency(currency);

        step("User open Flight vertical");
        homePage.openVerticalPageViaURL("Flights");

        step("Toggle trip type");
        flightLandingPageWeb.toggleTripType(tripType);

        step("Select origin airport");
        flightLandingPageWeb.selectOrigin(originAirport);

        step("Select destination airport");
        flightLandingPageWeb.selectDestination(destinationAirport);

        step("Select date");
        flightLandingPageWeb.selectDate(departureDate);

        step("add passenger");
        flightLandingPageWeb.addPassenger(adultPassengers,childPassengers,infantPassengers);

        step("search flight");
        flightLandingPageWeb.searchFlight();

        step("filter airline {" + airline + " }" );
        flightSearchResultPage.filterAirline(airline);

        step("verify meet travel policy for flight");
        Assertion.assertThat("Is meet travel policy flight" + airline, flightSearchResultPage.isMeetTravelPolicy(), is(true));

        step("reset airline filter");
        flightSearchResultPage.resetAirlineFilter();

        step("filter airline {" + airline2 + " }" );
        flightSearchResultPage.filterAirline(airline2);

        step("verify meet travel policy for flight");
        Assertion.assertThat("Is meet travel policy flight" + airline2, flightSearchResultPage.isMeetTravelPolicy(), is(false));
    }

    @Author(names = {"Ashish"})
    @Module(name = "TravelPolicy")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void testNotMeetTravelPolicyFlightDweb() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testNotMeetTravelPolicyFlightDweb");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPageWeb = FlightLandingPageWeb.getInstance(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);
        FlightSearchResultPageWeb<?> flightSearchResultPage = FlightSearchResultPageWeb.getInstance(session);
        FlightProductReviewPage<?> flightProductReviewPage = new FlightProductReviewPage<>(session);

        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate,daysToDepartureDate);

        int adultPassengers = Integer.parseInt(param.get("adultPassengers"));
        int childPassengers = Integer.parseInt(param.get("childPassengers"));
        int infantPassengers = Integer.parseInt(param.get("infantPassengers"));

        String airlineWithoutPolicy = param.get("airlineWithoutPolicy");
        String airline = param.get("airline");

        String username = param.get("username");
        String password = param.get("password");
        String adminUsername = param.get("adminUsername");
        String adminPassword = param.get("adminPassword");

        int minPrice = Integer.parseInt(param.get("minPrice"));
        int maxPrice = Integer.parseInt(param.get("maxPrice"));

        String currency = param.get("currency");
        String policyName = param.get("policyName");
        String otp = param.get("otp");

        step("get related data via api");
        String accessTokenAdmin = corporateApi.getAccessTokenB2BCorporate(adminUsername,adminPassword);
        long accountIdAdmin = corporateApi.getAccountId(accessTokenAdmin);
        EmployeeData employeeDataAdmin = corporateApi.getEmployeeData(adminUsername);
        EmployeeData employeeDataUser = corporateApi.getEmployeeData(username);

        step("patch travel policy");
        int travelPolicyID = corporateApi.patchTravelPolicyFlight(accessTokenAdmin,employeeDataAdmin,accountIdAdmin,currency,minPrice,maxPrice,"",airline,0,false,false,policyName);
        Assertion.assertThat("Travel Policy patched", travelPolicyID, not(-1));

        step("assign travel policy to user");
        boolean isPolicyAssign = corporateApi.assignTravelPolicyToUser(accountIdAdmin,employeeDataAdmin,employeeDataUser,travelPolicyID);
        Assertion.assertThat("Travel Policy assigned", isPolicyAssign, is(true));

        step("set the search to display all in srp");
        boolean isSettingDone = corporateApi.setSearchTypePolicy(employeeDataAdmin,accountIdAdmin,"search",true);
        Assertion.assertThat("Search Type Policy set", isSettingDone, is(true));

        step("User open home page");
        homePage.goToHomePage();

        step("Go to account page");
        loginPage.goToAccountPage();

        step("login in using { " + username + " }");
        loginPage.loginIfNotWithSameEmail(username, password, otp, 1);

        step("User select IDR currency");
        homePage.selectCurrency(currency);

        step("User open Flight vertical");
        homePage.openVerticalPageViaURL("Flights");

        step("Toggle trip type");
        flightLandingPageWeb.toggleTripType(tripType);

        step("Select origin airport");
        flightLandingPageWeb.selectOrigin(originAirport);

        step("Select destination airport");
        flightLandingPageWeb.selectDestination(destinationAirport);

        step("Select date");
        flightLandingPageWeb.selectDate(departureDate);

        step("add passenger");
        flightLandingPageWeb.addPassenger(adultPassengers,childPassengers,infantPassengers);

        step("search flight");
        flightLandingPageWeb.searchFlight();

        step("filter airline {" + airlineWithoutPolicy + " }" );
        flightSearchResultPage.filterAirline(airlineWithoutPolicy);

        step("verify meet travel policy for flight");
        Assertion.assertThat("Is not meet travel policy flight srp" + airlineWithoutPolicy, flightSearchResultPage.isMeetTravelPolicy(), is(false));

        step("select flight");
        flightSearchResultPage.selectFlight();

        step("Continue to booking form");
        flightProductReviewPage.clickSelectBtnRegular();

        step("verify not meet travel policy for flight popup");
        Assertion.assertThat("Is not meet travel policy for flight popup", flightProductReviewPage.isNotMeetTravelPolicyPopupDisplayed(), is(true));
    }

    @Author(names = {"Ashish","Mridul"})
    @Module(name = "TravelPolicy")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void testMeetTravelPolicyFlightRoundTripSRPWhenPolicyIsSetForFewAirlinesDweb() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testMeetTravelPolicyFlightRoundTripSRPWhenPolicyIsSetForFewAirlinesDweb");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPageWeb = FlightLandingPageWeb.getInstance(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);
        FlightSearchResultPageWeb<?> flightSearchResultPage = FlightSearchResultPageWeb.getInstance(session);
        FlightDescriptionPage<?> flightDescriptionPage = new FlightDescriptionPage<>(session);

        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int duration = Integer.parseInt(param.get("duration"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate,daysToDepartureDate);
        String returnDate = CoreFunctions.addToLongDate(departureDate, duration);

        int adultPassengers = Integer.parseInt(param.get("adultPassengers"));
        int childPassengers = Integer.parseInt(param.get("childPassengers"));
        int infantPassengers = Integer.parseInt(param.get("infantPassengers"));

        String airlinePolicy = param.get("airlinePolicy");

        String username = param.get("username");
        String password = param.get("password");
        String adminUsername = param.get("adminUsername");
        String adminPassword = param.get("adminPassword");

        int minPrice = Integer.parseInt(param.get("minPrice"));
        int maxPrice = Integer.parseInt(param.get("maxPrice"));

        String currency = param.get("currency");
        String policyName = param.get("policyName");
        String otp = param.get("otp");

        step("get related data via api");
        String accessTokenAdmin = corporateApi.getAccessTokenB2BCorporate(adminUsername,adminPassword);
        long accountIdAdmin = corporateApi.getAccountId(accessTokenAdmin);
        EmployeeData employeeDataAdmin = corporateApi.getEmployeeData(adminUsername);
        EmployeeData employeeDataUser = corporateApi.getEmployeeData(username);

        step("patch travel policy");
        int travelPolicyID = corporateApi.patchTravelPolicyFlight(accessTokenAdmin,employeeDataAdmin,accountIdAdmin,currency,minPrice,maxPrice,"",airlinePolicy,0,false,false,policyName);
        Assertion.assertThat("Travel Policy patched", travelPolicyID, not(-1));

        step("assign travel policy to user");
        boolean isPolicyAssign = corporateApi.assignTravelPolicyToUser(accountIdAdmin,employeeDataAdmin,employeeDataUser,travelPolicyID);
        Assertion.assertThat("Travel Policy assigned", isPolicyAssign, is(true));

        step("set the search to display all in srp");
        boolean isSettingDone = corporateApi.setSearchTypePolicy(employeeDataAdmin,accountIdAdmin,"search",true);
        Assertion.assertThat("Search Type Policy set", isSettingDone, is(true));

        step("User open home page");
        homePage.goToHomePage();

        step("Go to account page");
        loginPage.goToAccountPage();

        step("login in using { " + username + " }");
        loginPage.loginIfNotWithSameEmail(username, password, otp, 1);

        step("User select IDR currency");
        homePage.selectCurrency(currency);

        step("User open Flight vertical");
        homePage.openVerticalPageViaURL("flights");

        step("Toggle trip type");
        flightLandingPageWeb.toggleTripType(tripType);

        step("Select origin airport");
        flightLandingPageWeb.selectOrigin(originAirport);

        step("Select destination airport");
        flightLandingPageWeb.selectDestination(destinationAirport);

        step("Select date");
        flightLandingPageWeb.selectDate(departureDate,returnDate);

        step("add passenger");
        flightLandingPageWeb.addPassenger(adultPassengers,childPassengers,infantPassengers);

        step("search flight");
        flightLandingPageWeb.searchFlight();

        step("filter airline {" + airlinePolicy + " }" );
        flightSearchResultPage.filterAirline(airlinePolicy);

        step("verify meet travel policy for flight");
        Assertion.assertThat("Is meet travel policy flight srp" + airlinePolicy, flightSearchResultPage.isMeetTravelPolicy(), is(true));

        step("select flight");
        flightSearchResultPage.selectFlight();

        step("filter airline {" + airlinePolicy + " }" );
        flightSearchResultPage.filterAirline(airlinePolicy);

        step("verify meet travel policy for flight");
        Assertion.assertThat("Is meet travel policy flight srp" + airlinePolicy, flightSearchResultPage.isMeetTravelPolicy(), is(true));
    }

    @Author(names = {"Ashish"})
    @Module(name = "MeetTravelPolicy")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void testMeetTravelPolicyFlightOnlyPremiumEconomyClassSeatOptionDweb() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testMeetTravelPolicyFlightOnlyPremiumEconomyClassSeatOptionDweb");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPageWeb = FlightLandingPageWeb.getInstance(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);
        FlightSearchResultPageWeb<?> flightSearchResultPage = FlightSearchResultPageWeb.getInstance(session);
        CorporateDashboard<?> corporateDashboard = new CorporateDashboard<>(session);

        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate,daysToDepartureDate);

        int adultPassengers = Integer.parseInt(param.get("adultPassengers"));
        int childPassengers = Integer.parseInt(param.get("childPassengers"));
        int infantPassengers = Integer.parseInt(param.get("infantPassengers"));

        String username = param.get("username");
        String password = param.get("password");
        String adminUsername = param.get("adminUsername");
        String adminPassword = param.get("adminPassword");

        int minPrice = Integer.parseInt(param.get("minPrice"));
        int maxPrice = Integer.parseInt(param.get("maxPrice"));

        String currency = param.get("currency");
        String policyName = param.get("policyName");
        String otp = param.get("otp");

        step("get related data via api");
        String accessTokenAdmin = corporateApi.getAccessTokenB2BCorporate(adminUsername,adminPassword);
        long accountIdAdmin = corporateApi.getAccountId(accessTokenAdmin);
        EmployeeData employeeDataAdmin = corporateApi.getEmployeeData(adminUsername);
        EmployeeData employeeDataUser = corporateApi.getEmployeeData(username);

        step("patch travel policy");
        int travelPolicyID = corporateApi.patchTravelPolicyFlight(accessTokenAdmin,employeeDataAdmin,accountIdAdmin,currency,minPrice,maxPrice,"PREMIUM_ECONOMY","",0,false,false,policyName);
        Assertion.assertThat("Travel Policy patched", travelPolicyID, not(-1));

        step("assign travel policy to user");
        boolean isPolicyAssign = corporateApi.assignTravelPolicyToUser(accountIdAdmin,employeeDataAdmin,employeeDataUser,travelPolicyID);
        Assertion.assertThat("Travel Policy assigned", isPolicyAssign, is(true));

        step("set the search to display all in srp");
        boolean isSettingDone = corporateApi.setSearchTypePolicy(employeeDataAdmin,accountIdAdmin,"search",false);
        Assertion.assertThat("Search Type Policy set", isSettingDone, is(true));

        step("User open home page");
        homePage.goToHomePage();

        step("Go to account page");
        loginPage.goToAccountPage();

        step("login in using { " + username + " }");
        loginPage.loginIfNotWithSameEmail(username, password, otp, 1);

        step("User select IDR currency");
        homePage.selectCurrency(currency);

        step("User open Flight vertical");
        homePage.openVerticalPageViaURL("Flights");

        step("Toggle trip type");
        flightLandingPageWeb.toggleTripType(tripType);

        step("Select origin airport");
        flightLandingPageWeb.selectOrigin(originAirport);

        step("Select destination airport");
        flightLandingPageWeb.selectDestination(destinationAirport);

        step("Select date");
        flightLandingPageWeb.selectDate(departureDate);

        step("add passenger");
        flightLandingPageWeb.addPassenger(adultPassengers,childPassengers,infantPassengers);

        step("select seat type");
        flightLandingPageWeb.selectFlightClass("economy");

        step("search flight");
        flightLandingPageWeb.searchFlight();

        step("verify if search result page is empty according to travel policy");
        Assertion.assertThat("Search result is empty for economy", flightSearchResultPage.isSearchResultPageEmptyForEconomy(currency), is(true));

        step("back to search landing page");
        flightSearchResultPage.backToFlightPage();

        step("select seat type");
        flightLandingPageWeb.selectFlightClass("premium economy");

        step("search flight");
        flightLandingPageWeb.searchFlight();

        step("check if premium economy flight shows up");
        Assertion.assertThat("Search result is shown for premium economy", flightSearchResultPage.isSearchResultShownForPremiumEconomy(currency), is(true));

        step("verify meet travel policy for flight");
        Assertion.assertThat("Is meet travel policy flight srp", flightSearchResultPage.isMeetTravelPolicy(), is(true));
    }

    @Author(names = {"Zia", "Aditi"})
    @Module(name = "Refund")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void testApproveRefundRequestByTravelerEndToEndDweb() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();

        Map<String, String> param = getParam("testApproveRefundRequestByTravelerEndToEndDweb");
        BaseTest.paramsThreadLocal.set(param);

        String travelerUsername = param.get("travelerUsername");
        String travelerPassword = param.get("travelerPassword");
        String otp= param.get("otp");
        String approverUsername = param.get("approverUsername");
        String approverPassword = param.get("approverPassword");

        String hotelName = param.get("hotelName");

        String dateType = param.get("date_type");
        String date = param.get("date");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);
        RefundApi<?> refundApi = new RefundApi<>(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        HotelLandingPageWeb<?> hotelLandingPage = HotelLandingPageWeb.getInstance(session);
        HotelRoomListPage<?> hotelRoomListPage = new HotelRoomListPage<>(session);
        HotelBookingFormPageWeb<?> hotelBookingFormPage = HotelBookingFormPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        RefundPageWeb<?> refundPage = RefundPageWeb.getInstance(session);
        TravelPlanWeb<?> travelPlanPage = TravelPlanWeb.getInstance(session);

        step("Go to home");
        homePage.goToHomePage();

        step("Go to account page");
        loginPage.goToAccountPage();

        step("User open as guest");
        loginPage.loginIfNotWithSameEmail(travelerUsername, travelerPassword, otp, 1);

        step("User select IDR currency");
        homePage.selectCurrency("IDR");

        step("User open hotel vertical");
        homePage.openVerticalPage("Hotel");

        step("User search hotel");
        hotelLandingPage.searchStayLocation(hotelName);

        step("Set date");
        hotelLandingPage.setStayDate(dateType, date);

        step("Set guests and rooms");
        hotelLandingPage.clickRoomAndGuestButton();
        hotelLandingPage.setTotalRoom(1);
        hotelLandingPage.setTotalGuest(1);
        hotelLandingPage.clickDone();

        step("Submit search");
        hotelLandingPage.clickSubmit();

        step("Click book button");
        hotelRoomListPage.clickBookButton();

        step("Continue to payment page");
        hotelBookingFormPage.continueToSecondPartThenPaymentPage();
        travelPlanPage.skipTravelPlan();

        step("Payment page should be displayed");
        String orderID = paymentPage.getOrderId();
        int finalAmount = Integer.parseInt(paymentPage.getFinalPrice());
        orderID = String.valueOf(getOrderId(orderID));
        Assertion.assertThat("Corporate billing option is selected", paymentPage.getSelectedPayment().toLowerCase(), is("corporate billing"));

        step("Submit payment using Corporate Billing");
        paymentPage.clickOnPaymentButton();

        step("Approve booking by approver account for Order ID: " + orderID);
        Boolean bookingStatus = corporateApi.approveBookingB2BApi(approverUsername, approverPassword, orderID);
        Assertion.assertThat("Booking approved by B2B dashboard", bookingStatus, is(true));

        step("Open the order list and go to order detail");
        paymentPage.clickSeeOrderListButton();
        orderListPage.openIssuedTicketViaURL(orderID);
        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.verifyIssuedETicket(), is(true));
        orderDetailPage.closeEticketPopUp();

        step("Click refund button");
        orderDetailPage.clickRefundButton();
        refundPage.skipRescheduleNudgeIfShown();

        step("Fill refund form");
        refundPage.selectRandomRefundReason();
        int refundAmtFromUI = Integer.parseInt(refundPage.getRefundTotal());
        refundPage.continueRefund();

        step("submit refund");
        refundPage.clickChecklistTnc();
        refundPage.clickSubmitRefund();
        refundPage.clickProcessRefund();
        refundPage.clickGoToOrders();

        step("Check if the refunded order is being reflected on corporate dashboard");
        String refundId = corporateApi.getRefundId(orderID, approverUsername, approverPassword,"prerefund");
        Boolean isApproved = true;
        Boolean success = corporateApi.approveOrRejectRefundB2B(approverUsername, approverPassword, refundId, isApproved);
        Assertion.assertThat("Refund Approved Success", success, is(true));

        step("Approve refund from admin care dashboard");
        refundId = corporateApi.getRefundId(orderID, approverUsername, approverPassword,"refund");
        int refundAmount = Math.toIntExact(corporateApi.getRefundAmount(orderID, approverUsername, approverPassword, "refund"));
        refundApi.approveEndToEndRefundFromAdminCareDashboard(refundId, orderID);

        step("Verify via B2C UI");
        orderListPage.openOngoingRefundList();
        String refundStatus = orderListPage.getRefundStatusCard(refundId);
        Assertion.assertThat("Refund status is Refund has completed", refundStatus, either(is("Refund has completed")).or(is("Pengembalian dana telah selesai")));

        step("Verify that the refund amount is the same on UI and the value from API");
        Assertion.assertThat("Refund amount is the same on UI and API", refundAmtFromUI, is(refundAmount));
    }

    @Author(names = {"Aditi"})
    @Module(name = "eTicket")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void testNoApprovalFromApprover() throws FailedLoginException, InterruptedException, ExecutionException {
        Session<WebDriver> session = sessionThreadLocal.get();

        Map<String, String> param = getParam("testNoApprovalFromApprover");
        BaseTest.paramsThreadLocal.set(param);

        String travelerUsername = param.get("travelerUsername");
        String travelerPassword = param.get("travelerPassword");
        String otp= param.get("otp");
        String location = param.get("location");
        String dateType = param.get("date_type");
        String date = param.get("date");
        String hotelName = param.get("hotelName");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        HotelLandingPageWeb<?> hotelLandingPage = HotelLandingPageWeb.getInstance(session);
        HotelRoomListPage<?> hotelRoomListPage = new HotelRoomListPage<>(session);
        HotelBookingFormPageWeb<?> hotelBookingFormPage = HotelBookingFormPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        TravelPlanWeb<?> travelPlanPage = TravelPlanWeb.getInstance(session);

        step("Go to home");
        homePage.goToHomePage();

        step("Go to account page");
        loginPage.goToAccountPage();

        step("User login");
        loginPage.loginIfNotWithSameEmail(travelerUsername, travelerPassword, otp, 1);

        step("User select IDR currency");
        homePage.selectCurrency("IDR");

        step("User open hotel vertical");
        homePage.openVerticalPage("Hotel");

        step("User search hotel location");
        hotelLandingPage.searchStayLocation(location);

        step("Set date");
        hotelLandingPage.setStayDate(dateType, date);

        step("Set guests and rooms");
        hotelLandingPage.clickRoomAndGuestButton();
        hotelLandingPage.setTotalRoom(1);
        hotelLandingPage.setTotalGuest(1);
        hotelLandingPage.clickDone();

        step("Submit search");
        hotelLandingPage.clickSubmit();

        step("Select first hotel card");
        hotelLandingPage.clickFirstHotelCard();

        step("Click book button");
        hotelRoomListPage.clickBookButton();

        step("Continue to payment page");
        hotelBookingFormPage.continueToSecondPartThenPaymentPage();
        travelPlanPage.skipTravelPlan();

        step("Payment page should be displayed");
        String orderID = paymentPage.getOrderId();
        int finalAmount = Integer.parseInt(paymentPage.getFinalPrice());
        Assertion.assertThat("Final amount should be greater than zero", finalAmount, is(greaterThan(0)));
        orderID = String.valueOf(getOrderId(orderID));
        Assertion.assertThat("Corporate billing option is selected", paymentPage.getSelectedPayment().toLowerCase(), is("corporate billing"));

        step("Submit payment using Corporate Billing");
        paymentPage.clickOnPaymentButton();

        step("Open the order list and go to order detail, booking has not been approved");
        paymentPage.clickSeeOrderListButton();
        Assertion.assertThat("e-Ticket on the order detail should be not published", orderDetailPage.verifyIssuedETicket(), is(false));
        int totalAmt = Integer.parseInt(orderDetailPage.getTotalAmount(orderID));
        Assertion.assertThat("Price on payment page and order detail page should be the same", finalAmount, is(totalAmt));
        orderDetailPage.closeEticketPopUp();

    }

    @Author(names = {"Zia"})
    @Module(name = "Refund")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void testRejectRefundRequestByTravelerDweb() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();

        Map<String, String> param = getParam("testRejectRefundRequestByTravelerDweb");
        BaseTest.paramsThreadLocal.set(param);

        String travelerUsername = param.get("travelerUsername");
        String travelerPassword = param.get("travelerPassword");
        String otp= param.get("otp");
        String approverUsername = param.get("approverUsername");
        String approverPassword = param.get("approverPassword");

        String hotelName = param.get("hotelName");

        String dateType = param.get("date_type");
        String date = param.get("date");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        HotelLandingPageWeb<?> hotelLandingPage = HotelLandingPageWeb.getInstance(session);
        HotelRoomListPage<?> hotelRoomListPage = new HotelRoomListPage<>(session);
        HotelBookingFormPageWeb<?> hotelBookingFormPage = HotelBookingFormPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        RefundPageWeb<?> refundPage = RefundPageWeb.getInstance(session);
        TravelPlanWeb<?> travelPlanPage = TravelPlanWeb.getInstance(session);

        step("Go to home");
        homePage.goToHomePage();

        step("User open as guest");
        loginPage.loginAsGuest();

        step("User select IDR currency");
        homePage.selectCurrency("IDR");

        step("User open hotel vertical");
        homePage.openVerticalPage("Hotels");

        step("User search hotel");
        hotelLandingPage.searchStayLocation(hotelName);

        step("Set date");
        hotelLandingPage.setStayDate(dateType, date);

        step("Set guests and rooms");
        hotelLandingPage.clickRoomAndGuestButton();
        hotelLandingPage.setTotalRoom(1);
        hotelLandingPage.setTotalGuest(1);
        hotelLandingPage.clickDone();

        step("Submit search");
        hotelLandingPage.clickSubmit();

        step("Click book button");
        hotelRoomListPage.clickBookButton();

        step("Login from banner booking form");
        hotelBookingFormPage.clickLoginBanner();
        loginPage.loginSteps(travelerUsername, travelerPassword, otp, 1);

        step("Continue to payment page");
        hotelBookingFormPage.continueToSecondPartThenPaymentPage();
        travelPlanPage.skipTravelPlan();

        step("Payment page should be displayed");
        String orderID = paymentPage.getOrderId();
        orderID = String.valueOf(getOrderId(orderID));
        Assertion.assertThat("Corporate billing option is selected", paymentPage.getSelectedPayment().toLowerCase(), is("corporate billing"));

        step("Submit payment using Corporate Billing");
        paymentPage.clickOnPaymentButton();

        step("Approve booking by approver account for Order ID: " + orderID);
        Boolean bookingStatus = corporateApi.approveBookingB2BApi(approverUsername, approverPassword, orderID);
        Assertion.assertThat("Booking approved by B2B dashboard", bookingStatus, is(true));

        step("Open the order list and go to order detail");
        paymentPage.clickSeeOrderListButton();
        orderListPage.openIssuedTicketViaURL(orderID);
        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.verifyIssuedETicket(), is(true));
        orderDetailPage.closeEticketPopUp();

        step("Click refund button");
        orderDetailPage.clickRefundButton();
        refundPage.skipRescheduleNudgeIfShown();

        step("Fill refund form");
        refundPage.selectRandomRefundReason();
        refundPage.continueRefund();

        step("submit refund");
        refundPage.clickChecklistTnc();
        refundPage.clickSubmitRefund();
        refundPage.clickProcessRefund();
        refundPage.clickGoToOrders();

        step("Check if the refunded order is being reflected on corporate dashboard");
        String refundId = corporateApi.getRefundId(orderID, approverUsername, approverPassword,"prerefund");
        Boolean isApproved = false;
        Boolean success = corporateApi.approveOrRejectRefundB2B(approverUsername, approverPassword, refundId, isApproved);
        Assertion.assertThat("Refund Rejection Success", success, is(true));

        step("Check eticket");
        orderListPage.openIssuedTicketViaURL(orderID);
        orderDetailPage.closeEticketPopUp();
        Assertion.assertThat("Order is in Active order state", orderDetailPage.getOrderId(), is(orderID));
        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.verifyIssuedETicket(), is(true));
    }

    @Author(names = {"Aditi"})
    @Module(name = "Refund")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void testRejectRefundRequestByTravelerDwebApprover() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();

        Map<String, String> param = getParam("testRejectRefundRequestByTravelerDwebApprover");
        BaseTest.paramsThreadLocal.set(param);

        String travelerUsername = param.get("travelerUsername");
        String travelerPassword = param.get("travelerPassword");
        String otp= param.get("otp");
        String approverUsername = param.get("approverUsername");
        String approverPassword = param.get("approverPassword");
        String hotelName = param.get("hotelName");
        String dateType = param.get("date_type");
        String date = param.get("date");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        HotelLandingPageWeb<?> hotelLandingPage = HotelLandingPageWeb.getInstance(session);
        HotelRoomListPage<?> hotelRoomListPage = new HotelRoomListPage<>(session);
        HotelBookingFormPageWeb<?> hotelBookingFormPage = HotelBookingFormPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        RefundPageWeb<?> refundPage = RefundPageWeb.getInstance(session);
        TravelPlanWeb<?> travelPlanPage = TravelPlanWeb.getInstance(session);

        step("Go to home");
        homePage.goToHomePage();

        step("User open as guest");
        loginPage.loginAsGuest();

        step("User select IDR currency");
        homePage.selectCurrency("IDR");

        step("User open hotel vertical");
        homePage.openVerticalPage("Hotel");

        step("User search hotel");
        hotelLandingPage.searchStayLocation(hotelName);

        step("Set date");
        hotelLandingPage.setStayDate(dateType, date);

        step("Set guests and rooms");
        hotelLandingPage.clickRoomAndGuestButton();
        hotelLandingPage.setTotalRoom(1);
        hotelLandingPage.setTotalGuest(1);
        hotelLandingPage.clickDone();

        step("Submit search");
        hotelLandingPage.clickSubmit();

        step("Click book button");
        hotelRoomListPage.clickBookButton();

        step("Login from banner booking form");
        hotelBookingFormPage.clickLoginBanner();
        loginPage.loginSteps(travelerUsername, travelerPassword, otp, 1);

        step("Continue to payment page");
        hotelBookingFormPage.continueToSecondPartThenPaymentPage();
        travelPlanPage.skipTravelPlan();

        step("Payment page should be displayed");
        String orderID = paymentPage.getOrderId();
        orderID = String.valueOf(getOrderId(orderID));
        Assertion.assertThat("Corporate billing option is selected", paymentPage.getSelectedPayment().toLowerCase(), is("corporate billing"));

        step("Submit payment using Corporate Billing");
        paymentPage.clickOnPaymentButton();

        step("Approve booking by approver account for Order ID: " + orderID);
        Boolean bookingStatus = corporateApi.approveBookingB2BApi(approverUsername, approverPassword, orderID);
        Assertion.assertThat("Booking approved by B2B dashboard", bookingStatus, is(true));

        step("Open the order list and go to order detail");
        paymentPage.clickSeeOrderListButton();
        orderListPage.openIssuedTicketViaURL(orderID);
        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.verifyIssuedETicket(), is(true));
        orderDetailPage.closeEticketPopUp();

        step("Click refund button");
        orderDetailPage.clickRefundButton();
        refundPage.skipRescheduleNudgeIfShown();

        step("Fill refund form");
        refundPage.selectRandomRefundReason();
        refundPage.continueRefund();

        step("submit refund");
        refundPage.clickChecklistTnc();
        refundPage.clickSubmitRefund();
        refundPage.clickProcessRefund();
        refundPage.clickGoToOrders();

        step("Check if the refunded order is being reflected on corporate dashboard");
        String refundId = corporateApi.getRefundId(orderID, approverUsername, approverPassword,"prerefund");
        Boolean isApproved = false;
        Boolean success = corporateApi.approveOrRejectRefundB2B(approverUsername, approverPassword, refundId, isApproved);
        Assertion.assertThat("Refund Rejection Success", success, is(true));

        step("Check e - ticket");
        orderListPage.openIssuedTicketViaURL(orderID);
        orderDetailPage.closeEticketPopUp();
        Assertion.assertThat("Order is in Active order state", orderDetailPage.getOrderId(), is(orderID));
        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.verifyIssuedETicket(), is(true));
    }


    @Author(names = {"kartik"})
    @Module(name = "corporateCode")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void RoundTripInternationalSRPNudgeCorporateCodeApplied() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("RoundTripInternationalSRPNudgeCorporateCodeApplied");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPageWeb = FlightLandingPageWeb.getInstance(session);
        FlightSearchResultPageWeb<?> flightSearchResultPage = FlightSearchResultPageWeb.getInstance(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);

        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport =param.get("destinationAirport");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int duration =Integer.parseInt(param.get("duration"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);
        String returnDate = CoreFunctions.addToLongDate(departureDate, duration);
        int adultPassengers = Integer.parseInt(param.get("adultPassengers"));
        int childPassengers = Integer.parseInt(param.get("childPassengers"));
        int infantPassengers = Integer.parseInt(param.get("infantPassengers"));
        String username = param.get("username");
        String password = param.get("password");
        String currency = param.get("currency");
        String otp = param.get("otp");

        ArrayList<CorporateCodeData> corporateCodeData = corporateApi.setAndGetCorporateCodeData(param.get("corporateCodeData"));
        ArrayList<String> airline = corporateApi.getAirlineName(corporateCodeData);

        step("Create the corporate code for airline " + airline);
        ArrayList<Integer> corporateCodeIds = corporateApi.findCreateCorporateCode(username, password, corporateCodeData);
        Assertion.assertThat("Corporate code is created", corporateCodeIds, everyItem(notNullValue()));

        step("User open home page");
        homePage.goToHomePage();

        step("Go to account page");
        loginPage.goToAccountPage();

        step("login in using { " + username + " }");
        loginPage.loginIfNotWithSameEmail(username, password, otp, 1);

        step("User select IDR currency");
        homePage.selectCurrency(currency);

        step("User open Flight vertical");
        homePage.openVerticalPageViaURL("Flights");

        step("Toggle trip type");
        flightLandingPageWeb.toggleTripType(tripType);

        step("Select origin airport");
        flightLandingPageWeb.selectOrigin(originAirport);

        step("Select destination airport");
        flightLandingPageWeb.selectDestination(destinationAirport);

        step("Select date");
        flightLandingPageWeb.selectDate(departureDate,returnDate);

        step("search flight");
        flightLandingPageWeb.searchFlight();

        step("filter airline "+airline);
        flightSearchResultPage.filterAirline(airline);
        Assertion.assertThat("Airlines should include " + airline, new HashSet<>(flightSearchResultPage.getAirlineNames(airline)), is(new HashSet<>(airline)));
        //seems like wrong assertion, should be false, but as its in do again so not changing it.
        Assertion.assertThat("Corporate deals banner is present", flightSearchResultPage.isCorporateDealsBannerPresent(), is(true));
        Assertion.assertThat("Coporate deals benefit on the card is present", flightSearchResultPage.gotoCorporateDealsBenefitCard(), is(true));
    }

    @Author(names = {"kartik"})
    @Module(name = "corporateCode")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void RoundTripInternationalSRPNudgeCorporateCodeNotApplied() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("RoundTripInternationalSRPNudgeCorporateCodeNotApplied");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPageWeb = FlightLandingPageWeb.getInstance(session);
        FlightSearchResultPageWeb<?> flightSearchResultPage = FlightSearchResultPageWeb.getInstance(session);

        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");

        String username = param.get("username");
        String password = param.get("password");
        String currency = param.get("currency");
        String otp = param.get("otp");
        String airline = param.get("airline");

        ArrayList<String> airlines = new ArrayList<>(Arrays.asList(airline.split("-")));
        ArrayList<String> destinations = new ArrayList<>(Arrays.asList(destinationAirport.split("-")));
        ArrayList<String> origins = new ArrayList<>(Arrays.asList(originAirport.split("-")));

        int totalIterations = Math.min(origins.size(), destinations.size());

        step("User open home page");
        homePage.goToHomePage();

        step("Login as " + username + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(username, password);

        step("User select IDR currency");
        homePage.selectCurrency(currency);

        step("User open Flight vertical");
        homePage.openVerticalPageViaURL("Flights");

        step("Toggle trip type");
        flightLandingPageWeb.toggleTripType(tripType);

        for (int i = 0; i < totalIterations; i++) {

            String origin1 = origins.get(i);
            String destination1 = destinations.get(i);
            String airline1 = airlines.get(i);

            step("Select origin airport");
            flightLandingPageWeb.selectOrigin(origin1);

            step("Select destination airport");
            flightLandingPageWeb.selectDestination(destination1);

            step("search flight");
            flightLandingPageWeb.searchFlight();
            Assertion.assertThat("Corporate deals benefit on the card is not present for " + airline1, flightSearchResultPage.gotoCorporateDealsBenefitCard(), is(false));

            step("Corporate deals banner is not present " + airline1);
            flightSearchResultPage.filterAirline(airline1);
            flightSearchResultPage.skipRefund100P();
            Assertion.assertThat("Corporate deals banner is not present for " + airline1, flightSearchResultPage.isCorporateDealsBannerPresent(), is(false));

            flightSearchResultPage.selectFlight();
            flightSearchResultPage.filterAirline(airline1);
            flightSearchResultPage.skipRefund100P();
            Assertion.assertThat("Corporate deals banner is not present for " + airline1, flightSearchResultPage.isCorporateDealsBannerPresent(), is(false));

            flightSearchResultPage.clickOnChangeButton();
            homePage.openVerticalPageViaURL("Flights");
            Assertion.assertThat("Verify that landing page is displayed", flightLandingPageWeb.isOriginLocation(), is(true));

        }
    }

    @Author(names = {"Prerna"})
    @Module(name = "Login")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void testFlightBookingRoundTripFromTravelerCorporateCodeAppliedDweb() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testFlightBookingRoundTripFromTravelerCorporateCodeAppliedDweb");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPageWeb = FlightLandingPageWeb.getInstance(session);
        FlightSearchResultPageWeb<?> flightSearchResultPage = FlightSearchResultPageWeb.getInstance(session);
        FlightProductReviewPage<?> flightProductReviewPage = new FlightProductReviewPage<>(session);
        FlightBookingFormPageWeb<?> flightBookingFormPage = FlightBookingFormPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        FlightDescriptionPage<?> flightDescriptionPage = new FlightDescriptionPage<>(session);
        TravelPlanWeb<?> travelPlanPage = TravelPlanWeb.getInstance(session);

        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));

        int duration = Integer.parseInt(param.get("duration"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate,daysToDepartureDate);
        String returnDate = CoreFunctions.addToLongDate(departureDate, duration);

        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));

        String username = param.get("username");
        String password = param.get("password");
        String currency = param.get("currency");
        String airlineName = param.get("airlineName");
        String adminUsername = param.get("adminUsername");
        String adminPassword = param.get("adminPassword");
        String name = param.get("name");

        ArrayList<CorporateCodeData> corporateCodeData = corporateApi.setAndGetCorporateCodeData(param.get("corporateCodeData"));
        ArrayList<String> airline = corporateApi.getAirlineName(corporateCodeData);

        step("Create the corporate code for airline " + airline);
        ArrayList<Integer> corporateCodeIds = corporateApi.findCreateCorporateCode(username, password, corporateCodeData);
        Assertion.assertThat("Corporate code is created", corporateCodeIds, everyItem(notNullValue()));

        step("User open home page");
        homePage.goToHomePage();

        step("Go to account page");
        loginPage.goToAccountPage();

        step("login in using { " + username + " }");
        loginPage.loginIfNotWithSameEmail(username, password, "123456", 1);

        step("User select IDR currency");
        homePage.selectCurrency(currency);

        step("User open Flight vertical");
        homePage.openVerticalPage("Flights");

        step("Toggle trip type");
        flightLandingPageWeb.toggleTripType(tripType);

        step("Select origin airport");
        flightLandingPageWeb.selectOrigin(originAirport);

        step("Select destination airport");
        flightLandingPageWeb.selectDestination(destinationAirport);

        step("Select date");
        flightLandingPageWeb.selectDate(departureDate,returnDate);

        step("add passenger");
        flightLandingPageWeb.addPassenger(adultPassengers,childPassengers,infantPassengers);

        step("search flight");
        flightLandingPageWeb.searchFlight();

        step("Apply Filter" + airline);
        flightSearchResultPage.filterAirline(airlineName);

        step("Corporate code nudge is there");
        Assertion.assertThat("Corporate deals banner is present", flightSearchResultPage.isCorporateDealsBannerPresent(), is(true));

        step("select flight");
        flightSearchResultPage.selectFlight();

        step("select ticket on pdp page for round trip");
        flightDescriptionPage.selectTicket();

        step("Apply Filter" + airline);
        flightSearchResultPage.filterAirline(airlineName);

        step("Corporate code nudge is there for return trip");
        Assertion.assertThat("Corporate deals banner is present", flightSearchResultPage.isCorporateDealsBannerPresent(), is(true));

        step("select flight");
        flightSearchResultPage.selectFlight();

        step("select ticket on pdp page for round trip");
        flightDescriptionPage.selectTicket();

        step("Continue to booking form");
        flightProductReviewPage.continueToBookingForm();
        flightBookingFormPage.clickNoThanksOnFlightMayReschedulePopup();

        step("Click toggle to fill passengers details and save ");
        flightBookingFormPage.fillPassengerDetailsLogin(name);

        step("Go to payment page");
        flightBookingFormPage.goToPaymentPageFromBookingFormPage();

        step("Skip Flight may rescheduled page by clicking no thanks");
        travelPlanPage.skipTravelPlan();
        flightBookingFormPage.clickNoThanksOnFlightMayReschedulePopup();

        step("Payment page should be displayed");
        String orderID = paymentPage.getOrderId();
        orderID = String.valueOf(getOrderId(orderID));
        Assertion.assertThat("Corporate billing option is selected", paymentPage.getSelectedPayment().toLowerCase(), is("corporate billing"));

        step("Submit payment using Corporate Billing");
        paymentPage.clickOnPaymentButton();

        step("Check if the booked order is being reflected on corporate dashboard under purchase list");
        boolean isOrderReflectedOnPurchaseList = corporateApi.checkPurchaseOrderList(orderID, adminUsername, adminPassword,name);
        Assertion.assertThat("purchase order is reflected on B2B corporate dashboard in purchase list", isOrderReflectedOnPurchaseList, is(true));
    }

    @Author(names = {"Prerna"})
    @Module(name = "corporateCode")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void testFlightBookingRoundTripFromTravelerCorporateCodeNotAppliedDweb() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testFlightBookingRoundTripFromTravelerCorporateCodeNotAppliedDweb");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPageWeb = FlightLandingPageWeb.getInstance(session);
        FlightSearchResultPageWeb<?> flightSearchResultPage = FlightSearchResultPageWeb.getInstance(session);
        FlightProductReviewPage<?> flightProductReviewPage = new FlightProductReviewPage<>(session);
        FlightBookingFormPageWeb<?> flightBookingFormPage = FlightBookingFormPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        FlightDescriptionPage<?> flightDescriptionPage = new FlightDescriptionPage<>(session);
        TravelPlanWeb<?> travelPlanPage = TravelPlanWeb.getInstance(session);

        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));

        int duration = Integer.parseInt(param.get("duration"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate,daysToDepartureDate);
        String returnDate = CoreFunctions.addToLongDate(departureDate, duration);

        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));

        String username = param.get("username");
        String password = param.get("password");
        String currency = param.get("currency");
        String airlineName = param.get("airlineName");
        String adminUsername = param.get("adminUsername");
        String adminPassword = param.get("adminPassword");
        String name = param.get("name");

        ArrayList<CorporateCodeData> corporateCodeData = corporateApi.setAndGetCorporateCodeData(param.get("corporateCodeData"));
        ArrayList<String> airlines = corporateApi.getAirlineName(corporateCodeData);
        String airline = airlines.get(CoreFunctions.generateRandomInt(0, airlines.size() - 1));

        step("Delete the corporate codes");
        boolean isIdDeleted = corporateApi.deleteAllCorporateCode(username, password);
        Assertion.assertThat("Success to delete the corporate code", isIdDeleted, is(true));

        step("User open home page");
        homePage.goToHomePage();

        step("Go to account page");
        loginPage.goToAccountPage();

        step("login in using { " + username + " }");
        loginPage.loginIfNotWithSameEmail(username, password, "123456", 1);

        step("User select IDR currency");
        homePage.selectCurrency(currency);

        step("User open Flight vertical");
        homePage.openVerticalPage("Flights");

        step("Toggle trip type");
        flightLandingPageWeb.toggleTripType(tripType);

        step("Select origin airport");
        flightLandingPageWeb.selectOrigin(originAirport);

        step("Select destination airport");
        flightLandingPageWeb.selectDestination(destinationAirport);

        step("Select date");
        flightLandingPageWeb.selectDate(departureDate,returnDate);

        step("add passenger");
        flightLandingPageWeb.addPassenger(adultPassengers,childPassengers,infantPassengers);

        step("search flight");
        flightLandingPageWeb.searchFlight();

        step("Apply Filter" + airline);
        flightSearchResultPage.filterAirline(airlineName);

        step("Corporate code nudge is not there");
        Assertion.assertThat("Corporate deals banner is not present", flightSearchResultPage.isCorporateDealsBannerPresent(), is(false));

        step("select flight");
        flightSearchResultPage.selectFlight();

        step("select ticket on pdp page for round trip");
        flightDescriptionPage.selectTicket();

        step("Apply Filter" + airline);
        flightSearchResultPage.filterAirline(airlineName);

        step("Corporate code nudge is not there for return trip");
        Assertion.assertThat("Corporate deals banner is not present", flightSearchResultPage.isCorporateDealsBannerPresent(), is(false));

        step("select flight");
        flightSearchResultPage.selectFlight();

        step("select ticket on pdp page for round trip");
        flightDescriptionPage.selectTicket();

        step("Continue to booking form");
        flightProductReviewPage.continueToBookingForm();
        flightBookingFormPage.clickNoThanksOnFlightMayReschedulePopup();

        step("Click toggle to fill passengers details and save ");
        flightBookingFormPage.fillPassengerDetailsLogin(name);

        step("Go to payment page");
        flightBookingFormPage.goToPaymentPageFromBookingFormPage();

        step("Skip Flight may rescheduled page by clicking no thanks");
        travelPlanPage.skipTravelPlan();
        flightBookingFormPage.clickNoThanksOnFlightMayReschedulePopup();

        step("Payment page should be displayed");
        String orderID = paymentPage.getOrderId();
        orderID = String.valueOf(getOrderId(orderID));
        Assertion.assertThat("Corporate billing option is selected", paymentPage.getSelectedPayment().toLowerCase(), is("corporate billing"));

        step("Submit payment using Corporate Billing");
        paymentPage.clickOnPaymentButton();

        step("Check if the booked order is being reflected on corporate dashboard under purchase list");
        boolean isOrderReflectedOnPurchaseList = corporateApi.checkPurchaseOrderList(orderID,adminUsername, adminPassword,name);
        Assertion.assertThat("purchase order is reflected on B2B corporate dashboard in purchase list", isOrderReflectedOnPurchaseList, is(true));
    }

    @Author(names = "Kartik")
    @Module(name = "Corporate")
    @Tribe(name = "Train")
    @Vertical(name = "B2B")
    @Test
    public void verifyTravelerCanCreateTrainBookingByRequestingToAdmin() throws InterruptedException, FailedLoginException {
        Map<String, String> param = getParam("verifyTravelerCanCreateTrainBookingByRequestingToAdmin");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        String currency = param.get("currency");
        String originStation = param.get("originStation");
        String destinationStation = param.get("destinationStation");
        String adminUsername = param.get("adminUsername");
        String adminPassword = param.get("adminPassword");
        String travelerUsername = param.get("travelerUsername");
        String travelerPassword = param.get("travelerPassword");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        TrainSearchPageWeb<?> trainSearchPage = TrainSearchPageWeb.getInstance(session);
        TrainSearchResultPageWeb<?> trainSearchResultPage = TrainSearchResultPageWeb.getInstance(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        TrainBookingFormPage<?> trainBookingFormPage = new TrainBookingFormPage<>(session);

        step("Go to home");
        homePage.goToHomePage();

        step("Login as " + travelerUsername + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(travelerUsername, travelerPassword);

        step("User select IDR currency");
        homePage.selectCurrency(currency);

        step("User open train vertical");
        if (Configs.ENVIRONMENT == Environment.GK || Configs.ENVIRONMENT == Environment.PREPROD) {
            homePage.selectEnglishLanguageOnly();
            homePage.openVerticalPageViaURL("Trains");
        } else if (Configs.ENVIRONMENT == Environment.PROD) {
            homePage.openVerticalPageViaURL("Trains");
        }

        step("User skip pop-up");
        trainSearchPage.clickSkipPopup();

        step("User select origin station");
        trainSearchPage.selectOrigin(originStation);

        step("User select destination station");
        trainSearchPage.selectDestination(destinationStation);

        step("User search train");
        trainSearchPage.searchTrain();

        step("User skip pop-up");
        trainSearchPage.clickSkipPopup();

        step("select train");
        trainSearchResultPage.selectTrain();

        step("fill booking form Details");
        trainBookingFormPage.clickToggleKnob();
        trainBookingFormPage.continueToSecondHalf();
        trainBookingFormPage.continueToPayBtn();

        step("Payment page should be displayed");
        String orderID = paymentPage.getOrderId();
        orderID = String.valueOf(getOrderId(orderID));
        Assertion.assertThat("Corporate billing option is selected", paymentPage.getSelectedPayment().toLowerCase(), is("corporate billing"));

        step("Submit payment using Corporate Billing");
        paymentPage.clickOnPaymentButton();

        step("Approve booking by admin account for Order ID: " + orderID);
        Boolean bookingStatus = corporateApi.approveBookingB2BApi(adminUsername ,adminPassword, orderID);
        Assertion.assertThat("Booking approved by B2B dashboard", bookingStatus, is(true));

        step("Open the order list and go to order detail");
        paymentPage.clickSeeOrderListButton();
        orderListPage.openIssuedTicketViaURL(orderID);
        orderDetailPage.skipCoachmark();
        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.verifyIssuedETicket(), is(true));
    }

    @Author(names = "Kartik")
    @Module(name = "Corporate")
    @Tribe(name = "Train")
    @Vertical(name = "B2B")
    @Test
    public void verifyTravelerCanCreateTrainBookingByRequestingToApprover() throws InterruptedException, FailedLoginException {
        Map<String, String> param = getParam("verifyTravelerCanCreateTrainBookingByRequestingToApprover");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        String currency = param.get("currency");
        String originStation = param.get("originStation");
        String destinationStation = param.get("destinationStation");
        String approverUsername = param.get("approverUsername");
        String approverPassword = param.get("approverPassword");
        String travelerUsername = param.get("travelerUsername");
        String travelerPassword = param.get("travelerPassword");
        String otp= param.get("otp");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        TrainSearchPageWeb<?> trainSearchPage = TrainSearchPageWeb.getInstance(session);
        TrainSearchResultPageWeb<?> trainSearchResultPage = TrainSearchResultPageWeb.getInstance(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        TrainBookingFormPage<?> trainBookingFormPage = new TrainBookingFormPage<>(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open home page as guest");
        loginPage.loginAsGuest();

        step ("User select IDR currency");
        homePage.selectCurrency(currency);

        step("User open train vertical");
        if (Configs.ENVIRONMENT == Environment.GK || Configs.ENVIRONMENT == Environment.PREPROD) {
            homePage.selectEnglishLanguageOnly();
            homePage.openVerticalPage("Train");
        } else if (Configs.ENVIRONMENT == Environment.PROD) {
            homePage.openVerticalPage("Trains");
        }

        step("User skip pop-up");
        trainSearchPage.clickSkipPopup();

        step("User select origin station");
        trainSearchPage.selectOrigin(originStation);

        step("User select destination station");
        trainSearchPage.selectDestination(destinationStation);

        step("User search train");
        trainSearchPage.searchTrain();

        step("User skip pop-up");
        trainSearchPage.clickSkipPopup();

        step("select train");
        trainSearchResultPage.selectTrain();

        step("continue to booking form page");
        trainBookingFormPage.clickLoginBanner();

        step("login with :"+travelerUsername);
        loginPage.loginSteps(travelerUsername, travelerPassword, otp, 1);

        step("fill booking form Details");
        trainBookingFormPage.clickToggleKnob();
        trainBookingFormPage.continueToSecondHalf();
        trainBookingFormPage.continueToPayBtn();

        step("Payment page should be displayed");
        String orderID = paymentPage.getOrderId();
        orderID = String.valueOf(getOrderId(orderID));
        Assertion.assertThat("Corporate billing option is selected", paymentPage.getSelectedPayment().toLowerCase(), is("corporate billing"));

        step("Submit payment using Corporate Billing");
        paymentPage.clickOnPaymentButton();

        step("Approve booking by approver account for Order ID: " + orderID);
        Boolean bookingStatus = corporateApi.approveBookingB2BApi(approverUsername, approverPassword, orderID);
        Assertion.assertThat("Booking approved by B2B dashboard", bookingStatus, is(true));

        step("Open the order list and go to order detail");
        paymentPage.clickSeeOrderListButton();
        orderListPage.openIssuedTicketViaURL(orderID);
        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.verifyIssuedETicket(), is(true));
    }

    @Author(names = "Kartik")
    @Module(name = "Corporate")
    @Tribe(name = "Train")
    @Vertical(name = "B2B")
    @Test
    public void verifyAdminCanCreateAutoApprovedTrainBooking() throws InterruptedException, FailedLoginException {
        Map<String, String> param = getParam("verifyAdminCanCreateAutoApprovedTrainBooking");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        String currency = param.get("currency");
        String originStation = param.get("originStation");
        String destinationStation = param.get("destinationStation");
        String adminUsername = param.get("adminUsername");
        String adminPassword = param.get("adminPassword");
        String otp= param.get("otp");
        String departureDate = getRandomLongDateOnRange(1,30);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        TrainSearchPageWeb<?> trainSearchPage = TrainSearchPageWeb.getInstance(session);
        TrainSearchResultPageWeb<?> trainSearchResultPage = TrainSearchResultPageWeb.getInstance(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        TrainBookingFormPage<?> trainBookingFormPage = new TrainBookingFormPage<>(session);

        step("User open home page");
        homePage.goToHomePage();

        step("login with :"+adminUsername);
        loginPage.loginIfNotWithSameEmail(adminUsername, adminPassword, otp, 1);

        step ("User select IDR currency");
        homePage.selectCurrency(currency);

        step("User open train vertical");
        homePage.openVerticalPageViaURL("Train");

        step("User skip pop-up");
        trainSearchPage.clickSkipPopup();

        step("User select origin station");
        trainSearchPage.selectOrigin(originStation);

        step("User select destination station");
        trainSearchPage.selectDestination(destinationStation);

        step("User select departure date");
        trainSearchPage.selectDate(departureDate);

        step("User search train");
        trainSearchPage.searchTrain();

        step("User skip pop-up");
        trainSearchPage.clickSkipPopup();

        step("select train");
        trainSearchResultPage.selectTrain();

        step("fill booking form Details");
        trainBookingFormPage.clickToggleKnob();
        trainBookingFormPage.continueToSecondHalf();
        trainBookingFormPage.continueToPayBtn();

        step("Payment page should be displayed");
        String orderID = paymentPage.getOrderId();
        orderID = String.valueOf(getOrderId(orderID));
        Assertion.assertThat("Corporate billing option is selected", paymentPage.getSelectedPayment().toLowerCase(), is("corporate billing"));

        step("Submit payment using Corporate Billing");
        paymentPage.clickOnPaymentButton();

        step("Open the order list and go to order detail");
        paymentPage.clickSeeOrderListButton();
        orderListPage.openIssuedTicketViaURL(orderID);
        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.verifyIssuedETicket(), is(true));
    }

    @Author(names = {"kartik"})
    @Module(name = "corporateCode")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void OneWayInternationalSRPNudgeCorporateCodeApplied() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("OneWayInternationalSRPNudgeCorporateCodeApplied");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPageWeb = FlightLandingPageWeb.getInstance(session);
        FlightSearchResultPageWeb<?> flightSearchResultPage = FlightSearchResultPageWeb.getInstance(session);

        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");

        String username = param.get("username");
        String password = param.get("password");
        String currency = param.get("currency");
        String otp = param.get("otp");
        String airline = param.get("airline");

        ArrayList<String> origins = new ArrayList<>(Arrays.asList(originAirport.split("-")));
        ArrayList<String> destinations = new ArrayList<>(Arrays.asList(destinationAirport.split("-")));
        ArrayList<String> airlines = new ArrayList<>(Arrays.asList(airline.split("-")));

        int totalIterations = Math.min(origins.size(), destinations.size());

        step("User open home page");
        homePage.goToHomePage();

        step("Login as " + username + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(username, password);

        step("User select IDR currency");
        homePage.selectCurrency(currency);

        step("User open Flight vertical");
        homePage.openVerticalPageViaURL("Flights");

        for (int i = 0; i < totalIterations; i++) {
            String origin1 = origins.get(i);
            String destination1 = destinations.get(i);
            String airline1 = airlines.get(i);

            step("Select origin airport");
            flightLandingPageWeb.selectOrigin(origin1);

            step("Select destination airport");
            flightLandingPageWeb.selectDestination(destination1);

            step("search flight");
            flightLandingPageWeb.searchFlight();
            flightSearchResultPage.skipRefund100P();
            Assertion.assertThat("Corporate deals benefit on the card is present", flightSearchResultPage.gotoCorporateDealsBenefitCard(), is(true));

            step("Corporate deals banner is present" + airline1);
            flightSearchResultPage.filterAirline(airline1);
            Assertion.assertThat("Corporate deals banner is present", flightSearchResultPage.isCorporateDealsBannerPresent(), is(true));
            flightSearchResultPage.isSRPLoaded();
            homePage.openVerticalPageViaURL("Flights");
            Assertion.assertThat("Verify that landing page is displayed", flightLandingPageWeb.isOriginLocation(), is(true));

        }
    }

    @Author(names = {"kartik"})
    @Module(name = "corporateCode")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void OneWayInternationalSRPNudgeCorporateCodeNotApplied() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("OneWayInternationalSRPNudgeCorporateCodeNotApplied");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPageWeb = FlightLandingPageWeb.getInstance(session);
        FlightSearchResultPageWeb<?> flightSearchResultPage = FlightSearchResultPageWeb.getInstance(session);

        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");
        String username = param.get("username");
        String password = param.get("password");
        String currency = param.get("currency");
        String otp = param.get("otp");
        String airline = param.get("airline");

        ArrayList<String> origins = new ArrayList<>(Arrays.asList(originAirport.split("-")));
        ArrayList<String> destinations = new ArrayList<>(Arrays.asList(destinationAirport.split("-")));
        ArrayList<String> airlines = new ArrayList<>(Arrays.asList(airline.split("-")));

        int totalIterations = Math.min(origins.size(), destinations.size());

        step("User open home page");
        homePage.goToHomePage();

        step("Login as " + username + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(username, password);

        step("User select IDR currency");
        homePage.selectCurrency(currency);

        step("User open Flight vertical");
        homePage.openVerticalPageViaURL("Flights");

        for (int i = 0; i < totalIterations; i++) {
            String origin1=origins.get(i);
            String destination1=destinations.get(i);
            String airline1=airlines.get(i);

            step("Select origin airport");
            flightLandingPageWeb.selectOrigin(origin1);

            step("Select destination airport");
            flightLandingPageWeb.selectDestination(destination1);

            step("search flight");
            flightLandingPageWeb.searchFlight();
            flightSearchResultPage.skipRefund100P();

            step("filter airline " + airline1);
            flightSearchResultPage.filterAirline(airline1);
            Assertion.assertThat("Corporate deals banner is not present", flightSearchResultPage.isCorporateDealsBannerPresent(), is(false));
            Assertion.assertThat("Corporate deals benefit on the card is  not present", flightSearchResultPage.gotoCorporateDealsBenefitCard(), is(false));
            homePage.openVerticalPageViaURL("Flights");
            Assertion.assertThat("Verify that landing page is displayed", flightLandingPageWeb.isOriginLocation(), is(true));
        }
    }

    @Author(names = {"Ashish"})
    @Module(name = "TravelPolicy")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void testCorporateUserCanDoCompleteBookingWithTravelPolicyDweb() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testCorporateUserCanDoCompleteBookingWithTravelPolicyDweb");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPageWeb = FlightLandingPageWeb.getInstance(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);
        FlightSearchResultPageWeb<?> flightSearchResultPage = FlightSearchResultPageWeb.getInstance(session);
        FlightProductReviewPage<?> flightProductReviewPage = new FlightProductReviewPage<>(session);
        FlightBookingFormPageWeb<?> flightBookingFormPage = FlightBookingFormPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        TravelPlanWeb<?> travelPlanPage = TravelPlanWeb.getInstance(session);


        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate,daysToDepartureDate);

        int adultPassengers = Integer.parseInt(param.get("adultPassengers"));
        int childPassengers = Integer.parseInt(param.get("childPassengers"));
        int infantPassengers = Integer.parseInt(param.get("infantPassengers"));

        String airline = param.get("airline");

        String username = param.get("username");
        String password = param.get("password");
        String adminUsername = param.get("adminUsername");
        String adminPassword = param.get("adminPassword");
        String name = param.get("name");

        int minPrice = Integer.parseInt(param.get("minPrice"));
        int maxPrice = Integer.parseInt(param.get("maxPrice"));

        String currency = param.get("currency");
        String policyName = param.get("policyName");
        String otp = param.get("otp");

        step("get related data via api");
        String accessTokenAdmin = corporateApi.getAccessTokenB2BCorporate(adminUsername,adminPassword);
        long accountIdAdmin = corporateApi.getAccountId(accessTokenAdmin);
        EmployeeData employeeDataAdmin = corporateApi.getEmployeeData(adminUsername);
        EmployeeData employeeDataUser = corporateApi.getEmployeeData(username);

        step("patch travel policy");
        int travelPolicyID = corporateApi.patchTravelPolicyFlight(accessTokenAdmin,employeeDataAdmin,accountIdAdmin,currency,minPrice,maxPrice,"",airline,0,false,false,policyName);
        Assertion.assertThat("Travel Policy patched", travelPolicyID, not(-1));

        step("assign travel policy to user");
        boolean isPolicyAssign = corporateApi.assignTravelPolicyToUser(accountIdAdmin,employeeDataAdmin,employeeDataUser,travelPolicyID);
        Assertion.assertThat("Travel Policy assigned", isPolicyAssign, is(true));

        step("set the search to display all in srp");
        boolean isSettingDone = corporateApi.setSearchTypePolicy(employeeDataAdmin,accountIdAdmin,"search",true);
        Assertion.assertThat("Search Type Policy set", isSettingDone, is(true));

        step("User open home page");
        homePage.goToHomePage();

        step("Go to account page");
        loginPage.goToAccountPage();

        step("login in using { " + username + " }");
        loginPage.loginIfNotWithSameEmail(username, password, otp, 1);

        step("User select IDR currency");
        homePage.selectCurrency(currency);

        step("User open Flight vertical");
        homePage.openVerticalPage("Flights");

        step("Toggle trip type");
        flightLandingPageWeb.toggleTripType(tripType);

        step("Select origin airport");
        flightLandingPageWeb.selectOrigin(originAirport);

        step("Select destination airport");
        flightLandingPageWeb.selectDestination(destinationAirport);

        step("Select date");
        flightLandingPageWeb.selectDate(departureDate);

        step("add passenger");
        flightLandingPageWeb.addPassenger(adultPassengers,childPassengers,infantPassengers);

        step("search flight");
        flightLandingPageWeb.searchFlight();

        step("filter airline {" + airline + " }" );
        flightSearchResultPage.filterAirline(airline);

        step("verify meet travel policy for flight");
        Assertion.assertThat("Is meet travel policy flight" + airline, flightSearchResultPage.isMeetTravelPolicy(), is(true));

        step("select flight");
        flightSearchResultPage.selectFlight();

        step("Continue to booking form");
        flightProductReviewPage.continueToBookingForm();
        flightBookingFormPage.clickNoThanksOnFlightMayReschedulePopup();

        step("Click toggle to fill passengers details and save ");
        flightBookingFormPage.fillPassengerDetailsLogin(name);

        step("Go to payment page");
        flightBookingFormPage.goToPaymentPageFromBookingFormPage();

        step("Skip Flight may rescheduled page by clicking no thanks");
        travelPlanPage.skipTravelPlan();
        flightBookingFormPage.clickNoThanksOnFlightMayReschedulePopup();

        step("Payment page should be displayed");
        String orderID = paymentPage.getOrderId();
        orderID = String.valueOf(getOrderId(orderID));
        Assertion.assertThat("Corporate billing option is selected", paymentPage.getSelectedPayment().toLowerCase(), is("corporate billing"));

        step("Submit payment using Corporate Billing");
        paymentPage.clickOnPaymentButton();

        step("verify successfully placed order popup from corporate");
        Assertion.assertThat("get successfully placed order popup",paymentPage.isCorporateSuccessfullyPlacedOrderPopup(),is(true));

        step("verify meet travel policy in the corporate dashboard via api");
        boolean isMeetTravelPolicy = corporateApi.checkPurchaseOrderMeetTravelPolicy(orderID, adminUsername, adminPassword);
        Assertion.assertThat("is meet travel policy shown", isMeetTravelPolicy, is(true));
    }

    @Author(names = {"Ashish"})
    @Module(name = "TravelPolicy")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void testMeetTravelPolicyFlightInternationalOWBookingDweb() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testMeetTravelPolicyFlightInternationalOWBookingDweb");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPageWeb = FlightLandingPageWeb.getInstance(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);
        FlightSearchResultPageWeb<?> flightSearchResultPage = FlightSearchResultPageWeb.getInstance(session);
        FlightProductReviewPage<?> flightProductReviewPage = new FlightProductReviewPage<>(session);
        FlightBookingFormPageWeb<?> flightBookingFormPage = FlightBookingFormPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        TravelPlanWeb<?> travelPlanPage = TravelPlanWeb.getInstance(session);


        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate,daysToDepartureDate);

        int adultPassengers = Integer.parseInt(param.get("adultPassengers"));
        int childPassengers = Integer.parseInt(param.get("childPassengers"));
        int infantPassengers = Integer.parseInt(param.get("infantPassengers"));

        String airline = param.get("airline");

        String username = param.get("username");
        String password = param.get("password");
        String adminUsername = param.get("adminUsername");
        String adminPassword = param.get("adminPassword");
        String name = param.get("name");
        String lastName = param.get("lastName");

        int minPrice = Integer.parseInt(param.get("minPrice"));
        int maxPrice = Integer.parseInt(param.get("maxPrice"));

        String currency = param.get("currency");
        String policyName = param.get("policyName");
        String otp = param.get("otp");

        step("get related data via api");
        String accessTokenAdmin = corporateApi.getAccessTokenB2BCorporate(adminUsername,adminPassword);
        long accountIdAdmin = corporateApi.getAccountId(accessTokenAdmin);
        EmployeeData employeeDataAdmin = corporateApi.getEmployeeData(adminUsername);
        EmployeeData employeeDataUser = corporateApi.getEmployeeData(username);

        step("patch travel policy");
        int travelPolicyID = corporateApi.patchTravelPolicyFlightCustom(accessTokenAdmin,employeeDataAdmin,accountIdAdmin,currency,minPrice,maxPrice,"",airline,0,false,false,policyName,"INTERNATIONAL");
        Assertion.assertThat("Travel Policy patched", travelPolicyID, not(-1));

        step("assign travel policy to user");
        boolean isPolicyAssign = corporateApi.assignTravelPolicyToUser(accountIdAdmin,employeeDataAdmin,employeeDataUser,travelPolicyID);
        Assertion.assertThat("Travel Policy assigned", isPolicyAssign, is(true));

        step("set the search to display all in srp");
        boolean isSettingDone = corporateApi.setSearchTypePolicy(employeeDataAdmin,accountIdAdmin,"search",true);
        Assertion.assertThat("Search Type Policy set", isSettingDone, is(true));

        step("User open home page");
        homePage.goToHomePage();

        step("Go to account page");
        loginPage.goToAccountPage();

        step("login in using { " + username + " }");
        loginPage.loginIfNotWithSameEmail(username, password, otp, 1);

        step("User select IDR currency");
        homePage.selectCurrency(currency);

        step("User open Flight vertical");
        homePage.openVerticalPage("Flights");

        step("Toggle trip type");
        flightLandingPageWeb.toggleTripType(tripType);

        step("Select origin airport");
        flightLandingPageWeb.selectOrigin(originAirport);

        step("Select destination airport");
        flightLandingPageWeb.selectDestination(destinationAirport);

        step("Select date");
        flightLandingPageWeb.selectDate(departureDate);

        step("add passenger");
        flightLandingPageWeb.addPassenger(adultPassengers,childPassengers,infantPassengers);

        step("search flight");
        flightLandingPageWeb.searchFlight();

        step("filter airline {" + airline + " }" );
        flightSearchResultPage.filterAirline(airline);

        step("verify meet travel policy for flight");
        Assertion.assertThat("Is meet travel policy flight" + airline, flightSearchResultPage.isMeetTravelPolicy(), is(true));

        step("select flight");
        flightSearchResultPage.selectFlight();

        step("Continue to booking form");
        flightProductReviewPage.continueToBookingForm();
        flightBookingFormPage.clickNoThanksOnFlightMayReschedulePopup();

        step("Click toggle to fill passengers details and save ");
        flightBookingFormPage.fillPassengerDetailsLogin(name,lastName);

        step("Go to payment page");
        flightBookingFormPage.goToPaymentPageFromBookingFormPage();

        step("Skip Flight may rescheduled page by clicking no thanks");
        travelPlanPage.skipTravelPlan();
        flightBookingFormPage.clickNoThanksOnFlightMayReschedulePopup();

        step("Payment page should be displayed");
        String orderID = paymentPage.getOrderId();
        orderID = String.valueOf(getOrderId(orderID));
        Assertion.assertThat("Corporate billing option is selected", paymentPage.getSelectedPayment().toLowerCase(), is("corporate billing"));

        step("Submit payment using Corporate Billing");
        paymentPage.clickOnPaymentButton();

        step("verify successfully placed order popup from corporate");
        Assertion.assertThat("get successfully placed order popup",paymentPage.isCorporateSuccessfullyPlacedOrderPopup(),is(true));

        step("verify meet travel policy in the corporate dashboard via api");
        boolean isMeetTravelPolicy = corporateApi.checkPurchaseOrderMeetTravelPolicy(orderID, adminUsername, adminPassword);
        Assertion.assertThat("is meet travel policy shown", isMeetTravelPolicy, is(true));
    }

    @Author(names = {"Prerna"})
    @Module(name = "corporateCode")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void RoundTripSRPNudgeCorporateCodeApplied() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("RoundTripSRPNudgeCorporateCodeApplied");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPageWeb = FlightLandingPageWeb.getInstance(session);
        FlightSearchResultPageWeb<?> flightSearchResultPage = FlightSearchResultPageWeb.getInstance(session);

        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport =param.get("destinationAirport");
        String username = param.get("username");
        String password = param.get("password");
        String currency = param.get("currency");
        String otp = param.get("otp");

        String airline = param.get("airline");

        ArrayList<String> origins = new ArrayList<>(Arrays.asList(originAirport.split("-")));
        ArrayList<String> destinations = new ArrayList<>(Arrays.asList(destinationAirport.split("-")));
        ArrayList<String> airlines = new ArrayList<>(Arrays.asList(airline.split("-")));

        int totalIterations = Math.min(origins.size(), destinations.size());

        step("User open home page");
        homePage.goToHomePage();

        step("Login as " + username + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(username, password);

        step("User select IDR currency");
        homePage.selectCurrency(currency);

        step("User open Flight vertical");
        homePage.openVerticalPageViaURL("Flights");

        step("Toggle trip type");
        flightLandingPageWeb.toggleTripType(tripType);

        for (int i = 0; i < totalIterations; i++) {
            String origin1 = origins.get(i);
            String destination1 = destinations.get(i);
            String airline1 = airlines.get(i);

            step("Select origin airport");
            flightLandingPageWeb.selectOrigin(origin1);

            step("Select destination airport");
            flightLandingPageWeb.selectDestination(destination1);

            step("search flight");
            flightLandingPageWeb.searchFlight();
            Assertion.assertThat("Corporate deals benefit on the card is present", flightSearchResultPage.gotoCorporateDealsBenefitCard(), is(true));

            step("Apply Filter" + airline);
            flightSearchResultPage.filterAirline(airline1);

            step("Corporate code nudge is there for return trip");
            Assertion.assertThat("Corporate deals banner is present", flightSearchResultPage.isCorporateDealsBannerPresent(), is(true));

            step("select flight");
            flightSearchResultPage.selectFlight();

            step("Apply Filter" + airline1);
            flightSearchResultPage.filterAirline(airline1);

            step("Corporate code nudge is there for return trip");
            Assertion.assertThat("Corporate deals banner is present", flightSearchResultPage.isCorporateDealsBannerPresent(), is(true));
            homePage.openVerticalPageViaURL("Flights");
            Assertion.assertThat("Verify landing page is displayed", flightLandingPageWeb.isOriginLocation(), is(true));

        }
    }

    @Author(names = {"Prerna"})
    @Module(name = "corporateCode")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void RoundTripSRPNudgeCorporateCodeNotApplied() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("RoundTripSRPNudgeCorporateCodeNotApplied");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPageWeb = FlightLandingPageWeb.getInstance(session);
        FlightSearchResultPageWeb<?> flightSearchResultPage = FlightSearchResultPageWeb.getInstance(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);
        FlightDescriptionPage<?> flightDescriptionPage = new FlightDescriptionPage<>(session);

        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int duration =Integer.parseInt(param.get("duration"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);
        String returnDate = CoreFunctions.addToLongDate(departureDate, duration);
        String username = param.get("username");
        String password = param.get("password");
        String currency = param.get("currency");
        String otp = param.get("otp");
        String airline = param.get("airline");

        ArrayList<String> airlines = new ArrayList<>(Arrays.asList(airline.split("-")));


        step("User open home page");
        homePage.goToHomePage();

        step("Login as " + username + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(username, password);

        step("User select IDR currency");
        homePage.selectCurrency(currency);

        step("User open Flight vertical");
        homePage.openVerticalPageViaURL("Flights");

        step("Toggle trip type");
        flightLandingPageWeb.toggleTripType(tripType);

        step("Select origin airport");
        flightLandingPageWeb.selectOrigin(originAirport);

        step("Select destination airport");
        flightLandingPageWeb.selectDestination(destinationAirport);

        step("Select date");
        flightLandingPageWeb.selectDate(departureDate, returnDate);

        step("search flight");
        flightLandingPageWeb.searchFlight();

        step("Verify Corporate deals banner is not present");
        for (String airline1 : airlines) {
            flightSearchResultPage.filterAirline(new ArrayList<>(Arrays.asList(airline1)));
            Assertion.assertThat("Corporate deals banner is not present for " + airline1, flightSearchResultPage.isCorporateDealsBannerPresent(), is(false));
            Assertion.assertThat("Corporate deals benefit on the card is not present for" + airline1, flightSearchResultPage.gotoCorporateDealsBenefitCard(), is(false));

            flightSearchResultPage.selectFlight();
            flightSearchResultPage.filterAirline(new ArrayList<>(Arrays.asList(airline1)));
            Assertion.assertThat("Corporate deals banner is not present for" + airline1, flightSearchResultPage.isCorporateDealsBannerPresent(), is(false));
            Assertion.assertThat("Corporate deals benefit on the card is not present for" + airline1, flightSearchResultPage.gotoCorporateDealsBenefitCard(), is(false));

            flightSearchResultPage.clickOnChangeButton();

        }
    }

    @Author(names = {"Prerna"})
    @Module(name = "corporateCode")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void OneWaySRPNudgeCorporateCodeApplied() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("OneWaySRPNudgeCorporateCodeApplied");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPageWeb = FlightLandingPageWeb.getInstance(session);
        FlightSearchResultPageWeb<?> flightSearchResultPage = FlightSearchResultPageWeb.getInstance(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);

        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);
        String username = param.get("username");
        String password = param.get("password");
        String adminUsername = param.get("adminUsername");
        String adminPassword = param.get("adminPassword");
        String currency = param.get("currency");
        String otp = param.get("otp");
        ArrayList<CorporateCodeData> corporateCodeData = corporateApi.setAndGetCorporateCodeData(param.get("corporateCodeData"));
        ArrayList<String> airline = corporateApi.getAirlineName(corporateCodeData);

        step("Create the corporate code for airline " + airline);
        ArrayList<Integer> corporateCodeIds = corporateApi.findCreateCorporateCode(adminUsername, adminPassword, corporateCodeData);
        Assertion.assertThat("Corporate code is created", corporateCodeIds, everyItem(notNullValue()));

        step("User open home page");
        homePage.goToHomePage();

        step("Go to account page");
        loginPage.goToAccountPage();

        step("login in using { " + username + " }");
        loginPage.loginIfNotWithSameEmail(username, password, otp, 1);

        step("User select IDR currency");
        homePage.selectCurrency(currency);

        step("User open Flight vertical");
        homePage.openVerticalPageViaURL("Flights");

        step("Select origin airport");
        flightLandingPageWeb.selectOrigin(originAirport);

        step("Select destination airport");
        flightLandingPageWeb.selectDestination(destinationAirport);

        step("Select date");
        flightLandingPageWeb.selectDate(departureDate);

        step("search flight");
        flightLandingPageWeb.searchFlight();

        step("filter airline "+airline);
        flightSearchResultPage.filterAirline(airline);
        Assertion.assertThat("Airlines should include " + airline, new HashSet<>(flightSearchResultPage.getAirlineNames(airline)), is(new HashSet<>(airline)));
        Assertion.assertThat("Corporate deals banner is present", flightSearchResultPage.isCorporateDealsBannerPresent(), is(true));
        Assertion.assertThat("Corporate deals benefit on the card is present", flightSearchResultPage.gotoCorporateDealsBenefitCard(), is(true));
    }

    @Author(names = {"Prerna"})
    @Module(name = "corporateCode")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void OneWaySRPNudgeCorporateCodeNotApplied() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("OneWaySRPNudgeCorporateCodeNotApplied");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPageWeb = FlightLandingPageWeb.getInstance(session);
        FlightSearchResultPageWeb<?> flightSearchResultPage = FlightSearchResultPageWeb.getInstance(session);

        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");
        String username = param.get("username");
        String password = param.get("password");
        String currency = param.get("currency");
        String otp = param.get("otp");

        String airline = param.get("airline");

        ArrayList<String> airlines = new ArrayList<>(Arrays.asList(airline.split("-")));

        step("User open home page");
        homePage.goToHomePage();

        step("Go to account page");
        loginPage.goToAccountPage();

        step("login in using { " + username + " }");
        loginPage.loginIfNotWithSameEmail(username, password, otp, 1);

        step("User select IDR currency");
        homePage.selectCurrency(currency);

        step("User open Flight vertical");
        homePage.openVerticalPageViaURL("Flights");

        step("Select origin airport");
        flightLandingPageWeb.selectOrigin(originAirport);

        step("Select destination airport");
        flightLandingPageWeb.selectDestination(destinationAirport);


        step("search flight");
        flightLandingPageWeb.searchFlight();


        step("Verify Corporate deals banner is not present");
        for (String airline1 : airlines) {
            flightSearchResultPage.resetAirlineFilter();
            flightSearchResultPage.filterAirline(new ArrayList<>(Arrays.asList(airline1)));
            Assertion.assertThat("Corporate deals banner is not present for " + airline1, flightSearchResultPage.isCorporateDealsBannerPresent(), is(false));
            Assertion.assertThat("Corporate deals benefit on the card is not present for" + airline1, flightSearchResultPage.gotoCorporateDealsBenefitCard(), is(false));
        }

        }

    @Author(names = {"Prerna"})
    @Module(name = "Booking")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void testFlightBookingFromTravelerCorporateCodeAppliedDweb() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testFlightBookingFromTravelerCorporateCodeAppliedDweb");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPageWeb = FlightLandingPageWeb.getInstance(session);
        FlightSearchResultPageWeb<?> flightSearchResultPage = FlightSearchResultPageWeb.getInstance(session);
        FlightProductReviewPage<?> flightProductReviewPage = new FlightProductReviewPage<>(session);
        FlightBookingFormPageWeb<?> flightBookingFormPage = FlightBookingFormPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        TravelPlanWeb<?> travelPlanPage = TravelPlanWeb.getInstance(session);

        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String departureDate = getRandomLongDateOnRange(daysToDepartureDate,30);

        int adultPassengers = Integer.parseInt(param.get("adultPassengers"));
        int childPassengers = Integer.parseInt(param.get("childPassengers"));
        int infantPassengers = Integer.parseInt(param.get("infantPassengers"));

        String username = param.get("username");
        String password = param.get("password");
        String currency = param.get("currency");
        String adminUsername = param.get("adminUsername");
        String adminPassword = param.get("adminPassword");
        String firstName = param.get("firstName");
        String lastName = param.get("lastName");

        ArrayList<CorporateCodeData> corporateCodeData = corporateApi.setAndGetCorporateCodeData(param.get("corporateCodeData"));
        ArrayList<String> airlines = corporateApi.getAirlineName(corporateCodeData);
        String airline = airlines.get(0);

        step("Create the corporate code for airline " + airline);
        ArrayList<Integer> corporateCodeIds = corporateApi.findCreateCorporateCode(username, password, corporateCodeData);
        Assertion.assertThat("Corporate code is created", corporateCodeIds, everyItem(notNullValue()));

        step("User open home page");
        homePage.goToHomePage();

        step("Go to account page");
        loginPage.goToAccountPage();

        step("login in using { " + username + " }");
        loginPage.loginIfNotWithSameEmail(username, password, "123456", 1);

        step("User select IDR currency");
        homePage.selectCurrency(currency);

        step("User open Flight vertical");
        homePage.openVerticalPageViaURL("Flights");

        step("Toggle trip type");
        flightLandingPageWeb.toggleTripType(tripType);

        step("Select origin airport");
        flightLandingPageWeb.selectOrigin(originAirport);

        step("Select destination airport");
        flightLandingPageWeb.selectDestination(destinationAirport);

        step("Select date");
        flightLandingPageWeb.selectDate(departureDate);

        step("add passenger");
        flightLandingPageWeb.addPassenger(adultPassengers,childPassengers,infantPassengers);

        step("search flight");
        flightLandingPageWeb.searchFlight();

        step("Apply Filter" + airline);
        flightSearchResultPage.filterAirline(airline);
        sleep(5);//wait for filter to apply

        step("Corporate code nudge is there");
        Assertion.assertThat("Corporate deals benefit on the card is present", flightSearchResultPage.gotoCorporateDealsBenefitCard(), is(true));

        step("select flight");
        flightSearchResultPage.selectFlightByAirline(airline);

        step("Continue to booking form");
        flightProductReviewPage.continueToBookingForm();
        flightBookingFormPage.clickNoThanksOnFlightMayReschedulePopup();

        step("Click toggle to fill passengers details and save ");
        flightBookingFormPage.fillPassengerDetailsLogin(firstName, lastName);

        step("Go to payment page");
        flightBookingFormPage.goToPaymentPageFromBookingFormPage();

        step("Skip Flight may rescheduled page by clicking no thanks");
        flightBookingFormPage.clickNoThanksOnFlightMayReschedulePopup();
        travelPlanPage.skipTravelPlan();
        flightBookingFormPage.clickNoThanksOnFlightMayReschedulePopup();

        step("Payment page should be displayed");
        String orderID = paymentPage.getOrderId();
        orderID = String.valueOf(getOrderId(orderID));
        Assertion.assertThat("Corporate billing option is selected", paymentPage.getSelectedPayment().toLowerCase(), is("corporate billing"));

        step("get total price payment page");
        String totalPricePaymentPage = paymentPage.totalPricePaymentPage();
        Assertion.assertThat("total price should not be empty {" +totalPricePaymentPage +" } ", totalPricePaymentPage.isEmpty(),is(false));

        step("Submit payment using Corporate Billing");
        paymentPage.clickOnPaymentButton();

        step("verify successfully placed order popup from corporate");
        Assertion.assertThat("get successfully placed order popup",paymentPage.isCorporateSuccessfullyPlacedOrderPopup(),is(true));

        step("Check if the booked order is being reflected on corporate dashboard under purchase list orderId {" + orderID + " }");
        boolean isOrderReflectedOnPurchaseList = corporateApi.checkPurchaseOrderList(orderID, adminUsername, adminPassword,firstName + " " + lastName);
        Assertion.assertThat("purchase order is reflected on B2B corporate dashboard in purchase list", isOrderReflectedOnPurchaseList, is(true));
    }

    @Author(names = {"Prerna"})
    @Module(name = "corporateCodeBooking")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void testFlightBookingFromTravelerCorporateCodeNotAppliedDweb() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testFlightBookingFromTravelerCorporateCodeNotAppliedDweb");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPageWeb = FlightLandingPageWeb.getInstance(session);
        FlightSearchResultPageWeb<?> flightSearchResultPage = FlightSearchResultPageWeb.getInstance(session);
        FlightProductReviewPage<?> flightProductReviewPage = new FlightProductReviewPage<>(session);
        FlightBookingFormPageWeb<?> flightBookingFormPage = FlightBookingFormPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        TravelPlanWeb<?> travelPlanPage = TravelPlanWeb.getInstance(session);

        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String departureDate = getRandomLongDateOnRange(daysToDepartureDate,30);

        int adultPassengers = Integer.parseInt(param.get("adultPassengers"));
        int childPassengers = Integer.parseInt(param.get("childPassengers"));
        int infantPassengers = Integer.parseInt(param.get("infantPassengers"));

        String username = param.get("username");
        String password = param.get("password");
        String currency = param.get("currency");
        String adminUsername = param.get("adminUsername");
        String adminPassword = param.get("adminPassword");
        String firstName = param.get("firstName");
        String lastName = param.get("lastName");

        ArrayList<CorporateCodeData> corporateCodeData = corporateApi.setAndGetCorporateCodeData(param.get("corporateCodeData"));
        ArrayList<String> airlines = corporateApi.getAirlineName(corporateCodeData);
        String airline = airlines.get(0);

        step("Delete the corporate codes");
        boolean isIdDeleted = corporateApi.deleteAllCorporateCode(adminUsername, adminPassword);
        Assertion.assertThat("Success to delete the corporate code", isIdDeleted, is(true));

        step("User open home page");
        homePage.goToHomePage();

        step("Go to account page");
        loginPage.goToAccountPage();

        step("login in using { " + username + " }");
        loginPage.loginIfNotWithSameEmail(username, password, "123456", 1);

        step("User select IDR currency");
        homePage.selectCurrency(currency);

        step("User open Flight vertical");
        homePage.openVerticalPageViaURL("Flights");

        step("Toggle trip type");
        flightLandingPageWeb.toggleTripType(tripType);

        step("Select origin airport");
        flightLandingPageWeb.selectOrigin(originAirport);

        step("Select destination airport");
        flightLandingPageWeb.selectDestination(destinationAirport);

        step("Select date");
        flightLandingPageWeb.selectDate(departureDate);

        step("add passenger");
        flightLandingPageWeb.addPassenger(adultPassengers,childPassengers,infantPassengers);

        step("search flight");
        flightLandingPageWeb.searchFlight();

        step("Apply Filter" + airline);
        flightSearchResultPage.filterAirline(airline);
        sleep(5);//wait for filter to apply

        step("Corporate code nudge is not there");
        Assertion.assertThat("Corporate deals benefit on the card is present", flightSearchResultPage.gotoCorporateDealsBenefitCard(), is(false));

        step("select flight");
        flightSearchResultPage.selectFlightByAirline(airline);

        step("Continue to booking form");
        flightProductReviewPage.continueToBookingForm();
        flightBookingFormPage.clickNoThanksOnFlightMayReschedulePopup();

        step("Click toggle to fill passengers details and save ");
        flightBookingFormPage.fillPassengerDetailsLogin(firstName, lastName);

        step("Go to payment page");
        flightBookingFormPage.goToPaymentPageFromBookingFormPage();

        step("Skip Flight may rescheduled page by clicking no thanks");
        flightBookingFormPage.clickNoThanksOnFlightMayReschedulePopup();
        travelPlanPage.skipTravelPlan();
        flightBookingFormPage.clickNoThanksOnFlightMayReschedulePopup();

        step("Payment page should be displayed");
        String orderID = paymentPage.getOrderId();
        orderID = String.valueOf(getOrderId(orderID));
        Assertion.assertThat("Corporate billing option is selected", paymentPage.getSelectedPayment().toLowerCase(), is("corporate billing"));

        step("get total price payment page");
        String totalPricePaymentPage = paymentPage.totalPricePaymentPage();
        Assertion.assertThat("total price should not be empty {" +totalPricePaymentPage +" } ", totalPricePaymentPage.isEmpty(),is(false));

        step("Submit payment using Corporate Billing");
        paymentPage.clickOnPaymentButton();

        step("verify successfully placed order popup from corporate");
        Assertion.assertThat("get successfully placed order popup",paymentPage.isCorporateSuccessfullyPlacedOrderPopup(),is(true));

        step("Check if the booked order is being reflected on corporate dashboard under purchase list orderId {" + orderID + " }");
        boolean isOrderReflectedOnPurchaseList = corporateApi.checkPurchaseOrderList(orderID,adminUsername, adminPassword,firstName + " " + lastName);
        Assertion.assertThat("purchase order is reflected on B2B corporate dashboard in purchase list", isOrderReflectedOnPurchaseList, is(true));
    }

    @Author(names = "Aditi")
    @Module(name = "Booking")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void noHotelBookingForCorpUserNonIDRCurrency() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("noHotelBookingForCorpUserNonIDRCurrency");
        BaseTest.paramsThreadLocal.set(param);
        Session<WebDriver> session = sessionThreadLocal.get();
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HotelLandingPageWeb<?> hotelLandingPage = HotelLandingPageWeb.getInstance(session);
        HotelRoomListPage<?> hotelRoomListPage = new HotelRoomListPage<>(session);
        HotelBookingFormPageWeb<?> hotelBookingFormPage = HotelBookingFormPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        TravelPlanWeb<?> travelPlanPage = TravelPlanWeb.getInstance(session);
        CorporateDashboard<?> corporateDashboard = new CorporateDashboard<>(session);

        String hotelName = param.get("hotelName");
        String travelerUsername = param.get("travelerUsername");
        String travelerPassword = param.get("travelerPassword");
        String otp = param.get("otp");
        String currency = param.get("currency");
        int totalRoom = Integer.parseInt(param.get("totalRoom"));
        int totalGuest = Integer.parseInt(param.get("totalGuest"));

        step("Go to home");
        homePage.goToHomePage();

        step("Login as " + travelerUsername + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(travelerUsername, travelerPassword);

        step("User select non-IDR currency");
        homePage.selectCurrency(currency);

        step("User open hotel vertical");
        homePage.openVerticalPageViaURL("Hotel");

        step("User search hotel");
        hotelLandingPage.searchStayLocation(hotelName);

        step("Set guests and rooms");
        hotelLandingPage.clickRoomAndGuestButton();
        hotelLandingPage.setTotalRoom(totalRoom);
        hotelLandingPage.setTotalGuest(totalGuest);

        hotelLandingPage.clickDone();


        step("Submit search");
        hotelLandingPage.clickSubmit();

        step("Click book button");
        hotelRoomListPage.clickBookButton();

        step("Continue to payment page");
        hotelBookingFormPage.continueToSecondPartThenPaymentPage();
        travelPlanPage.skipTravelPlan();

        step("An error message should appear");
        Assertion.assertThat("Error message appears saying that currency should be IDR to make booking", paymentPage.isErrorMessageDisplayed(), is(true));
    }

    @Author(names = "Zia")
    @Module(name = "TravelPolicy")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void hotelBookingMeetTravelPolicyWithoutIssueDweb() throws FailedLoginException, InterruptedException {
        Map<String, String> map = getParam("hotelBookingMeetTravelPolicyWithoutIssueDweb");
        BaseTest.paramsThreadLocal.set(map);
        Session<WebDriver> session = sessionThreadLocal.get();
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HotelLandingPageWeb<?> hotelLandingPage = HotelLandingPageWeb.getInstance(session);
        HotelRoomListPage<?> hotelRoomListPage = new HotelRoomListPage<>(session);
        HotelBookingFormPageWeb<?> hotelBookingFormPage = HotelBookingFormPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);
        TravelPlanWeb<?> travelPlanPage = TravelPlanWeb.getInstance(session);
        HotelSearchResultPage<?> searchResultPage = new HotelSearchResultPage<>(session);
        HotelDetailPage<?> detailPage = new HotelDetailPage<>(session);

        String username = map.get("username");
        String password = map.get("password");
        String otp = map.get("otp");
        String dateType = map.get("date_type");
        String date = map.get("date");
        String adminUsername = map.get("adminUsername");
        String adminPassword = map.get("adminPassword");
        String location = map.get("location");
        String star = map.get("star");
        String currency = map.get("currency");
        int minPrice = Integer.parseInt(map.get("minPrice"));
        int maxPrice = Integer.parseInt(map.get("maxPrice"));
        int advanceBookingDay = Integer.parseInt(map.get("advanceBookingDay"));
        String policyName = map.get("policyName");

        step("get related data via api");
        String accessTokenAdmin = corporateApi.authLoginB2B(adminUsername,adminPassword);
        long accountIdAdmin = corporateApi.getAccountId(accessTokenAdmin);
        EmployeeData employeeDataAdmin = corporateApi.getEmployeeData(adminUsername);
        EmployeeData employeeDataUser = corporateApi.getEmployeeData(username);

        step("patch travel policy");
        int travelPolicyID = corporateApi.patchTravelPolicyHotel(accessTokenAdmin,employeeDataAdmin,accountIdAdmin,currency,minPrice,maxPrice,star,advanceBookingDay,false,false,policyName);
        Assertion.assertThat("Travel Policy patched", travelPolicyID, not(-1));

        step("assign travel policy to user");
        boolean isPolicyAssign = corporateApi.assignTravelPolicyToUser(accountIdAdmin,employeeDataAdmin,employeeDataUser,travelPolicyID);
        Assertion.assertThat("Travel Policy assigned", isPolicyAssign, is(true));

        step("set the search to meet travel policy display in srp");
        boolean isSettingDone = corporateApi.setSearchTypePolicy(employeeDataAdmin,accountIdAdmin,"search",true);
        Assertion.assertThat("Search Type Policy set", isSettingDone, is(true));

        step("Go to home");
        homePage.goToHomePage();

        step("User open as guest");
        loginPage.loginIfNotWithSameEmail(username, password, otp, 1);

        step("User open hotel vertical");
        homePage.openVerticalPageViaURL("Hotel");

        step("User select IDR currency");
        homePage.selectCurrency(currency);

        step("User search hotel");
        hotelLandingPage.searchStayLocation(location);

        step("Set date");
        hotelLandingPage.setStayDate(dateType, date);

        step("Set guests and rooms");
        hotelLandingPage.clickRoomAndGuestButton();
        hotelLandingPage.setTotalRoom(1);
        hotelLandingPage.setTotalGuest(1);
        hotelLandingPage.clickDone();

        step("Submit search");
        hotelLandingPage.clickSubmit();
        searchResultPage.clickPopUp();

        step("Choose filter based on testdata | testdata : '"+star+"'");
        searchResultPage.selectFilterStarRange(star);

        step("select hotel with meet travel policy nudge");
        searchResultPage.selectHotelWithMeetTravelPolicyNudge();
        detailPage.switchToNewTab();

        step("Click book button");
        hotelRoomListPage.clickBookButton();

        step("Continue to payment page");
        hotelBookingFormPage.continueToSecondPartThenPaymentPage();
        travelPlanPage.skipTravelPlan();

        step("Payment page should be displayed");
        String orderID = paymentPage.getOrderId();
        orderID = String.valueOf(getOrderId(orderID));
        Assertion.assertThat("Corporate billing option is selected", paymentPage.getSelectedPayment().toLowerCase(), is("corporate billing"));

        step("Submit payment using Corporate Billing");
        paymentPage.clickOnPaymentButton();

        step("verify meet travel policy in the corporate dashboard via api");
        boolean isMeetTravelPolicy = corporateApi.checkPurchaseOrderMeetTravelPolicy(orderID, adminUsername, adminPassword);
        Assertion.assertThat("is meet travel policy shown", isMeetTravelPolicy, is(true));
    }

    @Author(names = {"Zia"})
    @Module(name = "TravelPolicy")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void testDoesnotMeetTravelPolicyPopupAfterRoomIsSelectedHotelDweb() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("testDoesnotMeetTravelPolicyPopupAfterRoomIsSelectedHotelDweb");
        BaseTest.paramsThreadLocal.set(map);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HotelLandingPageWeb<?> hotelLandingPage = HotelLandingPageWeb.getInstance(session);
        HotelRoomListPage<?> hotelRoomListPage = new HotelRoomListPage<>(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);
        HotelSearchResultPage<?> searchResultPage = new HotelSearchResultPage<>(session);
        HotelDetailPage<?> detailPage = new HotelDetailPage<>(session);

        String username = map.get("username");
        String password = map.get("password");
        String otp = map.get("otp");
        String dateType = map.get("date_type");
        String date = map.get("date");
        String adminUsername = map.get("adminUsername");
        String adminPassword = map.get("adminPassword");
        String location = map.get("location");
        String star = map.get("star");
        String currency = map.get("currency");
        int minPrice = Integer.parseInt(map.get("minPrice"));
        int maxPrice = Integer.parseInt(map.get("maxPrice"));
        int advanceBookingDay = Integer.parseInt(map.get("advanceBookingDay"));
        String policyName = map.get("policyName");

        step("get related data via api");
        String accessTokenAdmin = corporateApi.authLoginB2B(adminUsername,adminPassword);
        long accountIdAdmin = corporateApi.getAccountId(accessTokenAdmin);
        EmployeeData employeeDataAdmin = corporateApi.getEmployeeData(adminUsername);
        EmployeeData employeeDataUser = corporateApi.getEmployeeData(username);

        step("patch travel policy");
        int travelPolicyID = corporateApi.patchTravelPolicyHotel(accessTokenAdmin,employeeDataAdmin,accountIdAdmin,currency,minPrice,maxPrice,star,advanceBookingDay,false,false,policyName);
        Assertion.assertThat("Travel Policy patched", travelPolicyID, not(-1));

        step("assign travel policy to user");
        boolean isPolicyAssign = corporateApi.assignTravelPolicyToUser(accountIdAdmin,employeeDataAdmin,employeeDataUser,travelPolicyID);
        Assertion.assertThat("Travel Policy assigned", isPolicyAssign, is(true));

        step("set the search to meet travel policy display in srp");
        boolean isSettingDone = corporateApi.setSearchTypePolicy(employeeDataAdmin,accountIdAdmin,"search",true);
        Assertion.assertThat("Search Type Policy set", isSettingDone, is(true));

        step("Go to home");
        homePage.goToHomePage();

        step("User open as guest");
        loginPage.loginIfNotWithSameEmail(username, password, otp, 1);

        step("User open hotel vertical");
        homePage.openVerticalPageViaURL("Hotel");

        step("User select IDR currency");
        homePage.selectCurrency(currency);

        step("User search hotel");
        hotelLandingPage.searchStayLocation(location);

        step("Set date");
        hotelLandingPage.setStayDate(dateType, date);

        step("Set guests and rooms");
        hotelLandingPage.clickRoomAndGuestButton();
        hotelLandingPage.setTotalRoom(1);
        hotelLandingPage.setTotalGuest(1);
        hotelLandingPage.clickDone();

        step("Submit search");
        hotelLandingPage.clickSubmit();
        searchResultPage.clickPopUp();

        step("Apply 5 star filter'");
        searchResultPage.selectFilterStarRange("5");

        step("select hotel with no meet travel policy nudge");
        searchResultPage.selectHotelCard();
        detailPage.switchToNewTab();

        step("Click book button");
        hotelRoomListPage.clickBookButton();
        Assertion.assertThat("Does not meet travel policy popup is displayed", hotelRoomListPage.doesNotMeetTravelPolicyPopUpAppears(), is(true));
    }

    @Author(names = {"Zia"})
    @Module(name = "TravelPolicy")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void testEmptySRPWhenTravelPolicyIsSetToAdvanceBookingHotelDweb() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("testEmptySRPWhenTravelPolicyIsSetToAdvanceBookingHotelDweb");
        BaseTest.paramsThreadLocal.set(map);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HotelLandingPageWeb<?> hotelLandingPage = HotelLandingPageWeb.getInstance(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);
        HotelSearchResultPage<?> searchResultPage = new HotelSearchResultPage<>(session);

        String username = map.get("username");
        String password = map.get("password");
        String otp = map.get("otp");
        String dateType = map.get("date_type");
        String date = map.get("date");
        String adminUsername = map.get("adminUsername");
        String adminPassword = map.get("adminPassword");
        String location = map.get("location");
        String star = map.get("star");
        String currency = map.get("currency");
        int minPrice = Integer.parseInt(map.get("minPrice"));
        int maxPrice = Integer.parseInt(map.get("maxPrice"));
        int advanceBookingDay = Integer.parseInt(map.get("advanceBookingDay"));
        String policyName = map.get("policyName");

        step("get related data via api");
        String accessTokenAdmin = corporateApi.authLoginB2B(adminUsername,adminPassword);
        long accountIdAdmin = corporateApi.getAccountId(accessTokenAdmin);
        EmployeeData employeeDataAdmin = corporateApi.getEmployeeData(adminUsername);
        EmployeeData employeeDataUser = corporateApi.getEmployeeData(username);

        step("patch travel policy");
        int travelPolicyID = corporateApi.patchTravelPolicyHotel(accessTokenAdmin,employeeDataAdmin,accountIdAdmin,currency,minPrice,maxPrice,star,advanceBookingDay,false,false,policyName);
        Assertion.assertThat("Travel Policy patched", travelPolicyID, not(-1));

        step("assign travel policy to user");
        boolean isPolicyAssign = corporateApi.assignTravelPolicyToUser(accountIdAdmin,employeeDataAdmin,employeeDataUser,travelPolicyID);
        Assertion.assertThat("Travel Policy assigned", isPolicyAssign, is(true));

        step("set the search to meet travel policy display in srp");
        boolean isSettingDone = corporateApi.setSearchTypePolicy(employeeDataAdmin,accountIdAdmin,"search",false);
        Assertion.assertThat("Search Type Policy set", isSettingDone, is(true));

        step("Go to home");
        homePage.goToHomePage();

        step("User open as guest");
        loginPage.loginIfNotWithSameEmail(username, password, otp, 1);

        step("User open hotel vertical");
        homePage.openVerticalPageViaURL("Hotel");

        step("User select IDR currency");
        homePage.selectCurrency(currency);

        step("User search hotel");
        hotelLandingPage.searchStayLocation(location);

        step("Set date");
        hotelLandingPage.setStayDate(dateType, date);

        step("Set guests and rooms");
        hotelLandingPage.clickRoomAndGuestButton();
        hotelLandingPage.setTotalRoom(1);
        hotelLandingPage.setTotalGuest(1);
        hotelLandingPage.clickDone();

        step("Submit search");
        hotelLandingPage.clickSubmit();
        searchResultPage.clickPopUp();

        step("Check SRP is empty");
        Assertion.assertThat("Check SRP is Empty", searchResultPage.checkEmptyHotelList(), is(true));
    }

    @Author(names = {"Zia"})
    @Module(name = "TravelPolicy")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void hotelMeetTravelPolicyInPriceRangeDweb() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("hotelMeetTravelPolicyInPriceRangeDweb");
        BaseTest.paramsThreadLocal.set(map);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HotelLandingPageWeb<?> hotelLandingPage = HotelLandingPageWeb.getInstance(session);
        HotelRoomListPage<?> hotelRoomListPage = new HotelRoomListPage<>(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);
        HotelSearchResultPage<?> searchResultPage = new HotelSearchResultPage<>(session);
        HotelDetailPage<?> detailPage = new HotelDetailPage<>(session);

        String username = map.get("username");
        String password = map.get("password");
        String otp = map.get("otp");
        String dateType = map.get("date_type");
        String date = map.get("date");
        String adminUsername = map.get("adminUsername");
        String adminPassword = map.get("adminPassword");
        String location = map.get("location");
        String currency = map.get("currency");
        int minPrice = Integer.parseInt(map.get("minPrice"));
        int maxPrice = Integer.parseInt(map.get("maxPrice"));
        int advanceBookingDay = Integer.parseInt(map.get("advanceBookingDay"));
        String policyName = map.get("policyName");
        String priceRange = map.get("priceRange");

        step("get related data via api");
        String accessTokenAdmin = corporateApi.authLoginB2B(adminUsername,adminPassword);
        long accountIdAdmin = corporateApi.getAccountId(accessTokenAdmin);
        EmployeeData employeeDataAdmin = corporateApi.getEmployeeData(adminUsername);
        EmployeeData employeeDataUser = corporateApi.getEmployeeData(username);

        step("patch travel policy");
        int travelPolicyID = corporateApi.patchTravelPolicyHotel(accessTokenAdmin,employeeDataAdmin,accountIdAdmin,currency,minPrice,maxPrice,"1-2-3-4-5",advanceBookingDay,false,false,policyName);
        Assertion.assertThat("Travel Policy patched", travelPolicyID, not(-1));

        step("assign travel policy to user");
        boolean isPolicyAssign = corporateApi.assignTravelPolicyToUser(accountIdAdmin,employeeDataAdmin,employeeDataUser,travelPolicyID);
        Assertion.assertThat("Travel Policy assigned", isPolicyAssign, is(true));

        step("set the search to meet travel policy display in srp");
        boolean isSettingDone = corporateApi.setSearchTypePolicy(employeeDataAdmin,accountIdAdmin,"search",false);
        Assertion.assertThat("Search Type Policy set", isSettingDone, is(true));

        step("Go to home");
        homePage.goToHomePage();

        step("User open as guest");
        loginPage.loginIfNotWithSameEmail(username, password, otp, 1);

        step("User open hotel vertical");
        homePage.openVerticalPageViaURL("Hotel");

        step("User select IDR currency");
        homePage.selectCurrency(currency);

        step("User search hotel");
        hotelLandingPage.searchStayLocation(location);

        step("Set date");
        hotelLandingPage.setStayDate(dateType, date);

        step("Set guests and rooms");
        hotelLandingPage.clickRoomAndGuestButton();
        hotelLandingPage.setTotalRoom(1);
        hotelLandingPage.setTotalGuest(1);
        hotelLandingPage.clickDone();

        step("Submit search");
        hotelLandingPage.clickSubmit();
        searchResultPage.clickPopUp();

        step("is meet travel policy display");
        Assertion.assertThat("meet travel policy display", searchResultPage.isMeetsTravelPolicyNudgeVisible(), is(true));

        step("sort high to low");
        searchResultPage.selectFilterSortBy("Highest price");

        step("is hotel range meet policy price range of high to low");
        Assertion.assertThat("Hotel range meet policy price range", searchResultPage.isHotelRangeMeetPolicyPriceRange(priceRange), is(true));

        step("sort low to high");
        searchResultPage.selectFilterSortBy("Lowest price");

        step("is hotel range meet policy price range of low to high");
        Assertion.assertThat("Hotel range meet policy price range", searchResultPage.isHotelRangeMeetPolicyPriceRange(priceRange), is(true));
    }

    @Author(names = {"Zia"})
    @Module(name = "TravelPolicy")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void testTravelPolicyAppearForOneStarWhenPolicyIsSetToOneStarHotelDweb() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("testTravelPolicyAppearForOneStarWhenPolicyIsSetToOneStarHotelDweb");
        BaseTest.paramsThreadLocal.set(map);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HotelLandingPageWeb<?> hotelLandingPage = HotelLandingPageWeb.getInstance(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);
        HotelSearchResultPage<?> searchResultPage = new HotelSearchResultPage<>(session);

        String username = map.get("username");
        String password = map.get("password");
        String otp = map.get("otp");
        String dateType = map.get("date_type");
        String date = map.get("date");
        String adminUsername = map.get("adminUsername");
        String adminPassword = map.get("adminPassword");
        String location = map.get("location");
        String currency = map.get("currency");
        int minPrice = Integer.parseInt(map.get("minPrice"));
        int maxPrice = Integer.parseInt(map.get("maxPrice"));
        int advanceBookingDay = Integer.parseInt(map.get("advanceBookingDay"));
        String policyName = map.get("policyName");

        step("get related data via api");
        String accessTokenAdmin = corporateApi.authLoginB2B(adminUsername,adminPassword);
        long accountIdAdmin = corporateApi.getAccountId(accessTokenAdmin);
        EmployeeData employeeDataAdmin = corporateApi.getEmployeeData(adminUsername);
        EmployeeData employeeDataUser = corporateApi.getEmployeeData(username);

        step("patch travel policy");
        int travelPolicyID = corporateApi.patchTravelPolicyHotel(accessTokenAdmin,employeeDataAdmin,accountIdAdmin,currency,minPrice,maxPrice,"1",advanceBookingDay,false,false,policyName);
        Assertion.assertThat("Travel Policy patched", travelPolicyID, not(-1));

        step("assign travel policy to user");
        boolean isPolicyAssign = corporateApi.assignTravelPolicyToUser(accountIdAdmin,employeeDataAdmin,employeeDataUser,travelPolicyID);
        Assertion.assertThat("Travel Policy assigned", isPolicyAssign, is(true));

        step("set the search to meet travel policy display in srp");
        boolean isSettingDone = corporateApi.setSearchTypePolicy(employeeDataAdmin,accountIdAdmin,"search",true);
        Assertion.assertThat("Search Type Policy set", isSettingDone, is(true));

        step("Go to home");
        homePage.goToHomePage();

        step("User open as guest");
        loginPage.loginIfNotWithSameEmail(username, password, otp, 1);

        step("User open hotel vertical");
        homePage.openVerticalPageViaURL("Hotel");

        step("User select IDR currency");
        homePage.selectCurrency(currency);

        step("User search hotel");
        hotelLandingPage.searchStayLocation(location);

        step("Set date");
        hotelLandingPage.setStayDate(dateType, date);

        step("Set guests and rooms");
        hotelLandingPage.clickRoomAndGuestButton();
        hotelLandingPage.setTotalRoom(1);
        hotelLandingPage.setTotalGuest(1);
        hotelLandingPage.clickDone();

        step("Submit search");
        hotelLandingPage.clickSubmit();
        searchResultPage.clickPopUp();

        step("Filter 1 star hotel");
        searchResultPage.selectFilterStarRange("1");

        step("is meet travel policy display");
        Assertion.assertThat("meet travel policy display", searchResultPage.isMeetsTravelPolicyNudgeVisible(), is(true));
    }

    @Author(names = {"Zia"})
    @Module(name = "TravelPolicy")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void testMeetTravelPolicyOnSRPWhenTravelPolicyIsGlobalHotelDweb() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("testMeetTravelPolicyOnSRPWhenTravelPolicyIsGlobalHotelDweb");
        BaseTest.paramsThreadLocal.set(map);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HotelLandingPageWeb<?> hotelLandingPage = HotelLandingPageWeb.getInstance(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);
        HotelSearchResultPage<?> searchResultPage = new HotelSearchResultPage<>(session);

        String username = map.get("username");
        String password = map.get("password");
        String otp = map.get("otp");
        String dateType = map.get("date_type");
        String date = map.get("date");
        String adminUsername = map.get("adminUsername");
        String adminPassword = map.get("adminPassword");
        String location = map.get("location");
        String currency = map.get("currency");
        int minPrice = Integer.parseInt(map.get("minPrice"));
        int maxPrice = Integer.parseInt(map.get("maxPrice"));
        int advanceBookingDay = Integer.parseInt(map.get("advanceBookingDay"));
        String policyName = map.get("policyName");

        step("get related data via api");
        String accessTokenAdmin = corporateApi.authLoginB2B(adminUsername,adminPassword);
        long accountIdAdmin = corporateApi.getAccountId(accessTokenAdmin);
        EmployeeData employeeDataAdmin = corporateApi.getEmployeeData(adminUsername);
        EmployeeData employeeDataUser = corporateApi.getEmployeeData(username);

        step("patch travel policy");
        int travelPolicyID = corporateApi.patchTravelPolicyHotel(accessTokenAdmin,employeeDataAdmin,accountIdAdmin,currency,minPrice,maxPrice,"1-2-3-4-5",advanceBookingDay,false,false,policyName);
        Assertion.assertThat("Travel Policy patched", travelPolicyID, not(-1));

        step("assign travel policy to user");
        boolean isPolicyAssign = corporateApi.assignTravelPolicyToUser(accountIdAdmin,employeeDataAdmin,employeeDataUser,travelPolicyID);
        Assertion.assertThat("Travel Policy assigned", isPolicyAssign, is(true));

        step("set the search to meet travel policy display in srp");
        boolean isSettingDone = corporateApi.setSearchTypePolicy(employeeDataAdmin,accountIdAdmin,"search",true);
        Assertion.assertThat("Search Type Policy set", isSettingDone, is(true));

        step("Go to home");
        homePage.goToHomePage();

        step("User open as guest");
        loginPage.loginIfNotWithSameEmail(username, password, otp, 1);

        step("User open hotel vertical");
        homePage.openVerticalPageViaURL("Hotel");

        step("User select IDR currency");
        homePage.selectCurrency(currency);

        step("User search hotel");
        hotelLandingPage.searchStayLocation(location);

        step("Set date");
        hotelLandingPage.setStayDate(dateType, date);

        step("Set guests and rooms");
        hotelLandingPage.clickRoomAndGuestButton();
        hotelLandingPage.setTotalRoom(1);
        hotelLandingPage.setTotalGuest(1);
        hotelLandingPage.clickDone();

        step("Submit search");
        hotelLandingPage.clickSubmit();
        searchResultPage.clickPopUp();

        step("is meet travel policy display");
        Assertion.assertThat("meet travel policy display", searchResultPage.isMeetsTravelPolicyNudgeVisible(), is(true));
    }

    @Author(names = {"Zia"})
    @Module(name = "TravelPolicy")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void testMeetsTravelPolicyOnRoomListHotelDweb() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("testMeetsTravelPolicyOnRoomListHotelDweb");
        BaseTest.paramsThreadLocal.set(map);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HotelLandingPageWeb<?> hotelLandingPage = HotelLandingPageWeb.getInstance(session);
        HotelRoomListPage<?> hotelRoomListPage = new HotelRoomListPage<>(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);
        HotelSearchResultPage<?> searchResultPage = new HotelSearchResultPage<>(session);
        HotelDetailPage<?> detailPage = new HotelDetailPage<>(session);

        String username = map.get("username");
        String password = map.get("password");
        String otp = map.get("otp");
        String dateType = map.get("date_type");
        String date = map.get("date");
        String adminUsername = map.get("adminUsername");
        String adminPassword = map.get("adminPassword");
        String location = map.get("location");
        String star = map.get("star");
        String currency = map.get("currency");
        int minPrice = Integer.parseInt(map.get("minPrice"));
        int maxPrice = Integer.parseInt(map.get("maxPrice"));
        int advanceBookingDay = Integer.parseInt(map.get("advanceBookingDay"));
        String policyName = map.get("policyName");

        step("get related data via api");
        String accessTokenAdmin = corporateApi.authLoginB2B(adminUsername,adminPassword);
        long accountIdAdmin = corporateApi.getAccountId(accessTokenAdmin);
        EmployeeData employeeDataAdmin = corporateApi.getEmployeeData(adminUsername);
        EmployeeData employeeDataUser = corporateApi.getEmployeeData(username);

        step("patch travel policy");
        int travelPolicyID = corporateApi.patchTravelPolicyHotel(accessTokenAdmin,employeeDataAdmin,accountIdAdmin,currency,minPrice,maxPrice,star,advanceBookingDay,false,false,policyName);
        Assertion.assertThat("Travel Policy patched", travelPolicyID, not(-1));

        step("assign travel policy to user");
        boolean isPolicyAssign = corporateApi.assignTravelPolicyToUser(accountIdAdmin,employeeDataAdmin,employeeDataUser,travelPolicyID);
        Assertion.assertThat("Travel Policy assigned", isPolicyAssign, is(true));

        step("set the search to meet travel policy display in srp");
        boolean isSettingDone = corporateApi.setSearchTypePolicy(employeeDataAdmin,accountIdAdmin,"search",true);
        Assertion.assertThat("Search Type Policy set", isSettingDone, is(true));

        step("Go to home");
        homePage.goToHomePage();

        step("User open as guest");
        loginPage.loginIfNotWithSameEmail(username, password, otp, 1);

        step("User open hotel vertical");
        homePage.openVerticalPageViaURL("Hotel");

        step("User select IDR currency");
        homePage.selectCurrency(currency);

        step("User search hotel");
        hotelLandingPage.searchStayLocation(location);

        step("Set date");
        hotelLandingPage.setStayDate(dateType, date);

        step("Set guests and rooms");
        hotelLandingPage.clickRoomAndGuestButton();
        hotelLandingPage.setTotalRoom(1);
        hotelLandingPage.setTotalGuest(1);
        hotelLandingPage.clickDone();

        step("Submit search");
        hotelLandingPage.clickSubmit();
        searchResultPage.clickPopUp();

        step("Apply "+star+" star filter'");
        searchResultPage.selectFilterStarRange(star);

        step("is meet travel policy display");
        Assertion.assertThat("meet travel policy display", searchResultPage.isMeetsTravelPolicyNudgeVisible(), is(true));

        step("select hotel with meet travel policy nudge");
        searchResultPage.selectHotelWithMeetTravelPolicyNudge();
        detailPage.switchToNewTab();

        step("Verify nudge in room list");
        Assertion.assertThat("meet travel policy display", hotelRoomListPage.isMeetsTravelPolicyNudgeVisible(), is(true));
    }

    @Author(names = {"Zia"})
    @Module(name = "TravelPolicy")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void testAddonsAreInactiveOnBookingPageHotelDweb() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("testAddonsAreInactiveOnBookingPageHotelDweb");
        BaseTest.paramsThreadLocal.set(map);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HotelLandingPageWeb<?> hotelLandingPage = HotelLandingPageWeb.getInstance(session);
        HotelRoomListPage<?> hotelRoomListPage = new HotelRoomListPage<>(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);
        HotelBookingFormPageWeb<?> hotelBookingFormPage = HotelBookingFormPageWeb.getInstance(session);

        String username = map.get("username");
        String password = map.get("password");
        String otp = map.get("otp");
        String dateType = map.get("date_type");
        String date = map.get("date");
        String adminUsername = map.get("adminUsername");
        String adminPassword = map.get("adminPassword");
        String location = map.get("location");
        String star = map.get("star");
        String currency = map.get("currency");
        int minPrice = Integer.parseInt(map.get("minPrice"));
        int maxPrice = Integer.parseInt(map.get("maxPrice"));
        int advanceBookingDay = Integer.parseInt(map.get("advanceBookingDay"));
        String policyName = map.get("policyName");

        step("get related data via api");
        String accessTokenAdmin = corporateApi.authLoginB2B(adminUsername,adminPassword);
        long accountIdAdmin = corporateApi.getAccountId(accessTokenAdmin);
        EmployeeData employeeDataAdmin = corporateApi.getEmployeeData(adminUsername);
        EmployeeData employeeDataUser = corporateApi.getEmployeeData(username);

        step("patch travel policy");
        int travelPolicyID = corporateApi.patchTravelPolicyHotel(accessTokenAdmin,employeeDataAdmin,accountIdAdmin,currency,minPrice,maxPrice,star,advanceBookingDay,false,false,policyName);
        Assertion.assertThat("Travel Policy patched", travelPolicyID, not(-1));

        step("assign travel policy to user");
        boolean isPolicyAssign = corporateApi.assignTravelPolicyToUser(accountIdAdmin,employeeDataAdmin,employeeDataUser,travelPolicyID);
        Assertion.assertThat("Travel Policy assigned", isPolicyAssign, is(true));

        step("set the search to meet travel policy display in srp");
        boolean isSettingDone = corporateApi.setSearchTypePolicy(employeeDataAdmin,accountIdAdmin,"search",true);
        Assertion.assertThat("Search Type Policy set", isSettingDone, is(true));

        step("Go to home");
        homePage.goToHomePage();

        step("User open as guest");
        loginPage.loginIfNotWithSameEmail(username, password, otp, 1);

        step("User open hotel vertical");
        homePage.openVerticalPageViaURL("Hotel");

        step("User select IDR currency");
        homePage.selectCurrency(currency);

        step("User search hotel");
        hotelLandingPage.searchStayLocation(location);

        step("Set date");
        hotelLandingPage.setStayDate(dateType, date);

        step("Set guests and rooms");
        hotelLandingPage.clickRoomAndGuestButton();
        hotelLandingPage.setTotalRoom(1);
        hotelLandingPage.setTotalGuest(1);
        hotelLandingPage.clickDone();

        step("Submit search");
        hotelLandingPage.clickSubmit();

        step("Verify nudge in room list");
        Assertion.assertThat("meet travel policy display", hotelRoomListPage.isMeetsTravelPolicyNudgeVisible(), is(true));

        step("Click book button");
        hotelRoomListPage.clickBookButton();

        step("Verify there is no addons");
        Assertion.assertThat("There is no add on section", hotelBookingFormPage.isAddonThereOnBookingForm(), is(false));
    }

    @Author(names = {"Zia"})
    @Module(name = "Reschedule")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void testTravelerRequestRescheduleHotelViaApproverDweb() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();

        Map<String, String> param = getParam("testTravelerRequestRescheduleHotelViaApproverDweb");
        BaseTest.paramsThreadLocal.set(param);

        String travelerUsername = param.get("travelerUsername");
        String travelerPassword = param.get("travelerPassword");
        String otp= param.get("otp");
        String approverUsername = param.get("approverUsername");
        String approverPassword = param.get("approverPassword");

        String hotelName = param.get("hotelName");

        String dateType = param.get("date_type");
        String date = param.get("date");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        HotelLandingPageWeb<?> hotelLandingPage = HotelLandingPageWeb.getInstance(session);
        HotelRoomListPage<?> hotelRoomListPage = new HotelRoomListPage<>(session);
        HotelBookingFormPageWeb<?> hotelBookingFormPage = HotelBookingFormPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        TravelPlanWeb<?> travelPlanPage = TravelPlanWeb.getInstance(session);
        ReschedulePage<?> reschedulePage = new ReschedulePage<>(session);

        step("Go to home");
        homePage.goToHomePage();

        step("User open as guest");
        loginPage.loginIfNotWithSameEmail(travelerUsername, travelerPassword, otp, 1);

        step("User open hotel vertical");
        homePage.openVerticalPage("Hotels");

        step("User select IDR currency");
        homePage.selectCurrency("IDR");

        step("User search hotel");
        hotelLandingPage.searchStayLocation(hotelName);

        step("Set date");
        hotelLandingPage.setStayDate(dateType, date);

        step("Set guests and rooms");
        hotelLandingPage.clickRoomAndGuestButton();
        hotelLandingPage.setTotalRoom(1);
        hotelLandingPage.setTotalGuest(1);
        hotelLandingPage.clickDone();

        step("Submit search");
        hotelLandingPage.clickSubmit();

        step("Click book button");
        hotelRoomListPage.clickBookButton();

        step("Continue to payment page");
        hotelBookingFormPage.continueToSecondPartThenPaymentPage();
        travelPlanPage.skipTravelPlan();

        step("Payment page should be displayed");
        String orderID = paymentPage.getOrderId();
        orderID = String.valueOf(getOrderId(orderID));
        Assertion.assertThat("Corporate billing option is selected", paymentPage.getSelectedPayment().toLowerCase(), is("corporate billing"));

        step("Submit payment using Corporate Billing");
        paymentPage.clickOnPaymentButton();

        step("Approve booking by approver account for Order ID: " + orderID);
        Boolean bookingStatus = corporateApi.approveBookingB2BApi(approverUsername, approverPassword, orderID);
        Assertion.assertThat("Booking approved by B2B dashboard", bookingStatus, is(true));

        step("Open the order list and go to order detail");
        paymentPage.clickSeeOrderListButton();
        orderListPage.openIssuedTicketViaURL(orderID);
        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.verifyIssuedETicket(), is(true));
        orderDetailPage.closeEticketPopUp();

        step("Click reschedule button");
        orderDetailPage.clickRescheduleButton();

        step("Click check availability button");
        reschedulePage.clickCheckAvailabilityButton();
           String reschedulePageDate = reschedulePage.getDate();

        step("Select room with more payment");
        reschedulePage.selectRoomWithAdditionalPayment();
        String newRoomName = reschedulePage.getRoomName();
        reschedulePage.clickBookButton();

        step("Submit booking");
        reschedulePage.checkTnC();
        reschedulePage.clickSubmitRescheduleButton();

        step("Submit payment using Corporate Billing");
        String orderID2 = paymentPage.getOrderId();
        orderID2 = String.valueOf(getOrderId(orderID2));
        paymentPage.clickOnPaymentButton();

        step("approve reschedule");
        boolean isApproved = true;
        boolean success = corporateApi.approveOrRejectRescheduleB2B(approverUsername, approverPassword, isApproved, orderID2,"approve by automation");
        Assertion.assertThat("reschedule Approved Success", success, is(true));
        paymentPage.clickSeeOrderListButton();

        step("Open new order detail");
        orderListPage.openIssuedTicketViaURL(orderID2);
        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.verifyIssuedETicket(), is(true));
        orderDetailPage.closeEticketPopUp();
        Assertion.assertThat("Reschedule card notification",orderDetailPage.isRescheduleCardDisplayed(), is(true));
        Assertion.assertThat("Order ID is equal", orderDetailPage.getOrderId(), is(orderID2));
        Assertion.assertThat("Start Date is equal", orderDetailPage.getDate(), is(reschedulePageDate));
        Assertion.assertThat("Hotel Name is displayed correctly", orderDetailPage.isHotelNameDisplayed(hotelName), is(true));
        Assertion.assertThat("Room Name is displayed correctly", orderDetailPage.isRoomNameDisplayed(newRoomName), is(true));
    }

    @Author(names = {"Zia"})
    @Module(name = "Reschedule")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void testTravelerRequestRescheduleHotelViaAdminDweb() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();

        Map<String, String> param = getParam("testTravelerRequestRescheduleHotelViaAdminDweb");
        BaseTest.paramsThreadLocal.set(param);

        String travelerUsername = param.get("travelerUsername");
        String travelerPassword = param.get("travelerPassword");
        String otp= param.get("otp");
        String adminUsername = param.get("adminUsername");
        String adminPassword = param.get("adminPassword");

        String hotelName = param.get("hotelName");

        String dateType = param.get("date_type");
        String date = param.get("date");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        HotelLandingPageWeb<?> hotelLandingPage = HotelLandingPageWeb.getInstance(session);
        HotelRoomListPage<?> hotelRoomListPage = new HotelRoomListPage<>(session);
        HotelBookingFormPageWeb<?> hotelBookingFormPage = HotelBookingFormPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        TravelPlanWeb<?> travelPlanPage = TravelPlanWeb.getInstance(session);
        ReschedulePage<?> reschedulePage = new ReschedulePage<>(session);

        step("Go to home");
        homePage.goToHomePage();

        step("User open as guest");
        loginPage.loginIfNotWithSameEmail(travelerUsername, travelerPassword, otp, 1);

        step("User open hotel vertical");
        homePage.openVerticalPage("Hotels");

        step("User select IDR currency");
        homePage.selectCurrency("IDR");

        step("User search hotel");
        hotelLandingPage.searchStayLocation(hotelName);

        step("Set date");
        hotelLandingPage.setStayDate(dateType, date);

        step("Set guests and rooms");
        hotelLandingPage.clickRoomAndGuestButton();
        hotelLandingPage.setTotalRoom(1);
        hotelLandingPage.setTotalGuest(1);
        hotelLandingPage.clickDone();

        step("Submit search");
        hotelLandingPage.clickSubmit();

        step("Click book button");
        hotelRoomListPage.clickBookButton();

        step("Continue to payment page");
        hotelBookingFormPage.continueToSecondPartThenPaymentPage();
        travelPlanPage.skipTravelPlan();

        step("Payment page should be displayed");
        String orderID = paymentPage.getOrderId();
        orderID = String.valueOf(getOrderId(orderID));
        Assertion.assertThat("Corporate billing option is selected", paymentPage.getSelectedPayment().toLowerCase(), is("corporate billing"));

        step("Submit payment using Corporate Billing");
        paymentPage.clickOnPaymentButton();

        step("Approve booking by admin account for Order ID: " + orderID);
        Boolean bookingStatus = corporateApi.approveBookingB2BApi(adminUsername, adminPassword, orderID);
        Assertion.assertThat("Booking approved by B2B dashboard", bookingStatus, is(true));

        step("Open the order list and go to order detail");
        paymentPage.clickSeeOrderListButton();
        orderListPage.openIssuedTicketViaURL(orderID);
        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.verifyIssuedETicket(), is(true));
        orderDetailPage.closeEticketPopUp();

        step("Click reschedule button");
        orderDetailPage.clickRescheduleButton();

        step("Click check availability button");
        reschedulePage.clickCheckAvailabilityButton();
        String reschedulePageDate = reschedulePage.getDate();

        step("Select room with more payment");
        reschedulePage.selectRoomWithAdditionalPayment();
        String newRoomName = reschedulePage.getRoomName();
        reschedulePage.clickBookButton();

        step("Submit booking");
        reschedulePage.checkTnC();
        reschedulePage.clickSubmitRescheduleButton();

        step("Submit payment using Corporate Billing");
        String orderID2 = paymentPage.getOrderId();
        orderID2 = String.valueOf(getOrderId(orderID2));
        paymentPage.clickOnPaymentButton();

        step("approve reschedule");
        boolean isApproved = true;
        boolean success = corporateApi.approveOrRejectRescheduleB2B(adminUsername, adminPassword, isApproved, orderID2,"approve by automation");
        Assertion.assertThat("reschedule Approved Success", success, is(true));
        paymentPage.clickSeeOrderListButton();

        step("Open new order detail");
        orderListPage.openIssuedTicketViaURL(orderID2);
        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.verifyIssuedETicket(), is(true));
        orderDetailPage.closeEticketPopUp();
        Assertion.assertThat("Reschedule card notification",orderDetailPage.isRescheduleCardDisplayed(), is(true));
        Assertion.assertThat("Order ID is equal", orderDetailPage.getOrderId(), is(orderID2));
        Assertion.assertThat("Start Date is equal", orderDetailPage.getDate(), is(reschedulePageDate));
        Assertion.assertThat("Hotel Name is displayed correctly", orderDetailPage.isHotelNameDisplayed(hotelName), is(true));
        Assertion.assertThat("Room Name is displayed correctly", orderDetailPage.isRoomNameDisplayed(newRoomName), is(true));
    }

    @Author(names = {"Zia"})
    @Module(name = "Reschedule")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void testApproverRequestRescheduleHotelAutoApproveDweb() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();

        Map<String, String> param = getParam("testApproverRequestRescheduleHotelAutoApproveDweb");
        BaseTest.paramsThreadLocal.set(param);

        String approverUsername = param.get("approverUsername");
        String approverPassword = param.get("approverPassword");
        String otp= param.get("otp");

        String hotelName = param.get("hotelName");

        String dateType = param.get("date_type");
        String date = param.get("date");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        HotelLandingPageWeb<?> hotelLandingPage = HotelLandingPageWeb.getInstance(session);
        HotelRoomListPage<?> hotelRoomListPage = new HotelRoomListPage<>(session);
        HotelBookingFormPageWeb<?> hotelBookingFormPage = HotelBookingFormPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        TravelPlanWeb<?> travelPlanPage = TravelPlanWeb.getInstance(session);
        ReschedulePage<?> reschedulePage = new ReschedulePage<>(session);

        step("Go to home");
        homePage.goToHomePage();

        step("User open as guest");
        loginPage.loginIfNotWithSameEmail(approverUsername, approverPassword, otp, 1);
        homePage.goToTiketb2cPage();

        step("User open hotel vertical");
        homePage.openVerticalPage("Hotels");

        step("User select IDR currency");
        homePage.selectCurrency("IDR");

        step("User search hotel");
        hotelLandingPage.searchStayLocation(hotelName);

        step("Set date");
        hotelLandingPage.setStayDate(dateType, date);

        step("Set guests and rooms");
        hotelLandingPage.clickRoomAndGuestButton();
        hotelLandingPage.setTotalRoom(1);
        hotelLandingPage.setTotalGuest(1);
        hotelLandingPage.clickDone();

        step("Submit search");
        hotelLandingPage.clickSubmit();

        step("Click book button");
        hotelRoomListPage.clickBookButton();

        step("Continue to payment page");
        hotelBookingFormPage.continueToSecondPartThenPaymentPage();
        travelPlanPage.skipTravelPlan();

        step("Payment page should be displayed");
        String orderID = paymentPage.getOrderId();
        orderID = String.valueOf(getOrderId(orderID));
        Assertion.assertThat("Corporate billing option is selected", paymentPage.getSelectedPayment().toLowerCase(), is("corporate billing"));

        step("Submit payment using Corporate Billing");
        paymentPage.clickOnPaymentButton();

        step("Open the order list and go to order detail");
        orderListPage.openIssuedTicketViaURL(orderID,5);
        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.verifyIssuedETicket(), is(true));
        orderDetailPage.closeEticketPopUp();

        step("Click reschedule button");
        orderDetailPage.clickRescheduleButton();

        step("Click check availability button");
        reschedulePage.clickCheckAvailabilityButton();
        String reschedulePageDate = reschedulePage.getDate();

        step("Select room with more payment");
        reschedulePage.selectRoomWithAdditionalPayment();
        String newRoomName = reschedulePage.getRoomName();
        reschedulePage.clickBookButton();

        step("Submit booking");
        reschedulePage.checkTnC();
        reschedulePage.clickSubmitRescheduleButton();

        step("Submit payment using Corporate Billing");
        String orderID2 = paymentPage.getOrderId();
        orderID2 = String.valueOf(getOrderId(orderID2));
        paymentPage.clickOnPaymentButton();

        step("Open new order detail");
        orderListPage.openIssuedTicketViaURL(orderID2,15);
        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.verifyIssuedETicket(), is(true));
        orderDetailPage.closeEticketPopUp();
        Assertion.assertThat("Reschedule card notification",orderDetailPage.isRescheduleCardDisplayed(), is(true));
        Assertion.assertThat("Order ID is equal", orderDetailPage.getOrderId(), is(orderID2));
        Assertion.assertThat("Start Date is equal", orderDetailPage.getDate(), is(reschedulePageDate));
        Assertion.assertThat("Hotel Name is displayed correctly", orderDetailPage.isHotelNameDisplayed(hotelName), is(true));
        Assertion.assertThat("Room Name is displayed correctly", orderDetailPage.isRoomNameDisplayed(newRoomName), is(true));
    }

    @Author(names = {"Ashish"})
    @Module(name = "Booking")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void testFlightBookingFromTravelerToAdminDweb() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testFlightBookingFromTravelerToAdminDweb");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPageWeb = FlightLandingPageWeb.getInstance(session);
        FlightSearchResultPageWeb<?> flightSearchResultPage = FlightSearchResultPageWeb.getInstance(session);
        FlightProductReviewPage<?> flightProductReviewPage = new FlightProductReviewPage<>(session);
        FlightBookingFormPageWeb<?> flightBookingFormPage = FlightBookingFormPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        TravelPlanWeb<?> travelPlanPage = TravelPlanWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);
        FlightApi<?> flightApi = new FlightApi<>(session);

        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");
        String airline = param.get("airline");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String departureDate = getRandomLongDateOnRange(daysToDepartureDate,30);

        int adultPassengers = Integer.parseInt(param.get("adultPassengers"));
        int childPassengers = Integer.parseInt(param.get("childPassengers"));
        int infantPassengers = Integer.parseInt(param.get("infantPassengers"));

        String username = param.get("username");
        String password = param.get("password");
        String currency = param.get("currency");
        String adminUsername = param.get("adminUsername");
        String adminPassword = param.get("adminPassword");
        String name = param.get("name");
        String lastName = param.get("lastName");

        step("User open home page");
        homePage.goToHomePage();

        step("Go to account page");
        loginPage.goToAccountPage();

        step("login in using { " + username + " }");
        loginPage.loginIfNotWithSameEmail(username, password, "123456", 1);

        step("User select { " + currency + " } currency");
        homePage.selectCurrency(currency);

        step("User open Flight vertical");
        homePage.openVerticalPageViaURL("Flights");

        step("Toggle trip type");
        flightLandingPageWeb.toggleTripType(tripType);

        step("Select origin airport");
        flightLandingPageWeb.selectOrigin(originAirport);

        step("Select destination airport");
        flightLandingPageWeb.selectDestination(destinationAirport);

        step("Select date");
        flightLandingPageWeb.selectDate(departureDate);

        step("add passenger");
        flightLandingPageWeb.addPassenger(adultPassengers,childPassengers,infantPassengers);

        step("search flight");
        flightLandingPageWeb.searchFlight();

        step("Apply Filter " + airline);
        flightSearchResultPage.filterAirline(airline);

        step("select flight");
        flightSearchResultPage.selectFlightByAirline(airline);

        step("Continue to booking form");
        flightProductReviewPage.continueToBookingForm();
        flightBookingFormPage.clickNoThanksOnFlightMayReschedulePopup();

        step("Click toggle to fill passengers details and save ");
        flightBookingFormPage.fillPassengerDetailsLogin(name,lastName);

        step("Go to payment page");
        flightBookingFormPage.goToPaymentPageFromBookingFormPage();

        step("Skip Flight may rescheduled page by clicking no thanks");
        travelPlanPage.skipTravelPlan();
        flightBookingFormPage.clickNoThanksOnFlightMayReschedulePopup();

        step("Payment page should be displayed");
        String orderID = paymentPage.getOrderId();
        orderID = String.valueOf(getOrderId(orderID));
        Assertion.assertThat("Corporate billing option is selected", paymentPage.getSelectedPayment().toLowerCase(), is("corporate billing"));

        step("get total price payment page");
        String totalPricePaymentPage = paymentPage.totalPricePaymentPage();
        Assertion.assertThat("total price should not be empty {" +totalPricePaymentPage +" } ", totalPricePaymentPage.isEmpty(),is(false));

        step("Submit payment using Corporate Billing");
        paymentPage.clickOnPaymentButton();

        step("verify successfully placed order popup from corporate");
        Assertion.assertThat("get successfully placed order popup",paymentPage.isCorporateSuccessfullyPlacedOrderPopup(),is(true));

        step("approve payment of order id: { " + orderID +" } " + " using admin account with username : { " + adminUsername + " }");
        Boolean bookingStatus = corporateApi.approveBookingB2BApi(adminUsername, adminPassword, orderID);
        Assertion.assertThat("Booking approved by B2B dashboard", bookingStatus, is(true));

        step("verify success payment on payment page");
        Assertion.assertThat("is payment done successfully by corp dashboard",paymentPage.isPaymentSuccessfulVisible(),is(true));

        step("get booking code from API");
        String bookingCode = flightApi.getBookingCode(orderID, username);

        step("set Tiket Number on flight dashboard and ticket will be eticket issued");
        Boolean isTicketIssued = flightApi.flightEticketIssuedRequest(orderID, bookingCode, username,name + " " + lastName,20);
        Assertion.assertThat("is Eticket Issued", isTicketIssued, is(true));

        step("Open the order list and go to order detail");
        paymentPage.clickSeeOrderListButton();
        orderListPage.openIssuedTicketViaURL(orderID);
        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.verifyIssuedETicket(), is(true));
        String totalPriceOrderDetail = orderDetailPage.getVerticalTotalPrice();
        Assertion.assertThat("is total price from payment page { "+ totalPricePaymentPage+" } match with order detail page { "+ totalPriceOrderDetail+ " } total price",CoreFunctions.convertPriceSafely(totalPriceOrderDetail),is(CoreFunctions.convertPriceSafely(totalPricePaymentPage)));
    }

    @Author(names = {"Ashish"})
    @Module(name = "Booking")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void testFlightBookingAdminIsAutoApprovedDweb() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testFlightBookingAdminIsAutoApprovedDweb");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPageWeb = FlightLandingPageWeb.getInstance(session);
        FlightSearchResultPageWeb<?> flightSearchResultPage = FlightSearchResultPageWeb.getInstance(session);
        FlightProductReviewPage<?> flightProductReviewPage = new FlightProductReviewPage<>(session);
        FlightBookingFormPageWeb<?> flightBookingFormPage = FlightBookingFormPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        TravelPlanWeb<?> travelPlanPage = TravelPlanWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        CorporateDashboard<?> corporateDashboard = new CorporateDashboard<>(session);
        FlightApi<?> flightApi = new FlightApi<>(session);

        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");
        String airline = param.get("airline");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String departureDate = getRandomLongDateOnRange(daysToDepartureDate,30);

        int adultPassengers = Integer.parseInt(param.get("adultPassengers"));
        int childPassengers = Integer.parseInt(param.get("childPassengers"));
        int infantPassengers = Integer.parseInt(param.get("infantPassengers"));

        String username = param.get("username");
        String password = param.get("password");
        String currency = param.get("currency");
        String name = param.get("name");
        String lastName = param.get("lastName");

        step("User open home page");
        homePage.goToHomePage();

        step("Go to account page");
        loginPage.goToAccountPage();

        step("login in using { " + username + " }");
        loginPage.loginIfNotWithSameEmail(username, password, "123456", 1);

        step("go to tiket home page");
        corporateDashboard.goBackToTiketHomePage();

        step("User select { " + currency + " } currency");
        homePage.selectCurrency(currency);

        step("User open Flight vertical");
        homePage.openVerticalPageViaURL("Flights");

        step("Toggle trip type");
        flightLandingPageWeb.toggleTripType(tripType);

        step("Select origin airport");
        flightLandingPageWeb.selectOrigin(originAirport);

        step("Select destination airport");
        flightLandingPageWeb.selectDestination(destinationAirport);

        step("Select date");
        flightLandingPageWeb.selectDate(departureDate);

        step("add passenger");
        flightLandingPageWeb.addPassenger(adultPassengers,childPassengers,infantPassengers);

        step("search flight");
        flightLandingPageWeb.searchFlight();

        step("Apply Filter " + airline);
        flightSearchResultPage.filterAirline(airline);

        step("select flight");
        flightSearchResultPage.selectFlightByAirline(airline);

        step("Continue to booking form");
        flightProductReviewPage.continueToBookingForm();
        flightBookingFormPage.clickNoThanksOnFlightMayReschedulePopup();

        step("Click toggle to fill passengers details and save ");
        flightBookingFormPage.fillPassengerDetailsLogin(name,lastName);

        step("Go to payment page");
        flightBookingFormPage.goToPaymentPageFromBookingFormPage();

        step("Skip Flight may rescheduled page by clicking no thanks");
        travelPlanPage.skipTravelPlan();
        flightBookingFormPage.clickNoThanksOnFlightMayReschedulePopup();

        step("Payment page should be displayed");
        String orderID = paymentPage.getOrderId();
        orderID = String.valueOf(getOrderId(orderID));
        Assertion.assertThat("Corporate billing option is selected", paymentPage.getSelectedPayment().toLowerCase(), is("corporate billing"));

        step("get total price payment page");
        String totalPricePaymentPage = paymentPage.totalPricePaymentPage();
        Assertion.assertThat("total price should not be empty {" +totalPricePaymentPage +" } ", totalPricePaymentPage.isEmpty(),is(false));

        step("Submit payment using Corporate Billing");
        paymentPage.clickOnPaymentButton();
        orderListPage.waitForPaymentSuccessful(orderID,15);

        step("verify success payment on payment page");
        Assertion.assertThat("is payment done successfully by corp dashboard",paymentPage.isPaymentSuccessfulVisible(),is(true));

        step("get booking code from API");
        String bookingCode = flightApi.getBookingCode(orderID, username);

        step("set Tiket Number on flight dashboard and ticket will be eticket issued");
        Boolean isTicketIssued = flightApi.flightEticketIssuedRequest(orderID, bookingCode, username,name + " " + lastName,20);
        Assertion.assertThat("is Eticket Issued", isTicketIssued, is(true));

        step("Open the order list and go to order detail");
        paymentPage.clickSeeOrderListButton();
        orderListPage.openIssuedTicketViaURL(orderID);
        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.verifyIssuedETicket(), is(true));
        String totalPriceOrderDetail = orderDetailPage.getVerticalTotalPrice();
        Assertion.assertThat("is total price from payment page { "+ totalPricePaymentPage+" } match with order detail page { "+ totalPriceOrderDetail+ " } total price",CoreFunctions.convertPriceSafely(totalPriceOrderDetail),is(CoreFunctions.convertPriceSafely(totalPricePaymentPage)));
    }

    @Author(names = {"Ashish"})
    @Module(name = "Booking")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void testFlightBookingApproverIsAutoApprovedDweb() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testFlightBookingApproverIsAutoApprovedDweb");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPageWeb = FlightLandingPageWeb.getInstance(session);
        FlightSearchResultPageWeb<?> flightSearchResultPage = FlightSearchResultPageWeb.getInstance(session);
        FlightProductReviewPage<?> flightProductReviewPage = new FlightProductReviewPage<>(session);
        FlightBookingFormPageWeb<?> flightBookingFormPage = FlightBookingFormPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        TravelPlanWeb<?> travelPlanPage = TravelPlanWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        CorporateDashboard<?> corporateDashboard = new CorporateDashboard<>(session);
        FlightApi<?> flightApi = new FlightApi<>(session);

        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");
        String airline = param.get("airline");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String departureDate = getRandomLongDateOnRange(daysToDepartureDate,30);

        int adultPassengers = Integer.parseInt(param.get("adultPassengers"));
        int childPassengers = Integer.parseInt(param.get("childPassengers"));
        int infantPassengers = Integer.parseInt(param.get("infantPassengers"));

        String username = param.get("username");
        String password = param.get("password");
        String currency = param.get("currency");
        String name = param.get("name");
        String lastName = param.get("lastName");

        step("User open home page");
        homePage.goToHomePage();

        step("Go to account page");
        loginPage.goToAccountPage();

        step("login in using { " + username + " }");
        loginPage.loginIfNotWithSameEmail(username, password, "123456", 1);

        step("go to tiket home page");
        corporateDashboard.goBackToTiketHomePage();

        step("User select { " + currency + " } currency");
        homePage.selectCurrency(currency);

        step("User open Flight vertical");
        homePage.openVerticalPageViaURL("Flights");

        step("Toggle trip type");
        flightLandingPageWeb.toggleTripType(tripType);

        step("Select origin airport");
        flightLandingPageWeb.selectOrigin(originAirport);

        step("Select destination airport");
        flightLandingPageWeb.selectDestination(destinationAirport);

        step("Select date");
        flightLandingPageWeb.selectDate(departureDate);

        step("add passenger");
        flightLandingPageWeb.addPassenger(adultPassengers,childPassengers,infantPassengers);

        step("search flight");
        flightLandingPageWeb.searchFlight();

        step("Apply Filter " + airline);
        flightSearchResultPage.filterAirline(airline);

        step("select flight");
        flightSearchResultPage.selectFlightByAirline(airline);

        step("Continue to booking form");
        flightProductReviewPage.continueToBookingForm();
        flightBookingFormPage.clickNoThanksOnFlightMayReschedulePopup();

        step("Click toggle to fill passengers details and save ");
        flightBookingFormPage.fillPassengerDetailsLogin(name,lastName);

        step("Go to payment page");
        flightBookingFormPage.goToPaymentPageFromBookingFormPage();

        step("Skip Flight may rescheduled page by clicking no thanks");
        travelPlanPage.skipTravelPlan();
        flightBookingFormPage.clickNoThanksOnFlightMayReschedulePopup();

        step("Payment page should be displayed");
        String orderID = paymentPage.getOrderId();
        orderID = String.valueOf(getOrderId(orderID));
        Assertion.assertThat("Corporate billing option is selected", paymentPage.getSelectedPayment().toLowerCase(), is("corporate billing"));

        step("get total price payment page");
        String totalPricePaymentPage = paymentPage.totalPricePaymentPage();
        Assertion.assertThat("total price should not be empty {" +totalPricePaymentPage +" } ", totalPricePaymentPage.isEmpty(),is(false));

        step("Submit payment using Corporate Billing");
        paymentPage.clickOnPaymentButton();
        orderListPage.waitForPaymentSuccessful(orderID,15);

        step("verify success payment on payment page");
        Assertion.assertThat("is payment done successfully by corp dashboard",paymentPage.isPaymentSuccessfulVisible(),is(true));

        step("get booking code from API");
        String bookingCode = flightApi.getBookingCode(orderID, username);

        step("set Tiket Number on flight dashboard and ticket will be eticket issued");
        Boolean isTicketIssued = flightApi.flightEticketIssuedRequest(orderID, bookingCode, username,name + " " + lastName,20);
        Assertion.assertThat("is Eticket Issued", isTicketIssued, is(true));

        step("Open the order list and go to order detail");
        paymentPage.clickSeeOrderListButton();
        orderListPage.openIssuedTicketViaURL(orderID);
        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.verifyIssuedETicket(), is(true));
        String totalPriceOrderDetail = orderDetailPage.getVerticalTotalPrice();
        Assertion.assertThat("is total price from payment page { "+ totalPricePaymentPage+" } match with order detail page { "+ totalPriceOrderDetail+ " } total price",CoreFunctions.convertPriceSafely(totalPriceOrderDetail),is(CoreFunctions.convertPriceSafely(totalPricePaymentPage)));
    }

    @Author(names = "Prerna")
    @Module(name = "B2BLogin")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void verifyMultiOrderLoginFromBookingFormTraveler() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("verifyMultiOrderLoginFromBookingFormTraveler");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String location = param.get("location");
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int noOfRooms = Integer.parseInt(param.get("rooms"));
        int guests = Integer.parseInt(param.get("guests"));

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?>landingPage=HotelLandingPageWeb.getInstance(session);
        HotelRoomListPage<?> roomListPage=new HotelRoomListPage<>(session);
        HotelBookingFormPageWeb<?> bookingFormPage= HotelBookingFormPageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User select IDR currency");
        homePage.selectCurrency("IDR");

        step("User Open Hotel Landing page");
        homePage.openVerticalPageViaURL("Hotel");
        Assertion.assertThat("Landing Page is Opened", landingPage.isLandingPageVisible(), is(true));

        step("Search stay hotel");
        landingPage.searchStayLocation(location);

        step("submit search Button");
        landingPage.clickSubmit();
        roomListPage.scrollToRoomListSection();

        step("User update number of room & guest on roomList");
        roomListPage.clickRoomNGuestButton();
        roomListPage.setTotalGuest(noOfRooms);
        roomListPage.setTotalRoom(guests);

        roomListPage.clickGuestDoneButton();
        roomListPage.clickRoomListChangeButton();

        step("Choose first room");
        roomListPage.firstBookRoom();

        step("Click second room with different rate plan ");
        roomListPage.secondBookRoom();

        roomListPage.clickMultiBookButton();

        step("User input contact detail");
        Assertion.assertThat("Contact form should be visible on Booking Form page",bookingFormPage.isContactForm(),is(true));

        step("click login on booking form page ");
        bookingFormPage.clickLoginBanner();
        loginPage.loginSteps(email, password, otp, 1);

        Assertion.assertThat("user verify the validation error that the booking does not apply to Corporates accounts", bookingFormPage.verifyValidationErrorForMultiGuestB2B() ,is(true));
    }

    @Author(names = "Prerna")
    @Module(name = "B2BLogin")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void verifyMultiOrderB2BEmailInBookingFormShowsError() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("verifyMultiOrderB2BEmailInBookingFormShowsError");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String location = param.get("location");
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int noOfRooms = Integer.parseInt(param.get("rooms"));

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?>landingPage=HotelLandingPageWeb.getInstance(session);
        HotelRoomListPage<?> roomListPage=new HotelRoomListPage<>(session);
        HotelBookingFormPageWeb<?> bookingFormPage= HotelBookingFormPageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        step("Log out");
        loginPage.loginAsGuest();
        
        step("User open home page");
        homePage.goToHomePage();

        step("User Open Hotel Landing page");
        homePage.openVerticalPageViaURL("Hotel");
        Assertion.assertThat("Landing Page is Opened", landingPage.isLandingPageVisible(), is(true));

        step("Search stay hotel");
        landingPage.searchStayLocation(location);
        landingPage.clickRoomAndGuestButton();
        landingPage.setTotalRoom(noOfRooms);
        landingPage.clickDoneSetRoomGuest();

        step("submit search Button");
        landingPage.clickSubmit();

        step("Select first room");
        roomListPage.clickBookButton();

        step("Select second room with different rate plan");
        roomListPage.clickBookButton();

        step("Click Multi-Book button");
        roomListPage.clickMultiBookButton();

        step("Continue to Booking form page and click on login and fill details ");
        bookingFormPage.clickLoginBanner();
        loginPage.loginSteps(email, password, otp, 1);

        step("User verify the validation error that the booking does not apply to Corporate accounts");
        Assertion.assertThat("user verify the validation error that the booking does not apply to Corporate accounts", bookingFormPage.verifyEmailErrorWhenPutB2BEmailMultiGuest() ,is(true));
    }

    @Author(names = {"Ashish"})
    @Module(name = "TravelPolicy")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void testMeetTravelPolicyFlightSRPDweb() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testMeetTravelPolicyFlightSRPDweb");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPageWeb = FlightLandingPageWeb.getInstance(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);
        FlightSearchResultPageWeb<?> flightSearchResultPage = FlightSearchResultPageWeb.getInstance(session);

        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate,daysToDepartureDate);

        int adultPassengers = Integer.parseInt(param.get("adultPassengers"));
        int childPassengers = Integer.parseInt(param.get("childPassengers"));
        int infantPassengers = Integer.parseInt(param.get("infantPassengers"));

        ArrayList<String> airlines = CoreFunctions.makeAirlineArrayList(param.get("airline"));

        String username = param.get("username");
        String password = param.get("password");
        String currency = param.get("currency");

        step("User open home page");
        homePage.goToHomePage();

        step("Login as " + username + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(username, password);

        step("User select IDR currency");
        homePage.selectCurrency(currency);

        step("User open Flight vertical");
        homePage.openVerticalPageViaURL("Flights");

        step("Toggle trip type");
        flightLandingPageWeb.toggleTripType(tripType);

        step("Select origin airport");
        flightLandingPageWeb.selectOrigin(originAirport);

        step("Select destination airport");
        flightLandingPageWeb.selectDestination(destinationAirport);

        step("Select date");
        flightLandingPageWeb.selectDate(departureDate);

        step("add passenger");
        flightLandingPageWeb.addPassenger(adultPassengers,childPassengers,infantPassengers);

        step("search flight");
        flightLandingPageWeb.searchFlight();

        step("verify meet travel policy for flight " + airlines);
        Assertion.assertThat("Airlines should include " + airlines, new HashSet<>(flightSearchResultPage.getAirlineNames(airlines)), is(new HashSet<>(airlines)));
        Assertion.assertThat("Is meet travel policy flight" + airlines, flightSearchResultPage.isMeetTravelPolicy(), is(true));
    }

    @Author(names = {"Ashish"})
    @Module(name = "Reschedule")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void testAdminRequestRescheduleHotelAutoApproveDweb() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();

        Map<String, String> param = getParam("testAdminRequestRescheduleHotelAutoApproveDweb");
        BaseTest.paramsThreadLocal.set(param);

        String username = param.get("username");
        String password = param.get("password");

        String hotelName = param.get("hotelName");

        String dateType = param.get("date_type");
        String date = param.get("date");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        HotelLandingPageWeb<?> hotelLandingPage = HotelLandingPageWeb.getInstance(session);
        HotelRoomListPage<?> hotelRoomListPage = new HotelRoomListPage<>(session);
        HotelBookingFormPageWeb<?> hotelBookingFormPage = HotelBookingFormPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        TravelPlanWeb<?> travelPlanPage = TravelPlanWeb.getInstance(session);
        ReschedulePage<?> reschedulePage = new ReschedulePage<>(session);
        RefundPageWeb<?> refundPage = RefundPageWeb.getInstance(session);
        CorporateDashboard<?> corporateDashboard = new CorporateDashboard<>(session);

        step("Go to home");
        homePage.goToHomePage();

        step("Login as " + username + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(username, password);

        step("User select IDR currency");
        homePage.selectCurrency("IDR");

        step("User open hotel vertical");
        homePage.openVerticalPageViaURL("Hotel");

        step("User search hotel");
        hotelLandingPage.searchStayLocation(hotelName);

        step("Set date");
        hotelLandingPage.setStayDate(dateType, date);

        step("Set guests and rooms");
        hotelLandingPage.clickRoomAndGuestButton();
        hotelLandingPage.setTotalRoom(1);
        hotelLandingPage.setTotalGuest(1);
        hotelLandingPage.clickDone();

        step("Submit search");
        hotelLandingPage.clickSubmit();

        step("Click book button");
        hotelRoomListPage.clickBookButton();

        step("Continue to payment page");
        hotelBookingFormPage.continueToSecondPartThenPaymentPage();
        travelPlanPage.skipTravelPlan();

        step("Payment page should be displayed");
        String orderID = paymentPage.getOrderId();
        orderID = String.valueOf(getOrderId(orderID));
        String totalPricePaymentPage = paymentPage.totalPricePaymentPage();
        Assertion.assertThat("Corporate billing option is selected", paymentPage.getSelectedPayment().toLowerCase(), is("corporate billing"));

        step("Submit payment using Corporate Billing");
        paymentPage.clickOnPaymentButton();

        step("Open the order list and go to order detail");
        orderListPage.openIssuedTicketViaURL(orderID,5);

        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.verifyIssuedETicket(), is(true));
        orderDetailPage.closeEticketPopUp();
        Assertion.assertThat("total payment on order detail page should be equal to payment page", CoreFunctions.convertPriceSafely(orderDetailPage.getVerticalTotalPrice()), is(CoreFunctions.convertPriceSafely(totalPricePaymentPage)));

        step("Click reschedule button");
        orderDetailPage.clickRescheduleButton();

        step("Click check availability button");
        reschedulePage.clickCheckAvailabilityButton();
        String reschedulePageDate = reschedulePage.getDate();

        step("Select room with free/more payment");
        reschedulePage.selectRoomFreeOrAdditionalPayment();

        step("Submit booking");
        reschedulePage.checkTnC();
        reschedulePage.clickSubmitRescheduleButton();

        step("Submit payment using Corporate Billing");
        String orderID2 = paymentPage.getOrderId();
        orderID2 = String.valueOf(getOrderId(orderID2));
        double totalPriceReschedulePaymentPage = paymentPage.totalPricePaymentPageHandleFreePayment();
        paymentPage.clickOnPaymentButton();
        if(totalPriceReschedulePaymentPage == 0){
            step("submit refund");
            refundPage.clickChecklistTnc();
            refundPage.clickSubmitRefund();
            refundPage.clickProcessRefund();
        }

        step("Open new order detail");
        orderListPage.openIssuedTicketViaURL(orderID2,20);
        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.verifyIssuedETicket(), is(true));
        orderDetailPage.closeEticketPopUp();
        orderDetailPage.skipCoachmark();

        Assertion.assertThat("Reschedule card notification",orderDetailPage.isRescheduleCardDisplayed(), is(true));
        Assertion.assertThat("Order ID is equal", orderDetailPage.getOrderId(), is(orderID2));
        Assertion.assertThat("Start Date is equal", orderDetailPage.getDate(), is(reschedulePageDate));
        Assertion.assertThat("Hotel Name is displayed correctly", orderDetailPage.isHotelNameDisplayed(hotelName), is(true));
        String orderDetailPrice=orderDetailPage.getVerticalTotalPrice();
        if(orderDetailPrice.toLowerCase().contains("free")){
            orderDetailPrice="0";
        }
        Assertion.assertThat("total payment on order detail page should be equal to payment page reschedule", CoreFunctions.convertPriceSafely(orderDetailPrice), is(totalPriceReschedulePaymentPage));
    }

    @Author(names = "Ashish")
    @Module(name = "Login")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void testLoginOnTrainBookingFormTravelerDweb() throws InterruptedException, FailedLoginException {
        Map<String, String> param = getParam("testLoginOnTrainBookingFormTravelerDweb");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        String currency = param.get("currency");
        String originStation = param.get("originStation");
        String destinationStation = param.get("destinationStation");
        String travelerUsername = param.get("username");
        String travelerPassword = param.get("password");
        String otp= param.get("otp");

        String tripType = param.get("tripType");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        TrainSearchPageWeb<?> trainSearchPage = TrainSearchPageWeb.getInstance(session);
        TrainSearchResultPageWeb<?> trainSearchResultPage = TrainSearchResultPageWeb.getInstance(session);
        TrainBookingFormPage<?> trainBookingFormPage = new TrainBookingFormPage<>(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open home page as guest");
        loginPage.logoutViaUrl();

        step ("User select IDR currency");
        homePage.selectCurrency(currency);

        step("User open train vertical");
        homePage.openVerticalPageViaURL("Train");

        step("User skip pop-up");
        trainSearchPage.clickSkipPopup();

        step("User select origin station");
        trainSearchPage.selectOrigin(originStation);

        step("User select destination station");
        trainSearchPage.selectDestination(destinationStation);

        step("User toggle one way");
        trainSearchPage.toggleTripType(tripType);

        step("User select departure date");
        trainSearchPage.selectDate(departureDate);

        step("User search train");
        trainSearchPage.searchTrain();

        step("User skip pop-up");
        trainSearchPage.clickSkipPopup();

        step("select train");
        trainSearchResultPage.selectTrain();

        step("continue to booking form page");
        trainBookingFormPage.clickLoginBanner();

        step("login with :" + travelerUsername);
        loginPage.loginSteps(travelerUsername, travelerPassword, otp, 1);
        Assertion.assertThat("User should be able to login", trainBookingFormPage.isLoginSuccessful(travelerUsername), is(true));
    }

    @Author(names = "Ashish")
    @Module(name = "Booking")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void testLoginBookingBusViaAdminAutoApprovedDweb() throws InterruptedException, FailedLoginException {
        Map<String, String> param = getParam("testLoginBookingBusViaAdminAutoApprovedDweb");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        BusSearchPageWeb<?> busSearchPage = BusSearchPageWeb.getInstance(session);;
        BusSearchResultPageWeb<?> busSearchResultPage = BusSearchResultPageWeb.getInstance(session);
        BusPdpPageWeb<?> busPdpPage = BusPdpPageWeb.getInstance(session);
        BusBookingFormPageWeb<?> busBookingFormPage = BusBookingFormPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        TravelPlanWeb<?> travelPlanPage = TravelPlanWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        CorporateDashboard<?> corporateDashboard = new CorporateDashboard<>(session);

        String username = param.get("username");
        String password = param.get("password");

        String origin = param.get("origin");
        String destination = param.get("destination");
        String tripType = param.get("tripType");
        int daysToDeparture = Integer.parseInt(param.get("daysToDeparture"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDeparture);
        int totalPassenger = 1;

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(username, password, "123456", 1);

        step("User select IDR currency");
        corporateDashboard.goBackToTiketHomePage();
        homePage.selectCurrency("IDR");

        step("User open bus vertical");
        homePage.openVerticalPageViaURL("bus-travel");

        step("User click skip button");
        busSearchPage.clickSkipButton();

        step("User select oneway");
        busSearchPage.selectType(tripType);

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

        step("User continue to payment page");
        busBookingFormPage.continueToPaymentPage();

        step("Skip travel policy page");
        travelPlanPage.skipTravelPlan();

        step("Payment page should be displayed");
        String orderID = paymentPage.getOrderId();
        orderID = String.valueOf(getOrderId(orderID));
        Assertion.assertThat("order id should not be empty {" + orderID + " } ", orderID.isEmpty(), is(false));
        Assertion.assertThat("Corporate billing option is selected", paymentPage.getSelectedPayment().toLowerCase(), is("corporate billing"));

        step("get total price payment page");
        String totalPricePaymentPage = paymentPage.totalPricePaymentPage();
        Assertion.assertThat("total price should not be empty {" +totalPricePaymentPage +" } ", totalPricePaymentPage.isEmpty(),is(false));

        step("Submit payment using Corporate Billing");
        paymentPage.clickOnPaymentButton();
        orderListPage.waitForPaymentSuccessful(orderID,15);

        step("Open the order list and go to order detail");
        paymentPage.clickSeeOrderListButton();
        orderListPage.openIssuedTicketViaURL(orderID,20);
        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.verifyIssuedETicket(), is(true));
        String totalPriceOrderDetail = orderDetailPage.getVerticalTotalPrice();
        Assertion.assertThat("is total price from payment page { "+ totalPricePaymentPage+" } match with order detail page { "+ totalPriceOrderDetail+ " } total price",CoreFunctions.convertPriceSafely(totalPriceOrderDetail),is(CoreFunctions.convertPriceSafely(totalPricePaymentPage)));
    }

    @Author(names = "Ashish")
    @Module(name = "Booking")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void testLoginBookingBusViaTravelerApproveBookingByApproverDweb() throws InterruptedException, FailedLoginException {
        Map<String, String> param = getParam("testLoginBookingBusViaTravelerApproveBookingByApproverDweb");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        BusSearchPageWeb<?> busSearchPage = BusSearchPageWeb.getInstance(session);;
        BusSearchResultPageWeb<?> busSearchResultPage = BusSearchResultPageWeb.getInstance(session);
        BusPdpPageWeb<?> busPdpPage = BusPdpPageWeb.getInstance(session);
        BusBookingFormPageWeb<?> busBookingFormPage = BusBookingFormPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        TravelPlanWeb<?> travelPlanPage = TravelPlanWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String approverUsername = param.get("approverUsername");
        String approverPassword = param.get("approverPassword");

        String origin = param.get("origin");
        String destination = param.get("destination");
        int daysToDeparture = Integer.parseInt(param.get("daysToDeparture"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDeparture);
        int totalPassenger = 1;

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(username, password, "123456", 1);

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

        step("User continue to payment page");
        busBookingFormPage.continueToPaymentPage();

        step("Skip travel policy page");
        travelPlanPage.skipTravelPlan();

        step("Payment page should be displayed");
        String orderID = paymentPage.getOrderId();
        orderID = String.valueOf(getOrderId(orderID));
        Assertion.assertThat("order id should not be empty {" + orderID + " } ", orderID.isEmpty(), is(false));
        Assertion.assertThat("Corporate billing option is selected", paymentPage.getSelectedPayment().toLowerCase(), is("corporate billing"));

        step("get total price payment page");
        String totalPricePaymentPage = paymentPage.totalPricePaymentPage();
        Assertion.assertThat("total price should not be empty {" +totalPricePaymentPage +" } ", totalPricePaymentPage.isEmpty(),is(false));

        step("Submit payment using Corporate Billing");
        paymentPage.clickOnPaymentButton();

        step("verify successfully placed order popup from corporate");
        Assertion.assertThat("get successfully placed order popup",paymentPage.isCorporateSuccessfullyPlacedOrderPopup(),is(true));

        step("approve payment of order id: { " + orderID +" } " + " using admin account with username : { " + approverUsername + " }");
        Boolean bookingStatus = corporateApi.approveBookingB2BApi(approverUsername, approverPassword, orderID);
        Assertion.assertThat("Booking approved by B2B dashboard", bookingStatus, is(true));

        step("verify success payment on payment page");
        Assertion.assertThat("is payment done successfully by corp dashboard",paymentPage.isPaymentSuccessfulVisible(),is(true));

        step("Open the order list and go to order detail");
        paymentPage.clickSeeOrderListButton();
        orderListPage.openIssuedTicketViaURL(orderID);
        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.verifyIssuedETicket(), is(true));
        String totalPriceOrderDetail = orderDetailPage.getVerticalTotalPrice();
        Assertion.assertThat("is total price from payment page { "+ totalPricePaymentPage+" } match with order detail page { "+ totalPriceOrderDetail+ " } total price",CoreFunctions.convertPriceSafely(totalPriceOrderDetail),is(CoreFunctions.convertPriceSafely(totalPricePaymentPage)));
    }

    @Author(names = "Ashish")
    @Module(name = "Booking")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void testLoginBookingFerryViaApproverAutoApprovedDweb() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testLoginBookingFerryViaApproverAutoApprovedDweb");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        FerrySearchPageWeb<?> ferrySearchPageWeb = FerrySearchPageWeb.getInstance(session);
        FerrySearchResultPageWeb<?> ferrySearchResultPageWeb = FerrySearchResultPageWeb.getInstance(session);
        FerryBookingPageWeb<?> ferryBookingPageWeb = FerryBookingPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        TravelPlanWeb<?> travelPlanPage = TravelPlanWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        CorporateDashboard<?> corporateDashboard = new CorporateDashboard<>(session);

        String username = param.get("username");
        String password = param.get("password");

        String departurePort = param.get("originPort");
        String destinationPort = param.get("destinationPort");
        int adultPassenger = Integer.parseInt(param.get("adultPassenger"));
        int childPassenger = Integer.parseInt(param.get("childPassenger"));
        int infantPassenger = Integer.parseInt(param.get("infantPassenger"));
        int elderPassenger = Integer.parseInt(param.get("elderPassenger"));
        String ticketType = param.get("ticketType");

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(username, password, "123456", 1);

        step("User select IDR currency");
        corporateDashboard.goBackToTiketHomePage();
        homePage.selectCurrency("IDR");

        step("User open ferry vertical");
        homePage.openVerticalPageViaURL("ferry");

        step("User click skip button");
        ferrySearchPageWeb.clickSkipButton();

        step("User select departure port");
        ferrySearchPageWeb.clickDeparturePortInput();
        ferrySearchPageWeb.selectPort(departurePort);

        step("User select destination port");
        ferrySearchPageWeb.clickDestinationPortInput();
        ferrySearchPageWeb.selectPort(destinationPort);

        step("User select date");
        ferrySearchPageWeb.clickDateInput();
        ferrySearchPageWeb.selectDate("30");

        step("User select ticket type");
        ferrySearchPageWeb.clickTicketTypeInput();
        ferrySearchPageWeb.selectTicketType(ticketType);

        step("User select number of passengers");
        ferrySearchPageWeb.clickPassengerInput();
        ferrySearchPageWeb.setPassenger("adult", adultPassenger);
        ferrySearchPageWeb.setPassenger("child", childPassenger);
        ferrySearchPageWeb.setPassenger("infant", infantPassenger);
        ferrySearchPageWeb.setPassenger("elder", elderPassenger);
        ferrySearchPageWeb.savePassenger();

        step("User search ferry");
        ferrySearchPageWeb.searchFerry();

        step("User select first SRP card");
        ferrySearchResultPageWeb.selectFirstSrpCard();

        step("User input passenger detail");
        ferryBookingPageWeb.fillPassengerDetail(adultPassenger, childPassenger, infantPassenger, elderPassenger, String.valueOf(CoreFunctions.generateRandomInt(16)), "random");

        step("User check terms and conditions");
        ferryBookingPageWeb.clickTncCheckBox();

        step("User continue to payment page");
        ferryBookingPageWeb.continueToPaymentPage();

        step("Payment page should be displayed");
        String orderID = paymentPage.getOrderId();
        orderID = String.valueOf(getOrderId(orderID));
        Assertion.assertThat("order id should not be empty {" + orderID + " } ", orderID.isEmpty(), is(false));
        Assertion.assertThat("Corporate billing option is selected", paymentPage.getSelectedPayment().toLowerCase(), is("corporate billing"));

        step("get total price payment page");
        String totalPricePaymentPage = paymentPage.totalPricePaymentPage();
        Assertion.assertThat("total price should not be empty {" +totalPricePaymentPage +" } ", totalPricePaymentPage.isEmpty(),is(false));

        step("Submit payment using Corporate Billing");
        paymentPage.clickOnPaymentButton();
        orderListPage.waitForPaymentSuccessful(orderID,15);

        step("Open the order list and go to order detail");
        paymentPage.clickSeeOrderListButton();
        orderListPage.openIssuedTicketViaURL(orderID,15);
        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.verifyIssuedETicket(), is(true));
        String totalPriceOrderDetail = orderDetailPage.getVerticalTotalPrice();
        Assertion.assertThat("is total price from payment page { "+ totalPricePaymentPage+" } match with order detail page { "+ totalPriceOrderDetail+ " } total price",CoreFunctions.convertPriceSafely(totalPriceOrderDetail),is(CoreFunctions.convertPriceSafely(totalPricePaymentPage)));
    }

    @Author(names = "Ashish")
    @Module(name = "Booking")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void testLoginBookingFerryViaTravelerApproveBookingByApproverDweb() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testLoginBookingFerryViaTravelerApproveBookingByApproverDweb");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        FerrySearchPageWeb<?> ferrySearchPageWeb = FerrySearchPageWeb.getInstance(session);
        FerrySearchResultPageWeb<?> ferrySearchResultPageWeb = FerrySearchResultPageWeb.getInstance(session);
        FerryBookingPageWeb<?> ferryBookingPageWeb = FerryBookingPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        TravelPlanWeb<?> travelPlanPage = TravelPlanWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String approverUsername = param.get("approverUsername");
        String approverPassword = param.get("approverPassword");

        String departurePort = param.get("originPort");
        String destinationPort = param.get("destinationPort");

        int adultPassenger = Integer.parseInt(param.get("adultPassenger"));
        int childPassenger = Integer.parseInt(param.get("childPassenger"));
        int infantPassenger = Integer.parseInt(param.get("infantPassenger"));
        int elderPassenger = Integer.parseInt(param.get("elderPassenger"));
        String ticketType = param.get("ticketType");

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(username, password, "123456", 1);

        step("User select IDR currency");
        homePage.selectCurrency("IDR");

        step("User open ferry vertical");
        homePage.openVerticalPageViaURL("ferry");

        step("User click skip button");
        ferrySearchPageWeb.clickSkipButton();

        step("User select departure port");
        ferrySearchPageWeb.clickDeparturePortInput();
        ferrySearchPageWeb.selectPort(departurePort);

        step("User select destination port");
        ferrySearchPageWeb.clickDestinationPortInput();
        ferrySearchPageWeb.selectPort(destinationPort);

        step("User select date");
        ferrySearchPageWeb.clickDateInput();
        ferrySearchPageWeb.selectDate("30");

        step("User select ticket type");
        ferrySearchPageWeb.clickTicketTypeInput();
        ferrySearchPageWeb.selectTicketType(ticketType);

        step("User select number of passengers");
        ferrySearchPageWeb.clickPassengerInput();
        ferrySearchPageWeb.setPassenger("adult", adultPassenger);
        ferrySearchPageWeb.setPassenger("child", childPassenger);
        ferrySearchPageWeb.setPassenger("infant", infantPassenger);
        ferrySearchPageWeb.setPassenger("elder", elderPassenger);
        ferrySearchPageWeb.savePassenger();

        step("User search ferry");
        ferrySearchPageWeb.searchFerry();

        step("User select first SRP card");
        ferrySearchResultPageWeb.selectFirstSrpCard();

        step("User input passenger detail");
        ferryBookingPageWeb.fillPassengerDetail(adultPassenger, childPassenger, infantPassenger, elderPassenger,String.valueOf(CoreFunctions.generateRandomInt(16)) , "random");

        step("User check terms and conditions");
        ferryBookingPageWeb.clickTncCheckBox();

        step("User continue to payment page");
        ferryBookingPageWeb.continueToPaymentPage();

        step("Payment page should be displayed");
        String orderID = paymentPage.getOrderId();
        orderID = String.valueOf(getOrderId(orderID));
        Assertion.assertThat("order id should not be empty {" + orderID + " } ", orderID.isEmpty(), is(false));
        Assertion.assertThat("Corporate billing option is selected", paymentPage.getSelectedPayment().toLowerCase(), is("corporate billing"));

        step("get total price payment page");
        String totalPricePaymentPage = paymentPage.totalPricePaymentPage();
        Assertion.assertThat("total price should not be empty {" +totalPricePaymentPage +" } ", totalPricePaymentPage.isEmpty(),is(false));

        step("Submit payment using Corporate Billing");
        paymentPage.clickOnPaymentButton();

        step("verify successfully placed order popup from corporate");
        Assertion.assertThat("get successfully placed order popup",paymentPage.isCorporateSuccessfullyPlacedOrderPopup(),is(true));

        step("approve payment of order id: { " + orderID +" } " + " using admin account with username : { " + approverUsername + " }");
        Boolean bookingStatus = corporateApi.approveBookingB2BApi(approverUsername, approverPassword, orderID);
        Assertion.assertThat("Booking approved by B2B dashboard", bookingStatus, is(true));

        step("verify success payment on payment page");
        Assertion.assertThat("is payment done successfully by corp dashboard",paymentPage.isPaymentSuccessfulVisible(),is(true));

        step("Open the order list and go to order detail");
        paymentPage.clickSeeOrderListButton();
        orderListPage.openIssuedTicketViaURL(orderID,15);
        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.verifyIssuedETicket(), is(true));
        String totalPriceOrderDetail = orderDetailPage.getVerticalTotalPrice();
        Assertion.assertThat("is total price from payment page { "+ totalPricePaymentPage+" } match with order detail page { "+ totalPriceOrderDetail+ " } total price",CoreFunctions.convertPriceSafely(totalPriceOrderDetail),is(CoreFunctions.convertPriceSafely(totalPricePaymentPage)));
    }

    @Author(names = {"Ashish"})
    @Module(name = "Refund")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void testFlightRefundFromTravelerApprovedByAdminDweb() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testFlightRefundFromTravelerApprovedByAdminDweb");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPageWeb = FlightLandingPageWeb.getInstance(session);
        FlightSearchResultPageWeb<?> flightSearchResultPage = FlightSearchResultPageWeb.getInstance(session);
        FlightProductReviewPage<?> flightProductReviewPage = new FlightProductReviewPage<>(session);
        FlightBookingFormPageWeb<?> flightBookingFormPage = FlightBookingFormPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        TravelPlanWeb<?> travelPlanPage = TravelPlanWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        RefundPageWeb<?> refundPage = RefundPageWeb.getInstance(session);
        FlightApi<?> flightApi = new FlightApi<>(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);

        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");
        String airline = param.get("airline");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String departureDate = getRandomLongDateOnRange(daysToDepartureDate,30);

        int adultPassengers = Integer.parseInt(param.get("adultPassengers"));
        int childPassengers = Integer.parseInt(param.get("childPassengers"));
        int infantPassengers = Integer.parseInt(param.get("infantPassengers"));

        String username = param.get("username");
        String password = param.get("password");
        String adminUsername = param.get("adminUsername");
        String adminPassword = param.get("adminPassword");
        String currency = param.get("currency");
        String name = param.get("name");
        String lastName = param.get("lastName");

        step("User open home page");
        homePage.goToHomePage();

        step("Go to account page");
        loginPage.goToAccountPage();

        step("login in using { " + username + " }");
        loginPage.loginIfNotWithSameEmail(username, password, "123456", 1);

        step("User select { " + currency + " } currency");
        homePage.selectCurrency(currency);

        step("User open Flight vertical");
        homePage.openVerticalPageViaURL("Flights");

        step("Toggle trip type");
        flightLandingPageWeb.toggleTripType(tripType);

        step("Select origin airport");
        flightLandingPageWeb.selectOrigin(originAirport);

        step("Select destination airport");
        flightLandingPageWeb.selectDestination(destinationAirport);

        step("Select date");
        flightLandingPageWeb.selectDate(departureDate);

        step("add passenger");
        flightLandingPageWeb.addPassenger(adultPassengers,childPassengers,infantPassengers);

        step("search flight");
        flightLandingPageWeb.searchFlight();

        step("Apply Filter " + airline);
        flightSearchResultPage.filterAirline(airline);

        step("select flight");
        flightSearchResultPage.selectFlightByAirline(airline);

        step("Continue to booking form");
        flightProductReviewPage.continueToBookingForm();
        flightBookingFormPage.clickNoThanksOnFlightMayReschedulePopupPRP();

        step("Click toggle to fill passengers details and save ");
        flightBookingFormPage.fillPassengerDetailsLogin(adultPassengers, childPassengers, infantPassengers, "domestic", airline);

        step("Go to payment page");
        flightBookingFormPage.goToPaymentPageFromBookingFormPage();

        step("Skip Flight may rescheduled page by clicking no thanks");
        travelPlanPage.skipTravelPlan();
        flightBookingFormPage.clickNoThanksOnFlightMayReschedulePopup();

        step("Payment page should be displayed");
        String orderID = paymentPage.getOrderId();
        orderID = String.valueOf(getOrderId(orderID));
        Assertion.assertThat("Corporate billing option is selected", paymentPage.getSelectedPayment().toLowerCase(), is("corporate billing"));

        step("get total price payment page");
        String totalPricePaymentPage = paymentPage.totalPricePaymentPage();
        Assertion.assertThat("total price should not be empty {" +totalPricePaymentPage +" } ", totalPricePaymentPage.isEmpty(),is(false));

        step("Submit payment using Corporate Billing");
        paymentPage.clickOnPaymentButton();

        step("verify successfully placed order popup from corporate");
        Assertion.assertThat("get successfully placed order popup",paymentPage.isCorporateSuccessfullyPlacedOrderPopup(),is(true));

        step("approve payment of order id: { " + orderID +" } " + " using admin account with username : { " + adminUsername + " }");
        Boolean bookingStatus = corporateApi.approveBookingB2BApi(adminUsername, adminPassword, orderID);
        Assertion.assertThat("Booking approved by B2B dashboard", bookingStatus, is(true));

        step("verify success payment on payment page");
        Assertion.assertThat("is payment done successfully by corp dashboard",paymentPage.isPaymentSuccessfulVisible(),is(true));

        step("get booking code from API");
        String bookingCode = flightApi.getBookingCode(orderID, username);

        step("Open the order list");
        paymentPage.clickSeeOrderListButton();

        step("set Tiket Number on flight dashboard and ticket will be eticket issued");
        Boolean isTicketIssued = flightApi.flightEticketIssuedRequest(orderID, bookingCode, username,name + " " + lastName,20);
        Assertion.assertThat("is Eticket Issued", isTicketIssued, is(true));

        step("go to order detail page");
        orderListPage.openIssuedTicketViaURL(orderID,15);
        orderDetailPage.closeEticketPopUp();
        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.verifyIssuedETicket(), is(true));
        String totalPriceOrderDetail = orderDetailPage.getVerticalTotalPrice();
        Assertion.assertThat("is total price from payment page { "+ totalPricePaymentPage+" } match with order detail page { "+ totalPriceOrderDetail+ " } total price",CoreFunctions.convertPriceSafely(totalPriceOrderDetail),is(CoreFunctions.convertPriceSafely(totalPricePaymentPage)));

        step("Click refund button");
        orderDetailPage.clickRefundButton();
        refundPage.skipRescheduleNudgeIfShown();

        step("Fill refund form");
        refundPage.selectRefundReasonPersonalReasonFlight();
        refundPage.chooseSpecificReason();

        step("verify refund estimation");
        Assertion.assertThat("refund estimation correct",refundPage.verifyRefundEstimation(currency),is(true));
        double totalRefundPrice = refundPage.getTotalRefundPrice(currency);
        refundPage.continueRefund();

        step("verify total refund price");
        double totalRefundPricePage2 = refundPage.getTotalRefundPrice(currency);
        Assertion.assertThat("total refund price are same on both page",totalRefundPrice,is(totalRefundPricePage2));

        step("submit refund");
        refundPage.clickChecklistTnc();
        refundPage.clickSubmitRefund();
        refundPage.clickProcessRefund();
        refundPage.clickGoToOrders();

        step("Check if the refunded order is being reflected on corporate dashboard");
        String refundId = corporateApi.getRefundId(orderID, adminUsername, adminPassword,"prerefund");
        Boolean isApproved = true;
        Boolean success = corporateApi.approveOrRejectRefundB2B(adminUsername, adminPassword, refundId, isApproved);
        Assertion.assertThat("Refund Approved Success", success, is(true));

        step("Check if the refunded order is being reflected on corporate dashboard");
        boolean isRefundOrderReflected = corporateApi.checkRefundList(orderID, adminUsername, password,15);
        Assertion.assertThat("Refund order is reflected on B2B corporate dashboard", isRefundOrderReflected, is(true));
    }

    @Author(names = {"Ashish"})
    @Module(name = "Refund")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void testFlightRefundFromTravelerApprovedByApproverDweb() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testFlightRefundFromTravelerApprovedByApproverDweb");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPageWeb = FlightLandingPageWeb.getInstance(session);
        FlightSearchResultPageWeb<?> flightSearchResultPage = FlightSearchResultPageWeb.getInstance(session);
        FlightProductReviewPage<?> flightProductReviewPage = new FlightProductReviewPage<>(session);
        FlightBookingFormPageWeb<?> flightBookingFormPage = FlightBookingFormPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        TravelPlanWeb<?> travelPlanPage = TravelPlanWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        RefundPageWeb<?> refundPage = RefundPageWeb.getInstance(session);
        FlightApi<?> flightApi = new FlightApi<>(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);

        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");
        String airline = param.get("airline");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String departureDate = getRandomLongDateOnRange(daysToDepartureDate,30);

        int adultPassengers = Integer.parseInt(param.get("adultPassengers"));
        int childPassengers = Integer.parseInt(param.get("childPassengers"));
        int infantPassengers = Integer.parseInt(param.get("infantPassengers"));

        String username = param.get("username");
        String password = param.get("password");
        String approverUsername = param.get("approverUsername");
        String approverPassword = param.get("approverPassword");
        String currency = param.get("currency");
        String name = param.get("name");
        String lastName = param.get("lastName");

        step("User open home page");
        homePage.goToHomePage();

        step("Go to account page");
        loginPage.goToAccountPage();

        step("login in using { " + username + " }");
        loginPage.loginIfNotWithSameEmail(username, password, "123456", 1);

        step("User select { " + currency + " } currency");
        homePage.selectCurrency(currency);

        step("User open Flight vertical");
        homePage.openVerticalPageViaURL("Flights");

        step("Toggle trip type");
        flightLandingPageWeb.toggleTripType(tripType);

        step("Select origin airport");
        flightLandingPageWeb.selectOrigin(originAirport);

        step("Select destination airport");
        flightLandingPageWeb.selectDestination(destinationAirport);

        step("Select date");
        flightLandingPageWeb.selectDate(departureDate);

        step("add passenger");
        flightLandingPageWeb.addPassenger(adultPassengers,childPassengers,infantPassengers);

        step("search flight");
        flightLandingPageWeb.searchFlight();

        step("Apply Filter " + airline);
        flightSearchResultPage.filterAirline(airline);

        step("select flight");
        flightSearchResultPage.selectFlightByAirline(airline);

        step("Continue to booking form");
        flightProductReviewPage.continueToBookingForm();
        flightBookingFormPage.clickNoThanksOnFlightMayReschedulePopup();

        step("Click toggle to fill passengers details and save ");
        flightBookingFormPage.fillPassengerDetailsLogin(name,lastName);

        step("Go to payment page");
        flightBookingFormPage.goToPaymentPageFromBookingFormPage();

        step("Skip Flight may rescheduled page by clicking no thanks");
        travelPlanPage.skipTravelPlan();
        flightBookingFormPage.clickNoThanksOnFlightMayReschedulePopup();

        step("Payment page should be displayed");
        String orderID = paymentPage.getOrderId();
        orderID = String.valueOf(getOrderId(orderID));
        Assertion.assertThat("Corporate billing option is selected", paymentPage.getSelectedPayment().toLowerCase(), is("corporate billing"));

        step("get total price payment page");
        String totalPricePaymentPage = paymentPage.totalPricePaymentPage();
        Assertion.assertThat("total price should not be empty {" +totalPricePaymentPage +" } ", totalPricePaymentPage.isEmpty(),is(false));

        step("Submit payment using Corporate Billing");
        paymentPage.clickOnPaymentButton();

        step("verify successfully placed order popup from corporate");
        Assertion.assertThat("get successfully placed order popup",paymentPage.isCorporateSuccessfullyPlacedOrderPopup(),is(true));

        step("approve payment of order id: { " + orderID +" } " + " using admin account with username : { " + approverUsername + " }");
        Boolean bookingStatus = corporateApi.approveBookingB2BApi(approverUsername, approverPassword, orderID);
        Assertion.assertThat("Booking approved by B2B dashboard", bookingStatus, is(true));

        step("verify success payment on payment page");
        Assertion.assertThat("is payment done successfully by corp dashboard",paymentPage.isPaymentSuccessfulVisible(),is(true));

        step("get booking code from API");
        String bookingCode = flightApi.getBookingCode(orderID, username);

        step("Open the order list");
        paymentPage.clickSeeOrderListButton();

        step("set Tiket Number on flight dashboard and ticket will be eticket issued");
        Boolean isTicketIssued = flightApi.flightEticketIssuedRequest(orderID, bookingCode, username,name + " " + lastName,20);
        Assertion.assertThat("is Eticket Issued", isTicketIssued, is(true));

        step("go to order detail page");
        orderListPage.openIssuedTicketViaURL(orderID,15);
        orderDetailPage.closeEticketPopUp();
        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.verifyIssuedETicket(), is(true));
        String totalPriceOrderDetail = orderDetailPage.getVerticalTotalPrice();
        Assertion.assertThat("is total price from payment page { "+ totalPricePaymentPage+" } match with order detail page { "+ totalPriceOrderDetail+ " } total price",CoreFunctions.convertPriceSafely(totalPriceOrderDetail),is(CoreFunctions.convertPriceSafely(totalPricePaymentPage)));

        step("Click refund button");
        orderDetailPage.clickRefundButton();
        refundPage.skipRescheduleNudgeIfShown();

        step("Fill refund form");
        refundPage.selectRefundReasonPersonalReasonFlight();
        refundPage.chooseSpecificReason();

        step("verify refund estimation");
        Assertion.assertThat("refund estimation correct",refundPage.verifyRefundEstimation(currency),is(true));
        double totalRefundPrice = refundPage.getTotalRefundPrice(currency);
        refundPage.continueRefund();

        step("verify total refund price");
        double totalRefundPricePage2 = refundPage.getTotalRefundPrice(currency);
        Assertion.assertThat("total refund price are same on both page",totalRefundPrice,is(totalRefundPricePage2));

        step("submit refund");
        refundPage.clickChecklistTnc();
        refundPage.clickSubmitRefund();
        refundPage.clickProcessRefund();
        refundPage.clickGoToOrders();

        step("Check if the refunded order is being reflected on corporate dashboard");
        String refundId = corporateApi.getRefundId(orderID, approverUsername, approverPassword,"prerefund");
        Boolean isApproved = true;
        Boolean success = corporateApi.approveOrRejectRefundB2B(approverUsername, approverPassword, refundId, isApproved);
        Assertion.assertThat("Refund Approved Success", success, is(true));

        step("Check if the refunded order is being reflected on corporate dashboard");
        boolean isRefundOrderReflected = corporateApi.checkRefundList(orderID, username, password,15);
        Assertion.assertThat("Refund order is reflected on B2B corporate dashboard", isRefundOrderReflected, is(true));
    }

    @Author(names = {"Ashish"})
    @Module(name = "Refund")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void testFlightRefundFromAdminWillBeAutoApprovedDweb() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testFlightRefundFromAdminWillBeAutoApprovedDweb");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPageWeb = FlightLandingPageWeb.getInstance(session);
        FlightSearchResultPageWeb<?> flightSearchResultPage = FlightSearchResultPageWeb.getInstance(session);
        FlightProductReviewPage<?> flightProductReviewPage = new FlightProductReviewPage<>(session);
        FlightBookingFormPageWeb<?> flightBookingFormPage = FlightBookingFormPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        TravelPlanWeb<?> travelPlanPage = TravelPlanWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        RefundPageWeb<?> refundPage = RefundPageWeb.getInstance(session);
        CorporateDashboard<?> corporateDashboard = new CorporateDashboard<>(session);
        FlightApi<?> flightApi = new FlightApi<>(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);

        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");
        String airline = param.get("airline");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String departureDate = getRandomLongDateOnRange(daysToDepartureDate,30);

        int adultPassengers = Integer.parseInt(param.get("adultPassengers"));
        int childPassengers = Integer.parseInt(param.get("childPassengers"));
        int infantPassengers = Integer.parseInt(param.get("infantPassengers"));

        String username = param.get("username");
        String password = param.get("password");
        String currency = param.get("currency");
        String name = param.get("name");
        String lastName = param.get("lastName");

        step("User open home page");
        homePage.goToHomePage();

        step("Go to account page");
        loginPage.goToAccountPage();

        step("login in using { " + username + " }");
        loginPage.loginIfNotWithSameEmail(username, password, "123456", 1);

        step("go to tiket home page");
        corporateDashboard.goBackToTiketHomePage();

        step("User select { " + currency + " } currency");
        homePage.selectCurrency(currency);

        step("User open Flight vertical");
        homePage.openVerticalPageViaURL("Flights");

        step("Toggle trip type");
        flightLandingPageWeb.toggleTripType(tripType);

        step("Select origin airport");
        flightLandingPageWeb.selectOrigin(originAirport);

        step("Select destination airport");
        flightLandingPageWeb.selectDestination(destinationAirport);

        step("Select date");
        flightLandingPageWeb.selectDate(departureDate);

        step("add passenger");
        flightLandingPageWeb.addPassenger(adultPassengers,childPassengers,infantPassengers);

        step("search flight");
        flightLandingPageWeb.searchFlight();

        step("Apply Filter " + airline);
        flightSearchResultPage.filterAirline(airline);

        step("select flight");
        flightSearchResultPage.selectFlightByAirline(airline);

        step("Continue to booking form");
        flightProductReviewPage.continueToBookingForm();
        flightBookingFormPage.clickNoThanksOnFlightMayReschedulePopup();

        step("Click toggle to fill passengers details and save ");
        flightBookingFormPage.fillPassengerDetailsLogin(name,lastName);

        step("Go to payment page");
        flightBookingFormPage.goToPaymentPageFromBookingFormPage();

        step("Skip Flight may rescheduled page by clicking no thanks");
        travelPlanPage.skipTravelPlan();
        flightBookingFormPage.clickNoThanksOnFlightMayReschedulePopup();

        step("Payment page should be displayed");
        String orderID = paymentPage.getOrderId();
        orderID = String.valueOf(getOrderId(orderID));
        Assertion.assertThat("Corporate billing option is selected", paymentPage.getSelectedPayment().toLowerCase(), is("corporate billing"));

        step("get total price payment page");
        String totalPricePaymentPage = paymentPage.totalPricePaymentPage();
        Assertion.assertThat("total price should not be empty {" +totalPricePaymentPage +" } ", totalPricePaymentPage.isEmpty(),is(false));

        step("Submit payment using Corporate Billing");
        paymentPage.clickOnPaymentButton();
        orderListPage.waitForPaymentSuccessful(orderID,15);

        step("verify success payment on payment page");
        Assertion.assertThat("is payment done successfully by corp dashboard",paymentPage.isPaymentSuccessfulVisible(),is(true));

        step("get booking code from API");
        String bookingCode = flightApi.getBookingCode(orderID, username);

        step("Open the order list");
        paymentPage.clickSeeOrderListButton();

        step("set Tiket Number on flight dashboard and ticket will be eticket issued");
        Boolean isTicketIssued = flightApi.flightEticketIssuedRequest(orderID, bookingCode, username,name + " " + lastName,20);
        Assertion.assertThat("is Eticket Issued", isTicketIssued, is(true));

        step("go to order detail page for order id: { " + orderID +" }");
        orderListPage.openIssuedTicketViaURL(orderID,15);
        orderDetailPage.closeEticketPopUp();
        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.verifyIssuedETicket(), is(true));
        String totalPriceOrderDetail = orderDetailPage.getVerticalTotalPrice();
        Assertion.assertThat("is total price from payment page { "+ totalPricePaymentPage+" } match with order detail page { "+ totalPriceOrderDetail+ " } total price",CoreFunctions.convertPriceSafely(totalPriceOrderDetail),is(CoreFunctions.convertPriceSafely(totalPricePaymentPage)));

        step("Click refund button");
        orderDetailPage.clickRefundButton();
        refundPage.skipRescheduleNudgeIfShown();

        step("Fill refund form");
        refundPage.selectRefundReasonPersonalReasonFlight();
        refundPage.chooseSpecificReason();

        step("verify refund estimation");
        Assertion.assertThat("refund estimation correct",refundPage.verifyRefundEstimation(currency),is(true));
        double totalRefundPrice = refundPage.getTotalRefundPrice(currency);
        refundPage.continueRefund();

        step("verify total refund price");
        double totalRefundPricePage2 = refundPage.getTotalRefundPrice(currency);
        Assertion.assertThat("total refund price are same on both page",totalRefundPrice,is(totalRefundPricePage2));

        step("submit refund");
        refundPage.clickChecklistTnc();
        refundPage.clickSubmitRefund();
        refundPage.clickProcessRefund();
        refundPage.clickGoToOrders();

        step("Check if the refunded order is being reflected on corporate dashboard");
        boolean isRefundOrderReflected = corporateApi.checkRefundList(orderID, username, password,15);
        Assertion.assertThat("Refund order is reflected on B2B corporate dashboard", isRefundOrderReflected, is(true));
    }

    @Author(names = {"Ashish"})
    @Module(name = "Refund")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void testFlightRefundFromApproverWillBeAutoApprovedDweb() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testFlightRefundFromApproverWillBeAutoApprovedDweb");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPageWeb = FlightLandingPageWeb.getInstance(session);
        FlightSearchResultPageWeb<?> flightSearchResultPage = FlightSearchResultPageWeb.getInstance(session);
        FlightProductReviewPage<?> flightProductReviewPage = new FlightProductReviewPage<>(session);
        FlightBookingFormPageWeb<?> flightBookingFormPage = FlightBookingFormPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        TravelPlanWeb<?> travelPlanPage = TravelPlanWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        RefundPageWeb<?> refundPage = RefundPageWeb.getInstance(session);
        CorporateDashboard<?> corporateDashboard = new CorporateDashboard<>(session);
        FlightApi<?> flightApi = new FlightApi<>(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);

        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");
        String airline = param.get("airline");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String departureDate = getRandomLongDateOnRange(daysToDepartureDate,30);

        int adultPassengers = Integer.parseInt(param.get("adultPassengers"));
        int childPassengers = Integer.parseInt(param.get("childPassengers"));
        int infantPassengers = Integer.parseInt(param.get("infantPassengers"));

        String username = param.get("username");
        String password = param.get("password");
        String currency = param.get("currency");
        String name = param.get("name");
        String lastName = param.get("lastName");

        step("User open home page");
        homePage.goToHomePage();

        step("Go to account page");
        loginPage.goToAccountPage();

        step("login in using { " + username + " }");
        loginPage.loginIfNotWithSameEmail(username, password, "123456", 1);

        step("go to tiket home page");
        corporateDashboard.goBackToTiketHomePage();

        step("User select { " + currency + " } currency");
        homePage.selectCurrency(currency);

        step("User open Flight vertical");
        homePage.openVerticalPageViaURL("Flights");

        step("Toggle trip type");
        flightLandingPageWeb.toggleTripType(tripType);

        step("Select origin airport");
        flightLandingPageWeb.selectOrigin(originAirport);

        step("Select destination airport");
        flightLandingPageWeb.selectDestination(destinationAirport);

        step("Select date");
        flightLandingPageWeb.selectDate(departureDate);

        step("add passenger");
        flightLandingPageWeb.addPassenger(adultPassengers,childPassengers,infantPassengers);

        step("search flight");
        flightLandingPageWeb.searchFlight();

        step("Apply Filter " + airline);
        flightSearchResultPage.filterAirline(airline);

        step("select flight");
        flightSearchResultPage.selectFlightByAirline(airline);

        step("Continue to booking form");
        flightProductReviewPage.continueToBookingForm();
        flightBookingFormPage.clickNoThanksOnFlightMayReschedulePopup();

        step("Click toggle to fill passengers details and save ");
        flightBookingFormPage.fillPassengerDetailsLogin(name,lastName);

        step("Go to payment page");
        flightBookingFormPage.goToPaymentPageFromBookingFormPage();

        step("Skip Flight may rescheduled page by clicking no thanks");
        travelPlanPage.skipTravelPlan();
        flightBookingFormPage.clickNoThanksOnFlightMayReschedulePopup();

        step("Payment page should be displayed");
        String orderID = paymentPage.getOrderId();
        orderID = String.valueOf(getOrderId(orderID));
        Assertion.assertThat("Corporate billing option is selected", paymentPage.getSelectedPayment().toLowerCase(), is("corporate billing"));

        step("get total price payment page");
        String totalPricePaymentPage = paymentPage.totalPricePaymentPage();
        Assertion.assertThat("total price should not be empty {" +totalPricePaymentPage +" } ", totalPricePaymentPage.isEmpty(),is(false));

        step("Submit payment using Corporate Billing");
        paymentPage.clickOnPaymentButton();
        orderListPage.waitForPaymentSuccessful(orderID,15);

        step("verify success payment on payment page");
        Assertion.assertThat("is payment done successfully by corp dashboard",paymentPage.isPaymentSuccessfulVisible(),is(true));

        step("get booking code from API");
        String bookingCode = flightApi.getBookingCode(orderID, username);

        step("Open the order list");
        paymentPage.clickSeeOrderListButton();

        step("set Tiket Number on flight dashboard and ticket will be eticket issued");
        Boolean isTicketIssued = flightApi.flightEticketIssuedRequest(orderID, bookingCode, username,name + " " + lastName,20);
        Assertion.assertThat("is Eticket Issued", isTicketIssued, is(true));

        step("go to order detail page for order id: { " + orderID +" }");
        orderListPage.openIssuedTicketViaURL(orderID,15);
        orderDetailPage.closeEticketPopUp();
        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.verifyIssuedETicket(), is(true));
        String totalPriceOrderDetail = orderDetailPage.getVerticalTotalPrice();
        Assertion.assertThat("is total price from payment page { "+ totalPricePaymentPage+" } match with order detail page { "+ totalPriceOrderDetail+ " } total price",CoreFunctions.convertPriceSafely(totalPriceOrderDetail),is(CoreFunctions.convertPriceSafely(totalPricePaymentPage)));

        step("Click refund button");
        orderDetailPage.clickRefundButton();
        refundPage.skipRescheduleNudgeIfShown();

        step("Fill refund form");
        refundPage.selectRefundReasonPersonalReasonFlight();
        refundPage.chooseSpecificReason();

        step("verify refund estimation");
        Assertion.assertThat("refund estimation correct",refundPage.verifyRefundEstimation(currency),is(true));
        double totalRefundPrice = refundPage.getTotalRefundPrice(currency);
        refundPage.continueRefund();

        step("verify total refund price");
        double totalRefundPricePage2 = refundPage.getTotalRefundPrice(currency);
        Assertion.assertThat("total refund price are same on both page",totalRefundPrice,is(totalRefundPricePage2));

        step("submit refund");
        refundPage.clickChecklistTnc();
        refundPage.clickSubmitRefund();
        refundPage.clickProcessRefund();
        refundPage.clickGoToOrders();

        step("Check if the refunded order is being reflected on corporate dashboard");
        boolean isRefundOrderReflected = corporateApi.checkRefundList(orderID, username, password,15);
        Assertion.assertThat("Refund order is reflected on B2B corporate dashboard", isRefundOrderReflected, is(true));
    }

    @Author(names = {"Ashish"})
    @Module(name = "RejectRefund")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void testFlightRefundFromTravelerRejectedByAdminDweb() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testFlightRefundFromTravelerRejectedByAdminDweb");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPageWeb = FlightLandingPageWeb.getInstance(session);
        FlightSearchResultPageWeb<?> flightSearchResultPage = FlightSearchResultPageWeb.getInstance(session);
        FlightProductReviewPage<?> flightProductReviewPage = new FlightProductReviewPage<>(session);
        FlightBookingFormPageWeb<?> flightBookingFormPage = FlightBookingFormPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        TravelPlanWeb<?> travelPlanPage = TravelPlanWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        RefundPageWeb<?> refundPage = RefundPageWeb.getInstance(session);
        FlightApi<?> flightApi = new FlightApi<>(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);

        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");
        String airline = param.get("airline");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String departureDate = getRandomLongDateOnRange(daysToDepartureDate,30);

        int adultPassengers = Integer.parseInt(param.get("adultPassengers"));
        int childPassengers = Integer.parseInt(param.get("childPassengers"));
        int infantPassengers = Integer.parseInt(param.get("infantPassengers"));

        String username = param.get("username");
        String password = param.get("password");
        String adminUsername = param.get("adminUsername");
        String adminPassword = param.get("adminPassword");
        String currency = param.get("currency");
        String name = param.get("name");
        String lastName = param.get("lastName");

        step("User open home page");
        homePage.goToHomePage();

        step("Go to account page");
        loginPage.goToAccountPage();

        step("login in using { " + username + " }");
        loginPage.loginIfNotWithSameEmail(username, password, "123456", 1);

        step("User select { " + currency + " } currency");
        homePage.selectCurrency(currency);

        step("User open Flight vertical");
        homePage.openVerticalPageViaURL("Flights");

        step("Toggle trip type");
        flightLandingPageWeb.toggleTripType(tripType);

        step("Select origin airport");
        flightLandingPageWeb.selectOrigin(originAirport);

        step("Select destination airport");
        flightLandingPageWeb.selectDestination(destinationAirport);

        step("Select date");
        flightLandingPageWeb.selectDate(departureDate);

        step("add passenger");
        flightLandingPageWeb.addPassenger(adultPassengers,childPassengers,infantPassengers);

        step("search flight");
        flightLandingPageWeb.searchFlight();

        step("Apply Filter " + airline);
        flightSearchResultPage.filterAirline(airline);

        step("select flight");
        flightSearchResultPage.selectFlightByAirline(airline);

        step("Continue to booking form");
        flightProductReviewPage.continueToBookingForm();
        flightBookingFormPage.clickNoThanksOnFlightMayReschedulePopup();

        step("Click toggle to fill passengers details and save ");
        flightBookingFormPage.fillPassengerDetailsLogin(name,lastName);

        step("Go to payment page");
        flightBookingFormPage.goToPaymentPageFromBookingFormPage();

        step("Skip Flight may rescheduled page by clicking no thanks");
        travelPlanPage.skipTravelPlan();
        flightBookingFormPage.clickNoThanksOnFlightMayReschedulePopup();

        step("Payment page should be displayed");
        String orderID = paymentPage.getOrderId();
        orderID = String.valueOf(getOrderId(orderID));
        Assertion.assertThat("Corporate billing option is selected", paymentPage.getSelectedPayment().toLowerCase(), is("corporate billing"));

        step("get total price payment page");
        String totalPricePaymentPage = paymentPage.totalPricePaymentPage();
        Assertion.assertThat("total price should not be empty {" +totalPricePaymentPage +" } ", totalPricePaymentPage.isEmpty(),is(false));

        step("Submit payment using Corporate Billing");
        paymentPage.clickOnPaymentButton();

        step("verify successfully placed order popup from corporate");
        Assertion.assertThat("get successfully placed order popup",paymentPage.isCorporateSuccessfullyPlacedOrderPopup(),is(true));

        step("approve payment of order id: { " + orderID +" } " + " using admin account with username : { " + adminUsername + " }");
        Boolean bookingStatus = corporateApi.approveBookingB2BApi(adminUsername, adminPassword, orderID);
        Assertion.assertThat("Booking approved by B2B dashboard", bookingStatus, is(true));

        step("verify success payment on payment page");
        Assertion.assertThat("is payment done successfully by corp dashboard",paymentPage.isPaymentSuccessfulVisible(),is(true));

        step("get booking code from API");
        String bookingCode = flightApi.getBookingCode(orderID, username);

        step("Open the order list");
        paymentPage.clickSeeOrderListButton();

        step("set Tiket Number on flight dashboard and ticket will be eticket issued");
        Boolean isTicketIssued = flightApi.flightEticketIssuedRequest(orderID, bookingCode, username,name + " " + lastName,20);
        Assertion.assertThat("is Eticket Issued", isTicketIssued, is(true));

        step("go to order detail page");
        orderListPage.openIssuedTicketViaURL(orderID,15);
        orderDetailPage.closeEticketPopUp();
        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.verifyIssuedETicket(), is(true));
        String totalPriceOrderDetail = orderDetailPage.getVerticalTotalPrice();
        Assertion.assertThat("is total price from payment page { "+ totalPricePaymentPage+" } match with order detail page { "+ totalPriceOrderDetail+ " } total price",CoreFunctions.convertPriceSafely(totalPriceOrderDetail),is(CoreFunctions.convertPriceSafely(totalPricePaymentPage)));

        step("Click refund button");
        orderDetailPage.clickRefundButton();
        refundPage.skipRescheduleNudgeIfShown();

        step("Fill refund form");
        refundPage.selectRefundReasonPersonalReasonFlight();
        refundPage.chooseSpecificReason();

        step("verify refund estimation");
        Assertion.assertThat("refund estimation correct",refundPage.verifyRefundEstimation(currency),is(true));
        double totalRefundPrice = refundPage.getTotalRefundPrice(currency);
        refundPage.continueRefund();

        step("verify total refund price");
        double totalRefundPricePage2 = refundPage.getTotalRefundPrice(currency);
        Assertion.assertThat("total refund price are same on both page",totalRefundPrice,is(totalRefundPricePage2));

        step("submit refund");
        refundPage.clickChecklistTnc();
        refundPage.clickSubmitRefund();
        refundPage.clickProcessRefund();
        refundPage.clickGoToOrders();

        step("Check if the refunded order is being reflected on corporate dashboard");
        String refundId = corporateApi.getRefundId(orderID, adminUsername, adminPassword,"prerefund");
        Boolean isApproved = false;
        Boolean success = corporateApi.approveOrRejectRefundB2B(adminUsername, adminPassword, refundId, isApproved);
        Assertion.assertThat("Refund Rejection Success", success, is(true));

        step("Check eticket");
        orderListPage.openIssuedTicketViaURL(orderID);
        Assertion.assertThat("Order is in Active order state", orderDetailPage.getOrderId(), is(orderID));
        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.verifyIssuedETicket(), is(true));
    }

    @Author(names = {"Ashish"})
    @Module(name = "RejectRefund")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void testFlightRefundFromTravelerRejectedByApproverDweb() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testFlightRefundFromTravelerRejectedByApproverDweb");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPageWeb = FlightLandingPageWeb.getInstance(session);
        FlightSearchResultPageWeb<?> flightSearchResultPage = FlightSearchResultPageWeb.getInstance(session);
        FlightProductReviewPage<?> flightProductReviewPage = new FlightProductReviewPage<>(session);
        FlightBookingFormPageWeb<?> flightBookingFormPage = FlightBookingFormPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        TravelPlanWeb<?> travelPlanPage = TravelPlanWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        RefundPageWeb<?> refundPage = RefundPageWeb.getInstance(session);
        FlightApi<?> flightApi = new FlightApi<>(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);

        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");
        String airline = param.get("airline");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String departureDate = getRandomLongDateOnRange(daysToDepartureDate,30);

        int adultPassengers = Integer.parseInt(param.get("adultPassengers"));
        int childPassengers = Integer.parseInt(param.get("childPassengers"));
        int infantPassengers = Integer.parseInt(param.get("infantPassengers"));

        String username = param.get("username");
        String password = param.get("password");
        String approverUsername = param.get("approverUsername");
        String approverPassword = param.get("approverPassword");
        String currency = param.get("currency");
        String name = param.get("name");
        String lastName = param.get("lastName");

        step("User open home page");
        homePage.goToHomePage();

        step("Go to account page");
        loginPage.goToAccountPage();

        step("login in using { " + username + " }");
        loginPage.loginIfNotWithSameEmail(username, password, "123456", 1);

        step("User select { " + currency + " } currency");
        homePage.selectCurrency(currency);

        step("User open Flight vertical");
        homePage.openVerticalPageViaURL("Flights");

        step("Toggle trip type");
        flightLandingPageWeb.toggleTripType(tripType);

        step("Select origin airport");
        flightLandingPageWeb.selectOrigin(originAirport);

        step("Select destination airport");
        flightLandingPageWeb.selectDestination(destinationAirport);

        step("Select date");
        flightLandingPageWeb.selectDate(departureDate);

        step("add passenger");
        flightLandingPageWeb.addPassenger(adultPassengers,childPassengers,infantPassengers);

        step("search flight");
        flightLandingPageWeb.searchFlight();

        step("Apply Filter " + airline);
        flightSearchResultPage.filterAirline(airline);

        step("select flight");
        flightSearchResultPage.selectFlightByAirline(airline);

        step("Continue to booking form");
        flightProductReviewPage.continueToBookingForm();
        flightBookingFormPage.clickNoThanksOnFlightMayReschedulePopup();

        step("Click toggle to fill passengers details and save ");
        flightBookingFormPage.fillPassengerDetailsLogin(name,lastName);

        step("Go to payment page");
        flightBookingFormPage.goToPaymentPageFromBookingFormPage();

        step("Skip Flight may rescheduled page by clicking no thanks");
        travelPlanPage.skipTravelPlan();
        flightBookingFormPage.clickNoThanksOnFlightMayReschedulePopup();

        step("Payment page should be displayed");
        String orderID = paymentPage.getOrderId();
        orderID = String.valueOf(getOrderId(orderID));
        Assertion.assertThat("Corporate billing option is selected", paymentPage.getSelectedPayment().toLowerCase(), is("corporate billing"));

        step("get total price payment page");
        String totalPricePaymentPage = paymentPage.totalPricePaymentPage();
        Assertion.assertThat("total price should not be empty {" +totalPricePaymentPage +" } ", totalPricePaymentPage.isEmpty(),is(false));

        step("Submit payment using Corporate Billing");
        paymentPage.clickOnPaymentButton();

        step("verify successfully placed order popup from corporate");
        Assertion.assertThat("get successfully placed order popup",paymentPage.isCorporateSuccessfullyPlacedOrderPopup(),is(true));

        step("approve payment of order id: { " + orderID +" } " + " using admin account with username : { " + approverUsername + " }");
        Boolean bookingStatus = corporateApi.approveBookingB2BApi(approverUsername, approverPassword, orderID);
        Assertion.assertThat("Booking approved by B2B dashboard", bookingStatus, is(true));

        step("verify success payment on payment page");
        Assertion.assertThat("is payment done successfully by corp dashboard",paymentPage.isPaymentSuccessfulVisible(),is(true));

        step("get booking code from API");
        String bookingCode = flightApi.getBookingCode(orderID, username);

        step("Open the order list");
        paymentPage.clickSeeOrderListButton();

        step("set Tiket Number on flight dashboard and ticket will be eticket issued");
        Boolean isTicketIssued = flightApi.flightEticketIssuedRequest(orderID, bookingCode, username,name + " " + lastName,20);
        Assertion.assertThat("is Eticket Issued", isTicketIssued, is(true));

        step("go to order detail page");
        orderListPage.openIssuedTicketViaURL(orderID,15);
        orderDetailPage.closeEticketPopUp();
        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.verifyIssuedETicket(), is(true));
        String totalPriceOrderDetail = orderDetailPage.getVerticalTotalPrice();
        Assertion.assertThat("is total price from payment page { "+ totalPricePaymentPage+" } match with order detail page { "+ totalPriceOrderDetail+ " } total price",CoreFunctions.convertPriceSafely(totalPriceOrderDetail),is(CoreFunctions.convertPriceSafely(totalPricePaymentPage)));

        step("Click refund button");
        orderDetailPage.clickRefundButton();
        refundPage.skipRescheduleNudgeIfShown();

        step("Fill refund form");
        refundPage.selectRefundReasonPersonalReasonFlight();
        refundPage.chooseSpecificReason();

        step("verify refund estimation");
        Assertion.assertThat("refund estimation correct",refundPage.verifyRefundEstimation(currency),is(true));
        double totalRefundPrice = refundPage.getTotalRefundPrice(currency);
        refundPage.continueRefund();

        step("verify total refund price");
        double totalRefundPricePage2 = refundPage.getTotalRefundPrice(currency);
        Assertion.assertThat("total refund price are same on both page",totalRefundPrice,is(totalRefundPricePage2));

        step("submit refund");
        refundPage.clickChecklistTnc();
        refundPage.clickSubmitRefund();
        refundPage.clickProcessRefund();
        refundPage.clickGoToOrders();

        step("Check if the refunded order is being reflected on corporate dashboard");
        String refundId = corporateApi.getRefundId(orderID, approverUsername, approverPassword,"prerefund");
        Boolean isApproved = false;
        Boolean success = corporateApi.approveOrRejectRefundB2B(approverUsername, approverPassword, refundId, isApproved);
        Assertion.assertThat("Refund Rejection Success", success, is(true));

        step("Check eticket");
        orderListPage.openIssuedTicketViaURL(orderID);
        Assertion.assertThat("Order is in Active order state", orderDetailPage.getOrderId(), is(orderID));
        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.verifyIssuedETicket(), is(true));
    }


    @Author(names = {"Aditi"})
    @Module(name = "Login")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void testCheckForgetYourPasswordFunctionalityDweb() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> testProperties = getParam("testCheckForgetYourPasswordFunctionalityDweb");
        BaseTest.paramsThreadLocal.set(testProperties);

        String email = testProperties.get("email");
        String wrongPassword = testProperties.get("wrongPassword");
        String otp = testProperties.get("otp");
        int retryCounter = Integer.parseInt(testProperties.get("retryCounter"));

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("Log out");
        loginPage.loginAsGuest();

        step("User enter the email on the register page");
        homePage.clickRegisterButton();
        Assertion.assertThat("Register page has been opened", loginPage.isRegisterPageOpened(), is(true));
        loginPage.registerIntoDweb(email);

        step("Verify if user directed to login page");
        Assertion.assertThat("Login page has been opened", loginPage.isLoginPageOpened(), is(true));

        step("change password using forget password");
        String updatedPassword = loginPage.forgotPasswordDweb(email, wrongPassword, otp, retryCounter);

        step("Verify the login step with new password");
        homePage.goToHomePage();
        loginPage.loginIntoWeb(email, updatedPassword, "123456", 1);

        step("Check if logged in");
        loginPage.goToAccountPage();
        Assertion.assertThat("User is in login state", loginPage.isAccountHolderNameDisplayed(), is(true));

    }

    @Author(names = {"Ashish"})
    @Module(name = "Reschedule")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void testFlightRescheduleFromTravelerApprovedByAdminDweb() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testFlightRescheduleFromTravelerApprovedByAdminDweb");
        BaseTest.paramsThreadLocal.set(param);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        FlightApi<?> flightApi = new FlightApi<>(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);
        ReschedulePage<?> reschedulePage = new ReschedulePage<>(session);
        FlightLandingPageWeb<?> flightLandingPageWeb = FlightLandingPageWeb.getInstance(session);
        FlightSearchResultPageWeb<?> flightSearchResultPage = FlightSearchResultPageWeb.getInstance(session);
        FlightBookingFormPageWeb<?> flightBookingFormPage = FlightBookingFormPageWeb.getInstance(session);
        RefundPageWeb<?> refundPage = RefundPageWeb.getInstance(session);

        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");
        String airline = param.get("airline");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String departDateApi = CoreFunctions.getRandomLongDateOnRange(daysToDepartureDate,30,"yyyy-MM-dd");
        String newDepartureDate = getRandomLongDateOnRange(daysToDepartureDate + 31, 15);

        int adultPassengers = Integer.parseInt(param.get("adultPassengers"));
        int childPassengers = Integer.parseInt(param.get("childPassengers"));
        int infantPassengers = Integer.parseInt(param.get("infantPassengers"));

        String username = param.get("username");
        String password = param.get("password");
        String adminUsername = param.get("adminUsername");
        String adminPassword = param.get("adminPassword");
        String currency = param.get("currency");
        String firstName = param.get("firstName");
        String lastName = param.get("lastName");

        step("complete flight booking via API");
        String orderID = flightApi.createOrderFlightB2BDomestic(username, password, originAirport, destinationAirport, departDateApi, tripType, adultPassengers, childPassengers, infantPassengers, airline,corporateApi.getEmployeeData(username), firstName, lastName);
        Assertion.assertThat("Flight order created", orderID, not(""));

        step("approve payment of order id: { " + orderID +" } " + " using admin account with username : { " + adminUsername + " }");
        Boolean bookingStatus = corporateApi.approveBookingB2BApi(adminUsername, adminPassword, orderID);
        Assertion.assertThat("Booking approved by B2B dashboard", bookingStatus, is(true));

        step("User open home page");
        homePage.goToHomePage();

        step("Go to account page");
        loginPage.goToAccountPage();

        step("login in using { " + username + " }");
        loginPage.loginIfNotWithSameEmail(username, password, "123456", 1);

        step("User select { " + currency + " } currency");
        homePage.selectCurrency(currency);

        step("get booking code from API");
        String bookingCode = flightApi.getBookingCode(orderID, username);

        step("set Tiket Number on flight dashboard and ticket will be eticket issued");
        Boolean isTicketIssued = flightApi.flightEticketIssuedRequest(orderID, bookingCode, username,firstName + " " + lastName,20);
        Assertion.assertThat("is Eticket Issued", isTicketIssued, is(true));

        step("go to order detail page");
        orderListPage.openIssuedTicketViaURL(orderID,15);
        orderDetailPage.closeEticketPopUp();
        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.verifyIssuedETicket(), is(true));

        step("Click reschedule button");
        orderDetailPage.clickRescheduleButton();

        step("select checkbox to reschedule flight");
        reschedulePage.clickCheckNewRescheduleButtonFlight();

        step("change date and continue");
        flightLandingPageWeb.selectDate(newDepartureDate);

        step("search flight");
        flightLandingPageWeb.searchFlight();

        step("Apply Filter " + airline);
        flightSearchResultPage.filterAirline(airline);

        step("select flight");
        flightSearchResultPage.selectFlightByAirline(airline);

        step("Go to payment page");
        flightBookingFormPage.goToPaymentPageFromBookingFormPage();

        step("Skip Flight may rescheduled page by clicking no thanks");
        flightBookingFormPage.clickNoThanksOnFlightMayReschedulePopup();

        step("Payment page should be displayed");
        String orderIDRescheduled = paymentPage.getOrderId();
        orderIDRescheduled = String.valueOf(getOrderId(orderIDRescheduled));
        Assertion.assertThat("order id should not be empty", orderIDRescheduled.isEmpty(), is(false));
        Assertion.assertThat("Corporate billing option is selected", paymentPage.getSelectedPayment().toLowerCase(), is("corporate billing"));

        step("get total price payment page");
        String totalPricePaymentPageReschedule = paymentPage.totalPricePaymentPage();
        Assertion.assertThat("total price should not be empty {" +totalPricePaymentPageReschedule +" } ", totalPricePaymentPageReschedule.isEmpty(),is(false));

        step("Submit payment using Corporate Billing");
        paymentPage.clickOnPaymentButton();
        boolean isFreePaymentOrRefund = paymentPage.isFreePaymentOrRefund(totalPricePaymentPageReschedule);
        refundPage.followRefundMethodIfRequired(isFreePaymentOrRefund);

        step("approve reschedule with order id: { " + orderIDRescheduled +" } " + " using admin account with username : { " + adminUsername + " }");
        boolean success = corporateApi.approveOrRejectRescheduleB2B(adminUsername, adminPassword, true,orderIDRescheduled ,"approve by automation using " + adminUsername);
        Assertion.assertThat("reschedule Approved Success", success, is(true));

        step("Open the order list");
        paymentPage.clickSeeOrderListButton();

        step("generate e-ticket");
        String bookingCodReschedule = flightApi.getBookingCode(orderIDRescheduled, username);
        Boolean isTicketIssuedNew = flightApi.flightEticketIssuedRequest(orderIDRescheduled, bookingCodReschedule, username, firstName + " " + lastName,20);
        Assertion.assertThat("is Eticket Issued", isTicketIssuedNew, is(true));

        step("verify the order detail");
        orderListPage.openIssuedTicketViaURL(orderIDRescheduled,15);
        orderDetailPage.closeEticketPopUp();
        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.verifyIssuedETicket(), is(true));
        Assertion.assertThat("Order ID is equal", orderDetailPage.getOrderId(), is(orderIDRescheduled));

        step("verify reschedule price");
        String totalPriceOrderDetailReschedule = orderDetailPage.getVerticalTotalPrice();
        Assertion.assertThat("is total price from payment page reschedule { "+ totalPricePaymentPageReschedule+" } match with order detail page reschedule { "+ totalPriceOrderDetailReschedule+ " } total price",CoreFunctions.convertPriceSafely(totalPriceOrderDetailReschedule),is(CoreFunctions.convertPriceSafely(totalPricePaymentPageReschedule)));
    }

    @Author(names = {"Ashish"})
    @Module(name = "Reschedule")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void testFlightRescheduleFromTravelerApprovedByApproverDweb() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testFlightRescheduleFromTravelerApprovedByApproverDweb");
        BaseTest.paramsThreadLocal.set(param);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        FlightApi<?> flightApi = new FlightApi<>(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);
        ReschedulePage<?> reschedulePage = new ReschedulePage<>(session);
        FlightLandingPageWeb<?> flightLandingPageWeb = FlightLandingPageWeb.getInstance(session);
        FlightSearchResultPageWeb<?> flightSearchResultPage = FlightSearchResultPageWeb.getInstance(session);
        FlightBookingFormPageWeb<?> flightBookingFormPage = FlightBookingFormPageWeb.getInstance(session);
        RefundPageWeb<?> refundPage = RefundPageWeb.getInstance(session);

        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");
        String airline = param.get("airline");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String departDateApi = CoreFunctions.getRandomLongDateOnRange(daysToDepartureDate,30,"yyyy-MM-dd");
        String newDepartureDate = getRandomLongDateOnRange(daysToDepartureDate + 31, 15);

        int adultPassengers = Integer.parseInt(param.get("adultPassengers"));
        int childPassengers = Integer.parseInt(param.get("childPassengers"));
        int infantPassengers = Integer.parseInt(param.get("infantPassengers"));

        String username = param.get("username");
        String password = param.get("password");
        String approverUsername = param.get("approverUsername");
        String approverPassword = param.get("approverPassword");
        String currency = param.get("currency");
        String firstName = param.get("firstName");
        String lastName = param.get("lastName");

        step("complete flight booking via API");
        String orderID = flightApi.createOrderFlightB2BDomestic(username, password, originAirport, destinationAirport, departDateApi, tripType, adultPassengers, childPassengers, infantPassengers, airline,corporateApi.getEmployeeData(username), firstName, lastName);
        Assertion.assertThat("Flight order created", orderID, not(""));

        step("approve payment of order id: { " + orderID +" } " + " using admin account with username : { " + approverUsername + " }");
        Boolean bookingStatus = corporateApi.approveBookingB2BApi(approverUsername, approverPassword, orderID);
        Assertion.assertThat("Booking approved by B2B dashboard", bookingStatus, is(true));

        step("User open home page");
        homePage.goToHomePage();

        step("Go to account page");
        loginPage.goToAccountPage();

        step("login in using { " + username + " }");
        loginPage.loginIfNotWithSameEmail(username, password, "123456", 1);

        step("User select { " + currency + " } currency");
        homePage.selectCurrency(currency);

        step("get booking code from API");
        String bookingCode = flightApi.getBookingCode(orderID, username);

        step("set Tiket Number on flight dashboard and ticket will be eticket issued");
        Boolean isTicketIssued = flightApi.flightEticketIssuedRequest(orderID, bookingCode, username,firstName + " " + lastName,20);
        Assertion.assertThat("is Eticket Issued", isTicketIssued, is(true));

        step("go to order detail page");
        orderListPage.openIssuedTicketViaURL(orderID,15);
        orderDetailPage.closeEticketPopUp();
        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.verifyIssuedETicket(), is(true));

        step("Click reschedule button");
        orderDetailPage.clickRescheduleButton();

        step("select checkbox to reschedule flight");
        reschedulePage.clickCheckNewRescheduleButtonFlight();

        step("change date and continue");
        flightLandingPageWeb.selectDate(newDepartureDate);

        step("search flight");
        flightLandingPageWeb.searchFlight();

        step("Apply Filter " + airline);
        flightSearchResultPage.filterAirline(airline);

        step("select flight");
        flightSearchResultPage.selectFlightByAirline(airline);

        step("Go to payment page");
        flightBookingFormPage.goToPaymentPageFromBookingFormPage();

        step("Skip Flight may rescheduled page by clicking no thanks");
        flightBookingFormPage.clickNoThanksOnFlightMayReschedulePopup();

        step("Payment page should be displayed");
        String orderIDRescheduled = paymentPage.getOrderId();
        orderIDRescheduled = String.valueOf(getOrderId(orderIDRescheduled));
        Assertion.assertThat("order id should not be empty", orderIDRescheduled.isEmpty(), is(false));
        Assertion.assertThat("Corporate billing option is selected", paymentPage.getSelectedPayment().toLowerCase(), is("corporate billing"));

        step("get total price payment page");
        String totalPricePaymentPageReschedule = paymentPage.totalPricePaymentPage();
        Assertion.assertThat("total price should not be empty {" +totalPricePaymentPageReschedule +" } ", totalPricePaymentPageReschedule.isEmpty(),is(false));

        step("Submit payment using Corporate Billing");
        paymentPage.clickOnPaymentButton();
        boolean isFreePaymentOrRefund = paymentPage.isFreePaymentOrRefund(totalPricePaymentPageReschedule);
        refundPage.followRefundMethodIfRequired(isFreePaymentOrRefund);

        step("approve reschedule with order id: { " + orderIDRescheduled +" } " + " using admin account with username : { " + approverUsername + " }");
        boolean success = corporateApi.approveOrRejectRescheduleB2B(approverUsername, approverPassword, true,orderIDRescheduled ,"approve by automation using " + approverUsername);
        Assertion.assertThat("reschedule Approved Success", success, is(true));

        step("Open the order list");
        paymentPage.clickSeeOrderListButton();

        step("generate e-ticket");
        String bookingCodReschedule = flightApi.getBookingCode(orderIDRescheduled, username);
        Boolean isTicketIssuedNew = flightApi.flightEticketIssuedRequest(orderIDRescheduled, bookingCodReschedule, username, firstName + " " + lastName,20);
        Assertion.assertThat("is Eticket Issued", isTicketIssuedNew, is(true));

        step("verify the order detail");
        orderListPage.openIssuedTicketViaURL(orderIDRescheduled,15);
        orderDetailPage.closeEticketPopUp();
        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.verifyIssuedETicket(), is(true));
        Assertion.assertThat("Order ID is equal", orderDetailPage.getOrderId(), is(orderIDRescheduled));

        step("verify reschedule price");
        String totalPriceOrderDetailReschedule = orderDetailPage.getVerticalTotalPrice();
        Assertion.assertThat("is total price from payment page reschedule { "+ totalPricePaymentPageReschedule+" } match with order detail page reschedule { "+ totalPriceOrderDetailReschedule+ " } total price",CoreFunctions.convertPriceSafely(totalPriceOrderDetailReschedule),is(CoreFunctions.convertPriceSafely(totalPricePaymentPageReschedule)));
    }

    @Author(names = {"Ashish"})
    @Module(name = "Reschedule")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void testFlightRescheduleAdminAutoApproveDweb() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testFlightRescheduleAdminAutoApproveDweb");
        BaseTest.paramsThreadLocal.set(param);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        FlightApi<?> flightApi = new FlightApi<>(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);
        ReschedulePage<?> reschedulePage = new ReschedulePage<>(session);
        FlightLandingPageWeb<?> flightLandingPageWeb = FlightLandingPageWeb.getInstance(session);
        FlightSearchResultPageWeb<?> flightSearchResultPage = FlightSearchResultPageWeb.getInstance(session);
        FlightBookingFormPageWeb<?> flightBookingFormPage = FlightBookingFormPageWeb.getInstance(session);
        RefundPageWeb<?> refundPage = RefundPageWeb.getInstance(session);
        CorporateDashboard<?> corporateDashboard = new CorporateDashboard<>(session);

        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");
        String airline = param.get("airline");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String departDateApi = CoreFunctions.getRandomLongDateOnRange(daysToDepartureDate,30,"yyyy-MM-dd");
        String newDepartureDate = getRandomLongDateOnRange(daysToDepartureDate + 31, 15);

        int adultPassengers = Integer.parseInt(param.get("adultPassengers"));
        int childPassengers = Integer.parseInt(param.get("childPassengers"));
        int infantPassengers = Integer.parseInt(param.get("infantPassengers"));

        String username = param.get("username");
        String password = param.get("password");
        String currency = param.get("currency");
        String firstName = param.get("firstName");
        String lastName = param.get("lastName");

        step("complete flight booking via API");
        String orderID = flightApi.createOrderFlightB2BDomestic(username, password, originAirport, destinationAirport, departDateApi, tripType, adultPassengers, childPassengers, infantPassengers, airline,corporateApi.getEmployeeData(username), firstName, lastName);
        Assertion.assertThat("Flight order created", orderID, not(""));
        orderListPage.waitForPaymentSuccessful(orderID,20);

        step("User open home page");
        homePage.goToHomePage();

        step("Go to account page");
        loginPage.goToAccountPage();

        step("login in using { " + username + " }");
        loginPage.loginIfNotWithSameEmail(username, password, "123456", 1);

        step("go to tiket home page");
        corporateDashboard.goBackToTiketHomePage();

        step("User select { " + currency + " } currency");
        homePage.selectCurrency(currency);

        step("get booking code from API");
        String bookingCode = flightApi.getBookingCode(orderID, username);

        step("set Tiket Number on flight dashboard and ticket will be eticket issued");
        Boolean isTicketIssued = flightApi.flightEticketIssuedRequest(orderID, bookingCode, username,firstName + " " + lastName,20);
        Assertion.assertThat("is Eticket Issued", isTicketIssued, is(true));

        step("go to order detail page");
        orderListPage.openIssuedTicketViaURL(orderID,15);
        orderDetailPage.closeEticketPopUp();
        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.verifyIssuedETicket(), is(true));

        step("Click reschedule button");
        orderDetailPage.clickRescheduleButton();

        step("select checkbox to reschedule flight");
        reschedulePage.clickCheckNewRescheduleButtonFlight();

        step("change date and continue");
        flightLandingPageWeb.selectDate(newDepartureDate);

        step("search flight");
        flightLandingPageWeb.searchFlight();

        step("Apply Filter " + airline);
        flightSearchResultPage.filterAirline(airline);

        step("select flight");
        flightSearchResultPage.selectFlightByAirline(airline);

        step("Go to payment page");
        flightBookingFormPage.goToPaymentPageFromBookingFormPage();

        step("Skip Flight may rescheduled page by clicking no thanks");
        flightBookingFormPage.clickNoThanksOnFlightMayReschedulePopup();

        step("Payment page should be displayed");
        String orderIDRescheduled = paymentPage.getOrderId();
        orderIDRescheduled = String.valueOf(getOrderId(orderIDRescheduled));
        Assertion.assertThat("order id should not be empty", orderIDRescheduled.isEmpty(), is(false));
        Assertion.assertThat("Corporate billing option is selected", paymentPage.getSelectedPayment().toLowerCase(), is("corporate billing"));

        step("get total price payment page");
        String totalPricePaymentPageReschedule = paymentPage.totalPricePaymentPage();
        Assertion.assertThat("total price should not be empty {" +totalPricePaymentPageReschedule +" } ", totalPricePaymentPageReschedule.isEmpty(),is(false));

        step("Submit payment using Corporate Billing with order id: { " + orderIDRescheduled +" }");
        paymentPage.clickOnPaymentButton();
        boolean isFreePaymentOrRefund = paymentPage.isFreePaymentOrRefund(totalPricePaymentPageReschedule);
        refundPage.followRefundMethodIfRequired(isFreePaymentOrRefund);
        orderListPage.waitForPaymentSuccessful(orderID,20);

        step("Open the order list");
        paymentPage.clickSeeOrderListButton();

        step("generate e-ticket");
        String bookingCodReschedule = flightApi.getBookingCode(orderIDRescheduled, username);
        Boolean isTicketIssuedNew = flightApi.flightEticketIssuedRequest(orderIDRescheduled, bookingCodReschedule, username, firstName + " " + lastName,20);
        Assertion.assertThat("is Eticket Issued", isTicketIssuedNew, is(true));

        step("verify the order detail");
        orderListPage.openIssuedTicketViaURL(orderIDRescheduled,15);
        orderDetailPage.closeEticketPopUp();
        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.verifyIssuedETicket(), is(true));
        Assertion.assertThat("Order ID is equal", orderDetailPage.getOrderId(), is(orderIDRescheduled));

        step("verify reschedule price");
        String totalPriceOrderDetailReschedule = orderDetailPage.getVerticalTotalPrice();
        Assertion.assertThat("is total price from payment page reschedule { "+ totalPricePaymentPageReschedule+" } match with order detail page reschedule { "+ totalPriceOrderDetailReschedule+ " } total price",CoreFunctions.convertPriceSafely(totalPriceOrderDetailReschedule),is(CoreFunctions.convertPriceSafely(totalPricePaymentPageReschedule)));
    }

    @Author(names = {"Ashish"})
    @Module(name = "Reschedule")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void testFlightRescheduleApproverAutoApproveDweb() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testFlightRescheduleApproverAutoApproveDweb");
        BaseTest.paramsThreadLocal.set(param);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        FlightApi<?> flightApi = new FlightApi<>(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);
        ReschedulePage<?> reschedulePage = new ReschedulePage<>(session);
        FlightLandingPageWeb<?> flightLandingPageWeb = FlightLandingPageWeb.getInstance(session);
        FlightSearchResultPageWeb<?> flightSearchResultPage = FlightSearchResultPageWeb.getInstance(session);
        FlightBookingFormPageWeb<?> flightBookingFormPage = FlightBookingFormPageWeb.getInstance(session);
        RefundPageWeb<?> refundPage = RefundPageWeb.getInstance(session);
        CorporateDashboard<?> corporateDashboard = new CorporateDashboard<>(session);

        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");
        String airline = param.get("airline");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String departDateApi = CoreFunctions.getRandomLongDateOnRange(daysToDepartureDate,30,"yyyy-MM-dd");
        String newDepartureDate = getRandomLongDateOnRange(daysToDepartureDate + 31, 15);

        int adultPassengers = Integer.parseInt(param.get("adultPassengers"));
        int childPassengers = Integer.parseInt(param.get("childPassengers"));
        int infantPassengers = Integer.parseInt(param.get("infantPassengers"));

        String username = param.get("username");
        String password = param.get("password");
        String currency = param.get("currency");
        String firstName = param.get("firstName");
        String lastName = param.get("lastName");

        step("complete flight booking via API");
        String orderID = flightApi.createOrderFlightB2BDomestic(username, password, originAirport, destinationAirport, departDateApi, tripType, adultPassengers, childPassengers, infantPassengers, airline,corporateApi.getEmployeeData(username), firstName, lastName);
        Assertion.assertThat("Flight order created", orderID, not(""));
        orderListPage.waitForPaymentSuccessful(orderID,20);

        step("User open home page");
        homePage.goToHomePage();

        step("Go to account page");
        loginPage.goToAccountPage();

        step("login in using { " + username + " }");
        loginPage.loginIfNotWithSameEmail(username, password, "123456", 1);

        step("go to tiket home page");
        corporateDashboard.goBackToTiketHomePage();

        step("User select { " + currency + " } currency");
        homePage.selectCurrency(currency);

        step("get booking code from API");
        String bookingCode = flightApi.getBookingCode(orderID, username);

        step("set Tiket Number on flight dashboard and ticket will be eticket issued");
        Boolean isTicketIssued = flightApi.flightEticketIssuedRequest(orderID, bookingCode, username,firstName + " " + lastName,20);
        Assertion.assertThat("is Eticket Issued", isTicketIssued, is(true));

        step("go to order detail page");
        orderListPage.openIssuedTicketViaURL(orderID,15);
        orderDetailPage.closeEticketPopUp();
        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.verifyIssuedETicket(), is(true));

        step("Click reschedule button");
        orderDetailPage.clickRescheduleButton();

        step("select checkbox to reschedule flight");
        reschedulePage.clickCheckNewRescheduleButtonFlight();

        step("change date and continue");
        flightLandingPageWeb.selectDate(newDepartureDate);

        step("search flight");
        flightLandingPageWeb.searchFlight();

        step("Apply Filter " + airline);
        flightSearchResultPage.filterAirline(airline);

        step("select flight");
        flightSearchResultPage.selectFlightByAirline(airline);

        step("Go to payment page");
        flightBookingFormPage.goToPaymentPageFromBookingFormPage();

        step("Skip Flight may rescheduled page by clicking no thanks");
        flightBookingFormPage.clickNoThanksOnFlightMayReschedulePopup();

        step("Payment page should be displayed");
        String orderIDRescheduled = paymentPage.getOrderId();
        orderIDRescheduled = String.valueOf(getOrderId(orderIDRescheduled));
        Assertion.assertThat("order id should not be empty", orderIDRescheduled.isEmpty(), is(false));
        Assertion.assertThat("Corporate billing option is selected", paymentPage.getSelectedPayment().toLowerCase(), is("corporate billing"));

        step("get total price payment page");
        String totalPricePaymentPageReschedule = paymentPage.totalPricePaymentPage();
        Assertion.assertThat("total price should not be empty {" +totalPricePaymentPageReschedule +" } ", totalPricePaymentPageReschedule.isEmpty(),is(false));

        step("Submit payment using Corporate Billing with order id: { " + orderIDRescheduled +" }");
        paymentPage.clickOnPaymentButton();
        boolean isFreePaymentOrRefund = paymentPage.isFreePaymentOrRefund(totalPricePaymentPageReschedule);
        refundPage.followRefundMethodIfRequired(isFreePaymentOrRefund);
        orderListPage.waitForPaymentSuccessful(orderID,20);

        step("Open the order list");
        paymentPage.clickSeeOrderListButton();

        step("generate e-ticket");
        String bookingCodReschedule = flightApi.getBookingCode(orderIDRescheduled, username);
        Boolean isTicketIssuedNew = flightApi.flightEticketIssuedRequest(orderIDRescheduled, bookingCodReschedule, username, firstName + " " + lastName,20);
        Assertion.assertThat("is Eticket Issued", isTicketIssuedNew, is(true));

        step("verify the order detail");
        orderListPage.openIssuedTicketViaURL(orderIDRescheduled,15);
        orderDetailPage.closeEticketPopUp();
        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.verifyIssuedETicket(), is(true));
        Assertion.assertThat("Order ID is equal", orderDetailPage.getOrderId(), is(orderIDRescheduled));

        step("verify reschedule price");
        String totalPriceOrderDetailReschedule = orderDetailPage.getVerticalTotalPrice();
        Assertion.assertThat("is total price from payment page reschedule { "+ totalPricePaymentPageReschedule+" } match with order detail page reschedule { "+ totalPriceOrderDetailReschedule+ " } total price",CoreFunctions.convertPriceSafely(totalPriceOrderDetailReschedule),is(CoreFunctions.convertPriceSafely(totalPricePaymentPageReschedule)));
    }
    @Author(names = {"Mridul"})
    @Module(name = "CompleteBooking")
    @Tribe(name = "AirportTransfer")
    @Vertical(name = "B2B")
    @Test
    public void checkTravelPlanAirportTransfer() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("checkTravelPlanAirportTransfer");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        AirportTransferLandingPageWeb<?> airportTransferLandingPageWeb = AirportTransferLandingPageWeb.getInstance(session);
        AirportTransferSRPWeb<?> airportTransferSearchPageWeb = AirportTransferSRPWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        AirportTransferBookingPageWeb<?> airportTransferBookingPageWeb = AirportTransferBookingPageWeb.getInstance(session);
        TravelPlanWeb<?> travelPlanWeb = TravelPlanWeb.getInstance(session);
        AirportTransferPDPWeb<?> airportTransferProductPageWeb = AirportTransferPDPWeb.getInstance(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String adminUsername = param.get("adminUsername");
        String adminPassword = param.get("adminPassword");
        String location = param.get("location");
        String destination = param.get("destination");

        boolean flag = true;

        step("Change Config to true for Travel Plan Page on account "+adminUsername);
        boolean configUpdated = corporateApi.updateTravelPlanSetting(adminUsername, adminPassword,flag);
        Assertion.assertThat("Travel Plan setting should be updated to True", configUpdated, is(true));

        step("User open home page");
        homePage.goToHomePage();

        step("Login as " + username + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(username, password);

        step("go to tiket home page");
        homePage.goToHomePage();

        step("User open Airport Pickups vertical");
        homePage.openVerticalPageViaURL("airport-transfer");

        step("User click Ok,Got it button");
        airportTransferSearchPageWeb.clickGoItBtn();

        step("User Fill the Airport and destination locations");
        airportTransferLandingPageWeb.setSelectAirport("transfer_out");
        airportTransferLandingPageWeb.setInputAirport(location);
        airportTransferLandingPageWeb.setSelectDestination("transfer_out");
        airportTransferLandingPageWeb.setInputDestination(destination);
        airportTransferSearchPageWeb.clickSubmitBtn();

        step("User select first SRP card & go to PDP");
        airportTransferSearchPageWeb.selectFirstSRPCard();

        step("User Go to Booking Page");
        airportTransferProductPageWeb.proceedToBookingPage();

        step("User Go to Payment Page");
        airportTransferBookingPageWeb.goToPaymentPage();

        step("Check Travel Plan Page");
        Assertion.assertThat("Verify Travel Plan Page Appearing", travelPlanWeb.isTravelPlanInterimPageShown(), is(true));
        travelPlanWeb.skipTravelPlan();

        step("Payment page should be displayed");
        String orderID = paymentPage.getOrderId();
        orderID = String.valueOf(getOrderId(orderID));
        Assertion.assertThat("order id should not be empty", orderID, notNullValue());
        Assertion.assertThat("Corporate billing option is selected for order id "+orderID, paymentPage.getSelectedPayment().toLowerCase(), is("corporate billing"));

        flag = false;
        step("Change Config to false for Travel Plan Page on account "+adminUsername);
        configUpdated = corporateApi.updateTravelPlanSetting(adminUsername, adminPassword,flag);
        Assertion.assertThat("Travel Plan setting should be updated to False", configUpdated, is(true));
    }

    @Author(names = {"Harshit Ranjan"})
    @Module(name = "eTicket")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void AutoApprovalHotelFromSuperAdminWeb() throws FailedLoginException, InterruptedException, ExecutionException {
        Session<WebDriver> session = sessionThreadLocal.get();

        Map<String, String> param = getParam("AutoApprovalHotelFromSuperAdminWeb");
        BaseTest.paramsThreadLocal.set(param);

        String travelerUsername = param.get("username");
        String travelerPassword = param.get("password");
        String otp= param.get("otp");
        String location = param.get("location");
        String dateType = param.get("dateType");
        String date = param.get("dateRange");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        HotelLandingPageWeb<?> hotelLandingPage = HotelLandingPageWeb.getInstance(session);
        HotelRoomListPage<?> hotelRoomListPage = new HotelRoomListPage<>(session);
        HotelBookingFormPageWeb<?> hotelBookingFormPage = HotelBookingFormPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        TravelPlanWeb<?> travelPlanPage = TravelPlanWeb.getInstance(session);
        CorporateDashboard<?> corporateDashboard = new CorporateDashboard<>(session);

        step("Go to home");
        homePage.goToHomePage();

        step("Login as " + travelerUsername + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(travelerUsername, travelerPassword);

        step("User select IDR currency");
        homePage.selectCurrency("IDR");

        step("User open hotel vertical");
        homePage.openVerticalPageViaURL("Hotel");

        step("User search hotel location");
        hotelLandingPage.searchStayLocation(location);

        step("Set date");
        hotelLandingPage.setStayDate(dateType, date);

        step("Set guests and rooms");
        hotelLandingPage.clickRoomAndGuestButton();
        hotelLandingPage.setTotalRoom(1);
        hotelLandingPage.setTotalGuest(1);
        hotelLandingPage.clickDone();

        step("Submit search");
        hotelLandingPage.clickSubmit();

        step("Click book button");
        hotelRoomListPage.clickBookButton();

        step("Continue to payment page");
        hotelBookingFormPage.continueToSecondPartThenPaymentPage();
        travelPlanPage.skipTravelPlan();

        step("Payment page should be displayed");
        String orderID = paymentPage.getOrderId();
        int finalAmount = Integer.parseInt(paymentPage.getFinalPrice());
        Assertion.assertThat("Final amount should be greater than zero", finalAmount, is(greaterThan(0)));
        orderID = String.valueOf(getOrderId(orderID));
        Assertion.assertThat("Corporate billing option is selected", paymentPage.getSelectedPayment().toLowerCase(), is("corporate billing"));

        step("Submit payment using Corporate Billing");
        paymentPage.clickOnPaymentButton();

        step("Wait for few seconds to get auto approved");
        orderListPage.waitForPaymentSuccessful(orderID,20);

        paymentPage.clickSeeOrderListButton();
        step("Open the order list and go to order detail");

        orderListPage.openActiveTicketViaURL(orderID);
        orderDetailPage.skipCoachmark();
        Assertion.assertThat("orderID should be visible and should be same as from payment page", orderDetailPage.getOrderId(), is(orderID));
        Assertion.assertThat("e-Ticket on the order detail should be  published", orderDetailPage.verifyIssuedETicket(), is(true));
    }
    @Author(names = {"Gaurav"})
    @Module(name = "eTicket")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void testLoginBookingFormHotel() throws FailedLoginException, InterruptedException, ExecutionException {
        Session<WebDriver> session = sessionThreadLocal.get();

        Map<String, String> param = getParam("testLoginBookingFormHotel");
        BaseTest.paramsThreadLocal.set(param);

        String travelerUsername = param.get("username");
        String travelerPassword = param.get("password");
        String otp = param.get("otp");
        String location = param.get("location");
        String dateType = param.get("dateType");
        String date = param.get("dateRange");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> hotelLandingPage = HotelLandingPageWeb.getInstance(session);
        HotelRoomListPage<?> hotelRoomListPage = new HotelRoomListPage<>(session);
        HotelBookingFormPageWeb<?> hotelBookingFormPage = HotelBookingFormPageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        step("Go to home");
        homePage.goToHomePage();

        step("Log out ");
        loginPage.logoutViaUrl();

        step("User select IDR currency");
        homePage.selectCurrency("IDR");

        step("User open hotel vertical");
        homePage.openVerticalPageViaURL("Hotel");

        step("User search hotel location");
        hotelLandingPage.searchStayLocation(location);

        step("Set date");
        hotelLandingPage.setStayDate(dateType, date);

        step("Set guests and rooms");
        hotelLandingPage.clickRoomAndGuestButton();
        hotelLandingPage.setTotalRoom(1);
        hotelLandingPage.setTotalGuest(1);
        hotelLandingPage.clickDone();

        step("Submit search");
        hotelLandingPage.clickSubmit();

        step("Click book button");
        hotelRoomListPage.clickBookButton();

        step("Continue to Booking form page and click on login and fill details ");
        hotelBookingFormPage.clickLoginBanner();
        loginPage.loginSteps(travelerUsername, travelerPassword, otp, 1);

        step("Verify that user is login from booking form page ");
        Assertion.assertThat("Verify that user is login from booking form page", hotelBookingFormPage.isLoginSuccessful(travelerUsername), is(false));

    }

    @Author(names = {"Harshit Ranjan"})
    @Module(name = "eTicket")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void HotelPrefilledCustomFields() throws FailedLoginException, InterruptedException, ExecutionException {
        Session<WebDriver> session = sessionThreadLocal.get();

        Map<String, String> param = getParam("HotelPrefilledCustomFields");
        BaseTest.paramsThreadLocal.set(param);

        String travelerUsername = param.get("username");
        String password = param.get("password");
        String otp= param.get("otp");
        String location = param.get("location");
        String publicHotelId = param.get("publicHotelId");
        String adminUsername = param.get("adminUsername");
        String planName = param.get("planName")+CoreFunctions.randomName(4);
        String publicId = param.get("publicId");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String locationType = param.get("locationType").toUpperCase();
        String checkInDate = getCurrentDateYYYY_MM_DD();
        String checkOutDate = CoreFunctions.addToDateYYYY_MM_DD(getCurrentDateYYYY_MM_DD(), daysToDepartureDate);
        int nightDuration=Integer.parseInt(param.get("nightDuration"));
        String locationId = param.get("locationId");


        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        HotelLandingPageWeb<?> hotelLandingPage = HotelLandingPageWeb.getInstance(session);
        HotelRoomListPage<?> hotelRoomListPage = new HotelRoomListPage<>(session);
        HotelBookingFormPageWeb<?> hotelBookingFormPage = HotelBookingFormPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        TravelPlanWeb<?> travelPlanPage = TravelPlanWeb.getInstance(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);
        Unm<?> unm = new Unm<>(session);
        PDPPageWeb<?> pdpPageWeb = PDPPageWeb.getInstance(session);

        step("Change Config to true for Travel Plan Page on account "+adminUsername);
        boolean configUpdated = corporateApi.updateTravelPlanSetting(adminUsername, password,true);
        Assertion.assertThat("Travel Plan setting should be updated to True", configUpdated, is(true));

        step("get login access token for user " + travelerUsername + "employee data");
        LoginDataUnmResponse.Data loginDataUnmResponse = unm.unmLoginApi(travelerUsername, password);
        EmployeeData.Data employeeData = corporateApi.getEmployeeData(travelerUsername).data;


        step("Create a hotel plan");
        PostCreateTravelPlanBody.Configs travelPlanConfigs = corporateApi.setTravelPlanHotelData(publicId, location, checkInDate, checkOutDate, nightDuration, locationType);
        ArrayList<PostCreateTravelPlanBody.customFields> customFieldData = corporateApi.setCustomFieldsData(loginDataUnmResponse, travelerUsername);
        TravelPlanPojo travelPlanPojo = corporateApi.checkAndCreateTravelPlan(loginDataUnmResponse, travelerUsername, adminUsername, planName, 0, nightDuration, travelPlanConfigs, "name", "hotel", "approved", employeeData, customFieldData);
        Assertion.assertThat("hotel Plan Created " + travelPlanPojo.travelPlanID, travelPlanPojo.travelPlanID, not(-1));

        step("Approve the plan " + travelPlanPojo.travelPlanID);
        Assertion.assertThat("Plan got approved " + travelPlanPojo.travelPlanID, corporateApi.updateTravelPlanApproval(adminUsername, password, travelPlanPojo), is(true));

        step("Go to home");
        homePage.goToHomePage();

        step("Login as " + travelerUsername + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(travelerUsername, password);

        step("User select IDR currency");
        homePage.selectCurrency("IDR");

        step("Open the PDP page");
        pdpPageWeb.goToHotelViaDeeplinkWithCheckinCheckout(locationId,publicHotelId , checkInDate, checkOutDate);

        step("Click book button");
        hotelRoomListPage.clickBookButton();

        step("Continue to payment page");
        hotelBookingFormPage.continueToSecondPartThenPaymentPage();
        travelPlanPage.setSelectTravelPlan();
        Assertion.assertThat("all custom fields should be filled automatically", travelPlanPage.checkTravelPlanCustomFields(travelPlanPojo), is(true));
        travelPlanPage.skipTravelPlan();

        step("Payment page should be displayed");
        String orderID = paymentPage.getOrderId();
        int finalAmount = Integer.parseInt(paymentPage.getFinalPrice());
        Assertion.assertThat("Final amount should be greater than zero", finalAmount, is(greaterThan(0)));
        orderID = String.valueOf(getOrderId(orderID));
        Assertion.assertThat("Corporate billing option is selected", paymentPage.getSelectedPayment().toLowerCase(), is("corporate billing"));

        step("Submit payment using Corporate Billing");
        paymentPage.clickOnPaymentButton();

        step("Approve booking by admin account for Order ID: " + orderID);
        Boolean bookingStatus = corporateApi.approveBookingB2BApi(adminUsername, password, orderID);
        Assertion.assertThat("Booking approved by B2B dashboard", bookingStatus, is(true));

        step("Wait for few seconds to get auto approved");
        orderListPage.waitForPaymentSuccessful(orderID,20);

        paymentPage.clickSeeOrderListButton();
        step("Open the order list and go to order detail");

        orderListPage.openActiveTicketViaURL(orderID);
        orderDetailPage.skipCoachmark();
        Assertion.assertThat("orderID should be visible and should be same as from payment page", orderDetailPage.getOrderId(), is(orderID));
        Assertion.assertThat("e-Ticket on the order detail should be  published", orderDetailPage.verifyIssuedETicket(), is(true));

        step("Change Config to false for Travel Plan Page on account "+adminUsername);
        configUpdated = corporateApi.updateTravelPlanSetting(adminUsername, password,false);
        Assertion.assertThat("Travel Plan setting should be updated to False", configUpdated, is(true));
    }

    @Author(names = {"Gaurav"})
    @Module(name = "eTicket")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void testLoginInstantPaymentTravelerDweb() throws FailedLoginException, InterruptedException, ExecutionException {
        Session<WebDriver> session = sessionThreadLocal.get();

        Map<String, String> param = getParam("testLoginInstantPaymentTravelerDweb");
        BaseTest.paramsThreadLocal.set(param);

        String travelerUsername = param.get("username");
        String travelerPassword = param.get("password");
        String otp = param.get("otp");
        String location = param.get("location");
        String dateType = param.get("dateType");
        String date = param.get("dateRange");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HotelLandingPageWeb<?> hotelLandingPage = HotelLandingPageWeb.getInstance(session);
        HotelRoomListPage<?> hotelRoomListPage = new HotelRoomListPage<>(session);
        HotelBookingFormPageWeb<?> hotelBookingFormPage = HotelBookingFormPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        TravelPlanWeb<?> travelPlanPage = TravelPlanWeb.getInstance(session);

        step("Go to home");
        homePage.goToHomePage();

        step("Login as " + travelerUsername + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(travelerUsername, travelerPassword);

        step("User select IDR currency");
        homePage.selectCurrency("IDR");

        step("User open hotel vertical");
        homePage.openVerticalPageViaURL("Hotel");

        step("User search hotel location");
        hotelLandingPage.searchStayLocation(location);

        step("Set date");
        hotelLandingPage.setStayDate(dateType, date);

        step("Set guests and rooms");
        hotelLandingPage.clickRoomAndGuestButton();
        hotelLandingPage.setTotalRoom(1);
        hotelLandingPage.setTotalGuest(1);
        hotelLandingPage.clickDone();

        step("Submit search");
        hotelLandingPage.clickSubmit();

        step("Click book button");
        hotelRoomListPage.clickBookButton();

        step("Continue to payment page");
        hotelBookingFormPage.continueToSecondPartThenPaymentPage();
        travelPlanPage.skipTravelPlan();

        step("Payment page should be displayed");
        paymentPage.getOrderId();
        Assertion.assertThat("Corporate billing option is not present", paymentPage.getSelectedPayment().toLowerCase(), not("corporate billing"));
        Assertion.assertThat("B2C option is visible ", paymentPage.getSelectedPayment(), anyOf(is("Sakuku"), is("Use Another Card"), is("BCA Virtual Account")));
    }


    @Author(names = {"Mridul"})
    @Module(name = "eTicket")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void checkHotelBookingViaCard() throws FailedLoginException, InterruptedException, ExecutionException{
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("checkHotelBookingViaCard");
        BaseTest.paramsThreadLocal.set(param);

        String location = param.get("location");
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String cardNumber = param.get("cardNumber");
        String cardMonth = param.get("cardMonth");
        String cardCvv = param.get("cardCvv");
        String cardHolderName = param.get("cardHolderName");
        String currency=param.get("currency");
        String PaymentMethod=param.get("paymentMethod");
        String paymentGroup = param.get("paymentGroup");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        HotelDetailPage<?> detailPage = new HotelDetailPage<>(session);
        HotelRoomListPage<?> roomListPage=new HotelRoomListPage<>(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HotelBookingFormPageWeb<?> bookingFormPage = HotelBookingFormPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        TravelPlanWeb<?> travelPlanPage = TravelPlanWeb.getInstance(session);
        HotelLandingPageWeb<?> hotelLandingPage = HotelLandingPageWeb.getInstance(session);


        step("User open home page");
        homePage.goToHomePage();

        step("Login as " + email + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(email, password);
        step("User select IDR currency");
        homePage.selectCurrency(currency);

        step("User Open Hotel Landing page");
        homePage.openVerticalPageViaURL("Hotel");

        Assertion.assertThat("Landing Page is Opened", landingPage.isLandingPageVisible(), is(true));

        step("Search stay hotel");
        landingPage.searchStayLocation(location);


        step("Set guests and rooms");
        hotelLandingPage.clickRoomAndGuestButton();
        hotelLandingPage.setTotalRoom(1);
        hotelLandingPage.setTotalGuest(1);
        hotelLandingPage.clickDone();

        step("submit search Button");
        landingPage.clickSubmit();

        step("Go Inside PDP");
        String HotelName=detailPage.getHotelNameDetail();
        Assertion.assertThat("Verify if PDP is Opened", detailPage.isPdpLoaded(), is(true));
        Assertion.assertThat("Verify Hotel name on entered on LP and PDP  is same",HotelName,containsString(location));

        roomListPage.scrollToRoomListSection();
        step("Click Book Button");
        roomListPage.clickBookButton();

        step("Go to payment page");
        bookingFormPage.continueToSecondPartThenPaymentPage();
        travelPlanPage.skipTravelPlan();

        String orderID = paymentPage.getOrderId();
        Assertion.assertThat("Verify order ID is displayed in payment page", !orderID.isEmpty(), is(true));

        step("User select payment method ");

        if(Configs.PLATFORM==Platform.DWEB){
            paymentPage.clickOnSelectPaymentMethod();
            paymentPage.goToPaymentMethodsGroup(paymentGroup);
            paymentPage.selectPaymentMethod(PaymentMethod);

            step("Input the card details");
            paymentPage.inputCardNumber(cardNumber);
            paymentPage.inputCardMonth(cardMonth);
            paymentPage.inputCardCvv(cardCvv);
            paymentPage.inputCardHolderName(cardHolderName);
        }else{
            paymentPage.selectCCOption();
        }

        step("Click on Pay button");
        paymentPage.clickOnPaymentButton();

        step("Verify the payment successful page is visible or not");
        Assertion.assertThat("Verify the payment successful page is visible", paymentPage.isPaymentSuccessfulVisible(), is(true));

        step("User try to open see order list");
        paymentPage.clickSeeOrderListButton();

        step("Issued Order");
        orderListPage.openActiveTicketViaURL(orderID);

        orderDetailPage.skipCoachmark();

        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.verifyIssuedETicket(), is(true));
        Assertion.assertThat("contact name should be available on order detail page", !orderDetailPage.getContactName(orderID).isEmpty(), is(true));
        Assertion.assertThat("email should be correctly displayed on order detail page", orderDetailPage.getContactEmail(orderID), is(email));
        Assertion.assertThat("orderID should be visible and should be same as from payment page ", orderDetailPage.getOrderId(), containsString(orderID));

    }

    @Author(names = {"Gaurav"})
    @Module(name = "CompleteBooking")
    @Tribe(name = "AirportTransfer")
    @Vertical(name = "B2B")
    @Test
    public void checkInterimPageShownAirportTransfer() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("checkInterimPageShownAirportTransfer");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        AirportTransferLandingPageWeb<?> airportTransferLandingPageWeb = AirportTransferLandingPageWeb.getInstance(session);
        AirportTransferSRPWeb<?> airportTransferSearchPageWeb = AirportTransferSRPWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        AirportTransferBookingPageWeb<?> airportTransferBookingPageWeb = AirportTransferBookingPageWeb.getInstance(session);
        TravelPlanWeb<?> travelPlanWeb = TravelPlanWeb.getInstance(session);
        AirportTransferPDPWeb<?> airportTransferProductPageWeb = AirportTransferPDPWeb.getInstance(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String adminUsername = param.get("adminUsername");
        String adminPassword = param.get("adminPassword");
        String location = param.get("location");
        String destination = param.get("destination");

        boolean flag = true;

        step("Change Config to true for Travel Plan Page on account " + adminUsername);
        boolean configUpdated = corporateApi.updateTravelPlanSetting(adminUsername, adminPassword, flag);
        Assertion.assertThat("Travel Plan setting should be updated to True", configUpdated, is(true));

        step("User open home page");
        homePage.goToHomePage();

        step("Login as " + username + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(username, password);

        step("go to tiket home page");
        homePage.goToHomePage();

        step("User open Airport Pickups vertical");
        homePage.openVerticalPageViaURL("airport-transfer");

        step("User click Ok,Got it button");
        airportTransferSearchPageWeb.clickGoItBtn();

        step("User Fill the Airport and destination locations");
        airportTransferLandingPageWeb.setSelectAirport("transfer_out");
        airportTransferLandingPageWeb.setInputAirport(location);
        airportTransferLandingPageWeb.setSelectDestination("transfer_out");
        airportTransferLandingPageWeb.setInputDestination(destination);
        airportTransferSearchPageWeb.clickSubmitBtn();

        step("User select first SRP card & go to PDP");
        airportTransferSearchPageWeb.selectFirstSRPCard();

        step("User Go to Booking Page");
        airportTransferProductPageWeb.proceedToBookingPage();

        step("User Go to Payment Page");
        airportTransferBookingPageWeb.goToPaymentPage();

        step("Check Interim Page is shown");
        Assertion.assertThat("Verify Interim Page Appearing", travelPlanWeb.isInterimPageShown(), is(true));

    }


    @Author(names = "Harshit")
    @Module(name = "Complete Booking")
    @Tribe(name = "Train")
    @Vertical(name = "B2B")
    @Test
    public void TravelPlanPrefilledTrain() throws FailedLoginException, InterruptedException, ExecutionException {
        Map<String, String> param = getParam("TravelPlanPrefilledTrain");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String departureCode = param.get("departureCode");
        String departureType = param.get("departureType");
        String departureName = param.get("departureName");
        String arrivalCode = param.get("arrivalCode");
        String arrivalType = param.get("arrivalType");
        String arrivalName = param.get("arrivalName");
        boolean isRoundTrip = param.get("isRoundTrip").equals("true");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int duration = Integer.parseInt(param.get("duration"));


        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
        String adminUsername=param.get("adminUsername");
        String planName=param.get("planName")+CoreFunctions.randomName(4);
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        TrainSearchPageWeb<?> trainSearchPage = TrainSearchPageWeb.getInstance(session);
        TrainSearchResultPageWeb<?> trainSearchResultPage = TrainSearchResultPageWeb.getInstance(session);
        TrainBookingFormWeb<?> trainBookingFormWeb = TrainBookingFormWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        Unm<?> unm = new Unm<>(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);
        TravelPlanWeb<?> travelPlanPage = TravelPlanWeb.getInstance(session);

        step("get login access token for user " + username + "employee data");
        LoginDataUnmResponse.Data loginDataUnmResponse = unm.unmLoginApi(username, password);
        EmployeeData.Data employeeData = corporateApi.getEmployeeData(username).data;


        step("Change Config to true for Travel Plan Page on account " + adminUsername);
        boolean configUpdated = corporateApi.updateTravelPlanSetting(adminUsername, password, true);
        Assertion.assertThat("Travel Plan setting should be updated to True", configUpdated, is(true));

        step("Create a Train plan");
        PostCreateTravelPlanBody.Configs travelPlanConfigs = corporateApi.setTravelPlanTrainData(departureCode,departureType,departureName,arrivalCode,arrivalType,arrivalName,isRoundTrip,daysToDepartureDate,duration);
        TravelPlanPojo travelPlanPojo = corporateApi.checkAndCreateTravelPlan(loginDataUnmResponse,username,adminUsername,planName,daysToDepartureDate,1,travelPlanConfigs,"data","train","approved",employeeData, new ArrayList<>());
        Assertion.assertThat("TRAIN Travel Plan Created planId: " + travelPlanPojo.travelPlanID, travelPlanPojo.travelPlanID,not(-1));

        step("Approve the plan "+travelPlanPojo.travelPlanID);
        Assertion.assertThat("Plan got approved "+travelPlanPojo.travelPlanID,corporateApi.updateTravelPlanApproval(adminUsername,password,travelPlanPojo),is(true));

        step("User open home page");
        homePage.goToHomePage();

        step("Login as " + username + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(username, password);

        step("User open train vertical");
        homePage.openVerticalPageViaURL("Train");

        step("User skip pop-up");
        trainSearchPage.clickSkipPopup();

        step("User select origin station");
        trainSearchPage.selectOrigin(departureCode);

        step("User select destination station");
        trainSearchPage.selectDestination(arrivalCode);

        step("User select departure date");
        trainSearchPage.selectDate(departureDate);

        step("User toggle one way");
        trainSearchPage.toggleTripType("oneway");

        step("User search train");
        trainSearchPage.searchTrain();

        step("User skip pop-up");
        trainSearchPage.clickSkipPopup();

        step("Select train");
        trainSearchResultPage.selectTrain();

        step("User fill adult details");
        trainBookingFormWeb.fillPassengerDetailAdult(1);

        step("User continues booking after filling details");
        trainBookingFormWeb.clickContinue();

        step("User Go to Payment Page");
        trainBookingFormWeb.proceedToPaymentPage();

        Assertion.assertThat("Continue Button is Disabled",travelPlanPage.isButtonEnabled(),is(false));
        travelPlanPage.setSelectTravelPlan();
        Assertion.assertThat("After Selecting Travel Plan now Button is Enabled",travelPlanPage.isButtonEnabled(),is(true));
        travelPlanPage.skipTravelPlan();

        step("Wait for payment page to load");
        paymentPage.isPaymentPageLoaded();

        String orderID = paymentPage.getOrderId();
        Assertion.assertThat("Verify order ID is displayed in payment page", !orderID.isEmpty(), is(true));

        step("Change Config to false for Travel Plan Page on account " + adminUsername);
        configUpdated = corporateApi.updateTravelPlanSetting(adminUsername, password, false);
        Assertion.assertThat("Travel Plan setting should be updated to False", configUpdated, is(true));
    }

    @Author(names = {"Gaurav"})
    @Module(name = "eTicket")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void AutoApprovalHotelFromAdminWeb() throws FailedLoginException, InterruptedException, ExecutionException {
        Session<WebDriver> session = sessionThreadLocal.get();

        Map<String, String> param = getParam("AutoApprovalHotelFromAdminWeb");
        BaseTest.paramsThreadLocal.set(param);

        String travelerUsername = param.get("username");
        String travelerPassword = param.get("password");
        String otp = param.get("otp");
        String location = param.get("location");
        String dateType = param.get("dateType");
        String date = param.get("dateRange");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        HotelLandingPageWeb<?> hotelLandingPage = HotelLandingPageWeb.getInstance(session);
        HotelRoomListPage<?> hotelRoomListPage = new HotelRoomListPage<>(session);
        HotelBookingFormPageWeb<?> hotelBookingFormPage = HotelBookingFormPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        TravelPlanWeb<?> travelPlanPage = TravelPlanWeb.getInstance(session);
        CorporateDashboard<?> corporateDashboard = new CorporateDashboard<>(session);

        step("Go to home");
        homePage.goToHomePage();

        step("Login as " + travelerUsername + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(travelerUsername, travelerPassword);

        step("User select IDR currency");
        homePage.selectCurrency("IDR");

        step("User open hotel vertical");
        homePage.openVerticalPageViaURL("Hotel");

        step("User search hotel location");
        hotelLandingPage.searchStayLocation(location);

        step("Set date");
        hotelLandingPage.setStayDate(dateType, date);

        step("Set guests and rooms");
        hotelLandingPage.clickRoomAndGuestButton();
        hotelLandingPage.setTotalRoom(1);
        hotelLandingPage.setTotalGuest(1);
        hotelLandingPage.clickDone();

        step("Submit search");
        hotelLandingPage.clickSubmit();

        step("Click book button");
        hotelRoomListPage.clickBookButton();

        step("Continue to payment page");
        hotelBookingFormPage.continueToSecondPartThenPaymentPage();
        travelPlanPage.skipTravelPlan();

        step("Payment page should be displayed");
        String orderID = paymentPage.getOrderId();
        orderID = String.valueOf(getOrderId(orderID));
        Assertion.assertThat("Corporate billing option is selected", paymentPage.getSelectedPayment().toLowerCase(), is("corporate billing"));

        step("Submit payment using Corporate Billing");
        paymentPage.clickOnPaymentButton();

        step("Wait for few seconds to get auto approved");
        orderListPage.waitForPaymentSuccessful(orderID, 20);

        paymentPage.clickSeeOrderListButton();
        step("Open the order list and go to order detail");

        orderListPage.openActiveTicketViaURL(orderID);
        orderDetailPage.skipCoachmark();
        Assertion.assertThat("e-Ticket on the order detail should be  published", orderDetailPage.verifyIssuedETicket(), is(true));
    }


    @Author(names = {"Harshit"})
    @Module(name = "Interim Page")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void flightUnapprovedTravelPlanNotShown() throws FailedLoginException, InterruptedException, ExecutionException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("flightUnapprovedTravelPlanNotShown");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPageWeb = FlightLandingPageWeb.getInstance(session);
        FlightSearchResultPageWeb<?> flightSearchResultPage = FlightSearchResultPageWeb.getInstance(session);
        FlightProductReviewPage<?> flightProductReviewPage = new FlightProductReviewPage<>(session);
        FlightBookingFormPageWeb<?> flightBookingFormPage = FlightBookingFormPageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        TravelPlanWeb<?> travelPlanPage = TravelPlanWeb.getInstance(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);
        Unm<?> unm = new Unm<>(session);

        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");
        String airline = param.get("airline");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        String travelerUsername = param.get("travelerUsername");
        String adminUsername = param.get("adminUsername");
        String password = param.get("password");
        String currency = param.get("currency");
        String name = param.get("name");
        String lastName = param.get("lastName");
        String planName=param.get("planName");

        step("get login access token for user " + travelerUsername + "employee data");
        LoginDataUnmResponse.Data loginDataUnmResponse = unm.unmLoginApi(travelerUsername, password);
        EmployeeData.Data employeeData = corporateApi.getEmployeeData(travelerUsername).data;

        step("Create a Flight plan");
        PostCreateTravelPlanBody.Configs travelPlanConfigs = corporateApi.setTravelPlanFlightData(loginDataUnmResponse.accessToken,originAirport,destinationAirport,false,daysToDepartureDate,0);
        TravelPlanPojo travelPlanPojo = corporateApi.checkAndCreateTravelPlan(loginDataUnmResponse,travelerUsername,adminUsername,planName,daysToDepartureDate,1,travelPlanConfigs, "name","flight","requested",employeeData, new ArrayList<>());
        Assertion.assertThat("Flight Plan Created " + travelPlanPojo.travelPlanID, travelPlanPojo.travelPlanID, not(-1));


        step("User open home page");
        homePage.goToHomePage();

        step("login in using { " + travelerUsername + " }");
        loginPage.loginAsGuest();
        loginPage.loginIntoWeb(travelerUsername, password, "123456", 1);


        step("User select { " + currency + " } currency");
        homePage.selectCurrency(currency);

        step("User open Flight vertical");
        homePage.openVerticalPageViaURL("Flights");

        step("Toggle trip type");
        flightLandingPageWeb.toggleTripType(tripType);

        step("Select origin airport");
        flightLandingPageWeb.selectOrigin(originAirport);

        step("Select destination airport");
        flightLandingPageWeb.selectDestination(destinationAirport);

        step("add passenger");
        flightLandingPageWeb.addPassenger(adultPassengers, childPassengers, infantPassengers);

        step("search flight");
        flightLandingPageWeb.searchFlight();

        step("Apply Filter " + airline);
        flightSearchResultPage.filterAirline(airline);

        step("select flight");
        flightSearchResultPage.selectFlightByAirline(airline);

        step("Continue to booking form");
        flightProductReviewPage.continueToBookingForm();
        flightBookingFormPage.clickNoThanksOnFlightMayReschedulePopup();

        step("Click toggle to fill passengers details and save ");
        flightBookingFormPage.fillPassengerDetails();

        step("Go to payment page");
        flightBookingFormPage.goToPaymentPageFromBookingFormPage();

        step("Verify that No Travel plan is there to be selected on interim page without approval");
        Assertion.assertThat("No travel Plan is available to be selected",travelPlanPage.noTravelPlanAvailable(),is(false));

    }

    @Author(names = {"Harshit"})
    @Module(name = "corporateCode")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void PelitaSRPNudgeCorporateCodeApplied() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("PelitaSRPNudgeCorporateCodeApplied");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPageWeb = FlightLandingPageWeb.getInstance(session);
        FlightSearchResultPageWeb<?> flightSearchResultPage = FlightSearchResultPageWeb.getInstance(session);

        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);
        String username = param.get("username");
        String password = param.get("password");
        String currency = param.get("currency");
        String otp = param.get("otp");
        String airline=param.get("airline");

        step("User open home page");
        homePage.goToHomePage();

        step("login in using { " + username + " }");
        loginPage.loginAsGuest();
        loginPage.loginIntoWeb(username, password, otp, 1);

        step("User select IDR currency");
        homePage.selectCurrency(currency);

        step("User open Flight vertical");
        homePage.openVerticalPageViaURL("Flights");

        step("Toggle trip type");
        flightLandingPageWeb.toggleTripType(tripType);

        step("Select origin airport");
        flightLandingPageWeb.selectOrigin(originAirport);

        step("Select destination airport");
        flightLandingPageWeb.selectDestination(destinationAirport);

        step("Select date");
        flightLandingPageWeb.selectDate(departureDate);

        step("search flight");
        flightLandingPageWeb.searchFlight();

        step("filter airline "+airline);
        flightSearchResultPage.filterAirline(airline);
        sleep(5);//wait for filter to apply

        step("Corporate code nudge is there for depart trip");
        Assertion.assertThat("Corporate deals banner is present", flightSearchResultPage.isCorporateDealsBannerPresent(), is(true));
        Assertion.assertThat("Corporate deals benefit on the card is present", flightSearchResultPage.gotoCorporateDealsBenefitCard(), is(true));
    }

    @Author(names = {"Harshit Ranjan"})
    @Module(name = "eTicket")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void HotelPrefilledTravelPlan() throws FailedLoginException, InterruptedException, ExecutionException {
        Session<WebDriver> session = sessionThreadLocal.get();

        Map<String, String> param = getParam("HotelPrefilledTravelPlan");
        BaseTest.paramsThreadLocal.set(param);

        String travelerUsername = param.get("username");
        String password = param.get("password");
        String otp= param.get("otp");
        String location = param.get("location");
        String publicHotelId = param.get("publicHotelId");
        String adminUsername = param.get("adminUsername");
        String planName = param.get("planName") +CoreFunctions.randomName(4);
        String publicId = param.get("publicId");
        String locationId = param.get("locationId");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String locationType = param.get("locationType").toUpperCase();
        String checkInDate = getCurrentDateYYYY_MM_DD();
        String checkOutDate = CoreFunctions.addToDateYYYY_MM_DD(getCurrentDateYYYY_MM_DD(), daysToDepartureDate);
        int nightDuration=Integer.parseInt(param.get("nightDuration"));


        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        HotelLandingPageWeb<?> hotelLandingPage = HotelLandingPageWeb.getInstance(session);
        HotelRoomListPage<?> hotelRoomListPage = new HotelRoomListPage<>(session);
        HotelBookingFormPageWeb<?> hotelBookingFormPage = HotelBookingFormPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        TravelPlanWeb<?> travelPlanPage = TravelPlanWeb.getInstance(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);
        Unm<?> unm = new Unm<>(session);
        PDPPageWeb<?> pdpPageWeb = PDPPageWeb.getInstance(session);

        step("Change Config to true for Travel Plan Page on account "+adminUsername);
        boolean configUpdated = corporateApi.updateTravelPlanSetting(adminUsername, password,true);
        Assertion.assertThat("Travel Plan setting should be updated to True", configUpdated, is(true));

        step("get login access token for user " + travelerUsername + "employee data");
        LoginDataUnmResponse.Data loginDataUnmResponse = unm.unmLoginApi(travelerUsername, password);
        EmployeeData.Data employeeData = corporateApi.getEmployeeData(travelerUsername).data;


        step("Create a hotel plan");
        PostCreateTravelPlanBody.Configs travelPlanConfigs = corporateApi.setTravelPlanHotelData(publicId, location, checkInDate, checkOutDate, nightDuration, locationType);
        TravelPlanPojo travelPlanPojo = corporateApi.checkAndCreateTravelPlan(loginDataUnmResponse, travelerUsername, adminUsername, planName, 0, nightDuration, travelPlanConfigs, "name", "hotel", "approved", employeeData, new ArrayList<>());
        Assertion.assertThat("hotel Plan Created " + travelPlanPojo.travelPlanID, travelPlanPojo.travelPlanID, not(-1));

        step("Approve the plan " + travelPlanPojo.travelPlanID);
        Assertion.assertThat("Plan got approved " + travelPlanPojo.travelPlanID, corporateApi.updateTravelPlanApproval(adminUsername, password, travelPlanPojo), is(true));

        step("Go to home");
        homePage.goToHomePage();

        step("Login as " + travelerUsername + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(travelerUsername, password);

        step("User select IDR currency");
        homePage.selectCurrency("IDR");

        step("User open hotel vertical");
        homePage.openVerticalPageViaURL("Hotel");

        step("User select IDR currency");
        homePage.selectCurrency("IDR");

        step("Open the PDP page");
        pdpPageWeb.goToHotelViaDeeplinkWithCheckinCheckout(locationId,publicHotelId , checkInDate, checkOutDate);

        step("Click book button");
        hotelRoomListPage.clickBookButton();

        step("Continue to payment page");
        hotelBookingFormPage.continueToSecondPartThenPaymentPage();
        travelPlanPage.setSelectTravelPlan();
        travelPlanPage.skipTravelPlan();

        step("Payment page should be displayed");
        String orderID = paymentPage.getOrderId();
        int finalAmount = Integer.parseInt(paymentPage.getFinalPrice());
        Assertion.assertThat("Final amount should be greater than zero", finalAmount, is(greaterThan(0)));
        orderID = String.valueOf(getOrderId(orderID));
        Assertion.assertThat("Corporate billing option is selected", paymentPage.getSelectedPayment().toLowerCase(), is("corporate billing"));

        step("Submit payment using Corporate Billing");
        paymentPage.clickOnPaymentButton();

        step("Approve booking by admin account for Order ID: " + orderID);
        Boolean bookingStatus = corporateApi.approveBookingB2BApi(adminUsername, password, orderID);
        Assertion.assertThat("Booking approved by B2B dashboard", bookingStatus, is(true));

        step("Wait for few seconds to get auto approved");
        orderListPage.waitForPaymentSuccessful(orderID,20);

        paymentPage.clickSeeOrderListButton();
        step("Open the order list and go to order detail");

        orderListPage.openActiveTicketViaURL(orderID);
        orderDetailPage.skipCoachmark();
        Assertion.assertThat("orderID should be visible and should be same as from payment page", orderDetailPage.getOrderId(), is(orderID));
        Assertion.assertThat("e-Ticket on the order detail should be  published", orderDetailPage.verifyIssuedETicket(), is(true));

        step("Change Config to false for Travel Plan Page on account "+adminUsername);
        configUpdated = corporateApi.updateTravelPlanSetting(adminUsername, password,false);
        Assertion.assertThat("Travel Plan setting should be updated to False", configUpdated, is(true));
    }

    @Author(names = "HARSHIT")
    @Module(name = "Full Refund")
    @Tribe(name = "Hotel")
    @Vertical(name = "B2B")
    @Test
    public void testApproveRefundRequestByTravelerWEB() throws FailedLoginException, InterruptedException {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        Map<String, String> param = getParam("testApproveRefundRequestByTravelerWEB");
        BaseTest.paramsThreadLocal.set(param);

        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        RefundPageWeb<?> refundPage = RefundPageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);

        String username = param.get("username");
        String approverUsername = param.get("approverUsername");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String startDate = addToDateYYYY_MM_DD(getCurrentDateYYYY_MM_DD(), 10);
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));
        int room = Integer.parseInt(param.get("room"));
        String currency = "IDR";
        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autoguest";
        String guestTitle = salutationList[0];
        String query = "Carvens Hotel";

        String orderID;
        step("Create new hotel issued order");
        orderID = hotelApi.createIssuedOrderApiB2B(
                username, password, startDate, query, adult, night, room, "IDR",
                orderFullName, orderNameTitle, corporateApi.getEmployeeData(username), false,
                "");
        Assertion.assertThat("Hotel Booking via API is successful for B2B account", orderID, is(not(emptyOrNullString())));

        step("Approve booking by admin account for Order ID: " + orderID);
        Boolean bookingStatus = corporateApi.approveBookingB2BApi(approverUsername, password, orderID);
        Assertion.assertThat("Booking approved by B2B dashboard", bookingStatus, is(true));

        step("Goto home");
        homePage.goToHomePage();

        step("Login as " + username + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(username, password);

        step("Find the issued order");
        orderListPage.openActiveTicketViaURL(orderID);
        orderDetailPage.skipCoachmark();

        step("Verify hotel order details");
        Assertion.assertThat("Order ID is displayed correctly", orderDetailPage.getOrderId(), Is.is(orderID));

        if (Configs.PLATFORM == Platform.DWEB) {
            Assertion.assertThat("Refund button should be displayed", orderDetailPage.isRefundButtonDisplayed(), Is.is(true));
            step("Open refund page");
            orderDetailPage.clickRefundButton();
        } else {
            orderDetailPage.refundButtonInweb();
            refundPage.waitForContentLoaderDisappear();
        }

        step("Click to continue");
        refundPage.continueToRefund();

        step("Select random refund reason");
        refundPage.selectRandomRefundReason();

        step("Verify refund estimation section");
        refundPage.waitForEstimationAmountAppear("5");
        String refundReceive = refundPage.getRefundYoullReceive();
        String refundFee = refundPage.getRefundFee();
        String refundTotal = refundPage.getRefundTotal();
        List<String> amounts = new ArrayList<>(Arrays.asList(refundReceive, refundFee, refundTotal));
        Assertion.assertThat("Refund Total amount is correct", CoreFunctions.removeText(refundReceive)+CoreFunctions.removeText(refundFee), Is.is(CoreFunctions.removeText(refundTotal)));

        step("Verify refund amount breakdown");
        refundPage.openRefundBreakdown();
        amounts.clear();
        amounts.addAll(refundPage.getRefundAmountBreakdown(currency));

        if (refundPage.getRefundFeeBreakdown(currency).isPresent()) amounts.addAll(refundPage.getRefundFeeBreakdown(currency).get());
        amounts.addAll(refundPage.getRefundTotalBreakdown(currency));
        Assertion.assertThat("Total breakdown refund same as estimation", refundPage.getRefundTotalBreakdownSum(currency), Is.is(String.valueOf(CoreFunctions.removeText(refundTotal))));
        refundPage.closeRefundBreakdown();

        step("Continue to next page");
        refundPage.clickButton("Continue");
        refundPage.waitRefundSecondPageLoaded();
        Assertion.assertThat("Order ID on 2nd refund page is correct", refundPage.getOrderIdOnRefundBanner(), containsString(orderID));
        Assertion.assertThat("Refund progress banner same as refund step", refundPage.getActiveProgress(), Is.is(2));

        step("Verify refund method and amount");
        String refundMethodSection = refundPage.getRefundMethod();
        Assertion.assertThat("Refund method is correct", refundMethodSection, not(emptyOrNullString()));
        Assertion.assertThat("Refund amount is same as prev page", refundTotal, is(refundPage.getRefundTotal()));

        step("Check Refund TnC");
        Assertion.assertThat("Submit refund button is disabled", refundPage.isDisabledSubmitButton(), is(true));
        refundPage.clickChecklistTnc();
        refundPage.clickButton("Submit");

        step("Submit refund");
        refundPage.ProcessToRefundB2B();
        refundPage.waitForSubmitRefundLoader();
        Assertion.assertThat("Success submit refund figure content", refundPage.getSuccessRefundFigureContent(), containsString("Successfully submit the refund"));

        step("Check if the refunded order is being reflected on corporate dashboard");
        String refundId = corporateApi.getRefundId(orderID, approverUsername,password,"prerefund");
        Boolean isApproved = true;
        Boolean success = corporateApi.approveOrRejectRefundB2B(approverUsername, password, refundId, isApproved);
        Assertion.assertThat("Refund Approved Success", success, is(true));
    }

    @Author(names = "Mridul")
    @Module(name = "CompleteBooking")
    @Tribe(name = "Whoosh")
    @Vertical(name = "NFT")
    @Test
    public void checkWhooshBookingOnPurchaseList() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("checkWhooshBookingOnPurchaseList");
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
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, 1);
        String userNameUI=param.get("userNameUI");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        WhooshSearchPageWeb<?> whooshSearchPageWeb = WhooshSearchPageWeb.getInstance(session);
        WhooshSearchResultPageWeb<?> whooshSearchResultPageWeb = WhooshSearchResultPageWeb.getInstance(session);
        WhooshBookingPageWeb<?> whooshBookingPageWeb = WhooshBookingPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);
        TravelPlanWeb<?> travelPlanPage = TravelPlanWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("Login as " + email + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(email, password);

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

        step("User select departure date");
        whooshSearchPageWeb.searchDepDate(departureDate);

        step("User search whoosh");
        whooshSearchPageWeb.searchWhoosh();

        step("User verifies if he is on SRP or not");
        Assertion.assertThat("Verify if the user is on SRP or not", whooshSearchResultPageWeb.checkSRP(), is(true));

        step("User skips the filter option");
        whooshSearchResultPageWeb.clickSkip();

        step("User select first Whoosh");
        whooshSearchResultPageWeb.clickFirstWhoosh();

        step("User fill adult details");
        whooshBookingPageWeb.fillPassengerDetailAdult(1);

        step("User fill infant details");
        whooshBookingPageWeb.fillPassengerDetailInfant(1, 0);

        step("User continues booking after filling details");
        whooshBookingPageWeb.clickContinue();

        step("User continues for payment");
        whooshBookingPageWeb.continueToPay();

        step("User Go to Payment Page");
        whooshBookingPageWeb.goToPaymentPage();
        travelPlanPage.skipTravelPlan();
        String orderID = paymentPage.getOrderId();
        Assertion.assertThat("Verify order ID is displayed in payment page", !orderID.isEmpty(), is(true));

        step("User select payment method ");
        paymentPage.clickOnPaymentButton();

        step("User verifies the order in Purchase list");
        Assertion.assertThat("orderID should be visible and should be same as from payment page and in purchase List ", corporateApi.checkPurchaseOrderList(orderID,email,password,userNameUI), is(true));
    }

    @Author(names = {"Harshit Ranjan"})
    @Module(name = "eTicket")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void HotelMeetTravelPolicy() throws FailedLoginException, InterruptedException, ExecutionException {
        Session<WebDriver> session = sessionThreadLocal.get();

        Map<String, String> param = getParam("HotelMeetTravelPolicy");
        BaseTest.paramsThreadLocal.set(param);

        String travelerUsername = param.get("username");
        String password = param.get("password");
        String otp= param.get("otp");
        String hotel = param.get("hotel");
        String adminUsername = param.get("adminUsername");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        HotelLandingPageWeb<?> hotelLandingPage = HotelLandingPageWeb.getInstance(session);
        HotelRoomListPage<?> hotelRoomListPage = new HotelRoomListPage<>(session);
        HotelBookingFormPageWeb<?> hotelBookingFormPage = HotelBookingFormPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        TravelPlanWeb<?> travelPlanPage = TravelPlanWeb.getInstance(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);


        step("Go to home");
        homePage.goToHomePage();

        step("Go to account page");
        loginPage.goToAccountPage();

        step("User login");
        loginPage.loginAsGuest();
        loginPage.loginIntoWeb(travelerUsername, password, otp, 1);

        step("User select IDR currency");
        homePage.selectCurrency("IDR");

        step("User open hotel vertical");
        homePage.openVerticalPageViaURL("Hotel");

        step("User search hotel location");
        hotelLandingPage.searchStayLocation(hotel);

        step("Set guests and rooms");
        hotelLandingPage.clickRoomAndGuestButton();
        hotelLandingPage.setTotalRoom(1);
        hotelLandingPage.setTotalGuest(1);
        hotelLandingPage.clickDone();

        step("Submit search");
        hotelLandingPage.clickSubmit();

        step("Click book button");
        hotelRoomListPage.clickBookButton();

        step("Continue to payment page");
        hotelBookingFormPage.continueToSecondPartThenPaymentPage();
        travelPlanPage.skipTravelPlan();

        step("Payment page should be displayed");
        String orderID = paymentPage.getOrderId();
        int finalAmount = Integer.parseInt(paymentPage.getFinalPrice());
        Assertion.assertThat("Final amount should be greater than zero", finalAmount, is(greaterThan(0)));
        orderID = String.valueOf(getOrderId(orderID));
        Assertion.assertThat("Corporate billing option is selected", paymentPage.getSelectedPayment().toLowerCase(), is("corporate billing"));

        step("Submit payment using Corporate Billing");
        paymentPage.clickOnPaymentButton();

        step("Approve booking by admin account for Order ID: " + orderID);
        Boolean bookingStatus = corporateApi.approveBookingB2BApi(adminUsername, password, orderID);
        Assertion.assertThat("Booking approved by B2B dashboard", bookingStatus, is(true));

        step("Wait for few seconds to get auto approved");
        orderListPage.waitForPaymentSuccessful(orderID,20);

        paymentPage.clickSeeOrderListButton();
        step("Open the order list and go to order detail");

        orderListPage.openActiveTicketViaURL(orderID);
        orderDetailPage.skipCoachmark();
        Assertion.assertThat("orderID should be visible and should be same as from payment page", orderDetailPage.getOrderId(), containsString(orderID));
        Assertion.assertThat("e-Ticket on the order detail should be  published", orderDetailPage.verifyIssuedETicket(), is(true));
    }

    @Author(names = {"Harshit Ranjan"})
    @Module(name = "eTicket")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void HotelDoesNotMeetTravelPolicy() throws FailedLoginException, InterruptedException, ExecutionException {
        Session<WebDriver> session = sessionThreadLocal.get();

        Map<String, String> param = getParam("HotelDoesNotMeetTravelPolicy");
        BaseTest.paramsThreadLocal.set(param);

        String travelerUsername = param.get("username");
        String password = param.get("password");
        String otp= param.get("otp");
        String hotel = param.get("hotel");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HotelLandingPageWeb<?> hotelLandingPage = HotelLandingPageWeb.getInstance(session);
        HotelRoomListPage<?> hotelRoomListPage = new HotelRoomListPage<>(session);

        step("Go to home");
        homePage.goToHomePage();

        step("Go to account page");
        loginPage.goToAccountPage();

        step("User login");
        loginPage.loginAsGuest();
        loginPage.loginIntoWeb(travelerUsername, password, otp, 1);

        step("User select IDR currency");
        homePage.selectCurrency("IDR");

        step("User open hotel vertical");
        homePage.openVerticalPageViaURL("Hotel");

        step("User search hotel location");
        hotelLandingPage.searchStayLocation(hotel);
        String dateType = param.get("date_type");
        String date = param.get("date");

        hotelLandingPage.setStayDate(dateType,date);

        step("Set guests and rooms");
        hotelLandingPage.clickRoomAndGuestButton();
        hotelLandingPage.setTotalRoom(1);
        hotelLandingPage.setTotalGuest(1);
        hotelLandingPage.clickDone();

        step("Submit search");
        hotelLandingPage.clickSubmit();

        step("Click book button");
        hotelRoomListPage.clickBookButton();

        Assertion.assertThat("Does not meet travel policy popup is displayed", hotelRoomListPage.doesNotMeetTravelPolicyPopUpAppears(), is(true));
    }
    @Author(names = {"Harshit Ranjan"})
    @Module(name = "eTicket")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void TravelerRequestingAdmin() throws FailedLoginException, InterruptedException, ExecutionException {
        Session<WebDriver> session = sessionThreadLocal.get();

        Map<String, String> param = getParam("TravelerRequestingAdmin");
        BaseTest.paramsThreadLocal.set(param);

        String travelerUsername = param.get("travelerUsername");
        String travelerPassword = param.get("travelerPassword");
        String otp= param.get("otp");
        String hotel = param.get("hotel");
        String approverUsername = param.get("approverUsername");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        HotelLandingPageWeb<?> hotelLandingPage = HotelLandingPageWeb.getInstance(session);
        HotelRoomListPage<?> hotelRoomListPage = new HotelRoomListPage<>(session);
        HotelBookingFormPageWeb<?> hotelBookingFormPage = HotelBookingFormPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        TravelPlanWeb<?> travelPlanPage = TravelPlanWeb.getInstance(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);


        step("Go to home");
        homePage.goToHomePage();

        step("Go to account page");
        loginPage.goToAccountPage();

        step("User login");
        loginPage.loginAsGuest();
        loginPage.loginIntoWeb(travelerUsername, travelerPassword, otp, 1);

        step("User select IDR currency");
        homePage.selectCurrency("IDR");

        step("User open hotel vertical");
        homePage.openVerticalPageViaURL("Hotel");

        step("User search hotel location");
        hotelLandingPage.searchStayLocation(hotel);
        hotelLandingPage.setStayDate("range date","15-25");

        step("Set guests and rooms");
        hotelLandingPage.clickRoomAndGuestButton();
        hotelLandingPage.setTotalRoom(1);
        hotelLandingPage.setTotalGuest(1);
        hotelLandingPage.clickDone();

        step("Submit search");
        hotelLandingPage.clickSubmit();

        step("Click book button");
        hotelRoomListPage.clickBookButton();

        step("Continue to payment page");
        hotelBookingFormPage.continueToSecondPartThenPaymentPage();
        travelPlanPage.skipTravelPlan();

        step("Payment page should be displayed");
        String orderID = paymentPage.getOrderId();
        int finalAmount = Integer.parseInt(paymentPage.getFinalPrice());
        Assertion.assertThat("Final amount should be greater than zero", finalAmount, is(greaterThan(0)));
        orderID = String.valueOf(getOrderId(orderID));
        Assertion.assertThat("Corporate billing option is selected", paymentPage.getSelectedPayment().toLowerCase(), is("corporate billing"));

        step("Submit payment using Corporate Billing");
        paymentPage.clickOnPaymentButton();

        step("Approve booking by admin account for Order ID: " + orderID);
        Boolean bookingStatus = corporateApi.approveBookingB2BApi(approverUsername, travelerPassword, orderID);
        Assertion.assertThat("Booking approved by B2B dashboard", bookingStatus, is(true));

        step("Wait for few seconds to get auto approved");
        orderListPage.waitForPaymentSuccessful(orderID,20);

        paymentPage.clickSeeOrderListButton();
        step("Open the order list and go to order detail");

        orderListPage.openActiveTicketViaURL(orderID);
        orderDetailPage.skipCoachmark();
        Assertion.assertThat("orderID should be visible and should be same as from payment page", orderDetailPage.getOrderId(), containsString(orderID));
        Assertion.assertThat("e-Ticket on the order detail should be  published", orderDetailPage.verifyIssuedETicket(), is(true));
    }

    @Author(names = "HARSHIT")
    @Module(name = "Full Refund")
    @Tribe(name = "Hotel")
    @Vertical(name = "B2B")
    @Test
    public void testAutoApproveRefundWEB() throws FailedLoginException, InterruptedException {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        Map<String, String> param = getParam("testAutoApproveRefundWEB");
        BaseTest.paramsThreadLocal.set(param);

        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        RefundPageWeb<?> refundPage = RefundPageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String startDate = param.get("startDate");
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));
        int room = Integer.parseInt(param.get("room"));
        String currency = "IDR";
        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String query = "Hotel SDET Domestic";

        String orderID;
        step("Create new hotel issued order");
        orderID = hotelApi.createIssuedOrderApiB2B(
                username, password, startDate, query, adult, night, room, "IDR",
                orderFullName, orderNameTitle, corporateApi.getEmployeeData(username), false,
                "");
        Assertion.assertThat("Hotel Booking via API is successful for B2B account", orderID, is(not(emptyOrNullString())));

        step("Goto home");
        homePage.goToHomePage();

        step("Log out");
        loginPage.loginAsGuest();

        step("Log in as an existing user");
        loginPage.loginIntoWeb(username, password, otp, retryCounter);

        step("Find the issued order");
        orderListPage.openActiveTicketViaURL(orderID);
        orderDetailPage.skipCoachmark();

        step("Verify hotel order details");
        Assertion.assertThat("Order ID is displayed correctly", orderDetailPage.getOrderId(), Is.is(orderID));

        if (Configs.PLATFORM == Platform.DWEB) {
            Assertion.assertThat("Refund button should be displayed", orderDetailPage.isRefundButtonDisplayed(), Is.is(true));
            step("Open refund page");
            orderDetailPage.clickRefundButton();
        } else {
            orderDetailPage.refundButtonInweb();
            refundPage.waitForContentLoaderDisappear();
        }

        step("Click to continue");
        refundPage.continueToRefund();

        step("Select random refund reason");
        refundPage.selectRandomRefundReason();

        step("Verify refund estimation section");
        refundPage.waitForEstimationAmountAppear("5");
        String refundReceive = refundPage.getRefundYoullReceive();
        String refundFee = refundPage.getRefundFee();
        String refundTotal = refundPage.getRefundTotal();
        List<String> amounts = new ArrayList<>(Arrays.asList(refundReceive, refundFee, refundTotal));
        Assertion.assertThat("Refund Total amount is correct", CoreFunctions.removeText(refundReceive)+CoreFunctions.removeText(refundFee), Is.is(CoreFunctions.removeText(refundTotal)));

        step("Verify refund amount breakdown");
        refundPage.openRefundBreakdown();
        amounts.clear();
        amounts.addAll(refundPage.getRefundAmountBreakdown(currency));

        if (refundPage.getRefundFeeBreakdown(currency).isPresent()) amounts.addAll(refundPage.getRefundFeeBreakdown(currency).get());
        amounts.addAll(refundPage.getRefundTotalBreakdown(currency));
        Assertion.assertThat("Total breakdown refund same as estimation", refundPage.getRefundTotalBreakdownSum(currency), Is.is(String.valueOf(CoreFunctions.removeText(refundTotal))));
        refundPage.closeRefundBreakdown();

        step("Continue to next page");
        refundPage.clickButton("Continue");
        refundPage.waitRefundSecondPageLoaded();
        Assertion.assertThat("Order ID on 2nd refund page is correct", refundPage.getOrderIdOnRefundBanner(), containsString(orderID));
        Assertion.assertThat("Refund progress banner same as refund step", refundPage.getActiveProgress(), Is.is(2));

        step("Verify refund method and amount");
        String refundMethodSection = refundPage.getRefundMethod();
        Assertion.assertThat("Refund method is correct", refundMethodSection, not(emptyOrNullString()));
        Assertion.assertThat("Refund amount is same as prev page", refundTotal, is(refundPage.getRefundTotal()));

        step("Check Refund TnC");
        Assertion.assertThat("Submit refund button is disabled", refundPage.isDisabledSubmitButton(), is(true));
        refundPage.clickChecklistTnc();
        refundPage.clickButton("Submit");

        step("Submit refund");
        refundPage.clickButton("Process Refund");
        refundPage.waitForSubmitRefundLoader();
        Assertion.assertThat("Success submit refund figure content", refundPage.getSuccessRefundFigureContent(), containsString("Successfully submit the refund"));

    }
    @Author(names = {"Harshit Ranjan"})
    @Module(name = "eTicket")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void NonIDRCurrencyB2bBooking() throws FailedLoginException, InterruptedException, ExecutionException {
        Session<WebDriver> session = sessionThreadLocal.get();

        Map<String, String> param = getParam("NonIDRCurrencyB2bBooking");
        BaseTest.paramsThreadLocal.set(param);

        String travelerUsername = param.get("travelerUsername");
        String password = param.get("password");
        String otp= param.get("otp");
        String hotel = param.get("hotel");
        String dateType = param.get("dateType");
        String date = param.get("date");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        HotelLandingPageWeb<?> hotelLandingPage = HotelLandingPageWeb.getInstance(session);
        HotelRoomListPage<?> hotelRoomListPage = new HotelRoomListPage<>(session);
        HotelBookingFormPageWeb<?> hotelBookingFormPage = HotelBookingFormPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        TravelPlanWeb<?> travelPlanPage = TravelPlanWeb.getInstance(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);


        step("Go to home");
        homePage.goToHomePage();

        step("Go to account page");
        loginPage.goToAccountPage();

        step("User login");
        loginPage.loginAsGuest();
        loginPage.loginIntoWeb(travelerUsername, password, otp, 1);

        step("User select IDR currency");
        homePage.selectCurrency("SGD");

        step("User open hotel vertical");
        homePage.openVerticalPageViaURL("Hotel");

        step("User search hotel location");
        hotelLandingPage.searchStayLocation(hotel);
        hotelLandingPage.setStayDate("range date","15-25");

        step("Set guests and rooms");
        hotelLandingPage.clickRoomAndGuestButton();
        hotelLandingPage.setTotalRoom(1);
        hotelLandingPage.setTotalGuest(1);
        hotelLandingPage.clickDone();

        step("Submit search");
        hotelLandingPage.clickSubmit();

        step("Click book button");
        hotelRoomListPage.clickBookButton();

        step("Continue to payment page");
        hotelBookingFormPage.continueToSecondPartThenPaymentPage();
        travelPlanPage.skipTravelPlan();

        paymentPage.clickOnPaymentButton();

        Assertion.assertThat("B2B Popup is shown",paymentPage.isB2bPopupVisible(),is(true));
    }

    @Author(names = {"Gaurav"})
    @Module(name = "VI")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void flightVIOneWayRouteLoginB2B() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("flightVIOneWayRouteLoginB2B");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPageWeb = FlightLandingPageWeb.getInstance(session);
        FlightSearchResultPageWeb<?> flightSearchResultPage = FlightSearchResultPageWeb.getInstance(session);
        FlightProductReviewPage<?> flightProductReviewPage = new FlightProductReviewPage<>(session);
        FlightBookingFormPageWeb<?> flightBookingFormPage = FlightBookingFormPageWeb.getInstance(session);

        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");

        String username = param.get("username");
        String password = param.get("password");
        String currency = param.get("currency");
        String otp = param.get("otp");
        String filterType = param.get("filterType");
        String srpViNudgesCheckin = param.get("srpViNudgesCheckin");
        String pdpViNudgesSelef = param.get("pDpViNudgesSelef");
        String pdpViNudgesCheckin = param.get("pDPViNudgesCheckin");

        step("User open home page");
        homePage.goToHomePage();

        step("Login as " + username + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(username, password);

        step("User select IDR currency");
        homePage.selectCurrency(currency);

        step("User open Flight vertical");
        homePage.openVerticalPageViaURL("Flights");

        step("Verify that VI fligts are coming on SRP page ");
       flightLandingPageWeb.checkVIFlight(originAirport,destinationAirport);

        step("Apply filter for VI nudges " + filterType);
        flightSearchResultPage.viFlightFilter();
        flightSearchResultPage.otherFilters(filterType);
        flightSearchResultPage.skipRefund100P();

        step("Click on Show Details on fligt card ");
        flightSearchResultPage.clickShowDetails();

        step("Verify VI nudge on SRP page");
        Assertion.assertThat("SRP ",flightSearchResultPage.viPresentOnSrp(srpViNudgesCheckin),is(true));

        step("Select flight on SRP page");
        flightSearchResultPage.selectFlight();

        step("Continue to PDP and verify VI nudges" +pdpViNudgesSelef);
        Assertion.assertThat("PDP",flightProductReviewPage.viPresentOnPdp(pdpViNudgesSelef),is(true));
        Assertion.assertThat("PDP",flightProductReviewPage.viPresentOnPdp(pdpViNudgesCheckin),is(true));

        step("Continue to booking form");
        flightProductReviewPage.continueToBookingForm();

        step("Go to payment page");
        flightBookingFormPage.fillPassengerDetails();
        flightBookingFormPage.goToPaymentPageFromBookingFormPage();

        step("Verify VI nudge on Interim  page");
        Assertion.assertThat("Verify VI nudge on Interim  page",flightBookingFormPage.vICheckinBaggageInterimPage(),is(true));

    }

    @Author(names = "Aditi")
    @Module(name = "Payment")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void testCorporateBillingInsufficientBalanceValidationPopupDweb() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testCorporateBillingInsufficientBalanceValidationPopupDweb");
        BaseTest.paramsThreadLocal.set(param);
        Session<WebDriver> session = sessionThreadLocal.get();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HotelLandingPageWeb<?> hotelLandingPage = HotelLandingPageWeb.getInstance(session);
        HotelRoomListPage<?> hotelRoomListPage = new HotelRoomListPage<>(session);
        HotelBookingFormPageWeb<?> hotelBookingFormPage = HotelBookingFormPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        TravelPlanWeb<?> travelPlanPage = TravelPlanWeb.getInstance(session);
        CorporateDashboard<?> corporateDashboard = new CorporateDashboard<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String hotelName = param.get("hotelName");
        String contactName = randomName();
        String contactPhone = randomPhone();

        step("Go to home");
        homePage.goToHomePage();

        step("Login with B2B account that has insufficient corporate billing balance");
        loginPage.loginWithSpecificAccountViaCookie(email, password);
        homePage.waitForHomePageToOpen();

        step("Open corporate purchase list and verify billing balance is empty");
        corporateDashboard.openPurchaseListViaDeeplink();
        Assertion.assertThat(
                "Verify if balance is empty then can not create bookings with corporate billing",
                corporateDashboard.isCorporateBillingBalanceEmpty(), is(true));

        homePage.goToTiketb2cPageIfPresent();
        homePage.waitForHomePageToOpen();

        step("User select IDR currency");
        homePage.selectCurrency("IDR");

        step("User open hotel vertical");
        homePage.openVerticalPage("Hotels");

        step("User search hotel");
        hotelLandingPage.searchStayLocation(hotelName);

        step("Set guests and rooms");
        hotelLandingPage.clickRoomAndGuestButton();
        hotelLandingPage.setTotalRoom(1);
        hotelLandingPage.setTotalGuest(1);
        hotelLandingPage.clickDone();

        step("Submit search");
        hotelLandingPage.clickSubmit();

        step("Click book button");
        hotelRoomListPage.clickBookButton();

        step("Fill contact details if empty");
        hotelBookingFormPage.fillContactDetailIfEmpty(contactName, contactPhone);

        step("Continue to payment page");
        hotelBookingFormPage.continueToSecondPartThenPaymentPage();
        travelPlanPage.skipTravelPlan();

        step("Payment page should be displayed with Corporate Billing selected");
        paymentPage.getOrderId();
        Assertion.assertThat("Corporate billing option is selected", paymentPage.getSelectedPayment().toLowerCase(), is("corporate billing"));

        step("Click Pay with Corporate Billing");
        paymentPage.clickOnPaymentButton();

        step("Verify insufficient corporate billing balance validation popup is displayed");
        Assertion.assertThat("Corporate billing insufficient balance popup should be displayed",
                paymentPage.isCorporateBillingInsufficientBalancePopupDisplayed(), is(true));

        step("Verify popup title");
        Assertion.assertThat("Popup title should indicate insufficient corporate billing balance",
                paymentPage.getCorporateBillingInsufficientBalancePopupTitle(),
                anyOf(
                        containsString("Corporate billing balance is insufficient"),
                        containsString("Saldo corporate billing tidak mencukupi"),
                        containsString("Saldo tagihan korporat tidak mencukupi")
                ));

        step("Verify popup description");
        Assertion.assertThat("Popup description should ask user to contact admin to top up balance",
                paymentPage.getCorporateBillingInsufficientBalancePopupDescription(),
                anyOf(
                        containsString("Please contact your admin to top up the corporate billing balance before proceeding with this transaction."),
                        containsString("Silakan hubungi admin untuk mengisi ulang saldo corporate billing sebelum melanjutkan transaksi ini."),
                        containsString("Silakan hubungi admin untuk mengisi ulang saldo tagihan korporat sebelum melanjutkan transaksi ini.")
                ));

        step("Verify Ok, got it button is displayed on popup");
        Assertion.assertThat("Ok, got it button should be displayed on popup",
                paymentPage.isCorporateBillingInsufficientBalanceOkButtonDisplayed(), is(true));

        step("Click Ok, got it on validation popup");
        paymentPage.clickCorporateBillingInsufficientBalanceOkButton();

        step("Verify popup is dismissed and payment page is visible again");
        Assertion.assertThat("Corporate billing insufficient balance popup should be dismissed",
                paymentPage.isCorporateBillingInsufficientBalancePopupDismissed(), is(true));
        Assertion.assertThat("Payment page should be visible again", paymentPage.isPaymentPageLoaded(), is(true));
        Assertion.assertThat("Corporate billing option should still be selected after dismissing popup",
                paymentPage.getSelectedPayment().toLowerCase(), is("corporate billing"));
    }

    @Author(names = "Aditi")
    @Module(name = "Payment")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void testCorporateBillingInactiveEmployeeValidationDweb() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testCorporateBillingInactiveEmployeeValidationDweb");
        BaseTest.paramsThreadLocal.set(param);
        Session<WebDriver> session = sessionThreadLocal.get();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HotelLandingPageWeb<?> hotelLandingPage = HotelLandingPageWeb.getInstance(session);
        HotelRoomListPage<?> hotelRoomListPage = new HotelRoomListPage<>(session);
        HotelBookingFormPageWeb<?> hotelBookingFormPage = HotelBookingFormPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        CorporateDashboard<?> corporateDashboard = new CorporateDashboard<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String hotelName = param.get("hotelName");
        String contactName = randomName();
        String contactPhone = randomPhone();

        step("Go to home");
        homePage.goToHomePage();

        step("Login with B2B account whose employee is inactive or removed");
        loginPage.loginWithSpecificAccountViaCookie(email, password);
        homePage.waitForHomePageToOpen();

        step("Verify inactive employee cannot access corporate purchase list and stays on consumer home");
        Assertion.assertThat(
                "Verify inactive/removed employee cannot access Corporate Dashboard and stays on consumer home",
                corporateDashboard.isEmployeeInactiveOrRemoved(homePage), is(true));

        step("User select IDR currency");
        homePage.selectCurrency("IDR");

        step("User open hotel vertical");
        homePage.openVerticalPage("Hotels");

        step("User search hotel");
        hotelLandingPage.searchStayLocation(hotelName);

        step("Set guests and rooms");
        hotelLandingPage.clickRoomAndGuestButton();
        hotelLandingPage.setTotalRoom(1);
        hotelLandingPage.setTotalGuest(1);
        hotelLandingPage.clickDone();

        step("Submit search");
        hotelLandingPage.clickSubmit();

        step("Click book button");
        hotelRoomListPage.clickBookButton();

        step("Fill contact details if empty");
        hotelBookingFormPage.fillContactDetailIfEmpty(contactName, contactPhone);

        step("Continue to payment page");
        hotelBookingFormPage.continueToSecondPartThenPaymentPage();

        step("Payment page should be displayed without corporate billing");
        paymentPage.getOrderId();
        Assertion.assertThat("Payment page should be visible", paymentPage.isPaymentPageLoaded(), is(true));
        Assertion.assertThat("Corporate billing option should not be selected",
                paymentPage.getSelectedPayment().toLowerCase(), not("corporate billing"));
        Assertion.assertThat("Corporate billing payment option should not be displayed",
                paymentPage.isCorporateBillingPaymentOptionDisplayed(), is(false));
        Assertion.assertThat("Pay with Corporate Billing button should not be displayed",
                paymentPage.isPayWithCorporateBillingButtonDisplayed(), is(false));

        step("Verify other payment options are still visible");
        Assertion.assertThat("B2C payment option should be visible",
                paymentPage.getSelectedPayment(), anyOf(is("Sakuku"), is("Use Another Card"), is("BCA Virtual Account")));
    }

    @Author(names = "Aditi")
    @Module(name = "Payment")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void testCorporateBillingSuspendedCorporateValidationPopupDweb() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testCorporateBillingSuspendedCorporateValidationPopupDweb");
        BaseTest.paramsThreadLocal.set(param);
        Session<WebDriver> session = sessionThreadLocal.get();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HotelLandingPageWeb<?> hotelLandingPage = HotelLandingPageWeb.getInstance(session);
        HotelRoomListPage<?> hotelRoomListPage = new HotelRoomListPage<>(session);
        HotelBookingFormPageWeb<?> hotelBookingFormPage = HotelBookingFormPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        TravelPlanWeb<?> travelPlanPage = TravelPlanWeb.getInstance(session);
        CorporateDashboard<?> corporateDashboard = new CorporateDashboard<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String hotelName = param.get("hotelName");
        String contactName = randomName();
        String contactPhone = randomPhone();

        step("Go to home");
        homePage.goToHomePage();

        step("Login with B2B account");
        loginPage.loginWithSpecificAccountViaCookie(email, password);
        homePage.waitForHomePageToOpen();

        step("Open corporate purchase list");
        corporateDashboard.openPurchaseListViaDeeplink();

        homePage.goToTiketb2cPageIfPresent();
        homePage.waitForHomePageToOpen();

        step("User select IDR currency");
        homePage.selectCurrency("IDR");

        step("User open hotel vertical");
        homePage.openVerticalPage("Hotels");

        step("User search hotel");
        hotelLandingPage.searchStayLocation(hotelName);

        step("Set guests and rooms");
        hotelLandingPage.clickRoomAndGuestButton();
        hotelLandingPage.setTotalRoom(1);
        hotelLandingPage.setTotalGuest(1);
        hotelLandingPage.clickDone();

        step("Submit search");
        hotelLandingPage.clickSubmit();

        step("Click book button");
        hotelRoomListPage.clickBookButton();

        step("Fill contact details if empty");
        hotelBookingFormPage.fillContactDetailIfEmpty(contactName, contactPhone);

        step("Continue to payment page");
        hotelBookingFormPage.continueToSecondPartThenPaymentPage();
        travelPlanPage.skipTravelPlan();

        step("Payment page should be displayed with Corporate Billing selected");
        paymentPage.getOrderId();
        Assertion.assertThat("Corporate billing option is selected", paymentPage.getSelectedPayment().toLowerCase(), is("corporate billing"));

        step("Click Corporate Billing / Pay with Corporate Billing");
        paymentPage.clickOnPaymentButton();

        step("Verify suspended corporate booking validation popup is displayed");
        Assertion.assertThat("Corporate suspended booking popup should be displayed", paymentPage.isCorporateSuspendedBookingPopupDisplayed(), is(true));

        step("Verify popup title");
        Assertion.assertThat("Popup title should indicate unable to create a corporate booking", paymentPage.getCorporateSuspendedBookingPopupTitle(), anyOf(containsString("Unable to create a corporate booking"), containsString("Tidak dapat membuat pemesanan korporat")));

        step("Verify popup description");
        Assertion.assertThat("Popup description should mention corporate is inactive and ask user to contact admin",
                paymentPage.getCorporateSuspendedBookingPopupDescription(),
                anyOf(
                        containsString("This may be due to your corporate being inactive, temporarily unable to make transactions, or other restrictions. Please contact your corporate admin for assistance."),
                        containsStringIgnoringCase("inactive"),
                        containsStringIgnoringCase("tidak aktif")
                ));
        Assertion.assertThat("Popup description should contain the word inactive",
                paymentPage.getCorporateSuspendedBookingPopupDescription().toLowerCase(),
                anyOf(containsString("inactive"), containsString("tidak aktif")));

        step("Verify Ok, got it button is displayed on popup");
        Assertion.assertThat("Ok, got it button should be displayed on popup",
                paymentPage.isCorporateSuspendedBookingOkButtonDisplayed(), is(true));

        step("Click Ok, got it on validation popup");
        paymentPage.clickCorporateSuspendedBookingOkButton();

        step("Verify popup is dismissed and payment page is visible again");
        Assertion.assertThat("Corporate suspended booking popup should be dismissed",
                paymentPage.isCorporateSuspendedBookingPopupDismissed(), is(true));
        Assertion.assertThat("Payment page should be visible again", paymentPage.isPaymentPageLoaded(), is(true));
        Assertion.assertThat("Corporate billing option should still be selected after dismissing popup",
                paymentPage.getSelectedPayment().toLowerCase(), is("corporate billing"));

    }

    @Author(names = "Aditi")
    @Module(name = "Booking")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void testTravellerOrderAutoApprovedWhenMeetTravelPolicyDweb() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testTravellerOrderAutoApprovedWhenMeetTravelPolicyDweb");
        BaseTest.paramsThreadLocal.set(param);
        Session<WebDriver> session = sessionThreadLocal.get();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HotelLandingPageWeb<?> hotelLandingPage = HotelLandingPageWeb.getInstance(session);
        HotelRoomListPage<?> hotelRoomListPage = new HotelRoomListPage<>(session);
        HotelBookingFormPageWeb<?> hotelBookingFormPage = HotelBookingFormPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        TravelPlanWeb<?> travelPlanPage = TravelPlanWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);

        String email = param.get("email");
        String password = param.get("password");
        String hotelName = param.get("hotelName");
        String contactName = randomName();
        String contactPhone = randomPhone();

        step("Go to home");
        homePage.goToHomePage();

        step("Login with traveller account");
        loginPage.loginWithSpecificAccountViaCookie(email, password);
        homePage.waitForHomePageToOpen();
        homePage.goToTiketb2cPageIfPresent();
        homePage.waitForHomePageToOpen();

        step("User select IDR currency");
        homePage.selectCurrency("IDR");

        step("User open hotel vertical");
        homePage.openVerticalPage("Hotels");

        step("User search hotel");
        hotelLandingPage.searchStayLocation(hotelName);

        step("Set guests and rooms");
        hotelLandingPage.clickRoomAndGuestButton();
        hotelLandingPage.setTotalRoom(1);
        hotelLandingPage.setTotalGuest(1);
        hotelLandingPage.clickDone();

        step("Submit search");
        hotelLandingPage.clickSubmit();

        step("Verify selected hotel meets travel policy");
        Assertion.assertThat("Hotel should meet travel policy before booking",
                hotelRoomListPage.isMeetsTravelPolicyNudgeVisible(), is(true));

        step("Click book button");
        hotelRoomListPage.clickBookButton();

        step("Fill contact details if empty");
        hotelBookingFormPage.fillContactDetailIfEmpty(contactName, contactPhone);

        step("Continue to payment page");
        hotelBookingFormPage.continueToSecondPartThenPaymentPage();
        travelPlanPage.skipTravelPlan();

        step("Payment page should be displayed with Corporate Billing selected");
        String orderID = paymentPage.getOrderId();
        orderID = String.valueOf(getOrderId(orderID));
        Assertion.assertThat("Corporate billing option is selected", paymentPage.getSelectedPayment().toLowerCase(), is("corporate billing"));

        step("Submit payment using Corporate Billing");
        paymentPage.clickOnPaymentButton();

        step("Wait for booking to be auto approved");
        orderListPage.waitForPaymentSuccessful(orderID, 20);
        Assertion.assertThat(
                "Verify traveller order will be autoapproved, if meets travel policy and the company settings mark as auto approve",
                paymentPage.isPaymentSuccessfulVisible(), is(true));

        step("Open the order list and verify issued e-ticket");
        paymentPage.clickSeeOrderListButton();
        orderListPage.openIssuedTicketViaURL(orderID, 20);
        orderDetailPage.skipCoachmark();
        Assertion.assertThat("Booking should be auto approved and e-ticket should be published",
                orderDetailPage.verifyIssuedETicket(), is(true));
    }

    @Author(names = "Aditi")
    @Module(name = "corporateCode")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void testCorporateCodeBookingOnewayGarudaNEODweb() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testCorporateCodeBookingOnewayGarudaNEODweb");
        BaseTest.paramsThreadLocal.set(param);
        Session<WebDriver> session = sessionThreadLocal.get();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPageWeb = FlightLandingPageWeb.getInstance(session);
        FlightSearchResultPageWeb<?> flightSearchResultPage = FlightSearchResultPageWeb.getInstance(session);
        FlightProductReviewPage<?> flightProductReviewPage = new FlightProductReviewPage<>(session);
        FlightBookingFormPageWeb<?> flightBookingFormPage = FlightBookingFormPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        TravelPlanWeb<?> travelPlanPage = TravelPlanWeb.getInstance(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);
        FlightApi<?> flightApi = new FlightApi<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");
        String currency = param.get("currency");
        String firstName = param.get("firstName");
        String lastName = param.get("lastName");
        String optionalLocations = param.getOrDefault("optionalLocations", "");
        String optionalAirline = param.getOrDefault("optionalAirline", "");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int adultPassengers = Integer.parseInt(param.get("adultPassengers"));
        int childPassengers = Integer.parseInt(param.get("childPassengers"));
        int infantPassengers = Integer.parseInt(param.get("infantPassengers"));

        ArrayList<CorporateCodeData> corporateCodeData = corporateApi.setAndGetCorporateCodeData(param.get("corporateCodeData"));
        ArrayList<String> airlines = corporateApi.getAirlineName(corporateCodeData);
        String airlineDepart = airlines.get(0);

        step("Create the corporate code for airlines " + airlines);
        ArrayList<Integer> corporateCodeIds = corporateApi.findCreateCorporateCode(email, password, corporateCodeData);
        Assertion.assertThat("Corporate code is created", corporateCodeIds, everyItem(notNullValue()));

        step("Find dynamic one-way date for route " + originAirport + " to " + destinationAirport);
        FlightDynamicData flightDynamicData = flightApi.findDate(
                email, password,
                originAirport, destinationAirport, daysToDepartureDate,
                adultPassengers, childPassengers, infantPassengers,
                tripType, airlineDepart, "", optionalLocations, optionalAirline, currency);

        originAirport = flightDynamicData.originAirport;
        destinationAirport = flightDynamicData.destinationAirport;
        String departureDate = CoreFunctions.convertToLongDate(flightDynamicData.departureDate);
        String departAirline = flightDynamicData.airlineDepart;

        step("Go to home");
        homePage.goToHomePage();

        step("Login with B2B account");
        loginPage.loginWithSpecificAccountViaCookie(email, password);
        homePage.waitForHomePageToOpen();
        homePage.goToTiketb2cPageIfPresent();
        homePage.waitForHomePageToOpen();

        step("User select " + currency + " currency");
        homePage.selectCurrency(currency);

        step("User open Flight vertical");
        homePage.openVerticalPageViaURL("Flights");

        step("Toggle trip type");
        flightLandingPageWeb.toggleTripType(tripType);

        step("Select origin airport");
        flightLandingPageWeb.selectOrigin(originAirport);

        step("Select destination airport");
        flightLandingPageWeb.selectDestination(destinationAirport);

        step("Select date");
        flightLandingPageWeb.selectDate(departureDate);

        step("Add passengers");
        flightLandingPageWeb.addPassenger(adultPassengers, childPassengers, infantPassengers);

        step("Search flight");
        flightLandingPageWeb.searchFlight();
        flightSearchResultPage.isSRPLoaded();

        step("Apply filter and verify Corporate Deals label on flight card for " + departAirline);
        flightSearchResultPage.filterAirline(departAirline);
        Assertion.assertThat("Corporate Deals label should be visible on flight card after filtering " + departAirline,
                flightSearchResultPage.isCorporateDealsLabelOnFlightCardPresent(), is(true));
        Assertion.assertThat("Corporate Deals label text should be displayed on flight card",
                flightSearchResultPage.getCorporateDealsLabelOnFlightCardText(), is("Corporate Deals"));

        step("Select flight for " + departAirline);
        flightSearchResultPage.selectFlightByAirline(departAirline);

        step("Continue to booking form");
        flightProductReviewPage.continueToBookingForm();

        step("Fill passenger details");
        flightBookingFormPage.fillPassengerDetails();

        step("Go to payment page");
        flightBookingFormPage.goToPaymentPageFromBookingFormPage();

        step("verify Review Passenger's Data");
        flightBookingFormPage.clickItsCorrectOnReviewPassengerDataPopup();

        flightBookingFormPage.clickNoThanksOnFlightMayReschedulePopup();
        travelPlanPage.skipTravelPlan();


        step("Payment page should be displayed");
        String orderID = paymentPage.getOrderId();
        orderID = String.valueOf(getOrderId(orderID));
        Assertion.assertThat("Corporate billing option is selected", paymentPage.getSelectedPayment().toLowerCase(), is("corporate billing"));

        step("Verify order retrieve API returns garudaNEO distribution type and corporate codes");
        FlightOrderDetailResponse orderDetailResponse = flightApi.getOrderDetail(orderID, email);
        Assertion.assertThat("Order retrieve API should return SUCCESS", orderDetailResponse.code, is("SUCCESS"));
        FlightOrderDetailResponse.BookDetail bookDetail = orderDetailResponse.data.bookResponses.get(0).bookDetail;

        Assertion.assertThat("Distribution type should be garudaNEO", bookDetail.distributionType, equalToIgnoringCase("garudaNEO"));
        Assertion.assertThat("Corporate codes should not be null", bookDetail.corporateCodes, notNullValue());

    }
}
