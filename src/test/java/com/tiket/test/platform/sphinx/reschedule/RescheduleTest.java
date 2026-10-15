package com.tiket.test.platform.sphinx.reschedule;

import com.tiket.apiPage.CorporateApi;
import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.apiPage.HotelApi;
import com.tiket.apiPage.OrderApi;
import com.tiket.apiPage.Unm;
import com.tiket.model.Session;
import com.tiket.page.accommodation.hotel.bookingformpage.BookingFormPage;
import com.tiket.page.accommodation.hotel.reschedulepage.HotelReschedulePage;
import com.tiket.page.accommodation.hotel.roomlistpage.RoomListPage;
import com.tiket.page.common.webview.WebViewPage;
import com.tiket.page.corporate.login.CorporateLoginPage;
import com.tiket.page.home.HomePage;
import com.tiket.page.login.LoginPage;
import com.tiket.page.order.orderdetail.OrderDetailPage;
import com.tiket.page.order.orderlist.OrderListPage;
import com.tiket.page.payment.PaymentPage;
import com.tiket.page.refund.RefundPage;
import com.tiket.page.reschedule.ReschedulePage;
import com.tiket.pojo.order.MapVerticalWithOrderId;
import com.tiket.pojo.order.MapVerticalWithOrderId.OrderDetailHotel;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.web.page.accommodation.hotels.reschedulePage.HotelReschedulePageWeb;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.tiket.core.CoreFunctions.*;
import static com.tiket.testdata.TestData.getObjectParams;
import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;

public class RescheduleTest extends BaseTest {

    @Author(names = {"Yazid"})
    @Vertical(name = "Reschedule")
    @Tribe(name = "Reschedule")
    @Module(name = "PRO")
    @Test
    public void rescheduleWithCashback() {
        Session<WebDriver> session = sessionThreadLocal.get();

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        HotelReschedulePage<WebDriver> hotelReschedulePage = HotelReschedulePage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        RefundPage<WebDriver> refundPage = RefundPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        HotelReschedulePageWeb<?> hotelReschedulePageWeb = HotelReschedulePageWeb.getInstance(session);

        Map<String, String> map = getParam("rescheduleWithCashback");
        BaseTest.paramsThreadLocal.set(map);

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");
        String startDate = generateRandomDate(map.get("rangeAddDate"));
        String query = map.get("hotelName");
        int adult = Integer.parseInt(map.get("adult"));
        int night = Integer.parseInt(map.get("night"));
        int room = Integer.parseInt(map.get("room"));

        String[] salutationList = { "Mr", "Mrs", "Ms" };
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = salutationList[0];
        String currency = "IDR";

        step("Goto home");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink" + currency);
        homePage.loginWithSpecificAccountViaDeeplinkOnly(username, password);
        String language = homePage.getLanguageApp();

        step("Create a new hotel with issued order");
        hotelApi.createIssuedOrderApi(language,
                username, password, startDate, query, adult, night, room,
                orderFullName, orderNameTitle, guestFullName, guestTitle,
                currency, true);
        String issuedOrderID = String.valueOf(hotelApi.getOrderId());
        Assertion.assertThat("Verify Booking via API is successful", issuedOrderID, is(not(blankOrNullString())));

        step("Find the issued order");
        homePage.openMyOrderPage();
        orderListPage.openIssuedTicketViaDeeplink(issuedOrderID);
        String oldCurrency = extractCurrency(orderDetailPage.getTotalPrice());

        step("Choose reschedule");
        orderDetailPage.clickReschedule(issuedOrderID);

        step("Choose the date for the reschedule");
        hotelReschedulePage.chooseNewDate(false);

        step("Choose a new room type with Cashback");
        webViewPage.switchToWebViewUrl("reschedule");
        String srpCurrency = extractCurrency(hotelReschedulePageWeb.getTheFirstRoomRefundEstimation());
        hotelReschedulePageWeb.chooseRoomWithCashback();

        step("Submit booking form");
        webViewPage.switchToNative();
        String bookingFormCurrency = extractCurrency(bookingFormPage.getTotalRefundPriceBookingForm());
        webViewPage.switchToWebViewUrl("reschedule/checkout");
        hotelReschedulePageWeb.submitRescheduleBookingForm();

        step("Submit the payment method");
        String orderID = paymentPage.checkOrderID();
        paymentPage.selectRefund();
        String paymentCurrency = extractCurrency(paymentPage.getDepositAmount());
        String estimatedRefundPayment = refundPage.getSubmitTotalEstimatedRefund();
        refundPage.submitRefundCashBackFreePayment();

        step("Open order list page");
        paymentPage.seeOrderList();

        step("Check the ongoing refund");
        orderListPage.openOngoingRefundTab();
        Assertion.assertThat("On refund list there will be one refund created for the cashback", orderListPage.getEstimatedTotalPayment(), is(estimatedRefundPayment));

        step("Check the new order detail");
        orderListPage.openIssuedTicketViaDeeplink(orderID);
        Assertion.assertThat("New Order is issued and have old order linked in order detail",orderDetailPage.isRescheduleCardDisplayed(orderID), is(true));

        step("Check price amount on the new order detail");
        String newTotalPaymentCurrency = extractCurrency(orderDetailPage.getTotalPrice());
        orderDetailPage.openPriceDetail();
        Assertion.assertThat("Total and breakdown calculation are matched", orderDetailPage.verifyHotelCashbackPriceDetail(currency), is(true));
        orderDetailPage.isCloseBottomSheetButtonPresent();

        step("Check previous order section");
        orderDetailPage.goToPreviousOrderBanner();
        orderDetailPage.clickPreviousOrder();
        Assertion.assertThat("Old order having section that there is order detail/pax already rescheduled",orderDetailPage.isNewScheduleButtonDisplayed(issuedOrderID), is(true));

        step("All reschedule page currency is same with old order currency");
        Assertion.assertThat("SRP currency should be equal with the old order currency", srpCurrency, is(oldCurrency));
        Assertion.assertThat("booking form currency should be equal with the old order currency", bookingFormCurrency, is(oldCurrency));
        Assertion.assertThat("payment currency should be equal with the old order currency", paymentCurrency, is(oldCurrency));
        Assertion.assertThat("new total payment currency should be equal with the old order currency", newTotalPaymentCurrency, is(oldCurrency));
    }

    @Author(names = {"Yazid"})
    @Vertical(name = "Reschedule")
    @Tribe(name = "Reschedule")
    @Module(name = "PRO")
    @Test
    public void rescheduleWithMorePayment() {
        Session<WebDriver> session = sessionThreadLocal.get();

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        HotelReschedulePage<WebDriver> hotelReschedulePage = HotelReschedulePage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        HotelReschedulePageWeb<?> hotelReschedulePageWeb = HotelReschedulePageWeb.getInstance(session);

        Map<String, String> map = getParam("rescheduleWithMorePayment");
        BaseTest.paramsThreadLocal.set(map);

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");
        String startDate = generateRandomDate(map.get("rangeAddDate"));
        String query = map.get("hotelName");
        int adult = Integer.parseInt(map.get("adult"));
        int night = Integer.parseInt(map.get("night"));
        int room = Integer.parseInt(map.get("room"));

        String[] salutationList = { "Mr", "Mrs", "Ms" };
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = salutationList[0];
        String currency = "IDR";

        step("Goto home");
        homePage.gotoHome();
        String language = homePage.getLanguageApp();

        step("Create a new hotel with issued order");
        hotelApi.createIssuedOrderApi(language,
                username, password, startDate, query, adult, night, room,
                orderFullName, orderNameTitle, guestFullName, guestTitle,
                currency, false);
        String issuedOrderID = String.valueOf(hotelApi.getOrderId());
        Assertion.assertThat("Verify Booking via API is successful", issuedOrderID, is(not(blankOrNullString())));

        step("Login as " + username + " via deeplink" + currency);
        homePage.loginWithSpecificAccountViaDeeplinkOnly(username, password);

        step("Find the issued order");
        homePage.openMyOrderPage();
        orderListPage.openIssuedTicketViaDeeplink(issuedOrderID);
        String oldCurrency = extractCurrency(orderDetailPage.getTotalPrice());

        step("Choose reschedule");
        orderDetailPage.clickReschedule(issuedOrderID);

        step("Choose the date for the reschedule");
        hotelReschedulePage.chooseNewDate(false);

        step("Choose a new room type with additional Price");
        webViewPage.switchToWebViewUrl("reschedule");
        String srpCurrency = extractCurrency(hotelReschedulePageWeb.getPriceRoomWithAdditionalPrice());
        hotelReschedulePageWeb.chooseRoomWithAdditionalPriceNoRoomName();

        step("Submit booking form");
        webViewPage.switchToWebViewUrl("reschedule/checkout");
        String bookingFormCurrency = extractCurrency(hotelReschedulePageWeb.getTotalPriceBookingForm());
        hotelReschedulePageWeb.submitRescheduleBookingForm();

        step("Submit the payment method");
        String orderID = paymentPage.checkOrderID();
        String paymentCurrency = extractCurrency(paymentPage.getTotalPricePayment());
        paymentPage.openPaymentMethod();
        paymentPage.selectPaymentMethod("Sakuku");
        paymentPage.clickPayButton();
        paymentPage.paymentChecker();

        step("Open order list page");
        paymentPage.seeOrderList();

        step("Check the new order detail");
        orderListPage.openIssuedTicketViaDeeplink(orderID);
        Assertion.assertThat("New Order is issued and have old order linked in order detail",orderDetailPage.isRescheduleCardDisplayed(orderID), is(true));

        step("Check price amount on the new order detail");
        String newTotalPaymentCurrency = extractCurrency(orderDetailPage.getTotalPrice());
        orderDetailPage.openPriceDetail();
        Assertion.assertThat("Total and breakdown calculation are matched", orderDetailPage.verifyHotelPriceDetail(currency), is(true));
        orderDetailPage.isCloseBottomSheetButtonPresent();

        step("Check previous order section");
        orderDetailPage.goToPreviousOrderBanner();
        orderDetailPage.clickPreviousOrder();
        Assertion.assertThat("Old order having section that there is order detail/pax already rescheduled",orderDetailPage.isNewScheduleButtonDisplayed(issuedOrderID), is(true));

        step("All reschedule page currency is same with old order currency");
        Assertion.assertThat("SRP currency should be equal with the old order currency", srpCurrency, is(oldCurrency));
        Assertion.assertThat("booking form currency should be equal with the old order currency", bookingFormCurrency, is(oldCurrency));
        Assertion.assertThat("payment currency should be equal with the old order currency", paymentCurrency, is(oldCurrency));
        Assertion.assertThat("new total payment currency should be equal with the old order currency", newTotalPaymentCurrency, is(oldCurrency));
    }

    @Author(names = {"Yazid"})
    @Module(name = "Reschedule")
    @Tribe(name = "Reschedule")
    @Vertical(name = "PRO")
    @Test
    public void verifySuccessPageRescheduleCashback() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifySuccessPageRescheduleCashback");
        BaseTest.paramsThreadLocal.set(param);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        RefundPage<WebDriver> refundPage = RefundPage.getInstance(session);
        HotelReschedulePage<WebDriver> hotelReschedulePage = HotelReschedulePage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);

        String loginType = param.get("loginType");
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
        String guestTitle = salutationList[0];
        String currency = "IDR";

        step("Create new hotel issued order");
        Boolean result = hotelApi.createIssuedOrderApi(
                "en", username, password, startDate, hotelName, adult, night, room,
                orderFullName, orderNameTitle, guestFullName, guestTitle, currency, true);
        Assertion.assertThat("Hotel Booking via API is successful", result, is(true));

        step("Get data from the issued order");
        String issuedOrderID = String.valueOf(hotelApi.getOrderId());

        step("Goto home");
        homePage.gotoHome();

        step("User is on login mode");
        loginPage.loginAsGuest();
        loginPage.loginWith(loginType, username, password);

        step("Find the issued order");
        orderListPage.openIssuedTicketViaDeeplink(issuedOrderID);

        step("Choose reschedule");
        orderDetailPage.clickReschedule(issuedOrderID);

        step("Choose check-in/out date for the reschedule");
        hotelReschedulePage.chooseNewDate(false);

        step("Choose a new room type with Cashback");
        roomListPage.getTheFirstHotelRoomName();
        getIntFromString(roomListPage.getTheFirstRoomRefundEstimation());
        roomListPage.clickBookRoom();

        step("Submit booking form");
        bookingFormPage.submitRescheduleBookingForm();

        step("Submit the payment method");
        String orderId = paymentPage.checkOrderID();
        paymentPage.selectRefund();
        refundPage.submitRefundCashBackFreePayment();

        step("User click see order list page");
        paymentPage.seeOrderList("refunds/"+orderId);
        Assertion.assertThat("user continue to your order list page", orderListPage.isOrderListPage(), is(true));
    }

    @Author(names = {"Yazid"})
    @Module(name = "Reschedule")
    @Tribe(name = "Reschedule")
    @Vertical(name = "PRO")
    @Test
    public void verifySuccessPageRescheduleCashbackB2B() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifySuccessPageRescheduleCashbackB2B");
        BaseTest.paramsThreadLocal.set(param);

        String username = param.get("username");
        String password = param.get("password");
        String startDate = generateRandomDate(param.get("rangeAddDate"));
        String query = param.get("hotelName");
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));
        int room = Integer.parseInt(param.get("room"));

        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = getRandomObject(salutationList);

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        HotelReschedulePage<WebDriver> hotelReschedulePage = HotelReschedulePage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);
        CorporateLoginPage<WebDriver> corporateLoginPage = CorporateLoginPage.getInstance(session);
        RefundPage<WebDriver> refundPage = RefundPage.getInstance(session);

        String issuedOrderID;
        step("Create new hotel issued order");
        issuedOrderID = hotelApi.createIssuedOrderApiB2B(
                username, password, startDate, query, adult, night, room,"IDR",
                orderFullName, orderNameTitle, guestFullName, guestTitle, corporateApi.getEmployeeData(username), true);
        Assertion.assertThat("Hotel Booking via API is successful for B2B account", !issuedOrderID.isEmpty(), is(true));

        step("Goto home");
        homePage.gotoHome();

        step("B2B login");
        loginPage.loginAsGuest();
        corporateLoginPage.corporateLogin(username, password);

        step("Find the issued order");
        orderListPage.openIssuedTicketViaDeeplink(issuedOrderID);

        step("Choose reschedule");
        orderDetailPage.clickReschedule(issuedOrderID);

        step("Choose check-in/out date for the reschedule");
        hotelReschedulePage.chooseNewDate(false);

        step("Choose a new room type with Cashback");
        roomListPage.getTheFirstHotelRoomName();
        getIntFromString(roomListPage.getTheFirstRoomRefundEstimation());
        roomListPage.clickBookRoom();

        step("Submit booking form");
        bookingFormPage.submitRescheduleBookingForm();

        step("Submit the payment method");
        String orderId = paymentPage.checkOrderID();
        paymentPage.selectRefund();
        refundPage.submitRefundCashBackFreePayment();

        step("User click see order list page");
        paymentPage.seeOrderList("refunds/"+orderId);
        Assertion.assertThat("user continue to your order list page", orderListPage.isOrderListPage(), is(true));
    }

    @Author(names = {"Yazid"})
    @Module(name = "Reschedule")
    @Tribe(name = "Reschedule")
    @Vertical(name = "PRO")
    @Test
    public void createHotelRescheduleCashbackNonIDR() {
        Session<WebDriver> session = sessionThreadLocal.get();
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        HotelReschedulePage<WebDriver> hotelReschedulePage = HotelReschedulePage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        RefundPage<WebDriver> refundPage = RefundPage.getInstance(session);

        Map<String, String> param = getParam("createHotelRescheduleCashbackNonIDR");
        BaseTest.paramsThreadLocal.set(param);

        String loginType = param.get("loginType");
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
        String cardNumber = param.get("cardNumber");
        String expiry = param.get("expiry");
        String cvv = param.get("cvv");
        String cardName = param.get("cardName");

        step("Goto home");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Change currency to Non-IDR");
        homePage.changeCurrency(currency,username);

        step("Create a hotel order with waiting payment status");
        String orderId = hotelApi.createWaitingPaymentOrderApi(
                username, password, startDate, hotelName, adult, night, room,
                orderFullName, orderNameTitle, guestFullName, guestTitle,
                currency,"id");
        Assertion.assertThat("Success to create waiting payment order", orderId, is(not(emptyOrNullString())));

        step("Open the order page");
        homePage.openMyOrderPage();

        step("Complete order with the payment method");
        orderListPage.openOrderCompletePayment(orderId);
        paymentPage.checkOrderID();
        paymentPage.fillCCDetailRealIssued(cardNumber, expiry, cvv, cardName);
        paymentPage.checkCcOtp();

        step("Open order list from success paid banner");
        paymentPage.seeOrderList();

        step("Get data from the issued order");
        String issuedRoomName = hotelApi.getHotelRoomName();
        String issuedOrderID = String.valueOf(hotelApi.getOrderId());

        step("Find the issued order");
        orderListPage.findActiveTicket(hotelName, issuedRoomName, issuedOrderID, 2);

        step("Choose reschedule");
        orderDetailPage.clickReschedule();

        step("Choose new date for the reschedule");
        hotelReschedulePage.changeNightDuration("1");

        step("Choose a new room type with Cashback");
        String newRoomName = roomListPage.getTheFirstHotelRoomName();
        Optional<Integer> refundEstimation = getIntFromString(roomListPage.getTheFirstRoomRefundEstimation());
        log("refundEstimation-> " + refundEstimation);
        roomListPage.clickBookRoom();

        step("Submit booking form");
        bookingFormPage.submitRescheduleBookingForm();

        step("Submit the payment method");
        String orderID = paymentPage.checkOrderID();
        paymentPage.selectRefund();
        refundPage.seeOrderList();

        step("Check the new order detail");
        orderListPage.findActiveTicket(hotelName, newRoomName, orderID, 2);
        Assertion.assertThat("Previous order button is displayed", orderDetailPage.isPreviousOrderBannerDisplayed(orderID), is(true));

        step("Check the currency on the new order detail");
        orderDetailPage.goToTotalPayment();
        String totalPayment = orderDetailPage.getTotalPayment();
        Assertion.assertThat("Current currency should be Non-IDR", totalPayment, containsString(currency));

        step("Check the reschedule menu on the new order detail");
        orderDetailPage.clickReschedule();
        Assertion.assertThat("The new order can't be rescheduled", orderDetailPage.isInfoPopupDisplayed(), is(true));
        orderDetailPage.closeInfoPopup();

        step("Check Previous order button");
        orderDetailPage.goToPreviousOrderBanner();
        orderDetailPage.clickPreviousOrder();
        Assertion.assertThat("Your order has been rescheduled banner", orderDetailPage.isOrderRescheduledBannerExist(), is(true));

        step("Check refund estimation amount");
        orderDetailPage.goToRefundDetail(orderID);
        Assertion.assertThat("Estimated total payment on the refund detail should be equal on the room list", refundPage.getTotalEstimatedRefund(), is(refundEstimation));
    }

    @Author(names = {"Yazid"})
    @Module(name = "Reschedule")
    @Tribe(name = "Reschedule")
    @Vertical(name = "PRO")
    @Test
    public void createHotelRescheduleNonCashbackNonIDR() {
        Session<WebDriver> session = sessionThreadLocal.get();
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        HotelReschedulePage<WebDriver> hotelReschedulePage = HotelReschedulePage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);

        Map<String, String> param = getParam("createHotelRescheduleNonCashbackNonIDR");
        BaseTest.paramsThreadLocal.set(param);

        String loginType = param.get("loginType");
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
        String cardNumber = param.get("cardNumber");
        String expiry = param.get("expiry");
        String cvv = param.get("cvv");
        String cardName = param.get("cardName");

        step("Goto home");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Change currency to Non-IDR");
        homePage.changeCurrency(currency,username);

        step("Create a hotel order with waiting payment status");
        String orderId = hotelApi.createWaitingPaymentOrderApi(
                username, password, startDate, hotelName, adult, night, room,
                orderFullName, orderNameTitle, guestFullName, guestTitle,
                currency,"id");
        Assertion.assertThat("Success to create waiting payment order", orderId, is(not(emptyOrNullString())));

        step("Open the order page");
        homePage.openMyOrderPage();

        step("Complete order with the payment method");
        orderListPage.openOrderCompletePayment(orderId);
        paymentPage.checkOrderID();
        paymentPage.fillCCDetailRealIssued(cardNumber, expiry, cvv, cardName);
        paymentPage.checkCcOtp();

        step("Open order list from success paid banner");
        paymentPage.seeOrderList();

        step("Get data from the issued order");
        String issuedRoomName = hotelApi.getHotelRoomName();
        String issuedOrderID = String.valueOf(hotelApi.getOrderId());

        step("Find the issued order");
        orderListPage.findActiveTicket(hotelName, issuedRoomName, issuedOrderID, 2);

        step("Choose reschedule");
        orderDetailPage.clickReschedule();

        step("Choose new date for the reschedule");
        hotelReschedulePage.changeNightDuration("3");

        step("Choose a new room type with additional Price");
        String newRoomName = roomListPage.chooseRoomWithAdditionalPrice();

        step("Submit booking form");
        bookingFormPage.submitRescheduleBookingForm();

        step("Submit the payment method");
        String orderID = paymentPage.checkOrderID();
        paymentPage.fillCCDetailRealIssued(cardNumber, expiry, cvv, cardName);
        paymentPage.checkCcOtp();

        step("Open order list page");
        paymentPage.seeOrderList();

        step("Check the new order detail");
        orderListPage.findActiveTicket(hotelName, newRoomName, orderID, 2);
        Assertion.assertThat("Previous order button is displayed", orderDetailPage.isPreviousOrderBannerDisplayed(orderID), is(true));

        step("Check the currency on the new order detail");
        orderDetailPage.goToTotalPayment();
        String totalPayment = orderDetailPage.getTotalPayment();
        Assertion.assertThat("Current currency should be Non-IDR", totalPayment, containsString(currency));

        step("Check the reschedule menu on the new order detail");
        orderDetailPage.clickReschedule();
        Assertion.assertThat("The new order can't be rescheduled", orderDetailPage.isInfoPopupDisplayed(), is(true));
        orderDetailPage.closeInfoPopup();

        step("Check Previous order button");
        orderDetailPage.goToPreviousOrderBanner();
        orderDetailPage.clickPreviousOrder();
        Assertion.assertThat("Your order has been rescheduled banner", orderDetailPage.isOrderRescheduledBannerExist(), is(true));
    }

    @Author(names = {"Kartik", "Naufal"})
    @Module(name = "Reschedule")
    @Tribe(name = "Reschedule")
    @Vertical(name = "PRO")
    @Test
    public void createHotelRescheduleCashback() {
        Session<WebDriver> session = sessionThreadLocal.get();
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        HotelReschedulePage<WebDriver> hotelReschedulePage = HotelReschedulePage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);

        Map<String, String> param = getParam("createHotelRescheduleCashback");
        BaseTest.paramsThreadLocal.set(param);
        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String startDate = generateRandomDate(param.get("rangeAddDate"));

        String query = param.get("hotelName");
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));
        int room = Integer.parseInt(param.get("room"));

        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = getRandomObject(salutationList);

        step("Create new hotel issued order");
        Boolean result = hotelApi.createIssuedOrderApi(
                username, password, startDate, query, adult, night, room,
                orderFullName, orderNameTitle, guestFullName, guestTitle,"IDR");
        Assertion.assertThat("Hotel Booking via API is successful", result, is(true));

        step("Get data from the issued order");
        String issuedRoomName = hotelApi.getHotelRoomName();
        String issuedOrderID = String.valueOf(hotelApi.getOrderId());
        String issuedORderName = hotelApi.getOrderName();
      

        step("Goto home");
        homePage.gotoHome();

        step("Log out");
        loginPage.loginAsGuest();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Open my order page");
        homePage.openMyOrderPage();

        step("Find the issued order");
        orderListPage.findActiveTicket(issuedORderName, issuedRoomName, issuedOrderID, 2);

        step("Choose reschedule");
        orderDetailPage.clickReschedule();

        step("Choose new date for the reschedule");
        hotelReschedulePage.clickSubmitSearch();

        step("Choose a new room type with additional Price");
        String newRoomName = roomListPage.chooseRoomWithRefundReschedule();

        step("Submit booking form");
        bookingFormPage.submitRescheduleBookingForm();

        step("Submit the payment method");
        String orderID = paymentPage.checkOrderID();
        paymentPage.selectRefund();

        step("Check the new order detail");
        hotelApi.checkOrderStatus(username, password, Long.parseLong(orderID));
        orderListPage.findActiveTicket(issuedORderName, newRoomName, orderID, 2);

        Assertion.assertThat("Previous order button is displayed", orderDetailPage.isPreviousOrderBannerDisplayed(orderID), is(true));

        step("Check Previous order button");
        orderDetailPage.clickPreviousOrder();
        Assertion.assertThat("Your order has been rescheduled banner", orderDetailPage.isOrderRescheduledBannerExist(), is(true));
    }

    @Author(names = {"Kartik", "Naufal"})
    @Module(name = "Reschedule")
    @Tribe(name = "Reschedule")
    @Vertical(name = "PRO")
    @Test
    public void createHotelRescheduleNonCashback() {
        Session<WebDriver> session = sessionThreadLocal.get();

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        HotelReschedulePage<WebDriver> hotelReschedulePage = HotelReschedulePage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);

        Map<String, String> param = getParam("createHotelRescheduleNonCashback");
        BaseTest.paramsThreadLocal.set(param);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String startDate = generateRandomDate(param.get("rangeAddDate"));
        String paymentType = param.get("paymentMethod");

        String query = param.get("hotelName");
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));
        int room = Integer.parseInt(param.get("room"));

        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = getRandomObject(salutationList);

        step("Create new hotel issued order");
        Boolean result = hotelApi.createIssuedOrderApi(
                username, password, startDate, query, adult, night, room,
                orderFullName, orderNameTitle, guestFullName, guestTitle,"IDR");
        Assertion.assertThat("Hotel Booking via API is successful", result, is(true));

        step("Get data from the issued order");
        String issuedRoomName = hotelApi.getHotelRoomName();
        String issuedOrderID = String.valueOf(hotelApi.getOrderId());
        String issuedOrderName = hotelApi.getOrderName();

        step("Goto home");
        homePage.gotoHome();

        step("Log out");
        loginPage.loginAsGuest();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Open my order page");
        homePage.openMyOrderPage();

        step("Find the issued order");
        orderListPage.findActiveTicket(issuedOrderName, issuedRoomName, issuedOrderID, 2);

        step("Choose reschedule");
        orderDetailPage.clickReschedule();

        step("Choose new date for the reschedule");
        hotelReschedulePage.chooseNewDate(true);

        step("Choose a new room type with additional Price");
        String newRoomName = roomListPage.chooseRoomWithAdditionalPrice();

        step("Submit booking form");
        bookingFormPage.submitRescheduleBookingForm();

        step("Submit the payment method");
        String orderID = paymentPage.checkOrderID();

        step("Open payment method");
        paymentPage.openPaymentMethod();

        step("Select payment method");
        paymentPage.selectPaymentMethod(paymentType);

        step("Submit payment");
        paymentPage.clickPayButton();

        step("Open order list page");
        paymentPage.seeOrderList();

        step("Check the new order detail");
        orderListPage.findActiveTicket(issuedOrderName, newRoomName, orderID, 2);

        step("Check Previous order button");
        orderDetailPage.clickPreviousOrder();
        Assertion.assertThat("This order has been rescheduled banner", orderDetailPage.isOrderRescheduledBannerExist(), is(true));
    }

    @Author(names = {"Dendi"})
    @Module(name = "Reschedule")
    @Tribe(name = "Reschedule")
    @Vertical(name = "PRO")
    @Test(dataProvider = "testRescheduleNonCashbackMultiOrderDataProvider")
    public void testRescheduleNonCashbackMultiOrder(Map<String, String> param) throws InterruptedException{
        Session<WebDriver> session = sessionThreadLocal.get();
        HotelApi<?> hotelApi = new HotelApi<>(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        HotelReschedulePage<WebDriver> hotelReschedulePage = HotelReschedulePage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        ReschedulePage<WebDriver> reschedulePage = ReschedulePage.getInstance(session);
        Unm<WebDriver> unm = new Unm<>(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        HotelReschedulePageWeb<?> hotelReschedulePageWeb = HotelReschedulePageWeb.getInstance(session);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String hotelName = param.get("hotelName");
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));
        int room = Integer.parseInt(param.get("room"));
        String startDate = generateRandomDate(param.get("rangeAddDate"));
        int qtyEachRoom = Integer.parseInt(param.get("qtyEachRoom"));
        String currency = "IDR";
        String cardNumber = param.get("cardNumber");
        String expiry = param.get("expiry");
        String cvv = param.get("cvv");
        String cardName = param.get("cardName");
        int rescheduleDuration = night + Integer.parseInt(param.get("rescheduleDuration"));

        step("Get api access token");
        String apiAccessToken = unm.getAccessToken(username, password,"B2C");

        step("check order present in order");
        MapVerticalWithOrderId activeOrderData = new OrderApi<>(session).getAvailaibleOrder(apiAccessToken);
        int limit = 15;
        activeOrderData = orderListPage.filterOrderByPaymentCurrency(activeOrderData, currency,limit);
        activeOrderData = reschedulePage.filterRescheduleAbleOrder(activeOrderData,username);
        List<MapVerticalWithOrderId.TitleAndOrderId>  activeMultiOrder = orderListPage.filterHotelMultiOrderOnly(activeOrderData);
        String bookedOrderID = "";
        int roomIndex = -1;
        if(!(activeMultiOrder==null) && !(activeMultiOrder.size()==0)){
            bookedOrderID = activeMultiOrder.get(0).orderId;
            MapVerticalWithOrderId.OrderDetailHotel orderDetailHotel = activeMultiOrder.get(0).getDetailedVerticalData(OrderDetailHotel.class);
            roomIndex = orderDetailHotel.getRescheduleAbleRoom(reschedulePage);
            hotelName = orderDetailHotel.hotelInternalOrderDetail.data.orderDetails.get(0).orderName;
            night = orderDetailHotel.hotelInternalOrderDetail.data.orderDetails.get(0).vertical.bookingNight;
            rescheduleDuration = night + Integer.parseInt(param.get("rescheduleDuration"));
        }

        if(roomIndex==-1 || bookedOrderID.equals("") || (activeMultiOrder==null || activeMultiOrder.size()==0)){
            step("Create hotel booking multi-room using CC");
            String bookingApiCc = hotelApi.createOrderMultiRoomApi(username, password,
                    hotelName, adult, night, room, startDate, qtyEachRoom, currency, cardNumber, expiry, cvv, cardName);
            Assertion.assertThat("Success to book multi-room by API using CC", bookingApiCc, is(equalToIgnoringCase(hotelName)));
            bookedOrderID = String.valueOf(hotelApi.getOrderId());
            roomIndex = room;
        }

        step("Go to home");
        homePage.gotoHome();

        step("Input credential on the login page");
        loginPage.loginAsGuest();
        loginPage.loginWith(loginType, username, password);

        step("user open the order detail page: " + bookedOrderID);
        orderListPage.openIssuedTicketViaDeeplink(bookedOrderID);

        step("user click reschedule button");
        orderDetailPage.clickReschedule(bookedOrderID);

        step("select "+roomIndex+" room to be rescheduled");
        orderDetailPage.webviewChooseRoomToRescheduleByIndex(roomIndex,bookedOrderID);

        step("Confirm room reschedule");
        orderDetailPage.webviewConfirmRescheduleMo();

        step("Confirm Refund back button work");
        webViewPage.switchToWebViewUrl("reschedule");
        hotelReschedulePageWeb.clickBackButton();
        webViewPage.switchToNative();
        Assertion.assertThat("Assert choose room btm sheet appear", orderDetailPage.isWebviewChooseRoomToReschedBtmSheetDislayed(), is(true));

        step("select third room to be rescheduled");
        orderDetailPage.webviewChooseRoomToRescheduleByIndex(roomIndex,bookedOrderID);

        step("Confirm room reschedule");
        orderDetailPage.webviewConfirmRescheduleMo();

        step("Choose new date for the reschedule");
        hotelReschedulePage.changeNightDuration(String.valueOf(rescheduleDuration));

        step("Assert that price are in idr");
        Assertion.assertThat("Assert room price are in idr", roomListPage.assertThatPriceCurrency(currency), is(true));

        step("Choose a new room type without additional Price");
        roomListPage.chooseRoomWithAdditionalPrice();

        step("Assert that total price is in idr or free");
        String totalPriceBookingForm =  bookingFormPage.getTotalPriceBookingForm();
        Assertion.assertThat("Assert total price are in idr",totalPriceBookingForm, containsString("IDR"));

        step("Verify total price match with price component");
        bookingFormPage.continueToAddOnPage();
        Assertion.assertThat("Verify total price match with price component", bookingFormPage.isPriceDetailRescheduleBookingFormCorrect(totalPriceBookingForm), is(true));

        step("Submit booking form");
        bookingFormPage.submitRescheduleBookingForm();

        step("Check payment page is in IDR");
        String newOrderID = paymentPage.checkOrderID();
        String currenyPaymentPage = paymentPage.getTotalPriceCurrency();
        Assertion.assertThat("Assert total price are in idr or free",currenyPaymentPage, anyOf(containsString("IDR")));

        step("verify price detail in payment page");
        String totalPricePaymentPage = paymentPage.getTotalPrice();
        paymentPage.openPriceDetail();
        String totalPricePriceDetail = paymentPage.getTotalPricePriceDetail();
        Assertion.assertThat("Verify total price from payment page match with total price price detail", convertPrice(totalPricePaymentPage), equalTo(convertPrice(totalPricePriceDetail)));
        Assertion.assertThat("Verify total price match with price component", paymentPage.isPriceBreakdownCorrect(), is(true));
        paymentPage.closePriceDetail();

        step("Open payment method");
        paymentPage.openPaymentMethod();

        step("Select Credit Card as Payment Method");
        paymentPage.selectPaymentMethod("Credit Card");

        step("Input and submit CC information");
        paymentPage.fillCCDetailRealIssued(cardNumber, expiry, cvv, cardName, newOrderID);

        step("Submit the payment method");
        paymentPage.clickPayButtonCC();
        orderListPage.waitForOrderToBeInIssuedState(newOrderID,30);
        Assertion.assertThat("Payment Successful and Thank You page is shown", paymentPage.isThankYouPageShown(newOrderID), is(true));

        step("Open order list");
        paymentPage.seeOrderList();

        step("Check the new order detail");
        orderListPage.openIssuedTicketViaDeeplink(newOrderID);

        step("Check the previous order detail");
        Assertion.assertThat("Previous order banner should be present",orderDetailPage.isPreviousOrderBannerDisplayed(newOrderID), is(true));

        step("Check Previous order button");
        orderDetailPage.goToPreviousOrderBanner();
        orderDetailPage.clickPreviousOrder();
        
        Assertion.assertThat("This order has been rescheduled banner present", orderDetailPage.isOrderRescheduledBannerExist(), is(true));
        Assertion.assertThat("Order ID should be showing as expected", orderDetailPage.getOrderID(), is(bookedOrderID));

        step("user click reschedule button");
        orderDetailPage.clickReschedule(bookedOrderID);

        step("select "+0+" room to be rescheduled");
        orderDetailPage.webviewChooseRoomToRescheduleByIndex(0,bookedOrderID);

        step("Verify reschedule not allowed present");
        hotelReschedulePage.verifyRescheduleNotAllowedPresent();

    }

    @DataProvider(name="testRescheduleNonCashbackMultiOrderDataProvider")
    public Object[][] testRescheduleNonCashbackMultiOrderDataProvider() {
        return getObjectParams("testRescheduleNonCashbackMultiOrder");
    }


    @Author(names = {"Dendi"})
    @Module(name = "Reschedule")
    @Tribe(name = "Reschedule")
    @Vertical(name = "PRO")
    @Test(dataProvider = "testRescheduleWithCashbackMultiOrderDataProvider")
    public void testRescheduleWithCashbackMultiOrder(Map<String, String> param) throws InterruptedException{
        Session<WebDriver> session = sessionThreadLocal.get();
        HotelApi<?> hotelApi = new HotelApi<>(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        HotelReschedulePage<WebDriver> hotelReschedulePage = HotelReschedulePage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        ReschedulePage<WebDriver> reschedulePage = ReschedulePage.getInstance(session);
        RefundPage<WebDriver> refundPage = RefundPage.getInstance(session);
        Unm<WebDriver> unm = new Unm<>(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        HotelReschedulePageWeb<?> hotelReschedulePageWeb = HotelReschedulePageWeb.getInstance(session);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String hotelName = param.get("hotelName");
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));
        int room = Integer.parseInt(param.get("room"));
        String startDate = generateRandomDate(param.get("rangeAddDate"));
        int qtyEachRoom = Integer.parseInt(param.get("qtyEachRoom"));
        String currency = "IDR";
        String cardNumber = param.get("cardNumber");
        String expiry = param.get("expiry");
        String cvv = param.get("cvv");
        String cardName = param.get("cardName");
        int rescheduleDuration = Integer.parseInt(param.get("rescheduleDuration"));

        step("Get api access token");
        String apiAccessToken = unm.getAccessToken(username, password,"B2C");

        step("check order present in order");
        MapVerticalWithOrderId activeOrderData = new OrderApi<>(session).getAvailaibleOrder(apiAccessToken);
        int limit = 20;
        activeOrderData = orderListPage.filterOrderByPaymentCurrency(activeOrderData, currency,limit);
        activeOrderData = reschedulePage.filterRescheduleAbleOrder(activeOrderData,username);
        List<MapVerticalWithOrderId.TitleAndOrderId>  activeMultiOrder = orderListPage.filterHotelMultiOrderOnly(activeOrderData);
        String bookedOrderID = "";
        int roomIndex = 0;
        if(!(activeMultiOrder==null) && !(activeMultiOrder.size()==0)){
            bookedOrderID = activeMultiOrder.get(0).orderId;
            MapVerticalWithOrderId.OrderDetailHotel orderDetailHotel = activeMultiOrder.get(0).getDetailedVerticalData(OrderDetailHotel.class);
            roomIndex = orderDetailHotel.getRescheduleAbleRoom(reschedulePage);
            log("Use room inde : " + roomIndex);
            hotelName = orderDetailHotel.hotelInternalOrderDetail.data.orderDetails.get(0).orderName;
            night = orderDetailHotel.hotelInternalOrderDetail.data.orderDetails.get(0).vertical.bookingNight;
            rescheduleDuration = night - Integer.parseInt(param.get("rescheduleDuration")) < 0 ? 1 : (night - Integer.parseInt(param.get("rescheduleDuration")));
        }

        if(roomIndex==-1 || bookedOrderID.equals("") || (activeMultiOrder==null || activeMultiOrder.size()==0)){
            step("Create hotel booking multi-room using CC");
            String bookingApiCc = hotelApi.createOrderMultiRoomApi(username, password,
                    hotelName, adult, night, room, startDate, qtyEachRoom, currency, cardNumber, expiry, cvv, cardName);
            Assertion.assertThat("Success to book multi-room by API using CC", bookingApiCc, is(equalToIgnoringCase(hotelName)));
            bookedOrderID = String.valueOf(hotelApi.getOrderId());
            roomIndex = room;
        }

        step("Go to home");
        homePage.gotoHome();

        step("Input credential on the login page");
        loginPage.loginAsGuest();
        loginPage.loginWith(loginType, username, password);

        step("user open the order detail page: " + bookedOrderID);
        orderListPage.openIssuedTicketViaDeeplink(bookedOrderID);

        step("user click reschedule button");
        orderDetailPage.clickReschedule(bookedOrderID);

        step("select "+roomIndex+" room to be rescheduled");
        orderDetailPage.webviewChooseRoomToRescheduleByIndex(roomIndex,bookedOrderID);

        step("Confirm room reschedule");
        orderDetailPage.webviewConfirmRescheduleMo();

        step("Confirm Refund back button work");
        webViewPage.switchToWebViewUrl("reschedule");
        hotelReschedulePageWeb.clickBackButton();
        webViewPage.switchToNative();
        Assertion.assertThat("Assert choose room btm sheet appear", orderDetailPage.isWebviewChooseRoomToReschedBtmSheetDislayed(), is(true));

        step("select third room to be rescheduled");
        orderDetailPage.webviewChooseRoomToRescheduleByIndex(roomIndex,bookedOrderID);

        step("Confirm room reschedule");
        orderDetailPage.webviewConfirmRescheduleMo();

        step("Choose new date for the reschedule");
        hotelReschedulePage.changeNightDuration(String.valueOf(rescheduleDuration));

        step("Assert that price are in idr");
        Assertion.assertThat("Assert room price are in idr", roomListPage.assertThatPriceCurrency(currency), is(true));

        step("Choose a new room type without additional Price");
        roomListPage.chooseRoomWithFreePayment();

        step("Assert that total price is in idr or free");
        String totalPriceBookingForm =  bookingFormPage.getTotalPriceBookingForm();
        Assertion.assertThat("Assert total price are in idr",totalPriceBookingForm, anyOf(containsString("FREE"),containsString("GRATIS")));

        step("Verify total price match with price component");
        bookingFormPage.continueToAddOnPage();
        Assertion.assertThat("Verify total price match with price component", bookingFormPage.isPriceDetailRescheduleBookingFormCorrect(totalPriceBookingForm), is(true));

        step("Submit booking form");
        bookingFormPage.submitRescheduleBookingForm();

        step("Check payment page is in IDR");
        String newOrderID = paymentPage.checkOrderID();
        String currenyPaymentPage = paymentPage.getTotalPriceCurrency();
        Assertion.assertThat("Assert total price are in idr or free",currenyPaymentPage, anyOf(containsString("GRATIS"),containsString("FREE")));

        step("verify price detail in payment page");
        paymentPage.openPriceDetail();
        Assertion.assertThat("Verify total price match with price component", paymentPage.isPriceBreakdownCorrect(), is(true));
        paymentPage.closePriceDetail();

        step("Submit the payment method");
        paymentPage.selectRefund();
        refundPage.submitRefundReschedule();

        step("Check the new order detail");
        orderListPage.openIssuedTicketViaDeeplink(newOrderID);

        step("Check the previous order detail");
        Assertion.assertThat("Previous order banner should be present",orderDetailPage.isPreviousOrderBannerDisplayed(newOrderID), is(true));

        step("Check Previous order button");
        orderDetailPage.goToPreviousOrderBanner();
        orderDetailPage.clickPreviousOrder();
        
        Assertion.assertThat("This order has been rescheduled banner present", orderDetailPage.isOrderRescheduledBannerExist(), is(true));
        Assertion.assertThat("Order ID should be showing as expected", orderDetailPage.getOrderID(), is(bookedOrderID));

        boolean isManageOrderSectionPresent = orderDetailPage.verifyManageOrderSection(bookedOrderID);
        if(isManageOrderSectionPresent){ //EXPECTATION : if all order has already reschedule, manage order section will dissapear
            step("user click reschedule button");
            orderDetailPage.clickReschedule(bookedOrderID);

            step("select "+roomIndex+" room to be rescheduled");
            orderDetailPage.webviewChooseRoomToRescheduleByIndex(roomIndex,bookedOrderID);

            step("confirm room reschedule");
            orderDetailPage.webviewConfirmRescheduleMo(bookedOrderID);

            step("Verify reschedule not allowed present");
            Assertion.assertThat("Verify reschedule not allowed", hotelReschedulePage.verifyRescheduleNotAllowedPresent(), is(true));
        }
  
    }

    @Author(names = {"kartik"})
    @Module(name = "Reschedule")
    @Tribe(name = "PRO")
    @Vertical(name = "PRO")
    @Test
    public void verifyEstimationPageFunctionalityRescheduleCashback() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyEstimationPageFunctionalityRescheduleCashback");
        BaseTest.paramsThreadLocal.set(param);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        HotelReschedulePage<WebDriver> hotelReschedulePage = HotelReschedulePage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        RefundPage<WebDriver> refundPage = RefundPage.getInstance(session);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String startDate = generateRandomDate(param.get("rangeAddDate"));
        String hotelName = param.get("hotelName");
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));
        int room = Integer.parseInt(param.get("room"));
        String currency = param.get("currency");
        String cardNumber = param.get("cardNumber");
        String expiry = param.get("expiry");
        String cvv = param.get("cvv");
        String cardName = param.get("cardName");

        String[] salutationList = { "Mr", "Mrs", "Ms" };
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = salutationList[0];


        step("create hotel booking via api");
        String OrderID = hotelApi.createIssuedOrderApi(
                username, password, startDate, hotelName, adult, night, room,
                orderFullName, orderNameTitle, guestFullName, guestTitle,
                currency,cardNumber,expiry,cvv,cardName);
        Assertion.assertThat("Hotel Booking via API is successful", OrderID, is(notNullValue()));

        step("Goto home");
        homePage.gotoHome();

        step("Log out");
        loginPage.loginAsGuest();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Open my order page");
        homePage.openMyOrderPage();

        step("Find the issued order");
        orderListPage.openActiveTicketViaDeeplink(OrderID);

        step("Choose reschedule");
        orderDetailPage.clickReschedule();

        step("Choose new date for the reschedule");
        hotelReschedulePage.changeNightDuration("1");

        step("Choose a new room type with Cashback");
        roomListPage.chooseRoomWithFreePayment();

        step("Submit booking form");
        bookingFormPage.submitRescheduleBookingForm();

        step("Submit the payment method");
        paymentPage.clickPayButton();

        step("Verify Estimation page functionality for reschedule cashback");
        refundPage.checkRefundReviewPage();
    }

    @DataProvider(name="testRescheduleWithCashbackMultiOrderDataProvider")
    public Object[][] testRescheduleWithCashbackMultiOrderDataProvider() {
        return getObjectParams("testRescheduleWithCashbackMultiOrder");
    }

    @Author(names = {"kartik"})
    @Module(name = "Reschedule")
    @Tribe(name = "PRO")
    @Vertical(name = "PRO")
    @Test
    public void testCompleteFlowRescheduleFree() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testCompleteFlowRescheduleFree");
        BaseTest.paramsThreadLocal.set(param);

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        HotelReschedulePage<WebDriver> hotelReschedulePage = HotelReschedulePage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        RefundPage<WebDriver> refundPage = RefundPage.getInstance(session);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String startDate = generateRandomDate(param.get("rangeAddDate"));
        String hotelName = param.get("hotelName");
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));
        int room = Integer.parseInt(param.get("room"));
        String currency = param.get("currency");
        String cardNumber = param.get("cardNumber");
        String expiry = param.get("expiry");
        String cvv = param.get("cvv");
        String cardName = param.get("cardName");

        String[] salutationList = { "Mr", "Mrs", "Ms" };
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = salutationList[0];


        step("create hotel booking via api");
        String OrderID = hotelApi.createIssuedOrderApi(
                username, password, startDate, hotelName, adult, night, room,
                orderFullName, orderNameTitle, guestFullName, guestTitle,
                currency,cardNumber,expiry,cvv,cardName);
        Assertion.assertThat("Hotel Booking via API is successful", OrderID, is(notNullValue()));

        step("Goto home");
        homePage.gotoHome();

        step("Log out");
        loginPage.loginAsGuest();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Open my order page");
        homePage.openMyOrderPage();

        step("Find the issued order");
        orderListPage.openActiveTicketViaDeeplink(OrderID);

        step("Choose reschedule");
        orderDetailPage.clickReschedule();

        step("Choose new date for the reschedule");
        hotelReschedulePage.clickSubmitSearch();

        step("Choose a new room type with free payment");
        roomListPage.chooseRoomWithFreePayment();

        step("Submit booking form");
        bookingFormPage.submitRescheduleBookingForm();

        step("Check payment page is in IDR");
        String newOrderID = paymentPage.checkOrderID();
        String currenyPaymentPage = paymentPage.getTotalPriceCurrency();
        Assertion.assertThat("Assert total price are in " +currency+" or free",currenyPaymentPage, anyOf(containsString("GRATIS"),containsString("FREE")));

        step("verify Total and breakdown calculation are matched");
        paymentPage.openPriceDetail();
        Assertion.assertThat("Verify total price match with price component", paymentPage.isPriceBreakdownCorrect(), is(true));
        paymentPage.closePriceDetail();

        step("Submit the payment method");
        paymentPage.clickPayButton();
        refundPage.submitRefundCashBackFreePayment();

        step("Open the new order detail");
        orderListPage.openIssuedTicketViaDeeplink(newOrderID);

        step("Check the previous order detail");
        Assertion.assertThat("Previous order banner should be present",orderDetailPage.isPreviousOrderBannerDisplayed(newOrderID), is(true));

        step("Check the previous order detail");
        orderDetailPage.goToPreviousOrderBanner();
        orderDetailPage.clickPreviousOrder();
        Assertion.assertThat("Previous order banner should be present",orderDetailPage.isNewScheduleButtonDisplayed(OrderID), is(true));
        Assertion.assertThat("Hotel Name should be showing as expected", orderDetailPage.isHotelNameDisplayed(hotelName, OrderID), is(true));
        Assertion.assertThat("Refund info is not present", orderDetailPage.isRefundInfoDisplayed(OrderID), is(false));

        step("Back to new order detail");
        orderDetailPage.clickNewScheduleButton(OrderID);

        step("Price on order detail should be 0");
        Assertion.assertThat("Verify no refund created", convertPrice(orderDetailPage.getTotalPaymentHotel()), is(0.0));
    }
}
