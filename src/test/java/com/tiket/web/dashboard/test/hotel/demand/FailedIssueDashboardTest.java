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
import com.tiket.web.dashboard.page.hotel.demand.ViewOrderDashboardPage;
import com.tiket.web.dashboard.page.order.orderCreationPage;
import org.testng.annotations.Test;

import java.util.Map;

import static com.tiket.core.CoreFunctions.*;
import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.core.Is.is;

public class FailedIssueDashboardTest extends BaseTest {

    @Author(names = {"Ketan Singh"})
    @Module(name = "Failed Issued")
    @Tribe(name = "Demand")
    @Vertical(name = "Accommodation")
    @Test
    public void testFailedIssuedUsingOrderId() {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        ViewOrderDashboardPage<?> viewOrderDashboardPage = new ViewOrderDashboardPage<>(session);
        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<>(session);
        FailedIssuedDashboardPage<?> failedIssuedDashboardPage = new FailedIssuedDashboardPage<>(session);
        Map<String, String> param = getParam("testFailedIssuedUsingOrderId");
        BaseTest.paramsThreadLocal.set(param);

        String email = param.get("email");
        String password = param.get("password");
        String url = param.get("url");
        String orderId = param.get("orderId");

        step("Login via cookie injection");
        loginMemberAdmin.loginViaCookie(email, password);

        step("Go to Failed Issued Dashboard");
        loginMemberAdmin.goToDashboard(url);
        Assertion.assertThat("Is Failed Issued Dashboard Page Loaded",failedIssuedDashboardPage.isFailedIssuedDashboardPageLoaded(), is(true));

        step("Input Order Id");
        failedIssuedDashboardPage.inputOrderId(orderId);

        step("Click Search");
        failedIssuedDashboardPage.clickSearch();

        step("Every Order Details Should be Visible");
        Assertion.assertThat("Index is Present",failedIssuedDashboardPage.isIndexLoaded(),is(true));
        Assertion.assertThat("Order Id is Present",failedIssuedDashboardPage.isOrderIdBoxLoaded(),containsString(orderId));
        Assertion.assertThat("Order Details Id is Present",failedIssuedDashboardPage.isOrderDetailsLoaded(),is(true));
        Assertion.assertThat("Tag/Refrence Id is Present",failedIssuedDashboardPage.isRefrenceLoaded(),is(true));
        Assertion.assertThat("Source is Present",failedIssuedDashboardPage.isSourceLoaded(),is(true));
        Assertion.assertThat("Payment Status is Present",failedIssuedDashboardPage.isPaymentStatusLoaded(),is(true));
        Assertion.assertThat("Payment Option is Present",failedIssuedDashboardPage.isPaymentOptionLoaded(),is(true));
        Assertion.assertThat("Hotel Name is Present",failedIssuedDashboardPage.isHotelNameLoaded(),is(true));
        Assertion.assertThat("Booking Date is Present",failedIssuedDashboardPage.isBookingDateLoaded(),is(true));
        Assertion.assertThat("Checkin is Present",failedIssuedDashboardPage.isCheckinLoaded(),is(true));
        Assertion.assertThat("Checkout is Present",failedIssuedDashboardPage.isCheckOutLoaded(),is(true));
        Assertion.assertThat("Total Night is Present",failedIssuedDashboardPage.isTotalNightLoaded(),is(true));
        Assertion.assertThat("Guest Name is Present",failedIssuedDashboardPage.isGuestNameLoaded(),is(true));
        Assertion.assertThat("Total Guest is Present",failedIssuedDashboardPage.isTotalGuestLoaded(),is(true));
        Assertion.assertThat("Total Room is Present",failedIssuedDashboardPage.isTotalRoomLoaded(),is(true));
        Assertion.assertThat("Order Issued is Present",failedIssuedDashboardPage.isOrderIssuedLoaded(),is(true));
        Assertion.assertThat("Total Price is Present",failedIssuedDashboardPage.isTotalPriceLoaded(),is(true));
        Assertion.assertThat("Status is Present",failedIssuedDashboardPage.isStatusLoaded(),is(true));
        Assertion.assertThat("Reason is Present",failedIssuedDashboardPage.isReasonLoaded(),is(true));
        Assertion.assertThat("Action column is Present",failedIssuedDashboardPage.isActionColumnLoaded(),is(true));
    }

    @Author(names = {"Ketan Singh"})
    @Module(name = "Failed Issued")
    @Tribe(name = "Demand")
    @Vertical(name = "Accommodation")
    @Test
    public void testActionColumnForFailedIssuedOrder() {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        ViewOrderDashboardPage<?> viewOrderDashboardPage = new ViewOrderDashboardPage<>(session);
        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<>(session);
        FailedIssuedDashboardPage<?> failedIssuedDashboardPage = new FailedIssuedDashboardPage<>(session);
        Map<String, String> param = getParam("testActionColumnForFailedIssuedOrder");
        BaseTest.paramsThreadLocal.set(param);

        String email = param.get("email");
        String password = param.get("password");
        String url = param.get("url");
        String orderId = param.get("orderId");

        step("Login via cookie injection");
        loginMemberAdmin.loginViaCookie(email, password);

        step("Go to Failed Issued Dashboard");
        loginMemberAdmin.goToDashboard(url);
        Assertion.assertThat("Is Failed Issued Dashboard Page Loaded",failedIssuedDashboardPage.isFailedIssuedDashboardPageLoaded(), is(true));

        step("Input Order Id");
        failedIssuedDashboardPage.inputOrderId(orderId);

        step("Click Search");
        failedIssuedDashboardPage.clickSearch();

        step("Every Action Column Button should be Visible");
        Assertion.assertThat("Order Id is Present",failedIssuedDashboardPage.isOrderIdBoxLoaded(),containsString(orderId));
        Assertion.assertThat("Action  is Present",failedIssuedDashboardPage.isActionColumnLoaded(),is(true));
        Assertion.assertThat("Rebooking Button  is Present",failedIssuedDashboardPage.isRebookingButtonLoaded(),is(true));
        Assertion.assertThat("Relocation Button  is Present",failedIssuedDashboardPage.isRelocationButtonLoaded(),is(true));
        Assertion.assertThat("Remove From List Button is Present",failedIssuedDashboardPage.isRemoveFromListButtonLoaded(),is(true));
    }

    @Author(names = {"Ketan Singh"})
    @Module(name = "Failed Issued")
    @Tribe(name = "Demand")
    @Vertical(name = "Accommodation")
    @Test
    public void testActionColumnButtonsAreClickable() {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        ViewOrderDashboardPage<?> viewOrderDashboardPage = new ViewOrderDashboardPage<>(session);
        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<>(session);
        FailedIssuedDashboardPage<?> failedIssuedDashboardPage = new FailedIssuedDashboardPage<>(session);
        Map<String, String> param = getParam("testActionColumnButtonsAreClickable");
        BaseTest.paramsThreadLocal.set(param);

        String email = param.get("email");
        String password = param.get("password");
        String url = param.get("url");
        String orderId = param.get("orderId");

        step("Login via cookie injection");
        loginMemberAdmin.loginViaCookie(email, password);

        step("Go to Failed Issued Dashboard");
        loginMemberAdmin.goToDashboard(url);
        Assertion.assertThat("Is Failed Issued Dashboard Page Loaded",failedIssuedDashboardPage.isFailedIssuedDashboardPageLoaded(), is(true));

        step("Input Order Id");
        failedIssuedDashboardPage.inputOrderId(orderId);

        step("Click Search");
        failedIssuedDashboardPage.clickSearch();

        step("Every Action Column Button should be Visible");
        Assertion.assertThat("Order Id is Present",failedIssuedDashboardPage.isOrderIdBoxLoaded(),containsString(orderId));
        Assertion.assertThat("Action  is Present",failedIssuedDashboardPage.isActionColumnLoaded(),is(true));
        Assertion.assertThat("Rebooking Button  is clickable",failedIssuedDashboardPage.isReebookingButtonClickable(),is(true));
        Assertion.assertThat("Relocation Button  is clickable",failedIssuedDashboardPage.isRelocationButtonClickable(),is(true));
        Assertion.assertThat("Remove From List Button is clickable",failedIssuedDashboardPage.isRemoveFromListButtonClickable(),is(true));
    }

    @Author(names = {"Ketan Singh"})
    @Module(name = "Failed Issued")
    @Tribe(name = "Demand")
    @Vertical(name = "Accommodation")
    @Test
    public void testPopUpButtonsAreClickable() {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        ViewOrderDashboardPage<?> viewOrderDashboardPage = new ViewOrderDashboardPage<>(session);
        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<>(session);
        FailedIssuedDashboardPage<?> failedIssuedDashboardPage = new FailedIssuedDashboardPage<>(session);
        Map<String, String> param = getParam("testPopUpButtonsAreClickable");
        BaseTest.paramsThreadLocal.set(param);

        String email = param.get("email");
        String password = param.get("password");
        String url = param.get("url");
        String orderId = param.get("orderId");

        step("Login via cookie injection");
        loginMemberAdmin.loginViaCookie(email, password);

        step("Go to Failed Issued Dashboard");
        loginMemberAdmin.goToDashboard(url);
        Assertion.assertThat("Is Failed Issued Dashboard Page Loaded",failedIssuedDashboardPage.isFailedIssuedDashboardPageLoaded(), is(true));

        step("Input Order Id");
        failedIssuedDashboardPage.inputOrderId(orderId);

        step("Click Search");
        failedIssuedDashboardPage.clickSearch();

        step("Every Action Column Button should be Visible");
        Assertion.assertThat("Order Id is Present",failedIssuedDashboardPage.isOrderIdBoxLoaded(),containsString(orderId));
        Assertion.assertThat("Action column is Present",failedIssuedDashboardPage.isActionColumnLoaded(),is(true));
        failedIssuedDashboardPage.clickRemoveFromListButton();
        Assertion.assertThat("Remove Confirmation Popup Is Loaded",failedIssuedDashboardPage.isRemoveConfirmationPopupLoaded(),is(true));
        failedIssuedDashboardPage.clickBookingStatusDropdown();
        failedIssuedDashboardPage.selectRefunded();
        failedIssuedDashboardPage.clickReasonDropdown();
        failedIssuedDashboardPage.selectIssued();
        Assertion.assertThat("Yes Button is Clickable",failedIssuedDashboardPage.isYesButtonClickable(),is(true));
        Assertion.assertThat("No Button is Clickable",failedIssuedDashboardPage.isNoButtonClickable(),is(true));
    }

    @Author(names = {"Ketan Singh", "Vipul Kumar"})
    @Module(name = "Failed Issued")
    @Tribe(name = "Demand")
    @Vertical(name = "Accommodation")
    @Test
    public void testRoomsAreShownWhenRebook() {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<>(session);
        FailedIssuedDashboardPage<?> failedIssuedDashboardPage = new FailedIssuedDashboardPage<>(session);
        orderCreationPage<?> orderCreationPage = new orderCreationPage<>(session);
        Map<String, String> param = getParam("testRoomsAreShownWhenRebook");
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

        step("Go to Failed Issued Dashboard");
        loginMemberAdmin.goToDashboard(url);
        Assertion.assertThat("Is Failed Issued Dashboard Page Loaded",failedIssuedDashboardPage.isFailedIssuedDashboardPageLoaded(), is(true));

        step("Input Order Id");
        failedIssuedDashboardPage.inputOrderId(bookedOrderID);

        step("Click Search");
        failedIssuedDashboardPage.clickSearch();

        step("Rebook Button is Loaded");
        Assertion.assertThat("Order Id is Present",failedIssuedDashboardPage.isOrderIdBoxLoaded(),containsString(bookedOrderID));
        Assertion.assertThat("Action  is Present",failedIssuedDashboardPage.isActionColumnLoaded(),is(true));
        Assertion.assertThat("Rebooking Button  is Present",failedIssuedDashboardPage.isRebookingButtonLoaded(),is(true));

        step("Click Rebooking button");
        failedIssuedDashboardPage.clickRebookingButton();

        step("check Rooms is Present");
        Assertion.assertThat("Room is shown",failedIssuedDashboardPage.isRoomLoaded(),is(true));
        Assertion.assertThat("Room Details is shown",failedIssuedDashboardPage.isRoomImageLoaded(),is(true));
        Assertion.assertThat("Room Name is shown",failedIssuedDashboardPage.isRoomNameLoaded(),is(true));
        Assertion.assertThat("Room Price is shown",failedIssuedDashboardPage.isRoomPriceLoaded(),is(true));

    }

    @Author(names = {"Ketan Singh"})
    @Module(name = "Failed Issued")
    @Tribe(name = "Demand")
    @Vertical(name = "Accommodation")
    @Test
    public void testPopupIsShownWhenClickContiueBooking() {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<>(session);
        FailedIssuedDashboardPage<?> failedIssuedDashboardPage = new FailedIssuedDashboardPage<>(session);
        orderCreationPage<?> orderCreationPage = new orderCreationPage<>(session);
        Map<String, String> param = getParam("testPopupIsShownWhenClickContiueBooking");
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

        step("Go to Failed Issued Dashboard");
        loginMemberAdmin.goToDashboard(url);
        Assertion.assertThat("Is Failed Issued Dashboard Page Loaded",failedIssuedDashboardPage.isFailedIssuedDashboardPageLoaded(), is(true));

        step("Input Order Id");
        failedIssuedDashboardPage.inputOrderId(bookedOrderID);

        step("Click Search");
        failedIssuedDashboardPage.clickSearch();

        step("Rebook Button is Loaded");
        Assertion.assertThat("Order Id is Present",failedIssuedDashboardPage.isOrderIdBoxLoaded(),containsString(bookedOrderID));
        Assertion.assertThat("Action  is Present",failedIssuedDashboardPage.isActionColumnLoaded(),is(true));
        Assertion.assertThat("Rebooking Button  is Present",failedIssuedDashboardPage.isRebookingButtonLoaded(),is(true));

        step("Click Rebooking button");
        failedIssuedDashboardPage.clickRebookingButton();

        step("check Rooms is Present");
        Assertion.assertThat("Room is shown",failedIssuedDashboardPage.isRoomLoaded(),is(true));
        Assertion.assertThat("Room Details is shown",failedIssuedDashboardPage.isRoomImageLoaded(),is(true));
        Assertion.assertThat("Room Name is shown",failedIssuedDashboardPage.isRoomNameLoaded(),is(true));
        Assertion.assertThat("Room Price is shown",failedIssuedDashboardPage.isRoomPriceLoaded(),is(true));
        Assertion.assertThat("Select button is clickable", failedIssuedDashboardPage.isSelectButtonClickable(),is(true));

        step("Click Select button");
        failedIssuedDashboardPage.clickSelectButton();

        step("Check New Hotel Page is opened");
        Assertion.assertThat("New Hotel is shown",failedIssuedDashboardPage.isNewHotelPageLoaded(),is(true));
        Assertion.assertThat("Terms and Condition Pane is loaded",failedIssuedDashboardPage.isTermsAndConditionLoaded(),is(true));
        Assertion.assertThat("Checkbox is loaded",failedIssuedDashboardPage.isCheckBoxLoaded(),is(true));

        step("click checkbox accepting terms and condition");
        failedIssuedDashboardPage.clickCheckBoxButton();

        step("Click Continue Booking Button");
        Assertion.assertThat("Continue Booking is Clickable",failedIssuedDashboardPage.isContinueBookingButtonClickable(),is(true));
        failedIssuedDashboardPage.clickContinueBookingButton();
        Assertion.assertThat("PopUp is Loaded",failedIssuedDashboardPage.isRebookPopUpLoaded(),is(true));
    }

    @Author(names = {"Raihan"})
    @Module(name = "FailedIssued")
    @Tribe(name = "Demand")
    @Vertical(name = "Accommodation")
    @Test
    public void testCheckIfTheHotelsAreBeingAbleToSelect () {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<>(session);
        FailedIssuedDashboardPage<?> failedIssuedDashboardPage = new FailedIssuedDashboardPage<>(session);
        Map<String, String> param = getParam("testCheckIfTheHotelsAreBeingAbleToSelect");
        BaseTest.paramsThreadLocal.set(param);

        String email = param.get("email");
        String password = param.get("password");
        String url = param.get("url");
        String orderId = param.get("orderId");

        step("Login via cookie injection");
        loginMemberAdmin.loginViaCookie(email, password);

        step("Go to Failed Issued Dashboard");
        loginMemberAdmin.goToDashboard(url);
        Assertion.assertThat("Is Failed Issued Dashboard Page Loaded",failedIssuedDashboardPage.isFailedIssuedDashboardPageLoaded(), is(true));

        step("Input Order Id");
        failedIssuedDashboardPage.inputOrderId(orderId);

        step("Click Search");
        failedIssuedDashboardPage.clickSearch();

        step("Rebook Button is Loaded");
        Assertion.assertThat("Order Id is Present",failedIssuedDashboardPage.isOrderIdBoxLoaded(),containsString(orderId));
        Assertion.assertThat("Action  is Present",failedIssuedDashboardPage.isActionColumnLoaded(),is(true));
        Assertion.assertThat("Relocation Button  is Present",failedIssuedDashboardPage.isRelocationButtonClickable(),is(true));

        step("Click Relocation button");
        failedIssuedDashboardPage.clickRelocationButton();

        step("check Hotels is Present");
        Assertion.assertThat("Hotel list is present", failedIssuedDashboardPage.isRelocationHotelListDisplayed(), is(true));

        step("Click hotel");
        failedIssuedDashboardPage.selectRelocationHotel();

        step("check Rooms is Present");
        Assertion.assertThat("Room is shown",failedIssuedDashboardPage.isRoomLoaded(),is(true));
        Assertion.assertThat("Room Details is shown",failedIssuedDashboardPage.isRoomImageLoaded(),is(true));
        Assertion.assertThat("Room Name is shown",failedIssuedDashboardPage.isRoomNameLoaded(),is(true));
        Assertion.assertThat("Room Price is shown",failedIssuedDashboardPage.isRoomPriceLoaded(),is(true));
        Assertion.assertThat("Select button is clickable", failedIssuedDashboardPage.isSelectButtonClickable(),is(true));
    }

    @Author(names = {"Raihan"})
    @Module(name = "FailedIssued")
    @Tribe(name = "Demand")
    @Vertical(name = "Accommodation")
    @Test
    public void testCheckIfUserIsAbleToSelectTheRoomOnRoomListPage() {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<>(session);
        FailedIssuedDashboardPage<?> failedIssuedDashboardPage = new FailedIssuedDashboardPage<>(session);
        Map<String, String> param = getParam("testCheckIfUserIsAbleToSelectTheRoomOnRoomListPage");
        BaseTest.paramsThreadLocal.set(param);

        String email = param.get("email");
        String password = param.get("password");
        String url = param.get("url");
        String orderId = param.get("orderId");

        step("Login via cookie injection");
        loginMemberAdmin.loginViaCookie(email, password);

        step("Go to Failed Issued Dashboard");
        loginMemberAdmin.goToDashboard(url);
        Assertion.assertThat("Is Failed Issued Dashboard Page Loaded",failedIssuedDashboardPage.isFailedIssuedDashboardPageLoaded(), is(true));

        step("Input Order Id");
        failedIssuedDashboardPage.inputOrderId(orderId);

        step("Click Search");
        failedIssuedDashboardPage.clickSearch();

        step("Rebook Button is Loaded");
        Assertion.assertThat("Order Id is Present",failedIssuedDashboardPage.isOrderIdBoxLoaded(),containsString(orderId));
        Assertion.assertThat("Action  is Present",failedIssuedDashboardPage.isActionColumnLoaded(),is(true));
        Assertion.assertThat("Relocation Button  is Present",failedIssuedDashboardPage.isRelocationButtonClickable(),is(true));

        step("Click Relocation button");
        failedIssuedDashboardPage.clickRelocationButton();

        step("check Hotels is Present");
        Assertion.assertThat("Hotel list is present", failedIssuedDashboardPage.isRelocationHotelListDisplayed(), is(true));

        step("Click hotel");
        failedIssuedDashboardPage.selectRelocationHotel();

        step("check Rooms is Present");
        Assertion.assertThat("Room is shown",failedIssuedDashboardPage.isRoomLoaded(),is(true));
        Assertion.assertThat("Room Details is shown",failedIssuedDashboardPage.isRoomImageLoaded(),is(true));
        Assertion.assertThat("Room Name is shown",failedIssuedDashboardPage.isRoomNameLoaded(),is(true));
        Assertion.assertThat("Room Price is shown",failedIssuedDashboardPage.isRoomPriceLoaded(),is(true));
        Assertion.assertThat("Select button is clickable", failedIssuedDashboardPage.isSelectButtonClickable(),is(true));

        step("Click Select Room button");
        failedIssuedDashboardPage.clickSelectRoom();
        Assertion.assertThat("Continue Booking is Clickable",failedIssuedDashboardPage.isContinueBookingButtonClickable(),is(true));
    }

    @Author(names = {"Raihan"})
    @Module(name = "FailedIssued")
    @Tribe(name = "Demand")
    @Vertical(name = "Accommodation")
    @Test
    public void testCheckIfTheGetRefundSectionIsClickable() {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<>(session);
        FailedIssuedDashboardPage<?> failedIssuedDashboardPage = new FailedIssuedDashboardPage<>(session);
        Map<String, String> param = getParam("testCheckIfTheGetRefundSectionIsClickable");
        BaseTest.paramsThreadLocal.set(param);

        String email = param.get("email");
        String password = param.get("password");
        String url = param.get("url");
        String orderId = param.get("orderId");
        String hotelName = param.get("hotelName");

        step("Login via cookie injection");
        loginMemberAdmin.loginViaCookie(email, password);

        step("Go to Failed Issued Dashboard");
        loginMemberAdmin.goToDashboard(url);
        Assertion.assertThat("Is Failed Issued Dashboard Page Loaded",failedIssuedDashboardPage.isFailedIssuedDashboardPageLoaded(), is(true));

        step("Input Order Id");
        failedIssuedDashboardPage.inputOrderId(orderId);

        step("Click Search");
        failedIssuedDashboardPage.clickSearch();

        step("Rebook Button is Loaded");
        Assertion.assertThat("Order Id is Present",failedIssuedDashboardPage.isOrderIdBoxLoaded(),containsString(orderId));
        Assertion.assertThat("Action  is Present",failedIssuedDashboardPage.isActionColumnLoaded(),is(true));
        Assertion.assertThat("Relocation Button  is Present",failedIssuedDashboardPage.isRelocationButtonClickable(),is(true));

        step("Click Relocation button");
        failedIssuedDashboardPage.clickRelocationButton();

        step("check Hotels is Present");
        Assertion.assertThat("Hotel list is present", failedIssuedDashboardPage.isRelocationHotelListDisplayed(), is(true));

        step("Click hotel");
        failedIssuedDashboardPage.selectRelocationHotelByHotelName(hotelName);

        step("check Rooms is Present");
        Assertion.assertThat("Room is shown",failedIssuedDashboardPage.isRoomLoaded(),is(true));
        Assertion.assertThat("Room Details is shown",failedIssuedDashboardPage.isRoomImageLoaded(),is(true));
        Assertion.assertThat("Room Name is shown",failedIssuedDashboardPage.isRoomNameLoaded(),is(true));
        Assertion.assertThat("Room Price is shown",failedIssuedDashboardPage.isRoomPriceLoaded(),is(true));
        Assertion.assertThat("Select button is clickable", failedIssuedDashboardPage.isSelectButtonClickable(),is(true));

        step("Select room");
        failedIssuedDashboardPage.clickSelectRoom();

        step("Check get refund section clickable");
        failedIssuedDashboardPage.clickGetRefund();
        Assertion.assertThat("Payment and refund details modal appeared", failedIssuedDashboardPage.isPaymentAndRefundDetailsModalShown(), is(true));
    }

    @Author(names = {"Raihan"})
    @Module(name = "FailedIssued")
    @Tribe(name = "Demand")
    @Vertical(name = "Accommodation")
    @Test
    public void testContinueButtonApplicableAfterTickCheckbox() {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<>(session);
        FailedIssuedDashboardPage<?> failedIssuedDashboardPage = new FailedIssuedDashboardPage<>(session);
        Map<String, String> param = getParam("testContinueButtonApplicableAfterTickCheckbox");
        BaseTest.paramsThreadLocal.set(param);

        String email = param.get("email");
        String password = param.get("password");
        String url = param.get("url");
        String orderId = param.get("orderId");
        String hotelName = param.get("hotelName");

        step("Login via cookie injection");
        loginMemberAdmin.loginViaCookie(email, password);

        step("Go to Failed Issued Dashboard");
        loginMemberAdmin.goToDashboard(url);
        Assertion.assertThat("Is Failed Issued Dashboard Page Loaded",failedIssuedDashboardPage.isFailedIssuedDashboardPageLoaded(), is(true));

        step("Input Order Id");
        failedIssuedDashboardPage.inputOrderId(orderId);

        step("Click Search");
        failedIssuedDashboardPage.clickSearch();

        step("Rebook Button is Loaded");
        Assertion.assertThat("Order Id is Present",failedIssuedDashboardPage.isOrderIdBoxLoaded(),containsString(orderId));
        Assertion.assertThat("Action is Present",failedIssuedDashboardPage.isActionColumnLoaded(),is(true));
        Assertion.assertThat("Relocation Button  is Present",failedIssuedDashboardPage.isRelocationButtonClickable(),is(true));

        step("Click Relocation button");
        failedIssuedDashboardPage.clickRelocationButton();

        step("check Hotels is Present");
        Assertion.assertThat("Hotel list is present", failedIssuedDashboardPage.isRelocationHotelListDisplayed(), is(true));

        step("Click hotel");
        failedIssuedDashboardPage.selectRelocationHotelByHotelName(hotelName);

        step("check Rooms is Present");
        Assertion.assertThat("Room is shown",failedIssuedDashboardPage.isRoomLoaded(),is(true));
        Assertion.assertThat("Room Details is shown",failedIssuedDashboardPage.isRoomImageLoaded(),is(true));
        Assertion.assertThat("Room Name is shown",failedIssuedDashboardPage.isRoomNameLoaded(),is(true));
        Assertion.assertThat("Room Price is shown",failedIssuedDashboardPage.isRoomPriceLoaded(),is(true));
        Assertion.assertThat("Select button is clickable", failedIssuedDashboardPage.isSelectButtonClickable(),is(true));

        step("Select room");
        failedIssuedDashboardPage.clickSelectRoom();

        if (failedIssuedDashboardPage.isCheckBoxLoaded()) {
            step("click checkbox accepting terms and condition");
            failedIssuedDashboardPage.clickCheckBoxButton();
        }

        step("Click Continue Booking Button");
        Assertion.assertThat("Continue Booking is Clickable",failedIssuedDashboardPage.isContinueBookingButtonClickable(),is(true));
    }

    @Author(names = {"Raihan"})
    @Module(name = "FailedIssued")
    @Tribe(name = "Demand")
    @Vertical(name = "Accommodation")
    @Test
    public void testCheckAgainClickable() {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<>(session);
        FailedIssuedDashboardPage<?> failedIssuedDashboardPage = new FailedIssuedDashboardPage<>(session);
        Map<String, String> param = getParam("testCheckAgainClickable");
        BaseTest.paramsThreadLocal.set(param);

        String email = param.get("email");
        String password = param.get("password");
        String url = param.get("url");
        String orderId = param.get("orderId");
        String hotelName = param.get("hotelName");

        step("Login via cookie injection");
        loginMemberAdmin.loginViaCookie(email, password);

        step("Go to Failed Issued Dashboard");
        loginMemberAdmin.goToDashboard(url);
        Assertion.assertThat("Is Failed Issued Dashboard Page Loaded",failedIssuedDashboardPage.isFailedIssuedDashboardPageLoaded(), is(true));

        step("Input Order Id");
        failedIssuedDashboardPage.inputOrderId(orderId);

        step("Click Search");
        failedIssuedDashboardPage.clickSearch();

        step("Rebook Button is Loaded");
        Assertion.assertThat("Order Id is Present",failedIssuedDashboardPage.isOrderIdBoxLoaded(),containsString(orderId));
        Assertion.assertThat("Action  is Present",failedIssuedDashboardPage.isActionColumnLoaded(),is(true));
        Assertion.assertThat("Relocation Button  is Present",failedIssuedDashboardPage.isRelocationButtonClickable(),is(true));

        step("Click Relocation button");
        failedIssuedDashboardPage.clickRelocationButton();

        step("check Hotels is Present");
        Assertion.assertThat("Hotel list is present", failedIssuedDashboardPage.isRelocationHotelListDisplayed(), is(true));

        step("Click hotel");
        failedIssuedDashboardPage.selectRelocationHotelByHotelName(hotelName);

        step("check Rooms is Present");
        Assertion.assertThat("Room is shown",failedIssuedDashboardPage.isRoomLoaded(),is(true));
        Assertion.assertThat("Room Details is shown",failedIssuedDashboardPage.isRoomImageLoaded(),is(true));
        Assertion.assertThat("Room Name is shown",failedIssuedDashboardPage.isRoomNameLoaded(),is(true));
        Assertion.assertThat("Room Price is shown",failedIssuedDashboardPage.isRoomPriceLoaded(),is(true));
        Assertion.assertThat("Select button is clickable", failedIssuedDashboardPage.isSelectButtonClickable(),is(true));

        step("Select room");
        failedIssuedDashboardPage.clickSelectRoom();

        if (failedIssuedDashboardPage.isCheckBoxLoaded()) {
            step("click checkbox accepting terms and condition");
            failedIssuedDashboardPage.clickCheckBoxButton();
        }

        step("Click Continue Booking Button");
        Assertion.assertThat("Continue Booking is Clickable",failedIssuedDashboardPage.isContinueBookingButtonClickable(),is(true));
        failedIssuedDashboardPage.clickContinueBookingButton();

        step("Click Check Again Button");
        Assertion.assertThat("Check Again is Clickable",failedIssuedDashboardPage.isCheckAgainButtonClickable(),is(true));
        failedIssuedDashboardPage.clickCheckAgainButton();
        Assertion.assertThat("Continue booking modal is disappeared", failedIssuedDashboardPage.isContinueToBookModalShown(), is(true));
    }

    @Author(names = {"Ketan"})
    @Module(name = "FailedIssued")
    @Tribe(name = "Demand")
    @Vertical(name = "Accommodation")
    @Test
    public void testCheckIfSelectButtonClickableForRebook () {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<>(session);
        FailedIssuedDashboardPage<?> failedIssuedDashboardPage = new FailedIssuedDashboardPage<>(session);
        Map<String, String> param = getParam("testCheckIfSelectButtonClickableForRebook");
        BaseTest.paramsThreadLocal.set(param);

        String email = param.get("email");
        String password = param.get("password");
        String url = param.get("url");
        String orderId = param.get("orderId");

        step("Login via cookie injection");
        loginMemberAdmin.loginViaCookie(email, password);

        step("Go to Failed Issued Dashboard");
        loginMemberAdmin.goToDashboard(url);
        Assertion.assertThat("Is Failed Issued Dashboard Page Loaded",failedIssuedDashboardPage.isFailedIssuedDashboardPageLoaded(), is(true));

        step("Input Order Id");
        failedIssuedDashboardPage.inputOrderId(orderId);

        step("Click Search");
        failedIssuedDashboardPage.clickSearch();

        step("Rebook Button is Loaded");
        Assertion.assertThat("Order Id is Present",failedIssuedDashboardPage.isOrderIdBoxLoaded(),containsString(orderId));
        Assertion.assertThat("Action  is Present",failedIssuedDashboardPage.isActionColumnLoaded(),is(true));
        Assertion.assertThat("Rebooking Button  is Present",failedIssuedDashboardPage.isRebookingButtonLoaded(),is(true));

        step("Click Rebooking button");
        failedIssuedDashboardPage.clickRebookingButton();

        step("check Rooms is Present");
        Assertion.assertThat("Room is shown",failedIssuedDashboardPage.isRoomLoaded(),is(true));
        Assertion.assertThat("Room Details is shown",failedIssuedDashboardPage.isRoomImageLoaded(),is(true));
        Assertion.assertThat("Room Name is shown",failedIssuedDashboardPage.isRoomNameLoaded(),is(true));
        Assertion.assertThat("Room Price is shown",failedIssuedDashboardPage.isRoomPriceLoaded(),is(true));
        Assertion.assertThat("Select button is clickable", failedIssuedDashboardPage.isSelectButtonClickable(),is(true));
    }

    @Author(names = {"Ketan Singh"})
    @Module(name = "Failed Issued")
    @Tribe(name = "Demand")
    @Vertical(name = "Accommodation")
    @Test
    public void testContinueBookingClickableOnRebook() {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<>(session);
        FailedIssuedDashboardPage<?> failedIssuedDashboardPage = new FailedIssuedDashboardPage<>(session);
        Map<String, String> param = getParam("testContinueBookingClickableOnRebook");
        BaseTest.paramsThreadLocal.set(param);

        String email = param.get("email");
        String password = param.get("password");
        String url = param.get("url");
        String orderId = param.get("orderId");

        step("Login via cookie injection");
        loginMemberAdmin.loginViaCookie(email, password);

        step("Go to Failed Issued Dashboard");
        loginMemberAdmin.goToDashboard(url);
        Assertion.assertThat("Is Failed Issued Dashboard Page Loaded",failedIssuedDashboardPage.isFailedIssuedDashboardPageLoaded(), is(true));

        step("Input Order Id");
        failedIssuedDashboardPage.inputOrderId(orderId);

        step("Click Search");
        failedIssuedDashboardPage.clickSearch();

        step("Rebook Button is Loaded");
        Assertion.assertThat("Order Id is Present",failedIssuedDashboardPage.isOrderIdBoxLoaded(),containsString(orderId));
        Assertion.assertThat("Action  is Present",failedIssuedDashboardPage.isActionColumnLoaded(),is(true));
        Assertion.assertThat("Rebooking Button  is Present",failedIssuedDashboardPage.isRebookingButtonLoaded(),is(true));

        step("Click Rebooking button");
        failedIssuedDashboardPage.clickRebookingButton();

        step("check Rooms is Present");
        Assertion.assertThat("Room is shown",failedIssuedDashboardPage.isRoomLoaded(),is(true));
        Assertion.assertThat("Room Details is shown",failedIssuedDashboardPage.isRoomImageLoaded(),is(true));
        Assertion.assertThat("Room Name is shown",failedIssuedDashboardPage.isRoomNameLoaded(),is(true));
        Assertion.assertThat("Room Price is shown",failedIssuedDashboardPage.isRoomPriceLoaded(),is(true));
        Assertion.assertThat("Select button is clickable", failedIssuedDashboardPage.isSelectButtonClickable(),is(true));

        step("Click Select button");
        failedIssuedDashboardPage.clickSelectButton();

        step("Check New Hotel Page is opened");
        Assertion.assertThat("New Hotel is shown",failedIssuedDashboardPage.isNewHotelPageLoaded(),is(true));
        Assertion.assertThat("Terms and Condition Pane is loaded",failedIssuedDashboardPage.isTermsAndConditionLoaded(),is(true));

        if (failedIssuedDashboardPage.isCheckBoxLoaded()) {
            step("click checkbox accepting terms and condition");
            failedIssuedDashboardPage.clickCheckBoxButton();
        }
        step("Click Continue Booking Button");
        Assertion.assertThat("Continue Booking is Clickable",failedIssuedDashboardPage.isContinueBookingButtonClickable(),is(true));
    }

    @Author(names = {"Ketan Singh"})
    @Module(name = "Failed Issued")
    @Tribe(name = "Demand")
    @Vertical(name = "Accommodation")
    @Test
    public void testPopupButtonClickableShownWhenRebook() {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<>(session);
        FailedIssuedDashboardPage<?> failedIssuedDashboardPage = new FailedIssuedDashboardPage<>(session);
        orderCreationPage<?> orderCreationPage = new orderCreationPage<>(session);
        Map<String, String> param = getParam("testPopupButtonClickableShownWhenRebook");
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

        step("Go to Failed Issued Dashboard");
        loginMemberAdmin.goToDashboard(url);
        Assertion.assertThat("Is Failed Issued Dashboard Page Loaded",failedIssuedDashboardPage.isFailedIssuedDashboardPageLoaded(), is(true));

        step("Input Order Id");
        failedIssuedDashboardPage.inputOrderId(bookedOrderID);

        step("Click Search");
        failedIssuedDashboardPage.clickSearch();

        step("Rebook Button is Loaded");
        Assertion.assertThat("Order Id is Present",failedIssuedDashboardPage.isOrderIdBoxLoaded(),containsString(bookedOrderID));
        Assertion.assertThat("Action  is Present",failedIssuedDashboardPage.isActionColumnLoaded(),is(true));
        Assertion.assertThat("Rebooking Button  is Present",failedIssuedDashboardPage.isRebookingButtonLoaded(),is(true));

        step("Click Rebooking button");
        failedIssuedDashboardPage.clickRebookingButton();

        step("check Rooms is Present");
        Assertion.assertThat("Room is shown",failedIssuedDashboardPage.isRoomLoaded(),is(true));
        Assertion.assertThat("Room Details is shown",failedIssuedDashboardPage.isRoomImageLoaded(),is(true));
        Assertion.assertThat("Room Name is shown",failedIssuedDashboardPage.isRoomNameLoaded(),is(true));
        Assertion.assertThat("Room Price is shown",failedIssuedDashboardPage.isRoomPriceLoaded(),is(true));
        Assertion.assertThat("Select button is clickable", failedIssuedDashboardPage.isSelectButtonClickable(),is(true));

        step("Click Select button");
        failedIssuedDashboardPage.clickSelectButton();

        step("Check New Hotel Page is opened");
        Assertion.assertThat("New Hotel is shown",failedIssuedDashboardPage.isNewHotelPageLoaded(),is(true));
        Assertion.assertThat("Terms and Condition Pane is loaded",failedIssuedDashboardPage.isTermsAndConditionLoaded(),is(true));

        if (failedIssuedDashboardPage.isCheckBoxLoaded()) {
            step("click checkbox accepting terms and condition");
            failedIssuedDashboardPage.clickCheckBoxButton();
        }

        step("Click Continue Booking Button");
        Assertion.assertThat("Continue Booking is Clickable",failedIssuedDashboardPage.isContinueBookingButtonClickable(),is(true));
        failedIssuedDashboardPage.clickContinueBookingButton();
        Assertion.assertThat("PopUp is Loaded",failedIssuedDashboardPage.isRebookPopUpLoaded(),is(true));

        step("Check Buttons are Clickable or not");
        Assertion.assertThat("Check Again button is clickable",failedIssuedDashboardPage.isCheckAgainButtonClickable(),is(true));
        Assertion.assertThat("Continue Booking button is clickable",failedIssuedDashboardPage.isContinueBookingRebookButtonClickable(),is(true));
    }
    @Author(names = {"Venkat"})
    @Module(name = "Failed Issued")
    @Tribe(name = "Demand")
    @Vertical(name = "Accommodation")
    @Test
    public void testDefaultListFailedIssuedDashboard() {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<>(session);
        FailedIssuedDashboardPage<?> failedIssuedDashboardPage = new FailedIssuedDashboardPage<>(session);
        orderCreationPage<?> orderCreationPage = new orderCreationPage<>(session);
        Map<String, String> param = getParam("testDefaultListFailedIssuedDashboard");
        BaseTest.paramsThreadLocal.set(param);

        String email = param.get("email");
        String password = param.get("password");
        String url = param.get("url");

        step("Login via cookie injection");
        loginMemberAdmin.loginViaCookie(email, password);

        step("Go to Failed Issued Dashboard");
        loginMemberAdmin.goToDashboard(url);
        Assertion.assertThat("Is Failed Issued Dashboard Page Loaded",failedIssuedDashboardPage.isFailedIssuedDashboardPageLoaded(), is(true));

        step("Default failed issued list should be visible");
        Assertion.assertThat("Order Id is Present",failedIssuedDashboardPage.isOrderIdBoxLoaded().isEmpty(),is(false));
        Assertion.assertThat("Order Details Id is Present",failedIssuedDashboardPage.isOrderDetailsLoaded(),is(true));
        Assertion.assertThat("Payment Status is Present",failedIssuedDashboardPage.isPaymentStatusLoaded(),is(true));
        Assertion.assertThat("Payment Option is Present",failedIssuedDashboardPage.isPaymentOptionLoaded(),is(true));
        Assertion.assertThat("Guest Name is Present",failedIssuedDashboardPage.isGuestNameLoaded(),is(true));
        Assertion.assertThat("Order Issued is Present",failedIssuedDashboardPage.isOrderIssuedLoaded(),is(true));
        Assertion.assertThat("Status is Present",failedIssuedDashboardPage.isStatusLoaded(),is(true));
        Assertion.assertThat("Reason is Present",failedIssuedDashboardPage.isReasonLoaded(),is(true));
    }
}

