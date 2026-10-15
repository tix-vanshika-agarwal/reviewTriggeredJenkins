package com.tiket.test.platform.liberty.my_order;

import com.tiket.apiPage.HotelApi;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.apiPage.*;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Environment;
import com.tiket.model.Platform;
import com.tiket.model.Session;
import com.tiket.page.accommodation.homes.HomesPage;
import com.tiket.page.accommodation.homes.homeslandingpage.HomesLandingPage;
import com.tiket.page.accommodation.hotel.HotelPage;
import com.tiket.page.accommodation.hotel.bookingformpage.BookingFormPage;
import com.tiket.page.accommodation.hotel.detailpage.DetailPage;
import com.tiket.page.accommodation.hotel.landingpage.LandingPage;
import com.tiket.page.accommodation.hotel.roomlistpage.RoomListPage;
import com.tiket.page.account.AccountPage;
import com.tiket.page.bus.BusPage;
import com.tiket.page.bus.bookingform.BusBookingFormPage;
import com.tiket.page.bus.pdp.BuspdpPage;
import com.tiket.page.bus.search.BusSearchPage;
import com.tiket.page.bus.searchresultpage.BusSearchResultPage;
import com.tiket.page.carrental.CarRentalPage;
import com.tiket.page.carrental.bookingpage.CarRentalBookingPage;
import com.tiket.page.carrental.reviewbookingpage.CarRentalReviewBookingPage;
import com.tiket.page.carrental.searchpage.SearchPageCarRental;
import com.tiket.page.carrental.searchresultpage.SearchResultPageCarRental;
import com.tiket.page.cartransfer.bookingform.CarTransferBookingPage;
import com.tiket.page.cartransfer.searchpage.CarTransferSearchPage;
import com.tiket.page.cartransfer.searchresultpage.CarTransferSearchResultPage;
import com.tiket.page.common.webview.WebViewPage;
import com.tiket.page.ferry.FerryBookingPage.FerryBookingPage;
import com.tiket.page.ferry.FerryPage;
import com.tiket.page.ferry.FerrySearchPage.FerrySearchPage;
import com.tiket.page.ferry.FerrySearchresultPage.FerrySearchResultPage;
import com.tiket.page.flight.FlightPage;
import com.tiket.page.flight.bookingform.FlightBookingFormPage;
import com.tiket.page.flight.prp.FlightProductReviewPage;
import com.tiket.page.flight.searchpage.FlightSearchPage;
import com.tiket.page.flight.searchresultpage.FlightSearchResultPage;
import com.tiket.page.home.HomePage;
import com.tiket.page.login.LoginPage;
import com.tiket.page.order.orderdetail.OrderDetailPage;
import com.tiket.page.order.orderhistory.OrderHistoryPage;
import com.tiket.page.order.orderlist.OrderListPage;
import com.tiket.page.order.orderlist.OrderListPage.OrderStatus;
import com.tiket.page.order.trip.TripPage;
import com.tiket.page.payment.PaymentPage;
import com.tiket.page.setting.SettingPage;
import com.tiket.page.train.TrainPage;
import com.tiket.page.train.bookingform.TrainBookingPage;
import com.tiket.page.train.searchpage.TrainSearchPage;
import com.tiket.page.train.searchresultpage.TrainSearchResultPage;
import com.tiket.page.ttd.TTDPage;
import com.tiket.page.ttd.landingpage.TTDLandingPage;
import com.tiket.page.ttd.productdetailpage.TTDProductDetailPage;
import com.tiket.page.ttd.searchpage.TTDSearchPage;
import com.tiket.page.whoosh.bookingform.WhooshBookingPage;
import com.tiket.page.whoosh.searchpage.WhooshSearchPage;
import com.tiket.page.whoosh.searchresultpage.WhooshSearchResultPage;
import com.tiket.pojo.accommodation.hotel.HotelAutoCompleteResponse;
import com.tiket.pojo.bus.BusCheckoutResponse;
import com.tiket.pojo.cartransfer.TripDetails;
import com.tiket.pojo.flight.FlightDynamicData;
import com.tiket.pojo.order.HotelInternalOrderDetail;
import com.tiket.pojo.order.MapVerticalWithOrderId;
import com.tiket.pojo.train.TrainData;
import com.tiket.pojo.unm.LoginDataUnmResponse;
import com.tiket.pojo.whoosh.TrainAndWhooshGeneralData;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.testbase.Configs;
import com.tiket.web.page.ttd.TtdBookingFormPage.TtdBookingFormPageWeb;
import com.tiket.web.page.ttd.TtdProductDetailPage.TtdProductDetailPageWeb;
import com.tiket.web.page.ttd.TtdSelectedPackageDetailPage.TtdSelectedPackageDetailPageWeb;
import com.tiket.page.common.phone.settings.DefaultAppsPage;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static com.tiket.core.CoreFunctions.*;
import static com.tiket.testdata.TestData.getObjectParams;
import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;

public class OrderTest extends BaseTest {
    @Author(names = {"Prerna", "Akbar", "Mridul Gupta"})
    @Module(name = "MyOrder")
    @Tribe(name = "Order")
    @Vertical(name = "PRO")
    @Test
    public void checkHotelDetailOrder() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("checkHotelDetailOrder");
        BaseTest.paramsThreadLocal.set(param);

        HotelApi<?> hotelApi = new HotelApi<>(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);

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
        Boolean result = hotelApi.createIssuedOrderApiWithProtection(
                username, password, startDate, query, adult, night, room,
                orderFullName, orderNameTitle, guestFullName, guestTitle,"IDR");
        Assertion.assertThat("Hotel Booking via API is successful", result, is(true));

        String issuedOrderID = String.valueOf(hotelApi.getOrderId());
        String issuedRoomName = hotelApi.getHotelRoomName();

        step("Goto home");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink (if needed)");
        homePage.loginWithSpecificAccountViaDeeplink(username, password, "IDR");

        step("Open my order page");
        homePage.openMyOrderPage();

        step("Find the issued order");
        orderListPage.openActiveTicketViaDeeplink(issuedOrderID);
        Assertion.assertThat("Order ID is displayed correctly", orderDetailPage.getOrderID(), is(issuedOrderID));
        Assertion.assertThat("Main e-ticket Displayed", orderDetailPage.isMainETiketCardDisplayed(), is(true));
        Assertion.assertThat("E-ticket card Displayed", orderDetailPage.isETiketAndReceiptCardDisplayed(), is(true));
        Assertion.assertThat("Hotel Name is displayed correctly", orderDetailPage.isHotelNameDisplayed(query), is(true));
        Assertion.assertThat("order Detail ID component should be verified", orderDetailPage.verifyOrderDetailIdComponent(), is(true));
        Assertion.assertThat("Room Name is displayed correctly", orderDetailPage.isRoomNameDisplayed(issuedRoomName), is(true));
        Assertion.assertThat("Total adult is equal", orderDetailPage.getTotalAdult(),is(Integer.toString(adult)));
        Assertion.assertThat("Total night is equal", orderDetailPage.getTotalNight(issuedOrderID) ,is(Integer.toString(night)));
        Assertion.assertThat("Total room is equal", orderDetailPage.getTotalRoom().trim() ,is(Integer.toString(room).trim()));
        Assertion.assertThat("call button should be clicked", orderDetailPage.clickCallBtn(), is(true));
        Assertion.assertThat("Button Full Protection Submit New Claim should be visible", orderDetailPage.ButtonFullProtectionSubmitNewClaim(), is(true));
        Assertion.assertThat("location redirection button should be verified", orderDetailPage.verifyLocationRedirectionButton(), is(true));
        Assertion.assertThat("refund and reschedule info section should be verified", orderDetailPage.verifyRefundAndRescheduleInfo(), is(true));
        Assertion.assertThat("LPG Banner should be displayed", orderDetailPage.verifyLpgBanner(), is(true));
        Assertion.assertThat("refund button should be displayed", orderDetailPage.isRefundButtonDisplayed(), is(true));
        Assertion.assertThat("reschedule button should be displayed", orderDetailPage.isRescheduleButtonDisplayed(), is(true));
        Assertion.assertThat("edit booking button should be displayed", orderDetailPage.verifyEditGuestDetail(), is(true));
        Assertion.assertThat("Page Module should be displayed", orderDetailPage.verifyPageModule(), is(true));
    }

    @Author(names = {"Ashish", "Prerna"})
    @Module(name = "MyOrder")
    @Tribe(name = "Order")
    @Vertical(name = "PRO")
    @Test(dataProvider="testCheckOrderFromNonLogInUserToLogInDataProvider")
    public void testCheckOrderFromNonLogInUserToLogIn(Map<String, String> param) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        CarTransferSearchPage<WebDriver> carTransferSearchPage = CarTransferSearchPage.getInstance(session);
        CarTransferSearchResultPage<WebDriver> carTransferSearchResultPage = CarTransferSearchResultPage.getInstance(session);
        CarTransferBookingPage<WebDriver> carTransferBookingPage = CarTransferBookingPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        var tripPage = TripPage.getInstance(session);

        String airport = param.get("airport");
        String destination = param.get("destination");
        String email = param.get("email");
        String phoneNumber = param.get("phoneNumber");
        String password = param.get("password");
        String paymentType = param.get("paymentType");
        String currency = "IDR";

        step("User open app");
        homePage.gotoHome();

        step("User user app as guest");
        loginPage.loginAsGuest();

        step("Set currency to IDR" );
        homePage.changeCurrency("IDR","");

        step("User open car transfer vertical page");
        homePage.openVerticalPageDeeplink("Car Transfers","",currency);
        carTransferSearchPage.allowLocationAccess();
        webViewPage.switchToWebViewUrl("airport-transfer");
        carTransferSearchPage.closePopups();

        step("User choose car transfer in");
        carTransferSearchPage.chooseTransferType("transfer_in");

        step("user input origin and destination");
        carTransferSearchPage.choosePickups("transfer_in",airport, destination);

        step("user search for car");
        carTransferSearchPage.searchCar();

        if(Configs.PLATFORM == Platform.IOS){
            webViewPage.switchToNative();
            carTransferSearchPage.allowLocationAccess();
            webViewPage.switchToWebViewUrl("airport-transfer");
        }

        step("user select car and vendor");
        carTransferSearchResultPage.selectCarAndVendor();

        step("user continue to booking page");
        carTransferSearchResultPage.continueToBookingPage();

        step("user fill contact detail in booking page");
        carTransferBookingPage.fillDetailOrder(email,phoneNumber);

        step("user continue to payment list page");
        carTransferBookingPage.continueToPayment();
        webViewPage.switchToNative();
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",!orderID.isEmpty(),is(true));

        step("Open payment method");
        paymentPage.openPaymentMethod();

        step("Select e-wallet as payment method");
        paymentPage.selectPaymentMethod(paymentType);

        step("Submit payment");
        paymentPage.clickPayButton();

        step("Open order list page");
        paymentPage.seeOrderList();
        tripPage.skipCoachmark();
        tripPage.switcherPage.switchToOrder();

        step("Input credential on login page");
        orderListPage.loginForOrderList(email, password);

        step("check orderId");
        homePage.openMyOrderPage();
        Assertion.assertThat("check if order id of guest email user is present in order of login user", orderListPage.matchOrderId(orderID), is(true));
    }

    @Author(names = {"Yazid","Dendi"})
    @Module(name = "SeeEticket")
    @Tribe(name = "Order")
    @Vertical(name = "PRO")
    @Test
    public void checkOrderETicketLogin() throws ExecutionException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("checkOrderETicketLogin");
        BaseTest.paramsThreadLocal.set(map);

        HotelPage<WebDriver> hotelPage = HotelPage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        Unm<WebDriver> unm = new Unm<>(session);

        String username = map.get("username");
        String password = map.get("password");
        String startDate = generateRandomDate(map.get("rangeAddDate"));
        String hotelName = map.get("hotelName");
        String query = map.get("hotelName");
        String currency = "IDR";
        String cardNumber = map.get("cardNumber");
        String expiry = map.get("expiry");
        String cvv = map.get("cvv");
        String cardName = map.get("cardName");
        int adult = Integer.parseInt(map.get("adult"));
        int night = Integer.parseInt(map.get("night"));
        int room = Integer.parseInt(map.get("room"));

        String[] salutationList = { "Mr", "Mrs", "Ms" };
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = getRandomObject(salutationList);

        step("Get api access token");
        LoginDataUnmResponse.Data authData = unm.unmLoginApi(username, password);
        String apiAccessToken = authData.accessToken;
        String accountId = String.valueOf(authData.accountId);

        step("check order present in order");
        MapVerticalWithOrderId.TitleAndOrderId activeOrderData = orderListPage.findFirstOrderFilterByVertical(apiAccessToken,username,accountId,"HOTEL",currency,false,false,OrderStatus.ETICKET_ISSUED);
        log("Active order data : " + activeOrderData);
        String bookedOrderID;
        String issuedRoomName;

        List<String> getGuestNameList = new ArrayList<String>();
        if(activeOrderData == null || activeOrderData.orderId==null){
            step("Create new hotel issued order");
            String result = hotelApi.createIssuedOrderApi(
                    username, password, startDate, query, adult, night, room,
                    orderFullName, orderNameTitle, guestFullName, guestTitle,currency,cardNumber,cardName,expiry,cvv);
            Assertion.assertThat("Hotel Booking via API is successful", result, not(blankOrNullString()));
            bookedOrderID = String.valueOf(hotelApi.getOrderId());
            getGuestNameList = hotelApi.guestNames;
            issuedRoomName = hotelApi.getHotelRoomName();
        }else{
            bookedOrderID = activeOrderData.orderId;
            HotelInternalOrderDetail hotelInternalOrderDetail = orderDetailPage.getHotelInternalOrderData(bookedOrderID);
            getGuestNameList = hotelInternalOrderDetail.data.travelers.stream().map(d -> d.fullName).toList();
            issuedRoomName = hotelInternalOrderDetail.data.orderDetails.get(0).vertical.roomName;
            hotelName = hotelInternalOrderDetail.data.orderDetails.get(0).orderName;
        }

        step("Goto home");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink (if needed)" + currency);
        homePage.loginWithSpecificAccountViaDeeplink(username, password, currency);

        step("Find the issued order");
        orderListPage.openIssuedTicketViaDeeplink(bookedOrderID);

        step("Open e-Ticket from header in english");
        orderDetailPage.clickEticketFromHeader();
        orderDetailPage.clickETicketLanguage("English");

        step("e-Ticket file is opened from header entry point in english");
        String actualPdfContent = orderDetailPage.getPDFContentFile();
        if(Configs.PLATFORM == Platform.ANDROID){
            Assertion.assertThat("Hotel name on the PDF should be equal on the App", actualPdfContent, containsString(hotelName));
            Assertion.assertThat("Room name on the PDF should be equal on the App", actualPdfContent, containsString(issuedRoomName));
            Assertion.assertThat("Guest name on the PDF should be equal on the App", actualPdfContent, containsString(getGuestNameList.get(0)));
            Assertion.assertThat("Verify content contains room type in english", actualPdfContent, containsString("Room Type"));

        }else{
            Assertion.assertThat("Verify pdf content ", orderDetailPage.isPdfContentOpen(), is(true));
            Assertion.assertThat("Hotel name on the PDF should be equal on the App", actualPdfContent, containsString(hotelName));
            Assertion.assertThat("Room name on the PDF should be equal on the App", actualPdfContent, containsString(issuedRoomName));
            Assertion.assertThat("Verify content contains room type in english", actualPdfContent, containsString("Room Type"));
        }

        step("Open e-Ticket from header in bahasa indonesia");
        orderListPage.openIssuedTicketViaDeeplink(bookedOrderID);
        orderDetailPage.clickEticketFromHeader();
        orderDetailPage.clickETicketLanguage("Bahasa Indonesia");

        step("e-Ticket file is opened from header entry point in bahasa indonesia");
        actualPdfContent = orderDetailPage.getPDFContentFile();
        if(Configs.PLATFORM == Platform.ANDROID){
            Assertion.assertThat("Hotel name on the PDF should be equal on the App", actualPdfContent, containsString(hotelName));
            Assertion.assertThat("Room name on the PDF should be equal on the App", actualPdfContent, containsString(issuedRoomName));
            Assertion.assertThat("Guest name on the PDF should be equal on the App", actualPdfContent, containsString(getGuestNameList.get(0)));
            Assertion.assertThat("Verify content contains room type in bahasa", actualPdfContent, containsString("Tipe Kamar"));
        }else{
            actualPdfContent = orderDetailPage.getPDFContentFile();
            Assertion.assertThat("Verify pdf content ", orderDetailPage.isPdfContentOpen(), is(true));
            Assertion.assertThat("Hotel name on the PDF should be equal on the App", actualPdfContent, containsString(hotelName));
            Assertion.assertThat("Room name on the PDF should be equal on the App", actualPdfContent, containsString(issuedRoomName));
            Assertion.assertThat("Guest name on the PDF should be equal on the App", actualPdfContent, containsString(getGuestNameList.get(0)));
            Assertion.assertThat("Verify content contains room type in bahasa", actualPdfContent, containsString("Tipe Kamar"));
        }

        step("open e-ticket from manage order in english");
        orderListPage.openIssuedTicketViaDeeplink(bookedOrderID);
        orderDetailPage.clickSeeEticketFromManageOrder();
        orderDetailPage.clickETicketLanguage("English");

        step("e-Ticket file is opened from manage order entry point in english");
        if(Configs.PLATFORM == Platform.ANDROID){
            actualPdfContent = orderDetailPage.getPDFContentFile();
            Assertion.assertThat("Hotel name on the PDF should be equal on the App", actualPdfContent, containsString(hotelName));
            Assertion.assertThat("Room name on the PDF should be equal on the App", actualPdfContent, containsString(issuedRoomName));
            Assertion.assertThat("Guest name on the PDF should be equal on the App", actualPdfContent, containsString(getGuestNameList.get(0)));
            Assertion.assertThat("Verify content contains room type in english", actualPdfContent, containsString("Room Type"));
        }else{
            actualPdfContent = orderDetailPage.getPDFContentFile();
            Assertion.assertThat("Verify pdf content ", orderDetailPage.isPdfContentOpen(), is(true));
            Assertion.assertThat("Hotel name on the PDF should be equal on the App", actualPdfContent, containsString(hotelName));
            Assertion.assertThat("Room name on the PDF should be equal on the App", actualPdfContent, containsString(issuedRoomName));
            Assertion.assertThat("Guest name on the PDF should be equal on the App", actualPdfContent, containsString(getGuestNameList.get(0)));
            Assertion.assertThat("Verify content contains room type in English", actualPdfContent, containsString("Room Type"));
        }

        step("open e-ticket from manage order in bahasa indonesia");
        orderListPage.openIssuedTicketViaDeeplink(bookedOrderID);
        orderDetailPage.clickSeeEticketFromManageOrder();
        orderDetailPage.clickETicketLanguage("Bahasa Indonesia");

        step("e-Ticket file is opened from manage order entry point in bahasa indonesia");
        if(Configs.PLATFORM == Platform.ANDROID){
            actualPdfContent = orderDetailPage.getPDFContentFile();
            Assertion.assertThat("Hotel name on the PDF should be equal on the App", actualPdfContent, containsString(hotelName));
            Assertion.assertThat("Room name on the PDF should be equal on the App", actualPdfContent, containsString(issuedRoomName));
            Assertion.assertThat("Guest name on the PDF should be equal on the App", actualPdfContent, containsString(getGuestNameList.get(0)));
            Assertion.assertThat("Verify content contains room type in bahasa", actualPdfContent, containsString("Tipe Kamar"));
        }else{
            actualPdfContent = orderDetailPage.getPDFContentFile();
            Assertion.assertThat("Verify pdf content ", orderDetailPage.isPdfContentOpen(), is(true));
            Assertion.assertThat("Hotel name on the PDF should be equal on the App", actualPdfContent, containsString(hotelName));
            Assertion.assertThat("Room name on the PDF should be equal on the App", actualPdfContent, containsString(issuedRoomName));
            Assertion.assertThat("Guest name on the PDF should be equal on the App", actualPdfContent, containsString(getGuestNameList.get(0)));
            Assertion.assertThat("Verify content contains room type in bahasa", actualPdfContent, containsString("Tipe Kamar"));
        }

    }

    @Author(names = {"Dendi"})
    @Module(name = "SeeEticket")
    @Tribe(name = "Order")
    @Vertical(name = "PRO")
    @Test
    public void checkOrderETicketGuest() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("checkOrderETicketGuest");
        BaseTest.paramsThreadLocal.set(map);

        HotelPage<WebDriver> hotelPage = HotelPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);

        String hotelNameSearch = map.get("hotelName");
        int totalRoom = Integer.parseInt(map.get("totalRoom"));
        int totalGuest = Integer.parseInt(map.get("totalGuest"));

        String cardNumber = map.get("cardNumber");
        String expiry = map.get("expiry");
        String cvv = map.get("cvv");
        String cardName = map.get("cardName");
        String payment_method = "Credit Card";
        String currency = "IDR";

        step("Goto home");
        homePage.gotoHome();

        step("Login as guest");
        loginPage.loginAsGuest();

        step("Open hotel landing page");
        homePage.openVerticalPageDeeplink("Hotel","",currency);

        step("search stay location: " + hotelNameSearch);
        landingPage.searchStayLocation(hotelNameSearch);

        step("set total room: " + totalRoom + " and total guest: " + totalGuest);
        landingPage.clickRoomAndGuestButton();
        landingPage.setTotalRoom(totalRoom);
        landingPage.setTotalGuest(totalGuest);
        landingPage.clickDone();

        step("submit search");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        step("Go to the room list");
        String hotelName = detailPage.getHotelNameDetail();
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();

        step("Select room from the room list");
        String roomName = roomListPage.getTheFirstHotelRoomName();
        roomListPage.selectMultiRoom(totalRoom, totalGuest);

        step("Fill the data on the booking form and submit it");
        bookingFormPage.fillContactOrderDetail();
        bookingFormPage.fillGuestDetail(false);
        List<String> getGuestNameList = bookingFormPage.getGuestNameList();
        bookingFormPage.continueToPaymentPage();
        bookingFormPage.PriceChangesPopUp();

        step("Get orderID");
        String orderID = paymentPage.checkOrderID();

        step("user choose pay with cc");
        paymentPage.openPaymentMethod();
        paymentPage.selectPaymentMethod(payment_method);
        paymentPage.fillCCDetailRealIssued(cardNumber,expiry,cvv,cardName);
        paymentPage.clickPayButton();
        paymentPage.checkCcOtp();

        step("Go to order list");
        paymentPage.seeOrderList();

        step("Find the issued order");
        orderListPage.openIssuedTicketViaDeeplink(orderID);

        step("Open e-Ticket from header in english");
        orderDetailPage.clickEticketFromHeader();
        orderDetailPage.clickETicketLanguage("English");

        step("e-Ticket file is opened from header entry point in english");
        String actualPdfContent = orderDetailPage.getPDFContentFile();
        if(Configs.PLATFORM == Platform.ANDROID){
            Assertion.assertThat("Hotel name on the PDF should be equal on the App", actualPdfContent, containsString(hotelName));
            Assertion.assertThat("Guest name on the PDF should be equal on the App", actualPdfContent, containsString(getGuestNameList.get(0)));
            Assertion.assertThat("Verify content contains room type in english", actualPdfContent, containsString("Room Type"));

        }else{
            Assertion.assertThat("Verify pdf content ", orderDetailPage.isPdfContentOpen(), is(true));
            Assertion.assertThat("Hotel name on the PDF should be equal on the App", actualPdfContent, containsString(hotelName));
            Assertion.assertThat("Verify content contains room type in english", actualPdfContent, containsString("Room Type"));
        }

        step("Open e-Ticket from header in bahasa indonesia");
        orderDetailPage.clickBackFromPdf();
        orderDetailPage.clickEticketFromHeader();
        orderDetailPage.clickETicketLanguage("Bahasa Indonesia");

        step("e-Ticket file is opened from header entry point in bahasa indonesia");
        actualPdfContent = orderDetailPage.getPDFContentFile();
        if(Configs.PLATFORM == Platform.ANDROID){
            Assertion.assertThat("Hotel name on the PDF should be equal on the App", actualPdfContent, containsString(hotelName));
            Assertion.assertThat("Guest name on the PDF should be equal on the App", actualPdfContent, containsString(getGuestNameList.get(0)));
            Assertion.assertThat("Verify content contains room type in bahasa", actualPdfContent, containsString("Tipe Kamar"));
        }else{
            actualPdfContent = orderDetailPage.getPDFContentFile();
            Assertion.assertThat("Verify pdf content ", orderDetailPage.isPdfContentOpen(), is(true));
            Assertion.assertThat("Hotel name on the PDF should be equal on the App", actualPdfContent, containsString(hotelName));
            Assertion.assertThat("Guest name on the PDF should be equal on the App", actualPdfContent, containsString(getGuestNameList.get(0)));
            Assertion.assertThat("Verify content contains room type in bahasa", actualPdfContent, containsString("Tipe Kamar"));
        }

        step("open e-ticket from manage order in english");
        orderDetailPage.clickBackFromPdf();
        orderDetailPage.clickSeeEticketFromManageOrder();
        orderDetailPage.clickETicketLanguage("English");

        step("e-Ticket file is opened from manage order entry point in english");
        if(Configs.PLATFORM == Platform.ANDROID){
            actualPdfContent = orderDetailPage.getPDFContentFile();
            Assertion.assertThat("Hotel name on the PDF should be equal on the App", actualPdfContent, containsString(hotelName));
            Assertion.assertThat("Guest name on the PDF should be equal on the App", actualPdfContent, containsString(getGuestNameList.get(0)));
            Assertion.assertThat("Verify content contains room type in english", actualPdfContent, containsString("Room Type"));
        }else{
            actualPdfContent = orderDetailPage.getPDFContentFile();
            Assertion.assertThat("Verify pdf content ", orderDetailPage.isPdfContentOpen(), is(true));
            Assertion.assertThat("Hotel name on the PDF should be equal on the App", actualPdfContent, containsString(hotelName));
            Assertion.assertThat("Guest name on the PDF should be equal on the App", actualPdfContent, containsString(getGuestNameList.get(0)));
            Assertion.assertThat("Verify content contains room type in English", actualPdfContent, containsString("Room Type"));
        }

        step("open e-ticket from manage order in bahasa indonesia");
        orderDetailPage.clickBackFromPdf();
        orderDetailPage.clickSeeEticketFromManageOrder();
        orderDetailPage.clickETicketLanguage("Bahasa Indonesia");


        step("e-Ticket file is opened from manage order entry point in bahasa indonesia");
        if(Configs.PLATFORM == Platform.ANDROID){
            actualPdfContent = orderDetailPage.getPDFContentFile();
            Assertion.assertThat("Hotel name on the PDF should be equal on the App", actualPdfContent, containsString(hotelName));
            Assertion.assertThat("Guest name on the PDF should be equal on the App", actualPdfContent, containsString(getGuestNameList.get(0)));
            Assertion.assertThat("Verify content contains room type in bahasa", actualPdfContent, containsString("Tipe Kamar"));
        }else{
            actualPdfContent = orderDetailPage.getPDFContentFile();
            Assertion.assertThat("Verify pdf content ", orderDetailPage.isPdfContentOpen(), is(true));
            Assertion.assertThat("Hotel name on the PDF should be equal on the App", actualPdfContent, containsString(hotelName));
            Assertion.assertThat("Guest name on the PDF should be equal on the App", actualPdfContent, containsString(getGuestNameList.get(0)));
            Assertion.assertThat("Verify content contains room type in bahasa", actualPdfContent, containsString("Tipe Kamar"));
        }


    }

    @Author(names = {"Zia"})
    @Module(name = "MyOrder")
    @Tribe(name = "Order")
    @Vertical(name = "PRO")
    @Test(dataProvider="testCheckOrderHistoryLoginDataProvider")
    public void testCheckOrderHistoryLogin(Map<String, String> param) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderHistoryPage<WebDriver> orderHistoryPage = OrderHistoryPage.getInstance(session);
        var tripPage = TripPage.getInstance(session);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String vertical1 = param.get("vertical1");
        String vertical2 = param.get("vertical2");

        step("User open app");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink (if needed)");
        homePage.loginWithSpecificAccountViaDeeplink(username, password, "IDR");

        step("Open my order page");
        String lang = homePage.getLanguageApp();
        homePage.openMyOrderPage();
        tripPage.skipCoachmark();
        tripPage.switcherPage.switchToOrder();

        step("open order history");
        orderListPage.openOrderHistory();

        step("Apply filter");
        orderHistoryPage.clickFilter();
        orderHistoryPage.selectFilterVertical(vertical1,lang);
        orderHistoryPage.selectFilterVertical(vertical2,lang);
        orderHistoryPage.setPeriod();
        orderHistoryPage.clickApplyFilter();

        step("check past order are visible");
        Assertion.assertThat("is past order list is visible", orderHistoryPage.verifyPastOrderList(), is(true));
        Assertion.assertThat("is Rescheduled orders shown", orderHistoryPage.isRescheduleOrderShown(), is(true));
        Assertion.assertThat("is finished orders shown", orderHistoryPage.isFinishedOrderShown(), is(true));
        Assertion.assertThat("is Refund Completed orders shown", orderHistoryPage.isRefundCompletedOrderShown(), is(true));
        Assertion.assertThat("is cancelled orders shown", orderHistoryPage.isCancelledOrderShown(), is(true));

        step("Apply Sort");
        orderHistoryPage.applySort();
        Assertion.assertThat("Both filter and sort applied", orderHistoryPage.bothSortAndFilterApplied() , is(true));

        step("Verify refund page");
        Assertion.assertThat("is past refund visible", orderHistoryPage.verifyPastRefundList("login"), is(true));
    }

    @Author(names = {"Ashish", "Zia"})
    @Module(name = "MyOrder")
    @Tribe(name = "Order")
    @Vertical(name = "PRO")
    @Test(dataProvider="testAirportTransferCheckDetailOrderLoginDataProvider")
    public void testAirportTransferCheckDetailOrderLogin(Map<String, String> param) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        Unm<WebDriver> unm = new Unm<>(session);
        CarTransferApi<?> carTransferApi = new CarTransferApi<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String expiry = param.get("expiry");
        String adminName = param.get("adminName");
        String adminPhone = param.get("adminPhone");

        String origin = param.get("origin");
        String destination = param.get("destination");
        String type = param.get("type");
        String pickupTime = param.get("pickupTime");
        pickupTime = pickupTime.replace('/',':');
        int pax = Integer.parseInt(param.get("pax"));
        String rangeAddDate = param.get("rangeAddDate");

        step("Get api access token");
        LoginDataUnmResponse.Data authData = unm.unmLoginApi(username, password);
        String apiAccessToken = authData.accessToken;
        String accountId = String.valueOf(authData.accountId);

        String orderId = "";
        double totalPrice;
        String pickupPlace = "";
        String dropPlace = "";
        String carType = "";
        String salutation = "";
        String guestName = "";
        String phoneNumber = "";

        step("Get active order");
        MapVerticalWithOrderId.TitleAndOrderId activeOrderData = orderListPage.findFirstOrderFilterByVertical(apiAccessToken,username,accountId,"AIRPORT_TRANSFER","",false,false,OrderStatus.ETICKET_ISSUED);
        if(activeOrderData == null || activeOrderData.orderId==null){
            step("Create new airport transfer issued order");
            orderId = carTransferApi.createIssuedOrderApiPayUsingSakuku(username, password, adminName, adminPhone, origin, destination, type, rangeAddDate, pickupTime, pax);
            Assertion.assertThat("Car transfer booking via API is successful", orderId, not(blankOrNullString()));
            totalPrice = carTransferApi.getGrandTotal();
            pickupPlace = carTransferApi.getPickupPlace();
            dropPlace = carTransferApi.getDropPlace();
            carType = carTransferApi.getCarType();
            salutation = carTransferApi.getSalutation();
            guestName = carTransferApi.getGuestName();
            phoneNumber = carTransferApi.getPhoneNumber();
        }else{
            MapVerticalWithOrderId.TitleAndOrderIdAirportTransfer atOrder = activeOrderData.getDetailedVerticalData( MapVerticalWithOrderId.TitleAndOrderIdAirportTransfer.class);
            orderId = atOrder.orderId;
            totalPrice = Double.parseDouble(atOrder.totalPrice);
            pickupPlace = atOrder.originName;
            dropPlace = atOrder.destinationName;
            carType = atOrder.fleetName;
            salutation = atOrder.salutation;
            guestName = atOrder.guestName;
            phoneNumber = atOrder.phoneNumber;
        }

        log("Total Price: " + totalPrice);

        step("User open app");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink (if needed)");
        homePage.loginWithSpecificAccountViaDeeplink(username, password, "IDR");

        step("Get langguage");
        String lang = homePage.getLanguageApp();

        step("Open my order page");
        homePage.openMyOrderPage();

        step("open order detail");
        orderListPage.openActiveTicketViaDeeplink(orderId);
        orderListPage.removePopUp();
        orderDetailPage.hidePromoBannerIfPresent();

        step("Verify header is the containing pick up location");
        Assertion.assertThat("Verify header title",  orderDetailPage.verifyOrderDetailHeader(pickupPlace), is(true));

        step("Verify car detail section");
        Assertion.assertThat("Verify car detail section",   orderDetailPage.verifyAtCarDetailSection(carType), is(true));

        step("Assert airport transfer order detail");
        Assertion.assertThat("Is airport transfer detail correct",orderDetailPage.checkAirportTransferDetail(pickupPlace,dropPlace), is(true));

        step("Verify airport transfer trip summary detail");
        Assertion.assertThat("Verify at trip summary detail", orderDetailPage.veriyAtSummaryDetail(pickupPlace,dropPlace), is(true));

        step("Verify facilities detail present");
        Assertion.assertThat("Verify facilities detail present", orderDetailPage.verifyAtFacilitiesDetailPresent(), is(true));

        step("Verify contact detail");
        Assertion.assertThat("Verify contact detail in order detail page",  orderDetailPage.verifyAtContactDetail(salutation,guestName,phoneNumber,lang), is(true));

        step("Verify AT total price");
        Assertion.assertThat("Verify AT Total price", CoreFunctions.convertPriceSafely(orderDetailPage.getAirportTransferTotalPrice())  , equalTo(totalPrice));
    }

    @Author(names = {"Okta"})
    @Module(name = "MyOrder")
    @Tribe(name = "Order")
    @Vertical(name = "PRO")
    @Test(dataProvider="testCheckCarRentalOrderDetailLoginDataProvider")
    public void testCheckCarRentalOrderDetailLogin(Map<String, String> param) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        CarRentalPage<WebDriver> carRentalPage = CarRentalPage.getInstance(session);
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

        LoginDataUnmResponse.Data authData = unm.unmLoginApi(email, password);
        String apiAccessToken = authData.accessToken;
        String accountId = String.valueOf(authData.accountId);
        MapVerticalWithOrderId.TitleAndOrderId activeOrderData = orderListPage.findFirstOrderFilterByVertical(apiAccessToken,email,accountId,"CAR","",false,false,OrderStatus.ETICKET_ISSUED);

        if(activeOrderData == null || activeOrderData.orderId==null){
            step("Create car rental issued order for 1 passenger and 1 day via API");
            Boolean orderResult = carRentalPage.createIssuedOrderApi(
                    email, phoneNumber, password, driverOption, location,
                    startDate, pickupTime, type, fullName, nameTitle,adminName,adminPhone);
            Assertion.assertThat("Car Booking via API is successful", orderResult, is(true));
            activeOrderData = orderListPage.findFirstOrderFilterByVertical(apiAccessToken,email,accountId,"CAR","",false,false,OrderStatus.ETICKET_ISSUED);
        }

        String orderDetail = activeOrderData.internalOrderDetailJSONObject;
        String issuedOrderID = activeOrderData.orderId;
        String orderLang = CoreFunctions.getDataFromJsonString(orderDetail, "data.userLang");
        String issuedCarName = CoreFunctions.getDataFromJsonString(orderDetail, "data.orderDetails.vertical.fleetName");
        String issuedCarRentalLocation = CoreFunctions.getDataFromJsonString(orderDetail, "data.orderDetails.vertical.pickupLocation");
        String carRentalDuration = CoreFunctions.getDataFromJsonString(orderDetail, "data.orderDetails.vertical.duration");
        pickupTime = CoreFunctions.getDataFromJsonString(orderDetail, "data.orderDetails.vertical.pickupTime");
        String dropTime = CoreFunctions.getDataFromJsonString(orderDetail, "data.orderDetails.vertical.dropOffTime");
        if(Boolean.parseBoolean(CoreFunctions.getDataFromJsonString(orderDetail, "data.orderDetails.vertical.isWithDriver"))){
            driverOption ="with_driver" ;
        }

        step("Open app");
        homePage.gotoHome();

        step("Login as " + email + " via deeplink (if needed)");
        homePage.loginWithSpecificAccountViaDeeplink(email, password, "IDR");

        step("Change App lang");
        String appLang = homePage.getLanguageApp();

        if(!appLang.equalsIgnoreCase(orderLang) && !appLang.equalsIgnoreCase("IDN")) {
            step("Change Language to 'IDN'");
            homePage.changeLanguageUsingDeeplink("IDN");
            appLang = "id";
        }

        step("Find issued car rental order");
        orderListPage.openIssuedTicketViaDeeplink(issuedOrderID);

        step("Verify user able to share eticket");
        orderDetailPage.closeInfoPopup();
        orderDetailPage.clickShareEticket();
        Assertion.assertThat("Verify user able to share eticket", orderDetailPage.verifyShareEticketBtmSheet("car", appLang, issuedOrderID), is(true));

        step("Open e-ticket from header");
        orderDetailPage.clickEticketFromHeader();

        step("e-ticket open from header entry point");
        Assertion.assertThat("Verify pdf content ", orderDetailPage.isPdfContentOpen(), is(true));
        orderDetailPage.clickBackFromPdf();

        step("Verify car rental product detail");
        Assertion.assertThat("Verify car rental product detail",orderDetailPage.verifyCarRentalProductDetail(issuedCarName, driverOption, appLang),is(true));

        step("Verify rental detail");
        Assertion.assertThat("Verify rental detail", orderDetailPage.verifyRentalDetail(issuedCarRentalLocation, issuedCarRentalLocation, pickupTime, dropTime, carRentalDuration), is(true));

        step("Verify driver detail");
        Assertion.assertThat("Verify driver detail", orderDetailPage.verifyDriverDetailExist(), is(true));

        step("Verify rr info present");
        Assertion.assertThat("Verify rr info present", orderDetailPage.verifyCarRrInfoSection(), is(true));

        step("Verify manage order exist");
        Assertion.assertThat("Verify manage order exist, eticket, receipt, refund button", orderDetailPage.verifyCarManageOrder(), is(true));

        step("Verify price detail open");
        orderDetailPage.openPriceDetail();
        Assertion.assertThat("Verify price detail open", orderDetailPage.verifyCrPriceDetailComponentCorrect(), is(true));

        step("Verify share receipt");
        orderDetailPage.shareReceipt();
        boolean isFileNameCorrect = orderDetailPage.isShareFileNameCorrect("Car", issuedOrderID, appLang);
        Assertion.assertThat("Verify file title has same name", isFileNameCorrect, is(true));
        orderDetailPage.closeShareReceiptBtmSheet();

        step("Verify help center section present");
        Assertion.assertThat("Verify help center section present", orderDetailPage.verifyHelpCenterSectionPresent(), is(true));
    }

    @Author(names = {"Zia","Akbar"})
    @Module(name = "MyOrder")
    @Tribe(name = "Order")
    @Vertical(name = "PRO")
    @Test(dataProvider="testTrainCheckDetailOrderLoginDataProvider")
    public void testTrainCheckDetailOrderLogin(Map<String, String> param) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);
        TrainPage<WebDriver>  trainPage= TrainPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);

        String loginType = param.get("loginType");
        String email = param.get("email");
        String password = param.get("password");
        String name = param.get("name");
        String phone = param.get("phone");
        String id = String.valueOf(CoreFunctions.generateRandomInt(3));
        id = "327202200202"+id+"1";

        String originCity = param.get("originCity");
        String originType = param.get("originType");
        String destinationCity = param.get("destinationCity");
        String destinationType = param.get("destinationType");
        String tripType = param.get("tripType");
        int adult = Integer.parseInt(param.get("adult"));
        int infant = Integer.parseInt(param.get("infant"));
        String date = generateRandomDate(param.get("rangeAddDate"));
        date = date.replace("-","");

        step("Create new train issued order");
        Boolean result = trainPage.createIssuedOrderApi(email, password, name, phone, id, originCity, destinationCity, date, tripType, adult, infant);
        Assertion.assertThat("Train Booking via API is successful", result, is(true));

        String orderId = trainPage.issuedOrderData("orderId");
        String totalPrice = trainPage.issuedOrderData("grandTotal");
        String originStation = trainPage.issuedOrderData("originStation");
        String destinationStation = trainPage.issuedOrderData("destinationStation");
        String trainName = trainPage.issuedOrderData("trainName").toUpperCase();
        String orderTitle = originCity + " - " + destinationCity;

        step("User open app");
        homePage.gotoHome();

        step("Login as " + email + " via deeplink (if needed)");
        homePage.loginWithSpecificAccountViaDeeplink(email, password, "IDR");

        step("Open my order page");
        homePage.openMyOrderPage();

        step("Find the issued order");
        orderListPage.openActiveTicketViaDeeplink(orderId);

        step("Assert train order detail");
        Assertion.assertThat("Is train detail correct",orderDetailPage.isTrainDetailCorrect(originStation, destinationStation, totalPrice), is(true));
        Assertion.assertThat("Verify arrival date, departure date, duration", orderDetailPage.isTrainDepartureArrivalDurationExists(), is(true));
        Assertion.assertThat("Verify Train name and Class", orderDetailPage.isTrainNameAndClassExists(), is(true));
        Assertion.assertThat("Passenger and facilities section exists", orderDetailPage.passengerAndFacilitiesExists(), is(true));
        Assertion.assertThat("refund and reschedule info section should be verified", orderDetailPage.verifyRefundAndRescheduleInfo(), is(true));
    }

    @Author(names = {"Akbar", "Prerna","Zia"})
    @Module(name = "MyOrder")
    @Tribe(name = "Order")
    @Vertical(name = "PRO")
    @Test
    public void checkTTDDetailOrderLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("checkTTDDetailOrderLogin");
        BaseTest.paramsThreadLocal.set(map);

        TTDPage<WebDriver> ttdPage = TTDPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);

        String username = map.get("username");
        String password = map.get("password");
        int dateRange = Integer.parseInt(map.get("dateRange"));
        int monthRange = Integer.parseInt(map.get("monthRange"));
        String productUrl = map.get("productUrl");
        String productPackages = map.get("productPackages");

        String[] salutationList = { "Mr", "Mrs", "Ms" };
        String guestFullName = randomName() + " autguest";
        String guestTitle = getRandomObject(salutationList);

        step("Create a ttd order with waiting payment status");
        String issuedOrderID = ttdPage.createWaitingPaymentOrderApi(
                username,password,
                productUrl,
                productPackages,
                guestFullName,guestTitle,
                guestFullName,username,guestTitle,dateRange,monthRange, false);
        String totalPriceTodo = String.valueOf(ttdPage.getGrandTotal());
        Assertion.assertThat("Success to create waiting payment order of price " + totalPriceTodo + " ", issuedOrderID, is(not(emptyOrNullString())));
        String issuedProductName = ttdPage.issuedOrderData("productName");
        String issuedPackageName = ttdPage.issuedOrderData("packageName");

        step("Goto home");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink (if needed)");
        homePage.loginWithSpecificAccountViaDeeplink(username, password, "IDR");

        step("Change Language to 'IDN'");
        homePage.changeLanguageUsingDeeplink("IDN");

        step("Open my order page");
        homePage.openMyOrderPage();

        step("Find the waiting payment order");
        orderListPage.openOrderWaitingPayment(issuedOrderID);

        step("verify order id waiting payment page");
        String displayedOrderID = orderDetailPage.getOrderID();
        Assertion.assertThat("Order ID is displayed correctly on waiting payment page", displayedOrderID, is(issuedOrderID));

        step("Verify waiting payment component showing in waiting payment page");
        Assertion.assertThat("Verify waiting payment component showing", orderDetailPage.verifyCompletePaymentExpiryTime(), is(true));

        step("Verify important info waiting payment page");
        Assertion.assertThat("Important info section showing on waiting payment page", orderDetailPage.getImportantInfo(), is(notNullValue()));

        step("Verify detail information waiting payment page");
        String ProductName = orderDetailPage.getTTDProductTitle();
        Assertion.assertThat("Product Name is displayed correctly on waiting payment page", ProductName, is(issuedProductName));

        step("Verify detail reservation waiting payment page");
        String displayedPackageName = orderDetailPage.getTTDPackageName();
        Assertion.assertThat("Package Name is equal on waiting payment page", displayedPackageName,is(issuedPackageName));

        orderDetailPage.openTTDGuestDetail();
        String displayedGuestFullName = orderDetailPage.getTTDGuestFullName();
        Assertion.assertThat("Guest full name is equal on waiting payment page", displayedGuestFullName,is(guestFullName));
        orderDetailPage.closeTTDGuestDetail();

        step("Verify insurance section issued order page");
        Assertion.assertThat("Insurance section is displayed issued order page", orderDetailPage.getInsuranceTTD(), is(notNullValue()));

        step("Check redirection to complete payment");
        orderDetailPage.continueToPayment();
        paymentPage.checkOrderID();
        Assertion.assertThat("Redirected to payment page", paymentPage.isInPaymentPage(), is(true));

        step("Open payment method and pay");
        paymentPage.openPaymentMethod();
        paymentPage.selectPaymentMethod("Sakuku");
        paymentPage.clickPayButton();
        Assertion.assertThat("Payment Successful and Thank You page is shown", paymentPage.isThankYouPageShown(), is(true));
        orderListPage.openIssuedTicketViaDeeplink(issuedOrderID);
        orderDetailPage.hidePromoBannerIfPresent();

        step("verify order id issued order page");
        displayedOrderID = orderDetailPage.getOrderID();
        Assertion.assertThat("Order ID is displayed correctly issued order page", displayedOrderID, is(issuedOrderID));

        step("Open e-Ticket from header");
        orderDetailPage.clickEticketFromHeader();
        Assertion.assertThat("Verify pdf content open", orderDetailPage.isPdfContentOpen(), is(true));
        orderDetailPage.clickBackFromPdf();

        step("Verify important info issued order page");
        Assertion.assertThat("Important info section showing issued order page", orderDetailPage.getImportantInfo(), is(notNullValue()));

        step("Verify detail information issued order page");
        ProductName = orderDetailPage.getTTDProductTitle();
        Assertion.assertThat("Product Name is displayed correctly issued order page", ProductName, is(issuedProductName));

        step("Verify detail reservation issued order page");
        displayedPackageName = orderDetailPage.getTTDPackageName();
        Assertion.assertThat("Package Name is equal issued order page", displayedPackageName,is(issuedPackageName));

        orderDetailPage.openTTDGuestDetail();
        displayedGuestFullName = orderDetailPage.getTTDGuestFullName();
        Assertion.assertThat("Guest full name is equal issued order page", displayedGuestFullName,is(guestTitle.toUpperCase()+". "+guestFullName));
        orderDetailPage.closeTTDGuestDetail();

        step("Verify insurance section issued order page");
        Assertion.assertThat("Insurance section is displayed issued order page", orderDetailPage.getInsuranceTTD(), is(notNullValue()));

        step("Click detail order ");
        orderDetailPage.clickDetailTTD();
        webViewPage.switchToWebViewUrl("to-do");
        Assertion.assertThat("Redirected to correct page", ttdProductDetailPageWeb.getProductName(), is(issuedProductName));
    }

    @Author(names = {"Alden"})
    @Module(name = "MyOrder")
    @Tribe(name = "Order")
    @Vertical(name = "PRO")
    @Test
    public void checkTTDDetailBundlingOrderLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("checkTTDDetailBundlingOrderLogin");
        BaseTest.paramsThreadLocal.set(map);

        TTDApi<?> ttdApi = new TTDApi<>(session);
        TTDProductDetailPage<WebDriver> ttdProductDetailPage = TTDProductDetailPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        var tripViewPage = TripPage.getInstance(session);

        String username = map.get("username");
        String password = map.get("password");
        String productUrl = map.get("productUrl");

        String[] salutationList = { "Mr", "Mrs", "Ms" };
        String guestFullName = randomName() + " autguest";
        String guestTitle = getRandomObject(salutationList);

        step("Create a ttd order");
        String issuedOrderID = ttdApi.createIssuedOrderSakukuApi(username, password, "IDR", productUrl, true, guestFullName, guestTitle, guestFullName, username, guestTitle);

        Assertion.assertThat("Success to create paid order", issuedOrderID, is(not(emptyOrNullString())));
        String issuedProductName = ttdApi.getProductName();
        String issuedPackageName = ttdApi.getPackageName();

        step("Goto home");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink (if needed)");
        homePage.loginWithSpecificAccountViaDeeplink(username, password, "IDR");

        step("Open my order page");
        homePage.openMyOrderPage();
        tripViewPage.skipCoachmark();
        tripViewPage.switcherPage.switchToOrder();

        step("Find the issued order");
        orderListPage.openIssuedTicketViaDeeplink(issuedOrderID);

        step("verify order id issued order page");
        String displayedOrderID = orderDetailPage.getOrderID();
        Assertion.assertThat("Order ID is displayed correctly issued order page", displayedOrderID, is(issuedOrderID));

        step("Open e-Ticket from header");
        orderDetailPage.clickEticketFromHeader();
        Assertion.assertThat("Verify pdf content open", orderDetailPage.isPdfContentOpen(), is(true));
        orderDetailPage.clickBackFromPdf();

        step("Verify detail information issued order page");
        String ProductName = orderDetailPage.getTTDProductTitle();
        Assertion.assertThat("Product Name is displayed correctly issued order page", ProductName, is(issuedProductName));

        step("Verify detail reservation issued order page");
        String displayedPackageName = orderDetailPage.getTTDPackageName();
        Assertion.assertThat("Package Name is equal issued order page", displayedPackageName,is(notNullValue()));

        orderDetailPage.openTTDGuestDetail();
        String displayedGuestFullName = orderDetailPage.getTTDGuestFullName();
        Assertion.assertThat("Guest full name is equal issued order page", displayedGuestFullName,is(guestFullName));
        orderDetailPage.closeTTDGuestDetail();

        step("Verify manage order section");
        Assertion.assertThat("Manage order section is correct", orderDetailPage.verifyManageOrderSection(), is(true));
    }

    @Author(names = {"Naufal", "Prerna"})
    @Vertical(name = "PRO")
    @Tribe(name = "Order")
    @Module(name = "MyOrder")
    @Test
    public void checkHomesDetailOrderLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("checkHomesDetailOrderLogin");
        BaseTest.paramsThreadLocal.set(map);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        HomesPage<WebDriver> homesPage = HomesPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        var tripPage = TripPage.getInstance(session);
        Unm<WebDriver> unm = new Unm<>(session);

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
        String guestTitle = getRandomObject(salutationList);

        step("Goto home");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink (if needed)");
        homePage.loginWithSpecificAccountViaDeeplink(username, password, "IDR");
        String lang = homePage.getLanguageApp();

        step("Get api access token");
        String apiAccessToken = unm.getAccessToken(username, password,"B2C");

        String issuedOrderID = "";
        try {
            step("Check if existing order is present");
            MapVerticalWithOrderId activeOrderData = new OrderApi<>(session).getAvailaibleOrder(apiAccessToken);
            MapVerticalWithOrderId.TitleAndOrderId titleAndOrderId = activeOrderData.homes.stream().filter(home -> OrderStatus.ETICKET_ISSUED.getName().equals(home.orderStatus))
                    .findFirst()
                    .orElseThrow();
            issuedOrderID = titleAndOrderId.orderId;
            query = titleAndOrderId.title;
            step("Found existing homes issued order with order id: " + issuedOrderID + " and title: " + titleAndOrderId.title);
        } catch (Exception e) {
            log("Failed to find existing homes issued order: " + e.getMessage());
            step("Create new homes issued order");
            homesPage.createIssuedOrderApi(
                    username, password, startDate, query, adult, night, room,
                    orderFullName, orderNameTitle, guestFullName, guestTitle,lang,"IDR");
            issuedOrderID = homesPage.issuedOrderData("orderId");
        }

        Assertion.assertThat("Got homes issued order", issuedOrderID, is(notNullValue()));

        step("Open my order page");
        homePage.openMyOrderPage();
        tripPage.skipCoachmark();
        tripPage.switcherPage.switchToOrder();

        step("Find the issued order");
        orderListPage.openIssuedTicketViaDeeplink(issuedOrderID);

        step("get reservation detail");
        String orderIdResult = orderDetailPage.getOrderID();

        String totalAdultResult = orderDetailPage.getTotalAdult();
        String totalNightResult = orderDetailPage.getTotalNight(issuedOrderID);
        String totalRoomResult = orderDetailPage.getTotalRoom();
        totalRoomResult = totalRoomResult.split("x")[0].trim();
        String guestFullNameResult = orderDetailPage.getGuestFullName();

        step("Assertion");
        Assertion.assertThat("Order ID is equal", issuedOrderID.equals(orderIdResult), is(true));
        Assertion.assertThat("Hotel Name is displayed correctly", orderDetailPage.isHotelNameDisplayed(query), is(true));

        Assertion.assertThat("total adult is equal", Integer.toString(adult).equals(totalAdultResult) ,is(true));
        Assertion.assertThat("total night is equal", Integer.toString(night).equals(totalNightResult) ,is(true));
        Assertion.assertThat("total room is equal", Integer.toString(room).equals(totalRoomResult) ,is(true));
        Assertion.assertThat("guest full name is correct", guestFullNameResult ,is(notNullValue()));

        step("Verify user able to share eticket");
        orderDetailPage.clickShareEticket();

        step("Verify share eticket");
        boolean isShareEticketWork = orderDetailPage.verifyShareEticketBtmSheet("homes", lang, issuedOrderID);
        Assertion.assertThat("Verify share eticket", isShareEticketWork, is(true));

        step("Open e-Ticket from header");
        orderDetailPage.clickEticketFromHeader();
        orderDetailPage.clickETicketLanguage("English");

        step("e-Ticket file is opened from header entry point");
        Assertion.assertThat("Verify pdf content open", orderDetailPage.isPdfContentOpen(), is(true));
        orderDetailPage.clickBackFromPdf();

        step("Verify help center section present");
        Assertion.assertThat("Verify help center section present", orderDetailPage.verifyHelpCenterSectionPresent(), is(true));

        step("Verify able to open order detail");
        orderDetailPage.openPriceDetail();
        orderDetailPage.closePriceDetail();

        step("Verify share receipt");
        orderDetailPage.shareReceipt();
        boolean isFileNameCorrect = orderDetailPage.isShareFileNameCorrect("hotel_homes", issuedOrderID,lang);
        Assertion.assertThat("Verify file title has same name", isFileNameCorrect, is(true));

        step("Close share receipt");
        orderDetailPage.closeShareReceiptBtmSheet();

        step("Verify open receipt");
        orderDetailPage.openReceipt();

        step("Verify receipt is open");
        if(Configs.PLATFORM == Platform.IOS){
            isFileNameCorrect = orderDetailPage.isShareFileNameCorrect("hotel_homes", issuedOrderID,lang);
            Assertion.assertThat("Verify file title has same name", isFileNameCorrect, is(true));

            step("Close share receipt");
            orderDetailPage.closeShareReceiptBtmSheet();
        }else{
            Assertion.assertThat("Verify pdf content open", orderDetailPage.isPdfContentOpen(), is(true));
            orderDetailPage.clickBackFromPdf();
        }

        step("User check order Detail ID component");
        Assertion.assertThat("order Detail ID component should be verified", orderDetailPage.verifyOrderDetailIdComponent(), is(true));

        step("user verify reservation details and special request");
        orderDetailPage.seeMoreDetails();
        Assertion.assertThat("Verify special request section", orderDetailPage.verifyReviseSpecialRequest(), is(true));

        step("User checks refund and reschedule Details");
        Assertion.assertThat("refund and reschedule info section should be verified", orderDetailPage.verifyRefundAndRescheduleInfo(), is(true));

        step("User check Refund Button");
        Assertion.assertThat("refund button should be displayed", orderDetailPage.isRefundButtonDisplayed(), is(true));

        step("User verify manage order section");
        Assertion.assertThat("Manage order should be displayed", orderDetailPage.verifyManageOrderSection(), is(true));

        step("User check Reschedule Button");
        Assertion.assertThat("reschedule button should be displayed", orderDetailPage.isRescheduleButtonDisplayed(), is(true));
    }

    @Author(names = {"Akbar", "Prerna"})
    @Vertical(name = "PRO")
    @Tribe(name = "Order")
    @Module(name = "MyOrder")
    @Test
    public void checkHomesDetailOrderGuest() throws ExecutionException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("checkHomesDetailOrderGuest");
        BaseTest.paramsThreadLocal.set(map);

        int totalGuest = Integer.parseInt(map.get("totalGuest"));
        String query = map.get("hotelName");
        String accommodationType = "homes";
        String rangeAddDate = map.get("rangeAddDate");
        String[] dates = generateCheckInOutDate(rangeAddDate, 1);
        String checkInDate = dates[0];
        String checkOutDate = dates[1];

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> hotelLandingPage = LandingPage.getInstance(session);
        HomesLandingPage<WebDriver> homesLandingPage = HomesLandingPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        HotelPage<WebDriver> hotelPage = HotelPage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("user open app as a guest");
        loginPage.loginAsGuest();
        homePage.changeCurrency("IDR","");

        step("Open Homes landing page");
        String lang = homePage.getLanguageApp();

        step("user hit PDP deeplink");
        HotelAutoCompleteResponse hotelAutoCompleteResponsePDP = hotelApi.getAutocompleteData("", "", query, accommodationType.toUpperCase());
        String hotelId = hotelAutoCompleteResponsePDP.data.contents.get(0).publicId;
        String country = hotelAutoCompleteResponsePDP.data.contents.get(0).country.name;
        hotelPage.openPDPDeeplink(accommodationType.toLowerCase(), country.toLowerCase(), hotelId, 1, totalGuest, checkInDate, checkOutDate);

        step("Go to the room list");
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();

        step("Select room from the room list");
        roomListPage.clickBookRoom();

        step("Completing booking form data");
        String expectedHotelName = bookingFormPage.getHotelNameBookingForm();
        String expectedHotelRoomName = bookingFormPage.getHotelRoomNameBookingForm();
        String expectedHotelRoomCount = bookingFormPage.getHotelRoomCountBookingForm();
        String expectedNightDuration = bookingFormPage.getDurationBookingForm();
        bookingFormPage.fillContactOrderDetail();
        String expectedGuestName = bookingFormPage.fillGuestDetail(false);

        step("Submit booking");
        bookingFormPage.continueToPaymentPage();

        step("Payment page should be displayed");
        String issuedOrderID = paymentPage.checkOrderID();

        step("Pay with ATM");
        paymentPage.openPaymentMethod();
        paymentPage.selectPaymentMethod("ATM");
        paymentPage.clickPayButton();
        paymentPage.paymentChecker();

        step("Open my order page");
        paymentPage.seeOrderList();

        step("Find the issued order");
        orderListPage.openActiveTicketViaDeeplink(issuedOrderID);

        step("get reservation detail");
        String orderIdResult = orderDetailPage.getOrderID();

        String totalAdultResult = orderDetailPage.getTotalAdult();
        String totalNightResult = orderDetailPage.getTotalNight(issuedOrderID);
        String totalRoomResult = orderDetailPage.getTotalRoom();
        totalRoomResult = totalRoomResult.split("x")[0].trim();
        String guestFullNameResult = orderDetailPage.getGuestFullName();

        step("Assertion");
        Assertion.assertThat("Order ID is equal", issuedOrderID.equals(orderIdResult), is(true));
        Assertion.assertThat("Hotel Name is displayed correctly", orderDetailPage.isHotelNameDisplayed(query), is(true));

        Assertion.assertThat("total adult is equal", Integer.toString(totalGuest).equals(totalAdultResult) ,is(true));
        Assertion.assertThat("total night is equal", expectedNightDuration.equals(totalNightResult) ,is(true));
        Assertion.assertThat("total room is equal", expectedHotelRoomCount.equals(totalRoomResult) ,is(true));
        Assertion.assertThat("guest full name is equal", guestFullNameResult.contains(expectedGuestName) ,is(true));

        step("Verify user able to share eticket");
        orderDetailPage.clickShareEticket();

        step("Verify share eticket");
        boolean isShareEticketWork = orderDetailPage.verifyShareEticketBtmSheet("hotel_homes", lang, issuedOrderID);
        Assertion.assertThat("Verify share eticket", isShareEticketWork, is(true));

        step("Open e-Ticket from header");
        orderDetailPage.clickEticketFromHeader();
        orderDetailPage.clickETicketLanguage("English");

        step("e-Ticket file is opened from header entry point");
        Assertion.assertThat("Verify pdf content open", orderDetailPage.isPdfContentOpen(), is(true));
        orderDetailPage.clickBackFromPdf();

        step("Verify help center section present");
        Assertion.assertThat("Verify help center section present", orderDetailPage.verifyHelpCenterSectionPresent(), is(true));

        step("Verify able to open order detail");
        orderDetailPage.openPriceDetail();
        orderDetailPage.closePriceDetail();

        step("Verify share receipt");
        orderDetailPage.shareReceipt();
        boolean isFileNameCorrect = orderDetailPage.isShareFileNameCorrect("hotel_homes", issuedOrderID,lang);
        Assertion.assertThat("Verify file title has same name", isFileNameCorrect, is(true));

        step("Close share receipt");
        orderDetailPage.closeShareReceiptBtmSheet();

        step("Verify open receipt");
        orderDetailPage.openReceipt();

        step("Verify receipt is open");
        if(Configs.PLATFORM == Platform.IOS){
            isFileNameCorrect = orderDetailPage.isShareFileNameCorrect("hotel_homes", issuedOrderID,lang);
            Assertion.assertThat("Verify file title has same name", isFileNameCorrect, is(true));

            step("Close share receipt");
            orderDetailPage.closeShareReceiptBtmSheet();
        }else{
            Assertion.assertThat("Verify pdf content open", orderDetailPage.isPdfContentOpen(), is(true));
            orderDetailPage.clickBackFromPdf();
        }

        step("User check order Detail ID component");
        Assertion.assertThat("order Detail ID component should be verified", orderDetailPage.verifyOrderDetailIdComponent(), is(true));

        step("User checks refund and reschedule Details");
        Assertion.assertThat("refund and reschedule info section should be verified", orderDetailPage.verifyRefundAndRescheduleInfo(), is(true));

        step("User check Refund Button");
        Assertion.assertThat("refund button should be displayed", orderDetailPage.isRefundButtonDisplayed(), is(true));

        step("User verify manage order section");
        Assertion.assertThat("Manage order should be displayed", orderDetailPage.verifyManageOrderSection(), is(true));

        step("User check Reschedule Button");
        Assertion.assertThat("reschedule button should be displayed", orderDetailPage.isRescheduleButtonDisplayed(), is(true));
    }

    @Author(names = {"Zia"})
    @Module(name = "MyOrder")
    @Tribe(name = "Order")
    @Vertical(name = "PRO")
    @Test()
    public void testCheckOrderHistoryGuest() {
        Session<WebDriver> session = sessionThreadLocal.get();

        Map<String, String> params = getParam("testCheckOrderHistoryGuest");
        BaseTest.paramsThreadLocal.set(params);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        SearchPageCarRental<WebDriver> carRentalSearchPage = SearchPageCarRental.getInstance(session);
        SearchResultPageCarRental<WebDriver> carRentalSearchResultPage = SearchResultPageCarRental.getInstance(session);
        CarRentalReviewBookingPage<WebDriver> carRentalReviewBookingPage = CarRentalReviewBookingPage.getInstance(session);
        CarRentalBookingPage<WebDriver> carRentalBookingPage = CarRentalBookingPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        LoginPage <WebDriver> loginPage = LoginPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderHistoryPage<WebDriver> orderHistoryPage = OrderHistoryPage.getInstance(session);

        String Location = params.get("location");
        String pickupLocation = params.get("pickupLocation");
        String email = params.get("email");
        String carRentalPackage = params.get("carRentalPackage");
        String dateRental = CoreFunctions.getCurrentLongDate();
        String phoneNumber = params.get("phoneNumber");
        String testTypeFilter = params.get("typeFilter");
        String durationFilter = params.get("durationFilter");

        step("Go To Home");
        homePage.gotoHome();

        step("User login As Guest");
        loginPage.loginAsGuest();

        step("Set currency to IDR" );
        homePage.changeCurrency("IDR","");

        step("Open Car Landing Page");
        homePage.openVerticalPageDeeplink("Car Rentals","","IDR");
        carRentalSearchPage.closeInformationPopUp();

        step("Select car rental type");
        carRentalSearchPage.chooseDriver("with_driver");

        step("Select car rental date");
        carRentalSearchPage.selectDate(CoreFunctions.addToLongDate(dateRental,2));

        step("Input car rental location");
        carRentalSearchPage.chooseLocation(Location);

        step("Search car rental");
        carRentalSearchPage.searchCar();

        step("choose car");
        carRentalSearchResultPage.chooseCar();

        step("continue to booking form from review page");
        carRentalReviewBookingPage.continueFromReviewDetailCar();

        step("pick up location");
        carRentalBookingPage.choosePickUpLocation(pickupLocation);

        step("continue to complete booking");
        carRentalBookingPage.continueToCompleteBookingPage();

        step("input contact detail");
        carRentalBookingPage.fillGuestDetailOrder(email,phoneNumber);
        carRentalBookingPage.setContinueToPayment();
        String orderID = paymentPage.checkOrderID();

        step("Open payment method");
        paymentPage.openPaymentMethod();

        step("Submit the payment method");
        paymentPage.selectPaymentMethod("BCA Virtual Account");
        paymentPage.clickPayButton();

        step("Open order list page");
        paymentPage.seeOrderList();

        step("Open pick one action");
        orderListPage.findOrder(orderID);

        step("Cancel order");
        orderListPage.cancelOrder();

        step("open order history");
        orderListPage.openOrderHistory();

        step("Check if past order and refund lists are visible");
        Assertion.assertThat("Past order list visibility", orderHistoryPage.verifyPastOrderList(), is(true));

        step("Apply Sort");
        orderHistoryPage.applySort();

        step("Verify order status, filter, and sort functionality on the Order History Page");
        boolean orderHistoryPageVerification = orderHistoryPage.verifyCancelStatusOnOrderHistoryPage(orderID, testTypeFilter, durationFilter);
        Assertion.assertThat("Order status and filter functionality", orderHistoryPageVerification, is(true));
        Assertion.assertThat("Sort functionality applied", orderHistoryPage.bothSortAndFilterApplied(), is(true));
        Assertion.assertThat("Past refund list visibility", orderHistoryPage.verifyPastRefundList("guest"), is(true));
    }

    @Author(names = {"Areeb"})
    @Module(name = "MyOrder")
    @Tribe(name = "Order")
    @Vertical(name = "Liberty")
    @Test
    public void verifyPreviousBookingInOrderHistoryForNonLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> params = getParam("verifyPreviousBookingInOrderHistoryForNonLogin");
        BaseTest.paramsThreadLocal.set(params);

        var homePage = HomePage.getInstance(session);
        var loginPage = LoginPage.getInstance(session);
        var orderListPage = OrderListPage.getInstance(session);
        var orderHistoryPage = OrderHistoryPage.getInstance(session);
        var tripViewPage = TripPage.getInstance(session);

        step("Go to home");
        homePage.gotoHome();

        step("Use app as guest");
        loginPage.loginAsGuest();

        step("Open my order page");
        homePage.openMyOrderPage();
        tripViewPage.skipCoachmark();

        step("Open order history");
        orderListPage.openOrderHistory();

        step("Verify 12 months filter is selected by default");
        Assertion.assertThat("12 months filter is selected by default", orderHistoryPage.isTwelveMonthsFilterSelected(), is(true));

        step("Click see previous bookings button");
        orderHistoryPage.clickSeePreviousBookings();

        step("Verify filter tab is opened");
        Assertion.assertThat("Filter tab is opened", orderHistoryPage.isFilterTabOpened(), is(true));
    }


    @Author(names = {"Zia"})
    @Module(name = "MyOrder")
    @Tribe(name = "Order")
    @Vertical(name = "PRO")
    @Test(dataProvider="testCheckCancelOrderLoginDataProvider")
    public void testCheckCancelOrderLogin(Map<String, String> params) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(params);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        SearchPageCarRental<WebDriver> carRentalSearchPage = SearchPageCarRental.getInstance(session);
        SearchResultPageCarRental<WebDriver> carRentalSearchResultPage = SearchResultPageCarRental.getInstance(session);
        CarRentalReviewBookingPage<WebDriver> carRentalReviewBookingPage = CarRentalReviewBookingPage.getInstance(session);
        CarRentalBookingPage<WebDriver> carRentalBookingPage = CarRentalBookingPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderHistoryPage<WebDriver> orderHistoryPage = OrderHistoryPage.getInstance(session);
        var tripPage = TripPage.getInstance(session);

        String Location = params.get("location");
        String pickupLocation = params.get("pickupLocation");
        String username = params.get("username");
        String password = params.get("password");
        String dateRental = CoreFunctions.getCurrentLongDate();
        String paymentType = params.get("paymentType");
        String testTypeFilter = params.get("typeFilter");
        String durationFilter = params.get("durationFilter");

        step("Go To Home");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink and change currency to IDR");
        homePage.loginWithSpecificAccountViaDeeplink(username, password, "IDR");

        step("Open Car Landing Page");
        homePage.openVerticalPageDeeplink("Car Rentals","","IDR");
        carRentalSearchPage.closeInformationPopUp();
        carRentalSearchPage.allowLocationAccess();

        step("Select car rental type");
        carRentalSearchPage.chooseDriver("with_driver");

        step("Select car rental date");
        carRentalSearchPage.selectDate(CoreFunctions.addToLongDate(dateRental,2));

        step("Input car rental location");
        carRentalSearchPage.chooseLocation(Location);

        step("Searh car rental");
        carRentalSearchPage.searchCar();

        step("choose car");
        carRentalSearchResultPage.chooseCar();

        step("continue to booking form from review page");
        carRentalReviewBookingPage.continueFromReviewDetailCar();

        step("pick up location");
        carRentalBookingPage.choosePickUpLocationAndToggleForDropOff(pickupLocation);

        step("continue to complete booking");
        carRentalBookingPage.continueToCompleteBookingPage();
        carRentalBookingPage.setContinueToPayment();
        String orderID = paymentPage.checkOrderID();

        step("Open payment method");
        paymentPage.openPaymentMethod();

        step("Select BCA Virtual as payment method");
        paymentPage.selectPaymentMethod(paymentType);

        step("Submit payment");
        paymentPage.clickPayButton();

        step("Go to the order list");
        paymentPage.seeOrderList();
        tripPage.skipCoachmark();
        tripPage.switcherPage.switchToOrder();

        step("Open pick one action");
        orderListPage.findOrder(orderID);

        step("Cancel order");
        boolean cancelOrderStatusOrderListPage = orderListPage.cancelOrder();

        step("open order history");
        orderListPage.openOrderHistory();

        step("Verify order status on order history page");
        boolean cancelOrderStatusOrderHistoryPage =orderHistoryPage.verifyCancelStatusOnOrderHistoryPage(orderID,testTypeFilter,durationFilter);

        Assertion.assertThat("Order status on order History Page", cancelOrderStatusOrderHistoryPage, is(true));
        Assertion.assertThat("Order status toast on order List Page (Check video on failure due to short lifetime of toast msg)", cancelOrderStatusOrderListPage, is(true));
    }

    @Author(names = {"Zia"})
    @Module(name = "MyOrder")
    @Tribe(name = "Order")
    @Vertical(name = "PRO")
    @Test(dataProvider="testCheckDeleteOrderLoginDataProvider")
    public void testCheckDeleteOrderLogin(Map<String, String> params) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(params);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        SearchPageCarRental<WebDriver> carRentalSearchPage = SearchPageCarRental.getInstance(session);
        SearchResultPageCarRental<WebDriver> carRentalSearchResultPage = SearchResultPageCarRental.getInstance(session);
        CarRentalReviewBookingPage<WebDriver> carRentalReviewBookingPage = CarRentalReviewBookingPage.getInstance(session);
        CarRentalBookingPage<WebDriver> carRentalBookingPage = CarRentalBookingPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderHistoryPage<WebDriver> orderHistoryPage = OrderHistoryPage.getInstance(session);
        var tripPage = TripPage.getInstance(session);

        String Location = params.get("location");
        String pickupLocation = params.get("pickupLocation");
        String loginMethod = params.get("loginType");
        String username = params.get("username");
        String password = params.get("password");
        String dateRental = CoreFunctions.getCurrentLongDate();
        String paymentType = params.get("paymentType");

        step("Go To Home");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink and change currency to IDR");
        homePage.loginWithSpecificAccountViaDeeplink(username, password, "IDR");

        step("Open Car Landing Page");
        homePage.openVerticalPageDeeplink("Car Rentals","","IDR");
        carRentalSearchPage.closeInformationPopUp();
        carRentalSearchPage.allowLocationAccess();

        step("Select car rental type");
        carRentalSearchPage.chooseDriver("with_driver");

        step("Select car rental date");
        carRentalSearchPage.selectDate(CoreFunctions.addToLongDate(dateRental,2));

        step("Input car rental location");
        carRentalSearchPage.chooseLocation(Location);

        step("Searh car rental");
        carRentalSearchPage.searchCar();

        step("choose car");
        carRentalSearchResultPage.chooseCar();

        step("continue to booking form from review page");
        carRentalReviewBookingPage.continueFromReviewDetailCar();

        step("pick up location");
        carRentalBookingPage.choosePickUpLocationAndToggleForDropOff(pickupLocation);

        step("continue to complete booking");
        carRentalBookingPage.continueToCompleteBookingPage();
        carRentalBookingPage.setContinueToPayment();
        String orderID = paymentPage.checkOrderID();

        step("Open payment method");
        paymentPage.openPaymentMethod();

        step("Select e-wallet as payment method");
        paymentPage.selectPaymentMethod(paymentType);

        step("Submit payment");
        paymentPage.clickPayButton();

        step("Go to the order list");
        paymentPage.seeOrderList();
        tripPage.skipCoachmark();
        tripPage.switcherPage.switchToOrder();

        step("Open pick one action");
        orderListPage.findOrder(orderID);

        step("Cancel order");
        orderListPage.cancelOrder();

        step("open order history");
        orderListPage.openOrderHistory();

        step("Delete the recent order on order history page");
        orderHistoryPage.deleteOrder(orderID);

        boolean isOrderDeleted = orderHistoryPage.isOrderDeleted(orderID);
        Assertion.assertThat("Order is deleted and is not on Order History", isOrderDeleted, is(true));
    }

    @Author(names = {"Zia"})
    @Module(name = "MyOrder")
    @Tribe(name = "Order")
    @Vertical(name = "PRO")
    @Test(dataProvider="testCheckDeleteOrderGuestDataProvider")
    public void testCheckDeleteOrderGuest(Map<String, String> params) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(params);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        SearchPageCarRental<WebDriver> carRentalSearchPage = SearchPageCarRental.getInstance(session);
        SearchResultPageCarRental<WebDriver> carRentalSearchResultPage = SearchResultPageCarRental.getInstance(session);
        CarRentalReviewBookingPage<WebDriver> carRentalReviewBookingPage = CarRentalReviewBookingPage.getInstance(session);
        CarRentalBookingPage<WebDriver> carRentalBookingPage = CarRentalBookingPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderHistoryPage<WebDriver> orderHistoryPage = OrderHistoryPage.getInstance(session);

        String Location = params.get("location");
        String pickupLocation = params.get("pickupLocation");
        String email = params.get("email");
        String carRentalDuration = params.get("carRentalDuration");
        String carRentalPackage = params.get("carRentalPackage");
        String carRentalSpecialRequest = params.get("specialRequest");
        String dateRental = CoreFunctions.getCurrentLongDate();
        String phoneNumber = params.get("phoneNumber");

        step("Go To Home");
        homePage.gotoHome();

        step("User user app as guest");
        loginPage.loginAsGuest();

        step("change currency to idr");
        homePage.changeCurrency("IDR","");

        step("Open Car Landing Page");
        homePage.openVerticalPageDeeplink("Car Rentals","","IDR");
        carRentalSearchPage.closeInformationPopUp();

        step("Select car rental type");
        carRentalSearchPage.chooseDriver("with_driver");

        step("Select car rental date");
        carRentalSearchPage.selectDate(CoreFunctions.addToLongDate(dateRental,2));

        step("Input car rental location");
        carRentalSearchPage.chooseLocation(Location);

        step("Search car rental");
        carRentalSearchPage.searchCar();

        step("choose car");
        carRentalSearchResultPage.chooseCar();

        step("continue to booking form from review page");
        carRentalReviewBookingPage.continueFromReviewDetailCar();

        step("pick up location");
        carRentalBookingPage.choosePickUpLocation(pickupLocation);

        step("continue to complete booking");
        carRentalBookingPage.continueToCompleteBookingPage();

        step("input contact detail");
        carRentalBookingPage.fillGuestDetailOrder(email,phoneNumber);
        carRentalBookingPage.setContinueToPayment();
        String orderID = paymentPage.checkOrderID();

        step("Open payment method");
        paymentPage.openPaymentMethod();

        step("Submit the payment method");
        paymentPage.selectPaymentMethod("BCA Virtual Account");
        paymentPage.clickPayButton();

        step("Open order list page");
        homePage.gotoHome();
        homePage.openMyOrderPage();

        step("Open pick one action");
        orderListPage.findOrder(orderID);

        step("Cancel order");
        orderListPage.cancelOrder();

        step("open order history");
        orderListPage.openOrderHistory();

        step("Delete the recent order on order history page");
        orderHistoryPage.deleteOrder(orderID);

        boolean isOrderDeleted = orderHistoryPage.isOrderDeleted(orderID);
        Assertion.assertThat("Order is deleted and is not on Order History", isOrderDeleted, is(true));
    }

    @Author(names = {"Zia"})
    @Module(name = "MyOrder")
    @Tribe(name = "Order")
    @Vertical(name = "PRO")
    @Test(dataProvider="testCheckCancelOrderGuestDataProvider")
    public void testCheckCancelOrderGuest(Map<String, String> params) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(params);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        SearchPageCarRental<WebDriver> carRentalSearchPage = SearchPageCarRental.getInstance(session);
        SearchResultPageCarRental<WebDriver> carRentalSearchResultPage = SearchResultPageCarRental.getInstance(session);
        CarRentalReviewBookingPage<WebDriver> carRentalReviewBookingPage = CarRentalReviewBookingPage.getInstance(session);
        CarRentalBookingPage<WebDriver> carRentalBookingPage = CarRentalBookingPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderHistoryPage<WebDriver> orderHistoryPage = OrderHistoryPage.getInstance(session);

        String Location = params.get("location");
        String pickupLocation = params.get("pickupLocation");
        String carRentalDuration = params.get("carRentalDuration");
        String carRentalPackage = params.get("carRentalPackage");
        String email = params.get("email");
        String phoneNumber = params.get("phoneNumber");
        String dateRental = CoreFunctions.getCurrentLongDate();
        String paymentType = params.get("paymentType");
        String testTypeFilter = params.get("typeFilter");
        String durationFilter = params.get("durationFilter");

        step("Go To Home");
        homePage.gotoHome();

        step("input credential");
        loginPage.loginAsGuest();

        step("Change currency to 'IDR'");
        homePage.changeCurrency("IDR","");

        step("Open Car Landing Page");
        homePage.openVerticalPageDeeplink("Car Rentals","","IDR");
        carRentalSearchPage.closeInformationPopUp();

        step("Select car rental type");
        carRentalSearchPage.chooseDriver("with_driver");

        step("Select car rental date");
        carRentalSearchPage.selectDate(CoreFunctions.addToLongDate(dateRental,2));

        step("Input car rental location");
        carRentalSearchPage.chooseLocation(Location);

        step("Search car rental");
        carRentalSearchPage.searchCar();

        step("choose car");
        carRentalSearchResultPage.chooseCar();

        step("continue to booking form from review page");
        carRentalReviewBookingPage.continueFromReviewDetailCar();

        step("pick up location");
        carRentalBookingPage.choosePickUpLocation(pickupLocation);

        step("continue to complete booking");
        carRentalBookingPage.continueToCompleteBookingPage();

        step("input contact detail");
        carRentalBookingPage.fillGuestDetailOrder(email,phoneNumber);
        carRentalBookingPage.setContinueToPayment();
        String orderID = paymentPage.checkOrderID();

        step("Open payment method");
        paymentPage.openPaymentMethod();

        step("Select BCA Virtual as payment method");
        paymentPage.selectPaymentMethod(paymentType);

        step("Submit payment");
        paymentPage.clickPayButton();

        step("Go to the order list");
        paymentPage.seeOrderList();

        step("Open pick one action");
        orderListPage.findOrder(orderID);

        step("Cancel order");
        boolean cancelOrderStatusOrderListPage = orderListPage.cancelOrder();

        step("open order history");
        orderListPage.openOrderHistory();

        step("Verify order status on order history page");
        boolean cancelOrderStatusOrderHistoryPage =orderHistoryPage.verifyCancelStatusOnOrderHistoryPage(orderID,testTypeFilter,durationFilter);

        Assertion.assertThat("Order status popup on order List Page", cancelOrderStatusOrderListPage, is(true));
        Assertion.assertThat("Order status on order History Page", cancelOrderStatusOrderHistoryPage, is(true));
    }

    @Author(names = {"Prerna", "Yazid"})
    @Module(name = "MyOrder")
    @Tribe(name = "Order")
    @Vertical(name = "PRO")
    @Test
    public void checkHotelDetailOrderGuest() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("checkHotelDetailOrderGuest");
        BaseTest.paramsThreadLocal.set(map);
        String hotelName = map.get("hotelName");
        String accommodationType = "hotel";
        String rangeAddDate = map.get("rangeAddDate");
        String protectionType = map.get("protectionType");
        String paymentMethod = map.get("paymentMethod");
        String[] dates = generateCheckInOutDate(rangeAddDate, 1);
        String checkInDate = dates[0];
        String checkOutDate = dates[1];

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        HotelPage<WebDriver> hotelPage = HotelPage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);

        step("Goto home");
        homePage.gotoHome();

        step("user open app as a guest");
        loginPage.loginAsGuest();
        
        step("Set currency to IDR");
        homePage.changeCurrency("IDR","guest");

        step("Open PDP via deeplink for hotel '" + hotelName + "'");
        HotelAutoCompleteResponse hotelAutoCompleteResponse =
                hotelApi.getAutocompleteData("", "", hotelName, accommodationType.toUpperCase());
        int selectedIndex = hotelApi.findHotelIndex(hotelAutoCompleteResponse, hotelName);
        String hotelId = hotelAutoCompleteResponse.data.contents.get(selectedIndex).publicId;
        String country = hotelAutoCompleteResponse.data.contents.get(selectedIndex).country.name;
        hotelPage.openPDPDeeplink(
                accommodationType.toLowerCase(), country.toLowerCase(), hotelId, 1, 1, checkInDate, checkOutDate);

        step("Go to the room list");
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();

        step("Select room from the room list");
        roomListPage.clickBookRoom();

        step("Completing booking form data");
        bookingFormPage.fillContactOrderDetail();
        bookingFormPage.fillGuestDetail(false);

        step("get data from booking form");
        hotelName = bookingFormPage.getHotelNameBookingForm();
        String roomName = bookingFormPage.getHotelRoomNameBookingForm();
        String roomCount = bookingFormPage.getHotelRoomCountBookingForm();
        String nightDuration = bookingFormPage.getDurationBookingForm();

        step("User go to 2nd page booking form");
        bookingFormPage.continueToAddOnPage();

        step("user choose and add protection: " + protectionType);
        bookingFormPage.chooseProtection(protectionType);
        String protectionPrice = bookingFormPage.getProtectionPrice();
        bookingFormPage.addProtection();
        bookingFormPage.getTotalToPayBookingForm();
        bookingFormPage.openSubtotal();

        Assertion.assertThat("Protection " + protectionType + " should be added with price " + protectionPrice,
                bookingFormPage.isProtectionAdded(protectionPrice), is(true));

        step("Submit Booking");
        bookingFormPage.continueToPaymentPage();
        bookingFormPage.PriceChangesPopUp();

        step("Choose payment method");
        String orderID =paymentPage.checkOrderID();

        step("Select "+paymentMethod+" as a payment method");
        paymentPage.openPaymentMethod();
        paymentPage.selectPaymentMethod(paymentMethod);
        paymentPage.clickPayButton();
        Assertion.assertThat("Payment Successful and Thank You page is shown", paymentPage.isThankYouPageShown(), is(true));

        step("Find the issued order");
        orderListPage.openActiveTicketViaDeeplink(orderID);
        Assertion.assertThat("Order ID is displayed correctly", orderDetailPage.getOrderID(), is(orderID));
        Assertion.assertThat("Main e-ticket Displayed", orderDetailPage.isMainETiketCardDisplayed(), is(true));
        Assertion.assertThat("E-ticket card Displayed", orderDetailPage.isETiketAndReceiptCardDisplayed(), is(true));
        Assertion.assertThat("Hotel Name is displayed correctly", orderDetailPage.isHotelNameDisplayed(hotelName), is(true));
        Assertion.assertThat("order Detail ID component should be verified", orderDetailPage.verifyOrderDetailIdComponent(), is(true));
        Assertion.assertThat("Room Name is displayed correctly", orderDetailPage.isRoomNameDisplayed(roomName), is(true));
        Assertion.assertThat("Total night is equal", orderDetailPage.getTotalNight(orderID) ,is((nightDuration)));
        Assertion.assertThat("Total room is equal", orderDetailPage.getTotalRoom().trim() ,is(roomCount));
        Assertion.assertThat("call button should be clicked", orderDetailPage.clickCallBtn(), is(true));
        Assertion.assertThat("Button Full Protection Submit New Claim should be visible", orderDetailPage.ButtonFullProtectionSubmitNewClaim(), is(true));
        Assertion.assertThat("location redirection button should be verified", orderDetailPage.verifyLocationRedirectionButton(), is(true));
        Assertion.assertThat("refund and reschedule info section should be verified", orderDetailPage.verifyRefundAndRescheduleInfo(), is(true));
        Assertion.assertThat("LPG Banner should be displayed", orderDetailPage.verifyLpgBanner(), is(true));
        Assertion.assertThat("refund button should be displayed", orderDetailPage.isRefundButtonDisplayed(), is(true));
        Assertion.assertThat("reschedule button should be displayed", orderDetailPage.isRescheduleButtonDisplayed(), is(true));
        Assertion.assertThat("edit booking button should be displayed", orderDetailPage.verifyEditGuestDetail(), is(true));
        Assertion.assertThat("Page Module should be displayed", orderDetailPage.verifyPageModule(), is(true));
    }
    @Author(names = {"Kartik"})
    @Module(name = "MyOrder")
    @Tribe(name = "order")
    @Vertical(name = "PRO")
    @Test(dataProvider="testflightCheckOrderDetailLoginDataProvider")
    public void flightCheckOrderDetailLogin(Map<String, String> param) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightProductReviewPage<WebDriver> flightProductReviewPage = FlightProductReviewPage.getInstance(session);
        FlightBookingFormPage<WebDriver> flightBookingFormPage = FlightBookingFormPage.getInstance(session);
        FlightPage<WebDriver> flightPage = FlightPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        FlightApi<?> flightApi = new FlightApi<>(session);

        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");
        String email = param.get("email");
        String phoneNumber = param.get("phoneNumber");
        String airline = param.get("airline");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        int duration = Integer.parseInt(param.get("duration"));
        String currency = "IDR";
        String loginMethod = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Find dynamic data");
        FlightDynamicData flightDynamicData = flightApi.findDateRT(
                username,password,
                originAirport, destinationAirport, daysToDepartureDate, duration,
                adultPassengers, childPassengers, infantPassengers,
                tripType, airline,airline,"","","","",currency
        );
        String departureDate = CoreFunctions.convertToLongDate(flightDynamicData.departureDate);
        String returnDate = CoreFunctions.convertToLongDate(flightDynamicData.returnDate);
        String departureDateYear = flightDynamicData.departureDate.split("-")[0];
        String returnDateYear = flightDynamicData.returnDate.split("-")[0];
        airline = flightDynamicData.airlineDepart;

        step("login as guest");
        loginPage.loginAsGuest();

        step("login with : " + username);
        loginPage.loginWith(loginMethod, username, password);

        step("Set currency to IDR");
        homePage.changeCurrency(currency,username);

        step("Open flights page");
        homePage.openVerticalPageDeeplink("Flights","",currency);
        flightSearchPage.closePopup();

        step("Toggle trip type");
        flightSearchPage.toggleTriptype(tripType);

        step("Select origin airport");
        flightSearchPage.selectOrigin(originAirport);
        flightSearchPage.closePopup();

        step("Select destination airport");
        flightSearchPage.selectDestination(destinationAirport);

        step("Add passenger");
        flightSearchPage.addPassenger(adultPassengers, childPassengers, infantPassengers);

        step("change date using deeplink");
        flightSearchPage.changeDateViaDeeplink(flightDynamicData, tripType);

        step("Toggle trip type { " + tripType + " }");
        flightSearchPage.toggleTriptype(tripType);

        step("Select date");
        flightSearchPage.selectDate(departureDate,returnDate);

        step("Search flight");
        flightSearchPage.searchFlight();

        step("Open filter");
        flightSearchResultPage.openFilter();

        step("Filter airline");
        flightSearchResultPage.filterAirline(airline);

        step("Save filter");
        flightSearchResultPage.saveFilter();

        step("Click flight detail chevron");
        flightSearchResultPage.selectFlight();

        step("Open filter");
        flightSearchResultPage.openFilter();

        step("Filter airline");
        flightSearchResultPage.filterAirline(airline);

        step("Save filter");
        flightSearchResultPage.saveFilter();

        step("Click flight detail chevron");
        flightSearchResultPage.selectFlight();

        step("Continue to booking form");
        flightProductReviewPage.continueToBookingForm(tripType);

        step("Get flight date time data");
        List<String> flightDateTime =  flightBookingFormPage.getFlightDateTime(tripType);
        //add year to expected flightdatetime
        String expectedDepartTime = flightDateTime.get(0).replace(" • ", " " + departureDateYear+" • ");
        String expectedDepartArrivalTime = flightDateTime.get(1).replace(" - ", " " + departureDateYear+" • ");
        String expectedReturnTime = flightDateTime.get(2).replace(" • ", " " + returnDateYear+" • ");
        String expectedReturnArrivalTime = flightDateTime.get(3).replace(" - ", " " + departureDateYear+" • ");

        step("Fill passenger detail");
        flightBookingFormPage.fillPassengerDetail(adultPassengers, childPassengers, infantPassengers, "domestic",airline);

        step("Continue to second half booking form");
        flightBookingFormPage.continueToSecondHalf();

        step("Continue to payment");
        flightBookingFormPage.continueToPayment();

        step("Skip full protection");
        flightBookingFormPage.skipFullProtection();

        step("Get booking page passenger list name");
        List<String> expectedPassengerList = flightBookingFormPage.getpassengerNames();

        step("Check order ID");
        String orderID = paymentPage.checkOrderID();

        step("Open payment method");
        paymentPage.openPaymentMethod();

        step("Select BCA Virtual as payment method");
        paymentPage.selectPaymentMethod("BCA Virtual Account");

        step("Get total payment from payment page");
        String totalPayment = paymentPage.getTotalPrice();

        step("Submit payment");
        paymentPage.clickPayButton();

        step("Go to the order list");
        paymentPage.seeOrderList();

        step("Open recent order in order group page");
        orderListPage.seeDetails();

        step("Verify order id is as expected in order group page");
        Assertion.assertThat("Verify order id is as expected", orderDetailPage.getOrderID(),containsString(orderID));

        step("Verify waiting payment component showing in order group page");
        Assertion.assertThat("Verify waiting payment component showing", orderDetailPage.verifyCompletePaymentExpiryTime(), is(true));

        step("verify origin data in order group page");
        String flightDepatureDate = orderDetailPage.getDepartureDate();
        Assertion.assertThat("departflight date should be as expected",flightDepatureDate, equalTo(expectedDepartTime.replaceAll(" • "," ")));
        String flightReturnDate = orderDetailPage.getReturnDate();
        Assertion.assertThat("flightReturnDate date should be as expected",flightReturnDate, equalTo(expectedReturnTime.replaceAll(" • "," ")));
        String departureAirline = orderDetailPage.getOrderGroupDepartureAirline();
        Assertion.assertThat("Verify departure airline", departureAirline, equalTo(airline));
        String returnAirline = orderDetailPage.getOrderGroupReturnAirline();
        Assertion.assertThat("Verify return airline", returnAirline, equalTo(airline));
        String departAirportname = orderDetailPage.geDepartAirportOrderGroup();
        Assertion.assertThat("depart airport details should be same", departAirportname, containsString(originAirport));
        String returnAirportName = orderDetailPage.getReturnAirportOrderGroup();
        Assertion.assertThat("return airport details should be same", returnAirportName, containsString(destinationAirport));

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
        Assertion.assertThat("Departure flight depart time", flightDepatureDateOrderDetail, equalTo(expectedDepartTime.replaceAll("•", "-")));
        Assertion.assertThat("Departure flight arrival time", flightArrivalDateOrderDetail, equalTo(expectedDepartArrivalTime.replaceAll("•", "-")));
        String totalPaymentOrderDetail = orderDetailPage.getTotalPayment();
        Assertion.assertThat("Verify total payment in order detail", CoreFunctions.convertPriceSafely(totalPaymentOrderDetail), equalTo(CoreFunctions.convertPriceSafely(totalPayment)));

        step("Verify flight depart passenger name");
        List<String> passengerList = orderDetailPage.getFlightsPassengerList();
        for(String element:expectedPassengerList){
            Assertion.assertThat("Verify passenger list",passengerList, hasItems(containsString(element)));
        }

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
        Assertion.assertThat("Departure flight depart time", flightDepatureDateOrderDetail, equalTo(expectedReturnTime.replaceAll("•", "-")));
        Assertion.assertThat("Departure flight arrival time", flightArrivalDateOrderDetail, equalTo(expectedReturnArrivalTime.replaceAll("•", "-")));
        totalPaymentOrderDetail = orderDetailPage.getTotalPayment();
        Assertion.assertThat("Verify total payment in order detail", CoreFunctions.convertPriceSafely(totalPaymentOrderDetail), equalTo(CoreFunctions.convertPriceSafely(totalPayment)));

        step("Verify flight return passenger name");
        List<String> passengerListReturn = orderDetailPage.getFlightsPassengerList();
        for(String element: passengerListReturn){
            Assertion.assertThat("Verify passenger list",passengerList, hasItems(containsString(element)));
        }

        step("back to order group page");
        orderDetailPage.clickBack();

        step("Check redirection to complete payment");
        orderDetailPage.completePaymentFromOrderGroupPaymentPage();
        Assertion.assertThat("Number va is visible", paymentPage.isVaNumberShow(), is(true));
    }

    @Author(names = {"Kartik","Dendi"})
    @Module(name = "MyOrder")
    @Tribe(name = "Order")
    @Vertical(name = "PRO")
    @Test(dataProvider="testflightCheckOrderDetailGuestDataProvider")
    public void flightCheckOrderDetailGuest(Map<String, String> param) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightProductReviewPage<WebDriver> flightProductReviewPage = FlightProductReviewPage.getInstance(session);
        FlightBookingFormPage<WebDriver> flightBookingFormPage = FlightBookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        FlightApi<?> flightApi = new FlightApi<>(session);

        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");
        String email = param.get("email");
        String phoneNumber = param.get("phoneNumber");
        String airline = param.get("airline");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        int duration = Integer.parseInt(param.get("duration"));
        String currency = "IDR";

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Find dynamic data");
        FlightDynamicData flightDynamicData = flightApi.findDateRT(
                "","",
                originAirport, destinationAirport, daysToDepartureDate, duration,
                adultPassengers, childPassengers, infantPassengers,
                tripType, airline,airline,"","","","",currency
        );
        String departureDate = CoreFunctions.convertToLongDate(flightDynamicData.departureDate);
        String returnDate = CoreFunctions.convertToLongDate(flightDynamicData.returnDate);
        String departureDateYear = flightDynamicData.departureDate.split("-")[0];
        String returnDateYear = flightDynamicData.returnDate.split("-")[0];
        airline = flightDynamicData.airlineDepart;

        step("login as guest");
        loginPage.loginAsGuest();

        step("Set currency to IDR");
        homePage.changeCurrency(currency,"");

        step("Open flights page");
        homePage.openVerticalPageDeeplink("Flights","",currency);
        flightSearchPage.closePopup();

        step("change date using deeplink");
        flightSearchPage.changeDateViaDeeplink(flightDynamicData, tripType);

        step("Toggle trip type { " + tripType + " }");
        flightSearchPage.toggleTriptype(tripType);

        step("Select origin airport");
        flightSearchPage.selectOrigin(originAirport);
        flightSearchPage.closePopup();

        step("Select destination airport");
        flightSearchPage.selectDestination(destinationAirport);

        step("Add passenger");
        flightSearchPage.addPassenger(adultPassengers, childPassengers, infantPassengers);

        step("change trip type");
        flightSearchPage.toggleTriptype(tripType);

        step("Select date");
        flightSearchPage.selectDate(departureDate,returnDate);

        step("Search flight");
        flightSearchPage.searchFlight();

        step("Open filter");
        flightSearchResultPage.openFilter();

        step("Filter airline");
        flightSearchResultPage.filterAirline(airline);

        step("Save filter");
        flightSearchResultPage.saveFilter();

        step("Check transit");
        flightSearchResultPage.openTransitFilter();
        flightSearchResultPage.filterTransitType("direct");
        flightSearchResultPage.saveFilter();

        step("Click flight detail chevron");
        flightSearchResultPage.selectFlight();

        step("Open filter");
        flightSearchResultPage.openFilter();

        step("Filter airline");
        flightSearchResultPage.filterAirline(airline);

        step("Save filter");
        flightSearchResultPage.saveFilter();

        step("Check transit");
        flightSearchResultPage.openTransitFilter();
        flightSearchResultPage.filterTransitType("direct");
        flightSearchResultPage.saveFilter();

        step("Click flight detail chevron");
        flightSearchResultPage.selectFlight();

        step("Continue to booking form");
        flightProductReviewPage.continueToBookingForm(tripType);

        step("Get flight date time data");
        List<String> flightDateTime =  flightBookingFormPage.getFlightDateTime(tripType);
        //add year to expected flightdatetime
        String expectedDepartTime = flightDateTime.get(0).replace(" • ", " " + departureDateYear+" • ");
        String expectedDepartArrivalTime = flightDateTime.get(1).replace(" - ", " " + departureDateYear+" • ");
        String expectedReturnTime = flightDateTime.get(2).replace(" • ", " " + returnDateYear+" • ");
        String expectedReturnArrivalTime = flightDateTime.get(3).replace(" - ", " " + departureDateYear+" • ");

        step("Fill contact detail");
        flightBookingFormPage.fillContactDetail(phoneNumber,email,"guest",airline);

        step("Fill passenger detail");
        flightBookingFormPage.fillPassengerDetail(adultPassengers, childPassengers, infantPassengers, "domestic",airline);

        step("Continue to second half booking form");
        flightBookingFormPage.continueToSecondHalf();

        step("Continue to payment");
        flightBookingFormPage.continueToPayment();

        step("Skip full protection");
        flightBookingFormPage.skipFullProtection();

        step("Get booking page passenger list name");
        List<String> expectedPassengerList = flightBookingFormPage.getpassengerNames();

        step("Check order ID");
        String orderID = paymentPage.checkOrderID();

        step("Open payment method");
        paymentPage.openPaymentMethod();

        step("Select BCA Virtual as payment method");
        paymentPage.selectPaymentMethod("BCA Virtual Account");

        step("Get total payment from payment page");
        String totalPayment = paymentPage.getTotalPrice();

        step("Submit payment");
        paymentPage.clickPayButton();

        step("Go to the order list");
        paymentPage.seeOrderList();

        step("Open recent order in order group page");
        orderListPage.seeDetails();

        step("Verify order id is as expected in order group page");
        Assertion.assertThat("Verify order id is as expected", orderDetailPage.getOrderID(),containsString(orderID));

        step("Verify waiting payment component showing in order group page");
        Assertion.assertThat("Verify waiting payment component showing", orderDetailPage.verifyCompletePaymentExpiryTime(), is(true));

        step("verify origin data in order group page");
        String flightDepatureDate = orderDetailPage.getDepartureDate();
        Assertion.assertThat("departflight date should be as expected",flightDepatureDate, equalTo(expectedDepartTime.replaceAll(" • "," ")));
        String flightReturnDate = orderDetailPage.getReturnDate();
        Assertion.assertThat("flightReturnDate date should be as expected",flightReturnDate, equalTo(expectedReturnTime.replaceAll(" • "," ")));
        String departureAirline = orderDetailPage.getOrderGroupDepartureAirline();
        Assertion.assertThat("Verify departure airline", departureAirline, equalTo(airline));
        String returnAirline = orderDetailPage.getOrderGroupReturnAirline();
        Assertion.assertThat("Verify return airline", returnAirline, equalTo(airline));
        String departAirportname = orderDetailPage.geDepartAirportOrderGroup();
        Assertion.assertThat("depart airport details should be same", departAirportname, containsString(originAirport));
        String returnAirportName = orderDetailPage.getReturnAirportOrderGroup();
        Assertion.assertThat("return airport details should be same", returnAirportName, containsString(destinationAirport));

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
        Assertion.assertThat("Departure flight depart time", flightDepatureDateOrderDetail, equalTo(expectedDepartTime.replaceAll("•", "-")));
        Assertion.assertThat("Departure flight arrival time", flightArrivalDateOrderDetail, equalTo(expectedDepartArrivalTime.replaceAll("•", "-")));
        String totalPaymentOrderDetail = orderDetailPage.getTotalPayment();
        Assertion.assertThat("Verify total payment in order detail", CoreFunctions.convertPriceSafely(totalPaymentOrderDetail), equalTo(CoreFunctions.convertPriceSafely(totalPayment)));

        step("Verify flight depart passenger name");
        List<String> passengerList = orderDetailPage.getFlightsPassengerList();
        for(String element:expectedPassengerList){
            Assertion.assertThat("Verify passenger list",passengerList, hasItems(containsString(element)));
        }

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
        Assertion.assertThat("Departure flight depart time", flightDepatureDateOrderDetail, equalTo(expectedReturnTime.replaceAll("•", "-")));
        Assertion.assertThat("Departure flight arrival time", flightArrivalDateOrderDetail, equalTo(expectedReturnArrivalTime.replaceAll("•", "-")));
        totalPaymentOrderDetail = orderDetailPage.getTotalPayment();
        Assertion.assertThat("Verify total payment in order detail", CoreFunctions.convertPriceSafely(totalPaymentOrderDetail), equalTo(CoreFunctions.convertPriceSafely(totalPayment)));

        step("Verify flight return passenger name");
        List<String> passengerListReturn = orderDetailPage.getFlightsPassengerList();
        for(String element: passengerListReturn){
            Assertion.assertThat("Verify passenger list",passengerList, hasItems(containsString(element)));
        }

        step("back to order group page");
        orderDetailPage.clickBack();

        step("Check redirection to complete payment");
        orderDetailPage.completePaymentFromOrderGroupPaymentPage();
        Assertion.assertThat("Number va is visible", paymentPage.isVaNumberShow(), is(true));
    }

    @Author(names = {"Okta", "Akbar"})
    @Module(name = "MyOrder")
    @Tribe(name = "Order")
    @Vertical(name = "PRO")
    @Test(dataProvider="testTrainCheckDetailOrderGuestDataProvider")
    public void testTrainCheckDetailOrderGuest(Map<String, String> param) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TrainSearchPage<WebDriver> trainSearchPage = TrainSearchPage.getInstance(session);
        TrainSearchResultPage<WebDriver> trainSearchResultPage = TrainSearchResultPage.getInstance(session);
        TrainBookingPage<WebDriver> trainBookingPage = TrainBookingPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);

        String tripType = param.get("tripType");
        String originStation = param.get("originStation");
        String destinationStation = param.get("destinationStation");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int infantPassengers = Integer.parseInt(param.get("infant"));

        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);
        String[] departureDateArray = departureDate.split(" ");
        String date = departureDateArray[0];

        String email = param.get("email");
        String paymentType = param.get("paymentType");

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Open app as guest");
        loginPage.loginAsGuest();

        step("Open train page");
        homePage.openVerticalPageDeeplink("Trains","","IDR");

        step("Select origin station");
        String originCity = trainSearchPage.selectOriginGetStationName(originStation);

        step("Select destination station");
        String destinationCity = trainSearchPage.selectDestinationGetStationName(destinationStation);

        step("Select date");
        trainSearchPage.selectDate(departureDate);

        step("Search train");
        trainSearchPage.searchTrain();

        step("Select train");
        trainSearchResultPage.selectTrain();

        step("Fill contact detail");
        trainBookingPage.fillGuestDetail(email);

        step("Fill passenger detail");
        String orderTitle = originCity + " - " + destinationCity;
        trainBookingPage.fillPassengerDetail(adultPassengers, infantPassengers);

        step("Select seat");
        trainBookingPage.selectSeat(tripType, adultPassengers);

        step("Continue to payment");
        trainBookingPage.continueToPayment();

        step("Get order ID");
        String orderIdText = paymentPage.checkOrderID();
        String[] orderIdArray = orderIdText.split(" : ");
        String orderID = orderIdArray[1];

        step("Select ATM as payment method");
        paymentPage.openPaymentMethod();
        paymentPage.selectPaymentMethod(paymentType);

        step("Get total price");
        String totalPrice = paymentPage.getTotalPriceFirstPage();

        step("Submit payment");
        paymentPage.clickPayButton();
        paymentPage.paymentChecker();

        step("Go to order list");
        paymentPage.seeOrderList();

        step("Open train order detail");
        orderListPage.openActiveTicketViaDeeplink(orderID);

        step("Assert train order detail");
        Assertion.assertThat("Is train detail correct", orderDetailPage.isTrainDetailCorrect(originStation, destinationStation, totalPrice), is(true));
        Assertion.assertThat("Is train detail correct",orderDetailPage.isTrainDetailCorrect(originStation, destinationStation, totalPrice), is(true));
        Assertion.assertThat("Verify arrival date, departure date, duration", orderDetailPage.isTrainDepartureArrivalDurationExists(), is(true));
        Assertion.assertThat("Verify Train name and Class", orderDetailPage.isTrainNameAndClassExists(), is(true));
        Assertion.assertThat("Passenger and facilities section exists", orderDetailPage.passengerAndFacilitiesExists(), is(true));
        Assertion.assertThat("refund and reschedule info section should be verified", orderDetailPage.verifyRefundAndRescheduleInfo(), is(true));
    }

    @Author(names = {"Ashish","Zia"})
    @Module(name = "MyOrder")
    @Tribe(name = "Order")
    @Vertical(name = "PRO")
    @Test(dataProvider="checkTTDDetailOrderGuestDataProvider")
    public void checkTTDDetailOrderGuest(Map<String, String> params) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(params);
        String product = CoreFunctions.convertSquareBracketToRound(params.get("product"));
        int ticketQty = Integer.parseInt(params.get("ticket_qty"));
        String cardNumber = params.get("cardNumber");
        String expiry = params.get("expiry");
        String cvv = params.get("cvv");
        String cardName = params.get("cardName");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);
        TtdSelectedPackageDetailPageWeb<?> ttdSelectedPackageDetailPageWeb = TtdSelectedPackageDetailPageWeb.getInstance(session);
        TtdBookingFormPageWeb<?> ttdBookingFormPageWeb = TtdBookingFormPageWeb.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);

        step("Go to home page");
        homePage.gotoHome();

        step("Make user as guest");
        loginPage.loginAsGuest();

        step("Open TTD landing page");
        homePage.openVerticalPageDeeplink("To Do","","IDR");

        step("Search the product");
        ttdLandingPage.clickSearchBar();
        ttdSearchPage.inputSearchKeyword(product);

        step("select the activity");
        ttdSearchPage.selectRecommendedActivity(product);

        step("Select ticket");
        webViewPage.switchToWebViewUrl("to-do");
        ttdProductDetailPageWeb.goToPackagesList();
        ttdProductDetailPageWeb.chooseTicket();

        step("Set total ticket");
        ttdSelectedPackageDetailPageWeb.fillAvailableSection();
        ttdSelectedPackageDetailPageWeb.setTicketQty(ticketQty,0);

        step("Submit package detail");
        ttdSelectedPackageDetailPageWeb.clickBookButton();
        webViewPage.switchToNative();

        step("Completing booking form data");
        webViewPage.switchToWebViewUrl("to-do");
        ttdBookingFormPageWeb.fillContactOrderDetail(false);
        ttdBookingFormPageWeb.fillGuestDetail();
        String guestFullName = ttdBookingFormPageWeb.getGuestName();
        String guestTitle = ttdBookingFormPageWeb.getGuestTitle();
        String issuedPackageName = ttdBookingFormPageWeb.getPackageName();

        step("Submit booking");
        ttdBookingFormPageWeb.continueToPaymentPage();
        webViewPage.switchToNative();

        step("Submit the payment method");
        String orderID = paymentPage.checkOrderID();

        step("Open my order page");
        homePage.gotoHome();
        homePage.openMyOrderPage();

        step("Find the issued order");
        orderListPage.openOrderWaitingPayment(orderID);

        step("verify order id waiting payment page");
        String displayedOrderID = orderDetailPage.getOrderID();
        Assertion.assertThat("Order ID is displayed correctly on waiting payment page", displayedOrderID, is(orderID));

        step("Verify waiting payment component showing in waiting payment page");
        Assertion.assertThat("Verify waiting payment component showing", orderDetailPage.verifyCompletePaymentExpiryTime(), is(true));

        step("Verify important info waiting payment page");
        Assertion.assertThat("Important info section showing on waiting payment page", orderDetailPage.getImportantInfo(), is(notNullValue()));

        step("Verify detail information waiting payment page");
        String ProductName = orderDetailPage.getTTDProductTitle();
        Assertion.assertThat("Product Name is displayed correctly on waiting payment page", ProductName, is(product));

        step("Verify detail reservation waiting payment page");
        String displayedPackageName = orderDetailPage.getTTDPackageName();
        Assertion.assertThat("Package Name is equal on waiting payment page", displayedPackageName,is(issuedPackageName));

        orderDetailPage.openTTDGuestDetail();
        String displayedGuestFullName = orderDetailPage.getTTDGuestFullName();
        Assertion.assertThat("Guest full name is equal on waiting payment page", displayedGuestFullName,is(guestTitle.toUpperCase()+". "+guestFullName));
        orderDetailPage.closeTTDGuestDetail();

        step("Verify insurance section issued order page");
        Assertion.assertThat("Insurance section is displayed issued order page", orderDetailPage.getInsuranceTTD(), is(notNullValue()));

        step("Check redirection to complete payment");
        orderDetailPage.continueToPayment();
        paymentPage.checkOrderID();
        Assertion.assertThat("Redirected to payment page", paymentPage.isInPaymentPage(), is(true));

        step("Open payment method");
        paymentPage.openPaymentMethod();

        step("Select Credit Card as Payment Method");
        paymentPage.selectPaymentMethod("Credit Card");
        paymentPage.fillCCDetailRealIssued(cardNumber, expiry, cvv, cardName);
        paymentPage.clickPayButtonCC();
        paymentPage.verifyCreditCard3ds();
        Assertion.assertThat("Payment Successful and Thank You page is shown", paymentPage.isThankYouPageShown(), is(true));
        orderListPage.openIssuedTicketViaDeeplink(orderID);

        step("verify order id issued order page");
        displayedOrderID = orderDetailPage.getOrderID();
        Assertion.assertThat("Order ID is displayed correctly issued order page", displayedOrderID, is(orderID));

        step("Open e-Ticket from header");
        orderDetailPage.clickEticketFromHeader();
        Assertion.assertThat("Verify pdf content open", orderDetailPage.isPdfContentOpen(), is(true));
        orderDetailPage.clickBackFromPdf();

        step("Verify important info issued order page");
        Assertion.assertThat("Important info section showing issued order page", orderDetailPage.getImportantInfo(), is(notNullValue()));

        step("Verify detail information issued order page");
        ProductName = orderDetailPage.getTTDProductTitle();
        Assertion.assertThat("Product Name is displayed correctly issued order page", ProductName, is(product));

        step("Verify detail reservation issued order page");
        displayedPackageName = orderDetailPage.getTTDPackageName();
        Assertion.assertThat("Package Name is equal issued order page", displayedPackageName,is(issuedPackageName));

        orderDetailPage.openTTDGuestDetail();
        displayedGuestFullName = orderDetailPage.getTTDGuestFullName();
        Assertion.assertThat("Guest full name is equal issued order page", displayedGuestFullName,is(guestTitle.toUpperCase()+". "+guestFullName));
        orderDetailPage.closeTTDGuestDetail();

        step("Verify insurance section issued order page");
        Assertion.assertThat("Insurance section is displayed issued order page", orderDetailPage.getInsuranceTTD(), is(notNullValue()));

        step("Click detail order ");
        orderDetailPage.clickDetailTTD();
        webViewPage.switchToWebViewUrl("to-do");
        Assertion.assertThat("Redirected to correct page", ttdProductDetailPageWeb.getProductName(), is(product));
    }

    @Author(names = {"Akbar"})
    @Module(name = "Multicurrency")
    @Tribe(name = "Order")
    @Vertical(name = "PRO")
    @Test
    public void testMulticurrencyTTDLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> params = getParam("testMulticurrencyTTDLogin");
        BaseTest.paramsThreadLocal.set(params);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        SettingPage<WebDriver> settingPage = SettingPage.getInstance(session);
        TTDPage<WebDriver> ttdPage = TTDPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);

        String loginType = params.get("loginType");
        String username = params.get("username");
        String password = params.get("password");
        String productUrlName = params.get("product");
        String currency = params.get("currency");

        String cardNumber = params.get("cardNumber");
        String expiry = params.get("expiry");
        String cvv = params.get("cvv");
        String cardName = params.get("cardName");

        String[] salutationList = { "MR", "MRS", "MS" };
        String contactFullName = randomName() + " autorder";
        String contactSalutation = salutationList[0];
        String guestFullName = randomName() + " autguest";
        String guestEmailAdress = guestFullName.replace(' ','-') + "@tiket.com";
        String guestSalutation = getRandomObject(salutationList);

        step("Create new ttd issued order");
        String orderId = ttdPage.createWaitingPaymentOrderApi(username, password, currency, productUrlName, false,
                contactFullName, contactSalutation, guestFullName, guestEmailAdress, guestSalutation);
        Assertion.assertThat("TTD Booking via API is successful", orderId, not(blankOrNullString()));

        step("Get data from the booked order");
        String bookedOrderID = ttdPage.issuedOrderData("orderId");
        String bookedProductName = ttdPage.issuedOrderData("productName");

        step("Go to home page");
        homePage.gotoHome();

        step("Make user as guest");
        loginPage.loginAsGuest();

        step("Login using '" + username + "' credential");
        loginPage.loginWith(loginType, username, password);
        String lang = homePage.getLanguageApp();

        if(!lang.equalsIgnoreCase("id")) {
            step("Change Language to 'IDN'");
            homePage.changeLanguageUsingDeeplink("IDN");
        }

        step("Go to order list page");
        homePage.openMyOrderPage();

        step("Find the issued order");
        orderListPage.openOrderCompletePayment(bookedOrderID);

        step("Fill payment details");
        paymentPage.fillCCDetailRealIssued(cardNumber,expiry,cvv,cardName);
        paymentPage.clickPayButton();
        paymentPage.checkCcOtp();

        step("Go to order list");
        paymentPage.seeOrderList();

        step("Go to order detail order id '" + bookedOrderID + "'");
        orderListPage.openActiveTicketViaDeeplink(bookedOrderID);

        step("Verify step");
        Assertion.assertThat("Price detail showing in non-IDR currency", orderDetailPage.checkPriceDetailIsIn(currency), is(true));
        Assertion.assertThat("Share receipt button is showing", orderDetailPage.isShareReceiptButtonDisplayed(), is(true));
        Assertion.assertThat("Receipt button is working", orderDetailPage.isReceiptButtonWork(currency), is(true));
        Assertion.assertThat("Refund button is showing", orderDetailPage.isRefundButtonDisplayed(), is(true));
    }

    @Author(names = {"Akbar"})
    @Module(name = "Multicurrency")
    @Tribe(name = "Order")
    @Vertical(name = "PRO")
    @Test
    public void testMulticurrencyTTDGuest() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> params = getParam("testMulticurrencyTTDGuest");
        BaseTest.paramsThreadLocal.set(params);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);
        TtdSelectedPackageDetailPageWeb<?> ttdSelectedPackageDetailPageWeb = TtdSelectedPackageDetailPageWeb.getInstance(session);
        TtdBookingFormPageWeb<?> ttdBookingFormPageWeb = TtdBookingFormPageWeb.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);

        String product = params.get("product");
        String currency = params.get("currency");

        String cardNumber = params.get("cardNumber");
        String expiry = params.get("expiry");
        String cvv = params.get("cvv");
        String cardName = params.get("cardName");

        step("Go to home page");
        homePage.gotoHome();

        step("Make user as guest");
        loginPage.loginAsGuest();
        String lang = homePage.getLanguageApp();

        if(!lang.equalsIgnoreCase("id")) {
            step("Change Language to 'IDN'");
            homePage.changeLanguageUsingDeeplink("IDN");
        }

        step("Change currency to '"+ currency +"'");
        homePage.changeCurrency(currency,"");

        step("Open TTD landing page");
        homePage.openVerticalPageDeeplink("To Do","","IDR");

        step("Search the product");
        ttdLandingPage.clickSearchBar();
        ttdSearchPage.inputSearchKeyword(product);

        step("select the activity");
        ttdSearchPage.selectRecommendedActivity(product);

        step("Get the product name");
        webViewPage.switchToWebViewUrl("to-do");
        ttdProductDetailPageWeb.getProductName();

        step("Select ticket");
        ttdProductDetailPageWeb.goToPackagesList();
        ttdProductDetailPageWeb.chooseTicket();

        step("Set total ticket");
        ttdSelectedPackageDetailPageWeb.fillAvailableSection();
        ttdSelectedPackageDetailPageWeb.setTicketQty(1,0);

        step("Submit package detail");
        ttdSelectedPackageDetailPageWeb.clickBookButton();
        webViewPage.switchToNative();

        step("Completing booking form data");
        webViewPage.switchToWebViewUrl("to-do");
        ttdBookingFormPageWeb.fillContactOrderDetail(false);
        ttdBookingFormPageWeb.fillGuestDetail();

        step("Submit booking");
        ttdBookingFormPageWeb.continueToPaymentPage();
        webViewPage.switchToNative();

        step("Fill payment details");
        String orderID = paymentPage.checkOrderID();
        paymentPage.fillCCDetailRealIssued(cardNumber,expiry,cvv,cardName);
        paymentPage.clickPayButton();
        paymentPage.checkCcOtp();

        step("Go to order list");
        paymentPage.seeOrderList();

        step("Go to order detail order id '" + orderID + "'");
        orderListPage.openActiveTicketViaDeeplink(orderID);

        step("Verify step");
        Assertion.assertThat("Price detail showing in non-IDR currency", orderDetailPage.checkPriceDetailIsIn(currency), is(true));
        Assertion.assertThat("Share receipt button is showing", orderDetailPage.isShareReceiptButtonDisplayed(), is(true));
        Assertion.assertThat("Receipt button is working", orderDetailPage.isReceiptButtonWork(currency), is(true));
        Assertion.assertThat("Refund button is showing", orderDetailPage.isRefundButtonDisplayed(), is(true));
    }

    @Author(names = {"Dendi"})
    @Module(name = "MyOrder")
    @Tribe(name = "Order")
    @Vertical(name = "PRO")
    @Test
    public void testLoginChangeTrainSeatFromMyOrder(){
        Map<String, String> param = getParam("testLoginChangeTrainSeatFromMyOrder");
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);

        TrainSearchPage<WebDriver> trainSearchPage = TrainSearchPage.getInstance(session);
        TrainSearchResultPage<WebDriver> trainSearchResultPage = TrainSearchResultPage.getInstance(session);
        TrainBookingPage<WebDriver> trainBookingPage = TrainBookingPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);

        String tripType = param.get("tripType");
        String currency = param.get("currency");
        String originStation = param.get("originStation");
        String destinationStation = param.get("destinationStation");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int infantPassengers = Integer.parseInt(param.get("infant"));

        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate,daysToDepartureDate);
        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");

        step("User open app");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink (if needed) and change currency to " + currency);
        homePage.loginWithSpecificAccountViaDeeplink(username, password, currency);

        step("Open train page");
        homePage.openVerticalPageDeeplink("Trains","","IDR");

        step("Select origin station");
        trainSearchPage.skipPopup();
        trainSearchPage.selectOrigin(originStation);

        step("Select destination station");
        trainSearchPage.selectDestination(destinationStation);

        step("Toggle roundTrip");
        trainSearchPage.toggleTripType(tripType);

        step("Select date");
        trainSearchPage.selectDate(departureDate);

        step("Add passenger");
        trainSearchPage.addPassenger(adultPassengers,infantPassengers);

        step("Search train");
        trainSearchPage.searchTrain();

        step("Skip Popup");
        trainSearchPage.skipPopup();

        step("Select departure train");
        trainSearchResultPage.selectTrain();

        step("Fill passenger detail");
        trainBookingPage.fillPassengerDetail(adultPassengers,infantPassengers);

        step("Continue to 2nd half");
        trainBookingPage.continueTo2ndHalf();

        step("Continue to payment");
        trainBookingPage.continueToPayment();

        step("Check order ID");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",!orderID.isBlank(),is(true));

        step("Open payment method");
        paymentPage.openPaymentMethod();

        step("Select virtual account as payment method");
        paymentPage.selectPaymentMethod("BCA Virtual Account");

        step("Submit payment");
        paymentPage.clickPayButton();

        step("Go to the order list");
        paymentPage.seeOrderList();

        step("Open order detail for order "  + orderID);
        orderListPage.openOrderDetailWaitingForPayment(orderID);

        step("Get current seat data");
        String oldSeat = orderDetailPage.getTrainSeatData();

        step("Click change seat on train order detail");
        orderDetailPage.clickChangeTrainSeat();

        step("Select empty seat in train change seat menu");
        orderDetailPage.choosNewTrainSeatWagoon();

        step("Get newly selected seat data");
        String newSeatData = orderDetailPage.getTrainSeatFromConfirmationPage();

        step("Verify change wagoon by swiping left");
        String oldWagoon = oldSeat.split(" ")[0] +" " +oldSeat.split(" ")[1];
        String newWagoon = newSeatData.split(" ")[0] + " " + newSeatData.split(" ")[1];
        Assertion.assertThat("Check user now having different wagoon type in train seat page",oldWagoon, not(equalToIgnoringCase(newWagoon)));

        step("Continue to payment from train change seat page");
        orderDetailPage.continueToPaymentTrainChangeSeat();
        orderID = paymentPage.checkOrderID();

        step("Open payment method");
        paymentPage.openPaymentMethod();

        step("Select virtual account as payment method");
        paymentPage.selectPaymentMethod("BCA Virtual Account");

        step("Submit payment");
        paymentPage.clickPayButton();

        step("Go to the order list");
        paymentPage.seeOrderList();

        step("Reopen order detail");
        orderListPage.openOrderDetailWaitingForPayment(orderID);

        step("Verify seat change");
        String newSeatDataInOrderDetail = orderDetailPage.getTrainSeatData();
        Assertion.assertThat("Verify old seat is different with new seat in order detail",newSeatDataInOrderDetail, not(equalTo(oldSeat)));
        Assertion.assertThat("Verify new seat shown in order detail is the same as seat dat in previous change seat page", newSeatDataInOrderDetail,equalToIgnoringCase(newSeatData));

    }

    @Author(names = {"Dendi"})
    @Module(name = "MyOrder")
    @Tribe(name = "Order")
    @Vertical(name = "PRO")
    @Test
    public void testGuestChangeTrainSeatFromMyOrder(){
        Map<String, String> param = getParam("testGuestChangeTrainSeatFromMyOrder");
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);

        TrainSearchPage<WebDriver> trainSearchPage = TrainSearchPage.getInstance(session);
        TrainSearchResultPage<WebDriver> trainSearchResultPage = TrainSearchResultPage.getInstance(session);
        TrainBookingPage<WebDriver> trainBookingPage = TrainBookingPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);

        String tripType = param.get("tripType");
        String currency = param.get("currency");
        String originStation = param.get("originStation");
        String destinationStation = param.get("destinationStation");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int infantPassengers = Integer.parseInt(param.get("infant"));

        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate,daysToDepartureDate);
        String username = param.get("username");

        step("User open app");
        homePage.gotoHome();

        step("User user app as guest");
        loginPage.loginAsGuest();

        step("Change currency");
        homePage.changeCurrency(currency,"");

        step("Open train page");
        homePage.openVerticalPageDeeplink("Trains","",currency);

        step("Select origin station");
        trainSearchPage.skipPopup();
        trainSearchPage.selectOrigin(originStation);

        step("Select destination station");
        trainSearchPage.selectDestination(destinationStation);

        step("Toggle roundtrip");
        trainSearchPage.toggleTripType(tripType);

        step("Select date");
        trainSearchPage.selectDate(departureDate);

        step("Add passenger");
        trainSearchPage.addPassenger(adultPassengers,infantPassengers);

        step("Search train");
        trainSearchPage.searchTrain();

        step("Skip Popup");
        trainSearchPage.skipPopup();

        step("Select departure train");
        trainSearchResultPage.selectTrain();

        step("Fill contact detail");
        trainBookingPage.fillGuestDetail(username);

        step("Fill passenger detail");
        trainBookingPage.fillPassengerDetail(adultPassengers,infantPassengers);

        step("Continue to 2nd half");
        trainBookingPage.continueTo2ndHalf();

        step("Continue to payment");
        trainBookingPage.continueToPayment();

        step("Check order ID");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",!orderID.isBlank(),is(true));

        step("Open payment method");
        paymentPage.openPaymentMethod();

        step("Select virtual account as payment method");
        paymentPage.selectPaymentMethod("BCA Virtual Account");

        step("Submit payment");
        paymentPage.clickPayButton();

        step("Go to the order list");
        paymentPage.seeOrderList();

        step("Open order detail for order "  + orderID);
        orderListPage.openOrderDetailWaitingForPayment(orderID);

        step("Get current seat data");
        String oldSeat = orderDetailPage.getTrainSeatData();

        step("Click change seat on train order detail");
        orderDetailPage.clickChangeTrainSeat();

        step("Select empty seat in train change seat menu");
        orderDetailPage.choosNewTrainSeatWagoon();

        step("Get newly selected seat data");
        String newSeatData = orderDetailPage.getTrainSeatFromConfirmationPage();

        step("Verify change wagoon by swiping left");
        String oldWagoon = oldSeat.split(" ")[0] +" " +oldSeat.split(" ")[1];
        String newWagoon = newSeatData.split(" ")[0] + " " + newSeatData.split(" ")[1];
        Assertion.assertThat("Check user now having different wagoon type in train seat page",oldWagoon, not(equalToIgnoringCase(newWagoon)));

        step("Continue to payment from train change seat page");
        orderDetailPage.continueToPaymentTrainChangeSeat();

        step("Open payment method");
        paymentPage.openPaymentMethod();

        step("Select virtual account as payment method");
        paymentPage.selectPaymentMethod("BCA Virtual Account");

        step("Submit payment");
        paymentPage.clickPayButton();

        step("Go to the order list");
        paymentPage.seeOrderList();

        step("Reopen order detail");
        orderListPage.openOrderDetailWaitingForPayment(orderID);

        step("Verify seat change");
        String newSeatDataInOrderDetail = orderDetailPage.getTrainSeatData();
        Assertion.assertThat("Verify old seat is different with new seat in order detail",newSeatDataInOrderDetail, not(equalTo(oldSeat)));
        Assertion.assertThat("Verify new seat shown in order detail is the same as seat dat in previous change seat page", newSeatDataInOrderDetail,equalToIgnoringCase(newSeatData));
    }

    @Author(names = {"Akbar"})
    @Module(name = "Multicurrency")
    @Tribe(name = "Order")
    @Vertical(name = "PRO")
    @Test
    public void testMulticurrencyHotelNativeLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testMulticurrencyHotelNativeLogin");
        BaseTest.paramsThreadLocal.set(param);

        HotelApi<?> hotelApi = new HotelApi<>(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        var unm = new Unm<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String currency = param.get("currency");

        String cardNumber = param.get("cardNumber");
        String expiry = param.get("expiry");
        String cvv = param.get("cvv");
        String cardName = param.get("cardName");

        String query = param.get("hotelName");
        String startDate = generateRandomDate(param.get("rangeAddDate"));
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));
        int room = Integer.parseInt(param.get("room"));

        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = getRandomObject(salutationList);

        step("Get api access token");
        String apiAccessToken = unm.getAccessToken(username, password,"B2C");

        String issuedOrderID = "";
        try {
            step("Check if existing order is present");
            MapVerticalWithOrderId activeOrderData = new OrderApi<>(session).getAvailaibleOrder(apiAccessToken);
            MapVerticalWithOrderId.TitleAndOrderId titleAndOrderId = activeOrderData.hotel.stream().filter(hotel -> {
                        boolean eIssued = OrderStatus.WAITING_PAYMENT.getName().equals(hotel.orderStatus);
                        String hotelCurrency = hotel.currency;
                        return eIssued && !"IDR".equalsIgnoreCase(hotelCurrency);
                    })
                    .findFirst()
                    .orElseThrow();
            issuedOrderID = titleAndOrderId.orderId;
            currency = titleAndOrderId.currency;
            step("Found existing multi order hotel waiting payment order with order id: " + issuedOrderID + " and title: " + titleAndOrderId.title);
        } catch (Exception e) {
            step("Failed to find existing hotel waiting payment order: " + e.getMessage());
            step("Create new hotel order waiting for payment via API");
            issuedOrderID = hotelApi.createWaitingPaymentOrderApi(
                    username, password, startDate, query, adult, night, room,
                    orderFullName, orderNameTitle, guestFullName, guestTitle, currency,"id");
        }

        Assertion.assertThat("Got hotel waiting payemnt order ID", issuedOrderID, is(notNullValue()));

        String bookedOrderID = issuedOrderID;

        step("Go to home page");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink (if needed)" + currency);
        homePage.loginWithSpecificAccountViaDeeplink(username, password, currency);
        String lang = homePage.getLanguageApp();

        if(!lang.equalsIgnoreCase("id")) {
            step("Change Language to 'IDN'");
            homePage.changeLanguageUsingDeeplink("IDN");
        }

        step("Go to order list page");
        homePage.openMyOrderPage();

        step("Find the issued order");
        orderListPage.openOrderCompletePayment(bookedOrderID);

        step("Fill payment details");
        paymentPage.fillCCDetailRealIssued(cardNumber,expiry,cvv,cardName);
        paymentPage.clickPayButton();
        paymentPage.checkCcOtp();

        step("Go to order list");
        paymentPage.seeOrderList();

        step("Go to order detail order id '" + bookedOrderID + "'");
        orderListPage.openActiveTicketViaDeeplink(bookedOrderID);
        orderDetailPage.hidePromoBannerIfPresent();

        step("Verify step");
        Assertion.assertThat("Price detail showing in non-IDR currency", orderDetailPage.checkPriceDetailIsIn(currency), is(true));
        Assertion.assertThat("Share receipt button is showing", orderDetailPage.isShareReceiptButtonDisplayed(), is(true));
        Assertion.assertThat("Receipt button is working", orderDetailPage.isReceiptButtonWork(currency), is(true));
        Assertion.assertThat("Refund button is showing", orderDetailPage.isRefundButtonDisplayed(), is(true));
    }

    @Author(names = {"Akbar"})
    @Module(name = "Multicurrency")
    @Tribe(name = "Order")
    @Vertical(name = "PRO")
    @Test
    public void testMulticurrencyHotelNativeGuest() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> params = getParam("testMulticurrencyHotelNativeGuest");
        BaseTest.paramsThreadLocal.set(params);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        SettingPage<WebDriver> settingPage = SettingPage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);

        String currency = params.get("currency");

        String hotelNameSearch = params.get("hotelName");
        int totalRoom = Integer.parseInt(params.get("totalRoom"));
        int totalGuest = Integer.parseInt(params.get("totalGuest"));

        String cardNumber = params.get("cardNumber");
        String expiry = params.get("expiry");
        String cvv = params.get("cvv");
        String cardName = params.get("cardName");

        step("Go to home page");
        homePage.gotoHome();

        step("Make user as guest");
        loginPage.loginAsGuest();
        String lang = homePage.getLanguageApp();

        if(!lang.equalsIgnoreCase("id")) {
            step("Change Language to 'IDN'");
            homePage.changeLanguageUsingDeeplink("IDN");
        }

        step("Change currency to '"+ currency +"'");
        homePage.changeCurrency(currency,"");

        step("Open hotel landing page");
        homePage.openVerticalPageDeeplink("Hotel","",currency);

        step("search stay location: " + hotelNameSearch);
        landingPage.searchStayLocation(hotelNameSearch);

        step("set total room: " + totalRoom + " and total guest: " + totalGuest);
        landingPage.clickRoomAndGuestButton();
        landingPage.setTotalRoom(totalRoom);
        landingPage.setTotalGuest(totalGuest);
        landingPage.clickDone();

        step("submit search");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        step("Go to the room list");
        String hotelName = detailPage.getHotelNameDetail();
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();

        step("Select room from the room list");
        String roomName = roomListPage.getTheFirstHotelRoomName();
        roomListPage.selectMultiRoom(totalRoom, totalGuest);

        step("Fill the data on the booking form and submit it");
        bookingFormPage.fillContactOrderDetail();
        bookingFormPage.fillGuestDetail(false);
        bookingFormPage.getGuestNameList();
        bookingFormPage.continueToPaymentPage();
        bookingFormPage.PriceChangesPopUp();

        step("Fill payment details");
        String orderID = paymentPage.checkOrderID();
        paymentPage.fillCCDetailRealIssued(cardNumber,expiry,cvv,cardName);
        paymentPage.clickPayButton();
        paymentPage.checkCcOtp();

        step("Go to order list");
        paymentPage.seeOrderList();

        step("Go to order detail order id '" + orderID + "'");
        orderListPage.openActiveTicketViaDeeplink(orderID);

        step("Verify step");
        Assertion.assertThat("Price detail showing in non-IDR currency", orderDetailPage.checkPriceDetailIsIn(currency), is(true));
        Assertion.assertThat("Share receipt button is showing", orderDetailPage.isShareReceiptButtonDisplayed(), is(true));
        Assertion.assertThat("Receipt button is working", orderDetailPage.isReceiptButtonWork(currency), is(true));
        Assertion.assertThat("Refund button is showing", orderDetailPage.isRefundButtonDisplayed(), is(true));
    }

    @Author(names = {"Akbar"})
    @Module(name = "Multicurrency")
    @Tribe(name = "Order")
    @Vertical(name = "PRO")
    @Test
    public void testMulticurrencyCheckPriceDetailLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testMulticurrencyCheckPriceDetailLogin");
        BaseTest.paramsThreadLocal.set(param);

        HotelPage<WebDriver> hotelPage = HotelPage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        SettingPage<WebDriver> settingPage = SettingPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String currency = param.get("currency");

        String cardNumber = param.get("cardNumber");
        String expiry = param.get("expiry");
        String cvv = param.get("cvv");
        String cardName = param.get("cardName");

        String query = param.get("hotelName");
        String startDate = generateRandomDate(param.get("rangeAddDate"));
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));
        int room = Integer.parseInt(param.get("room"));

        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = getRandomObject(salutationList);

        step("Create new hotel issued order");
        String result = hotelApi.createWaitingPaymentOrderApi(
                username, password, startDate, query, adult, night, room,
                orderFullName, orderNameTitle, guestFullName, guestTitle, currency,"id");
        Assertion.assertThat("Hotel Booking via API is successful", result, is(notNullValue()));

        String bookedOrderID = String.valueOf(hotelApi.getOrderId());
        String bookedRoomName = hotelApi.getHotelRoomName();

        step("Go to home page");
        homePage.gotoHome();

        step("Make user as guest");
        loginPage.loginAsGuest();

        step("Login using '" + username + "' credential");
        loginPage.loginWith(loginType, username, password);
        String lang = homePage.getLanguageApp();

        if(!lang.equalsIgnoreCase("id")) {
            step("Change Language to 'IDN'");
            homePage.changeLanguageUsingDeeplink("IDN");
        }

        step("Go to order list page");
        homePage.openMyOrderPage();

        step("Find the issued order");
        orderListPage.openOrderCompletePayment(bookedOrderID);

        step("Fill payment details");
        paymentPage.fillCCDetailRealIssued(cardNumber,expiry,cvv,cardName);
        paymentPage.clickPayButton();
        paymentPage.checkCcOtp();

        step("Go to order list");
        paymentPage.seeOrderList();

        step("Go to order detail order id '" + bookedOrderID + "'");
        orderListPage.openActiveTicketViaDeeplink(bookedOrderID);

        step("Verify step");
        Assertion.assertThat("Price detail showing in non-IDR currency", orderDetailPage.checkPriceDetailIsIn(currency), is(true));
        Assertion.assertThat("Share receipt button is showing", orderDetailPage.isShareReceiptButtonDisplayed(), is(true));
        Assertion.assertThat("Receipt button is working", orderDetailPage.isReceiptButtonWork(currency), is(true));
    }

    @Author(names = {"Akbar"})
    @Module(name = "Multicurrency")
    @Tribe(name = "Order")
    @Vertical(name = "PRO")
    @Test
    public void testMulticurrencyCheckPriceDetailGuest() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> params = getParam("testMulticurrencyCheckPriceDetailGuest");
        BaseTest.paramsThreadLocal.set(params);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        SettingPage<WebDriver> settingPage = SettingPage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);

        String currency = params.get("currency");

        String hotelNameSearch = params.get("hotelName");
        int totalRoom = Integer.parseInt(params.get("totalRoom"));
        int totalGuest = Integer.parseInt(params.get("totalGuest"));

        String cardNumber = params.get("cardNumber");
        String expiry = params.get("expiry");
        String cvv = params.get("cvv");
        String cardName = params.get("cardName");

        step("Go to home page");
        homePage.gotoHome();

        step("Make user as guest");
        loginPage.loginAsGuest();
        String lang = homePage.getLanguageApp();

        if(!lang.equalsIgnoreCase("id")) {
            step("Change Language to 'IDN'");
            homePage.changeLanguageUsingDeeplink("IDN");
        }

        step("Change currency to '"+ currency +"'");
        homePage.changeCurrency(currency,"");

        step("Open hotel landing page");
        homePage.openVerticalPageDeeplink("Hotel","",currency);

        step("search stay location: " + hotelNameSearch);
        landingPage.searchStayLocation(hotelNameSearch);

        step("set total room: " + totalRoom + " and total guest: " + totalGuest);
        landingPage.clickRoomAndGuestButton();
        landingPage.setTotalRoom(totalRoom);
        landingPage.setTotalGuest(totalGuest);
        landingPage.clickDone();

        step("submit search");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        step("Go to the room list");
        String hotelName = detailPage.getHotelNameDetail();
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();

        step("Select room from the room list");
        String roomName = roomListPage.getTheFirstHotelRoomName();
        roomListPage.clickBookRoom();

        step("Fill the data on the booking form and submit it");
        bookingFormPage.fillContactOrderDetail();
        bookingFormPage.fillGuestDetail(false);
        bookingFormPage.getGuestNameList();
        bookingFormPage.continueToPaymentPage();
        bookingFormPage.PriceChangesPopUp();

        step("Fill payment details");
        String orderID = paymentPage.checkOrderID();
        paymentPage.fillCCDetailRealIssued(cardNumber,expiry,cvv,cardName);
        paymentPage.clickPayButton();
        paymentPage.checkCcOtp();

        step("Go to order list");
        paymentPage.seeOrderList();

        step("Go to order detail order id '" + orderID + "'");
        orderListPage.openActiveTicketViaDeeplink(orderID);

        step("Verify step");
        Assertion.assertThat("Price detail showing in non-IDR currency", orderDetailPage.checkPriceDetailIsIn(currency), is(true));
        Assertion.assertThat("Share receipt button is showing", orderDetailPage.isShareReceiptButtonDisplayed(), is(true));
        Assertion.assertThat("Receipt button is working", orderDetailPage.isReceiptButtonWork(currency), is(true));
    }

    @Author(names = {"Dendi"})
    @Module(name = "CrossSell")
    @Tribe(name = "Order")
    @Vertical(name = "PRO")
    @Test
    public void testLoginOrderCrossSell() throws InterruptedException, ExecutionException{
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> params = getParam("testLoginOrderCrossSell");
        BaseTest.paramsThreadLocal.set(params);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        Unm<WebDriver> unm = new Unm<>(session);
        CarTransferApi<?> carTransferApi = new CarTransferApi<>(session);

        String username = params.get("username");
        String password = params.get("password");

        String origin = params.get("origin");
        String destination = params.get("destination");
        String type = params.get("type");
        String pickupTime = params.get("pickupTime");
        int pax = Integer.parseInt(params.get("pax"));
        String rangeAddDate = params.get("rangeAddDate");

        String cardNumber = params.get("cardNumber");
        String cardHolderName = params.get("cardHolderName");
        String expiry = params.get("expiry");
        String[] expirySplit = expiry.split("/");
        String cardExpiredMonth = expirySplit[0];
        String cardExpiredYear = expirySplit[1];
        String cardCvvNumber = params.get("cardCvvNumber");
        String adminName = params.get("adminName");
        String adminPhone = params.get("adminPhone");

        step("Get api access token");
        String apiAccessToken = unm.getAccessToken(username, password,"B2C");

        step("Check if existing order is present");
        MapVerticalWithOrderId activeOrderData = new OrderApi<>(session).getAvailaibleOrder(apiAccessToken);
        if(activeOrderData.isEmpty){
            step("Create new airport transfer issued order");
            String orderId  = carTransferApi.createIssuedOrderApi(username, password, origin,destination,type,rangeAddDate,pickupTime,pax,cardNumber,cardHolderName,cardExpiredMonth,cardExpiredYear,cardCvvNumber,adminName,adminPhone);
            Assertion.assertThat("Car transfer booking via API is successful", orderId, not(blankOrNullString()));
            activeOrderData = new OrderApi<>(session).getAvailaibleOrder(apiAccessToken);
        }

        String orderID = activeOrderData.firstOrder.orderId;
        String orderHash = activeOrderData.firstOrder.orderHash;

        step("Open app");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink (if needed)");
        homePage.loginWithSpecificAccountViaDeeplink(username, password, "IDR");
        String language = homePage.getLanguageApp();

        step("Open order detail");
        orderListPage.openActiveTicketViaDeeplink(orderID);
        orderDetailPage.hidePromoBannerIfPresent();

        step("Get expected url of add product section in order detail page");
        String expectedSectionTitle = orderDetailPage.getPageModuleSectionTitle(apiAccessToken,orderID,orderHash,language);
        List<String> expectedUrl = orderDetailPage.getPageModuleSectionUrl(apiAccessToken,orderID,orderHash,language);

        step("Verify add product section in order detail page");
        log("Expected section title: " + expectedSectionTitle);
        log("Expected section url: " + expectedUrl);
        Assertion.assertThat("Verify add product section",  orderDetailPage.verifyCrossSellRedirection(expectedSectionTitle,expectedUrl), is(true));
    }

    @Author(names = {"Dendi"})
    @Module(name = "CrossSell")
    @Tribe(name = "Order")
    @Vertical(name = "PRO")
    @Test
    public void testGuestOrderCrossSell() throws InterruptedException, ExecutionException{
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> params = getParam("testGuestOrderCrossSell");
        BaseTest.paramsThreadLocal.set(params);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        CarTransferSearchPage<WebDriver> carTransferSearchPage = CarTransferSearchPage.getInstance(session);
        CarTransferSearchResultPage<WebDriver> carTransferSearchResultPage = CarTransferSearchResultPage.getInstance(session);
        CarTransferBookingPage<WebDriver> carTransferBookingPage = CarTransferBookingPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);

        String username = params.get("username");
        String cardNumber = params.get("cardNumber");
        String cardHolderName = params.get("cardHolderName");
        String expiry = params.get("expiry");
        String cardCvvNumber = params.get("cvv");
        String orderDetailSectionTitleID = params.get("orderDetailSectionTitleID");
        String orderDetailSectionTitleEN = params.get("orderDetailSectionTitleEN");
        String airport = params.get("airport");
        String destination = params.get("destination");
        String currency = params.get("currency");
        String transferType = params.get("transferType");
        String payment_method = params.get("paymentMethod");
        String phoneNumber =params.get("phoneNumber");


        step("Get api access token");
        String apiAccessToken = accountPage.getToken();

        step("Open app");
        homePage.gotoHome();

        step("login");
        loginPage.loginAsGuest();
        String language = homePage.getLanguageApp();
        String expectedSectionTitle = language.equalsIgnoreCase("id") ? orderDetailSectionTitleID : orderDetailSectionTitleEN;

        step("Change currency");
        homePage.changeCurrency(currency,"");

        step("User open car transfer vertical page");
        homePage.openVerticalPageDeeplink("Car Transfers","",currency);
        webViewPage.switchToWebViewUrl("airport-transfer");
        carTransferSearchPage.closePopups();

        step("User choose car transfer out");
        carTransferSearchPage.chooseTransferType(transferType);

        step("user input origin and destination");
        carTransferSearchPage.choosePickup(airport, destination);

        step("user search for car");
        carTransferSearchPage.searchCar();

        if(Configs.PLATFORM == Platform.IOS){
            webViewPage.switchToNative();
            carTransferSearchPage.allowLocationAccess();
            webViewPage.switchToWebViewUrl("airport-transfer");
        }

        step("user select car and vendor");
        carTransferSearchResultPage.selectCarAndVendor();

        step("user continue to booking page");
        carTransferSearchResultPage.continueToBookingPage();

        step("user continue to payment list page");
        carTransferBookingPage.fillDetailOrder(username,phoneNumber);

        step("user continue to payment list page");
        carTransferBookingPage.continueToPayment();
        webViewPage.switchToNative();
        String expectedOrderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",!expectedOrderID.isEmpty(),is(true));

        step("user choose pay with cc");
        paymentPage.openPaymentMethod();
        paymentPage.selectPaymentMethod(payment_method);

        step("user fill cc detail");
        paymentPage.fillCCDetailRealIssued(cardNumber, expiry, cardCvvNumber, cardHolderName);
        paymentPage.clickPayButtonCC();
        paymentPage.verifyCreditCard3ds();

        step("Open the order list and go to order detail");
        paymentPage.seeOrderList();
        String orderDetailName = language.equals("en")? "From":"Dari" + " "+(transferType.equals("transfer_out") ? airport: destination);

        step("Verify cross sell component in order list page");
        Assertion.assertThat("Verify cross sell component in order list page", orderListPage.verifyCrossSellInOrderListPage(), is(true));

        step("Verify cross sell see all btn openin a srp page in order list page");
        Assertion.assertThat("Verify cross sell see all btn openin a srp page in order list page", orderListPage.verifyCrossSellSeeAllBtn(), is(true));

        step("Open train order detail");
        orderListPage.openOrderDetail(orderDetailName);

        step("Get expected url of add product section in order detail page");
        String orderHash = orderDetailPage.getTrainInternalOrderData(expectedOrderID).data.orderHash;
        List<String> expectedUrl = orderDetailPage.getPageModuleSectionUrl(apiAccessToken,expectedOrderID,orderHash,language);

        step("Verify add product section in order detail page");
        Assertion.assertThat("Verify add product section",  orderDetailPage.verifyCrossSellRedirection(expectedSectionTitle,expectedUrl), is(true));
    }

    @Author(names = {"Akbar","Zia"})
    @Module(name = "Multicurrency")
    @Tribe(name = "Order")
    @Vertical(name = "PRO")
    @Test
    public void testMulticurrencyCheckReceiptLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testMulticurrencyCheckReceiptLogin");
        BaseTest.paramsThreadLocal.set(param);

        HotelPage<WebDriver> hotelPage = HotelPage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        SettingPage<WebDriver> settingPage = SettingPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String currency = param.get("currency");

        String cardNumber = param.get("cardNumber");
        String expiry = param.get("expiry");
        String cvv = param.get("cvv");
        String cardName = param.get("cardName");

        String query = param.get("hotelName");
        String startDate = generateRandomDate(param.get("rangeAddDate"));
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));
        int room = Integer.parseInt(param.get("room"));

        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = getRandomObject(salutationList);

        step("Go to home page");
        homePage.gotoHome();

        step("Make user as guest");
        loginPage.loginAsGuest();

        step("Login using '" + username + "' credential");
        loginPage.loginWith(loginType, username, password);

        step("Create new hotel issued order");
        String result = hotelApi.createWaitingPaymentOrderApi(
                username, password, startDate, query, adult, night, room,
                orderFullName, orderNameTitle, guestFullName, guestTitle, currency,"id");
        Assertion.assertThat("Hotel Booking via API is successful", result, is(notNullValue()));

        String bookedOrderID = String.valueOf(hotelApi.getOrderId());

        step("Change language to id");
        String lang = homePage.getLanguageApp();
        if(!lang.equalsIgnoreCase("id")) {
            step("Change Language to 'IDN'");
            homePage.changeLanguageUsingDeeplink("IDN");
        }

        step("Go to order list page");
        homePage.openMyOrderPage();

        step("Find the issued order");
        orderListPage.openOrderCompletePayment(bookedOrderID);

        step("Fill payment details");
        paymentPage.fillCCDetailRealIssued(cardNumber,expiry,cvv,cardName);
        paymentPage.clickPayButtonCC();
        paymentPage.verifyCreditCard3ds();
        Assertion.assertThat("Payment Successful and Thank You page is shown", paymentPage.isThankYouPageShown(), is(true));

        step("Go to order list");
        paymentPage.seeOrderList();

        step("Go to order detail order id '" + bookedOrderID + "'");
        orderListPage.openIssuedTicketViaDeeplink(bookedOrderID);

        step("Verify price detail in order detail page");
        Assertion.assertThat("Price detail showing in non-IDR currency", orderDetailPage.checkPriceDetailIsIn(currency), is(true));
        Assertion.assertThat("Share receipt button is showing", orderDetailPage.isShareReceiptButtonDisplayed(), is(true));
        String totalPrice = orderDetailPage.getTotalPrice();
        Assertion.assertThat("Price is having the correct format ID "+totalPrice, CoreFunctions.verifySeparatorRegex(Collections.singletonList(totalPrice),"id",currency,2), is(true));

        if(Configs.PLATFORM == Platform.ANDROID) {
            step("Click receipt tab");
            orderDetailPage.openReceipt();

            step("Verify see receipt PDF");
            String pdfContent = orderDetailPage.getPDFContentFile();
            orderDetailPage.clickBackFromPdf();
            Assertion.assertThat("Receipt PDF is containing the non-IDR currency", pdfContent, containsString(currency));
            Assertion.assertThat("Receipt PDF is containing the same total price with order detail page", pdfContent, containsString(totalPrice));

        } else if(Configs.PLATFORM == Platform.IOS) {
            step("Verify receipt share button");
            Assertion.assertThat("Receipt button is working", orderDetailPage.isReceiptButtonWork(currency), is(true));
        }

        step("Go to home page");
        homePage.gotoHome();

        step("Change Language to 'EN'");
        homePage.openAccount();
        accountPage.selectLanguage("ENG");
        settingPage.backToHome();

        step("Create new hotel issued order");
        String result2 = hotelApi.createWaitingPaymentOrderApi(
                username, password, startDate, query, adult, night, room,
                orderFullName, orderNameTitle, guestFullName, guestTitle, currency,"en");
        Assertion.assertThat("Hotel Booking via API is successful", result2, is(notNullValue()));

        String bookedOrderID2 = String.valueOf(hotelApi.getOrderId());

        step("Go to order list page");
        homePage.openMyOrderPage();

        step("Find the issued order");
        orderListPage.openOrderCompletePayment(bookedOrderID2);

        step("Fill payment details");
        paymentPage.fillCCDetailRealIssued(cardNumber,expiry,cvv,cardName);
        paymentPage.clickPayButtonCC();
        paymentPage.verifyCreditCard3ds();
        Assertion.assertThat("Payment Successful and Thank You page is shown", paymentPage.isThankYouPageShown(), is(true));

        step("Go to order list");
        paymentPage.seeOrderList();

        step("Go to order detail order id '" + bookedOrderID2 + "'");
        orderListPage.openIssuedTicketViaDeeplink(bookedOrderID2);

        step("Verify price detail in order detail page");
        Assertion.assertThat("Price detail showing in non-IDR currency", orderDetailPage.checkPriceDetailIsIn(currency), is(true));
        Assertion.assertThat("Share receipt button is showing", orderDetailPage.isShareReceiptButtonDisplayed(), is(true));
        totalPrice = orderDetailPage.getTotalPrice();
        Assertion.assertThat("Price is having the correct format EN "+totalPrice, CoreFunctions.verifySeparatorRegex(Collections.singletonList(totalPrice),"en",currency,2), is(true));

        if(Configs.PLATFORM == Platform.ANDROID) {
            step("Click receipt tab");
            orderDetailPage.openReceipt();

            step("Verify see receipt PDF");
            String pdfContent = orderDetailPage.getPDFContentFile();
            orderDetailPage.clickBackFromPdf();
            Assertion.assertThat("Receipt PDF is containing the non-IDR currency", pdfContent, containsString(currency));
            Assertion.assertThat("Receipt PDF is containing the same total price with order detail page", pdfContent, containsString(totalPrice));

        } else if(Configs.PLATFORM == Platform.IOS) {
            step("Verify receipt share button");
            Assertion.assertThat("Receipt button is working", orderDetailPage.isReceiptButtonWork(currency), is(true));
        }
    }

    @Author(names = {"Akbar"})
    @Module(name = "Multicurrency")
    @Tribe(name = "Order")
    @Vertical(name = "PRO")
    @Test
    public void testMulticurrencyCheckReceiptGuest() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> params = getParam("testMulticurrencyCheckReceiptGuest");
        BaseTest.paramsThreadLocal.set(params);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        SettingPage<WebDriver> settingPage = SettingPage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);

        String currency = params.get("currency");

        String hotelNameSearch = params.get("hotelName");

        String cardNumber = params.get("cardNumber");
        String expiry = params.get("expiry");
        String cvv = params.get("cvv");
        String cardName = params.get("cardName");

        step("Go to home page");
        homePage.gotoHome();

        step("Make user as guest");
        loginPage.loginAsGuest();
        String lang = homePage.getLanguageApp();

        if(!lang.equalsIgnoreCase("id")) {
            step("Change Language to 'IDN'");
            homePage.changeLanguageUsingDeeplink("IDN");
        }

        step("Change currency to '"+ currency +"'");
        homePage.changeCurrency(currency,"");

        step("Open hotel landing page");
        homePage.openVerticalPageDeeplink("Hotel","",currency);

        step("search stay location: " + hotelNameSearch);
        landingPage.searchStayLocation(hotelNameSearch);

        step("set total room: 1");
        landingPage.clickRoomAndGuestButton();
        landingPage.setTotalRoom(1);
        landingPage.clickDone();

        step("submit search");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        step("Go to the room list");
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();

        step("Select room from the room list");
        roomListPage.getTheFirstHotelRoomPrice();
        roomListPage.clickBookRoom();

        step("Fill the data on the booking form and submit it");
        bookingFormPage.fillContactOrderDetail();
        bookingFormPage.fillGuestDetail(false);
        bookingFormPage.continueToPaymentPage();
        bookingFormPage.PriceChangesPopUp();

        step("Fill payment details");
        String orderID = paymentPage.checkOrderID();
        paymentPage.fillCCDetailRealIssued(cardNumber,expiry,cvv,cardName);
        paymentPage.clickPayButtonCC();
        paymentPage.verifyCreditCard3ds();

        step("Go to order list");
        Assertion.assertThat("Payment Successful and Thank You page is shown", paymentPage.isThankYouPageShown(), is(true));
        paymentPage.seeOrderList();

        step("Go to order detail order id '" + orderID);
        orderListPage.openIssuedTicketViaDeeplink(orderID);

        step("Verify price detail in order detail page");
        Assertion.assertThat("Price detail showing in non-IDR currency", orderDetailPage.checkPriceDetailIsIn(currency), is(true));
        Assertion.assertThat("Share receipt button is showing", orderDetailPage.isShareReceiptButtonDisplayed(), is(true));
        String totalPrice = orderDetailPage.getTotalPrice();
        Assertion.assertThat("Price is having the correct format ID "+totalPrice, CoreFunctions.verifySeparatorRegex(Collections.singletonList(totalPrice),"id",currency,2), is(true));

        if(Configs.PLATFORM == Platform.ANDROID) {
            step("Click receipt tab");
            orderDetailPage.openReceipt();

            step("Verify see receipt PDF");
            String pdfContent = orderDetailPage.getPDFContentFile();
            orderDetailPage.clickBackFromPdf();
            Assertion.assertThat("Receipt PDF is containing the non-IDR currency", pdfContent, containsString(currency));
            Assertion.assertThat("Receipt PDF is containing the same total price with order detail page", pdfContent, containsString(totalPrice));

        } else if(Configs.PLATFORM == Platform.IOS) {
            step("Verify receipt share button");
            Assertion.assertThat("Receipt button is working", orderDetailPage.isReceiptButtonWork(currency), is(true));
        }

        step("Go to home page");
        homePage.gotoHome();

        step("Change Language to 'ENG'");
        homePage.openAccount();
        accountPage.selectLanguage("ENG");
        settingPage.backToHome();

        step("Open hotel landing page");
        homePage.openVerticalPageDeeplink("Hotel","",currency);

        step("search stay location: " + hotelNameSearch);
        landingPage.searchStayLocation(hotelNameSearch);

        step("set total room: 1");
        landingPage.clickRoomAndGuestButton();
        landingPage.setTotalRoom(1);
        landingPage.clickDone();

        step("submit search");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        step("Go to the room list");
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();

        step("Select room from the room list");
        roomListPage.getTheFirstHotelRoomPrice();
        roomListPage.clickBookRoom();

        step("Fill the data on the booking form and submit it");
        bookingFormPage.fillContactOrderDetail();
        bookingFormPage.fillGuestDetail(false);
        bookingFormPage.continueToPaymentPage();
        bookingFormPage.PriceChangesPopUp();

        step("Fill payment details");
        orderID = paymentPage.checkOrderID();
        paymentPage.fillCCDetailRealIssued(cardNumber,expiry,cvv,cardName);
        paymentPage.clickPayButtonCC();
        paymentPage.verifyCreditCard3ds();

        step("Go to order list");
        Assertion.assertThat("Payment Successful and Thank You page is shown", paymentPage.isThankYouPageShown(), is(true));
        paymentPage.seeOrderList();

        step("Go to order detail order id '" + orderID);
        orderListPage.openIssuedTicketViaDeeplink(orderID);

        step("Verify price detail in order detail page");
        Assertion.assertThat("Price detail showing in non-IDR currency", orderDetailPage.checkPriceDetailIsIn(currency), is(true));
        Assertion.assertThat("Share receipt button is showing", orderDetailPage.isShareReceiptButtonDisplayed(), is(true));
        totalPrice = orderDetailPage.getTotalPrice();
        Assertion.assertThat("Price is having the correct format EN "+totalPrice, CoreFunctions.verifySeparatorRegex(Collections.singletonList(totalPrice),"en",currency,2), is(true));

        if(Configs.PLATFORM == Platform.ANDROID) {
            step("Click receipt tab");
            orderDetailPage.openReceipt();

            step("Verify see receipt PDF");
            String pdfContent = orderDetailPage.getPDFContentFile();
            orderDetailPage.clickBackFromPdf();
            Assertion.assertThat("Receipt PDF is containing the non-IDR currency", pdfContent, containsString(currency));
            Assertion.assertThat("Receipt PDF is containing the same total price with order detail page", pdfContent, containsString(totalPrice));

        } else if(Configs.PLATFORM == Platform.IOS) {
            step("Verify receipt share button");
            Assertion.assertThat("Receipt button is working", orderDetailPage.isReceiptButtonWork(currency), is(true));
        }
    }

    @Author(names = {"Dendi"})
    @Module(name = "ShareReceipt")
    @Tribe(name = "Order")
    @Vertical(name = "PRO")
    @Test
    public void testCheckShareReceiptGuest() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> params = getParam("testCheckShareReceiptGuest");
        BaseTest.paramsThreadLocal.set(params);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        SettingPage<WebDriver> settingPage = SettingPage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);

        String currency = "IDR";

        String hotelNameSearch = params.get("hotelName");
        int totalRoom = Integer.parseInt(params.get("totalRoom"));
        int totalGuest = Integer.parseInt(params.get("totalGuest"));

        String cardNumber = params.get("cardNumber");
        String expiry = params.get("expiry");
        String cvv = params.get("cvv");
        String cardName = params.get("cardName");
        String payment_method = "Credit Card";

        step("Go to home page");
        homePage.gotoHome();

        step("Make user as guest");
        loginPage.loginAsGuest();
        String lang = homePage.getLanguageApp();

        if(!lang.equalsIgnoreCase("id")) {
            step("Change Language to 'IDN'");
            homePage.changeLanguageUsingDeeplink("IDN");
        }

        step("Change currency to '"+ currency +"'");
        homePage.changeCurrency(currency,"");

        step("Open hotel landing page");
        homePage.openVerticalPageDeeplink("Hotel","",currency);

        step("search stay location: " + hotelNameSearch);
        landingPage.searchStayLocation(hotelNameSearch);

        step("set total room: " + totalRoom + " and total guest: " + totalGuest);
        landingPage.clickRoomAndGuestButton();
        landingPage.setTotalRoom(totalRoom);
        landingPage.setTotalGuest(totalGuest);
        landingPage.clickDone();

        step("submit search");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        step("Go to the room list");
        String hotelName = detailPage.getHotelNameDetail();
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();

        step("Select room from the room list");
        String roomName = roomListPage.getTheFirstHotelRoomName();
        roomListPage.selectMultiRoom(totalRoom, totalGuest);

        step("Fill the data on the booking form and submit it");
        bookingFormPage.fillContactOrderDetail();
        bookingFormPage.fillGuestDetail(false);
        bookingFormPage.getGuestNameList();
        bookingFormPage.continueToPaymentPage();
        bookingFormPage.PriceChangesPopUp();

        step("Get orderID");
        String orderID = paymentPage.checkOrderID();

        step("user choose pay with cc");
        paymentPage.openPaymentMethod();
        paymentPage.selectPaymentMethod(payment_method);
        paymentPage.fillCCDetailRealIssued(cardNumber,expiry,cvv,cardName);
        paymentPage.clickPayButton();
        paymentPage.checkCcOtp();

        step("Go to order list");
        paymentPage.seeOrderList();

        step("Go to order detail order id '" + orderID + "'");
        orderListPage.openActiveTicketViaDeeplink(orderID);

        step("Verify step 1");
        Assertion.assertThat("Price detail showing in IDR currency", orderDetailPage.checkPriceDetailIsIn(currency), is(true));
        Assertion.assertThat("Share receipt button is showing", orderDetailPage.isShareReceiptButtonDisplayed(), is(true));

        step("Open Receipt");
        String totalPrice = orderDetailPage.getTotalPrice();


        orderDetailPage.shareReceipt();
        boolean isFileNameCorrect = orderDetailPage.isShareFileNameCorrect("Hotel",orderID,lang);

        step("Verify step 2");
        Assertion.assertThat("Price is having the correct format", CoreFunctions.isStringNumberInEn(totalPrice), is(false));
        Assertion.assertThat("Verify file title has same name", isFileNameCorrect, is(true));
    }

    @Author(names = {"Dendi"})
    @Module(name = "ShareReceipt")
    @Tribe(name = "Order")
    @Vertical(name = "PRO")
    @Test
    public void testCheckShareReceiptLogin() throws ExecutionException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testCheckShareReceiptLogin");
        BaseTest.paramsThreadLocal.set(param);

        HotelApi<?> hotelApi = new HotelApi<>(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        Unm<WebDriver> unm = new Unm<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String currency = "IDR";

        String query = param.get("hotelName");
        String startDate = generateRandomDate(param.get("rangeAddDate"));
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));
        int room = Integer.parseInt(param.get("room"));

        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = getRandomObject(salutationList);

        step("Get api access token");
        String apiAccessToken = unm.getAccessToken(username, password,"B2C");

        String bookedOrderID;
        try {
            step("Check if existing order is present");
            MapVerticalWithOrderId activeOrderData = new OrderApi<>(session).getAvailaibleOrder(apiAccessToken);
            bookedOrderID = activeOrderData.hotel.stream().filter(order -> OrderStatus.ETICKET_ISSUED.getName().equals(order.orderStatus)).findFirst().orElseThrow().orderId;
        } catch (Exception e) {
            log("Failed to find existing hotel order" + e.getMessage());
            step("Create new hotel issued order");
            hotelApi.createIssuedOrderApi(
                    username, password, startDate, query, adult, night, room,
                    orderFullName, orderNameTitle, guestFullName, guestTitle,"IDR");
            bookedOrderID = String.valueOf(hotelApi.getOrderId());
        }

        Assertion.assertThat("Got booked order ID", bookedOrderID, is(notNullValue()));

        step("Go to home page");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink (if needed)" + currency);
        homePage.loginWithSpecificAccountViaDeeplink(username, password, currency);
        String lang = homePage.getLanguageApp();

        if(!lang.equalsIgnoreCase("id")) {
            step("Change Language to 'IDN'");
            homePage.changeLanguageUsingDeeplink("IDN");
            lang = homePage.getLanguageApp();
        }

        step("Go to order detail order id '" + bookedOrderID + "'");
        orderListPage.openActiveTicketViaDeeplink(bookedOrderID);
        orderDetailPage.hidePromoBannerIfPresent();

        step("Verify step 1");
        Assertion.assertThat("Price detail showing in IDR currency", orderDetailPage.checkPriceDetailIsIn(currency), is(true));
        Assertion.assertThat("Share receipt button is showing", orderDetailPage.isShareReceiptButtonDisplayed(), is(true));

        step("Open Receipt");
        String totalPrice = orderDetailPage.getTotalPrice();

        orderDetailPage.shareReceipt();
        boolean isFileNameCorrect = orderDetailPage.isShareFileNameCorrect("Hotel",bookedOrderID,lang);

        step("Verify step 2");
        Assertion.assertThat("Price is having the correct format", CoreFunctions.isStringNumberInEn(totalPrice), is(false));
        Assertion.assertThat("Verify file title has same name", isFileNameCorrect, is(true));
    }

    @Author(names = {"Dendi"})
    @Module(name = "BusOrderDetail")
    @Tribe(name = "Order")
    @Vertical(name = "PRO")
    @Test
    public void testLoginBusOrderDetail() throws Exception {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testLoginBusOrderDetail");

        BusPage<WebDriver> busPage = BusPage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        Unm<WebDriver> unm = new Unm<>(session);
        DefaultAppsPage<?> defaultAppsPage = DefaultAppsPage.getInstance(session);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String originId = param.get("originId");
        String destinationId = param.get("destinationId");
        String departureDate = generateRandomDate(param.get("rangeAddDate"));
        String cardNumber = param.get("cardNumber");
        String expiry = param.get("expiry");
        String[] expirySplit = expiry.split("/");
        String cardExpiredMonth = expirySplit[0];
        String cardExpiredYear = expirySplit[1];
        String cvv = param.get("cvv");
        String cardName = param.get("cardName");
        String vertical = "";

        step("Get api access token");
        String apiAccessToken = unm.getAccessToken(username, password,"B2C");

        String bookedOrderID;
        try {
            step("Check if existing order is present");
            MapVerticalWithOrderId activeOrderData = new OrderApi<>(session).getAvailaibleOrder(apiAccessToken);
            bookedOrderID = activeOrderData.bus.stream().filter(order -> OrderStatus.ETICKET_ISSUED.getName().equals(order.orderStatus)).findFirst().orElseThrow().orderId;
            vertical = activeOrderData.bus.get(0).normalizeVerticalName();
        } catch (Exception e) {
            log("Failed to find existing bus order" + e.getMessage());
            step("Create new Bus issued order");
            BusCheckoutResponse result = busPage.createIssuedOrder(
                    username, password, originId, destinationId,departureDate,cardNumber,cardName,cardExpiredMonth,cardExpiredYear,cvv);
            bookedOrderID = result.data.order.orderId;
            vertical = "bus";
        }

        Assertion.assertThat("Got booked order ID", bookedOrderID, is(notNullValue()));

        step("Goto home");
        homePage.gotoHome();

        step("Clear browsers default preference");
        defaultAppsPage.clearBrowserDefaultPreferenceOnLambdaTest();

        step("In App Go To Home");
        homePage.gotoHome();
        String lang = homePage.getLanguageApp();

        step("Login as " + username + " via deeplink (if needed)");
        homePage.loginWithSpecificAccountViaDeeplink(username, password, "IDR");

        step("user open order detail");
        orderListPage.openActiveTicketViaDeeplink(bookedOrderID);
        orderDetailPage.hidePromoBannerIfPresent();

        step("Verify user able to share eticket");
        orderDetailPage.clickShareEticket();
        orderDetailPage.clickETicketLanguage("English");

        step("Verify share eticket");
        boolean isShareEticketWork = orderDetailPage.verifyShareEticketBtmSheet(vertical, lang, bookedOrderID);
        Assertion.assertThat("Verify share eticket", isShareEticketWork, is(true));

        step("Open e-Ticket from header");
        orderDetailPage.clickEticketFromHeader();
        orderDetailPage.clickETicketLanguage("English");

        step("e-Ticket file is opened from header entry point");
        Assertion.assertThat("Verify pdf content open", orderDetailPage.isPdfContentOpen(), is(true));
        orderListPage.openActiveTicketViaDeeplink(bookedOrderID);
        orderDetailPage.hidePromoBannerIfPresent();

        step("Verify departure data present");
        Assertion.assertThat("Verify departure data present", orderDetailPage.verifyBusDepartureSection(), is(true));

        step("Verify trip detail");
        orderDetailPage.openBusTripDetail();
        Assertion.assertThat("Verify trip detail component present",orderDetailPage.verifyTripDetailPresent(), is(true));

        step("Verify able to open order detail");
        orderDetailPage.openPriceDetail();

        step("Verify price detail");
        Assertion.assertThat("Verify price detail breakdown", orderDetailPage.verifyBusPriceDetailBreakDown(), is(true));

        step("Verify share receipt");
        orderDetailPage.shareReceipt();
        boolean isFileNameCorrect = orderDetailPage.isShareFileNameCorrect("Bus",bookedOrderID,lang);
        Assertion.assertThat("Verify file title has same name", isFileNameCorrect, is(true));

        step("Close share receipt");
        orderDetailPage.closeShareReceiptBtmSheet();

        step("Verify manage order exist");
        Assertion.assertThat("Verify manage order section",  orderDetailPage.verifyBusManageOrderSection(), is(true));

        step("Verify open receipt");
        orderDetailPage.openReceipt();

        step("Verify receipt is open");
        Assertion.assertThat("Verify pdf content open", orderDetailPage.isPdfContentOpen(), is(true));
        orderListPage.openActiveTicketViaDeeplink(bookedOrderID);
        orderDetailPage.hidePromoBannerIfPresent();

        step("Verify help center section present");
        Assertion.assertThat("Verify help center section present", orderDetailPage.verifyHelpCenterSectionPresent(), is(true));
    }

    @Author(names = {"Dendi"})
    @Module(name = "BusOrderDetail")
    @Tribe(name = "Order")
    @Vertical(name = "PRO")
    @Test
    public void testGuestBusOrderDetail() throws ExecutionException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testGuestBusOrderDetail");

        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        BusSearchPage<WebDriver> busSearchPage = BusSearchPage.getInstance(session);
        BusSearchResultPage<WebDriver> busSearchResultPage = BusSearchResultPage.getInstance(session);
        BuspdpPage<WebDriver> buspdpPage = BuspdpPage.getInstance(session);
        BusBookingFormPage<WebDriver> busBookingFormPage = BusBookingFormPage.getInstance(session);

        String currency = "IDR";
        String payment_method = "Credit Card";
        String cardNumber = param.get("cardNumber");
        String expiry = param.get("expiry");
        String cvv = param.get("cvv");
        String cardName = param.get("cardName");
        String vertical = "";
        String tripType = param.get("tripType");
        String originLocation = param.get("originLocation");
        String destinationLocation = param.get("destinationLocation");
        int daysToDeparture = Integer.parseInt(param.get("daysToDeparture"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String dateDeparture = CoreFunctions.addToLongDate(currentDate,daysToDeparture);
        int duration = Integer.parseInt(param.get("duration"));
        String dateReturn = CoreFunctions.addToLongDate(dateDeparture,duration);
        int passenger = Integer.parseInt(param.get("passenger"));

        String fullName = CoreFunctions.randomName();
        String phoneNumber =CoreFunctions.randomPhone();
        String emailAddress = CoreFunctions.randomEmail();

        step("In App Go To Home");
        homePage.gotoHome();
        String lang = homePage.getLanguageApp();

        step("user login as guest");
        loginPage.loginAsGuest();

        step("change cuuency");
        homePage.changeCurrency(currency,"");

        step("User login as guest");
        loginPage.loginAsGuest();

        step("Change currency");
        homePage.changeCurrency("IDR","");

        step("Open Bus vertical");
        homePage.openVerticalPageDeeplink("Bus","","IDR");

        step("Skip coachmark");
        busSearchPage.skipCoachMark();

        step("Switch trip type to round trip");
        busSearchPage.switchTripType(tripType);

        step("Input origin location");
        busSearchPage.inputOriginLocation(originLocation);

        step("Input destination location");
        busSearchPage.inputDestinationLocation(destinationLocation);

        step("Select date");
        busSearchPage.selectDate(dateDeparture,dateReturn);

        step("Select pax amount");
        busSearchPage.selectPassenger(passenger);

        step("Click search bus");
        busSearchPage.clickSearchBus();

        step("Skip bus srp coachmark");
        busSearchResultPage.skipCoachMark();

        step("Select bus departure on srp");
        busSearchResultPage.selectFirstBus();

        step("select seat departure");
        buspdpPage.openSeat();
        buspdpPage.clickSelectSeatButton();

        step("Select bus return on srp");
        busSearchResultPage.selectFirstBus();

        step("select seat return");
        buspdpPage.openSeat();
        buspdpPage.clickSelectSeatButton();

        step("Input guest contact detail");
        busBookingFormPage.inputGuestContactDetail(fullName,phoneNumber,emailAddress);
        busBookingFormPage.inputPassengerGuestDetail();
        busBookingFormPage.fillPassengerData(passenger);

        step("Continue to payment page");
        busBookingFormPage.continueToPaymentPage();

        step("user choose pay with cc");
        paymentPage.openPaymentMethod();
        paymentPage.selectPaymentMethod(payment_method);

        step("Get order ID");
        String bookedOrderID = paymentPage.checkOrderID();

        step("user fill cc detail");
        paymentPage.fillCCDetailRealIssued(cardNumber, expiry, cvv, cardName);
        paymentPage.clickPayButtonCC();
        paymentPage.verifyCreditCard3ds();

        step("Open the order list and go to order detail");
        paymentPage.seeOrderList();

        step("user open order detail");
        orderListPage.openActiveTicketViaDeeplink(bookedOrderID);

        step("Verify user able to share eticket");
        orderDetailPage.clickShareEticket();

        step("Verify share eticket");
        boolean isShareEticketWork = orderDetailPage.verifyShareEticketBtmSheet(vertical, lang, bookedOrderID);
        Assertion.assertThat("Verify share eticket", isShareEticketWork, is(true));

        step("Open e-Ticket from header");
        orderDetailPage.clickEticketFromHeader();
        orderDetailPage.clickETicketLanguage("English");

        step("e-Ticket file is opened from header entry point");
        Assertion.assertThat("Verify pdf content open", orderDetailPage.isPdfContentOpen(), is(true));
        orderDetailPage.clickBackFromPdf();

        step("Verify departure data present");
        Assertion.assertThat("Verify departure data present", orderDetailPage.verifyBusDepartureSection(), is(true));

        step("Verify trip detail");
        orderDetailPage.openBusTripDetail();
        Assertion.assertThat("Verify trip detail component present",orderDetailPage.verifyTripDetailPresent(), is(true));

        step("Verify help center section present");
        Assertion.assertThat("Verify help center section present", orderDetailPage.verifyHelpCenterSectionPresent(), is(true));

        step("Verify able to open order detail");
        orderDetailPage.openPriceDetail();

        step("Verify price detail");
        Assertion.assertThat("Verify price detail breakdown", orderDetailPage.verifyBusPriceDetailBreakDown(), is(true));

        step("Verify share receipt");
        orderDetailPage.shareReceipt();
        boolean isFileNameCorrect = orderDetailPage.isShareFileNameCorrect("Bus",bookedOrderID,lang);
        Assertion.assertThat("Verify file title has same name", isFileNameCorrect, is(true));

        step("Verify manage order exist");
        Assertion.assertThat("Verify manage order section",  orderDetailPage.verifyBusManageOrderSection(), is(true));

        step("Verify open receipt");
        orderDetailPage.openReceipt();

        step("Verify receipt is open");
        Assertion.assertThat("Verify pdf content open", orderDetailPage.isPdfContentOpen(), is(true));
        orderDetailPage.clickBackFromPdf();
    }

    @Author(names = {"Dendi"})
    @Module(name = "MyOrder")
    @Tribe(name = "Order")
    @Vertical(name = "PRO")
    @Test
    public void testAirportTransferCheckDetailOrderGuest() throws ExecutionException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String,String> testProperties = getParam("testAirportTransferCheckDetailOrderGuest");
        BaseTest.paramsThreadLocal.set(testProperties);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        CarTransferSearchPage<WebDriver> carTransferSearchPage = CarTransferSearchPage.getInstance(session);
        CarTransferSearchResultPage<WebDriver> carTransferSearchResultPage = CarTransferSearchResultPage.getInstance(session);
        CarTransferBookingPage<WebDriver> carTransferBookingPage = CarTransferBookingPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        CarTransferApi<?> carTransferApi = new CarTransferApi<>(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);

        String airport = testProperties.get("airport");
        String destination = testProperties.get("destination");
        String currency = testProperties.get("currency");
        String transferType = "transfer_out";
        String payment_method = testProperties.get("paymentMethod");
        String email = testProperties.get("username");
        String phoneNumber =testProperties.get("phoneNumber");
        String adminName = testProperties.get("adminName");
        String adminPhone = testProperties.get("adminPhone");
        String cardNumber = testProperties.get("cardNumber");
        String expiry = testProperties.get("expiry");
        String cvv = testProperties.get("cvv");
        String cardName = testProperties.get("cardName");

        step("User open app");
        homePage.gotoHome();
        String appLang = homePage.getLanguageApp();
        String salutation = appLang.equalsIgnoreCase("id") ? "TUAN" : "MR";

        step("login as guest");
        loginPage.loginAsGuest();

        step("Get langguage");
        String lang = homePage.getLanguageApp();

        step("Change currency to 'IDR'");
        homePage.changeCurrency("IDR","");

        step("User open car transfer vertical page");
        homePage.openVerticalPageDeeplink("Car Transfers","",currency);
        webViewPage.switchToWebViewUrl("airport-transfer");
        carTransferSearchPage.closePopups();

        step("User choose car transfer out");
        carTransferSearchPage.chooseTransferType(transferType);

        step("user input origin and destination");
        TripDetails tripDetails = carTransferSearchPage.choosePickupAndGetAddress(airport, destination);

        step("user search for car");
        carTransferSearchPage.searchCar();

        if(Configs.PLATFORM == Platform.IOS){
            webViewPage.switchToNative();
            carTransferSearchPage.allowLocationAccess();
            webViewPage.switchToWebViewUrl("airport-transfer");
        }

        step("user select car and vendor");
        carTransferSearchResultPage.selectCarAndVendor();

        step("Get car type");
        String carType = carTransferSearchResultPage.getCarType();

        step("user continue to booking page");
        carTransferSearchResultPage.continueToBookingPage();

        step("user continue to payment list page");
        carTransferBookingPage.fillDetailOrder(email,phoneNumber);
        String guestName = carTransferBookingPage.getGuestName();

        step("user continue to payment list page");
        carTransferBookingPage.continueToPayment();
        webViewPage.switchToNative();

        step("Verify use get orderId");
        String expectedOrderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",!expectedOrderID.isEmpty(),is(true));

        step("user choose pay with atm");
        paymentPage.openPaymentMethod();
        paymentPage.selectPaymentMethod(payment_method);

        step("Get total payment");
        String totalPrice = paymentPage.getTotalPrice();

        step("user fill cc detail");
        paymentPage.fillCCDetailRealIssued(cardNumber, expiry, cvv, cardName);
        paymentPage.clickPayButtonCC();
        paymentPage.verifyCreditCard3ds();

        step("Open order list");
        paymentPage.SeeOrderListAfterPaymentSuccess();

        step("Confirm order AT");
        String confirmOrderCode = carTransferApi.confirmOrder(expectedOrderID,adminName,adminPhone);
        Assertion.assertThat("Verify order confirmed", confirmOrderCode, equalTo("SUCCESS"));

        step("open order detail");
        orderListPage.openIssuedTicketViaDeeplink(expectedOrderID);
        orderListPage.removePopUp();

        step("Verify header is the containing pick up location");
        Assertion.assertThat("Verify header title",  orderDetailPage.verifyOrderDetailHeader(airport), is(true));

        step("Verify car detail section");
        Assertion.assertThat("Verify car detail section",   orderDetailPage.verifyAtCarDetailSection(carType), is(true));

        step("Assert airport transfer order detail");
        Assertion.assertThat("Is airport transfer detail correct",orderDetailPage.checkAirportTransferDetail(airport, tripDetails.destination), is(true));

        step("Verify airport transfer trip summary detail");
        Assertion.assertThat("Verify at trip summary detail", orderDetailPage.veriyAtSummaryDetail(tripDetails.origin,tripDetails.destination), is(true));

        step("Verify facilities detail present");
        Assertion.assertThat("Verify facilities detail present", orderDetailPage.verifyAtFacilitiesDetailPresent(), is(true));

        step("Verify contact detail");
        Assertion.assertThat("Verify contact detail in order detail page",  orderDetailPage.verifyAtContactDetail(salutation,guestName,phoneNumber,lang), is(true));

        step("Verify AT total price");
        Assertion.assertThat("Verify AT Total price", orderDetailPage.getAirportTransferTotalPrice() , containsString(totalPrice));
    }

    @Author(names = {"Dendi"})
    @Module(name = "MyOrder")
    @Tribe(name = "Order")
    @Vertical(name = "PRO")
    @Test
    public void testGuestCarRentalOrderDetail() throws ExecutionException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String,String> testProperties = getParam("testGuestCarRentalOrderDetail");
        BaseTest.paramsThreadLocal.set(testProperties);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        SearchPageCarRental<WebDriver> carRentalSearchPage = SearchPageCarRental.getInstance(session);
        SearchResultPageCarRental<WebDriver> carRentalSearchResultPage = SearchResultPageCarRental.getInstance(session);
        CarRentalReviewBookingPage<WebDriver> carRentalReviewBookingPage = CarRentalReviewBookingPage.getInstance(session);
        CarRentalBookingPage<WebDriver> carRentalBookingPage = CarRentalBookingPage.getInstance(session);
        CarRentalPage<WebDriver> carRentalPage = CarRentalPage.getInstance(session);

        String Location = testProperties.get("location");
        String pickupLocation = testProperties.get("pickupLocation");
        String email = testProperties.get("email");
        String carRentalDuration = testProperties.get("carRentalDuration");
        String carRentalPackage = testProperties.get("carRentalPackage");
        String dateRental = CoreFunctions.getCurrentLongDate();
        String phoneNumber = testProperties.get("phoneNumber");
        String currency = testProperties.get("currency");
        String cardNumber = testProperties.get("cardNumber");
        String expiry = testProperties.get("expiry");
        String cvv = testProperties.get("cvv");
        String cardName = testProperties.get("cardName");
        String payment_method = testProperties.get("paymentMethod");
        String adminName = testProperties.get("adminName");
        String adminPhone = testProperties.get("adminPhone");
        String driverType = testProperties.get("driverType");

        step("Go to home");
        homePage.gotoHome();

        step("Get current app langguage");
        String appLang = homePage.getLanguageApp();

        step("input credential");
        loginPage.loginAsGuest();

        step("Open Car Landing Page");
        homePage.openVerticalPageDeeplink("Car Rentals",appLang,currency);
        carRentalSearchPage.closeInformationPopUp();

        step("Select car rental type");
        carRentalSearchPage.chooseDriver(driverType);

        step("Select car rental date");
        carRentalSearchPage.selectDate(CoreFunctions.addToLongDate(dateRental,30));

        step("Input car rental location");
        carRentalSearchPage.chooseLocation(Location);

        step("Searh car rental");
        carRentalSearchPage.searchCar();

        step("choose car");
        carRentalSearchResultPage.chooseCar();

        step("Get pick up and drop off date");
        List<String> dropPickUpdate = carRentalReviewBookingPage.getPickUpAndDropOffDate();

        step("Get fleet name");
        String fleetName = carRentalReviewBookingPage.getFleetName();

        step("continue to booking form from review page");
        carRentalReviewBookingPage.continueFromReviewDetailCar();

        step("pick up location");
        carRentalBookingPage.choosePickUpLocation(pickupLocation);

        step("continue to complete booking");
        carRentalBookingPage.continueToCompleteBookingPage();

        step("input contact detail");
        carRentalBookingPage.fillGuestDetailOrder(email,phoneNumber);
        carRentalBookingPage.setContinueToPayment();

        step("get order id");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",!orderID.isEmpty(),is(true));

        step("user choose pay with cc");
        paymentPage.openPaymentMethod();
        paymentPage.selectPaymentMethod(payment_method);

        step("user fill cc detail");
        paymentPage.fillCCDetailRealIssued(cardNumber, expiry, cvv, cardName);
        paymentPage.clickPayButtonCC();
        paymentPage.verifyCreditCard3ds();

        step("Confirm order AT");
        String confirmOrderCode = carRentalPage.confirmOrder(orderID,adminName,adminPhone);
        Assertion.assertThat("Verify order confirmed", confirmOrderCode, equalTo("SUCCESS"));

        step("Go to home");
        homePage.gotoHome();

        step("Open my order page");
        homePage.openMyOrderPage();

        step("open order detail");
        orderListPage.openIssuedTicketViaDeeplink(orderID);
        orderListPage.removePopUp();

        step("Verify user able to share eticket");
        orderDetailPage.clickShareEticketNative();
        Assertion.assertThat("Verify user able to share eticket", orderDetailPage.verifyShareEticketBtmSheet("CAR", appLang, orderID), is(true));

        step("Open e-ticket from header");
        orderDetailPage.clickEticketFromHeader();

        step("e-ticket open from header entry point");
        Assertion.assertThat("Verify pdf content ", orderDetailPage.isPdfContentOpen(), is(true));

        step("Close eticket pdf");
        orderDetailPage.clickBackFromPdf();

        step("Verify car rental product detail");
        Assertion.assertThat("Verify car rental product detail",orderDetailPage.verifyCarRentalProductDetail(fleetName, driverType, appLang),is(true));

        step("Verify driver detail");
        Assertion.assertThat("Verify driver detail", orderDetailPage.verifyDriverDetailExist(), is(true));

        step("Verify rental detail");
        Assertion.assertThat("Verify rental detail", orderDetailPage.verifyRentalDetail(pickupLocation, pickupLocation, dropPickUpdate.get(0), dropPickUpdate.get(1), carRentalDuration), is(true));

        step("Verify help center section present");
        Assertion.assertThat("Verify help center section present", orderDetailPage.verifyHelpCenterSectionPresent(), is(true));

        step("Verify rr info present");
        Assertion.assertThat("Verify rr info present", orderDetailPage.verifyCarRrInfoSection(), is(true));

        step("Verify share receipt");
        orderDetailPage.shareReceiptNative();
        boolean isFileNameCorrect = orderDetailPage.isShareFileNameCorrect("Car", orderID, appLang);
        Assertion.assertThat("Verify file title has same name", isFileNameCorrect, is(true));
        orderDetailPage.closeShareReceiptBtmSheet();

        step("Verify price detail open");
        orderDetailPage.openPriceDetailNative();
        Assertion.assertThat("Verify price detail open", orderDetailPage.verifyCrPriceDetailComponentCorrect(), is(true));

        step("Swiping back to top of car rental page to verify floating button");
        orderDetailPage.carRentalScrollToTop();
        orderDetailPage.carRentalClickScrollDownBtn();
        Assertion.assertThat("Verify car manage order shown after clicking floating button to scroll down",orderDetailPage.verifyCarManageOrder(), is(true));

        step("Verify refund button");
        orderDetailPage.clickRefundButton();
        Assertion.assertThat("Verify need login pop up show up", orderDetailPage.isLoginChallengeShown(), is(true));
    }


    @Author(names = {"Dendi"})
    @Module(name = "ShareEticket")
    @Tribe(name = "Order")
    @Vertical(name = "PRO")
    @Test
    public void testShareEticketLogin() throws ExecutionException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String,String> map = getParam("testShareEticketLogin");
        BaseTest.paramsThreadLocal.set(map);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        Unm<WebDriver> unm = new Unm<>(session);

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
        String guestTitle = getRandomObject(salutationList);

        step("Get api access token");
        String apiAccessToken = unm.getAccessToken(username, password,"B2C");

        String bookedOrderID;
        String vertical;
        try {
            step("Check if existing order is present");
            MapVerticalWithOrderId activeOrderData = new OrderApi<>(session).getAvailaibleOrder(apiAccessToken);
            bookedOrderID = activeOrderData.hotel.stream().filter(order -> OrderStatus.ETICKET_ISSUED.getName().equals(order.orderStatus)).findFirst().orElseThrow().orderId;
            vertical = activeOrderData.firstOrder.normalizeVerticalName();
        } catch (Exception e) {
            log("Failed to find existing hotel order" + e.getMessage());
            step("Create new hotel issued order");
            hotelApi.createIssuedOrderApi(
                    username, password, startDate, query, adult, night, room,
                    orderFullName, orderNameTitle, guestFullName, guestTitle,"IDR");
            bookedOrderID = String.valueOf(hotelApi.getOrderId());
            vertical = "hotel";
        }

        Assertion.assertThat("Got booked order ID", bookedOrderID, is(notNullValue()));

        step("Goto home");
        homePage.gotoHome();

        step("Get app langguage");
        String appLang = homePage.getLanguageApp();
        String eticketLang = appLang;

        step("Login as " + username + " via deeplink (if needed)");
        homePage.loginWithSpecificAccountViaDeeplink(username, password, "IDR");

        step("Find the issued order");
        orderListPage.openIssuedTicketViaDeeplink(bookedOrderID);
        orderDetailPage.hidePromoBannerIfPresent();

        step("click share eticket from header");
        if ((Configs.PLATFORM == Platform.IOS && !vertical.contains("bus") && !vertical.contains("ferry") && !vertical.contains("car") && !vertical.contains("hotel")) || (Configs.PLATFORM == Platform.ANDROID && "car".equalsIgnoreCase(vertical))) {
            orderDetailPage.clickShareEticketNative();
            orderDetailPage.clickETicketLanguage("English");
            eticketLang = "EN";
        }else{
            orderDetailPage.clickShareEticket();
        }

        step("Verify eticket from header work");
        orderDetailPage.hidePromoBannerIfPresent();
        boolean isShareEticketWork = orderDetailPage.verifyShareEticketBtmSheet(vertical, eticketLang, bookedOrderID);
        Assertion.assertThat("Verify share eticket", isShareEticketWork, is(true));
    }

    @Author(names = {"Dendi"})
    @Module(name = "ShareEticket")
    @Tribe(name = "Order")
    @Vertical(name = "PRO")
    @Test
    public void testShareEticketGuest() throws ExecutionException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String,String> testProperties = getParam("testShareEticketGuest");
        BaseTest.paramsThreadLocal.set(testProperties);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        SearchPageCarRental<WebDriver> carRentalSearchPage = SearchPageCarRental.getInstance(session);
        SearchResultPageCarRental<WebDriver> carRentalSearchResultPage = SearchResultPageCarRental.getInstance(session);
        CarRentalReviewBookingPage<WebDriver> carRentalReviewBookingPage = CarRentalReviewBookingPage.getInstance(session);
        CarRentalBookingPage<WebDriver> carRentalBookingPage = CarRentalBookingPage.getInstance(session);
        CarRentalPage<WebDriver> carRentalPage = CarRentalPage.getInstance(session);

        String Location = testProperties.get("location");
        String pickupLocation = testProperties.get("pickupLocation");
        String email = testProperties.get("email");
        String carRentalDuration = testProperties.get("carRentalDuration");
        String carRentalPackage = testProperties.get("carRentalPackage");
        String dateRental = CoreFunctions.getCurrentLongDate();
        String phoneNumber = testProperties.get("phoneNumber");
        String currency = testProperties.get("currency");
        String cardNumber = testProperties.get("cardNumber");
        String expiry = testProperties.get("expiry");
        String cvv = testProperties.get("cvv");
        String cardName = testProperties.get("cardName");
        String payment_method = testProperties.get("paymentMethod");
        String adminName = testProperties.get("adminName");
        String adminPhone = testProperties.get("adminPhone");

        step("Go to home");
        homePage.gotoHome();

        step("Get current app language");
        String appLang = homePage.getLanguageApp();

        step("input credential");
        loginPage.loginAsGuest();

        step("Change currency");
        homePage.changeCurrency(currency,"");

        step("Open Car Landing Page");
        homePage.openVerticalPageDeeplink("Car Rentals",appLang,currency);
        carRentalSearchPage.closeInformationPopUp();

        step("Select car rental type");
        carRentalSearchPage.chooseDriver("with_driver");

        step("Select car rental date");
        carRentalSearchPage.selectDate(CoreFunctions.addToLongDate(dateRental,30));

        step("Input car rental location");
        carRentalSearchPage.chooseLocation(Location);

        step("Searh car rental");
        carRentalSearchPage.searchCar();

        step("choose car");
        carRentalSearchResultPage.chooseCar();

        step("continue to booking form from review page");
        carRentalReviewBookingPage.continueFromReviewDetailCar();

        step("pick up location");
        carRentalBookingPage.choosePickUpLocation(pickupLocation);

        step("continue to complete booking");
        carRentalBookingPage.continueToCompleteBookingPage();

        step("input contact detail");
        carRentalBookingPage.fillGuestDetailOrder(email,phoneNumber);
        carRentalBookingPage.setContinueToPayment();

        step("get order id");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",!orderID.isEmpty(),is(true));

        step("user choose pay with cc");
        paymentPage.openPaymentMethod();
        paymentPage.selectPaymentMethod(payment_method);

        step("user fill cc detail");
        paymentPage.fillCCDetailRealIssued(cardNumber, expiry, cvv, cardName);
        paymentPage.clickPayButtonCC();
        paymentPage.verifyCreditCard3ds();

        step("Confirm order AT");
        String confirmOrderCode = carRentalPage.confirmOrder(orderID,adminName,adminPhone);
        Assertion.assertThat("Verify order confirmed", confirmOrderCode, equalTo("SUCCESS"));

        step("open order detail");
        orderListPage.openIssuedTicketViaDeeplink(orderID);
        orderListPage.removePopUp();

        step("click share eticket from header");
        orderDetailPage.clickShareEticketNative();

        step("Verify eticket from header work");
        boolean isShareEticketWork = orderDetailPage.verifyShareEticketBtmSheet("car", appLang, orderID);
        Assertion.assertThat("Verify share eticket", isShareEticketWork, is(true));
    }

    @Author(names = {"Kartik"})
    @Module(name = "Order")
    @Tribe(name = "MultiOrder")
    @Vertical(name = "PRO")
    @Test
    public void testOrderDetailHotelMultiOrder() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testOrderDetailHotelMultiOrder");
        HotelApi<?> hotelApi = new HotelApi<>(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        var unm = new Unm<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String hotelName = param.get("hotelName");
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));
        int room = Integer.parseInt(param.get("room"));
        String startDate = generateRandomDate(param.get("rangeAddDate"));
        int qtyEachRoom = Integer.parseInt(param.get("qtyEachRoom"));
        String currency = "IDR";

        step("Get api access token");
        String apiAccessToken = unm.getAccessToken(username, password,"B2C");

        String issuedOrderID;
        try {
            step("Check if existing order is present");
            MapVerticalWithOrderId activeOrderData = new OrderApi<>(session).getAvailaibleOrder(apiAccessToken);
            MapVerticalWithOrderId.TitleAndOrderId titleAndOrderId = activeOrderData.hotel.stream().filter(hotel -> {
                        boolean eIssued = OrderStatus.ETICKET_ISSUED.getName().equals(hotel.orderStatus);
                        boolean isMultiOrder = hotel.checkIfMultiOrder();
                        return eIssued && isMultiOrder;
                    })
                    .findFirst()
                    .orElseThrow();
            issuedOrderID = titleAndOrderId.orderId;
            step("Found existing multi order hotel issued order with order id: " + issuedOrderID + " and title: " + titleAndOrderId.title);
            hotelName = titleAndOrderId.title;
        } catch (Exception e) {
            log("Failed to create multi-room hotel order" + e.getMessage());
            step("Create Multi-Order Hotel with types of room paid using " + currency + " currency");
            hotelApi.createOrderMultiRoomApi(username, password,
                    hotelName, adult, night, room, startDate, qtyEachRoom, currency, false, "");
            issuedOrderID = String.valueOf(hotelApi.getOrderId());
        }

        Assertion.assertThat("Got issued order ID", issuedOrderID, is(notNullValue()));

        step("Get data from the issued order");
        String orderID = issuedOrderID;

        step("Go to home");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink (if needed)" + currency);
        homePage.loginWithSpecificAccountViaDeeplink(username, password, currency);

        step("user open your order");
        homePage.openMyOrderPage();

        step("user open the order detail page: " + orderID);
        orderListPage.openActiveTicketViaDeeplink(orderID);
        orderListPage.removePopUp();
        orderDetailPage.hidePromoBannerIfPresent();

        step("user check hotel name same as testdata");
        Assertion.assertThat("hotel name should be equal to testdata", orderDetailPage.isHotelNameDisplayed(hotelName), is(true));

        step("User check order Detail ID component");
        Assertion.assertThat("order Detail ID component should be verified", orderDetailPage.verifyOrderDetailIdComponent(), is(true));

        step("User check call button");
        Assertion.assertThat("call button should be clicked", orderDetailPage.clickCallBtn(), is(true));

        step("user verify reservation details and special request");
        orderDetailPage.seeMoreDetails();
        Assertion.assertThat("Verify special request section", orderDetailPage.verifyReviseSpecialRequest(), is(true));

        step("User verifies the location redirection button");
        Assertion.assertThat("location redirection button should be verified", orderDetailPage.verifyLocationRedirectionButton(), is(true));

        step("User checks refund and reschedule Details");
        Assertion.assertThat("refund and reschedule info section should be verified", orderDetailPage.verifyRefundAndRescheduleInfo(), is(true));

        step("User check Refund Button");
        Assertion.assertThat("refund button should be displayed", orderDetailPage.isRefundButtonDisplayed(), is(true));

        step("User check Reschedule Button");
        Assertion.assertThat("reschedule button should be displayed", orderDetailPage.isRescheduleButtonDisplayed(), is(true));

        step("User check Page Module");
        Assertion.assertThat("Page Module should be displayed", orderDetailPage.verifyPageModule(), is(true));

        step("User check edit booking button");
        Assertion.assertThat("edit booking button should be displayed", orderDetailPage.verifyEditGuestDetail(), is(true));
    }

    @Author(names = {"Dendi"})
    @Module(name = "OrderDetail")
    @Tribe(name = "Order")
    @Vertical(name = "PRO")
    @Test
    public void testLoginOrderDetailFerry() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testLoginOrderDetailFerry");
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        FerryPage<WebDriver> ferryPage = FerryPage.getInstance(session);
        Unm<WebDriver> unm = new Unm<>(session);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String currency = param.get("currency");

        String cardNumber = param.get("cardNumber");
        String cardHolderName = param.get("cardHolderName");
        String cardExpiredMonth = param.get("cardExpiredMonth");
        String cardCvvNumber = param.get("cardCvvNumber");
        String cardExpiredYear = param.get("cardExpiredYear");

        String origin = param.get("origin");
        String destination = param.get("destination");
        String departureDate = CoreFunctions.getEndOfCurrentMonth("yyyy-MM-dd");
        String departureTime = param.get("departureTime").replace("-", ":");
        String vehicleType = param.get("vehicleType");
        int adult = Integer.parseInt(param.get("adult"));
        int child = Integer.parseInt(param.get("child"));
        int infant = Integer.parseInt(param.get("infant"));
        int elderly = Integer.parseInt(param.get("elderly"));

        step("Get api access token");
        LoginDataUnmResponse.Data authData = unm.unmLoginApi(username, password);
        String apiAccessToken = authData.accessToken;
        String accountId = String.valueOf(authData.accountId);

        step("check order present in order");
        MapVerticalWithOrderId.TitleAndOrderId activeOrderData = orderListPage.findFirstOrderFilterByVertical(apiAccessToken,username,accountId,"FERRY",currency,false,false,OrderStatus.ETICKET_ISSUED);
        log("Active order data : " + activeOrderData);
        String bookedOrderID;
        String orderLang;

        if(activeOrderData == null || activeOrderData.orderId==null){
            step("Create Ferry order ");
            bookedOrderID = String.valueOf(ferryPage.createIssuedOrderFerry(username,password,origin,destination,departureDate,departureTime,vehicleType,adult,child,infant,elderly,cardNumber,cardHolderName,cardExpiredMonth,cardExpiredYear,cardCvvNumber));
            orderLang = "id";
        }else{
            bookedOrderID = activeOrderData.orderId;
            orderLang = activeOrderData.userLang;
        }

        step("Go to home");
        homePage.gotoHome();

        step("User login");
        loginPage.loginAsGuest();
        loginPage.loginWith(loginType, username, password);

        step("Get current user lang");
        String lang = homePage.getLanguageApp();
        if(!lang.equalsIgnoreCase(orderLang)){
            step("Change Language to 'IDN'");
            homePage.changeLanguageUsingDeeplink("IDN");
        }

        step("Open order " + bookedOrderID);
        orderListPage.openIssuedTicketViaDeeplink(bookedOrderID);

        step("Check user able to share eticket");
        orderDetailPage.clickShareEticket();
        boolean isShareEticketWork = orderDetailPage.verifyShareEticketBtmSheet("ferry",lang, bookedOrderID);
        Assertion.assertThat("Verify share eticket", isShareEticketWork, is(true));

        step("Open e-Ticket from header in bahasa indonesia");
        orderDetailPage.clickEticketFromHeader();
        orderDetailPage.clickETicketLanguage("Bahasa Indonesia");

        step("e-Ticket file is opened from header entry point in bahasa indonesia");
        String actualPdfContent = orderDetailPage.getPDFContentFile();
        Assertion.assertThat("Hotel the PDF content should be in bahasa", actualPdfContent, containsString("Pelabuhan"));

        step("Open e-Ticket from header in enlish");
        orderDetailPage.clickBackFromPdf();
        orderDetailPage.clickEticketFromHeader();
        orderDetailPage.clickETicketLanguage("English");


        step("e-Ticket file is opened from header entry point in english");
        actualPdfContent = orderDetailPage.getPDFContentFile();
        Assertion.assertThat("Hotel the PDF content should be in english", actualPdfContent, containsString("port"));

        step("Close eticket menu");
        orderDetailPage.clickBackFromPdf();

        step("Verify help center section present");
        Assertion.assertThat("Verify help center section present", orderDetailPage.verifyHelpCenterSectionPresentWebview(), is(true));

        step("Check user can see refund and reschedule policy section");
        Assertion.assertThat("Verify rr info present", orderDetailPage.verifyFerryRrInfoSection(), is(true));

        step("Check User able to see or share receipt");
        orderDetailPage.shareReceipt();
        boolean isFileNameCorrect = orderDetailPage.isShareFileNameCorrect("Ferry",bookedOrderID,orderLang);
        Assertion.assertThat("Verify file title has same name", isFileNameCorrect, is(true));

        step("Close share receipt");
        orderDetailPage.closeShareReceiptBtmSheet();

        step("Check user can see manage order section"); //check add to calendar section here too, since it has
        Assertion.assertThat("Verify manage order section present", orderDetailPage.verifyFerryManageOrderSection(), is(true));

        step("open price detail");
        orderDetailPage.openPriceDetail();

        step("verify price and price detail match");
        String priceFromOrderDetail= orderDetailPage.getTotalPriceOrderDetailPage();
        String totalPricePriceDetail = orderDetailPage.getTotalPricePriceDetail();
        Assertion.assertThat("Verify price match order detail page and price detail ", convertPrice(priceFromOrderDetail), equalTo(convertPrice(totalPricePriceDetail)));
        Assertion.assertThat("Verify ferry order detail price breakdown correct", orderDetailPage.verifyFerryPriceDetailBreakDown(), is(true));
    }


    @Author(names = {"Dendi"})
    @Module(name = "OrderDetail")
    @Tribe(name = "Order")
    @Vertical(name = "PRO")
    @Test
    public void testGuestOrderDetailFerry() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testGuestOrderDetailFerry");
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        FerrySearchPage<WebDriver> ferrySearchPage = FerrySearchPage.getInstance(session);
        FerrySearchResultPage<WebDriver> ferrySearchResultPage = FerrySearchResultPage.getInstance(session);
        FerryBookingPage<WebDriver> ferryBookingPage = FerryBookingPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        FerryApi<?> ferryApi = new  FerryApi<>(session);

        String origin = param.get("origin");
        String destination = param.get("destination");
        String vehicleType = param.get("vehicleType");
        int adult = Integer.parseInt(param.get("adult"));
        int child = Integer.parseInt(param.get("child"));
        String email = param.get("email");

        step("Get available date from API");
        String departureDate = ferryApi.getFerryAvailableDate(origin, destination, vehicleType, adult, child,0,0);

        step("user open app");
        homePage.gotoHome();

        step("user continue as guest");
        loginPage.loginAsGuest();

        if(Configs.ENVIRONMENT == Environment.PROD) {
            step("Change currency to 'IDR'");
            homePage.changeCurrency("IDR","");
        }

        step("Get App Language");
        String language = homePage.getLanguageApp();

        step("Open Ferry Landing Page");
        homePage.openVerticalPageDeeplink("Ferry", language, "IDR");

        step("Skip coachmark");
        ferrySearchPage.skipCoachMark();

        step("input origin port");
        ferrySearchPage.inputOrigin(origin);

        step("input destination port");
        ferrySearchPage.inputDestination(destination);

        step("User input pax qty");
        ferrySearchPage.openTicketQty();
        ferrySearchPage.setTicketQty(adult, child, 0, 0);
        ferrySearchPage.saveTicketQty();

        step("Input ticket type");
        ferrySearchPage.selectTicketType(vehicleType);

        step("Departure date");
        ferrySearchPage.selectDate(departureDate);

        step("submit search");
        ferrySearchPage.submitSearch();

        step("Skip information coachmark in srp");
        ferrySearchResultPage.skipCoachMark();

        step("Check booking form price detail");
        ferrySearchResultPage.chooseFirstFleet();

        step("Input passenger detail");
        ferryBookingPage.fillGuestContactDetail(email, "kartikdixit", "8840196616");

        step("input  guest detail");
        ferryBookingPage.fillGuestData(adult, child, 0, 0);

        step("Fill plate number if required");
        ferryBookingPage.fillLicensePlateNumber();

        step("Check term and condition");
        ferryBookingPage.clickTncCheckBox();

        step("continue to payment page");
        ferryBookingPage.submitBooking();
        ferryBookingPage.setLocationService();

        step("verify order id is received");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id", !orderID.isEmpty(), is(true));

        step("get total price");
        String totalPrice = paymentPage.getTotalPrice();
        Assertion.assertThat("total price should be displayed", !totalPrice.isEmpty(), is(true));

        step("user choose pay with cc");
        paymentPage.openPaymentMethod();
        paymentPage.selectPaymentMethod("ATM");
        paymentPage.clickPayButton();
        paymentPage.paymentChecker();

        step("Open order " + orderID);
        homePage.gotoHome();
        orderListPage.openIssuedTicketViaDeeplink(orderID);

        step("Check user able to share eticket");
        orderDetailPage.clickShareEticket();
        boolean isShareEticketWork = orderDetailPage.verifyShareEticketBtmSheet("ferry",language, orderID);
        Assertion.assertThat("Verify share eticket", isShareEticketWork, is(true));

        step("Open e-Ticket from header in bahasa indonesia");
        orderDetailPage.clickEticketFromHeader();
        orderDetailPage.clickETicketLanguage("Bahasa Indonesia");

        step("e-Ticket file is opened from header entry point in bahasa indonesia");
        String actualPdfContent = orderDetailPage.getPDFContentFile();
        Assertion.assertThat("Hotel the PDF content should be in bahasa", actualPdfContent, containsString("Pelabuhan"));

        step("Open e-Ticket from header in enlish");
        orderDetailPage.clickBackFromPdf();
        orderDetailPage.clickEticketFromHeader();
        orderDetailPage.clickETicketLanguage("English");

        step("e-Ticket file is opened from header entry point in english");
        actualPdfContent = orderDetailPage.getPDFContentFile();
        Assertion.assertThat("ferry the PDF content should be in english", actualPdfContent, containsString("port"));

        step("Close eticket menu");
        orderDetailPage.clickBackFromPdf();

        step("Verify help center section present");
        Assertion.assertThat("Verify help center section present", orderDetailPage.verifyHelpCenterSectionPresentWebview(), is(true));

        step("Check user can see refund and reschedule policy section");
        Assertion.assertThat("Verify rr info present", orderDetailPage.verifyFerryRrInfoSection(), is(true));

        step("Check User able to see or share receipt");
        orderDetailPage.shareReceipt();
        boolean isFileNameCorrect = orderDetailPage.isShareFileNameCorrect("Ferry",orderID,language);
        Assertion.assertThat("Verify file title has same name", isFileNameCorrect, is(true));

        step("Close share receipt");
        orderDetailPage.closeShareReceiptBtmSheet();

        step("Check user can see manage order section"); //check add to calendar section here too, since it has
        Assertion.assertThat("Verify manage order section present", orderDetailPage.verifyFerryManageOrderSection(), is(true));

        step("Verify price detail breakdown");
        orderDetailPage.openPriceDetail();

        step("verify price and price detail match");
        String priceFromOrderDetail= orderDetailPage.getTotalPriceOrderDetailPage();
        String totalPricePriceDetail = orderDetailPage.getTotalPricePriceDetail();
        Assertion.assertThat("Verify price match order detail page and price detail ", convertPrice(priceFromOrderDetail), equalTo(convertPrice(totalPricePriceDetail)));
        Assertion.assertThat("Verify ferry order detail price breakdown correct", orderDetailPage.verifyFerryPriceDetailBreakDown(), is(true));
    }

    @Author(names = {"Riya Bajpai"})
    @Module(name = "MyOrder")
    @Tribe(name = "Order")
    @Vertical(name = "GroundTransport")
    @Test
    public void testWhooshDetailOrder() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testWhooshDetailOrder");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        WhooshSearchPage<WebDriver> whooshSearchPage = WhooshSearchPage.getInstance(session);
        WhooshSearchResultPage<WebDriver> whooshSearchResultPage = WhooshSearchResultPage.getInstance(session);
        WhooshBookingPage<WebDriver> whooshBookingPage = WhooshBookingPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);

        String username = param.get("username");
        String password = param.get("password");
        String originStation = param.get("originStation");
        String destinationStation = param.get("destinationStation");
        String currency = param.get("currency");
        String dateRange = param.get("dateRange");
        String tripType = param.get("tripType");
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int infantPassengers = Integer.parseInt(param.get("infant"));

        TrainAndWhooshGeneralData trainAndWhooshGeneralData = new TrainAndWhooshGeneralData(originStation, destinationStation,adultPassengers,adultPassengers,infantPassengers,tripType,dateRange);
        String departureDate = trainAndWhooshGeneralData.departureDateLong;

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink (if needed) and change currency to " + currency);
        homePage.loginWithSpecificAccountViaDeeplink(username, password, currency);

        step("Open whoosh Landing Page");
        homePage.openVerticalPageDeeplink("Whoosh", "en", currency);

        step("Skip Popup");
        whooshSearchPage.skipPopup();

        step("Select origin station");
        trainAndWhooshGeneralData.originCityName = whooshSearchPage.selectOriginAndGetCityName(originStation);

        step("Select destination station");
        trainAndWhooshGeneralData.destinationCityName = whooshSearchPage.selectDestinationAndGetCityName(destinationStation);

        step("Toggle trip type to "+tripType);
        whooshSearchPage.selectRoundTripToggle(tripType);

        step("Add passenger");
        whooshSearchPage.addPassenger(adultPassengers,infantPassengers);

        step("Select date");
        whooshSearchPage.selectDate(departureDate);

        step("Search whoosh");
        whooshSearchPage.searchTrain();

        step("Skip Popup");
        whooshSearchPage.skipPopup();

        step("Select departure whoosh");
        whooshSearchResultPage.selectWhoosh();

        step("Fill passenger detail");
        TrainData passengerNamesAndId = whooshBookingPage.fillPassengerDetail(adultPassengers,infantPassengers);

        step("Continue to 2nd half");
        whooshBookingPage.continueTo2ndHalf();
        TrainData dataFromBookingPage = whooshBookingPage.getDetailsWhooshAndPassenger("en");
        dataFromBookingPage.adult = adultPassengers;
        dataFromBookingPage.infant = infantPassengers;
        dataFromBookingPage.guestList = passengerNamesAndId.guestList;
        dataFromBookingPage.idNumberList = passengerNamesAndId.idNumberList;

        step("Continue to payment");
        whooshBookingPage.continueToPayment();

        step("Check order ID");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",!orderID.isBlank(),is(true));

        step("Open payment method");
        paymentPage.openPaymentMethod();

        step("select sakaku as payment method");
        paymentPage.selectPaymentMethod("Sakuku");

        step("Submit payment");
        paymentPage.clickPayButton();

        step("Go to the order list");
        paymentPage.seeOrderList();

        step("open issued order");
        orderListPage.refreshTheOrderList(2);
        orderListPage.openIssuedTicketViaDeeplink(orderID);
        
        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.getETicketLabel(), either(is("Your e-ticket is here")).or(is("E-tiketmu di sini")));

        step("Verify Order ID");
        Assertion.assertThat("Order ID should match", orderDetailPage.getOrderID(), is(orderID));

        step("Verify Booking Code" );
        Assertion.assertThat("Booking code should be displayed", orderDetailPage.getBookingCode().isBlank(), is(false));

        step("Click on See e-boarding pass");
        Assertion.assertThat("See e-boarding pass button should be visible", orderDetailPage.clickSeeBoardingPass(), is(true));

        step("Verify passenger and train details on boarding pass");
        Assertion.assertThat("Passenger and Whoosh details should be correct", orderDetailPage.verifyBoardingPassDetails(dataFromBookingPage), is(true));

        step("Return to Order Detail page");
        orderDetailPage.clickBackNew();
        Assertion.assertThat("User should return to Order Detail page", orderDetailPage.verifyIsOnOrderDetailPageCheck(), is(true));

        step("Verify departure train details");
        Assertion.assertThat("Departure and arrival train details should be correct", orderDetailPage.verifyDepartureTrainDetails(dataFromBookingPage), is(true));

        step("Verify passengers and facilities");
        Assertion.assertThat("Passenger names, seats and facilities should be displayed correctly", orderDetailPage.verifyPassengerAndFacilities(dataFromBookingPage), is(true));

        step("Open Price Details");
        Assertion.assertThat("Price detail should open successfully", orderDetailPage.openPriceDetail(), is(true));

        step("Close Price Details");
        orderDetailPage.closePriceDetail();

        step("Verify Help Center");
        orderListPage.openIssuedTicketViaDeeplink(orderID);
        Assertion.assertThat("Help Center should open successfully", orderDetailPage.verifyHelpCenter(), is(true));

        step("Verify Contact Customer Care");
        Assertion.assertThat("Contact Customer Care page should open successfully", orderDetailPage.verifyContactCustomerCare(), is(true));

        step("reopen the order detail page");
        orderListPage.openIssuedTicketViaDeeplink(orderID);

        step("Verify Important Info");
        Assertion.assertThat("Important Info page should open successfully", orderDetailPage.verifyImportantInfo(), is(true));
        orderDetailPage.clickBackNew();

    }
    @Author(names = {"Vanshika Agarwal"})
    @Module(name = "OrderDetail")
    @Tribe(name = "Order")
    @Vertical(name = "LPG")
    @Test
    public void lowestPriceGuarantee() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("lowestPriceGuarantee");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        HotelPage<WebDriver> hotelPage = HotelPage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        HomesPage<WebDriver> homesPage = HomesPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        TTDApi<?> ttdApi = new TTDApi<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String vertical = param.get("vertical").toLowerCase();
        String date = generateRandomDate(param.get("rangeAddDate"));
        int room = Integer.parseInt(param.get("rooms"));
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));
        String query = param.get("hotelName");
        String productSlug = param.get("productSlug");
        String urlToCompare = param.get("compareUrl");
        String finalPrice = param.get("finalPrice");
        String invalidHighPrice = param.get("invalidHighPrice");

        String[] salutationList = { "Mr", "Mrs", "Ms" };
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autoguest";
        String guestTitle = salutationList[0];
        String currency = "IDR";
        String lang = "en";
        boolean refundable = Boolean.parseBoolean(param.getOrDefault("refundable", "false"));

        boolean result = false;
        String issuedOrderID;

        switch (vertical) {
            case "hotels":
            case "hotel":
                step("Create new hotel issued order");
                result = hotelApi.createIssuedOrderApi(
                        username, password, date, query, adult, night, room,
                        orderFullName, orderNameTitle, guestFullName, guestTitle, currency);
                issuedOrderID =  String.valueOf(hotelApi.getOrderId());

                break;

            case "home":
            case "homes":
                step("Create new homes issued order");
                result = homesPage.createIssuedOrderApi(
                        username, password, date, query, adult, night, room,
                        orderFullName, orderNameTitle, guestFullName, guestTitle, lang, currency);
                issuedOrderID =  String.valueOf(homesPage.getOrderId());
                break;

            case "ttd":
                step("Create new TTD issued order");
                issuedOrderID = ttdApi.createIssuedOrderSakukuApi(username, password, currency, productSlug, refundable,
                        guestFullName, guestTitle, guestFullName, username, guestTitle, false);
                result = issuedOrderID != null && !issuedOrderID.trim().isEmpty();
                break;

            default:
                throw new IllegalArgumentException("Invalid vertical passed in test data: " + vertical);
        }

        step("Verify Booking via API is successful");
        Assertion.assertThat("Verify Booking via API is successful",result,is(true));

        step("user open app");
        homePage.gotoHome();

        step("login as " + username + " via deeplink");
        homePage.loginWithSpecificAccountViaDeeplinkOnly(username, password);

        step("Open my order page");
        homePage.openMyOrderPage();

        step("Find the issued order");
        orderListPage.openOrderDetailViaDeeplink(issuedOrderID);
        Assertion.assertThat("Order ID is displayed correctly", orderDetailPage.getOrderID(), is(issuedOrderID));

        step("Hide promotional banner if it appears");
        orderDetailPage.hidePromoBannerIfPresent();
        Assertion.assertThat("LPG Banner should be displayed", orderDetailPage.verifyLpgBanner(), is(true));

        step("Click submit claim button");
        orderDetailPage.clickSubmitClaimBtn();

        step("Verify Lowest Price Guarantee page is displayed");
        Assertion.assertThat("Lowest Price Guarantee page is displayed", orderDetailPage.verifyLowestPriceGuaranteePage(), is(true));

        step("User able to open and close Read Before Claim");
        Assertion.assertThat("Read Before Claim opened and closed successfully", orderDetailPage.openAndCloseReadBeforeClaim(), is(true));

        step("Negative Case: User input a price higher than total payment");
        orderDetailPage.fillClaimDetails(invalidHighPrice, urlToCompare);
        Assertion.assertThat("Validation text for higher price is displayed", orderDetailPage.verifyHighPriceValidationError(), is(true));

        step("User input the final price and URL");
        orderDetailPage.fillClaimDetails(finalPrice, urlToCompare);

        step("User able to open and close See Guideline");
        Assertion.assertThat("See Guideline opened and closed successfully", orderDetailPage.verifySeeGuideline(), is(true));

        step("User attach screenshot valid case");
        orderDetailPage.uploadScreenshot();

        step("User checklist the TnC ");
        orderDetailPage.checkTnC();
        orderDetailPage.clickClaimNow();

        step("Open my order page");
        homePage.gotoHome();
        homePage.openMyOrderPage();
        orderListPage.openOrderDetailViaDeeplink(issuedOrderID);
        orderDetailPage.hidePromoBannerIfPresent();
        orderDetailPage.verifyLpgBanner();

        step("Asserting that status changed to waiting for approval");
        Assertion.assertThat("Claim success page displayed", orderDetailPage.statusChange(), is(true));

    }


    @DataProvider(name="testCheckOrderFromNonLogInUserToLogInDataProvider")
    public Object[][] testCheckOrderFromNonLogInUserToLogInDataProvider() {
        return getObjectParams("testCheckOrderFromNonLogInUserToLogIn");
    }

    @DataProvider(name="testCheckOrderHistoryLoginDataProvider")
    public Object[][] testCheckOrderHistoryLoginDataProvider() {
        return getObjectParams("testCheckOrderHistoryLogin");
    }

    @DataProvider(name="testAirportTransferCheckDetailOrderLoginDataProvider")
    public Object[][] testAirportTransferCheckDetailOrderLoginDataProvider() {
        return getObjectParams("testAirportTransferCheckDetailOrderLogin");
    }

    @DataProvider(name="testTrainCheckDetailOrderLoginDataProvider")
    public Object[][] testTrainCheckDetailOrderLoginDataProvider(){
        return getObjectParams("testTrainCheckDetailOrderLogin");
    }

    @DataProvider(name="testCheckCarRentalOrderDetailLoginDataProvider")
    public Object[][] testCheckCarRentalOrderDetailLoginDataProvider() {
        return getObjectParams("testCheckCarRentalOrderDetailLogin");
    }

    @DataProvider(name="testCheckCancelOrderLoginDataProvider")
    public Object[][] testCheckCancelOrderLoginDataProvider() {
        return getObjectParams("testCheckCancelOrderLogin");
    }

    @DataProvider(name="testCheckDeleteOrderLoginDataProvider")
    public Object[][] testCheckDeleteOrderLoginDataProvider() {
        return getObjectParams("testCheckDeleteOrderLogin");
    }

    @DataProvider(name="testCheckDeleteOrderGuestDataProvider")
    public Object[][] testCheckDeleteOrderGuestDataProvider() {
        return getObjectParams("testCheckDeleteOrderGuest");
    }

    @DataProvider(name="testCheckCancelOrderGuestDataProvider")
    public Object[][] testCheckCancelOrderGuestDataProvider() {
        return getObjectParams("testCheckCancelOrderGuest");
    }
    @DataProvider(name="testflightCheckOrderDetailLoginDataProvider")
    public Object[][] testflightCheckOrderDetailLoginDataProvider() {
        return getObjectParams("flightCheckOrderDetailLogin");
    }

    @DataProvider(name="testTrainCheckDetailOrderGuestDataProvider")
    public Object[][] testTrainCheckDetailOrderGuestDataProvider(){
        return getObjectParams("testTrainCheckDetailOrderGuest");
    }

    @DataProvider(name="testflightCheckOrderDetailGuestDataProvider")
    public Object[][] testflightCheckOrderDetailGuestDataProvider(){
        return getObjectParams("flightCheckOrderDetailGuest");
    }

    @DataProvider(name="checkTTDDetailOrderGuestDataProvider")
    public Object[][] checkTTDDetailOrderGuestDataProvider() {
        return getObjectParams("checkTTDDetailOrderGuest");
    }
}
