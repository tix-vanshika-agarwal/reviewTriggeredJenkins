package com.tiket.test.nft.whoosh;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Environment;
import com.tiket.model.Platform;
import com.tiket.model.Session;
import com.tiket.page.common.booking_form.CommonBookingFormPage;
import com.tiket.page.common.webview.WebViewPage;
import com.tiket.page.bus.search.BusSearchPage;
import com.tiket.page.home.HomePage;
import com.tiket.page.login.LoginPage;
import com.tiket.page.order.orderdetail.OrderDetailPage;
import com.tiket.page.order.orderlist.OrderListPage;
import com.tiket.page.payment.PaymentPage;
import com.tiket.page.whoosh.bookingform.WhooshBookingPage;
import com.tiket.page.whoosh.searchpage.WhooshSearchPage;
import com.tiket.page.whoosh.searchresultpage.WhooshSearchResultPage;
import com.tiket.pojo.train.TrainData;
import com.tiket.pojo.whoosh.TrainAndWhooshGeneralData;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.testbase.Configs;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import com.tiket.page.refund.RefundPage;

import java.util.Map;

import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;

public class WhooshTest extends BaseTest {

    @Author(names = {"Ashish"})
    @Module(name = "RecentlySearched")
    @Tribe(name = "Whoosh")
    @Vertical(name = "GroundTransport")
    @Test
    public void testWhooshCheckRecentSearchedLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testWhooshCheckRecentSearchedLogin");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        WhooshSearchPage<WebDriver> whooshSearchPage = WhooshSearchPage.getInstance(session);
        WhooshSearchResultPage<WebDriver> whooshSearchResultPage = WhooshSearchResultPage.getInstance(session);

        String username = param.get("username");
        String password = param.get("password");
        String loginType = param.get("loginType");
        String currency = param.get("currency");
        String originStation = param.get("originStation");
        String destinationStation = param.get("destinationStation");
        String tripType = param.get("tripType");

        int adultPax = 1, infantPax = 0;

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        TrainAndWhooshGeneralData trainAndWhooshGeneralData = new TrainAndWhooshGeneralData(originStation, destinationStation,adultPax + infantPax,adultPax,infantPax,tripType,daysToDepartureDate);
        String departureDate = trainAndWhooshGeneralData.departureDateLong;

        step("Go to Home Page");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink (if needed) and change currency to " + currency);
        homePage.loginIfLoggedOutAndChangeCurrencyViaDeeplink(username, password, currency);

        step("Open Whoosh & skip popup if required");
        homePage.openVerticalPageDeeplink("Whoosh","",currency);
        whooshSearchPage.skipPopup();

        step("Clear recently searched if present");
        whooshSearchPage.clearRecentlySearch();

        step("Select trip type");
        whooshSearchPage.selectRoundTripToggle(tripType);

        step("Select origin station");
        whooshSearchPage.selectOrigin(originStation);

        step("Select destination station");
        whooshSearchPage.selectDestination(destinationStation);

        step("Select date");
        whooshSearchPage.selectDate(departureDate);

        step("set passenger data");
        whooshSearchPage.addPassenger(adultPax,infantPax);

        step("Get Whoosh date");
        String whooshSelectedDate = whooshSearchPage.getWhooshDate(trainAndWhooshGeneralData);
        Assertion.assertThat("Selected date is correct", whooshSelectedDate, anyOf(containsString(trainAndWhooshGeneralData.whooshDateFormatted_dd_MMM_En), containsString(trainAndWhooshGeneralData.whooshDateFormatted_dd_MMM_Id)));

        step("Search whoosh");
        whooshSearchPage.searchTrain();

        step("Skip Popup");
        whooshSearchPage.skipPopup();

        step("Verify search result page");
        Assertion.assertThat("user is in search result",whooshSearchResultPage.verifySearch(trainAndWhooshGeneralData),is(true));

        step("Back to search page");
        whooshSearchResultPage.backToSearchPage();

        step("Verify recently search card is present");
        Assertion.assertThat("is recently search present",whooshSearchPage.verifyRecentlySearch(trainAndWhooshGeneralData),is(true));

        step("Click recently searched to go to srp");
        whooshSearchPage.clickRecentlySearch(trainAndWhooshGeneralData.whooshDateFormatted_dd_MMM_yy_En, trainAndWhooshGeneralData.whooshDateFormatted_dd_MMM_yy_Id);

        step("Verify correct search result displayed");
        Assertion.assertThat("user is in search result",whooshSearchResultPage.verifySearch(trainAndWhooshGeneralData),is(true));
    }

    @Author(names = {"Ashish"})
    @Module(name = "RecentlySearched")
    @Tribe(name = "Whoosh")
    @Vertical(name = "GroundTransport")
    @Test
    public void testWhooshCheckRecentSearchedGuest() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testWhooshCheckRecentSearchedGuest");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        WhooshSearchPage<WebDriver> whooshSearchPage = WhooshSearchPage.getInstance(session);
        WhooshSearchResultPage<WebDriver> whooshSearchResultPage = WhooshSearchResultPage.getInstance(session);

        String currency = param.get("currency");
        String originStation = param.get("originStation");
        String destinationStation = param.get("destinationStation");
        String tripType = param.get("tripType");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        TrainAndWhooshGeneralData trainAndWhooshGeneralData = new TrainAndWhooshGeneralData(originStation, destinationStation,1,1,0,tripType,daysToDepartureDate);
        String departureDate = trainAndWhooshGeneralData.departureDateLong;

        step("Go to Home Page");
        homePage.gotoHome();

        step("Logout via deeplink");
        homePage.logoutViaDeeplink();

        step("change currency");
        homePage.changeCurrency(currency, "");

        step("Open Whoosh & skip popup if required");
        homePage.openVerticalPageDeeplink("Whoosh","",currency);
        whooshSearchPage.skipPopup();

        step("Clear recently searched if present");
        whooshSearchPage.clearRecentlySearch();

        step("Select trip type");
        whooshSearchPage.selectRoundTripToggle(tripType);

        step("Select origin station");
        whooshSearchPage.selectOrigin(originStation);

        step("Select destination station");
        whooshSearchPage.selectDestination(destinationStation);

        step("Select date");
        whooshSearchPage.selectDate(departureDate);

        step("Get Whoosh date");
        String whooshSelectedDate = whooshSearchPage.getWhooshDate(trainAndWhooshGeneralData);
        Assertion.assertThat("Selected date is correct", whooshSelectedDate, anyOf(containsString(trainAndWhooshGeneralData.whooshDateFormatted_dd_MMM_En), containsString(trainAndWhooshGeneralData.whooshDateFormatted_dd_MMM_Id)));

        step("Search whoosh");
        whooshSearchPage.searchTrain();

        step("Skip Popup");
        whooshSearchPage.skipPopup();

        step("Verify search result page");
        Assertion.assertThat("user is in search result",whooshSearchResultPage.verifySearch(trainAndWhooshGeneralData),is(true));

        step("Back to search page");
        whooshSearchResultPage.backToSearchPage();

        step("Verify recently search card is present");
        Assertion.assertThat("is recently search present",whooshSearchPage.verifyRecentlySearch(trainAndWhooshGeneralData),is(true));

        step("Click recently searched to go to srp");
        whooshSearchPage.clickRecentlySearch(trainAndWhooshGeneralData.whooshDateFormatted_dd_MMM_yy_En, trainAndWhooshGeneralData.whooshDateFormatted_dd_MMM_yy_Id);

        step("Verify correct search result displayed");
        Assertion.assertThat("user is in search result",whooshSearchResultPage.verifySearch(trainAndWhooshGeneralData),is(true));
    }

    @Author(names = {"Ashish"})
    @Module(name = "CrossSell")
    @Tribe(name = "Whoosh")
    @Vertical(name = "GroundTransport")
    @Test
    public void testWhooshCheckCrossSellBusGuest() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testWhooshCheckCrossSellBusGuest");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        WhooshSearchPage<WebDriver> whooshSearchPage = WhooshSearchPage.getInstance(session);
        WhooshSearchResultPage<WebDriver> whooshSearchResultPage = WhooshSearchResultPage.getInstance(session);
        BusSearchPage<WebDriver> busSearchPage = BusSearchPage.getInstance(session);

        String username = param.get("username");
        String currency = param.get("currency");
        String originStation = param.get("originStation");
        String destinationStation = param.get("destinationStation");
        String tripType = param.get("tripType");
        int adultPassengers = Integer.parseInt(param.get("adultPassengers"));

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        TrainAndWhooshGeneralData trainAndWhooshGeneralData = new TrainAndWhooshGeneralData(originStation, destinationStation,adultPassengers,adultPassengers,0,tripType,daysToDepartureDate);
        String departureDate = trainAndWhooshGeneralData.departureDateLong;

        step("Go to Home Page");
        homePage.gotoHome();

        step("Logout via deeplink");
        homePage.logoutViaDeeplink();

        step("change currency");
        homePage.changeCurrency(currency, username);

        step("Open Whoosh & skip popup if required");
        homePage.openVerticalPageDeeplink("Whoosh","",currency);
        whooshSearchPage.skipPopup();

        step("Select trip type");
        whooshSearchPage.selectRoundTripToggle(tripType);

        step("Select origin station");
        trainAndWhooshGeneralData.originCityName = whooshSearchPage.selectOriginAndGetCityName(originStation);

        step("Select destination station");
        trainAndWhooshGeneralData.destinationCityName = whooshSearchPage.selectDestinationAndGetCityName(destinationStation);

        step("Select date");
        whooshSearchPage.selectDate(departureDate);

        step("Add passenger");
        whooshSearchPage.addPassenger(adultPassengers,0);

        step("Get Whoosh date");
        String whooshSelectedDate = whooshSearchPage.getWhooshDate(trainAndWhooshGeneralData);
        Assertion.assertThat("Selected date is correct", whooshSelectedDate, anyOf(containsString(trainAndWhooshGeneralData.whooshDateFormatted_dd_MMM_En), containsString(trainAndWhooshGeneralData.whooshDateFormatted_dd_MMM_Id)));

        step("Search whoosh");
        whooshSearchPage.searchTrain();

        step("Skip Popup");
        whooshSearchPage.skipPopup();

        step("Verify that SRP don't have any data");
        Boolean verifyData = whooshSearchResultPage.verifySRPHaveNoResult();
        Assertion.assertThat("Verify that search result page don't have any data",verifyData,is(true));

        step("Click cross vertical bus button");
        whooshSearchResultPage.clickCrossSell();
        Assertion.assertThat("Verify redirected to BUS landing page",busSearchPage.isBusLandingPageDisplayed(),is(true));
    }

    @Author(names = {"Ashish"})
    @Module(name = "OrderAndPaymentDetails")
    @Tribe(name = "Whoosh")
    @Vertical(name = "GroundTransport")
    @Test
    public void testWhooshCheckDetailPaymentPageGuest() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testWhooshCheckDetailPaymentPageGuest");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        WhooshSearchPage<WebDriver> whooshSearchPage = WhooshSearchPage.getInstance(session);
        WhooshSearchResultPage<WebDriver> whooshSearchResultPage = WhooshSearchResultPage.getInstance(session);
        WhooshBookingPage<WebDriver> whooshBookingPage = WhooshBookingPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);

        String tripType = param.get("tripType");
        String currency = param.get("currency");
        String originStation = param.get("originStation");
        String destinationStation = param.get("destinationStation");
        String email = param.get("email");
        String dateRange = param.get("dateRange");

        int adultPassengers = Integer.parseInt(param.get("adult"));
        int infantPassengers = Integer.parseInt(param.get("infant"));

        TrainAndWhooshGeneralData trainAndWhooshGeneralData = new TrainAndWhooshGeneralData(originStation, destinationStation,adultPassengers,adultPassengers,infantPassengers,tripType,dateRange);
        String departureDate = trainAndWhooshGeneralData.departureDateLong;

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Logout via deeplink");
        homePage.logoutViaDeeplink();

        step("Change currency to 'IDR'");
        homePage.changeCurrency(currency,"");

        step("Get App Language");
        String language = homePage.getLanguageApp();

        step("Open whoosh Landing Page");
        homePage.openVerticalPageDeeplink("Whoosh", "", currency);

        step("Skip Popup");
        whooshSearchPage.skipPopup();

        step("Select origin station");
        trainAndWhooshGeneralData.originCityName = whooshSearchPage.selectOriginAndGetCityName(originStation);

        step("Select destination station");
        trainAndWhooshGeneralData.destinationCityName = whooshSearchPage.selectDestinationAndGetCityName(destinationStation);

        step("Toggle trip type to "+tripType);
        whooshSearchPage.selectRoundTripToggle(tripType);

        step("Select date");
        whooshSearchPage.selectDate(departureDate);

        step("Add passenger");
        whooshSearchPage.addPassenger(adultPassengers,infantPassengers);

        step("Search whoosh");
        whooshSearchPage.searchTrain();

        step("Skip Popup");
        whooshSearchPage.skipPopup();

        step("Select departure whoosh");
        whooshSearchResultPage.selectWhoosh();

        step("Fill contact data");
        whooshBookingPage.fillGuestDetail(email);

        step("Fill passenger detail");
        whooshBookingPage.fillPassengerDetail(adultPassengers,infantPassengers);

        step("Continue to 2nd half");
        whooshBookingPage.continueTo2ndHalf();

        step("get details from the booking page");
        trainAndWhooshGeneralData.trainDataBookingPage = whooshBookingPage.getDetailsWhooshAndPassenger(language);

        step("get total payment");
        trainAndWhooshGeneralData.totalPaymentBookingPage = whooshBookingPage.getTotalPayment();

        step("Open price detail");
        whooshBookingPage.openPriceDetail(tripType);

        step("Verify price detail");
        Assertion.assertThat("Is price breakdown correct", whooshBookingPage.isPriceBreakdownCorrect(trainAndWhooshGeneralData), is(true));

        step("close price detail");
        whooshBookingPage.closePriceDetail();

        step("Continue to payment");
        whooshBookingPage.continueToPayment();

        step("Check order ID");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",!orderID.isBlank(),is(true));

        step("get guest and train data from payment page");
        trainAndWhooshGeneralData.trainDataPaymentPage = paymentPage.getTrainGuestInfoPaymentPage();

        step("open price detail page");
        paymentPage.openPriceDetail();

        step("get depart and arrival station price detail");
        trainAndWhooshGeneralData.departAndArrivalStationPriceDetail = paymentPage.getDepartAndArrivalStationPriceDetail();

        step("get total price");
        trainAndWhooshGeneralData.totalPaymentPaymentPage = paymentPage.getPriceWithoutOtherFees();

        step("price breakdown");
        Assertion.assertThat("is price breakdown correct: ", paymentPage.priceBreakDown(), is(true));

        step("verify details");
        paymentPage.verifyDetailsTrainAndPassenger(trainAndWhooshGeneralData);
    }

    @Author(names = {"Ashish"})
    @Module(name = "OrderAndPaymentDetails")
    @Tribe(name = "Whoosh")
    @Vertical(name = "GroundTransport")
    @Test
    public void testWhooshCheckDetailPaymentPageLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testWhooshCheckDetailPaymentPageLogin");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        WhooshSearchPage<WebDriver> whooshSearchPage = WhooshSearchPage.getInstance(session);
        WhooshSearchResultPage<WebDriver> whooshSearchResultPage = WhooshSearchResultPage.getInstance(session);
        WhooshBookingPage<WebDriver> whooshBookingPage = WhooshBookingPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        CommonBookingFormPage<WebDriver> commonBookingFormPage = CommonBookingFormPage.getInstance(session);

        String username = param.get("username");
        String password = param.get("password");
        String tripType = param.get("tripType");
        String currency = param.get("currency");
        String originStation = param.get("originStation");
        String destinationStation = param.get("destinationStation");
        String dateRange = param.get("dateRange");

        int adultPassengers = Integer.parseInt(param.get("adult"));
        int infantPassengers = Integer.parseInt(param.get("infant"));

        TrainAndWhooshGeneralData trainAndWhooshGeneralData = new TrainAndWhooshGeneralData(originStation, destinationStation,adultPassengers,adultPassengers,infantPassengers,tripType,dateRange);
        String departureDate = trainAndWhooshGeneralData.departureDateLong;

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink (if needed) and change currency to " + currency);
        homePage.loginIfLoggedOutAndChangeCurrencyViaDeeplink(username, password, currency);

        step("Get App Language");
        String language = homePage.getLanguageApp();

        step("Open whoosh Landing Page");
        homePage.openVerticalPageDeeplink("Whoosh", "", currency);

        step("Skip Popup");
        whooshSearchPage.skipPopup();

        step("Select origin station");
        trainAndWhooshGeneralData.originCityName = whooshSearchPage.selectOriginAndGetCityName(originStation);

        step("Select destination station");
        trainAndWhooshGeneralData.destinationCityName = whooshSearchPage.selectDestinationAndGetCityName(destinationStation);

        step("Toggle trip type to "+tripType);
        whooshSearchPage.selectRoundTripToggle(tripType);

        step("Select date");
        whooshSearchPage.selectDate(departureDate);

        step("Add passenger");
        whooshSearchPage.addPassenger(adultPassengers,infantPassengers);

        step("Search whoosh");
        whooshSearchPage.searchTrain();

        step("Skip Popup");
        whooshSearchPage.skipPopup();

        step("Select departure whoosh");
        whooshSearchResultPage.selectWhoosh();

        step("Fill passenger detail");
        whooshBookingPage.fillPassengerDetail(adultPassengers,infantPassengers);

        step("Continue to 2nd half");
        whooshBookingPage.continueTo2ndHalf();

        step("get details from the booking page");
        trainAndWhooshGeneralData.trainDataBookingPage = whooshBookingPage.getDetailsWhooshAndPassenger(language);

        step("get total payment");
        trainAndWhooshGeneralData.totalPaymentBookingPage = whooshBookingPage.getTotalPayment();

        step("Open price detail");
        whooshBookingPage.openPriceDetail(tripType);

        step("Verify price detail");
        Assertion.assertThat("Is price breakdown correct", whooshBookingPage.isPriceBreakdownCorrect(trainAndWhooshGeneralData), is(true));

        step("close price detail");
        whooshBookingPage.closePriceDetail();

        step("Continue to payment");
        whooshBookingPage.continueToPayment();

        step("Check order ID");
        commonBookingFormPage.waitProcessingBooking();
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",!orderID.isBlank(),is(true));

        step("get guest and whoosh data from payment page");
        trainAndWhooshGeneralData.trainDataPaymentPage = paymentPage.getTrainGuestInfoPaymentPage();

        step("open price detail page");
        paymentPage.openPriceDetail();

        step("get depart and arrival station price detail");
        trainAndWhooshGeneralData.departAndArrivalStationPriceDetail = paymentPage.getDepartAndArrivalStationPriceDetail();

        step("get total price");
        trainAndWhooshGeneralData.totalPaymentPaymentPage = paymentPage.getPriceWithoutOtherFees();

        step("price breakdown");
        Assertion.assertThat("is price breakdown correct: ", paymentPage.priceBreakDown(), is(true));

        step("verify details");
        paymentPage.verifyDetailsTrainAndPassenger(trainAndWhooshGeneralData);
    }

    @Author(names = {"Ashish"})
    @Module(name = "Whoosh")
    @Tribe(name = "NFT")
    @Vertical(name = "GroundTransport")
    @Test
    public void Whoosh_testWhooshCheckOrderDetailInMyOrderMenuGuest(){
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("Whoosh_testWhooshCheckOrderDetailInMyOrderMenuGuest");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        WhooshSearchPage<WebDriver> whooshSearchPage = WhooshSearchPage.getInstance(session);
        WhooshSearchResultPage<WebDriver> whooshSearchResultPage = WhooshSearchResultPage.getInstance(session);
        WhooshBookingPage<WebDriver> whooshBookingPage = WhooshBookingPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);

        String tripType = param.get("tripType");
        String currency = param.get("currency");
        String originStation = param.get("originStation");
        String destinationStation = param.get("destinationStation");
        String dateRange = param.get("dateRange");

        int adultPassengers = Integer.parseInt(param.get("adult"));
        int infantPassengers = Integer.parseInt(param.get("infant"));

        String departureDate = CoreFunctions.addToLongRandomDate(dateRange);
        String email = param.get("email");

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Logout via deeplink");
        homePage.logoutViaDeeplink();

        step("Change currency to 'IDR'");
        homePage.changeCurrency("IDR","");

        step("Open whoosh Landing Page");
        homePage.openVerticalPageDeeplink("Whoosh", "", currency);

        step("Skip Popup");
        whooshSearchPage.skipPopup();

        step("Select origin station");
        whooshSearchPage.selectOrigin(originStation);

        step("Select destination station");
        whooshSearchPage.selectDestination(destinationStation);

        step("Toggle trip type to "+tripType);
        whooshSearchPage.selectRoundTripToggle(tripType);

        step("Select date");
        whooshSearchPage.selectDate(departureDate);

        step("Add passenger");
        whooshSearchPage.addPassenger(adultPassengers,infantPassengers);

        step("Search whoosh");
        whooshSearchPage.searchTrain();

        step("Skip Popup");
        whooshSearchPage.skipPopup();

        step("Select departure whoosh");
        whooshSearchResultPage.selectWhoosh();

        step("Fill contact data");
        whooshBookingPage.fillGuestDetail(email);

        step("Fill passenger detail");
        whooshBookingPage.fillPassengerDetail(adultPassengers,infantPassengers);

        step("Continue to 2nd half");
        whooshBookingPage.continueTo2ndHalf();

        step("Continue to payment");
        whooshBookingPage.continueToPayment();

        step("Check order ID");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",!orderID.isBlank(),is(true));

        step("Open payment method");
        paymentPage.openPaymentMethod();

        if(Configs.ENVIRONMENT == Environment.PROD){
            step("Select virtual account as payment method");
            paymentPage.selectPaymentMethod("BCA Virtual Account");
        }else {
            step("select sakaku as payment method");
            paymentPage.selectPaymentMethod("Sakuku");
        }

        step("get guest and whoosh data from payment page");
        TrainData data = paymentPage.getTrainGuestInfoPaymentPage();
        data.orderId = orderID;

        step("get total price");
        String totalPrice = paymentPage.getTotalPriceFirstPage();

        step("Submit payment");
        paymentPage.clickPayButton();

        if(Configs.ENVIRONMENT == Environment.PROD){
            step("Go to the order list");
            paymentPage.seeOrderList();

            step("Open order");
            orderListPage.openOrderDetailWaitingForPayment(orderID);
        }else {
            step("open issued order");
            Assertion.assertThat("Payment Successful and Thank You page is shown", paymentPage.isThankYouPageShown(), is(true));
            orderListPage.openIssuedTicketViaDeeplink(orderID);
            
            Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.getETicketLabel(), either(is("Your e-ticket is here")).or(is("E-tiketmu di sini")));
        }

        step("check whoosh and passenger detail correct");
        orderDetailPage.checkTrainDetails(data);
        orderDetailPage.checkPassengerDetailsTrain(data);

        step("verify total payment correct");
        Assertion.assertThat("is total payment from payment is equals to order menu",orderDetailPage.verifyTotalPaymentTrain(data, totalPrice),is(true));
    }

    @Author(names = {"Ashish"})
    @Module(name = "Whoosh")
    @Tribe(name = "NFT")
    @Vertical(name = "GroundTransport")
    @Test
    public void Whoosh_testWhooshCheckOrderDetailInMyOrderMenuLogin(){
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("Whoosh_testWhooshCheckOrderDetailInMyOrderMenuLogin");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        WhooshSearchPage<WebDriver> whooshSearchPage = WhooshSearchPage.getInstance(session);
        WhooshSearchResultPage<WebDriver> whooshSearchResultPage = WhooshSearchResultPage.getInstance(session);
        WhooshBookingPage<WebDriver> whooshBookingPage = WhooshBookingPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String tripType = param.get("tripType");
        String currency = param.get("currency");
        String originStation = param.get("originStation");
        String destinationStation = param.get("destinationStation");
        String dateRange = param.get("dateRange");

        int adultPassengers = Integer.parseInt(param.get("adult"));
        int infantPassengers = Integer.parseInt(param.get("infant"));

        String departureDate = CoreFunctions.addToLongRandomDate(dateRange);
        String email = param.get("email");

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink (if needed) and change currency to " + currency);
        homePage.loginIfLoggedOutAndChangeCurrencyViaDeeplink(username, password, currency);

        step("Open whoosh Landing Page");
        homePage.openVerticalPageDeeplink("Whoosh", "", currency);

        step("Skip Popup");
        whooshSearchPage.skipPopup();

        step("Select origin station");
        whooshSearchPage.selectOrigin(originStation);

        step("Select destination station");
        whooshSearchPage.selectDestination(destinationStation);

        step("Toggle trip type to "+tripType);
        whooshSearchPage.selectRoundTripToggle(tripType);

        step("Select date");
        whooshSearchPage.selectDate(departureDate);

        step("Add passenger");
        whooshSearchPage.addPassenger(adultPassengers,infantPassengers);

        step("Search whoosh");
        whooshSearchPage.searchTrain();

        step("Skip Popup");
        whooshSearchPage.skipPopup();

        step("Select departure whoosh");
        whooshSearchResultPage.selectWhoosh();

        step("Fill contact data");
        whooshBookingPage.fillGuestDetail(email);

        step("Fill passenger detail");
        whooshBookingPage.fillPassengerDetail(adultPassengers,infantPassengers);

        step("Continue to 2nd half");
        whooshBookingPage.continueTo2ndHalf();

        step("Continue to payment");
        whooshBookingPage.continueToPayment();

        step("Check order ID");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",!orderID.isBlank(),is(true));

        step("Open payment method");
        paymentPage.openPaymentMethod();

        if(Configs.ENVIRONMENT == Environment.PROD){
            step("Select virtual account as payment method");
            paymentPage.selectPaymentMethod("BCA Virtual Account");
        }else {
            step("select sakaku as payment method");
            paymentPage.selectPaymentMethod("Sakuku");
        }

        step("get guest and whoosh data from payment page");
        TrainData data = paymentPage.getTrainGuestInfoPaymentPage();
        data.orderId = orderID;

        step("get total price");
        String totalPrice = paymentPage.getTotalPriceFirstPage();

        step("Submit payment");
        paymentPage.clickPayButton();

        if(Configs.ENVIRONMENT == Environment.PROD){
            step("Go to the order list");
            paymentPage.seeOrderList();

            step("Open order");
            orderListPage.openOrderDetailWaitingForPayment(orderID);
        }else {
            step("open issued order");
            Assertion.assertThat("Payment Successful and Thank You page is shown", paymentPage.isThankYouPageShown(), is(true));
            orderListPage.openIssuedTicketViaDeeplink(orderID);
            
            Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.getETicketLabel(), either(is("Your e-ticket is here")).or(is("E-tiketmu di sini")));
        }

        step("check whoosh and passenger detail correct");
        orderDetailPage.checkTrainDetails(data);
        orderDetailPage.checkPassengerDetailsTrain(data);

        step("verify total payment correct");
        Assertion.assertThat("is total payment from payment is equals to order menu",orderDetailPage.verifyTotalPaymentTrain(totalPrice),is(true));
    }

    @Author(names = {"Ashish"})
    @Module(name = "RoundTripInsurance")
    @Tribe(name = "Whoosh")
    @Vertical(name = "GroundTransport")
    @Test
    public void testWhooshCompleteBookingRoundTripInsuranceGuest(){
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testWhooshCompleteBookingRoundTripInsuranceGuest");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        WhooshSearchPage<WebDriver> whooshSearchPage = WhooshSearchPage.getInstance(session);
        WhooshSearchResultPage<WebDriver> whooshSearchResultPage = WhooshSearchResultPage.getInstance(session);
        WhooshBookingPage<WebDriver> whooshBookingPage = WhooshBookingPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);

        String tripType = param.get("tripType");
        String currency = param.get("currency");
        String originStation = param.get("originStation");
        String destinationStation = param.get("destinationStation");
        String dateRange = param.get("dateRange");

        int duration = Integer.parseInt(param.get("duration"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int infantPassengers = Integer.parseInt(param.get("infant"));

        TrainAndWhooshGeneralData trainAndWhooshGeneralData = new TrainAndWhooshGeneralData(originStation, destinationStation,adultPassengers,adultPassengers,infantPassengers,tripType,dateRange,duration);
        String departureDate = trainAndWhooshGeneralData.departureDateLong;
        String returnDate = trainAndWhooshGeneralData.returnDateLong;

        String email = param.get("email");

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Logout via deeplink");
        homePage.logoutViaDeeplink();

        step("Change currency to 'IDR'");
        homePage.changeCurrency("IDR","");

        step("Open whoosh Landing Page");
        homePage.openVerticalPageDeeplink("Whoosh", "", currency);

        step("Skip Popup");
        whooshSearchPage.skipPopup();

        step("Select origin station");
        whooshSearchPage.selectOrigin(originStation);

        step("Select destination station");
        whooshSearchPage.selectDestination(destinationStation);

        step("Toggle tripType { " + tripType +" }");
        whooshSearchPage.selectRoundTripToggle(tripType);

        step("Select date");
        whooshSearchPage.selectDate(departureDate,returnDate);

        step("Add passenger");
        whooshSearchPage.addPassenger(adultPassengers,infantPassengers);

        step("Search whoosh");
        whooshSearchPage.searchTrain();

        step("Skip Popup");
        whooshSearchPage.skipPopup();

        step("Select departure whoosh");
        trainAndWhooshGeneralData.srpFirstCardPricePerPaxDepart = whooshSearchResultPage.selectWhooshAndGetPricePerPax();

        step("Select return whoosh");
        trainAndWhooshGeneralData.srpFirstCardPricePerPaxReturn = whooshSearchResultPage.selectWhooshAndGetPricePerPax();

        step("Fill contact data");
        whooshBookingPage.fillGuestDetail(email);

        step("Fill passenger detail");
        whooshBookingPage.fillPassengerDetail(adultPassengers,infantPassengers);

        step("Continue to 2nd half");
        whooshBookingPage.continueTo2ndHalf();

        step("Select insurance");
        trainAndWhooshGeneralData.insuranceGeneralData = whooshBookingPage.selectInsurance();

        step("Open price detail");
        whooshBookingPage.openPriceDetail(tripType);

        step("verify selected insurance title { " + trainAndWhooshGeneralData.insuranceGeneralData.insuranceTitle + " } with price: " + trainAndWhooshGeneralData.insuranceGeneralData.insurancePrice + " and adult price and infant price");
        Assertion.assertThat("is insurance selected displayed in price detail", whooshBookingPage.verifyGeneralDataPriceDetail(trainAndWhooshGeneralData), is(true));

        step("verify price calculation");
        Assertion.assertThat("is calculation of prices are correct and match with total price",whooshBookingPage.isPriceSumCorrect(),is(true));

        step("close price detail");
        whooshBookingPage.closePriceDetail();

        step("Continue to payment");
        whooshBookingPage.continueToPayment();

        step("Check order ID");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id that is {" + orderID + " }",!orderID.isBlank(),is(true));
    }

    @Author(names = {"Ashish"})
    @Module(name = "Whoosh")
    @Tribe(name = "NFT")
    @Vertical(name = "GroundTransport")
    @Test
    public void Whoosh_testWhooshCompleteBookingRoundTripInsuranceLogin(){
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("Whoosh_testWhooshCompleteBookingRoundTripInsuranceLogin");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        WhooshSearchPage<WebDriver> whooshSearchPage = WhooshSearchPage.getInstance(session);
        WhooshSearchResultPage<WebDriver> whooshSearchResultPage = WhooshSearchResultPage.getInstance(session);
        WhooshBookingPage<WebDriver> whooshBookingPage = WhooshBookingPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);

        String username = param.get("username");
        String password = param.get("password");
        String tripType = param.get("tripType");
        String currency = param.get("currency");
        String originStation = param.get("originStation");
        String destinationStation = param.get("destinationStation");
        String dateRange = param.get("dateRange");

        int duration = Integer.parseInt(param.get("duration"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int infantPassengers = Integer.parseInt(param.get("infant"));

        TrainAndWhooshGeneralData trainAndWhooshGeneralData = new TrainAndWhooshGeneralData(originStation, destinationStation,adultPassengers,adultPassengers,infantPassengers,tripType,dateRange,duration);
        String departureDate = trainAndWhooshGeneralData.departureDateLong;
        String returnDate = trainAndWhooshGeneralData.returnDateLong;

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink (if needed) and change currency to " + currency);
        homePage.loginIfLoggedOutAndChangeCurrencyViaDeeplink(username, password, currency);

        step("Open whoosh Landing Page");
        homePage.openVerticalPageDeeplink("Whoosh", "", currency);

        step("Skip Popup");
        whooshSearchPage.skipPopup();

        step("Select origin station");
        whooshSearchPage.selectOrigin(originStation);

        step("Select destination station");
        whooshSearchPage.selectDestination(destinationStation);

        step("Toggle tripType { " + tripType +" }");
        whooshSearchPage.selectRoundTripToggle(tripType);

        step("Select date");
        whooshSearchPage.selectDate(departureDate,returnDate);

        step("Add passenger");
        whooshSearchPage.addPassenger(adultPassengers,infantPassengers);

        step("Search whoosh");
        whooshSearchPage.searchTrain();

        step("Skip Popup");
        whooshSearchPage.skipPopup();

        step("Select departure whoosh");
        trainAndWhooshGeneralData.srpFirstCardPricePerPaxDepart = whooshSearchResultPage.selectWhooshAndGetPricePerPax();

        step("Select return whoosh");
        trainAndWhooshGeneralData.srpFirstCardPricePerPaxReturn = whooshSearchResultPage.selectWhooshAndGetPricePerPax();

        step("Fill passenger detail");
        whooshBookingPage.fillPassengerDetail(adultPassengers,infantPassengers);

        step("Continue to 2nd half");
        whooshBookingPage.continueTo2ndHalf();

        step("Select insurance");
        trainAndWhooshGeneralData.insuranceGeneralData = whooshBookingPage.selectInsurance();

        step("Open price detail");
        whooshBookingPage.openPriceDetail(tripType);

        step("verify selected insurance title { " + trainAndWhooshGeneralData.insuranceGeneralData.insuranceTitle + " } with price: " + trainAndWhooshGeneralData.insuranceGeneralData.insurancePrice + " and adult price and infant price");
        Assertion.assertThat("is insurance selected displayed in price detail", whooshBookingPage.verifyGeneralDataPriceDetail(trainAndWhooshGeneralData), is(true));

        step("verify price calculation");
        Assertion.assertThat("is calculation of prices are correct and match with total price",whooshBookingPage.isPriceSumCorrect(),is(true));

        step("Continue to payment");
        whooshBookingPage.continueToPayment();

        step("Check order ID");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id that is {" + orderID + " }",!orderID.isBlank(),is(true));

        step("verify trip title and header");
        paymentPage.openPriceDetail();
        if (Configs.PLATFORM == Platform.ANDROID) webViewPage.switchToWebViewUrl("payment");
        Assertion.assertThat("trip title is displayed", paymentPage.getTripTitle(true, originStation, destinationStation), is(true));
        Assertion.assertThat("trip header is displayed", paymentPage.getTripHeader(true), is(true));
    }

    @Author(names = {"Ashish"})
    @Module(name = "onlineCheckIn")
    @Tribe(name = "Whoosh")
    @Vertical(name = "GroundTransport")
    @Test
    public void testWhooshUserAbleToOnlineCheckInGuest() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testWhooshUserAbleToOnlineCheckInGuest");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        WhooshSearchPage<WebDriver> whooshSearchPage = WhooshSearchPage.getInstance(session);
        WhooshSearchResultPage<WebDriver> whooshSearchResultPage = WhooshSearchResultPage.getInstance(session);
        WhooshBookingPage<WebDriver> whooshBookingPage = WhooshBookingPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);

        String email = param.get("email");
        String originCity = param.get("originCity");
        String destinationCity = param.get("destinationCity");
        String currency = param.get("currency");
        String dateRange = param.get("dateRange");
        String tripType = param.get("tripType");

        int adultPassengers = Integer.parseInt(param.get("adult"));
        int infantPassengers = Integer.parseInt(param.get("infant"));

        String departureDate = CoreFunctions.addToLongRandomDate(dateRange);

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Logout via deeplink");
        homePage.logoutViaDeeplink();

        step("Change currency to 'IDR'");
        homePage.changeCurrency("IDR","");

        step("Get App Language");
        String language = homePage.getLanguageApp();

        step("Open whoosh Landing Page");
        homePage.openVerticalPageDeeplink("Whoosh", language, currency);

        step("Skip Popup");
        whooshSearchPage.skipPopup();

        step("Select origin station");
        whooshSearchPage.selectOrigin(originCity);

        step("Select destination station");
        whooshSearchPage.selectDestination(destinationCity);

        step("Add passenger");
        whooshSearchPage.addPassenger(adultPassengers,infantPassengers);

        step("Toggle tripType { " + tripType +" }");
        whooshSearchPage.selectRoundTripToggle(tripType);

        step("Select date");
        whooshSearchPage.selectDate(departureDate);

        step("Search whoosh");
        whooshSearchPage.searchTrain();

        step("Skip Popup");
        whooshSearchPage.skipPopup();

        step("Select departure whoosh");
        whooshSearchResultPage.selectWhoosh();

        step("Fill contact data");
        whooshBookingPage.fillGuestDetail(email);

        step("Fill passenger detail");
        TrainData passengerNamesAndId = whooshBookingPage.fillPassengerDetail(adultPassengers,infantPassengers);

        step("Continue to 2nd half");
        whooshBookingPage.continueTo2ndHalf();
        TrainData dataFromBookingPage = whooshBookingPage.getDetailsWhooshAndPassenger(language);
        dataFromBookingPage.adult = adultPassengers;
        dataFromBookingPage.infant = infantPassengers;
        dataFromBookingPage.guestList = passengerNamesAndId.guestList;
        dataFromBookingPage.idNumberList = passengerNamesAndId.idNumberList;

        step("Continue to payment");
        whooshBookingPage.continueToPayment();

        step("Check order ID");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",!orderID.isBlank(),is(true));

        step("Open payment method");
        paymentPage.openPaymentMethod();

        step("select sakaku as payment method");
        paymentPage.selectPaymentMethod("Sakuku");

        step("Submit payment");
        paymentPage.clickPayButton();
        Assertion.assertThat("Payment Successful and Thank You page is shown", paymentPage.isThankYouPageShown(), is(true));

        step("open issued order");
        orderListPage.openIssuedTicketViaDeeplink(orderID);
        
        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.getETicketLabel(), either(is("Your e-ticket is here")).or(is("E-tiketmu di sini")));

        step("click on See e-boarding pass");
        Assertion.assertThat("See e-boarding pass button is visible",orderDetailPage.clickSeeBoardingPass(),is(true));

        step("passenger data and whoosh data correct on boarding pass");
        Assertion.assertThat("passenger data and whoosh data correct on boarding pass ",orderDetailPage.verifyBoardingPassDetails(dataFromBookingPage),is(true));
    }

    @Author(names = {"Ashish"})
    @Module(name = "onlineCheckIn")
    @Tribe(name = "Whoosh")
    @Vertical(name = "GroundTransport")
    @Test
    public void testWhooshUserAbleToOnlineCheckInLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testWhooshUserAbleToOnlineCheckInLogin");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        WhooshSearchPage<WebDriver> whooshSearchPage = WhooshSearchPage.getInstance(session);
        WhooshSearchResultPage<WebDriver> whooshSearchResultPage = WhooshSearchResultPage.getInstance(session);
        WhooshBookingPage<WebDriver> whooshBookingPage = WhooshBookingPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String originCity = param.get("originCity");
        String destinationCity = param.get("destinationCity");
        String currency = param.get("currency");
        String dateRange = param.get("dateRange");
        String tripType = param.get("tripType");

        int adultPassengers = Integer.parseInt(param.get("adult"));
        int infantPassengers = Integer.parseInt(param.get("infant"));

        String departureDate = CoreFunctions.addToLongRandomDate(dateRange);

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink (if needed) and change currency to " + currency);
        homePage.loginWithSpecificAccountViaDeeplink(username, password, currency);

        step("Get App Language");
        String language = homePage.getLanguageApp();

        step("Open whoosh Landing Page");
        homePage.openVerticalPageDeeplink("Whoosh", language, currency);

        step("Skip Popup");
        whooshSearchPage.skipPopup();

        step("Select origin station");
        whooshSearchPage.selectOrigin(originCity);

        step("Select destination station");
        whooshSearchPage.selectDestination(destinationCity);

        step("Add passenger");
        whooshSearchPage.addPassenger(adultPassengers,infantPassengers);

        step("Toggle tripType { " + tripType +" }");
        whooshSearchPage.selectRoundTripToggle(tripType);

        step("Select date");
        whooshSearchPage.selectDate(departureDate);

        step("Search whoosh");
        whooshSearchPage.searchTrain();

        step("Skip Popup");
        whooshSearchPage.skipPopup();

        step("Select departure whoosh");
        whooshSearchResultPage.selectWhoosh();

        step("Fill passenger detail");
        TrainData passengerNamesAndId = whooshBookingPage.fillPassengerDetail(adultPassengers,infantPassengers);

        step("Continue to 2nd half");
        whooshBookingPage.continueTo2ndHalf();
        TrainData dataFromBookingPage = whooshBookingPage.getDetailsWhooshAndPassenger(language);
        dataFromBookingPage.adult = adultPassengers;
        dataFromBookingPage.infant = infantPassengers;
        dataFromBookingPage.guestList = passengerNamesAndId.guestList;
        dataFromBookingPage.idNumberList = passengerNamesAndId.idNumberList;

        step("Continue to payment");
        whooshBookingPage.continueToPayment();

        step("Check order ID");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",!orderID.isBlank(),is(true));

        step("Open payment method");
        paymentPage.openPaymentMethod();

        step("select sakaku as payment method");
        paymentPage.selectPaymentMethod("Sakuku");

        step("Submit payment");
        paymentPage.clickPayButton();
        Assertion.assertThat("Payment Successful and Thank You page is shown", paymentPage.isThankYouPageShown(), is(true));

        step("open issued order");
        orderListPage.openIssuedTicketViaDeeplink(orderID);
        
        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.getETicketLabel(), either(is("Your e-ticket is here")).or(is("E-tiketmu di sini")));

        step("click on See e-boarding pass");
        Assertion.assertThat("See e-boarding pass button is visible",orderDetailPage.clickSeeBoardingPass(),is(true));

        step("passenger data and whoosh data correct on boarding pass");
        Assertion.assertThat("passenger data and whoosh data correct on boarding pass ",orderDetailPage.verifyBoardingPassDetails(dataFromBookingPage),is(true));
    }
    @Author(names = {"Riya Bajpai"})
    @Module(name = "Whoosh")
    @Tribe(name = "NFT")
    @Vertical(name = "GroundTransport")
    @Test
    public void testWhooshERefundGuest() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testWhooshERefundGuest");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<?> loginPage = LoginPage.getInstance(session);
        WhooshSearchPage<WebDriver> whooshSearchPage = WhooshSearchPage.getInstance(session);
        WhooshSearchResultPage<WebDriver> whooshSearchResultPage = WhooshSearchResultPage.getInstance(session);
        WhooshBookingPage<WebDriver> whooshBookingPage = WhooshBookingPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        RefundPage<WebDriver> refundPage = RefundPage.getInstance(session);

        String originCity = param.get("originCity");
        String destinationCity = param.get("destinationCity");
        String currency = param.get("currency");
        String dateRange = param.get("dateRange");

        String username = param.get("username");
        String password = param.get("password");

        int adultPassengers = Integer.parseInt(param.get("adult"));
        int infantPassengers = Integer.parseInt(param.get("infant"));

        String departureDate = CoreFunctions.addToLongRandomDate(dateRange);

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Logout via deeplink");
        homePage.logoutViaDeeplink();

       step("Change currency to 'IDR'");
       homePage.changeCurrency("IDR", "");

        step("Get App Language");
        String language = homePage.getLanguageApp();

        step("Open whoosh Landing Page");
        homePage.openVerticalPageDeeplink("Whoosh", language, currency);

        step("Skip Popup");
        whooshSearchPage.skipPopup();

        step("Select origin station");
        whooshSearchPage.selectOrigin(originCity);

        step("Select destination station");
        whooshSearchPage.selectDestination(destinationCity);

        step("Add passenger");
        whooshSearchPage.addPassenger(adultPassengers, infantPassengers);

        step("Select date");
        whooshSearchPage.selectDate(departureDate);

        step("Search whoosh");
        whooshSearchPage.searchTrain();

        step("Skip Popup");
        whooshSearchPage.skipPopup();

        step("Select departure whoosh");
        whooshSearchResultPage.selectWhoosh();

        step("Fill contact data");
        whooshBookingPage.fillGuestDetail(username);

        step("Fill passenger detail");
        TrainData passengerNamesAndId = whooshBookingPage.fillPassengerDetail(adultPassengers, infantPassengers);

        step("Continue to 2nd half");
        whooshBookingPage.continueTo2ndHalf();
        TrainData dataFromBookingPage = whooshBookingPage.getDetailsWhooshAndPassenger(language);
        dataFromBookingPage.adult = adultPassengers;
        dataFromBookingPage.infant = infantPassengers;
        dataFromBookingPage.guestList = passengerNamesAndId.guestList;
        dataFromBookingPage.idNumberList = passengerNamesAndId.idNumberList;

        step("Continue to payment");
        whooshBookingPage.continueToPayment();

        step("Check order ID");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id", !orderID.isBlank(), is(true));

        step("Open payment method");
        paymentPage.openPaymentMethod();

        step("select sakaku as payment method");
        paymentPage.selectPaymentMethod("Sakuku");

        step("Submit payment");
        paymentPage.clickPayButton();
        Assertion.assertThat("Payment Successful and Thank You page is shown", paymentPage.isThankYouPageShown(), is(true));

        step("open issued order");
        orderListPage.openIssuedTicketViaDeeplink(orderID);
        orderDetailPage.hidePromoBannerIfPresent();
        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.getETicketLabel(), either(is("Your e-ticket is here")).or(is("E-tiketmu di sini")));

        step("Click Refund");
        orderDetailPage.clickRefund();

        step("Login in Now");
        orderDetailPage.clickLoginNow();

        step("Login");
        loginPage.loginWithNative("email", username, password, true);

        step("Click Refund");
        orderDetailPage.hidePromoBannerIfPresent();
        if(Configs.PLATFORM == Platform.ANDROID) orderDetailPage.clickRefund();

        step("Choose Personal Reason");
        refundPage.choosePersonalReason();

        step("Continue");
        refundPage.clickContinueButton();

        step("Submit Refund");
        refundPage.submitRefund();

        step("Complete Refund Flow");
        refundPage.getRefundDetails();

        step("Check at least 3 steps completed");
        Assertion.assertThat("Sent Request step should be completed", refundPage.isRefundSentToTiketCheckDisplayed(), is(true));
        Assertion.assertThat("Review Refund step should be completed", refundPage.isRefundReviewedByTiketCheckDisplayed(), is(true));
        Assertion.assertThat("Verify Refund Detail step should be completed", refundPage.isRefundVerifiedByTiketCheckDisplayed(), is(true));

    }

    @Author(names = {"Prachi Upadhyay"})
    @Module(name = "Whoosh")
    @Tribe(name = "NFT")
    @Vertical(name = "GroundTransport")
    @Test
    public void testWhooshERefund() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testWhooshERefund");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        WhooshSearchPage<WebDriver> whooshSearchPage = WhooshSearchPage.getInstance(session);
        WhooshSearchResultPage<WebDriver> whooshSearchResultPage = WhooshSearchResultPage.getInstance(session);
        WhooshBookingPage<WebDriver> whooshBookingPage = WhooshBookingPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        RefundPage<WebDriver> refundPage = RefundPage.getInstance(session);

        String originCity = param.get("originCity");
        String destinationCity = param.get("destinationCity");
        String currency = param.get("currency");
        String dateRange = param.get("dateRange");

        String username = param.get("username");
        String password = param.get("password");

        int adultPassengers = Integer.parseInt(param.get("adult"));
        int infantPassengers = Integer.parseInt(param.get("infant"));

        String departureDate = CoreFunctions.addToLongRandomDate(dateRange);

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink (if needed) and change currency to " + currency);
        homePage.loginIfLoggedOutAndChangeCurrencyViaDeeplink(username, password, currency);

        step("Get App Language");
        String language = homePage.getLanguageApp();

        step("Open whoosh Landing Page");
        homePage.openVerticalPageDeeplink("Whoosh", language, currency);

        step("Skip Popup");
        whooshSearchPage.skipPopup();

        step("Select origin station");
        whooshSearchPage.selectOrigin(originCity);

        step("Select destination station");
        whooshSearchPage.selectDestination(destinationCity);

        step("Add passenger");
        whooshSearchPage.addPassenger(adultPassengers, infantPassengers);

        step("Select date");
        whooshSearchPage.selectDate(departureDate);

        step("Search whoosh");
        whooshSearchPage.searchTrain();

        step("Skip Popup");
        whooshSearchPage.skipPopup();

        step("Select departure whoosh");
        whooshSearchResultPage.selectWhoosh();

        step("Fill contact data");
        whooshBookingPage.fillGuestDetail(username);

        step("Fill passenger detail");
        TrainData passengerNamesAndId = whooshBookingPage.fillPassengerDetail(adultPassengers, infantPassengers);

        step("Continue to 2nd half");
        whooshBookingPage.continueTo2ndHalf();
        TrainData dataFromBookingPage = whooshBookingPage.getDetailsWhooshAndPassenger(language);
        dataFromBookingPage.adult = adultPassengers;
        dataFromBookingPage.infant = infantPassengers;
        dataFromBookingPage.guestList = passengerNamesAndId.guestList;
        dataFromBookingPage.idNumberList = passengerNamesAndId.idNumberList;

        step("Continue to payment");
        whooshBookingPage.continueToPayment();

        step("Check order ID");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id", !orderID.isBlank(), is(true));

        step("Open payment method");
        paymentPage.openPaymentMethod();

        step("select sakaku as payment method");
        paymentPage.selectPaymentMethod("Sakuku");

        step("Submit payment");
        paymentPage.clickPayButton();
        Assertion.assertThat("Payment Successful and Thank You page is shown", paymentPage.isThankYouPageShown(), is(true));

        step("open issued order");
        orderListPage.openOrderDetailViaDeeplink(orderID);
        
        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.getETicketLabel(), either(is("Your e-ticket is here")).or(is("E-tiketmu di sini")));

        step("Click Refund");
        orderDetailPage.clickRefund();

        step("Choose Personal Reason");
        refundPage.choosePersonalReason();

        step("Continue");
        refundPage.clickContinueButton();

        step("Change refund method to Blibli Points");
        refundPage.changeRefundMethodToBlibliPoints();

        step("Submit Refund");
        refundPage.submitRefund();

        step("Complete Refund Flow");
        refundPage.getRefundDetails();

        step("Check at least 3 steps completed");
        Assertion.assertThat("Sent Request step should be completed", refundPage.isRefundSentToTiketCheckDisplayed(), is(true));
        Assertion.assertThat("Review Refund step should be completed", refundPage.isRefundReviewedByTiketCheckDisplayed(), is(true));
        Assertion.assertThat("Verify Refund Detail step should be completed", refundPage.isRefundVerifiedByTiketCheckDisplayed(), is(true));

    }
}
