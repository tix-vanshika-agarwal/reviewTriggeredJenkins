package com.tiket.web.test.platform.liberty;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.bq.OutboundNotificationVerifier;
import com.tiket.core.CoreFunctions;
import com.tiket.dbclient.bigquery.NotificationChannel;
import com.tiket.dbclient.bigquery.NotificationTemplateType;
import com.tiket.model.Platform;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.testbase.Configs;
import com.tiket.web.dWeb.page.accommodation.hotels.HotelDetailPage;
import com.tiket.web.dWeb.page.account.InboxPage;
import com.tiket.web.dWeb.page.account.PointHistoryPage;
import com.tiket.web.dWeb.page.payment.PaymentPage;
import com.tiket.web.dashboard.page.order.orderCreationPage;
import com.tiket.web.page.accommodation.hotels.BookingForm.HotelBookingFormPageWeb;
import com.tiket.web.page.accommodation.hotels.hotelLandingPage.HotelLandingPageWeb;
import com.tiket.web.page.accommodation.hotels.hotelRoomListPage.HotelRoomListPageWeb;
import com.tiket.web.page.common.home.HomePageWeb;
import com.tiket.web.page.common.home.LoginPageWeb;
import com.tiket.web.page.order.OrderDetail.OrderDetailWeb;
import com.tiket.web.page.order.OrderList.OrderListWeb;
import com.tiket.web.page.platform.eiffel.loyalty.LoyaltyPointPageWeb;
import com.tiket.web.page.platform.sphinx.refund.RefundDetailWeb;
import com.tiket.web.page.platform.sphinx.refund.RefundPageWeb;
import com.tiket.web.page.ttd.TtdBookingFormPage.TtdBookingFormPageDweb;
import com.tiket.web.page.ttd.TtdBookingFormPage.TtdBookingFormPageWeb;
import com.tiket.web.page.ttd.TtdLandingPage.TtdLandingPageWeb;
import com.tiket.web.page.ttd.TtdProductDetailPage.TtdProductDetailPageWeb;
import org.hamcrest.Matchers;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import javax.security.auth.login.FailedLoginException;
import java.util.Map;

import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;

public class MessageTest extends BaseTest {
    @Author(names = "Rizki Maulana")
    @Module(name = "Email Notification")
    @Tribe(name = "Message")
    @Vertical(name = "Platform")
    @Test
    public void verifyEmailProcessPaymentAfterCheckoutOrder() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("verifyEmailProcessPaymentAfterCheckoutOrder");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        HotelRoomListPageWeb<?> roomListPage = HotelRoomListPageWeb.getInstance(session);
        HotelDetailPage<?> detailPage = new HotelDetailPage<>(session);
        HotelBookingFormPageWeb<?> bookingFormPage = HotelBookingFormPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String location = param.get("location");
        String salutation = param.get("salutation");
        int rooms = Integer.parseInt(param.get("rooms"));
        int guests = Integer.parseInt(param.get("guests"));
        String dateRange = param.get("dateRange");
        String dateType = param.get("dateType");
        String currency = param.get("currency");
        String paymentMethod = param.get("paymentMethod");
        String paymentGroup = param.get("paymentGroup");

        step("Go to Home Page");
        homePage.goToHomePage();

        step("Login as " + email + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(email, password);

        step("Select currency: " + currency);
        homePage.selectCurrency(currency);

        step("Navigate to Hotel vertical and search for stay");
        homePage.openVerticalPageViaURL("Hotel");
        landingPage.searchStayLocation(location);

        step("Set date");
        landingPage.setStayDate(dateType,dateRange);
        landingPage.clickSubmit();

        step("Go Inside PDP");
        Assertion.assertThat("PDP is loaded", detailPage.isPdpLoaded(), Matchers.is(true));
        detailPage.seeHotelRoom();

        step("Set guest and room count");
        roomListPage.clickRoomListChangeButton();
        roomListPage.clickRoomNGuestButton();
        roomListPage.setTotalGuest(guests);
        roomListPage.setTotalRoom(rooms);
        roomListPage.clickGuestDoneAndSubmitButton();

        step("Click select on Room List");
        roomListPage.clickBookRoomButton();

        step("Click book button");
        bookingFormPage.fillSalutations(salutation);
        bookingFormPage.fillGuestDetailsCombinedRandom(guests);
        bookingFormPage.continueToPayment();
        bookingFormPage.continueToPaymentPage();

        step("Submit payment method");
        String orderId = paymentPage.getOrderId();
        Assertion.assertThat("Order Id successfully generated", orderId, notNullValue());

        paymentPage.clickOnSelectPaymentMethod();
        paymentPage.goToPaymentMethodsGroup(paymentGroup);
        paymentPage.selectPaymentMethod(paymentMethod);
        paymentPage.clickOnPaymentButton();
        paymentPage.clickContinueToPaymentPopUp();

        step("Check issued on order detail page");
        orderListPage.openActiveTicketViaURL(orderId);
        orderDetailPage.skipCoachmark();
        Assertion.assertThat("Order successfully issued", orderDetailPage.validateCompletePaymentButtonExist(), is(true));

        step("Check email for the order");
        OutboundNotificationVerifier.verify(email, orderId, NotificationChannel.EMAIL, NotificationTemplateType.CHECKOUT);
    }

    @Author(names = "Ram Pratap Singh, Rizki Maulana, Hafiz")
    @Module(name = "Push Notification")
    @Tribe(name = "Message")
    @Vertical(name = "Platform")
    @Test
    public void verifyPushNotificationRequestRefund() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("verifyPushNotificationRequestRefund");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        RefundPageWeb<?> refundPage = RefundPageWeb.getInstance(session);
        RefundDetailWeb<?> refundDetailWeb = RefundDetailWeb.getInstance(session);
        InboxPage<?> inboxPage = new InboxPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String productName = param.get("productName");
        boolean refundable = Boolean.parseBoolean(param.get("refundable"));
        String currency = param.get("currency");
        String contactFullName = param.get("contactFullName");
        String contactSalutation = param.get("contactSalutation");
        String guestFullName = param.get("guestFullName");
        String guestEmailAddress = param.get("guestEmailAddress");
        String guestSalutation = param.get("guestSalutation");

        step("Go to Home Page");
        homePage.goToHomePage();

        step("Login as " + username + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(username, password);

        step("Book refundable order for prerequisite");
        orderCreationPage<?> orderCreationPage = new orderCreationPage<>(session);
        orderCreationPage.createIssuedOrderApiTodo(username, password, productName, refundable, currency, contactFullName, contactSalutation, guestFullName, guestEmailAddress, guestSalutation);
        int orderId = orderCreationPage.getOrderId();

        step("Open Reschedule Page");
        orderListPage.openActiveTicketViaURL(String.valueOf(orderId));
        orderDetailPage.clickRefundButtonTTD();
        refundPage.skipRescheduleNudgeIfShown();

        step("Submit refund");
        refundPage.selectRandomRefundReason();
        refundPage.continueRefund();
        refundPage.clickChecklistTnc();
        refundPage.clickSubmitRefund();
        refundPage.clickProcessRefund();
        refundPage.clickGoToOrders();

        step("Check refund is issued");
        String refundId = refundDetailWeb.getRefundId();
        Assertion.assertThat("Verify refund id is generated", refundId, notNullValue());

        step("Check push notification for the order");
        homePage.goToHomePage();
        homePage.openInbox();
        Assertion.assertThat("Verify push notification received", inboxPage.getInboxTitle(0),
                anyOf(
                        containsStringIgnoringCase("funds have been transferred"),
                        containsStringIgnoringCase("your refund has been disbursed"),
                        containsStringIgnoringCase("refund request is approved"),
                        containsStringIgnoringCase("your refund has been submitted"),
                        containsStringIgnoringCase("refund berhasil diajukan"),
                        containsStringIgnoringCase("pengajuan refund disetujui"),
                        containsStringIgnoringCase("dana refund dicairkan"),
                        containsStringIgnoringCase("dana sudah ditransfer")
                ));

        step("Check push notification redirect to url from vertical");
        inboxPage.clickInbox(0);
        Assertion.assertThat("Verify refundId from inbox is correct", refundDetailWeb.getRefundId(), is(refundId));
    }

    @Author(names = "Rizki Maulana")
    @Module(name = "Email Notification")
    @Tribe(name = "Message")
    @Vertical(name = "Platform")
    @Test
    public void verifyEmailEReceiptIssuedOrder() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("verifyEmailEReceiptIssuedOrder");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        TtdLandingPageWeb<?> ttdLandingPageWeb = TtdLandingPageWeb.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);
        TtdBookingFormPageWeb<?> ttdBookingFormPageWeb = TtdBookingFormPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);

        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String product = param.get("product");
        String packageName = param.get("packageName");
        int quantity = Integer.parseInt(param.get("quantity"));
        int ticketIndex = Integer.parseInt(param.get("ticketIndex"));
        String paymentGroup = param.get("paymentGroup");
        String paymentMethod = param.get("paymentMethod");

        step("Go to Home Page");
        homePage.goToHomePage();

        step("Login as " + username + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(username, password);

        step("Go to TTD Landing Page");
        ttdLandingPageWeb.goToTtdLandingPage();

        step("Search Product");
        ttdLandingPageWeb.inputSearchKeyword(product);
        ttdLandingPageWeb.selectRecommendedActivity(product);

        step("Book Package");
        ttdProductDetailPageWeb.findPackage(packageName);
        ttdProductDetailPageWeb.chooseTicket(packageName);
        ttdProductDetailPageWeb.setTicketQty(quantity, ticketIndex);
        ttdProductDetailPageWeb.clickBookButton();
        ttdBookingFormPageWeb.clickSameAsContactToggleBtn();
        ttdBookingFormPageWeb.continueToPaymentPage();

        step("Submit payment method");
        String orderId = paymentPage.getOrderId();
        paymentPage.clickOnSelectPaymentMethod();
        paymentPage.goToPaymentMethodsGroup(paymentGroup);
        paymentPage.selectPaymentMethod(paymentMethod);
        paymentPage.clickOnPaymentButton();
        Assertion.assertThat("Payment successful page is visible", paymentPage.isPaymentSuccessfulVisible(), is(true));

        step("Check issued on order detail page");
        orderListPage.openActiveTicketViaURL(orderId);
        orderDetailPage.skipCoachmark();
        Assertion.assertThat("Order successfully issued", orderDetailPage.verifyIssuedETicket(), is(true));

        step("Check email for the order");
        OutboundNotificationVerifier.verify(username, orderId, NotificationChannel.EMAIL, NotificationTemplateType.ERECEIPT);
    }

    @Author(names = "Rizki Maulana")
    @Module(name = "Email Notification")
    @Tribe(name = "Message")
    @Vertical(name = "Platform")
    @Test
    public void verifyEmailProcessPaymentAfterCheckoutOrderNonLogin() {
        Map<String, String> param = getParam("verifyEmailProcessPaymentAfterCheckoutOrderNonLogin");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        TtdLandingPageWeb<?> ttdLandingPageWeb = TtdLandingPageWeb.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);
        TtdBookingFormPageWeb<?> ttdBookingFormPageWeb = TtdBookingFormPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);

        String salutation = param.get("salutation");
        String fullName = param.get("fullName");
        String phoneNumber = param.get("phoneNumber");
        String email = param.get("email");
        String product = param.get("product");
        String packageName = param.get("packageName");
        int quantity = Integer.parseInt(param.get("quantity"));
        int ticketIndex = Integer.parseInt(param.get("ticketIndex"));
        String paymentGroup = param.get("paymentGroup");
        String paymentMethod = param.get("paymentMethod");

        step("Go to Home Page");
        homePage.goToHomePage();
        homePage.selectCurrency("IDR");

        step("Go to TTD Landing Page");
        ttdLandingPageWeb.goToTtdLandingPage();

        step("Search Product");
        ttdLandingPageWeb.inputSearchKeyword(product);
        ttdLandingPageWeb.selectRecommendedActivity(product);

        step("User Go for the package and  choose tickets.");
        ttdProductDetailPageWeb.findPackage(packageName);
        ttdProductDetailPageWeb.chooseTicket(packageName);
        ttdProductDetailPageWeb.setTicketQty(quantity, ticketIndex);
        ttdProductDetailPageWeb.clickBookButton();
        ttdBookingFormPageWeb.fillContactDetailsNonLogin(salutation, fullName, phoneNumber, email);
        ttdBookingFormPageWeb.continueToPaymentPage();

        step("Submit payment method");
        String orderId = paymentPage.getOrderId();
        Assertion.assertThat("Order Id successfully generated", orderId, notNullValue());
        paymentPage.clickOnSelectPaymentMethod();
        paymentPage.goToPaymentMethodsGroup(paymentGroup);
        paymentPage.selectPaymentMethod(paymentMethod);
        paymentPage.clickOnPaymentButton();

        step("Check email for the order");
        OutboundNotificationVerifier.verify(email, orderId, NotificationChannel.EMAIL, NotificationTemplateType.CHECKOUT);
    }

    @Author(names = "Rizki Maulana")
    @Module(name = "Email Notification")
    @Tribe(name = "Message")
    @Vertical(name = "Platform")
    @Test
    public void verifyEmailEReceiptIssuedOrderNonLogin() {
        Map<String, String> param = getParam("verifyEmailEReceiptIssuedOrderNonLogin");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        TtdLandingPageWeb<?> ttdLandingPageWeb = TtdLandingPageWeb.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);
        TtdBookingFormPageWeb<?> ttdBookingFormPageWeb = TtdBookingFormPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);

        String salutation = param.get("salutation");
        String fullName = param.get("fullName");
        String phoneNumber = param.get("phoneNumber");
        String email = param.get("email");
        String product = param.get("product");
        String packageName = param.get("packageName");
        int quantity = Integer.parseInt(param.get("quantity"));
        int ticketIndex = Integer.parseInt(param.get("ticketIndex"));
        String paymentGroup = param.get("paymentGroup");
        String paymentMethod = param.get("paymentMethod");

        step("Go to Home Page");
        homePage.goToHomePage();
        homePage.selectCurrency("IDR");
        homePage.selectBahasaLanguage();

        step("Go to TTD Landing Page");
        ttdLandingPageWeb.goToTtdLandingPage();

        step("Search Product");
        ttdLandingPageWeb.inputSearchKeyword(product);
        ttdLandingPageWeb.selectRecommendedActivity(product);

        step("Book Package");
        ttdProductDetailPageWeb.findPackage(packageName);
        ttdProductDetailPageWeb.chooseTicket(packageName);
        ttdProductDetailPageWeb.setTicketQty(quantity, ticketIndex);
        ttdProductDetailPageWeb.clickBookButton();
        ttdBookingFormPageWeb.fillContactDetailsNonLogin(salutation, fullName, phoneNumber, email);
        ttdBookingFormPageWeb.continueToPaymentPage();

        step("Submit payment method");
        String orderId = paymentPage.getOrderId();
        paymentPage.clickOnSelectPaymentMethod();
        paymentPage.goToPaymentMethodsGroup(paymentGroup);
        paymentPage.selectPaymentMethod(paymentMethod);
        paymentPage.clickOnPaymentButton();
        Assertion.assertThat("Payment successful page is visible", paymentPage.isPaymentSuccessfulVisible(), is(true));

        step("Check email for the order");
        OutboundNotificationVerifier.verify(email, orderId, NotificationChannel.EMAIL, NotificationTemplateType.ERECEIPT);
    }


    @Author(names = "Manan Kumar")
    @Module(name = "Email Notification")
    @Tribe(name = "Message")
    @Vertical(name = "Platform")
    @Test
    public void testLoginReceiveEmailEvoucherAfterOrderIssued() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testLoginReceiveEmailEvoucherAfterOrderIssued");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        TtdLandingPageWeb<?> ttdLandingPageWeb = TtdLandingPageWeb.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);
        TtdBookingFormPageWeb<?> ttdBookingFormPageWeb = TtdBookingFormPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);

        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String product = param.get("product");
        String packageName = param.get("packageName");
        int quantity = Integer.parseInt(param.get("quantity"));
        int ticketIndex = Integer.parseInt(param.get("ticketIndex"));
        String paymentGroup = param.get("paymentGroup");
        String paymentMethod = param.get("paymentMethod");

        step("User Go to Home Page");
        homePage.goToHomePage();

        step("Login as " + username + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(username, password);

        step("Go to TTD Landing Page");
        ttdLandingPageWeb.goToTtdLandingPage();

        step("User Search for the Product");
        ttdLandingPageWeb.inputSearchKeyword(product);
        ttdLandingPageWeb.selectRecommendedActivity(product);

        step("User Book the Package for e-ticket and e-voucher");
        ttdProductDetailPageWeb.findPackage(packageName);
        ttdProductDetailPageWeb.chooseTicket(packageName);
        ttdProductDetailPageWeb.setTicketQty(quantity, ticketIndex);

        step("User Complete Booking details on booking page.");
        ttdProductDetailPageWeb.clickBookButton();
        ttdBookingFormPageWeb.clickSameAsContactToggleBtn();
        ttdBookingFormPageWeb.continueToPaymentPage();

        step("User submit payment method");
        String orderId = paymentPage.getOrderId();
        Assertion.assertThat("Order Id successfully generated", orderId, notNullValue());

        step("User Choose Payment Method");
        paymentPage.clickOnSelectPaymentMethod();
        paymentPage.goToPaymentMethodsGroup(paymentGroup);
        paymentPage.selectPaymentMethod(paymentMethod);
        paymentPage.clickOnPaymentButton();
        Assertion.assertThat("Payment successful page is visible", paymentPage.isPaymentSuccessfulVisible(), is(true));

        step("User Check issued on order detail page");
        orderListPage.openActiveTicketViaURL(orderId);
        orderDetailPage.skipCoachmark();
        Assertion.assertThat("Order successfully issued", orderDetailPage.validateCompletePaymentButtonExist(), is(true));

        step("Check email for the orderId and e-voucher");
        OutboundNotificationVerifier.verify(username, orderId, NotificationChannel.EMAIL, NotificationTemplateType.ETICKET);
    }

    @Author(names = "Manan Kumar")
    @Module(name = "Email Notification")
    @Tribe(name = "Message")
    @Vertical(name = "Platform")
    @Test
    public void testNonLoginReceiveEmailEvoucherAfterOrderIssued() {
        Map<String, String> param = getParam("testNonLoginReceiveEmailEvoucherAfterOrderIssued");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        TtdLandingPageWeb<?> ttdLandingPageWeb = TtdLandingPageWeb.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);
        TtdBookingFormPageWeb<?> ttdBookingFormPageWeb = TtdBookingFormPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);

        String salutation = param.get("salutation");
        String fullName = param.get("fullName");
        String phoneNumber = param.get("phoneNumber");
        String email = param.get("email");
        String product = param.get("product");
        String packageName = param.get("packageName");
        int quantity = Integer.parseInt(param.get("quantity"));
        int ticketIndex = Integer.parseInt(param.get("ticketIndex"));
        String paymentGroup = param.get("paymentGroup");
        String paymentMethod = param.get("paymentMethod");

        step("User Go to TTD Landing Page without login");
        ttdLandingPageWeb.goToTtdLandingPage();

        step("Select IDR as currency");
        homePage.selectCurrency("IDR");

        step("User Search Product");
        ttdLandingPageWeb.inputSearchKeyword(product);
        ttdLandingPageWeb.selectRecommendedActivity(product);

        step("User Book the Package for e-ticket and e-voucher");
        ttdProductDetailPageWeb.findPackage(packageName);
        ttdProductDetailPageWeb.chooseTicket(packageName);
        ttdProductDetailPageWeb.setTicketQty(quantity, ticketIndex);

        step("User Complete Booking details on booking page.");
        ttdProductDetailPageWeb.clickBookButton();
        ttdBookingFormPageWeb.fillContactDetailsNonLogin(salutation, fullName, phoneNumber, email);
        ttdBookingFormPageWeb.continueToPaymentPage();

        step("User Submit payment method");
        String orderId = paymentPage.getOrderId();
        Assertion.assertThat("Order Id successfully generated", orderId, notNullValue());

        step("User Choose Payment Method");
        paymentPage.clickOnSelectPaymentMethod();
        paymentPage.goToPaymentMethodsGroup(paymentGroup);
        paymentPage.selectPaymentMethod(paymentMethod);
        paymentPage.clickOnPaymentButton();
        Assertion.assertThat("Payment successful page is visible", paymentPage.isPaymentSuccessfulVisible(), is(true));

        step("UserCheck email for the orderId and e-voucher");
        OutboundNotificationVerifier.verify(email, orderId, NotificationChannel.EMAIL, NotificationTemplateType.ETICKET);
    }

    @Author(names = "Ram Pratap Singh")
    @Module(name = "WhatsApp Notification")
    @Tribe(name = "Message")
    @Vertical(name = "Platform")
    @Test
    public void testLoginVerifyWhatsappAfterOrderIssued() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testLoginVerifyWhatsappAfterOrderIssued");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        LoyaltyPointPageWeb<?> loyaltyPage = LoyaltyPointPageWeb.getInstance(session);

        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
        String phone = param.get("phone");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String driverOption = param.get("driverOption");
        String location = param.get("location");
        String pickupTime = param.get("pickupTime").replace("#", ":");
        String fullName = param.get("fullName");
        String title = param.get("title");
        String startDateRange = param.get("startDateRange");
        int rentalDuration = Integer.parseInt(param.get("rentalDuration"));
        String paymentGroup = param.get("paymentGroup");
        String paymentMethod = param.get("paymentMethod");

        step("Go to Home Page");
        homePage.goToHomePage();

        step("Open as logged in user");
        loginPage.loginIfNotWithSameEmail(username, password, otp, retryCounter);

        step("Book car rental");
        Map<String, String> result = loyaltyPage.bookCarRental(
                username,
                phone,
                password,
                driverOption,
                location,
                CoreFunctions.generateRandomDate(startDateRange),
                pickupTime,
                fullName,
                title,
                rentalDuration);

        String orderId = result.get("orderId");
        String orderHash = result.get("orderHash");

        step("Submit payment method");
        paymentPage.goToPaymentPage(orderId, orderHash);
        paymentPage.clickOnSelectPaymentMethod();
        paymentPage.goToPaymentMethodsGroup(paymentGroup);
        paymentPage.selectPaymentMethod(paymentMethod);
        paymentPage.clickOnPaymentButton();
        Assertion.assertThat("Payment successful page is visible", paymentPage.isPaymentSuccessfulVisible(), is(true));

        step("Check Whatsapp Message for the order");
        OutboundNotificationVerifier.verify("62" + phone, orderId, NotificationChannel.WHATSAPP);
    }

    @Author(names = "Ram Pratap Singh")
    @Module(name = "WhatsApp Notification")
    @Tribe(name = "Message")
    @Vertical(name = "Platform")
    @Test
    public void testNonLoginVerifyWhatsappAfterOrderIssued() {
        Map<String, String> param = getParam("testNonLoginVerifyWhatsappAfterOrderIssued");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        LoyaltyPointPageWeb<?> loyaltyPage = LoyaltyPointPageWeb.getInstance(session);

        String username = param.get("username");
        String password = param.get("password");
        String phone = param.get("phone");
        String driverOption = param.get("driverOption");
        String location = param.get("location");
        String pickupTime = param.get("pickupTime").replace("#", ":");
        String fullName = param.get("fullName");
        String title = param.get("title");
        String startDateRange = param.get("startDateRange");
        int rentalDuration = Integer.parseInt(param.get("rentalDuration"));
        String paymentGroup = param.get("paymentGroup");
        String paymentMethod = param.get("paymentMethod");

        step("Go to Home Page");
        homePage.goToHomePage();
        homePage.selectCurrency("IDR");
        homePage.selectBahasaLanguage();

        step("Book car rental");
        Map<String, String> result = loyaltyPage.bookCarRental(
                username,
                phone,
                password,
                driverOption,
                location,
                CoreFunctions.generateRandomDate(startDateRange),
                pickupTime,
                fullName,
                title,
                rentalDuration);

        String orderId = result.get("orderId");
        String orderHash = result.get("orderHash");

        step("Submit payment method");
        paymentPage.goToPaymentPage(orderId, orderHash);
        paymentPage.clickOnSelectPaymentMethod();
        paymentPage.goToPaymentMethodsGroup(paymentGroup);
        paymentPage.selectPaymentMethod(paymentMethod);
        paymentPage.clickOnPaymentButton();
        Assertion.assertThat("Payment successful page is visible", paymentPage.isPaymentSuccessfulVisible(), is(true));

        step("Check Whatsapp Message for the order");
        OutboundNotificationVerifier.verify("62" + phone, orderId, NotificationChannel.WHATSAPP);
    }

    @Author(names = "Ram Pratap Singh")
    @Module(name = "WhatsApp Notification")
    @Tribe(name = "Message")
    @Vertical(name = "Platform")
    @Test
    public void testReceiveWAConfirmedScheduleAfterOrderIssuedLogin() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testReceiveWAConfirmedScheduleAfterOrderIssuedLogin");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        LoyaltyPointPageWeb<?> loyaltyPage = LoyaltyPointPageWeb.getInstance(session);

        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
        String phone = param.get("phone");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String driverOption = param.get("driverOption");
        String location = param.get("location");
        String pickupTime = param.get("pickupTime").replace("#", ":");
        String fullName = param.get("fullName");
        String title = param.get("title");
        String startDateRange = param.get("startDateRange");
        int rentalDuration = Integer.parseInt(param.get("rentalDuration"));
        String paymentGroup = param.get("paymentGroup");
        String paymentMethod = param.get("paymentMethod");

        step("Go to Home Page");
        homePage.goToHomePage();

        step("Open as logged in user");
        loginPage.loginIfNotWithSameEmail(username, password, otp, retryCounter);

        step("Book car rental");
        Map<String, String> result = loyaltyPage.bookCarRental(
                username,
                phone,
                password,
                driverOption,
                location,
                CoreFunctions.generateRandomDate(startDateRange),
                pickupTime,
                fullName,
                title,
                rentalDuration);

        String orderId = result.get("orderId");
        String orderHash = result.get("orderHash");

        step("Submit payment method");
        paymentPage.goToPaymentPage(orderId, orderHash);
        paymentPage.clickOnSelectPaymentMethod();
        paymentPage.goToPaymentMethodsGroup(paymentGroup);
        paymentPage.selectPaymentMethod(paymentMethod);
        paymentPage.clickOnPaymentButton();
        Assertion.assertThat("Payment successful page is visible", paymentPage.isPaymentSuccessfulVisible(), is(true));

        step("Check Whatsapp Message for the order");
        OutboundNotificationVerifier.verify("62" + phone, orderId, NotificationChannel.WHATSAPP);
    }

    @Author(names = "Rizki Maulana")
    @Module(name = "Whatsapp Message")
    @Tribe(name = "Message")
    @Vertical(name = "Platform")
    @Test
    public void verifyWAConfirmedScheduleIssuedOrderLogin() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("verifyWAConfirmedScheduleIssuedOrderLogin");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        LoyaltyPointPageWeb<?> loyaltyPage = LoyaltyPointPageWeb.getInstance(session);

        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
        String phone = param.get("phone");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String driverOption = param.get("driverOption");
        String location = param.get("location");
        String pickupTime = param.get("pickupTime").replace("#", ":");
        String fullName = param.get("fullName");
        String title = param.get("title");
        String startDateRange = param.get("startDateRange");
        int rentalDuration = Integer.parseInt(param.get("rentalDuration"));
        String paymentGroup = param.get("paymentGroup");
        String paymentMethod = param.get("paymentMethod");

        step("Go to Home Page");
        homePage.goToHomePage();

        step("Login as " + username + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(username, password);

        step("Book car rental");
        Map<String, String> result = loyaltyPage.bookCarRental(
                username,
                phone,
                password,
                driverOption,
                location,
                CoreFunctions.generateRandomDate(startDateRange),
                pickupTime,
                fullName,
                title,
                rentalDuration);

        String orderId = result.get("orderId");
        String orderHash = result.get("orderHash");

        step("Submit payment method");
        paymentPage.goToPaymentPage(orderId, orderHash);
        paymentPage.clickOnSelectPaymentMethod();
        paymentPage.goToPaymentMethodsGroup(paymentGroup);
        paymentPage.selectPaymentMethod(paymentMethod);
        paymentPage.clickOnPaymentButton();
        Assertion.assertThat("Payment successful page is visible", paymentPage.isPaymentSuccessfulVisible(), is(true));

        step("Check Whatsapp Message for the order");
        OutboundNotificationVerifier.verify("62" + phone, orderId, NotificationChannel.WHATSAPP);
    }

    @Author(names = "Rizki Maulana")
    @Module(name = "Whatsapp Message")
    @Tribe(name = "Message")
    @Vertical(name = "Platform")
    @Test
    public void verifyWAConfirmedScheduleIssuedOrderNonLogin() {
        Map<String, String> param = getParam("verifyWAConfirmedScheduleIssuedOrderNonLogin");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        LoyaltyPointPageWeb<?> loyaltyPage = LoyaltyPointPageWeb.getInstance(session);

        String username = param.get("username");
        String password = param.get("password");
        String phone = param.get("phone");
        String driverOption = param.get("driverOption");
        String location = param.get("location");
        String pickupTime = param.get("pickupTime").replace("#", ":");
        String fullName = param.get("fullName");
        String title = param.get("title");
        String startDateRange = param.get("startDateRange");
        int rentalDuration = Integer.parseInt(param.get("rentalDuration"));
        String paymentGroup = param.get("paymentGroup");
        String paymentMethod = param.get("paymentMethod");

        step("Go to Home Page");
        homePage.goToHomePage();
        homePage.selectCurrency("IDR");
        homePage.selectBahasaLanguage();

        step("Book car rental");
        Map<String, String> result = loyaltyPage.bookCarRental(
                username,
                phone,
                password,
                driverOption,
                location,
                CoreFunctions.generateRandomDate(startDateRange),
                pickupTime,
                fullName,
                title,
                rentalDuration);

        String orderId = result.get("orderId");
        String orderHash = result.get("orderHash");

        step("Submit payment method");
        paymentPage.goToPaymentPage(orderId, orderHash);
        paymentPage.clickOnSelectPaymentMethod();
        paymentPage.goToPaymentMethodsGroup(paymentGroup);
        paymentPage.selectPaymentMethod(paymentMethod);
        paymentPage.clickOnPaymentButton();
        Assertion.assertThat("Payment successful page is visible", paymentPage.isPaymentSuccessfulVisible(), is(true));

        step("Check Whatsapp Message for the order");
        OutboundNotificationVerifier.verify("62" + phone, orderId, NotificationChannel.WHATSAPP);
    }

    @Author(names = "Manan Kumar")
    @Module(name = "Email Notification")
    @Tribe(name = "Message")
    @Vertical(name = "Platform")
    @Test
    public void testNonLoginE2EVerifyEmailAfterIssuedOrderForIDRAndNonIDR() {
        Map<String, String> param = getParam("testNonLoginE2EVerifyEmailAfterIssuedOrderForIDRAndNonIDR");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        TtdLandingPageWeb<?> ttdLandingPageWeb = TtdLandingPageWeb.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);
        TtdBookingFormPageWeb<?> ttdBookingFormPageWeb = TtdBookingFormPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);

        String salutation = param.get("salutation");
        String fullName = param.get("fullName");
        String phoneNumber = param.get("phoneNumber");
        String email = param.get("email");
        String product = param.get("product");
        String packageName = param.get("packageName");
        int quantity = Integer.parseInt(param.get("quantity"));
        int ticketIndex = Integer.parseInt(param.get("ticketIndex"));
        String paymentGroup = param.get("paymentGroup");
        String paymentMethod = param.get("paymentMethod");

        step("User Go to TTD Landing Page without login");
        ttdLandingPageWeb.goToTtdLandingPage();

        step("Select IDR as currency");
        homePage.selectCurrency("IDR");

        step("User Search Product");
        ttdLandingPageWeb.inputSearchKeyword(product);
        ttdLandingPageWeb.selectRecommendedActivity(product);

        step("User Book the Package for e-voucher");
        ttdProductDetailPageWeb.findPackage(packageName);
        ttdProductDetailPageWeb.chooseTicket(packageName);
        ttdProductDetailPageWeb.setTicketQty(quantity, ticketIndex);

        step("User Book the Package for e-ticket and e-voucher");
        ttdProductDetailPageWeb.clickBookButton();
        ttdBookingFormPageWeb.fillContactDetailsNonLogin(salutation, fullName, phoneNumber, email);

        step("User clicks on Continue to Payment Button");
        ttdBookingFormPageWeb.continueToPaymentPage();

        step("User Submit payment method");
        String orderId = paymentPage.getOrderId();
        Assertion.assertThat("Order Id successfully generated", orderId, notNullValue());

        step("User Choose Payment Method");
        paymentPage.clickOnSelectPaymentMethod();
        paymentPage.goToPaymentMethodsGroup(paymentGroup);
        paymentPage.selectPaymentMethod(paymentMethod);
        paymentPage.clickOnPaymentButton();
        Assertion.assertThat("Payment successful page is visible", paymentPage.isPaymentSuccessfulVisible(), is(true));

        step("UserCheck email for the orderId and e-voucher");
        OutboundNotificationVerifier.verify(email, orderId, NotificationChannel.EMAIL, NotificationTemplateType.ETICKET);
    }

    @Author(names = "Manan Kumar")
    @Module(name = "Push Notification")
    @Tribe(name = "Message")
    @Vertical(name = "Platform")
    @Test
    public void testE2ELoginReceiveEticketIssuedPushNotificationAfterOrderIssued() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testE2ELoginReceiveEticketIssuedPushNotificationAfterOrderIssued");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        TtdLandingPageWeb<?> ttdLandingPageWeb = TtdLandingPageWeb.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);
        TtdBookingFormPageWeb<?> ttdBookingFormPageWeb = TtdBookingFormPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        InboxPage<?> inboxPage = new InboxPage<>(session);
        PointHistoryPage<?> pointHistoryPage = new PointHistoryPage<>(session);


        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String product = param.get("product");
        String packageName = param.get("packageName");
        int quantity = Integer.parseInt(param.get("quantity"));
        int ticketIndex = Integer.parseInt(param.get("ticketIndex"));
        String paymentGroup = param.get("paymentGroup");
        String paymentMethod = param.get("paymentMethod");
        String vertical = param.get("vertical");

        step("User Go to Home Page");
        homePage.goToHomePage();

        step("Open as logged in user");
        loginPage.loginIfNotWithSameEmail(username, password, otp, retryCounter);

        step("Go to TTD Landing Page");
        ttdLandingPageWeb.goToTtdLandingPage();

        step("User Search for the Product");
        ttdLandingPageWeb.inputSearchKeyword(product);
        ttdLandingPageWeb.selectRecommendedActivity(product);

        step("User Book the Package for e-ticket.");
        ttdProductDetailPageWeb.findPackage(packageName);

        step("Uer Selects Package");
        ttdProductDetailPageWeb.chooseTicket(packageName);

        step("User Selects Tiket");
        ttdProductDetailPageWeb.setTicketQty(quantity, ticketIndex);
        ttdProductDetailPageWeb.clickBookButton();

        step("User click on auto fill toggle");
        ttdBookingFormPageWeb.clickSameAsContactToggleBtn();

        step("User clicks on Continue payment button");
        ttdBookingFormPageWeb.continueToPaymentPage();

        Assertion.assertThat("Success redirected to payment page", paymentPage.isPaymentPageLoaded(), is(true));
        String orderId = paymentPage.getOrderId();

        step("Capture Blibli Tiket Points to be earned");
        int earnedPoints = paymentPage.getEarnedPoints();

       step("Open all payment method");
       paymentPage.clickOnSelectPaymentMethod();

       step("Select the payment Method");
       if (Configs.PLATFORM == Platform.DWEB) {
           paymentPage.goToPaymentMethodsGroup(paymentGroup);
           paymentPage.selectPaymentMethod(paymentMethod);
       } else {
           paymentPage.selectPaymentMethodMWeb(paymentMethod, paymentGroup);
       }

       step("Submit the payment method");
       paymentPage.clickOnPaymentButton();
       Assertion.assertThat("Payment successful page is visible", paymentPage.isPaymentSuccessfulVisible(), is(true));

       step("User Check issued on order detail page via URL.");
       orderListPage.openActiveTicketViaURL(orderId);
       orderDetailPage.skipCoachmark();
       Assertion.assertThat("Order successfully issued", orderDetailPage.validateCompletePaymentButtonExist(), is(true));

       step("User open for the push notification in inbox");
       homePage.goToHomePage();
       homePage.openInbox();

       step("Verify earned points in the first inbox notification match the payment page");
       int inboxPoints = inboxPage.getEarnedPointsFromInbox(0);
       Assertion.assertThat("Earned points in inbox notification match payment page", inboxPoints, is(earnedPoints));

       step("Verify the inbox notification time matches the current booking time");
       Assertion.assertThat("Inbox notification time matches current booking time", inboxPage.isInboxTimeCloseToNow(0, 5), is(true));

       step("Open the earned-points notification and land on the pending point-history page");
       inboxPage.clickInbox(0);

       step("Verify the first pending point-history transaction date, points and vertical");
       Assertion.assertThat("Point-history transaction matches (date is today, points and vertical)", pointHistoryPage.verifyFirstTransaction(earnedPoints, vertical), is(true));
    }
}

