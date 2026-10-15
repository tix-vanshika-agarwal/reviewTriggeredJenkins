package com.tiket.test.promo;


import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Environment;
import com.tiket.model.Platform;
import com.tiket.model.Session;
import com.tiket.page.accommodation.hotel.bookingformpage.BookingFormPage;
import com.tiket.page.accommodation.hotel.detailpage.DetailPage;
import com.tiket.page.accommodation.hotel.landingpage.LandingPage;
import com.tiket.page.accommodation.hotel.roomlistpage.RoomListPage;
import com.tiket.page.accommodation.hotel.searchresultpage.SearchResultPage;
import com.tiket.page.account.AccountPage;
import com.tiket.page.home.HomePage;
import com.tiket.page.login.LoginPage;
import com.tiket.page.order.orderlist.OrderListPage;
import com.tiket.page.payment.PaymentPage;
import com.tiket.page.promo.gift_card_creation_page.GiftCardCreationPage;
import com.tiket.page.promo.gift_card_detail_page.GiftCardDetailPage;
import com.tiket.page.promo.gift_card_landing_page.GiftCardPage;
import com.tiket.page.promo.gift_card_splitting_page.GiftCardSplittingPage;
import com.tiket.page.setting.SettingPage;
import com.tiket.pojo.giftCard.GiftCardOrderResponse;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.testbase.Configs;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import java.util.Map;

import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;

public class PromoTest extends BaseTest{

    @Author(names = {"Aditi"})
    @Module(name = "PromoSuggestion")
    @Tribe(name = "Promo")
    @Vertical(name = "Promo")
    @Test
    public void testCheckPromoSuggestionEligibleSection() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testCheckPromoSuggestionEligibleSection");
        BaseTest.paramsThreadLocal.set(param);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String hotelName = param.get("hotelName");

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginAsGuest();
        loginPage.loginWith(loginType, username, password);
        homePage.changeCurrency("IDR", username);

        step("Open hotel landing page");
        homePage.openVerticalPageDeeplink("Hotel", "", "");

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
        roomListPage.getTheFirstHotelRoomPrice();
        roomListPage.clickBookRoom();

        step("Submit booking");
        bookingFormPage.continueToPaymentPage();

        step("Payment page should be displayed");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("Order id should be displayed", !orderID.isEmpty(), is(true));

        step("Open voucher and promo card section");
        paymentPage.seeAllPromoCodes();

        step("Are the eligible promo code visible");
        Assertion.assertThat("Eligible promo cards are displayed", paymentPage.checkEligiblePromoCodesDisplayed(), is(true));

    }

    @Author(names = {"Aditi"})
    @Module(name = "PromoSuggestion")
    @Tribe(name = "Promo")
    @Vertical(name = "Promo")
    @Test
    public void testCheckPromoSuggestionNotEligibleSection() throws InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testCheckPromoSuggestionNotEligibleSection");
        BaseTest.paramsThreadLocal.set(param);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String hotelName = param.get("hotelName");
        String date_type = param.get("date_type");
        String date = param.get("date");

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginAsGuest();
        loginPage.loginWith(loginType, username, password);
        homePage.changeCurrency("IDR", username);

        step("Open hotel landing page");
        homePage.openVerticalPageDeeplink("Hotel", "", "");

        step("Search stay location");
        landingPage.searchStayLocation(hotelName);

        step("set stay date");
        landingPage.setStayDate(date_type, date);

        step("Submit search");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        step("Go to the room list");
        detailPage.RemovePopUp();
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();

        step("Select room from the room list");
        detailPage.RemovePopUp();
        roomListPage.getTheFirstHotelRoomPrice();
        roomListPage.clickBookRoom();

        step("Submit booking");
        bookingFormPage.continueToPaymentPage();

        step("Payment page should be displayed");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("Order id should be displayed", !orderID.isEmpty(), is(true));

        step("Open voucher and promo card section");
        paymentPage.seeAllPromoCodes();

        step("Are the not eligible promo cards visible");
        Assertion.assertThat("Not eligible promo cards are displayed", paymentPage.checkNotEligiblePromoCodesDisplayed(), is(true));

    }

    @Author(names = {"Aditi"})
    @Module(name = "PromoSuggestion")
    @Tribe(name = "Promo")
    @Vertical(name = "Promo")
    @Test
    public void testCheckBestPromoSuggestion() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testCheckBestPromoSuggestion");
        BaseTest.paramsThreadLocal.set(param);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String hotelName = param.get("hotelName");

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginAsGuest();
        loginPage.loginWith(loginType, username, password);
        homePage.changeCurrency("IDR", username);

        step("Open hotel landing page");
        homePage.openVerticalPageDeeplink("Hotel", "", "");

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
        roomListPage.getTheFirstHotelRoomPrice();
        roomListPage.clickBookRoom();

        step("Submit booking");
        bookingFormPage.continueToPaymentPage();

        step("Payment page should be displayed");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("Order id should be displayed", !orderID.isEmpty(), is(true));

        step("Open voucher and promo card section");
        paymentPage.seeAllPromoCodes();

        step("Is the best suitable promo visible");
        Assertion.assertThat("Best suitable promo is visible along with the Apply button", paymentPage.isBestPromoDisplayed(), is(true));

    }

    @Author(names = {"Aditi"})
    @Module(name = "PromoSuggestion")
    @Tribe(name = "Promo")
    @Vertical(name = "Promo")
    @Test
    public void testCheckClosePromoSuggestionDrawer() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testCheckClosePromoSuggestionDrawer");
        BaseTest.paramsThreadLocal.set(param);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String hotelName = param.get("hotelName");

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginAsGuest();
        loginPage.loginWith(loginType, username, password);
        homePage.changeCurrency("IDR", username);

        step("Open hotel landing page");
        homePage.openVerticalPageDeeplink("Hotel", "", "");

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
        roomListPage.getTheFirstHotelRoomPrice();
        roomListPage.clickBookRoom();

        step("Submit booking");
        bookingFormPage.continueToPaymentPage();

        step("Payment page should be displayed");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("Order id should be displayed", !orderID.isEmpty(), is(true));

        step("Open voucher and promo card section");
        paymentPage.seeAllPromoCodes();

        step("Is the close visible");
        Assertion.assertThat("Close button is present on the promo suggestion page", paymentPage.isPromoCloseButtonDisplayed(), is(true));

    }

    @Author(names = {"Prerna"})
    @Module(name = "PromoSuggestion")
    @Tribe(name = "Promo")
    @Vertical(name = "Promo")
    @Test
    public void testApplyPromoInvalidVoucher() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testApplyPromoInvalidVoucher");
        BaseTest.paramsThreadLocal.set(param);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String hotelName = param.get("hotelName");
        String promo = CoreFunctions.generateRandomString(8);
        String promoCodeErrorEN = param.get("promoCodeErrorEN");
        String promoCodeErrorIN = param.get("promoCodeErrorIN");

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginAsGuest();
        loginPage.loginWith(loginType, username, password);
        homePage.changeCurrency("IDR", username);

        step("Open hotel landing page");
        homePage.openVerticalPageDeeplink("Hotel", "", "");

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
        roomListPage.getTheFirstHotelRoomPrice();
        roomListPage.clickBookRoom();

        step("Submit booking");
        bookingFormPage.continueToPaymentPage();

        step("Payment page should be displayed");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("Order id should be displayed", !orderID.isEmpty(), is(true));

        step("Open voucher and promo card section");
        paymentPage.seeAllPromoCodes();

        step("Input Promo String " + promo);
        paymentPage.applyPromoCode(promo);

        step("Check Error Message");
        boolean promoReasonCorrect = paymentPage.isErrorMessageCorrect(promoCodeErrorEN, promoCodeErrorIN);
        Assertion.assertThat("Verify error message", promoReasonCorrect, is(true));
    }

    @Author(names = {"Prerna"})
    @Module(name = "GiftVoucher")
    @Tribe(name = "Promo")
    @Vertical(name = "Promo")
    @Test
    public void testUnApplyGVFlow() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testUnApplyGVFlow");
        BaseTest.paramsThreadLocal.set(param);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String hotelName = param.get("hotelName");
        String gv = param.get("GV");

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginAsGuest();
        loginPage.loginWith(loginType, username, password);
        homePage.changeCurrency("IDR", username);

        step("Open hotel landing page");
        homePage.openVerticalPageDeeplink("Hotel", "", "");

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
        roomListPage.getTheFirstHotelRoomPrice();
        roomListPage.clickBookRoom();

        step("Submit booking");
        bookingFormPage.continueToPaymentPage();

        step("Payment page should be displayed");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("Order id should be displayed", !orderID.isEmpty(), is(true));

        step("Get original total price");
        String totalPrice = paymentPage.getTotalPrice();

        step("Open voucher and promo card section");
        paymentPage.seeAllPromoCodes();

        step("Input GV String " + gv);
        paymentPage.applyPromoCode(gv);

        step("Check voucher promo code applied");
        Assertion.assertThat("Verify discount icon present", paymentPage.isDiscountIconPresent(), is(true));

        step("Check for voucher promo strikeout/crossed price");
        String newPrice = paymentPage.getTotalPrice();
        String discount = paymentPage.getDiscountAmmount();
        Assertion.assertThat("Verify that the price has been correctly calculated", CoreFunctions.convertPrice(totalPrice) - CoreFunctions.convertPrice(discount), is(CoreFunctions.convertPrice(newPrice)));
        Assertion.assertThat("verify new price is less than price without GV", CoreFunctions.convertPrice(newPrice)< CoreFunctions.convertPrice(totalPrice), is(true));

        step("cancel voucher promo");
        paymentPage.unApplyPromoCode();

        step("Check promo has been unapply");
        Assertion.assertThat("Verify promo has been unapply", paymentPage.verifyPromoUnApply(), is(true));
    }

    @Author(names = {"Prerna"})
    @Module(name = "promo")
    @Tribe(name = "Promo")
    @Vertical(name = "Promo")
    @Test
    public void testUnApplyInvalidPaymentMethod() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testUnApplyInvalidPaymentMethod");
        BaseTest.paramsThreadLocal.set(param);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String hotelName = param.get("hotelName");
        String promo = param.get("promo");
        String paymentMethod = param.get("paymentMethod");
        String currency = "IDR";

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginAsGuest();
        loginPage.loginWith(loginType, username, password);
        homePage.changeCurrency(currency, username);

        step("Open hotel landing page");
        homePage.openVerticalPageDeeplink("Hotel", "", "");

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
        roomListPage.getTheFirstHotelRoomPrice();
        roomListPage.clickBookRoom();

        step("Submit booking");
        bookingFormPage.continueToPaymentPage();

        step("Payment page should be displayed");
        String orderID = paymentPage.checkOrderID();
        paymentPage.openPaymentMethod();
        paymentPage.selectPaymentMethod(paymentMethod);
        Assertion.assertThat("Order id should be displayed", !orderID.isEmpty(), is(true));

        step("Get original total price");
        String totalPrice = paymentPage.getTotalPrice();

        step("Open voucher and promo card section");
        paymentPage.seeAllPromoCodes();

        step("Input GV String " + promo);
        paymentPage.applyPromoCode(promo);

        step("Check voucher promo code applied");
        Assertion.assertThat("Verify discount icon present", paymentPage.isDiscountIconPresent(), is(true));
        String promoAmount = paymentPage.getDiscountAmountOnPromoCodeSection(currency);

        step("Check for voucher promo strikeout/crossed price");
        String newPrice = paymentPage.getTotalPrice();
        String discount = paymentPage.getDiscountAmmount();
        Assertion.assertThat("Verify that the price has been correctly calculated", CoreFunctions.convertPrice(totalPrice) - CoreFunctions.convertPrice(discount), is(CoreFunctions.convertPrice(newPrice)));
        Assertion.assertThat("verify new price is less than price without GV", CoreFunctions.convertPrice(newPrice)< CoreFunctions.convertPrice(totalPrice), is(true));

        step("Go to home page");
        homePage.goToHomeDeeplink();

        step("open My order page");
        homePage.openMyOrderPage();

        step("open order from waiting payment list "+ orderID);
        orderListPage.openOrderCompletePayment(orderID);

        step("Click Continue without promo button");
        paymentPage.clickContinueWithoutPromo();

        step("Get Price after promo");
        String totalPriceAfterPromo = paymentPage.getTotalPrice();

        step("Check promo has been unapply");
        Assertion.assertThat("Verify promo has been unapply", paymentPage.verifyPromoUnApply(), is(true));
        Assertion.assertThat("Price back to normal", totalPrice, equalTo(totalPriceAfterPromo));
    }

    @Author(names = {"Alden"})
    @Module(name = "GiftCard")
    @Tribe(name = "Promo")
    @Vertical(name = "Promo")
    @Test
    public void verifyRedeemGiftCardNonLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyRedeemGiftCardNonLogin");
        BaseTest.paramsThreadLocal.set(param);

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        GiftCardPage<WebDriver> giftCardPage = GiftCardPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("Login as guest");
        loginPage.loginAsGuest();

        step("change currency to IDR");
        homePage.changeCurrency("IDR", "");

        step("change country to Indonesia");
        accountPage.chooseCountry("Indonesia");

        step("Open gift card page");
        homePage.deeplinkToGiftCardPage("IDR");

        step("Redeem gift card and verify login bottomsheet is showing");
        giftCardPage.clickRedeemGiftCard();
        Assertion.assertThat("Verify login bottomsheet is showing", giftCardPage.isLoginDialogShown(), is(true));
    }

    @Author(names = {"Alden"})
    @Module(name = "GiftCard")
    @Tribe(name = "Promo")
    @Vertical(name = "Promo")
    @Test
    public void verifyRedeemGiftCardInvalidLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyRedeemGiftCardInvalidLogin");
        BaseTest.paramsThreadLocal.set(param);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String giftCode = param.get("giftCode");

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        GiftCardPage<WebDriver> giftCardPage = GiftCardPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink and change currency to IDR");
        homePage.loginWithSpecificAccountViaDeeplink(username, password, "IDR");

        step("change country to Indonesia");
        accountPage.chooseCountry("Indonesia");

        step("Open gift card page");
        homePage.deeplinkToGiftCardPage("IDR");

        step("Redeem gift card and input code");
        giftCardPage.clickRedeemGiftCard();
        giftCardPage.inputGiftCode(giftCode);
        giftCardPage.clickCodeRedeemButton();

        step("verify code cannot be redeemed");
        Assertion.assertThat("redeem unsuccessful", giftCardPage.codeRedeemSuccessful(), is(false));
    }

    @Author(names = {"Alden"})
    @Module(name = "GiftCard")
    @Tribe(name = "Promo")
    @Vertical(name = "Promo")
    @Test
    public void verifyRedeemGiftCardValidLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyRedeemGiftCardValidLogin");
        BaseTest.paramsThreadLocal.set(param);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String giftCode = param.get("giftCode");
        String apiEmail = param.get("apiEmail");
        String apiProfileId = param.get("apiProfileId");
        String fullName = CoreFunctions.randomName() + " Automation";
        String phone = CoreFunctions.randomPhone();
        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String salutation = CoreFunctions.getRandomObject(salutationList);
        int voucherAmount = Integer.parseInt(param.get("voucherAmount"));
        String apiPassword = param.get("apiPassword");
        String orderId = "";
        String orderHash = "";

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        GiftCardPage<WebDriver> giftCardPage = GiftCardPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);

        if (giftCode.equals("DYNAMIC")) {
            step("create gift card order via API");
            GiftCardOrderResponse orderDetailsData = giftCardPage.createGiftCardOrderViaAPI(
                    "", voucherAmount, apiProfileId, apiEmail, fullName, phone, salutation, apiEmail,apiPassword);
            GiftCardOrderResponse.Order orderDetails = orderDetailsData.order;
            orderId = orderDetails.orderId;
            orderHash = orderDetails.orderHash;
        }

        step("Goto home");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink and change currency to IDR");
        homePage.loginWithSpecificAccountViaDeeplink(username, password, "IDR");

        step("change country to Indonesia");
        accountPage.chooseCountry("Indonesia");

        if (giftCode.equals("DYNAMIC")) {
            step("pay gift card");
            Assertion.assertThat("gift card is paid", giftCardPage.verifyGiftCardOrderPayment(orderId, orderHash), is("PAID"));

            step("get redeem code");
            Assertion.assertThat("gift card is issued", giftCardPage.verifyissuance(orderId, orderHash), is("ISSUED"));
            giftCode = giftCardPage.getRedeemCode(orderId, orderHash);
        }

        step("Open gift card page");
        homePage.deeplinkToGiftCardPage("IDR");

        step("Redeem gift card and input code");
        giftCardPage.clickRedeemGiftCard();
        giftCardPage.inputGiftCode(giftCode);
        giftCardPage.clickCodeRedeemButton();

        step("verify code can be redeemed");
        Assertion.assertThat("redeem successful", giftCardPage.codeRedeemSuccessful(), is(true));

        step("scratch and see split options");
        giftCardPage.scratchGiftCard();
        Assertion.assertThat("verify voucher can be split", giftCardPage.verifySplitOption(), is(true));
    }

    @Author(names = {"Alden"})
    @Module(name = "GiftCard")
    @Tribe(name = "Promo")
    @Vertical(name = "Promo")
    @Test
    public void verifyRedeemGiftCardValidNonIdrLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyRedeemGiftCardValidNonIdrLogin");
        BaseTest.paramsThreadLocal.set(param);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String giftCode = param.get("giftCode");
        String currency = param.get("currency");
        String apiEmail = param.get("apiEmail");
        String apiProfileId = param.get("apiProfileId");
        String fullName = CoreFunctions.randomName() + " Automation";
        String phone = CoreFunctions.randomPhone();
        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String salutation = CoreFunctions.getRandomObject(salutationList);
        int voucherAmount = Integer.parseInt(param.get("voucherAmount"));
        String apiPassword = param.get("apiPassword");

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        GiftCardPage<WebDriver> giftCardPage = GiftCardPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink" + currency);
        homePage.loginWithSpecificAccountViaDeeplinkOnly(username, password);

        if (giftCode.equals("DYNAMIC") && (Configs.ENVIRONMENT == Environment.PREPROD || Configs.ENVIRONMENT == Environment.GK)) {
            step("create gift card order via API " + apiEmail);
            GiftCardOrderResponse orderDetailsData = giftCardPage.createGiftCardOrderViaAPI(
                    currency, voucherAmount, apiProfileId, apiEmail, fullName, phone, salutation, apiEmail, apiPassword);

            step("pay via API");
            GiftCardOrderResponse.Order orderDetails = orderDetailsData.order;
            String orderId = orderDetails.orderId;
            String orderHash = orderDetails.orderHash;

            step("pay gift card");
            Assertion.assertThat("gift card is paid", giftCardPage.verifyGiftCardOrderPayment(orderId, orderHash), is("PAID"));

            step("get redeem code");
            Assertion.assertThat("gift card is issued", giftCardPage.verifyissuance(orderId, orderHash), is("ISSUED"));
            giftCode = giftCardPage.getRedeemCode(orderId, orderHash);
        }

        step("Open gift card page");
        homePage.deeplinkToGiftCardPage(currency);

        step("Redeem gift card and input code");
        giftCardPage.clickRedeemGiftCard();
        giftCardPage.inputGiftCode(giftCode);
        giftCardPage.clickCodeRedeemButton();

        step("verify code can be redeemed");
        Assertion.assertThat("redeem successful", giftCardPage.codeRedeemSuccessful(), is(true));

        step("scratch and see split options");
        giftCardPage.scratchGiftCard();
        Assertion.assertThat("verify voucher can be split", giftCardPage.verifySplitOption(), is(true));
    }

    @Author(names = {"Alden"})
    @Module(name = "GiftCard")
    @Tribe(name = "Promo")
    @Vertical(name = "Promo")
    @Test
    public void verifySplitGiftCardLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifySplitGiftCardLogin");
        BaseTest.paramsThreadLocal.set(param);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String apiEmail = param.get("apiEmail");
        String apiPassword = param.get("apiPassword");
        String apiProfileId = param.get("apiProfileId");
        String fullName = CoreFunctions.randomName() + " Automation";
        String phone = CoreFunctions.randomPhone();
        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String salutation = CoreFunctions.getRandomObject(salutationList);


        int voucherAmount = Integer.parseInt(param.get("voucherAmount"));

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        GiftCardPage<WebDriver> giftCardPage = GiftCardPage.getInstance(session);
        GiftCardSplittingPage<WebDriver> giftCardSplittingPage = GiftCardSplittingPage.getInstance(session);

        step("create gift card order via API");
        GiftCardOrderResponse orderDetailsData = giftCardPage.createGiftCardOrderViaAPI(
                "", voucherAmount, apiProfileId, apiEmail, fullName, phone, salutation, apiEmail, apiPassword);

        step("pay via API");
        GiftCardOrderResponse.Order orderDetails = orderDetailsData.order;
        String orderId = orderDetails.orderId;
        String orderHash = orderDetails.orderHash;

        step("pay gift card");
        Assertion.assertThat("gift card is paid", giftCardPage.verifyGiftCardOrderPayment(orderId, orderHash), is("PAID"));

        step("get redeem code");
        Assertion.assertThat("gift card is issued", giftCardPage.verifyissuance(orderId, orderHash), is("ISSUED"));
        String giftCode = giftCardPage.getRedeemCode(orderId, orderHash);

        step("Goto home");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink");
        homePage.loginIfLoggedOutAndChangeCurrencyViaDeeplink(username, password,"IDR");

        step("Open gift card page");
        homePage.deeplinkToGiftCardPage("IDR");

        step("Redeem gift card and input code");
        giftCardPage.clickRedeemGiftCard();
        giftCardPage.inputGiftCode(giftCode);
        giftCardPage.clickCodeRedeemButton();

        step("verify code can be redeemed");
        Assertion.assertThat("redeem successful", giftCardPage.codeRedeemSuccessful(), is(true));

        step("scratch and see split options");
        giftCardPage.scratchGiftCard();
        Assertion.assertThat("verify voucher can be split", giftCardPage.verifySplitOption(), is(true));

        step("split gift card");
        giftCardPage.splitGiftCard();
        String splittedCode = giftCardSplittingPage.getSplittedVoucherCode();
        giftCardSplittingPage.openVoucherBox();
        Assertion.assertThat("Gift card voucher code exists in voucher box", giftCardSplittingPage.verifyCodeinVoucherBox(splittedCode), is(true));
    }

    @Author(names = {"Alden"})
    @Module(name = "GiftCard")
    @Tribe(name = "Promo")
    @Vertical(name = "Promo")
    @Test
    public void verifySplitGiftCardNonIdrLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifySplitGiftCardNonIdrLogin");
        BaseTest.paramsThreadLocal.set(param);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String currency = param.get("currency");
        String apiEmail = param.get("apiEmail");
        String apiPassword = param.get("apiPassword");
        String apiProfileId = param.get("apiProfileId");
        String fullName = CoreFunctions.randomName() + " Automation";
        String phone = CoreFunctions.randomPhone();
        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String salutation = CoreFunctions.getRandomObject(salutationList);
        int voucherAmount = Integer.parseInt(param.get("voucherAmount"));

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        GiftCardPage<WebDriver> giftCardPage = GiftCardPage.getInstance(session);
        GiftCardSplittingPage<WebDriver> giftCardSplittingPage = GiftCardSplittingPage.getInstance(session);

        step("create gift card order via API");
        GiftCardOrderResponse orderDetailsData = giftCardPage.createGiftCardOrderViaAPI(
                currency, voucherAmount, apiProfileId, apiEmail, fullName, phone, salutation, apiEmail, apiPassword);

        step("pay via API");
        GiftCardOrderResponse.Order orderDetails = orderDetailsData.order;
        String orderId = orderDetails.orderId;
        String orderHash = orderDetails.orderHash;

        step("pay gift card");
        Assertion.assertThat("gift card is paid", giftCardPage.verifyGiftCardOrderPayment(orderId, orderHash), is("PAID"));

        step("get redeem code");
        Assertion.assertThat("gift card is issued", giftCardPage.verifyissuance(orderId, orderHash), is("ISSUED"));
        String giftCode = giftCardPage.getRedeemCode(orderId, orderHash);

        step("Goto home");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink" + currency);
        homePage.loginWithSpecificAccountViaDeeplinkOnly(username, password);

        step("Open gift card page");
        homePage.deeplinkToGiftCardPage(currency);

        step("Redeem gift card and input code");
        giftCardPage.clickRedeemGiftCard();
        giftCardPage.inputGiftCode(giftCode);
        giftCardPage.clickCodeRedeemButton();

        step("verify code can be redeemed");
        Assertion.assertThat("redeem successful", giftCardPage.codeRedeemSuccessful(), is(true));

        step("scratch and see split options");
        giftCardPage.scratchGiftCard();
        Assertion.assertThat("verify voucher can be split", giftCardPage.verifySplitOption(), is(true));

        step("split gift card");
        giftCardPage.splitGiftCard();
        String splittedCode = giftCardSplittingPage.getSplittedVoucherCode();
        giftCardSplittingPage.openVoucherBox();
        Assertion.assertThat("Gift card voucher code exists in voucher box", giftCardSplittingPage.verifyCodeinVoucherBox(splittedCode), is(true));
    }

    @Author(names = {"Alden"})
    @Module(name = "GiftCard")
    @Tribe(name = "Promo")
    @Vertical(name = "Promo")
    @Test
    public void verifyCreateGiftCardNonLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyCreateGiftCardNonLogin");
        BaseTest.paramsThreadLocal.set(param);

        String paymentMethod = param.get("paymentMethod");

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        GiftCardPage<WebDriver> giftCardPage = GiftCardPage.getInstance(session);
        GiftCardCreationPage<WebDriver> giftCardCreationPage = GiftCardCreationPage.getInstance(session);
        GiftCardDetailPage<WebDriver> giftCardDetailPage = GiftCardDetailPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("Logout via deeplink");
        homePage.logoutViaDeeplink();

        step("Change currency to IDR");
        homePage.changeCurrency("IDR","");

        step("Open gift card page");
        homePage.deeplinkToGiftCardPage("IDR");

        step("Create gift card");
        giftCardPage.clickSendGiftCard();
        giftCardCreationPage.fillInCardDetail();
        giftCardCreationPage.clickContinueButtonFooter();

        step("Fill in Contact details");
        giftCardDetailPage.fillInGuestDetail();
        giftCardDetailPage.inputSendeName();
        giftCardDetailPage.clickContinueToPayment();

        step("pay gift card");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("Order id should be displayed", !orderID.isEmpty(), is(true));

        if (Configs.ENVIRONMENT != Environment.PROD) {
            step("select payment method");
            paymentPage.openPaymentMethod();
            paymentPage.selectPaymentMethod(paymentMethod);
            paymentPage.clickPayButton();

            step("verify creation successful");
            paymentPage.SeeOrderListAfterPaymentSuccess();
            orderListPage.openIssuedTicketViaDeeplink(orderID);
            Assertion.assertThat("Gift card details page is displayed", orderListPage.verifyGiftOrderDetails(), is(true));
        }
    }

    @Author(names = {"Alden"})
    @Module(name = "GiftCard")
    @Tribe(name = "Promo")
    @Vertical(name = "Promo")
    @Test
    public void verifyCreateGiftCardLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyCreateGiftCardLogin");
        BaseTest.paramsThreadLocal.set(param);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String paymentMethod = param.get("paymentMethod");

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        GiftCardPage<WebDriver> giftCardPage = GiftCardPage.getInstance(session);
        GiftCardCreationPage<WebDriver> giftCardCreationPage = GiftCardCreationPage.getInstance(session);
        GiftCardDetailPage<WebDriver> giftCardDetailPage = GiftCardDetailPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink and change currency to IDR");
        homePage.loginWithSpecificAccountViaDeeplink(username, password, "IDR");

        step("Open gift card page");
        homePage.deeplinkToGiftCardPage("IDR");

        step("Create gift card");
        giftCardPage.clickSendGiftCard();
        giftCardCreationPage.fillInCardDetail();
        giftCardCreationPage.clickContinueButtonFooter();

        step("Fill in Contact details");
        giftCardDetailPage.inputSendeName();
        giftCardDetailPage.clickContinueToPayment();

        step("pay gift card");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("Order id should be displayed", !orderID.isEmpty(), is(true));

        if (Configs.ENVIRONMENT != Environment.PROD) {
            step("select payment method");
            paymentPage.openPaymentMethod();
            paymentPage.selectPaymentMethod(paymentMethod);
            paymentPage.clickPayButton();

            step("verify creation successful");
            paymentPage.SeeOrderListAfterPaymentSuccess();
            orderListPage.openIssuedTicketViaDeeplink(orderID);
            Assertion.assertThat("Gift card details page is displayed", orderListPage.verifyGiftOrderDetails(), is(true));

            step("Verify that the redeem code is present on the order details page");
            Assertion.assertThat("Redeem code is displayed", orderListPage.verifyRedeemCodePresent(), is(true));
        }
    }

    @Author(names = {"Aditi"})
    @Module(name = "GiftCard")
    @Tribe(name = "Promo")
    @Vertical(name = "Promo")
    @Test
    public void verifyCreateGiftCardLoginNonIDR() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyCreateGiftCardLoginNonIDR");
        BaseTest.paramsThreadLocal.set(param);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String currency = param.get("currency");
        String cardNumber = param.get("cardNumber");
        String expiryDate = param.get("expiry");
        String cvv = param.get("cvv");

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        GiftCardPage<WebDriver> giftCardPage = GiftCardPage.getInstance(session);
        GiftCardCreationPage<WebDriver> giftCardCreationPage = GiftCardCreationPage.getInstance(session);
        GiftCardDetailPage<WebDriver> giftCardDetailPage = GiftCardDetailPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink and change currency to " + currency);
        homePage.loginWithSpecificAccountViaDeeplink(username, password, currency);

        step("Open gift card page");
        homePage.deeplinkToGiftCardPage(currency);

        step("Create gift card");
        giftCardPage.clickSendGiftCard();
        giftCardCreationPage.fillInCardDetail();
        giftCardCreationPage.clickContinueButtonFooter();

        step("Fill in Contact details");
        giftCardDetailPage.inputSendeName();
        giftCardDetailPage.clickContinueToPayment();

        step("pay the gift card");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("Order id should be displayed", !orderID.isEmpty(), is(true));

        if (Configs.ENVIRONMENT == Environment.PREPROD || Configs.ENVIRONMENT == Environment.GK)
        {
            step("select payment method");
            paymentPage.fillCardDetailPayment(cardNumber, expiryDate, cvv, "sdet auto");

            step("Click pay");
            paymentPage.clickPayButton();

            step("Verify 3DS if applicable");
            paymentPage.verifyCreditCard3ds();

            step("verify creation successful");
            paymentPage.SeeOrderListAfterPaymentSuccess();
            orderListPage.openIssuedTicketViaDeeplink(orderID);
            Assertion.assertThat("Gift card details page is displayed", orderListPage.verifyGiftOrderDetails(), is(true));

            step("Verify that the redeem code is present on the order details page");
            Assertion.assertThat("Redeem code is displayed", orderListPage.verifyRedeemCodePresent(), is(true));

        }

    }

    @Author(names = {"Alden"})
    @Module(name = "GiftCard")
    @Tribe(name = "Promo")
    @Vertical(name = "Promo")
    @Test
    public void verifyCreateGiftCardExceedingLimitLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyCreateGiftCardExceedingLimitLogin");
        BaseTest.paramsThreadLocal.set(param);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        GiftCardPage<WebDriver> giftCardPage = GiftCardPage.getInstance(session);
        GiftCardCreationPage<WebDriver> giftCardCreationPage = GiftCardCreationPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink and change currency to IDR");
        homePage.loginWithSpecificAccountViaDeeplink(username, password, "IDR");

        step("Open gift card page");
        homePage.deeplinkToGiftCardPage("IDR");

        step("Create gift card");
        giftCardPage.clickSendGiftCard();
        giftCardCreationPage.fillInCardDetail(10000000);

        step("Verify amount error is shown");
        Assertion.assertThat("Amount error is shown", giftCardCreationPage.isAmountErrorShown(), is(true));
    }
}
