package com.tiket.web.dashboard.test.hotel.demand;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.web.dashboard.page.common.LoginMemberAdmin;
import com.tiket.web.dashboard.page.hotel.demand.FailedIssuedDashboardPage;
import com.tiket.web.dashboard.page.hotel.demand.FailedCheckinDashboardPage;
import com.tiket.web.dashboard.page.hotel.demand.ViewOrderDashboardPage;
import com.tiket.web.dashboard.page.order.orderCreationPage;
import org.testng.annotations.Test;

import java.util.Map;

import static com.tiket.core.CoreFunctions.*;
import static com.tiket.core.CoreFunctions.randomName;
import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.core.Is.is;

public class FailedCheckinDashboardTest extends BaseTest {

    @Author(names = {"Ketan Singh"})
    @Module(name = "Failed Checkin")
    @Tribe(name = "Demand")
    @Vertical(name = "Accommodation")
    @Test
    public void testPopupDisplayedWhenClickReportCheckIn() {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        Map<String, String> param = getParam("testPopupDisplayedWhenClickReportCheckIn");
        FailedCheckinDashboardPage<?> failedCheckinDashboardPage = new FailedCheckinDashboardPage<>(session);
        ViewOrderDashboardPage<?> viewOrderDashboardPage = new ViewOrderDashboardPage<>(session);
        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<>(session);
        orderCreationPage<?> orderCreationPage = new orderCreationPage<>(session);
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
        String currency="IDR";
        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autoguest";
        String guestTitle = salutationList[0];

        step("Create new hotel issued order");
        orderCreationPage.createHotelSakukuIssuedOrderApi(username, hotelpassword, startDate, hotelName,
                adult, night, room, orderFullName, orderNameTitle, guestFullName, guestTitle,currency);

        step("Get data from the issued order");
        String bookedOrderID = String.valueOf(orderCreationPage.getOrderId());

        step("Login via cookie injection");
        loginMemberAdmin.loginViaCookie(email, password);

        step("Go to View Order Dashboard");
        loginMemberAdmin.goToDashboard(url);
        Assertion.assertThat("Is Search Order Detail Box Loaded",viewOrderDashboardPage.isSearchOrderDetailBoxLoaded(), is(true));

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
        Assertion.assertThat("Order Detail Box is Loaded",viewOrderDashboardPage.isOrderDetailBoxLoaded(),is(true));
        Assertion.assertThat("Order Id is Present",viewOrderDashboardPage.getOrderId(),containsString(bookedOrderID));
        Assertion.assertThat("Action column is Present",viewOrderDashboardPage.isActionPaneLoaded(),is(true));
        viewOrderDashboardPage.clickMoreActionButton();
        Assertion.assertThat("More Action Box is Loaded",viewOrderDashboardPage.isMoreActionBoxLoaded(),is(true));
        Assertion.assertThat("Report Failed Checkin is Visible",viewOrderDashboardPage.isReportFailedCheckinVisible(),containsString("Report Failed Checkin"));
        failedCheckinDashboardPage.clickReportFailedCheckinButton();

        step("Check Popup Is Displayed");
        Assertion.assertThat("Confirmation Popup Box Is Loaded",failedCheckinDashboardPage.isConfirmationPopupBoxLoaded(),is(true));
        Assertion.assertThat("Category Option Is Visible",failedCheckinDashboardPage.isCategoryVisible(),is(true));
        Assertion.assertThat("CheckIn Option is Visible",failedCheckinDashboardPage.isCheckInProblemVisible(),is(true));
        Assertion.assertThat("Reported From Option is Visible",failedCheckinDashboardPage.isReportedFromVisible(),is(true));
        Assertion.assertThat("Root Cause Option is Visible",failedCheckinDashboardPage.isRootCauseVisible(),is(true));
    }

    @Author(names = {"Ketan Singh"})
    @Module(name = "Failed Checkin")
    @Tribe(name = "Demand")
    @Vertical(name = "Accommodation")
    @Test
    public void testDropDownListShownWhenClickReportFailedCheckIn() {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        Map<String, String> param = getParam("testDropDownListShownWhenClickReportFailedCheckIn");
        FailedCheckinDashboardPage<?> failedCheckinDashboardPage = new FailedCheckinDashboardPage<>(session);
        ViewOrderDashboardPage<?> viewOrderDashboardPage = new ViewOrderDashboardPage<>(session);
        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<>(session);
        orderCreationPage<?> orderCreationPage = new orderCreationPage<>(session);
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
        String currency="IDR";
        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autoguest";
        String guestTitle = salutationList[0];

        step("Create new hotel issued order");
        orderCreationPage.createHotelSakukuIssuedOrderApi(username, hotelpassword, startDate, hotelName,
                adult, night, room, orderFullName, orderNameTitle, guestFullName, guestTitle,currency);

        step("Get data from the issued order");
        String bookedOrderID = String.valueOf(orderCreationPage.getOrderId());

        step("Login via cookie injection");
        loginMemberAdmin.loginViaCookie(email, password);

        step("Go to View Order Dashboard");
        loginMemberAdmin.goToDashboard(url);
        Assertion.assertThat("Is Search Order Detail Box Loaded",viewOrderDashboardPage.isSearchOrderDetailBoxLoaded(), is(true));

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
        Assertion.assertThat("Order Detail Box is Loaded",viewOrderDashboardPage.isOrderDetailBoxLoaded(),is(true));
        Assertion.assertThat("Order Id is Present",viewOrderDashboardPage.getOrderId(),containsString(bookedOrderID));
        Assertion.assertThat("Action column is Present",viewOrderDashboardPage.isActionPaneLoaded(),is(true));
        viewOrderDashboardPage.clickMoreActionButton();
        Assertion.assertThat("More Action Box is Loaded",viewOrderDashboardPage.isMoreActionBoxLoaded(),is(true));
        Assertion.assertThat("Report Failed Checkin is Visible",viewOrderDashboardPage.isReportFailedCheckinVisible(),containsString("Report Failed Checkin"));
        failedCheckinDashboardPage.clickReportFailedCheckinButton();

        step("Check Popup Is Displayed");
        Assertion.assertThat("Confirmation Popup Box Is Loaded",failedCheckinDashboardPage.isConfirmationPopupBoxLoaded(),is(true));
        Assertion.assertThat("Category Option Is Visible",failedCheckinDashboardPage.isCategoryVisible(),is(true));
        Assertion.assertThat("CheckIn Option is Visible",failedCheckinDashboardPage.isCheckInProblemVisible(),is(true));
        Assertion.assertThat("Reported From Option is Visible",failedCheckinDashboardPage.isReportedFromVisible(),is(true));
        Assertion.assertThat("Root Cause Option is Visible",failedCheckinDashboardPage.isRootCauseVisible(),is(true));

        step("Check Dropdown List For Category");
        failedCheckinDashboardPage.selectCategory();
        Assertion.assertThat("Category Dropdown Is Visible",failedCheckinDashboardPage.isCategoryDropdownVisible(),is(true));
        failedCheckinDashboardPage.selectFirstCategoryOption();
        Assertion.assertThat("First Category Option is selected and visible",failedCheckinDashboardPage.isFirstCategoryOptionVisible(),containsString("Barcode Voucher Sudah Digunakan atau Tidak Berlaku"));

        step("Check Dropdown List For CheckIn-Problem");
        failedCheckinDashboardPage.selectCheckInProblem();
        Assertion.assertThat("CheckIn Problem Dropdown Is Visible",failedCheckinDashboardPage.isCheckInProblemDropdownVisible(),is(true));
        failedCheckinDashboardPage.selectFirstCheckInProblemOption();
        Assertion.assertThat("First CheckIn Problem Option is selected and visible",failedCheckinDashboardPage.isFirstCheckInProblemOptionVisible(),containsString("Hotel Full Book"));

        step("Check Dropdown List For Reported From");
        failedCheckinDashboardPage.selectReportedFrom();
        Assertion.assertThat("Reported From Dropdown Is Visible",failedCheckinDashboardPage.isReportedFromDropdownVisible(),is(true));
        failedCheckinDashboardPage.selectFirstReportedFromOption();
        Assertion.assertThat("First Reported From Option is selected and visible",failedCheckinDashboardPage.isFirstReportedFromOptionVisible(),containsString("Customer"));

        step("Check Dropdown List For Root Cause");
        failedCheckinDashboardPage.selectRootCause();
        Assertion.assertThat("Root Cause Dropdown Is Visible",failedCheckinDashboardPage.isRootCauseDropdownVisible(),is(true));
        failedCheckinDashboardPage.selectFirstRootCauseOption();
        Assertion.assertThat("First Root Cause Option is selected and visible",failedCheckinDashboardPage.isFirstRootCauseOptionVisible(),containsString("CM tidak connect ke Extranet"));
    }

    @Author(names = {"Ketan Singh"})
    @Module(name = "Failed Checkin")
    @Tribe(name = "Demand")
    @Vertical(name = "Accommodation")
    @Test
    public void testOrderIdEditableOnFailedCheckIn() {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        Map<String, String> param = getParam("testOrderIdEditableOnFailedCheckIn");
        FailedCheckinDashboardPage<?> failedCheckinDashboardPage = new FailedCheckinDashboardPage<>(session);
        ViewOrderDashboardPage<?> viewOrderDashboardPage = new ViewOrderDashboardPage<>(session);
        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<>(session);
        orderCreationPage<?> orderCreationPage = new orderCreationPage<>(session);
        BaseTest.paramsThreadLocal.set(param);

        String email = param.get("email");
        String password = param.get("password");
        String url = param.get("url");
        String randomOrderId= param.get("randomOrderId");

        String username = "muhammad.yazid@tiket.com";
        String hotelpassword = "TestTiket@123";
        String startDate = generateRandomDate("24-30");
        String hotelName = "Carvens Hotel";
        int adult = 1;
        int night = 1;
        int room = 1;
        String currency="IDR";
        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autoguest";
        String guestTitle = salutationList[0];

        step("Create new hotel issued order");
        orderCreationPage.createHotelSakukuIssuedOrderApi(username, hotelpassword, startDate, hotelName,
                adult, night, room, orderFullName, orderNameTitle, guestFullName, guestTitle,currency);

        step("Get data from the issued order");
        String bookedOrderID = String.valueOf(orderCreationPage.getOrderId());

        step("Login via cookie injection");
        loginMemberAdmin.loginViaCookie(email, password);

        step("Go to View Order Dashboard");
        loginMemberAdmin.goToDashboard(url);
        Assertion.assertThat("Is Search Order Detail Box Loaded",viewOrderDashboardPage.isSearchOrderDetailBoxLoaded(), is(true));

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
        Assertion.assertThat("Order Detail Box is Loaded",viewOrderDashboardPage.isOrderDetailBoxLoaded(),is(true));
        Assertion.assertThat("Order Id is Present",viewOrderDashboardPage.getOrderId(),containsString(bookedOrderID));
        Assertion.assertThat("Action column is Present",viewOrderDashboardPage.isActionPaneLoaded(),is(true));
        viewOrderDashboardPage.clickMoreActionButton();
        Assertion.assertThat("More Action Box is Loaded",viewOrderDashboardPage.isMoreActionBoxLoaded(),is(true));
        Assertion.assertThat("Report Failed Checkin is Visible",viewOrderDashboardPage.isReportFailedCheckinVisible(),containsString("Report Failed Checkin"));
        failedCheckinDashboardPage.clickReportFailedCheckinButton();

        step("Check Popup Is Displayed");
        Assertion.assertThat("Confirmation Popup Box Is Loaded",failedCheckinDashboardPage.isConfirmationPopupBoxLoaded(),is(true));
        Assertion.assertThat("Category Option Is Visible",failedCheckinDashboardPage.isCategoryVisible(),is(true));
        Assertion.assertThat("CheckIn Option is Visible",failedCheckinDashboardPage.isCheckInProblemVisible(),is(true));
        Assertion.assertThat("Reported From Option is Visible",failedCheckinDashboardPage.isReportedFromVisible(),is(true));
        Assertion.assertThat("Root Cause Option is Visible",failedCheckinDashboardPage.isRootCauseVisible(),is(true));

        step("Check Dropdown List For Category");
        failedCheckinDashboardPage.selectCategory();
        Assertion.assertThat("Category Dropdown Is Visible",failedCheckinDashboardPage.isCategoryDropdownVisible(),is(true));
        failedCheckinDashboardPage.selectFirstCategoryOption();
        Assertion.assertThat("First Category Option is selected and visible",failedCheckinDashboardPage.isFirstCategoryOptionVisible(),containsString("Barcode Voucher Sudah Digunakan atau Tidak Berlaku"));

        step("Check Dropdown List For CheckIn-Problem");
        failedCheckinDashboardPage.selectCheckInProblem();
        Assertion.assertThat("CheckIn Problem Dropdown Is Visible",failedCheckinDashboardPage.isCheckInProblemDropdownVisible(),is(true));
        failedCheckinDashboardPage.selectFirstCheckInProblemOption();
        Assertion.assertThat("First CheckIn Problem Option is selected and visible",failedCheckinDashboardPage.isFirstCheckInProblemOptionVisible(),containsString("Hotel Full Book"));

        step("Check Dropdown List For Reported From");
        failedCheckinDashboardPage.selectReportedFrom();
        Assertion.assertThat("Reported From Dropdown Is Visible",failedCheckinDashboardPage.isReportedFromDropdownVisible(),is(true));
        failedCheckinDashboardPage.selectFirstReportedFromOption();
        Assertion.assertThat("First Reported From Option is selected and visible",failedCheckinDashboardPage.isFirstReportedFromOptionVisible(),containsString("Customer"));

        step("Check Dropdown List For Root Cause");
        failedCheckinDashboardPage.selectRootCause();
        Assertion.assertThat("Root Cause Dropdown Is Visible",failedCheckinDashboardPage.isRootCauseDropdownVisible(),is(true));
        failedCheckinDashboardPage.selectFirstRootCauseOption();
        Assertion.assertThat("First Root Cause Option is selected and visible",failedCheckinDashboardPage.isFirstRootCauseOptionVisible(),containsString("CM tidak connect ke Extranet"));

        step("Click Continue");
        failedCheckinDashboardPage.clickYesContinueButton();

        step("Check Failed checkIn Page Loaded");
        Assertion.assertThat("Failed Checkin Page Loaded",failedCheckinDashboardPage.isFailedCheckinPageLoaded(), is(true));
        Assertion.assertThat("Order Id Filter Is Loaded",failedCheckinDashboardPage.isOrderIdFilterLoaded(), is(true));

        step("Edit Order ID Value");
        failedCheckinDashboardPage.setOrderIdInput(randomOrderId);
        Assertion.assertThat("New OrderId Visible",failedCheckinDashboardPage.newEnteredOrderId(),containsString(randomOrderId));
    }
}
