package com.tiket.web.test.nft.train;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Environment;
import com.tiket.model.Platform;
import com.tiket.model.Session;
import com.tiket.pojo.train.TrainClass;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.testbase.Configs;
import com.tiket.web.WebTest;
import com.tiket.web.dWeb.page.payment.PaymentPage;
import com.tiket.web.page.common.calendar.CalendarPageWeb;
import com.tiket.web.page.common.home.HomePageWeb;
import com.tiket.web.page.common.home.LoginPageWeb;
import com.tiket.web.page.nft.train.bookingForm_Page.TrainBookingFormWeb;
import com.tiket.web.page.nft.train.searchResult_Page.TrainSearchResultPageWeb;
import com.tiket.web.page.nft.train.search_Page.TrainSearchPageWeb;

import com.tiket.web.page.platform.payment.PaymentPageWeb;
import com.tiket.web.page.order.OrderDetail.OrderDetailWeb;
import com.tiket.web.page.order.OrderList.OrderListWeb;
import org.testng.annotations.Test;

import javax.security.auth.login.FailedLoginException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;

public class TrainTest extends WebTest {

    @Author(names = "Vipul Kumar")
    @Module(name = "SRP")
    @Tribe(name = "Train")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyTrainRecentlySearchedGuestWeb() {
        Map<String, String> param = getParam("testVerifyTrainRecentlySearchedGuestWeb");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String currency = param.get("currency");
        String originStation = param.get("originStation");
        String destinationStation = param.get("destinationStation");
        String tripType = param.get("tripType");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        TrainSearchPageWeb<?> trainSearchPage = TrainSearchPageWeb.getInstance(session);
        TrainSearchResultPageWeb<?> trainSearchResultPage = TrainSearchResultPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open home page as guest");
        loginPage.loginAsGuest();

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

        step("User verify Train List is present on SRP");
        Assertion.assertThat("Verify that Train List is present on SRP after Search", trainSearchResultPage.checkTrainCard(), is(true));

        step("User verify Recently Searched Train is present");
        Assertion.assertThat("verify that Recently Searched Train is present", trainSearchResultPage.goBackToLandingTrainPage(), is(true));
    }

    @Author(names = {"Naufal Hafiz", "Clement"})
    @Module(name = "Booking Form")
    @Tribe(name = "Train")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyPriceDetailBookingFormWebLogin() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testVerifyPriceDetailBookingFormWebLogin");
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

        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        TrainSearchPageWeb<?> trainSearchPage = TrainSearchPageWeb.getInstance(session);
        TrainSearchResultPageWeb<?> trainSearchResultPage = TrainSearchResultPageWeb.getInstance(session);
        TrainBookingFormWeb<?> trainBookingFormWeb = TrainBookingFormWeb.getInstance(session);
        CalendarPageWeb<?> calendarPageWeb = CalendarPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(username, password, otp, retryCounter);

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
        trainSearchPage.clickOnCalendarWidgetToOpen();
        calendarPageWeb.selectDate(departureDate);

        step("User search train");
        trainSearchPage.searchTrain();

        step("User skip pop-up");
        trainSearchPage.clickSkipPopup();

        step("User gets the price of first train");
        String price = trainSearchResultPage.getPrice();

        step("Select train");
        trainSearchResultPage.selectTrain();

        step("User fill adult details");
        trainBookingFormWeb.fillPassengerDetailAdult(adultCount);

        step("User continue to booking form 2");
        trainBookingFormWeb.continueToBookingForm2();

        step("User clicks total price details button");
        trainBookingFormWeb.checkPriceBtn();

        step("User verifies whether price details page opens or not");
        Assertion.assertThat("Verifies whether price details page appears or not", trainBookingFormWeb.isPriceDetailsDisplayed(), is(true));
        step("User verifies the price details on booking page");
        Assertion.assertThat("Verifies the price on booking page", trainBookingFormWeb.checkPrice(adultCount), is(price));
        trainBookingFormWeb.closePricePopUp();
    }

    @Author(names = "Naufal Hafiz")
    @Module(name = "Booking Form")
    @Tribe(name = "Train")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyPriceDetailBookingFormWebGuest(){
        Map<String, String> param = getParam("testVerifyPriceDetailBookingFormWebGuest");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String currency = param.get("currency");
        String originStation = param.get("originStation");
        String destinationStation = param.get("destinationStation");
        String tripType = param.get("tripType");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);

        String username = param.get("username");
        String phoneNumber = param.get("phoneNumber");
        String email = param.get("email");
        String id = param.get("id");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        TrainSearchPageWeb<?> trainSearchPage = TrainSearchPageWeb.getInstance(session);
        TrainSearchResultPageWeb<?> trainSearchResultPage = TrainSearchResultPageWeb.getInstance(session);
        TrainBookingFormWeb<?> trainBookingFormWeb = TrainBookingFormWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open home page as guest");
        loginPage.loginAsGuest();

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

        step("Select train");
        trainSearchResultPage.selectTrain();

        step("Fill out booking form");
        trainBookingFormWeb.fillBookingForm(username, phoneNumber, email, id);
        trainBookingFormWeb.continueToBookingForm2();

        step("Get initial price");
        String initialPrice = trainBookingFormWeb.getInitialPrice();

        step("Get final price");
        String finalPrice = trainBookingFormWeb.getFinalPrice();

        step("User verify same price");
        Assertion.assertThat("verify that initial and final price are the same", initialPrice, is(finalPrice));
    }

    @Author(names = "Hafizh")
    @Module(name = "SRP")
    @Tribe(name = "Train")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyTrainFilterByNameGuestWeb() {
        Map<String, String> param = getParam("testVerifyTrainFilterByNameGuestWeb");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String currency = param.get("currency");
        String originStation = param.get("originStation");
        String destinationStation = param.get("destinationStation");
        String tripType = param.get("tripType");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);
        String trainName = param.get("trainName");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        TrainSearchPageWeb<?> trainSearchPage = TrainSearchPageWeb.getInstance(session);
        TrainSearchResultPageWeb<?> trainSearchResultPage = TrainSearchResultPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open home page as guest");
        loginPage.loginAsGuest();

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

        step("User open filter & sort chips");
        trainSearchResultPage.clickFilterChips();

        step("User click train name section");
        trainSearchResultPage.selectTrainNameSectionInFilter();

        step("User filter by train name");
        trainSearchResultPage.filterByName(trainName);

        step("User click save button");
        trainSearchResultPage.clickSaveButton();
        Assertion.assertThat("Verify that filter chips is applied", trainSearchResultPage.isFilterChipsApplied(), is(true));

        step("Assert train name shown in SRP is correct");
        Assertion.assertThat("Verify train name in SRP after filter", trainSearchResultPage.verifyName(trainName), is(true));
    }

    @Author(names = "Hafizh")
    @Module(name = "SRP")
    @Tribe(name = "Train")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyTrainFilterByClassGuestWeb() {
        Map<String, String> param = getParam("testVerifyTrainFilterByClassGuestWeb");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String currency = param.get("currency");
        String originStation = param.get("originStation");
        String destinationStation = param.get("destinationStation");
        String tripType = param.get("tripType");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);
        String trainClassParam = param.get("trainClass");
        TrainClass trainClass = new TrainClass(trainClassParam);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        TrainSearchPageWeb<?> trainSearchPage = TrainSearchPageWeb.getInstance(session);
        TrainSearchResultPageWeb<?> trainSearchResultPage = TrainSearchResultPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open home page as guest");
        loginPage.loginAsGuest();

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

        step("User open filter & sort chips");
        trainSearchResultPage.clickFilterChips();

        step("User click train class section");
        trainSearchResultPage.selectClassType();

        step("User filter by train class");
        trainSearchResultPage.filterByClass(trainClass);

        step("User click save button");
        trainSearchResultPage.clickSaveButton();
        Assertion.assertThat("Verify that filter chips is applied", trainSearchResultPage.isFilterChipsApplied(), is(true));

        step("Assert train class shown in SRP is correct");
        Assertion.assertThat("Verify train class in SRP after filter", trainSearchResultPage.verifyClass(trainClass), is(true));
    }

    @Author(names = {"Naufal Hafiz", "Clement"})
    @Module(name = "Complete Booking")
    @Tribe(name = "Train")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyOrderDetailLogin() throws FailedLoginException, InterruptedException, ExecutionException {
        Map<String, String> param = getParam("testVerifyOrderDetailLogin");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String originStation = param.get("originStation");
        String destinationStation = param.get("destinationStation");
        String tripType = param.get("tripType");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);

        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));

        String paymentMethod = param.get("paymentMethod");
        String paymentGroup = param.get("paymentGroup");
        String paymentType = param.get("paymentType");
        int adultCount = Integer.parseInt(param.get("adultCount"));

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        TrainSearchPageWeb<?> trainSearchPage = TrainSearchPageWeb.getInstance(session);
        TrainSearchResultPageWeb<?> trainSearchResultPage = TrainSearchResultPageWeb.getInstance(session);
        TrainBookingFormWeb<?> trainBookingFormWeb = TrainBookingFormWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(username, password, otp, retryCounter);

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

        step("Select train");
        trainSearchResultPage.selectTrain();

        step("Get passenger name");
        String passengerName = trainBookingFormWeb.getPassengerName();

        step("Fill passenger detail");
        trainBookingFormWeb.fillPassengerDetailAdult(adultCount);

        step("Extract departure and arrival");
        String departureTime = trainBookingFormWeb.getDepartureArrivalTime("departure");
        String arrivalTime = trainBookingFormWeb.getDepartureArrivalTime("arrival");

        step("User continue to booking form 2");
        trainBookingFormWeb.continueToBookingForm2();

        step("Proceed to payment page");
        trainBookingFormWeb.proceedToPaymentPage();

        step("Wait for payment page to load");
        paymentPage.isPaymentPageLoaded();

        step("Fetch the price after payment method is applied");
        String finalPrice = paymentPage.getFinalPrice();

        String orderID = paymentPage.getOrderId();
        Assertion.assertThat("Verify order ID is displayed in payment page", !orderID.isEmpty(), is(true));

        if (Configs.ENVIRONMENT == Environment.PROD) {
            step("User select payment method ");
            paymentPage.clickOnSelectPaymentMethod();
            if (Configs.PLATFORM == Platform.DWEB) {
                paymentPage.goToPaymentMethodsGroup(paymentGroup);
                paymentPage.selectPaymentMethod(paymentMethod);
            } else {
                paymentPage.selectPaymentMethodMWeb(paymentMethod, paymentType);
            }

            step("User complete payment");
            paymentPage.clickOnPaymentButton();

            step("User open order list page");
            paymentPage.clickSeeOrderListButton();
            Assertion.assertThat("OrderId is visible on order list page", orderListPage.getLatestOrderId(), equalToIgnoringCase(paymentPage.getOrderIdText()));
        }

        if (Configs.ENVIRONMENT == Environment.GK || Configs.ENVIRONMENT == Environment.PREPROD) {
            step("User select payment method ");
            paymentPage.clickOnSelectPaymentMethod();
            if (Configs.PLATFORM == Platform.DWEB) {
                paymentPage.goToPaymentMethodsGroup(paymentGroup);
                paymentPage.selectPaymentMethod(paymentMethod);
            } else {
                paymentPage.selectPaymentMethodMWeb(paymentMethod, paymentType);
            }

            step("User complete payment");
            paymentPage.clickOnPaymentButton();

            step("User open order detail page");
            orderListPage.openActiveTicketViaURL(orderID);
            orderDetailPage.refreshTheWebpage();
            orderDetailPage.skipCoachmark();

            String orderId = orderDetailPage.getOrderId();

            Assertion.assertThat("Order ID is correct", orderId, containsStringIgnoringCase(orderID));
            Assertion.assertThat("Departure time is correct", orderDetailPage.getDepartureArrivalTimeTrain("departure"), equalToIgnoringCase(departureTime));
            Assertion.assertThat("Arrival time is correct", orderDetailPage.getDepartureArrivalTimeTrain("arrival"), equalToIgnoringCase(arrivalTime));
            Assertion.assertThat("Passenger name is correct", orderDetailPage.getPassengerName(), equalToIgnoringCase(passengerName));
            Assertion.assertThat("Price is correct", orderDetailPage.getFinalPriceOrderDetail(), containsStringIgnoringCase(finalPrice));
        }
    }

    @Author(names = "Hafizh")
    @Module(name = "Booking Form")
    @Tribe(name = "Train")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyBookTrainNonIdrGuestWeb() {
        Map<String, String> param = getParam("testVerifyBookTrainNonIdrGuestWeb");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String currency = param.get("currency");
        String originStation = param.get("originStation");
        String destinationStation = param.get("destinationStation");
        String tripType = param.get("tripType");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);
        String username = param.get("username");
        String phoneNumber = param.get("phoneNumber");
        String email = param.get("email");
        String id = param.get("id");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        TrainSearchPageWeb<?> trainSearchPage = TrainSearchPageWeb.getInstance(session);
        TrainSearchResultPageWeb<?> trainSearchResultPage = TrainSearchResultPageWeb.getInstance(session);
        TrainBookingFormWeb<?> trainBookingFormWeb = TrainBookingFormWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open home page as guest");
        loginPage.loginAsGuest();

        step ("User select USD currency");
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

        step("User select passenger");
        trainSearchPage.selectPassenger(1,0);

        step("User saves the passengers");
        trainSearchPage.savePassengers();

        step("User search train");
        trainSearchPage.searchTrain();

        step("User skip pop-up");
        trainSearchPage.clickSkipPopup();

        step("Assert train price shown in SRP is in IDR");
        Assertion.assertThat("Train price in SRP is in IDR", trainSearchResultPage.isSrpPriceShownIdr(), is(true));

        step("Select train");
        trainSearchResultPage.selectTrain();

        step("Fill out booking form");
        trainBookingFormWeb.fillBookingForm(username, phoneNumber, email, id);
        trainBookingFormWeb.continueToBookingForm2();

        step("Assert train price shown in Booking Form is in IDR");
        Assertion.assertThat("Train price in Booking Form is in IDR", trainBookingFormWeb.isBookingFormPriceShownIdr(), is(true));

        step("Continue to payment page");
        trainBookingFormWeb.proceedToPaymentPage();

        step("Assert train price shown in Payment Page is in IDR");
        Assertion.assertThat("Train price in Payment Page is in IDR", paymentPage.isPriceCurrencyInIDR(), is(true));
    }

    @Author(names = "Hafizh")
    @Module(name = "SRP")
    @Tribe(name = "Train")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyTrainRecentlySearchedLoginWeb() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testVerifyTrainRecentlySearchedLoginWeb");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String currency = param.get("currency");
        String originStation = param.get("originStation");
        String destinationStation = param.get("destinationStation");
        String tripType = param.get("tripType");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        TrainSearchPageWeb<?> trainSearchPage = TrainSearchPageWeb.getInstance(session);
        TrainSearchResultPageWeb<?> trainSearchResultPage = TrainSearchResultPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

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

        step("User verify Train List is present on SRP");
        Assertion.assertThat("Verify that Train List is present on SRP after Search", trainSearchResultPage.checkTrainCard(), is(true));

        step("User verify Recently Searched Train is present");
        Assertion.assertThat("verify that Recently Searched Train is present", trainSearchResultPage.goBackToLandingTrainPage(), is(true));
    }

    @Author(names = "Hafizh")
    @Module(name = "Train")
    @Tribe(name = "NFT")
    @Vertical(name = "NFT")
    @Test
    public void Train_testVerifyTrainFilterByClassLoginWeb() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("Train_testVerifyTrainFilterByClassLoginWeb");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String originStation = param.get("originStation");
        String destinationStation = param.get("destinationStation");
        String tripType = param.get("tripType");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);
        String trainClassParam = param.get("trainClass");
        TrainClass trainClass = new TrainClass(trainClassParam);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        TrainSearchPageWeb<?> trainSearchPage = TrainSearchPageWeb.getInstance(session);
        TrainSearchResultPageWeb<?> trainSearchResultPage = TrainSearchResultPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User select English language");
        homePage.selectEnglishLanguage();

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

        step("User open filter & sort chips");
        trainSearchResultPage.clickFilterChips();

        step("User click train class section");
        trainSearchResultPage.selectClassType();

        step("User filter by train class");
        trainSearchResultPage.filterByClass(trainClass);

        step("User click save button");
        trainSearchResultPage.clickSaveButton();
        Assertion.assertThat("Verify that filter chips is applied", trainSearchResultPage.isFilterChipsApplied(), is(true));

        step("Assert train class shown in SRP is correct");
        Assertion.assertThat("Verify train class in SRP after filter", trainSearchResultPage.verifyClass(trainClass), is(true));
    }

    @Author(names = "Hafizh")
    @Module(name = "SRP")
    @Tribe(name = "Train")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyTrainFilterByNameLoginWeb() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testVerifyTrainFilterByNameLoginWeb");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String originStation = param.get("originStation");
        String destinationStation = param.get("destinationStation");
        String tripType = param.get("tripType");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);
        String trainName = param.get("trainName");

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        TrainSearchPageWeb<?> trainSearchPage = TrainSearchPageWeb.getInstance(session);
        TrainSearchResultPageWeb<?> trainSearchResultPage = TrainSearchResultPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

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

        step("User open filter & sort chips");
        trainSearchResultPage.clickFilterChips();

        step("User click train name section");
        trainSearchResultPage.selectTrainNameSectionInFilter();

        step("User filter by train name");
        trainSearchResultPage.filterByName(trainName);

        step("User click save button");
        trainSearchResultPage.clickSaveButton();
        Assertion.assertThat("Verify that filter chips is applied", trainSearchResultPage.isFilterChipsApplied(), is(true));

        step("Assert train name shown in SRP is correct");
        Assertion.assertThat("Verify train name in SRP after filter", trainSearchResultPage.verifyName(trainName), is(true));
    }

    @Author(names = "Hafizh")
    @Module(name = "Complete Booking")
    @Tribe(name = "Train")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyTrainCompleteBookingOnewayGuestWeb() {
        Map<String, String> param = getParam("testVerifyTrainCompleteBookingOnewayGuestWeb");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String currency = param.get("currency");
        String originStation = param.get("originStation");
        String destinationStation = param.get("destinationStation");
        String tripType = param.get("tripType");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);
        String queryParamInfant = param.get("queryParamInfant");
        String newValueInfant = param.get("infant");

        String name = CoreFunctions.randomNameWithoutTest();
        String phoneNumber = CoreFunctions.randomPhone();
        String email = CoreFunctions.randomEmail();
        String id = String.valueOf(CoreFunctions.generateRandomInt(16));

        String infantName = CoreFunctions.randomNameWithoutTest();
        String infantId = String.valueOf(CoreFunctions.generateRandomInt(16));

        String paymentMethod = param.get("paymentMethod");
        String paymentGroup = param.get("paymentGroup");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        TrainSearchPageWeb<?> trainSearchPage = TrainSearchPageWeb.getInstance(session);
        TrainSearchResultPageWeb<?> trainSearchResultPage = TrainSearchResultPageWeb.getInstance(session);
        TrainBookingFormWeb<?> trainBookingFormWeb = TrainBookingFormWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as guest user");
        loginPage.loginAsGuest();

        step("User select IDR currency");
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
        trainSearchResultPage.editInfantPaxInSrp(queryParamInfant, newValueInfant);

        step("Select train");
        trainSearchResultPage.selectTrain();

        step("Fill out booking form");
        trainBookingFormWeb.fillBookingForm(name, phoneNumber, email, id);

        step("Fill passenger detail for infant");
        trainBookingFormWeb.fillPassengerDetailInfant(infantName, infantId);

        step("User continue to booking form 2");
        trainBookingFormWeb.continueToBookingForm2();

        step("Proceed to payment page");
        trainBookingFormWeb.proceedToPaymentPage();

        String orderID = paymentPage.getOrderId();
        Assertion.assertThat("Verify order ID is displayed in payment page", !orderID.isEmpty(), is(true));

        step("User select payment method ");
        paymentPage.clickOnSelectPaymentMethod();
        paymentPage.goToPaymentMethodsGroup(paymentGroup);
        paymentPage.selectPaymentMethod(paymentMethod);

        step("User complete payment");
        paymentPage.clickOnPaymentButton();

        String orderIdOnPaymentConfirmation = paymentPage.getOrderId();

        step("Assert order id from payment page and payment confirmation page matches");
        Assertion.assertThat("Order ID on payment page: " +  orderID + " and Order ID on payment confirmation page: "
                + orderIdOnPaymentConfirmation + " matches", orderID.equals(orderIdOnPaymentConfirmation), is(true));

        step("Assert user is on payment confirmation page");
        Assertion.assertThat("User can see button to check payment status and see order list", paymentPage.isUserOnPaymentConfirmationPage(), is(true));

        step("Assert VA number shown only contains digits");
        Assertion.assertThat("VA number shown only contains digits", paymentPage.isVaNumberDigitsOnly(), is(true));
    }

    @Author(names = {"Vipul Kumar", "Clement"})
    @Module(name = "CompleteBooking")
    @Tribe(name = "Train")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyBookTrainNonIdrUsingDeeplinkLoginWeb() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testVerifyBookTrainNonIdrUsingDeeplinkLoginWeb");
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

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        TrainSearchPageWeb<?> trainSearchPage = TrainSearchPageWeb.getInstance(session);
        TrainSearchResultPageWeb<?> trainSearchResultPage = TrainSearchResultPageWeb.getInstance(session);
        TrainBookingFormWeb<?> trainBookingFormWeb = TrainBookingFormWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);
        CalendarPageWeb<?> calendarPage = CalendarPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User change to " + currency + " currency");
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
        trainSearchPage.clickOnCalendarWidgetToOpen();
        calendarPage.selectDate(departureDate);

        step("User select passenger");
        trainSearchPage.selectPassenger(adultCount,infantCount);

        step("User saves the passengers");
        trainSearchPage.savePassengers();

        step("User search train");
        trainSearchPage.searchTrain();

        step("User skip pop-up");
        trainSearchPage.clickSkipPopup();

        step("Assert train price shown in SRP is in IDR");
        Assertion.assertThat("Train price in SRP is in IDR", trainSearchResultPage.isSrpPriceShownIdr(), is(true));

        step("User gets the price of first Train");
        String price = trainSearchResultPage.getPrice();

        step("Select train");
        trainSearchResultPage.selectTrain();

        if (Configs.PLATFORM == Platform.DWEB){
            step("User enter contact details");
            trainBookingFormWeb.fillContactDetails(fullName, phoneNumber, emailID);
        }

        step("User fill adult details");
        trainBookingFormWeb.fillPassengerDetailAdult(adultCount);

        step("User fill infant details");
        trainBookingFormWeb.fillPassengerDetailInfant(adultCount, infantCount);

        step("User continue to booking form 2");
        trainBookingFormWeb.continueToBookingForm2();

        step("User clicks Total price details button");
        trainBookingFormWeb.checkPriceBtn();

        step("User verifies whether price details page opens or not");
        Assertion.assertThat("Verifies whether price details page appears or not", trainBookingFormWeb.isPriceDetailsDisplayed(), is(true));

        step("User verifies the price details on booking page");
        Assertion.assertThat("Verifies the price on booking page", trainBookingFormWeb.checkPrice(adultCount), is(price));
        trainBookingFormWeb.closePricePopUp();

        step("User adds insurance");
        trainBookingFormWeb.addInsurance();

        step("User continues for payment");
        trainBookingFormWeb.proceedToPaymentPage();

        step("User Go to Payment Page");
        trainBookingFormWeb.goToPaymentPage();
        String orderID = paymentPage.getOrderId();
        Assertion.assertThat("Verify order ID is displayed in payment page", !orderID.isEmpty(), is(true));

        step("Train price shown on Payment Page must be in IDR");
        Assertion.assertThat("Train price on Payment Page must be in IDR", paymentPage.isPriceCurrencyInIDR(), is(true));
    }

    @Author(names = {"Vipul Kumar", "Clement"})
    @Module(name = "Train")
    @Tribe(name = "NFT")
    @Vertical(name = "NFT")
    @Test
    public void Train_testVerifyBookTrainForRoundTripWithMultiplePassengersLoggedInWeb() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("Train_testVerifyBookTrainForRoundTripWithMultiplePassengersLoggedInWeb");
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
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        TrainSearchPageWeb<?> trainSearchPage = TrainSearchPageWeb.getInstance(session);
        TrainSearchResultPageWeb<?> trainSearchResultPage = TrainSearchResultPageWeb.getInstance(session);
        TrainBookingFormWeb<?> trainBookingFormWeb = TrainBookingFormWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);
        CalendarPageWeb<?> calendarPage = CalendarPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("Login as " + email + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(email, password);

        step("User select English language");
        homePage.selectEnglishLanguage();

        step("User open train vertical");
        homePage.openVerticalPageViaURL("Train");

        step("User skip pop-up");
        trainSearchPage.clickSkipPopup();

        step("User select origin station");
        trainSearchPage.selectOrigin(originStation);

        step("User select destination station");
        trainSearchPage.selectDestination(destinationStation);

        step("User toggle " + tripType + " way");
        trainSearchPage.toggleTripType(tripType);

        step("User select departure date");
        trainSearchPage.clickOnCalendarWidgetToOpen();
        calendarPage.selectDate(departureDate, returnDate);

        step("User select passenger");
        trainSearchPage.selectPassenger(adultCount,infantCount);

        step("User saves the passengers");
        trainSearchPage.savePassengers();

        step("User search train");
        trainSearchPage.searchTrain();

        step("User skip pop-up");
        trainSearchPage.clickSkipPopup();

        step("Assert train price shown in SRP is in IDR");
        Assertion.assertThat("Train price in SRP is in IDR", trainSearchResultPage.isSrpPriceShownIdr(), is(true));

        step("user Select Departure train");
        trainSearchResultPage.selectTrain();
        trainSearchResultPage.checkReturn();

        step("User Select Return train");
        trainSearchResultPage.selectTrain();

        step("User fill adult details");
        trainBookingFormWeb.fillPassengerDetailAdult(adultCount);

        step("User fill infant details");
        trainBookingFormWeb.fillPassengerDetailInfant(adultCount, infantCount);

        step("User verifies departure origin station");
        Assertion.assertThat("verifies departure origin station", trainBookingFormWeb.verifyDepartureOrigin(), is(originStation));

        step("User verifies departure destination station");
        Assertion.assertThat("verifies departure destination station", trainBookingFormWeb.verifyDepartureDestination(), is(destinationStation));

        step("User verifies return origin station");
        Assertion.assertThat("verifies return origin station", trainBookingFormWeb.verifyReturnOrigin(), is(destinationStation));

        step("User verifies return destination station");
        Assertion.assertThat("verifies return destination station", trainBookingFormWeb.verifyReturnDestination(), is(originStation));

        step("User continues booking after filling details");
        trainBookingFormWeb.continueToBookingForm2();

        step("User clicks Total price details button");
        trainBookingFormWeb.checkPriceBtn();

        step("User verifies whether price details page opens or not");
        Assertion.assertThat("Verifies whether price details page appears or not", trainBookingFormWeb.isPriceDetailsDisplayed(), is(true));
        trainBookingFormWeb.closePricePopUp();

        step("User adds insurance");
        trainBookingFormWeb.addNotAutoSelectedInsurance();

        step("User continues for payment");
        trainBookingFormWeb.continueToPay();

        step("User Go to Payment Page");
        trainBookingFormWeb.goToPaymentPage();
        String orderID = paymentPage.getOrderId();
        
        Assertion.assertThat("Verify order ID is displayed in payment page", !orderID.isEmpty(), is(true));
        Assertion.assertThat("Insurance is applied", paymentPage.isInsuranceApplied(), is(true));
    }

    @Author(names = {"Vipul Kumar", "Clement"})
    @Module(name = "CompleteBooking")
    @Tribe(name = "Train")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyBookTrainForRoundTripWithMultiplePassengersGuestWeb() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testVerifyBookTrainForRoundTripWithMultiplePassengersGuestWeb");
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

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        TrainSearchPageWeb<?> trainSearchPage = TrainSearchPageWeb.getInstance(session);
        TrainSearchResultPageWeb<?> trainSearchResultPage = TrainSearchResultPageWeb.getInstance(session);
        TrainBookingFormWeb<?> trainBookingFormWeb = TrainBookingFormWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);
        CalendarPageWeb<?> calendarPage = CalendarPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open home page as guest");
        loginPage.loginAsGuest();

        step("User open train vertical");
        homePage.openVerticalPageViaURL("Train");

        step("User skip pop-up");
        trainSearchPage.clickSkipPopup();

        step("User select origin station");
        trainSearchPage.selectOrigin(originStation);

        step("User select destination station");
        trainSearchPage.selectDestination(destinationStation);

        step("User toggle " + tripType + " way");
        trainSearchPage.toggleTripType(tripType);

        step("User select dates");
        trainSearchPage.clickOnCalendarWidgetToOpen();
        calendarPage.selectDate(departureDate, returnDate);

        step("User select passenger");
        trainSearchPage.selectPassenger(adultCount,infantCount);

        step("User saves the passengers");
        trainSearchPage.savePassengers();

        step("User search train");
        trainSearchPage.searchTrain();

        step("User skip pop-up");
        trainSearchPage.clickSkipPopup();

        step("Assert train price shown in SRP is in IDR");
        Assertion.assertThat("Train price in SRP is in IDR", trainSearchResultPage.isSrpPriceShownIdr(), is(true));

        step("User gets the price of first Train");
        String price = trainSearchResultPage.getPrice();

        step("user Select Departure train");
        trainSearchResultPage.selectTrain();
        trainSearchResultPage.checkReturn();

        step("User Select Return train");
        trainSearchResultPage.selectTrain();

        step("User enter contact details");
        trainBookingFormWeb.fillContactDetails(fullName, phoneNumber, emailID);

        step("User fill adult details");
        trainBookingFormWeb.fillPassengerDetailAdult(adultCount);

        step("User fill infant details");
        trainBookingFormWeb.fillPassengerDetailInfant(adultCount, infantCount);

        step("User verifies departure origin station");
        Assertion.assertThat("verifies departure origin station", trainBookingFormWeb.verifyDepartureOrigin(), is(originStation));

        step("User verifies departure destination station");
        Assertion.assertThat("verifies departure destination station", trainBookingFormWeb.verifyDepartureDestination(), is(destinationStation));

        step("User verifies return origin station");
        Assertion.assertThat("verifies return origin station", trainBookingFormWeb.verifyReturnOrigin(), is(destinationStation));

        step("User verifies return destination station");
        Assertion.assertThat("verifies return destination station", trainBookingFormWeb.verifyReturnDestination(), is(originStation));

        step("User continues booking after filling details");
        trainBookingFormWeb.continueToBookingForm2();

        step("User clicks Total price details button");
        trainBookingFormWeb.checkPriceBtn();

        step("User verifies whether price details page opens or not");
        Assertion.assertThat("Verifies whether price details page appears or not", trainBookingFormWeb.isPriceDetailsDisplayed(), is(true));

        step("User verifies the price details on booking page");
        Assertion.assertThat("Verifies the price on booking page", trainBookingFormWeb.checkPrice(adultCount), is(price));
        trainBookingFormWeb.closePricePopUp();

        step("User adds insurance");
        trainBookingFormWeb.addNotAutoSelectedInsurance();

        step("User continues for payment");
        trainBookingFormWeb.proceedToPaymentPage();

        step("User proceed to payment page");
        trainBookingFormWeb.goToPaymentPage();
        String orderID = paymentPage.getOrderId();
        Assertion.assertThat("Verify order ID is displayed in payment page", !orderID.isEmpty(), is(true));
        Assertion.assertThat("Insurance is applied", paymentPage.isInsuranceApplied(), is(true));
    }

    @Author(names = {"Okta", "Clement"})
    @Module(name = "CompleteBooking")
    @Tribe(name = "Train")
    @Vertical(name = "NFT")
    @Test
    public void testTrainCompleteBookingRoundTripGuestWeb() {
        Map<String, String> param = getParam("testTrainCompleteBookingRoundTripGuestWeb");
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
        String phoneNumber = "62" + CoreFunctions.randomInt(8);
        String randomEmail = CoreFunctions.randomEmail();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        TrainSearchPageWeb<?> trainSearchPage = TrainSearchPageWeb.getInstance(session);
        TrainSearchResultPageWeb<?> trainSearchResultPage = TrainSearchResultPageWeb.getInstance(session);
        TrainBookingFormWeb<?> trainBookingFormWeb = TrainBookingFormWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);
        CalendarPageWeb<?> calendarPage = CalendarPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open home page as guest");
        loginPage.loginAsGuest();

        step("User open train vertical");
        homePage.openVerticalPageViaURL("Train");

        step("User skip pop-up");
        trainSearchPage.clickSkipPopup();

        step("User select origin station");
        trainSearchPage.selectOrigin(originStation);

        step("User select destination station");
        trainSearchPage.selectDestination(destinationStation);

        step("User toggle " + tripType);
        trainSearchPage.toggleTripType(tripType);

        step("User select dates");
        trainSearchPage.clickOnCalendarWidgetToOpen();
        calendarPage.selectDate(departureDate, returnDate);

        step("User select passenger");
        trainSearchPage.selectPassenger(adultCount, infantCount);

        step("User saves the passengers");
        trainSearchPage.savePassengers();

        step("User search train");
        trainSearchPage.searchTrain();

        step("User skip pop-up");
        trainSearchPage.clickSkipPopup();

        step("User gets the price of first train");
        String price = trainSearchResultPage.getPrice();

        step("User select departure train");
        trainSearchResultPage.selectTrain();

        if (Configs.PLATFORM == Platform.DWEB){
            step("User verifies whether they can select return train or not");
            Assertion.assertThat("Verifies whether user can select return train or not", trainSearchResultPage.checkReturn(), is(true));
        }

        step("User select return train");
        trainSearchResultPage.selectTrain();

        step("User enter contact details");
        trainBookingFormWeb.fillContactDetails(fullName, phoneNumber, randomEmail);

        step("User fill adult details");
        trainBookingFormWeb.fillPassengerDetailAdult(adultCount);

        step("User fill infant details");
        trainBookingFormWeb.fillPassengerDetailInfant(adultCount, infantCount);

        step("User verifies departure origin station");
        Assertion.assertThat("Verifies departure origin station", trainBookingFormWeb.verifyDepartureOrigin(), is(originStation));

        step("User verifies departure destination station");
        Assertion.assertThat("Verifies departure destination station", trainBookingFormWeb.verifyDepartureDestination(), is(destinationStation));

        step("User verifies return origin station");
        Assertion.assertThat("Verifies return origin station", trainBookingFormWeb.verifyReturnOrigin(), is(destinationStation));

        step("User verifies return destination station");
        Assertion.assertThat("Verifies return destination station", trainBookingFormWeb.verifyReturnDestination(), is(originStation));

        step("User continue to booking form 2");
        trainBookingFormWeb.continueToBookingForm2();

        step("User clicks total price details button");
        trainBookingFormWeb.checkPriceBtn();

        step("User verifies whether price details page opens or not");
        Assertion.assertThat("Verifies whether price details page appears or not", trainBookingFormWeb.isPriceDetailsDisplayed(), is(true));

        step("User verifies the price details on booking page");
        Assertion.assertThat("Verifies the price on booking page", trainBookingFormWeb.checkPrice(adultCount), is(price));
        trainBookingFormWeb.closePricePopUp();

        step("User select seat");
        trainBookingFormWeb.saveSeat();

        step("User continues for payment");
        trainBookingFormWeb.proceedToPaymentPage();

        step("User proceed to payment page");
        trainBookingFormWeb.goToPaymentPage();
        String orderID = paymentPage.getOrderId();
        Assertion.assertThat("Verify order ID is displayed in payment page", !orderID.isEmpty(), is(true));
    }

    @Author(names = {"Okta", "Clement"})
    @Module(name = "CompleteBooking")
    @Tribe(name = "Train")
    @Vertical(name = "NFT")
    @Test
    public void testTrainCompleteBookingRoundTripLoginWeb() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testTrainCompleteBookingRoundTripLoginWeb");
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
        String phoneNumber = "62" + CoreFunctions.randomInt(8);
        String randomEmail = CoreFunctions.randomEmail();

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        TrainSearchPageWeb<?> trainSearchPage = TrainSearchPageWeb.getInstance(session);
        TrainSearchResultPageWeb<?> trainSearchResultPage = TrainSearchResultPageWeb.getInstance(session);
        TrainBookingFormWeb<?> trainBookingFormWeb = TrainBookingFormWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);
        CalendarPageWeb<?> calendarPage = CalendarPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User open train vertical");
        homePage.openVerticalPageViaURL("Train");

        step("User skip pop-up");
        trainSearchPage.clickSkipPopup();

        step("User select origin station");
        trainSearchPage.selectOrigin(originStation);

        step("User select destination station");
        trainSearchPage.selectDestination(destinationStation);

        step("User toggle " + tripType);
        trainSearchPage.toggleTripType(tripType);

        step("User select dates");
        trainSearchPage.clickOnCalendarWidgetToOpen();
        calendarPage.selectDate(departureDate, returnDate);

        step("User select passenger");
        trainSearchPage.selectPassenger(adultCount, infantCount);

        step("User saves the passengers");
        trainSearchPage.savePassengers();

        step("User search train");
        trainSearchPage.searchTrain();

        step("User skip pop-up");
        trainSearchPage.clickSkipPopup();

        step("User gets the price of first train");
        String price = trainSearchResultPage.getPrice();

        step("User select departure train");
        trainSearchResultPage.selectTrain();

        if (Configs.PLATFORM == Platform.DWEB){
            step("User verifies whether they can select return train or not");
            Assertion.assertThat("Verifies whether user can select return train or not", trainSearchResultPage.checkReturn(), is(true));
        }

        step("User select return train");
        trainSearchResultPage.selectTrain();

        step("User fill adult details");
        trainBookingFormWeb.fillPassengerDetailAdult(adultCount);

        step("User fill infant details");
        trainBookingFormWeb.fillPassengerDetailInfant(adultCount, infantCount);

        step("User verifies departure origin station");
        Assertion.assertThat("Verifies departure origin station", trainBookingFormWeb.verifyDepartureOrigin(), is(originStation));

        step("User verifies departure destination station");
        Assertion.assertThat("Verifies departure destination station", trainBookingFormWeb.verifyDepartureDestination(), is(destinationStation));

        step("User verifies return origin station");
        Assertion.assertThat("Verifies return origin station", trainBookingFormWeb.verifyReturnOrigin(), is(destinationStation));

        step("User verifies return destination station");
        Assertion.assertThat("Verifies return destination station", trainBookingFormWeb.verifyReturnDestination(), is(originStation));

        step("User continue to booking form 2");
        trainBookingFormWeb.continueToBookingForm2();

        step("User clicks total price details button");
        trainBookingFormWeb.checkPriceBtn();

        step("User verifies whether price details page opens or not");
        Assertion.assertThat("Verifies whether price details page appears or not", trainBookingFormWeb.isPriceDetailsDisplayed(), is(true));

        step("User verifies the price details on booking page");
        Assertion.assertThat("Verifies the price on booking page", trainBookingFormWeb.checkPrice(adultCount), is(price));
        trainBookingFormWeb.closePricePopUp();

        step("User select seat");
        trainBookingFormWeb.saveSeat();

        step("User continues for payment");
        trainBookingFormWeb.proceedToPaymentPage();

        step("User proceed to payment page");
        trainBookingFormWeb.goToPaymentPage();
        String orderID = paymentPage.getOrderId();
        Assertion.assertThat("Verify order ID is displayed in payment page", !orderID.isEmpty(), is(true));
    }

    @Author(names = {"Okta", "Clement"})
    @Module(name = "CompleteBooking")
    @Tribe(name = "Train")
    @Vertical(name = "NFT")
    @Test
    public void testTrainCompleteBookingWithInsuranceOnewayGuestWeb() {
        Map<String, String> param = getParam("testTrainCompleteBookingWithInsuranceOnewayGuestWeb");
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
        String phoneNumber = "62" + CoreFunctions.randomInt(8);
        String randomEmail = CoreFunctions.randomEmail();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        TrainSearchPageWeb<?> trainSearchPage = TrainSearchPageWeb.getInstance(session);
        TrainSearchResultPageWeb<?> trainSearchResultPage = TrainSearchResultPageWeb.getInstance(session);
        TrainBookingFormWeb<?> trainBookingFormWeb = TrainBookingFormWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);
        CalendarPageWeb<?> calendarPage = CalendarPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open home page as guest");
        loginPage.loginAsGuest();

        step("User open train vertical");
        homePage.openVerticalPageViaURL("Train");

        step("User skip pop-up");
        trainSearchPage.clickSkipPopup();

        step("User select origin station");
        trainSearchPage.selectOrigin(originStation);

        step("User select destination station");
        trainSearchPage.selectDestination(destinationStation);

        step("User toggle " + tripType);
        trainSearchPage.toggleTripType(tripType);

        step("User select departure date");
        trainSearchPage.clickOnCalendarWidgetToOpen();
        calendarPage.selectDate(departureDate);

        step("User select passenger");
        trainSearchPage.selectPassenger(adultCount, infantCount);

        step("User saves the passengers");
        trainSearchPage.savePassengers();

        step("User search train");
        trainSearchPage.searchTrain();

        step("User skip pop-up");
        trainSearchPage.clickSkipPopup();

        step("User gets the price of first train");
        String price = trainSearchResultPage.getPrice();

        step("User select train");
        trainSearchResultPage.selectTrain();

        step("User enter contact details");
        trainBookingFormWeb.fillContactDetails(fullName, phoneNumber, randomEmail);

        step("User fill adult details");
        trainBookingFormWeb.fillPassengerDetailAdult(adultCount);

        step("User fill infant details");
        trainBookingFormWeb.fillPassengerDetailInfant(adultCount, infantCount);

        step("User verifies origin station");
        Assertion.assertThat("Verifies origin station", trainBookingFormWeb.verifyDepartureOrigin(), is(originStation));

        step("User verifies destination station");
        Assertion.assertThat("Verifies destination station", trainBookingFormWeb.verifyDepartureDestination(), is(destinationStation));

        step("User continues booking after filling details");
        trainBookingFormWeb.continueToBookingForm2();

        step("User clicks total price details button");
        trainBookingFormWeb.checkPriceBtn();

        step("User verifies whether price details page opens or not");
        Assertion.assertThat("Verifies whether price details page appears or not", trainBookingFormWeb.isPriceDetailsDisplayed(), is(true));

        step("User verifies the price details on booking page");
        Assertion.assertThat("Verifies the price on booking page", trainBookingFormWeb.checkPrice(adultCount), is(price));
        trainBookingFormWeb.closePricePopUp();

        step("User adds insurance");
        trainBookingFormWeb.addNotAutoSelectedInsurance();

        step("User continues for payment");
        trainBookingFormWeb.proceedToPaymentPage();

        step("User proceed to payment page");
        trainBookingFormWeb.goToPaymentPage();
        String orderID = paymentPage.getOrderId();
        Assertion.assertThat("Verify order ID is displayed in payment page", !orderID.isEmpty(), is(true));
        Assertion.assertThat("Insurance is applied", paymentPage.isInsuranceApplied(), is(true));
    }

    @Author(names = {"Okta", "Clement"})
    @Module(name = "CompleteBooking")
    @Tribe(name = "Train")
    @Vertical(name = "NFT")
    @Test
    public void testTrainCompleteBookingWithInsuranceOnewayLoginWeb() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testTrainCompleteBookingWithInsuranceOnewayLoginWeb");
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

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        TrainSearchPageWeb<?> trainSearchPage = TrainSearchPageWeb.getInstance(session);
        TrainSearchResultPageWeb<?> trainSearchResultPage = TrainSearchResultPageWeb.getInstance(session);
        TrainBookingFormWeb<?> trainBookingFormWeb = TrainBookingFormWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);
        CalendarPageWeb<?> calendarPage = CalendarPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User open train vertical");
        homePage.openVerticalPageViaURL("Train");

        step("User skip pop-up");
        trainSearchPage.clickSkipPopup();

        step("User select origin station");
        trainSearchPage.selectOrigin(originStation);

        step("User select destination station");
        trainSearchPage.selectDestination(destinationStation);

        step("User toggle " + tripType);
        trainSearchPage.toggleTripType(tripType);

        step("User select departure date");
        trainSearchPage.clickOnCalendarWidgetToOpen();
        calendarPage.selectDate(departureDate);

        step("User select passenger");
        trainSearchPage.selectPassenger(adultCount, infantCount);

        step("User saves the passengers");
        trainSearchPage.savePassengers();

        step("User search train");
        trainSearchPage.searchTrain();

        step("User skip pop-up");
        trainSearchPage.clickSkipPopup();

        step("User gets the price of first train");
        String price = trainSearchResultPage.getPrice();

        step("User select train");
        trainSearchResultPage.selectTrain();

        step("User fill adult details");
        trainBookingFormWeb.fillPassengerDetailAdult(adultCount);

        step("User fill infant details");
        trainBookingFormWeb.fillPassengerDetailInfant(adultCount, infantCount);

        step("User verifies origin station");
        Assertion.assertThat("Verifies origin station", trainBookingFormWeb.verifyDepartureOrigin(), is(originStation));

        step("User verifies destination station");
        Assertion.assertThat("Verifies destination station", trainBookingFormWeb.verifyDepartureDestination(), is(destinationStation));

        step("User continues booking after filling details");
        trainBookingFormWeb.continueToBookingForm2();

        step("User clicks total price details button");
        trainBookingFormWeb.checkPriceBtn();

        step("User verifies whether price details page opens or not");
        Assertion.assertThat("Verifies whether price details page appears or not", trainBookingFormWeb.isPriceDetailsDisplayed(), is(true));

        step("User verifies the price details on booking page");
        Assertion.assertThat("Verifies the price on booking page", trainBookingFormWeb.checkPrice(adultCount), is(price));
        trainBookingFormWeb.closePricePopUp();

        step("User adds insurance");
        trainBookingFormWeb.addNotAutoSelectedInsurance();

        step("User continues for payment");
        trainBookingFormWeb.proceedToPaymentPage();

        step("User proceed to payment page");
        trainBookingFormWeb.goToPaymentPage();
        String orderID = paymentPage.getOrderId();
        Assertion.assertThat("Verify order ID is displayed in payment page", !orderID.isEmpty(), is(true));
        Assertion.assertThat("Insurance is applied", paymentPage.isInsuranceApplied(), is(true));
    }

    @Author(names = {"Vipul Kumar", "Clement"})
    @Module(name = "CompleteBooking")
    @Tribe(name = "Train")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyBookTrainWithSingleAdultAndInfantOneWay() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testVerifyBookTrainWithSingleAdultAndInfantOneWay");
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
        String paymentMethod = param.get("paymentMethod");
        String paymentGroup = param.get("paymentGroup");

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        TrainSearchPageWeb<?> trainSearchPage = TrainSearchPageWeb.getInstance(session);
        TrainSearchResultPageWeb<?> trainSearchResultPage = TrainSearchResultPageWeb.getInstance(session);
        TrainBookingFormWeb<?> trainBookingFormWeb = TrainBookingFormWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User change to " + currency + " currency");
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

        step("User select passenger");
        trainSearchPage.selectPassenger(adultCount,infantCount);

        step("User saves the passengers");
        trainSearchPage.savePassengers();

        step("User search train");
        trainSearchPage.searchTrain();

        step("User skip pop-up");
        trainSearchPage.clickSkipPopup();

        step("Assert train price shown in SRP is in IDR");
        Assertion.assertThat("Train price in SRP is in IDR", trainSearchResultPage.isSrpPriceShownIdr(), is(true));

        step("User gets the price of first Train");
        String price = trainSearchResultPage.getPrice();

        step("Select train");
        trainSearchResultPage.selectTrain();

        step("User fill adult details");
        trainBookingFormWeb.fillPassengerDetailAdult(adultCount);

        step("User fill infant details");
        trainBookingFormWeb.fillPassengerDetailInfant(adultCount, infantCount);

        step("User verifies departure origin station");
        Assertion.assertThat("verifies departure origin station", trainBookingFormWeb.verifyDepartureOrigin(), is(originStation));

        step("User verifies departure destination station");
        Assertion.assertThat("verifies departure destination station", trainBookingFormWeb.verifyDepartureDestination(), is(destinationStation));

        step("User continues booking after filling details");
        trainBookingFormWeb.continueToBookingForm2();

        step("User clicks Total price details button");
        trainBookingFormWeb.checkPriceBtn();

        step("User verifies whether price details page opens or not");
        Assertion.assertThat("Verifies whether price details page appears or not", trainBookingFormWeb.isPriceDetailsDisplayed(), is(true));

        step("User verifies the price details on booking page");
        Assertion.assertThat("Verifies the price on booking page", trainBookingFormWeb.checkPrice(adultCount), is(price));
        trainBookingFormWeb.closePricePopUp();

        step("User adds insurance");
        trainBookingFormWeb.addInsurance();

        step("User continues for payment");
        trainBookingFormWeb.proceedToPaymentPage();

        step("User Go to Payment Page");
        trainBookingFormWeb.goToPaymentPage();
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

        Assertion.assertThat("Order ID is shown correctly", orderDetailPage.getOrderId(), containsString(orderID));
    }

    @Author(names = {"Vipul Kumar", "Clement"})
    @Module(name = "CompleteBooking")
    @Tribe(name = "Train")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyBarCodeAfterBookTrainOneWay() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testVerifyBarCodeAfterBookTrainOneWay");
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
        String paymentMethod = param.get("paymentMethod");
        String paymentGroup = param.get("paymentGroup");

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        TrainSearchPageWeb<?> trainSearchPage = TrainSearchPageWeb.getInstance(session);
        TrainSearchResultPageWeb<?> trainSearchResultPage = TrainSearchResultPageWeb.getInstance(session);
        TrainBookingFormWeb<?> trainBookingFormWeb = TrainBookingFormWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        CalendarPageWeb<?> calendarPage = CalendarPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

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
        trainSearchPage.clickOnCalendarWidgetToOpen();
        calendarPage.selectDate(departureDate);

        step("User select passenger");
        trainSearchPage.selectPassenger(adultCount,infantCount);

        step("User saves the passengers");
        trainSearchPage.savePassengers();

        step("User search train");
        trainSearchPage.searchTrain();

        step("User skip pop-up");
        trainSearchPage.clickSkipPopup();

        step("Assert train price shown in SRP is in IDR");
        Assertion.assertThat("Train price in SRP is in IDR", trainSearchResultPage.isSrpPriceShownIdr(), is(true));

        step("User gets the price of first Train");
        String price = trainSearchResultPage.getPrice();

        step("Select train");
        trainSearchResultPage.selectTrain();

        step("User fill adult details");
        trainBookingFormWeb.fillPassengerDetailAdult(adultCount);

        step("User fill infant details");
        trainBookingFormWeb.fillPassengerDetailInfant(adultCount, infantCount);

        step("User verifies departure origin station");
        Assertion.assertThat("verifies departure origin station", trainBookingFormWeb.verifyDepartureOrigin(), is(originStation));

        step("User verifies departure destination station");
        Assertion.assertThat("verifies departure destination station", trainBookingFormWeb.verifyDepartureDestination(), is(destinationStation));

        step("User continues booking after filling details");
        trainBookingFormWeb.continueToBookingForm2();

        step("User clicks Total price details button");
        trainBookingFormWeb.checkPriceBtn();

        step("User verifies whether price details page opens or not");
        Assertion.assertThat("Verifies whether price details page appears or not", trainBookingFormWeb.isPriceDetailsDisplayed(), is(true));

        step("User verifies the price details on booking page");
        Assertion.assertThat("Verifies the price on booking page", trainBookingFormWeb.checkPrice(adultCount), is(price));
        trainBookingFormWeb.closePricePopUp();

        step("User adds insurance");
        trainBookingFormWeb.addInsurance();

        step("User continues for payment");
        trainBookingFormWeb.proceedToPaymentPage();

        step("User Go to Payment Page");
        trainBookingFormWeb.goToPaymentPage();
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

        step("User can see bar code clearly");
        Assertion.assertThat("Verify Bar Code is present or not", orderDetailPage.trainBarCode(), is(true));
    }

    @Author(names = "Hafizh")
    @Module(name = "HomePageWeb")
    @Tribe(name = "Train")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyTrainIsStillDisplayedNonIDRLoginWeb() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testVerifyTrainIsStillDisplayedNonIDRLoginWeb");
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

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User close promo bottomsheet");
        homePage.closeBottomsheetPromo();

        step("User change to " + currency + " currency");
        homePage.selectCurrency(currency);

        step("User is redirected back to home page");
        Assertion.assertThat("Verify Train is still displayed on home page", homePage.verifyVerticalIsDisplayed("Train"), is(true));
    }

    @Author(names = "Hafizh")
    @Module(name = "HomePageWeb")
    @Tribe(name = "Train")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyTrainIsStillDisplayedNonIDRGuestWeb() {
        Map<String, String> param = getParam("testVerifyTrainIsStillDisplayedNonIDRGuestWeb");
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
        Assertion.assertThat("Verify Train is still displayed on home page", homePage.verifyVerticalIsDisplayed("Train"), is(true));
    }

    @Author(names = "Vipul Kumar")
    @Module(name = "SRP")
    @Tribe(name = "Train")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyAllTrainSortsLoginWeb() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testVerifyAllTrainSortsLoginWeb");
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
        CalendarPageWeb<?> calendarPage = CalendarPageWeb.getInstance(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        TrainSearchPageWeb<?> trainSearchPage = TrainSearchPageWeb.getInstance(session);
        TrainSearchResultPageWeb<?> trainSearchResultPage = TrainSearchResultPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

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
        trainSearchPage.clickOnCalendarWidgetToOpen();
        calendarPage.selectDate(departureDate);

        step("User select passenger");
        trainSearchPage.selectPassenger(adultCount,infantCount);

        step("User saves the passengers");
        trainSearchPage.savePassengers();

        step("User search train");
        trainSearchPage.searchTrain();

        step("User skip pop-up");
        trainSearchPage.clickSkipPopup();

        step("User open filter & sort chips");
        trainSearchResultPage.clickFilterChips();

        step("User open all sorting options");
        trainSearchResultPage.openAllSortOption();

        step("User sort by lowest price");
        trainSearchResultPage.selectSortType(TrainSearchResultPageWeb.SortType.LOWEST_PRICE);
        List<Integer> prices = trainSearchResultPage.verifySortByLowestPrice();
        Assertion.assertThat("First train card price should be <= last train card price",
                prices.get(0), lessThanOrEqualTo(prices.get(prices.size() - 1)));

        step("User sort by shortest duration");
        trainSearchResultPage.clickFilterChips();
        trainSearchResultPage.openAllSortOption();
        trainSearchResultPage.selectSortType(TrainSearchResultPageWeb.SortType.SHORTEST_DURATION);
        List<Integer> durations = trainSearchResultPage.verifySortByShortestDuration();
        Assertion.assertThat("First duration should be <= last duration",
                durations.get(0), lessThanOrEqualTo(durations.get(durations.size() - 1)));

        step("User sort by earliest departure");
        trainSearchResultPage.clickFilterChips();
        trainSearchResultPage.openAllSortOption();
        trainSearchResultPage.selectSortType(TrainSearchResultPageWeb.SortType.EARLIEST_DEPARTURE);
        List<Integer> depAsc = trainSearchResultPage.getTrainDepartureTimes();
        Assertion.assertThat("First departure should be <= last departure",
                depAsc.get(0), lessThanOrEqualTo(depAsc.get(depAsc.size() - 1)));

        step("User sort by latest departure");
        trainSearchResultPage.clickFilterChips();
        trainSearchResultPage.openAllSortOption();
        trainSearchResultPage.selectSortType(TrainSearchResultPageWeb.SortType.LATEST_DEPARTURE);
        List<Integer> depDesc = trainSearchResultPage.getTrainDepartureTimes();
        Assertion.assertThat("First departure should be >= last departure",
                depDesc.get(0), greaterThanOrEqualTo(depDesc.get(depDesc.size() - 1)));

        step("User sort by earliest arrival");
        trainSearchResultPage.clickFilterChips();
        trainSearchResultPage.openAllSortOption();
        trainSearchResultPage.selectSortType(TrainSearchResultPageWeb.SortType.EARLIEST_ARRIVAL);
        List<Integer> arrAsc = trainSearchResultPage.getTrainArrivalTimes();
        Assertion.assertThat("First arrival should be <= last arrival",
                arrAsc.get(0), lessThanOrEqualTo(arrAsc.get(arrAsc.size() - 1)));

        step("User sort by latest arrival");
        trainSearchResultPage.clickFilterChips();
        trainSearchResultPage.openAllSortOption();
        trainSearchResultPage.selectSortType(TrainSearchResultPageWeb.SortType.LATEST_ARRIVAL);
        List<Integer> arrDesc = trainSearchResultPage.getTrainArrivalTimes();
        Assertion.assertThat("First arrival should be >= last arrival",
                arrDesc.get(0), greaterThanOrEqualTo(arrDesc.get(arrDesc.size() - 1)));
    }

    @Author(names = "Vipul Kumar")
    @Module(name = "SRP")
    @Tribe(name = "Train")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyAllTrainSortsGuestWeb() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testVerifyAllTrainSortsGuestWeb");
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
        TrainSearchPageWeb<?> trainSearchPage = TrainSearchPageWeb.getInstance(session);
        TrainSearchResultPageWeb<?> trainSearchResultPage = TrainSearchResultPageWeb.getInstance(session);
        CalendarPageWeb<?> calendarPage = CalendarPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open home page as guest");
        loginPage.loginAsGuest();

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
        trainSearchPage.clickOnCalendarWidgetToOpen();
        calendarPage.selectDate(departureDate);

        step("User select passenger");
        trainSearchPage.selectPassenger(adultCount, infantCount);

        step("User saves the passengers");
        trainSearchPage.savePassengers();

        step("User search train");
        trainSearchPage.searchTrain();

        step("User skip pop-up");
        trainSearchPage.clickSkipPopup();

        step("User open filter & sort chips");
        trainSearchResultPage.clickFilterChips();

        step("User open all sorting options");
        trainSearchResultPage.openAllSortOption();

        step("User sort by lowest price");
        trainSearchResultPage.selectSortType(TrainSearchResultPageWeb.SortType.LOWEST_PRICE);
        List<Integer> prices = trainSearchResultPage.verifySortByLowestPrice();
        Assertion.assertThat("First train card price should be <= last train card price",
                prices.get(0), lessThanOrEqualTo(prices.get(prices.size() - 1)));

        step("User sort by shortest duration");
        trainSearchResultPage.clickFilterChips();
        trainSearchResultPage.openAllSortOption();
        trainSearchResultPage.selectSortType(TrainSearchResultPageWeb.SortType.SHORTEST_DURATION);
        List<Integer> durations = trainSearchResultPage.verifySortByShortestDuration();
        Assertion.assertThat("First duration should be <= last duration",
                durations.get(0), lessThanOrEqualTo(durations.get(durations.size() - 1)));

        step("User sort by earliest departure");
        trainSearchResultPage.clickFilterChips();
        trainSearchResultPage.openAllSortOption();
        trainSearchResultPage.selectSortType(TrainSearchResultPageWeb.SortType.EARLIEST_DEPARTURE);
        List<Integer> depAsc = trainSearchResultPage.getTrainDepartureTimes();
        Assertion.assertThat("First departure should be <= last departure",
                depAsc.get(0), lessThanOrEqualTo(depAsc.get(depAsc.size() - 1)));

        step("User sort by latest departure");
        trainSearchResultPage.clickFilterChips();
        trainSearchResultPage.openAllSortOption();
        trainSearchResultPage.selectSortType(TrainSearchResultPageWeb.SortType.LATEST_DEPARTURE);
        List<Integer> depDesc = trainSearchResultPage.getTrainDepartureTimes();
        Assertion.assertThat("First departure should be >= last departure",
                depDesc.get(0), greaterThanOrEqualTo(depDesc.get(depDesc.size() - 1)));

        step("User sort by earliest arrival");
        trainSearchResultPage.clickFilterChips();
        trainSearchResultPage.openAllSortOption();
        trainSearchResultPage.selectSortType(TrainSearchResultPageWeb.SortType.EARLIEST_ARRIVAL);
        List<Integer> arrAsc = trainSearchResultPage.getTrainArrivalTimes();
        Assertion.assertThat("First arrival should be <= last arrival",
                arrAsc.get(0), lessThanOrEqualTo(arrAsc.get(arrAsc.size() - 1)));

        step("User sort by latest arrival");
        trainSearchResultPage.clickFilterChips();
        trainSearchResultPage.openAllSortOption();
        trainSearchResultPage.selectSortType(TrainSearchResultPageWeb.SortType.LATEST_ARRIVAL);
        List<Integer> arrDesc = trainSearchResultPage.getTrainArrivalTimes();
        Assertion.assertThat("First arrival should be >= last arrival",
                arrDesc.get(0), greaterThanOrEqualTo(arrDesc.get(arrDesc.size() - 1)));
    }

    @Author(names = {"Clement","Hafizh"})
    @Module(name = "CompleteBooking")
    @Tribe(name = "Train")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyUserAbleToSeeOrderDetailLoginWeb() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testVerifyUserAbleToSeeOrderDetailLoginWeb");
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

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));

        String paymentMethod = param.get("paymentMethod");
        String paymentGroup = param.get("paymentGroup");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        TrainSearchPageWeb<?> trainSearchPage = TrainSearchPageWeb.getInstance(session);
        TrainSearchResultPageWeb<?> trainSearchResultPage = TrainSearchResultPageWeb.getInstance(session);
        TrainBookingFormWeb<?> trainBookingFormWeb = TrainBookingFormWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User open train vertical");
        homePage.openVerticalPageViaURL("Train");

        step("User skip pop-up");
        trainSearchPage.clickSkipPopup();

        step("User select origin station");
        trainSearchPage.selectOrigin(originStation);

        step("User select destination station");
        trainSearchPage.selectDestination(destinationStation);

        step("User toggle " + tripType + " way");
        trainSearchPage.toggleTripType(tripType);

        step("User select dates");
        trainSearchPage.selectDate(departureDate);

        step("User select passenger");
        trainSearchPage.selectPassenger(adultCount,infantCount);

        step("User saves the passengers");
        trainSearchPage.savePassengers();

        step("User search train");
        trainSearchPage.searchTrain();

        step("User skip pop-up");
        trainSearchPage.clickSkipPopup();

        step("user Select Departure train");
        trainSearchResultPage.selectTrain();

        step("User fill adult details");
        trainBookingFormWeb.fillPassengerDetailAdult(adultCount);

        step("User continues booking after filling details");
        trainBookingFormWeb.clickContinue();

        step("Get passenger name");
        String passengerName = trainBookingFormWeb.getPassengerNameOnBF2();

        step("User Go to Payment Page");
        trainBookingFormWeb.proceedToPaymentPage();

        step("Fetch the order ID");
        String orderID = paymentPage.getOrderId();

        step("Fetch the price after payment method is applied");
        String finalPrice = (paymentPage.getFinalPrice());

        step("Verify order ID is displayed in payment page");
        Assertion.assertThat("Verify order ID is displayed in payment page", !orderID.isEmpty(), is(true));

        step("User select payment method ");
        paymentPage.clickOnSelectPaymentMethod();
        paymentPage.goToPaymentMethodsGroup(paymentGroup);
        paymentPage.selectPaymentMethod(paymentMethod);

        step("User complete payment");
        paymentPage.clickOnPaymentButton();

        String orderIdOnPaymentConfirmation = paymentPage.getOrderId();

        step("Assert order id from payment page and payment confirmation page matches");
        Assertion.assertThat("Order ID on payment page: " +  orderID + " and Order ID on payment confirmation page: "
                + orderIdOnPaymentConfirmation + " matches", orderID.equals(orderIdOnPaymentConfirmation), is(true));

        step("User goes into Order List Page");
        paymentPage.clickSeeOrderListButton();

        step("User open order detail page");
        orderListPage.openActiveTicketViaURL(orderID);
        orderDetailPage.refreshTheWebpage();
        orderDetailPage.skipCoachmark();

        Assertion.assertThat("Passenger name is correct", orderDetailPage.getPassengerName(), equalToIgnoringCase(passengerName));
        Assertion.assertThat("Price is correct", orderDetailPage.getFinalPriceOrderDetail(), equalToIgnoringCase(finalPrice));
    }

    @Author(names = {"Clement","Hafizh"})
    @Module(name = "CompleteBooking")
    @Tribe(name = "Train")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyPaymentDetailOnPaymentListPageLoginWeb() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testVerifyPaymentDetailOnPaymentListPageLoginWeb");
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

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        TrainSearchPageWeb<?> trainSearchPage = TrainSearchPageWeb.getInstance(session);
        TrainSearchResultPageWeb<?> trainSearchResultPage = TrainSearchResultPageWeb.getInstance(session);
        TrainBookingFormWeb<?> trainBookingFormWeb = TrainBookingFormWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User open train vertical");
        homePage.openVerticalPageViaURL("Train");

        step("User skip pop-up");
        trainSearchPage.clickSkipPopup();

        step("User select origin station");
        trainSearchPage.selectOrigin(originStation);

        step("User select destination station");
        trainSearchPage.selectDestination(destinationStation);

        step("User toggle " + tripType + " way");
        trainSearchPage.toggleTripType(tripType);

        step("User select dates");
        trainSearchPage.selectDate(departureDate);

        step("User select passenger");
        trainSearchPage.selectPassenger(adultCount,infantCount);

        step("User saves the passengers");
        trainSearchPage.savePassengers();

        step("User search train");
        trainSearchPage.searchTrain();

        step("User skip pop-up");
        trainSearchPage.clickSkipPopup();

        step("User fetch train price on SRP");
        String priceOnSrp = trainSearchResultPage.getPrice();

        step("User Select Departure train");
        trainSearchResultPage.selectTrain();

        step("User fill adult details");
        trainBookingFormWeb.fillPassengerDetailAdult(adultCount);

        step("Extract departure and arrival");
        String departureTime = trainBookingFormWeb.getDepartureArrivalTime("departure");
        String arrivalTime = trainBookingFormWeb.getDepartureArrivalTime("arrival");

        step("Extract price");
        String price = trainBookingFormWeb.getFinalPrice();
        trainBookingFormWeb.closePricePopUp();

        step("User continues booking after filling details");
        trainBookingFormWeb.clickContinue();

        step("Get passenger name");
        String passengerNameOnBf2 = trainBookingFormWeb.getPassengerNameOnBF2();

        step("User Go to Payment Page");
        trainBookingFormWeb.proceedToPaymentPage();

        step("Verify price");
        paymentPage.openPriceSummary();

        if (Configs.PLATFORM == Platform.DWEB) {
            Assertion.assertThat("Price is correct", paymentPage.getInitialPrice(), equalToIgnoringCase(price));
        }
        else {
            Assertion.assertThat("Price is correct", paymentPage.getInitialPrice(), equalToIgnoringCase(priceOnSrp));
        }

        step("Close price details");
        paymentPage.closeOrderDetails();

        step("Verify order details");
        paymentPage.openOrderDetails();
        Assertion.assertThat("Passenger name is correct", paymentPage.getTrainPassengerNameOnOrderSummary(), is(passengerNameOnBf2));
        Assertion.assertThat("Departure time is correct", paymentPage.getDepartureTime(), equalToIgnoringCase(departureTime));
        Assertion.assertThat("Arrival time is correct", paymentPage.getArrivalTime(), equalToIgnoringCase(arrivalTime));
    }

    @Author(names = {"Clement","Hafizh", "Manan Kumar"})
    @Module(name = "Train")
    @Tribe(name = "NFT")
    @Vertical(name = "NFT")
    @Test
    public void Train_testVerifyPaymentDetailOnPaymentListPageGuestWeb() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("Train_testVerifyPaymentDetailOnPaymentListPageGuestWeb");
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
        TrainSearchPageWeb<?> trainSearchPage = TrainSearchPageWeb.getInstance(session);
        TrainSearchResultPageWeb<?> trainSearchResultPage = TrainSearchResultPageWeb.getInstance(session);
        TrainBookingFormWeb<?> trainBookingFormWeb = TrainBookingFormWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as guest");
        loginPage.loginAsGuest();

        step("User open train vertical");
        homePage.openVerticalPageViaURL("Train");

        step("User skip pop-up");
        trainSearchPage.clickSkipPopup();

        step("User select origin station");
        trainSearchPage.selectOrigin(originStation);

        step("User select destination station");
        trainSearchPage.selectDestination(destinationStation);

        step("User toggle " + tripType + " way");
        trainSearchPage.toggleTripType(tripType);

        step("User select dates");
        trainSearchPage.selectDate(departureDate);

        step("User select passenger");
        trainSearchPage.selectPassenger(adultCount,infantCount);

        step("User saves the passengers");
        trainSearchPage.savePassengers();

        step("User search train");
        trainSearchPage.searchTrain();

        step("User skip pop-up");
        trainSearchPage.clickSkipPopup();

        step("User fetch train price on SRP");
        String priceOnSrp = trainSearchResultPage.getPrice();

        step("User Select Departure train");
        trainSearchResultPage.selectTrain();

        step("User enter contact details");
        trainBookingFormWeb.fillContactDetails(fullName, phoneNumber, emailID);

        step("User fill adult details");
        trainBookingFormWeb.fillPassengerDetailAdult(adultCount);

        step("Extract departure and arrival");
        String departureTime = trainBookingFormWeb.getDepartureArrivalTime("departure");
        String arrivalTime = trainBookingFormWeb.getDepartureArrivalTime("arrival");

        step("User continues booking after filling details");
        trainBookingFormWeb.clickContinue();

        step("Get passenger name");
        String passengerNameOnBf2 = trainBookingFormWeb.getPassengerNameOnBF2();

        step("Extract price");
        String price = trainBookingFormWeb.getPriceDetails();

        step("User Go to Payment Page");
        trainBookingFormWeb.proceedToPaymentPage();

        step("Verify price");
        paymentPage.openPriceSummary();

        if (Configs.PLATFORM == Platform.DWEB) {
            Assertion.assertThat("Price is correct", paymentPage.getInitialPrice(), equalToIgnoringCase(price));
        }
        else {
            Assertion.assertThat("Price is correct", paymentPage.getInitialPrice(), equalToIgnoringCase(priceOnSrp));
        }

        step("Close price details");
        paymentPage.closeOrderDetails();

        step("Verify order details");
        paymentPage.openOrderDetails();
        Assertion.assertThat("Passenger name is correct", paymentPage.getTrainPassengerNameOnOrderSummary(), is(passengerNameOnBf2));
        Assertion.assertThat("Departure time is correct", paymentPage.getDepartureTime(), equalToIgnoringCase(departureTime));
        Assertion.assertThat("Arrival time is correct", paymentPage.getArrivalTime(), equalToIgnoringCase(arrivalTime));
    }

    @Author(names = "Mridul Gupta")
    @Module(name = "CompleteBooking")
    @Tribe(name = "Train")
    @Vertical(name = "NFT")
    @Test
    public void testCompleteTrainBookingOneWayForOneAdultLoginWeb() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testCompleteTrainBookingOneWayForOneAdultLoginWeb");
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
        TrainSearchPageWeb<?> trainSearchPage = TrainSearchPageWeb.getInstance(session);
        TrainSearchResultPageWeb<?> trainSearchResultPage = TrainSearchResultPageWeb.getInstance(session);
        TrainBookingFormWeb<?> trainBookingFormWeb = TrainBookingFormWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);
        CalendarPageWeb<?> calendarPage = CalendarPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open home page as logged in user via cookie");
        loginPage.loginWithSpecificAccountViaCookie(email, password);

        step("User select " + currency + " currency");
        homePage.selectCurrency(currency);

        step("User open Train vertical via deeplink");
        homePage.openVerticalPageViaURL("Train");

        step("User skip pop-up");
        trainSearchPage.clickSkipPopup();

        step("User select origin station: " + originStation);
        trainSearchPage.selectOrigin(originStation);

        step("User select destination station: " + destinationStation);
        trainSearchPage.selectDestination(destinationStation);

        step("User toggle " + tripType + " (roundtrip toggle off)");
        trainSearchPage.toggleTripType(tripType);

        step("User select departure date (next " + daysToDepartureDate + " days)");
        trainSearchPage.clickOnCalendarWidgetToOpen();
        calendarPage.selectDate(departureDate);

        step("User select passenger: " + adultCount + " adult(s)");
        trainSearchPage.selectPassenger(adultCount, infantCount);
        trainSearchPage.savePassengers();

        step("User tap search train button");
        trainSearchPage.searchTrain();
        trainSearchPage.clickSkipPopup();

        step("User select available Train schedule on SRP");
        trainSearchResultPage.selectTrain();

        step("User complete booking form (adult passenger details)");
        trainBookingFormWeb.fillPassengerDetailAdult(adultCount);

        step("User fill infant passenger details");
        trainBookingFormWeb.fillPassengerDetailInfant(adultCount, infantCount);

        step("User verifies origin & destination on booking form");
        Assertion.assertThat("Verifies origin station contains " + originStation,
                trainBookingFormWeb.verifyDepartureOrigin(), containsString(originStation));
        Assertion.assertThat("Verifies destination station contains " + destinationStation,
                trainBookingFormWeb.verifyDepartureDestination(), containsString(destinationStation));

        step("User tap continue button to go to booking form 2");
        trainBookingFormWeb.continueToBookingForm2();

        step("User tap continue to payment button on Train booking form 2");
        trainBookingFormWeb.proceedToPaymentPage();

        step("User proceed to payment page and verify order ID");
        trainBookingFormWeb.goToPaymentPage();
        String orderID = paymentPage.getOrderId();
        Assertion.assertThat("Verify order ID is displayed in payment page", !orderID.isEmpty(), is(true));
    }

    @Author(names = "Mridul Gupta")
    @Module(name = "CompleteBooking")
    @Tribe(name = "Train")
    @Vertical(name = "NFT")
    @Test
    public void testCompleteTrainBookingRoundTripForTwoAdultsGuestWeb() {
        Map<String, String> param = getParam("testCompleteTrainBookingRoundTripForTwoAdultsGuestWeb");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String currency = param.get("currency");
        String originStation = param.get("originStation");
        String destinationStation = param.get("destinationStation");
        String tripType = param.get("tripType");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int daysToReturnDate = Integer.parseInt(param.get("daysToReturnDate"));
        int adultCount = Integer.parseInt(param.get("adultCount"));
        int infantCount = Integer.parseInt(param.get("infantCount"));

        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);
        String returnDate = CoreFunctions.addToLongDate(departureDate, daysToReturnDate);

        String fullName = CoreFunctions.randomName();
        String phoneNumber = "62" + CoreFunctions.randomInt(8);
        String randomEmail = CoreFunctions.randomEmail();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        TrainSearchPageWeb<?> trainSearchPage = TrainSearchPageWeb.getInstance(session);
        TrainSearchResultPageWeb<?> trainSearchResultPage = TrainSearchResultPageWeb.getInstance(session);
        TrainBookingFormWeb<?> trainBookingFormWeb = TrainBookingFormWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);
        CalendarPageWeb<?> calendarPage = CalendarPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open home page as guest (non-login)");
        loginPage.loginAsGuest();

        step("User select " + currency + " currency");
        homePage.selectCurrency(currency);

        step("User open Train vertical via deeplink");
        homePage.openVerticalPageViaURL("Train");

        step("User skip pop-up");
        trainSearchPage.clickSkipPopup();

        step("User select origin station: " + originStation);
        trainSearchPage.selectOrigin(originStation);

        step("User select destination station: " + destinationStation);
        trainSearchPage.selectDestination(destinationStation);

        step("User toggle " + tripType + " (roundtrip toggle on)");
        trainSearchPage.toggleTripType(tripType);

        step("User select departure date (next " + daysToDepartureDate + " days) and return date (next "
                + (daysToDepartureDate + daysToReturnDate) + " days)");
        trainSearchPage.clickOnCalendarWidgetToOpen();
        calendarPage.selectDate(departureDate, returnDate);

        step("User select passenger: " + adultCount + " adult(s)");
        trainSearchPage.selectPassenger(adultCount, infantCount);
        trainSearchPage.savePassengers();

        step("User tap search train button");
        trainSearchPage.searchTrain();
        trainSearchPage.clickSkipPopup();

        step("User select available departure Train schedule on SRP");
        trainSearchResultPage.selectTrain();

        if (Configs.PLATFORM == Platform.DWEB) {
            Assertion.assertThat("Verify user can proceed to select return train",
                    trainSearchResultPage.checkReturn(), is(true));
        }

        step("User select available return Train schedule on SRP");
        trainSearchResultPage.selectTrain();

        step("User fill contact details");
        trainBookingFormWeb.fillContactDetails(fullName, phoneNumber, randomEmail);

        step("User complete booking form (adult passenger details)");
        trainBookingFormWeb.fillPassengerDetailAdult(adultCount);

        step("User fill infant passenger details");
        trainBookingFormWeb.fillPassengerDetailInfant(adultCount, infantCount);

        step("User verifies departure origin & destination stations");
        Assertion.assertThat("Verifies departure origin station contains " + originStation,
                trainBookingFormWeb.verifyDepartureOrigin(), containsString(originStation));
        Assertion.assertThat("Verifies departure destination station contains " + destinationStation,
                trainBookingFormWeb.verifyDepartureDestination(), containsString(destinationStation));

        step("User verifies return origin & destination stations (swapped for return)");
        Assertion.assertThat("Verifies return origin station contains " + destinationStation,
                trainBookingFormWeb.verifyReturnOrigin(), containsString(destinationStation));
        Assertion.assertThat("Verifies return destination station contains " + originStation,
                trainBookingFormWeb.verifyReturnDestination(), containsString(originStation));

        step("User tap continue button to go to booking form 2");
        trainBookingFormWeb.continueToBookingForm2();

        step("User tap continue to payment button on Train booking form 2");
        trainBookingFormWeb.proceedToPaymentPage();

        step("User proceed to payment page and verify order ID");
        trainBookingFormWeb.goToPaymentPage();
        String orderID = paymentPage.getOrderId();
        Assertion.assertThat("Verify order ID is displayed in payment page", !orderID.isEmpty(), is(true));
    }

    @Author(names = "Mridul Gupta")
    @Module(name = "CompleteBooking")
    @Tribe(name = "Train")
    @Vertical(name = "NFT")
    @Test
    public void testCompleteTrainBookingOneWayWithInsuranceForThreeAdultsLoginWeb() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testCompleteTrainBookingOneWayWithInsuranceForThreeAdultsLoginWeb");
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
        TrainSearchPageWeb<?> trainSearchPage = TrainSearchPageWeb.getInstance(session);
        TrainSearchResultPageWeb<?> trainSearchResultPage = TrainSearchResultPageWeb.getInstance(session);
        TrainBookingFormWeb<?> trainBookingFormWeb = TrainBookingFormWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);
        CalendarPageWeb<?> calendarPage = CalendarPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open home page as logged in user via cookie");
        loginPage.loginWithSpecificAccountViaCookie(email, password);

        step("User select " + currency + " currency");
        homePage.selectCurrency(currency);

        step("User open Train vertical via deeplink");
        homePage.openVerticalPageViaURL("Train");

        step("User skip pop-up");
        trainSearchPage.clickSkipPopup();

        step("User select origin station: " + originStation);
        trainSearchPage.selectOrigin(originStation);

        step("User select destination station: " + destinationStation);
        trainSearchPage.selectDestination(destinationStation);

        step("User toggle " + tripType + " trip (roundtrip toggle off)");
        trainSearchPage.toggleTripType(tripType);

        step("User select departure date (next " + daysToDepartureDate + " days)");
        trainSearchPage.clickOnCalendarWidgetToOpen();
        calendarPage.selectDate(departureDate);

        step("User select passenger: " + adultCount + " adult(s)");
        trainSearchPage.selectPassenger(adultCount, infantCount);
        trainSearchPage.savePassengers();

        step("User tap search train button");
        trainSearchPage.searchTrain();
        trainSearchPage.clickSkipPopup();

        step("User select first available Train schedule on SRP");
        trainSearchResultPage.selectTrain();

        step("User fill booking form (adult passenger details)");
        trainBookingFormWeb.fillPassengerDetailAdult(adultCount);

        step("User fill infant passenger details");
        trainBookingFormWeb.fillPassengerDetailInfant(adultCount, infantCount);

        step("User verifies origin & destination on booking form");
        Assertion.assertThat("Verifies origin station contains " + originStation,
                trainBookingFormWeb.verifyDepartureOrigin(), containsString(originStation));
        Assertion.assertThat("Verifies destination station contains " + destinationStation,
                trainBookingFormWeb.verifyDepartureDestination(), containsString(destinationStation));

        step("User tap continue button to go to booking form 2");
        trainBookingFormWeb.continueToBookingForm2();

        step("User apply any insurance add-on on Train booking form 2");
        trainBookingFormWeb.addNotAutoSelectedInsurance();

        step("User tap on continue to payment button");
        trainBookingFormWeb.proceedToPaymentPage();

        step("User proceed to payment page and verify order ID");
        trainBookingFormWeb.goToPaymentPage();
        String orderID = paymentPage.getOrderId();
        Assertion.assertThat("Verify order ID is displayed in payment page", !orderID.isEmpty(), is(true));
        Assertion.assertThat("Verify insurance add-on is applied to the order", paymentPage.isInsuranceApplied(), is(true));
    }

    @Author(names = "Mridul Gupta")
    @Module(name = "MyOrder")
    @Tribe(name = "Train")
    @Vertical(name = "NFT")
    @Test
    public void testChangeTrainSeatOnUnpaidOrderLoginWeb() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testChangeTrainSeatOnUnpaidOrderLoginWeb");
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
        TrainSearchPageWeb<?> trainSearchPage = TrainSearchPageWeb.getInstance(session);
        TrainSearchResultPageWeb<?> trainSearchResultPage = TrainSearchResultPageWeb.getInstance(session);
        TrainBookingFormWeb<?> trainBookingFormWeb = TrainBookingFormWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);
        CalendarPageWeb<?> calendarPage = CalendarPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);

        // === PRECONDITION: create an unpaid (Waiting for Payments) Train order for the login user ===

        step("User open home page");
        homePage.goToHomePage();

        step("User open home page as logged in user via cookie");
        loginPage.loginWithSpecificAccountViaCookie(email, password);

        step("User select " + currency + " currency");
        homePage.selectCurrency(currency);

        step("User open Train vertical via deeplink");
        homePage.openVerticalPageViaURL("Train");

        step("User skip pop-up");
        trainSearchPage.clickSkipPopup();

        step("User select origin station: " + originStation);
        trainSearchPage.selectOrigin(originStation);

        step("User select destination station: " + destinationStation);
        trainSearchPage.selectDestination(destinationStation);

        step("User toggle " + tripType + " trip type");
        trainSearchPage.toggleTripType(tripType);

        step("User select departure date (next " + daysToDepartureDate + " days)");
        trainSearchPage.clickOnCalendarWidgetToOpen();
        calendarPage.selectDate(departureDate);

        step("User select passenger: " + adultCount + " adult(s)");
        trainSearchPage.selectPassenger(adultCount, infantCount);
        trainSearchPage.savePassengers();

        step("User tap search button");
        trainSearchPage.searchTrain();
        trainSearchPage.clickSkipPopup();

        step("User select first available train");
        trainSearchResultPage.selectTrain();

        step("User fill adult passenger details");
        trainBookingFormWeb.fillPassengerDetailAdult(adultCount);

        step("User fill infant passenger details");
        trainBookingFormWeb.fillPassengerDetailInfant(adultCount, infantCount);

        step("User continue to booking form 2");
        trainBookingFormWeb.continueToBookingForm2();

        step("User proceed to payment page");
        trainBookingFormWeb.proceedToPaymentPage();

        String orderID = paymentPage.getOrderId();
        Assertion.assertThat("Verify order ID is generated on payment page", !orderID.isEmpty(), is(true));

        // === ACTUAL TEST: navigate to unpaid order detail, change seat, continue to payment ===

        step("Login user go to their unpaid (Waiting for Payments) Train active order detail");
        orderListPage.openActiveTicketViaURL(orderID);

        step("User tap on Change seat button on Train order details");
        Assertion.assertThat("Change seat button is displayed on unpaid Train order detail",
                orderDetailPage.isChangeTrainSeatButtonDisplayed(), is(true));
        orderDetailPage.clickChangeTrainSeatIfPresent();

        step("User select any available seat and tap save");
        trainBookingFormWeb.saveSeat();

        step("User continue to payment process");
        trainBookingFormWeb.continueToPay();

        step("Verify user is back on payment page after seat change");
        String orderIDAfterSeatChange = paymentPage.getOrderId();
        Assertion.assertThat("Verify order ID is displayed in payment page after seat change",
                !orderIDAfterSeatChange.isEmpty(), is(true));
    }
}
