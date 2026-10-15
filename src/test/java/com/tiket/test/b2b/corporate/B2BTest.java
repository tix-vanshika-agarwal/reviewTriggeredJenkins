package com.tiket.test.b2b.corporate;

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
import com.tiket.page.accommodation.hotel.bookingformpage.BookingFormPage;
import com.tiket.page.accommodation.hotel.detailpage.DetailPage;
import com.tiket.page.accommodation.hotel.landingpage.LandingPage;
import com.tiket.page.accommodation.hotel.reschedulepage.HotelReschedulePage;
import com.tiket.page.accommodation.hotel.roomlistpage.RoomListPage;
import com.tiket.page.accommodation.hotel.searchresultpage.SearchResultPage;
import com.tiket.page.account.AccountPage;
import com.tiket.page.bus.bookingform.BusBookingFormPage;
import com.tiket.page.bus.pdp.BuspdpPage;
import com.tiket.page.bus.search.BusSearchPage;
import com.tiket.page.bus.searchresultpage.BusSearchResultPage;
import com.tiket.page.carrental.bookingpage.CarRentalBookingPage;
import com.tiket.page.carrental.reviewbookingpage.CarRentalReviewBookingPage;
import com.tiket.page.carrental.searchpage.SearchPageCarRental;
import com.tiket.page.carrental.searchresultpage.SearchResultPageCarRental;
import com.tiket.page.cartransfer.bookingform.CarTransferBookingPage;
import com.tiket.page.cartransfer.searchpage.CarTransferSearchPage;
import com.tiket.page.cartransfer.searchresultpage.CarTransferSearchResultPage;
import com.tiket.page.common.customerCareDashboard.CustomerCareDashboard;
import com.tiket.page.common.webview.WebViewPage;
import com.tiket.page.corporate.login.CorporateLoginPage;
import com.tiket.page.ferry.FerryBookingPage.FerryBookingPage;
import com.tiket.page.ferry.FerryPage;
import com.tiket.page.ferry.FerrySearchPage.FerrySearchPage;
import com.tiket.page.ferry.FerrySearchresultPage.FerrySearchResultPage;
import com.tiket.page.flight.FlightPage;
import com.tiket.page.flight.bookingform.FlightBookingFormPage;
import com.tiket.page.flight.prp.FlightProductReviewPage;
import com.tiket.page.flight.reschedulepage.FlightReschedulePage;
import com.tiket.page.flight.searchpage.FlightSearchPage;
import com.tiket.page.flight.searchresultpage.FlightSearchResultPage;
import com.tiket.page.home.HomePage;
import com.tiket.page.login.LoginPage;
import com.tiket.page.order.orderdetail.OrderDetailPage;
import com.tiket.page.order.orderlist.OrderListPage;
import com.tiket.page.order.trip.switcher.SwitcherPage;
import com.tiket.page.order.trip.TripPage;
import com.tiket.page.payment.PaymentPage;
import com.tiket.page.refund.RefundPage;
import com.tiket.page.setting.SettingPage;
import com.tiket.page.train.bookingform.TrainBookingPage;
import com.tiket.page.train.searchpage.TrainSearchPage;
import com.tiket.page.train.searchresultpage.TrainSearchResultPage;
import com.tiket.page.ttd.landingpage.TTDLandingPage;
import com.tiket.page.ttd.searchpage.TTDSearchPage;
import com.tiket.page.whoosh.bookingform.WhooshBookingPage;
import com.tiket.page.whoosh.searchpage.WhooshSearchPage;
import com.tiket.page.whoosh.searchresultpage.WhooshSearchResultPage;
import com.tiket.pojo.accommodation.hotel.ChooseFirstRoomData;
import com.tiket.pojo.accommodation.hotel.HotelAutoCompleteResponse;
import com.tiket.pojo.b2b.corporate.*;
import com.tiket.pojo.customerCareDashboard.OrderDetailResponse;
import com.tiket.pojo.flight.FlightDynamicData;
import com.tiket.pojo.flight.PriceDetail;
import com.tiket.pojo.unm.LoginDataUnmResponse;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.testbase.Configs;
import com.tiket.web.page.accommodation.hotels.reschedulePage.HotelReschedulePageWeb;
import com.tiket.web.page.corporate.travel_plan.TravelPlanWeb;
import com.tiket.web.page.nft.airport_transfer.booking_page.AirportTransferBookingPageWeb;
import com.tiket.web.page.nft.airport_transfer.landing_page.AirportTransferLandingPageWeb;
import com.tiket.web.page.nft.airport_transfer.pdp.AirportTransferPDPWeb;
import com.tiket.web.page.nft.airport_transfer.srp.AirportTransferSRPWeb;
import com.tiket.web.page.ttd.TtdBookingFormPage.TtdBookingFormPageWeb;
import com.tiket.web.page.ttd.TtdProductDetailPage.TtdProductDetailPageWeb;
import com.tiket.web.page.ttd.TtdSelectedPackageDetailPage.TtdSelectedPackageDetailPageWeb;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import java.util.*;
import java.util.concurrent.ExecutionException;

import static com.tiket.core.CoreFunctions.*;
import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class B2BTest extends BaseTest {

    @Author(names = {"Yazid"})
    @Module(name = "corporateCodeBooking")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void flightBookingWithCorporateDealApplied() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("flightBookingWithCorporateDealApplied");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        FlightPage<WebDriver> flightPage = FlightPage.getInstance(session);
        CorporateLoginPage<WebDriver> corporateLoginPage = CorporateLoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightProductReviewPage<WebDriver> flightProductReviewPage = FlightProductReviewPage.getInstance(session);
        FlightBookingFormPage<WebDriver> flightBookingFormPage = FlightBookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);
        TravelPlanWeb<?> travelPlanWeb = TravelPlanWeb.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);

        String username = param.get("username");
        String password = param.get("password");
        String currency = param.get("currency");
        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");

        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate,daysToDepartureDate);

        ArrayList<CorporateCodeData> corporateCodeData = corporateApi.setAndGetCorporateCodeData(param.get("corporateCodeData"));
        ArrayList<String> airline = corporateApi.getAirlineName(corporateCodeData);

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginAsGuest();
        corporateLoginPage.corporateLogin(username, password);

        step("Change currency");
        homePage.changeCurrency(currency,username);

        step("Open flights page");
        homePage.openVerticalPageDeeplink("Flights","",currency);
        flightSearchPage.closePopup();

        step("change date using deeplink");
        flightSearchPage.changeDateViaDeeplink(daysToDepartureDate,tripType);

        step("Toggle trip type { " + tripType + " }");
        flightSearchPage.toggleTriptype(tripType);

        step("Select origin airport");
        flightSearchPage.selectOrigin(originAirport);
        flightSearchPage.closePopup();

        step("Select destination airport");
        flightSearchPage.selectDestination(destinationAirport);

        step("Select date");
        flightSearchPage.selectDate(departureDate);

        step("Add passenger");
        flightSearchPage.addPassenger(adultPassengers,childPassengers,infantPassengers);

        step("Search flight");
        flightSearchPage.searchFlight();

        step("Open filter");
        flightSearchResultPage.openFilter();

        step("Filter airline");
        flightSearchResultPage.filterAirline(airline);

        step("Save filter");
        flightSearchResultPage.saveFilter();
        Assertion.assertThat("Coporate deals benefit on the card is present", flightSearchResultPage.gotoCorporateDealsBenefitCard(), is(true));

        step("Select flight with Corporate Deals");
        flightSearchResultPage.selectFlightCorporateDeals();
        Assertion.assertThat("Corporate deals banner is present", flightProductReviewPage.isCorporateDealsPresent(), is(true));

        step("Continue to booking form");
        flightProductReviewPage.continueToBookingForm(tripType);

        step("Fill passenger detail");
        flightBookingFormPage.fillPassengerDetail(adultPassengers,childPassengers,infantPassengers, "domestic",airline.get(0));

        step("Continue to second half booking form");
        flightBookingFormPage.continueToSecondHalf();

        step("Continue to payment");
        flightBookingFormPage.continueToPayment();

        step("Skip full protection");
        flightBookingFormPage.skipFullProtection();
        webViewPage.switchToWebViewUrl("corporate/interim");
        travelPlanWeb.skipTravelPlan();
        webViewPage.switchToNative();
        Assertion.assertThat("Successfully book the flight with corporate deals", paymentPage.checkOrderID(), notNullValue());
    }

    @Author(names = {"Yazid"})
    @Module(name = "corporateCodeBooking")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void flightBookingWithCorporateDealNotApplied() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("flightBookingWithCorporateDealNotApplied");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        FlightPage<WebDriver> flightPage = FlightPage.getInstance(session);
        CorporateLoginPage<WebDriver> corporateLoginPage = CorporateLoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightProductReviewPage<WebDriver> flightProductReviewPage = FlightProductReviewPage.getInstance(session);
        FlightBookingFormPage<WebDriver> flightBookingFormPage = FlightBookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);
        TravelPlanWeb<?> travelPlanWeb = TravelPlanWeb.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);

        String username = param.get("username");
        String password = param.get("password");
        String currency = param.get("currency");
        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");

        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate,daysToDepartureDate);
        String airline = param.get("airline");

        //steps for gk and pre-prod only
        if (Configs.ENVIRONMENT == Environment.GK || Configs.ENVIRONMENT == Environment.PREPROD) {
            step("Delete the corporate codes for both airlines");
            boolean isIdDeleted = corporateApi.deleteAllCorporateCode(username, password);
            Assertion.assertThat("Success to deleted the corporate code", isIdDeleted, is(true));
        }

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginAsGuest();
        corporateLoginPage.corporateLogin(username, password);

        step("Change currency");
        homePage.changeCurrency(currency,username);

        step("Open flights page");
        homePage.openVerticalPageDeeplink("Flights","",currency);
        flightSearchPage.closePopup();

        step("change date using deeplink");
        flightSearchPage.changeDateViaDeeplink(daysToDepartureDate,tripType);

        step("Toggle trip type { " + tripType + " }");
        flightSearchPage.toggleTriptype(tripType);

        step("Select origin airport");
        flightSearchPage.selectOrigin(originAirport);
        flightSearchPage.closePopup();

        step("Select destination airport");
        flightSearchPage.selectDestination(destinationAirport);

        step("Select date");
        flightSearchPage.selectDate(departureDate);

        step("Add passenger");
        flightSearchPage.addPassenger(adultPassengers,childPassengers,infantPassengers);

        step("Search flight");
        flightSearchPage.searchFlight();

        step("Open filter");
        flightSearchResultPage.openFilter();

        step("Filter airline");
        flightSearchResultPage.filterAirline(airline);

        step("Save filter");
        flightSearchResultPage.saveFilter();
        Assertion.assertThat("Corporate deals benefit on the card is not present", flightSearchResultPage.gotoCorporateDealsBenefitCard(), is(false));

        step("Select flight without Corporate Deals");
        flightSearchResultPage.selectFlight();
        Assertion.assertThat("Corporate deals banner is not present", flightProductReviewPage.isCorporateDealsPresent(), is(false));

        step("Continue to booking form");
        flightProductReviewPage.continueToBookingForm(tripType);

        step("Fill passenger detail");
        flightBookingFormPage.fillPassengerDetail(adultPassengers,childPassengers,infantPassengers, "domestic",airline);

        step("Continue to second half booking form");
        flightBookingFormPage.continueToSecondHalf();

        step("Continue to payment");
        flightBookingFormPage.continueToPayment();

        step("Skip full protection");
        webViewPage.switchToWebViewUrl("corporate/interim");
        travelPlanWeb.skipTravelPlan();
        webViewPage.switchToNative();
        flightBookingFormPage.skipFullProtection();
        webViewPage.switchToWebViewUrl("corporate/interim");
        travelPlanWeb.skipTravelPlan();
        webViewPage.switchToNative();
        Assertion.assertThat("Successfully book the flight without corporate deals", paymentPage.checkOrderID(), notNullValue());
    }

    @Author(names = {"Yazid"})
    @Module(name = "Booking")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void hotelBookingFromTravelerToApprover() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("hotelBookingFromTravelerToApprover");
        BaseTest.paramsThreadLocal.set(param);

        String hotelName = param.get("hotelName");
        String travelerUsername = param.get("travelerUsername");
        String travelerPassword = param.get("travelerPassword");
        String approverUsername = param.get("approverUsername");
        String approverPassword = param.get("approverPassword");

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        CorporateLoginPage<WebDriver> corporateLoginPage = CorporateLoginPage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        TravelPlanWeb<?> travelPlanWeb = TravelPlanWeb.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("User open app as guest");
        loginPage.loginAsGuest();

        step("Change currency");
        homePage.changeCurrency("IDR","");

        step("Open hotel landing page");
        homePage.openVerticalPageDeeplink("Hotel","", "IDR");

        step("Search stay location");
        landingPage.searchStayLocation(hotelName);

        step("Submit search");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        step("Go to the room list");
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();

        step("Select room from the room list");
        roomListPage.getTheFirstHotelRoomPrice();
        roomListPage.clickBookRoom();

        step("Login on the booking form");
        bookingFormPage.openLoginPage();
        corporateLoginPage.corporateLoginB2BViaDifferentPage(travelerUsername,travelerPassword);

        step("Submit the booking form");
        bookingFormPage.continueToPaymentPage();
        webViewPage.switchToWebViewUrl("corporate/interim");
        travelPlanWeb.skipTravelPlan();
        webViewPage.switchToNative();
        bookingFormPage.PriceChangesPopUp();

        step("Payment page should be displayed");
        String orderID = paymentPage.checkOrderID();
        orderID = String.valueOf(getOrderId(orderID));
        Assertion.assertThat("Corporate billing option is selected", paymentPage.getSelectedPayment().toLowerCase(), is("corporate billing"));

        step("Submit payment using Corporate Billing");
        paymentPage.clickPayButtonWithoutWaitForPreviousBookingPopup();

        step("Approve booking by approver account for Order ID: " + orderID);
        Boolean bookingStatus = corporateApi.approveBookingB2BApi(approverUsername, approverPassword, orderID);
        Assertion.assertThat("Booking approved by B2B dashboard", bookingStatus, is(true));

        step("Open the order list and go to order detail");
        paymentPage.SeeOrderListAfterPaymentSuccess();;
        orderListPage.openIssuedTicketViaDeeplink(orderID);
        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.getETicketLabel(), either(is("Your e-ticket is here")).or(is("E-tiketmu di sini")));
    }

    @Author(names = {"Yazid"})
    @Module(name = "Login")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void loginLinkAvailable() {
        //deprecated case
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> testProperties = getParam("loginLinkAvailable");
        BaseTest.paramsThreadLocal.set(testProperties);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);

        step("User open app");
        homePage.gotoHome();
        loginPage.loginAsGuest();

        step("User open the login page");
        loginPage.openLoginPage();
        Assertion.assertThat("Verify that B2B login link is available at the bottom of B2C login page", loginPage.isCorporateLoginLinkVisible(), is(true));
    }

    @Author(names = {"Yazid"})
    @Module(name = "Login")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void loginPageRedirection() {
        //deprecated case
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> testProperties = getParam("loginPageRedirection");
        BaseTest.paramsThreadLocal.set(testProperties);
        String email = testProperties.get("email");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        CorporateLoginPage<WebDriver> corporateLoginPage = CorporateLoginPage.getInstance(session);

        step("User open app");
        homePage.gotoHome();
        loginPage.loginAsGuest();

        step("User open the B2C login page");
        loginPage.openLoginPage();

        step("User submit the B2B email address");
        loginPage.inputEmailOrPhoneNumber(email);

        step("User click B2B continue button");
        loginPage.clickB2BContinueButton();
        Assertion.assertThat("Verify that user should redirect to B2B login page", corporateLoginPage.isCorporateLoginPageVisible(), is(true));
        Assertion.assertThat("Email value should be there", email, is(corporateLoginPage.getEmailValue()));
    }

    @Author(names = {"Yazid"})
    @Module(name = "Login")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void languageValidation() {
        //deprecated case
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> testProperties = getParam("languageValidation");
        BaseTest.paramsThreadLocal.set(testProperties);
        String email = testProperties.get("email");
        String language = testProperties.get("language").toUpperCase();
        String expectedChangeEmailText = testProperties.get("changeEmailText").toLowerCase();
        String expectedContinueText = testProperties.get("continueText").toLowerCase();

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        SettingPage<WebDriver> settingPage = SettingPage.getInstance(session);

        step("User open app");
        homePage.gotoHome();
        loginPage.loginAsGuest();

        step("User change the language to " + language);
        homePage.openAccount();
        accountPage.selectLanguage(language);
        settingPage.backToHome();

        step("User open the B2C login page");
        loginPage.openLoginPage();

        step("User submit the B2B email address");
        loginPage.inputEmailOrPhoneNumber(email);
        Assertion.assertThat("\"Change Email\" button text should be shown as expected",
                loginPage.getChangeEmailButtonText().replace(",", "").toLowerCase(), is(expectedChangeEmailText));
        Assertion.assertThat("\"B2B Continue\" button text should be shown as expected",
                loginPage.getB2BContinueButtonText().replace(",", "").toLowerCase(), is(expectedContinueText));
    }

    @Author(names = {"Yazid"})
    @Module(name = "Login")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void loginPageValidation() {
        //deprecated case
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> testProperties = getParam("loginPageValidation");
        BaseTest.paramsThreadLocal.set(testProperties);
        String email = testProperties.get("email");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);

        step("User open app");
        homePage.gotoHome();
        loginPage.loginAsGuest();

        step("User open the B2C login page");
        loginPage.openLoginPage();

        step("User submit the B2B email address");
        loginPage.inputEmailOrPhoneNumber(email);
        Assertion.assertThat("\"Change Email\" button is visible", loginPage.isChangeEmailButtonVisible(), is(true));
        Assertion.assertThat("\"B2B Continue\" button is visible", loginPage.isB2BContinueButtonVisible(), is(true));
    }

    @Author(names = {"Yazid"})
    @Module(name = "Login")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void unregisteredEmail() {
        //deprecated case
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> testProperties = getParam("unregisteredEmail");
        BaseTest.paramsThreadLocal.set(testProperties);
        String email = testProperties.get("email");
        String password = testProperties.get("password");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        CorporateLoginPage<WebDriver> corporateLoginPage = CorporateLoginPage.getInstance(session);

        step("User open app");
        homePage.gotoHome();
        loginPage.loginAsGuest();

        step("User open the B2B login page");
        loginPage.openLoginPage();
        loginPage.clickCorporateLoginLink();

        step("User submit B2C account that has not registered as a B2B account");
        corporateLoginPage.inputEmail(email);
        corporateLoginPage.inputPassword(password);
        corporateLoginPage.clickContinueButton();
        Assertion.assertThat("User should get validation message that this email is not registered", corporateLoginPage.isUnregisteredMessageVisible(), is(true));
    }

    @Author(names = {"Akbar"})
    @Module(name = "Login")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void testCorpLoginTraveler() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> testProperties = getParam("testCorpLoginTraveler");
        BaseTest.paramsThreadLocal.set(testProperties);

        String email = testProperties.get("email");
        String password = testProperties.get("password");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        CorporateLoginPage<WebDriver> corporateLoginPage = CorporateLoginPage.getInstance(session);

        step("User open app");
        homePage.gotoHome();
        loginPage.loginAsGuest();

        step("Verify the corp login page with traveler credentials " + email);
        corporateLoginPage.corporateLogin(email,password);

        step("Verify Step");
        Assertion.assertThat("User is on login state", homePage.isUserLogin(), is(true));
    }

    @Author(names = {"Akbar"})
    @Module(name = "Login")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void testCorpLoginValidAdmin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> testProperties = getParam("testCorpLoginValidAdmin");
        BaseTest.paramsThreadLocal.set(testProperties);

        String email = testProperties.get("email");
        String password = testProperties.get("password");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        CorporateLoginPage<WebDriver> corporateLoginPage = CorporateLoginPage.getInstance(session);

        step("User open app");
        homePage.gotoHome();
        loginPage.loginAsGuest();

        step("Verify the corp login page with valid admin credentials " + email);
        corporateLoginPage.corporateLogin(email,password);

        step("Verify Step");
        Assertion.assertThat("User is on login state", homePage.isUserLogin(), is(true));
    }

    @Author(names = {"Akbar"})
    @Module(name = "Login")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void testCorpLoginApprover() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> testProperties = getParam("testCorpLoginApprover");
        BaseTest.paramsThreadLocal.set(testProperties);

        String email = testProperties.get("email");
        String password = testProperties.get("password");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        CorporateLoginPage<WebDriver> corporateLoginPage = CorporateLoginPage.getInstance(session);

        step("User open app");
        homePage.gotoHome();
        loginPage.loginAsGuest();

        step("Verify the corp login with valid approver credentials " + email);
        corporateLoginPage.corporateLogin(email,password);

        step("Verify Step");
        Assertion.assertThat("User is on login state", homePage.isUserLogin(), is(true));
    }

    @Author(names = {"Akbar"})
    @Module(name = "Login")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void testLoginOnTrainBookingFormTraveler(){
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testLoginOnTrainBookingFormTraveler");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        CorporateLoginPage<WebDriver> corporateLoginPage = CorporateLoginPage.getInstance(session);
        TrainSearchPage<WebDriver> trainSearchPage = TrainSearchPage.getInstance(session);
        TrainSearchResultPage<WebDriver> trainSearchResultPage = TrainSearchResultPage.getInstance(session);
        TrainBookingPage<WebDriver> trainBookingPage = TrainBookingPage.getInstance(session);

        String tripType = param.get("tripType");
        String currency = param.get("currency");
        String originStation = param.get("originStation");
        String destinationStation = param.get("destinationStation");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int infantPassengers = Integer.parseInt(param.get("infant"));

        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate,daysToDepartureDate);

        String username = param.get("username");
        String password = param.get("password");

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginAsGuest();

        step("Change currency");
        homePage.changeCurrency(currency,"");

        step("Open train page");
        homePage.openVerticalPageDeeplink("Trains","",currency);

        step("Select origin station");
        trainSearchPage.skipPopup();
        trainSearchPage.selectOrigin(originStation);

        step("Select destination station");
        trainSearchPage.selectDestination(destinationStation);

        step("Toggle roundtrip");
        trainSearchPage.toggleTripType(tripType);

        step("Select date");
        trainSearchPage.selectDate(departureDate);

        step("Add passenger");
        trainSearchPage.addPassenger(adultPassengers,infantPassengers);

        step("Search train");
        trainSearchPage.searchTrain();

        step("Skip Popup");
        trainSearchPage.skipPopup();

        step("Select departure train");
        trainSearchResultPage.selectTrain();

        step("Click login banner");
        trainBookingPage.openLoginPage();

        step("Fill login credentials");
        corporateLoginPage.corporateLoginB2BViaDifferentPage(username,password);

        step("Verify Step");
        Assertion.assertThat("Login successful",trainBookingPage.isLoggedIn(),is(true));
    }

    @Author(names = {"Akbar"})
    @Module(name = "Login")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void testLoginOnFlightBookingFormAdmin(){
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testLoginOnFlightBookingFormAdmin");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        CorporateLoginPage<WebDriver> corporateLoginPage = CorporateLoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightProductReviewPage<WebDriver> flightProductReviewPage = FlightProductReviewPage.getInstance(session);
        FlightBookingFormPage<WebDriver> flightBookingFormPage = FlightBookingFormPage.getInstance(session);
        FlightPage<WebDriver> flightPage = FlightPage.getInstance(session);

        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));

        String username = param.get("username");
        String password = param.get("password");
        String currency = param.get("currency");

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Find dynamic data");
        flightPage.setDataOneWay(
                daysToDepartureDate,""
        );
        String departureDate =CoreFunctions.convertToLongDate(flightPage.getDate("departureDate"));

        step("logout");
        loginPage.loginAsGuest();

        step("Change currency");
        homePage.changeCurrency(currency,"");

        step("Open flights page");
        homePage.openVerticalPageDeeplink("Flights","",currency);
        flightSearchPage.closePopup();

        step("change date using deeplink");
        flightSearchPage.changeDateViaDeeplink(daysToDepartureDate,tripType);

        step("Toggle trip type { " + tripType + " }");
        flightSearchPage.toggleTriptype(tripType);

        step("Select origin airport");
        flightSearchPage.selectOrigin(originAirport);
        flightSearchPage.closePopup();

        step("Select destination airport");
        flightSearchPage.selectDestination(destinationAirport);

        step("Select date");
        flightSearchPage.selectDate(departureDate);

        step("Add passenger");
        flightSearchPage.addPassenger(adultPassengers,childPassengers,infantPassengers);

        step("Search flight");
        flightSearchPage.searchFlight();

        step("Click flight detail chevron");
        flightSearchResultPage.selectFlight();

        step("Continue to booking form");
        flightProductReviewPage.continueToBookingForm(tripType);

        step("Click login banner");
        flightBookingFormPage.openLoginPage();

        step("corporate login");
        corporateLoginPage.corporateLoginB2BViaDifferentPage(username,password);

        step("Verify Step");
        Assertion.assertThat("Login successful",flightBookingFormPage.isLoggedIn(),is(true));
    }

    @Author(names = {"Akbar"})
    @Module(name = "Login")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void testLoginOnHotelBookingFormApprover(){
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testLoginOnHotelBookingFormApprover");
        BaseTest.paramsThreadLocal.set(param);

        String location = param.get("location");

        String username = param.get("username");
        String password = param.get("password");
        String currency = param.get("currency");

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        CorporateLoginPage<WebDriver> corporateLoginPage = CorporateLoginPage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("User open app as guest");
        loginPage.loginAsGuest();

        step("Change currency");
        homePage.changeCurrency(currency,"");

        step("Open hotel landing page");
        homePage.openVerticalPageDeeplink("Hotel","",currency);

        step("Search stay location");
        landingPage.searchStayLocation(location);

        step("set total room");
        landingPage.clickRoomAndGuestButton();
        landingPage.setTotalRoom(1);
        landingPage.clickDone();

        step("Submit search");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        step("Select hotel from the hotel list");
        searchResultPage.selectFirstHotelCard();

        step("Go to the room list");
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();

        step("Select room from the room list");
        roomListPage.getTheFirstHotelRoomPrice();
        roomListPage.clickBookRoom();

        step("Get the booking form data");
        bookingFormPage.openLoginPage();

        step("Fill login credentials");
        corporateLoginPage.corporateLoginB2BViaDifferentPage(username,password);

        step("Verify Step");
        Assertion.assertThat("Login successful",bookingFormPage.isLoggedIn(),is(true));
    }

    @Author(names = {"Ashish"})
    @Module(name = "Booking")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void testLoginTravelerApproveBookingByAdminApi(){
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testLoginTravelerApproveBookingByAdminApi");
        BaseTest.paramsThreadLocal.set(param);

        String username = param.get("username");
        String password = param.get("password");
        String hotelName = param.get("hotelName");
        String adminEmail = param.get("adminEmail");
        String adminPassword = param.get("adminPassword");

        String rangeAddDate = param.getOrDefault("rangeAddDate", "20-30");
        int totalNight = Integer.parseInt(param.getOrDefault("night", "1"));
        int totalRoom = Integer.parseInt(param.getOrDefault("totalRoom", "1"));
        int totalGuest = Integer.parseInt(param.getOrDefault("totalGuest", "1"));
        String accommodationType = "hotel";

        String[] dates = generateCheckInOutDate(rangeAddDate, totalNight);
        String checkInDate = dates[0];
        String checkOutDate = dates[1];

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        CorporateLoginPage<WebDriver> corporateLoginPage = CorporateLoginPage.getInstance(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        TravelPlanWeb<?> travelPlanWeb = TravelPlanWeb.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        HotelPage<WebDriver> hotelPage = HotelPage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);

        step("User open app");
        homePage.gotoHome();
        loginPage.loginAsGuest();

        step("B2B login");
        corporateLoginPage.corporateLogin(username,password);

        step("Verify Step");
        Assertion.assertThat("User is on login state", homePage.isUserLogin(), is(true));

        step("Change currency");
        homePage.changeCurrency("IDR",username);

        step("user open PDP via deeplink");
        HotelAutoCompleteResponse hotelAutoCompleteResponse = hotelApi.getAutocompleteData(username, password, hotelName, accommodationType.toUpperCase());
        int selectedIndex = hotelApi.findHotelIndex(hotelAutoCompleteResponse, hotelName);
        String hotelId = hotelAutoCompleteResponse.data.contents.get(selectedIndex).publicId;
        String country = hotelAutoCompleteResponse.data.contents.get(selectedIndex).country.name;
        hotelPage.openPDPDeeplink(accommodationType.toLowerCase(), country.toLowerCase(), hotelId, totalRoom, totalGuest, checkInDate, checkOutDate);

        step("Go to the room list");
        detailPage.RemovePopUp();
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();

        step("Select room from the room list");
        detailPage.RemovePopUp();
        detailPage.clickBookButton100pRefund(1);

        if(Configs.ENVIRONMENT == Environment.PROD && detailPage.isMeetTravelPolicy()){
            //because of travel policy getting this
            step("click yes continue");
            detailPage.clickYesContinue();
        }

        step("Submit booking");
        bookingFormPage.cancelRatePLanBottomSheetBookingPart1();
        bookingFormPage.continueToPaymentPage();
        webViewPage.switchToWebViewUrl("corporate/interim");
        travelPlanWeb.skipTravelPlan();
        webViewPage.switchToNative();
        bookingFormPage.PriceChangesPopUp();

        step("Payment page should be displayed");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("Order id should be displayed", !orderID.isEmpty(), is(true));

        step("Click pay with corporate billing");
        paymentPage.clickPayButtonWithoutWaitForPreviousBookingPopup();

        if(Configs.ENVIRONMENT == Environment.PROD && detailPage.isMeetTravelPolicy()){
            //because of travel policy getting this
            step("click yes continue");
            paymentPage.clickYesContinue();
        }

        step("verify successfully placed order popup from corporate");
        Assertion.assertThat("get successfully placed order popup",paymentPage.isCorporateSuccessfullyPlacedOrderPopup(),is(true));

        //steps for gk and pre-prod only
        if (Configs.ENVIRONMENT == Environment.GK || Configs.ENVIRONMENT == Environment.PREPROD) {
            step("Approve Booking By Dashboard Api" + orderID);
            orderID = CoreFunctions.getOrderId(orderID).toString();
            Boolean bookingStatus = corporateApi.approveBookingB2BApi(adminEmail, adminPassword, orderID);
            Assertion.assertThat("Booking approved by B2B dashboard", bookingStatus, is(true));

            step("open order page");
            paymentPage.SeeOrderListAfterPaymentSuccess();;

            step("check order status");
            orderListPage.openIssuedTicketViaDeeplink(orderID);
            Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.getETicketLabel(), either(is("Your e-ticket is here")).or(is("E-tiketmu di sini")));
        }

        //steps for logout only
        if (Configs.ENVIRONMENT == Environment.PROD) {
            step("Wait for app to launch");
            homePage.gotoHome();

            step("logout");
            loginPage.loginAsGuest();
        }
    }

    @Author(names = {"Kartik"})
    @Vertical(name = "B2B")
    @Tribe(name = "Corporate")
    @Module(name = "Booking")
    @Test
    public void testUserCannotBookWithNonIDRCorporate() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("testUserCannotBookWithNonIDRCorporate");
        BaseTest.paramsThreadLocal.set(map);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        TravelPlanWeb<?> travelPlanWeb = TravelPlanWeb.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);

        String location = map.get("location");
        String email = map.get("email");
        String password = map.get("password");
        String currency = map.get("currency");

        step("Goto home");
        homePage.gotoHome();

        step("Login as B2B account " + email + " via deeplink and change currency to " + currency);
        homePage.loginWithSpecificAccountViaDeeplink(email, password, currency);

        step("Verify Step");
        Assertion.assertThat("User is on login state", homePage.isUserLogin(), is(true));

        step("Open hotel landing page");
        homePage.openVerticalPageDeeplink("Hotel","",currency);

        step("Search stay location");
        landingPage.searchStayLocation(location);

        step("set total room");
        landingPage.clickRoomAndGuestButton();
        landingPage.setTotalRoom(1);
        landingPage.clickDone();

        step("Submit search");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        step("Select hotel from SRP");
        searchResultPage.selectFirstHotelCard();

        step("Scroll down to room list");
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();

        step("Open hotel booking form page");
        roomListPage.openRoomDetailPage();
        detailPage.clickBookButton(1);
        bookingFormPage.cancelRatePLanBottomSheetBookingPart1();
        if(Configs.ENVIRONMENT == Environment.PROD && detailPage.isMeetTravelPolicy()){
            //because of travel policy getting this
            step("click yes continue");
            detailPage.clickYesContinue();
        }

        step("Submit booking");
        bookingFormPage.continueToPaymentPage();
        webViewPage.switchToWebViewUrl("corporate/interim");
        travelPlanWeb.skipTravelPlan();
        webViewPage.switchToNative();
        bookingFormPage.PriceChangesPopUp();

        step("Verify Assertion");
        Boolean priceOnPaymentPage = paymentPage.corporateNonIdrBookingPopUP();
        Assertion.assertThat("verify Can not book pop up appeared ",priceOnPaymentPage,is(true));
    }

    @Author(names = {"kartik"})
    @Module(name = "Booking")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void testCannotBookMultiOrderFromCorporateAccount() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("testCannotBookMultiOrderFromCorporateAccount");
        BaseTest.paramsThreadLocal.set(map);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);

        String hotelName = map.get("hotelName");
        String email = map.get("email");
        int totalRoom = Integer.parseInt(map.get("totalRoom"));

        step("Goto home");
        homePage.gotoHome();

        step("Login as Guest");
        loginPage.loginAsGuest();

        step("Change currency to IDR");
        homePage.changeCurrency("IDR","");

        step("Open hotel landing page");
        homePage.openVerticalPageDeeplink("Hotel","","");

        step("Search stay hotel");
        landingPage.searchStayLocation(hotelName);

        step("set total room");
        landingPage.clickRoomAndGuestButton();
        landingPage.setTotalRoom(totalRoom);
        landingPage.clickDone();

        step("Submit search");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        step("Scroll down to room list");
        detailPage.RemovePopUp();
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();

        step("Open hotel booking form page");
        detailPage.selectRoomMultiOrder(totalRoom);

        step("Completing booking form data");
        bookingFormPage.fillContactOrderDetail(email);
        bookingFormPage.fillGuestDetail(false);

        step("Submit Booking");
        bookingFormPage.continueToPaymentPage();
        bookingFormPage.PriceChangesPopUp();

        step("Verify Assertion login in and book 1 room type");
        Assertion.assertThat("login in and book 1 room type ",bookingFormPage.canNotBookMultiOrderCorporatePopUp(),is(true));
    }


    @Author(names = {"kartik"})
    @Module(name = "Booking")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void testCannotBookMultiOrderFromTravelerAccount() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("testCannotBookMultiOrderFromTravelerAccount");
        BaseTest.paramsThreadLocal.set(map);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);
        CorporateLoginPage<WebDriver> corporateLoginPage = CorporateLoginPage.getInstance(session);

        String hotelName = map.get("hotelName");
        String email = map.get("email");
        String password = map.get("password");
        int totalRoom = Integer.parseInt(map.get("totalRoom"));

        step("Goto home");
        homePage.gotoHome();

        step("Login as Guest");
        loginPage.loginAsGuest();

        step("Change currency to IDR");
        homePage.changeCurrency("IDR","");

        step("Open hotel landing page");
        homePage.openVerticalPageDeeplink("Hotel","","");

        step("Search stay hotel");
        landingPage.searchStayLocation(hotelName);

        step("set total room");
        landingPage.clickRoomAndGuestButton();
        landingPage.setTotalRoom(totalRoom);
        landingPage.clickDone();

        step("Submit search");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        step("Scroll down to room list");
        detailPage.RemovePopUp();
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();

        step("Open hotel booking form page");
        detailPage.selectRoomMultiOrder(totalRoom);

        step("Click login banner");
        bookingFormPage.openLoginPage();

        step("B2B login");
        corporateLoginPage.corporateLoginB2BViaDifferentPage(email,password);

        step("Verify Assertion");
        Assertion.assertThat("verify booking does not apply to corporate accounts Banner appeared ",bookingFormPage.canNotBookMultiOrderTravellerPopUp(),is(true));
    }

    @Author(names = {"kartik"})
    @Module(name = "Booking")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void testCannotBookMultiOrderFromAdminAccount() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("testCannotBookMultiOrderFromAdminAccount");
        BaseTest.paramsThreadLocal.set(map);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);
        CorporateLoginPage<WebDriver> corporateLoginPage = CorporateLoginPage.getInstance(session);

        String hotelName = map.get("hotelName");
        String email = map.get("email");
        String password = map.get("password");
        int totalRoom = Integer.parseInt(map.get("totalRoom"));

        step("Goto home");
        homePage.gotoHome();

        step("Login as Guest");
        loginPage.loginAsGuest();

        step("Change currency to IDR");
        homePage.changeCurrency("IDR","");

        step("Open hotel landing page");
        homePage.openVerticalPageDeeplink("Hotel","","");

        step("Search stay hotel");
        landingPage.searchStayLocation(hotelName);

        step("set total room");
        landingPage.clickRoomAndGuestButton();
        landingPage.setTotalRoom(totalRoom);
        landingPage.clickDone();

        step("Submit search");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        step("Scroll down to room list");
        detailPage.RemovePopUp();
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();

        step("Open hotel booking form page");
        detailPage.selectRoomMultiOrder(totalRoom);

        step("Click login banner");
        bookingFormPage.openLoginPage();

        step("B2B login");
        corporateLoginPage.corporateLoginB2BViaDifferentPage(email,password);

        step("Verify Assertion");
        Assertion.assertThat("verify when using Admin Account 'booking does not apply to corporate accounts Banner' appeared ",bookingFormPage.canNotBookMultiOrderTravellerPopUp(),is(true));
    }

    @Author(names = {"kartik"})
    @Module(name = "Booking")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void testCannotBookMultiOrderFromApproverAccount() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("testCannotBookMultiOrderFromApproverAccount");
        BaseTest.paramsThreadLocal.set(map);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);
        CorporateLoginPage<WebDriver> corporateLoginPage = CorporateLoginPage.getInstance(session);

        String hotelName = map.get("hotelName");
        String email = map.get("email");
        String password = map.get("password");
        int totalRoom = Integer.parseInt(map.get("totalRoom"));

        step("Goto home");
        homePage.gotoHome();

        step("Login as Guest");
        loginPage.loginAsGuest();

        step("Change currency to IDR");
        homePage.changeCurrency("IDR","");

        step("Open hotel landing page");
        homePage.openVerticalPageDeeplink("Hotel","","");

        step("Search stay hotel");
        landingPage.searchStayLocation(hotelName);

        step("set total room");
        landingPage.clickRoomAndGuestButton();
        landingPage.setTotalRoom(totalRoom);
        landingPage.clickDone();

        step("Submit search");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        step("Scroll down to room list");
        detailPage.RemovePopUp();
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();

        step("Open hotel booking form page");
        detailPage.selectRoomMultiOrder(totalRoom);

        step("Click login banner");
        bookingFormPage.openLoginPage();

        step("B2B login");
        corporateLoginPage.corporateLoginB2BViaDifferentPage(email,password);

        step("Verify Assertion");
        Assertion.assertThat("verify when using Admin Account 'booking does not apply to corporate accounts Banner' appeared ",bookingFormPage.canNotBookMultiOrderTravellerPopUp(),is(true));
    }

    @Author(names = {"Akbar"})
    @Module(name = "Booking")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void specialRefundTTDOrderReflectOnExternalDashboard() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("specialRefundTTDOrderReflectOnExternalDashboard");
        BaseTest.paramsThreadLocal.set(map);

        String product = map.get("product");
        int ticketQty = Integer.parseInt(map.get("ticket_qty"));

        String username = map.get("username");
        String password = map.get("password");

        String approverUsername = map.get("approverUsername");
        String approverPassword = map.get("approverPassword");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        CorporateLoginPage<WebDriver> corporateLoginPage = CorporateLoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);
        TtdSelectedPackageDetailPageWeb<?> ttdSelectedPackageDetailPageWeb = TtdSelectedPackageDetailPageWeb.getInstance(session);
        TtdBookingFormPageWeb<?> ttdBookingFormPageWeb = TtdBookingFormPageWeb.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);
        CustomerCareDashboard<WebDriver> customerCareDashboard = CustomerCareDashboard.getInstance(session);

        step("Go to home page");
        homePage.gotoHome();

        step("Make user as guest");
        loginPage.loginAsGuest();

        step("B2B login");
        corporateLoginPage.corporateLogin(username,password);

        step("Change currency to 'IDR'");
        homePage.changeCurrency("IDR",username);

        step("Open TTD landing page");
        homePage.openVerticalPageDeeplink("To Do","","IDR");

        step("Search the product");
        ttdLandingPage.clickSearchBar();
        ttdSearchPage.inputSearchKeyword(product);

        step("select the activity");
        ttdSearchPage.selectRecommendedActivity(product);
        webViewPage.switchToWebViewUrl("to-do");
        ttdProductDetailPageWeb.getProductName();

        step("Select ticket");
        ttdProductDetailPageWeb.goToPackagesList();
        ttdProductDetailPageWeb.chooseTicket();

        step("Set total ticket");
        ttdSelectedPackageDetailPageWeb.fillAvailableSection();
        ttdSelectedPackageDetailPageWeb.setTicketQty(ticketQty, 0);

        step("Submit package detail");
        ttdSelectedPackageDetailPageWeb.clickBookButton();
        webViewPage.switchToNative();

        step("Completing booking form data");
        webViewPage.switchToWebViewUrl("to-do");
        ttdBookingFormPageWeb.fillContactOrderDetail(true);
        ttdBookingFormPageWeb.fillGuestDetail();
        int actualTicketPrice = (int) convertPrice(ttdBookingFormPageWeb.getPrice());

        step("Submit booking");
        ttdBookingFormPageWeb.continueToPaymentPage();
        ttdBookingFormPageWeb.checkPriceNotifChange();
        webViewPage.switchToNative();

        step("Payment page should be displayed");
        String orderID = paymentPage.checkOrderID();
        orderID = String.valueOf(getOrderId(orderID));
        Assertion.assertThat("Corporate billing option is selected", paymentPage.getSelectedPayment().toLowerCase(), is("corporate billing"));

        step("Submit payment using Corporate Billing");
        paymentPage.clickPayButtonWithoutWaitForPreviousBookingPopup();

        step("Approve booking by approver account for Order ID: " + orderID);
        Boolean bookingStatus = corporateApi.approveBookingB2BApi(approverUsername, approverPassword, orderID);
        Assertion.assertThat("Booking approved by B2B dashboard", bookingStatus, is(true));

        step("Search order by order id");
        OrderDetailResponse orderDetailResponse = customerCareDashboard.searchOrderByOrderID(orderID);
        String orderId = orderDetailResponse.data.searchOrder.data.searchData.get(0).orderId;
        String orderDetailId = orderDetailResponse.data.searchOrder.data.searchData.get(0).orderDetailId;

        step("Request special refund with 'customer_request' reason and amount of " + actualTicketPrice);
        boolean specialRefundRequestStatus = customerCareDashboard.requestSpecialRefund(orderId, orderDetailId, actualTicketPrice);
        Assertion.assertThat("Special refund request successful", specialRefundRequestStatus, is(true));

        step("Check if the refunded order is being reflected on corporate dashboard");
        boolean isRefundOrderReflected = corporateApi.checkRefundList(orderId, approverUsername, approverPassword);
        Assertion.assertThat("Refund order is reflected on B2B corporate dashboard", isRefundOrderReflected, is(true));
    }

    @Author(names = {"Ashish"})
    @Module(name = "Booking")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void testLoginBookingTrainViaTravelerApproveBookingByAdmin(){
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testLoginBookingTrainViaTravelerApproveBookingByAdmin");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TrainSearchPage<WebDriver> trainSearchPage = TrainSearchPage.getInstance(session);
        TrainBookingPage<WebDriver> trainBookingPage = TrainBookingPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        TravelPlanWeb<?> travelPlanWeb = TravelPlanWeb.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);

        String email = param.get("username");
        String password = param.get("password");
        String adminEmail = param.get("adminEmail");
        String adminPassword = param.get("adminPassword");

        String tripType = param.get("tripType");
        String currency = param.get("currency");
        String originStation = param.get("originStation");
        String destinationStation = param.get("destinationStation");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int infantPassengers = Integer.parseInt(param.get("infant"));

        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate,daysToDepartureDate);

        String[] departureDateArray = departureDate.split(" ");
        String date = departureDateArray[0];

        step("User open app");
        homePage.gotoHome();

        step("Ensure logged in as B2B account");
        homePage.loginWithSpecificAccountViaDeeplink(email, password,currency);

        step("Verify Step");
        Assertion.assertThat("User is on login state", homePage.isUserLogin(), is(true));

        step("Change currency");
        homePage.changeCurrency(currency,email);

        step("Open train page");
        homePage.openVerticalPageDeeplink("Trains","",currency);

        step("Select origin station");
        trainSearchPage.skipPopup();
        trainSearchPage.selectOrigin(originStation);

        step("Select destination station");
        trainSearchPage.selectDestination(destinationStation);

        step("Toggle oneway");
        trainSearchPage.toggleTripType(tripType);

        step("Select date");
        trainSearchPage.selectDate(departureDate);

        step("Add passenger");
        trainSearchPage.addPassenger(adultPassengers,infantPassengers);

        step("Search train");
        trainSearchPage.searchTrain();

        step("Skip Popup");
        trainSearchPage.skipPopup();

        step("Select departure train");
        trainSearchPage.selectTrainViaIndex(3);

        step("Fill passenger detail");
        trainBookingPage.fillPassengerDetail(adultPassengers,infantPassengers);

        step("Continue to 2nd half");
        trainBookingPage.continueTo2ndHalf();

        step("Continue to payment");
        trainBookingPage.continueToPayment();
        webViewPage.switchToWebViewUrl("corporate/interim");
        travelPlanWeb.skipTravelPlan();
        webViewPage.switchToNative();

        step("Payment page should be displayed");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("Order id should be displayed", !orderID.isEmpty(), is(true));

        step("Submit payment using Corporate Billing");
        paymentPage.clickPayButtonWithoutWaitForPreviousBookingPopup();

        step("verify successfully placed order popup from corporate");
        Assertion.assertThat("get successfully placed order popup",paymentPage.isCorporateSuccessfullyPlacedOrderPopup(),is(true));

        step("Approve Booking By Dashboard Api" + orderID);
        orderID = CoreFunctions.getOrderId(orderID).toString();
        Boolean bookingStatus = corporateApi.approveBookingB2BApi(adminEmail, adminPassword, orderID);
        Assertion.assertThat("Booking approved by B2B dashboard", bookingStatus, is(true));

        step("verify successful payment popup from corporate");
        Assertion.assertThat("get successfully placed order popup",paymentPage.isCorporatePaymentSuccessPopupDisplayed(),is(true));

        step("open order page");
        paymentPage.SeeOrderListAfterPaymentSuccess();

        step("check order status");
        orderListPage.openIssuedTicketViaDeeplink(orderID);
        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.getETicketLabel(), either(is("Your e-ticket is here")).or(is("E-tiketmu di sini")));
    }

    @Author(names = {"Ashish"})
    @Module(name = "Booking")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void testLoginBookingTrainViaTravelerApproveBookingByApprover(){
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testLoginBookingTrainViaTravelerApproveBookingByApprover");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TrainSearchPage<WebDriver> trainSearchPage = TrainSearchPage.getInstance(session);
        TrainBookingPage<WebDriver> trainBookingPage = TrainBookingPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);
        CorporateLoginPage<WebDriver> corporateLoginPage = CorporateLoginPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        TravelPlanWeb<?> travelPlanWeb = TravelPlanWeb.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);

        String email = param.get("username");
        String password = param.get("password");
        String approverEmail = param.get("approverEmail");
        String approverPassword = param.get("approverPassword");

        String tripType = param.get("tripType");
        String currency = param.get("currency");
        String originStation = param.get("originStation");
        String destinationStation = param.get("destinationStation");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int infantPassengers = Integer.parseInt(param.get("infant"));

        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate,daysToDepartureDate);

        String[] departureDateArray = departureDate.split(" ");
        String date = departureDateArray[0];

        step("User open app");
        homePage.gotoHome();
        loginPage.loginAsGuest();

        step("B2B login");
        corporateLoginPage.corporateLogin(email,password);

        step("Verify Step");
        Assertion.assertThat("User is on login state", homePage.isUserLogin(), is(true));

        step("Change currency");
        homePage.changeCurrency(currency,email);

        step("Open train page");
        homePage.openVerticalPageDeeplink("Trains","",currency);

        step("Select origin station");
        trainSearchPage.skipPopup();
        trainSearchPage.selectOrigin(originStation);

        step("Select destination station");
        trainSearchPage.selectDestination(destinationStation);

        step("Toggle oneway");
        trainSearchPage.toggleTripType(tripType);

        step("Select date");
        trainSearchPage.selectDate(departureDate);

        step("Add passenger");
        trainSearchPage.addPassenger(adultPassengers,infantPassengers);

        step("Search train");
        trainSearchPage.searchTrain();

        step("Skip Popup");
        trainSearchPage.skipPopup();

        step("Select departure train");
        trainSearchPage.selectTrainViaIndex(3);

        step("Fill passenger detail");
        trainBookingPage.fillPassengerDetail(adultPassengers,infantPassengers);

        step("Continue to 2nd half");
        trainBookingPage.continueTo2ndHalf();

        step("Continue to payment");
        trainBookingPage.continueToPayment();
        webViewPage.switchToWebViewUrl("corporate/interim");
        travelPlanWeb.skipTravelPlan();
        webViewPage.switchToNative();

        step("Payment page should be displayed");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("Order id should be displayed", !orderID.isEmpty(), is(true));

        step("Submit payment using Corporate Billing");
        paymentPage.clickPayButtonWithoutWaitForPreviousBookingPopup();

        step("verify successfully placed order popup from corporate");
        Assertion.assertThat("get successfully placed order popup",paymentPage.isCorporateSuccessfullyPlacedOrderPopup(),is(true));

        step("Approve Booking By Dashboard Api" + orderID);
        orderID = CoreFunctions.getOrderId(orderID).toString();
        Boolean bookingStatus = corporateApi.approveBookingB2BApi(approverEmail, approverPassword, orderID);
        Assertion.assertThat("Booking approved by B2B dashboard", bookingStatus, is(true));

        step("verify successful payment popup from corporate");
        Assertion.assertThat("get successfully placed order popup",paymentPage.isCorporatePaymentSuccessPopupDisplayed(),is(true));

        step("Open the order list and go to order detail");
        paymentPage.SeeOrderListAfterPaymentSuccess();

        step("check order status");
        orderListPage.openIssuedTicketViaDeeplink(orderID);
        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.getETicketLabel(), either(is("Your e-ticket is here")).or(is("E-tiketmu di sini")));
    }

    @Author(names = {"Prerna"})
    @Module(name = "Booking")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void testLoginBookingTrainViaAdminWillBeAutoApproved(){
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testLoginBookingTrainViaAdminWillBeAutoApproved");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TrainSearchPage<WebDriver> trainSearchPage = TrainSearchPage.getInstance(session);
        TrainBookingPage<WebDriver> trainBookingPage = TrainBookingPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        TravelPlanWeb<?> travelPlanWeb = TravelPlanWeb.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);

        String email = param.get("username");
        String password = param.get("password");

        String tripType = param.get("tripType");
        String currency = param.get("currency");
        String originStation = param.get("originStation");
        String destinationStation = param.get("destinationStation");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int infantPassengers = Integer.parseInt(param.get("infant"));

        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate,daysToDepartureDate);

        String[] departureDateArray = departureDate.split(" ");
        String date = departureDateArray[0];

        step("User open app");
        homePage.gotoHome();

        step("Login as B2B account " + email + " via deeplink and change currency to " + currency);
        homePage.loginWithSpecificAccountViaDeeplink(email, password, currency);

        step("Verify Step");
        Assertion.assertThat("User is on login state", homePage.isUserLogin(), is(true));

        step("Open train page");
        homePage.openVerticalPageDeeplink("Trains","",currency);

        step("Select origin station");
        trainSearchPage.skipPopup();
        trainSearchPage.selectOrigin(originStation);

        step("Select destination station");
        trainSearchPage.selectDestination(destinationStation);

        step("Toggle oneway");
        trainSearchPage.toggleTripType(tripType);

        step("Select date");
        trainSearchPage.selectDate(departureDate);

        step("Add passenger");
        trainSearchPage.addPassenger(adultPassengers,infantPassengers);

        step("Search train");
        trainSearchPage.searchTrain();

        step("Skip Popup");
        trainSearchPage.skipPopup();

        step("Select departure train");
        trainSearchPage.selectTrainViaIndex(2);

        step("Fill passenger detail");
        trainBookingPage.fillPassengerDetail(adultPassengers,infantPassengers);

        step("Continue to 2nd half");
        trainBookingPage.continueTo2ndHalf();

        step("Continue to payment");
        trainBookingPage.continueToPayment();
        webViewPage.switchToWebViewUrl("corporate/interim");
        travelPlanWeb.skipTravelPlan();
        webViewPage.switchToNative();

        step("Payment page should be displayed");
        String orderID = paymentPage.checkOrderID();
        orderID = String.valueOf(getOrderId(orderID));

        step("Submit payment using Corporate Billing");
        paymentPage.clickPayButtonWithoutWaitForPreviousBookingPopup();

        step("verify successfully placed order popup from corporate");
        Assertion.assertThat("get successfully placed order popup",paymentPage.isCorporateSuccessfullyPlacedOrderPopup(),is(true));

        step("Waiting 60 seconds for Booking to get Auto Approved" + orderID);
        sleep(60);

        step("verify successful payment popup from corporate");
        Assertion.assertThat("get successfully placed order popup",paymentPage.isCorporatePaymentSuccessPopupDisplayed(),is(true));

        step("Open the order list and go to order detail");
        paymentPage.SeeOrderListAfterPaymentSuccess();
        orderListPage.openIssuedTicketViaDeeplink(orderID);
        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.getETicketLabel(), either(is("Your e-ticket is here")).or(is("E-tiketmu di sini")));
    }

    @Author(names = {"Kartik"})
    @Module(name = "Booking")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void testNoSuccessPaymentWithoutDashboardApproval(){
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testNoSuccessPaymentWithoutDashboardApproval");
        BaseTest.paramsThreadLocal.set(param);

        String email = param.get("email");
        String password = param.get("password");
        String hotelName = param.get("hotelName");
        String adminEmail = param.get("adminEmail");
        String adminPassword = param.get("adminPassword");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        CorporateLoginPage<WebDriver> corporateLoginPage = CorporateLoginPage.getInstance(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        TravelPlanWeb<?> travelPlanWeb = TravelPlanWeb.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);

        step("User open app");
        homePage.gotoHome();
        loginPage.loginAsGuest();

        step("B2B login");
        corporateLoginPage.corporateLogin(email,password);

        step("Change currency");
        homePage.changeCurrency("IDR",email);

        step("Open hotel landing page");
        homePage.openVerticalPageDeeplink("Hotel","","");

        step("Search stay location");
        landingPage.searchStayLocation(hotelName);

        step("Submit search");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        step("Go to the room list");
        detailPage.RemovePopUp();
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();

        step("Select room from the room list");
        detailPage.RemovePopUp();
        roomListPage.clickBookRoom();

        step("Submit booking");
        String guestName = bookingFormPage.getContactNameDetail();
        bookingFormPage.continueToPaymentPage();
        webViewPage.switchToWebViewUrl("corporate/interim");
        travelPlanWeb.skipTravelPlan();
        webViewPage.switchToNative();
        bookingFormPage.PriceChangesPopUp();

        step("Payment page should be displayed");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("Order id should be displayed",!orderID.isEmpty() , is(true));

        step("Click pay with corporate billing");
        paymentPage.clickPayButtonWithoutWaitForPreviousBookingPopup();

        step("open order page");
        paymentPage.SeeOrderListAfterPaymentSuccess();;

        step("check order status");
        Assertion.assertThat("Order is Waiting for payment and E-ticket not issued",  orderListPage.verifyWaitingForPayment(orderID), is(true));

        step("Check if the booked order is being reflected on corporate dashboard under purchase list");
        boolean isOrderReflectedOnPurchaseList = corporateApi.checkPurchaseOrderList(orderID, adminEmail, adminPassword,guestName);
        Assertion.assertThat("purchase order is reflected on B2B corporate dashboard in purchase list", isOrderReflectedOnPurchaseList, is(true));
    }

    @Author(names = {"Ashish"})
    @Module(name = "hotelBooking")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void hotelBookingFromApproverAutoApproved() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("hotelBookingFromApproverAutoApproved");
        BaseTest.paramsThreadLocal.set(param);

        String hotelName = param.get("hotelName");
        String approverUsername = param.get("approverUsername");
        String approverPassword = param.get("approverPassword");

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        CorporateLoginPage<WebDriver> corporateLoginPage = CorporateLoginPage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        TravelPlanWeb<?> travelPlanWeb = TravelPlanWeb.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("User open app as guest");
        loginPage.loginAsGuest();

        step("Change currency");
        homePage.changeCurrency("IDR","");

        step("Open hotel landing page");
        homePage.openVerticalPageDeeplink("Hotel","","");

        step("Search stay location");
        landingPage.searchStayLocation(hotelName);

        step("Submit search");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        step("Go to the room list");
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();

        step("Select room from the room list");
        roomListPage.getTheFirstHotelRoomPrice();
        roomListPage.clickBookRoom();

        step("open b2b Login on the booking form");
        bookingFormPage.openLoginPage();

        step("B2B login");
        corporateLoginPage.corporateLoginB2BViaDifferentPage(approverUsername,approverPassword);

        step("Submit the booking form");
        bookingFormPage.continueToPaymentPage();
        webViewPage.switchToWebViewUrl("corporate/interim");
        travelPlanWeb.skipTravelPlan();
        webViewPage.switchToNative();
        bookingFormPage.PriceChangesPopUp();

        step("Payment page should be displayed");
        String orderID = paymentPage.checkOrderID();
        orderID = String.valueOf(getOrderId(orderID));
        Assertion.assertThat("Corporate billing option is selected", paymentPage.getSelectedPayment().toLowerCase(), is("corporate billing"));

        step("Submit payment using Corporate Billing");
        paymentPage.clickPayButtonWithoutWaitForPreviousBookingPopup();

        step("verify successfully placed order popup from corporate");
        Assertion.assertThat("get successfully placed order popup",paymentPage.isCorporateSuccessfullyPlacedOrderPopup(),is(true));

        step("Waiting 60 seconds for Booking to get Auto Approved" + orderID);
        sleep(60);

        step("verify successful payment popup from corporate");
        Assertion.assertThat("get successfully placed order popup",paymentPage.isCorporatePaymentSuccessPopupDisplayed(),is(true));

        step("Open the order list and go to order detail");
        paymentPage.SeeOrderListAfterPaymentSuccess();
        orderListPage.openIssuedTicketViaDeeplink(orderID);
        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.getETicketLabel(), either(is("Your e-ticket is here")).or(is("E-tiketmu di sini")));
    }

    @Author(names = {"Ashish"})
    @Module(name = "hotelBooking")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void hotelBookingFromAdminAutoApproved() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("hotelBookingFromAdminAutoApproved");
        BaseTest.paramsThreadLocal.set(param);

        String hotelName = param.get("hotelName");
        String adminUsername = param.get("adminUsername");
        String adminPassword = param.get("adminPassword");

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        CorporateLoginPage<WebDriver> corporateLoginPage = CorporateLoginPage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        TravelPlanWeb<?> travelPlanWeb = TravelPlanWeb.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("User open app as guest");
        loginPage.loginAsGuest();

        step("Change currency");
        homePage.changeCurrency("IDR","");

        step("Open hotel landing page");
        homePage.openVerticalPageDeeplink("Hotel","","");

        step("Search stay location");
        landingPage.searchStayLocation(hotelName);

        step("Submit search");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        step("Go to the room list");
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();

        step("Select room from the room list");
        roomListPage.getTheFirstHotelRoomPrice();
        roomListPage.clickBookRoom();

        step("open b2b Login on the booking form");
        bookingFormPage.openLoginPage();

        step("B2B login");
        corporateLoginPage.corporateLoginB2BViaDifferentPage(adminUsername,adminPassword);

        step("Submit the booking form");
        bookingFormPage.continueToPaymentPage();
        webViewPage.switchToWebViewUrl("corporate/interim");
        travelPlanWeb.skipTravelPlan();
        webViewPage.switchToNative();
        bookingFormPage.PriceChangesPopUp();

        step("Payment page should be displayed");
        String orderID = paymentPage.checkOrderID();
        orderID = String.valueOf(getOrderId(orderID));
        Assertion.assertThat("Corporate billing option is selected", paymentPage.getSelectedPayment().toLowerCase(), is("corporate billing"));

        step("Submit payment using Corporate Billing");
        paymentPage.clickPayButtonWithoutWaitForPreviousBookingPopup();

        step("verify successfully placed order popup from corporate");
        Assertion.assertThat("get successfully placed order popup",paymentPage.isCorporateSuccessfullyPlacedOrderPopup(),is(true));

        step("Waiting 60 seconds for Booking to get Auto Approved" + orderID);
        sleep(60);

        step("verify successful payment popup from corporate");
        Assertion.assertThat("get successfully placed order popup",paymentPage.isCorporatePaymentSuccessPopupDisplayed(),is(true));

        step("Open the order list and go to order detail");
        paymentPage.SeeOrderListAfterPaymentSuccess();

        orderListPage.openIssuedTicketViaDeeplink(orderID);
        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.getETicketLabel(), either(is("Your e-ticket is here")).or(is("E-tiketmu di sini")));
    }

    @Author(names = {"Yazid"})
    @Module(name = "Booking")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void hotelBookingFromSuperAdminAutoApproved() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("hotelBookingFromSuperAdminAutoApproved");
        BaseTest.paramsThreadLocal.set(param);

        String hotelName = param.get("hotelName");
        String superAdminUsername = param.get("superAdminUsername");
        String superAdminPassword = param.get("superAdminPassword");

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        TravelPlanWeb<?> travelPlanWeb = TravelPlanWeb.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);

        step("User open app");
        homePage.gotoHome();

        step("Login as B2B super-admin account " + superAdminUsername + " via deeplink and change currency to IDR");
        homePage.loginWithSpecificAccountViaDeeplink(superAdminUsername, superAdminPassword, "IDR");

        step("Open hotel landing page");
        homePage.openVerticalPageDeeplink("Hotel","","IDR");

        step("Search stay location");
        landingPage.searchStayLocation(hotelName);

        step("Submit search");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        step("Go to the room list");
        detailPage.RemovePopUp();
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();

        step("Select room from the room list");
        roomListPage.getTheFirstHotelRoomPrice();
        roomListPage.clickBookRoom();

        step("Submit the booking form");
        bookingFormPage.continueToPaymentPage();
        webViewPage.switchToWebViewUrl("corporate/interim");
        travelPlanWeb.skipTravelPlan();
        webViewPage.switchToNative();
        bookingFormPage.PriceChangesPopUp();

        step("Payment page should be displayed");
        String orderID = paymentPage.checkOrderID();
        orderID = String.valueOf(getOrderId(orderID));
        Assertion.assertThat("Corporate billing option is selected", paymentPage.getSelectedPayment().toLowerCase(), is("corporate billing"));

        step("Submit payment using Corporate Billing");
        paymentPage.clickPayButtonWithoutWaitForPreviousBookingPopup();

        step("verify successfully placed order popup from corporate");
        Assertion.assertThat("get successfully placed order popup",paymentPage.isCorporateSuccessfullyPlacedOrderPopup(),is(true));

        step("Polling order status (up to 15 tries x 5s) until Booking is Auto Approved " + orderID);
        orderListPage.waitForPaymentSuccessful(orderID, 15);

        step("verify successful payment popup from corporate");
        Assertion.assertThat("get successfully placed order popup",paymentPage.isCorporatePaymentSuccessPopupDisplayed(),is(true));

        step("Open the order list and go to order detail");
        paymentPage.SeeOrderListAfterPaymentSuccess();
        orderListPage.openIssuedTicketViaDeeplink(orderID);
        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.getETicketLabel(), either(is("Your e-ticket is here")).or(is("E-tiketmu di sini")));
    }

    @Author(names = {"Yazid"})
    @Module(name = "TravelPlan")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void verifyTravelPlanInterimPageAirportTransfer() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyTravelPlanInterimPageAirportTransfer");
        BaseTest.paramsThreadLocal.set(param);

        String email = param.get("email");
        String password = param.get("password");
        String currency = param.get("currency");
        String airportCode = param.get("airportCode");
        String destination = param.get("destination");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        CorporateLoginPage<WebDriver> corporateLoginPage = CorporateLoginPage.getInstance(session);
        CarTransferSearchPage<WebDriver> carTransferSearchPage = CarTransferSearchPage.getInstance(session);
        AirportTransferLandingPageWeb<?> airportTransferLandingPageWeb = AirportTransferLandingPageWeb.getInstance(session);
        CarTransferSearchResultPage<WebDriver> carTransferSearchResultPage = CarTransferSearchResultPage.getInstance(session);
        AirportTransferSRPWeb<?> airportTransferSearchPage = AirportTransferSRPWeb.getInstance(session);
        AirportTransferPDPWeb<?> airportTransferPDPWeb = AirportTransferPDPWeb.getInstance(session);
        CarTransferBookingPage<WebDriver> carTransferBookingPage = CarTransferBookingPage.getInstance(session);
        AirportTransferBookingPageWeb<?> airportTransferBookingPage = AirportTransferBookingPageWeb.getInstance(session);
        TravelPlanWeb<?> travelPlanWeb = TravelPlanWeb.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);

        step("login with b2b traveler account");
        homePage.gotoHome();
        loginPage.loginAsGuest();
        corporateLoginPage.corporateLogin(email, password);
        homePage.changeCurrency(currency, email);
        String language = homePage.getLanguageApp();

        step("select Airport transfer");
        homePage.openVerticalPageDeeplink("Car Transfers", language, currency);
        carTransferSearchPage.allowLocationAccess();
        webViewPage.switchToWebViewUrl("airport-transfer");
        airportTransferLandingPageWeb.closePopup();

        step("search for from Airport ("+airportCode+" -> "+destination+")");
        airportTransferLandingPageWeb.chooseFromAirport();
        airportTransferLandingPageWeb.searchAirport(airportCode);
        airportTransferLandingPageWeb.searchDestination(destination);

        step("select car");
        airportTransferLandingPageWeb.searchCars();
        airportTransferSearchPage.chooseCar();
        airportTransferSearchPage.chooseVendor();

        step("proceed further to booking form page");
        airportTransferPDPWeb.clickSubmitCar();

        step("after clicking continue to pay");
        airportTransferBookingPage.clickSubmitBooking();
        webViewPage.switchToWebViewUrl("airport-transfer/booking");

        step("will get Redirected to Interim Page");
        Assertion.assertThat("Interim Page should be visible", travelPlanWeb.isTravelPlanInterimPageShown(), is(true));
        webViewPage.switchToNative();
    }

    @Author(names = {"Prerna"})
    @Module(name = "Booking")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void flightBookingFromTravelerToAdmin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("flightBookingFromTravelerToAdmin");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightProductReviewPage<WebDriver> flightProductReviewPage = FlightProductReviewPage.getInstance(session);
        FlightBookingFormPage<WebDriver> flightBookingFormPage = FlightBookingFormPage.getInstance(session);
        FlightPage<WebDriver> flightPage = FlightPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        CorporateLoginPage<WebDriver> corporateLoginPage = CorporateLoginPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);
        TravelPlanWeb<?> travelPlanWeb = TravelPlanWeb.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        FlightApi<?> flightApi = new FlightApi<>(session);

        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        String airline = param.get("airline");

        String username = param.get("username");
        String password = param.get("password");
        String adminUsername = param.get("adminUsername");
        String adminPassword = param.get("adminPassword");

        step("Find dynamic data");
        FlightDynamicData flightDynamicData = flightApi.findDate(
                username,password,
                originAirport,destinationAirport,daysToDepartureDate,
                adultPassengers,childPassengers,infantPassengers,
                tripType,airline,"","","","IDR"
        );
        String departureDate =CoreFunctions.convertToLongDate(flightDynamicData.departureDate);
        airline = flightDynamicData.airlineDepart;

        if (Configs.ENVIRONMENT == Environment.PREPROD){
            step("Validate all suppliers are mock-issued");
            flightApi.validateAllSuppliersAreMockIssuedByAirline(flightDynamicData.airlineCodeDepart, flightDynamicData.originAirport, flightDynamicData.destinationAirport);

        }

        step("User open app");
        homePage.gotoHome();
        loginPage.loginAsGuest();

        step("B2B login");
        corporateLoginPage.corporateLogin(username,password);

        step("Change currency");
        homePage.changeCurrency("IDR",username);

        step("Open flights page");
        homePage.openVerticalPageDeeplink("Flights","","");
        flightSearchPage.closePopup();

        step("change date using deeplink");
        flightSearchPage.changeDateViaDeeplink(flightDynamicData, tripType);

        step("Toggle trip type { " + tripType + " }");
        flightSearchPage.toggleTriptype(tripType);

        step("Select origin airport");
        flightSearchPage.selectOrigin(originAirport);
        flightSearchPage.closePopup();

        step("Select destination airport");
        flightSearchPage.selectDestination(destinationAirport);

        step("Select date");
        flightSearchPage.selectDate(departureDate);

        step("Add passenger");
        flightSearchPage.addPassenger(adultPassengers,childPassengers,infantPassengers);

        step("Search flight");
        flightSearchPage.searchFlight();

        step("Open filter");
        flightSearchResultPage.openFilter();

        step("Filter airline");
        flightSearchResultPage.filterAirline(airline);

        step("Save filter");
        flightSearchResultPage.saveFilter();

        step("Click flight detail chevron");
        flightSearchResultPage.selectFlight();

        step("Continue to booking form");
        flightProductReviewPage.continueToBookingForm(tripType);

        step("verify date format");
        flightBookingFormPage.verifyDateFormat(tripType);

        step("Fill passenger detail");
        flightBookingFormPage.fillPassengerDetail(adultPassengers,childPassengers,infantPassengers, "domestic",airline);

        step("Continue to second half booking form");
        flightBookingFormPage.continueToSecondHalf();

        step("Continue to payment");
        flightBookingFormPage.continueToPayment();
        webViewPage.switchToWebViewUrl("corporate/interim");
        travelPlanWeb.skipTravelPlan();
        webViewPage.switchToNative();

        step("Skip full protection");
        flightBookingFormPage.skipFullProtection();

        step("Payment page should be displayed");
        String orderID = paymentPage.checkOrderID();
        orderID = String.valueOf(getOrderId(orderID));

        step("Submit payment using Corporate Billing");
        paymentPage.clickPayButtonWithoutWaitForPreviousBookingPopup();

        step("verify successfully placed order popup from corporate");
        Assertion.assertThat("get successfully placed order popup",paymentPage.isCorporateSuccessfullyPlacedOrderPopup(),is(true));

        step("Approve Booking By Dashboard Api" + orderID);
        orderID = CoreFunctions.getOrderId(orderID).toString();
        Boolean bookingStatus = corporateApi.approveBookingB2BApi(adminUsername, adminPassword, orderID);
        Assertion.assertThat("Booking approved by B2B dashboard", bookingStatus, is(true));

        step("verify successful payment popup from corporate");
        Assertion.assertThat("get successfully placed order popup",paymentPage.isCorporatePaymentSuccessPopupDisplayed(),is(true));

         step("Open the order list and go to order detail");
         paymentPage.SeeOrderListAfterPaymentSuccess();

         step("get booking code from API");
         String bookingCode = flightPage.getBookingCode(orderID, username);

         step("set Tiket Number on flight dashboard and ticket will be eticket issued");
         Boolean isTicketIssued = flightPage.flightEticketIssuedRequest(orderID, bookingCode, username);
         Assertion.assertThat("is Eticket Issued", isTicketIssued, is(true));

         step("check order status");
         orderListPage.openIssuedTicketViaDeeplink(orderID);
         Assertion.assertThat("OrderStatus is Eticket issued", orderDetailPage.getETicketLabel(), either(is("Your e-ticket is here")).or(is("E-tiketmu di sini")));
    }

    @Author(names = {"Prerna"})
    @Module(name = "Booking")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void flightBookingFromAdminIsAutoApproved() {
        //deprecated using api for this case
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("flightBookingFromAdminIsAutoApproved");
        BaseTest.paramsThreadLocal.set(param);

         HomePage<WebDriver> homePage = HomePage.getInstance(session);
         LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
         FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
         FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
         FlightProductReviewPage<WebDriver> flightProductReviewPage = FlightProductReviewPage.getInstance(session);
         FlightBookingFormPage<WebDriver> flightBookingFormPage = FlightBookingFormPage.getInstance(session);
         FlightPage<WebDriver> flightPage = FlightPage.getInstance(session);
         PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
         CorporateLoginPage<WebDriver> corporateLoginPage = CorporateLoginPage.getInstance(session);
         OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
         OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
         TravelPlanWeb<?> travelPlanWeb = TravelPlanWeb.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
         FlightApi<?> flightApi = new FlightApi<>(session);

        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        String airline = param.get("airline");

        String username = param.get("username");
        String password = param.get("password");

        step("Find dynamic data");
        FlightDynamicData flightDynamicData = flightApi.findDate(
                username,password,
                originAirport,destinationAirport,daysToDepartureDate,
                adultPassengers,childPassengers,infantPassengers,
                tripType,airline,"","","","IDR"
        );
        String departureDate =CoreFunctions.convertToLongDate(flightDynamicData.departureDate);
        airline = flightDynamicData.airlineDepart;

        if (Configs.ENVIRONMENT == Environment.PREPROD){
            step("Validate all suppliers are mock-issued");
            flightApi.validateAllSuppliersAreMockIssuedByAirline(flightDynamicData.airlineCodeDepart, flightDynamicData.originAirport, flightDynamicData.destinationAirport);
        }

        step("User open app");
        homePage.gotoHome();
        loginPage.loginAsGuest();

        step("B2B login");
        corporateLoginPage.corporateLogin(username,password);

        step("Change currency");
        homePage.changeCurrency("IDR",username);

        step("Open flights page");
        homePage.openVerticalPageDeeplink("Flights","","");
        flightSearchPage.closePopup();

        step("change date using deeplink");
        flightSearchPage.changeDateViaDeeplink(flightDynamicData, tripType);

        step("Toggle trip type { " + tripType + " }");
        flightSearchPage.toggleTriptype(tripType);

        step("Select origin airport");
        flightSearchPage.selectOrigin(originAirport);
        flightSearchPage.closePopup();

        step("Select destination airport");
        flightSearchPage.selectDestination(destinationAirport);

        step("Select date");
        flightSearchPage.selectDate(departureDate);

        step("Add passenger");
        flightSearchPage.addPassenger(adultPassengers,childPassengers,infantPassengers);

        step("Search flight");
        flightSearchPage.searchFlight();

        step("Open filter");
        flightSearchResultPage.openFilter();

        step("Filter airline");
        flightSearchResultPage.filterAirline(airline);

        step("Save filter");
        flightSearchResultPage.saveFilter();

        step("Click flight detail chevron");
        flightSearchResultPage.selectFlight();

        step("Continue to booking form");
        flightProductReviewPage.continueToBookingForm(tripType);

        step("verify date format");
        flightBookingFormPage.verifyDateFormat(tripType);

        step("Fill passenger detail");
        flightBookingFormPage.fillPassengerDetail(adultPassengers,childPassengers,infantPassengers, "domestic",airline);

        step("Continue to second half booking form");
        flightBookingFormPage.continueToSecondHalf();

        step("Continue to payment");
        flightBookingFormPage.continueToPayment();
        webViewPage.switchToWebViewUrl("corporate/interim");
        travelPlanWeb.skipTravelPlan();
        webViewPage.switchToNative();

        step("Skip full protection");
        flightBookingFormPage.skipFullProtection();

        step("Payment page should be displayed");
        String orderID = paymentPage.checkOrderID();
        orderID = String.valueOf(getOrderId(orderID));

        step("Submit payment using Corporate Billing");
        paymentPage.clickPayButtonWithoutWaitForPreviousBookingPopup();

        step("verify successfully placed order popup from corporate");
        Assertion.assertThat("get successfully placed order popup",paymentPage.isCorporateSuccessfullyPlacedOrderPopup(),is(true));

        step("Waiting 60 seconds for order to be auto approved");
        sleep(60);

        step("verify successful payment popup from corporate");
        Assertion.assertThat("get successfully placed order popup",paymentPage.isCorporatePaymentSuccessPopupDisplayed(),is(true));

         step("Open the order list and go to order detail");
         paymentPage.SeeOrderListAfterPaymentSuccess();

         step("get booking code from API");
         String bookingCode = flightPage.getBookingCode(orderID, username);
         log(bookingCode);

         step("set Tiket Number on flight dashboard and ticket will be eticket issued");
         Boolean isTicketIssued = flightPage.flightEticketIssuedRequest(orderID, bookingCode, username);
         Assertion.assertThat("is Eticket Issued", isTicketIssued, is(true));

         step("check order status");
         orderListPage.openIssuedTicketViaDeeplink(orderID);
         Assertion.assertThat("OrderStatus is Eticket issued", orderDetailPage.getETicketLabel(), either(is("Your e-ticket is here")).or(is("E-tiketmu di sini")));
    }

    @Author(names = {"Prerna"})
    @Module(name = "Booking")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void flightBookingFromApproverIsAutoApproved() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("flightBookingFromApproverIsAutoApproved");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightProductReviewPage<WebDriver> flightProductReviewPage = FlightProductReviewPage.getInstance(session);
        FlightBookingFormPage<WebDriver> flightBookingFormPage = FlightBookingFormPage.getInstance(session);
        FlightPage<WebDriver> flightPage = FlightPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        TravelPlanWeb<?> travelPlanWeb = TravelPlanWeb.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        FlightApi<?> flightApi = new FlightApi<>(session);

        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        ArrayList<String> airlines = CoreFunctions.makeAirlineArrayList(param.get("airline"));

        String username = param.get("username");
        String password = param.get("password");

        step("Find dynamic data");
        FlightDynamicData flightDynamicData = flightApi.findDateWithMockIssuedAirline(
                username,password,
                originAirport,destinationAirport,daysToDepartureDate,
                adultPassengers,childPassengers,infantPassengers,
                tripType,airlines,"","","IDR"
        );
        String departureDate =CoreFunctions.convertToLongDate(flightDynamicData.departureDate);
        String airline = flightDynamicData.airlineDepart;

        step("User open app");
        homePage.gotoHome();

        step("Login as B2B account " + username + " via deeplink and change currency to IDR");
        homePage.loginWithSpecificAccountViaDeeplink(username, password, "IDR");

        step("Open flights page");
        homePage.openVerticalPageDeeplink("Flights","","");
        flightSearchPage.closePopup();

        step("change date using deeplink");
        flightSearchPage.changeDateViaDeeplink(flightDynamicData, tripType);

        step("Toggle trip type { " + tripType + " }");
        flightSearchPage.toggleTriptype(tripType);

        step("Select origin airport");
        flightSearchPage.selectOrigin(originAirport);
        flightSearchPage.closePopup();

        step("Select destination airport");
        flightSearchPage.selectDestination(destinationAirport);

        step("Select date");
        flightSearchPage.selectDate(departureDate);

        step("Add passenger");
        flightSearchPage.addPassenger(adultPassengers,childPassengers,infantPassengers);

        step("Search flight");
        flightSearchPage.searchFlight();

        step("Open filter");
        flightSearchResultPage.openFilter();

        step("Filter airline");
        flightSearchResultPage.filterAirline(airline);

        step("Save filter");
        flightSearchResultPage.saveFilter();

        step("Click flight detail chevron");
        flightSearchResultPage.selectFlight();

        step("Continue to booking form");
        flightProductReviewPage.continueToBookingForm(tripType);

        step("verify date format");
        flightBookingFormPage.verifyDateFormat(tripType);

        step("Fill passenger detail");
        flightBookingFormPage.fillPassengerDetail(adultPassengers,childPassengers,infantPassengers, "domestic",airline);

        step("Continue to second half booking form");
        flightBookingFormPage.continueToSecondHalf();

        step("Continue to payment");
        flightBookingFormPage.continueToPayment();

        step("Skip full protection");
        flightBookingFormPage.skipFullProtection();
        webViewPage.switchToWebViewUrl("corporate/interim");
        travelPlanWeb.skipTravelPlan();
        webViewPage.switchToNative();

        step("Payment page should be displayed");
        String orderID = paymentPage.checkOrderID();
        orderID = String.valueOf(getOrderId(orderID));

        step("Validate all suppliers are mock-issued");
        flightApi.validateAllSuppliersAreMockIssuedByAirline(flightDynamicData.airlineCodeDepart, flightDynamicData.originAirport, flightDynamicData.destinationAirport);

        step("Submit payment using Corporate Billing");
        paymentPage.clickPayButtonWithoutWaitForPreviousBookingPopup();

        step("verify successfully placed order popup from corporate");
        Assertion.assertThat("get successfully placed order popup",paymentPage.isCorporateSuccessfullyPlacedOrderPopup(),is(true));

        step("Waiting 60 seconds for order to be auto approved");
        orderListPage.waitForPaymentSuccessful(orderID,30);

        step("verify successful payment popup from corporate");
        Assertion.assertThat("get successfully placed order popup",paymentPage.isCorporatePaymentSuccessPopupDisplayed(),is(true));

         step("Open the order list and go to order detail");
         paymentPage.SeeOrderListAfterPaymentSuccess();

         step("Open the order list and go to order detail");
         homePage.openMyOrderPage();

        step("check order status");
        orderListPage.openOrderDetailViaDeeplink(orderID);
        Assertion.assertThat("e-Ticket on the order detail should be {wait for confirmation} or {Your e-ticket is here}", orderDetailPage.getETicketLabel(), either(is("Your e-ticket is here")).or(is("E-tiketmu di sini")).or(is("Waiting for confirmation")).or(is("Menunggu konfirmasi")).or(is("Processing e-ticket")).or(is("Memproses e-tiket")));
    }

    @Author(names = {"Ashish"})
    @Module(name = "Booking")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void testLoginBookingBusViaTravelerApproveBookingByApprover(){
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testLoginBookingBusViaTravelerApproveBookingByApprover");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        CorporateLoginPage<WebDriver> corporateLoginPage = CorporateLoginPage.getInstance(session);
        BusSearchPage<WebDriver> busSearchPage = BusSearchPage.getInstance(session);
        BusSearchResultPage<WebDriver> busSearchResultPage = BusSearchResultPage.getInstance(session);
        BuspdpPage<WebDriver> buspdpPage = BuspdpPage.getInstance(session);
        BusBookingFormPage<WebDriver> busBookingFormPage = BusBookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);

        String username = param.get("username");
        String password = param.get("password");
        String approverEmail = param.get("approverEmail");
        String approverPassword = param.get("approverPassword");

        String tripType = param.get("tripType");
        String originLocation = param.get("originLocation");
        String destinationLocation = param.get("destinationLocation");
        int daysToDeparture = Integer.parseInt(param.get("daysToDeparture"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String dateDeparture = CoreFunctions.addToLongDate(currentDate,daysToDeparture);
        int passenger = Integer.parseInt(param.get("passenger"));

        step("User open app");
        homePage.gotoHome();
        loginPage.loginAsGuest();

        step("B2B login");
        corporateLoginPage.corporateLogin(username,password);

        step("Change currency");
        homePage.changeCurrency("IDR",username);

        step("Open Bus vertical");
        homePage.openVerticalPageDeeplink("Bus","","");

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

        step("Input contact detail");
        busBookingFormPage.togglePassengerSameAsContact();

        step("Continue to payment page");
        busBookingFormPage.continueToPaymentPage();

        step("Payment page should be displayed");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("Order id should be displayed", !orderID.isEmpty(), is(true));

        step("Submit payment using Corporate Billing");
        paymentPage.clickPayButtonWithoutWaitForPreviousBookingPopup();

        step("verify successfully placed order popup from corporate");
        Assertion.assertThat("get successfully placed order popup",paymentPage.isCorporateSuccessfullyPlacedOrderPopup(),is(true));

        step("Approve Booking By Dashboard Api" + orderID);
        orderID = CoreFunctions.getOrderId(orderID).toString();
        Boolean bookingStatus = corporateApi.approveBookingB2BApi(approverEmail, approverPassword, orderID);
        Assertion.assertThat("Booking approved by B2B dashboard", bookingStatus, is(true));

        step("verify successful payment popup from corporate");
        Assertion.assertThat("get successfully placed order popup",paymentPage.isCorporatePaymentSuccessPopupDisplayed(),is(true));

        step("Open the order list and go to order detail");
        paymentPage.SeeOrderListAfterPaymentSuccess();

        step("check order status");
        orderListPage.openIssuedTicketViaDeeplink(orderID);
        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.getETicketLabel(), either(is("Your e-ticket is here")).or(is("E-tiketmu di sini")));
    }

    @Author(names = {"Ashish"})
    @Module(name = "Booking")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void testLoginBookingBusViaAdminAutoApproved(){
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testLoginBookingBusViaAdminAutoApproved");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        CorporateLoginPage<WebDriver> corporateLoginPage = CorporateLoginPage.getInstance(session);
        BusSearchPage<WebDriver> busSearchPage = BusSearchPage.getInstance(session);
        BusSearchResultPage<WebDriver> busSearchResultPage = BusSearchResultPage.getInstance(session);
        BuspdpPage<WebDriver> buspdpPage = BuspdpPage.getInstance(session);
        BusBookingFormPage<WebDriver> busBookingFormPage = BusBookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);

        String username = param.get("username");
        String password = param.get("password");

        String tripType = param.get("tripType");
        String originLocation = param.get("originLocation");
        String destinationLocation = param.get("destinationLocation");
        int daysToDeparture = Integer.parseInt(param.get("daysToDeparture"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String dateDeparture = CoreFunctions.addToLongDate(currentDate,daysToDeparture);
        int passenger = Integer.parseInt(param.get("passenger"));

        step("User open app");
        homePage.gotoHome();
        loginPage.loginAsGuest();

        step("B2B login");
        corporateLoginPage.corporateLogin(username,password);

        step("Change currency");
        homePage.changeCurrency("IDR",username);

        step("Open Bus vertical");
        homePage.openVerticalPageDeeplink("Bus","","");

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

        step("Input contact detail");
        busBookingFormPage.togglePassengerSameAsContact();

        step("Continue to payment page");
        busBookingFormPage.continueToPaymentPage();

        step("Payment page should be displayed");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("Order id should be displayed", !orderID.isEmpty(), is(true));

        step("Submit payment using Corporate Billing");
        paymentPage.clickPayButtonWithoutWaitForPreviousBookingPopup();

        step("verify successfully placed order popup from corporate");
        Assertion.assertThat("get successfully placed order popup",paymentPage.isCorporateSuccessfullyPlacedOrderPopup(),is(true));
        sleep(60);

        step("verify successful payment popup from corporate");
        Assertion.assertThat("get successfully placed order popup",paymentPage.isCorporatePaymentSuccessPopupDisplayed(),is(true));

        step("Open the order list and go to order detail");
        paymentPage.SeeOrderListAfterPaymentSuccess();

        step("check order status");
        orderListPage.openIssuedTicketViaDeeplink(orderID);
        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.getETicketLabel(), either(is("Your e-ticket is here")).or(is("E-tiketmu di sini")));
    }

    @Author(names = {"Ashish"})
    @Module(name = "Booking")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void testLoginBookingFerryViaTravelerApproveBookingByApprover(){
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testLoginBookingFerryViaTravelerApproveBookingByApprover");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        CorporateLoginPage<WebDriver> corporateLoginPage = CorporateLoginPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        FerryPage<WebDriver> ferryPage = FerryPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);

        String username = param.get("username");
        String password = param.get("password");
        String approverEmail = param.get("approverEmail");
        String approverPassword = param.get("approverPassword");

        String origin = param.get("origin");
        String destination = param.get("destination");
        String departureDate = CoreFunctions.getEndOfCurrentMonth("yyyy-MM-dd");
        String departureTime = param.get("departureTime").replace("-", ":");
        String ticketType = param.get("ticketType");

        int adult = Integer.parseInt(param.get("adult"));
        int child = Integer.parseInt(param.get("child"));
        int infant = Integer.parseInt(param.get("infant"));
        int elderly = Integer.parseInt(param.get("elder"));

        step("Create ferry waiting payment order via API");
        int orderId = ferryPage.createWaitingPaymentOrderFerryB2B(username,password,origin,destination,departureDate,departureTime,ticketType,adult,child,infant,elderly);
        Assertion.assertThat("Verify orderId is generated", orderId, greaterThan(0));
        String orderIdStr = String.valueOf(orderId);

        step("User open app");
        homePage.gotoHome();
        loginPage.loginAsGuest();

        step("login via b2b account");
        corporateLoginPage.corporateLogin(username,password);

        step("change currency to default IDR");
        homePage.changeCurrency("IDR",username);

        step("Open Your Orders page");
        homePage.openMyOrderPage();

        step("open payment page");
        orderListPage.openOrderCompletePayment(orderIdStr);
        String orderIdFromPaymentPage = paymentPage.checkOrderID();
        Assertion.assertThat("is open correct payment order",orderIdFromPaymentPage.contains(orderIdStr),is(true));

        step("Submit payment using Corporate Billing");
        paymentPage.clickPayButtonWithoutWaitForPreviousBookingPopup();

        step("verify successfully placed order popup from corporate");
        Assertion.assertThat("get successfully placed order popup",paymentPage.isCorporateSuccessfullyPlacedOrderPopup(),is(true));

        step("Approve Booking By Dashboard Api " + orderIdFromPaymentPage);
        Boolean bookingStatus = corporateApi.approveBookingB2BApi(approverEmail, approverPassword, orderIdFromPaymentPage);
        Assertion.assertThat("Booking approved by B2B dashboard", bookingStatus, is(true));

        step("verify successful payment popup from corporate");
        Assertion.assertThat("get successfully placed order popup",paymentPage.isCorporatePaymentSuccessPopupDisplayed(),is(true));

        step("Open the order list and go to order detail");
        paymentPage.SeeOrderListAfterPaymentSuccess();

        step("check order status");
        orderListPage.openIssuedTicketViaDeeplink(orderIdFromPaymentPage);
        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.getETicketLabel(), either(is("Your e-ticket is here")).or(is("E-tiketmu di sini")));
    }


    @Author(names = {"Ashish"})
    @Module(name = "Booking")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void testLoginBookingFerryViaApproverAutoApproved(){
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testLoginBookingFerryViaApproverAutoApproved");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        CorporateLoginPage<WebDriver> corporateLoginPage = CorporateLoginPage.getInstance(session);
        FerrySearchPage<WebDriver> ferrySearchPage = FerrySearchPage.getInstance(session);
        FerrySearchResultPage<WebDriver> ferrySearchResultPage = FerrySearchResultPage.getInstance(session);
        FerryBookingPage<WebDriver> ferryBookingPage = FerryBookingPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        TravelPlanWeb<?> travelPlanWeb = TravelPlanWeb.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        FerryApi<?> ferryApi = new FerryApi<>(session);

        String username = param.get("username");
        String password = param.get("password");

        String originPort = param.get("originPort");
        String destinationPort = param.get("destinationPort");
        String ticketType = param.get("ticketType");
        int adult = Integer.parseInt(param.get("adult"));
        int child = Integer.parseInt(param.get("child"));
        String VaccineVerifiedID = param.get("VaccineVerifiedID");
        String vaccineCheck = param.get("vaccineCheck");

        step("Get available date from API");
        String departureDate = ferryApi.getFerryAvailableDate(originPort, destinationPort, ticketType, adult, child,0,0);

        step("User open app");
        homePage.gotoHome();
        loginPage.loginAsGuest();

        step("login using b2b account");
        corporateLoginPage.corporateLogin(username,password);

        step("change currency to default IDR");
        homePage.changeCurrency("IDR",username);

        step("Open ferry vertical");
        homePage.openVerticalPageDeeplink("Ferry","","");

        step("Skip coachmark");
        ferrySearchPage.skipCoachMark();

        step("input origin port");
        ferrySearchPage.skipCoachMark();
        ferrySearchPage.inputOrigin(originPort);

        step("input destination port");
        ferrySearchPage.inputDestination(destinationPort);

        step("User input pax qty");
        ferrySearchPage.openTicketQty();
        ferrySearchPage.setTicketQty(adult,child,0,0);
        ferrySearchPage.saveTicketQty();

        step("Input ticket type");
        ferrySearchPage.selectTicketType(ticketType);

        step("Departure date");
        ferrySearchPage.selectDate(departureDate);

        step("submit search");
        ferrySearchPage.submitSearch();

        step("Skip information coachmark in srp");
        ferrySearchResultPage.skipCoachMark();

        step("Check booking form price detail");
        ferrySearchResultPage.chooseFirstFleet();

        step("Input passenger detail");
        ferryBookingPage.fillGuestsAndVaccineDetails(vaccineCheck, VaccineVerifiedID, adult, 0);

        step("Check term and condition");
        ferryBookingPage.clickTncCheckBox();

        step("continue to payment page");
        ferryBookingPage.submitBooking();
        ferryBookingPage.setLocationService();
        webViewPage.switchToWebViewUrl("corporate/interim");
        travelPlanWeb.skipTravelPlan();
        webViewPage.switchToNative();

        step("Payment page should be displayed");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("Order id should be displayed", !orderID.isEmpty(), is(true));

        step("Submit payment using Corporate Billing");
        paymentPage.clickPayButtonWithoutWaitForPreviousBookingPopup();

        step("verify successfully placed order popup from corporate");
        Assertion.assertThat("get successfully placed order popup",paymentPage.isCorporateSuccessfullyPlacedOrderPopup(),is(true));
        sleep(60);

        step("verify successful payment popup from corporate");
        Assertion.assertThat("get successfully placed order popup",paymentPage.isCorporatePaymentSuccessPopupDisplayed(),is(true));

        step("Open the order list and go to order detail");
        paymentPage.SeeOrderListAfterPaymentSuccess();

        step("check order status");
        orderListPage.openIssuedTicketViaDeeplink(orderID);
        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.getETicketLabel(), either(is("Your e-ticket is here")).or(is("E-tiketmu di sini")));
    }

    @Author(names = {"Prerna"})
    @Module(name = "Refund")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void testApproveRefundRequestByTraveler() {
        Session<WebDriver> session = sessionThreadLocal.get();

        Map<String, String> param = getParam("testApproveRefundRequestByTraveler");
        BaseTest.paramsThreadLocal.set(param);

        String username = param.get("username");
        String password = param.get("password");
        String startDate = generateRandomDate(param.get("rangeAddDate"));
        String approverUsername = param.get("approverUsername");
        String approverPassword = param.get("approverPassword");

        String query = param.get("hotelName");
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));
        int room = Integer.parseInt(param.get("room"));

        String[] salutationList = { "Mr", "Mrs", "Ms" };
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = getRandomObject(salutationList);

        HotelPage<WebDriver> hotelPage = HotelPage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        RefundPage<WebDriver> refundPage = RefundPage.getInstance(session);

        String orderID;
        step("Create new hotel issued order");
        orderID = hotelApi.createIssuedOrderApiB2B(
                username, password, startDate, query, adult, night, room,"IDR",
                orderFullName, orderNameTitle, guestFullName, guestTitle, corporateApi.getEmployeeData(username), false);
        Assertion.assertThat("Hotel Booking via API is successful for B2B account", !orderID.isEmpty(), is(true));
        sleep(5);

        step("Approve booking by approver account for Order ID: " + orderID);
        Boolean bookingStatus = corporateApi.approveBookingB2BApi(approverUsername, approverPassword, orderID);
        Assertion.assertThat("Booking approved by B2B dashboard", bookingStatus, is(true));

        step("Goto home");
        homePage.gotoHome();

        step("Login as B2B traveler account " + username + " via deeplink");
        homePage.loginViaDeeplink(username, password);

        step("Find the issued order");
        homePage.openMyOrderPage();
        orderListPage.openIssuedTicketViaDeeplink(orderID);

        step("Open refund menu");
        orderDetailPage.clickRefundButton();

        step("Choose refund reason");
        refundPage.chooseRefundReasonWebView();

        step("Click process refund button");
        refundPage.clickProcessRefundBtn();

        step("Submit refund");
        refundPage.submitRefundCorporateBilling();

        step("Check if the refunded order is being reflected on corporate dashboard");
        String refundId = corporateApi.getRefundId(orderID, approverUsername, approverPassword,"prerefund");
        Boolean isApproved = true;
        Boolean success = corporateApi.approveOrRejectRefundB2B(approverUsername, approverPassword, refundId, isApproved);
        Assertion.assertThat("Refund Approved Success", success, is(true));
    }

    @Author(names = {"Prerna"})
    @Module(name = "Refund")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void testRejectRefundRequestByTraveler() throws ExecutionException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();

        Map<String, String> param = getParam("testRejectRefundRequestByTraveler");
        BaseTest.paramsThreadLocal.set(param);

        String username = param.get("username");
        String password = param.get("password");
        String startDate = generateRandomDate(param.get("rangeAddDate"));
        String approverUsername = param.get("approverUsername");
        String approverPassword = param.get("approverPassword");

        String query = param.get("hotelName");
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));
        int room = Integer.parseInt(param.get("room"));

        String[] salutationList = { "Mr", "Mrs", "Ms" };
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = getRandomObject(salutationList);

        HotelPage<WebDriver> hotelPage = HotelPage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        RefundPage<WebDriver> refundPage = RefundPage.getInstance(session);
        CorporateLoginPage<WebDriver> corporateLoginPage = CorporateLoginPage.getInstance(session);

        String orderID;
        step("Create new hotel issued order");
        orderID = hotelApi.createIssuedOrderApiB2B(
            username, password, startDate, query, adult, night, room,"IDR",
            orderFullName, orderNameTitle, guestFullName, guestTitle, corporateApi.getEmployeeData(username), false);
        Assertion.assertThat("Hotel Booking via API is successful for B2B account", !orderID.isEmpty(), is(true));
        sleep(5);

        step("Approve booking by approver account for Order ID: " + orderID);
        Boolean bookingStatus = corporateApi.approveBookingB2BApi(approverUsername, approverPassword, orderID);
        Assertion.assertThat("Booking approved by B2B dashboard", bookingStatus, is(true));

        step("Goto home");
        homePage.gotoHome();

        step("Log out");
        loginPage.loginAsGuest();

        step("B2B login");
        corporateLoginPage.corporateLogin(username,password);

        step("Find the issued order");
        homePage.openMyOrderPage();
        orderListPage.openIssuedTicketViaDeeplink(orderID);

        step("Open refund menu");
        orderDetailPage.clickRefundButton();

        step("Choose refund reason");
        refundPage.chooseRefundReasonWebView();

        step("Click process refund button");
        refundPage.clickProcessRefundBtn();

        step("Submit refund");
        refundPage.submitRefundCorporateBilling();

        sleep(20);
        step("Check if the refunded order is being reflected on corporate dashboard");
        String refundId = corporateApi.getRefundId(orderID, approverUsername, approverPassword,"prerefund");
        Boolean isApproved = false;
        Boolean success = corporateApi.approveOrRejectRefundB2B(approverUsername, approverPassword, refundId, isApproved);
        Assertion.assertThat("Refund Rejection Success", success, is(true));

        step("go to order list");
        refundPage.clickGoToOrderListBtnOnRefundPage();

        step("Open order from active ticket");
        orderListPage.openIssuedTicketViaDeeplink(orderID);

        Assertion.assertThat("Order is in Active order state", orderDetailPage.getOrderID(), is(orderID));
        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.getETicketLabel(), either(is("Your e-ticket is here")).or(is("E-tiketmu di sini")));
    }

    @Author(names = {"Ashish"})
    @Module(name = "Refund")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void testLoginRefundHotelViaApproverAutoApproved() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testLoginRefundHotelViaApproverAutoApproved");
        BaseTest.paramsThreadLocal.set(param);

        String startDate = generateRandomDate(param.get("rangeAddDate"));
        String approverUsername = param.get("username");
        String approverPassword = param.get("password");

        String query = param.get("hotelName");
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));
        int room = Integer.parseInt(param.get("room"));

        String[] salutationList = { "Mr", "Mrs", "Ms" };
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = getRandomObject(salutationList);

        HotelPage<WebDriver> hotelPage = HotelPage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        RefundPage<WebDriver> refundPage = RefundPage.getInstance(session);
        CorporateLoginPage<WebDriver> corporateLoginPage = CorporateLoginPage.getInstance(session);

        step("Create new hotel issued order");
        String orderID = hotelApi.createIssuedOrderApiB2B(
                approverUsername, approverPassword, startDate, query, adult, night, room,"IDR",
                orderFullName, orderNameTitle, guestFullName, guestTitle, corporateApi.getEmployeeData(approverUsername), false);
        Assertion.assertThat("Hotel Booking via API is successful for B2B account", !orderID.isEmpty(), is(true));

        step("Goto home");
        homePage.gotoHome();

        step("Log out");
        loginPage.loginAsGuest();

        step("B2B login");
        corporateLoginPage.corporateLogin(approverUsername,approverPassword);

        step("Find the issued order");
        homePage.openMyOrderPage();
        sleep(50);
        orderListPage.openIssuedTicketViaDeeplink(orderID);

        step("Open refund menu");
        orderDetailPage.clickRefundButton();

        step("Choose refund reason");
        refundPage.chooseRefundReasonWebView();

        step("Click process refund button");
        refundPage.clickProcessRefundBtn();

        step("Submit refund");
        refundPage.submitRefundCorporateBilling();

        step("go to order list");
        refundPage.clickGoToOrderListBtnOnRefundPage();
        sleep(70);

        step("Check if the refunded order is being reflected on corporate dashboard");
        boolean isRefundOrderReflected = corporateApi.checkRefundList(orderID, approverUsername, approverPassword);
        Assertion.assertThat("Refund order is reflected on B2B corporate dashboard", isRefundOrderReflected, is(true));
    }

    @Author(names = {"Ashish"})
    @Module(name = "Refund")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void testLoginRefundHotelViaAdminAutoApproved() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testLoginRefundHotelViaAdminAutoApproved");
        BaseTest.paramsThreadLocal.set(param);

        String startDate = generateRandomDate(param.get("rangeAddDate"));
        String adminUsername = param.get("username");
        String adminPassword = param.get("password");

        String query = param.get("hotelName");
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));
        int room = Integer.parseInt(param.get("room"));

        String[] salutationList = { "Mr", "Mrs", "Ms" };
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = getRandomObject(salutationList);

        HotelPage<WebDriver> hotelPage = HotelPage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        RefundPage<WebDriver> refundPage = RefundPage.getInstance(session);

        step("Create new hotel issued order");
        String orderID = hotelApi.createIssuedOrderApiB2B(
                adminUsername, adminPassword, startDate, query, adult, night, room,"IDR",
                orderFullName, orderNameTitle, guestFullName, guestTitle, corporateApi.getEmployeeData(adminUsername), false,true);
        Assertion.assertThat("Hotel Booking via API is successful for B2B account", !orderID.isEmpty(), is(true));

        step("Goto home");
        homePage.gotoHome();

        step("Login as B2B admin account " + adminUsername + " via deeplink");
        homePage.loginViaDeeplink(adminUsername, adminPassword);

        step("Find the issued order " + orderID);
        homePage.openMyOrderPage();
        sleep(30);
        orderListPage.openIssuedTicketViaDeeplink(orderID);

        step("Open refund menu");
        orderDetailPage.clickRefundButton();

        step("Choose refund reason");
        refundPage.chooseRefundReasonWebView();

        step("Click process refund button");
        refundPage.clickProcessRefundBtn();

        step("Submit refund");
        refundPage.submitRefundCorporateBilling();

        step("go to order list");
        refundPage.clickGoToOrderListBtnOnRefundPage();
        sleep(70);

        step("Check if the refunded order is being reflected on corporate dashboard");
        boolean isRefundOrderReflected =corporateApi.checkRefundList(orderID, adminUsername, adminPassword);
        Assertion.assertThat("Refund order is reflected on B2B corporate dashboard", isRefundOrderReflected, is(true));
    }

    @Author(names = {"Ashish"})
    @Module(name = "Refund")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void testLoginRefundHotelTravelerViaApprover() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testLoginRefundHotelTravelerViaApprover");
        BaseTest.paramsThreadLocal.set(param);

        String username = param.get("username");
        String password = param.get("password");
        String startDate = generateRandomDate(param.get("rangeAddDate"));
        String approverUsername = param.get("approverEmail");
        String approverPassword = param.get("approverPassword");

        String query = param.get("hotelName");
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));
        int room = Integer.parseInt(param.get("room"));

        String[] salutationList = { "Mr", "Mrs", "Ms" };
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = getRandomObject(salutationList);

        HotelPage<WebDriver> hotelPage = HotelPage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        RefundPage<WebDriver> refundPage = RefundPage.getInstance(session);
        CorporateLoginPage<WebDriver> corporateLoginPage = CorporateLoginPage.getInstance(session);

        step("Create new hotel issued order");
        String orderID = hotelApi.createIssuedOrderApiB2B(
                username, password, startDate, query, adult, night, room,"IDR",
                orderFullName, orderNameTitle, guestFullName, guestTitle, corporateApi.getEmployeeData(username), false);
        Assertion.assertThat("Hotel Booking via API is successful for B2B account", !orderID.isEmpty(), is(true));
        sleep(5);

        step("Approve booking by approver account for Order ID: " + orderID);
        Boolean bookingStatus = corporateApi.approveBookingB2BApi(approverUsername, approverPassword, orderID);
        Assertion.assertThat("Booking approved by B2B dashboard", bookingStatus, is(true));

        step("Goto home");
        homePage.gotoHome();

        step("Log out");
        loginPage.loginAsGuest();

        step("B2B login");
        corporateLoginPage.corporateLogin(username,password);

        step("Find the issued order " + orderID);
        orderListPage.openIssuedTicketViaDeeplink(orderID);

        step("Open refund menu");
        orderDetailPage.clickRefundButton();

        step("Choose refund reason");
        refundPage.chooseRefundReasonWebView();

        step("Click process refund button");
        refundPage.clickProcessRefundBtn();

        step("Submit refund");
        refundPage.submitRefundCorporateBilling();
        sleep(20);

        step("check and Approve refund on dashboard");
        refundPage.waitForRefundPopUp();
        String refundId = corporateApi.getRefundId(orderID, approverUsername, approverPassword,"prerefund");
        Boolean isApproved = true;
        Boolean success = corporateApi.approveOrRejectRefundB2B(approverUsername, approverPassword, refundId, isApproved);
        Assertion.assertThat("Refund Approved Success", success, is(true));
    }

    @Author(names = {"Ashish"})
    @Module(name = "Refund")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void testRejectRefundRequestByTravelerViaApprover() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testRejectRefundRequestByTravelerViaApprover");
        BaseTest.paramsThreadLocal.set(param);

        String username = param.get("username");
        String password = param.get("password");
        String startDate = generateRandomDate(param.get("rangeAddDate"));
        String approverUsername = param.get("approverEmail");
        String approverPassword = param.get("approverPassword");

        String query = param.get("hotelName");
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));
        int room = Integer.parseInt(param.get("room"));

        String[] salutationList = { "Mr", "Mrs", "Ms" };
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = getRandomObject(salutationList);

        HotelPage<WebDriver> hotelPage = HotelPage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        RefundPage<WebDriver> refundPage = RefundPage.getInstance(session);
        CorporateLoginPage<WebDriver> corporateLoginPage = CorporateLoginPage.getInstance(session);

        String orderID;
        step("Create new hotel issued order");
        orderID = hotelApi.createIssuedOrderApiB2B(
                username, password, startDate, query, adult, night, room,"IDR",
                orderFullName, orderNameTitle, guestFullName, guestTitle, corporateApi.getEmployeeData(username), false);
        Assertion.assertThat("Hotel Booking via API is successful for B2B account", !orderID.isEmpty(), is(true));
        sleep(5);

        step("Approve booking by approver account for Order ID: " + orderID);
        Boolean bookingStatus = corporateApi.approveBookingB2BApi(approverUsername, approverPassword, orderID);
        Assertion.assertThat("Booking approved by B2B dashboard", bookingStatus, is(true));

        step("Goto home");
        homePage.gotoHome();

        step("Log out");
        loginPage.loginAsGuest();

        step("B2B login");
        corporateLoginPage.corporateLogin(username,password);

        step("Find the issued order");
        orderListPage.openIssuedTicketViaDeeplink(orderID);

        step("Open refund menu");
        orderDetailPage.clickRefundButton();

        step("Choose refund reason");
        refundPage.chooseRefundReasonWebView();

        step("Click process refund button");
        refundPage.clickProcessRefundBtn();

        step("Submit refund");
        refundPage.submitRefundCorporateBilling();
        sleep(20);

        step("Check if the refunded order is being reflected on corporate dashboard");
        String refundId = corporateApi.getRefundId(orderID, approverUsername, approverPassword,"prerefund");
        Boolean isApproved = false;
        Boolean success = corporateApi.approveOrRejectRefundB2B(approverUsername, approverPassword, refundId, isApproved);
        Assertion.assertThat("Refund Rejection Success", success, is(true));

        step("go to order list");
        refundPage.clickGoToOrderListBtnOnRefundPage();

        step("Open order from active ticket");
        orderListPage.openIssuedTicketViaDeeplink(orderID);

        Assertion.assertThat("Order is in Active order state", orderDetailPage.getOrderID(), is(orderID));
        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.getETicketLabel(), either(is("Your e-ticket is here")).or(is("E-tiketmu di sini")));
    }

    @Author(names = {"Ashish", "Prerna"})
    @Module(name = "Login")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void testCheckForgetYourPasswordFunctionality() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> testProperties = getParam("testCheckForgetYourPasswordFunctionality");
        BaseTest.paramsThreadLocal.set(testProperties);

        String email = testProperties.get("email");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        CorporateLoginPage<WebDriver> corporateLoginPage = CorporateLoginPage.getInstance(session);

        step("User open app");
        homePage.gotoHome();
        loginPage.loginAsGuest();

        step("change password using forget password functionality");
        String updatedPassword = corporateLoginPage.forgetPasswordB2B(email, "Testing@1");

        Assertion.assertThat("is password changed","Testing@1", not(updatedPassword));

        step("go to home");
        homePage.gotoHome();

        step("Verify the corp login page with traveler credentials " + email);
        corporateLoginPage.corporateLogin(email, updatedPassword);

        step("Verify Step");
        Assertion.assertThat("User is on login state", homePage.isUserLogin(), is(true));
    }

    @Author(names = {"Prerna"})
    @Module(name = "Booking")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void testFlightBookingFromAdminbeAutoapproved() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> testProperties = getParam("testFlightBookingFromAdminbeAutoapproved");
        BaseTest.paramsThreadLocal.set(testProperties);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        FlightApi<?> flightApi = new FlightApi<>(session);

        String username = testProperties.get("email");
        String password = testProperties.get("password");
        String originStation = testProperties.get("originStation");
        String destinationStation = testProperties.get("destinationStation");
        int daysToDepartureDate = Integer.parseInt(testProperties.get("daysToDepartureDate"));
        String searchType = testProperties.get("searchType");
        int adult = Integer.parseInt(testProperties.get("adult"));
        int child = Integer.parseInt(testProperties.get("child"));
        int infant = Integer.parseInt(testProperties.get("infant"));
        String firstName = testProperties.get("firstName");
        String lastName = testProperties.get("lastName");
        ArrayList<String> airlines = CoreFunctions.makeAirlineArrayList(testProperties.get("airline"));

        step("Find dynamic data");
        FlightDynamicData flightDynamicData = flightApi.findDateWithMockIssuedAirline(
                username,password,
                originStation,destinationStation,daysToDepartureDate,
                adult,child,infant,
                searchType,airlines,"","","IDR"
        );
        String departureDate = flightDynamicData.departureDate;
        String airline = flightDynamicData.airlineDepart;

        step("complete flight booking via API");
        String searchTypeApi = searchType.equalsIgnoreCase("oneway") ? "ONE_WAY"
                : searchType.equalsIgnoreCase("roundtrip") ? "ROUND_TRIP"
                : searchType;
        String orderID = flightApi.createOrderFlightB2BDomestic(username, password, originStation, destinationStation, departureDate, searchTypeApi, adult, child, infant, airline,corporateApi.getEmployeeData(username), firstName, lastName);
        Assertion.assertThat("Flight order created", orderID, not(""));

        step("Goto home with order_id " + orderID);
        homePage.gotoHome();

        step("Login as B2B account " + username + " via deeplink");
        homePage.loginViaDeeplink(username, password);

        step("Open the order list and go to order detail");
        homePage.openMyOrderPage();

        step("check order status");
        orderListPage.openOrderDetailViaDeeplink(orderID);
        Assertion.assertThat("e-Ticket on the order detail should be {wait for confirmation} or {Your e-ticket is here}", orderDetailPage.getETicketLabel(), either(is("Your e-ticket is here")).or(is("E-tiketmu di sini")).or(is("Waiting for confirmation")).or(is("Menunggu konfirmasi")).or(is("Processing e-ticket")).or(is("Memproses e-tiket")));
    }

    @Author(names = {"Prerna"})
    @Module(name = "Refund")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void testFlightRefundFromAdminWillBeAutoapproved(){
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> testProperties = getParam("testFlightRefundFromAdminWillBeAutoapproved");
        BaseTest.paramsThreadLocal.set(testProperties);

        String email = testProperties.get("email");
        String password = testProperties.get("password");
        String originStation = testProperties.get("originStation");
        String destinationStation = testProperties.get("destinationStation");
        int daysToDepartureDate = Integer.parseInt(testProperties.get("daysToDepartureDate"));
        String cabinClass = testProperties.get("cabinClass");
        String originType = testProperties.get("originType");
        String destinationType = testProperties.get("destinationType");
        String searchType = testProperties.get("searchType");
        int adult = Integer.parseInt(testProperties.get("adult"));
        int child = Integer.parseInt(testProperties.get("child"));
        int infant = Integer.parseInt(testProperties.get("infant"));
        String airline = testProperties.get("airline");
        String firstName = testProperties.get("firstName");
        String lastName = testProperties.get("lastName");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        CorporateLoginPage<WebDriver> corporateLoginPage = CorporateLoginPage.getInstance(session);
        FlightPage<WebDriver> flightPage = FlightPage.getInstance(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailtPage = OrderDetailPage.getInstance(session);
        RefundPage<WebDriver> refundPage = RefundPage.getInstance(session);
        FlightApi<?> flightApi = new FlightApi<>(session);

        step("Find dynamic data");
        FlightDynamicData flightDynamicData = flightApi.findDate(
                email,password,
                originStation,destinationStation,daysToDepartureDate,
                adult,child,infant,
                searchType,airline,"","","",""
        );
        originStation = flightDynamicData.originAirport;
        destinationStation = flightDynamicData.destinationAirport;
        String departureDate = CoreFunctions.convertToLongDate(flightDynamicData.departureDate);
        airline = flightDynamicData.airlineDepart;

        if (Configs.ENVIRONMENT == Environment.PREPROD) {
            step("Validate all suppliers are mock-issued");
            flightApi.validateAllSuppliersAreMockIssuedByAirline(flightDynamicData.airlineCodeDepart, flightDynamicData.originAirport, flightDynamicData.destinationAirport);
        }

        step("complete flight booking via API");
        String orderID = flightPage.createOrderFlightB2BDomestic(email, password, originStation, destinationStation, departureDate, cabinClass, originType, destinationType, searchType, adult, child, infant, airline,corporateApi.getEmployeeData(email), firstName, lastName);
        Assertion.assertThat("Flight order created", orderID, not(""));

        step("Goto home");
        homePage.gotoHome();

        step("Log out");
        loginPage.loginAsGuest();

        step("B2B login" + email);
        corporateLoginPage.corporateLogin(email,password);

        step("get booking code from API");
        String bookingCode = flightPage.getBookingCode(orderID, email);
        log(bookingCode);

        step("set Tiket Number on flight dashboard and ticket will be eticket issued");
        Boolean isTicketIssued = flightPage.flightEticketIssuedRequest(orderID, bookingCode, email);
        Assertion.assertThat("is Eticket Issued", isTicketIssued, is(true));

        step("Open the order list and go to order detail");
        homePage.openMyOrderPage();

        step("open order detail");
        orderListPage.openIssuedTicketViaDeeplink(orderID);

        step("Open refund menu");
        orderDetailtPage.clickRefundButton();

        step("Choose refund reason");
        refundPage.chooseRefundReasonWebView();

        step("Click process refund button");
        refundPage.clickProcessRefundBtn();

        step("Choose specific refund reason");
        refundPage.chooseRefundReasonWebView();

        step("click continue");
        refundPage.chooseRefundReasonWebView();

        step("Submit refund");
        refundPage.submitRefundCorporateBilling();

        step("go to order list");
        refundPage.clickGoToOrderListBtnOnRefundPage();
        sleep(70);

        step("Check if the refunded order is being reflected on corporate dashboard");
        boolean isRefundOrderReflected = corporateApi.checkRefundList(orderID, email, password);
        Assertion.assertThat("Refund order is reflected on B2B corporate dashboard", isRefundOrderReflected, is(true));
    }

    @Author(names = {"Prerna"})
    @Module(name = "Refund")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void testFlightRefundFromApproverWillBeAutoapproved(){
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> testProperties = getParam("testFlightRefundFromApproverWillBeAutoapproved");
        BaseTest.paramsThreadLocal.set(testProperties);

        String email = testProperties.get("email");
        String password = testProperties.get("password");
        String originStation = testProperties.get("originStation");
        String destinationStation = testProperties.get("destinationStation");
        int daysToDepartureDate = Integer.parseInt(testProperties.get("daysToDepartureDate"));
        String cabinClass = testProperties.get("cabinClass");
        String originType = testProperties.get("originType");
        String destinationType = testProperties.get("destinationType");
        String searchType = testProperties.get("searchType");
        int adult = Integer.parseInt(testProperties.get("adult"));
        int child = Integer.parseInt(testProperties.get("child"));
        int infant = Integer.parseInt(testProperties.get("infant"));
        String airline = testProperties.get("airline");
        String firstName = testProperties.get("firstName");
        String lastName = testProperties.get("lastName");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        CorporateLoginPage<WebDriver> corporateLoginPage = CorporateLoginPage.getInstance(session);
        FlightPage<WebDriver> flightPage = FlightPage.getInstance(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailtPage = OrderDetailPage.getInstance(session);
        RefundPage<WebDriver> refundPage = RefundPage.getInstance(session);
        FlightApi<?> flightApi = new FlightApi<>(session);

        step("Find dynamic data");
        FlightDynamicData flightDynamicData = flightApi.findDate(
                email,password,
                originStation,destinationStation,daysToDepartureDate,
                adult,child,infant,
                searchType,airline,"","","",""
        );
        originStation = flightDynamicData.originAirport;
        destinationStation = flightDynamicData.destinationAirport;
        String departureDate = CoreFunctions.convertToLongDate(flightDynamicData.departureDate);
        airline = flightDynamicData.airlineDepart;

        if (Configs.ENVIRONMENT == Environment.PREPROD) {
            step("Validate all suppliers are mock-issued");
            flightApi.validateAllSuppliersAreMockIssuedByAirline(flightDynamicData.airlineCodeDepart, flightDynamicData.originAirport, flightDynamicData.destinationAirport);
        }

        step("complete flight booking via API");
        String orderID = flightPage.createOrderFlightB2BDomestic(email, password, originStation, destinationStation, departureDate, cabinClass, originType, destinationType, searchType, adult, child, infant, airline,corporateApi.getEmployeeData(email), firstName, lastName);
        Assertion.assertThat("Flight order created", orderID, not(""));

        step("Goto home");
        homePage.gotoHome();

        step("Log out");
        loginPage.loginAsGuest();

        step("B2B login" + email);
        corporateLoginPage.corporateLogin(email,password);

        step("get booking code from API");
        String bookingCode = flightPage.getBookingCode(orderID, email);
        log(bookingCode);

        step("set Tiket Number on flight dashboard and ticket will be eticket issued");
        Boolean isTicketIssued = flightPage.flightEticketIssuedRequest(orderID, bookingCode, email);
        Assertion.assertThat("is Eticket Issued", isTicketIssued, is(true));

        step("Open the order list and go to order detail");
        homePage.openMyOrderPage();

        step("open order detail");
        orderListPage.openIssuedTicketViaDeeplink(orderID);

        step("Open refund menu");
        orderDetailtPage.clickRefundButton();

        step("Choose refund reason");
        refundPage.chooseRefundReasonWebView();

        step("Click process refund button");
        refundPage.clickProcessRefundBtn();

        step("Choose specific refund reason");
        refundPage.chooseRefundReasonWebView();

        step("click continue");
        refundPage.chooseRefundReasonWebView();

        step("Submit refund");
        refundPage.submitRefundCorporateBilling();

        step("go to order list");
        refundPage.clickGoToOrderListBtnOnRefundPage();
        sleep(70);

        step("Check if the refunded order is being reflected on corporate dashboard");
        boolean isRefundOrderReflected = corporateApi.checkRefundList(orderID, email, password);
        Assertion.assertThat("Refund order is reflected on B2B corporate dashboard", isRefundOrderReflected, is(true));
    }

    @Author(names = {"Prerna"})
    @Module(name = "Refund")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void testFlightRefundFromTravelerApprovedByApprover(){
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> testProperties = getParam("testFlightRefundFromTravelerApprovedByApprover");
        BaseTest.paramsThreadLocal.set(testProperties);

        String email = testProperties.get("email");
        String password = testProperties.get("password");
        String originStation = testProperties.get("originStation");
        String destinationStation = testProperties.get("destinationStation");
        int daysToDepartureDate = Integer.parseInt(testProperties.get("daysToDepartureDate"));
        String cabinClass = testProperties.get("cabinClass");
        String originType = testProperties.get("originType");
        String destinationType = testProperties.get("destinationType");
        String searchType = testProperties.get("searchType");
        int adult = Integer.parseInt(testProperties.get("adult"));
        int child = Integer.parseInt(testProperties.get("child"));
        int infant = Integer.parseInt(testProperties.get("infant"));
        String airline = testProperties.get("airline");
        String firstName = testProperties.get("firstName");
        String lastName = testProperties.get("lastName");
        String approverUsername = testProperties.get("approverUsername");
        String approverPassword = testProperties.get("approverPassword");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        CorporateLoginPage<WebDriver> corporateLoginPage = CorporateLoginPage.getInstance(session);
        FlightPage<WebDriver> flightPage = FlightPage.getInstance(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailtPage = OrderDetailPage.getInstance(session);
        RefundPage<WebDriver> refundPage = RefundPage.getInstance(session);
        FlightApi<?> flightApi = new FlightApi<>(session);

        step("Find dynamic data");
        FlightDynamicData flightDynamicData = flightApi.findDate(
                email,password,
                originStation,destinationStation,daysToDepartureDate,
                adult,child,infant,
                searchType,airline,"","","",""
        );
        originStation = flightDynamicData.originAirport;
        destinationStation = flightDynamicData.destinationAirport;
        String departureDate = CoreFunctions.convertToLongDate(flightDynamicData.departureDate);
        airline = flightDynamicData.airlineDepart;

        if (Configs.ENVIRONMENT == Environment.PREPROD) {
            step("Validate all suppliers are mock-issued");
            flightApi.validateAllSuppliersAreMockIssuedByAirline(flightDynamicData.airlineCodeDepart, flightDynamicData.originAirport, flightDynamicData.destinationAirport);
        }

        step("complete flight booking via API");
        String orderID = flightPage.createOrderFlightB2BDomestic(email, password, originStation, destinationStation, departureDate, cabinClass, originType, destinationType, searchType, adult, child, infant, airline,corporateApi.getEmployeeData(email), firstName, lastName);
        Assertion.assertThat("Flight order created", orderID, not(""));

        step("Approve booking by approver account for Order ID: " + orderID);
        Boolean bookingStatus = corporateApi.approveBookingB2BApi(approverUsername, approverPassword, orderID);
        Assertion.assertThat("Booking approved by B2B dashboard", bookingStatus, is(true));

        step("Goto home");
        homePage.gotoHome();

        step("Log out");
        loginPage.loginAsGuest();

        step("B2B login" + email);
        corporateLoginPage.corporateLogin(email,password);

        step("get booking code from API");
        String bookingCode = flightPage.getBookingCode(orderID, email);
        log(bookingCode);

        step("set Tiket Number on flight dashboard and ticket will be eticket issued");
        Boolean isTicketIssued = flightPage.flightEticketIssuedRequest(orderID, bookingCode, email);
        Assertion.assertThat("is Eticket Issued", isTicketIssued, is(true));

        step("Open the order list and go to order detail");
        homePage.openMyOrderPage();

        step("open order detail");
        orderListPage.openIssuedTicketViaDeeplink(orderID);

        step("Open refund menu");
        orderDetailtPage.clickRefundButton();

        step("Choose refund reason");
        refundPage.chooseRefundReasonWebView();

        step("Click process refund button");
        refundPage.clickProcessRefundBtn();

        step("Choose specific refund reason");
        refundPage.chooseRefundReasonWebView();

        step("click continue");
        refundPage.chooseRefundReasonWebView();

        step("Submit refund");
        refundPage.submitRefundCorporateBilling();

        step("Approver refund from "+ approverUsername);
        String refundId = corporateApi.getRefundId(orderID, approverUsername, approverPassword,"prerefund");
        Boolean isApproved = true;
        Boolean success = corporateApi.approveOrRejectRefundB2B(approverUsername, approverPassword, refundId, isApproved);
        Assertion.assertThat("Refund Approved Success", success, is(true));

        step("go to order list");
        refundPage.clickGoToOrderListBtnOnRefundPage();
        sleep(70);

        step("Check if the refunded order is being reflected on corporate dashboard");
        boolean isRefundOrderReflected = corporateApi.checkRefundList(orderID, email, password);
        Assertion.assertThat("Refund order is reflected on B2B corporate dashboard", isRefundOrderReflected, is(true));
    }

    @Author(names = {"Prerna"})
    @Module(name = "Refund")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void testFlightRefundFromTravelerApprovedByAdmin(){
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> testProperties = getParam("testFlightRefundFromTravelerApprovedByAdmin");
        BaseTest.paramsThreadLocal.set(testProperties);

        String email = testProperties.get("email");
        String password = testProperties.get("password");
        String originStation = testProperties.get("originStation");
        String destinationStation = testProperties.get("destinationStation");
        int daysToDepartureDate = Integer.parseInt(testProperties.get("daysToDepartureDate"));
        String cabinClass = testProperties.get("cabinClass");
        String originType = testProperties.get("originType");
        String destinationType = testProperties.get("destinationType");
        String searchType = testProperties.get("searchType");
        int adult = Integer.parseInt(testProperties.get("adult"));
        int child = Integer.parseInt(testProperties.get("child"));
        int infant = Integer.parseInt(testProperties.get("infant"));
        String airline = testProperties.get("airline");
        String firstName = testProperties.get("firstName");
        String lastName = testProperties.get("lastName");
        String adminUsername = testProperties.get("adminUsername");
        String adminPassword = testProperties.get("adminPassword");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        CorporateLoginPage<WebDriver> corporateLoginPage = CorporateLoginPage.getInstance(session);
        FlightPage<WebDriver> flightPage = FlightPage.getInstance(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailtPage = OrderDetailPage.getInstance(session);
        RefundPage<WebDriver> refundPage = RefundPage.getInstance(session);
        FlightApi<?> flightApi = new FlightApi<>(session);

        step("Find dynamic data");
        FlightDynamicData flightDynamicData = flightApi.findDate(
                email,password,
                originStation,destinationStation,daysToDepartureDate,
                adult,child,infant,
                searchType,airline,"","","",""
        );
        originStation = flightDynamicData.originAirport;
        destinationStation = flightDynamicData.destinationAirport;
        String departureDate = CoreFunctions.convertToLongDate(flightDynamicData.departureDate);
        airline = flightDynamicData.airlineDepart;

        if (Configs.ENVIRONMENT == Environment.PREPROD) {
            step("Validate all suppliers are mock-issued");
            flightApi.validateAllSuppliersAreMockIssuedByAirline(flightDynamicData.airlineCodeDepart, flightDynamicData.originAirport, flightDynamicData.destinationAirport);
        }

        step("complete flight booking via API");
        String orderID = flightPage.createOrderFlightB2BDomestic(email, password, originStation, destinationStation, departureDate, cabinClass, originType, destinationType, searchType, adult, child, infant, airline,corporateApi.getEmployeeData(email), firstName, lastName);
        Assertion.assertThat("Flight order created", orderID, not(""));

        step("Approve booking by "+adminUsername+" account for Order ID: " + orderID);
        Boolean bookingStatus = corporateApi.approveBookingB2BApi(adminUsername, adminPassword, orderID);
        Assertion.assertThat("Booking approved by B2B dashboard", bookingStatus, is(true));

        step("Goto home");
        homePage.gotoHome();

        step("Log out");
        loginPage.loginAsGuest();

        step("B2B login" + email);
        corporateLoginPage.corporateLogin(email,password);

        step("get booking code from API");
        String bookingCode = flightPage.getBookingCode(orderID, email);
        log(bookingCode);

        step("set Tiket Number on flight dashboard and ticket will be eticket issued");
        Boolean isTicketIssued = flightPage.flightEticketIssuedRequest(orderID, bookingCode, email);
        Assertion.assertThat("is Eticket Issued", isTicketIssued, is(true));

        step("Open the order list and go to order detail");
        homePage.openMyOrderPage();

        step("open order detail");
        orderListPage.openIssuedTicketViaDeeplink(orderID);

        step("Open refund menu");
        orderDetailtPage.clickRefundButton();

        step("Choose refund reason");
        refundPage.chooseRefundReasonWebView();

        step("Click process refund button");
        refundPage.clickProcessRefundBtn();

        step("Choose specific refund reason");
        refundPage.chooseRefundReasonWebView();

        step("click continue");
        refundPage.chooseRefundReasonWebView();

        step("Submit refund");
        refundPage.submitRefundCorporateBilling();

        step("Approver refund from "+ adminUsername);
        String refundId = corporateApi.getRefundId(orderID, adminUsername, adminPassword,"prerefund");
        Boolean isApproved = true;
        Boolean success = corporateApi.approveOrRejectRefundB2B(adminUsername, adminPassword, refundId, isApproved);
        Assertion.assertThat("Refund Approved Success", success, is(true));

        step("go to order list");
        refundPage.clickGoToOrderListBtnOnRefundPage();
        sleep(70);

        step("Check if the refunded order is being reflected on corporate dashboard");
        boolean isRefundOrderReflected = corporateApi.checkRefundList(orderID, email, password);
        Assertion.assertThat("Refund order is reflected on B2B corporate dashboard", isRefundOrderReflected, is(true));
    }

    @Author(names = {"Prerna"})
    @Module(name = "Refund")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void testFlightRefundFromTravelerRejectedByApprover(){
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> testProperties = getParam("testFlightRefundFromTravelerRejectedByApprover");
        BaseTest.paramsThreadLocal.set(testProperties);

        String email = testProperties.get("email");
        String password = testProperties.get("password");
        String originStation = testProperties.get("originStation");
        String destinationStation = testProperties.get("destinationStation");
        int daysToDepartureDate = Integer.parseInt(testProperties.get("daysToDepartureDate"));
        String cabinClass = testProperties.get("cabinClass");
        String originType = testProperties.get("originType");
        String destinationType = testProperties.get("destinationType");
        String searchType = testProperties.get("searchType");
        int adult = Integer.parseInt(testProperties.get("adult"));
        int child = Integer.parseInt(testProperties.get("child"));
        int infant = Integer.parseInt(testProperties.get("infant"));
        String airline = testProperties.get("airline");
        String firstName = testProperties.get("firstName");
        String lastName = testProperties.get("lastName");
        String approverUsername = testProperties.get("approverUsername");
        String approverPassword = testProperties.get("approverPassword");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        CorporateLoginPage<WebDriver> corporateLoginPage = CorporateLoginPage.getInstance(session);
        FlightPage<WebDriver> flightPage = FlightPage.getInstance(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        RefundPage<WebDriver> refundPage = RefundPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        FlightApi<?> flightApi = new FlightApi<>(session);

        step("Find dynamic data");
        FlightDynamicData flightDynamicData = flightApi.findDate(
                email,password,
                originStation,destinationStation,daysToDepartureDate,
                adult,child,infant,
                searchType,airline,"","","",""
        );
        originStation = flightDynamicData.originAirport;
        destinationStation = flightDynamicData.destinationAirport;
        String departureDate = CoreFunctions.convertToLongDate(flightDynamicData.departureDate);
        airline = flightDynamicData.airlineDepart;

        if (Configs.ENVIRONMENT == Environment.PREPROD) {
            step("Validate all suppliers are mock-issued");
            flightApi.validateAllSuppliersAreMockIssuedByAirline(flightDynamicData.airlineCodeDepart, flightDynamicData.originAirport, flightDynamicData.destinationAirport);
        }

        step("complete flight booking via API");
        String orderID = flightPage.createOrderFlightB2BDomestic(email, password, originStation, destinationStation, departureDate, cabinClass, originType, destinationType, searchType, adult, child, infant, airline,corporateApi.getEmployeeData(email), firstName, lastName);
        Assertion.assertThat("Flight order created", orderID, not(""));

        step("Approve booking by approver account for Order ID: " + orderID);
        Boolean bookingStatus = corporateApi.approveBookingB2BApi(approverUsername, approverPassword, orderID);
        Assertion.assertThat("Booking approved by B2B dashboard", bookingStatus, is(true));

        step("Goto home");
        homePage.gotoHome();

        step("Log out");
        loginPage.loginAsGuest();

        step("B2B login" + email);
        corporateLoginPage.corporateLogin(email,password);

        step("get booking code from API");
        String bookingCode = flightPage.getBookingCode(orderID, email);
        log(bookingCode);

        step("set Tiket Number on flight dashboard and ticket will be eticket issued");
        Boolean isTicketIssued = flightPage.flightEticketIssuedRequest(orderID, bookingCode, email);
        Assertion.assertThat("is Eticket Issued", isTicketIssued, is(true));

        step("Open the order list and go to order detail");
        homePage.openMyOrderPage();

        step("open order detail");
        orderListPage.openIssuedTicketViaDeeplink(orderID);

        step("Open refund menu");
        orderDetailPage.clickRefundButton();

        step("Choose refund reason");
        refundPage.chooseRefundReasonWebView();

        step("Click process refund button");
        refundPage.clickProcessRefundBtn();

        step("Choose specific refund reason");
        refundPage.chooseRefundReasonWebView();

        step("click continue");
        refundPage.chooseRefundReasonWebView();

        step("Submit refund");
        refundPage.submitRefundCorporateBilling();

        step("order Rejected by "+ approverUsername);
        String refundId = corporateApi.getRefundId(orderID, approverUsername, approverPassword,"prerefund");
        Boolean isApproved = false;
        Boolean success = corporateApi.approveOrRejectRefundB2B(approverUsername, approverPassword, refundId, isApproved);
        Assertion.assertThat("Refund Rejection Success", success, is(true));

        step("go to order list");
        refundPage.clickGoToOrderListBtnOnRefundPage();

        step("Open order from active ticket");
        orderListPage.openIssuedTicketViaDeeplink(orderID);

        Assertion.assertThat("Order is in Active order state", orderDetailPage.getOrderID(), is(orderID));
        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.getETicketLabel(), either(is("Your e-ticket is here")).or(is("E-tiketmu di sini")));
    }

    @Author(names = {"Prerna"})
    @Module(name = "Refund")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void testFlightRefundFromTravelerRejectedByAdmin(){
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> testProperties = getParam("testFlightRefundFromTravelerRejectedByAdmin");
        BaseTest.paramsThreadLocal.set(testProperties);

        String email = testProperties.get("email");
        String password = testProperties.get("password");
        String originStation = testProperties.get("originStation");
        String destinationStation = testProperties.get("destinationStation");
        int daysToDepartureDate = Integer.parseInt(testProperties.get("daysToDepartureDate"));
        String cabinClass = testProperties.get("cabinClass");
        String originType = testProperties.get("originType");
        String destinationType = testProperties.get("destinationType");
        String searchType = testProperties.get("searchType");
        int adult = Integer.parseInt(testProperties.get("adult"));
        int child = Integer.parseInt(testProperties.get("child"));
        int infant = Integer.parseInt(testProperties.get("infant"));
        String airline = testProperties.get("airline");
        String firstName = testProperties.get("firstName");
        String lastName = testProperties.get("lastName");
        String adminUsername = testProperties.get("adminUsername");
        String adminPassword = testProperties.get("adminPassword");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        CorporateLoginPage<WebDriver> corporateLoginPage = CorporateLoginPage.getInstance(session);
        FlightPage<WebDriver> flightPage = FlightPage.getInstance(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailtPage = OrderDetailPage.getInstance(session);
        RefundPage<WebDriver> refundPage = RefundPage.getInstance(session);
        FlightApi<?> flightApi = new FlightApi<WebDriver>(session);

        step("Find dynamic data");
        FlightDynamicData flightDynamicData = flightApi.findDate(
                email,password,
                originStation,destinationStation,daysToDepartureDate,
                adult,child,infant,
                searchType,airline,"","","",""
        );
        originStation = flightDynamicData.originAirport;
        destinationStation = flightDynamicData.destinationAirport;
        String departureDate = CoreFunctions.convertToLongDate(flightDynamicData.departureDate);
        airline = flightDynamicData.airlineDepart;

        if (Configs.ENVIRONMENT == Environment.PREPROD) {
            step("Validate all suppliers are mock-issued");
            flightApi.validateAllSuppliersAreMockIssuedByAirline(flightDynamicData.airlineCodeDepart, flightDynamicData.originAirport, flightDynamicData.destinationAirport);
        }

        step("complete flight booking via API");
        String orderID = flightPage.createOrderFlightB2BDomestic(email, password, originStation, destinationStation, departureDate, cabinClass, originType, destinationType, searchType, adult, child, infant, airline,corporateApi.getEmployeeData(email), firstName, lastName);
        Assertion.assertThat("Flight order created", orderID, not(""));

        step("Approve booking by approver account for Order ID: " + orderID);
        Boolean bookingStatus = corporateApi.approveBookingB2BApi(adminUsername, adminPassword, orderID);
        Assertion.assertThat("Booking approved by B2B dashboard", bookingStatus, is(true));

        step("Goto home");
        homePage.gotoHome();

        step("Log out");
        loginPage.loginAsGuest();

        step("B2B login" + email);
        corporateLoginPage.corporateLogin(email,password);

        step("get booking code from API");
        String bookingCode = flightPage.getBookingCode(orderID, email);
        log(bookingCode);

        step("set Tiket Number on flight dashboard and ticket will be eticket issued");
        Boolean isTicketIssued = flightPage.flightEticketIssuedRequest(orderID, bookingCode, email);
        Assertion.assertThat("is Eticket Issued", isTicketIssued, is(true));

        step("Open the order list and go to order detail");
        homePage.openMyOrderPage();

        step("open order detail");
        orderListPage.openIssuedTicketViaDeeplink(orderID);

        step("Open refund menu");
        orderDetailtPage.clickRefundButton();

        step("Choose refund reason");
        refundPage.chooseRefundReasonWebView();

        step("Click process refund button");
        refundPage.clickProcessRefundBtn();

        step("Choose specific refund reason");
        refundPage.chooseRefundReasonWebView();

        step("click continue");
        refundPage.chooseRefundReasonWebView();

        step("Submit refund");
        refundPage.submitRefundCorporateBilling();

        step("order Rejected by "+ adminUsername);
        String refundId = corporateApi.getRefundId(orderID, adminUsername, adminPassword,"prerefund");
        Boolean isApproved = false;
        Boolean success = corporateApi.approveOrRejectRefundB2B(adminUsername, adminPassword, refundId, isApproved);
        Assertion.assertThat("Refund Rejection Success", success, is(true));

        step("go to order list");
        refundPage.clickGoToOrderListBtnOnRefundPage();

        step("Open order from active ticket");
        orderListPage.openIssuedTicketViaDeeplink(orderID);

        Assertion.assertThat("Order is in Active order state", orderDetailtPage.getOrderID(), is(orderID));
        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailtPage.getETicketLabel(), either(is("Your e-ticket is here")).or(is("E-tiketmu di sini")));
    }

    @Author(names = {"Ashish"})
    @Module(name = "Reschedule")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void testAdminRequestRescheduleHotelAutoApprove() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testAdminRequestRescheduleHotelAutoApprove");
        BaseTest.paramsThreadLocal.set(param);

        String username = param.get("username");
        String password = param.get("password");
        String startDate = generateRandomDate(param.get("rangeAddDate"));

        String query = param.get("hotelName");
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));
        int room = Integer.parseInt(param.get("room"));

        String[] salutationList = { "Mr", "Mrs", "Ms" };
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = getRandomObject(salutationList);

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        HotelPage<WebDriver> hotelPage = HotelPage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        HotelReschedulePageWeb<?> hotelReschedulePageWeb = HotelReschedulePageWeb.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);

        String orderID;
        step("Create new hotel issued order");
        orderID = hotelApi.createIssuedOrderApiB2B(
                username, password, startDate, query, adult, night, room, "IDR",
                orderFullName, orderNameTitle, guestFullName, guestTitle,
                corporateApi.getEmployeeData(username),
                /* selectRoomMostExpansive */ false,
                /* isRefundable */ true);
        Assertion.assertThat("Hotel Booking via API is successful for B2B account", !orderID.isEmpty(), is(true));
        orderListPage.waitForPaymentSuccessful(orderID,15);

        step("Goto home");
        homePage.gotoHome();

        step("Login as B2B admin account " + username + " via deeplink");
        homePage.loginViaDeeplink(username, password);

        step("Open my order page");
        homePage.openMyOrderPage();

        step("Find the issued order");
        orderListPage.refreshTheOrderList(2);
        orderListPage.openIssuedTicketViaDeeplink(orderID);

        step("Choose reschedule");
        orderDetailPage.clickReschedule(orderID);

        step("Choose new date for the reschedule");
        webViewPage.switchToWebViewUrl("bookings/reschedule");
        String newStartDate = hotelReschedulePageWeb.changeNightDuration(1);

        step("Choose a new room type");
        webViewPage.switchToWebViewUrl("bookings/reschedule");
        String newRoomName = hotelReschedulePageWeb.getRoomName();
        HotelReschedulePageWeb.RoomPriceInfo priceInfo = hotelReschedulePageWeb.bookFirstAvailableRoom();
        boolean isPaymentFree = priceInfo.state() != HotelReschedulePageWeb.RoomPriceState.ADDITIONAL;


        step("Submit booking form");
        webViewPage.switchToWebViewUrl("reschedule/checkout");
        hotelReschedulePageWeb.submitRescheduleBookingForm();

        step("Submit the payment method");
        webViewPage.switchToNative();
        String orderIDNew = paymentPage.checkOrderID();
        paymentPage.selectRefundAndClickPay();
        paymentPage.clickOnCheckMarkAndClickContinue(isPaymentFree);

         step("wait for order auto approve");
         orderListPage.waitForPaymentSuccessful(orderIDNew,15);

         step("see order list");
         paymentPage.seeOrderList(orderIDNew, isPaymentFree);

         step("verify the new order detail");
         orderListPage.refreshTheOrderList(2);
         orderListPage.openIssuedTicketViaDeeplink(orderIDNew);
         Assertion.assertThat("Reschedule card notification",orderDetailPage.isRescheduleCardDisplayed(orderIDNew), is(true));
         // The new iOS UI surfaces only the orderDetailId on the order detail page
         // (the parent orderId is no longer rendered natively). Resolve the expected
         String orderDetailIdNew = orderListPage.getOrderDetailIdByOrderId(orderIDNew);
         Assertion.assertThat("Order Detail ID is equal", orderDetailPage.getOrderDetailID(), is(orderDetailIdNew));
         Assertion.assertThat("Start Date is equal", orderDetailPage.getStartDate(orderIDNew), is(newStartDate));
         Assertion.assertThat("Hotel Name is displayed correctly", orderDetailPage.isHotelNameDisplayed(query), is(true));
         Assertion.assertThat("Room Name is displayed correctly", orderDetailPage.isRoomNameDisplayed(newRoomName), is(true));
     }

    @Author(names = {"Ashish"})
    @Module(name = "Reschedule")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void testApproverRequestRescheduleHotelAutoApprove() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testApproverRequestRescheduleHotelAutoApprove");
        BaseTest.paramsThreadLocal.set(param);

        String approverUsername = param.get("username");
        String approverPassword = param.get("password");
        String startDate = generateRandomDate(param.get("rangeAddDate"));

        String query = param.get("hotelName");
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));
        int room = Integer.parseInt(param.get("room"));

        String[] salutationList = { "Mr", "Mrs", "Ms" };
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = getRandomObject(salutationList);

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        HotelPage<WebDriver> hotelPage = HotelPage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        HotelReschedulePage<WebDriver> hotelReschedulePage = HotelReschedulePage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);
        CorporateLoginPage<WebDriver> corporateLoginPage = CorporateLoginPage.getInstance(session);

        String orderID;
        step("Create new hotel issued order");
        orderID = hotelApi.createIssuedOrderApiB2B(
                approverUsername, approverPassword, startDate, query, adult, night, room,"IDR",
                orderFullName, orderNameTitle, guestFullName, guestTitle, corporateApi.getEmployeeData(approverUsername), false);
        Assertion.assertThat("Hotel Booking via API is successful for B2B account", !orderID.isEmpty(), is(true));
        sleep(50);

        step("Goto home");
        homePage.gotoHome();

        step("Log out");
        loginPage.loginAsGuest();

        step("B2B login");
        corporateLoginPage.corporateLogin(approverUsername,approverPassword);

        step("Open my order page");
        homePage.openMyOrderPage();

        step("Find the issued order");
        orderListPage.refreshTheOrderList(2);
        orderListPage.openIssuedTicketViaDeeplink(orderID);

         step("Choose reschedule");
         orderDetailPage.clickReschedule(orderID);

        step("Choose new date for the reschedule");
        String newStartDate = hotelReschedulePage.chooseNewDate(false);

        step("Choose a new room type with additional Price");
        ChooseFirstRoomData chooseFirstRoomData = roomListPage.chooseFirstRoom();

        step("Submit booking form");
        bookingFormPage.submitRescheduleBookingForm();

        step("Submit the payment method");
        String orderIDNew = paymentPage.checkOrderID();
        paymentPage.selectRefundAndClickPay();
        paymentPage.clickOnCheckMarkAndClickContinue(chooseFirstRoomData.isFreeRoom);

         step("wait for order auto approve");
         sleep(70);

         step("see order list");
         paymentPage.seeOrderList(orderIDNew, chooseFirstRoomData.isFreeRoom);

         step("verify the new order detail");
         orderListPage.refreshTheOrderList(2);
         orderListPage.openIssuedTicketViaDeeplink(orderIDNew);
         Assertion.assertThat("Reschedule card notification",orderDetailPage.isRescheduleCardDisplayed(orderIDNew), is(true));
         Assertion.assertThat("Order ID is equal", orderDetailPage.getOrderID(), is(orderIDNew));
         Assertion.assertThat("Start Date is equal", orderDetailPage.getStartDate(orderIDNew), is(newStartDate));
         Assertion.assertThat("Hotel Name is displayed correctly", orderDetailPage.isHotelNameDisplayed(query), is(true));
         Assertion.assertThat("Room Name is displayed correctly", orderDetailPage.isRoomNameDisplayed(chooseFirstRoomData.newRoomName), is(true));
     }

    @Author(names = {"Yazid"})
    @Module(name = "Reschedule")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void verifyApprovalNotesReschedulePreviousOrder() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyApprovalNotesReschedulePreviousOrder");
        BaseTest.paramsThreadLocal.set(param);

        String username = param.get("username");
        String password = param.get("password");
        String adminUsername = param.get("adminUsername");
        String adminPassword = param.get("adminPassword");
        String startDate = generateRandomDate(param.get("rangeAddDate"));
        String query = param.get("hotelName"); // use our own native hotel
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night")); // should be less than (rescheduleDuration)
        int room = Integer.parseInt(param.get("room"));
        String[] salutationList = { "Mr", "Mrs", "Ms" };
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String notes = param.get("notes");
        int rescheduleDuration = Integer.parseInt(param.get("rescheduleDuration")); // should be more than (night)

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        HotelReschedulePage<WebDriver> hotelReschedulePage = HotelReschedulePage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);
        CorporateLoginPage<?> corporateLoginPage = CorporateLoginPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        HotelReschedulePageWeb<?> hotelReschedulePageWeb = HotelReschedulePageWeb.getInstance(session);

        String orderID;
        step("Create new hotel issued order");
        orderID = hotelApi.createIssuedOrderApiB2B(
                username, password, startDate, query, adult, night, room,"IDR",
                orderFullName, orderNameTitle, corporateApi.getEmployeeData(username), false,
                notes);
        Assertion.assertThat("Hotel Booking via API is successful for B2B account", orderID, is(not(emptyOrNullString())));

        step("Goto home");
        homePage.gotoHome();

        step("Approve booking by admin account for Order ID: " + orderID);
        Boolean bookingStatus = corporateApi.approveBookingB2BApi(adminUsername, adminPassword, orderID);
        Assertion.assertThat("Booking approved by B2B dashboard", bookingStatus, is(true));

        step("Log out");
        loginPage.loginAsGuest();

        step("B2B login");
        corporateLoginPage.corporateLogin(username,password);

        step("Open my order page");
        homePage.openMyOrderPage();

        step("Find the issued order");
        orderListPage.refreshTheOrderList(2);
        orderListPage.openIssuedTicketViaDeeplink(orderID);

        step("Choose reschedule");
        orderDetailPage.clickReschedule(orderID);

        step("Choose new date for the reschedule");
        webViewPage.switchToWebViewUrl("reschedule");
        hotelReschedulePageWeb.changeNightDuration(rescheduleDuration);

        step("Choose a new room type with additional Price");
        webViewPage.switchToWebViewUrl("reschedule");
        hotelReschedulePageWeb.chooseRoomWithAdditionalPrice();

        step("Submit booking form");
        webViewPage.switchToWebViewUrl("reschedule/checkout");
        hotelReschedulePageWeb.submitRescheduleBookingForm();

        step("Submit the payment method");
        webViewPage.switchToNative();
        paymentPage.checkOrderID();
        paymentPage.clickPayButton();

        step("On external dashboard inside reschedule request check for same approval notes");
        String dashboardNotes = corporateApi.getOrderNotesFromReschedule(adminUsername, adminPassword, orderID);
        Assertion.assertThat("Approval notes are same as requested", dashboardNotes, is(notes));
    }

    @Author(names = {"Ashish"})
    @Module(name = "Reschedule")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void testTravelerRequestRescheduleHotelViaAdmin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testTravelerRequestRescheduleHotelViaAdmin");
        BaseTest.paramsThreadLocal.set(param);

        String username = param.get("username");
        String password = param.get("password");
        String adminUsername = param.get("adminUsername");
        String adminPassword = param.get("adminPassword");
        String startDate = generateRandomDate(param.get("rangeAddDate"));

        String query = param.get("hotelName");
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));
        int room = Integer.parseInt(param.get("room"));

        String[] salutationList = { "Mr", "Mrs", "Ms" };
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = getRandomObject(salutationList);

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        HotelPage<WebDriver> hotelPage = HotelPage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        HotelReschedulePageWeb<?> hotelReschedulePageWeb = HotelReschedulePageWeb.getInstance(session);

        String orderID;
        step("Create new hotel issued order");
        orderID = hotelApi.createIssuedOrderApiB2B(
                username, password, startDate, query, adult, night, room, "IDR",
                orderFullName, orderNameTitle, guestFullName, guestTitle,
                corporateApi.getEmployeeData(username),
                /* selectRoomMostExpansive */ false,
                /* isRefundable */ true);
        Assertion.assertThat("Hotel Booking via API is successful for B2B account", !orderID.isEmpty(), is(true));
        sleep(5);

        step("Approve booking by admin account for Order ID: " + orderID);
        Boolean bookingStatus = corporateApi.approveBookingB2BApi(adminUsername, adminPassword, orderID);
        Assertion.assertThat("Booking approved by B2B dashboard", bookingStatus, is(true));

        step("Goto home");
        homePage.gotoHome();

        step("Login as B2B traveler account " + username + " via deeplink");
        homePage.loginViaDeeplink(username, password);

        step("Open my order page");
        homePage.openMyOrderPage();

        step("Find the issued order");
        orderListPage.refreshTheOrderList(2);
        orderListPage.openIssuedTicketViaDeeplink(orderID);

        step("Choose reschedule");
        orderDetailPage.clickReschedule(orderID);

        step("Choose new date for the reschedule");
        webViewPage.switchToWebViewUrl("bookings/reschedule");
        String newStartDate = hotelReschedulePageWeb.changeNightDuration(1);

        step("Choose a new room type");
        webViewPage.switchToWebViewUrl("bookings/reschedule");
        String newRoomName = hotelReschedulePageWeb.getRoomName();
        HotelReschedulePageWeb.RoomPriceInfo priceInfo = hotelReschedulePageWeb.bookFirstAvailableRoom();
        boolean isPaymentFree = priceInfo.state() != HotelReschedulePageWeb.RoomPriceState.ADDITIONAL;


        step("Submit booking form");
        webViewPage.switchToWebViewUrl("reschedule/checkout");
        hotelReschedulePageWeb.submitRescheduleBookingForm();

        step("Submit the payment method");
        webViewPage.switchToNative();
        String orderIDNew = paymentPage.checkOrderID();
        paymentPage.selectRefundAndClickPay();
        paymentPage.clickOnCheckMarkAndClickContinue(isPaymentFree);

         step("approve reschedule");
         boolean isApproved = true;
         boolean success = corporateApi.approveOrRejectRescheduleB2B(adminUsername, adminPassword, isApproved, orderID,"approve by automation");
         Assertion.assertThat("reschedule Approved Success", success, is(true));

         step("see order list");
         paymentPage.seeOrderList(orderIDNew, isPaymentFree);

        step("Check the new order detail");
        orderListPage.refreshTheOrderList(2);
        orderListPage.openIssuedTicketViaDeeplink(orderIDNew);
        Assertion.assertThat("Reschedule card notification",orderDetailPage.isRescheduleCardDisplayed(orderIDNew), is(true));
        // The new iOS UI surfaces only the orderDetailId on the order detail page
        // (the parent orderId is no longer rendered natively). Resolve the expected
        String orderDetailIdNew = orderListPage.getOrderDetailIdByOrderId(orderIDNew);
        Assertion.assertThat("Order Detail ID is equal", orderDetailPage.getOrderDetailID(), is(orderDetailIdNew));
        Assertion.assertThat("Start Date is equal", orderDetailPage.getStartDate(orderIDNew), is(newStartDate));
        Assertion.assertThat("Hotel Name is displayed correctly", orderDetailPage.isHotelNameDisplayed(query), is(true));
        Assertion.assertThat("Room Name is displayed correctly", orderDetailPage.isRoomNameDisplayed(newRoomName), is(true));
    }

    @Author(names = {"Ashish"})
    @Module(name = "Reschedule")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void testTravelerRequestRescheduleHotelViaApprover() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testTravelerRequestRescheduleHotelViaApprover");
        BaseTest.paramsThreadLocal.set(param);

        String username = param.get("username");
        String password = param.get("password");
        String approverUsername = param.get("approverUsername");
        String approverPassword = param.get("approverPassword");
        String startDate = generateRandomDate(param.get("rangeAddDate"));

        String query = param.get("hotelName");
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));
        int room = Integer.parseInt(param.get("room"));

        String[] salutationList = { "Mr", "Mrs", "Ms" };
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = getRandomObject(salutationList);

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        HotelPage<WebDriver> hotelPage = HotelPage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        HotelReschedulePageWeb<?> hotelReschedulePageWeb = HotelReschedulePageWeb.getInstance(session);

        String orderID;
        step("Create new hotel issued order");
        orderID = hotelApi.createIssuedOrderApiB2B(
                username, password, startDate, query, adult, night, room, "IDR",
                orderFullName, orderNameTitle, guestFullName, guestTitle,
                corporateApi.getEmployeeData(username),
                /* selectRoomMostExpansive */ false,
                /* isRefundable */ true);
        Assertion.assertThat("Hotel Booking via API is successful for B2B account", !orderID.isEmpty(), is(true));
        sleep(5);

        step("Approve booking by approver account for Order ID: " + orderID);
        Boolean bookingStatus = corporateApi.approveBookingB2BApi(approverUsername, approverPassword, orderID);
        Assertion.assertThat("Booking approved by B2B dashboard", bookingStatus, is(true));

        step("Goto home");
        homePage.gotoHome();

        step("Login as B2B traveler account " + username + " via deeplink");
        homePage.loginViaDeeplink(username, password);

        step("Open my order page");
        homePage.openMyOrderPage();

        step("Find the issued order");
        orderListPage.refreshTheOrderList(2);
        orderListPage.openIssuedTicketViaDeeplink(orderID);

        step("Choose reschedule");
        orderDetailPage.clickReschedule(orderID);

        step("Choose new date for the reschedule");
        webViewPage.switchToWebViewUrl("bookings/reschedule");
        String newStartDate = hotelReschedulePageWeb.changeNightDuration(1);

        step("Choose a new room type");
        webViewPage.switchToWebViewUrl("bookings/reschedule");
        String newRoomName = hotelReschedulePageWeb.getRoomName();
        HotelReschedulePageWeb.RoomPriceInfo priceInfo = hotelReschedulePageWeb.bookFirstAvailableRoom();
        boolean isPaymentFree = priceInfo.state() != HotelReschedulePageWeb.RoomPriceState.ADDITIONAL;


        step("Submit booking form");
        webViewPage.switchToWebViewUrl("reschedule/checkout");
        hotelReschedulePageWeb.submitRescheduleBookingForm();

        step("Submit the payment method");
        webViewPage.switchToNative();
        String orderIDNew = paymentPage.checkOrderID();
        paymentPage.selectRefundAndClickPay();
        paymentPage.clickOnCheckMarkAndClickContinue(isPaymentFree);

         step("approve reschedule");
         boolean isApproved = true;
         boolean success = corporateApi.approveOrRejectRescheduleB2B(approverUsername, approverPassword, isApproved, orderID,"approve by automation");
         Assertion.assertThat("reschedule Approved Success", success, is(true));

         step("see order list");
         paymentPage.seeOrderList(orderIDNew, isPaymentFree);

        step("Check the new order detail");
        orderListPage.refreshTheOrderList(2);
        orderListPage.openIssuedTicketViaDeeplink(orderIDNew);
        Assertion.assertThat("Reschedule card notification",orderDetailPage.isRescheduleCardDisplayed(orderIDNew), is(true));
        // The new iOS UI surfaces only the orderDetailId on the order detail page
        // (the parent orderId is no longer rendered natively). Resolve the expected
        String orderDetailIdNew = orderListPage.getOrderDetailIdByOrderId(orderIDNew);
        Assertion.assertThat("Order Detail ID is equal", orderDetailPage.getOrderDetailID(), is(orderDetailIdNew));
        Assertion.assertThat("Start Date is equal", orderDetailPage.getStartDate(orderIDNew), is(newStartDate));
        Assertion.assertThat("Hotel Name is displayed correctly", orderDetailPage.isHotelNameDisplayed(query), is(true));
        Assertion.assertThat("Room Name is displayed correctly", orderDetailPage.isRoomNameDisplayed(newRoomName), is(true));
    }

    @Author(names = {"Prerna"})
    @Module(name = "Refund")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void testApproveRefundRequestByTravelerEndToEnd() {
        Session<WebDriver> session = sessionThreadLocal.get();

        Map<String, String> param = getParam("testApproveRefundRequestByTravelerEndToEnd");
        BaseTest.paramsThreadLocal.set(param);

        String username = param.get("username");
        String password = param.get("password");
        String startDate = generateRandomDate(param.get("rangeAddDate"));
        String approverUsername = param.get("approverUsername");
        String approverPassword = param.get("approverPassword");

        String query = param.get("hotelName");
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));
        int room = Integer.parseInt(param.get("room"));

        String[] salutationList = { "Mr", "Mrs", "Ms" };
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = getRandomObject(salutationList);

        HotelPage<WebDriver> hotelPage = HotelPage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        RefundPage<WebDriver> refundPage = RefundPage.getInstance(session);
        CorporateLoginPage<WebDriver> corporateLoginPage = CorporateLoginPage.getInstance(session);

        String orderID;
        step("Create new hotel issued order");
        orderID = hotelApi.createIssuedOrderApiB2B(
                username, password, startDate, query, adult, night, room,"IDR",
                orderFullName, orderNameTitle, guestFullName, guestTitle, corporateApi.getEmployeeData(username), false,true);
        Assertion.assertThat("Hotel Booking via API is successful for B2B account", !orderID.isEmpty(), is(true));
        sleep(5);

        step("Goto home");
        homePage.gotoHome();

        step("Log out");
        loginPage.loginAsGuest();

        step("B2B login");
        corporateLoginPage.corporateLogin(username,password);

        step("Approve booking by approver account for Order ID: " + orderID);
        Boolean bookingStatus = corporateApi.approveBookingB2BApi(approverUsername, approverPassword, orderID);
        Assertion.assertThat("Booking approved by B2B dashboard", bookingStatus, is(true));

        step("Find the issued order");
        orderListPage.openIssuedTicketViaDeeplink(orderID);

        step("Open refund menu");
        orderDetailPage.clickRefundButton();

        step("Choose refund reason");
        refundPage.chooseRefundReasonWebView();

        step("Click process refund button");
        refundPage.clickProcessRefundBtn();

        step("Submit refund");
        refundPage.submitRefundCorporateBilling();

        step("go to home");
        homePage.gotoHome();
        homePage.openMyOrderPage();

        step("Check if the refunded order is being reflected on corporate dashboard");
        String refundId = corporateApi.getRefundId(orderID, approverUsername, approverPassword,"prerefund");
        Boolean isApproved = true;
        Boolean success = corporateApi.approveOrRejectRefundB2B(approverUsername, approverPassword, refundId, isApproved);
        Assertion.assertThat("Refund Approved Success", success, is(true));

        step("Approve refund from admin care dashboard");
        refundId = corporateApi.getRefundId(orderID, approverUsername, approverPassword,"refund");
        refundPage.approveEndToEndRefundFromAdminCareDashboard(refundId, orderID);

        step("Check Refund Status on App");
        orderListPage.openOngoingRefundTab();
        String refundStatus = orderListPage.refundStatus(refundId);

        Assertion.assertThat("Refund status is Refund has completed", refundStatus, either(is("Refund has completed")).or(is("Pengembalian dana telah selesai")));
    }

     @Author(names = {"Ashish"})
     @Module(name = "Reschedule")
     @Tribe(name = "Corporate")
     @Vertical(name = "B2B")
     @Test
     public void testAdminRequestRescheduleFlightAutoApprove() {
         Session<WebDriver> session = sessionThreadLocal.get();
         Map<String, String> param = getParam("testAdminRequestRescheduleFlightAutoApprove");
         BaseTest.paramsThreadLocal.set(param);

         HomePage<WebDriver> homePage = HomePage.getInstance(session);
         LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
         CorporateLoginPage<WebDriver> corporateLoginPage = CorporateLoginPage.getInstance(session);
         OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
         OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
         FlightReschedulePage<WebDriver> flightReschedulePage = FlightReschedulePage.getInstance(session);
         FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
         FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
         FlightBookingFormPage<WebDriver> bookingFormPage = FlightBookingFormPage.getInstance(session);
         PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
         FlightApi<?> flightApi = new FlightApi<>(session);
         CorporateApi<?> corporateApi = new CorporateApi<>(session);

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

         step("Find dynamic data");
         FlightDynamicData flightDynamicData = flightApi.findDate(
                 username,password,
                 originAirport,destinationAirport,daysToDepartureDate,
                 adultPassengers,childPassengers,infantPassengers,
                 tripType,airline,"","","",""
         );
         originAirport = flightDynamicData.originAirport;
         destinationAirport = flightDynamicData.destinationAirport;
         String departureDate = CoreFunctions.convertToLongDate(flightDynamicData.departureDate);
         airline = flightDynamicData.airlineDepart;

         if (Configs.ENVIRONMENT == Environment.PREPROD) {
             step("Validate all suppliers are mock-issued");
             flightApi.validateAllSuppliersAreMockIssuedByAirline(flightDynamicData.airlineCodeDepart, flightDynamicData.originAirport, flightDynamicData.destinationAirport);
         }

         step("complete flight booking via API");
         String orderID = flightApi.createOrderFlightB2BDomestic(username, password, originAirport, destinationAirport, departDateApi, tripType, adultPassengers, childPassengers, infantPassengers, airline,corporateApi.getEmployeeData(username), firstName, lastName);
         Assertion.assertThat("Flight order created", orderID, not(""));
         orderListPage.waitForPaymentSuccessful(orderID,20);

         step("Goto home");
         homePage.gotoHome();

         step("Log out");
         loginPage.loginAsGuest();

         step("B2B login" + username);
         corporateLoginPage.corporateLogin(username,password);

         step("Change currency");
         homePage.changeCurrency(currency,username);

         step("Open my order page");
         homePage.openMyOrderPage();

         step("get booking code from API");
         String bookingCode = flightApi.getBookingCode(orderID, username);

         step("set Tiket Number on flight dashboard and ticket will be e-ticket issued order id {" + orderID + " }");
         Boolean isTicketIssued = flightApi.flightEticketIssuedRequest(orderID, bookingCode, username,firstName + " " + lastName,20);
         Assertion.assertThat("is Eticket Issued", isTicketIssued, is(true));

         step("Find the issued order");
         orderListPage.openIssuedTicketViaDeeplink(orderID);
         Assertion.assertThat("OrderStatus is E-ticket issued", orderDetailPage.getETicketLabel(), either(is("Your e-ticket is here")).or(is("E-tiketmu di sini")));

         step("get total price order page");
         String totalPriceOrderDetail = orderDetailPage.getTotalPrice();
         Assertion.assertThat("totalPriceOrderDetail: ",!totalPriceOrderDetail.isEmpty(),is(true));

         step("Choose reschedule");
         orderDetailPage.clickReschedule(orderID);

         step("select passenger and click find reschedule");
         flightReschedulePage.clickPassengerCheckbox();
         flightReschedulePage.clickFindRescheduleButton();

         step("select date to reschedule");
         flightSearchPage.selectDateWebView(newDepartureDate);
         flightSearchPage.searchFlightWebView();

         step("filter based on airline");
         flightSearchResultPage.openFilterWebView();
         flightSearchResultPage.filterAirlineWebView(airline);
         flightSearchResultPage.saveFilterWebView();

         step("get reschedule price");
         String reschedulePrice = flightSearchResultPage.getReschedulePrice();
         Assertion.assertThat("Reschedule price is not empty", !reschedulePrice.isEmpty(), is(true));

         step("choose the flight");
         flightSearchResultPage.selectFlightWebView();

         step("click next to move to 2nd half booking page");
         bookingFormPage.clickNextButtonWebView();

         step("match price from srp");
         String reschedulePriceBookingPage = bookingFormPage.getReschedulePrice();
         Assertion.assertThat("Reschedule price is correct", convertPrice(reschedulePrice), is(convertPrice(reschedulePriceBookingPage)));

         step("open payment page");
         bookingFormPage.clickContinueToPaymentWebView();
         bookingFormPage.clickNoThanksOnFlightMayReschedulePopup();

         step("check new order id");
         String orderIDRescheduled = paymentPage.checkOrderID();
         Assertion.assertThat("new Order ID is not same with older one", orderIDRescheduled, not(orderID));

         step("get total payment");
         String totalPricePaymentPageReschedule = paymentPage.getTotalPricePayment();
         Assertion.assertThat("totalPaymentInReschedule: ",!totalPricePaymentPageReschedule.isEmpty(),is(true));

         step("submit payment");
         paymentPage.selectRefundAndClickPay();
         boolean isPaymentFree = paymentPage.isFreePaymentOrRefund(reschedulePrice);
         paymentPage.clickOnCheckMarkAndClickContinue(isPaymentFree);
         orderListPage.waitForPaymentSuccessful(orderIDRescheduled,20);

         step("see order list");
         paymentPage.seeOrderList(orderIDRescheduled, isPaymentFree);

         step("generate e-ticket");
         String bookingCodReschedule = flightApi.getBookingCode(orderIDRescheduled, username);
         Boolean isTicketIssuedNew = flightApi.flightEticketIssuedRequest(orderIDRescheduled, bookingCodReschedule, username, firstName + " " + lastName,20);
         Assertion.assertThat("is E-ticket Issued", isTicketIssuedNew, is(true));

         step("verify the order detail");
         orderListPage.openIssuedTicketViaDeeplink(orderIDRescheduled);
         Assertion.assertThat("Order ID is equal", orderDetailPage.getOrderID(), is(orderIDRescheduled));
         Assertion.assertThat("OrderStatus is E-ticket issued", orderDetailPage.getETicketLabel(), either(is("Your e-ticket is here")).or(is("E-tiketmu di sini")));

         step("verify reschedule price");
         String totalPriceOrderDetailReschedule = orderDetailPage.getTotalPrice();
         Assertion.assertThat("is total price from payment page reschedule { "+ totalPricePaymentPageReschedule+" } match with order detail page reschedule { "+ totalPriceOrderDetailReschedule+ " } total price",CoreFunctions.convertPriceSafely(totalPriceOrderDetailReschedule),is(CoreFunctions.convertPriceSafely(totalPricePaymentPageReschedule)));
     }

     @Author(names = {"Ashish"})
     @Module(name = "Reschedule")
     @Tribe(name = "Corporate")
     @Vertical(name = "B2B")
     @Test
     public void testApproverRequestRescheduleFlightAutoApprove() {
         Session<WebDriver> session = sessionThreadLocal.get();
         Map<String, String> param = getParam("testApproverRequestRescheduleFlightAutoApprove");
         BaseTest.paramsThreadLocal.set(param);

         HomePage<WebDriver> homePage = HomePage.getInstance(session);
         LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
         CorporateLoginPage<WebDriver> corporateLoginPage = CorporateLoginPage.getInstance(session);
         OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
         OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
         FlightReschedulePage<WebDriver> flightReschedulePage = FlightReschedulePage.getInstance(session);
         FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
         FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
         FlightBookingFormPage<WebDriver> bookingFormPage = FlightBookingFormPage.getInstance(session);
         PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
         FlightApi<?> flightApi = new FlightApi<>(session);
         CorporateApi<?> corporateApi = new CorporateApi<>(session);

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

         step("Find dynamic data");
         FlightDynamicData flightDynamicData = flightApi.findDate(
                 username,password,
                 originAirport,destinationAirport,daysToDepartureDate,
                 adultPassengers,childPassengers,infantPassengers,
                 tripType,airline,"","","",""
         );
         originAirport = flightDynamicData.originAirport;
         destinationAirport = flightDynamicData.destinationAirport;
         String departureDate = CoreFunctions.convertToLongDate(flightDynamicData.departureDate);
         airline = flightDynamicData.airlineDepart;

         if (Configs.ENVIRONMENT == Environment.PREPROD) {
             step("Validate all suppliers are mock-issued");
             flightApi.validateAllSuppliersAreMockIssuedByAirline(flightDynamicData.airlineCodeDepart, flightDynamicData.originAirport, flightDynamicData.destinationAirport);
         }

         step("complete flight booking via API");
         String orderID = flightApi.createOrderFlightB2BDomestic(username, password, originAirport, destinationAirport, departDateApi, tripType, adultPassengers, childPassengers, infantPassengers, airline,corporateApi.getEmployeeData(username), firstName, lastName);
         Assertion.assertThat("Flight order created", orderID, not(""));
         orderListPage.waitForPaymentSuccessful(orderID,20);

         step("Goto home");
         homePage.gotoHome();

         step("Log out");
         loginPage.loginAsGuest();

         step("B2B login" + username);
         corporateLoginPage.corporateLogin(username,password);

         step("Change currency");
         homePage.changeCurrency(currency,username);

         step("Open my order page");
         homePage.openMyOrderPage();

         step("get booking code from API");
         String bookingCode = flightApi.getBookingCode(orderID, username);

         step("set Tiket Number on flight dashboard and ticket will be e-ticket issued order id {" + orderID + " }");
         Boolean isTicketIssued = flightApi.flightEticketIssuedRequest(orderID, bookingCode, username,firstName + " " + lastName,20);
         Assertion.assertThat("is Eticket Issued", isTicketIssued, is(true));

         step("Find the issued order");
         orderListPage.openIssuedTicketViaDeeplink(orderID);
         Assertion.assertThat("OrderStatus is E-ticket issued", orderDetailPage.getETicketLabel(), either(is("Your e-ticket is here")).or(is("E-tiketmu di sini")));

         step("get total price order page");
         String totalPriceOrderDetail = orderDetailPage.getTotalPrice();
         Assertion.assertThat("totalPriceOrderDetail: ",!totalPriceOrderDetail.isEmpty(),is(true));

         step("Choose reschedule");
         orderDetailPage.clickReschedule(orderID);

         step("select passenger and click find reschedule");
         flightReschedulePage.clickPassengerCheckbox();
         flightReschedulePage.clickFindRescheduleButton();

         step("select date to reschedule");
         flightSearchPage.selectDateWebView(newDepartureDate);
         flightSearchPage.searchFlightWebView();

         step("filter based on airline");
         flightSearchResultPage.openFilterWebView();
         flightSearchResultPage.filterAirlineWebView(airline);
         flightSearchResultPage.saveFilterWebView();

         step("get reschedule price");
         String reschedulePrice = flightSearchResultPage.getReschedulePrice();
         Assertion.assertThat("Reschedule price is not empty", !reschedulePrice.isEmpty(), is(true));

         step("choose the flight");
         flightSearchResultPage.selectFlightWebView();

         step("click next to move to 2nd half booking page");
         bookingFormPage.clickNextButtonWebView();

         step("match price from srp");
         String reschedulePriceBookingPage = bookingFormPage.getReschedulePrice();
         Assertion.assertThat("Reschedule price is correct", convertPrice(reschedulePrice), is(convertPrice(reschedulePriceBookingPage)));

         step("open payment page");
         bookingFormPage.clickContinueToPaymentWebView();
         bookingFormPage.clickNoThanksOnFlightMayReschedulePopup();

         step("check new order id");
         String orderIDRescheduled = paymentPage.checkOrderID();
         Assertion.assertThat("new Order ID is not same with older one", orderIDRescheduled, not(orderID));

         step("get total payment");
         String totalPricePaymentPageReschedule = paymentPage.getTotalPricePayment();
         Assertion.assertThat("totalPaymentInReschedule: ",!totalPricePaymentPageReschedule.isEmpty(),is(true));

         step("submit payment");
         paymentPage.selectRefundAndClickPay();
         boolean isPaymentFree = paymentPage.isFreePaymentOrRefund(reschedulePrice);
         paymentPage.clickOnCheckMarkAndClickContinue(isPaymentFree);
         orderListPage.waitForPaymentSuccessful(orderIDRescheduled,20);

         step("see order list");
         paymentPage.seeOrderList(orderIDRescheduled, isPaymentFree);

         step("generate e-ticket");
         String bookingCodReschedule = flightApi.getBookingCode(orderIDRescheduled, username);
         Boolean isTicketIssuedNew = flightApi.flightEticketIssuedRequest(orderIDRescheduled, bookingCodReschedule, username, firstName + " " + lastName,20);
         Assertion.assertThat("is E-ticket Issued", isTicketIssuedNew, is(true));

         step("verify the order detail");
         orderListPage.openIssuedTicketViaDeeplink(orderIDRescheduled);
         Assertion.assertThat("Order ID is equal", orderDetailPage.getOrderID(), is(orderIDRescheduled));
         Assertion.assertThat("OrderStatus is E-ticket issued", orderDetailPage.getETicketLabel(), either(is("Your e-ticket is here")).or(is("E-tiketmu di sini")));

         step("verify reschedule price");
         String totalPriceOrderDetailReschedule = orderDetailPage.getTotalPrice();
         Assertion.assertThat("is total price from payment page reschedule { "+ totalPricePaymentPageReschedule+" } match with order detail page reschedule { "+ totalPriceOrderDetailReschedule+ " } total price",CoreFunctions.convertPriceSafely(totalPriceOrderDetailReschedule),is(CoreFunctions.convertPriceSafely(totalPricePaymentPageReschedule)));
     }

     @Author(names = {"Ashish"})
     @Module(name = "Reschedule")
     @Tribe(name = "Corporate")
     @Vertical(name = "B2B")
     @Test
     public void testTravelerRequestRescheduleFlightViaAdmin() {
         Session<WebDriver> session = sessionThreadLocal.get();
         Map<String, String> param = getParam("testTravelerRequestRescheduleFlightViaAdmin");
         BaseTest.paramsThreadLocal.set(param);

         HomePage<WebDriver> homePage = HomePage.getInstance(session);
         LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
         CorporateLoginPage<WebDriver> corporateLoginPage = CorporateLoginPage.getInstance(session);
         OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
         OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
         FlightReschedulePage<WebDriver> flightReschedulePage = FlightReschedulePage.getInstance(session);
         FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
         FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
         FlightBookingFormPage<WebDriver> bookingFormPage = FlightBookingFormPage.getInstance(session);
         PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
         FlightApi<?> flightApi = new FlightApi<>(session);
         CorporateApi<?> corporateApi = new CorporateApi<>(session);

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

         step("Find dynamic data");
         FlightDynamicData flightDynamicData = flightApi.findDate(
                 username,password,
                 originAirport,destinationAirport,daysToDepartureDate,
                 adultPassengers,childPassengers,infantPassengers,
                 tripType,airline,"","","",""
         );
         originAirport = flightDynamicData.originAirport;
         destinationAirport = flightDynamicData.destinationAirport;
         String departureDate = CoreFunctions.convertToLongDate(flightDynamicData.departureDate);
         airline = flightDynamicData.airlineDepart;

         if (Configs.ENVIRONMENT == Environment.PREPROD) {
             step("Validate all suppliers are mock-issued");
             flightApi.validateAllSuppliersAreMockIssuedByAirline(flightDynamicData.airlineCodeDepart, flightDynamicData.originAirport, flightDynamicData.destinationAirport);
         }

         step("complete flight booking via API");
         String orderID = flightApi.createOrderFlightB2BDomestic(username, password, originAirport, destinationAirport, departDateApi, tripType, adultPassengers, childPassengers, infantPassengers, airline,corporateApi.getEmployeeData(username), firstName, lastName);
         Assertion.assertThat("Flight order created", orderID, not(""));

         step("approve payment of order id: { " + orderID +" } " + " using admin account with username : { " + adminUsername + " }");
         Boolean bookingStatus = corporateApi.approveBookingB2BApi(adminUsername, adminPassword, orderID);
         Assertion.assertThat("Booking approved by B2B dashboard", bookingStatus, is(true));

         step("Goto home");
         homePage.gotoHome();

         step("Log out");
         loginPage.loginAsGuest();

         step("B2B login" + username);
         corporateLoginPage.corporateLogin(username,password);

         step("Change currency");
         homePage.changeCurrency(currency,username);

         step("Open my order page");
         homePage.openMyOrderPage();

         step("get booking code from API");
         String bookingCode = flightApi.getBookingCode(orderID, username);

         step("set Tiket Number on flight dashboard and ticket will be eticket issued");
         Boolean isTicketIssued = flightApi.flightEticketIssuedRequest(orderID, bookingCode, username,firstName + " " + lastName,20);
         Assertion.assertThat("is Eticket Issued", isTicketIssued, is(true));

         step("Find the issued order");
         orderListPage.openIssuedTicketViaDeeplink(orderID);

         step("get total price order page");
         String totalPriceOrderDetail = orderDetailPage.getTotalPrice();
         Assertion.assertThat("totalPriceOrderDetail: ",!totalPriceOrderDetail.isEmpty(),is(true));

         step("Choose reschedule");
         orderDetailPage.clickReschedule(orderID);

         step("select passenger and click find reschedule");
         flightReschedulePage.clickPassengerCheckbox();
         flightReschedulePage.clickFindRescheduleButton();

         step("select date to reschedule");
         flightSearchPage.selectDateWebView(newDepartureDate);
         flightSearchPage.searchFlightWebView();

         step("filter based on airline");
         flightSearchResultPage.openFilterWebView();
         flightSearchResultPage.filterAirlineWebView(airline);
         flightSearchResultPage.saveFilterWebView();

         step("get reschedule price");
         String reschedulePrice = flightSearchResultPage.getReschedulePrice();

         step("choose the flight");
         flightSearchResultPage.selectFlightWebView();

         step("click next to move to 2nd half booking page");
         bookingFormPage.clickNextButtonWebView();

         step("match price from srp");
         String reschedulePriceBookingPage = bookingFormPage.getReschedulePrice();
         Assertion.assertThat("Reschedule price is correct", convertPrice(reschedulePrice), is(convertPrice(reschedulePriceBookingPage)));

         step("open payment page");
         bookingFormPage.clickContinueToPaymentWebView();
         bookingFormPage.clickNoThanksOnFlightMayReschedulePopup();

         step("check new order id");
         String orderIDRescheduled = paymentPage.checkOrderID();
         Assertion.assertThat("new Order ID is not same with older one", orderIDRescheduled, not(orderID));

         step("get total payment");
         String totalPricePaymentPageReschedule = paymentPage.getTotalPricePayment();
         Assertion.assertThat("totalPaymentInReschedule: ",!totalPricePaymentPageReschedule.isEmpty(),is(true));

         step("submit payment");
         paymentPage.clickPayButtonWithoutWaitForPreviousBookingPopup();
         boolean isPaymentFree = paymentPage.isFreePaymentOrRefund(reschedulePrice);
         paymentPage.clickOnCheckMarkAndClickContinue(isPaymentFree);

         step("approve reschedule with order id: { " + orderIDRescheduled +" } " + " using admin account with username : { " + adminUsername + " }");
         boolean success = corporateApi.approveOrRejectRescheduleB2B(adminUsername, adminPassword, true,orderIDRescheduled ,"approve by automation using " + adminUsername);
         Assertion.assertThat("reschedule Approved Success", success, is(true));

         step("see order list");
         paymentPage.seeOrderList(orderIDRescheduled, isPaymentFree);

         step("generate e-ticket");
         String bookingCodReschedule = flightApi.getBookingCode(orderIDRescheduled, username);
         Boolean isTicketIssuedNew = flightApi.flightEticketIssuedRequest(orderIDRescheduled, bookingCodReschedule, username, firstName + " " + lastName,20);
         Assertion.assertThat("is Eticket Issued", isTicketIssuedNew, is(true));

         step("verify the order detail");
         orderListPage.openIssuedTicketViaDeeplink(orderIDRescheduled);
         Assertion.assertThat("Order ID is equal", orderDetailPage.getOrderID(), is(orderIDRescheduled));
         Assertion.assertThat("OrderStatus is Eticket issued", orderDetailPage.getETicketLabel(), either(is("Your e-ticket is here")).or(is("E-tiketmu di sini")));

         step("verify reschedule price");
         String totalPriceOrderDetailReschedule = orderDetailPage.getTotalPrice();
         Assertion.assertThat("is total price from payment page reschedule { "+ totalPricePaymentPageReschedule+" } match with order detail page reschedule { "+ totalPriceOrderDetailReschedule+ " } total price",CoreFunctions.convertPriceSafely(totalPriceOrderDetailReschedule),is(CoreFunctions.convertPriceSafely(totalPricePaymentPageReschedule)));
     }

     @Author(names = {"Ashish"})
     @Module(name = "Reschedule")
     @Tribe(name = "Corporate")
     @Vertical(name = "B2B")
     @Test
     public void testTravelerRequestRescheduleFlightViaApprover() {
         Session<WebDriver> session = sessionThreadLocal.get();
         Map<String, String> param = getParam("testTravelerRequestRescheduleFlightViaApprover");
         BaseTest.paramsThreadLocal.set(param);

         HomePage<WebDriver> homePage = HomePage.getInstance(session);
         LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
         CorporateLoginPage<WebDriver> corporateLoginPage = CorporateLoginPage.getInstance(session);
         FlightPage<WebDriver> flightPage = FlightPage.getInstance(session);
         OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
         OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
         FlightReschedulePage<WebDriver> flightReschedulePage = FlightReschedulePage.getInstance(session);
         FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
         FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
         FlightBookingFormPage<WebDriver> bookingFormPage = FlightBookingFormPage.getInstance(session);
         PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
         FlightApi<?> flightApi = new FlightApi<>(session);
         CorporateApi<?> corporateApi = new CorporateApi<>(session);

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

         step("Find dynamic data");
         FlightDynamicData flightDynamicData = flightApi.findDate(
                 username,password,
                 originAirport,destinationAirport,daysToDepartureDate,
                 adultPassengers,childPassengers,infantPassengers,
                 tripType,airline,"","","",currency
         );
         originAirport = flightDynamicData.originAirport;
         destinationAirport = flightDynamicData.destinationAirport;
         newDepartureDate = CoreFunctions.convertToLongDate(flightDynamicData.departureDate);
         airline = flightDynamicData.airlineDepart;

         if (Configs.ENVIRONMENT == Environment.PREPROD) {
             step("Validate all suppliers are mock-issued");
             flightApi.validateAllSuppliersAreMockIssuedByAirline(flightDynamicData.airlineCodeDepart, flightDynamicData.originAirport, flightDynamicData.destinationAirport);
         }

         step("complete flight booking via API");
         String orderID = flightApi.createOrderFlightB2BDomestic(username, password, originAirport, destinationAirport, departDateApi, tripType, adultPassengers, childPassengers, infantPassengers, airline,corporateApi.getEmployeeData(username), firstName, lastName);
         Assertion.assertThat("Flight order created", orderID, not(""));

         step("approve payment of order id: { " + orderID +" } " + " using admin account with username : { " + approverUsername + " }");
         Boolean bookingStatus = corporateApi.approveBookingB2BApi(approverUsername, approverPassword, orderID);
         Assertion.assertThat("Booking approved by B2B dashboard", bookingStatus, is(true));

         step("Goto home");
         homePage.gotoHome();

         step("Log out");
         loginPage.loginAsGuest();

         step("B2B login" + username);
         corporateLoginPage.corporateLogin(username,password);

         step("Change currency");
         homePage.changeCurrency(currency,username);

         step("Open my order page");
         homePage.openMyOrderPage();

         step("get booking code from API");
         String bookingCode = flightApi.getBookingCode(orderID, username);

         step("set Tiket Number on flight dashboard and ticket will be eticket issued");
         Boolean isTicketIssued = flightApi.flightEticketIssuedRequest(orderID, bookingCode, username,firstName + " " + lastName,20);
         Assertion.assertThat("is Eticket Issued", isTicketIssued, is(true));

         step("Find the issued order");
         orderListPage.openIssuedTicketViaDeeplink(orderID);

         step("get total price order page");
         String totalPriceOrderDetail = orderDetailPage.getTotalPrice();
         Assertion.assertThat("totalPriceOrderDetail: ",!totalPriceOrderDetail.isEmpty(),is(true));

         step("Choose reschedule");
         orderDetailPage.clickReschedule(orderID);

         step("select passenger and click find reschedule");
         flightReschedulePage.clickPassengerCheckbox();
         flightReschedulePage.clickFindRescheduleButton();

         step("select date to reschedule");
         flightSearchPage.selectDateWebView(newDepartureDate);
         flightSearchPage.searchFlightWebView();

         step("filter based on airline");
         flightSearchResultPage.openFilterWebView();
         flightSearchResultPage.filterAirlineWebView(airline);
         flightSearchResultPage.saveFilterWebView();

         step("get reschedule price");
         String reschedulePrice = flightSearchResultPage.getReschedulePrice();

         step("choose the flight");
         flightSearchResultPage.selectFlightWebView();

         step("click next to move to 2nd half booking page");
         bookingFormPage.clickNextButtonWebView();

         step("match price from srp");
         String reschedulePriceBookingPage = bookingFormPage.getReschedulePrice();
         Assertion.assertThat("Reschedule price is correct", convertPrice(reschedulePrice), is(convertPrice(reschedulePriceBookingPage)));

         step("open payment page");
         bookingFormPage.clickContinueToPaymentWebView();
         bookingFormPage.clickNoThanksOnFlightMayReschedulePopup();

         step("check new order id");
         String orderIDRescheduled = paymentPage.checkOrderID();
         Assertion.assertThat("new Order ID is not same with older one", orderIDRescheduled, not(orderID));

         step("get total payment");
         String totalPricePaymentPageReschedule = paymentPage.getTotalPricePayment();
         Assertion.assertThat("totalPaymentInReschedule: ",!totalPricePaymentPageReschedule.isEmpty(),is(true));

         step("submit payment");
         paymentPage.clickPayButtonWithoutWaitForPreviousBookingPopup();
         boolean isPaymentFree = paymentPage.isFreePaymentOrRefund(reschedulePrice);
         paymentPage.clickOnCheckMarkAndClickContinue(isPaymentFree);

         step("approve reschedule with order id: { " + orderIDRescheduled +" } " + " using admin account with username : { " + approverUsername + " }");
         boolean success = corporateApi.approveOrRejectRescheduleB2B(approverUsername, approverPassword, true,orderIDRescheduled ,"approve by automation using " + approverUsername);
         Assertion.assertThat("reschedule Approved Success", success, is(true));

         step("see order list");
         paymentPage.seeOrderList(orderIDRescheduled, isPaymentFree);

         step("generate e-ticket");
         String bookingCodReschedule = flightApi.getBookingCode(orderIDRescheduled, username);
         Boolean isTicketIssuedNew = flightApi.flightEticketIssuedRequest(orderIDRescheduled, bookingCodReschedule, username, firstName + " " + lastName,20);
         Assertion.assertThat("is Eticket Issued", isTicketIssuedNew, is(true));

         step("verify the order detail");
         orderListPage.openIssuedTicketViaDeeplink(orderIDRescheduled);
         Assertion.assertThat("Order ID is equal", orderDetailPage.getOrderID(), is(orderIDRescheduled));
         Assertion.assertThat("OrderStatus is Eticket issued", orderDetailPage.getETicketLabel(), either(is("Your e-ticket is here")).or(is("E-tiketmu di sini")));

         step("verify reschedule price");
         String totalPriceOrderDetailReschedule = orderDetailPage.getTotalPrice();
         Assertion.assertThat("is total price from payment page reschedule { "+ totalPricePaymentPageReschedule+" } match with order detail page reschedule { "+ totalPriceOrderDetailReschedule+ " } total price",CoreFunctions.convertPriceSafely(totalPriceOrderDetailReschedule),is(CoreFunctions.convertPriceSafely(totalPricePaymentPageReschedule)));
     }

    @Author(names = {"Ashish"})
    @Module(name = "MeetTravelPolicy")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void testMeetTravelPolicyFlightOnlyPremiumEconomyClassSeatOption() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testMeetTravelPolicyFlightOnlyPremiumEconomyClassSeatOption");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightPage<WebDriver> flightPage = FlightPage.getInstance(session);

        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String username = param.get("username");
        String password = param.get("password");
        String currency = param.get("currency");

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Find dynamic data");
        flightPage.setDataOneWay(
                daysToDepartureDate,""
        );
        String departureDate =CoreFunctions.convertToLongDate(flightPage.getDate("departureDate"));

        step("Login as B2B account " + username + " via deeplink and change currency to " + currency);
        homePage.loginWithSpecificAccountViaDeeplink(username, password, currency);

        step("Open flights page");
        homePage.openVerticalPageDeeplink("Flights","",currency);
        flightSearchPage.closePopup();

        step("change date using deeplink");
        flightSearchPage.changeDateViaDeeplink(daysToDepartureDate,tripType);

        step("Toggle trip type { " + tripType + " }");
        flightSearchPage.toggleTriptype(tripType);

        step("Select origin airport");
        flightSearchPage.selectOrigin(originAirport);
        flightSearchPage.closePopup();

        step("Select destination airport");
        flightSearchPage.selectDestination(destinationAirport);

        step("Select date");
        flightSearchPage.selectDate(departureDate);

        step("select flight class [economy]");
        flightSearchPage.selectFlightClass("economy");

        step("Search flight");
        flightSearchPage.searchFlight();

        step("verify if search result page is empty according to travel policy");
        Assertion.assertThat("Search result is empty for economy", flightSearchResultPage.isSearchResultPageEmptyForEconomy(currency), is(true));

        step("back to search landing page");
        flightSearchResultPage.backToFlightPage();

        step("select flight class [premium economy]");
        flightSearchPage.selectFlightClass("premium economy");

        step("Search flight");
        flightSearchPage.searchFlight();

        step("check if premium economy flight shows up");
        Assertion.assertThat("Search result is shown for premium economy", flightSearchResultPage.isSearchResultShownForPremiumEconomy(currency), is(true));
    }

    @Author(names = {"Ashish"})
    @Module(name = "TravelPolicy")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void testFlightVerticalIsAvailableWhenTravelPolicyActivated() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testFlightVerticalIsAvailableWhenTravelPolicyActivated");
        BaseTest.paramsThreadLocal.set(param);

        CorporateLoginPage<WebDriver> corporateLoginPage = CorporateLoginPage.getInstance(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);

        String username = param.get("username");
        String password = param.get("password");
        String adminUsername = param.get("adminUsername");
        String adminPassword = param.get("adminPassword");
        int minPrice = Integer.parseInt(param.get("minPrice"));
        int maxPrice = Integer.parseInt(param.get("maxPrice"));
        String currency = param.get("currency");
        String policyName = param.get("policyName");

        step("get related data via api");
        String accessTokenAdmin = corporateApi.authLoginB2B(adminUsername,adminPassword);
        long accountIdAdmin = corporateApi.getAccountId(accessTokenAdmin);
        EmployeeData employeeDataAdmin = corporateApi.getEmployeeData(adminUsername);
        EmployeeData employeeDataUser = corporateApi.getEmployeeData(username);

        step("patch travel policy");
        int travelPolicyID = corporateApi.patchTravelPolicyFlight(accessTokenAdmin,employeeDataAdmin,accountIdAdmin,currency,minPrice,maxPrice,"","",0,false,false,policyName);
        Assertion.assertThat("Travel Policy patched", travelPolicyID, not(-1));

        step("assign travel policy to user");
        boolean isPolicyAssign = corporateApi.assignTravelPolicyToUser(accountIdAdmin,employeeDataAdmin,employeeDataUser,travelPolicyID);
        Assertion.assertThat("Travel Policy assigned", isPolicyAssign, is(true));

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Log out");
        loginPage.loginAsGuest();

        step("B2B login" + username);
        corporateLoginPage.corporateLogin(username,password);

        step("Change currency");
        homePage.changeCurrency(currency,username);

        step("Check if flight vertical is available");
        homePage.openVerticalPageDeeplink("Flights","",currency);
        flightSearchPage.closePopup();
        Assertion.assertThat("is flight vertical Available on {" + username + "} when Travel Policy Activated",flightSearchPage.isOnFlightLandingPage(),is(true));
    }

    @Author(names = {"Ashish"})
    @Module(name = "TravelPolicy")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void hotelBookingMeetTravelPolicyWithoutIssue() {
        Map<String, String> map = getParam("hotelBookingMeetTravelPolicyWithoutIssue");
        BaseTest.paramsThreadLocal.set(map);

        String username = map.get("username");
        String password = map.get("password");
        String adminUsername = map.get("adminUsername");
        String adminPassword = map.get("adminPassword");
        String location = map.get("location");
        String star = map.get("star");
        String currency = map.get("currency");
        String date_type = map.get("date_type");
        String date = map.get("date");

        Session<WebDriver> session = sessionThreadLocal.get();

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);
        TravelPlanWeb<?> travelPlanWeb = TravelPlanWeb.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("Login as B2B account " + username + " via deeplink and change currency to " + currency);
        homePage.loginWithSpecificAccountViaDeeplink(username, password, currency);

        step("Open hotel page");
        homePage.openVerticalPageDeeplink("Hotel","",currency);

        step("Select stay dates");
        landingPage.changeDateViaDeeplink(date,date_type,1,1);

        step("Search stay location");
        landingPage.searchStayLocation(location);

        step("Submit search");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        step("Choose filter based on testdata | testdata : '"+star+"'");
        searchResultPage.selectFilterStarRange(star);

        step("select hotel with meet travel policy nudge");
        searchResultPage.selectHotelWithMeetTravelPolicyNudge();

        step("Go to the room list");
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();

        step("Select room from the room list");
        roomListPage.getTheFirstHotelRoomPrice();
        roomListPage.clickBookRoom();

        step("Submit the booking form");
        bookingFormPage.continueToPaymentPage();
        webViewPage.switchToWebViewUrl("corporate/interim");
        travelPlanWeb.skipTravelPlan();
        webViewPage.switchToNative();
        bookingFormPage.PriceChangesPopUp();

        step("Payment page should be displayed");
        String orderID = paymentPage.checkOrderID();
        orderID = String.valueOf(getOrderId(orderID));
        Assertion.assertThat("Corporate billing option is selected", paymentPage.getSelectedPayment().toLowerCase(), is("corporate billing"));

        step("Submit payment using Corporate Billing");
        paymentPage.clickPayButtonWithoutWaitForPreviousBookingPopup();

        step("verify meet travel policy in the corporate dashboard via api");
        boolean isMeetTravelPolicy = corporateApi.checkPurchaseOrderMeetTravelPolicy(orderID, adminUsername, adminPassword);
        Assertion.assertThat("is meet travel policy shown", isMeetTravelPolicy, is(true));
    }

    @Author(names = {"Ashish"})
    @Module(name = "MeetTravelPolicy")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void hotelMeetTravelPolicyInPriceRange() {
        Map<String, String> map = getParam("hotelMeetTravelPolicyInPriceRange");
        BaseTest.paramsThreadLocal.set(map);

        String username = map.get("username");
        String password = map.get("password");
        String adminUsername = map.get("adminUsername");
        String adminPassword = map.get("adminPassword");
        String location = map.get("location");
        int totalCard = Integer.parseInt(map.get("totalCard"));
        String priceRange = map.get("priceRange");
        String currency = map.get("currency");
        int minPrice = Integer.parseInt(map.get("minPrice"));
        int maxPrice = Integer.parseInt(map.get("maxPrice"));
        int advanceBookingDay = Integer.parseInt(map.get("advanceBookingDay"));
        String policyName = map.get("policyName");

        Session<WebDriver> session = sessionThreadLocal.get();

        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);
        CorporateLoginPage<WebDriver> corporateLoginPage = CorporateLoginPage.getInstance(session);

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

        step("Goto home");
        homePage.gotoHome();

        step("Input credential on login page");
        corporateLoginPage.corporateLogin(username, password);

        step("change currency");
        homePage.changeCurrency(currency,username);

        step("Open hotel page");
        homePage.openVerticalPageDeeplink("Hotel","",currency);

        step("Search stay location");
        landingPage.searchStayLocation(location);

        step("Submit search");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        step("is meet travel policy display");
        Assertion.assertThat("meet travel policy display", searchResultPage.isMeetsTravelPolicyNudgeVisible(), is(true));

        step("sort high to low");
        searchResultPage.selectFilterSortBy("highest price");

        step("is hotel range meet policy price range of high to low");
        Assertion.assertThat("Hotel range meet policy price range", searchResultPage.isHotelRangeMeetPolicyPriceRange(totalCard,priceRange), is(true));

        step("sort low to high");
        searchResultPage.selectFilterSortBy("lowest price");

        step("is hotel range meet policy price range of high to low");
        Assertion.assertThat("Hotel range meet policy price range", searchResultPage.isHotelRangeMeetPolicyPriceRange(totalCard,priceRange), is(true));
    }

    @Author(names = {"Prerna"})
    @Module(name = "TravelPolicy")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void testTravelPolicyAppearForOneStarWhenPolicyIsSetToOneStarHotel() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testTravelPolicyAppearForOneStarWhenPolicyIsSetToOneStarHotel");
        BaseTest.paramsThreadLocal.set(param);

        String username = param.get("username");
        String password = param.get("password");
        String adminUsername = param.get("adminUsername");
        String adminPassword = param.get("adminPassword");

        String dateType = param.get("dateType");
        String dateRange = param.get("dateRange");
        String location = param.get("location");
        String star = param.get("star");

        int minPrice = Integer.parseInt(param.get("minPrice"));
        int maxPrice = Integer.parseInt(param.get("maxPrice"));
        int advanceBookingDay = Integer.parseInt(param.get("advanceBookingDay"));
        String currency = param.get("currency");
        String policyName = param.get("policyName");

        CorporateLoginPage<WebDriver> corporateLoginPage = CorporateLoginPage.getInstance(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);

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

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Log out");
        loginPage.loginAsGuest();

        step("B2B login" + username);
        corporateLoginPage.corporateLogin(username,password);

        step("Change currency");
        homePage.changeCurrency("IDR",username);

        step("Open hotel landing page");
        homePage.openVerticalPageDeeplink("Hotel", "", "IDR");

        step("Search stay location");
        landingPage.searchStayLocation(location);
        landingPage.setStayDate(dateType, dateRange);

        step("submit search");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        step("Filter 1 star hotel");
        searchResultPage.selectFilterStar("1");

        step("Check Meets Travel Policy nudge appears");
        searchResultPage.waitForSrpToLoad();
        Assertion.assertThat("Meets Travel Policy nudge appears for 1 star hotel", searchResultPage.isMeetsTravelPolicyNudgeVisible(), is(true));
    }

    @Author(names = {"Prerna"})
    @Module(name = "TravelPolicy")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void testMeetTravelPolicyOnSRPWhenTravelPolicyIsGlobalHotel() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testMeetTravelPolicyOnSRPWhenTravelPolicyIsGlobalHotel");
        BaseTest.paramsThreadLocal.set(param);

        String username = param.get("username");
        String password = param.get("password");
        String adminUsername = param.get("adminUsername");
        String adminPassword = param.get("adminPassword");
        String dateType = param.get("dateType");
        String dateRange = param.get("dateRange");
        String location = param.get("location");
        String star = param.get("star");
        String currency = param.get("currency");

        int minPrice = Integer.parseInt(param.get("minPrice"));
        int maxPrice = Integer.parseInt(param.get("maxPrice"));
        int advanceBookingDay = Integer.parseInt(param.get("advanceBookingDay"));
        String policyName = param.get("policyName");


        CorporateLoginPage<WebDriver> corporateLoginPage = CorporateLoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);

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

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Log out");
        loginPage.loginAsGuest();

        step("B2B login" + username);
        corporateLoginPage.corporateLogin(username,password);

        step("Change currency");
        homePage.changeCurrency("IDR",username);

        step("Open hotel landing page");
        homePage.openVerticalPageDeeplink("Hotel", "", "IDR");

        step("Search stay location");
        landingPage.searchStayLocation(location);
        landingPage.setStayDate(dateType, dateRange);

        step("submit search");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        step("Check Meets Travel Policy nudge appears");
        searchResultPage.waitForSrpToLoad();
        Assertion.assertThat("Meets Travel Policy nudge appears on SRP", searchResultPage.isMeetsTravelPolicyNudgeVisible(), is(true));
    }

    @Author(names = {"Prerna"})
    @Module(name = "TravelPolicy")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void testEmptySRPWhenTravelPolicyIsSetToAdvanceBookingHotel() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testEmptySRPWhenTravelPolicyIsSetToAdvanceBookingHotel");
        BaseTest.paramsThreadLocal.set(param);

        String username = param.get("username");
        String password = param.get("password");
        String location = param.get("location");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Login as B2B account " + username + " via deeplink and change currency to IDR");
        homePage.loginWithSpecificAccountViaDeeplink(username, password, "IDR");

        step("Open hotel landing page");
        homePage.openVerticalPageDeeplink("Hotel", "", "IDR");

        step("Search stay location");
        landingPage.searchStayLocation(location);

        step("submit search");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        step("Check SRP is empty");
        Assertion.assertThat("Check SRP is Empty", searchResultPage.checkEmptyHotelList(), is(true));
    }

    @Author(names = {"Ashish"})
    @Module(name = "MeetTravelPolicy")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void testMeetTravelPolicyFlightInternationalOWBooking() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testMeetTravelPolicyFlightInternationalOWBooking");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightProductReviewPage<WebDriver> flightProductReviewPage = FlightProductReviewPage.getInstance(session);
        FlightBookingFormPage<WebDriver> flightBookingFormPage = FlightBookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        TravelPlanWeb<?> travelPlanWeb = TravelPlanWeb.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        FlightApi<?> flightApi = new FlightApi<>(session);

        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int adultPassengers = Integer.parseInt(param.get("adultPassengers"));
        int childPassengers = Integer.parseInt(param.get("childPassengers"));
        int infantPassengers = Integer.parseInt(param.get("infantPassengers"));
        String airline = param.get("airline");
        String username = param.get("username");
        String password = param.get("password");

        String currency = param.get("currency");

        PriceDetail priceDetail = new PriceDetail();
        priceDetail.tripType=tripType;
        priceDetail.adult=adultPassengers;
        priceDetail.child=childPassengers;
        priceDetail.infant=infantPassengers;

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Find dynamic data");
        FlightDynamicData flightDynamicData = flightApi.findDate(
                username,password,
                originAirport,destinationAirport,daysToDepartureDate,
                adultPassengers,childPassengers,infantPassengers,
                tripType,airline,"","","",currency
        );
        originAirport = flightDynamicData.originAirport;
        destinationAirport = flightDynamicData.destinationAirport;
        String departureDate = CoreFunctions.convertToLongDate(flightDynamicData.departureDate);
        airline = flightDynamicData.airlineDepart;

        step("Login as B2B account " + username + " via deeplink and change currency to " + currency);
        homePage.loginWithSpecificAccountViaDeeplink(username, password, currency);

        step("Open flights page");
        homePage.openVerticalPageDeeplink("Flights","",currency);
        flightSearchPage.closePopup();

        step("change date using deeplink");
        flightSearchPage.changeDateViaDeeplink(flightDynamicData, tripType);

        step("Toggle trip type { " + tripType + " }");
        flightSearchPage.toggleTriptype(tripType);

        step("select flight class [economy]");
        flightSearchPage.selectFlightClass("economy");

        step("Select origin airport");
        flightSearchPage.selectOrigin(originAirport);
        flightSearchPage.closePopup();

        step("Select destination airport");
        flightSearchPage.selectDestination(destinationAirport);

        step("Select date");
        flightSearchPage.selectDate(departureDate);

        step("Add passenger");
        flightSearchPage.addPassenger(adultPassengers,childPassengers,infantPassengers);

        step("Search flight");
        flightSearchPage.searchFlight();

        step("Open filter");
        flightSearchResultPage.openFilter();

        step("Filter airline");
        flightSearchResultPage.filterAirline(airline);

        step("Save filter");
        flightSearchResultPage.saveFilter();

        step("verify meet travel policy for international flight");
        Assertion.assertThat("Is meet travel policy for international flight", flightSearchResultPage.isMeetTravelPolicy(), is(true));

        step("Click flight detail chevron");
        flightSearchResultPage.selectFlight();

        step("Continue to booking form");
        flightProductReviewPage.continueToBookingForm(tripType);

        step("verify date format");
        flightBookingFormPage.verifyDateFormat(tripType);

        step("Fill passenger detail");
        flightBookingFormPage.fillPassengerDetail(adultPassengers,childPassengers,infantPassengers,"international",airline);

        step("Continue to second half booking form");
        flightBookingFormPage.continueToSecondHalf();

        step("Open price detail");
        flightBookingFormPage.openPriceDetail();

        step("Verify price detail");
        Assertion.assertThat("Is price breakdown correct", flightBookingFormPage.isPriceBreakdownCorrect(priceDetail,currency), is(true));

        step("Continue to payment");
        flightBookingFormPage.continueToPayment();

        step("Skip full protection");
        flightBookingFormPage.skipFullProtection();
        webViewPage.switchToWebViewUrl("corporate/interim");
        travelPlanWeb.skipTravelPlan();
        webViewPage.switchToNative();

        step("Check order ID");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",!orderID.isBlank(),is(true));
    }

    @Author(names = {"Ashish"})
    @Module(name = "MeetTravelPolicy")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void testMeetTravelPolicyFlightSRP() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testMeetTravelPolicyFlightSRP");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightPage<WebDriver> flightPage = FlightPage.getInstance(session);

        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int adultPassengers = Integer.parseInt(param.get("adultPassengers"));
        int childPassengers = Integer.parseInt(param.get("childPassengers"));
        int infantPassengers = Integer.parseInt(param.get("infantPassengers"));
        String airline = param.get("airline");
        String username = param.get("username");
        String password = param.get("password");

        String currency = param.get("currency");

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Find dynamic data");
        flightPage.setDataOneWay(
                daysToDepartureDate,airline
        );
        String departureDate =CoreFunctions.convertToLongDate(flightPage.getDate("departureDate"));

        step("Login as B2B account " + username + " via deeplink and change currency to " + currency);
        homePage.loginWithSpecificAccountViaDeeplink(username, password, currency);

        step("Open flights page");
        homePage.openVerticalPageDeeplink("Flights","",currency);
        flightSearchPage.closePopup();

        step("change date using deeplink");
        flightSearchPage.changeDateViaDeeplink(daysToDepartureDate,tripType);

        step("Toggle trip type { " + tripType + " }");
        flightSearchPage.toggleTriptype(tripType);

        step("select flight class [economy]");
        flightSearchPage.selectFlightClass("economy");

        step("Select origin airport");
        flightSearchPage.selectOrigin(originAirport);
        flightSearchPage.closePopup();

        step("Select destination airport");
        flightSearchPage.selectDestination(destinationAirport);

        step("Select date");
        flightSearchPage.selectDate(departureDate);

        step("Add passenger");
        flightSearchPage.addPassenger(adultPassengers,childPassengers,infantPassengers);

        step("Search flight");
        flightSearchPage.searchFlight();

        step("verify meet travel policy for flight");
        Assertion.assertThat("Is meet travel policy flight", flightSearchResultPage.isMeetTravelPolicyAll(airline), is(true));
    }

    @Author(names = {"Yazid"})
    @Module(name = "TripView")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void verifyTripViewItinerary() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyTripViewItinerary");
        BaseTest.paramsThreadLocal.set(param);

        // use account that make the booking is auto-approved
        String username = param.get("username");
        String password = param.get("password");
        String currency = param.get("currency");

        // this location param should be represent the hotel location and to-do product location
        String location = param.get("location");

        // hotel data
        String hotelName = param.get("hotelName");
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));
        int room = Integer.parseInt(param.get("room"));

        // ttd data
        String productUrlName = param.get("productUrlName");
        boolean refundable = Boolean.parseBoolean(param.get("refundable"));

        String[] salutationList = { "Mr", "Mrs", "Ms" };
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = getRandomObject(salutationList);

        HotelApi<?> hotelApi = new HotelApi<>(session);
        TTDApi<?> ttdApi = new TTDApi<>(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);

        // Hotel check-in date comes from the test data range; the TTD visit date follows it.
        String startDate = generateRandomDate(param.get("rangeDate"));
        EmployeeData employeeData = corporateApi.getEmployeeData(username);

        step("Create hotel issued order with corporate account via API on " + startDate);
        String hotelOrderID = hotelApi.createIssuedOrderApiB2B(
                username, password, startDate, hotelName, adult, night, room, currency,
                orderFullName, orderNameTitle, guestFullName, guestTitle, employeeData, false);
        Assertion.assertThat("Hotel Booking via API is successful for B2B account", hotelOrderID, is(not(nullValue())));

        step("Create TTD issued order via API with corporate account on the same date as the hotel: " + startDate);
        String ttdOrderID = ttdApi.createIssuedOrderApiB2B(
                username, password, currency, productUrlName, refundable,
                orderFullName, orderNameTitle, guestFullName, guestTitle, startDate, employeeData);
        Assertion.assertThat("TTD Booking via API is successful for B2B account", ttdOrderID, is(not(nullValue())));

        String hotelProductName = hotelApi.getHotelName();
        String ttdProductName = ttdApi.getProductName();

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        SwitcherPage<WebDriver> switcherPage = SwitcherPage.getInstance(session);
        TripPage<WebDriver> tripPage = TripPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("Login as B2B account " + username + " via deeplink");
        homePage.loginWithSpecificAccountViaDeeplinkOnly(username, password);
        String appLanguage = homePage.getLanguageApp();

        step("Open your order page");
        homePage.openMyOrderPage();
        orderListPage.removePopUp();

        step("Select itinerary view");
        Assertion.assertThat("Itinerary view is displayed", switcherPage.switchToTrip(), is(true));

        step("Verify hotel card '" + hotelProductName + "' is displayed under the " + location + " trip on " + startDate);
        Assertion.assertThat("Hotel card is displayed on the trip view itinerary",
                tripPage.isTripCardDisplayedOnItinerary(location, startDate, hotelProductName, appLanguage), is(true));

        step("Verify TTD card '" + ttdProductName + "' is displayed under the " + location + " trip on " + startDate);
        Assertion.assertThat("TTD card is displayed on the trip view itinerary",
                tripPage.isTripCardDisplayedOnItinerary(location, startDate, ttdProductName, appLanguage), is(true));
    }

    @Author(names = {"Ashish"})
    @Module(name = "TravelPolicy")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void testNotMeetTravelPolicyFlight() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testNotMeetTravelPolicyFlight");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightProductReviewPage<WebDriver> flightProductReviewPage = FlightProductReviewPage.getInstance(session);

        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate =CoreFunctions.addToLongDate(currentDate,daysToDepartureDate);

        int adultPassengers = Integer.parseInt(param.get("adultPassengers"));
        int childPassengers = Integer.parseInt(param.get("childPassengers"));
        int infantPassengers = Integer.parseInt(param.get("infantPassengers"));
        ArrayList<String> airline = CoreFunctions.makeAirlineArrayList(param.get("airline"));
        String username = param.get("username");
        String password = param.get("password");

        String currency = param.get("currency");

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Login as B2B account " + username + " via deeplink and change currency to " + currency);
        homePage.loginWithSpecificAccountViaDeeplink(username, password, currency);

        step("Open flights page");
        homePage.openVerticalPageDeeplink("Flights","",currency);
        flightSearchPage.closePopup();

        step("change date using deeplink");
        flightSearchPage.changeDateViaDeeplink(daysToDepartureDate,tripType);

        step("Toggle trip type { " + tripType + " }");
        flightSearchPage.toggleTriptype(tripType);

        step("select flight class [economy]");
        flightSearchPage.selectFlightClass("economy");

        step("Select origin airport");
        flightSearchPage.selectOrigin(originAirport);
        flightSearchPage.closePopup();

        step("Select destination airport");
        flightSearchPage.selectDestination(destinationAirport);

        step("Select date");
        flightSearchPage.selectDate(departureDate);

        step("Add passenger");
        flightSearchPage.addPassenger(adultPassengers,childPassengers,infantPassengers);

        step("Search flight");
        flightSearchPage.searchFlight();

        step("Open filter");
        flightSearchResultPage.openFilter();

        step("Filter airline");
        flightSearchResultPage.filterAirline(airline);

        step("Save filter");
        flightSearchResultPage.saveFilter();

        step("verify not meet travel policy for international flight");
        Assertion.assertThat("Is not meet travel policy for international flight", flightSearchResultPage.isMeetTravelPolicy(), is(false));

        step("Click flight detail chevron");
        flightSearchResultPage.selectFlight();

        step("Continue to booking form");
        flightProductReviewPage.clickSelectBtnRegular();

        step("verify not meet travel policy for flight popup");
        Assertion.assertThat("Is not meet travel policy for flight popup", flightProductReviewPage.isNotMeetTravelPolicyPopupDisplayed(), is(true));
    }

    @Author(names = {"Prerna"})
    @Module(name = "TravelPolicy")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void testDoesnotMeetTravelPolicyPopupAfterRoomIsSelectedHotel() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testDoesnotMeetTravelPolicyPopupAfterRoomIsSelectedHotel");
        BaseTest.paramsThreadLocal.set(param);

        String location = param.get("location");
        String username = param.get("username");
        String password = param.get("password");
        String star = param.get("star");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Login as B2B account " + username + " via deeplink and change currency to IDR");
        homePage.loginWithSpecificAccountViaDeeplink(username,password,"IDR");

        step("Open hotel landing page");
        homePage.openVerticalPageDeeplink("Hotel", "en", "IDR");

        step("Search stay location");
        landingPage.searchStayLocation(location);

        step("submit search");
        landingPage.clickSubmit();
        searchResultPage.waitForSrpToLoad();
        landingPage.fillNightCheckInForm();

        step("Select 5 star hotel");
        searchResultPage.selectFilterStar(star);

        step("Select first hotel");
        searchResultPage.goToFirstHotelCard();
        searchResultPage.selectFirstHotelCard();

        step("book the room");
        detailPage.seeHotelRoom();

        step("Select room from the room list");
        roomListPage.clickBookRoomWithoutSkippingRatePlan();
        Assertion.assertThat("Does not meet travel policy popup is displayed", roomListPage.doesNotMeetTravelPolicyPopUpAppears(), is(true));
    }

    @Author(names = {"Prerna"})
    @Module(name = "TravelPolicy")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void testMeetsTravelPolicyOnRoomListHotel() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testMeetsTravelPolicyOnRoomListHotel");
        BaseTest.paramsThreadLocal.set(param);

        String username = param.get("username");
        String password = param.get("password");
        String location = param.get("location");
        String star = param.get("star");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Login as B2B account " + username + " via deeplink and change currency to IDR");
        homePage.loginWithSpecificAccountViaDeeplink(username, password, "IDR");

        step("Open hotel landing page");
        homePage.openVerticalPageDeeplink("Hotel", "en", "IDR");

        step("Search stay location");
        landingPage.searchStayLocation(location);

        step("submit search");
        landingPage.clickSubmit();
        searchResultPage.waitForSrpToLoad();
        landingPage.fillNightCheckInForm();

        step("Select 3 star hotel");
        searchResultPage.selectFilterStar(star);

        step("Select first hotel");
        searchResultPage.goToFirstHotelCard();
        searchResultPage.selectFirstHotelCard();

        step("book the room");
        detailPage.seeHotelRoom();
        Assertion.assertThat("Meet Travel Policy Nudge is displayed", roomListPage.meetTravelPolicyDisplayed(), is(true));
    }

    @Author(names = {"Prerna"})
    @Module(name = "TravelPolicy")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void testAddonsAreInactiveOnBookingPageHotel() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testAddonsAreInactiveOnBookingPageHotel");
        BaseTest.paramsThreadLocal.set(param);

        String username = param.get("username");
        String password = param.get("password");
        String location = param.get("location");
        String star = param.get("star");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Login as B2B account " + username + " via deeplink and change currency to IDR");
        homePage.loginWithSpecificAccountViaDeeplink(username, password, "IDR");

        step("Open hotel landing page");
        homePage.openVerticalPageDeeplink("Hotel", "en", "IDR");

        step("Search stay location");
        landingPage.searchStayLocation(location);

        step("submit search");
        landingPage.clickSubmit();
        searchResultPage.waitForSrpToLoad();
        landingPage.fillNightCheckInForm();

        step("Select 3 star hotel");
        searchResultPage.selectFilterStar(star);

        step("Open hotel PDP");
        searchResultPage.selectFirstHotelCard();

        step("Scroll down to room list");
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();

        step("Open hotel booking page");
        roomListPage.clickBookRoom();

        step("Fill booking detail");
        bookingFormPage.fillGuestDetail(false);

        step("go to addon page");
        bookingFormPage.continueToAddOnPage();

        step("Check addons are inactive");
        Assertion.assertThat("is Addon There On Booking Form", bookingFormPage.isAddonThereOnBookingForm(), is(false));
    }

    @Author(names = {"Ashish"})
    @Module(name = "MeetTravelPolicy")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void testMeetTravelPolicyFlightSRPWhenPolicyIsSetForFewAirlines() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testMeetTravelPolicyFlightSRPWhenPolicyIsSetForFewAirlines");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightPage<WebDriver> flightPage = FlightPage.getInstance(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);

        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate =CoreFunctions.addToLongDate(currentDate,daysToDepartureDate);

        int adultPassengers = Integer.parseInt(param.get("adultPassengers"));
        int childPassengers = Integer.parseInt(param.get("childPassengers"));
        int infantPassengers = Integer.parseInt(param.get("infantPassengers"));
        ArrayList<String> airlines = CoreFunctions.makeAirlineArrayList(param.get("airline"));
        String airline2 = param.get("airlineWithoutPolicy");
        String username = param.get("username");
        String password = param.get("password");

        String currency = param.get("currency");

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Login as B2B account " + username + " via deeplink and change currency to " + currency);
        homePage.loginWithSpecificAccountViaDeeplink(username, password, currency);

        step("Open flights page");
        homePage.openVerticalPageDeeplink("Flights","",currency);
        flightSearchPage.closePopup();

        step("Toggle trip type");
        flightSearchPage.toggleTriptype(tripType);

        step("select flight class [economy]");
        flightSearchPage.selectFlightClass("economy");

        step("Select origin airport");
        flightSearchPage.selectOrigin(originAirport);
        flightSearchPage.closePopup();

        step("Select destination airport");
        flightSearchPage.selectDestination(destinationAirport);

        step("Select date");
        flightSearchPage.selectDate(departureDate);

        step("Add passenger");
        flightSearchPage.addPassenger(adultPassengers,childPassengers,infantPassengers);

        step("Search flight");
        flightSearchPage.searchFlight();

        step("Open filter");
        flightSearchResultPage.openFilter();

        step("Filter airline " + airlines);
        flightSearchResultPage.filterAirline(airlines);

        step("Save filter");
        flightSearchResultPage.saveFilter();

        step("verify meet travel policy for flight");
        Assertion.assertThat("Is meet travel policy flight" + airlines, flightSearchResultPage.isMeetTravelPolicy(), is(true));

        step("Open filter");
        flightSearchResultPage.openFilter();

        step("Reset Filter");
        flightSearchResultPage.resetFilter();

        step("Filter airline "+ airline2);
        flightSearchResultPage.filterAirline(airline2);

        step("Save filter");
        flightSearchResultPage.saveFilter();

        step("verify meet travel policy for flight");
        Assertion.assertThat("Meet Travel Policy not available for "+ airline2, flightSearchResultPage.isMeetTravelPolicy(), is(false));
    }

    @Author(names = {"Prerna","Ashish"})
    @Module(name = "Booking")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void testCorporateUserCanDoCompleteBookingWithTravelPolicy() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testCorporateUserCanDoCompleteBookingWithTravelPolicy");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightProductReviewPage<WebDriver> flightProductReviewPage = FlightProductReviewPage.getInstance(session);
        FlightBookingFormPage<WebDriver> flightBookingFormPage = FlightBookingFormPage.getInstance(session);
        FlightPage<WebDriver> flightPage = FlightPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        CorporateLoginPage<WebDriver> corporateLoginPage = CorporateLoginPage.getInstance(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);
        TravelPlanWeb<?> travelPlanWeb = TravelPlanWeb.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        FlightApi<?> flightApi = new FlightApi<>(session);

        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        String airline = param.get("airline");
        String username = param.get("username");
        String password = param.get("password");

        String currency = param.get("currency");
        String adminUsername = param.get("adminUsername");
        String adminPassword = param.get("adminPassword");
        int minPrice = Integer.parseInt(param.get("minPrice"));
        int maxPrice = Integer.parseInt(param.get("maxPrice"));
        int advanceBookingDay = Integer.parseInt(param.get("advanceBookingDay"));
        String policyName = param.get("policyName");

        step("get related data via api");
        String accessTokenAdmin = corporateApi.authLoginB2B(adminUsername,adminPassword);
        long accountIdAdmin = corporateApi.getAccountId(accessTokenAdmin);
        EmployeeData employeeDataAdmin = corporateApi.getEmployeeData(adminUsername);
        EmployeeData employeeDataUser = corporateApi.getEmployeeData(username);

        step("patch travel policy");
        int travelPolicyID = corporateApi.patchTravelPolicyFlight(accessTokenAdmin,employeeDataAdmin,accountIdAdmin,currency,minPrice,maxPrice,"",airline,advanceBookingDay,false,false,policyName);
        Assertion.assertThat("Travel Policy patched", travelPolicyID, not(-1));

        step("assign travel policy to user");
        boolean isPolicyAssign = corporateApi.assignTravelPolicyToUser(accountIdAdmin,employeeDataAdmin,employeeDataUser,travelPolicyID);
        Assertion.assertThat("Travel Policy assigned", isPolicyAssign, is(true));

        step("set the search to display all in srp");
        boolean isSettingDone = corporateApi.setSearchTypePolicy(employeeDataAdmin,accountIdAdmin,"search",true);
        Assertion.assertThat("Search Type Policy set", isSettingDone, is(true));

        step("Find dynamic data");
        FlightDynamicData flightDynamicData = flightApi.findDate(
                username,password,
                originAirport,destinationAirport,daysToDepartureDate,
                adultPassengers,childPassengers,infantPassengers,
                tripType,airline,"","","",currency
        );
        originAirport = flightDynamicData.originAirport;
        destinationAirport = flightDynamicData.destinationAirport;
        String departureDate = CoreFunctions.convertToLongDate(flightDynamicData.departureDate);
        airline = flightDynamicData.airlineDepart;

        if (Configs.ENVIRONMENT == Environment.PREPROD) {
            step("Validate all suppliers are mock-issued");
            flightApi.validateAllSuppliersAreMockIssuedByAirline(flightDynamicData.airlineCodeDepart, flightDynamicData.originAirport, flightDynamicData.destinationAirport);
        }

        step("User open app");
        homePage.gotoHome();
        loginPage.loginAsGuest();

        step("B2B login");
        corporateLoginPage.corporateLogin(username,password);

        step("Change currency");
        homePage.changeCurrency("IDR",username);

        step("Open flights page");
        homePage.openVerticalPageDeeplink("Flights","",currency);
        flightSearchPage.closePopup();

        step("change date using deeplink");
        flightSearchPage.changeDateViaDeeplink(flightDynamicData, tripType);

        step("Toggle trip type { " + tripType + " }");
        flightSearchPage.toggleTriptype(tripType);

        step("Select origin airport");
        flightSearchPage.selectOrigin(originAirport);
        flightSearchPage.closePopup();

        step("Select destination airport");
        flightSearchPage.selectDestination(destinationAirport);

        step("Select date");
        flightSearchPage.selectDate(departureDate);

        step("Add passenger");
        flightSearchPage.addPassenger(adultPassengers,childPassengers,infantPassengers);

        step("Search flight");
        flightSearchPage.searchFlight();

        step("Open filter");
        flightSearchResultPage.openFilter();

        step("Filter airline");
        flightSearchResultPage.filterAirline(airline);

        step("Save filter");
        flightSearchResultPage.saveFilter();

        step("verify meet travel policy for flight");
        Assertion.assertThat("Is meet travel policy flight" + airline, flightSearchResultPage.isMeetTravelPolicy(), is(true));

        step("Click flight detail chevron");
        flightSearchResultPage.selectFlight();

        step("Continue to booking form");
        flightProductReviewPage.continueToBookingForm(tripType);

        step("verify date format");
        flightBookingFormPage.verifyDateFormat(tripType);

        step("Fill passenger detail");
        flightBookingFormPage.fillPassengerDetail(adultPassengers,childPassengers,infantPassengers, "domestic",airline);

        step("Continue to second half booking form");
        flightBookingFormPage.continueToSecondHalf();

        step("Continue to payment");
        flightBookingFormPage.continueToPayment();

        step("Skip full protection");
        flightBookingFormPage.skipFullProtection();
        webViewPage.switchToWebViewUrl("corporate/interim");
        travelPlanWeb.skipTravelPlan();
        webViewPage.switchToNative();

        step("Payment page should be displayed");
        String orderID = paymentPage.checkOrderID();
        orderID = String.valueOf(getOrderId(orderID));

        step("Submit payment using Corporate Billing");
        paymentPage.clickPayButtonWithoutWaitForPreviousBookingPopup();

        step("verify successfully placed order popup from corporate");
        Assertion.assertThat("get successfully placed order popup",paymentPage.isCorporateSuccessfullyPlacedOrderPopup(),is(true));

        step("verify meet travel policy in the corporate dashboard via api");
        boolean isMeetTravelPolicy = corporateApi.checkPurchaseOrderMeetTravelPolicy(orderID, adminUsername, adminPassword);
        Assertion.assertThat("is meet travel policy shown", isMeetTravelPolicy, is(true));
    }

    @Author(names = {"Prerna","Ashish"})
    @Module(name = "MeetTravelPolicy")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void testMeetTravelPolicyFlightRoundTripSRPWhenPolicyIsSetForFewAirlines() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testMeetTravelPolicyFlightRoundTripSRPWhenPolicyIsSetForFewAirlines");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightPage<WebDriver> flightPage = FlightPage.getInstance(session);

        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int adultPassengers = Integer.parseInt(param.get("adultPassengers"));
        int childPassengers = Integer.parseInt(param.get("childPassengers"));
        int infantPassengers = Integer.parseInt(param.get("infantPassengers"));
        String airline = param.get("airline");
        String username = param.get("username");
        String password = param.get("password");

        String currency = param.get("currency");
        int duration = Integer.parseInt(param.get("duration"));

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Find dynamic data");
        flightPage.setDataRoundTrip(daysToDepartureDate, duration, airline, airline);
        String departureDate =CoreFunctions.convertToLongDate (flightPage.getDate("departureDate"));
        String returnDate =CoreFunctions.convertToLongDate(flightPage.getDate("returnDate"));
        airline = flightPage.getDate("airline");

        step("Login as B2B account " + username + " via deeplink and change currency to " + currency);
        homePage.loginWithSpecificAccountViaDeeplink(username, password, currency);

        step("Open flights page");
        homePage.openVerticalPageDeeplink("Flights","",currency);
        flightSearchPage.closePopup();

        step("change date using deeplink");
        flightSearchPage.changeDateViaDeeplink(daysToDepartureDate,duration,tripType);

        step("Toggle trip type { " + tripType + " }");
        flightSearchPage.toggleTriptype(tripType);

        step("Select origin airport");
        flightSearchPage.selectOrigin(originAirport);
        flightSearchPage.closePopup();

        step("Select destination airport");
        flightSearchPage.selectDestination(destinationAirport);

        step("Select date");
        flightSearchPage.selectDate(departureDate,returnDate);

        step("Add passenger");
        flightSearchPage.addPassenger(adultPassengers,childPassengers,infantPassengers);

        step("select flight class [economy]");
        flightSearchPage.selectFlightClass("economy");

        step("Search flight");
        flightSearchPage.searchFlight();

        step("Open filter");
        flightSearchResultPage.openFilter();

        step("Filter airline");
        flightSearchResultPage.filterAirline(airline);

        step("Save filter");
        flightSearchResultPage.saveFilter();

        step("verify meet travel policy for flight");
        Assertion.assertThat("Is meet travel policy departure flight" + airline, flightSearchResultPage.isMeetTravelPolicy(), is(true));

        step("Click flight detail chevron");
        flightSearchResultPage.selectFlight();

        step("Open filter");
        flightSearchResultPage.openFilter();

        step("Filter airline");
        flightSearchResultPage.filterAirline(airline);

        step("Save filter");
        flightSearchResultPage.saveFilter();

        step("verify meet travel policy for flight");
        Assertion.assertThat("Is meet travel policy return flight" + airline, flightSearchResultPage.isMeetTravelPolicy(), is(true));
    }

    @Author(names = {"Zia","Kartik","Mridul"})
    @Module(name = "corporateCode")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void corporateCodeOneWaySrpNudgeApplied() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("corporateCodeOneWaySrpNudgeApplied");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        CorporateLoginPage<WebDriver> corporateLoginPage = CorporateLoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);

        String username = param.get("username");
        String password = param.get("password");
        String currency = param.get("currency");
        String tripType = param.get("tripType");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        Map<String,String> scheduleData = CoreFunctions.parseDelimitedSectionToMap( param.get("scheduleData"));

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginAsGuest();
        corporateLoginPage.corporateLogin(username, password);

        step("Change currency");
        homePage.changeCurrency(currency,username);

        step("Open flights page");
        homePage.openVerticalPageDeeplink("Flights","",currency);
        flightSearchPage.closePopup();

        step("change date using deeplink");
        flightSearchPage.changeDateViaDeeplink(daysToDepartureDate,tripType);

        step("Toggle trip type { " + tripType + " }");
        flightSearchPage.toggleTriptype(tripType);

        for (Map.Entry<String, String> entry : scheduleData.entrySet()) {
            String[] key = entry.getKey().split("-");
            String value = entry.getValue();
            String originAirport = key[0];
            String destinationAirport = key[1];

            step("Select origin airport");
            flightSearchPage.selectOrigin(originAirport);
            flightSearchPage.closePopup();

            step("Select destination airport");
            flightSearchPage.selectDestination(destinationAirport);

            step("Search flight");
            flightSearchPage.searchFlight();

            step("Open filter");
            flightSearchResultPage.openFilter();

            step("Filter airline for "+ value);
            flightSearchResultPage.filterAirline(value);
            flightSearchResultPage.saveFilter();

            step("corporate deals verification");
            Assertion.assertThat("Corporate deals benefit on the card is present: ", flightSearchResultPage.gotoCorporateDealsBenefitCard(), is(true));

            step("Go back to flight search page");
            flightSearchResultPage.backToFlightPage();
        }
    }

    @Author(names = {"Zia","Kartik"})
    @Module(name = "corporateCode")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void corporateCodeOneWaySrpNudgeNotApplied() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("corporateCodeOneWaySrpNudgeNotApplied");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        CorporateLoginPage<WebDriver> corporateLoginPage = CorporateLoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);

        String username = param.get("username");
        String password = param.get("password");
        String currency = param.get("currency");
        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");

        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate,daysToDepartureDate);
        ArrayList<String> airline = CoreFunctions.makeAirlineArrayList(param.get("airline"));

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginAsGuest();
        corporateLoginPage.corporateLogin(username, password);

        step("Change currency");
        homePage.changeCurrency(currency, username);

        step("Open flights page");
        homePage.openVerticalPageDeeplink("Flights","",currency);
        flightSearchPage.closePopup();

        step("change date using deeplink");
        flightSearchPage.changeDateViaDeeplink(daysToDepartureDate,tripType);

        step("Toggle trip type { " + tripType + " }");
        flightSearchPage.toggleTriptype(tripType);

        step("Select origin airport");
        flightSearchPage.selectOrigin(originAirport);
        flightSearchPage.closePopup();

        step("Select destination airport");
        flightSearchPage.selectDestination(destinationAirport);

        step("Select date");
        flightSearchPage.selectDate(departureDate);

        step("Add passenger");
        flightSearchPage.addPassenger(adultPassengers, childPassengers, infantPassengers);

        step("Search flight");
        flightSearchPage.searchFlight();

        step("Filter airline for " + airline);
        flightSearchResultPage.openFilter();
        flightSearchResultPage.filterAirline(airline);

        step("Save filter");
        flightSearchResultPage.saveFilter();

        step("corporate deal verification");
        Assertion.assertThat("Airlines should include " + airline, new HashSet<>(flightSearchResultPage.getAirlineNames(airline)), is(new HashSet<>(airline)));
        Assertion.assertThat("Corporate deals benefit on the card is not present", flightSearchResultPage.gotoCorporateDealsBenefitCard(), is(false));
    }

    @Author(names = {"Ashish"})
    @Module(name = "corporateCode")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void corporateCodeRoundTripSRPNudgeApplied() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("corporateCodeRoundTripSRPNudgeApplied");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        CorporateLoginPage<WebDriver> corporateLoginPage = CorporateLoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);

        String username = param.get("username");
        String password = param.get("password");
        String currency = param.get("currency");
        String tripType = param.get("tripType");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int duration = Integer.parseInt(param.get("duration"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate,daysToDepartureDate);
        String returnDate = CoreFunctions.addToLongDate(departureDate,duration);

        Map<String,String> scheduleData = CoreFunctions.parseDelimitedSectionToMap( param.get("scheduleData"));

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginAsGuest();
        corporateLoginPage.corporateLogin(username, password);

        step("Change currency");
        homePage.changeCurrency(currency,username);

        step("Open flights page");
        homePage.openVerticalPageDeeplink("Flights","",currency);
        flightSearchPage.closePopup();

        step("change date using deeplink");
        flightSearchPage.changeDateViaDeeplink(daysToDepartureDate,duration,tripType);

        step("select date");
        flightSearchPage.selectDate(departureDate,returnDate);

        step("Toggle trip type { " + tripType + " }");
        flightSearchPage.toggleTriptype(tripType);

        for (Map.Entry<String, String> entry : scheduleData.entrySet()) {
            String[] key = entry.getKey().split("-");
            String airline = entry.getValue();
            String originAirport = key[0];
            String destinationAirport = key[1];

            step("Select origin airport");
            flightSearchPage.selectOrigin(originAirport);
            flightSearchPage.closePopup();

            step("Select destination airport");
            flightSearchPage.selectDestination(destinationAirport);

            step("Search flight");
            flightSearchPage.searchFlight();

            step("Filter airline for "+ airline);
            flightSearchResultPage.openFilter();
            flightSearchResultPage.filterAirline(airline);

            step("Save filter");
            flightSearchResultPage.saveFilter();

            step("corporate deal verification");
            Assertion.assertThat("Corporate deals benefit on the card is present depart airline : " + airline, flightSearchResultPage.gotoCorporateDealsBenefitCard(), is(true));

            step("select flight");
            flightSearchResultPage.selectFlight();

            step("Filter airline for "+ airline);
            flightSearchResultPage.openFilter();
            flightSearchResultPage.filterAirline(airline);

            step("Save filter");
            flightSearchResultPage.saveFilter();

            step("corporate deal verification");
            Assertion.assertThat("Corporate deals benefit on the card is present return airline: " + airline, flightSearchResultPage.gotoCorporateDealsBenefitCard(), is(true));

            step("Go back to flight search page");
            flightSearchResultPage.backToFlightPage();
            flightSearchResultPage.backToFlightPage();
        }
    }

    @Author(names = {"Kartik","Ashish"})
    @Module(name = "corporateCode")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void corporateCodeRoundTripSRPNudgeNotApplied() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("corporateCodeRoundTripSRPNudgeNotApplied");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        CorporateLoginPage<WebDriver> corporateLoginPage = CorporateLoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);

        String username = param.get("username");
        String password = param.get("password");
        String currency = param.get("currency");
        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int duration = Integer.parseInt(param.get("duration"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate,daysToDepartureDate);
        String returnDate = CoreFunctions.addToLongDate(departureDate,duration);

        ArrayList<String> airline = CoreFunctions.makeAirlineArrayList(param.get("airline"));
        String airlineDepart = param.get("airlineDepart");

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginAsGuest();
        corporateLoginPage.corporateLogin(username, password);

        step("Change currency");
        homePage.changeCurrency(currency,username);

        step("Open flights page");
        homePage.openVerticalPageDeeplink("Flights","",currency);
        flightSearchPage.closePopup();

        step("change date using deeplink");
        flightSearchPage.changeDateViaDeeplink(daysToDepartureDate,duration,tripType);

        step("Toggle trip type { " + tripType + " }");
        flightSearchPage.toggleTriptype(tripType);

        step("Select origin airport");
        flightSearchPage.selectOrigin(originAirport);
        flightSearchPage.closePopup();

        step("Select destination airport");
        flightSearchPage.selectDestination(destinationAirport);

        step("Select date");
        flightSearchPage.selectDate(departureDate,returnDate);

        step("Add passenger");
        flightSearchPage.addPassenger(adultPassengers,childPassengers,infantPassengers);

        step("Search flight");
        flightSearchPage.searchFlight();

        step("Filter airline for "+ airline);
        flightSearchResultPage.openFilter();
        flightSearchResultPage.filterAirline(airline);

        step("Save filter");
        flightSearchResultPage.saveFilter();

        step("corporate deal verification");
        Assertion.assertThat("Airlines should include " + airline, new HashSet<>(flightSearchResultPage.getAirlineNames(airline)), is(new HashSet<>(airline)));
        Assertion.assertThat("Corporate deals benefit on the card is not present", flightSearchResultPage.gotoCorporateDealsBenefitCard(), is(false));

        step("Open filter");
        flightSearchResultPage.openFilter();

        step("Filter airline for "+ airlineDepart);
        flightSearchResultPage.resetFilter();
        flightSearchResultPage.filterAirline(airlineDepart);

        step("Save filter");
        flightSearchResultPage.saveFilter();

        step("select flight");
        flightSearchResultPage.selectFlight();

        step("Filter airline for "+ airline);
        flightSearchResultPage.openFilter();
        flightSearchResultPage.filterAirline(airline);

        step("Save filter");
        flightSearchResultPage.saveFilter();

        step("corporate deal verification");
        Assertion.assertThat("Airlines should include " + airline, new HashSet<>(flightSearchResultPage.getAirlineNames(airline)), is(new HashSet<>(airline)));
        Assertion.assertThat("Corporate deals benefit on the card is not present", flightSearchResultPage.gotoCorporateDealsBenefitCard(), is(false));
    }

    @Author(names = {"Prerna","kartik"})
    @Module(name = "corporateCode")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void corporateCodeOneWayInternationalSrpNudgeApplied() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("corporateCodeOneWayInternationalSrpNudgeApplied");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        CorporateLoginPage<WebDriver> corporateLoginPage = CorporateLoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);

        String username = param.get("username");
        String password = param.get("password");
        String currency = param.get("currency");
        String tripType = param.get("tripType");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate,daysToDepartureDate);

        Map<String,String> scheduleData = CoreFunctions.parseDelimitedSectionToMap( param.get("scheduleData"));

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginAsGuest();
        corporateLoginPage.corporateLogin(username, password);

        step("Change currency");
        homePage.changeCurrency(currency,username);

        step("Open flights page");
        homePage.openVerticalPageDeeplink("Flights","",currency);
        flightSearchPage.closePopup();

        step("change date using deeplink");
        flightSearchPage.changeDateViaDeeplink(daysToDepartureDate,tripType);
        flightSearchPage.selectDate(departureDate);

        step("Toggle trip type { " + tripType + " }");
        flightSearchPage.toggleTriptype(tripType);

        for (Map.Entry<String, String> entry : scheduleData.entrySet()) {
            String[] key = entry.getKey().split("-");
            String value = entry.getValue();
            String originAirport = key[0];
            String destinationAirport = key[1];

            step("Select origin airport");
            flightSearchPage.selectOrigin(originAirport);
            flightSearchPage.closePopup();

            step("Select destination airport");
            flightSearchPage.selectDestination(destinationAirport);

            step("Search flight");
            flightSearchPage.searchFlight();

            step("filter corporate code");
            flightSearchResultPage.filterOther("Corporate Deals");

            step("Open filter");
            flightSearchResultPage.openFilter();

            step("Filter airline for "+ value);
            flightSearchResultPage.filterAirline(value);
            flightSearchResultPage.saveFilter();

            step("corporate deals verification");
            Assertion.assertThat("Corporate deals benefit on the card is present: ", flightSearchResultPage.gotoCorporateDealsBenefitCard(), is(true));

            step("Go back to flight search page");
            flightSearchResultPage.backToFlightPage();
        }
    }

    @Author(names = {"Ashish"})
    @Module(name = "corporateCode")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void corporateCodeRoundTripInternationalSRPNudgeApplied() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("corporateCodeRoundTripInternationalSRPNudgeApplied");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        CorporateLoginPage<WebDriver> corporateLoginPage = CorporateLoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);

        String username = param.get("username");
        String password = param.get("password");
        String currency = param.get("currency");
        String tripType = param.get("tripType");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int duration = Integer.parseInt(param.get("duration"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate,daysToDepartureDate);
        String returnDate = CoreFunctions.addToLongDate(departureDate,duration);
        Map<String,String> scheduleData = CoreFunctions.parseDelimitedSectionToMap( param.get("scheduleData"));

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginAsGuest();
        corporateLoginPage.corporateLogin(username, password);

        step("Change currency");
        homePage.changeCurrency(currency,username);

        step("Open flights page");
        homePage.openVerticalPageDeeplink("Flights","",currency);
        flightSearchPage.closePopup();

        step("Toggle trip type { " + tripType + " }");
        flightSearchPage.toggleTriptype(tripType);

        step("change date using deeplink");
        flightSearchPage.changeDateViaDeeplink(daysToDepartureDate,duration,tripType);
        flightSearchPage.selectDate(departureDate,returnDate);

        for (Map.Entry<String, String> entry : scheduleData.entrySet()) {
            String[] key = entry.getKey().split("-");
            String airline = entry.getValue();
            String originAirport = key[0];
            String destinationAirport = key[1];

            step("Select origin airport");
            flightSearchPage.selectOrigin(originAirport);
            flightSearchPage.closePopup();

            step("Select destination airport");
            flightSearchPage.selectDestination(destinationAirport);

            step("Search flight");
            flightSearchPage.searchFlight();

            step("filter corporate code");
            flightSearchResultPage.filterOther("Corporate Deals");

            step("Filter airline for "+ airline);
            flightSearchResultPage.openFilter();
            flightSearchResultPage.filterAirline(airline);

            step("Save filter");
            flightSearchResultPage.saveFilter();

            step("corporate deal verification");
            Assertion.assertThat("Corporate deals benefit on the card is present depart airline : " + airline, flightSearchResultPage.gotoCorporateDealsBenefitCard(), is(true));

            step("select flight");
            flightSearchResultPage.selectFlight();

            step("filter corporate code");
            flightSearchResultPage.filterOther("Corporate Deals");

            step("Filter airline for "+ airline);
            flightSearchResultPage.openFilter();
            flightSearchResultPage.filterAirline(airline);

            step("Save filter");
            flightSearchResultPage.saveFilter();

            step("corporate deal verification");
            Assertion.assertThat("Corporate deals benefit on the card is present return airline: " + airline, flightSearchResultPage.gotoCorporateDealsBenefitCard(), is(true));

            step("Go back to flight search page");
            flightSearchResultPage.backToFlightPage();
            flightSearchResultPage.backToFlightPage();
        }
    }

    @Author(names = {"Kartik"})
    @Module(name = "corporateCode")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void corporateCodeRoundTripInternationalSRPNudgeNotApplied() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("corporateCodeRoundTripInternationalSRPNudgeNotApplied");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        CorporateLoginPage<WebDriver> corporateLoginPage = CorporateLoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);

        String username = param.get("username");
        String password = param.get("password");
        String currency = param.get("currency");
        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int duration = Integer.parseInt(param.get("duration"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate,daysToDepartureDate);
        String returnDate = CoreFunctions.addToLongDate(departureDate,duration);

        ArrayList<String> airline = CoreFunctions.makeAirlineArrayList(param.get("airline"));
        String airlineDepart = param.get("airlineDepart");

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginAsGuest();
        corporateLoginPage.corporateLogin(username, password);

        step("Change currency");
        homePage.changeCurrency(currency,username);

        step("Open flights page");
        homePage.openVerticalPageDeeplink("Flights","",currency);
        flightSearchPage.closePopup();

        step("change date using deeplink");
        flightSearchPage.changeDateViaDeeplink(daysToDepartureDate,duration,tripType);

        step("Toggle trip type { " + tripType + " }");
        flightSearchPage.toggleTriptype(tripType);

        step("Select origin airport");
        flightSearchPage.selectOrigin(originAirport);
        flightSearchPage.closePopup();

        step("Select destination airport");
        flightSearchPage.selectDestination(destinationAirport);

        step("Select date");
        flightSearchPage.selectDate(departureDate,returnDate);

        step("Add passenger");
        flightSearchPage.addPassenger(adultPassengers,childPassengers,infantPassengers);

        step("Search flight");
        flightSearchPage.searchFlight();

        step("Filter airline for "+ airline);
        flightSearchResultPage.openFilter();
        flightSearchResultPage.filterAirline(airline);

        step("Save filter");
        flightSearchResultPage.saveFilter();

        step("corporate deal verification");
        Assertion.assertThat("Airlines should include " + airline, new HashSet<>(flightSearchResultPage.getAirlineNames(airline)), is(new HashSet<>(airline)));
        Assertion.assertThat("Corporate deals benefit on the card is not present", flightSearchResultPage.gotoCorporateDealsBenefitCard(), is(false));

        step("Open filter");
        flightSearchResultPage.openFilter();

        step("Filter airline for "+ airlineDepart);
        flightSearchResultPage.resetFilter();
        flightSearchResultPage.filterAirline(airlineDepart);

        step("Save filter");
        flightSearchResultPage.saveFilter();

        step("select flight");
        flightSearchResultPage.selectFlight();

        step("Filter airline for "+ airline);
        flightSearchResultPage.openFilter();
        flightSearchResultPage.filterAirline(airline);

        step("Save filter");
        flightSearchResultPage.saveFilter();

        step("corporate deal verification");
        Assertion.assertThat("Airlines should include " + airline, new HashSet<>(flightSearchResultPage.getAirlineNames(airline)), is(new HashSet<>(airline)));
        Assertion.assertThat("Coporate deals benefit on the card is not present", flightSearchResultPage.gotoCorporateDealsBenefitCard(), is(false));
    }

    @Author(names = {"Kartik"})
    @Module(name = "corporateCode")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void corporateCodeOneWayInternationalSRPNudgeNotApplied() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("corporateCodeOneWayInternationalSRPNudgeNotApplied");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        CorporateLoginPage<WebDriver> corporateLoginPage = CorporateLoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);

        String username = param.get("username");
        String password = param.get("password");
        String currency = param.get("currency");
        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");

        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate,daysToDepartureDate);

        ArrayList<String> airline = CoreFunctions.makeAirlineArrayList(param.get("airline"));

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginAsGuest();
        corporateLoginPage.corporateLogin(username, password);

        step("Change currency");
        homePage.changeCurrency(currency,username);

        step("Open flights page");
        homePage.openVerticalPageDeeplink("Flights","",currency);
        flightSearchPage.closePopup();

        step("change date using deeplink");
        flightSearchPage.changeDateViaDeeplink(daysToDepartureDate,tripType);

        step("Toggle trip type { " + tripType + " }");
        flightSearchPage.toggleTriptype(tripType);

        step("Select origin airport");
        flightSearchPage.selectOrigin(originAirport);
        flightSearchPage.closePopup();

        step("Select destination airport");
        flightSearchPage.selectDestination(destinationAirport);

        step("Select date");
        flightSearchPage.selectDate(departureDate);

        step("Add passenger");
        flightSearchPage.addPassenger(adultPassengers,childPassengers,infantPassengers);

        step("Search flight");
        flightSearchPage.searchFlight();

        step("Filter airline for "+ airline);
        flightSearchResultPage.openFilter();
        flightSearchResultPage.filterAirline(airline);

        step("Save filter");
        flightSearchResultPage.saveFilter();

        step("corporate deal verification");
        Assertion.assertThat("Airlines should include " + airline, new HashSet<>(flightSearchResultPage.getAirlineNames(airline)), is(new HashSet<>(airline)));
        Assertion.assertThat("Corporate deals benefit on the card is not present", flightSearchResultPage.gotoCorporateDealsBenefitCard(), is(false));
    }

    @Author(names = {"Aditi","Ashish"})
    @Module(name = "corporateCodeBooking")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void flightBookingWithCorporateDealNotAppliedRoundTrip() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("flightBookingWithCorporateDealNotAppliedRoundTrip");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        FlightPage<WebDriver> flightPage = FlightPage.getInstance(session);
        CorporateLoginPage<WebDriver> corporateLoginPage = CorporateLoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightProductReviewPage<WebDriver> flightProductReviewPage = FlightProductReviewPage.getInstance(session);
        FlightBookingFormPage<WebDriver> flightBookingFormPage = FlightBookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);
        TravelPlanWeb<?> travelPlanWeb = TravelPlanWeb.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        FlightApi<?> flightApi = new FlightApi<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String adminEmail = param.get("adminUsername");
        String adminPassword = param.get("adminPassword");

        String currency = param.get("currency");
        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");

        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int duration = Integer.parseInt(param.get("duration"));
        String airline = param.get("airline");

        //steps for gk and pre-prod only
        if (Configs.ENVIRONMENT == Environment.GK || Configs.ENVIRONMENT == Environment.PREPROD) {
            step("Delete the corporate codes for both airlines");
            boolean isIdDeleted = corporateApi.deleteAllCorporateCode(adminEmail, adminPassword);
            Assertion.assertThat("Success to deleted the corporate code", isIdDeleted, is(true));
        }

        step("Find dynamic data");
        FlightDynamicData flightDynamicData = flightApi.findDate(
                username,password,
                originAirport,destinationAirport,daysToDepartureDate,
                adultPassengers,childPassengers,infantPassengers,
                tripType,airline,"","","",currency
        );
        originAirport = flightDynamicData.originAirport;
        destinationAirport = flightDynamicData.destinationAirport;
        String departureDate = CoreFunctions.convertToLongDate(flightDynamicData.departureDate);
        String returnDate = CoreFunctions.convertToLongDate(flightDynamicData.returnDate);
        airline = flightDynamicData.airlineDepart;

        if (Configs.ENVIRONMENT == Environment.PREPROD) {
            step("Validate all suppliers are mock-issued");
            flightApi.validateAllSuppliersAreMockIssuedByAirline(flightDynamicData.airlineCodeDepart, flightDynamicData.originAirport, flightDynamicData.destinationAirport);
            //check for return trip
            if(tripType.equalsIgnoreCase("roundtrip")){
                flightApi.validateAllSuppliersAreMockIssuedByAirline(flightDynamicData.airlineCodeDepart, flightDynamicData.destinationAirport, flightDynamicData.originAirport);
            }

        }

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginAsGuest();
        corporateLoginPage.corporateLogin(username, password);

        step("Change currency");
        homePage.changeCurrency(currency,username);

        step("Open flights page");
        homePage.openVerticalPageDeeplink("Flights","",currency);
        flightSearchPage.closePopup();

        step("change date using deeplink");
        flightSearchPage.changeDateViaDeeplink(flightDynamicData, tripType);

        step("Toggle trip type { " + tripType + " }");
        flightSearchPage.toggleTriptype(tripType);

        step("Select origin airport");
        flightSearchPage.selectOrigin(originAirport);
        flightSearchPage.closePopup();

        step("Select destination airport");
        flightSearchPage.selectDestination(destinationAirport);

        step("Select date");
        flightSearchPage.selectDate(departureDate,returnDate);

        step("Add passenger");
        flightSearchPage.addPassenger(adultPassengers,childPassengers,infantPassengers);

        step("Search flight");
        flightSearchPage.searchFlight();

        step("Open filter");
        flightSearchResultPage.openFilter();

        step("Filter airline");
        flightSearchResultPage.filterAirline(airline);

        step("Save filter");
        flightSearchResultPage.saveFilter();

        step("verify corporate deals nudge & label");
        Assertion.assertThat("Corporate deals benefit on the card is not present", flightSearchResultPage.gotoCorporateDealsBenefitCard(), is(false));

        step("Select flight without Corporate Deals");
        flightSearchResultPage.selectFlight();

        step("Open filter");
        flightSearchResultPage.openFilter();

        step("Filter airline");
        flightSearchResultPage.filterAirline(airline);

        step("Save filter");
        flightSearchResultPage.saveFilter();

        step("verify corporate deals nudge & label");
        Assertion.assertThat("Corporate deals benefit on the card is not present", flightSearchResultPage.gotoCorporateDealsBenefitCard(), is(false));

        step("Click flight detail chevron");
        flightSearchResultPage.selectFlight();

        step("Continue to booking form");
        flightProductReviewPage.continueToBookingForm(tripType);

        step("Fill passenger detail");
        List<String> passengerNameList = flightBookingFormPage.fillPassengerDetail(adultPassengers,childPassengers,infantPassengers, "domestic",airline);

        step("Continue to second half booking form");
        flightBookingFormPage.continueToSecondHalf();

        step("Continue to payment");
        flightBookingFormPage.continueToPayment();

        step("Skip full protection");
        flightBookingFormPage.skipFullProtection();
        webViewPage.switchToWebViewUrl("corporate/interim");
        travelPlanWeb.skipTravelPlan();
        webViewPage.switchToNative();

        step("is order id show on payment page");
        String orderID = paymentPage.checkOrderID();
        orderID = String.valueOf(getOrderId(orderID));
        Assertion.assertThat("Successfully book the flight with corporate deals",orderID , notNullValue());

        step("Submit payment using Corporate Billing");
        paymentPage.clickPayButtonWithoutWaitForPreviousBookingPopup();

        step("verify successfully placed order popup from corporate");
        Assertion.assertThat("get successfully placed order popup",paymentPage.isCorporateSuccessfullyPlacedOrderPopup(),is(true));

        //steps for gk and pre-prod only
        if (Configs.ENVIRONMENT == Environment.GK || Configs.ENVIRONMENT == Environment.PREPROD) {
            step("verify if order reflected in corporate dashboard");
            Assertion.assertThat("purchase order is reflected on B2B corporate dashboard in purchase list", corporateApi.checkPurchaseOrderList(orderID, adminEmail, adminPassword, passengerNameList.get(0)), is(true));
        }
    }

    @Author(names = {"Aditi","Ashish"})
    @Module(name = "corporateCodeBooking")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void flightBookingWithCorporateDealAppliedRoundTrip() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("flightBookingWithCorporateDealAppliedRoundTrip");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        CorporateLoginPage<WebDriver> corporateLoginPage = CorporateLoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightProductReviewPage<WebDriver> flightProductReviewPage = FlightProductReviewPage.getInstance(session);
        FlightBookingFormPage<WebDriver> flightBookingFormPage = FlightBookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);
        TravelPlanWeb<?> travelPlanWeb = TravelPlanWeb.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        FlightApi<?> flightApi = new FlightApi<>(session);

        String travellerUsername = param.get("travellerUsername");
        String travellerPassword = param.get("travellerPassword");
        String adminEmail = param.get("username");
        String adminPassword = param.get("password");
        String currency = param.get("currency");
        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");

        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int duration = Integer.parseInt(param.get("duration"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate,daysToDepartureDate);
        String returnDate = CoreFunctions.addToLongDate(departureDate,duration);

        ArrayList<CorporateCodeData> corporateCodeData = corporateApi.setAndGetCorporateCodeData(param.get("corporateCodeData"));
        ArrayList<String> airline = corporateApi.getAirlineName(corporateCodeData);

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginAsGuest();
        corporateLoginPage.corporateLogin(travellerUsername, travellerPassword);

        step("Change currency");
        homePage.changeCurrency(currency,travellerUsername);

        step("Open flights page");
        homePage.openVerticalPageDeeplink("Flights","",currency);
        flightSearchPage.closePopup();

        step("change date using deeplink");
        flightSearchPage.changeDateViaDeeplink(daysToDepartureDate,duration,tripType);

        step("Toggle trip type { " + tripType + " }");
        flightSearchPage.toggleTriptype(tripType);

        step("Select origin airport");
        flightSearchPage.selectOrigin(originAirport);
        flightSearchPage.closePopup();

        step("Select destination airport");
        flightSearchPage.selectDestination(destinationAirport);

        step("Select date");
        flightSearchPage.selectDate(departureDate,returnDate);

        step("Add passenger");
        flightSearchPage.addPassenger(adultPassengers,childPassengers,infantPassengers);

        step("Search flight");
        flightSearchPage.searchFlight();

        step("Open filter");
        flightSearchResultPage.openFilter();

        step("Filter airline");
        flightSearchResultPage.filterAirline(airline);

        step("Save filter");
        flightSearchResultPage.saveFilter();

        step("verify corporate deals nudge & label");
        Assertion.assertThat("Corporate deals benefit on the card is present", flightSearchResultPage.gotoCorporateDealsBenefitCard(), is(true));

        step("Select flight without Corporate Deals");
        flightSearchResultPage.selectFlight();

        step("Open filter");
        flightSearchResultPage.openFilter();

        step("Filter airline");
        flightSearchResultPage.filterAirline(airline);

        step("Save filter");
        flightSearchResultPage.saveFilter();

        step("verify corporate deals nudge & label");
        Assertion.assertThat("Corporate deals benefit on the card is present", flightSearchResultPage.gotoCorporateDealsBenefitCard(), is(true));

        step("Click flight detail chevron");
        flightSearchResultPage.selectFlight();

        step("Continue to booking form");
        flightProductReviewPage.continueToBookingForm(tripType);

        step("Fill passenger detail");
        flightBookingFormPage.fillPassengerDetail(adultPassengers,childPassengers,infantPassengers, "domestic",airline.get(0));

        step("Continue to second half booking form");
        flightBookingFormPage.continueToSecondHalf();

        step("Continue to payment");
        flightBookingFormPage.continueToPayment();

        step("Skip full protection");
        flightBookingFormPage.skipFullProtection();
        webViewPage.switchToWebViewUrl("corporate/interim");
        travelPlanWeb.skipTravelPlan();
        webViewPage.switchToNative();

        step("is order id show on payment page");
        String orderID = paymentPage.checkOrderID();
        orderID = String.valueOf(getOrderId(orderID));
        Assertion.assertThat("Successfully book the flight with corporate deals",orderID , notNullValue());

        step("Submit payment using Corporate Billing");
        paymentPage.clickPayButtonWithoutWaitForPreviousBookingPopup();

        step("verify successfully placed order popup from corporate");
        Assertion.assertThat("get successfully placed order popup",paymentPage.isCorporateSuccessfullyPlacedOrderPopup(),is(true));

        step("verify if order reflected in corporate dashboard");
        Assertion.assertThat("purchase order is reflected on B2B corporate dashboard in purchase list", corporateApi.checkPurchaseOrderCorporateCode(orderID, adminEmail, adminPassword, tripType, corporateCodeData.get(0).getCorporateCode(), corporateCodeData.get(0).getAirlineName()), is(true));
    }

    @Author(names = {"Mridul"})
    @Module(name = "Booking")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void testLoginBookingBusViaB2BInstantPayment(){
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testLoginBookingBusViaB2BInstantPayment");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        CorporateLoginPage<WebDriver> corporateLoginPage = CorporateLoginPage.getInstance(session);
        BusSearchPage<WebDriver> busSearchPage = BusSearchPage.getInstance(session);
        BusSearchResultPage<WebDriver> busSearchResultPage = BusSearchResultPage.getInstance(session);
        BuspdpPage<WebDriver> buspdpPage = BuspdpPage.getInstance(session);
        BusBookingFormPage<WebDriver> busBookingFormPage = BusBookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        TravelPlanWeb<?> travelPlanWeb = TravelPlanWeb.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);

        String username = param.get("username");
        String password = param.get("password");
        String tripType = param.get("tripType");
        String originLocation = param.get("originLocation");
        String destinationLocation = param.get("destinationLocation");
        int daysToDeparture = Integer.parseInt(param.get("daysToDeparture"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String dateDeparture = CoreFunctions.addToLongDate(currentDate,daysToDeparture);
        int passenger = Integer.parseInt(param.get("passenger"));
        String name = CoreFunctions.randomName();
        String phoneNumber = CoreFunctions.randomPhone();
        String adminUsername = param.get("adminUsername");
        String adminPassword = param.get("adminPassword");

        step("User open app");
        homePage.gotoHome();
        loginPage.loginAsGuest();

        step("B2B login");
        corporateLoginPage.corporateLogin(username,password);

        step("Change currency");
        homePage.changeCurrency("IDR",username);

        step("Open Bus vertical");
        homePage.openVerticalPageDeeplink("Bus","","");

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

        step("Input contact detail");
        busBookingFormPage.togglePassengerSameAsContact();

        step("Continue to payment page");
        busBookingFormPage.continueToPaymentPage();
        webViewPage.switchToWebViewUrl("corporate/interim");
        travelPlanWeb.skipTravelPlan();
        webViewPage.switchToNative();

        step("Payment page should be displayed");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("Order id should be displayed", !orderID.isEmpty(), is(true));

        step("Open payment method");
        paymentPage.openPaymentMethod();

        step("Submit payment using Sakuku");
        paymentPage.checkSeeAllPaymentMethod();
        paymentPage.selectPaymentMethod("Sakuku");

        step("verify successful payment popup from corporate");
        Assertion.assertThat("get successfully placed order popup",paymentPage.isCorporatePaymentSuccessPopupDisplayed(),is(true));

        step("Open the order list and go to order detail");
        paymentPage.SeeOrderListAfterPaymentSuccess();

        step("Verify in Booking List that order should be present");
        Assertion.assertThat("Order is reflected on B2B corporate dashboard under booking List " , corporateApi.checkBookingOrder(orderID, username, password,"Bus", currentDate, dateDeparture), is(true));

    }

    @Author(names = {"Mridul"})
    @Module(name = "Booking")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void testLoginBookingFerryViaB2BInstantPayment(){
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testLoginBookingFerryViaB2BInstantPayment");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        CorporateLoginPage<WebDriver> corporateLoginPage = CorporateLoginPage.getInstance(session);
        FerrySearchPage<WebDriver> ferrySearchPage = FerrySearchPage.getInstance(session);
        FerrySearchResultPage<WebDriver> ferrySearchResultPage = FerrySearchResultPage.getInstance(session);
        FerryBookingPage<WebDriver> ferryBookingPage = FerryBookingPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);

        String username = param.get("username");
        String password = param.get("password");

        String originPort = param.get("originPort");
        String destinationPort = param.get("destinationPort");
        String ticketType = param.get("ticketType");
        int adult = Integer.parseInt(param.get("adult"));
        int child = Integer.parseInt(param.get("child"));
        String daysToDepart =  CoreFunctions.getEndOfCurrentMonth("d MMMM yyyy");
        String VaccineVerifiedID = param.get("VaccineVerifiedID");
        String vaccineCheck = param.get("vaccineCheck");

        step("User open app");
        homePage.gotoHome();
        loginPage.loginAsGuest();

        step("login using b2b account");
        corporateLoginPage.corporateLogin(username,password);

        step("change currency to default IDR");
        homePage.changeCurrency("IDR",username);

        step("Open ferry vertical");
        homePage.openVerticalPageDeeplink("Ferry","","");

        step("Skip coachmark");
        ferrySearchPage.skipCoachMark();

        step("input origin port");
        ferrySearchPage.skipCoachMark();
        ferrySearchPage.inputOrigin(originPort);

        step("input destination port");
        ferrySearchPage.inputDestination(destinationPort);

        step("User input pax qty");
        ferrySearchPage.openTicketQty();
        ferrySearchPage.saveTicketQty();

        step("Input ticket type");
        ferrySearchPage.selectTicketType(ticketType);

        step("Departure date");
        ferrySearchPage.selectDate(daysToDepart);

        step("submit search");
        ferrySearchPage.submitSearch();

        step("Skip information coachmark in srp");
        ferrySearchResultPage.skipCoachMark();

        step("Check booking form price detail");
        ferrySearchResultPage.chooseFirstFleet();

        step("Input passenger detail");
        ferryBookingPage.fillGuestsAndVaccineDetails(vaccineCheck, VaccineVerifiedID, adult, 0);

        step("Check term and condition");
        ferryBookingPage.clickTncCheckBox();

        step("continue to payment page");
        ferryBookingPage.submitBooking();
        ferryBookingPage.setLocationService();

        step("Payment page should be displayed");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("Order id should be displayed", !orderID.isEmpty(), is(true));

        step("Open payment method");
        paymentPage.openPaymentMethod();

        step("Submit payment using Sakuku");
        paymentPage.checkSeeAllPaymentMethod();
        paymentPage.selectPaymentMethod("Sakuku");

        step("verify successful payment popup from corporate");
        Assertion.assertThat("get successfully placed order popup",paymentPage.isCorporatePaymentSuccessPopupDisplayed(),is(true));

        step("Open the order list and go to order detail");
        paymentPage.SeeOrderListAfterPaymentSuccess();

        step("Verify in Booking List that order should be present");
        Assertion.assertThat("Order is reflected on B2B corporate dashboard under booking List " , corporateApi.checkBookingOrder(orderID, username, password,"Ferry",CoreFunctions.getCurrentDate("d MMMM YYYY") , daysToDepart), is(true));

    }



    @Author(names = {"Aditi"})
    @Module(name = "Booking")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void testHotelBookingInstantPayment() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testHotelBookingInstantPayment");
        BaseTest.paramsThreadLocal.set(param);

        String hotelName = param.get("hotelName");
        String travelerUsername = param.get("travelerUsername");
        String travelerPassword = param.get("travelerPassword");
        String daysToDepart =  CoreFunctions.getEndOfCurrentMonth("d MMMM yyyy");

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        CorporateLoginPage<WebDriver> corporateLoginPage = CorporateLoginPage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        TravelPlanWeb<?> travelPlanWeb = TravelPlanWeb.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("User login");
        corporateLoginPage.corporateLogin(travelerUsername, travelerPassword);

        step("Change currency");
        homePage.changeCurrency("IDR","");
        homePage.openAccount();

        step("Open hotel landing page");
        homePage.openVerticalPageDeeplink("Hotel","", "IDR");

        step("Search stay location");
        landingPage.searchStayLocation(hotelName);

        step("Submit search");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        step("Go to the room list");
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();

        step("Select room from the room list");
        roomListPage.getTheFirstHotelRoomPrice();
        roomListPage.clickBookRoom();

        step("Submit the booking form");
        bookingFormPage.continueToPaymentPage();
        webViewPage.switchToWebViewUrl("corporate/interim");
        travelPlanWeb.skipTravelPlan();
        webViewPage.switchToNative();
        bookingFormPage.PriceChangesPopUp();

        step("Payment page should be displayed");
        String orderID = paymentPage.checkOrderID();
        orderID = String.valueOf(getOrderId(orderID));

        step("Open payment method");
        paymentPage.openPaymentMethod();

        step("Submit payment using Sakuku");
        paymentPage.checkSeeAllPaymentMethod();
        paymentPage.selectPaymentMethod("Sakuku");

        step("verify successful payment popup from corporate");
        Assertion.assertThat("get successfully placed order popup",paymentPage.isCorporatePaymentSuccessPopupDisplayed(),is(true));

        step("Open the order list and go to order detail");
        paymentPage.SeeOrderListAfterPaymentSuccess();

        step("Verify that order is reflected in the booking list");
        Assertion.assertThat("Order is reflected on B2B corporate dashboard under booking List " , corporateApi.checkBookingOrder(orderID, travelerUsername, travelerPassword,"tixhotel", CoreFunctions.getCurrentDate("d MMMM YYYY") , daysToDepart), is(true));
    }
    @Author(names = {"Mridul"})
    @Module(name = "Booking")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void testLoginBookingFerryViaB2BCardPayment() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testLoginBookingFerryViaB2BCardPayment");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        CorporateLoginPage<WebDriver> corporateLoginPage = CorporateLoginPage.getInstance(session);
        FerrySearchPage<WebDriver> ferrySearchPage = FerrySearchPage.getInstance(session);
        FerrySearchResultPage<WebDriver> ferrySearchResultPage = FerrySearchResultPage.getInstance(session);
        FerryBookingPage<WebDriver> ferryBookingPage = FerryBookingPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);

        String username = param.get("username");
        String password = param.get("password");

        String originPort = param.get("originPort");
        String destinationPort = param.get("destinationPort");
        String ticketType = param.get("ticketType");
        int adult = Integer.parseInt(param.get("adult"));
        int child = Integer.parseInt(param.get("child"));
        String daysToDepart =  CoreFunctions.getEndOfCurrentMonth("d MMMM yyyy");
        String VaccineVerifiedID = param.get("VaccineVerifiedID");
        String vaccineCheck = param.get("vaccineCheck");
        String cardNumber = param.get("cardNumber");
        String expiry = param.get("expiry");
        String cvv = param.get("cvv");
        String cardName = param.get("cardName");

        step("User open app");
        homePage.gotoHome();
        loginPage.loginAsGuest();

        step("login using b2b account");
        corporateLoginPage.corporateLogin(username,password);

        step("change currency to default IDR");
        homePage.changeCurrency("IDR",username);

        step("Open ferry vertical");
        homePage.openVerticalPageDeeplink("Ferry","","");

        step("Skip coachmark");
        ferrySearchPage.skipCoachMark();

        step("input origin port");
        ferrySearchPage.skipCoachMark();
        ferrySearchPage.inputOrigin(originPort);

        step("input destination port");
        ferrySearchPage.inputDestination(destinationPort);

        step("User input pax qty");
        ferrySearchPage.openTicketQty();
        ferrySearchPage.saveTicketQty();

        step("Input ticket type");
        ferrySearchPage.selectTicketType(ticketType);

        step("Departure date");
        ferrySearchPage.selectDate(daysToDepart);

        step("submit search");
        ferrySearchPage.submitSearch();

        step("Skip information coachmark in srp");
        ferrySearchResultPage.skipCoachMark();

        step("Check booking form price detail");
        ferrySearchResultPage.chooseFirstFleet();

        step("Input passenger detail");
        ferryBookingPage.fillGuestsAndVaccineDetails(vaccineCheck, VaccineVerifiedID, adult, 0);

        step("Check term and condition");
        ferryBookingPage.clickTncCheckBox();

        step("continue to payment page");
        ferryBookingPage.submitBooking();
        ferryBookingPage.setLocationService();

        step("Payment page should be displayed");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("Order id should be displayed", !orderID.isEmpty(), is(true));

        step("Open payment method");
        paymentPage.openPaymentMethod();

        step("Submit payment using Credit Card payment method");
        paymentPage.checkSeeAllPaymentMethod();
        paymentPage.selectPaymentMethod("Credit Card");

        step("user fill cc detail");
        paymentPage.fillCCDetailRealIssued(cardNumber, expiry, cvv, cardName);
        paymentPage.clickPayButtonCC();

        step("verify successful payment popup from corporate");
        Assertion.assertThat("get successfully placed order popup",paymentPage.isCorporatePaymentSuccessPopupDisplayed(),is(true));

        step("Open the order list and go to order detail");
        paymentPage.SeeOrderListAfterPaymentSuccess();

        step("Verify in Booking List that order should be present");
        Assertion.assertThat("Order is reflected on B2B corporate dashboard under booking List " , corporateApi.checkBookingOrder(orderID, username, password,"Ferry",CoreFunctions.getCurrentDate("d MMMM YYYY") , daysToDepart), is(true));

    }

    @Author(names = {"Mridul"})
    @Module(name = "Booking")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void testLoginBookingHotelViaB2BCardPayment(){
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testLoginBookingHotelViaB2BCardPayment");
        BaseTest.paramsThreadLocal.set(param);

        String hotelName = param.get("hotelName");
        String username = param.get("username");
        String password = param.get("password");
        String cardNumber = param.get("cardNumber");
        String expiry = param.get("expiry");
        String cvv = param.get("cvv");
        String cardName = param.get("cardName");
        String dateRange = param.get("date");

        int totalRoom = 1;
        int totalGuest = 1;
        int totalNight = 1;
        String accommodationType = "hotel";

        String[] dates = generateCheckInOutDate(dateRange, totalNight);
        String checkInDate = dates[0];
        String checkOutDate = dates[1];

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);
        TravelPlanWeb<?> travelPlanWeb = TravelPlanWeb.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        HotelPage<WebDriver> hotelPage = HotelPage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);

        step("User open app");
        homePage.gotoHome();

        step("Login as B2B account " + username + " via deeplink and change currency to IDR");
        homePage.loginWithSpecificAccountViaDeeplink(username, password, "IDR");

        step("user open PDP via deeplink");
        HotelAutoCompleteResponse hotelAutoCompleteResponse = hotelApi.getAutocompleteData(username, password, hotelName, accommodationType.toUpperCase());
        int selectedIndex = hotelApi.findHotelIndex(hotelAutoCompleteResponse, hotelName);
        String hotelId = hotelAutoCompleteResponse.data.contents.get(selectedIndex).publicId;
        String country = hotelAutoCompleteResponse.data.contents.get(selectedIndex).country.name;
        hotelPage.openPDPDeeplink(accommodationType.toLowerCase(), country.toLowerCase(), hotelId, totalRoom, totalGuest, checkInDate, checkOutDate);
        landingPage.fillNightCheckInForm();

        step("Go to the room list");
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();

        step("Select room from the room list");
        roomListPage.getTheFirstHotelRoomPrice();
        roomListPage.clickBookRoom();

        step("Submit the booking form");
        bookingFormPage.continueToPaymentPage();
        webViewPage.switchToWebViewUrl("corporate/interim");
        travelPlanWeb.skipTravelPlan();
        webViewPage.switchToNative();
        bookingFormPage.PriceChangesPopUp();

        step("Payment page should be displayed");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("Order id should be displayed", !orderID.isEmpty(), is(true));

        step("Open payment method");
        paymentPage.openPaymentMethod();

        step("Submit payment using Credit Card payment method");
        paymentPage.checkSeeAllPaymentMethod();
        paymentPage.selectPaymentMethod("Credit Card");

        step("user fill cc detail");
        paymentPage.fillCCDetailRealIssued(cardNumber, expiry, cvv, cardName);
        paymentPage.clickPayButtonCC();

        step("Open the order list and go to order detail");
        paymentPage.SeeOrderListAfterPaymentSuccess();

        step("Verify in Booking List that order should be present");
        Assertion.assertThat("Order is reflected on B2B corporate dashboard under booking List " , corporateApi.checkBookingOrder(orderID, username, password,"tixhotel",CoreFunctions.getCurrentDate("d MMMM YYYY") , CoreFunctions.getCurrentDate("d MMMM YYYY")), is(true));
    }

    @Author(names = {"Mridul"})
    @Module(name = "Booking")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void testLoginBookingBusViaB2BCardPayment(){
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testLoginBookingBusViaB2BCardPayment");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        CorporateLoginPage<WebDriver> corporateLoginPage = CorporateLoginPage.getInstance(session);
        BusSearchPage<WebDriver> busSearchPage = BusSearchPage.getInstance(session);
        BusSearchResultPage<WebDriver> busSearchResultPage = BusSearchResultPage.getInstance(session);
        BuspdpPage<WebDriver> buspdpPage = BuspdpPage.getInstance(session);
        BusBookingFormPage<WebDriver> busBookingFormPage = BusBookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        TravelPlanWeb<?> travelPlanWeb = TravelPlanWeb.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);

        String username = param.get("username");
        String password = param.get("password");
        String tripType = param.get("tripType");
        String originLocation = param.get("originLocation");
        String destinationLocation = param.get("destinationLocation");
        int daysToDeparture = Integer.parseInt(param.get("daysToDeparture"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String dateDeparture = CoreFunctions.addToLongDate(currentDate,daysToDeparture);
        int passenger = Integer.parseInt(param.get("passenger"));
        String cardNumber = param.get("cardNumber");
        String expiry = param.get("expiry");
        String cvv = param.get("cvv");
        String cardName = param.get("cardName");

        step("User open app");
        homePage.gotoHome();
        loginPage.loginAsGuest();

        step("B2B login");
        corporateLoginPage.corporateLogin(username,password);

        step("Change currency");
        homePage.changeCurrency("IDR",username);

        step("Open Bus vertical");
        homePage.openVerticalPageDeeplink("Bus","","");

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

        step("Input contact detail");
        busBookingFormPage.togglePassengerSameAsContact();
        busBookingFormPage.fillPassengerData(passenger);

        step("Continue to payment page");
        busBookingFormPage.continueToPaymentPage();
        webViewPage.switchToWebViewUrl("corporate/interim");
        travelPlanWeb.skipTravelPlan();
        webViewPage.switchToNative();

        step("Payment page should be displayed");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("Order id should be displayed", !orderID.isEmpty(), is(true));

        step("Open payment method");
        paymentPage.openPaymentMethod();

        step("Submit payment using Credit Card payment method");
        paymentPage.checkSeeAllPaymentMethod();
        paymentPage.selectPaymentMethod("Credit Card");

        step("user fill cc detail");
        paymentPage.fillCCDetailRealIssued(cardNumber, expiry, cvv, cardName);
        paymentPage.clickPayButtonCC();

        step("Open the order list and go to order detail");
        paymentPage.SeeOrderListAfterPaymentSuccess();

        step("Verify in Booking List that order should be present");
        Assertion.assertThat("Order is reflected on B2B corporate dashboard under booking List " , corporateApi.checkBookingOrder(orderID, username, password,"Bus",CoreFunctions.getCurrentDate("d MMMM YYYY") , CoreFunctions.getCurrentDate("d MMMM YYYY")), is(true));
    }

    @Author(names = {"Ashish"})
    @Module(name = "Booking")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void testBookingTTDInstantPaymentBCA() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("testBookingTTDInstantPaymentBCA");
        BaseTest.paramsThreadLocal.set(map);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        CorporateLoginPage<WebDriver> corporateLoginPage = CorporateLoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);
        TtdSelectedPackageDetailPageWeb<?> ttdSelectedPackageDetailPageWeb = TtdSelectedPackageDetailPageWeb.getInstance(session);
        TtdBookingFormPageWeb<?> ttdBookingFormPageWeb = TtdBookingFormPageWeb.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        PaymentApi<?> paymentHitterApiPage = new PaymentApi<>(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);

        String product = map.get("product");
        int ticketQty = Integer.parseInt(map.get("ticket_qty"));
        String channelType = map.get("channelType");

        String username = map.get("username");
        String password = map.get("password");

        step("Go to home page");
        homePage.gotoHome();

        step("Make user as guest");
        loginPage.loginAsGuest();

        step("B2B login");
        corporateLoginPage.corporateLogin(username,password);

        step("Change currency to 'IDR'");
        homePage.changeCurrency("IDR",username);

        step("Open TTD landing page");
        homePage.openVerticalPageDeeplink("To Do","","IDR");

        step("Search the product");
        ttdLandingPage.clickSearchBar();
        ttdSearchPage.inputSearchKeyword(product);

        step("select the activity");
        ttdSearchPage.selectRecommendedActivity(product);
        webViewPage.switchToWebViewUrl("to-do");
        ttdProductDetailPageWeb.getProductName();

        step("Select ticket");
        ttdProductDetailPageWeb.goToPackagesList();
        ttdProductDetailPageWeb.chooseTicket();

        step("Set total ticket");
        ttdSelectedPackageDetailPageWeb.fillAvailableSection();
        ttdSelectedPackageDetailPageWeb.setTicketQty(ticketQty, 0);

        step("Submit package detail");
        ttdSelectedPackageDetailPageWeb.clickBookButton();
        webViewPage.switchToNative();

        step("Completing booking form data");
        webViewPage.switchToWebViewUrl("to-do");
        ttdBookingFormPageWeb.fillContactOrderDetail(true);
        ttdBookingFormPageWeb.fillGuestDetail();

        step("Submit booking");
        ttdBookingFormPageWeb.continueToPaymentPage();
        ttdBookingFormPageWeb.checkPriceNotifChange();
        webViewPage.switchToNative();

        step("Payment page should be displayed");
        String orderID = paymentPage.checkOrderID();
        orderID = String.valueOf(getOrderId(orderID));

        step("Open payment method & select BCA VA");
        paymentPage.openPaymentMethod();
        paymentPage.selectPaymentMethod("BCA Virtual Account");

        step("Submit payment using Corporate Billing");
        paymentPage.clickPayButton();

        step("get account info number");
        String requestId = paymentPage.getAccountInfoNumber();

        step("Hit the payment api to complete the payment");
        paymentHitterApiPage.hitBCAVAApi(requestId,orderID,channelType);
        orderListPage.waitForPaymentSuccessful(orderID,10);

        step("Verify in Booking List that order should be present");
        Assertion.assertThat("Order is reflected on B2B corporate dashboard under booking List " , corporateApi.checkBookingOrder(orderID, username, password,"event",CoreFunctions.getCurrentDate("d MMMM YYYY") , CoreFunctions.getCurrentDate("d MMMM YYYY"),"instant_payment"), is(true));

        step("Open the order list and go to order detail");
        paymentPage.SeeOrderListAfterPaymentSuccess();

        step("check order status");
        orderListPage.openIssuedTicketViaDeeplink(orderID);
        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.getETicketLabel(), either(is("Your e-ticket is here")).or(is("E-tiketmu di sini")));
    }

    @Author(names = {"Ashish"})
    @Module(name = "Booking")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void testLoginBookingTrainViaAdminDeposit(){
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testLoginBookingTrainViaAdminDeposit");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TrainSearchPage<WebDriver> trainSearchPage = TrainSearchPage.getInstance(session);
        TrainBookingPage<WebDriver> trainBookingPage = TrainBookingPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        CorporateLoginPage<WebDriver> corporateLoginPage = CorporateLoginPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        TravelPlanWeb<?> travelPlanWeb = TravelPlanWeb.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);

        String email = param.get("username");
        String password = param.get("password");

        String tripType = param.get("tripType");
        String currency = param.get("currency");
        String originStation = param.get("originStation");
        String destinationStation = param.get("destinationStation");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int infantPassengers = Integer.parseInt(param.get("infant"));

        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate,daysToDepartureDate);

        step("User open app");
        homePage.gotoHome();
        loginPage.loginAsGuest();

        step("B2B login");
        corporateLoginPage.corporateLogin(email,password);

        step("Verify Step");
        Assertion.assertThat("User is on login state", homePage.isUserLogin(), is(true));

        step("Change currency");
        homePage.changeCurrency(currency,email);

        step("Open train page");
        homePage.openVerticalPageDeeplink("Trains","",currency);

        step("Select origin station");
        trainSearchPage.skipPopup();
        trainSearchPage.selectOrigin(originStation);

        step("Select destination station");
        trainSearchPage.selectDestination(destinationStation);

        step("Toggle oneway");
        trainSearchPage.toggleTripType(tripType);

        step("Select date");
        trainSearchPage.selectDate(departureDate);

        step("Add passenger");
        trainSearchPage.addPassenger(adultPassengers,infantPassengers);

        step("Search train");
        trainSearchPage.searchTrain();

        step("Skip Popup");
        trainSearchPage.skipPopup();

        step("Select departure train");
        trainSearchPage.selectTrainViaIndex(2);

        step("Fill passenger detail");
        trainBookingPage.fillPassengerDetail(adultPassengers,infantPassengers);

        step("Continue to 2nd half");
        trainBookingPage.continueTo2ndHalf();

        step("Continue to payment");
        trainBookingPage.continueToPayment();
        webViewPage.switchToWebViewUrl("corporate/interim");
        travelPlanWeb.skipTravelPlan();
        webViewPage.switchToNative();

        step("Payment page should be displayed");
        String orderID = paymentPage.checkOrderID();
        orderID = String.valueOf(getOrderId(orderID));

        step("Submit payment using deposit");
        paymentPage.clickPayButtonWithoutWaitForPreviousBookingPopup();
        orderListPage.waitForPaymentSuccessful(orderID,10);

        step("Verify in Booking List that order should be present");
        Assertion.assertThat("Order is reflected on B2B corporate dashboard under booking List " , corporateApi.checkBookingOrder(orderID, email, password,"train",CoreFunctions.getCurrentDate("d MMMM YYYY") , CoreFunctions.getCurrentDate("d MMMM YYYY"),"deposit"), is(true));

        step("verify successful payment popup from corporate");
        Assertion.assertThat("get successfully placed order popup",paymentPage.isCorporatePaymentSuccessPopupDisplayed(),is(true));

        step("Open the order list and go to order detail");
        paymentPage.SeeOrderListAfterPaymentSuccess();
        orderListPage.openIssuedTicketViaDeeplink(orderID);
        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.getETicketLabel(), either(is("Your e-ticket is here")).or(is("E-tiketmu di sini")));
    }

    @Author(names = {"Mridul"})
    @Module(name = "Booking")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void testLoginBookingFlightViaB2BCardPayment(){
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testLoginBookingFlightViaB2BCardPayment");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        CorporateLoginPage<WebDriver> corporateLoginPage = CorporateLoginPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);
        TravelPlanWeb<?> travelPlanWeb = TravelPlanWeb.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightProductReviewPage<WebDriver> flightProductReviewPage = FlightProductReviewPage.getInstance(session);
        FlightBookingFormPage<WebDriver> flightBookingFormPage = FlightBookingFormPage.getInstance(session);
        FlightApi<?> flightApi = new FlightApi<>(session);

        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        String airline = param.get("airline");

        String username = param.get("username");
        String password = param.get("password");
        String cardNumber = param.get("cardNumber");
        String expiry = param.get("expiry");
        String cvv = param.get("cvv");
        String cardName = param.get("cardName");

        step("Find dynamic data");
        FlightDynamicData flightDynamicData = flightApi.findDate(
                username,password,
                originAirport,destinationAirport,daysToDepartureDate,
                adultPassengers,childPassengers,infantPassengers,
                tripType,airline,"","","","IDR"
        );
        String departureDate =CoreFunctions.convertToLongDate(flightDynamicData.departureDate);
        airline = flightDynamicData.airlineDepart;

        if (Configs.ENVIRONMENT == Environment.PREPROD){
            step("Validate all suppliers are mock-issued");
            flightApi.validateAllSuppliersAreMockIssuedByAirline(flightDynamicData.airlineCodeDepart, flightDynamicData.originAirport, flightDynamicData.destinationAirport);
        }

        step("User open app");
        homePage.gotoHome();
        loginPage.loginAsGuest();

        step("B2B login");
        corporateLoginPage.corporateLogin(username,password);

        step("Change currency");
        homePage.changeCurrency("IDR",username);

        step("Open flights page");
        homePage.openVerticalPageDeeplink("Flights","","");
        flightSearchPage.closePopup();

        step("change date using deeplink");
        flightSearchPage.changeDateViaDeeplink(flightDynamicData, tripType);

        step("Toggle trip type { " + tripType + " }");
        flightSearchPage.toggleTriptype(tripType);

        step("Select origin airport");
        flightSearchPage.selectOrigin(originAirport);
        flightSearchPage.closePopup();

        step("Select destination airport");
        flightSearchPage.selectDestination(destinationAirport);

        step("Select date");
        flightSearchPage.selectDate(departureDate);

        step("Add passenger");
        flightSearchPage.addPassenger(adultPassengers,childPassengers,infantPassengers);

        step("Search flight");
        flightSearchPage.searchFlight();

        step("Open filter");
        flightSearchResultPage.openFilter();

        step("Filter airline");
        flightSearchResultPage.filterAirline(airline);

        step("Save filter");
        flightSearchResultPage.saveFilter();

        step("Click flight detail chevron");
        flightSearchResultPage.selectFlight();

        step("Continue to booking form");
        flightProductReviewPage.continueToBookingForm(tripType);

        step("Fill passenger detail");
        flightBookingFormPage.fillPassengerDetail(adultPassengers,childPassengers,infantPassengers, "domestic",airline);

        step("Continue to second half booking form");
        flightBookingFormPage.continueToSecondHalf();

        step("Continue to payment");
        flightBookingFormPage.continueToPayment();
        webViewPage.switchToWebViewUrl("corporate/interim");
        travelPlanWeb.skipTravelPlan();
        webViewPage.switchToNative();

        step("Skip full protection");
        flightBookingFormPage.skipFullProtection();
        webViewPage.switchToWebViewUrl("corporate/interim");
        travelPlanWeb.skipTravelPlan();
        webViewPage.switchToNative();

        step("Payment page should be displayed");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("Order id should be displayed", !orderID.isEmpty(), is(true));

        step("Open payment method");
        paymentPage.openPaymentMethod();

        step("Submit payment using Credit Card payment method");
        paymentPage.checkSeeAllPaymentMethod();
        paymentPage.selectPaymentMethod("Credit Card");

        step("user fill cc detail");
        paymentPage.fillCCDetailRealIssued(cardNumber, expiry, cvv, cardName);
        paymentPage.clickPayButtonCC();

        step("Open the order list and go to order detail");
        paymentPage.SeeOrderListAfterPaymentSuccess();

        step("Verify in Booking List that order should be present");
        Assertion.assertThat("Order is reflected on B2B corporate dashboard under booking List " , corporateApi.checkBookingOrder(orderID, username, password,"flight",CoreFunctions.getCurrentDate("d MMMM YYYY") , CoreFunctions.getCurrentDate("d MMMM YYYY")), is(true));

    }

    @Author(names = {"Aditi"})
    @Module(name = "Booking")
    @Tribe(name = "Instant Payment")
    @Vertical(name = "B2B")
    @Test
    public void testLoginBookingTrainViaTravelerBookingByCC(){
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testLoginBookingTrainViaTravelerBookingByCC");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TrainSearchPage<WebDriver> trainSearchPage = TrainSearchPage.getInstance(session);
        TrainBookingPage<WebDriver> trainBookingPage = TrainBookingPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);
        CorporateLoginPage<WebDriver> corporateLoginPage = CorporateLoginPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        TravelPlanWeb<?> travelPlanWeb = TravelPlanWeb.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);

        String email = param.get("username");
        String password = param.get("password");
        String tripType = param.get("tripType");
        String currency = param.get("currency");
        String originStation = param.get("originStation");
        String destinationStation = param.get("destinationStation");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        String cardNumber = param.get("cardNumber");
        String expiry = param.get("expiry");
        String cvv = param.get("cvv");
        String cardName = param.get("cardName");

        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate,daysToDepartureDate);

        String[] departureDateArray = departureDate.split(" ");
        String date = departureDateArray[0];

        step("User open app");
        homePage.gotoHome();
        loginPage.loginAsGuest();

        step("B2B login");
        corporateLoginPage.corporateLogin(email,password);

        step("Verify Step");
        Assertion.assertThat("User is on login state", homePage.isUserLogin(), is(true));

        step("Change currency");
        homePage.changeCurrency(currency,email);

        step("Open train page");
        homePage.openVerticalPageDeeplink("Trains","",currency);

        step("Select origin station");
        trainSearchPage.skipPopup();
        trainSearchPage.selectOrigin(originStation);

        step("Select destination station");
        trainSearchPage.selectDestination(destinationStation);

        step("Toggle oneway");
        trainSearchPage.toggleTripType(tripType);

        step("Select date");
        trainSearchPage.selectDate(departureDate);

        step("Add passenger");
        trainSearchPage.addPassenger(adultPassengers,infantPassengers);

        step("Search train");
        trainSearchPage.searchTrain();

        step("Skip Popup");
        trainSearchPage.skipPopup();

        step("Select departure train");
        trainSearchPage.selectTrainViaIndex(2);

        step("Fill passenger detail");
        trainBookingPage.fillPassengerDetail(adultPassengers,infantPassengers);

        step("Continue to 2nd half");
        trainBookingPage.continueTo2ndHalf();

        step("Continue to payment");
        trainBookingPage.continueToPayment();
        webViewPage.switchToWebViewUrl("corporate/interim");
        travelPlanWeb.skipTravelPlan();
        webViewPage.switchToNative();

        step("Payment page should be displayed");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("Order id should be displayed", !orderID.isEmpty(), is(true));

        step("Open payment method");
        paymentPage.openPaymentMethod();

        step("Submit payment using Credit Card payment method");
        paymentPage.checkSeeAllPaymentMethod();
        paymentPage.selectPaymentMethod("Credit Card");

        step("user fill cc detail");
        paymentPage.fillCCDetailRealIssued(cardNumber, expiry, cvv, cardName);
        paymentPage.clickPayButtonCC();

        step("Open the order list and go to order detail");
        paymentPage.SeeOrderListAfterPaymentSuccess();

        step("Verify in Booking List that order should be present");
        Assertion.assertThat("Order is reflected on B2B corporate dashboard under booking List " , corporateApi.checkBookingOrder(orderID, email, password,"train",CoreFunctions.getCurrentDate("d MMMM YYYY") , CoreFunctions.getCurrentDate("d MMMM YYYY")), is(true));

    }

    @Author(names = {"Aditi"})
    @Module(name = "Booking")
    @Tribe(name = "Instant Payment")
    @Vertical(name = "B2B")
    @Test
    public void testLoginBookingTTDViaTravelerBookingByCC() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("testLoginBookingTTDViaTravelerBookingByCC");
        BaseTest.paramsThreadLocal.set(map);

        String product = map.get("product");
        int ticketQty = Integer.parseInt(map.get("ticket_qty"));
        String username = map.get("username");
        String password = map.get("password");
        String cardNumber = map.get("cardNumber");
        String expiry = map.get("expiry");
        String cvv = map.get("cvv");
        String cardName = map.get("cardName");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        CorporateLoginPage<WebDriver> corporateLoginPage = CorporateLoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);
        TtdSelectedPackageDetailPageWeb<?> ttdSelectedPackageDetailPageWeb = TtdSelectedPackageDetailPageWeb.getInstance(session);
        TtdBookingFormPageWeb<?> ttdBookingFormPageWeb = TtdBookingFormPageWeb.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);

        step("Go to home page");
        homePage.gotoHome();

        step("Make user as guest");
        loginPage.loginAsGuest();

        step("B2B login");
        corporateLoginPage.corporateLogin(username,password);

        step("Change currency to 'IDR'");
        homePage.changeCurrency("IDR",username);

        step("Open TTD landing page");
        homePage.openVerticalPageDeeplink("To Do","","IDR");

        step("Search the product");
        ttdLandingPage.clickSearchBar();
        ttdSearchPage.inputSearchKeyword(product);

        step("select the activity");
        ttdSearchPage.selectRecommendedActivity(product);
        webViewPage.switchToWebViewUrl("to-do");
        ttdProductDetailPageWeb.getProductName();

        step("Select ticket");
        ttdProductDetailPageWeb.goToPackagesList();
        ttdProductDetailPageWeb.chooseTicket();

        step("Set total ticket");
        ttdSelectedPackageDetailPageWeb.fillAvailableSection();
        ttdSelectedPackageDetailPageWeb.setTicketQty(ticketQty, 0);

        step("Submit package detail");
        ttdSelectedPackageDetailPageWeb.clickBookButton();
        webViewPage.switchToNative();

        step("Completing booking form data");
        webViewPage.switchToWebViewUrl("to-do");
        ttdBookingFormPageWeb.fillContactOrderDetail(true);
        ttdBookingFormPageWeb.fillGuestDetail();
        ttdBookingFormPageWeb.getPrice();

        step("Submit booking");
        ttdBookingFormPageWeb.continueToPaymentPage();
        ttdBookingFormPageWeb.checkPriceNotifChange();
        webViewPage.switchToNative();

        step("Payment page should be displayed");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("Order id should be displayed", !orderID.isEmpty(), is(true));

        step("Open payment method");
        paymentPage.openPaymentMethod();

        step("Submit payment using Credit Card payment method");
        paymentPage.checkSeeAllPaymentMethod();
        paymentPage.selectPaymentMethod("Credit Card");
        paymentPage.fillCCDetailRealIssued(cardNumber, expiry, cvv, cardName);
        paymentPage.clickPayButtonCC();

        step("Open the order list and go to order detail");
        paymentPage.SeeOrderListAfterPaymentSuccess();

        step("Verify in Booking List that order should be present");
        Assertion.assertThat("Order is reflected on B2B corporate dashboard under booking List " , corporateApi.checkBookingOrder(orderID, username, password,"event",CoreFunctions.getCurrentDate("d MMMM YYYY") , CoreFunctions.getCurrentDate("d MMMM YYYY")), is(true));
    }

    @Author(names = {"Mridul"})
    @Module(name = "Booking")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void testLoginBookingCarRentalViaB2BCardPayment(){
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testLoginBookingCarRentalViaB2BCardPayment");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        CorporateLoginPage<WebDriver> corporateLoginPage = CorporateLoginPage.getInstance(session);
        SearchPageCarRental<WebDriver> carRentalSearchPage = SearchPageCarRental.getInstance(session);
        SearchResultPageCarRental<WebDriver> carRentalSearchResultPage = SearchResultPageCarRental.getInstance(session);
        CarRentalReviewBookingPage<WebDriver> carRentalReviewBookingPage = CarRentalReviewBookingPage.getInstance(session);
        CarRentalBookingPage<WebDriver> carRentalBookingPage = CarRentalBookingPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);
        TravelPlanWeb<?> travelPlanWeb = TravelPlanWeb.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);

        String username = param.get("username");
        String password = param.get("password");
        String Location = param.get("Location");
        int daysToDeparture = Integer.parseInt(param.get("daysToDeparture"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String dateDeparture = CoreFunctions.addToLongDate(currentDate,daysToDeparture);
        String cardNumber = param.get("cardNumber");
        String expiry = param.get("expiry");
        String cvv = param.get("cvv");
        String cardName = param.get("cardName");

        step("User open app");
        homePage.gotoHome();
        loginPage.loginAsGuest();

        step("B2B login");
        corporateLoginPage.corporateLogin(username,password);

        step("Open Car Rental vertical");
        homePage.openVerticalPageDeeplink("Car Rentals","","IDR");

        step("Skip coachmark");
        carRentalSearchPage.closeInformationPopUp();

        step("Select car rental type");
        carRentalSearchPage.chooseDriver("with_driver");

        step("Select car rental date");
        carRentalSearchPage.selectDate(CoreFunctions.addToLongDate(dateDeparture,7));

        step("Input car rental location");
        carRentalSearchPage.chooseLocation(Location);

        step("Search car rental");
        carRentalSearchPage.searchCar("1");

        step("Verify price car type");
        Assertion.assertThat("is car type price in IDR", carRentalSearchResultPage.getPriceCarType().contains("IDR"), is(true));

        step("choose car");
        carRentalSearchResultPage.chooseCarType();

        step("Verify price car vendor");
        Assertion.assertThat("is car vendor price in IDR", carRentalSearchResultPage.getPriceCarVendor().contains("IDR"), is(true));

        step("choose vendor");
        carRentalSearchResultPage.chooseCarVendor();

        step("Verify price in PDP page");
        Assertion.assertThat("is car PDP price in IDR", carRentalSearchResultPage.getPricePDP().contains("IDR"), is(true));

        step("continue to booking form from review page");
        carRentalReviewBookingPage.continueFromReviewDetailCar();

        step("continue to complete booking");
        carRentalBookingPage.choosePickUpLocation(Location);
        carRentalBookingPage.continueToCompleteBookingPage();

        step("continue to Payment Page");
        carRentalBookingPage.setContinueToPayment();

        step("Skip travel plan page");
        webViewPage.switchToWebViewUrl("corporate/interim");
        travelPlanWeb.skipTravelPlan();
        webViewPage.switchToNative();

        step("get order id");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",!orderID.isEmpty(),is(true));

        step("Open payment method");
        paymentPage.openPaymentMethod();

        step("Submit payment using Credit Card payment method");
        paymentPage.checkSeeAllPaymentMethod();
        paymentPage.selectPaymentMethod("Credit Card");

        step("user fill cc detail");
        paymentPage.fillCCDetailRealIssued(cardNumber, expiry, cvv, cardName);
        paymentPage.clickPayButtonCC();

        step("Open the order list and go to order detail");
        paymentPage.SeeOrderListAfterPaymentSuccess();

        step("Verify in Booking List that order should be present");
        Assertion.assertThat("Order is reflected on B2B corporate dashboard under booking List " , corporateApi.checkBookingOrder(orderID, username, password,"car",CoreFunctions.getCurrentDate("d MMMM YYYY") , CoreFunctions.getCurrentDate("d MMMM YYYY")), is(true));
    }

    @Author(names = {"Mridul","Harshit Ranjan"})
    @Module(name = "TravelPlan")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void verifyTravelPlanInterimPageAirportTransferViaAPI() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyTravelPlanInterimPageAirportTransferViaAPI");
        BaseTest.paramsThreadLocal.set(param);

        String email = param.get("username");
        String password = param.get("password");
        String adminUsername = param.get("adminUsername");
        String adminPassword = param.get("adminPassword");
        String currency = param.get("currency");
        String airport = param.get("airport");
        String destination = param.get("destination");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        CarTransferSearchPage<WebDriver> carTransferSearchPage = CarTransferSearchPage.getInstance(session);
        CarTransferSearchResultPage<WebDriver> carTransferSearchResultPage = CarTransferSearchResultPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        CarTransferBookingPage<WebDriver> carTransferBookingPage = CarTransferBookingPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);

        TravelPlanWeb<?> travelPlanWeb = TravelPlanWeb.getInstance(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);

        step("Change Config to true for Travel Plan Page on account "+adminUsername);
        boolean configUpdated = corporateApi.updateTravelPlanSetting(adminUsername, adminPassword,true);
        Assertion.assertThat("Travel Plan setting should be updated to True", configUpdated, is(true));

        step("User open app");
        homePage.gotoHome();

        step("Login as B2B account " + email + " via deeplink");
        homePage.loginViaDeeplink(email, password);


        step("Open Car Landing Page");
        homePage.openVerticalPageDeeplink("Car Transfers", "", currency);
        carTransferSearchPage.allowLocationAccess();
        webViewPage.switchToWebViewUrl("airport-transfer");
        carTransferSearchPage.closePopups();

        step("User choose car transfer in");
        carTransferSearchPage.chooseTransferType("transfer_in");

        step("select date 1-3 (Random)");
        carTransferSearchPage.toggleForLaterOrder();

        step("user input origin and destination");
        carTransferSearchPage.choosePickups("transfer_in", airport, destination);

        step("user search for car");
        carTransferSearchPage.searchCar();

        if(Configs.PLATFORM == Platform.IOS){
            webViewPage.switchToNative();
            carTransferSearchPage.allowLocationAccess();
            webViewPage.switchToWebViewUrl("airport-transfer");
        }

        step("user select car");
        carTransferSearchResultPage.selectCar();

        step("user select vendor");
        carTransferSearchResultPage.selectVendor();


        step("user continue to booking page");
        carTransferSearchResultPage.continueToBookingPage();

        step("user continue to payment list page");
        carTransferBookingPage.continueToPayment();
        webViewPage.switchToNative();

        step("will get Redirected to Interim Page");
        webViewPage.switchToWebViewUrl("airport-transfer/booking");
        Assertion.assertThat("Interim Page should be visible", travelPlanWeb.isTravelPlanInterimPageShown(), is(true));

        step("Skip travel plan page");
        travelPlanWeb.skipTravelPlan();
        webViewPage.switchToNative();

        step("Payment page should be displayed");
        String orderID = paymentPage.checkOrderID();
        orderID = String.valueOf(getOrderId(orderID));
        Assertion.assertThat("Corporate billing option is selected for order id "+orderID, paymentPage.getSelectedPayment().toLowerCase(), is("corporate billing"));

        step("Change Config to false for Travel Plan Page on account "+adminUsername);
        configUpdated = corporateApi.updateTravelPlanSetting(adminUsername, adminPassword,false);
        Assertion.assertThat("Travel Plan setting should be updated to False", configUpdated, is(true));
    }

    @Author(names = {"Ashish"})
    @Module(name = "Booking")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void testLoginBookingHotelViaAdminDeposit(){
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testLoginBookingHotelViaAdminDeposit");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        CorporateLoginPage<WebDriver> corporateLoginPage = CorporateLoginPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        TravelPlanWeb<?> travelPlanWeb = TravelPlanWeb.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);

        String email = param.get("username");
        String password = param.get("password");

        String hotelName = param.get("hotelName");
        String currency = param.get("currency");

        step("User open app");
        homePage.gotoHome();
        loginPage.loginAsGuest();

        step("B2B login");
        corporateLoginPage.corporateLogin(email,password);

        step("Verify Step");
        Assertion.assertThat("User is on login state", homePage.isUserLogin(), is(true));

        step("Change currency");
        homePage.changeCurrency(currency,email);

        step("Open hotel landing page");
        homePage.openVerticalPageDeeplink("Hotel","","IDR");

        step("Search stay location");
        landingPage.searchStayLocation(hotelName);

        step("Submit search");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        step("Go to the room list");
        detailPage.RemovePopUp();
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();

        step("Select room from the room list");
        roomListPage.getTheFirstHotelRoomPrice();
        roomListPage.clickBookRoom();

        step("Submit the booking form");
        bookingFormPage.continueToPaymentPage();
        webViewPage.switchToWebViewUrl("corporate/interim");
        travelPlanWeb.skipTravelPlan();
        webViewPage.switchToNative();
        bookingFormPage.PriceChangesPopUp();

        step("Payment page should be displayed");
        String orderID = paymentPage.checkOrderID();
        orderID = String.valueOf(getOrderId(orderID));

        step("Submit payment using deposit");
        paymentPage.clickPayButtonWithoutWaitForPreviousBookingPopup();
        orderListPage.waitForPaymentSuccessful(orderID,10);

        step("Verify in Booking List that order should be present for orderID: {" + orderID + "}");
        Assertion.assertThat("Order is reflected on B2B corporate dashboard under booking List " , corporateApi.checkBookingOrder(orderID, email, password,"tixhotel",CoreFunctions.getCurrentDate("d MMMM YYYY") , CoreFunctions.getCurrentDate("d MMMM YYYY"),"deposit"), is(true));

        step("verify successful payment popup from corporate");
        Assertion.assertThat("get successfully placed order popup",paymentPage.isCorporatePaymentSuccessPopupDisplayed(),is(true));

        step("Open the order list and go to order detail");
        paymentPage.SeeOrderListAfterPaymentSuccess();
        orderListPage.openIssuedTicketViaDeeplink(orderID);
        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.getETicketLabel(), either(is("Your e-ticket is here")).or(is("E-tiketmu di sini")));
    }

    @Author(names = {"Mridul"})
    @Module(name = "TravelPlan")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void createWooshBookingUsingTravelPlan() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("createWooshBookingUsingTravelPlan");
        BaseTest.paramsThreadLocal.set(param);

        String username = param.get("username");
        String password = param.get("password");
        String adminUsername = param.get("adminUsername");
        String adminPassword = param.get("adminPassword");
        String currency = param.get("currency");
        String originStation = param.get("originStation");
        String destinationStation = param.get("destinationStation");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String originStationCode = param.get("originStationCode");
        String destinationStationCode = param.get("destinationStationCode");
        String planName = param.get("planName");

        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate,daysToDepartureDate);

        CorporateApi<?> corporateApi = new CorporateApi<>(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        CorporateLoginPage<WebDriver> corporateLoginPage = CorporateLoginPage.getInstance(session);
        WhooshSearchPage<WebDriver> whooshSearchPage = WhooshSearchPage.getInstance(session);
        WhooshBookingPage<WebDriver> whooshBookingPage = WhooshBookingPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        Unm<?> unm = new Unm<>(session);

        step("get login access token for user " + username + "employee data");
        LoginDataUnmResponse.Data loginDataUnmResponse = unm.unmLoginApi(username, password);
        EmployeeData.Data employeeData = corporateApi.getEmployeeData(username).data;

        step("Create a whoosh plan");
        PostCreateTravelPlanBody.Configs travelPlanConfigs = corporateApi.setTravelPlanWhooshData(originStationCode, originStation, destinationStationCode, destinationStation, false,departureDate, "");
        TravelPlanPojo travelPlanPojo = corporateApi.checkAndCreateTravelPlan(loginDataUnmResponse,username,adminUsername,planName,daysToDepartureDate,1,travelPlanConfigs,"data","whoosh","approved",employeeData, new ArrayList<>());
        Assertion.assertThat("Whoosh Travel Plan Created planId: " + travelPlanPojo.travelPlanID, travelPlanPojo.travelPlanID,not(-1));

        step("Approve the plan "+travelPlanPojo.travelPlanID);
        Assertion.assertThat("Plan got approved "+travelPlanPojo.travelPlanID,corporateApi.updateTravelPlanApproval(adminUsername,adminPassword,travelPlanPojo),is(true));

        step("login with b2b traveler account");
        homePage.gotoHome();
        loginPage.loginAsGuest();
        corporateLoginPage.corporateLogin(username, password);
        homePage.changeCurrency(currency, username);

        step("Select Whoosh ");
        homePage.openVerticalPageDeeplink("Whoosh","","");

        step("Select origin station");
        whooshSearchPage.skipPopup();
        whooshSearchPage.selectOrigin(originStation);

        step("Select destination station");
        whooshSearchPage.selectDestination(destinationStation);

        step("Select date");
        whooshSearchPage.selectDate(departureDate);

        step("Search train");
        whooshSearchPage.searchTrain();

        step("Skip Popup");
        whooshSearchPage.skipPopup();

        step("Select departure train");
        whooshSearchPage.selectTrainViaIndex(1);

        step("Fill passenger detail");
        whooshBookingPage.fillPassengerDetail(1,0);

        step("Continue to 2nd half");
        whooshBookingPage.continueTo2ndHalf();

        step("Continue to payment");
        whooshBookingPage.continueToPayment();

        step("Select Travel plan");
        whooshBookingPage.selectTravelPlan(planName);

        step("Payment page should be displayed");
        String orderID = paymentPage.checkOrderID();
        orderID = String.valueOf(getOrderId(orderID));

        step("Submit payment using Corporate Billing");
        paymentPage.clickPayButtonWithoutWaitForPreviousBookingPopup();

        step("verify successfully placed order popup from corporate");
        Assertion.assertThat("get successfully placed order popup",paymentPage.isCorporateSuccessfullyPlacedOrderPopup(),is(true));

        orderListPage.waitForPaymentSuccessful(orderID,10);

        step("Verify using API name and order id");
        Assertion.assertThat("Name and Order ID on purchase List" , corporateApi.checkTravelPlanStatus(adminUsername,adminPassword, 3,orderID,currentDate,currentDate,planName) ,is(true));
    }

    @Author(names = {"Yazid"})
    @Module(name = "MultiCity")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void flightMultiCityBookingWithTravelPlan() throws ExecutionException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("flightMultiCityBookingWithTravelPlan");
        BaseTest.paramsThreadLocal.set(param);

        CorporateApi<?> corporateApi = new CorporateApi<>(session);
        Unm<?> unm = new Unm<>(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightProductReviewPage<WebDriver> flightProductReviewPage = FlightProductReviewPage.getInstance(session);
        FlightBookingFormPage<WebDriver> flightBookingFormPage = FlightBookingFormPage.getInstance(session);
        TravelPlanWeb<?> travelPlanWeb = TravelPlanWeb.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);

        // use account that the booking is not auto-approved
        String username = param.get("username");
        String password = param.get("password");
        String adminUsername = param.get("adminUsername");
        String adminPassword = param.get("adminPassword");
        String planName = param.get("planName");
        String currency = param.get("currency");
        int adult = Integer.parseInt(param.get("adult"));
        int child = Integer.parseInt(param.get("child"));
        int infant = Integer.parseInt(param.get("infant"));
        String cabinClass = param.get("class");

        // e.g. routes = "CGK-SUB/SUB-DPS/DPS-CGK" and daysToDepartureDates = "3/6/9" (days from today, one per route)
        List<String> routes = Arrays.stream(param.get("routes").split("/")).map(String::trim).toList();
        List<Integer> daysToDepartureDates = Arrays.stream(param.get("daysToDepartureDates").split("/"))
                .map(String::trim).map(Integer::parseInt).toList();

        // The plan runs from the 1st segment departure until the last segment departure.
        int daysToDepartureDate = daysToDepartureDates.get(0);
        int planDuration = daysToDepartureDates.get(daysToDepartureDates.size() - 1) - daysToDepartureDate;

        step("get login access token for user " + username);
        LoginDataUnmResponse.Data loginDataUnmResponse = unm.unmLoginApi(username, password);
        EmployeeData.Data employeeData = corporateApi.getEmployeeData(username).data;

        step("Create a multi city flight plan " + routes);
        PostCreateTravelPlanMultiCityBody.Configs travelPlanConfigs = corporateApi.setTravelPlanFlightData(loginDataUnmResponse.accessToken, routes, daysToDepartureDates);
        TravelPlanPojo travelPlanPojo = corporateApi.checkAndCreateTravelPlan(loginDataUnmResponse, username, adminUsername, planName, daysToDepartureDate, planDuration, travelPlanConfigs, "flight", "requested", employeeData, new ArrayList<>());
        Assertion.assertThat("Multi City Flight Plan Created", travelPlanPojo, is(not(nullValue())));
        Assertion.assertThat("Multi City Flight Plan Created " + travelPlanPojo.travelPlanID, travelPlanPojo.travelPlanID, not(-1));

        step("Approve the multi city flight plan " + travelPlanPojo.travelPlanID);
        Assertion.assertThat("Multi City Flight Plan got approved " + travelPlanPojo.travelPlanID,
                corporateApi.updateTravelPlanApproval(adminUsername, adminPassword, travelPlanPojo), is(true));

        step("Login as B2B account " + username + " via deeplink and change currency to " + currency);
        homePage.gotoHome();
        if (Configs.PLATFORM == Platform.IOS) {
            CorporateLoginPage<WebDriver> corporateLoginPage = CorporateLoginPage.getInstance(session);
            corporateLoginPage.corporateLogin(username, password);
            homePage.changeCurrency(currency, username);
        } else {
            homePage.loginWithSpecificAccountViaDeeplink(username, password, currency);
        }

        // Same routes and dates as the travel plan: both use today + daysToDepartureDates.
        step("Open multi city flight SRP via deeplink " + routes);
        flightSearchPage.deeplinkMultiCitySrp(routes, daysToDepartureDates, adult, child, infant, cabinClass);
        flightSearchResultPage.waitForPageLoad();

        step("Select the flight for each route " + routes);
        flightSearchResultPage.selectFlightForEachRoute(routes);

        step("Continue to booking form");
        flightProductReviewPage.continueToBookingForm();

        step("Select any saved passenger");
        flightBookingFormPage.selectAnySavedPassenger();

        step("Continue to second half booking form");
        flightBookingFormPage.continueToSecondHalf();

        step("Continue to payment");
        flightBookingFormPage.continueToPayment();
        flightBookingFormPage.waitForLoadingDialogToDisappear();

        step("Select travel plan " + planName + " on interim page");
        webViewPage.switchToWebViewUrl("corporate/interim");
        travelPlanWeb.selectTravelPlan(planName);
        Assertion.assertThat("Travel Plan on Interim Page should be selected with given plan name " + planName, travelPlanWeb.isCorrectTravelPlanSelected(planName), is(true));
        travelPlanWeb.selectContinueToPay();
        webViewPage.switchToNative();
        flightBookingFormPage.waitForLoadingDialogToDisappear();
        flightBookingFormPage.baggageAllowancePopup();        

        step("Payment page should be displayed");
        String orderID = paymentPage.checkOrderID();
        orderID = String.valueOf(getOrderId(orderID));
        Assertion.assertThat("Corporate billing option is selected", paymentPage.getSelectedPayment().toLowerCase(), is("corporate billing"));

        step("Submit payment using Corporate Billing");
        paymentPage.clickPayButtonWithoutWaitForPreviousBookingPopup();
        paymentPage.continueIfBottomSheetPresent();
        Assertion.assertThat("get successfully placed order popup", paymentPage.isCorporateSuccessfullyPlacedOrderPopup(), is(true));

        // Only check the order id is present on the dashboard, the booking is intentionally not approved.
        step("Check order " + orderID + " is reflected on B2B corporate dashboard booking list");
        String currentDate = CoreFunctions.getCurrentDate("d MMMM yyyy");
        Assertion.assertThat("Order ID is reflected on B2B corporate dashboard under booking pending approvals",
                corporateApi.checkBookingOrderMultiCity(orderID, adminUsername, adminPassword, "flight", currentDate, currentDate, "pending", routes.size()), is(true));
    }

    @Author(names = {"Ashish"})
    @Module(name = "InterimPage")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void testLoginFlightTravelPlanNotApprovedCheckInterimPage() throws ExecutionException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testLoginFlightTravelPlanNotApprovedCheckInterimPage");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightProductReviewPage<WebDriver> flightProductReviewPage = FlightProductReviewPage.getInstance(session);
        FlightBookingFormPage<WebDriver> flightBookingFormPage = FlightBookingFormPage.getInstance(session);
        TravelPlanWeb<?> travelPlanWeb = TravelPlanWeb.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);
        Unm<?> unm = new Unm<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String adminUsername = param.get("adminUsername");

        String currency = param.get("currency");
        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");
        String planName = param.get("planName");

        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));

        step("get login access token for user " + username + "employee data");
        LoginDataUnmResponse.Data loginDataUnmResponse = unm.unmLoginApi(username, password);
        EmployeeData.Data employeeData = corporateApi.getEmployeeData(username).data;

        step("Create a flight plan");
        PostCreateTravelPlanBody.Configs travelPlanConfigs = corporateApi.setTravelPlanFlightData(loginDataUnmResponse.accessToken,originAirport,destinationAirport,false,daysToDepartureDate,0);
        TravelPlanPojo travelPlanPojo = corporateApi.checkAndCreateTravelPlan(loginDataUnmResponse,username,adminUsername,planName,daysToDepartureDate,1,travelPlanConfigs, "name","flight","requested",employeeData, new ArrayList<>());
        Assertion.assertThat("Train Plan Created " + travelPlanPojo.travelPlanID, travelPlanPojo.travelPlanID,not(-1));

        step("set data form plan");
        daysToDepartureDate = CoreFunctions.getDaysToDepartureDate(travelPlanPojo.startDate);
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate,daysToDepartureDate);
        originAirport = travelPlanPojo.flight.departureCode;
        destinationAirport = travelPlanPojo.flight.arrivalCode;

        step("User open app");
        homePage.gotoHome();
        loginPage.loginAsGuest();

        step("B2B login");
        loginPage.loginWith("email",username,password);

        step("Change currency");
        homePage.changeCurrency(currency,username);

        step("Open flights page");
        homePage.openVerticalPageDeeplink("Flights","","");
        flightSearchPage.closePopup();

        step("change date using deeplink");
        flightSearchPage.changeDateViaDeeplink(daysToDepartureDate,tripType);

        step("Toggle trip type { " + tripType + " }");
        flightSearchPage.toggleTriptype(tripType);

        step("Select origin airport");
        flightSearchPage.selectOrigin(originAirport);
        flightSearchPage.closePopup();

        step("Select destination airport");
        flightSearchPage.selectDestination(destinationAirport);

        step("Select date");
        flightSearchPage.selectDate(departureDate);

        step("Add passenger");
        flightSearchPage.addPassenger(adultPassengers,childPassengers,infantPassengers);

        step("Search flight");
        flightSearchPage.searchFlight();

        step("Click flight detail chevron");
        flightSearchResultPage.selectFlight();

        step("Continue to booking form");
        flightProductReviewPage.continueToBookingForm(tripType);

        step("Fill passenger detail");
        flightBookingFormPage.fillPassengerDetail(adultPassengers,childPassengers,infantPassengers, "domestic","");

        step("Continue to second half booking form");
        flightBookingFormPage.continueToSecondHalf();

        step("Continue to payment");
        flightBookingFormPage.continueToPayment();
        flightBookingFormPage.skipFullProtection();

        step("check if travel plan is not approved");
        webViewPage.switchToWebViewUrl("corporate/interim");
        Assertion.assertThat("Travel Plan on Interim Page should not be displayed with plan name " + planName, travelPlanWeb.checkTravelPlanIsInactiveOrPlanIsThereOrNot(planName), is(false));
        webViewPage.switchToNative();
    }

    @Author(names = {"Aditi"})
    @Module(name = "ApprovalNotes")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void verifyApprovalNotesInterimPageAirportTransfer() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyApprovalNotesInterimPageAirportTransfer");
        BaseTest.paramsThreadLocal.set(param);

        String email = param.get("email");
        String password = param.get("password");
        String currency = param.get("currency");
        String airportCode = param.get("airportCode");
        String destination = param.get("destination");
        String adminEmail = param.get("adminUsername");
        String adminPassword = param.get("adminPassword");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        CorporateLoginPage<WebDriver> corporateLoginPage = CorporateLoginPage.getInstance(session);
        CarTransferSearchPage<WebDriver> carTransferSearchPage = CarTransferSearchPage.getInstance(session);
        AirportTransferLandingPageWeb<?> airportTransferLandingPageWeb = AirportTransferLandingPageWeb.getInstance(session);
        CarTransferSearchResultPage<WebDriver> carTransferSearchResultPage = CarTransferSearchResultPage.getInstance(session);
        AirportTransferSRPWeb<?> airportTransferSearchPage = AirportTransferSRPWeb.getInstance(session);
        AirportTransferPDPWeb<?> airportTransferPDPWeb = AirportTransferPDPWeb.getInstance(session);
        CarTransferBookingPage<WebDriver> carTransferBookingPage = CarTransferBookingPage.getInstance(session);
        AirportTransferBookingPageWeb<?> airportTransferBookingPage = AirportTransferBookingPageWeb.getInstance(session);
        TravelPlanWeb<?> travelPlanWeb = TravelPlanWeb.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);

        step("login with b2b traveler account");
        homePage.gotoHome();
        loginPage.loginAsGuest();
        corporateLoginPage.corporateLogin(email, password);
        homePage.changeCurrency(currency, email);
        String language = homePage.getLanguageApp();

        step("select Airport transfer");
        homePage.openVerticalPageDeeplink("Car Transfers", language, currency);
        carTransferSearchPage.allowLocationAccess();
        webViewPage.switchToWebViewUrl("airport-transfer");
        airportTransferLandingPageWeb.closePopup();

        step("search for from Airport ("+airportCode+" -> "+destination+")");
        airportTransferLandingPageWeb.chooseFromAirport();
        airportTransferLandingPageWeb.searchAirport(airportCode);
        airportTransferLandingPageWeb.searchDestination(destination);

        step("select car");
        airportTransferLandingPageWeb.searchCars();
        airportTransferSearchPage.chooseCar();
        airportTransferSearchPage.chooseVendor();

        step("proceed further to booking form page");
        airportTransferPDPWeb.clickSubmitCar();

        step("after clicking continue to pay");
        airportTransferBookingPage.clickSubmitBooking();
        webViewPage.switchToNative();

        step("will get Redirected to Interim Page");
        webViewPage.switchToWebViewUrl("airport-transfer/booking");
        Assertion.assertThat("Interim Page should be visible", travelPlanWeb.isTravelPlanInterimPageShown(), is(true));

        step("Add approval notes on the interim page");
        String initialNotes = travelPlanWeb.addApprovalNotes();
        travelPlanWeb.skipTravelPlan();
        webViewPage.switchToNative();

        step("Verify orderID is displayed in payment page");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("Verify order ID is displayed in payment page", !orderID.isEmpty(), is(true));

        step("Check if Corporate billing option is selected");
        Assertion.assertThat("Corporate billing option is selected", paymentPage.getSelectedPayment().toLowerCase(), is("corporate billing"));

        step("Click pay with corporate billing");
        paymentPage.clickPayButtonWithoutWaitForPreviousBookingPopup();

        step("verify successfully placed order popup from corporate");
        Assertion.assertThat("get successfully placed order popup",paymentPage.isCorporateSuccessfullyPlacedOrderPopup(),is(true));

        step("verify if approval notes present in purchase list for the specific order");
        String notesFromDashboard = corporateApi.checkApprovalNotesPurchaseOrderList(orderID, adminEmail, adminPassword);
        Assertion.assertThat("Approval notes reflected on purchase list for the specific order is same as entered on travel plan page", notesFromDashboard, is(initialNotes));

    }

    @Author(names = {"Aditi"})
    @Module(name = "Booking")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void testBookingTrainInstantPaymentBCA() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testBookingTrainInstantPaymentBCA");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        TrainSearchResultPage<WebDriver> trainSearchResultPage = TrainSearchResultPage.getInstance(session);
        PaymentApi<?> paymentHitterApiPage = new PaymentApi<>(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);
        TrainSearchPage<WebDriver> trainSearchPage = TrainSearchPage.getInstance(session);
        TrainBookingPage<WebDriver> trainBookingPage = TrainBookingPage.getInstance(session);
        CorporateLoginPage<WebDriver> corporateLoginPage = CorporateLoginPage.getInstance(session);
        TravelPlanWeb<?> travelPlanWeb = TravelPlanWeb.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);

        String username = param.get("username");
        String password = param.get("password");
        String tripType = param.get("tripType");
        String currency = param.get("currency");
        String originStation = param.get("originStation");
        String destinationStation = param.get("destinationStation");
        String channelType = param.get("channelType");
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));

        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate,daysToDepartureDate);

        step("Wait for app to launch");
        homePage.gotoHome();

        step("B2B login");
        corporateLoginPage.corporateLogin(username,password);

        step("Get App Language");
        String language = homePage.getLanguageApp();

        step("Open Train Landing Page");
        homePage.openVerticalPageDeeplink("Trains", language, currency);
        step("Skip Popup");
        trainSearchPage.skipPopup();

        step("Select origin station");
        trainSearchPage.selectOrigin(originStation);

        step("Select destination station");
        trainSearchPage.selectDestination(destinationStation);

        step("Select date");
        trainSearchPage.selectDate(departureDate);

        step("Add passenger");
        trainSearchPage.addPassenger(adultPassengers,infantPassengers);

        step("Search train");
        trainSearchPage.searchTrain();

        step("Skip Popup");
        trainSearchPage.skipPopup();

        if(Configs.PLATFORM == Platform.ANDROID) {
            step("Open price detail train that has nudges");
            trainSearchPage.openTrainDetailWithNudges();

            step("Verify price is present in price detail tab");
            Assertion.assertThat("Verify price is present in price detail tab", trainSearchPage.verifyTrainDetailPrice(), is(true));

            step("continue to booking page");
            trainSearchPage.clickChooseInTrainDetail();

            step("Verify first page stepper");
            Assertion.assertThat("Verify first page stepper is visible", trainBookingPage.verifyFirstPageStepper(), is(true));
        }
        else {
            step("user select train");
            trainSearchResultPage.selectTrain();
        }

        step("Fill passenger detail");
        trainBookingPage.fillPassengerDetail(adultPassengers,infantPassengers);

        step("Continue to 2nd half");
        trainBookingPage.continueTo2ndHalf();

        if(Configs.PLATFORM == Platform.ANDROID){
            step("Verify second page stepper");
            Assertion.assertThat("Verify stepper show user in second page", trainBookingPage.verifySecondPageStepper(), is(true));
        }

        step("Select seat");
        trainBookingPage.selectSeat(tripType,adultPassengers);

        step("Continue to payment");
        trainBookingPage.continueToPayment();
        webViewPage.switchToWebViewUrl("corporate/interim");
        travelPlanWeb.skipTravelPlan();
        webViewPage.switchToNative();

        step("Check order ID");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id", !orderID.isBlank(),is(true));

        step("Open payment method & select BCA VA");
        paymentPage.openPaymentMethod();
        paymentPage.selectPaymentMethod("BCA Virtual Account");

        step("Submit payment ");
        paymentPage.clickPayButton();

        step("get account info number");
        String requestId = paymentPage.getAccountInfoNumber();

        step("Hit the payment api to complete the payment");
        paymentHitterApiPage.hitBCAVAApi(requestId,orderID,channelType);
        orderListPage.waitForPaymentSuccessful(orderID,10);

        step("Verify in Booking List that order should be present");
        Assertion.assertThat("Order is reflected on B2B corporate dashboard under booking List " , corporateApi.checkBookingOrder(orderID, username, password,"train",CoreFunctions.getCurrentDate("d MMMM YYYY") , CoreFunctions.getCurrentDate("d MMMM YYYY"),"instant_payment"), is(true));
        
        step("Open the order list and go to order detail");
        paymentPage.SeeOrderListAfterPaymentSuccess();

        step("check order status");
        orderListPage.openIssuedTicketViaDeeplink(orderID);
        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.getETicketLabel(), either(is("Your e-ticket is here")).or(is("E-tiketmu di sini")));
    }

    @Author(names = "Mridul")
    @Module(name = "Booking")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void hotelBookingBySuperAdminConvertTraveler(){
        Map<String, String> param = getParam("hotelBookingBySuperAdminConvertTraveler");
        BaseTest.paramsThreadLocal.set(param);
        Session<WebDriver> session = sessionThreadLocal.get();

        CorporateApi<?> corporateApi = new CorporateApi<>(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        CorporateLoginPage<WebDriver> corporateLoginPage = CorporateLoginPage.getInstance(session);
        TravelPlanWeb<?> travelPlanWeb = TravelPlanWeb.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);

        String superAdminUsername = param.get("superAdminUsername");
        String superAdminPassword = param.get("superAdminPassword");
        String  travelerUsername= param.get("travelerUsername");
        String travelerPassword = param.get("travelerPassword");
        String hotelName = param.get("hotelName");
        String currency = param.get("currency");

        step("Convert this Email : "+superAdminUsername+" to Traveler using this email : "+travelerUsername);
        corporateApi.updateEmployeeCorporateRole(superAdminUsername,superAdminPassword,travelerUsername,1,"super_administrator");
        boolean isConvertedToTraveler = corporateApi.updateEmployeeCorporateRole(travelerUsername,travelerPassword,superAdminUsername,1,"traveler");

        step("Check the SUPER ADMIN email "+superAdminUsername+" is converted to TRAVELER");
        Assertion.assertThat("Check the SUPER ADMIN email "+superAdminUsername+" is converted to TRAVELER",isConvertedToTraveler,is(true));

        step("login with b2b traveler account");
        homePage.gotoHome();
        loginPage.loginAsGuest();
        corporateLoginPage.corporateLogin(superAdminUsername, superAdminPassword);
        homePage.changeCurrency(currency, superAdminUsername);

        step("Open hotel landing page");
        homePage.openVerticalPageDeeplink("Hotel","",currency);

        step("Search stay location");
        landingPage.searchStayLocation(hotelName);

        step("Submit search");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        step("Go to the room list");
        detailPage.RemovePopUp();
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();

        step("Select room from the room list");
        roomListPage.getTheFirstHotelRoomPrice();
        roomListPage.clickBookRoom();

        step("Submit the booking form");
        bookingFormPage.continueToPaymentPage();
        webViewPage.switchToWebViewUrl("corporate/interim");
        travelPlanWeb.skipTravelPlan();
        webViewPage.switchToNative();
        bookingFormPage.PriceChangesPopUp();

        step("Payment page should be displayed");
        String orderID = paymentPage.checkOrderID();
        orderID = String.valueOf(getOrderId(orderID));

        step("Submit payment using Corporate Billing");
        paymentPage.clickPayButtonWithoutWaitForPreviousBookingPopup();
        orderListPage.waitForPaymentSuccessful(orderID,10);

        step("Wait for 1 mins");
        sleep(60); // Adding a wait to handle super admin downgrade to traveler, ensuring auto-approval in case of issues.

        step("Open the order list and go to order detail");
        paymentPage.SeeOrderListAfterPaymentSuccess();
        Assertion.assertThat("orderID should be visible and should be same as from payment page and in purchase List ", corporateApi.checkPurchaseOrderList(orderID,superAdminUsername,superAdminPassword,"Super Admin"), is(true));

        step("Convert this Email : "+superAdminUsername+" to Super Admin using this email : "+travelerUsername);
        boolean isConvertedToSuper = corporateApi.updateEmployeeCorporateRole(travelerUsername,travelerPassword,superAdminUsername,1,"super_administrator");

        step("Check the traveler email "+superAdminUsername+" is converted back to SUPER ADMIN");
        Assertion.assertThat("Check the traveler email "+superAdminUsername+" is converted back to SUPER ADMIN",isConvertedToSuper,is(true));

    }

    @Author(names = {"Ashish"})
    @Module(name = "InterimPage")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void testLoginTrainTravelPlanApprovedCheckInterimPage() throws ExecutionException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testLoginTrainTravelPlanApprovedCheckInterimPage");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TrainSearchPage<WebDriver> trainSearchPage = TrainSearchPage.getInstance(session);
        TrainBookingPage<WebDriver> trainBookingPage = TrainBookingPage.getInstance(session);
        TravelPlanWeb<?> travelPlanWeb = TravelPlanWeb.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);
        Unm<?> unm = new Unm<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String adminUsername = param.get("adminUsername");
        String adminPassword = param.get("adminPassword");

        String tripType = param.get("tripType");
        String currency = param.get("currency");
        String departureCode = param.get("departureCode");
        String departureName = param.get("departureName");
        String departureType = param.get("departureType");
        String arrivalCode = param.get("arrivalCode");
        String arrivalName = param.get("arrivalName");
        String arrivalType = param.get("arrivalType");
        boolean isRoundTrip = tripType.contains("round");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        String planName = param.get("planName");

        step("get login access token for user " + username + "employee data");
        LoginDataUnmResponse.Data loginDataUnmResponse = unm.unmLoginApi(username, password);
        EmployeeData.Data employeeData = corporateApi.getEmployeeData(username).data;

        step("Create a train plan");
        PostCreateTravelPlanBody.Configs travelPlanConfigs = corporateApi.setTravelPlanTrainData(departureCode,departureType, departureName, arrivalCode,arrivalType, arrivalName,isRoundTrip, daysToDepartureDate,0);
        TravelPlanPojo travelPlanPojo = corporateApi.checkAndCreateTravelPlan(loginDataUnmResponse,username,adminUsername,planName,daysToDepartureDate,1,travelPlanConfigs, "name","train","approved",employeeData, new ArrayList<>());
        Assertion.assertThat("Train Plan Created " + travelPlanPojo.travelPlanID, travelPlanPojo.travelPlanID,not(-1));

        step("Approve the plan "+travelPlanPojo.travelPlanID);
        Assertion.assertThat("Plan got approved "+travelPlanPojo.travelPlanID,corporateApi.updateTravelPlanApproval(adminUsername,adminPassword,travelPlanPojo),is(true));

        step("set data form plan");
        daysToDepartureDate = CoreFunctions.getDaysToDepartureDate(travelPlanPojo.startDate);
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate,daysToDepartureDate);
        departureName = travelPlanPojo.train.departureCode;
        arrivalName = travelPlanPojo.train.arrivalCode;

        step("User open app");
        homePage.gotoHome();
        loginPage.loginAsGuest();

        step("B2B login");
        loginPage.loginWith("email",username,password);

        step("Verify Step");
        Assertion.assertThat("User is on login state", homePage.isUserLogin(), is(true));

        step("Change currency");
        homePage.changeCurrency(currency,username);

        step("Open train page");
        homePage.openVerticalPageDeeplink("Trains","",currency);

        step("Select origin station");
        trainSearchPage.skipPopup();
        trainSearchPage.selectOrigin(departureName);

        step("Select destination station");
        trainSearchPage.selectDestination(arrivalName);

        step("Toggle oneway");
        trainSearchPage.toggleTripType(tripType);

        step("Select date");
        trainSearchPage.selectDate(departureDate);

        step("Add passenger");
        trainSearchPage.addPassenger(adultPassengers,infantPassengers);

        step("Search train");
        trainSearchPage.searchTrain();

        step("Skip Popup");
        trainSearchPage.skipPopup();

        step("Select departure train");
        trainSearchPage.selectTrainViaIndex(2);

        step("Fill passenger detail");
        trainBookingPage.fillPassengerDetail(adultPassengers,infantPassengers);

        step("Continue to 2nd half");
        trainBookingPage.continueTo2ndHalf();

        step("Continue to payment");
        trainBookingPage.continueToPayment();

        step("select Travel plan");
        webViewPage.switchToWebViewUrl("corporate/interim");
        travelPlanWeb.selectTravelPlan(planName);
        Assertion.assertThat("Travel Plan on Interim Page should be selected with given plan name" + planName, travelPlanWeb.isCorrectTravelPlanSelected(planName), is(true));
        webViewPage.switchToNative();
    }

    @Author(names = {"Yazid"})
    @Module(name = "HotelTravelPlan")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void hotelTravelPlanCustomFields() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("hotelTravelPlanCustomFields");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        TravelPlanWeb<?> travelPlanWeb = TravelPlanWeb.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);
        Unm<?> unm = new Unm<>(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        HotelPage<WebDriver> hotelPage = HotelPage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);

        String adminUsername = param.get("adminUsername");
        String username = param.get("username");
        String password = param.get("password");
        String planName = param.get("planName");
        int nightDuration = Integer.parseInt(param.get("nightDuration"));
        String location = param.get("location");
        String publicId = param.get("publicId");
        String locationType = param.get("locationType").toUpperCase();
        String checkInDate = CoreFunctions.addToDateYYYY_MM_DD(getCurrentDateYYYY_MM_DD(), 4);
        String checkOutDate = CoreFunctions.addToDateYYYY_MM_DD(checkInDate, nightDuration);
        String currency = "IDR";
        String accommodationType = "hotel";
        int totalRoom = 1;
        int totalGuest = 1;
        String language = "en";

        step("Change Config to true for Travel Plan Page on account "+adminUsername);
        boolean configUpdated = corporateApi.updateTravelPlanSetting(adminUsername, password,true);
        Assertion.assertThat("Travel Plan setting should be updated to True", configUpdated, is(true));

        step("get login access token for user " + username + "employee data");
        LoginDataUnmResponse.Data loginDataUnmResponse = unm.unmLoginApi(username, password);
        EmployeeData.Data employeeData = corporateApi.getEmployeeData(username).data;

        step("Create a hotel plan");
        PostCreateTravelPlanBody.Configs travelPlanConfigs = corporateApi.setTravelPlanHotelData(publicId, location, checkInDate, checkOutDate, nightDuration, locationType);
        ArrayList<PostCreateTravelPlanBody.customFields> customFieldData = corporateApi.setCustomFieldsData(loginDataUnmResponse, username);
        TravelPlanPojo travelPlanPojo = corporateApi.checkAndCreateTravelPlan(loginDataUnmResponse, username, adminUsername, planName, 0, nightDuration, travelPlanConfigs, "name", "hotel", "approved", employeeData, customFieldData);
        Assertion.assertThat("hotel Plan Created " + travelPlanPojo.travelPlanID, travelPlanPojo.travelPlanID, not(-1));

        step("Approve the plan " + travelPlanPojo.travelPlanID);
        Assertion.assertThat("Plan got approved " + travelPlanPojo.travelPlanID, corporateApi.updateTravelPlanApproval(adminUsername, password, travelPlanPojo), is(true));

        step("Goto home");
        homePage.gotoHome();

        step("Login as B2B account " + username + " via deeplink and change currency to " + currency);
        homePage.logoutViaDeeplink();
        homePage.loginWithSpecificAccountViaDeeplink(username, password, currency);

        step("Get the SRP deeplink parameter");
        HotelAutoCompleteResponse hotelAutoCompleteResponse = hotelApi.getAutocompleteData(username, password, location, locationType);
        String locationId = hotelAutoCompleteResponse.data.contents.get(0).publicId;

        step("user go to the SRP via deeplink");
        hotelPage.openSRPDeeplink(accommodationType.toLowerCase(), totalRoom, totalGuest, locationId, locationType, location, checkInDate, checkOutDate, language);
        landingPage.fillNightCheckInForm();

        step("Select hotel from the hotel list");
        searchResultPage.selectFirstHotelCard();

        step("Go to the room list");
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();

        step("Select room from the room list");
        roomListPage.getTheFirstHotelRoomPrice();
        roomListPage.clickBookRoom();

        step("Submit the booking form");
        bookingFormPage.continueToPaymentPage();

        step("select Travel plan");
        webViewPage.switchToWebViewUrl("corporate/interim");
        travelPlanWeb.selectTravelPlan(planName);
        Assertion.assertThat("Travel Plan on Interim Page should be selected with given plan name" + planName, travelPlanWeb.isCorrectTravelPlanSelected(planName), is(true));
        Assertion.assertThat("all custom fields should be filled automatically", travelPlanWeb.checkTravelPlanCustomFields(travelPlanPojo), is(true));
        travelPlanWeb.selectContinueToPay();
        webViewPage.switchToNative();
        bookingFormPage.PriceChangesPopUp();

        step("Payment page should be displayed");
        String orderID = paymentPage.checkOrderID();
        orderID = String.valueOf(getOrderId(orderID));
        Assertion.assertThat("Corporate billing option is selected", paymentPage.getSelectedPayment().toLowerCase(), is("corporate billing"));

        step("Submit payment using Corporate Billing");
        paymentPage.clickPayButtonWithoutWaitForPreviousBookingPopup();
        Assertion.assertThat("get successfully placed order popup",paymentPage.isCorporateSuccessfullyPlacedOrderPopup(),is(true));

        step("Admin can see the custom fields attached with orderID under corporate Orders");
        Assertion.assertThat("Custom fields from travel plan are attached and visible to admin for orderID " + orderID, corporateApi.checkCustomFieldsPurchaseOrder(adminUsername, password, orderID, travelPlanPojo), greaterThan(0));
    }

    @Author(names = {"Aditi"})
    @Module(name = "HotelTravelPlan")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void testLoginHotelTravelPlanApproved() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testLoginHotelTravelPlanApproved");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        TravelPlanWeb<?> travelPlanWeb = TravelPlanWeb.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);
        Unm<?> unm = new Unm<>(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        HotelPage<WebDriver> hotelPage = HotelPage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String adminUsername = param.get("adminUsername");
        String adminPassword = param.get("adminPassword");
        String planName = param.get("planName");
        String location = param.get("location");
        String publicId = param.get("publicId");
        String locationType = param.get("locationType");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));

        int totalRoom = 1;
        int totalGuest = 1;
        int totalNight = 1;
        String currency = "IDR";
        String language = "en";
        String accommodationType = "hotel";

        String checkInDate = getCurrentDateYYYY_MM_DD();
        String checkOutDate = CoreFunctions.addToDateYYYY_MM_DD(checkInDate, totalNight);

        step("Change Config to true for Travel Plan Page on account "+adminUsername);
        boolean configUpdated = corporateApi.updateTravelPlanSetting(adminUsername, adminPassword,true);
        Assertion.assertThat("Travel Plan setting should be updated to True", configUpdated, is(true));

        step("get login access token for user " + username + "employee data");
        LoginDataUnmResponse.Data loginDataUnmResponse = unm.unmLoginApi(username, password);
        EmployeeData.Data employeeData = corporateApi.getEmployeeData(username).data;

        step("Create a hotel plan");
        PostCreateTravelPlanBody.Configs travelPlanConfigs = corporateApi.setTravelPlanHotelData(publicId, location, getCurrentDateYYYY_MM_DD(), CoreFunctions.addToDateYYYY_MM_DD(getCurrentDateYYYY_MM_DD(), daysToDepartureDate), daysToDepartureDate, locationType);
        TravelPlanPojo travelPlanPojo = corporateApi.checkAndCreateTravelPlan(loginDataUnmResponse, username, adminUsername, planName, daysToDepartureDate, 1, travelPlanConfigs, "name", "hotel", "approved", employeeData, new ArrayList<>());
        Assertion.assertThat("hotel Plan Created " + travelPlanPojo.travelPlanID, travelPlanPojo.travelPlanID, not(-1));

        step("Approve the plan " + travelPlanPojo.travelPlanID);
        Assertion.assertThat("Plan got approved " + travelPlanPojo.travelPlanID, corporateApi.updateTravelPlanApproval(adminUsername, adminPassword, travelPlanPojo), is(true));

        step("Goto home");
        homePage.gotoHome();

        step("Login as B2B account " + username + " via deeplink and change currency to " + currency);
        homePage.loginWithSpecificAccountViaDeeplink(username, password, currency);

        step("Get the SRP deeplink parameter");
        HotelAutoCompleteResponse hotelAutoCompleteResponse = hotelApi.getAutocompleteData(username, password, location, locationType);
        int selectedIndex = hotelApi.findHotelIndex(hotelAutoCompleteResponse, location);
        String locationId = hotelAutoCompleteResponse.data.contents.get(selectedIndex).publicId;

        step("user open SRP via deeplink");
        hotelPage.openSRPDeeplink(accommodationType.toLowerCase(), totalRoom, totalGuest, locationId, locationType, location, checkInDate, checkOutDate, language);
        landingPage.fillNightCheckInForm();

        step("Select hotel from the hotel list");
        searchResultPage.selectFirstHotelCard();

        step("Go to the room list");
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();

        step("Select room from the room list");
        roomListPage.getTheFirstHotelRoomPrice();
        roomListPage.clickBookRoom();

        step("Submit the booking form");
        bookingFormPage.continueToPaymentPage();

        step("select Travel plan");
        webViewPage.switchToWebViewUrl("corporate/interim");
        travelPlanWeb.selectTravelPlan(planName);
        Assertion.assertThat("Travel Plan on Interim Page should be selected with given plan name" + planName, travelPlanWeb.isCorrectTravelPlanSelected(planName), is(true));
        travelPlanWeb.selectContinueToPay();
        webViewPage.switchToNative();

        step("Payment page should be displayed");
        String orderID = paymentPage.checkOrderID();
        orderID = String.valueOf(getOrderId(orderID));
        Assertion.assertThat("Corporate billing option is selected", paymentPage.getSelectedPayment().toLowerCase(), is("corporate billing"));

        step("Submit payment using Corporate Billing");
        paymentPage.clickPayButtonWithoutWaitForPreviousBookingPopup();
        Assertion.assertThat("get successfully placed order popup",paymentPage.isCorporateSuccessfullyPlacedOrderPopup(),is(true));

        step("Approve booking by admin account for Order ID: " + orderID);
        Boolean bookingStatus = corporateApi.approveBookingB2BApi(adminUsername, adminPassword, orderID);
        Assertion.assertThat("Booking approved by B2B dashboard", bookingStatus, is(true));

        step("Open the order list and go to order detail");
        paymentPage.SeeOrderListAfterPaymentSuccess();;
        orderListPage.openIssuedTicketViaDeeplink(orderID);
        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.getETicketLabel(), either(is("Your e-ticket is here")).or(is("E-tiketmu di sini")));

        step("Change Config to false for Travel Plan Page on account "+adminUsername);
        configUpdated = corporateApi.updateTravelPlanSetting(adminUsername, adminPassword,false);
        Assertion.assertThat("Travel Plan setting should be updated to False", configUpdated, is(true));
    }

    @Author(names = {"Aditi"})
    @Module(name = "TravelPlan")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void selectTravelPlanWhooshBooking() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("selectTravelPlanWhooshBooking");
        BaseTest.paramsThreadLocal.set(param);

        String username = param.get("username");
        String password = param.get("password");
        String adminUsername = param.get("adminUsername");
        String adminPassword = param.get("adminPassword");
        String currency = param.get("currency");
        String originStation = param.get("originStation");
        String destinationStation = param.get("destinationStation");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String originStationCode = param.get("originStationCode");
        String destinationStationCode = param.get("destinationStationCode");
        String planName = param.get("planName");
        String tripType = param.getOrDefault("tripType", "roundtrip");
        int adultPassengers = Integer.parseInt(param.getOrDefault("adult", "1"));
        int infantPassengers = Integer.parseInt(param.getOrDefault("infant", "0"));

        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);
        String returnDate = CoreFunctions.addToLongDate(departureDate, daysToDepartureDate);
        boolean isRoundTrip = tripType.equalsIgnoreCase("roundtrip");

        CorporateApi<?> corporateApi = new CorporateApi<>(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        WhooshSearchPage<WebDriver> whooshSearchPage = WhooshSearchPage.getInstance(session);
        WhooshSearchResultPage<WebDriver> whooshSearchResultPage = WhooshSearchResultPage.getInstance(session);
        WhooshBookingPage<WebDriver> whooshBookingPage = WhooshBookingPage.getInstance(session);
        TravelPlanWeb<?> travelPlanWeb = TravelPlanWeb.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        Unm<?> unm = new Unm<>(session);

        step("Change Config to true for Travel Plan Page on account " + adminUsername);
        boolean configUpdated = corporateApi.updateTravelPlanSetting(adminUsername, adminPassword, true);
        Assertion.assertThat("Travel Plan setting should be updated to True", configUpdated, is(true));

        step("get login access token for user");
        LoginDataUnmResponse.Data loginDataUnmResponse = unm.unmLoginApi(username, password);
        EmployeeData.Data employeeData = corporateApi.getEmployeeData(username).data;

        step("Create a whoosh plan");
        PostCreateTravelPlanBody.Configs travelPlanConfigs = corporateApi.setTravelPlanWhooshData(originStationCode, originStation, destinationStationCode, destinationStation, isRoundTrip, departureDate, returnDate);
        TravelPlanPojo travelPlanPojo = corporateApi.checkAndCreateTravelPlan(loginDataUnmResponse, username, adminUsername, planName, daysToDepartureDate, 1, travelPlanConfigs, "data", "whoosh", "approved", employeeData, new ArrayList<>());
        Assertion.assertThat("Whoosh Travel Plan created with planId: " + travelPlanPojo.travelPlanID, travelPlanPojo.travelPlanID, not(-1));

        step("Approve the plan " + travelPlanPojo.travelPlanID);
        Assertion.assertThat("Plan got approved " + travelPlanPojo.travelPlanID, corporateApi.updateTravelPlanApproval(adminUsername, adminPassword, travelPlanPojo), is(true));

        departureDate = CoreFunctions.convertToLongDate(travelPlanPojo.whoosh.departureDate);
        if (isRoundTrip) {
            returnDate = CoreFunctions.convertToLongDate(travelPlanPojo.whoosh.returnDate);
        }

        step("Login as B2B traveler account " + username + " via deeplink and change currency to " + currency);
        homePage.gotoHome();
        homePage.loginWithSpecificAccountViaDeeplink(username, password, currency);

        step("Open whoosh Landing Page");
        homePage.openVerticalPageDeeplink("Whoosh", "", currency);

        step("Skip Popup");
        whooshSearchPage.skipPopup();

        step("Toggle trip type to " + tripType);
        whooshSearchPage.selectRoundTripToggle(tripType);

        step("Select date");
        whooshSearchPage.changeDateViaDeeplink(departureDate,returnDate,"roundtrip");

        step("Select origin station");
        whooshSearchPage.selectOrigin(originStation);

        step("Select destination station");
        whooshSearchPage.selectDestination(destinationStation);

        step("Add passenger");
        whooshSearchPage.addPassenger(adultPassengers, infantPassengers);

        step("Search whoosh");
        whooshSearchPage.searchTrain();

        step("Skip Popup");
        whooshSearchPage.skipPopup();

        step("Select departure whoosh");
        whooshSearchResultPage.selectWhoosh();

        if (isRoundTrip) {
            step("Select return whoosh");
            whooshSearchResultPage.selectWhoosh();
        }

        step("Fill passenger detail");
        whooshBookingPage.fillPassengerDetail(adultPassengers, infantPassengers);

        step("Continue to 2nd half");
        whooshBookingPage.continueTo2ndHalf();

        step("Continue to payment");
        whooshBookingPage.continueToPayment();

        step("select Travel plan");
        webViewPage.switchToWebViewUrl("corporate/interim");
        travelPlanWeb.selectTravelPlan(planName);
        Assertion.assertThat("Travel Plan on Interim Page should be selected with given plan name" + planName, travelPlanWeb.isCorrectTravelPlanSelected(planName), is(true));
        travelPlanWeb.selectContinueToPay();
        webViewPage.switchToNative();

        step("Check order ID");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",!orderID.isBlank(),is(true));

        step("Change Config to false for Travel Plan Page on account " + adminUsername);
        configUpdated = corporateApi.updateTravelPlanSetting(adminUsername, adminPassword, false);
        Assertion.assertThat("Travel Plan setting should be updated to False", configUpdated, is(true));
    }

    @Author(names = {"Aditi"})
    @Module(name = "VIBooking")
    @Tribe(name = "B2B")
    @Vertical(name = "B2B")
    @Test
    public void testFlightVIOneWayRouteLoginB2B() throws InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testFlightVIOneWayRouteLoginB2B");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightProductReviewPage<WebDriver> flightProductReviewPage = FlightProductReviewPage.getInstance(session);
        FlightBookingFormPage<WebDriver> flightBookingFormPage = FlightBookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        TravelPlanWeb<?> travelPlanWeb = TravelPlanWeb.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        FlightApi<WebDriver> flightApi = new FlightApi<>(session);

        String tripType = param.get("tripType");
        String location1 = param.get("location1");
        String location1CityName = param.get("location1CityName");
        String location2 = param.get("location2");
        String location2CityName = param.get("location2CityName");
        String location3 = param.get("location3");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate =CoreFunctions.addToLongDate(currentDate,daysToDepartureDate);

        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        String airline = param.get("airline");
        String currency = param.get("currency");
        String username = param.get("username");
        String password = param.get("password");

        PriceDetail priceDetail = new PriceDetail();
        priceDetail.tripType=tripType;
        priceDetail.adult=adultPassengers;
        priceDetail.child=childPassengers;
        priceDetail.infant=infantPassengers;

        step("Set the scheduler config to true");
        Assertion.assertThat("Config is set to true", flightApi.toggleScheduleIngestion(true), is(true));

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Login as B2B account " + username + " via deeplink and change currency to " + currency);
        homePage.loginWithSpecificAccountViaDeeplink(username, password, currency);

        step("Open flights page");
        homePage.openVerticalPageDeeplink("Flights","",currency);
        flightSearchPage.closePopup();

        step("Toggle trip type");
        flightSearchPage.toggleTriptype(tripType);

        step("Select date");
        flightSearchPage.selectDate(departureDate);

        step("Add passenger");
        flightSearchPage.addPassenger(adultPassengers,childPassengers,infantPassengers);

        //create cache of schedule each segments route
        if(Configs.PLATFORM == Platform.ANDROID){

            step("select location1 { " + location1 +" } and location2 { " + location2 +" }");
            flightSearchPage.selectOrigin(location1);
            flightSearchPage.closePopup();
            flightSearchPage.selectDestination(location2);

            step("Search flight");
            flightSearchPage.searchFlight();
            flightSearchResultPage.waitForPageLoad();
        }else {
            //using deeplink to set the date
            step("select location1 { " + location1 +" } and location2 { " + location2 +" }");
            flightSearchPage.deeplinkWithSelectedDateSrp(location1,location2,location1CityName,location2CityName,daysToDepartureDate,adultPassengers,childPassengers,infantPassengers);
            webViewPage.switchToNative();
            flightSearchResultPage.waitForPageLoad();
            flightSearchResultPage.changeTrip(tripType);
            flightSearchResultPage.waitForPageLoad();
        }

        step("Back to flights page");
        flightSearchResultPage.backToFlightPage();

        step("select location2 { " + location2 +" } and location3 { " + location3 +" }");
        flightSearchPage.selectOrigin(location2);
        flightSearchPage.closePopup();
        flightSearchPage.selectDestination(location3);

        step("Search flight");
        flightSearchPage.searchFlight();
        flightSearchResultPage.waitForPageLoad();

        step("Back to flights page");
        flightSearchResultPage.backToFlightPage();

        step("select location1 { " + location1 +" } and location3 { " + location3 +" }");
        flightSearchPage.selectOrigin(location1);
        flightSearchPage.closePopup();
        flightSearchPage.selectDestination(location3);

        step("Search flight");
        flightSearchPage.searchFlight();

        step("Back to flights page");
        flightSearchResultPage.backToFlightPage();

        step("search again with same route to make sure schedule data is cached");
        flightSearchPage.searchFlight();
        flightSearchResultPage.waitForPageLoad();

        step("Use self transfer filter");
        flightSearchPage.selectSelfTransferFilter();

        step("verify airline");
        Assertion.assertThat("is VI airline present",flightSearchResultPage.isVI_AirlineDisplayed(),is(true));

        step("Click flight detail chevron");
        flightSearchResultPage.selectFlight();

        step("verify self-transfer indicator in prp");
        Assertion.assertThat("Self-transfer indicator in prp ",flightProductReviewPage.isCheckInAgainTextDisplayed(),is(true));

        step("Tap self-transfer banner and verify Self Transfer Protection included in bottom sheet");
        Assertion.assertThat("Self Transfer Protection included shown in PRP bottom sheet ",flightProductReviewPage.isConnectingFlightProtection(),is(true));

        step("Continue to booking form");
        flightProductReviewPage.continueToBookingForm(tripType);

        step("Skip full protection if visible");
        flightBookingFormPage.skipFullProtectionPrp();

        step("verify date format");
        flightBookingFormPage.verifyDateFormat(tripType);

        step("Fill contact & passenger detail");
        flightBookingFormPage.fillPassengerDetail(adultPassengers,childPassengers,infantPassengers,"domestic",airline);

        step("Continue to second half booking form");
        flightBookingFormPage.continueToSecondHalf();

        step("verify yellow banner");
        Assertion.assertThat("Validate there will be warning message 'You chose connecting flights'",flightBookingFormPage.verifyWarningMassage(),is(true));

        step("Continue to payment");
        flightBookingFormPage.continueToPayment();

        step("Skip full protection");
        flightBookingFormPage.skipFullProtection();
        webViewPage.switchToWebViewUrl("corporate/interim");
        Assertion.assertThat("Connecting flights card is shown", travelPlanWeb.isConnectingFlightsCardShown(), is(true));
        travelPlanWeb.skipTravelPlan();
        webViewPage.switchToNative();
        flightBookingFormPage.baggageAllowancePopup();

        step("Check order ID");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",!orderID.isBlank(),is(true));
    }

}
