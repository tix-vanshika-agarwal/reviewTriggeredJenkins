package com.tiket.test.platform.sphinx.refund;

import com.tiket.apiPage.CorporateApi;
import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.apiPage.CarTransferApi;
import com.tiket.apiPage.HotelApi;
import com.tiket.apiPage.OrderApi;
import com.tiket.apiPage.TTDApi;
import com.tiket.apiPage.Unm;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Environment;
import com.tiket.model.Platform;
import com.tiket.model.Session;
import com.tiket.page.accommodation.hotel.bookingformpage.BookingFormPage;
import com.tiket.page.accommodation.hotel.detailpage.DetailPage;
import com.tiket.page.accommodation.hotel.landingpage.LandingPage;
import com.tiket.page.accommodation.hotel.reschedulepage.HotelReschedulePage;
import com.tiket.page.accommodation.hotel.roomlistpage.RoomListPage;
import com.tiket.page.account.AccountPage;
import com.tiket.page.carrental.CarRentalPage;
import com.tiket.page.cartransfer.CarTransferPage;
import com.tiket.page.common.webview.WebViewPage;
import com.tiket.page.corporate.login.CorporateLoginPage;
import com.tiket.page.flight.FlightPage;
import com.tiket.page.home.HomePage;
import com.tiket.page.login.LoginPage;
import com.tiket.page.order.orderdetail.OrderDetailPage;
import com.tiket.page.order.orderhistory.OrderHistoryPage;
import com.tiket.page.order.orderlist.OrderListPage;
import com.tiket.page.order.orderlist.OrderListPage.OrderStatus;
import com.tiket.page.order.orderlist.OrderListPage.PaymentType;
import com.tiket.page.payment.PaymentPage;
import com.tiket.page.refund.RefundPage;
import com.tiket.page.setting.SettingPage;
import com.tiket.page.ttd.TTDPage;
import com.tiket.page.utilities.message.inbox.InboxPage;
import com.tiket.pojo.flight.FlightTicketDetail;
import com.tiket.pojo.order.GeneralVerticalInternalOrderDetail;
import com.tiket.pojo.order.HotelInternalOrderDetail;
import com.tiket.pojo.order.MapVerticalWithOrderId;
import com.tiket.pojo.order.MapVerticalWithOrderId.OrderDetailHotel;
import com.tiket.pojo.order.MapVerticalWithOrderId.TitleAndOrderId;
import com.tiket.pojo.unm.LoginDataUnmResponse;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.testbase.Configs;
import com.tiket.web.dashboard.page.refund.RefundCreation;
import com.tiket.web.page.accommodation.hotels.reschedulePage.HotelReschedulePageWeb;
import lombok.SneakyThrows;
import org.hamcrest.CoreMatchers;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import static com.tiket.core.CoreFunctions.*;
import static com.tiket.testdata.TestData.getObjectParams;
import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;

public class RefundTest extends BaseTest {

    @Author(names = {"Yazid"})
    @Module(name = "Refund")
    @Tribe(name = "Refund")
    @Vertical(name = "PRO")
    @Test
    public void verifyRefundPageFromInbox() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyRefundPageFromInbox");
        BaseTest.paramsThreadLocal.set(param);

        String dashboardUsername = param.get("dashboardUsername");
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

        HotelApi<?> hotelApi = new HotelApi<>(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        RefundCreation<WebDriver> refundCreation = new RefundCreation<>(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        InboxPage<WebDriver> inboxPage = InboxPage.getInstance(session);
        RefundPage<WebDriver> refundPage = RefundPage.getInstance(session);

        step("Create new issued order via API");
        hotelApi.createIssuedOrderApi(
                "en", username, password, startDate, hotelName, adult, night, room,
                orderFullName, orderNameTitle, guestFullName, guestTitle, currency, false);

        step("Get data from the issued order via API");
        String orderId = String.valueOf(hotelApi.getOrderId());
        JSONObject internalOrderData = orderListPage.getInternalOrderData(orderId);
        GeneralVerticalInternalOrderDetail internalOrderDetail = orderListPage.getVerticalInternalOrderDtata(internalOrderData);
        String amountFromVendor = String.valueOf(internalOrderDetail.data.payment.get(0).paymentAmount);
        long orderDetailId = Long.parseLong(internalOrderDetail.data.orderDetails.get(0).orderDetailId);
        long orderIdFromApi = Long.parseLong(internalOrderDetail.data.orderId);

        step("Submit refund from the dashboard API");
        refundCreation.createFormMiddlewareApi(
                amountFromVendor,
                false,
                null,
                orderDetailId,
                orderIdFromApi,
                "customer_request",
                "Dummy Name",
                "1414141414",
                "bank-mandiri-idn",
                "Automation App",
                dashboardUsername);

        step("Goto home");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink" + currency);
        homePage.loginWithSpecificAccountViaDeeplinkOnly(username, password);

        step("Open inbox page");
        homePage.clickInboxIcon();

        step("Open refund notification");
        inboxPage.openRefundNotification();

        step("Open the total refund amount");
        refundPage.openEstimatedRefundDetail();
        Assertion.assertThat("Total breakdown pop up shown", refundPage.calculatePriceBreakdown(), is(notNullValue()));

        step("Close the refund pop-up");
        refundPage.closeTheRefundPaymentDetails();
        Assertion.assertThat("Total breakdown pop up can be dismissed with x button", refundPage.isPriceBreakdownAppear(), is(false));

        step("when click back");
        refundPage.clickBackButton();
        Assertion.assertThat("refund details webview closed, user directed to inbox page", inboxPage.verifyWeAreOnInboxPage(), is(true));
    }

    @Author(names = {"Yazid"})
    @Module(name = "Refund")
    @Tribe(name = "Refund")
    @Vertical(name = "PRO")
    @Test
    public void verifyRefundPageFromRefundList() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyRefundPageFromRefundList");
        BaseTest.paramsThreadLocal.set(param);

        String dashboardUsername = param.get("dashboardUsername");
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

        HotelApi<?> hotelApi = new HotelApi<>(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        RefundCreation<WebDriver> refundCreation = new RefundCreation<>(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        RefundPage<WebDriver> refundPage = RefundPage.getInstance(session);

        step("Create new issued order via API");
        hotelApi.createIssuedOrderApi(
                "en", username, password, startDate, hotelName, adult, night, room,
                orderFullName, orderNameTitle, guestFullName, guestTitle, currency, false);

        step("Get data from the issued order via API");
        String orderId = String.valueOf(hotelApi.getOrderId());
        JSONObject internalOrderData = orderListPage.getInternalOrderData(orderId);
        GeneralVerticalInternalOrderDetail internalOrderDetail = orderListPage.getVerticalInternalOrderDtata(internalOrderData);
        String amountFromVendor = String.valueOf(internalOrderDetail.data.payment.get(0).paymentAmount);
        long orderDetailId = Long.parseLong(internalOrderDetail.data.orderDetails.get(0).orderDetailId);
        long orderIdFromApi = Long.parseLong(internalOrderDetail.data.orderId);

        step("Submit refund from the dashboard API");
        String refundId = refundCreation.createFormMiddlewareApi(
                amountFromVendor,
                false,
                null,
                orderDetailId,
                orderIdFromApi,
                "customer_request",
                "Dummy Name",
                "1414141414",
                "bank-mandiri-idn",
                "Automation App",
                dashboardUsername);

        step("Goto home");
        homePage.gotoHome();

        step("User is on login mode");
        loginPage.loginAsGuest();
        loginPage.loginWith(loginType, username, password);

        step("Open Your Orders page");
        homePage.openMyOrderPage();

        step("Open Ongoing Refund tab");
        orderListPage.openOngoingRefundTab();

        step("Open the refund card by refund ID: " + refundId);
        orderListPage.findRefundCard(refundId);
        orderListPage.openRefundCard(refundId);

        step("Open the total refund amount");
        refundPage.openEstimatedRefundDetail();
        Assertion.assertThat("Total breakdown pop up shown", refundPage.calculatePriceBreakdown(), is(notNullValue()));

        step("Close the refund pop-up");
        refundPage.closeTheRefundPaymentDetails();
        Assertion.assertThat("Total breakdown pop up can be dismissed with x button", refundPage.isPriceBreakdownAppear(), is(false));

        step("when click back");
        refundPage.clickBackButton();
        if (Configs.PLATFORM == Platform.ANDROID) Assertion.assertThat("refund details webview closed, user directed to active order list", orderListPage.ongoingRefundTabStatus(), is(true));
        if (Configs.PLATFORM == Platform.IOS) Assertion.assertThat("refund details webview closed, user directed to active order list", orderListPage.isOrderListPage(), is(true));
    }

    @Author(names = {"Yazid"})
    @Module(name = "Refund")
    @Tribe(name = "Refund")
    @Vertical(name = "PRO")
    @Test
    public void verifyHotelRefundIdrMultiOrder() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyHotelRefundIdrMultiOrder");
        BaseTest.paramsThreadLocal.set(param);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        RefundPage<WebDriver> refundPage = RefundPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        Unm<WebDriver> unm = new Unm<>(session);

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

        step("Get api access token");
        LoginDataUnmResponse.Data authData = unm.unmLoginApi(username, password);
        String accessToken = authData.accessToken;
        String accountId = String.valueOf(authData.accountId);

        step("check order present in order");
        MapVerticalWithOrderId activeOrderData = new OrderApi<>(session).getAvailaibleOrder(accessToken);
        int limit = 15;
        activeOrderData = orderListPage.filterOrderByPaymentCurrency(activeOrderData, currency,limit);
        activeOrderData = refundPage.getRefundAbleOrder(activeOrderData,username,accountId,limit);
        List<MapVerticalWithOrderId.TitleAndOrderId>  activeMultiOrder = orderListPage.filterHotelMultiOrderOnly(activeOrderData);
        String bookedOrderID = "";
        int roomIndex = -1;
        if(!(activeMultiOrder==null) && !(activeMultiOrder.size()==0)){
            bookedOrderID = activeMultiOrder.get(0).orderId;
            MapVerticalWithOrderId.OrderDetailHotel orderDetailHotel = activeMultiOrder.get(0).getDetailedVerticalData(OrderDetailHotel.class);
            roomIndex = orderDetailHotel.getRefundAbleRoom(refundPage);
            hotelName = orderDetailHotel.hotelInternalOrderDetail.data.orderDetails.get(0).orderName;
            night = orderDetailHotel.hotelInternalOrderDetail.data.orderDetails.get(0).vertical.bookingNight;
        }

        if(roomIndex==-1 || bookedOrderID.equals("") || (activeMultiOrder==null || activeMultiOrder.size()==0)){
            //TODO check waiting payment
            //if empty -> create using below method
            step("user have refundable Multi-Order Hotel with " + room + " types of room paid using " + currency + " currency");
            String result = hotelApi.createOrderMultiRoomApi(username, password,
                    hotelName, adult, night, room, startDate, qtyEachRoom, currency,cardNumber,expiry,cvv,cardName);
            Assertion.assertThat("Success to book multi-room by API using cc", result, is(not(blankOrNullString())));
            bookedOrderID = String.valueOf(hotelApi.getOrderId());
            roomIndex = 0;
        }

        step("Go to home");
        homePage.gotoHome();

        step("Input credential on the login page");
        loginPage.loginAsGuest();
        loginPage.loginWith(loginType, username, password);

        step("user open your order");
        homePage.openMyOrderPage();

        step("user open the order detail page: " + bookedOrderID);
        orderListPage.openActiveTicketViaDeeplink(bookedOrderID);
        orderListPage.removePopUp();

        step("user see room details for room type 1");
        orderDetailPage.seeRoomDetails(roomIndex);

        step("Get the room info data");
        String roomName = orderDetailPage.getRoomName(bookedOrderID);
        int roomAmount = orderDetailPage.getRoomAmount();
        int guestAmount = orderDetailPage.getRommGuestAmount();
        String checkInDate = orderDetailPage.getCheckInDate();
        int totalNights = Integer.parseInt(orderDetailPage.getTotalNight(bookedOrderID));
        String checkOutDate = orderDetailPage.getCheckOutDate();

        step("user click Request Refund button");
        orderDetailPage.openTabRefundRescheduleDetailRoom();
        orderDetailPage.openRefundRequestDetailRoom();

        step("order review in refund page match with user order");
        Assertion.assertThat("hotel name is should be equal as testdata", refundPage.getHotelName(), is(hotelName));
        Assertion.assertThat("check-in date should be equal with the room info on order page", normalizeDateNumber(refundPage.getCheckInDate()), is(checkInDate));
        Assertion.assertThat("check-out date should be equal with the room info on order page", normalizeDateNumber(refundPage.getCheckOutDate()), is(checkOutDate));
        Assertion.assertThat("total nights should be equal with the room info on order page", refundPage.getTotalNight(), is(totalNights));
        Assertion.assertThat("room name and total room should be equal with the room info on order page", refundPage.getRoomName(), is(roomAmount + "x " + roomName));
        Assertion.assertThat("total guests should be equal with the room info on order page", refundPage.getTotalGuest(), is(guestAmount));

        step("user pick the reason");
        refundPage.chooseRefundReasonWebView();

        step("1st page of price matrix shown and the currency is " + currency);
        Assertion.assertThat("Estimated date prices currency is " + currency, refundPage.getEstimationDatePrices(), everyItem(containsString(currency)));

        step("Get the first estimated refund price");
        String totalEstimatedRefund = refundPage.getEstimatedRefund();

        step("user continue to estimation page");
        refundPage.clickProcessRefundBtn();

        step("2nd page of price matrix shown and the currency is " + currency);
        Assertion.assertThat("Total refund currency is " + currency, refundPage.getTotalRefund(), containsString(currency));

        step("estimation page shown match with the previous page -> 1st & 2nd page");
        Assertion.assertThat("Total refund on 2nd page should be equal with the 1st page", refundPage.getTotalRefund(), is(totalEstimatedRefund));

        step("Submit the refund request");
        refundPage.submitRefund();

        step("refund successfully submitted");
        boolean isRefundSubmitSuccessfully = refundPage.checkRefundSubmission();
        Assertion.assertThat("Refund should be successfully submitted", isRefundSubmitSuccessfully, is(true));

        step("Go to the refund detail");
        refundPage.viewRefundDetailPage();

        step("3rd page of price matrix shown and the currency is " + currency);
        String estimatedRefundDetails = refundPage.getTotalRefund();
        Assertion.assertThat("Total refund currency is " + currency, refundPage.getTotalRefund(), containsString(currency));

        step("Open refund price details");
        refundPage.openEstimatedRefundDetail();

        step("3rd page (price breakdown) of price matrix shown and the currency is " + currency);
        Assertion.assertThat("Prices in refund total detail breakdown is " +currency, refundPage.getRefundDetailPrices(), everyItem(containsString(currency)));

        step("refund Total and breakdown calculation are matched");
        Assertion.assertThat("Refund estimation on the 3rd page should be equal with the 1st page", totalEstimatedRefund, is(estimatedRefundDetails));
        Assertion.assertThat("Refund details calculation should be equal the total payment details", refundPage.calculatePriceBreakdown(), is(convertPrice(totalEstimatedRefund)));
        refundPage.closeTheRefundPaymentDetails();

        step("room status change to processing Refund or refunded");
        refundPage.clickBackButton();
        orderListPage.openActiveTicketViaDeeplink(bookedOrderID);
        Assertion.assertThat("Status on the order list should be e-Ticket Issued", orderDetailPage.getRoomStatus(roomName), either(is("Processing refund")).or(is("Refund sedang diproses")));
    }

    @Author(names = {"Yazid"})
    @Module(name = "Refund")
    @Tribe(name = "Refund")
    @Vertical(name = "PRO")
    @Test
    public void verifyRefundHotelNonIDR() throws ExecutionException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyRefundHotelNonIDR");
        BaseTest.paramsThreadLocal.set(param);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        RefundPage<WebDriver> refundPage = RefundPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        Unm<WebDriver> unm = new Unm<>(session);

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
        String issuedRoomName = "";
        double issuedTotalPayment = 0;

        step("Get api access token");
        LoginDataUnmResponse.Data authData = unm.unmLoginApi(username, password);
        String accessToken = authData.accessToken;
        String accountId = String.valueOf(authData.accountId);

        step("check order present in order");
        MapVerticalWithOrderId.TitleAndOrderId activeOrderData = orderListPage.findFirstOrderFilterByVertical(accessToken,username,accountId,"HOTEL",currency,true,false,OrderStatus.ETICKET_ISSUED);
        log("Active order data : " + activeOrderData);
        String bookedOrderID;

        if(activeOrderData == null || activeOrderData.orderId==null){
            step("Create new hotel issued order");
            String issuedOrderID = hotelApi.createIssuedOrderApi(
                    username, password, startDate, hotelName, adult, night, room,
                    orderFullName, orderNameTitle, guestFullName, guestTitle,
                    currency,cardNumber,expiry,cvv,cardName);
            Assertion.assertThat("Hotel Booking via API is successful", issuedOrderID, is(notNullValue()));
            bookedOrderID = String.valueOf(hotelApi.getOrderId());
            issuedTotalPayment = Double.parseDouble(String.valueOf(hotelApi.getGrandTotal()));
            issuedRoomName = hotelApi.getHotelRoomName();
        }else{
            bookedOrderID = activeOrderData.orderId;
            HotelInternalOrderDetail hotelInternalOrderDetail = orderDetailPage.getHotelInternalOrderData(bookedOrderID);
            hotelName = hotelInternalOrderDetail.data.orderDetails.get(0).orderName;
            issuedTotalPayment = hotelInternalOrderDetail.data.payments.get(0).getPaymentAmounAsDouble();
            issuedRoomName = hotelInternalOrderDetail.data.orderDetails.get(0).orderNameDetail;
        }

        step("Goto home");
        homePage.gotoHome();

        step("Log out");
        loginPage.loginAsGuest();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Open my order page");
        homePage.openMyOrderPage();

        step("Find the issued order");
        orderListPage.openActiveTicketViaDeeplink(bookedOrderID);

        step("Open refund menu");
        orderDetailPage.clickRefundButton();

        step("Choose refund reason");
        refundPage.chooseRefundReason();

        step("Click process refund button");
        refundPage.clickProcessRefundBtn();

        step("Get the estimated refund on the refund process page");
        Double estimatedRefundProcess = refundPage.getTotalEstimatedRefundWebView();
        Assertion.assertThat("Refund estimation should be equal issued total payment", estimatedRefundProcess, is(issuedTotalPayment));

        step("Submit refund");
        refundPage.submitRefund();

        step("check refund submit successfully");
        boolean isRefundSubmitSuccessfully = refundPage.checkRefundSubmission();
        Assertion.assertThat("Successfully submit the refund", isRefundSubmitSuccessfully, is(true));

        step("Go to the refund detail");
        refundPage.viewRefundDetailPage();

        step("Get the Order ID on the refund details");
        String orderIdRefundDetails = refundPage.getOrderIdRefundDetails();
        Assertion.assertThat("Order ID on the refund detail should be equal with issued order ID", orderIdRefundDetails, is(bookedOrderID));

        step("Get the Refund ID on the refund details");
        String refundIdRefundDetails = refundPage.getRefundIdRefundDetails();

        step("Get the refund currency on the refund detail page");
        String actualCurrency = refundPage.getTheRefundCurrency();
        Assertion.assertThat("The currency should be equal " + currency, actualCurrency, is(currency));

        step("Get the estimated refund on the refund detail page");
        Double estimatedRefundDetails = refundPage.getTotalEstimatedRefundWebView();
        Assertion.assertThat("Refund estimation on the refund detail should be equal with the process page", estimatedRefundDetails, is(estimatedRefundProcess));

        step("Open estimated refund details");
        refundPage.openEstimatedRefundDetail();
        String totalEstimatedRefund = refundPage.getTotalEstimatedRefundDetails();
        Assertion.assertThat("Refund estimation on the payment details should be equal on the refund detail page", getIntFromString(totalEstimatedRefund), is(estimatedRefundDetails));
        Assertion.assertThat("Refund details calculation should be equal the total payment details", refundPage.calculatePriceBreakdown(), is(convertPrice(totalEstimatedRefund)));
        refundPage.closeTheRefundPaymentDetails();

        step("Go back to the Your Orders page");
        refundPage.clickBackButton();
        orderListPage.openOngoingRefundTab();
        Assertion.assertThat("Ongoing Refund tab should be active", orderListPage.ongoingRefundTabStatus(), is(true));

        step("Find the refund card by refund ID: " + refundIdRefundDetails);
        Assertion.assertThat("Refund ID should be present on the ongoing refund list", orderListPage.findRefundCard(refundIdRefundDetails), is(true));

        step("Open the active tickets tab");
        orderListPage.openActiveTicketTab();

        step("Find the active order that on refund process");
        String orderStatus = orderListPage.findActiveTicket(hotelName, issuedRoomName, bookedOrderID, 2);
        Assertion.assertThat("Order status should be \"Memproses refund\" or \"Processing refund\"", orderStatus.toLowerCase(), either(is("memproses refund")).or(is("processing refund")));
        Assertion.assertThat("Refund banner info is visible on the order details", orderDetailPage.refundBannerInfo(), is(true));

        step("Open refund menu");
        orderDetailPage.clickRefundButton();
        Assertion.assertThat("Refund is not available anymore on the order ID: " + bookedOrderID, orderDetailPage.refundNotAvailable(), is(true));
    }

    @Author(names = {"Yazid"})
    @Module(name = "Refund")
    @Tribe(name = "Refund")
    @Vertical(name = "PRO")
    @Test
    public void verifyRefundHotelIDR() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyRefundHotelIDR");
        BaseTest.paramsThreadLocal.set(param);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        RefundPage<WebDriver> refundPage = RefundPage.getInstance(session);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String startDate = generateRandomDate(param.get("rangeAddDate"));
        String hotelName = param.get("hotelName");
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));
        int room = Integer.parseInt(param.get("room"));

        String[] salutationList = { "Mr", "Mrs", "Ms" };
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = salutationList[0];

        step("Create new hotel issued order");
        Boolean result = hotelApi.createIssuedOrderApi(
                username, password, startDate, hotelName, adult, night, room,
                orderFullName, orderNameTitle, guestFullName, guestTitle,"IDR");
        Assertion.assertThat("Hotel Booking via API is successful", result, is(true));

        step("Get data from the issued order");
        String issuedOrderID = String.valueOf(hotelApi.getOrderId());
        String issuedRoomName = hotelApi.getHotelRoomName();
        String issuedTotalPayment = String.valueOf(hotelApi.getGrandTotal());

        step("Goto home");
        homePage.gotoHome();

        step("Log out");
        loginPage.loginAsGuest();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Open my order page");
        homePage.openMyOrderPage();

        step("Find the issued order");
        orderListPage.openActiveTicketViaDeeplink(issuedOrderID);

        step("Open refund menu");
        orderDetailPage.clickRefundButton();

        step("Choose refund reason");
        refundPage.chooseRefundReason();

        step("Click process refund button");
        refundPage.clickProcessRefundBtn();

        step("Get the estimated refund on the refund process page");
        Double estimatedRefundProcess = refundPage.getTotalEstimatedRefundWebView();
        Assertion.assertThat("Refund estimation should be equal issued total payment", estimatedRefundProcess, is(getIntFromString(issuedTotalPayment)));

        step("Submit refund");
        refundPage.submitRefund();

        step("check refund submit successfully");
        boolean isRefundSubmitSuccessfully = refundPage.checkRefundSubmission();
        Assertion.assertThat("Successfully submit the refund", isRefundSubmitSuccessfully, is(true));

        step("Go to the refund detail");
        refundPage.viewRefundDetailPage();

        step("Get the Order ID on the refund details");
        String orderIdRefundDetails = refundPage.getOrderIdRefundDetails();
        Assertion.assertThat("Order ID on the refund detail should be equal with issued order ID", orderIdRefundDetails, is(issuedOrderID));

        step("Get the Refund ID on the refund details");
        String refundIdRefundDetails = refundPage.getRefundIdRefundDetails();

        step("Get the refund currency on the refund detail page");
        String actualCurrency = refundPage.getTheRefundCurrency();
        Assertion.assertThat("The currency should be equal IDR", actualCurrency, is("IDR"));

        step("Get the estimated refund on the refund detail page");
        Double estimatedRefundDetails = refundPage.getTotalEstimatedRefundWebView();
        Assertion.assertThat("Refund estimation on the refund detail should be equal with the process page", estimatedRefundDetails, is(estimatedRefundProcess));

        step("Open estimated refund details");
        refundPage.openEstimatedRefundDetail();
        String totalEstimatedRefund = refundPage.getTotalEstimatedRefundDetails();
        Assertion.assertThat("Refund estimation on the payment details should be equal on the refund detail page", getIntFromString(totalEstimatedRefund), is(estimatedRefundDetails));
        Assertion.assertThat("Refund details calculation should be equal the total payment details", refundPage.calculatePriceBreakdown(), is(convertPrice(totalEstimatedRefund)));
        refundPage.closeTheRefundPaymentDetails();

        step("Go back to the Your Orders page");
        refundPage.clickBackButton();
        orderListPage.openOngoingRefundTab();
        Assertion.assertThat("Ongoing Refund tab should be active", orderListPage.ongoingRefundTabStatus(), is(true));

        step("Find the refund card by refund ID: " + refundIdRefundDetails);
        Assertion.assertThat("Refund ID should be present on the ongoing refund list", orderListPage.findRefundCard(refundIdRefundDetails), is(true));

        step("Open the active tickets tab");
        orderListPage.openActiveTicketTab();

        step("Find the active order that on refund process");
        String orderStatus = orderListPage.findActiveTicket(hotelName, issuedRoomName, issuedOrderID, 2);
        Assertion.assertThat("Order status should be \"Memproses refund\" or \"Processing refund\"", orderStatus.toLowerCase(), either(is("memproses refund")).or(is("processing refund")));
        Assertion.assertThat("Refund banner info is visible on the order details", orderDetailPage.refundBannerInfo(), is(true));

        step("Open refund menu");
        orderDetailPage.clickRefundButton();
        Assertion.assertThat("Refund is not available anymore on the order ID: " + issuedOrderID, orderDetailPage.refundNotAvailable(), is(true));
    }

    @Author(names = {"Yazid"})
    @Module(name = "Refund")
    @Tribe(name = "Refund")
    @Vertical(name = "PRO")
    @Test(groups = "specialAccount")
    public void refundMainGiftVoucherPromo() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("refundMainGiftVoucherPromo");
        BaseTest.paramsThreadLocal.set(param);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        RefundPage<WebDriver> refundPage = RefundPage.getInstance(session);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String startDate = generateRandomDate(param.get("rangeAddDate"));
        String hotelName = param.get("hotelName");
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));
        int room = Integer.parseInt(param.get("room"));
        String discountCode = param.get("discountCode");

        String[] salutationList = { "Mr", "Mrs", "Ms" };
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = salutationList[0];

        step("Create new hotel issued order with discountCode: " + discountCode);
        Boolean result = hotelApi.createOrderDiscountApi(
                username, password, startDate, hotelName, adult, night, room,
                orderFullName, orderNameTitle, guestFullName, guestTitle, discountCode,"IDR");
        Assertion.assertThat("Hotel Booking via API is successful", result, is(true));

        step("Get data from the issued order");
        String issuedOrderID = String.valueOf(hotelApi.getOrderId());
        String issuedTotalDiscount = String.valueOf(hotelApi.getTotalDiscount());
        String issuedTotalPayment = String.valueOf(hotelApi.getGrandTotal());

        step("Goto home");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Open my order page");
        homePage.openMyOrderPage();

        step("Find the issued order");
        orderListPage.openActiveTicketViaDeeplink(issuedOrderID);

        step("Open refund tab");
        orderDetailPage.clickRefundButton();

        step("Choose refund reason");
        refundPage.chooseRefundReason();

        step("Click process refund button");
        refundPage.clickProcessRefundBtn();

        step("Get the refund method on the refund process page");
        String refundMethodProcess = refundPage.getRefundMethod();
        Assertion.assertThat("Default refund method should be correct", refundMethodProcess, is("blipay"));
        /*because paid order using ATM, then the refund method should be blipay or bank transfer*/

        step("Get the estimated refund on the refund process page");
        Double estimatedRefundProcess = refundPage.getTotalEstimatedRefundWebView();
        Assertion.assertThat("Refund estimation should be equal issued total payment", estimatedRefundProcess, is(getIntFromString(issuedTotalPayment)));

        step("Submit refund");
        refundPage.submitRefund();

        step("check refund submit successfully");
        boolean isRefundSubmitSuccessfully = refundPage.checkRefundSubmission();
        Assertion.assertThat("is refund submit successfully", isRefundSubmitSuccessfully, is(true));

        step("Go to the refund detail");
        refundPage.viewRefundDetailPage();

        step("Get the Order ID on the refund details");
        String orderIdRefundDetails = refundPage.getOrderIdRefundDetails();
        Assertion.assertThat("Order ID on the refund detail should be equal with issued order ID", orderIdRefundDetails, is(issuedOrderID));

        step("Get the estimated refund on the refund detail page");
        Double estimatedRefundDetails = refundPage.getTotalEstimatedRefundWebView();
        Assertion.assertThat("Refund estimation on the refund detail should be equal with the process page", estimatedRefundDetails, is(estimatedRefundProcess));

        step("Get the refund method on the refund detail page");
        String refundMethodDetail = refundPage.getRefundMethod();
        Assertion.assertThat("Refund method on the refund detail should be equal with the process page", refundMethodDetail, is(refundMethodProcess));

        step("Open estimated refund details");
        refundPage.openEstimatedRefundDetail();
        Double promoAmount = convertPrice(refundPage.getPromoAmountDetail());
        String actualPromoAmount = String.valueOf(promoAmount);
        Assertion.assertThat("Promo amount on the refund detail should be equal with issued discount", actualPromoAmount, is(issuedTotalDiscount));
    }

    @Author(names = {"Zia"})
    @Module(name = "refundReason")
    @Tribe(name = "Refund")
    @Vertical(name = "PRO")
    @Test(dataProvider="testChooseRefundReasonLoginDataProvider")
    public void testChooseRefundReasonLogin(Map<String, String> param) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        RefundPage<WebDriver> refundPage = RefundPage.getInstance(session);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String rangeDate = param.get("rangeAddDate");
        String startDate = generateRandomDate(rangeDate);
        String query = param.get("hotelName");
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));
        int room = Integer.parseInt(param.get("room"));

        String[] salutationList = { "Mr", "Mrs", "Ms" };
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = getRandomObject(salutationList);

        step("Create new hotel issued order");
        Boolean result = hotelApi.createIssuedOrderApi(
                username, password, startDate, query, adult, night, room,
                orderFullName, orderNameTitle, guestFullName, guestTitle,"IDR");
        Assertion.assertThat("Hotel Booking via API is successful", result, is(true));

        String issuedOrderID = String.valueOf(hotelApi.getOrderId());
        String issuedRoomName = hotelApi.getHotelRoomName();

        step("Goto home");
        homePage.gotoHome();

        step("Log out");
        loginPage.loginAsGuest();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Open my order page");
        homePage.openMyOrderPage();

        step("Find the issued order");
        orderListPage.openActiveTicketViaDeeplink(issuedOrderID);

        step("Open refund tab");
        orderDetailPage.clickRefundButton();

        step("Choose refund reason");
        String refundReason = refundPage.chooseRefundReason();

        step("Assert selected reason shown");
        Assertion.assertThat("Correct refund reason show",refundPage.isCorrectReasonShowing(refundReason),is(true));

        step("Assert refund estimation shown");
        Assertion.assertThat("Correct refund estimation show",refundPage.isEstimationShowing(),is(true));
    }

    @DataProvider(name="testChooseRefundReasonLoginDataProvider")
    public Object[][] testChooseRefundReasonLoginDataProvider() {
        return getObjectParams("testChooseRefundReasonLogin");
    }

    @Author(names = {"Yazid"})
    @Module(name = "Refund")
    @Tribe(name = "Refund")
    @Vertical(name = "PRO")
    @Test(groups = "login")
    public void pastRefundListDetailsIDR() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("pastRefundListDetailsIDR");
        BaseTest.paramsThreadLocal.set(param);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderHistoryPage<WebDriver> orderHistoryPage = OrderHistoryPage.getInstance(session);
        RefundPage<WebDriver> refundPage = RefundPage.getInstance(session);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String currency = "IDR";

        step("Open app");
        homePage.gotoHome();

        step("Input credential on the login page");
        loginPage.loginWith(loginType, username, password);
        String language = homePage.getLanguageApp();

        step("Set currency to IDR");
        homePage.changeCurrency("IDR",username);

        step("Open Your Orders page");
        homePage.openMyOrderPage();

        step("Open order history page");
        orderListPage.openOrderHistory();

        step("Open past refund list");
        orderHistoryPage.verifyPastRefundList("login");

        step("Get the refund list item data");
        String refundStatusList = orderHistoryPage.getRefundStatus(currency);
        String refundIdList = orderHistoryPage.getRefundId(currency);
        String refundProductNameList = orderHistoryPage.getRefundProductName(currency);
        String receiveDateList = orderHistoryPage.getRefundReceiveDate(currency);
        String totalRefundList = orderHistoryPage.getTotalRefund(currency);

        step("Select item on the refund list");
        orderHistoryPage.selectRefundItem(currency);

        step("Compare data from the selected item to the refund detail page");
        Assertion.assertThat("Refund name item should be equal with the list", refundPage.getRefundProductName(), is(refundProductNameList));
        Assertion.assertThat("Refund ID should be equal with the list", refundPage.getRefundIdRefundDetails(), is(refundIdList));
        Assertion.assertThat("Refund status should be equal with the list", refundPage.getRefundStatus(), is(refundStatusList));
        Assertion.assertThat("Refund receive date estimation should be equal with the list", refundPage.getEstimatedReceiveDate(language), is(receiveDateList));
        Assertion.assertThat("Refund method should be not empty", refundPage.getRefundMethod(), is(not("")));
        Double estimatedRefundDetails = refundPage.getTotalEstimatedRefundWebView();
        Assertion.assertThat("Refund estimation on the refund detail should be equal with the list", estimatedRefundDetails, is(CoreFunctions.convertPriceSafely(totalRefundList)));

        step("Open estimated refund details");
        refundPage.openEstimatedRefundDetail();
        String totalEstimatedRefund = refundPage.getTotalEstimatedRefundDetails();
        Assertion.assertThat("Refund estimation on the breakdown should be equal on the refund detail page", getIntFromString(totalEstimatedRefund), is(estimatedRefundDetails));
        Assertion.assertThat("Refund details calculation should be equal the total breakdown", refundPage.calculatePriceBreakdown(), is(convertPrice(totalEstimatedRefund)));
    }

    @Author(names = {"Yazid"})
    @Module(name = "Refund")
    @Tribe(name = "Refund")
    @Vertical(name = "PRO")
    @Test(groups = "specialAccount")
    public void pastRefundListDetailsNonIDR() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("pastRefundListDetailsNonIDR");
        BaseTest.paramsThreadLocal.set(param);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderHistoryPage<WebDriver> orderHistoryPage = OrderHistoryPage.getInstance(session);
        RefundPage<WebDriver> refundPage = RefundPage.getInstance(session);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String currency = param.get("currency");

        step("Open app");
        homePage.gotoHome();

        step("Input credential on the login page");
        loginPage.loginWith(loginType, username, password);
        String language = homePage.getLanguageApp();

        step("Open Your Orders page");
        homePage.openMyOrderPage();

        step("Open order history page");
        orderListPage.openOrderHistory();

        step("Open past refund list");
        orderHistoryPage.verifyPastRefundList("login");

        step("Get the refund list item data");
        String refundStatusList = orderHistoryPage.getRefundStatus(currency);
        String refundIdList = orderHistoryPage.getRefundId(currency);
        String refundProductNameList = orderHistoryPage.getRefundProductName(currency);
        String receiveDateList = orderHistoryPage.getRefundReceiveDate(currency);
        String totalRefundList = orderHistoryPage.getTotalRefund(currency);

        step("Select item on the refund list");
        orderHistoryPage.selectRefundItem(currency);

        step("Compare data from the selected item to the refund detail page");
        Assertion.assertThat("Refund name item should be equal with the list", refundPage.getRefundProductName(), is(refundProductNameList));
        Assertion.assertThat("Refund ID should be equal with the list", refundPage.getRefundIdRefundDetails(), is(refundIdList));
        Assertion.assertThat("Refund status should be equal with the list", refundPage.getRefundStatus(), is(refundStatusList));
        Assertion.assertThat("Refund receive date estimation should be equal with the list", refundPage.getEstimatedReceiveDate(language), is(receiveDateList));
        Assertion.assertThat("Refund method should be not empty", refundPage.getRefundMethod(), is(not("")));
        Double estimatedRefundDetails = refundPage.getTotalEstimatedRefundWebView();
        Assertion.assertThat("Refund estimation on the refund detail should be equal with the list", estimatedRefundDetails, is(getIntFromString(totalRefundList)));

        step("Open estimated refund details");
        refundPage.openEstimatedRefundDetail();
        String totalEstimatedRefund = refundPage.getTotalEstimatedRefundDetails();
        Assertion.assertThat("Refund estimation on the breakdown should be equal on the refund detail page", getIntFromString(totalEstimatedRefund), is(estimatedRefundDetails));
        Assertion.assertThat("Refund details calculation should be equal the total breakdown", refundPage.calculatePriceBreakdown(), is(convertPrice(totalEstimatedRefund)));
    }

    @Author(names = {"Okta","Kartik"})
    @Module(name = "RefundDetail")
    @Tribe(name = "Refund")
    @Vertical(name = "PRO")
    @Test(dataProvider = "testCheckRefundListDetailLoginDataProvider")
    public void testCheckRefundListDetailLogin(Map<String, String> param) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        RefundPage<WebDriver> refundPage = RefundPage.getInstance(session);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");

        step("Open app");
        homePage.gotoHome();

        step("Login as guest");
        loginPage.loginAsGuest();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Set currency to IDR");
        homePage.changeCurrency("IDR",username);

        step("Open Your Orders page");
        homePage.openMyOrderPage();

        step("Open Ongoing Refund tab");
        orderListPage.openOngoingRefundTab();

        step("Find hotel refund in Your Orders list");
        orderListPage.findHotelRefund();

        step("Get hotel refund data in Your Orders list");
        String refundIdList = orderListPage.getRefundId();
        String hotelNameList = orderListPage.getRefundHotelName(refundIdList);
        String estimatedDateList = orderListPage.getHotelRefundEstimatedDate(refundIdList);
        String estimatedPaymentList = orderListPage.getHotelRefundEstimatedPayment(refundIdList);
        String statusList = orderListPage.getHotelRefundStatus();

        step("Open hotel refund detail page");
        orderListPage.openHotelRefund(hotelNameList);

        step("Get hotel refund data in refund detail page");
        String hotelNameOrder = refundPage.getRefundHotelNameOrder();
        String refundIdProgress = refundPage.getRefundIdRefundDetails();
        String statusProgress = refundPage.getHotelRefundStatusProgress(statusList);
        String estimatedDateProgress = refundPage.getHotelRefundEstimatedDateProgress();
        String estimatedDateDetail = refundPage.getHotelRefundEstimatedDateDetail();
        String estimatedPaymentDetail = refundPage.getHotelRefundEstimatedPaymentDetail();

        boolean refundIdResult = refundIdList.equals(refundIdProgress);

        step("Assert refund ID in order list and detail page");
        Assertion.assertThat("Refund ID in order list is the same on detail page", refundIdResult, is(true));

        step("Assert hotel name in order list and detail page");
        Assertion.assertThat("Hotel name in order list is the same on detail page", hotelNameList, is(hotelNameOrder));

        boolean estimatedDateResult = Objects.equals(estimatedDateList, estimatedDateProgress) && Objects.equals(estimatedDateProgress, estimatedDateDetail);

        step("Assert estimated date in order list and detail page");
        Assertion.assertThat("Estimated date in order list is the same on detail page", estimatedDateResult, is(true));

        step("Assert estimated payment in order list and detail page");
        Assertion.assertThat("Estimated payment in order list is the same on detail page", estimatedPaymentList.contains(estimatedPaymentDetail), is(true));

        step("Assert refund status in order list and detail page");
        Assertion.assertThat("Refund status in order list is the same on detail page", statusList, is(statusProgress));
    }

    @DataProvider(name="testCheckRefundListDetailLoginDataProvider")
    public Object[][] testCheckRefundListDetailLoginDataProvider(){
        return getObjectParams("testCheckRefundListDetailLogin");
    }

    @Author(names = {"Zia"})
    @Module(name = "fullRefund")
    @Tribe(name = "Refund")
    @Vertical(name = "PRO")
    @Test(dataProvider="testFullRefundLoginDataProvider")
    public void testFullRefundLogin(Map<String, String> param) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        RefundPage<WebDriver> refundPage = RefundPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String rangeDate = param.get("rangeAddDate");
        String startDate = generateRandomDate(rangeDate);
        String query = param.get("hotelName");
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));
        int room = Integer.parseInt(param.get("room"));

        String[] salutationList = { "Mr", "Mrs", "Ms" };
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = getRandomObject(salutationList);

        step("Create new hotel issued order");
        Boolean result = hotelApi.createIssuedOrderApi(
                username, password, startDate, query, adult, night, room,
                orderFullName, orderNameTitle, guestFullName, guestTitle,"IDR");
        Assertion.assertThat("Hotel Booking via API is successful", result, is(true));

        String issuedOrderID = String.valueOf(hotelApi.getOrderId());
        String issuedRoomName = hotelApi.getHotelRoomName();
        String orderPrice = String.valueOf(hotelApi.getGrandTotal());

        step("Goto home");
        homePage.gotoHome();

        step("Log out");
        loginPage.loginAsGuest();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Open my order page");
        homePage.openMyOrderPage();

        step("Find the issued order");
        orderListPage.openActiveTicketViaDeeplink(issuedOrderID);

        step("Open refund tab");
        orderDetailPage.clickRefundButton();

        step("Choose refund reason");
        refundPage.chooseRefundReason();

        step("Click start refund button");
        refundPage.clickProcessRefundBtn();

        step("Assert refund estimation same as order and using blipay");
        Assertion.assertThat("Correct refund amount shown",refundPage.isUsingBlipay(),is(true));
        Assertion.assertThat("Correct refund amount shown",refundPage.isFullRefund(orderPrice),is(true));
    }

    @DataProvider(name="testFullRefundLoginDataProvider")
    public Object[][] testFullRefundLoginDataProvider() {
        return getObjectParams("testFullRefundLogin");
    }

    @Author(names = {"Ashish"})
    @Module(name = "refund")
    @Tribe(name = "Refund")
    @Vertical(name = "PRO")
    @Test(dataProvider="testSubmitRefundHotelLoginDataProvider")
    public void testSubmitRefundHotelLogin(Map<String, String> param) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        RefundPage<WebDriver> refundPage = RefundPage.getInstance(session);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String rangeDate = param.get("rangeAddDate");
        String startDate = generateRandomDate(rangeDate);
        String query = param.get("hotelName");
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));
        int room = Integer.parseInt(param.get("room"));

        String[] salutationList = { "Mr", "Mrs", "Ms" };
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = getRandomObject(salutationList);

        step("Create new hotel issued order");
        Boolean result = hotelApi.createIssuedOrderApi(
                username, password, startDate, query, adult, night, room,
                orderFullName, orderNameTitle, guestFullName, guestTitle,"IDR");
        Assertion.assertThat("Hotel Booking via API is successful", result, is(true));

        String issuedOrderID = String.valueOf(hotelApi.getOrderId());
        String issuedRoomName = hotelApi.getHotelRoomName();

        step("Goto home");
        homePage.gotoHome();

        step("Log out");
        loginPage.loginAsGuest();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Open my order page");
        homePage.openMyOrderPage();

        step("Find the issued order");
        orderListPage.openActiveTicketViaDeeplink(issuedOrderID);

        step("Order id from order detailPage");
        String orderIdOrderDetailPage = orderDetailPage.getFullOrderId();

        step("Open refund tab");
        orderDetailPage.clickRefundButton();

        step("Choose refund reason");
        String refundReason = refundPage.chooseRefundReason();

        step("Assert selected reason shown");
        boolean isRefundReasonShowing = refundPage.isCorrectReasonShowing(refundReason);

        step("Order id from refund page 1");
        String orderIdOrderRefundPage1 = refundPage.getOrderIdRefundPage();

        step("Click process refund button");
        refundPage.clickProcessRefundBtn();

        step("Order id from refund page 2");
        String orderIdOrderRefundPage2 = refundPage.getOrderIdRefundPage();

        step("Submit refund");
        refundPage.submitRefund();

        step("check refund submit successfully");
        boolean isRefundSubmitSuccessfully = refundPage.checkRefundSubmission();

        Assertion.assertThat("order id are same on all pages",refundPage.checkAllOrderId(orderIdOrderDetailPage,orderIdOrderRefundPage1,orderIdOrderRefundPage2),is(true));
        Assertion.assertThat("Correct refund reason show",isRefundReasonShowing,is(true));
        Assertion.assertThat("is refund submit successfully",isRefundSubmitSuccessfully,is(true));
    }

    @DataProvider(name="testSubmitRefundHotelLoginDataProvider")
    public Object[][] testSubmitRefundHotelLoginDataProvider() {
        return getObjectParams("testSubmitRefundHotelLogin");
    }

    @Author(names = {"Yazid", "Ashish"})
    @Module(name = "PartialTixRefund")
    @Tribe(name = "Refund")
    @Vertical(name = "PRO")
    @Test
    public void testPartialTixRefundLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testPartialTixRefundLogin");
        BaseTest.paramsThreadLocal.set(param);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        RefundPage<WebDriver> refundPage = RefundPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String rangeDate = param.get("rangeAddDate");
        String startDate = generateRandomDate(rangeDate);
        String query = param.get("hotelName");
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));
        int room = Integer.parseInt(param.get("room"));

        String[] salutationList = { "Mr", "Mrs", "Ms" };
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = getRandomObject(salutationList);

        step("Create new hotel issued order");
        Boolean result = hotelApi.createOrderTixPointsApi(
                username, password, startDate, query, adult, night, room,
                orderFullName, orderNameTitle, guestFullName, guestTitle,"IDR");

        Assertion.assertThat("Hotel Booking with tiket points via API is successful", result, is(true));

        String issuedOrderID = String.valueOf(hotelApi.getOrderId());
        String issuedRoomName = hotelApi.getHotelRoomName();
        int totalTiketPoints = Integer.parseInt(hotelApi.getTotalTiketPoints());
        int totalPrice = Integer.parseInt(String.valueOf(hotelApi.getGrandTotal()));

        step("Goto home");
        homePage.gotoHome();

        step("Log out");
        loginPage.loginAsGuest();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Open my order page");
        homePage.openMyOrderPage();

        step("Find the issued order");
        orderListPage.openActiveTicketViaDeeplink(issuedOrderID);

        step("Open refund tab");
        orderDetailPage.clickRefundButton();

        step("Choose refund reason");
        refundPage.chooseRefundReason();

        step("Click start refund button");
        refundPage.clickProcessRefundBtn();

        step("Assert refund estimation same as order");
        Assertion.assertThat("Correct refund amount shown",refundPage.isUsingBlipay(),is(true));
        Assertion.assertThat("refund in partial tiket point",refundPage.checkRefundMethodTixPoints(),is(true));
        Assertion.assertThat("Correct refund amount shown",refundPage.isTiketPointAndTotalPriceCorrect(totalTiketPoints,totalPrice),is(true));
    }

    @Author(names = {"Prerna"})
    @Module(name = "RefundMethod")
    @Tribe(name = "Refund")
    @Vertical(name = "PRO")
    @Test(groups = "specialAccount")
    public void refundMethodSwitchToBankTransfer() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("refundMethodSwitchToBankTransfer");
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

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        RefundPage<WebDriver> refundPage = RefundPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        Unm<WebDriver> unm = new Unm<>(session);

        step("Get api access token");
        LoginDataUnmResponse.Data authData = unm.unmLoginApi(username, password);
        String apiAccessToken = authData.accessToken;
        String accountId = String.valueOf(authData.accountId);

        step("check order present in order");
        MapVerticalWithOrderId.TitleAndOrderId activeOrderData = orderListPage.findFirstOrderFilterByVertical(apiAccessToken,username,accountId,"HOTEL","IDR",false,false,OrderStatus.ETICKET_ISSUED);
        log("Active order data : " + activeOrderData);
        String bookedOrderID;

        if(activeOrderData == null || activeOrderData.orderId==null){
            step("Create new hotel issued order");
            Boolean result = hotelApi.createIssuedOrderApi(
                username, password, startDate, hotelName, adult, night, room,
                orderFullName, orderNameTitle, guestFullName, guestTitle,"IDR");
            Assertion.assertThat("Hotel Booking via API is successful", result, is(true));
            bookedOrderID = String.valueOf(hotelApi.getOrderId());
        }else{
            bookedOrderID = activeOrderData.orderId;
            HotelInternalOrderDetail hotelInternalOrderDetail = orderDetailPage.getHotelInternalOrderData(bookedOrderID);
            hotelName = hotelInternalOrderDetail.data.orderDetails.get(0).orderName;
        }
      
        step("Get data from the issued order");
        String issuedOrderID = String.valueOf(hotelApi.getOrderId());

        step("Goto home");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Open my order page");
        homePage.openMyOrderPage();

        step("Find the issued order");
        orderListPage.openActiveTicketViaDeeplink(issuedOrderID);

        step("Go to the refund button and open the refund page");
        
        orderDetailPage.clickRefundButton();
        orderDetailPage.chooseAndSubmitRefundReason();

        step("Change refund method");
        refundPage.clickProcessRefundBtn();
        refundPage.changeRefundMethodToBankTransfer();
        refundPage.clickContinueButton();
        String refundPaymentMethod = refundPage.selectedRefundMethod();
        boolean refundAmmountcorrect = refundPage.checkRefundAmmountWithBankTransfer();

        step("Assertions");
        Assertion.assertThat("is Bank Transfer selected as Refund Method", refundPaymentMethod, is("Transfer Bank"));
        Assertion.assertThat("is refund ammount correct", refundAmmountcorrect, is(true));
    }

    @Author(names = {"Kartik"})
    @Module(name = "RefundMethod")
    @Tribe(name = "Refund")
    @Vertical(name = "PRO")
    @Test
    public void testCheckRefundListDetailNonIDR()throws ExecutionException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testCheckRefundListDetailNonIDR");
        BaseTest.paramsThreadLocal.set(param);

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        RefundPage<WebDriver> refundPage = RefundPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);

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

        step("Create new hotel issued order");
        String issuedOrderID = hotelApi.createIssuedOrderApi(
                username, password, startDate, hotelName, adult, night, room,
                orderFullName, orderNameTitle, guestFullName, guestTitle,
                currency,cardNumber,expiry,cvv,cardName);
        Assertion.assertThat("Hotel Booking via API is successful", issuedOrderID, is(notNullValue()));

        step("Get data from the issued order");
        String issuedRoomName = hotelApi.getHotelRoomName();

        step("Goto home");
        homePage.gotoHome();

        step("Log out");
        loginPage.loginAsGuest();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Open my order page");
        homePage.openMyOrderPage();

        step("Find the issued order");
        orderListPage.openActiveTicketViaDeeplink(issuedOrderID);

        step("Open refund menu");
        orderDetailPage.clickRefundButton();

        step("Choose refund reason");
        refundPage.chooseRefundReason();

        step("Click process refund button");
        refundPage.clickProcessRefundBtn();

        step("Submit refund");
        refundPage.submitRefund();
        String refundIdRefundDetails = refundPage.getRefundIdRefundDetails();

        step("Get the refund method on the refund process page");
        String refundMethodProcess = refundPage.getRefundMethod();

        step("Go back to the Your Orders page");
        refundPage.clickBackButton();

        step("Open Order List tab");
        orderListPage.openOngoingRefundTab();

        step("Find refund in Your Orders list");
        orderListPage.findRefundCard(refundIdRefundDetails);

        step("Get refund data in Your Orders list");
        String refundIdList = orderListPage.getRefundId();
        String hotelNameList = orderListPage.getRefundHotelName(hotelName);
        String estimatedDateList = orderListPage.getHotelRefundEstimatedDate(refundIdList.trim());
        String estimatedPaymentList = orderListPage.getHotelRefundEstimatedPayment(refundIdList);
        String statusList = orderListPage.getHotelRefundStatus();

        step("Open refund detail page");
        orderListPage.openHotelRefund(hotelNameList);

        step("Get refund data in refund detail page");
        String hotelNameOrder = refundPage.getRefundHotelNameOrder();
        String refundIdProgress = refundPage.getRefundIdRefundDetails();
        String statusProgress = refundPage.getHotelRefundStatusProgress(statusList);
        String refundCompleteDate = refundPage.getCompleteHotelRefundDate().trim();
        String estimatedPaymentDetail = refundPage.getHotelRefundEstimatedPaymentDetail();
        String CurrencyUsed = refundPage.getTheRefundCurrency();
        String refundMethodUsed = refundPage.getRefundMethod();
        boolean refundIdResult = refundIdList.equals(refundIdProgress);

        step("Assert refund ID in order list and detail page");
        Assertion.assertThat("Refund ID in order list is the same on detail page", refundIdResult, is(true));

        step("Assert Product name in order list and detail page");
        Assertion.assertThat("Product name in order list is the same on detail page", hotelNameList, is(hotelNameOrder));

        boolean estimatedDateResult =estimatedDateList.equals(refundCompleteDate);

        step("Assert estimated date in order list and detail page");
        Assertion.assertThat("Estimated date in order list is the same on detail page", estimatedDateResult, is(true));

        step("Assert estimated payment in order list and detail page");
        Assertion.assertThat("Estimated payment in order list is the same on detail page", estimatedPaymentList.contains(estimatedPaymentDetail), is(true));

        step("Assert refund status in order list and detail page");
        Assertion.assertThat("Refund status in order list is the same on detail page", statusList, is(statusProgress));

        step("Assert refund Currency in order list and detail page");
        Assertion.assertThat("Refund status in order list is the same on detail page", CurrencyUsed, is("IDR"));

        step("Assert refund method in order list and detail page");
        Assertion.assertThat("Default refund method should be correct", refundMethodProcess, is(refundMethodUsed));

        step("Open estimated refund details");
        Double estimatedRefundDetails = refundPage.getTotalEstimatedRefundWebView();
        refundPage.openEstimatedRefundDetail();
        String totalEstimatedRefund = refundPage.getTotalEstimatedRefundDetails();
        Assertion.assertThat("Refund estimation on the payment details should be equal on the refund detail page", getIntFromString(totalEstimatedRefund), is(estimatedRefundDetails));
        Assertion.assertThat("Refund details calculation should be equal the total payment details", refundPage.calculatePriceBreakdown(), is(convertPrice(totalEstimatedRefund)));
        refundPage.closeTheRefundPaymentDetails();
    }

    @Author(names = {"Akbar"})
    @Module(name = "Refund")
    @Tribe(name = "Refund")
    @Vertical(name = "PRO")
    @Test
    public void verifyERefundTTDIDR() throws ExecutionException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyERefundTTDIDR");
        BaseTest.paramsThreadLocal.set(param);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        TTDApi<?> ttdApi = new TTDApi<>(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        RefundPage<WebDriver> refundPage = RefundPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        Unm<WebDriver> unm = new Unm<>(session);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");

        String currency = "IDR";

        String productUrlName = param.get("productUrlName");
        boolean refundable = true;
        String cardNumber = param.get("cardNumber");
        String expiry = param.get("expiry");
        String cvv = param.get("cvv");
        String cardName = param.get("cardName");

        String[] salutationList = { "Mr", "Mrs", "Ms" };
        String contactFullName = randomName() + " autorder";
        String contactSalutation = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestEmailAddress = randomEmail();
        String guestSalutation = salutationList[0];

        step("Get api access token");
        LoginDataUnmResponse.Data authData = unm.unmLoginApi(username, password);
        String apiAccessToken = authData.accessToken;
        String accountId = String.valueOf(authData.accountId);

        step("check order present in order");
        MapVerticalWithOrderId activeOrderData = new OrderApi<>(session).getAvailaibleOrderByVertical("ttd", apiAccessToken);
        activeOrderData = orderListPage.filterVerticalOrderByPaymentCurrency("ttd", activeOrderData, currency);
        activeOrderData = refundPage.getRefundAbleOrderByVertical("ttd", activeOrderData, username,accountId);

        String productName="";
        String bookedOrderID = "";
        if(activeOrderData.isEmpty || activeOrderData.ttd.size()==0){
            step("Create new ttd issued order");
            bookedOrderID = ttdApi.createIssuedOrderApi(username, password, currency, productUrlName, refundable, contactFullName, contactSalutation, guestFullName, guestEmailAddress, guestSalutation, cardNumber, expiry, cvv, cardName);
            productName = ttdApi.getProductName();
        }else{
            bookedOrderID = activeOrderData.ttd.get(0).orderId;
            productName = activeOrderData.ttd.get(0).orderName;
        }

        step("Goto home");
        homePage.gotoHome();

        step("Log out");
        loginPage.loginAsGuest();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Find the issued order");
        orderListPage.openActiveTicketViaDeeplink(bookedOrderID);

        step("Open refund menu");
        orderDetailPage.clickRefundButton();

        step("Get the Order ID on the refund details");
        String orderIdRefundDetails = refundPage.getOrderIdRefundDetails();
        Assertion.assertThat("Order ID on the refund detail should be equal with issued order ID", orderIdRefundDetails, is(bookedOrderID));

        step("Choose refund reason");
        refundPage.chooseRefundReasonWebView();

        step("Click process refund button");
        refundPage.clickProcessRefundBtn();

        step("Get the estimated refund on the refund process page");
        Double estimatedRefundProcess = refundPage.getTotalEstimatedRefundWebView();

        step("Submit refund");
        refundPage.submitRefund();

        step("check refund submit successfully");
        boolean isRefundSubmitSuccessfully = refundPage.checkRefundSubmission();
        Assertion.assertThat("Refund successfully submitted", isRefundSubmitSuccessfully, is(true));

        step("Go to the refund detail");
        refundPage.viewRefundDetailPage();

        step("Verify the product name on the refund details");
        String displayedProductName = refundPage.getRefundProductName();
        Assertion.assertThat("Refund page showing correct product name", displayedProductName, containsString(productName));

        step("Get the Refund ID on the refund details");
        String refundIdRefundDetails = refundPage.getRefundIdRefundDetails();

        step("Get the refund currency on the refund detail page");
        String actualCurrency = refundPage.getTheRefundCurrency();
        Assertion.assertThat("The currency should be equal " + currency, actualCurrency, is(currency));

        step("Get the estimated refund on the refund detail page");
        Double estimatedRefundDetails = refundPage.getTotalEstimatedRefundWebView();
        Assertion.assertThat("Refund estimation on the refund detail should be equal with the process page", estimatedRefundDetails, is(estimatedRefundProcess));

        step("Open estimated refund details");
        refundPage.openEstimatedRefundDetail();
        String totalEstimatedRefund = refundPage.getTotalEstimatedRefundDetails();
        Assertion.assertThat("Refund estimation on the payment details should be equal on the refund detail page", CoreFunctions.convertPriceSafely(totalEstimatedRefund), is(estimatedRefundDetails));
        Assertion.assertThat("Refund details calculation should be equal the total payment details", refundPage.calculatePriceBreakdown(), is(convertPrice(totalEstimatedRefund)));
        refundPage.closeTheRefundPaymentDetails();

        step("Go back to the Your Orders page");
        refundPage.clickBackButton();
        orderListPage.openOngoingRefundTab();
        Assertion.assertThat("Ongoing Refund tab should be active", orderListPage.ongoingRefundTabStatus(), is(true));

        step("Find the refund card by refund ID: " + refundIdRefundDetails);
        Assertion.assertThat("Refund ID should be present on the ongoing refund list", orderListPage.findRefundCard(refundIdRefundDetails), is(true));

        step("Open the active tickets tab");
        orderListPage.openActiveTicketTab();

        step("Find the active order that on refund process");
        String orderStatus = orderListPage.findActiveTicket(productName, "-", bookedOrderID, 2);
        Assertion.assertThat("Order status should be \"Memproses refund\" or \"Processing refund\"", orderStatus.toLowerCase(), either(is("memproses refund")).or(is("processing refund")));
        Assertion.assertThat("Refund banner info is visible on the order details", orderDetailPage.refundBannerInfo(), is(true));

        step("Open refund menu");
        orderDetailPage.clickRefundButton();
        Assertion.assertThat("Refund is not available anymore on the order ID: " + bookedOrderID, orderDetailPage.refundNotAvailable(), is(true));
    }

    @Author(names = {"Akbar"})
    @Module(name = "Refund")
    @Tribe(name = "Refund")
    @Vertical(name = "PRO")
    @Test
    public void verifyERefundTTDNonIDR() throws ExecutionException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyERefundTTDNonIDR");
        BaseTest.paramsThreadLocal.set(param);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        TTDApi<?> ttdApi = new TTDApi<>(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        RefundPage<WebDriver> refundPage = RefundPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        Unm<WebDriver> unm = new Unm<>(session);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");

        String currency = param.get("currency");

        String productUrlName = param.get("productUrlName");
        boolean refundable = true;
        String cardNumber = param.get("cardNumber");
        String expiry = param.get("expiry");
        String cvv = param.get("cvv");
        String cardName = param.get("cardName");

        String[] salutationList = { "Mr", "Mrs", "Ms" };
        String contactFullName = randomName() + " autorder";
        String contactSalutation = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestEmailAddress = randomEmail();
        String guestSalutation = salutationList[0];

        step("Get api access token");
        LoginDataUnmResponse.Data authData = unm.unmLoginApi(username, password);
        String apiAccessToken = authData.accessToken;
        String accountId = String.valueOf(authData.accountId);

        step("check order present in order");
        MapVerticalWithOrderId activeOrderData = new OrderApi<>(session).getAvailaibleOrder(apiAccessToken);
        String bookedOrderID = "";
        String orderStatus = "";
        String productName = "";
        int limit = 20;
        activeOrderData = refundPage.getRefundAbleOrder(activeOrderData, username,accountId,limit);
        if(activeOrderData.isEmpty && activeOrderData.ttd.size()==0){
            step("Create new hotel issued order");
            step("Create new TTD order");
            bookedOrderID = ttdApi.createIssuedOrderApi(username, password, currency, productUrlName, refundable,
                    contactFullName, contactSalutation, guestFullName, guestEmailAddress, guestSalutation, cardNumber,
                    expiry, cvv, cardName);
            Assertion.assertThat("TTD Booking via API is successful", bookedOrderID, not(blankOrNullString()));
            productName = ttdApi.getProductName();
        }else{
            bookedOrderID = activeOrderData.ttd.get(0).orderId;
            productName = activeOrderData.ttd.get(0).orderName;
            orderStatus = activeOrderData.ttd.get(0).orderStatus;
        }

        step("Goto home");
        homePage.gotoHome();

        step("Log out");
        loginPage.loginAsGuest();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Open the order page");
        homePage.openMyOrderPage();

        step("Find the issued order");
        orderListPage.openActiveTicketViaDeeplink(bookedOrderID);

        step("Open refund menu");
        orderDetailPage.clickRefundButton();

        step("Choose refund reason");
        refundPage.chooseRefundReasonWebView();

        step("Click process refund button");
        refundPage.clickProcessRefundBtn();

        step("Get the estimated refund on the refund process page");
        Double estimatedRefundProcess = refundPage.getTotalEstimatedRefundWebView();

        step("Submit refund");
        refundPage.submitRefund();

        step("check refund submit successfully");
        boolean isRefundSubmitSuccessfully = refundPage.checkRefundSubmission();
        Assertion.assertThat("Refund successfully submitted", isRefundSubmitSuccessfully, is(true));

        step("Go to the refund detail");
        refundPage.viewRefundDetailPage();

        step("Get the Order ID on the refund details");
        String orderIdRefundDetails = refundPage.getOrderIdRefundDetails();
        Assertion.assertThat("Order ID on the refund detail should be equal with issued order ID", orderIdRefundDetails, is(bookedOrderID));

        step("Verify the product name on the refund details");
        String displayedProductName = refundPage.getRefundProductName();
        Assertion.assertThat("Refund page showing correct product name", displayedProductName, is(productName));

        step("Get the Refund ID on the refund details");
        String refundIdRefundDetails = refundPage.getRefundIdRefundDetails();

        step("Get the refund currency on the refund detail page");
        String actualCurrency = refundPage.getTheRefundCurrency();
        Assertion.assertThat("The currency should be equal " + currency, actualCurrency, is(currency));

        step("Get the estimated refund on the refund detail page");
        Double estimatedRefundDetails = refundPage.getTotalEstimatedRefundWebView();
        Assertion.assertThat("Refund estimation on the refund detail should be equal with the process page", estimatedRefundDetails, is(estimatedRefundProcess));

        step("Open estimated refund details");
        refundPage.openEstimatedRefundDetail();
        String totalEstimatedRefund = refundPage.getTotalEstimatedRefundDetails();
        Assertion.assertThat("Refund estimation on the payment details should be equal on the refund detail page", getIntFromString(totalEstimatedRefund), is(estimatedRefundDetails));
        Assertion.assertThat("Refund details calculation should be equal the total payment details", refundPage.calculatePriceBreakdown(), is(convertPrice(totalEstimatedRefund)));
        refundPage.closeTheRefundPaymentDetails();

        step("Go back to the Your Orders page");
        refundPage.clickBackButton();
        orderListPage.openOngoingRefundTab();
        Assertion.assertThat("Ongoing Refund tab should be active", orderListPage.ongoingRefundTabStatus(), is(true));

        step("Find the refund card by refund ID: " + refundIdRefundDetails);
        Assertion.assertThat("Refund ID should be present on the ongoing refund list", orderListPage.findRefundCard(refundIdRefundDetails), is(true));

        step("Open the active tickets tab");
        orderListPage.openActiveTicketTab();

        step("Find the active order that on refund process");
        orderListPage.openActiveTicketViaDeeplink(bookedOrderID);
        Assertion.assertThat("Order status should be \"Memproses refund\" or \"Processing refund\"", orderStatus.toLowerCase(), either(is("memproses refund")).or(is("processing refund")));
        Assertion.assertThat("Refund banner info is visible on the order details", orderDetailPage.refundBannerInfo(), is(true));

        step("Open refund menu");
        orderDetailPage.clickRefundButton();
        Assertion.assertThat("Refund is not available anymore on the order ID: " + bookedOrderID, orderDetailPage.refundNotAvailable(), is(true));
    }

    @Author(names = {"Prerna"})
    @Module(name = "Refund")
    @Tribe(name = "Refund")
    @Vertical(name = "PRO")
    @Test()
    public void testCheckCarRentalRefundIDR() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testCheckCarRentalRefundIDR");
        BaseTest.paramsThreadLocal.set(param);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        CarRentalPage<WebDriver> carRentalPage = CarRentalPage.getInstance(session);
        RefundPage<WebDriver> refundPage = RefundPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);

        String loginType = param.get("loginType");
        String email = param.get("username");
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

        String cardNumber = param.get("cardNumber");
        String cardHolderName = param.get("cardName");
        String expiry = param.get("expiry");
        String[] expirySplit = expiry.split("/");
        String cardExpiredMonth = expirySplit[0];
        String cardExpiredYear = expirySplit[1];
        String cardCvvNumber = param.get("cvv");
        String adminName = param.get("adminName");
        String adminPhone = param.get("adminPhone");

        step("Create car rental issued order for 1 passenger and 1 day via API");
        Boolean orderResult = carRentalPage.createProductTillPaymentApi(
                email, phoneNumber, password, driverOption, location,
                startDate, pickupTime, type, fullName, nameTitle,1);
        Assertion.assertThat("Car Booking via API is successful", orderResult, is(true));

        step("Get the data from issued order");
        String issuedOrderID = carRentalPage.issuedOrderData("orderId");
        String issuedCarName = carRentalPage.issuedOrderData("carName");
        String issuedCarRentalLocation = carRentalPage.issuedOrderData("carRentalLocation");

        step("Open app");
        homePage.gotoHome();

        step("Login as guest");
        loginPage.loginAsGuest();

        step("Input credential on login page");
        loginPage.loginWith(loginType, email, password);

        step("Open Your Orders page");
        homePage.openMyOrderPage();

        step("Open order complete payment from My Orders Page");
        orderListPage.openOrderCompletePayment(issuedOrderID);

        step("Choose payment method");
        paymentPage.checkOrderID();
        paymentPage.openPaymentMethod();
        paymentPage.selectPaymentMethod("Credit Card");

        step("Fill CC Info");
        paymentPage.fillCardDetailPayment(cardNumber, expiry, cardCvvNumber, cardHolderName);

        step("Click pay button");
        paymentPage.clickPayButton();

        Assertion.assertThat("is Payment Success", paymentPage.isPaymentSuccessful(), is(true));

        step("Go to Your Order Page");
        paymentPage.SeeOrderListAfterPaymentSuccess();

        step("complete order via api ");
        Boolean orderSuccessful = carRentalPage.completeCarrentalOrderApi(issuedOrderID, adminName, adminPhone);
        Assertion.assertThat("isOrderCompleted", orderSuccessful, is(true));

        step("Find issued car rental order");
        orderListPage.openActiveTicketViaDeeplink(issuedOrderID);

        step("Click refund");
        orderDetailPage.clickRefundButton();

        step("Choose refund reason");
        String refundReason = refundPage.chooseRefundReasonWebView();

        step("get refund ammount on refund detail page");
        String refundAmount = refundPage.getTotalEstimatedRefundAmountProcessPage();

        step("Click process refund button");
        refundPage.clickProcessRefundBtn();

        step("Get refund Reason on estimated page");
        String refundReasonEstimatedPage = refundPage.getEstimatedRefundReasonWebView();

        step("Get the estimated refund on the refund process page");
        double estimatedRefundProcess = refundPage.getTotalEstimatedRefundWebView();

        Assertion.assertThat("refund reason selected is same on Estimation Page", refundReason, is(refundReasonEstimatedPage));
        Assertion.assertThat("Refund Amount is same on process page and Estimated Page ", getIntFromString(refundAmount), is(estimatedRefundProcess));

        step("Submit refund");
        refundPage.submitRefund();

        step("check refund submit successfully");
        boolean isRefundSubmitSuccessfully = refundPage.checkRefundSubmission();
        Assertion.assertThat("Refund successfully submitted", isRefundSubmitSuccessfully, is(true));

        step("Go to the refund detail");
        refundPage.viewRefundDetailPage();

        step("Verify the product name on the refund details");
        String displayedProductName = refundPage.getRefundProductName();
        Assertion.assertThat("Refund page showing correct product name", displayedProductName, is(issuedCarName));

        step("Get the Refund ID on the refund details");
        String refundIdRefundDetails = refundPage.getRefundIdRefundDetails();

        step("Get the refund currency on the refund detail page");
        String actualCurrency = refundPage.getTheRefundCurrency();
        String currency = "IDR";
        Assertion.assertThat("The currency should be equal " + currency, actualCurrency, is(currency));

        step("Get the estimated refund on the refund detail page");
        Double estimatedRefundDetails = refundPage.getTotalEstimatedRefundWebView();
        Assertion.assertThat("Refund estimation on the refund detail should be equal with the process page", estimatedRefundDetails, is(estimatedRefundProcess));

        step("Open estimated refund details");
        refundPage.openEstimatedRefundDetail();
        String totalEstimatedRefund = refundPage.getTotalEstimatedRefundDetails();
        Assertion.assertThat("Refund estimation on the payment details should be equal on the refund detail page", getIntFromString(totalEstimatedRefund), is(estimatedRefundDetails));
        refundPage.closeTheRefundPaymentDetails();

        step("Go back to the Your Orders page");
        refundPage.clickBackButton();
        orderListPage.openOngoingRefundTab();
        Assertion.assertThat("Ongoing Refund tab should be active", orderListPage.ongoingRefundTabStatus(), is(true));

        step("Find the refund card by refund ID: " + refundIdRefundDetails);
        Assertion.assertThat("Refund ID should be present on the ongoing refund list", orderListPage.findRefundCard(refundIdRefundDetails), is(true));

        step("Open the active tickets tab");
        orderListPage.openActiveTicketTab();

        step("Find the active order that on refund process");
        String orderStatus = orderListPage.findActiveTicket(issuedCarName, issuedCarRentalLocation, issuedOrderID, 2);
        Assertion.assertThat("Order status should be \"Memproses refund\" or \"Processing refund\"", orderStatus.toLowerCase(), either(is("memproses refund")).or(is("processing refunds")));
        Assertion.assertThat("Refund banner info is visible on the order details", orderDetailPage.refundBannerInfo(), is(true));

        step("Open refund menu");
        orderDetailPage.clickRefundButton();
        Assertion.assertThat("Refund is not available anymore on the order ID: " + issuedOrderID, orderDetailPage.refundNotAvailable(), is(true));
    }

    @Author(names = {"Kartik"})
    @Module(name = "Reschedule")
    @Tribe(name = "Reschedule")
    @Vertical(name = "PRO")
    @Test
    public void testRescheduleRefundMainMethod() {
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

        Map<String, String> param = getParam("testRescheduleRefundMainMethod");
        BaseTest.paramsThreadLocal.set(param);
        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String startDate = generateRandomDate(param.get("rangeAddDate"));

        String query = param.get("hotelName");
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));
        int room = Integer.parseInt(param.get("room"));

        String cardNumber = param.get("cardNumber");
        String cardHolderName = param.get("cardHolderName");
        String cardCvvNumber = param.get("cvv");
        String expiry = param.get("expiry");

        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = getRandomObject(salutationList);
        String paymentMethod = param.get("paymentMethod");

        step("Create new hotel issued order");
        Boolean result = hotelApi.createIssuedOrderApi(
                username, password, startDate, query, adult, night, room,
                orderFullName, orderNameTitle, guestFullName, guestTitle,"IDR");
        Assertion.assertThat("Hotel Booking via API is successful", result, is(true));

        step("Get data from the issued order");
        String issuedOrderID = String.valueOf(hotelApi.getOrderId());

        step("Goto home");
        homePage.gotoHome();

        step("Log out");
        loginPage.loginAsGuest();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Open my order page");
        homePage.openMyOrderPage();

        step("Find the issued order");
        orderListPage.openActiveTicketViaDeeplink(issuedOrderID);

        step("Choose reschedule");
        orderDetailPage.clickReschedule();

        step("Choose new date for the reschedule");
        hotelReschedulePage.clickSubmitSearch();

        step("Choose a new room type with additional Price");
        roomListPage.chooseRoomWithAdditionalPrice();

        step("Submit booking form");
        bookingFormPage.submitRescheduleBookingForm();

        step("Submit the payment method");
        String orderID = paymentPage.checkOrderID();
        paymentPage.openPaymentMethod();
        paymentPage.selectPaymentMethod(paymentMethod);

        step("Fill CC Info");
        paymentPage.fillCardDetailPayment(cardNumber, expiry, cardCvvNumber, cardHolderName);

        step("Click pay button");
        paymentPage.clickPayButton();
        Assertion.assertThat("is Payment Success", paymentPage.isPaymentSuccessful(), is(true));

        step("Go to Your Order Page");
        paymentPage.SeeOrderListAfterPaymentSuccess();

        step("Find the issued order");
        orderListPage.openActiveTicketViaDeeplink(orderID);

        step("Open refund menu");
        orderDetailPage.clickRefundButton();

        step("Choose refund reason");
        refundPage.chooseRefundReason();

        step("Click process refund button");
        refundPage.clickProcessRefundBtn();

        step("Submit refund");
        refundPage.submitRefund();

        step("check refund submit successfully");
        boolean isRefundSubmitSuccessfully = refundPage.checkRefundSubmission();
        Assertion.assertThat("Successfully submit the refund", isRefundSubmitSuccessfully, is(true));

        step("Go to the refund detail");
        Double estimatedRefundDetails = refundPage.getTotalEstimatedRefundWebView();

        step("Get the Order ID on the refund details");
        refundPage.viewRefundDetailPage();

        step("Open estimated refund details");
        refundPage.openEstimatedRefundDetail();
        Assertion.assertThat("Refund details calculation should be equal the total payment details", refundPage.calculatePriceBreakdown(), is(estimatedRefundDetails));
        refundPage.closeTheRefundPaymentDetails();

        step("Get refund data in Your Orders list");
        List<String> refundMethodProcess = refundPage.getRefundMethodNames();

        Assertion.assertThat("First payment method should be blipay", refundMethodProcess.get(0), is("blipay"));
        Assertion.assertThat("Second payment method should be " + paymentMethod, refundMethodProcess.get(1), is(equalToIgnoringCase(paymentMethod)));
    }


    @Author(names = {"Kartik"})
    @Module(name = "RefundMethod")
    @Tribe(name = "Refund")
    @Vertical(name = "PRO")
    @Test
    public void verifyRefundMethodBlipay() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyRefundMethodBlipay");
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
        String blipayPhone = param.get("blipayPhone");

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        RefundPage<WebDriver> refundPage = RefundPage.getInstance(session);

        step("Create new hotel issued order");
        Boolean result = hotelApi.createIssuedOrderApi(
                username, password, startDate, hotelName, adult, night, room,
                orderFullName, orderNameTitle, guestFullName, guestTitle,"IDR");
        Assertion.assertThat("Hotel Booking via API is successful", result, is(true));

        step("Get data from the issued order");
        String issuedOrderID = String.valueOf(hotelApi.getOrderId());

        step("Goto home");
        homePage.gotoHome();

        step("Log out");
        loginPage.loginAsGuest();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Open my order page");
        homePage.openMyOrderPage();

        step("Find the issued order");
        orderListPage.openActiveTicketViaDeeplink(issuedOrderID);

        step("Go to the refund button and open the refund page");
        
        orderDetailPage.clickRefundButton();

        step("Choose refund reason");
        refundPage.chooseRefundReasonWebView();
        refundPage.clickProcessRefundBtn();

        step("Change refund method");
        refundPage.changeRefundMethodToBliPay();

        step("Assertions");
        Assertion.assertThat("is Blipay selected as Refund Method and correct refund method shown", refundPage.isUsingBlipay(), is(true));
        Assertion.assertThat("Registered phone number should be shown correct", refundPage.isRegisteredPhoneExist(blipayPhone), is(true));
    }

    @Author(names = {"Dendi"})
    @Module(name = "RefundMethod")
    @Tribe(name = "Refund")
    @Vertical(name = "PRO")
    @Test
    public void verifyRefundCorporateBilling(){
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyRefundCorporateBilling");
        BaseTest.paramsThreadLocal.set(param);

        String username = param.get("username");
        String password = param.get("password");
        String startDate = generateRandomDate(param.get("rangeAddDate"));
        String approverUsername = param.get("approverUsername");
        String approverPassword = param.get("approverPassword");
        String query = param.get("hotelName");
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));
        int room = Integer.parseInt(param.get("room"));
        String[] salutationList = { "Mr", "Mrs", "Ms" };
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = getRandomObject(salutationList);
        String accountId = param.get("accountId");

        HotelApi<?> hotelApi = new HotelApi<>(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        RefundPage<WebDriver> refundPage = RefundPage.getInstance(session);
        CorporateLoginPage<WebDriver> corporateLoginPage = CorporateLoginPage.getInstance(session);

        step("Get api access token");
        String apiAccessToken = "Bearer " + corporateLoginPage.getAccessTokenB2BCorporate(username,password);

        step("check order present in order");
        TitleAndOrderId activeOrderData = orderListPage.findFirstOrderFilterByVertical(apiAccessToken,username,accountId,"hotel","IDR",true,false);

        String orderID;
        if(activeOrderData == null){
            step("Create new hotel issued order");
            orderID = hotelApi.createIssuedOrderApiB2B(
                    username, password, startDate, query, adult, night, room,"IDR",
                    orderFullName, orderNameTitle, guestFullName, guestTitle, corporateApi.getEmployeeData(username), false);
            Assertion.assertThat("Hotel Booking via API is successful for B2B account", !orderID.isEmpty(), is(true));
            sleep(5);

            step("Approve booking by approver account for Order ID: " + orderID);
            Boolean bookingStatus = corporateApi.approveBookingB2BApi(approverUsername, approverPassword, orderID);
            Assertion.assertThat("Booking approved by B2B dashboard", bookingStatus, is(true));
        }else{
            orderID = activeOrderData.orderId;
        }

        step("Goto home");
        homePage.gotoHome();

        step("Log out");
        loginPage.loginAsGuest();

        step("B2B login");
        corporateLoginPage.corporateLogin(username,password);

        step("Find the issued order");
        orderListPage.openActiveTicketViaDeeplink(orderID);

        step("Skip popup");
        

        step("Open refund menu");
        orderDetailPage.clickRefundButton();

        step("Choose refund reason");
        refundPage.chooseRefundReasonWebView();

        step("Click process refund button");
        refundPage.clickProcessRefundBtn();

        step("Verify refund method is corporate billng");
        String refundMethod = refundPage.getRefundMethod();
        Assertion.assertThat("Verify refund method is corporate billing", refundMethod, equalTo("Corporate Billing"));

    }

    @Author(names = {"Dendi"})
    @Module(name = "RefundMethod")
    @Tribe(name = "Refund")
    @Vertical(name = "PRO")
    @Test
    public void verifyRefundFullTiketPoint(){
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyRefundFullTiketPoint");
        BaseTest.paramsThreadLocal.set(param);

        String username = param.get("username");
        String password = param.get("password");
        String loginMethod = param.get("loginMethod");
        String adminName = param.get("adminName");
        String adminPhoneNumber = param.get("adminPhoneNumber");

        String origin = param.get("origin");
        String destination = param.get("destination");
        String type = param.get("type");
        String pickupTime = param.get("pickupTime");
        int pax = Integer.parseInt(param.get("pax"));
        String rangeAddDate = param.get("rangeAddDate");

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        RefundPage<WebDriver> refundPage = RefundPage.getInstance(session);
        CarTransferPage<WebDriver> carTransferPage = CarTransferPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        Unm<WebDriver> unm = new Unm<>(session);
        CarTransferApi<?> carTransferApi = new CarTransferApi<>(session);

        step("Get api access token");
        LoginDataUnmResponse.Data authData = unm.unmLoginApi(username, password);
        String apiAccessToken = authData.accessToken;
        String accountId = String.valueOf(authData.accountId);

        step("check order present in order");
        MapVerticalWithOrderId activeOrderData = new OrderApi<>(session).getAvailaibleOrder(apiAccessToken);
        int limit = 20;
        activeOrderData = refundPage.getRefundAbleOrder(activeOrderData, username,accountId,limit);
        List<TitleAndOrderId> filteredOrder = orderListPage.filterOrderByPaymentType(activeOrderData,PaymentType.fulltiketpoints);

        String orderID;
        if(filteredOrder== null || filteredOrder.size() == 0 ){
            step("Create car transfer waiting payment order for 1 passenger and 1 day via API");
            orderID = carTransferApi.createWaitingPaymentOrderApi(username,password,origin,destination,type,rangeAddDate,pickupTime,pax);
            Assertion.assertThat("Car transfer booking via API is successful", orderID, not(blankOrNullString()));
        }else{
            step("Get order id from order list");
            orderID = filteredOrder.get(0).orderId;
        }

        step("Goto home");
        homePage.gotoHome();

        step("Log out");
        loginPage.loginAsGuest();

        step("User login");
        loginPage.loginWith(loginMethod,username,password);

        if(filteredOrder== null || filteredOrder.size() == 0 ){
            step("Open payment page via deeplink");
            paymentPage.openWaitingPaymentPage(orderID);
            paymentPage.waitForPaymentPageToLoad(10);
  
            step("Toggle pay with loyalty");
            paymentPage.payWithTiketPoint();
            paymentPage.clickPayButton();

            step("Wait until order got paid");
            paymentPage.SeeOrderListAfterPaymentSuccess();

            step("Issued car rental");
            String completeOrderCode = carTransferApi.confirmOrder(orderID, adminName, adminPhoneNumber);
            log("Is complete success: " + completeOrderCode);
        }

        step("Find the issued order");
        orderListPage.openIssuedTicketViaDeeplink(orderID);

        step("Open refund menu");
        orderDetailPage.clickRefundButton();

        step("Choose refund reason");
        refundPage.chooseRefundReasonWebView();

        step("Click process refund button");
        refundPage.clickProcessRefundBtn();

        step("Verify refund method is corporate billng");
        String refundMethod = refundPage.getRefundMethod();
        Assertion.assertThat("Verify refund method is full tiket point", refundMethod, equalTo("Blibli Tiket Points"));
    }

    @Author(names = {"Zia"})
    @Module(name = "Refund")
    @Tribe(name = "Refund")
    @Vertical(name = "PRO")
    @Test
    public void refundPartialPax() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> testProperties = getParam("refundPartialPax");
        BaseTest.paramsThreadLocal.set(testProperties);

        String email = testProperties.get("email");
        String password = testProperties.get("password");
        String originStation = testProperties.get("originStation");
        String destinationStation = testProperties.get("destinationStation");
        int daysToDepartureDate = Integer.parseInt(testProperties.get("daysToDepartureDate"));
        String searchType = testProperties.get("searchType");
        int adult = Integer.parseInt(testProperties.get("adult"));
        int child = Integer.parseInt(testProperties.get("child"));
        int infant = Integer.parseInt(testProperties.get("infant"));
        String airline = testProperties.get("airline");
        String phoneNumber = testProperties.get("phoneNumber");
        String loginType = testProperties.get("loginType");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightPage<WebDriver> flightPage = FlightPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        RefundPage<WebDriver> refundPage = RefundPage.getInstance(session);

        step("set date and get depart date in YYYY-MM-DD format");
        flightPage.setDataOneWay(daysToDepartureDate, airline);
        String departureDate = flightPage.getDate("departureDate");

        step("complete flight booking via API");
        String[] passengerNames = {CoreFunctions.randomNameWithoutTest() + " Testing", CoreFunctions.randomNameWithoutTest() + " Testing"};
        String orderID = flightPage.createOrderFlightB2CDomestic(email, password, originStation, destinationStation, departureDate, searchType, adult, child, infant, airline, passengerNames, phoneNumber);
        Assertion.assertThat("Flight order created", orderID, not(""));

        step("Goto home");
        homePage.gotoHome();

        step("Login as " + email + " via deeplink");
        homePage.loginWithSpecificAccountViaDeeplinkOnly(email, password);

        step("get booking code from API");
        String bookingCode = flightPage.getBookingCode(orderID, email);

        step("set Tiket Number on flight dashboard and ticket will be eticket issued");
        Boolean isTicketIssued = flightPage.flightEticketIssuedRequest(orderID, bookingCode, email,passengerNames);
        Assertion.assertThat("is Eticket Issued", isTicketIssued, is(true));

        step("Open the order list and go to order detail");
        homePage.openMyOrderPage();

        step("check order status");
        orderListPage.openIssuedTicketViaDeeplink(orderID);
        Assertion.assertThat("OrderStatus is Eticket issued", orderDetailPage.getETicketLabel(), either(is("Your e-ticket is here")).or(is("E-tiketmu di sini")));

        step("Get order data");
        FlightTicketDetail flightDetail = new FlightTicketDetail();
        flightDetail.flightAirline= airline;
        flightDetail.flightDepartAirport = originStation;
        flightDetail.flightArrivalAirport = destinationStation;

        step("Open refund menu");
        orderDetailPage.clickRefundButton();

        step("Verify order in refund page match");
        Assertion.assertThat("Order detail match in refund page", refundPage.verifyFlightOrder(flightDetail), is(true));

        step("Choose refund reason");
        refundPage.chooseRefundReasonWebViewFlight();

        step("Pick one passenger");
        refundPage.selectPassenger(1);
        refundPage.chooseReasonPerPax(1);

        step("Check estimated refund amount currency is IDR");
        Assertion.assertThat("total estimation price currency is IDR", refundPage.checkTotalEstimationPriceCurrency("IDR"), is(true));

        step("Get estimated refund amount");
        String refundAmountEstimation = refundPage.getEstimatedRefund();

        step("Continue refund");
        refundPage.clickProcessRefundBtn();

        step("Verify total refund match previous page");
        String refundAmountTotal = refundPage.getEstimatedRefund();
        Assertion.assertThat("Refund amount match", refundAmountEstimation, is(refundAmountTotal));

        step("Verify refund breakdown match");
        refundPage.openEstimatedRefundDetail();
        Assertion.assertThat("Refund details calculation should be equal the total payment details", refundPage.verifyPriceBreakDown(), is(true));
        refundPage.closeTheRefundPaymentDetails();

        step("Click agree tnc");
        refundPage.clickCheckBoxTnC();

        step("Click submit refund");
        refundPage.clickSubmitRefund();

        step("Verify refund submit successfully");
        Assertion.assertThat("Refund successfully submitted", refundPage.checkRefundSubmission(), is(true));

        step("Go to the refund detail");
        refundPage.viewRefundDetailPage();

        step("Go back to order detail");
        refundPage.clickBackButtonDetail();

        step("check order status");
        orderListPage.openIssuedTicketViaDeeplink(orderID);
        Assertion.assertThat("OrderStatus is Eticket issued", orderDetailPage.getETicketLabel(), either(is("Your e-ticket is here")).or(is("E-tiketmu di sini")));
        Assertion.assertThat("1 pax is refund process",orderDetailPage.getRefundHeader(),containsString("1"));
    }

    @Author(names = {"Prerna"})
    @Module(name = "Refund")
    @Tribe(name = "Refund")
    @Vertical(name = "PRO")
    @Test
    public void verifyCurrencyOnHotelRefundEstimationPageIDR() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyCurrencyOnHotelRefundEstimationPageIDR");
        BaseTest.paramsThreadLocal.set(param);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        RefundPage<WebDriver> refundPage = RefundPage.getInstance(session);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String startDate = generateRandomDate(param.get("rangeAddDate"));
        String hotelName = param.get("hotelName");
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));
        int room = Integer.parseInt(param.get("room"));

        String[] salutationList = { "Mr", "Mrs", "Ms" };
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = salutationList[0];

        step("Create new hotel issued order");
        Boolean result = hotelApi.createIssuedOrderApi(
                username, password, startDate, hotelName, adult, night, room,
                orderFullName, orderNameTitle, guestFullName, guestTitle,"IDR");
        Assertion.assertThat("Hotel Booking via API is successful", result, is(true));

        step("Get data from the issued order");
        String issuedOrderID = String.valueOf(hotelApi.getOrderId());

        step("Goto home");
        homePage.gotoHome();

        step("Log out");
        loginPage.loginAsGuest();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Open my order page");
        homePage.openMyOrderPage();

        step("Find the issued order");
        orderListPage.openActiveTicketViaDeeplink(issuedOrderID);

        step("Open refund menu");
        orderDetailPage.clickRefundButton();

        step("click Continue to Refund");
        refundPage.continueToRefundPopup();

        step("Choose refund reason");
        refundPage.chooseRefundReasonWebView();

        step("Check estemated refund ammount");
        Assertion.assertThat("total estimation price currency is IDR", refundPage.checkTotalEstimationPriceCurrency("IDR"), is(true));

        step("Click estimations for different dates");
        refundPage.clickCheckEstimationsForDifferentDates();

        Assertion.assertThat("The prices on Estimation page should have IDR Currency", refundPage.estimationPageCurrencyIsAsExpected("IDR"), is(true));

        step("Click process refund button");
        refundPage.clickProcessRefundBtn();
        
        Assertion.assertThat("Price on 2nd estimation page is as expected",refundPage.checkTotalEstimationPriceCurrency("IDR") , is(true) );

    }

    @Author(names = {"Prerna"})
    @Module(name = "Refund")
    @Tribe(name = "Refund")
    @Vertical(name = "PRO")
    @Test
    public void verifyCurrencyOnHotelRefundEstimationPageNonIDR() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyCurrencyOnHotelRefundEstimationPageNonIDR");
        BaseTest.paramsThreadLocal.set(param);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        RefundPage<WebDriver> refundPage = RefundPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        Unm<WebDriver> unm = new Unm<>(session);

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
        String issuedRoomName = "";
        double issuedTotalPayment = 0;

        step("Get api access token");
        LoginDataUnmResponse.Data authData = unm.unmLoginApi(username, password);
        String accessToken = authData.accessToken;
        String accountId = String.valueOf(authData.accountId);

        step("check order present in order");
        MapVerticalWithOrderId.TitleAndOrderId activeOrderData = orderListPage.findFirstOrderFilterByVertical(accessToken,username,accountId,"HOTEL",currency,true,false,OrderStatus.ETICKET_ISSUED);
        log("Active order data : " + activeOrderData);
        String bookedOrderID;

        if(activeOrderData == null || activeOrderData.orderId==null){
            step("Create new hotel issued order");
            String issuedOrderID = hotelApi.createIssuedOrderApi(
                    username, password, startDate, hotelName, adult, night, room,
                    orderFullName, orderNameTitle, guestFullName, guestTitle,
                    currency,cardNumber,expiry,cvv,cardName);
            Assertion.assertThat("Hotel Booking via API is successful", issuedOrderID, is(notNullValue()));
            bookedOrderID = String.valueOf(hotelApi.getOrderId());
            issuedTotalPayment = Double.parseDouble(String.valueOf(hotelApi.getGrandTotal()));
            issuedRoomName = hotelApi.getHotelRoomName();
        }else{
            bookedOrderID = activeOrderData.orderId;
            HotelInternalOrderDetail hotelInternalOrderDetail = orderDetailPage.getHotelInternalOrderData(bookedOrderID);
            hotelName = hotelInternalOrderDetail.data.orderDetails.get(0).orderName;
            issuedTotalPayment = hotelInternalOrderDetail.data.payments.get(0).getPaymentAmounAsDouble();
            issuedRoomName = hotelInternalOrderDetail.data.orderDetails.get(0).orderNameDetail;
        }

        step("Goto home");
        homePage.gotoHome();

        step("Log out");
        loginPage.loginAsGuest();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Open my order page");
        homePage.openMyOrderPage();

        step("Find the issued order");
        orderListPage.openActiveTicketViaDeeplink(bookedOrderID);

        step("Open refund menu");
        orderDetailPage.clickRefundButton();

        step("click Continue to Refund");
        refundPage.continueToRefundPopup();

        step("Choose refund reason");
        refundPage.chooseRefundReasonWebView();

        step("Check estemated refund ammount");
        Assertion.assertThat("total estimation price currency is "+currency, refundPage.checkTotalEstimationPriceCurrency(currency), is(true));

        step("Click estimations for different dates");
        refundPage.clickCheckEstimationsForDifferentDates();

        Assertion.assertThat("The prices on Estimation page should have Currency "+ currency, refundPage.estimationPageCurrencyIsAsExpected(currency), is(true));

        step("Click process refund button");
        refundPage.clickProcessRefundBtn();
        
        Assertion.assertThat("Price on 2nd estimation page is as expected",refundPage.checkTotalEstimationPriceCurrency(currency) , is(true) );

    }

    @Author(names = {"Ashish"})
    @Module(name = "Refund")
    @Tribe(name = "Refund")
    @Vertical(name = "PRO")
    @Test
    public void verifyEstimationPageFunctionalityForERefund() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyEstimationPageFunctionalityForERefund");
        BaseTest.paramsThreadLocal.set(param);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
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
        String issuedOrderID = hotelApi.createIssuedOrderApi(
                username, password, startDate, hotelName, adult, night, room,
                orderFullName, orderNameTitle, guestFullName, guestTitle,
                currency,cardNumber,expiry,cvv,cardName);
        Assertion.assertThat("Hotel Booking via API is successful", issuedOrderID, is(notNullValue()));

        step("Goto home");
        homePage.gotoHome();

        step("Log out");
        loginPage.loginAsGuest();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Open my order page");
        homePage.openMyOrderPage();

        step("Find the issued order");
        orderListPage.openActiveTicketViaDeeplink(issuedOrderID);

        step("Open refund menu");
        orderDetailPage.clickRefundButton();

        step("click Continue to Refund");
        refundPage.continueToRefundPopup();

        step("verify back btn and header of refund page");
        Assertion.assertThat("is refund titled header displayed on refund page",refundPage.isHeaderRefundDisplayed(),is(true));
        Assertion.assertThat("is back btn displayed on header",refundPage.backBtnDisplayedOnHeader(),is(true));

        step("check back btn functionality");
        refundPage.clickBackButtonDetail();
        Assertion.assertThat("is back btn function working fine",refundPage.checkBackBtnFunctionality(),is(true));

        step("check select reason functionality");
        Assertion.assertThat("is select reason functionality working",refundPage.verifySelectReasonFunctionality(),is(true));

        step("Choose refund reason");
        String refundReason = refundPage.chooseRefundReasonWebView();
        Assertion.assertThat("selected reason { "+refundReason+" } is displayed on refund page 1",refundPage.getSelectedReason(),is(containsString(refundReason)));

        step("verify estimation popup functionality");
        Assertion.assertThat("is estimation calculation correct",refundPage.verifyCalculationForEstimation(currency),is(true));
        refundPage.openEstimationDetailPopup();
        Assertion.assertThat("is estimation calculation on estimation popup correct",refundPage.verifyCalculationForEstimationPopup(currency),is(true));
        Assertion.assertThat("is close btn functionality working fine",refundPage.closeEstimationDetailPopup(),is(true));

        step("verify estimation on other date functionality");
        String todayRefundAmount = refundPage.getTodayRefundAmount(currency);
        refundPage.clickCheckEstimationsForDifferentDates();
        Assertion.assertThat("estimation on other date functionality",refundPage.verifyEstimationOnOtherDateFunctionality(todayRefundAmount),is(true));
        Assertion.assertThat("is close btn functionality working fine",refundPage.closeEstimationDetailPopup(),is(true));

        step("help center guide functionality");
        Assertion.assertThat("open the help center guide and verify back btn functionality",refundPage.openAndVerifyHelpCenterGuide(),is(true));

        step("important info functionality");
        Assertion.assertThat("is important info functionality working",refundPage.openAndVerifyImportantInfoFunctionality(),is(true));

        step("go to refund page 2 and go back to refund page 1 and verify all fields still cached");
        refundPage.clickProcessRefundBtn();
        Assertion.assertThat("is on refund page 2",refundPage.isOnRefundPage2(),is(true));
        Assertion.assertThat("go back to refund page 1 and verify all fields still cached",refundPage.goBackToRefundPage1AndVerifyData(refundReason),is(true));
        refundPage.clickProcessRefundBtn();

        step("verify estimation popup functionality");
        refundPage.openEstimationDetailPopup();
        Assertion.assertThat("is estimation calculation on estimation popup correct",refundPage.verifyCalculationForEstimationPopup(currency),is(true));
        Assertion.assertThat("is close btn functionality working fine",refundPage.closeEstimationDetailPopup(),is(true));

        step("verify term and conditions");
        Assertion.assertThat("is term and conditions popup working fine",refundPage.openTermAndConditionsAndVerifyFunctionality(),is(true));

        step("verify check box functionality");
        Assertion.assertThat("check box functionality working fine",refundPage.verifyCheckboxFunctionality(),is(true));

        step("verify re-check btn functionality after clicking submit to refund");
        Assertion.assertThat("re-check btn functionality after clicking submit to refund",refundPage.verifyReCheckBtn(),is(true));

        step("Click submit refund");
        refundPage.clickSubmitRefund();

        step("Verify refund submit successfully");
        Assertion.assertThat("Refund successfully submitted", refundPage.checkRefundSubmission(), is(true));
    }

    @Author(names = {"Zia"})
    @Module(name = "Refund")
    @Tribe(name = "Refund")
    @Vertical(name = "PRO")
    @Test
    @SneakyThrows
    public void verifySwitchRefundPaymentMethod() {
        Session<WebDriver> session = sessionThreadLocal.get();

        Map<String, String> param = getParam("verifySwitchRefundPaymentMethod");
        BaseTest.paramsThreadLocal.set(param);

        HotelApi<?> hotelApi = new HotelApi<>(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        RefundPage<WebDriver> refundPage = RefundPage.getInstance(session);
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
        String guestTitle = getRandomObject(salutationList);

        step("Goto home");
        homePage.gotoHome();

        step("user login");
        loginPage.loginAsGuest();
        loginPage.loginWith(loginType, username, password);

        step("Create new hotel issued order");
        String orderId = hotelApi.createWaitingPaymentOrderApi(
                username, password, startDate, hotelName, adult, night, room,
                orderFullName, orderNameTitle, guestFullName, guestTitle,
                "IDR","id");
        Assertion.assertThat("Success to create waiting payment order", orderId, is(not(emptyOrNullString())));

        step("Open the order page");
        homePage.openMyOrderPage();

        step("Complete order with the payment method");
        orderListPage.openOrderCompletePayment(orderId);
        paymentPage.openPaymentMethod();
        paymentPage.selectPaymentMethod("ATM");
        paymentPage.clickPayButton();
        paymentPage.paymentChecker();

        step("Open order list page");
        paymentPage.seeOrderList();

        step("Open order detail");
        orderListPage.openIssuedTicketViaDeeplink(orderId);

        step("Open refund menu");
        orderDetailPage.clickRefundButton();

        step("choose refund webview");
        refundPage.chooseRefundReasonWebView();

        step("Continue refund");
        refundPage.clickProcessRefundBtn();

        step("Verify current selected method is BlibliPay");
        Assertion.assertThat("Current selected method is blipay", refundPage.getCurrentMethod(), is("BlibliPay"));

        step("Switch to bank transfer");
        refundPage.changeRefundMethodToBankTransfer();
        long bankDetails = Long.parseLong(refundPage.getCurrentMethod());
        Assertion.assertThat("Current selected method is bank transfer",bankDetails , isA(long.class));

        step("Switch to BlibliPay again");
        refundPage.changeRefundMethodToBliPay();
        Assertion.assertThat("Current selected method is blipay", refundPage.getCurrentMethod(), is("BlibliPay"));
    }


    @Author(names = {"Prerna"})
    @Module(name = "Refund")
    @Tribe(name = "Refund")
    @Vertical(name = "PRO")
    @Test
    public void verifyAddBankAccountOnEstimationPage() {
        Map<String, String> map = getParam("verifyAddBankAccountOnEstimationPage");
        BaseTest.paramsThreadLocal.set(map);

        Session<WebDriver> session = sessionThreadLocal.get();

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        RefundPage<WebDriver> refundPage = RefundPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");

        String location = map.get("location");
        String payment_method = map.get("paymentMethod");

        String accountNumber = map.get("accountNumber");
        String accountName = map.get("accountName");
        String bankName = map.get("bankName");
        String date = map.get("rangeAddDate");

        step("Goto home");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Open hotel landing page");
        homePage.changeCurrency("IDR", username);
        homePage.openVerticalPageDeeplink("Hotel","","IDR");

        step("Search stay location");
        landingPage.searchStayLocation(location);

        step("set stay date");
        landingPage.setStayDate("range date", date);

        step("Submit search");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        step("Go to the room list");
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();

        step("Select room from the room list");
        roomListPage.clickBookRoom();

        step("Submit booking");
        bookingFormPage.continueToPaymentPage();
        bookingFormPage.PriceChangesPopUp();

        step("Choose payment method and pay with credit card");
        String expectedOrderId = paymentPage.checkOrderID();
        paymentPage.openPaymentMethod();
        paymentPage.selectPaymentMethod(payment_method);

        step("Submit payment");
        paymentPage.clickPayButton();

        step("Go to the order list");
        paymentPage.seeOrderList();

        step("Find the issued order");
        orderListPage.openActiveTicketViaDeeplink(expectedOrderId);

        step("Open refund menu");
        orderDetailPage.clickRefundButton();

        step("click Continue to Refund");
        refundPage.continueToRefundPopup();

        step("Choose refund reason");
        refundPage.chooseRefundReasonWebView();

        step("Click process refund button");
        refundPage.clickProcessRefundBtn();

        step("Click Edit icon under Bank account");
        refundPage.clickEditBankAccount();

        step("Add Bank Accounts");
        refundPage.addBankAccountDetails(accountNumber, bankName);

        step("click check Bank account");
        refundPage.clickCheckBankAccount();

        step("input wrong otp");
        refundPage.inputOTPWhileAddingBankAccount("1233");

        Assertion.assertThat("OTP Is Wrong", refundPage.isOTPWrong(), is(true));

        step("click check Bank account");
        refundPage.clickCheckBankAccount();

        step("input correct otp");
        refundPage.inputOTPWhileAddingBankAccount("1234");

        step("Verify account holder name");
        Assertion.assertThat("Account holder name is as expected", refundPage.accountHolderName(), is(accountName));
    }

    @Author(names = {"Zia"})
    @Module(name = "Refund")
    @Tribe(name = "Refund")
    @Vertical(name = "PRO")
    @Test
    public void verifyRescheduleNudgesNoFeature() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyRescheduleNudgesNoFeature");
        BaseTest.paramsThreadLocal.set(param);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        CarRentalPage<WebDriver> carRentalPage = CarRentalPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        RefundPage<WebDriver> refundPage = RefundPage.getInstance(session);
        Unm<WebDriver> unm = new Unm<>(session);

        String loginType = param.get("loginType");
        String email = param.get("username");
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

        String adminName = param.get("adminName");
        String adminPhone = param.get("adminPhone");
        String dashboardUsername = param.get("dashboardUsername");
        String dashboardPassword = param.get("dashboardPassword");

        LoginDataUnmResponse.Data authData = unm.unmLoginApi(email, password);
        String apiAccessToken = authData.accessToken;
        String accountId = String.valueOf(authData.accountId);

        step("Create car rental issued order for 1 passenger and 1 day via API");
        Boolean orderResult = carRentalPage.createIssuedOrderApiV4(
                email, password, driverOption, location,
                startDate, pickupTime, type, fullName, nameTitle, adminName, adminPhone,
                dashboardUsername, dashboardPassword);
        Assertion.assertThat("Car Booking via API is successful", orderResult, is(true));
        MapVerticalWithOrderId.TitleAndOrderId activeOrderData = orderListPage.findFirstOrderFilterByVertical(apiAccessToken, email, accountId, "CAR", "", false, false, OrderStatus.ETICKET_ISSUED);

        // Active order list is capped and sorted by travel date, so the new order may be missing; fall back to the created order id
        String issuedOrderID = activeOrderData != null && activeOrderData.orderId != null
                ? activeOrderData.orderId
                : carRentalPage.getLastCreatedOrderId();
        Assertion.assertThat("Issued car rental order id is available", issuedOrderID != null, is(true));

        step("Open app");
        homePage.gotoHome();

        step("Login as guest");
        loginPage.loginAsGuest();

        step("Input credential on login page");
        loginPage.loginWith(loginType, email, password);

        step("Find issued car rental order");
        orderListPage.openIssuedTicketViaDeeplink(issuedOrderID);

        step("Click refund");
        orderDetailPage.clickRefundButtonWithoutSkip(issuedOrderID);

        step("Verify in refund page");
        Assertion.assertThat("Is refund page open ",refundPage.isInRefundPage(),is(true));
    }

    @Author(names = {"Zia"})
    @Module(name = "Refund")
    @Tribe(name = "Refund")
    @Vertical(name = "PRO")
    @Test
    public void verifyRescheduleNudgesNotReschedulable() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyRescheduleNudgesNotReschedulable");
        BaseTest.paramsThreadLocal.set(param);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        RefundPage<WebDriver> refundPage = RefundPage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        HotelReschedulePage<WebDriver> hotelReschedulePage = HotelReschedulePage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String startDate = generateRandomDate(param.get("rangeAddDate"));
        String hotelName = param.get("hotelName");
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));
        int room = Integer.parseInt(param.get("room"));
        String cardNumber = param.get("cardNumber");
        String expiry = param.get("expiry");
        String cvv = param.get("cvv");
        String cardName = param.get("cardName");

        String[] salutationList = { "Mr", "Mrs", "Ms" };
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = salutationList[0];

        step("Create new hotel issued order");
        Boolean result = hotelApi.createIssuedOrderApi(
                username, password, startDate, hotelName, adult, night, room,
                orderFullName, orderNameTitle, guestFullName, guestTitle,"IDR");
        Assertion.assertThat("Hotel Booking via API is successful", result, is(true));
        String issuedOrderID = String.valueOf(hotelApi.getOrderId());

        step("Open app");
        homePage.gotoHome();

        step("Login as guest");
        loginPage.loginAsGuest();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Open order detail");
        orderListPage.openIssuedTicketViaDeeplink(issuedOrderID);

        step("Click reschedule");
        orderDetailPage.clickReschedule(issuedOrderID);

        step("Choose new date for the reschedule");
        hotelReschedulePage.clickSubmitSearch();

        step("Choose room");
        roomListPage.chooseRoomWithAdditionalPrice();

        step("Submit booking form");
        bookingFormPage.submitRescheduleBookingForm();

        step("Payment page should be displayed");
        String newOrderID = paymentPage.checkOrderID();
        Assertion.assertThat("Order id should be displayed", !newOrderID.isEmpty(), is(true));

        step("Pay with CC");
        paymentPage.openPaymentMethod();
        paymentPage.selectPaymentMethod("Credit Card");

        step("Fill Credit Card Details");
        paymentPage.fillCCDetailRealIssued(cardNumber, expiry, cvv, cardName);

        step("Click pay Button");
        paymentPage.clickPayButtonCC();
        paymentPage.verifyCreditCard3ds();

        step("Assert if the payment is a success");
        Assertion.assertThat("Payment Successful and Thank You page is shown", paymentPage.isThankYouPageShown(), is(true));

        step("Check the new order detail");
        orderListPage.openIssuedTicketViaDeeplink(newOrderID);

        step("Click refund");
        orderDetailPage.clickRefundButtonWithoutSkip(newOrderID);

        step("Verify in refund page");
        Assertion.assertThat("Is refund page open ",refundPage.isInRefundPage(),is(true));
    }

    @Author(names = {"Zia"})
    @Module(name = "Refund")
    @Tribe(name = "Refund")
    @Vertical(name = "PRO")
    @Test
    public void verifyCompleteFlowPastRefundListAndItsDetails() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyCompleteFlowPastRefundListAndItsDetails");
        BaseTest.paramsThreadLocal.set(param);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderHistoryPage<WebDriver> orderHistoryPage = OrderHistoryPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        SettingPage<WebDriver> settingPage = SettingPage.getInstance(session);

        String loginType = param.get("loginType");
        String email = param.get("username");
        String password = param.get("password");

        step("Open app");
        homePage.gotoHome();

        step("User open app as login user " +email);
        loginPage.loginAsGuest();
        loginPage.loginWith(loginType, email, password);

        step("Open my order page");
        homePage.openMyOrderPage();

        step("Open order history page");
        orderListPage.openOrderHistory();

        step("click past refund");
        orderHistoryPage.openPastRefund();

        step("check currency change");
        String getRefundIdOfFirstPastRefund = orderHistoryPage.getPastRefundId(1);
        String currencyOfFirstPastRefund = orderHistoryPage.getPastRefundCurrency(getRefundIdOfFirstPastRefund);
        String orderTitle = orderHistoryPage.getPastRefundOrderTitle(getRefundIdOfFirstPastRefund);

        step("go back and change to different currency");
        homePage.goToHomeDeeplink();
        String otherCurrency = orderHistoryPage.getOtherCurrencyThenSaid(currencyOfFirstPastRefund);
        homePage.changeCurrency(otherCurrency,email);

        step("change language to eng");
        homePage.openAccount();
        accountPage.selectLanguage("eng");
        settingPage.backToHome();

        step("Open my order page");
        homePage.openMyOrderPage();

        step("Open order history page");
        orderListPage.openOrderHistory();

        step("click past refund");
        orderHistoryPage.openPastRefund();

        step("verify past refund");
        orderHistoryPage.swipeToPastRefund(getRefundIdOfFirstPastRefund);
        Assertion.assertThat("past refund card is correct",orderHistoryPage.verifyPastRefundCard(getRefundIdOfFirstPastRefund,currencyOfFirstPastRefund),is(true));

        step("open refund detail and verify");
        orderHistoryPage.openPastRefundDetail(getRefundIdOfFirstPastRefund);
        Assertion.assertThat("past refund detail is correct",orderHistoryPage.verifyPastRefundDetail(getRefundIdOfFirstPastRefund,currencyOfFirstPastRefund,orderTitle),is(true));
        orderHistoryPage.clickBackBtnOnRefundDetailPage();

        step("verify the redirection");
        Assertion.assertThat("when click back btn will be redirected to past refund",orderHistoryPage.isRedirectedToCorrectPage(),is(true));
    }

    @Author(names = {"Zia"})
    @Module(name = "Refund")
    @Tribe(name = "Refund")
    @Vertical(name = "PRO")
    @Test
    public void verifyCompleteFlowActiveRefundListAndItsDetails() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyCompleteFlowActiveRefundListAndItsDetails");
        BaseTest.paramsThreadLocal.set(param);

        String loginType = param.get("loginType");
        String email = param.get("username");
        String password = param.get("password");

        HotelApi<?> hotelApi = new HotelApi<>(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        RefundCreation<WebDriver> refundCreation = new RefundCreation<>(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        OrderHistoryPage<WebDriver> orderHistoryPage = OrderHistoryPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        SettingPage<WebDriver> settingPage = SettingPage.getInstance(session);


        if (Configs.ENVIRONMENT != Environment.PROD) {
            String dashboardUsername = param.get("dashboardUsername");
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

            step("Create new issued order via API");
            hotelApi.createIssuedOrderApi(
                    "en", email, password, startDate, hotelName, adult, night, room,
                    orderFullName, orderNameTitle, guestFullName, guestTitle, currency, false);

            step("Get data from the issued order via API");
            String orderId = String.valueOf(hotelApi.getOrderId());
            JSONObject internalOrderData = orderListPage.getInternalOrderData(orderId);
            GeneralVerticalInternalOrderDetail internalOrderDetail = orderListPage.getVerticalInternalOrderDtata(internalOrderData);
            String amountFromVendor = String.valueOf(internalOrderDetail.data.payment.get(0).paymentAmount);
            long orderDetailId = Long.parseLong(internalOrderDetail.data.orderDetails.get(0).orderDetailId);
            long orderIdFromApi = Long.parseLong(internalOrderDetail.data.orderId);

            step("Submit refund from the dashboard API");
            refundCreation.createFormMiddlewareApi(
                    amountFromVendor,
                    false,
                    null,
                    orderDetailId,
                    orderIdFromApi,
                    "customer_request",
                    "Dummy Name",
                    "1414141414",
                    "bank-mandiri-idn",
                    "Automation App",
                    dashboardUsername);
        }

        step("Open app");
        homePage.gotoHome();

        step("Login as " + email + " via deeplink");
        homePage.loginWithSpecificAccountViaDeeplinkOnly(email, password);

        step("Open my order page");
        homePage.openMyOrderPage();

        step("Open ongoing refund tab");
        orderListPage.openOngoingRefundTab();

        step("check currency change");
        String getRefundIdOfFirstPastRefund = orderListPage.getRefundId();
        String currencyOfFirstPastRefund = orderListPage.getEstimatedTotalPayment(getRefundIdOfFirstPastRefund).split(" ")[0];
        String orderTitle = orderListPage.getRefundOrderTitle(getRefundIdOfFirstPastRefund);

        step("go back and change to different currency");
        homePage.goToHomeDeeplink();
        String otherCurrency = orderHistoryPage.getOtherCurrencyThenSaid(currencyOfFirstPastRefund);
        homePage.changeCurrency(otherCurrency,email);

        step("Change language to eng");
        homePage.openAccount();
        accountPage.selectLanguage("eng");
        settingPage.backToHome();

        step("Open my order page");
        homePage.openMyOrderPage();

        step("Open ongoing refund  page");
        orderListPage.openOngoingRefundTab();

        step("verify active refund");
        orderListPage.swipeToActiveRefund(getRefundIdOfFirstPastRefund);
        Assertion.assertThat("active refund card is correct",orderListPage.verifyActiveRefundCard(getRefundIdOfFirstPastRefund,currencyOfFirstPastRefund),is(true));

        step("open refund detail and verify");
        orderListPage.openRefundCard(getRefundIdOfFirstPastRefund);
        Assertion.assertThat("active refund detail is correct",orderHistoryPage.verifyActiveRefundDetail(getRefundIdOfFirstPastRefund,currencyOfFirstPastRefund,orderTitle),is(true));
        orderHistoryPage.clickBackBtnOnRefundDetailPage();

        step("verify the redirection");
        if (Configs.PLATFORM == Platform.ANDROID) Assertion.assertThat("refund details webview closed, user directed to active order list", orderListPage.ongoingRefundTabStatus(), is(true));
        if (Configs.PLATFORM == Platform.IOS) Assertion.assertThat("refund details webview closed, user directed to active order list", orderListPage.isOrderListPage(), is(true));    }

    @Author(names = {"Ashish"})
    @Module(name = "Refund")
    @Tribe(name = "Refund")
    @Vertical(name = "PRO")
    @Test
    public void verifyRescheduleNudgesFunctionality() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyRescheduleNudgesFunctionality");
        BaseTest.paramsThreadLocal.set(param);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        RefundPage<WebDriver> refundPage = RefundPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        HotelReschedulePageWeb<?> hotelReschedulePageWeb = HotelReschedulePageWeb.getInstance(session);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String startDate = generateRandomDate(param.get("rangeAddDate"));
        String hotelName = param.get("hotelName");
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));
        int room = Integer.parseInt(param.get("room"));


        String[] salutationList = { "Mr", "Mrs", "Ms" };
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = salutationList[0];

        step("Create new hotel issued order");
        Boolean result = hotelApi.createIssuedOrderApi(
                username, password, startDate, hotelName, adult, night, room,
                orderFullName, orderNameTitle, guestFullName, guestTitle,"IDR");

        step("Get data from the issued order");
        String issuedOrderID = String.valueOf(hotelApi.getOrderId());
        Assertion.assertThat("Verify Booking via API is successful",result,is(true));

        step("Goto home");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink");
        homePage.loginWithSpecificAccountViaDeeplinkOnly(username, password);

        step("Open my order page");
        homePage.openMyOrderPage();

        step("Find the issued order");
        orderListPage.openActiveTicketViaDeeplink(issuedOrderID);

        step("Open refund menu");
        orderDetailPage.clickAndOpenRescheduleNudgeButton();

        step("verify reschedule nudge");
        Assertion.assertThat("is reschedule nudge",refundPage.verifyRescheduleNudge(),is(true));

        step("verify back btn");
        Assertion.assertThat("is back btn working",refundPage.verifyBackBtn(),is(true));

        step("verify refund btn");
        Assertion.assertThat("is refund btn working",refundPage.verifyRefundBtn(),is(true));

        step("click reschedule btn");
        refundPage.clickRescheduleBtnOnRescheduleNudge();

        step("verify reschedule page");
        webViewPage.switchToWebViewUrl("reschedule");
        Assertion.assertThat("is on reschedule page",hotelReschedulePageWeb.isInReschedulePage(),is(true));

        step("verify reschedule page back btn");
        hotelReschedulePageWeb.clickBackButton();
        webViewPage.switchToNative();
        Assertion.assertThat("is on reschedule nudge open",refundPage.verifyRescheduleNudge(), CoreMatchers.is(true));
    }

    @Author(names = {"Prerna"})
    @Module(name = "RefundReschedule")
    @Tribe(name = "Refund")
    @Vertical(name = "PRO")
    @Test
    public void testCompleteFlowFullERefund() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testCompleteFlowFullERefund");
        BaseTest.paramsThreadLocal.set(param);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        RefundPage<WebDriver> refundPage = RefundPage.getInstance(session);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String startDate = generateRandomDate(param.get("rangeAddDate"));
        String hotelName = param.get("hotelName");
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));
        int room = Integer.parseInt(param.get("room"));

        String[] salutationList = { "Mr", "Mrs", "Ms" };
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = salutationList[0];

        step("Create new hotel issued order");
        Boolean result = hotelApi.createIssuedOrderApi(
                username, password, startDate, hotelName, adult, night, room,
                orderFullName, orderNameTitle, guestFullName, guestTitle,"IDR");
        Assertion.assertThat("Hotel Booking via API is successful", result, is(true));

        step("Get data from the issued order");
        String issuedOrderID = String.valueOf(hotelApi.getOrderId());
        log(issuedOrderID);

        step("Goto home");
        homePage.gotoHome();

        step("Log out");
        loginPage.loginAsGuest();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Open my order page");
        homePage.openMyOrderPage();

        step("Find the issued order");
        orderListPage.openActiveTicketViaDeeplink(issuedOrderID);

        step("Open refund menu");
        orderDetailPage.clickRefundButton();

        step("Choose refund reason");
        refundPage.chooseRefundReasonWebView();

        step("Click process refund button");
        refundPage.clickProcessRefundBtn();

        step("Get the estimated refund on the refund process page");
        Double estimatedRefundProcess = refundPage.getTotalEstimatedRefundWebView();

        step("Submit refund");
        refundPage.submitRefund();

        step("check refund submit successfully");
        boolean isRefundSubmitSuccessfully = refundPage.checkRefundSubmission();
        Assertion.assertThat("Successfully submit the refund", isRefundSubmitSuccessfully, is(true));

        step("Go to the refund detail");
        refundPage.viewRefundDetailPage();

        step("Get the Refund ID on the refund details");
        String refundIdRefundDetails = refundPage.getRefundIdRefundDetails();

        step("Get the estimated refund on the refund detail page");
        Double estimatedRefundDetails = refundPage.getTotalEstimatedRefundWebView();
        Assertion.assertThat("Refund estimation on the refund detail should be equal with the process page", estimatedRefundDetails, is(estimatedRefundProcess));

        step("Open estimated refund details");
        refundPage.openEstimatedRefundDetail();
        String totalEstimatedRefund = refundPage.getTotalEstimatedRefundDetails();
        Assertion.assertThat("Refund estimation on the payment details should be equal on the refund detail page", Double.parseDouble(totalEstimatedRefund), is(estimatedRefundDetails));
        Assertion.assertThat("Refund details calculation should be equal the total payment details", refundPage.calculatePriceBreakdown(), is(convertPrice(totalEstimatedRefund)));
        refundPage.closeTheRefundPaymentDetails();

        step("Go back to the Your Orders page");
        refundPage.clickBackButton();
        orderListPage.openOngoingRefundTab();

        step("Find the refund card by refund ID: " + refundIdRefundDetails);
        Assertion.assertThat("Refund ID should be present on the ongoing refund list", orderListPage.findRefundCard(refundIdRefundDetails), is(true));

        step("Open the active tickets tab");
        orderListPage.openActiveTicketTab();

        step("Find the active order that on refund process");
        orderListPage.openActiveTicketViaDeeplink(issuedOrderID);
        orderDetailPage.verifyRefundStatusTitleHotel();
    }

    @Author(names = {"Zia"})
    @Module(name = "RefundReschedule")
    @Tribe(name = "Refund")
    @Vertical(name = "PRO")
    @Test
    public void testRefundSuccessPageB2C() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testRefundSuccessPageB2C");
        BaseTest.paramsThreadLocal.set(param);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        RefundPage<WebDriver> refundPage = RefundPage.getInstance(session);

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

        step("Create new hotel issued order");
        Boolean result = hotelApi.createIssuedOrderApi(
                username, password, startDate, hotelName, adult, night, room,
                orderFullName, orderNameTitle, guestFullName, guestTitle,"IDR");
        Assertion.assertThat("Hotel Booking via API is successful", result, is(true));

        step("Get data from the issued order");
        String issuedOrderID = String.valueOf(hotelApi.getOrderId());
        log(issuedOrderID);

        step("Goto home");
        homePage.gotoHome();

        step("User open app as login user " +username);
        String currentEmail = homePage.getCurrentEmail();

        if(!currentEmail.equals(username)){
            loginPage.loginAsGuest();
            loginPage.loginWith(loginType, username, password);
        }

        step("Get current language");
        String lang = homePage.getLanguageApp();

        step("Open my order page");
        homePage.openMyOrderPage();

        step("Find the issued order");
        orderListPage.openActiveTicketViaDeeplink(issuedOrderID);

        step("Open refund menu");
        orderDetailPage.clickRefundButton();

        step("Choose refund reason");
        refundPage.chooseRefundReasonWebView();

        step("Click process refund button");
        refundPage.clickProcessRefundBtn();

        step("Get the estimated refund on the refund process page");
        Double estimatedRefundProcess = refundPage.getTotalEstimatedRefundWebView();

        step("Submit refund");
        refundPage.submitRefund();

        step("check refund submit successfully");
        String refundDialogText = refundPage.getRefundDialogText();
        String expectedDialog = "Success submit the request";
        if(lang.equals("id")){
            expectedDialog = "Pengajuan refund berhasil";
        }
        Assertion.assertThat("Refund text is in correct language", refundDialogText, is(expectedDialog));

        step("Go to the refund detail");
        refundPage.viewRefundDetailPage();

        step("Get the Refund ID on the refund details");
        String refundIdRefundDetails = refundPage.getRefundIdRefundDetails();
        Assertion.assertThat("Refund ID should be present on the refund detail page", refundIdRefundDetails, is(notNullValue()));

        step("Get the estimated refund on the refund detail page");
        Double estimatedRefundDetails = refundPage.getTotalEstimatedRefundWebView();
        Assertion.assertThat("Refund estimation on the refund detail should be equal with the process page", estimatedRefundDetails, is(estimatedRefundProcess));
        }

    @Author(names = {"Zia"})
    @Module(name = "RefundReschedule")
    @Tribe(name = "Refund")
    @Vertical(name = "PRO")
    @Test
    public void testRefundSuccessPageB2B() {
        Session<WebDriver> session = sessionThreadLocal.get();

        Map<String, String> param = getParam("testRefundSuccessPageB2B");
        BaseTest.paramsThreadLocal.set(param);

        String username = param.get("travelerUsername");
        String password = param.get("travelerPassword");
        String startDate = generateRandomDate(param.get("rangeAddDate"));
        String approverUsername = param.get("approverUsername");
        String approverPassword = param.get("approverPassword");

        String query = param.get("hotelName");
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));
        int room = Integer.parseInt(param.get("room"));

        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = getRandomObject(salutationList);

        HotelApi<?> hotelApi = new HotelApi<>(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        RefundPage<WebDriver> refundPage = RefundPage.getInstance(session);
        CorporateLoginPage<WebDriver> corporateLoginPage = CorporateLoginPage.getInstance(session);

        String orderID;
        step("Create new hotel issued order");
        orderID = hotelApi.createIssuedOrderApiB2B(
                username, password, startDate, query, adult, night, room, "IDR",
                orderFullName, orderNameTitle, guestFullName, guestTitle, corporateApi.getEmployeeData(username), false);
        Assertion.assertThat("Hotel Booking via API is successful for B2B account", !orderID.isEmpty(), is(true));
        sleep(5);

        step("Approve booking by approver account for Order ID: " + orderID);
        Boolean bookingStatus = corporateApi.approveBookingB2BApi(approverUsername, approverPassword, orderID);
        Assertion.assertThat("Booking approved by B2B dashboard", bookingStatus, is(true));

        step("Goto home");
        homePage.gotoHome();

        step("User open app as login user " + username);
        String currentEmail = homePage.getCurrentEmail();

        if (!currentEmail.equals(username)) {
            loginPage.loginAsGuest();

            step("B2B login");
            corporateLoginPage.corporateLogin(username, password);
        }

        step("Get current language");
        String lang = homePage.getLanguageApp();

        step("Find the issued order");
        homePage.openMyOrderPage();
        orderListPage.openIssuedTicketViaDeeplink(orderID);

        step("Skip popup");
        

        step("Open refund menu");
        orderDetailPage.clickRefundButton();

        step("Choose refund reason");
        refundPage.chooseRefundReasonWebView();

        step("Click process refund button");
        refundPage.clickProcessRefundBtn();

        step("Submit refund");
        refundPage.submitRefund();

        step("check refund submit successfully");
        String refundDialogText = refundPage.getRefundDialogText();
        String expectedDialog = "Successfully submit the refund";
        if (lang.equals("id")) {
            expectedDialog = "Berhasil mengajukan refund";
        }
        Assertion.assertThat("Refund text is in correct language", refundDialogText, is(expectedDialog));

        step("go to order list");
        refundPage.clickGoToOrderListBtnOnRefundPage();

        step("Verify redirected to order list");
        Assertion.assertThat("refund details webview closed, user directed to active order list", orderListPage.isOrderListPage(), is(true));
    }
    }