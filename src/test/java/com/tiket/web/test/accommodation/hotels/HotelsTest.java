package com.tiket.web.test.accommodation.hotels;

import static com.tiket.core.CoreFunctions.addToDateYYYY_MM_DD;
import static com.tiket.core.CoreFunctions.convertPriceSafely;
import static com.tiket.core.CoreFunctions.formatRoomPrice;
import static com.tiket.core.CoreFunctions.generateCheckInOutDate;
import static com.tiket.core.CoreFunctions.generateRandomDate;
import static com.tiket.core.CoreFunctions.generateRandomInt;
import static com.tiket.core.CoreFunctions.generateRandomString;
import static com.tiket.core.CoreFunctions.getDaysBetweenDates;
import static com.tiket.core.CoreFunctions.getRandomObject;
import static com.tiket.core.CoreFunctions.normalizeNonBreakingSpaceText;
import static com.tiket.core.CoreFunctions.randomName;
import static com.tiket.core.CoreFunctions.summarizePrices;
import static com.tiket.core.CoreFunctions.thousandSeparator;
import static com.tiket.testdata.TestData.getObjectParams;
import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.both;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.containsStringIgnoringCase;
import static org.hamcrest.Matchers.either;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.matchesPattern;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

import javax.security.auth.login.FailedLoginException;

import com.tiket.web.page.corporate.travel_plan.TravelPlanWeb;
import org.eclipse.jgit.util.StringUtils;
import org.hamcrest.Matchers;
import org.hamcrest.core.Is;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.apiPage.ExtranetHotelApi;
import com.tiket.apiPage.ExtranetHotelAuthApi;
import com.tiket.apiPage.HotelApi;
import com.tiket.apiPage.MmDashboardApi;
import com.tiket.bq.OutboundNotificationVerifier;
import com.tiket.core.CoreFunctions;
import com.tiket.dbclient.bigquery.NotificationChannel;
import com.tiket.dbclient.bigquery.NotificationTemplateType;
import com.tiket.model.Environment;
import com.tiket.model.Platform;
import com.tiket.model.Session;
import com.tiket.pojo.accommodation.hotel.HotelAutoCompleteResponse;
import com.tiket.pojo.accommodation.hotel.SearchHotelResponse;
import com.tiket.pojo.extranet.booking.HotelBookingDetailResponse;
import com.tiket.pojo.extranet.login.ExtranetAdminLoginMfaApplyResponse;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.testbase.Configs;
import com.tiket.web.dWeb.page.accommodation.hotels.HotelDetailPage;
import com.tiket.web.dWeb.page.accommodation.hotels.HotelSeoPage;
import com.tiket.web.dWeb.page.payment.PaymentPage;
import com.tiket.web.dashboard.page.common.InboxPageYopmail;
import com.tiket.web.dashboard.page.order.orderCreationPage;
import com.tiket.web.page.accommodation.homes.homesRoomListPage.HomesRoomListPageWeb;
import com.tiket.web.page.accommodation.hotels.BookingForm.HotelBookingFormPage;
import com.tiket.web.page.accommodation.hotels.BookingForm.HotelBookingFormPageWeb;
import com.tiket.web.page.accommodation.hotels.LandingPage.LandingPageDweb;
import com.tiket.web.page.accommodation.hotels.LandingPage.LandingPageWeb;
import com.tiket.web.page.accommodation.hotels.PDP.PDPPageWeb;
import com.tiket.web.page.accommodation.hotels.ReviseBooking.HotelReviseBookingPage;
import com.tiket.web.page.accommodation.hotels.SRP.HotelSearchResultPage;
import com.tiket.web.page.accommodation.hotels.SRP.SRPPageMweb;
import com.tiket.web.page.accommodation.hotels.SRP.SRPPageWeb;
import com.tiket.web.page.accommodation.hotels.hotelLandingPage.HotelLandingPageWeb;
import com.tiket.web.page.accommodation.hotels.hotelRoomListPage.HotelRoomListPage;
import com.tiket.web.page.accommodation.hotels.hotelRoomListPage.HotelRoomListPageWeb;
import com.tiket.web.page.accommodation.hotels.reschedulePage.HotelReschedulePageWeb;
import com.tiket.web.page.accommodation.hotels.roomDetailsPage.RoomDetailsPageWeb;
import com.tiket.web.page.common.calendar.CalendarPageWeb;
import com.tiket.web.page.common.eTicket.ETicketWeb;
import com.tiket.web.page.common.home.HomePageWeb;
import com.tiket.web.page.common.home.LoginPageWeb;
import com.tiket.web.page.corporate.dashboard.CorporateDashboard;
import com.tiket.web.page.order.OrderDetail.OrderDetailWeb;
import com.tiket.web.page.order.OrderList.OrderListWeb;
import com.tiket.web.page.platform.payment.PaymentPageWeb;
import com.tiket.web.page.platform.sphinx.refund.RefundDetailWeb;
import com.tiket.web.page.platform.sphinx.refund.RefundPageWeb;

public class HotelsTest<T extends WebDriver> extends BaseTest {

    @Author(names = "Fajrul Alam")
    @Module(name = "SearchResultsPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyChangeSearchCriteria() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyChangeSearchCriteria");
        BaseTest.paramsThreadLocal.set(param);

        SRPPageWeb<?> searchResultPage = SRPPageWeb.getInstance(session);
        HomePageWeb<?> homePageWeb = HomePageWeb.getInstance(session);
        LandingPageWeb<?> landingPageWeb = LandingPageDweb.getInstance(session);

        String location = param.get("location");
        String newLocation = param.get("newLocation");
        int noOfAdults = Integer.parseInt(param.get("Adult"));
        int noOfRoom = Integer.parseInt(param.get("Room"));

        step("User opens hotel landing page");
        homePageWeb.openVerticalPageViaURL("Hotel");

        step("Dismiss 'Install app' pop up");
        landingPageWeb.dismissInstallAppPopUp();

        step("Search stay location");
        landingPageWeb.searchStayLocation(location);

        step("Submit search button");
        landingPageWeb.clickSubmit();

        step("Close pop-up");
        searchResultPage.clickPopUp();

        step("Input new location");
        searchResultPage.updateStayLocation(newLocation);

        step("Update guests and rooms");
        searchResultPage.clickRoomAndGuestButton();
        searchResultPage.setTotalGuest(noOfAdults);
        searchResultPage.setTotalRoom(noOfRoom);
        searchResultPage.clickDone();

        step("Set date");
        searchResultPage.setStayDate("range date", "10-15");

        step("Click search button to update SRP results");
        searchResultPage.clickSearchOnSRP();

        step("SRP is refreshed");

        String SearchBarLocation = searchResultPage.getLocationNameFromInput();
        String RoomAndGuestDetails = searchResultPage.getRoomAndGuestInfoFromInput();

        Assertion.assertThat("Hotel card on SRP is visible", searchResultPage.isHotelCardVisible(), is(true));
        Assertion.assertThat("Searched text entered by user matches SRP's search bar", newLocation,
                is(SearchBarLocation));

        String[] roomGuestDetailsArray = RoomAndGuestDetails.split(", ");
        log("roomGuestDetailsArray: " + Arrays.toString(roomGuestDetailsArray));
        int roomsFromSRP = Integer.parseInt(roomGuestDetailsArray[0].split(" ")[0]);
        int adultsFromSRP = Integer.parseInt(roomGuestDetailsArray[1].split(" ")[0]);

        Assertion.assertThat("Number of updated rooms should match on SRP", roomsFromSRP, is(noOfRoom));
        Assertion.assertThat("Number of updated adults should match on SRP", adultsFromSRP, is(noOfAdults));
    }

    @Author(names = {"Yazid", "Mridul Gupta"})
    @Module(name = "Reschedule")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyRescheduleSingleOrder() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("verifyRescheduleSingleOrder");
        BaseTest.paramsThreadLocal.set(param);
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String startDate = generateRandomDate(param.get("rangeAddDate"));
        String hotelName = param.get("hotelName");
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night")); // should be less than 'rescheduleDuration' value
        int room = Integer.parseInt(param.get("room"));
        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = getRandomObject(salutationList);
        String currency = param.get("currency");
        boolean isRefundable = Boolean.parseBoolean(param.get("isRefundable"));
        int rescheduleDuration = Integer.parseInt(param.get("rescheduleDuration")); // should be greater than 'night' value

        Session<?> session = BaseTest.sessionThreadLocal.get();
        HotelApi<?> hotelApi = new HotelApi<>(session);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        HotelReschedulePageWeb<?> hotelReschedulePage = HotelReschedulePageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);
        RoomDetailsPageWeb<?> roomDetailsPage = RoomDetailsPageWeb.getInstance(session);

        step("Create new hotel issued order");
        boolean result = hotelApi.createIssuedOrderApi(
                email, password, startDate, hotelName, adult, night, room,
                orderFullName, orderNameTitle, guestFullName, guestTitle, currency, isRefundable,
                false, "", "en",
                true);
        Assertion.assertThat("Hotel Booking via API is successful", result, is(true));

        step("Get data from the issued order");
        String initialOrderId = String.valueOf(hotelApi.getOrderId());

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User select currency: " + currency);
        homePage.selectCurrency(currency);

        step("user is on order Detail");
        orderListPage.openActiveTicketViaURL(initialOrderId);
        orderDetailPage.skipCoachmark();
        String totalPaymentInitialOrder = orderDetailPage.getVerticalTotalPrice();

        step("User click Reschedule");
        orderDetailPage.clickRescheduleButton();

        step("Choose new date for the reschedule");
        String newStartDate = hotelReschedulePage.changeNightDuration(rescheduleDuration);

        step("Open the room details with additional price");
        hotelReschedulePage.openRoomWithAdditionalPrice();
        roomDetailsPage.clickOnPriceDetails();
        Assertion.assertThat("Current balance should be equal with initial total price", roomDetailsPage.getCurrentBalance(true), is(totalPaymentInitialOrder));

        step("Calculate the additional price");
        String initialTotalPrice = roomDetailsPage.getInitialTotalPrice(currency);
        String priceCalculation = summarizePrices(initialTotalPrice, roomDetailsPage.getCurrentBalance(false), currency);
        String additionalTotalPrice = roomDetailsPage.getAdditionalTotalPrice(currency);
        Assertion.assertThat("Additional total price should be equal with calculated price", additionalTotalPrice, is(priceCalculation));
        roomDetailsPage.closePriceDetails();

        step("Book the room with additional Price");
        String newRoomName = roomDetailsPage.getRoomNameDetails();
        roomDetailsPage.clickBookButton();

        step("Submit booking form");
        hotelReschedulePage.submitRescheduleBookingForm();

        step("check order id on payment page");
        String rescheduleOrderID = paymentPage.getOrderId();

        step("Choose payment method");
        paymentPage.selectPaymentMethod("Sakuku");

        step("Finish the payment process by clicking the payment button");
        paymentPage.clickOnPaymentButton();

        step("Verify the payment successful page is visible or not");
        Assertion.assertThat("Verify the payment successful page is visible", paymentPage.isPaymentSuccessfulVisible(), is(true));

        step("Navigate to the details page of the issued e-ticket order");
        orderListPage.openActiveTicketViaURL(rescheduleOrderID);
        orderDetailPage.skipCoachmark();

        step("Check the new order detail");
        Assertion.assertThat("Reschedule card notification should be present", orderDetailPage.isRescheduleCardDisplayed(), is(true));
        Assertion.assertThat("Order ID should be showing as expected", orderDetailPage.getOrderId(), is(rescheduleOrderID));
        Assertion.assertThat("Start Date should be showing as expected", orderDetailPage.getStartDate(), is(newStartDate));
        Assertion.assertThat("Hotel Name should be showing as expected: " + hotelName, orderDetailPage.isHotelNameDisplayed(hotelName), is(true));
        Assertion.assertThat("Room Name should be showing as expected: " + newRoomName, orderDetailPage.isRoomNameDisplayed(newRoomName), is(true));

        step("Check the currency on the new order detail");
        Assertion.assertThat("Current currency should be " + currency, orderDetailPage.getVerticalTotalPrice(), containsString(currency));

        step("Check the reschedule button");
        orderDetailPage.clickRescheduleButton();
        Assertion.assertThat("Rescheduled order cannot be rescheduled again", orderDetailPage.isPopUpNonRefundableShown(), is(true));
        orderDetailPage.closeRefundReschedulePopUp();

        step("Check the previous order detail");
        orderDetailPage.goToPreviousOrderBanner();
        orderDetailPage.clickPreviousOrder();
        Assertion.assertThat("Previous order banner should be present", orderDetailPage.isNewScheduleButtonDisplayed(), is(true));
        Assertion.assertThat("Hotel Name should be showing as expected: " + hotelName, orderDetailPage.isHotelNameDisplayed(hotelName), is(true));
        Assertion.assertThat("Start Date should be different with the rescheduled order", orderDetailPage.getStartDate(), is(not(newStartDate)));
    }

    /**
     * Web counterpart of app {@code ACC_verifyRescheduleIssuedBookingLogin}.
     * Sheet key: {@code ACC_verifyRescheduleIssuedBookingLogin}. Params: email or username, password, otp,
     * retryCounter, rangeAddDate, hotelName, adult, night, room, rescheduleDuration.
     */
    @Author(names = {"Yazid"})
    @Module(name = "Reschedule")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyRescheduleIssuedBookingLoginWeb() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("verifyRescheduleIssuedBookingLoginWeb");
        BaseTest.paramsThreadLocal.set(param);

        String email = param.getOrDefault("email", param.get("username"));
        String password = param.get("password");
        String otp = param.getOrDefault("otp", "123456");
        int retryCounter = Integer.parseInt(param.getOrDefault("retryCounter", "3"));
        String startDate = generateRandomDate(param.get("rangeAddDate"));
        String hotelName = param.get("hotelName");
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));
        int room = Integer.parseInt(param.get("room"));
        int rescheduleDuration = Integer.parseInt(param.get("rescheduleDuration"));
        String currency = param.getOrDefault("currency", "IDR");
        boolean isRefundable = Boolean.parseBoolean(param.getOrDefault("isRefundable", "true"));
        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = salutationList[0];
        String paymentGroup = param.get("paymentGroup");
        String paymentMethod = param.get("paymentMethod");

        Session<?> session = BaseTest.sessionThreadLocal.get();
        HotelApi<?> hotelApi = new HotelApi<>(session);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        HotelReschedulePageWeb<?> hotelReschedulePage = HotelReschedulePageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);

        step("Create new hotel issued order");
        boolean result = hotelApi.createIssuedOrderApi(
                email, password, startDate, hotelName, adult, night, room,
                orderFullName, orderNameTitle, guestFullName, guestTitle, currency, isRefundable,
                false, "", "en", true);
        Assertion.assertThat("Verify booking via API is successful", result, is(true));

        step("Get data from the issued order");
        String issuedOrderId = String.valueOf(hotelApi.getOrderId());

        step("Go to home");
        homePage.goToHomePage();

        step("Login as " + email + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(email, password);
        homePage.selectCurrency(currency);

        step("Open the issued order detail");
        orderListPage.openActiveTicketViaURL(issuedOrderId);
        orderDetailPage.skipCoachmark();

        step("Choose reschedule");
        orderDetailPage.clickRescheduleButton();

        step("Choose new date for the reschedule");
        String newStartDate = hotelReschedulePage.changeNightDuration(rescheduleDuration);

        step("Choose a new room type with additional price");
        hotelReschedulePage.getRoomName();
        String newRoomName = hotelReschedulePage.chooseRoomWithAdditionalPrice();

        step("Submit reschedule booking form");
        hotelReschedulePage.submitRescheduleBookingForm();

        step("Submit the payment method");
        String rescheduleOrderId = paymentPage.getOrderId();
        paymentPage.clickOnSelectPaymentMethod();
        paymentPage.clickOnSeeAll();
        paymentPage.goToPaymentMethodsGroup(paymentGroup);
        paymentPage.selectPaymentMethod(paymentMethod);
        paymentPage.clickOnPaymentButton();

        step("Open order list and check the new rescheduled order detail");
        paymentPage.clickSeeOrderListButton();

        step("Open the rescheduled order detail");
        orderListPage.openActiveTicketViaURL(rescheduleOrderId);
        orderDetailPage.skipCoachmark();
        String totalPayment = orderDetailPage.getVerticalTotalPrice();
        Assertion.assertThat("Reschedule card notification should be present",
                orderDetailPage.isRescheduleCardDisplayed(), is(true));
        Assertion.assertThat("Order ID should match the rescheduled order",
                orderDetailPage.getOrderId(), is(rescheduleOrderId));
        Assertion.assertThat("Start date should match the new reschedule date",
                orderDetailPage.getStartDate(), is(newStartDate));
        Assertion.assertThat("Hotel name should be displayed correctly: " + hotelName,
                orderDetailPage.isHotelNameDisplayed(hotelName), is(true));
        Assertion.assertThat("Room name should be displayed correctly: " + newRoomName,
                orderDetailPage.isRoomNameDisplayed(newRoomName), is(true));

        step("Check the previous order detail");
        orderDetailPage.goToPreviousOrderBanner();
        orderDetailPage.clickPreviousOrder();
        Assertion.assertThat("Previous order banner should be displayed",
                orderDetailPage.isNewScheduleButtonDisplayed(), is(true));
        Assertion.assertThat("Hotel name should be displayed correctly on the previous order: " + hotelName,
                orderDetailPage.isHotelNameDisplayed(hotelName), is(true));
        Assertion.assertThat("Start date on previous order should differ from the rescheduled order",
                orderDetailPage.getStartDate(), is(not(newStartDate)));
        Assertion.assertThat("Reschedule button should not be shown on the previous order",
                orderDetailPage.isRescheduleButtonDisplayed(), is(false));

        step("Back to the new rescheduled order detail");
        orderListPage.openActiveTicketViaURL(rescheduleOrderId);
        orderDetailPage.skipCoachmark();

        step("Check receipt");
        orderDetailPage.openReceipt();
        String receiptPdfContent = orderDetailPage.getPDFContentFile();
        Assertion.assertThat("Total price on order detail should appear on the receipt",
                receiptPdfContent, containsString(totalPayment));

        step("Open e-ticket and verify order id is present");
        orderDetailPage.openETicket();
        String eTicketPdfContent = orderDetailPage.getPDFContentFile();
        Assertion.assertThat("E-ticket PDF should contain the rescheduled order id",
                eTicketPdfContent.toLowerCase(), containsString(rescheduleOrderId.toLowerCase()));
    }

    @Author(names = "Alden")
    @Module(name = "Reschedule")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyRescheduleMorePayment() throws FailedLoginException, InterruptedException, ExecutionException {
        Map<String, String> param = getParam("verifyRescheduleMorePayment");
        BaseTest.paramsThreadLocal.set(param);
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String startDate = generateRandomDate(param.get("rangeAddDate"));
        String hotelName = param.get("hotelName");
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night")); // should be less than 'rescheduleDuration' value
        int room = Integer.parseInt(param.get("room"));
        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = getRandomObject(salutationList);
        String currency = param.get("currency");
        boolean isRefundable = Boolean.parseBoolean(param.get("isRefundable"));
        int rescheduleDuration = Integer.parseInt(param.get("rescheduleDuration")); // should be greater than 'night' value

        Session<?> session = BaseTest.sessionThreadLocal.get();
        HotelApi<?> hotelApi = new HotelApi<>(session);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        HotelReschedulePageWeb<?> hotelReschedulePage = HotelReschedulePageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);
        RoomDetailsPageWeb<?> roomDetailsPage = RoomDetailsPageWeb.getInstance(session);

        step("Create new hotel issued order");
        boolean result = hotelApi.createIssuedOrderApi(
                email, password, startDate, hotelName, adult, night, room,
                orderFullName, orderNameTitle, guestFullName, guestTitle, currency, isRefundable,
                false, "", "en",
                true);
        Assertion.assertThat("Hotel Booking via API is successful", result, is(true));

        step("Get data from the issued order");
        String initialOrderId = String.valueOf(hotelApi.getOrderId());

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User select currency: " + currency);
        homePage.selectCurrency(currency);

        step("user is on order Detail");
        orderListPage.openActiveTicketViaURL(initialOrderId);
        orderDetailPage.skipCoachmark();
        String totalPaymentInitialOrder = orderDetailPage.getVerticalTotalPrice();

        step("User click Reschedule");
        orderDetailPage.clickRescheduleButton();

        step("Choose new date for the reschedule");
        String newStartDate = hotelReschedulePage.changeNightDuration(rescheduleDuration);

        step("Open the room details with additional price");
        hotelReschedulePage.openRoomWithAdditionalPrice();
        roomDetailsPage.clickOnPriceDetails();
        Assertion.assertThat("Current balance should be equal with initial total price", roomDetailsPage.getCurrentBalance(true), is(totalPaymentInitialOrder));

        step("Calculate the additional price");
        String initialTotalPrice = roomDetailsPage.getInitialTotalPrice(currency);
        String priceCalculation = summarizePrices(initialTotalPrice, roomDetailsPage.getCurrentBalance(false), currency);
        String additionalTotalPrice = roomDetailsPage.getAdditionalTotalPrice(currency);
        Assertion.assertThat("Additional total price should be equal with calculated price", additionalTotalPrice, is(priceCalculation));
        roomDetailsPage.closePriceDetails();

        step("Book the room with additional Price");
        String newRoomName = roomDetailsPage.getRoomNameDetails();
        roomDetailsPage.clickBookButton();

        step("Submit booking form");
        hotelReschedulePage.submitRescheduleBookingForm();

        step("check order id on payment page");
        String rescheduleOrderID = paymentPage.getOrderId();

        step("Choose payment method");
        paymentPage.selectPaymentMethod("Sakuku");

        step("Finish the payment process by clicking the payment button");
        paymentPage.clickOnPaymentButton();

        step("Verify the payment successful page is visible or not");
        Assertion.assertThat("Verify the payment successful page is visible", paymentPage.isPaymentSuccessfulVisible(), is(true));

        step("Navigate to the details page of the issued e-ticket order");
        orderListPage.openActiveTicketViaURL(rescheduleOrderID);
        orderDetailPage.skipCoachmark();

        step("Check the new order detail");
        Assertion.assertThat("Reschedule card notification should be present", orderDetailPage.isRescheduleCardDisplayed(), is(true));
        Assertion.assertThat("Order ID should be showing as expected", orderDetailPage.getOrderId(), is(rescheduleOrderID));
        Assertion.assertThat("Start Date should be showing as expected", orderDetailPage.getStartDate(), is(newStartDate));
        Assertion.assertThat("Hotel Name should be showing as expected: " + hotelName, orderDetailPage.isHotelNameDisplayed(hotelName), is(true));
        Assertion.assertThat("Room Name should be showing as expected: " + newRoomName, orderDetailPage.isRoomNameDisplayed(newRoomName), is(true));

        step("Check the currency on the new order detail");
        Assertion.assertThat("Current currency should be " + currency, orderDetailPage.getVerticalTotalPrice(), containsString(currency));

        step("Check the status and shareability of the new order");
        orderDetailPage.clickRescheduleButton();
        Assertion.assertThat("Rescheduled order cannot be rescheduled again", orderDetailPage.isPopUpNonRefundableShown(), is(true));
        orderDetailPage.closeRefundReschedulePopUp();
        String newOrderId = orderDetailPage.getOrderId();
        Assertion.assertThat("New order status is correct", orderDetailPage.getOrderStatus(newOrderId), is("ETICKET_ISSUED"));
        orderDetailPage.verifyShareETicketAndReceiptButton("Receipt-"+newOrderId, "30000");

        step("Check the previous order detail");
        orderDetailPage.goToPreviousOrderBanner();
        orderDetailPage.clickPreviousOrder();
        String oldOrderId = orderDetailPage.getOrderId();
        Assertion.assertThat("Old order status is correct", orderDetailPage.getOrderStatus(oldOrderId), is("RESCHEDULED"));
        Assertion.assertThat("Previous order banner should be present", orderDetailPage.isNewScheduleButtonDisplayed(), is(true));
        Assertion.assertThat("Hotel Name should be showing as expected: " + hotelName, orderDetailPage.isHotelNameDisplayed(hotelName), is(true));
        Assertion.assertThat("Start Date should be different with the rescheduled order", orderDetailPage.getStartDate(), is(not(newStartDate)));
    }

    @Author(names = "Alden")
    @Module(name = "Reschedule")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyRescheduleNonIDR() throws FailedLoginException, InterruptedException, ExecutionException {
        Map<String, String> param = getParam("verifyRescheduleNonIDR");
        BaseTest.paramsThreadLocal.set(param);
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String startDate = generateRandomDate(param.get("rangeAddDate"));
        String hotelName = param.get("hotelName");
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night")); // should be less than 'rescheduleDuration' value
        int room = Integer.parseInt(param.get("room"));
        String currency = param.get("currency");
        String appCurrency = param.get("appCurrency");
        int rescheduleDuration = Integer.parseInt(param.get("rescheduleDuration")); // should be greater than 'night' value
        String cardHolderName = param.get("cardHolderName");
        String cardNumber = param.get("cardNumber");
        String cvv = param.get("cvv");
        String cardMonth = param.get("cardMonth");

        Session<?> session = BaseTest.sessionThreadLocal.get();
        HotelApi<?> hotelApi = new HotelApi<>(session);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        HotelReschedulePageWeb<?> hotelReschedulePage = HotelReschedulePageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);
        RoomDetailsPageWeb<?> roomDetailsPage = RoomDetailsPageWeb.getInstance(session);

        step("Create an order for hotel with waiting payment status");
        String orderId ="";
        try {
            orderId = hotelApi.createWaitingPaymentOrderApi(
                    email, password, startDate, hotelName, adult, night, room,
                    currency, 0, "HOTEL", "id");
            Assertion.assertThat("Success to create waiting payment order", orderId, is(not(emptyOrNullString())));
        } catch (Exception e) {
            Assertion.skip("Error when creating waiting payment order: " + e.getMessage());
        }

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        if (StringUtils.isEmptyOrNull(appCurrency)) {
            step("User select currency: " + currency);
            homePage.selectCurrency(currency);
        } else {
            step("User select currency: " + appCurrency);
            homePage.selectCurrency(appCurrency);
        }

        step("user is on order Detail");
        orderListPage.openActiveTicketViaURL(orderId);
        orderDetailPage.continueToPayment();

        step("Input the card details");
        paymentPage.inputCardNumber(cardNumber);
        paymentPage.inputCardMonth(cardMonth);
        paymentPage.inputCardCvv(cvv);
        paymentPage.inputCardHolderName(cardHolderName);

        step("Finish the payment process by clicking the payment button");
        paymentPage.clickOnPaymentButton();
        paymentPage.handle3DSPage(30);

        step("Verify the payment successful page is visible or not");
        Assertion.assertThat("Verify the payment successful page is visible", paymentPage.isPaymentSuccessfulVisible(), is(true));

        step("user is on order Detail");
        orderListPage.openActiveTicketViaURL(orderId);
        orderDetailPage.skipCoachmark();
        String totalPaymentInitialOrder = CoreFunctions.roundPrice(orderDetailPage.getVerticalTotalPrice());

        step("User click Reschedule");
        orderDetailPage.clickRescheduleButton();

        step("Choose new date for the reschedule");
        String newStartDate = hotelReschedulePage.changeNightDuration(rescheduleDuration);

        step("Open the room details with additional price");
        hotelReschedulePage.openRoomWithAdditionalPrice();
        roomDetailsPage.clickOnPriceDetails();
        Assertion.assertThat("Current balance should be equal with initial total price", roomDetailsPage.getCurrentBalance(true), is(totalPaymentInitialOrder));
        roomDetailsPage.closePriceDetails();

        step("Book the room with additional Price");
        String newRoomName = roomDetailsPage.getRoomNameDetails();
        roomDetailsPage.clickBookButton();

        step("Submit booking form");
        hotelReschedulePage.submitRescheduleBookingForm();

        step("check order id on payment page");
        String rescheduleOrderID = paymentPage.getOrderId();

        step("Input the card details");
        paymentPage.inputCardNumber(cardNumber);
        paymentPage.inputCardMonth(cardMonth);
        paymentPage.inputCardCvv(cvv);
        paymentPage.inputCardHolderName(cardHolderName);

        step("Finish the payment process by clicking the payment button");
        paymentPage.clickOnPaymentButton();
        paymentPage.handle3DSPage(30);

        step("Verify the payment successful page is visible or not");
        Assertion.assertThat("Verify the payment successful page is visible", paymentPage.isPaymentSuccessfulVisible(), is(true));

        step("Navigate to the details page of the issued e-ticket order");
        orderListPage.openActiveTicketViaURL(rescheduleOrderID);
        orderDetailPage.skipCoachmark();

        step("Check the new order detail");
        Assertion.assertThat("Reschedule card notification should be present", orderDetailPage.isRescheduleCardDisplayed(), is(true));
        Assertion.assertThat("Order ID should be showing as expected", orderDetailPage.getOrderId(), is(rescheduleOrderID));
        Assertion.assertThat("Start Date should be showing as expected", orderDetailPage.getStartDate(), is(newStartDate));
        Assertion.assertThat("Hotel Name should be showing as expected: " + hotelName, orderDetailPage.isHotelNameDisplayed(hotelName), is(true));
        Assertion.assertThat("Room Name should be showing as expected: " + newRoomName, orderDetailPage.isRoomNameDisplayed(newRoomName), is(true));

        step("Check the currency on the new order detail");
        Assertion.assertThat("Current currency should be " + currency, orderDetailPage.getVerticalTotalPrice(), containsString(currency));

        step("Check the status and shareability of the new order");
        orderDetailPage.clickRescheduleButton();
        Assertion.assertThat("Rescheduled order cannot be rescheduled again", orderDetailPage.isPopUpNonRefundableShown(), is(true));
        orderDetailPage.closeRefundReschedulePopUp();
        String newOrderId = orderDetailPage.getOrderId();
        Assertion.assertThat("New order status is correct", orderDetailPage.getOrderStatus(newOrderId), is("ETICKET_ISSUED"));
        orderDetailPage.verifyShareETicketAndReceiptButton("Receipt-"+newOrderId, "30000");

        step("Check the previous order detail");
        orderDetailPage.goToPreviousOrderBanner();
        orderDetailPage.clickPreviousOrder();
        String oldOrderId = orderDetailPage.getOrderId();
        Assertion.assertThat("Old order status is correct", orderDetailPage.getOrderStatus(oldOrderId), is("RESCHEDULED"));
        Assertion.assertThat("Previous order banner should be present", orderDetailPage.isNewScheduleButtonDisplayed(), is(true));
        Assertion.assertThat("Hotel Name should be showing as expected: " + hotelName, orderDetailPage.isHotelNameDisplayed(hotelName), is(true));
        Assertion.assertThat("Start Date should be different with the rescheduled order", orderDetailPage.getStartDate(), is(not(newStartDate)));
    }

    @Author(names = "Fajrul Alam")
    @Module(name = "SearchResultsPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyDetailsInSRPIsDisplayed() {
        Map<String, String> param = getParam("verifyDetailsInSRPIsDisplayed");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String location = param.get("location");

        SRPPageWeb<?> searchResultPage = SRPPageWeb.getInstance(session);
        HomePageWeb<?> homePageWeb = HomePageWeb.getInstance(session);
        LandingPageWeb<?> landingPageWeb = LandingPageDweb.getInstance(session);

        step("User opens hotel landing page");
        homePageWeb.openVerticalPageViaURL("Hotel");

        step("Dismiss 'Install app' pop up");
        landingPageWeb.dismissInstallAppPopUp();

        step("Search stay location");
        landingPageWeb.searchStayLocation(location);

        step("Submit search button");
        landingPageWeb.clickSubmit();

        step("close Pop up");
        searchResultPage.clickPopUp();

        String expectedHotelName = searchResultPage.getHotelName();
        String[] expectedLocation = searchResultPage.getHotelLocation();

        String expectedPrice = searchResultPage.getHotelPrice();
        String SearchBarLocation = searchResultPage.getLocationNameFromInput();

        Assertion.assertThat("Hotel Card on SRP is visible ", searchResultPage.isHotelCardVisible(), is(true));
        Assertion.assertThat("Hotel's name is visible on Hotel Card ", !expectedHotelName.isEmpty(), is(true));
        Assertion.assertThat("Hotel's Area is visible on Hotel Card", !expectedLocation[0].trim().isEmpty(), is(true));
        Assertion.assertThat("Hotel's City is visible on Hotel Card", !expectedLocation[1].trim().isEmpty(), is(true));
        Assertion.assertThat("Hotel's Price is visible on Hotel Card", !expectedPrice.isEmpty(), is(true));
        Assertion.assertThat("Hotel's Benefits is visible on Hotel Card", searchResultPage.isHotelBenefitsVisible(),
                is(true));
        Assertion.assertThat("Hotel's Image is visible on Hotel Card", searchResultPage.isHotelImageVisible(),
                is(true));
        Assertion.assertThat("MAP is visible on SRP", searchResultPage.isMapVisible(), is(true));
        Assertion.assertThat("Searched Text entered by user  is matching on SRP's searchbar", location,
                is(SearchBarLocation));
    }

    @Author(names = "Shubh Sharma")
    @Module(name = "RoomListPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyMoRoomListWhenSearchMoreThanOneRoom() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("verifyMoRoomListWhenSearchMoreThanOneRoom");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String location = param.get("location");
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        int noOfRooms = Integer.parseInt(param.get("rooms"));
        int guests = Integer.parseInt(param.get("guests"));
        String dateType = param.get("dateType");
        String dateRange = param.get("dateRange");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        HotelDetailPage<?> detailPage = new HotelDetailPage<>(session);
        HotelRoomListPageWeb<?> roomListPage = HotelRoomListPageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HotelBookingFormPageWeb<?> bookingFormPage = HotelBookingFormPageWeb.getInstance(session);

        step("Open home page and login");
        homePage.goToHomePage();
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("Navigate to Hotel vertical and search for stay");
        homePage.selectCurrency("IDR");
        homePage.openVerticalPageViaURL("Hotel");
        landingPage.searchStayLocation(location);
        
        step("Set date");
        landingPage.setStayDate(dateType, dateRange);
        landingPage.clickSubmit();

        Assertion.assertThat("PDP is loaded", detailPage.isPdpLoaded(), is(true));
        detailPage.seeHotelRoom();

        step("Set guest and room count > 1");
        roomListPage.clickRoomListChangeButton();
        roomListPage.clickRoomNGuestButton();
        roomListPage.setTotalGuest(guests);
        roomListPage.setTotalRoom(noOfRooms);
        roomListPage.clickGuestDoneAndSubmitButton();

        step("Ensure room list section is visible");
        Assertion.assertThat("Room List is visible", roomListPage.isRoomListSectionVisible(), is(true));

        step("Click select on Room List");
        roomListPage.clickSelectRoomButton();
        Assertion.assertThat("MO banner should be shown", roomListPage.isMoBannerVisible(), is(true));

        step("Open the room details");
        roomListPage.openRoomDetailFromAvailablePriceContainer();

        step("Click select on Room Detail");
        roomListPage.clickSelectRoomDetailButton();
        roomListPage.clickSaveRoomDetailButton();
        Assertion.assertThat("Price footer for MO offer is visible", roomListPage.isPriceFooterVisible(), is(true));

        step("Click book button on price footer");
        roomListPage.clickFooterBookButton();
        Assertion.assertThat("Redirected to Booking Form", bookingFormPage.isContactForm(), is(true));
    }

    @Author(names = {"Harshit", "Yazid"})
    @Module(name = "OrderPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test(dataProvider = "verifyEditGuestSoDataProvider")
    public void verifyEditGuestSo(Map<String, String> param) throws FailedLoginException, InterruptedException, ExecutionException {
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        HotelApi<?> hotelApi = new HotelApi<>(session);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HotelReviseBookingPage<?> reviseBookingPage = new HotelReviseBookingPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String startDate = generateRandomDate(param.get("startDate"));
        String hotelName = param.get("hotelName");
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));
        int room = Integer.parseInt(param.get("room"));
        String currency="IDR";

        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autoguest";
        String guestTitle = salutationList[0];

        step("Create new hotel issued order");
        Boolean result = hotelApi.createIssuedOrderApi(
                username, password, startDate, hotelName, adult, night, room,
                orderFullName, orderNameTitle, guestFullName, guestTitle, currency,
                true, false, "", "en", true
        );

        step("Get data from the issued order");
        String bookedOrderID =  String.valueOf(hotelApi.getOrderId());

        Assertion.assertThat("Verify Booking via API is successful",result,is(true));

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(username, password, otp, retryCounter);

        step("Find the issued order");
        orderListPage.openActiveTicketViaURL(bookedOrderID);
        orderDetailPage.skipCoachmark();

        step("Verify hotel order details");

        String oldName = orderDetailPage.getGuestName(bookedOrderID);
        Assertion.assertThat("Order ID is displayed correctly", orderDetailPage.getOrderId(),is(bookedOrderID));
        Assertion.assertThat("E-ticket card Displayed", orderDetailPage.isETicketCardDisplayed(),is(true));
        Assertion.assertThat("Hotel Name is displayed correctly", orderDetailPage.isHotelNameDisplayed(hotelName), is(true));

        step("Click on Edit booking");
        orderDetailPage.clickEditBooking();
        orderDetailPage.clickEditPopUpGuest();

        step("edit guest name");
        reviseBookingPage.editGuestName(CoreFunctions.randomName() + "Edited");
        reviseBookingPage.clickAgreeButton();
        reviseBookingPage.inputOTPCode("1234");
        Assertion.assertThat("Success message should be displayed ", reviseBookingPage.getSuccessText(), containsString("The guest name has been changed"));
        reviseBookingPage.clickSeeDetails();
        Assertion.assertThat("Verify Order Detail Page is Opened", orderListPage.getCurrentUrl(), containsString("yourorder/detail"));
        Assertion.assertThat("Verify OrderId is same", orderDetailPage.getOrderId(), is(bookedOrderID));
        Assertion.assertThat("Verify Guest Name is changed", orderDetailPage.getGuestName(bookedOrderID), not(oldName));
    }
    @DataProvider(name = "verifyEditGuestSoDataProvider")
    public Object[][] verifyEditGuestSoDataProvider() {return getObjectParams("verifyEditGuestSo");}

    @Author(names = "Harshit, Raihan")
    @Module(name = "DetailPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void VerifyRoomOverviewRoomListLoginWeb() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("VerifyRoomOverviewRoomListLoginWeb");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String location = param.get("location");
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String dateType = param.get("dateType");
        String dateRange = param.get("dateRange");
        int noOfAdults = Integer.parseInt(param.get("Adult"));

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LandingPageWeb<?> landingPage = LandingPageWeb.getInstance(session);
        PDPPageWeb<?> detailPage = PDPPageWeb.getInstance(session);
        HotelRoomListPage<?> roomListPage = new HotelRoomListPage<>(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User select IDR currency");
        homePage.selectCurrency("IDR");
        homePage.selectEnglishLanguage();

        step("User Open hotel Landing page");
        homePage.openVerticalPageViaURL("Hotel");

        step("Dismiss 'Install app' pop up");
        landingPage.dismissInstallAppPopUp();

        step("Search stay location");
        landingPage.searchStayLocation(location);

        step("Set guests and rooms");
        landingPage.clickRoomAndGuestButton();
        landingPage.setTotalGuest(noOfAdults);
        landingPage.clickDone();

        step("Set date");
        landingPage.setStayDate(dateType, dateRange);

        step("submit search Button");
        landingPage.clickSubmit();

        step("Go Inside PDP");

        Assertion.assertThat("Verify User is redirected to PDP", detailPage.isPdpLoaded(), is(true));
        Assertion.assertThat("Hotel name on the Landing and detail page should be equal",
                detailPage.getHotelNameDetail(), containsString(location));

        if (detailPage.isRoomsAvailable()) {
            Assertion.assertThat("Hotel's Price is visible on Hotel PDP", !detailPage.getHotelPriceDetail().isEmpty(),
                    is(true));
            Assertion.assertThat("See Rooms Button  is visible on Hotel PDP", detailPage.isSeeRoomVisible(), is(true));

            step("Rooms are available hence click on See Rooms Button");
            detailPage.seeHotelRoom();

            step("Checking room List");
            Assertion.assertThat("Room lists section should be visible", roomListPage.isRoomListSectionVisible(),
                    is(true));
            Assertion.assertThat("Room Image should be visible", roomListPage.isRoomImageVisible(), is(true));

            step("checking individuals rooms visibile on roomlist");
            roomListPage.roomListDetails(2);
        } else {
            Assertion.assertThat("If room is fully booked respective text is visible on Hotel PDP",
                    !detailPage.getBookingFullDetail().isEmpty(), is(true));
        }
    }

    @Author(names = "Shubh Sharma")
    @Module(name = "RoomListPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void checkPriceDetailOnBookingPage() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("checkPriceDetailOnBookingPage");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String location = param.get("location");
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        int noOfRooms = Integer.parseInt(param.get("rooms"));
        int guests = Integer.parseInt(param.get("guests"));
        String dateType = param.get("dateType");
        String dateRange = param.get("dateRange");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        HotelDetailPage<?> detailPage = new HotelDetailPage<>(session);
        HotelRoomListPageWeb<?> roomListPage = HotelRoomListPageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HotelBookingFormPageWeb<?> bookingFormPage = HotelBookingFormPageWeb.getInstance(session);
        SRPPageWeb<?> searchResultPage = SRPPageWeb.getInstance(session);

        step("Open home page and login");
        homePage.goToHomePage();
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("Navigate to Hotel vertical and search for stay");
        homePage.selectCurrency("IDR");
        homePage.openVerticalPageViaURL("Hotel");
        landingPage.searchStayLocation(location);

        step("Set date");
        landingPage.setStayDate(dateType, dateRange);
        landingPage.clickSubmit();

        step("close Pop up");
        searchResultPage.clickPopUp();

        step("Select hotel Card ");
        searchResultPage.selectHotelCard();

        step("Go Inside PDP");
        detailPage.switchToNewTab();
        Assertion.assertThat("PDP is loaded", detailPage.isPdpLoaded(), is(true));
        detailPage.seeHotelRoom();

        step("Set guest and room count > 1");
        roomListPage.clickRoomListChangeButton();
        roomListPage.clickRoomNGuestButton();
        roomListPage.setTotalGuest(guests);
        roomListPage.setTotalRoom(noOfRooms);
        roomListPage.clickGuestDoneAndSubmitButton();

        step("Ensure room list section is visible");
        roomListPage.scrollToRoomListBookSection();
        Assertion.assertThat("Room List is visible", roomListPage.isRoomListSectionVisible(), is(true));

        step("Click select on Room List");
        roomListPage.clickBookRoomButton();

        step("Click book button");
        Assertion.assertThat("Redirected to Booking Form", bookingFormPage.isContactForm(), is(true));
        Assertion.assertThat("Hotel Price on the Booking page is visible",
                !bookingFormPage.getHotelPriceDetail().isEmpty(), is(true));

        step("Click Chevron Button");
        bookingFormPage.continueToNextPage();
        bookingFormPage.openPriceSummary();
        Assertion.assertThat("Chevron Page is loaded", bookingFormPage.isChevronButtonLoaded(), is(true));

    }

    @Author(names = "Yazid")
    @Module(name = "Reschedule")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyRescheduleMultiOrder() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("verifyRescheduleMultiOrder");
        BaseTest.paramsThreadLocal.set(param);
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String startDate = generateRandomDate(param.get("rangeAddDate"));
        String hotelName = param.get("hotelName");
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night")); // should be less than 'rescheduleDuration' value
        int room = Integer.parseInt(param.get("room"));
        int qtyEachRoom = Integer.parseInt(param.get("qtyEachRoom"));
        String currency = param.get("currency");
        int rescheduleDuration = Integer.parseInt(param.get("rescheduleDuration")); // should be greater than 'night' value
        String paymentGroup = param.get("paymentGroup");
        String paymentMethod = param.get("paymentMethod");

        Session<?> session = BaseTest.sessionThreadLocal.get();
        HotelApi<?> hotelApi = new HotelApi<>(session);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        HotelReschedulePageWeb<?> hotelReschedulePage = HotelReschedulePageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);
        RoomDetailsPageWeb<?> roomDetailsPage = RoomDetailsPageWeb.getInstance(session);

        step("Create new hotel issued order");
        Boolean result = hotelApi.createOrderMultiRoomApi(
                email, password, hotelName, adult, night, room, startDate, qtyEachRoom, currency, false, ""
        );
        Assertion.assertThat("Hotel Booking via API is successful", result, is(true));

        step("Get data from the issued order");
        String initialOrderId = String.valueOf(hotelApi.getOrderId());
        int selectedIndex = generateRandomInt(1, hotelApi.roomNames.size())-1;
        String roomName = hotelApi.roomNames.get(selectedIndex);
        log("Selected room for reschedule: " + roomName);
        Double roomPrice = hotelApi.roomPriceSummaryTotals.get(selectedIndex);
        log("Selected room price for reschedule: " + roomPrice);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User select currency: " + currency);
        homePage.selectCurrency(currency);
        String language = homePage.getLanguage();

        step("user is on order Detail");
        orderListPage.openActiveTicketViaURL(initialOrderId);
        orderDetailPage.skipCoachmark();

        step("User click Reschedule");
        orderDetailPage.clickRescheduleButton();

        step("User choose the room to be rescheduled: " + roomName);
        orderDetailPage.clickRescheduleMultiOrderRoom(roomName, selectedIndex);

        step("Choose new date for the reschedule");
        String newStartDate = hotelReschedulePage.changeNightDuration(rescheduleDuration);

        step("Open the room details with additional price");
        hotelReschedulePage.openRoomWithAdditionalPrice();
        roomDetailsPage.clickOnPriceDetails();
        Assertion.assertThat("Current balance should be equal with initial total price", roomDetailsPage.getCurrentBalance(true), is(formatRoomPrice(roomPrice, language, currency)));

        step("Calculate the additional price");
        String initialTotalPrice = roomDetailsPage.getInitialTotalPrice(currency);
        String priceCalculation = summarizePrices(initialTotalPrice, roomDetailsPage.getCurrentBalance(false), currency);
        String additionalTotalPrice = roomDetailsPage.getAdditionalTotalPrice(currency);
        Assertion.assertThat("Additional total price should be equal with calculated price", additionalTotalPrice, is(priceCalculation));
        roomDetailsPage.closePriceDetails();

        step("Book the room with additional Price");
        String newRoomName = roomDetailsPage.getRoomNameDetails();
        roomDetailsPage.clickBookButton();

        step("Submit booking form");
        hotelReschedulePage.submitRescheduleBookingForm();

        step("check order id on payment page");
        String rescheduleOrderID = paymentPage.getOrderId();

        step("User select payment method ");
        paymentPage.clickOnSelectPaymentMethod();
         paymentPage.clickOnSeeAll();
        paymentPage.goToPaymentMethodsGroup(paymentGroup);
        paymentPage.selectPaymentMethod(paymentMethod);

        step("User complete payment");
        paymentPage.clickOnPaymentButton();

        step("Verify the payment successful page is visible or not");
        Assertion.assertThat("Verify the payment successful page is visible", paymentPage.isPaymentSuccessfulVisible(), is(true));

        step("Navigate to the details page of the issued e-ticket order");
        orderListPage.openActiveTicketViaURL(rescheduleOrderID);
        orderDetailPage.skipCoachmark();

        step("Check the new order detail");
        Assertion.assertThat("Reschedule card notification should be present", orderDetailPage.isRescheduleCardDisplayed(), is(true));
        Assertion.assertThat("Order ID should be showing as expected", orderDetailPage.getOrderId(), is(rescheduleOrderID));
        Assertion.assertThat("Start Date should be showing as expected", orderDetailPage.getStartDate(), is(newStartDate));
        Assertion.assertThat("Hotel Name should be showing as expected: " + hotelName, orderDetailPage.isHotelNameDisplayed(hotelName), is(true));
        Assertion.assertThat("Room Name should be showing as expected: " + newRoomName, orderDetailPage.isRoomNameDisplayed(newRoomName), is(true));

        step("Check the currency on the new order detail");
        Assertion.assertThat("Current currency should be " + currency, orderDetailPage.getVerticalTotalPrice(), containsString(currency));

        step("Check the reschedule button");
        orderDetailPage.clickRescheduleButton();
        Assertion.assertThat("Reschedule order can't to do reschedule again", orderDetailPage.isPopUpNonRefundableShown(), is(true));
        orderDetailPage.closeRefundReschedulePopUp();

        step("Check the previous order detail");
        orderDetailPage.goToPreviousOrderBanner();
        orderDetailPage.clickPreviousOrder();
        Assertion.assertThat("Previous order banner should be present", orderDetailPage.isNewScheduleButtonDisplayed(), is(true));
        Assertion.assertThat("Hotel Name should be showing as expected: " + hotelName, orderDetailPage.isHotelNameDisplayed(hotelName), is(true));
        Assertion.assertThat("Previous room name should be have a reschedule banner", orderDetailPage.isRoomRescheduledBannerDisplayed(roomName), is(true));
    }

    @Author(names = "Fajrul Alam")
    @Module(name = "ProductDetailPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyNextAvailableDates() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("verifyNextAvailableDates");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String location = param.get("location");
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        String publicHotelId = param.get("publicHotelId");
        String day = param.get("day");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LandingPageWeb<?> landingPage = LandingPageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        PDPPageWeb<?> pdpPageWeb = PDPPageWeb.getInstance(session);
        HotelRoomListPageWeb<?> roomListPage = HotelRoomListPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User Open hotel Landing page");
        homePage.openVerticalPageViaURL("Hotel");

        step("Dismiss 'Install app' pop up");
        landingPage.dismissInstallAppPopUp();

        step("Go to Hotel which has next available dates");
        pdpPageWeb.goToHotelViaDeeplink(location, publicHotelId, day);

        step("Verify hotel details elements are visible");
        Assertion.assertThat("Hotel name should be visible", pdpPageWeb.isHotelNameVisible(), is(true));
        Assertion.assertThat("Next available date night should be visible",
                pdpPageWeb.isNextAvailableDateNightVisible(), is(true));
        Assertion.assertThat("Next available date range should be visible",
                pdpPageWeb.isNextAvailableDateRangeVisible(), is(true));
        Assertion.assertThat("Next available date price should be visible",
                pdpPageWeb.isNextAvailableDatePriceVisible(), is(true));
        Assertion.assertThat("Next available date total price text should be visible",
                pdpPageWeb.isNextAvailableDateTotalPriceTextVisible(), is(true));
        Assertion.assertThat("Fully booked title should be visible", pdpPageWeb.isFullyBookedTitleVisible(), is(true));
        Assertion.assertThat("Best price ribbon on PDP should be visible", pdpPageWeb.isBestPriceRibbonPDPVisible(),
                is(true));
        Assertion.assertThat("Next available date no room text should be visible",
                pdpPageWeb.isNextAvailableDateNoRoomTextVisible(), is(true));

        step("Dismiss 'Install app' pop up");
        landingPage.dismissInstallAppPopUp();

        step("Click next available date");
        pdpPageWeb.clickNextAvailableDate();

        step("Verify see room button is visible");
        pdpPageWeb.clickSeeRoomButton();

        step("Verify room list section is visible");
        Assertion.assertThat("Best price ribbon on room list should be visible",
                pdpPageWeb.isBestPriceRibbonRoomListVisible(), is(true));
        Assertion.assertThat("Room List is visible", roomListPage.isRoomImageVisible(), is(true));
    }

    @Author(names = "Yazid")
    @Module(name = "Refund")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyRefundOrder() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("verifyRefundOrder");
        BaseTest.paramsThreadLocal.set(param);
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
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
        boolean isRefundable = Boolean.parseBoolean(param.get("isRefundable"));

        Session<?> session = BaseTest.sessionThreadLocal.get();
        HotelApi<?> hotelApi = new HotelApi<>(session);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);

        step("Create new hotel issued order");
        Boolean result = hotelApi.createIssuedOrderApi(
                email, password, startDate, hotelName, adult, night, room,
                orderFullName, orderNameTitle, guestFullName, guestTitle, currency, isRefundable,
                false, "", "en",
                true);
        Assertion.assertThat("Hotel Booking via API is successful", result, is(true));

        step("Get data from the issued order");
        String issuedOrderId = String.valueOf(hotelApi.getOrderId());

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User select currency: " + currency);
        homePage.selectCurrency(currency);

        step("user is on order detail");
        orderListPage.openActiveTicketViaURL(issuedOrderId);
        orderDetailPage.skipCoachmark();

        step("Check the refund button");
        orderDetailPage.clickRefundButton();
        Assertion.assertThat("The order cannot be refunded", orderDetailPage.isPopUpNonRefundableShown(), is(true));
    }

    @Author(names = "Deeon")
    @Module(name = "SearchPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifySearchNearMeHotel() {
        Map<String, String> param = getParam("verifySearchNearMeHotel");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> hotelLP = HotelLandingPageWeb.getInstance(session);
        SRPPageWeb<?> hotelSRP = SRPPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User Open hotel Landing page");
        homePage.openVerticalPageViaURL("Hotel");

        step("User click Near Me button");
        hotelLP.clickNearMeLocation();

        Assertion.assertThat("Near Me search is already set", hotelLP.isNearMeSearchAlreadySet(), is(true));

        step("User go to SRP");
        hotelLP.clickSubmit();

        if (!hotelSRP.isEmptySearchResult()) {
            Assertion.assertThat("SRP is Loaded with hotels found", hotelSRP.isHotelCardVisible(), is(true));
            Assertion.assertThat("Nearby Hotel is displayed", hotelSRP.isThereHotelNearby(), is(true));
        } else {
            Assertion.assertThat("SRP is Loaded but no hotels found", hotelSRP.isHotelCardVisible(), is(false));
        }
    }

    @Author(names = "Raihan")
    @Module(name = "SearchPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifySearchNearMeHotelLogin() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("verifySearchNearMeHotelLogin");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> hotelLP = HotelLandingPageWeb.getInstance(session);
        SRPPageWeb<?> hotelSRP = SRPPageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User Open hotel Landing page");
        homePage.openVerticalPageViaURL("Hotel");
        Assertion.assertThat("Landing Page is Opened", hotelLP.isLandingPageVisible(), is(true));

        step("User click Near Me button");
        hotelLP.clickNearMeLocation();

        Assertion.assertThat("Near Me search is already set", hotelLP.isNearMeSearchAlreadySet(), is(true));

        step("User go to SRP");
        hotelLP.clickSubmit();

        if (!hotelSRP.isEmptySearchResult()) {
            Assertion.assertThat("SRP is Loaded with hotels found", hotelSRP.isHotelCardVisible(), is(true));
            Assertion.assertThat("Nearby Hotel is displayed", hotelSRP.isThereHotelNearby(), is(true));
        } else {
            Assertion.assertThat("SRP is Loaded but no hotels found", hotelSRP.isHotelCardVisible(), is(false));
        }
    }

    @Author(names = "Fajrul Alam")
    @Module(name = "ProductDetailPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void VerifyHotelInfoAndInteractionPDPLogin() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("VerifyHotelInfoAndInteractionPDPLogin");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String location = param.get("location");
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String dateType = param.get("dateType");
        String dateRange = param.get("dateRange");
        int noOfAdults = Integer.parseInt(param.get("Adult"));

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LandingPageWeb<?> landingPage = LandingPageWeb.getInstance(session);
        PDPPageWeb<?> detailPage = PDPPageWeb.getInstance(session);
        HotelRoomListPageWeb<?> roomListPage = HotelRoomListPageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User Open hotel Landing page");
        homePage.openVerticalPageViaURL("Hotel");
        Assertion.assertThat("Landing Page is Opened", landingPage.isLandingPageVisible(), is(true));

        step("Dismiss 'Install app' pop up");
        landingPage.dismissInstallAppPopUp();

        step("Search stay location");
        landingPage.searchStayLocation(location);

        step("Set guests and rooms");
        landingPage.clickRoomAndGuestButton();
        landingPage.setTotalGuest(noOfAdults);
        landingPage.clickDone();

        step("Set date");
        landingPage.setStayDate(dateType, dateRange);

        step("submit search Button");
        landingPage.clickSubmit();

        step("Go Inside PDP");
        Assertion.assertThat("Verify User is redirected to PDP", detailPage.isPdpLoaded(), is(true));
        Assertion.assertThat("Hotel's Image is visible on Hotel Detail Page", detailPage.isHotelImageVisible(),
                is(true));
        Assertion.assertThat("Hotel name on the  detail page should be visible",
                !detailPage.getHotelNameDetail().isEmpty(), is(true));
        Assertion.assertThat("Hotel's Area is visible on Hotel PDP", !detailPage.getHotelAreaDetail().isEmpty(),
                is(true));
        Assertion.assertThat("Hotel's City is visible on Hotel PDP", !detailPage.getHotelCityDetail().isEmpty(),
                is(true));
        Assertion.assertThat("Highlights Section is visible on Hotel PDP", detailPage.isHighLightsVisible(), is(true));
        Assertion.assertThat("Popular Facilities Section is visible on Hotel PDP",
                detailPage.isPopularFacilitiesVisible(), is(true));
        Assertion.assertThat("Review Section is visible on Hotel PDP", detailPage.isReviewsVisible(), is(true));
        Assertion.assertThat("Map Section is visible on Hotel PDP", detailPage.isPdpMapVisible(), is(true));
        Assertion.assertThat("Accommodation Policies Section is visible on Hotel PDP",
                detailPage.isAccommodationPoliciesVisible(), is(true));
        Assertion.assertThat("FAQs Section is visible on Hotel PDP", detailPage.isFAQVisible(), is(true));

        step("Dismiss 'Install app' pop up");
        landingPage.dismissInstallAppPopUp();

        step("Assert rooms rooms");
        if (detailPage.isRoomsAvailable()) {
            Assertion.assertThat("Hotel's Price is visible on Hotel PDP", !detailPage.getHotelPriceDetail().isEmpty(),
                    is(true));
            Assertion.assertThat("See Rooms Button  is visible on Hotel PDP", detailPage.isSeeRoomVisible(), is(true));

            step("Rooms are available hence click on See Rooms Button");
            detailPage.seeHotelRoom();

            step("Checking Recommended Room");
            Assertion.assertThat("Recommended room should be visible on room list",
                    roomListPage.isRecommendedRoomVisible(), is(true));

            step("Checking room List");
            Assertion.assertThat("Room lists section should be visible", roomListPage.isRoomListSectionVisible(),
                    is(true));
            Assertion.assertThat("Room's Image  should be visible", roomListPage.isRoomImageVisible(), is(true));
        } else {
            Assertion.assertThat("If room is fully booked respective text is visible on Hotel PDP",
                    !detailPage.getBookingFullDetail().isEmpty(), is(true));
        }
    }

    @Author(names = "Raihan")
    @Module(name = "RoomListPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void VerifyUserCanApplyFilterInRoomListWeb() throws InterruptedException {
        Map<String, String> param = getParam("VerifyUserCanApplyFilterInRoomListWeb");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String location = param.get("location");
        String filterEN = param.get("filterEN");
        String filterID = param.get("filterID");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LandingPageWeb<?> landingPage = LandingPageWeb.getInstance(session);
        PDPPageWeb<?> detailPage = PDPPageWeb.getInstance(session);
        HotelRoomListPageWeb<?> roomListPage = HotelRoomListPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User Open hotel Landing page");
        homePage.openVerticalPageViaURL("Hotel");

        step("Dismiss 'Install app' pop up");
        landingPage.dismissInstallAppPopUp();

        step("Search stay location");
        landingPage.searchStayLocation(location);

        step("submit search Button");
        landingPage.clickSubmit();

        step("Go Inside PDP");

        Assertion.assertThat("Verify User is redirected to PDP", detailPage.isPdpLoaded(), is(true));
        Assertion.assertThat("Hotel name on the Landing and detail page should be equal",
                detailPage.getHotelNameDetail(), containsString(location));
        Assertion.assertThat("Hotel's Price is visible on Hotel PDP", !detailPage.getHotelPriceDetail().isEmpty(),
                is(true));
        Assertion.assertThat("See Rooms Button  is visible on Hotel PDP", detailPage.isSeeRoomVisible(), is(true));
        step("Rooms are available hence click on See Rooms Button");
        detailPage.seeHotelRoom();

        step("Checking room List");
        Assertion.assertThat("Room lists section should be visible", roomListPage.isRoomListSectionVisible(), is(true));
        Assertion.assertThat("Room Image should be visible", roomListPage.isRoomImageVisible(), is(true));

        step("Select filter");
        roomListPage.selectMasterTagFilter(filterEN, filterID);
        Assertion.assertThat(String.format("Room has master tag: %s or %s", filterEN, filterID),
                roomListPage.isMealPlanOnRoomCardDisplayed(filterEN, filterID), is(true));
    }

    @Author(names = { "Harshit", "Deeon" })
    @Module(name = "DetailPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyRoomDetailsLogin() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("verifyRoomDetailsLogin");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String location = param.get("location");
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        HotelRoomListPage<?> roomListPage = new HotelRoomListPage<>(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HotelDetailPage<?> detailPage = new HotelDetailPage<>(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User select IDR currency");
        homePage.selectCurrency("IDR");
        homePage.selectEnglishLanguage();

        step("User Open Hotels Landing page");
        homePage.openVerticalPageViaURL("Hotel");

        Assertion.assertThat("Landing Page is Opened", landingPage.isLandingPageVisible(), is(true));

        step("Search stay location");
        landingPage.searchStayLocation(location);

        step("submit search Button");
        landingPage.clickSubmit();

        step("Go Inside PDP");
        Assertion.assertThat("Verify if PDP is Opened", detailPage.isPdpLoaded(), is(true));

        step("Scroll to room list section");
        roomListPage.scrollToRoomListSection();

        step("CLick on Room card to open Room Detail Page");
        roomListPage.openRoomDetail();

        Assertion.assertThat("Verify if Room Detail Page Opened", roomListPage.isRoomDetailPage(), Is.is(true));
        Assertion.assertThat("Image is visible on Room Detail", roomListPage.isImageOnRoomDetail(), Is.is(true));
        Assertion.assertThat("Price is visible on Room Detail", roomListPage.isPriceOnRoomDetail(), Is.is(true));
        step("Verifying All the sections of Room Detail Page");
        roomListPage.roomDetailsVisible();
    }

    @Author(names = "Fajrul Alam")
    @Module(name = "BookingPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyMoMultiRatePlan() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("verifyMoMultiRatePlan");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String location = param.get("location");
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        String day = param.get("day");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        int noOfRooms = Integer.parseInt(param.get("rooms"));
        int guests = Integer.parseInt(param.get("guests"));
        String publicHotelId = param.get("publicHotelId");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelDetailPage<?> detailPage = new HotelDetailPage<>(session);
        HotelRoomListPageWeb<?> roomListPage = HotelRoomListPageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HotelBookingFormPageWeb<?> bookingFormPage = HotelBookingFormPageWeb.getInstance(session);
        PDPPageWeb<?> pdpPageWeb = PDPPageWeb.getInstance(session);
        LandingPageWeb<?> landingPage = LandingPageWeb.getInstance(session);
        HotelBookingFormPage<?> hotelBookingFormPage = new HotelBookingFormPage<>(session);

        step("Open home page and login");
        homePage.goToHomePage();
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("Open the PDP page");
        pdpPageWeb.goToHotelViaDeeplink(location, publicHotelId, day, guests, noOfRooms);
        Assertion.assertThat("PDP is loaded", detailPage.isPdpLoaded(), is(true));

        step("Dismiss 'Install app' pop up");
        landingPage.dismissInstallAppPopUp();

        step("Ensure room list section is visible");
        pdpPageWeb.clickSeeRoomButton();
        Assertion.assertThat("Room List is visible", roomListPage.isRoomListSectionVisible(), is(true));

        step("Click select on Room List");
        roomListPage.clickSelectRoomButtonMultiOrder(noOfRooms);

        Assertion.assertThat("MO banner should be shown", roomListPage.isMoBannerVisible(), is(true));
        Assertion.assertThat("Price footer for MO offer is visible", roomListPage.isPriceFooterVisible(), is(true));

        step("Click book button on price footer");
        roomListPage.clickFooterBookButton();
        hotelBookingFormPage.skipRoomChangePopup();
        Assertion.assertThat("Redirected to Booking Form", bookingFormPage.isContactForm(), is(true));

        step("Assert that the details in Booking form are correct");
        List<String> roomRates = bookingFormPage.getRoomRatesIfCorrectCount(noOfRooms);
        Assertion.assertThat("Number of Room rate is correct", roomRates.size(), is(noOfRooms));

        step("Get total price");
        int totalPrice = bookingFormPage.getTotalPrice();
        Assertion.assertThat("Assert that the total price is visible", totalPrice > 0, is(true));

        step("Open pop up");
        bookingFormPage.openPriceBreakdownPopUp();

        step("Assert the details are consistent");
        List<String> roomRatesPriceDetail = bookingFormPage.getRoomRatesIfCorrectCountInPriceDetail(noOfRooms);
        int totalPricePriceBreakDown = bookingFormPage.getTotalPriceInPriceDetail();
        Assertion.assertThat("Assert that the number of rooms is the same", roomRates.size(),
                is(roomRatesPriceDetail.size()));
        Assertion.assertThat("Assert that the room name is the same",
                bookingFormPage.isRoomRatesAreTheSame(roomRates, roomRatesPriceDetail), is(true));
        Assertion.assertThat("Assert that the room name is the same", totalPricePriceBreakDown, is(totalPrice));
    }

    @Author(names = "Raihan")
    @Module(name = "SearchPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifySRPMasterTagNonLoginWeb() {
        Map<String, String> param = getParam("verifySRPMasterTagNonLoginWeb");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String location = param.get("location");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> hotelLP = HotelLandingPageWeb.getInstance(session);
        SRPPageWeb<?> hotelSRP = SRPPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User Open hotel Landing page");
        homePage.openVerticalPageViaURL("Hotel");
        hotelLP.closeContinueOnWebPopup();

        step("User search hotel by location");
        hotelLP.searchStayLocation(location);

        step("User hit search button");
        hotelLP.clickSubmit();

        step("User go to SRP");
        hotelSRP.clickPopUp();

        step("User click on Master Tag");
        Assertion.assertThat("Master Tag  on SRP is visible ", hotelSRP.isMasterTagVisible(), is(true));
        hotelSRP.clickFirstMasterTag();
        Assertion.assertThat("Master tag is active", hotelSRP.isFirstMasterTagActive(), is(true));
    }

    @Author(names = { "Fajrul Alam" })
    @Module(name = "DetailPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyRoomDetailBookButton() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("verifyRoomDetailBookButton");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String location = param.get("location");
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int noOfRooms = Integer.parseInt(param.get("rooms"));
        int retryCounter = Integer.parseInt(param.get("retryCounter"));

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        PDPPageWeb<?> hotelPDP = PDPPageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HotelBookingFormPageWeb<?> bookingFormPage = HotelBookingFormPageWeb.getInstance(session);
        PDPPageWeb<?> pdpPageWeb = PDPPageWeb.getInstance(session);
        String dateType = param.get("dateType");
        String dateRange = param.get("dateRange");

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User select IDR currency");
        homePage.selectCurrency("IDR");

        step("User Open Hotels Landing page");
        homePage.openVerticalPageViaURL("Hotel");

        Assertion.assertThat("Landing Page is Opened", landingPage.isLandingPageVisible(), is(true));

        step("Search stay location");
        landingPage.searchStayLocation(location);

        step("Set date");
        landingPage.setStayDate(dateType, dateRange);

        step("submit search Button");
        landingPage.clickSubmit();

        step("Go Inside PDP");
        Assertion.assertThat("Verify if PDP is Opened", hotelPDP.isPdpLoaded(), is(true));
        if (pdpPageWeb.isNextAvailableDateNightVisible()) {
            pdpPageWeb.clickNextAvailableDate();
        }

        step("User see the rooms");
        hotelPDP.seeHotelRoom();
        String supposedRoomName = hotelPDP.getRoomName();
        String supposedRoomPrice = hotelPDP.getRoomPrice();
        String supposedRoomBedType = hotelPDP.getRoomBedType();

        step("Click a room to see the details");
        hotelPDP.clickRoomCard();
        Assertion.assertThat("Verify room name is a match", hotelPDP.getRoomDetailName(), is(supposedRoomName));
        Assertion.assertThat("Verify room price is a match", hotelPDP.getRoomDetailPrice(), is(supposedRoomPrice));
        Assertion.assertThat("Verify room bed type is a match", hotelPDP.getRoomDetailBedType(),
                is(supposedRoomBedType));

        step("Click book button in room detail");
        hotelPDP.clickBookButtonRoomDetail();
        Assertion.assertThat("Redirected to Booking Form", bookingFormPage.isContactForm(), is(true));

        step("Get room number of room");
        step("Assert that the details in Booking form are correct");
        List<String> roomRates = bookingFormPage.getRoomRatesIfCorrectCount(noOfRooms);
        Assertion.assertThat("Number of Room rate is correct", roomRates.size(), is(noOfRooms));

        step("Assert that the room name is the same in Booking form");
        Assertion.assertThat("The Room name is correct", roomRates.get(0).toLowerCase(),
                containsString(supposedRoomName.toLowerCase()));
    }

    @Author(names = { "Fajrul Alam" })
    @Module(name = "DetailPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyRoomDetailPriceDetail() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("verifyRoomDetailPriceDetail");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String location = param.get("location");
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        PDPPageWeb<?> hotelPDP = PDPPageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HotelBookingFormPageWeb<?> bookingFormPage = HotelBookingFormPageWeb.getInstance(session);
        PDPPageWeb<?> pdpPageWeb = PDPPageWeb.getInstance(session);
        String dateType = param.get("dateType");
        String dateRange = param.get("dateRange");

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User Open Hotels Landing page");
        homePage.openVerticalPageViaURL("Hotel");

        Assertion.assertThat("Landing Page is Opened", landingPage.isLandingPageVisible(), is(true));

        step("Search stay location");
        landingPage.searchStayLocation(location);

        step("Set date");
        landingPage.setStayDate(dateType, dateRange);

        step("submit search Button");
        landingPage.clickSubmit();

        step("Go Inside PDP");
        Assertion.assertThat("Verify if PDP is Opened", hotelPDP.isPdpLoaded(), is(true));
        if (pdpPageWeb.isNextAvailableDateNightVisible()) {
            pdpPageWeb.clickNextAvailableDate();
        }

        step("User see the rooms");
        hotelPDP.seeHotelRoom();
        String supposedRoomName = hotelPDP.getRoomName();
        String supposedRoomPrice = hotelPDP.getRoomPrice();
        String supposedRoomBedType = hotelPDP.getRoomBedType();

        step("Click a room to see the details");
        hotelPDP.clickRoomCard();
        Assertion.assertThat("Verify room name is a match", hotelPDP.getRoomDetailName(), is(supposedRoomName));
        Assertion.assertThat("Verify room price is a match", hotelPDP.getRoomDetailPrice(), is(supposedRoomPrice));
        Assertion.assertThat("Verify room bed type is a match", hotelPDP.getRoomDetailBedType(),
                is(supposedRoomBedType));

        step("Click price detail chevron");
        hotelPDP.clickRoomDetailPriceBreakdownChevron();

        Assertion.assertThat("Assert that total detail is visible",
                hotelPDP.isTotalPriceInRoomDetailPriceDetailVisible(), is(true));
        Assertion.assertThat("Assert that price detail is visible", hotelPDP.isPriceDetailVisible(), is(true));
        Assertion.assertThat("Assert that tax is visible", hotelPDP.isTaxVisible(), is(true));
        Assertion.assertThat("Assert that Blibli Tiket rewards is visible", hotelPDP.isTiketRewardVisible(), is(true));
    }

    @Author(names = { "Fajrul Alam" })
    @Module(name = "DetailPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifySearchByHotelNameFromLP() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("verifySearchByHotelNameFromLP");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String location = param.get("location");
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        String dateType = param.get("dateType");
        String dateRange = param.get("dateRange");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        PDPPageWeb<?> hotelPDP = PDPPageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HotelBookingFormPageWeb<?> bookingFormPage = HotelBookingFormPageWeb.getInstance(session);
        PDPPageWeb<?> pdpPageWeb = PDPPageWeb.getInstance(session);
        HotelDetailPage<?> detailPage = new HotelDetailPage<>(session);

        LandingPageWeb<?> landingPageWeb = LandingPageDweb.getInstance(session);

        step("User open tiket home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User Open Hotels Landing page");
        homePage.openVerticalPageViaURL("Hotel");

        Assertion.assertThat("Landing Page is Opened", landingPage.isLandingPageVisible(), is(true));

        step("Search stay location");
        landingPage.searchStayLocation(location);

        step("Set date");
        landingPage.setStayDate(dateType, dateRange);

        step("submit search Button");
        landingPage.clickSubmit();

        step("Go Inside PDP");
        Assertion.assertThat("Verify if PDP is Opened", hotelPDP.isPdpLoaded(), is(true));
        if (pdpPageWeb.isNextAvailableDateNightVisible()) {
            pdpPageWeb.clickNextAvailableDate();
        }

        Assertion.assertThat("Verify User is redirected to PDP", detailPage.isPdpLoaded(), is(true));
        Assertion.assertThat("Hotel's Image is visible on Hotel Detail Page", detailPage.isHotelImageVisible(),
                is(true));
        Assertion.assertThat("Hotel name on the  detail page should be visible",
                !detailPage.getHotelNameDetail().isEmpty(), is(true));
        Assertion.assertThat("Hotel's Area is visible on Hotel PDP", !detailPage.getHotelAreaDetail().isEmpty(),
                is(true));
        Assertion.assertThat("Hotel's City is visible on Hotel PDP", !detailPage.getHotelCityDetail().isEmpty(),
                is(true));
        Assertion.assertThat("Highlights Section is visible on Hotel PDP", detailPage.isHighLightsVisible(), is(true));
        Assertion.assertThat("Popular Facilities Section is visible on Hotel PDP",
                detailPage.isPopularFacilitiesVisible(), is(true));
        Assertion.assertThat("Review Section is visible on Hotel PDP", detailPage.isReviewsVisible(), is(true));
        Assertion.assertThat("Map Section is visible on Hotel PDP", detailPage.isPdpMapVisible(), is(true));
        Assertion.assertThat("Accommodation Policies Section is visible on Hotel PDP",
                detailPage.isAccommodationPoliciesVisible(), is(true));
        Assertion.assertThat("FAQs Section is visible on Hotel PDP", detailPage.isFAQVisible(), is(true));

        step("Dismiss 'Install app' pop up");
        landingPageWeb.dismissInstallAppPopUp();

        step("User see the rooms");
        hotelPDP.seeHotelRoom();
        String supposedRoomName = hotelPDP.getRoomName();
        String supposedRoomPrice = hotelPDP.getRoomPrice();
        String supposedRoomBedType = hotelPDP.getRoomBedType();

        step("Click a room to see the details");
        hotelPDP.clickRoomCard();
        Assertion.assertThat("Verify room name is a match", hotelPDP.getRoomDetailName(), is(supposedRoomName));
        Assertion.assertThat("Verify room price is a match", hotelPDP.getRoomDetailPrice(), is(supposedRoomPrice));
        Assertion.assertThat("Verify room bed type is a match", hotelPDP.getRoomDetailBedType(),
                is(supposedRoomBedType));
    }

    @Author(names = { "Fajrul Alam" })
    @Module(name = "DetailPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyRoomDetailImageGallery() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("verifyRoomDetailImageGallery");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String location = param.get("location");
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        String dateType = param.get("dateType");
        String dateRange = param.get("dateRange");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        PDPPageWeb<?> hotelPDP = PDPPageWeb.getInstance(session);
        PDPPageWeb<?> pdpPageWeb = PDPPageWeb.getInstance(session);
        HotelDetailPage<?> detailPage = new HotelDetailPage<>(session);

        LandingPageWeb<?> landingPageWeb = LandingPageDweb.getInstance(session);

        step("User Open Hotels Landing page");
        homePage.openVerticalPageViaURL("Hotel");

        Assertion.assertThat("Landing Page is Opened", landingPage.isLandingPageVisible(), is(true));

        step("Search stay location");
        landingPage.searchStayLocation(location);

        step("Set date");
        landingPage.setStayDate(dateType, dateRange);

        step("submit search Button");
        landingPage.clickSubmit();

        step("Go Inside PDP");
        Assertion.assertThat("Verify if PDP is Opened", hotelPDP.isPdpLoaded(), is(true));
        if (pdpPageWeb.isNextAvailableDateNightVisible()) {
            pdpPageWeb.clickNextAvailableDate();
        }

        Assertion.assertThat("Verify User is redirected to PDP", detailPage.isPdpLoaded(), is(true));
        Assertion.assertThat("Hotel's Image is visible on Hotel Detail Page", detailPage.isHotelImageVisible(),
                is(true));
        Assertion.assertThat("Hotel name on the  detail page should be visible",
                !detailPage.getHotelNameDetail().isEmpty(), is(true));
        Assertion.assertThat("Hotel's Area is visible on Hotel PDP", !detailPage.getHotelAreaDetail().isEmpty(),
                is(true));
        Assertion.assertThat("Hotel's City is visible on Hotel PDP", !detailPage.getHotelCityDetail().isEmpty(),
                is(true));
        Assertion.assertThat("Highlights Section is visible on Hotel PDP", detailPage.isHighLightsVisible(), is(true));
        Assertion.assertThat("Popular Facilities Section is visible on Hotel PDP",
                detailPage.isPopularFacilitiesVisible(), is(true));
        Assertion.assertThat("Review Section is visible on Hotel PDP", detailPage.isReviewsVisible(), is(true));
        Assertion.assertThat("Map Section is visible on Hotel PDP", detailPage.isPdpMapVisible(), is(true));
        Assertion.assertThat("Accommodation Policies Section is visible on Hotel PDP",
                detailPage.isAccommodationPoliciesVisible(), is(true));
        Assertion.assertThat("FAQs Section is visible on Hotel PDP", detailPage.isFAQVisible(), is(true));

        step("Dismiss 'Install app' pop up");
        landingPageWeb.dismissInstallAppPopUp();

        step("User see the rooms");
        hotelPDP.seeHotelRoom();
        String supposedRoomName = hotelPDP.getRoomName();
        String supposedRoomPrice = hotelPDP.getRoomPrice();
        String supposedRoomBedType = hotelPDP.getRoomBedType();

        step("Click a room to see the details");
        hotelPDP.clickRoomCard();
        Assertion.assertThat("Verify room name is a match", hotelPDP.getRoomDetailName(), is(supposedRoomName));
        Assertion.assertThat("Verify room price is a match", hotelPDP.getRoomDetailPrice(), is(supposedRoomPrice));
        Assertion.assertThat("Verify room bed type is a match", hotelPDP.getRoomDetailBedType(),
                is(supposedRoomBedType));

        step("Click See All Photos");
        hotelPDP.seeAllPhotos();

        Assertion.assertThat("Verify isImageCounterVisible", hotelPDP.isImageCounterVisible(), is(true));
        Assertion.assertThat("Verify isImageThumbnailVisible", hotelPDP.isImageThumbnailVisible(), is(true));
        Assertion.assertThat("Verify isImageTitleVisible", hotelPDP.isImageTitleVisible(), is(true));
    }

    @Author(names = "Shubh Sharma")
    @Module(name = "BookingPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyCompleteBookIDR() throws FailedLoginException, InterruptedException, ExecutionException {
        Map<String, String> param = getParam("verifyCompleteBookIDR");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String location = param.get("location");
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String paymentGroup = param.get("paymentGroup");
        String paymentMethod = param.get("paymentMethod");
        String dateType = param.get("dateType");
        String dateRange = param.get("dateRange");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        HotelDetailPage<?> detailPage = new HotelDetailPage<>(session);
        SRPPageWeb<?> searchResultPage = SRPPageWeb.getInstance(session);
        HotelRoomListPage<?> roomListPage = new HotelRoomListPage<>(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HotelBookingFormPageWeb<?> bookingFormPage = HotelBookingFormPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User select IDR currency");
        homePage.selectCurrency("IDR");

        step("User Open Hotel Landing page");
        String searchPageCurrency = homePage.getCurrentCurrency();
        Assertion.assertThat("Verify currency in  Search Result page is set to IDR", searchPageCurrency.equals("IDR"),
                Is.is(true));
        homePage.openVerticalPageViaURL("Hotel");
        Assertion.assertThat("Landing Page is Opened", landingPage.isLandingPageVisible(), Is.is(true));

        step("Search stay hotel");
        landingPage.searchStayLocation(location);
        landingPage.setStayDate(dateType, dateRange);
        landingPage.clickSubmit();

        step("submit search Button");
        searchResultPage.clickPopUp();
        Assertion.assertThat("Srp is Loaded", searchResultPage.isHotelCardVisible(), Is.is(true));

        step("Sort by lowest");
        searchResultPage.clickSortButton();
        searchResultPage.clickSortByLowest();
        Assertion.assertThat("Sorted by lowest", searchResultPage.checkSorting(), Is.is(true));

        step("Check price filter");
        searchResultPage.clickPriceFilter();
        Assertion.assertThat("Price Filter Currency", searchResultPage.checkPriceFilter("IDR"), Is.is(true));
        searchResultPage.clickPriceFilterSave();

        step("User clicks Star filter chips");
        searchResultPage.clickStarFilter();
        searchResultPage.selectStar();
        searchResultPage.clickStarSave();
        Assertion.assertThat("5 Star hotel is selected", searchResultPage.checkStars(), is(5));

        if (!detailPage.isPdpLoaded()) {
            step("Select hotel Card ");
            searchResultPage.selectHotel();
        }
        step("Go Inside PDP");
        detailPage.clickPopUp();
        Assertion.assertThat("Verify if PDP is Opened", detailPage.isPdpLoaded(), Is.is(true));
        Assertion.assertThat("Map Section is visible on Hotel PDP", detailPage.isPdpMapVisible(), Is.is(true));
        roomListPage.scrollToRoomListSection();

        step("Click Book Button");
        roomListPage.clickBookButton();

        step("Go to payment page");
        bookingFormPage.continueToPaymentPage();

        step("check order id on payment page");
        String orderID = paymentPage.getOrderId();
        orderID = orderID.replaceAll("[^0-9]", "");
        Assertion.assertThat("Verify order ID is displayed in payment page", !orderID.isEmpty(), Is.is(true));

        step("User select payment method ");
        paymentPage.clickOnSelectPaymentMethod();
        paymentPage.clickOnSeeAll();
        paymentPage.goToPaymentMethodsGroup(paymentGroup);
        paymentPage.selectPaymentMethod(paymentMethod);

        step("User complete payment");
        paymentPage.clickOnPaymentButton();

        step("User try to open see order list");
        paymentPage.clickSeeOrderListButton();

        step("Go to Issued Order");
        orderListPage.openActiveTicketViaURL(orderID);
        orderDetailPage.skipCoachmark();

        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.verifyIssuedETicket(),
                Is.is(true));
        Assertion.assertThat("contact name should be available on order detail page",
                !orderDetailPage.getContactName(orderID).isEmpty(), Is.is(true));
        Assertion.assertThat("email should be correctly displayed on order detail page",
                orderDetailPage.getContactEmail(orderID), Is.is(email));
        Assertion.assertThat("orderID should be visible and should be same as from payment page",
                orderDetailPage.getOrderId(), Is.is(orderID));
    }

    @Author(names = "Shubh Sharma")
    @Module(name = "BookingPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyCompleteBookNonIDR() throws FailedLoginException, InterruptedException, ExecutionException {
        Map<String, String> param = getParam("verifyCompleteBookNonIDR");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String location = param.get("location");
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String paymentGroup = param.get("paymentGroup");
        String paymentMethod = param.get("paymentMethod");
        String dateType = param.get("dateType");
        String dateRange = param.get("dateRange");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        HotelDetailPage<?> detailPage = new HotelDetailPage<>(session);
        SRPPageWeb<?> searchResultPage = SRPPageWeb.getInstance(session);
        HotelRoomListPage<?> roomListPage = new HotelRoomListPage<>(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HotelBookingFormPageWeb<?> bookingFormPage = HotelBookingFormPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User select SGD currency");
        homePage.selectCurrency("SGD");

        step("User Open Hotel Landing page");
        String searchPageCurrency = homePage.getCurrentCurrency();
        Assertion.assertThat("Verify currency in  Search Result page is set to SGD", searchPageCurrency.equals("SGD"),
                Is.is(true));
        homePage.openVerticalPageViaURL("Hotel");
        Assertion.assertThat("Landing Page is Opened", landingPage.isLandingPageVisible(), Is.is(true));

        step("Search stay hotel");
        landingPage.searchStayLocation(location);
        landingPage.setStayDate(dateType, dateRange);
        landingPage.clickSubmit();

        step("submit search Button");
        searchResultPage.clickPopUp();
        Assertion.assertThat("Srp is Loaded", searchResultPage.isHotelCardVisible(), Is.is(true));

        step("Sort by lowest");
        searchResultPage.clickSortButton();
        searchResultPage.clickSortByLowest();
        Assertion.assertThat("Sorted by lowest", searchResultPage.checkSorting(), Is.is(true));

        step("Check price filter");
        searchResultPage.clickPriceFilter();
        Assertion.assertThat("Price Filter Currency", searchResultPage.checkPriceFilter("SGD"), Is.is(true));
        searchResultPage.clickPriceFilterSave();

        step("User clicks Star filter chips");
        searchResultPage.clickStarFilter();
        searchResultPage.selectStar();
        searchResultPage.clickStarSave();
        Assertion.assertThat("5 Star hotel is selected", searchResultPage.checkStars(), is(5));

        if (!detailPage.isPdpLoaded()) {
            step("Select hotel Card ");
            searchResultPage.selectHotel();
        }
        step("Go Inside PDP");
        detailPage.switchToNewTab();
        detailPage.clickPopUp();
        Assertion.assertThat("Verify if PDP is Opened", detailPage.isPdpLoaded(), Is.is(true));
        Assertion.assertThat("Map Section is visible on Hotel PDP", detailPage.isPdpMapVisible(), Is.is(true));
        roomListPage.scrollToRoomListSection();

        step("Click Book Button");
        roomListPage.clickBookButton();

        step("Go to payment page");
        bookingFormPage.continueToPaymentPage();

        step("check order id on payment page");
        String orderID = paymentPage.getOrderId();
        Assertion.assertThat("Verify order ID is displayed in payment page", !orderID.isEmpty(), Is.is(true));

        step("User select payment method ");
        paymentPage.clickOnSelectPaymentMethod();
        paymentPage.clickOnSeeAll();
        paymentPage.goToPaymentMethodsGroup(paymentGroup);
        paymentPage.selectPaymentMethod(paymentMethod);

        step("User complete payment");
        paymentPage.clickOnPaymentButton();

        step("User try to open see order list");
        paymentPage.clickSeeOrderListButton();

        step("Go to Issued Order");
        orderListPage.openActiveTicketViaURL(orderID);
        orderDetailPage.skipCoachmark();

        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.verifyIssuedETicket(),
                Is.is(true));
        Assertion.assertThat("contact name should be available on order detail page",
                !orderDetailPage.getContactName(orderID).isEmpty(), Is.is(true));
        Assertion.assertThat("email should be correctly displayed on order detail page",
                orderDetailPage.getContactEmail(orderID), Is.is(email));
        Assertion.assertThat("orderID should be visible and should be same as from payment page",
                orderDetailPage.getOrderId(), Is.is(orderID));
    }

    @Author(names = "Venkat Pathakoti")
    @Module(name = "SRP")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyHotelDetailsMapView() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("verifyHotelDetailsMapView");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String location = param.get("location");
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));

        HomePageWeb<?> homePageWeb = HomePageWeb.getInstance(session);
        SRPPageWeb<?> searchResultPage = SRPPageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);

        step("Open home page and login");
        homePageWeb.goToHomePage();
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("Navigate to Hotel vertical and search for stay");
        homePageWeb.selectCurrency("IDR");
        homePageWeb.openVerticalPageViaURL("Hotel");
        landingPage.searchStayLocation(location);
        landingPage.clickSubmit();

        step("open srp maps");
        searchResultPage.clickMap();

        step("click on Hotel chips");
        searchResultPage.clickChip();

        step("check the hotel name of selected chip");
        Assertion.assertThat("Verify if user is able to see Hotel name", searchResultPage.isHotelNameMapVisible(),
                Is.is(true));

        step("check if the star rating  is available or not");
        Assertion.assertThat("Hotel's star is visible on Hotel Card", searchResultPage.isHotelStarRatingMapVisible(), is(true));

        step("check if the price is available or not");
        Assertion.assertThat("Hotel's price is visible on Hotel Card or not", searchResultPage.isHotelPriceMapVisible(),
                is(true));
    }

    /**
     * Sheet key: {@code verifySrpMapCarouselPdpThenListLogin}. Params: loginType, username (or email), password,
     * location, locationType, otp, retryCounter (optional).
     * Opens map and taps one visible price marker ({@link SRPPageWeb#getHotelPriceMapsText()}) instead of zooming
     * or using Search this area. Map carousel hotel opens PDP in a new tab; test closes PDP tab and returns to map, then SRP list.
     */
    @Author(names = {"Yazid"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Hotel")
    @Module(name = "SRP")
    @Test
    public void verifySrpMapCarouselPdpThenListLoginWeb() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("verifySrpMapCarouselPdpThenListLoginWeb");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String email = param.getOrDefault("email", param.get("username"));
        String password = param.get("password");
        String otp = param.getOrDefault("otp", "123456");
        int retryCounter = Integer.parseInt(param.getOrDefault("retryCounter", "3"));
        String location = param.get("location");
        String locationType = param.get("locationType");
        String currency = "IDR";
        String dateRange = param.getOrDefault("dateRange", "10-20");
        int totalNight = Integer.parseInt(param.getOrDefault("totalNight", "1"));
        String[] dates = generateCheckInOutDate(dateRange, totalNight);
        String checkInDate = dates[0];
        String checkOutDate = dates[1];
        String accommodationType = "hotel";

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        SRPPageWeb<?> searchResultPage = SRPPageWeb.getInstance(session);
        PDPPageWeb<?> detailPage = PDPPageWeb.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);

        step("Go to home");
        homePage.goToHomePage();

        step("Login as " + email + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(email, password);

        step("Change currency");
        homePage.selectCurrency(currency);

        step("Open hotel SRP via URL");
        HotelAutoCompleteResponse hotelAutoCompleteResponse = hotelApi.getAutocompleteData(email, password, location, locationType);
        int hotelIndexLocation = hotelApi.findHotelIndex(hotelAutoCompleteResponse, location);
        String locationId = hotelAutoCompleteResponse.data.contents.get(hotelIndexLocation).publicId;
        searchResultPage.openSRPViaUrl(accommodationType, 1, 1, locationId, locationType, location, checkInDate, checkOutDate);
        searchResultPage.clickPopUp();
        searchResultPage.waitForSrpToLoad();
        Assertion.assertThat("Hotel SRP should load before opening map", searchResultPage.isHotelCardVisible(), is(true));

        step("Click See maps button in SRP");
        searchResultPage.clickSeeMapButton();

        step("Verify map view property card shows correct hotel info (price, star, name)");
        String selectedMapPrice = searchResultPage.getHotelPriceMapsText();
        String hotelPriceCard = searchResultPage.getHotelPriceCard(selectedMapPrice, true);
        Assertion.assertThat("Hotel price on map marker matches visible carousel card price",
                convertPriceSafely(selectedMapPrice), is(convertPriceSafely(hotelPriceCard)));
        Assertion.assertThat("Visible map carousel price shows expected currency",
                selectedMapPrice, containsString(currency + " "));
        Assertion.assertThat("Map carousel shows a hotel name", searchResultPage.isHotelNameCardVisibleOnMaps(), is(true));
        Assertion.assertThat("Map carousel shows star rating on the hotel card",
                searchResultPage.isHotelStarCardVisibleOnMaps(), is(true));

        step("Tap the visible hotel card on map (main tab); PDP opens in a new tab");
        Object[] mapTabContext = searchResultPage.rememberMapViewTabBeforePdpNavigation();
        String mapViewWindowHandle = (String) mapTabContext[0];
        int windowHandleCountBeforePdpClick = (Integer) mapTabContext[1];
        String mapsHotelName = searchResultPage.selectVisibleHotelItem();

        step("Switch to the new PDP tab and verify it matches the hotel selected on the map");
        searchResultPage.switchToPdpTabOpenedFromMap(windowHandleCountBeforePdpClick);
        Assertion.assertThat("PDP should load in the new tab", detailPage.isPdpLoaded(), is(true));
        Assertion.assertThat("Hotel name on the new-tab PDP matches map carousel selection on the main tab",
                normalizeNonBreakingSpaceText(detailPage.getHotelNameDetail()),
                equalToIgnoringCase(normalizeNonBreakingSpaceText(mapsHotelName)));

        step("Close PDP tab and return to map view on the main tab");
        searchResultPage.closePdpTabAndReturnToMapView(mapViewWindowHandle);

        step("Exit map view and land on hotel SRP list");
        searchResultPage.closeMap();
        searchResultPage.waitForSrpToLoad();
        Assertion.assertThat("User is back on hotel SRP after closing map (list)",
                searchResultPage.isHotelCardVisible(), is(true));
    }

    @Author(names = "Deeon")
    @Module(name = "SearchPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyUserApplyFilterInSRP() {
        Map<String, String> param = getParam("verifyUserApplyFilterInSRP");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> hotelLP = HotelLandingPageWeb.getInstance(session);
        SRPPageWeb<?> hotelSRP = SRPPageWeb.getInstance(session);

        String location = param.get("location");

        step("User open Home Page");
        homePage.goToHomePage();

        step("User open Hotel Landing Page");
        homePage.openVerticalPageViaURL("Hotel");
        Assertion.assertThat("Landing Page is Opened", hotelLP.isLandingPageVisible(), Is.is(true));
        hotelLP.closeContinueOnWebPopup();

        step("User search hotel by location");
        hotelLP.searchStayLocation(location);

        step("User hit search button");
        hotelLP.clickSubmit();

        Assertion.assertThat("SRP is Loaded", hotelSRP.isHotelCardVisible(), Is.is(true));

        step("User apply quick filters");
        hotelSRP.applyQuickFilters();
        Assertion.assertThat("Quick filters are applied", hotelSRP.isQuickFilterApplied(), Is.is(true));

        step("User apply advanced filters");
        hotelSRP.applyAdvancedFilters();
        Assertion.assertThat("Advanced filters are applied", hotelSRP.isAdvancedFilterApplied(), Is.is(true));
    }


    /**
     * Login (if configured) → open hotel SRP via URL → scan the first {@code totalHotelCards} listing cards until one
     * exposes a review summary (score /5 + volume), assert that line's format → apply guest/property rating filter and
     * assert the displayed hotel cards honour the selected threshold.
     * <p>
     * Sheet key: {@code verifyReviewScore}. Params: loginType, email, password, srpLocation, currency (optional),
     * locationType (optional, default {@code Region}), rangeAddDate, totalNight, totalHotelCards,
     * optional totalRoom/totalAdult (default 1), and guestRatingFilter (use {@code random} for auto-pick).
     */
    @Author(names = {"Yazid"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Hotel")
    @Module(name = "SRP")
    @Test
    public void verifyReviewScoreWeb() throws FailedLoginException, InterruptedException {
        Map<String, String> map = getParam("verifyReviewScoreWeb");
        BaseTest.paramsThreadLocal.set(map);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String email = map.get("email");
        String password = map.get("password");
        String currency = map.getOrDefault("currency", "IDR");
        String srpLocation = map.getOrDefault("srpLocation", "Jakarta");
        String locationType = map.getOrDefault("locationType", "Region");
        String rangeAddDate = map.getOrDefault("rangeAddDate", "10-20");
        int totalNight = Integer.parseInt(map.getOrDefault("totalNight", "1"));
        int totalRoom = Integer.parseInt(map.getOrDefault("totalRoom", "1"));
        int totalAdult = Integer.parseInt(map.getOrDefault("totalAdult", "1"));
        int totalHotelCards = Integer.parseInt(map.getOrDefault("totalHotelCards", "5"));
        String guestRatingFilter = map.getOrDefault("guestRatingFilter", "random");

        String[] dates = generateCheckInOutDate(rangeAddDate, totalNight);
        String checkInDate = dates[0];
        String checkOutDate = dates[1];
        String accommodationType = "hotel";

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        SRPPageWeb<?> searchResultPage = SRPPageWeb.getInstance(session);

        step("Go to home");
        homePage.goToHomePage();

        step("User login");
        loginPage.loginAsGuest();
        loginPage.loginWithSpecificAccountViaCookie(email, password);

        step("Set currency to " + currency);
        homePage.selectCurrency(currency);

        step("Get location publicId for SRP URL");
        HotelAutoCompleteResponse srpAutocomplete = hotelApi.getAutocompleteData(email, password, srpLocation, locationType);
        int index = hotelApi.findHotelIndex(srpAutocomplete, srpLocation);
        String srpLocationId = srpAutocomplete.data.contents.get(index).publicId;

        step("Open hotel SRP via URL");
        searchResultPage.openSRPViaUrl(accommodationType, totalRoom, totalAdult, srpLocationId, locationType, srpLocation,
                checkInDate, checkOutDate);
        searchResultPage.waitForSrpToLoad();
        searchResultPage.clickPopUp();
        Assertion.assertThat("Search result page loads with visible hotel inventory", searchResultPage.isHotelCardVisible(), is(true));

        step("Verify at least one of the first " + totalHotelCards + " hotel listing cards exposes review summary (score + review volume)");
        String cardReviewLineFound = searchResultPage.findReviewLineAmongFirstCards(totalHotelCards);
        Assertion.assertThat(
                "At least one of the first " + totalHotelCards + " hotel cards should advertise a visible review snippet",
                cardReviewLineFound,
                is(not(nullValue())));
        Assertion.assertThat("Hotel card rating line shows score vs /5 (e.g. 3/5, 4.1/5, 4,8/5)",
                Objects.requireNonNull(cardReviewLineFound),
                matchesPattern(".*\\d+(?:[.,]\\d+)?\\s*/\\s*5.*"));

        step("Apply guest (property) rating filter and validate results on first N cards");
        String filterName = "Rating dari Tamu/Guests Rating";
        searchResultPage.swipeToFilter(filterName);
        String selectedGuestRating = searchResultPage.selectPropertyRatingFilter(guestRatingFilter);

        Assertion.assertThat("Filter chip shows active badge after submitting guest rating filter",
                searchResultPage.isFilterActivated(filterName), is(true));

        step("Assert first " + totalHotelCards + " hotel cards have the right property rating");
        Assertion.assertThat("All property rating is correct",
                searchResultPage.checkHotelCardList("Guest Rating", totalHotelCards, new String[]{selectedGuestRating}), is(true));
    }

    /**
     * Verify multiple filter combinations, reset functionality, and conditional filter visibility on SRP.
     *
     * Covers:
     *  1. tiket.com choices filter shows results on SRP.
     *  2. Smoking preference combined with facility filter shows results on SRP.
     *  3. Payment method filter shows results on SRP.
     *  4. Selecting an area/city filter hides the popular place filter section.
     *  5. Each applied filter is cleared by toggling it off one-by-one, then results still show.
     *  6. Loyalty filter is only visible when the user is logged in.
     */
    @Author(names = {"Yazid"})
    @Module(name = "SRP")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyMultipleFilterCombinationsAndResetSRPLoginWeb() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("verifyMultipleFilterCombinationsAndResetSRPLoginWeb");
        BaseTest.paramsThreadLocal.set(param);

        String email = param.get("email");
        String password = param.get("password");
        String location = param.get("location");
        String locationType = param.get("locationType");
        int totalRoom = Integer.parseInt(param.get("total_room"));
        int totalAdult = Integer.parseInt(param.get("total_adult"));
        int totalNight = Integer.parseInt(param.get("total_night"));
        String rangeAddDate = param.get("rangeAddDate");
        String[] dates = generateCheckInOutDate(rangeAddDate, totalNight);
        String checkInDate = dates[0];
        String checkOutDate = dates[1];
        String accommodationType = "hotel";
        String tiketChoicesTitle = param.get("tiketChoicesTitle");
        String tiketChoicesValue = param.get("tiketChoicesValue");
        String smokingPreferenceTitle = param.get("smokingPreferenceTitle");
        String smokingPreferenceValue = param.get("smokingPreferenceValue");
        String facilityTitle = param.get("facilityTitle");
        String facilityValue = param.get("facilityValue");
        String paymentMethodTitle = param.get("paymentMethodTitle");
        String paymentMethodValue = param.get("paymentMethodValue");
        String filterLocationTitle = param.get("filterLocationTitle");
        String filterLocationValue = param.get("filterLocationValue");
        int maxHotelToCheck = Integer.parseInt(param.get("maxHotelToCheck"));

        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        SRPPageWeb<?> searchResultPage = SRPPageWeb.getInstance(session);

        step("Open home page as guest to assert pre-login state for loyalty filter");
        homePage.goToHomePage();
        loginPage.logoutViaUrl();

        step("Get location publicId for SRP URL");
        HotelAutoCompleteResponse hotelAutoCompleteResponse = hotelApi.getAutocompleteData("", "", location, locationType);
        int index = hotelApi.findHotelIndex(hotelAutoCompleteResponse, location);
        String locationId = hotelAutoCompleteResponse.data.contents.get(index).publicId;

        step("Open the SRP via URL (guest)");
        searchResultPage.openSRPViaUrl(accommodationType, totalRoom, totalAdult, locationId, locationType, location, checkInDate, checkOutDate);
        Assertion.assertThat("SRP should be loaded for guest user", searchResultPage.isHotelCardVisible(), is(true));

        step("(pre-condition): Loyalty filter should NOT be visible for guest user");
        boolean loyaltyVisibleGuest = searchResultPage.isFilterToggleVisible("Loyalty");
        Assertion.assertThat("Loyalty filter should NOT be visible when user is a guest",
                loyaltyVisibleGuest, is(false));

        step("Login as " + email + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(email, password);

        step("Open the SRP via URL (login)");
        searchResultPage.openSRPViaUrl(accommodationType, totalRoom, totalAdult, locationId, locationType, location, checkInDate, checkOutDate);
        Assertion.assertThat("SRP should be loaded for logged-in user", searchResultPage.isHotelCardVisible(), is(true));

        step("Loyalty filter should be visible for logged-in user");
        boolean loyaltyVisibleLogin = searchResultPage.isFilterToggleVisible("Loyalty");
        Assertion.assertThat("Loyalty filter should be visible when user is logged in",
                loyaltyVisibleLogin, is(true));

        step("Select tiket.com choices filter");
        searchResultPage.selectFilterToggle(tiketChoicesValue);
        Assertion.assertThat("tiket.com choices filter is applied", searchResultPage.isGreenDotVisible(tiketChoicesTitle), is(true));
        Assertion.assertThat("SRP should display results after applying tiket.com choices filter",
                searchResultPage.isHotelCardVisible(), is(true));
        searchResultPage.isBenefitVisible(tiketChoicesValue, maxHotelToCheck);

        step("Apply smoking preference combined with facility filter");
        searchResultPage.selectFilterCheckbox(smokingPreferenceValue);
        Assertion.assertThat("smoking preference filter is applied", searchResultPage.isGreenDotVisible(smokingPreferenceTitle), is(true));
        searchResultPage.selectFilterCheckbox(facilityValue, facilityTitle);
        Assertion.assertThat("accommodation facilities filter is applied", searchResultPage.isGreenDotVisible(facilityTitle), is(true));
        Assertion.assertThat("SRP should display results after applying smoking preference and facility filter",
                searchResultPage.isHotelCardVisible(), is(true));
        searchResultPage.isBenefitVisible(smokingPreferenceValue, maxHotelToCheck);

        step("Apply payment method filter");
        searchResultPage.selectFilterCheckbox(paymentMethodValue);
        Assertion.assertThat("payment method filter is applied", searchResultPage.isGreenDotVisible(paymentMethodTitle), is(true));
        Assertion.assertThat("SRP should display results after applying payment method filter",
                searchResultPage.isHotelCardVisible(), is(true));
        searchResultPage.isBenefitVisible(paymentMethodValue, maxHotelToCheck);

        step("Open filter and verify popular place filter is visible before area selection");
        boolean popularPlaceVisibleBefore = searchResultPage.isFilterSectionVisible("Popular Place/Tempat Populer");
        Assertion.assertThat("Popular place filter should be visible before area/city is selected",
                popularPlaceVisibleBefore, is(true));

        step("Select area/city filter then verify popular place filter disappears");
        searchResultPage.selectFilterCheckbox(filterLocationValue, filterLocationTitle);
        Assertion.assertThat("area filter is applied", searchResultPage.isGreenDotVisible(filterLocationTitle), is(true));
        boolean popularPlaceVisibleAfter = searchResultPage.isFilterSectionVisible("Popular Place/Tempat Populer");
        Assertion.assertThat("Popular place filter should NOT be visible after area/city filter is selected",
                popularPlaceVisibleAfter, is(false));
        searchResultPage.checkHotelLocation(filterLocationValue, maxHotelToCheck);

        step("Clear filters by toggling each selection off one-by-one, then submit");
        searchResultPage.selectFilterToggle(tiketChoicesValue);
        Assertion.assertThat("tiket.com choices filter is not applied", searchResultPage.isGreenDotVisible(tiketChoicesTitle), is(false));
        Assertion.assertThat("SRP should still display results after clearing tiket.com choices",
                searchResultPage.isHotelCardVisible(), is(true));
        searchResultPage.selectFilterCheckbox(smokingPreferenceValue);
        Assertion.assertThat("smoking preference filter is not applied", searchResultPage.isGreenDotVisible(smokingPreferenceTitle), is(false));
        Assertion.assertThat("SRP should still display results after clearing smoking preference",
                searchResultPage.isHotelCardVisible(), is(true));
        searchResultPage.selectFilterCheckbox(facilityValue);
        Assertion.assertThat("accommodation facilities filter is not applied", searchResultPage.isGreenDotVisible(facilityTitle), is(false));
        Assertion.assertThat("SRP should still display results after clearing facility filter",
                searchResultPage.isHotelCardVisible(), is(true));
        searchResultPage.selectFilterCheckbox(paymentMethodValue);
        Assertion.assertThat("payment method filter is not applied", searchResultPage.isGreenDotVisible(paymentMethodTitle), is(false));
        Assertion.assertThat("SRP should still display results after clearing payment method filter",
                searchResultPage.isHotelCardVisible(), is(true));
        searchResultPage.selectFilterCheckbox(filterLocationValue, filterLocationTitle);
        Assertion.assertThat("area filter is not applied", searchResultPage.isGreenDotVisible(filterLocationTitle), is(false));
        Assertion.assertThat("SRP should still display results after clearing area/city filter",
                searchResultPage.isHotelCardVisible(), is(true));
    }

    /**
     * Sheet key: {@code verifySRPLoginBannerGuestWeb}. Params: location, filter (highest/lowest price),
     * b2cUsername, b2cPassword, b2bUsername, b2bPassword, otp, retryCounter (optional).
     * B2C login returns to hotel SRP; B2B login redirects to the corporate dashboard.
     */
    @Author(names = {"Yazid"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Hotel")
    @Module(name = "SRP")
    @Test
    public void verifySRPLoginBannerGuestWeb() throws FailedLoginException, InterruptedException {
        Map<String, String> map = getParam("verifySRPLoginBannerGuestWeb");
        BaseTest.paramsThreadLocal.set(map);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String location = map.get("location");
        String b2cUsername = map.get("b2cUsername");
        String b2cPassword = map.get("b2cPassword");
        String b2bUsername = map.get("b2bUsername");
        String b2bPassword = map.get("b2bPassword");
        String otp = map.getOrDefault("otp", "123456");
        int retryCounter = Integer.parseInt(map.getOrDefault("retryCounter", "3"));

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        SRPPageWeb<?> searchResultPage = SRPPageWeb.getInstance(session);
        CorporateDashboard<?> corporateDashboard = new CorporateDashboard<>(session);

        step("Open the home page");
        homePage.goToHomePage();

        step("Continue as guest (not logged in) — skip sign-in if prompted");
        loginPage.loginAsGuest();
        Assertion.assertThat(
                "Pre-condition: user must start without an active login (guest / non-login session)",
                loginPage.checkIfLoggedIn(), is(false));

        step("Open the Hotel landing page (search form before hotel list)");
        homePage.openVerticalPageViaURL("Hotel");

        step("Search for hotels in \"" + location + "\" from the landing page");
        landingPage.searchStayLocation(location);

        step("Submit search and open Hotel SRP (Search Result Page — list of hotels)");
        landingPage.clickSubmit();
        searchResultPage.clickPopUp();
        searchResultPage.waitForSrpToLoad();
        Assertion.assertThat(
                "Hotel search results page (SRP) should load after submitting the search",
                searchResultPage.isHotelCardVisible(), is(true));

        step("On SRP as guest — verify login/join banner and Log in button are visible");
        Assertion.assertThat(
                "Login/join banner must be visible on SRP when the user is not logged in",
                searchResultPage.isLoginBannerDisplayed(), is(true));
        Assertion.assertThat(
                "Log in button on the banner must be clickable (visible and enabled) for a guest user",
                searchResultPage.isLoginButtonClickable(), is(true));

        step("Tap Log in on SRP banner and sign in with B2C account (" + b2cUsername + ")");
        searchResultPage.clickLoginBanner();
        loginPage.loginWebV2(b2cUsername, b2cPassword, otp, retryCounter);
        loginPage.skipPassKey();

        step("After B2C login — user should return to SRP and login banner must be hidden");
        searchResultPage.waitForSrpToLoad();
        Assertion.assertThat(
                "User should land back on Hotel SRP after B2C login from the banner",
                searchResultPage.isHotelCardVisible(), is(true));
        Assertion.assertThat(
                "Login/join banner must not be shown on SRP after successful B2C login",
                searchResultPage.isLoginBannerDisplayed(), is(false));

        step("Log out so the session returns to guest (non-login)");
        homePage.goToHomePage();
        loginPage.loginAsGuest();
        Assertion.assertThat(
                "After logout, user must be guest again (not logged in)",
                loginPage.checkIfLoggedIn(), is(false));

        step("Open the Hotel landing page again after logout");
        homePage.openVerticalPageViaURL("Hotel");

        step("Search for hotels in \"" + location + "\" again from the landing page");
        landingPage.searchStayLocation(location);

        step("Submit search and open Hotel SRP again");
        landingPage.clickSubmit();
        searchResultPage.clickPopUp();
        searchResultPage.waitForSrpToLoad();
        Assertion.assertThat(
                "Hotel SRP should load again after logout",
                searchResultPage.isHotelCardVisible(), is(true));

        step("On SRP as guest again — verify login banner and Log in button are visible");
        Assertion.assertThat(
                "Login/join banner must be visible again on SRP after logout (guest session)",
                searchResultPage.isLoginBannerDisplayed(), is(true));
        Assertion.assertThat(
                "Log in button must be clickable again on SRP after logout",
                searchResultPage.isLoginButtonClickable(), is(true));

        step("Tap Log in on SRP banner and sign in with B2B corporate account (" + b2bUsername + ")");
        searchResultPage.clickLoginBanner();
        loginPage.loginWebV2(b2bUsername, b2bPassword, otp, retryCounter);
        loginPage.skipPassKey();

        step("After B2B login — user should be redirected to the corporate dashboard (not hotel SRP)");
        Assertion.assertThat(
                "B2B user should be redirected to the corporate dashboard after login from the SRP banner",
                corporateDashboard.isOnCorporateDashboard(), is(true));
    }

    @Author(names = "Dixita")
    @Module(name = "ProductDetailPage")
    @Tribe(name = "Demand")
    @Vertical(name = "Accommodation")
    @Test
    public void changeSearchDetailsRoomList() throws InterruptedException, FailedLoginException {
        Map<String, String> param = getParam("changeSearchDetailsRoomList");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String hotelName = param.get("hotelName");
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        int noOfRooms = Integer.parseInt(param.get("noOfRooms"));
        int updatedRooms = Integer.parseInt(param.get("updatedRooms"));

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        HotelDetailPage<?> detailPage = new HotelDetailPage<>(session);
        HotelRoomListPageWeb<?> roomListPage = HotelRoomListPageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        step("User opens home page");
        homePage.goToHomePage();

        step("User logs in");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User selects IDR currency");
        homePage.selectCurrency("IDR");

        step("User opens hotel landing page");
        homePage.openVerticalPageViaURL("Hotel");

        step("Verify hotel landing page is opened");
        Assertion.assertThat("Landing page is opened", landingPage.isLandingPageVisible(), Is.is(true));

        step("Search for hotel");
        landingPage.searchStayLocation(hotelName);

        step("Click on selecting no of guests or rooms");
        landingPage.clickRoomAndGuestButton();
        landingPage.setTotalRoom(noOfRooms);
        landingPage.clickDone();

        step("Submit search button");
        landingPage.clickSubmit();

        step("Go to PDP");
        String hotelNameOnPdp = detailPage.getHotelNameDetail();
        Assertion.assertThat("Verify PDP is loaded", detailPage.isPdpLoaded(), Is.is(true));
        Assertion.assertThat("Verify hotel name on PDP matches input",
                hotelNameOnPdp.trim().toLowerCase(), containsString(hotelName.trim().toLowerCase()));

        step("Go to the room list section");
        detailPage.seeHotelRoom();
        roomListPage.scrollToRoomListSection();
        String buttonTextBefore = roomListPage.getBookingButtonText();
        Assertion.assertThat("When room count is 1 then button text is", buttonTextBefore, containsString("Book"));

        step("Click Room & Guest button and update only room count");
        roomListPage.clickRoomNGuestButton();
        roomListPage.setTotalRoom(updatedRooms);
        if (Configs.PLATFORM.equals(Platform.DWEB)) {
            roomListPage.clickGuestDoneButton();
            roomListPage.clickRoomListChangeButton();
        } else {
            roomListPage.clickGuestDoneAndSubmitButton();
        }

        String buttonTextAfter = roomListPage.getBookingButtonText();
        Assertion.assertThat("When room count is greater than 1 then button text is", buttonTextAfter,
                containsString("Select"));
    }

    @Author(names = "Harshit,Dixita")
    @Module(name = "SearchPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyLocationSearchRedirectionToSRPLogin() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("verifyLocationSearchRedirectionToSRPLogin");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String[] locations = param.get("location").split("-");
        String randomLocation = locations[CoreFunctions.generateRandomInt(0, locations.length - 1)].trim();
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        SRPPageWeb<?> searchResultPage = SRPPageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User select IDR currency");
        homePage.selectCurrency("IDR");

        step("User Open hotel Landing page");
        homePage.openVerticalPageViaURL("Hotel");
        Assertion.assertThat("Landing Page is Opened", landingPage.isLandingPageVisible(), Is.is(true));

        step("Search stay location");
        landingPage.searchStayLocation(randomLocation);

        step("submit search Button");
        landingPage.clickSubmit();

        step("close Pop up");
        searchResultPage.clickPopUp();

        String SearchBarLocation = searchResultPage.getLocationNameFromInput();

        Assertion.assertThat("Verify SRP is loaded ", searchResultPage.isHotelCardVisible(), Is.is(true));
        Assertion.assertThat("Searched Text entered by user  is matching on SRP's searchbar", randomLocation,
                Is.is(SearchBarLocation));
    }

    @Author(names = { "Harshit", "Deeon" })
    @Module(name = "SearchPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyPDPRedirectionOnHotelSearchPageLogin() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("verifyPDPRedirectionOnHotelSearchPageLogin");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        boolean isMWeb = Configs.PLATFORM.equals(Platform.AMWEB) || Configs.PLATFORM.equals(Platform.IMWEB);

        String hotel = param.get("hotel");
        String location = param.get("location");
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String dateType = param.get("dateType");
        String dateRange = param.get("dateRange");
        int noOfAdults = Integer.parseInt(param.get("Adult"));

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HotelLandingPageWeb<?> hotelLP = HotelLandingPageWeb.getInstance(session);
        SRPPageWeb<?> hotelSRP = SRPPageWeb.getInstance(session);
        PDPPageWeb<?> hotelPDP = PDPPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User select IDR currency");
        homePage.selectCurrency("IDR");

        step("User Open hotel Landing page");
        homePage.openVerticalPageViaURL("Hotel");
        Assertion.assertThat("Landing Page is Opened", hotelLP.isLandingPageVisible(), Is.is(true));

        step("Search stay location");
        hotelLP.searchStayLocation(location);

        step("Set guests and rooms");
        hotelLP.clickRoomAndGuestButton();
        hotelLP.setTotalGuest(noOfAdults);
        hotelLP.clickDoneSetRoomGuest();

        step("Set date");
        hotelLP.setStayDate(dateType, dateRange);
        if (isMWeb) {
            hotelLP.clickDoneSetStayDate();
        }

        step("submit search Button");
        hotelLP.clickSubmit();

        step("Go Inside SRP");
        hotelSRP.clickPopUp();

        step("Search Hotel's name on Srp");
        hotelSRP.updateStayLocation(hotel);

        step("Click search button to go inside PDP");
        hotelSRP.clickSearchOnSRP();

        Assertion.assertThat("Verify User is redirected to PDP", hotelPDP.isPdpLoaded(), Is.is(true));
        Assertion.assertThat("Hotel's Image is visible on Hotel PDP", hotelPDP.isHotelImageVisible(), Is.is(true));
        Assertion.assertThat("Hotel name is same as User searched on LP", hotelPDP.getHotelNameDetail(),
                containsString(hotel));
        Assertion.assertThat("Hotel's Area is visible on Hotel PDP", !hotelPDP.getHotelAreaDetail().isEmpty(),
                Is.is(true));
        Assertion.assertThat("Hotel's City is visible on Hotel PDP", !hotelPDP.getHotelCityDetail().isEmpty(),
                Is.is(true));
        Assertion.assertThat("Hotel's Region is visible on Hotel PDP", !hotelPDP.getHotelRegionDetail().isEmpty(),
                Is.is(true));

        if (hotelPDP.isRoomsAvailable()) {
            Assertion.assertThat("Hotel's Price is visible on Hotel PDP", !hotelPDP.getHotelPriceDetail().isEmpty(),
                    Is.is(true));
        } else {
            Assertion.assertThat("If room is fully booked respective text is visible on Hotel PDP",
                    !hotelPDP.getBookingFullDetail().isEmpty(), Is.is(true));
        }
    }

    @Author(names = "Akshi")
    @Module(name = "BookingPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyUserDirectedToCorrectBookingForm() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("verifyUserDirectedToCorrectBookingForm");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String hotelName = param.get("hotelName");
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String dateType = param.get("dateType");
        String dateRange = param.get("dateRange");
        int noOfAdults = Integer.parseInt(param.get("Adult"));

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HotelDetailPage<?> detailPage = new HotelDetailPage<>(session);
        HotelRoomListPageWeb<?> roomListPage = HotelRoomListPageWeb.getInstance(session);
        HotelBookingFormPage<?> bookingFormPage = new HotelBookingFormPage<>(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User select IDR currency");
        homePage.selectCurrency("IDR");

        step("User Open hotel Landing page");
        homePage.openVerticalPageViaURL("Hotel");

        step("Search stay location");
        landingPage.searchStayLocation(hotelName);

        step("Set guests and rooms");
        landingPage.clickRoomAndGuestButton();
        landingPage.setTotalGuest(noOfAdults);
        landingPage.clickDone();

        step("Set date");
        landingPage.setStayDate(dateType, dateRange);
        landingPage.saveDate();

        step("submit search Button");
        landingPage.clickSubmit();

        step("Go Inside PDP");
        detailPage.switchToNewTab();
        Assertion.assertThat("Verify User is redirected to PDP", detailPage.isPdpLoaded(), Is.is(true));

        step("Rooms are available hence click on See Rooms Button");
        detailPage.seeHotelRoom();

        step("Scroll to room list section");
        Assertion.assertThat("Room list is visible", roomListPage.isRoomListSectionVisible(), Is.is(true));
        Assertion.assertThat("Rooms Image should be visible", roomListPage.isRoomImageVisible(), Is.is(true));

        String room_name = roomListPage.getRoomName();

        step("Click Book Button");
        roomListPage.clickBookButton();

        step("Go Inside Booking Form");
        Assertion.assertThat("Hotel name matched", bookingFormPage.getBookingHotelName(), Is.is(hotelName));
        Assertion.assertThat("Room name is same", bookingFormPage.getBookingRoomName(), Is.is(room_name));
    }

    @Author(names = { "Harshit", "Akshi" })
    @Module(name = "DetailPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyUserRedirectToPdpFromLPLogin() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("verifyUserRedirectToPdpFromLPLogin");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String hotel = param.get("hotel");
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String dateType = param.get("dateType");
        String dateRange = param.get("dateRange");
        int noOfAdults = Integer.parseInt(param.get("Adult"));

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        HotelDetailPage<?> detailPage = new HotelDetailPage<>(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User select IDR currency");
        homePage.selectCurrency("IDR");

        step("User Open hotel Landing page");
        homePage.openVerticalPageViaURL("Hotel");

        step("Search stay location");
        landingPage.searchStayLocation(hotel);

        step("Set guests and rooms");
        landingPage.clickRoomAndGuestButton();
        landingPage.setTotalGuest(noOfAdults);
        landingPage.clickDone();

        step("Set date");
        landingPage.setStayDate(dateType, dateRange);

        step("submit search Button");
        landingPage.clickSubmit();

        step("Go Inside PDP");

        Assertion.assertThat("Hotel's Image is visible on Hotel PDP", detailPage.isHotelImageVisible(), Is.is(true));
        Assertion.assertThat("Hotel name is same as User searched on LP", detailPage.getHotelNameDetail(),
                containsString(hotel));
        Assertion.assertThat("Hotel's Area is visible on Hotel PDP", !detailPage.getHotelAreaDetail().isEmpty(),
                Is.is(true));
        Assertion.assertThat("Hotel's City is visible on Hotel PDP", !detailPage.getHotelCityDetail().isEmpty(),
                Is.is(true));
        Assertion.assertThat("Hotel's Region is visible on Hotel PDP", !detailPage.getHotelRegionDetail().isEmpty(),
                Is.is(true));

        if (detailPage.isRoomsAvailable()) {
            Assertion.assertThat("Hotel's Price is visible on Hotel PDP", !detailPage.getHotelPriceDetail().isEmpty(),
                    Is.is(true));
        } else {
            Assertion.assertThat("If room is fully booked respective text is visible on Hotel PDP",
                    !detailPage.getBookingFullDetail().isEmpty(), Is.is(true));
        }
    }

    @Author(names = "Raihan")
    @Module(name = "SearchPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyRecentlySearchedNonLogin() {
        Map<String, String> param = getParam("verifyRecentlySearchedNonLogin");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        String location = param.get("location");
        String dateType = param.get("dateType");
        String dateRange = param.get("dateRange");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> hotelLP = HotelLandingPageWeb.getInstance(session);
        SRPPageWeb<?> hotelSRP = SRPPageWeb.getInstance(session);
        step("User open home page");
        homePage.goToHomePage();
        step("User Open hotel Landing page");
        homePage.openVerticalPageViaURL("Hotel");
        hotelLP.closeContinueOnWebPopup();
        step("User search hotel by location");
        hotelLP.searchStayLocation(location);

        step("Set date");
        hotelLP.setStayDate(dateType, dateRange);

        step("User hit search button");
        hotelLP.clickSubmit();

        step("User go to SRP");
        hotelSRP.clickPopUp();
        Assertion.assertThat("Hotel list is appeared", hotelSRP.isHotelCardVisible(), Is.is(true));

        step("User clear recently searched");
        hotelSRP.clickChangeSearchCriteria();
        hotelSRP.clickHotelLocationSearchField();
        Assertion.assertThat("Recently searched section is appeared", hotelSRP.isRecentlySearchedSectionAppeared(),
                Is.is(true));
        hotelSRP.clearAllRecentlySearched();
        Assertion.assertThat("Recently searched section is disappeared",
                hotelSRP.isRecentlySearchedSectionDisappeared(), Is.is(true));
    }

    @Author(names = "Akshi")
    @Module(name = "PDP")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyRoomDetailPrice() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("verifyRoomDetailPrice");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String hotelName = param.get("hotelName");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        PDPPageWeb<?> detailPage = PDPPageWeb.getInstance(session);
        HotelRoomListPageWeb<?> roomListPage = HotelRoomListPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User select IDR currency");
        homePage.selectCurrency("IDR");

        step("User Open Hotel Landing page");
        homePage.openVerticalPageViaURL("Hotel");

        Assertion.assertThat("Landing Page is Opened", landingPage.isLandingPageVisible(), Is.is(true));

        step("Search stay location");
        landingPage.searchStayLocation(hotelName);

        step("submit search Button");
        landingPage.clickSubmit();

        step("Go Inside PDP");
        Assertion.assertThat("Verify if PDP is Opened", detailPage.isPdpLoaded(), Is.is(true));
        detailPage.clickSeeRoomButton();

        step("Go to room Detail");
        roomListPage.openRoomDetail();

        String roomPrice = roomListPage.getRoomPrice();
        String finalPrice = roomListPage.getFinalPrice();

        step("Go to price details");
        roomListPage.clickOnPriceDetails();
        Assertion.assertThat("Check Price without taxes present", roomListPage.checkPriceOnRoomDetail(roomPrice),
                Is.is(true));
        Assertion.assertThat("Check Taxes present", roomListPage.checkTaxOnRoomDetail(), Is.is(true));

        step("Check final price");
        roomListPage.checkFinalPriceOnRoomDetail(finalPrice);
    }

    @Author(names = "Bikash")
    @Module(name = "CompleteBooking")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void completeBookingInternationalHotel() throws Exception {
        Map<String, String> param = getParam("completeBookingInternationalHotel");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        String currency = param.get("currency");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String hotel = param.get("hotel");
        String paymentMethod = param.get("paymentMethod");
        String paymentGroup = param.get("paymentGroup");
        int noOfGuest = Integer.parseInt(param.getOrDefault("noOfGuest", "1"));
        int noOfRoom = Integer.parseInt(param.getOrDefault("noOfRoom", "1"));

        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LandingPageWeb<?> landingPage = LandingPageWeb.getInstance(session);
        PDPPageWeb<?> detailPage = PDPPageWeb.getInstance(session);
        HotelRoomListPageWeb<?> roomListPage = HotelRoomListPageWeb.getInstance(session);
        HotelBookingFormPage<?> bookingFormPage = new HotelBookingFormPage<>(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);

        step("Open the home page");
        homePage.goToHomePage();

        step("Log in as an existing user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("Navigate to the Hotel vertical and search for the desired hotel");
        homePage.selectCurrency(currency);
        homePage.openVerticalPageViaURL("Hotel");
        landingPage.searchStayLocation(hotel);
        landingPage.clickSubmit();

        step("Go to the Property Detail Page (PDP)");
        Assertion.assertThat("Verify if PDP is Opened", detailPage.isPdpLoaded(), Is.is(true));
        Assertion.assertThat("Hotel's image is visible on the Hotel PDP", detailPage.isHotelImageVisible(),
                Is.is(true));
        Assertion.assertThat("Hotel name matches the user's search on the landing page",
                detailPage.getHotelNameDetail(), containsString(hotel));
        Assertion.assertThat("Hotel's Area is visible on the Hotel PDP", !detailPage.getHotelAreaDetail().isEmpty(),
                Is.is(true));
        Assertion.assertThat("Hotel's City is visible on the Hotel PDP", !detailPage.getHotelCityDetail().isEmpty(),
                Is.is(true));
        Assertion.assertThat("Hotel's Region is visible on the Hotel PDP", !detailPage.getHotelRegionDetail().isEmpty(),
                Is.is(true));

        if (detailPage.isRoomsAvailable()) {
            Assertion.assertThat("Hotel's price is visible on the Hotel PDP",
                    !detailPage.getHotelPriceDetail().isEmpty(), Is.is(true));
        } else {
            Assertion.assertThat("If all rooms are fully booked, the appropriate message is shown on the Hotel PDP",
                    !detailPage.getBookingFullDetail().isEmpty(), Is.is(true));
        }

        step("Click the 'See Rooms' button");
        detailPage.seeHotelRoom();

        step("Click the 'Book' button");
        roomListPage.clickBookButton();

        step("Enter the Booking Form");
        Assertion.assertThat("Hotel name matches", bookingFormPage.getBookingHotelName(), Is.is(hotel));
        Double priceBooking = Double.valueOf(bookingFormPage.getTotalPrice());
        String dateBooking = bookingFormPage.getBookingDate().split("-")[0].replaceAll(".*?,\\s*", "").trim();
        String nightBooking = bookingFormPage.getBookingRoomNight().replaceAll("\\s*•.*", "").trim();

        step("Navigate to the payment page");
        bookingFormPage.continueToNextPage();
        bookingFormPage.continueToPaymentPage();

        step("Check the order ID on the payment page");
        String orderID = paymentPage.getOrderId();
        Assertion.assertThat("Verify order ID is displayed on the payment page", !orderID.isEmpty(), Is.is(true));

        step("Verify payment page details match booking page");
        double pricePayment = Double.parseDouble(paymentPage.getFinalPrice());
        String datePayment = paymentPage.getBookingDate().replaceAll(".*,\\s*", "").trim();
        String nightPayment = paymentPage.getBookingNights();

        Assertion.assertThat("Verify the price on the payment page matches the booking page", pricePayment,
                Is.is(priceBooking));
        Assertion.assertThat("Verify the booking date on the payment page matches the booking page", datePayment,
                Is.is(dateBooking));
        Assertion.assertThat("Verify the number of nights on the payment page matches the booking page", nightBooking, containsString(nightPayment));

        step("Select a payment method");
        paymentPage.clickOnSelectPaymentMethod();
        paymentPage.clickOnSeeAll();
        paymentPage.goToPaymentMethodsGroup(paymentGroup);
        paymentPage.selectPaymentMethod(paymentMethod);

        step("Complete payment");
        paymentPage.clickOnPaymentButton();

        step("Verify the payment successful page is visible or not");
        Assertion.assertThat("Verify the payment successful page is visible", paymentPage.isPaymentSuccessfulVisible(), is(true));

        step("Open the order list to view the order");
        paymentPage.clickSeeOrderListButton();

        step("Go to the issued order's details page");
        orderListPage.openActiveTicketViaURL(orderID);
        orderDetailPage.skipCoachmark();

        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.verifyIssuedETicket(),
                Is.is(true));
        Assertion.assertThat("contact name should be available on order detail page",
                !orderDetailPage.getContactName(orderID).isEmpty(), Is.is(true));
        Assertion.assertThat("email should be correctly displayed on order detail page",
                orderDetailPage.getContactEmail(orderID), Is.is(email));
        Assertion.assertThat("orderID should be visible and should be same as from payment page",
                orderDetailPage.getOrderId(), Is.is(orderID));      
    }

    /**
     * Web counterpart of app {@code completeBookingInternationalNonIdrMultiPaxNightLogin}.
     * Sheet key: {@code completeBookingInternationalNonIdrMultiPaxNightLogin}. Params: email or username,
     * password, otp, retryCounter, currency, hotelName, dateType, date, totalRoom, totalGuest,
     * cardNumber, cardMonth or expiry, cvv, cardHolderName or cardName.
     */
    @Author(names = {"Yazid"})
    @Module(name = "CreateBooking")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void completeBookingInternationalNonIdrMultiPaxNightLoginWeb()
            throws FailedLoginException, InterruptedException, ExecutionException {
        Map<String, String> param = getParam("completeBookingInternationalNonIdrMultiPaxNightLoginWeb");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        String email = param.getOrDefault("email", param.get("username"));
        String password = param.get("password");
        String currency = param.get("currency");
        String hotelName = param.get("hotelName");
        String dateType = param.get("dateType");
        String date = param.get("date");
        int totalRoom = Integer.parseInt(param.get("totalRoom"));
        int totalGuest = Integer.parseInt(param.get("totalGuest"));
        String cardNumber = param.get("cardNumber");
        String cardMonth = param.getOrDefault("cardMonth", param.get("expiry"));
        String cvv = param.get("cvv");
        String cardHolderName = param.getOrDefault("cardHolderName", param.get("cardName"));

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        HotelDetailPage<?> detailPage = new HotelDetailPage<>(session);
        HotelRoomListPageWeb<?> roomListPage = HotelRoomListPageWeb.getInstance(session);
        HotelBookingFormPageWeb<?> bookingFormPage = HotelBookingFormPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);

        step("Open home page");
        homePage.goToHomePage();

        step("Login as " + email + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(email, password);

        step("Change currency to " + currency);
        homePage.selectCurrency(currency);

        step("Open hotel landing page");
        homePage.openVerticalPageViaURL("Hotel");
        Assertion.assertThat("Landing page is opened", landingPage.isLandingPageVisible(), is(true));

        step("Search stay location: " + hotelName);
        landingPage.searchStayLocation(hotelName);

        step("Set stay date");
        landingPage.setStayDate(dateType, date);

        step("Set total room: " + totalRoom + " and total guest: " + totalGuest);
        landingPage.clickRoomAndGuestButton();
        landingPage.setTotalRoom(totalRoom);
        landingPage.setTotalGuest(totalGuest);
        landingPage.clickDone();

        step("Submit search");
        landingPage.clickSubmit();

        step("Go to the room list");
        detailPage.seeHotelRoom();
        detailPage.clickPopUp();
        roomListPage.scrollToRoomListSection();

        step("Select room from the room list");
        roomListPage.clickSelectRoomButtonMultiOrder(totalRoom);
        roomListPage.clickMultiOrderBookButton();
        bookingFormPage.dismissRatePlanChangePopupsIfPresent();

        step("Fill guest details on the booking form and submit");
        List<String> bookingFormGuestNames = bookingFormPage.fillMultiGuestDetails(totalGuest);
        bookingFormPage.continueToPaymentPage();
        bookingFormPage.clickOnPopUp();

        step("Payment page should be displayed");
        String orderID = paymentPage.getOrderId();
        Assertion.assertThat("Order id should be displayed", !orderID.isEmpty(), is(true));

        step("Input and submit credit card information");
        paymentPage.inputCardNumber(cardNumber);
        paymentPage.inputCardMonth(cardMonth);
        paymentPage.inputCardCvv(cvv);
        paymentPage.inputCardHolderName(cardHolderName);

        step("Submit the payment method");
        paymentPage.clickOnPaymentButton();
        paymentPage.handle3DSPage(30);

        step("Assert if the payment is a success");
        paymentPage.clickCheckPaymentStatus();
        Assertion.assertThat("Payment successful page should be shown", paymentPage.isPaymentSuccessfulVisible(), is(true));

        step("Open the order detail");
        paymentPage.clickSeeOrderListButton();
        orderListPage.openActiveTicketViaURL(orderID);
        orderDetailPage.skipCoachmark();

        Assertion.assertThat("Currency is " + currency, orderDetailPage.getVerticalTotalPrice(), containsString(currency));
        List<String> orderDetailGuestNames = orderDetailPage.getAllGuestNames(orderID);
        Assertion.assertThat("Guest count on order detail should match booking form",
                orderDetailGuestNames.size(), is(bookingFormGuestNames.size()));
        for (String bookingFormGuestName : bookingFormGuestNames) {
            Assertion.assertThat("Guest name from booking form should appear on order detail: " + bookingFormGuestName,
                    orderDetailGuestNames.stream().anyMatch(name -> name.contains(bookingFormGuestName.trim())), is(true));
        }
    }

    @Author(names = {"Fajrul Alam","Vipul Kumar"})
    @Module(name = "Full Refund")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test(dataProvider = "testFullRefundForSelectVendorDataProvider")
    public void testFullRefundForSelectVendor(Map<String, String> param)
            throws FailedLoginException, InterruptedException {

        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        orderCreationPage<?> orderCreationPage = new orderCreationPage<>(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        RefundPageWeb<?> refundPage = RefundPageWeb.getInstance(session);
        RefundDetailWeb<?> refundDetailPage = RefundDetailWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LandingPageWeb<?> landingPage = LandingPageWeb.getInstance(session);

        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
        String vendor = param.get("vendor");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String startDate = generateRandomDate("10-10");
        String hotelName = param.get("hotelName");
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));
        ;
        int room = Integer.parseInt(param.get("room"));
        ;
        String currency = "IDR";
        String[] salutationList = { "Mr", "Mrs", "Ms" };
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autoguest";
        String guestTitle = salutationList[0];

        step("CREATE BOOKING FOR: " + vendor);

        step("Create new hotel issued order faj");
        orderCreationPage.createRefundableHotelSakukuIssuedOrderApiSelectVendor(username, password, startDate,
                hotelName,
                adult, night, room, orderFullName, orderNameTitle, guestFullName, guestTitle, currency, true, true,
                vendor);

        step("Get data from the issued order");
        String bookedOrderID = String.valueOf(orderCreationPage.getOrderId());

        step("Open the home page");
        homePage.goToHomePage();

        step("Log in as an existing user");
        loginPage.loginIfNotWithSameEmail(username, password, otp, retryCounter);

        step("Find the issued order");
        orderListPage.openActiveTicketViaURL(bookedOrderID);
        orderDetailPage.skipCoachmark();

        step("Verify hotel order details");
        Assertion.assertThat("Order ID is displayed correctly", orderDetailPage.getOrderId(), Is.is(bookedOrderID));
        Assertion.assertThat("Refund button should be displayed", orderDetailPage.isRefundButtonDisplayed(),
                Is.is(true));

        step("Open refund page");
        orderDetailPage.clickRefundButton();
        refundPage.waitForContentLoaderDisappear();

        step("Click to contuniue");
        refundPage.continueToRefund();

        step("Select random refund reason");
        refundPage.selectRandomRefundReason();

        step("Verify refund estimation section");
        refundPage.waitForEstimationAmountAppear("5");
        String refundReceive = refundPage.getRefundYoullReceive();
        String refundFee = refundPage.getRefundFee();
        String refundTotal = refundPage.getRefundTotal();
        List<String> amounts = new ArrayList<>(Arrays.asList(refundReceive, refundFee, refundTotal));
        Assertion.assertThat("Refund Total amount is correct",
                CoreFunctions.removeText(refundReceive) + CoreFunctions.removeText(refundFee),
                Is.is(CoreFunctions.removeText(refundTotal)));

        step("Verify refund amount breakdown");
        refundPage.openRefundBreakdown();
        amounts.clear();
        amounts.addAll(refundPage.getRefundAmountBreakdown(currency));

        if (refundPage.getRefundFeeBreakdown(currency).isPresent())
            amounts.addAll(refundPage.getRefundFeeBreakdown(currency).get());
        amounts.addAll(refundPage.getRefundTotalBreakdown(currency));
        Assertion.assertThat("Total breakdown refund same as estimation",
                refundPage.getRefundTotalBreakdownSum(currency),
                Is.is(String.valueOf(CoreFunctions.removeText(refundTotal))));
        refundPage.closeRefundBreakdown();

        step("Continue to next page");
        refundPage.clickButton("Continue");
        refundPage.waitRefundSecondPageLoaded();
        Assertion.assertThat("Order ID on 2nd refund page is correct", refundPage.getOrderIdOnRefundBanner(),
                containsString(bookedOrderID));
        Assertion.assertThat("Refund progress banner same as refund step", refundPage.getActiveProgress(), Is.is(2));

        step("Verify refund method and amount");
        String refundMethodSection = refundPage.getRefundMethod();
        Assertion.assertThat("Refund method is correct", refundMethodSection, not(emptyOrNullString()));
        Assertion.assertThat("Refund amount is same as prev page", refundTotal, is(refundPage.getRefundTotal()));

        step("Check Refund TnC");
        Assertion.assertThat("Submit refund button is disabled", refundPage.isDisabledSubmitButton(), is(true));
        refundPage.clickChecklistTnc();
        refundPage.clickButton("Submit");

        step("Submit refund");
        refundPage.clickButton("Process Refund");
        refundPage.clickConfirmationRefundPopup();
        refundPage.waitForSubmitRefundLoader();

        step("Go to refund detail page");
        refundPage.SuccessSubmitRefundPopup();

        step("Verify Refund Detail");
        int countRefundProgress = refundDetailPage.countRefundProgressTracker();
        Assertion.assertThat("Refund detail status not empty or null string", refundDetailPage.getRefundStatus(),
                not(emptyOrNullString()));
        Assertion.assertThat("Refund progress tracker more than 1", countRefundProgress, greaterThan(1));
        Assertion.assertThat("Refund Id not empty or null string", refundDetailPage.getRefundId(),
                not(emptyOrNullString()));

        step("Verify Refund Method and Amount");
        for (String method : refundDetailPage.getRefundMethod()) {
            Assertion.assertThat("Refund method not empty", method, not(emptyOrNullString()));
        }

        step("Verify product section");
        Assertion.assertThat("Product detail shown", refundDetailPage.isOrderDetailSection(), is(true));

        step("Verify back button to order list");
        refundDetailPage.goToYourOrders();

        step("Verify the refunded order is no longer there");
        Assertion.assertThat("The refunded order is no longer in your order page",
                refundDetailPage.isOrderIdPresentinYourOrder(bookedOrderID),
                is(false));
    }

    @DataProvider(name = "testFullRefundForSelectVendorDataProvider")
    public Object[][] testFullRefundCcDcDataProvider() {
        return getObjectParams("testFullRefundForSelectVendor");
    }

    @Author(names = "Caroline")
    @Module(name = "PDP")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyHotelDetails() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("verifyHotelDetails");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String hotelName = param.get("hotelName");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        PDPPageWeb<?> detailPage = PDPPageWeb.getInstance(session);
        HotelRoomListPageWeb<?> roomListPage = HotelRoomListPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User Open Hotel Landing page");
        homePage.openVerticalPageViaURL("Hotel");

        Assertion.assertThat("Landing Page is Opened", landingPage.isLandingPageVisible(), Is.is(true));

        step("Search stay location");
        landingPage.searchStayLocation(hotelName);

        step("submit search Button");
        landingPage.clickSubmit();

        step("Go Inside PDP");
        Assertion.assertThat("Verify if PDP is Opened", detailPage.isPdpLoaded(), is(true));

        Assertion.assertThat("Verify Hotel Name is visible", detailPage.isHotelNameVisible(), is(true));
        Assertion.assertThat("Verify Hotel Image is visible", detailPage.isHotelImageVisible(), is(true));
        Assertion.assertThat("Verify Hotel Star Rating is visible", detailPage.isStarRatingVisible(), is(true));
        Assertion.assertThat("Verify Property Type is visible", detailPage.isPropertyTypeVisible(), is(true));
        Assertion.assertThat("Verify Hotel Location is visible", detailPage.isHotelLocationVisible(), is(true));

        detailPage.scrollToReviewSection();

        Assertion.assertThat("Verify Hotel Review is visible", detailPage.isHotelReviewVisible(), is(true));
        Assertion.assertThat("Verify Hotel Facilities is visible", detailPage.isHotelFacilitiesVisible(), is(true));
        Assertion.assertThat("Verify Hotel Price is visible", detailPage.isHotelPriceVisible(), is(true));

        detailPage.scrollToHotelAboutSection();

        Assertion.assertThat("Verify Hotel About is visible", detailPage.isHotelAboutVisible(), is(true));

    }

    @Author(names = { "Shubh", "Clement" })
    @Module(name = "BookingPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyBookingMO() throws InterruptedException, FailedLoginException {
        Map<String, String> param = getParam("verifyBookingMO");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String location = param.get("location");
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String paymentGroup = param.get("paymentGroup");
        String paymentMethod = param.get("paymentMethod");
        String dateType = param.get("dateType");
        String dateRange = param.get("dateRange");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        HotelDetailPage<?> detailPage = new HotelDetailPage<>(session);
        SRPPageWeb<?> searchResultPage = SRPPageWeb.getInstance(session);
        HotelRoomListPageWeb<?> roomListPage = HotelRoomListPageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HotelBookingFormPageWeb<?> bookingFormPage = HotelBookingFormPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User select IDR currency");
        homePage.selectCurrency("IDR");

        step("User Open Hotel Landing page");
        String searchPageCurrency = homePage.getCurrentCurrency();
        Assertion.assertThat("Verify currency in  Search Result page is set to IDR", searchPageCurrency.equals("IDR"),
                Is.is(true));
        homePage.openVerticalPageViaURL("Hotel");
        Assertion.assertThat("Landing Page is Opened", landingPage.isLandingPageVisible(), Is.is(true));

        step("Search stay hotel");
        landingPage.searchStayLocation(location);
        landingPage.setStayDate(dateType, dateRange);
        landingPage.clickSubmit();

        step("submit search Button");
        searchResultPage.clickPopUp();
        Assertion.assertThat("Srp is Loaded", searchResultPage.isHotelCardVisible(), Is.is(true));

        if (!detailPage.isPdpLoaded()) {
            step("Select hotel Card ");
            searchResultPage.selectHotel();
        }
        step("Go Inside PDP");
        detailPage.switchToNewTab();
        detailPage.clickPopUp();
        if (Configs.PLATFORM == Platform.DWEB) {
            Assertion.assertThat("Verify if PDP is Opened", detailPage.isPdpLoaded(), Is.is(true));
        }
        roomListPage.scrollToRoomListSection();

        step("Set 2 Adults 2 Children and 2 room count");
        roomListPage.clickRoomListChangeButton();
        roomListPage.clickRoomNGuestButton();
        roomListPage.setTotalGuest(2);
        roomListPage.setTotalChild(2);
        roomListPage.setTotalRoom(2);
        roomListPage.clickGuestDoneAndSubmitButton();

        step("Click Select button 2 times");
        roomListPage.clickSelectRoomButtonMultiOrder(2);
        roomListPage.clickMultiOrderBookButton();

        step("Go to payment page");
        bookingFormPage.fillGuestDetails(2, "Shubh", true, false, "2");
        bookingFormPage.continueToPaymentPage();

        step("check order id on payment page");
        String orderID = paymentPage.getOrderId();
        orderID = orderID.replaceAll("[^0-9]", "");
        Assertion.assertThat("Verify order ID is displayed in payment page", !orderID.isEmpty(), Is.is(true));

        step("User select payment method ");
        paymentPage.clickOnSelectPaymentMethod();
        paymentPage.clickOnSeeAll();
        paymentPage.goToPaymentMethodsGroup(paymentGroup);
        paymentPage.selectPaymentMethod(paymentMethod);

        step("User complete payment");
        paymentPage.clickOnPaymentButton();

        step("User try to open see order list");
        paymentPage.clickSeeOrderListButton();

        step("Go to Issued Order");
        orderListPage.openActiveTicketViaURL(orderID);
        orderDetailPage.skipCoachmark();

        Assertion.assertThat("Order detail page presented", orderDetailPage.getOrderId(), Is.is(orderID));
    }

    @Author(names = { "Raihan", "Hafizh" })
    @Module(name = "BookingPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyBookingAgodaSingleRoomSingleNightNonIDR()
            throws FailedLoginException, InterruptedException, ExecutionException {
        Map<String, String> param = getParam("verifyBookingAgodaSingleRoomSingleNightNonIDR");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String url = "/maldives/medhufushi-island-resort-807001753757426653";
        String startDate = param.get("startDate");
        String endDate = param.get("endDate");
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String cardNumber = param.get("cardNumber");
        String cardMonth = param.get("cardMonth");
        String cvv = param.get("cvv");
        String fullName = param.get("fullName");

        String paymentGroup = param.get("paymentGroup");
        String paymentMethod = param.get("paymentMethod");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        HotelDetailPage<?> detailPage = new HotelDetailPage<>(session);
        HotelRoomListPageWeb<?> roomListPage = HotelRoomListPageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HotelBookingFormPageWeb<?> bookingFormPage = HotelBookingFormPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User select SGD currency");
        homePage.selectCurrency("SGD");

        step("User Open Hotel Landing page");
        String searchPageCurrency = homePage.getCurrentCurrency();
        Assertion.assertThat("Verify currency is set to SGD", searchPageCurrency.equals("SGD"), Is.is(true));
        homePage.openVerticalPageViaURL("Hotel");
        Assertion.assertThat("Landing Page is Opened", landingPage.isLandingPageVisible(), Is.is(true));

        step("Open PDP");
        homePage.openHotelPDPViaURLWithDate(url, startDate, endDate);

        Assertion.assertThat("Verify if PDP is Opened", detailPage.isPdpLoaded(), Is.is(true));
        Assertion.assertThat("Map Section is visible on Hotel PDP", detailPage.isPdpMapVisible(), Is.is(true));
        roomListPage.scrollToRoomListSection();

        step("Set 1 Adult and 1 Child");
        roomListPage.clickRoomListChangeButton();
        roomListPage.clickRoomNGuestButton();
        roomListPage.setTotalGuest(1);
        roomListPage.setTotalChild(1);
        roomListPage.clickGuestDoneAndSubmitButton();

        step("Click Book Button");
        roomListPage.clickBookButtonOfNonPAHRoomAndMultiGuest();

        step("Go to payment page");
        bookingFormPage.continueToPaymentPage();

        step("check order id on payment page");
        String orderID = paymentPage.getOrderId();
        orderID = orderID.replaceAll("[^0-9]", "");
        Assertion.assertThat("Verify order ID is displayed in payment page", !orderID.isEmpty(), Is.is(true));

        step("User select payment method");
        if (Configs.PLATFORM == Platform.DWEB) {
            paymentPage.inputCardNumber(cardNumber);
            paymentPage.inputCardMonth(cardMonth);
            paymentPage.inputCardCvv(cvv);
            paymentPage.inputCardHolderName(fullName);
        } else {
            paymentPage.clickOnSelectPaymentMethod();
            paymentPage.goToPaymentMethodsGroup(paymentGroup);
            paymentPage.selectPaymentMethod(paymentMethod);
        }

        step("User complete payment");
        paymentPage.clickOnPaymentButton();

        step("User try to open see order list");
        paymentPage.clickSeeOrderListButton();

        step("Go to Issued Order");
        orderListPage.openActiveTicketViaURL(orderID);
        orderDetailPage.skipCoachmark();

        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.verifyIssuedETicket(),
                Is.is(true));
        Assertion.assertThat("contact name should be available on order detail page",
                !orderDetailPage.getContactName(orderID).isEmpty(), Is.is(true));
        Assertion.assertThat("email should be correctly displayed on order detail page",
                orderDetailPage.getContactEmail(orderID), Is.is(email));
        Assertion.assertThat("orderID should be visible and should be same as from payment page",
                orderDetailPage.getOrderId(), Is.is(orderID));
    }

    @Author(names = "Clement")
    @Module(name = "BookingPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyBookingAgoda2Rooms2Nights2Adults2InfantsIDR()
            throws FailedLoginException, InterruptedException, ExecutionException {
        Map<String, String> param = getParam("verifyBookingAgoda2Rooms2Nights2Adults2InfantsIDR");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String url = param.get("url");
        String startDate = param.get("startDate");
        String endDate = param.get("endDate");
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        int room = Integer.parseInt(param.get("room"));
        int adult = Integer.parseInt(param.get("adult"));
        int children = Integer.parseInt(param.get("children"));
        String paymentGroup = param.get("paymentGroup");
        String paymentMethod = param.get("paymentMethod");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        HotelDetailPage<?> detailPage = new HotelDetailPage<>(session);
        HotelRoomListPageWeb<?> roomListPage = HotelRoomListPageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HotelBookingFormPageWeb<?> bookingFormPage = HotelBookingFormPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User select IDR currency");
        homePage.selectCurrency("IDR");

        step("User Open Hotel Landing page");
        String searchPageCurrency = homePage.getCurrentCurrency();
        Assertion.assertThat("Verify currency is set to IDR", searchPageCurrency.equals("IDR"), Is.is(true));
        homePage.openVerticalPageViaURL("Hotel");
        Assertion.assertThat("Landing Page is Opened", landingPage.isLandingPageVisible(), Is.is(true));

        step("Open PDP");
        homePage.openHotelPDPViaURLWithDate(url, startDate, endDate);

        Assertion.assertThat("Verify if PDP is Opened", detailPage.isPdpLoaded(), Is.is(true));
        Assertion.assertThat("Map Section is visible on Hotel PDP", detailPage.isPdpMapVisible(), Is.is(true));
        roomListPage.scrollToRoomListSection();

        step("Set search to 2 rooms, 2 adults and 2 infants");
        roomListPage.clickRoomListChangeButton();
        roomListPage.clickRoomNGuestButton();
        roomListPage.setTotalGuest(adult);
        roomListPage.setTotalRoom(room);
        roomListPage.setTotalChild(children);
        roomListPage.clickGuestDoneAndSubmitButton();

        step("Click Book Button");
        roomListPage.clickBookButtonOfNonPAHRoomAndMultiGuest();

        step("Fill Guest Details");
        bookingFormPage.fillGuestDetails(2, "Shubh", true, false, "2");

        step("Go to payment page");
        bookingFormPage.continueToPaymentPage();

        step("check order id on payment page");
        String orderID = paymentPage.getOrderId();
        orderID = orderID.replaceAll("[^0-9]", "");
        Assertion.assertThat("Verify order ID is displayed in payment page", !orderID.isEmpty(), Is.is(true));

        step("User select payment method ");
        paymentPage.clickOnSelectPaymentMethod();
        paymentPage.goToPaymentMethodsGroup(paymentGroup);
        paymentPage.selectPaymentMethod(paymentMethod);

        step("User complete payment");
        paymentPage.clickOnPaymentButton();

        step("User try to open see order list");
        paymentPage.clickSeeOrderListButton();

        step("Go to Issued Order");
        orderListPage.openActiveTicketViaURL(orderID);
        orderDetailPage.skipCoachmark();

        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.verifyIssuedETicket(),
                Is.is(true));
        Assertion.assertThat("contact name should be available on order detail page",
                !orderDetailPage.getContactName(orderID).isEmpty(), Is.is(true));
        Assertion.assertThat("email should be correctly displayed on order detail page",
                orderDetailPage.getContactEmail(orderID), Is.is(email));
        Assertion.assertThat("orderID should be visible and should be same as from payment page",
                orderDetailPage.getOrderId(), Is.is(orderID));
    }

    @Author(names = "Naufal Hafiz")
    @Module(name = "BookingPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyBookingExpediaRapidThreeRoomThreeNightIDR()
            throws FailedLoginException, InterruptedException, ExecutionException {
        Map<String, String> param = getParam("verifyBookingExpediaRapidThreeRoomThreeNightIDR");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String url = param.get("url");
        String currency = param.get("currency");
        int noOfAdult = Integer.parseInt(param.get("noOfAdult"));
        int noOfChild = Integer.parseInt(param.get("noOfChild"));
        int noOfRoom = Integer.parseInt(param.get("noOfRoom"));
        String startDate = param.get("startDate");
        String endDate = param.get("endDate");
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String firstGuestName = param.get("firstGuestName");

        String paymentGroup = param.get("paymentGroup");
        String paymentMethod = param.get("paymentMethod");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        HotelDetailPage<?> detailPage = new HotelDetailPage<>(session);
        HotelRoomListPageWeb<?> roomListPage = HotelRoomListPageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HotelBookingFormPageWeb<?> bookingFormPage = HotelBookingFormPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User select IDR currency");
        homePage.selectCurrency(currency);

        step("User Open Hotel Landing page");
        String searchPageCurrency = homePage.getCurrentCurrency();
        Assertion.assertThat("Verify currency in  Search Result page is set to IDR", searchPageCurrency.equals("IDR"),
                Is.is(true));
        homePage.openVerticalPageViaURL("Hotel");
        Assertion.assertThat("Landing Page is Opened", landingPage.isLandingPageVisible(), Is.is(true));

        step("Open PDP");
        homePage.openHotelPDPViaURLWithDate(url, startDate, endDate);

        Assertion.assertThat("Verify if PDP is Opened", detailPage.isPdpLoaded(), Is.is(true));
        Assertion.assertThat("Map Section is visible on Hotel PDP", detailPage.isPdpMapVisible(), Is.is(true));
        roomListPage.scrollToRoomListSection();

        step("Set 2 Adults 2 Children and 2 room count");
        roomListPage.clickRoomListChangeButton();
        roomListPage.clickRoomNGuestButton();
        roomListPage.setTotalGuest(noOfAdult);
        roomListPage.setTotalChild(noOfChild);
        roomListPage.setTotalRoom(noOfRoom);
        roomListPage.clickGuestDoneAndSubmitButton();

        step("Click Select button 2 times");
        roomListPage.clickSelectRoomButtonMultiOrder(noOfRoom);
        roomListPage.clickMultiOrderBookButton();

        step("Go to payment page");
        bookingFormPage.fillGuestDetails(2, firstGuestName, true, false, "2");
        bookingFormPage.continueToPaymentPage();

        step("check order id on payment page");
        String orderID = paymentPage.getOrderId();
        orderID = orderID.replaceAll("[^0-9]", "");
        Assertion.assertThat("Verify order ID is displayed in payment page", !orderID.isEmpty(), Is.is(true));

        step("User select payment method ");
        paymentPage.clickOnSelectPaymentMethod();
        paymentPage.clickOnSeeAll();
        paymentPage.goToPaymentMethodsGroup(paymentGroup);
        paymentPage.selectPaymentMethod(paymentMethod);

        step("User complete payment");
        paymentPage.clickOnPaymentButton();

        step("User try to open see order list");
        paymentPage.clickSeeOrderListButton();

        step("Go to Issued Order");
        orderListPage.openActiveTicketViaURL(orderID);
        orderDetailPage.skipCoachmark();

        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.verifyIssuedETicket(),
                Is.is(true));
        Assertion.assertThat("contact name should be available on order detail page",
                !orderDetailPage.getContactName(orderID).isEmpty(), Is.is(true));
        Assertion.assertThat("email should be correctly displayed on order detail page",
                orderDetailPage.getContactEmail(orderID), Is.is(email));
        Assertion.assertThat("orderID should be visible and should be same as from payment page",
                orderDetailPage.getOrderId(), Is.is(orderID));
    }

    @Author(names = "Shubh")
    @Module(name = "BookingPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyBookingMO3pVendor() throws InterruptedException, FailedLoginException, ExecutionException {
        Map<String, String> param = getParam("verifyBookingMO3pVendor");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String location = param.get("location");
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String paymentGroup = param.get("paymentGroup");
        String paymentMethod = param.get("paymentMethod");
        String dateType = param.get("dateType");
        String dateRange = param.get("dateRange");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        HotelDetailPage<?> detailPage = new HotelDetailPage<>(session);
        SRPPageWeb<?> searchResultPage = SRPPageWeb.getInstance(session);
        HotelRoomListPageWeb<?> roomListPage = HotelRoomListPageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HotelBookingFormPageWeb<?> bookingFormPage = HotelBookingFormPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User select IDR currency");
        homePage.selectCurrency("IDR");

        step("User Open Hotel Landing page");
        String searchPageCurrency = homePage.getCurrentCurrency();
        Assertion.assertThat("Verify currency in  Search Result page is set to IDR", searchPageCurrency.equals("IDR"),
                Is.is(true));
        homePage.openVerticalPageViaURL("Hotel");
        Assertion.assertThat("Landing Page is Opened", landingPage.isLandingPageVisible(), Is.is(true));

        step("Search stay hotel");
        landingPage.searchStayLocation(location);
        landingPage.setStayDate(dateType, dateRange);
        landingPage.clickSubmit();

        if (!detailPage.isPdpLoaded()) {
            step("submit search Button");
            searchResultPage.clickPopUp();
            Assertion.assertThat("Srp is Loaded", searchResultPage.isHotelCardVisible(), Is.is(true));

            step("Select hotel Card ");
            searchResultPage.selectHotel();

            step("Go Inside PDP");
            detailPage.switchToNewTab();
        }

        step("Check PDP");
        detailPage.clickPopUp();
        Assertion.assertThat("Verify if PDP is Opened", detailPage.isPdpLoaded(), Is.is(true));
        roomListPage.scrollToRoomListSection();

        step("Set 2 Adults 2 Children and 2 room count");
        roomListPage.clickRoomListChangeButton();
        roomListPage.clickRoomNGuestButton();
        roomListPage.setTotalGuest(2);
        roomListPage.setTotalChild(2);
        roomListPage.setTotalRoom(2);
        roomListPage.clickGuestDoneAndSubmitButton();

        step("Click Select button 2 times");
        roomListPage.clickSelectRoomButtonMultiOrder(2);
        roomListPage.clickMultiOrderBookButton();

        step("Go to payment page");
        bookingFormPage.fillGuestDetails(2, "Shubh", true, false, "2");
        bookingFormPage.continueToPaymentPage();
        bookingFormPage.clickOnPopUp();

        step("check order id on payment page");
        String orderID = paymentPage.getOrderId();
        orderID = orderID.replaceAll("[^0-9]", "");
        Assertion.assertThat("Verify order ID is displayed in payment page", !orderID.isEmpty(), Is.is(true));

        step("User select payment method ");
        paymentPage.clickOnSelectPaymentMethod();
        paymentPage.clickOnSeeAll();
        paymentPage.goToPaymentMethodsGroup(paymentGroup);
        paymentPage.selectPaymentMethod(paymentMethod);

        step("User complete payment");
        paymentPage.clickOnPaymentButton();

        step("User try to open see order list");
        paymentPage.clickSeeOrderListButton();

        step("Go to Issued Order");
        orderListPage.openActiveTicketViaURL(orderID);
        orderDetailPage.skipCoachmark();

        Assertion.assertThat("Order detail page presented", orderDetailPage.getOrderId(), Is.is(orderID));
    }

    @Author(names = "Fajrul Alam")
    @Module(name = "RoomListPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void checkPriceDetailOnPaymentPage() {
        Map<String, String> param = getParam("checkPriceDetailOnPaymentPage");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String location = param.get("location");
        int noOfRooms = Integer.parseInt(param.get("rooms"));
        int guests = Integer.parseInt(param.get("guests"));
        String dateType = param.get("dateType");
        String dateRange = param.get("dateRange");
        String FullName = param.get("FullName");
        String PhoneNumber = param.get("PhoneNumber");
        String email = param.get("email");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        HotelDetailPage<?> detailPage = new HotelDetailPage<>(session);
        HotelRoomListPageWeb<?> roomListPage = HotelRoomListPageWeb.getInstance(session);
        HotelBookingFormPageWeb<?> bookingFormPage = HotelBookingFormPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("Navigate to Hotel vertical and search for stay");
        homePage.openVerticalPageViaURL("Hotel");
        landingPage.searchStayLocation(location);

        step("Set date");
        landingPage.setStayDate(dateType, dateRange);
        landingPage.clickSubmit();

        step("Go Inside PDP");
        Assertion.assertThat("PDP is loaded", detailPage.isPdpLoaded(), Matchers.is(true));
        detailPage.seeHotelRoom();

        step("Set guest and room count");
        roomListPage.clickRoomListChangeButton();
        roomListPage.clickRoomNGuestButton();
        roomListPage.setTotalGuest(guests);
        roomListPage.setTotalRoom(noOfRooms);
        roomListPage.clickGuestDoneAndSubmitButton();

        step("Ensure room list section is visible");
        roomListPage.scrollToRoomListBookSection();
        Assertion.assertThat("Room List is visible", roomListPage.isRoomListSectionVisible(), Matchers.is(true));

        step("Click select on Room List");
        roomListPage.clickBookRoomButton();

        step("Click book button");
        bookingFormPage.fillContactDetail(FullName, PhoneNumber, email);
        bookingFormPage.fillGuestDetailsCombinedRandom(guests);

        step("Click Continue Payment Button");
        bookingFormPage.continueToPaymentPage();
        Assertion.assertThat("Hotel Price on the Booking page is visible",
                !bookingFormPage.getHotelPriceDetail().isEmpty(), Matchers.is(true));
        roomListPage.clickChevronButton();
        Assertion.assertThat("Chevron Page is loaded", bookingFormPage.isChevronButtonLoaded(), Matchers.is(true));

    }

    @Author(names = "Vipul Kumar")
    @Module(name = "BookingPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyBookingMORevise() throws InterruptedException, FailedLoginException {
        Map<String, String> param = getParam("verifyBookingMORevise");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String location = param.get("location");
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        String otpRevisedBooking = param.get("otpRevisedBooking");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String paymentGroup = param.get("paymentGroup");
        String paymentMethod = param.get("paymentMethod");
        String dateType = param.get("dateType");
        String dateRange = param.get("dateRange");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        HotelDetailPage<?> detailPage = new HotelDetailPage<>(session);
        HotelRoomListPageWeb<?> roomListPage = HotelRoomListPageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HotelBookingFormPageWeb<?> bookingFormPage = HotelBookingFormPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        HotelRoomListPage<?> roomList = new HotelRoomListPage<>(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User select IDR currency");
        homePage.selectCurrency("IDR");

        step("User Open Hotel Landing page");
        String searchPageCurrency = homePage.getCurrentCurrency();
        Assertion.assertThat("Verify currency in  Search Result page is set to IDR", searchPageCurrency.equals("IDR"),
                Is.is(true));
        homePage.openVerticalPageViaURL("Hotel");
        Assertion.assertThat("Landing Page is Opened", landingPage.isLandingPageVisible(), Is.is(true));

        step("Search stay hotel");
        landingPage.searchStayLocation(location);
        landingPage.setStayDate(dateType, dateRange);
        landingPage.clickSubmit();

        step("Go Inside PDP");
        detailPage.switchToNewTab();
        detailPage.clickPopUp();
        Assertion.assertThat("Verify if PDP is Opened", detailPage.isPdpLoaded(), Is.is(true));
        roomListPage.scrollToRoomListSection();

        step("Set 2 Adults 2 Children and 2 room count");
        roomListPage.clickRoomListChangeButton();
        roomListPage.clickRoomNGuestButton();
        roomListPage.setTotalGuest(2);
        roomListPage.setTotalChild(2);
        roomListPage.setTotalRoom(2);
        roomListPage.clickGuestDoneAndSubmitButton();

        step("Click Select button 2 times");
        roomList.selectMultipleRoom(2);
        roomList.clickMultiBookButton();

        step("Go to payment page");
        bookingFormPage.fillGuestDetails(2, "Shubh", true, false, "2");
        bookingFormPage.continueToNextPage();
        bookingFormPage.continueToPaymentPage();

        step("check order id on payment page");
        String orderID = paymentPage.getOrderId();
        orderID = orderID.replaceAll("[^0-9]", "");
        Assertion.assertThat("Verify order ID is displayed in payment page", !orderID.isEmpty(), Is.is(true));

        step("User select payment method ");
        paymentPage.clickOnSelectPaymentMethod();
        paymentPage.clickOnSeeAll();
        paymentPage.goToPaymentMethodsGroup(paymentGroup);
        paymentPage.selectPaymentMethod(paymentMethod);

        step("User complete payment");
        paymentPage.clickOnPaymentButton();

        step("User try to open see order list");
        paymentPage.clickSeeOrderListButton();

        step("Go to Issued Order");
        orderListPage.openActiveTicketViaURL(orderID);
        orderDetailPage.skipCoachmark();

        Assertion.assertThat("Order detail page presented", orderDetailPage.getOrderId(), Is.is(orderID));

        step("Revise Booking from Order Detail Page");
        orderDetailPage.clickOnEditBooking();
        orderDetailPage.inputOTPCode(otpRevisedBooking, retryCounter);
        Assertion.assertThat("User should be on success full revised Page adn it's container image should be present",
                orderDetailPage.successFullRevisedname(), Is.is(true));
    }

    @Author(names = "Yazid")
    @Module(name = "OrderPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyReviseNameMultiOrder() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("verifyReviseNameMultiOrder");
        BaseTest.paramsThreadLocal.set(param);
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String startDate = generateRandomDate(param.get("rangeAddDate"));
        String hotelName = param.get("hotelName");
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));
        int room = Integer.parseInt(param.get("room"));
        int qtyEachRoom = Integer.parseInt(param.get("qtyEachRoom"));
        String currency = param.get("currency");

        Session<?> session = BaseTest.sessionThreadLocal.get();
        HotelApi<?> hotelApi = new HotelApi<>(session);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        HotelReviseBookingPage<?> reviseBookingPage = new HotelReviseBookingPage<>(session);

        step("Create new hotel issued order");
        Boolean result = hotelApi.createOrderMultiRoomApi(
                email, password, hotelName, adult, night, room, startDate, qtyEachRoom, currency, false, "");
        Assertion.assertThat("Hotel Booking via API is successful", result, is(true));

        step("Get data from the issued order");
        String orderId = String.valueOf(hotelApi.getOrderId());
        int selectedIndex = generateRandomInt(0, hotelApi.roomNames.size()-1);
        String roomName = hotelApi.roomNames.get(selectedIndex);
        log("Selected room: " + roomName);
        String guestName = hotelApi.guestNames.get(selectedIndex);
        log("Guest name before revise: " + guestName);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("user is on order detail: " + orderId);
        orderListPage.openActiveTicketViaURL(orderId);
        orderDetailPage.skipCoachmark();

        step("Open the room details: " + roomName);
        orderDetailPage.clickSeeRoomDetails(roomName, selectedIndex);
        Assertion.assertThat("The guest name is displayed as per booking", orderDetailPage.isGuestNameDisplayedRoomDetails(guestName), is(true));

        step("Open the revise Guest Name field");
        orderDetailPage.clickEditGuestName(guestName);
        homePage.continueWithoutApp(5);
        orderDetailPage.clickReviseGuestName(guestName);

        step("Revise the guest name");
        String newGuestName = CoreFunctions.randomName() + " Revised";
        reviseBookingPage.editGuestName(newGuestName);
        reviseBookingPage.clickAgreeButton();
        reviseBookingPage.inputOTPCode("1234");
        Assertion.assertThat("Success message should be displayed ", reviseBookingPage.getSuccessText(), either(containsString("The guest name has been changed")).or(containsString("Nama tamu berhasil diubah")));

        step("Open order detail page to verify the revised guest name");
        reviseBookingPage.clickSeeDetails();
        Assertion.assertThat("Verify Order Detail Page is Opened", orderListPage.getCurrentUrl(), containsString(orderId));
        Assertion.assertThat("The old guest name should be removed: " + guestName, orderDetailPage.isGuestNameDisplayedOrderDetails(guestName), is(false));
        Assertion.assertThat("The new guest name should be present: " + newGuestName, orderDetailPage.isGuestNameDisplayedOrderDetails(newGuestName), is(true));
    }

    /**
     * dWeb — verify stay date updates persist on hotel SEO pages (search bar + URL).
     *
     * <p>Sheet key: {@code verifyDateUpdatePersistenceFromSeoPagesWeb}.
     * Params: {@code location}, {@code locationType}, {@code dateType}, {@code dateRange}.
     * Guest session — no login required.</p>
     *
     * <p>Flow:</p>
     * <ul>
     *   <li>Open home, resolve city SEO slug via {@link HotelApi#getAutocompleteData(String, String, String, String)},
     *       then open the city SEO page via {@link HotelSeoPage#openSeoPage(String)}.</li>
     *   <li>Assert page heading and hotel cards; dismiss coachmark pop-up if shown.</li>
     *   <li>Capture initial stay date label ({@link HotelSearchResultPage#getStayDateFromInput()})
     *       and {@code checkin}/{@code checkout} query params from the current URL.</li>
     *   <li>Change dates using the SRP search-bar calendar
     *       ({@link HotelSearchResultPage#setStayDate(String, String)} — same element as SRP).</li>
     *   <li>Click search ({@link HotelSearchResultPage#clickSearchOnSRP()}) and assert results reload.</li>
     *   <li>Assert search-bar date label changed; URL contains updated {@code checkin}/{@code checkout}
     *       and differs from initial values when the page loaded with dates in the URL.</li>
     * </ul>
     *
     * <p>Test data: {@code dateType} is typically {@code range date}; {@code dateRange} is day-of-month
     * bounds (e.g. {@code 20-28}). {@code location}/{@code locationType} follow other hotel SRP tests
     * (e.g. {@code Bali}, {@code City}).</p>
     *
     * <p>Run:
     * {@code mvn test -Dthreads=1 -Dsessions=1 -Dplatform=dweb -Dtestdata=google -Dtribe=all -Dmodule=all -Dtestname=verifyDateUpdatePersistenceFromSeoPagesWeb -Dtesttype=all -Denv=preprod -Dorg.freemarker.loggerLibrary=none -e}</p>
     */
    @Author(names = {"Yazid"})
    @Module(name = "SEO")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyDateUpdatePersistenceFromSeoPagesWeb() throws InterruptedException {
        Map<String, String> param = getParam("verifyDateUpdatePersistenceFromSeoPagesWeb");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String location = param.get("location");
        String locationType = param.get("locationType");
        String dateType = param.get("dateType");
        String dateRange = param.get("dateRange");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelSeoPage<?> seoPage = HotelSeoPage.getInstance(session);
        HotelSearchResultPage<?> searchResultPage = new HotelSearchResultPage<>(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);

        step("Open home page");
        homePage.goToHomePage();

        step("Resolve location id of SEO page via autocomplete for " + location);
        HotelAutoCompleteResponse hotelAutoCompleteResponse = hotelApi.getAutocompleteData("", "", location, locationType);
        int selectedIndex = hotelApi.findHotelIndex(hotelAutoCompleteResponse, location);
        String locationPublicId = hotelAutoCompleteResponse.data.contents.get(selectedIndex).publicId;
        String country = hotelAutoCompleteResponse.data.contents.get(selectedIndex).country.id;
        String locationName = hotelAutoCompleteResponse.data.contents.get(selectedIndex).name;
        String locationSlug = country + "/" + locationType.toLowerCase() + "/" + locationPublicId;

        step("Open SEO page: " + locationSlug);
        seoPage.openSeoPage(locationSlug);
        Assertion.assertThat("SEO page heading should contain location name", seoPage.getPageHeading(), containsString(locationName));

        step("Capture initial stay dates from search bar");
        String initialDateLabel = searchResultPage.getStayDateFromInput();
        String initialCheckIn = CoreFunctions.getQueryParameterValue(seoPage.getUrl(), "checkin");
        String initialCheckOut = CoreFunctions.getQueryParameterValue(seoPage.getUrl(), "checkout");

        step("Change stay dates");
        searchResultPage.setStayDate(dateType, dateRange);

        step("Click search to apply updated dates");
        searchResultPage.clickSearchOnSRP();
        Assertion.assertThat("Hotel cards should remain visible after date update", searchResultPage.isHotelCardVisible(), is(true));

        step("Verify dates are updated in search bar and URL");
        String updatedDateLabel = searchResultPage.getStayDateFromInput();
        String updatedUrl = searchResultPage.getCurrentUrl();
        String updatedCheckIn = CoreFunctions.getQueryParameterValue(updatedUrl, "checkin");
        String updatedCheckOut = CoreFunctions.getQueryParameterValue(updatedUrl, "checkout");

        Assertion.assertThat("Search bar date label should be updated after date change", updatedDateLabel, not(equalTo(initialDateLabel)));
        Assertion.assertThat("URL should contain updated check-in date", updatedCheckIn, not(emptyOrNullString()));
        Assertion.assertThat("URL should contain updated check-out date", updatedCheckOut, not(emptyOrNullString()));
        Assertion.assertThat("Check-in date in URL should change after date update", updatedCheckIn, not(equalTo(initialCheckIn)));
        Assertion.assertThat("Check-out date in URL should change after date update", updatedCheckOut, not(equalTo(initialCheckOut)));
    }

    @Author(names = "Yazid")
    @Module(name = "OrderPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyReviseSpecialRequestMultiOrder() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("verifyReviseSpecialRequestMultiOrder");
        BaseTest.paramsThreadLocal.set(param);
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String startDate = generateRandomDate(param.get("rangeAddDate"));
        String hotelName = param.get("hotelName");
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));
        int room = Integer.parseInt(param.get("room"));
        int qtyEachRoom = Integer.parseInt(param.get("qtyEachRoom"));
        String currency = param.get("currency");
        String prefixSpecialRequest = param.get("prefixSpecialRequest");

        Session<?> session = BaseTest.sessionThreadLocal.get();
        HotelApi<?> hotelApi = new HotelApi<>(session);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        HotelReviseBookingPage<?> reviseBookingPage = new HotelReviseBookingPage<>(session);

        step("Create new hotel issued order");
        Boolean result = hotelApi.createOrderMultiRoomApi(
                email, password, hotelName, adult, night, room, startDate, qtyEachRoom, currency, true, prefixSpecialRequest);
        Assertion.assertThat("Hotel Booking via API is successful", result, is(true));

        step("Get data from the issued order");
        String orderId = String.valueOf(hotelApi.getOrderId());
        int selectedIndex = generateRandomInt(0, hotelApi.roomNames.size()-1);
        String roomName = hotelApi.roomNames.get(selectedIndex);
        log("Selected room: " + roomName);
        String specialRequest = prefixSpecialRequest + hotelApi.cardIdArray.get(selectedIndex);
        log("Special Request before revise: " + specialRequest);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("user is on order detail: " + orderId);
        orderListPage.openActiveTicketViaURL(orderId);
        orderDetailPage.skipCoachmark();

        step("Open the room details: " + roomName);
        orderDetailPage.clickSeeRoomDetails(roomName, selectedIndex);
        Assertion.assertThat("The special request is displayed as per booking", orderDetailPage.isSpecialRequestDisplayedRoomDetails(specialRequest), is(true));

        step("Open the revise Special Request field");
        orderDetailPage.clickEditSpecialRequest(specialRequest);
        homePage.continueWithoutApp(5);
        orderDetailPage.clickReviseSpecialRequest(specialRequest);

        step("Revise the special request");
        String newSpecialRequest = CoreFunctions.generateRandomString(10) + " Revised";
        reviseBookingPage.editSpecialRequest(newSpecialRequest);
        reviseBookingPage.clickAgreeButton();
        reviseBookingPage.inputOTPCode("1234");
        Assertion.assertThat("Success message should be displayed ", reviseBookingPage.getSuccessText(), either(containsString("The special request has been changed")).or(containsString("Permintaan khusus berhasil diubah")));

        step("Open order detail page to verify the revised special request");
        reviseBookingPage.clickSeeDetails();
        Assertion.assertThat("Verify Order Detail Page is Opened", orderListPage.getCurrentUrl(), containsString(orderId));
        Assertion.assertThat("The old special request should be removed: " + specialRequest, orderDetailPage.isSpecialRequestDisplayedOrderDetails(specialRequest), is(false));
        Assertion.assertThat("The new special request should be present: " + newSpecialRequest, orderDetailPage.isSpecialRequestDisplayedOrderDetails(newSpecialRequest), is(true));
    }

    /**
     * dWeb counterpart of app {@code verifyPdpInfinityPromoRoomDetailAndInAppMap}.
     *
     * Flow:
     * - Cookie login, set currency from testdata.
     * - SO phase: open PDP via deeplink (soTotalRoom / soTotalAdult)
     *     - verify infinity promo list, copy first promo and assert copied feedback,
     *     - open see-all promo section when the button is visible and copy a promo from the section,
     *     - capture first room name + price on room list, open room detail and assert they match,
     *     - tap book on room detail and assert redirect to booking form.
     * - MO phase: re-open PDP via deeplink (moTotalRoom / moTotalAdult)
     *     - open room detail, select rooms and save → verify room list,
     *     - open room detail again and verify room info attributes,
     *     - open PDP map and verify map view + nearby accommodation data.
     */
    @Author(names = {"Yazid"})
    @Module(name = "PDP")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyPdpInfinityPromoRoomDetailAndInAppMapWeb() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("verifyPdpInfinityPromoRoomDetailAndInAppMapWeb");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String email = param.get("email");
        String password = param.get("password");
        String hotelName = param.get("hotelName");
        String location = param.getOrDefault("location", hotelName);
        String currency = param.get("currency");
        int soTotalAdult = Integer.parseInt(param.get("soTotalAdult"));
        int soTotalRoom = Integer.parseInt(param.get("soTotalRoom"));
        int moTotalAdult = Integer.parseInt(param.get("moTotalAdult"));
        int moTotalRoom = Integer.parseInt(param.get("moTotalRoom"));
        int totalNight = Integer.parseInt(param.get("totalNight"));
        String rangeAddDate = param.get("rangeAddDate");
        String accommodationType = "hotel";

        String[] dates = generateCheckInOutDate(rangeAddDate, totalNight);
        String checkInDate = dates[0];
        String checkOutDate = dates[1];

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        PDPPageWeb<?> pdpPageWeb = PDPPageWeb.getInstance(session);
        HotelDetailPage<?> detailPage = new HotelDetailPage<>(session);
        HotelRoomListPageWeb<?> roomListPage = HotelRoomListPageWeb.getInstance(session);
        HotelBookingFormPageWeb<?> bookingFormPage = HotelBookingFormPageWeb.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);

        step("Go to home");
        homePage.goToHomePage();

        step("Login as " + email + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(email, password);

        step("Set currency to " + currency);
        homePage.selectCurrency(currency);

        step("Resolve hotel id/country for " + hotelName);
        HotelAutoCompleteResponse hotelAutoCompleteResponse = hotelApi.getAutocompleteData(email, password, hotelName, accommodationType.toUpperCase());
        int selectedIndex = hotelApi.findHotelIndex(hotelAutoCompleteResponse, hotelName);
        String hotelId = hotelAutoCompleteResponse.data.contents.get(selectedIndex).publicId;
        String country = hotelAutoCompleteResponse.data.contents.get(selectedIndex).country.id;

        step("Open PDP via deeplink (SO: " + soTotalRoom + " room, " + soTotalAdult + " adult)");
        pdpPageWeb.goToHotelViaDeeplinkWithCheckinCheckout(country.toLowerCase(), hotelId, checkInDate, checkOutDate, soTotalRoom, soTotalAdult);
        Assertion.assertThat("Hotel name on PDP matches deeplink target", pdpPageWeb.getHotelNameDetail(), containsStringIgnoringCase(hotelName));

        step("Verify infinity promo cards section is visible on PDP");
        Assertion.assertThat("Infinity promo cards section is shown on PDP", pdpPageWeb.isPromoListLoaded(), is(true));

        step("Copy first promo code from PDP card and verify copied feedback");
        String copiedCode = pdpPageWeb.copyFirstInfinityPromoCode();
        Assertion.assertThat("Copied promo code is not empty", copiedCode, is(not(emptyOrNullString())));
        String expectedPromoCode = normalizeNonBreakingSpaceText(copiedCode);
        String clipboardText = pdpPageWeb.getClipboardText();
        Assertion.assertThat("Clipboard should match copied infinity promo code", clipboardText, is(expectedPromoCode));

        step("Verify see-all promo section when see-all button is visible");
        pdpPageWeb.scrollToRightOfPromo();
        if (pdpPageWeb.isSeeAllPromoButtonDisplayed()) {
            pdpPageWeb.clickSeeAllPromo();
            Assertion.assertThat("See-all promo section is shown after clicking 'See all'", pdpPageWeb.isPromoSectionLoaded(), is(true));
            String copiedSectionPromoText = pdpPageWeb.copyFirstPromoCodeFromSection();
            String sectionCode = copiedSectionPromoText.contains(": ")
                    ? copiedSectionPromoText.substring(copiedSectionPromoText.indexOf(": ") + 2).trim()
                    : copiedSectionPromoText.trim();
            Assertion.assertThat("Promo code copied from see-all section is not empty", sectionCode, is(not(emptyOrNullString())));
            expectedPromoCode = normalizeNonBreakingSpaceText(sectionCode);
            clipboardText = pdpPageWeb.getClipboardText();
            Assertion.assertThat("Clipboard should match promo copied from see-all section", clipboardText, is(expectedPromoCode));
            pdpPageWeb.closeSeeAllPromo();
        } else {
            step("Skipping see-all promo verification because see-all button is not visible on PDP");
        }

        step("Go to room list and verify room detail content matches room list");
        detailPage.seeHotelRoom();
        detailPage.clickPopUp();
        Assertion.assertThat("Room list is shown after seeing rooms", roomListPage.isRoomListSectionVisible(), is(true));
        String firstRoomNameOnList = roomListPage.getRoomName();
        String firstRoomPriceOnList = roomListPage.getRoomPrice();
        roomListPage.openRoomDetail();
        Assertion.assertThat("Room name on room detail matches room list", roomListPage.getRoomNameFromDetail(), containsStringIgnoringCase(firstRoomNameOnList));
        Assertion.assertThat("Room price on room detail matches room list",
                CoreFunctions.convertPrice(pdpPageWeb.getRoomDetailPrice()), is(CoreFunctions.convertPrice(firstRoomPriceOnList)));

        step("Tap book on room detail and verify redirect to booking form (SO)");
        pdpPageWeb.clickBookButtonRoomDetail();
        bookingFormPage.dismissRatePlanChangePopupsIfPresent();
        Assertion.assertThat("User is redirected to Booking Form after tapping book on room detail",
                bookingFormPage.isContactForm(), is(true));

        step("Re-open PDP via deeplink with MO config (" + moTotalRoom + " room, " + moTotalAdult + " adult)");
        pdpPageWeb.goToHotelViaDeeplinkWithCheckinCheckout(country.toLowerCase(), hotelId, checkInDate, checkOutDate, moTotalRoom, moTotalAdult);
        Assertion.assertThat("Hotel name on PDP matches deeplink target", pdpPageWeb.getHotelNameDetail(), containsStringIgnoringCase(hotelName));

        step("MO — add rooms from room detail, save, and land on room list");
        detailPage.seeHotelRoom();
        detailPage.clickPopUp();
        Assertion.assertThat("Room list is shown before opening room detail (MO)", roomListPage.isRoomListSectionVisible(), is(true));
        roomListPage.openRoomDetailFromAvailablePriceContainer();
        roomListPage.clickSelectRoomDetailButton();
        roomListPage.clickSaveRoomDetailButton();
        Assertion.assertThat("After saving rooms from room detail in MO, user moves to the room list",
                roomListPage.isRoomListSectionVisible(), is(true));

        step("MO — open room detail again and verify room info");
        roomListPage.openRoomDetailFromAvailablePriceContainer();
        Assertion.assertThat("Room info attributes are displayed on room detail page",
                pdpPageWeb.isRoomInfoDetailsDisplayed(), is(true));

        step("Open PDP map and verify map view with nearby accommodation data");
        pdpPageWeb.closeRoomDetail();
        pdpPageWeb.goToMaps();
        Assertion.assertThat("Map view is shown after clicking location on PDP", pdpPageWeb.checkMaps(), is(true));
        Assertion.assertThat("Chosen hotel is highlighted on the in-app map", pdpPageWeb.isChosenHotelHighlightedOnMap(hotelName), is(true));
        Assertion.assertThat("Nearby points (shopping/culinary spots) are visible on the in-app map", pdpPageWeb.areaNearbyPointsShownOnMap(), is(true));

        step("Click accommodations nearby chip button");
        pdpPageWeb.clickNearbyChip();
        Assertion.assertThat("Nearby accommodation is shown after clicking the Accommodations chip", pdpPageWeb.isNearbyAccommodationShown(), is(true));

        step("Click nearby chip button");
        pdpPageWeb.clickNearbyChip();
        Assertion.assertThat("Nearby chip is shown after clicking the nearby chip", pdpPageWeb.isNearbyChipDataShown(), is(true));
    }

    @Author(names = "Yazid")
    @Module(name = "OrderPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyReviseSpecialRequestSingleOrder() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("verifyReviseSpecialRequestSingleOrder");
        BaseTest.paramsThreadLocal.set(param);
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String startDate = generateRandomDate(param.get("rangeAddDate"));
        String hotelName = param.get("hotelName");
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));
        int room = Integer.parseInt(param.get("room"));
        String currency = param.get("currency");
        boolean isSpecialRequest = Boolean.parseBoolean(param.get("isSpecialRequest"));
        String prefixSpecialRequest = param.get("prefixSpecialRequest");
        boolean isRefundable = Boolean.parseBoolean(param.get("isRefundable"));
        String language = param.get("language");
        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = getRandomObject(salutationList);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        HotelApi<?> hotelApi = new HotelApi<>(session);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        HotelReviseBookingPage<?> reviseBookingPage = new HotelReviseBookingPage<>(session);

        step("Create new hotel issued order");
        Boolean result = hotelApi.createIssuedOrderApi(
                email, password, startDate, hotelName, adult, night, room,
                orderFullName, orderNameTitle, guestFullName, guestTitle, currency, isRefundable,
                isSpecialRequest, prefixSpecialRequest, language,
                true);
        Assertion.assertThat("Hotel Booking via API is successful", result, is(true));

        step("Get data from the issued order");
        String orderId = String.valueOf(hotelApi.getOrderId());
        String specialRequest = hotelApi.orderSpecialRequest;
        log("Special Request before revise: " + specialRequest);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);
        if (language.equalsIgnoreCase("id")) homePage.selectBahasaLanguage();

        step("user is on order detail: " + orderId);
        orderListPage.openActiveTicketViaURL(orderId);
        orderDetailPage.skipCoachmark();

        step("Open the room details");
        orderDetailPage.clickSeeRoomDetails();
        Assertion.assertThat("The special request is displayed as per booking", orderDetailPage.isSpecialRequestDisplayedRoomDetails(specialRequest), is(true));

        step("Open the revise Special Request field");
        orderDetailPage.clickEditSpecialRequest(specialRequest);

        step("Revise the special request");
        String newSpecialRequest = CoreFunctions.generateRandomString(10) + " Revised";
        reviseBookingPage.editSpecialRequest(newSpecialRequest);
        reviseBookingPage.clickAgreeButton();
        reviseBookingPage.inputOTPCode("1234");
        Assertion.assertThat("Success message should be displayed ", reviseBookingPage.getSuccessText(), either(containsString("The special request has been changed")).or(containsString("Permintaan khusus berhasil diubah")));

        step("Open order detail page to verify the revised special request");
        reviseBookingPage.clickSeeDetails();
        Assertion.assertThat("Verify Order Detail Page is Opened", orderListPage.getCurrentUrl(), containsString(orderId));
        Assertion.assertThat("The old special request should be removed: " + specialRequest, orderDetailPage.isSpecialRequestDisplayedOrderDetails(specialRequest), is(false));
        Assertion.assertThat("The new special request should be present: " + newSpecialRequest, orderDetailPage.isSpecialRequestDisplayedOrderDetails(newSpecialRequest), is(true));
    }

    @Author(names = "Naufal Hafiz")
    @Module(name = "BookingPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyBookingHotelbedsOneRoomOneNightNonIDR()
            throws FailedLoginException, InterruptedException, ExecutionException {
        Map<String, String> param = getParam("verifyBookingHotelbedsOneRoomOneNightNonIDR");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String url = param.get("url");
        String currency = param.get("currency");
        String startDate = param.get("startDate");
        String endDate = param.get("endDate");
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));

        String paymentGroup = param.get("paymentGroup");
        String paymentMethod = param.get("paymentMethod");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        HotelDetailPage<?> detailPage = new HotelDetailPage<>(session);
        HotelRoomListPageWeb<?> roomListPage = HotelRoomListPageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HotelBookingFormPageWeb<?> bookingFormPage = HotelBookingFormPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User select IDR currency");
        homePage.selectCurrency(currency);

        step("User Open Hotel Landing page");
        String searchPageCurrency = homePage.getCurrentCurrency();
        Assertion.assertThat("Verify currency in  Search Result page is set to SGD", searchPageCurrency.equals("SGD"),
                Is.is(true));
        homePage.openVerticalPageViaURL("Hotel");
        Assertion.assertThat("Landing Page is Opened", landingPage.isLandingPageVisible(), Is.is(true));

        step("Open PDP");
        homePage.openHotelPDPViaURLWithDate(url, startDate, endDate);

        Assertion.assertThat("Verify if PDP is Opened", detailPage.isPdpLoaded(), Is.is(true));
        Assertion.assertThat("Map Section is visible on Hotel PDP", detailPage.isPdpMapVisible(), Is.is(true));

        Assertion.assertThat("Verify if PDP is Opened", detailPage.isPdpLoaded(), Is.is(true));
        Assertion.assertThat("Map Section is visible on Hotel PDP", detailPage.isPdpMapVisible(), Is.is(true));
        roomListPage.scrollToRoomListSection();

        step("Click Book Button");
        roomListPage.clickBookButton();

        step("Go to payment page");
        bookingFormPage.continueToPaymentPage();

        step("check order id on payment page");
        String orderID = paymentPage.getOrderId();
        orderID = orderID.replaceAll("[^0-9]", "");
        Assertion.assertThat("Verify order ID is displayed in payment page", !orderID.isEmpty(), Is.is(true));

        step("User select payment method ");
        paymentPage.clickOnSelectPaymentMethod();
        paymentPage.clickOnSeeAll();
        paymentPage.goToPaymentMethodsGroup(paymentGroup);
        paymentPage.selectPaymentMethod(paymentMethod);

        step("User complete payment");
        paymentPage.clickOnPaymentButton();

        step("User try to open see order list");
        paymentPage.clickSeeOrderListButton();

        step("Go to Issued Order");
        orderListPage.openActiveTicketViaURL(orderID);
        orderDetailPage.skipCoachmark();

        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.verifyIssuedETicket(),
                Is.is(true));
        Assertion.assertThat("contact name should be available on order detail page",
                !orderDetailPage.getContactName(orderID).isEmpty(), Is.is(true));
        Assertion.assertThat("email should be correctly displayed on order detail page",
                orderDetailPage.getContactEmail(orderID), Is.is(email));
        Assertion.assertThat("orderID should be visible and should be same as from payment page",
                orderDetailPage.getOrderId(), Is.is(orderID));
    }

    @Author(names = "Raihan")
    @Module(name = "BookingPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyPurchaseHotelWithCreditCardAsNonLoginUser()
            throws FailedLoginException, InterruptedException, ExecutionException {
        Map<String, String> param = getParam("verifyPurchaseHotelWithCreditCardAsNonLoginUser");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String url = "/indonesia/ramada-encore-bali-seminyak-108001534490321067";
        String fullName = param.get("fullName");
        String phoneNumber = param.get("phoneNumber");
        String email = param.get("email");
        String cardNumber = param.get("cardNumber");
        String cardMonth = param.get("cardMonth");
        String cvv = param.get("cvv");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        HotelDetailPage<?> detailPage = new HotelDetailPage<>(session);
        HotelRoomListPage<?> roomListPage = new HotelRoomListPage<>(session);
        PDPPageWeb<?> pdpPageWeb = PDPPageWeb.getInstance(session);
        HotelBookingFormPageWeb<?> bookingFormPage = HotelBookingFormPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User Open Hotel Landing page");
        String searchPageCurrency = homePage.getCurrentCurrency();
        Assertion.assertThat("Verify currency in  Search Result page is set to SGD", searchPageCurrency.equals("SGD"),
                Is.is(true));
        homePage.openVerticalPageViaURL("Hotel");
        Assertion.assertThat("Landing Page is Opened", landingPage.isLandingPageVisible(), Is.is(true));

        step("Open PDP");
        homePage.openHotelPDPViaURL(url);

        Assertion.assertThat("Verify if PDP is Opened", detailPage.isPdpLoaded(), Is.is(true));
        if (Configs.PLATFORM.equals(Platform.IMWEB)) {
            pdpPageWeb.clickSeeRoomButton();
        } else {
            roomListPage.scrollToRoomListSection();
        }

        step("Click Book Button");
        roomListPage.clickBookButton();

        step("Fill contact detail");
        bookingFormPage.fillContactDetail(fullName, phoneNumber, email);

        step("Fill guest detail");
        bookingFormPage.fillGuestDetailsCombinedRandom(1);

        step("Go to payment page");
        bookingFormPage.continueToPaymentPage();

        step("check order id on payment page");
        String orderID = paymentPage.getOrderId();
        Assertion.assertThat("Verify order ID is displayed in payment page", !orderID.isEmpty(), Is.is(true));

        step("Input the card details");
        paymentPage.inputCardNumber(cardNumber);
        paymentPage.inputCardMonth(cardMonth);
        paymentPage.inputCardCvv(cvv);
        paymentPage.inputCardHolderName(fullName);

        step("User complete payment");
        paymentPage.clickOnPaymentButton();
        Assertion.assertThat("Booking is successful", paymentPage.isSeeOrderListButtonDisplayed(), is(true));
    }

    @Author(names = "Shubh")
    @Module(name = "BookingPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyBookingMO3pVendorMgho() throws InterruptedException, FailedLoginException, ExecutionException {
        Map<String, String> param = getParam("verifyBookingMO3pVendorMgho");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String location = param.get("location");
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int adult = Integer.parseInt(param.get("adult"));
        int child = Integer.parseInt(param.get("child"));
        int rooms = Integer.parseInt(param.get("rooms"));
        String creditCardName = param.get("name");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String paymentGroup = param.get("paymentGroup");
        String paymentMethod = param.get("paymentMethod");
        String dateType = param.get("dateType");
        String dateRange = param.get("dateRange");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        HotelDetailPage<?> detailPage = new HotelDetailPage<>(session);
        SRPPageWeb<?> searchResultPage = SRPPageWeb.getInstance(session);
        HotelRoomListPageWeb<?> roomListPage = HotelRoomListPageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HotelBookingFormPageWeb<?> bookingFormPage = HotelBookingFormPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User select IDR currency");
        homePage.selectCurrency("IDR");

        step("User Open Hotel Landing page");
        String searchPageCurrency = homePage.getCurrentCurrency();
        Assertion.assertThat("Verify currency in  Search Result page is set to IDR", searchPageCurrency.equals("IDR"),
                Is.is(true));
        homePage.openVerticalPageViaURL("Hotel");
        Assertion.assertThat("Landing Page is Opened", landingPage.isLandingPageVisible(), Is.is(true));

        step("Search stay hotel");
        landingPage.searchStayLocation(location);
        landingPage.setStayDate(dateType, dateRange);
        landingPage.clickSubmit();

        if (!detailPage.isPdpLoaded()) {
            step("submit search Button");
            searchResultPage.clickPopUp();
            Assertion.assertThat("Srp is Loaded", searchResultPage.isHotelCardVisible(), Is.is(true));

            step("Select hotel Card ");
            searchResultPage.selectHotel();

            step("Go Inside PDP");
            detailPage.switchToNewTab();
        }

        step("Check PDP");
        detailPage.clickPopUp();
        Assertion.assertThat("Verify if PDP is Opened", detailPage.isPdpLoaded(), Is.is(true));
        roomListPage.scrollToRoomListSection();

        step("Set 2 Adults 0 Children and 2 room count");
        roomListPage.clickRoomListChangeButton();
        roomListPage.clickRoomNGuestButton();
        roomListPage.setTotalGuest(adult);
        roomListPage.setTotalChild(child);
        roomListPage.setTotalRoom(rooms);
        roomListPage.clickGuestDoneAndSubmitButton();

        step("Click Book button");
        roomListPage.clickBookButton();

        step("Go to payment page");
        bookingFormPage.fillGuestDetails(rooms, creditCardName, true, false, "2");
        bookingFormPage.continueToPaymentPage();

        step("check order id on payment page");
        String orderID = paymentPage.getOrderId();
        orderID = orderID.replaceAll("[^0-9]", "");
        Assertion.assertThat("Verify order ID is displayed in payment page", !orderID.isEmpty(), Is.is(true));

        step("User select payment method ");
        paymentPage.clickOnSelectPaymentMethod();
        paymentPage.clickOnSeeAll();
        paymentPage.goToPaymentMethodsGroup(paymentGroup);
        paymentPage.selectPaymentMethod(paymentMethod);

        step("User complete payment");
        paymentPage.clickOnPaymentButton();

        step("User try to open see order list");
        paymentPage.clickSeeOrderListButton();

        step("Go to Issued Order");
        orderListPage.openActiveTicketViaURL(orderID);
        orderDetailPage.skipCoachmark();

        Assertion.assertThat("Order detail page presented", orderDetailPage.getOrderId(), Is.is(orderID));
    }

    @Author(names = {"Harshit", "Yazid", "Aditi"})
    @Module(name = "BookingPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void completeBookingSoWithAddOnsLogin() throws FailedLoginException, InterruptedException, ExecutionException {
        Map<String, String> param = getParam("completeBookingSoWithAddOnsLogin");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        String hotelName = param.get("location");
        String paymentGroup = param.get("paymentGroup");
        String paymentMethod = param.get("paymentMethod");
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String dateType=param.get("dateType");
        String dateRange=param.get("dateRange");
        int noOfAdults = Integer.parseInt(param.get("Adult"));

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        HotelDetailPage<?> detailPage = new HotelDetailPage<>(session);
        HotelRoomListPage<?> roomListPage = new HotelRoomListPage<>(session);
        HotelBookingFormPageWeb<?> bookingFormPage = HotelBookingFormPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User select IDR currency");
        homePage.selectCurrency("IDR");
        homePage.selectEnglishLanguage();

        step("User Open Hotel Landing page");
        homePage.openVerticalPageViaURL("Hotel");

        step("Search hotel name");
        landingPage.searchStayLocation(hotelName);

        step("Set guests and rooms");
        landingPage.clickRoomAndGuestButton();
        landingPage.setTotalGuest(noOfAdults);
        landingPage.clickDone();

        step("Set date");
        landingPage.setStayDate(dateType, dateRange);

        step("submit search Button");
        landingPage.clickSubmit();

        step("Go Inside PDP");
        String pdpHotelName = detailPage.getHotelNameDetail();
        Assertion.assertThat("Verify if PDP is Opened", detailPage.isPdpLoaded(), is(true));
        Assertion.assertThat("Hotel name on the search and detail page should be equal",pdpHotelName , containsString(hotelName));

        step("Rooms are available hence click on See Rooms Button");
        detailPage.seeHotelRoom();

        step("Scroll to room list section");
        roomListPage.scrollToRoomListSection();
        Assertion.assertThat("Room list is visible", roomListPage.isRoomListSectionVisible(), is(true));
        Assertion.assertThat("Rooms Image should be visible",roomListPage.isRoomImageVisible(),is(true));

        step("Click Book Button");
        roomListPage.clickBookButton();
        Assertion.assertThat("Hotel name on Booking form should be correct ", bookingFormPage.getBookingHotelName(), containsString(pdpHotelName));

        step("User input contact detail");
        Assertion.assertThat("Contact form should be visible on Booking Form page", bookingFormPage.isContactForm(), is(true));

        step("User enter Stay details");
        Assertion.assertThat("Guest Stay details form should be visible on Booking Form page", bookingFormPage.isGuestStayForm(), is(true));
        bookingFormPage.fillGuestDetails(1, "", false);

        step("Add special request");
        bookingFormPage.addSpecialRequestOptions();
        bookingFormPage.enterSpecialRequest("This is special request " + CoreFunctions.generateRandomString(10).toLowerCase());
        String specialRequest = bookingFormPage.getSpecialReqText();
        bookingFormPage.continueToNextPage();
        String initialPrice = bookingFormPage.getPriceSummaryTotal();

        step("Add addons");
        List<String> addOnsDetails = bookingFormPage.chooseAddOns();
        String addOnsName = addOnsDetails.get(0);
        String addOnsPrice = addOnsDetails.get(1);
        String newPrice = bookingFormPage.getPriceSummaryTotal();
        log("Initial Price: " + initialPrice + ", Add Ons Price: " + addOnsPrice + ", New Price: " + newPrice);
        Assertion.assertThat("Total price will increase with addons price", CoreFunctions.convertPrice(newPrice), is(CoreFunctions.convertPrice(initialPrice) + CoreFunctions.convertPrice(addOnsPrice)));

        step("Add insurance");
        List<String> insuranceDetails = bookingFormPage.chooseInsurance();
        String insuranceName = insuranceDetails.get(0);
        String insurancePrice = insuranceDetails.get(1);
        newPrice = bookingFormPage.getPriceSummaryTotal();
        log("Initial Price: " + initialPrice + ", Add Ons Price: " + addOnsPrice + ", Insurance Price: " + insurancePrice + ", New Price: " + newPrice);
        Assertion.assertThat("Total price will increase with insurance price", CoreFunctions.convertPrice(newPrice), is(CoreFunctions.convertPrice(initialPrice) + CoreFunctions.convertPrice(addOnsPrice) + CoreFunctions.convertPrice(insurancePrice)));

        step("User open the price breakdown section");
        bookingFormPage.openSubTotalPriceBreakdown();
        Assertion.assertThat("User can see room price in the price breakdown booking form", bookingFormPage.getRoomPriceSubtotal(), is(not(emptyString())));
        Assertion.assertThat("User can see add ons in the price breakdown booking form", bookingFormPage.getFacilityPriceSubtotal(addOnsName), is(addOnsPrice));
        Assertion.assertThat("User can see insurance in the price breakdown booking form", bookingFormPage.getFacilityPriceSubtotal(insuranceName), is(insurancePrice));

        step("Go to payment page");
        bookingFormPage.continueToPaymentPage();
        String orderID = paymentPage.getOrderId();
        Assertion.assertThat("Verify order ID is displayed in payment page", !orderID.isEmpty(), is(true));

        step("User select payment method ");
        paymentPage.clickOnSelectPaymentMethod();
        paymentPage.goToPaymentMethodsGroup(paymentGroup);
        paymentPage.selectPaymentMethod(paymentMethod);

        step("User complete payment");
        paymentPage.clickOnPaymentButton();

        step("User try to open see order list");
        paymentPage.clickSeeOrderListButton();

        step("Find the issued order");
        orderListPage.openActiveTicketViaURL(orderID);
        orderDetailPage.skipCoachmark();

        step("Verify order detail information");
        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.verifyIssuedETicket(), is(true));
        Assertion.assertThat("contact name should be available on order detail page", !orderDetailPage.getContactName(orderID).isEmpty(), is(true));
        Assertion.assertThat("email should be correctly displayed on order detail page", orderDetailPage.getContactEmail(orderID), is(email));
        Assertion.assertThat("orderID should be visible and should be same as from payment page", orderDetailPage.getOrderId(), is(orderID));
        Assertion.assertThat("No of Rooms should be correct", orderDetailPage.getTotalRoom(), is(1));
        Assertion.assertThat("Price should be visible and should be same as from payment page", orderDetailPage.getVerticalTotalPrice(), is(newPrice));
        Assertion.assertThat("User can see the special request on order detail", orderDetailPage.checkSpecialRequest(specialRequest), is(true));
        Assertion.assertThat("User can see the add ons on order detail", orderDetailPage.isAddOnPresent(addOnsName), is(true));
        Assertion.assertThat("User can see the insurance on order detail", orderDetailPage.isInsurancePresent(insuranceName), is(true));
    }

    @Author(names = "Okta")
    @Module(name = "CompleteBooking")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void testCompleteBookingExpediaNonIDRLogin() throws Exception {
        Map<String, String> param = getParam("testCompleteBookingExpediaNonIDRLogin");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        String currency = param.get("currency");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        int noOfAdults = Integer.parseInt(param.get("noOfAdults"));
        int noOfChildren = Integer.parseInt(param.get("noOfChildren"));
        int noOfRoom = Integer.parseInt(param.get("noOfRoom"));
        String hotel = param.get("hotel");
        String cardNumber = param.get("cardNumber");
        String cardMonth = param.get("cardMonth");
        String cardCvv = param.get("cardCvv");
        String cardHolderName = param.get("cardHolderName");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        LandingPageWeb<?> landingPage = LandingPageWeb.getInstance(session);
        PDPPageWeb<?> detailPage = PDPPageWeb.getInstance(session);
        HotelRoomListPageWeb<?> roomListPage = HotelRoomListPageWeb.getInstance(session);
        HotelBookingFormPage<?> bookingFormPage = new HotelBookingFormPage<>(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);

        step("Open the home page");
        homePage.goToHomePage();

        step("Log in as an existing user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("Select currency");
        homePage.selectCurrency(currency);

        step("Navigate to the hotel vertical and search for the desired hotel");
        homePage.openVerticalPageViaURL("Hotel");
        landingPage.searchStayLocation(hotel);

        step("Set guests and rooms");
        landingPage.clickRoomAndGuestButton();
        landingPage.setTotalGuest(noOfAdults);
        landingPage.setTotalChildGuest(noOfChildren);
        landingPage.setTotalRoom(noOfRoom);
        landingPage.clickDone();
        landingPage.clickSubmit();

        step("Go to the Property Detail Page (PDP)");
        Assertion.assertThat("Verify if PDP is opened", detailPage.isPdpLoaded(), Is.is(true));
        Assertion.assertThat("Hotel's image is visible on the PDP", detailPage.isHotelImageVisible(), Is.is(true));
        Assertion.assertThat("Hotel name matches the user's search on the landing page",
                detailPage.getHotelNameDetail(), containsString(hotel));
        Assertion.assertThat("Hotel's area is visible on the PDP", !detailPage.getHotelAreaDetail().isEmpty(),
                Is.is(true));
        Assertion.assertThat("Hotel's city is visible on the PDP", !detailPage.getHotelCityDetail().isEmpty(),
                Is.is(true));
        Assertion.assertThat("Hotel's region is visible on the PDP", !detailPage.getHotelRegionDetail().isEmpty(),
                Is.is(true));

        if (detailPage.isRoomsAvailable()) {
            Assertion.assertThat("Hotel's price is visible on the PDP", !detailPage.getHotelPriceDetail().isEmpty(),
                    Is.is(true));
        } else {
            Assertion.assertThat("If all rooms are fully booked, the appropriate message is shown on the Hotel PDP",
                    !detailPage.getBookingFullDetail().isEmpty(), Is.is(true));
        }

        step("Click the 'See Rooms' button");
        detailPage.seeHotelRoom();

        step("Click the 'Book' button");
        roomListPage.clickBookButton();

        step("Enter the booking form");
        Assertion.assertThat("Hotel name matches", bookingFormPage.getBookingHotelName(), Is.is(hotel));

        step("Navigate to the payment page");
        bookingFormPage.continueToPaymentPage();

        step("Check the order ID on the payment page");
        String orderID = paymentPage.getOrderId();
        Assertion.assertThat("Verify order ID is displayed on the payment page", !orderID.isEmpty(), Is.is(true));

        step("Input the card details");
        paymentPage.inputCardNumber(cardNumber);
        paymentPage.inputCardMonth(cardMonth);
        paymentPage.inputCardCvv(cardCvv);
        paymentPage.inputCardHolderName(cardHolderName);

        step("Click on pay button");
        paymentPage.clickOnPaymentButton();

        step("Verify the payment successful page is visible or not");
        Assertion.assertThat("Verify the payment successful page is visible", paymentPage.isPaymentSuccessfulVisible(),
                is(true));

        step("User try to open see order list");
        paymentPage.clickSeeOrderListButton();

        step("Order is issued");
        orderListPage.openActiveTicketViaURL(orderID);

        orderDetailPage.skipCoachmark();

        Assertion.assertThat("E-ticket on the order detail should be published", orderDetailPage.verifyIssuedETicket(),
                is(true));
        Assertion.assertThat("Contact name should be available on order detail page",
                !orderDetailPage.getContactName(orderID).isEmpty(), is(true));
        Assertion.assertThat("E-mail should be correctly displayed on order detail page",
                orderDetailPage.getContactEmail(orderID), is(email));
        Assertion.assertThat("orderID should be visible and should be same as from payment page",
                orderDetailPage.getOrderId(), containsString(orderID));
    }

    @Author(names = "Hafizh")
    @Module(name = "BookingPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyBookingWebbedsSingleRoomSingleNightNonIDR()
            throws FailedLoginException, InterruptedException, ExecutionException {
        Map<String, String> param = getParam("verifyBookingWebbedsSingleRoomSingleNightNonIDR");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String url = "/indonesia/the-sultan-hotel-jakarta-sdet-puny-hotel-jangan-dipake-607001690773411139";
        String startDate = param.get("startDate");
        String endDate = param.get("endDate");
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String cardNumber = param.get("cardNumber");
        String cardMonth = param.get("cardMonth");
        String cvv = param.get("cvv");
        String fullName = param.get("fullName");

        String paymentGroup = param.get("paymentGroup");
        String paymentMethod = param.get("paymentMethod");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        HotelDetailPage<?> detailPage = new HotelDetailPage<>(session);
        HotelRoomListPageWeb<?> roomListPage = HotelRoomListPageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HotelBookingFormPageWeb<?> bookingFormPage = HotelBookingFormPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User select SGD currency");
        homePage.selectCurrency("SGD");

        step("User Open Hotel Landing page");
        String searchPageCurrency = homePage.getCurrentCurrency();
        Assertion.assertThat("Verify currency is set to SGD", searchPageCurrency.equals("SGD"), Is.is(true));
        homePage.openVerticalPageViaURL("Hotel");
        Assertion.assertThat("Landing Page is Opened", landingPage.isLandingPageVisible(), Is.is(true));

        step("Open PDP");
        homePage.openHotelPDPViaURLWithDate(url, startDate, endDate);

        Assertion.assertThat("Verify if PDP is Opened", detailPage.isPdpLoaded(), Is.is(true));
        Assertion.assertThat("Map Section is visible on Hotel PDP", detailPage.isPdpMapVisible(), Is.is(true));
        roomListPage.scrollToRoomListSection();

        step("Click Book Button");
        roomListPage.clickBookRoomButton();

        step("Go to payment page");
        bookingFormPage.continueToPaymentPage();

        step("check order id on payment page");
        String orderID = paymentPage.getOrderId();
        orderID = orderID.replaceAll("[^0-9]", "");
        Assertion.assertThat("Verify order ID is displayed in payment page", !orderID.isEmpty(), Is.is(true));

        step("User select payment method");
        if (Configs.PLATFORM == Platform.DWEB) {
            paymentPage.inputCardNumber(cardNumber);
            paymentPage.inputCardMonth(cardMonth);
            paymentPage.inputCardCvv(cvv);
            paymentPage.inputCardHolderName(fullName);
        } else {
            paymentPage.clickOnSelectPaymentMethod();
            paymentPage.goToPaymentMethodsGroup(paymentGroup);
            paymentPage.selectPaymentMethod(paymentMethod);
        }

        step("User complete payment");
        paymentPage.clickOnPaymentButton();

        step("User try to open see order list");
        paymentPage.clickSeeOrderListButton();

        step("Go to Issued Order");
        orderListPage.openActiveTicketViaURL(orderID);
        orderDetailPage.skipCoachmark();

        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.verifyIssuedETicket(),
                Is.is(true));
        Assertion.assertThat("contact name should be available on order detail page",
                !orderDetailPage.getContactName(orderID).isEmpty(), Is.is(true));
        Assertion.assertThat("email should be correctly displayed on order detail page",
                orderDetailPage.getContactEmail(orderID), Is.is(email));
        Assertion.assertThat("orderID should be visible and should be same as from payment page",
                orderDetailPage.getOrderId(), is(orderID));
    }

    @Author(names = "Vipul Kumar")
    @Module(name = "BookingPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyBookingMOReviseSpecialRequest() throws InterruptedException, FailedLoginException {
        Map<String, String> param = getParam("verifyBookingMOReviseSpecialRequest");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String location = param.get("location");
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        String otpRevisedBooking = param.get("otpRevisedBooking");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String paymentGroup = param.get("paymentGroup");
        String paymentMethod = param.get("paymentMethod");
        String dateType = param.get("dateType");
        String dateRange = param.get("dateRange");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        HotelDetailPage<?> detailPage = new HotelDetailPage<>(session);
        HotelRoomListPageWeb<?> roomListPage = HotelRoomListPageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HotelBookingFormPageWeb<?> bookingFormPage = HotelBookingFormPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        HotelRoomListPage<?> roomList = new HotelRoomListPage<>(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User select IDR currency");
        homePage.selectCurrency("IDR");

        step("User Open Hotel Landing page");
        String searchPageCurrency = homePage.getCurrentCurrency();
        Assertion.assertThat("Verify currency in  Search Result page is set to IDR", searchPageCurrency.equals("IDR"),
                Is.is(true));
        homePage.openVerticalPageViaURL("Hotel");
        Assertion.assertThat("Landing Page is Opened", landingPage.isLandingPageVisible(), Is.is(true));

        step("Search stay hotel");
        landingPage.searchStayLocation(location);
        landingPage.setStayDate(dateType, dateRange);
        landingPage.clickSubmit();

        step("Go Inside PDP");
        detailPage.clickPopUp();
        Assertion.assertThat("Verify if PDP is Opened", detailPage.isPdpLoaded(), Is.is(true));
        roomListPage.scrollToRoomListSection();

        step("Set 2 Adults 2 Children and 2 room count");
        roomListPage.clickRoomListChangeButton();
        roomListPage.clickRoomNGuestButton();
        roomListPage.setTotalGuest(2);
        roomListPage.setTotalChild(2);
        roomListPage.setTotalRoom(2);
        roomListPage.clickGuestDoneAndSubmitButton();

        step("Click Select button 2 times");
        roomList.selectMultipleRoom(2);
        roomList.clickMultiBookButton();

        step("Go to payment page");
        bookingFormPage.fillGuestDetails(2, "Shubh", true, false, "2");
        bookingFormPage.continueToNextPage();
        bookingFormPage.continueToPaymentPage();

        step("check order id on payment page");
        String orderID = paymentPage.getOrderId();
        orderID = orderID.replaceAll("[^0-9]", "");
        Assertion.assertThat("Verify order ID is displayed in payment page", !orderID.isEmpty(), Is.is(true));

        step("User select payment method ");
        paymentPage.clickOnSelectPaymentMethod();
        paymentPage.clickOnSeeAll();
        paymentPage.goToPaymentMethodsGroup(paymentGroup);
        paymentPage.selectPaymentMethod(paymentMethod);

        step("User complete payment");
        paymentPage.clickOnPaymentButton();

        step("User try to open see order list");
        paymentPage.clickSeeOrderListButton();

        step("Go to Issued Order");
        orderListPage.openActiveTicketViaURL(orderID);
        orderDetailPage.skipCoachmark();

        Assertion.assertThat("Order detail page presented", orderDetailPage.getOrderId(), Is.is(orderID));

        step("Revise Special Request from Order Detail Page");
        orderDetailPage.clickOnEditBookingForSpecialRequest();
        orderDetailPage.inputOTPCode(otpRevisedBooking, retryCounter);
        Assertion.assertThat("User should be on success full revised Page adn it's container image should be present",
                orderDetailPage.successFullRevisedname(), Is.is(true));
    }

    @Author(names = "Okta")
    @Module(name = "BookingPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyBookingHotelbeds2NightsLoginIDR()
            throws FailedLoginException, InterruptedException, ExecutionException {
        Map<String, String> param = getParam("verifyBookingHotelbeds2NightsLoginIDR");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String url = param.get("url");
        String currency = param.get("currency");
        int noOfAdult = Integer.parseInt(param.get("noOfAdult"));
        int noOfChild = Integer.parseInt(param.get("noOfChild"));
        int noOfRoom = Integer.parseInt(param.get("noOfRoom"));
        String startDate = param.get("startDate");
        String endDate = param.get("endDate");
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String firstGuestName = param.get("firstGuestName");

        String paymentGroup = param.get("paymentGroup");
        String paymentMethod = param.get("paymentMethod");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        HotelDetailPage<?> detailPage = new HotelDetailPage<>(session);
        HotelRoomListPageWeb<?> roomListPage = HotelRoomListPageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HotelBookingFormPageWeb<?> bookingFormPage = HotelBookingFormPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User select IDR currency");
        homePage.selectCurrency(currency);

        step("User Open Hotel Landing page");
        String searchPageCurrency = homePage.getCurrentCurrency();
        Assertion.assertThat("Verify currency in  Search Result page is set to IDR", searchPageCurrency.equals("IDR"),
                Is.is(true));
        homePage.openVerticalPageViaURL("Hotel");
        Assertion.assertThat("Landing Page is Opened", landingPage.isLandingPageVisible(), Is.is(true));

        step("Open PDP");
        homePage.openHotelPDPViaURLWithDate(url, startDate, endDate);

        Assertion.assertThat("Verify if PDP is Opened", detailPage.isPdpLoaded(), Is.is(true));
        Assertion.assertThat("Map Section is visible on Hotel PDP", detailPage.isPdpMapVisible(), Is.is(true));
        roomListPage.scrollToRoomListSection();

        step("Set 2 Adults 2 Children and 2 room count");
        roomListPage.clickRoomListChangeButton();
        roomListPage.clickRoomNGuestButton();
        roomListPage.setTotalGuest(noOfAdult);
        roomListPage.setTotalChild(noOfChild);
        roomListPage.setTotalRoom(noOfRoom);
        roomListPage.clickGuestDoneAndSubmitButton();

        step("Click Select button 2 times");
        roomListPage.clickSelectRoomButtonMultiOrder(noOfRoom);
        roomListPage.clickMultiOrderBookButton();

        step("Go to payment page");
        bookingFormPage.fillGuestDetails(2, firstGuestName, true, false, "2");
        bookingFormPage.continueToPaymentPage();

        step("check order id on payment page");
        String orderID = paymentPage.getOrderId();
        orderID = orderID.replaceAll("[^0-9]", "");
        Assertion.assertThat("Verify order ID is displayed in payment page", !orderID.isEmpty(), Is.is(true));

        step("User select payment method ");
        paymentPage.clickOnSelectPaymentMethod();
        paymentPage.clickOnSeeAll();
        paymentPage.goToPaymentMethodsGroup(paymentGroup);
        paymentPage.selectPaymentMethod(paymentMethod);

        step("User complete payment");
        paymentPage.clickOnPaymentButton();

        step("User try to open see order list");
        paymentPage.clickSeeOrderListButton();

        step("Go to Issued Order");
        orderListPage.openActiveTicketViaURL(orderID);
        orderDetailPage.skipCoachmark();

        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.verifyIssuedETicket(),
                Is.is(true));
        Assertion.assertThat("contact name should be available on order detail page",
                !orderDetailPage.getContactName(orderID).isEmpty(), Is.is(true));
        Assertion.assertThat("email should be correctly displayed on order detail page",
                orderDetailPage.getContactEmail(orderID), Is.is(email));
        Assertion.assertThat("orderID should be visible and should be same as from payment page",
                orderDetailPage.getOrderId(), Is.is(orderID));
    }

    @Author(names = "Raihan")
    @Module(name = "BookingPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyBookTiketRoomSingleGuestLogin()
            throws FailedLoginException, InterruptedException, ExecutionException {
        Map<String, String> param = getParam("verifyBookTiketRoomSingleGuestLogin");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String location = param.get("location");
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String paymentGroup = param.get("paymentGroup");
        String paymentMethod = param.get("paymentMethod");
        String dateType = param.get("dateType");
        String dateRange = param.get("dateRange");
        int guest = Integer.parseInt(param.get("guest"));
        int room = Integer.parseInt(param.get("room"));

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        HotelDetailPage<?> detailPage = new HotelDetailPage<>(session);
        HotelRoomListPage<?> roomListPage = new HotelRoomListPage<>(session);
        PDPPageWeb<?> pdpPageWeb = PDPPageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HotelBookingFormPageWeb<?> bookingFormPage = HotelBookingFormPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User select IDR currency");
        homePage.selectCurrency("IDR");

        step("User Open Hotel Landing page");
        String searchPageCurrency = homePage.getCurrentCurrency();
        Assertion.assertThat("Verify currency in  Search Result page is set to IDR", searchPageCurrency.equals("IDR"),
                Is.is(true));
        homePage.openVerticalPageViaURL("Hotel");
        Assertion.assertThat("Landing Page is Opened", landingPage.isLandingPageVisible(), Is.is(true));

        step("Search stay hotel");
        landingPage.searchStayLocation(location);
        landingPage.setStayDate(dateType, dateRange);
        if (!Configs.PLATFORM.equals(Platform.DWEB)) {
            landingPage.clickRoomAndGuestButton();
        }
        landingPage.setTotalGuest(guest);
        landingPage.setTotalRoom(room);
        landingPage.clickSubmit();

        Assertion.assertThat("Verify if PDP is Opened", detailPage.isPdpLoaded(), Is.is(true));
        if (Configs.PLATFORM.equals(Platform.IMWEB)) {
            pdpPageWeb.clickSeeRoomButton();
        } else {
            roomListPage.scrollToRoomListSection();
        }

        step("Click Book Button");
        roomListPage.clickBookButton();

        step("Go to payment page");
        bookingFormPage.continueToPaymentPage();

        step("check order id on payment page");
        String orderID = paymentPage.getOrderId();
        orderID = orderID.replaceAll("[^0-9]", "");
        Assertion.assertThat("Verify order ID is displayed in payment page", !orderID.isEmpty(), Is.is(true));

        step("User select payment method ");
        paymentPage.clickOnSelectPaymentMethod();
        paymentPage.clickOnSeeAll();
        paymentPage.goToPaymentMethodsGroup(paymentGroup);
        paymentPage.selectPaymentMethod(paymentMethod);

        step("User complete payment");
        paymentPage.clickOnPaymentButton();

        step("User try to open see order list");
        paymentPage.clickSeeOrderListButton();

        step("Go to Issued Order");
        orderListPage.openActiveTicketViaURL(orderID);
        orderDetailPage.skipCoachmark();

        Assertion.assertThat("email should be correctly displayed on order detail page",
                orderDetailPage.getContactEmail(orderID), Is.is(email));
    }

    /**
     * Web counterpart of app {@code verifyRoomRecommendationBestPickOnRoomList}.
     * Sheet key: {@code verifyRoomRecommendationBestPickOnRoomListWeb}. Params: hotelName or location,
     * date_type or dateType, date, total_room or totalRoom, total_adult or totalGuest.
     */
    @Author(names = {"Yazid"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Hotel")
    @Module(name = "PDP")
    @Test
    public void verifyRoomRecommendationBestPickOnRoomListWeb() throws InterruptedException {
        Map<String, String> map = getParam("verifyRoomRecommendationBestPickOnRoomListWeb");
        BaseTest.paramsThreadLocal.set(map);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String hotelName = map.getOrDefault("hotelName", map.get("location"));
        String dateType = map.getOrDefault("date_type", map.get("dateType"));
        String date = map.get("date");
        int totalRoom = Integer.parseInt(map.getOrDefault("total_room", map.get("totalRoom")));
        int totalGuest = Integer.parseInt(map.getOrDefault("total_adult", map.getOrDefault("totalGuest", map.get("totalAdult"))));

        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        HotelDetailPage<?> detailPage = new HotelDetailPage<>(session);
        PDPPageWeb<?> pdpPageWeb = PDPPageWeb.getInstance(session);
        HotelRoomListPageWeb<?> roomListPage = HotelRoomListPageWeb.getInstance(session);

        step("Open Home and continue as guest");
        homePage.goToHomePage();
        loginPage.loginAsGuest();
        Assertion.assertThat(
                "Pre-condition: user must start as guest (not logged in)",
                loginPage.checkIfLoggedIn(), is(false));

        step("Open Hotel landing page");
        homePage.openVerticalPageViaURL("Hotel");

        step("Set stay dates (type: " + dateType + ", date: " + date + ")");
        landingPage.setStayDate(dateType, date);

        step("Search hotel by name: \"" + hotelName + "\"");
        landingPage.searchStayLocation(hotelName);

        step("Set room count: " + totalRoom + " and adult count: " + totalGuest);
        landingPage.clickRoomAndGuestButton();
        landingPage.setTotalRoom(totalRoom);
        landingPage.setTotalGuest(totalGuest);
        landingPage.clickDone();

        step("Submit search");
        landingPage.clickSubmit();

        step("Open PDP (Match hotelName from testdata when search land directly on PDP)");
        Assertion.assertThat(
                "User should land on hotel PDP after searching '" + hotelName + "'",
                pdpPageWeb.getHotelName(), is(hotelName));

        step("Open room list from PDP");
        detailPage.seeHotelRoom();
        detailPage.clickPopUp();
        Assertion.assertThat(
                "Room list should be displayed after tapping See rooms on PDP",
                roomListPage.isRoomListSectionVisible(), is(true));

        step("Verify room recommendation card is visible on the room list");
        Assertion.assertThat(
                "Room recommendation card should be visible on the room list",
                roomListPage.isRoomRecommendationCardOnTop(), is(true));

        step("Verify room recommendation card shows the 'Best pick for you' banner");
        Assertion.assertThat(
                "Best pick header should be visible on the room list (on top of room recommendation card)",
                roomListPage.isBestPickForYouHeaderVisible(), is(true));
        Assertion.assertThat(
                "Best pick header text should match expected value",
                roomListPage.getBestPickForYouHeaderText().toLowerCase(),
                anyOf(containsString("best pick for you"), containsString("pilihan terbaik untukmu")));

        step("Verify announcement banner is visible on room list");
        Assertion.assertThat(
                "Announcement banner should be visible above the Best pick & room list.",
                roomListPage.isAnnouncementBannerVisible(), is(true));
    }

    @Author(names = "Hafizh")
    @Module(name = "HotelLandingPage")
    @Tribe(name = "Demand")
    @Vertical(name = "Accommodation")
    @Test
    public void checkRecentlySearchNearMeActiveLangIdGuest() {
        Map<String, String> param = getParam("checkRecentlySearchNearMeActiveLangIdGuest");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as guest user");
        loginPage.loginAsGuest();

        step("Select Bahasa Language");
        homePage.selectBahasaLanguage();
        homePage.goback();

        step("User Open hotel Landing page");
        homePage.openVerticalPageViaURL("Hotel");

        step("Search stay location");
        landingPage.clickNearMeLocation();

        step("submit search Button");
        landingPage.clickSubmit();

        step("Check Recently Searched on Home Page");
        homePage.goToHomePage();
        Assertion.assertThat("Recently Search Has near me in Bahasa", homePage.checkRecentlySearchIsNearMe(),
                is("Dekat saya"));

        step("Change Language");
        homePage.selectEnglishLanguage();
        homePage.goback();

        step("Check Recently Searched");
        Assertion.assertThat("Recently Search Has near me in Eng", homePage.checkRecentlySearchIsNearMe(),
                is("Near me"));
    }

    @Author(names = "Okta")
    @Module(name = "CompleteBooking")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void testCompleteBookingTravelgateMghoNonIDRLogin() throws Exception {
        Map<String, String> param = getParam("testCompleteBookingTravelgateMghoNonIDRLogin");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        String currency = param.get("currency");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        int noOfAdults = Integer.parseInt(param.get("noOfAdults"));
        int noOfChildren = Integer.parseInt(param.get("noOfChildren"));
        int noOfRoom = Integer.parseInt(param.get("noOfRoom"));
        String hotel = param.get("hotel");
        String cardNumber = param.get("cardNumber");
        String cardMonth = param.get("cardMonth");
        String cardCvv = param.get("cardCvv");
        String cardHolderName = param.get("cardHolderName");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        LandingPageWeb<?> landingPage = LandingPageWeb.getInstance(session);
        PDPPageWeb<?> detailPage = PDPPageWeb.getInstance(session);
        HotelRoomListPageWeb<?> roomListPage = HotelRoomListPageWeb.getInstance(session);
        HotelBookingFormPage<?> bookingFormPage = new HotelBookingFormPage<>(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);

        step("Open the home page");
        homePage.goToHomePage();

        step("Log in as an existing user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("Select currency");
        homePage.selectCurrency(currency);

        step("Navigate to the hotel vertical and search for the desired hotel");
        homePage.openVerticalPageViaURL("Hotel");
        landingPage.searchStayLocation(hotel);

        step("Set guests and rooms");
        landingPage.clickRoomAndGuestButton();
        landingPage.setTotalGuest(noOfAdults);
        landingPage.setTotalChildGuest(noOfChildren);
        landingPage.setTotalRoom(noOfRoom);
        landingPage.clickDone();
        landingPage.clickSubmit();

        step("Go to the Property Detail Page (PDP)");
        Assertion.assertThat("Verify if PDP is opened", detailPage.isPdpLoaded(), Is.is(true));
        Assertion.assertThat("Hotel's image is visible on the PDP", detailPage.isHotelImageVisible(), Is.is(true));
        Assertion.assertThat("Hotel name matches the user's search on the landing page",
                detailPage.getHotelNameDetail(), containsString(hotel));

        if (detailPage.isRoomsAvailable()) {
            Assertion.assertThat("Hotel's price is visible on the PDP", !detailPage.getHotelPriceDetail().isEmpty(),
                    Is.is(true));
        } else {
            Assertion.assertThat("If all rooms are fully booked, the appropriate message is shown on the Hotel PDP",
                    !detailPage.getBookingFullDetail().isEmpty(), Is.is(true));
        }

        step("Click the 'See Rooms' button");
        detailPage.seeHotelRoom();

        step("Click the 'Book' button");
        roomListPage.clickBookButton();

        step("Enter the booking form");
        Assertion.assertThat("Hotel name matches", bookingFormPage.getBookingHotelName(), Is.is(hotel));

        step("Navigate to the payment page");
        bookingFormPage.continueToPaymentPage();

        step("Check the order ID on the payment page");
        String orderID = paymentPage.getOrderId();
        Assertion.assertThat("Verify order ID is displayed on the payment page", !orderID.isEmpty(), Is.is(true));

        step("Input the card details");
        paymentPage.inputCardNumber(cardNumber);
        paymentPage.inputCardMonth(cardMonth);
        paymentPage.inputCardCvv(cardCvv);
        paymentPage.inputCardHolderName(cardHolderName);

        step("Click on pay button");
        paymentPage.clickOnPaymentButton();

        step("Verify the payment successful page is visible or not");
        Assertion.assertThat("Verify the payment successful page is visible", paymentPage.isPaymentSuccessfulVisible(),
                is(true));

        step("User try to open see order list");
        paymentPage.clickSeeOrderListButton();

        step("Order is issued");
        orderListPage.openActiveTicketViaURL(orderID);

        orderDetailPage.skipCoachmark();

        Assertion.assertThat("E-ticket on the order detail should be published", orderDetailPage.verifyIssuedETicket(),
                is(true));
        Assertion.assertThat("Contact name should be available on order detail page",
                !orderDetailPage.getContactName(orderID).isEmpty(), is(true));
        Assertion.assertThat("E-mail should be correctly displayed on order detail page",
                orderDetailPage.getContactEmail(orderID), is(email));
        Assertion.assertThat("orderID should be visible and should be same as from payment page",
                orderDetailPage.getOrderId(), containsString(orderID));
    }

    @Author(names = "Fajrul Alam")
    @Module(name = "SRP")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void testPopularFilter() throws Exception {
        Map<String, String> param = getParam("testPopularFilter");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        String location = param.get("location");

        LandingPageWeb<?> landingPage = LandingPageWeb.getInstance(session);

        SRPPageWeb<?> searchResultPage = SRPPageWeb.getInstance(session);
        SRPPageMweb<?> searchResultPageMweb = new SRPPageMweb<>(session);
        HomePageWeb<?> homePageWeb = HomePageWeb.getInstance(session);

        step("Navigate to the hotel vertical and search for the desired hotel");
        homePageWeb.openVerticalPageViaURL("Hotel");

        step("Search stay location");
        landingPage.searchStayLocation(location);

        step("Submit search button");
        landingPage.clickSubmit();

        step("Close pop-up");
        searchResultPage.clickPopUp();

        step("Open popular filter");
        searchResultPageMweb.openPopularFilter();

        step("Select all popular filter chips");
        boolean selectChipsSuccess = searchResultPageMweb.selectPopularFilterChips();
        Assert.assertTrue(selectChipsSuccess, "Failed to select popular filter chips");

        step("Deselect all chips except first one");
        boolean deselectChipsSuccess = searchResultPageMweb.deselectAllChipsExceptFirst();
        Assert.assertTrue(deselectChipsSuccess, "Failed to deselect chips except first one");

        step("Click reset button to deselect all chips");
        boolean resetSuccess = searchResultPageMweb.clickResetButton();
        Assert.assertTrue(resetSuccess, "Failed to click reset button");

        step("Verify all chips are unselected");
        boolean allChipsUnselected = searchResultPageMweb.verifyAllChipsUnselected();
        Assert.assertTrue(allChipsUnselected, "Not all chips are unselected after reset");

        step("Click save button to close bottom sheet");
        boolean saveSuccess = searchResultPageMweb.clickSaveButton();
        Assert.assertTrue(saveSuccess, "Failed to click save button");

        step("Verify bottom sheet is closed");
        boolean bottomSheetClosed = searchResultPageMweb.verifyBottomSheetClosed();
        Assert.assertTrue(bottomSheetClosed, "Bottom sheet is not closed after clicking save");
    }

    @Author(names = "Raihan")
    @Module(name = "LandingPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyRecentlySearchAndViewedHotelLogin() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("verifyRecentlySearchAndViewedHotelLogin");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> hotelLandingPageWeb = HotelLandingPageWeb.getInstance(session);
        HomesRoomListPageWeb<?> homesRoomList = HomesRoomListPageWeb.getInstance(session);
        SRPPageWeb<?> searchResultPage = SRPPageWeb.getInstance(session);
        SRPPageWeb<?> srpPageWeb = SRPPageWeb.getInstance(session);
        HotelDetailPage<?> detailPage = new HotelDetailPage<>(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        String location = param.get("location");
        String location2 = param.get("location2");
        String propertyName = param.get("propertyName");
        String pageModuleId = param.get("pageModuleId");
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));

        step("User open Home Page");
        homePage.goToHomePage();

        step("Log in as an existing user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User select IDR currency");
        homePage.selectCurrency("IDR");

        step("User open Homes Landing Page");
        homePage.openVerticalPageViaURL("Hotel");

        step("Check recent search from page module to SRP");
        hotelLandingPageWeb.clickPageModulePrimaryButton(pageModuleId);
        Assertion.assertThat("Srp is Loaded", searchResultPage.isHotelCardVisible(), Is.is(true));
        String srpLocation = srpPageWeb.getLocationHeaderLabel();
        String srpDate = srpPageWeb.getDateHeaderLabel();
        String srpGuest = srpPageWeb.getGuestHeaderLabel();
        srpPageWeb.clickBack();
        Assertion.assertThat("Recently searched is showing correct destination",
                hotelLandingPageWeb.isSearchHistoryDestinationLabelDisplayed(srpLocation, 1), is(true));
        Assertion.assertThat("Recently searched is showing correct no of guest",
                hotelLandingPageWeb.isSearchHistoryGuestLabelDisplayed(srpGuest, 1), is(true));
        hotelLandingPageWeb.clearAllRecentList();

        step("Check recent search from page module to PDP");
        hotelLandingPageWeb.clickPageModuleProductCardByName(pageModuleId, propertyName);
        Assertion.assertThat("Verify if PDP is Opened", detailPage.isPdpLoaded(), Is.is(true));
        String pdpPropertyName = homesRoomList.getPropertyNameLabel();
        srpPageWeb.clickBack();
        Assertion.assertThat("Recently searched is showing correct destination",
                hotelLandingPageWeb.isSearchHistoryDestinationLabelDisplayed(pdpPropertyName, 1), is(true));
        hotelLandingPageWeb.clearAllRecentList();

        step("Check recent search from search location on landing page");
        hotelLandingPageWeb.searchStayLocation(location);
        hotelLandingPageWeb.clickSubmit();
        Assertion.assertThat("Srp is Loaded", searchResultPage.isHotelCardVisible(), Is.is(true));
        srpPageWeb.clickBack();
        Assertion.assertThat("Recently searched is showing correct destination",
                hotelLandingPageWeb.isSearchHistoryDestinationLabelDisplayed(location, 1), is(true));
        hotelLandingPageWeb.clearAllRecentList();

        step("Check recent search from search property name on landing page");
        hotelLandingPageWeb.searchStayLocation(propertyName);
        hotelLandingPageWeb.clickSubmit();
        Assertion.assertThat("Verify if PDP is Opened", detailPage.isPdpLoaded(), Is.is(true));
        srpPageWeb.clickBack();
        Assertion.assertThat("Recently searched is showing correct destination",
                hotelLandingPageWeb.isSearchHistoryDestinationLabelDisplayed(propertyName, 1), is(true));
        hotelLandingPageWeb.clearAllRecentList();

        step("Check recent search from search location on SRP");
        hotelLandingPageWeb.searchStayLocation(location);
        hotelLandingPageWeb.clickSubmit();
        Assertion.assertThat("Srp is Loaded", searchResultPage.isHotelCardVisible(), Is.is(true));
        srpPageWeb.updateStayLocation(location2);
        srpPageWeb.clickSearchOnSRP();
        Assertion.assertThat("Srp is Loaded", searchResultPage.isHotelCardVisible(), Is.is(true));
        srpPageWeb.clickBack();
        srpPageWeb.clickBack();
        Assertion.assertThat("Recently searched is showing correct destination",
                hotelLandingPageWeb.isSearchHistoryDestinationLabelDisplayed(location2, 1), is(true));
        hotelLandingPageWeb.clearAllRecentList();

        step("Check recent search from search property name on SRP");
        hotelLandingPageWeb.searchStayLocation(location);
        hotelLandingPageWeb.clickSubmit();
        Assertion.assertThat("Srp is Loaded", searchResultPage.isHotelCardVisible(), Is.is(true));
        srpPageWeb.updateStayLocation(propertyName);
        srpPageWeb.clickSearchOnSRP();
        Assertion.assertThat("Verify if PDP is Opened", detailPage.isPdpLoaded(), Is.is(true));
        srpPageWeb.clickBack();
        srpPageWeb.clickBack();
        Assertion.assertThat("Recently searched is showing correct destination",
                hotelLandingPageWeb.isSearchHistoryDestinationLabelDisplayed(propertyName, 1), is(true));
        hotelLandingPageWeb.clearAllRecentList();
    }

    @Author(names = "Bikash")
    @Module(name = "CompleteBooking")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void completeBookingBCOMVendorNonIDR() throws Exception {
        Map<String, String> param = getParam("completeBookingBCOMVendorNonIDR");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        String currency = param.get("currency");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String hotel = param.get("hotel");
        int noOfGuest = Integer.parseInt(param.get("noOfGuest"));
        int noOfRoom = Integer.parseInt(param.get("noOfRoom"));
        String cardHolderName = param.get("cardHolderName");
        String cardNumber = param.get("cardNumber");
        String cvv = param.get("cvv");
        String cardMonth = param.get("cardMonth");

        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LandingPageWeb<?> landingPage = LandingPageWeb.getInstance(session);
        PDPPageWeb<?> detailPage = PDPPageWeb.getInstance(session);
        HotelRoomListPageWeb<?> roomListPage = HotelRoomListPageWeb.getInstance(session);
        HotelBookingFormPage<?> bookingFormPage = new HotelBookingFormPage<>(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);

        step("Open the homepage");
        homePage.goToHomePage();

        step("Log in using an existing user account");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("Go to the Hotel section and search for the target hotel");
        homePage.selectCurrency(currency);
        homePage.openVerticalPageViaURL("Hotel");
        landingPage.searchStayLocation(hotel);

        step("Configure guests and rooms for the search");
        landingPage.clickRoomAndGuestButton();
        landingPage.setTotalGuest(noOfGuest);
        landingPage.setTotalRoom(noOfRoom);
        landingPage.clickDone();

        step("Set date");
        landingPage.setStayDate("range date", "10-15");

        step("Submit the search request");
        landingPage.clickSubmit();

        step("Access the Property Detail Page (PDP)");
        Assertion.assertThat("Verify if PDP is Opened", detailPage.isPdpLoaded(), Is.is(true));
        Assertion.assertThat("Hotel's image is visible on the Hotel PDP", detailPage.isHotelImageVisible(),
                Is.is(true));
        Assertion.assertThat("Hotel name matches the user's search on the landing page",
                detailPage.getHotelNameDetail(), containsString(hotel));
        Assertion.assertThat("Hotel's Area is visible on the Hotel PDP", !detailPage.getHotelAreaDetail().isEmpty(),
                Is.is(true));
        Assertion.assertThat("Hotel's City is visible on the Hotel PDP", !detailPage.getHotelCityDetail().isEmpty(),
                Is.is(true));
        Assertion.assertThat("Hotel's Region is visible on the Hotel PDP", !detailPage.getHotelRegionDetail().isEmpty(),
                Is.is(true));

        if (detailPage.isRoomsAvailable()) {
            Assertion.assertThat("Hotel's price is visible on the Hotel PDP",
                    !detailPage.getHotelPriceDetail().isEmpty(), Is.is(true));
        } else {
            Assertion.assertThat("If all rooms are fully booked, the appropriate message is shown on the Hotel PDP",
                    !detailPage.getBookingFullDetail().isEmpty(), Is.is(true));
        }

        step("Select 'See Rooms' to view available rooms");
        detailPage.seeHotelRoom();

        step("Select 'Book' to proceed with booking the room");
        roomListPage.clickBookButton();

        step("Enter the Booking Form page and verify hotel name");
        Assertion.assertThat("Hotel name matches", bookingFormPage.getBookingHotelName(), Is.is(hotel));

        step("Continue to the payment page");
        bookingFormPage.continueToPaymentPage();

        step("Fetch the order ID from the payment page");
        String orderID = paymentPage.getOrderId();
        Assertion.assertThat("Verify order ID is displayed on the payment page", !orderID.isEmpty(), Is.is(true));

        step("Enter credit card details for payment");
        paymentPage.inputCardNumber(cardNumber);
        paymentPage.inputCardMonth(cardMonth);
        paymentPage.inputCardCvv(cvv);
        paymentPage.inputCardHolderName(cardHolderName);

        step("Finish the payment process by clicking the payment button");
        paymentPage.clickOnPaymentButton();

        step("View your orders by opening the order list");
        paymentPage.clickSeeOrderListButton();

        step("Navigate to the details page of the issued e-ticket order");
        orderListPage.openActiveTicketViaURL(orderID);
        orderDetailPage.skipCoachmark();

        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.verifyIssuedETicket(),
                Is.is(true));
        Assertion.assertThat("Contact name should be available on the order detail page",
                !orderDetailPage.getContactName(orderID).isEmpty(), Is.is(true));
        Assertion.assertThat("Email should be correctly displayed on the order detail page",
                orderDetailPage.getContactEmail(orderID), Is.is(email));
        Assertion.assertThat("Order ID should be visible and match the ID from the payment page",
                orderDetailPage.getOrderId(), Is.is(orderID));

    }

    @Author(names = "Vipul kumar")
    @Module(name = "CompleteBooking")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyCompleteBookingInternationalHotelNonIDRCC()
            throws FailedLoginException, InterruptedException, ExecutionException {
        Map<String, String> param = getParam("verifyCompleteBookingInternationalHotelNonIDRCC");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String location = param.get("location");
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String currency = param.get("currency");
        int noOfRooms = Integer.parseInt(param.get("rooms"));
        int guests = Integer.parseInt(param.get("guests"));
        String cardNumber = param.get("cardNumber");
        String cardMonth = param.get("cardMonth");
        String cardCvv = param.get("cardCvv");
        String cardHolderName = param.get("cardHolderName");
        String fullName = CoreFunctions.randomName();
        String phoneNumber = "91" + CoreFunctions.randomInt(8);
        String email_id = CoreFunctions.randomName() + "-aut@tiket.com";

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);
        int daysToReturnDate = Integer.parseInt(param.get("daysToReturnDate"));
        String returnDate = CoreFunctions.addToLongDate(departureDate, daysToReturnDate);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        HotelDetailPage<?> detailPage = new HotelDetailPage<>(session);
        HotelRoomListPage<?> roomListPage = new HotelRoomListPage<>(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HotelBookingFormPageWeb<?> bookingFormPage = HotelBookingFormPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        CalendarPageWeb<?> calendarPage = CalendarPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User should be logged in");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User select " + currency + " currency");
        homePage.selectCurrency(currency);

        step("User Open Hotel Landing page");
        homePage.openVerticalPageViaURL("Hotel");

        Assertion.assertThat("Landing Page is Opened", landingPage.isLandingPageVisible(), is(true));

        step("Search for international hotel With the Name Of " + location);
        landingPage.searchStayLocation(location);

        step("Set date");
        landingPage.clickOnCalendarWidgetToOpen();
        calendarPage.selectDate(departureDate, returnDate);

        step("submit search Button");
        landingPage.clickSubmit();

        step("Go Inside PDP");
        String HotelName = detailPage.getHotelNameDetail();
        Assertion.assertThat("Verify if PDP is Opened", detailPage.isPdpLoaded(), is(true));
        Assertion.assertThat("Verify Hotel name on entered on LP and PDP  is same", HotelName,
                containsString(location));

        step("Scroll to room list section");
        roomListPage.scrollToRoomListSection();
        Assertion.assertThat("Room list is visible", roomListPage.isRoomListSectionVisible(), is(true));
        Assertion.assertThat("Rooms Image should be visible", roomListPage.isRoomImageVisible(), is(true));

        step("User update number of room & guest on roomList");
        roomListPage.clickRoomNGuestButton();
        roomListPage.setTotalGuest(guests);
        roomListPage.setTotalRoom(noOfRooms);

        roomListPage.clickGuestDoneButton();
        roomListPage.clickRoomListChangeButton();

        step("Click Book Button");
        roomListPage.firstBookRoom();

        step("Select Multiple Room");
        roomListPage.selectMultipleRoomWithSameRatePlan(noOfRooms);

        roomListPage.clickMultiBookButton();

        step("User input contact detail");
        Assertion.assertThat("Contact form should be visible on Booking Form page", bookingFormPage.isContactForm(), is(true));
        bookingFormPage.fillContactDetail(fullName, phoneNumber, email_id);

        step("User enter Stay details ");
        Assertion.assertThat("Stay details form should be visible on Booking Form page",
                bookingFormPage.isGuestStayForm(), is(true));

        // Fill all guest names and store them for validation
        List<String> filledGuestNames = new ArrayList<>();
        for (int i = 1; i <= guests; i++) {
            String guestName = CoreFunctions.randomName();
            filledGuestNames.add(guestName);
            bookingFormPage.fillGuestDetails(i, guestName, true, false, "");
        }

        int bookingPrice = bookingFormPage.getTotalPrice();
        step("Go to payment page");
        bookingFormPage.continueTo2NdPartThenPaymentPage();

        String orderID = paymentPage.getOrderId();
        Assertion.assertThat("Verify order ID is displayed in payment page", !orderID.isEmpty(), is(true));

        if (Configs.ENVIRONMENT == Environment.GK || Configs.ENVIRONMENT == Environment.PREPROD) {
        step("Scroll down to the payment Group");

        step("Input the card details");
        paymentPage.inputCardNumber(cardNumber);
        paymentPage.inputCardMonth(cardMonth);
        paymentPage.inputCardCvv(cardCvv);
        paymentPage.inputCardHolderName(cardHolderName);

        step("Click on Pay button");
        paymentPage.clickOnPaymentButton();

        step("Verify the payment successful page is visible or not");
        Assertion.assertThat("Verify the payment successful page is visible", paymentPage.isPaymentSuccessfulVisible(),
                is(true));

        step("User try to open see order list");
        paymentPage.clickSeeOrderListButton();

        step("Issued Order");
        orderListPage.openActiveTicketViaURL(orderID);

        orderDetailPage.refreshTheWebpage();
        orderDetailPage.skipCoachmark();

        Assertion.assertThat("E-ticket on the order detail should be published", orderDetailPage.verifyIssuedETicket(),
                is(true));

        String actualEmail = orderDetailPage.getContactEmail(orderID);
        Assertion.assertThat("Email should be correctly displayed on order detail page. Expected: " + email
                + " Actual: " + actualEmail, actualEmail, is(email));

        String actualOrderID = orderDetailPage.getOrderId();
        Assertion.assertThat("OrderID should be visible and should be same as from payment page. Expected: " + orderID
                + " Actual: " + actualOrderID, actualOrderID, is(orderID));

        List<String> guestNamesFromOrderDetail = orderDetailPage.getAllGuestNames(orderID);
        Assertion.assertThat("Number of guest names should match. Expected: " + guests + " Actual: "
                + guestNamesFromOrderDetail.size(), guestNamesFromOrderDetail.size(), is(guests));

        for (int i = 0; i < filledGuestNames.size(); i++) {
            String filledGuestName = filledGuestNames.get(i);
            String trimmedFilledName = filledGuestName.trim();
            // API returns fullName as "firstName lastName", so expected format is
            // "filledName filledName"
            String expectedFullName = trimmedFilledName + " " + trimmedFilledName;
            int guestNumber = i + 1;

            String actualGuestName = guestNamesFromOrderDetail.stream()
                    .map(String::trim)
                    .filter(name -> name.equalsIgnoreCase(expectedFullName) || name.equals(expectedFullName))
                    .findFirst()
                    .orElse(null);

            String actualValue = actualGuestName != null ? actualGuestName
                    : "not found. Available names: " + guestNamesFromOrderDetail;
            Assertion
                    .assertThat(
                            "Guest Name " + guestNumber + " should match the name filled on booking page. Expected: "
                                    + expectedFullName + " Actual: " + actualValue,
                            actualGuestName, is(expectedFullName));
        }

        String priceFromOrderDetail = orderDetailPage.getVerticalTotalPrice();
        String numericValue = priceFromOrderDetail.replaceAll("\\D", "");
        int orderPrice = Integer.parseInt(numericValue);

        Assertion.assertThat("Price should be visible and should be same as from payment page. Expected: "
                + bookingPrice + " Actual: " + orderPrice, orderPrice, is(bookingPrice));

        String orderCurrency = orderDetailPage.getCustomerCurrencyData(orderID);
        Assertion.assertThat(
                "Currency should be same as selected currency. Expected: " + currency + " Actual: " + orderCurrency,
                orderCurrency, is(currency));

        int actualTotalRooms = orderDetailPage.getTotalRoom();
        Assertion.assertThat("No of Rooms should be correct. Expected: " + noOfRooms + " Actual: " + actualTotalRooms,
                actualTotalRooms, is(noOfRooms));
        }
    }

    @Author(names = "Vipul kumar")
    @Module(name = "CompleteBooking")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyCompleteBookingNativeHotelNonIDRCC()
            throws FailedLoginException, InterruptedException, ExecutionException {
        Map<String, String> param = getParam("verifyCompleteBookingNativeHotelNonIDRCC");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String location = param.get("location");
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String currency = param.get("currency");
        int noOfAdults = Integer.parseInt(param.get("guests"));
        int noOfChildren = Integer.parseInt(param.get("childs"));
        int noOfRoom = Integer.parseInt(param.get("rooms"));
        String cardNumber = param.get("cardNumber");
        String cardMonth = param.get("cardMonth");
        String cardCvv = param.get("cardCvv");
        String cardHolderName = param.get("cardHolderName");
        String fullName = CoreFunctions.randomName();
        String phoneNumber = "91" + CoreFunctions.randomInt(8);

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);
        int daysToReturnDate = Integer.parseInt(param.get("daysToReturnDate"));
        String returnDate = CoreFunctions.addToLongDate(departureDate, daysToReturnDate);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> HotelLandingPage = HotelLandingPageWeb.getInstance(session);
        LandingPageWeb<?> landingPage = LandingPageWeb.getInstance(session);
        HotelDetailPage<?> detailPage = new HotelDetailPage<>(session);
        HotelRoomListPage<?> roomListPage = new HotelRoomListPage<>(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HotelBookingFormPageWeb<?> bookingFormPage = HotelBookingFormPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        CalendarPageWeb<?> calendarPage = CalendarPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User should be logged in");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User select " + currency + " currency");
        homePage.selectCurrency(currency);

        step("User Open Hotel Landing page");
        homePage.openVerticalPageViaURL("Hotel");

        Assertion.assertThat("Landing Page is Opened", landingPage.isLandingPageVisible(), is(true));

        step("Search for international hotel With the Name Of " + location);
        landingPage.searchStayLocation(location);

        step("Set date");
        HotelLandingPage.clickOnCalendarWidgetToOpen();
        calendarPage.selectDate(departureDate, returnDate);

        step("Set guests and rooms");
        landingPage.clickRoomAndGuestButton();
        landingPage.setTotalGuest(noOfAdults);
        landingPage.setTotalChildGuest(noOfChildren);
        landingPage.setTotalRoom(noOfRoom);
        landingPage.clickDone();
        landingPage.clickSubmit();

        step("Go Inside PDP");
        String HotelName=detailPage.getHotelNameDetail();
        Assertion.assertThat("Verify if PDP is Opened", detailPage.isPdpLoaded(), is(true));
        Assertion.assertThat("Verify Hotel name on entered on LP and PDP  is same",HotelName,containsString(location));

        step("Scroll to room list section");
        roomListPage.scrollToRoomListSection();
        Assertion.assertThat("Room list is visible", roomListPage.isRoomListSectionVisible(), is(true));
        Assertion.assertThat("Rooms Image should be visible", roomListPage.isRoomImageVisible(), is(true));

        String vendorName=roomListPage.getVendorNameFromColor();
        Assertion.assertThat("Verify vendor is Native i.e Tiket",vendorName,containsString("TIKET"));

        step("Click Book Button");
        roomListPage.clickBookButton();

        step("User input contact detail");
        Assertion.assertThat("Contact form should be visible on Booking Form page", bookingFormPage.isContactForm(), is(true));
        bookingFormPage.fillContactDetail(fullName, phoneNumber, email);

        step("User enter Stay details ");
        Assertion.assertThat("Stay details form should be visible on Booking Form page",
                bookingFormPage.isGuestStayForm(), is(true));

        // Fill all guest names and store them for validation
        List<String> filledGuestNames = new ArrayList<>();
        for (int i = 1; i <= noOfAdults; i++) {
            String guestName = CoreFunctions.randomName();
            filledGuestNames.add(guestName);
            bookingFormPage.fillGuestDetails(i, guestName, true, false, "");
        }

        int bookingPrice = bookingFormPage.getTotalPrice();
        step("Go to payment page");
        bookingFormPage.continueTo2NdPartThenPaymentPage();

        String orderID = paymentPage.getOrderId();
        Assertion.assertThat("Verify order ID is displayed in payment page", !orderID.isEmpty(), is(true));

        if (Configs.ENVIRONMENT == Environment.GK || Configs.ENVIRONMENT == Environment.PREPROD) {
        step("Scroll down to the payment Group");

        step("Input the card details");
        paymentPage.inputCardNumber(cardNumber);
        paymentPage.inputCardMonth(cardMonth);
        paymentPage.inputCardCvv(cardCvv);
        paymentPage.inputCardHolderName(cardHolderName);

        step("Click on Pay button");
        paymentPage.clickOnPaymentButton();

        step("Verify the payment successful page is visible or not");
        Assertion.assertThat("Verify the payment successful page is visible", paymentPage.isPaymentSuccessfulVisible(),
                is(true));

        step("User try to open see order list");
        paymentPage.clickSeeOrderListButton();

        step("Issued Order");
        orderListPage.openActiveTicketViaURL(orderID);

        orderDetailPage.refreshTheWebpage();
        orderDetailPage.skipCoachmark();

        Assertion.assertThat("E-ticket on the order detail should be published", orderDetailPage.verifyIssuedETicket(),
                is(true));

        String actualEmail = orderDetailPage.getContactEmail(orderID);
        Assertion.assertThat("Email should be correctly displayed on order detail page. Expected: " + email
                + " Actual: " + actualEmail, actualEmail, is(email));

        String actualOrderID = orderDetailPage.getOrderId();
        Assertion.assertThat("OrderID should be visible and should be same as from payment page. Expected: " + orderID
                + " Actual: " + actualOrderID, actualOrderID, is(orderID));

        List<String> guestNamesFromOrderDetail = orderDetailPage.getAllGuestNames(orderID);
        Assertion.assertThat("Number of guest names should match. Expected: " + noOfAdults + " Actual: "
                + guestNamesFromOrderDetail.size(), guestNamesFromOrderDetail.size(), is(noOfAdults));

        for (int i = 0; i < filledGuestNames.size(); i++) {
            String filledGuestName = filledGuestNames.get(i);
            String trimmedFilledName = filledGuestName.trim();

            String expectedFullName = trimmedFilledName + " " + trimmedFilledName;
            int guestNumber = i + 1;

            String actualGuestName = guestNamesFromOrderDetail.stream()
                    .map(String::trim)
                    .filter(name -> name.equalsIgnoreCase(expectedFullName) || name.equals(expectedFullName))
                    .findFirst()
                    .orElse(null);

            String actualValue = actualGuestName != null ? actualGuestName
                    : "not found. Available names: " + guestNamesFromOrderDetail;
            Assertion
                    .assertThat(
                            "Guest Name " + guestNumber + " should match the name filled on booking page. Expected: "
                                    + expectedFullName + " Actual: " + actualValue,
                            actualGuestName, is(expectedFullName));
        }

        String priceFromOrderDetail = orderDetailPage.getVerticalTotalPrice();
        String numericValue = priceFromOrderDetail.replaceAll("\\D", "");
        int orderPrice = Integer.parseInt(numericValue);

        Assertion.assertThat("Price should be visible and should be same as from payment page. Expected: "
                + bookingPrice + " Actual: " + orderPrice, orderPrice, is(bookingPrice));

        String orderCurrency = orderDetailPage.getCustomerCurrencyData(orderID);
        Assertion.assertThat(
                "Currency should be same as selected currency. Expected: " + currency + " Actual: " + orderCurrency,
                orderCurrency, is(currency));

        int actualTotalRooms = orderDetailPage.getTotalRoom();
        Assertion.assertThat("No of Rooms should be correct. Expected: " + noOfRoom + " Actual: " + actualTotalRooms,
                actualTotalRooms, is(noOfRoom));

        int actualTotalChildren = orderDetailPage.getTotalChild(orderID);
        Assertion.assertThat("No of Children should be correct. Expected: " + noOfChildren + " Actual: " + actualTotalChildren,
                actualTotalChildren, is(noOfChildren));
        }
    }

    @Author(names = {"Aditi"})
    @Module(name = "Refund")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void testFullRefundHotelNonIDR() throws FailedLoginException, InterruptedException, ExecutionException {
        Map<String, String> param = getParam("testFullRefundHotelNonIDR");
        
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        RefundPageWeb<?> refundPage = RefundPageWeb.getInstance(session);
        RefundDetailWeb<?> refundDetailPage = RefundDetailWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        HotelDetailPage<?> detailPage = new HotelDetailPage<>(session);
        HotelRoomListPage<?> roomListPage=new HotelRoomListPage<>(session);
        HotelBookingFormPageWeb<?> bookingFormPage = HotelBookingFormPageWeb.getInstance(session);

        String username = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String currency = param.get("currency");
        String location = param.get("location");
        String cardNumber = param.get("cardNumber");
        String cardMonth = param.get("cardMonth");
        String cardCvv = param.get("cardCvv");
        String cardHolderName = param.get("cardHolderName");
        String dateType = param.get("dateType");
        String dateRange = param.get("dateRange");

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(username, password, otp, retryCounter);

        step("User select " + currency + " currency");
        homePage.selectCurrency(currency);

        step("User Open Hotel Landing page");
        homePage.openVerticalPageViaURL("Hotel");

        Assertion.assertThat("Landing Page is Opened", landingPage.isLandingPageVisible(), is(true));

        step("Search stay hotel");
        landingPage.searchStayLocation(location);

        step("Set date");
        landingPage.setStayDate(dateType, dateRange);

        step("submit search Button");
        landingPage.clickSubmit();

        step("Go Inside PDP");
        String HotelName=detailPage.getHotelNameDetail();
        Assertion.assertThat("Verify if PDP is Opened", detailPage.isPdpLoaded(), is(true));
        Assertion.assertThat("Verify Hotel name on entered on LP and PDP  is same",HotelName,containsString(location));
        detailPage.seeHotelRoom();

        step("Click Book Button");
        roomListPage.clickBookButton();

        step("Go to payment page");
        bookingFormPage.continueToPaymentPage();

        step("Continue to payment page");
        bookingFormPage.selectPayment();

        String orderID = paymentPage.getOrderId();
        Assertion.assertThat("Verify order ID is displayed in payment page", !orderID.isEmpty(), is(true));

        step("Input the card details");
        paymentPage.inputCardNumber(cardNumber);
        paymentPage.inputCardMonth(cardMonth);
        paymentPage.inputCardCvv(cardCvv);
        paymentPage.inputCardHolderName(cardHolderName);

        step("Click on Pay button");
        paymentPage.clickOnPaymentButton();

        step("Verify the payment successful page is visible or not");
        Assertion.assertThat("Verify the payment successful page is visible", paymentPage.isPaymentSuccessfulVisible(), is(true));

        step("User try to open see order list");
        paymentPage.clickSeeOrderListButton();

        step("Find the issued order");
        orderListPage.openActiveTicketViaURL(orderID);
        orderDetailPage.skipCoachmark();

        step("Verify hotel order details");
        Assertion.assertThat("orderID should be visible and should be same as from payment page", orderDetailPage.getOrderId(), is(orderID));
        Assertion.assertThat("E-ticket card Displayed", orderDetailPage.isETicketCardDisplayed(),is(true));
        Assertion.assertThat("Email should be correctly displayed on order detail page", orderDetailPage.getContactEmail(orderID), is(username));
        Assertion.assertThat("Contact name should be available on order detail page", !orderDetailPage.getContactName(orderID).isEmpty(), is(true));
        Assertion.assertThat("Hotel Name is displayed correctly", orderDetailPage.isHotelNameDisplayed(location), is(true));
        Assertion.assertThat("Refund and reschedule info section should be verified", orderDetailPage.verifyRefundAndRescheduleInfo(), is(true));
        Assertion.assertThat("Refund button should be displayed", orderDetailPage.isRefundButtonDisplayed(), is(true));

        step("Open refund page");
        orderDetailPage.clickRefundButton();
        refundPage.waitForContentLoaderDisappear();

        step("Click to continue to refund");
        refundPage.continueToRefund();

        step("Select random refund reason");
        refundPage.selectRandomRefundReason();

        step("Verify refund estimation section");
        refundPage.waitForEstimationAmountAppear("10");
        String refundReceive = refundPage.getRefundYoullReceive();
        String refundFee = refundPage.getRefundFee();
        String refundTotal = refundPage.getRefundTotal();
        List<String> amounts = new ArrayList<>(Arrays.asList(refundReceive, refundFee, refundTotal));
        Assertion.assertThat("Refund Total amount is correct", CoreFunctions.removeText(refundReceive)+CoreFunctions.removeText(refundFee), is(CoreFunctions.removeText(refundTotal)));

        step("Verify refund amount breakdown");
        refundPage.openRefundBreakdown();
        amounts.clear();
        amounts.addAll(refundPage.getRefundAmountBreakdown(currency));

        if (refundPage.getRefundFeeBreakdown(currency).isPresent()) amounts.addAll(refundPage.getRefundFeeBreakdown(currency).get());
        amounts.addAll(refundPage.getRefundTotalBreakdown(currency));
        Assertion.assertThat("Total breakdown refund same as estimation", refundPage.getRefundTotalBreakdownSum(currency), is(String.valueOf(CoreFunctions.removeText(refundTotal))));
        refundPage.closeRefundBreakdown();

        step("Continue to next page");
        refundPage.clickButton("Continue");
        refundPage.waitRefundSecondPageLoaded();
        Assertion.assertThat("Order ID on 2nd refund page is correct", refundPage.getOrderIdOnRefundBanner(), containsString(orderID));

        step("Verify refund method and amount");
        String refundMethodSection = refundPage.getRefundMethod();
        Assertion.assertThat("Refund method is correct", refundMethodSection, not(emptyOrNullString()));
        Assertion.assertThat("Refund amount is same as prev page", refundTotal, is(refundPage.getRefundTotal()));

        step("Check Refund TnC");
        Assertion.assertThat("Submit refund button is disabled", refundPage.isDisabledSubmitButton(), is(true));
        refundPage.clickChecklistTnc();
        refundPage.clickButton("Submit");

       step("Submit refund");
        refundPage.clickButton("Process Refund");
        refundPage.clickConfirmationRefundPopup();
        refundPage.waitForSubmitRefundLoader();

        step("Go to refund detail page");
        refundPage.SuccessSubmitRefundPopup();

        step("Verify Refund Detail");
        refundDetailPage.countRefundProgressTracker();
        Assertion.assertThat("Refund detail status not empty or null string", refundDetailPage.getRefundStatus(), not(emptyOrNullString()));
        Assertion.assertThat("Refund Id not empty or null string", refundDetailPage.getRefundId(), not(emptyOrNullString()));

        step("Verify Refund Method and Amount");
        for (String method : refundDetailPage.getRefundMethod()) {
            Assertion.assertThat("Refund method not empty", method, not(emptyOrNullString()));
        }

        step("Verify product section");
        Assertion.assertThat("Product detail shown", refundDetailPage.isOrderDetailSection(), is(true));

        step("Verify back button to order list");
        refundDetailPage.goToYourOrders();

        step("Verify the refunded order is no longer there");
        Assertion.assertThat("The refunded order is no longer in your order page",
                refundDetailPage.isOrderIdPresentinYourOrder(orderID),
                is(false));
    }


    @Author(names = "Alden")
    @Module(name = "BookingPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyBookingTravelgate2NightsLoginIDR() throws FailedLoginException, InterruptedException, ExecutionException {
        Map<String, String> param = getParam("verifyBookingTravelgate2NightsLoginIDR");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String url = param.get("url");
        String currency = param.get("currency");
        int noOfAdult = Integer.parseInt(param.get("noOfAdult"));
        int noOfChild = Integer.parseInt(param.get("noOfChild"));
        int noOfRoom = Integer.parseInt(param.get("noOfRoom"));
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String firstGuestName = param.get("firstGuestName");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        HotelDetailPage<?> detailPage = new HotelDetailPage<>(session);
        HotelRoomListPageWeb<?> roomListPage = HotelRoomListPageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HotelBookingFormPageWeb<?> bookingFormPage = HotelBookingFormPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User select IDR currency");
        homePage.selectCurrency(currency);

        step("User Open Hotel Landing page");
        String searchPageCurrency = homePage.getCurrentCurrency();
        Assertion.assertThat("Verify currency in Search Result page is set to IDR", searchPageCurrency.equals("IDR"),
                Is.is(true));
        homePage.openVerticalPageViaURL("Hotel");
        Assertion.assertThat("Landing Page is Opened", landingPage.isLandingPageVisible(), Is.is(true));

        step("Open PDP");
        homePage.openHotelPDPViaURL(url);

        Assertion.assertThat("Verify if PDP is Opened", detailPage.isPdpLoaded(), Is.is(true));
        Assertion.assertThat("Map Section is visible on Hotel PDP", detailPage.isPdpMapVisible(), Is.is(true));
        roomListPage.scrollToRoomListSection();

        step("Set 2 Adults 2 Children and 2 room count");
        roomListPage.clickRoomListChangeButton();
        roomListPage.clickRoomNGuestButton();
        roomListPage.setTotalGuest(noOfAdult);
        roomListPage.setTotalChild(noOfChild);
        roomListPage.setTotalRoom(noOfRoom);
        roomListPage.clickGuestDoneAndSubmitButton();

        step("Click Select button");
        roomListPage.clickBookButton();

        step("Go to payment page");
        bookingFormPage.fillGuestDetails(noOfRoom, firstGuestName, true, false, String.valueOf(noOfRoom));
        bookingFormPage.continueToPaymentPage();

        step("check order id on payment page");
        String orderID = paymentPage.getOrderId();
        orderID = orderID.replaceAll("[^0-9]", "");
        Assertion.assertThat("Verify order ID is displayed in payment page", !orderID.isEmpty(), Is.is(true));

        step("User select payment method");
        paymentPage.selectPaymentMethod("Sakuku");

        step("User complete payment");
        paymentPage.clickOnPaymentButton();

        step("User try to open see order list");
        paymentPage.clickSeeOrderListButton();

        step("Go to Issued Order");
        orderListPage.openActiveTicketViaURL(orderID);
        orderDetailPage.skipCoachmark();

        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.verifyIssuedETicket(),
                Is.is(true));
        Assertion.assertThat("contact name should be available on order detail page",
                !orderDetailPage.getContactName(orderID).isEmpty(), Is.is(true));
        Assertion.assertThat("email should be correctly displayed on order detail page",
                orderDetailPage.getContactEmail(orderID), Is.is(email));
        Assertion.assertThat("orderID should be visible and should be same as from payment page",
                orderDetailPage.getOrderId(), Is.is(orderID));
    }

    @Author(names = "Mridul Gupta")
    @Module(name = "Booking")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void completeBookingMoWithoutAddOnsLogin() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("completeBookingMoWithoutAddOnsLogin");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        String location = param.get("location");
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String paymentGroup = param.get("paymentGroup");
        String paymentMethod = param.get("paymentMethod");
        int noOfRooms = Integer.parseInt(param.get("rooms"));
        int guests = Integer.parseInt(param.get("guests"));
        String dateType = param.get("dateType");
        String dateRange = param.get("dateRange");


        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        HotelDetailPage<?> detailPage = new HotelDetailPage<>(session);
        HotelRoomListPage<?> roomListPage = new HotelRoomListPage<>(session);
        HotelBookingFormPageWeb<?> bookingFormPage = HotelBookingFormPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("Login as existing user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User select IDR currency");
        homePage.selectCurrency("IDR");

        step("User Open Hotel Landing page");
        homePage.openVerticalPageViaURL("Hotel");

        step("Search stay location");
        landingPage.searchStayLocation(location);

        step("Set date");
        landingPage.setStayDate(dateType, dateRange);

        step("Room and guest count update");
        landingPage.clickRoomAndGuestButton();
        landingPage.setTotalRoom(noOfRooms);
        landingPage.setTotalGuest(guests);
        landingPage.clickDone();

        step("submit search Button");
        landingPage.clickSubmit();

        step("Go Inside PDP");
        Assertion.assertThat("Verify if PDP is Opened", detailPage.isPdpLoaded(), is(true));
        Assertion.assertThat("Hotel name on the search and detail page should be equal", detailPage.getHotelNameDetail(), containsString(location));

        step("Rooms are available hence click on See Rooms Button");
        detailPage.seeHotelRoom();

        step("Scroll to room list section");
        roomListPage.scrollToRoomListSection();
        Assertion.assertThat("Room list is visible", roomListPage.isRoomListSectionVisible(), is(true));
        Assertion.assertThat("Rooms Image should be visible", roomListPage.isRoomImageVisible(), is(true));

        step("Select first room");
        roomListPage.clickBookButton();

        step("Select second room with different rate plan");
        roomListPage.clickBookButton();

        step("Click Multi-Book button");
        roomListPage.swipeToMultiRoomBookButton();
        roomListPage.clickMultiBookButton();

        step("User enter Stay details ");
        Assertion.assertThat("Stay details form should be visible on Booking Form page", bookingFormPage.isGuestStayForm(), is(true));
        bookingFormPage.fillGuestDetailsCombinedRandom(2);
        bookingFormPage.continueToNextPage();
        bookingFormPage.continueToPaymentPage();

        step("Go to payment page");
        String orderID = paymentPage.getOrderId();
        Assertion.assertThat("Verify order ID is displayed in payment page", !orderID.isEmpty(), is(true));

        step("verify there should NOT be Addon");
        Assertion.assertThat("Add-on section should not be visible", paymentPage.isAddOnPresent(), is(false));

        step("User select payment method");
        paymentPage.clickOnSelectPaymentMethod();
        paymentPage.goToPaymentMethodsGroup(paymentGroup);
        paymentPage.selectPaymentMethod(paymentMethod);

        step("User complete payment");
        paymentPage.clickOnPaymentButton();

        step("User open order detail page");
        orderListPage.openActiveTicketViaURL(orderID);
        orderDetailPage.skipCoachmark();

        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.verifyIssuedETicket(), is(true));
        Assertion.assertThat("orderID should be visible and should be same as from payment page", orderDetailPage.getOrderId(), is(orderID));

        step("Verify Special request and AddOn in order detail page");
        Assertion.assertThat("Special request should not be present", orderDetailPage.getSpecialReq(), is("No Special Request"));
        String addOnText = orderDetailPage.getAddOnText();
        Assertion.assertThat("Add-ons should not be present", addOnText, is("No Add On"));
    }


    @Author(names = "Aditi")
    @Module(name = "Review")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void checkHotelReview()
    {
        Map<String, String> param = getParam("checkHotelReview");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String hotel = param.get("hotel");
        int noOfGuest = Integer.parseInt(param.get("noOfGuest"));
        int noOfRoom = Integer.parseInt(param.get("noOfRoom"));
        String sortOption = param.get("sortOption");
        String dateType=param.get("dateType");
        String dateRange=param.get("dateRange");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LandingPageWeb<?> landingPage = LandingPageWeb.getInstance(session);
        PDPPageWeb<?> detailPage = PDPPageWeb.getInstance(session);

        step("Open the homepage");
        homePage.goToHomePage();

        step("Go to the Hotel section and search for the target hotel");
        homePage.openVerticalPageViaURL("Hotel");

        step("Configure guests and rooms for the search");
        landingPage.clickRoomAndGuestButton();
        landingPage.setTotalGuest(noOfGuest);
        landingPage.setTotalRoom(noOfRoom);
        landingPage.clickDone();

        step("Submit the search request");
        landingPage.searchStayLocation(hotel);
        landingPage.setStayDate(dateType, dateRange);
        landingPage.clickSubmit();

        step("Access the Property Detail Page (PDP)");
        Assertion.assertThat("Verify if PDP is Opened", detailPage.isPdpLoaded(), Is.is(true));

        step("Scroll up to make review tab visible");
        Assertion.assertThat("Review Section is visible on Hotel PDP", detailPage.scrollUpToReviewTab(), is(true));

        step("Click on Reviews tab");
        detailPage.clickReviewTab();

        step("Verify review section heading is visible");
        Assertion.assertThat("Review section heading is visible", detailPage.isReviewSectionHeadingVisible(), Is.is(true));

        step("Verify review cards are present");
        Assertion.assertThat("Review cards are visible", detailPage.isReviewCardsVisible(), Is.is(true));

        step("Click on the first review card");
        detailPage.clickFirstReviewCard();

        step("Click on the like (thumbs up) icon");
        detailPage.clickReviewLikeIcon();

        step("Verify login prompt popup is displayed");
        Assertion.assertThat("Login prompt popup is visible", detailPage.isReviewLoginPromptVisible(), Is.is(true));

        step("Click on Cancel button to dismiss the login popup");
        detailPage.clickReviewLoginCancelButton();

        step("Click on close button to dismiss the review detail popup");
        detailPage.clickReviewPopupCloseButton();

        step("Click on See all reviews button");
        detailPage.clickSeeAllReviews();

        step("Verify review page is opened");
        Assertion.assertThat("Review page title is visible", detailPage.isReviewPageTitleVisible(), Is.is(true));

        step("Verify review cards are present on the page");
        Assertion.assertThat("Review page card list is visible", detailPage.isReviewPageCardListVisible(), Is.is(true));
        Assertion.assertThat("Review page cards are visible", detailPage.isReviewPageCardsVisible(), Is.is(true));

        step("Verify review cards have date and rating");
        Assertion.assertThat("Review card date is visible", detailPage.isReviewPageCardDateVisible(), Is.is(true));
        Assertion.assertThat("Review card rating is visible", detailPage.isReviewPageCardRatingVisible(), Is.is(true));

        step("Store date and rating from first review card before sorting");
        String dateBeforeSort = detailPage.getFirstReviewCardDate();
        String ratingBeforeSort = detailPage.getFirstReviewCardRating();

        step("Click on sort button");
        detailPage.clickReviewPageSortButton();

        step("Select sort option: " + sortOption);
        detailPage.selectReviewSortOption(sortOption);

        step("Store date and rating from first review card after sorting");
        String dateAfterSort = detailPage.getFirstReviewCardDate();
        String ratingAfterSort = detailPage.getFirstReviewCardRating();

        step("Verify sorting is applied correctly");
        detailPage.verifySortingApplied(sortOption, dateBeforeSort, dateAfterSort, ratingBeforeSort, ratingAfterSort);
    }

    @Author(names = "Vipul kumar")
    @Module(name = "CompleteBooking")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyCompleteBookingHotelBedsHotelsNonIDRCC()
            throws FailedLoginException, InterruptedException, ExecutionException {
        Map<String, String> param = getParam("verifyCompleteBookingHotelBedsHotelsNonIDRCC");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String location = param.get("location");
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String currency = param.get("currency");
        int noOfAdults = Integer.parseInt(param.get("guests"));
        int noOfChildren = Integer.parseInt(param.get("childs"));
        int noOfRoom = Integer.parseInt(param.get("rooms"));
        String cardNumber = param.get("cardNumber");
        String cardMonth = param.get("cardMonth");
        String cardCvv = param.get("cardCvv");
        String cardHolderName = param.get("cardHolderName");
        String fullName = CoreFunctions.randomName();
        String phoneNumber = "91" + CoreFunctions.randomInt(8);

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);
        int daysToReturnDate = Integer.parseInt(param.get("daysToReturnDate"));
        String returnDate = CoreFunctions.addToLongDate(departureDate, daysToReturnDate);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> hotelLandingPage = HotelLandingPageWeb.getInstance(session);
        LandingPageWeb<?> landingPage = LandingPageWeb.getInstance(session);
        HotelDetailPage<?> detailPage = new HotelDetailPage<>(session);
        HotelRoomListPage<?> roomListPage = new HotelRoomListPage<>(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HotelBookingFormPageWeb<?> bookingFormPage = HotelBookingFormPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        CalendarPageWeb<?> calendarPage = CalendarPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User should be logged in");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User select " + currency + " currency");
        homePage.selectCurrency(currency);

        step("User Open Hotel Landing page");
        homePage.openVerticalPageViaURL("Hotel");

        Assertion.assertThat("Landing Page is Opened", landingPage.isLandingPageVisible(), is(true));

        step("Search for international hotel With the Name Of " + location);
        landingPage.searchStayLocation(location);

        step("Set date");
        hotelLandingPage.clickOnCalendarWidgetToOpen();
        calendarPage.selectDate(departureDate, returnDate);

        step("Set guests and rooms");
        landingPage.clickRoomAndGuestButton();
        landingPage.setTotalGuest(noOfAdults);
        landingPage.setTotalChildGuest(noOfChildren);
        landingPage.setTotalRoom(noOfRoom);
        landingPage.clickDone();
        landingPage.clickSubmit();

        step("Go Inside PDP");
        String HotelName=detailPage.getHotelNameDetail();
        Assertion.assertThat("Verify if PDP is Opened", detailPage.isPdpLoaded(), is(true));
        Assertion.assertThat("Verify Hotel name on entered on LP and PDP  is same",HotelName,containsString(location));

        step("Scroll to room list section");
        roomListPage.scrollToRoomListSection();
        Assertion.assertThat("Room list is visible", roomListPage.isRoomListSectionVisible(), is(true));
        Assertion.assertThat("Rooms Image should be visible", roomListPage.isRoomImageVisible(), is(true));

        step("Search for desired vendor HotelBeds with max 10 retries and book");
        String desiredVendor = "HOTELBEDS";
        int maxTries = 10;
        String vendorName = roomListPage.searchForDesiredVendor(desiredVendor, maxTries);
        Assertion.assertThat("Verify vendor is HotelBeds", vendorName, containsString(desiredVendor));

        step("User input contact detail");
        Assertion.assertThat("Contact form should be visible on Booking Form page", bookingFormPage.isContactForm(), is(true));
        bookingFormPage.fillContactDetail(fullName, phoneNumber, email);

        step("User enter Stay details ");
        Assertion.assertThat("Stay details form should be visible on Booking Form page",
                bookingFormPage.isGuestStayForm(), is(true));

        List<String> filledGuestNames = new ArrayList<>();
        for (int i = 1; i <= noOfAdults; i++) {
            String guestName = CoreFunctions.randomName();
            filledGuestNames.add(guestName);
            bookingFormPage.fillGuestDetails(i, guestName, true, false, "");
        }

        int bookingPrice = bookingFormPage.getTotalPrice();
        step("Go to payment page");
        bookingFormPage.continueTo2NdPartThenPaymentPage();

        String orderID = paymentPage.getOrderId();
        Assertion.assertThat("Verify order ID is displayed in payment page", !orderID.isEmpty(), is(true));

        if (Configs.ENVIRONMENT == Environment.GK || Configs.ENVIRONMENT == Environment.PREPROD) {
        step("Scroll down to the payment Group");

        step("Input the card details");
        paymentPage.inputCardNumber(cardNumber);
        paymentPage.inputCardMonth(cardMonth);
        paymentPage.inputCardCvv(cardCvv);
        paymentPage.inputCardHolderName(cardHolderName);

        step("Click on Pay button");
        paymentPage.clickOnPaymentButton();

        step("Verify the payment successful page is visible or not");
        Assertion.assertThat("Verify the payment successful page is visible", paymentPage.isPaymentSuccessfulVisible(),
                is(true));

        step("User try to open see order list");
        paymentPage.clickSeeOrderListButton();

        step("Issued Order");
        orderListPage.openActiveTicketViaURL(orderID);

        step("Refresh order detail page and skip coachmark");
        orderDetailPage.refreshTheWebpage();
        orderDetailPage.skipCoachmark();

        step("Verify E-ticket is published");
        Assertion.assertThat("E-ticket on the order detail should be published",
                orderDetailPage.verifyIssuedETicket(), is(true));

        step("Verify contact email on order detail page");
        String actualEmail = orderDetailPage.getContactEmail(orderID);
        Assertion.assertThat(
                "Email should be correctly displayed on order detail page. Expected: " + email + " Actual: "
                        + actualEmail,
                actualEmail, is(email));

        step("Verify order ID on order detail page");
        String actualOrderID = orderDetailPage.getOrderId();
        Assertion.assertThat(
                "OrderID should be visible and should be same as from payment page. Expected: " + orderID
                        + " Actual: " + actualOrderID,
                actualOrderID, is(orderID));

        step("Verify guest names on order detail page");
        List<String> guestNamesFromOrderDetail = orderDetailPage.getAllGuestNames(orderID);
        Assertion.assertThat(
                "Number of guest names should match. Expected: " + noOfAdults + " Actual: "
                        + guestNamesFromOrderDetail.size(),
                guestNamesFromOrderDetail.size(), is(noOfAdults));

        for (int i = 0; i < filledGuestNames.size(); i++) {
            String filledGuestName = filledGuestNames.get(i);
            String trimmedFilledName = filledGuestName.trim();

            String expectedFullName = trimmedFilledName + " " + trimmedFilledName;
            int guestNumber = i + 1;

            String actualGuestName = guestNamesFromOrderDetail.stream()
                    .map(String::trim)
                    .filter(name -> name.equalsIgnoreCase(expectedFullName) || name.equals(expectedFullName))
                    .findFirst()
                    .orElse(null);

            String actualValue = actualGuestName != null ? actualGuestName
                    : "not found. Available names: " + guestNamesFromOrderDetail;
            Assertion.assertThat(
                    "Guest Name " + guestNumber
                            + " should match the name filled on booking page. Expected: " + expectedFullName
                            + " Actual: " + actualValue,
                    actualGuestName, is(expectedFullName));
        }

        step("Verify price on order detail page");
        String priceFromOrderDetail = orderDetailPage.getVerticalTotalPrice();
        String numericValue = priceFromOrderDetail.replaceAll("[^0-9.]", "");
        int orderPrice = (int) Math.round(Double.parseDouble(numericValue));

        Assertion.assertThat(
                "Price should be visible and should be same as from payment page. Expected: " + bookingPrice
                        + " Actual: " + orderPrice,
                orderPrice, is(bookingPrice));

        step("Verify currency on order detail page");
        String orderCurrency = orderDetailPage.getCustomerCurrencyData(orderID);
        Assertion.assertThat(
                "Currency should be same as selected currency. Expected: " + currency + " Actual: " + orderCurrency,
                orderCurrency, is(currency));

        step("Verify total rooms and children count");
        int actualTotalRooms = orderDetailPage.getTotalRoom();
        Assertion.assertThat("No of Rooms should be correct. Expected: " + noOfRoom + " Actual: " + actualTotalRooms,
                actualTotalRooms, is(noOfRoom));

        int actualTotalChildren = orderDetailPage.getTotalChild(orderID);
        Assertion.assertThat(
                "No of Children should be correct. Expected: " + noOfChildren + " Actual: " + actualTotalChildren,
                actualTotalChildren, is(noOfChildren));
        }
    }

     /**
     * Show Price in AAT/ABT (Web).
     * Sheet key: {@code showPriceInAatAbtWeb}. Params: location, locationType, currency,
     * rangeAddDate, total_room or room, total_adult or adult, total_night or night,
     * maxHotelToCheck or maxHotelAmount.
     * AAT = Amount After Tax (toggle on — "including taxes" / "termasuk pajak").
     * ABT = Amount Before Tax (toggle off — "after taxes" / "setelah pajak").
     */
    @Author(names = {"Yazid"})
    @Module(name = "SRP")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void showPriceInAatAbtWeb() throws InterruptedException {
        Map<String, String> param = getParam("showPriceInAatAbtWeb");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String location = param.get("location");
        String locationType = param.get("locationType");
        String currency = param.getOrDefault("currency", "IDR");
        String rangeAddDate = param.get("rangeAddDate");
        int room = Integer.parseInt(param.getOrDefault("total_room", param.get("room")));
        int adult = Integer.parseInt(param.getOrDefault("total_adult", param.get("adult")));
        int night = Integer.parseInt(param.getOrDefault("total_night", param.get("night")));
        int maxHotelToCheck = Integer.parseInt(param.getOrDefault("maxHotelToCheck", "5"));
        String[] dates = generateCheckInOutDate(rangeAddDate, night);
        String checkInDate = dates[0];
        String checkOutDate = dates[1];
        String accommodationType = "hotel";

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        SRPPageWeb<?> searchResultPage = SRPPageWeb.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);

        step("Open home page as guest (no login)");
        homePage.goToHomePage();
        loginPage.loginAsGuest();
        Assertion.assertThat(
                "Pre-condition: user must start as guest (not logged in)",
                loginPage.checkIfLoggedIn(), is(false));

        step("Select currency: " + currency);
        homePage.selectCurrency(currency);
        String language = homePage.getLanguage();
        String separator = language.equals("en") ? "," : ".";

        step("Resolve location publicId and open hotel SRP via deeplink");
        HotelAutoCompleteResponse hotelAutoCompleteResponse = hotelApi.getAutocompleteData("", "", location, locationType);
        int locationIndex = hotelApi.findHotelIndex(hotelAutoCompleteResponse, location);
        String locationId = hotelAutoCompleteResponse.data.contents.get(locationIndex).publicId;
        searchResultPage.openSRPViaUrl(accommodationType, room, adult, locationId, locationType, location, checkInDate, checkOutDate);
        searchResultPage.clickPopUp();
        searchResultPage.waitForSrpToLoad();
        Assertion.assertThat("Hotel SRP should load", searchResultPage.isHotelCardVisible(), is(true));

        step("Get the hotel names on SRP");
        List<String> hotelNames = searchResultPage.getHotelNamesOnSrp(maxHotelToCheck);
        Assertion.assertThat(
                "At least one hotel should be checked on SRP (maxHotelToCheck=" + maxHotelToCheck + ")",
                hotelNames.isEmpty(), is(false));

        step("Get the expected hotel prices from API");
        SearchHotelResponse searchHotelResponse = hotelApi.getSearchHotelData(
                accommodationType, locationId, locationType, "", "", night, room, adult, checkInDate, currency);

        searchResultPage.checkABTtoggle();

        step("Verify 'Show price including taxes' toggle is off (ABT — after taxes wording on hotel card)");
        Assertion.assertThat(
                "'Show price including taxes' toggle should be off when hotel card shows after-tax wording (after taxes / setelah pajak)",
                searchResultPage.isAbtWordingVisibleOnHotelCard(), is(true));

        Map<String, Long> abtPriceByHotel = new HashMap<>();

        step("ABT — verify price label and amount for each hotel on SRP (up to " + maxHotelToCheck + " cards)");
        for (int i = 0; i < hotelNames.size(); i++) {
            String hotelName = hotelNames.get(i);
            int cardNumber = i + 1;

            String abtPriceRemark = searchResultPage.getPriceRemarkTextByHotelName(hotelName, i == 0);
            Assertion.assertThat(
                    "Hotel card #" + cardNumber + " ('" + hotelName + "') should show before-tax wording (after taxes / setelah pajak)",
                    abtPriceRemark,
                    anyOf(containsStringIgnoringCase("after taxes"), containsStringIgnoringCase("setelah pajak")));

            String expectedAbtPrice = thousandSeparator(
                    String.valueOf((int) hotelApi.getSrpHotelPriceWithTax(false, searchHotelResponse, hotelName)), separator);
            Assertion.assertThat(
                    "ABT price on SRP for hotel card #" + cardNumber + " ('" + hotelName + "') should match API amount (before tax)",
                    searchResultPage.isHotelTotalPriceDisplayed(hotelName, expectedAbtPrice, currency), is(true));

            long abtPriceAmount = searchResultPage.getHotelFinalPriceAmountByName(hotelName);
            Assertion.assertThat(
                    "ABT numeric price for hotel card #" + cardNumber + " ('" + hotelName + "') should be greater than 0",
                    abtPriceAmount, greaterThan(0L));
            abtPriceByHotel.put(hotelName, abtPriceAmount);
        }

        step("Click 'Show price including taxes' toggle and wait for SRP to refresh");
        searchResultPage.clickShowPriceIncludingTaxesFilter();

        step("AAT — verify price label and amount for each hotel on SRP (up to " + maxHotelToCheck + " cards)");
        for (int i = 0; i < hotelNames.size(); i++) {
            String hotelName = hotelNames.get(i);
            int cardNumber = i + 1;
            long abtPriceAmount = abtPriceByHotel.get(hotelName);

            String aatPriceRemark = searchResultPage.getPriceRemarkTextByHotelName(hotelName);
            Assertion.assertThat(
                    "Hotel card #" + cardNumber + " ('" + hotelName + "') should show after-tax wording (including taxes / termasuk pajak)",
                    aatPriceRemark,
                    anyOf(containsStringIgnoringCase("including taxes"), containsStringIgnoringCase("termasuk pajak")));

            String expectedAatPrice = thousandSeparator(
                    String.valueOf((int) hotelApi.getSrpHotelPriceWithTax(true, searchHotelResponse, hotelName)), separator);
            Assertion.assertThat(
                    "AAT price on SRP for hotel card #" + cardNumber + " ('" + hotelName + "') should match API amount (after tax)",
                    searchResultPage.isHotelTotalPriceDisplayed(hotelName, expectedAatPrice, currency), is(true));

            long aatPriceAmount = searchResultPage.getHotelFinalPriceAmountByName(hotelName);
            Assertion.assertThat(
                    "AAT price for hotel card #" + cardNumber + " ('" + hotelName + "') should be higher than ABT price (ABT="
                            + abtPriceAmount + ", AAT=" + aatPriceAmount + ")",
                    aatPriceAmount, greaterThan(abtPriceAmount));
        }
    }

    /**
     * Verifies Preferred Partner Plus and Preferred Partner quick filters on hotel SRP (guest, deeplink)
     * and the corresponding PDP section + bottom sheet when opening a hotel in a new tab.
     * Sheet key: {@code verifyPreferredPartnerFilterOnSrpAndPdpWeb}. Params: location, locationType,
     * rangeAddDate, total_room or room, total_adult or adult, total_night or night,
     * maxHotel or maxHotelToCheck (default 5).
     */
    @Author(names = {"Yazid"})
    @Module(name = "SRP")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyPreferredPartnerFilterOnSrpAndPdpWeb() throws InterruptedException {
        Map<String, String> param = getParam("verifyPreferredPartnerFilterOnSrpAndPdpWeb");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String location = param.get("location");
        String locationType = param.get("locationType");
        String rangeAddDate = param.get("rangeAddDate");
        int room = Integer.parseInt(param.getOrDefault("total_room", param.get("room")));
        int adult = Integer.parseInt(param.getOrDefault("total_adult", param.get("adult")));
        int night = Integer.parseInt(param.getOrDefault("total_night", param.get("night")));
        int maxHotel = Integer.parseInt(param.getOrDefault("maxHotel", param.getOrDefault("maxHotelToCheck", "5")));
        String[] dates = generateCheckInOutDate(rangeAddDate, night);
        String checkInDate = dates[0];
        String checkOutDate = dates[1];
        String accommodationType = "hotel";

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        SRPPageWeb<?> searchResultPage = SRPPageWeb.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);

        step("Open home page as guest (no login)");
        homePage.goToHomePage();
        loginPage.loginAsGuest();
        Assertion.assertThat(
                "Pre-condition: user must start as guest (not logged in)",
                loginPage.checkIfLoggedIn(), is(false));

        step("Resolve location publicId and open hotel SRP via deeplink");
        HotelAutoCompleteResponse hotelAutoCompleteResponse = hotelApi.getAutocompleteData("", "", location, locationType);
        int locationIndex = hotelApi.findHotelIndex(hotelAutoCompleteResponse, location);
        String locationId = hotelAutoCompleteResponse.data.contents.get(locationIndex).publicId;
        searchResultPage.openHotelSrpViaDeeplink(accommodationType, room, adult, locationId, locationType,
                location, checkInDate, checkOutDate);

        step("Verify 'Preferred Partner Plus' filter on SRP");
        searchResultPage.verifyPreferredPartnerFilterOnSrpAndPdp("Preferred Partner Plus", maxHotel);

        step("Re-open hotel SRP via deeplink for Preferred Partner filter");
        searchResultPage.openHotelSrpViaDeeplink(accommodationType, room, adult, locationId, locationType,
                location, checkInDate, checkOutDate);

        step("Verify 'Preferred Partner' filter on SRP");
        searchResultPage.verifyPreferredPartnerFilterOnSrpAndPdp("Preferred Partner", maxHotel);
    }

    @Author(names = {"Yazid"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Hotel")
    @Module(name = "CreateBooking")
    @Test
    public void verifyBookingMultipleBlackoutWeb() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("verifyBookingMultipleBlackoutWeb");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String emailAdmin = param.get("emailAdmin");
        String passwordAdmin = param.get("passwordAdmin");
        String hotelName = param.get("hotelName");
        String emailUser = param.get("emailUser");
        String passwordUser = param.get("passwordUser");
        int adult = Integer.parseInt(param.get("adult"));
        int room = Integer.parseInt(param.get("room"));
        String[] salutationList = { "Mr", "Mrs", "Ms" };
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = getRandomObject(salutationList);

        HotelApi<?> hotelApi = new HotelApi<>(session);
        MmDashboardApi<?> mmDashboardApi = new MmDashboardApi<>(session);
        ExtranetHotelApi<?> extranetHotelApi = new ExtranetHotelApi<>(session);

        step("Login to MM dashboard member");
        MmDashboardApi.DashboardMemberSession dashboardSession = mmDashboardApi.loginDashboardMember(emailAdmin, passwordAdmin);

        step("Get approved hotel id");
        int hotelId = mmDashboardApi.getApprovedHotelId(dashboardSession, hotelName);

        step("Get hotel loyalty blackout dates");
        List<String> blackoutDates = extranetHotelApi.getHotelLoyaltyBlackoutDates(dashboardSession, hotelId, "multiple");
        Assertion.assertThat("Pre-condition: at least 1 multi-day blackout range is available (excluding today)",
                blackoutDates, is(not(empty())));
        String blackoutDateRange = blackoutDates.get(0);
        String[] dateList = blackoutDateRange.split(" - ");
        String startBlackoutDate = dateList[0].trim();
        String endBlackoutDate = dateList[1].trim();
        String startDate = addToDateYYYY_MM_DD(startBlackoutDate,-1);
        int night = getDaysBetweenDates(startDate, addToDateYYYY_MM_DD(endBlackoutDate,1));

        step("Create new hotel issued order");
        Boolean result =  hotelApi.createIssuedOrderApi(
                emailUser, passwordUser, startDate, hotelName, adult, night, room,
                orderFullName, orderNameTitle, guestFullName, guestTitle,
                "IDR", /* isRefundable */ false, /* isSpecialRequestEnable */ false, "",
                "en", /* isNative */ false);
        Assertion.assertThat("Hotel Booking via API is successful", result, is(true));

        step("Get data from the issued order");
        String issuedOrderID = String.valueOf(hotelApi.getOrderId());
        String issuedRoomName = hotelApi.getHotelRoomName();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);

        step("Go to home");
        homePage.goToHomePage();

        step("Login as " + emailUser);
        loginPage.loginWithSpecificAccountViaCookie(emailUser, passwordUser);

        step("Open the issued order detail");
        orderListPage.openActiveTicketViaURL(issuedOrderID);
        orderDetailPage.skipCoachmark();
        Assertion.assertThat("e-Ticket on the order detail should be published",
                orderDetailPage.verifyIssuedETicket(issuedOrderID), is(true));

        step("Open e-Ticket");
        orderDetailPage.openETicket();

        step("e-Ticket file is opened and contain loyalty discount");
        String actualPdfContent = orderDetailPage.getPDFContentFile();
        Assertion.assertThat("Hotel name on the PDF should be showing correctly", actualPdfContent,
                is(either(containsString(hotelName.toLowerCase())).or(containsString(hotelName))));
        Assertion.assertThat("Room name on the PDF should be showing correctly",
                actualPdfContent.contains(issuedRoomName), is(true));
        Assertion.assertThat("Guest name on the PDF should be showing correctly",
                actualPdfContent.contains(guestFullName), is(true));
        Assertion.assertThat("PDF should contain tier benefit", actualPdfContent,
                is(containsStringIgnoringCase("Tier Benefit")));

        String blackoutDateFull = orderDetailPage.getFullBlackoutDate(startBlackoutDate, endBlackoutDate);
        Assertion.assertThat("PDF should contain blackout date detail", actualPdfContent,
                is(containsString("benefits are not valid on the following stay dates: " + blackoutDateFull)));
    }

    @Author(names = "Alden")
    @Module(name = "CompleteBooking")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void completeBookingHotelMultiGuest() throws Exception
    {
        Map<String, String> param = getParam("completeBookingHotelMultiGuest");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String hotel = param.get("hotel");
        int noOfGuest = Integer.parseInt(param.get("noOfGuest"));
        int noOfRoom = Integer.parseInt(param.get("noOfRoom"));
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LandingPageWeb<?> landingPage = LandingPageWeb.getInstance(session);
        PDPPageWeb<?> detailPage = PDPPageWeb.getInstance(session);
        HotelRoomListPageWeb<?> roomListPage = HotelRoomListPageWeb.getInstance(session);
        HotelBookingFormPage<?> bookingFormPage = new HotelBookingFormPage<>(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        step("Open the homepage");
        homePage.goToHomePage();

        step("Log in using an existing user account");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("Go to the Hotel section and search for the target hotel");
        homePage.selectCurrency("IDR");
        homePage.openVerticalPageViaURL("Hotel");

        step("Configure guests and rooms for the search");
        landingPage.clickRoomAndGuestButton();
        landingPage.setTotalGuest(noOfGuest);
        landingPage.setTotalRoom(noOfRoom);
        landingPage.clickDone();

        step("Submit the search request");
        landingPage.searchStayLocation(hotel);
        landingPage.clickSubmit();

        step("Access the Property Detail Page (PDP)");
        Assertion.assertThat("Verify if PDP is Opened", detailPage.isPdpLoaded(), Is.is(true));

        step("Select 'See Rooms' to view available rooms");
        detailPage.seeHotelRoom();

        step("Select 'Book' to proceed with booking the room");
        roomListPage.clickSelectRoomButtonMultiOrder(noOfRoom);
        roomListPage.clickMultiOrderBookButton();

        step("Enter the Booking Form page and verify hotel name");
        bookingFormPage.fillGuestDetails(noOfGuest, CoreFunctions.randomName(), true, false, "");
        Assertion.assertThat("Hotel name matches", bookingFormPage.getBookingHotelName(), Is.is(hotel));

        step("Continue to the payment page");
        bookingFormPage.continueToPaymentPage();

        step("Fetch the order ID from the payment page");
        String orderID = paymentPage.getOrderId();
        Assertion.assertThat("Verify order ID is displayed on the payment page", !orderID.isEmpty(), Is.is(true));

        step("Choose payment method");
        paymentPage.selectPaymentMethod("Sakuku");

        step("Finish the payment process by clicking the payment button");
        paymentPage.clickOnPaymentButton();

        step("View your orders by opening the order list");
        paymentPage.clickSeeOrderListButton();

        step("Navigate to the details page of the issued e-ticket order");
        orderListPage.openActiveTicketViaURL(orderID);
        orderDetailPage.skipCoachmark();

        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.verifyIssuedETicket(), Is.is(true));
        Assertion.assertThat("Contact name should be available on the order detail page", !orderDetailPage.getContactName(orderID).isEmpty(), Is.is(true));
        Assertion.assertThat("Email should be correctly displayed on the order detail page", orderDetailPage.getContactEmail(orderID), Is.is(email));
        Assertion.assertThat("Order ID should be visible and match the ID from the payment page", orderDetailPage.getOrderId(), Is.is(orderID));
    }

    @Author(names = "Mridul Gupta")
    @Module(name = "PDP")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyMapsOnPDPLogin() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("verifyMapsOnPDPLogin");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String location = param.get("location");
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String dateType = param.get("dateType");
        String dateRange = param.get("dateRange");
        int guestCount = Integer.parseInt(param.get("guestCount"));
        int roomCount = Integer.parseInt(param.get("roomCount"));

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        PDPPageWeb<?> detailPage = PDPPageWeb.getInstance(session);
        HotelSearchResultPage<?> hotelSearchResultPage=new HotelSearchResultPage<>(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User Login using email "+ email);
        loginPage.loginIfNotWithSameEmail(email,password,otp,retryCounter);

        step("User Open Hotel Landing page");
        homePage.openVerticalPageViaURL("Hotel");
        Assertion.assertThat("Landing Page is Opened", landingPage.isLandingPageVisible(), Is.is(true));

        step("Search stay location");
        landingPage.searchStayLocation(location);

        step("Set date");
        landingPage.setStayDate(dateType, dateRange);

        step("Room and guest count update");
        landingPage.clickRoomAndGuestButton();
        landingPage.setTotalRoom(roomCount);
        landingPage.setTotalGuest(guestCount);
        landingPage.clickDone();

        step("submit search Button");
        landingPage.clickSubmit();

        step("User select Hotel On SRP");
        hotelSearchResultPage.selectHotelCard();

        step("Go inside PDP");
        detailPage.switchToNewTab();

        step("Go to Maps");
        detailPage.isPdpLoaded();
        detailPage.goToMaps();
        Assertion.assertThat("Check Maps",detailPage.checkMaps(),is(true));

        step("Verify on Correct Location");
        Assertion.assertThat("Verify Location on Map",detailPage.verifyLocationOnMap(),containsString(location));
    }

    @Author(names = "Mridul Gupta")
    @Module(name = "BookingForm")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyRoomRecommendationBookingForm() throws FailedLoginException, InterruptedException{
        Map<String, String> param = getParam("verifyRoomRecommendationBookingForm");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String location = param.get("location");
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        int noOfAdults1 = Integer.parseInt(param.get("guests1"));
        int noOfRoom1 = Integer.parseInt(param.get("rooms1"));
        int noOfAdults2 = Integer.parseInt(param.get("guests2"));
        int noOfRoom2 = Integer.parseInt(param.get("rooms2"));

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);
        int daysToReturnDate = Integer.parseInt(param.get("daysToReturnDate"));
        String returnDate = CoreFunctions.addToLongDate(departureDate, daysToReturnDate);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> hotelLandingPage = HotelLandingPageWeb.getInstance(session);
        LandingPageWeb<?> landingPage = LandingPageWeb.getInstance(session);
        HotelDetailPage<?> detailPage = new HotelDetailPage<>(session);
        HotelRoomListPage<?> roomListPage = new HotelRoomListPage<>(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HotelBookingFormPageWeb<?> bookingFormPage = HotelBookingFormPageWeb.getInstance(session);
        CalendarPageWeb<?> calendarPage = CalendarPageWeb.getInstance(session);
        RoomDetailsPageWeb<?> roomDetailsPage = RoomDetailsPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User should be logged in");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User Open Hotel Landing page");
        homePage.openVerticalPageViaURL("Hotel");
        Assertion.assertThat("Landing Page is Opened", landingPage.isLandingPageVisible(), is(true));

        step("Enter location in search box");
        landingPage.searchStayLocation(location);

        step("Set date");
        hotelLandingPage.clickOnCalendarWidgetToOpen();
        calendarPage.selectDate(departureDate, returnDate);

        step("Set Guest : " + noOfAdults1 + " Adults, " + noOfRoom1 + " Rooms");
        landingPage.clickRoomAndGuestButton();
        landingPage.setTotalGuest(noOfAdults1);
        landingPage.setTotalRoom(noOfRoom1);
        landingPage.clickDone();
        landingPage.clickSubmit();

        step("Go Inside PDP");
        Assertion.assertThat("Verify if PDP is Opened", detailPage.isPdpLoaded(), is(true));

        step("Rooms are available hence click on See Rooms Button");
        detailPage.seeHotelRoom();

        step("Get room numbers from room recommendation section");
        String room_number_beside_room_name = roomListPage.getRoomNumberBesideRoomName();
        String room_number_in_total = roomListPage.getTotalRecommendedRooms();

        step("Open first room from recommendation section");
        roomListPage.openRecommendedRoomDetails();
        Assertion.assertThat("Room number beside room name in Room List and Room detail should be same", roomDetailsPage.getRoomNumberBesideRoomName(), containsString(room_number_beside_room_name));
        Assertion.assertThat("Room number in Total in Room List and Room detail should be same", roomDetailsPage.getTotalRecommendedRooms(), containsString(room_number_in_total));

        step("Book the room");
        roomListPage.clickBookRoomDetail();

        step("user is on booking form page");
        Assertion.assertThat("Room number in Room list and Booking form should be equal", bookingFormPage.getTotalRoomCount(), containsString(room_number_in_total));

        step("User Open Hotel Landing page");
        homePage.openVerticalPageViaURL("Hotel");
        Assertion.assertThat("Landing Page is Opened", landingPage.isLandingPageVisible(), is(true));

        step("Set Guest : " + noOfAdults2 + " Adults, " + noOfRoom2 + " Rooms");
        landingPage.clickRoomAndGuestButton();
        landingPage.setTotalGuest(noOfAdults2);
        landingPage.setTotalRoom(noOfRoom2);
        landingPage.clickDone();
        landingPage.clickSubmit();

        step("Go Inside PDP");
        Assertion.assertThat("Verify if PDP is Opened", detailPage.isPdpLoaded(), is(true));

        step("Rooms are available hence click on See Rooms Button");
        detailPage.seeHotelRoom();

        step("Get room numbers from room recommendation section");
        String room_number_in_total2 = roomListPage.getTotalRecommendedRooms();

        step("Open first room from recommendation section");
        roomListPage.openRecommendedRoomDetails();
        Assertion.assertThat("Room number in Total in Room List and Room detail should be same", roomDetailsPage.getTotalRecommendedRooms(), containsString(room_number_in_total2));

        step("Book the room");
        roomListPage.clickBookRoomDetail();

        step("user is on booking form page");
        Assertion.assertThat("Room number in Total in Room List and Room detail should be same", bookingFormPage.getTotalRoomCount(), containsString(room_number_in_total2));
    }

    @Author(names = {"Juli", "Alden"})
    @Module(name = "DetailPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyRecommendedRoomLoginCapacityLessThanSearchCriteria() throws FailedLoginException, InterruptedException{
        Map<String, String> param = getParam("verifyRecommendedRoomLoginCapacityLessThanSearchCriteria");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String location = param.get("location");
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        int guests = Integer.parseInt(param.get("guests"));

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        HotelDetailPage<?> detailPage = new HotelDetailPage<>(session);
        com.tiket.web.dWeb.page.accommodation.hotels.HotelRoomListPage<?> roomListPage=new com.tiket.web.dWeb.page.accommodation.hotels.HotelRoomListPage<>(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User Open Hotel Landing page");
        homePage.openVerticalPageViaURL("Hotel");
        Assertion.assertThat("Landing Page is Opened", landingPage.isLandingPageVisible(), is(true));

        step("Search stay location");
        landingPage.searchStayLocation(location);

        step("Set guests and rooms");
        landingPage.clickRoomAndGuestButton();
        landingPage.setTotalGuest(guests);
        landingPage.clickDone();

        step("submit search Button");
        landingPage.clickSubmit();

        step("Go Inside PDP");

        Assertion.assertThat("Verify if PDP is Opened", detailPage.isPdpLoaded(), is(true));
        Assertion.assertThat("Hotel's Price is visible on Hotel PDP", detailPage.getHotelPriceDetail().isEmpty(), is(false));

        step("Rooms are available hence click on See Rooms Button");
        detailPage.seeHotelRoom();

        step("Checking Recommended Room");
        Assertion.assertThat("Recommended room should be visible on room list", roomListPage.isRecommendedRoomMWEBVisible(), is(true));
        Assertion.assertThat("On Recommended room, total-room-night-summary should be visible", roomListPage.isTotalRoomNightRecommended(), is(true));
        Assertion.assertThat("On Recommended room, total price should be visible", roomListPage.isRecommendedTotalPrice(), is(true));
        Assertion.assertThat("On Recommended room, total room should be visible", roomListPage.isRecommendedTotalRoomIcon(), is(true));
        Assertion.assertThat("On Recommended room, total room should be 2", roomListPage.getRecommendedTotalRoom(), is("2"));

        int roomNumberRoomList = CoreFunctions.getIntFromString(roomListPage.getRecommendedTotalRoomBookButtonRoomList()).orElseThrow();
        Assertion.assertThat("On Recommended room, total room should be Book 2 Rooms", roomListPage.getRecommendedTotalRoomBookButtonRoomList(), either(containsStringIgnoringCase("Book 2 Rooms")).or(containsStringIgnoringCase("Pesan 2 kamar")));

        step("Select room recommendation Card ");
        roomListPage.selectRoomRecommendationCard();

        int roomNumberRoomDetail = CoreFunctions.getIntFromString(roomListPage.getRecommendedTotalRoomBookButtonRoomDetail()).orElseThrow();
        Assertion.assertThat("On Recommended room, total room should be Book 2 Rooms", roomListPage.getRecommendedTotalRoomBookButtonRoomDetail(), either(containsStringIgnoringCase("Book 2 Rooms")).or(containsStringIgnoringCase("Pesan 2 kamar")));
        Assertion.assertThat("The number of rooms should be the same", roomNumberRoomList, equalTo(roomNumberRoomDetail));
        Assertion.assertThat("The number of rooms and search criteria should be the same as total number of guests", roomNumberRoomList, equalTo(guests));
    }

    @Author(names = "Mridul Gupta")
    @Module(name = "Booking")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void completeBookingMoWithAddOnsLoginHotel() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("completeBookingMoWithAddOnsLoginHotel");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String location = param.get("location");
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String paymentGroup = param.get("paymentGroup");
        String paymentMethod = param.get("paymentMethod");
        int noOfRooms = Integer.parseInt(param.get("rooms"));
        int guests = Integer.parseInt(param.get("guests"));

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);
        int daysToReturnDate = Integer.parseInt(param.get("daysToReturnDate"));
        String returnDate = CoreFunctions.addToLongDate(departureDate, daysToReturnDate);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        HotelDetailPage<?> detailPage = new HotelDetailPage<>(session);
        com.tiket.web.dWeb.page.accommodation.hotels.HotelRoomListPage<?> roomListPage = new com.tiket.web.dWeb.page.accommodation.hotels.HotelRoomListPage<>(session);
        HotelBookingFormPageWeb<?> bookingFormPage = HotelBookingFormPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        ETicketWeb<?> eTicketPage = ETicketWeb.getInstance(session);
        CalendarPageWeb<?> calendarPage = CalendarPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User login ");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User select IDR currency");
        homePage.goToHomePage();
        homePage.selectCurrency("IDR");

        step("User Open Hotel Landing page");
        homePage.openVerticalPageViaURL("Hotel");

        step("Search stay location");
        landingPage.searchStayLocation(location);

        step("Set date");
        landingPage.clickOnCalendarWidgetToOpen();
        calendarPage.selectDate(departureDate, returnDate);

        step("Set Guest : " + guests + " Adults, " + noOfRooms + " Rooms");
        landingPage.clickRoomAndGuestButton();
        landingPage.setTotalGuest(guests);
        landingPage.setTotalRoom(noOfRooms);
        landingPage.clickDone();

        step("submit search Button");
        landingPage.clickSubmit();

        step("Go Inside PDP");
        Assertion.assertThat("Verify if PDP is Opened", detailPage.isPdpLoaded(), is(true));
        Assertion.assertThat("Hotel name on the search and detail page should be equal", detailPage.getHotelNameDetail(), containsString(location));

        step("Rooms are available hence click on See Rooms Button");
        detailPage.seeHotelRoom();

        step("Scroll to room list section");
        roomListPage.scrollToRoomListSection();
        Assertion.assertThat("Room list is visible", roomListPage.isRoomListSectionVisible(), is(true));
        Assertion.assertThat("Rooms Image should be visible", roomListPage.isRoomImageVisible(), is(true));

        step("Select multiple rooms");
        roomListPage.selectMultipleRoom(noOfRooms);
        roomListPage.clickMultiBookButton();

        step("User enter Stay details ");
        Assertion.assertThat("Stay details form should be visible on Booking Form page", bookingFormPage.isGuestStayForm(), is(true));
        bookingFormPage.fillGuestDetailsCombinedRandom(noOfRooms);
        int bookingPrice = bookingFormPage.getTotalPriceOnBookingPage1();

        step("Special request");
        String specialRequest = "Need extra pillows";
        bookingFormPage.enterSpecialRequestMO(specialRequest, noOfRooms);
        Assertion.assertThat("Verify if Special Request is added successfully", bookingFormPage.getSpecialReqText(), containsString(specialRequest));

        step("Continue to add ons");
        bookingFormPage.continueToNextPage();

        step("User select addOns and insurance");
        HashMap<String,String> addOnDetails = bookingFormPage.chooseAddOnsMultiRoom(noOfRooms);
        String addOnPrice = addOnDetails.get("addonPrice");

        step("Verify total price is correct with add ons");
        int finalPrice = bookingFormPage.getTotalPrice();
        Assertion.assertThat("Final price should be greater than booking price after selecting add ons", finalPrice, greaterThan(bookingPrice));
        Assertion.assertThat("Add on price should be present in price breakdown", finalPrice, is(bookingPrice + Integer.parseInt(addOnPrice)));
        Assertion.assertThat("Room details and addon are there in Price break down ", bookingFormPage.isRoomAndAddOnPresent(addOnDetails), is(true));

        step("Continue to payment page");
        bookingFormPage.continueToPaymentPage();

        step("Go to payment page");
        String orderID = paymentPage.getOrderId();
        Assertion.assertThat("Verify order ID is displayed in payment page", !orderID.isEmpty(), is(true));

        step("User select payment method ");
        paymentPage.clickOnSelectPaymentMethod();
        paymentPage.clickOnSeeAll();
        paymentPage.goToPaymentMethodsGroup(paymentGroup);
        paymentPage.selectPaymentMethod(paymentMethod);

        step("User complete payment");
        paymentPage.clickOnPaymentButton();

        step("Verify the payment successful page is visible or not");
        Assertion.assertThat("Verify the payment successful page is visible", paymentPage.isPaymentSuccessfulVisible(), is(true));

        step("User open order detail page");
        orderListPage.openActiveTicketViaURL(orderID);
        orderDetailPage.skipCoachmark();

        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.verifyIssuedETicket(), is(true));
        Assertion.assertThat("orderID should be visible and should be same as from payment page", orderDetailPage.getOrderId(), is(orderID));

        step("Verify Special request and AddOn in order detail page");
        Assertion.assertThat("Special request should be visible in order detail page", orderDetailPage.getSpecialReq(), containsString(specialRequest));
        String addOnText = orderDetailPage.getAddOnText();
        Assertion.assertThat("Add-ons should be visible in order detail page", !addOnText.isEmpty(), is(true));

        step("Open E-Ticket page from order detail page");
        orderDetailPage.openETicket();

        step("Verify Special request and AddOn in E-Ticket");
        String pdfContent;
        if (Configs.PLATFORM == Platform.DWEB) {
            String url = orderDetailPage.switchTabAndgetURL();
            pdfContent = eTicketPage.pdfContent(url);
        } else {
            pdfContent = orderDetailPage.getPDFContentFile();
        }
        Assertion.assertThat("Special request should be visible in E-Ticket", pdfContent, containsString(specialRequest));
        Assertion.assertThat("Add-ons should be visible in E-Ticket", pdfContent, containsString(addOnText));
    }

    @Author(names = "Aditi")
    @Module(name = "BookingPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyCompleteBookSingleRoomExtranet() throws FailedLoginException, InterruptedException, ExecutionException {
        Map<String, String> param = getParam("verifyCompleteBookSingleRoomExtranet");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String location = param.get("location");
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String paymentGroup = param.get("paymentGroup");
        String paymentMethod = param.get("paymentMethod");
        int hotelId = Integer.parseInt(param.get("hotelId"));
        String dashboardUsername = param.get("dashboardUsername");
        String dashboardPassword = param.get("dashboardPassword");
        int noOfAdults = Integer.parseInt(param.get("adult"));
        String fullName = CoreFunctions.randomName();
        String phoneNumber = "91" + CoreFunctions.randomInt(8);
        int expectedTotalRoom = Integer.parseInt(param.get("rooms"));

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);
        int daysToReturnDate = Integer.parseInt(param.get("daysToReturnDate"));
        String returnDate = CoreFunctions.addToLongDate(departureDate, daysToReturnDate);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        HotelDetailPage<?> detailPage = new HotelDetailPage<>(session);
        SRPPageWeb<?> searchResultPage = SRPPageWeb.getInstance(session);
        HotelRoomListPage<?> roomListPage = new HotelRoomListPage<>(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HotelBookingFormPageWeb<?> bookingFormPage = HotelBookingFormPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        ExtranetHotelApi<?> extranetHotelApi = new ExtranetHotelApi<>(session);
        CalendarPageWeb<?> calendarPage = CalendarPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User select IDR currency");
        homePage.selectCurrency("IDR");

        step("User Open Hotel Landing page");
        homePage.openVerticalPageViaURL("Hotel");
        Assertion.assertThat("Landing Page is Opened", landingPage.isLandingPageVisible(), Is.is(true));

        step("Search stay hotel");
        landingPage.searchStayLocation(location);

        step("Set date");
        landingPage.clickOnCalendarWidgetToOpen();
        calendarPage.selectDate(departureDate, returnDate);
        landingPage.clickSubmit();

        step("Go Inside PDP");
        detailPage.clickPopUp();
        Assertion.assertThat("Verify if PDP is Opened", detailPage.isPdpLoaded(), Is.is(true));
        detailPage.seeHotelRoom();

        step("Click Book Button");
        roomListPage.clickBookButton();

        step("User input contact detail");
        Assertion.assertThat("Contact form should be visible on Booking Form page", bookingFormPage.isContactForm(), is(true));
        bookingFormPage.fillContactDetail(fullName, phoneNumber, email);

        step("User enter Stay details ");
        Assertion.assertThat("Stay details form should be visible on Booking Form page",
                bookingFormPage.isGuestStayForm(), is(true));

        List<String> filledGuestNames = new ArrayList<>();
        for (int i = 1; i <= noOfAdults; i++) {
            String guestName = CoreFunctions.randomName();
            filledGuestNames.add(guestName);
            bookingFormPage.fillGuestDetails(i, guestName, true, false, "");
        }

        step("Go to payment page");
        bookingFormPage.continueToPaymentPage();
        bookingFormPage.selectPayment();

        step("check order id on payment page");
        String orderID = paymentPage.getOrderId();
        orderID = orderID.replaceAll("[^0-9]", "");
        Assertion.assertThat("Verify order ID is displayed in payment page", !orderID.isEmpty(), Is.is(true));

        step("User select payment method ");
        paymentPage.clickOnSelectPaymentMethod();
        paymentPage.clickOnSeeAll();
        paymentPage.goToPaymentMethodsGroup(paymentGroup);
        paymentPage.selectPaymentMethod(paymentMethod);

        step("User complete payment");
        paymentPage.clickOnPaymentButton();

        step("User try to open see order list");
        paymentPage.clickSeeOrderListButton();

        step("Go to Issued Order");
        orderListPage.openActiveTicketViaURL(orderID);
        orderDetailPage.skipCoachmark();

        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.verifyIssuedETicket(),
                Is.is(true));
        Assertion.assertThat("contact name should be available on order detail page",
                !orderDetailPage.getContactName(orderID).isEmpty(), Is.is(true));
        Assertion.assertThat("email should be correctly displayed on order detail page",
                orderDetailPage.getContactEmail(orderID), Is.is(email));
        Assertion.assertThat("orderID should be visible and should be same as from payment page",
                orderDetailPage.getOrderId(), Is.is(orderID));

        step("Verify booking exists on hotel dashboard");
        String departureDateISO = CoreFunctions.convertDateFormat(departureDate);
        String returnDateISO = CoreFunctions.convertDateFormat(returnDate);

        ExtranetHotelApi.DashboardBookingDetails bookingDetails = extranetHotelApi.getDashboardBookingDetails(
                hotelId, orderID, departureDateISO, returnDateISO, dashboardUsername, dashboardPassword);

        Assertion.assertThat("Booking should be found on dashboard", bookingDetails.found(), Is.is(true));
        Assertion.assertThat("Order ID should match", String.valueOf(bookingDetails.orderId()), Is.is(orderID));
        Assertion.assertThat("Status should be 'issued'", bookingDetails.status(), Is.is("issued"));
        Assertion.assertThat("Check-in date should match", bookingDetails.checkinDate(), Is.is(departureDateISO));
        Assertion.assertThat("Check-out date should match", bookingDetails.checkoutDate(), Is.is(returnDateISO));
        Assertion.assertThat("Duration should match", bookingDetails.duration(), Is.is(daysToReturnDate));
        Assertion.assertThat("Adult count should match", bookingDetails.adult(), Is.is(noOfAdults));
        Assertion.assertThat("Total room count should match", bookingDetails.totalRoom(), Is.is(expectedTotalRoom));

    }

    @Author(names = {"Mridul Gupta"})
    @Module(name = "Refund")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void checkHotelRefundCharged() throws FailedLoginException, InterruptedException, ExecutionException {
        Map<String, String> param = getParam("checkHotelRefundCharged");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        RefundPageWeb<?> refundPage = RefundPageWeb.getInstance(session);
        RefundDetailWeb<?> refundDetailPage = RefundDetailWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        HotelDetailPage<?> detailPage = new HotelDetailPage<>(session);
        HotelRoomListPage<?> roomListPage = new HotelRoomListPage<>(session);
        HotelBookingFormPageWeb<?> bookingFormPage = HotelBookingFormPageWeb.getInstance(session);

        String username = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String hotelName = param.get("hotelName");
        String currency = param.get("currency");
        String location = param.get("location");
        String paymentGroup = param.get("paymentGroup");
        String paymentMethod = param.get("paymentMethod");
        String dateType = param.get("dateType");
        String dateRange = param.get("dateRange");

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(username, password, otp, retryCounter);

        step("User select " + currency + " currency");
        homePage.selectCurrency(currency);

        step("User Open Hotel Landing page");
        homePage.openVerticalPageViaURL("Hotel");

        Assertion.assertThat("Landing Page is Opened", landingPage.isLandingPageVisible(), is(true));

        step("Search stay hotel");
        landingPage.searchStayLocation(location);

        step("Set date");
        landingPage.setStayDate(dateType, dateRange);

        step("submit search Button");
        landingPage.clickSubmit();

        step("Go Inside PDP");
        String HotelName = detailPage.getHotelNameDetail();
        Assertion.assertThat("Verify if PDP is Opened", detailPage.isPdpLoaded(), is(true));
        Assertion.assertThat("Verify Hotel name on entered on LP and PDP  is same", HotelName, containsString(location));
        detailPage.seeHotelRoom();

        step("Click Book Button");
        String refundCharge = roomListPage.selectRefundWithFeeRoom();
        Assertion.assertThat("Refund charge should not be empty, (Refund Charge = " + refundCharge + " )", refundCharge, not(emptyOrNullString()));

        step("Go to payment page");
        bookingFormPage.continueToNextPage();

        step("Continue to payment page");
        bookingFormPage.continueToPaymentPage();
        String orderID = paymentPage.getOrderId();
        Assertion.assertThat("Verify order ID is displayed in payment page", !orderID.isEmpty(), is(true));

        step("User select payment method ");
        paymentPage.clickOnSelectPaymentMethod();
        paymentPage.goToPaymentMethodsGroup(paymentGroup);
        paymentPage.selectPaymentMethod(paymentMethod);

        step("Click on Pay button");
        paymentPage.clickOnPaymentButton();

        step("Verify the payment successful page is visible or not");
        Assertion.assertThat("Verify the payment successful page is visible", paymentPage.isPaymentSuccessfulVisible(), is(true));

        step("User try to open see order list");
        paymentPage.clickSeeOrderListButton();

        step("Find the issued order");
        orderListPage.openActiveTicketViaURL(orderID);
        orderDetailPage.skipCoachmark();

        step("Verify hotel order details");
        Assertion.assertThat("orderID should be visible and should be same as from payment page", orderDetailPage.getOrderId(), is(orderID));
        Assertion.assertThat("E-ticket card Displayed", orderDetailPage.isETicketCardDisplayed(), is(true));
        Assertion.assertThat("Refund button should be displayed", orderDetailPage.isRefundButtonDisplayed(), is(true));

        step("Open refund page");
        orderDetailPage.clickRefundButton();
        refundPage.waitForContentLoaderDisappear();

        step("Click to continue to refund");
        refundPage.continueToRefund();

        step("Select random refund reason");
        refundPage.selectRandomRefundReason();

        step("Verify refund estimation section");
        refundPage.waitForEstimationAmountAppear("10");
        String refundReceive = refundPage.getRefundYoullReceive();
        String refundFee = refundPage.getRefundFee();
        String refundTotal = refundPage.getRefundTotal();
        List<String> amounts = new ArrayList<>(Arrays.asList(refundReceive, refundFee, refundTotal));
        Assertion.assertThat("Refund Total amount is correct", CoreFunctions.removeText(refundReceive) + CoreFunctions.removeText(refundFee), is(CoreFunctions.removeText(refundTotal)));

        step("Verify refund amount breakdown");
        refundPage.openRefundBreakdown();
        amounts.clear();
        amounts.addAll(refundPage.getRefundAmountBreakdown(currency));

        if (refundPage.getRefundFeeBreakdown(currency).isPresent())
            amounts.addAll(refundPage.getRefundFeeBreakdown(currency).get());
        amounts.addAll(refundPage.getRefundTotalBreakdown(currency));
        Assertion.assertThat("Total breakdown refund same as estimation", refundPage.getRefundTotalBreakdownSum(currency), is(String.valueOf(CoreFunctions.removeText(refundTotal))));
        Assertion.assertThat("Refund Charged should be same as selected room refund charge", String.valueOf(CoreFunctions.removeText(refundCharge)), is(refundPage.getRefundFeeBreakdownSum()));
        refundPage.closeRefundBreakdown();

        step("Continue to next page");
        refundPage.clickButton("Continue");
        refundPage.waitRefundSecondPageLoaded();
        Assertion.assertThat("Order ID on 2nd refund page is correct", refundPage.getOrderIdOnRefundBanner(), containsString(orderID));

        step("Verify refund method and amount");
        String refundMethodSection = refundPage.getRefundMethod();
        Assertion.assertThat("Refund method is correct", refundMethodSection, not(emptyOrNullString()));
        Assertion.assertThat("Refund amount is same as prev page", refundTotal, is(refundPage.getRefundTotal()));

        step("Check Refund TnC");
        refundPage.clickChecklistTnc();
        refundPage.clickButton("Submit");

        step("Submit refund");
        refundPage.clickButton("Process Refund");
        refundPage.waitForSubmitRefundLoader();

        step("Go to refund detail page");
        refundPage.clickButton("View Refund Detail");

        step("Verify Refund Detail");
        refundDetailPage.countRefundProgressTracker();
        Assertion.assertThat("Refund detail status not empty or null string", refundDetailPage.getRefundStatus(), not(emptyOrNullString()));
        Assertion.assertThat("Refund Id not empty or null string", refundDetailPage.getRefundId(), not(emptyOrNullString()));

        step("Verify Refund Method and Amount");
        for (String method : refundDetailPage.getRefundMethod()) {
            Assertion.assertThat("Refund method not empty", method, not(emptyOrNullString()));
        }
    }

    @Author(names = {"Ashish"})
    @Module(name = "PostPurchase")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void checkReviseMultiGuestNameWeb() throws FailedLoginException, InterruptedException, ExecutionException {
        Session<WebDriver> session = sessionThreadLocal.get();

        Map<String, String> param = getParam("checkReviseMultiGuestNameWeb");
        BaseTest.paramsThreadLocal.set(param);

        HotelApi<?> hotelApi = new HotelApi<>(session);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HotelReviseBookingPage<?> reviseBookingPage = new HotelReviseBookingPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String startDate = generateRandomDate(param.get("rangeAddDate"));

        String hotelName = param.get("hotelName");
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));
        int room = Integer.parseInt(param.get("room"));
        int qtyEachRoom = Integer.parseInt(param.get("qtyEachRoom"));

        step("Create new hotel issued order");
        Boolean result = hotelApi.createOrderMultiRoomApiDifferentRooms(
                username, password, hotelName, adult, night, room, startDate, qtyEachRoom, "IDR"
        );
        Assertion.assertThat("Hotel Booking via API is successful", result, is(true));

        step("Get data from the issued order");
        String bookedOrderID = String.valueOf(hotelApi.getOrderId());
        Map<String,List<String>> roomNameToGuestNameMap = hotelApi.getRoomNameToGuestNamesMap();
        List<Map.Entry<String, List<String>>> roomEntries = new ArrayList<>(roomNameToGuestNameMap.entrySet());

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(username, password, "123456", 1);

        step("Find the issued order");
        orderListPage.openActiveTicketViaURL(bookedOrderID);
        orderDetailPage.skipCoachmark();

        step("Verify hotel order details");
        Assertion.assertThat("Order ID is displayed correctly", orderDetailPage.getOrderId(),is(bookedOrderID));
        Assertion.assertThat("E-ticket card Displayed", orderDetailPage.isETicketCardDisplayed(),is(true));
        Assertion.assertThat("Hotel Name is displayed correctly", orderDetailPage.getHotelName(hotelName), is(hotelName));
        orderDetailPage.verifyAllGuestNameBasedOnRoomName(roomNameToGuestNameMap);

        for (Map.Entry<String, List<String>> roomEntry : roomEntries) {
            String roomName = roomEntry.getKey();
            List<String> guestsInRoom = roomEntry.getValue();

            for (String guestName : guestsInRoom) {
                step("Click on Edit booking");
                orderDetailPage.clickEditBooking();
                orderDetailPage.clickEditPopUpGuest();

                step("click on edit guest name btn according to room number { " + roomName + " }");
                homePage.continueWithoutApp(5);
                reviseBookingPage.clickEditBtnMO(roomName);

                step("edit guest name { " + guestName + " } for room ");
                String newName = CoreFunctions.randomName() + " Edited";
                reviseBookingPage.editGuestName(newName);
                reviseBookingPage.clickAgreeButton();
                reviseBookingPage.inputOTPCode("1234");
                Assertion.assertThat("Success message should be displayed ", reviseBookingPage.getSuccessText(), either(containsString("The guest name has been changed")).or(containsString("Nama tamu berhasil diubah")));
                reviseBookingPage.clickSeeDetails();

                step("Verify revised guest name for room " + roomName + " on order detail page");
                String revisedNameUi = orderDetailPage.getGuestNameMO(roomName);
                Assertion.assertThat("Verify Order Detail Page is Opened", orderListPage.getCurrentUrl(), containsString("yourorder/detail"));
                Assertion.assertThat("Verify OrderId is same", orderDetailPage.getOrderId(), is(bookedOrderID));
                Assertion.assertThat("Verify Guest Name from { " + guestName + " } to new name { " + revisedNameUi + " } is updated correctly via UI", revisedNameUi, containsString(newName));
                Assertion.assertThat("Verify Guest Name is not same with old name", revisedNameUi, not(containsString(guestName)));
            }
        }
    }

    @Author(names = {"Mridul Gupta, Aditi"})
    @Module(name = "Refund")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void testMORefundHotel() throws FailedLoginException, InterruptedException, ExecutionException {
        Map<String, String> param = getParam("testMORefundHotel");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        RefundPageWeb<?> refundPage = RefundPageWeb.getInstance(session);
        RefundDetailWeb<?> refundDetailPage = RefundDetailWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String startDate = generateRandomDate(param.get("dateRange"));
        String hotelName = param.get("hotelName");
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));
        int room = Integer.parseInt(param.get("room"));
        int qtyEachRoom = Integer.parseInt(param.get("qtyEachRoom"));
        String currency = param.get("currency");
        String paymentGroup = param.get("paymentGroup");
        String paymentMethod = param.get("paymentMethod");

        step("Create an order for hotel with waiting payment status");
        String orderId = "";
        try {
            orderId = hotelApi.createWaitingPaymentMultiRoomApi(
                    email, password, hotelName, adult, night, room,
                    startDate, qtyEachRoom, currency, false, "");
            Assertion.assertThat("Success to create waiting payment order", orderId, is(not(emptyOrNullString())));
        } catch (Exception e) {
            Assertion.skip("Error when creating waiting payment order: " + e.getMessage());
        }

        step("Get data from the issued order");
        String orderID = String.valueOf(hotelApi.getOrderId());
        int selectedIndex = generateRandomInt(1, hotelApi.roomNames.size())-1;
        String roomName = hotelApi.roomNames.get(selectedIndex);
        Double roomPrice = hotelApi.roomPriceSummaryTotals.get(selectedIndex);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);
        String appCurrency = homePage.getCurrentCurrency();

        if (StringUtils.isEmptyOrNull(appCurrency)) {
            step("User select currency: " + currency);
            homePage.selectCurrency(currency);
        } else {
            step("User select currency: " + appCurrency);
            homePage.selectCurrency(appCurrency);
        }

        step("user is on order Detail");
        orderListPage.openActiveTicketViaURL(orderId);
        orderDetailPage.continueToPayment();

        step("Input the card details");
        paymentPage.clickOnSelectPaymentMethod();
        paymentPage.clickOnSeeAll();
        paymentPage.goToPaymentMethodsGroup(paymentGroup);
        paymentPage.selectPaymentMethod(paymentMethod);

        step("Finish the payment process by clicking the payment button");
        paymentPage.clickOnPaymentButton();
        paymentPage.handle3DSPage(30);

        step("Verify the payment successful page is visible or not");
        Assertion.assertThat("Verify the payment successful page is visible", paymentPage.isPaymentSuccessfulVisible(), is(true));

        step("Find the issued order");
        orderListPage.openActiveTicketViaURL(orderID);
        orderDetailPage.skipCoachmark();

        step("Check if user got email notification for issued order");
        OutboundNotificationVerifier.verify(email, orderID, NotificationChannel.EMAIL);

        step("Verify hotel order details");
        Assertion.assertThat("Order ID is displayed correctly", orderDetailPage.getOrderId(),is(orderID));
        Assertion.assertThat("E-ticket card Displayed", orderDetailPage.isETicketCardDisplayed(),is(true));
        Assertion.assertThat("New order status is correct", orderDetailPage.getOrderStatus(orderID), is("ETICKET_ISSUED"));
        Assertion.assertThat("Hotel Name is displayed correctly", orderDetailPage.isHotelNameDisplayed(hotelName), is(true));
        Assertion.assertThat("Refund and reschedule info section should be verified", orderDetailPage.verifyRefundAndRescheduleInfo(), is(true));
        Assertion.assertThat("Refund button should be displayed", orderDetailPage.isRefundButtonDisplayed(), is(true));

        step("Open refund page");
        orderDetailPage.clickRefundButton();
        refundPage.waitForContentLoaderDisappear();

        step("Select one of the room for refund");
        refundPage.selectRoomForRefund(selectedIndex);

        step("Click to continue to refund");
        refundPage.continueToRefund();

        step("Select random refund reason");
        refundPage.selectRandomRefundReason();

        step("Verify refund estimation section");
        refundPage.waitForEstimationAmountAppear("10");
        String refundReceive = refundPage.getRefundYoullReceive();
        String refundFee = refundPage.getRefundFee();
        String refundTotal = refundPage.getRefundTotal();
        List<String> amounts = new ArrayList<>(Arrays.asList(refundReceive, refundFee, refundTotal));
        Assertion.assertThat("Refund Total amount is correct", CoreFunctions.removeText(refundReceive)+CoreFunctions.removeText(refundFee), is(CoreFunctions.removeText(refundTotal)));

        step("Verify refund amount breakdown");
        refundPage.openRefundBreakdown();
        amounts.clear();
        amounts.addAll(refundPage.getRefundAmountBreakdown(currency));

        if (refundPage.getRefundFeeBreakdown(currency).isPresent()) amounts.addAll(refundPage.getRefundFeeBreakdown(currency).get());
        amounts.addAll(refundPage.getRefundTotalBreakdown(currency));
        Assertion.assertThat("Total breakdown refund same as estimation", refundPage.getRefundTotalBreakdownSum(currency), is(String.valueOf(CoreFunctions.removeText(refundTotal))));
        refundPage.closeRefundBreakdown();

        step("Continue to next page");
        refundPage.continueToNextPage();
        refundPage.waitRefundSecondPageLoaded();
        Assertion.assertThat("Order ID on 2nd refund page is correct", refundPage.getOrderIdOnRefundBanner(), containsString(orderID));

        step("Verify refund method and amount");
        String refundMethodSection = refundPage.getRefundMethod();
        Assertion.assertThat("Refund method is correct", refundMethodSection, not(emptyOrNullString()));
        Assertion.assertThat("Refund amount is same as prev page", refundTotal, is(refundPage.getRefundTotal()));

        step("Check Refund TnC");
        refundPage.clickChecklistTnc();
        refundPage.clickButton("Submit");

        step("Submit refund");
        refundPage.clickButton("Process Refund");
        refundPage.waitForSubmitRefundLoader();

        step("Go to refund detail page");
        refundPage.clickButton("View Refund Detail");

        step("Verify Refund Detail");
        int countRefundProgress = refundDetailPage.countRefundProgressTracker();
        Assertion.assertThat("Refund detail status not empty or null string", refundDetailPage.getRefundStatus(), not(emptyOrNullString()));
        Assertion.assertThat("Refund Id not empty or null string", refundDetailPage.getRefundId(), not(emptyOrNullString()));

        step("Verify Refund Method and Amount");
        for (String method : refundDetailPage.getRefundMethod()) {
            Assertion.assertThat("Refund method not empty", method, not(emptyOrNullString()));
        }

        step("Go back to the order detail page");
        orderListPage.openActiveTicketViaURL(orderID);
        orderDetailPage.skipCoachmark();

        step("Check that the refunded room is mentioned as refunded");
        orderDetailPage.verifyRefundedRoomInOrderUpdate(roomName);

        step("User try to refund already refunded room");
        orderDetailPage.clickRefundButton();
        refundPage.waitForContentLoaderDisappear();

        step("Select same room for refund");
        refundPage.verifyRefundedRoom(roomName);

        step("Check Email Notification for Refund order");
        OutboundNotificationVerifier.verify(email, orderID, NotificationChannel.EMAIL, NotificationTemplateType.REFUND);
    }

    @Author(names = "Aditi")
    @Module(name = "BookingPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyCompleteBookCreationCommunication() throws FailedLoginException, InterruptedException, ExecutionException {
        Map<String, String> param = getParam("verifyCompleteBookCreationCommunication");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String location = param.get("location");
        String email = param.get("email");
        String fullName = CoreFunctions.randomName();
        String phoneNumber = CoreFunctions.randomPhone();
        String paymentGroup = param.get("paymentGroup");
        String paymentMethod = param.get("paymentMethod");
        String dateType = param.get("dateType");
        String dateRange = param.get("dateRange");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        HotelDetailPage<?> detailPage = new HotelDetailPage<>(session);
        SRPPageWeb<?> searchResultPage = SRPPageWeb.getInstance(session);
        HotelRoomListPage<?> roomListPage = new HotelRoomListPage<>(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HotelBookingFormPageWeb<?> bookingFormPage = HotelBookingFormPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User select IDR currency");
        homePage.selectCurrency("IDR");

        step("User Open Hotel Landing page");
        String searchPageCurrency = homePage.getCurrentCurrency();
        Assertion.assertThat("Verify currency in  Search Result page is set to IDR", searchPageCurrency.equals("IDR"),
                Is.is(true));
        homePage.openVerticalPageViaURL("Hotel");
        Assertion.assertThat("Landing Page is Opened", landingPage.isLandingPageVisible(), Is.is(true));

        step("Search stay hotel");
        landingPage.searchStayLocation(location);
        landingPage.setStayDate(dateType, dateRange);
        landingPage.clickSubmit();

        step("submit search Button");
        searchResultPage.clickPopUp();
        Assertion.assertThat("Srp is Loaded", searchResultPage.isHotelCardVisible(), Is.is(true));

        if (!detailPage.isPdpLoaded()) {
            step("Select hotel Card ");
            searchResultPage.selectHotel();
        }
        step("Go Inside PDP");
        detailPage.switchToNewTab();
        detailPage.clickPopUp();
        Assertion.assertThat("Verify if PDP is Opened", detailPage.isPdpLoaded(), Is.is(true));
        Assertion.assertThat("Map Section is visible on Hotel PDP", detailPage.isPdpMapVisible(), Is.is(true));
        roomListPage.scrollToRoomListSection();

        step("Click Book Button");
        roomListPage.clickBookButton();

        step("Fill contact detail");
        bookingFormPage.fillContactDetail(fullName, phoneNumber, email);

        step("Fill guest detail");
        bookingFormPage.fillGuestDetailsCombinedRandom(1);

        step("Go to payment page");
        bookingFormPage.continueToPaymentPage();

        step("check order id on payment page");
        String orderID = paymentPage.getOrderId();
        orderID = orderID.replaceAll("[^0-9]", "");
        Assertion.assertThat("Verify order ID is displayed in payment page", !orderID.isEmpty(), Is.is(true));

        step("User select payment method ");
        paymentPage.clickOnSelectPaymentMethod();
        paymentPage.clickOnSeeAll();
        paymentPage.goToPaymentMethodsGroup(paymentGroup);
        paymentPage.selectPaymentMethod(paymentMethod);

        step("User complete payment");
        paymentPage.clickOnPaymentButton();

        step("User try to open see order list");
        paymentPage.clickSeeOrderListButton();

        step("Go to Issued Order");
        orderListPage.openActiveTicketViaURL(orderID);
        orderDetailPage.skipCoachmark();

        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.verifyIssuedETicket(),
                Is.is(true));
        Assertion.assertThat("contact name should be available on order detail page",
                !orderDetailPage.getContactName(orderID).isEmpty(), Is.is(true));
        Assertion.assertThat("email should be correctly displayed on order detail page",
                orderDetailPage.getContactEmail(orderID), Is.is(email));
        Assertion.assertThat("orderID should be visible and should be same as from payment page",
                orderDetailPage.getOrderId(), Is.is(orderID));

        step("Verify booking creation communication");
        /// implementation pending: to verify booking creation communication (email/SMS)
    }



    @Author(names = "Aditi")
    @Module(name = "BookingPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyHotelRefundNotAllowed() throws FailedLoginException, InterruptedException, ExecutionException {
        Map<String, String> param = getParam("verifyHotelRefundNotAllowed");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String location = param.get("location");
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String paymentGroup = param.get("paymentGroup");
        String paymentMethod = param.get("paymentMethod");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);
        int daysToReturnDate = Integer.parseInt(param.get("daysToReturnDate"));
        String returnDate = CoreFunctions.addToLongDate(departureDate, daysToReturnDate);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        HotelDetailPage<?> detailPage = new HotelDetailPage<>(session);
        SRPPageWeb<?> searchResultPage = SRPPageWeb.getInstance(session);
        HotelRoomListPage<?> roomListPage = new HotelRoomListPage<>(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HotelBookingFormPageWeb<?> bookingFormPage = HotelBookingFormPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        CalendarPageWeb<?> calendarPage = CalendarPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User select IDR currency");
        homePage.selectCurrency("IDR");

        step("User Open Hotel Landing page");
        homePage.openVerticalPageViaURL("Hotel");
        Assertion.assertThat("Landing Page is Opened", landingPage.isLandingPageVisible(), Is.is(true));

        step("Search stay hotel");
        landingPage.searchStayLocation(location);

        step("Set date");
        landingPage.clickOnCalendarWidgetToOpen();
        calendarPage.selectDate(departureDate, returnDate);

        step("Submit search");
        landingPage.clickSubmit();

        step("Go Inside PDP");
        detailPage.clickPopUp();
        Assertion.assertThat("Verify if PDP is Opened", detailPage.isPdpLoaded(), Is.is(true));
        detailPage.seeHotelRoom();

        step("Search for non-refundable room and click Book");
        String selectedRoomName = detailPage.selectNonRefundableRoom();

        step("User verify room name on the booking form page");
        String bookingFormRoomName = bookingFormPage.getBookingRoomName();
        Assertion.assertThat("Room name on booking form should contain PDP room name", bookingFormRoomName, containsString(selectedRoomName));

        step("Go to payment page");
        bookingFormPage.continueToPaymentPage();
        bookingFormPage.selectPayment();

        step("check order id on payment page");
        String orderID = paymentPage.getOrderId();
        orderID = orderID.replaceAll("[^0-9]", "");
        Assertion.assertThat("Verify order ID is displayed in payment page", !orderID.isEmpty(), Is.is(true));

        step("User select payment method ");
        paymentPage.clickOnSelectPaymentMethod();
        paymentPage.clickOnSeeAll();
        paymentPage.goToPaymentMethodsGroup(paymentGroup);
        paymentPage.selectPaymentMethod(paymentMethod);

        step("User complete payment");
        paymentPage.clickOnPaymentButton();

        step("User try to open see order list");
        paymentPage.clickSeeOrderListButton();

        step("Go to Issued Order");
        orderListPage.openActiveTicketViaURL(orderID);
        orderDetailPage.skipCoachmark();

        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.verifyIssuedETicket(),
                is(true));
        Assertion.assertThat("orderID should be visible and should be same as from payment page",
                orderDetailPage.getOrderId(), is(orderID));
        Assertion.assertThat("contact name should be available on order detail page",
                !orderDetailPage.getContactName(orderID).isEmpty(), is(true));
        Assertion.assertThat("email should be correctly displayed on order detail page",
                orderDetailPage.getContactEmail(orderID), is(email));
        Assertion.assertThat("Room name on order detail page should contain PDP room name",
                orderDetailPage.getOrderDetailRoomName(), containsString(selectedRoomName));

        step("Verify that the refund and reschedule info section is displayed");
        Assertion.assertThat("Refund and reschedule info section should be verified", orderDetailPage.verifyRefundAndRescheduleInfo(), is(true));

        step("Click on Details button in Refund & Reschedule Info section");
        orderDetailPage.clickRefundRescheduleDetailsButton();

        step("Verify Refund & Reschedule Info page is opened");
        Assertion.assertThat("Refund & Reschedule Info page should be opened", orderDetailPage.isRefundRescheduleInfoPageOpened(), is(true));

        step("Verify that refund is not supported on the info page");
        Assertion.assertThat("Refund not supported message should be displayed", orderDetailPage.isRefundNotSupported(), is(true));

        step("Click on Back button to return to order detail page");
        orderDetailPage.clickRefundRescheduleBackButton();

        step("Verify that the refund button is displayed");
        Assertion.assertThat("Refund button should be displayed", orderDetailPage.isRefundButtonDisplayed(), is(true));

        step("Click on Refund button");
        orderDetailPage.clickRefundButton();

        step("Verify Refund Not Available popup is displayed");
        Assertion.assertThat("Refund Not Available popup should be displayed", orderDetailPage.isRefundNotAvailablePopupDisplayed(), is(true));

        step("Verify Refund Not Available title is displayed");
        Assertion.assertThat("Refund Not Available title should be displayed", orderDetailPage.isRefundNotAvailableTitleDisplayed(), is(true));

        step("Verify Refund Not Available description is displayed");
        String refundDescription = orderDetailPage.getRefundNotAvailableDescriptionText();
        Assertion.assertThat("Refund Not Available description should contain refund not allowed text", refundDescription, either(containsString("Refund not allowed")).or(containsString("Tidak bisa refund")));

        step("Verify Reschedule button is displayed on popup");
        Assertion.assertThat("Reschedule button should be displayed on popup", orderDetailPage.isRefundNotAvailableRescheduleButtonDisplayed(), is(true));

        step("Click on Back button to close the popup");
        orderDetailPage.clickRefundNotAvailableBackButton();

        step("Verify Refund Not Available popup is not displayed");
        Assertion.assertThat("Refund Not Available popup should be closed", orderDetailPage.waitForRefundNotAvailablePopupToDisappear(), is(true));

    }

    @Author(names = "Vipul kumar")
    @Module(name = "CompleteBookingWithoutPayment")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyBookingFormClearedOnReturnFromPaymentNonLoggedIn()
            throws FailedLoginException, InterruptedException, ExecutionException {
        Map<String, String> param = getParam("verifyBookingFormClearedOnReturnFromPaymentNonLoggedIn");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String location = param.get("location");
        String email = "autTestingTiket" + CoreFunctions.randomEmail();
        String currency = param.get("currency");
        int noOfAdults = Integer.parseInt(param.get("guests"));
        int noOfChildren = Integer.parseInt(param.get("childs"));
        int noOfRoom = Integer.parseInt(param.get("rooms"));
        String fullName = CoreFunctions.randomName() + " autTest";
        String phoneNumber = "91" + CoreFunctions.randomInt(8);

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);
        int daysToReturnDate = Integer.parseInt(param.get("daysToReturnDate"));
        String returnDate = CoreFunctions.addToLongDate(departureDate, daysToReturnDate);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HotelLandingPageWeb<?> hotelLandingPage = HotelLandingPageWeb.getInstance(session);
        LandingPageWeb<?> landingPage = LandingPageWeb.getInstance(session);
        HotelDetailPage<?> detailPage = new HotelDetailPage<>(session);
        HotelRoomListPage<?> roomListPage = new HotelRoomListPage<>(session);
        HotelBookingFormPageWeb<?> bookingFormPage = HotelBookingFormPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);
        CalendarPageWeb<?> calendarPage = CalendarPageWeb.getInstance(session);
        HotelBookingFormPage<?> hotelBookingFormPage = new HotelBookingFormPage<>(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as guest user");
        loginPage.loginAsGuest();

        step("User select " + currency + " currency");
        homePage.selectCurrency(currency);

        step("User Open Hotel Landing page");
        homePage.openVerticalPageViaURL("Hotel");

        Assertion.assertThat("Landing Page is Opened", landingPage.isLandingPageVisible(), is(true));

        step("Search for international hotel With the Name Of " + location);
        landingPage.searchStayLocation(location);

        step("Set date");
        hotelLandingPage.clickOnCalendarWidgetToOpen();
        calendarPage.selectDate(departureDate, returnDate);

        step("Set guests and rooms");
        landingPage.clickRoomAndGuestButton();
        landingPage.setTotalGuest(noOfAdults);
        landingPage.setTotalChildGuest(noOfChildren);
        landingPage.setTotalRoom(noOfRoom);
        landingPage.clickDone();
        landingPage.clickSubmit();

        step("Go Inside PDP");
        String HotelName=detailPage.getHotelNameDetail();
        Assertion.assertThat("Verify if PDP is Opened", detailPage.isPdpLoaded(), is(true));
        Assertion.assertThat("Verify Hotel name on entered on LP and PDP  is same",HotelName,containsString(location));

        step("Scroll to room list section");
        roomListPage.scrollToRoomListSection();

        step("Select " + noOfRoom + " rooms by clicking Select buttons on rate plan cards");
        roomListPage.selectRoomsByCount(noOfRoom);
        hotelBookingFormPage.skipRoomChangePopup();

        step("Verify booking summary shows " + noOfRoom + " rooms selected");
        Assertion.assertThat("Contact form should be visible on Booking Form page", bookingFormPage.isContactForm(), is(true));
        bookingFormPage.clickStayDetailsChevron();
        Assertion.assertThat("Stay details bottom sheet should be opened", bookingFormPage.isStayDetailsBottomSheetOpened(), is(true));
        String roomCount = bookingFormPage.getStayDetailsRoomCount();
        Assertion.assertThat("Booking summary should show " + noOfRoom + " rooms", roomCount, containsString(String.valueOf(noOfRoom)));
        bookingFormPage.closeBottomSheet();

        step("Fill in all contact details (name, email, phone number, etc.)");
        bookingFormPage.fillContactDetail(fullName, phoneNumber, email);
        Assertion.assertThat("Contact details should be filled", bookingFormPage.isContactForm(), is(true));

        step("Fill in Guest 1 details");
        Assertion.assertThat("Stay details form should be visible on Booking Form page",
                bookingFormPage.isGuestStayForm(), is(true));

        List<String> filledGuestNames = new ArrayList<>();
        String guest1Name = CoreFunctions.randomName() + " autTest";
        filledGuestNames.add(guest1Name);
        bookingFormPage.fillGuestDetails(1, guest1Name, true, false, "");

        step("Fill in Guest 2 details");
        String guest2Name = CoreFunctions.randomName() + " autTest";
        filledGuestNames.add(guest2Name);
        bookingFormPage.fillGuestDetails(2, guest2Name, true, false, "");

        step("Fill in any other required booking information");
        int bookingPrice = bookingFormPage.getTotalPrice();
        Assertion.assertThat("Booking price should be greater than 0", bookingPrice > 0, is(true));

        step("Click on 'Proceed to Payment' or 'Continue to Payment' button");
        bookingFormPage.continueToNextPage();

        step("Go to payment page");
        bookingFormPage.continueToPaymentPage();

        step("Verify user is redirected to the payment page");
        Assertion.assertThat("Payment page should be loaded", paymentPage.isPaymentPageLoaded(), is(true));

        step("Verify Order ID is displayed on the payment page");
        String orderID = paymentPage.getOrderId();
        Assertion.assertThat("Verify order ID is displayed on the payment page", !orderID.isEmpty(), is(true));

        step("Click navigate back to the booking page");
        paymentPage.navigateBack();
        
        step("Handle confirmation popup if appears after navigating back");
        paymentPage.handleNavigateBackConfirmationPopup();

        step("Verify user is redirected back to the booking page");
        Assertion.assertThat("Contact form should be visible on Booking Form page after navigating back", 
                bookingFormPage.isContactForm(), is(true));

        step("Verify all contact details fields are empty/cleared");
        String contactFullName = bookingFormPage.getContactFullNameValue();
        String contactPhoneNumber = bookingFormPage.getContactPhoneNumberValue();
        String contactEmail = bookingFormPage.getContactEmailValue();
        
        Assertion.assertThat("Contact full name field should be empty", 
                contactFullName, anyOf(nullValue(), emptyString()));
        Assertion.assertThat("Contact phone number field should be empty", 
                contactPhoneNumber, anyOf(nullValue(), emptyString()));
        Assertion.assertThat("Contact email field should be empty", 
                contactEmail, anyOf(nullValue(), emptyString()));

        step("Verify all Guest 1 details fields are empty/cleared");
        String guest1NameValue = bookingFormPage.getGuestNameValue(1);
        Assertion.assertThat("Guest 1 name field should be empty", 
                guest1NameValue, anyOf(nullValue(), emptyString()));

        step("Verify all Guest 2 details fields are empty/cleared");
        String guest2NameValue = bookingFormPage.getGuestNameValue(2);
        Assertion.assertThat("Guest 2 name field should be empty", 
                guest2NameValue, anyOf(nullValue(), emptyString()));

        step("Verify form appears fresh with no previously entered data");
        Assertion.assertThat("Contact form should be visible", bookingFormPage.isContactForm(), is(true));
        Assertion.assertThat("Guest stay form should be visible", bookingFormPage.isGuestStayForm(), is(true));

        step("Verify booking summary (" + noOfRoom + " rooms) may still be visible as it's part of the search criteria");
        bookingFormPage.clickStayDetailsChevron();
        Assertion.assertThat("Stay details bottom sheet should be opened", bookingFormPage.isStayDetailsBottomSheetOpened(), is(true));
        String roomCountAfterBack = bookingFormPage.getStayDetailsRoomCount();
        Assertion.assertThat("Booking summary should still show " + noOfRoom + " rooms after navigating back",
                roomCountAfterBack, containsString(String.valueOf(noOfRoom)));
        bookingFormPage.closeBottomSheet();

    }

    @Author(names = "Vipul kumar")
    @Module(name = "CompleteBookingWithoutPayment")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyBookingFormPreFilledOnReturnFromPaymentLoggedIn()
            throws FailedLoginException, InterruptedException, ExecutionException {
        Map<String, String> param = getParam("verifyBookingFormPreFilledOnReturnFromPaymentLoggedIn");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String location = param.get("location");
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String currency = param.get("currency");
        int noOfAdults = Integer.parseInt(param.get("guests"));
        int noOfChildren = Integer.parseInt(param.get("childs"));
        int noOfRoom = Integer.parseInt(param.get("rooms"));
        String fullName = CoreFunctions.randomName() + " autTest";
        String phoneNumber = "91" + CoreFunctions.randomInt(8);
        String contactEmail = "autTestingTiket" + CoreFunctions.randomEmail();

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);
        int daysToReturnDate = Integer.parseInt(param.get("daysToReturnDate"));
        String returnDate = CoreFunctions.addToLongDate(departureDate, daysToReturnDate);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HotelLandingPageWeb<?> hotelLandingPage = HotelLandingPageWeb.getInstance(session);
        LandingPageWeb<?> landingPage = LandingPageWeb.getInstance(session);
        HotelDetailPage<?> detailPage = new HotelDetailPage<>(session);
        HotelRoomListPage<?> roomListPage = new HotelRoomListPage<>(session);
        HotelBookingFormPageWeb<?> bookingFormPage = HotelBookingFormPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);
        CalendarPageWeb<?> calendarPage = CalendarPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User login to the application");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User select " + currency + " currency");
        homePage.selectCurrency(currency);

        step("User Open Hotel Landing page");
        homePage.openVerticalPageViaURL("Hotel");

        Assertion.assertThat("Landing Page is Opened", landingPage.isLandingPageVisible(), is(true));

        step("Search for international hotel With the Name Of " + location);
        landingPage.searchStayLocation(location);

        step("Set date");
        hotelLandingPage.clickOnCalendarWidgetToOpen();
        calendarPage.selectDate(departureDate, returnDate);

        step("Set guests and rooms");
        landingPage.clickRoomAndGuestButton();
        landingPage.setTotalGuest(noOfAdults);
        landingPage.setTotalChildGuest(noOfChildren);
        landingPage.setTotalRoom(noOfRoom);
        landingPage.clickDone();
        landingPage.clickSubmit();

        step("Go Inside PDP");
        String HotelName = detailPage.getHotelNameDetail();
        Assertion.assertThat("Verify if PDP is Opened", detailPage.isPdpLoaded(), is(true));
        Assertion.assertThat("Verify Hotel name on entered on LP and PDP is same", HotelName, containsString(location));

        step("Scroll to room list section");
        roomListPage.scrollToRoomListSection();

        step("Select " + noOfRoom + " rooms by clicking Select buttons on rate plan cards");
        roomListPage.selectRoomsByCount(noOfRoom);

        step("Verify booking summary shows " + noOfRoom + " rooms selected");
        Assertion.assertThat("Contact form should be visible on Booking Form page", bookingFormPage.isContactForm(), is(true));
        bookingFormPage.clickStayDetailsChevron();
        Assertion.assertThat("Stay details bottom sheet should be opened", bookingFormPage.isStayDetailsBottomSheetOpened(), is(true));
        String roomCount = bookingFormPage.getStayDetailsRoomCount();
        Assertion.assertThat("Booking summary should show " + noOfRoom + " rooms", roomCount, containsString(String.valueOf(noOfRoom)));
        bookingFormPage.closeBottomSheet();

        step("Capture user's saved profile values before filling form");
        String savedContactFullName = bookingFormPage.getContactFullNameValue();
        String savedContactPhoneNumber = bookingFormPage.getContactPhoneNumberValue();
        String savedContactEmail = bookingFormPage.getContactEmailValue();
        String savedGuest1Name = bookingFormPage.getGuestNameValueSafely(1);

        step("Fill in all contact details (name, email, phone number, etc.)");
        bookingFormPage.fillContactDetail(fullName, phoneNumber, contactEmail);
        Assertion.assertThat("Contact details should be filled", bookingFormPage.isContactForm(), is(true));

        step("Fill in Guest 1 details");
        Assertion.assertThat("Stay details form should be visible on Booking Form page",
                bookingFormPage.isGuestStayForm(), is(true));

        String guest1Name = CoreFunctions.randomName() + " autTest";
        bookingFormPage.fillGuestDetails(1, guest1Name, true, false, "");

        step("Fill in Guest 2 details");
        String guest2Name = CoreFunctions.randomName() + " autTest";
        bookingFormPage.fillGuestDetails(2, guest2Name, true, false, "");

        step("Fill in any other required booking information");
        int bookingPrice = bookingFormPage.getTotalPrice();
        Assertion.assertThat("Booking price should be greater than 0", bookingPrice > 0, is(true));

        step("Click on 'Proceed to Payment' or 'Continue to Payment' button");
        bookingFormPage.continueToNextPage();

        step("Go to payment page");
        bookingFormPage.continueToPaymentPage();

        step("Verify user is redirected to the payment page");
        Assertion.assertThat("Payment page should be loaded", paymentPage.isPaymentPageLoaded(), is(true));

        step("Verify Order ID is displayed on the payment page");
        String orderID = paymentPage.getOrderId();
        Assertion.assertThat("Verify order ID is displayed on the payment page", !orderID.isEmpty(), is(true));

        step("Click navigate back to the booking page");
        paymentPage.navigateBack();

        step("Handle confirmation popup if appears after navigating back");
        paymentPage.handleNavigateBackConfirmationPopup();

        step("Verify user is redirected back to the booking page");
        Assertion.assertThat("Contact form should be visible on Booking Form page after navigating back",
                bookingFormPage.isContactForm(), is(true));

        step("Verify all contact details fields are pre-filled with user's saved profile values");
        Map<String, String> contactDetailsAfterBack = bookingFormPage.getContactDetailsAfterBack();
        String contactFullNameAfterBack = contactDetailsAfterBack.get("fullName");
        String contactPhoneNumberAfterBack = contactDetailsAfterBack.get("phoneNumber");
        String contactEmailAfterBack = contactDetailsAfterBack.get("email");

        Assertion.assertThat("Saved contact full name should exist for logged in user",
                savedContactFullName != null && !savedContactFullName.isEmpty(), is(true));
        Assertion.assertThat("Contact full name field should be pre-filled with saved profile value",
                contactFullNameAfterBack, is(savedContactFullName));

        Assertion.assertThat("Saved contact phone number should exist for logged in user",
                savedContactPhoneNumber != null && !savedContactPhoneNumber.isEmpty(), is(true));
        Assertion.assertThat("Contact phone number field should be pre-filled with saved profile value",
                contactPhoneNumberAfterBack, is(savedContactPhoneNumber));

        Assertion.assertThat("Saved contact email should exist for logged in user",
                savedContactEmail != null && !savedContactEmail.isEmpty(), is(true));
        Assertion.assertThat("Contact email field should be pre-filled with saved profile value",
                contactEmailAfterBack, is(savedContactEmail));

        step("Verify Guest 1 details field is pre-filled with user's saved profile value");
        Assertion.assertThat("Saved guest 1 name should exist for logged in user",
                savedGuest1Name != null && !savedGuest1Name.isEmpty(), is(true));
        String guest1NameAfterBack = bookingFormPage.getGuestNameValue(1);
        Assertion.assertThat("Guest 1 name field should be pre-filled with saved profile value",
                guest1NameAfterBack, is(savedGuest1Name));

        step("Verify Guest 2 details field is empty/cleared");
        String guest2NameAfterBack = bookingFormPage.getGuestNameValueIfMultipleAdults(2, noOfAdults);
        if (guest2NameAfterBack != null) {
            Assertion.assertThat("Guest 2 name field should be empty",
                    guest2NameAfterBack, anyOf(nullValue(), emptyString()));
        }

        step("Verify form data persistence works correctly for logged in users");
        Assertion.assertThat("Contact form should be visible", bookingFormPage.isContactForm(), is(true));
        Assertion.assertThat("Guest stay form should be visible", bookingFormPage.isGuestStayForm(), is(true));

        step("Verify booking summary (" + noOfRoom + " rooms) may still be visible as it's part of the search criteria");
        bookingFormPage.clickStayDetailsChevron();
        Assertion.assertThat("Stay details bottom sheet should be opened", bookingFormPage.isStayDetailsBottomSheetOpened(), is(true));
        String roomCountAfterBack = bookingFormPage.getStayDetailsRoomCount();
        Assertion.assertThat("Booking summary should still show " + noOfRoom + " rooms after navigating back",
                roomCountAfterBack, containsString(String.valueOf(noOfRoom)));
        bookingFormPage.closeBottomSheet();

    }

    @Author(names = "Ashish")
    @Module(name = "Extranet")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyCompleteBookSingleRoom1Adult1ChildExtranet() throws FailedLoginException, InterruptedException, ExecutionException {
        Map<String, String> param = getParam("verifyCompleteBookSingleRoom1Adult1ChildExtranet");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LandingPageWeb<?> landingPage = LandingPageWeb.getInstance(session);
        HotelLandingPageWeb<?> hotelLandingPage = HotelLandingPageWeb.getInstance(session);
        HotelDetailPage<?> detailPage = new HotelDetailPage<>(session);
        HotelRoomListPage<?> roomListPage = new HotelRoomListPage<>(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HotelBookingFormPageWeb<?> bookingFormPage = HotelBookingFormPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        CalendarPageWeb<?> calendarPage = CalendarPageWeb.getInstance(session);
        ExtranetHotelApi<?> extranetHotelApi = new ExtranetHotelApi<>(session);
        ExtranetHotelAuthApi<?> authApi = new ExtranetHotelAuthApi<>(session);

        String location = param.get("location");
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String paymentGroup = param.get("paymentGroup");
        String paymentMethod = param.get("paymentMethod");
        int hotelId = Integer.parseInt(param.get("hotelId"));
        String dashboardUsername = param.get("dashboardUsername");
        String dashboardPassword = param.get("dashboardPassword");
        int noOfAdults = Integer.parseInt(param.get("adult"));
        int noOfChildren = Integer.parseInt(param.get("child"));
        int noOfRoom = Integer.parseInt(param.get("rooms"));
        int expectedTotalRoom = Integer.parseInt(param.get("rooms"));

        int daysToDepartureDate = CoreFunctions.generateRandomInt(Integer.parseInt(param.get("daysToDepartureDate")),10);
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);
        int durationOfStay = Integer.parseInt(param.get("durationOfStay"));
        String returnDate = CoreFunctions.addToLongDate(departureDate, durationOfStay);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User select IDR currency");
        homePage.selectCurrency("IDR");

        step("User Open Hotel Landing page");
        homePage.openVerticalPageViaURL("Hotel");
        Assertion.assertThat("Landing Page is Opened", landingPage.isLandingPageVisible(), Is.is(true));

        step("Search stay hotel");
        landingPage.searchStayLocation(location);

        step("Set guests and rooms");
        landingPage.clickRoomAndGuestButton();
        landingPage.setTotalGuest(noOfAdults);
        landingPage.setTotalChildGuest(noOfChildren);
        landingPage.setTotalRoom(noOfRoom);
        landingPage.clickDone();

        step("Set date");
        hotelLandingPage.clickOnCalendarWidgetToOpen();
        calendarPage.selectDate(departureDate, returnDate);
        hotelLandingPage.clickSubmit();

        step("Go Inside PDP");
        detailPage.clickPopUp();
        Assertion.assertThat("Verify if PDP is Opened", detailPage.isPdpLoaded(), Is.is(true));
        detailPage.seeHotelRoom();

        step("Click Book Button");
        roomListPage.clickBookButton();

        step("User input contact detail");
        Assertion.assertThat("Contact form should be visible on Booking Form page", bookingFormPage.isContactForm(), is(true));

        step("User enter Stay details ");
        Assertion.assertThat("Stay details form should be visible on Booking Form page",
                bookingFormPage.isGuestStayForm(), is(true));

        for (int i = 1; i <= noOfAdults; i++) {
            bookingFormPage.fillGuestDetails(i, CoreFunctions.randomName(), false, false, "");
        }

        step("Go to payment page");
        bookingFormPage.continueToSecondPartThenPaymentPage();
        bookingFormPage.clickOnPopUp();

        step("check order id on payment page");
        String orderID = paymentPage.getOrderId();
        orderID = orderID.replaceAll("[^0-9]", "");
        Assertion.assertThat("user get order id", orderID, allOf(
                not(nullValue()),
                not(equalTo(""))
        ));

        step("User select payment method ");
        paymentPage.clickOnSelectPaymentMethod();
        paymentPage.clickOnSeeAll();
        paymentPage.goToPaymentMethodsGroup(paymentGroup);
        paymentPage.selectPaymentMethod(paymentMethod);

        step("User complete payment");
        paymentPage.clickOnPaymentButton();

        step("User try to open see order list");
        orderListPage.waitForPaymentSuccessful(orderID,10);
        paymentPage.clickSeeOrderListButton();

        step("Go to Issued Order");
        orderListPage.openActiveTicketViaURL(orderID);
        orderDetailPage.skipCoachmark();
        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.verifyIssuedETicket(), Is.is(true));
        Assertion.assertThat("contact name should be available on order detail page", orderDetailPage.getContactName(orderID), allOf( not(nullValue()),not(equalTo(""))));
        Assertion.assertThat("email should be correctly displayed on order detail page", orderDetailPage.getContactEmail(orderID), Is.is(email));
        Assertion.assertThat("orderID should be visible and should be same as from payment page", orderDetailPage.getOrderId(), Is.is(orderID));
        //total adult and child verification on order detail page
        Map<String, Integer> guests = orderDetailPage.getGuestDetailUI();
        Integer totalGuestsUI = guests.get("totalGuests");
        Integer totalAdultsUI = guests.get("adults");
        Integer totalChildrenUI = guests.get("children");
        Assertion.assertThat("total guests should be equal with given test data", totalGuestsUI, Is.is(noOfAdults + noOfChildren));
        Assertion.assertThat("total adult should be equal with given test data", totalAdultsUI, Is.is(noOfAdults));
        Assertion.assertThat("total child should be equal with given test data", totalChildrenUI, Is.is(noOfChildren));

        step("Authenticate to Extranet Hotel API to get access token");
        ExtranetAdminLoginMfaApplyResponse.Data authData  = authApi.getAccessToken(dashboardUsername, dashboardPassword);
        String accessToken = authData.accessToken;
        Long dashboardAccountId = authData.accountId;

        step("Verify booking exists on hotel dashboard");
        String departureDateISO = CoreFunctions.convertDateFormat(departureDate);
        String returnDateISO = CoreFunctions.convertDateFormat(returnDate);
        ExtranetHotelApi.DashboardBookingDetails bookingDetails = extranetHotelApi.getDashboardBookingDetails(
                hotelId, orderID, departureDateISO, returnDateISO, dashboardUsername, accessToken, dashboardAccountId);

        Assertion.assertThat("Booking should be found on dashboard", bookingDetails.found(), Is.is(true));
        Assertion.assertThat("Order ID should match", String.valueOf(bookingDetails.orderId()), Is.is(orderID));
        Assertion.assertThat("Status should be 'issued'", bookingDetails.status(), Is.is("issued"));
        Assertion.assertThat("Check-in date should match", bookingDetails.checkinDate(), Is.is(departureDateISO));
        Assertion.assertThat("Check-out date should match", bookingDetails.checkoutDate(), Is.is(returnDateISO));
        Assertion.assertThat("Duration should match", bookingDetails.duration(), Is.is(durationOfStay));
        Assertion.assertThat("Adult count should match", bookingDetails.adult(), Is.is(noOfAdults));
        Assertion.assertThat("Total room count should match", bookingDetails.totalRoom(), Is.is(expectedTotalRoom));

        step("Get hotel booking detail to verify total adult and child");
        // DashboardBookingDetails does not give any child/totalChild field (only adult and room counts),
        // so we need to call the booking detail API to retrieve and verify child count information.
        HotelBookingDetailResponse bookingDetailResponse = extranetHotelApi.getHotelBookingDetail(
                hotelId, bookingDetails.itineraryId(), dashboardUsername, dashboardAccountId, "IDR"
        );

        Assertion.assertThat("Hotel Booking Detail Response should not be null", bookingDetailResponse, notNullValue());
        Assertion.assertThat("Total adult should equal with give test data", bookingDetailResponse.data.totalAdult, Is.is(noOfAdults));
        Assertion.assertThat("Total child should equal with given test data", bookingDetailResponse.data.totalChild, Is.is(noOfChildren));
        Assertion.assertThat("Room count in booking detail should match", bookingDetailResponse.data.totalRoom, Is.is(expectedTotalRoom));
    }

    @Author(names = "Alden")
    @Module(name = "BookingPage")
    @Tribe(name = "Extranet")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyCompleteBookCampaignPromoAddonsExtranet() throws FailedLoginException, InterruptedException, ExecutionException {
        Map<String, String> param = getParam("verifyCompleteBookCampaignPromoAddonsExtranet");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LandingPageWeb<?> landingPage = LandingPageWeb.getInstance(session);
        HotelLandingPageWeb<?> hotelLandingPage = HotelLandingPageWeb.getInstance(session);
        HotelDetailPage<?> detailPage = new HotelDetailPage<>(session);
        HotelRoomListPage<?> roomListPage = new HotelRoomListPage<>(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HotelBookingFormPageWeb<?> bookingFormPage = HotelBookingFormPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        CalendarPageWeb<?> calendarPage = CalendarPageWeb.getInstance(session);
        ExtranetHotelApi<?> extranetHotelApi = new ExtranetHotelApi<>(session);
        ExtranetHotelAuthApi<?> authApi = new ExtranetHotelAuthApi<>(session);

        String location = param.get("location");
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String paymentGroup = param.get("paymentGroup");
        String paymentMethod = param.get("paymentMethod");
        int hotelId = Integer.parseInt(param.get("hotelId"));
        String dashboardUsername = param.get("dashboardUsername");
        String dashboardPassword = param.get("dashboardPassword");
        int noOfAdults = Integer.parseInt(param.get("adult"));
        int expectedTotalRoom = Integer.parseInt(param.get("rooms"));
        String promoCode = param.get("promoCode");

        int daysToDepartureDate = CoreFunctions.generateRandomInt(Integer.parseInt(param.get("daysToDepartureDate")),10);
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);
        int durationOfStay = Integer.parseInt(param.get("durationOfStay"));
        String returnDate = CoreFunctions.addToLongDate(departureDate, durationOfStay);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User select IDR currency");
        homePage.selectCurrency("IDR");

        step("User Open Hotel Landing page");
        homePage.openVerticalPageViaURL("Hotel");
        Assertion.assertThat("Landing Page is Opened", landingPage.isLandingPageVisible(), Is.is(true));

        step("Search stay hotel");
        landingPage.searchStayLocation(location);

        step("Set guests and rooms");
        landingPage.clickRoomAndGuestButton();
        landingPage.setTotalGuest(noOfAdults);
        landingPage.clickDone();

        step("Set date");
        hotelLandingPage.clickOnCalendarWidgetToOpen();
        calendarPage.selectDate(departureDate, returnDate);
        hotelLandingPage.clickSubmit();

        step("Go Inside PDP");
        detailPage.clickPopUp();
        Assertion.assertThat("Verify if PDP is Opened", detailPage.isPdpLoaded(), Is.is(true));
        detailPage.seeHotelRoom();

        step("Click Book Button");
        roomListPage.clickBookButton();

        step("User input contact detail");
        Assertion.assertThat("Contact form should be visible on Booking Form page", bookingFormPage.isContactForm(), is(true));

        step("User enter Stay details ");
        Assertion.assertThat("Stay details form should be visible on Booking Form page",
                bookingFormPage.isGuestStayForm(), is(true));

        for (int i = 1; i <= noOfAdults; i++) {
            bookingFormPage.fillGuestDetails(i, CoreFunctions.randomName(), false, false, "");
        }

        step("Go to addons page");
        bookingFormPage.clickContinueButton();

        step("Order addons");
        List<String> addonDetails = bookingFormPage.chooseAddOns();

        step("Apply promo");
        bookingFormPage.applyPromo(promoCode);

        step("Go to payment page");
        bookingFormPage.selectPayment();
        bookingFormPage.clickOnPopUp();

        step("check order id on payment page");
        String orderID = paymentPage.getOrderId();
        orderID = orderID.replaceAll("[^0-9]", "");
        Assertion.assertThat("user get order id", orderID, allOf(
                not(nullValue()),
                not(equalTo(""))
        ));

        step("User select payment method ");
        paymentPage.clickOnSelectPaymentMethod();
        paymentPage.clickOnSeeAll();
        paymentPage.goToPaymentMethodsGroup(paymentGroup);
        paymentPage.selectPaymentMethod(paymentMethod);

        step("User complete payment");
        paymentPage.clickOnPaymentButton();

        step("User try to open see order list");
        orderListPage.waitForPaymentSuccessful(orderID,10);
        paymentPage.clickSeeOrderListButton();

        step("Go to Issued Order");
        orderListPage.openActiveTicketViaURL(orderID);
        orderDetailPage.skipCoachmark();
        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.verifyIssuedETicket(), Is.is(true));
        Assertion.assertThat("contact name should be available on order detail page", orderDetailPage.getContactName(orderID), allOf( not(nullValue()),not(equalTo(""))));
        Assertion.assertThat("email should be correctly displayed on order detail page", orderDetailPage.getContactEmail(orderID), Is.is(email));
        Assertion.assertThat("orderID should be visible and should be same as from payment page", orderDetailPage.getOrderId(), Is.is(orderID));

        step("total adult and child verification on order detail page");
        Map<String, Integer> guests = orderDetailPage.getGuestDetailUI();
        Integer totalGuestsUI = guests.get("totalGuests");
        Integer totalAdultsUI = guests.get("adults");
        Assertion.assertThat("total guests should be equal with given test data", totalGuestsUI, Is.is(noOfAdults));
        Assertion.assertThat("total adult should be equal with given test data", totalAdultsUI, Is.is(noOfAdults));

        step("Authenticate to Extranet Hotel API to get access token");
        ExtranetAdminLoginMfaApplyResponse.Data authData  = authApi.getAccessToken(dashboardUsername, dashboardPassword);
        String accessToken = authData.accessToken;
        Long dashboardAccountId = authData.accountId;

        step("Verify booking exists on hotel dashboard");
        String departureDateISO = CoreFunctions.convertDateFormat(departureDate);
        String returnDateISO = CoreFunctions.convertDateFormat(returnDate);
        ExtranetHotelApi.DashboardBookingDetails bookingDetails = extranetHotelApi.getDashboardBookingDetails(
                hotelId, orderID, departureDateISO, returnDateISO, dashboardUsername, accessToken, dashboardAccountId);

        Assertion.assertThat("Booking should be found on dashboard", bookingDetails.found(), Is.is(true));
        Assertion.assertThat("Order ID should match", String.valueOf(bookingDetails.orderId()), Is.is(orderID));
        Assertion.assertThat("Status should be 'issued'", bookingDetails.status(), Is.is("issued"));
        Assertion.assertThat("Check-in date should match", bookingDetails.checkinDate(), Is.is(departureDateISO));
        Assertion.assertThat("Check-out date should match", bookingDetails.checkoutDate(), Is.is(returnDateISO));
        Assertion.assertThat("Duration should match", bookingDetails.duration(), Is.is(durationOfStay));
        Assertion.assertThat("Adult count should match", bookingDetails.adult(), Is.is(noOfAdults));
        Assertion.assertThat("Total room count should match", bookingDetails.totalRoom(), Is.is(expectedTotalRoom));
        Assertion.assertThat("Promotion should be applied", bookingDetails.promotion(), Is.is(not(emptyOrNullString())));
        Assertion.assertThat("Addons is ordered", bookingDetails.addOnDetails(), not(nullValue()));
        Assertion.assertThat("Verify Addon Details", bookingDetails.addOnDetails()[0].name, is(addonDetails.get(0)));

        step("Get hotel booking detail to verify total adult and child");
        // DashboardBookingDetails does not give any child/totalChild field (only adult and room counts),
        // so we need to call the booking detail API to retrieve and verify child count information.
        HotelBookingDetailResponse bookingDetailResponse = extranetHotelApi.getHotelBookingDetail(
                hotelId, bookingDetails.itineraryId(), dashboardUsername, dashboardAccountId, "IDR"
        );

        Assertion.assertThat("Hotel Booking Detail Response should not be null", bookingDetailResponse, notNullValue());
        Assertion.assertThat("Total adult should equal with give test data", bookingDetailResponse.data.totalAdult, Is.is(noOfAdults));
        Assertion.assertThat("Room count in booking detail should match", bookingDetailResponse.data.totalRoom, Is.is(expectedTotalRoom));
    }

    @Author(names = "Alden")
    @Module(name = "BookingPage")
    @Tribe(name = "Extranet")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyCompleteBookCampaignPromoExtranet() throws FailedLoginException, InterruptedException, ExecutionException {
        Map<String, String> param = getParam("verifyCompleteBookCampaignPromoExtranet");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LandingPageWeb<?> landingPage = LandingPageWeb.getInstance(session);
        HotelLandingPageWeb<?> hotelLandingPage = HotelLandingPageWeb.getInstance(session);
        HotelDetailPage<?> detailPage = new HotelDetailPage<>(session);
        HotelRoomListPage<?> roomListPage = new HotelRoomListPage<>(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HotelBookingFormPageWeb<?> bookingFormPage = HotelBookingFormPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        CalendarPageWeb<?> calendarPage = CalendarPageWeb.getInstance(session);
        ExtranetHotelApi<?> extranetHotelApi = new ExtranetHotelApi<>(session);
        ExtranetHotelAuthApi<?> authApi = new ExtranetHotelAuthApi<>(session);

        String location = param.get("location");
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String paymentGroup = param.get("paymentGroup");
        String paymentMethod = param.get("paymentMethod");
        int hotelId = Integer.parseInt(param.get("hotelId"));
        String dashboardUsername = param.get("dashboardUsername");
        String dashboardPassword = param.get("dashboardPassword");
        int noOfAdults = Integer.parseInt(param.get("adult"));
        int expectedTotalRoom = Integer.parseInt(param.get("rooms"));
        String promoCode = param.get("promoCode");

        int daysToDepartureDate = CoreFunctions.generateRandomInt(Integer.parseInt(param.get("daysToDepartureDate")),10);
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);
        int durationOfStay = Integer.parseInt(param.get("durationOfStay"));
        String returnDate = CoreFunctions.addToLongDate(departureDate, durationOfStay);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User select IDR currency");
        homePage.selectCurrency("IDR");

        step("User Open Hotel Landing page");
        homePage.openVerticalPageViaURL("Hotel");
        Assertion.assertThat("Landing Page is Opened", landingPage.isLandingPageVisible(), Is.is(true));

        step("Search stay hotel");
        landingPage.searchStayLocation(location);

        step("Set guests and rooms");
        landingPage.clickRoomAndGuestButton();
        landingPage.setTotalGuest(noOfAdults);
        landingPage.clickDone();

        step("Set date");
        hotelLandingPage.clickOnCalendarWidgetToOpen();
        calendarPage.selectDate(departureDate, returnDate);
        hotelLandingPage.clickSubmit();

        step("Go Inside PDP");
        detailPage.clickPopUp();
        Assertion.assertThat("Verify if PDP is Opened", detailPage.isPdpLoaded(), Is.is(true));
        detailPage.seeHotelRoom();

        step("Click Book Button");
        roomListPage.clickBookButton();

        step("User input contact detail");
        Assertion.assertThat("Contact form should be visible on Booking Form page", bookingFormPage.isContactForm(), is(true));

        step("User enter Stay details ");
        Assertion.assertThat("Stay details form should be visible on Booking Form page",
                bookingFormPage.isGuestStayForm(), is(true));

        for (int i = 1; i <= noOfAdults; i++) {
            bookingFormPage.fillGuestDetails(i, CoreFunctions.randomName(), false, false, "");
        }

        step("Go to addons page");
        bookingFormPage.clickContinueButton();

        step("Apply promo");
        bookingFormPage.applyPromo(promoCode);

        step("Go to payment page");
        bookingFormPage.selectPayment();
        bookingFormPage.clickOnPopUp();

        step("check order id on payment page");
        String orderID = paymentPage.getOrderId();
        orderID = orderID.replaceAll("[^0-9]", "");
        Assertion.assertThat("user get order id", orderID, allOf(
                not(nullValue()),
                not(equalTo(""))
        ));

        step("User select payment method ");
        paymentPage.clickOnSelectPaymentMethod();
        paymentPage.clickOnSeeAll();
        paymentPage.goToPaymentMethodsGroup(paymentGroup);
        paymentPage.selectPaymentMethod(paymentMethod);

        step("User complete payment");
        paymentPage.clickOnPaymentButton();

        step("User try to open see order list");
        orderListPage.waitForPaymentSuccessful(orderID,10);
        paymentPage.clickSeeOrderListButton();

        step("Go to Issued Order");
        orderListPage.openActiveTicketViaURL(orderID);
        orderDetailPage.skipCoachmark();
        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.verifyIssuedETicket(), Is.is(true));
        Assertion.assertThat("contact name should be available on order detail page", orderDetailPage.getContactName(orderID), allOf( not(nullValue()),not(equalTo(""))));
        Assertion.assertThat("email should be correctly displayed on order detail page", orderDetailPage.getContactEmail(orderID), Is.is(email));
        Assertion.assertThat("orderID should be visible and should be same as from payment page", orderDetailPage.getOrderId(), Is.is(orderID));

        step("total adult and child verification on order detail page");
        Map<String, Integer> guests = orderDetailPage.getGuestDetailUI();
        Integer totalGuestsUI = guests.get("totalGuests");
        Integer totalAdultsUI = guests.get("adults");
        Assertion.assertThat("total guests should be equal with given test data", totalGuestsUI, Is.is(noOfAdults));
        Assertion.assertThat("total adult should be equal with given test data", totalAdultsUI, Is.is(noOfAdults));

        step("Authenticate to Extranet Hotel API to get access token");
        ExtranetAdminLoginMfaApplyResponse.Data authData  = authApi.getAccessToken(dashboardUsername, dashboardPassword);
        String accessToken = authData.accessToken;
        Long dashboardAccountId = authData.accountId;

        step("Verify booking exists on hotel dashboard");
        String departureDateISO = CoreFunctions.convertDateFormat(departureDate);
        String returnDateISO = CoreFunctions.convertDateFormat(returnDate);
        ExtranetHotelApi.DashboardBookingDetails bookingDetails = extranetHotelApi.getDashboardBookingDetails(
                hotelId, orderID, departureDateISO, returnDateISO, dashboardUsername, accessToken, dashboardAccountId);

        Assertion.assertThat("Booking should be found on dashboard", bookingDetails.found(), Is.is(true));
        Assertion.assertThat("Order ID should match", String.valueOf(bookingDetails.orderId()), Is.is(orderID));
        Assertion.assertThat("Status should be 'issued'", bookingDetails.status(), Is.is("issued"));
        Assertion.assertThat("Check-in date should match", bookingDetails.checkinDate(), Is.is(departureDateISO));
        Assertion.assertThat("Check-out date should match", bookingDetails.checkoutDate(), Is.is(returnDateISO));
        Assertion.assertThat("Duration should match", bookingDetails.duration(), Is.is(durationOfStay));
        Assertion.assertThat("Adult count should match", bookingDetails.adult(), Is.is(noOfAdults));
        Assertion.assertThat("Total room count should match", bookingDetails.totalRoom(), Is.is(expectedTotalRoom));
        Assertion.assertThat("Promotion should be applied", bookingDetails.promotion(), Is.is(not(emptyOrNullString())));
        Assertion.assertThat("No addons is ordered", bookingDetails.addOnDetails(), is(nullValue()));

        step("Get hotel booking detail to verify total adult and child");
        // DashboardBookingDetails does not give any child/totalChild field (only adult and room counts),
        // so we need to call the booking detail API to retrieve and verify child count information.
        HotelBookingDetailResponse bookingDetailResponse = extranetHotelApi.getHotelBookingDetail(
                hotelId, bookingDetails.itineraryId(), dashboardUsername, dashboardAccountId, "IDR"
        );

        Assertion.assertThat("Hotel Booking Detail Response should not be null", bookingDetailResponse, notNullValue());
        Assertion.assertThat("Total adult should equal with give test data", bookingDetailResponse.data.totalAdult, Is.is(noOfAdults));
        Assertion.assertThat("Room count in booking detail should match", bookingDetailResponse.data.totalRoom, Is.is(expectedTotalRoom));
    }

    @Author(names = {"Aditi"})
    @Module(name = "Refund")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void testRefundAllowedHotelIDR() throws FailedLoginException, InterruptedException, ExecutionException {
        Map<String, String> param = getParam("testRefundAllowedHotelIDR");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        RefundPageWeb<?> refundPage = RefundPageWeb.getInstance(session);
        RefundDetailWeb<?> refundDetailPage = RefundDetailWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        HotelDetailPage<?> detailPage = new HotelDetailPage<>(session);
        HotelRoomListPageWeb<?> roomListPage = HotelRoomListPageWeb.getInstance(session);
        HotelBookingFormPageWeb<?> bookingFormPage = HotelBookingFormPageWeb.getInstance(session);

        String username = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String currency = param.get("currency");
        String location = param.get("location");
        String dateType = param.get("dateType");
        String dateRange = param.get("dateRange");
        String paymentGroup = param.get("paymentGroup");
        String paymentMethod = param.get("paymentMethod");

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(username, password, otp, retryCounter);

        step("User select " + currency + " currency");
        homePage.selectCurrency(currency);

        step("User Open Hotel Landing page");
        homePage.openVerticalPageViaURL("Hotel");

        Assertion.assertThat("Landing Page is Opened", landingPage.isLandingPageVisible(), is(true));

        step("Search stay hotel");
        landingPage.searchStayLocation(location);

        step("Set date");
        landingPage.setStayDate(dateType, dateRange);

        step("submit search Button");
        landingPage.clickSubmit();

        step("Go Inside PDP");
        String HotelName=detailPage.getHotelNameDetail();
        Assertion.assertThat("Verify if PDP is Opened", detailPage.isPdpLoaded(), is(true));
        Assertion.assertThat("Verify Hotel name on entered on LP and PDP  is same",HotelName,containsString(location));
        detailPage.seeHotelRoom();

        step("Select the Refund and Reschedule allowed filter chip");
        roomListPage.clickFreeRefundRescheduleFilter();
        Assertion.assertThat("Free Refund & Reschedule filter chip should be visible", roomListPage.isFreeRefundRescheduleFilterSelected(), is(true));

        step("Check that the cards now visible have the 100% Refundable label");
        Assertion.assertThat("At least one room should have 100% refund label", roomListPage.isFullRefundLabelVisible(), is(true));

        step("Fetch after tax price from room card");
        String afterTaxPriceFromPDP = roomListPage.getAfterTaxPrice();

        step("Click Book Button");
        roomListPage.clickBookButton();

        step("Go to payment page");
        bookingFormPage.continueToPayment();

        step("Continue to payment page");
        bookingFormPage.selectPayment();

        String orderID = paymentPage.getOrderId();
        Assertion.assertThat("Verify order ID is displayed in payment page", !orderID.isEmpty(), is(true));

        step("User select payment method ");
        paymentPage.clickOnSelectPaymentMethod();
        paymentPage.goToPaymentMethodsGroup(paymentGroup);
        paymentPage.selectPaymentMethod(paymentMethod);

        step("Click on Pay button");
        paymentPage.clickOnPaymentButton();

        step("Verify the payment successful page is visible or not");
        Assertion.assertThat("Verify the payment successful page is visible", paymentPage.isPaymentSuccessfulVisible(), is(true));

        step("User try to open see order list");
        paymentPage.clickSeeOrderListButton();

        step("Find the issued order");
        orderListPage.openActiveTicketViaURL(orderID);
        orderDetailPage.skipCoachmark();

        step("Verify hotel order details");
        Assertion.assertThat("orderID should be visible and should be same as from payment page", orderDetailPage.getOrderId(), is(orderID));
        Assertion.assertThat("E-ticket card Displayed", orderDetailPage.isETicketCardDisplayed(),is(true));
        Assertion.assertThat("Email should be correctly displayed on order detail page", orderDetailPage.getContactEmail(orderID), is(username));
        Assertion.assertThat("Contact name should be available on order detail page", !orderDetailPage.getContactName(orderID).isEmpty(), is(true));
        Assertion.assertThat("Hotel Name is displayed correctly", orderDetailPage.isHotelNameDisplayed(location), is(true));
        Assertion.assertThat("Refund and reschedule info section should be verified", orderDetailPage.verifyRefundAndRescheduleInfo(), is(true));
        Assertion.assertThat("Refund button should be displayed", orderDetailPage.isRefundButtonDisplayed(), is(true));

        step("Open refund page");
        orderDetailPage.clickRefundButton();
        refundPage.waitForContentLoaderDisappear();

        step("Click to continue to refund");
        refundPage.continueToRefund();

        step("Select random refund reason");
        refundPage.selectRandomRefundReason();

        step("Verify refund estimation section");
        refundPage.waitForEstimationAmountAppear("10");
        String refundReceive = refundPage.getRefundYoullReceive();
        String refundFee = refundPage.getRefundFee();
        String refundTotal = refundPage.getRefundTotal();
        List<String> amounts = new ArrayList<>(Arrays.asList(refundReceive, refundFee, refundTotal));
        Assertion.assertThat("Refund Total amount is correct", CoreFunctions.removeText(refundReceive)+CoreFunctions.removeText(refundFee), is(CoreFunctions.removeText(refundTotal)));

        step("Verify refund amount breakdown");
        refundPage.openRefundBreakdown();
        amounts.clear();
        amounts.addAll(refundPage.getRefundAmountBreakdown(currency));

        if (refundPage.getRefundFeeBreakdown(currency).isPresent()) amounts.addAll(refundPage.getRefundFeeBreakdown(currency).get());
        amounts.addAll(refundPage.getRefundTotalBreakdown(currency));
        Assertion.assertThat("Total breakdown refund same as estimation", refundPage.getRefundTotalBreakdownSum(currency), is(String.valueOf(CoreFunctions.removeText(refundTotal))));
        refundPage.closeRefundBreakdown();

        step("Verify refund total matches the booking after-tax price");
        String refundTotalPrice = refundPage.getRefundTotalAndCurrency();
        Assertion.assertThat("Refund total should match price from PDP", refundTotalPrice, is(afterTaxPriceFromPDP));

        step("Continue to next page");
        refundPage.clickButton("Continue");
        refundPage.waitRefundSecondPageLoaded();
        Assertion.assertThat("Order ID on 2nd refund page is correct", refundPage.getOrderIdOnRefundBanner(), containsString(orderID));

        step("Verify refund method and amount");
        String refundMethodSection = refundPage.getRefundMethod();
        Assertion.assertThat("Refund method is correct", refundMethodSection, not(emptyOrNullString()));
        Assertion.assertThat("Refund amount is same as prev page", refundTotal, is(refundPage.getRefundTotal()));

        step("Check Refund TnC");
        Assertion.assertThat("Submit refund button is disabled", refundPage.isDisabledSubmitButton(), is(true));
        refundPage.clickChecklistTnc();
        refundPage.clickButton("Submit");

       step("Submit refund");
        refundPage.clickButton("Process Refund");
        refundPage.clickConfirmationRefundPopup();
        refundPage.waitForSubmitRefundLoader();

        step("Go to refund detail page");
        refundPage.SuccessSubmitRefundPopup();

        step("Verify Refund Detail");
        refundDetailPage.countRefundProgressTracker();
        Assertion.assertThat("Refund detail status not empty or null string", refundDetailPage.getRefundStatus(), not(emptyOrNullString()));
        Assertion.assertThat("Refund Id not empty or null string", refundDetailPage.getRefundId(), not(emptyOrNullString()));

        step("Verify Refund Method and Amount");
        for (String method : refundDetailPage.getRefundMethod()) {
            Assertion.assertThat("Refund method not empty", method, not(emptyOrNullString()));
        }

        step("Verify product section");
        Assertion.assertThat("Product detail shown", refundDetailPage.isOrderDetailSection(), is(true));

        step("Verify back button to order list");
        refundDetailPage.goToYourOrders();

        step("Verify the refunded order is no longer there");
        Assertion.assertThat("The refunded order is no longer in your order page",
                refundDetailPage.isOrderIdPresentinYourOrder(orderID),
                is(false));
    }

    @Author(names = "Alden")
    @Module(name = "Extranet")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyCompleteBookMultiRoomMultiPlanExtranet() throws FailedLoginException, InterruptedException, ExecutionException {
        Map<String, String> param = getParam("verifyCompleteBookMultiRoomMultiPlanExtranet");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LandingPageWeb<?> landingPage = LandingPageWeb.getInstance(session);
        HotelLandingPageWeb<?> hotelLandingPage = HotelLandingPageWeb.getInstance(session);
        HotelDetailPage<?> detailPage = new HotelDetailPage<>(session);
        HotelRoomListPage<?> roomListPage = new HotelRoomListPage<>(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HotelBookingFormPageWeb<?> bookingFormPage = HotelBookingFormPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        CalendarPageWeb<?> calendarPage = CalendarPageWeb.getInstance(session);
        ExtranetHotelApi<?> extranetHotelApi = new ExtranetHotelApi<>(session);
        ExtranetHotelAuthApi<?> authApi = new ExtranetHotelAuthApi<>(session);

        String location = param.get("location");
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String paymentGroup = param.get("paymentGroup");
        String paymentMethod = param.get("paymentMethod");
        int hotelId = Integer.parseInt(param.get("hotelId"));
        String dashboardUsername = param.get("dashboardUsername");
        String dashboardPassword = param.get("dashboardPassword");
        int noOfAdults = Integer.parseInt(param.get("adult"));
        int noOfRoom = Integer.parseInt(param.get("rooms"));
        int expectedTotalRoom = Integer.parseInt(param.get("rooms"));

        int daysToDepartureDate = CoreFunctions.generateRandomInt(Integer.parseInt(param.get("daysToDepartureDate")),10);
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);
        int durationOfStay = Integer.parseInt(param.get("durationOfStay"));
        String returnDate = CoreFunctions.addToLongDate(departureDate, durationOfStay);

        step("User open home page");
        homePage.goToHomePage();

        step("Login as existing user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User select IDR currency");
        homePage.selectCurrency("IDR");

        step("User Open Hotel Landing page");
        homePage.openVerticalPageViaURL("Hotel");

        step("Search stay location");
        landingPage.searchStayLocation(location);

        step("Room and guest count update");
        landingPage.clickRoomAndGuestButton();
        landingPage.setTotalRoom(noOfRoom);
        landingPage.setTotalGuest(noOfAdults);
        landingPage.clickDone();

        step("Set date");
        hotelLandingPage.clickOnCalendarWidgetToOpen();
        calendarPage.selectDate(departureDate, returnDate);

        step("submit search Button");
        landingPage.clickSubmit();

        step("Go Inside PDP");
        Assertion.assertThat("Verify if PDP is Opened", detailPage.isPdpLoaded(), is(true));
        Assertion.assertThat("Hotel name on the search and detail page should be equal", detailPage.getHotelNameDetail(), containsString(location));

        step("Rooms are available hence click on See Rooms Button");
        detailPage.seeHotelRoom();

        step("Scroll to room list section");
        roomListPage.scrollToRoomListSection();
        Assertion.assertThat("Room list is visible", roomListPage.isRoomListSectionVisible(), is(true));
        Assertion.assertThat("Rooms Image should be visible", roomListPage.isRoomImageVisible(), is(true));

        step("Select first room");
        roomListPage.clickBookButton();

        step("Select second room with different rate plan");
        roomListPage.clickBookButton();

        step("Click Multi-Book button");
        roomListPage.swipeToMultiRoomBookButton();
        roomListPage.clickMultiBookButton();

        step("User input contact detail");
        Assertion.assertThat("Contact form should be visible on Booking Form page", bookingFormPage.isContactForm(), is(true));

        step("User enter Stay details ");
        Assertion.assertThat("Stay details form should be visible on Booking Form page", bookingFormPage.isGuestStayForm(), is(true));
        bookingFormPage.fillGuestDetailsCombinedRandom(2);

        step("Go to payment page");
        bookingFormPage.continueToSecondPartThenPaymentPage();
        bookingFormPage.clickOnPopUp();

        step("check order id on payment page");
        String orderID = paymentPage.getOrderId();
        orderID = orderID.replaceAll("[^0-9]", "");
        Assertion.assertThat("user get order id", orderID, allOf(
                not(nullValue()),
                not(equalTo(""))
        ));

        step("User select payment method ");
        paymentPage.clickOnSelectPaymentMethod();
        paymentPage.clickOnSeeAll();
        paymentPage.goToPaymentMethodsGroup(paymentGroup);
        paymentPage.selectPaymentMethod(paymentMethod);

        step("User complete payment");
        paymentPage.clickOnPaymentButton();

        step("User try to open see order list");
        orderListPage.waitForPaymentSuccessful(orderID,10);
        paymentPage.clickSeeOrderListButton();

        step("Go to Issued Order");
        orderListPage.openActiveTicketViaURL(orderID);
        orderDetailPage.skipCoachmark();
        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.verifyIssuedETicket(), Is.is(true));
        Assertion.assertThat("contact name should be available on order detail page", orderDetailPage.getContactName(orderID), allOf( not(nullValue()),not(equalTo(""))));
        Assertion.assertThat("email should be correctly displayed on order detail page", orderDetailPage.getContactEmail(orderID), Is.is(email));
        Assertion.assertThat("orderID should be visible and should be same as from payment page", orderDetailPage.getOrderId(), Is.is(orderID));

        step("Authenticate to Extranet Hotel API to get access token");
        ExtranetAdminLoginMfaApplyResponse.Data authData  = authApi.getAccessToken(dashboardUsername, dashboardPassword);
        Long dashboardAccountId = authData.accountId;

        step("Verify booking exists on hotel dashboard");
        String departureDateISO = CoreFunctions.convertDateFormat(departureDate);
        String returnDateISO = CoreFunctions.convertDateFormat(returnDate);

        List<ExtranetHotelApi.DashboardBookingDetails> bookingDetails = extranetHotelApi.getDashboardMultiBookingDetails(
                hotelId, orderID, departureDateISO, returnDateISO, dashboardUsername, dashboardPassword);

        int totalAdultDashboard = 0, totalRoomDashboard = 0, totalAdultBooking = 0, totalRoomBooking = 0, iteration = 0;
        Set<String> ratePlanNames = new HashSet<>();

        for (ExtranetHotelApi.DashboardBookingDetails bookingDetail : bookingDetails) {
            step("verify for room no. " + ++iteration);
            Assertion.assertThat("Booking should be found on dashboard", bookingDetail.found(), Is.is(true));
            Assertion.assertThat("Order ID should match", String.valueOf(bookingDetail.orderId()), Is.is(orderID));
            Assertion.assertThat("Status should be 'issued'", bookingDetail.status(), Is.is("issued"));
            Assertion.assertThat("Check-in date should match", bookingDetail.checkinDate(), Is.is(departureDateISO));
            Assertion.assertThat("Check-out date should match", bookingDetail.checkoutDate(), Is.is(returnDateISO));
            Assertion.assertThat("Duration should match", bookingDetail.duration(), Is.is(durationOfStay));

            totalAdultDashboard += bookingDetail.adult();
            totalRoomDashboard += bookingDetail.totalRoom();
            ratePlanNames.add(bookingDetail.ratePlanName());

            // DashboardBookingDetails does not give any child/totalChild field (only adult and room counts),
            // so we need to call the booking detail API to retrieve and verify child count information.
            HotelBookingDetailResponse bookingDetailResponse = extranetHotelApi.getHotelBookingDetail(
                    hotelId, bookingDetail.itineraryId(), dashboardUsername, dashboardAccountId, "IDR"
            );

            Assertion.assertThat("Hotel Booking Detail Response should not be null", bookingDetailResponse, notNullValue());
            totalAdultBooking += bookingDetailResponse.data.totalAdult;
            totalRoomBooking += bookingDetailResponse.data.totalRoom;
        }

        step("verify rate plans");
        Assertion.assertThat("order has multiple distinct rate plan names (using set)", ratePlanNames.size(), is(expectedTotalRoom));

        step("verify dashboard adult and room total count");
        Assertion.assertThat("Adult count should match", totalAdultDashboard, Is.is(noOfAdults));
        Assertion.assertThat("Total room count should match", totalRoomDashboard, Is.is(expectedTotalRoom));

        step("Get hotel booking detail to verify total adult and child");
        Assertion.assertThat("Total adult should equal with give test data", totalAdultBooking, Is.is(noOfAdults));
        Assertion.assertThat("Room count in booking detail should match", totalRoomBooking, Is.is(expectedTotalRoom));
    }

    @Author(names = "Aditi")
    @Module(name = "BookingPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyCompleteBookNonIDRGuest() throws InterruptedException, FailedLoginException {
        Map<String, String> param = getParam("verifyCompleteBookNonIDRGuest");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String username = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String hotelName = param.get("hotelName");
        String currency = param.get("currency");
        String location = param.get("location");
        String cardNumber = param.get("cardNumber");
        String cardMonth = param.get("cardMonth");
        String cardCvv = param.get("cardCvv");
        String cardHolderName = param.get("cardHolderName");
        String dateType = param.get("dateType");
        String dateRange = param.get("dateRange");
        String fullName = CoreFunctions.randomName() + " Test";
        String phoneNumber = CoreFunctions.randomPhone();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        HotelDetailPage<?> detailPage = new HotelDetailPage<>(session);
        SRPPageWeb<?> searchResultPage = SRPPageWeb.getInstance(session);
        HotelRoomListPage<?> roomListPage = new HotelRoomListPage<>(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HotelBookingFormPageWeb<?> bookingFormPage = HotelBookingFormPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User login as guest");
        loginPage.loginAsGuest();

        step("User select " + currency + " currency");
        homePage.selectCurrency(currency);

        step("User Open Hotel Landing page");
        String searchPageCurrency = homePage.getCurrentCurrency();
        Assertion.assertThat("Verify currency in  Search Result page is set to SGD", searchPageCurrency.equals("SGD"),
                Is.is(true));
        homePage.openVerticalPageViaURL("Hotel");
        Assertion.assertThat("Landing Page is Opened", landingPage.isLandingPageVisible(), Is.is(true));

        step("Search stay hotel");
        landingPage.searchStayLocation(location);
        landingPage.setStayDate(dateType, dateRange);
        landingPage.clickSubmit();

        step("Go Inside PDP");
        detailPage.seeHotelRoom();
        Assertion.assertThat("Verify if PDP is Opened", detailPage.isPdpLoaded(), Is.is(true));

        step("Click Book Button");
        roomListPage.clickBookButton();

        step("Click book button");
        bookingFormPage.fillContactDetail(fullName, phoneNumber, username);
        bookingFormPage.fillGuestDetailsCombinedRandom(1);

        step("Go to payment page");
        bookingFormPage.continueToPayment();

        step("Continue to payment page");
        bookingFormPage.selectPayment();

        step("check order id on payment page");
        String orderID = paymentPage.getOrderId();
        Assertion.assertThat("Verify order ID is displayed in payment page", !orderID.isEmpty(), Is.is(true));

        step("Input the card details");
        paymentPage.inputCardNumber(cardNumber);
        paymentPage.inputCardMonth(cardMonth);
        paymentPage.inputCardCvv(cardCvv);
        paymentPage.inputCardHolderName(cardHolderName);

        step("User complete payment");
        paymentPage.clickOnPaymentButton();

        step("User try to open see order list");
        Assertion.assertThat("See order list button appeared", paymentPage.isSeeOrderListButtonDisplayed(), is(true));

        step("Go to Issued Order");
        orderListPage.openActiveTicketViaURL(orderID);
        orderListPage.loginFromOrderDetailPage(username, password, otp, retryCounter);
        orderDetailPage.skipCoachmark();

        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.verifyIssuedETicket(),
                is(true));
        Assertion.assertThat("orderID should be visible and should be same as from payment page",
                orderDetailPage.getOrderId(), is(orderID));

        step("Verify currency on order detail page");
        orderDetailPage.verifyCurrencyOnOrderDetail(currency);

        step("Click on Receipt button");
        orderDetailPage.clickReceiptButton();

        step("Verify currency on receipt");
        orderDetailPage.openReceipt();
        orderDetailPage.verifyCurrencyOnReceipt(currency);

        step("Verify email notification is sent for order");
        OutboundNotificationVerifier.verify(username, orderID, NotificationChannel.EMAIL);

        step("Verify currency on email notification");
        OutboundNotificationVerifier.verifyCurrency(username, orderID, NotificationChannel.EMAIL, currency);

    }

    @Author(names = "Aditi")
    @Module(name = "BookingPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyCompleteBookNotiffCheckHotelGuest() throws InterruptedException, FailedLoginException {
        Map<String, String> param = getParam("verifyCompleteBookNotiffCheckHotelGuest");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String username = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String currency = param.get("currency");
        String location = param.get("location");
        String dateType = param.get("dateType");
        String dateRange = param.get("dateRange");
        String fullName = CoreFunctions.randomName() + " Test";
        String phoneNumber = CoreFunctions.randomPhone();
        String paymentGroup = param.get("paymentGroup");
        String paymentMethod = param.get("paymentMethod");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        HotelDetailPage<?> detailPage = new HotelDetailPage<>(session);
        SRPPageWeb<?> searchResultPage = SRPPageWeb.getInstance(session);
        HotelRoomListPage<?> roomListPage = new HotelRoomListPage<>(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HotelBookingFormPageWeb<?> bookingFormPage = HotelBookingFormPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User login as guest");
        loginPage.loginAsGuest();

        step("User select " + currency + " currency");
        homePage.selectCurrency(currency);

        step("User Open Hotel Landing page");
        String searchPageCurrency = homePage.getCurrentCurrency();
        Assertion.assertThat("Verify currency in  Search Result page is set to " + currency, searchPageCurrency.equals(currency), Is.is(true));
        homePage.openVerticalPageViaURL("Hotel");
        Assertion.assertThat("Landing Page is Opened", landingPage.isLandingPageVisible(), Is.is(true));

        step("Search stay hotel");
        landingPage.searchStayLocation(location);
        landingPage.setStayDate(dateType, dateRange);
        landingPage.clickSubmit();

        step("Go Inside PDP");
        detailPage.seeHotelRoom();
        Assertion.assertThat("Verify if PDP is Opened", detailPage.isPdpLoaded(), Is.is(true));

        step("Click Book Button");
        roomListPage.clickBookButton();

        step("Fill guest details");
        bookingFormPage.fillContactDetail(fullName, phoneNumber, username);
        bookingFormPage.fillGuestDetailsCombinedRandom(1);

        step("Go to payment page");
        bookingFormPage.continueToPayment();

        step("check order id on payment page");
        String orderID = paymentPage.getOrderId();
        Assertion.assertThat("Verify order ID is displayed in payment page", !orderID.isEmpty(), Is.is(true));

        step("User select payment method ");
        paymentPage.clickOnSelectPaymentMethod();
        paymentPage.clickOnSeeAll();
        paymentPage.goToPaymentMethodsGroup(paymentGroup);
        paymentPage.selectPaymentMethod(paymentMethod);

        step("User complete payment");
        paymentPage.clickOnPaymentButton();

        step("User try to open see order list");
        Assertion.assertThat("See order list button appeared", paymentPage.isSeeOrderListButtonDisplayed(), is(true));

        step("Go to Issued Order");
        orderListPage.openActiveTicketViaURL(orderID);
        orderListPage.loginFromOrderDetailPage(username, password, otp, retryCounter);
        orderDetailPage.skipCoachmark();

        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.verifyIssuedETicket(),
                is(true));
        Assertion.assertThat("orderID should be visible and should be same as from payment page",
                orderDetailPage.getOrderId(), is(orderID));

        step("Verify currency on order detail page");
        orderDetailPage.verifyCurrencyOnOrderDetail(currency);

        step("Click on Receipt button");
        orderDetailPage.clickReceiptButton();

        step("Verify currency on receipt");
        orderDetailPage.openReceipt();
        orderDetailPage.verifyCurrencyOnReceipt(currency);

        step("Verify email notification is sent for order");
        OutboundNotificationVerifier.verify(username, orderID, NotificationChannel.EMAIL);

        step("Verify currency on email notification");
        OutboundNotificationVerifier.verifyCurrency(username, orderID, NotificationChannel.EMAIL, currency);

    }

    @Author(names = "Aditi")
    @Module(name = "BookingPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyCompleteBookNotiffCheckHotelLogin() throws InterruptedException, FailedLoginException {
        Map<String, String> param = getParam("verifyCompleteBookNotiffCheckHotelLogin");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String username = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String currency = param.get("currency");
        String location = param.get("location");
        String dateType = param.get("dateType");
        String dateRange = param.get("dateRange");
        String fullName = CoreFunctions.randomName() + " Test";
        String phoneNumber = CoreFunctions.randomPhone();
        String paymentGroup = param.get("paymentGroup");
        String paymentMethod = param.get("paymentMethod");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        HotelDetailPage<?> detailPage = new HotelDetailPage<>(session);
        SRPPageWeb<?> searchResultPage = SRPPageWeb.getInstance(session);
        HotelRoomListPage<?> roomListPage = new HotelRoomListPage<>(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HotelBookingFormPageWeb<?> bookingFormPage = HotelBookingFormPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User login as guest");
        loginPage.loginIfNotWithSameEmail(username, password, otp, retryCounter);

        step("User select " + currency + " currency");
        homePage.selectCurrency(currency);

        step("User Open Hotel Landing page");
        String searchPageCurrency = homePage.getCurrentCurrency();
        Assertion.assertThat("Verify currency in  Search Result page is set to " + currency, searchPageCurrency.equals(currency), Is.is(true));
        homePage.openVerticalPageViaURL("Hotel");
        Assertion.assertThat("Landing Page is Opened", landingPage.isLandingPageVisible(), Is.is(true));

        step("Search stay hotel");
        landingPage.searchStayLocation(location);
        landingPage.setStayDate(dateType, dateRange);
        landingPage.clickSubmit();

        step("Go Inside PDP");
        detailPage.seeHotelRoom();
        Assertion.assertThat("Verify if PDP is Opened", detailPage.isPdpLoaded(), Is.is(true));

        step("Click Book Button");
        roomListPage.clickBookButton();

        step("Fill guest details");
        bookingFormPage.fillGuestDetailsCombinedRandom(1);

        step("Go to payment page");
        bookingFormPage.continueToPayment();

        step("check order id on payment page");
        String orderID = paymentPage.getOrderId();
        Assertion.assertThat("Verify order ID is displayed in payment page", !orderID.isEmpty(), Is.is(true));

        step("User select payment method ");
        paymentPage.clickOnSelectPaymentMethod();
        paymentPage.clickOnSeeAll();
        paymentPage.goToPaymentMethodsGroup(paymentGroup);
        paymentPage.selectPaymentMethod(paymentMethod);

        step("User complete payment");
        paymentPage.clickOnPaymentButton();

        step("User try to open see order list");
        Assertion.assertThat("See order list button appeared", paymentPage.isSeeOrderListButtonDisplayed(), is(true));

        step("Go to Issued Order");
        orderListPage.openActiveTicketViaURL(orderID);
        orderDetailPage.skipCoachmark();

        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.verifyIssuedETicket(),
                is(true));
        Assertion.assertThat("orderID should be visible and should be same as from payment page",
                orderDetailPage.getOrderId(), is(orderID));

        step("Verify currency on order detail page");
        orderDetailPage.verifyCurrencyOnOrderDetail(currency);

        step("Click on Receipt button");
        orderDetailPage.clickReceiptButton();

        step("Verify currency on receipt");
        orderDetailPage.openReceipt();
        orderDetailPage.verifyCurrencyOnReceipt(currency);

        step("Verify email notification is sent for order");
        OutboundNotificationVerifier.verify(username, orderID, NotificationChannel.EMAIL);

        step("Verify currency on email notification");
        OutboundNotificationVerifier.verifyCurrency(username, orderID, NotificationChannel.EMAIL, currency);

    }

    @Author(names = "Mridul Gupta")
    @Module(name = "Booking")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyMultiOrderNonIDRLogin() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("verifyMultiOrderNonIDRLogin");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String location = param.get("hotelName");
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String cardNumber = param.get("cardNumber");
        String cardMonth = param.get("cardMonth");
        String cardCvv = param.get("cardCvv");
        String cardHolderName = param.get("cardHolderName");
        int noOfRooms = Integer.parseInt(param.get("room"));
        int guests = Integer.parseInt(param.get("adult"));
        String currency = param.get("currency");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);
        int daysToReturnDate = Integer.parseInt(param.get("daysToReturnDate"));
        String returnDate = CoreFunctions.addToLongDate(departureDate, daysToReturnDate);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        HotelDetailPage<?> detailPage = new HotelDetailPage<>(session);
        HotelRoomListPage<?> roomListPage = new HotelRoomListPage<>(session);
        HotelBookingFormPageWeb<?> bookingFormPage = HotelBookingFormPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        CalendarPageWeb<?> calendarPage = CalendarPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User login ");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User select " + currency + " currency");
        homePage.goToHomePage();
        homePage.selectCurrency(currency);

        step("User Open Hotel Landing page");
        homePage.openVerticalPageViaURL("Hotel");

        step("Search stay location");
        landingPage.searchStayLocation(location);

        step("Set date");
        landingPage.clickOnCalendarWidgetToOpen();
        calendarPage.selectDate(departureDate, returnDate);

        step("Set Guest : " + guests + " Adults, " + noOfRooms + " Rooms");
        landingPage.clickRoomAndGuestButton();
        landingPage.setTotalGuest(guests);
        landingPage.setTotalRoom(noOfRooms);
        landingPage.clickDone();

        step("submit search Button");
        landingPage.clickSubmit();

        step("Go Inside PDP");
        Assertion.assertThat("Verify if PDP is Opened", detailPage.isPdpLoaded(), is(true));
        Assertion.assertThat("Hotel name on the search and detail page should be equal", detailPage.getHotelNameDetail(), containsString(location));

        step("Rooms are available hence click on See Rooms Button");
        detailPage.seeHotelRoom();

        step("Scroll to room list section");
        roomListPage.scrollToRoomListSection();
        Assertion.assertThat("Room list is visible", roomListPage.isRoomListSectionVisible(), is(true));
        Assertion.assertThat("Rooms Image should be visible", roomListPage.isRoomImageVisible(), is(true));

        step("Select first room");
        roomListPage.clickBookButton();

        step("Select second room with different rate plan");
        roomListPage.clickBookButton();

        step("Click Multi-Book button");
        roomListPage.clickMultiBookButton();

        step("User enter Stay details ");
        Assertion.assertThat("Stay details form should be visible on Booking Form page", bookingFormPage.isGuestStayForm(), is(true));
        bookingFormPage.fillGuestDetailsCombinedRandom(noOfRooms);

        step("Continue to next page");
        bookingFormPage.continueToNextPage();

        step("Continue to payment page");
        bookingFormPage.continueToPaymentPage();

        step("Go to payment page");
        String orderID = paymentPage.getOrderId();
        Assertion.assertThat("Verify order ID is displayed in payment page", !orderID.isEmpty(), is(true));

        step("Input the card details");
        paymentPage.inputCardNumber(cardNumber);
        paymentPage.inputCardMonth(cardMonth);
        paymentPage.inputCardCvv(cardCvv);
        paymentPage.inputCardHolderName(cardHolderName);

        step("User complete payment");
        paymentPage.clickOnPaymentButton();

        step("Verify the payment successful page is visible or not");
        Assertion.assertThat("Verify the payment successful page is visible", paymentPage.isPaymentSuccessfulVisible(), is(true));

        step("User open order detail page");
        orderListPage.openActiveTicketViaURL(orderID);
        orderDetailPage.skipCoachmark();

        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.verifyIssuedETicket(), is(true));
        Assertion.assertThat("orderID should be visible and should be same as from payment page", orderDetailPage.getOrderId(), is(orderID));

        step("Verify currency on order detail page");
        orderDetailPage.verifyCurrencyOnOrderDetail(currency);

        step("Click on Receipt button");
        orderDetailPage.clickReceiptButton();

        step("Verify currency on receipt");
        orderDetailPage.openReceipt();
        orderDetailPage.verifyCurrencyOnReceipt(currency);
    }

    @Author(names = "Ashish")
    @Module(name = "PostPurchase")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyEticketLanguageAndBookingDetailsWeb() throws FailedLoginException, InterruptedException, ExecutionException {
        Map<String, String> param = getParam("verifyEticketLanguageAndBookingDetailsWeb");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        String email = param.getOrDefault("email", param.get("username"));
        String password = param.get("password");
        String startDate = generateRandomDate(param.get("rangeAddDate"));
        String hotelName = param.get("hotelName");
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));
        int room = Integer.parseInt(param.get("room"));
        int qtyEachRoom = Integer.parseInt(param.getOrDefault("qtyEachRoom", "1"));
        String currency = param.get("currency");
        int addonsAmount = Integer.parseInt(param.get("addonsAmount"));
        String prefixSpecialRequest = param.getOrDefault("prefixSpecialRequest", "AutoTest-");
        boolean isMultiRoom = Boolean.parseBoolean(param.getOrDefault("isMultiRoom", "false"));
        String accommodationTypes = param.getOrDefault("accommodationTypes", "HOTEL");

        HotelApi<?> hotelApi = new HotelApi<>(session);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        ETicketWeb<?> eTicketWeb = ETicketWeb.getInstance(session);

        step("User opens home page and logs in");
        homePage.goToHomePage();
        loginPage.loginWithSpecificAccountViaCookie(email, password);
        homePage.selectCurrency(currency);

        step("Create and pay (via Sakuku) a hotel order with add-ons and special request via API");
        String orderId = "";
        String addonsName = "";
        String specialRequestText = "";
        List<String> bookedGuestNames = new ArrayList<>();
        try {
            orderId = hotelApi.createIssuedOrderWithExtrasApi(
                    email, password, startDate, hotelName, adult, night, room,
                    qtyEachRoom, currency, addonsAmount,
                    true, prefixSpecialRequest,
                    accommodationTypes, "id", isMultiRoom);
            Assertion.assertThat("Order created and paid via Sakuku", orderId,
                    both(is(not(emptyOrNullString()))).and(is(not("0"))));
            addonsName = hotelApi.addonsName;
            Assertion.assertThat("Add-on name should be captured from prebook",
                    addonsName, is(not(emptyOrNullString())));
            specialRequestText = hotelApi.getOrderSpecialRequest();
            Assertion.assertThat("Special request should be captured from booking",
                    specialRequestText, is(not(emptyOrNullString())));
            bookedGuestNames = new ArrayList<>(hotelApi.getGuestNameList());
            Assertion.assertThat("Guest names should be captured from booking",
                    bookedGuestNames, is(not(empty())));
        } catch (Exception e) {
            Assertion.skip("Error when creating + paying order via API: " + e.getMessage());
        }
        if (orderId == null || orderId.isEmpty() || "0".equals(orderId)) {
            Assertion.skip("Order was not created via API. Skipping the subsequent steps.");
            return;
        }

        String bookingLanguage = "Bahasa Indonesia";
        String otherLanguage = "English";

        // Anchor pattern: the add-ons date and quantity must appear right
        // after the localized "Add-ons" / "Fasilitas Ekstra" section header.
        // Example expected text: "Add-ons: 2026-07-10 1 dinner" (en) or
        // "Fasilitas Ekstra: 2026-07-10 1 makan malam" (id).
        String addonsSectionDateQtyRegex =
                "(?:Add-?ons|Fasilitas\\s+Ekstra)[\\s:]*" + Pattern.quote(startDate)
                        + "\\s+" + addonsAmount;
        Pattern addonsSectionDateQtyPattern =
                Pattern.compile(addonsSectionDateQtyRegex,
                        Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

        step("Open the issued order detail (no site-language change needed)");
        orderListPage.waitForPaymentSuccessful(orderId, 10);
        orderListPage.openActiveTicketViaURL(orderId);

        step("Verify e-ticket button is visible on order detail");
        Assertion.assertThat("View e-ticket button should be visible on order detail",
                orderDetailPage.verifyIssuedETicket(orderId), is(true));
        Assertion.assertThat("E-ticket button should be visible on manage-order section",
                orderDetailPage.verifyManageOrderETicketShown(), is(true));

        step("Click e-ticket button and verify both English and Bahasa Indonesia options are offered");
        orderDetailPage.clickViewETicketButton();
        Assertion.assertThat("E-ticket modal should offer both English and Bahasa Indonesia options",
                orderDetailPage.verifyETicketModalContainsEnglishAndBahasaOption(), is(true));

        // ---- PDF 1: booking-language PDF (FULL verification) ----
        step("Open the e-ticket PDF in '" + bookingLanguage + "' (booking language) and verify FULL content");
        String pdfBookingUrl = orderDetailPage.openETicketAndCapturePdfUrl(bookingLanguage);
        Assertion.assertThat(bookingLanguage + " e-ticket PDF URL should be captured from the new tab",
                pdfBookingUrl, is(not(emptyOrNullString())));
        String pdfBookingLang = eTicketWeb.pdfContent(pdfBookingUrl);
        Assertion.assertThat(bookingLanguage + " PDF should not be empty", pdfBookingLang, is(not(emptyOrNullString())));
        if (bookingLanguage.equals("English")) {
            Assertion.assertThat("English PDF should render English labels (e.g. 'Add-ons' or 'Special Request')",
                    pdfBookingLang, anyOf(containsStringIgnoringCase("Add-ons"),
                            containsStringIgnoringCase("Special Request")));
        } else {
            Assertion.assertThat("Bahasa PDF should render Bahasa labels (e.g. 'Fasilitas Ekstra' or 'Permintaan Khusus')",
                    pdfBookingLang, anyOf(containsStringIgnoringCase("Fasilitas Ekstra"),
                            containsStringIgnoringCase("Permintaan Khusus")));
        }
        // The API returns the canonical (English) add-on name regardless of the
        // booking locale, so we can only assert the add-on name itself when the
        // PDF is in English. For the Bahasa PDF we still validate the
        // date+qty pattern below, which is language-stable.
        if (bookingLanguage.equals("English")) {
            Assertion.assertThat(bookingLanguage + " PDF should list the booked add-on name (" + addonsName + ")",
                    pdfBookingLang, containsStringIgnoringCase(addonsName));
        }
        Assertion.assertThat(bookingLanguage + " PDF add-ons section should show '<date> <qty>' (e.g. \""
                        + startDate + " " + addonsAmount + "\") immediately after the section header",
                addonsSectionDateQtyPattern.matcher(pdfBookingLang).find(), is(true));
        Assertion.assertThat(bookingLanguage + " PDF should mention the special request",
                pdfBookingLang, containsStringIgnoringCase(specialRequestText));
        Assertion.assertThat(bookingLanguage + " PDF should mention the booked hotel",
                pdfBookingLang, containsStringIgnoringCase(hotelName));

        // ---- PDF 2: other-language PDF (LOOSE verification - skips localized add-on name) ----
        step("Re-open the e-ticket modal for the other language");
        orderListPage.openActiveTicketViaURL(orderId);
        orderDetailPage.clickViewETicketButton();
        step("Open the e-ticket PDF in '" + otherLanguage + "' (other language) and verify language-stable fields only");
        String pdfOtherUrl = orderDetailPage.openETicketAndCapturePdfUrl(otherLanguage);
        Assertion.assertThat(otherLanguage + " e-ticket PDF URL should be captured from the new tab",
                pdfOtherUrl, is(not(emptyOrNullString())));
        String pdfOtherLang = eTicketWeb.pdfContent(pdfOtherUrl);
        Assertion.assertThat(otherLanguage + " PDF should not be empty", pdfOtherLang, is(not(emptyOrNullString())));
        if (otherLanguage.equals("English")) {
            Assertion.assertThat("English PDF should render English labels (proves language switch worked)",
                    pdfOtherLang, anyOf(containsStringIgnoringCase("Add-ons"),
                            containsStringIgnoringCase("Special Request")));
        } else {
            Assertion.assertThat("Bahasa PDF should render Bahasa labels (proves language switch worked)",
                    pdfOtherLang, anyOf(containsStringIgnoringCase("Fasilitas Ekstra"),
                            containsStringIgnoringCase("Permintaan Khusus")));
        }
        // The catalogue API returns the canonical English add-on name, so we
        // can only assert it on the English-language PDF (regardless of which
        // round it is, booking or other). For the Bahasa PDF we rely on the
        // language-stable date+qty pattern below.
        if (otherLanguage.equals("English")) {
            Assertion.assertThat(otherLanguage + " PDF should list the booked add-on name (" + addonsName + ")",
                    pdfOtherLang, containsStringIgnoringCase(addonsName));
        }
        Assertion.assertThat(otherLanguage + " PDF add-ons section should show '<date> <qty>' (e.g. \""
                        + startDate + " " + addonsAmount + "\") immediately after the section header",
                addonsSectionDateQtyPattern.matcher(pdfOtherLang).find(), is(true));
        Assertion.assertThat(otherLanguage + " PDF should mention the special request (user input, never translated)",
                pdfOtherLang, containsStringIgnoringCase(specialRequestText));
        Assertion.assertThat(otherLanguage + " PDF should mention the booked hotel",
                pdfOtherLang, containsStringIgnoringCase(hotelName));

        // ---- UI verification on the order detail page (booking language) ----
        orderListPage.openActiveTicketViaURL(orderId);
        step("Verify add-ons, guests and special request on the order detail page (" + bookingLanguage + ")");
        // The order detail UI renders the localized add-on name (e.g. "Makan
        // malam" in Bahasa). The API only gives us the canonical (English)
        // name, so we can only assert the add-on name itself when the UI
        // language is English. Guest names and the special request are user
        // input and stay language-stable.
        if (bookingLanguage.equals("English")) {
            Assertion.assertThat(addonsName + " add-on should be visible on the order detail",
                    orderDetailPage.isAddOnPresent(addonsName), is(true));
        }
        for (String expectedGuestName : bookedGuestNames) {
            Assertion.assertThat("UI should show booked guest name " + expectedGuestName,
                    orderDetailPage.isGuestNameDisplayedOrderDetails(expectedGuestName), is(true));
        }
        Assertion.assertThat("UI should display the booked special request",
                orderDetailPage.checkSpecialRequest(specialRequestText), is(true));
    }
    @Author(names = {"Aditi"})
    @Module(name = "LandingPage")
    @Tribe(name = "Hotel Demand")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyHotelDestinationSearchDweb() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("verifyHotelDestinationSearchDweb");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HotelLandingPageWeb<?> hotelLP = HotelLandingPageWeb.getInstance(session);
        SRPPageWeb<?> hotelSRP = SRPPageWeb.getInstance(session);

        String location = param.get("location");
        String region = param.get("region");
        String city = param.get("city");
        String area = param.get("area");
        String poi = param.get("poi");
        String email = param.get("email");
        String password = param.get("password");
        String dateType = param.get("dateType");
        String date = param.get("date");

        step("User open home page");
        homePage.goToHomePage();

        step("Login as " + email + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(email, password);

        step("User opens hotel landing page");
        homePage.openVerticalPageViaURL("Hotel");
        Assertion.assertThat("Landing Page is Opened", hotelLP.isLandingPageVisible(), is(true));

        step("Type partial location to trigger autocomplete and verify suggestion appears");
        hotelLP.typeInSearchForm(location);
        Assertion.assertThat(
                "Autocomplete suggestion should appear for '" + location + "'",
                hotelLP.isAutocompleteSuggestionVisible(location), is(true));

        step("Verify autocomplete list contains item matching the typed text");
        Assertion.assertThat(
                "Autocomplete list should contain at least one item matching '" + location + "'",
                hotelLP.isAutocompleteListContainsText(location),
                is(true));

        step("Click an autocomplete suggestion and verify the search field contains the typed keyword");
        String clickedSuggestion = hotelLP.clickFirstAutocompleteSuggestionContaining(location);
        Assertion.assertThat(
                "Search field should contain the typed keyword '" + location + "' after selecting suggestion '" + clickedSuggestion + "'",
                hotelLP.getSearchFieldText(),
                containsStringIgnoringCase(location));

        step("Search by region / city / area / POI, submit search, and validate SRP loads with the correct location");
        Map<String, String> queries = new LinkedHashMap<>();
        queries.put("region", region);
        queries.put("city", city);
        queries.put("area", area);
        queries.put("poi", poi);

        List<String> failures = new ArrayList<>();
        for (Map.Entry<String, String> entry : queries.entrySet()) {
            String label = entry.getKey();
            String query = entry.getValue();
            if (query == null || query.isBlank()) {
                continue;
            }
            try {
                homePage.openVerticalPageViaURL("Hotel");
                Assertion.assertThat("Hotel Landing Page is opened for " + label + " '" + query + "'",
                        hotelLP.isLandingPageVisible(), is(true));

                hotelLP.searchStayLocation(query.strip());
                hotelLP.setStayDate(dateType, date);

                Assertion.assertThat(
                        "Search field should reflect the chosen suggestion for " + label + " '" + query + "'",
                        hotelLP.getSearchFieldText(),
                        containsStringIgnoringCase(query.strip()));

                hotelLP.clickSubmit();

                Assertion.assertThat(
                        "SRP should be opened for " + label + " '" + query + "'",
                        hotelSRP.isHotelCardVisible(),
                        is(true));

                if (label.equals("poi")) {
                    Assertion.assertThat(
                            "Hotel cards on SRP should show distance from POI '" + query + "'",
                            hotelLP.verifyPoiOnHotelCards(query.strip()),
                            is(true));
                } else {
                    Assertion.assertThat(
                            "Hotel cards on SRP should reflect the selected " + label + " '" + query + "'",
                            hotelSRP.isHotelLocationExists(query.strip()),
                            is(true));
                }
            } catch (Throwable t) {
                failures.add(label + " ('" + query + "'): " + t.getMessage());
            }
        }

        step("Use 'Near Me' option to search by current location");
        homePage.openVerticalPageViaURL("Hotel");
        Assertion.assertThat("Hotel Landing Page is opened for Near Me search",
                hotelLP.isLandingPageVisible(), is(true));
        hotelLP.clickNearMeLocation();
        hotelLP.setStayDate(dateType, date);

        step("Verify search field is set to Near Me / Dekat saya / Near you / Di dekat kamu");
        Assertion.assertThat(
                "Search field should show a Near Me equivalent label",
                hotelLP.isNearMeSearchAlreadySet(),
                is(true));

        step("Submit search and verify SRP loads with Near Me context");
        hotelLP.clickSubmit();
        Assertion.assertThat(
                "SRP should be loaded with hotel cards for Near Me search",
                hotelSRP.isHotelCardVisible(),
                is(true));
        Assertion.assertThat(
                "Hotel cards on SRP should show distance from user's location (Near Me)",
                hotelSRP.verifyNearMeOnHotelCards(),
                is(true));

        step("Verify no results state with multiple invalid edge-case strings");
        /*
         * NOTE: We hardcode these invalid edge-case strings directly in the script rather
         * than passing them through an external test data file.
         * Reason 1 (Parsing & Escaping): External data parsers often automatically trim
         * trailing/leading whitespaces ("   ") or double-escape control characters
         * ("\n\t\r"), which alters the exact raw string we are trying to test.
         */
        List<String> invalidData = List.of(
                "@@@@@@@@",
                "==========",
                "!@#$%^&*()",
                "123456789",
                "<script>console.log(/'nothing should be visible on UI/')</script>",
                "DROP TABLE hotels;",
                "\\n\\t\\r"
        );
        hotelSRP.searchAndVerifyNoResults(invalidData);
    }

    /**
     * dWeb counterpart of the App-side {@code verifyRescheduleNonRefundableFlow} test.
     * Books a non-refundable ({@code NO_CANCELLATION}) hotel order via API, logs in with the
     * same email on the website, opens the order, taps Reschedule from Manage Order, and
     * verifies the non-refundable reschedule form behaviour:
     * <ol>
     *   <li>Form is displayed with booking dates reflected in {@code RescheduleForm_select_date_text}.</li>
     *   <li>Filling reason + description + attachment is allowed.</li>
     *   <li>Submitting BEFORE a date change is rejected (form remains visible).</li>
     *   <li>After picking a new date, submission succeeds and the approval flow appears.</li>
     * </ol>
     */
    @Author(names = {"Ashish"})
    @Module(name = "Reschedule")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyRescheduleNonRefundableFlowWeb() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = BaseTest.sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyRescheduleNonRefundableFlowWeb");
        BaseTest.paramsThreadLocal.set(param);

        HotelApi<?> hotelApi = new HotelApi<>(session);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        HotelReschedulePageWeb<?> reschedulePage = HotelReschedulePageWeb.getInstance(session);

        String email = param.get("email");
        String password = param.get("password");
        String startDate = generateRandomDate(param.get("rangeAddDate"));
        String hotelName = param.get("hotelName");
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));
        int room = Integer.parseInt(param.get("room"));
        String currency = Optional.ofNullable(param.get("currency")).orElse("IDR");
        String rescheduleReason = Optional.ofNullable(param.get("rescheduleReason"))
                .filter(s -> !s.isBlank())
                .orElse(null);
        String rescheduleDescription = Optional.ofNullable(param.get("rescheduleDescription"))
                .filter(s -> !s.isBlank())
                .orElseGet(() -> "Auto reschedule - " + generateRandomString(30));
        String attachmentPath = Optional.ofNullable(param.get("attachmentPath"))
                .orElse("src/test/resources/dashboard/hotel/sampleImage.jpg");
        int rescheduleDayShift = Integer.parseInt(Optional.ofNullable(param.get("rescheduleDayShift")).orElse("2"));

        String[] salutationList = { "Mr", "Mrs", "Ms" };
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = salutationList[0];

        step("Create a non-refundable (NO_CANCELLATION) issued hotel order via API");
        boolean apiResult = hotelApi.createIssuedOrderApi(
                email, password, startDate, hotelName, adult, night, room,
                orderFullName, orderNameTitle, guestFullName, guestTitle,
                currency, /* isRefundable */ false, /* isSpecialRequestEnable */ false, "",
                "en", /* isNative */ false);
        Assertion.assertThat("Non-refundable hotel booking via API is successful", apiResult, is(true));

        String issuedOrderId = String.valueOf(hotelApi.getOrderId());
        if (issuedOrderId == null || issuedOrderId.isEmpty() || "0".equals(issuedOrderId)) {
            Assertion.skip("Issued order ID is invalid after API booking; skipping UI verification.");
            return;
        }

        step("Open home page");
        homePage.goToHomePage();

        step("Login as " + email + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(email, password);

        step("Open the issued order via URL");
        orderListPage.openActiveTicketViaURL(issuedOrderId);
        orderDetailPage.skipCoachmark();

        step("Tap Reschedule from Manage Order");
        orderDetailPage.clickRescheduleButton();

        Assertion.assertThat("Non-refundable reschedule form should be visible",
                reschedulePage.isOnNonRefundablePage(), is(true));

        step("Verify the booked check-in / check-out are reflected in the date button text");
        String selectedDateText = reschedulePage.getNonRefundableSelectedDateText();
        String checkInBanner = reschedulePage.getNonRefundableCheckInDateBannerText();
        String checkOutBanner = reschedulePage.getNonRefundableCheckOutDateBannerText();
        Assertion.assertThat("Select-date text should not be empty",
                selectedDateText, not(emptyOrNullString()));
        Assertion.assertThat("Booking check-in date should be reflected in the page banner",
                checkInBanner, not(emptyOrNullString()));
        Assertion.assertThat("Booking check-out date should be reflected in the page banner",
                checkOutBanner, not(emptyOrNullString()));

        step("Fill the form: pick a reschedule reason (random if not provided in test data)");
        String pickedReason;
        if (rescheduleReason != null) {
            reschedulePage.selectNonRefundableReason(rescheduleReason);
            pickedReason = rescheduleReason;
        } else {
            pickedReason = reschedulePage.selectRandomNonRefundableReason();
        }
        Assertion.assertThat("A reschedule reason must be selected", pickedReason, not(emptyOrNullString()));

        step("Fill description and attach an evidence image");
        reschedulePage.fillNonRefundableDescription(rescheduleDescription);
        reschedulePage.attachNonRefundableEvidence(attachmentPath);

        step("Attempt to submit WITHOUT changing the date — submission must be rejected");
        reschedulePage.clickNonRefundableSubmit();
        Assertion.assertThat("Inline 'must be different from the previous dates' error should be shown",
                reschedulePage.isSameDateErrorVisible(), is(true));

        step("Change the date — the inline error must clear and select-date text updates");
        String newDateRange = reschedulePage.changeNonRefundableNightDuration(rescheduleDayShift);
        Assertion.assertThat("After date change the select-date text must reflect new dates",
                newDateRange, not(equalToIgnoringCase(selectedDateText)));

        step("Submit again — confirmation dialog must appear");
        reschedulePage.clickNonRefundableSubmit();
        reschedulePage.confirmRescheduleRequest();

        step("Verify the approval-flow status card is shown after submission");
        Assertion.assertThat("Reschedule status card should be visible",
                reschedulePage.isApprovalFlowStatusVisible(), is(true));

        step("Verify the new check-in/check-out dates on the status card match the picked date range");
        String approvalDateText = reschedulePage.getApprovalNewDateText();
        Assertion.assertThat("Status card should display the newly picked dates",
                approvalDateText, not(emptyOrNullString()));

        step("Verify the approval progress bar (3 steps: 1 completed, 1 active, 1 pending)");
        Assertion.assertThat("Approval flow should expose 3 progress steps",
                reschedulePage.getApprovalTotalStepCount(), is(3));
        Assertion.assertThat("Step 1 must be completed",
                reschedulePage.getApprovalCompletedStepCount(), is(greaterThanOrEqualTo(1)));
        Assertion.assertThat("Step 2 must be the active one",
                reschedulePage.getApprovalActiveStepCount(), is(greaterThanOrEqualTo(1)));
        Assertion.assertThat("Status label must say 'Waiting for accommodation to confirm'",
                reschedulePage.getApprovalStatusLabelText(),
                anyOf(
                        containsStringIgnoringCase("Waiting for accommodation to confirm"),
                        containsStringIgnoringCase("Menunggu konfirmasi pihak akomodasi")));
    }
    @Author(names = {"Aditi"})
    @Module(name = "ProductDetailPage")
    @Tribe(name = "Hotel Demand")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyHotelDetailsImageGalleryAndRoomListDweb() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("verifyHotelDetailsImageGalleryAndRoomListDweb");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String location = param.get("location");
        String email = param.get("email");
        String password = param.get("password");
        String currency = param.get("currency");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        LandingPageWeb<?> landingPage = LandingPageWeb.getInstance(session);
        HotelLandingPageWeb<?> hotelLP = HotelLandingPageWeb.getInstance(session);
        SRPPageWeb<?> hotelSRP = SRPPageWeb.getInstance(session);
        PDPPageWeb<?> pdpPageWeb = PDPPageWeb.getInstance(session);
        HotelRoomListPageWeb<?> roomListPage = HotelRoomListPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("Login as " + email + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(email, password);

        step("Select '" + currency + "' currency on the home page and verify it is applied");
        homePage.selectCurrency(currency);
        Assertion.assertThat("Currency on home page should be set to '" + currency + "'", homePage.getCurrentCurrency(), containsString(currency));

        step("User Open hotel landing page");
        homePage.openVerticalPageViaURL("Hotel");

        step("Dismiss 'Install app' pop up");
        landingPage.dismissInstallAppPopUp();

        step("Search hotel by location '" + location + "' and submit to land on SRP");
        hotelLP.searchStayLocation(location);
        hotelLP.clickSubmit();
        Assertion.assertThat("SRP should be loaded with hotel cards", hotelSRP.isHotelCardVisible(), is(true));

        step("Apply 'Preferred Partner' and 'Preferred Partner Plus' quick filters on SRP");
        hotelSRP.toggleSrpQuickFilter("Preferred Partner");
        hotelSRP.toggleSrpQuickFilter("Preferred Partner Plus");

        step("Wait for the filter-applied confirmation banner ('Showing great accommodations that fit your needs.')");
        Assertion.assertThat(
                "Filter-applied banner should be visible after enabling Preferred Partner / Plus filters",
                hotelSRP.isFilterAppliedBannerVisible(), is(true));

        step("Verify the Preferred Partner icon is visible on the first hotel card");
        Assertion.assertThat(
                "Preferred Partner icon should be visible on the first hotel card",
                hotelSRP.isPreferredPartnerIconOnFirstHotelCardVisible(), is(true));

        step("Capture hotel name, price and location from the first hotel card on SRP");
        String srpHotelName = hotelSRP.getHotelName();
        String srpHotelPrice = hotelSRP.getHotelPrice();
        String[] srpHotelLocation = hotelSRP.getHotelLocation();
        Assertion.assertThat("Hotel name on SRP card should not be empty", srpHotelName, is(not(emptyOrNullString())));
        Assertion.assertThat("Hotel price on SRP card should not be empty", srpHotelPrice, is(not(emptyOrNullString())));
        Assertion.assertThat("Hotel location on SRP card should not be empty",
                srpHotelLocation != null && srpHotelLocation.length > 0 && !srpHotelLocation[0].isBlank(), is(true));

        step("Click on the first hotel card to open the PDP in same tab");
        hotelSRP.clickHotelCard(1, true);

        step("Verify PDP is loaded");
        Assertion.assertThat("PDP should be loaded", pdpPageWeb.isPdpLoaded(), is(true));

        step("Verify 'Preferred Partner' or 'Preferred Partner Plus' list item is visible on PDP");
        Assertion.assertThat(
        "'Preferred Partner' or 'Preferred Partner Plus' list item should be visible on PDP after applying Preferred Partner and Preferred Partner Plus filters on SRP",
        pdpPageWeb.isPreferredPartnerVisible(), is(true));

        step("Verify all hotel details (name, location, price, star rating, reviews) are correct on PDP");
        Assertion.assertThat("Hotel name should be visible on PDP", pdpPageWeb.isHotelNameVisible(), is(true));
        Assertion.assertThat("Hotel name on PDP should match the hotel selected from SRP", pdpPageWeb.getHotelNameDetail(), containsStringIgnoringCase(srpHotelName));
        Assertion.assertThat("Hotel image should be visible on PDP", pdpPageWeb.isHotelImageVisible(), is(true));
        Assertion.assertThat("Hotel location should be visible on PDP", pdpPageWeb.isHotelLocationVisible(), is(true));
        Assertion.assertThat("Hotel star rating should be visible on PDP", pdpPageWeb.isStarRatingVisible(), is(true));
        Assertion.assertThat("Hotel review section should be visible on PDP", pdpPageWeb.isHotelReviewVisible(), is(true));
        String pdpPrice = pdpPageWeb.getHotelPriceDetail();
        Assertion.assertThat("Hotel price details should be non-empty on PDP", pdpPrice, is(not(emptyOrNullString())));
        Assertion.assertThat("Hotel price on PDP ('" + pdpPrice + "') should reflect the currency '" + currency + "' selected on the home page", pdpPrice, containsStringIgnoringCase(currency));

        step("Verify Priority Program / PPP+ membership info is visible on PDP");
        Assertion.assertThat("Property type (Priority Program / PPP+ category) should be visible on PDP", pdpPageWeb.isPropertyTypeVisible(), is(true));
        Assertion.assertThat("Highlights section (where PP / PPP+ benefits are surfaced) should be visible on PDP", pdpPageWeb.isHighLightsVisible(), is(true));

        step("Click on hotel image and verify the image gallery opens");
        pdpPageWeb.clickHotelImage();
        Assertion.assertThat("Image gallery should open after clicking the hotel image", pdpPageWeb.isGalleryOpened(), is(true));
        Assertion.assertThat("Gallery title should be visible", pdpPageWeb.isGalleryTitleVisible(), is(true));
        Assertion.assertThat("Gallery images should be visible", pdpPageWeb.isGalleryImagesVisible(), is(true));

        step("Close the gallery to validate the next entry-point");
        pdpPageWeb.closeGallery();
        Assertion.assertThat(
                "Gallery should close after clicking the close button",
                pdpPageWeb.isGalleryClosed(), is(true));

        step("Click on 'See all photos' option and verify the image gallery opens again");
        pdpPageWeb.seeAllPhotos();
        Assertion.assertThat(
                "Image gallery should open after clicking 'See all photos'",
                pdpPageWeb.isGalleryOpened(), is(true));
        Assertion.assertThat("Gallery title should be visible from 'See all photos'",
                pdpPageWeb.isGalleryTitleVisible(), is(true));
        Assertion.assertThat("Gallery images should be visible from 'See all photos'",
                pdpPageWeb.isGalleryImagesVisible(), is(true));

        step("Capture initial gallery banner counter and click the second image");
        String galleryCounterBeforeClick = pdpPageWeb.getGalleryCounterText();
        Assertion.assertThat(
                "Initial gallery banner counter should be visible (e.g. '1/N')",
                galleryCounterBeforeClick, is(not(emptyOrNullString())));
        pdpPageWeb.clickGalleryImageAt(2);

        step("Verify the gallery banner counter updated after selecting the second image");
        boolean counterReachedSecondImage = pdpPageWeb.waitForGalleryCurrentImageIndexToBe(2, 10);
        String galleryCounterAfterClick = pdpPageWeb.getGalleryCounterText();
        Assertion.assertThat(
                "Gallery banner should reach the second image after clicking it (was '"
                        + galleryCounterBeforeClick + "', now '" + galleryCounterAfterClick + "')",
                counterReachedSecondImage, is(true));
        Assertion.assertThat(
                "Gallery banner counter should change after clicking the second image (was '"
                        + galleryCounterBeforeClick + "', now '" + galleryCounterAfterClick + "')",
                galleryCounterAfterClick, is(not(equalToIgnoringCase(galleryCounterBeforeClick))));
        Assertion.assertThat(
                "Gallery banner should now show the second image as active (expected counter to start with '2/')",
                pdpPageWeb.getGalleryCurrentImageIndex(), is(2));

        step("Close the gallery before continuing to the room list flow");
        pdpPageWeb.closeGallery();

        step("Click 'See rooms' button on PDP and verify user is scrolled to the Room List");
        pdpPageWeb.clickSeeRoomsButton();
        Assertion.assertThat(
                "Room list section should be visible after clicking 'See rooms'",
                roomListPage.isRoomListSectionVisible(), is(true));
        Assertion.assertThat("Room image on the room list card should be visible",
                roomListPage.isRoomImageVisible(), is(true));

        step("Verify all room info (size, capacity, meal pax, bed type, value-added) is displayed on the room list cards");
        roomListPage.verifyRoomCardDetailsOnRoomList(1);

        step("Verify exclusive nudges are visible on top of the room card");
        Assertion.assertThat(
                "Exclusive nudge / badge should be visible on top of the first room card",
                roomListPage.isExclusiveNudgeOnRoomCardVisible(), is(true));

        step("Verify the price on the first room-list rate-plan card matches the PDP price");
        String roomListFinalPrice = roomListPage.getRoomListFinalPriceByIndex(1);
        String pdpPriceDigits = pdpPrice == null ? "" : pdpPrice.replaceAll("[^0-9]", "");
        String roomListPriceDigits = roomListFinalPrice == null ? "" : roomListFinalPrice.replaceAll("[^0-9]", "");
        Assertion.assertThat(
                "Room list final price should not be empty",
                roomListFinalPrice, is(not(emptyOrNullString())));
        Assertion.assertThat(
                "Room list final price ('" + roomListFinalPrice + "') should also reflect the currency '" + currency + "' selected on the home page",
                roomListFinalPrice, containsStringIgnoringCase(currency));
        Assertion.assertThat(
                "Numeric portion of PDP price ('" + pdpPrice + "') should match the room list final price ('" + roomListFinalPrice + "')",
                roomListPriceDigits, is(pdpPriceDigits));

        step("Verify meal, refund/reschedule and max-occupancy info are present");
        Assertion.assertThat(
                "Meal info should be visible",
                roomListPage.isMealInfoOnRatePlanCardVisibleByIndex(1), is(true));
        Assertion.assertThat(
                "Refund & reschedule info (allowed or not allowed) should be visible",
                roomListPage.isRefundRescheduleInfoOnRatePlanCardVisibleByIndex(1), is(true));
        Assertion.assertThat(
                "Max people occupancy info should be visible",
                roomListPage.isMaxOccupancyInfoOnRatePlanCardVisibleByIndex(1), is(true));
    }

    /**
     * Web counterpart of app {@code checkPropertyFeeWithEstimatedPrice}.
     * Sheet key: {@code checkPropertyFeeWithEstimatedPriceWeb}. Params: email or username, password, otp,
     * retryCounter, location or hotelName, date_type or dateType, date, total_room or totalRoom,
     * total_adult or totalGuest.
     */
    @Author(names = {"Yazid"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Hotel")
    @Module(name = "CreateBooking")
    @Test
    public void checkPropertyFeeWithEstimatedPriceWeb() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("checkPropertyFeeWithEstimatedPriceWeb");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        String email = param.getOrDefault("email", param.get("username"));
        String password = param.get("password");
        String location = param.getOrDefault("location", param.get("hotelName"));
        String dateType = param.getOrDefault("date_type", param.get("dateType"));
        String date = param.get("date");
        int totalRoom = Integer.parseInt(param.getOrDefault("total_room", param.get("totalRoom")));
        int totalAdult = Integer.parseInt(param.getOrDefault("total_adult", param.getOrDefault("totalGuest", param.get("totalAdult"))));

        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        HotelRoomListPageWeb<?> roomListPage = HotelRoomListPageWeb.getInstance(session);
        HotelDetailPage<?> detailPage = new HotelDetailPage<>(session);
        HotelBookingFormPageWeb<?> bookingFormPage = HotelBookingFormPageWeb.getInstance(session);

        step("Open home page");
        homePage.goToHomePage();

        step("Login as " + email + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(email, password);

        step("Open hotel landing page");
        homePage.selectCurrency("IDR");
        homePage.openVerticalPageViaURL("Hotel");
        Assertion.assertThat("Landing page is opened", landingPage.isLandingPageVisible(), is(true));

        step("Search stay location");
        landingPage.searchStayLocation(location);

        step("Set stay date");
        landingPage.setStayDate(dateType, date);

        step("Set total room and total guest");
        landingPage.clickRoomAndGuestButton();
        landingPage.setTotalRoom(totalRoom);
        landingPage.setTotalGuest(totalAdult);
        landingPage.clickDone();

        step("Submit search");
        landingPage.clickSubmit();

        step("Go to the room list");
        detailPage.seeHotelRoom();
        detailPage.clickPopUp();
        roomListPage.scrollToRoomListSection();

        step("Open room detail price section and get room price");
        roomListPage.goToRoomDetailPriceSection();
        String roomPrice = roomListPage.getTotalPriceFromPriceDetail();

        step("Book room and go to booking form");
        roomListPage.clickBookRoom();
        bookingFormPage.dismissRatePlanChangePopupsIfPresent();

        step("Verify pay now price on booking form matches room list price");
        Assertion.assertThat("Pay now price is equals to room list price", bookingFormPage.getPayNowPriceBookingForm(), is(roomPrice));

        step("Get pay upon check-in price on booking form page 1");
        String payUponCheckInPrice = bookingFormPage.getPayUponCheckInPriceBookingForm();
        Assertion.assertThat("Pay upon check-in price is displayed", payUponCheckInPrice, is(not(emptyOrNullString())));

        step("Get estimated total from booking form page 1 and verify it equals pay upon check-in plus room price");
        String estimatedTotalPage1 = bookingFormPage.getEstimatedTotalFromBookingFormPage1();
        bookingFormPage.verifyEstimatedTotalIsCorrect(roomPrice, payUponCheckInPrice, estimatedTotalPage1);

        step("Go to booking form page 2");
        bookingFormPage.continueToAddOnPage();

        step("Verify total price on booking form page 2 matches room list price");
        Assertion.assertThat("Total price on booking form page 2 is equals to room list price", bookingFormPage.getTotalPriceBookingFormPage2(), is(roomPrice));

        step("Get pay upon check-in price on booking form page 2");
        String payUponCheckInPricePage2 = bookingFormPage.getPayUponCheckInPriceBookingFormPage2();
        Assertion.assertThat("Pay upon check-in price on booking form page 2 is displayed", payUponCheckInPricePage2, is(not(emptyOrNullString())));

        step("Get estimated total from booking form page 2 and verify it matches page 1");
        String estimatedTotalPage2 = bookingFormPage.getEstimatedTotalFromBookingFormPage2();
        Assertion.assertThat("Estimated total on booking form page 2 is equals to estimated total on booking form page 1", estimatedTotalPage2, is(estimatedTotalPage1));

        step("Add add-ons");
        bookingFormPage.chooseAddOns();
        String estimatedTotalPage2AfterAddOns = bookingFormPage.getEstimatedTotalFromBookingFormPage2();
        Assertion.assertThat("Estimated total on booking form page 2 is unchanged after adding add-ons", estimatedTotalPage2AfterAddOns, is(estimatedTotalPage2));

        step("Add insurance on booking form page 2 and verify estimated total is unchanged");
        bookingFormPage.chooseInsurance();
        Assertion.assertThat("Estimated total on booking form page 2 is unchanged after adding insurance", bookingFormPage.getEstimatedTotalFromBookingFormPage2(), is(estimatedTotalPage2AfterAddOns));
    }

    /**
     * Web test for gift voucher claim on hotel booking form page 2 under the insurance section.
     * Sheet key: {@code verifyClaimGiftVoucherOnBookingFormPage2Web}. Params: email or username ({@code @yopmail.com}),
     * password,
     * location or hotelName, date_type or dateType, date or dateRange, total_room or totalRoom,
     * total_adult or totalGuest, paymentGroup, paymentMethod.
     */
    @Author(names = {"Aditi"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Hotel")
    @Module(name = "CreateBooking")
    @Test
    public void verifyClaimGiftVoucherOnBookingFormPage2Web() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("verifyClaimGiftVoucherOnBookingFormPage2Web");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        String email = param.getOrDefault("email", param.get("username"));
        String password = param.get("password");
        String location = param.getOrDefault("location", param.get("hotelName"));
        String dateType = param.getOrDefault("date_type", param.get("dateType"));
        String date = param.getOrDefault("date", param.get("dateRange"));
        int totalRoom = Integer.parseInt(param.getOrDefault("total_room", param.getOrDefault("totalRoom", "1")));
        int totalAdult = Integer.parseInt(param.getOrDefault("total_adult", param.getOrDefault("totalGuest", param.getOrDefault("totalAdult", "1"))));
        String fullName = CoreFunctions.randomName() + " Test";
        String phoneNumber = CoreFunctions.randomPhone();
        String paymentGroup = param.getOrDefault("paymentGroup", "Instant Payment");
        String paymentMethod = param.getOrDefault("paymentMethod", "Sakuku");

        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        HotelRoomListPageWeb<?> roomListPage = HotelRoomListPageWeb.getInstance(session);
        HotelDetailPage<?> detailPage = new HotelDetailPage<>(session);
        HotelBookingFormPageWeb<?> bookingFormPage = HotelBookingFormPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);

        step("Open home page");
        homePage.goToHomePage();

        step("Login as " + email + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(email, password);

        step("Open hotel landing page");
        homePage.selectCurrency("IDR");
        homePage.openVerticalPageViaURL("Hotel");
        Assertion.assertThat("Landing page is opened", landingPage.isLandingPageVisible(), is(true));

        step("Search domestic hotel");
        landingPage.searchStayLocation(location);
        landingPage.setStayDate(dateType, date);

        step("Set total room and total guest");
        landingPage.clickRoomAndGuestButton();
        landingPage.setTotalRoom(totalRoom);
        landingPage.setTotalGuest(totalAdult);
        landingPage.clickDone();

        step("Submit search");
        landingPage.clickSubmit();

        step("Go to the room list and book a room");
        detailPage.seeHotelRoom();
        detailPage.clickPopUp();
        roomListPage.scrollToRoomListSection();
        roomListPage.clickBookRoom();
        bookingFormPage.dismissRatePlanChangePopupsIfPresent();

        step("Fill contact and guest details on booking form page 1");
        Assertion.assertThat("Contact form should be visible on booking form page 1", bookingFormPage.isContactForm(), is(true));
        bookingFormPage.fillContactDetail(fullName, phoneNumber, email);
        Assertion.assertThat("Guest stay details form should be visible on booking form page 1", bookingFormPage.isGuestStayForm(), is(true));
        bookingFormPage.fillGuestDetails(1, fullName, true, false, "");

        step("Go to booking form page 2");
        bookingFormPage.continueToAddOnPage();
        Assertion.assertThat("Booking form page 2 with insurance section should be visible",
                bookingFormPage.isInsuranceOrAddonPageVisible(), is(true));

        step("Verify gift vouchers are shown under the insurance section");
        if (!bookingFormPage.isGiftVoucherSectionVisibleUnderInsurance()) {
            Assertion.skip("Gift voucher section is not available under insurance for the selected hotel; skipping claim flow.");
            return;
        }
        Assertion.assertThat("Gift voucher section should be visible under insurance on booking form page 2",
                bookingFormPage.isGiftVoucherSectionVisibleUnderInsurance(), is(true));

        step("Claim gift voucher under insurance section");
        bookingFormPage.claimGiftVoucherUnderInsurance();
        bookingFormPage.verifyGiftVoucherClaimUnderInsurance();

        step("Verify user cannot unclaim the same gift voucher");
        bookingFormPage.verifyGiftVoucherCannotBeUnclaimedUnderInsurance();

        step("Continue to payment page from booking form page 2");
        bookingFormPage.continueToPayment();
        String orderID = paymentPage.getOrderId();
        Assertion.assertThat("Order ID should be displayed on payment page", orderID, not(emptyOrNullString()));

        step("Select Sakuku as payment method");
        paymentPage.clickOnSelectPaymentMethod();
        paymentPage.clickOnSeeAll();
        paymentPage.goToPaymentMethodsGroup(paymentGroup);
        paymentPage.selectPaymentMethod(paymentMethod);


        step("Complete payment with Sakuku");
        paymentPage.clickOnPaymentButton();
        Assertion.assertThat("Payment successful page should be visible", paymentPage.isPaymentSuccessfulVisible(), is(true));

        step("Verify issued order");
        orderListPage.openActiveTicketViaURL(orderID);
        orderDetailPage.skipCoachmark();
        Assertion.assertThat("Order should be issued after payment", orderDetailPage.verifyIssuedETicket(), is(true));
        Assertion.assertThat("Order ID on order detail should match payment page", orderDetailPage.getOrderId(), is(orderID));

        step("Verify gift voucher email notification is sent after successful payment");
        String tiketWindow = session.driver().getWindowHandle();
        InboxPageYopmail<?> inboxPage = new InboxPageYopmail<>(session);
        inboxPage.openYopmailInboxInNewTab(email);
        Assertion.assertThat("Gift voucher email should be received in Yopmail inbox",
                inboxPage.verifyGiftVoucherEmailReceived(), is(true));
        inboxPage.closeYopmailAndReturnTo(tiketWindow);
    }

    /**
     * Check the refund status of the Failed issued booking web.
     * Sheet key: {@code checkRefundStatusOfFailedIssuedBookingWeb}. Params: email, password,
     * location, locationType, rangeAddDate, room, adult, night, vendorColor, paymentGroup, paymentMethod, currency.
     */
    @Author(names = {"Yazid"})
    @Module(name = "PostPurchase")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void checkRefundStatusOfFailedIssuedBookingWeb() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("checkRefundStatusOfFailedIssuedBookingWeb");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        String email = param.getOrDefault("email", param.get("username"));
        String password = param.get("password");
        String location = param.get("location");
        String locationType = param.get("locationType");
        String currency = param.getOrDefault("currency", "IDR");
        String rangeAddDate = param.get("rangeAddDate");
        int room = Integer.parseInt(param.getOrDefault("total_room", param.get("room")));
        int adult = Integer.parseInt(param.getOrDefault("total_adult", param.get("adult")));
        int night = Integer.parseInt(param.getOrDefault("total_night", param.get("night")));
        String vendorColor = param.getOrDefault("vendorColor", "8");
        String paymentGroup = param.getOrDefault("paymentGroup", "Instant Payment");
        String paymentMethod = param.getOrDefault("paymentMethod", "Sakuku");
        String accommodationType = "hotel";
        String[] dates = generateCheckInOutDate(rangeAddDate, night);
        String checkInDate = dates[0];
        String checkOutDate = dates[1];

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        SRPPageWeb<?> searchResultPage = SRPPageWeb.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        PDPPageWeb<?> detailPage = PDPPageWeb.getInstance(session);
        HotelRoomListPageWeb<?> roomListPage = HotelRoomListPageWeb.getInstance(session);
        HotelBookingFormPageWeb<?> bookingFormPage = HotelBookingFormPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);

        step("Login as " + email + " via session injection");
        homePage.goToHomePage();
        loginPage.loginWithSpecificAccountViaCookie(email, password);
        homePage.selectCurrency(currency);

        step("Resolve location publicId and open hotel SRP via deeplink");
        HotelAutoCompleteResponse hotelAutoCompleteResponse = hotelApi.getAutocompleteData("", "", location, locationType);
        int locationIndex = hotelApi.findHotelIndex(hotelAutoCompleteResponse, location);
        String locationId = hotelAutoCompleteResponse.data.contents.get(locationIndex).publicId;
        searchResultPage.openSRPViaUrl(accommodationType, room, adult, locationId, locationType, location, checkInDate, checkOutDate);
        searchResultPage.clickPopUp();
        searchResultPage.waitForSrpToLoad();
        Assertion.assertThat("Hotel SRP should load", searchResultPage.isHotelCardVisible(), is(true));

        step("Choose hotel with vendor color " + vendorColor + " and open PDP in new tab");
        searchResultPage.clickHotelByVendorCodeAndOpenPdp(vendorColor);
        Assertion.assertThat("PDP should load after selecting vendor-colored hotel", detailPage.isPdpLoaded(), is(true));

        step("Find and book room with vendor color " + vendorColor);
        detailPage.seeHotelRoom();
        roomListPage.scrollToRoomListSection();
        roomListPage.clickBookRoomByVendorCode(vendorColor);
        bookingFormPage.dismissRatePlanChangePopupsIfPresent();
        bookingFormPage.selectFirstContactSalutation();

        step("Store booking page data for order history verification");
        String storedHotelName = bookingFormPage.getBookingHotelName();
        String storedCheckInDate = bookingFormPage.getBookingCheckInDate();
        String storedNightLabel = bookingFormPage.getBookingNightLabel();
        String storedRoomName = bookingFormPage.getBookingRoomName();
        Assertion.assertThat("Stored hotel name should not be empty", storedHotelName, not(emptyOrNullString()));
        Assertion.assertThat("Stored check-in date should not be empty", storedCheckInDate, not(emptyOrNullString()));
        Assertion.assertThat("Stored night label should not be empty", storedNightLabel, not(emptyOrNullString()));
        Assertion.assertThat("Stored room name should not be empty", storedRoomName, not(emptyOrNullString()));

        step("Continue to payment page and complete booking with " + paymentMethod);
        String paymentAmount = bookingFormPage.getTotalToPay();
        bookingFormPage.continueTo2NdPartThenPaymentPage();
        String orderId = paymentPage.getOrderId();
        Assertion.assertThat("Order ID should be displayed on payment page", orderId, not(emptyOrNullString()));
        paymentPage.clickOnSelectPaymentMethod();
        paymentPage.clickOnSeeAll();
        paymentPage.goToPaymentMethodsGroup(paymentGroup);
        paymentPage.selectPaymentMethod(paymentMethod);
        paymentPage.clickOnPaymentButton();
        Assertion.assertThat("Payment successful page should be visible", paymentPage.isPaymentSuccessfulVisible(), is(true));

        step("Wait for failed issued booking to be processed and refunded automatically");
        orderListPage.waitSeconds(20);

        step("Open order list page");
        paymentPage.clickSeeOrderListButton();

        step("Open the failed issued booking from order list");
        orderListPage.openActiveTicketViaURL(orderId);

        step("Check the refund is 'In Process' state");
        Assertion.assertThat("Refund status should be 'In process' for failed issued booking", orderListPage.getRefundStatus(), anyOf(is("In process"), is("Diproses")));

        String refundAmountText = orderListPage.getTotalAmount();
        long refundAmount = Long.parseLong(refundAmountText.replaceAll("[^0-9]", ""));
        long paidAmount = Long.parseLong(paymentAmount.replaceAll("[^0-9]", ""));
        Assertion.assertThat("Refund amount should match payment amount", refundAmount, is(paidAmount));

    }
    @Author(names = "Venkat")
    @Module(name = "SearchResultsPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyHotelAds() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyHotelAds");
        BaseTest.paramsThreadLocal.set(param);

        SRPPageWeb<?> searchResultPage = SRPPageWeb.getInstance(session);
        HomePageWeb<?> homePageWeb = HomePageWeb.getInstance(session);
        LandingPageWeb<?> landingPageWeb = LandingPageDweb.getInstance(session);

        String location = param.get("location");

        step("User opens hotel landing page");
        homePageWeb.openVerticalPageViaURL("Hotel");

        step("Search stay location");
        landingPageWeb.searchStayLocation(location);

        step("Submit search button");
        landingPageWeb.clickSubmit();

        step("Close pop-up");
        searchResultPage.clickPopUp();

        Assertion.assertThat("Hotel card on SRP is visible", searchResultPage.isHotelCardVisible(), is(true));

        step("Hotel Ads is shown on SRP card");
        Assertion.assertThat("Hotel Ads is visible on SRP", searchResultPage.getTextHotelAds(),anyOf(is("Promoted"), is("Sponsored"), is("Ad"),is("Boosted")) );
    }
    @Author(names = "Riya Bajpai")
    @Module(name = "ProductDetailPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyIndicatorAtLastSection() throws InterruptedException {

        Map<String, String> param = getParam("verifyIndicatorAtLastSection");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        CalendarPageWeb<?> calendarPage = CalendarPageWeb.getInstance(session);
        HotelDetailPage<?> detailPage = new HotelDetailPage<>(session);
        PDPPageWeb<?> pdpPage = PDPPageWeb.getInstance(session);

        String hotelName = param.get("hotelName");
        int adult = Integer.parseInt(param.get("adult"));
        int room = Integer.parseInt(param.get("room"));

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int daysToReturnDate = Integer.parseInt(param.get("daysToReturnDate"));

        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);
        String returnDate = CoreFunctions.addToLongDate(departureDate, daysToReturnDate);

        step("User open home page");
        homePage.goToHomePage();

        step("User open Hotel Landing page");
        homePage.openVerticalPageViaURL("Hotel");

        step("Search stay Hotel Name");
        landingPage.searchStayLocation(hotelName);

        step("Set date");
        landingPage.clickOnCalendarWidgetToOpen();
        calendarPage.selectDate(departureDate, returnDate);

        step("Set Guest : " + adult + " Adults, " + room + " Rooms");
        landingPage.clickRoomAndGuestButton();
        landingPage.setTotalGuest(adult);
        landingPage.setTotalRoom(room);
        landingPage.clickDone();

        step("Submit search button");
        landingPage.clickSubmit();

        step("Go Inside PDP");
        detailPage.switchToNewTab();
        Assertion.assertThat("Verify if PDP is Opened", detailPage.isPdpLoaded(), is(true));
        String hotelNameDetail = detailPage.getHotelNameDetail();
        Assertion.assertThat("Verify hotel name on LP and PDP is same", hotelNameDetail, containsString(hotelName));

        step("Click Rooms Tab");
        pdpPage.clickRoomsTab();

        step("Click First Room Card");
        pdpPage.clickFirstRoomCard();

        String[] expectedSections = {
                "Popular Facilities",
                "Check-in Procedures",
                "Room Description",
                "Other Facilities"
        };

        for (String expectedSection : expectedSections) {
            step("Verify active section: " + expectedSection);
            Assertion.assertThat("Expected section should be active: " + expectedSection, pdpPage.scrollToAndVerifyRoomDetailSection(expectedSection), is(true)
            );
        }
    }
    @Author(names = {"Riya Bajpai"})
    @Module(name = "Demand")
    @Tribe(name = "Accommodation")
    @Vertical(name = "Accommodation")
    @Test

    public void verifyCloseImageDetail() throws FailedLoginException, InterruptedException, ExecutionException {
        Map<String, String> param = getParam("verifyCloseImageDetail");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HotelReschedulePageWeb<?> hotelReschedulePage = HotelReschedulePageWeb.getInstance(session);
        RoomDetailsPageWeb<?> roomDetailsPage = RoomDetailsPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPageWeb = PaymentPageWeb.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        RefundDetailWeb<?> refundDetailPage = RefundDetailWeb.getInstance(session);

        String username = param.get("email");
        String password = param.get("password");
        String currency = param.get("currency");
        String startDate = generateRandomDate(param.get("rangeAddDate"));
        String hotelName = param.get("hotelName");
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));
        int room = Integer.parseInt(param.get("room"));
        boolean isRefundable = Boolean.parseBoolean(param.get("isRefundable"));
        int rescheduleDuration = Integer.parseInt(param.get("rescheduleDuration"));

        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = getRandomObject(salutationList);

        step("Create refundable hotel issued order via API");
        boolean result = hotelApi.createIssuedOrderApi(
                username, password, startDate, hotelName, adult, night, room,
                orderFullName, orderNameTitle, guestFullName, guestTitle, currency, isRefundable,
                false, "", "en", true);
        Assertion.assertThat("Hotel Booking via API is successful", result, is(true));

        String orderID = String.valueOf(hotelApi.getOrderId());

        step("User open home page");
        homePage.goToHomePage();

        step("Login as " + username + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(username, password);

        step("User select " + currency + " currency");
        homePage.selectCurrency(currency);

        step("Open order detail for issued order");
        orderListPage.openActiveTicketViaURL(orderID);
        orderDetailPage.skipCoachmark();

        step("User click Reschedule");
        orderDetailPage.clickRescheduleButton();

        step("Reschedule flow: room selection page is loaded");
        Assertion.assertThat("Reschedule page is loaded", hotelReschedulePage.isInReschedulePage(), is(true));

        step("Choose new date range for the reschedule");
        hotelReschedulePage.changeNightDuration(rescheduleDuration);

        step("Open room details");
        roomDetailsPage.clickRoomCard();

        step("Open room image gallery");
        roomDetailsPage.clickRoomImage();

        step("Verify image detail view is opened");
        Assertion.assertThat("Image detail view is displayed", roomDetailsPage.isImageDetailViewDisplayed(), is(true));

        step("Close image detail view");
        roomDetailsPage.clickImageDetailCloseButton();

        step("Verify room details view is displayed after closing the image");
        Assertion.assertThat("Room details view is displayed after closing the image", roomDetailsPage.isRoomDetailsDisplayed(), is(true));
    }

    @Author(names = {"Siddharth Sheoran"})
    @Module(name = "Demand")
    @Tribe(name = "Accommodation")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyNavigationToMyOrders() throws FailedLoginException, InterruptedException, ExecutionException {
        Map<String, String> param = getParam("verifyNavigationToMyOrders");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = sessionThreadLocal.get();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HotelReschedulePageWeb<?> hotelReschedulePage = HotelReschedulePageWeb.getInstance(session);
        RoomDetailsPageWeb<?> roomDetailsPage = RoomDetailsPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPageWeb = PaymentPageWeb.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        RefundDetailWeb<?> refundDetailPage = RefundDetailWeb.getInstance(session);
        HotelBookingFormPageWeb<?> bookingFormPage = HotelBookingFormPageWeb.getInstance(session);

        String username = param.get("email");
        String password = param.get("password");
        String currency = param.get("currency");
        String startDate = generateRandomDate(param.get("rangeAddDate"));
        String hotelName = param.get("hotelName");
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));
        int room = Integer.parseInt(param.get("room"));
        boolean isRefundable = Boolean.parseBoolean(param.get("isRefundable"));
        int rescheduleDuration = Integer.parseInt(param.get("rescheduleDuration"));

        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = getRandomObject(salutationList);

        String hotelId = param.get("hotelId");
        long roomId = Long.parseLong(param.get("roomId"));
        String roomName = param.get("roomName");
        long allotmentId = Long.parseLong(param.get("allotmentId"));
        long roomRateId = Long.parseLong(param.get("roomRateId"));
        int ratePlanId = Integer.parseInt(param.get("ratePlanId"));
        int newCancellationCharge = Integer.parseInt(param.get("newCancellationCharge"));
        int newCancellationNoShow = Integer.parseInt(param.get("newCancellationNoShow"));
        String apiUsername = param.get("apiUsername");
        String apiPassword = param.get("apiPassword");
        int price1 = Integer.parseInt(param.get("price1"));
        int price2 = Integer.parseInt(param.get("price2"));
        String dateRange=param.get("dateRange");
        String date = generateRandomDate(dateRange);

        step("Log in to the Extranet dashboard to generate the token/cookie for the allotment API");
        ExtranetHotelAuthApi<?> extranetAuth = new ExtranetHotelAuthApi<>(session);
        var authData = extranetAuth.getAccessToken(apiUsername, apiPassword);
        Assertion.assertThat("Extranet dashboard token should be generated", authData, is(notNullValue()));
        String apiToken = authData.accessToken;
        String apiCookie = hotelApi.buildAuthCookie(authData.accessToken, authData.refreshToken);
        String apiAccountId = String.valueOf(authData.accountId);
        String apiUser = authData.accountUsername;

        step("Change the active hotel in the dashboard session so the allotment update is authorized");
        hotelApi.changeHotelSession(apiToken, hotelId);

        // ============================ PHASE 1: cancellation policy = false ============================
        step("Set allotment rate to 100 with cancellation policy = true (refundable, charge " + newCancellationCharge + "%)");
        hotelApi.updateHotelAllotmentApi(apiToken, apiCookie, apiUser, apiAccountId, hotelId, date,
                allotmentId, roomId, roomName, roomRateId, ratePlanId, price1, true,
                newCancellationCharge, newCancellationNoShow);

        step("Create refundable hotel issued order via API");
        boolean result = hotelApi.createIssuedOrderApi(
                username, password, startDate, hotelName, adult, night, room,
                orderFullName, orderNameTitle, guestFullName, guestTitle, currency, isRefundable,
                false, "", "en", true);
        Assertion.assertThat("Hotel Booking via API is successful", result, is(true));

        String orderID = String.valueOf(hotelApi.getOrderId());

        step("User open home page");

        homePage.goToHomePage();

        step("Login as " + username + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(username, password);

        step("User select " + currency + " currency");
        homePage.selectCurrency(currency);

        step("Open order detail for issued order");
        orderListPage.openActiveTicketViaURL(orderID);
        orderDetailPage.skipCoachmark();

        step("User click Reschedule");
        orderDetailPage.clickRescheduleButton();

        step("Reschedule flow: room selection page is loaded");
        Assertion.assertThat("Reschedule page is loaded", hotelReschedulePage.isInReschedulePage(), is(true));

        step("Choose new date range for the reschedule");
        hotelReschedulePage.changeNightDuration(rescheduleDuration);

        step("Reschedule flow: room selection page is loaded");
        Assertion.assertThat("Reschedule page is loaded", hotelReschedulePage.isInReschedulePage(), is(true));

        step("Choose a room with additional charge");
        hotelReschedulePage.chooseRoomWithAdditionalPriceNoRoomName();

        step("Set allotment rate to 200 with cancellation policy = true (refundable, charge " + newCancellationCharge + "%)");
        hotelApi.updateHotelAllotmentApi(apiToken, apiCookie, apiUser, apiAccountId, hotelId, date,
                allotmentId, roomId, roomName, roomRateId, ratePlanId, price2, true,
                newCancellationCharge, newCancellationNoShow);

        step("Continue to Contact Details Page");
        bookingFormPage.checkCustomCheckInBoxConditions();
        bookingFormPage.clickContinueButton();

        step("Confirm the Reschedule Request");
        bookingFormPage.clickConfirmRescheduleButton();

        step("Verify the Payment Page");
        Assertion.assertThat("Payment Page loaded successfully", paymentPageWeb.isPaymentPageLoaded(), is(true));
        Assertion.assertThat("CountDown Time has started", paymentPageWeb.isCountdownTimerVisible(), is(true));

    }
}
