package com.tiket.test.platform.eiffel.loyalty;

import com.tiket.apiPage.HotelApi;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.apiPage.*;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Session;
import com.tiket.model.SortItem;
import com.tiket.page.accommodation.hotel.HotelPage;
import com.tiket.page.accommodation.hotel.bookingformpage.BookingFormPage;
import com.tiket.page.accommodation.hotel.detailpage.DetailPage;
import com.tiket.page.accommodation.hotel.landingpage.LandingPage;
import com.tiket.page.accommodation.hotel.reviewpage.ReviewPage;
import com.tiket.page.accommodation.hotel.roomlistpage.RoomListPage;
import com.tiket.page.account.AccountPage;
import com.tiket.page.bus.BusPage;
import com.tiket.page.carrental.CarRentalPage;
import com.tiket.page.cartransfer.CarTransferPage;
import com.tiket.page.cartransfer.bookingform.CarTransferBookingPage;
import com.tiket.page.cartransfer.searchpage.CarTransferSearchPage;
import com.tiket.page.cartransfer.searchresultpage.CarTransferSearchResultPage;
import com.tiket.page.ferry.FerryPage;
import com.tiket.page.flight.FlightPage;
import com.tiket.page.home.HomePage;
import com.tiket.page.login.LoginPage;
import com.tiket.page.loyalty.my_review.MyReviewPage;
import com.tiket.page.loyalty.ulp_tiket.UlpPage;
import com.tiket.page.order.orderdetail.OrderDetailPage;
import com.tiket.page.order.orderlist.OrderListPage;
import com.tiket.page.payment.PaymentPage;
import com.tiket.page.setting.SettingPage;
import com.tiket.page.train.TrainPage;
import com.tiket.page.train.bookingform.TrainBookingPage;
import com.tiket.page.train.searchpage.TrainSearchPage;
import com.tiket.page.train.searchresultpage.TrainSearchResultPage;
import com.tiket.page.ttd.TTDPage;
import com.tiket.page.ttd.landingpage.TTDLandingPage;
import com.tiket.page.ttd.productdetailpage.TTDProductDetailPage;
import com.tiket.page.ttd.searchpage.TTDSearchPage;
import com.tiket.page.ttd.searchresultpage.TTDSearchResultPage;
import com.tiket.page.common.webview.WebViewPage;
import com.tiket.pojo.accommodation.hotel.HotelAutoCompleteResponse;
import com.tiket.pojo.accommodation.hotel.payathotel.PayAtHotelRoom;
import com.tiket.pojo.bus.BusCheckoutResponse;
import com.tiket.pojo.loyalty.PendingReviewResponse;
import com.tiket.pojo.member.RegisterNewAccountData;
import com.tiket.pojo.train.CarTransferInternalOrderDetail;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.web.page.common.mission.MissionPageWeb;
import com.tiket.web.page.platform.eiffel.loyalty.LoyaltyPointPageWeb;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static com.tiket.core.CoreFunctions.*;
import static com.tiket.model.Language.ENGLISH;
import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;

public class LoyaltyCoreTest extends BaseTest{

    @Author(names = {"Yazid"})
    @Module(name = "LoyaltyCoreFull")
    @Tribe(name = "LoyaltyCore")
    @Vertical(name = "Platform")
    @Test
    public void testPayWithFullTiketPointsBusShuttle() throws ExecutionException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testPayWithFullTiketPointsBusShuttle");
        BaseTest.paramsThreadLocal.set(param);

        BusPage<WebDriver> busPage = BusPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        LoyaltyApi<?> loyaltyApi = new LoyaltyApi<>(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);

        String loginType = param.get("loginType");
        String email = param.get("username");
        String password = param.get("password");
        String currency = "IDR";
        String originId = param.get("originId");
        String destinationId = param.get("destinationId");
        String departureDate = generateRandomDate(param.get("rangeAddDate"));
        String pdProcessType = param.get("pdProcessType");
        int pdRatio = Integer.parseInt(param.get("pdRatio"));

        step("Create waiting payment order for Bus & Shuttle");
        BusCheckoutResponse apiData = busPage.createWaitingPaymentOrderApi(email, password, originId, destinationId, departureDate);
        String orderId = apiData.data.order.orderId;
        String totalPrice = String.valueOf(apiData.data.totalFare);

        step("user open app");
        homePage.gotoHome();
        loginPage.loginAsGuest();

        step("user login using "+email+" account");
        loginPage.loginWith(loginType, email, password);

        step("Change currency");
        homePage.changeCurrency(currency,email);

        step("get tiket points");
        String tiketPoints = homePage.getActiveTiketPointsInRewardsPage();

        step("check if order can be book using full tiket points, else inject point");
        tiketPoints = loyaltyApi.injectTiketPointsBasedOnCondition( Integer.parseInt(totalPrice), pdProcessType, pdRatio, email,tiketPoints, "full");

        step("Open Your Orders page");
        homePage.openMyOrderPage();

        step("open payment page");
        orderListPage.openOrderCompletePayment(orderId);
        String appOrderId = paymentPage.checkOrderID();
        Assertion.assertThat("is open correct payment order", appOrderId.contains(orderId), is(true));

        step("get total price");
        double appTotalPrice = CoreFunctions.convertPrice(paymentPage.getTotalPrice());

        step("Toggle pay with loyalty");
        paymentPage.payWithTiketPoint();
        Assertion.assertThat("total price changes to gratis/free",paymentPage.checkTotalPriceChangesToFree(),is(true));

        step("Submit payment");
        paymentPage.clickPayButton();

        step("Open order list page");
        paymentPage.seeOrderList(orderId);

        step("get deducted tiket points");
        String newTiketPoints = homePage.getActiveTiketPointsInRewardsPage();
        int tiketPointsActual = Integer.parseInt(newTiketPoints.replaceAll("[^0-9]", ""));
        int calculatedTiketPoints = Integer.parseInt(tiketPoints) - (int) appTotalPrice;

        step("Verify loyalty point is " + tiketPointsActual + " and should be equal to " + calculatedTiketPoints);
        Assertion.assertThat("Verify loyalty point value is correct", tiketPointsActual,equalTo(calculatedTiketPoints));
    }

    @Author(names = {"Yazid"})
    @Module(name = "LoyaltyCoreHalf")
    @Tribe(name = "LoyaltyCore")
    @Vertical(name = "Platform")
    @Test
    public void testPayWithHalfTiketPointsBusShuttle() throws ExecutionException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testPayWithHalfTiketPointsBusShuttle");
        BaseTest.paramsThreadLocal.set(param);

        BusPage<WebDriver> busPage = BusPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        LoyaltyApi<?> loyaltyApi = new LoyaltyApi<>(session);

        String loginType = param.get("loginType");
        String email = param.get("username");
        String password = param.get("password");
        String currency = "IDR";
        String originId = param.get("originId");
        String destinationId = param.get("destinationId");
        String departureDate = generateRandomDate(param.get("rangeAddDate"));
        String pdProcessType = param.get("pdProcessType");
        int pdRatio = Integer.parseInt(param.get("pdRatio"));

        step("Create waiting payment order for Bus & Shuttle");
        BusCheckoutResponse apiData = busPage.createWaitingPaymentOrderApi(email, password, originId, destinationId, departureDate);
        String orderId = apiData.data.order.orderId;
        String totalPrice = String.valueOf(apiData.data.totalFare);
        int halfPoint = Integer.parseInt(totalPrice) / 2;

        step("user open app");
        homePage.gotoHome();
        loginPage.loginAsGuest();

        step("user login using "+email+" account");
        loginPage.loginWith(loginType, email, password);

        step("Change currency");
        homePage.changeCurrency(currency,email);

        step("get tiket points");
        String tiketPoints = homePage.getActiveTiketPointsInRewardsPage();

        //inject tiket point
        step("check if order can be book using half tiket points, else delete or inject point");
        tiketPoints = loyaltyApi.injectTiketPointsBasedOnCondition( halfPoint, pdProcessType, pdRatio, email,tiketPoints,"half");

        step("Open Your Orders page");
        homePage.openMyOrderPage();

        step("open payment page");
        orderListPage.openOrderCompletePayment(orderId);
        String appOrderId = paymentPage.checkOrderID();
        Assertion.assertThat("is open correct payment order", appOrderId.contains(orderId), is(true));

        step("choose ATM payment method");
        paymentPage.openPaymentMethod();
        paymentPage.selectPaymentMethod("ATM");

        step("Toggle pay with loyalty");
        paymentPage.payWithTiketPoint();

        step("Submit payment");
        paymentPage.clickPayButton();

        step("Auto paid order via api");
        paymentPage.postAutoPaidOrder();

        step("Open order list page");
        paymentPage.seeOrderList(orderId);

        step("get deducted tiket points");
        String newTiketPoints = homePage.getActiveTiketPointsInRewardsPage();
        int tiketPointsActual = Integer.parseInt(newTiketPoints.replaceAll("[^0-9]", ""));

        step("Verify loyalty point is 0");
        Assertion.assertThat("Verify loyalty point value is 0",tiketPointsActual,is(0));
    }

    @Author(names = {"Dendi"})
    @Module(name = "LoyaltyCoreHalf")
    @Tribe(name = "LoyaltyCore")
    @Vertical(name = "Platform")
    @Test
    public void testPayTrainWithHalfTiketPoints() throws InterruptedException, ExecutionException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testPayTrainWithHalfTiketPoints");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TrainSearchPage<WebDriver> trainSearchPage = TrainSearchPage.getInstance(session);
        TrainSearchResultPage<WebDriver> trainSearchResultPage = TrainSearchResultPage.getInstance(session);
        TrainBookingPage<WebDriver> trainBookingPage = TrainBookingPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        LoyaltyApi<?> loyaltyApi = new LoyaltyApi<>(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);

        String pdProcessType = param.get("pdProcessType");
        int pdPoint = Integer.parseInt(param.get("pdPoint"));
        int pdRation = Integer.parseInt(param.get("pdRatio"));
        String tripType = param.get("tripType");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int infantPassengers = Integer.parseInt(param.get("infant"));

        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate,daysToDepartureDate);

        String username = param.get("username");
        String password = param.get("password");
        String currency = "IDR";
        String originStation = param.get("originStation");
        String destinationStation = param.get("destinationStation");

        step("user open app");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink and change currency to " + currency);
        homePage.loginWithSpecificAccountViaDeeplink(username, password, currency);

        step("get tiket points");
        String tiketPoints = homePage.getActiveTiketPointsInRewardsPage();

        //inject tiket point
        step("inject tiket point based on condition");
        tiketPoints = loyaltyApi.injectTiketPointsBasedOnCondition( pdPoint, pdProcessType, pdRation, username,tiketPoints,"half");

        step("Open train page");
        homePage.openVerticalPageDeeplink("Trains","",currency);

        step("Select origin station");
        trainSearchPage.selectOrigin(originStation);

        step("Select destination station");
        trainSearchPage.selectDestination(destinationStation);

        step("Select trip type");
        trainSearchPage.toggleTripType(tripType);

        step("Select date");
        trainSearchPage.selectDate(departureDate);

        step("Add passenger");
        trainSearchPage.addPassenger(adultPassengers,infantPassengers);

        step("Search train");
        trainSearchPage.searchTrain();

        step("Skip popup");
        trainSearchPage.skipPopup();

        step("Select departure train");
        trainSearchResultPage.selectTrain();

        step("Fill passenger detail");
        trainBookingPage.fillPassengerDetail(adultPassengers,infantPassengers);

        step("Continue to 2nd half");
        trainBookingPage.continueTo2ndHalf();

        step("Continue to payment");
        trainBookingPage.continueToPayment();
        
        step("Check order ID & get total price");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",!orderID.isEmpty(),is(true));

        step("choose atm payment method");
        paymentPage.openPaymentMethod();
        paymentPage.selectPaymentMethod("ATM");

        step("Toggle pay with loyalty");
        paymentPage.payWithTiketPoint();

        step("Submit payment");
        paymentPage.clickPayButton();

        step("Auto paid order via api");
        paymentPage.postAutoPaidOrder();

        step("Open order list page");
        paymentPage.seeOrderList(orderID);

        step("get deducted tiket points");
        String newTiketPoints = homePage.getActiveTiketPointsInRewardsPage();
        int tiketPointsActual = Integer.parseInt(newTiketPoints.replaceAll("[^0-9]", ""));

        step("Verify loyalty point is 0");
        Assertion.assertThat("Verify loyalty point value is 0",tiketPointsActual,is(0));
    }

    @Author(names = {"Ashish"})
    @Module(name = "LoyaltyCoreHalf")
    @Tribe(name = "LoyaltyCore")
    @Vertical(name = "Platform")
    @Test
    public void testPayWithHalfTiketPointsCarTransfers() throws InterruptedException, ExecutionException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testPayWithHalfTiketPointsCarTransfers");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        LoyaltyApi<?> loyaltyApi = new LoyaltyApi<>(session);
        CarTransferApi<?> carTransferApi = new CarTransferApi<>(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);

        String pdProcessType = param.get("pdProcessType");
        int pdRation = Integer.parseInt(param.get("pdRation"));

        String loginMethod = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");

        String airport = param.get("airport");
        String destination = param.get("destination");
        String currency = param.get("currency");
        String type = param.get("type");
        String pickupTime = param.get("pickupTime").replace("/",":");
        int pax = Integer.parseInt(param.get("pax"));
        String rangeAddDate = param.get("rangeAddDate");

        step("Create car transfer waiting payment order for 1 passenger and 1 day via API");
        String issuedOrderID = carTransferApi.createWaitingPaymentOrderApi(username,password,airport,destination,type,rangeAddDate,pickupTime,pax);
        Assertion.assertThat("Car transfer booking via API is successful", issuedOrderID, not(blankOrNullString()));
        String grandTotal = String.valueOf((int)carTransferApi.getGrandTotal());
        int halfPoint = Integer.parseInt(grandTotal) / 2;

        step("user open app");
        homePage.gotoHome();

        step("user login as guest");
        loginPage.loginAsGuest();

        step("user login using "+username+" account");
        loginPage.loginWith(loginMethod, username, password);

        step("Change currency");
        homePage.changeCurrency(currency,username);

        step("get tiket points");
        String tiketPoints = homePage.getActiveTiketPointsInRewardsPage();

        //inject tiket point
        step("inject tiket point based on condition");
        tiketPoints = loyaltyApi.injectTiketPointsBasedOnCondition( halfPoint, pdProcessType, pdRation, username,tiketPoints,"half");

        step("Open Your Orders page");
        homePage.openMyOrderPage();

        step("open payment page");
        orderListPage.openOrderCompletePayment(issuedOrderID);
        String orderId = paymentPage.checkOrderID();
        Assertion.assertThat("is open correct payment order",orderId.contains(issuedOrderID),is(true));

        step("choose atm payment method");
        paymentPage.openPaymentMethod();
        paymentPage.selectPaymentMethod("ATM");

        step("Toggle pay with loyalty");
        paymentPage.payWithTiketPoint();

        step("Submit payment");
        paymentPage.clickPayButton();

        step("Auto paid order via api");
        paymentPage.postAutoPaidOrder();

        step("Open order list page");
        paymentPage.seeOrderList(issuedOrderID);

        step("get deducted tiket points");
        String newTiketPoints = homePage.getActiveTiketPointsInRewardsPage();
        int tiketPointsActual = Integer.parseInt(newTiketPoints.replaceAll("[^0-9]", ""));

        step("Verify loyalty point is 0");
        Assertion.assertThat("Verify loyalty point value is 0",tiketPointsActual,is(0));
    }

    @Author(names = {"Ashish"})
    @Module(name = "LoyaltyCoreFull")
    @Tribe(name = "LoyaltyCore")
    @Vertical(name = "Platform")
    @Test
    public void testPayWithFullTiketPointsCarRental() throws InterruptedException, ExecutionException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testPayWithFullTiketPointsCarRental");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        CarRentalPage<WebDriver> carRentalPage = CarRentalPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        LoyaltyApi<?> loyaltyApi = new LoyaltyApi<>(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);

        String pdProcessType = param.get("pdProcessType");
        int pdRation = Integer.parseInt(param.get("pdRation"));
        String currency = param.get("currency");

        String loginMethod = param.get("loginType");
        String username = param.get("username");
        String phoneNumber = param.get("phoneNumber");
        String password = param.get("password");

        String driverOption = param.get("driverOption");
        String location = param.get("location");
        String startDate = generateRandomDate(param.get("rangeAddDate"));
        String type = param.get("type");

        String pickupTestData = param.get("pickupTime");
        StringBuilder pickup = new StringBuilder(pickupTestData);
        pickup.setCharAt(2, ':');
        String pickupTime = pickup.toString();

        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String fullName = randomName() + " Autorder";
        String nameTitle = getRandomObject(salutationList);

        int rentalDuration = Integer.parseInt(param.get("rentalDuration"));

        step("Create car rental issued order for 1 passenger and 1 day via API");
        Boolean orderResult = carRentalPage.createProductTillPaymentApi(
                username, phoneNumber, password, driverOption, location,
                startDate, pickupTime, type, fullName, nameTitle,rentalDuration);
        Assertion.assertThat("Car rental booking via API is successful", orderResult, is(true));

        step("Get the data from issued order");
        String issuedOrderID = carRentalPage.issuedOrderData("orderId");
        CarTransferInternalOrderDetail carTransferInternalOrderDetail = orderDetailPage.getCarTransferInternalOrderdata(issuedOrderID);
        String grandTotal = String.valueOf(carTransferInternalOrderDetail.data.price);
        
        step("user open app");
        homePage.gotoHome();

        step("user login as guest");
        loginPage.loginAsGuest();

        step("user login using "+username+" account");
        loginPage.loginWith(loginMethod, username, password);

         step("Change currency");
        homePage.changeCurrency(currency,username);

        step("get tiket points");
        String tiketPoints = homePage.getActiveTiketPointsInRewardsPage();

        step("check if order can be book using full tiket points, else inject point");
        tiketPoints = loyaltyApi.injectTiketPointsBasedOnCondition( Integer.parseInt(grandTotal), pdProcessType, pdRation, username,tiketPoints,"full");
        
        step("Open Your Orders page");
        homePage.openMyOrderPage();

        step("open payment page");
        orderListPage.openOrderCompletePayment(issuedOrderID);
        String orderId = paymentPage.checkOrderID();
        Assertion.assertThat("is open correct payment order",orderId.contains(issuedOrderID),is(true));

        step("get total price");
        double appTotalPrice = CoreFunctions.convertPrice(paymentPage.getTotalPrice());

        step("Toggle pay with loyalty");
        paymentPage.payWithTiketPoint();
        Assertion.assertThat("total price changes to gratis/free",paymentPage.checkTotalPriceChangesToFree(),is(true));

        step("Submit payment");
        paymentPage.clickPayButton();

        step("Auto paid order via api");
        paymentPage.postAutoPaidOrder();

        step("Open order list page");
        paymentPage.seeOrderList(orderId);

        step("get deducted tiket points");
        String newTiketPoints = homePage.getActiveTiketPointsInRewardsPage();
        int tiketPointsActual = Integer.parseInt(newTiketPoints.replaceAll("[^0-9]", ""));
        int calculatedTiketPoints = Integer.parseInt(tiketPoints) - (int) appTotalPrice;

        step("Verify loyalty point is " + tiketPointsActual + " and should be equal to " + calculatedTiketPoints);
        Assertion.assertThat("Verify loyalty point value is correct", tiketPointsActual,equalTo(calculatedTiketPoints));
    }

    @Author(names = {"Dendi"})
    @Module(name = "LoyaltyCoreHalf")
    @Tribe(name = "LoyaltyCore")
    @Vertical(name = "Platform")
    @Test
    public void testPayWithHalfTiketPointsTodo() throws InterruptedException, ExecutionException{
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("testPayWithHalfTiketPointsTodo");
        BaseTest.paramsThreadLocal.set(map);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        TTDPage<WebDriver> ttdPage = TTDPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        LoyaltyApi<?> loyaltyApi = new LoyaltyApi<>(session);

        String pdProcessType = map.get("pdProcessType");
        String username = map.get("username");
        String password = map.get("password");
        String currency = map.get("currency");
        int pdRation = Integer.parseInt(map.get("pdRation"));
        int dateRange = Integer.parseInt(map.get("dateRange"));
        int monthRange = Integer.parseInt(map.get("monthRange"));
        String productUrl = map.get("productUrl");
        String productPackages = map.get("productPackages");

        String[] salutationList = { "Mr", "Mrs", "Ms" };
        String guestFullName = randomName() + " autguest";
        String guestTitle = getRandomObject(salutationList);

        step("Create a ttd order with waiting payment status");
        String orderId = ttdPage.createWaitingPaymentOrderApi(
            username,password,
            productUrl,
            productPackages,
            guestFullName,guestTitle,
            guestFullName,username,guestTitle,dateRange,monthRange,false);
        String totalPriceTodo = String.valueOf(ttdPage.getGrandTotal());
        Assertion.assertThat("Success to create waiting payment order of price " + totalPriceTodo + " ", orderId, is(not(emptyOrNullString())));
        int totalPriceTodoInt = Integer.parseInt(totalPriceTodo.replaceAll("[^0-9]", ""));
        int halfPoint = totalPriceTodoInt / 2;

        step("user open app");
        homePage.gotoHome();

        step("user login using "+username+" account");
        homePage.loginWithSpecificAccountViaDeeplink(username, password, currency);

        step("get tiket points");
        String tiketPoints = homePage.getActiveTiketPointsInRewardsPage();

        //inject tiket point
        step("check if order can be book using half tiket points, else delete or inject point");
        tiketPoints = loyaltyApi.injectTiketPointsBasedOnCondition( halfPoint, pdProcessType, pdRation, username,tiketPoints,"half");

        step("open payment page for order " + orderId);
        paymentPage.openWaitingPaymentPage(orderId);
        orderId = paymentPage.checkOrderID();
        Assertion.assertThat("is open correct payment order", !orderId.isEmpty(),is(true));

        step("choose atm payment method");
        paymentPage.openPaymentMethod();
        paymentPage.selectPaymentMethod("ATM");

        step("Toggle pay with loyalty");
        paymentPage.payWithTiketPoint();
        Assertion.assertThat("total price not changes to gratis/free",paymentPage.checkTotalPriceChangesToFree(),is(false));

        step("Submit payment");
        paymentPage.clickPayButton();

        step("Open order list page");
        paymentPage.seeOrderList(orderId);

        step("get deducted tiket points");
        String newTiketPoints = homePage.getActiveTiketPointsInRewardsPage();
        int tiketPointsActual = Integer.parseInt(newTiketPoints.replaceAll("[^0-9]", ""));

        step("Verify loyalty point is 0");
        Assertion.assertThat("Verify loyalty point value is 0",tiketPointsActual,is(0));
    }

    @Author(names = {"Ashish"})
    @Module(name = "LoyaltyCoreHalf")
    @Tribe(name = "LoyaltyCore")
    @Vertical(name = "Platform")
    @Test
    public void testPayWithHalfTiketPointsHotelNative() throws InterruptedException, ExecutionException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testPayWithHalfTiketPointsHotelNative");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        LoyaltyApi<?> loyaltyApi = new LoyaltyApi<>(session);
        OrderApi<?> orderApi = new OrderApi<>(session);

        String pdProcessType = param.get("pdProcessType");
        int pdRation = Integer.parseInt(param.get("pdRation"));

        String username = param.get("username");
        String password = param.get("password");
        String startDate = generateRandomDate(param.get("rangeAddDate"));
        String toolTipText = param.get("toolTipText");

        String hotelName = param.get("hotelName");
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));
        int room = Integer.parseInt(param.get("room"));

        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = getRandomObject(salutationList);

        String currency = param.get("currency");

        step("Verify user has no refund history");
        orderApi.verifyNoRefundHistory(username, password);

        step("Given login user has booked hotel product");
        String issuedOrderID = hotelApi.createWaitingPaymentOrderApi(
                username, password, startDate, hotelName, adult, night, room,
                orderFullName, orderNameTitle, guestFullName, guestTitle,
                currency,"id");
        String totalPriceHotel = String.valueOf(hotelApi.getGrandTotal());
        Assertion.assertThat("Success to create waiting payment order of price " + totalPriceHotel + " ", issuedOrderID, is(not(emptyOrNullString())));
        int totalPriceHotelInt = Integer.parseInt(totalPriceHotel.replaceAll("[^0-9]", ""));
        int maxUsablePoints = totalPriceHotelInt/2;

        step("user open app");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink and change currency to " + currency);
        homePage.loginWithSpecificAccountViaDeeplink(username, password, currency);

        step("And user has tix points more than 50% of order price");
        String tiketPoints = homePage.getActiveTiketPointsInRewardsPage();
        tiketPoints = loyaltyApi.injectTiketPointsBasedOnCondition(
                totalPriceHotelInt, pdProcessType, pdRation, username, tiketPoints, "full");
        int pointsBeforePayment = Integer.parseInt(tiketPoints.replaceAll("[^0-9]", ""));
        Assertion.assertThat("User tix points should exceed 50% of order price",
                pointsBeforePayment, greaterThan(maxUsablePoints));

        step("When user go to payment page");
        homePage.openMyOrderPage();
        orderListPage.openOrderCompletePayment(issuedOrderID);
        String orderId = paymentPage.checkOrderID();
        Assertion.assertThat("is open correct payment order", orderId.contains(issuedOrderID), is(true));

        step("choose payment method");
        paymentPage.openPaymentMethod();
        paymentPage.selectPaymentMethod("Sakuku");

        step("Verify tooltip info for tix points cap");
        paymentPage.verifyTiketPointsCapInfo(toolTipText);

        step("Verify redeemable tix points on payment page should not exceed 50% of order price");
        int redeemableTiketPointsOnPaymentPage = paymentPage.getRedeemableTiketPointsOnPaymentPage();
        Assertion.assertThat("Redeemable tix points on payment page should not exceed 50% of order price",
                redeemableTiketPointsOnPaymentPage, lessThanOrEqualTo(maxUsablePoints));

        step("Toggle pay with loyalty");
        paymentPage.payWithTiketPoint();
        Assertion.assertThat("total price not changes to gratis/free",paymentPage.checkTotalPriceChangesToFree(),is(false));

        step("And user can successfully pay the order");
        paymentPage.clickPayButton();
        paymentPage.postAutoPaidOrder();
        paymentPage.seeOrderList(orderId);

        step("Verify only capped tix points are deducted");
        String newTiketPoints = homePage.getActiveTiketPointsInRewardsPage();
        int tiketPointsActual = Integer.parseInt(newTiketPoints.replaceAll("[^0-9]", ""));
        int expectedRemaining = pointsBeforePayment - redeemableTiketPointsOnPaymentPage;
        Assertion.assertThat("Only capped tix points should be deducted",
                tiketPointsActual, equalTo(expectedRemaining));
    }

    @Author(names = {"Ashish"})
    @Module(name = "LoyaltyCoreHalf")
    @Tribe(name = "LoyaltyCore")
    @Vertical(name = "Platform")
    @Test
    public void testPayWithHalfTiketPointsCarRental() throws InterruptedException, ExecutionException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testPayWithHalfTiketPointsCarRental");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        CarRentalPage<WebDriver> carRentalPage = CarRentalPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        LoyaltyApi<?> loyaltyApi = new LoyaltyApi<>(session);

        String pdProcessType = param.get("pdProcessType");
        int pdRation = Integer.parseInt(param.get("pdRation"));
        int pdPoint = Integer.parseInt(param.get("pdPoint"));
        String currency = param.get("currency");

        String loginMethod = param.get("loginType");
        String username = param.get("username");
        String phoneNumber = param.get("phoneNumber");
        String password = param.get("password");

        String driverOption = param.get("driverOption");
        String location = param.get("location");
        String startDate = generateRandomDate(param.get("rangeAddDate"));
        String type = param.get("type");

        String pickupTestData = param.get("pickupTime");
        StringBuilder pickup = new StringBuilder(pickupTestData);
        pickup.setCharAt(2, ':');
        String pickupTime = pickup.toString();

        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String fullName = randomName() + " Autorder";
        String nameTitle = getRandomObject(salutationList);

        int rentalDuration = Integer.parseInt(param.get("rentalDuration"));

        step("Create car rental issued order for 1 passenger and 1 day via API");
        Boolean orderResult = carRentalPage.createProductTillPaymentApi(
                username, phoneNumber, password, driverOption, location,
                startDate, pickupTime, type, fullName, nameTitle,rentalDuration);
        Assertion.assertThat("Car rental booking via API is successful", orderResult, is(true));

        step("Get the data from issued order");
        String issuedOrderID = carRentalPage.issuedOrderData("orderId");

        step("user open app");
        homePage.gotoHome();

        step("user login as guest");
        loginPage.loginAsGuest();

        step("user login using "+username+" account");
        loginPage.loginWith(loginMethod, username, password);

        step("Change currency");
        homePage.changeCurrency(currency,username);

        step("get tiket points");
        String tiketPoints = homePage.getActiveTiketPointsInRewardsPage();

        //inject tiket point
        step("check if order can be book using half tiket points, else delete or inject point");
        tiketPoints = loyaltyApi.injectTiketPointsBasedOnCondition( pdPoint, pdProcessType, pdRation, username,tiketPoints,"half");

        step("Open Your Orders page");
        homePage.openMyOrderPage();

        step("open payment page");
        orderListPage.openOrderCompletePayment(issuedOrderID);
        String orderId = paymentPage.checkOrderID();
        Assertion.assertThat("is open correct payment order",orderId.contains(issuedOrderID),is(true));

        step("choose atm payment method");
        paymentPage.openPaymentMethod();
        paymentPage.selectPaymentMethod("ATM");

        step("Toggle pay with loyalty");
        paymentPage.payWithTiketPoint();
        Assertion.assertThat("total price not changes to gratis/free",paymentPage.checkTotalPriceChangesToFree(),is(false));

        step("Submit payment");
        paymentPage.clickPayButton();

        step("Auto paid order via api");
        paymentPage.postAutoPaidOrder();

        step("Open order list page");
        paymentPage.seeOrderList(orderId);

        step("get deducted tiket points");
        String newTiketPoints = homePage.getActiveTiketPointsInRewardsPage();
        int tiketPointsActual = Integer.parseInt(newTiketPoints.replaceAll("[^0-9]", ""));

        step("Verify loyalty point is 0");
        Assertion.assertThat("Verify loyalty point value is 0",tiketPointsActual,is(0));
    }

    @Author(names = {"Ashish"})
    @Module(name = "LoyaltyCoreFull")
    @Tribe(name = "LoyaltyCore")
    @Vertical(name = "Platform")
    @Test
    public void testPayWithFullTiketPointsHotelNative() throws InterruptedException, ExecutionException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testPayWithFullTiketPointsHotelNative");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HotelPage<WebDriver> hotelPage = HotelPage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        LoyaltyApi<?> loyaltyApi = new LoyaltyApi<>(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);

        String pdProcessType = param.get("pdProcessType");
        int pdRation = Integer.parseInt(param.get("pdRation"));

        String loginMethod = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String startDate = generateRandomDate(param.get("rangeAddDate"));

        String hotelName = param.get("hotelName");
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));
        int room = Integer.parseInt(param.get("room"));

        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = getRandomObject(salutationList);

        String currency = param.get("currency");

        step("Create a hotel order with waiting payment status");
        String issuedOrderID = hotelApi.createWaitingPaymentOrderApi(
                username, password, startDate, hotelName, adult, night, room,
                orderFullName, orderNameTitle, guestFullName, guestTitle,
                currency,"id");
        String totalPriceHotel = String.valueOf(hotelApi.getGrandTotal());
        Assertion.assertThat("Success to create waiting payment order of price " + totalPriceHotel + " ", issuedOrderID, is(not(emptyOrNullString())));

        step("user open app");
        homePage.gotoHome();

        step("user login as guest");
        loginPage.loginAsGuest();

        step("user login using "+username+" account");
        loginPage.loginWith(loginMethod, username, password);

        step("Change currency");
        homePage.changeCurrency(currency,username);

        step("get tiket points");
        String tiketPoints = homePage.getActiveTiketPointsInRewardsPage();

        step("check if order can be book using full tiket points, else inject point");
        tiketPoints = loyaltyApi.injectTiketPointsBasedOnCondition( Integer.parseInt(totalPriceHotel), pdProcessType, pdRation, username,tiketPoints, "full");

        step("Open Your Orders page");
        homePage.openMyOrderPage();

        step("open payment page");
        orderListPage.openOrderCompletePayment(issuedOrderID);
        String orderId = paymentPage.checkOrderID();
        Assertion.assertThat("is open correct payment order",orderId.contains(issuedOrderID),is(true));

        step("get total price");
        double appTotalPrice = CoreFunctions.convertPrice(paymentPage.getTotalPrice());

        step("Toggle pay with loyalty");
        paymentPage.payWithTiketPoint();
        Assertion.assertThat("total price changes to gratis/free",paymentPage.checkTotalPriceChangesToFree(),is(true));

        step("Submit payment");
        paymentPage.clickPayButton();

        step("Auto paid order via api");
        paymentPage.postAutoPaidOrder();
        
        step("see order list");
        paymentPage.seeOrderList(orderId);

        step("get deducted tiket points");
        String newTiketPoints = homePage.getActiveTiketPointsInRewardsPage();
        int tiketPointsActual = Integer.parseInt(newTiketPoints.replaceAll("[^0-9]", ""));
        int calculatedTiketPoints = Integer.parseInt(tiketPoints) - (int) appTotalPrice;

        step("Verify loyalty point is " + tiketPointsActual + " and should be equal to " + calculatedTiketPoints);
        Assertion.assertThat("Verify loyalty point value is correct", tiketPointsActual,equalTo(calculatedTiketPoints));
    }
    @Author(names = {"Dendi"})
    @Module(name = "LoyaltyCoreFull")
    @Tribe(name = "LoyaltyCore")
    @Vertical(name = "Platform")
    @Test
    public void testPayWithFullTiketPointsTrain() throws InterruptedException, ExecutionException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testPayWithFullTiketPointsTrain");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TrainPage<WebDriver> trainPage = TrainPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        LoyaltyApi<?> loyaltyApi = new LoyaltyApi<>(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);

        String pdProcessType = param.get("pdProcessType");
        int pdRation = Integer.parseInt(param.get("pdRation"));
        String currency = param.get("currency");
        
        String loginType = param.get("loginType");
        String email = param.get("email");
        String password = param.get("password");
        String name = param.get("name");
        String phone = param.get("phone");
        String id = String.valueOf(CoreFunctions.randomInt(3));
        id = "327202200202"+id+"1";

        String originCity = param.get("originCity");
        String destinationCity = param.get("destinationCity");
        String tripType = param.get("tripType");
        int adult = Integer.parseInt(param.get("adult"));
        int infant = Integer.parseInt(param.get("infant"));
        String date = generateRandomDate(param.get("rangeAddDate"));
        date = date.replace("-","");

        step("Create train waiting payment order for 1 passenger and 1 day via API");
        String orderResult = trainPage.createWaitingPaymentOrderApi(
            email, password, name, phone, id, originCity, destinationCity, date, tripType, adult, infant);
        Assertion.assertThat("Train booking via API is successful", orderResult, not(blankOrNullString()));

        step("Get the data from issued order");
        String issuedOrderID = String.valueOf(trainPage.getOrderId());
        String grandTotal = String.valueOf(trainPage.getGrandTotal());

        step("user open app");
        homePage.gotoHome();

        step("user login as guest");
        loginPage.loginAsGuest();

        step("user login using "+email+" account");
        loginPage.loginWith(loginType, email, password);

        step("Change currency");
        homePage.changeCurrency(currency,email);

        step("get tiket points");
        String tiketPoints = homePage.getActiveTiketPointsInRewardsPage();

        step("check if order can be book using full tiket points, else inject point");
        tiketPoints = loyaltyApi.injectTiketPointsBasedOnCondition( Integer.parseInt(grandTotal), pdProcessType, pdRation, email,tiketPoints, "full");
        
        step("Open Your Orders page");
        homePage.openMyOrderPage();

        step("open payment page");
        orderListPage.openOrderCompletePayment(issuedOrderID);
        String orderId = paymentPage.checkOrderID();
        Assertion.assertThat("is open correct payment order",orderId.contains(issuedOrderID),is(true));

        step("get total price");
        double appTotalPrice = CoreFunctions.convertPrice(paymentPage.getTotalPrice());

        step("Toggle pay with loyalty");
        paymentPage.payWithTiketPoint();
        Assertion.assertThat("total price changes to gratis/free",paymentPage.checkTotalPriceChangesToFree(),is(true));

        step("Submit payment");
        paymentPage.clickPayButton();

        step("Auto paid order via api");
        paymentPage.postAutoPaidOrder();

        step("see order list");
        paymentPage.seeOrderList(orderId);

        step("get deducted tiket points");
        String newTiketPoints = homePage.getActiveTiketPointsInRewardsPage();
        int tiketPointsActual = Integer.parseInt(newTiketPoints.replaceAll("[^0-9]", ""));
        int calculatedTiketPoints = Integer.parseInt(tiketPoints) - (int) appTotalPrice;

        step("Verify loyalty point is " + tiketPointsActual + " and should be equal to " + calculatedTiketPoints);
        Assertion.assertThat("Verify loyalty point value is correct", tiketPointsActual,equalTo(calculatedTiketPoints));
    }

    @Author(names = {"Dendi"})
    @Module(name = "LoyaltyCoreFull")
    @Tribe(name = "LoyaltyCore")
    @Vertical(name = "Platform")
    @Test
     public void testPayWithFullTiketPointsCarTransfer() throws NumberFormatException, InterruptedException, ExecutionException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testPayWithFullTiketPointsCarTransfer");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        LoyaltyApi<?> loyaltyApi = new LoyaltyApi<>(session);
        CarTransferApi<?> carTransferApi = new CarTransferApi<>(session);

        String pdProcessType = param.get("pdProcessType");
        int pdRation = Integer.parseInt(param.get("pdRation"));
        String currency = param.get("currency");
        
        String loginType = param.get("loginType");
        String email = param.get("email");
        String password = param.get("password");
        String origin = param.get("origin");
        String destination = param.get("destination");
        String type = param.get("type");
        String pickupTime = param.get("pickupTime").replace("/",":");
        int pax = Integer.parseInt(param.get("pax"));
        String rangeAddDate = param.get("rangeAddDate");

        step("Create car transfer waiting payment order for 1 passenger and 1 day via API");
        String issuedOrderID = carTransferApi.createWaitingPaymentOrderApi(email,password,origin,destination,type,rangeAddDate,pickupTime,pax);
        Assertion.assertThat("Car transfer booking via API is successful", issuedOrderID, not(blankOrNullString()));
        String grandTotal = String.valueOf((int)carTransferApi.getGrandTotal());

        step("user open app");
        homePage.gotoHome();

        step("user login as guest");
        loginPage.loginAsGuest();

        step("user login using "+email+" account");
        loginPage.loginWith(loginType, email, password);

        step("Change currency");
        homePage.changeCurrency(currency,email);

        step("get tiket points");
        String tiketPoints = homePage.getActiveTiketPointsInRewardsPage();

        step("check if order can be book using full tiket points, else inject point");
        tiketPoints = loyaltyApi.injectTiketPointsBasedOnCondition( Integer.parseInt(grandTotal), pdProcessType, pdRation, email, tiketPoints, "full");

        step("Open Your Orders page");
        homePage.openMyOrderPage();

        step("open payment page");
        orderListPage.openOrderCompletePayment(issuedOrderID);
        String orderId = paymentPage.checkOrderID();
        Assertion.assertThat("is open correct payment order",orderId.contains(issuedOrderID),is(true));

        step("get total price");
        double appTotalPrice = CoreFunctions.convertPrice(paymentPage.getTotalPrice());

        step("Toggle pay with loyalty");
        paymentPage.payWithTiketPoint();
        Assertion.assertThat("total price changes to gratis/free",paymentPage.checkTotalPriceChangesToFree(),is(true));

        step("Submit payment");
        paymentPage.clickPayButton();

        step("Auto paid order via api");
        paymentPage.postAutoPaidOrder();

        step("Open order list page");
        paymentPage.seeOrderList(orderId);

        step("get deducted tiket points");
        String newTiketPoints = homePage.getActiveTiketPointsInRewardsPage();
        int tiketPointsActual = Integer.parseInt(newTiketPoints.replaceAll("[^0-9]", ""));
        int calculatedTiketPoints = Integer.parseInt(tiketPoints) - (int) appTotalPrice;

        step("Verify loyalty point is " + tiketPointsActual + " and should be equal to " + calculatedTiketPoints);
        Assertion.assertThat("Verify loyalty point value is correct", tiketPointsActual,equalTo(calculatedTiketPoints));
     }

    @Author(names = {"Dendi"})
    @Module(name = "LoyaltyCoreFull")
    @Tribe(name = "LoyaltyCore")
    @Vertical(name = "Platform")
    @Test
     public void testPayWithFullTiketPointsTodos() throws NumberFormatException, InterruptedException, ExecutionException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("testPayWithFullTiketPointsTodos");
        BaseTest.paramsThreadLocal.set(map);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        LoyaltyApi<?> loyaltyApi = new LoyaltyApi<>(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        TTDPage<WebDriver> ttdPage = TTDPage.getInstance(session);

        String pdProcessType = map.get("pdProcessType");
        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");
        String currency = map.get("currency");
        int pdRation = Integer.parseInt(map.get("pdRation"));
        int dateRange = Integer.parseInt(map.get("dateRange"));
        int monthRange = Integer.parseInt(map.get("monthRange"));
        String productUrl = map.get("productUrl");
        String productPackages = map.get("productPackages");

        String[] salutationList = { "Mr", "Mrs", "Ms" };
        String guestFullName = randomName() + " autguest";
        String guestTitle = getRandomObject(salutationList);

        step("Create ttd waiting payment order todo via API");
        String orderId = ttdPage.createWaitingPaymentOrderApi(
            username,password,
            productUrl,
            productPackages,
            guestFullName,guestTitle,
            guestFullName,username,guestTitle,dateRange,monthRange,false);

        String totalPriceTodo = String.valueOf(ttdPage.getGrandTotal());
        Assertion.assertThat("Success to create waiting payment order of price " + totalPriceTodo + " ", orderId, is(not(emptyOrNullString())));

        step("user open app");
        homePage.gotoHome();

        step("user login as guest");
        loginPage.loginAsGuest();

        step("user login using "+username+" account");
        loginPage.loginWith(loginType, username, password);

        step("Change currency");
        homePage.changeCurrency(currency,username);

        step("get tiket points");
        String tiketPoints = homePage.getActiveTiketPointsInRewardsPage();

        step("check if order can be book using full tiket points, else inject point");
        tiketPoints = loyaltyApi.injectTiketPointsBasedOnCondition( Integer.parseInt(totalPriceTodo), pdProcessType, pdRation, username, tiketPoints, "full");

        step("Open Your Orders page");
        homePage.openMyOrderPage();

        step("open payment page");
        orderListPage.openOrderCompletePayment(orderId);
        String orderIdPaymentPage = paymentPage.checkOrderID();
        Assertion.assertThat("is open correct payment order",orderIdPaymentPage.contains(orderId),is(true));

        step("get total price");
        double appTotalPrice = CoreFunctions.convertPrice(paymentPage.getTotalPrice());

        step("Toggle pay with loyalty");
        paymentPage.payWithTiketPoint();
        Assertion.assertThat("total price changes to gratis/free",paymentPage.checkTotalPriceChangesToFree(),is(true));

        step("Submit payment");
        paymentPage.clickPayButton();

        step("Auto paid order via api");
        paymentPage.postAutoPaidOrder();

        step("Open order list page");
        paymentPage.seeOrderList(orderId);

        step("get deducted tiket points");
        String newTiketPoints = homePage.getActiveTiketPointsInRewardsPage();
        int tiketPointsActual = Integer.parseInt(newTiketPoints.replaceAll("[^0-9]", ""));
        int calculatedTiketPoints = Integer.parseInt(tiketPoints) - (int) appTotalPrice;

        step("Verify loyalty point is " + tiketPointsActual + " and should be equal to " + calculatedTiketPoints);
        Assertion.assertThat("Verify loyalty point value is correct", tiketPointsActual,equalTo(calculatedTiketPoints));
    }

    @Author(names = {"Dendi"})
    @Module(name = "LoyaltyCoreFull")
    @Tribe(name = "LoyaltyCore")
    @Vertical(name = "Platform")
    @Test
    public void testPayWithFullTiketPointsFerry() throws NumberFormatException, InterruptedException, ExecutionException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> testdata = getParam("testPayWithFullTiketPointsFerry");
        BaseTest.paramsThreadLocal.set(testdata);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        LoyaltyApi<?> loyaltyApi = new LoyaltyApi<>(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        FerryPage<WebDriver> ferryPage = FerryPage.getInstance(session);

        String email = testdata.get("email");
        String password = testdata.get("password");
        String origin = testdata.get("origin");
        String destination = testdata.get("destination");
        String vehicleType = testdata.get("vehicleType");
        int adult = Integer.parseInt(testdata.get("adult"));
        int child = Integer.parseInt(testdata.get("child"));
        int infant = Integer.parseInt(testdata.get("infant"));
        int elderly = Integer.parseInt(testdata.get("elderly"));
        String pdProcessType = testdata.get("pdProcessType");
        int pdRatio = Integer.parseInt(testdata.get("pdRatio"));
        String departureDate = CoreFunctions.getEndOfCurrentMonth("yyyy-MM-dd");
        String departureTime = testdata.get("departureTime").replace("-", ":");

        step("Create ttd waiting payment order ferry via API");
        int orderId = ferryPage.createWaitingPaymentOrderFerry(email,password,origin,destination,departureDate,departureTime,vehicleType,adult,child,infant,elderly);
        Assertion.assertThat("Verify orderId is generated", orderId, greaterThan(0));
        String orderIdStr = String.valueOf(orderId);
        String totalPriceFerry = String.valueOf(ferryPage.getTotalPrice());

        step("user open app");
        homePage.gotoHome();

        step("user login as guest");
        loginPage.loginAsGuest();

        step("user login using "+email+" account");
        loginPage.loginWith("email", email, password);

        step("Change currency");
        homePage.changeCurrency("IDR",email);

        step("get tiket points");
        String tiketPoints = homePage.getActiveTiketPointsInRewardsPage();

        step("check if order can be book using full tiket points, else inject point");
        tiketPoints = loyaltyApi.injectTiketPointsBasedOnCondition( Integer.parseInt(totalPriceFerry), pdProcessType, pdRatio, email, tiketPoints, "full");

        step("Open Your Orders page");
        homePage.openMyOrderPage();

        step("open payment page");
        orderListPage.openOrderCompletePayment(orderIdStr);
        String orderIdPaymentPage = paymentPage.checkOrderID();
        Assertion.assertThat("is open correct payment order",orderIdPaymentPage.contains(orderIdStr),is(true));

        step("get total price");
        double appTotalPrice = CoreFunctions.convertPrice(paymentPage.getTotalPrice());

        step("Toggle pay with loyalty");
        paymentPage.payWithTiketPoint();
        Assertion.assertThat("total price changes to gratis/free",paymentPage.checkTotalPriceChangesToFree(),is(true));

        step("Submit payment");
        paymentPage.clickPayButton();

        step("Auto paid order via api");
        paymentPage.postAutoPaidOrder();

        step("Open order list page");
        paymentPage.seeOrderList(orderIdPaymentPage);

        step("get deducted tiket points");
        String newTiketPoints = homePage.getActiveTiketPointsInRewardsPage();
        int tiketPointsActual = Integer.parseInt(newTiketPoints.replaceAll("[^0-9]", ""));
        int calculatedTiketPoints = Integer.parseInt(tiketPoints) - (int) appTotalPrice;

        step("Verify loyalty point is " + tiketPointsActual + " and should be equal to " + calculatedTiketPoints);
        Assertion.assertThat("Verify loyalty point value is correct", tiketPointsActual,equalTo(calculatedTiketPoints));
    }

    @Author(names = {"Dendi"})
    @Module(name = "LoyaltyCoreHalf")
    @Tribe(name = "LoyaltyCore")
    @Vertical(name = "Platform")
    @Test
    public void testPayWithHalfTiketPointsFerry() throws NumberFormatException, InterruptedException, ExecutionException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> testdata = getParam("testPayWithHalfTiketPointsFerry");
        BaseTest.paramsThreadLocal.set(testdata);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        LoyaltyApi<?> loyaltyApi = new LoyaltyApi<>(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        FerryPage<WebDriver> ferryPage = FerryPage.getInstance(session);

        String email = testdata.get("email");
        String password = testdata.get("password");
        String origin = testdata.get("origin");
        String destination = testdata.get("destination");
        String vehicleType = testdata.get("vehicleType");
        int adult = Integer.parseInt(testdata.get("adult"));
        int child = Integer.parseInt(testdata.get("child"));
        int infant = Integer.parseInt(testdata.get("infant"));
        int elderly = Integer.parseInt(testdata.get("elderly"));
        String pdProcessType = testdata.get("pdProcessType");
        int pdRatio = Integer.parseInt(testdata.get("pdRatio"));
        String departureDate = CoreFunctions.getEndOfCurrentMonth("yyyy-MM-dd");
        String departureTime = testdata.get("departureTime").replace("-", ":");

        step("Create ferry waiting payment order ferry via API");
        int orderId = ferryPage.createWaitingPaymentOrderFerry(email,password,origin,destination,departureDate,departureTime,vehicleType,adult,child,infant,elderly);
        Assertion.assertThat("Verify orderId is generated", orderId, greaterThan(0));
        String orderIdStr = String.valueOf(orderId);
        String totalPriceFerry = String.valueOf(ferryPage.getTotalPrice());
        int halfPoint = Integer.parseInt(totalPriceFerry) / 2;

        step("user open app");
        homePage.gotoHome();

        step("user login as guest");
        loginPage.loginAsGuest();

        step("user login using "+email+" account");
        loginPage.loginWith("email", email, password);

        step("Change currency");
        homePage.changeCurrency("IDR",email);

        step("get tiket points");
        String tiketPoints = homePage.getActiveTiketPointsInRewardsPage();

        //inject tiket point
        step("check if order can be book using half tiket points, else delete or inject point");
        tiketPoints = loyaltyApi.injectTiketPointsBasedOnCondition( halfPoint, pdProcessType, pdRatio, email,tiketPoints,"half");

        step("Open Your Orders page");
        homePage.openMyOrderPage();

        step("open payment page");
        orderListPage.openOrderCompletePayment(orderIdStr);
        String orderIdFromPaymentPage = paymentPage.checkOrderID();
        Assertion.assertThat("is open correct payment order",orderIdFromPaymentPage.contains(orderIdStr),is(true));

        step("choose atm payment method");
        paymentPage.openPaymentMethod();
        paymentPage.selectPaymentMethod("ATM");

        step("Toggle pay with loyalty");
        paymentPage.payWithTiketPoint();
        Assertion.assertThat("total price not changes to gratis/free",paymentPage.checkTotalPriceChangesToFree(),is(false));

        step("Submit payment");
        paymentPage.clickPayButton();

        step("Open order list page");
        paymentPage.seeOrderList(orderIdFromPaymentPage);

        step("get deducted tiket points");
        String newTiketPoints = homePage.getActiveTiketPointsInRewardsPage();
        int tiketPointsActual = Integer.parseInt(newTiketPoints.replaceAll("[^0-9]", ""));

        step("Verify loyalty point is 0");
        Assertion.assertThat("Verify loyalty point value is 0",tiketPointsActual,is(0));
    }

    @Author(names = {"Kartik"})
    @Module(name = "LoyaltyCoreFull")
    @Tribe(name = "LoyaltyCore")
    @Vertical(name = "Platform")
    @Test
    public void testPayWithFullTiketPointsFlight() throws ExecutionException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testPayWithFullTiketPointsFlight");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        LoyaltyApi<?> loyaltyApi = new LoyaltyApi<>(session);
        PaymentApi<?> paymentApi = new PaymentApi<>(session);
        FlightApi<?> flightApi = new FlightApi<>(session);

        String loginType = param.get("loginType");
        String email = param.get("username");
        String password = param.get("password");
        String originStation = param.get("originStation");
        String destinationStation = param.get("destinationStation");
        String originType = param.get("originType");
        String destinationType = param.get("destinationType");
        String searchType = param.get("searchType");
        int adult = Integer.parseInt(param.get("adult"));
        int child = Integer.parseInt(param.get("child"));
        int infant = Integer.parseInt(param.get("infant"));

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String departDateApi = CoreFunctions.getRandomLongDateOnRange(daysToDepartureDate,30,"yyyy-MM-dd");

        String airline = param.get("airline");
        String firstName = param.get("firstName");
        String lastName = param.get("lastName");
        String phone = param.get("phone");
        String pdProcessType = param.get("pdProcessType");
        int pdRatio = Integer.parseInt(param.get("pdRatio"));

        step("user open app");
        homePage.gotoHome();
        loginPage.loginAsGuest();

        step("user login using " + email + " account");
        loginPage.loginWith(loginType, email, password);

        step("Create waiting payment order for Flight");
        String orderId = flightApi.createWaitForPaymentFlightB2CDomestic(email, password, originStation, destinationStation, departDateApi, searchType, adult, child, infant, airline, firstName, lastName);
        double totalPriceFlight = paymentApi.getTotalPriceViaApi(orderId);

        step("Change currency");
        homePage.changeCurrency("IDR",email);

        step("get tiket points");
        String tiketPoints = homePage.getActiveTiketPointsInRewardsPage();

        step("Inject tiket point if required");
        tiketPoints = loyaltyApi.injectTiketPointsBasedOnCondition( (int)totalPriceFlight, pdProcessType, pdRatio, email, String.valueOf(tiketPoints),"full");

        step("Open Your Orders page");
        homePage.openMyOrderPage();

        step("open payment page");
        orderListPage.openOrderCompletePayment(orderId);
        String orderIdPaymentPage = paymentPage.checkOrderID();
        Assertion.assertThat("is open correct payment order",orderIdPaymentPage.contains(orderId),is(true));

        step("get total price");
        double appTotalPrice = CoreFunctions.convertPrice(paymentPage.getTotalPrice());

        step("Toggle pay with loyalty");
        paymentPage.payWithTiketPoint();
        Assertion.assertThat("total price changes to gratis/free",paymentPage.checkTotalPriceChangesToFree(),is(true));

        step("Submit payment");
        paymentPage.clickPayButton();

        step("Open order list page");
        paymentPage.seeOrderList(orderIdPaymentPage);

        step("get deducted tiket points");
        String newTiketPoints = homePage.getActiveTiketPointsInRewardsPage();
        int tiketPointsActual = Integer.parseInt(newTiketPoints.replaceAll("[^0-9]", ""));
        int calculatedTiketPoints = Integer.parseInt(tiketPoints) - (int) appTotalPrice;

        step("Verify loyalty point is " + tiketPointsActual + " and should be equal to " + calculatedTiketPoints);
        Assertion.assertThat("Verify loyalty point value is correct", tiketPointsActual,equalTo(calculatedTiketPoints));
    }

    @Author(names = {"Kartik"})
    @Module(name = "LoyaltyCoreHalf")
    @Tribe(name = "LoyaltyCore")
    @Vertical(name = "Platform")
    @Test
    public void testPayWithHalfTiketPointsFlight() throws ExecutionException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testPayWithHalfTiketPointsFlight");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        LoyaltyApi<?> loyaltyApi = new LoyaltyApi<>(session);
        PaymentApi<?> paymentApi = new PaymentApi<>(session);
        FlightApi<?> flightApi = new FlightApi<>(session);

        String loginType = param.get("loginType");
        String email = param.get("username");
        String password = param.get("password");
        String originStation = param.get("originStation");
        String destinationStation = param.get("destinationStation");

        String searchType = param.get("searchType");
        int adult = Integer.parseInt(param.get("adult"));
        int child = Integer.parseInt(param.get("child"));
        int infant = Integer.parseInt(param.get("infant"));
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String departDateApi = CoreFunctions.getRandomLongDateOnRange(daysToDepartureDate,30,"yyyy-MM-dd");

        String airline = param.get("airline");
        String firstName = param.get("firstName");
        String lastName = param.get("lastName");
        String pdProcessType = param.get("pdProcessType");
        int pdRatio = Integer.parseInt(param.get("pdRatio"));

        step("Create waiting payment order for Flight");
        String orderId = flightApi.createWaitForPaymentFlightB2CDomestic(email, password, originStation, destinationStation, departDateApi, searchType, adult, child, infant, airline, firstName, lastName);
        double totalPriceFlight = paymentApi.getTotalPriceViaApi(orderId);
        int halfPoint = (int) totalPriceFlight / 2;

        step("user open app");
        homePage.gotoHome();

        step("Login as " + email + " via deeplink and change currency to IDR");
        homePage.loginWithSpecificAccountViaDeeplink(email, password, "IDR");

        step("get tiket points");
        String tiketPoints = homePage.getActiveTiketPointsInRewardsPage();

        //inject tiket point
        step("check if order can be book using half tiket points, else delete or inject point");
        tiketPoints = loyaltyApi.injectTiketPointsBasedOnCondition( halfPoint, pdProcessType, pdRatio, email,tiketPoints,"half");

        step("Open Your Orders page");
        homePage.openMyOrderPage();

        step("open payment page");
        orderListPage.openOrderCompletePayment(orderId);
        String orderIdFromPaymentPage = paymentPage.checkOrderID();
        Assertion.assertThat("is open correct payment order",orderIdFromPaymentPage.contains(orderId),is(true));

        step("choose atm payment method");
        paymentPage.openPaymentMethod();
        paymentPage.selectPaymentMethod("ATM");

        step("Toggle pay with loyalty");
        paymentPage.payWithTiketPoint();

        step("Submit payment");
        paymentPage.clickPayButton();

        step("Open order list page");
        paymentPage.seeOrderList(orderIdFromPaymentPage);

        step("get deducted tiket points");
        String newTiketPoints = homePage.getActiveTiketPointsInRewardsPage();
        int tiketPointsActual = Integer.parseInt(newTiketPoints.replaceAll("[^0-9]", ""));

        step("Verify loyalty point is 0");
        Assertion.assertThat("Verify loyalty point value is 0",tiketPointsActual,is(0));
    }

    @Author(names = {"Manan Kumar"})
    @Module(name = "LoyaltyCore")
    @Tribe(name = "Eiffel")
    @Vertical(name = "Eiffel")
    @Test
    public void testOpenRewardsPageAsGuestUser() throws ExecutionException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testOpenRewardsPageAsGuestUser");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);

        String username = param.get("username");
        String password = param.get("password");

        step("Given a non-login user opens the app");
        homePage.gotoHome();

        step("Set session as non-login (guest)");
        loginPage.loginAsGuest();

        step("User clicks on rewards page");
        homePage.clickOnRewardButton();

        step("User should be redirected to tiket's rewards page");
        boolean isOnRewardsPage = homePage.isOnRewardsPage();
        Assertion.assertThat("User should be on the rewards page", isOnRewardsPage, is(true));

        step("User clicks on login button on rewards page");
        homePage.clickLoginButton();

        step("User inputs all required login fields");
        loginPage.loginFromDifferentPages(username, password);

        step("Verify user is successfully logged in");
        Assertion.assertThat("User should be successfully logged in", homePage.isUserLogin(), is(true));
    }

    @Author(names = {"Ashish"})
    @Module(name = "Rewards")
    @Tribe(name = "LoyaltyCore")
    @Vertical(name = "Eiffel")
    @Test
    public void verifyMissionStampIncreaseForEligibleHotelApp() throws Exception {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyMissionStampIncreaseForEligibleHotelApp");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        HotelPage<WebDriver> hotelPage = HotelPage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        MissionPageWeb<?> missionPage = new MissionPageWeb<>(session);
        OrderApi<?> orderApi = new OrderApi<>(session);
        LoyaltyApi<?> loyaltyApi = new LoyaltyApi<>(session);
        Unm<?> unm = new Unm<>(session);

        String missionSlug = param.get("missionSlug") != null ? param.get("missionSlug") : "stay-with-benefits";
        String hotelName = param.get("hotelName");
        String currency = param.get("currency") != null ? param.get("currency") : "IDR";
        int adult = Integer.parseInt(param.get("numberOfAdults"));
        int night = Integer.parseInt(param.get("numberOfNights"));
        int room = Integer.parseInt(param.get("numberOfRooms"));
        String startDate = generateRandomDate(param.get("rangeAddDate"));

        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = getRandomObject(salutationList);

        RegisterNewAccountData accountData = null;
        boolean loggedIn = false;
        try {
            step("Create a fresh account via API so the mission starts from zero stamps");
            accountData = unm.registerNewAccountViaApiUNM();
            String username = accountData.email;
            String password = accountData.password;

            step("Open app");
            homePage.gotoHome();

            step("login as " + username + " via deeplink");
            homePage.loginWithSpecificAccountViaDeeplinkOnly(username, password);
            loggedIn = true;

            step("Change currency to " + currency);
            homePage.changeCurrency(currency, username);

            step("Join the mission '" + missionSlug + "' via API for " + username);
            loyaltyApi.joinMission(missionSlug, username);

            step("Open mission page via deeplink and switch to webview");
            missionPage.openMissionDeeplink(missionSlug);
            homePage.closeCurrencyCountryPopup();
            webViewPage.switchToWebViewUrl("mission");

            step("Refresh the mission page and verify join status (join CTA gone)");
            missionPage.refreshMissionPage();
            Assertion.assertThat(
                    "The 'Swipe to join' CTA must be gone after enrolling via API.",
                    missionPage.isJoinMissionRequired(), is(false));

            step("Read the baseline stamp count right after joining (expected 0)");
            Assertion.assertThat("Stamp summary should be visible after joining",
                    missionPage.isStampSummaryVisible(), is(true));
            int stampsBefore = missionPage.getCollectedStampCount();
            int totalStamps = missionPage.getTotalStampCount();
            Assertion.assertThat(
                    "A freshly joined account must have 0 collected stamps.",
                    stampsBefore, is(0));

            step("Cross-check baseline against the mission detail API");
            Assertion.assertThat("API total stamps must match UI",
                    loyaltyApi.getMissionTotalStamps(missionSlug, username), is(totalStamps));

            step("Switch back to native");
            webViewPage.switchToNative();

            step("Create a hotel issued order via API (Sakuku payment) for '" + hotelName + "'");
            Boolean orderResult = hotelApi.createIssuedOrderApi(
                    username, password, startDate, hotelName, adult, night, room,
                    orderFullName, orderNameTitle, guestFullName, guestTitle,
                    currency);
            Assertion.assertThat("Hotel booking via API (Sakuku) is successful", orderResult, is(true));
            String issuedOrderID = String.valueOf(hotelApi.getOrderId());
            Assertion.assertThat("Hotel order ID is generated",
                    issuedOrderID, is(not(emptyOrNullString())));

            //add sleep to wait for order to be processed by mission engine
            step("Wait for 5 seconds to allow mission engine to process the order");
            sleep(5);

            step("Resolve orderDetailId and trigger publish-finish-order via API");
            long orderIdLong = Long.parseLong(issuedOrderID.replaceAll("[^0-9]", ""));
            long orderDetailId = orderApi.getHotelOrderDetailId(issuedOrderID);
            orderApi.publishFinishOrder(orderIdLong, orderDetailId);

            step("Wait for order to reach FINISHED status");
            Assertion.assertThat(
                    "Order must reach FINISHED status so the mission engine can evaluate the transaction.",
                    orderApi.waitForOrderFinished(issuedOrderID, 60, 6), is(true));

            step("Poll mission detail API until stamp count rises above baseline of " + stampsBefore);
            int apiStampsAfter = loyaltyApi.waitForStampCountAbove(missionSlug, username, stampsBefore, 12, 5);
            Assertion.assertThat(
                    "Finishing a mission-eligible hotel order must grant a mission stamp. "
                            + "apiCollected=" + apiStampsAfter + ", baseline=" + stampsBefore,
                    apiStampsAfter, is(greaterThan(stampsBefore)));

            step("Open mission page again to verify UI reflects the increased stamp count");
            missionPage.openMissionDeeplink(missionSlug);
            homePage.closeCurrencyCountryPopup();
            webViewPage.switchToWebViewUrl("mission");
            Assertion.assertThat("Stamp summary should be visible",
                    missionPage.isStampSummaryVisible(), is(true));
            int uiStampsAfter = missionPage.waitForCollectedStampCount(apiStampsAfter, 6, 5);
            Assertion.assertThat(
                    "Mission UI collected-stamp count must match API after eligible order "
                            + "(UI=" + uiStampsAfter + ", API=" + apiStampsAfter + ").",
                    uiStampsAfter, is(apiStampsAfter));

            step("Switch back to native for cleanup");
            webViewPage.switchToNative();

        } finally {
            step("go to home page");
            homePage.goToHomeUsingDeeplinkWithoutCondition();

            step("Cleanup: restore native context, logout and delete temporary account");
            homePage.logoutAndDeleteTemporaryAccount(accountData, loggedIn);
        }
    }

    @Author(names = {"Ashish"})
    @Module(name = "Rewards")
    @Tribe(name = "LoyaltyCore")
    @Vertical(name = "Eiffel")
    @Test
    public void verifyClaimMissionRewardsApp() throws Exception {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyClaimMissionRewardsApp");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        MissionPageWeb<?> missionPage = new MissionPageWeb<>(session);
        LoyaltyApi<?> loyaltyApi = new LoyaltyApi<>(session);
        Unm<?> unm = new Unm<>(session);

        String missionSlug = param.get("missionSlug") != null ? param.get("missionSlug") : "stay-with-benefits";
        String missionId = param.get("missionId");
        String vertical = param.get("vertical") != null ? param.get("vertical") : "HOTEL";
        String challenge = param.get("challenge");
        String challengeSignature = param.get("challengeSignature");
        String whitelistUsername = param.get("whitelistUsername");
        String currency = param.get("currency") != null ? param.get("currency") : "IDR";

        RegisterNewAccountData accountData = null;
        boolean loggedIn = false;
        try {
            step("Create a fresh account via API so the mission starts from zero stamps");
            accountData = unm.registerNewAccountViaApiUNM();
            String username = accountData.email;
            String password = accountData.password;

            step("Open app");
            homePage.gotoHome();

            step("login as " + username + " via deeplink");
            homePage.loginWithSpecificAccountViaDeeplinkOnly(username, password);
            loggedIn = true;

            step("Change currency to " + currency);
            homePage.changeCurrency(currency, username);

            step("Enroll the new account in mission '" + missionSlug + "' via API");
            loyaltyApi.joinMission(missionSlug, username);

            step("Open the mission page via deeplink and switch to webview");
            missionPage.openMissionDeeplink(missionSlug);
            homePage.closeCurrencyCountryPopup();
            webViewPage.switchToWebViewUrl("mission");

            step("Refresh the mission page and confirm enrollment");
            missionPage.refreshMissionPage();
            Assertion.assertThat(
                    "The 'Swipe to join' CTA must be gone after enrolling via API.",
                    missionPage.isJoinMissionRequired(), is(false));

            step("Read total stamps required from the mission detail API");
            int totalStamps = loyaltyApi.getMissionTotalStamps(missionSlug, username);
            Assertion.assertThat("A joinable mission must require at least one stamp.",
                    totalStamps, greaterThan(0));

            step("Confirm the mission page renders the same required stamp count");
            Assertion.assertThat("Stamp summary should be visible",
                    missionPage.isStampSummaryVisible(), is(true));
            Assertion.assertThat("UI total stamps must match API",
                    missionPage.getTotalStampCount(), is(totalStamps));

            step("Switch back to native before injecting stamps");
            webViewPage.switchToNative();

            step("Inject all " + totalStamps + " stamps via mission tools API to complete the mission");
            long accountId = loyaltyApi.injectMissionStamp(username, missionId, totalStamps, vertical,
                    challenge, challengeSignature, whitelistUsername);

            step("Poll mission detail API until ALL " + totalStamps + " stamps are marked COMPLETE");
            Assertion.assertThat(
                    "Every stamp must reach COMPLETE status within the wait budget.",
                    loyaltyApi.waitForAllStampsComplete(missionSlug, username, totalStamps, 12, 5), is(true));

            step("Poll until the reward becomes claimable");
            Assertion.assertThat(
                    "The reward must become claimable (cta=CLAIM_REWARD / reward.status=COMPLETED).",
                    loyaltyApi.waitForMissionRewardClaimable(missionSlug, username, 12, 5), is(true));

            step("Open the completed mission and surface the reward popup");
            missionPage.openMissionDeeplink(missionSlug);
            homePage.closeCurrencyCountryPopup();
            webViewPage.switchToWebViewUrl("mission");

            boolean rewardPopupShown = missionPage.isRewardReceivedPopupVisible(20);
            if (!rewardPopupShown && missionPage.isClaimRewardAvailable()) {
                step("Reward popup did not auto-open - clicking the claim CTA");
                missionPage.clickClaimReward();
                rewardPopupShown = missionPage.isRewardReceivedPopupVisible(20);
            }
            Assertion.assertThat(
                    "The reward popup must appear after all stamps are injected.",
                    rewardPopupShown, is(true));

            String rewardTitle = missionPage.getRewardTitleTextSafely();
            String rewardAmountText = missionPage.getRewardAmountText();
            boolean isPointsReward = missionPage.isPointsReward();
            int uiPoints = isPointsReward ? missionPage.getRewardPointsValue() : 0;
            step("Reward on popup -> title='" + rewardTitle + "', amount='" + rewardAmountText
                    + "', type=" + (isPointsReward ? "POINTS" : "GIFT VOUCHER"));

            step("Claim the reward from the popup");
            missionPage.clickClaimInRewardPopup();

            step("Verify on the UI that the claim was accepted (reward popup advances past the claimable state)");
            Assertion.assertThat(
                    "After clicking Claim, the reward popup's claim button must disappear (popup closes / "
                            + "transitions). If it stays visible, the claim was not accepted on the UI.",
                    missionPage.isRewardClaimAcceptedOnUi(20), is(true));

            step("Verify the mission reward transitions to CLAIMED via the mission detail API");
            Assertion.assertThat(
                    "After clicking Claim, the mission reward status must become CLAIMED. If the popup stays "
                            + "visible but the API never reports CLAIMED, the claim did not actually succeed "
                            + "(guards against a false pass, especially for non-points rewards).",
                    loyaltyApi.waitForMissionRewardStatus(missionSlug, username, "CLAIMED", 12, 5), is(true));

            if (isPointsReward) {
                step("Verify credited points in point-history API match " + uiPoints + " points from popup");
                int creditedPoints = loyaltyApi.getCreditedRewardPoint(accountId, uiPoints);
                Assertion.assertThat(
                        "Credited points in loyalty API must equal " + uiPoints + " shown on popup.",
                        creditedPoints, is(uiPoints));
            } else {
                step("Verify the gift-voucher reward shows a concrete amount on the popup");
                Assertion.assertThat(
                        "A gift-voucher reward must display a non-empty amount.",
                        rewardAmountText, is(not(emptyOrNullString())));
            }

        } finally {

            step("Switch back to native for cleanup");
            webViewPage.switchToNative();

            step("go to home page");
            homePage.goToHomeUsingDeeplinkWithoutCondition();

            step("Cleanup: restore native context, logout and delete temporary account");
            homePage.logoutAndDeleteTemporaryAccount(accountData, loggedIn);
        }
    }

    @Author(names = {"Ashish"})
    @Module(name = "Rewards")
    @Tribe(name = "LoyaltyCore")
    @Vertical(name = "Eiffel")
    @Test
    public void verifyMissionGbvProgressForEligibleHotelApp() throws Exception {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyMissionGbvProgressForEligibleHotelApp");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        HotelPage<WebDriver> hotelPage = HotelPage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        MissionPageWeb<?> missionPage = new MissionPageWeb<>(session);
        OrderApi<?> orderApi = new OrderApi<>(session);
        LoyaltyApi<?> loyaltyApi = new LoyaltyApi<>(session);
        Unm<?> unm = new Unm<>(session);

        String missionSlug = param.get("missionSlug") != null ? param.get("missionSlug") : "mission-gbv-multivertical";
        String hotelName = param.get("hotelName");
        String currency = param.get("currency") != null ? param.get("currency") : "IDR";
        int adult = Integer.parseInt(param.get("numberOfAdults"));
        int night = Integer.parseInt(param.get("numberOfNights"));
        int room = Integer.parseInt(param.get("numberOfRooms"));
        String startDate = generateRandomDate(param.get("rangeAddDate"));

        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = getRandomObject(salutationList);

        RegisterNewAccountData accountData = null;
        boolean loggedIn = false;
        try {
            step("Create a fresh account via API so the GBV mission starts from zero progress");
            accountData = unm.registerNewAccountViaApiUNM();
            String username = accountData.email;
            String password = accountData.password;

            step("Open app");
            homePage.gotoHome();

            step("login as " + username + " via deeplink");
            homePage.loginWithSpecificAccountViaDeeplinkOnly(username, password);
            loggedIn = true;

            step("Change currency to " + currency);
            homePage.changeCurrency(currency, username);

            step("Join the GBV mission '" + missionSlug + "' via API for " + username);
            loyaltyApi.joinMission(missionSlug, username);

            step("Open mission page via deeplink and switch to webview");
            missionPage.openMissionDeeplink(missionSlug);
            homePage.closeCurrencyCountryPopup();
            webViewPage.switchToWebViewUrl("mission");

            step("Refresh the mission page and confirm the GBV progress bar renders");
            missionPage.refreshMissionPage();
            Assertion.assertThat(
                    "The GBV (spend-based) progress bar must be visible on the mission page.",
                    missionPage.isGbvProgressVisible(), is(true));

            step("Read the baseline GBV spent amount right after joining (expected 0)");
            long gbvSpentBefore = missionPage.getGbvSpentAbsolute();
            Assertion.assertThat(
                    "A freshly joined account must have 0 GBV spent. UI=" + gbvSpentBefore,
                    gbvSpentBefore, is(0L));

            step("Cross-check baseline against the mission detail API (expected 0 GBV progress)");
            long apiGbvBefore = loyaltyApi.getMissionGbvProgress(missionSlug, username);
            Assertion.assertThat("API GBV progress must start at 0. API=" + apiGbvBefore,
                    apiGbvBefore, is(0L));

            step("Switch back to native");
            webViewPage.switchToNative();

            step("Create a hotel issued order via API (Sakuku payment) for '" + hotelName + "'");
            Boolean orderResult = hotelApi.createIssuedOrderApi(
                    username, password, startDate, hotelName, adult, night, room,
                    orderFullName, orderNameTitle, guestFullName, guestTitle,
                    currency);
            Assertion.assertThat("Hotel booking via API (Sakuku) is successful", orderResult, is(true));
            String issuedOrderID = String.valueOf(hotelApi.getOrderId());
            Assertion.assertThat("Hotel order ID is generated",
                    issuedOrderID, is(not(emptyOrNullString())));

            //add sleep to wait for order to be processed by mission engine
            step("Wait for 5 seconds to allow mission engine to process the order");
            sleep(5);

            step("Resolve orderDetailId and trigger publish-finish-order via API");
            long orderIdLong = Long.parseLong(issuedOrderID.replaceAll("[^0-9]", ""));
            long orderDetailId = orderApi.getHotelOrderDetailId(issuedOrderID);
            orderApi.publishFinishOrder(orderIdLong, orderDetailId);

            step("Wait for order to reach FINISHED status");
            Assertion.assertThat(
                    "Order must reach FINISHED status so the mission engine can evaluate the transaction.",
                    orderApi.waitForOrderFinished(issuedOrderID, 60, 6), is(true));

            step("Poll mission detail API until GBV progress rises above baseline of " + apiGbvBefore);
            long apiGbvAfter = loyaltyApi.waitForGbvProgressAbove(missionSlug, username, apiGbvBefore, 12, 5);
            Assertion.assertThat(
                    "Finishing a mission-eligible hotel order must credit GBV progress. "
                            + "apiGbv=" + apiGbvAfter + ", baseline=" + apiGbvBefore,
                    apiGbvAfter, is(greaterThan(apiGbvBefore)));

            step("Open mission page again to verify UI reflects the increased GBV spent amount");
            missionPage.openMissionDeeplink(missionSlug);
            homePage.closeCurrencyCountryPopup();
            webViewPage.switchToWebViewUrl("mission");
            missionPage.refreshMissionPage();
            Assertion.assertThat("GBV progress bar should be visible",
                    missionPage.isGbvProgressVisible(), is(true));

            step("Verify the mission UI GBV spent reflects the API-credited spend of " + apiGbvAfter);
            Assertion.assertThat(
                    "Mission UI GBV spent amount must reflect the API-credited spend of " + apiGbvAfter
                            + " (the UI shows an abbreviated figure, so its significant digits must match "
                            + "the API value — a stale/non-zero-but-wrong amount must not pass).",
                    missionPage.waitForGbvSpentMatchingApi(apiGbvAfter, 6, 5), is(true));

            step("Switch back to native for cleanup");
            webViewPage.switchToNative();

        } finally {
            step("go to home page");
            homePage.goToHomeUsingDeeplinkWithoutCondition();

            step("Cleanup: logout and delete temporary account");
            homePage.logoutAndDeleteTemporaryAccount(accountData, loggedIn);
        }
    }

    @Author(names = {"Ashish"})
    @Module(name = "Rewards")
    @Tribe(name = "LoyaltyCore")
    @Vertical(name = "Platform")
    @Test
    public void verifyNoMissionStampForPayAtHotelApp() throws Exception {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyNoMissionStampForPayAtHotelApp");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        HotelPage<WebDriver> hotelPage = HotelPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        MissionPageWeb<?> missionPage = new MissionPageWeb<>(session);
        OrderApi<?> orderApi = new OrderApi<>(session);
        LoyaltyApi<?> loyaltyApi = new LoyaltyApi<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String missionSlug = param.get("missionSlug") != null ? param.get("missionSlug") : "stay-with-benefits";
        String currency = param.get("currency") != null ? param.get("currency") : "IDR";
        String masterTag = param.get("masterTag") != null ? param.get("masterTag") : "Pay at Hotel";
        // Check-in date is driven by availability: the PAH finder scans the configured dateRange window
        // (set a wide range like "1-60") and picks whichever date actually has Pay at Hotel availability.
        String dateRange = param.get("dateRange");
        int totalNight = Integer.parseInt(param.get("numberOfNights"));
        int totalRoom = Integer.parseInt(param.get("numberOfRooms"));
        int totalAdult = Integer.parseInt(param.get("numberOfAdults"));
        String cardNumber = param.get("cardNumber");
        String expiry = param.get("cardExpiry");
        String cvv = param.get("cardCvv");
        String cardName = param.get("cardHolderName");

        boolean isLoggedIn = false;
        try {
            step("Open app");
            homePage.gotoHome();

            step("login as " + username + " via deeplink");
            homePage.loginWithSpecificAccountViaDeeplinkOnly(username, password);
            isLoggedIn = true;

            step("Change currency to " + currency);
            homePage.changeCurrency(currency, username);

            step("Open the mission page and check whether the user still needs to join");
            missionPage.openMissionDeeplink(missionSlug);
            homePage.closeCurrencyCountryPopup();
            webViewPage.switchToWebViewUrl("mission");
            // Same guard as LoyaltyRewardsTest web: re-join on an already-enrolled sheet account
            // returns 4xx from joinMission and fails the run even though the PAH intent is unchanged.
            if (missionPage.isJoinMissionRequired()) {
                step("Join the mission '" + missionSlug + "' via API for " + username);
                loyaltyApi.joinMission(missionSlug, username);

                step("Refresh the mission page and verify join status (join CTA gone)");
                missionPage.refreshMissionPage();
                Assertion.assertThat(
                        "The 'Swipe to join' CTA must be gone after enrolling via API.",
                        missionPage.isJoinMissionRequired(), is(false));
            } else {
                step("User has already joined the mission - continuing without re-joining");
            }

            step("Read the baseline stamp count for the joined account");
            Assertion.assertThat("Stamp summary should be visible after joining",
                    missionPage.isStampSummaryVisible(), is(true));
            int stampsBefore = missionPage.getCollectedStampCount();
            int totalStamps = missionPage.getTotalStampCount();
            step("BASELINE mission stamps: collected=" + stampsBefore + ", total=" + totalStamps);

            step("Cross-check baseline against the mission detail API");
            Assertion.assertThat("API total stamps must match UI",
                    loyaltyApi.getMissionTotalStamps(missionSlug, username), is(totalStamps));

            step("Switch back to native");
            webViewPage.switchToNative();

            step("Go to native home before booking Pay at Hotel");
            homePage.goToHomeUsingDeeplinkWithoutCondition();

            step("Find a Pay at Hotel PDP and open it via deeplink");
            PayAtHotelRoom selectedPah = hotelPage.openFirstAvailablePayAtHotelPdpDeeplink(
                    dateRange, totalNight, totalRoom, totalAdult);
            Assertion.assertThat(
                    "Pay at Hotel is available - one of the PAH Hotel items opened a PDP with an available room.",
                    selectedPah, is(notNullValue()));

            step("Go to the room list");
            detailPage.RemovePopUp();
            detailPage.seeHotelRoom();
            detailPage.RemovePopUp();

            step("Select the Pay at Hotel master tag '" + masterTag + "' in the room list");
            roomListPage.clickMastertag(masterTag);

            step("Select the first Pay at Hotel room and book it");
            roomListPage.getTheFirstHotelRoomPrice();
            roomListPage.clickBookRoom();

            step("Fill the guest details on the booking form");
            bookingFormPage.fillGuestDetail(false);

            step("Continue to the payment page");
            bookingFormPage.continueToPaymentPage();
            bookingFormPage.PriceChangesPopUp();

            step("Capture the order ID from the payment page");
            String orderID = paymentPage.checkOrderID();
            Assertion.assertThat("Order ID should be generated for the PAH booking",
                    orderID != null && !orderID.isEmpty(), is(true));

            step("Input and submit the credit card details for the PAH guarantee");
            paymentPage.fillCCDetailRealIssued(cardNumber, expiry, cvv, cardName);
            paymentPage.clickPayButton();
            paymentPage.verifyCreditCard3ds();

            step("Verify the payment succeeded and the thank-you page is shown");
            Assertion.assertThat("Payment successful and thank-you page is shown for the PAH booking",
                    paymentPage.isThankYouPageShown(), is(true));

            paymentPage.seeOrderList();

            step("Wait for the order to reach issued status before publish-finish (issuance can lag thank-you)");
            String issuedStatus = orderListPage.waitForPaymentSuccessful(orderID, 6);
            Assertion.assertThat(
                    "Order " + orderID + " must reach an issued status (ETICKET_ISSUED/PROCESSING_ETICKET) "
                            + "before publish-finish — otherwise finish/mission checks fail on an unissued order.",
                    issuedStatus.equalsIgnoreCase("ETICKET_ISSUED")
                            || issuedStatus.equalsIgnoreCase("PROCESSING_ETICKET"),
                    is(true));

            step("Resolve orderDetailId for the PAH order");
            long orderIdLong = Long.parseLong(Objects.requireNonNull(orderID).replaceAll("[^0-9]", ""));
            long orderDetailId = orderApi.getHotelOrderDetailId(orderID);

            step("Trigger publish-finish-order and wait for FINISHED, re-publishing if the order does not "
                    + "finish (the publish-finish event is sometimes not consumed on the first hit)");
            Assertion.assertThat(
                    "Order " + orderID + " must reach FINISHED status (with retried publish-finish-order) so "
                            + "the mission engine can evaluate the Pay at Hotel transaction.",
                    orderApi.publishFinishOrderUntilFinished(orderIdLong, orderDetailId, 3, 60, 6), is(true));

            step("Cross-check via the mission detail API that the finished Pay at Hotel order grants NO stamp - "
                    + "poll a bounded settle window so a delayed/async stamp increment cannot slip through");
            Assertion.assertThat(
                    "Pay at Hotel must NOT grant a mission stamp: the mission detail API collected-stamp count "
                            + "for " + username + " must stay at the baseline (" + stampsBefore + ") across the whole "
                            + "post-FINISHED settle window. Any increase means a stamp was wrongly awarded for a "
                            + "Pay at Hotel transaction.",
                    loyaltyApi.isStampCountStableAt(missionSlug, username, stampsBefore, 6, 10), is(true));

            step("Re-open the mission page and verify the UI stamp count is unchanged");
            missionPage.openMissionDeeplink(missionSlug);
            homePage.closeCurrencyCountryPopup();
            webViewPage.switchToWebViewUrl("mission");
            Assertion.assertThat("Stamp summary should be visible after the PAH order",
                    missionPage.isStampSummaryVisible(), is(true));
            int stampsAfter = missionPage.getCollectedStampCount();
            step("AFTER PAH booking + finished order: UI collected=" + stampsAfter + ", total=" + totalStamps);
            Assertion.assertThat(
                    "PAY AT HOTEL MUST NOT GRANT A MISSION STAMP. After completing a Pay at Hotel booking, "
                            + "issuing the order and triggering the publish-finish-order event, the collected mission "
                            + "stamp count must stay EXACTLY the same as the baseline (" + stampsBefore + "/"
                            + totalStamps + "). A higher count means a stamp was wrongly awarded for a Pay at Hotel "
                            + "transaction.",
                    stampsAfter, is(stampsBefore));

            step("Switch back to native for cleanup");
            webViewPage.switchToNative();
        } finally {
            // RetryAnalyzer.exception() would attach the real failure under "Cleanup" instead of under
            // the step that actually failed (e.g. thank-you page / login).
            // Also swallow cleanup exceptions so they cannot mask the original test failure.
            try {
                step("go to home page");
                homePage.goToHomeUsingDeeplinkWithoutCondition();
            } catch (Throwable cleanupError) {
                log("Cleanup: goToHomeUsingDeeplinkWithoutCondition failed (ignored): " + cleanupError.getMessage());
            }
            try {
                step("Cleanup: logout the test account");
                homePage.logoutAccount(isLoggedIn);
            } catch (Throwable cleanupError) {
                log("Cleanup: logoutAccount failed (ignored): " + cleanupError.getMessage());
            }
        }
    }

    @Author(names = {"Vanshika Agarwal"})
    @Module(name = "LoyaltyCore")
    @Tribe(name = "Eiffel")
    @Vertical(name = "Hotel")
    @Test
    public void verifyEarningPoints() throws InterruptedException, ExecutionException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyEarningPoints");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);
        LoyaltyApi<?> loyaltyApi = new LoyaltyApi<>(session);
        LoyaltyPointPageWeb<?> loyaltyPointPageWeb = LoyaltyPointPageWeb.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);

        String username = param.get("username");
        String password = param.get("password");
        String location = param.get("location");
        String paymentMethod = param.get("paymentMethod");

        step("user open app");
        homePage.gotoHome();

        step("login as " + username + " via deeplink");
        homePage.loginWithSpecificAccountViaDeeplinkOnly(username, password);
        homePage.changeCurrency("IDR",username);

        step("Open hotel landing page");
        homePage.openVerticalPageDeeplink("Hotels","","");

        step("Search stay location");
        landingPage.searchStayLocation(location);

        step("Submit search");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        step("Go to the room list");
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();

        step("Select room from the room list");
        roomListPage.openRoomDetailPage();
        detailPage.clickBookButton(1);
        bookingFormPage.cancelRatePLanBottomSheetBookingPart1();

        step("Fill guest details");
        bookingFormPage.fillGuestDetail(false);

        step("Continue to Add-ons page");
        bookingFormPage.continueToAddOnPage();

        step("Continue to Payment page");
        bookingFormPage.continueToPaymentPage();
        bookingFormPage.PriceChangesPopUp();

        step("Fetch order id");
        String orderId=paymentPage.checkOrderID();

        step("Payment Submission");
        paymentPage.openPaymentMethod();
        paymentPage.selectPaymentMethod(paymentMethod);
        paymentPage.clickPayButton();
        paymentPage.paymentChecker();
        
        step("Go To Homepage");
        homePage.goToHomeUsingDeeplinkWithoutCondition();

        step("Navigate to Rewards Page and get pending points");
        String uiPendingPointsStr = homePage.getPendingTiketPointsInRewardsPage();
        int uiPendingPoints = Integer.parseInt(uiPendingPointsStr.replaceAll("[^0-9]", ""));
        Assertion.assertThat("UI Pending points should be greater than 0", uiPendingPoints, greaterThan(0));

        step("Fetch Account ID dynamically");
        long accountId = loyaltyApi.getAccountId(username);


        step("Verify pending-list API contains orderId, orderType, pointStatus and matching points");
        loyaltyPointPageWeb.verifyPendingEarningPoint(
                accountId,
                orderId,
                uiPendingPoints,
                "HOTEL_PAID",
                "Earned",
                5,
                10);
    }



}
