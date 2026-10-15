package com.tiket.web.dWeb.test.payment;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.apiPage.PaymentApi;
import com.tiket.apiPage.TTDApi;
import com.tiket.model.Environment;
import com.tiket.model.Session;
import com.tiket.pojo.paymentDweb.BCAVASnapInquiryResponse;
import com.tiket.pojo.paymentDweb.BCAVASnapPaymentResponse;
import com.tiket.pojo.paymentDweb.MandiriVASnapInquiryResponse;
import com.tiket.pojo.paymentDweb.MandiriVASnapPaymentResponse;
import com.tiket.pojo.paymentDweb.OrderDetailResponse;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.testbase.Configs;
import com.tiket.web.dWeb.page.payment.PaymentApiHitterPage;
import com.tiket.web.dWeb.page.payment.PaymentPage;
import com.tiket.web.dashboard.page.order.CreateCarRentalOrderPage;
import com.tiket.web.dashboard.page.order.orderCreationPage;
import com.tiket.web.dashboard.page.promo.PromoDashboard;
import com.tiket.web.page.common.home.HomePageWeb;
import com.tiket.web.page.common.home.LoginPageWeb;
import com.tiket.web.page.platform.payment.PaymentPageHelperWeb;

import com.tiket.web.page.platform.payment.PaymentPageWeb;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import javax.security.auth.login.FailedLoginException;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static com.tiket.core.CoreFunctions.*;
import static com.tiket.testdata.TestData.getObjectParams;
import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.Matchers.blankOrNullString;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;

public class PaymentTest extends BaseTest{
    @Author(names = { "Hiten" })
    @Module(name = "PaymentMethod")
    @Tribe(name = "Payment")
    @Vertical(name = "PROP")
    @Test(dataProvider = "verifyInstantAndEwalletPaymentMethodsDataProvider")
    public void verifyInstantAndEwalletPaymentMethods(Map<String, String> map) throws InterruptedException , FailedLoginException {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        PromoDashboard<?> promoDashboard = new PromoDashboard<>(session);
        PaymentPage<?> landingPage = new PaymentPage<>(session);
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

        step("Login into dweb if not login using the given account");
        promoDashboard.maximize();
        loginPage.loginIfNotWithSameEmail(email,passwordDweb,otp,0);

        step("Verify user is logged in and home page is opened");
        homePage.waitForHomePageToOpen();
        Assertion.assertThat("Verify the home page is opened", homePage.isHomePageOpened(), is(true));
        Assertion.assertThat("Verify the user logged in", loginPage.checkIfLoggedIn(), is(true));

        step("Select english language if not in english");
        homePage.selectEnglishLanguageOnly();

        step("Go to the payment page");
        landingPage.goToPaymentPage(orderIdCreated, orderHashCreated);

        step("Verify payment page is loaded or not");
        Assertion.assertThat("Verify the payment page is loaded or not", landingPage.isPaymentPageLoaded(), is(true));

        String orderId = landingPage.getOrderId();
        int beforePrice = Integer.parseInt(landingPage.getFinalPrice());

        step("Click on select payment method");
        landingPage.clickOnSelectPaymentMethod();

        step("Scroll down to the payment Group");
        landingPage.goToPaymentMethodsGroup(paymentGroup);

        step("Select Payment Method");
        landingPage.selectPaymentMethod(paymentMethod);
        Assertion.assertThat("Verify the selected payment", landingPage.getSelectedPayment(), is(paymentMethod));

        step("Fetch the price after payment method is applied");
        int finalPrice = Integer.parseInt(landingPage.getFinalPrice());

        step("Verify the service charge after payment is applied");
        Assertion.assertThat("Verify the service charge", (finalPrice-beforePrice), is(serviceCharge));

        step("Click on Pay button");
        landingPage.clickOnPaymentButton();

        if(!paymentMethod.equals("Sakuku")){
            step("Verify confirmation page");
            Assertion.assertThat("Verify the confimation page is loaded", landingPage.isPaymentConfirmationPageLoaded(), is(true));
            int priceOnConfirmationPage = Integer.parseInt(landingPage.getFinalPrice());
            Assertion.assertThat("Verify the price on confirmation page or on landing page is same or not", priceOnConfirmationPage, is(finalPrice));
        }

        if(!paymentMethod.equals("Indomaret")){
            step("Verify the payment successful page is visible or not");
            Assertion.assertThat("Verify the payment successful page is visible", landingPage.isPaymentSuccessfulVisible(), is(true));

            step("Verify the status from database");
            String query = landingPage.queryString(orderId);
            Map<String,String> paymentTransactionDetails = landingPage.getPaymentTransactionDetails(config, database, collectionName, query);
            String paymentStatus = paymentTransactionDetails.get("paymentStatus");
            String paymentAmount = paymentTransactionDetails.get("paymentAmount");
            Assertion.assertThat("Verify the payment status is paid",paymentStatus , is("PAID"));
            Assertion.assertThat("Verify the paid amount", Double.parseDouble(paymentAmount), is(Double.valueOf(finalPrice)));
        }
    }
    @DataProvider(name = "verifyInstantAndEwalletPaymentMethodsDataProvider")
    public Object[][] verifyInstantAndEwalletPaymentMethodsDataProvider() {return getObjectParams("verifyInstantAndEwalletPaymentMethods");}

    @Author(names = { "Hiten" })
    @Module(name = "PaymentMethod")
    @Tribe(name = "Payment")
    @Vertical(name = "PROP")
    @Test(dataProvider = "verifyTransferPaymentMethodDataProvider")
    public void verifyTransferPaymentMethod(Map<String, String> map) throws InterruptedException , FailedLoginException {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        PromoDashboard<?> promoDashboard = new PromoDashboard<>(session);
        PaymentPage<?> landingPage = new PaymentPage<>(session);
        CreateCarRentalOrderPage<?> carRentalOrderPage = new CreateCarRentalOrderPage<>(session);
        PaymentApiHitterPage<?> paymentHitterApiPage = new PaymentApiHitterPage<>(session);

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

        step("Login as " + email + " via session injection");
        promoDashboard.maximize();
        loginPage.loginWithSpecificAccountViaCookie(email, passwordDweb);

        step("Select english language if not in english");
        homePage.selectEnglishLanguageOnly();

        step("Go to the payment page");
        landingPage.goToPaymentPage(orderIdCreated, orderHashCreated);

        step("Verify payment page is loaded or not");
        Assertion.assertThat("Verify the payment page is loaded or not", landingPage.isPaymentPageLoaded(), is(true));

        String orderId = landingPage.getOrderId();
        int beforePrice = Integer.parseInt(landingPage.getFinalPrice());

        step("Click on select payment method");
        landingPage.clickOnSelectPaymentMethod();

        step("Scroll down to the payment Group");
        landingPage.goToPaymentMethodsGroup(paymentGroup);

        step("Select Payment Method");
        landingPage.selectPaymentMethod(paymentMethod);
        Assertion.assertThat("Verify the selected payment", landingPage.getSelectedPayment(), is(paymentMethod));

        step("Open price history and fetch the unique code");
        landingPage.openPriceSummary();
        Assertion.assertThat("Is unique code is avaialable", landingPage.isUniqueCodeTextDisplayed(), is(true));
        String uniqueCode = landingPage.getUniqueCode();

        step("Close the price history");
        landingPage.closeDrawer();

        step("Fetch the price after payment method is applied");
        int finalPrice = Integer.parseInt(landingPage.getFinalPrice());

        step("Verify the service charge after payment is applied");
        Assertion.assertThat("Verify the service charge", (finalPrice-beforePrice+Integer.parseInt(uniqueCode)), is(serviceCharge));

        step("Click on Pay button");
        landingPage.clickOnPaymentButton();

        step("Verify confirmation page");
        Assertion.assertThat("Verify the confimation page is loaded", landingPage.isPaymentConfirmationPageLoaded(), is(true));
        int priceOnConfirmationPage = Integer.parseInt(landingPage.getFinalPrice());
        Assertion.assertThat("Verify the price on confirmation page or on landing page is same or not", priceOnConfirmationPage, is(finalPrice));

        step("Verify details on database after click on payment button but not hitting the payment api");
        String query = landingPage.queryString(orderId);
        Map<String,String> paymentTransactionDetailsBeforeHittingApi = landingPage.getPaymentTransactionDetails(config, database, collectionName, query);
        String paymentStatusBeforeHittingApi = paymentTransactionDetailsBeforeHittingApi.get("paymentStatus");
        Assertion.assertThat("Verify the payment status is paid",paymentStatusBeforeHittingApi , is("CHECKOUT"));
        String uniqueCodeFromDB = paymentTransactionDetailsBeforeHittingApi.get("uniqueCode");
        Assertion.assertThat("Verify the unique code is same or not", uniqueCode, is(uniqueCodeFromDB));

        if(paymentMethod.equals("MANDIRI TRANSFER")){
            step("Hit the mandiri transfer API to pay the amount");
            paymentHitterApiPage.hitMandiriTransferApi(orderId, finalPrice);

            step("Verify the payment successful page is visible or not");
            Assertion.assertThat("Verify the payment successful page is visible", landingPage.isPaymentSuccessfulVisible(), is(true));

            step("Verify the status from database");
            Map<String,String> paymentTransactionDetails = landingPage.getPaymentTransactionDetails(config, database, collectionName, query);
            String paymentStatus = paymentTransactionDetails.get("paymentStatus");
            String paymentAmount = paymentTransactionDetails.get("paymentAmount");
            Assertion.assertThat("Verify the payment status is paid",paymentStatus , is("PAID"));
            Assertion.assertThat("Verify the paid amount", Double.parseDouble(paymentAmount), is(Double.valueOf(finalPrice)));
        }
    }
    @DataProvider(name = "verifyTransferPaymentMethodDataProvider")
    public Object[][] verifyTransferPaymentMethodDataProvider() {return getObjectParams("verifyTransferPaymentMethod");}

    @Author(names = { "Hiten" })
    @Module(name = "PaymentMethod")
    @Tribe(name = "Payment")
    @Vertical(name = "PROP")
    @Test(dataProvider = "verifyVirtualAccountPaymentMethodDataProvider")
    public void verifyVirtualAccountPaymentMethod(Map<String, String> map) throws InterruptedException , FailedLoginException {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        PromoDashboard<?> promoDashboard = new PromoDashboard<>(session);
        PaymentPage<?> landingPage = new PaymentPage<>(session);
        CreateCarRentalOrderPage<?> carRentalOrderPage = new CreateCarRentalOrderPage<>(session);
        PaymentApiHitterPage<?> paymentHitterApiPage = new PaymentApiHitterPage<>(session);

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
        String bankCode = map.get("bankCode");
        String otp = map.get("otp");

        step("Create Car Rental order via API and fetch the orderID");
        Map<String,String> orderIdAndHash = carRentalOrderPage.createCarRentalProductTillPaymentApi(email, phoneNumber,passwordDweb ,driverOption, location, startDate, pickupTime, type, fullName, nameTitle, rentalDuration);
        String orderIdCreated = orderIdAndHash.get("orderId");
        String orderHashCreated = orderIdAndHash.get("orderHash");

        step("Login into dweb if not login using the given account");
        promoDashboard.maximize();
        loginPage.loginWithSpecificAccountViaCookie(email, passwordDweb);

        step("Verify user is logged in and home page is opened");
        homePage.waitForHomePageToOpen();
        Assertion.assertThat("Verify the home page is opened", homePage.isHomePageOpened(), is(true));
        Assertion.assertThat("Verify the user logged in", loginPage.checkIfLoggedIn(), is(true));

        step("Select english language if not in english");
        homePage.selectEnglishLanguageOnly();

        step("Go to the payment page");
        landingPage.goToPaymentPage(orderIdCreated, orderHashCreated);

        step("Verify payment page is loaded or not");
        Assertion.assertThat("Verify the payment page is loaded or not", landingPage.isPaymentPageLoaded(), is(true));

        String orderId = landingPage.getOrderId();
        int beforePrice = Integer.parseInt(landingPage.getFinalPrice());

        step("Click on select payment method");
        landingPage.clickOnSelectPaymentMethod();

        step("Scroll down to the payment Group");
        landingPage.goToPaymentMethodsGroup(paymentGroup);

        step("Select Payment Method");
        landingPage.selectPaymentMethod(paymentMethod);
        Assertion.assertThat("Verify the selected payment", landingPage.getSelectedPayment(), is(paymentMethod));

        step("Fetch the price after payment method is applied");
        int finalPrice = Integer.parseInt(landingPage.getFinalPrice());

        step("Verify the service charge after payment is applied");
        Assertion.assertThat("Verify the service charge", (finalPrice-beforePrice), is(serviceCharge));

        step("Click on Pay button");
        landingPage.clickOnPaymentButton();

        step("Verify confirmation page");
        Assertion.assertThat("Verify the confimation page is loaded", landingPage.isPaymentConfirmationPageLoaded(), is(true));
        int priceOnConfirmationPage = Integer.parseInt(landingPage.getFinalPrice());
        Assertion.assertThat("Verify the price on confirmation page or on landing page is same or not", priceOnConfirmationPage, is(finalPrice));

        step("Verify details on database after click on payment button but not hitting the payment api");
        String query = landingPage.queryString(orderId);
        Map<String,String> paymentTransactionDetailsBeforeHittingApi = landingPage.getPaymentTransactionDetails(config, database, collectionName, query);
        String paymentStatusBeforeHittingApi = paymentTransactionDetailsBeforeHittingApi.get("paymentStatus");
        Assertion.assertThat("Verify the payment status is paid",paymentStatusBeforeHittingApi , is("CHECKOUT"));

        if(Configs.ENVIRONMENT == Environment.GK){
            step("Hit the mandiri transfer API to pay the amount");
            paymentHitterApiPage.hitVAStagingGenerateTokenAndHitPayment(bankCode, landingPage.getFinalPrice(), orderId);

            step("Verify the payment successful page is visible or not");
            Assertion.assertThat("Verify the payment successful page is visible", landingPage.isPaymentSuccessfulVisible(), is(true));

            step("Verify the status from database");
            Map<String,String> paymentTransactionDetails = landingPage.getPaymentTransactionDetails(config, database, collectionName, query);
            String paymentStatus = paymentTransactionDetails.get("paymentStatus");
            String paymentAmount = paymentTransactionDetails.get("paymentAmount");
            Assertion.assertThat("Verify the payment status is paid",paymentStatus , is("PAID"));
            Assertion.assertThat("Verify the paid amount", Double.parseDouble(paymentAmount), is(Double.valueOf(finalPrice)));
        }
    }
    @DataProvider(name = "verifyVirtualAccountPaymentMethodDataProvider")
    public Object[][] verifyVirtualAccountPaymentMethodDataProvider() {return getObjectParams("verifyVirtualAccountPaymentMethod");}

    @Author(names = { "Hiten" })
    @Module(name = "PaymentMethod")
    @Tribe(name = "Payment")
    @Vertical(name = "PROP")
    @Test(dataProvider = "verifyPaymentMethodUsingCardDataProvider")
    public void verifyPaymentMethodUsingCard(Map<String, String> map) throws InterruptedException , FailedLoginException {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        PromoDashboard<?> promoDashboard = new PromoDashboard<>(session);
        PaymentPage<?> landingPage = new PaymentPage<>(session);
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
        String cardNumber = map.get("cardNumber");
        String cardMonth = map.get("cardMonth");
        String cvv = map.get("cvv");
        String cardHolderName = map.get("cardHolderName");
        String TwoCTwoP = map.get("TwoCTwoP");
        String cardOtp = map.get("cardOtp");

        step("Create Car Rental order via API and fetch the orderID");
        Map<String,String> orderIdAndHash = carRentalOrderPage.createCarRentalProductTillPaymentApi(email, phoneNumber,passwordDweb ,driverOption, location, startDate, pickupTime, type, fullName, nameTitle, rentalDuration);
        String orderIdCreated = orderIdAndHash.get("orderId");
        String orderHashCreated = orderIdAndHash.get("orderHash");

        step("Login into dweb if not login using the given account");
        promoDashboard.maximize();
        loginPage.loginWithSpecificAccountViaCookie(email, passwordDweb);

        step("Verify user is logged in and home page is opened");
        homePage.waitForHomePageToOpen();
        Assertion.assertThat("Verify the home page is opened", homePage.isHomePageOpened(), is(true));
        Assertion.assertThat("Verify the user logged in", loginPage.checkIfLoggedIn(), is(true));

        step("Select english language if not in english");
        homePage.selectEnglishLanguageOnly();

        step("Go to the payment page");
        landingPage.goToPaymentPage(orderIdCreated, orderHashCreated);

        step("Verify payment page is loaded or not");
        Assertion.assertThat("Verify the payment page is loaded or not", landingPage.isPaymentPageLoaded(), is(true));

        String orderId = landingPage.getOrderId();
        int beforePrice = Integer.parseInt(landingPage.getFinalPrice());

        step("Click on select payment method");
        landingPage.clickOnSelectPaymentMethod();

        step("Scroll down to the payment Group");
        landingPage.goToPaymentMethodsGroup(paymentGroup);

        step("Select Payment Method");
        landingPage.selectPaymentMethod(paymentMethod);
        Assertion.assertThat("Verify the selected payment", landingPage.getSelectedPayment(), is("Credit Card"));

        step("Fetch the price after payment method is applied");
        int finalPrice = Integer.parseInt(landingPage.getFinalPrice());

        step("Verify the service charge after payment is applied");
        Assertion.assertThat("Verify the service charge", (finalPrice-beforePrice), is(serviceCharge));

        step("Input the card details");
        landingPage.inputCardNumber(cardNumber);
        landingPage.inputCardMonth(cardMonth);
        landingPage.inputCardCvv(cvv);
        landingPage.inputCardHolderName(cardHolderName);

        step("Open price history");
        landingPage.openPriceSummary();
        Assertion.assertThat("Is transaction fees is avaialable", landingPage.isTransactionFeesDisplayed(), is(true));

        step("Close the price history");
        landingPage.closeDrawer();

        step("Click on Pay button");
        landingPage.clickOnPaymentButton();

        if(TwoCTwoP.equals("true")){
            step("Verify 2c2p page is loaded");
            landingPage.is2c2pConfirmationPageLoaded();

            step("Input otp on 2c2p page");
            landingPage.inputOtpOn2C2PPage(cardOtp);

            String amountOn2c2p = landingPage.getPaymentAmountOn2c2p();
            Assertion.assertThat("Verify the amount on landing page and 2c2p pag is same", Integer.parseInt(amountOn2c2p), is(finalPrice));

            step("Click on submit button");
            landingPage.clickOnSubmitButton();
        }

        step("Verify the payment successful page is visible or not");
        Assertion.assertThat("Verify the payment successful page is visible", landingPage.isPaymentSuccessfulVisible(), is(true));

        step("Verify the status from database");
        String query = landingPage.queryString(orderId);
        Map<String,String> paymentTransactionDetails = landingPage.getPaymentTransactionDetails(config, database, collectionName, query);
        String paymentStatus = paymentTransactionDetails.get("paymentStatus");
        String paymentAmount = paymentTransactionDetails.get("paymentAmount");
        Assertion.assertThat("Verify the payment status is paid",paymentStatus , is("PAID"));
        Assertion.assertThat("Verify the paid amount", Double.parseDouble(paymentAmount), is(Double.valueOf(finalPrice)));
    }
    @DataProvider(name = "verifyPaymentMethodUsingCardDataProvider")
    public Object[][] verifyPaymentMethodUsingCardDataProvider() {return getObjectParams("verifyPaymentMethodUsingCard");}

    @Author(names = { "Hiten" })
    @Module(name = "PaymentMethod")
    @Tribe(name = "Payment")
    @Vertical(name = "PROP")
    @Test(dataProvider = "verifyPaymentMethodKlikBCADataProvider")
    public void verifyPaymentMethodKlikBCA(Map<String, String> map) throws InterruptedException , FailedLoginException {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PaymentPage<?> landingPage = new PaymentPage<>(session);
        PaymentPageHelperWeb<?> paymentHelperPage = PaymentPageHelperWeb.getInstance(session);
        CreateCarRentalOrderPage<?> carRentalOrderPage = new CreateCarRentalOrderPage<>(session);
        PaymentApiHitterPage<?> paymentHitterApiPage = new PaymentApiHitterPage<>(session);

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
        Assertion.assertThat("Is transaction fees is avaialable", landingPage.isTransactionFeesDisplayed(), is(true));

        step("Close the price history");
        landingPage.closeDrawer();

        step("Click on Pay button");
        landingPage.clickOnPaymentButton();

        step("Verify confirmation page");
        Assertion.assertThat("Verify the confimation page is loaded", landingPage.isPaymentConfirmationPageLoaded(), is(true));
        int priceOnConfirmationPage = Integer.parseInt(landingPage.getFinalPrice());
        Assertion.assertThat("Verify the price on confirmation page or on landing page is same or not", priceOnConfirmationPage, is(finalPrice));

        step("Hit the Payment API to complete the payment");
        paymentHitterApiPage.hitKlikBCAApi(orderIdCreated, String.valueOf(finalPrice));

        step("Verify the payment successful page is visible or not");
        Assertion.assertThat("Verify the payment successful page is visible", landingPage.isPaymentSuccessfulVisible(), is(true));

        step("Verify the status from database");
        String query = landingPage.queryString(orderIdCreated);
        Map<String,String> paymentTransactionDetails = landingPage.getPaymentTransactionDetails(config, database, collectionName, query);
        String paymentStatus = paymentTransactionDetails.get("paymentStatus");
        String paymentAmount = paymentTransactionDetails.get("paymentAmount");
        Assertion.assertThat("Verify the payment status is paid",paymentStatus , is("PAID"));
        Assertion.assertThat("Verify the paid amount", Double.parseDouble(paymentAmount), is(Double.valueOf(finalPrice)));
    }
    @DataProvider(name = "verifyPaymentMethodKlikBCADataProvider")
    public Object[][] verifyPaymentMethodKlikBCADataProvider() {return getObjectParams("verifyPaymentMethodKlikBCA");}

    @Author(names = { "Hiten" })
    @Module(name = "PaymentMethod")
    @Tribe(name = "Payment")
    @Vertical(name = "PROP")
    @Test(dataProvider = "verifyPaymentMethodGopayAndShopeepayDataProvider")
    public void verifyPaymentMethodGopayAndShopeepay(Map<String, String> map) throws InterruptedException , FailedLoginException {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PaymentPage<?> landingPage = new PaymentPage<>(session);
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

        step("Open price history");
        landingPage.openPriceSummary();
        Assertion.assertThat("Is transaction fees is avaialable", landingPage.isTransactionFeesDisplayed(), is(true));

        step("Close the price history");
        landingPage.closeDrawer();

        step("Click on Pay button");
        landingPage.clickOnPaymentButton();

        step("Verify confirmation page");
        Assertion.assertThat("Verify the confimation page is loaded", landingPage.isPaymentConfirmationPageLoaded(), is(true));
        int priceOnConfirmationPage = Integer.parseInt(landingPage.getFinalPrice());
        Assertion.assertThat("Verify the price on confirmation page or on landing page is same or not", priceOnConfirmationPage, is(finalPrice));

        step("Verify the status from database");
        String query = landingPage.queryString(orderIdCreated);
        Map<String,String> paymentTransactionDetails = landingPage.getPaymentTransactionDetails(config, database, collectionName, query);
        String paymentStatus = paymentTransactionDetails.get("paymentStatus");
        String paymentAmount = paymentTransactionDetails.get("paymentAmount");
        Assertion.assertThat("Verify the payment status is paid",paymentStatus , is("CHECKOUT"));
        Assertion.assertThat("Verify the paid amount", Double.parseDouble(paymentAmount), is(Double.valueOf(finalPrice)));
    }
    @DataProvider(name = "verifyPaymentMethodGopayAndShopeepayDataProvider")
    public Object[][] verifyPaymentMethodGopayAndShopeepayDataProvider() {return getObjectParams("verifyPaymentMethodGopayAndShopeepay");}

    @Author(names = {"Hiten","Shubham Bansal"})
    @Module(name = "PaymentMethod")
    @Tribe(name = "Payment")
    @Vertical(name = "PROP")
    @Test
    public void verifyPaymentMethodBCAVirtualAccount() throws InterruptedException, FailedLoginException {
        Map<String, String> map = getParam("verifyPaymentMethodBCAVirtualAccount");
        BaseTest.paramsThreadLocal.set(map);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PaymentPage<?> landingPage = new PaymentPage<>(session);
        PaymentPageHelperWeb<?> paymentHelperPage = PaymentPageHelperWeb.getInstance(session);
        CreateCarRentalOrderPage<?> carRentalOrderPage = new CreateCarRentalOrderPage<>(session);
        PaymentApi<?> paymentApi = new PaymentApi<>(session);

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

        step("Click continue to payment button if exist");
        landingPage.clickContinueToPayment();

        step("Verify confirmation page");
        Assertion.assertThat("Verify the confirmation page is loaded", landingPage.isPaymentConfirmationPageLoaded(), is(true));

      step("Get order details");
      String orderId = orderIdCreated;
      OrderDetailResponse orderDetail = paymentApi.getOrderDetails(orderId);
      String customerNo = orderDetail.data.contact.getCustomerNo();
      long totalAmount = orderDetail.data.getTotalAmountAsLong();
      String virtualAccountNo = orderDetail.data.getVirtualAccountNo();

      step("Get BCA Snap access token via API");
      String bcaSnapAccessToken = paymentApi.getBCAVASnapAccessToken();
      Assertion.assertThat("BCA Snap access token is retrieved", bcaSnapAccessToken, not(blankOrNullString()));

      step("Hit BCA Snap VA inquiry API");
      BCAVASnapInquiryResponse bcaSnapInquiryResponse = paymentApi.hitBCAVAInternalInquiry(bcaSnapAccessToken, customerNo, virtualAccountNo, orderId);
      Assertion.assertThat("BCA Snap inquiry is successful", bcaSnapInquiryResponse.responseCode, is("2002400"));
      Assertion.assertThat("BCA Snap inquiry status is success",
        bcaSnapInquiryResponse.virtualAccountData.inquiryStatus, is("00"));

      step("Hit BCA Snap VA payment API");
      BCAVASnapPaymentResponse bcaSnapPaymentResponse = paymentApi.hitBCAVAInternalPayment(
        bcaSnapAccessToken, customerNo, virtualAccountNo, totalAmount, bcaSnapInquiryResponse, orderId);
      Assertion.assertThat("BCA Snap payment is successful",
        bcaSnapPaymentResponse.responseMessage.toLowerCase(), containsString("success"));

        step("Verify the payment successful page is visible or not");
        Assertion.assertThat("Verify the payment successful page is visible", landingPage.isPaymentSuccessfulVisible(), is(true));

        step("Verify the status from database");
        String query = landingPage.queryString(orderId);
        Map<String,String> paymentTransactionDetails = landingPage.getPaymentTransactionDetails(config, database, collectionName, query);
        String paymentStatus = paymentTransactionDetails.get("paymentStatus");
        String paymentAmount = paymentTransactionDetails.get("paymentAmount");
        Assertion.assertThat("Verify the payment status is paid",paymentStatus , is("PAID"));
        Assertion.assertThat("Verify the paid amount", Double.parseDouble(paymentAmount), is(Double.valueOf(totalAmount)));
    }

  @Author(names = {"Hiten"})
  @Module(name = "PaymentMethod")
  @Tribe(name = "Payment")
  @Vertical(name = "PROP")
  @Test
  public void verifyPaymentMethodBCAVirtualAccountTTD() throws InterruptedException, FailedLoginException, ExecutionException {
    Map<String, String> map = getParam("verifyPaymentMethodBCAVirtualAccountTTD");
    BaseTest.paramsThreadLocal.set(map);
    Session<?> session = BaseTest.sessionThreadLocal.get();
    PaymentPage<?> landingPage = new PaymentPage<>(session);
    PaymentPageHelperWeb<?> paymentHelperPage = PaymentPageHelperWeb.getInstance(session);
    PaymentApi<?> paymentApi = new PaymentApi<>(session);
    TTDApi<?> ttdApi = new TTDApi<>(session);

    String email = map.get("email");
    String passwordDweb = map.get("passwordDweb");
    String username = map.get("email");
    String password = map.get("passwordDweb");
    String productUrlName = map.get("productUrlName");
    String config = map.get("config");
    String database = map.get("database");
    String collectionName = map.get("collectionName");
    String paymentGroup = map.get("paymentGroup");
    String paymentMethod = map.get("paymentMethod");
    int serviceCharge = Integer.parseInt(map.get("serviceCharge"));

    String[] salutationList = {"Mr", "Mrs", "Ms"};
    String contactFullName = randomName() + " autorder";
    String contactSalutation = getRandomObject(salutationList);
    String guestFullName = randomName() + " autguest";
    String guestEmailAddress = randomEmail();
    String guestSalutation = salutationList[0];

    step("Create TTD order via API");
    Map<String, String> ttdOrderData = ttdApi.createWaitingPaymentOrder(
        username, password, "IDR", productUrlName, true,
        contactFullName, contactSalutation, guestFullName, guestEmailAddress, guestSalutation);
    String orderIdCreated = ttdOrderData.get("orderId");
    String orderHashCreated = ttdOrderData.get("orderHash");

    step("Login and select payment method");
    int finalPrice = paymentHelperPage.loginViaCookieAndSelectPaymentMethod(email, passwordDweb, orderIdCreated, orderHashCreated, paymentGroup, paymentMethod, serviceCharge);

//    step("Open price history");
//    landingPage.openPriceSummary();
//    Assertion.assertThat("Is transaction fees is avaialable", landingPage.isTransactionFeesDisplayed(), is(true));
//
//    step("Close the price history");
//    landingPage.closeDrawer();

    step("Click on Pay button");
    landingPage.clickOnPaymentButton();

    step("Click continue to payment button if exist");
    landingPage.clickContinueToPayment();

    step("Verify confirmation page");
    Assertion.assertThat("Verify the confimation page is loaded", landingPage.isPaymentConfirmationPageLoaded(), is(true));

    step("Get order details");
    String orderId = orderIdCreated;
    OrderDetailResponse orderDetail = paymentApi.getOrderDetails(orderId);
    String customerNo = orderDetail.data.contact.getCustomerNo();
    long totalAmount = orderDetail.data.getTotalAmountAsLong();
    String virtualAccountNo = orderDetail.data.getVirtualAccountNo();

    step("Get BCA Snap access token via API");
    String bcaSnapAccessToken = paymentApi.getBCAVASnapAccessToken();
    Assertion.assertThat("BCA Snap access token is retrieved", bcaSnapAccessToken, not(blankOrNullString()));

    step("Hit BCA Snap VA inquiry API");
    BCAVASnapInquiryResponse bcaSnapInquiryResponse = paymentApi.hitBCAVAInternalInquiry(bcaSnapAccessToken, customerNo, virtualAccountNo, orderId);
    Assertion.assertThat("BCA Snap inquiry is successful", bcaSnapInquiryResponse.responseCode, is("2002400"));
    Assertion.assertThat("BCA Snap inquiry status is success",
      bcaSnapInquiryResponse.virtualAccountData.inquiryStatus, is("00"));

    step("Hit BCA Snap VA payment API");
    BCAVASnapPaymentResponse bcaSnapPaymentResponse = paymentApi.hitBCAVAInternalPayment(
      bcaSnapAccessToken, customerNo, virtualAccountNo, totalAmount, bcaSnapInquiryResponse, orderId);
    Assertion.assertThat("BCA Snap payment is successful",
      bcaSnapPaymentResponse.responseMessage.toLowerCase(), containsString("success"));

    step("Verify the payment successful page is visible or not");
    Assertion.assertThat("Verify the payment successful page is visible", landingPage.isPaymentSuccessfulVisible(), is(true));

    step("Verify the status from database");
    String query = landingPage.queryString(orderId);
    Map<String,String> paymentTransactionDetails = landingPage.getPaymentTransactionDetails(config, database, collectionName, query);
    String paymentStatus = paymentTransactionDetails.get("paymentStatus");
    String paymentAmount = paymentTransactionDetails.get("paymentAmount");
    Assertion.assertThat("Verify the payment status is paid",paymentStatus , is("PAID"));
    Assertion.assertThat("Verify the paid amount", Double.parseDouble(paymentAmount), is(Double.valueOf(totalAmount)));
  }

  @Author(names = {"Alfred"})
  @Module(name = "PaymentMethod")
  @Tribe(name = "Payment")
  @Vertical(name = "PROP")
  @Test
  public void verifyPaymentMethodMandiriVirtualAccountWeb() throws InterruptedException, FailedLoginException {
    Map<String, String> map = getParam("verifyPaymentMethodMandiriVirtualAccountWeb");
    BaseTest.paramsThreadLocal.set(map);
    Session<?> session = BaseTest.sessionThreadLocal.get();
    PaymentPage<?> landingPage = new PaymentPage<>(session);
    PaymentPageHelperWeb<?> paymentHelperPage = PaymentPageHelperWeb.getInstance(session);
    CreateCarRentalOrderPage<?> carRentalOrderPage = new CreateCarRentalOrderPage<>(session);
    PaymentApi<?> paymentApi = new PaymentApi<>(session);

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
    Map<String,String> orderIdAndHash = carRentalOrderPage.createCarRentalProductTillPaymentApi(email, phoneNumber,passwordDweb ,driverOption, location, startDate, pickupTime, type, fullName, nameTitle, rentalDuration);
    String orderIdCreated = orderIdAndHash.get("orderId");
    String orderHashCreated = orderIdAndHash.get("orderHash");

    step("Login and select payment method");
    int finalPrice = paymentHelperPage.loginViaCookieAndSelectPaymentMethod(email, passwordDweb, orderIdCreated, orderHashCreated, paymentGroup, paymentMethod, serviceCharge);

    step("Open price history");
    landingPage.openPriceSummary();
    Assertion.assertThat("Is transaction fees is avaialable", landingPage.isTransactionFeesDisplayed(), is(true));

    step("Close the price history");
    landingPage.closeDrawer();

    step("Click on Pay button");
    landingPage.clickOnPaymentButton();

    step("Click continue to payment button if exist");
    landingPage.clickContinueToPayment();

    step("Verify confirmation page");
    Assertion.assertThat("Verify the confimation page is loaded", landingPage.isPaymentConfirmationPageLoaded(), is(true));

    step("Get order details");
    String orderId = orderIdCreated;
    OrderDetailResponse orderDetail = paymentApi.getOrderDetails(orderId);
    long totalAmount = orderDetail.data.getTotalAmountAsLong();
    String virtualAccountNo = orderDetail.data.getVirtualAccountNo();
    String customerNo = virtualAccountNo.substring(5);

    step("Get Mandiri Snap access token via API");
    String mandiriSnapAccessToken = paymentApi.getMandiriVASnapAccessToken();
    Assertion.assertThat("Mandiri Snap access token is retrieved", mandiriSnapAccessToken, not(blankOrNullString()));

    step("Hit Mandiri Snap VA inquiry API");
    MandiriVASnapInquiryResponse
      mandiriSnapInquiryResponse = paymentApi.hitMandiriVAInternalInquiry(mandiriSnapAccessToken, customerNo, virtualAccountNo, orderId);
    Assertion.assertThat("Mandiri Snap inquiry is successful", mandiriSnapInquiryResponse.responseCode, is("2002400"));
    Assertion.assertThat("Mandiri Snap inquiry status is success",
      mandiriSnapInquiryResponse.virtualAccountData.inquiryStatus, is("00"));

    step("Hit Mandiri Snap VA payment API");
    MandiriVASnapPaymentResponse mandiriVASnapPaymentResponse = paymentApi.hitMandiriVAInternalPayment(
      mandiriSnapAccessToken, customerNo, virtualAccountNo, totalAmount, mandiriSnapInquiryResponse, orderId);
    Assertion.assertThat("BCA Snap payment is successful",
      mandiriVASnapPaymentResponse.responseMessage.toLowerCase(), containsString("success"));

    step("Verify the payment successful page is visible or not");
    Assertion.assertThat("Verify the payment successful page is visible", landingPage.isPaymentSuccessfulVisible(), is(true));

    step("Verify the status from database");
    String query = landingPage.queryString(orderId);
    Map<String,String> paymentTransactionDetails = landingPage.getPaymentTransactionDetails(config, database, collectionName, query);
    String paymentStatus = paymentTransactionDetails.get("paymentStatus");
    String paymentAmount = paymentTransactionDetails.get("paymentAmount");
    Assertion.assertThat("Verify the payment status is paid",paymentStatus , is("PAID"));
    Assertion.assertThat("Verify the paid amount", Double.parseDouble(paymentAmount), is(Double.valueOf(totalAmount)));
  }

  @Author(names = {"Alfred"})
  @Module(name = "PaymentMethod")
  @Tribe(name = "Payment")
  @Vertical(name = "PROP")
  @Test
  public void verifyPaymentMethodBNIVirtualAccountWeb() throws InterruptedException, FailedLoginException {
    Map<String, String> map = getParam("verifyPaymentMethodBNIVirtualAccountWeb");
    BaseTest.paramsThreadLocal.set(map);
    Session<?> session = BaseTest.sessionThreadLocal.get();
    PaymentPage<?> landingPage = new PaymentPage<>(session);
    PaymentPageHelperWeb<?> paymentHelperPage = PaymentPageHelperWeb.getInstance(session);
    CreateCarRentalOrderPage<?> carRentalOrderPage = new CreateCarRentalOrderPage<>(session);
    PaymentApi<?> paymentApi = new PaymentApi<>(session);

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
    Map<String,String> orderIdAndHash = carRentalOrderPage.createCarRentalProductTillPaymentApi(email, phoneNumber,passwordDweb ,driverOption, location, startDate, pickupTime, type, fullName, nameTitle, rentalDuration);
    String orderIdCreated = orderIdAndHash.get("orderId");
    String orderHashCreated = orderIdAndHash.get("orderHash");

    step("Login and select payment method");
    int finalPrice = paymentHelperPage.loginViaCookieAndSelectPaymentMethod(email, passwordDweb, orderIdCreated, orderHashCreated, paymentGroup, paymentMethod, serviceCharge);

    step("Open price history");
    landingPage.openPriceSummary();
    Assertion.assertThat("Is transaction fees is avaialable", landingPage.isTransactionFeesDisplayed(), is(true));

    step("Close the price history");
    landingPage.closeDrawer();

    step("Click on Pay button");
    landingPage.clickOnPaymentButton();

    step("Click continue to payment button if exist");
    landingPage.clickContinueToPayment();

    step("Verify confirmation page");
    Assertion.assertThat("Verify the confimation page is loaded", landingPage.isPaymentConfirmationPageLoaded(), is(true));

    step("Get order details");
    String orderId = orderIdCreated;
    OrderDetailResponse orderDetail = paymentApi.getOrderDetails(orderId);
    String customerNo = orderDetail.data.contact.getCustomerNo();
    long totalAmount = orderDetail.data.getTotalAmountAsLong();
    String virtualAccountNo = orderDetail.data.getVirtualAccountNo();

    step("Hit BNI Snap VA Paid Simulator");
    boolean isBniSimulatorSuccess = landingPage.payBniSnapVaSimulator(virtualAccountNo, totalAmount);
    Assertion.assertThat("BNI Snap VA simulator payment is successful", isBniSimulatorSuccess, is(true));

    step("Verify the status from database");
    String query = landingPage.queryString(orderId);
    Map<String,String> paymentTransactionDetails = landingPage.getPaymentTransactionDetails(config, database, collectionName, query);
    String paymentStatus = paymentTransactionDetails.get("paymentStatus");
    String paymentAmount = paymentTransactionDetails.get("paymentAmount");
    Assertion.assertThat("Verify the payment status is paid", paymentStatus, is("PAID"));
    Assertion.assertThat("Verify the paid amount", Double.parseDouble(paymentAmount), is(Double.valueOf(totalAmount)));
  }

  @Author(names = {"Alfred"})
  @Module(name = "PaymentMethod")
  @Tribe(name = "Payment")
  @Vertical(name = "PROP")
  @Test
  public void verifyPaymentMethodBRIVirtualAccountWeb() throws InterruptedException, FailedLoginException {
    Map<String, String> map = getParam("verifyPaymentMethodBRIVirtualAccountWeb");
    BaseTest.paramsThreadLocal.set(map);
    Session<?> session = BaseTest.sessionThreadLocal.get();
    PaymentPage<?> landingPage = new PaymentPage<>(session);
    PaymentPageHelperWeb<?> paymentHelperPage = PaymentPageHelperWeb.getInstance(session);
    CreateCarRentalOrderPage<?> carRentalOrderPage = new CreateCarRentalOrderPage<>(session);
    PaymentApi<?> paymentApi = new PaymentApi<>(session);

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
    Map<String,String> orderIdAndHash = carRentalOrderPage.createCarRentalProductTillPaymentApi(email, phoneNumber,passwordDweb ,driverOption, location, startDate, pickupTime, type, fullName, nameTitle, rentalDuration);
    String orderIdCreated = orderIdAndHash.get("orderId");
    String orderHashCreated = orderIdAndHash.get("orderHash");

    step("Login and select payment method");
    int finalPrice = paymentHelperPage.loginViaCookieAndSelectPaymentMethod(email, passwordDweb, orderIdCreated, orderHashCreated, paymentGroup, paymentMethod, serviceCharge);

    step("Open price history");
    landingPage.openPriceSummary();
    Assertion.assertThat("Is transaction fees is avaialable", landingPage.isTransactionFeesDisplayed(), is(true));

    step("Close the price history");
    landingPage.closeDrawer();

    step("Click on Pay button");
    landingPage.clickOnPaymentButton();

    step("Click continue to payment button if exist");
    landingPage.clickContinueToPayment();

    step("Verify confirmation page");
    Assertion.assertThat("Verify the confimation page is loaded", landingPage.isPaymentConfirmationPageLoaded(), is(true));

    step("Get order details");
    String orderId = orderIdCreated;
    OrderDetailResponse orderDetail = paymentApi.getOrderDetails(orderId);
    long totalAmount = orderDetail.data.getTotalAmountAsLong();
    String virtualAccountNo = orderDetail.data.getVirtualAccountNo();

    step("Hit Nicepay BRI Snap VA Paid Simulator");
    boolean isBriSimulatorSuccess = paymentApi.hitNicepayBriSnapVaSimulator("BRIN", orderId, totalAmount, virtualAccountNo);
    Assertion.assertThat("Nicepay BRI VA simulator payment is successful", isBriSimulatorSuccess, is(true));

    step("Verify the status from database");
    String query = landingPage.queryString(orderId);
    Map<String,String> paymentTransactionDetails = landingPage.getPaymentTransactionDetails(config, database, collectionName, query);
    String paymentStatus = paymentTransactionDetails.get("paymentStatus");
    String paymentAmount = paymentTransactionDetails.get("paymentAmount");
    Assertion.assertThat("Verify the payment status is paid", paymentStatus, is("PAID"));
    Assertion.assertThat("Verify the paid amount", Double.parseDouble(paymentAmount), is(Double.valueOf(totalAmount)));
  }

  @Author(names = {"Alfred"})
  @Module(name = "PaymentMethod")
  @Tribe(name = "Payment")
  @Vertical(name = "PROP")
  @Test
  public void verifyPaymentMethodBSIVirtualAccountWeb() throws InterruptedException, FailedLoginException {
    Map<String, String> map = getParam("verifyPaymentMethodBSIVirtualAccountWeb");
    BaseTest.paramsThreadLocal.set(map);
    Session<?> session = BaseTest.sessionThreadLocal.get();
    PaymentPage<?> landingPage = new PaymentPage<>(session);
    PaymentPageHelperWeb<?> paymentHelperPage = PaymentPageHelperWeb.getInstance(session);
    CreateCarRentalOrderPage<?> carRentalOrderPage = new CreateCarRentalOrderPage<>(session);
    PaymentApi<?> paymentApi = new PaymentApi<>(session);

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
    Map<String,String> orderIdAndHash = carRentalOrderPage.createCarRentalProductTillPaymentApi(email, phoneNumber,passwordDweb ,driverOption, location, startDate, pickupTime, type, fullName, nameTitle, rentalDuration);
    String orderIdCreated = orderIdAndHash.get("orderId");
    String orderHashCreated = orderIdAndHash.get("orderHash");

    step("Login and select payment method");
    int finalPrice = paymentHelperPage.loginViaCookieAndSelectPaymentMethod(email, passwordDweb, orderIdCreated, orderHashCreated, paymentGroup, paymentMethod, serviceCharge);

    step("Open price history");
    landingPage.openPriceSummary();
//    Assertion.assertThat("Is transaction fees is avaialable", landingPage.isTransactionFeesDisplayed(), is(true));

    step("Close the price history");
    landingPage.closeDrawer();

    step("Click on Pay button");
    landingPage.clickOnPaymentButton();

    step("Click continue to payment button if exist");
    landingPage.clickContinueToPayment();

    step("Verify confirmation page");
    Assertion.assertThat("Verify the confimation page is loaded", landingPage.isPaymentConfirmationPageLoaded(), is(true));

    step("Get order details");
    String orderId = orderIdCreated;
    OrderDetailResponse orderDetail = paymentApi.getOrderDetails(orderId);
    String customerNo = orderDetail.data.contact.getCustomerNo();
    long totalAmount = orderDetail.data.getTotalAmountAsLong();
    String virtualAccountNo = orderDetail.data.getVirtualAccountNo();

    step("Hit BSI Snap VA Paid Simulator");
    boolean isBsiSimulatorSuccess = landingPage.payBsiSnapVaSimulator(virtualAccountNo, totalAmount);
    Assertion.assertThat("BSI Snap VA simulator payment is successful", isBsiSimulatorSuccess, is(true));

    step("Verify the status from database");
    String query = landingPage.queryString(orderId);
    Map<String,String> paymentTransactionDetails = landingPage.getPaymentTransactionDetails(config, database, collectionName, query);
    String paymentStatus = paymentTransactionDetails.get("paymentStatus");
    String paymentAmount = paymentTransactionDetails.get("paymentAmount");
    Assertion.assertThat("Verify the payment status is paid", paymentStatus, is("PAID"));
    Assertion.assertThat("Verify the paid amount", Double.parseDouble(paymentAmount), is(Double.valueOf(totalAmount)));
  }

  @Author(names = {"Alfred"})
  @Module(name = "PaymentMethod")
  @Tribe(name = "Payment")
  @Vertical(name = "PROP")
  @Test
  public void verifyPaymentMethodPermataVirtualAccountWeb() throws InterruptedException, FailedLoginException {
    Map<String, String> map = getParam("verifyPaymentMethodPermataVirtualAccountWeb");
    BaseTest.paramsThreadLocal.set(map);
    Session<?> session = BaseTest.sessionThreadLocal.get();
    PaymentPage<?> landingPage = new PaymentPage<>(session);
    PaymentPageHelperWeb<?> paymentHelperPage = PaymentPageHelperWeb.getInstance(session);
    CreateCarRentalOrderPage<?> carRentalOrderPage = new CreateCarRentalOrderPage<>(session);
    PaymentApi<?> paymentApi = new PaymentApi<>(session);

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
    Map<String,String> orderIdAndHash = carRentalOrderPage.createCarRentalProductTillPaymentApi(email, phoneNumber,passwordDweb ,driverOption, location, startDate, pickupTime, type, fullName, nameTitle, rentalDuration);
    String orderIdCreated = orderIdAndHash.get("orderId");
    String orderHashCreated = orderIdAndHash.get("orderHash");

    step("Login and select payment method");
    int finalPrice = paymentHelperPage.loginViaCookieAndSelectPaymentMethod(email, passwordDweb, orderIdCreated, orderHashCreated, paymentGroup, paymentMethod, serviceCharge);

    step("Open price history");
    landingPage.openPriceSummary();
    //    Assertion.assertThat("Is transaction fees is avaialable", landingPage.isTransactionFeesDisplayed(), is(true));

    step("Close the price history");
    landingPage.closeDrawer();

    step("Click on Pay button");
    landingPage.clickOnPaymentButton();

    step("Click continue to payment button if exist");
    landingPage.clickContinueToPayment();

    step("Verify confirmation page");
    Assertion.assertThat("Verify the confimation page is loaded", landingPage.isPaymentConfirmationPageLoaded(), is(true));

    step("Get order details");
    String orderId = orderIdCreated;
    OrderDetailResponse orderDetail = paymentApi.getOrderDetails(orderId);
    String customerNo = orderDetail.data.contact.getCustomerNo();
    long totalAmount = orderDetail.data.getTotalAmountAsLong();
    String virtualAccountNo = orderDetail.data.getVirtualAccountNo();

    step("Hit Permata Snap VA Paid Simulator");
    boolean isPermataSimulatorSuccess = landingPage.payPermataSnapVaSimulator(virtualAccountNo, totalAmount);
    Assertion.assertThat("Permata Snap VA simulator payment is successful", isPermataSimulatorSuccess, is(true));

    step("Verify the status from database");
    String query = landingPage.queryString(orderId);
    Map<String,String> paymentTransactionDetails = landingPage.getPaymentTransactionDetails(config, database, collectionName, query);
    String paymentStatus = paymentTransactionDetails.get("paymentStatus");
    String paymentAmount = paymentTransactionDetails.get("paymentAmount");
    Assertion.assertThat("Verify the payment status is paid", paymentStatus, is("PAID"));
    Assertion.assertThat("Verify the paid amount", Double.parseDouble(paymentAmount), is(Double.valueOf(totalAmount)));
  }

    @Author(names = {"Hiten"})
    @Module(name = "PaymentMethod")
    @Tribe(name = "Payment")
    @Vertical(name = "PROP")
    @Test
    public void verifyPaymentMethodPayLater() throws InterruptedException, FailedLoginException {
        Map<String, String> map = getParam("verifyPaymentMethodPayLater");
        BaseTest.paramsThreadLocal.set(map);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PaymentPage<?> landingPage = new PaymentPage<>(session);
        CreateCarRentalOrderPage<?> carRentalOrderPage = new CreateCarRentalOrderPage<>(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        PromoDashboard<?> promoDashboard = new PromoDashboard<>(session);
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
        String otp = map.get("otp");

        step("Create Car Rental order via API and fetch the orderID");
        Map<String,String> orderIdAndHash = carRentalOrderPage.createCarRentalProductTillPaymentApi(email, phoneNumber,passwordDweb ,driverOption, location, startDate, pickupTime, type, fullName, nameTitle, rentalDuration);
        String orderIdCreated = orderIdAndHash.get("orderId");
        String orderHashCreated = orderIdAndHash.get("orderHash");

        step("Login into dweb if not login using the given account");
        promoDashboard.maximize();
        loginPage.loginIfNotWithSameEmail(email,passwordDweb,otp,0);

        step("Verify user is logged in and home page is opened");
        homePage.waitForHomePageToOpen();
        Assertion.assertThat("Verify the home page is opened", homePage.isHomePageOpened(), is(true));
        Assertion.assertThat("Verify the user logged in", loginPage.checkIfLoggedIn(), is(true));

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

        step("Verify the bibli tiket paylater is disabled");
        Assertion.assertThat("Verify the bibli tiket pay later is disabled", landingPage.isTiketPayLaterDisabled(), is(true));
    }

  @Author(names = {"Alfred"})
  @Module(name = "PaymentMethod")
  @Tribe(name = "Payment")
  @Vertical(name = "PROP")
  @Test
  public void verifyPaymentMethodAkulakuWeb() throws InterruptedException, FailedLoginException {
    Map<String, String> map = getParam("verifyPaymentMethodAkulakuWeb");
    BaseTest.paramsThreadLocal.set(map);
    Session<?> session = BaseTest.sessionThreadLocal.get();
    PaymentPage<?> landingPage = new PaymentPage<>(session);
    PaymentPageHelperWeb<?> paymentHelperPage = PaymentPageHelperWeb.getInstance(session);
    CreateCarRentalOrderPage<?> carRentalOrderPage = new CreateCarRentalOrderPage<>(session);
    PaymentApi<?> paymentApi = new PaymentApi<>(session);

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
    Map<String,String> orderIdAndHash = carRentalOrderPage.createCarRentalProductTillPaymentApi(email, phoneNumber,passwordDweb ,driverOption, location, startDate, pickupTime, type, fullName, nameTitle, rentalDuration);
    String orderIdCreated = orderIdAndHash.get("orderId");
    String orderHashCreated = orderIdAndHash.get("orderHash");

    step("Login and select payment method");
    int finalPrice = paymentHelperPage.loginViaCookieAndSelectPaymentMethod(email, passwordDweb, orderIdCreated, orderHashCreated, paymentGroup, paymentMethod, serviceCharge);

    step("Open price history");
    landingPage.openPriceSummary();
    Assertion.assertThat("Is transaction fees is avaialable", landingPage.isTransactionFeesDisplayed(), is(true));

    step("Close the price history");
    landingPage.closeDrawer();

    step("Click on Pay button");
    landingPage.clickOnPaymentButton();

    step("Click continue to payment button if exist");
    landingPage.clickContinueToPayment();

    step("Get order details");
    String orderId = orderIdCreated;
    OrderDetailResponse orderDetail = paymentApi.getOrderDetails(orderId);
    String customerNo = orderDetail.data.contact.getCustomerNo();
    long totalAmount = orderDetail.data.getTotalAmountAsLong();
    String virtualAccountNo = orderDetail.data.getVirtualAccountNo();

    step("Complete Akulaku Payment Paid Simulator");
    boolean isAkulakuSimulatorSuccess = landingPage.payAkulakuSimulator();
    Assertion.assertThat("Akulaku simulator payment is successful", isAkulakuSimulatorSuccess, is(true));

    step("Verify the status from database");
    String query = landingPage.queryString(orderId);
    Map<String,String> paymentTransactionDetails = landingPage.getPaymentTransactionDetails(config, database, collectionName, query);
    String paymentStatus = paymentTransactionDetails.get("paymentStatus");
    String paymentAmount = paymentTransactionDetails.get("paymentAmount");
    Assertion.assertThat("Verify the payment status is paid", paymentStatus, is("PAID"));
    Assertion.assertThat("Verify the paid amount", Double.parseDouble(paymentAmount), is(Double.valueOf(totalAmount)));
  }

  @Author(names = {"Alfred"})
  @Module(name = "PaymentMethod")
  @Tribe(name = "Payment")
  @Vertical(name = "PROP")
  @Test
  public void verifyPaymentMethodKredivoWeb() throws InterruptedException, FailedLoginException {
    Map<String, String> map = getParam("verifyPaymentMethodKredivoWeb");
    BaseTest.paramsThreadLocal.set(map);
    Session<?> session = BaseTest.sessionThreadLocal.get();
    PaymentPage<?> landingPage = new PaymentPage<>(session);
    PaymentPageHelperWeb<?> paymentHelperPage = PaymentPageHelperWeb.getInstance(session);
    CreateCarRentalOrderPage<?> carRentalOrderPage = new CreateCarRentalOrderPage<>(session);
    PaymentApi<?> paymentApi = new PaymentApi<>(session);

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
    Map<String,String> orderIdAndHash = carRentalOrderPage.createCarRentalProductTillPaymentApi(email, phoneNumber,passwordDweb ,driverOption, location, startDate, pickupTime, type, fullName, nameTitle, rentalDuration);
    String orderIdCreated = orderIdAndHash.get("orderId");
    String orderHashCreated = orderIdAndHash.get("orderHash");

    step("Login and select payment method");
    int finalPrice = paymentHelperPage.loginViaCookieAndSelectPaymentMethod(email, passwordDweb, orderIdCreated, orderHashCreated, paymentGroup, paymentMethod, serviceCharge);

    step("Select Kredivo Installment");
    landingPage.selectKredivoInstallment();

    step("Open price history");
    landingPage.openPriceSummary();
    Assertion.assertThat("Is transaction fees is avaialable", landingPage.isTransactionFeesDisplayed(), is(true));

    step("Close the price history");
    landingPage.closeDrawer();

    step("Click on Pay button");
    landingPage.clickOnPaymentButton();

    step("Click continue to payment button if exist");
    landingPage.clickContinueToPayment();

    step("Get order details");
    String orderId = orderIdCreated;
    OrderDetailResponse orderDetail = paymentApi.getOrderDetails(orderId);
    long totalAmount = orderDetail.data.getTotalAmountAsLong();
    String virtualAccountNo = orderDetail.data.getVirtualAccountNo();

    step("Complete Kredivo Payment Paid Simulator");
    boolean isKredivoSimulatorSuccess = landingPage.payKredivoSimulator();
    Assertion.assertThat("Kredivo simulator payment is successful", isKredivoSimulatorSuccess, is(true));

    step("Verify the status from database");
    String query = landingPage.queryString(orderId);
    Map<String,String> paymentTransactionDetails = landingPage.getPaymentTransactionDetails(config, database, collectionName, query);
    String paymentStatus = paymentTransactionDetails.get("paymentStatus");
    String paymentAmount = paymentTransactionDetails.get("paymentAmount");
    Assertion.assertThat("Verify the payment status is paid", paymentStatus, is("PAID"));
    Assertion.assertThat("Verify the paid amount", Double.parseDouble(paymentAmount), is(Double.valueOf(totalAmount)));
  }

  @Author(names = {"Alfred"})
  @Module(name = "PaymentMethod")
  @Tribe(name = "Payment")
  @Vertical(name = "PROP")
  @Test
  public void verifyPaymentMethodWalletGopayWeb() throws InterruptedException, FailedLoginException {
    Map<String, String> map = getParam("verifyPaymentMethodWalletGopayWeb");
    BaseTest.paramsThreadLocal.set(map);
    Session<?> session = BaseTest.sessionThreadLocal.get();
    PaymentPage<?> landingPage = new PaymentPage<>(session);
    PaymentPageHelperWeb<?> paymentHelperPage = PaymentPageHelperWeb.getInstance(session);
    CreateCarRentalOrderPage<?> carRentalOrderPage = new CreateCarRentalOrderPage<>(session);
    PaymentApi<?> paymentApi = new PaymentApi<>(session);

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
    Map<String,String> orderIdAndHash = carRentalOrderPage.createCarRentalProductTillPaymentApi(email, phoneNumber,passwordDweb ,driverOption, location, startDate, pickupTime, type, fullName, nameTitle, rentalDuration);
    String orderIdCreated = orderIdAndHash.get("orderId");
    String orderHashCreated = orderIdAndHash.get("orderHash");

    step("Login and select payment method");
    int finalPrice = paymentHelperPage.loginViaCookieAndSelectPaymentMethod(email, passwordDweb, orderIdCreated, orderHashCreated, paymentGroup, paymentMethod, serviceCharge);

    step("Open price history");
    landingPage.openPriceSummary();
    Assertion.assertThat("Is transaction fees is avaialable", landingPage.isTransactionFeesDisplayed(), is(true));

    step("Close the price history");
    landingPage.closeDrawer();

    step("Click on Pay button");
    landingPage.clickOnPaymentButton();

    step("Click continue to payment button if exist");
    landingPage.clickContinueToPayment();

    step("Complete Gopay Payment Paid Simulator");
    boolean isGopaySimulatorSuccess = landingPage.payGopaySimulator();
    Assertion.assertThat("Gopay simulator payment is successful", isGopaySimulatorSuccess, is(true));

    step("Get order details");
    String orderId = orderIdCreated;
    OrderDetailResponse orderDetail = paymentApi.getOrderDetails(orderId);
    String customerNo = orderDetail.data.contact.getCustomerNo();
    long totalAmount = orderDetail.data.getTotalAmountAsLong();
    String virtualAccountNo = orderDetail.data.getVirtualAccountNo();

    step("Verify the status from database");
    String query = landingPage.queryString(orderId);
    Map<String,String> paymentTransactionDetails = landingPage.getPaymentTransactionDetails(config, database, collectionName, query);
    String paymentStatus = paymentTransactionDetails.get("paymentStatus");
    String paymentAmount = paymentTransactionDetails.get("paymentAmount");
    Assertion.assertThat("Verify the payment status is paid", paymentStatus, is("PAID"));
    Assertion.assertThat("Verify the paid amount", Double.parseDouble(paymentAmount), is(Double.valueOf(totalAmount)));
  }

  @Author(names = {"Alfred"})
  @Module(name = "PaymentMethod")
  @Tribe(name = "Payment")
  @Vertical(name = "PROP")
  @Test
  public void verifyPaymentMethodWalletDANAWeb() throws InterruptedException, FailedLoginException {
    Map<String, String> map = getParam("verifyPaymentMethodWalletDANAWeb");
    BaseTest.paramsThreadLocal.set(map);
    Session<?> session = BaseTest.sessionThreadLocal.get();
    PaymentPage<?> landingPage = new PaymentPage<>(session);
    PaymentPageHelperWeb<?> paymentHelperPage = PaymentPageHelperWeb.getInstance(session);
    CreateCarRentalOrderPage<?> carRentalOrderPage = new CreateCarRentalOrderPage<>(session);
    PaymentApi<?> paymentApi = new PaymentApi<>(session);

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
    Map<String,String> orderIdAndHash = carRentalOrderPage.createCarRentalProductTillPaymentApi(email, phoneNumber,passwordDweb ,driverOption, location, startDate, pickupTime, type, fullName, nameTitle, rentalDuration);
    String orderIdCreated = orderIdAndHash.get("orderId");
    String orderHashCreated = orderIdAndHash.get("orderHash");

    step("Login and select payment method");
    int finalPrice = paymentHelperPage.loginViaCookieAndSelectPaymentMethod(email, passwordDweb, orderIdCreated, orderHashCreated, paymentGroup, paymentMethod, serviceCharge);

//    step("Open price history");
//    landingPage.openPriceSummary();
//    Assertion.assertThat("Is transaction fees is avaialable", landingPage.isTransactionFeesDisplayed(), is(true));
//
//    step("Close the price history");
//    landingPage.closeDrawer();

    step("Click on Pay button");
    landingPage.clickOnPaymentButton();

    step("Click continue to payment button if exist");
    landingPage.clickContinueToPayment();

    step("Complete DANA Payment Paid Simulator");
    boolean isDanaSimulatorSuccess = landingPage.payDanaSimulator();
    Assertion.assertThat("DANA simulator payment is successful", isDanaSimulatorSuccess, is(true));

    step("Get order details");
    String orderId = orderIdCreated;
    OrderDetailResponse orderDetail = paymentApi.getOrderDetails(orderId);
    String customerNo = orderDetail.data.contact.getCustomerNo();
    long totalAmount = orderDetail.data.getTotalAmountAsLong();
    String virtualAccountNo = orderDetail.data.getVirtualAccountNo();

    step("Verify the status from database");
    String query = landingPage.queryString(orderId);
    Map<String,String> paymentTransactionDetails = landingPage.getPaymentTransactionDetails(config, database, collectionName, query);
    String paymentStatus = paymentTransactionDetails.get("paymentStatus");
    String paymentAmount = paymentTransactionDetails.get("paymentAmount");
    Assertion.assertThat("Verify the payment status is paid", paymentStatus, is("PAID"));
    Assertion.assertThat("Verify the paid amount", Double.parseDouble(paymentAmount), is(Double.valueOf(totalAmount)));

    step("Top Up DANA Payment Method after success transaction");
    paymentApi.topUpDanaBalance(phoneNumber, totalAmount);
  }

    @Author(names = {"Hiten"})
    @Module(name = "PaymentMethod")
    @Tribe(name = "Payment")
    @Vertical(name = "PROP")
    @Test(dataProvider = "verifyPaymentMethodUsingCardNONIDRDataProvider")
    public void verifyPaymentMethodUsingCardNONIDR(Map<String, String> map) throws InterruptedException, FailedLoginException, ExecutionException {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PaymentPage<?> landingPage = new PaymentPage<>(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        PromoDashboard<?> promoDashboard = new PromoDashboard<>(session);
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
        String config = map.get("config");
        String database = map.get("database");
        String collectionName = map.get("collectionName");
        String language = map.get("language");
        String otp = map.get("otp");

        step("Create new NON-IDR hotel order");
        Map<String,String> orderIdAndHash = orderCreationPage.createWaitingPaymentOrderApi(username, password, startDate, query, adult, night, room, orderFullName, orderNameTitle, guestFullName, guestTitle, currency,language);
        String orderIdCreated = orderIdAndHash.get("orderId");
        String orderHashCreated = orderIdAndHash.get("orderHash");

        step("Login into dweb if not login using the given account");
        promoDashboard.maximize();
        loginPage.loginWithSpecificAccountViaCookie(username, password);

        step("Verify user is logged in and home page is opened");
        homePage.waitForHomePageToOpen();
        Assertion.assertThat("Verify the home page is opened", homePage.isHomePageOpened(), is(true));
        Assertion.assertThat("Verify the user logged in", loginPage.checkIfLoggedIn(), is(true));

        step("Select english language if not in english");
        homePage.selectEnglishLanguageOnly();

        step("Go to the payment page");
        landingPage.goToPaymentPage(orderIdCreated, orderHashCreated);

        step("Verify payment page is loaded or not");
        Assertion.assertThat("Verify the payment page is loaded or not", landingPage.isPaymentPageLoaded(), is(true));

        double beforePrice = Double.parseDouble(landingPage.getFinalPrice());
        System.out.println("beforePrice "+ beforePrice);

        step("Input the card details");
        landingPage.inputCardNumber(cardNumber);
        landingPage.inputCardMonth(expiry);
        landingPage.inputCardCvv(cvv);
        landingPage.inputCardHolderName(cardName);

        double afterPrice = Double.parseDouble(landingPage.getFinalPrice());
        Assertion.assertThat("Verify the price before selecting the payment is same after selecting the payment method", beforePrice, is(afterPrice));

        step("Open price history");
        landingPage.openPriceSummary();
        Assertion.assertThat("Is transaction fees is avaialable", landingPage.isTransactionFeesDisplayed(), is(false));

        step("Close the price history");
        landingPage.closeDrawer();

        step("Click on Pay button");
        landingPage.clickOnPaymentButton();

        step("Verify the payment successful page is visible or not");
        Assertion.assertThat("Verify the payment successful page is visible", landingPage.isPaymentSuccessfulVisible(), is(true));

        step("Verify the status from database");
        String queryDB = landingPage.queryString(orderIdCreated);
        Map<String,String> paymentTransactionDetails = landingPage.getPaymentTransactionDetails(config, database, collectionName, queryDB);
        String paymentStatus = paymentTransactionDetails.get("paymentStatus");
        String paymentAmount = paymentTransactionDetails.get("paymentAmount");
        Assertion.assertThat("Verify the payment status is paid",paymentStatus , is("PAID"));
        Assertion.assertThat("Verify the paid amount", Double.parseDouble(paymentAmount), is(Double.valueOf(beforePrice)));
    }
    @DataProvider(name = "verifyPaymentMethodUsingCardNONIDRDataProvider")
    public Object[][] verifyPaymentMethodUsingCardNONIDRDataProvider() {return getObjectParams("verifyPaymentMethodUsingCardNONIDR");}

    @Author(names = {"Hiten"})
    @Module(name = "PaymentMethod")
    @Tribe(name = "Payment")
    @Vertical(name = "PROP")
    @Test(dataProvider = "verifyPaymentMethodUsingCardNONIDRNonLoginDataProvider")
    public void verifyPaymentMethodUsingCardNONIDRNonLogin(Map<String, String> map) throws InterruptedException, FailedLoginException, ExecutionException {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PaymentPage<?> landingPage = new PaymentPage<>(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        PromoDashboard<?> promoDashboard = new PromoDashboard<>(session);
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
        String config = map.get("config");
        String database = map.get("database");
        String collectionName = map.get("collectionName");
        String language = map.get("language");

        step("Create new NON-IDR hotel order");
        Map<String,String> orderIdAndHash = orderCreationPage.createWaitingPaymentOrderApi(username, password, startDate, query, adult, night, room, orderFullName, orderNameTitle, guestFullName, guestTitle, currency,language);
        String orderIdCreated = orderIdAndHash.get("orderId");
        String orderHashCreated = orderIdAndHash.get("orderHash");
        homePage.goToHomePage();

        step("Logot from dweb if already login");
        promoDashboard.maximize();
        if(loginPage.checkIfLoggedIn()){
            loginPage.logoutFromDweb();
        }

        step("Verify the home page is opened");
        homePage.waitForHomePageToOpen();
        Assertion.assertThat("Verify the home page is opened", homePage.isHomePageOpened(), is(true));

        step("Select english language if not in english");
        homePage.selectEnglishLanguageOnly();

        step("Go to the payment page");
        landingPage.goToPaymentPage(orderIdCreated, orderHashCreated);

        step("Verify payment page is loaded or not");
        Assertion.assertThat("Verify the payment page is loaded or not", landingPage.isPaymentPageLoaded(), is(true));

        double beforePrice = Double.parseDouble(landingPage.getFinalPrice());
        System.out.println("beforePrice "+ beforePrice);

        step("Input the card details");
        landingPage.inputCardNumber(cardNumber);
        landingPage.inputCardMonth(expiry);
        landingPage.inputCardCvv(cvv);
        landingPage.inputCardHolderName(cardName);

        double afterPrice = Double.parseDouble(landingPage.getFinalPrice());
        Assertion.assertThat("Verify the price before selecting the payment is same after selecting the payment method", beforePrice, is(afterPrice));

        step("Open price history");
        landingPage.openPriceSummary();
        Assertion.assertThat("Is transaction fees is avaialable", landingPage.isTransactionFeesDisplayed(), is(false));

        step("Close the price history");
        landingPage.closeDrawer();

        step("Click on Pay button");
        landingPage.clickOnPaymentButton();

        step("Verify the payment successful page is visible or not");
        Assertion.assertThat("Verify the payment successful page is visible", landingPage.isPaymentSuccessfulVisible(), is(true));

        step("Verify the status from database");
        String queryDB = landingPage.queryString(orderIdCreated);
        Map<String,String> paymentTransactionDetails = landingPage.getPaymentTransactionDetails(config, database, collectionName, queryDB);
        String paymentStatus = paymentTransactionDetails.get("paymentStatus");
        String paymentAmount = paymentTransactionDetails.get("paymentAmount");
        Assertion.assertThat("Verify the payment status is paid",paymentStatus , is("PAID"));
        Assertion.assertThat("Verify the paid amount", Double.parseDouble(paymentAmount), is(Double.valueOf(beforePrice)));
    }
    @DataProvider(name = "verifyPaymentMethodUsingCardNONIDRNonLoginDataProvider")
    public Object[][] verifyPaymentMethodUsingCardNONIDRNonLoginDataProvider() {return getObjectParams("verifyPaymentMethodUsingCardNONIDRNonLogin");}

    @Author(names = { "Hiten" })
    @Module(name = "PaymentMethod")
    @Tribe(name = "Payment")
    @Vertical(name = "PROP")
    @Test(dataProvider = "verifyPaymentMethodUsingCardNonLoginDataProvider")
    public void verifyPaymentMethodUsingCardNonLogin(Map<String, String> map) throws InterruptedException , FailedLoginException {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        PromoDashboard<?> promoDashboard = new PromoDashboard<>(session);
        PaymentPage<?> landingPage = new PaymentPage<>(session);
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
        String cardNumber = map.get("cardNumber");
        String cardMonth = map.get("cardMonth");
        String cvv = map.get("cvv");
        String cardHolderName = map.get("cardHolderName");

        step("Create Car Rental order via API and fetch the orderID");
        Map<String,String> orderIdAndHash = carRentalOrderPage.createCarRentalProductTillPaymentApi(email, phoneNumber,passwordDweb ,driverOption, location, startDate, pickupTime, type, fullName, nameTitle, rentalDuration);
        String orderIdCreated = orderIdAndHash.get("orderId");
        String orderHashCreated = orderIdAndHash.get("orderHash");
        homePage.goToHomePage();

        step("Login into dweb if not login using the given account");
        promoDashboard.maximize();
        if(loginPage.checkIfLoggedIn()){
            loginPage.logoutFromDweb();
        }

        step("Verify user is logged in and home page is opened");
        homePage.waitForHomePageToOpen();
        Assertion.assertThat("Verify the home page is opened", homePage.isHomePageOpened(), is(true));

        step("Select english language if not in english");
        homePage.selectEnglishLanguageOnly();

        step("Go to the payment page");
        landingPage.goToPaymentPage(orderIdCreated, orderHashCreated);

        step("Verify payment page is loaded or not");
        Assertion.assertThat("Verify the payment page is loaded or not", landingPage.isPaymentPageLoaded(), is(true));

        String orderId = landingPage.getOrderId();
        int beforePrice = Integer.parseInt(landingPage.getFinalPrice());

        step("Click on select payment method");
        landingPage.clickOnSelectPaymentMethod();

        step("Scroll down to the payment Group");
        landingPage.goToPaymentMethodsGroup(paymentGroup);

        step("Select Payment Method");
        landingPage.selectPaymentMethod(paymentMethod);
        Assertion.assertThat("Verify the selected payment", landingPage.getSelectedPayment(), is("Credit Card"));

        step("Fetch the price after payment method is applied");
        int finalPrice = Integer.parseInt(landingPage.getFinalPrice());

        step("Verify the service charge after payment is applied");
        Assertion.assertThat("Verify the service charge", (finalPrice-beforePrice), is(serviceCharge));

        step("Input the card details");
        landingPage.inputCardNumber(cardNumber);
        landingPage.inputCardMonth(cardMonth);
        landingPage.inputCardCvv(cvv);
        landingPage.inputCardHolderName(cardHolderName);

        step("Open price history");
        landingPage.openPriceSummary();
        Assertion.assertThat("Is transaction fees is avaialable", landingPage.isTransactionFeesDisplayed(), is(true));

        step("Close the price history");
        landingPage.closeDrawer();

        step("Click on Pay button");
        landingPage.clickOnPaymentButton();

        step("Verify the payment successful page is visible or not");
        Assertion.assertThat("Verify the payment successful page is visible", landingPage.isPaymentSuccessfulVisible(), is(true));

        step("Verify the status from database");
        String query = landingPage.queryString(orderId);
        Map<String,String> paymentTransactionDetails = landingPage.getPaymentTransactionDetails(config, database, collectionName, query);
        String paymentStatus = paymentTransactionDetails.get("paymentStatus");
        String paymentAmount = paymentTransactionDetails.get("paymentAmount");
        Assertion.assertThat("Verify the payment status is paid",paymentStatus , is("PAID"));
        Assertion.assertThat("Verify the paid amount", Double.parseDouble(paymentAmount), is(Double.valueOf(finalPrice)));

        step("Top Up DANA Payment Method after success transaction");
    }
    @DataProvider(name = "verifyPaymentMethodUsingCardNonLoginDataProvider")
    public Object[][] verifyPaymentMethodUsingCardNonLoginDataProvider() {return getObjectParams("verifyPaymentMethodUsingCardNonLogin");}

    @Author(names = { "Hiten" })
    @Module(name = "PaymentMethod")
    @Tribe(name = "Payment")
    @Vertical(name = "PROP")
    @Test(dataProvider = "verifyPaymentMethodOctoClicksDataProvider")
    public void verifyPaymentMethodOctoClicks(Map<String, String> map) throws InterruptedException , FailedLoginException {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PaymentPage<?> landingPage = new PaymentPage<>(session);
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

        step("Open price history");
        landingPage.openPriceSummary();
        Assertion.assertThat("Is transaction fees is avaialable", landingPage.isTransactionFeesDisplayed(), is(true));

        step("Close the price history");
        landingPage.closeDrawer();

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
    @DataProvider(name = "verifyPaymentMethodOctoClicksDataProvider")
    public Object[][] verifyPaymentMethodOctoClicksDataProvider() {return getObjectParams("verifyPaymentMethodOctoClicks");}

    @Author(names = { "Hiten" })
    @Module(name = "PaymentMethod")
    @Tribe(name = "Payment")
    @Vertical(name = "PROP")
    @Test(dataProvider = "verifyOVOAndOneKlikAndBlibliPayPaymentMethodDataProvider")
    public void verifyOVOAndOneKlikAndBlibliPayPaymentMethod(Map<String, String> map) throws InterruptedException , FailedLoginException {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PaymentPage<?> landingPage = new PaymentPage<>(session);
        CreateCarRentalOrderPage<?> carRentalOrderPage = new CreateCarRentalOrderPage<>(session);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        PromoDashboard<?> promoDashboard = new PromoDashboard<>(session);

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
        promoDashboard.maximize();
        loginPage.loginIfNotWithSameEmail(email,passwordDweb,otp,0);

        step("Verify user is logged in and home page is opened");
        homePage.waitForHomePageToOpen();
        Assertion.assertThat("Verify the home page is opened", homePage.isHomePageOpened(), is(true));
        Assertion.assertThat("Verify the user logged in", loginPage.checkIfLoggedIn(), is(true));

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

        step("Verify the payment method is not available");
        Assertion.assertThat("Verify the payment method is not available", landingPage.isPaymentMethodVisible(paymentMethod), is(true));
    }
    @DataProvider(name = "verifyOVOAndOneKlikAndBlibliPayPaymentMethodDataProvider")
    public Object[][] verifyOVOAndOneKlikAndBlibliPayPaymentMethodDataProvider() {return getObjectParams("verifyOVOAndOneKlikAndBlibliPayPaymentMethod");}

    @Author(names = {"Hiten"})
    @Module(name = "PaymentMethod")
    @Tribe(name = "Payment")
    @Vertical(name = "PROP")
    @Test
    public void verifyPaymentMethodUsingNicePayCard() throws InterruptedException, FailedLoginException {
        Map<String, String> map = getParam("verifyPaymentMethodUsingNicePayCard");
        BaseTest.paramsThreadLocal.set(map);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PaymentPage<?> landingPage = new PaymentPage<>(session);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        PromoDashboard<?> promoDashboard = new PromoDashboard<>(session);
        CreateCarRentalOrderPage<?> carRentalOrderPage = new CreateCarRentalOrderPage<>(session);
        PaymentPageHelperWeb<?> paymentHelperPage = PaymentPageHelperWeb.getInstance(session);

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
        String cardNumber = map.get("cardNumber");
        String cardMonth = map.get("cardMonth");
        String cvv = map.get("cvv");
        String cardHolderName = map.get("cardHolderName");

        step("Create Car Rental order via API and fetch the orderID");
        Map<String,String> orderIdAndHash = carRentalOrderPage.createCarRentalProductTillPaymentApi(email, phoneNumber,passwordDweb ,driverOption, location, startDate, pickupTime, type, fullName, nameTitle, rentalDuration);
        String orderIdCreated = orderIdAndHash.get("orderId");
        String orderHashCreated = orderIdAndHash.get("orderHash");

        step("Login into dweb if not login using the given account");
        promoDashboard.maximize();
        loginPage.loginIfNotWithSameEmail(email,passwordDweb,otp,0);

        step("Verify user is logged in and home page is opened");
        homePage.waitForHomePageToOpen();
        Assertion.assertThat("Verify the home page is opened", homePage.isHomePageOpened(), is(true));
        Assertion.assertThat("Verify the user logged in", loginPage.checkIfLoggedIn(), is(true));

        step("Select english language if not in english");
        homePage.selectEnglishLanguageOnly();

        step("Go to the payment page");
        landingPage.goToPaymentPage(orderIdCreated, orderHashCreated);

        step("Verify payment page is loaded or not");
        Assertion.assertThat("Verify the payment page is loaded or not", landingPage.isPaymentPageLoaded(), is(true));

        String orderId = landingPage.getOrderId();
        int beforePrice = Integer.parseInt(landingPage.getFinalPrice());

        step("Click on select payment method");
        landingPage.clickOnSelectPaymentMethod();

        step("Scroll down to the payment Group");
        landingPage.goToPaymentMethodsGroup(paymentGroup);

        step("Select Payment Method");
        landingPage.selectPaymentMethod(paymentMethod);
        Assertion.assertThat("Verify the selected payment", landingPage.getSelectedPayment(), is("Credit Card"));

        step("Fetch the price after payment method is applied");
        int finalPrice = Integer.parseInt(landingPage.getFinalPrice());

        step("Verify the service charge after payment is applied");
        Assertion.assertThat("Verify the service charge", (finalPrice-beforePrice), is(serviceCharge));

        step("Input the card details");
        landingPage.inputCardNumber(cardNumber);
        landingPage.inputCardMonth(cardMonth);
        landingPage.inputCardCvv(cvv);
        landingPage.inputCardHolderName(cardHolderName);

        step("Open price history");
        landingPage.openPriceSummary();
        Assertion.assertThat("Is transaction fees is avaialable", landingPage.isTransactionFeesDisplayed(), is(true));

        step("Close the price history");
        landingPage.closeDrawer();

        step("Click on Pay button");
        landingPage.clickOnPaymentButton();

        step("Verify the payment successful page is visible or not");
        Assertion.assertThat("Verify the payment successful page is visible", landingPage.isPaymentSuccessfulVisible(), is(true));

        step("Verify the status from database");
        String query = landingPage.queryString(orderIdCreated);
        Map<String,String> paymentTransactionDetails = landingPage.getPaymentTransactionDetails(config, database, collectionName, query);
        String paymentStatus = paymentTransactionDetails.get("paymentStatus");
        String paymentAmount = paymentTransactionDetails.get("paymentAmount");
        Assertion.assertThat("Verify the payment status is paid",paymentStatus , is("PAID"));
        Assertion.assertThat("Verify the paid amount", Double.parseDouble(paymentAmount), is(Double.valueOf(finalPrice)));
    }

    @Author(names = {"Benny"})
    @Module(name = "PaymentMethod")
    @Tribe(name = "Payment")
    @Vertical(name = "PROP")
    @Test
    public void verifyTIXPointAsDiscount () throws InterruptedException, FailedLoginException {
        Map<String, String> map = getParam("verifyTIXPointAsDiscount");
        BaseTest.paramsThreadLocal.set(map);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        PromoDashboard<?> promoDashboard = new PromoDashboard<>(session);
        PaymentPage<?> landingPage = new PaymentPage<>(session);
        CreateCarRentalOrderPage<?> carRentalOrderPage = new CreateCarRentalOrderPage<>(
            session);

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

        step("Create Car Rental order via API and fetch the orderID");
        Map<String, String> orderIdAndHash = carRentalOrderPage.createCarRentalProductTillPaymentApi(
            email, phoneNumber, passwordDweb, driverOption, location, startDate, pickupTime,
            type, fullName, nameTitle, rentalDuration);
        String orderIdCreated = orderIdAndHash.get("orderId");
        String orderHashCreated = orderIdAndHash.get("orderHash");

        step("Login into dweb if not login using the given account");
        promoDashboard.maximize();
        loginPage.loginIfNotWithSameEmail(email, passwordDweb, "123456", 0);

        step("Verify user is logged in and home page is opened");
        homePage.waitForHomePageToOpen();
        Assertion.assertThat("Verify the home page is opened", homePage.isHomePageOpened(),
            is(true));
        Assertion.assertThat("Verify the user logged in", loginPage.checkIfLoggedIn(),
            is(true));

        step("Select english language if not in english");
        homePage.selectEnglishLanguageOnly();

        step("Go to the payment page");
        landingPage.goToPaymentPage(orderIdCreated, orderHashCreated);

        step("Verify payment page is loaded or not");
        Assertion.assertThat("Verify the payment page is loaded or not",
            landingPage.isPaymentPageLoaded(), is(true));

        step("Fetch the price before tiket points");
        int beforePrice = Integer.parseInt(landingPage.getFinalPrice());

        step("Click on tiket points");
        landingPage.clickOnTiketPoints();

        step("Open price history");
        landingPage.openPriceSummary();

        if(!landingPage.isTiketPointsDisplayed()) {

            step("Close the price history");
            landingPage.closeDrawer();

            step("Open price history");
            landingPage.openPriceSummary();

            Assertion.assertThat("Is tiket points available", landingPage.isTiketPointsDisplayed(), is(true));
        }

        step("Close the price history");
        landingPage.closeDrawer();

        step("Verify the tiket points is applied");
        Assertion.assertThat("Verify the tiket points text", landingPage.getTiketPointsText(),
            is(true));

        step("Verify the status from database");
        String queryDB = landingPage.queryString(orderIdCreated);
        Map<String, String> paymentTransactionDetails = landingPage.getPaymentTransactionDetails(
            config, database, collectionName, queryDB);
        String paymentStatus = paymentTransactionDetails.get("paymentStatus");
        String paymentAmount = paymentTransactionDetails.get("paymentAmount");
        Assertion.assertThat("Verify the payment status is not paid", paymentStatus,
            is("SHOPPINGCART"));
        Assertion.assertThat("Verify the paid amount", Double.parseDouble(paymentAmount),
            is(Double.valueOf(0)));
    }

    @Author(names = {"Hafizh"})
    @Module(name = "PaymentMethod")
    @Tribe(name = "Payment")
    @Vertical(name = "Sphinx")
    @Test
    public void verify0pInstallmentLabelPayLaterWeb() throws InterruptedException, FailedLoginException {
        Map<String, String> map = getParam("verify0pInstallmentLabelPayLaterWeb");
        BaseTest.paramsThreadLocal.set(map);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PaymentPageWeb<?> paymentPageWeb = PaymentPageWeb.getInstance(session);
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
        String discountCode0p = map.get("discountCode0p");
        String discountCodeNon0p = map.get("discountCodeNon0p");

        step("Create Car Rental order via API and fetch the orderID");
        Map<String,String> orderIdAndHash = carRentalOrderPage.createCarRentalProductTillPaymentApi(email, phoneNumber,passwordDweb ,driverOption, location, startDate, pickupTime, type, fullName, nameTitle, rentalDuration);
        String orderIdCreated = orderIdAndHash.get("orderId");
        String orderHashCreated = orderIdAndHash.get("orderHash");

        step("Reuse the session from the order creation API and select payment method");
        paymentHelperPage.injectSessionAndSelectPaymentMethod(email, passwordDweb, orderIdCreated, orderHashCreated, paymentGroup, paymentMethod);

        step("Open installment options");
        paymentPageWeb.openPayLaterInstallment();

        step("Verify 0p installment label is not shown when user haven't applied promo specifics to 0p installment fee");
        Assertion.assertThat("0p installment fee label is not shown", paymentPageWeb.isInstallmentInterestZeroLabelDisplayed(), is(false));

        step("Close installment option bottomsheet");
        paymentPageWeb.closePayLaterInstallment();

        step("User apply promo for 0p installment fee");
        paymentPageWeb.applyPromoWithSpecificDiscountCode(discountCode0p);

        step("Verify the promo was applied");
        Assertion.assertThat("Promo is applied", paymentPageWeb.isPromoAppliedInPromoSheet(), is(true));

        step("Open installment options");
        paymentPageWeb.openPayLaterInstallment();

        step("Verify 0p installment label is shown when user have applied promo specifics to 0p installment fee");
        Assertion.assertThat("0p installment fee label is shown", paymentPageWeb.isInstallmentInterestZeroLabelDisplayed(), is(true));

        step("Close installment option bottomsheet");
        paymentPageWeb.closePayLaterInstallment();

        step("Unapply promo for 0p installment fee BTPL");
        paymentPageWeb.unapplyPromoFromPromoSheet();

        step("Verify the promo was removed");
        Assertion.assertThat("Promo is unapplied", paymentPageWeb.isPromoInstantUnApplied(), is(true));

        step("Apply promo for non 0p installment fee BTPL");
        paymentPageWeb.applyPromoWithSpecificDiscountCode(discountCodeNon0p);

        step("Verify the promo was applied, so the assertion below cannot pass vacuously");
        Assertion.assertThat("Promo is applied", paymentPageWeb.isPromoAppliedInPromoSheet(), is(true));

        step("Open installment options");
        paymentPageWeb.openPayLaterInstallment();

        step("Verify 0p installment label is not shown when user haven't applied promo specifics to 0p installment fee");
        Assertion.assertThat("0p installment fee label is not shown", paymentPageWeb.isInstallmentInterestZeroLabelDisplayed(), is(false));
    }
}
