package com.tiket.web.test.giftcard;

import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.core.Is.is;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Session;
import com.tiket.pojo.giftCard.GiftCardOrderResponse;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.web.page.common.home.HomePageWeb;
import com.tiket.web.page.common.home.LoginPageWeb;
import com.tiket.web.page.giftcard.GiftCardPageWeb;
import java.util.Map;
import javax.security.auth.login.FailedLoginException;

import com.tiket.web.page.order.OrderList.OrderListWeb;
import com.tiket.web.page.platform.payment.PaymentPageWeb;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

public class GiftCardTest extends BaseTest {
    @Author(names = {"Fery"})
    @Module(name = "GiftCard")
    @Tribe(name = "Promo")
    @Vertical(name = "GiftCard")
    @Test
    public void verifyGiftCardLandingPage() throws FailedLoginException, InterruptedException {
        Map<String, String> map = getParam("verifyGiftCardLandingPage");
        BaseTest.paramsThreadLocal.set(map);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        GiftCardPageWeb<?> giftCardPage = GiftCardPageWeb.getInstance(session);

        String email = map.get("email");
        String passwordDweb = map.get("passwordDweb");

        step("Login as " + email + " via session injection");
        giftCardPage.maximize();
        loginPage.loginWithSpecificAccountViaCookie(email, passwordDweb);


        step("Select english language if not in english");
        homePage.selectEnglishLanguageOnly();

        step("Go to Gift Card landing page");
        giftCardPage.goToGiftCardPage();
        giftCardPage.verifyLandingPageLoads();

        step("Check gift card landing page contents");
        Assertion.assertThat("Verify user can see Gift Card Hero Image", giftCardPage.isHeroImageVisible(), is(true));
        Assertion.assertThat("Verify user can see CTA button is visible", giftCardPage.isSendAndRedeemButtonVisible(), is(true));
        Assertion.assertThat("Verify user can see order bulk section", giftCardPage.isBulkOrderButtonVisible(), is(true));
        Assertion.assertThat("Verify user can see the occasion category selection", giftCardPage.isOccasionSectionVisible(), is(true));
        Assertion.assertThat("Verify user can see the static usp banner", giftCardPage.isStaticUspListVisible(), is(true));
    }

    @Author(names = {"Fery"})
    @Module(name = "GiftCard")
    @Tribe(name = "Promo")
    @Vertical(name = "GiftCard")
    @Test
    public void verifyCardSelectionGiftCard() throws FailedLoginException, InterruptedException {
        Map<String, String> map = getParam("verifyCardSelectionGiftCard");
        BaseTest.paramsThreadLocal.set(map);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        GiftCardPageWeb<?> giftCardPage = GiftCardPageWeb.getInstance(session);

        String email = map.get("email");
        String passwordDweb = map.get("passwordDweb");
        int cardIndex = Integer.parseInt(map.get("cardIndex"));

        step("Login as " + email + " via session injection");
        giftCardPage.maximize();
        loginPage.loginWithSpecificAccountViaCookie(email, passwordDweb);


        step("Select english language if not in english");
        homePage.selectEnglishLanguageOnly();

        step("Go to Gift Card landing page");
        giftCardPage.goToGiftCardPage();
        giftCardPage.verifyLandingPageLoads();

        step("Go to sender detail page");
        giftCardPage.clickSendGiftCardButton();

        step("Verify card design list is shown");
        Assertion.assertThat("Verify card design list is shown", giftCardPage.isCardItemsVisible(), is(true));

        step("Verify selected card design become active");
        Assertion.assertThat("Compare caption is different before and after click different card", giftCardPage.verifyTextChangesAfterClick(cardIndex), is(true));
    }

    @Author(names = {"Vidhi Jindal"})
    @Module(name = "GiftCard")
    @Tribe(name = "Promo")
    @Vertical(name = "GiftCard")
    @Test
    public void verifyGiftCardRedeemedValidationError() throws FailedLoginException, InterruptedException {
        Map<String, String> map = getParam("verifyGiftCardRedeemedValidationError");
        BaseTest.paramsThreadLocal.set(map);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        GiftCardPageWeb<?> giftCardPage = GiftCardPageWeb.getInstance(session);

        String email = map.get("email");
        String passwordDweb = map.get("passwordDweb");
        String giftCardCode = map.get("giftCardCode");
        String errorMsgEN = map.get("errorMsgEN");
        String errorMsgID = map.get("errorMsgID");

        step("Login as " + email + " via session injection");
        giftCardPage.maximize();
        loginPage.loginWithSpecificAccountViaCookie(email, passwordDweb);


        step("Select english language if not in english");
        homePage.selectEnglishLanguageOnly();

        step("Go to Gift Card landing page");
        giftCardPage.goToGiftCardPage();
        giftCardPage.verifyLandingPageLoads();

        step("Check gift card landing page contents");
        Assertion.assertThat("Verify user can see Gift Card Hero Image", giftCardPage.isHeroImageVisible(), is(true));
        Assertion.assertThat("Verify user can see CTA button is visible", giftCardPage.isSendAndRedeemButtonVisible(), is(true));
        step("Click Redeem Gift Card.");
        giftCardPage.clickRedeemGiftCard();

        step("Input Gift Card Code.");
        giftCardPage.inputCodeInputField(giftCardCode);
        giftCardPage.clickRedeemCode();

        step("Check gift card code is already redeemed.");
        Assertion.assertThat("Verify validation error for already redeemed code.", giftCardPage.verifyReedemedGiftCardErrorMsgFunct(errorMsgEN, errorMsgID), is(true));
    }

    @Author(names = {"Tasya"})
    @Module(name = "GiftCard")
    @Tribe(name = "Promo")
    @Vertical(name = "GiftCard")
    @Test
    public void verifyOccasionCategorySelection() throws FailedLoginException, InterruptedException {
        Map<String, String> map = getParam("verifyOccasionCategorySelection");
        BaseTest.paramsThreadLocal.set(map);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        GiftCardPageWeb<?> giftCardPage = GiftCardPageWeb.getInstance(session);

        String email = map.get("email");
        String passwordDweb = map.get("passwordDweb");
        String expectedValue = map.get("expectedValue");

        step("Login as " + email + " via session injection");
        giftCardPage.maximize();
        loginPage.loginWithSpecificAccountViaCookie(email, passwordDweb);


        step("Select english language if not in english");
        homePage.selectEnglishLanguageOnly();

        step("Go to Gift Card landing page");
        giftCardPage.goToGiftCardPage();
        giftCardPage.verifyLandingPageLoads();

        step("Verify the occasion category list is shown");
        Assertion.assertThat("Verify the occasion category list is shown", giftCardPage.isOccasionSectionVisible(), is(true));

        step("Verify user scroll/click the carousel of occasion category");
        giftCardPage.verifyClickOccasionButton();

        step("Verify user redirected to select card design page");
        giftCardPage.verifyRedirectToSelectCardDesign(expectedValue);

        step("Check occasion category Selection");
        Assertion.assertThat("Verify user redirected to select card design page", giftCardPage.verifyRedirectionProcessSuccess(), is(true));
    }
    
    @Author(names = {"Vidhi Jindal"})
    @Module(name = "GiftCard")
    @Tribe(name = "Promo")
    @Vertical(name = "GiftCard")
    @Test
    public void verifyGiftCardInvalidValidationError() throws FailedLoginException, InterruptedException {
        Map<String, String> map = getParam("verifyGiftCardInvalidValidationError");
        BaseTest.paramsThreadLocal.set(map);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        GiftCardPageWeb<?> giftCardPage = GiftCardPageWeb.getInstance(session);

        String email = map.get("email");
        String passwordDweb = map.get("passwordDweb");
        String giftCardCode = map.get("giftCardCode");
        String errorMsgEN = map.get("errorMsgEN");
        String errorMsgID = map.get("errorMsgID");

        step("Login as " + email + " via session injection");
        giftCardPage.maximize();
        loginPage.loginWithSpecificAccountViaCookie(email, passwordDweb);


        step("Select english language if not in english");
        homePage.selectEnglishLanguageOnly();

        step("Go to Gift Card landing page");
        giftCardPage.goToGiftCardPage();
        giftCardPage.verifyLandingPageLoads();

        step("Check gift card landing page contents");
        Assertion.assertThat("Verify user can see Gift Card Hero Image", giftCardPage.isHeroImageVisible(), is(true));
        Assertion.assertThat("Verify user can see CTA button is visible", giftCardPage.isSendAndRedeemButtonVisible(), is(true));

        step("Click Redeem Gift Card.");
        giftCardPage.clickRedeemGiftCard();

        step("Input Gift Card Code.");
        giftCardPage.inputCodeInputField(giftCardCode);
        giftCardPage.clickRedeemCode();

        step("Check gift card code is invalid.");
        Assertion.assertThat("Verify validation error for invalid gift card code.", giftCardPage.verifyInvalidGiftCardCodeErrorMsgFunct(errorMsgEN, errorMsgID), is(true));
    }

    @Author(names = {"Fery"})
    @Module(name = "GiftCard")
    @Tribe(name = "Promo")
    @Vertical(name = "GiftCard")
    @Test
    public void verifyRedeemCodeIDR() throws FailedLoginException, InterruptedException {
        Map<String, String> map = getParam("verifyRedeemCodeIDR");
        BaseTest.paramsThreadLocal.set(map);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        GiftCardPageWeb<?> giftCardPage = GiftCardPageWeb.getInstance(session);

        String email = map.get("email");
        String passwordDweb = map.get("passwordDweb");
        String apiEmail = map.get("apiEmail");
        String apiProfileId = map.get("apiProfileId");
        String fullName = CoreFunctions.randomName() + " Automation";
        String phone = CoreFunctions.randomPhone();
        String currency = map.get("currency");
        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String salutation = CoreFunctions.getRandomObject(salutationList);
        int voucherAmount = Integer.parseInt(map.get("voucherAmount"));

        step("Login as " + email + " via session injection");
        giftCardPage.maximize();
        loginPage.loginWithSpecificAccountViaCookie(email, passwordDweb);


        step("Select english language if not in english");
        homePage.selectEnglishLanguageOnly();

        step("Go to Gift Card landing page");
        giftCardPage.goToGiftCardPage();
        giftCardPage.verifyLandingPageLoads();

        step("API helper for gift card issuance");
        step("create gift card order via API");
        GiftCardOrderResponse orderDetailsData = giftCardPage.createGiftCardOrderViaAPI(currency, voucherAmount, apiProfileId, apiEmail, fullName, phone, salutation, email, passwordDweb);

        step("pay via API");
        GiftCardOrderResponse.Order orderDetails = orderDetailsData.order;
        String orderId = orderDetails.orderId;
        String orderHash = orderDetails.orderHash;

        step("pay gift card");
        Assertion.assertThat("gift card is paid", giftCardPage.verifyGiftCardOrderPayment(orderId, orderHash), is("PAID"));

        step("get redeem code");
        Assertion.assertThat("gift card is issued", giftCardPage.verifyissuance(orderId, orderHash), is("ISSUED"));
        String giftCode = giftCardPage.getRedeemCode(orderId, orderHash);

        step("Redeem gift card flow");
        giftCardPage.clickRedeemGiftCardButton();
        giftCardPage.inputRedeemCode(giftCode);
        giftCardPage.clickRedeem();
        giftCardPage.clickRedeemClaimGiftCard();
        giftCardPage.selectVoucherOptions();
        giftCardPage.clickSubmitConfirmVoucherButton();
        Assertion.assertThat("Verify voucher success to be redeemed", giftCardPage.isTncRedeemDetailsShown(), is(true));
    }

    @Author(names = {"Fery"})
    @Module(name = "GiftCard")
    @Tribe(name = "Promo")
    @Vertical(name = "GiftCard")
    @Test
    public void verifyRedeemCodeNonIDR() throws FailedLoginException, InterruptedException {
        Map<String, String> map = getParam("verifyRedeemCodeNonIDR");
        BaseTest.paramsThreadLocal.set(map);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        GiftCardPageWeb<?> giftCardPage = GiftCardPageWeb.getInstance(session);

        String email = map.get("email");
        String passwordDweb = map.get("passwordDweb");
        String apiEmail = map.get("apiEmail");
        String apiProfileId = map.get("apiProfileId");
        String fullName = CoreFunctions.randomName() + " Automation";
        String phone = CoreFunctions.randomPhone();
        String currency = map.get("currency");
        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String salutation = CoreFunctions.getRandomObject(salutationList);
        int voucherAmount = Integer.parseInt(map.get("voucherAmount"));

        step("Login as " + email + " via session injection");
        giftCardPage.maximize();
        loginPage.loginWithSpecificAccountViaCookie(email, passwordDweb);


        step("Select english language if not in english");
        homePage.selectEnglishLanguageOnly();

        step("Go to Gift Card landing page");
        giftCardPage.goToGiftCardPage();
        giftCardPage.verifyLandingPageLoads();

        step("API helper for gift card issuance");
        step("create gift card order via API");
        GiftCardOrderResponse orderDetailsData = giftCardPage.createGiftCardOrderViaAPI(
                currency, voucherAmount, apiProfileId, apiEmail, fullName, phone, salutation, email, passwordDweb);

        step("pay via API");
        GiftCardOrderResponse.Order orderDetails = orderDetailsData.order;
        String orderId = orderDetails.orderId;
        String orderHash = orderDetails.orderHash;

        step("pay gift card");
        Assertion.assertThat("gift card is paid", giftCardPage.verifyGiftCardOrderPayment(orderId, orderHash), is("PAID"));

        step("get redeem code");
        Assertion.assertThat("gift card is issued", giftCardPage.verifyissuance(orderId, orderHash), is("ISSUED"));
        String giftCode = giftCardPage.getRedeemCode(orderId, orderHash);

        step("Redeem gift card flow");
        giftCardPage.clickRedeemGiftCardButton();
        giftCardPage.inputRedeemCode(giftCode);
        giftCardPage.clickRedeem();
        giftCardPage.clickRedeemClaimGiftCard();
        giftCardPage.selectVoucherOptions();
        giftCardPage.clickSubmitConfirmVoucherButton();
        Assertion.assertThat("Verify voucher success to be redeemed", giftCardPage.isTncRedeemDetailsShown(), is(true));
    }

    @Author(names = {"Tasya"})
    @Module(name = "GiftCard")
    @Tribe(name = "Promo")
    @Vertical(name = "GiftCard")
    @Test
    public void verifyTermsConditionSendGCPage() throws FailedLoginException, InterruptedException {
        Map<String, String> map = getParam("verifyTermsConditionSendGCPage");
        BaseTest.paramsThreadLocal.set(map);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        GiftCardPageWeb<?> giftCardPage = GiftCardPageWeb.getInstance(session);

        String email = map.get("email");
        String passwordDweb = map.get("passwordDweb");
        String recipientName = map.get("recipientName");
        String phoneNumber = map.get("phoneNumber");
        String amount = map.get("amount");
        String message = map.get("message");

        step("Login as " + email + " via session injection");
        giftCardPage.maximize();
        loginPage.loginWithSpecificAccountViaCookie(email, passwordDweb);

        step("User select IDR currency");
        homePage.selectCurrency("IDR");

        step("Select english language if not in english");
        homePage.selectEnglishLanguageOnly();

        step("Go to Gift Card landing page");
        giftCardPage.goToGiftCardPage();
        giftCardPage.verifyLandingPageLoads();

        step("Verify user can click on send gift card button");
        giftCardPage.clickSendGiftCardButton();

        step("Verify user can click on the pencil button to add/edit recipient");
        giftCardPage.clickEditRecipientButton();

        step("Verify user can fill the recipient form");
        giftCardPage.fillRecipientDetails(recipientName, phoneNumber, email, amount, message);

        step("Verify user can click continue");
        giftCardPage.clickContinueSenderButton();

        step("Verify user redirect to complete order page");
        giftCardPage.verifyCompleteOrderPageOpened();

        step("Verify terms and condition in Sender Page");
        giftCardPage.verifyTncSenderPage();

        step("Check TnC modal contents");
        Assertion.assertThat("Verify user can see Tnc Validity Period", giftCardPage.isTncPeriodVisible(), is(true));
        Assertion.assertThat("Verify user can see Tnc Eligible Products", giftCardPage.isEligibleProductsVisible(), is(true));
        Assertion.assertThat("Verify user can see Tnc Ineligible Products", giftCardPage.isIneligibleProductsVisible(), is(true));
        Assertion.assertThat("Verify user can see Others/Notes", giftCardPage.isTncNotesSendePageVisible(), is(true));

        step("Verify user can close TnC");
        Assertion.assertThat("Verify TnC modal closed", giftCardPage.closeTncAndVerifyClosed(), is(true));
    }

    @Author(names = {"Amit", "Vidhi Jindal"})
    @Module(name = "GiftCard")
    @Tribe(name = "Promo")
    @Vertical(name = "GiftCard")
    @Test
    public void verifyGiftCardPostIssuance() throws FailedLoginException, InterruptedException {
        Map<String, String> map = getParam("verifyGiftCardPostIssuance");
        BaseTest.paramsThreadLocal.set(map);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        GiftCardPageWeb<?> giftCardPage = GiftCardPageWeb.getInstance(session);

        String email = map.get("email");
        String passwordDweb = map.get("passwordDweb");
        String Name = map.get("Name");
        String Mobile = map.get("Mobile");
        String currency = map.get("currency");
        String Email = map.get("Email");
        String Amount = map.get("Amount");
        String senderName = map.get("senderName");

        step("Login as " + email + " via session injection");
        giftCardPage.maximize();
        loginPage.loginWithSpecificAccountViaCookie(email, passwordDweb);


        step("Select english language if not in english");
        homePage.selectEnglishLanguageOnly();

        step("User select IDR currency");
        homePage.selectCurrency(currency);

        step("Go to Gift Card landing page");
        giftCardPage.goToGiftCardPage();
        giftCardPage.verifyLandingPageLoads();

        step("Go to sender detail page");
        giftCardPage.clickSendGiftCardButton();

        step("Add Recipient");
        giftCardPage.clickAddRecipient();
        giftCardPage.fillRecipientName(Name);
        giftCardPage.inputRECIPIENT_MOBILE(Mobile);
        giftCardPage.inputRECIPIENT_EMAIL(Email);
        giftCardPage.fillGiftCardAmount(Amount);

        step("Click Save");
        giftCardPage.recipient_Click_Save();

        step("Continue Click");
        giftCardPage.continueToSendersForm();

        step("Send Later");
        giftCardPage.click_Send_Later();

        step("Enter sender's details.");
        giftCardPage.fillSenderName(senderName);

        step("Check T&C");
        Assertion.assertThat("Verify that terms and condition displayed",giftCardPage.isTermsAndConditionVisible(),is(true));
        Assertion.assertThat("Verify that Sender name is displayed",giftCardPage.isSenderNameDisplayed(),is(true));
        Assertion.assertThat("Send Date",giftCardPage.sendDate(),is(true));
        Assertion.assertThat("Send Time",giftCardPage.sendTime(),is(true));
        Assertion.assertThat("Reciepeint Name",giftCardPage.getRecipientNameValue(),is(true));

    }

    @Author(names = {"Vidhi Jindal"})
    @Module(name = "GiftCard")
    @Tribe(name = "Promo")
    @Vertical(name = "GiftCard")
    @Test
    public void verifyCreateGiftCardWithSingleRecipient() throws FailedLoginException, InterruptedException {
        Map<String, String> map = getParam("verifyCreateGiftCardWithSingleRecipient");
        BaseTest.paramsThreadLocal.set(map);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePageWeb<?> homePageWeb = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);
        GiftCardPageWeb<?> giftCardPage = GiftCardPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);

        String email = map.get("email");
        String passwordDweb = map.get("passwordDweb");
        String currency = map.get("currency");
        String paymentGroup = map.get("paymentGroup");
        String recipientName = map.get("recipientName");
        String senderName = map.get("senderName");
        String phoneNumber = map.get("phoneNumber");
        String paymentMethod = map.get("paymentMethod");
        String amount = map.get("amount");
        String expectedValue = map.get("expectedValue");

        step("Login as " + email + " via session injection");
        giftCardPage.maximize();
        loginPage.loginWithSpecificAccountViaCookie(email, passwordDweb);


        step("Select english language if not in english");
        homePageWeb.selectEnglishLanguageOnly();

        step("User select IDR currency");
        homePageWeb.selectCurrency(currency);

        step("Go to Gift Card landing page");
        giftCardPage.goToGiftCardPage();
        giftCardPage.verifyLandingPageLoads();

        step("Check gift card landing page contents");
        Assertion.assertThat("Verify user can see Gift Card Hero Image", giftCardPage.isHeroImageVisible(), is(true));
        Assertion.assertThat("Verify user can see CTA button is visible", giftCardPage.isSendAndRedeemButtonVisible(), is(true));

        step("Verify the occasion category list is shown");
        Assertion.assertThat("Verify the occasion category list is shown", giftCardPage.isOccasionSectionVisible(), is(true));

        step("Verify user scroll/click the carousel of occasion category");
        giftCardPage.verifyClickOccasionButton();

        step("Complete recipient details.");
        giftCardPage.clickAddRecipient();
        giftCardPage.fillRecipientName(recipientName);
        giftCardPage.fillContactNumber(phoneNumber);
        giftCardPage.fillEmailAddress(email);
        giftCardPage.fillGiftCardAmount(amount);
        giftCardPage.continueSaving();

        step("Verify user redirected to select card design page");
        giftCardPage.verifyRedirectToSelectCardDesign(expectedValue);

        step("Check occasion category Selection");
        Assertion.assertThat("Verify user redirected to select card design page", giftCardPage.verifyRedirectionProcessSuccess(), is(true));

        step("Verify Recipient Details is present");
        Assertion.assertThat("Verify recipient name is present.", giftCardPage.isRecepientNamePresent(), containsString(recipientName));
        Assertion.assertThat("Verify recipient contact number is present.", giftCardPage.isRecepientNumberPresent(), containsString(phoneNumber));
        Assertion.assertThat("Verify recipient email is present.", giftCardPage.isRecepientEmailPresent(), containsString(email));
        Assertion.assertThat("Verify gift card amount.", giftCardPage.isGiftCardAmountPresent(), containsString(amount));
        giftCardPage.continueToSendersForm();

        step("Enter sender's details.");
        giftCardPage.clickSalutation();
        giftCardPage.fillSenderName(senderName);
        giftCardPage.clickContinueButton();

        step("Verify payment page is loaded or not");
        Assertion.assertThat("Verify the payment page is loaded or not",
                paymentPage.isPaymentPageLoaded(), is(true));

        step("Change payment method");
        paymentPage.clickOnSelectPaymentMethod();

        step("Scroll down to the payment Group");
        paymentPage.goToPaymentMethodsGroup(paymentGroup);

        step("Select Payment Method");
        paymentPage.selectPaymentMethod(paymentMethod);

        step("Click on Pay button");
        paymentPage.clickOnPaymentButton();

        step("Verify the payment successful page is visible or not");
        Assertion.assertThat("Verify the payment successful page is visible", paymentPage.isPaymentSuccessfulVisible(), Matchers.is(true));

        step("Verify Order is issued on Order Page.");
        homePageWeb.goToYourOrderUrl();
        Assertion.assertThat("Verify gift card order is present in the order page.", orderListPage.isOrderIssued(), is(true));
    }

    @Author(names = {"Vidhi Jindal"})
    @Module(name = "GiftCard")
    @Tribe(name = "Promo")
    @Vertical(name = "GiftCard")
    @Test
    public void verifyCreateGiftCardWithSingleRecipientNonIDR() throws FailedLoginException, InterruptedException {
        Map<String, String> map = getParam("verifyCreateGiftCardWithSingleRecipientNonIDR");
        BaseTest.paramsThreadLocal.set(map);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePageWeb<?> homePageWeb = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);
        GiftCardPageWeb<?> giftCardPage = GiftCardPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);

        String email = map.get("email");
        String passwordDweb = map.get("passwordDweb");
        String currency = map.get("currency");
        String paymentGroup = map.get("paymentGroup");
        String recipientName = map.get("recipientName");
        String senderName = map.get("senderName");
        String phoneNumber = map.get("phoneNumber");
        String paymentMethod = map.get("paymentMethod");
        String amount = map.get("amount");
        String expectedValue = map.get("expectedValue");
        String cardNumber = map.get("cardNumber");
        String cardMonth = map.get("cardMonth");
        String cvv = map.get("cvv");
        String cardHolderName = map.get("cardHolderName");


        step("Login as " + email + " via session injection");
        giftCardPage.maximize();
        loginPage.loginWithSpecificAccountViaCookie(email, passwordDweb);


        step("Select english language if not in english");
        homePageWeb.selectEnglishLanguageOnly();

        step("User select Non-IDR currency.");
        homePageWeb.selectCurrency(currency);

        step("Go to Gift Card landing page");
        giftCardPage.goToGiftCardPage();
        giftCardPage.verifyLandingPageLoads();

        step("Check gift card landing page contents");
        Assertion.assertThat("Verify user can see Gift Card Hero Image", giftCardPage.isHeroImageVisible(), is(true));
        Assertion.assertThat("Verify user can see CTA button is visible", giftCardPage.isSendAndRedeemButtonVisible(), is(true));

        step("Verify the occasion category list is shown");
        Assertion.assertThat("Verify the occasion category list is shown", giftCardPage.isOccasionSectionVisible(), is(true));

        step("Verify user scroll/click the carousel of occasion category");
        giftCardPage.verifyClickOccasionButton();

        step("Complete recipient details.");
        giftCardPage.clickAddRecipient();
        giftCardPage.fillRecipientName(recipientName);
        giftCardPage.fillContactNumber(phoneNumber);
        giftCardPage.fillEmailAddress(email);
        giftCardPage.fillGiftCardAmount(amount);
        giftCardPage.continueSaving();

        step("Verify user redirected to select card design page");
        giftCardPage.verifyRedirectToSelectCardDesign(expectedValue);

        step("Check occasion category Selection");
        Assertion.assertThat("Verify user redirected to select card design page", giftCardPage.verifyRedirectionProcessSuccess(), is(true));

        step("Verify Recipient Details is present");
        Assertion.assertThat("Verify recipient name is present.", giftCardPage.isRecepientNamePresent(), containsString(recipientName));
        Assertion.assertThat("Verify recipient contact number is present.", giftCardPage.isRecepientNumberPresent(), containsString(phoneNumber));
        Assertion.assertThat("Verify recipient email is present.", giftCardPage.isRecepientEmailPresent(), containsString(email));
        Assertion.assertThat("Verify gift card amount.", giftCardPage.isGiftCardAmountPresentNonIDR(), containsString(amount));
        giftCardPage.continueToSendersForm();

        step("Enter sender's details.");
        giftCardPage.fillSenderName(senderName);
        giftCardPage.clickContinueButton();

        step("Verify payment page is loaded or not");
        Assertion.assertThat("Success redirected to payment page", paymentPage.isPaymentPageLoaded(), is(true));
        String orderId = paymentPage.getOrderId();

        step("Input the card details");
        paymentPage.inputCardNumber(cardNumber);
        paymentPage.inputCardMonth(cardMonth);
        paymentPage.inputCardCvv(cvv);
        paymentPage.inputCardHolderName(cardHolderName);

        step("Click on Pay button");
        paymentPage.clickOnPaymentButton();

        step("Verify the payment successful page is visible or not");
        Assertion.assertThat("Verify the payment successful page is visible", paymentPage.isPaymentSuccessfulVisible(), Matchers.is(true));

        step("Verify Order is issued on Order Page.");
        homePageWeb.goToYourOrderUrl();

        step("Sort orders by latest transaction");
        orderListPage.clickSortButton();
        orderListPage.clickSortAction("latestTx");
        Assertion.assertThat("Verify gift card order is present in the order page.", orderListPage.isOrderIssued(), is(true));
    }
  
    @Author(names = {"Alden"})
    @Module(name = "GiftCard")
    @Tribe(name = "Promo")
    @Vertical(name = "GiftCard")
    @Test
    public void verifyGiftCardPriceDetailSenderPage() throws FailedLoginException, InterruptedException {
        Map<String, String> map = getParam("verifyGiftCardPriceDetailSenderPage");
        BaseTest.paramsThreadLocal.set(map);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePageWeb<?> homePageWeb = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        GiftCardPageWeb<?> giftCardPage = GiftCardPageWeb.getInstance(session);

        String email = map.get("email");
        String passwordDweb = map.get("passwordDweb");
        String currency = map.get("currency");
        String recipientName = map.get("recipientName");
        String senderName = map.get("senderName");
        String phoneNumber = map.get("phoneNumber");
        String amount = map.get("amount");
        String expectedValue = map.get("expectedValue");

        step("Login as " + email + " via session injection");
        giftCardPage.maximize();
        loginPage.loginWithSpecificAccountViaCookie(email, passwordDweb);


        step("Select english language if not in english");
        homePageWeb.selectEnglishLanguageOnly();

        step("User select IDR currency");
        homePageWeb.selectCurrency(currency);

        step("Go to Gift Card landing page");
        giftCardPage.goToGiftCardPage();
        giftCardPage.verifyLandingPageLoads();

        step("Check gift card landing page contents");
        Assertion.assertThat("Verify user can see Gift Card Hero Image", giftCardPage.isHeroImageVisible(), is(true));
        Assertion.assertThat("Verify user can see CTA button is visible", giftCardPage.isSendAndRedeemButtonVisible(), is(true));

        step("Verify the occasion category list is shown");
        Assertion.assertThat("Verify the occasion category list is shown", giftCardPage.isOccasionSectionVisible(), is(true));

        step("Verify user scroll/click the carousel of occasion category");
        giftCardPage.verifyClickOccasionButton();

        step("Complete recipient details.");
        giftCardPage.clickAddRecipient();
        giftCardPage.fillRecipientName(recipientName);
        giftCardPage.fillContactNumber(phoneNumber);
        giftCardPage.fillEmailAddress(email);
        giftCardPage.fillGiftCardAmount(amount);
        giftCardPage.continueSaving();

        step("Verify user redirected to select card design page");
        giftCardPage.verifyRedirectToSelectCardDesign(expectedValue);

        step("Check occasion category Selection");
        Assertion.assertThat("Verify user redirected to select card design page", giftCardPage.verifyRedirectionProcessSuccess(), is(true));

        step("Verify Recipient Details is present");
        Assertion.assertThat("Verify recipient name is present.", giftCardPage.isRecepientNamePresent(), containsString(recipientName));
        Assertion.assertThat("Verify recipient contact number is present.", giftCardPage.isRecepientNumberPresent(), containsString(phoneNumber));
        Assertion.assertThat("Verify recipient email is present.", giftCardPage.isRecepientEmailPresent(), containsString(email));
        Assertion.assertThat("Verify gift card amount.", giftCardPage.isGiftCardAmountPresent(), containsString(amount));
        giftCardPage.continueToSendersForm();

        step("Enter sender's details.");
        giftCardPage.clickSalutation();
        giftCardPage.fillSenderName(senderName);

        step("Verify price details");
        long totalPrice = giftCardPage.getPrice();
        Assertion.assertThat("Total price sum in price detail is correct", giftCardPage.verifyPriceDetails(), is(totalPrice));
    }
  
    @Author(names = {"Vidhi Jindal"})
    @Module(name = "GiftCard")
    @Tribe(name = "Promo")
    @Vertical(name = "GiftCard")
    @Test
    public void verifyDetailsRedeemGiftCardNonIDR() throws FailedLoginException, InterruptedException {
        Map<String, String> map = getParam("verifyDetailsRedeemGiftCardNonIDR");
        BaseTest.paramsThreadLocal.set(map);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        GiftCardPageWeb<?> giftCardPage = GiftCardPageWeb.getInstance(session);

        String email = map.get("email");
        String passwordDweb = map.get("passwordDweb");
        String apiEmail = map.get("apiEmail");
        String apiProfileId = map.get("apiProfileId");
        String fullName = CoreFunctions.randomName() + " Automation";
        String phone = CoreFunctions.randomPhone();
        String currency = map.get("currency");
        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String salutation = CoreFunctions.getRandomObject(salutationList);
        int voucherAmount = Integer.parseInt(map.get("voucherAmount"));

        step("Login as " + email + " via session injection");
        giftCardPage.maximize();
        loginPage.loginWithSpecificAccountViaCookie(email, passwordDweb);


        step("Select english language if not in english");
        homePage.selectEnglishLanguageOnly();

        step("Go to Gift Card landing page");
        giftCardPage.goToGiftCardPage();
        giftCardPage.verifyLandingPageLoads();

        step("API helper for gift card issuance");
        step("create gift card order via API");
        GiftCardOrderResponse orderDetailsData = giftCardPage.createGiftCardOrderViaAPI(
                currency, voucherAmount, apiProfileId, apiEmail, fullName, phone, salutation, email, passwordDweb);

        step("pay via API");
        GiftCardOrderResponse.Order orderDetails = orderDetailsData.order;
        String orderId = orderDetails.orderId;
        String orderHash = orderDetails.orderHash;

        step("pay gift card");
        Assertion.assertThat("gift card is paid", giftCardPage.verifyGiftCardOrderPayment(orderId, orderHash), is("PAID"));

        step("get redeem code");
        Assertion.assertThat("gift card is issued", giftCardPage.verifyissuance(orderId, orderHash), is("ISSUED"));
        String giftCode = giftCardPage.getRedeemCode(orderId, orderHash);

        step("Redeem gift card flow");
        giftCardPage.clickRedeemGiftCardButton();
        giftCardPage.inputRedeemCode(giftCode);
        giftCardPage.clickRedeem();
        giftCardPage.clickRedeemClaimGiftCard();
        giftCardPage.selectVoucherOptions();
        giftCardPage.clickSubmitConfirmVoucherButton();
        Assertion.assertThat("Verify voucher success to be redeemed", giftCardPage.isTncRedeemDetailsShown(), is(true));

        step("Verify details of the gift card is shown for each section.");
        Assertion.assertThat("Verify Validity period of gift card.", giftCardPage.verifyValidityPeriod(), is(true));
        Assertion.assertThat("Verify Eligible products section is present.", giftCardPage.verifyEligibleProducts(), is(true));
        Assertion.assertThat("Verify InEligible products section is present.", giftCardPage.verifyInEligibleProducts(), is(true));
        Assertion.assertThat("Verify other details are shown.", giftCardPage.verifyOtherDetails(), is(true));
        Assertion.assertThat("Verify voucher amount in NonIDR.", giftCardPage.verifyGiftCardAmount(), is(true));
        Assertion.assertThat("Verify Gift card expiry date.", giftCardPage.verifyGiftCardExpiryDate(), is(true));
        Assertion.assertThat("Verify voucher times", giftCardPage.verifyNumbersOfGiftCardVouchers(), is(true));
        Assertion.assertThat("Verify redeem gift card code.", giftCardPage.verifyRedeemGiftCardCode(), is(true));
    }
  
    @Author(names = {"Vidhi Jindal"})
    @Module(name = "GiftCard")
    @Tribe(name = "Promo")
    @Vertical(name = "GiftCard")
    @Test
    public void verifyGiftCardOccasionLandingPage() throws FailedLoginException, InterruptedException {
        Map<String, String> map = getParam("verifyGiftCardOccasionLandingPage");
        BaseTest.paramsThreadLocal.set(map);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        GiftCardPageWeb<?> giftCardPage = GiftCardPageWeb.getInstance(session);

        String email = map.get("email");
        String occasion = map.get("occasion");
        String passwordDweb = map.get("passwordDweb");

        step("Login as " + email + " via session injection");
        giftCardPage.maximize();
        loginPage.loginWithSpecificAccountViaCookie(email, passwordDweb);


        step("Select english language if not in english");
        homePage.selectEnglishLanguageOnly();

        step("Go to Gift Card landing page");
        giftCardPage.goToGiftCardPage();
        giftCardPage.verifyLandingPageLoads();

        step("Verify the occasion category list is shown");
        Assertion.assertThat("Verify the occasion category list is shown", giftCardPage.isOccasionSectionVisible(), is(true));

        step("Verify user scroll/click the carousel of occasion category");
        giftCardPage.verifyClickSpecificOccasionButton(occasion);

        step("Verify user redirected to select card design page.");
        Assertion.assertThat("Verify user redirected to select card design page", giftCardPage.verifyRedirectionProcessSuccess(), is(true));

        step("Verify the occasion category selection and card design selection as user selected on previous page.");
        Assertion.assertThat("Verify user occasion category selection and card design selection.", giftCardPage.verifyRedirectToSelectSpecificCardDesign(occasion), is(true));
    }
}
