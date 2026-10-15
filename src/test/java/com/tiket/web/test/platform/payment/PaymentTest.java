package com.tiket.web.test.platform.payment;

import org.json.JSONObject;
import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.apiPage.FlightApi;
import com.tiket.apiPage.PaymentApi;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Environment;
import com.tiket.model.Platform;
import com.tiket.model.Session;
import com.tiket.pojo.flight.FlightDynamicData;
import com.tiket.pojo.paymentDweb.BCAVASnapInquiryResponse;
import com.tiket.pojo.paymentDweb.BCAVASnapPaymentResponse;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.testbase.Configs;
import com.tiket.web.WebTest;
import com.tiket.web.dWeb.page.accommodation.hotels.HotelRoomListPage;
import com.tiket.web.dWeb.page.payment.PaymentApiHitterPage;
import com.tiket.web.dWeb.page.payment.PaymentPage;
import com.tiket.web.dWeb.page.reschedule.ReschedulePage;
import com.tiket.web.dashboard.page.order.CreateCarRentalOrderPage;
import com.tiket.web.dashboard.page.payment.AvsCheckConfiguration;
import com.tiket.web.dashboard.page.payment.PaymentDashboardPage;
import com.tiket.web.dashboard.page.order.orderCreationPage;
import com.tiket.web.page.accommodation.hotels.BookingForm.HotelBookingFormPageWeb;
import com.tiket.web.page.accommodation.hotels.hotelLandingPage.HotelLandingPageWeb;
import com.tiket.web.page.common.home.HomePageWeb;
import com.tiket.web.page.common.home.LoginPageWeb;
import com.tiket.web.page.corporate.dashboard.CorporateDashboard;
import com.tiket.web.page.corporate.travel_plan.TravelPlanWeb;
import com.tiket.web.page.flight.reschedule.FlightReschedulePageWeb;
import com.tiket.web.page.flight.rescheduleCart.FlightRescheduleCartWeb;
import com.tiket.web.page.order.OrderDetail.OrderDetailWeb;
import com.tiket.web.page.flight.bookingForm.FlightBookingFormPageWeb;
import com.tiket.web.page.flight.landingpage.FlightLandingPageWeb;
import com.tiket.web.page.flight.productReview.FlightProductReviewPageWeb;
import com.tiket.web.page.flight.searchresultpage.FlightSearchResultPageWeb;
import com.tiket.web.page.order.OrderList.OrderListWeb;
import com.tiket.web.page.payment.SavedPaymentMethod.SavedPaymentMethodWeb;
import com.tiket.web.page.platform.payment.PaymentPageHelperWeb;
import com.tiket.web.page.platform.payment.PaymentPageWeb;
import com.tiket.web.page.ttd.TtdBookingFormPage.TtdBookingFormPageWeb;
import com.tiket.web.page.ttd.TtdLandingPage.TtdLandingPageWeb;
import com.tiket.web.page.ttd.TtdProductDetailPage.TtdProductDetailPageWeb;
import org.openqa.selenium.WebDriver;
import org.testng.SkipException;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import javax.security.auth.login.FailedLoginException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static com.tiket.core.CoreFunctions.*;
import static com.tiket.testdata.TestData.getObjectParams;
import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;

public class PaymentTest extends WebTest {

    @Author(names = {"Hiten"})
    @Module(name = "PaymentMethod")
    @Tribe(name = "Payment")
    @Vertical(name = "Platform")
    @Test(dataProvider = "verifyInstantPaymentMethodWebDataProvider")
    public void verifyInstantPaymentMethodWeb(Map<String, String> map) throws InterruptedException , FailedLoginException {
        Session<WebDriver> session = sessionThreadLocal.get();
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);
        orderCreationPage<?> orderCreationPage = new orderCreationPage<>(session);
        PaymentPageHelperWeb<?> paymentHelperPage = PaymentPageHelperWeb.getInstance(session);
        String email = map.get("email");
        String passwordDweb = map.get("passwordDweb");
        String query = map.get("query");
        String startDate = generateRandomDate(map.get("rangeAddDate"));
        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String fullName = randomName() + " Autorder";
        String nameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " Autguest";
        String config = map.get("config");
        String database = map.get("database");
        String collectionName = map.get("collectionName");
        String paymentGroup = map.get("paymentGroup");
        String paymentMethod = map.get("paymentMethod");
        int serviceCharge = Integer.parseInt(map.get("serviceCharge"));
        String otp = map.get("otp");

        step("Create Car Rental order via API and fetch the orderID");
        orderCreationPage.createWaitingPaymentOrderHotel(email, passwordDweb, startDate, query, 1, 1, 1, fullName, nameTitle, guestFullName, nameTitle,"IDR");
        String orderIdCreated = String.valueOf(orderCreationPage.getOrderId());
        String orderHashCreated = orderCreationPage.getOrderHash();

        step("Login and select payment method");
        int finalPrice = paymentHelperPage.loginViaCookieAndSelectPaymentMethod(email, passwordDweb, orderIdCreated, orderHashCreated, paymentGroup, paymentMethod, serviceCharge);

        step("Click on Pay button");
        paymentPage.clickOnPaymentButton();

        if(!paymentMethod.equals("Sakuku") && !paymentMethod.equals("LinkAja")){
            step("Verify confirmation page");
            Assertion.assertThat("Verify the confimation page should be loaded", paymentPage.isPaymentConfirmationPageLoaded(), is(true));
        }

        if(!paymentMethod.equals("Indomaret") && !paymentMethod.equals("Alfamart / Alfamidi / Lawson / Dan+Dan")){
            step("Verify that payment successful page is visible");
            Assertion.assertThat("Verify the payment successful page is visible", paymentPage.isPaymentSuccessfulVisible(), is(true));

            step("Verify the status from database");
            String queryDb = paymentPage.queryString(orderIdCreated);
            Map<String,String> paymentTransactionDetails = paymentPage.getPaymentTransactionDetails(config, database, collectionName, queryDb);
            String paymentStatus = paymentTransactionDetails.get("paymentStatus");
            Assertion.assertThat("Verify the payment status is paid",paymentStatus , is("PAID"));
        }
    }

    @DataProvider(name = "verifyInstantPaymentMethodWebDataProvider")
    public Object[][] verifyInstantPaymentMethodWebDataProvider() {return getObjectParams("verifyInstantPaymentMethodWeb");}


    @Author(names = { "Hiten", "Yoan","Shubham Bansal"})
    @Module(name = "PaymentMethod")
    @Tribe(name = "Payment")
    @Vertical(name = "Platform")
    @Test(dataProvider = "verifyVirtualAccountPaymentMethodWebDataProvider")
    public void verifyVirtualAccountPaymentMethodWeb(Map<String, String> map) throws InterruptedException , FailedLoginException {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(map);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);
        CreateCarRentalOrderPage<?> carRentalOrderPage = new CreateCarRentalOrderPage<>(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);

        String email = map.get("email");
        String phoneNumber = map.get("phoneNumber");
        String passwordDweb = map.get("passwordDweb");
        String driverOption = map.get("driverOption");
        String location = map.get("location");
        String startDate = generateRandomDate(map.get("rangeAddDate"));
        String type = map.get("type");
        String pickupTestData = map.get("pickupTime");
        StringBuilder pickup = new StringBuilder(pickupTestData);
        pickup.setCharAt(2, ':');
        String pickupTime = pickup.toString();
        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String fullName = randomName() + " Autorder";
        String nameTitle = getRandomObject(salutationList);
        int rentalDuration = Integer.parseInt(map.get("rentalDuration"));
        String paymentGroup = map.get("paymentGroup");
        String paymentMethod = map.get("paymentMethod");

        step("Create Car Rental order via API and fetch the orderID");
        Map<String, String> orderIdAndHash = carRentalOrderPage.createCarRentalProductTillPaymentApi(email, phoneNumber, passwordDweb, driverOption, location, startDate, pickupTime, type, fullName, nameTitle, rentalDuration);
        String orderIdCreated = orderIdAndHash.get("orderId");
        String orderHashCreated = orderIdAndHash.get("orderHash");

        step("Login as " + email + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(email, passwordDweb);

        step("Select english language if not in english");
        homePage.selectEnglishLanguageOnly();

        step("Go to payment page");
        paymentPage.goToPaymentPage(orderIdCreated, orderHashCreated);

        step("Verify payment page is loaded");
        Assertion.assertThat("Verify payment page is loaded or not", paymentPage.isPaymentPageLoaded(), is(true));

        step("Access payment group");
        paymentPage.clickOnSelectPaymentMethod();

        step("Scroll down to access selected payment group");
        paymentPage.goToPaymentMethodsGroup(paymentGroup);

        step("Select payment group");
        paymentPage.clickOnPaymentGroup(paymentGroup);

        step("Select Payment Method");
        paymentPage.selectPaymentMethod(paymentMethod);
        Assertion.assertThat("Verify the selected payment", paymentPage.getSelectedPayment(), is(paymentMethod));

        step("Open price history");
        paymentPage.openPriceSummary();

        step("Verify convenience fee if available");
        paymentPage.logConvenienceFeeIfPresent();

        step("Close the price history");
        paymentPage.closeDrawer();

        step("Click on Pay button");
        paymentPage.clickOnPaymentButton();

        step("Verify confirmation page");
        Assertion.assertThat("Verify the confirmation page is loaded", paymentPage.isPaymentConfirmationPageLoaded(), is(true));

        step("Verify the virtual account number is visible");
        Assertion.assertThat("Verify the virtual account number is visible", paymentPage.isVirtualAccountInfoVisible(), is(true));

    }
    @DataProvider(name = "verifyVirtualAccountPaymentMethodWebDataProvider")
    public Object[][] verifyVirtualAccountPaymentMethodWebDataProvider() {return getObjectParams("verifyVirtualAccountPaymentMethodWeb");}

    @Author(names = { "Hiten" })
    @Module(name = "PaymentMethod")
    @Tribe(name = "Payment")
    @Vertical(name = "PROP")
    @Test(dataProvider = "verifyPaymentMethodOctoClicksWebDataProvider")
    public void verifyPaymentMethodOctoClickWeb(Map<String, String> map) throws InterruptedException , FailedLoginException {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PaymentPageWeb<?> landingPage = PaymentPageWeb.getInstance(session);
        PaymentPageHelperWeb<?> paymentHelperPage = PaymentPageHelperWeb.getInstance(session);
        CreateCarRentalOrderPage<?> carRentalOrderPage = new CreateCarRentalOrderPage<>(session);

        String email = map.get("email");
        String phoneNumber = map.get("phoneNumber");
        String passwordDweb = map.get("passwordDweb");
        String driverOption = map.get("driverOption");
        String location = map.get("location");
        String startDate = generateRandomDate(map.get("rangeAddDate"));
        String type = map.get("type");
        String pickupTestData = map.get("pickupTime");
        StringBuilder pickup = new StringBuilder(pickupTestData);
        pickup.setCharAt(2, ':');
        String pickupTime = pickup.toString();
        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String fullName = randomName() + " Autorder";
        String nameTitle = getRandomObject(salutationList);
        int rentalDuration = Integer.parseInt(map.get("rentalDuration"));
        String config = map.get("config");
        String database = map.get("database");
        String collectionName = map.get("collectionName");
        String paymentGroup = map.get("paymentGroup");
        String paymentMethod = map.get("paymentMethod");
        int serviceCharge = Integer.parseInt(map.get("serviceCharge"));
        String otp = map.get("otp");

        step("Create Car Rental order via API and fetch the orderID");
        Map<String,String> orderIdAndHash = carRentalOrderPage.createCarRentalProductTillPaymentApi(email, phoneNumber,passwordDweb ,driverOption, location, startDate, pickupTime, type, fullName, nameTitle, rentalDuration);
        String orderIdCreated = orderIdAndHash.get("orderId");
        String orderHashCreated = orderIdAndHash.get("orderHash");

        step("Login and select payment method");
        int finalPrice = paymentHelperPage.loginAndSelectPaymentMethod(email, passwordDweb, otp, orderIdCreated, orderHashCreated, paymentGroup, paymentMethod, serviceCharge);

        step("Click on Pay button");
        landingPage.clickOnPaymentButton();

        step("Verify octoclicks page loaded");
        Assertion.assertThat("Verify the octoclicks page is loaded", landingPage.isOctoClicksPageLoaded(), is(true));

        step("Verify the status from database");
        String query = landingPage.queryString(orderIdCreated);
        Map<String,String> paymentTransactionDetails = landingPage.getPaymentTransactionDetails(config, database, collectionName, query);
        String paymentStatus = paymentTransactionDetails.get("paymentStatus");
        String paymentAmount = paymentTransactionDetails.get("paymentAmount");
        Assertion.assertThat("Verify the payment status is paid",paymentStatus , is("CHECKOUT"));
        Assertion.assertThat("Verify the paid amount", Double.parseDouble(paymentAmount), is(Double.valueOf(finalPrice)));
    }
    @DataProvider(name = "verifyPaymentMethodOctoClicksWebDataProvider")
    public Object[][] verifyPaymentMethodOctoClicksWebDataProvider() {return getObjectParams("verifyPaymentMethodOctoClickWeb");}


    @Author(names = { "Hiten" })
    @Module(name = "PaymentMethod")
    @Tribe(name = "Payment")
    @Vertical(name = "Platform")
    @Test(dataProvider = "verifyOVOAndOneKlikPaymentMethodWebWebDataProvider")
    public void verifyOVOAndOneKlikPaymentMethodWeb(Map<String, String> map) throws InterruptedException , FailedLoginException {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PaymentPageWeb<?> landingPage = PaymentPageWeb.getInstance(session);
        CreateCarRentalOrderPage<?> carRentalOrderPage = new CreateCarRentalOrderPage<>(session);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        String email = map.get("email");
        String phoneNumber = map.get("phoneNumber");
        String passwordDweb = map.get("passwordDweb");
        String driverOption = map.get("driverOption");
        String location = map.get("location");
        String startDate = generateRandomDate(map.get("rangeAddDate"));
        String type = map.get("type");
        String pickupTestData = map.get("pickupTime");
        StringBuilder pickup = new StringBuilder(pickupTestData);
        pickup.setCharAt(2, ':');
        String pickupTime = pickup.toString();
        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String fullName = randomName() + " Autorder";
        String nameTitle = getRandomObject(salutationList);
        int rentalDuration = Integer.parseInt(map.get("rentalDuration"));
        String paymentGroup = map.get("paymentGroup");
        String paymentMethod = map.get("paymentMethod");
        String otp = map.get("otp");

        step("Create Car Rental order via API and fetch the orderID");
        Map<String,String> orderIdAndHash = carRentalOrderPage.createCarRentalProductTillPaymentApi(email, phoneNumber,passwordDweb ,driverOption, location, startDate, pickupTime, type, fullName, nameTitle, rentalDuration);
        String orderIdCreated = orderIdAndHash.get("orderId");
        String orderHashCreated = orderIdAndHash.get("orderHash");

        step("Login into dweb if not login using the given account");
        loginPage.loginIfNotWithSameEmail(email,passwordDweb,otp,0);

        step("Select english language if not in english");
        homePage.selectEnglishLanguageOnly();

        step("Go to the payment page");
        landingPage.goToPaymentPage(orderIdCreated, orderHashCreated);

        step("Verify payment page is loaded or not");
        Assertion.assertThat("Verify the payment page is loaded or not", landingPage.isPaymentPageLoaded(), is(true));

        step("Click on select payment method");
        landingPage.clickOnSelectPaymentMethod();

        step("Scroll down to the payment Group");
        landingPage.goToPaymentMethodsGroup(paymentGroup);

        if(paymentGroup.equals("NO CARD INSTALLMENT")){
            landingPage.isOVODisabled();
        }

        step("Verify the payment method is not available");
        Assertion.assertThat("Verify the payment method is not available", landingPage.isPaymentMethodVisible(paymentMethod), is(false));
    }
    @DataProvider(name = "verifyOVOAndOneKlikPaymentMethodWebWebDataProvider")
    public Object[][] verifyOVOAndOneKlikPaymentMethodWebWebDataProvider() {return getObjectParams("verifyOVOAndOneKlikPaymentMethodWeb");}

    @Author(names = { "Hiten","Yoan" })
    @Module(name = "PaymentMethod")
    @Tribe(name = "Payment")
    @Vertical(name = "Platform")
    @Test(dataProvider = "verifyPaymentMethodUsingCardWebDataProvider")
    public void verifyPaymentMethodUsingCardWeb(Map<String, String> map) throws InterruptedException , FailedLoginException {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);
        PaymentPageHelperWeb<?> paymentHelperPage = PaymentPageHelperWeb.getInstance(session);
        CreateCarRentalOrderPage<?> carRentalOrderPage = new CreateCarRentalOrderPage<>(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);

        String email = map.get("email");
        String phoneNumber = map.get("phoneNumber");
        String passwordDweb = map.get("passwordDweb");
        String driverOption = map.get("driverOption");
        String location = map.get("location");
        String startDate = generateRandomDate(map.get("rangeAddDate"));
        String type = map.get("type");
        String pickupTestData = map.get("pickupTime");
        StringBuilder pickup = new StringBuilder(pickupTestData);
        pickup.setCharAt(2, ':');
        String pickupTime = pickup.toString();
        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String fullName = randomName() + " Autorder";
        String nameTitle = getRandomObject(salutationList);
        int rentalDuration = Integer.parseInt(map.get("rentalDuration"));
        String config = map.get("config");
        String database = map.get("database");
        String collectionName = map.get("collectionName");
        String paymentGroup = map.get("paymentGroup");
        String paymentMethod = map.get("paymentMethod");
        int serviceCharge = Integer.parseInt(map.get("serviceCharge"));
        String cardNumber = map.get("cardNumber");
        String cardMonth = map.get("cardMonth");
        String cvv = map.get("cvv");
        String cardHolderName = map.get("cardHolderName");
        String otp = map.get("otp");

        step("Create Car Rental order via API and fetch the orderID");
        Map<String, String> orderIdAndHash = carRentalOrderPage.createCarRentalProductTillPaymentApi(email, phoneNumber, passwordDweb, driverOption, location, startDate, pickupTime, type, fullName, nameTitle, rentalDuration);
        String orderIdCreated = orderIdAndHash.get("orderId");
        String orderHashCreated = orderIdAndHash.get("orderHash");

        step("Login as " + email + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(email, passwordDweb);

        step("Select english language if not in english");
        homePage.selectEnglishLanguageOnly();

        step("Go to payment page");
        paymentPage.goToPaymentPage(orderIdCreated, orderHashCreated);

        String orderId = paymentPage.getOrderId();
        int beforePrice =(int) Double.parseDouble(paymentPage.getFinalPrice());

        step("Verify payment page is loaded");
        Assertion.assertThat("Verify payment page is loaded or not", paymentPage.isPaymentPageLoaded(), is(true));

        step("Access payment group");
        paymentPage.clickOnSelectPaymentMethod();

        step("Scroll down to access selected payment group");
        paymentPage.goToPaymentMethodsGroup(paymentGroup);

        step("Select payment group");
        paymentPage.clickOnPaymentGroup(paymentGroup);

        step("Select Payment Method");
        paymentPage.selectPaymentMethod(paymentMethod);
        Assertion.assertThat("Verify the selected payment", paymentPage.getSelectedPayment(), is("Credit Card"));

        step("Input the card details");
        paymentHelperPage.inputCardDetails(cardNumber, cardMonth, cvv, cardHolderName);

        step("Open price history");
        paymentPage.openPriceSummary();

        step("Fetch the price after payment method is applied");
        int afterPrice = (int) Double.parseDouble(paymentPage.getFinalPrice());

        step("Verify the service charge after payment is applied");
        Assertion.assertThat("Verify the service charge", (afterPrice - beforePrice), is(serviceCharge));

        step("Close the price history");
        paymentPage.closeDrawer();

        step("Route card payment to Xendit via API");
        paymentPage.routeCardPaymentToXendit();

        step("Click on Pay button");
        paymentPage.clickOnPaymentButton();

        step("Click submit on 3ds page");
        paymentPage.handle3DSPage(10);

        step("Verify the payment successful page is visible or not");
        Assertion.assertThat("Verify the payment successful page is visible", paymentPage.isPaymentSuccessfulVisible(), is(true));

        step("Verify the status from database");
        String query = paymentPage.queryString(orderId);
        Map<String,String> paymentTransactionDetails = paymentPage.getPaymentTransactionDetails(config, database, collectionName, query);
        String paymentStatus = paymentTransactionDetails.get("paymentStatus");
        String paymentAmount = paymentTransactionDetails.get("paymentAmount");
        Assertion.assertThat("Verify the payment status is paid",paymentStatus , is("PAID"));
        Assertion.assertThat("Verify the paid amount", Double.parseDouble(paymentAmount), is(Double.valueOf(afterPrice)));
    }

    @DataProvider(name = "verifyPaymentMethodUsingCardWebDataProvider")
    public Object[][] verifyPaymentMethodUsingCardWebDataProvider() {return getObjectParams("verifyPaymentMethodUsingCardWeb");}

    @Author(names = {"Hiten","Yoan","Shubham Bansal"})
    @Module(name = "PaymentMethod")
    @Tribe(name = "Payment")
    @Vertical(name = "Platform")
    @Test(dataProvider = "verifyPaymentMethodUsingCardNONIDRLoginWebDataProvider")
    public void verifyPaymentMethodUsingCardNONIDRLoginWeb(Map<String, String> map) throws InterruptedException, FailedLoginException, ExecutionException {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PaymentPageWeb<?> landingPage = PaymentPageWeb.getInstance(session);
        PaymentPageHelperWeb<?> paymentHelperPage = PaymentPageHelperWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        orderCreationPage<?> orderCreationPage = new orderCreationPage<>(session);

        String username = map.get("username");
        String password = map.get("password");
        String currency = map.get("currency");
        String cardNumber = map.get("cardNumber");
        String expiry = map.get("expiry");
        String cvv = map.get("cvv");
        String cardName = map.get("cardName");
        String query = map.get("hotelName");
        String startDate = generateRandomDate(map.get("rangeAddDate"));
        int adult = Integer.parseInt(map.get("adult"));
        int night = Integer.parseInt(map.get("night"));
        int room = Integer.parseInt(map.get("room"));
        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = getRandomObject(salutationList);
        String language = map.get("language");
        String otp = map.get("otp");
        String paymentGroup = map.get("paymentGroup");
        String paymentMethod = map.get("paymentMethod");
        int serviceCharge = Integer.parseInt(map.get("serviceCharge"));

        step("Create new NON-IDR hotel order");
        Map<String,String> orderIdAndHash = orderCreationPage.createWaitingPaymentOrderApi(username, password, startDate, query, adult, night, room, orderFullName, orderNameTitle, guestFullName, guestTitle, currency, language);
        String orderIdCreated = orderIdAndHash.get("orderId");
        String orderHashCreated = orderIdAndHash.get("orderHash");

        step("Login as " + username + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(username, password);

        step("Select english language if not in english");
        homePage.selectEnglishLanguageOnly();

        step("Go to the payment page");
        landingPage.goToPaymentPage(orderIdCreated, orderHashCreated);

        int beforePrice =(int) Double.parseDouble(landingPage.getFinalPrice());

        step("Verify payment page is loaded or not");
        Assertion.assertThat("Verify the payment page is loaded or not", landingPage.isPaymentPageLoaded(), is(true));

        step("Verify recommended payment or select payment method");
        if (!landingPage.isSeeAllPaymentMethodDisplayed()) {
            Assertion.assertThat("Verify the recommended payment", landingPage.getSelectedPayment(), containsString("Card"));
        } else {
            landingPage.clickOnSelectPaymentMethod();
            landingPage.goToPaymentMethodsGroup(paymentGroup);
            landingPage.clickOnPaymentGroup(paymentGroup);
            landingPage.selectPaymentMethod(paymentMethod);
            Assertion.assertThat("Verify the selected payment", landingPage.getSelectedPayment(), containsString("Card"));
        }

        step("Input the card details");
        paymentHelperPage.inputCardDetails(cardNumber, expiry, cvv, cardName);

        step("Open price history");
        landingPage.openPriceSummary();

        step("Fetch the price after payment method is applied");
        int afterPrice = (int) Double.parseDouble(landingPage.getFinalPrice());

        step("Verify the service charge after payment is applied");
        Assertion.assertThat("Verify the service charge", (afterPrice - beforePrice), is(serviceCharge));

        step("Close the price history");
        landingPage.closeDrawer();

        step("Click on Pay button");
        landingPage.clickOnPaymentButton();

        step("Handle 3DS page of Juspay if shown");
        landingPage.handle3DSPage(30);

        step("Verify the payment successful page is visible or not");
        Assertion.assertThat("Verify the payment successful page is visible", landingPage.isPaymentSuccessfulVisible(), is(true));

    }

    @DataProvider(name = "verifyPaymentMethodUsingCardNONIDRLoginWebDataProvider")
    public Object[][] verifyPaymentMethodUsingCardNONIDRLoginWebDataProvider() {return getObjectParams("verifyPaymentMethodUsingCardNONIDRLoginWeb");}

    @Author(names = { "Hiten" })
    @Module(name = "PaymentMethod")
    @Tribe(name = "Payment")
    @Vertical(name = "PROP")
    @Test(dataProvider = "verifyPaymentMethodUsingCardNonLoginWebDataProvider")
    public void verifyPaymentMethodUsingCardNonLoginWeb(Map<String, String> map) throws InterruptedException , FailedLoginException {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);
        orderCreationPage<?> orderCreationPage = new orderCreationPage<>(session);
        PaymentPageHelperWeb<?> paymentHelperPage = PaymentPageHelperWeb.getInstance(session);

        String productUrlName = map.get("productUrlName");
        String currency = map.get("currency");
        String email = map.get("email");
        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String fullName = randomName() + " Autorder";
        String nameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = getRandomObject(salutationList);
        String config = map.get("config");
        String database = map.get("database");
        String collectionName = map.get("collectionName");
        String paymentGroup = map.get("paymentGroup");
        String paymentMethod = map.get("paymentMethod");
        int serviceCharge = Integer.parseInt(map.get("serviceCharge"));
        String cardNumber = map.get("cardNumber");
        String cardMonth = map.get("cardMonth");
        String cvv = map.get("cvv");
        String cardHolderName = map.get("cardHolderName");

        step("Create Car Rental order via API and fetch the orderID");
        orderCreationPage.createGuestWaitingPaymentOrderApiTodo(productUrlName, true, currency, fullName, nameTitle, guestFullName, email, guestTitle);
        String orderIdCreated = String.valueOf(orderCreationPage.getOrderId());
        String orderHashCreated = orderCreationPage.getOrderHash();

        step("Logout from dweb if already login");
        homePage.goToHomePage();
        loginPage.loginAsGuest();

        step("Select english language if not in english");
        homePage.selectEnglishLanguageOnly();

        step("Go to the payment page");
        paymentPage.goToPaymentPage(orderIdCreated, orderHashCreated);

        step("Verify payment page is loaded or not");
        Assertion.assertThat("Verify the payment page is loaded or not", paymentPage.isPaymentPageLoaded(), is(true));

        String orderId = paymentPage.getOrderId();
        int beforePrice = (int) Double.parseDouble(paymentPage.getFinalPrice());

        step("Click on select payment method");
        paymentPage.clickOnSelectPaymentMethod();

        step("Scroll down to the payment Group");
        paymentPage.goToPaymentMethodsGroup(paymentGroup);

        step("Select Payment Method");
        paymentPage.selectCreditCard();
        Assertion.assertThat("Verify the selected payment", paymentPage.getSelectedPayment(), is("Credit Card"));

        step("Fetch the price after payment method is applied");
        int finalPrice = (int) Double.parseDouble(paymentPage.getFinalPrice());

        step("Verify the service charge after payment is applied");
        Assertion.assertThat("Verify the service charge", (finalPrice-beforePrice), is(serviceCharge));

        step("Input the card details");
        paymentHelperPage.inputCardDetails(cardNumber , cardMonth , cvv , cardHolderName);

        step("Click on Pay button");
        paymentPage.clickOnPaymentButton();

        step("Handle 3DS page if shown");
        paymentPage.handle3DSPage(10);

        step("Verify the payment successful page is visible or not");
        Assertion.assertThat("Verify the payment successful page is visible", paymentPage.isPaymentSuccessfulVisible(), is(true));

        step("Verify the status from database");
        String query = paymentPage.queryString(orderId);
        Map<String,String> paymentTransactionDetails = paymentPage.getPaymentTransactionDetails(config, database, collectionName, query);
        String paymentStatus = paymentTransactionDetails.get("paymentStatus");
        Assertion.assertThat("Verify the payment status is paid",paymentStatus , is("PAID"));
    }

    @DataProvider(name = "verifyPaymentMethodUsingCardNonLoginWebDataProvider")
    public Object[][] verifyPaymentMethodUsingCardNonLoginWebDataProvider() {return getObjectParams("verifyPaymentMethodUsingCardNonLoginWeb");}

    @Author(names = { "Hiten","Shubham Bansal"})
    @Module(name = "PaymentMethod")
    @Tribe(name = "Payment")
    @Vertical(name = "PROP")
    @Test(dataProvider = "verifyPaymentMethodShopeepayDataProvider")
    public void verifyPaymentMethodShopeepay(Map<String, String> map) throws InterruptedException , FailedLoginException {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PaymentPageWeb<?> landingPage = PaymentPageWeb.getInstance(session);
        PaymentPageHelperWeb<?> paymentHelperPage = PaymentPageHelperWeb.getInstance(session);
        CreateCarRentalOrderPage<?> carRentalOrderPage = new CreateCarRentalOrderPage<>(session);

        String email = map.get("email");
        String phoneNumber = map.get("phoneNumber");
        String passwordDweb = map.get("passwordDweb");
        String driverOption = map.get("driverOption");
        String location = map.get("location");
        String startDate = generateRandomDate(map.get("rangeAddDate"));
        String type = map.get("type");
        String pickupTestData = map.get("pickupTime");
        StringBuilder pickup = new StringBuilder(pickupTestData);
        pickup.setCharAt(2, ':');
        String pickupTime = pickup.toString();
        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String fullName = randomName() + " Autorder";
        String nameTitle = getRandomObject(salutationList);
        int rentalDuration = Integer.parseInt(map.get("rentalDuration"));
        String config = map.get("config");
        String database = map.get("database");
        String collectionName = map.get("collectionName");
        String paymentGroup = map.get("paymentGroup");
        String paymentMethod = map.get("paymentMethod");
        int serviceCharge = Integer.parseInt(map.get("serviceCharge"));
        String otp = map.get("otp");

        step("Create Car Rental order via API and fetch the orderID");
        Map<String,String> orderIdAndHash = carRentalOrderPage.createCarRentalProductTillPaymentApi(email, phoneNumber,passwordDweb ,driverOption, location, startDate, pickupTime, type, fullName, nameTitle, rentalDuration);
        String orderIdCreated = orderIdAndHash.get("orderId");
        String orderHashCreated = orderIdAndHash.get("orderHash");

        step("Login and select payment method");
        int finalPrice = paymentHelperPage.loginViaCookieAndSelectPaymentMethod(email, passwordDweb, orderIdCreated, orderHashCreated, paymentGroup, paymentMethod, serviceCharge);

        step("Open price history");
        landingPage.openPriceSummary();

        step("Verify convenience fee if available");
        landingPage.logConvenienceFeeIfPresent();

        step("Close the price history");
        landingPage.closeDrawer();

        step("Click on Pay button");
        landingPage.clickOnPaymentButton();

        step("Verify confirmation page");
        Assertion.assertThat("Verify the confirmation page is loaded", landingPage.isPaymentConfirmationPageLoaded(), is(true));

        step("Verify the status from database");
        String query = landingPage.queryString(orderIdCreated);
        Map<String,String> paymentTransactionDetails = landingPage.getPaymentTransactionDetails(config, database, collectionName, query);
        String paymentStatus = paymentTransactionDetails.get("paymentStatus");
        Assertion.assertThat("Verify the payment status is paid",paymentStatus , is("CHECKOUT"));
    }

    @DataProvider(name = "verifyPaymentMethodShopeepayDataProvider")
    public Object[][] verifyPaymentMethodShopeepayDataProvider() {return getObjectParams("verifyPaymentMethodShopeepay");}

    @Author(names = {"Hiten","Shubham Bansal"})
    @Module(name = "PaymentMethod")
    @Tribe(name = "Payment")
    @Vertical(name = "Platform")
    @Test
    public void verifyLoginNudgeOnPaymentPage() {
        Map<String, String> map = getParam("verifyLoginNudgeOnPaymentPage");
        BaseTest.paramsThreadLocal.set(map);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);
        LoginPageWeb<?> loginPageWeb = LoginPageWeb.getInstance(session);
        HomePageWeb<?> homePageWeb = HomePageWeb.getInstance(session);
        CreateCarRentalOrderPage<?> carRentalOrderPage = new CreateCarRentalOrderPage<>(session);

        String email = map.get("email");
        String phoneNumber = map.get("phoneNumber");
        String driverOption = map.get("driverOption");
        String location = map.get("location");
        String startDate = generateRandomDate(map.get("rangeAddDate"));
        String type = map.get("type");
        String pickupTestData = map.get("pickupTime");
        StringBuilder pickup = new StringBuilder(pickupTestData);
        pickup.setCharAt(2, ':');
        String pickupTime = pickup.toString();
        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String fullName = randomName() + " Autorder";
        String nameTitle = getRandomObject(salutationList);
        int rentalDuration = Integer.parseInt(map.get("rentalDuration"));
        String nudgeTextEn = map.get("nudgeTextEn");
        String nudgeTextId = map.get("nudgeTextId");

        step("Create Car Rental order via API and fetch the orderID");
        Map<String,String> orderIdAndHash = carRentalOrderPage.createGuestCarRentalTillPaymentApi(email, phoneNumber, driverOption, location, startDate, pickupTime, type, fullName, nameTitle, rentalDuration);
        String orderIdCreated = orderIdAndHash.get("orderId");
        String orderHashCreated = orderIdAndHash.get("orderHash");

        step("Logout if already logged in");
        homePageWeb.goToHomePage();
        loginPageWeb.loginAsGuest();

        step("Go to payment page");
        paymentPage.goToPaymentPage(orderIdCreated, orderHashCreated);

        step("Verify login nudge positive text is visible");
        Assertion.assertThat("Login nudge positive text should be visible", paymentPage.isLoginNudgePositiveTextVisible(), is(true));

        step("Click see all payment methods");
        paymentPage.clickSeeAllPaymentMethodNudge();

        step("Verify login nudge wrapper is visible");
        Assertion.assertThat("Login nudge wrapper should be visible", paymentPage.isLoginNudgeWrapperVisible(), is(true));
    }

    @Author(names = {"Yoan"})
    @Module(name = "SavedPaymentMethod")
    @Tribe(name = "Payment")
    @Vertical(name = "Platform")
    @Test

    public void verifySavedPaymentMethodAccessWeb() throws FailedLoginException, InterruptedException{
        Map<String, String> param = getParam("verifySavedPaymentMethodAccessWeb");
        Session<?> session = BaseTest.sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);

        LoginPageWeb<?> loginPageWeb = LoginPageWeb.getInstance(session);
        HomePageWeb<?> homePageWeb = HomePageWeb.getInstance(session);
        SavedPaymentMethodWeb<?> savedPaymentMethodWeb = SavedPaymentMethodWeb.getInstance(session);

        String username = param.get("username");
        String password = param.get("password");

        step("Login as " + username + " via session injection");
        loginPageWeb.loginWithSpecificAccountViaCookie(username, password);

        step("Go to Home Page");
        homePageWeb.goToHomePage();

        step("Open Saved Payment Method page");
        if(Configs.PLATFORM.equals(Platform.DWEB)) {
            homePageWeb.goToSavedPaymentMethod();
        } else {
            homePageWeb.goToAccountPage();
            homePageWeb.goToSavedPaymentMethod();
        }

        step("Check if already in Saved Payment Method Page");
        Assertion.assertThat("Payment Method is Opened", savedPaymentMethodWeb.waitForSavedPaymentMethodTitleAppear(10), is(true));
    }

    @Author(names = {"Vidhi Jindal"})
    @Module(name = "PaymentMethod")
    @Tribe(name = "Payment")
    @Vertical(name = "Platform")
    @Test
    public void verifyBlibliPayOnWeb() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = BaseTest.sessionThreadLocal.get();
        Map<String, String> map = getParam("verifyBlibliPayOnWeb");
        BaseTest.paramsThreadLocal.set(map);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        SavedPaymentMethodWeb<?> savedPaymentMethodWeb = SavedPaymentMethodWeb.getInstance(session);

        String email = map.get("email");
        String password = map.get("password");
        String otp = map.get("otp");

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, 0);

        step("Select english language if not in english");
        homePage.selectEnglishLanguageOnly();

        step("User open Saved Payment Methods page");
        if(Configs.PLATFORM.equals(Platform.DWEB)) {
            homePage.goToSavedPaymentMethod();
        } else {
            homePage.goToAccountPage();
            homePage.goToSavedPaymentMethod();
        }

        step("User is not able to see the Bliblipay payment method is available only on apps");
        Assertion.assertThat("Verify Bliblipay is not present in the page.", savedPaymentMethodWeb.isPresentBlibliPay(), is(false));
    }


    @Author(names = {"Vidhi Jindal"})
    @Module(name = "PaymentMethod")
    @Tribe(name = "Payment")
    @Vertical(name = "Platform")
    @Test
    public void verifyBlibliTiketPayLater() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = BaseTest.sessionThreadLocal.get();
        Map<String, String> map = getParam("verifyBlibliTiketPayLater");
        BaseTest.paramsThreadLocal.set(map);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        SavedPaymentMethodWeb<?> savedPaymentMethodWeb = SavedPaymentMethodWeb.getInstance(session);

        String email = map.get("email");
        String password = map.get("password");
        String otp = map.get("otp");

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, 0);

        step("Select english language if not in english");
        homePage.selectEnglishLanguageOnly();

        step("User open Payment Methods page");
        if(Configs.PLATFORM.equals(Platform.DWEB)) {
            homePage.goToSavedPaymentMethod();
        } else {
            homePage.goToAccountPage();
            homePage.goToSavedPaymentMethod();
        }

        step("User is able to see the Blibli Tiket Pay Later payment method is available only on apps");
        Assertion.assertThat("Verify Blibli Tiket Pay Later is not present in the page.", savedPaymentMethodWeb.isPresentBlibliTiketPayLater(), is(false));
    }

    @Author(names = { "Hiten", "Yoan","Shubham Bansal"})
    @Module(name = "PaymentMethod")
    @Tribe(name = "Payment")
    @Vertical(name = "Platform")
    @Test(dataProvider = "verifyPaymentMethodKlikBCAWebDataProvider")
    public void verifyPaymentMethodKlikBCAWeb(Map<String, String> map) throws InterruptedException , FailedLoginException {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PaymentPageWeb<?> landingPage = PaymentPageWeb.getInstance(session);
        CreateCarRentalOrderPage<?> carRentalOrderPage = new CreateCarRentalOrderPage<>(session);
        PaymentApiHitterPage<?> paymentHitterApiPage = new PaymentApiHitterPage<>(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);


        String email = map.get("email");
        String phoneNumber = map.get("phoneNumber");
        String passwordDweb = map.get("passwordDweb");
        String driverOption = map.get("driverOption");
        String location = map.get("location");
        String startDate = generateRandomDate(map.get("rangeAddDate"));
        String type = map.get("type");
        String pickupTestData = map.get("pickupTime");
        StringBuilder pickup = new StringBuilder(pickupTestData);
        pickup.setCharAt(2, ':');
        String pickupTime = pickup.toString();
        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String fullName = randomName() + " Autorder";
        String nameTitle = getRandomObject(salutationList);
        int rentalDuration = Integer.parseInt(map.get("rentalDuration"));
        String config = map.get("config");
        String database = map.get("database");
        String collectionName = map.get("collectionName");
        String paymentGroup = map.get("paymentGroup");
        String paymentMethod = map.get("paymentMethod");

        step("Create Car Rental order via API and fetch the orderID");
        Map<String,String> orderIdAndHash = carRentalOrderPage.createCarRentalProductTillPaymentApi(email, phoneNumber,passwordDweb ,driverOption, location, startDate, pickupTime, type, fullName, nameTitle, rentalDuration);
        String orderIdCreated = orderIdAndHash.get("orderId");
        String orderHashCreated = orderIdAndHash.get("orderHash");

        step("Login as " + email + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(email, passwordDweb);

        step("Select english language if not in english");
        homePage.selectEnglishLanguageOnly();

        step("Go to payment page");
        landingPage.goToPaymentPage(orderIdCreated, orderHashCreated);

        step("Fetch the order id and the price on the landing page");

        step("Verify payment page is loaded");
        Assertion.assertThat("Verify payment page is loaded or not", landingPage.isPaymentPageLoaded(), is(true));

        step("Access payment group");
        landingPage.clickOnSelectPaymentMethod();

        step("Scroll down to access selected payment group");
        landingPage.goToPaymentMethodsGroup(paymentGroup);

        step("Select payment group");
        landingPage.clickOnPaymentGroup(paymentGroup);

        step("Select Payment Method");
        landingPage.selectPaymentMethod(paymentMethod);
        Assertion.assertThat("Verify the selected payment", landingPage.getSelectedPayment(), is(paymentMethod));

        step("Open price history");
        landingPage.openPriceSummary();

        step("Wait for the price to stabilize after payment method is applied");
        landingPage.getFinalPrice();

        step("Verify convenience fee if available");
        landingPage.logConvenienceFeeIfPresent();

        step("Close the price history");
        landingPage.closeDrawer();

        step("Click on Pay button");
        landingPage.clickOnPaymentButton();

        step("Verify confirmation page");
        Assertion.assertThat("Verify the confirmation page is loaded", landingPage.isPaymentConfirmationPageLoaded(), is(true));

        step("Hit the Payment API to complete the payment");
        int afterPrice = (int) Double.parseDouble(landingPage.getFinalPrice());
        paymentHitterApiPage.hitKlikBCAApi(orderIdCreated, String.valueOf(afterPrice));

        step("Verify the payment successful page is visible or not");
        Assertion.assertThat("Verify the payment successful page is visible", landingPage.isPaymentSuccessfulVisible(), is(true));

        step("Verify the status from database");
        String query = landingPage.queryString(orderIdCreated);
        Map<String,String> paymentTransactionDetails = landingPage.getPaymentTransactionDetails(config, database, collectionName, query);
        String paymentStatus = paymentTransactionDetails.get("paymentStatus");
        String paymentAmount = paymentTransactionDetails.get("paymentAmount");
        Assertion.assertThat("Verify the payment status is paid",paymentStatus , is("PAID"));
        Assertion.assertThat("Verify the paid amount", Double.parseDouble(paymentAmount), is(Double.valueOf(afterPrice)));

    }

    @DataProvider(name = "verifyPaymentMethodKlikBCAWebDataProvider")
    public Object[][] verifyPaymentMethodKlikBCAWebDataProvider() {return getObjectParams("verifyPaymentMethodKlikBCAWeb");}

    @Author(names = { "Yoan" })
    @Module(name = "PaymentMethod")
    @Tribe(name = "Payment")
    @Vertical(name = "Platform")
    @Test(dataProvider = "verifySeeOrderListNonLoginWebDataProvider")
    public void verifySeeOrderListNonLoginWeb(Map<String, String> map) throws InterruptedException , FailedLoginException, ExecutionException {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);
        orderCreationPage<?> orderCreationPage = new orderCreationPage<>(session);
        PaymentPageHelperWeb<?> paymentHelperPage =PaymentPageHelperWeb.getInstance(session);
        String username = map.get("username");
        String password = map.get("password");
        String currency = map.get("currency");
        String query = map.get("hotelName");
        String startDate = generateRandomDate(map.get("rangeAddDate"));
        int adult = Integer.parseInt(map.get("adult"));
        int night = Integer.parseInt(map.get("night"));
        int room = Integer.parseInt(map.get("room"));
        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = getRandomObject(salutationList);
        String language = map.get("language");
        String paymentGroup = map.get("paymentGroup");
        String paymentMethod = map.get("paymentMethod");
        int serviceCharge = Integer.parseInt(map.get("serviceCharge"));

        step("Create hotel order in IDR currency");
        Map<String,String> orderIdAndHash = orderCreationPage.createWaitingPaymentOrderApi(username, password, startDate, query, adult, night, room, orderFullName, orderNameTitle, guestFullName, guestTitle, currency,language);
        String orderIdCreated = orderIdAndHash.get("orderId");
        String orderHashCreated = orderIdAndHash.get("orderHash");

        step("Logout from dweb if already login");
        homePage.goToHomePage();
        if(loginPage.checkIfLoggedIn()){
            loginPage.logoutFromDweb();
        }

        step("Select english language if not in english");
        homePage.selectEnglishLanguageOnly();

        step("Go to the payment page and select payment method");
        paymentHelperPage.goToPaymentPageAndSelectPaymentMethod(orderIdCreated, orderHashCreated, paymentGroup, paymentMethod);

        step("Click Pay Now button");
        paymentPage.clickOnPaymentButton();

        step("Verify confirmation page is loaded");
        Assertion.assertThat("Verify the confimation page is loaded", paymentPage.isPaymentConfirmationPageLoaded(), is(true));

        step("Click on See Order List button");
        paymentPage.clickSeeOrderListButton();

        step("Verify login page after clicking see order list button");
        Assertion.assertThat("Verify the login page is opened", loginPage.isLoginPageOpened(), is(true));

    }

    @DataProvider(name = "verifySeeOrderListNonLoginWebDataProvider")
    public Object[][] verifySeeOrderListNonLoginWebDataProvider() {return getObjectParams("verifySeeOrderListNonLoginWeb");}

    @Author(names = {"Akshat Johri"})
    @Module(name = "PaymentMethod")
    @Tribe(name = "Refund")
    @Vertical(name = "Platform")
    @Test
    public void testVerifyAddingBankAccountForRefund() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = BaseTest.sessionThreadLocal.get();
        Map<String, String> map = getParam("testVerifyAddingBankAccountForRefund");
        BaseTest.paramsThreadLocal.set(map);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        SavedPaymentMethodWeb<?> savedPaymentMethodWeb = SavedPaymentMethodWeb.getInstance(session);

        String email = map.get("email");
        String password = map.get("password");
        String otp = map.get("otp");
        String bankName = map.get("bankName");
        String accountNumber = map.get("accountNumber");
        String bankOtp = map.get("bankOtp");


        step("User open home page");
        homePage.goToHomePage();

        step("Login as " + email + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(email, password);

        step("Select english language if not in english");
        homePage.selectEnglishLanguageOnly();

        step("User open Saved Payment Methods page");
        if(Configs.PLATFORM.equals(Platform.DWEB)) {
            homePage.goToSavedPaymentMethod();
        } else {
            homePage.goToAccountPage();
            homePage.goToSavedPaymentMethod();
        }

        step("User clicks on add bank account");
        savedPaymentMethodWeb.addBankAccount();

        step("User enter the details of bank account");
        savedPaymentMethodWeb.fillBankAccountDetails(bankName,bankOtp,accountNumber);

        step("User saves the bank account");
        savedPaymentMethodWeb.saveAccount();

        step("User verifies whether the bank account is added or not");
        Assertion.assertThat("verifies whether bank account is added or not", savedPaymentMethodWeb.isBankAccountAdded(accountNumber), is(true));
    }

    @Author(names = {"Akshat Johri"})
    @Module(name = "PaymentMethod")
    @Tribe(name = "Payment")
    @Vertical(name = "Platform")
    @Test
    public void testVerifyAddingDebitCard() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = BaseTest.sessionThreadLocal.get();
        Map<String, String> map = getParam("testVerifyAddingDebitCard");
        BaseTest.paramsThreadLocal.set(map);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        SavedPaymentMethodWeb<?> savedPaymentMethodWeb = SavedPaymentMethodWeb.getInstance(session);

        String email = map.get("email");
        String password = map.get("password");
        String otp = map.get("otp");
        String cardNumber = map.get("cardNumber");
        String cardMonth = map.get("cardMonth");
        String cvv = map.get("cvv");
        String cardHolderName = map.get("cardHolderName");

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, 0);

        step("Select english language if not in english");
        homePage.selectEnglishLanguageOnly();

        step("User open Saved Payment Methods page");
        if(Configs.PLATFORM.equals(Platform.DWEB)) {
            homePage.goToSavedPaymentMethod();
        } else {
            homePage.goToAccountPage();
            homePage.goToSavedPaymentMethod();
        }

        step("User clicks on add debit card");
        savedPaymentMethodWeb.addCard();

        step("User enters the debit card details");
        savedPaymentMethodWeb.addCardDetails(cardNumber,cardMonth,cvv,cardHolderName);

        step("User saves the account");
        savedPaymentMethodWeb.saveAccount();

        step("User verifies 3DS");
        savedPaymentMethodWeb.verify3DS();

        step("User verifies whether the card is added or not");
        Assertion.assertThat("verifies whether the card is added or not", savedPaymentMethodWeb.isCardAdded(), is(true));
    }

    @Author(names = {"Akshat Johri"})
    @Module(name = "PaymentMethod")
    @Tribe(name = "Payment")
    @Vertical(name = "Platform")
    @Test
    public void testVerifyAddingCreditCard() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = BaseTest.sessionThreadLocal.get();
        Map<String, String> map = getParam("testVerifyAddingCreditCard");
        BaseTest.paramsThreadLocal.set(map);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        SavedPaymentMethodWeb<?> savedPaymentMethodWeb = SavedPaymentMethodWeb.getInstance(session);

        String email = map.get("email");
        String password = map.get("password");
        String otp = map.get("otp");
        String cardNumber = map.get("cardNumber");
        String cardMonth = map.get("cardMonth");
        String cvv = map.get("cvv");
        String cardHolderName = map.get("cardHolderName");

        step("User open home page");
        homePage.goToHomePage();

        step("Login as " + email + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(email, password);

        step("Select english language if not in english");
        homePage.selectEnglishLanguageOnly();

        step("User open Saved Payment Methods page");
        if(Configs.PLATFORM.equals(Platform.DWEB)) {
            homePage.goToSavedPaymentMethod();
        } else {
            homePage.goToAccountPage();
            homePage.goToSavedPaymentMethod();
        }

        step("User clicks on add debit card");
        savedPaymentMethodWeb.addCard();

        step("User enters the debit card details");
        savedPaymentMethodWeb.addCardDetails(cardNumber,cardMonth,cvv,cardHolderName);

        step("User saves the account");
        savedPaymentMethodWeb.saveAccount();

        step("User verifies 3DS");
        savedPaymentMethodWeb.verify3DS();

        step("User verifies whether the card is added or not");
        Assertion.assertThat("verifies whether the card is added or not", savedPaymentMethodWeb.isCardAdded(), is(true));
    }

    @Author(names = { "Akshat Johri" })
    @Module(name = "PaymentMethod")
    @Tribe(name = "Payment")
    @Vertical(name = "Platform")
    @Test(dataProvider = "testVerifyChangePaymentWebDataProvider")
    public void testVerifyChangePaymentWeb(Map<String, String> map) throws InterruptedException , FailedLoginException {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);
        PaymentPageHelperWeb<?> paymentHelperPage = PaymentPageHelperWeb.getInstance(session);
        CreateCarRentalOrderPage<?> carRentalOrderPage = new CreateCarRentalOrderPage<>(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);

        String email = map.get("email");
        String phoneNumber = map.get("phoneNumber");
        String passwordDweb = map.get("passwordDweb");
        String driverOption = map.get("driverOption");
        String location = map.get("location");
        String startDate = generateRandomDate(map.get("rangeAddDate"));
        String type = map.get("type");
        String pickupTestData = map.get("pickupTime");
        StringBuilder pickup = new StringBuilder(pickupTestData);
        pickup.setCharAt(2, ':');
        String pickupTime = pickup.toString();
        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String fullName = randomName() + " Autorder";
        String nameTitle = getRandomObject(salutationList);
        int rentalDuration = Integer.parseInt(map.get("rentalDuration"));
        String paymentGroup = map.get("paymentGroup");
        int serviceCharge = Integer.parseInt(map.get("serviceCharge"));
        String otp = map.get("otp");
        String cardNumber = map.get("cardNumber");
        String cardMonth = map.get("cardMonth");
        String cvv = map.get("cvv");
        String cardHolderName = map.get("cardHolderName");
        String TwoCTwoP = map.get("TwoCTwoP");
        String xendit = map.get("xendit");
        String ThreeDS = map.get("ThreeDS");
        String cardOtp = map.get("cardOtp");

        step("Create Car Rental order via API and fetch the orderID");
        Map<String,String> orderIdAndHash = carRentalOrderPage.createCarRentalProductTillPaymentApi(email, phoneNumber,passwordDweb ,driverOption, location, startDate, pickupTime, type, fullName, nameTitle, rentalDuration);
        String orderIdCreated = orderIdAndHash.get("orderId");
        String orderHashCreated = orderIdAndHash.get("orderHash");

        step("Login as " + email + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(email, passwordDweb);

        step("Select english language if not in english");
        homePage.selectEnglishLanguageOnly();

        step("Go to the payment page");
        paymentPage.goToPaymentPage(orderIdCreated, orderHashCreated);

        step("Verify payment page is loaded or not");
        Assertion.assertThat("Verify the payment page is loaded or not", paymentPage.isPaymentPageLoaded(), is(true));

        step("Fetch the order id and the price on the landing page");
        String orderId = paymentPage.getOrderId();
        int beforePrice = (int) Double.parseDouble(paymentPage.getFinalPrice());

        step("Click on select payment method");
        paymentPage.clickOnSelectPaymentMethod();

        step("Scroll down to the payment Group");
        paymentPage.goToPaymentMethodsGroup(paymentGroup);

        step("Select Payment Method");
        paymentPage.selectIndoMaret();

        step("Click on Pay button");
        paymentPage.clickOnPaymentButton();

        step("Verify change payment button is visible or not");
        Assertion.assertThat("Verify change payment button is visible or not", paymentPage.isChangePaymentButtonVisible(), is(true));

        step("Click on change payment method button");
        paymentPage.clickChangePaymentMethodButton();

        step("Click on confirm the change payment button");
        paymentPage.setConfirmChangePaymentMethod();

        step("Verify payment page is loaded or not");
        Assertion.assertThat("Verify the payment page is loaded or not", paymentPage.isPaymentPageVisible(), is(true));
    }
    @DataProvider(name = "testVerifyChangePaymentWebDataProvider")
    public Object[][] testVerifyChangePaymentWebDataProvider() {return getObjectParams("testVerifyChangePaymentWeb");}

    @Author(names = { "Akshat Johri", "Yoan", "Shubham Bansal"})
    @Module(name = "PaymentMethod")
    @Tribe(name = "Payment")
    @Vertical(name = "Platform")
    @Test(dataProvider = "verifyOrderListAfterPaymentDataProvider")
    public void verifyOrderListAfterPayment(Map<String, String> map) throws InterruptedException, FailedLoginException, ExecutionException {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);
        PaymentPageHelperWeb<?> paymentHelperPage = PaymentPageHelperWeb.getInstance(session);
        CreateCarRentalOrderPage<?> carRentalOrderPage = new CreateCarRentalOrderPage<>(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);

        String email = map.get("email");
        String phoneNumber = map.get("phoneNumber");
        String passwordDweb = map.get("passwordDweb");
        String driverOption = map.get("driverOption");
        String location = map.get("location");
        String startDate = generateRandomDate(map.get("rangeAddDate"));
        String type = map.get("type");
        String pickupTestData = map.get("pickupTime");
        StringBuilder pickup = new StringBuilder(pickupTestData);
        pickup.setCharAt(2, ':');
        String pickupTime = pickup.toString();
        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String fullName = randomName() + " Autorder";
        String nameTitle = getRandomObject(salutationList);
        int rentalDuration = Integer.parseInt(map.get("rentalDuration"));
        String paymentGroup = map.get("paymentGroup");
        int serviceCharge = Integer.parseInt(map.get("serviceCharge"));
        String otp = map.get("otp");
        String paymentMethod = map.get("paymentMethod");
        String cardNumber = map.get("cardNumber");
        String expiry = map.get("cardMonth");
        String cvv = map.get("cvv");
        String cardName = map.get("cardHolderName");

        step("Create Car Rental order via API and fetch the orderID");
        Map<String,String> orderIdAndHash = carRentalOrderPage.createCarRentalProductTillPaymentApi(email, phoneNumber,passwordDweb ,driverOption, location, startDate, pickupTime, type, fullName, nameTitle, rentalDuration);
        String orderIdCreated = orderIdAndHash.get("orderId");
        String orderHashCreated = orderIdAndHash.get("orderHash");

        step("Login into dweb if not login using the given account");
        loginPage.loginIfNotWithSameEmail(email,passwordDweb,otp,0);

        step("Select english language if not in english");
        homePage.selectEnglishLanguageOnly();

        step("Go to the payment page");
        paymentPage.goToPaymentPage(orderIdCreated, orderHashCreated);

        step("Verify payment page is loaded or not");
        Assertion.assertThat("Verify the payment page is loaded or not", paymentPage.isPaymentPageLoaded(), is(true));

        step("Fetch the order id and the price on the landing page");
        String orderId = paymentPage.getOrderId();
        int beforePrice = (int) Double.parseDouble(paymentPage.getFinalPrice());

        step("Verify payment page is loaded");
        Assertion.assertThat("Verify payment page is loaded or not", paymentPage.isPaymentPageLoaded(), is(true));

        step("Access payment group");
        paymentPage.clickOnSelectPaymentMethod();

        step("Scroll down to access selected payment group");
        paymentPage.goToPaymentMethodsGroup(paymentGroup);

        step("Select payment group");
        paymentPage.clickOnPaymentGroup(paymentGroup);

        step("Select Payment Method");
        paymentPage.selectPaymentMethod(paymentMethod);

        boolean isCreditCardFlow = paymentPage.getSelectedPayment() != null
                && paymentPage.getSelectedPayment().toLowerCase().contains("credit");

        if (isCreditCardFlow) {
            step("Input the card details");
            paymentHelperPage.inputCardDetails(cardNumber, expiry, cvv, cardName);
        }

        step("Click on Pay button");
        paymentPage.clickOnPaymentButton();

        if (isCreditCardFlow) {
            step("Handle 3DS page if shown");
            paymentPage.handle3DSPage(10);
        }

        step("Verify the payment successful page is visible or not");
        Assertion.assertThat("Verify the payment successful page is visible", paymentPage.isPaymentSuccessfulVisible(), is(true));

        step("User open order list page after success");
        paymentPage.clickSeeOrderListButtonAfterSuccess();

        step("Verify order list page is opened");
        Assertion.assertThat("Verify order list page is opened", orderListPage.isOrderListPageDisplayed(), is(true));
    }
    @DataProvider(name = "verifyOrderListAfterPaymentDataProvider")
    public Object[][] verifyOrderListAfterPaymentDataProvider() {return getObjectParams("verifyOrderListAfterPayment");}

    @Author(names = {"Yoan"})
    @Module(name = "PaymentMethod")
    @Tribe(name = "Payment")
    @Vertical(name = "Platform")
    @Test(dataProvider = "verifyCheckoutWithPermataVAWebDataProvider")
    public void verifyCheckoutWithPermataVAWeb(Map<String, String> map) throws InterruptedException , FailedLoginException, ExecutionException {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PaymentPageWeb<?> landingPage = PaymentPageWeb.getInstance(session);
        PaymentPageHelperWeb<?> paymentHelperPage = PaymentPageHelperWeb.getInstance(session);
        CreateCarRentalOrderPage<?> carRentalOrderPage = new CreateCarRentalOrderPage<>(session);

        String email = map.get("email");
        String phoneNumber = map.get("phoneNumber");
        String passwordDweb = map.get("passwordDweb");
        String driverOption = map.get("driverOption");
        String location = map.get("location");
        String startDate = generateRandomDate(map.get("rangeAddDate"));
        String type = map.get("type");
        String pickupTestData = map.get("pickupTime");
        StringBuilder pickup = new StringBuilder(pickupTestData);
        pickup.setCharAt(2, ':');
        String pickupTime = pickup.toString();
        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String fullName = randomName() + " Autorder";
        String nameTitle = getRandomObject(salutationList);
        int rentalDuration = Integer.parseInt(map.get("rentalDuration"));
        String paymentGroup = map.get("paymentGroup");
        String paymentMethod = map.get("paymentMethod");
        int serviceCharge = Integer.parseInt(map.get("serviceCharge"));
        String otp = map.get("otp");

        step("Create Car Rental order via API and fetch the orderID");
        Map<String,String> orderIdAndHash = carRentalOrderPage.createCarRentalProductTillPaymentApi(email, phoneNumber,passwordDweb ,driverOption, location, startDate, pickupTime, type, fullName, nameTitle, rentalDuration);
        String orderIdCreated = orderIdAndHash.get("orderId");
        String orderHashCreated = orderIdAndHash.get("orderHash");

        step("Login and select payment method");
        int finalPrice = paymentHelperPage.loginViaCookieAndSelectPaymentMethod(email, passwordDweb, orderIdCreated, orderHashCreated, paymentGroup, paymentMethod, serviceCharge);

        step("Open price history");
        landingPage.openPriceSummary();
        Assertion.assertThat("Is transaction fees is available", landingPage.isTransactionFeesDisplayed(), is(false));

        step("Close the price history");
        landingPage.closeDrawer();

        step("Click on Pay button");
        landingPage.clickOnPaymentButton();

        step("Verify confirmation page");
        Assertion.assertThat("Verify the confirmation page is loaded", landingPage.isPaymentConfirmationPageLoaded(), is(true));

        step("Verify payment is Permata VA on payment confirmation page");
        Assertion.assertThat("Verify payment is Permata VA", landingPage.isPaymentPermataVAonPaymentConfirmationPage(), is(true));

        step("Verify VA logo is visible on confirmation page");
        Assertion.assertThat("Verify VA logo is visible on confirmation page", landingPage.isPaymentLogoOnPaymentConfirmationPageVisible(), is(true));

        step("Verify VA number is visible on confirmation page");
        Assertion.assertThat("Verify VA number is visible on confirmation page", landingPage.isVANumberDisplayed(), is(true));

        step("Verify How To Pay section is visible on confirmation page");
        Assertion.assertThat("Verify How To Pay section is visible", landingPage.isHowToPaySectionDisplayed(), is(true));

    }
    @DataProvider(name = "verifyCheckoutWithPermataVAWebDataProvider")
    public Object[][] verifyCheckoutWithPermataVAWebDataProvider() {return getObjectParams("verifyCheckoutWithPermataVAWeb");}

    @Author(names = {"Yoan"})
    @Module(name = "PaymentMethod")
    @Tribe(name = "Payment")
    @Vertical(name = "Platform")
    @Test(dataProvider = "verifyCheckoutWithBSIVAWebDataProvider")
    public void verifyCheckoutWithBSIVAWeb(Map<String, String> map) throws InterruptedException , FailedLoginException, ExecutionException {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PaymentPageWeb<?> landingPage = PaymentPageWeb.getInstance(session);
        PaymentPageHelperWeb<?> paymentHelperPage = PaymentPageHelperWeb.getInstance(session);
        CreateCarRentalOrderPage<?> carRentalOrderPage = new CreateCarRentalOrderPage<>(session);

        String email = map.get("email");
        String phoneNumber = map.get("phoneNumber");
        String passwordDweb = map.get("passwordDweb");
        String driverOption = map.get("driverOption");
        String location = map.get("location");
        String startDate = generateRandomDate(map.get("rangeAddDate"));
        String type = map.get("type");
        String pickupTestData = map.get("pickupTime");
        StringBuilder pickup = new StringBuilder(pickupTestData);
        pickup.setCharAt(2, ':');
        String pickupTime = pickup.toString();
        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String fullName = randomName() + " Autorder";
        String nameTitle = getRandomObject(salutationList);
        int rentalDuration = Integer.parseInt(map.get("rentalDuration"));
        String paymentGroup = map.get("paymentGroup");
        String paymentMethod = map.get("paymentMethod");
        int serviceCharge = Integer.parseInt(map.get("serviceCharge"));
        String otp = map.get("otp");

        step("Create Car Rental order via API and fetch the orderID");
        Map<String,String> orderIdAndHash = carRentalOrderPage.createCarRentalProductTillPaymentApi(email, phoneNumber,passwordDweb ,driverOption, location, startDate, pickupTime, type, fullName, nameTitle, rentalDuration);
        String orderIdCreated = orderIdAndHash.get("orderId");
        String orderHashCreated = orderIdAndHash.get("orderHash");

        step("Login and select payment method");
        int finalPrice = paymentHelperPage.loginViaCookieAndSelectPaymentMethod(email, passwordDweb, orderIdCreated, orderHashCreated, paymentGroup, paymentMethod, serviceCharge);

        step("Open price history");
        landingPage.openPriceSummary();
        Assertion.assertThat("Is transaction fees is available", landingPage.isTransactionFeesDisplayed(), is(false));

        step("Close the price history");
        landingPage.closeDrawer();

        step("Click on Pay button");
        landingPage.clickOnPaymentButton();

        step("Verify confirmation page");
        Assertion.assertThat("Verify the confirmation page is loaded", landingPage.isPaymentConfirmationPageLoaded(), is(true));

        step("Verify payment is BSI VA on payment confirmation page");
        Assertion.assertThat("Verify payment is BSI VA", landingPage.isPaymentBSIVAonPaymentConfirmationPage(), is(true));

        step("Verify VA logo is visible on confirmation page");
        Assertion.assertThat("Verify VA logo is visible on confirmation page", landingPage.isPaymentLogoOnPaymentConfirmationPageVisible(), is(true));

        step("Verify VA number is visible on confirmation page");
        Assertion.assertThat("Verify VA number is visible on confirmation page", landingPage.isVANumberDisplayed(), is(true));

        step("Verify How To Pay section is visible on confirmation page");
        Assertion.assertThat("Verify How To Pay section is visible", landingPage.isHowToPaySectionDisplayed(), is(true));

    }
    @DataProvider(name = "verifyCheckoutWithBSIVAWebDataProvider")
    public Object[][] verifyCheckoutWithBSIVAWebDataProvider() {return getObjectParams("verifyCheckoutWithBSIVAWeb");}

    @Author(names = { "Akshat Johri" })
    @Module(name = "PaymentMethod")
    @Tribe(name = "Payment")
    @Vertical(name = "Platform")
    @Test(dataProvider = "verifyPaymentMethodUsingCardWithInstallmentWebDataProvider")
    public void verifyPaymentMethodUsingCardWithInstallmentWeb(Map<String, String> map) throws InterruptedException , FailedLoginException {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);
        PaymentPageHelperWeb<?> paymentHelperPage = PaymentPageHelperWeb.getInstance(session);
        CreateCarRentalOrderPage<?> carRentalOrderPage = new CreateCarRentalOrderPage<>(session);

        String email = map.get("email");
        String phoneNumber = map.get("phoneNumber");
        String passwordDweb = map.get("passwordDweb");
        String driverOption = map.get("driverOption");
        String location = map.get("location");
        String startDate = generateRandomDate(map.get("rangeAddDate"));
        String type = map.get("type");
        String pickupTestData = map.get("pickupTime");
        StringBuilder pickup = new StringBuilder(pickupTestData);
        pickup.setCharAt(2, ':');
        String pickupTime = pickup.toString();
        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String fullName = randomName() + " Autorder";
        String nameTitle = getRandomObject(salutationList);
        int rentalDuration = Integer.parseInt(map.get("rentalDuration"));
        String paymentGroup = map.get("paymentGroup");
        int serviceCharge = Integer.parseInt(map.get("serviceCharge"));
        String otp = map.get("otp");
        String cardNumber = map.get("cardNumber");
        String cardMonth = map.get("cardMonth");
        String cvv = map.get("cvv");
        String cardHolderName = map.get("cardHolderName");

        step("Create Car Rental order via API and fetch the orderID");
        Map<String,String> orderIdAndHash = carRentalOrderPage.createCarRentalProductTillPaymentApi(email, phoneNumber,passwordDweb ,driverOption, location, startDate, pickupTime, type, fullName, nameTitle, rentalDuration);
        String orderIdCreated = orderIdAndHash.get("orderId");
        String orderHashCreated = orderIdAndHash.get("orderHash");

        step("Login into dweb if not login using the given account");
        loginPage.loginIfNotWithSameEmail(email,passwordDweb,otp,0);

        step("Select english language if not in english");
        homePage.selectEnglishLanguageOnly();

        step("Go to the payment page");
        paymentPage.goToPaymentPage(orderIdCreated, orderHashCreated);

        step("Verify payment page is loaded or not");
        Assertion.assertThat("Verify the payment page is loaded or not", paymentPage.isPaymentPageLoaded(), is(true));

        step("Fetch the order id and the price on the landing page");
        String orderId = paymentPage.getOrderId();
        int beforePrice = (int) Double.parseDouble(paymentPage.getFinalPrice());

        step("Click on select payment method");
        paymentPage.clickOnSelectPaymentMethod();

        step("Scroll down to the payment Group");
        paymentPage.goToPaymentMethodsGroup(paymentGroup);

        step("Select Payment Method");
        paymentPage.selectCreditCard();
        Assertion.assertThat("Verify the selected payment", paymentPage.getSelectedPayment(), is("Credit Card"));

        step("Fetch the price after payment method is applied");
        int finalPrice = (int) Double.parseDouble(paymentPage.getFinalPrice());

        step("Verify the service charge after payment is applied");
        Assertion.assertThat("Verify the service charge", (finalPrice-beforePrice), is(serviceCharge));

        step("Input the card details");
        paymentHelperPage.inputCardDetails(cardNumber, cardMonth, cvv, cardHolderName);

        step("User clicks on installment plans");
        paymentPage.selectInstallment();

        step("Click on Pay button");
        paymentPage.clickOnPaymentButton();

        step("Handle 3DS page if shown");
        paymentPage.handle3DSPage(10);

        step("Verify the payment successful page is visible or not");
        Assertion.assertThat("Verify the payment successful page is visible", paymentPage.isPaymentSuccessfulVisible(), is(true));
    }

    @DataProvider(name = "verifyPaymentMethodUsingCardWithInstallmentWebDataProvider")
    public Object[][] verifyPaymentMethodUsingCardWithInstallmentWebDataProvider() {return getObjectParams("verifyPaymentMethodUsingCardWithInstallmentWeb");}

    @Author(names = "Manan Kumar")
    @Module(name = "Payment")
    @Tribe(name = "Sphinx")
    @Vertical(name = "Platform")
    @Test
    public void testEarningPointsVisibleOnLandingAndPaymentConfirmation() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testEarningPointsVisibleOnLandingAndPaymentConfirmation");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        TtdLandingPageWeb<?> ttdLandingPageWeb = TtdLandingPageWeb.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);
        TtdBookingFormPageWeb<?> ttdBookingFormPageWeb = TtdBookingFormPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);

        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String product = param.get("product");
        String packageName = param.get("packageName");
        int quantity = Integer.parseInt(param.get("quantity"));
        int ticketIndex = Integer.parseInt(param.get("ticketIndex"));

        step("User Go to Home Page");
        homePage.goToHomePage();

        step("Open as logged in user");
        loginPage.loginIfNotWithSameEmail(username, password, otp, retryCounter);

        step("Go to TTD Landing Page");
        ttdLandingPageWeb.goToTtdLandingPage();

        step("User Search for the Product that will give earning points");
        ttdLandingPageWeb.inputSearchKeyword(product);
        ttdLandingPageWeb.selectRecommendedActivity(product);

        step("User Book the Package for e-ticket and e-voucher");
        ttdProductDetailPageWeb.findPackage(packageName);

        step("User choose Tickets for the package");
        ttdProductDetailPageWeb.chooseTicket(packageName);
        ttdProductDetailPageWeb.setTicketQty(quantity, ticketIndex);

        step("User Clicks on book button");
        ttdProductDetailPageWeb.clickBookButton();

        step("User Verifies that earning points visible on Booking Page");
        Assertion.assertThat("is Points Visible", paymentPage.isEarningPointsVisibleOnBookingPage(), is(true));

        step("User Stores the earning points on booking page");
        String earningPointsOnBookingPage = paymentPage.getEarningPointsFromBookingPage();

        step("User Navigate to the Payment Page");
        ttdBookingFormPageWeb.continueToPaymentPage();

        step("User Verifies that earning points visible on payment page");
        Assertion.assertThat("is Points Visible", paymentPage.isEarningPointsVisibleOnPaymentPage(), is(true));
        String earningPointsOnPaymentPage = paymentPage.getEarningPointsFromPaymentPage();
        Assertion.assertThat("Earning points should match", paymentPage.verifyEarningPointsMatch(earningPointsOnBookingPage, earningPointsOnPaymentPage), is(true));
    }

    @Author(names = {"Rizki Maulana"})
    @Module(name = "PaymentMethod")
    @Tribe(name = "Payment")
    @Vertical(name = "Platform")
    @Test
    public void verifyPayRefundDepositPayment() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyPayRefundDepositPayment");
        BaseTest.paramsThreadLocal.set(param);
        HotelLandingPageWeb<?> hotelLandingPageWeb = HotelLandingPageWeb.getInstance(session);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        orderCreationPage<?> orderCreationPage = new orderCreationPage<>(session);
        ReschedulePage<?> reschedulePage = new ReschedulePage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));

        String hotelId = param.get("hotelId");
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));
        int room = Integer.parseInt(param.get("room"));
        String startDate = generateRandomDate(param.get("rangeAddDate"));
        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = getRandomObject(salutationList);

        step("Login as " + username + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(username, password);

        step("Create order");
        orderCreationPage.createHotelSakukuIssuedOrderApiWithHotelIdAndTheMostExpensive(
                username, password, startDate, hotelId, adult, night, room,
                orderFullName, orderNameTitle, guestFullName, guestTitle, "IDR"
        );
        String bookedOrderID = String.valueOf(orderCreationPage.getOrderId());

        step("Find the issued order");
        orderListPage.openActiveTicketViaURL(bookedOrderID);
        orderDetailPage.skipCoachmark();

        step("Check reschedule status");
        reschedulePage.checkRescheduleableStatus(username);

        step("Click check availability button");
        reschedulePage.clickCheckAvailabilityButton();
        hotelLandingPageWeb.closeContinueOnWebPopup();

        step("Click select room with same amount");
        reschedulePage.clickSelectRoomWithSameAmount();

        step("Click Book button");
        reschedulePage.clickBookButton();

        step("Click checkTnC button");
        reschedulePage.checkTnC();

        step("Click submit reschedule button");
        reschedulePage.clickSubmitRescheduleButton();
        hotelLandingPageWeb.closeContinueOnWebPopup();

        step("Click pay with Refund deposit button");
        reschedulePage.clickPayWithRefundDeposit();

        step("Click submit refund request");
        reschedulePage.clickSubmitRefundRequest();
        Assertion.assertThat("Refund request is successful", reschedulePage.verifyRescheduleSuccess(), is(true));
        reschedulePage.verifyHotelStatus();
        Assertion.assertThat("Order successfully issued", orderDetailPage.verifyIssuedETicket(), is(true));

        step("Verify price detail");
        reschedulePage.comparePrices();
    }

    @Author(names = "Manan Kumar")
    @Module(name = "Payment")
    @Tribe(name = "Payment")
    @Vertical(name = "Platform")
    @Test
    public void testVerifyDownPayOrderOnPaymentPageLogin() throws InterruptedException, FailedLoginException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testVerifyDownPayOrderOnPaymentPageLogin");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPage = FlightLandingPageWeb.getInstance(session);
        FlightSearchResultPageWeb<?> flightSearchResultPageWeb = FlightSearchResultPageWeb.getInstance(session);
        FlightProductReviewPageWeb<?> flightProductReviewPageWeb = FlightProductReviewPageWeb.getInstance(session);
        FlightBookingFormPageWeb<?> flightBookingFormPageWeb = FlightBookingFormPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPageWeb = PaymentPageWeb.getInstance(session);
        FlightApi<?> flightApi = new FlightApi<>(session);

        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));

        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);

        String username = param.get("username");
        String otp = param.get("otp");
        String password = param.get("password");
        String airlineName = param.get("airlineName");

        // Dummy params to find dynamic data
        int adult = 1;
        int child = 0;
        int infant = 0;
        String searchType = "oneway";

        step("Find dynamic data");
        FlightDynamicData flightDynamicData = flightApi.findDate(
                username,password,
                originAirport,destinationAirport,daysToDepartureDate,
                adult, child, infant, searchType, airlineName,"", "", "", ""
        );
        originAirport = flightDynamicData.originAirport;
        destinationAirport = flightDynamicData.destinationAirport;
        departureDate = CoreFunctions.convertToLongDate(flightDynamicData.departureDate);
        airlineName = flightDynamicData.airlineDepart;

        step("User open home page");
        homePage.goToHomePage();

        step("Logging in using {" + username + "}");
        loginPage.loginIfNotWithSameEmail(username, password, otp, 1);

        step("User open Flight Landing Page");
        homePage.openVerticalPageViaURL("Flights");

        step("Select origin airport");
        flightLandingPage.selectOrigin(originAirport);

        step("Select destination airport");
        flightLandingPage.selectDestination(destinationAirport);

        step("Select date");
        flightLandingPage.selectDate(departureDate);

        step("search flight");
        flightLandingPage.searchFlight();

        step("filter airline {" + airlineName + " }");
        flightSearchResultPageWeb.filterAirline(airlineName);

        step("select flight for departure");
        flightSearchResultPageWeb.selectFlight();

        step("User Verifies that flight have Down Pay Option ");
        Assertion.assertThat("Is down pay Available for this flight?", flightProductReviewPageWeb.isFlightHaveDownPay(), is(true));

        step("select the SELECT Button");
        flightProductReviewPageWeb.clickDownPaySelectBtn();

        step("User clicks No thanks button");
        flightProductReviewPageWeb.clickNoThanksBtn();

        step("User enter the passenger Name");
        flightBookingFormPageWeb.fillPassengerDetails();

        if(Configs.PLATFORM.equals(Platform.AMWEB)){
            step("User clicks on Save Button ");
            flightLandingPage.clickSaveButton();

            step("Click on continue button");
            flightLandingPage.clickContinueButton();
        }

        step("User unchecks the extra protection option");
        flightBookingFormPageWeb.selectChooseBtn();

        step("Validate all suppliers are mock issued by airline, skipped if real issued supplier exist");
        boolean isDepartSuppliersMocked = flightApi.areAllSuppliersMockIssuedByAirline(flightDynamicData.airlineCodeDepart, originAirport, destinationAirport);

        step("going to paymentPage");
        flightBookingFormPageWeb.goToPaymentPageFromBookingFormPage();
        flightBookingFormPageWeb.clickNoThanksOnFlightMayReschedulePopup();

        step("click I don't want it");
        flightBookingFormPageWeb.clickOkayGotIt();

        step("User closes baggage change popup ");
        flightBookingFormPageWeb.clickGotItInBaggagePopup();

        step("Payment page should be displayed");
        Assertion.assertThat("PaymentPageLoaded", paymentPageWeb.isPaymentPagepresent(), is(true));

        step("User selects Sakuku payment method");
        paymentPageWeb.selectSakukuPaymentMethod();

        step("User verifies that DownPay button visible and clickable.");
        Assertion.assertThat("Is button visible and clickable", paymentPageWeb.isDownPayBtnVisibleAndClickable(), is(true));

        step("only submit payment if all suppliers are mocked { isDepartSuppliersMocked: " + isDepartSuppliersMocked +" }");
        if(!isDepartSuppliersMocked) {
            throw new SkipException("Skipping the test since not all suppliers are mocked, cannot proceed to submit payment");
        }

        step("User clicks on DownPay button");
        paymentPageWeb.clickDownPayButton();

        step("User verifies Down pay modal is showing");
        Assertion.assertThat("Down pay modal is visible", paymentPageWeb.isDownPayModalVisible(), is(true));

        step("User verifies Down pay price detail showing current bill");
        Assertion.assertThat("Down pay current bill is visible", paymentPageWeb.isDownPayCurrentBillVisible(), is(true));

        step("User verifies Down pay due date is showing");
        Assertion.assertThat("Down pay due date is visible", paymentPageWeb.isDownPayDueDateVisible(), is(true));
    }

    @Author(names = {"Shubham Bansal"})
    @Module(name = "PaymentMethod")
    @Tribe(name = "Payment")
    @Vertical(name = "Platform")
    @Test
    public void testVerifyDeletingCreditCard() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = BaseTest.sessionThreadLocal.get();
        Map<String, String> map = getParam("testVerifyDeletingCreditCard");
        BaseTest.paramsThreadLocal.set(map);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        SavedPaymentMethodWeb<?> savedPaymentMethodWeb = SavedPaymentMethodWeb.getInstance(session);

        String email = map.get("email");
        String password = map.get("password");
        String otp = map.get("otp");
        String cardNumber = map.get("cardNumber");
        String cardMonth = map.get("cardMonth");
        String cvv = map.get("cvv");
        String cardHolderName = map.get("cardHolderName");

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, 0);

        step("Select english language if not in english");
        homePage.selectEnglishLanguageOnly();

        step("User open Saved Payment Methods page");
        if(Configs.PLATFORM.equals(Platform.DWEB)) {
            homePage.goToSavedPaymentMethod();
        } else {
            homePage.goToAccountPage();
            homePage.goToSavedPaymentMethod();
        }

        String lastFourDigits = cardNumber.substring(cardNumber.length() - 4);

        step("Verify if existing card is not present");
        if (!savedPaymentMethodWeb.isCardPresent(lastFourDigits)) {

            step("Click on add card since no existing card is present");
            savedPaymentMethodWeb.addCard();

            step("Enter the credit card details");
            savedPaymentMethodWeb.addCardDetails(cardNumber, cardMonth, cvv, cardHolderName);

            step("Save the account");
            savedPaymentMethodWeb.saveAccount();

            if (Configs.PLATFORM.equals(Platform.AMWEB)) {
                step("Handle Save card popup if appears (amWeb)");
                savedPaymentMethodWeb.handleSaveCardPopupNativeIfAny();
            }

            step("User verifies 3DS and wait for redirect back to Saved Payment Methods page");
            savedPaymentMethodWeb.verify3DS();
        }

        step("User deletes/remove the card");
        savedPaymentMethodWeb.removeCard(lastFourDigits);

        step("User verifies whether the card is removed");
        Assertion.assertThat("Card should not be present", savedPaymentMethodWeb.isCardPresent(lastFourDigits), is(false));
    }

    @Author(names = {"Yoan"})
    @Module(name = "PaymentMethod")
    @Tribe(name = "Payment")
    @Vertical(name = "Platform")
    @Test(dataProvider = "verifyCheckoutWithDANAWebDataProvider")
    public void verifyCheckoutWithDANAWeb(Map<String, String> map) throws InterruptedException , FailedLoginException, ExecutionException {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PaymentPageWeb<?> landingPage = PaymentPageWeb.getInstance(session);
        PaymentPageHelperWeb<?> paymentHelperPage = PaymentPageHelperWeb.getInstance(session);
        CreateCarRentalOrderPage<?> carRentalOrderPage = new CreateCarRentalOrderPage<>(session);

        String email = map.get("email");
        String phoneNumber = map.get("phoneNumber");
        String passwordDweb = map.get("passwordDweb");
        String driverOption = map.get("driverOption");
        String location = map.get("location");
        String startDate = generateRandomDate(map.get("rangeAddDate"));
        String type = map.get("type");
        String pickupTestData = map.get("pickupTime");
        StringBuilder pickup = new StringBuilder(pickupTestData);
        pickup.setCharAt(2, ':');
        String pickupTime = pickup.toString();
        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String fullName = randomName() + " Autorder";
        String nameTitle = getRandomObject(salutationList);
        int rentalDuration = Integer.parseInt(map.get("rentalDuration"));
        String paymentGroup = map.get("paymentGroup");
        String paymentMethod = map.get("paymentMethod");
        int serviceCharge = Integer.parseInt(map.get("serviceCharge"));
        String otp = map.get("otp");

        step("Create Car Rental order via API and fetch the orderID");
        Map<String, String> orderIdAndHash = carRentalOrderPage.createCarRentalProductTillPaymentApi(email, phoneNumber, passwordDweb, driverOption, location, startDate, pickupTime, type, fullName, nameTitle, rentalDuration);
        String orderIdCreated = orderIdAndHash.get("orderId");
        String orderHashCreated = orderIdAndHash.get("orderHash");

        step("Login and select payment method");
        int finalPrice = paymentHelperPage.loginAndSelectPaymentMethod(email, passwordDweb, otp, orderIdCreated, orderHashCreated, paymentGroup, paymentMethod, serviceCharge);

        step("Open price history");
        landingPage.openPriceSummary();
        Assertion.assertThat("Is transaction fees is available", landingPage.isTransactionFeesDisplayed(), is(false));

        step("Close the price history");
        landingPage.closeDrawer();

        step("Click on Pay button");
        landingPage.clickOnPaymentButton();

        step("Click on DANA webview pay button");
        landingPage.clickDanaWebviewPayButton();

        step("Verify DANA payment is successful");
        Assertion.assertThat("Verify DANA payment is successful", landingPage.isPaymentSuccessfulVisible(), is(true));

    }
    @DataProvider(name = "verifyCheckoutWithDANAWebDataProvider")
    public Object[][] verifyCheckoutWithDANAWebDataProvider() {return getObjectParams("verifyCheckoutWithDANAWeb");}

    @Author(names = {"Shubham Bansal"})
    @Module(name = "PaymentMethod")
    @Tribe(name = "Payment")
    @Vertical(name = "Platform")
    @Test(dataProvider = "verifyOrderSummaryOnPaymentPagesWebDataProvider")
    public void verifyOrderSummaryOnPaymentPagesWeb(Map<String, String> map) throws InterruptedException, FailedLoginException, ExecutionException {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);
        orderCreationPage<?> orderCreationPage = new orderCreationPage<>(session);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        String email = map.get("email");
        String password = map.get("password");
        String currency = map.get("currency");
        String query = map.get("hotelName");
        String startDate = generateRandomDate(map.get("rangeAddDate"));
        int adult = Integer.parseInt(map.get("adult"));
        int night = Integer.parseInt(map.get("night"));
        int room = Integer.parseInt(map.get("room"));
        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = getRandomObject(salutationList);
        String language = map.get("language");
        String paymentGroup = map.get("paymentGroup");
        String paymentMethod = map.get("paymentMethod");
        String otp = map.get("otp");

        step("Create hotel order in " + currency + " currency");
        Map<String,String> orderIdAndHash = orderCreationPage.createWaitingPaymentOrderApi(email, password, startDate, query, adult, night, room, orderFullName, orderNameTitle, guestFullName, guestTitle, currency, language);
        String orderIdCreated = orderIdAndHash.get("orderId");
        String orderHashCreated = orderIdAndHash.get("orderHash");

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, 0);

        step("Select English language if not in English");
        homePage.selectEnglishLanguageOnly();

        step("Go to the payment page");
        paymentPage.goToPaymentPage(orderIdCreated, orderHashCreated);

        step("Verify payment page is loaded");
        Assertion.assertThat("Verify the payment page is loaded", paymentPage.isPaymentPageLoaded(), is(true));

        step("Verify order summary on landing payment page");
        String landingProductNameText = paymentPage.getAndVerifyOrderSummaryProduct();

        step("Click on select payment method");
        paymentPage.clickOnSelectPaymentMethod();

        step("Scroll down to the payment Group");
        paymentPage.goToPaymentMethodsGroup(paymentGroup);

        step("Select Payment Method");
        paymentPage.clickOnPaymentGroup(paymentGroup);
        paymentPage.selectPaymentMethod(paymentMethod);

        step("Click on Pay button");
        paymentPage.clickOnPaymentButton();

        step("Verify confirmation page");
        Assertion.assertThat("Verify the confirmation page is loaded", paymentPage.isPaymentConfirmationPageLoaded(), is(true));

        step("Verify order summary on payment confirmation page");
        String confirmationProductNameText = paymentPage.getAndVerifyOrderSummaryProduct();

        Assertion.assertThat("Verify order summary product name matches between landing and confirmation",
                confirmationProductNameText, is(landingProductNameText));
    }
    @DataProvider(name = "verifyOrderSummaryOnPaymentPagesWebDataProvider")
    public Object[][] verifyOrderSummaryOnPaymentPagesWebDataProvider() {return getObjectParams("verifyOrderSummaryOnPaymentPagesWeb");}

    @Author(names = {"Hiten","Ashish","Shubham Bansal"})
    @Module(name = "PaymentMethod")
    @Tribe(name = "Payment")
    @Vertical(name = "Platform")
    @Test
    public void verifyPaymentUsingB2BCorporatePayment () throws InterruptedException, FailedLoginException {
        Map<String, String> map = getParam("verifyPaymentUsingB2BCorporatePayment");
        BaseTest.paramsThreadLocal.set(map);
        Session<WebDriver> session = sessionThreadLocal.get();

        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        HotelLandingPageWeb<?> hotelLandingPage = HotelLandingPageWeb.getInstance(session);
        HotelRoomListPage<?> hotelRoomListPage = new HotelRoomListPage<>(session);
        HotelBookingFormPageWeb<?> hotelBookingFormPage = HotelBookingFormPageWeb.getInstance(session);
        CorporateDashboard<?> corporateDashboard = new CorporateDashboard<>(session);
        TravelPlanWeb<?> travelPlanPage = TravelPlanWeb.getInstance(session);

        String username = map.get("username");
        String password = map.get("password");
        String currency = map.get("currency");
        String query = map.get("hotelName");
        String config = map.get("config");
        String database = map.get("database");
        String collectionName = map.get("collectionName");
        String otp = map.get("otp");
        int retryCounter = Integer.parseInt(map.get("retryCounter"));

        step("Go to home");
        homePage.goToHomePage();

        step("Login as " + username + " via session injection");
        loginPage.loginIfNotWithSameEmail(username, password, otp, retryCounter);

        step("go to tiket.com page from corp dashboard");
        corporateDashboard.goBackToTiketHomePage();

        step("User select IDR currency");
        homePage.selectCurrency(currency);

        step("Select english language if not in english");
        homePage.selectEnglishLanguageOnly();

        step("User open hotel vertical");
        homePage.openVerticalPageViaURL("Hotel");

        step("User search hotel location");
        hotelLandingPage.searchStayLocation(query);

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
        hotelBookingFormPage.continueTo2NdPartThenPaymentPage();
        hotelBookingFormPage.clickOnPopUp();
        travelPlanPage.skipTravelPlan();

        step("Payment page should be displayed");
        String orderID = paymentPage.getOrderId();
        int finalPrice = Integer.parseInt(paymentPage.getFinalPrice());
        Assertion.assertThat("Final amount should be greater than zero", finalPrice, is(greaterThan(0)));
        orderID = String.valueOf(getOrderId(orderID));
        Assertion.assertThat("Corporate billing option is selected", paymentPage.getSelectedPayment().toLowerCase(), is("corporate billing"));

        step("Click on pay button");
        paymentPage.clickOnPaymentButton();

        step("Verify the payment successful page is visible or not");
        Assertion.assertThat("Verify the payment successful page is visible",
                paymentPage.isPaymentSuccessfulVisible(), is(true));

        step("Verify the status from database");
        String queryDB = paymentPage.queryString(orderID);
        Map<String, String> paymentTransactionDetails = paymentPage.getPaymentTransactionDetails(
                config, database, collectionName, queryDB);
        String paymentStatus = paymentTransactionDetails.get("paymentStatus");
        String paymentAmount = paymentTransactionDetails.get("paymentAmount");
        Assertion.assertThat("Verify the payment status is paid", paymentStatus, is("PAID"));
        Assertion.assertThat("Verify the paid amount", Double.parseDouble(paymentAmount),
                is(Double.valueOf(finalPrice)));
    }

    @Author(names = "Manan Kumar")
    @Module(name = "Payment")
    @Tribe(name = "Refund")
    @Vertical(name = "Platform")
    @Test
    public void testVerifyRefundCompleteFlowRescheduleWithCashbackFlight() throws InterruptedException, FailedLoginException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testVerifyRefundCompleteFlowRescheduleWithCashbackFlight");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPage = FlightLandingPageWeb.getInstance(session);
        FlightSearchResultPageWeb<?> flightSearchResultPageWeb = FlightSearchResultPageWeb.getInstance(session);
        FlightProductReviewPageWeb<?> flightProductReviewPageWeb = FlightProductReviewPageWeb.getInstance(session);
        FlightBookingFormPageWeb<?> flightBookingFormPageWeb = FlightBookingFormPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPageWeb = PaymentPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        FlightReschedulePageWeb<?> flightReschedulePageWeb = FlightReschedulePageWeb.getInstance(session);
        FlightRescheduleCartWeb<?> flightRescheduleCartWeb = FlightRescheduleCartWeb.getInstance(session);
        FlightApi<?> flightApi = new FlightApi<>(session);

        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");
        String destination2 = param.get("destination2");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));

        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);

        String username = param.get("username");
        String otp = param.get("otp");
        String password = param.get("password");
        String airlineName = param.get("airlineName");

        // Dummy params to find dynamic data
        int adult = 1;
        int child = 0;
        int infant = 0;
        String searchType = "oneway";

        step("Find dynamic data 1");
        FlightDynamicData flightDynamicData1 = flightApi.findDate(
                username,password,
                originAirport,destinationAirport,daysToDepartureDate,
                adult, child, infant,
                searchType, airlineName,"", "", "", ""
        );

        originAirport = flightDynamicData1.originAirport;
        destinationAirport = flightDynamicData1.destinationAirport;
        departureDate = CoreFunctions.convertToLongDate(flightDynamicData1.departureDate);
        airlineName = flightDynamicData1.airlineDepart;

        step("Find dynamic data 2");
        FlightDynamicData flightDynamicData2 = flightApi.findDate(
                username,password,
                originAirport,destination2,daysToDepartureDate,
                adult, child, infant,
                searchType, airlineName,"", "", "", ""
        );
        destination2 = flightDynamicData2.destinationAirport;

        step("User open home page");
        homePage.goToHomePage();

        step("Logging in using {" + username + "}");
        loginPage.loginIfNotWithSameEmail(username, password, otp, 1);

        step("User open Flight Landing Page");
        homePage.openVerticalPageViaURL("Flights");

        step("Select origin airport");
        flightLandingPage.selectOrigin(originAirport);

        step("Select destination airport");
        flightLandingPage.selectDestination(destinationAirport);

        step("Select date");
        flightLandingPage.selectDate(departureDate);

        step("search flight");
        flightLandingPage.searchFlight();

        step("filter airline {" + airlineName + " }");
        flightSearchResultPageWeb.filterAirline(airlineName);

        step("select flight for departure");
        flightSearchResultPageWeb.selectFlight();

        step("User Verifies that flight have Reschedule Available.");
        Assertion.assertThat("Is Reschedule Available for this flight?", flightProductReviewPageWeb.isRescheduleAvailable(), is(true));

        step("select the SELECT Button");
        flightProductReviewPageWeb.clickDownPaySelectBtn();

        step("User clicks No thanks button");
        flightProductReviewPageWeb.clickNoThanksBtn();

        step("User enter the passenger Details");
        flightBookingFormPageWeb.fillPassengerDetails();

        step("User fills birthday date");
        flightBookingFormPageWeb.fillBirthdayWithLastClickableDate();

        if(Configs.PLATFORM.equals(Platform.AMWEB)){
            step("User clicks on Save Button ");
            flightLandingPage.clickSaveButton();

            step("Click on continue button");
            flightLandingPage.clickContinueButton();
        }

        step("User unchecks the extra protection option");
        flightBookingFormPageWeb.selectChooseBtn();

        step("going to paymentPage");
        flightBookingFormPageWeb.goToPaymentPageFromBookingFormPage();
        flightBookingFormPageWeb.clickNoThanksOnFlightMayReschedulePopup();

        step("click I don't want it");
        flightBookingFormPageWeb.clickOkayGotIt();

        step("User closes baggage change popup ");
        flightBookingFormPageWeb.clickGotItInBaggagePopup();

        step("Payment page should be displayed");
        Assertion.assertThat("PaymentPageLoaded", paymentPageWeb.isPaymentPagepresent(), is(true));

        step("User selects Sakuku payment method");
        paymentPageWeb.selectSakukuPaymentMethod();

        step("Validate all suppliers are mock issued by airline, skipped if real issued supplier exist");
        boolean isDepartSuppliersMocked1 = flightApi.areAllSuppliersMockIssuedByAirline(flightDynamicData1.airlineCodeDepart, originAirport, destinationAirport);

        step("Validate all suppliers are mock issued by airline, skipped if real issued supplier exist");
        boolean isDepartSuppliersMocked2 = flightApi.areAllSuppliersMockIssuedByAirline(flightDynamicData2.airlineCodeDepart, originAirport, destination2);

        step("only submit payment if all suppliers are mocked { isDepartSuppliersMocked: " + (isDepartSuppliersMocked1 && isDepartSuppliersMocked2) +" }");
        if(!(isDepartSuppliersMocked1 && isDepartSuppliersMocked2)) {
            throw new SkipException("Skipping the test since not all suppliers are mocked, cannot proceed to submit payment");
        }

        step("User Submit payment method");
        String orderId = paymentPageWeb.getOrderId();
        Assertion.assertThat("Order Id successfully generated", orderId, notNullValue());

        step("User clicks Pay in full button");
        paymentPageWeb.clickPayInFullButton();

        step("Verify payment successful");
        Assertion.assertThat("Payment should be successful", paymentPageWeb.isPaymentSuccessfulHeaderVisible(), is(true));

        step("Go to Order Detail Page");
        orderListPage.openActiveTicketViaURL(orderId);

        step("Click on Reschedule button");
        orderListPage.clickRescheduleButton();

        step("Pick pax that will be reschedule product");
        flightReschedulePageWeb.selectAllPassengers();

        step("Change destination airport to " + destination2);
        flightReschedulePageWeb.changeDestinationAirport(destination2);

        step("User click on Find new Schedule");
        flightReschedulePageWeb.pressFindNewScheduleButton();

        step("filter airline {" + airlineName + " }");
        flightSearchResultPageWeb.filterAirline(airlineName);

        step("select flight for departure");
        flightSearchResultPageWeb.selectFlight();

        step("User verifies +IDR 0 is present");
        Assertion.assertThat("IDR 0 should be present", flightReschedulePageWeb.isIDRZeroPresent(), is(true));

        step("Select Reschedule Flight");
        flightReschedulePageWeb.clickSelectRescheduleFlightButton();

        step("Click on Continue to Payment");
        flightRescheduleCartWeb.clickContinueToPayment();

        step("Confirm Continue to Payment popup");
        flightRescheduleCartWeb.confirmContinueToPayment();

        step("User closes baggage change popup ");
        flightBookingFormPageWeb.clickRefundOkGotIt();

        step("Click on Pay button to pay with Refund Deposit");
        paymentPageWeb.clickOnPaymentButton();

        step("Click on Terms and Condition checkbox");
        paymentPageWeb.clickTermsAndConditionCheckbox();

        step("Click on Submit Refund Request button");
        paymentPageWeb.clickSubmitRefundButton();

        step("Verify Reschedule request submitted amd created");
        Assertion.assertThat("Reschedule request submitted should be visible", paymentPageWeb.isRescheduleRequestSubmittedVisible(), is(true));
    }

    @Author(names = {"Yoan"})
    @Module(name = "PaymentMethod")
    @Tribe(name = "Payment")
    @Vertical(name = "Platform")
    @Test(dataProvider = "verifyCountdownAndCompleteBeforeWebDataProvider")
    public void verifyCountdownAndCompleteBeforeWeb(Map<String, String> map) throws InterruptedException, FailedLoginException, ExecutionException {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PaymentPageWeb<?> landingPage = PaymentPageWeb.getInstance(session);
        CreateCarRentalOrderPage<?> carRentalOrderPage = new CreateCarRentalOrderPage<>(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);

        String email = map.get("email");
        String phoneNumber = map.get("phoneNumber");
        String passwordDweb = map.get("passwordDweb");
        String driverOption = map.get("driverOption");
        String location = map.get("location");
        String startDate = generateRandomDate(map.get("rangeAddDate"));
        String type = map.get("type");
        String pickupTestData = map.get("pickupTime");
        StringBuilder pickup = new StringBuilder(pickupTestData);
        pickup.setCharAt(2, ':');
        String pickupTime = pickup.toString();
        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String fullName = randomName() + " Autorder";
        String nameTitle = getRandomObject(salutationList);
        int rentalDuration = Integer.parseInt(map.get("rentalDuration"));
        String paymentGroup = map.get("paymentGroup");
        String paymentMethod = map.get("paymentMethod");
        int serviceCharge = Integer.parseInt(map.get("serviceCharge"));
        String otp = map.get("otp");

        step("Create Car Rental order via API and fetch the orderID");
        Map<String,String> orderIdAndHash = carRentalOrderPage.createCarRentalProductTillPaymentApi(email, phoneNumber, passwordDweb, driverOption, location, startDate, pickupTime, type, fullName, nameTitle, rentalDuration);
        String orderIdCreated = orderIdAndHash.get("orderId");
        String orderHashCreated = orderIdAndHash.get("orderHash");

        step("Login into dweb if not login using the given account");
        loginPage.loginIfNotWithSameEmail(email,passwordDweb,otp,0);

        step("Select english language if not in english");
        homePage.selectEnglishLanguageOnly();

        step("Go to payment page");
        landingPage.goToPaymentPage(orderIdCreated, orderHashCreated);

        String orderId = landingPage.getOrderId();
        int beforePrice =(int) Double.parseDouble(landingPage.getFinalPrice());

        step("Verify payment page is loaded");
        Assertion.assertThat("Verify payment page is loaded or not", landingPage.isPaymentPageLoaded(), is(true));

        step("Validate countdown in landing is visible");
        Assertion.assertThat("Is countdown visible", landingPage.isCountdownTimerVisible(), is(true));

        step("Validate countdown in landing is running");
        Assertion.assertThat("Is countdown running", landingPage.isCountdownRunning(), is(true));

        step("Access payment group");
        landingPage.clickOnSelectPaymentMethod();

        step("Scroll down to access selected payment group");
        landingPage.goToPaymentMethodsGroup(paymentGroup);

        step("Select payment group");
        landingPage.clickOnPaymentGroup(paymentGroup);

        step("Select Payment Method");
        landingPage.selectPaymentMethod(paymentMethod);

        step("Open price history");
        landingPage.openPriceSummary();
        Assertion.assertThat("Is transaction fees is available", landingPage.isTransactionFeesDisplayed(), is(false));

        step("Close the price history");
        landingPage.closeDrawer();

        step("Click on Pay button");
        landingPage.clickOnPaymentButton();

        step("Verify confirmation page");
        Assertion.assertThat("Verify the confirmation page is loaded", landingPage.isPaymentConfirmationPageLoaded(), is(true));

        step("Validate countdown in confirmation is visible");
        Assertion.assertThat("Is countdown visible", landingPage.isCountdownTimerVisible(), is(true));

        step("Validate due date in confirmation page");
        Assertion.assertThat("Is due date correct", landingPage.isDueDateConfirmationVisible(), is(true));

    }
    @DataProvider(name = "verifyCountdownAndCompleteBeforeWebDataProvider")
    public Object[][] verifyCountdownAndCompleteBeforeWebDataProvider() {return getObjectParams("verifyCountdownAndCompleteBeforeWeb");}


  @Author(names = { "Hiten", "Yoan" })
  @Module(name = "PaymentMethod")
  @Tribe(name = "Payment")
  @Vertical(name = "Platform")
  @Test(dataProvider = "verifyPaymentMethodVABCASNAPDataProvider")
  public void verifyPaymentMethodVABCASNAP(Map<String, String> map) throws InterruptedException, FailedLoginException {
    Session<?> session = BaseTest.sessionThreadLocal.get();
    PaymentPageWeb<?> landingPage = PaymentPageWeb.getInstance(session);
    CreateCarRentalOrderPage<?> carRentalOrderPage = new CreateCarRentalOrderPage<>(session);
    PaymentApi<?> paymentApi = new PaymentApi<>(session);
    LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
    HomePageWeb<?> homePage = HomePageWeb.getInstance(session);

    String email = map.get("email");
    String phoneNumber = map.get("phoneNumber");
    String passwordDweb = map.get("passwordDweb");
    String driverOption = map.get("driverOption");
    String location = map.get("location");
    String startDate = generateRandomDate(map.get("rangeAddDate"));
    String type = map.get("type");
    String pickupTestData = map.get("pickupTime");
    StringBuilder pickup = new StringBuilder(pickupTestData);
    pickup.setCharAt(2, ':');
    String pickupTime = pickup.toString();
    String[] salutationList = {"Mr", "Mrs", "Ms"};
    String fullName = randomName() + " Autorder";
    String nameTitle = getRandomObject(salutationList);
    int rentalDuration = Integer.parseInt(map.get("rentalDuration"));
    String config = map.get("config");
    String database = map.get("database");
    String collectionName = map.get("collectionName");
    String paymentGroup = map.get("paymentGroup");
    String paymentMethod = map.get("paymentMethod");
    int serviceCharge = Integer.parseInt(map.get("serviceCharge"));

    step("Create Car Rental order via API and fetch the orderID");
    Map<String,String> orderIdAndHash = carRentalOrderPage.createCarRentalProductTillPaymentApi(email, phoneNumber, passwordDweb, driverOption, location, startDate, pickupTime, type, fullName, nameTitle, rentalDuration);
    String orderIdCreated = orderIdAndHash.get("orderId");
    String orderHashCreated = orderIdAndHash.get("orderHash");

    step("Login as " + email + " via session injection");
    loginPage.loginWithSpecificAccountViaCookie(email, passwordDweb);

    step("Select english language if not in english");
    homePage.selectEnglishLanguageOnly();

    step("Go to payment page");
    landingPage.goToPaymentPage(orderIdCreated, orderHashCreated);

    step("Fetch the order id and the price on the landing page");
    String orderId = landingPage.getOrderId();
    int beforePrice = (int) Double.parseDouble(landingPage.getFinalPrice());

    step("Verify payment page is loaded");
    Assertion.assertThat("Verify payment page is loaded or not", landingPage.isPaymentPageLoaded(), is(true));

    step("Access payment group");
    landingPage.clickOnSelectPaymentMethod();

    step("Scroll down to access selected payment group");
    landingPage.goToPaymentMethodsGroup(paymentGroup);

    step("Select payment group");
    landingPage.clickOnPaymentGroup(paymentGroup);

    step("Select Payment Method");
    landingPage.selectPaymentMethod(paymentMethod);
    Assertion.assertThat("Verify the selected payment", landingPage.getSelectedPayment(), is(paymentMethod));

    step("Open price history");
    landingPage.openPriceSummary();

    step("Fetch the price after payment method is applied");
    int afterPrice = (int) Double.parseDouble(landingPage.getFinalPrice());

    step("Verify the service charge after payment is applied");
    Assertion.assertThat("Verify the service charge", (afterPrice - beforePrice), is(serviceCharge));

    step("Close the price history");
    landingPage.closeDrawer();

    step("Click on Pay button");
    landingPage.clickOnPaymentButton();

    step("Verify confirmation page");
    Assertion.assertThat("Verify the confirmation page is loaded", landingPage.isPaymentConfirmationPageLoaded(), is(true));

    step("Get BCA Snap access token via API");
    String bcaSnapAccessToken = paymentApi.getBCAVASnapAccessToken();
    Assertion.assertThat("BCA Snap access token is retrieved", bcaSnapAccessToken, not(blankOrNullString()));

    step("Hit BCA Snap VA inquiry API");
    BCAVASnapInquiryResponse bcaSnapInquiryResponse = paymentApi.hitBCAVASnapInquiry(bcaSnapAccessToken, orderId);
    Assertion.assertThat("BCA Snap inquiry is successful", bcaSnapInquiryResponse.responseCode, is("2002400"));
    Assertion.assertThat("BCA Snap inquiry status is success", bcaSnapInquiryResponse.virtualAccountData.inquiryStatus, is("00"));

    step("Hit BCA Snap VA payment API");
    BCAVASnapPaymentResponse bcaSnapPaymentResponse = paymentApi.hitBCAVASnapPayment(
            bcaSnapAccessToken, orderId, afterPrice, bcaSnapInquiryResponse);
    Assertion.assertThat("BCA Snap payment is successful",
            bcaSnapPaymentResponse.responseMessage.toLowerCase(), containsString("success"));

    step("Verify the payment successful page is visible or not");
    Assertion.assertThat("Verify the payment successful page is visible", landingPage.isPaymentSuccessfulVisible(), is(true));

    step("Verify the status from database");
    String query = landingPage.queryString(orderIdCreated);
    Map<String,String> paymentTransactionDetails = landingPage.getPaymentTransactionDetails(config, database, collectionName, query);
    String paymentStatus = paymentTransactionDetails.get("paymentStatus");
    String paymentAmount = paymentTransactionDetails.get("paymentAmount");
    Assertion.assertThat("Verify the payment status is paid", paymentStatus, is("PAID"));
    Assertion.assertThat("Verify the paid amount", Double.parseDouble(paymentAmount), is(Double.valueOf(afterPrice)));

  }

  @DataProvider(name = "verifyPaymentMethodVABCASNAPDataProvider")
  public Object[][] verifyPaymentMethodVABCASNAPDataProvider() {return getObjectParams("verifyPaymentMethodVABCASNAP");}

    @Author(names = {"Manan Kumar"})
    @Module(name = "SavedPaymentMethod")
    @Tribe(name = "Payment")
    @Vertical(name = "Platform")
    @Test
    public void verifyAddCreditCardWithAvsOnSavedPaymentMethodWeb() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = BaseTest.sessionThreadLocal.get();
        Map<String, String> map = getParam("verifyAddCreditCardWithAvsOnSavedPaymentMethodWeb");
        BaseTest.paramsThreadLocal.set(map);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        SavedPaymentMethodWeb<?> savedPaymentMethodWeb = SavedPaymentMethodWeb.getInstance(session);

        String email = map.get("email");
        String password = map.get("password");
        String otp = map.get("otp");
        String cardNumber = map.get("cardNumber");
        String cardMonth = map.get("cardMonth");
        String cvv = map.get("cvv");
        String cardHolderName = map.get("cardHolderName");
        String postalCode = map.get("postalCode");
        String billingAddress = map.get("billingAddress");
        String lastFourDigits = cardNumber.substring(cardNumber.length() - 4);
        String panelUsername = map.get("panelUsername");
        String panelPassword = map.get("panelPassword");
        String avsRuleIdActive = map.get("avsRuleIdActive");
        String avsRuleIdInactive = map.get("avsRuleIdInactive");
        String avsBin = map.get("avsBin");

        step("Ensure AVS rule is active via panel API");
        PaymentDashboardPage<?> paymentDashboardPage = new PaymentDashboardPage<>(session);
        AvsCheckConfiguration<?> avsCheckConfiguration = new AvsCheckConfiguration<>(session);
        paymentDashboardPage.goToAdminCarePage(panelUsername, panelPassword);
        avsCheckConfiguration.ensureAvsRuleEnabledForCard(cardNumber, avsRuleIdActive, avsRuleIdInactive, avsBin);

        step("Given user is on home page");
        homePage.goToHomePage();

        step("And user is in login state");
        if (Configs.PLATFORM.equals(Platform.DWEB)) {
            loginPage.loginWithSpecificAccountViaCookie(email, password);
        } else {
            loginPage.loginIfNotWithSameEmail(email, password, otp, 0);
        }

        step("Select english language if not in english");
        homePage.selectEnglishLanguageOnly();

        step("And user click on profile icon and goes to saved payment method");
        if (Configs.PLATFORM.equals(Platform.DWEB)) {
            homePage.goToSavedPaymentMethod();
        } else {
            homePage.goToAccountPage();
            homePage.goToSavedPaymentMethod();
        }

        step("Verify saved payment method page is opened");
        Assertion.assertThat("Saved payment method page should be opened", savedPaymentMethodWeb.waitForSavedPaymentMethodTitleAppear(10), is(true));

        step("User verifies that the card is there or not");
        boolean isCardPresent = savedPaymentMethodWeb.isCardPresent(lastFourDigits);

        if(isCardPresent) {
            step("User deletes/remove the card");
            savedPaymentMethodWeb.removeCard(lastFourDigits);

            step("User verifies whether the card is removed");
            Assertion.assertThat("Card should not be present", savedPaymentMethodWeb.isCardPresent(lastFourDigits), is(false));
        }

        step("User clicks on add credit card");
        savedPaymentMethodWeb.addCard();

        step("User enters the credit card details");
        savedPaymentMethodWeb.addCardDetails(cardNumber, cardMonth, cvv, cardHolderName);

        step("User enters AVS details");
        Assertion.assertThat("AVS postal code field should be displayed", savedPaymentMethodWeb.isAvsPostalCodeDisplayed(), is(true));
        savedPaymentMethodWeb.addAvsDetails(postalCode, billingAddress);

        step("User saves the account");
        savedPaymentMethodWeb.saveAccount();

        step("User verifies 3DS");
        savedPaymentMethodWeb.verify3DS();

        step("User verifies whether the card is added or not");
        Assertion.assertThat("Card should be present", savedPaymentMethodWeb.isCardPresent(lastFourDigits), is(true));

        step("User deletes/remove the card");
        savedPaymentMethodWeb.removeCard(lastFourDigits);

        step("User verifies whether the card is removed");
        Assertion.assertThat("Card should not be present", savedPaymentMethodWeb.isCardPresent(lastFourDigits), is(false));
    }

    @Author(names = {"Hafizh"})
    @Module(name = "PaymentMethod")
    @Tribe(name = "Payment")
    @Vertical(name = "Platform")
    @Test(dataProvider = "verifyCheckoutNonTokenizedCardLoginWebDataProvider")
    public void verifyCheckoutNonTokenizedCardLoginWeb(Map<String, String> map) throws InterruptedException , FailedLoginException {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);
        PaymentPageHelperWeb<?> paymentHelperPage = PaymentPageHelperWeb.getInstance(session);
        CreateCarRentalOrderPage<?> carRentalOrderPage = new CreateCarRentalOrderPage<>(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);

        String email = map.get("email");
        String phoneNumber = map.get("phoneNumber");
        String passwordDweb = map.get("passwordDweb");
        String driverOption = map.get("driverOption");
        String location = map.get("location");
        String startDate = generateRandomDate(map.get("rangeAddDate"));
        String type = map.get("type");
        String pickupTestData = map.get("pickupTime");
        StringBuilder pickup = new StringBuilder(pickupTestData);
        pickup.setCharAt(2, ':');
        String pickupTime = pickup.toString();
        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String fullName = randomName() + " Autorder";
        String nameTitle = getRandomObject(salutationList);
        int rentalDuration = Integer.parseInt(map.get("rentalDuration"));
        String config = map.get("config");
        String database = map.get("database");
        String collectionName = map.get("collectionName");
        String paymentGroup = map.get("paymentGroup");
        String paymentMethod = map.get("paymentMethod");
        String cardNumber = map.get("cardNumber");
        String cardMonth = map.get("cardMonth");
        String cvv = map.get("cvv");
        String cardHolderName = map.get("cardHolderName");
        String cardNumberInDB = map.get("cardNumberInDB");

        step("Create Car Rental order via API and fetch the orderID");
        Map<String, String> orderIdAndHash = carRentalOrderPage.createCarRentalProductTillPaymentApi(email, phoneNumber, passwordDweb, driverOption, location, startDate, pickupTime, type, fullName, nameTitle, rentalDuration);
        String orderIdCreated = orderIdAndHash.get("orderId");
        String orderHashCreated = orderIdAndHash.get("orderHash");

        step("Login as " + email + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(email, passwordDweb);

        step("Select english language if not in english");
        homePage.selectEnglishLanguageOnly();

        step("Go to payment page");
        paymentPage.goToPaymentPage(orderIdCreated, orderHashCreated);

        String orderId = paymentPage.getOrderId();

        step("Verify payment page is loaded");
        Assertion.assertThat("Verify payment page is loaded or not", paymentPage.isPaymentPageLoaded(), is(true));

        step("Access payment group");
        paymentPage.clickOnSelectPaymentMethod();

        step("Scroll down to access selected payment group");
        paymentPage.goToPaymentMethodsGroup(paymentGroup);

        step("Select payment group");
        paymentPage.clickOnPaymentGroup(paymentGroup);

        step("Select Payment Method");
        paymentPage.selectPaymentMethod(paymentMethod);
        Assertion.assertThat("Verify the selected payment", paymentPage.getSelectedPayment(), is("Credit Card"));

        step("Reopen payment group");
        paymentPage.clickOnSelectPaymentMethod();

        step("Scroll down to access selected payment group");
        paymentPage.goToPaymentMethodsGroup(paymentGroup);

        step("Select payment group");
        paymentPage.clickOnPaymentGroup(paymentGroup);

        step("Close payment method group");
        paymentPage.clickClosePaymentMethodGroup();

        step("Close payment group");
        paymentPage.clickClosePaymentGroup();

        step("Verify card fields are empty after open and closing payment group");
        Assertion.assertThat("Verify all card fields are empty", paymentPage.isCardFieldsEmpty(), is(true));

        step("Input the card details");
        paymentHelperPage.inputCardDetails(cardNumber, cardMonth, cvv, cardHolderName);

        step("Click on Pay button");
        paymentPage.clickOnPaymentButton();

        step("Verify payment successful page is visible");
        Assertion.assertThat("Verify the payment successful page is visible", paymentPage.isPaymentSuccessfulVisible(), is(true));

        step("Verify the status from database");
        String query = paymentPage.queryString(orderId);
        Map<String,String> paymentTransactionDetails = paymentPage.getPaymentTransactionDetails(config, database, collectionName, query);
        String paymentStatus = paymentTransactionDetails.get("paymentStatus");
        String cardHolderNumber = paymentTransactionDetails.get("cardHolderNumber");
        Assertion.assertThat("Verify payment status is paid", paymentStatus, is("PAID"));
        Assertion.assertThat("Verify card number is using new non tokenized card", cardHolderNumber, is(cardNumberInDB));
    }

    @DataProvider(name = "verifyCheckoutNonTokenizedCardLoginWebDataProvider")
    public Object[][] verifyCheckoutNonTokenizedCardLoginWebDataProvider() {return getObjectParams("verifyCheckoutNonTokenizedCardLoginWeb");}


    @Author(names = {"Nasha"})
    @Module(name = "OrderSummary")
    @Tribe(name = "Payment")
    @Vertical(name = "Platform")
    @Test
    public void verifyOrderSummaryOnPaymentPageMultiCity() throws InterruptedException, FailedLoginException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyOrderSummaryOnPaymentPageMultiCity");
        BaseTest.paramsThreadLocal.set(param);

        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);
        FlightApi<?> flightApi = new FlightApi<>(session);

        List<String[]> routes = CoreFunctions.splitPairs(param.get("routes"), "\\|", "-");
        int legCount = routes.size();
        List<String> departureDates = CoreFunctions.generateAscendingRandomLongDates(
                param.get("rangeAddDate"), param.get("gapRange"), legCount);
        List<String> airlineCodes = CoreFunctions.expandToSize(param.get("airlines"), "\\|", legCount);

        step("Create a multi-city order for " + legCount + " journeys via API, left awaiting payment");
        log("Routes: " + param.get("routes") + ", airlines: " + airlineCodes + ", departure dates: " + departureDates);
        FlightApi.MultiCityOrder order = flightApi.createMultiCityOrderWaitingForPayment(
                param.get("username"), param.get("password"), routes, departureDates, airlineCodes,
                Integer.parseInt(param.get("adult")), Integer.parseInt(param.get("child")),
                Integer.parseInt(param.get("infant")), param.get("cabinClass"), param.get("phoneNumber"));
        Assertion.assertThat("Multi-city order is created", order, notNullValue());

        step("Reuse the API session in the browser");
        loginPage.loginWithSpecificAccountViaCookie(param.get("username"), param.get("password"));

        step("Open the payment page by link for order " + order.orderId());
        paymentPage.goToPaymentPage(order.orderId(), order.orderHash());
        Assertion.assertThat("Payment page is loaded", paymentPage.isPaymentPageLoaded(), is(true));

        step("Verify the multi-city order summary on the payment page");
        paymentPage.verifyMultiCityOrderSummary(legCount, param.get("expectedOrderSummaryTitle"),
                param.get("orderSummaryIcon"));
    }

    @Author(names = {"Pankaj Singh Kushwaha"})
    @Module(name = "PaymentMethod")
    @Tribe(name = "Payment")
    @Vertical(name = "Sphinx")
    @Test
    public void verifySeeAllButtonShowsPointerCursorOnCardDetailsPage () throws InterruptedException, FailedLoginException {
        Map<String, String> map = getParam("verifySeeAllButtonShowsPointerCursorOnCardDetailsPage");
        BaseTest.paramsThreadLocal.set(map);
        Session<WebDriver> session = sessionThreadLocal.get();

        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        HotelLandingPageWeb<?> hotelLandingPage = HotelLandingPageWeb.getInstance(session);
        HotelRoomListPage<?> hotelRoomListPage = new HotelRoomListPage<>(session);
        HotelBookingFormPageWeb<?> hotelBookingFormPage = HotelBookingFormPageWeb.getInstance(session);
        CorporateDashboard<?> corporateDashboard = new CorporateDashboard<>(session);
        TravelPlanWeb<?> travelPlanPage = TravelPlanWeb.getInstance(session);

        String username = map.get("username");
        String password = map.get("password");
        String currency = map.get("currency");
        String query = map.get("hotelName");
        String config = map.get("config");
        String database = map.get("database");
        String collectionName = map.get("collectionName");
        String otp = map.get("otp");
        String hoverColor = map.get("hoverColor");
        int retryCounter = Integer.parseInt(map.get("retryCounter"));

        step("Go to home");
        homePage.goToHomePage();

        step("Login as " + username + " via session injection");
        loginPage.loginIfNotWithSameEmail(username, password, otp, retryCounter);

        step("User select IDR currency");
        homePage.selectCurrency(currency);

        step("Select english language if not in english");
        homePage.selectEnglishLanguageOnly();

        step("User open hotel vertical");
        homePage.openVerticalPageViaURL("Hotel");

        step("User search hotel location");
        hotelLandingPage.searchStayLocation(query);

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
        hotelBookingFormPage.continueTo2NdPartThenPaymentPage();
        hotelBookingFormPage.clickOnPopUp();

        step("Payment page should be displayed");
        Assertion.assertThat("Verify payment page is displayed", paymentPage.isPaymentPageLoaded(), is(true));

        step("See all button should show pointer cursor");
        paymentPage.verifySeeAllButtonHasPointerCursor();

        step("Click See all button");
        paymentPage.clickSeeAllButton();

        step("Payment method should show pointer cursor and "+ hoverColor +"background on hover");
        paymentPage.verifyPaymentMethodHoverShowsPointerCursorAndBackground(hoverColor);
    }

    @Author(names = {"Pankaj Singh Kushwaha"})
    @Module(name = "PaymentMethod")
    @Tribe(name = "Payment")
    @Vertical(name = "Sphinx")
    @Test
    public void verifyPaymentTypeNotTransformedAfterCheckoutWithPromo() throws InterruptedException, FailedLoginException {
        Map<String, String> map = getParam("verifyPaymentTypeNotTransformedAfterCheckoutWithPromo");
        BaseTest.paramsThreadLocal.set(map);
        Session<WebDriver> session = sessionThreadLocal.get();

        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        HotelLandingPageWeb<?> hotelLandingPage = HotelLandingPageWeb.getInstance(session);
        HotelRoomListPage<?> hotelRoomListPage = new HotelRoomListPage<>(session);
        HotelBookingFormPageWeb<?> hotelBookingFormPage = HotelBookingFormPageWeb.getInstance(session);

        String username = map.get("username");
        String password = map.get("password");
        String currency = map.get("currency");
        String query = map.get("hotelName");
        String cardNumber = map.get("cardNumber");
        String cardExpiry = map.get("cardExpiry");
        String cardCvv = map.get("cardCvv");
        String cardHolderName = map.get("cardHolderName");
        String promoCode = map.get("promoCode");
        String expectedPaymentType = map.get("expectedPaymentType");
        String unexpectedPaymentType = map.get("unexpectedPaymentType");
        String otp = map.get("otp");
        int retryCounter = Integer.parseInt(map.get("retryCounter"));

        step("Go to home");
        homePage.goToHomePage();

        step("Login as " + username + " via session injection");
        loginPage.loginIfNotWithSameEmail(username, password, otp, retryCounter);

        step("User select " + currency + " currency");
        homePage.selectCurrency(currency);

        step("Select english language if not in english");
        homePage.selectEnglishLanguageOnly();

        step("GIVEN user has created an order: open hotel vertical and search");
        homePage.openVerticalPageViaURL("Hotel");
        hotelLandingPage.searchStayLocation(query);

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
        hotelBookingFormPage.continueTo2NdPartThenPaymentPage();
        hotelBookingFormPage.clickOnPopUp();

        step("Payment page should be displayed");
        Assertion.assertThat("Verify payment page is displayed", paymentPage.isPaymentPageLoaded(), is(true));

        step("Select Credit/Debit Card payment method");
        paymentPage.selectCreditDebitCardRadio();

        step("Fill credit/debit card details");
        paymentPage.fillCardDetails(cardNumber, cardExpiry, cardCvv, cardHolderName);

        step("Click See promos/vouchers");
        paymentPage.clickPromoSuggestion();

        step("Apply promo " + promoCode);
        paymentPage.applyPromoFromSuggestionList(promoCode);

        step("Click pay button");
        String nextPageUrl = paymentPage.clickPayAndGetNextPageUrl();

        step("Next page url should contain " + expectedPaymentType + " and not contain " + unexpectedPaymentType);
        Assertion.assertThat("Verify next page url contains " + expectedPaymentType, nextPageUrl.toLowerCase(), containsString(expectedPaymentType.toLowerCase()));
        Assertion.assertThat("Verify next page url does not contain " + unexpectedPaymentType, nextPageUrl.toLowerCase(), not(containsString(unexpectedPaymentType.toLowerCase())));
    }

    @Author(names = "Manan Kumar")
    @Module(name = "Payment")
    @Tribe(name = "Sphinx")
    @Vertical(name = "Platform")
    @Test
    public void verifySuccessDownPayOrderUsingCardInstallmentWeb() throws InterruptedException, FailedLoginException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifySuccessDownPayOrderUsingCardInstallmentWeb");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPage = FlightLandingPageWeb.getInstance(session);
        FlightSearchResultPageWeb<?> flightSearchResultPageWeb = FlightSearchResultPageWeb.getInstance(session);
        FlightProductReviewPageWeb<?> flightProductReviewPageWeb = FlightProductReviewPageWeb.getInstance(session);
        FlightBookingFormPageWeb<?> flightBookingFormPageWeb = FlightBookingFormPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPageWeb = PaymentPageWeb.getInstance(session);
        PaymentPageHelperWeb<?> paymentHelperPage = PaymentPageHelperWeb.getInstance(session);
        FlightApi<?> flightApi = new FlightApi<>(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);

        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));

        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);

        String username = param.get("username");
        String password = param.get("password");
        String airlineName = param.get("airlineName");
        String paymentGroup = param.get("paymentGroup");
        String cardNumber = param.get("cardNumber");
        String cardMonth = param.get("cardMonth");
        String cvv = param.get("cvv");
        String cardHolderName = param.get("cardHolderName");
        String installmentTenure = param.get("installmentTenure");
        String secondInstallmentTenure = param.get("secondInstallmentTenure");

        int adult = Integer.parseInt(param.get("adult"));
        int child = Integer.parseInt(param.get("child"));
        int infant = Integer.parseInt(param.get("infant"));
        String searchType = param.get("searchType");

        step("Find dynamic flight data with down pay eligible route");
        FlightDynamicData flightDynamicData = flightApi.findDate(
                username, password,
                originAirport, destinationAirport, daysToDepartureDate,
                adult, child, infant, searchType, airlineName, "", "", "", ""
        );
        originAirport = flightDynamicData.originAirport;
        destinationAirport = flightDynamicData.destinationAirport;
        departureDate = CoreFunctions.convertToLongDate(flightDynamicData.departureDate);
        airlineName = flightDynamicData.airlineDepart;

        step("User open home page");
        homePage.goToHomePage();

        step("Login as " + username + " via cookie injection");
        loginPage.loginWithSpecificAccountViaCookie(username, password);

        step("Select english language if not in english");
        homePage.selectEnglishLanguageOnly();

        step("User open Flight Landing Page");
        homePage.openVerticalPageViaURL("Flights");

        step("Select origin airport {" + originAirport + "}");
        flightLandingPage.selectOrigin(originAirport);

        step("Select destination airport {" + destinationAirport + "}");
        flightLandingPage.selectDestination(destinationAirport);

        step("Toggle flight trip type to "+ searchType);
        flightLandingPage.toggleTripType(searchType);

        step("Select departure date {" + departureDate + "}");
        flightLandingPage.selectDate(departureDate);

        step("Search flight");
        flightLandingPage.searchFlight();

        step("Filter airline {" + airlineName + "}");
        flightSearchResultPageWeb.filterAirlineForNewUI(airlineName);

        step("Select flight for departure");
        flightSearchResultPageWeb.selectFlight();

        step("Verify flight has Down Pay option");
        Assertion.assertThat("Down pay is available for this flight", flightProductReviewPageWeb.isFlightHaveDownPay(), is(true));

        step("Select down pay fare");
        flightProductReviewPageWeb.clickDownPaySelectBtn();

        step("User clicks No thanks button");
        flightProductReviewPageWeb.clickNoThanksBtn();

        step("User enters passenger details");
        flightBookingFormPageWeb.clickPassengerDetailsToggle();

        step("User clicks Review & pay button");
        flightProductReviewPageWeb.clickReviewAndPayBtn();

        step("User confirms details by clicking It's correct");
        flightProductReviewPageWeb.clickItsCorrectBtn();

        step("User clicks No thanks button for rescheduled and delayed POPUP");
        flightProductReviewPageWeb.clickNoThanksBtn();

        if (Configs.PLATFORM.equals(Platform.AMWEB)) {
            step("User clicks on Save Button");
            flightLandingPage.clickSaveButton();

            step("Click on continue button");
            flightLandingPage.clickContinueButton();
        }

        step("Going to payment page");
        flightBookingFormPageWeb.goToPaymentPageFromBookingFormPage();
        flightBookingFormPageWeb.clickNoThanksOnFlightMayReschedulePopup();


        step("Payment page should be displayed");
        Assertion.assertThat("Payment page is loaded", paymentPageWeb.isPaymentPagepresent(), is(true));
        String paidOrderId = paymentPageWeb.getOrderId();
        log("First payment order id: " + paidOrderId);

        step("User opens payment method group");
        paymentPageWeb.clickOnSelectPaymentMethod();
        paymentPageWeb.goToPaymentMethodsGroup(paymentGroup);

        step("User selects CC/DC payment method with installment");
        paymentPageWeb.selectCreditCard();

        step("User enters card details");
        paymentHelperPage.inputCardDetails(cardNumber, cardMonth, cvv, cardHolderName);

        step("User selects installment from dropdown");
        paymentPageWeb.selectInstallmentByTenure(installmentTenure);

        step("User verifies Down pay banner is visible in landing");
        Assertion.assertThat("DownPay info banner is visible on payment page", paymentPageWeb.isDownPayInfoBannerVisible(), is(true));

        step("User verifies price detail on payment landing page");
        paymentPageWeb.openPriceSummary();
        Assertion.assertThat("Price summary is visible on payment page", paymentPageWeb.isPriceSummaryVisible(), is(true));
        Assertion.assertThat("Final price is present on payment page", paymentPageWeb.getFinalPrice(), not(emptyOrNullString()));
        paymentPageWeb.closeDrawer();

        step("User verifies DownPay button is visible and clickable");
        Assertion.assertThat("DownPay button is visible and clickable", paymentPageWeb.isDownPayBtnVisibleAndClickable(), is(true));

        step("Validate all suppliers are mock issued by airline, skipped if real issued supplier exist");
        boolean isDepartSuppliersMocked = flightApi.areAllSuppliersMockIssuedByAirline(
                flightDynamicData.airlineCodeDepart, originAirport, destinationAirport);

        step("Only submit payment if all suppliers are mocked { isDepartSuppliersMocked: " + isDepartSuppliersMocked + " }");
        if (!isDepartSuppliersMocked) {
            throw new SkipException("Skipping the test since not all suppliers are mocked, cannot proceed to submit payment");
        }

        step("User clicks on DownPay button");
        paymentPageWeb.clickDownPayButton();

        step("User verifies Down pay modal and price detail");
        Assertion.assertThat("Down pay modal is visible", paymentPageWeb.isDownPayModalVisible(), is(true));

        step("User verifies current bill due date is displayed");
        Assertion.assertThat("Down pay current bill is visible", paymentPageWeb.isDownPayCurrentBillVisible(), is(true));

        step("User verifies next bill due date is displayed");
        Assertion.assertThat("Down pay due date is visible", paymentPageWeb.isDownPayDueDateVisible(), is(true));

        step("User confirms down pay checkout");
        paymentPageWeb.clickDownPayConfirmInModal();

        step("User clicks Pay button");
        paymentPageWeb.clickOnPaymentButton();

        step("Verify payment successful page is visible");
        Assertion.assertThat("Payment successful page is visible", paymentPageWeb.isPaymentSuccessfulVisible(), is(true));

        step("User verifies down pay due date is displayed on payment confirmation page");
        Assertion.assertThat("Down pay due date is visible on payment confirmation page", paymentPageWeb.isDownPayDueDateOnConfirmationPageVisible(), is(true));

        step("User opens order detail directly via URL for order " + paidOrderId);
        orderListPage.openActiveTicketViaURL(paidOrderId);

        step("User clicks Total payment on order detail page");
        orderDetailPage.clickTotalPayment();

        step("User clicks Complete the payment button");
        orderDetailPage.clickCompleteThePayment();

        step("Payment page for the subsequent payment should be displayed");
        Assertion.assertThat("Payment page is loaded for subsequent payment", paymentPageWeb.isPaymentPagepresent(), is(true));
        String secondPaidOrderId = paymentPageWeb.getOrderId();
        log("Subsequent payment order id: " + secondPaidOrderId);

        step("User opens payment method group for subsequent payment");
        paymentPageWeb.clickOnSelectPaymentMethod();
        paymentPageWeb.goToPaymentMethodsGroup(paymentGroup);

        step("User selects CC/DC payment method for subsequent payment");
        paymentPageWeb.selectCreditCard();

        step("User enters card details for subsequent payment");
        paymentHelperPage.inputCardDetails(cardNumber, cardMonth, cvv, cardHolderName);

        step("User selects second installment tenure {" + secondInstallmentTenure + "}");
        paymentPageWeb.selectInstallmentByTenure(secondInstallmentTenure);

        step("User clicks Pay with card button");
        paymentPageWeb.clickOnPaymentButton();

        step("Verify payment successful page is visible for subsequent payment");
        Assertion.assertThat("Payment successful page is visible for subsequent payment", paymentPageWeb.isPaymentSuccessfulVisible(), is(true));

        step("User opens order detail for second insatllment directly via URL for order " + secondPaidOrderId);
        orderListPage.openActiveTicketViaURL(secondPaidOrderId);

        step("User verifies order id and tenor in payment dashboard API follow the payment sequence");
        List<String> expectedSequence = List.of(paidOrderId + ":" + installmentTenure.trim(), secondPaidOrderId + ":" + secondInstallmentTenure.trim());
        Assertion.assertThat("Order id and tenor in payment dashboard match the paid sequence",
                new PaymentApi<>(session).getInstallmentOrderTenorSequence(username, password, List.of(paidOrderId, secondPaidOrderId)), is(expectedSequence));
    }
}
