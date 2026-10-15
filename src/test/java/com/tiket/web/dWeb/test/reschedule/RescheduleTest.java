package com.tiket.web.dWeb.test.reschedule;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.apiPage.CorporateApi;
import com.tiket.apiPage.Unm;
import com.tiket.model.Session;
import com.tiket.pojo.order.MapVerticalWithOrderId;
import com.tiket.pojo.unm.LoginDataUnmResponse;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.web.dWeb.page.account.AccountPage;
import com.tiket.web.page.order.OrderDetail.OrderDetailWeb;
import com.tiket.web.page.order.OrderList.OrderListWeb;
import com.tiket.web.dWeb.page.reschedule.ReschedulePage;
import com.tiket.web.dashboard.page.order.orderCreationPage;
import com.tiket.web.page.common.home.HomePageWeb;
import com.tiket.web.page.common.home.LoginPageWeb;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import javax.security.auth.login.FailedLoginException;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static com.tiket.core.CoreFunctions.*;
import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.core.Is.is;

public class RescheduleTest extends BaseTest {
    @Author(names = {"Amit Bhardwaj"})
    @Module(name = "Reschedule")
    @Tribe(name = "Refund")
    @Vertical(name = "PRO")
    @Test
    public void testRescheduleAdditionalPrice() throws FailedLoginException, InterruptedException, ExecutionException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testRescheduleAdditionalPrice");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        AccountPage<?> accountPage = new AccountPage<>(session);
        orderCreationPage<?> orderCreationPage = new orderCreationPage<>(session);
        ReschedulePage<?> reschedulePage = new ReschedulePage<>(session);
        Unm<?> unm = new Unm<>(session);
        Map<String, String> map = getParam("testRescheduleAdditionalPrice");
        final String username = map.get("username");
        String email = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));

        String hotelName = param.get("hotelName");
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));
        int room = Integer.parseInt(param.get("room"));
        String startDate = generateRandomDate(param.get("rangeAddDate"));
        String currency = param.get("currency");
        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = getRandomObject(salutationList);

        step("Login as " + email + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(email, password);

        step("Get api access token");
        LoginDataUnmResponse.Data authData = unm.unmLoginApi(username, password);
        String apiAccessToken = authData.accessToken;
        String accountId = String.valueOf(authData.accountId);

        step("Open my order page");
        homePage.goToMyOrderPage();

        step("Check order present in order list");
        MapVerticalWithOrderId.TitleAndOrderId activeOrderData = orderListPage
                .findFirstOrderFilterByVertical(
                        apiAccessToken,
                        email,
                        accountId,
                        "hotel",
                        currency,
                        true,
                        true,
                        com.tiket.page.order.orderlist.OrderListPage.OrderStatus.ETICKET_ISSUED);

        step("Fetch hotel order with issued status, else create order");
        String bookedOrderID = orderListPage.getHotelBookedOrderId(
                orderCreationPage,
                activeOrderData,
                email,
                password,
                startDate,
                hotelName,
                adult,
                night,
                room,
                orderFullName,
                orderNameTitle,
                guestFullName,
                guestTitle
        );

        step("Get internal data for the issued order");
        String hotelNameData = orderDetailPage.getVerticalData(bookedOrderID).get("orderName");
        String roomNameData = orderDetailPage.getVerticalData(bookedOrderID).get("roomName");
        int roomCountData = Integer.parseInt(orderDetailPage.getVerticalData(bookedOrderID).get("bookingRoom"));
        int nightDurationData = Integer.parseInt(orderDetailPage.getVerticalData(bookedOrderID).get("bookingNight"));
        String orderId = orderDetailPage.getVerticalData(bookedOrderID).get("orderId");

        step("Find the issued order");
        orderListPage.openActiveTicketViaURL(bookedOrderID);
        orderDetailPage.skipCoachmark();

        step("Verify hotel order details");
        Assertion.assertThat("Order ID is displayed correctly", orderDetailPage.getOrderId(),
                is(bookedOrderID));
        Assertion.assertThat("E-ticket card Displayed", orderDetailPage.isETicketCardDisplayed(),
                is(true));
        Assertion.assertThat("Hotel Name is displayed correctly",
                orderDetailPage.isHotelNameDisplayed(hotelNameData), is(true));
        Assertion.assertThat("Total night is equal", orderDetailPage.getTotalNight(),
                is((nightDurationData)));
        Assertion.assertThat("Total room is equal", orderDetailPage.getTotalRoom(),
                is(roomCountData));
        Assertion.assertThat("Room Name is displayed correctly",
                orderDetailPage.roomNameDisplayed(), is(roomNameData));
        Assertion.assertThat("Order ID is displayed correctly",
                orderDetailPage.getOrderId(), is(orderId));

        step("Click reschedule status");
        reschedulePage.checkRescheduleableStatus(username);

        step("Click check availability button");
        reschedulePage.clickCheckAvailabilityButton();

        step("Click select room with additional payment");
        reschedulePage.selectRoomWithAdditionalPayment();

        step("Click Book Button button");
        reschedulePage.clickBookButton();

        step("Click checkTnC button");
        reschedulePage.checkTnC();

        step("Click submit reschedule button");
        reschedulePage.clickSubmitRescheduleButton();

        step("Click see all button");
        reschedulePage.clickSeeAll();

        step("Click select payment method");
        reschedulePage.clickSelectPaymentMethod();

        step("Click pay with Sakuku button");
        reschedulePage.clickPayWithSakuku();

        step("New order is issued and have old order linked in order detail");
        reschedulePage.verifyHotelStatus();
        reschedulePage.getNewOrderText();
        reschedulePage.verifyOrderIds();
        reschedulePage.compareOrderIds();

        step("Verify reschedule page currency is same with old order currency");
        reschedulePage.getOrderCurrency();
        reschedulePage.compareCurrencies();

        step("Verify Total and breakdown calculation are matched");
        reschedulePage.comparePrices();

        step("Verify Old order having section that there is order detail/pax already rescheduled");
        reschedulePage.rescheduleCompleted();

        step("Verify On refund list there will be no refund created");
        reschedulePage.refundNotInitiated();
    }

    @Author(names = {"Amit Bhardwaj"})
    @Module(name = "Reschedule")
    @Tribe(name = "Refund")
    @Vertical(name = "PRO")
    @Test
    public void testRescheduleSamePrice() throws FailedLoginException, InterruptedException, ExecutionException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testRescheduleSamePrice");
        BaseTest.paramsThreadLocal.set(param);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        AccountPage<?> accountPage = new AccountPage<>(session);
        orderCreationPage<?> orderCreationPage = new orderCreationPage<>(session);
        ReschedulePage<?> reschedulePage = new ReschedulePage<>(session);
        Map<String, String> map = getParam("testRescheduleSamePrice");
        final String username = map.get("username");
        String email = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));

        String hotelName = param.get("hotelName");
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));
        int room = Integer.parseInt(param.get("room"));
        String startDate = generateRandomDate(param.get("rangeAddDate"));
        String currency = param.get("currency");
        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = getRandomObject(salutationList);

        step("Login as " + username + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(username, password);

        step("Get api access token");
        String apiAccessToken = accountPage.getAccessTokenUNM(username, password);
        String apiAccessTokenBearer = "Bearer " + apiAccessToken;
        String accountId = String.valueOf(corporateApi.getAccountId(apiAccessToken));

        step("Open my order page");
        homePage.goToMyOrderPage();

        step("Check order present in order list");
        MapVerticalWithOrderId.TitleAndOrderId activeOrderData = orderListPage.findFirstOrderFilterByVertical(apiAccessTokenBearer, username, accountId, "hotel", "", true, true, com.tiket.page.order.orderlist.OrderListPage.OrderStatus.ETICKET_ISSUED);

        step("Create order");
        String bookedOrderID = orderListPage.getHotelBookedOrderId(
                orderCreationPage,
                activeOrderData,
                email,
                password,
                startDate,
                hotelName,
                adult,
                night,
                room,
                orderFullName,
                orderNameTitle,
                guestFullName,
                guestTitle
        );

        step("Get internal data for the issued order");
        String hotelNameData = orderDetailPage.getVerticalData(bookedOrderID).get("orderName");

        step("Find the issued order");
        orderListPage.openActiveTicketViaURL(bookedOrderID);
        orderDetailPage.skipCoachmark();

        step("Verify hotel order details");
        Assertion.assertThat("Order ID is displayed correctly", orderDetailPage.getOrderId(),
                is(bookedOrderID));
        Assertion.assertThat("E-ticket card Displayed", orderDetailPage.isETicketCardDisplayed(),
                is(true));
        Assertion.assertThat("Hotel Name is displayed correctly",
                orderDetailPage.isHotelNameDisplayed(hotelNameData), is(true));

        step("Click reschedule status");
        reschedulePage.checkRescheduleableStatus(username);

        step("Click check availability button");
        reschedulePage.clickCheckAvailabilityButton();

        step("Click select room with same amount");
        reschedulePage.clickSelectRoomWithSameAmount();

        step("Click Book button");
        reschedulePage.clickBookButton();

        step("Click checkTnC button");
        reschedulePage.checkTnC();

        step("Click submit reschedule button");
        reschedulePage.clickSubmitRescheduleButton();

        step("Click pay with Refund deposit button");
        reschedulePage.clickPayWithRefundDeposit();

        step("Click submit refund request");
        reschedulePage.clickSubmitRefundRequest();
        reschedulePage.verifyHotelStatus();

        step("New order is issued and have old order linked in order detail");
        reschedulePage.verifyHotelStatus();
        reschedulePage.getNewOrderText();
        reschedulePage.verifyOrderIds();
        reschedulePage.compareOrderIds();

        step("Verify reschedule page currency is same with old order currency");
        reschedulePage.getOrderCurrency();
        reschedulePage.compareCurrencies();

        step("Verify Total and breakdown calculation are matched");
        reschedulePage.comparePrices();

        step("Verify Old order having section that there is order detail/pax already rescheduled");
        reschedulePage.rescheduleCompleted();

        step("Verify On refund list there will be no refund created");
        reschedulePage.refundNotInitiated();
    }
}