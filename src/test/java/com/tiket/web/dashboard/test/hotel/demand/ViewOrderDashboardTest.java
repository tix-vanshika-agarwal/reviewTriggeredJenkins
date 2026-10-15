package com.tiket.web.dashboard.test.hotel.demand;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.web.dWeb.page.accommodation.hotels.HotelDetailPage;
import com.tiket.web.dWeb.page.payment.PaymentPage;
import com.tiket.web.dashboard.page.common.LoginMemberAdmin;
import com.tiket.web.dashboard.page.hotel.demand.ViewOrderDashboardPage;
import com.tiket.web.dashboard.page.order.orderCreationPage;
import com.tiket.web.page.accommodation.hotels.BookingForm.HotelBookingFormPageWeb;
import com.tiket.web.page.accommodation.hotels.hotelLandingPage.HotelLandingPageWeb;
import com.tiket.web.page.accommodation.hotels.hotelRoomListPage.HotelRoomListPageWeb;
import com.tiket.web.page.common.home.HomePageWeb;
import com.tiket.web.page.common.home.LoginPageWeb;
import com.tiket.web.page.order.OrderDetail.OrderDetailWeb;
import com.tiket.web.page.order.OrderList.OrderListWeb;
import org.testng.annotations.Test;

import javax.security.auth.login.FailedLoginException;
import java.util.Map;

import static com.tiket.core.CoreFunctions.*;
import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;


public class ViewOrderDashboardTest extends BaseTest {
    @Author(names = {"Fajrul Alam"})
    @Module(name = "ViewOrder")
    @Tribe(name = "Demand")
    @Vertical(name = "Accommodation")
    @Test
    public void testViewOrderValidOrderIdNoSource() {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        ViewOrderDashboardPage<?> viewOrderDashboardPage = new ViewOrderDashboardPage<>(session);
        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<>(session);
        Map<String, String> param = getParam("testViewOrderValidOrderIdNoSource");
        BaseTest.paramsThreadLocal.set(param);

        String email = param.get("email");
        String password = param.get("password");
        String url = param.get("url");
        String orderId = param.get("orderId");

        step("Go to member login page");
        loginMemberAdmin.gotoHome();

        step("Log out");
        boolean isLoggedInMemberAdmin = loginMemberAdmin.isUserLoggedIn(email, password);

        step("Assert that user is logged in to Member Admin");
        Assertion.assertThat("User is logged in", isLoggedInMemberAdmin, is(true));

        step("Go to View Order Dashboard");
        loginMemberAdmin.goToDashboard(url);

        step("Input Order Id");
        viewOrderDashboardPage.setInputIdField(orderId);

        step("Click Search");
        viewOrderDashboardPage.clickSearch();

        step("Assert that order table is not shown");
        Assertion.assertThat("Order table is not shown", viewOrderDashboardPage.isOrderTableShown(), is(false));
    }

    @Author(names = {"Fajrul Alam"})
    @Module(name = "ViewOrder")
    @Tribe(name = "Demand")
    @Vertical(name = "Accommodation")
    @Test
    public void testViewOrderClickDropdownSources() {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        ViewOrderDashboardPage<?> viewOrderDashboardPage = new ViewOrderDashboardPage<>(session);
        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<>(session);
        Map<String, String> param = getParam("testViewOrderClickDropdownSources");
        BaseTest.paramsThreadLocal.set(param);

        String email = param.get("email");
        String password = param.get("password");
        String url = param.get("url");
        String orderId = param.get("orderId");

        step("Login via cookie injection");
        loginMemberAdmin.loginViaCookie(email, password);

        step("Go to View Order Dashboard");
        loginMemberAdmin.goToDashboard(url);

        step("Input Order Id");
        viewOrderDashboardPage.setInputIdField(orderId);

        step("Click Search");
        viewOrderDashboardPage.clickSearch();

        step("Click Source Dropdown");
        viewOrderDashboardPage.clickSourceDropdown();

        step("Select Source Order ID");
        viewOrderDashboardPage.selectSourceOrderID();
        Assertion.assertThat("Source Order ID is selected", viewOrderDashboardPage.checkSelectedSource("Order ID"), is(true));

        step("Select Source Itinerary ID");
        viewOrderDashboardPage.selectSourceItineraryID();
        Assertion.assertThat("Source Itinerary ID is selected", viewOrderDashboardPage.checkSelectedSource("Itinerary ID"), is(true));

        step("Select Source Vendor Itinerary ID");
        viewOrderDashboardPage.selectSourceVendorItineraryID();
        Assertion.assertThat("Source Vendor Itinerary ID is selected", viewOrderDashboardPage.checkSelectedSource("Vendor Itinerary ID"), is(true));
    }

    @Author(names = {"Ketan Singh"})
    @Module(name = "ViewOrder")
    @Tribe(name = "Demand")
    @Vertical(name = "Accommodation")
    @Test
    public void testFilterUsingOrderId() {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        ViewOrderDashboardPage<?> viewOrderDashboardPage = new ViewOrderDashboardPage<>(session);
        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<>(session);
        Map<String, String> param = getParam("testFilterUsingOrderId");
        BaseTest.paramsThreadLocal.set(param);

        String email = param.get("email");
        String password = param.get("password");
        String url = param.get("url");
        String orderId = param.get("orderId");

        step("Login via cookie injection");
        loginMemberAdmin.loginViaCookie(email, password);

        step("Go to View Order Dashboard");
        loginMemberAdmin.goToDashboard(url);
        Assertion.assertThat("Is Search Order Detail Box Loaded", viewOrderDashboardPage.isSearchOrderDetailBoxLoaded(), is(true));

        step("Click Source Dropdown");
        viewOrderDashboardPage.clickSourceDropdown();

        step("Select Source Order ID");
        viewOrderDashboardPage.selectSourceOrderID();
        Assertion.assertThat("Source Order ID is selected", viewOrderDashboardPage.checkSelectedSource("Order ID"), is(true));

        step("Input Order Id");
        viewOrderDashboardPage.setOrderIdInput(orderId);

        step("Click Search");
        viewOrderDashboardPage.clickSearch();

        step("Every Order Details Should be Visible");
        Assertion.assertThat("Order Detail Box is Loaded", viewOrderDashboardPage.isOrderDetailBoxLoaded(), is(true));
        Assertion.assertThat("Order Id is Present", viewOrderDashboardPage.getOrderId(), containsString(orderId));
        Assertion.assertThat("Itinerary Id is Present", viewOrderDashboardPage.isItineraryIdLoaded(), is(true));
        Assertion.assertThat("Date purchased is Present", viewOrderDashboardPage.isDatePurchasedLoaded(), is(true));
        Assertion.assertThat("Product Type is Present", viewOrderDashboardPage.isProductTypeLoaded(), is(true));
        Assertion.assertThat("Order Detail is Present", viewOrderDashboardPage.isOrderDetailLoaded(), is(true));
        Assertion.assertThat("Passenger Detail is Present", viewOrderDashboardPage.isPassengerDetailLoaded(), is(true));
        Assertion.assertThat("Order Status is Present", viewOrderDashboardPage.isOrderStatusLoaded(), is(true));
        Assertion.assertThat("Action column is Present", viewOrderDashboardPage.isActionPaneLoaded(), is(true));
    }

    @Author(names = {"Ketan Singh"})
    @Module(name = "ViewOrder")
    @Tribe(name = "Demand")
    @Vertical(name = "Accommodation")
    @Test
    public void testOrderStatusForSuccessBooking() {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        ViewOrderDashboardPage<?> viewOrderDashboardPage = new ViewOrderDashboardPage<>(session);
        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<>(session);
        orderCreationPage<?> orderCreationPage = new orderCreationPage<>(session);
        Map<String, String> param = getParam("testOrderStatusForSuccessBooking");
        BaseTest.paramsThreadLocal.set(param);

        String email = param.get("email");
        String password = param.get("password");
        String url = param.get("url");

        String username = "muhammad.yazid@tiket.com";
        String hotelpassword = "TestTiket@123";
        String startDate = generateRandomDate("24-30");
        String hotelName = "Carvens Hotel";
        int adult = 1;
        int night = 1;
        int room = 1;
        String currency = "IDR";
        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autoguest";
        String guestTitle = salutationList[0];

        step("Create new hotel issued order");
        orderCreationPage.createHotelSakukuIssuedOrderApi(username, hotelpassword, startDate, hotelName,
                adult, night, room, orderFullName, orderNameTitle, guestFullName, guestTitle, currency);

        step("Get data from the issued order");
        String bookedOrderID = String.valueOf(orderCreationPage.getOrderId());

        step("Login via cookie injection");
        loginMemberAdmin.loginViaCookie(email, password);

        step("Go to View Order Dashboard");
        loginMemberAdmin.goToDashboard(url);
        Assertion.assertThat("Is Search Order Detail Box Loaded", viewOrderDashboardPage.isSearchOrderDetailBoxLoaded(), is(true));

        step("Click Source Dropdown");
        viewOrderDashboardPage.clickSourceDropdown();

        step("Select Source Order ID");
        viewOrderDashboardPage.selectSourceOrderID();
        Assertion.assertThat("Source Order ID is selected", viewOrderDashboardPage.checkSelectedSource("Order ID"), is(true));

        step("Input Order Id");
        viewOrderDashboardPage.setOrderIdInput(bookedOrderID);

        step("Click Search");
        viewOrderDashboardPage.clickSearch();

        step("Order Status Should be eticket issued");
        Assertion.assertThat("Order Detail Box is Loaded", viewOrderDashboardPage.isOrderDetailBoxLoaded(), is(true));
        Assertion.assertThat("Order Id is Present", viewOrderDashboardPage.getOrderId(), containsString(bookedOrderID));
        Assertion.assertThat("Order Status is E-Ticket Issued", viewOrderDashboardPage.getSuccessOrderStatus(), containsString("E-ticket issued"));
    }

    @Author(names = {"Ketan Singh"})
    @Module(name = "ViewOrder")
    @Tribe(name = "Demand")
    @Vertical(name = "Accommodation")
    @Test
    public void testOrderStatusForFailedBooking() {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        ViewOrderDashboardPage<?> viewOrderDashboardPage = new ViewOrderDashboardPage<>(session);
        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<>(session);
        Map<String, String> param = getParam("testOrderStatusForFailedBooking");
        BaseTest.paramsThreadLocal.set(param);

        String email = param.get("email");
        String password = param.get("password");
        String url = param.get("url");
        String orderId = param.get("orderId");

        step("Login via cookie injection");
        loginMemberAdmin.loginViaCookie(email, password);

        step("Go to View Order Dashboard");
        loginMemberAdmin.goToDashboard(url);
        Assertion.assertThat("Is Search Order Detail Box Loaded", viewOrderDashboardPage.isSearchOrderDetailBoxLoaded(), is(true));

        step("Click Source Dropdown");
        viewOrderDashboardPage.clickSourceDropdown();

        step("Select Source Order ID");
        viewOrderDashboardPage.selectSourceOrderID();
        Assertion.assertThat("Source Order ID is selected", viewOrderDashboardPage.checkSelectedSource("Order ID"), is(true));

        step("Input Order Id");
        viewOrderDashboardPage.setOrderIdInput(orderId);

        step("Click Search");
        viewOrderDashboardPage.clickSearch();

        step("Order Status should be Booking unsuccessful");
        Assertion.assertThat("Order Detail Box is Loaded", viewOrderDashboardPage.isOrderDetailBoxLoaded(), is(true));
        Assertion.assertThat("Order Id is Present", viewOrderDashboardPage.getOrderId(), containsString(orderId));
        Assertion.assertThat("Order Status is Booking Unsuccessful", viewOrderDashboardPage.getFailedOrderStaus(), containsString("Booking unsuccessful"));
    }

    @Author(names = {"Aldy Heryana", "Vipul Kumar"})
    @Module(name = "ViewOrder")
    @Tribe(name = "Demand")
    @Vertical(name = "Accommodation")
    @Test
    public void testGuestNameFieldEditable() {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        ViewOrderDashboardPage<?> viewOrderDashboardPage = new ViewOrderDashboardPage<>(session);
        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<>(session);
        orderCreationPage<?> orderCreationPage = new orderCreationPage<>(session);
        Map<String, String> param = getParam("testGuestNameFieldEditable");
        BaseTest.paramsThreadLocal.set(param);

        String email = param.get("email");
        String password = param.get("password");
        String url = param.get("url");
        String guestName = param.get("guestName");

        String hotelApiUsername = param.get("hotelApiUsername");
        String hotelApiPassword = param.get("hotelApiPassword");
        String hotelName = param.get("hotelName");
        String currencyParam = param.get("currency");
        String currency = (currencyParam == null || currencyParam.isBlank()) ? "IDR" : currencyParam.trim();

        String startDate = generateRandomDate("24-30");
        int adult = 1;
        int night = 1;
        int room = 1;
        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autoguest";
        String guestTitle = salutationList[0];

        step("Create new hotel issued order");
        orderCreationPage.createHotelSakukuIssuedOrderApi(hotelApiUsername, hotelApiPassword, startDate, hotelName,
                adult, night, room, orderFullName, orderNameTitle, guestFullName, guestTitle, currency);

        step("Get data from the issued order");
        String bookedOrderID = String.valueOf(orderCreationPage.getOrderId());

        step("Login via cookie injection");
        loginMemberAdmin.loginViaCookie(email, password);

        step("Go to View Order Dashboard");
        loginMemberAdmin.goToDashboard(url);

        step("Click Source Dropdown");
        viewOrderDashboardPage.clickSourceDropdown();

        step("Click source OrderId");
        viewOrderDashboardPage.selectSourceOrderID();

        step("Input source OrderId");
        viewOrderDashboardPage.SetInputOrderId(bookedOrderID);

        step("Click Search");
        viewOrderDashboardPage.clickSearch();

        step("Order Status should be Finished");
        Assertion.assertThat("OrderId Detail Box is Loaded", viewOrderDashboardPage.isOrderDetailBoxLoaded(), is(true));
        Assertion.assertThat("OrderId is Present", viewOrderDashboardPage.getOrderId(), containsString(bookedOrderID));

        step("Click More");
        viewOrderDashboardPage.clickMoreAction();

        step("Click Edit Booking");
        viewOrderDashboardPage.clickEditBooking();
        step("Open Edit Booking Form Page");
        Assertion.assertThat("Edit Booking Page is Visible", viewOrderDashboardPage.isEditBookingPageLoaded(), is(true));

        step("scroll");
        viewOrderDashboardPage.scrollEditBooking();

        step("Input guest name");
        viewOrderDashboardPage.guestNameField(guestName);

        Assertion.assertThat("guest name field is editable", viewOrderDashboardPage.checkGuestInput(guestName), is(true));

    }

    @Author(names = {"Ketan Singh"})
    @Module(name = "ViewOrder")
    @Tribe(name = "Demand")
    @Vertical(name = "Accommodation")
    @Test
    public void testClickableActionButton() {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        ViewOrderDashboardPage<?> viewOrderDashboardPage = new ViewOrderDashboardPage<>(session);
        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<>(session);
        orderCreationPage<?> orderCreationPage = new orderCreationPage<>(session);
        Map<String, String> param = getParam("testClickableActionButton");
        BaseTest.paramsThreadLocal.set(param);

        String email = param.get("email");
        String password = param.get("password");
        String url = param.get("url");

        String username = "muhammad.yazid@tiket.com";
        String hotelpassword = "TestTiket@123";
        String startDate = generateRandomDate("24-30");
        String hotelName = "Carvens Hotel";
        int adult = 1;
        int night = 1;
        int room = 1;
        String currency = "IDR";
        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autoguest";
        String guestTitle = salutationList[0];

        step("Create new hotel issued order");
        orderCreationPage.createHotelSakukuIssuedOrderApi(username, hotelpassword, startDate, hotelName,
                adult, night, room, orderFullName, orderNameTitle, guestFullName, guestTitle, currency);

        step("Get data from the issued order");
        String bookedOrderID = String.valueOf(orderCreationPage.getOrderId());

        step("Login via cookie injection");
        loginMemberAdmin.loginViaCookie(email, password);

        step("Go to View Order Dashboard");
        loginMemberAdmin.goToDashboard(url);
        Assertion.assertThat("Is Search Order Detail Box Loaded", viewOrderDashboardPage.isSearchOrderDetailBoxLoaded(), is(true));

        step("Click Source Dropdown");
        viewOrderDashboardPage.clickSourceDropdown();

        step("Select Source Order ID");
        viewOrderDashboardPage.selectSourceOrderID();
        Assertion.assertThat("Source Order ID is selected", viewOrderDashboardPage.checkSelectedSource("Order ID"), is(true));

        step("Input Order Id");
        viewOrderDashboardPage.setOrderIdInput(bookedOrderID);

        step("Click Search");
        viewOrderDashboardPage.clickSearch();

        step("Action Button Should Be Shown and Clickable");
        Assertion.assertThat("Order Detail Box is Loaded", viewOrderDashboardPage.isOrderDetailBoxLoaded(), is(true));
        Assertion.assertThat("Order Id is Present", viewOrderDashboardPage.getOrderId(), containsString(bookedOrderID));
        Assertion.assertThat("Action column is Present", viewOrderDashboardPage.isActionPaneLoaded(), is(true));
        Assertion.assertThat("More Action Button is Clickable", viewOrderDashboardPage.isActionButtonClickable(), is(true));
    }

    @Author(names = {"Ketan Singh"})
    @Module(name = "ViewOrder")
    @Tribe(name = "Demand")
    @Vertical(name = "Accommodation")
    @Test
    public void testActionItemsShownForSuccessfulOrder() {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        ViewOrderDashboardPage<?> viewOrderDashboardPage = new ViewOrderDashboardPage<>(session);
        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<>(session);
        orderCreationPage<?> orderCreationPage = new orderCreationPage<>(session);
        Map<String, String> param = getParam("testActionItemsShownForSuccessfulOrder");
        BaseTest.paramsThreadLocal.set(param);

        String email = param.get("email");
        String password = param.get("password");
        String url = param.get("url");

        String username = "muhammad.yazid@tiket.com";
        String hotelpassword = "TestTiket@123";
        String startDate = generateRandomDate("24-30");
        String hotelName = "Carvens Hotel";
        int adult = 1;
        int night = 1;
        int room = 1;
        String currency = "IDR";
        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autoguest";
        String guestTitle = salutationList[0];

        step("Create new hotel issued order");
        orderCreationPage.createHotelSakukuIssuedOrderApi(username, hotelpassword, startDate, hotelName,
                adult, night, room, orderFullName, orderNameTitle, guestFullName, guestTitle, currency);

        step("Get data from the issued order");
        String bookedOrderID = String.valueOf(orderCreationPage.getOrderId());

        step("Login via cookie injection");
        loginMemberAdmin.loginViaCookie(email, password);

        step("Go to View Order Dashboard");
        loginMemberAdmin.goToDashboard(url);
        Assertion.assertThat("Is Search Order Detail Box Loaded", viewOrderDashboardPage.isSearchOrderDetailBoxLoaded(), is(true));

        step("Click Source Dropdown");
        viewOrderDashboardPage.clickSourceDropdown();

        step("Select Source Order ID");
        viewOrderDashboardPage.selectSourceOrderID();
        Assertion.assertThat("Source Order ID is selected", viewOrderDashboardPage.checkSelectedSource("Order ID"), is(true));

        step("Input Order Id");
        viewOrderDashboardPage.setOrderIdInput(bookedOrderID);

        step("Click Search");
        viewOrderDashboardPage.clickSearch();

        step("Action Button Should Be Shown With Its Items");
        Assertion.assertThat("Order Detail Box is Loaded", viewOrderDashboardPage.isOrderDetailBoxLoaded(), is(true));
        Assertion.assertThat("Order Id is Present", viewOrderDashboardPage.getOrderId(), containsString(bookedOrderID));
        Assertion.assertThat("Action column is Present", viewOrderDashboardPage.isActionPaneLoaded(), is(true));
        viewOrderDashboardPage.clickMoreActionButton();
        Assertion.assertThat("More Action Box is Loaded", viewOrderDashboardPage.isMoreActionBoxLoaded(), is(true));
        Assertion.assertThat("Edit Booking is visible", viewOrderDashboardPage.isEditBookingVisible(), containsString("Edit Booking"));
        Assertion.assertThat("Report Failed Checkin is Visible", viewOrderDashboardPage.isReportFailedCheckinVisible(), containsString("Report Failed Checkin"));
        Assertion.assertThat("Cancel Booking is visible", viewOrderDashboardPage.isCancelBookingVisible(), containsString("Cancel Booking"));
    }

    @Author(names = {"Ketan Singh"})
    @Module(name = "ViewOrder")
    @Tribe(name = "Demand")
    @Vertical(name = "Accommodation")
    @Test
    public void testCheckGuestDetails() {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        ViewOrderDashboardPage<?> viewOrderDashboardPage = new ViewOrderDashboardPage<>(session);
        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<>(session);
        orderCreationPage<?> orderCreationPage = new orderCreationPage<>(session);
        Map<String, String> param = getParam("testCheckGuestDetails");
        BaseTest.paramsThreadLocal.set(param);

        String email = param.get("email");
        String password = param.get("password");
        String url = param.get("url");
        int adultno = Integer.parseInt(param.get("adultno"));

        String username = "muhammad.yazid@tiket.com";
        String hotelpassword = "TestTiket@123";
        String startDate = generateRandomDate("24-30");
        String hotelName = "Carvens Hotel";
        int adult = adultno;
        int night = 1;
        int room = adultno;
        String currency = "IDR";
        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autoguest";
        String guestTitle = salutationList[0];

        step("Create new hotel issued order");
        orderCreationPage.createHotelSakukuIssuedOrderApi(username, hotelpassword, startDate, hotelName,
                adult, night, room, orderFullName, orderNameTitle, guestFullName, guestTitle, currency);

        step("Get data from the issued order");
        String bookedOrderID = String.valueOf(orderCreationPage.getOrderId());

        step("Login via cookie injection");
        loginMemberAdmin.loginViaCookie(email, password);

        step("Go to View Order Dashboard");
        loginMemberAdmin.goToDashboard(url);
        Assertion.assertThat("Is Search Order Detail Box Loaded", viewOrderDashboardPage.isSearchOrderDetailBoxLoaded(), is(true));

        step("Click Source Dropdown");
        viewOrderDashboardPage.clickSourceDropdown();

        step("Select Source Order ID");
        viewOrderDashboardPage.selectSourceOrderID();
        Assertion.assertThat("Source Order ID is selected", viewOrderDashboardPage.checkSelectedSource("Order ID"), is(true));

        step("Input Order Id");
        viewOrderDashboardPage.setOrderIdInput(bookedOrderID);

        step("Click Search");
        viewOrderDashboardPage.clickSearch();

        step("Open Edit Booking Page");
        Assertion.assertThat("Action column is Present", viewOrderDashboardPage.isActionPaneLoaded(), is(true));
        viewOrderDashboardPage.clickMoreActionButton();
        Assertion.assertThat("More Action Box is Loaded", viewOrderDashboardPage.isMoreActionBoxLoaded(), is(true));
        Assertion.assertThat("Edit Booking is visible", viewOrderDashboardPage.isEditBookingVisible(), containsString("Edit Booking"));
        viewOrderDashboardPage.clickEditBookingButton();
        Assertion.assertThat("Edit Booking Page is Visible", viewOrderDashboardPage.isEditBookingPageLoaded(), is(true));

        step("Check Guest Details");
        Assertion.assertThat("Guest Details Section is Visible", viewOrderDashboardPage.isGuestDetailsSectionLoaded(), is(true));
        viewOrderDashboardPage.isAllGuestDetailsLoaded(adult);
    }

    @Author(names = {"Ketan Singh"})
    @Module(name = "ViewOrder")
    @Tribe(name = "Demand")
    @Vertical(name = "Accommodation")
    @Test
    public void testClickableSubmitButtonInGuestDetails() {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        ViewOrderDashboardPage<?> viewOrderDashboardPage = new ViewOrderDashboardPage<>(session);
        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<>(session);
        orderCreationPage<?> orderCreationPage = new orderCreationPage<>(session);
        Map<String, String> param = getParam("testClickableSubmitButtonInGuestDetails");
        BaseTest.paramsThreadLocal.set(param);

        String email = param.get("email");
        String password = param.get("password");
        String url = param.get("url");
        int guests = Integer.parseInt(param.get("guests"));

        String username = "muhammad.yazid@tiket.com";
        String hotelpassword = "TestTiket@123";
        String startDate = generateRandomDate("24-30");
        String hotelName = "Carvens Hotel";
        int adult = guests;
        int night = 1;
        int room = guests;
        String currency = "IDR";
        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autoguest";
        String guestTitle = salutationList[0];

        step("Create new hotel issued order");
        orderCreationPage.createHotelSakukuIssuedOrderApi(username, hotelpassword, startDate, hotelName,
                adult, night, room, orderFullName, orderNameTitle, guestFullName, guestTitle, currency);

        step("Get data from the issued order");
        String bookedOrderID = String.valueOf(orderCreationPage.getOrderId());

        step("Login via cookie injection");
        loginMemberAdmin.loginViaCookie(email, password);

        step("Go to View Order Dashboard");
        loginMemberAdmin.goToDashboard(url);
        Assertion.assertThat("Is Search Order Detail Box Loaded", viewOrderDashboardPage.isSearchOrderDetailBoxLoaded(), is(true));

        step("Click Source Dropdown");
        viewOrderDashboardPage.clickSourceDropdown();

        step("Select Source Order ID");
        viewOrderDashboardPage.selectSourceOrderID();
        Assertion.assertThat("Source Order ID is selected", viewOrderDashboardPage.checkSelectedSource("Order ID"), is(true));

        step("Input Order Id");
        viewOrderDashboardPage.setOrderIdInput(bookedOrderID);

        step("Click Search");
        viewOrderDashboardPage.clickSearch();

        step("Open Edit Booking Page");
        Assertion.assertThat("Action column is Present", viewOrderDashboardPage.isActionPaneLoaded(), is(true));
        viewOrderDashboardPage.clickMoreActionButton();
        Assertion.assertThat("More Action Box is Loaded", viewOrderDashboardPage.isMoreActionBoxLoaded(), is(true));
        Assertion.assertThat("Edit Booking is visible", viewOrderDashboardPage.isEditBookingVisible(), containsString("Edit Booking"));
        viewOrderDashboardPage.clickEditBookingButton();
        Assertion.assertThat("Edit Booking Page is Visible", viewOrderDashboardPage.isEditBookingPageLoaded(), is(true));

        step("Check Guest Details");
        Assertion.assertThat("Guest Details Section is Visible", viewOrderDashboardPage.isGuestDetailsSectionLoaded(), is(true));
        viewOrderDashboardPage.isAllGuestDetailsLoaded(adult);

        step("Edit Guest Details");
        viewOrderDashboardPage.fillGuestDetails(adult);
        Assertion.assertThat("Submit Button is Clickable", viewOrderDashboardPage.isSubmitButtonClickable(), is(true));

    }

    @Author(names = {"Ketan Singh"})
    @Module(name = "ViewOrder")
    @Tribe(name = "Demand")
    @Vertical(name = "Accommodation")
    @Test
    public void testPopupWhenGuestDetailsChanged() {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        ViewOrderDashboardPage<?> viewOrderDashboardPage = new ViewOrderDashboardPage<>(session);
        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<>(session);
        orderCreationPage<?> orderCreationPage = new orderCreationPage<>(session);
        Map<String, String> param = getParam("testPopupWhenGuestDetailsChanged");
        BaseTest.paramsThreadLocal.set(param);

        String email = param.get("email");
        String password = param.get("password");
        String url = param.get("url");
        int guests = Integer.parseInt(param.get("guests"));

        String username = "muhammad.yazid@tiket.com";
        String hotelpassword = "TestTiket@123";
        String startDate = generateRandomDate("24-30");
        String hotelName = "Carvens Hotel";
        int adult = guests;
        int night = 1;
        int room = guests;
        String currency = "IDR";
        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autoguest";
        String guestTitle = salutationList[0];

        step("Create new hotel issued order");
        orderCreationPage.createHotelSakukuIssuedOrderApi(username, hotelpassword, startDate, hotelName,
                adult, night, room, orderFullName, orderNameTitle, guestFullName, guestTitle, currency);

        step("Get data from the issued order");
        String bookedOrderID = String.valueOf(orderCreationPage.getOrderId());

        step("Login via cookie injection");
        loginMemberAdmin.loginViaCookie(email, password);

        step("Go to View Order Dashboard");
        loginMemberAdmin.goToDashboard(url);
        Assertion.assertThat("Is Search Order Detail Box Loaded", viewOrderDashboardPage.isSearchOrderDetailBoxLoaded(), is(true));

        step("Click Source Dropdown");
        viewOrderDashboardPage.clickSourceDropdown();

        step("Select Source Order ID");
        viewOrderDashboardPage.selectSourceOrderID();
        Assertion.assertThat("Source Order ID is selected", viewOrderDashboardPage.checkSelectedSource("Order ID"), is(true));

        step("Input Order Id");
        viewOrderDashboardPage.setOrderIdInput(bookedOrderID);

        step("Click Search");
        viewOrderDashboardPage.clickSearch();

        step("Open Edit Booking Page");
        Assertion.assertThat("Action column is Present", viewOrderDashboardPage.isActionPaneLoaded(), is(true));
        viewOrderDashboardPage.clickMoreActionButton();
        Assertion.assertThat("More Action Box is Loaded", viewOrderDashboardPage.isMoreActionBoxLoaded(), is(true));
        Assertion.assertThat("Edit Booking is visible", viewOrderDashboardPage.isEditBookingVisible(), containsString("Edit Booking"));
        viewOrderDashboardPage.clickEditBookingButton();
        Assertion.assertThat("Edit Booking Page is Visible", viewOrderDashboardPage.isEditBookingPageLoaded(), is(true));

        step("Check Guest Details");
        Assertion.assertThat("Guest Details Section is Visible", viewOrderDashboardPage.isGuestDetailsSectionLoaded(), is(true));
        viewOrderDashboardPage.isAllGuestDetailsLoaded(adult);
        String OldFirstName = viewOrderDashboardPage.getFirstName();
        String OldSecondName = viewOrderDashboardPage.getSecondName();


        step("Edit Guest Details");
        viewOrderDashboardPage.fillGuestDetails(adult);
        Assertion.assertThat("Submit Button is Clickable", viewOrderDashboardPage.isSubmitButtonClickable(), is(true));
        viewOrderDashboardPage.clickSubmitButton();


        step("Check Popup Is Loaded");
        Assertion.assertThat("Popup Is Loaded", viewOrderDashboardPage.isPopUpLoaded(), is(true));
        viewOrderDashboardPage.clickOkButton();
        String newFirstName = viewOrderDashboardPage.getFirstName();
        String newSecondName = viewOrderDashboardPage.getSecondName();
        Assertion.assertThat("First Guest Name Is Changed", newFirstName, not(OldFirstName));
        Assertion.assertThat("Second Guest Name Is Changed", newSecondName, not(OldSecondName));
    }

    @Author(names = {"Ketan Singh"})
    @Module(name = "ViewOrder")
    @Tribe(name = "Demand")
    @Vertical(name = "Accommodation")
    @Test
    public void testOkButtonIsClickable() {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        ViewOrderDashboardPage<?> viewOrderDashboardPage = new ViewOrderDashboardPage<>(session);
        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<>(session);
        orderCreationPage<?> orderCreationPage = new orderCreationPage<>(session);
        Map<String, String> param = getParam("testOkButtonIsClickable");
        BaseTest.paramsThreadLocal.set(param);

        String email = param.get("email");
        String password = param.get("password");
        String url = param.get("url");
        int guests = Integer.parseInt(param.get("guests"));

        String username = "muhammad.yazid@tiket.com";
        String hotelpassword = "TestTiket@123";
        String startDate = generateRandomDate("24-30");
        String hotelName = "Carvens Hotel";
        int adult = guests;
        int night = 1;
        int room = 2;
        String currency = "IDR";
        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autoguest";
        String guestTitle = salutationList[0];

        step("Create new hotel issued order");
        orderCreationPage.createHotelSakukuIssuedOrderApi(username, hotelpassword, startDate, hotelName,
                adult, night, room, orderFullName, orderNameTitle, guestFullName, guestTitle, currency);

        step("Get data from the issued order");
        String bookedOrderID = String.valueOf(orderCreationPage.getOrderId());

        step("Login via cookie injection");
        loginMemberAdmin.loginViaCookie(email, password);

        step("Go to View Order Dashboard");
        loginMemberAdmin.goToDashboard(url);
        Assertion.assertThat("Is Search Order Detail Box Loaded", viewOrderDashboardPage.isSearchOrderDetailBoxLoaded(), is(true));

        step("Click Source Dropdown");
        viewOrderDashboardPage.clickSourceDropdown();

        step("Select Source Order ID");
        viewOrderDashboardPage.selectSourceOrderID();
        Assertion.assertThat("Source Order ID is selected", viewOrderDashboardPage.checkSelectedSource("Order ID"), is(true));

        step("Input Order Id");
        viewOrderDashboardPage.setOrderIdInput(bookedOrderID);

        step("Click Search");
        viewOrderDashboardPage.clickSearch();

        step("Open Edit Booking Page");
        Assertion.assertThat("Action column is Present", viewOrderDashboardPage.isActionPaneLoaded(), is(true));
        viewOrderDashboardPage.clickMoreActionButton();
        Assertion.assertThat("More Action Box is Loaded", viewOrderDashboardPage.isMoreActionBoxLoaded(), is(true));
        Assertion.assertThat("Edit Booking is visible", viewOrderDashboardPage.isEditBookingVisible(), containsString("Edit Booking"));
        viewOrderDashboardPage.clickEditBookingButton();
        Assertion.assertThat("Edit Booking Page is Visible", viewOrderDashboardPage.isEditBookingPageLoaded(), is(true));

        step("Check Guest Details");
        Assertion.assertThat("Guest Details Section is Visible", viewOrderDashboardPage.isGuestDetailsSectionLoaded(), is(true));
        viewOrderDashboardPage.isAllGuestDetailsLoaded(adult);

        step("Edit Guest Details");
        viewOrderDashboardPage.fillGuestDetails(adult);
        Assertion.assertThat("Submit Button is Clickable", viewOrderDashboardPage.isSubmitButtonClickable(), is(true));
        viewOrderDashboardPage.clickSubmitButton();

        step("Check Popup Is Loaded");
        Assertion.assertThat("Popup Is Loaded", viewOrderDashboardPage.isPopUpLoaded(), is(true));
        Assertion.assertThat("OK Button Is Clickable", viewOrderDashboardPage.isOkButtonClickable(), is(true));
    }

    @Author(names = {"Ketan Singh", "Vipul Kumar"})
    @Module(name = "ViewOrder")
    @Tribe(name = "Demand")
    @Vertical(name = "Accommodation")
    @Test
    public void testChangeSpecialRequestForSuccessBooking() {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        ViewOrderDashboardPage<?> viewOrderDashboardPage = new ViewOrderDashboardPage<>(session);
        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<>(session);
        orderCreationPage<?> orderCreationPage = new orderCreationPage<>(session);
        Map<String, String> param = getParam("testChangeSpecialRequestForSuccessBooking");
        BaseTest.paramsThreadLocal.set(param);

        String email = param.get("email");
        String password = param.get("password");
        String url = param.get("url");
        String Special_Request = param.get("Special_Request");

        String hotelApiUsername = param.get("hotelApiUsername");
        String hotelApiPassword = param.get("hotelApiPassword");
        String hotelName = param.get("hotelName");
        String currencyParam = param.get("currency");
        String currency = (currencyParam == null || currencyParam.isBlank()) ? "IDR" : currencyParam.trim();

        String startDate = generateRandomDate("24-30");
        int adult = 1;
        int night = 1;
        int room = 1;
        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autoguest";
        String guestTitle = salutationList[0];

        step("Create new hotel issued order");
        orderCreationPage.createHotelSakukuIssuedOrderApi(hotelApiUsername, hotelApiPassword, startDate, hotelName,
                adult, night, room, orderFullName, orderNameTitle, guestFullName, guestTitle, currency);

        step("Get data from the issued order");
        String bookedOrderID = String.valueOf(orderCreationPage.getOrderId());

        step("Login via cookie injection");
        loginMemberAdmin.loginViaCookie(email, password);

        step("Go to View Order Dashboard");
        loginMemberAdmin.goToDashboard(url);
        Assertion.assertThat("Is Search Order Detail Box Loaded", viewOrderDashboardPage.isSearchOrderDetailBoxLoaded(), is(true));

        step("Click Source Dropdown");
        viewOrderDashboardPage.clickSourceDropdown();

        step("Select Source Order ID");
        viewOrderDashboardPage.selectSourceOrderID();
        Assertion.assertThat("Source Order ID is selected", viewOrderDashboardPage.checkSelectedSource("Order ID"), is(true));

        step("Input Order Id");
        viewOrderDashboardPage.setOrderIdInput(bookedOrderID);

        step("Click Search");
        viewOrderDashboardPage.clickSearch();

        step("Every Order Details Should be Visible");
        Assertion.assertThat("Order Detail Box is Loaded", viewOrderDashboardPage.isOrderDetailBoxLoaded(), is(true));
        Assertion.assertThat("Order Id is Present", viewOrderDashboardPage.getOrderId(), containsString(bookedOrderID));
        Assertion.assertThat("Itinerary Id is Present", viewOrderDashboardPage.isItineraryIdLoaded(), is(true));
        Assertion.assertThat("Date purchased is Present", viewOrderDashboardPage.isDatePurchasedLoaded(), is(true));
        Assertion.assertThat("Product Type is Present", viewOrderDashboardPage.isProductTypeLoaded(), is(true));
        Assertion.assertThat("Order Detail is Present", viewOrderDashboardPage.isOrderDetailLoaded(), is(true));
        Assertion.assertThat("Passenger Detail is Present", viewOrderDashboardPage.isPassengerDetailLoaded(), is(true));
        Assertion.assertThat("Order Status is E-Ticket Issued", viewOrderDashboardPage.getSuccessOrderStatus(), containsString("E-ticket issued"));
        Assertion.assertThat("Action column is Present", viewOrderDashboardPage.isActionPaneLoaded(), is(true));
        Assertion.assertThat("More Action Button is Clickable", viewOrderDashboardPage.isActionButtonClickable(), is(true));

        step("Action Button Should Be Shown With Its Items");
        Assertion.assertThat("Action column is Present", viewOrderDashboardPage.isActionPaneLoaded(), is(true));
        viewOrderDashboardPage.clickMoreActionButton();
        Assertion.assertThat("More Action Box is Loaded", viewOrderDashboardPage.isMoreActionBoxLoaded(), is(true));
        Assertion.assertThat("Edit Booking is visible", viewOrderDashboardPage.isEditBookingVisible(), containsString("Edit Booking"));

        step("Click Edit booking");
        viewOrderDashboardPage.clickEditBookingButton();
        Assertion.assertThat("Edit Booking Page is Visible", viewOrderDashboardPage.isEditBookingPageLoaded(), is(true));

        step("Check Special request");
        Assertion.assertThat("Special Request Section is Visible", viewOrderDashboardPage.isSpecialRequestSectionLoaded(), is(true));

        step("Input Special Request");
        viewOrderDashboardPage.setSpecialRequestInputIdField(Special_Request);
        Assertion.assertThat("New Special Request is Visible", viewOrderDashboardPage.getNewSpecialRequest(), containsString(Special_Request));
    }

    @Author(names = {"Ketan Singh", "Bikash kumar jha"})
    @Module(name = "ViewOrder")
    @Tribe(name = "Demand")
    @Vertical(name = "Accommodation")
    @Test
    public void testCheckSubmitButtonClickableForSpecialRequest() {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        ViewOrderDashboardPage<?> viewOrderDashboardPage = new ViewOrderDashboardPage<>(session);
        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<>(session);
        orderCreationPage<?> orderCreationPage = new orderCreationPage<>(session);
        Map<String, String> param = getParam("testCheckSubmitButtonClickableForSpecialRequest");
        BaseTest.paramsThreadLocal.set(param);

        String email = param.get("email");
        String password = param.get("password");
        String url = param.get("url");

        String hotelApiUsername = param.get("hotelApiUsername");
        String hotelApiPassword = param.get("hotelApiPassword");
        String hotelName = param.get("hotelName");
        String currencyParam = param.get("currency");
        String currency = (currencyParam == null || currencyParam.isBlank()) ? "IDR" : currencyParam.trim();

        String startDate = generateRandomDate("24-30");
        int adult = 1;
        int night = 1;
        int room = 1;
        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autoguest";
        String guestTitle = salutationList[0];

        step("Create new hotel issued order");
        orderCreationPage.createHotelSakukuIssuedOrderApi(hotelApiUsername, hotelApiPassword, startDate, hotelName,
                adult, night, room, orderFullName, orderNameTitle, guestFullName, guestTitle, currency);

        step("Get data from the issued order");
        String bookedOrderID = String.valueOf(orderCreationPage.getOrderId());

        step("Login via cookie injection");
        loginMemberAdmin.loginViaCookie(email, password);

        step("Go to View Order Dashboard");
        loginMemberAdmin.goToDashboard(url);
        Assertion.assertThat("Is Search Order Detail Box Loaded", viewOrderDashboardPage.isSearchOrderDetailBoxLoaded(), is(true));

        step("Click Source Dropdown");
        viewOrderDashboardPage.clickSourceDropdown();

        step("Select Source Order ID");
        viewOrderDashboardPage.selectSourceOrderID();
        Assertion.assertThat("Source Order ID is selected", viewOrderDashboardPage.checkSelectedSource("Order ID"), is(true));

        step("Input Order Id");
        viewOrderDashboardPage.setOrderIdInput(bookedOrderID);

        step("Click Search");
        viewOrderDashboardPage.clickSearch();

        step("Every Order Details Should be Visible");
        Assertion.assertThat("Order Detail Box is Loaded", viewOrderDashboardPage.isOrderDetailBoxLoaded(), is(true));
        Assertion.assertThat("Order Id is Present", viewOrderDashboardPage.getOrderId(), containsString(bookedOrderID));
        Assertion.assertThat("Itinerary Id is Present", viewOrderDashboardPage.isItineraryIdLoaded(), is(true));
        Assertion.assertThat("Date purchased is Present", viewOrderDashboardPage.isDatePurchasedLoaded(), is(true));
        Assertion.assertThat("Product Type is Present", viewOrderDashboardPage.isProductTypeLoaded(), is(true));
        Assertion.assertThat("Order Detail is Present", viewOrderDashboardPage.isOrderDetailLoaded(), is(true));
        Assertion.assertThat("Passenger Detail is Present", viewOrderDashboardPage.isPassengerDetailLoaded(), is(true));
        Assertion.assertThat("Order Status is E-Ticket Issued", viewOrderDashboardPage.getSuccessOrderStatus(), containsString("E-ticket issued"));
        Assertion.assertThat("Action column is Present", viewOrderDashboardPage.isActionPaneLoaded(), is(true));
        Assertion.assertThat("More Action Button is Clickable", viewOrderDashboardPage.isActionButtonClickable(), is(true));

        step("Action Button Should Be Shown With Its Items");
        Assertion.assertThat("Action column is Present", viewOrderDashboardPage.isActionPaneLoaded(), is(true));
        viewOrderDashboardPage.clickMoreActionButton();
        Assertion.assertThat("More Action Box is Loaded", viewOrderDashboardPage.isMoreActionBoxLoaded(), is(true));
        Assertion.assertThat("Edit Booking is visible", viewOrderDashboardPage.isEditBookingVisible(), containsString("Edit Booking"));

        step("Click Edit booking");
        viewOrderDashboardPage.clickEditBookingButton();
        Assertion.assertThat("Edit Booking Page is Visible", viewOrderDashboardPage.isEditBookingPageLoaded(), is(true));

        step("Check Special request");
        Assertion.assertThat("Special Request Section is Visible", viewOrderDashboardPage.isSpecialRequestSectionLoaded(), is(true));

        step("Input Special Request");
        String name = randomName();
        viewOrderDashboardPage.setSpecialRequestInputIdField(name);
        Assertion.assertThat("New Special Request is Visible", viewOrderDashboardPage.getNewSpecialRequest(), containsString(name));

        step("Check Submit is clickable");
        Assertion.assertThat("Submit button is clickable", viewOrderDashboardPage.isSpecialRequestSubmitButtonClickable(), is(true));
    }

    @Author(names = {"Ketan Singh", "Bikash kumar jha"})
    @Module(name = "ViewOrder")
    @Tribe(name = "Demand")
    @Vertical(name = "Accommodation")
    @Test
    public void testOkButtonClickableForSpecialRequest() {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        ViewOrderDashboardPage<?> viewOrderDashboardPage = new ViewOrderDashboardPage<>(session);
        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<>(session);
        orderCreationPage<?> orderCreationPage = new orderCreationPage<>(session);
        Map<String, String> param = getParam("testOkButtonClickableForSpecialRequest");
        BaseTest.paramsThreadLocal.set(param);

        String email = param.get("email");
        String password = param.get("password");
        String url = param.get("url");

        String hotelApiUsername = param.get("hotelApiUsername");
        String hotelApiPassword = param.get("hotelApiPassword");
        String hotelName = param.get("hotelName");
        String currencyParam = param.get("currency");
        String currency = (currencyParam == null || currencyParam.isBlank()) ? "IDR" : currencyParam.trim();

        String startDate = generateRandomDate("24-30");
        int adult = 1;
        int night = 1;
        int room = 1;
        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autoguest";
        String guestTitle = salutationList[0];

        step("Create new hotel issued order");
        orderCreationPage.createHotelSakukuIssuedOrderApi(hotelApiUsername, hotelApiPassword, startDate, hotelName,
                adult, night, room, orderFullName, orderNameTitle, guestFullName, guestTitle, currency);

        step("Get data from the issued order");
        String bookedOrderID = String.valueOf(orderCreationPage.getOrderId());

        step("Login via cookie injection");
        loginMemberAdmin.loginViaCookie(email, password);

        step("Go to View Order Dashboard");
        loginMemberAdmin.goToDashboard(url);
        Assertion.assertThat("Is Search Order Detail Box Loaded", viewOrderDashboardPage.isSearchOrderDetailBoxLoaded(), is(true));

        step("Click Source Dropdown");
        viewOrderDashboardPage.clickSourceDropdown();

        step("Select Source Order ID");
        viewOrderDashboardPage.selectSourceOrderID();
        Assertion.assertThat("Source Order ID is selected", viewOrderDashboardPage.checkSelectedSource("Order ID"), is(true));

        step("Input Order Id");
        viewOrderDashboardPage.setOrderIdInput(bookedOrderID);

        step("Click Search");
        viewOrderDashboardPage.clickSearch();

        step("Every Order Details Should be Visible");
        Assertion.assertThat("Order Detail Box is Loaded", viewOrderDashboardPage.isOrderDetailBoxLoaded(), is(true));
        Assertion.assertThat("Order Id is Present", viewOrderDashboardPage.getOrderId(), containsString(bookedOrderID));
        Assertion.assertThat("Itinerary Id is Present", viewOrderDashboardPage.isItineraryIdLoaded(), is(true));
        Assertion.assertThat("Date purchased is Present", viewOrderDashboardPage.isDatePurchasedLoaded(), is(true));
        Assertion.assertThat("Product Type is Present", viewOrderDashboardPage.isProductTypeLoaded(), is(true));
        Assertion.assertThat("Order Detail is Present", viewOrderDashboardPage.isOrderDetailLoaded(), is(true));
        Assertion.assertThat("Passenger Detail is Present", viewOrderDashboardPage.isPassengerDetailLoaded(), is(true));
        Assertion.assertThat("Order Status is E-Ticket Issued", viewOrderDashboardPage.getSuccessOrderStatus(), containsString("E-ticket issued"));
        Assertion.assertThat("Action column is Present", viewOrderDashboardPage.isActionPaneLoaded(), is(true));
        Assertion.assertThat("More Action Button is Clickable", viewOrderDashboardPage.isActionButtonClickable(), is(true));

        step("Action Button Should Be Shown With Its Items");
        Assertion.assertThat("Action column is Present", viewOrderDashboardPage.isActionPaneLoaded(), is(true));
        viewOrderDashboardPage.clickMoreActionButton();
        Assertion.assertThat("More Action Box is Loaded", viewOrderDashboardPage.isMoreActionBoxLoaded(), is(true));
        Assertion.assertThat("Edit Booking is visible", viewOrderDashboardPage.isEditBookingVisible(), containsString("Edit Booking"));

        step("Click Edit booking");
        viewOrderDashboardPage.clickEditBookingButton();
        Assertion.assertThat("Edit Booking Page is Visible", viewOrderDashboardPage.isEditBookingPageLoaded(), is(true));

        step("Check Special request");
        Assertion.assertThat("Special Request Section is Visible", viewOrderDashboardPage.isSpecialRequestSectionLoaded(), is(true));

        step("Input Special Request");
        String name = randomName();
        viewOrderDashboardPage.setSpecialRequestInputIdField(name);
        Assertion.assertThat("New Special Request is Visible", viewOrderDashboardPage.getNewSpecialRequest(), containsString(name));

        step("Check Submit is clickable");
        Assertion.assertThat("Submit button is clickable", viewOrderDashboardPage.isSpecialRequestSubmitButtonClickable(), is(true));

        step("Click Submit Button");
        viewOrderDashboardPage.clickSpecialRequestSubmitButton();

        step("Check Confirmation Popup is Loaded");
        Assertion.assertThat("Confirmation Popup is Loaded", viewOrderDashboardPage.isSpecialRequestPopUpLoaded(), is(true));

        step("Check Ok button is clickable");
        Assertion.assertThat("Ok button is clickable", viewOrderDashboardPage.isSpecialRequestOkButtonClickable(), is(true));
    }

    @Author(names = "Akshi")
    @Module(name = "ViewOrder")
    @Tribe(name = "Demand")
    @Vertical(name = "Accommodation")
    @Test
    public void testSpecialRequest() {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        ViewOrderDashboardPage<?> viewOrderDashboardPage = new ViewOrderDashboardPage<>(session);
        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<>(session);
        orderCreationPage<?> orderCreationPage = new orderCreationPage<>(session);
        Map<String, String> param = getParam("testSpecialRequest");
        BaseTest.paramsThreadLocal.set(param);

        String email = param.get("email");
        String password = param.get("password");
        String url = param.get("url");

        String username = param.get("username");
        String hotelPassword = param.get("hotelPassword");
        String startDateRaw = param.get("startDate").trim();
        String startDate = startDateRaw.split("-").length >= 3 ? startDateRaw : generateRandomDate(startDateRaw);
        String hotelName = param.get("hotelName");
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));
        int room = Integer.parseInt(param.get("room"));
        String currency = param.get("currency");
        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autoguest";
        String guestTitle = salutationList[0];

        step("Create new hotel issued order");
        orderCreationPage.createHotelSakukuIssuedOrderApi(username, hotelPassword, startDate, hotelName,
                adult, night, room, orderFullName, orderNameTitle, guestFullName, guestTitle, currency);

        step("Get data from the issued order");
        String bookedOrderID = String.valueOf(orderCreationPage.getOrderId());

        step("Login via cookie injection");
        loginMemberAdmin.loginViaCookie(email, password);

        step("Go to View Order Dashboard");
        loginMemberAdmin.goToDashboard(url);
        Assertion.assertThat("Is Search Order Detail Box Loaded", viewOrderDashboardPage.isSearchOrderDetailBoxLoaded(), is(true));

        step("Click Source Dropdown");
        viewOrderDashboardPage.clickSourceDropdown();

        step("Select Source Order ID");
        viewOrderDashboardPage.selectSourceOrderID();
        Assertion.assertThat("Source Order ID is selected", viewOrderDashboardPage.checkSelectedSource("Order ID"), is(true));

        step("Input Order Id");
        viewOrderDashboardPage.setOrderIdInput(bookedOrderID);

        step("Click Search");
        viewOrderDashboardPage.clickSearch();

        step("Every Order Details Should be Visible");
        Assertion.assertThat("Order Detail Box is Loaded", viewOrderDashboardPage.isOrderDetailBoxLoaded(), is(true));
        Assertion.assertThat("Order Id is Present", viewOrderDashboardPage.getOrderId(), containsString(bookedOrderID));

        step("Action Button Should Be Shown With Its Items");
        Assertion.assertThat("Action column is Present", viewOrderDashboardPage.isActionPaneLoaded(), is(true));
        viewOrderDashboardPage.clickMoreActionButton();
        Assertion.assertThat("More Action Box is Loaded", viewOrderDashboardPage.isMoreActionBoxLoaded(), is(true));
        Assertion.assertThat("Edit Booking is visible", viewOrderDashboardPage.isEditBookingVisible(), containsString("Edit Booking"));

        step("Click Edit booking");
        viewOrderDashboardPage.clickEditBookingButton();
        Assertion.assertThat("Edit Booking Page is Visible", viewOrderDashboardPage.isEditBookingPageLoaded(), is(true));

        step("Check Special request");
        Assertion.assertThat("Special Request Section is Visible", viewOrderDashboardPage.isSpecialRequestSectionLoaded(), is(true));

        step("Input Special Request");
        String name = randomName();
        viewOrderDashboardPage.setSpecialRequestInputIdField(name);
        Assertion.assertThat("New Special Request is Visible", viewOrderDashboardPage.getNewSpecialRequest(), containsString(name));

        Assertion.assertThat("Checkbox in change special request is clickable", viewOrderDashboardPage.isSpecialRequestCheckboxClickable(), is(true));

        step("Check Submit is clickable");
        Assertion.assertThat("Submit button is clickable", viewOrderDashboardPage.isSpecialRequestSubmitButtonClickable(), is(true));

        step("Click Submit Button");
        viewOrderDashboardPage.clickSpecialRequestSubmitButton();

        step("Check Confirmation Popup is Loaded");
        Assertion.assertThat("Confirmation Popup is Loaded", viewOrderDashboardPage.isSpecialRequestPopUpLoaded(), is(true));

        Assertion.assertThat("Check cross button clickable", viewOrderDashboardPage.isSpecialRequestCrossButtonClickable(), is(true));
        viewOrderDashboardPage.clickSpecialRequestSubmitButton();

        step("Check Ok button is clickable");
        Assertion.assertThat("Ok button is clickable", viewOrderDashboardPage.isSpecialRequestOkButtonClickable(), is(true));

    }

    @Author(names = {"Aldy Heryana"})
    @Module(name = "ViewOrder")
    @Tribe(name = "Demand")
    @Vertical(name = "Accommodation")
    @Test
    public void testCheckEachSourceAbleToSelect() {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        ViewOrderDashboardPage<?> viewOrderDashboardPage = new ViewOrderDashboardPage<>(session);
        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<>(session);
        Map<String, String> param = getParam("testCheckEachSourceAbleToSelect");
        BaseTest.paramsThreadLocal.set(param);

        String email = param.get("email");
        String password = param.get("password");
        String url = param.get("url");
        String orderId = param.get("orderId");
        String itineraryId = param.get("itineraryId");
        String vendorItineraryId = param.get("vendorItineraryId");

        step("Login via cookie injection");
        loginMemberAdmin.loginViaCookie(email, password);

        step("Go to View Order Dashboard");
        loginMemberAdmin.goToDashboard(url);

        step("Click Source Dropdown");
        viewOrderDashboardPage.clickSourceDropdown();

        step("Click source OrderId");
        viewOrderDashboardPage.selectSourceOrderID();

        step("Input source OrderId");
        viewOrderDashboardPage.SetInputOrderId(orderId);

        step("Click Search");
        viewOrderDashboardPage.clickSearch();

        step("Order Status should be Finished");
        Assertion.assertThat("OrderId Detail Box is Loaded", viewOrderDashboardPage.isOrderDetailBoxLoaded(), is(true));
        Assertion.assertThat("OrderId is Present", viewOrderDashboardPage.getOrderId(), containsString(orderId));
        Assertion.assertThat("Order Status is Finished", viewOrderDashboardPage.getSuccessOrderStatus(), containsString("Finished"));

        step("Click Source Dropdown");
        viewOrderDashboardPage.clickSourceDropdownItineraryId();

        step("Click source ItineraryId");
        viewOrderDashboardPage.selectSourceItineraryID();

        step("Input ItineraryId");
        viewOrderDashboardPage.SetInputItineraryId(itineraryId);

        step("Click Search");
        viewOrderDashboardPage.clickSearch();

        step("Order Status should be Finished");
        Assertion.assertThat("ItineraryId Detail Box is Loaded", viewOrderDashboardPage.isOrderDetailBoxLoaded(), is(true));
        Assertion.assertThat("ItineraryId is Present", viewOrderDashboardPage.getItinid(), containsString(itineraryId));
        Assertion.assertThat("Order Status is Finished", viewOrderDashboardPage.getSuccessOrderStatus(), containsString("Finished"));

        step("Click Source Dropdown");
        viewOrderDashboardPage.clickSourceDropdownVendorItineraryId();

        step("Click source Vendor ItineraryId");
        viewOrderDashboardPage.selectSourceVendorItineraryID();

        step("Input Vendor ItineraryId");
        viewOrderDashboardPage.SetInputVendorItineraryId(vendorItineraryId);

        step("Click Search");
        viewOrderDashboardPage.clickSearch();

        step("Order Status should be Finished");
        Assertion.assertThat("ItineraryId Detail Box is Loaded", viewOrderDashboardPage.isOrderDetailBoxLoaded(), is(true));
        Assertion.assertThat("Vendor ItineraryId is Present", viewOrderDashboardPage.getItinid(), containsString(vendorItineraryId));
        Assertion.assertThat("Order Status is Finished", viewOrderDashboardPage.getSuccessOrderStatus(), containsString("Finished"));
    }

    @Author(names = {"Aldy Heryana"})
    @Module(name = "ViewOrder")
    @Tribe(name = "Demand")
    @Vertical(name = "Accommodation")
    @Test

    public void testApproveButtonFailedCheckin() {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        ViewOrderDashboardPage<?> viewOrderDashboardPage = new ViewOrderDashboardPage<>(session);
        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<>(session);
        Map<String, String> param = getParam("testApproveButtonFailedCheckin");
        BaseTest.paramsThreadLocal.set(param);

        String email = param.get("email");
        String password = param.get("password");
        String url = param.get("url");

        step("Login via cookie injection");
        loginMemberAdmin.loginViaCookie(email, password);

        step("Go to View Order Dashboard");
        loginMemberAdmin.goToDashboard(url);

        step("Action Button Should Be Shown and Clickable");
        Assertion.assertThat("approve toggle column is Present", viewOrderDashboardPage.isToggleClickable(), is(true));
        Assertion.assertThat("approve toggle is Clickable", viewOrderDashboardPage.isToggleClickable(), is(true));
    }

    @Author(names = {"Aldy Heryana"})
    @Module(name = "ViewOrder")
    @Tribe(name = "Demand")
    @Vertical(name = "Accommodation")
    @Test

    public void testFailedCasesRelocate() {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        ViewOrderDashboardPage<?> viewOrderDashboardPage = new ViewOrderDashboardPage<>(session);
        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<>(session);
        orderCreationPage<?> orderCreationPage = new orderCreationPage<>(session);
        Map<String, String> param = getParam("testFailedCasesRelocate");
        BaseTest.paramsThreadLocal.set(param);

        String email = param.get("email");
        String password = param.get("password");
        String url = param.get("url");

        String username = "muhammad.yazid@tiket.com";
        String hotelpassword = "TestTiket@123";
        String startDate = generateRandomDate("24-30");
        String hotelName = "Carvens Hotel";
        int adult = 1;
        int night = 1;
        int room = 1;
        String currency = "IDR";
        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autoguest";
        String guestTitle = salutationList[0];

        step("Create new hotel issued order");
        orderCreationPage.createHotelSakukuIssuedOrderApi(username, hotelpassword, startDate, hotelName,
                adult, night, room, orderFullName, orderNameTitle, guestFullName, guestTitle, currency);

        step("Get data from the issued order");
        String bookedOrderID = String.valueOf(orderCreationPage.getOrderId());

        step("Login via cookie injection");
        loginMemberAdmin.loginViaCookie(email, password);

        step("Go to View Order Dashboard");
        loginMemberAdmin.goToDashboard(url);
        Assertion.assertThat("Is Search Order Detail Box Loaded", viewOrderDashboardPage.isSearchOrderDetailBoxLoaded(), is(true));

        step("Click Source Dropdown");
        viewOrderDashboardPage.clickSourceDropdown();

        step("Select Source Order ID");
        viewOrderDashboardPage.selectSourceOrderID();
        Assertion.assertThat("Source Order ID is selected", viewOrderDashboardPage.checkSelectedSource("Order ID"), is(true));

        step("Input Order Id");
        viewOrderDashboardPage.setOrderIdInput(bookedOrderID);

        step("Click Search");
        viewOrderDashboardPage.clickSearch();

        step("Click More");
        viewOrderDashboardPage.clickMoreAction();

        step("Click Report Failed Check In");
        viewOrderDashboardPage.clickFailedCheckin();

        step("Click Category Dropdown");
        viewOrderDashboardPage.clickCategoryDropdown();

        step("Click failed check in category");
        viewOrderDashboardPage.selectFailedCheckinCategory();

        step("Click checkin problem Dropdown");
        viewOrderDashboardPage.clickCheckinProblemDropdown();

        step("Click checkin problem category");
        viewOrderDashboardPage.SelectCheckinProblem();

        step("Click reported from dropdown");
        viewOrderDashboardPage.clickReportedDropdown();

        step("Click reported from category");
        viewOrderDashboardPage.selectReportedCategory();

        step("Click root cause Dropdown");
        viewOrderDashboardPage.clickRootCauseDropdown();

        step("Click root cause category");
        viewOrderDashboardPage.selectRootCause();

        step("click continue");
        viewOrderDashboardPage.clickContinueFailed();

        step("Input Order Id");
        viewOrderDashboardPage.inputOrderidFailedCheckin(bookedOrderID);

        step("Click Search");
        viewOrderDashboardPage.clickSearchFailedCheckIn();

        step("Order id is showing");
        Assertion.assertThat("Order Id is Showing", viewOrderDashboardPage.getOrderIdFailedCheckin(), containsString(bookedOrderID));

        step("Click More");
        viewOrderDashboardPage.clickMoreAction();

        step("Click Relocation");
        viewOrderDashboardPage.clickRelocation();

        step("Order id is showing on relocation page");
        Assertion.assertThat("Order Id is Showing", viewOrderDashboardPage.getOrderIdRelocation(), containsString(bookedOrderID));
    }

    @Author(names = "Pankaj Singh Kushwaha")
    @Module(name = "ViewOrder")
    @Tribe(name = "Demand")
    @Vertical(name = "Accommodation")
    @Test
    public void VerifyUIElementsFieldEditabilityAndNavigationFlowInGuestNameEditSection() throws FailedLoginException, InterruptedException {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        ViewOrderDashboardPage<?> viewOrderDashboardPage = new ViewOrderDashboardPage<>(session);
        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<>(session);
        orderCreationPage<?> orderCreationPage = new orderCreationPage<>(session);
        Map<String, String> param = getParam("VerifyUIElementsFieldEditabilityAndNavigationFlowInGuestNameEditSection");
        BaseTest.paramsThreadLocal.set(param);

        String email = param.get("email");
        String password = param.get("password");
        String url = param.get("url");

        String username = param.get("username");
        String hotelPassword = param.get("hotelPassword");
        String startDateRaw = param.get("startDate").trim();
        String startDate = startDateRaw.split("-").length >= 3 ? startDateRaw : generateRandomDate(startDateRaw);
        String hotelName = param.get("hotelName");
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));
        int room = Integer.parseInt(param.get("room"));
        String currency = param.get("currency");
        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autoguest";
        String guestTitle = salutationList[0];

        step("Create new hotel issued order");
        orderCreationPage.createHotelSakukuIssuedOrderApi(username, hotelPassword, startDate, hotelName,
                adult, night, room, orderFullName, orderNameTitle, guestFullName, guestTitle, currency);

        step("Get data from the issued order");
        String bookedOrderID = String.valueOf(orderCreationPage.getOrderId());

        step("Go to member login page");
        loginMemberAdmin.gotoHome();

        step("Log out");
        boolean isLoggedInMemberAdmin = loginMemberAdmin.isUserLoggedIn(email, password);

        step("Assert that user is logged in to Member Admin");
        Assertion.assertThat("User is logged in", isLoggedInMemberAdmin, is(true));

        step("Go to View Order Dashboard");
        loginMemberAdmin.goToDashboard(url);
        Assertion.assertThat("Is Search Order Detail Box Loaded", viewOrderDashboardPage.isSearchOrderDetailBoxLoaded(), is(true));

        step("Click Source Dropdown");
        viewOrderDashboardPage.clickSourceDropdown();

        step("Select Source Order ID");
        viewOrderDashboardPage.selectSourceOrderID();
        Assertion.assertThat("Source Order ID is selected", viewOrderDashboardPage.checkSelectedSource("Order ID"), is(true));

        step("Input Order Id");
        viewOrderDashboardPage.setOrderIdInput(bookedOrderID);

        step("Click Search");
        viewOrderDashboardPage.clickSearch();

        step("Every Order Details Should be Visible");
        Assertion.assertThat("Order Detail Box is Loaded", viewOrderDashboardPage.isOrderDetailBoxLoaded(), is(true));
        Assertion.assertThat("Order Id is Present", viewOrderDashboardPage.getOrderId(), containsString(bookedOrderID));

        step("Action Button Should Be Shown With Its Items");
        Assertion.assertThat("Action column is Present", viewOrderDashboardPage.isActionPaneLoaded(), is(true));
        viewOrderDashboardPage.clickMoreActionButton();
        Assertion.assertThat("More Action Box is Loaded", viewOrderDashboardPage.isMoreActionBoxLoaded(), is(true));
        Assertion.assertThat("Edit Booking is visible", viewOrderDashboardPage.isEditBookingVisible(), containsString("Edit Booking"));

        step("Click Edit booking");
        viewOrderDashboardPage.clickEditBookingButton();
        Assertion.assertThat("Edit Booking Page is Visible", viewOrderDashboardPage.isEditBookingPageLoaded(), is(true));

        step("Verify Order ID Summary and Guest Salutation Match Booking Parameters");
        String actualOrderId = viewOrderDashboardPage.getEditPageTableOrderId();
        Assertion.assertThat("Order ID in Edit view matches booking reference", actualOrderId, is(bookedOrderID));

        String actualSalutation = viewOrderDashboardPage.getGuestSalutationValue();
        Assertion.assertThat("Passenger salutation/title value matches dataset", actualSalutation, is(guestTitle));

        step("Check Guest Details");
        Assertion.assertThat("Guest Details Section is Visible", viewOrderDashboardPage.isGuestDetailsSectionLoaded(), is(true));
        viewOrderDashboardPage.isGuestNameMatchedWithThePaymentPage(adult, actualSalutation,guestFullName);
        String OldFirstName = viewOrderDashboardPage.getFirstName();

        step("Edit Guest Details");
        viewOrderDashboardPage.fillGuestDetails(adult);
        Assertion.assertThat("Checkbox is Selectable", viewOrderDashboardPage.isGuestDetailCheckboxClickable(), is(true));
        viewOrderDashboardPage.clickGuestDetailCheckbox();
        Assertion.assertThat("Submit Button is Clickable", viewOrderDashboardPage.isSubmitButtonClickable(), is(true));
        viewOrderDashboardPage.clickSubmitButton();

        step("Check Popup Is Loaded");
        Assertion.assertThat("Popup Is Loaded", viewOrderDashboardPage.isPopUpLoaded(), is(true));

        step("click on Ok button");
        viewOrderDashboardPage.clickOkButton();

        String newFirstName = viewOrderDashboardPage.getFirstName();
        Assertion.assertThat("First Guest Name Is Changed", newFirstName, not(OldFirstName));

        step("Navigate to Search Booking Dashboard");
        viewOrderDashboardPage.clickSearchBookingMenu();

        step("Check the Search Booking");
        Assertion.assertThat("Is Search Booking page is loaded", viewOrderDashboardPage.isSearchBookingPageLoaded(), is(true));

        step("Enter Order ID and Execute Search");
        viewOrderDashboardPage.searchBookingByOrderId(bookedOrderID);

        step("Verify Table Results Order ID Matches Search Context Parameters");
        String actualTableOrderId = viewOrderDashboardPage.getOrderIdFromTable();
        Assertion.assertThat("Order ID listed inside the dashboard results table matches the query source", actualTableOrderId, is(bookedOrderID));

        step("Open Row Options Context Menu");
        viewOrderDashboardPage.clickResultRowActionButton();

        step("Click Change Guest Name Options");
        viewOrderDashboardPage.clickChangeGuestNameOption();

        step("Get the old name");
        String oldName = viewOrderDashboardPage.getGuestNameFromModal();

        step("Verify the Close Button is Clickable");
        Assertion.assertThat("Is modal close button clickable", viewOrderDashboardPage.isModalCloseButtonClickable(), is(true));
        viewOrderDashboardPage.clickModalCloseButton();

        step("Open Row Options Context Menu");
        viewOrderDashboardPage.clickResultRowActionButton();

        step("Click Change Guest Name Options");
        viewOrderDashboardPage.clickChangeGuestNameOption();

        step("Verify the salutation matches the booking data");
        String actualModalSalutation = viewOrderDashboardPage.getGuestTitleFromModal();
        Assertion.assertThat("Modal passenger salutation matches configuration reference", actualModalSalutation, is(guestTitle));

        step("Check the 'Create Voucher Without Updating Vendor' Option");
        viewOrderDashboardPage.clickCreateVoucherWithoutUpdatingVendorCheckbox();

        step("Update with the new name");
        String updatedName = randomName()+"Testing";
        viewOrderDashboardPage.updateGuestNameAndSubmit(updatedName);

        step("Wait for Asynchronous Table Refresh");
        boolean isNameUpdated = viewOrderDashboardPage.waitForPassengerNameToBeUpdated(updatedName);
        Assertion.assertThat("Table dynamically updated with new name node", isNameUpdated, is(true));

        step("Verify Upgraded Guest Name on Dashboard Results Table");
        String currentTablePassengerName = viewOrderDashboardPage.getPassengerNameFromTable();
        Assertion.assertThat("Guest name successfully updated on core dashboard", currentTablePassengerName, is(updatedName));
        Assertion.assertThat("First Guest Name Is Changed", currentTablePassengerName, not(oldName));

    }

    @Author(names = {"Venkat"})
    @Module(name = "ViewOrder")
    @Tribe(name = "Demand")
    @Vertical(name = "Accommodation")
    @Test
    public void testActionItemsShownForDifferentOrder() {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        ViewOrderDashboardPage<?> viewOrderDashboardPage = new ViewOrderDashboardPage<>(session);
        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<>(session);
        orderCreationPage<?> orderCreationPage = new orderCreationPage<>(session);
        Map<String, String> param = getParam("testActionItemsShownForDifferentOrder");
        BaseTest.paramsThreadLocal.set(param);

        String email = param.get("email");
        String password = param.get("password");
        String url = param.get("url");
        String refundOrderId = param.get("refundOrderId");
        String failedOrderId = param.get("failedOrderId");
        String cancelledOrderId = param.get("cancelledOrderId");

        String username = param.get("username");
        String hotelpassword = param.get("hotelPassword");
        String startDateRange = param.get("startDate");
        if (startDateRange == null || startDateRange.isBlank()) {
            startDateRange = "24-30";
        }
        String startDate = generateRandomDate(startDateRange);
        String hotelName = param.get("hotelName");
        int adult = Integer.parseInt(param.get("adult").trim());
        int night = Integer.parseInt(param.get("night").trim());
        int room = Integer.parseInt(param.get("room").trim());
        String currencyParam = param.get("currency");
        String currency = (currencyParam == null || currencyParam.isBlank()) ? "IDR" : currencyParam.trim();
        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autoguest";
        String guestTitle = salutationList[0];

        step("Create new hotel issued order");
        orderCreationPage.createHotelSakukuIssuedOrderApi(username, hotelpassword, startDate, hotelName,
                adult, night, room, orderFullName, orderNameTitle, guestFullName, guestTitle,currency);

        step("Get data from the issued order");
        String successfulOrderID = String.valueOf(orderCreationPage.getOrderId());

        step("Go to member login page");
        loginMemberAdmin.gotoHome();

        step("Log In");
        boolean isLoggedInMemberAdmin = loginMemberAdmin.isUserLoggedIn(email,password);

        step("Assert that user is logged in to Member Admin");
        Assertion.assertThat("User is logged in", isLoggedInMemberAdmin, is(true));

        step("Go to View Order Dashboard");
        loginMemberAdmin.goToDashboard(url);
        Assertion.assertThat("Is Search Order Detail Box Loaded",viewOrderDashboardPage.isSearchOrderDetailBoxLoaded(), is(true));

        step("Click Source Dropdown");
        viewOrderDashboardPage.clickSourceDropdown();

        step("Select Source Order ID");
        viewOrderDashboardPage.selectSourceOrderID();
        Assertion.assertThat("Source Order ID is selected", viewOrderDashboardPage.checkSelectedSource("Order ID"), is(true));

        step("Input Cancelled Order Id");
        viewOrderDashboardPage.setOrderIdInput(cancelledOrderId);

        step("Click Search");
        viewOrderDashboardPage.clickSearch();

        step("Action Button Should Be Shown With Its Items");
        Assertion.assertThat("Order Detail Box is Loaded",viewOrderDashboardPage.isOrderDetailBoxLoaded(),is(true));
        Assertion.assertThat("Order Id is Present",viewOrderDashboardPage.getOrderId(),containsString(cancelledOrderId));
        Assertion.assertThat("Action column is Present",viewOrderDashboardPage.isActionPaneLoaded(),is(true));
        viewOrderDashboardPage.clickMoreActionButton();
        Assertion.assertThat("More Action Box is Loaded",viewOrderDashboardPage.isMoreActionBoxLoaded(),is(true));
        Assertion.assertThat("Edit Booking is visible",viewOrderDashboardPage.isEditBookingVisible(),containsString("Edit Booking"));
        Assertion.assertThat("Cancel Booking is visible",viewOrderDashboardPage.isCancelBookingVisible(),containsString("Cancel Booking"));

        viewOrderDashboardPage.clickMoreActionButton();
        step("Input Order Id");
        viewOrderDashboardPage.setOrderIdInput(successfulOrderID);

        step("Click Search");
        viewOrderDashboardPage.clickSearch();

        step("Action Button Should Be Shown With Its Items");
        Assertion.assertThat("Order Detail Box is Loaded",viewOrderDashboardPage.isOrderDetailBoxLoaded(),is(true));
        Assertion.assertThat("Order Id is Present",viewOrderDashboardPage.getOrderId(),containsString(successfulOrderID));
        Assertion.assertThat("Action column is Present",viewOrderDashboardPage.isActionPaneLoaded(),is(true));
        viewOrderDashboardPage.clickMoreActionButton();
        Assertion.assertThat("More Action Box is Loaded",viewOrderDashboardPage.isMoreActionBoxLoaded(),is(true));
        Assertion.assertThat("Edit Booking is visible",viewOrderDashboardPage.isEditBookingVisible(),containsString("Edit Booking"));
        Assertion.assertThat("Report Failed Checkin is Visible",viewOrderDashboardPage.isReportFailedCheckinVisible(),containsString("Report Failed Checkin"));
        Assertion.assertThat("Cancel Booking is visible",viewOrderDashboardPage.isCancelBookingVisible(),containsString("Cancel Booking"));

        viewOrderDashboardPage.clickMoreActionButton();
        step("Input Refund Order Id");
        viewOrderDashboardPage.setOrderIdInput(refundOrderId);

        step("Click Search");
        viewOrderDashboardPage.clickSearch();

        step("Action Button Should Be Shown With Its Items");
        Assertion.assertThat("Order Detail Box is Loaded",viewOrderDashboardPage.isOrderDetailBoxLoaded(),is(true));
        Assertion.assertThat("Order Id is Present",viewOrderDashboardPage.getOrderId(),containsString(refundOrderId));
        Assertion.assertThat("Action column is Present",viewOrderDashboardPage.isActionPaneLoaded(),is(true));
        viewOrderDashboardPage.clickMoreActionButton();
        Assertion.assertThat("More Action Box is Loaded",viewOrderDashboardPage.isMoreActionBoxLoaded(),is(true));
        Assertion.assertThat("Edit Booking is visible",viewOrderDashboardPage.isEditBookingVisible(),containsString("Edit Booking"));
        Assertion.assertThat("Cancel Booking is visible",viewOrderDashboardPage.isCancelBookingVisible(),containsString("Cancel Booking"));

        viewOrderDashboardPage.clickMoreActionButton();
        step("Input Failed Order Id");
        viewOrderDashboardPage.setOrderIdInput(failedOrderId);

        step("Click Search");
        viewOrderDashboardPage.clickSearch();

        step("Action Button Should Be Shown With Its Items");
        Assertion.assertThat("Order Detail Box is Loaded",viewOrderDashboardPage.isOrderDetailBoxLoaded(),is(true));
        Assertion.assertThat("Order Id is Present",viewOrderDashboardPage.getOrderId(),containsString(failedOrderId));
        Assertion.assertThat("Action column is Present",viewOrderDashboardPage.isActionPaneLoaded(),is(true));
        viewOrderDashboardPage.clickMoreActionButton();
        Assertion.assertThat("More Action Box is Loaded",viewOrderDashboardPage.isMoreActionBoxLoaded(),is(true));
        Assertion.assertThat("Edit Booking is visible",viewOrderDashboardPage.isEditBookingVisible(),containsString("Edit Booking"));
        Assertion.assertThat("Cancel Booking is visible",viewOrderDashboardPage.isCancelBookingVisible(),containsString("Cancel Booking"));

    }
    @Author(names = {"Venkat","Riya Bajpai"})
    @Module(name = "ViewOrder")
    @Tribe(name = "Demand")
    @Vertical(name = "Accommodation")
    @Test
    public void testOrderStatusForDifferentBookings() {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        ViewOrderDashboardPage<?> viewOrderDashboardPage = new ViewOrderDashboardPage<>(session);
        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<>(session);
        orderCreationPage<?> orderCreationPage = new orderCreationPage<>(session);
        Map<String, String> param = getParam("testOrderStatusForDifferentBookings");
        BaseTest.paramsThreadLocal.set(param);

        String email = param.get("email");
        String password = param.get("password");
        String url = param.get("url");
        String refundOrderId = param.get("refundOrderId");
        String failedOrderId = param.get("failedOrderId");
        String cancelledOrderId = param.get("cancelledOrderId");
        String itineraryIdRefund = param.get("itineraryIdRefund");
        String itineraryIdFailed = param.get("itineraryIdFailed");
        String itineraryIdCancelled = param.get("itineraryIdCancelled");

        String username = param.get("username");
        String hotelPassword = param.get("hotelPassword");
        String startDateRaw = param.get("startDate").trim();
        String startDate = startDateRaw.split("-").length >= 3 ? startDateRaw : generateRandomDate(startDateRaw);
        String hotelName = param.get("hotelName");
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));
        int room = Integer.parseInt(param.get("room"));
        String currency=param.get("currency");
        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autoguest";
        String guestTitle = salutationList[0];

        step("Create new hotel issued order");
        orderCreationPage.createHotelSakukuIssuedOrderApi(username, hotelPassword, startDate, hotelName,
                adult, night, room, orderFullName, orderNameTitle, guestFullName, guestTitle,currency);

        step("Get data from the issued order");
        String SuccessfulOrderID = String.valueOf(orderCreationPage.getOrderId());

        step("Go to member login page");
        loginMemberAdmin.gotoHome();

        step("Log In");
        boolean isLoggedInMemberAdmin = loginMemberAdmin.isUserLoggedIn(email,password);

        step("Assert that user is logged in to Member Admin");
        Assertion.assertThat("User is logged in", isLoggedInMemberAdmin, is(true));

        step("Go to View Order Dashboard");
        loginMemberAdmin.goToDashboard(url);
        Assertion.assertThat("Is Search Order Detail Box Loaded",viewOrderDashboardPage.isSearchOrderDetailBoxLoaded(), is(true));

        step("Click Source Dropdown");
        viewOrderDashboardPage.clickSourceDropdown();

        step("Select Source Order ID");
        viewOrderDashboardPage.selectSourceOrderID();
        Assertion.assertThat("Source Order ID is selected", viewOrderDashboardPage.checkSelectedSource("Order ID"), is(true));

        step("Input Order Id");
        viewOrderDashboardPage.setOrderIdInput(SuccessfulOrderID);

        step("Click Search");
        viewOrderDashboardPage.clickSearch();

        step("Order Status Should be eticket issued");
        Assertion.assertThat("Order Detail Box is Loaded",viewOrderDashboardPage.isOrderDetailBoxLoaded(),is(true));
        Assertion.assertThat("Order Id is Present",viewOrderDashboardPage.getOrderId(),containsString(SuccessfulOrderID));
        Assertion.assertThat("Order Status is E-Ticket Issued",viewOrderDashboardPage.getSuccessOrderStatus(),containsString("E-ticket issued"));
        Assertion.assertThat("Itinerary Id is present", viewOrderDashboardPage.isOrderDetailItineraryIdPresent(), is(true));

        step("Input Order Id");
        viewOrderDashboardPage.setOrderIdInput(refundOrderId);

        step("Click Search");
        viewOrderDashboardPage.clickSearch();

        step("Order Status Should be Refunded");
        Assertion.assertThat("Order Detail Box is Loaded",viewOrderDashboardPage.isOrderDetailBoxLoaded(),is(true));
        Assertion.assertThat("Order Id is Present",viewOrderDashboardPage.getOrderId(),containsString(refundOrderId));
        Assertion.assertThat("Order Status is Refunded",viewOrderDashboardPage.getSuccessOrderStatus(),containsString("Refunded"));
        Assertion.assertThat("Itinerary Id is present", viewOrderDashboardPage.isOrderDetailItineraryIdPresent(), is(true));
        Assertion.assertThat("Itinerary Id matches expected", viewOrderDashboardPage.getItinid(), containsString(itineraryIdRefund.trim()));


        step("Input Order Id");
        viewOrderDashboardPage.setOrderIdInput(failedOrderId);

        step("Click Search");
        viewOrderDashboardPage.clickSearch();

        step("Order Status Should be Failed");
        Assertion.assertThat("Order Detail Box is Loaded",viewOrderDashboardPage.isOrderDetailBoxLoaded(),is(true));
        Assertion.assertThat("Order Id is Present",viewOrderDashboardPage.getOrderId(),containsString(failedOrderId));
        Assertion.assertThat("Order Status is Booking Unsuccessful",viewOrderDashboardPage.getFailedOrderStaus(),containsString("Booking unsuccessful"));
        Assertion.assertThat("Itinerary Id is present", viewOrderDashboardPage.isOrderDetailItineraryIdPresent(), is(true));
        Assertion.assertThat("Itinerary Id matches expected", viewOrderDashboardPage.getItinid(), containsString(itineraryIdFailed.trim()));


        step("Input Order Id");
        viewOrderDashboardPage.setOrderIdInput(cancelledOrderId);

        step("Click Search");
        viewOrderDashboardPage.clickSearch();

        step("Order Status Should be Cancelled");
        Assertion.assertThat("Order Detail Box is Loaded",viewOrderDashboardPage.isOrderDetailBoxLoaded(),is(true));
        Assertion.assertThat("Order Id is Present",viewOrderDashboardPage.getOrderId(),containsString(cancelledOrderId));
        Assertion.assertThat("Order Status is Cancelled",viewOrderDashboardPage.getCancelledOrderStatus(),containsString("Cancelled"));
        Assertion.assertThat("Itinerary Id is present", viewOrderDashboardPage.isOrderDetailItineraryIdPresent(), is(true));
        Assertion.assertThat("Itinerary Id matches expected", viewOrderDashboardPage.getItinid(), containsString(itineraryIdCancelled.trim()));

    }
    @Author(names = "Venkat")
    @Module(name = "ViewOrder")
    @Tribe(name = "Demand")
    @Vertical(name = "Accommodation")
    @Test
    public void VerifyResendCancellationEmail() throws FailedLoginException, InterruptedException {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        ViewOrderDashboardPage<?> viewOrderDashboardPage = new ViewOrderDashboardPage<>(session);
        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<>(session);
        Map<String, String> param = getParam("VerifyResendCancellationEmail");
        BaseTest.paramsThreadLocal.set(param);

        String username = param.get("username");
        String password = param.get("password");
        String url = param.get("url");
        String refundedOrderID= param.get("refundedOrderID");

        step("Using refunded order ID: " + refundedOrderID);

        step("Go to member login page");
        loginMemberAdmin.gotoHome();

        step("Log In");
        boolean isLoggedInMemberAdmin = loginMemberAdmin.isUserLoggedIn(username, password);

        step("Assert that user is logged in to Member Admin");
        Assertion.assertThat("User is logged in", isLoggedInMemberAdmin, is(true));

        step("Go to View Order Dashboard");
        loginMemberAdmin.goToDashboard(url);
        Assertion.assertThat("Is Search Order Detail Box Loaded", viewOrderDashboardPage.isSearchOrderDetailBoxLoaded(), is(true));

        step("Click Source Dropdown");
        viewOrderDashboardPage.clickSourceDropdown();

        step("Select Source Order ID");
        viewOrderDashboardPage.selectSourceOrderID();
        Assertion.assertThat("Source Order ID is selected", viewOrderDashboardPage.checkSelectedSource("Order ID"), is(true));

        step("Input Order Id");
        viewOrderDashboardPage.setOrderIdInput(refundedOrderID);

        step("Click Search");
        viewOrderDashboardPage.clickSearch();

        step("Every Order Details Should be Visible");
        Assertion.assertThat("Order Detail Box is Loaded", viewOrderDashboardPage.isOrderDetailBoxLoaded(), is(true));
        Assertion.assertThat("Order Id is Present", viewOrderDashboardPage.getOrderId(), containsString(refundedOrderID));

        step("Action Button Should Be Shown With Its Items");
        Assertion.assertThat("Action column is Present", viewOrderDashboardPage.isActionPaneLoaded(), is(true));
        viewOrderDashboardPage.clickMoreActionButton();

        step("Resend Partner Hotel Cancellation Email dropdown is shown");
        Assertion.assertThat("Resend Email is Present", viewOrderDashboardPage.isResendEmailDisplayed(), is(true));

        viewOrderDashboardPage.clickResendCancEmail();
        Assertion.assertThat("Verify Snackbar text is Correct", viewOrderDashboardPage.getSnackbarText(), is("Refund email has been resent successfully"));

    }
}
