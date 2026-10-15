package com.tiket.web.test.promo;

import static com.tiket.core.CoreFunctions.*;
import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.core.Is.is;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.apiPage.PaymentApi;
import com.tiket.apiPage.Unm;
import com.tiket.common.RetryUtils;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Session;
import com.tiket.pojo.giftCard.GiftCardOrderResponse;
import com.tiket.pojo.payment.PaymentCorePromoApplyResponse;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.web.dWeb.page.accommodation.hotels.HotelRoomListPage;
import com.tiket.web.dWeb.page.payment.PaymentPage;
import com.tiket.web.dashboard.page.order.orderCreationPage;
import com.tiket.web.dashboard.page.promo.PromoDashboard;
import com.tiket.web.page.accommodation.hotels.BookingForm.HotelBookingFormPageWeb;
import com.tiket.web.page.accommodation.hotels.hotelLandingPage.HotelLandingPageWeb;
import com.tiket.web.page.common.home.HomePageWeb;
import com.tiket.web.page.common.home.LoginPageWeb;

import com.tiket.web.page.corporate.travel_plan.TravelPlanWeb;
import com.tiket.web.page.giftcard.GiftCardPageWeb;
import com.tiket.web.page.platform.payment.PaymentPageWeb;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import javax.security.auth.login.FailedLoginException;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

public class PromoTest extends BaseTest {

    @Author(names = { "Fery" })
    @Module(name = "PromoPayment")
    @Tribe(name = "Promo")
    @Vertical(name = "Promo")
    @Test
    public void verifyPromoExpired() throws FailedLoginException, InterruptedException {
        Map<String, String> map = getParam("verifyPromoExpired");
        BaseTest.paramsThreadLocal.set(map);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        PaymentPageWeb<?> landingPage = PaymentPageWeb.getInstance(session);
        orderCreationPage<?> orderCreationPage = new orderCreationPage<>(session);

        String email = map.get("email");
        String currency = map.get("currency");
        String productUrlName = map.get("productUrlName");
        String passwordDweb = map.get("passwordDweb");
        String[] salutationList = { "Mr", "Mrs", "Ms" };
        String fullName = randomName() + " Autorder";
        String nameTitle = getRandomObject(salutationList);
        String promoCodeName = map.get("promoCodeName");
        String errorMsgEN = map.get("errorMsgEN");
        String errorMsgID = map.get("errorMsgID");

        step("Create Event/Todo order via API and fetch the orderID");
        orderCreationPage.createGuestWaitingPaymentOrderApiTodo(productUrlName, true, currency, fullName, nameTitle, fullName, email, nameTitle);
        String orderIdCreated = String.valueOf(orderCreationPage.getOrderId());
        String orderHashCreated = orderCreationPage.getOrderHash();

        step("Login as " + email + " via session injection");
        landingPage.maximize();
        loginPage.loginWithSpecificAccountViaCookie(email, passwordDweb);


        step("Select english language if not in english");
        homePage.selectEnglishLanguageOnly();

        step("Go to the payment page");
        landingPage.goToPaymentPage(orderIdCreated, orderHashCreated);

        step("Verify payment page is loaded or not");
        Assertion.assertThat("Verify the payment page is loaded or not",
                landingPage.isPaymentPageLoaded(), is(true));

        step("Click in promo suggestion");
        landingPage.clickPromoSuggestion();

        step("input text in promo box");
        landingPage.inputPromoInInputField(promoCodeName);

        step("Click apply button");
        landingPage.clickApplyButton();

        step("Verify Error Message");
        Assertion.assertThat("Verify Error Message",
                landingPage.verifyErrorMsg(errorMsgEN, errorMsgID), is(true));
    }

    @Author(names = { "Rounit", "Fery" })
    @Module(name = "PromoPayment")
    @Tribe(name = "Promo")
    @Vertical(name = "Promo")
    @Test
    public void verifyPromoSuggestionInEligibleSection()
            throws InterruptedException, FailedLoginException {
        Map<String, String> map = getParam("verifyPromoSuggestionInEligibleSection");
        BaseTest.paramsThreadLocal.set(map);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        PaymentPageWeb<?> landingPage = PaymentPageWeb.getInstance(session);
        orderCreationPage<?> orderCreationPage = new orderCreationPage<>(session);

        String email = map.get("email");
        String currency = map.get("currency");
        String productUrlName = map.get("productUrlName");
        String passwordDweb = map.get("passwordDweb");
        String[] salutationList = { "Mr", "Mrs", "Ms" };
        String fullName = randomName() + " Autorder";
        String nameTitle = getRandomObject(salutationList);
        String otp = map.get("otp");

        step("Create Event/Todo order via API and fetch the orderID");
        orderCreationPage.createGuestWaitingPaymentOrderApiTodo(productUrlName, true, currency, fullName, nameTitle, fullName, email, nameTitle);
        String orderIdCreated = String.valueOf(orderCreationPage.getOrderId());
        String orderHashCreated = orderCreationPage.getOrderHash();

        step("User open home page");
        homePage.goToHomePage();

        step("Login as " + email + " via session injection");
        landingPage.maximize();
        loginPage.loginWithSpecificAccountViaCookie(email, passwordDweb);


        step("Select english language if not in english");
        homePage.selectEnglishLanguageOnly();

        step("Go to the payment page");
        landingPage.goToPaymentPage(orderIdCreated, orderHashCreated);

        step("Verify payment page is loaded or not");
        Assertion.assertThat("Verify the payment page is loaded or not",
                landingPage.isPaymentPageLoaded(), is(true));

        step("Click on promo suggestion");
        landingPage.clickPromoSuggestion();

        step("Verify Eligible Promo List");
        Assertion.assertThat("Verify Eligible Promo List", landingPage.verifyEligiblePromo(),
                is(true));
    }

    @Author(names = { "Rounit", "Fery" })
    @Module(name = "PromoPayment")
    @Tribe(name = "Promo")
    @Vertical(name = "Promo")
    @Test
    public void verifyPromoSuggestionInNonEligibleSection()
            throws InterruptedException, FailedLoginException {
        Map<String, String> map = getParam("verifyPromoSuggestionInNonEligibleSection");
        BaseTest.paramsThreadLocal.set(map);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        PaymentPageWeb<?> landingPage = PaymentPageWeb.getInstance(session);
        orderCreationPage<?> orderCreationPage = new orderCreationPage<>(session);

        String email = map.get("email");
        String currency = map.get("currency");
        String productUrlName = map.get("productUrlName");
        String passwordDweb = map.get("passwordDweb");
        String[] salutationList = { "Mr", "Mrs", "Ms" };
        String fullName = randomName() + " Autorder";
        String nameTitle = getRandomObject(salutationList);
        String otp = map.get("otp");

        step("Create Event/Todo order via API and fetch the orderID");
        orderCreationPage.createGuestWaitingPaymentOrderApiTodo(productUrlName, true, currency, fullName, nameTitle, fullName, email, nameTitle);
        String orderIdCreated = String.valueOf(orderCreationPage.getOrderId());
        String orderHashCreated = orderCreationPage.getOrderHash();

        step("User open home page");
        homePage.goToHomePage();

        step("Login as " + email + " via session injection");
        landingPage.maximize();
        loginPage.loginWithSpecificAccountViaCookie(email, passwordDweb);


        step("Select english language if not in english");
        homePage.selectEnglishLanguageOnly();

        step("Go to the payment page");
        landingPage.goToPaymentPage(orderIdCreated, orderHashCreated);

        step("Verify payment page is loaded or not");
        Assertion.assertThat("Verify the payment page is loaded or not",
                landingPage.isPaymentPageLoaded(), is(true));

        step("Click on promo suggestion");
        landingPage.clickPromoSuggestion();

        step("Verify Non Eligible Promo List");
        Assertion.assertThat("Verify Non Eligible Promo List", landingPage.verifyNonEligiblePromo(),
                is(true));
    }

    @Author(names = { "Rounit", "Fery" })
    @Module(name = "PromoPayment")
    @Tribe(name = "Promo")
    @Vertical(name = "Promo")
    @Test
    public void verifyBestSuitedPromoInPromoSuggestion()
            throws InterruptedException, FailedLoginException {
        Map<String, String> map = getParam("verifyBestSuitedPromoInPromoSuggestion");
        BaseTest.paramsThreadLocal.set(map);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        PaymentPageWeb<?> landingPage = PaymentPageWeb.getInstance(session);
        orderCreationPage<?> orderCreationPage = new orderCreationPage<>(session);

        String email = map.get("email");
        String currency = map.get("currency");
        String productUrlName = map.get("productUrlName");
        String passwordDweb = map.get("passwordDweb");
        String[] salutationList = { "Mr", "Mrs", "Ms" };
        String fullName = randomName() + " Autorder";
        String nameTitle = getRandomObject(salutationList);
        String otp = map.get("otp");
        
        step("Create Event/Todo order via API and fetch the orderID");
        orderCreationPage.createGuestWaitingPaymentOrderApiTodo(productUrlName, true, currency, fullName, nameTitle, fullName, email, nameTitle);
        String orderIdCreated = String.valueOf(orderCreationPage.getOrderId());
        String orderHashCreated = orderCreationPage.getOrderHash();

        step("User open home page");
        homePage.goToHomePage();

        step("Login as " + email + " via session injection");
        landingPage.maximize();
        loginPage.loginWithSpecificAccountViaCookie(email, passwordDweb);


        step("Select english language if not in english");
        homePage.selectEnglishLanguageOnly();

        step("Go to the payment page");
        landingPage.goToPaymentPage(orderIdCreated, orderHashCreated);

        step("Verify payment page is loaded or not");
        Assertion.assertThat("Verify the payment page is loaded or not",
                landingPage.isPaymentPageLoaded(), is(true));

        step("Click on promo suggestion");
        landingPage.clickPromoSuggestion();

        step("Verify Best Suited Promo from Promo List");
        Assertion.assertThat("Verify Best Suited Promo from Promo List",
                landingPage.verifyBestSuitedPromo(), is(true));
    }

    @Author(names = { "Benny", "Fery" })
    @Module(name = "PromoPayment")
    @Tribe(name = "Promo")
    @Vertical(name = "Promo")
    @Test
    public void verifyClosePromoSuggestion() throws InterruptedException, FailedLoginException {
        Map<String, String> map = getParam("verifyClosePromoSuggestion");
        BaseTest.paramsThreadLocal.set(map);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        PaymentPageWeb<?> landingPage = PaymentPageWeb.getInstance(session);
        orderCreationPage<?> orderCreationPage = new orderCreationPage<>(session);

        String email = map.get("email");
        String currency = map.get("currency");
        String productUrlName = map.get("productUrlName");
        String passwordDweb = map.get("passwordDweb");
        String[] salutationList = { "Mr", "Mrs", "Ms" };
        String fullName = randomName() + " Autorder";
        String nameTitle = getRandomObject(salutationList);

        step("Create Event/Todo order via API and fetch the orderID");
        orderCreationPage.createGuestWaitingPaymentOrderApiTodo(productUrlName, true, currency, fullName, nameTitle, fullName, email, nameTitle);
        String orderIdCreated = String.valueOf(orderCreationPage.getOrderId());
        String orderHashCreated = orderCreationPage.getOrderHash();

        step("Login as " + email + " via session injection");
        landingPage.maximize();
        loginPage.loginWithSpecificAccountViaCookie(email, passwordDweb);


        step("Select english language if not in english");
        homePage.selectEnglishLanguageOnly();

        step("Go to the payment page");
        landingPage.goToPaymentPage(orderIdCreated, orderHashCreated);

        step("Verify payment page is loaded or not");
        Assertion.assertThat("Verify the payment page is loaded or not",
                landingPage.isPaymentPageLoaded(), is(true));

        step("Click in promo suggestion");
        landingPage.clickPromoSuggestion();

        step("Click in close promo suggestion");
        landingPage.clickClosePromoSuggestion();

        step("Verify drawer promo suggestion is closed or not");
        Assertion.assertThat("Verify drawer promo suggestion is closed or not",
                landingPage.isPaymentPageLoaded(), is(true));
    }

    @Author(names = { "Fery" })
    @Module(name = "PromoPayment")
    @Tribe(name = "Promo")
    @Vertical(name = "Promo")
    @Test
    public void verifyCheckAvbAfterApply() throws InterruptedException, FailedLoginException {
        Map<String, String> map = getParam("verifyCheckAvbAfterApply");
        BaseTest.paramsThreadLocal.set(map);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        PaymentPageWeb<?> landingPage = PaymentPageWeb.getInstance(session);
        orderCreationPage<?> orderCreationPage = new orderCreationPage<>(session);

        String email = map.get("email");
        String currency = map.get("currency");
        String productUrlName = map.get("productUrlName");
        String passwordDweb = map.get("passwordDweb");
        String[] salutationList = { "Mr", "Mrs", "Ms" };
        String fullName = randomName() + " Autorder";
        String nameTitle = getRandomObject(salutationList);
        String otp = map.get("otp");
        String promoCodeName = map.get("promoCodeName");
        String appliedMsg = map.get("appliedMsg");
        String paymentGroup = map.get("paymentGroup");
        String paymentMethod = map.get("paymentMethod");
        String wordingApplied = map.get("wordingApplied");

        step("Create Event/Todo order via API and fetch the orderID");
        orderCreationPage.createGuestWaitingPaymentOrderApiTodo(productUrlName, true, currency, fullName, nameTitle, fullName, email, nameTitle);
        String orderIdCreated = String.valueOf(orderCreationPage.getOrderId());
        String orderHashCreated = orderCreationPage.getOrderHash();

        step("User open home page");
        homePage.goToHomePage();

        step("Login as " + email + " via session injection");
        landingPage.maximize();
        loginPage.loginWithSpecificAccountViaCookie(email, passwordDweb);


        step("Select english language if not in english");
        homePage.selectEnglishLanguageOnly();

        step("Go to the payment page");
        landingPage.goToPaymentPage(orderIdCreated, orderHashCreated);

        step("Verify payment page is loaded or not");
        Assertion.assertThat("Verify the payment page is loaded or not",
                landingPage.isPaymentPageLoaded(), is(true));

        step("Click on promo suggestion");
        landingPage.clickPromoSuggestion();

        step("Input & apply promo in the promo box");
        landingPage.inputPromoInInputField(promoCodeName);
        landingPage.clickApplyButton();

        step("Change payment method");
        landingPage.clickOnSelectPaymentMethod();

        step("Scroll down to the payment Group");
        landingPage.goToPaymentMethodsGroup(paymentGroup);

        step("Select Payment Method");
        landingPage.selectPaymentMethod(paymentMethod);

        step("Verify that Check availability working");
        Assertion.assertThat("Check availability working",
                landingPage.isPromoInstantApplied(wordingApplied), is(true));
    }

    @Author(names = { "Rounit", "Fery" })
    @Module(name = "PromoPayment")
    @Tribe(name = "Promo")
    @Vertical(name = "Promo")
    @Test
    public void verifyApplyPromoFromTNC() throws InterruptedException, FailedLoginException {
        Map<String, String> map = getParam("verifyApplyPromoFromTNC");
        BaseTest.paramsThreadLocal.set(map);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        PaymentPageWeb<?> landingPage = PaymentPageWeb.getInstance(session);
        orderCreationPage<?> orderCreationPage = new orderCreationPage<>(session);

        String email = map.get("email");
        String currency = map.get("currency");
        String productUrlName = map.get("productUrlName");
        String passwordDweb = map.get("passwordDweb");
        String[] salutationList = { "Mr", "Mrs", "Ms" };
        String fullName = randomName() + " Autorder";
        String nameTitle = getRandomObject(salutationList);
        String wordingApplied = map.get("wordingApplied");
        String wordingCodeApplied = map.get("wordingCodeApplied");
        String promoCodeName = map.get("promoCodeName");

        step("Create Event/Todo order via API and fetch the orderID");
        orderCreationPage.createGuestWaitingPaymentOrderApiTodo(productUrlName, true, currency, fullName, nameTitle, fullName, email, nameTitle);
        String orderIdCreated = String.valueOf(orderCreationPage.getOrderId());
        String orderHashCreated = orderCreationPage.getOrderHash();

        step("Login as " + email + " via session injection");
        landingPage.maximize();
        loginPage.loginWithSpecificAccountViaCookie(email, passwordDweb);


        step("Select english language if not in english");
        homePage.selectEnglishLanguageOnly();

        step("Go to the payment page");
        landingPage.goToPaymentPage(orderIdCreated, orderHashCreated);

        step("Verify payment page is loaded or not");
        Assertion.assertThat("Verify the payment page is loaded or not",
                landingPage.isPaymentPageLoaded(), is(true));

        step("Click in promo suggestion");
        landingPage.clickPromoSuggestion();

        step("Scroll to promo and click on its T&C button");
        landingPage.scrollToPromoAndClickTNC(promoCodeName);

        step("Click apply button");
        landingPage.clickTNCApplyButton();

        step("Verify promo instant is applied or not");
        Assertion.assertThat("Verify promo instant is applied or not",
                landingPage.isPromoInstantApplied(wordingApplied), is(true));
    }

    @Author(names = { "Benny", "Fery" })
    @Module(name = "PromoPayment")
    @Tribe(name = "Promo")
    @Vertical(name = "Promo")
    @Test
    public void verifyApplyPromoFromPromoBox() throws InterruptedException, FailedLoginException {
        Map<String, String> map = getParam("verifyApplyPromoFromPromoBox");
        BaseTest.paramsThreadLocal.set(map);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePageWeb<?> homePageWeb = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPageWeb = LoginPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPageWeb = PaymentPageWeb.getInstance(session);
        orderCreationPage<?> orderCreationPage = new orderCreationPage<>(session);

        String email = map.get("email");
        String currency = map.get("currency");
        String productUrlName = map.get("productUrlName");
        String passwordDweb = map.get("passwordDweb");
        String[] salutationList = { "Mr", "Mrs", "Ms" };
        String fullName = randomName() + " Autorder";
        String nameTitle = getRandomObject(salutationList);
        String promoCodeName = map.get("promoCodeName");
        String wordingApplied = map.get("wordingApplied");
        String wordingCodeApplied = map.get("wordingCodeApplied");
        String otp = map.get("otp");

        step("Create Event/Todo order via API and fetch the orderID");
        orderCreationPage.createGuestWaitingPaymentOrderApiTodo(productUrlName, true, currency, fullName, nameTitle, fullName, email, nameTitle);
        String orderIdCreated = String.valueOf(orderCreationPage.getOrderId());
        String orderHashCreated = orderCreationPage.getOrderHash();

        step("Login as " + email + " via session injection");
        paymentPageWeb.maximize();
        loginPageWeb.loginWithSpecificAccountViaCookie(email, passwordDweb);


        step("Select english language if not in english");
        homePageWeb.selectEnglishLanguageOnly();

        step("Go to the payment page");
        paymentPageWeb.goToPaymentPage(orderIdCreated, orderHashCreated);

        step("Verify payment page is loaded or not");
        Assertion.assertThat("Verify the payment page is loaded or not", paymentPageWeb.isPaymentPageLoaded(),
                is(true));

        step("Click in promo suggestion");
        paymentPageWeb.clickPromoSuggestion();

        step("input text in promo box");
        paymentPageWeb.inputPromoInInputField(promoCodeName);

        step("Click apply button");
        paymentPageWeb.clickApplyButton();

        step("Verify promo instant is applied or not");
        Assertion.assertThat("Verify promo instant is applied or not",
                paymentPageWeb.isPromoInstantApplied(wordingApplied), is(true));
    }

    @Author(names = { "Rounit", "Fery" })
    @Module(name = "PromoPayment")
    @Tribe(name = "Promo")
    @Vertical(name = "Promo")
    @Test
    public void verifyPSv3Flow() throws InterruptedException, FailedLoginException {
        Map<String, String> map = getParam("verifyPSv3Flow");
        BaseTest.paramsThreadLocal.set(map);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        PaymentPageWeb<?> landingPage = PaymentPageWeb.getInstance(session);
        orderCreationPage<?> orderCreationPage = new orderCreationPage<>(session);

        String email = map.get("email");
        String currency = map.get("currency");
        String productUrlName = map.get("productUrlName");
        String passwordDweb = map.get("passwordDweb");
        String[] salutationList = { "Mr", "Mrs", "Ms" };
        String fullName = randomName() + " Autorder";
        String nameTitle = getRandomObject(salutationList);
        String promoCodeName = map.get("promoCodeName");
        String wordingApplied = map.get("wordingApplied");
        String wordingCodeApplied = map.get("wordingCodeApplied");

        step("Create Event/Todo order via API and fetch the orderID");
        orderCreationPage.createGuestWaitingPaymentOrderApiTodo(productUrlName, true, currency, fullName, nameTitle, fullName, email, nameTitle);
        String orderIdCreated = String.valueOf(orderCreationPage.getOrderId());
        String orderHashCreated = orderCreationPage.getOrderHash();

        step("Login as " + email + " via session injection");
        landingPage.maximize();
        loginPage.loginWithSpecificAccountViaCookie(email, passwordDweb);


        step("Select english language if not in english");
        homePage.selectEnglishLanguageOnly();

        step("Go to the payment page");
        landingPage.goToPaymentPage(orderIdCreated, orderHashCreated);

        step("Verify payment page is loaded or not");
        Assertion.assertThat("Verify the payment page is loaded or not", landingPage.isPaymentPageLoaded(), is(true));

        step("Click in promo suggestion");
        landingPage.clickPromoSuggestion();

        step("Click on PROMO with tag CHANGE PAYMENT");
        landingPage.clickChangePayment(promoCodeName);

        step("Click on CHANGE PAYMENT METHOD BUTTON in the Pop-up");
        landingPage.clickChangePaymentMethod();

        step("Verify promo instant is applied or not");
        Assertion.assertThat("Verify promo instant is applied or not",
                landingPage.isPromoInstantApplied(wordingApplied), is(true));
    }

    @Author(names = { "Rounit", "Fery" })
    @Module(name = "PromoPayment")
    @Tribe(name = "Promo")
    @Vertical(name = "Promo")
    @Test
    public void verifyUnapplyPromoCodeFlow() throws InterruptedException, FailedLoginException {
        Map<String, String> map = getParam("verifyUnapplyPromoCodeFlow");
        BaseTest.paramsThreadLocal.set(map);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePageWeb<?> homePageWeb = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPageWeb = LoginPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPageWeb = PaymentPageWeb.getInstance(session);
        orderCreationPage<?> orderCreationPage = new orderCreationPage<>(session);

        String email = map.get("email");
        String currency = map.get("currency");
        String productUrlName = map.get("productUrlName");
        String passwordDweb = map.get("passwordDweb");
        String[] salutationList = { "Mr", "Mrs", "Ms" };
        String fullName = randomName() + " Autorder";
        String nameTitle = getRandomObject(salutationList);
        String promoCodeName = map.get("promoCodeName");
        String wordingApplied = map.get("wordingApplied");
        String wordingCodeApplied = map.get("wordingCodeApplied");
        
        step("Create Event/Todo order via API and fetch the orderID");
        orderCreationPage.createGuestWaitingPaymentOrderApiTodo(productUrlName, true, currency, fullName, nameTitle, fullName, email, nameTitle);
        String orderIdCreated = String.valueOf(orderCreationPage.getOrderId());
        String orderHashCreated = orderCreationPage.getOrderHash();

        step("Login as " + email + " via session injection");
        paymentPageWeb.maximize();
        loginPageWeb.loginWithSpecificAccountViaCookie(email, passwordDweb);


        step("Select english language if not in english");
        homePageWeb.selectEnglishLanguageOnly();

        step("Go to the payment page");
        paymentPageWeb.goToPaymentPage(orderIdCreated, orderHashCreated);

        step("Verify payment page is loaded or not");
        Assertion.assertThat("Verify the payment page is loaded or not", paymentPageWeb.isPaymentPageLoaded(),
                is(true));

        step("Click in promo suggestion");
        paymentPageWeb.clickPromoSuggestion();

        step("input text in promo box");
        paymentPageWeb.inputPromoInInputField(promoCodeName);

        step("Click apply button");
        paymentPageWeb.clickApplyButton();

        step("Click UnApply button");
        paymentPageWeb.clickUnApplyButton();

        step("Click on Remove Promo button");
        paymentPageWeb.clickRemovePromo();

        step("Verify Promo instant is UnApplied or not");
        Assertion.assertThat("Verify Promo instant is UnApplied or not", paymentPageWeb.isPromoInstantUnApplied(),
                is(true));
    }

    @Author(names = { "Vidhi Jindal" })
    @Module(name = "PromoPayment")
    @Tribe(name = "Promo")
    @Vertical(name = "Promo")
    @Test
    public void verifyLoginBeforePromo() throws FailedLoginException, InterruptedException {
        Map<String, String> map = getParam("verifyLoginBeforePromo");
        BaseTest.paramsThreadLocal.set(map);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        PaymentPageWeb<?> landingPage = PaymentPageWeb.getInstance(session);
        orderCreationPage<?> orderCreationPage = new orderCreationPage<>(session);

        String email = map.get("email");
        String currency = map.get("currency");
        String productUrlName = map.get("productUrlName");
        String passwordDweb = map.get("passwordDweb");
        String[] salutationList = { "Mr", "Mrs", "Ms" };
        String fullName = randomName() + " Autorder";
        String nameTitle = getRandomObject(salutationList);
        String promoCodeName = map.get("promoCodeName");
        String otp = map.get("otp");
        int retryCounter = Integer.parseInt(map.get("retryCounter"));
        String errorMsgEN = map.get("errorMsgEN");
        String errorMsgID = map.get("errorMsgID");

        step("Create Event/Todo order via API and fetch the orderID");
        orderCreationPage.createGuestWaitingPaymentOrderApiTodo(productUrlName, true, currency, fullName, nameTitle, fullName, email, nameTitle);
        String orderIdCreated = String.valueOf(orderCreationPage.getOrderId());
        String orderHashCreated = orderCreationPage.getOrderHash();

        step("Clear session cookies to ensure guest state");
        loginPage.clearSessionCookies();

        step("Go to the payment page.");
        landingPage.goToPaymentPage(orderIdCreated, orderHashCreated);

        step("Verify payment page is loaded or not.");
        Assertion.assertThat("Verify the payment page is loaded or not", landingPage.isPaymentPageLoaded(), is(true));

        step("User clicks in promo suggestion.");
        landingPage.clickPromoSuggestion();

        step("User applies promo either through scrolling or by directly entering the promo code in the promo field and clicks apply.");
        landingPage.promoInputOrScroll(promoCodeName);

        step("Log In Pop Up appears and user is redirected to login page.");
        landingPage.clickLogInNow();

        step("User logs in into web, using his credentials.");
        loginPage.loginIntoWebAfterPopUpAppears(email, passwordDweb, otp, retryCounter);

        step("User verifies whether promo is applied in the payment landing page.");
        Assertion.assertThat("Promo is applied on the payment page.", landingPage.promoIsApplied(), is(true));
    }

    @Author(names = { "Benny", "Vidhi Jindal" })
    @Module(name = "PromoPayment")
    @Tribe(name = "Promo")
    @Vertical(name = "PROP")
    @Test
    public void verifyValidationErrorEmptyPromo() throws InterruptedException, FailedLoginException {
        Map<String, String> map = getParam("verifyValidationErrorEmptyPromo");
        BaseTest.paramsThreadLocal.set(map);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        PaymentPageWeb<?> landingPage = PaymentPageWeb.getInstance(session);
        orderCreationPage<?> orderCreationPage = new orderCreationPage<>(session);

        String email = map.get("email");
        String currency = map.get("currency");
        String productUrlName = map.get("productUrlName");
        String passwordDweb = map.get("passwordDweb");
        String[] salutationList = { "Mr", "Mrs", "Ms" };
        String fullName = randomName() + " Autorder";
        String nameTitle = getRandomObject(salutationList);
        String expectedErrorMessage = map.get("expectedErrorMessage");

        step("Create Event/Todo order via API and fetch the orderID");
        orderCreationPage.createGuestWaitingPaymentOrderApiTodo(productUrlName, true, currency, fullName, nameTitle, fullName, email, nameTitle);
        String orderIdCreated = String.valueOf(orderCreationPage.getOrderId());
        String orderHashCreated = orderCreationPage.getOrderHash();

        step("Login as " + email + " via session injection");
        landingPage.maximize();
        loginPage.loginWithSpecificAccountViaCookie(email, passwordDweb);


        step("Select english language if not in english");
        homePage.selectEnglishLanguageOnly();

        step("Go to the payment page");
        landingPage.goToPaymentPage(orderIdCreated, orderHashCreated);

        step("Verify payment page is loaded or not");
        Assertion.assertThat("Verify the payment page is loaded or not", landingPage.isPaymentPageLoaded(), is(true));

        step("Click in promo suggestion");
        landingPage.clickPromoSuggestion();

        step("Click apply button");
        landingPage.clickApplyButton();

        step("Verify validation error empty promo");
        Assertion.assertThat("Verify validation error empty promo",
                landingPage.isValidationCorrect(expectedErrorMessage), is(true));
    }

    @Author(names = { "Rounit", "Vidhi Jindal" })
    @Module(name = "PromoPayment")
    @Tribe(name = "Promo")
    @Vertical(name = "PROP")
    @Test
    public void verifyValidationOrderErrorType() throws InterruptedException, FailedLoginException {
        Map<String, String> map = getParam("verifyValidationOrderErrorType");
        BaseTest.paramsThreadLocal.set(map);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        PaymentPageWeb<?> landingPage = PaymentPageWeb.getInstance(session);
        orderCreationPage<?> orderCreationPage = new orderCreationPage<>(session);

        String email = map.get("email");
        String currency = map.get("currency");
        String productUrlName = map.get("productUrlName");
        String passwordDweb = map.get("passwordDweb");
        String[] salutationList = { "Mr", "Mrs", "Ms" };
        String fullName = randomName() + " Autorder";
        String nameTitle = getRandomObject(salutationList);
        String promoCodeName = map.get("promoCodeName");
        String wordingApplied = map.get("wordingApplied");
        String wordingCodeApplied = map.get("wordingCodeApplied");
        String paymentMethod = map.get("paymentMethod");
        String errorMsgEN = map.get("errorMsgEN");
        String errorMsgID = map.get("errorMsgID");

        step("Create Event/Todo order via API and fetch the orderID");
        orderCreationPage.createGuestWaitingPaymentOrderApiTodo(productUrlName, true, currency, fullName, nameTitle, fullName, email, nameTitle);
        String orderIdCreated = String.valueOf(orderCreationPage.getOrderId());
        String orderHashCreated = orderCreationPage.getOrderHash();

        step("Login as " + email + " via session injection");
        landingPage.maximize();
        loginPage.loginWithSpecificAccountViaCookie(email, passwordDweb);


        step("Select english language if not in english");
        homePage.selectEnglishLanguageOnly();

        step("Go to the payment page");
        landingPage.goToPaymentPage(orderIdCreated, orderHashCreated);

        step("Verify payment page is loaded or not");
        Assertion.assertThat("Verify the payment page is loaded or not", landingPage.isPaymentPageLoaded(), is(true));

        step("Click in promo suggestion");
        landingPage.clickPromoSuggestion();

        step("input text in promo box");
        landingPage.inputPromoInInputField(promoCodeName);

        step("Click apply button");
        landingPage.clickApplyButton();

        step("Verify Error Message");
        Assertion.assertThat("Verify Error Message", landingPage.verifyErrorMsg(errorMsgEN, errorMsgID), is(true));
    }

    @Author(names = { "Rounit", "Vidhi Jindal" })
    @Module(name = "PromoPayment")
    @Tribe(name = "Promo")
    @Vertical(name = "Promo")
    @Test
    public void verifyApplyHybridPromo() throws InterruptedException, FailedLoginException {
        Map<String, String> map = getParam("verifyApplyHybridPromo");
        BaseTest.paramsThreadLocal.set(map);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        PaymentPageWeb<?> landingPage = PaymentPageWeb.getInstance(session);
        orderCreationPage<?> orderCreationPage = new orderCreationPage<>(session);

        String email = map.get("email");
        String currency = map.get("currency");
        String productUrlName = map.get("productUrlName");
        String passwordDweb = map.get("passwordDweb");
        String[] salutationList = { "Mr", "Mrs", "Ms" };
        String fullName = randomName() + " Autorder";
        String nameTitle = getRandomObject(salutationList);
        String promoCodeName = map.get("promoCodeName");
        String wordingApplied = map.get("wordingApplied");
        String wordingCodeApplied = map.get("wordingCodeApplied");
        String paymentGroup = map.get("paymentGroup");
        String paymentMethod = map.get("paymentMethod");

        step("Create Event/Todo order via API and fetch the orderID");
        orderCreationPage.createGuestWaitingPaymentOrderApiTodo(productUrlName, true, currency, fullName, nameTitle, fullName, email, nameTitle);
        String orderIdCreated = String.valueOf(orderCreationPage.getOrderId());
        String orderHashCreated = orderCreationPage.getOrderHash();

        step("Login as " + email + " via session injection");
        landingPage.maximize();
        loginPage.loginWithSpecificAccountViaCookie(email, passwordDweb);


        step("Select english language if not in english");
        homePage.selectEnglishLanguageOnly();

        step("Go to the payment page");
        landingPage.goToPaymentPage(orderIdCreated, orderHashCreated);

        step("Verify payment page is loaded or not");
        Assertion.assertThat("Verify the payment page is loaded or not",
                landingPage.isPaymentPageLoaded(), is(true));

        step("Change payment method");
        landingPage.clickOnSelectPaymentMethod();

        step("Scroll down to the payment Group");
        landingPage.goToPaymentMethodsGroup(paymentGroup);

        step("Select Payment Method");
        landingPage.selectPaymentMethod(paymentMethod);

        step("Click in promo suggestion");
        landingPage.clickPromoSuggestion();

        step("input text in promo box");
        landingPage.inputPromoInInputField(promoCodeName);

        step("Click apply button");
        landingPage.clickApplyButton();

        step("Verify promo instant is applied or not");
        Assertion.assertThat("Verify promo instant is applied or not",
                landingPage.isPromoInstantApplied(wordingApplied), is(true));
    }

    @Author(names = { "Rounit", "Vidhi Jindal" })
    @Module(name = "PromoPayment")
    @Tribe(name = "Promo")
    @Vertical(name = "Promo")
    @Test
    public void verifyPromoSuggestionForNonLoginCase() throws InterruptedException, FailedLoginException {
        Map<String, String> map = getParam("verifyPromoSuggestionForNonLoginCase");
        BaseTest.paramsThreadLocal.set(map);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        PaymentPageWeb<?> landingPage = PaymentPageWeb.getInstance(session);
        orderCreationPage<?> orderCreationPage = new orderCreationPage<>(session);

        String email = map.get("email");
        String currency = map.get("currency");
        String productUrlName = map.get("productUrlName");
        String passwordDweb = map.get("passwordDweb");
        String[] salutationList = { "Mr", "Mrs", "Ms" };
        String fullName = randomName() + " Autorder";
        String nameTitle = getRandomObject(salutationList);

        step("Create Event/Todo order via API and fetch the orderID");
        orderCreationPage.createGuestWaitingPaymentOrderApiTodo(productUrlName, true, currency, fullName, nameTitle, fullName, email, nameTitle);
        String orderIdCreated = String.valueOf(orderCreationPage.getOrderId());
        String orderHashCreated = orderCreationPage.getOrderHash();

        step("User open home page");
        homePage.goToHomePage();

        step("Logout via URL");
        loginPage.logoutViaUrl();

        step("Select english language if not in english");
        homePage.selectEnglishLanguageOnly();

        step("Go to the payment page");
        landingPage.goToPaymentPage(orderIdCreated, orderHashCreated);

        step("Verify payment page is loaded or not");
        Assertion.assertThat("Verify the payment page is loaded or not", landingPage.isPaymentPageLoaded(), is(true));

        step("Click on promo suggestion");
        landingPage.clickPromoSuggestion();

        step("Verify Eligible Promo List");
        Assertion.assertThat("Verify Eligible Promo List", landingPage.verifyEligiblePromo(), is(true));

        // No more non eligible promo list in the newest UI
    }

    @Author(names = { "Benny", "Vidhi Jindal" })
    @Module(name = "PromoPayment")
    @Tribe(name = "Promo")
    @Vertical(name = "Promo")
    @Test
    public void verifyValidationInvalidDiscountCode() throws InterruptedException, FailedLoginException {
        Map<String, String> map = getParam("verifyValidationInvalidDiscountCode");
        BaseTest.paramsThreadLocal.set(map);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        PaymentPageWeb<?> landingPage = PaymentPageWeb.getInstance(session);
        orderCreationPage<?> orderCreationPage = new orderCreationPage<>(session);

        String email = map.get("email");
        String currency = map.get("currency");
        String productUrlName = map.get("productUrlName");
        String passwordDweb = map.get("passwordDweb");
        String[] salutationList = { "Mr", "Mrs", "Ms" };
        String fullName = randomName() + " Autorder";
        String nameTitle = getRandomObject(salutationList);
        String expectedErrorMessage = map.get("expectedErrorMessage");
        String promoCodeName = map.get("promoCodeName");

        step("Create Event/Todo order via API and fetch the orderID");
        orderCreationPage.createGuestWaitingPaymentOrderApiTodo(productUrlName, true, currency, fullName, nameTitle, fullName, email, nameTitle);
        String orderIdCreated = String.valueOf(orderCreationPage.getOrderId());
        String orderHashCreated = orderCreationPage.getOrderHash();

        step("Login as " + email + " via session injection");
        landingPage.maximize();
        loginPage.loginWithSpecificAccountViaCookie(email, passwordDweb);


        step("Select english language if not in english");
        homePage.selectEnglishLanguageOnly();

        step("Go to the payment page");
        landingPage.goToPaymentPage(orderIdCreated, orderHashCreated);

        step("Verify payment page is loaded or not");
        Assertion.assertThat("Verify the payment page is loaded or not", landingPage.isPaymentPageLoaded(), is(true));

        step("Click in promo suggestion");
        landingPage.clickPromoSuggestion();

        step("input text in promo box");
        landingPage.inputPromoInInputField(promoCodeName);

        step("Click apply button");
        landingPage.clickApplyButton();

        step("Verify validation error invalid discount code");
        Assertion.assertThat("Verify validation error invalid discount code",
                landingPage.isValidationCorrect(expectedErrorMessage), is(true));
    }

    @Author(names = { "Rounit", "Vidhi Jindal" })
    @Module(name = "PromoPayment")
    @Tribe(name = "Promo")
    @Vertical(name = "Promo")
    @Test
    public void verifyUnapplyPromoDueToChangeIntoTiketPoints() throws InterruptedException, FailedLoginException {
        Map<String, String> map = getParam("verifyUnapplyPromoDueToChangeIntoTiketPoints");
        BaseTest.paramsThreadLocal.set(map);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        PaymentPageWeb<?> landingPage = PaymentPageWeb.getInstance(session);
        orderCreationPage<?> orderCreationPage = new orderCreationPage<>(session);

        String email = map.get("email");
        String currency = map.get("currency");
        String productUrlName = map.get("productUrlName");
        String passwordDweb = map.get("passwordDweb");
        String[] salutationList = { "Mr", "Mrs", "Ms" };
        String fullName = randomName() + " Autorder";
        String nameTitle = getRandomObject(salutationList);
        String promoCodeName = map.get("promoCodeName");
        String wordingApplied = map.get("wordingApplied");
        String wordingCodeApplied = map.get("wordingCodeApplied");

        step("Create Event/Todo order via API and fetch the orderID");
        orderCreationPage.createGuestWaitingPaymentOrderApiTodo(productUrlName, true, currency, fullName, nameTitle, fullName, email, nameTitle);
        String orderIdCreated = String.valueOf(orderCreationPage.getOrderId());
        String orderHashCreated = orderCreationPage.getOrderHash();

        step("Login as " + email + " via session injection");
        landingPage.maximize();
        loginPage.loginWithSpecificAccountViaCookie(email, passwordDweb);


        step("Select english language if not in english");
        homePage.selectEnglishLanguageOnly();

        step("Go to the payment page");
        landingPage.goToPaymentPage(orderIdCreated, orderHashCreated);

        step("Verify payment page is loaded or not");
        Assertion.assertThat("Verify the payment page is loaded or not", landingPage.isPaymentPageLoaded(), is(true));

        step("Click in promo suggestion");
        landingPage.clickPromoSuggestion();

        step("input text in promo box");
        landingPage.inputPromoInInputField(promoCodeName);

        step("Click apply button");
        landingPage.clickApplyButton();

        step("Click On Tiket Points Button");
        landingPage.clickOnTiketPoints();

        step("Click on Continue With Points to UnApply Promo");
        landingPage.clickContinueWithPoints();

        step("Verify Promo/Gift Voucher instant is UnApplied or not");
        Assertion.assertThat("Verify Promo/Gift Voucher instant is UnApplied or not",
                landingPage.isPromoInstantUnApplied(), is(true));
    }

    @Author(names = { "Rounit", "Vidhi Jindal" })
    @Module(name = "PromoPayment")
    @Tribe(name = "Promo")
    @Vertical(name = "Promo")
    @Test
    public void verifyUnapplyGiftVoucherFlow() throws InterruptedException, FailedLoginException {
        Map<String, String> map = getParam("verifyUnapplyGiftVoucherFlow");
        BaseTest.paramsThreadLocal.set(map);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        PaymentPageWeb<?> landingPage = PaymentPageWeb.getInstance(session);
        orderCreationPage<?> orderCreationPage = new orderCreationPage<>(session);

        String email = map.get("email");
        String currency = map.get("currency");
        String productUrlName = map.get("productUrlName");
        String passwordDweb = map.get("passwordDweb");
        String[] salutationList = { "Mr", "Mrs", "Ms" };
        String fullName = randomName() + " Autorder";
        String nameTitle = getRandomObject(salutationList);
        String promoCodeName = map.get("promoCodeName");
        String wordingApplied = map.get("wordingApplied");
        String wordingCodeApplied = map.get("wordingCodeApplied");

        step("Create Event/Todo order via API and fetch the orderID");
        orderCreationPage.createGuestWaitingPaymentOrderApiTodo(productUrlName, true, currency, fullName, nameTitle, fullName, email, nameTitle);
        String orderIdCreated = String.valueOf(orderCreationPage.getOrderId());
        String orderHashCreated = orderCreationPage.getOrderHash();

        step("Login as " + email + " via session injection");
        landingPage.maximize();
        loginPage.loginWithSpecificAccountViaCookie(email, passwordDweb);


        step("Select english language if not in english");
        homePage.selectEnglishLanguageOnly();

        step("Go to the payment page");
        landingPage.goToPaymentPage(orderIdCreated, orderHashCreated);

        step("Verify payment page is loaded or not");
        Assertion.assertThat("Verify the payment page is loaded or not", landingPage.isPaymentPageLoaded(), is(true));

        step("Click in promo suggestion");
        landingPage.clickPromoSuggestion();

        step("input text in promo box");
        landingPage.inputPromoInInputField(promoCodeName);

        step("Click apply button");
        landingPage.clickApplyButton();

        step("Click UnApply button");
        landingPage.clickUnApplyButton();

        step("Click on Remove Promo button");
        landingPage.clickRemovePromo();

        step("Verify Gift Voucher instant is UnApplied or not");
        Assertion.assertThat("Verify Gift Voucher instant is UnApplied or not", landingPage.isPromoInstantUnApplied(),
                is(true));
    }

    @Author(names = { "Rounit", "Vidhi Jindal" })
    @Module(name = "PromoPayment")
    @Tribe(name = "Promo")
    @Vertical(name = "Promo")
    @Test
    public void verifyUnapplyFlowDueToInvalidPaymentMethod() throws InterruptedException, FailedLoginException {
        Map<String, String> map = getParam("verifyUnapplyFlowDueToInvalidPaymentMethod");
        BaseTest.paramsThreadLocal.set(map);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        PaymentPageWeb<?> landingPage = PaymentPageWeb.getInstance(session);
        orderCreationPage<?> orderCreationPage = new orderCreationPage<>(session);

        String email = map.get("email");
        String currency = map.get("currency");
        String productUrlName = map.get("productUrlName");
        String passwordDweb = map.get("passwordDweb");
        String[] salutationList = { "Mr", "Mrs", "Ms" };
        String fullName = randomName() + " Autorder";
        String nameTitle = getRandomObject(salutationList);
        String promoCodeName = map.get("promoCodeName");
        String wordingApplied = map.get("wordingApplied");
        String wordingCodeApplied = map.get("wordingCodeApplied");
        String paymentMethod = map.get("paymentMethod");

        step("Create Event/Todo order via API and fetch the orderID");
        orderCreationPage.createGuestWaitingPaymentOrderApiTodo(productUrlName, true, currency, fullName, nameTitle, fullName, email, nameTitle);
        String orderIdCreated = String.valueOf(orderCreationPage.getOrderId());
        String orderHashCreated = orderCreationPage.getOrderHash();

        step("Login as " + email + " via session injection");
        landingPage.maximize();
        loginPage.loginWithSpecificAccountViaCookie(email, passwordDweb);


        step("Select english language if not in english");
        homePage.selectEnglishLanguageOnly();

        step("Go to the payment page");
        landingPage.goToPaymentPage(orderIdCreated, orderHashCreated);

        step("Verify payment page is loaded or not");
        Assertion.assertThat("Verify the payment page is loaded or not", landingPage.isPaymentPageLoaded(), is(true));

        step("Click in promo suggestion");
        landingPage.clickPromoSuggestion();

        step("input text in promo box");
        landingPage.inputPromoInInputField(promoCodeName);

        step("Click apply button");
        landingPage.clickApplyButton();

        step("Click Change Payment Method Button");
        landingPage.clickChangePaymentMethod();

        step("Click on See All Payment Method Button");
        landingPage.clickOnSeeAll();

        step("Select Payment Method");
        landingPage.clickDiffPaymentMethod();

        step("Click Continue Without Promo");
        landingPage.clickContinueWithoutPromo();

        step("Verify Promo instant is UnApplied or not");
        Assertion.assertThat("Verify Promo instant is UnApplied or not", landingPage.isPromoInstantUnApplied(),
                is(true));
    }

    @Author(names = { "Rounit", "Vidhi Jindal" })
    @Module(name = "PromoPayment")
    @Tribe(name = "Promo")
    @Vertical(name = "Promo")
    @Test
    public void verifyPSv3FlowMultipleChangePaymentMethod() throws InterruptedException, FailedLoginException {
        Map<String, String> map = getParam("verifyPSv3FlowMultipleChangePaymentMethod");
        BaseTest.paramsThreadLocal.set(map);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        PaymentPageWeb<?> landingPage = PaymentPageWeb.getInstance(session);
        orderCreationPage<?> orderCreationPage = new orderCreationPage<>(session);

        String email = map.get("email");
        String currency = map.get("currency");
        String productUrlName = map.get("productUrlName");
        String passwordDweb = map.get("passwordDweb");
        String[] salutationList = { "Mr", "Mrs", "Ms" };
        String fullName = randomName() + " Autorder";
        String nameTitle = getRandomObject(salutationList);
        String promoCodeName = map.get("promoCodeName");
        String wordingApplied = map.get("wordingApplied");
        String wordingCodeApplied = map.get("wordingCodeApplied");

        step("Create Event/Todo order via API and fetch the orderID");
        orderCreationPage.createGuestWaitingPaymentOrderApiTodo(productUrlName, true, currency, fullName, nameTitle, fullName, email, nameTitle);
        String orderIdCreated = String.valueOf(orderCreationPage.getOrderId());
        String orderHashCreated = orderCreationPage.getOrderHash();

        step("Login as " + email + " via session injection");
        landingPage.maximize();
        loginPage.loginWithSpecificAccountViaCookie(email, passwordDweb);


        step("Select english language if not in english");
        homePage.selectEnglishLanguageOnly();

        step("Go to the payment page");
        landingPage.goToPaymentPage(orderIdCreated, orderHashCreated);

        step("Verify payment page is loaded or not");
        Assertion.assertThat("Verify the payment page is loaded or not", landingPage.isPaymentPageLoaded(), is(true));

        step("Click in promo suggestion");
        landingPage.clickPromoSuggestion();

        step("input text in promo box");
        landingPage.inputPromoInInputField(promoCodeName);

        step("Click apply button");
        landingPage.clickApplyButton();

        step("Click on CHANGE PAYMENT METHOD BUTTON in the Pop-up");
        landingPage.clickChangePaymentMethod();

        step("Click on First Payment Method");
        landingPage.clickOnFirstPaymentMethod();

        step("Verify promo instant is applied or not");
        Assertion.assertThat("Verify promo instant is applied or not",
                landingPage.isPromoInstantApplied(wordingApplied), is(true));
    }

    @Author(names = { "Vidhi Jindal" })
    @Module(name = "PromoPayment")
    @Tribe(name = "Promo")
    @Vertical(name = "Promo")
    @Test
    public void verifyPromoCashbackTillPaid() throws InterruptedException, FailedLoginException {
        Map<String, String> map = getParam("verifyPromoCashbackTillPaid");
        BaseTest.paramsThreadLocal.set(map);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        PaymentPageWeb<?> landingPage = PaymentPageWeb.getInstance(session);
        orderCreationPage<?> orderCreationPage = new orderCreationPage<>(session);

        String email = map.get("email");
        String currency = map.get("currency");
        String productUrlName = map.get("productUrlName");
        String passwordDweb = map.get("passwordDweb");
        String[] salutationList = { "Mr", "Mrs", "Ms" };
        String fullName = randomName() + " Autorder";
        String nameTitle = getRandomObject(salutationList);
        String promoCodeName = map.get("promoCodeName");
        String wordingApplied = map.get("wordingApplied");
        String wordingCodeApplied = map.get("wordingCodeApplied");
        String paymentGroup = map.get("paymentGroup");
        String paymentMethod = map.get("paymentMethod");

        step("Create Event/Todo order via API and fetch the orderID");
        orderCreationPage.createGuestWaitingPaymentOrderApiTodo(productUrlName, true, currency, fullName, nameTitle, fullName, email, nameTitle);
        String orderIdCreated = String.valueOf(orderCreationPage.getOrderId());
        String orderHashCreated = orderCreationPage.getOrderHash();

        step("Login as " + email + " via session injection");
        landingPage.maximize();
        loginPage.loginWithSpecificAccountViaCookie(email, passwordDweb);


        step("Select english language if not in english");
        homePage.selectEnglishLanguageOnly();

        step("Go to the payment page");
        landingPage.goToPaymentPage(orderIdCreated, orderHashCreated);

        step("Verify payment page is loaded or not");
        Assertion.assertThat("Verify the payment page is loaded or not",
                landingPage.isPaymentPageLoaded(), is(true));

        step("Change payment method");
        landingPage.clickOnSelectPaymentMethod();

        step("Scroll down to the payment Group");
        landingPage.goToPaymentMethodsGroup(paymentGroup);

        step("Select Payment Method");
        landingPage.selectPaymentMethod(paymentMethod);

        step("Click in promo suggestion");
        landingPage.clickPromoSuggestion();

        step("input text in promo box");
        landingPage.inputPromoInInputField(promoCodeName);

        step("Click apply button");
        landingPage.clickApplyButton();

        step("Verify promo cashback is applied.");
        Assertion.assertThat("Selected payment method is as per the promo settings in landing payment page",
                landingPage.isPromoInstantApplied(wordingApplied), is(true));
    }

    @Author(names = { "Vidhi Jindal" })
    @Module(name = "PromoPayment")
    @Tribe(name = "Promo")
    @Vertical(name = "Promo")
    @Test
    public void verifyPromoMultiCurrencyTillPaid() throws InterruptedException, FailedLoginException {
        Map<String, String> map = getParam("verifyPromoMultiCurrencyTillPaid");
        BaseTest.paramsThreadLocal.set(map);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        PaymentPageWeb<?> landingPage = PaymentPageWeb.getInstance(session);
        orderCreationPage<?> orderCreationPage = new orderCreationPage<>(session);

        String email = map.get("email");
        String passwordDweb = map.get("passwordDweb");
        String currency = map.get("currency");
        String productUrlName = map.get("productUrlName");
        String[] salutationList = { "Mr", "Mrs", "Ms" };
        String fullName = randomName() + " Autorder";
        String nameTitle = getRandomObject(salutationList);
        String promoCodeName = map.get("promoCodeName");
        String wordingApplied = map.get("wordingApplied");
        String wordingCodeApplied = map.get("wordingCodeApplied");
        String paymentGroup = map.get("paymentGroup");
        String paymentMethod = map.get("paymentMethod");
        String cardNumber = map.get("cardNumber");
        String cardMonth = map.get("cardMonth");
        String cvv = map.get("cvv");
        String cardHolderName = map.get("cardHolderName");

        step("Create Event/Todo order via API and fetch the orderID");
        orderCreationPage.createGuestWaitingPaymentOrderApiTodo(productUrlName, true, currency, fullName, nameTitle, fullName, email, nameTitle);
        String orderIdCreated = String.valueOf(orderCreationPage.getOrderId());
        String orderHashCreated = orderCreationPage.getOrderHash();

        step("Login as " + email + " via session injection");
        landingPage.maximize();
        loginPage.loginWithSpecificAccountViaCookie(email, passwordDweb);


        step("Select english language if not in english");
        homePage.selectEnglishLanguageOnly();

        step("User select non-IDR/multi currency");
        homePage.selectCurrency(currency);

        step("Go to the payment page");
        landingPage.goToPaymentPage(orderIdCreated, orderHashCreated);

        step("Verify payment page is loaded or not");
        Assertion.assertThat("Verify the payment page is loaded or not",
                landingPage.isPaymentPageLoaded(), is(true));

        step("Change payment method");
        landingPage.clickOnSelectPaymentMethod();

        step("Scroll down to the payment Group");
        landingPage.goToPaymentMethodsGroup(paymentGroup);

        step("Select Payment Method");
        landingPage.selectPaymentMethod(paymentMethod);

        step("Input the card details");
        landingPage.inputCardNumber(cardNumber);
        landingPage.inputCardMonth(cardMonth);
        landingPage.inputCardCvv(cvv);
        landingPage.inputCardHolderName(cardHolderName);

        step("Click in promo suggestion");
        landingPage.clickPromoSuggestion();

        step("input text in promo box");
        landingPage.inputPromoInInputField(promoCodeName);

        step("Click apply button");
        landingPage.clickApplyButton();

        step("Verify multicurrency promo is applied or not");
        Assertion.assertThat("Verify multicurrency promo is applied or not",
                landingPage.isPromoInstantApplied(wordingApplied), is(true));
    }

    @Author(names = { "Vidhi Jindal" })
    @Module(name = "PromoPayment")
    @Tribe(name = "Promo")
    @Vertical(name = "Promo")
    @Test
    public void verifyValidationErrorPromoOutOfStock() throws InterruptedException, FailedLoginException {
        Map<String, String> map = getParam("verifyValidationErrorPromoOutOfStock");
        BaseTest.paramsThreadLocal.set(map);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        PaymentPageWeb<?> landingPage = PaymentPageWeb.getInstance(session);
        orderCreationPage<?> orderCreationPage = new orderCreationPage<>(session);

        String email = map.get("email");
        String currency = map.get("currency");
        String productUrlName = map.get("productUrlName");
        String passwordDweb = map.get("passwordDweb");
        String[] salutationList = { "Mr", "Mrs", "Ms" };
        String fullName = randomName() + " Autorder";
        String nameTitle = getRandomObject(salutationList);
        String errorMsgEN = map.get("errorMsgEN");
        String errorMsgID = map.get("errorMsgID");
        String promoCodeName = map.get("promoCodeName");

        step("Create Event/Todo order via API and fetch the orderID");
        orderCreationPage.createGuestWaitingPaymentOrderApiTodo(productUrlName, true, currency, fullName, nameTitle, fullName, email, nameTitle);
        String orderIdCreated = String.valueOf(orderCreationPage.getOrderId());
        String orderHashCreated = orderCreationPage.getOrderHash();

        step("Verify the order is created successfully");
        Assertion.assertThat("Verify the order is created", orderIdCreated != null && !orderIdCreated.isEmpty(),
                is(true));

        step("Login as " + email + " via session injection");
        landingPage.maximize();
        loginPage.loginWithSpecificAccountViaCookie(email, passwordDweb);


        step("Select English language if not already set");
        homePage.selectEnglishLanguageOnly();

        step("Go to the payment page using the created order ID and hash");
        landingPage.goToPaymentPage(orderIdCreated, orderHashCreated);

        step("Verify payment page is loaded successfully");
        Assertion.assertThat("Verify the payment page is loaded", landingPage.isPaymentPageLoaded(), is(true));

        step("Click on promo suggestion for applying the promo code");
        landingPage.clickPromoSuggestion();

        step("Input a promo code.");
        landingPage.inputPromoInInputField(promoCodeName);

        step("Click the apply button to apply the promo code");
        landingPage.clickApplyButton();

        step("Verify validation error is displayed due to the promo code out of stock.");
        Assertion.assertThat("Verify validation error for promo code out of stock",
                landingPage.verifyErrorMsg(errorMsgEN, errorMsgID), is(true));
    }

    @Author(names = { "Vidhi Jindal" })
    @Module(name = "PromoPayment")
    @Tribe(name = "Promo")
    @Vertical(name = "Promo")
    @Test
    public void verifyApplyDebitCardSpecificBIN() throws InterruptedException, FailedLoginException {
        Map<String, String> map = getParam("verifyApplyDebitCardSpecificBIN");
        BaseTest.paramsThreadLocal.set(map);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        PaymentPageWeb<?> landingPage = PaymentPageWeb.getInstance(session);
        GiftCardPageWeb<?> giftCardPage = GiftCardPageWeb.getInstance(session);

        String email = map.get("email");
        String passwordDweb = map.get("passwordDweb");

        String[] salutationList = { "Mr", "Mrs", "Ms" };
        String fullName = randomName() + " Autorder";
        String title = getRandomObject(salutationList);
        String currency = map.get("currency");
        int voucherAmount = Integer.parseInt(map.get("voucherAmount"));
        String profileId = map.get("apiProfileId");
        String phone = map.get("phone");
        String promoCodeName = map.get("promoCodeName");
        String wordingApplied = map.get("wordingApplied");
        String wordingCodeApplied = map.get("wordingCodeApplied");
        String paymentGroup = map.get("paymentGroup");
        String paymentMethod = map.get("paymentMethod");
        String cardNumber = map.get("cardNumber");
        String cardMonth = map.get("cardMonth");
        String cvv = map.get("cvv");
        String cardHolderName = map.get("cardHolderName");

        step("Create Gift Card order via API and fetch the orderID");
        GiftCardOrderResponse orderDetailsData = giftCardPage.createGiftCardOrderViaAPI(currency, voucherAmount,
                profileId, email, fullName, phone, title, email, passwordDweb);

        step("Fetch Order Id & Hash");
        GiftCardOrderResponse.Order orderDetails = orderDetailsData.order;
        String orderId = orderDetails.orderId;
        String orderHash = orderDetails.orderHash;

        step("Login as " + email + " via session injection");
        landingPage.maximize();
        loginPage.loginWithSpecificAccountViaCookie(email, passwordDweb);


        step("Select english language if not in english");
        homePage.selectEnglishLanguageOnly();

        step("Go to the payment page");
        landingPage.goToPaymentPage(orderId, orderHash);

        step("Verify payment page is loaded or not");
        Assertion.assertThat("Verify the payment page is loaded or not", landingPage.isPaymentPageLoaded(), is(true));

        step("Change payment method");
        landingPage.clickOnSelectPaymentMethod();

        step("Scroll down to the payment Group");
        landingPage.goToPaymentMethodsGroup(paymentGroup);

        step("Select Payment Method");
        landingPage.selectPaymentMethod(paymentMethod);

        step("Input the card details");
        landingPage.inputCardNumber(cardNumber);
        landingPage.inputCardMonth(cardMonth);
        landingPage.inputCardCvv(cvv);
        landingPage.inputCardHolderName(cardHolderName);

        step("Click in promo suggestion");
        landingPage.clickPromoSuggestion();

        step("input text in promo box");
        landingPage.inputPromoInInputField(promoCodeName);

        step("Click apply button");
        landingPage.clickApplyButton();

        step("Verify promo is applied or not");
        Assertion.assertThat("Verify promo is applied or not",
                landingPage.isPromoInstantApplied(wordingApplied), is(true));
    }

    @Author(names = { "Vidhi Jindal" })
    @Module(name = "PromoPayment")
    @Tribe(name = "Promo")
    @Vertical(name = "Promo")
    @Test
    public void verifyCheckAvbErrorTenorNotValid() throws InterruptedException, FailedLoginException {
        Map<String, String> map = getParam("verifyCheckAvbErrorTenorNotValid");
        BaseTest.paramsThreadLocal.set(map);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        PaymentPageWeb<?> landingPage = PaymentPageWeb.getInstance(session);
        GiftCardPageWeb<?> giftCardPage = GiftCardPageWeb.getInstance(session);

        String email = map.get("email");
        String currency = map.get("currency");
        String passwordDweb = map.get("passwordDweb");
        String[] salutationList = { "Mr", "Mrs", "Ms" };
        String fullName = randomName() + " Autorder";
        String title = getRandomObject(salutationList);
        int voucherAmount = Integer.parseInt(map.get("voucherAmount"));
        String profileId = map.get("apiProfileId");
        String phone = map.get("phone");
        String promoCodeName = map.get("promoCodeName");
        String wordingApplied = map.get("wordingApplied");
        String wordingCodeApplied = map.get("wordingCodeApplied");
        String errorMsgEN = map.get("errorMsgEN");
        String errorMsgID = map.get("errorMsgID");
        String paymentGroup = map.get("paymentGroup");
        String paymentMethod = map.get("paymentMethod");
        String cardNumber = map.get("cardNumber");
        String cardMonth = map.get("cardMonth");
        String cvv = map.get("cvv");
        String cardHolderName = map.get("cardHolderName");

        step("Create Gift Card order via API and fetch the orderID");
        GiftCardOrderResponse orderDetailsData = giftCardPage.createGiftCardOrderViaAPI(currency, voucherAmount,
                profileId, email, fullName, phone, title, email, passwordDweb);

        step("Fetch Order Id & Hash");
        GiftCardOrderResponse.Order orderDetails = orderDetailsData.order;
        String orderId = orderDetails.orderId;
        String orderHash = orderDetails.orderHash;

        step("Login as " + email + " via session injection");
        landingPage.maximize();
        loginPage.loginWithSpecificAccountViaCookie(email, passwordDweb);


        step("Select english language if not in english");
        homePage.selectEnglishLanguageOnly();

        step("Go to the payment page");
        landingPage.goToPaymentPage(orderId, orderHash);

        step("Verify payment page is loaded or not");
        Assertion.assertThat("Verify the payment page is loaded or not", landingPage.isPaymentPageLoaded(), is(true));

        step("Change payment method");
        landingPage.clickOnSelectPaymentMethod();

        step("Scroll down to the payment Group");
        landingPage.goToPaymentMethodsGroup(paymentGroup);

        step("Select Payment Method");
        landingPage.selectPaymentMethod(paymentMethod);

        step("Input the card details");
        landingPage.inputCardNumber(cardNumber);
        landingPage.inputCardMonth(cardMonth);
        landingPage.inputCardCvv(cvv);
        landingPage.inputCardHolderName(cardHolderName);

        step("Change the tenure");
        landingPage.changeTenure();

        step("Click in promo suggestion");
        landingPage.clickPromoSuggestion();

        step("input text in promo box");
        landingPage.inputPromoInInputField(promoCodeName);

        step("Click apply button");
        landingPage.clickApplyButton();

        step("Verify promo code is applied or not");
        Assertion.assertThat("Verify promo code is applied.",
                landingPage.isPromoInstantApplied(wordingApplied), is(true));

        step("Change the tenure of Credit Card");
        landingPage.changeTenureToAnother();

        step("Verify Error Message");
        landingPage.verifyErrorPopUpMsgFunct(errorMsgEN);
    }

    @Author(names = { "Vidhi Jindal" })
    @Module(name = "PromoPayment")
    @Tribe(name = "Promo")
    @Vertical(name = "Promo")
    @Test
    public void verifyApplyFullGiftVoucherTillPaid()
            throws InterruptedException, FailedLoginException, ExecutionException {
        Map<String, String> map = getParam("verifyApplyFullGiftVoucherTillPaid");
        BaseTest.paramsThreadLocal.set(map);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        PaymentPageWeb<?> landingPage = PaymentPageWeb.getInstance(session);

        String email = map.get("email");
        String currency = map.get("currency");
        String productUrlName = map.get("productUrlName");
        String passwordDweb = map.get("passwordDweb");
        String[] salutationList = { "Mr", "Mrs", "Ms" };
        String fullName = randomName() + " Autorder";
        String nameTitle = getRandomObject(salutationList);
        String promoCodeName = map.get("promoCodeName");
        String wordingApplied = map.get("wordingApplied");
        String wordingCodeApplied = map.get("wordingCodeApplied");
        orderCreationPage<?> orderCreationPage = new orderCreationPage<>(session);

        String idempotentKey = orderCreationPage.generateIdempotentKey();
        String apiKey = map.get("apiKey");
        int discountAmount = Integer.parseInt(map.get("discountAmount"));
        int discountPercentage = Integer.parseInt(map.get("discountPercentage"));
        int maximumVoucherAmount = Integer.parseInt(map.get("maximumVoucherAmount"));
        int minimumTransactionAmount = Integer.parseInt(map.get("minimumTransactionAmount"));
        int cashbackAmount = Integer.parseInt(map.get("cashbackAmount"));
        int maximumVoucherCashbackAmount = Integer.parseInt(map.get("maximumVoucherCashbackAmount"));
        String discountScheme = map.get("discountScheme");
        String giftVoucherAdjustmentCode = map.getOrDefault("giftVoucherAdjustmentCode", "");
        List<String> userEmails = Arrays.asList(map.get("userEmails"));
        System.out.println("userEmails refundtest -> " + userEmails);
        List<String> validityRestriction = Arrays.asList(map.get("validityRestriction"));
        String startDate = generateRandomDate(map.get("rangeAddDate"));

        step("Create Event/Todo order via API and fetch the orderID");
        orderCreationPage.createGuestWaitingPaymentOrderApiTodo(productUrlName, true, currency, fullName, nameTitle, fullName, email, nameTitle);
        String orderIdCreated = String.valueOf(orderCreationPage.getOrderId());
        String orderHashCreated = orderCreationPage.getOrderHash();
        String gvCode = orderCreationPage.createGvApi(email, idempotentKey, apiKey, discountAmount, cashbackAmount,
                maximumVoucherCashbackAmount, discountPercentage, maximumVoucherAmount, minimumTransactionAmount, "IDR",
                startDate, discountScheme, giftVoucherAdjustmentCode, userEmails,
                validityRestriction).data.giftVoucherCode;

        step("Login as " + email + " via session injection");
        landingPage.maximize();
        loginPage.loginWithSpecificAccountViaCookie(email, passwordDweb);


        step("Select english language if not in english");
        homePage.selectEnglishLanguageOnly();

        step("Go to the payment page");
        landingPage.goToPaymentPage(orderIdCreated, orderHashCreated);

        step("Verify payment page is loaded or not");
        Assertion.assertThat("Verify the payment page is loaded or not", landingPage.isPaymentPageLoaded(), is(true));

        step("Click in promo suggestion");
        landingPage.clickPromoSuggestion();

        step("input text in promo box");
        landingPage.inputPromoInInputField(gvCode);

        step("Click apply button");
        landingPage.clickApplyButton();

        step("Verify gift voucher is applied or not");
        Assertion.assertThat("Verify gift voucher is applied.",
                landingPage.isPromoInstantApplied(wordingApplied), is(true));

        step("Verify Order price is free or not.");
        Assertion.assertThat("Verify Order price is free.", landingPage.isOrderFree(), is(true));
    }

    @Author(names = { "Vidhi Jindal" })
    @Module(name = "PromoPayment")
    @Tribe(name = "Promo")
    @Vertical(name = "Promo")
    @Test
    public void verifyApplyCreditCardSpecificBIN() throws InterruptedException, FailedLoginException {
        Map<String, String> map = getParam("verifyApplyCreditCardSpecificBIN");
        BaseTest.paramsThreadLocal.set(map);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        PaymentPageWeb<?> landingPage = PaymentPageWeb.getInstance(session);
        GiftCardPageWeb<?> giftCardPage = GiftCardPageWeb.getInstance(session);

        String email = map.get("email");
        String passwordDweb = map.get("passwordDweb");

        String[] salutationList = { "Mr", "Mrs", "Ms" };
        String fullName = randomName() + " Autorder";
        String title = getRandomObject(salutationList);
        String currency = map.get("currency");
        int voucherAmount = Integer.parseInt(map.get("voucherAmount"));
        String profileId = map.get("apiProfileId");
        String phone = map.get("phone");
        String promoCodeName = map.get("promoCodeName");
        String wordingApplied = map.get("wordingApplied");
        String wordingCodeApplied = map.get("wordingCodeApplied");
        String paymentGroup = map.get("paymentGroup");
        String paymentMethod = map.get("paymentMethod");

        step("Create Gift Card order via API and fetch the orderID");
        GiftCardOrderResponse orderDetailsData = giftCardPage.createGiftCardOrderViaAPI(currency, voucherAmount,
                profileId, email, fullName, phone, title, email, passwordDweb);

        step("Fetch Order Id & Hash");
        GiftCardOrderResponse.Order orderDetails = orderDetailsData.order;
        String orderId = orderDetails.orderId;
        String orderHash = orderDetails.orderHash;

        step("Login as " + email + " via session injection");
        landingPage.maximize();
        loginPage.loginWithSpecificAccountViaCookie(email, passwordDweb);


        step("Select english language if not in english");
        homePage.selectEnglishLanguageOnly();

        step("Go to the payment page");
        landingPage.goToPaymentPage(orderId, orderHash);

        step("Verify payment page is loaded or not");
        Assertion.assertThat("Verify the payment page is loaded or not", landingPage.isPaymentPageLoaded(), is(true));

        step("Change payment method");
        landingPage.clickOnSelectPaymentMethod();

        step("Scroll down to the payment Group");
        landingPage.goToPaymentMethodsGroup(paymentGroup);

        step("Select Payment Method");
        landingPage.selectPaymentMethodCCTokenize(paymentMethod);

        step("Click in promo suggestion");
        landingPage.clickPromoSuggestion();

        step("input text in promo box");
        landingPage.inputPromoInInputField(promoCodeName);

        step("Click apply button");
        landingPage.clickApplyButton();

        step("Verify promo is applied or not");
        Assertion.assertThat("Verify promo is applied or not",
                landingPage.isPromoInstantApplied(wordingApplied), is(true));
    }

    @Author(names = { "Vidhi Jindal" })
    @Module(name = "PromoPayment")
    @Tribe(name = "Promo")
    @Vertical(name = "Promo")
    @Test
    public void verifyApplyB2BPromo() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("verifyApplyB2BPromo");
        BaseTest.paramsThreadLocal.set(param);
        Session<WebDriver> session = sessionThreadLocal.get();
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HotelLandingPageWeb<?> hotelLandingPage = HotelLandingPageWeb.getInstance(session);
        HotelRoomListPage<?> hotelRoomListPage = new HotelRoomListPage<>(session);
        HotelBookingFormPageWeb<?> hotelBookingFormPage = HotelBookingFormPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        TravelPlanWeb<?> travelPlanPage = TravelPlanWeb.getInstance(session);

        String travelerUsername = param.get("travelerUsername");
        String travelerPassword = param.get("travelerPassword");
        String otp = param.get("otp");
        String location = param.get("location");
        String dateType = param.get("dateType");
        String date = param.get("date");
        String promoCodeName = param.get("promoCodeName");
        String wordingApplied = param.get("wordingApplied");
        String wordingCodeApplied = param.get("wordingCodeApplied");

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
        hotelRoomListPage.clickContinue();
        hotelBookingFormPage.selectPayment();
        travelPlanPage.skipTravelPlan();

        step("Payment page should be displayed");
        String orderID = paymentPage.getOrderId();
        int finalAmount = Integer.parseInt(paymentPage.getFinalPrice());
        Assertion.assertThat("Final amount should be greater than zero", finalAmount, is(greaterThan(0)));
        orderID = String.valueOf(getOrderId(orderID));
        Assertion.assertThat("Corporate billing option is selected", paymentPage.getSelectedPayment().toLowerCase(),
                is("corporate billing"));

        step("Click in promo suggestion");
        paymentPage.clickPromoSuggestion();

        step("input text in promo box");
        paymentPage.inputPromoInInputField(promoCodeName);

        step("Click apply button");
        paymentPage.clickApplyButton();

        step("Verify promo B2B is applied or not");
        Assertion.assertThat("Verify promo B2B is applied.",
                paymentPage.isPromoInstantApplied(wordingApplied, wordingCodeApplied), is(true));
    }

    @Author(names = { "Vidhi Jindal" })
    @Module(name = "PromoPayment")
    @Tribe(name = "Promo")
    @Vertical(name = "Promo")
    @Test
    public void verifyValidationErrorInvalidAccountID() throws InterruptedException, FailedLoginException {
        Map<String, String> map = getParam("verifyValidationErrorInvalidAccountID");
        BaseTest.paramsThreadLocal.set(map);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        PaymentPageWeb<?> landingPage = PaymentPageWeb.getInstance(session);
        orderCreationPage<?> orderCreationPage = new orderCreationPage<>(session);

        String email = map.get("email");
        String passwordDweb = map.get("passwordDweb");

        String[] salutationList = { "Mr", "Mrs", "Ms" };
        String fullName = randomName() + " Autorder";
        String nameTitle = getRandomObject(salutationList);
        String currency = map.get("currency");
        String productUrlName = map.get("productUrlName");
        String promoCodeName = map.get("promoCodeName");
        String errorMsgEN = map.get("errorMsgEN");
        String errorMsgID = map.get("errorMsgID");

        step("Create Event/Todo order via API and fetch the orderID");
        orderCreationPage.createGuestWaitingPaymentOrderApiTodo(productUrlName, true, currency, fullName, nameTitle, fullName, email, nameTitle);
        String orderIdCreated = String.valueOf(orderCreationPage.getOrderId());
        String orderHashCreated = orderCreationPage.getOrderHash();

        step("Login as " + email + " via session injection");
        landingPage.maximize();
        loginPage.loginWithSpecificAccountViaCookie(email, passwordDweb);


        step("Select english language if not in english");
        homePage.selectEnglishLanguageOnly();

        step("Go to the payment page");
        landingPage.goToPaymentPage(orderIdCreated, orderHashCreated);

        step("Verify payment page is loaded or not");
        Assertion.assertThat("Verify the payment page is loaded or not", landingPage.isPaymentPageLoaded(), is(true));

        step("Click in promo suggestion");
        landingPage.clickPromoSuggestion();

        step("input text in promo box");
        landingPage.inputPromoInInputField(promoCodeName);

        step("Click apply button");
        landingPage.clickApplyButton();

        step("Verify Error Message");
        Assertion.assertThat("Verify Error Message", landingPage.verifyErrorMsg(errorMsgEN, errorMsgID), is(true));
    }

    @Author(names = { "Vidhi Jindal" })
    @Module(name = "PromoPayment")
    @Tribe(name = "Promo")
    @Vertical(name = "Promo")
    @Test
    public void verifyApplyGVhybridAPIhelper() throws InterruptedException, FailedLoginException, ExecutionException {
        Map<String, String> map = getParam("verifyApplyGVhybridAPIhelper");
        BaseTest.paramsThreadLocal.set(map);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        PaymentPageWeb<?> landingPage = PaymentPageWeb.getInstance(session);
        orderCreationPage<?> orderCreationPage = new orderCreationPage<>(session);

        String email = map.get("email");
        String currency = map.get("currency");
        String productUrlName = map.get("productUrlName");
        String passwordDweb = map.get("passwordDweb");
        String[] salutationList = { "Mr", "Mrs", "Ms" };
        String fullName = randomName() + " Autorder";
        String nameTitle = getRandomObject(salutationList);
        String promoCodeName = map.get("promoCodeName");
        String wordingApplied = map.get("wordingApplied");
        String wordingCodeApplied = map.get("wordingCodeApplied");
        String userConsumer = map.get("userConsumer");

        String idempotentKey = orderCreationPage.generateIdempotentKey();
        String apiKey = map.get("apiKey");
        int discountAmount = Integer.parseInt(map.get("discountAmount"));
        int discountPercentage = Integer.parseInt(map.get("discountPercentage"));
        int maximumVoucherAmount = Integer.parseInt(map.get("maximumVoucherAmount"));
        int minimumTransactionAmount = Integer.parseInt(map.get("minimumTransactionAmount"));
        int cashbackAmount = Integer.parseInt(map.get("cashbackAmount"));
        int maximumVoucherCashbackAmount = Integer.parseInt(map.get("maximumVoucherCashbackAmount"));
        String discountScheme = map.get("discountScheme");
        String giftVoucherAdjustmentCode = map.getOrDefault("giftVoucherAdjustmentCode", "");
        List<String> userEmails = Arrays.asList(map.get("userEmails"));
        System.out.println("userEmails refundtest -> " + userEmails);
        List<String> validityRestriction = Arrays.asList(map.get("validityRestriction"));
        String startDate = generateRandomDate(map.get("rangeAddDate"));

        step("Create Event/Todo order via API and fetch the orderID");
        orderCreationPage.createGuestWaitingPaymentOrderApiTodo(productUrlName, true, currency, fullName, nameTitle, fullName, email, nameTitle);
        String orderIdCreated = String.valueOf(orderCreationPage.getOrderId());
        String orderHashCreated = orderCreationPage.getOrderHash();
        String gvCode = orderCreationPage.createGvApi(email, idempotentKey, apiKey, discountAmount, cashbackAmount,
                maximumVoucherCashbackAmount, discountPercentage, maximumVoucherAmount, minimumTransactionAmount, "IDR",
                startDate, discountScheme, giftVoucherAdjustmentCode, userEmails,
                validityRestriction).data.giftVoucherCode;

        step("Login as " + email + " via session injection");
        landingPage.maximize();
        loginPage.loginWithSpecificAccountViaCookie(email, passwordDweb);


        step("Select english language if not in english");
        homePage.selectEnglishLanguageOnly();

        step("Go to the payment page");
        landingPage.goToPaymentPage(orderIdCreated, orderHashCreated);

        step("Verify payment page is loaded or not");
        Assertion.assertThat("Verify the payment page is loaded or not", landingPage.isPaymentPageLoaded(), is(true));

        step("Click in promo suggestion");
        landingPage.clickPromoSuggestion();

        step("input text in promo box");
        landingPage.inputPromoInInputField(gvCode);

        step("Click apply button");
        landingPage.clickApplyButton();

        step("Verify gift voucher is applied or not");
        Assertion.assertThat("Verify gift voucher is applied.",
                landingPage.isPromoInstantApplied(wordingApplied), is(true));
    }

    @Author(names = { "Vidhi Jindal" })
    @Module(name = "PromoPayment")
    @Tribe(name = "Promo")
    @Vertical(name = "Promo")
    @Test
    public void verifyApplyGVAPIhelper() throws InterruptedException, FailedLoginException, ExecutionException {
        Map<String, String> map = getParam("verifyApplyGVAPIhelper");
        BaseTest.paramsThreadLocal.set(map);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        PaymentPageWeb<?> landingPage = PaymentPageWeb.getInstance(session);
        orderCreationPage<?> orderCreationPage = new orderCreationPage<>(session);

        String email = map.get("email");
        String currency = map.get("currency");
        String productUrlName = map.get("productUrlName");
        String passwordDweb = map.get("passwordDweb");
        String[] salutationList = { "Mr", "Mrs", "Ms" };
        String fullName = randomName() + " Autorder";
        String nameTitle = getRandomObject(salutationList);
        String promoCodeName = map.get("promoCodeName");
        String wordingApplied = map.get("wordingApplied");
        String wordingCodeApplied = map.get("wordingCodeApplied");

        String idempotentKey = orderCreationPage.generateIdempotentKey();
        String apiKey = map.get("apiKey");
        int discountAmount = Integer.parseInt(map.get("discountAmount"));
        int discountPercentage = Integer.parseInt(map.get("discountPercentage"));
        int maximumVoucherAmount = Integer.parseInt(map.get("maximumVoucherAmount"));
        int minimumTransactionAmount = Integer.parseInt(map.get("minimumTransactionAmount"));
        int cashbackAmount = Integer.parseInt(map.get("cashbackAmount"));
        int maximumVoucherCashbackAmount = Integer.parseInt(map.get("maximumVoucherCashbackAmount"));
        String discountScheme = map.get("discountScheme");
        String giftVoucherAdjustmentCode = map.getOrDefault("giftVoucherAdjustmentCode", "");
        List<String> userEmails = Arrays.asList(map.get("userEmails"));
        System.out.println("userEmails refundtest -> " + userEmails);
        List<String> validityRestriction = Arrays.asList(map.get("validityRestriction"));
        String startDate = generateRandomDate(map.get("rangeAddDate"));

        step("Create Event/Todo order via API and fetch the orderID");
        orderCreationPage.createGuestWaitingPaymentOrderApiTodo(productUrlName, true, currency, fullName, nameTitle, fullName, email, nameTitle);
        String orderIdCreated = String.valueOf(orderCreationPage.getOrderId());
        String orderHashCreated = orderCreationPage.getOrderHash();
        String gvCode = orderCreationPage.createGvApi(email, idempotentKey, apiKey, discountAmount, cashbackAmount,
                maximumVoucherCashbackAmount, discountPercentage, maximumVoucherAmount, minimumTransactionAmount, "IDR",
                startDate, discountScheme, giftVoucherAdjustmentCode, userEmails,
                validityRestriction).data.giftVoucherCode;

        step("Login as " + email + " via session injection");
        landingPage.maximize();
        loginPage.loginWithSpecificAccountViaCookie(email, passwordDweb);


        step("Select english language if not in english");
        homePage.selectEnglishLanguageOnly();

        step("Go to the payment page");
        landingPage.goToPaymentPage(orderIdCreated, orderHashCreated);

        step("Verify payment page is loaded or not");
        Assertion.assertThat("Verify the payment page is loaded or not", landingPage.isPaymentPageLoaded(), is(true));

        step("Click in promo suggestion");
        landingPage.clickPromoSuggestion();

        step("input text in promo box");
        landingPage.inputPromoInInputField(gvCode);

        step("Click apply button");
        landingPage.clickApplyButton();

        step("Verify gift voucher is applied or not");
        Assertion.assertThat("Verify gift voucher is applied.",
                landingPage.isPromoInstantApplied(wordingApplied), is(true));
    }

    @Author(names = { "Vidhi Jindal" })
    @Module(name = "PromoPayment")
    @Tribe(name = "Promo")
    @Vertical(name = "Promo")
    @Test
    public void verifyApplyHalfGVAdjustment() throws InterruptedException, FailedLoginException, ExecutionException {
        Map<String, String> map = getParam("verifyApplyHalfGVAdjustment");
        BaseTest.paramsThreadLocal.set(map);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        PaymentPageWeb<?> landingPage = PaymentPageWeb.getInstance(session);

        String email = map.get("email");
        String currency = map.get("currency");
        String productUrlName = map.get("productUrlName");
        String passwordDweb = map.get("passwordDweb");
        String[] salutationList = { "Mr", "Mrs", "Ms" };
        String fullName = randomName() + " Autorder";
        String nameTitle = getRandomObject(salutationList);
        String paymentGroup = map.get("paymentGroup");
        String paymentMethod = map.get("paymentMethod");
        String wordingApplied = map.get("wordingApplied");
        orderCreationPage<?> orderCreationPage = new orderCreationPage<>(session);

        String idempotentKey = orderCreationPage.generateIdempotentKey();
        String apiKey = map.get("apiKey");
        int discountAmount = Integer.parseInt(map.get("discountAmount"));
        int discountPercentage = Integer.parseInt(map.get("discountPercentage"));
        int maximumVoucherAmount = Integer.parseInt(map.get("maximumVoucherAmount"));
        int minimumTransactionAmount = Integer.parseInt(map.get("minimumTransactionAmount"));
        int cashbackAmount = Integer.parseInt(map.get("cashbackAmount"));
        int maximumVoucherCashbackAmount = Integer.parseInt(map.get("maximumVoucherCashbackAmount"));
        String discountScheme = map.get("discountScheme");
        String giftVoucherAdjustmentCode = map.getOrDefault("giftVoucherAdjustmentCode", "");
        List<String> userEmails = Arrays.asList(map.get("userEmails"));
        System.out.println("userEmails refundtest -> " + userEmails);
        List<String> validityRestriction = Arrays.asList(map.get("validityRestriction"));
        String startDate = generateRandomDate(map.get("rangeAddDate"));
        
        step("Create Event/Todo order via API and fetch the orderID");
        orderCreationPage.createGuestWaitingPaymentOrderApiTodo(productUrlName, true, currency, fullName, nameTitle, fullName, email, nameTitle);
        String orderIdCreated = String.valueOf(orderCreationPage.getOrderId());
        String orderHashCreated = orderCreationPage.getOrderHash();
        String gvCode = orderCreationPage.createGvApi(email, idempotentKey, apiKey, discountAmount, cashbackAmount,
                maximumVoucherCashbackAmount, discountPercentage, maximumVoucherAmount, minimumTransactionAmount, "IDR",
                startDate, discountScheme, giftVoucherAdjustmentCode, userEmails,
                validityRestriction).data.giftVoucherCode;

        step("Login as " + email + " via session injection");
        landingPage.maximize();
        loginPage.loginWithSpecificAccountViaCookie(email, passwordDweb);


        step("Select english language if not in english");
        homePage.selectEnglishLanguageOnly();

        step("Go to the payment page");
        landingPage.goToPaymentPage(orderIdCreated, orderHashCreated);

        step("Verify payment page is loaded or not");
        Assertion.assertThat("Verify the payment page is loaded or not", landingPage.isPaymentPageLoaded(), is(true));

        step("Change payment method");
        landingPage.clickOnSelectPaymentMethod();

        step("Scroll down to the payment Group");
        landingPage.goToPaymentMethodsGroup(paymentGroup);

        step("Select Payment Method");
        landingPage.selectPaymentMethod(paymentMethod);

        step("Click in promo suggestion");
        landingPage.clickPromoSuggestion();

        step("input text in promo box");
        landingPage.inputPromoInInputField(gvCode);

        step("Click apply button");
        landingPage.clickApplyButton();

        step("Verify gift voucher is applied or not");
        Assertion.assertThat("Verify gift voucher is applied.",
                landingPage.isPromoInstantApplied(wordingApplied), is(true));
    }

    @Author(names = { "Fery" })
    @Module(name = "PromoPayment")
    @Tribe(name = "Promo")
    @Vertical(name = "Promo")
    @Test
    public void verifyPromoMetadata() throws InterruptedException, FailedLoginException, ExecutionException {
        Map<String, String> map = getParam("verifyPromoMetadata");
        BaseTest.paramsThreadLocal.set(map);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        PaymentPageWeb<?> landingPage = PaymentPageWeb.getInstance(session);
        GiftCardPageWeb<?> giftCardPage = GiftCardPageWeb.getInstance(session);

        String email = map.get("email");
        String passwordDweb = map.get("passwordDweb");
        String currency = map.get("currency");
        int voucherAmount = Integer.parseInt(map.get("voucherAmount"));
        String profileId = map.get("profileId");
        String fullName = map.get("fullName");
        String phone = map.get("phone");
        String title = map.get("title");
        String promoCodeName = map.get("promoCodeName");
        String wordingApplied = map.get("wordingApplied");
        String wordingCodeApplied = map.get("wordingCodeApplied");

        step("Create Gift Card order via API and fetch the orderID");
        GiftCardOrderResponse orderDetailsData = giftCardPage.createGiftCardOrderViaAPI(currency, voucherAmount,
                profileId, email, fullName, phone, title, email, passwordDweb);

        step("Fetch Order Id & Hash");
        GiftCardOrderResponse.Order orderDetails = orderDetailsData.order;
        String orderId = orderDetails.orderId;
        String orderHash = orderDetails.orderHash;

        step("Login as " + email + " via session injection");
        landingPage.maximize();
        loginPage.loginWithSpecificAccountViaCookie(email, passwordDweb);


        step("Select english language if not in english");
        homePage.selectEnglishLanguageOnly();

        step("Go to the payment page");
        landingPage.goToPaymentPage(orderId, orderHash);

        step("Verify payment page is loaded or not");
        Assertion.assertThat("Verify the payment page is loaded or not", landingPage.isPaymentPageLoaded(), is(true));

        step("Click in promo suggestion");
        landingPage.clickPromoSuggestion();

        step("input text in promo box");
        landingPage.inputPromoInInputField(promoCodeName);

        step("Click apply button");
        landingPage.clickApplyButton();

        step("Verify promo is applied or not");
        Assertion.assertThat("Verify promo is applied.",
                landingPage.isPromoInstantApplied(wordingApplied), is(true));
    }

    @Author(names = { "Fery" })
    @Module(name = "PromoPayment")
    @Tribe(name = "Promo")
    @Vertical(name = "Promo")
    @Test
    public void verifyUsageCountUsername() throws InterruptedException, FailedLoginException, ExecutionException {
        Map<String, String> map = getParam("verifyUsageCountUsername");
        BaseTest.paramsThreadLocal.set(map);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        PaymentPageWeb<?> landingPage = PaymentPageWeb.getInstance(session);
        GiftCardPageWeb<?> giftCardPage = GiftCardPageWeb.getInstance(session);

        String email = map.get("email");
        String passwordDweb = map.get("passwordDweb");
        String currency = map.get("currency");
        int voucherAmount = Integer.parseInt(map.get("voucherAmount"));
        String profileId = map.get("profileId");
        String fullName = map.get("fullName");
        String phone = map.get("phone");
        String title = map.get("title");
        String promoCodeName = map.get("promoCodeName");

        String accountId = map.get("accountId");
        String uniqueLinkName = map.get("uniqueLinkName");
        String quotaLevel = map.get("quotaLevel");
        String quotaPromoCodeLevel = map.get("quotaPromoCodeLevel");
        String campaignName = map.get("campaignName");
        String startDate = CoreFunctions.convertToStartOfDay(CoreFunctions.getCurrentDateYYYY_MM_DD());
        String endDate = CoreFunctions.generateRandomDate("10-10", "yyyy-MM-dd 00:00:00");
        String createdDate = CoreFunctions.getCurrentDateYYYY_MM_DD();
        String updatedDate = CoreFunctions.getCurrentDateYYYY_MM_DD();
        String productType = map.get("productType");
        String errorMsgEN = map.get("errorMsgEN");
        String errorMsgId = map.get("errorMsgId");
        String budgetId = map.get("budgetId");


        step("Prerequisite 1: Promo Creation via API");
        PromoDashboard<?> helper = new PromoDashboard<>(session);
        List<String> data = helper.createPromo(
                uniqueLinkName,
                email,
                accountId,
                quotaLevel,
                quotaPromoCodeLevel,
                promoCodeName,
                campaignName,
                startDate,
                endDate,
                createdDate,
                updatedDate,
                budgetId
            );
        String createdCode = data.get(0);

        step("Prerequisite 2: Create Gift Card Order for Prerequisite via API");
        GiftCardOrderResponse preOrderData = giftCardPage.createGiftCardOrderViaAPI(currency, voucherAmount, profileId, email, fullName, phone, title, email, passwordDweb);
        GiftCardOrderResponse.Order preOrderDataDetails = preOrderData.order;
        String preOrderId = preOrderDataDetails.orderId;
        String preOrderHash = preOrderDataDetails.orderHash;

        step("Create Gift Card order via API and fetch the orderID");
        GiftCardOrderResponse orderDetailsData = giftCardPage.createGiftCardOrderViaAPI(currency, voucherAmount,
                profileId, email, fullName, phone, title, email, passwordDweb);

        step("Fetch Order Id & Hash");
        GiftCardOrderResponse.Order orderDetails = orderDetailsData.order;
        String orderId = orderDetails.orderId;
        String orderHash = orderDetails.orderHash;

        step("Login as " + email + " via session injection");
        landingPage.maximize();
        loginPage.loginWithSpecificAccountViaCookie(email, passwordDweb);


        step("Select english language if not in english");
        homePage.selectEnglishLanguageOnly();

        step("Go to the payment page");
        landingPage.goToPaymentPage(orderId, orderHash);

        step("Verify payment page is loaded or not");
        Assertion.assertThat("Verify the payment page is loaded or not", landingPage.isPaymentPageLoaded(), is(true));

        step("Prerequisite 3: Exhaust the promo usage via API using the browser's device_id");
        PaymentApi<?> paymentApi = new PaymentApi<>(session);
        String accessToken = new Unm<>(session).getAccessToken(email, passwordDweb, "B2C");
        String deviceId = loginPage.getDeviceId();
        if (deviceId == null || deviceId.isBlank()) {
            throw new AssertionError("device_id cookie is missing; cannot exhaust promo usage against the same device as UI");
        }
        PaymentCorePromoApplyResponse preApply = RetryUtils.retryUntil(
                6, 3000,
                () -> paymentApi.applyPromoGv(accessToken, preOrderId, preOrderHash, createdCode, "IDR", "", productType, Long.valueOf(voucherAmount), deviceId),
                response -> "SUCCESS".equals(response.code),
                "Apply promo to exhaust usage (awaiting promo propagation)");
        Assertion.assertThat("Verify the promo is applied", preApply.code, is("SUCCESS"));

        step("Click in promo suggestion");
        landingPage.clickPromoSuggestion();

        step("input text in promo box");
        landingPage.inputPromoInInputField(createdCode);

        step("Click apply button");
        landingPage.clickApplyButton();

        step("Verify promo error message is correct");
        Assertion.assertThat("Verify promo error message is correct.",
                landingPage.verifyErrorMsg(errorMsgEN, errorMsgId), is(true));
    }

    @Author(names = { "Fery" })
    @Module(name = "PromoPayment")
    @Tribe(name = "Promo")
    @Vertical(name = "Promo")
    @Test
    public void verifyUsageCountCreditCard() throws InterruptedException, FailedLoginException, ExecutionException {
        Map<String, String> map = getParam("verifyUsageCountCreditCard");
        BaseTest.paramsThreadLocal.set(map);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        PaymentPageWeb<?> landingPage = PaymentPageWeb.getInstance(session);
        GiftCardPageWeb<?> giftCardPage = GiftCardPageWeb.getInstance(session);

        String email = map.get("email");
        String passwordDweb = map.get("passwordDweb");
        String currency = map.get("currency");
        int voucherAmount = Integer.parseInt(map.get("voucherAmount"));
        String profileId = map.get("profileId");
        String fullName = map.get("fullName");
        String phone = map.get("phone");
        String[] salutationList = { "Mr", "Mrs", "Ms" };
        String title = getRandomObject(salutationList);
        String promoCodeName = map.get("promoCodeName");

        String accountId = map.get("accountId");
        String uniqueLinkName = map.get("uniqueLinkName");
        String quotaLevel = map.get("quotaLevel");
        String quotaPromoCodeLevel = map.get("quotaPromoCodeLevel");
        String campaignName = map.get("campaignName");
        String startDate = CoreFunctions.convertToStartOfDay(CoreFunctions.getCurrentDateYYYY_MM_DD());
        String endDate = CoreFunctions.generateRandomDate("10-10", "yyyy-MM-dd 00:00:00");
        String createdDate = CoreFunctions.getCurrentDateYYYY_MM_DD();
        String updatedDate = CoreFunctions.getCurrentDateYYYY_MM_DD();
        String productType = map.get("productType");
        String errorMsgEN = map.get("errorMsgEN");
        String errorMsgId = map.get("errorMsgId");

        String paymentGroup = map.get("paymentGroup");
        String paymentMethod = map.get("paymentMethod");
        // Card details are fixed for this scenario and used only here; fall back to the scenario card when not set in testdata.
        String cardNumber = map.getOrDefault("cardNumber", "41111111XXXX1111");
        String cardNumberToken = map.getOrDefault("cardNumberToken", "69f87563bf9c55dc2e58afe5");
        String binNumber = map.getOrDefault("binNumber", "41111111");
        String budgetId = map.get("budgetId");

        step("Prerequisite 1: Credit Card Promo Creation via API");
        PromoDashboard<?> helper = new PromoDashboard<>(session);
        List<String> data = helper.createCreditCardPromo(
                uniqueLinkName,
                email,
                accountId,
                quotaLevel,
                quotaPromoCodeLevel,
                promoCodeName,
                campaignName,
                startDate,
                endDate,
                createdDate,
                updatedDate,
                budgetId
            );
        String createdCode = data.get(0);

        step("Prerequisite 2: Create Gift Card Order for Prerequisite via API");
        GiftCardOrderResponse preOrderData = giftCardPage.createGiftCardOrderViaAPI(currency, voucherAmount, profileId, email, fullName, phone, title, email, passwordDweb);
        GiftCardOrderResponse.Order preOrderDataDetails = preOrderData.order;
        String preOrderId = preOrderDataDetails.orderId;
        String preOrderHash = preOrderDataDetails.orderHash;

        step("Prerequisite 3: Apply the promo for the Credit Card via API to exhaust its usage count");
        PaymentApi<?> paymentApi = new PaymentApi<>(session);
        String accessToken = new Unm<>(session).getAccessToken(email, passwordDweb, "B2C");
        PaymentCorePromoApplyResponse preApply = RetryUtils.retryUntil(
                6, 3000,
                () -> paymentApi.applyPromoGv(accessToken, preOrderId, preOrderHash, createdCode, currency, "CREDITCARD", productType, Long.valueOf(voucherAmount), cardNumber, cardNumberToken, binNumber),
                response -> "SUCCESS".equals(response.code),
                "Apply credit card promo to exhaust usage (awaiting promo propagation)");
        Assertion.assertThat("Verify the promo is applied", preApply.code, is("SUCCESS"));

        step("Create Gift Card order via API and fetch the orderID");
        GiftCardOrderResponse orderDetailsData = giftCardPage.createGiftCardOrderViaAPI(currency, voucherAmount,
                profileId, email, fullName, phone, title, email, passwordDweb);

        step("Fetch Order Id & Hash");
        GiftCardOrderResponse.Order orderDetails = orderDetailsData.order;
        String orderId = orderDetails.orderId;
        String orderHash = orderDetails.orderHash;

        step("Login as " + email + " via session injection");
        landingPage.maximize();
        loginPage.loginWithSpecificAccountViaCookie(email, passwordDweb);

        step("Select english language if not in english");
        homePage.selectEnglishLanguageOnly();

        step("Go to the payment page");
        landingPage.goToPaymentPage(orderId, orderHash);

        step("Verify payment page is loaded or not");
        Assertion.assertThat("Verify the payment page is loaded or not", landingPage.isPaymentPageLoaded(), is(true));

        step("Open the payment method selector");
        landingPage.clickOnSelectPaymentMethod();

        step("Scroll down to the payment Group");
        landingPage.goToPaymentMethodsGroup(paymentGroup);

        step("Select the credit card with the specific bin number");
        landingPage.selectPaymentMethodCCTokenize(paymentMethod);

        step("Click in promo suggestion");
        landingPage.clickPromoSuggestion();

        step("Verify the promo is not visible in suggestion as the credit card usage count has reached 0");
        Assertion.assertThat("Verify the promo is not shown in suggestion.",
                landingPage.isPromoVisibleInSuggestion(createdCode), is(false));

        step("input text in promo box");
        landingPage.inputPromoInInputField(createdCode);

        step("Click apply button");
        landingPage.clickApplyButton();

        step("Verify promo error message is correct");
        Assertion.assertThat("Verify promo error message is correct.",
                landingPage.verifyErrorMsg(errorMsgEN, errorMsgId), is(true));
    }

    @Author(names = { "Aditi" })
    @Module(name = "PromoPayment")
    @Tribe(name = "Promo")
    @Vertical(name = "Promo")
    @Test
    public void verifyDynamicPromo() throws InterruptedException, FailedLoginException {
        Map<String, String> map = getParam("verifyDynamicPromo");
        BaseTest.paramsThreadLocal.set(map);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        PaymentPageWeb<?> landingPage = PaymentPageWeb.getInstance(session);
        orderCreationPage<?> orderCreationPage = new orderCreationPage<>(session);

        String email = map.get("email");
        String passwordDweb = map.get("passwordDweb");

        String[] salutationList = { "Mr", "Mrs", "Ms" };
        String fullName = randomName() + " Autorder";
        String nameTitle = getRandomObject(salutationList);
        String currency = map.get("currency");
        String productUrlName = map.get("productUrlName");
        String promoCodeName = map.get("promoCodeName");
        String wordingApplied = map.get("wordingApplied");
        int guestCount = Integer.parseInt(map.get("guestCount"));

        step("Create Event/Todo order via API and fetch the orderID");
        orderCreationPage.createGuestWaitingPaymentOrderApiTodo(productUrlName, true, currency, fullName, nameTitle, fullName, email, nameTitle, guestCount);
        String orderIdCreated = String.valueOf(orderCreationPage.getOrderId());
        String orderHashCreated = orderCreationPage.getOrderHash();

        step("Login as " + email + " via session injection");
        landingPage.maximize();
        loginPage.loginWithSpecificAccountViaCookie(email, passwordDweb);

        step("Select english language if not in english");
        homePage.selectEnglishLanguageOnly();

        step("Go to the payment page");
        landingPage.goToPaymentPage(orderIdCreated, orderHashCreated);

        step("Verify payment page is loaded or not");
        Assertion.assertThat("Verify the payment page is loaded or not", landingPage.isPaymentPageLoaded(), is(true));

        step("Capture price before applying promo");
        String priceBeforePromo = landingPage.getFinalPrice();

        step("Click in promo suggestion");
        landingPage.clickPromoSuggestion();

        step("input text in promo box");
        landingPage.inputPromoInInputField(promoCodeName);

        step("Click apply button");
        landingPage.clickApplyButton();

        step("Verify promo is applied or not");
        Assertion.assertThat("Verify promo is applied.",
                landingPage.isPromoInstantApplied(wordingApplied), is(true));

        step("Verify promo discount matches payment page price calculation");
        landingPage.verifyPromoDiscountPriceCalculation(priceBeforePromo);
    }

    @Author(names = { "Aditi" })
    @Module(name = "PromoPayment")
    @Tribe(name = "Promo")
    @Vertical(name = "Promo")
    @Test
    public void verifyPromoExpiredLowBudget() throws FailedLoginException, InterruptedException {
        Map<String, String> map = getParam("verifyPromoExpiredLowBudget");
        BaseTest.paramsThreadLocal.set(map);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        PaymentPageWeb<?> landingPage = PaymentPageWeb.getInstance(session);
        orderCreationPage<?> orderCreationPage = new orderCreationPage<>(session);

        String email = map.get("email");
        String currency = map.get("currency");
        String productUrlName = map.get("productUrlName");
        String passwordDweb = map.get("passwordDweb");
        String[] salutationList = { "Mr", "Mrs", "Ms" };
        String fullName = randomName() + " Autorder";
        String nameTitle = getRandomObject(salutationList);
        String promoCodeName = map.get("promoCodeName");
        String errorMsgEN = map.get("errorMsgEN");
        String errorMsgID = map.get("errorMsgID");
        int guestCount = Integer.parseInt(map.get("guestCount"));

        step("Create Event/Todo order via API and fetch the orderID");
        orderCreationPage.createGuestWaitingPaymentOrderApiTodo(productUrlName, true, currency, fullName, nameTitle, fullName, email, nameTitle, guestCount);
        String orderIdCreated = String.valueOf(orderCreationPage.getOrderId());
        String orderHashCreated = orderCreationPage.getOrderHash();

        step("Login as " + email + " via session injection");
        landingPage.maximize();
        loginPage.loginWithSpecificAccountViaCookie(email, passwordDweb);

        step("Go to the payment page");
        landingPage.goToPaymentPage(orderIdCreated, orderHashCreated);

        step("Verify payment page is loaded or not");
        Assertion.assertThat("Verify the payment page is loaded or not",
                landingPage.isPaymentPageLoaded(), is(true));

        step("Click in promo suggestion");
        landingPage.clickPromoSuggestion();

        step("input text in promo box");
        landingPage.inputPromoInInputField(promoCodeName);

        step("Click apply button");
        landingPage.clickApplyButton();

        step("Verify Error Message");
        Assertion.assertThat("Verify Error Message",
                landingPage.verifyErrorMsg(errorMsgEN, errorMsgID), is(true));
    }
}
