package com.tiket.web.test.platform.liberty;

import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.security.auth.login.FailedLoginException;

import com.tiket.apiPage.HotelApi;
import com.tiket.page.order.orderdetail.OrderDetailPage;
import com.tiket.pojo.member.RegisterNewAccountData;
import org.hamcrest.Matchers;

import static com.tiket.core.CoreFunctions.*;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.apiPage.TTDApi;
import com.tiket.apiPage.Unm;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Session;
import com.tiket.page.order.orderlist.OrderListPage.OrderStatus;
import com.tiket.pojo.order.MapVerticalWithOrderId;
import com.tiket.pojo.unm.LoginDataUnmResponse;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import static com.tiket.testdata.TestData.getParam;
import com.tiket.web.dWeb.page.nft.car_transfer.CarTransferPage;
import com.tiket.web.dWeb.page.nft.train.TrainPage;
import com.tiket.web.dWeb.page.payment.PaymentPage;
import com.tiket.web.dashboard.page.common.InboxPageYopmail;
import com.tiket.web.dashboard.page.order.orderCreationPage;
import com.tiket.web.page.common.home.HomePageWeb;
import com.tiket.web.page.common.home.LoginPageWeb;
import com.tiket.web.page.flight.bookingForm.FlightBookingFormPageWeb;
import com.tiket.web.page.flight.descriptionPage.FlightDescriptionPageWeb;
import com.tiket.web.page.flight.landingpage.FlightLandingPageWeb;
import com.tiket.web.page.flight.productReview.FlightProductReviewPageWeb;
import com.tiket.web.page.flight.searchresultpage.FlightSearchResultPageWeb;
import com.tiket.web.page.giftcard.GiftCardPageWeb;
import com.tiket.web.page.order.OrderDetail.OrderDetailWeb;
import com.tiket.web.page.order.OrderHistory.OrderHistoryWeb;
import com.tiket.web.page.order.OrderList.OrderListWeb;
import com.tiket.web.page.platform.payment.PaymentPageWeb;
import com.tiket.web.page.ttd.TtdBookingFormPage.TtdBookingFormPageWeb;
import com.tiket.web.page.ttd.TtdLandingPage.TtdLandingPageWeb;
import com.tiket.web.page.ttd.TtdProductDetailPage.TtdProductDetailPageWeb;

public class OrderTest extends BaseTest {

    @Author(names = {"Fery"})
    @Module(name = "MyOrder")
    @Tribe(name = "Order")
    @Vertical(name = "PRO")
    @Test
    public void verifyHotelOrderDetail() throws FailedLoginException, InterruptedException, ExecutionException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyHotelOrderDetail");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        orderCreationPage<?> orderCreationPage = new orderCreationPage<>(session);
        Unm<?> unm = new Unm<>(session);

        String email = param.get("email");
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

        step("Go to home page");
        homePage.goToHomePage();

        step("Login as " + email + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(email, password);

        step("Get api access token");
        LoginDataUnmResponse.Data authData = unm.unmLoginApi(email, password);
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
                false,
                false,
                OrderStatus.ETICKET_ISSUED);


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

        step("Find the issued order");
        orderListPage.openActiveTicketViaURL(bookedOrderID);
        orderDetailPage.skipCoachmark();

        step("Verify hotel order details");
        Assertion.assertThat("Order ID is displayed correctly", orderDetailPage.getOrderId(),
            containsString(bookedOrderID));
        Assertion.assertThat("E-ticket card Displayed", orderDetailPage.isETicketCardDisplayed(),
            is(true));
        Assertion.assertThat("Hotel Name is displayed correctly",
            orderDetailPage.isHotelNameDisplayed(hotelNameData), is(true));
        Assertion.assertThat("Call button is clickable", orderDetailPage.isCallButtonDisplayed(),
            is(true));
        Assertion.assertThat("Location redirection button should be verified",
            orderDetailPage.verifyLocationRedirectionButton(), is(true));
        Assertion.assertThat("Refund and reschedule info section should be verified",
            orderDetailPage.verifyRefundAndRescheduleInfo(), is(true));
        Assertion.assertThat("Refund button should be displayed",
            orderDetailPage.isRefundButtonDisplayed(), is(true));
        Assertion.assertThat("Reschedule button should be displayed",
            orderDetailPage.isRescheduleButtonDisplayed(), is(true));
        Assertion.assertThat("Edit booking button should be displayed",
            orderDetailPage.verifyEditGuestDetail(), is(true));
        Assertion.assertThat("Page Module should be displayed", orderDetailPage.verifyPageModule(),
            is(true));
    }

    @Author(names = {"Fery, Daffa"})
    @Module(name = "MyOrder")
    @Tribe(name = "Order")
    @Vertical(name = "PRO")
    @Test
    public void verifyAirportTransferOrderDetail() throws FailedLoginException, InterruptedException, ExecutionException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyAirportTransferOrderDetail");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        CarTransferPage<?> carTransferPage = new CarTransferPage<>(session);
        Unm<?> unm = new Unm<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));

        String adminName = param.get("adminName");
        String adminPhone = param.get("adminPhone");
        String currency = param.get("currency");
        String origin = param.get("origin");
        String destination = param.get("destination");
        String type = param.get("type");
        String rangeAddDate = param.get("rangeAddDate");
        String pickupTime = param.get("pickupTime").replace('/', ':');
        int pax = Integer.parseInt(param.get("pax"));

        step("Go to home page");
        homePage.goToHomePage();

        step("Login as " + email + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(email, password);

        step("Get api access token");
        LoginDataUnmResponse.Data authData = unm.unmLoginApi(email, password);
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
                "airport_transfer",
                currency,
                false,
                false,
                OrderStatus.ETICKET_ISSUED);

        step("Create a airport transfer order with issued status or create order");
        String bookedOrderID = activeOrderData == null
                ? carTransferPage.createIssuedOrderApi(
                        email, password, adminName, adminPhone,
                        origin, destination, type, rangeAddDate, pickupTime, pax)
                : activeOrderData.orderId;

        step("Get internal data for the issued order");
        String carType = orderDetailPage.getCarType(bookedOrderID);
        String totalPrice = orderDetailPage.getTotalAmount(bookedOrderID);
        String pickupPlace = orderDetailPage.getVerticalData(bookedOrderID).get("pickupLocation").split(",")[0];
        String dropPlace = orderDetailPage.getVerticalData(bookedOrderID).get("dropOffLocation").split(",")[0];
        String salutation = orderDetailPage.getSalutation(bookedOrderID);
        String guestName = orderDetailPage.getGuestName(bookedOrderID);
        String phoneNumber = orderDetailPage.getPhoneNumber(bookedOrderID);
        String pickupPlaceDetail = orderDetailPage.getVerticalData(bookedOrderID).get("pickupLocation");
        String dropPlaceDetail = orderDetailPage.getVerticalData(bookedOrderID).get("dropOffLocation");

        step("Find the issued order");
        orderListPage.openActiveTicketViaURL(bookedOrderID);
        orderDetailPage.skipCoachmark();

        step("Verify airport transfer order details");
        Assertion.assertThat("Order ID is displayed correctly", orderDetailPage.getOrderIdAt(), containsString(bookedOrderID));
        Assertion.assertThat("E-ticket card Displayed", orderDetailPage.isETicketCardDisplayed(), is(true));
        Assertion.assertThat("Verify car detail section", orderDetailPage.verifyAtCarDetailSection(), is(carType));
        Assertion.assertThat("Verify at trip summary detail", orderDetailPage.verifyAtSummaryDetail(pickupPlaceDetail, dropPlaceDetail), is(true));
        orderDetailPage.closeModal();
        Assertion.assertThat("Verify facilities detail present", orderDetailPage.verifyAtFacilitiesDetailPresent(), is(true));
        Assertion.assertThat("Verify vendor section present", orderDetailPage.verifyAtVendorSectionPresent(), is(true));
        Assertion.assertThat("Verify contact detail in order detail page", orderDetailPage.verifyAtContactDetail(salutation, guestName, phoneNumber), is(true));
        Assertion.assertThat("Verify AT Total price", CoreFunctions.convertPriceSafely(orderDetailPage.getVerticalTotalPrice()), equalTo(CoreFunctions.convertPriceSafely(totalPrice)));
        Assertion.assertThat("E-ticket button should be displayed in Manage Order", orderDetailPage.verifyEticketManageOrder(), is(true));
        Assertion.assertThat("Page Module should be displayed", orderDetailPage.verifyPageModule(), is(true));
    }

    @Author(names = {"Fery"})
    @Module(name = "MyOrder")
    @Tribe(name = "Order")
    @Vertical(name = "PRO")
    @Test
    public void verifyTrainOrderDetail() throws FailedLoginException, InterruptedException, ExecutionException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyTrainOrderDetail");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        TrainPage<?> trainPage = new TrainPage<>(session);
        Unm<?> unm = new Unm<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));

        String startDate = generateRandomDate(param.get("rangeAddDate"));
        String name = param.get("name");
        String phone = param.get("phone");
        String originCity = param.get("originCity");
        String destinationCity = param.get("destinationCity");
        String tripType = param.get("tripType");
        int adult = Integer.parseInt(param.get("adult"));
        int infant = Integer.parseInt(param.get("infant"));
        String idNumber = param.get("idNumber");

        String currency = param.get("currency");

        step("Go to home page");
        homePage.goToHomePage();

        step("Login as " + email + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(email, password);

        step("Get api access token");
        LoginDataUnmResponse.Data authData = unm.unmLoginApi(email, password);
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
                "train",
                currency,
                false,
                false,
                OrderStatus.ETICKET_ISSUED);

        step("Create a train order with issued status");
        String bookedOrderID = orderListPage.getTrainBookedOrderId(
            trainPage,
            activeOrderData,
            email,
            password,
            name,
            phone,
            idNumber,
            originCity,
            destinationCity,
            startDate,
            tripType,
            adult,
            infant
        );

        step("Get internal data for the issued order");
        String totalPrice = orderDetailPage.getTotalAmount(bookedOrderID);

        step("Find the issued order");
        orderListPage.openActiveTicketViaURL(bookedOrderID);
        orderDetailPage.skipCoachmark();

        step("Verify Train order details");
        Assertion.assertThat("E-ticket card Displayed", orderDetailPage.isETicketCardDisplayed(), is(true));
        Assertion.assertThat("Is train detail departure correct", orderDetailPage.isTrainDetailCorrect(), is(notNullValue()));
        Assertion.assertThat("Is train detail return correct", orderDetailPage.isTrainDetailCorrect(), is(notNullValue()));
        Assertion.assertThat("Verify arrival date, departure date, duration", orderDetailPage.isTrainDepartureArrivalDurationExists(), is(true));
        Assertion.assertThat("Passenger and facilities section exists", orderDetailPage.passengerAndFacilitiesExists(), is(true));
        Assertion.assertThat("Refund and reschedule info section should be verified", orderDetailPage.verifyRefundAndRescheduleInfo(), is(true));
        Assertion.assertThat("Total price is correct", CoreFunctions.convertPriceSafely(orderDetailPage.getVerticalTotalPrice()), is(CoreFunctions.convertPriceSafely(totalPrice)));
    }

    @Author(names = {"Daffa, Hafiz"})
    @Module(name = "MyOrder")
    @Tribe(name = "Order")
    @Vertical(name = "PRO")
    @Test
    public void checkSeeEticket()
            throws FailedLoginException, InterruptedException, ExecutionException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("checkSeeEticket");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        orderCreationPage<?> orderCreationPage = new orderCreationPage<>(session);
        Unm<?> unm = new Unm<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));

        String hotelName = param.get("hotelName");
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));
        int room = Integer.parseInt(param.get("room"));
        int waitingTime = Integer.parseInt(param.get("waitingTime"));
        String startDate = generateRandomDate(param.get("rangeAddDate"));

        String currency = param.get("currency");
        String url = param.get("url");

        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = getRandomObject(salutationList);

        step("Go to home page");
        homePage.goToHomePage();

        step("Login as " + email + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(email, password);

        step("Get api access token");
        LoginDataUnmResponse.Data authData = unm.unmLoginApi(email, password);
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
                        false,
                        false,
                        OrderStatus.ETICKET_ISSUED);
        String bookedOrderID;

        if (activeOrderData == null || activeOrderData.orderId == null || activeOrderData.orderId.isEmpty()) {
            step("Create a hotel order with issued status");
            orderCreationPage.createRefundableHotelSakukuIssuedOrderApiSelectVendor(
                    email, password, startDate, hotelName, adult, night, room,
                    orderFullName, orderNameTitle, guestFullName, guestTitle, currency,
                    false, true, "TIKET"
            );
            bookedOrderID = String.valueOf(orderCreationPage.getOrderId());
            Assertion.assertThat("Success to create issued order", bookedOrderID,
                    is(not(emptyOrNullString())));
        } else {
            bookedOrderID = activeOrderData.orderId;
        }

        step("Find the issued order");
        orderListPage.openActiveTicketViaURL(bookedOrderID);
        orderDetailPage.skipCoachmark();

        step("Open e-Ticket from header");
        orderDetailPage.clickEticketFromHeader();
        Assertion.assertThat("is view e-ticket header displayed", orderDetailPage.checkETicketLanguage("English"), is(true));
        Assertion.assertThat("is new tab opened and is url correct?", orderDetailPage.isNewTabOpened(url, waitingTime), is(true));
        orderDetailPage.closeEticketPopUp();
    }

    @Author(names = {"Daffa" , "Jashan"})
    @Module(name = "MyOrder")
    @Tribe(name = "Order")
    @Vertical(name = "PRO")
    @Test
    public void verifyHomesOrderDetailWeb()
            throws FailedLoginException, InterruptedException, ExecutionException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyHomesOrderDetailWeb");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        orderCreationPage<?> orderCreationPage = new orderCreationPage<>(session);
        Unm<?> unm = new Unm<>(session);

        String email = param.get("email");
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

        step("Go to home page");
        homePage.goToHomePage();

        step("Login as " + email + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(email, password);

        step("Get api access token");
        LoginDataUnmResponse.Data authData = unm.unmLoginApi(email, password);
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
                        "hotel_homes",
                        currency,
                        false,
                        false,
                        OrderStatus.ETICKET_ISSUED);


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

        step("Find the issued order");
        orderListPage.openActiveTicketViaURL(bookedOrderID);
        orderDetailPage.skipCoachmark();

        step("Verify homes order details");
        Assertion.assertThat("Order ID is displayed correctly", orderDetailPage.getOrderId(),
                containsString(bookedOrderID));
        Assertion.assertThat("E-ticket card Displayed", orderDetailPage.isETicketCardDisplayed(),
                is(true));
        Assertion.assertThat("Homes Name is displayed correctly",
                orderDetailPage.isHotelNameDisplayed(hotelNameData), is(true));
        Assertion.assertThat("Room Name is displayed correctly",
                orderDetailPage.roomNameDisplayed(), is(roomNameData));
        Assertion.assertThat("Total night is equal", orderDetailPage.getTotalNight(),
                is((nightDurationData)));
        Assertion.assertThat("Total room is equal", orderDetailPage.getTotalRoom(),
                is(roomCountData));
        Assertion.assertThat("Call button is clickable", orderDetailPage.isCallButtonDisplayed(),
                is(true));
        Assertion.assertThat("Location redirection button should be verified",
                orderDetailPage.verifyLocationRedirectionButton(), is(true));
        Assertion.assertThat("Refund and reschedule info section should be verified",
                orderDetailPage.verifyRefundAndRescheduleInfo(), is(true));
        Assertion.assertThat("Refund button should be displayed",
                orderDetailPage.isRefundButtonDisplayed(), is(true));
        Assertion.assertThat("Reschedule button should be displayed",
                orderDetailPage.isRescheduleButtonDisplayed(), is(true));
        Assertion.assertThat("Edit booking button should be displayed",
                orderDetailPage.verifyEditGuestDetail(), is(true));
        Assertion.assertThat("Page Module should be displayed", orderDetailPage.verifyPageModule(),
                is(true));
    }

    @Author(names = {"Daffa"})
    @Module(name = "MyOrder")
    @Tribe(name = "Order")
    @Vertical(name = "PRO")
    @Test
    public void verifyCarRentalOrderDetail()
            throws FailedLoginException, InterruptedException, ExecutionException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyCarRentalOrderDetail");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        orderCreationPage<?> orderCreationPage = new orderCreationPage<>(session);
        Unm<?> unm = new Unm<>(session);

        String currency = param.get("currency");

        String email = param.get("email");
        String password = param.get("password");
        String phoneNumber = param.get("phoneNumber");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));

        String driverOption = param.get("driverOption");
        String location = param.get("location");
        String startDate = generateRandomDate(param.get("rangeAddDate"));
        String type = param.get("type");
        int rentalDuration = Integer.parseInt(param.get("rentalDuration"));

        String pickupTestData = param.get("pickupTime");
        StringBuilder pickup = new StringBuilder(pickupTestData);
        pickup.setCharAt(2, ':');
        String pickupTime = pickup.toString();

        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String fullName = randomName() + " Autorder";
        String nameTitle = getRandomObject(salutationList);
        String adminName = param.get("adminName");
        String adminPhoneNumber = param.get("adminPhone");

        step("Go to home page");
        homePage.goToHomePage();

        step("Login as " + email + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(email, password);

        step("Get api access token");
        LoginDataUnmResponse.Data authData = unm.unmLoginApi(email, password);
        String apiAccessToken = authData.accessToken;
        String accountId = String.valueOf(authData.accountId);

        step("Open my order page");
        String pageLang = homePage.getPageLanguageCode();
        homePage.goToMyOrderPage();

        step("Check order present in order list");
        MapVerticalWithOrderId.TitleAndOrderId activeOrderData = orderListPage
                .findFirstOrderFilterByVertical(
                        apiAccessToken,
                        email,
                        accountId,
                        "car",
                        currency,
                        false,
                        false,
                        OrderStatus.ETICKET_ISSUED);

        step("Fetch Car Rental order with issued status, else create order");
        String bookedOrderID = orderListPage.getCarRentalBookedOrderId(
                orderCreationPage,
                activeOrderData,
                email,
                phoneNumber,
                password,
                driverOption,
                location,
                startDate,
                pickupTime,
                type,
                fullName,
                nameTitle,
                adminName,
                adminPhoneNumber,
                rentalDuration
        );

        step("Get internal data for the issued order");
        String totalPrice = orderDetailPage.getTotalAmount(bookedOrderID);
        String pickupPlace = orderDetailPage.getVerticalData(bookedOrderID).get("pickupLocation");
        String salutation = orderDetailPage.getSalutation(bookedOrderID);
        String guestName = orderDetailPage.getGuestName(bookedOrderID);
        String userPhoneNumber = orderDetailPage.getPhoneNumber(bookedOrderID);
        String userRentalDuration = orderDetailPage.getVerticalData(bookedOrderID).get("duration");

        step("Find the issued order");
        orderListPage.openActiveTicketViaURL(bookedOrderID);
        orderDetailPage.skipCoachmark();

        step("Verify Car Rental order details");
        Assertion.assertThat("Order ID is displayed correctly", orderDetailPage.getOrderIdAt(),containsString(bookedOrderID));
        Assertion.assertThat("E-ticket card Displayed", orderDetailPage.isETicketCardDisplayed(), is(true));
        Assertion.assertThat("Is car rental detail correct", orderDetailPage.checkCarRentalDetail(pickupPlace, userRentalDuration), is(true));
        Assertion.assertThat("Verify vendor section present", orderDetailPage.verifyAtVendorSectionPresent(), is(true));
        Assertion.assertThat("Verify contact detail in order detail page", orderDetailPage.verifyAtContactDetail(salutation, guestName, userPhoneNumber), is(true));
        Assertion.assertThat("Verify car rental Total price", CoreFunctions.convertPriceSafely(orderDetailPage.getVerticalTotalPrice()), equalTo(CoreFunctions.convertPriceSafely(totalPrice)));
        Assertion.assertThat("E-ticket button should be displayed in Manage Order", orderDetailPage.verifyEticketManageOrder(), is(true));
        Assertion.assertThat("Page Module should be displayed", orderDetailPage.verifyPageModule(), is(true));
    }

    @Author(names = {"Daffa"})
    @Module(name = "MyOrder")
    @Tribe(name = "Order")
    @Vertical(name = "PRO")
    @Test
    public void verifyTTDOrderDetail()
            throws FailedLoginException, InterruptedException, ExecutionException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyTTDOrderDetail");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        orderCreationPage<?> orderCreationPage = new orderCreationPage<>(session);
        Unm<?> unm = new Unm<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));

        String currency = param.get("currency");
        String productUrlName = param.get("productUrlName");
        boolean refundable = true;

        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String fullName = randomName() + " Autorder";
        String nameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = getRandomObject(salutationList);
        String guestEmailAddress = randomEmail();

        step("Go to home page");
        homePage.goToHomePage();

        step("Login as " + email + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(email, password);

        step("Get api access token");
        LoginDataUnmResponse.Data authData = unm.unmLoginApi(email, password);
        String apiAccessToken = authData.accessToken;
        String accountId = String.valueOf(authData.accountId);

        step("Check order present in order list");
        MapVerticalWithOrderId.TitleAndOrderId activeOrderData = orderListPage
                .findFirstOrderFilterByVertical(
                        apiAccessToken,
                        email,
                        accountId,
                        "to_do",
                        currency,
                        false,
                        false,
                        OrderStatus.ETICKET_ISSUED);

        step("Fetch TTD order with issued status, else create order");
        String bookedOrderID = orderListPage.getTTDBookedOrderId(
                orderCreationPage,
                activeOrderData,
                email,
                password,
                currency,
                productUrlName,
                refundable,
                fullName,
                nameTitle,
                guestFullName,
                guestEmailAddress,
                guestTitle
        );

        step("Get internal data for the issued order");
        String totalPrice = orderDetailPage.getTotalAmount(bookedOrderID);
        String userContactName = orderDetailPage.getContactName(bookedOrderID);
        String userContactPhoneNumber = orderDetailPage.getPhoneNumber(bookedOrderID);
        String userContactEmail = orderDetailPage.getContactEmail(bookedOrderID);

        step("Open my order page");
        homePage.goToMyOrderPage();

        step("Find the issued order");
        orderListPage.openActiveTicketViaURL(bookedOrderID);
        orderDetailPage.skipCoachmark();

        step("Verify TTD order details");
        Assertion.assertThat("E-ticket card Displayed", orderDetailPage.isETicketCardDisplayed(), is(true));
        Assertion.assertThat("Order ID is displayed correctly", orderDetailPage.getOrderId(),containsString(bookedOrderID));
        Assertion.assertThat("Is TTD validity and contact name correct", orderDetailPage.isTTDValidityAndContactNameExist(), is(true));
        Assertion.assertThat("Verify contact name in order detail page", orderDetailPage.verifyContactNameAtReservation(), containsString(userContactName));
        Assertion.assertThat("Verify contact phone number in order detail page", orderDetailPage.verifyPhoneNumberAtReservation(), is(userContactPhoneNumber));
        Assertion.assertThat("Verify contact email in order detail page", orderDetailPage.verifyContactEmailAtReservation(), is(userContactEmail));
        Assertion.assertThat("Verify TTD total price", CoreFunctions.convertPriceSafely(orderDetailPage.getVerticalTotalPrice()), equalTo(CoreFunctions.convertPriceSafely(totalPrice)));
        Assertion.assertThat("E-ticket button should be displayed in Manage Order", orderDetailPage.verifyEticketManageOrder(), is(true));
        Assertion.assertThat("Page Module should be displayed", orderDetailPage.verifyPageModule(), is(true));
    }

    @Author(names = {"Daffa"})
    @Module(name = "MyOrder")
    @Tribe(name = "Order")
    @Vertical(name = "PRO")
    @Test
    public void verifyFerryOrderDetail()
            throws FailedLoginException, InterruptedException, ExecutionException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyFerryOrderDetail");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        orderCreationPage<?> orderCreationPage = new orderCreationPage<>(session);
        Unm<?> unm = new Unm<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));

        String currency = param.get("currency");
        String ferryOrigin = param.get("origin");
        String ferryDestination = param.get("destination");
        String departureDate = CoreFunctions.generateRandomDate(param.get("rangeDate"));
        String departureTime = param.get("departureTime").replace("-", ":");
        String vehicleType = param.get("vehicleType");
        int adult = Integer.parseInt(param.get("adult"));
        int child = Integer.parseInt(param.get("child"));
        int infant = Integer.parseInt(param.get("infant"));
        int elderly = Integer.parseInt(param.get("elderly"));

        step("Go to home page");
        homePage.goToHomePage();

        step("Check login state and log in");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("Get api access token");
        LoginDataUnmResponse.Data authData = unm.unmLoginApi(email, password);
        String apiAccessToken = authData.accessToken;
        String accountId = String.valueOf(authData.accountId);

        step("Check order present in order list");
        MapVerticalWithOrderId.TitleAndOrderId activeOrderData = orderListPage
                .findFirstOrderFilterByVertical(
                        apiAccessToken,
                        email,
                        accountId,
                        "ferry",
                        currency,
                        false,
                        false,
                        OrderStatus.ETICKET_ISSUED);

        step("Fetch Ferry order with issued status, else create order");
        String bookedOrderID = orderListPage.getFerryBookedOrderId(
                orderCreationPage,
                activeOrderData,
                email,
                password,
                ferryOrigin,
                ferryDestination,
                departureDate,
                departureTime,
                vehicleType,
                adult,
                child,
                infant,
                elderly
        );

        step("Get internal data for the issued order");
        String totalPrice = orderDetailPage.getTotalAmount(bookedOrderID);
        String userDeparturePort = orderDetailPage.getVerticalData(bookedOrderID).get("departurePort");;
        String userArrivalPort = orderDetailPage.getVerticalData(bookedOrderID).get("arrivalPort");
        String userBookingCode = orderDetailPage.getVerticalData(bookedOrderID).get("bookingCode");

        step("Open my order page");
        homePage.goToMyOrderPage();

        step("Find the issued order");
        orderListPage.openActiveTicketViaURL(bookedOrderID);
        orderDetailPage.skipCoachmark();

        step("Verify TTD order details");
        Assertion.assertThat("E-ticket card Displayed", orderDetailPage.isETicketCardDisplayed(), is(true));
        Assertion.assertThat("is Booking code Ferry correct", orderDetailPage.verifyBookingCodeFerry(), is(userBookingCode));
        Assertion.assertThat("Is Ferry departure port correct", orderDetailPage.verifyDepatureCityFerry(), equalToIgnoringCase(userDeparturePort));
        Assertion.assertThat("Is Ferry arrival port correct", orderDetailPage.verifyArrivalCityFerry(), equalToIgnoringCase(userArrivalPort));
        Assertion.assertThat("Passenger and facilities section exists", orderDetailPage.passengerAndFacilitiesExists(), is(true));
        Assertion.assertThat("Verify Ferry total price", CoreFunctions.convertPriceSafely(orderDetailPage.getVerticalTotalPrice()), equalTo(CoreFunctions.convertPriceSafely(totalPrice)));
        Assertion.assertThat("E-ticket button should be displayed in Manage Order", orderDetailPage.verifyEticketManageOrder(), is(true));

    }

    @Author(names = {"Daffa, Hafiz"})
    @Module(name = "MyOrder")
    @Tribe(name = "Order")
    @Vertical(name = "PRO")
    @Test
    public void checkOrderHistory() throws FailedLoginException, InterruptedException, ExecutionException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("checkOrderHistory");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderHistoryWeb<?> OrderHistoryPage = OrderHistoryWeb.getInstance(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));

        step("Go to home page");
        homePage.goToHomePage();

        step("Check login state and log in");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("Go to my order page");
        homePage.goToMyOrderPage();

        step("Go to order history");
        orderListPage.openOrderHistoryPage();

        step("Check past order and refund list are visible");
        Assertion.assertThat("is order card exist", OrderHistoryPage.isOrderCardExist(), is(true));
        Assertion.assertThat("is past refund tab is visible", OrderHistoryPage.verifyPastRefundTab(), is(true));
        Assertion.assertThat("is finished order shown", OrderHistoryPage.isFinishedOrderShown(), is(true));
        Assertion.assertThat("is cancelled order shown", OrderHistoryPage.isCancelledOrderShown(), is(true));

        step("Check filter functionality and badge");
        Assertion.assertThat("Verify filter functionality and badge", OrderHistoryPage.selectFilterAndVerifyBadge(), is(true));

        step("Check sorting functionality and badge");
        Assertion.assertThat("Verify sorting functionality and badge", OrderHistoryPage.applySortingAndVerifyBadge(), is(true));
    }

    @Author(names = {"Daffa"})
    @Module(name = "MyOrder")
    @Tribe(name = "Order")
    @Vertical(name = "PRO")
    @Test
    public void checkMultiOrderHotel() throws FailedLoginException, InterruptedException, ExecutionException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("checkMultiOrderHotel");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        orderCreationPage<?> orderCreationPage = new orderCreationPage<>(session);
        Unm<?> unm = new Unm<>(session);

        String currency = param.get("currency");
        String hotelName = param.get("hotelName");
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));
        int room = Integer.parseInt(param.get("room"));
        String startDate = generateRandomDate(param.get("rangeAddDate"));
        int qtyEachRoom = Integer.parseInt(param.get("qtyEachRoom"));

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));

        step("Go to home page");
        homePage.goToHomePage();

        step("Login as " + email + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(email, password);

        step("Create Multi-Order Hotel with types of room paid using " + currency + " currency");
        String bookedOrderID = orderCreationPage.createOrderHotelMultiRoomApi(
                email,password,hotelName,adult,night,room,startDate,qtyEachRoom,currency);

        step("Open my order page");
        homePage.goToMyOrderPage();

        step("Get internal data for the issued order");
        String hotelNameData = orderDetailPage.getVerticalData(bookedOrderID).get("orderName");

        step("Find the issued order");
        orderListPage.openActiveTicketViaURL(bookedOrderID);
        orderDetailPage.skipCoachmark();

        step("Verify Multi Order hotel order details");
        Assertion.assertThat("Order ID is displayed correctly", orderDetailPage.getOrderId(),
                containsString(bookedOrderID));
        Assertion.assertThat("Hotel Name is displayed correctly",
                orderDetailPage.verifyHotelName(), equalToIgnoringCase(hotelNameData));
        Assertion.assertThat("E-ticket card Displayed", orderDetailPage.isETicketCardDisplayed(),
                is(true));
        Assertion.assertThat("Call button is clickable", orderDetailPage.isCallButtonDisplayed(),
                is(true));
        Assertion.assertThat("Location redirection button should be verified",
                orderDetailPage.verifyLocationRedirectionButton(), is(true));
        Assertion.assertThat("Refund and reschedule info section should be verified",
                orderDetailPage.verifyRefundAndRescheduleInfoHotelMultiRoom(), is(true));
        Assertion.assertThat("Refund button should be displayed",
                orderDetailPage.isRefundButtonDisplayed(), is(true));
        Assertion.assertThat("Reschedule button should be displayed",
                orderDetailPage.isRescheduleButtonDisplayed(), is(true));
        Assertion.assertThat("Edit booking button should be displayed",
                orderDetailPage.verifyEditGuestDetail(), is(true));
        Assertion.assertThat("Page Module should be displayed", orderDetailPage.verifyPageModule(),
                is(true));
    }

    @Author(names = {"Daffa"})
    @Module(name = "MyOrder")
    @Tribe(name = "Order")
    @Vertical(name = "PRO")
    @Test
    public void checkCancelOrder() throws FailedLoginException, InterruptedException, ExecutionException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("checkCancelOrder");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        orderCreationPage<?> orderCreationPage = new orderCreationPage<>(session);
        OrderHistoryWeb<?> OrderHistoryPage = OrderHistoryWeb.getInstance(session);
        Unm<?> unm = new Unm<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));

        String hotelName = param.get("hotelName");
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));
        int room = Integer.parseInt(param.get("room"));
        String startDate = generateRandomDate(param.get("rangeAddDate"));

        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = getRandomObject(salutationList);

        step("Go to home page");
        homePage.goToHomePage();

        step("Login as " + email + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(email, password);

        step("Create waiting for payment order");
        orderCreationPage.createWaitingPaymentOrderHotel(
                email,password,startDate,hotelName,adult,night,room,orderFullName,orderNameTitle,guestFullName,guestTitle,"IDR");

        step("Open my order page");
        homePage.goToMyOrderPage();

        step("Cancel order from order list");
        boolean cancelOrderStatus = orderListPage.cancelOrder();

        step("Go to order history");
        orderListPage.openOrderHistoryPage();

        step("Verify cancel order in order history");
        Assertion.assertThat("Cancel order status popup on order List Page", cancelOrderStatus, is(true));
        Assertion.assertThat("Cancel order shown in history page", OrderHistoryPage.isCancelledOrderShown(), is(true));
    }

    @Author(names = {"Daffa"})
    @Module(name = "MyOrder")
    @Tribe(name = "Order")
    @Vertical(name = "PRO")
    @Test
    public void verifyFlightOrderDetail()
            throws FailedLoginException, InterruptedException, ExecutionException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyFlightOrderDetail");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        Unm<?> unm = new Unm<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String currency = param.get("currency");

        step("Go to home page");
        homePage.goToHomePage();

        step("Login as " + email + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(email, password);

        step("Get api access token");
        LoginDataUnmResponse.Data authData = unm.unmLoginApi(email, password);
        String apiAccessToken = authData.accessToken;
        String accountId = String.valueOf(authData.accountId);

        step("Check order present in order list");
        MapVerticalWithOrderId.TitleAndOrderId activeOrderData = orderListPage.findFirstOrderFilterByVertical(
                apiAccessToken,
                email,
                accountId,
                "flight",
                currency,
                false,
                false,
                OrderStatus.ETICKET_ISSUED);
        if (activeOrderData == null) {
            Assertion.skip("Can't find any active order id flight");
        }

        step("Get internal data for the issued order");
        String airlineName = orderDetailPage.getVerticalData(activeOrderData.orderId).get("airlinesName").split(" ")[0];
        String departureAirport = orderDetailPage.getVerticalData(activeOrderData.orderId).get("departureCity");
        String arrivalAirport = orderDetailPage.getVerticalData(activeOrderData.orderId).get("arrivalCity");

        step("Open my order page");
        homePage.goToMyOrderPage();

        step("Find the issued order");
        orderListPage.openActiveTicketViaURL(activeOrderData.orderId);
        orderDetailPage.skipCoachmark();

        step("Verify flight order details");
        Assertion.assertThat("Verify airline name should be displayed", orderDetailPage.getAirlineNameFlight(),
                containsStringIgnoringCase(airlineName));
        Assertion.assertThat("E-ticket card Displayed", orderDetailPage.isETicketCardDisplayed(),
                is(true));
        Assertion.assertThat("Order ID is displayed correctly", orderDetailPage.getOrderId(),
                containsStringIgnoringCase(activeOrderData.orderId));
        Assertion.assertThat("Verify departure airport name should be displayed", orderDetailPage.verifyDepartureAirportName(),
                containsString(departureAirport));
        Assertion.assertThat("Verify arrival airport name should be displayed", orderDetailPage.verifyArrivalAirportName(),
                containsString(arrivalAirport));
        Assertion.assertThat("Refund button should be displayed",
                orderDetailPage.isRefundButtonDisplayed(), is(true));
        Assertion.assertThat("Reschedule button should be displayed",
                orderDetailPage.isRescheduleButtonDisplayed(), is(true));
        Assertion.assertThat("Page Module should be displayed", orderDetailPage.verifyPageModule(),
                is(true));
    }

    @Author(names = {"Daffa"})
    @Module(name = "MyOrder")
    @Tribe(name = "Order")
    @Vertical(name = "PRO")
    @Test
    public void checkMultiCurrencyFlight()
            throws FailedLoginException, InterruptedException, ExecutionException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("checkMultiCurrencyFlight");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String orderId = param.get("orderId");

        step("Go to home page");
        homePage.goToHomePage();

        step("Login as " + email + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(email, password);

        step("Get internal data for the issued order");
        String totalPrice = orderDetailPage.getTotalAmountMultiCurrency(orderId);
        String custCurrency = orderDetailPage.getCustomerCurrencyData(orderId);

        step("Open my order page");
        homePage.goToMyOrderPage();

        step("Find the issued order");
        orderListPage.openActiveTicketViaURL(orderId);
        orderDetailPage.skipCoachmark();

        step("Verify flight order details");
        Assertion.assertThat("E-ticket card Displayed", orderDetailPage.isETicketCardDisplayed(),
                is(true));
        Assertion.assertThat("Order ID is displayed correctly", orderDetailPage.getOrderId(),
                containsString(orderId));
        Assertion.assertThat("Verify customer currency other than IDR", orderDetailPage.getCustomerCurrency(),
                is(custCurrency));
        Assertion.assertThat("Verify total price", CoreFunctions.convertPriceSafely(orderDetailPage.getVerticalTotalPrice().split(" ")[1]),
                equalTo(CoreFunctions.convertPriceSafely(totalPrice)));
        Assertion.assertThat("Refund button should be displayed",
                orderDetailPage.isRefundButtonDisplayed(), is(true));
        Assertion.assertThat("Reschedule button should be displayed",
                orderDetailPage.isRescheduleButtonDisplayed(), is(true));
    }

    @Author(names = {"Daffa"})
    @Module(name = "MyOrder")
    @Tribe(name = "Order")
    @Vertical(name = "PRO")
    @Test
    public void check100pRefundFlight()
            throws FailedLoginException, InterruptedException, ExecutionException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("check100pRefundFlight");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        Unm<?> unm = new Unm<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String orderId = param.get("orderId");

        step("Go to home page");
        homePage.goToHomePage();

        step("Login as " + email + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(email, password);

        step("Open my order page");
        homePage.openYourOrderViaURL();

        step("Find the issued order");
        orderListPage.openActiveTicketViaURL(orderId);
        orderDetailPage.skipCoachmark();

        step("Verify flight order details");
        Assertion.assertThat("E-ticket card Displayed", orderDetailPage.isETicketCardDisplayed(),
                is(true));
        Assertion.assertThat("Order ID is displayed correctly", orderDetailPage.getOrderId(),
                containsString(orderId));
        Assertion.assertThat("100p refund flight showing in price detail", orderDetailPage.verify100pRefundPriceDetail(),
                is(true));
        Assertion.assertThat("100p refund flight showing in manage order",
                orderDetailPage.verify100pRefundManageOrder(), is(true));
        Assertion.assertThat("Reschedule button should be displayed",
                orderDetailPage.isRescheduleButtonDisplayed(), is(true));
    }

    @Author(names = {"Daffa"})
    @Module(name = "MyOrder")
    @Tribe(name = "Order")
    @Vertical(name = "PRO")
    @Test
    public void checkDeleteOrder() throws FailedLoginException, InterruptedException, ExecutionException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("checkDeleteOrder");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        orderCreationPage<?> orderCreationPage = new orderCreationPage<>(session);
        OrderHistoryWeb<?> OrderHistoryPage = OrderHistoryWeb.getInstance(session);
        Unm<?> unm = new Unm<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));

        String hotelName = param.get("hotelName");
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));
        int room = Integer.parseInt(param.get("room"));
        String startDate = generateRandomDate(param.get("rangeAddDate"));

        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = getRandomObject(salutationList);

        step("Go to home page");
        homePage.goToHomePage();

        step("Login as " + email + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(email, password);

        step("Create waiting for payment order");
        orderCreationPage.createWaitingPaymentOrderHotel(
                email,password,startDate,hotelName,adult,night,room,orderFullName,orderNameTitle,guestFullName,guestTitle,"IDR");

        step("Open my order page");
        homePage.goToMyOrderPage();

        step("Cancel order from order list");
        orderListPage.cancelOrder();

        step("Go to order history and delete order");
        orderListPage.openOrderHistoryPage();
        boolean deleteOrderStatus = OrderHistoryPage.deleteOrder();

        step("Verify delete order in order history");
        Assertion.assertThat("Order is deleted and is not on Order History", deleteOrderStatus, is(true));
    }

    @Author(names = {"Daffa"})
    @Module(name = "MyOrder")
    @Tribe(name = "Order")
    @Vertical(name = "PRO")
    @Test
    public void verifyBusOrderDetail()
            throws FailedLoginException, InterruptedException, ExecutionException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyBusOrderDetail");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        Unm<?> unm = new Unm<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String orderId = param.get("orderId");

        step("Go to home page");
        homePage.goToHomePage();

        step("Check login state and log in");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("Get internal data for the issued order");
        String bookingCode = orderDetailPage.getVerticalData(orderId).get("bookingCode");
        String totalPrice = orderDetailPage.getTotalAmount(orderId);
        String departureTerminal = orderDetailPage.getVerticalData(orderId).get("busDepartureTerminal");
        String arrivalTerminal = orderDetailPage.getVerticalData(orderId).get("busArrivalTerminal");

        step("Open my order page");
        homePage.goToMyOrderPage();

        step("Find the issued order");
        orderListPage.openActiveTicketViaURL(orderId);
        orderDetailPage.skipCoachmark();

        step("Verify bus order details");
        Assertion.assertThat("E-ticket card Displayed", orderDetailPage.isETicketCardDisplayed(), is(true));
        Assertion.assertThat("Booking code is showing", orderDetailPage.verifyBookingCodeBus(), equalToCompressingWhiteSpace(bookingCode));
        Assertion.assertThat("Order ID is displayed correctly", orderDetailPage.getOrderId(),containsString(orderId));
        Assertion.assertThat("Verify bus departure terminal", orderDetailPage.verifyBusDepartureTerminal(), equalToIgnoringCase(departureTerminal));
        Assertion.assertThat("Verify bus arrival terminal", orderDetailPage.verifyBusArrivalTerminal(), equalToIgnoringCase(arrivalTerminal));
        Assertion.assertThat("Verify passenger detail in order detail page", orderDetailPage.passengerAndFacilitiesExists(), is(true));
        Assertion.assertThat("Verify bus & shuttle Total price", CoreFunctions.convertPriceSafely(orderDetailPage.getVerticalTotalPrice()), equalTo(CoreFunctions.convertPriceSafely(totalPrice)));
        Assertion.assertThat("E-ticket button should be displayed in Manage Order", orderDetailPage.verifyEticketManageOrder(), is(true));
    }
    
    @Author(names = {"Ram","Ishika"})
    @Module(name = "MyOrder")
    @Tribe(name = "Order")
    @Vertical(name = "PRO")
    @Test
    public void verifyFlightOrderGroup() throws FailedLoginException, InterruptedException, ExecutionException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyFlightOrderGroup");
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPageWeb = FlightLandingPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        FlightSearchResultPageWeb<?> flightSearchResultPage = FlightSearchResultPageWeb.getInstance(session) ;
        FlightDescriptionPageWeb<?> flightDescriptionPage = FlightDescriptionPageWeb.getInstance(session) ;
        FlightProductReviewPageWeb<?> flightProductReviewPage = FlightProductReviewPageWeb.getInstance(session) ;
        FlightBookingFormPageWeb<?> flightBookingFormPage = FlightBookingFormPageWeb.getInstance(session) ;
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session) ;

        String currency = param.get("currency");
        String name = param.get("name");

        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int duration = Integer.parseInt(param.get("duration"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate,daysToDepartureDate);
        String returnDate = CoreFunctions.addToLongDate(departureDate, duration);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));

        step("User open home page");
        homePage.goToHomePage();

        step("Login as " + email + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(email, password);

        step("User select IDR currency");
        homePage.selectCurrency(currency);

        step("User open Flight vertical");
        homePage.goToFlightPage();

        step("Toggle trip type");
        flightLandingPageWeb.toggleTripType(tripType);

        step("Select origin airport");
        flightLandingPageWeb.selectOrigin(originAirport);

        step("Select destination airport");
        flightLandingPageWeb.selectDestination(destinationAirport);

        step("Select date");
        flightLandingPageWeb.selectDate(departureDate,returnDate);

        step("search flight");
        flightLandingPageWeb.searchFlight();

        step("select flight");
        flightSearchResultPage.selectFlight();

        step("select ticket on pdp page for round trip");
        flightDescriptionPage.selectTicket();

        step("Click Login Profile Icon");
        flightDescriptionPage.clickLoginProfileIcon();

        step("select flight");
        flightSearchResultPage.selectFlightSecondCard();

        step("select ticket on pdp page for round trip");
        flightDescriptionPage.selectTicket();

        step("Continue to booking form");
        flightProductReviewPage.continueToBookingForm();

        step("Skip Flight may rescheduled page by clicking no thanks");
        flightBookingFormPage.clickNoThanksButton();

        step("Click toggle to fill passengers details and save ");
        flightBookingFormPage.fillPassengerDetailsLogin(name);

        step("Go to payment page");
        flightBookingFormPage.goToPaymentPageFromBookingFormPage();

        step("Get Order ID");
        String orderID = paymentPage.getOrderId() ;

        step("Get total payment from payment page");
        String totalPayment = paymentPage.getFinalPrice();

        step("User open home page");
        homePage.goToHomePage();

        step("Open my order page");
        homePage.goToMyOrderPage();

        step("Verify order id is as expected in order group page");
        Assertion.assertThat("Verify order id is as expected", orderDetailPage.getOrderIdFlight(), containsString(orderID));

        step("Open the ongoing order page for roundtrip") ;
        orderListPage.openOrderDetailPage(orderID);

        step("Verify waiting payment component showing in order group page");
        Assertion.assertThat("Verify waiting payment component showing", orderDetailPage.verifyCompletePaymentExpiryTime(), is(true));

        step("verify origin data in order group page");
        String flightDepatureDate = orderDetailPage.getDepartureDate() ;
        String flightReturnDate = orderDetailPage.getReturnDate();
        String departureAirline = orderDetailPage.getOrderGroupDepartureAirline();
        String returnAirline = orderDetailPage.getOrderGroupReturnAirline();
        String departAirportname = orderDetailPage.geDepartAirportOrderGroup();
        String returnAirportName = orderDetailPage.getReturnAirportOrderGroup();
        Assertion.assertThat("depart airport details should be same", departAirportname, containsString(originAirport));
        Assertion.assertThat("return airport details should be same", returnAirportName, containsString(destinationAirport));
        Assertion.assertThat("departflight date should be as expected",flightDepatureDate, is(notNullValue()));
        Assertion.assertThat("flightReturnDate date should be as expected",flightReturnDate, is(notNullValue()));
        Assertion.assertThat("Verify departure airline", departureAirline, is(notNullValue()));
        Assertion.assertThat("Verify return airline", returnAirline, is(notNullValue()));

        step("Verify price detail open in order group page");
        String totalPaymentOrderPreview = orderDetailPage.getTotalPaymentInFlightPreviewPage();
        Assertion.assertThat("Verify total payment in order detail", CoreFunctions.convertPriceSafely(totalPaymentOrderPreview), equalTo(CoreFunctions.convertPriceSafely(totalPayment)));

        step("Verify price detail component in order group page");
        orderDetailPage.openFlightGroupPriceDetail();
        Assertion.assertThat("Verify price detail component", orderDetailPage.verifyFlightPriceDetailBreakDown(totalPayment), is(true));

        step("Open flight depart detail");
        orderDetailPage.clickFlightDepartOrderDetail();

        step("Verify depart order detail");
        String flightDepatureDateOrderDetail = orderDetailPage.getDepartureDateOrderDetail();
        String flightArrivalDateOrderDetail = orderDetailPage.getArrivalDateOrderDetail();
        String originAirportOrderDetail = orderDetailPage.getOriginAirportOrderDetail();
        String destinationAirportOrderDetail = orderDetailPage.getDestinationAirportOrderDetail();
        Assertion.assertThat("origin airport details should be same",originAirportOrderDetail,containsString(originAirport) );
        Assertion.assertThat("origin airport details should be same",destinationAirportOrderDetail,containsString(destinationAirport) );
        Assertion.assertThat("Departure flight depart time", flightDepatureDateOrderDetail, is(notNullValue()));
        Assertion.assertThat("Departure flight arrival time", flightArrivalDateOrderDetail, is(notNullValue()));
        String totalPaymentOrderDetail = orderDetailPage.getTotalPaymentInFlightPreviewPage();
        Assertion.assertThat("Verify total payment in order detail", CoreFunctions.convertPriceSafely(totalPaymentOrderDetail), equalTo(CoreFunctions.convertPriceSafely(totalPayment)));

        step("back to order group page");
        orderDetailPage.clickBack();

        step("Click flight return detail");
        orderDetailPage.clickFlightReturnOrderDetail();

        step("Verify return order detail");
        flightDepatureDateOrderDetail = orderDetailPage.getDepartureDateOrderDetail();
        flightArrivalDateOrderDetail = orderDetailPage.getArrivalDateOrderDetail();
        originAirportOrderDetail = orderDetailPage.getOriginAirportOrderDetail();
        destinationAirportOrderDetail = orderDetailPage.getDestinationAirportOrderDetail();
        Assertion.assertThat("origin airport details should be same",originAirportOrderDetail,containsString(destinationAirport) );
        Assertion.assertThat("origin airport details should be same",destinationAirportOrderDetail,containsString(originAirport) );
        Assertion.assertThat("Departure flight depart time", flightDepatureDateOrderDetail, is(notNullValue()));
        Assertion.assertThat("Departure flight arrival time", flightArrivalDateOrderDetail, is(notNullValue()));
        totalPaymentOrderDetail = orderDetailPage.getTotalPaymentInFlightPreviewPage();
        Assertion.assertThat("Verify total payment in order detail", CoreFunctions.convertPriceSafely(totalPaymentOrderDetail), equalTo(CoreFunctions.convertPriceSafely(totalPayment)));

        step("back to order group page");
        orderDetailPage.clickBack();

        step("Check redirection to complete payment");
        orderDetailPage.completePaymentFromOrderGroupPaymentPage();
        Assertion.assertThat("Payment Page Loaded", paymentPage.isPaymentPageLoaded(), is(true));
    }

    @Author(names = {"Daffa"})
    @Module(name = "MyOrder")
    @Tribe(name = "Order")
    @Vertical(name = "Liberty")
    @Test
    public void checkShareEticket()
            throws FailedLoginException, InterruptedException, ExecutionException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("checkShareEticket");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        orderCreationPage<?> orderCreationPage = new orderCreationPage<>(session);
        Unm<?> unm = new Unm<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));

        String hotelName = param.get("hotelName");
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));
        int room = Integer.parseInt(param.get("room"));
        String startDate = generateRandomDate(param.get("rangeAddDate"));

        String currency = param.get("currency");
        String emailUser = param.get("emailUser");


        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = getRandomObject(salutationList);

        step("Go to home page");
        homePage.goToHomePage();

        step("Check login state and log in");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("Get api access token");
        LoginDataUnmResponse.Data authData = unm.unmLoginApi(email, password);
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
                        false,
                        false,
                        OrderStatus.ETICKET_ISSUED);
        String bookedOrderID;

        if (activeOrderData == null) {
            step("Create a hotel order with issued status");
            orderCreationPage.createHotelSakukuIssuedOrderApi(
                    email, password, startDate, hotelName, adult, night, room,
                    orderFullName, orderNameTitle, guestFullName, guestTitle,currency
            );
            bookedOrderID = String.valueOf(orderCreationPage.getOrderId());
            Assertion.assertThat("Success to create issued order", bookedOrderID,
                    is(not(emptyOrNullString())));
        } else {
            bookedOrderID = activeOrderData.orderId;
        }

        step("Find the issued order");
        orderListPage.openActiveTicketViaURL(bookedOrderID);
        orderDetailPage.skipCoachmark();

        step("Open e-Ticket from header and verify");
        orderDetailPage.clickShareEticket();
        Assertion.assertThat("Verify snackbar share eticket showing", orderDetailPage.shareEticketWithEmail(emailUser), is(true));
    }

    @Author(names = {"Rizki Maulana"})
    @Module(name = "MyOrder")
    @Tribe(name = "Order")
    @Vertical(name = "PRO")
    @Test
    public void verifyETicketMultilanguage() throws FailedLoginException, InterruptedException, ExecutionException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyETicketMultilanguage");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        orderCreationPage<?> orderCreationPage = new orderCreationPage<>(session);
        Unm<?> unm = new Unm<>(session);

        String email = param.get("email");
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

        step("Go to home page");
        homePage.goToHomePage();

        step("Check login state and log in");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("Get api access token");
        LoginDataUnmResponse.Data authData = unm.unmLoginApi(email, password);
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
                        false,
                        false,
                        OrderStatus.ETICKET_ISSUED);


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

        step("Find the issued order");
        orderListPage.openActiveTicketViaURL(bookedOrderID);
        orderDetailPage.skipCoachmark();

        step("Verify if E-Ticket is multilanguage");
        Assertion.assertThat("Verify 'Your e-ticket is here' span is presence", orderDetailPage.verifyYourETicketIsHereSpanShown(), is(true));
        Assertion.assertThat("Verify View E-ticket span is presence", orderDetailPage.verifyIssuedETicket(), is(true));
        Assertion.assertThat("Verify E-Ticket button in Manage Order is presence", orderDetailPage.verifyManageOrderETicketShown(), is(true));

        orderDetailPage.clickViewETicketButton();
        Assertion.assertThat("Verify View E-ticket modal from View E-ticket span contains English and Bahasa Indonesia option", orderDetailPage.verifyETicketModalContainsEnglishAndBahasaOption(), is(true));
        orderDetailPage.closeETicketPopup();

        orderDetailPage.clickToManageOrderEticket();
        Assertion.assertThat("Verify View E-ticket modal from Manage Order contains English and Bahasa Indonesia option", orderDetailPage.verifyETicketModalContainsEnglishAndBahasaOption(), is(true));
    }

    @Author(names = {"Jashan,Rizky"})
    @Module(name = "MyOrder")
    @Tribe(name = "Order")
    @Vertical(name = "Liberty")
    @Test
    public void verifyGiftCardOrder() throws FailedLoginException, InterruptedException {
        Map<String, String> map = getParam("verifyGiftCardOrder");
        BaseTest.paramsThreadLocal.set(map);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePageWeb<?> homePageWeb = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        GiftCardPageWeb<?> giftCardPage = GiftCardPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);

        String email = map.get("email");
        String passwordDweb = map.get("passwordDweb");
        String currency = map.get("currency");
        String amount = map.get("amount");
        String recipientName = map.get("recipientName");
        String giftCardName = map.get("giftCardName");
        String senderName = map.get("senderName");
        String expectedDeliveryDateTime = map.get("expectedDeliveryDateTime");
        String phoneNumber = map.get("phoneNumber");
        String paymentGroup = map.get("paymentGroup");
        String paymentMethod = map.get("paymentMethod");
        String otp = map.get("otp");
        int retryCounter = Integer.parseInt(map.get("retryCounter"));

        step("Login as " + email + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(email, passwordDweb);

        step("Verify user is logged in and home page is opened");
        homePageWeb.waitForHomePageToOpen();

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
        homePageWeb.goToHomePage();
        homePageWeb.goToMyOrderPage();
        Assertion.assertThat("Verify gift card order is present in the order page.", orderListPage.isOrderIssued(), is(true));

        step("Open Gift Card order detail and verify components");
        orderListPage.clickFirstOrderCard();
        Assertion.assertThat("Gift Card name showing correctly on Gift Card detail", orderDetailPage.isGiftCardNameDisplayed(giftCardName), is(true));
        Assertion.assertThat("Test data 'expectedDeliveryDateTime' must be provided and non-empty", expectedDeliveryDateTime, not(emptyOrNullString()));
        Assertion.assertThat("Gift Card sender showing correctly on Gift Card detail", orderDetailPage.isGiftCardSenderDisplayed(senderName), is(true));
        Assertion.assertThat("Gift Card recipient showing correctly on Gift Card detail", orderDetailPage.isGiftCardRecipientDisplayed(recipientName), is(true));
        Assertion.assertThat("Receipt button should be displayed", orderDetailPage.isGiftCardReceiptButtonDisplayed(), is(true));
        Assertion.assertThat("Page Module should be displayed", orderDetailPage.verifyPageModule(), is(true));
    }

    @Author(names = {"Yazid"})
    @Module(name = "Post Purchase")
    @Tribe(name = "Order")
    @Vertical(name = "PRO")
    @Test
    public void verifyPostPurchaseInsuranceOrderDetail() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyPostPurchaseInsuranceOrderDetail");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);

        String email = param.get("email");
        String password = param.get("password");
        String orderId = param.get("orderId");
        String relatedVerticalName = param.get("relatedVerticalName");

        step("Go to home page");
        homePage.goToHomePage();

        step("Login as " + email);
        loginPage.loginWithSpecificAccountViaCookie(email, password);

        step("Open issued insurance order detail");
        orderListPage.openActiveTicketViaURL(orderId);
        orderDetailPage.skipCoachmark();

        String orderDetailId = orderListPage.getOrderIdHashAndDetailId(orderId).get("orderDetailId");

        step("Verify insurance order detail components");
        Assertion.assertThat("E-ticket card Displayed", orderDetailPage.isETicketCardDisplayed(), is(true));
        Assertion.assertThat("Order ID is displayed correctly", orderDetailPage.getOrderId(), containsString(orderId));

        step("Verify Claim center button redirect to insurance webview");
        Assertion.assertThat("Claim center button should be displayed", orderDetailPage.isClaimCenterButtonDisplayed(), is(true));
        String claimCenterUrl = orderDetailPage.clickClaimCenterButtonAndGetUrl();
        Assertion.assertThat("Claim center should redirect to insurance claim webview", claimCenterUrl, allOf(
                containsString("insurance-claim"),
                containsString("orderDetailId=" + orderDetailId),
                containsString("orderId=" + orderId)));

        step("Back to origin insurance order detail");
        orderListPage.openActiveTicketViaURL(orderId);

        step("Verify Insurance Name is displayed");
        Assertion.assertThat("Insurance Name should not be empty", orderDetailPage.getInsuranceProductName(), not(emptyOrNullString()));

        step("Verify See policy and coverage button redirect to insurance FAQ webview");
        Assertion.assertThat("See policy and coverage button should be displayed", orderDetailPage.isSeePolicyAndCoverageButtonDisplayed(), is(true));
        String policyUrl = orderDetailPage.clickSeePolicyAndCoverageButtonAndGetUrl();
        Assertion.assertThat("See policy and coverage should redirect to insurance FAQ webview", policyUrl,
                matchesPattern(".*insurance-claim/faq/\\d+.*"));

        step("Back to origin insurance order detail");
        orderListPage.openActiveTicketViaURL(orderId);

        step("Verify Related order section and redirect to parent order");
        Assertion.assertThat("Related order vertical name should match", orderDetailPage.getRelatedOrderVerticalName(), containsStringIgnoringCase(relatedVerticalName));
        orderDetailPage.clickRelatedOrder();
        String relatedOrderUrl = orderDetailPage.getCurrentURL();
        Assertion.assertThat("Related order URL should contain orderId", relatedOrderUrl, containsString("orderId="));
        Assertion.assertThat("Related order should redirect to a different order", relatedOrderUrl, not(containsString("orderId=" + orderId)));
        Assertion.assertThat("Add-ons popup should be displayed on parent order", orderDetailPage.isAddOnsPopupDisplayed(), is(true));

        step("Back to origin insurance order detail");
        orderListPage.openActiveTicketViaURL(orderId);

        step("Verify Manage Order and Page Module sections");
        Assertion.assertThat("E-Ticket button should be displayed", orderDetailPage.verifyEticketManageOrder(), is(true));
        Assertion.assertThat("Receipt button should be displayed", orderDetailPage.isReceiptButtonDisplayed(), is(true));
        Assertion.assertThat("Page Module should be displayed with more than 1 element", orderDetailPage.getPageModuleViewCount(), greaterThan(1));
    }

    @Author(names = {"Yazid"})
    @Module(name = "Email Notification")
    @Tribe(name = "Order")
    @Vertical(name = "Liberty")
    @Test
    public void verifyReceiveEvoucherAndReceiptEmail()
            throws FailedLoginException, InterruptedException, ExecutionException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyReceiveEvoucherAndReceiptEmail");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        TTDApi<?> ttdApi = new TTDApi<>(session);

        String email = param.getOrDefault("email", param.get("username"));
        String password = param.get("password");
        String currency = param.get("currency");
        String ttdProductUrlName = param.get("ttdProductUrlName");
        boolean refundable = Boolean.parseBoolean(param.getOrDefault("refundable", "true"));

        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String contactFullName = randomName() + " autorder";
        String contactSalutation = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestEmailAddress = randomEmail();
        String guestSalutation = salutationList[0];

        step("Go to home page");
        homePage.goToHomePage();

        step("Login as " + email);
        loginPage.loginWithSpecificAccountViaCookie(email, password);

        step("Create new TTD issued order via API");
        String bookedOrderId = ttdApi.createIssuedOrderSakukuApi(email, password, currency, ttdProductUrlName, refundable,
                contactFullName, contactSalutation, guestFullName, guestEmailAddress, guestSalutation);
        Assertion.assertThat("TTD booking via API is successful", bookedOrderId, not(blankOrNullString()));

        step("Open issued order detail and verify order is issued");
        orderListPage.openActiveTicketViaURL(bookedOrderId);
        orderDetailPage.skipCoachmark();
        Assertion.assertThat("Order should be issued after payment", orderDetailPage.verifyIssuedETicket(), is(true));
        Assertion.assertThat("Order ID on order detail should match booked order", orderDetailPage.getOrderId(), is(bookedOrderId));

        step("Verify user receives e-voucher email");
        String tiketWindow = session.driver().getWindowHandle();
        InboxPageYopmail<?> emailInboxPage = new InboxPageYopmail<>(session);
        emailInboxPage.openYopmailInboxInNewTab(email);
        Assertion.assertThat("User should receive e-voucher email in Yopmail inbox",
                emailInboxPage.verifyEvoucherEmailReceived(bookedOrderId), is(true));

        step("Verify user receives order-receipt email");
        emailInboxPage.refreshInbox();
        Assertion.assertThat("User should receive order-receipt email in Yopmail inbox",
                emailInboxPage.verifyOrderReceiptEmailReceived(bookedOrderId), is(true));
        emailInboxPage.closeYopmailAndReturnTo(tiketWindow);
    }

    @Author(names = {"Yazid"})
    @Module(name = "Email Notification")
    @Tribe(name = "Order")
    @Vertical(name = "Liberty")
    @Test
    public void verifyReceiveEvoucherAndReceiptEmailNonLogin() throws InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyReceiveEvoucherAndReceiptEmailNonLogin");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        TtdLandingPageWeb<?> ttdLandingPageWeb = TtdLandingPageWeb.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);
        TtdBookingFormPageWeb<?> ttdBookingFormPageWeb = TtdBookingFormPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);

        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String salutation = getRandomObject(salutationList);
        String fullName = randomName() + " autorder";
        String phoneNumber = randomPhone();
        String email = param.get("email"); // email should be from yopmail and not registered on Tiket.com
        String currency = param.get("currency");
        String product = param.get("product");
        String packageName = param.get("packageName");
        String paymentGroup = param.get("paymentGroup");
        String paymentMethod = param.getOrDefault("paymentMethod", "Sakuku");

        step("Ensure session is non-login");
        loginPage.logoutNeeded();

        step("Change currency to " + currency);
        homePage.selectCurrency(currency);

        step("Go to TTD landing page");
        ttdLandingPageWeb.goToTtdLandingPage();

        step("Search and select TTD product");
        ttdLandingPageWeb.inputSearchKeyword(product);
        ttdLandingPageWeb.selectRecommendedActivity(product);

        step("Book TTD package");
        ttdProductDetailPageWeb.findPackage(packageName);
        ttdProductDetailPageWeb.chooseTicket(packageName);
        ttdProductDetailPageWeb.setTicketQty(1, 0);
        ttdProductDetailPageWeb.clickBookButton();

        step("Fill contact details with email from test data");
        ttdBookingFormPageWeb.fillContactDetailsNonLogin(salutation, fullName, phoneNumber, email);
        ttdBookingFormPageWeb.continueToPaymentPage();

        step("Pay using "+paymentMethod+" until order is issued");
        String bookedOrderId = paymentPage.getOrderId();
        Assertion.assertThat("Order ID should be generated on payment page", bookedOrderId, not(blankOrNullString()));
        paymentPage.clickOnSelectPaymentMethod();
        paymentPage.goToPaymentMethodsGroup(paymentGroup);
        paymentPage.selectPaymentMethod(paymentMethod);
        paymentPage.clickOnPaymentButton();
        Assertion.assertThat("Payment successful page should be visible", paymentPage.isPaymentSuccessfulVisible(), is(true));

        step("Verify user receives e-voucher email");
        String tiketWindow = session.driver().getWindowHandle();
        InboxPageYopmail<?> emailInboxPage = new InboxPageYopmail<>(session);
        emailInboxPage.openYopmailInboxInNewTab(email);
        Assertion.assertThat("User should receive e-voucher email in Yopmail inbox",
                emailInboxPage.verifyEvoucherEmailReceived(bookedOrderId), is(true));

        step("Verify user receives order-receipt email");
        emailInboxPage.refreshInbox();
        Assertion.assertThat("User should receive order-receipt email in Yopmail inbox",
                emailInboxPage.verifyOrderReceiptEmailReceived(bookedOrderId), is(true));
        emailInboxPage.closeYopmailAndReturnTo(tiketWindow);
    }

    @Author(names = {"Vanshika Agarwal"})
    @Module(name = "Trip view")
    @Tribe(name = "Order")
    @Vertical(name = "Liberty")
    @Test
    public void verifyTripViewSection() throws FailedLoginException, InterruptedException, ExecutionException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyTripViewSection");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        TTDApi<?> ttdApi = new TTDApi<>(session);
        TrainPage<?> trainPage = new TrainPage<>(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        OrderDetailWeb<?> orderDetailPageWeb = OrderDetailWeb.getInstance(session);

        String phone = param.get("phone");
        String originCity = param.get("originCity");
        String destinationCity = param.get("destinationCity");

        String tripType = "oneway";
        int adult = Integer.parseInt(param.get("adult"));
        int infant = Integer.parseInt(param.get("infant"));
        String idNumber = param.getOrDefault("idNumber", "3175031201960001");
        String hotelName = param.get("hotelName");
        int night = 1;
        int room = Integer.parseInt(param.get("room"));
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String startDate = getJakartaDateYYYY_MM_DD(daysToDepartureDate);
        String ttdProductUrlName = param.get("ttdProductName");
        String currency = param.getOrDefault("currency", "IDR");

        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = getRandomObject(salutationList);
        String guestEmailAddress = randomEmail();
        String bookedOrderIDHotel = "";
        String bookedOrderIdTTD = "";
        String bookedOrderIDTrain = "";

        step("create a new account");
        Unm<WebDriver> unm = new Unm<>(session);

        RegisterNewAccountData accountData = unm.registerNewAccountViaApiUNM();
        String email = accountData.email;
        String password = accountData.password;


        try {
            step("Go to home page");
            homePage.goToHomePage();
            homePage.selectEnglishLanguageOnly();

            step("Login as " + email + " via session injection");
            loginPage.loginWithSpecificAccountViaCookie(email, password);

            step("Create new hotel issued order");
            try {
                hotelApi.createIssuedOrderApiWithProtection(
                        email, password, startDate, hotelName, adult, night, room,
                        orderFullName, orderNameTitle, guestFullName, guestTitle, currency);
                bookedOrderIDHotel = String.valueOf(hotelApi.getOrderId());
                Assertion.assertThat("Hotel issued order creation successful", bookedOrderIDHotel, not(emptyOrNullString()));
            } catch (Exception err) {
                orderDetailPageWeb.logOrderCreationFailure("Hotel", err);
            }

            step("Create new TTD issued order via API");
            try {
                bookedOrderIdTTD = ttdApi.createIssuedOrderSakukuApi(
                        email, password, currency, ttdProductUrlName.replace(" ","-"), false,
                        orderFullName, orderNameTitle, guestFullName, guestEmailAddress, guestTitle, startDate
                );
                Assertion.assertThat("TTD booking via API successful", bookedOrderIdTTD, not(blankOrNullString()));
            } catch (Exception err) {
                orderDetailPageWeb.logOrderCreationFailure("TTD", err);
            }

            step("Create a train order with issued status");
            try {
                bookedOrderIDTrain = trainPage.createIssuedOrderApi(
                        email, password, orderFullName, phone, idNumber,
                        originCity, destinationCity, startDate, tripType, adult, infant
                );
                Assertion.assertThat("Train booking via API successful", bookedOrderIDTrain, not(blankOrNullString()));
            } catch (Exception err) {
                orderDetailPageWeb.logOrderCreationFailure("Train", err);
            }

            step("Create a hotel order (waiting for payment)");
            try {
                hotelApi.createWaitingPaymentOrderApi(
                        email, password, startDate, hotelName, adult, night, room, currency, 0, "hotel", "en");
            } catch (Exception err) {
                orderDetailPageWeb.logOrderCreationFailure("Hotel (waiting for payment)", err);
            }

            step("Assert the 3 booking");
            Assertion.assertThat("Valid bookings ", !bookedOrderIDTrain.isEmpty() && !bookedOrderIdTTD.isEmpty() && !bookedOrderIDHotel.isEmpty(), is(true));

            step("Navigate to My Order page and skip coachmarks");
            homePage.selectEnglishLanguageOnly();
            homePage.goToMyOrderPage();
            orderDetailPageWeb.skipCoachmark();

            step("Navigate to Your Itineraries section");
            Assertion.assertThat("Your Itineraries tab is active", orderDetailPageWeb.isYourItinerariesTabActive(), is(true));

            step("Verify Expand / Collapse functionality for trip group");
            boolean initialExpanded = orderDetailPageWeb.isTripSectionExpanded();
            orderDetailPageWeb.toggleTripHeader();
            Assertion.assertThat("Trip section toggle toggled state correctly", orderDetailPageWeb.isTripSectionExpanded(), is(!initialExpanded));
            orderDetailPageWeb.ensureTripSectionExpanded();

            step("Verify Grouping: Explicitly check that Hotel, TTD, and Train order card headings are present in the trip group");
            boolean isHotelCardPresent = orderDetailPageWeb.isOrderCardPresentInTripView(hotelName);
            boolean isTTDCardPresent = orderDetailPageWeb.isOrderCardPresentInTripView(ttdProductUrlName);
            boolean isTrainCardPresent = orderDetailPageWeb.isTrainOrderCardPresentInTripView(originCity);
            Assertion.assertThat("Hotel order card ('" + hotelName + "') is present in the trip group", isHotelCardPresent, is(true));
            Assertion.assertThat("TTD order card ('" + ttdProductUrlName + "') is present in the trip group", isTTDCardPresent, is(true));
            Assertion.assertThat("Train order card ('" + originCity + "') is present in the trip group", isTrainCardPresent, is(true));

            step("Verify Hotel Card Data");
            boolean isHotelValid = orderDetailPageWeb.verifyTripCardData(hotelName, startDate, guestFullName);
            Assertion.assertThat("Hotel card displays correct hotel name, dates, and guest name", isHotelValid, is(true));

            step("Verify TTD Card Data");
            boolean isTTDValid = orderDetailPageWeb.verifyTripCardData(ttdProductUrlName, startDate, adult + " pax");
            Assertion.assertThat("TTD card displays correct product name, date, and pax count", isTTDValid, is(true));

            step("Verify Train Card Data");
            boolean isTrainValid = orderDetailPageWeb.verifyTrainTripCardData(originCity, destinationCity, orderFullName);
            Assertion.assertThat("Train card displays correct locations, times, and passenger name", isTrainValid, is(true));

            step("Verify High and Low Profile Cross Sell in trip groups");
            boolean isLowProfileCrossSellVisible = orderDetailPageWeb.isLowProfileCrossSellBannerDisplayed();
            Assertion.assertThat("Low-profile cross-sell banner is displayed", isLowProfileCrossSellVisible, is(true));
            boolean isHighProfileCrossSellVisible = orderDetailPageWeb.isHighProfileCrossSellBannerDisplayed();
            Assertion.assertThat("High-profile cross-sell banner is displayed", isHighProfileCrossSellVisible, is(true));

            step("Verify TTD E-Ticket flow");
            orderDetailPageWeb.clickCardViewETicketByTitle(ttdProductUrlName);
            boolean isTTDETicketValid = orderDetailPageWeb.verifyTTDETicketAndGoBack();
            Assertion.assertThat("TTD E-Ticket page is displayed correctly", isTTDETicketValid, is(true));
            orderDetailPageWeb.ensureTripSectionExpanded();

            step("Verify Train E-Ticket flow ");
            orderDetailPageWeb.clickCardViewETicketByStation(originCity);
            boolean isTrainETicketOpened = orderDetailPageWeb.verifyETicketInNewTabAndClose();
            Assertion.assertThat("Train E-Ticket opened in a new tab", isTrainETicketOpened, is(true));

            step("Verify Hotel E-Ticket flow ");
            orderDetailPageWeb.clickCardViewETicketByTitle(hotelName);
            orderDetailPageWeb.selectEnglishLanguageForHotelETicket();
            boolean isHotelETicketOpened = orderDetailPageWeb.verifyETicketInNewTabAndClose();
            Assertion.assertThat("Hotel E-Ticket opened in a new tab from language selector", isHotelETicketOpened, is(true));
            orderDetailPageWeb.clickBackFromLanguageSelector();
            orderDetailPageWeb.ensureTripSectionExpanded();

            step("Verify Order Detail Navigation by clicking order card");
            orderDetailPageWeb.clickTripOrderCard(hotelName);
            Assertion.assertThat("Navigated to Order Detail page", orderDetailPageWeb.getCurrentURL(), containsString("detail"));

            step("Navigate back to Your Orders -> Your Itineraries");
            orderDetailPageWeb.hidePromoBannerIfPresent();
            orderDetailPageWeb.clickBack();
            orderDetailPageWeb.clickYourItinerariesTab();
            Assertion.assertThat("Your Itineraries tab is active", orderDetailPageWeb.isYourItinerariesTabActive(), is(true));

            step("Verify 'See All Orders' button navigates to Active Orders list");
            orderDetailPageWeb.clickSeeAllOrdersInTripView();
            Assertion.assertThat("Active Orders tab active after clicking See All Orders", !orderDetailPageWeb.isYourItinerariesTabActive(), is(true));

            step("Verify Section Navigation: Seamless switching between Itineraries and Active Tickets");
            orderDetailPageWeb.clickYourItinerariesTab();
            Assertion.assertThat("Switched back to Your Itineraries section", orderDetailPageWeb.isYourItinerariesTabActive(), is(true));
            orderDetailPageWeb.clickActiveOrdersTab();
            Assertion.assertThat("Switched to Active Orders section", !orderDetailPageWeb.isYourItinerariesTabActive(), is(true));
        }finally {
            step("go to home");
            homePage.goToHomePage();
            loginPage.loginAsGuest();

            step("delete account");
            unm.deleteAccountViaApi(accountData.unmData);
        }
    }
}