package com.tiket.web.dashboard.test.b2b_external;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.apiPage.HotelApi;
import com.tiket.model.Platform;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Session;
import com.tiket.pojo.b2b.corporate.EmployeeData;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.testbase.Configs;
import com.tiket.web.dWeb.page.accommodation.hotels.HotelRoomListPage;
import com.tiket.web.dWeb.page.flight.FlightProductReviewPage;
import com.tiket.web.dWeb.page.payment.PaymentPage;
import com.tiket.web.dashboard.page.b2bCorp_internal.*;
import com.tiket.web.dashboard.page.b2b_external.*;
import com.tiket.web.dashboard.page.b2b_external.reschedule_List.RescheduleListPageWeb;
import com.tiket.web.dashboard.page.common.InboxPageYopmail;
import com.tiket.web.dashboard.page.common.LoginB2CInDWEB;
import com.tiket.web.dashboard.page.common.LoginMemberAdmin;
import com.tiket.web.page.accommodation.hotels.BookingForm.HotelBookingFormPageWeb;
import com.tiket.web.page.accommodation.hotels.hotelLandingPage.HotelLandingPageWeb;
import com.tiket.web.page.common.home.HomePageWeb;
import com.tiket.web.page.common.home.LoginPageWeb;
import com.tiket.web.page.corporate.dashboard.CorporateDashboard;
import com.tiket.web.page.corporate.travel_plan.TravelPlanWeb;
import com.tiket.web.page.flight.bookingForm.FlightBookingFormPageWeb;
import com.tiket.web.page.flight.landingpage.FlightLandingPageWeb;
import com.tiket.web.page.flight.searchresultpage.FlightSearchResultPageWeb;
import com.tiket.web.page.order.OrderDetail.OrderDetailWeb;
import com.tiket.web.page.order.OrderList.OrderListWeb;
import com.tiket.web.page.platform.sphinx.refund.RefundPageWeb;
import org.hamcrest.CoreMatchers;
import org.openqa.selenium.WebDriver;
import com.tiket.apiPage.FlightApi;
import com.tiket.apiPage.CorporateApi;
import org.openqa.selenium.WebElement;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import javax.security.auth.login.FailedLoginException;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.tiket.core.CoreFunctions.*;
import static com.tiket.testdata.TestData.getObjectParams;
import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;


public class B2BExternalDashboardTest extends BaseTest {
    @Author(names = {"Faj"})
    @Module(name = "Login")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test
    public void LoginAdmin() throws InterruptedException, FailedLoginException {
        Map<String, String> param = getParam("LoginAdmin");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePage<?> homePage = new HomePage<>(session);
        LoginB2CInDWEB<?> loginB2CInDWEB = new LoginB2CInDWEB<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");

        step("Login via cookie injection and open corporate dashboard");
        homePage.loginViaCookieAndOpenCorporateDashboard(email, password);

        step("Check if user has successfully logged in");
        boolean isUserLoggedIn = loginB2CInDWEB.checkIfLoggedIn();

        step("Close tutorial banner if exists");
        loginB2CInDWEB.closeTutorialbanner();

        step("Assert that user is logged in");
        Assertion.assertThat("Is user logged in", isUserLoggedIn, is(true));

        step("Log out From Admin dashboard");
        homePage.logoutFromAdminDashboard();
    }

    @Author(names = {"Faj"})
    @Module(name = "Login")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test
    public void LoginApprover() throws InterruptedException, FailedLoginException {
        Map<String, String> param = getParam("LoginApprover");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePage<?> homePage = new HomePage<>(session);
        LoginB2CInDWEB<?> loginB2CInDWEB = new LoginB2CInDWEB<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");

        step("Login via cookie injection and open corporate dashboard");
        homePage.loginViaCookieAndOpenCorporateDashboard(email, password);

        step("Check if user has successfully logged in");
        boolean isUserLoggedIn = loginB2CInDWEB.checkIfLoggedIn();

        step("Close tutorial banner if exists");
        loginB2CInDWEB.closeTutorialbanner();

        step("Assert that user is logged in");
        Assertion.assertThat("Is user logged in", isUserLoggedIn, is(true));

        step("Log out From Admin dashboard");
        homePage.logoutFromAdminDashboard();
    }

    @Author(names = {"Faj"})
    @Module(name = "Login")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test
    public void LoginTraveler() throws InterruptedException, FailedLoginException {
        Map<String, String> param = getParam("LoginTraveler");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePage<?> homePage = new HomePage<>(session);
        LoginB2CInDWEB<?> loginB2CInDWEB = new LoginB2CInDWEB<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");

        step("Login via cookie injection");
        homePage.loginViaCookie(email, password);

        step("Check if user has successfully logged in");
        boolean isUserLoggedIn = loginB2CInDWEB.checkIfUserInB2CPage();

        step("Assert that user is logged in");
        Assertion.assertThat("Is user logged in", isUserLoggedIn, is(true));

        step("Log out From Admin dashboard");
        homePage.logoutFromDweb();
    }

    @Author(names = {"Faj"})
    @Module(name = "Train")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test
    public void CheckTrainVoucher() throws InterruptedException {
        Map<String, String> param = getParam("CheckTrainVoucher");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        InboxPageYopmail<?> inboxPage = new InboxPageYopmail<>(session);

        String email = param.get("email");
        String password = param.get("password");

        step("Go to Yopmail");
        inboxPage.goToYopmail();

        step("Input email");
        inboxPage.inputEmail(email);

        step("Go to inbox");
        inboxPage.goToInbox();

        step("Find the mail");
        ArrayList<String> passengerNames = new ArrayList<>();
        passengerNames.add("Tuan Louietest");
        Optional<HashMap<String, Object>> content = Optional.ofNullable(inboxPage.iterateToFindEmail("Monday, February 26, 2024 5:44:03 PM", passengerNames));

        step("Assert that content is not empty");
        Assertion.assertThat("Content is not empty", content.isPresent(), is(true));
    }

    @Author(names = {"Faj"})
    @Module(name = "Train")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test(dataProvider = "OrderTrain_1paxDataProvider")
    public void OrderTrain_1pax(Map<String, String> param) throws Exception {
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePage<?> homePage = new HomePage<>(session);
        LoginB2CInDWEB<?> loginB2CInDWEB = new LoginB2CInDWEB<>(session);
        TrainOrderPage<?> trainPage = new TrainOrderPage<>(session);
        PurchaseListPage purchaseListPage = new PurchaseListPage(session);
        BookingListPage bookingListPage = new BookingListPage(session);
        InboxPageYopmail<?> inboxPage = new InboxPageYopmail<>(session);

        String arrivalStationCode = param.get("arrivalStationCode");
        String departureStationCode = param.get("departureStationCode");
        String emailTraveler = param.get("emailTraveler");
        String passwordTraveler = param.get("passwordTraveler");
        String otp = param.get("otp");
        int maturePassenger = Integer.parseInt(param.get("maturePassenger"));
        int babyPassenger = Integer.parseInt(param.get("babyPassenger"));

        step("Login as traveler via cookie injection");
        homePage.loginViaCookie(emailTraveler, passwordTraveler);

        step("Check if user has successfully logged in");
        boolean isUserLoggedInAsTraveler = loginB2CInDWEB.checkIfUserInB2CPage();

        step("Assert that user is logged in");
        Assertion.assertThat("Is user logged in", isUserLoggedInAsTraveler, is(true));

        step("Click the Train Vertical");
        trainPage.GoToTrainPage();

        step("Set Depart Station");
        trainPage.setDeparture(departureStationCode);

        step("Set Destination Station");
        trainPage.setArrival(arrivalStationCode);

        step("Set the date");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        trainPage.setDate(daysToDepartureDate);

        step("Set Passenger");
        trainPage.setPassengers(maturePassenger, babyPassenger);

        step("Search for Train");
        List<WebElement> trainTickets = trainPage.searchTrain();

        step("Click the first one available ticket");
        trainPage.clickTrainCard(1);

        step("Input passengers");
        String name = trainPage.name();
        trainPage.fillPassengerDetail1(name);

        step("Continue to Payment Page");
        trainPage.continuePayment();

        step("Get Train Order ID from My Orders");
        String orderID = trainPage.getOrderID();

        step("Confirm Payment");
        trainPage.confirmPayment();

        step("Go to My Orders");
        trainPage.goToMyOrders();

        step("Get the order Details");
        HashMap<String, Object> orderDetailsMap = trainPage.getOrderDetails(emailTraveler, orderID, maturePassenger, babyPassenger);

        step("Login as admin via cookie injection and open corporate dashboard");
        String emailAdmin = param.get("emailAdmin");
        String passwordAdmin = param.get("passwordAdmin");
        homePage.loginViaCookieAndOpenCorporateDashboard(emailAdmin, passwordAdmin);

        step("Check if user has successfully logged in");
        boolean isUserLoggedIn = loginB2CInDWEB.checkIfLoggedIn();

        step("Close tutorial banner if exists");
        loginB2CInDWEB.closeTutorialbanner();

        step("Assert that user is logged in");
        Assertion.assertThat("Is user logged in", isUserLoggedIn, is(true));

        step("Go to the Purchase List");
        purchaseListPage.goToPurchaseListPage();

        step("Find Train Order ID in Purchase List");
        purchaseListPage.searchByOrderID(orderID);

        step("Check if Train Purchase Card Exists");
        boolean purchaseExists = purchaseListPage.checkIfSearchedOrderExists(orderID);

        step("Asset that purchase order exists");
        Assertion.assertThat("Does train purchase order exist", purchaseExists, is(true));

        step("Approve purchase");
        purchaseListPage.approvePurchase(orderID);

        step("Check if Order has been Removed From Purchase List");
        boolean isOrderDisplayed = purchaseListPage.checkIfOrderRemovedFromPurchaseList(orderID);

        step("Go to Booking List");
        Assertion.assertThat("Is Booking removed", isOrderDisplayed, is(false));
        purchaseListPage.headToBookingListPage();

        step("Send keys to 'Search By Order ID' Text field");
        bookingListPage.setSearchTextfield(orderID);

        step("Hit 'Search' Button");
        bookingListPage.hitSearchButton();

        step("Get data from Booking Card in B2B Dashboard");
        HashMap<String, Object> orderDetailDashboardMap = bookingListPage.getOrderDetailsB2BDashboard();

        step("Convert amount to Integer");
        int totalPriceDashboard = bookingListPage.convertAmountToInt(String.valueOf(orderDetailDashboardMap.get("totalPrice")));

        step("Print Order Details from 'Your Orders'");
        trainPage.printMap(orderDetailsMap);

        step("Print Order Details from Booking List External Dashboard");
        trainPage.printMap(orderDetailDashboardMap);

        step("Assert that data is consistent");
        Assertion.assertThat("Order ID", orderDetailDashboardMap.get("orderIDFromDetail"), is(orderDetailsMap.get("orderIDFromDetail")));
        Assertion.assertThat("Email", orderDetailDashboardMap.get("email"), is(is(orderDetailsMap.get("email"))));
        Assertion.assertThat("Passengers", orderDetailDashboardMap.get("passengers"), is(orderDetailsMap.get("passengers")));
        Assertion.assertThat("Total Price", orderDetailDashboardMap.get("totalPrice"), is(orderDetailsMap.get("totalPrice")));
        Assertion.assertThat("Train Name", String.valueOf(orderDetailsMap.get("trainName")).contains(String.valueOf(orderDetailDashboardMap.get("trainName"))), is(true));
        Assertion.assertThat("Train Class", orderDetailDashboardMap.get("trainClass").equals(orderDetailsMap.get("trainClass")), is(true));
        Assertion.assertThat("Departure Station", orderDetailDashboardMap.get("departureStation"), is(orderDetailsMap.get("departureStation")));
        Assertion.assertThat("Arrival Station", orderDetailDashboardMap.get("arrivalStation"), is(orderDetailsMap.get("arrivalStation")));
        Assertion.assertThat("Departure Date", String.valueOf(orderDetailDashboardMap.get("departureDate")).contains(String.valueOf(orderDetailsMap.get("departureDate"))), is(true));

        step("Query the data from Corporote DB");
        HashMap<String, Object> corporateDBData = bookingListPage.getDataDB(orderID);

        step("Get the property from DB");
        String totalPrice_DB = String.valueOf(corporateDBData.get("totalPrice"));
        totalPrice_DB = totalPrice_DB.replace(".00", "");
        int totalPrice_DB_int = Integer.parseInt(totalPrice_DB);

        step("Assert data in DB is consistent");
        Assertion.assertThat("TotalPrice", totalPrice_DB_int, is(totalPriceDashboard));
        Assertion.assertThat("Product Type", String.valueOf(corporateDBData.get("productType")), is("train"));
        Assertion.assertThat("Payment Status", String.valueOf(corporateDBData.get("paymentStatus")), is("approved"));

        step("Click download csv");
        Assertion.assertThat("Is downloading booking list", bookingListPage.downloadCSV(), is(true));

        step("Get the downloaded csv");
        String dateStringFileFormat = CoreFunctions.getCurrentDateDD_MM_YYYY();
        File fileCSV = CoreFunctions.getDownloadedFileMacOS("booking_lists_" + dateStringFileFormat);

        step("Get CSV Content");
        List<List<String>> csvContent = bookingListPage.getCSVContent(fileCSV);

        step("Check the number of Rows Folder");
        int totalBookingCSV = CoreFunctions.checkNoOfRowsCSV(csvContent);

        step("Check orderID exists in CSV");
        boolean orderIDExistInCSV = bookingListPage.checkOrderIDInCSV(orderID, csvContent);

        Assertion.assertThat("Order ID is in the CSV", orderIDExistInCSV, is(true));

        step("Go to Yopmail");
        inboxPage.goToYopmail();

        step("Input email");
        inboxPage.inputEmail(emailTraveler);

        step("Go to inbox");
        inboxPage.goToInbox();

        step("Find the mail");
        ArrayList<String> passengerNames = (ArrayList<String>) orderDetailDashboardMap.get("passengers");
        Optional<HashMap<String, Object>> content = Optional.ofNullable(inboxPage.iterateToFindEmail(orderID, passengerNames));

        step("Assert that content is not empty");
        Assertion.assertThat("Content is not empty", content.isPresent(), is(true));

        step("Assert that data in Voucher is consistent");
        HashMap<String, Object> voucherEmaiLDetail = content.orElseThrow();
        Assertion.assertThat("Order ID", voucherEmaiLDetail.get("orderID"), is(orderDetailDashboardMap.get("orderIDFromDetail")));
        Assertion.assertThat("Passenger", voucherEmaiLDetail.get("allPassengerNamesExist"), is(true));
        Assertion.assertThat("Departure Station", voucherEmaiLDetail.get("departureStation"), is(orderDetailDashboardMap.get("departureStation")));
        Assertion.assertThat("Arrival Station", voucherEmaiLDetail.get("arrivalStation"), is(orderDetailDashboardMap.get("arrivalStation")));
        Assertion.assertThat("Arrival Station", voucherEmaiLDetail.get("departureDate"), is(orderDetailDashboardMap.get("departureDate")));
    }

    @DataProvider(name = "OrderTrain_1paxDataProvider")
    public Object[][] OrderTrain_1paxDataProvider() {
        return getObjectParams("OrderTrain_1pax");
    }

    @Author(names = {"Faj"})
    @Module(name = "Booking List")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test
    public void AdminSearchBookingListByOrderID() throws InterruptedException, FailedLoginException {
        Map<String, String> param = getParam("AdminSearchBookingListByOrderID");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        BookingListPage<?> bookingListPage = new BookingListPage<>(session);
        LoginB2CInDWEB<?> loginB2CInDWEB = new LoginB2CInDWEB<>(session);
        HomePage<?> homePage = new HomePage<>(session);

        String orderID = param.get("orderID");
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");

        step("Login via cookie injection and open corporate dashboard");
        homePage.loginViaCookieAndOpenCorporateDashboard(email, password);

        step("Close tutorial banner if exists");
        loginB2CInDWEB.closeTutorialbanner();

        step("Go to Booking List");
        bookingListPage.goToBookingListPage();

        step("Close Onboarding Dialog");
        bookingListPage.closeOnBoardingDialog();

        step("Send keys to 'Search By Order ID' Text field");
        bookingListPage.setSearchTextfield(orderID);

        step("Hit 'Search' Button");
        bookingListPage.hitSearchButton();

        boolean isOrderIdExist = bookingListPage.checkIfSearchedOrderExists(orderID);

        step("Check if Searched Booking shows up");
        Assertion.assertThat("Searched booking ID exists", isOrderIdExist, is(true));

        step("Log out From Admin dashboard");
        homePage.logoutFromAdminDashboard();
    }

    @Author(names = {"Faj"})
    @Module(name = "Booking List")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test
    public void AdminSeeTotalAmountInBookingListFlight() throws InterruptedException, FailedLoginException {
        Map<String, String> param = getParam("AdminSeeTotalAmountInBookingListFlight");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        BookingListPage<?> bookingListPage = new BookingListPage<>(session);
        LoginB2CInDWEB<?> loginB2CInDWEB = new LoginB2CInDWEB<>(session);
        HomePage<?> homePage = new HomePage<>(session);

        String orderID = param.get("orderID");
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");

        step("Login via cookie injection and open corporate dashboard");
        homePage.loginViaCookieAndOpenCorporateDashboard(email, password);

        step("Close tutorial banner if exists");
        loginB2CInDWEB.closeTutorialbanner();

        step("Go to Booking List");
        bookingListPage.goToBookingListPage();

        step("Send keys to 'Search By Order ID' Text field");
        bookingListPage.setSearchTextfield(orderID);

        step("Hit 'Search' Button");
        bookingListPage.hitSearchButton();

        step("Get Total Price In Header");
        String totalPriceInHeader = bookingListPage.getTotalPriceInHeader();

        step("Click Price Detail Tab");
        bookingListPage.clickPriceDetailsTab();

        step("Get Total Price In Price Details");
        String totalPriceInDetail = bookingListPage.getTotalPriceInPriceDetails();

        step("Log out From Admin dashboard");
        homePage.logoutFromAdminDashboard();

        step("Assert that they are equal");
        Assertion.assertThat("Is price consistent", totalPriceInDetail, is(totalPriceInHeader));
    }

    @Author(names = {"Faj"})
    @Module(name = "Booking List")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test
    public void AdminSeeTotalAmountInBookingListHotel() throws InterruptedException, FailedLoginException {
        Map<String, String> param = getParam("AdminSeeTotalAmountInBookingListHotel");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        BookingListPage<?> bookingListPage = new BookingListPage<>(session);
        LoginB2CInDWEB<?> loginB2CInDWEB = new LoginB2CInDWEB<>(session);
        HomePage<?> homePage = new HomePage<>(session);

        String orderID = param.get("orderID");
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");

        step("Login via cookie injection and open corporate dashboard");
        homePage.loginViaCookieAndOpenCorporateDashboard(email, password);

        step("Close tutorial banner if exists");
        loginB2CInDWEB.closeTutorialbanner();

        step("Go to Booking List");
        bookingListPage.goToBookingListPage();

        step("Close Onboarding Dialog");
        bookingListPage.closeOnBoardingDialog();

        step("Send keys to 'Search By Order ID' Text field");
        bookingListPage.setSearchTextfield(orderID);

        step("Hit 'Search' Button");
        bookingListPage.hitSearchButton();

        step("Get Total Price In Header");
        String totalPriceInHeader = bookingListPage.getTotalPriceInHeader();

        step("Click Price Detail Tab");
        bookingListPage.clickPriceDetailsTab();

        step("Get Total Price In Price Details");
        String totalPriceInDetail = bookingListPage.getTotalPriceInPriceDetails();

        step("Open Home Page");
        homePage.gotoHome();

        step("Log out");
        homePage.logoutFromDweb();

        step("Assert that they are equal");
        Assertion.assertThat("Is price consistent", totalPriceInDetail, is(totalPriceInHeader));
    }

    @Author(names = {"Faj"})
    @Module(name = "Booking List")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test(dataProvider = "AdminDownloadBookingListDataProvider")
    public void AdminDownloadBookingList(Map<String, String> param) throws Exception {
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        BookingListPage<?> bookingListPage = new BookingListPage<>(session);
        LoginB2CInDWEB<?> loginB2CInDWEB = new LoginB2CInDWEB<>(session);
        HomePage<?> homePage = new HomePage<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");

        step("Login via cookie injection and open corporate dashboard");
        homePage.loginViaCookieAndOpenCorporateDashboard(email, password);

        step("Check if user has successfully logged in");
        boolean isUserLoggedIn = loginB2CInDWEB.checkIfLoggedIn();

        step("Close tutorial banner if exists");
        loginB2CInDWEB.closeTutorialbanner();

        step("Go to Booking List Page");
        bookingListPage.goToBookingListPage();

        step("Click download csv");
        Assertion.assertThat("Is downloading booking list", bookingListPage.downloadCSV(), is(true));

        step("Log out From Admin dashboard");
        homePage.logoutFromAdminDashboard();

    }

    @DataProvider(name = "AdminDownloadBookingListDataProvider")
    public Object[][] AdminDownloadBookingListDataProvider() {
        return getObjectParams("AdminDownloadBookingList");
    }

    @Author(names = {"Faj"})
    @Module(name = "Purchase List")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test(dataProvider = "OpenExpiredPurchaseListDataProvider")
    public void OpenExpiredPurchaseList(Map<String, String> param) throws Exception {
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePage<?> homePage = new HomePage<>(session);
        LoginB2CInDWEB<?> loginB2CInDWEB = new LoginB2CInDWEB<>(session);
        PurchaseListPage purchaseListPage = new PurchaseListPage(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");

        step("Login via cookie injection and open corporate dashboard");
        homePage.loginViaCookieAndOpenCorporateDashboard(email, password);

        step("Check if user has successfully logged in");
        boolean isUserLoggedInAsTraveler = loginB2CInDWEB.checkIfLoggedIn();

        step("Assert that user is logged in");
        Assertion.assertThat("Is user logged in", isUserLoggedInAsTraveler, is(true));

        step("Go to purchase list");
        purchaseListPage.goToPurchaseListPage();

        step("Open expired purchase list");
        purchaseListPage.openExpiredPurchaseList();

        step("Check if Expired Purchase List is opened");
        boolean isExpiredPurchaseListOpened = purchaseListPage.checkExpiredPurchaseList();

        step("Assert that user is already in the expired purchase list");
        Assertion.assertThat("Is expired purchase list opened", isExpiredPurchaseListOpened, is(true));

        step("Log out From Admin dashboard");
        homePage.logoutFromAdminDashboard();
    }

    @DataProvider(name = "OpenExpiredPurchaseListDataProvider")
    public Object[][] OpenExpiredPurchaseListDataProvider() {
        return getObjectParams("OpenExpiredPurchaseList");
    }

    @Author(names = {"Faj"})
    @Module(name = "Login")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test
    public void AddEmployeeViaDashboard() throws Exception {
        Map<String, String> param = getParam("AddEmployeeViaDashboard");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePage<?> homePage = new HomePage<>(session);
        LoginB2CInDWEB<?> loginB2CInDWEB = new LoginB2CInDWEB<>(session);
        EmployeeListPage<?> employeeListPage = new EmployeeListPage<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        String name = CoreFunctions.randomName();

        step("Login via cookie injection and open corporate dashboard");
        homePage.loginViaCookieAndOpenCorporateDashboard(email, password);

        step("Check if user has successfully logged in");
        boolean isUserLoggedIn = loginB2CInDWEB.checkIfLoggedIn();

        step("Close tutorial banner if exists");
        loginB2CInDWEB.closeTutorialbanner();

        step("Assert that user is logged in");
        Assertion.assertThat("Is user logged in", isUserLoggedIn, is(true));

        step("Open the Employee List");
        employeeListPage.goToEmployeeListPage();

        step("Click Add Employee");
        employeeListPage.clickAddEmployee();

        step("Fill the new Employee Details");
        employeeListPage.fillEmployeeDetails("Traveler", name);

        step("Click Submit");
        employeeListPage.clickSubmit();

        step("Search newly Added Employee");
        employeeListPage.searchNewEmployee(name);

        step("Check if the searched Name exists");
        boolean isNameExists = employeeListPage.checkIfExists(name);

        step("Assert that the newly added employee can be found");
        Assertion.assertThat("Is new employee exists", isNameExists, is(true));

        step("Download CSV");
        employeeListPage.downloadCSV();

        step("Open Home Page");
        homePage.gotoHome();

        step("Log out");
        homePage.logoutFromDweb();

    }

    @Author(names = {"Faj"})
    @Module(name = "Login")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test(dataProvider = "LoginCorporateInternalDashboard_DataProvider")
    public void LoginCorporateInternalDashboard(Map<String, String> param) throws Exception {
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<>(session);
        CorporateDataPage<?> corporateDataPage = new CorporateDataPage<>(session);

        String email = param.get("email");
        String password = param.get("password");

        step("Login via cookie injection");
        loginMemberAdmin.loginViaCookie(email, password);

        step("Go to Corporate Internal Dashboard");
        loginMemberAdmin.goToDashboard();

        step("Check if user is logged in");
        boolean isLoggedInCRP = corporateDataPage.assertUserIsLoggedIn(email);

        step("Logout");
        loginMemberAdmin.logout();
    }

    @DataProvider(name = "LoginCorporateInternalDashboard_DataProvider")
    public Object[][] LoginCorporateInternalDashboard_DataProvider() {
        return getObjectParams("LoginCorporateInternalDashboard");
    }

    @Author(names = {"Faj"})
    @Module(name = "Registered Corporate")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test(dataProvider = "OpenRegisteredCorporate_DataProvider")
    public void OpenRegisteredCorporate(Map<String, String> param) throws Exception {
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<>(session);
        CorporateDataPage<?> corporateDataPage = new CorporateDataPage<>(session);
        CorporateInternalDashboardSidebar<?> corporateInternalDashboardSidebar = new CorporateInternalDashboardSidebar<>(session);
        RegisteredCorporatePage<?> registeredCorporatePage = new RegisteredCorporatePage<>(session);

        String email = param.get("email");
        String password = param.get("password");

        step("Login via cookie injection");
        loginMemberAdmin.loginViaCookie(email, password);

        step("Go to Corporate Internal Dashboard");
        loginMemberAdmin.goToDashboard();

        step("Check if user is logged in");
        boolean isLoggedInCRP = corporateDataPage.assertUserIsLoggedIn(email);

        step("Expand the Corporate Information Sidebar");
        corporateInternalDashboardSidebar.expandCorporateInformation();

        step("Click on Registered Corporate Option");
        corporateInternalDashboardSidebar.openRegisteredCorporate();

        step("Check if page is opened successfully");
        boolean isPageOpened = registeredCorporatePage.isPageOpened();

        step("Logout");
        loginMemberAdmin.logout();
    }

    @DataProvider(name = "OpenRegisteredCorporate_DataProvider")
    public Object[][] OpenRegisteredCorporate_DataProvider() {
        return getObjectParams("OpenRegisteredCorporate");
    }

    @Author(names = {"Faj"})
    @Module(name = "Corporate Data")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test(dataProvider = "SearchCorporateData_DataProvider")
    public void SearchCorporateData(Map<String, String> param) throws Exception {
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<>(session);
        CorporateDataPage<?> corporateDataPage = new CorporateDataPage<>(session);
        HomePage<?> homePage = new HomePage<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String corporateName = param.get("corporateName");

        step("Login via cookie injection");
        loginMemberAdmin.loginViaCookie(email, password);

        step("Go to Corporate Internal Dashboard");
        loginMemberAdmin.goToDashboard();

        step("Check if user is logged in");
        boolean isLoggedInCRP = corporateDataPage.checkUserIsLoggedIn();

        step("Assert that user is is logged in to corporate dashboard");
        Assertion.assertThat("User is logged in", isLoggedInCRP, is(true));

        step("Click ON corporate information");
        corporateDataPage.clickCorporateInformation();

        step("Click ON corporate data");
        corporateDataPage.clickCorporateData();

        step("Send corporate name to search");
        corporateDataPage.inputSearch(corporateName);

        step("Check if search is successful");
        boolean isCrpExists = corporateDataPage.checkIfCorporateExists(corporateName);

        Assertion.assertThat("Verify Search corporate name is appeared ",isCrpExists,is(true));

        step("Log out From Admin dashboard");
        homePage.logoutFromAdminDashboard();
    }

    @DataProvider(name = "SearchCorporateData_DataProvider")
    public Object[][] SearchCorporateData_DataProvider() {
        return getObjectParams("SearchCorporateData");
    }

    @Author(names = {"Faj"})
    @Module(name = "Corporate Data")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test(dataProvider = "DownloadCorporateData_DataProvider")
    public void DownloadCorporateData(Map<String, String> param) throws Exception {
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<>(session);
        CorporateDataPage<?> corporateDataPage = new CorporateDataPage<>(session);
        HomePage<?> homePage = new HomePage<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String corporateName = param.get("corporateName");

        step("Login via cookie injection");
        loginMemberAdmin.loginViaCookie(email, password);

        step("Go to Corporate Internal Dashboard");
        loginMemberAdmin.goToDashboard();

        step("Check if user is logged in");
        boolean isLoggedInCRP = corporateDataPage.checkUserIsLoggedIn();

        step("Assert that user is is logged in to corporate dashboard");
        Assertion.assertThat("User is logged in", isLoggedInCRP, is(true));

        step("Click ON corporate information");
        corporateDataPage.clickCorporateInformation();

        step("Click ON corporate data");
        corporateDataPage.clickCorporateData();

        step("Click download CSV");
        corporateDataPage.downloadCSV();

        step("Log out From Admin dashboard");
        loginMemberAdmin.logout();

    }

    @DataProvider(name = "DownloadCorporateData_DataProvider")
    public Object[][] DownloadCorporateData_DataProvider() {
        return getObjectParams("DownloadCorporateData");
    }

    @Author(names = {"Ashish"})
    @Module(name = "Employee List")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test
    public void OpenEmployeeList() throws Exception {
        Map<String, String> param = getParam("OpenEmployeeList");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePage<?> homePage = new HomePage<>(session);
        LoginB2CInDWEB<?> loginB2CInDWEB = new LoginB2CInDWEB<>(session);
        EmployeeListPage<?> employeeListPage = new EmployeeListPage<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        String name = CoreFunctions.randomName();

        step("Login via cookie injection and open corporate dashboard");
        homePage.loginViaCookieAndOpenCorporateDashboard(email, password);

        step("Check if user has successfully logged in");
        boolean isUserLoggedIn = loginB2CInDWEB.checkIfLoggedIn();

        step("Close tutorial banner if exists");
        loginB2CInDWEB.closeTutorialbanner();

        step("Assert that user is logged in");
        Assertion.assertThat("Is user logged in", isUserLoggedIn, is(true));

        step("Open the Employee List");
        employeeListPage.goToEmployeeListPage();

        step("Log out From Admin dashboard");
        homePage.logoutFromAdminDashboard();
    }

    @Author(names = {"Ashish"})
    @Module(name = "Employee List")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test
    public void DownloadEmployeeList() throws Exception {
        Map<String, String> param = getParam("DownloadEmployeeList");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePage<?> homePage = new HomePage<>(session);
        LoginB2CInDWEB<?> loginB2CInDWEB = new LoginB2CInDWEB<>(session);
        EmployeeListPage<?> employeeListPage = new EmployeeListPage<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");

        step("Login via cookie injection and open corporate dashboard");
        homePage.loginViaCookieAndOpenCorporateDashboard(email, password);

        step("Check if user has successfully logged in");
        boolean isUserLoggedIn = loginB2CInDWEB.checkIfLoggedIn();

        step("Close tutorial banner if exists");
        loginB2CInDWEB.closeTutorialbanner();

        step("Assert that user is logged in");
        Assertion.assertThat("Is user logged in", isUserLoggedIn, is(true));

        step("Open the Employee List");
        employeeListPage.goToEmployeeListPage();

        step("Click download list");
        Assertion.assertThat("Is downloading booking list", employeeListPage.downloadList(), is(true));

        step("Log out From Admin dashboard");
        homePage.logoutFromAdminDashboard();
    }

    @Author(names = {"Ashish"})
    @Module(name = "Purchase List")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test(dataProvider = "OpenPurchaseListDataProvider")

    public void OpenPurchaseList(Map<String, String> param) throws Exception {
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePage<?> homePage = new HomePage<>(session);
        LoginB2CInDWEB<?> loginB2CInDWEB = new LoginB2CInDWEB<>(session);
        PurchaseListPage purchaseListPage = new PurchaseListPage(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");

        step("Login via cookie injection and open corporate dashboard");
        homePage.loginViaCookieAndOpenCorporateDashboard(email, password);

        step("Check if user has successfully logged in");
        boolean isUserLoggedInAsTraveler = loginB2CInDWEB.checkIfLoggedIn();

        step("Assert that user is logged in");
        Assertion.assertThat("Is user logged in", isUserLoggedInAsTraveler, is(true));

        step("Go to purchase list");
        purchaseListPage.goToPurchaseListPage();

        step("Check if Purchase List is opened");
        boolean isPurchaseListOpened = purchaseListPage.isPurchaseListOpened();

        step("Assert that user is already in the expired purchase list");
        Assertion.assertThat("Is expired purchase list opened", isPurchaseListOpened, is(true));

        step("Log out From Admin dashboard");
        homePage.logoutFromAdminDashboard();
    }

    @DataProvider(name = "OpenPurchaseListDataProvider")
    public Object[][] OpenPurchaseListDataProvider() {
        return getObjectParams("OpenPurchaseList");
    }

    @Author(names = {"Ashish"})
    @Module(name = "Login")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test
    public void OpenCorporateInformation() throws Exception {
        Map<String, String> param = getParam("OpenCorporateInformation");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePage<?> homePage = new HomePage<>(session);
        LoginB2CInDWEB<?> loginB2CInDWEB = new LoginB2CInDWEB<>(session);
        CorporateExternalDashboardSidebar<?> CorporateExternalDashboardSidebar = new CorporateExternalDashboardSidebar<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");

        step("Login via cookie injection and open corporate dashboard");
        homePage.loginViaCookieAndOpenCorporateDashboard(email, password);

        step("Check if user has successfully logged in");
        boolean isUserLoggedIn = loginB2CInDWEB.checkIfLoggedIn();

        step("Assert that user is logged in");
        Assertion.assertThat("Is user logged in", isUserLoggedIn, is(true));

        step("Open the Corporate Information");
        CorporateExternalDashboardSidebar.goToCorporateInformationPage();

        step("Log out From Admin dashboard");
        homePage.logoutFromAdminDashboard();
    }

    @Author(names = {"Ashish"})
    @Module(name = "Login")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test(dataProvider = "GoToB2CLandingPage_DataProvider")
    public void GoToB2CLandingPage(Map<String, String> param) throws Exception {
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePage<?> homePage = new HomePage<>(session);
        LoginB2CInDWEB<?> loginB2CInDWEB = new LoginB2CInDWEB<>(session);
        CorporateExternalDashboardSidebar<?> CorporateExternalDashboardSidebar = new CorporateExternalDashboardSidebar<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");

        step("Login via cookie injection and open corporate dashboard");
        homePage.loginViaCookieAndOpenCorporateDashboard(email, password);

        step("Check if user has successfully logged in");
        boolean isUserLoggedIn = loginB2CInDWEB.checkIfLoggedIn();

        step("Assert that user is logged in");
        Assertion.assertThat("Is user logged in", isUserLoggedIn, is(true));

        step("Click on Tiket.com Page");
        CorporateExternalDashboardSidebar.goToB2C();

        step("Log out From Admin dashboard");
        homePage.logoutFromAdminDashboard();
    }

    @DataProvider(name = "GoToB2CLandingPage_DataProvider")
    public Object[][] GoToB2CLandingPage_DataProvider() {
        return getObjectParams("GoToB2CLandingPage");
    }

    @Author(names = {"Ashish"})
    @Module(name = "Refund List")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test
    public void OpenRefundList() throws Exception {
        Map<String, String> param = getParam("OpenRefundList");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePage<?> homePage = new HomePage<>(session);
        LoginB2CInDWEB<?> loginB2CInDWEB = new LoginB2CInDWEB<>(session);
        CorporateExternalDashboardSidebar<?> CorporateExternalDashboardSidebar = new CorporateExternalDashboardSidebar<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");

        step("Login via cookie injection and open corporate dashboard");
        homePage.loginViaCookieAndOpenCorporateDashboard(email, password);

        step("Check if user has successfully logged in");
        boolean isUserLoggedIn = loginB2CInDWEB.checkIfLoggedIn();

        step("Assert that user is logged in");
        Assertion.assertThat("Is user logged in", isUserLoggedIn, is(true));

        step("Open the Refund List");
        CorporateExternalDashboardSidebar.goToRefundList();

        step("Log out From Admin dashboard");
        homePage.logoutFromAdminDashboard();
    }

    @Author(names = {"Ashish"})
    @Module(name = "Reschedule List")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test(dataProvider = "OpenRescheduleList_DataProvider")
    public void OpenRescheduleList(Map<String, String> param) throws Exception {
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePage<?> homePage = new HomePage<>(session);
        LoginB2CInDWEB<?> loginB2CInDWEB = new LoginB2CInDWEB<>(session);
        CorporateExternalDashboardSidebar<?> CorporateExternalDashboardSidebar = new CorporateExternalDashboardSidebar<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");

        step("Login via cookie injection and open corporate dashboard");
        homePage.loginViaCookieAndOpenCorporateDashboard(email, password);

        step("Check if user has successfully logged in");
        boolean isUserLoggedIn = loginB2CInDWEB.checkIfLoggedIn();

        step("Assert that user is logged in");
        Assertion.assertThat("Is user logged in", isUserLoggedIn, is(true));

        step("Open the Reschedule List");
        CorporateExternalDashboardSidebar.goToRescheduleList();

        step("Log out From Admin dashboard");
        homePage.logoutFromAdminDashboard();
    }

    @DataProvider(name = "OpenRescheduleList_DataProvider")
    public Object[][] OpenRescheduleList_DataProvider() {
        return getObjectParams("OpenRescheduleList");
    }

    @Author(names = {"Ashish"})
    @Module(name = "Raise Deposit Request")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test(dataProvider = "RaiseDepositRequest_DataProvider")
    public void RaiseDepositRequest(Map<String, String> param) throws Exception {
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<>(session);
        CorporateDataPage<?> corporateDataPage = new CorporateDataPage<>(session);
        CorporateInternalDashboardSidebar<?> corporateInternalDashboardSidebar = new CorporateInternalDashboardSidebar<>(session);
        RegisteredCorporatePage<?> registeredCorporatePage = new RegisteredCorporatePage<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String amount = param.get("amount");
        String filePath = param.get("filePath");

        step("Login via cookie injection");
        loginMemberAdmin.loginViaCookie(email, password);

        step("Go to Corporate Internal Dashboard");
        loginMemberAdmin.goToDashboard();

        step("Check if user is logged in");
        boolean isLoggedInCRP = corporateDataPage.assertUserIsLoggedIn(email);

        step("Expand the Corporate Payment Preference");
        corporateInternalDashboardSidebar.expandCorporatePaymentPreference();

        step("Enter Raise Deposit Request Flow");
        corporateInternalDashboardSidebar.depositFlow();

        step("Enter Request Details");
        corporateInternalDashboardSidebar.enterDetails(amount, filePath);

        step("Check if page is opened successfully");
        boolean isPageOpened = registeredCorporatePage.isPageOpened();

        step("Logout");
        loginMemberAdmin.logout();
    }

    @DataProvider(name = "RaiseDepositRequest_DataProvider")
    public Object[][] RaiseDepositRequest_DataProvider() {
        return getObjectParams("RaiseDepositRequest");
    }

    @Author(names = {"Ashish"})
    @Module(name = "Approve Deposit Request")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test(dataProvider = "ApproveDepositRequest_DataProvider")
    public void ApproveDepositRequest(Map<String, String> param) throws Exception {
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<>(session);
        CorporateDataPage<?> corporateDataPage = new CorporateDataPage<>(session);
        CorporateInternalDashboardSidebar<?> corporateInternalDashboardSidebar = new CorporateInternalDashboardSidebar<>(session);
        RegisteredCorporatePage<?> registeredCorporatePage = new RegisteredCorporatePage<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String amount = param.get("amount");
        String file = param.get("file");

        step("Login via cookie injection");
        loginMemberAdmin.loginViaCookie(email, password);

        step("Go to Corporate Internal Dashboard");
        loginMemberAdmin.goToDashboard();

        step("Check if user is logged in");
        boolean isLoggedInCRP = corporateDataPage.assertUserIsLoggedIn(email);

        step("Expand the Corporate Deposit Request");
        corporateInternalDashboardSidebar.expandCorporateDepositRequest();

        step("Approve the raised request");
        corporateInternalDashboardSidebar.approveRequest();

        step("Logout");
        loginMemberAdmin.logout();
    }

    @DataProvider(name = "ApproveDepositRequest_DataProvider")
    public Object[][] ApproveDepositRequest_DataProvider() {
        return getObjectParams("ApproveDepositRequest");
    }

    @Author(names = {"Ashish"})
    @Module(name = "CHA Codes")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test(dataProvider = "ChaCodes_DataProvider")
    public void ChaCodes(Map<String, String> param) throws Exception {
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<>(session);
        CorporateDataPage<?> corporateDataPage = new CorporateDataPage<>(session);
        CorporateInternalDashboardSidebar<?> corporateInternalDashboardSidebar = new CorporateInternalDashboardSidebar<>(session);
        RegisteredCorporatePage<?> registeredCorporatePage = new RegisteredCorporatePage<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String amount = param.get("amount");

        step("Login via cookie injection");
        loginMemberAdmin.loginViaCookie(email, password);

        step("Go to Corporate Internal Dashboard");
        loginMemberAdmin.goToDashboard();

        step("Check if user is logged in");
        boolean isLoggedInCRP = corporateDataPage.assertUserIsLoggedIn(email);

        step("Expand the Corporate Deal");
        corporateInternalDashboardSidebar.expandCorporateDeal();

        step("Check if Active Code page is opened successfully");
        boolean isPageOpened = corporateInternalDashboardSidebar.isPageOpened();

        step("Check Inactive code");
        corporateInternalDashboardSidebar.checkInactiveCode();

        step("Check if Inactive code page is opened successfully");
        isPageOpened = corporateInternalDashboardSidebar.isPageOpened();

        step("Logout");
        loginMemberAdmin.logout();
    }

    @DataProvider(name = "ChaCodes_DataProvider")
    public Object[][] ChaCodes_DataProvider() {
        return getObjectParams("ChaCodes");
    }

    @Author(names = {"Ashish"})
    @Module(name = "Corporate Data")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test(dataProvider = "AddNewCorporate_DataProvider")
    public void AddNewCorporate(Map<String, String> param) throws Exception {
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<>(session);
        CorporateDataPage<?> corporateDataPage = new CorporateDataPage<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String corporateName = param.get("corporateName");

        step("Login via cookie injection");
        loginMemberAdmin.loginViaCookie(email, password);

        step("Go to Corporate Internal Dashboard");
        loginMemberAdmin.goToDashboard();

        step("Check if user is logged in");
        boolean isLoggedInCRP = corporateDataPage.checkUserIsLoggedIn();

        step("Assert that user is is logged in to corporate dashboard");
        Assertion.assertThat("User is logged in", isLoggedInCRP, is(true));

        step("Click ON corporate information");
        corporateDataPage.clickCorporateInformation();

        step("Click ON corporate data");
        corporateDataPage.clickCorporateData();

        step("Add New corporate");
        String name = CorporateDataPage.name();
        corporateDataPage.addCorporate(name);

        step("Logout");
        loginMemberAdmin.logout();
    }

    @DataProvider(name = "AddNewCorporate_DataProvider")
    public Object[][] AddNewCorporate_DataProvider() {
        return getObjectParams("AddNewCorporate");
    }

    @Author(names = {"Ashish"})
    @Module(name = "Company Information")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test(dataProvider = "CheckGaurdaCode_DataProvider")
    public void CheckGaurdaCode(Map<String, String> param) throws Exception {
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePage<?> homePage = new HomePage<>(session);
        LoginB2CInDWEB<?> loginB2CInDWEB = new LoginB2CInDWEB<>(session);
        CorporateExternalDashboardSidebar<?> CorporateExternalDashboardSidebar = new CorporateExternalDashboardSidebar<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");

        step("Login via cookie injection and open corporate dashboard");
        homePage.loginViaCookieAndOpenCorporateDashboard(email, password);

        step("Check if user has successfully logged in");
        boolean isUserLoggedIn = loginB2CInDWEB.checkIfLoggedIn();

        step("Assert that user is logged in");
        Assertion.assertThat("Is user logged in", isUserLoggedIn, is(true));

        step("Open the Corporate Information");
        CorporateExternalDashboardSidebar.goToCorporateInformation();

        step("Log out From Admin dashboard");
        homePage.logoutFromAdminDashboard();
    }

    @DataProvider(name = "CheckGaurdaCode_DataProvider")
    public Object[][] CheckGaurdaCode_DataProvider() {
        return getObjectParams("CheckGaurdaCode");
    }


    @Author(names = {"Ashish"})
    @Module(name = "Travel policy")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test(dataProvider = "OpenTravelPolicy_DataProvider")
    public void OpenTravelPolicy(Map<String, String> param) throws Exception {
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePage<?> homePage = new HomePage<>(session);
        LoginB2CInDWEB<?> loginB2CInDWEB = new LoginB2CInDWEB<>(session);
        CorporateExternalDashboardSidebar<?> CorporateExternalDashboardSidebar = new CorporateExternalDashboardSidebar<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");

        step("Login via cookie injection and open corporate dashboard");
        homePage.loginViaCookieAndOpenCorporateDashboard(email, password);

        step("Check if user has successfully logged in");
        boolean isUserLoggedIn = loginB2CInDWEB.checkIfLoggedIn();

        step("Assert that user is logged in");
        Assertion.assertThat("Is user logged in", isUserLoggedIn, is(true));

        step("Open the Travel Policy");
        CorporateExternalDashboardSidebar.goToTravelPolicy();

        step("Log out From Admin dashboard");
        homePage.logoutFromAdminDashboard();
    }

    @DataProvider(name = "OpenTravelPolicy_DataProvider")
    public Object[][] OpenTravelPolicy_DataProvider() {
        return getObjectParams("OpenTravelPolicy");
    }

    @Author(names = {"Harshit"})
    @Module(name = "CHA Codes")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test(dataProvider = "ChaInactive_DataProvider")
    public void ChaInactive(Map<String, String> param) throws Exception {
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<>(session);
        CorporateInternalDashboardSidebar<?> corporateInternalDashboardSidebar = new CorporateInternalDashboardSidebar<>(session);

        String email = param.get("email");
        String password = param.get("password");

        step("Login via cookie injection");
        loginMemberAdmin.loginViaCookie(email, password);
        boolean isLoggedInMemberAdmin = true;

        Assertion.assertThat("Verify User is logged in", isLoggedInMemberAdmin, is(true));

        step("Go to Corporate Internal Dashboard");
        loginMemberAdmin.goToDashboard();

        step("Expand the Corporate Deal");
        corporateInternalDashboardSidebar.expandCorporateDeal();

        step("Check if Active Code page is opened successfully");
        Assertion.assertThat("Check if Active Code page is opened successfully", corporateInternalDashboardSidebar.isPageOpened(), is(true));

        step("Deactivate Code from active Page");

        String newcode = corporateInternalDashboardSidebar.deactivateCode();
        corporateInternalDashboardSidebar.isB2BLoggedIn();
        Assertion.assertThat("Verify if Code is removed from active list ", corporateInternalDashboardSidebar.isCodePresent(newcode), is(false));

        step("Open Inactive Page");
        corporateInternalDashboardSidebar.checkInactiveCode();

        step("Activate Code from inactive Page");
        String code = corporateInternalDashboardSidebar.activateCode();

        corporateInternalDashboardSidebar.isB2BLoggedIn();
        Assertion.assertThat("Verify if Code is removed from inactive list ", corporateInternalDashboardSidebar.isCodePresent(code), is(false));

        step("Open Active Page");
        corporateInternalDashboardSidebar.gotoActivePage();

        Assertion.assertThat("Verify if Active page is opened ", corporateInternalDashboardSidebar.isPageOpened(), is(true));

        step("Search the newly Activated Code");
        corporateInternalDashboardSidebar.searchCode(code);
        Assertion.assertThat("Verify if Code is present in active list ", corporateInternalDashboardSidebar.isCodePresent(code), is(true));

        step("Logout");
        loginMemberAdmin.logout();
    }

    @DataProvider(name = "ChaInactive_DataProvider")
    public Object[][] ChaInactive_DataProvider() {
        return getObjectParams("ChaInactive");
    }

    @Author(names = {"Harshit"})
    @Module(name = "CHA Codes")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test(dataProvider = "ChaActivate_DataProvider")
    public void ChaActivate(Map<String, String> param) throws Exception {
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<>(session);
        CorporateInternalDashboardSidebar<?> corporateInternalDashboardSidebar = new CorporateInternalDashboardSidebar<>(session);

        String email = param.get("email");
        String password = param.get("password");

        step("Login via cookie injection");
        loginMemberAdmin.loginViaCookie(email, password);
        boolean isLoggedInMemberAdmin = true;

        Assertion.assertThat("Verify User is logged in", isLoggedInMemberAdmin, is(true));

        step("Go to Corporate Internal Dashboard");
        loginMemberAdmin.goToDashboard();

        step("Expand the Corporate Deal");
        corporateInternalDashboardSidebar.expandCorporateDeal();

        step("Check if Active Code page is opened successfully");
        Assertion.assertThat("Check if Active Code page is opened successfully", corporateInternalDashboardSidebar.isPageOpened(), is(true));

        step("Deactivate Code from active Page");

        String code = corporateInternalDashboardSidebar.deactivateCode();
        corporateInternalDashboardSidebar.isB2BLoggedIn();
        Assertion.assertThat("Verify if Code is removed from active list ", corporateInternalDashboardSidebar.isCodePresent(code), is(false));

        step("Open Inactive Page");
        corporateInternalDashboardSidebar.checkInactiveCode();

        step("Search the newly Deactivated Code");
        corporateInternalDashboardSidebar.searchCode(code);
        Assertion.assertThat("Verify if Code is present in Inactive list ", corporateInternalDashboardSidebar.isCodePresent(code), is(true));

        step("Logout");
        loginMemberAdmin.logout();
    }

    @DataProvider(name = "ChaActivate_DataProvider")
    public Object[][] ChaActivate_DataProvider() {
        return getObjectParams("ChaActivate");
    }

    @Author(names = {"Harshit Ranjan", "Manan Kumar"})
    @Module(name = "Travel plan")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test(dataProvider = "TravelPlanConfigurationAccom_DataProvider")
    public void TravelPlanConfigurationAccom(Map<String, String> param) throws Exception {
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePage<?> homePage = new HomePage<>(session);
        LoginB2CInDWEB<?> loginB2CInDWEB = new LoginB2CInDWEB<>(session);
        CorporateExternalDashboardSidebar<?> CorporateExternalDashboardSidebar = new CorporateExternalDashboardSidebar<>(session);
        TravelPlan<?> travelPlan = new TravelPlan<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        String product = param.get("product");
        String name = CoreFunctions.randomName() + "Test Plan";
        String arrival = param.get("arrival");
        String departure = param.get("departure");
        String dateRange = param.get("dateRange");
        String starRating = param.get("starRating");
        String maxBudget = param.get("maxBudget");


        step("Login via cookie injection and open corporate dashboard");
        homePage.loginViaCookieAndOpenCorporateDashboard(email, password);

        boolean isUserLoggedIn = loginB2CInDWEB.checkIfLoggedIn();

        step("Check that user is logged in");
        Assertion.assertThat("Is user logged in", isUserLoggedIn, is(true));

        step("Open the Travel Plan");
        CorporateExternalDashboardSidebar.openTravelPlanPage();

        step("Go to Create Travel Plan");

        travelPlan.clickCreateTravel();
        Assertion.assertThat("Verify if Create Travel Plan page is opened", travelPlan.isCreateTravelPageOpened(), is(true));

        step("Create Travel Plan");
        travelPlan.CreateTravelPlan(name, dateRange, product, arrival, departure);

        step("User clicks on Set exceptions.");
        travelPlan.clickSetExceptions();

        step("Verify Add accom exception dialog is opened.");
        Assertion.assertThat("Add accommodation exception dialog is opened", travelPlan.isAccomExceptionDialogOpened(), is(true));

        step("User selects hotel Star Rating");
        travelPlan.selectExceptionHotelStar(starRating);

        step("User enters max budget per night per room.");
        travelPlan.enterMaxBudget(maxBudget);

        step("User clicks on Save exception.");
        travelPlan.saveException();

        step("Verify exception is added successfully.");
        Assertion.assertThat("Exception added successfully! message is displayed", travelPlan.isExceptionAddedSuccessfully(), is(true));

        step("Select Approver And Submit");
        travelPlan.selectApproverAndSubmit();

        step("Travel Plan Request is Submitted");
        Assertion.assertThat("Verify if Travel Plan is created Successfully", travelPlan.isTravelPlanCreated(name), is(true));

        step("User clicks on Open to go to travel plan detail.");
        travelPlan.clickOpenButton();

        step("User clicks on Check details to open the travel plan exception.");
        travelPlan.clickCheckDetails();

        step("Verify travel plan exception has the same hotel star and budget that were passed.");
        Assertion.assertThat("Hotel star of exception", travelPlan.getExceptionPopupValue("Hotel star"), startsWith(starRating));
        Assertion.assertThat("Max. budget of exception", travelPlan.getExceptionPopupValue("Max. budget"), startsWith(String.format(java.util.Locale.US, "IDR %,d", Long.parseLong(maxBudget))));

        step("User closes the travel plan exception popup.");
        travelPlan.closeExceptionPopup();

        step("User clicks on Cancel request to cleanup.");
        travelPlan.cancelTravelPlan();

        step("Verify travel plan request is cancelled successfully.");
        Assertion.assertThat("You've deleted your travel plan request. message is displayed", travelPlan.isTravelPlanDeletedSuccessfully(), is(true));

        step("Log out From Admin dashboard");
        homePage.logoutFromAdminDashboard();
    }

    @DataProvider(name = "TravelPlanConfigurationAccom_DataProvider")
    public Object[][] TravelPlanConfigurationAccom_DataProvider() {
        return getObjectParams("TravelPlanConfigurationAccom");
    }

    @Author(names = {"Harshit Ranjan"})
    @Module(name = "Travel plan")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test(dataProvider = "TravelPlanConfigurationCarRentals_DataProvider")
    public void TravelPlanConfigurationCarRentals(Map<String, String> param) throws Exception {
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePage<?> homePage = new HomePage<>(session);
        LoginB2CInDWEB<?> loginB2CInDWEB = new LoginB2CInDWEB<>(session);
        CorporateExternalDashboardSidebar<?> CorporateExternalDashboardSidebar = new CorporateExternalDashboardSidebar<>(session);
        TravelPlan<?> travelPlan = new TravelPlan<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        String product = param.get("product");
        String name = CoreFunctions.randomName() + "Test Plan";
        String arrival = param.get("arrival");
        String departure = param.get("departure");
        String dateRange = param.get("dateRange");

        step("Login via cookie injection and open corporate dashboard");
        homePage.loginViaCookieAndOpenCorporateDashboard(email, password);

        boolean isUserLoggedIn = loginB2CInDWEB.checkIfLoggedIn();

        step("Check that user is logged in");
        Assertion.assertThat("Is user logged in", isUserLoggedIn, is(true));

        step("Open the Travel Plan");
        CorporateExternalDashboardSidebar.openTravelPlanPage();

        step("Go to Create Travel Plan");

        travelPlan.clickCreateTravel();
        Assertion.assertThat("Verify if Create Travel Plan page is opened", travelPlan.isCreateTravelPageOpened(), is(true));

        step("Create Travel Plan");
        travelPlan.CreateTravelPlan(name, dateRange, product, arrival, departure);
        travelPlan.selectApproverAndSubmit();

        step("Travel Plan Request is Submitted");
        Assertion.assertThat("Verify if Travel Plan is created Successfully", travelPlan.isTravelPlanCreated(name), is(true));

        step("Delete The Travel Plan");
        travelPlan.deleteTravelPlan();
        Assertion.assertThat("Verify if Travel Plan is deleted Successfully", travelPlan.isTravelPlanCreated(name), is(false));

        step("Log out From Admin dashboard");
        homePage.logoutFromAdminDashboard();
    }

    @DataProvider(name = "TravelPlanConfigurationCarRentals_DataProvider")
    public Object[][] TravelPlanConfigurationCarRentals_DataProvider() {
        return getObjectParams("TravelPlanConfigurationCarRentals");
    }

    @Author(names = {"Harshit Ranjan"})
    @Module(name = "Travel plan")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test(dataProvider = "TravelPlanConfigurationTrains_DataProvider")
    public void TravelPlanConfigurationTrains(Map<String, String> param) throws Exception {
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePage<?> homePage = new HomePage<>(session);
        LoginB2CInDWEB<?> loginB2CInDWEB = new LoginB2CInDWEB<>(session);
        CorporateExternalDashboardSidebar<?> CorporateExternalDashboardSidebar = new CorporateExternalDashboardSidebar<>(session);
        TravelPlan<?> travelPlan = new TravelPlan<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        String product = param.get("product");
        String name = CoreFunctions.randomName() + "Test Plan";
        String arrival = param.get("arrival");
        String departure = param.get("departure");
        String dateRange = param.get("dateRange");

        step("Login via cookie injection and open corporate dashboard");
        homePage.loginViaCookieAndOpenCorporateDashboard(email, password);

        boolean isUserLoggedIn = loginB2CInDWEB.checkIfLoggedIn();

        step("Check that user is logged in");
        Assertion.assertThat("Is user logged in", isUserLoggedIn, is(true));

        step("Open the Travel Plan");
        CorporateExternalDashboardSidebar.openTravelPlanPage();

        step("Go to Create Travel Plan");

        travelPlan.clickCreateTravel();
        Assertion.assertThat("Verify if Create Travel Plan page is opened", travelPlan.isCreateTravelPageOpened(), is(true));

        step("Create Travel Plan");
        travelPlan.CreateTravelPlan(name, dateRange, product, arrival, departure);
        travelPlan.selectApproverAndSubmit();

        step("Travel Plan Request is Submitted");
        Assertion.assertThat("Verify if Travel Plan is created Successfully", travelPlan.isTravelPlanCreated(name), is(true));

        step("Delete The Travel Plan");
        travelPlan.deleteTravelPlan();
        Assertion.assertThat("Verify if Travel Plan is deleted Successfully", travelPlan.isTravelPlanCreated(name), is(false));

        step("Log out From Admin dashboard");
        homePage.logoutFromAdminDashboard();
    }

    @DataProvider(name = "TravelPlanConfigurationTrains_DataProvider")
    public Object[][] TravelPlanConfigurationTrains_DataProvider() {
        return getObjectParams("TravelPlanConfigurationTrains");
    }

    @Author(names = {"Harshit Ranjan","Manan Kumar"})
    @Module(name = "Travel plan")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test(dataProvider = "TravelPlanConfigurationFlights_DataProvider")
    public void TravelPlanConfigurationFlights(Map<String, String> param) throws Exception {
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePage<?> homePage = new HomePage<>(session);
        LoginB2CInDWEB<?> loginB2CInDWEB = new LoginB2CInDWEB<>(session);
        CorporateExternalDashboardSidebar<?> CorporateExternalDashboardSidebar = new CorporateExternalDashboardSidebar<>(session);
        TravelPlan<?> travelPlan = new TravelPlan<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        String product = param.get("product");
        String name = CoreFunctions.randomName() + "Test Plan";
        String arrival = param.get("arrival");
        String departure = param.get("departure");
        String dateRange = param.get("dateRange");
        String airline = param.get("airline");
        String cabin = param.get("cabin");
        String maxBudget = param.get("maxBudget");

        step("Login via cookie injection and open corporate dashboard");
        homePage.loginViaCookieAndOpenCorporateDashboard(email, password);

        boolean isUserLoggedIn = loginB2CInDWEB.checkIfLoggedIn();

        step("Check that user is logged in");
        Assertion.assertThat("Is user logged in", isUserLoggedIn, is(true));

        step("Open the Travel Plan");
        CorporateExternalDashboardSidebar.openTravelPlanPage();

        step("Go to Create Travel Plan");

        travelPlan.clickCreateTravel();
        Assertion.assertThat("Verify if Create Travel Plan page is opened", travelPlan.isCreateTravelPageOpened(), is(true));

        step("Create Travel Plan");
        travelPlan.CreateTravelPlan(name, dateRange, product, arrival, departure);

        step("User clicks on Set exceptions.");
        travelPlan.clickSetExceptions();

        step("Verify Add flight exception dialog is opened.");
        Assertion.assertThat("Add flight exception dialog is opened", travelPlan.isFlightExceptionDialogOpened(), is(true));

        step("User selects airline.");
        travelPlan.selectExceptionAirline(airline);

        step("User selects cabin class.");
        travelPlan.selectExceptionCabin(cabin);

        step("User enters max budget per flight.");
        travelPlan.enterMaxBudget(maxBudget);

        step("User clicks on Save exception.");
        travelPlan.saveException();

        step("Verify exception is added successfully.");
        Assertion.assertThat("Exception added successfully! message is displayed", travelPlan.isExceptionAddedSuccessfully(), is(true));

        step("Select Approver And Submit");
        travelPlan.selectApproverAndSubmit();

        step("Travel Plan Request is Submitted");
        Assertion.assertThat("Verify if Travel Plan is created Successfully", travelPlan.isTravelPlanCreated(name), is(true));

        step("User clicks on Open to go to travel plan detail.");
        travelPlan.clickOpenButton();

        step("User clicks on Check details to open the travel plan exception.");
        travelPlan.clickCheckDetails();

        step("Verify travel plan exception has the same airline, cabin and budget that were passed.");
        Assertion.assertThat("Airlines of exception", travelPlan.getExceptionPopupValue("Airlines"), containsStringIgnoringCase(airline));
        Assertion.assertThat("Cabin of exception", travelPlan.getExceptionPopupValue("Cabin"), equalToIgnoringCase(cabin));
        Assertion.assertThat("Max. budget of exception", travelPlan.getExceptionPopupValue("Max. budget"), startsWith(String.format(java.util.Locale.US, "IDR %,d", Long.parseLong(maxBudget))));

        step("User closes the travel plan exception popup.");
        travelPlan.closeExceptionPopup();

        step("User clicks on Cancel request to cleanup.");
        travelPlan.cancelTravelPlan();

        step("Verify travel plan request is cancelled successfully.");
        Assertion.assertThat("You've deleted your travel plan request. message is displayed", travelPlan.isTravelPlanDeletedSuccessfully(), is(true));

        step("Log out From Admin dashboard");
        homePage.logoutFromAdminDashboard();
    }

    @DataProvider(name = "TravelPlanConfigurationFlights_DataProvider")
    public Object[][] TravelPlanConfigurationFlights_DataProvider() {
        return getObjectParams("TravelPlanConfigurationFlights");
    }

    @Author(names = {"Harshit Ranjan"})
    @Module(name = "Travel plan")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test(dataProvider = "MaxTravelPlanConfigCarRentals_DataProvider")
    public void MaxTravelPlanConfigCarRentals(Map<String, String> param) throws Exception {
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePage<?> homePage = new HomePage<>(session);
        LoginB2CInDWEB<?> loginB2CInDWEB = new LoginB2CInDWEB<>(session);
        CorporateExternalDashboardSidebar<?> CorporateExternalDashboardSidebar = new CorporateExternalDashboardSidebar<>(session);
        TravelPlan<?> travelPlan = new TravelPlan<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        String product = param.get("product");
        String name = CoreFunctions.randomName() + "Test Plan";
        String arrival = param.get("arrival");
        String departure = param.get("departure");
        String dateRange = param.get("dateRange");

        step("Login via cookie injection and open corporate dashboard");
        homePage.loginViaCookieAndOpenCorporateDashboard(email, password);

        boolean isUserLoggedIn = loginB2CInDWEB.checkIfLoggedIn();

        step("Check that user is logged in");
        Assertion.assertThat("Is user logged in", isUserLoggedIn, is(true));

        step("Open the Travel Plan");
        CorporateExternalDashboardSidebar.openTravelPlanPage();

        step("Go to Create Travel Plan");

        travelPlan.clickCreateTravel();
        Assertion.assertThat("Verify if Create Travel Plan page is opened", travelPlan.isCreateTravelPageOpened(), is(true));

        step("Create Travel Plan");
        travelPlan.CreateTravelPlan(name, dateRange, product, arrival, departure);

        step("Create 5 Config for Travel Plan");
        travelPlan.addFiveConfig();

        Assertion.assertThat("Add Another Config Button should be disabled", travelPlan.isAddButtonClickable(), is(false));

        Assertion.assertThat("Maximum 5 config for travel Plan can be created for Car Rentals vertical", travelPlan.noOfConfig(), is(5));

        step("Log out From Admin dashboard");
        homePage.logoutFromAdminDashboard();
    }

    @DataProvider(name = "MaxTravelPlanConfigCarRentals_DataProvider")
    public Object[][] MaxTravelPlanConfigCarRentals_DataProvider() {
        return getObjectParams("MaxTravelPlanConfigCarRentals");
    }

    @Author(names = {"Harshit Ranjan"})
    @Module(name = "Travel plan")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test(dataProvider = "MaxTravelPlanConfigTrains_DataProvider")
    public void MaxTravelPlanConfigTrains(Map<String, String> param) throws Exception {
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePage<?> homePage = new HomePage<>(session);
        LoginB2CInDWEB<?> loginB2CInDWEB = new LoginB2CInDWEB<>(session);
        CorporateExternalDashboardSidebar<?> CorporateExternalDashboardSidebar = new CorporateExternalDashboardSidebar<>(session);
        TravelPlan<?> travelPlan = new TravelPlan<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        String product = param.get("product");
        String name = CoreFunctions.randomName() + "Test Plan";
        String arrival = param.get("arrival");
        String departure = param.get("departure");
        String dateRange = param.get("dateRange");

        step("Login via cookie injection and open corporate dashboard");
        homePage.loginViaCookieAndOpenCorporateDashboard(email, password);

        boolean isUserLoggedIn = loginB2CInDWEB.checkIfLoggedIn();

        step("Check that user is logged in");
        Assertion.assertThat("Is user logged in", isUserLoggedIn, is(true));

        step("Open the Travel Plan");
        CorporateExternalDashboardSidebar.openTravelPlanPage();

        step("Go to Create Travel Plan");

        travelPlan.clickCreateTravel();
        Assertion.assertThat("Verify if Create Travel Plan page is opened", travelPlan.isCreateTravelPageOpened(), is(true));

        step("Create Travel Plan");
        travelPlan.CreateTravelPlan(name, dateRange, product, arrival, departure);

        step("Create 5 Config for Travel Plan");
        travelPlan.addFiveConfig();

        Assertion.assertThat("Add Another Config Button should be disabled", travelPlan.isAddButtonClickable(), is(false));

        Assertion.assertThat("Maximum 5 config for travel Plan can be created for Trains vertical", travelPlan.noOfConfig(), is(5));

        step("Log out From Admin dashboard");
        homePage.logoutFromAdminDashboard();
    }

    @DataProvider(name = "MaxTravelPlanConfigTrains_DataProvider")
    public Object[][] MaxTravelPlanConfigTrains_DataProvider() {
        return getObjectParams("MaxTravelPlanConfigTrains");
    }

    @Author(names = {"Harshit Ranjan"})
    @Module(name = "Travel plan")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test(dataProvider = "MaxTravelPlanConfigFlights_DataProvider")
    public void MaxTravelPlanConfigFlights(Map<String, String> param) throws Exception {
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePage<?> homePage = new HomePage<>(session);
        LoginB2CInDWEB<?> loginB2CInDWEB = new LoginB2CInDWEB<>(session);
        CorporateExternalDashboardSidebar<?> CorporateExternalDashboardSidebar = new CorporateExternalDashboardSidebar<>(session);
        TravelPlan<?> travelPlan = new TravelPlan<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        String product = param.get("product");
        String name = CoreFunctions.randomName() + "Test Plan";
        String arrival = param.get("arrival");
        String departure = param.get("departure");
        String dateRange = param.get("dateRange");

        step("Login via cookie injection and open corporate dashboard");
        homePage.loginViaCookieAndOpenCorporateDashboard(email, password);

        boolean isUserLoggedIn = loginB2CInDWEB.checkIfLoggedIn();

        step("Check that user is logged in");
        Assertion.assertThat("Is user logged in", isUserLoggedIn, is(true));

        step("Open the Travel Plan");
        CorporateExternalDashboardSidebar.openTravelPlanPage();

        step("Go to Create Travel Plan");

        travelPlan.clickCreateTravel();
        Assertion.assertThat("Verify if Create Travel Plan page is opened", travelPlan.isCreateTravelPageOpened(), is(true));

        step("Create Travel Plan");
        travelPlan.CreateTravelPlan(name, dateRange, product, arrival, departure);

        step("Create 5 Config for Travel Plan");
        travelPlan.addFiveConfig();

        Assertion.assertThat("Add Another Config Button should be disabled", travelPlan.isAddButtonClickable(), is(false));

        Assertion.assertThat("Maximum 5 config for travel Plan can be created for Flights vertical", travelPlan.noOfConfig(), is(5));

        step("Log out From Admin dashboard");
        homePage.logoutFromAdminDashboard();
    }

    @DataProvider(name = "MaxTravelPlanConfigFlights_DataProvider")
    public Object[][] MaxTravelPlanConfigFlights_DataProvider() {
        return getObjectParams("MaxTravelPlanConfigFlights");
    }

    @Author(names = {"Harshit Ranjan"})
    @Module(name = "Travel plan")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test(dataProvider = "MaxTravelPlanConfigAccom_DataProvider")
    public void MaxTravelPlanConfigAccom(Map<String, String> param) throws Exception {
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePage<?> homePage = new HomePage<>(session);
        LoginB2CInDWEB<?> loginB2CInDWEB = new LoginB2CInDWEB<>(session);
        CorporateExternalDashboardSidebar<?> CorporateExternalDashboardSidebar = new CorporateExternalDashboardSidebar<>(session);
        TravelPlan<?> travelPlan = new TravelPlan<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        String product = param.get("product");
        String name = CoreFunctions.randomName() + "Test Plan";
        String arrival = param.get("arrival");
        String departure = param.get("departure");
        String dateRange = param.get("dateRange");


        step("Login via cookie injection and open corporate dashboard");
        homePage.loginViaCookieAndOpenCorporateDashboard(email, password);

        boolean isUserLoggedIn = loginB2CInDWEB.checkIfLoggedIn();

        step("Check that user is logged in");
        Assertion.assertThat("Is user logged in", isUserLoggedIn, is(true));

        step("Open the Travel Plan");
        CorporateExternalDashboardSidebar.openTravelPlanPage();

        step("Go to Create Travel Plan");

        travelPlan.clickCreateTravel();
        Assertion.assertThat("Verify if Create Travel Plan page is opened", travelPlan.isCreateTravelPageOpened(), is(true));

        step("Create Travel Plan");
        travelPlan.CreateTravelPlan(name, dateRange, product, arrival, departure);

        step("Create 5 Config for Travel Plan");
        travelPlan.addFiveConfig();

        Assertion.assertThat("Add Another Config Button should be disabled", travelPlan.isAddButtonClickable(), is(false));

        Assertion.assertThat("Maximum 5 config for travel Plan can be created for Accommodation vertical", travelPlan.noOfConfig(), is(5));

        step("Log out From Admin dashboard");
        homePage.logoutFromAdminDashboard();
    }

    @DataProvider(name = "MaxTravelPlanConfigAccom_DataProvider")
    public Object[][] MaxTravelPlanConfigAccom_DataProvider() {
        return getObjectParams("MaxTravelPlanConfigAccom");
    }

    @Author(names = {"Harshit Ranjan"})
    @Module(name = "Travel plan")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test(dataProvider = "verifyTravelPlanPageOpened_DataProvider")
    public void verifyTravelPlanPageOpened(Map<String, String> param) throws Exception {
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePage<?> homePage = new HomePage<>(session);
        LoginB2CInDWEB<?> loginB2CInDWEB = new LoginB2CInDWEB<>(session);
        CorporateExternalDashboardSidebar<?> CorporateExternalDashboardSidebar = new CorporateExternalDashboardSidebar<>(session);
        TravelPlan<?> travelPlan = new TravelPlan<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");

        step("Login via cookie injection and open corporate dashboard");
        homePage.loginViaCookieAndOpenCorporateDashboard(email, password);

        boolean isUserLoggedIn = loginB2CInDWEB.checkIfLoggedIn();

        step("Check that user is logged in");
        Assertion.assertThat("Is user logged in", isUserLoggedIn, is(true));

        step("Open the Travel Plan");
        CorporateExternalDashboardSidebar.openTravelPlanPage();

        step("Go to Create Travel Plan");
        travelPlan.clickCreateTravel();
        Assertion.assertThat("Verify if Create Travel Plan page is opened", travelPlan.isCreateTravelPageOpened(), is(true));

        step("Log out From Admin dashboard");
        homePage.logoutFromAdminDashboard();
    }

    @DataProvider(name = "verifyTravelPlanPageOpened_DataProvider")
    public Object[][] verifyTravelPlanPageOpened_DataProvider() {
        return getObjectParams("verifyTravelPlanPageOpened");
    }

    @Author(names = {"Harshit Ranjan"})
    @Module(name = "Team Directory")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test(dataProvider = "verifyTeamDirectoryUnderSidebar_DataProvider")
    public void verifyTeamDirectoryUnderSidebar(Map<String, String> param) throws Exception {
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePage<?> homePage = new HomePage<>(session);
        LoginB2CInDWEB<?> loginB2CInDWEB = new LoginB2CInDWEB<>(session);
        CorporateExternalDashboardSidebar<?> CorporateExternalDashboardSidebar = new CorporateExternalDashboardSidebar<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");

        step("Login via cookie injection and open corporate dashboard");
        homePage.loginViaCookieAndOpenCorporateDashboard(email, password);

        boolean isUserLoggedIn = loginB2CInDWEB.checkIfLoggedIn();

        step("Check that user is logged in");
        Assertion.assertThat("Is user logged in", isUserLoggedIn, is(true));

        step("Check Team Directory present in sidebar");
        Assertion.assertThat("Verify Team Directory Option is present in sidebar", CorporateExternalDashboardSidebar.isTeamManagementVisible(), is(true));

        step("Log out From Admin dashboard");
        homePage.logoutFromAdminDashboard();
    }

    @DataProvider(name = "verifyTeamDirectoryUnderSidebar_DataProvider")
    public Object[][] verifyTeamDirectoryUnderSidebar_DataProvider() {
        return getObjectParams("verifyTeamDirectoryUnderSidebar");
    }

    @Author(names = {"Harshit Ranjan"})
    @Module(name = "Team Directory")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test(dataProvider = "verifyCreateDepartmentPopup_DataProvider")
    public void verifyCreateDepartmentPopup(Map<String, String> param) throws Exception {
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePage<?> homePage = new HomePage<>(session);
        LoginB2CInDWEB<?> loginB2CInDWEB = new LoginB2CInDWEB<>(session);
        CorporateExternalDashboardSidebar<?> CorporateExternalDashboardSidebar = new CorporateExternalDashboardSidebar<>(session);
        DepartmentPage<?> DepartmentPage = new DepartmentPage<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");

        step("Login via cookie injection and open corporate dashboard");
        homePage.loginViaCookieAndOpenCorporateDashboard(email, password);

        boolean isUserLoggedIn = loginB2CInDWEB.checkIfLoggedIn();

        step("Check that user is logged in");
        Assertion.assertThat("Is user logged in", isUserLoggedIn, is(true));

        step("Check Team Directory present in sidebar");
        Assertion.assertThat("Verify Team Directory Option is present in sidebar", CorporateExternalDashboardSidebar.isTeamManagementVisible(), is(true));

        step("Go to Department");
        CorporateExternalDashboardSidebar.goToDepartment();

        step("Click create Department button");
        DepartmentPage.clickDepartmentButton();

        step("Check if create department popup appeared");
        Assertion.assertThat("Verify Create Department Popup Apppeared", DepartmentPage.isCreateDepartmentPopup(), is(true));

        step("Log out From Admin dashboard");
        homePage.logoutFromAdminDashboard();
    }

    @DataProvider(name = "verifyCreateDepartmentPopup_DataProvider")
    public Object[][] verifyCreateDepartmentPopup_DataProvider() {
        return getObjectParams("verifyCreateDepartmentPopup");
    }

    @Author(names = {"Harshit Ranjan"})
    @Module(name = "Team Directory")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test(dataProvider = "verifyAddMemberButton_DataProvider")
    public void verifyAddMemberButton(Map<String, String> param) throws Exception {
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePage<?> homePage = new HomePage<>(session);
        LoginB2CInDWEB<?> loginB2CInDWEB = new LoginB2CInDWEB<>(session);
        CorporateExternalDashboardSidebar<?> CorporateExternalDashboardSidebar = new CorporateExternalDashboardSidebar<>(session);
        DepartmentPage<?> DepartmentPage = new DepartmentPage<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");

        step("Login via cookie injection and open corporate dashboard");
        homePage.loginViaCookieAndOpenCorporateDashboard(email, password);

        boolean isUserLoggedIn = loginB2CInDWEB.checkIfLoggedIn();

        step("Check that user is logged in");
        Assertion.assertThat("Is user logged in", isUserLoggedIn, is(true));

        step("Check Team Directory present in sidebar");
        Assertion.assertThat("Verify Team Directory Option is present in sidebar", CorporateExternalDashboardSidebar.isTeamManagementVisible(), is(true));

        step("Go to Department");
        CorporateExternalDashboardSidebar.goToDepartment();

        step("Click on Department name to enter Department Detail Page");
        DepartmentPage.goToDepartmentDetailPage();

        step("Check If Add Member button is present");
        Assertion.assertThat("Verify that Add Member Button is present on Department Detail page", DepartmentPage.isAddMemberButton(), is(true));

        step("Log out From Admin dashboard");
        homePage.logoutFromAdminDashboard();
    }

    @DataProvider(name = "verifyAddMemberButton_DataProvider")
    public Object[][] verifyAddMemberButton_DataProvider() {
        return getObjectParams("verifyAddMemberButton");
    }

    @Author(names = {"Harshit Ranjan"})
    @Module(name = "Team Directory")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test(dataProvider = "verifyDepartmentDetailPage_DataProvider")
    public void verifyDepartmentDetailPage(Map<String, String> param) throws Exception {
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePage<?> homePage = new HomePage<>(session);
        LoginB2CInDWEB<?> loginB2CInDWEB = new LoginB2CInDWEB<>(session);
        CorporateExternalDashboardSidebar<?> CorporateExternalDashboardSidebar = new CorporateExternalDashboardSidebar<>(session);
        DepartmentPage<?> DepartmentPage = new DepartmentPage<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");

        step("Login via cookie injection and open corporate dashboard");
        homePage.loginViaCookieAndOpenCorporateDashboard(email, password);

        boolean isUserLoggedIn = loginB2CInDWEB.checkIfLoggedIn();

        step("Check that user is logged in");
        Assertion.assertThat("Is user logged in", isUserLoggedIn, is(true));

        step("Check Team Directory present in sidebar");
        Assertion.assertThat("Verify Team Directory Option is present in sidebar", CorporateExternalDashboardSidebar.isTeamManagementVisible(), is(true));

        step("Go to Department");
        CorporateExternalDashboardSidebar.goToDepartment();

        step("Click on Department name to enter Department Detail Page");
        DepartmentPage.goToDepartmentDetailPage();

        Assertion.assertThat("Verify Department Detail Page is Opened", DepartmentPage.isDepartmentDetailPageOpened(), is(true));

        step("Log out From Admin dashboard");
        homePage.logoutFromAdminDashboard();
    }

    @DataProvider(name = "verifyDepartmentDetailPage_DataProvider")
    public Object[][] verifyDepartmentDetailPage_DataProvider() {
        return getObjectParams("verifyDepartmentDetailPage");
    }

    @Author(names = {"Harshit Ranjan"})
    @Module(name = "Login")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test(dataProvider = "LoginSuperAdmin_DataProvider")
    public void LoginSuperAdmin(Map<String, String> param) throws Exception {
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePage<?> homePage = new HomePage<>(session);
        LoginB2CInDWEB<?> loginB2CInDWEB = new LoginB2CInDWEB<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");

        step("Login via cookie injection and open corporate dashboard");
        homePage.loginViaCookieAndOpenCorporateDashboard(email, password);

        boolean isUserLoggedIn = loginB2CInDWEB.checkIfLoggedIn();

        step("Check that user is logged in");
        Assertion.assertThat("Is user logged in", isUserLoggedIn, is(true));

        step("Log out From Admin dashboard");
        homePage.logoutFromAdminDashboard();
    }

    @DataProvider(name = "LoginSuperAdmin_DataProvider")
    public Object[][] LoginSuperAdmin_DataProvider() {
        return getObjectParams("LoginSuperAdmin");
    }

    @Author(names = {"Harshit Ranjan"})
    @Module(name = "Add Employee")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test(dataProvider = "AddEmployeeAsAdmin_DataProvider")
    public void AddEmployeeAsAdmin(Map<String, String> param) throws Exception {
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePage<?> homePage = new HomePage<>(session);
        LoginB2CInDWEB<?> loginB2CInDWEB = new LoginB2CInDWEB<>(session);
        EmployeeListPage<?> employeeListPage = new EmployeeListPage<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        String name = CoreFunctions.randomName();
        String roleName = param.get("role");

        step("Login via cookie injection and open corporate dashboard");
        homePage.loginViaCookieAndOpenCorporateDashboard(email, password);

        boolean isUserLoggedIn = loginB2CInDWEB.checkIfLoggedIn();

        step("Check that user is logged in");
        Assertion.assertThat("Is user logged in", isUserLoggedIn, is(true));

        step("Open the Employee List");
        employeeListPage.goToEmployeeListPage();

        step("Click Add Employee");
        employeeListPage.clickAddEmployee();

        step("Fill the new Employee Details");
        employeeListPage.fillEmployeeDetails(roleName, name);

        step("Click Submit");
        employeeListPage.clickSubmit();

        step("Search newly Added Employee");
        employeeListPage.searchNewEmployee(name);

        step("Check if the searched Name exists");
        boolean isNameExists = employeeListPage.checkIfExists(name);

        step("Assert that the newly added employee can be found");
        Assertion.assertThat("Is new employee exists", isNameExists, is(true));

        step("Log out From Admin dashboard");
        homePage.logoutFromAdminDashboard();
    }

    @DataProvider(name = "AddEmployeeAsAdmin_DataProvider")
    public Object[][] AddEmployeeAsAdmin_DataProvider() {
        return getObjectParams("AddEmployeeAsAdmin");
    }

    @Author(names = {"Harshit Ranjan"})
    @Module(name = "Travel plan")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test(dataProvider = "TravelPlanConfigurationWhoosh_DataProvider")
    public void TravelPlanConfigurationWhoosh(Map<String, String> param) throws Exception {
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePage<?> homePage = new HomePage<>(session);
        LoginB2CInDWEB<?> loginB2CInDWEB = new LoginB2CInDWEB<>(session);
        CorporateExternalDashboardSidebar<?> CorporateExternalDashboardSidebar = new CorporateExternalDashboardSidebar<>(session);
        TravelPlan<?> travelPlan = new TravelPlan<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        String product = param.get("product");
        String name = CoreFunctions.randomName() + "Test Plan";
        String arrival = param.get("arrival");
        String departure = param.get("departure");
        String dateRange = param.get("dateRange");

        step("Login via cookie injection and open corporate dashboard");
        homePage.loginViaCookieAndOpenCorporateDashboard(email, password);

        boolean isUserLoggedIn = loginB2CInDWEB.checkIfLoggedIn();

        step("Check that user is logged in");
        Assertion.assertThat("Is user logged in", isUserLoggedIn, is(true));

        step("Open the Travel Plan");
        CorporateExternalDashboardSidebar.openTravelPlanPage();

        step("Go to Create Travel Plan");

        travelPlan.clickCreateTravel();
        Assertion.assertThat("Verify if Create Travel Plan page is opened", travelPlan.isCreateTravelPageOpened(), is(true));

        step("Create Travel Plan");
        travelPlan.CreateTravelPlan(name, dateRange, product, arrival, departure);
        travelPlan.selectApproverAndSubmit();

        step("Travel Plan Request is Submitted");
        Assertion.assertThat("Verify if Travel Plan is created Successfully", travelPlan.isTravelPlanCreated(name), is(true));

        step("Delete The Travel Plan");
        travelPlan.deleteTravelPlan();
        Assertion.assertThat("Verify if Travel Plan is deleted Successfully", travelPlan.isTravelPlanCreated(name), is(false));

        step("Log out From Admin dashboard");
        homePage.logoutFromAdminDashboard();
    }

    @DataProvider(name = "TravelPlanConfigurationWhoosh_DataProvider")
    public Object[][] TravelPlanConfigurationWhoosh_DataProvider() {
        return getObjectParams("TravelPlanConfigurationWhoosh");
    }

    @Author(names = {"Harshit Ranjan"})
    @Module(name = "Travel policy")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test(dataProvider = "TravelPolicyVisibleToAdmin_DataProvider")
    public void TravelPolicyVisibleToAdmin(Map<String, String> param) throws Exception {
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePage<?> homePage = new HomePage<>(session);
        LoginB2CInDWEB<?> loginB2CInDWEB = new LoginB2CInDWEB<>(session);
        CorporateExternalDashboardSidebar<?> CorporateExternalDashboardSidebar = new CorporateExternalDashboardSidebar<>(session);
        TravelPolicyPage<?> TravelPolicyPage = new TravelPolicyPage<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");

        step("Login via cookie injection and open corporate dashboard");
        homePage.loginViaCookieAndOpenCorporateDashboard(email, password);

        step("Check if user has successfully logged in");
        boolean isUserLoggedIn = loginB2CInDWEB.checkIfLoggedIn();

        step("Assert that user is logged in");
        Assertion.assertThat("Is user logged in", isUserLoggedIn, is(true));

        step("Open the Travel Policy");
        CorporateExternalDashboardSidebar.goToTravelPolicy();

        step("Verify Travel Policy Page is Opened");
        Assertion.assertThat("Travel Policy Page is opened", TravelPolicyPage.isTravelPolicyPageOpened(), is(true));

        step("Click on Add Travel Policy Button");
        TravelPolicyPage.clickAddPolicy();

        Assertion.assertThat("Popup to create Travel Policy Appeared", TravelPolicyPage.isCreatePolicyPopup(), is(true));

        step("Log out From Admin dashboard");
        homePage.logoutFromAdminDashboard();
    }

    @DataProvider(name = "TravelPolicyVisibleToAdmin_DataProvider")
    public Object[][] TravelPolicyVisibleToAdmin_DataProvider() {
        return getObjectParams("TravelPolicyVisibleToAdmin");
    }

    @Author(names = {"Harshit Ranjan"})
    @Module(name = "Team Directory")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test(dataProvider = "verifyAddGroupPopup_DataProvider")
    public void verifyAddGroupPopup(Map<String, String> param) throws Exception {
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePage<?> homePage = new HomePage<>(session);
        LoginB2CInDWEB<?> loginB2CInDWEB = new LoginB2CInDWEB<>(session);
        CorporateExternalDashboardSidebar<?> CorporateExternalDashboardSidebar = new CorporateExternalDashboardSidebar<>(session);
        GroupsPage<?> GroupsPage = new GroupsPage<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");

        step("Login via cookie injection and open corporate dashboard");
        homePage.loginViaCookieAndOpenCorporateDashboard(email, password);

        boolean isUserLoggedIn = loginB2CInDWEB.checkIfLoggedIn();

        step("Check that user is logged in");
        Assertion.assertThat("Is user logged in", isUserLoggedIn, is(true));

        step("Check Team Directory present in sidebar");
        Assertion.assertThat("Verify Team Directory Option is present in sidebar", CorporateExternalDashboardSidebar.isTeamManagementVisible(), is(true));

        step("Go to Department");
        CorporateExternalDashboardSidebar.goToGroup();

        step("Click create Group button");
        GroupsPage.clickAddGroup();

        step("Check if create group popup appeared");
        Assertion.assertThat("Verify Create Group Popup Appeared", GroupsPage.isAddGroupPopupOpened(), is(true));

        step("Log out From Admin dashboard");
        homePage.logoutFromAdminDashboard();
    }

    @DataProvider(name = "verifyAddGroupPopup_DataProvider")
    public Object[][] verifyAddGroupPopup_DataProvider() {
        return getObjectParams("verifyAddGroupPopup");
    }

    @Author(names = {"Harshit Ranjan"})
    @Module(name = "Travel policy")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test(dataProvider = "verifyAllFieldsAreShownOnTravelPolicyPage_DataProvider")
    public void verifyAllFieldsAreShownOnTravelPolicyPage(Map<String, String> param) throws Exception {
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePage<?> homePage = new HomePage<>(session);
        LoginB2CInDWEB<?> loginB2CInDWEB = new LoginB2CInDWEB<>(session);
        CorporateExternalDashboardSidebar<?> CorporateExternalDashboardSidebar = new CorporateExternalDashboardSidebar<>(session);
        TravelPolicyPage<?> TravelPolicyPage = new TravelPolicyPage<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");

        step("Login via cookie injection and open corporate dashboard");
        homePage.loginViaCookieAndOpenCorporateDashboard(email, password);

        step("Check if user has successfully logged in");
        boolean isUserLoggedIn = loginB2CInDWEB.checkIfLoggedIn();

        step("Assert that user is logged in");
        Assertion.assertThat("Is user logged in", isUserLoggedIn, is(true));

        step("Open the Travel Policy");
        CorporateExternalDashboardSidebar.goToTravelPolicy();

        step("Verify Travel Policy Page is Opened");
        Assertion.assertThat("Travel Policy Page is opened", TravelPolicyPage.isTravelPolicyPageOpened(), is(true));

        step("Verify All Fields are shown");
        Assertion.assertThat("Verify If Policy Names is Displayed", TravelPolicyPage.isFieldPresent("Policy name"), is(true));
        Assertion.assertThat("Verify If Department Names is Displayed", TravelPolicyPage.isFieldPresent("Departments"), is(true));
        Assertion.assertThat("Verify If Group Names is Displayed", TravelPolicyPage.isFieldPresent("Groups"), is(true));
        Assertion.assertThat("Verify If Other Employee Names is Displayed", TravelPolicyPage.isFieldPresent("Other"), is(true));

        step("Log out From Admin dashboard");
        homePage.logoutFromAdminDashboard();
    }

    @DataProvider(name = "verifyAllFieldsAreShownOnTravelPolicyPage_DataProvider")
    public Object[][] verifyAllFieldsAreShownOnTravelPolicyPage_DataProvider() {
        return getObjectParams("verifyAllFieldsAreShownOnTravelPolicyPage");
    }

    @Author(names = {"Harshit Ranjan"})
    @Module(name = "Travel policy")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test(dataProvider = "verifyEditTravelPolicyFlow_DataProvider")
    public void verifyEditTravelPolicyFlow(Map<String, String> param) throws Exception {
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePage<?> homePage = new HomePage<>(session);
        LoginB2CInDWEB<?> loginB2CInDWEB = new LoginB2CInDWEB<>(session);
        CorporateExternalDashboardSidebar<?> CorporateExternalDashboardSidebar = new CorporateExternalDashboardSidebar<>(session);
        TravelPolicyPage<?> TravelPolicyPage = new TravelPolicyPage<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");

        step("Login via cookie injection and open corporate dashboard");
        homePage.loginViaCookieAndOpenCorporateDashboard(email, password);

        step("Check if user has successfully logged in");
        boolean isUserLoggedIn = loginB2CInDWEB.checkIfLoggedIn();

        step("Assert that user is logged in");
        Assertion.assertThat("Is user logged in", isUserLoggedIn, is(true));

        step("Open the Travel Policy");
        CorporateExternalDashboardSidebar.goToTravelPolicy();

        step("Verify Travel Policy Page is Opened");
        Assertion.assertThat("Travel Policy Page is opened", TravelPolicyPage.isTravelPolicyPageOpened(), is(true));

        step("Verify All Fields are shown");
        Assertion.assertThat("Verify If Policy Names is Displayed", TravelPolicyPage.isFieldPresent("Policy name"), is(true));

        step("Open Policy Detail page by clicking on one policy name");
        TravelPolicyPage.openPolicyDetail();

        step("Click on Edit button");
        TravelPolicyPage.clickEditPolicy();

        Assertion.assertThat("Verify Edit Travel Policy Form is Opened", TravelPolicyPage.isEditPolicyFormPageOpened(), is(true));

        step("Log out From Admin dashboard");
        homePage.logoutFromAdminDashboard();
    }

    @DataProvider(name = "verifyEditTravelPolicyFlow_DataProvider")
    public Object[][] verifyEditTravelPolicyFlow_DataProvider() {
        return getObjectParams("verifyEditTravelPolicyFlow");
    }

    @Author(names = {"Harshit Ranjan"})
    @Module(name = "Travel policy")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test(dataProvider = "onlyShowAssignedPolicyVisibleToApprover_DataProvider")
    public void onlyShowAssignedPolicyVisibleToApprover(Map<String, String> param) throws Exception {
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePage<?> homePage = new HomePage<>(session);
        LoginB2CInDWEB<?> loginB2CInDWEB = new LoginB2CInDWEB<>(session);
        CorporateExternalDashboardSidebar<?> CorporateExternalDashboardSidebar = new CorporateExternalDashboardSidebar<>(session);
        TravelPolicyPage<?> TravelPolicyPage = new TravelPolicyPage<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");

        step("Login via cookie injection and open corporate dashboard");
        homePage.loginViaCookieAndOpenCorporateDashboard(email, password);

        step("Check if user has successfully logged in");
        boolean isUserLoggedIn = loginB2CInDWEB.checkIfLoggedIn();

        step("Assert that user is logged in");
        Assertion.assertThat("Is user logged in", isUserLoggedIn, is(true));

        step("Open the Travel Policy");
        CorporateExternalDashboardSidebar.GoToPolicyNonAdmin();

        step("Verify Only Assigned Travel Policy is Shown");
        Assertion.assertThat("Only Applied Travel Policy is shown", TravelPolicyPage.YourTravelPolicyIsShown(), is(true));
        Assertion.assertThat("Add policy button is not shown", TravelPolicyPage.AddPolicyButtonNotAppeared(), is(true));

        step("Log out From Admin dashboard");
        homePage.logoutFromAdminDashboard();
    }

    @DataProvider(name = "onlyShowAssignedPolicyVisibleToApprover_DataProvider")
    public Object[][] onlyShowAssignedPolicyVisibleToApprover_DataProvider() {
        return getObjectParams("onlyShowAssignedPolicyVisibleToApprover");
    }

    @Author(names = {"Harshit Ranjan"})
    @Module(name = "Add Employee")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test(dataProvider = "DeleteOtherAdmin_DataProvider")
    public void DeleteOtherAdmin(Map<String, String> param) throws Exception {
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePage<?> homePage = new HomePage<>(session);
        LoginB2CInDWEB<?> loginB2CInDWEB = new LoginB2CInDWEB<>(session);
        EmployeeListPage<?> employeeListPage = new EmployeeListPage<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");

        step("Login via cookie injection and open corporate dashboard");
        homePage.loginViaCookieAndOpenCorporateDashboard(email, password);

        boolean isUserLoggedIn = loginB2CInDWEB.checkIfLoggedIn();

        step("Check that user is logged in");
        Assertion.assertThat("Is user logged in", isUserLoggedIn, is(true));

        step("Open the Employee List");
        employeeListPage.goToEmployeeListPage();

        employeeListPage.setRegistrationStatusFilter();

        step("click on 3 dots Action Button and Delete");
        employeeListPage.deleteEmployee();

        String text = employeeListPage.getSnackbarText();

        Assertion.assertThat("Verify SnackBar  Contains Success text", text, containsString("deleted successfully"));

        step("Log out From Admin dashboard");
        homePage.logoutFromAdminDashboard();
    }

    @DataProvider(name = "DeleteOtherAdmin_DataProvider")
    public Object[][] DeleteOtherAdmin_DataProvider() {
        return getObjectParams("DeleteOtherAdmin");
    }

    @Author(names = {"Harshit Ranjan"})
    @Module(name = "Travel policy")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test(dataProvider = "onlyShowAssignedPolicyVisibleToTraveller_DataProvider")
    public void onlyShowAssignedPolicyVisibleToTraveller(Map<String, String> param) throws Exception {
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePage<?> homePage = new HomePage<>(session);
        LoginB2CInDWEB<?> loginB2CInDWEB = new LoginB2CInDWEB<>(session);
        CorporateExternalDashboardSidebar<?> CorporateExternalDashboardSidebar = new CorporateExternalDashboardSidebar<>(session);
        TravelPolicyPage<?> TravelPolicyPage = new TravelPolicyPage<>(session);
        EmployeeListPage<?> employeeListPage = new EmployeeListPage<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");

        step("Login via cookie injection");
        homePage.loginViaCookie(email, password);

        boolean isUserLoggedIn = loginB2CInDWEB.checkIfUserInB2CPage();
        step("Assert that user is logged in");
        Assertion.assertThat("Is user logged in", isUserLoggedIn, is(true));

        employeeListPage.openTravelerDashboard();

        step("Open the Travel Policy");
        CorporateExternalDashboardSidebar.GoToPolicyNonAdmin();

        step("Verify Only Assigned Travel Policy is Shown");
        Assertion.assertThat("Only Applied Travel Policy is shown", TravelPolicyPage.YourTravelPolicyIsShown(), is(true));
        Assertion.assertThat("Add policy button is not shown", TravelPolicyPage.AddPolicyButtonNotAppeared(), is(true));

        step("Log out From Admin dashboard");
        homePage.logoutFromAdminDashboard();
    }

    @DataProvider(name = "onlyShowAssignedPolicyVisibleToTraveller_DataProvider")
    public Object[][] onlyShowAssignedPolicyVisibleToTraveller_DataProvider() {
        return getObjectParams("onlyShowAssignedPolicyVisibleToTraveller");
    }

    @Author(names = {"Harshit Ranjan"})
    @Module(name = "Travel policy")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test(dataProvider = "verifyCustomFieldTabAndPageRedirection_DataProvider")
    public void verifyCustomFieldTabAndPageRedirection(Map<String, String> param) throws Exception {
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePage<?> homePage = new HomePage<>(session);
        LoginB2CInDWEB<?> loginB2CInDWEB = new LoginB2CInDWEB<>(session);
        CorporateExternalDashboardSidebar<?> CorporateExternalDashboardSidebar = new CorporateExternalDashboardSidebar<>(session);
        CustomFieldPage<?> customFieldPage = new CustomFieldPage<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");

        step("Login via cookie injection and open corporate dashboard");
        homePage.loginViaCookieAndOpenCorporateDashboard(email, password);

        boolean isUserLoggedIn = loginB2CInDWEB.checkIfLoggedIn();

        step("Check that user is logged in");
        Assertion.assertThat("Is user logged in", isUserLoggedIn, is(true));

        step("Click on Settings on Sidebar");
        CorporateExternalDashboardSidebar.clickOnSettings();

        Assertion.assertThat("Verify CustomField Option is Present on Sidebar", CorporateExternalDashboardSidebar.isCustomFieldOptionPresent(), is(true));

        step("Click on Custom Field Sidebar to check its redirection");
        CorporateExternalDashboardSidebar.goToTravelerCustomField();

        Assertion.assertThat("Verify Custom field page is Opened and Heading is Present", customFieldPage.isCustomFieldHeadingVisible(), is(true));
        Assertion.assertThat("Verify Add Custom Field Button is Present", customFieldPage.isAddCustomFieldButton(), is(true));

        step("Log out From Admin dashboard");
        homePage.logoutFromAdminDashboard();
    }

    @DataProvider(name = "verifyCustomFieldTabAndPageRedirection_DataProvider")
    public Object[][] verifyCustomFieldTabAndPageRedirection_DataProvider() {
        return getObjectParams("verifyCustomFieldTabAndPageRedirection");
    }

    @Author(names = {"Harshit Ranjan"})
    @Module(name = "Travel policy")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test(dataProvider = "verifyCustomFieldsForm_DataProvider")
    public void verifyCustomFieldsForm(Map<String, String> param) throws Exception {
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePage<?> homePage = new HomePage<>(session);
        LoginB2CInDWEB<?> loginB2CInDWEB = new LoginB2CInDWEB<>(session);
        CorporateExternalDashboardSidebar<?> CorporateExternalDashboardSidebar = new CorporateExternalDashboardSidebar<>(session);
        CustomFieldPage<?> customFieldPage = new CustomFieldPage<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");

        step("Login via cookie injection and open corporate dashboard");
        homePage.loginViaCookieAndOpenCorporateDashboard(email, password);

        boolean isUserLoggedIn = loginB2CInDWEB.checkIfLoggedIn();

        step("Check that user is logged in");
        Assertion.assertThat("Is user logged in", isUserLoggedIn, is(true));

        step("Click on Settings on Sidebar");
        CorporateExternalDashboardSidebar.clickOnSettings();

        Assertion.assertThat("Verify CustomField Option is Present on Sidebar", CorporateExternalDashboardSidebar.isCustomFieldOptionPresent(), is(true));

        step("Click on Custom Field Sidebar to check its redirection");
        CorporateExternalDashboardSidebar.goToTravelerCustomField();

        Assertion.assertThat("Verify Custom field page is Opened and Heading is Present", customFieldPage.isCustomFieldHeadingVisible(), is(true));
        customFieldPage.clickAddCustomFieldButton();

        Assertion.assertThat("Verify Custom Field Form is Opened", customFieldPage.isFormHeadingPresent(), is(true));
        Assertion.assertThat("Verify Field Name Input is present", customFieldPage.isFieldNameInputPresent(), is(true));
        Assertion.assertThat("Verify Field Type Input is present", customFieldPage.isFieldTypeInputPresent(), is(true));
        Assertion.assertThat("Verify Short Description Input Input is present", customFieldPage.isShortDescriptionInputPresent(), is(true));
        Assertion.assertThat("Verify Save button is Present on the form", customFieldPage.isSaveFormButtonPresent(), is(true));

        step("Log out From Admin dashboard");
        homePage.logoutFromAdminDashboard();
    }

    @DataProvider(name = "verifyCustomFieldsForm_DataProvider")
    public Object[][] verifyCustomFieldsForm_DataProvider() {
        return getObjectParams("verifyCustomFieldsForm");
    }

    @Author(names = {"Harshit Ranjan"})
    @Module(name = "Travel policy")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test(dataProvider = "verifySuperAdminCanAddCustomFieldSingle_DataProvider")
    public void verifySuperAdminCanAddCustomFieldSingle(Map<String, String> param) throws Exception {
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePage<?> homePage = new HomePage<>(session);
        LoginB2CInDWEB<?> loginB2CInDWEB = new LoginB2CInDWEB<>(session);
        CorporateExternalDashboardSidebar<?> CorporateExternalDashboardSidebar = new CorporateExternalDashboardSidebar<>(session);
        CustomFieldPage<?> customFieldPage = new CustomFieldPage<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");

        step("Login via cookie injection and open corporate dashboard");
        homePage.loginViaCookieAndOpenCorporateDashboard(email, password);

        boolean isUserLoggedIn = loginB2CInDWEB.checkIfLoggedIn();

        step("Check that user is logged in");
        Assertion.assertThat("Is user logged in", isUserLoggedIn, is(true));

        step("Click on Settings on Sidebar");
        CorporateExternalDashboardSidebar.clickOnSettings();

        Assertion.assertThat("Verify CustomField Option is Present on Sidebar", CorporateExternalDashboardSidebar.isCustomFieldOptionPresent(), is(true));

        step("Click on Custom Field Sidebar to check its redirection");
        CorporateExternalDashboardSidebar.goToTravelerCustomField();

        Assertion.assertThat("Verify Custom field page is Opened and Heading is Present", customFieldPage.isCustomFieldHeadingVisible(), is(true));
        customFieldPage.clickAddCustomFieldButton();

        Assertion.assertThat("Verify Custom Field Form is Opened", customFieldPage.isFormHeadingPresent(), is(true));

        String FieldName = CoreFunctions.randomName() + "AutoField";
        String Option1 = CoreFunctions.generateRandomString(4) + "Auto-Option";
        String Option2 = CoreFunctions.generateRandomString(4) + "Auto-Option2";

        step("Create a Single selection custom Field");

        customFieldPage.checkInterimPage();
        customFieldPage.enterFieldName(FieldName);

        customFieldPage.fillCustomFieldForm("Single Selection", Option1, Option2);
        String text = customFieldPage.getSnackbarText();
        Assertion.assertThat("Success Snack bar shows the name of Custom Field Added", text, containsString(FieldName));

        customFieldPage.deleteCustomField();
        customFieldPage.verifyDeleteSnackbar();

        step("Log out From Admin dashboard");
        homePage.logoutFromAdminDashboard();

    }

    @DataProvider(name = "verifySuperAdminCanAddCustomFieldSingle_DataProvider")
    public Object[][] verifySuperAdminCanAddCustomFieldSingle_DataProvider() {
        return getObjectParams("verifySuperAdminCanAddCustomFieldSingle");
    }

    @Author(names = {"Harshit Ranjan"})
    @Module(name = "Travel policy")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test(dataProvider = "verifyAdminCanAddCustomFieldMultiple_DataProvider")
    public void verifyAdminCanAddCustomFieldMultiple(Map<String, String> param) throws Exception {
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePage<?> homePage = new HomePage<>(session);
        LoginB2CInDWEB<?> loginB2CInDWEB = new LoginB2CInDWEB<>(session);
        CorporateExternalDashboardSidebar<?> CorporateExternalDashboardSidebar = new CorporateExternalDashboardSidebar<>(session);
        CustomFieldPage<?> customFieldPage = new CustomFieldPage<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");

        step("Login via cookie injection and open corporate dashboard");
        homePage.loginViaCookieAndOpenCorporateDashboard(email, password);

        boolean isUserLoggedIn = loginB2CInDWEB.checkIfLoggedIn();

        step("Check that user is logged in");
        Assertion.assertThat("Is user logged in", isUserLoggedIn, is(true));

        step("Click on Settings on Sidebar");
        CorporateExternalDashboardSidebar.clickOnSettings();

        Assertion.assertThat("Verify CustomField Option is Present on Sidebar", CorporateExternalDashboardSidebar.isCustomFieldOptionPresent(), is(true));

        step("Click on Custom Field Sidebar to check its redirection");
        CorporateExternalDashboardSidebar.goToTravelerCustomField();

        Assertion.assertThat("Verify Custom field page is Opened and Heading is Present", customFieldPage.isCustomFieldHeadingVisible(), is(true));
        customFieldPage.clickAddCustomFieldButton();

        Assertion.assertThat("Verify Custom Field Form is Opened", customFieldPage.isFormHeadingPresent(), is(true));

        String FieldName = CoreFunctions.randomName() + "AutoField";
        String Option1 = CoreFunctions.generateRandomString(4) + "Auto-Option";
        String Option2 = CoreFunctions.generateRandomString(4) + "Auto-Option2";
        String Option3 = CoreFunctions.generateRandomString(4) + "Auto-Option3";
        String Option4 = CoreFunctions.generateRandomString(4) + "Auto-Option4";

        step("Create a Multiple selection custom Field");

        customFieldPage.checkInterimPage();
        customFieldPage.enterFieldName(FieldName);

        customFieldPage.fillCustomFieldForm("Multiple Selection", Option1, Option2, Option3, Option4);
        String text = customFieldPage.getSnackbarText();
        Assertion.assertThat("Success Snack bar shows the name of Custom Field Added", text, containsString(FieldName));

        customFieldPage.deleteCustomField();
        customFieldPage.verifyDeleteSnackbar();

        step("Log out From Admin dashboard");
        homePage.logoutFromAdminDashboard();

    }

    @DataProvider(name = "verifyAdminCanAddCustomFieldMultiple_DataProvider")
    public Object[][] verifyAdminCanAddCustomFieldMultiple_DataProvider() {
        return getObjectParams("verifyAdminCanAddCustomFieldMultiple");
    }

    @Author(names = {"Harshit Ranjan"})
    @Module(name = "Travel policy")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test(dataProvider = "verifyAdminCanAddCustomFieldShortAnswer_DataProvider")
    public void verifyAdminCanAddCustomFieldShortAnswer(Map<String, String> param) throws Exception {
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePage<?> homePage = new HomePage<>(session);
        LoginB2CInDWEB<?> loginB2CInDWEB = new LoginB2CInDWEB<>(session);
        CorporateExternalDashboardSidebar<?> corporateExternalDashboardSidebar = new CorporateExternalDashboardSidebar<>(session);
        CustomFieldPage<?> customFieldPage = new CustomFieldPage<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");

        step("Login via cookie injection and open corporate dashboard");
        homePage.loginViaCookieAndOpenCorporateDashboard(email, password);

        boolean isUserLoggedIn = loginB2CInDWEB.checkIfLoggedIn();

        step("Check that user is logged in");
        Assertion.assertThat("Is user logged in", isUserLoggedIn, is(true));

        step("Click on Settings on Sidebar");
        corporateExternalDashboardSidebar.clickOnSettings();

        Assertion.assertThat("Verify CustomField Option is Present on Sidebar", corporateExternalDashboardSidebar.isCustomFieldOptionPresent(), is(true));

        step("Click on Custom Field Sidebar to check its redirection");
        corporateExternalDashboardSidebar.goToTravelerCustomField();

        Assertion.assertThat("Verify Custom field page is Opened and Heading is Present", customFieldPage.isCustomFieldHeadingVisible(), is(true));
        customFieldPage.clickAddCustomFieldButton();

        Assertion.assertThat("Verify Custom Field Form is Opened", customFieldPage.isFormHeadingPresent(), is(true));

        String fieldName = CoreFunctions.randomName() + "AutoField";

        step("Create a Short Answer custom Field");

        customFieldPage.checkInterimPage();
        customFieldPage.enterFieldName(fieldName);

        customFieldPage.fillCustomFieldForm("Short Answer");
        String text = customFieldPage.getSnackbarText();
        Assertion.assertThat("Success Snack bar shows the name of Custom Field Added", text, containsString(fieldName));

        customFieldPage.deleteCustomField();
        customFieldPage.verifyDeleteSnackbar();

        step("Log out From Admin dashboard");
        homePage.logoutFromAdminDashboard();

    }

    @DataProvider(name = "verifyAdminCanAddCustomFieldShortAnswer_DataProvider")
    public Object[][] verifyAdminCanAddCustomFieldShortAnswer_DataProvider() {
        return getObjectParams("verifyAdminCanAddCustomFieldShortAnswer");
    }

    @Author(names = {"Harshit Ranjan"})
    @Module(name = "Travel policy")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test(dataProvider = "verifyAdminCanAddCustomFieldLongAnswer_DataProvider")
    public void verifyAdminCanAddCustomFieldLongAnswer(Map<String, String> param) throws Exception {
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePage<?> homePage = new HomePage<>(session);
        LoginB2CInDWEB<?> loginB2CInDWEB = new LoginB2CInDWEB<>(session);
        CorporateExternalDashboardSidebar<?> corporateExternalDashboardSidebar = new CorporateExternalDashboardSidebar<>(session);
        CustomFieldPage<?> customFieldPage = new CustomFieldPage<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");

        step("Login via cookie injection and open corporate dashboard");
        homePage.loginViaCookieAndOpenCorporateDashboard(email, password);

        boolean isUserLoggedIn = loginB2CInDWEB.checkIfLoggedIn();

        step("Check that user is logged in");
        Assertion.assertThat("Is user logged in", isUserLoggedIn, is(true));

        step("Click on Settings on Sidebar");
        corporateExternalDashboardSidebar.clickOnSettings();

        Assertion.assertThat("Verify CustomField Option is Present on Sidebar", corporateExternalDashboardSidebar.isCustomFieldOptionPresent(), is(true));

        step("Click on Custom Field Sidebar to check its redirection");
        corporateExternalDashboardSidebar.goToTravelerCustomField();

        Assertion.assertThat("Verify Custom field page is Opened and Heading is Present", customFieldPage.isCustomFieldHeadingVisible(), is(true));
        customFieldPage.clickAddCustomFieldButton();

        Assertion.assertThat("Verify Custom Field Form is Opened", customFieldPage.isFormHeadingPresent(), is(true));

        String fieldName = CoreFunctions.randomName() + "AutoField";

        step("Create a Long text  custom Field");

        customFieldPage.checkInterimPage();
        customFieldPage.enterFieldName(fieldName);

        customFieldPage.fillCustomFieldForm("Long Text");
        String text = customFieldPage.getSnackbarText();
        Assertion.assertThat("Success Snack bar shows the name of Custom Field Added", text, containsString(fieldName));

        customFieldPage.deleteCustomField();
        customFieldPage.verifyDeleteSnackbar();

        step("Log out From Admin dashboard");
        homePage.logoutFromAdminDashboard();

    }

    @DataProvider(name = "verifyAdminCanAddCustomFieldLongAnswer_DataProvider")
    public Object[][] verifyAdminCanAddCustomFieldLongAnswer_DataProvider() {
        return getObjectParams("verifyAdminCanAddCustomFieldLongAnswer");
    }

    @Author(names = {"Harshit Ranjan"})
    @Module(name = "Travel policy")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test(dataProvider = "verifyAdminCanAddCustomFieldFileUpload_DataProvider")
    public void verifyAdminCanAddCustomFieldFileUpload(Map<String, String> param) throws Exception {
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePage<?> homePage = new HomePage<>(session);
        LoginB2CInDWEB<?> loginB2CInDWEB = new LoginB2CInDWEB<>(session);
        CorporateExternalDashboardSidebar<?> corporateExternalDashboardSidebar = new CorporateExternalDashboardSidebar<>(session);
        CustomFieldPage<?> customFieldPage = new CustomFieldPage<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");

        step("Login via cookie injection and open corporate dashboard");
        homePage.loginViaCookieAndOpenCorporateDashboard(email, password);

        boolean isUserLoggedIn = loginB2CInDWEB.checkIfLoggedIn();

        step("Check that user is logged in");
        Assertion.assertThat("Is user logged in", isUserLoggedIn, is(true));

        step("Click on Settings on Sidebar");
        corporateExternalDashboardSidebar.clickOnSettings();

        Assertion.assertThat("Verify CustomField Option is Present on Sidebar", corporateExternalDashboardSidebar.isCustomFieldOptionPresent(), is(true));

        step("Click on Custom Field Sidebar to check its redirection");
        corporateExternalDashboardSidebar.goToTravelerCustomField();

        Assertion.assertThat("Verify Custom field page is Opened and Heading is Present", customFieldPage.isCustomFieldHeadingVisible(), is(true));
        customFieldPage.clickAddCustomFieldButton();

        Assertion.assertThat("Verify Custom Field Form is Opened", customFieldPage.isFormHeadingPresent(), is(true));

        String fieldName = CoreFunctions.randomName() + "AutoField";

        step("Create a File upload  custom Field");

        customFieldPage.checkInterimPage();
        customFieldPage.enterFieldName(fieldName);

        customFieldPage.fillCustomFieldForm("File Upload");
        String text = customFieldPage.getSnackbarText();
        Assertion.assertThat("Success Snack bar shows the name of Custom Field Added", text, containsString(fieldName));

        customFieldPage.deleteCustomField();
        customFieldPage.verifyDeleteSnackbar();

        step("Log out From Admin dashboard");
        homePage.logoutFromAdminDashboard();

    }

    @DataProvider(name = "verifyAdminCanAddCustomFieldFileUpload_DataProvider")
    public Object[][] verifyAdminCanAddCustomFieldFileUpload_DataProvider() {
        return getObjectParams("verifyAdminCanAddCustomFieldFileUpload");
    }

    @Author(names = {"Harshit"})
    @Module(name = "Document Setting")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test(dataProvider = "verifyDocumentSettingsPage_DataProvider")
    public void verifyDocumentSettingsPage(Map<String, String> param) throws Exception {
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<>(session);
        CorporateInternalDashboardSidebar<?> corporateInternalDashboardSidebar = new CorporateInternalDashboardSidebar<>(session);
        DocumentSettingPage<?> documentSettingPage = new DocumentSettingPage<>(session);

        String email = param.get("email");
        String password = param.get("password");

        step("Login via cookie injection");
        loginMemberAdmin.loginViaCookie(email, password);
        boolean isLoggedInMemberAdmin = true;

        Assertion.assertThat("Verify User is logged in", isLoggedInMemberAdmin, is(true));

        step("Go to Corporate Internal Dashboard");
        loginMemberAdmin.goToDashboard();

        corporateInternalDashboardSidebar.goToDocumentSetting();

        step("Go to Document setting page");
        String govt = documentSettingPage.isGovtTabVisible();
        String indo = documentSettingPage.isIndonesianTabVisible();
        String foreign = documentSettingPage.isForeignTabVisible();

        Assertion.assertThat("Verify Indonesian Tab name is correct", indo, containsString("Indonesian"));
        Assertion.assertThat("Verify Foreign Tab name is correct", foreign, containsString("Foreign"));
        Assertion.assertThat("Verify Govt Tab name is correct", govt, containsString("Government"));

        step("Logout");
        loginMemberAdmin.logout();
    }

    @DataProvider(name = "verifyDocumentSettingsPage_DataProvider")
    public Object[][] verifyDocumentSettingsPage_DataProvider() {
        return getObjectParams("verifyDocumentSettingsPage");
    }

    @Author(names = {"Harshit"})
    @Module(name = "Document Setting")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test(dataProvider = "verifyAMSuperVisorCanAddIndoDoc_DataProvider")
    public void verifyAMSuperVisorCanAddIndoDoc(Map<String, String> param) throws Exception {
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<>(session);
        CorporateInternalDashboardSidebar<?> corporateInternalDashboardSidebar = new CorporateInternalDashboardSidebar<>(session);
        DocumentSettingPage<?> documentSettingPage = new DocumentSettingPage<>(session);

        String email = param.get("email");
        String password = param.get("password");

        step("Login via cookie injection");
        loginMemberAdmin.loginViaCookie(email, password);
        boolean isLoggedInMemberAdmin = true;

        Assertion.assertThat("Verify User is logged in", isLoggedInMemberAdmin, is(true));

        step("Go to Corporate Internal Dashboard");
        loginMemberAdmin.goToDashboard();

        corporateInternalDashboardSidebar.goToDocumentSetting();

        step("Go to Document setting page");
        String indo = documentSettingPage.isIndonesianTabVisible();
        Assertion.assertThat("Verify Indonesian Tab name is correct", indo, containsString("Indonesian"));

        step("Add new Doc for Indonesian company");
        String docName = CoreFunctions.randomName() + "Doc";
        documentSettingPage.addNewDoc(docName);

        step("New doc is Added");
        Assertion.assertThat("verify New Doc is added", documentSettingPage.isNewDocumentAdded(docName), is(true));

        step("Logout");
        loginMemberAdmin.logout();
    }

    @DataProvider(name = "verifyAMSuperVisorCanAddIndoDoc_DataProvider")
    public Object[][] verifyAMSuperVisorCanAddIndoDoc_DataProvider() {
        return getObjectParams("verifyAMSuperVisorCanAddIndoDoc");
    }

    @Author(names = {"Harshit Ranjan"})
    @Module(name = "Travel policy")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test(dataProvider = "verifyAdminCanDeactivateCustomField_DataProvider")
    public void verifyAdminCanDeactivateCustomField(Map<String, String> param) throws Exception {
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePage<?> homePage = new HomePage<>(session);
        LoginB2CInDWEB<?> loginB2CInDWEB = new LoginB2CInDWEB<>(session);
        CorporateExternalDashboardSidebar<?> corporateExternalDashboardSidebar = new CorporateExternalDashboardSidebar<>(session);
        CustomFieldPage<?> customFieldPage = new CustomFieldPage<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");

        step("Login via cookie injection and open corporate dashboard");
        homePage.loginViaCookieAndOpenCorporateDashboard(email, password);

        boolean isUserLoggedIn = loginB2CInDWEB.checkIfLoggedIn();

        step("Check that user is logged in");
        Assertion.assertThat("Is user logged in", isUserLoggedIn, is(true));

        step("Click on Settings on Sidebar");
        corporateExternalDashboardSidebar.clickOnSettings();

        Assertion.assertThat("Verify CustomField Option is Present on Sidebar", corporateExternalDashboardSidebar.isCustomFieldOptionPresent(), is(true));

        step("Click on Custom Field Sidebar to check its redirection");
        corporateExternalDashboardSidebar.goToTravelerCustomField();

        Assertion.assertThat("Verify Custom field page is Opened and Heading is Present", customFieldPage.isCustomFieldHeadingVisible(), is(true));

        step("Deactivate Custom Field");
        String text = customFieldPage.deactivateCustomField();

        Assertion.assertThat("Verify CustomField is deactivated Successfully", text, containsString("deactivated"));

        step("Activate it Successfully again");
        customFieldPage.activateCustomField();

        step("Log out From Admin dashboard");
        homePage.logoutFromAdminDashboard();
    }

    @DataProvider(name = "verifyAdminCanDeactivateCustomField_DataProvider")
    public Object[][] verifyAdminCanDeactivateCustomField_DataProvider() {
        return getObjectParams("verifyAdminCanDeactivateCustomField");
    }


    @Author(names = {"Harshit Ranjan"})
    @Module(name = "Travel policy")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test(dataProvider = "verifyAdminCanDeleteCustomField_DataProvider")
    public void verifyAdminCanDeleteCustomField(Map<String, String> param) throws Exception {
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePage<?> homePage = new HomePage<>(session);
        LoginB2CInDWEB<?> loginB2CInDWEB = new LoginB2CInDWEB<>(session);
        CorporateExternalDashboardSidebar<?> corporateExternalDashboardSidebar = new CorporateExternalDashboardSidebar<>(session);
        CustomFieldPage<?> customFieldPage = new CustomFieldPage<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");

        step("Login via cookie injection and open corporate dashboard");
        homePage.loginViaCookieAndOpenCorporateDashboard(email, password);

        boolean isUserLoggedIn = loginB2CInDWEB.checkIfLoggedIn();

        step("Check that user is logged in");
        Assertion.assertThat("Is user logged in", isUserLoggedIn, is(true));

        step("Click on Settings on Sidebar");
        corporateExternalDashboardSidebar.clickOnSettings();

        Assertion.assertThat("Verify CustomField Option is Present on Sidebar", corporateExternalDashboardSidebar.isCustomFieldOptionPresent(), is(true));

        step("Click on Custom Field Sidebar to check its redirection");
        corporateExternalDashboardSidebar.goToTravelerCustomField();

        Assertion.assertThat("Verify Custom field page is Opened and Heading is Present", customFieldPage.isCustomFieldHeadingVisible(), is(true));

        customFieldPage.clickAddCustomFieldButton();

        String FieldName = CoreFunctions.randomName() + "AutoField";
        String Option1 = CoreFunctions.generateRandomString(4) + "Auto-Option";
        String Option2 = CoreFunctions.generateRandomString(4) + "Auto-Option2";

        step("Create a Single selection custom Field");

        customFieldPage.checkInterimPage();
        customFieldPage.enterFieldName(FieldName);

        customFieldPage.fillCustomFieldForm("Single Selection", Option1, Option2);
        String text = customFieldPage.getSnackbarText();
        Assertion.assertThat("Success Snack bar shows the name of Custom Field Added", text, containsString(FieldName));

        step("Delete Custom Field");
        customFieldPage.deleteCustomField();

        customFieldPage.verifyDeleteSnackbar();

        step("Log out From Admin dashboard");
        homePage.logoutFromAdminDashboard();

    }

    @DataProvider(name = "verifyAdminCanDeleteCustomField_DataProvider")
    public Object[][] verifyAdminCanDeleteCustomField_DataProvider() {
        return getObjectParams("verifyAdminCanDeleteCustomField");
    }

    @Author(names = {"Harshit"})
    @Module(name = "Document Setting")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test(dataProvider = "verifyAMSuperVisorCanEditIndoCorpDoc_DataProvider")
    public void verifyAMSuperVisorCanEditIndoCorpDoc(Map<String, String> param) throws Exception {
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<>(session);
        CorporateInternalDashboardSidebar<?> corporateInternalDashboardSidebar = new CorporateInternalDashboardSidebar<>(session);
        DocumentSettingPage<?> documentSettingPage = new DocumentSettingPage<>(session);

        String email = param.get("email");
        String password = param.get("password");

        step("Login via cookie injection");
        loginMemberAdmin.loginViaCookie(email, password);
        boolean isLoggedInMemberAdmin = true;

        Assertion.assertThat("Verify User is logged in", isLoggedInMemberAdmin, is(true));

        step("Go to Corporate Internal Dashboard");
        loginMemberAdmin.goToDashboard();

        corporateInternalDashboardSidebar.goToDocumentSetting();

        step("Go to Document setting page");
        String indo = documentSettingPage.isIndonesianTabVisible();
        Assertion.assertThat("Verify Indonesian Tab name is correct", indo, containsString("Indonesian"));

        String text = documentSettingPage.editDocument();
        Assertion.assertThat("Verify Doc Name is edited successfully", text, containsString("update"));
        Assertion.assertThat("Verify newly Edited Doc is visible", documentSettingPage.newlyAddedDocIsVisible(), is(true));

        step("Logout");
        loginMemberAdmin.logout();
    }

    @DataProvider(name = "verifyAMSuperVisorCanEditIndoCorpDoc_DataProvider")
    public Object[][] verifyAMSuperVisorCanEditIndoCorpDoc_DataProvider() {
        return getObjectParams("verifyAMSuperVisorCanEditIndoCorpDoc");
    }

    @Author(names = {"Harshit"})
    @Module(name = "Document Setting")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test(dataProvider = "verifyAMSuperVisorCanDeleteIndoCorpDoc_DataProvider")
    public void verifyAMSuperVisorCanDeleteIndoCorpDoc(Map<String, String> param) throws Exception {
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<>(session);
        CorporateInternalDashboardSidebar<?> corporateInternalDashboardSidebar = new CorporateInternalDashboardSidebar<>(session);
        DocumentSettingPage<?> documentSettingPage = new DocumentSettingPage<>(session);

        String email = param.get("email");
        String password = param.get("password");

        step("Login via cookie injection");
        loginMemberAdmin.loginViaCookie(email, password);
        boolean isLoggedInMemberAdmin = true;

        Assertion.assertThat("Verify User is logged in", isLoggedInMemberAdmin, is(true));

        step("Go to Corporate Internal Dashboard");
        loginMemberAdmin.goToDashboard();

        corporateInternalDashboardSidebar.goToDocumentSetting();

        step("Go to Document setting page");
        String indo = documentSettingPage.isIndonesianTabVisible();
        Assertion.assertThat("Verify Indonesian Tab name is correct", indo, containsString("Indonesian"));

        String text = documentSettingPage.deleteDoc();
        Assertion.assertThat("Verify Doc Name is edited successfully", text, containsString("update"));

        step("Logout");
        loginMemberAdmin.logout();
    }

    @DataProvider(name = "verifyAMSuperVisorCanDeleteIndoCorpDoc_DataProvider")
    public Object[][] verifyAMSuperVisorCanDeleteIndoCorpDoc_DataProvider() {
        return getObjectParams("verifyAMSuperVisorCanDeleteIndoCorpDoc");
    }

    @Author(names = {"Harshit Ranjan"})
    @Module(name = "Travel policy")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test(dataProvider = "verifyAdminCanEditCustomField_DataProvider")
    public void verifyAdminCanEditCustomField(Map<String, String> param) throws Exception {
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePage<?> homePage = new HomePage<>(session);
        LoginB2CInDWEB<?> loginB2CInDWEB = new LoginB2CInDWEB<>(session);
        CorporateExternalDashboardSidebar<?> corporateExternalDashboardSidebar = new CorporateExternalDashboardSidebar<>(session);
        CustomFieldPage<?> customFieldPage = new CustomFieldPage<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");

        step("Login via cookie injection and open corporate dashboard");
        homePage.loginViaCookieAndOpenCorporateDashboard(email, password);

        boolean isUserLoggedIn = loginB2CInDWEB.checkIfLoggedIn();

        step("Check that user is logged in");
        Assertion.assertThat("Is user logged in", isUserLoggedIn, is(true));

        step("Click on Settings on Sidebar");
        corporateExternalDashboardSidebar.clickOnSettings();

        Assertion.assertThat("Verify CustomField Option is Present on Sidebar", corporateExternalDashboardSidebar.isCustomFieldOptionPresent(), is(true));

        step("Click on Custom Field Sidebar to check its redirection");
        corporateExternalDashboardSidebar.goToTravelerCustomField();

        Assertion.assertThat("Verify Custom field page is Opened and Heading is Present", customFieldPage.isCustomFieldHeadingVisible(), is(true));

        step("Edit Custom Field");
        String text = customFieldPage.editCustomField();

        Assertion.assertThat("Custom field is Edited successfully", text, containsString("updated"));

        step("Log out From Admin dashboard");
        homePage.logoutFromAdminDashboard();

    }

    @DataProvider(name = "verifyAdminCanEditCustomField_DataProvider")
    public Object[][] verifyAdminCanEditCustomField_DataProvider() {
        return getObjectParams("verifyAdminCanEditCustomField");
    }

    @Author(names = {"Harshit Ranjan"})
    @Module(name = "Travel policy")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test(dataProvider = "verifyCustomFieldOnAllPages_DataProvider")
    public void verifyCustomFieldOnAllPages(Map<String, String> param) throws Exception {
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePage<?> homePage = new HomePage<>(session);
        LoginB2CInDWEB<?> loginB2CInDWEB = new LoginB2CInDWEB<>(session);
        CorporateExternalDashboardSidebar<?> corporateExternalDashboardSidebar = new CorporateExternalDashboardSidebar<>(session);
        CustomFieldPage<?> customFieldPage = new CustomFieldPage<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");

        step("Login via cookie injection and open corporate dashboard");
        homePage.loginViaCookieAndOpenCorporateDashboard(email, password);

        boolean isUserLoggedIn = loginB2CInDWEB.checkIfLoggedIn();

        step("Check that user is logged in");
        Assertion.assertThat("Is user logged in", isUserLoggedIn, is(true));

        step("Click on Settings on Sidebar");
        corporateExternalDashboardSidebar.clickOnSettings();

        Assertion.assertThat("Verify CustomField Option is Present on Sidebar", corporateExternalDashboardSidebar.isCustomFieldOptionPresent(), is(true));

        step("Click on Custom Field Sidebar to check its redirection");
        corporateExternalDashboardSidebar.goToTravelerCustomField();

        Assertion.assertThat("Verify Custom field page is Opened and Heading is Present", customFieldPage.isCustomFieldHeadingVisible(), is(true));

        step("verify custom fields data for All Pages tab");

        Assertion.assertThat("Verify Field name Header is present", customFieldPage.isFieldNameHeaderPresent(), is(true));
        Assertion.assertThat("Verify Field Type Header is present", customFieldPage.isFieldTypeHeaderPresent(), is(true));
        Assertion.assertThat("Verify Field Status Header is present", customFieldPage.isFieldStatusHeaderPresent(), is(true));
        Assertion.assertThat("Verify Field Requirement Header is present", customFieldPage.isFieldRequirementHeaderPresent(), is(true));

        Assertion.assertThat("Verify Field name row data is present", customFieldPage.isFieldNameRowDataPresent(), is(true));
        Assertion.assertThat("Verify Field Type row data is present", customFieldPage.isFieldTypeRowDataPresent(), is(true));
        Assertion.assertThat("Verify Field Status row data is present", customFieldPage.isFieldStatusRowDataPresent(), is(true));

        step("Log out From Admin dashboard");
        homePage.logoutFromAdminDashboard();

    }

    @DataProvider(name = "verifyCustomFieldOnAllPages_DataProvider")
    public Object[][] verifyCustomFieldOnAllPages_DataProvider() {
        return getObjectParams("verifyCustomFieldOnAllPages");
    }


    @Author(names = {"Harshit Ranjan"})
    @Module(name = "Travel policy")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test(dataProvider = "verifyCustomFieldOnTravelPlanPageTab_DataProvider")
    public void verifyCustomFieldOnTravelPlanPageTab(Map<String, String> param) throws Exception {
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePage<?> homePage = new HomePage<>(session);
        LoginB2CInDWEB<?> loginB2CInDWEB = new LoginB2CInDWEB<>(session);
        CorporateExternalDashboardSidebar<?> corporateExternalDashboardSidebar = new CorporateExternalDashboardSidebar<>(session);
        CustomFieldPage<?> customFieldPage = new CustomFieldPage<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");

        step("Login via cookie injection and open corporate dashboard");
        homePage.loginViaCookieAndOpenCorporateDashboard(email, password);

        boolean isUserLoggedIn = loginB2CInDWEB.checkIfLoggedIn();

        step("Check that user is logged in");
        Assertion.assertThat("Is user logged in", isUserLoggedIn, is(true));

        step("Click on Settings on Sidebar");
        corporateExternalDashboardSidebar.clickOnSettings();

        Assertion.assertThat("Verify CustomField Option is Present on Sidebar", corporateExternalDashboardSidebar.isCustomFieldOptionPresent(), is(true));

        step("Click on Custom Field Sidebar to check its redirection");
        corporateExternalDashboardSidebar.goToTravelerCustomField();

        Assertion.assertThat("Verify Custom field page is Opened and Heading is Present", customFieldPage.isCustomFieldHeadingVisible(), is(true));

        step("Click on Travel Plan page tab");
        customFieldPage.openTravelPlanTab();

        Assertion.assertThat("Verify Field name Header is present", customFieldPage.isFieldNameHeaderPresent(), is(true));
        Assertion.assertThat("Verify Field Type Header is present", customFieldPage.isFieldTypeHeaderPresent(), is(true));
        Assertion.assertThat("Verify Field Status Header is present", customFieldPage.isFieldStatusHeaderPresent(), is(true));
        Assertion.assertThat("Verify Field Requirement Header is present", customFieldPage.isFieldRequirementHeaderPresent(), is(true));

        Assertion.assertThat("Verify Field name row data is present", customFieldPage.isFieldNameRowDataPresent(), is(true));
        Assertion.assertThat("Verify Field Type row data is present", customFieldPage.isFieldTypeRowDataPresent(), is(true));
        Assertion.assertThat("Verify Field Status row data is present", customFieldPage.isFieldStatusRowDataPresent(), is(true));

        step("Log out From Admin dashboard");
        homePage.logoutFromAdminDashboard();

    }

    @DataProvider(name = "verifyCustomFieldOnTravelPlanPageTab_DataProvider")
    public Object[][] verifyCustomFieldOnTravelPlanPageTab_DataProvider() {
        return getObjectParams("verifyCustomFieldOnTravelPlanPageTab");
    }

    @Author(names = {"Harshit Ranjan"})
    @Module(name = "Travel policy")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test(dataProvider = "verifyCustomFieldOnCorporateBookingFormPageTab_DataProvider")
    public void verifyCustomFieldOnCorporateBookingFormPageTab(Map<String, String> param) throws Exception {
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePage<?> homePage = new HomePage<>(session);
        LoginB2CInDWEB<?> loginB2CInDWEB = new LoginB2CInDWEB<>(session);
        CorporateExternalDashboardSidebar<?> corporateExternalDashboardSidebar = new CorporateExternalDashboardSidebar<>(session);
        CustomFieldPage<?> customFieldPage = new CustomFieldPage<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");

        step("Login via cookie injection and open corporate dashboard");
        homePage.loginViaCookieAndOpenCorporateDashboard(email, password);

        boolean isUserLoggedIn = loginB2CInDWEB.checkIfLoggedIn();

        step("Check that user is logged in");
        Assertion.assertThat("Is user logged in", isUserLoggedIn, is(true));

        step("Click on Settings on Sidebar");
        corporateExternalDashboardSidebar.clickOnSettings();

        Assertion.assertThat("Verify CustomField Option is Present on Sidebar", corporateExternalDashboardSidebar.isCustomFieldOptionPresent(), is(true));

        step("Click on Custom Field Sidebar to check its redirection");
        corporateExternalDashboardSidebar.goToTravelerCustomField();

        Assertion.assertThat("Verify Custom field page is Opened and Heading is Present", customFieldPage.isCustomFieldHeadingVisible(), is(true));

        step("Click on Corporate Booking Form page tab");
        customFieldPage.openCorporateBookingFormTab();

        Assertion.assertThat("Verify Field name Header is present", customFieldPage.isFieldNameHeaderPresent(), is(true));
        Assertion.assertThat("Verify Field Type Header is present", customFieldPage.isFieldTypeHeaderPresent(), is(true));
        Assertion.assertThat("Verify Field Status Header is present", customFieldPage.isFieldStatusHeaderPresent(), is(true));
        Assertion.assertThat("Verify Field Requirement Header is present", customFieldPage.isFieldRequirementHeaderPresent(), is(true));

        Assertion.assertThat("Verify Field name row data is present", customFieldPage.isFieldNameRowDataPresent(), is(true));
        Assertion.assertThat("Verify Field Type row data is present", customFieldPage.isFieldTypeRowDataPresent(), is(true));
        Assertion.assertThat("Verify Field Status row data is present", customFieldPage.isFieldStatusRowDataPresent(), is(true));

        step("Log out From Admin dashboard");
        homePage.logoutFromAdminDashboard();

    }

    @DataProvider(name = "verifyCustomFieldOnCorporateBookingFormPageTab_DataProvider")
    public Object[][] verifyCustomFieldOnCorporateBookingFormPageTab_DataProvider() {
        return getObjectParams("verifyCustomFieldOnCorporateBookingFormPageTab");
    }


    @Author(names = {"Harshit"})
    @Module(name = "Document Setting")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test(dataProvider = "verifyAMLeadCanAddForeignDoc_DataProvider")
    public void verifyAMLeadCanAddForeignDoc(Map<String, String> param) throws Exception {
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<>(session);
        CorporateInternalDashboardSidebar<?> corporateInternalDashboardSidebar = new CorporateInternalDashboardSidebar<>(session);
        DocumentSettingPage<?> documentSettingPage = new DocumentSettingPage<>(session);

        String email = param.get("email");
        String password = param.get("password");

        step("Login via cookie injection");
        loginMemberAdmin.loginViaCookie(email, password);
        boolean isLoggedInMemberAdmin = true;

        Assertion.assertThat("Verify User is logged in", isLoggedInMemberAdmin, is(true));

        step("Go to Corporate Internal Dashboard");
        loginMemberAdmin.goToDashboard();

        corporateInternalDashboardSidebar.goToDocumentSetting();

        step("Go to Document setting page");
        String foreign = documentSettingPage.isForeignTabVisible();
        Assertion.assertThat("Verify Foreign Tab name is correct", foreign, containsString("Foreign"));

        documentSettingPage.goToForeignCorp();

        step("Add new Doc for Foreign company");
        String docName = CoreFunctions.randomName() + "Doc";
        documentSettingPage.addNewDoc(docName);

        step("New doc is Added");
        Assertion.assertThat("verify New Doc is added", documentSettingPage.isNewDocumentAdded(docName), is(true));

        documentSettingPage.deleteDoc();

        step("Logout");
        loginMemberAdmin.logout();
    }

    @DataProvider(name = "verifyAMLeadCanAddForeignDoc_DataProvider")
    public Object[][] verifyAMLeadCanAddForeignDoc_DataProvider() {
        return getObjectParams("verifyAMLeadCanAddForeignDoc");
    }

    @Author(names = {"Harshit"})
    @Module(name = "Document Setting")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test(dataProvider = "verifyAMLeadCanEditForeignCorpDoc_DataProvider")
    public void verifyAMLeadCanEditForeignCorpDoc(Map<String, String> param) throws Exception {
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<>(session);
        CorporateInternalDashboardSidebar<?> corporateInternalDashboardSidebar = new CorporateInternalDashboardSidebar<>(session);
        DocumentSettingPage<?> documentSettingPage = new DocumentSettingPage<>(session);

        String email = param.get("email");
        String password = param.get("password");

        step("Login via cookie injection");
        loginMemberAdmin.loginViaCookie(email, password);
        boolean isLoggedInMemberAdmin = true;

        Assertion.assertThat("Verify User is logged in", isLoggedInMemberAdmin, is(true));

        step("Go to Corporate Internal Dashboard");
        loginMemberAdmin.goToDashboard();

        corporateInternalDashboardSidebar.goToDocumentSetting();

        step("Go to Document setting page");
        String foreign = documentSettingPage.isForeignTabVisible();
        Assertion.assertThat("Verify Foreign Tab name is correct", foreign, containsString("Foreign"));

        documentSettingPage.goToForeignCorp();

        String text = documentSettingPage.editDocument();
        Assertion.assertThat("Verify Doc Name is edited successfully", text, containsString("update"));
        Assertion.assertThat("Verify newly Edited Doc is visible", documentSettingPage.newlyAddedDocIsVisible(), is(true));

        step("Logout");
        loginMemberAdmin.logout();
    }

    @DataProvider(name = "verifyAMLeadCanEditForeignCorpDoc_DataProvider")
    public Object[][] verifyAMLeadCanEditForeignCorpDoc_DataProvider() {
        return getObjectParams("verifyAMLeadCanEditForeignCorpDoc");
    }

    @Author(names = {"Harshit"})
    @Module(name = "Document Setting")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test(dataProvider = "verifyAMCorpCanAddGovtDoc_DataProvider")
    public void verifyAMCorpCanAddGovtDoc(Map<String, String> param) throws Exception {
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<>(session);
        CorporateInternalDashboardSidebar<?> corporateInternalDashboardSidebar = new CorporateInternalDashboardSidebar<>(session);
        DocumentSettingPage<?> documentSettingPage = new DocumentSettingPage<>(session);

        String email = param.get("email");
        String password = param.get("password");

        step("Login via cookie injection");
        loginMemberAdmin.loginViaCookie(email, password);
        boolean isLoggedInMemberAdmin = true;

        Assertion.assertThat("Verify User is logged in", isLoggedInMemberAdmin, is(true));

        step("Go to Corporate Internal Dashboard");
        loginMemberAdmin.goToDashboard();

        corporateInternalDashboardSidebar.goToDocumentSetting();

        step("Go to Document setting page ");
        String govt = documentSettingPage.isGovtTabVisible();
        Assertion.assertThat("Verify Govt tab name is correct", govt, containsString("Government"));

        documentSettingPage.goToGovtCorp();

        step("Add new Doc for Govt company");
        String docName = CoreFunctions.randomName() + "Doc";
        documentSettingPage.addNewDoc(docName);

        step("New doc is Added");
        Assertion.assertThat("verify New Doc is added", documentSettingPage.isNewDocumentAdded(docName), is(true));

        documentSettingPage.deleteDoc();
        step("Logout");
        loginMemberAdmin.logout();
    }

    @DataProvider(name = "verifyAMCorpCanAddGovtDoc_DataProvider")
    public Object[][] verifyAMCorpCanAddGovtDoc_DataProvider() {
        return getObjectParams("verifyAMCorpCanAddGovtDoc");
    }

    @Author(names = {"Ram Pratap Singh"})
    @Module(name = "External Dashboard")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test(dataProvider = "verifyGarudaNudgesDisplayedAfterApplyingDirectCode_DataProvider")
    public void verifyGarudaNudgesDisplayedAfterApplyingDirectCode(Map<String, String> param) throws Exception {
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePage<?> homePage = new HomePage<>(session);
        LoginB2CInDWEB<?> loginB2CInDWEB = new LoginB2CInDWEB<>(session);
        CorporateExternalDashboardSidebar<?> corporateExternalDashboardSidebar = new CorporateExternalDashboardSidebar<>(session);
        FlightApi<?> flightApi = new FlightApi<>(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);

        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");
        String airline = param.get("airline");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String departDateApi = CoreFunctions.getRandomLongDateOnRange(daysToDepartureDate, 30, "yyyy-MM-dd");

        int adultPassengers = Integer.parseInt(param.get("adultPassengers"));
        int childPassengers = Integer.parseInt(param.get("childPassengers"));
        int infantPassengers = Integer.parseInt(param.get("infantPassengers"));

        String username = param.get("username");
        String password = param.get("password");
        String firstName = param.get("firstName");
        String lastName = param.get("lastName");

        String email = param.get("email");
        String otp = param.get("otp");

        step("Login via cookie injection and open corporate dashboard");
        homePage.loginViaCookieAndOpenCorporateDashboard(email, password);

        step("Check if user has successfully logged in");
        boolean isUserLoggedIn = loginB2CInDWEB.checkIfLoggedIn();

        step("Assert that user is logged in");
        Assertion.assertThat("Is user logged in", isUserLoggedIn, is(true));

        step("Open the Corporate Information");
        corporateExternalDashboardSidebar.goToCorporateInformation();

        step("complete flight booking via API");
        String orderID = flightApi.createOrderFlightB2BDomestic(username, password, originAirport, destinationAirport, departDateApi, tripType, adultPassengers, childPassengers, infantPassengers, airline, corporateApi.getEmployeeData(username), firstName, lastName);
        Assertion.assertThat("Flight order created", orderID, not(""));

        step("The user navigates to Booking List Page");
        corporateExternalDashboardSidebar.goToBookingListMenu();

        step("The user should be able to see the Garuda Nudge after applying the direct code");
        Assertion.assertThat("Verify Garuda Nudge is displayed after applying direct code", corporateExternalDashboardSidebar.isCorporateDealGarudaVisible(), is(true));

        step("Log out From Admin dashboard");
        homePage.logoutFromAdminDashboard();
    }

    @DataProvider(name = "verifyGarudaNudgesDisplayedAfterApplyingDirectCode_DataProvider")
    public Object[][] verifyGarudaNudgesDisplayedAfterApplyingDirectCode_DataProvider() {
        return getObjectParams("verifyGarudaNudgesDisplayedAfterApplyingDirectCode");
    }

    @Author(names = {"Ram Pratap Singh"})
    @Module(name = "External Dashboard")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test(dataProvider = "verifySearchByRequestId_DataProvider")
    public void verifySearchByRequestId(Map<String, String> param) throws Exception {
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePage<?> homePage = new HomePage<>(session);
        LoginB2CInDWEB<?> loginB2CInDWEB = new LoginB2CInDWEB<>(session);
        CorporateExternalDashboardSidebar<?> corporateExternalDashboardSidebar = new CorporateExternalDashboardSidebar<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        String requestId = param.get("requestId");

        step("Login via cookie injection and open corporate dashboard");
        homePage.loginViaCookieAndOpenCorporateDashboard(email, password);

        step("Check if user has successfully logged in");
        boolean isUserLoggedIn = loginB2CInDWEB.checkIfLoggedIn();

        step("Assert that user is logged in");
        Assertion.assertThat("Is user logged in", isUserLoggedIn, is(true));

        step("User navigates to Deposit History Page");
        corporateExternalDashboardSidebar.goToDepositHistory();

        step("The user searches for a request ID");
        corporateExternalDashboardSidebar.searchByRequestId(requestId);

        step("The user should be able to see the request ID in the search results");
        Assertion.assertThat("Verifying Request ID", corporateExternalDashboardSidebar.isRequestIdDisplayed(requestId), is(true));

        step("Log out From Admin dashboard");
        homePage.logoutFromAdminDashboard();
    }

    @DataProvider(name = "verifySearchByRequestId_DataProvider")
    public Object[][] verifySearchByRequestId_DataProvider() {
        return getObjectParams("verifySearchByRequestId");
    }

    @Author(names = {"Ram Pratap Singh"})
    @Module(name = "External Dashboard")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test(dataProvider = "verifyRefundApprovalConfigAutoSelectsForCorporateTravelers_DataProvider")
    public void verifyRefundApprovalConfigAutoSelectsForCorporateTravelers(Map<String, String> param) throws Exception {
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePage<?> homePage = new HomePage<>(session);
        LoginB2CInDWEB<?> loginB2CInDWEB = new LoginB2CInDWEB<>(session);
        CorporateExternalDashboardSidebar<?> corporateExternalDashboardSidebar = new CorporateExternalDashboardSidebar<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");

        step("Login via cookie injection and open corporate dashboard");
        homePage.loginViaCookieAndOpenCorporateDashboard(email, password);

        step("Check if user has successfully logged in");
        boolean isUserLoggedIn = loginB2CInDWEB.checkIfLoggedIn();

        step("Assert that user is logged in");
        Assertion.assertThat("Is user logged in", isUserLoggedIn, is(true));

        step("User navigates to Company Settings Page");
        corporateExternalDashboardSidebar.goToCompanySettings();

        step("User navigates to Refund $ Reschedule Tab");
        corporateExternalDashboardSidebar.goToRefundAndRescheduleTab();

        step("The user should be able to see the Refund Approval Config Auto Selects for Corporate Travelers");
        Assertion.assertThat("Verifying the Refund field", corporateExternalDashboardSidebar.isRefundAutoApproveMessageDisplayed(), is(true));

        step("Log out From Admin dashboard");
        homePage.logoutFromAdminDashboard();
    }

    @DataProvider(name = "verifyRefundApprovalConfigAutoSelectsForCorporateTravelers_DataProvider")
    public Object[][] verifyRefundApprovalConfigAutoSelectsForCorporateTravelers_DataProvider() {
        return getObjectParams("verifyRefundApprovalConfigAutoSelectsForCorporateTravelers");
    }

    @Author(names = {"Ram Pratap Singh"})
    @Module(name = "External Dashboard")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test(dataProvider = "verifyRescheduleApprovalConfigAutoSelectsForCorporateTravelers_DataProvider")
    public void verifyRescheduleApprovalConfigAutoSelectsForCorporateTravelers(Map<String, String> param) throws Exception {
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePage<?> homePage = new HomePage<>(session);
        LoginB2CInDWEB<?> loginB2CInDWEB = new LoginB2CInDWEB<>(session);
        CorporateExternalDashboardSidebar<?> corporateExternalDashboardSidebar = new CorporateExternalDashboardSidebar<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");

        step("Login via cookie injection and open corporate dashboard");
        homePage.loginViaCookieAndOpenCorporateDashboard(email, password);

        step("Check if user has successfully logged in");
        boolean isUserLoggedIn = loginB2CInDWEB.checkIfLoggedIn();

        step("Assert that user is logged in");
        Assertion.assertThat("Is user logged in", isUserLoggedIn, is(true));

        step("User navigates to Company Settings Page");
        corporateExternalDashboardSidebar.goToCompanySettings();

        step("User navigates to Refund $ Reschedule Tab");
        corporateExternalDashboardSidebar.goToRefundAndRescheduleTab();

        step("The user should be able to see the Reschedule Approval Config Auto Selects for Corporate Travelers");
        Assertion.assertThat("Verifying the Reschedule field", corporateExternalDashboardSidebar.isRescheduleAutoApproveMessageDisplayed(), is(true));

        step("Log out From Admin dashboard");
        homePage.logoutFromAdminDashboard();
    }

    @DataProvider(name = "verifyRescheduleApprovalConfigAutoSelectsForCorporateTravelers_DataProvider")
    public Object[][] verifyRescheduleApprovalConfigAutoSelectsForCorporateTravelers_DataProvider() {
        return getObjectParams("verifyRescheduleApprovalConfigAutoSelectsForCorporateTravelers");
    }

    @Author(names = {"Manan Kumar"})
    @Module(name = "Travel policy")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test(dataProvider = "testTravelPolicyFieldIsVisibleOnEditEmployeePage_DataProvider")
    public void testTravelPolicyFieldIsVisibleOnEditEmployeePage(Map<String, String> param) throws Exception {
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePage<?> homePage = new HomePage<>(session);
        EmployeeListPage<?> employeeListPage = new EmployeeListPage<>(session);
        LoginB2CInDWEB<?> loginB2CInDWEB = new LoginB2CInDWEB<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        String policy = param.get("policy");

        step("Login via cookie injection and open corporate dashboard");
        homePage.loginViaCookieAndOpenCorporateDashboard(email, password);

        boolean isUserLoggedIn = loginB2CInDWEB.checkIfLoggedIn();

        step("Check that user is logged in");
        Assertion.assertThat("Is user logged in", isUserLoggedIn, is(true));

        step("User Navigates to Employee List Page.");
        employeeListPage.goToEmployeeListPage();

        employeeListPage.searchNameEmployee("New");

        step("User clicks on Action button.");
        employeeListPage.clickActionOption();

        step("User clicks on Edit Employee option.");
        employeeListPage.clickEditEmployeeOption();

        step("Verify Travel Policy field is visible on Edit Employee page.");
        Assertion.assertThat("Travel Policy field visible", employeeListPage.isTravelPolicyVisible(), is(true));

        step("User Select Travel Policy.");
        employeeListPage.selectTravelPolicy();

        step("User Saves the Travel Policy.");
        employeeListPage.clickSaveButton();

        step("User Again open Travel Policy.");
        employeeListPage.clickActionOption();
        employeeListPage.clickEditEmployeeOption();

        step("Verify Same Policy will be assigned via edit employee pop up");
        Assertion.assertThat("Assigned Policy should be visible", employeeListPage.isSuccessMessageVisible(policy), is(true));
    }

    @DataProvider(name = "testTravelPolicyFieldIsVisibleOnEditEmployeePage_DataProvider")
    public Object[][] testTravelPolicyFieldIsVisibleOnEditEmployeePage_DataProvider() {
        return getObjectParams("testTravelPolicyFieldIsVisibleOnEditEmployeePage");
    }

    @Author(names = {"Manan Kumar"})
    @Module(name = "Travel policy")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test(dataProvider = "testDeleteButtonVisibilityAndEnablementForCorporateAdmins_DataProvider")
    public void testDeleteButtonVisibilityAndEnablementForCorporateAdmins(Map<String, String> param) throws Exception {
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePage<?> homePage = new HomePage<>(session);
        CorporateExternalDashboardSidebar<?> CorporateExternalDashboardSidebar = new CorporateExternalDashboardSidebar<>(session);
        LoginB2CInDWEB<?> loginB2CInDWEB = new LoginB2CInDWEB<>(session);
        TravelPolicyPage<?> travelPolicyPage = new TravelPolicyPage<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");

        step("Login via cookie injection and open corporate dashboard");
        homePage.loginViaCookieAndOpenCorporateDashboard(email, password);

        boolean isUserLoggedIn = loginB2CInDWEB.checkIfLoggedIn();

        step("Check that user is logged in");
        Assertion.assertThat("Is user logged in", isUserLoggedIn, is(true));

        step("User Navigates to the Travel Policy");
        CorporateExternalDashboardSidebar.goToTravelPolicy();

        String PolicyName = CoreFunctions.randomName();

        travelPolicyPage.clickAddPolicy();
        travelPolicyPage.addNewPolicy(PolicyName);

        CorporateExternalDashboardSidebar.goToTravelPolicyPage();

        travelPolicyPage.searchNewPolicy(PolicyName);

        step("User Selects the Policy.");
        travelPolicyPage.openPolicyDetail();

        step("User clicks on Setting option.");
        travelPolicyPage.clickSettingButton();

        step("Verify Delete Policy option is present.");
        Assertion.assertThat("Delete Policy option is present", travelPolicyPage.isDeletePolicyOptionPresent(), is(true));

        step("User Selects the Delete Policy option.");
        travelPolicyPage.selectDeletePolicy();

        step("User clicks on Delete button in the confirmation popup.");
        travelPolicyPage.clickDeleteButton();

        step("Verify that the policy is deleted successfully.");
        Assertion.assertThat("Delete POP_UP is visible", travelPolicyPage.isDeletePolicyPopupVisible(), is(true));
    }

    @DataProvider(name = "testDeleteButtonVisibilityAndEnablementForCorporateAdmins_DataProvider")
    public Object[][] testDeleteButtonVisibilityAndEnablementForCorporateAdmins_DataProvider() {
        return getObjectParams("testDeleteButtonVisibilityAndEnablementForCorporateAdmins");
    }

    @Author(names = {"Manan Kumar"})
    @Module(name = "Travel policy")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test(dataProvider = "testOnlyCorporateAdminsCanAccessSettings_DataProvider")
    public void testOnlyCorporateAdminsCanAccessSettings(Map<String, String> param) throws Exception {
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePage<?> homePage = new HomePage<>(session);
        LoginB2CInDWEB<?> loginB2CInDWEB = new LoginB2CInDWEB<>(session);
        CorporateExternalDashboardSidebar<?> corporateExternalDashboardSidebar = new CorporateExternalDashboardSidebar<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");

        step("Login via cookie injection and open corporate dashboard");
        homePage.loginViaCookieAndOpenCorporateDashboard(email, password);

        boolean isUserLoggedIn = loginB2CInDWEB.checkIfLoggedIn();

        step("Check that user is logged in");
        Assertion.assertThat("Is user logged in", isUserLoggedIn, is(true));

        step("User navigates to Company Settings Page");
        corporateExternalDashboardSidebar.goToCompanySettings();

        step("User made Edit in Travel Policy");
        corporateExternalDashboardSidebar.isTravelPolicyEditable();

        step("User made Edit in Refund & Reschedule.");
        corporateExternalDashboardSidebar.isRefundEditable();

        step("Verify Settings section should be accessible for Corporate Administrator role..");
        Assertion.assertThat("Settings section should be accessible for Corporate Administrator role.", corporateExternalDashboardSidebar.isSettingsSectionAccessByRole(), is(true));
    }

    @DataProvider(name = "testOnlyCorporateAdminsCanAccessSettings_DataProvider")
    public Object[][] testOnlyCorporateAdminsCanAccessSettings_DataProvider() {
        return getObjectParams("testOnlyCorporateAdminsCanAccessSettings");
    }

    @Author(names = {"Manan Kumar"})
    @Module(name = "Travel policy")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test(dataProvider = "testClickingManageProductsRedirectsToManageProductsFlow_DataProvider")
    public void testClickingManageProductsRedirectsToManageProductsFlow(Map<String, String> param) throws Exception {
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePage<?> homePage = new HomePage<>(session);
        CorporateExternalDashboardSidebar<?> CorporateExternalDashboardSidebar = new CorporateExternalDashboardSidebar<>(session);
        LoginB2CInDWEB<?> loginB2CInDWEB = new LoginB2CInDWEB<>(session);
        TravelPolicyPage<?> travelPolicyPage = new TravelPolicyPage<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");

        step("Login via cookie injection and open corporate dashboard");
        homePage.loginViaCookieAndOpenCorporateDashboard(email, password);

        boolean isUserLoggedIn = loginB2CInDWEB.checkIfLoggedIn();

        step("Check that user is logged in");
        Assertion.assertThat("Is user logged in", isUserLoggedIn, is(true));

        step("User Navigates to the Travel Policy");
        CorporateExternalDashboardSidebar.goToTravelPolicy();

        step("User Selects the Policy.");
        travelPolicyPage.openPolicyDetail();

        step("User clicks on Setting option.");
        travelPolicyPage.clickSettingButton();

        step("User Selects the Manage Products option.");
        travelPolicyPage.selectManageProducts();

        step("User Select Other Verticals.");
        travelPolicyPage.selectVerticals();

        step("User clicks on Save button.");
        travelPolicyPage.clickSaveButton();

        step("Verify that the manage product list pop up will open.");
        Assertion.assertThat("manage product list pop up is visible", travelPolicyPage.isManagePopupVisible(), is(true));
    }

    @DataProvider(name = "testClickingManageProductsRedirectsToManageProductsFlow_DataProvider")
    public Object[][] testClickingManageProductsRedirectsToManageProductsFlow_DataProvider() {
        return getObjectParams("testClickingManageProductsRedirectsToManageProductsFlow");
    }

    @Author(names = {"Ram Pratap Singh"})
    @Module(name = "External Dashboard")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test(dataProvider = "verifyRequestDepositTopUpCreatedForApproval_DataProvider")
    public void verifyRequestDepositTopUpCreatedForApproval(Map<String, String> param) throws Exception {
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePage<?> homePage = new HomePage<>(session);
        LoginB2CInDWEB<?> loginB2CInDWEB = new LoginB2CInDWEB<>(session);
        CorporateExternalDashboardSidebar<?> corporateExternalDashboardSidebar = new CorporateExternalDashboardSidebar<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        String amount = param.get("amount");
        String filePath = param.get("filePath");

        step("Login via cookie injection and open corporate dashboard");
        homePage.loginViaCookieAndOpenCorporateDashboard(email, password);

        step("Check if user has successfully logged in");
        boolean isUserLoggedIn = loginB2CInDWEB.checkIfLoggedIn();

        step("Assert that user is logged in");
        Assertion.assertThat("Is user logged in", isUserLoggedIn, is(true));

        step("User navigates to Deposit history page");
        corporateExternalDashboardSidebar.goToDepositHistory();

        step("The user should be able to Request Deposit top up created for approval");
        corporateExternalDashboardSidebar.createRequestDepositTopUp(amount, filePath);

        step("The user should be able to see the Request Deposit top up created for approval");
        Assertion.assertThat("Verifying the approval snackbar", corporateExternalDashboardSidebar.isRequestSubmittedSnackbarDisplayed(), is(true));

        step("Log out From Admin dashboard");
        homePage.logoutFromAdminDashboard();
    }

    @DataProvider(name = "verifyRequestDepositTopUpCreatedForApproval_DataProvider")
    public Object[][] verifyRequestDepositTopUpCreatedForApproval_DataProvider() {
        return getObjectParams("verifyRequestDepositTopUpCreatedForApproval");
    }

    @Author(names = {"Ram Pratap Singh"})
    @Module(name = "External Dashboard")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test(dataProvider = "verifyAdminCanChangePasswordFromSettingsPage_DataProvider")
    public void verifyAdminCanChangePasswordFromSettingsPage(Map<String, String> param) throws Exception {
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePage<?> homePage = new HomePage<>(session);
        LoginB2CInDWEB<?> loginB2CInDWEB = new LoginB2CInDWEB<>(session);
        CorporateExternalDashboardSidebar<?> corporateExternalDashboardSidebar = new CorporateExternalDashboardSidebar<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");

        step("Login via cookie injection and open corporate dashboard");
        homePage.loginViaCookieAndOpenCorporateDashboard(email, password);

        step("Check if user has successfully logged in");
        boolean isUserLoggedIn = loginB2CInDWEB.checkIfLoggedIn();

        step("Assert that user is logged in");
        Assertion.assertThat("Is user logged in", isUserLoggedIn, is(true));

        step("User navigates to Account page");
        corporateExternalDashboardSidebar.goToAccountPage();

        step("The user should be able to change password from Settings page");
        corporateExternalDashboardSidebar.changePassword(password, password);

        step("The user should be able to see the password updated snackbar");
        Assertion.assertThat("Verifying the password updated snackbar", corporateExternalDashboardSidebar.isPasswordUpdatedSnackbarDisplayed(), is(true));
    }

    @DataProvider(name = "verifyAdminCanChangePasswordFromSettingsPage_DataProvider")
    public Object[][] verifyAdminCanChangePasswordFromSettingsPage_DataProvider() {
        return getObjectParams("verifyAdminCanChangePasswordFromSettingsPage");
    }

    @Author(names = {"Ram Pratap Singh"})
    @Module(name = "External Dashboard")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test(dataProvider = "verifyApproverCanChangePasswordFromSettingsPage_DataProvider")
    public void verifyApproverCanChangePasswordFromSettingsPage(Map<String, String> param) throws Exception {
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePage<?> homePage = new HomePage<>(session);
        LoginB2CInDWEB<?> loginB2CInDWEB = new LoginB2CInDWEB<>(session);
        CorporateExternalDashboardSidebar<?> corporateExternalDashboardSidebar = new CorporateExternalDashboardSidebar<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");

        step("Login via cookie injection and open corporate dashboard");
        homePage.loginViaCookieAndOpenCorporateDashboard(email, password);

        step("Check if user has successfully logged in");
        boolean isUserLoggedIn = loginB2CInDWEB.checkIfLoggedIn();

        step("Assert that user is logged in");
        Assertion.assertThat("Is user logged in", isUserLoggedIn, is(true));

        step("User navigates to Account Page");
        corporateExternalDashboardSidebar.goToAccountPage();

        step("The user should be able to change password from Settings page");
        corporateExternalDashboardSidebar.changePassword(password, password);

        step("The user should be able to see the password updated snackbar");
        Assertion.assertThat("Verifying the password updated snackbar", corporateExternalDashboardSidebar.isPasswordUpdatedSnackbarDisplayed(), is(true));
    }

    @DataProvider(name = "verifyApproverCanChangePasswordFromSettingsPage_DataProvider")
    public Object[][] verifyApproverCanChangePasswordFromSettingsPage_DataProvider() {
        return getObjectParams("verifyApproverCanChangePasswordFromSettingsPage");
    }

    @Author(names = {"Ram Pratap Singh"})
    @Module(name = "External Dashboard")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test(dataProvider = "verifyAdminCanBulkUploadEmployeeCsv_DataProvider")
    public void verifyAdminCanBulkUploadEmployeeCsv(Map<String, String> param) throws Exception {
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePage<?> homePage = new HomePage<>(session);
        LoginB2CInDWEB<?> loginB2CInDWEB = new LoginB2CInDWEB<>(session);
        EmployeeListPage<?> employeeListPage = new EmployeeListPage<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        String filePath = param.get("filePath");

        step("Login via cookie injection and open corporate dashboard");
        homePage.loginViaCookieAndOpenCorporateDashboard(email, password);

        step("Check if user has successfully logged in");
        boolean isUserLoggedIn = loginB2CInDWEB.checkIfLoggedIn();

        step("Assert that user is logged in");
        Assertion.assertThat("Is user logged in", isUserLoggedIn, is(true));

        step("User navigates to Employee List page");
        employeeListPage.goToEmployeeList();

        step("The user uploads a CSV file to bulk add employees");
        employeeListPage.bulkAddEmployees(filePath);

        step("The user should be able to see the success snackbar after bulk upload");
        Assertion.assertThat("Verifying the success snackbar", employeeListPage.isBulkEmployeeUploadSuccess(), is(true));
    }

    @DataProvider(name = "verifyAdminCanBulkUploadEmployeeCsv_DataProvider")
    public Object[][] verifyAdminCanBulkUploadEmployeeCsv_DataProvider() {
        return getObjectParams("verifyAdminCanBulkUploadEmployeeCsv");
    }

    @Author(names = {"Manan Kumar"})
    @Module(name = "Travel policy")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test(dataProvider = "testErrorForTravelPolicyNameOver100Chars_DataProvider")
    public void testErrorForTravelPolicyNameOver100Chars(Map<String, String> param) throws Exception {
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePage<?> homePage = new HomePage<>(session);
        CorporateExternalDashboardSidebar<?> CorporateExternalDashboardSidebar = new CorporateExternalDashboardSidebar<>(session);
        LoginB2CInDWEB<?> loginB2CInDWEB = new LoginB2CInDWEB<>(session);
        TravelPolicyPage<?> travelPolicyPage = new TravelPolicyPage<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        String policyName = param.get("policyName");

        step("Login via cookie injection and open corporate dashboard");
        homePage.loginViaCookieAndOpenCorporateDashboard(email, password);

        boolean isUserLoggedIn = loginB2CInDWEB.checkIfLoggedIn();

        step("Check that user is logged in");
        Assertion.assertThat("Is user logged in", isUserLoggedIn, is(true));

        step("User Navigates to the Travel Policy");
        CorporateExternalDashboardSidebar.goToTravelPolicy();

        step("User Navigates to the Add Policy");
        travelPolicyPage.clickAddPolicy();

        step("User Enters the Policy Name.");
        travelPolicyPage.enterPolicyName(policyName);

        step("Verify that the policy is not created when the name exceeds 100 characters.");
        Assertion.assertThat("Error Message is visible.", travelPolicyPage.isErrorMessageDisplayed(), is(true));
    }

    @DataProvider(name = "testErrorForTravelPolicyNameOver100Chars_DataProvider")
    public Object[][] testErrorForTravelPolicyNameOver100Chars_DataProvider() {
        return getObjectParams("testErrorForTravelPolicyNameOver100Chars");
    }

    @Author(names = {"Manan Kumar"})
    @Module(name = "External Dashboard")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test(dataProvider = "testDeactivatedAccountRoleRestriction_DataProvider")
    public void testDeactivatedAccountRoleRestriction(Map<String, String> param) throws Exception {
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePage<?> homePage = new HomePage<>(session);
        EmployeeListPage<?> employeeListPage = new EmployeeListPage<>(session);
        LoginB2CInDWEB<?> loginB2CInDWEB = new LoginB2CInDWEB<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        String name = param.get("name");

        step("Login via cookie injection and open corporate dashboard");
        homePage.loginViaCookieAndOpenCorporateDashboard(email, password);

        boolean isUserLoggedIn = loginB2CInDWEB.checkIfLoggedIn();

        step("Check that user is logged in");
        Assertion.assertThat("Is user logged in", isUserLoggedIn, is(true));

        step("User Navigates to Employee List Page.");
        employeeListPage.goToEmployeeListPage();

        employeeListPage.searchNameEmployee(name);

        step("Verify that set as Super Admin option is present");
        Assertion.assertThat("verify that SuperAdmin option should  be present", employeeListPage.isAdminPresent(), is(true));

        step("Deactivate the Member.");
        employeeListPage.toggleMember();

        step("User clicks on Edit Option.");
        employeeListPage.clickActionButton();

        step("Verify that set as Super Admin option is NOT present");
        Assertion.assertThat("verify that SuperAdmin option should NOT be present", employeeListPage.isAdminPresent(), is(false));

        step("Activate the member.");
        employeeListPage.toggleMember();
    }

    @DataProvider(name = "testDeactivatedAccountRoleRestriction_DataProvider")
    public Object[][] testDeactivatedAccountRoleRestriction_DataProvider() {
        return getObjectParams("testDeactivatedAccountRoleRestriction");
    }

    @Author(names = {"Manan Kumar"})
    @Module(name = "External Dashboard")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test(dataProvider = "testSuperAdminCanMakeOtherSuperAdmin_DataProvider")
    public void testSuperAdminCanMakeOtherSuperAdmin(Map<String, String> param) throws Exception {
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePage<?> homePage = new HomePage<>(session);
        LoginB2CInDWEB<?> loginB2CInDWEB = new LoginB2CInDWEB<>(session);
        EmployeeListPage<?> employeeListPage = new EmployeeListPage<>(session);

        String email = param.get("email");
        String email2 = param.get("email2");
        String password = param.get("password");
        String otp = param.get("otp");

        step("Login via cookie injection and open corporate dashboard");
        homePage.loginViaCookieAndOpenCorporateDashboard(email, password);

        boolean isUserLoggedIn = loginB2CInDWEB.checkIfLoggedIn();

        step("Check that user is logged in");
        Assertion.assertThat("Is user logged in", isUserLoggedIn, is(true));

        step("User Navigates to Employee List Page.");
        employeeListPage.goToEmployeeListPage();

        employeeListPage.searchNameEmployee("New");

        step("User clicks on Action button.");
        employeeListPage.clickAdminEditOption();

        step("User made other ADMIN as Super Admin.");
        employeeListPage.selectSuperAdmin();

        employeeListPage.searchNameEmployee("New");

        step("Verify that other user is now Super Admin.");
        Assertion.assertThat("Another member is now Super Admin.", employeeListPage.getCurrentRole(), is("Super Administrator"));


        step("Log out From Admin dashboard");
        homePage.logoutFromAdminDashboard();

        step("Undo the changes");

        homePage.loginViaCookieAndOpenCorporateDashboard(email2, password);

        step("User Navigates to Employee List Page.");
        employeeListPage.goToEmployeeListPage();

        employeeListPage.searchNameEmployee("Sadmin");

        step("User clicks on Action button.");
        employeeListPage.clickAdminEditOption();

        step("User made other ADMIN as Super Admin.");
        employeeListPage.selectSuperAdmin();

        step("Log out From Admin dashboard");
        homePage.logoutFromAdminDashboard();
    }

    @DataProvider(name = "testSuperAdminCanMakeOtherSuperAdmin_DataProvider")
    public Object[][] testSuperAdminCanMakeOtherSuperAdmin_DataProvider() {
        return getObjectParams("testSuperAdminCanMakeOtherSuperAdmin");
    }

    @Author(names = {"Manan Kumar"})
    @Module(name = "External Dashboard")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test(dataProvider = "testSuperAdminBecomesAdminAfterAssigningNewSuperAdmin_DataProvider")
    public void testSuperAdminBecomesAdminAfterAssigningNewSuperAdmin(Map<String, String> param) throws Exception {
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePage<?> homePage = new HomePage<>(session);
        LoginB2CInDWEB<?> loginB2CInDWEB = new LoginB2CInDWEB<>(session);
        EmployeeListPage<?> employeeListPage = new EmployeeListPage<>(session);

        String email = param.get("email");
        String email2 = param.get("email2");
        String password = param.get("password");
        String otp = param.get("otp");

        step("Login via cookie injection and open corporate dashboard");
        homePage.loginViaCookieAndOpenCorporateDashboard(email, password);

        boolean isUserLoggedIn = loginB2CInDWEB.checkIfLoggedIn();

        step("Check that user is logged in");
        Assertion.assertThat("Is user logged in", isUserLoggedIn, is(true));

        step("User Navigates to Employee List Page.");
        employeeListPage.goToEmployeeListPage();

        employeeListPage.searchNameEmployee("New");

        step("User clicks on Action button.");
        employeeListPage.clickAdminEditOption();

        step("User made other ADMIN as Super Admin.");
        employeeListPage.selectSuperAdmin();

        employeeListPage.searchNameEmployee("New");

        step("Verify that other user is now Super Admin.");
        Assertion.assertThat("Other user is now Super Admin.", employeeListPage.getCurrentRole(), is("Super Administrator"));

        employeeListPage.searchNameEmployee("Sadmin");

        step("Verify that previous SuperAdmin is now  Admin");
        Assertion.assertThat("Verify that previous SuperAdmin is now  Admin", employeeListPage.getCurrentRole(), is("Administrator"));

        step("Log out From Admin dashboard");
        homePage.logoutFromAdminDashboard();


        step("Undo the changes");

        homePage.loginViaCookieAndOpenCorporateDashboard(email2, password);

        step("User Navigates to Employee List Page.");
        employeeListPage.goToEmployeeListPage();

        employeeListPage.searchNameEmployee("Sadmin");

        step("User clicks on Action button.");
        employeeListPage.clickAdminEditOption();

        step("User made other ADMIN as Super Admin.");
        employeeListPage.selectSuperAdmin();

        employeeListPage.searchNameEmployee("Sadmin");

        step("Verify that other user is now Super Admin.");
        Assertion.assertThat("Other user is now Super Admin.", employeeListPage.getCurrentRole(), is("Super Administrator"));

        step("Log out From Admin dashboard");
        homePage.logoutFromAdminDashboard();

    }

    @DataProvider(name = "testSuperAdminBecomesAdminAfterAssigningNewSuperAdmin_DataProvider")
    public Object[][] testSuperAdminBecomesAdminAfterAssigningNewSuperAdmin_DataProvider() {
        return getObjectParams("testSuperAdminBecomesAdminAfterAssigningNewSuperAdmin");
    }

    @Author(names = {"Nishtha"})
    @Module(name = "Add Employee")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test(dataProvider = "testCannotAddEmployeeWithoutEmployeeName_DataProvider")
    public void testCannotAddEmployeeWithoutEmployeeName(Map<String, String> param) throws Exception {
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePage<?> homePage = new HomePage<>(session);
        LoginB2CInDWEB<?> loginB2CInDWEB = new LoginB2CInDWEB<>(session);
        EmployeeListPage<?> employeeListPage = new EmployeeListPage<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        String name = CoreFunctions.randomName();

        step("Login via cookie injection and open corporate dashboard");
        homePage.loginViaCookieAndOpenCorporateDashboard(email, password);

        step("Check if user has successfully logged in");
        boolean isUserLoggedIn = loginB2CInDWEB.checkIfLoggedIn();

        step("Close tutorial banner if exists");
        loginB2CInDWEB.closeTutorialbanner();

        step("Assert that user is logged in");
        Assertion.assertThat("Is user logged in", isUserLoggedIn, is(true));

        step("Open the Employee List");
        employeeListPage.goToEmployeeListPage();

        step("Click Add Employee");
        employeeListPage.clickAddEmployee();

        step("Fill the new Employee Details");
        employeeListPage.fillEmployeeDetailsWithoutName("Traveler", name);

        step("Click Submit");
        employeeListPage.clickSubmit();

        step("Check if the name error exists");
        boolean isNameErrorExists = employeeListPage.checkIfErrorAppears();

        step("Assert that the newly added employee cannot be found");
        Assertion.assertThat("Verify newly employee cannot be found : name error appears", isNameErrorExists, is(true));

        step("Log out From Admin dashboard");
        homePage.logoutFromAdminDashboard();
    }

    @DataProvider(name = "testCannotAddEmployeeWithoutEmployeeName_DataProvider")
    public Object[][] testCannotAddEmployeeWithoutEmployeeName_DataProvider() {
        return getObjectParams("testCannotAddEmployeeWithoutEmployeeName");
    }

    @Author(names = {"Nishtha"})
    @Module(name = "Booking List")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test(dataProvider = "testAdminBookingListCanShareReceipt_DataProvider")
    public void testAdminBookingListCanShareReceipt(Map<String, String> param) throws InterruptedException, FailedLoginException {
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        BookingListPage<?> bookingListPage = new BookingListPage<>(session);
        LoginB2CInDWEB<?> loginB2CInDWEB = new LoginB2CInDWEB<>(session);
        HomePage<?> homePage = new HomePage<>(session);

        String orderID = param.get("orderId");
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");

        step("Login via cookie injection and open corporate dashboard");
        homePage.loginViaCookieAndOpenCorporateDashboard(email, password);

        step("Close tutorial banner if exists");
        loginB2CInDWEB.closeTutorialbanner();

        step("Go to Booking List");
        bookingListPage.goToBookingListPage();

        step("Close Onboarding Dialog");
        bookingListPage.closeOnBoardingDialog();

        step("Send keys to 'Search By Order ID' Text field");
        bookingListPage.setSearchTextfield(orderID);

        step("Hit 'Search' Button");
        bookingListPage.hitSearchButton();
        bookingListPage.checkIfSearchedOrderExists(orderID);

        step("Click on more actions button");
        bookingListPage.clickOnMoreActionsBtn();

        step("Click on Share Receipt option");
        bookingListPage.clickOnShareReceiptOption();

        step("Click on Share via Email button");
        bookingListPage.clickOnShareViaEmailBtn();

        step("Is share popup closed");
        boolean isSharePopupClosed = bookingListPage.isShareReceiptPopupDisplayed();

        step("Check if receipt is shared via email and popup is closed");
        Assertion.assertThat("Verify receipt is shared via email and popup is closed", isSharePopupClosed, is(false));

        step("Log out From Admin dashboard");
        homePage.logoutFromAdminDashboard();
    }

    @Author(names = {"Manan Kumar"})
    @Module(name = "External Dashboard")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test(dataProvider = "testAdminCanDisableTravelPlanSetting_DataProvider")
    public void testAdminCanDisableTravelPlanSetting(Map<String, String> param) throws Exception {
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePage<?> homePage = new HomePage<>(session);
        LoginB2CInDWEB<?> loginB2CInDWEB = new LoginB2CInDWEB<>(session);
        CorporateExternalDashboardSidebar<?> corporateExternalDashboardSidebar = new CorporateExternalDashboardSidebar<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");

        step("Login via cookie injection and open corporate dashboard");
        homePage.loginViaCookieAndOpenCorporateDashboard(email, password);

        boolean isUserLoggedIn = loginB2CInDWEB.checkIfLoggedIn();

        step("Check that user is logged in");
        Assertion.assertThat("Is user logged in", isUserLoggedIn, is(true));

        step("User navigates to Company Settings Page");
        corporateExternalDashboardSidebar.goToCompanySettings();

        step("User made Edit in Travel Plan");
        corporateExternalDashboardSidebar.editTravelPlan();

        step("User select changes in TRAVEL Plan.");
        corporateExternalDashboardSidebar.clickTravelPlanRadioToOFF();

        step("Verify that travel plan config will be disabled");
        Assertion.assertThat("travel plan config should be disabled.", corporateExternalDashboardSidebar.isTravelPlanSuccessSnackbarDisplayed(), is(true));
    }

    @DataProvider(name = "testAdminCanDisableTravelPlanSetting_DataProvider")
    public Object[][] testAdminCanDisableTravelPlanSetting_DataProvider() {
        return getObjectParams("testAdminCanDisableTravelPlanSetting");
    }

    @Author(names = {"Manan Kumar"})
    @Module(name = "External Dashboard")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test(dataProvider = "testAdminCanEnableTravelPlanSetting_DataProvider")
    public void testAdminCanEnableTravelPlanSetting(Map<String, String> param) throws Exception {
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePage<?> homePage = new HomePage<>(session);
        LoginB2CInDWEB<?> loginB2CInDWEB = new LoginB2CInDWEB<>(session);
        CorporateExternalDashboardSidebar<?> corporateExternalDashboardSidebar = new CorporateExternalDashboardSidebar<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");

        step("Login via cookie injection and open corporate dashboard");
        homePage.loginViaCookieAndOpenCorporateDashboard(email, password);

        boolean isUserLoggedIn = loginB2CInDWEB.checkIfLoggedIn();

        step("Check that user is logged in");
        Assertion.assertThat("Is user logged in", isUserLoggedIn, is(true));

        step("User navigates to Company Settings Page");
        corporateExternalDashboardSidebar.goToCompanySettings();

        step("User made Edit in Travel Plan");
        corporateExternalDashboardSidebar.editTravelPlan();

        step("User select changes in TRAVEL Plan.");
        corporateExternalDashboardSidebar.clickTravelPlanRadioToON();

        step("Verify that travel plan config will be enabled");
        Assertion.assertThat("travel plan config should be enabled.", corporateExternalDashboardSidebar.isTravelPlanSuccessSnackbarDisplayed(), is(true));
    }

    @DataProvider(name = "testAdminCanEnableTravelPlanSetting_DataProvider")
    public Object[][] testAdminCanEnableTravelPlanSetting_DataProvider() {
        return getObjectParams("testAdminCanEnableTravelPlanSetting");
    }

    @DataProvider(name = "testAdminBookingListCanShareReceipt_DataProvider")
    public Object[][] testAdminBookingListCanShareReceipt_DataProvider() {
        return getObjectParams("testAdminBookingListCanShareReceipt");
    }

    @Author(names = {"Nishtha"})
    @Module(name = "Employee List")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test(dataProvider = "testEmployeeListResendLinkButton_DataProvider")
    public void testEmployeeListResendLinkButton(Map<String, String> param) throws Exception {
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePage<?> homePage = new HomePage<>(session);
        LoginB2CInDWEB<?> loginB2CInDWEB = new LoginB2CInDWEB<>(session);
        EmployeeListPage<?> employeeListPage = new EmployeeListPage<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        String name = param.get("name");

        step("Login via cookie injection and open corporate dashboard");
        homePage.loginViaCookieAndOpenCorporateDashboard(email, password);

        boolean isUserLoggedIn = loginB2CInDWEB.checkIfLoggedIn();

        step("Check that user is logged in");
        Assertion.assertThat("Is user logged in", isUserLoggedIn, is(true));

        step("Open the Employee List");
        employeeListPage.goToEmployeeListPage();

        step("Search newly Added Employee");
        employeeListPage.searchNewEmployee(name);

        step("Check if the searched Name exists");
        employeeListPage.checkIfExists(name);

        step("Click on Action Button");
        employeeListPage.clickActionOption();

        step("Click on Resend Registration Link Option on Action Iten Dropdown");
        employeeListPage.clickResendRegistrationLinkOption();

        step("Assert that the registration link is successfully sent again");
        Assertion.assertThat("Is registration link sent", employeeListPage.checkResendLinkSnackbar(), is(true));

        step("Log out From Admin dashboard");
        homePage.logoutFromAdminDashboard();
    }

    @DataProvider(name = "testEmployeeListResendLinkButton_DataProvider")
    public Object[][] testEmployeeListResendLinkButton_DataProvider() {
        return getObjectParams("testEmployeeListResendLinkButton");
    }

    @Author(names = {"Nishtha"})
    @Module(name = "Employee List")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test(dataProvider = "testRegistrationFilterOnEmployeeList_DataProvider")
    public void testRegistrationFilterOnEmployeeList(Map<String, String> param) throws Exception {
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePage<?> homePage = new HomePage<>(session);
        LoginB2CInDWEB<?> loginB2CInDWEB = new LoginB2CInDWEB<>(session);
        EmployeeListPage<?> employeeListPage = new EmployeeListPage<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");

        step("Login via cookie injection and open corporate dashboard");
        homePage.loginViaCookieAndOpenCorporateDashboard(email, password);

        boolean isUserLoggedIn = loginB2CInDWEB.checkIfLoggedIn();

        step("Check that user is logged in");
        Assertion.assertThat("Is user logged in", isUserLoggedIn, is(true));

        step("Open the Employee List");
        employeeListPage.goToEmployeeListPage();

        step("Check if Registration Status Filter is displayed");
        boolean isElementDisplayed = employeeListPage.isRegistrationStatusFilterPresent();

        step("Assert that the registration status filter displayed on employee list page");
        Assertion.assertThat("Is registration status filter displayed", isElementDisplayed, is(true));

        step("Log out From Admin dashboard");
        homePage.logoutFromAdminDashboard();
    }

    @DataProvider(name = "testRegistrationFilterOnEmployeeList_DataProvider")
    public Object[][] testRegistrationFilterOnEmployeeList_DataProvider() {
        return getObjectParams("testRegistrationFilterOnEmployeeList");
    }

    @Author(names = {"Nishtha"})
    @Module(name = "Booking List")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test(dataProvider = "testAdminCanSeeReceipt_DataProvider")
    public void testAdminCanSeeReceipt(Map<String, String> param) throws InterruptedException, FailedLoginException {
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        BookingListPage<?> bookingListPage = new BookingListPage<>(session);
        LoginB2CInDWEB<?> loginB2CInDWEB = new LoginB2CInDWEB<>(session);
        HomePage<?> homePage = new HomePage<>(session);

        String orderID = param.get("orderId");
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");

        step("Login via cookie injection and open corporate dashboard");
        homePage.loginViaCookieAndOpenCorporateDashboard(email, password);

        step("Close tutorial banner if exists");
        loginB2CInDWEB.closeTutorialbanner();

        step("Go to Booking List");
        bookingListPage.goToBookingListPage();

        step("Close Onboarding Dialog");
        bookingListPage.closeOnBoardingDialog();

        step("Send keys to 'Search By Order ID' Text field");
        bookingListPage.setSearchTextfield(orderID);

        step("Hit 'Search' Button");
        bookingListPage.hitSearchButton();
        bookingListPage.checkIfSearchedOrderExists(orderID);

        step("Click on more actions button");
        bookingListPage.clickOnMoreActionsBtn();

        step("Check if receipt is displayed in new tab");
        boolean isReceiptDisplayed = bookingListPage.checkReceiptOpensInNewTab(orderID);

        step("Check if receipt displayed in new tab");
        Assertion.assertThat("Verify receipt is displayed in new tab", isReceiptDisplayed, is(true));

        step("Log out From Admin dashboard");
        homePage.logoutFromAdminDashboard();
    }

    @DataProvider(name = "testAdminCanSeeReceipt_DataProvider")
    public Object[][] testAdminCanSeeReceipt_DataProvider() {
        return getObjectParams("testAdminCanSeeReceipt");
    }

    @Author(names = {"Manan Kumar"})
    @Module(name = "External Dashboard")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test(dataProvider = "testSuperAdminCanCreateMultipleAdminsSuccessfully_DataProvider")
    public void testSuperAdminCanCreateMultipleAdminsSuccessfully(Map<String, String> param) throws Exception {
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePage<?> homePage = new HomePage<>(session);
        EmployeeListPage<?> employeeListPage = new EmployeeListPage<>(session);
        LoginB2CInDWEB<?> loginB2CInDWEB = new LoginB2CInDWEB<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        String role = param.get("role");

        step("Login via cookie injection and open corporate dashboard");
        homePage.loginViaCookieAndOpenCorporateDashboard(email, password);

        boolean isUserLoggedIn = loginB2CInDWEB.checkIfLoggedIn();

        step("Check that user is logged in");
        Assertion.assertThat("Is user logged in", isUserLoggedIn, is(true));

        step("User Navigates to Employee List Page.");
        employeeListPage.goToEmployeeListPage();

        employeeListPage.searchNameEmployee("New");

        step("User clicks on Action button.");
        employeeListPage.clickActionButton();

        step("User clicks on Edit Option.");
        employeeListPage.clickEdit();

        step("User Clicks on User Role Dropdown.");
        employeeListPage.selectUserRole();

        step("User Selects Admin Role.");
        employeeListPage.selectAdminRole();

        step("User clicks on Save button.");
        employeeListPage.clickSave();

        step("Verify that the Member will be Admin Now.");
        Assertion.assertThat("User Role Should be Admin", employeeListPage.isUserAdmin(), is(true));
    }

    @DataProvider(name = "testSuperAdminCanCreateMultipleAdminsSuccessfully_DataProvider")
    public Object[][] testSuperAdminCanCreateMultipleAdminsSuccessfully_DataProvider() {
        return getObjectParams("testSuperAdminCanCreateMultipleAdminsSuccessfully");
    }

    @Author(names = {"Ram Pratap Singh"})
    @Module(name = "External Dashboard")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test(dataProvider = "testverifyOrderAppearsInBookingListAfterInstantPaymentSuccess_DataProvider")
    public void testverifyOrderAppearsInBookingListAfterInstantPaymentSuccess(Map<String, String> param) throws Exception {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);

        String travelerUsername = param.get("username");
        String travelerPassword = param.get("password");
        String adminUsername = param.get("approverUsername");
        String adminPassword = param.get("approverPassword");
        String location = param.get("location");
        String dateType = param.get("dateType");
        String date = param.get("dateRange");
        String paymentGroup = param.get("paymentGroup");
        String paymentMethod = param.get("paymentMethod");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HotelLandingPageWeb<?> hotelLandingPage = HotelLandingPageWeb.getInstance(session);
        HotelRoomListPage<?> hotelRoomListPage = new HotelRoomListPage<>(session);
        HotelBookingFormPageWeb<?> hotelBookingFormPage = HotelBookingFormPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        TravelPlanWeb<?> travelPlanPage = TravelPlanWeb.getInstance(session);
        HomePage<?> corporateHomePage = new HomePage<>(session);
        BookingListPage<?> bookingListPage = new BookingListPage<>(session);

        step("Go to home");
        homePage.goToHomePage();

        step("Login as instant-payment traveler { " + travelerUsername + " } via Cookie");
        loginPage.loginWithSpecificAccountViaCookie(travelerUsername, travelerPassword);

        step("User select IDR currency");
        homePage.selectCurrency("IDR");

        step("User open hotel vertical");
        homePage.openVerticalPageViaURL("Hotel");

        step("User search hotel location");
        hotelLandingPage.searchStayLocation(location);

        step("Set date");
        hotelLandingPage.setStayDate(dateType, date);

        step("Set guests and rooms");
        hotelLandingPage.clickRoomAndGuestButton();
        hotelLandingPage.setTotalRoom(1);
        hotelLandingPage.setTotalGuest(1);
        hotelLandingPage.clickDone();

        step("Submit search");
        hotelLandingPage.clickSubmit();

        step("Click book button");
        hotelRoomListPage.scrollToRoomListSection();
        hotelRoomListPage.clickBookButton();

        step("Select 'Pay Now' and continue to payment page");
        hotelBookingFormPage.continueTo2NdPartThenPaymentPage();
        travelPlanPage.skipTravelPlan();

        step("Payment page should be displayed");
        String orderID = paymentPage.getOrderId();
        orderID = String.valueOf(getOrderId(orderID));
        Assertion.assertThat("Corporate billing option is not present for instant-payment account", paymentPage.getSelectedPayment().toLowerCase(), not("corporate billing"));

        step("User select { " + paymentMethod + " } payment method");
        paymentPage.clickOnSelectPaymentMethod();
        paymentPage.goToPaymentMethodsGroup(paymentGroup);
        paymentPage.selectPaymentMethod(paymentMethod);

        step("Click on Pay button");
        paymentPage.clickOnPaymentButton();

        step("Verify the payment successful page is visible (Sakuku instant payment auto-approves)");
        Assertion.assertThat("Verify the payment successful page is visible", paymentPage.isPaymentSuccessfulVisible(), is(true));

        step("Login to the corporate external dashboard using admin creds { " + adminUsername + " }");
        corporateHomePage.loginViaCookieAndOpenCorporateDashboard(adminUsername, adminPassword);

        step("Go to the Booking List");
        bookingListPage.goToBookingListPage();

        step("Send keys to 'Search By Order ID' text field");
        bookingListPage.setSearchTextfield(orderID);

        step("Hit 'Search' button");
        bookingListPage.hitSearchButton();

        step("Check if orderID { " + orderID + " } exists in the Booking List");
        Assertion.assertThat("check if orderID exists in Booking List", bookingListPage.checkIfSearchedOrderExists(orderID), is(true));
    }

    @DataProvider(name = "testverifyOrderAppearsInBookingListAfterInstantPaymentSuccess_DataProvider")
    public Object[][] testverifyOrderAppearsInBookingListAfterInstantPaymentSuccess_DataProvider() {
        return getObjectParams("testverifyOrderAppearsInBookingListAfterInstantPaymentSuccess");
    }

    @Author(names = {"Ram Pratap Singh"})
    @Module(name = "External Dashboard")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test(dataProvider = "verifyAdminCanOpenRefundListDataProvider")
    public void verifyAdminCanOpenRefundList(Map<String, String> param) throws Exception {
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePage<?> homePage = new HomePage<>(session);
        LoginB2CInDWEB<?> loginB2CInDWEB = new LoginB2CInDWEB<>(session);
        CorporateExternalDashboardSidebar<?> CorporateExternalDashboardSidebar = new CorporateExternalDashboardSidebar<>(session);
        RefundListPage<?> refundListPage = new RefundListPage<>(session);

        String email = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");

        step("Login via cookie injection and open corporate dashboard");
        homePage.loginViaCookieAndOpenCorporateDashboard(email, password);

        step("Check if user has successfully logged in");
        boolean isUserLoggedIn = loginB2CInDWEB.checkIfLoggedIn();

        step("Assert that user is logged in");
        Assertion.assertThat("Is user logged in", isUserLoggedIn, is(true));

        step("Open the Refund List");
        CorporateExternalDashboardSidebar.goToRefundList();

        step("Verify that the Refund List page is opened");
        boolean isRefundListPageOpened = refundListPage.isRefundListPageDisplayed();
        Assertion.assertThat("Verify that the Refund List page is opened", isRefundListPageOpened, is(true));
    }

    @DataProvider(name = "verifyAdminCanOpenRefundListDataProvider")
    public Object[][] verifyAdminCanOpenRefundListDataProvider() {
        return getObjectParams("verifyAdminCanOpenRefundList");
    }

    @Author(names = {"Ram Pratap Singh"})
    @Module(name = "External Dashboard")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test
    public void verifyPurchaseListDisplaysOrdersWithWaitingForPaymentStatus() throws Exception {
        Map<String, String> param = getParam("verifyPurchaseListDisplaysOrdersWithWaitingForPaymentStatus");

        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);

        String travelerUsername = param.get("username");
        String travelerPassword = param.get("password");
        String otp = param.get("otp");
        String location = param.get("location");
        String dateType = param.get("dateType");
        String date = param.get("dateRange");
        String paymentGroup = param.get("paymentGroup");
        String paymentMethod = param.get("paymentMethod");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HotelLandingPageWeb<?> hotelLandingPage = HotelLandingPageWeb.getInstance(session);
        HotelRoomListPage<?> hotelRoomListPage = new HotelRoomListPage<>(session);
        HotelBookingFormPageWeb<?> hotelBookingFormPage = HotelBookingFormPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        TravelPlanWeb<?> travelPlanPage = TravelPlanWeb.getInstance(session);
        CorporateDashboard<?> corporateDashboard = new CorporateDashboard<>(session);
        CorporateExternalDashboardSidebar<?> corporateExternalDashboardSidebar = new CorporateExternalDashboardSidebar<>(session);
        PurchaseListPage<?> purchaseListPage = new PurchaseListPage<>(session);

        step("Go to home");
        homePage.goToHomePage();

        step("Go to account page");
        loginPage.goToAccountPage();

        step("User login");
        loginPage.loginIfNotWithSameEmail(travelerUsername, travelerPassword, otp, 1);

        if (Configs.PLATFORM.equals(Platform.DWEB)) {
            corporateDashboard.goBackToTiketHomePage();
            homePage.waitForHomePageToOpen();
        }

        step("User select IDR currency");
        homePage.selectCurrency("IDR");

        step("User open hotel vertical");
        homePage.openVerticalPageViaURL("Hotel");

        step("User search hotel location");
        hotelLandingPage.searchStayLocation(location);

        step("Set date");
        hotelLandingPage.setStayDate(dateType, date);

        step("Set guests and rooms");
        hotelLandingPage.clickRoomAndGuestButton();
        hotelLandingPage.setTotalRoom(1);
        hotelLandingPage.setTotalGuest(1);
        hotelLandingPage.clickDone();

        step("Submit search");
        hotelLandingPage.clickSubmit();

        step("Click book button");
        hotelRoomListPage.scrollToRoomListSection();
        hotelRoomListPage.clickBookButton();
        hotelRoomListPage.enterMobile("1111111111");

        step("Continue to payment page");
        hotelBookingFormPage.continueToPaymentPage();
        travelPlanPage.skipTravelPlan();

        step("Payment page should be displayed");
        String orderID = paymentPage.getOrderId();
        int finalAmount = Integer.parseInt(paymentPage.getFinalPrice());
        Assertion.assertThat("Final amount should be greater than zero", finalAmount, is(greaterThan(0)));
        orderID = String.valueOf(getOrderId(orderID));

        step("User select payment method ");
        paymentPage.clickOnSelectPaymentMethod();
        paymentPage.goToPaymentMethodsGroup(paymentGroup);
        paymentPage.selectPaymentMethod(paymentMethod);

        step("Click on Pay button");
        paymentPage.clickOnPaymentButton();

        step("Verify confirmation page");
        Assertion.assertThat("Verify the confimation page is loaded", paymentPage.isPaymentConfirmationPageLoaded(), is(true));

        step("user navigates to Corporate Dashboard");
        corporateExternalDashboardSidebar.goToCorporateDashboardPage();

        step("Go to the Purchase List");
        purchaseListPage.goToPurchaseListPage();

        step("Find Hotel Order ID in Purchase List");
        purchaseListPage.searchByOrderID(orderID);

        step("Check if Hotel Purchase List displays the order with Waiting for Payment status");
        boolean purchaseExists = purchaseListPage.checkWaitingForPayment(orderID);
        Assertion.assertThat("Verify if Hotel Purchase List displays the order with Waiting for Payment status", purchaseExists, is(true));
    }

    @Author(names = {"Ram Pratap Singh"})
    @Module(name = "External Dashboard")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test(dataProvider = "verifyBookingDetailsConsistencyBetweenMyOrderAndDashboard_DataProvider")
    public void verifyBookingDetailsConsistencyBetweenMyOrderAndDashboard(Map<String, String> param) throws Exception {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPageWeb = FlightLandingPageWeb.getInstance(session);
        FlightSearchResultPageWeb<?> flightSearchResultPage = FlightSearchResultPageWeb.getInstance(session);
        FlightProductReviewPage<?> flightProductReviewPage = new FlightProductReviewPage<>(session);
        FlightBookingFormPageWeb<?> flightBookingFormPage = FlightBookingFormPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        TravelPlanWeb<?> travelPlanPage = TravelPlanWeb.getInstance(session);
        FlightApi<?> flightApi = new FlightApi<>(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);
        CorporateExternalDashboardSidebar<?> corporateExternalDashboardSidebar = new CorporateExternalDashboardSidebar<>(session);
        BookingListPage<?> bookingListPage = new BookingListPage<>(session);
        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");
        String airline = param.get("airline");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String departureDate = getRandomLongDateOnRange(daysToDepartureDate, 30);

        int adultPassengers = Integer.parseInt(param.get("adultPassengers"));
        int childPassengers = Integer.parseInt(param.get("childPassengers"));
        int infantPassengers = Integer.parseInt(param.get("infantPassengers"));

        String username = param.get("username");
        String password = param.get("password");
        String approverUsername = param.get("approverUsername");
        String approverPassword = param.get("approverPassword");
        String currency = param.get("currency");
        String name = param.get("name");
        String lastName = param.get("lastName");

        step("User open home page");
        homePage.goToHomePage();

        step("Go to account page");
        loginPage.goToAccountPage();

        step("login in using { " + username + " }");
        loginPage.loginIfNotWithSameEmail(username, password, "123456", 1);

        step("User select { " + currency + " } currency");
        homePage.selectCurrency(currency);

        step("User open Flight vertical");
        homePage.openVerticalPageViaURL("Flights");

        step("Toggle trip type");
        flightLandingPageWeb.toggleTripType(tripType);

        step("Select origin airport");
        flightLandingPageWeb.selectOrigin(originAirport);

        step("Select destination airport");
        flightLandingPageWeb.selectDestination(destinationAirport);

        step("Select date");
        flightLandingPageWeb.selectDate(departureDate);

        step("add passenger");
        flightLandingPageWeb.addPassenger(adultPassengers, childPassengers, infantPassengers);

        step("search flight");
        flightLandingPageWeb.searchFlight();

        step("Apply Filter " + airline);
        flightSearchResultPage.filterAirline(airline);

        step("select flight");
        flightSearchResultPage.selectFlightByAirline(airline);

        step("Continue to booking form");
        flightProductReviewPage.continueToBookingForm();
        flightBookingFormPage.clickNoThanksOnFlightMayReschedulePopup();

        step("Click toggle to fill passengers details and save ");
        flightBookingFormPage.fillPassengerDetailsLogin(name, lastName);

        step("Go to payment page");
        flightBookingFormPage.goToPaymentPageFromBookingFormPage();

        step("Skip Flight may rescheduled page by clicking no thanks");
        travelPlanPage.skipTravelPlan();
        flightBookingFormPage.clickNoThanksOnFlightMayReschedulePopup();

        step("Payment page should be displayed");
        String orderID = paymentPage.getOrderId();
        orderID = String.valueOf(getOrderId(orderID));
        String finalAmount = paymentPage.getFinalPrice();
        Assertion.assertThat("Corporate billing option is selected", paymentPage.getSelectedPayment().toLowerCase(), is("corporate billing"));

        step("get total price payment page");
        paymentPage.totalPricePaymentPage();

        step("Submit payment using Corporate Billing");
        paymentPage.clickOnPaymentButton();

        step("approve payment of order id: { " + orderID + " } " + " using admin account with username : { " + approverUsername + " }");
        corporateApi.approveBookingB2BApi(approverUsername, approverPassword, orderID);

        step("get booking code from API");
        flightApi.getBookingCode(orderID, username);

        step("Open the order list");
        paymentPage.clickSeeOrderListButton();

        step("go to Corporate dashboard page");
        corporateExternalDashboardSidebar.goToCorporateDashboardPage();

        step("go to booking list page");
        bookingListPage.goToBookingListPage();

        step("Send keys to 'Search By Order ID' Text field");
        bookingListPage.setSearchTextfield(orderID);

        step("Hit 'Search' Button");
        bookingListPage.hitSearchButton();

        step("User retrieves the flight details from booking list and verifies the details");
        Map<String, String> flightDetails = bookingListPage.getFlightDetails();

        Assertion.assertThat("verify the name", flightDetails.get("Name").contains(name), is(true));
        Assertion.assertThat("verify Price", flightDetails.get("Price").replaceAll("[^0-9]", "").contains(finalAmount), is(true));
    }

    @DataProvider(name = "verifyBookingDetailsConsistencyBetweenMyOrderAndDashboard_DataProvider")
    public Object[][] verifyBookingDetailsConsistencyBetweenMyOrderAndDashboard_DataProvider() {
        return getObjectParams("verifyBookingDetailsConsistencyBetweenMyOrderAndDashboard");
    }

    @Author(names = {"Ram Pratap Singh"})
    @Module(name = "External Dashboard")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test(dataProvider = "verifyRefundEstimationOpensRefundDetails_DataProvider")
    public void verifyRefundEstimationOpensRefundDetails(Map<String, String> param) throws Exception {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPageWeb = FlightLandingPageWeb.getInstance(session);
        FlightSearchResultPageWeb<?> flightSearchResultPage = FlightSearchResultPageWeb.getInstance(session);
        FlightProductReviewPage<?> flightProductReviewPage = new FlightProductReviewPage<>(session);
        FlightBookingFormPageWeb<?> flightBookingFormPage = FlightBookingFormPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        TravelPlanWeb<?> travelPlanPage = TravelPlanWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        RefundPageWeb<?> refundPage = RefundPageWeb.getInstance(session);
        FlightApi<?> flightApi = new FlightApi<>(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);
        CorporateExternalDashboardSidebar<?> corporateExternalDashboardSidebar = new CorporateExternalDashboardSidebar<>(session);
        RefundListPage<?> refundListPage = new RefundListPage<>(session);

        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");
        String airline = param.get("airline");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String departureDate = getRandomLongDateOnRange(daysToDepartureDate, 30);

        int adultPassengers = Integer.parseInt(param.get("adultPassengers"));
        int childPassengers = Integer.parseInt(param.get("childPassengers"));
        int infantPassengers = Integer.parseInt(param.get("infantPassengers"));

        String username = param.get("username");
        String password = param.get("password");
        String approverUsername = param.get("approverUsername");
        String approverPassword = param.get("approverPassword");
        String currency = param.get("currency");
        String name = param.get("name");
        String lastName = param.get("lastName");

        step("User open home page");
        homePage.goToHomePage();

        step("Go to account page");
        loginPage.goToAccountPage();

        step("login in using { " + username + " }");
        loginPage.loginIfNotWithSameEmail(username, password, "123456", 1);

        step("User select { " + currency + " } currency");
        homePage.selectCurrency(currency);

        step("User open Flight vertical");
        homePage.openVerticalPageViaURL("Flights");

        step("Toggle trip type");
        flightLandingPageWeb.toggleTripType(tripType);

        step("Select origin airport");
        flightLandingPageWeb.selectOrigin(originAirport);

        step("Select destination airport");
        flightLandingPageWeb.selectDestination(destinationAirport);

        step("Select date");
        flightLandingPageWeb.selectDate(departureDate);

        step("add passenger");
        flightLandingPageWeb.addPassenger(adultPassengers, childPassengers, infantPassengers);

        step("search flight");
        flightLandingPageWeb.searchFlight();

        step("Apply Filter " + airline);
        flightSearchResultPage.filterAirline(airline);

        step("select flight");
        flightSearchResultPage.selectFlightByAirline(airline);

        step("Continue to booking form");
        flightProductReviewPage.continueToBookingForm();
        flightBookingFormPage.clickNoThanksOnFlightMayReschedulePopup();

        step("Click toggle to fill passengers details and save ");
        flightBookingFormPage.fillPassengerDetailsLogin(name, lastName);

        step("Go to payment page");
        flightBookingFormPage.goToPaymentPageFromBookingFormPage();

        step("Skip Flight may rescheduled page by clicking no thanks");
        travelPlanPage.skipTravelPlan();
        flightBookingFormPage.clickNoThanksOnFlightMayReschedulePopup();

        step("Payment page should be displayed");
        String orderID = paymentPage.getOrderId();
        orderID = String.valueOf(getOrderId(orderID));
        Assertion.assertThat("Corporate billing option is selected", paymentPage.getSelectedPayment().toLowerCase(), is("corporate billing"));

        step("get total price payment page");
        paymentPage.totalPricePaymentPage();

        step("Submit payment using Corporate Billing");
        paymentPage.clickOnPaymentButton();

        step("approve payment of order id: { " + orderID + " } " + " using admin account with username : { " + approverUsername + " }");
        corporateApi.approveBookingB2BApi(approverUsername, approverPassword, orderID);

        step("get booking code from API");
        String bookingCode = flightApi.getBookingCode(orderID, username);

        step("Open the order list");
        paymentPage.clickSeeOrderListButton();

        step("set Tiket Number on flight dashboard and ticket will be eticket issued");
        flightApi.flightEticketIssuedRequest(orderID, bookingCode, username, name + " " + lastName, 20);

        step("go to order detail page");
        orderListPage.openIssuedTicketViaURL(orderID, 15);
        orderDetailPage.closeEticketPopUp();
        orderDetailPage.getVerticalTotalPrice();

        step("Click refund button");
        orderDetailPage.clickRefundButton();
        refundPage.skipRescheduleNudgeIfShown();

        step("Fill refund form");
        refundPage.selectRefundReasonPersonalReasonFlight();
        refundPage.chooseSpecificReason();

        step("verify refund estimation");
        double totalRefundPrice = refundPage.getTotalRefundPrice(currency);
        refundPage.continueRefund();

        step("verify total refund price");
        double totalRefundPricePage2 = refundPage.getTotalRefundPrice(currency);
        Assertion.assertThat("total refund price are same on both page", totalRefundPrice, is(totalRefundPricePage2));

        step("submit refund");
        refundPage.clickChecklistTnc();
        refundPage.clickSubmitRefund();
        refundPage.clickProcessRefund();
        refundPage.clickGoToOrders();

        step("Go to Corporate dashboard page");
        corporateExternalDashboardSidebar.goToCorporateDashboardPage();

        step("Go to Refund List page");
        refundListPage.goToRefundListPage();

        step("Search for the order ID in Refund List");
        refundListPage.searchByOrderID(orderID);

        step("Verify that the order ID exists in Refund List");
        boolean orderExists = refundListPage.checkIfSearchedOrderExists(orderID);
        Assertion.assertThat("Verify that the order ID exists in Refund List", orderExists, is(true));
    }

    @DataProvider(name = "verifyRefundEstimationOpensRefundDetails_DataProvider")
    public Object[][] verifyRefundEstimationOpensRefundDetails_DataProvider() {
        return getObjectParams("verifyRefundEstimationOpensRefundDetails");
    }

    @Author(names = {"Manan Kumar"})
    @Module(name = "External Dashboard")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test(dataProvider = "testAccommodationCheckInNotBeforeTravelPlanStartDate_DataProvider")
    public void testAccommodationCheckInNotBeforeTravelPlanStartDate(Map<String, String> param) throws Exception {
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePage<?> homePage = new HomePage<>(session);
        LoginB2CInDWEB<?> loginB2CInDWEB = new LoginB2CInDWEB<>(session);
        TravelPlan<?> travelPlan = new TravelPlan<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        String plan = param.get("plan");
        String product = param.get("product");
        int daysDifference = Integer.parseInt(param.get("daysDifference"));

        step("Login via cookie injection and open corporate dashboard");
        homePage.loginViaCookieAndOpenCorporateDashboard(email, password);

        boolean isUserLoggedIn = loginB2CInDWEB.checkIfLoggedIn();

        step("Check that user is logged in");
        Assertion.assertThat("Is user logged in", isUserLoggedIn, is(true));

        step("User Navigates to Travel Plan Page.");
        travelPlan.selectTravelPlan();

        step("User selects the travel PLAN.");
        travelPlan.setTravelPlan(plan);

        step("User Selects Start Date.");
        travelPlan.setStartDate();

        step("User Selects End Date.");
        travelPlan.setEndDate(daysDifference);

        step("User add Travel Product");
        travelPlan.addTravelProduct(product);

        step("Verify that check In cannot be select before Start Date.");
        Assertion.assertThat("Date should not be clickable", travelPlan.isPastStartDateSelectable(), is(false));
    }

    @DataProvider(name = "testAccommodationCheckInNotBeforeTravelPlanStartDate_DataProvider")
    public Object[][] testAccommodationCheckInNotBeforeTravelPlanStartDate_DataProvider() {
        return getObjectParams("testAccommodationCheckInNotBeforeTravelPlanStartDate");
    }

    @Author(names = {"Manan Kumar"})
    @Module(name = "External Dashboard")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test(dataProvider = "testAccommodationCheckOutNotAfterTravelPlanEndDate_DataProvider")
    public void testAccommodationCheckOutNotAfterTravelPlanEndDate(Map<String, String> param) throws Exception {
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePage<?> homePage = new HomePage<>(session);
        LoginB2CInDWEB<?> loginB2CInDWEB = new LoginB2CInDWEB<>(session);
        TravelPlan<?> travelPlan = new TravelPlan<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        String plan = param.get("plan");
        String product = param.get("product");
        int daysDifference = Integer.parseInt(param.get("daysDifference"));


        step("Login via cookie injection and open corporate dashboard");
        homePage.loginViaCookieAndOpenCorporateDashboard(email, password);

        boolean isUserLoggedIn = loginB2CInDWEB.checkIfLoggedIn();

        step("Check that user is logged in");
        Assertion.assertThat("Is user logged in", isUserLoggedIn, is(true));

        step("User Navigates to Travel Plan Page.");
        travelPlan.selectTravelPlan();

        step("User selects the travel PLAN.");
        travelPlan.setTravelPlan(plan);

        step("User Selects Start Date.");
        travelPlan.setStartDate();

        step("User Selects End Date.");
        travelPlan.setEndDate(daysDifference);

        step("User add Travel Product");
        travelPlan.addTravelProduct(product);

        step("Verify that check out cannot be select after end Date.");
        Assertion.assertThat("Date should not be clickable", travelPlan.isAfterEndDateSelectable(), is(false));
    }

    @DataProvider(name = "testAccommodationCheckOutNotAfterTravelPlanEndDate_DataProvider")
    public Object[][] testAccommodationCheckOutNotAfterTravelPlanEndDate_DataProvider() {
        return getObjectParams("testAccommodationCheckOutNotAfterTravelPlanEndDate");
    }

    @Author(names = {"Manan Kumar"})
    @Module(name = "External Dashboard")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test(dataProvider = "testMultiCityFlightTravelPlanConfiguration_DataProvider")
    public void testMultiCityFlightTravelPlanConfiguration(Map<String, String> param) throws Exception {
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePage<?> homePage = new HomePage<>(session);
        LoginB2CInDWEB<?> loginB2CInDWEB = new LoginB2CInDWEB<>(session);
        CorporateExternalDashboardSidebar<?> corporateExternalDashboardSidebar = new CorporateExternalDashboardSidebar<>(session);
        TravelPlan<?> travelPlan = new TravelPlan<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String plan = CoreFunctions.randomName() + "Test Plan";
        String product = param.get("product");
        String dateRange = param.get("dateRange");
        String origin1 = param.get("origin1");
        String destination1 = param.get("destination1");
        String origin2 = param.get("origin2");
        String destination2 = param.get("destination2");
        String origin3 = param.get("origin3");
        String destination3 = param.get("destination3");
        String airline = param.get("airline");
        String cabin = param.get("cabin");
        String maxBudget = param.get("maxBudget");

        step("Login via cookie injection and open corporate dashboard");
        homePage.loginViaCookieAndOpenCorporateDashboard(email, password);

        step("Check that user is logged in");
        Assertion.assertThat("Is user logged in", loginB2CInDWEB.checkIfLoggedIn(), is(true));

        step("Open the Travel Plan");
        corporateExternalDashboardSidebar.openTravelPlanPage();

        step("Go to Create Travel Plan");
        travelPlan.clickCreateTravel();
        Assertion.assertThat("Verify if Create Travel Plan page is opened", travelPlan.isCreateTravelPageOpened(), is(true));

        step("Enter travel plan name, select dates and add Flights product");
        travelPlan.CreateTravelPlan(plan, dateRange, product, "", "");

        step("Verify Multi-city is present along with Oneway/Round-trip.");
        Assertion.assertThat("Oneway/Round-trip tab is displayed", travelPlan.isTripTypeTabDisplayed("Oneway/Round-trip"), is(true));
        Assertion.assertThat("Multi-city tab is displayed", travelPlan.isTripTypeTabDisplayed("Multi-city"), is(true));

        step("User clicks on Multi City.");
        travelPlan.selectMultiCity();

        step("User clicks on Set exceptions.");
        travelPlan.clickSetExceptions();

        step("Verify Add flight exception dialog is opened.");
        Assertion.assertThat("Add flight exception dialog is opened", travelPlan.isFlightExceptionDialogOpened(), is(true));

        step("User selects airline.");
        travelPlan.selectExceptionAirline(airline);

        step("User selects cabin class.");
        travelPlan.selectExceptionCabin(cabin);

        step("User enters max budget per flight.");
        travelPlan.enterMaxBudget(maxBudget);

        step("User clicks on Save exception.");
        travelPlan.saveException();

        step("Verify exception is added successfully.");
        Assertion.assertThat("Exception added successfully! message is displayed", travelPlan.isExceptionAddedSuccessfully(), is(true));

        step("User clicks on add route to get 3 routes.");
        travelPlan.addAnotherRoute();

        step("User adds origin and destination for route 1.");
        travelPlan.addMultiCityRoute(1, origin1, destination1);

        step("User adds origin and destination for route 2.");
        travelPlan.addMultiCityRoute(2, origin2, destination2);

        step("User selects date for route 2 within the travel plan dates.");
        travelPlan.selectRouteDate(2);

        step("User adds origin and destination for route 3.");
        travelPlan.addMultiCityRoute(3, origin3, destination3);

        step("User selects date for route 3 within the travel plan dates.");
        travelPlan.selectRouteDate(3);

        step("User clicks on create travel plan.");
        travelPlan.selectApproverAndSubmit();
        Assertion.assertThat("Verify if Travel Plan is created Successfully", travelPlan.isTravelPlanCreated(plan), is(true));

        step("User clicks on Open to go to travel plan detail.");
        travelPlan.clickOpenButton();

        step("Verify travel plan exception has the same budget that was passed.");
        Assertion.assertThat("Estimated budget of exception", travelPlan.getExceptionBudget(), is(String.format(java.util.Locale.US, "IDR %,d", Long.parseLong(maxBudget))));

        step("Verify all 3 routes are present for multi city.");
        Assertion.assertThat("Route 1 is present", travelPlan.isRoutePresent(1, origin1, destination1), is(true));
        Assertion.assertThat("Route 2 is present", travelPlan.isRoutePresent(2, origin2, destination2), is(true));
        Assertion.assertThat("Route 3 is present", travelPlan.isRoutePresent(3, origin3, destination3), is(true));

        step("User clicks on Cancel request to cleanup.");
        travelPlan.cancelTravelPlan();

        step("Verify travel plan request is cancelled successfully.");
        Assertion.assertThat("You've deleted your travel plan request. message is displayed", travelPlan.isTravelPlanDeletedSuccessfully(), is(true));
    }

    @DataProvider(name = "testMultiCityFlightTravelPlanConfiguration_DataProvider")
    public Object[][] testMultiCityFlightTravelPlanConfiguration_DataProvider() {
        return getObjectParams("testMultiCityFlightTravelPlanConfiguration");
    }

    @Author(names = {"Ram Pratap Singh"})
    @Module(name = "External Dashboard")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test(dataProvider = "verifyRefundIsApproved_DataProvider")
    public void verifyRefundIsApproved(Map<String, String> param) throws Exception {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPageWeb = FlightLandingPageWeb.getInstance(session);
        FlightSearchResultPageWeb<?> flightSearchResultPage = FlightSearchResultPageWeb.getInstance(session);
        FlightProductReviewPage<?> flightProductReviewPage = new FlightProductReviewPage<>(session);
        FlightBookingFormPageWeb<?> flightBookingFormPage = FlightBookingFormPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        TravelPlanWeb<?> travelPlanPage = TravelPlanWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        RefundPageWeb<?> refundPage = RefundPageWeb.getInstance(session);
        FlightApi<?> flightApi = new FlightApi<>(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);
        CorporateExternalDashboardSidebar<?> corporateExternalDashboardSidebar = new CorporateExternalDashboardSidebar<>(session);
        RefundListPage<?> refundListPage = new RefundListPage<>(session);
        HomePage<?> homePage1 = new HomePage<>(session);

        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");
        String airline = param.get("airline");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String departureDate = getRandomLongDateOnRange(daysToDepartureDate, 30);

        int adultPassengers = Integer.parseInt(param.get("adultPassengers"));
        int childPassengers = Integer.parseInt(param.get("childPassengers"));
        int infantPassengers = Integer.parseInt(param.get("infantPassengers"));

        String username = param.get("username");
        String username1 = param.get("username1");
        String password = param.get("password");
        String approverUsername = param.get("approverUsername");
        String approverPassword = param.get("approverPassword");
        String currency = param.get("currency");
        String name = param.get("name");
        String lastName = param.get("lastName");

        step("User open home page");
        homePage.goToHomePage();

        step("Go to account page");
        loginPage.goToAccountPage();

        step("login in using { " + username + " }");
        loginPage.loginIfNotWithSameEmail(username, password, "123456", 1);

        step("User select { " + currency + " } currency");
        homePage.selectCurrency(currency);

        step("User open Flight vertical");
        homePage.openVerticalPageViaURL("Flights");

        step("Toggle trip type");
        flightLandingPageWeb.toggleTripType(tripType);

        step("Select origin airport");
        flightLandingPageWeb.selectOrigin(originAirport);

        step("Select destination airport");
        flightLandingPageWeb.selectDestination(destinationAirport);

        step("Select date");
        flightLandingPageWeb.selectDate(departureDate);

        step("add passenger");
        flightLandingPageWeb.addPassenger(adultPassengers, childPassengers, infantPassengers);

        step("search flight");
        flightLandingPageWeb.searchFlight();

        step("Apply Filter " + airline);
        flightSearchResultPage.filterAirline(airline);

        step("select flight");
        flightSearchResultPage.selectFlightByAirline(airline);

        step("Continue to booking form");
        flightProductReviewPage.continueToBookingForm();
        flightBookingFormPage.clickNoThanksOnFlightMayReschedulePopup();

        step("Click toggle to fill passengers details and save ");
        flightBookingFormPage.fillPassengerDetailsLogin(name, lastName);

        step("Go to payment page");
        flightBookingFormPage.goToPaymentPageFromBookingFormPage();

        step("Skip Flight may rescheduled page by clicking no thanks");
        travelPlanPage.skipTravelPlan();
        flightBookingFormPage.clickNoThanksOnFlightMayReschedulePopup();

        step("Payment page should be displayed");
        String orderID = paymentPage.getOrderId();
        orderID = String.valueOf(getOrderId(orderID));
        Assertion.assertThat("Corporate billing option is selected", paymentPage.getSelectedPayment().toLowerCase(), is("corporate billing"));

        step("get total price payment page");
        paymentPage.totalPricePaymentPage();

        step("Submit payment using Corporate Billing");
        paymentPage.clickOnPaymentButton();

        step("approve payment of order id: { " + orderID + " } " + " using admin account with username : { " + approverUsername + " }");
        corporateApi.approveBookingB2BApi(approverUsername, approverPassword, orderID);

        step("get booking code from API");
        String bookingCode = flightApi.getBookingCode(orderID, username);

        step("Open the order list");
        paymentPage.clickSeeOrderListButton();

        step("set Tiket Number on flight dashboard and ticket will be eticket issued");
        flightApi.flightEticketIssuedRequest(orderID, bookingCode, username, name + " " + lastName, 20);

        step("go to order detail page");
        orderListPage.openIssuedTicketViaURL(orderID, 15);
        orderDetailPage.closeEticketPopUp();
        orderDetailPage.getVerticalTotalPrice();

        step("Click refund button");
        orderDetailPage.clickRefundButton();
        refundPage.skipRescheduleNudgeIfShown();

        step("Fill refund form");
        refundPage.selectRefundReasonPersonalReasonFlight();
        refundPage.chooseSpecificReason();

        step("verify refund estimation");
        double totalRefundPrice = refundPage.getTotalRefundPrice(currency);
        refundPage.continueRefund();

        step("verify total refund price");
        double totalRefundPricePage2 = refundPage.getTotalRefundPrice(currency);
        Assertion.assertThat("total refund price are same on both page", totalRefundPrice, is(totalRefundPricePage2));

        step("submit refund");
        refundPage.clickChecklistTnc();
        refundPage.clickSubmitRefund();
        refundPage.clickProcessRefund();
        refundPage.clickGoToOrders();

        step("Go to Corporate dashboard page");
        corporateExternalDashboardSidebar.goToCorporateDashboardPage();

        step("logout from corporate dashboard");
        homePage1.logoutFromAdminDashboard();

        step("User open home page");
        homePage.goToHomePage();

        step("Go to account page");
        loginPage.goToAccountPage();

        step("login in using { " + username1 + " }");
        loginPage.loginIfNotWithSameEmail(username1, password, "123456", 1);

        step("Go to Corporate dashboard page");
        corporateExternalDashboardSidebar.goToCorporateDashboardPage();

        step("Go to Refund List page");
        refundListPage.goToRefundListPage();

        step("Search for the order ID in Refund List");
        refundListPage.searchByOrderID(orderID);

        step("The user approves the refund");
        refundListPage.approveRefund();

        step("The user confirms the approval");
        boolean isRefundApproved = refundListPage.isRefundApproved();
        Assertion.assertThat("Verify that the refund is approved", isRefundApproved, is(true));
    }

    @DataProvider(name = "verifyRefundIsApproved_DataProvider")
    public Object[][] verifyRefundIsApproved_DataProvider() {
        return getObjectParams("verifyRefundIsApproved");
    }

    @Author(names = {"Ram Pratap Singh"})
    @Module(name = "External Dashboard")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test(dataProvider = "verifyRefundIsRejected_DataProvider")
    public void verifyRefundIsRejected(Map<String, String> param) throws Exception {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        FlightLandingPageWeb<?> flightLandingPageWeb = FlightLandingPageWeb.getInstance(session);
        FlightSearchResultPageWeb<?> flightSearchResultPage = FlightSearchResultPageWeb.getInstance(session);
        FlightProductReviewPage<?> flightProductReviewPage = new FlightProductReviewPage<>(session);
        FlightBookingFormPageWeb<?> flightBookingFormPage = FlightBookingFormPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        TravelPlanWeb<?> travelPlanPage = TravelPlanWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        RefundPageWeb<?> refundPage = RefundPageWeb.getInstance(session);
        FlightApi<?> flightApi = new FlightApi<>(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);
        CorporateExternalDashboardSidebar<?> corporateExternalDashboardSidebar = new CorporateExternalDashboardSidebar<>(session);
        RefundListPage<?> refundListPage = new RefundListPage<>(session);
        HomePage<?> homePage1 = new HomePage<>(session);

        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");
        String airline = param.get("airline");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String departureDate = getRandomLongDateOnRange(daysToDepartureDate, 30);

        int adultPassengers = Integer.parseInt(param.get("adultPassengers"));
        int childPassengers = Integer.parseInt(param.get("childPassengers"));
        int infantPassengers = Integer.parseInt(param.get("infantPassengers"));

        String username = param.get("username");
        String username1 = param.get("username1");
        String password = param.get("password");
        String approverUsername = param.get("approverUsername");
        String approverPassword = param.get("approverPassword");
        String currency = param.get("currency");
        String name = param.get("name");
        String lastName = param.get("lastName");

        step("User open home page");
        homePage.goToHomePage();

        step("Go to account page");
        loginPage.goToAccountPage();

        step("login in using { " + username + " }");
        loginPage.loginIfNotWithSameEmail(username, password, "123456", 1);

        step("User select { " + currency + " } currency");
        homePage.selectCurrency(currency);

        step("User open Flight vertical");
        homePage.openVerticalPageViaURL("Flights");

        step("Toggle trip type");
        flightLandingPageWeb.toggleTripType(tripType);

        step("Select origin airport");
        flightLandingPageWeb.selectOrigin(originAirport);

        step("Select destination airport");
        flightLandingPageWeb.selectDestination(destinationAirport);

        step("Select date");
        flightLandingPageWeb.selectDate(departureDate);

        step("add passenger");
        flightLandingPageWeb.addPassenger(adultPassengers, childPassengers, infantPassengers);

        step("search flight");
        flightLandingPageWeb.searchFlight();

        step("Apply Filter " + airline);
        flightSearchResultPage.filterAirline(airline);

        step("select flight");
        flightSearchResultPage.selectFlightByAirline(airline);

        step("Continue to booking form");
        flightProductReviewPage.continueToBookingForm();
        flightBookingFormPage.clickNoThanksOnFlightMayReschedulePopup();

        step("Click toggle to fill passengers details and save ");
        flightBookingFormPage.fillPassengerDetailsLogin(name, lastName);

        step("Go to payment page");
        flightBookingFormPage.goToPaymentPageFromBookingFormPage();

        step("Skip Flight may rescheduled page by clicking no thanks");
        travelPlanPage.skipTravelPlan();
        flightBookingFormPage.clickNoThanksOnFlightMayReschedulePopup();

        step("Payment page should be displayed");
        String orderID = paymentPage.getOrderId();
        orderID = String.valueOf(getOrderId(orderID));
        Assertion.assertThat("Corporate billing option is selected", paymentPage.getSelectedPayment().toLowerCase(), is("corporate billing"));

        step("get total price payment page");
        paymentPage.totalPricePaymentPage();

        step("Submit payment using Corporate Billing");
        paymentPage.clickOnPaymentButton();

        step("approve payment of order id: { " + orderID + " } " + " using admin account with username : { " + approverUsername + " }");
        corporateApi.approveBookingB2BApi(approverUsername, approverPassword, orderID);

        step("get booking code from API");
        String bookingCode = flightApi.getBookingCode(orderID, username);

        step("Open the order list");
        paymentPage.clickSeeOrderListButton();

        step("set Tiket Number on flight dashboard and ticket will be eticket issued");
        flightApi.flightEticketIssuedRequest(orderID, bookingCode, username, name + " " + lastName, 20);

        step("go to order detail page");
        orderListPage.openIssuedTicketViaURL(orderID, 15);
        orderDetailPage.closeEticketPopUp();
        orderDetailPage.getVerticalTotalPrice();

        step("Click refund button");
        orderDetailPage.clickRefundButton();
        refundPage.skipRescheduleNudgeIfShown();

        step("Fill refund form");
        refundPage.selectRefundReasonPersonalReasonFlight();
        refundPage.chooseSpecificReason();

        step("verify refund estimation");
        double totalRefundPrice = refundPage.getTotalRefundPrice(currency);
        refundPage.continueRefund();

        step("verify total refund price");
        double totalRefundPricePage2 = refundPage.getTotalRefundPrice(currency);
        Assertion.assertThat("total refund price are same on both page", totalRefundPrice, is(totalRefundPricePage2));

        step("submit refund");
        refundPage.clickChecklistTnc();
        refundPage.clickSubmitRefund();
        refundPage.clickProcessRefund();
        refundPage.clickGoToOrders();

        step("Go to Corporate dashboard page");
        corporateExternalDashboardSidebar.goToCorporateDashboardPage();

        step("logout from corporate dashboard");
        homePage1.logoutFromAdminDashboard();

        step("User open home page");
        homePage.goToHomePage();

        step("Go to account page");
        loginPage.goToAccountPage();

        step("login in using { " + username1 + " }");
        loginPage.loginIfNotWithSameEmail(username1, password, "123456", 1);

        step("Go to Corporate dashboard page");
        corporateExternalDashboardSidebar.goToCorporateDashboardPage();

        step("Go to Refund List page");
        refundListPage.goToRefundListPage();

        step("Search for the order ID in Refund List");
        refundListPage.searchByOrderID(orderID);

        step("The user rejects the refund");
        refundListPage.rejectRefund(name);

        step("The user confirms the rejection");
        boolean isRefundRejected = refundListPage.isRefundRejected();
        Assertion.assertThat("Verify that the refund is rejected", isRefundRejected, is(true));
    }

    @DataProvider(name = "verifyRefundIsRejected_DataProvider")
    public Object[][] verifyRefundIsRejected_DataProvider() {
        return getObjectParams("verifyRefundIsRejected");
    }

    @Author(names = {"Nishtha"})
    @Module(name = "Corporate Data")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test(dataProvider = "testPICCanChangeSuperAdmin_DataProvider")
    public void testPICCanChangeSuperAdmin(Map<String, String> param) throws Exception {
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<>(session);
        CorporateDataPage<?> corporateDataPage = new CorporateDataPage<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String corporateName = param.get("corporateName");

        step("Login via cookie injection");
        loginMemberAdmin.loginViaCookie(email, password);

        step("Go to Corporate Internal Dashboard");
        loginMemberAdmin.goToDashboard();

        step("Check if user is logged in");
        boolean isLoggedInCRP = corporateDataPage.checkUserIsLoggedIn();

        step("Assert that user is is logged in to corporate dashboard");
        Assertion.assertThat("User is logged in", isLoggedInCRP, is(true));

        step("Click ON corporate information");
        corporateDataPage.clickCorporateInformation();

        step("Click ON corporate data");
        corporateDataPage.clickCorporateData();

        step("Send corporate name to search");
        corporateDataPage.inputSearch(corporateName);

        step("Click Search");
        corporateDataPage.clickSearch();

        step("Check if search is successful");
        corporateDataPage.checkIfCorporateExists(corporateName);

        step("Click on See Details Action Button");
        corporateDataPage.clickSeeDetailsBtn();

        step("Click ON Edit Button");
        corporateDataPage.clickEditBtn();

        step("Fill Company Details");
        String officeAddress = param.get("officeAddress");
        String addressDetail = param.get("addressDetail");
        String fullName = CoreFunctions.randomName();
        corporateDataPage.fillCompanyDetails(officeAddress, addressDetail, fullName, corporateName);

        step("Update the Corporate PIC name");
        String corporatePICName1 = param.get("corporatePICName1");
        String corporatePICName2 = param.get("corporatePICName2");
        corporateDataPage.fillCorporatePICDetails(corporatePICName1, corporatePICName2);

        step("Verify that the corporate PIC is updated");
        String expectedSnackbarText = param.get("expectedSnackbarText");
        Assertion.assertThat("Verify if Corporate PIC is same as passed.", corporateDataPage.isCorporatePICUpdated(expectedSnackbarText), is(true));
    }

    @DataProvider(name = "testPICCanChangeSuperAdmin_DataProvider")
    public Object[][] testPICCanChangeSuperAdmin_DataProvider() {
        return getObjectParams("testPICCanChangeSuperAdmin");
    }

    @Author(names = {"Rizki Maulana"})
    @Module(name = "Corporate Data")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test(dataProvider = "testCorporateDocumentsShowNecessaryDocuments_DataProvider")
    public void testCorporateDocumentsShowNecessaryDocuments(Map<String, String> param) throws Exception {
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<>(session);
        CorporateDataPage<?> corporateDataPage = new CorporateDataPage<>(session);
        CorporateDetailPageWeb<?> corporateDetailPage = CorporateDetailPageWeb.getInstance(session);

        String email = param.get("email");
        String password = param.get("password");
        String corporateName = param.get("corporateName");

        step("Login via AM account on Internal Dashboard");
        loginMemberAdmin.loginViaCookie(email, password);
        loginMemberAdmin.goToDashboard();

        step("Click on Corporate Information (Left side Navbar)");
        corporateDataPage.clickCorporateInformation();

        step("Click on Corporate Data and select any corporate");
        corporateDataPage.clickCorporateData();
        if (corporateName != null && !corporateName.isEmpty()) {
            corporateDataPage.inputSearch(corporateName);
            corporateDataPage.clickSearch();
        }

        step("Go to corporate Detail Page");
        corporateDataPage.clickSeeDetailsBtn();

        step("Click on Corporate Document");
        corporateDetailPage.clickCorporateDocuments();

        step("Assertions");
        Assertion.assertThat("Validate there is E-Agreement", corporateDetailPage.validateThereIsEAgreement(), is(true));
        Assertion.assertThat("Validate there is Legal Docs", corporateDetailPage.validateThereIsLegalDocument(), is(true));
        Assertion.assertThat("Validate there is E-Agreement Supporting Documents", corporateDetailPage.validateThereIsEAgreementSupportingDocument(), is(true));

        step("Log out");
        loginMemberAdmin.logout();
    }

    @DataProvider(name = "testCorporateDocumentsShowNecessaryDocuments_DataProvider")
    public Object[][] testCorporateDocumentsShowNecessaryDocuments_DataProvider() {
        return getObjectParams("testCorporateDocumentsShowNecessaryDocuments");
    }

    @Author(names = {"Rizki Maulana"})
    @Module(name = "Corporate Data")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test(dataProvider = "testCorporateDetailPageHasFourTabs_DataProvider")
    public void testCorporateDetailPageHasFourTabs(Map<String, String> param) throws Exception {
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<>(session);
        CorporateDataPage<?> corporateDataPage = new CorporateDataPage<>(session);
        CorporateDetailPageWeb<?> corporateDetailPage = CorporateDetailPageWeb.getInstance(session);

        String email = param.get("email");
        String password = param.get("password");
        String corporateName = param.get("corporateName");

        step("Login via AM account on Internal Dashboard");
        loginMemberAdmin.loginViaCookie(email, password);
        loginMemberAdmin.goToDashboard();

        step("Click on Corporate Information (Left side Navbar)");
        corporateDataPage.clickCorporateInformation();

        step("Click on Corporate Data and select any corporate");
        corporateDataPage.clickCorporateData();
        if (corporateName != null && !corporateName.isEmpty()) {
            corporateDataPage.inputSearch(corporateName);
            corporateDataPage.clickSearch();
        }

        step("Go to corporate Detail Page");
        corporateDataPage.clickSeeDetailsBtn();

        step("Assertions");
        Assertion.assertThat("All 4 Tabs Sould be visible on Corporate Detail Page", corporateDetailPage.validateThereAre4Tabs(), is(true));

        step("Log out");
        loginMemberAdmin.logout();
    }

    @DataProvider(name = "testCorporateDetailPageHasFourTabs_DataProvider")
    public Object[][] testCorporateDetailPageHasFourTabs_DataProvider() {
        return getObjectParams("testCorporateDetailPageHasFourTabs");
    }


    @Author(names = {"Rizki Maulana"})
    @Module(name = "Registered Corporate")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test(dataProvider = "testDocumentSubmissionTabShowsUploadedDocuments_DataProvider")
    public void testDocumentSubmissionTabShowsUploadedDocuments(Map<String, String> param) throws Exception {
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<>(session);
        CorporateDataPage<?> corporateDataPage = new CorporateDataPage<>(session);
        RegisteredCorporatePage<?> registeredCorporatePage = new RegisteredCorporatePage<>(session);

        String email = param.get("email");
        String password = param.get("password");

        step("Login to Internal Dashboard");
        loginMemberAdmin.loginViaCookie(email, password);
        loginMemberAdmin.goToDashboard();

        step("Click on Registered Corp Tab on left Side navbar");
        corporateDataPage.clickCorporateInformation();
        corporateDataPage.clickRegisteredCorporate();

        step("Click on Lead Name");
        corporateDataPage.clickRandomLeadNames();

        step("Click on Negotiation & Pre-Onboarding");
        registeredCorporatePage.clickNegotiationPreOnboardingTab();

        step("Click on Document Tab");
        registeredCorporatePage.clickDocumentSubmissionButton();

        step("Assertions");
        Assertion.assertThat("Legal and Supporting All documents should be visible", registeredCorporatePage.verifyRequiredAndSupportingDocumentsDisplayed(), is(true));

        step("Log out");
        loginMemberAdmin.logout();
    }

    @DataProvider(name = "testDocumentSubmissionTabShowsUploadedDocuments_DataProvider")
    public Object[][] testDocumentSubmissionTabShowsUploadedDocuments_DataProvider() {
        return getObjectParams("testDocumentSubmissionTabShowsUploadedDocuments");
    }

    @Author(names = {"Rizki Maulana"})
    @Module(name = "Registered Corporate")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test(dataProvider = "testApprovedLeadHasOnboardText_DataProvider")
    public void testApprovedLeadHasOnboardText(Map<String, String> param) throws Exception {
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<>(session);
        CorporateDataPage<?> corporateDataPage = new CorporateDataPage<>(session);
        RegisteredCorporatePage<?> registeredCorporatePage = new RegisteredCorporatePage<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String status = param.get("status");

        step("Login to Internal Dashboard");
        loginMemberAdmin.loginViaCookie(email, password);
        loginMemberAdmin.goToDashboard();

        step("Click on Registered Corp Tab on left Side navbar");
        corporateDataPage.clickCorporateInformation();
        corporateDataPage.clickRegisteredCorporate();

        step("Filter for Approved corporates");
        corporateDataPage.setStatusFilter(status);

        step("Click on Lead Name");
        corporateDataPage.clickRandomLeadNames();

        step("Click on Negotiation & Pre-Onboarding");
        registeredCorporatePage.clickNegotiationPreOnboardingTab();

        step("Click on Proceed to Onboard");
        registeredCorporatePage.clickProceedToOnboardingButton();

        step("Assertions");
        Assertion.assertThat("Ready to onboard text should be present", registeredCorporatePage.verifyReadyToOnboardTextDisplayed(), is(true));

        step("Log out");
        loginMemberAdmin.logout();
    }

    @DataProvider(name = "testApprovedLeadHasOnboardText_DataProvider")
    public Object[][] testApprovedLeadHasOnboardText_DataProvider() {
        return getObjectParams("testApprovedLeadHasOnboardText");
    }

    @Author(names = {"Manan Kumar"})
    @Module(name = "External Dashboard")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test(dataProvider = "testUNMRedirectionLinkInEmployeeEmailTemplate_DataProvider")
    public void testUNMRedirectionLinkInEmployeeEmailTemplate(Map<String, String> param) throws Exception {
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePage<?> homePage = new HomePage<>(session);
        EmployeeListPage<?> employeeListPage = new EmployeeListPage<>(session);
        LoginB2CInDWEB<?> loginB2CInDWEB = new LoginB2CInDWEB<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        String name = param.get("name");
        String email2 = param.get("email2");

        step("Login via cookie injection and open corporate dashboard");
        homePage.loginViaCookieAndOpenCorporateDashboard(email, password);

        boolean isUserLoggedIn = loginB2CInDWEB.checkIfLoggedIn();

        step("Check that user is logged in");
        Assertion.assertThat("Is user logged in", isUserLoggedIn, is(true));

        step("User Navigates to Employee List Page.");
        employeeListPage.goToEmployeeListPage();

        step("User searches for the name of employee.");
        employeeListPage.searchNameEmployee(name);

        step("User clicks on Action button.");
        employeeListPage.clickEmployeeActionButton();

        step("User re-send the Registration mail.");
        employeeListPage.sendMail();

        step("Verify that the Registration mail is sent successfully.");
        Assertion.assertThat("Registration mail is sent successfully", employeeListPage.isMailSentSuccessfully(), is(true));

        step("User Opens mail page.");
        employeeListPage.openYopmailInNewTab(email2);

        step("Verify that the UNM redirection link is present in the email template.");
        Assertion.assertThat("UNM redirection link is present in the email template", employeeListPage.isUNMRedirectionLinkPresent(email2), is(true));
    }

    @DataProvider(name = "testUNMRedirectionLinkInEmployeeEmailTemplate_DataProvider")
    public Object[][] testUNMRedirectionLinkInEmployeeEmailTemplate_DataProvider() {
        return getObjectParams("testUNMRedirectionLinkInEmployeeEmailTemplate");
    }

    @Author(names = {"Nishtha"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test(dataProvider = "testEndToEndCorporateOnboarding_DataProvider")
    public void testEndToEndCorporateOnboarding(Map<String, String> param) throws Exception {
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        RegisterB2CInWEB<?> registerB2CInDWEB = new RegisterB2CInWEB<>(session);

        Long timeStamp = System.currentTimeMillis();
        String email = (timeStamp) + "auto@gmail.com";
        String otp = param.get("otp");
        String fullName = CoreFunctions.randomName();
        String phoneNumber = String.valueOf(CoreFunctions.randomInt(10));
        String corporateName = CoreFunctions.randomName() + CoreFunctions.generateRandomNumeric(8);
        String officeAddress = param.get("officeAddress");
        String filePath = param.get("filePath");

        step("The user navigates to the corporate landing page, clears session cookies and switches the language to English.");
        registerB2CInDWEB.navigateToCorporateLandingPage();
        registerB2CInDWEB.clearCorporateSessionCookies();
        registerB2CInDWEB.selectEnglishLanguage();

        step("The user clicks register now button.");
        registerB2CInDWEB.clickRegisterNowButton();

        step("The user enters the email and click check Email button");
        registerB2CInDWEB.inputEmail(email);
        registerB2CInDWEB.clickCheckEmailButton();
        registerB2CInDWEB.inputOTPCode(otp);

        step("The user will proceed to get registration form");
        registerB2CInDWEB.fillUserDetails(fullName, phoneNumber);
        registerB2CInDWEB.clickGetRegistrationFormButton();
        registerB2CInDWEB.clickContinueToRegistrationFormButton();

        step("The user fill the sign up details with Invoice as payment method");
        registerB2CInDWEB.fillSignUpDetailsToRegister(fullName, corporateName, officeAddress, filePath, "Invoice");
        Assertion.assertThat("Verify invoice payment is clicked", registerB2CInDWEB.isPaymentMethodSelected("Invoice"), is(true));
        registerB2CInDWEB.clickContinueButton();

        step("The user uploads the document and click on continue button and send request.");
        List<String> pendingDocumentsBeforeUpload = registerB2CInDWEB.getAllDocuments();
        registerB2CInDWEB.uploadAllPendingRequiredDocuments(filePath);
        Assertion.assertThat("Verify all docs are uploaded", registerB2CInDWEB.getAllDocuments().isEmpty(), is(true));
        registerB2CInDWEB.clickOnContinue();
        registerB2CInDWEB.clickSendButton();

        step("Verify waiting for document approval page will open");
        Assertion.assertThat("Verify waiting for document approval page will open", registerB2CInDWEB.isWaitingForApprovalPageOpened(), is(true));

        step("Verify uploaded document section");
        Assertion.assertThat("Verify uploaded document section", registerB2CInDWEB.isUploadedDocumentSectionDisplayed(), is(true));
        Assertion.assertThat("Verify uploaded documents exist", registerB2CInDWEB.verifyUploadedDocumentsExist(pendingDocumentsBeforeUpload), is(true));

        step("Verify corporate details section");
        Assertion.assertThat("Verify corporate details section", registerB2CInDWEB.isCorporateDetailsSectionDisplayed(), is(true));
        Assertion.assertThat("Verify corporate name", registerB2CInDWEB.getValueByFieldName("Corporate Name"), is(corporateName));

        step("Verify payment details section");
        Assertion.assertThat("Verify payment details section", registerB2CInDWEB.isPaymentDetailsSectionDisplayed(), is(true));
        Assertion.assertThat("Verify payment method is Invoice", registerB2CInDWEB.getValueByFieldName("Payment Method"), is("Invoice"));
    }

    @DataProvider(name = "testEndToEndCorporateOnboarding_DataProvider")
    public Object[][] testEndToEndCorporateOnboarding_DataProvider() {
        return getObjectParams("testEndToEndCorporateOnboarding");
    }

    @Author(names = {"Nishtha"})
    @Module(name = "Travel policy")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test(dataProvider = "testCreateCustomFieldForSingleSelectionSuperAdmin_DataProvider")
    public void testCreateCustomFieldForSingleSelectionSuperAdmin(Map<String, String> param) throws Exception {
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePage<?> homePage = new HomePage<>(session);
        LoginB2CInDWEB<?> loginB2CInDWEB = new LoginB2CInDWEB<>(session);
        CorporateExternalDashboardSidebar<?> CorporateExternalDashboardSidebar = new CorporateExternalDashboardSidebar<>(session);
        CustomFieldPage<?> customFieldPage = new CustomFieldPage<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");

        step("Login via cookie injection and open corporate dashboard");
        homePage.loginViaCookieAndOpenCorporateDashboard(email, password);

        boolean isUserLoggedIn = loginB2CInDWEB.checkIfLoggedIn();

        step("Check that user is logged in");
        Assertion.assertThat("Is user logged in", isUserLoggedIn, is(true));

        step("Click on Settings on Sidebar");
        CorporateExternalDashboardSidebar.clickOnSettings();

        Assertion.assertThat("Verify CustomField Option is Present on Sidebar", CorporateExternalDashboardSidebar.isCustomFieldOptionPresent(), is(true));

        step("Click on Custom Field Sidebar to check its redirection");
        CorporateExternalDashboardSidebar.goToTravelerCustomField();

        Assertion.assertThat("Verify Custom field page is Opened and Heading is Present", customFieldPage.isCustomFieldHeadingVisible(), is(true));
        customFieldPage.clickAddCustomFieldButton();

        Assertion.assertThat("Verify Custom Field Form is Opened", customFieldPage.isFormHeadingPresent(), is(true));

        String FieldName = CoreFunctions.randomName() + "AutoField";
        String Option1 = CoreFunctions.generateRandomString(4) + "Auto-Option";
        String Option2 = CoreFunctions.generateRandomString(4) + "Auto-Option2";

        step("Create a Single selection custom Field");

        customFieldPage.checkInterimPage();
        customFieldPage.enterFieldName(FieldName);

        customFieldPage.fillCustomFieldForm("Single Selection", Option1, Option2);
        String text = customFieldPage.getSnackbarText();
        Assertion.assertThat("Success Snack bar shows the name of Custom Field Added", text, containsString(FieldName));

        customFieldPage.deleteCustomField();
        customFieldPage.verifyDeleteSnackbar();

        step("Log out From Admin dashboard");
        homePage.logoutFromAdminDashboard();

    }

    @DataProvider(name = "testCreateCustomFieldForSingleSelectionSuperAdmin_DataProvider")
    public Object[][] testCreateCustomFieldForSingleSelectionSuperAdmin_DataProvider() {
        return getObjectParams("testCreateCustomFieldForSingleSelectionSuperAdmin");
    }

    @Author(names = {"Nishtha"})
    @Module(name = "Travel plan")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test(dataProvider = "testCustomFieldsAreDisplayedOnTravelPlanPage_DataProvider")
    public void testCustomFieldsAreDisplayedOnTravelPlanPage(Map<String, String> param) throws Exception {
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePage<?> homePage = new HomePage<>(session);
        LoginB2CInDWEB<?> loginB2CInDWEB = new LoginB2CInDWEB<>(session);
        CorporateExternalDashboardSidebar<?> CorporateExternalDashboardSidebar = new CorporateExternalDashboardSidebar<>(session);
        TravelPlan<?> travelPlan = new TravelPlan<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        String product = param.get("product");
        String name = CoreFunctions.randomName() + "Test Plan";
        String arrival = param.get("arrival");
        String departure = param.get("departure");
        String dateRange = param.get("dateRange");

        step("Login via cookie injection and open corporate dashboard");
        homePage.loginViaCookieAndOpenCorporateDashboard(email, password);

        boolean isUserLoggedIn = loginB2CInDWEB.checkIfLoggedIn();

        step("Check that user is logged in");
        Assertion.assertThat("Is user logged in", isUserLoggedIn, is(true));

        step("Open the Travel Plan");
        CorporateExternalDashboardSidebar.openTravelPlanPage();

        step("Go to Create Travel Plan");

        travelPlan.clickCreateTravel();
        Assertion.assertThat("Verify if Create Travel Plan page is opened", travelPlan.isCreateTravelPageOpened(), is(true));

        step("Create Travel Plan");
        travelPlan.CreateTravelPlan(name, dateRange, product, arrival, departure);
        travelPlan.selectApprover();
        travelPlan.travelPlanAdditionalDetails();

        step("Users sends and submit the request");
        travelPlan.clickRequestApproval();
        travelPlan.clickSubmitApproval();

        step("Travel Plan Request is Submitted");
        Assertion.assertThat("Verify if Travel Plan is created Successfully", travelPlan.isTravelPlanCreated(name), is(true));

        step("User clicks on open button");
        travelPlan.clickOpenButton();

        step("Verify the custom field values are same as passed");
        Assertion.assertThat("Verify if custom field values are same as passed", travelPlan.checkIfCustomFieldsArePresent(), is(true));

        step("Delete The Travel Plan");
        travelPlan.clickCancelButton();

        step("Log out From Admin dashboard");
        homePage.logoutFromAdminDashboard();
    }

    @DataProvider(name = "testCustomFieldsAreDisplayedOnTravelPlanPage_DataProvider")
    public Object[][] testCustomFieldsAreDisplayedOnTravelPlanPage_DataProvider() {
        return getObjectParams("testCustomFieldsAreDisplayedOnTravelPlanPage");
    }

    @Author(names = {"Nishtha"})
    @Module(name = "Travel plan")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test(dataProvider = "testApproverCanSeeRequestForTravelPlanCustomCreation_DataProvider")
    public void testApproverCanSeeRequestForTravelPlanCustomCreation(Map<String, String> param) throws Exception {
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePage<?> homePage = new HomePage<>(session);
        LoginB2CInDWEB<?> loginB2CInDWEB = new LoginB2CInDWEB<>(session);
        CorporateExternalDashboardSidebar<?> CorporateExternalDashboardSidebar = new CorporateExternalDashboardSidebar<>(session);
        TravelPlan<?> travelPlan = new TravelPlan<>(session);

        String emailAdmin = param.get("emailAdmin");
        String password = param.get("password");
        String otp = param.get("otp");
        String product = param.get("product");
        String name = CoreFunctions.randomName() + "Test Plan";
        String arrival = param.get("arrival");
        String departure = param.get("departure");
        String dateRange = param.get("dateRange");

        step("Login as admin via cookie injection and open corporate dashboard");
        homePage.loginViaCookieAndOpenCorporateDashboard(emailAdmin, password);

        boolean isUserLoggedIn = loginB2CInDWEB.checkIfLoggedIn();

        step("Check that user is logged in");
        Assertion.assertThat("Is user logged in", isUserLoggedIn, is(true));

        step("Open the Travel Plan");
        CorporateExternalDashboardSidebar.openTravelPlanPage();

        step("Go to Create Travel Plan");
        travelPlan.clickCreateTravel();

        step("Create Travel Plan");
        travelPlan.CreateTravelPlan(name, dateRange, product, arrival, departure);
        travelPlan.selectApprover();
        travelPlan.travelPlanAdditionalDetails();

        step("Users sends and submit the request");
        travelPlan.clickRequestApproval();
        travelPlan.clickSubmitApproval();

        step("Travel Plan Request is Submitted");
        Assertion.assertThat("Verify if Travel Plan is created Successfully", travelPlan.isTravelPlanCreated(name), is(true));

        step("User clicks on open button");
        travelPlan.clickOpenButton();

        step("Verify the custom field values are same as passed");
        Assertion.assertThat("Verify if custom field values are same as passed", travelPlan.checkIfCustomFieldsArePresent(), is(true));

        step("Log out From Admin dashboard");
        homePage.logoutFromAdminDashboard();

        //login approver
        String emailApprover = param.get("emailApprover");
        step("Login as approver via cookie injection and open corporate dashboard");
        homePage.loginViaCookieAndOpenCorporateDashboard(emailApprover, password);

        isUserLoggedIn = loginB2CInDWEB.checkIfLoggedIn();

        step("Check that user is logged in");
        Assertion.assertThat("Is user logged in", isUserLoggedIn, is(true));

        step("Open the Travel Plan");
        CorporateExternalDashboardSidebar.clickTravelPlanSidebar();

        step("Go to Travel Plan Requests");
        CorporateExternalDashboardSidebar.clickTravelPlanRequest();

        step("Verify that the request is visible to the approver");
        CorporateExternalDashboardSidebar.clickOpenButton();

        step("Verify the custom field values are same as passed");
        String expectedAdditionalDetails = param.get("expectedAdditionalDetails");
        Assertion.assertThat("Verify if custom field values are same as passed", travelPlan.checkIfTravelPlanRequestIsPresent(expectedAdditionalDetails), is(true));

        step("Log out From Admin dashboard");
        homePage.logoutFromAdminDashboard();
    }

    @DataProvider(name = "testApproverCanSeeRequestForTravelPlanCustomCreation_DataProvider")
    public Object[][] testApproverCanSeeRequestForTravelPlanCustomCreation_DataProvider() {
        return getObjectParams("testApproverCanSeeRequestForTravelPlanCustomCreation");
    }

    @Author(names = {"Manan Kumar"})
    @Module(name = "External Dashboard")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test(dataProvider = "testApprovalNotesDisplayed_DataProvider")
    public void testApprovalNotesDisplayed(Map<String, String> param) throws Exception {
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePage<?> homePage = new HomePage<>(session);
        LoginB2CInDWEB<?> loginB2CInDWEB = new LoginB2CInDWEB<>(session);
        PurchaseListPage<?> purchaseListPage = new PurchaseListPage<>(session);
        RescheduleListPageWeb<?> rescheduleListPageWeb = RescheduleListPageWeb.getInstance(session);
        CorporateExternalDashboardSidebar<?> corporateExternalDashboardSidebar = new CorporateExternalDashboardSidebar<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        String location = param.get("location");
        String name = param.get("name");
        String notes = param.get("notes");
        String approverMail = param.get("approverMail");


        step("Login as traveler via cookie injection");
        homePage.loginViaCookie(email, password);

        step("User click on skip for now button for passKey.");
        loginB2CInDWEB.clickSkipForNowButton();

        step("User navigates to hotel Page.");
        rescheduleListPageWeb.gotoHotelPage();

        step("User Enters for hotel location.");
        rescheduleListPageWeb.enterHotelLocation(location);

        step("User Enters CheckIn and CheckOut Date.");
        rescheduleListPageWeb.enterDate();

        step("User Clicks on Search Button.");
        rescheduleListPageWeb.clickSearchHotelButton();

        step("User Book Hotel with 100% refund and reschedule.");
        rescheduleListPageWeb.bookHotelWithFullRefundAndReschedule();

        step("User Enters The Guest Name");
        rescheduleListPageWeb.enterGuestName(name);

        step("User Clicks on Select payment method.");
        rescheduleListPageWeb.selectPaymentMethod();

        step("User Enter Approval Notes for Approver");
        rescheduleListPageWeb.enterApprovalNotes(notes);

        step("User Clicks on Continue Payment Button.");
        rescheduleListPageWeb.clickContinueToPayment();

        step("User Clicks on Pay With Corporate Billing.");
        rescheduleListPageWeb.clickPayWithCorporateBilling();

        step("User clicks on SEE order list");
        rescheduleListPageWeb.clickSeeOrderList();

        step("USer copies the Order ID");
        String ID = rescheduleListPageWeb.getOrderID();

        step("The user logs out from the traveler dashboard.");

        step("User log in as Approver.");

        step("Login via cookie injection and open corporate dashboard");
        homePage.loginViaCookieAndOpenCorporateDashboard(approverMail, password);

        step("User click on skip for now button for passKey.");
        loginB2CInDWEB.clickSkipForNowButton();

        step("User Navigates to the Purchase List");
        purchaseListPage.goToPurchaseListPage();

        step("Approver approves the the Request on purchase list page.");
        purchaseListPage.approvePurchase(ID);

        step("Login again as traveler via cookie injection");
        homePage.loginViaCookie(email, password);

        step("User click on skip for now button for passKey.");
        loginB2CInDWEB.clickSkipForNowButton();

        step("User navigate to the Your Orders Page.");
        rescheduleListPageWeb.goToOrdersPage();

        step("Traveller Reschedules the booking.");
        rescheduleListPageWeb.rescheduleBooking();

        step("Traveller check the Availability and proceed to reschedule.");
        rescheduleListPageWeb.checkAvailability();

        step("Traveller confirms the reschedule.");
        rescheduleListPageWeb.makeRescheduleRequest();

        step("Traveller completes the Payment for reschedule.");
        rescheduleListPageWeb.payWithRefundDeposit();

        step("User log in as Approver.");

        step("Login via cookie injection and open corporate dashboard");
        homePage.loginViaCookieAndOpenCorporateDashboard(approverMail, password);

        step("User click on skip for now button for passKey.");
        loginB2CInDWEB.clickSkipForNowButton();

        step("User Navigates to the Reschedule List");
        corporateExternalDashboardSidebar.goToRescheduleList();

        step("User Selects the Reschedule Request.");
        rescheduleListPageWeb.goToRescheduleRequest();

        step("Verify that the approval notes are displayed on the Reschedule History page.");
        Assertion.assertThat("Approval notes are displayed", rescheduleListPageWeb.isApprovalNotesVisible(notes), is(true));
    }

    @DataProvider(name = "testApprovalNotesDisplayed_DataProvider")
    public Object[][] testApprovalNotesDisplayed_DataProvider() {
        return getObjectParams("testApprovalNotesDisplayed");
    }

    @Author(names = {"Harshit Ranjan"})
    @Module(name = "Travel plan")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test(dataProvider = "verifyUserCanEditTravelPlan_DataProvider")
    public void verifyUserCanEditTravelPlan(Map<String, String> param) throws Exception {
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePage<?> homePage = new HomePage<>(session);
        LoginB2CInDWEB<?> loginB2CInDWEB = new LoginB2CInDWEB<>(session);
        CorporateExternalDashboardSidebar<?> CorporateExternalDashboardSidebar = new CorporateExternalDashboardSidebar<>(session);
        TravelPlan<?> travelPlan = new TravelPlan<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        String product = param.get("product");
        String name = CoreFunctions.randomName() + "Test Plan";
        String arrival = param.get("arrival");
        String departure = param.get("departure");
        String dateRange = param.get("dateRange");
        String newName = CoreFunctions.randomName() + "Edited Plan";
        String newProduct = param.get("newProduct");
        String newArrival = param.get("newArrival");
        String newDeparture = param.get("newDeparture");

        step("Login via cookie injection and open corporate dashboard");
        homePage.loginViaCookieAndOpenCorporateDashboard(email, password);

        boolean isUserLoggedIn = loginB2CInDWEB.checkIfLoggedIn();

        step("Check that user is logged in");
        Assertion.assertThat("Is user logged in", isUserLoggedIn, is(true));

        step("Open the Travel Plan");
        CorporateExternalDashboardSidebar.openTravelPlanPage();

        step("Go to Create Travel Plan");

        travelPlan.clickCreateTravel();
        Assertion.assertThat("Verify if Create Travel Plan page is opened", travelPlan.isCreateTravelPageOpened(), is(true));

        step("Create Travel Plan");
        travelPlan.CreateTravelPlan(name, dateRange, product, arrival, departure);
        travelPlan.selectApproverAndSubmit();

        step("Travel Plan Request is Submitted");
        Assertion.assertThat("Verify if Travel Plan is created Successfully", travelPlan.isTravelPlanCreated(name), is(true));


        step("Edit The Travel Plan");
        travelPlan.editTravelPlan(newName, newProduct, newArrival, newDeparture);

        Assertion.assertThat("Travel Plan name has changed", travelPlan.isTravelPlanNameExist(newName), is(true));
        Assertion.assertThat("Product Type has changed", newProduct, containsString(travelPlan.getProductType()));

        step("Delete The Travel Plan");
        travelPlan.cancelTravelPlan();
        Assertion.assertThat("Verify if Travel Plan is deleted Successfully", travelPlan.isTravelPlanCreated(newName), is(false));

        step("Log out From Admin dashboard");
        homePage.logoutFromAdminDashboard();
    }

    @DataProvider(name = "verifyUserCanEditTravelPlan_DataProvider")
    public Object[][] verifyUserCanEditTravelPlan_DataProvider() {
        return getObjectParams("verifyUserCanEditTravelPlan");
    }

    @Author(names = {"Harshit"})
    @Module(name = "Booking List")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test(dataProvider = "SuperAdminDownloadBookingListDataProvider")
    public void SuperAdminDownloadBookingList(Map<String, String> param) throws Exception {
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        BookingListPage<?> bookingListPage = new BookingListPage<>(session);
        LoginB2CInDWEB<?> loginB2CInDWEB = new LoginB2CInDWEB<>(session);
        HomePage<?> homePage = new HomePage<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");

        step("Login via cookie injection and open corporate dashboard");
        homePage.loginViaCookieAndOpenCorporateDashboard(email, password);

        step("Check if user has successfully logged in");
        boolean isUserLoggedIn = loginB2CInDWEB.checkIfLoggedIn();

        step("Close tutorial banner if exists");
        loginB2CInDWEB.closeTutorialbanner();

        step("Go to Booking List Page");
        bookingListPage.goToBookingListPage();

        step("Click download csv");
        Assertion.assertThat("Is downloading booking list", bookingListPage.downloadCSV(), is(true));

        step("Log out From Admin dashboard");
        homePage.logoutFromAdminDashboard();
    }

    @DataProvider(name = "SuperAdminDownloadBookingListDataProvider")
    public Object[][] SuperAdminDownloadBookingListDataProvider() {
        return getObjectParams("SuperAdminDownloadBookingList");
    }

    @Author(names = {"Harshit"})
    @Module(name = "Booking List")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test(dataProvider = "SuperAdminDownloadTransactionListDataProvider")
    public void SuperAdminDownloadTransactionList(Map<String, String> param) throws Exception {
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        CorporateExternalDashboardSidebar<?> corporateExternalDashboardSidebar = new CorporateExternalDashboardSidebar<>(session);
        LoginB2CInDWEB<?> loginB2CInDWEB = new LoginB2CInDWEB<>(session);
        HomePage<?> homePage = new HomePage<>(session);
        TransactionListPage<?> transactionListPage = new TransactionListPage<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");

        step("Login via cookie injection and open corporate dashboard");
        homePage.loginViaCookieAndOpenCorporateDashboard(email, password);

        step("Check if user has successfully logged in");
        boolean isUserLoggedIn = loginB2CInDWEB.checkIfLoggedIn();

        step("Go to Transaction List Page");
        corporateExternalDashboardSidebar.goToTransactionList();

        Assertion.assertThat("Verify Transaction report page is opened", transactionListPage.isTransactionListPageOpened(), is(true));

        step("Download Transaction List");
        transactionListPage.downloadTransactionList();

        Assertion.assertThat("Verify Processing File text is visible after clicking download", transactionListPage.isProcessingFileVisible(), is(true));
        Assertion.assertThat("Verify correct Snack bar  text is present after clicking download", transactionListPage.getTextFromSnackbar(), containsString("Processing the file. Please wait."));

        step("Log out From Admin dashboard");
        homePage.logoutFromAdminDashboard();
    }

    @DataProvider(name = "SuperAdminDownloadTransactionListDataProvider")
    public Object[][] SuperAdminDownloadTransactionListDataProvider() {
        return getObjectParams("SuperAdminDownloadTransactionList");
    }

    @Author(names = {"Harshit"})
    @Module(name = "Corporate Information")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test(dataProvider = "verifyPelitaCodeAdded_DataProvider")
    public void verifyPelitaCodeAdded(Map<String, String> param) throws Exception {
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePage<?> homePage = new HomePage<>(session);
        LoginB2CInDWEB<?> loginB2CInDWEB = new LoginB2CInDWEB<>(session);
        CorporateExternalDashboardSidebar<?> corporateExternalDashboardSidebar = new CorporateExternalDashboardSidebar<>(session);
        CorporateInformationPage<?> corporateInformationPage = new CorporateInformationPage<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");

        step("Login via cookie injection and open corporate dashboard");
        homePage.loginViaCookieAndOpenCorporateDashboard(email, password);

        step("Check if user has successfully logged in");
        boolean isUserLoggedIn = loginB2CInDWEB.checkIfLoggedIn();

        step("Assert that user is logged in");
        Assertion.assertThat("Is user logged in", isUserLoggedIn, is(true));

        step("Open the Corporate Information page");
        corporateExternalDashboardSidebar.clickCorporateInformation();

        step("Verify Corporate Information page is opened");
        boolean isPageOpen = corporateInformationPage.isCorporateInformationPageOpen();
        Assertion.assertThat("Corporate Information page should be open", isPageOpen, is(true));

        step("Add Pelita code");

        if (corporateInformationPage.isPelitaCodeAdded()) {
            step("Delete Pelita code if already exist");
            corporateInformationPage.deleteCorporateCode();
            String deleteSnackbarText = corporateInformationPage.getSnackBarText();

            Assertion.assertThat("Snackbar should contain code deleted success message", deleteSnackbarText, containsString("You've successfully deleted the code."));
        }

        corporateInformationPage.addCorporateCodePelita();

        step("Verify code is added successfully");
        String addSnackbarText = corporateInformationPage.getSnackBarText();
        Assertion.assertThat("Snackbar should contain code added success message", addSnackbarText, containsString("You've successfully added a new code"));

        step("Log out From Admin dashboard");
        homePage.logoutFromAdminDashboard();
    }

    @DataProvider(name = "verifyPelitaCodeAdded_DataProvider")
    public Object[][] verifyPelitaCodeAdded_DataProvider() {
        return getObjectParams("verifyPelitaCodeAdded");
    }

    @Author(names = {"Manan Kumar"})
    @Module(name = "Cost Center")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test(dataProvider = "verifyAdminCanCreateCostCenter_DataProvider")
    public void verifyAdminCanCreateCostCenter(Map<String, String> param) throws Exception {
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePage<?> homePage = new HomePage<>(session);
        LoginB2CInDWEB<?> loginB2CInDWEB = new LoginB2CInDWEB<>(session);
        CorporateExternalDashboardSidebar<?> corporateExternalDashboardSidebar = new CorporateExternalDashboardSidebar<>(session);
        CostCenterPage<?> costCenterPage = new CostCenterPage<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        String costCenterName = "Manan" + CoreFunctions.randomName();
        String costCenterId = "Manan" + CoreFunctions.randomInt(4);
        String description = "Test " + CoreFunctions.randomName();

        step("Login via cookie injection and open corporate dashboard");
        homePage.loginViaCookieAndOpenCorporateDashboard(email, password);

        step("Check that user is logged in");
        boolean isUserLoggedIn = loginB2CInDWEB.checkIfLoggedIn();
        Assertion.assertThat("Is user logged in", isUserLoggedIn, is(true));

        step("Go to Settings");
        corporateExternalDashboardSidebar.clickOnSettings();

        step("Go to Cost Center");
        corporateExternalDashboardSidebar.goToCostCenter();

        step("Verify Cost Center page is opened");
        Assertion.assertThat("Cost Center page should be open", costCenterPage.isCostCenterPageOpen(), is(true));

        step("Click Edit");
        costCenterPage.clickEditButton();

        step("Click Add new cost center");
        costCenterPage.clickAddNewCostCenterButton();

        step("Fill cost center ID, name and description");
        costCenterPage.fillCostCenterDetails(costCenterId, costCenterName, description);

        step("Hit Save");
        costCenterPage.clickSaveButton();

        step("Click on Save button from POP window");
        costCenterPage.clickSaveButtonFromPopup();

        step("Verify cost center created successfully");
        Assertion.assertThat("Cost center should be created successfully", costCenterPage.isSuccessSnackbarDisplayed(), is(true));
        Assertion.assertThat("Verify success snackbar text", costCenterPage.getSuccessSnackbarText(), containsString("successfully saved the Cost Center settings"));

        step("Verify created cost center data is present in the table");
        Assertion.assertThat("Created cost center should be visible in table with correct ID, name and description", costCenterPage.isCostCenterCreatedInTable(costCenterId, costCenterName, description, true), is(true));

        step("Click Edit to delete the created cost center");
        costCenterPage.clickEditButton();

        step("Delete the created cost center by its ID");
        costCenterPage.clickDeleteButtonByCostCenterId(costCenterId);

        step("Save after deletion");
        costCenterPage.clickSaveButton();

        step("Click on Save button from POP window");
        costCenterPage.clickSaveButtonFromPopup();

        step("Verify deleted cost center is no longer in the table");
        Assertion.assertThat("Deleted cost center should be absent from table", costCenterPage.isCostCenterCreatedInTable(costCenterId, costCenterName, description, false), is(false));

        step("Log out From Admin dashboard");
        homePage.logoutFromAdminDashboard();
    }

    @DataProvider(name = "verifyAdminCanCreateCostCenter_DataProvider")
    public Object[][] verifyAdminCanCreateCostCenter_DataProvider() {
        return getObjectParams("verifyAdminCanCreateCostCenter");
    }

    @Author(names = {"Manan"})
    @Module(name = "E-Invoice")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test(dataProvider = "verifyPaymentPageOpenedOnPayInvoiceDataProvider")
    public void verifyPaymentPageOpenedOnPayInvoice(Map<String, String> param) throws Exception {
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePage<?> homePage = new HomePage<>(session);
        LoginB2CInDWEB<?> loginB2CInDWEB = new LoginB2CInDWEB<>(session);
        CorporateExternalDashboardSidebar<?> corporateExternalDashboardSidebar = new CorporateExternalDashboardSidebar<>(session);
        InvoiceAndBillingPage<?> invoiceAndBillingPage = new InvoiceAndBillingPage<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");

        step("Login via cookie injection and open corporate dashboard");
        homePage.loginViaCookieAndOpenCorporateDashboard(email, password);

        step("Verify user is logged in to the corporate dashboard");
        boolean isUserLoggedIn = loginB2CInDWEB.checkIfLoggedIn();
        Assertion.assertThat("User should be logged in to the corporate dashboard", isUserLoggedIn, is(true));

        step("Go to Invoice and Billing from sidebar");
        corporateExternalDashboardSidebar.goToInvoiceAndBilling();

        step("Verify Invoice and Billing page is opened");
        Assertion.assertThat("Invoice and Billing page should be opened", invoiceAndBillingPage.isInvoiceAndBillingPageOpened(), is(true));

        step("Click on View Invoice from active list");
        invoiceAndBillingPage.clickViewInvoice();

        step("Store the invoice ID displayed on the invoice detail page");
        String invoiceId = invoiceAndBillingPage.getInvoiceId();

        step("Store the total amount due from the invoice detail page");
        String totalAmountDue = invoiceAndBillingPage.getTotalAmountDue();

        step("Click on Pay Invoice button on top right corner");
        invoiceAndBillingPage.clickPayInvoice();

        step("Verify the payment page corresponds to the correct invoice");
        Assertion.assertThat("Payment page should display the correct invoice ID", invoiceAndBillingPage.isInvoiceIdPresentOnPaymentPage(invoiceId), is(true));

        step("Verify Order ID is present on payment page");
        Assertion.assertThat("Order ID should be displayed on the payment page", invoiceAndBillingPage.isOrderIdPresentOnPaymentPage(), is(true));

        step("Verify amount displayed on payment page matches total amount due from invoice");
        Assertion.assertThat("Payment page should display the same total amount as the invoice", invoiceAndBillingPage.isSameAmountDisplayedOnPaymentPage(totalAmountDue), is(true));

        step("Go back to admin dashboard and Log out");
        homePage.goToCorporateDashboard();
        homePage.logoutFromAdminDashboard();
    }

    @DataProvider(name = "verifyPaymentPageOpenedOnPayInvoiceDataProvider")
    public Object[][] verifyPaymentPageOpenedOnPayInvoiceDataProvider() {
        return getObjectParams("verifyPaymentPageOpenedOnPayInvoice");
    }

    @Author(names = {"Abhishek Sharma"})
    @Module(name = "MICE")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void verifyUserCanAcceptQuotationB2B() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("verifyUserCanAcceptQuotationB2B");
        BaseTest.paramsThreadLocal.set(param);
        Session<WebDriver> session = sessionThreadLocal.get();

        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        CorporateInternalDashboardSidebar<?> corporateInternalDashboardSidebar = new CorporateInternalDashboardSidebar<>(session);
        TiketPlusForCorporateInternal<?> tiketPlusForCorporateInternal = new TiketPlusForCorporateInternal<>(session);
        TiketPlusForCorporateExternal<?> tiketPlusForCorporateExternal = new TiketPlusForCorporateExternal<>(session);
        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<>(session);
        CorporateDataPage<?> corporateDataPage = new CorporateDataPage<>(session);

        String username = param.get("username");
        String password = param.get("password");

        String internalEmail = param.get("adminEmail");
        String internalPassword = param.get("adminPassword");
        String corporateClientName = param.get("corporateClientName");
        String picEmployeeName = param.get("picEmployeeName");

        String basicInformationTab = param.get("basicInformationTab");
        String quotationDetailsTab = param.get("quotationDetailsTab");
        String vendorPaymentTab = param.get("vendorPaymentTab");
        String reviewAndSubmitTab = param.get("reviewAndSubmitTab");
        String inventorySource = param.get("inventorySource");
        String bankName = param.get("bankName");
        String bankAccountNumber = param.get("bankAccountNumber");
        String itemName = generateRandomString(5) + " Item Name";
        String itemDescription = generateRandomString(5) + " Item Description";
        String itemAmount = param.get("itemAmount");
        String itemQuantity = param.get("itemQuantity");
        int dateOffset = Integer.parseInt(param.get("dateOffset"));

        step("Login via cookie injection to Internal Dashboard");
        loginMemberAdmin.loginViaCookie(internalEmail, internalPassword);

        step("Go to Corporate Internal Dashboard");
        loginMemberAdmin.goToDashboard();

        step("Check if user is logged in");
        boolean isLoggedInCRP = corporateDataPage.checkUserIsLoggedIn();

        step("Assert that user is logged in to corporate dashboard");
        Assertion.assertThat("User is logged in", isLoggedInCRP, is(true));

        step("Expand tiketPlus for Corporate");
        corporateInternalDashboardSidebar.expandTiketPlusForCorporate();

        step("Click the Quotation List");
        corporateInternalDashboardSidebar.openQuotationList();

        step("Check if page is opened successfully");
        Assertion.assertThat("Quotation List page is opened", tiketPlusForCorporateInternal.isQuotationListPageOpened(), is(true));

        step("Click New Quotation button");
        tiketPlusForCorporateInternal.clickNewQuotationButton();
        Assertion.assertThat("Create New Quotation page opened", tiketPlusForCorporateInternal.isCreateNewQuotationPageOpened(), is(true));
        Assertion.assertThat("Verify Current tab name: " + basicInformationTab, tiketPlusForCorporateInternal.getNewQuotationCurrentTabName(), containsString(basicInformationTab));

        step("Select Corporate client name: " + corporateClientName);
        tiketPlusForCorporateInternal.selectCorporateClientName(corporateClientName);
        Assertion.assertThat("Verify Corporate client name appears in input", tiketPlusForCorporateInternal.getCorporateClientName(), containsString(corporateClientName));

        step("Select PIC Employee name: " + picEmployeeName);
        tiketPlusForCorporateInternal.selectPicEmployeeName(picEmployeeName);
        Assertion.assertThat("Verify PIC Employee name appears in input", tiketPlusForCorporateInternal.getPicEmployeeName(), containsString(picEmployeeName));

        step("Click Next Quotation Details Button");
        tiketPlusForCorporateInternal.clickNextQuotationDetailsButton();

        step("Verify Current tab name: " + quotationDetailsTab + " and other details");
        Assertion.assertThat("Verify Current tab name: " + quotationDetailsTab, tiketPlusForCorporateInternal.getNewQuotationCurrentTabName(), containsString(quotationDetailsTab));

        step("Set Item name: " + itemName);
        tiketPlusForCorporateInternal.setQuotationDetailsItemName(itemName);

        step("Set Item description: " + itemDescription);
        tiketPlusForCorporateInternal.setQuotationDetailsItemDescription(itemDescription);

        step("Set Item Amount: " + itemAmount);
        tiketPlusForCorporateInternal.setQuotationDetailsItemAmount(itemAmount);

        step("Set Item Quantity: " + itemQuantity);
        tiketPlusForCorporateInternal.setQuotationDetailsItemQuantity(itemQuantity);

        step("Set Start Date");
        String paymentDueDate = tiketPlusForCorporateInternal.getPaymentDueDate();
        String startDate = tiketPlusForCorporateInternal.setStartDate(paymentDueDate, dateOffset);

        step("Set End Date");
        tiketPlusForCorporateInternal.setEndDate(startDate, dateOffset);

        step("Get Data From tab: " + quotationDetailsTab);
        String quotationNumber = tiketPlusForCorporateInternal.getQuotationDetailsQuotationNumber();
        String tax = tiketPlusForCorporateInternal.getQuotationDetailsItemTax();
        String totalAmount = String.valueOf(Integer.parseInt(itemAmount) + Integer.parseInt(tax));

        step("Click Next Vendor Payment Button");
        tiketPlusForCorporateInternal.clickNextVendorPaymentButton();

        step("Verify Current tab name: " + vendorPaymentTab + " and other details");
        Assertion.assertThat("Verify Current tab name: " + vendorPaymentTab, tiketPlusForCorporateInternal.getNewQuotationCurrentTabName(), containsString(vendorPaymentTab));

        step("Choose " + inventorySource + " as Inventory Source");
        tiketPlusForCorporateInternal.selectVendorPaymentInventorySource(inventorySource);
        Assertion.assertThat("Verify Inventory source: " + inventorySource + " Button selected", tiketPlusForCorporateInternal.getVendorPaymentInventorySource(inventorySource), is(true));

        step("Select Currency");
        tiketPlusForCorporateInternal.selectVendorPaymentCurrency();

        step("Select Product Category");
        tiketPlusForCorporateInternal.selectVendorPaymentProductCategory();

        step("Select Regional Hotel");
        tiketPlusForCorporateInternal.selectVendorPaymentRegionalHotel();

        step("Select Revenue Category");
        tiketPlusForCorporateInternal.selectVendorPaymentRevenueCategory();

        step("Select Payment Due Date");
        tiketPlusForCorporateInternal.selectVendorPaymentPaymentDueDate(paymentDueDate);

        step("Select Vendor Name");
        tiketPlusForCorporateInternal.selectVendorPaymentVendorName();

        step("Select Product Provider");
        tiketPlusForCorporateInternal.selectVendorPaymentProductProvider();

        step("Select Payment Type");
        tiketPlusForCorporateInternal.selectVendorPaymentPaymentType();

        step("Select Bank Name: " + bankName);
        tiketPlusForCorporateInternal.selectVendorPaymentVendorBankName(bankName);

        step("Select and verify Bank Account Number: " + bankAccountNumber);
        tiketPlusForCorporateInternal.setVendorPaymentBankAccountNumber(bankAccountNumber);
        tiketPlusForCorporateInternal.clickVendorPaymentCheckAccount();
        Assertion.assertThat("Verify account", tiketPlusForCorporateInternal.getVendorPaymentAccountVerifyText(), containsStringIgnoringCase("account verified successfully"));

        step("Click on Next Review & Submit button");
        tiketPlusForCorporateInternal.clickNextReviewAndSubmitButton();

        step("Verify Current tab name: " + reviewAndSubmitTab + " and other details");
        Assertion.assertThat("Verify Current tab name: " + reviewAndSubmitTab, tiketPlusForCorporateInternal.getNewQuotationCurrentTabName(), containsString(reviewAndSubmitTab));

        step("Verify Data on tab: " + reviewAndSubmitTab);
        Assertion.assertThat("Verify Corporate client name", tiketPlusForCorporateInternal.getReviewAndSubmitDetailByHeadingAndName("Corporate Client", "Corporate Name"), containsString(corporateClientName));
        Assertion.assertThat("Verify Agent PIC email", tiketPlusForCorporateInternal.getReviewAndSubmitDetailByHeadingAndName("Assign Agent PIC", "Email Address"), containsString(internalEmail));
        Assertion.assertThat("Verify Quotation Number", tiketPlusForCorporateInternal.getReviewAndSubmitDetailByHeadingAndName("Quotation Details", "Quotation Number"), containsString(quotationNumber));
        Assertion.assertThat("Verify Quotation Date", tiketPlusForCorporateInternal.getReviewAndSubmitDetailByHeadingAndName("Quotation Details", "Quotation Date"), containsString(tiketPlusForCorporateInternal.getTodayDateFormatted()));

        Assertion.assertThat("Verify Item name", tiketPlusForCorporateInternal.getReviewAndSubmitTableDetailByHeading("Quotation Overview", "Item Name", false), containsString(itemName));
        Assertion.assertThat("Verify Item description", tiketPlusForCorporateInternal.getReviewAndSubmitTableDetailByHeading("Quotation Overview", "Description", false), containsString(itemDescription));
        Assertion.assertThat("Verify Item Net amount", tiketPlusForCorporateInternal.getReviewAndSubmitTableDetailByHeading("Quotation Overview", "Net Amount", true), containsString(itemAmount));
        Assertion.assertThat("Verify Item Tax", tiketPlusForCorporateInternal.getReviewAndSubmitTableDetailByHeading("Quotation Overview", "Tax", true), containsString(tax));
        Assertion.assertThat("Verify Item Quantity", tiketPlusForCorporateInternal.getReviewAndSubmitTableDetailByHeading("Quotation Overview", "Qty", true), containsString(itemQuantity));
        Assertion.assertThat("Verify Item Total Amount", tiketPlusForCorporateInternal.getReviewAndSubmitTableDetailByHeading("Quotation Overview", "Amount", true), containsString(totalAmount));
        Assertion.assertThat("Verify Item Total Amount Due", tiketPlusForCorporateInternal.getReviewAndSubmitTotalAmountDue(), containsString(totalAmount));

        Assertion.assertThat("Verify Payment Information Item name", tiketPlusForCorporateInternal.getReviewAndSubmitTableDetailByHeading("Vendor Payment Information", "Item Name", false), containsString(itemName));
        Assertion.assertThat("Verify Payment Information Bank Account", tiketPlusForCorporateInternal.getReviewAndSubmitTableDetailByHeading("Vendor Payment Information", "Bank Account", false), containsString(bankAccountNumber));

        step("Click on Submit to lead Button");
        tiketPlusForCorporateInternal.clickSubmitToLeadButton();

        step("Verify and submit confirmation popup details");
        Assertion.assertThat("Verify popup appears", tiketPlusForCorporateInternal.isModalLoaded(), is(true));
        Assertion.assertThat("Verify popup heading", tiketPlusForCorporateInternal.getModalHeading(), containsString("Submit this quotation?"));
        Assertion.assertThat("Verify popup description", tiketPlusForCorporateInternal.getModalDescription(), containsString("Once submitted, this quotation will be sent to the corporate client."));
        tiketPlusForCorporateInternal.clickModalSubmitButton();

        step("Now logout and do External User login");
        loginMemberAdmin.logout();
        loginPage.loginWithSpecificAccountViaCookie(username, password);
        loginPage.navigateToExternalCorporateDashboard();

        step("Expand tiketPlus for Corporate");
        corporateInternalDashboardSidebar.expandTiketPlusForCorporate();

        step("Click the Quotation List");
        corporateInternalDashboardSidebar.openQuotationList();

        step("Check if page is opened successfully");
        Assertion.assertThat("Quotation List page is opened", tiketPlusForCorporateExternal.isQuotationListPageOpened(), is(true));

        step("Search Quotation Number: " + quotationNumber);
        tiketPlusForCorporateExternal.searchQuotationNumber(quotationNumber);
        Assertion.assertThat("Verify Quotation Number", tiketPlusForCorporateExternal.getTableDetailByHeading("Quotation Number", false), containsString(quotationNumber));
        Assertion.assertThat("Verify Quotation Status: Active", tiketPlusForCorporateExternal.getTableDetailByHeading("Status", false), containsString("Active"));

        step("Click More Actions for Quotation");
        tiketPlusForCorporateExternal.clickTableDetailMoreActionsButton();

        step("Click See Quotation Button");
        tiketPlusForCorporateExternal.clickQuotationActionButton("See quotation");

        step("Verify Quotation Details");
        Assertion.assertThat("Verify Quotation Page Loaded", tiketPlusForCorporateExternal.isQuotationDetailsPageLoaded(quotationNumber), is(true));
        Assertion.assertThat("Verify Quotation Number", tiketPlusForCorporateExternal.getQuotationPageQuotationNumber(), containsString(quotationNumber));
        Assertion.assertThat("Verify Quotation Status", tiketPlusForCorporateExternal.getQuotationPageQuotationActiveStatus(), containsString("Active"));
        Assertion.assertThat("Verify Quotation Company Name", tiketPlusForCorporateExternal.getQuotationPageCompanyName(), containsString(corporateClientName));
        Assertion.assertThat("Verify Quotation Email Address", tiketPlusForCorporateExternal.getQuotationPageEmailAddress(), containsString(username));
        Assertion.assertThat("Verify Item Name", tiketPlusForCorporateExternal.getTableDetailByHeading("Item Name", false), containsString(itemName));
        Assertion.assertThat("Verify Item Description", tiketPlusForCorporateExternal.getTableDetailByHeading("Description", false), containsString(itemDescription));
        Assertion.assertThat("Verify Item Amount", tiketPlusForCorporateExternal.getTableDetailByHeading("Amount", true), containsString(itemAmount));
        Assertion.assertThat("Verify Total Amount", tiketPlusForCorporateExternal.getQuotationDetailTotalAmount(), containsString(totalAmount));
        Assertion.assertThat("Verify Quotation Number in Activity Log", tiketPlusForCorporateExternal.getQuotationDetailActivityLogText(), containsString(quotationNumber));

        step("Click Accept Quotation");
        tiketPlusForCorporateExternal.clickAcceptQuotation();

        step("Verify Modal Details");
        Assertion.assertThat("Verify Modal Loaded", tiketPlusForCorporateExternal.isModalLoaded(), is(true));
        Assertion.assertThat("Verify Modal Heading", tiketPlusForCorporateExternal.getModalHeading(), containsString("Accept Quotation"));
        tiketPlusForCorporateExternal.selectPaymentMethod("corporate_balance");
        Assertion.assertThat("Verify Selected Payment Method", tiketPlusForCorporateExternal.isPaymentMethod("corporate_balance"), is(true));

        step("Click Accept on modal");
        tiketPlusForCorporateExternal.clickAcceptQuotation();

        step("Verify Modal Details");
        Assertion.assertThat("Verify Modal Loaded", tiketPlusForCorporateExternal.isModalLoaded(), is(true));
        Assertion.assertThat("Verify Modal Heading", tiketPlusForCorporateExternal.getModalHeading(), containsString("Quotation Accepted Successfully"));
        Assertion.assertThat("Verify Modal Description contains Quotation Number", tiketPlusForCorporateExternal.getModalDescription(), containsString(quotationNumber));
        Assertion.assertThat("Verify Modal Order Id present", tiketPlusForCorporateExternal.getModalOrderId().length(), greaterThan(0));

        step("Click Modal Close Button");
        tiketPlusForCorporateExternal.clickModalCloseButton();

        step("Verify Quotation Status");
        Assertion.assertThat("Verify Quotation Status: Accepted", tiketPlusForCorporateExternal.getQuotationPageQuotationAcceptedStatus(), containsString("Accepted"));
    }

    @Author(names = {"Abhishek Sharma"})
    @Module(name = "MICE")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void verifyUserCanRejectQuotationB2B() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("verifyUserCanRejectQuotationB2B");
        BaseTest.paramsThreadLocal.set(param);
        Session<WebDriver> session = sessionThreadLocal.get();

        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        CorporateInternalDashboardSidebar<?> corporateInternalDashboardSidebar = new CorporateInternalDashboardSidebar<>(session);
        TiketPlusForCorporateInternal<?> tiketPlusForCorporateInternal = new TiketPlusForCorporateInternal<>(session);
        TiketPlusForCorporateExternal<?> tiketPlusForCorporateExternal = new TiketPlusForCorporateExternal<>(session);
        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<>(session);
        CorporateDataPage<?> corporateDataPage = new CorporateDataPage<>(session);

        String username = param.get("username");
        String password = param.get("password");

        String internalEmail = param.get("adminEmail");
        String internalPassword = param.get("adminPassword");
        String corporateClientName = param.get("corporateClientName");
        String picEmployeeName = param.get("picEmployeeName");

        String basicInformationTab = param.get("basicInformationTab");
        String quotationDetailsTab = param.get("quotationDetailsTab");
        String vendorPaymentTab = param.get("vendorPaymentTab");
        String reviewAndSubmitTab = param.get("reviewAndSubmitTab");
        String inventorySource = param.get("inventorySource");
        String bankName = param.get("bankName");
        String bankAccountNumber = param.get("bankAccountNumber");
        String itemName = generateRandomString(5) + " Item Name";
        String itemDescription = generateRandomString(5) + " Item Description";
        String itemAmount = param.get("itemAmount");
        String itemQuantity = param.get("itemQuantity");
        int dateOffset = Integer.parseInt(param.get("dateOffset"));

        step("Login via cookie injection to Internal Dashboard");
        loginMemberAdmin.loginViaCookie(internalEmail, internalPassword);

        step("Go to Corporate Internal Dashboard");
        loginMemberAdmin.goToDashboard();

        step("Check if user is logged in");
        boolean isLoggedInCRP = corporateDataPage.checkUserIsLoggedIn();

        step("Assert that user is logged in to corporate dashboard");
        Assertion.assertThat("User is logged in", isLoggedInCRP, is(true));

        step("Expand tiketPlus for Corporate");
        corporateInternalDashboardSidebar.expandTiketPlusForCorporate();

        step("Click the Quotation List");
        corporateInternalDashboardSidebar.openQuotationList();

        step("Check if page is opened successfully");
        Assertion.assertThat("Quotation List page is opened", tiketPlusForCorporateInternal.isQuotationListPageOpened(), is(true));

        step("Click New Quotation button");
        tiketPlusForCorporateInternal.clickNewQuotationButton();
        Assertion.assertThat("Create New Quotation page opened", tiketPlusForCorporateInternal.isCreateNewQuotationPageOpened(), is(true));
        Assertion.assertThat("Verify Current tab name: " + basicInformationTab, tiketPlusForCorporateInternal.getNewQuotationCurrentTabName(), containsString(basicInformationTab));

        step("Select Corporate client name: " + corporateClientName);
        tiketPlusForCorporateInternal.selectCorporateClientName(corporateClientName);
        Assertion.assertThat("Verify Corporate client name appears in input", tiketPlusForCorporateInternal.getCorporateClientName(), containsString(corporateClientName));

        step("Select PIC Employee name: " + picEmployeeName);
        tiketPlusForCorporateInternal.selectPicEmployeeName(picEmployeeName);
        Assertion.assertThat("Verify PIC Employee name appears in input", tiketPlusForCorporateInternal.getPicEmployeeName(), containsString(picEmployeeName));

        step("Click Next Quotation Details Button");
        tiketPlusForCorporateInternal.clickNextQuotationDetailsButton();

        step("Verify Current tab name: " + quotationDetailsTab + " and other details");
        Assertion.assertThat("Verify Current tab name: " + quotationDetailsTab, tiketPlusForCorporateInternal.getNewQuotationCurrentTabName(), containsString(quotationDetailsTab));

        step("Set Item name: " + itemName);
        tiketPlusForCorporateInternal.setQuotationDetailsItemName(itemName);

        step("Set Item description: " + itemDescription);
        tiketPlusForCorporateInternal.setQuotationDetailsItemDescription(itemDescription);

        step("Set Item Amount: " + itemAmount);
        tiketPlusForCorporateInternal.setQuotationDetailsItemAmount(itemAmount);

        step("Set Item Quantity: " + itemQuantity);
        tiketPlusForCorporateInternal.setQuotationDetailsItemQuantity(itemQuantity);

        step("Set Start Date");
        String paymentDueDate = tiketPlusForCorporateInternal.getPaymentDueDate();
        String startDate = tiketPlusForCorporateInternal.setStartDate(paymentDueDate, dateOffset);

        step("Set End Date");
        tiketPlusForCorporateInternal.setEndDate(startDate, dateOffset);

        step("Get Data From tab: " + quotationDetailsTab);
        String quotationNumber = tiketPlusForCorporateInternal.getQuotationDetailsQuotationNumber();
        String tax = tiketPlusForCorporateInternal.getQuotationDetailsItemTax();
        String totalAmount = String.valueOf(Integer.parseInt(itemAmount) + Integer.parseInt(tax));

        step("Click Next Vendor Payment Button");
        tiketPlusForCorporateInternal.clickNextVendorPaymentButton();

        step("Verify Current tab name: " + vendorPaymentTab + " and other details");
        Assertion.assertThat("Verify Current tab name: " + vendorPaymentTab, tiketPlusForCorporateInternal.getNewQuotationCurrentTabName(), containsString(vendorPaymentTab));

        step("Choose " + inventorySource + " as Inventory Source");
        tiketPlusForCorporateInternal.selectVendorPaymentInventorySource(inventorySource);
        Assertion.assertThat("Verify Inventory source: " + inventorySource + " Button selected", tiketPlusForCorporateInternal.getVendorPaymentInventorySource(inventorySource), is(true));

        step("Select Currency");
        tiketPlusForCorporateInternal.selectVendorPaymentCurrency();

        step("Select Product Category");
        tiketPlusForCorporateInternal.selectVendorPaymentProductCategory();

        step("Select Regional Hotel");
        tiketPlusForCorporateInternal.selectVendorPaymentRegionalHotel();

        step("Select Revenue Category");
        tiketPlusForCorporateInternal.selectVendorPaymentRevenueCategory();

        step("Select Payment Due Date");
        tiketPlusForCorporateInternal.selectVendorPaymentPaymentDueDate(paymentDueDate);

        step("Select Vendor Name");
        tiketPlusForCorporateInternal.selectVendorPaymentVendorName();

        step("Select Product Provider");
        tiketPlusForCorporateInternal.selectVendorPaymentProductProvider();

        step("Select Payment Type");
        tiketPlusForCorporateInternal.selectVendorPaymentPaymentType();

        step("Select Bank Name: " + bankName);
        tiketPlusForCorporateInternal.selectVendorPaymentVendorBankName(bankName);

        step("Select and verify Bank Account Number: " + bankAccountNumber);
        tiketPlusForCorporateInternal.setVendorPaymentBankAccountNumber(bankAccountNumber);
        tiketPlusForCorporateInternal.clickVendorPaymentCheckAccount();
        Assertion.assertThat("Verify account", tiketPlusForCorporateInternal.getVendorPaymentAccountVerifyText(), containsStringIgnoringCase("account verified successfully"));

        step("Click on Next Review & Submit button");
        tiketPlusForCorporateInternal.clickNextReviewAndSubmitButton();

        step("Verify Current tab name: " + reviewAndSubmitTab + " and other details");
        Assertion.assertThat("Verify Current tab name: " + reviewAndSubmitTab, tiketPlusForCorporateInternal.getNewQuotationCurrentTabName(), containsString(reviewAndSubmitTab));

        step("Verify Data on tab: " + reviewAndSubmitTab);
        Assertion.assertThat("Verify Corporate client name", tiketPlusForCorporateInternal.getReviewAndSubmitDetailByHeadingAndName("Corporate Client", "Corporate Name"), containsString(corporateClientName));
        Assertion.assertThat("Verify Agent PIC email", tiketPlusForCorporateInternal.getReviewAndSubmitDetailByHeadingAndName("Assign Agent PIC", "Email Address"), containsString(internalEmail));
        Assertion.assertThat("Verify Quotation Number", tiketPlusForCorporateInternal.getReviewAndSubmitDetailByHeadingAndName("Quotation Details", "Quotation Number"), containsString(quotationNumber));
        Assertion.assertThat("Verify Quotation Date", tiketPlusForCorporateInternal.getReviewAndSubmitDetailByHeadingAndName("Quotation Details", "Quotation Date"), containsString(tiketPlusForCorporateInternal.getTodayDateFormatted()));

        Assertion.assertThat("Verify Item name", tiketPlusForCorporateInternal.getReviewAndSubmitTableDetailByHeading("Quotation Overview", "Item Name", false), containsString(itemName));
        Assertion.assertThat("Verify Item description", tiketPlusForCorporateInternal.getReviewAndSubmitTableDetailByHeading("Quotation Overview", "Description", false), containsString(itemDescription));
        Assertion.assertThat("Verify Item Net amount", tiketPlusForCorporateInternal.getReviewAndSubmitTableDetailByHeading("Quotation Overview", "Net Amount", true), containsString(itemAmount));
        Assertion.assertThat("Verify Item Tax", tiketPlusForCorporateInternal.getReviewAndSubmitTableDetailByHeading("Quotation Overview", "Tax", true), containsString(tax));
        Assertion.assertThat("Verify Item Quantity", tiketPlusForCorporateInternal.getReviewAndSubmitTableDetailByHeading("Quotation Overview", "Qty", true), containsString(itemQuantity));
        Assertion.assertThat("Verify Item Total Amount", tiketPlusForCorporateInternal.getReviewAndSubmitTableDetailByHeading("Quotation Overview", "Amount", true), containsString(totalAmount));
        Assertion.assertThat("Verify Item Total Amount Due", tiketPlusForCorporateInternal.getReviewAndSubmitTotalAmountDue(), containsString(totalAmount));

        Assertion.assertThat("Verify Payment Information Item name", tiketPlusForCorporateInternal.getReviewAndSubmitTableDetailByHeading("Vendor Payment Information", "Item Name", false), containsString(itemName));
        Assertion.assertThat("Verify Payment Information Bank Account", tiketPlusForCorporateInternal.getReviewAndSubmitTableDetailByHeading("Vendor Payment Information", "Bank Account", false), containsString(bankAccountNumber));

        step("Click on Submit to lead Button");
        tiketPlusForCorporateInternal.clickSubmitToLeadButton();

        step("Verify and submit confirmation popup details");
        Assertion.assertThat("Verify popup appears", tiketPlusForCorporateInternal.isModalLoaded(), is(true));
        Assertion.assertThat("Verify popup heading", tiketPlusForCorporateInternal.getModalHeading(), containsString("Submit this quotation?"));
        Assertion.assertThat("Verify popup description", tiketPlusForCorporateInternal.getModalDescription(), containsString("Once submitted, this quotation will be sent to the corporate client."));
        tiketPlusForCorporateInternal.clickModalSubmitButton();

        step("Now logout and do External User login");
        loginMemberAdmin.logout();
        loginPage.loginWithSpecificAccountViaCookie(username, password);
        loginPage.navigateToExternalCorporateDashboard();

        step("Expand tiketPlus for Corporate");
        corporateInternalDashboardSidebar.expandTiketPlusForCorporate();

        step("Click the Quotation List");
        corporateInternalDashboardSidebar.openQuotationList();

        step("Check if page is opened successfully");
        Assertion.assertThat("Quotation List page is opened", tiketPlusForCorporateExternal.isQuotationListPageOpened(), is(true));

        step("Search Quotation Number: " + quotationNumber);
        tiketPlusForCorporateExternal.searchQuotationNumber(quotationNumber);
        Assertion.assertThat("Verify Quotation Number", tiketPlusForCorporateExternal.getTableDetailByHeading("Quotation Number", false), containsString(quotationNumber));
        Assertion.assertThat("Verify Quotation Status: Active", tiketPlusForCorporateExternal.getTableDetailByHeading("Status", false), containsString("Active"));

        step("Click More Actions for Quotation");
        tiketPlusForCorporateExternal.clickTableDetailMoreActionsButton();

        step("Click See Quotation Button");
        tiketPlusForCorporateExternal.clickQuotationActionButton("See quotation");

        step("Verify Quotation Details");
        Assertion.assertThat("Verify Quotation Page Loaded", tiketPlusForCorporateExternal.isQuotationDetailsPageLoaded(quotationNumber), is(true));
        Assertion.assertThat("Verify Quotation Number", tiketPlusForCorporateExternal.getQuotationPageQuotationNumber(), containsString(quotationNumber));
        Assertion.assertThat("Verify Quotation Status", tiketPlusForCorporateExternal.getQuotationPageQuotationActiveStatus(), containsString("Active"));
        Assertion.assertThat("Verify Quotation Company Name", tiketPlusForCorporateExternal.getQuotationPageCompanyName(), containsString(corporateClientName));
        Assertion.assertThat("Verify Quotation Email Address", tiketPlusForCorporateExternal.getQuotationPageEmailAddress(), containsString(username));
        Assertion.assertThat("Verify Item Name", tiketPlusForCorporateExternal.getTableDetailByHeading("Item Name", false), containsString(itemName));
        Assertion.assertThat("Verify Item Description", tiketPlusForCorporateExternal.getTableDetailByHeading("Description", false), containsString(itemDescription));
        Assertion.assertThat("Verify Item Amount", tiketPlusForCorporateExternal.getTableDetailByHeading("Amount", true), containsString(itemAmount));
        Assertion.assertThat("Verify Total Amount", tiketPlusForCorporateExternal.getQuotationDetailTotalAmount(), containsString(totalAmount));
        Assertion.assertThat("Verify Quotation Number in Activity Log", tiketPlusForCorporateExternal.getQuotationDetailActivityLogText(), containsString(quotationNumber));

        step("Click Reject Quotation");
        tiketPlusForCorporateExternal.clickRejectQuotation();

        step("Verify Modal Details");
        Assertion.assertThat("Verify Modal Loaded", tiketPlusForCorporateExternal.isModalLoaded(), is(true));
        Assertion.assertThat("Verify Modal Heading", tiketPlusForCorporateExternal.getModalHeading(), containsString("Reject Quotation"));

        step("Fill Random Reason");
        tiketPlusForCorporateExternal.fillRandomReasonRejectQuotation(generateRandomString(10));

        step("Click Save Button");
        tiketPlusForCorporateExternal.clickSaveButtonRejectQuotation();

        step("Verify Quotation Status");
        Assertion.assertThat("Verify Quotation Status: Rejected", tiketPlusForCorporateExternal.getQuotationPageQuotationRejectedStatus(), containsString("Rejected"));
    }

    @Author(names = {"Pankaj Singh Kushwaha"})
    @Module(name = "MICE")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void verifyAdminCanCreateMiceForNewBookingRequest() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("verifyAdminCanCreateMiceForNewBookingRequest");
        BaseTest.paramsThreadLocal.set(param);
        Session<WebDriver> session = sessionThreadLocal.get();

        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        CorporateInternalDashboardSidebar<?> corporateInternalDashboardSidebar = new CorporateInternalDashboardSidebar<>(session);
        TiketPlusForCorporateExternal<?> tiketPlusForCorporateExternal = new TiketPlusForCorporateExternal<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String eventType = param.get("eventType");
        String eventName = param.get("eventName") + " " + randomName();
        String participants = param.get("participateNum");
        String area = param.get("eventArea");
        String description = param.get("eventDesc");
        int startIdx = Integer.parseInt(param.get("startDate"));
        int endIdx = Integer.parseInt(param.get("endDate"));
        String productType = param.get("productType");
        String addReq = param.get("addReq");
        String firstPartName = param.get("firstPartName");
        String secondPartName = param.get("secondPartName");
        String idNumFirst = param.get("idNumFirst");
        String idNumSec = param.get("idNumSec");
        String travelApprovalNum = param.get("travelApprovalNum");
        int firstPartDobDay = Integer.parseInt(param.get("firstPartDOB"));
        int secPartDobDay = Integer.parseInt(param.get("secPartDOB"));
        String requestType = param.get("requestType");
        String requestorName = param.get("requestor");
        String snackbarMsg = param.get("snackbarMsg");

        step("Step 1: Login to External Corporate Dashboard and Navigate to External Corporate Dashboard");
        loginPage.loginWithSpecificAccountViaCookie(username, password);
        loginPage.navigateToExternalCorporateDashboard();

        step("Step 2: Expand tiketPlus for Corporate");
        corporateInternalDashboardSidebar.expandTiketPlusForCorporate();

        step("Step 3: Open the Request List and Click on the New Request Button");
        corporateInternalDashboardSidebar.openRequestList();
        tiketPlusForCorporateExternal.clickNewRequestButton();

        step("Step 4: Verify the New Request Page is Loaded with New Event Request and TakeOver Payment Options");
        tiketPlusForCorporateExternal.verifyNewRequestPageLoaded();

        step("Step 5: Click on the New Event Request Button and Verify New Booking Request Page Opened");
        tiketPlusForCorporateExternal.clickCreateEventRequestButton();
        tiketPlusForCorporateExternal.verifyNewBookingRequestPageOpened();

        step("Step 6: Fill in the Event Details");
        tiketPlusForCorporateExternal.selectEventType(eventType);
        tiketPlusForCorporateExternal.fillEventDetails(eventName, participants, area, description);
        tiketPlusForCorporateExternal.selectDateInNextMonth("event-start-date", startIdx);
        tiketPlusForCorporateExternal.selectDateInNextMonth("event-end-date", endIdx);
        String startDate = tiketPlusForCorporateExternal.getStartDate();
        String endDate = tiketPlusForCorporateExternal.getEndDate();

        step("Step 7: Click 'Next: Event Products' and verify the Event Product section opens");
        tiketPlusForCorporateExternal.clickNextEventProducts();
        tiketPlusForCorporateExternal.verifyEventProductsSectionOpens();

        step("Step 8: Select Product Types and fill Additional Requirements");
        tiketPlusForCorporateExternal.selectProductTypes(productType);
        tiketPlusForCorporateExternal.fillAdditionalRequirements(addReq);

        step("Step 9: Click on the Next Contact And Approval and verify Contact And Participant Section Opens");
        tiketPlusForCorporateExternal.clickNextContactAndApproval();
        tiketPlusForCorporateExternal.verifyContactAndParticipantSectionOpens();

        step("Step 10: Fill details for the first participant.");
        tiketPlusForCorporateExternal.fillSingleParticipant(firstPartName, idNumFirst, firstPartDobDay);
        String firstParticipantDOB = tiketPlusForCorporateExternal.getDOB();

        step("Step 11: Click 'Add Participant' button to save the first person");
        tiketPlusForCorporateExternal.clickAddParticipant();

        step("Step 12: Fill details for the second participant");
        tiketPlusForCorporateExternal.fillSingleParticipant(secondPartName, idNumSec, secPartDobDay);
        String secondParticipantDOB = tiketPlusForCorporateExternal.getDOB();

        step("Step 13: Click 'Add Participant' button to save the second person");
        tiketPlusForCorporateExternal.clickAddParticipant();

        step("Step 14: Fill the Additional Information section");
        tiketPlusForCorporateExternal.fillTravelApprovalNumber(travelApprovalNum);

        step("Step 15: Click 'Next: Review & Submit' and verify the page opens");
        tiketPlusForCorporateExternal.clickNextReviewAndSubmit();
        tiketPlusForCorporateExternal.verifyReviewAndSubmitPageOpens();

        step("Step 16: Verify all details on the summary page match the input data");
        tiketPlusForCorporateExternal.verifySummaryDetails(
                eventName, eventType, participants, area, description,
                productType, addReq,
                firstPartName, idNumFirst, firstParticipantDOB,
                secondPartName, idNumSec, secondParticipantDOB,
                travelApprovalNum
        );

        step("Step 17: Click the final Submit Request button and Verify the confirmation modal appears");
        tiketPlusForCorporateExternal.clickFinalSubmitRequestButton();
        tiketPlusForCorporateExternal.verifySubmitConfirmationModalAppears();

        step("Step 18: Click 'Submit request' to finalize the booking and Verify the Snackbar Message");
        tiketPlusForCorporateExternal.clickSubmitRequestConfirmation();
        tiketPlusForCorporateExternal.verifySnackbarAndWaitUntilDismissed(snackbarMsg);

        step("Step 19: Search for the newly created event in the Dashboard");
        tiketPlusForCorporateExternal.searchForEventInDashboard(eventName);

        step("Step 20: Verify the event details in the Dashboard table match the requested data");
        tiketPlusForCorporateExternal.verifyEventInDashboardTable(eventName, requestType, requestorName, startDate, endDate, area, participants);

        step("Step 21: Cancel the newly created request and verify the confirmation snackbar");
        tiketPlusForCorporateExternal.cancelRequestFromDashboard();
        tiketPlusForCorporateExternal.verifyCancelRequestSnackbar("You’ve canceled the request.");
    }

    @Author(names = {"Pankaj Singh Kushwaha"})
    @Module(name = "MICE")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void verifyAdminCanCreateMiceForTakeoverPaymentRequest() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("verifyAdminCanCreateMiceForTakeoverPaymentRequest");
        BaseTest.paramsThreadLocal.set(param);
        Session<WebDriver> session = sessionThreadLocal.get();

        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        CorporateInternalDashboardSidebar<?> corporateInternalDashboardSidebar = new CorporateInternalDashboardSidebar<>(session);
        TiketPlusForCorporateExternal<?> tiketPlusForCorporateExternal = new TiketPlusForCorporateExternal<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String productType = param.get("productType");
        String takeoverEventName = param.get("eventName")+" "+randomName();
        String participateNum = param.get("participateNum");
        String orderAmt = param.get("orderAmt");
        int startIdx = Integer.parseInt(param.get("startDate"));
        int endIdx = Integer.parseInt(param.get("endDate"));
        String vendorName = param.get("vendorName");
        String addNote = param.get("addNote");
        String bookingCode = param.get("bookingCode");
        String travelApprovalNum = param.get("travelApprovalNum");
        String snackbarMsg = param.get("snackbarMsg");
        String requestType = param.get("requestType");
        String requestorName = param.get("requestor");
        String location = param.get("location");

        step("Step 1: Login to External Corporate Dashboard and Navigate to External Corporate Dashboard");
        loginPage.loginWithSpecificAccountViaCookie(username, password);
        loginPage.navigateToExternalCorporateDashboard();

        step("Step 2: Expand tiketPlus for Corporate");
        corporateInternalDashboardSidebar.expandTiketPlusForCorporate();

        step("Step 3: Open the Request List and Click on the New Request Button");
        corporateInternalDashboardSidebar.openRequestList();
        tiketPlusForCorporateExternal.clickNewRequestButton();

        step("Step 4: Verify the New Request Page is Loaded with New Event Request and TakeOver Payment Options");
        tiketPlusForCorporateExternal.verifyNewRequestPageLoaded();

        step("Step 5: Click 'Create payment request' for Takeover Payment");
        tiketPlusForCorporateExternal.clickCreatePaymentRequest();

        step("Step 6: Verify the Takeover Payment order details page is opened");
        tiketPlusForCorporateExternal.verifyTakeoverPaymentPageOpened();

        step("Step 7: Select Product Type");
        tiketPlusForCorporateExternal.selectProductTypes(productType);

        step("Step 8: Fill Takeover text inputs");
        tiketPlusForCorporateExternal.fillTakeoverTextInputs(takeoverEventName, participateNum, orderAmt, vendorName, addNote);

        step("Step 9: Select Order Start and End Dates");
        tiketPlusForCorporateExternal.selectDateInNextMonth("order-start-date", startIdx);
        tiketPlusForCorporateExternal.selectDateInNextMonth("order-end-date", endIdx);
        String startDate = tiketPlusForCorporateExternal.getTakeOverStartDate();
        String endDate = tiketPlusForCorporateExternal.getTakeOverEndDate();

        step("Step 10: Click on the Next Contact And Travel Button and verify Contact And Booking Section Opens");
        tiketPlusForCorporateExternal.clickNextContactAndTravelButton();
        tiketPlusForCorporateExternal.verifyContactAndBookingSectionOpens();

        step("Step 11: Fill Booking Code and Travel Approval Number");
        tiketPlusForCorporateExternal.fillBookingAndTravelDetails(bookingCode, travelApprovalNum);

        step("Step 12: Click 'Next: Review & Submit' Button and verify the Review and Submit page opens");
        tiketPlusForCorporateExternal.clickNextReviewAndSubmit();
        tiketPlusForCorporateExternal.verifyReviewAndSubmitPageOpens();

        step("Step 13: Verify all details on the Takeover Payment summary page");
        tiketPlusForCorporateExternal.verifyTakeoverSummaryDetails(
                productType,
                takeoverEventName,
                participateNum,
                String.valueOf(startIdx),
                String.valueOf(endIdx),
                vendorName,
                orderAmt,
                addNote,
                bookingCode,
                travelApprovalNum
        );

        step("Step 14: Click the final Submit button for the Takeover request");
        tiketPlusForCorporateExternal.clickFinalSubmitRequestButton();

        step("Step 15: Verify confirmation modal and confirm submission");
        tiketPlusForCorporateExternal.verifyTakeOverSubmitConfirmationModalAppears();
        tiketPlusForCorporateExternal.clickSubmitRequestConfirmation();

        step("Step 16: Verify the success snackbar");
        tiketPlusForCorporateExternal.verifySnackbarAndWaitUntilDismissed(snackbarMsg);

        step("Step 17: Search for the newly created Takeover Payment event in the Dashboard");
        tiketPlusForCorporateExternal.searchForEventInDashboard(takeoverEventName);

        step("Step 18: Verify the Takeover Payment event details in the Dashboard table match the requested data");
        tiketPlusForCorporateExternal.verifyEventInDashboardTable(
                takeoverEventName,
                requestType,
                requestorName,
                String.valueOf(startDate),
                String.valueOf(endDate),
                location,
                participateNum
        );

        step("Step 19: Cancel the newly created request and verify the confirmation snackbar");
        tiketPlusForCorporateExternal.cancelRequestFromDashboard();
        tiketPlusForCorporateExternal.verifyCancelRequestSnackbar("You’ve canceled the request.");
    }

    @Author(names = {"Nasha"})
    @Module(name = "MICE")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void verifyAdminCanViewQuotationDetailsFromQuotationList() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("verifyAdminCanViewQuotationDetailsFromQuotationList");
        BaseTest.paramsThreadLocal.set(param);
        Session<WebDriver> session = sessionThreadLocal.get();

        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        CorporateInternalDashboardSidebar<?> corporateInternalDashboardSidebar = new CorporateInternalDashboardSidebar<>(session);
        TiketPlusForCorporateExternal<?> tiketPlusForCorporateExternal = new TiketPlusForCorporateExternal<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String quotationNumber = param.get("quotationNumber");
        String status = param.get("status");
        String corporateClientName = param.get("corporateClientName");
        String itemDescription = param.get("itemDescription");
        String itemAmount = param.get("itemAmount");
        String totalAmount = param.get("totalAmount");
        String itemName = param.get("itemName");
        String quoteDate = param.get("quoteDate");

        step("Login to External Corporate Dashboard");
        loginPage.loginWithSpecificAccountViaCookie(username, password);

        step("Navigate to External Corporate Dashboard");
        loginPage.navigateToExternalCorporateDashboard();

        step("click tiket plus for corporate");
        corporateInternalDashboardSidebar.expandTiketPlusForCorporate();

        step("open quotation list");
        corporateInternalDashboardSidebar.openQuotationList();

        step("Search Quotation Number: " + quotationNumber);
        tiketPlusForCorporateExternal.searchQuotationNumber(quotationNumber);

        step("Click More Actions for Quotation");
        tiketPlusForCorporateExternal.clickTableDetailMoreActionsButton();

        step("Click See Quotation Button");
        tiketPlusForCorporateExternal.clickQuotationActionButton("See quotation");
        Assertion.assertThat("Verify Quotation Page Loaded", tiketPlusForCorporateExternal.isQuotationDetailsPageLoaded(quotationNumber), is(true));

        step("Verify receiver information");
        Assertion.assertThat("Verify Quotation Number", tiketPlusForCorporateExternal.getQuotationPageQuotationNumber(), containsString(quotationNumber));
        Assertion.assertThat("Verify Quotation Status", tiketPlusForCorporateExternal.getQuotationPageQuotationStatus(), containsString(status));
        Assertion.assertThat("Verify Quotation Company Name", tiketPlusForCorporateExternal.getQuotationPageCompanyName(), containsString(corporateClientName));
        Assertion.assertThat("Verify Quotation Email Address", tiketPlusForCorporateExternal.getQuotationPageEmailAddress(), containsString(username));

        step("Verify Quotation Details");
        Assertion.assertThat("Verify Quote Date", tiketPlusForCorporateExternal.getQuotationDetailQuoteDate(), containsString(quoteDate));
        Assertion.assertThat("Verify Item Name", tiketPlusForCorporateExternal.getTableDetailByHeading("Item Name", false), containsString(itemName));
        Assertion.assertThat("Verify Item Description", tiketPlusForCorporateExternal.getTableDetailByHeading("Description", false), containsString(itemDescription));
        Assertion.assertThat("Verify Item Amount", tiketPlusForCorporateExternal.getTableDetailByHeading("Amount", true), containsString(itemAmount));
        Assertion.assertThat("Verify Total Amount", tiketPlusForCorporateExternal.getQuotationDetailTotalAmount(), containsString(totalAmount));

        step("Verify Activity logs");
        Assertion.assertThat("Verify Quotation Number in Activity Log", tiketPlusForCorporateExternal.getQuotationDetailActivityLogText(), containsString(quotationNumber));

        step("Verify Discussion");
        Assertion.assertThat("Verify Discussion", tiketPlusForCorporateExternal.getQuotationDetailDiscussionText(), containsString(quotationNumber));
    }

    @Author(names = {"Nasha"})
    @Module(name = "E-invoice")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void verifyAdminCanViewCreatedInvoiceOnInvoiceDetailPage() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("verifyAdminCanViewCreatedInvoiceOnInvoiceDetailPage");
        BaseTest.paramsThreadLocal.set(param);
        Session<WebDriver> session = sessionThreadLocal.get();
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        CorporateExternalDashboardSidebar<?> corporateExternalDashboardSidebar = new CorporateExternalDashboardSidebar<>(session);
        InvoiceAndBillingPage<?> invoiceAndBillingPage = new InvoiceAndBillingPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String invoiceNumber = param.get("invoiceNumber");
        String status = param.get("status");
        String companyName = param.get("companyName");
        String expectedDesc1 = param.get("expectedDesc1");
        String expectedNet1 = param.get("expectedNet1");
        String expectedTax1 = param.get("expectedTax1");
        String expectedQty1 = param.get("expectedQty1");
        String expectedAmt1 = param.get("expectedAmt1");
        String invoiceDate = param.get("invoiceDate");
        String expectedDesc2 = param.get("expectedDesc2");
        String expectedNet2 = param.get("expectedNet2");
        String expectedTax2 = param.get("expectedTax2");
        String expectedQty2 = param.get("expectedQty2");
        String expectedAmt2 = param.get("expectedAmt2");
        String totalAmountDue = param.get("totalAmountDue");


        step("Login");
        loginPage.loginWithSpecificAccountViaCookie(username, password);

        step("Navigate to External Corporate Dashboard");
        loginPage.navigateToExternalCorporateDashboard();

        step("Go to invoice and billing");
        corporateExternalDashboardSidebar.goToInvoiceAndBilling();

        step("Verify Invoice and Billing page is opened");
        Assertion.assertThat("Invoice and Billing page should be opened", invoiceAndBillingPage.isInvoiceAndBillingPageOpened(), is(true));

        step("Search invoice number");
        invoiceAndBillingPage.searchInvoiceNumber(invoiceNumber);

        step("Click on View Invoice from active list");
        invoiceAndBillingPage.clickViewInvoice();

        step("Verify invoice detail page is opened");
        Assertion.assertThat("Invoice detail page should be opened", invoiceAndBillingPage.isInvoiceDetailPageOpened(), is(true));

        step("Verify invoice number");
        Assertion.assertThat("Verify invoice number", invoiceAndBillingPage.getInvoiceNumber(), containsString(invoiceNumber));

        step("Verify invoice status");
        Assertion.assertThat("Verify invoice status", invoiceAndBillingPage.getInvoiceStatus(), containsString(status));

        step("Verify mark as paid button is displayed");
        Assertion.assertThat("Mark as paid button should be displayed", invoiceAndBillingPage.isMarkAsPaidButtonDisplayed(), is(true));

        step("Verify pay button is displayed");
        Assertion.assertThat("Pay button should be displayed", invoiceAndBillingPage.isPayButtonDisplayed(), is(true));

        step("Verify corp info");
        Assertion.assertThat("Corporate client name should be displayed", invoiceAndBillingPage.getCompanyName(), containsString(companyName));
        Assertion.assertThat("Invoice date should be displayed", invoiceAndBillingPage.getInvoiceDate(), containsString(invoiceDate));

        step("Verify items info");
        Assertion.assertThat("Total transaction should be displayed", invoiceAndBillingPage.isItemDisplayed("Total Transaction"), is(true));
        Assertion.assertThat("Service fee should be displayed", invoiceAndBillingPage.isItemDisplayed("Service Fee"), is(true));

        step("Verify total transaction item");
        String targetItem1 = "Total Transaction";
        Assertion.assertThat("[Total Transaction] Description value should match", invoiceAndBillingPage.getItemDetailCellValue(targetItem1, "Description"), is(expectedDesc1));
        Assertion.assertThat("[Total Transaction] Net Amount value should match", invoiceAndBillingPage.getItemDetailCellValue(targetItem1, "Net Amount"), is(expectedNet1));
        Assertion.assertThat("[Total Transaction] Tax value should match", invoiceAndBillingPage.getItemDetailCellValue(targetItem1, "Tax"), is(expectedTax1));
        Assertion.assertThat("[Total Transaction] Qty value should match", invoiceAndBillingPage.getItemDetailCellValue(targetItem1, "Qty"), is(expectedQty1));
        Assertion.assertThat("[Total Transaction] Amount value should match", invoiceAndBillingPage.getItemDetailCellValue(targetItem1, "Amount"), is(expectedAmt1));

        step("Verify service fee item");
        String targetItem2 = "Service Fee";
        Assertion.assertThat("[Service Fee] Description value should match", invoiceAndBillingPage.getItemDetailCellValue(targetItem2, "Description"), is(expectedDesc2));
        Assertion.assertThat("[Service Fee] Net Amount value should match", invoiceAndBillingPage.getItemDetailCellValue(targetItem2, "Net Amount"), is(expectedNet2));
        Assertion.assertThat("[Service Fee] Tax value should match", invoiceAndBillingPage.getItemDetailCellValue(targetItem2, "Tax"), is(expectedTax2));
        Assertion.assertThat("[Service Fee] Qty value should match", invoiceAndBillingPage.getItemDetailCellValue(targetItem2, "Qty"), is(expectedQty2));
        Assertion.assertThat("[Service Fee] Amount value should match", invoiceAndBillingPage.getItemDetailCellValue(targetItem2, "Amount"), is(expectedAmt2));

        step("Verify amount info");
        Assertion.assertThat("Total amount due should match", invoiceAndBillingPage.getTotalAmountDueValue(), is(totalAmountDue));

        step("Verify related docs");
        Assertion.assertThat("Invoice document should be listed", invoiceAndBillingPage.isDocumentPresent("Invoice"), is(true));
        Assertion.assertThat("Transfer Request document should be listed", invoiceAndBillingPage.isDocumentPresent("Transfer Request"), is(true));
        Assertion.assertThat("Cover Letter document should be listed", invoiceAndBillingPage.isDocumentPresent("Cover Letter"), is(true));
        Assertion.assertThat("Tax Invoice document should be listed", invoiceAndBillingPage.isDocumentPresent("Tax Invoice"), is(true));

        step("Verify log history");
        Assertion.assertThat("Log history should be displayed", invoiceAndBillingPage.isLogHistoryDisplayed(), is(true));

    }
    @Author(names = {"Nasha"})
    @Module(name = "Internal")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void verifyLeadIsOnbordingWithPaymentPreferenceAsInstantPayment() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("verifyLeadIsOnbordingWithPaymentPreferenceAsInstantPayment");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        RegisterB2CInWEB<?> registerB2CInDWEB = new RegisterB2CInWEB<>(session);

        Long timeStamp = System.currentTimeMillis();
        String email = (timeStamp) + CoreFunctions.randomName()+ "auto@gmail.com";
        String otp = param.get("otp");
        String fullName = CoreFunctions.randomName();
        String corporateName = CoreFunctions.randomName() + CoreFunctions.generateRandomNumeric(8);
        String officeAddress = param.get("officeAddress");
        String filePath = param.get("filePath");
        String phoneNumber = String.valueOf(CoreFunctions.randomInt(10));

        step("The user navigates to the corporate landing page, clears session cookies and switches the language to English.");
        registerB2CInDWEB.navigateToCorporateLandingPage();
        registerB2CInDWEB.clearCorporateSessionCookies();
        registerB2CInDWEB.selectEnglishLanguage();

        step("The user clicks register now button.");
        registerB2CInDWEB.clickRegisterNowButton();

        step("The user enters the email and click check Email button");
        registerB2CInDWEB.inputEmail(email);
        registerB2CInDWEB.clickCheckEmailButton();
        registerB2CInDWEB.inputOTPCode(otp);

        step("The user will proceed to get registration form");
        registerB2CInDWEB.fillUserDetails(fullName, phoneNumber);
        registerB2CInDWEB.clickGetRegistrationFormButton();
        registerB2CInDWEB.clickContinueToRegistrationFormButton();

        step("The user fill the sign up details");
        registerB2CInDWEB.fillSignUpDetailsToRegister(fullName, corporateName, officeAddress, filePath, "Instant Payment");
        Assertion.assertThat("Verify instant payment is clicked", registerB2CInDWEB.isPaymentMethodSelected("Instant Payment"), is(true));

        step("Verify benefit plans are displayed for instant payment and the user selects the Starter tier");
        Assertion.assertThat("Verify benefit plans section is displayed", registerB2CInDWEB.isBenefitPlansSectionDisplayed(), is(true));
        registerB2CInDWEB.selectBenefitTier("Starter");
        Assertion.assertThat("Verify Starter benefit tier is selected", registerB2CInDWEB.isBenefitTierSelected("Starter"), is(true));
        registerB2CInDWEB.clickContinueButton();

        step("Verify the registration is approved directly without any document upload");
        Assertion.assertThat("Verify document upload page is not displayed", registerB2CInDWEB.isDocumentUploadPageDisplayed(), is(false));
        Assertion.assertThat("Verify registration approved text is displayed", registerB2CInDWEB.isRegistrationApproved(), is(true));

    }

    @Author(names = {"Riya Bajpai"})
    @Module(name = "Internal Dashboard")
    @Tribe(name = "CORPORATE")
    @Vertical(name = "B2B")
    @Test(dataProvider = "testEndToEndCorporateOnboardingWithDeposit_DataProvider")
    public void testEndToEndCorporateOnboardingWithDeposit(Map<String, String> param) throws Exception {
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        RegisterB2CInWEB<?> registerB2CInDWEB = new RegisterB2CInWEB<>(session);

        Long timeStamp = System.currentTimeMillis();
        String email = (timeStamp) + "auto@gmail.com";
        String otp = param.get("otp");
        String fullName = CoreFunctions.randomName();
        String phoneNumber = String.valueOf(CoreFunctions.randomInt(10)).substring(0, 10);
        String corporateName = CoreFunctions.randomName() + CoreFunctions.generateRandomNumeric(8);
        String officeAddress = param.get("officeAddress");
        String filePath = param.get("filePath");
        String companyType = param.get("companyType");


        step("The user navigates to the corporate landing page, clears session cookies and switches the language to English.");
        registerB2CInDWEB.navigateToCorporateLandingPage();
        registerB2CInDWEB.clearCorporateSessionCookies();
        registerB2CInDWEB.selectEnglishLanguage();

        step("The user clicks register now button.");
        registerB2CInDWEB.clickRegisterNowButton();

        step("The user enters the email and click check Email button");
        registerB2CInDWEB.inputEmail(email);
        registerB2CInDWEB.clickCheckEmailButton();
        registerB2CInDWEB.inputOTPCode(otp);

        step("The user will proceed to get registration form");
        registerB2CInDWEB.fillUserDetails(fullName, phoneNumber);
        registerB2CInDWEB.clickGetRegistrationFormButton();
        registerB2CInDWEB.clickContinueToRegistrationFormButton();

        step("The user fill the sign up details");
        registerB2CInDWEB.fillSignUpDetailsToRegister(fullName, corporateName, officeAddress, filePath, "Deposit");
        Assertion.assertThat("Verify deposit payment is clicked", registerB2CInDWEB.isPaymentMethodSelected("Deposit"), is(true));
        registerB2CInDWEB.clickContinueButton();

        step("The user uploads the document and click on continue button and send request.");
        List<String> pendingDocumentsBeforeUpload = registerB2CInDWEB.getAllDocuments();
        registerB2CInDWEB.uploadAllPendingRequiredDocuments(filePath);
        Assertion.assertThat("Verify all docs are uploaded", registerB2CInDWEB.getAllDocuments().isEmpty(), is(true));
        registerB2CInDWEB.clickOnContinue();
        registerB2CInDWEB.clickSendButton();

        step("Verify waiting for document approval page will open");
        Assertion.assertThat("Verify waiting for document approval page will open", registerB2CInDWEB.isWaitingForApprovalPageOpened(), is(true));

        step("Verify uploaded document section");
        Assertion.assertThat("Verify uploaded document section", registerB2CInDWEB.isUploadedDocumentSectionDisplayed(), is(true));
        Assertion.assertThat("Verify uploaded documents exist", registerB2CInDWEB.verifyUploadedDocumentsExist(pendingDocumentsBeforeUpload), is(true));

        step("Verify corporate details section");
        Assertion.assertThat("Verify corporate details section", registerB2CInDWEB.isCorporateDetailsSectionDisplayed(), is(true));
        Assertion.assertThat("Verify corporate type", registerB2CInDWEB.getValueByFieldName("Corporate Type"), is(companyType));
        Assertion.assertThat("Verify corporate name", registerB2CInDWEB.getValueByFieldName("Corporate Name"), is(corporateName));

        step("Verify payment details section");
        Assertion.assertThat("Verify payment details section", registerB2CInDWEB.isPaymentDetailsSectionDisplayed(), is(true));
        Assertion.assertThat("Verify payment method is Deposit Payment", registerB2CInDWEB.getValueByFieldName("Payment Method"), is("Deposit"));
    }

    @DataProvider(name = "testEndToEndCorporateOnboardingWithDeposit_DataProvider")
    public Object[][] testEndToEndCorporateOnboardingWithDeposit_DataProvider() {
        return getObjectParams("testEndToEndCorporateOnboardingWithDeposit");
    }



    @Author(names = {"Vanshika Agarwal"})
    @Module(name = "E-INVOICE")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test(dataProvider = "verifyManualSubmissionByAdmin_DataProvider")
    public void verifyManualSubmissionByAdmin(Map<String, String> param) throws Exception {
        BaseTest.paramsThreadLocal.set(param);

        Session<WebDriver> session = sessionThreadLocal.get();

        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<>(session);
        CorporateDataPage<?> corporateDataPage = new CorporateDataPage<>(session);
        CorporateInternalDashboardSidebar<?> corporateInternalDashboardSidebar = new CorporateInternalDashboardSidebar<>(session);
        TiketPlusForCorporateInternal<?> corporateInternal= new TiketPlusForCorporateInternal<>(session);

        HomePage<?> homePage = new HomePage<>(session);
        LoginB2CInDWEB<?> loginB2CInDWEB = new LoginB2CInDWEB<>(session);
        CorporateExternalDashboardSidebar<?> corporateExternalDashboardSidebar = new CorporateExternalDashboardSidebar<>(session);
        InvoiceAndBillingPage<?> invoiceAndBillingPage = new InvoiceAndBillingPage<>(session);

        String username = param.get("internalUsername");
        String password = param.get("internalPassword");
        String extUsername = param.get("externalUsername");
        String extPassword = param.get("externalPassword");
        String otp = param.get("otp");
        String filePath = param.get("filePath");
        String company = param.get("company");
        String notes = generateRandomString(5) + " Notes";
        String termsAndConditions = generateRandomString(5) + " TnC";

        step("Login to member admin via cookie injection");
        loginMemberAdmin.loginViaCookie(username, password);

        step("Go to Corporate Internal Dashboard");
        loginMemberAdmin.goToDashboard();

        step("Check if user is logged in");
        boolean isLoggedInCRP = corporateDataPage.checkUserIsLoggedIn();

        step("Assert that user is is logged in to corporate dashboard");
        Assertion.assertThat("User is logged in", isLoggedInCRP, CoreMatchers.is(true));

        step("Navigate to Invoice & Billing");
        corporateInternalDashboardSidebar.goToInvoiceAndBilling();

        step("Verify Invoice & Billing page");
        Assertion.assertThat("Invoice & Billing page should be opened",
                corporateInternal.isInvoiceAndBillingPageOpened(), is(true));

        step("Click Create Invoice");
        corporateInternal.clickCreateNewInvoice();

        step("Select Corporate");
        corporateInternal.selectCorporate(company);

        step("Select Invoice Type");
        corporateInternal.selectSystemOrderInvoiceType();

        step("Continue");
        corporateInternal.clickContinue();

        step("Add Order");
        corporateInternal.addOrder();

        step("Add Notes and TnC");
        corporateInternal.enterNotes(notes);
        corporateInternal.enterTermsAndConditions(termsAndConditions);

        step("Continue to Review");
        corporateInternal.clickContinue();

        step("Verify Review Invoice page");
        Assertion.assertThat("Review Invoice page should be opened",
                corporateInternal.isReviewInvoicePageOpened(), is(true));

        step("Publish Invoice");
        corporateInternal.clickPublishInvoice();

        step("Click Publish and send");
        corporateInternal.clickPublishAndSend();

        step("Verify Invoice Created");
        Assertion.assertThat("Invoice status should be Unpaid",
                corporateInternal.getInvoiceStatus(), containsString("Unpaid"));

        step("Capture Invoice Details");
        String invoiceNumber = corporateInternal.getInvoiceNumber();
        String totalAmount = corporateInternal.getTotalAmount();

        step("User logs out.");
        loginMemberAdmin.logout();

        //external dashboard steps

        step("Login via cookie injection and open corporate dashboard");
        homePage.loginViaCookieAndOpenCorporateDashboard(extUsername, extPassword);
        boolean isUserLoggedIn = loginB2CInDWEB.checkIfLoggedIn();

        step("Close tutorial banner if exists");
        loginB2CInDWEB.closeTutorialbanner();

        step("Check that user is logged in");
        Assertion.assertThat("Is user logged in", isUserLoggedIn, is(true));

        step("Go to Invoice and Billing from sidebar");
        corporateExternalDashboardSidebar.goToInvoiceAndBilling();

        step("Verify Invoice and Billing page is opened");
        Assertion.assertThat("Invoice and Billing page should be opened", invoiceAndBillingPage.isInvoiceAndBillingPageOpened(), is(true));

        step("Click on View Invoice from active list");
        invoiceAndBillingPage.searchInvoiceNumber(invoiceNumber);
        invoiceAndBillingPage.clickOnInvoice();

        step("Click Mark as Paid");
        invoiceAndBillingPage.clickMarkAsPaid();

        step("Select Payment Method - Credit Card");
        invoiceAndBillingPage.selectPaymentMethod();

        step("Select Payment Date");
        invoiceAndBillingPage.selectPaymentDate();

        step("Upload payment proof file");
        invoiceAndBillingPage.uploadSingleFile(filePath);

        step("Tick confirmation checkbox");
        invoiceAndBillingPage.acceptConfirmationCheckbox();

        step("Submit Mark as Paid");
        invoiceAndBillingPage.clickMarkAsPaidSubmit();

        step("Verify Invoice Status becomes Payment Review");
        Assertion.assertThat("Invoice status updated to Payment Review", invoiceAndBillingPage.getInvoiceStatusUpdate(), containsString("Payment Review"));

        step("Store the invoice ID displayed on the invoice detail page");
        String invoiceId = invoiceAndBillingPage.getInvoiceIdUpdate();

        step("Store the total amount due from the invoice detail page");
        String totalAmountDue = invoiceAndBillingPage.getTotalAmountDueUpdate();

        step("Verify Invoice Number on External");
        Assertion.assertThat("Invoice number matches", invoiceId, containsString(invoiceNumber));

        step("Verify Total Amount on External");
        Assertion.assertThat("Amount matches", totalAmountDue, containsString(totalAmount));

        step("Verify Notes and TnC on External");
        Assertion.assertThat("Notes matches", invoiceAndBillingPage.getNotes(), containsString(notes));
        Assertion.assertThat("TnC matches", invoiceAndBillingPage.getTnC(), containsString(termsAndConditions));

    }
    @DataProvider(name = "verifyManualSubmissionByAdmin_DataProvider")
    public Object[][] verifyManualSubmissionByAdmin_DataProvider() {
        return getObjectParams("verifyManualSubmissionByAdmin");
    }

    @Author(names = {"Prachi Upadhyay"})
    @Module(name = "Purchase List")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test(dataProvider = "verifyPolicyContentDoesNotBleedBetweenCards_DataProvider")
    public void verifyPolicyContentDoesNotBleedBetweenCards(Map<String, String> param) throws Exception {
        BaseTest.paramsThreadLocal.set(param);
        Session<WebDriver> session = sessionThreadLocal.get();

        HomePage<?> homePage = new HomePage<>(session);
        LoginB2CInDWEB<?> loginB2CInDWEB = new LoginB2CInDWEB<>(session);
        PurchaseListPage<?> purchaseListPage = new PurchaseListPage<>(session);

        HotelApi<?> hotelApi = new HotelApi<>(session);
        FlightApi<?> flightApi = new FlightApi<>(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);

        String travelerEmail = param.get("username");
        String travelerPassword = param.get("password");
        String adminEmail = param.get("adminEmail");
        String adminPassword = param.get("adminPassword");

        String hotelName = param.get("hotelName");
        String airlineName = param.get("airlineName");

        int daysToDepartureDate = param.containsKey("daysToDepartureDate") ? Integer.parseInt(param.get("daysToDepartureDate")) : 90;

        String hotelStartDate = CoreFunctions.getRandomLongDateOnRange(daysToDepartureDate, 10, "yyyy-MM-dd");
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));
        int room = Integer.parseInt(param.get("room"));
        String currency = param.get("currency");
        String fullName = param.get("fullName");
        String nameTitle = param.get("nameTitle");

        String flightOrigin = param.get("flightOrigin");
        String flightDestination = param.get("flightDestination");
        String flightDepartureDate = CoreFunctions.getRandomLongDateOnRange(daysToDepartureDate + 2, 10, "yyyy-MM-dd");
        String searchType = param.get("searchType");
        int infant = Integer.parseInt(param.get("infant"));

        String[] nameParts = fullName.split(" ");
        String firstName = nameParts[0];
        String lastName = nameParts.length > 1 ? nameParts[1] : "Traveler";

        step("Fetch Employee Data for B2B API mapping");
        EmployeeData employeeData = corporateApi.getEmployeeData(travelerEmail);
        Assertion.assertThat("Employee Data fetched successfully", employeeData != null, is(true));

        step("Create Hotel Booking via API (Pending Approval)");
        String hotelOrderId = hotelApi.createIssuedOrderApiB2B(
                travelerEmail, travelerPassword, hotelStartDate, hotelName,
                adult, night, room, currency, fullName, nameTitle,
                employeeData, false, "Automation Testing"
        );
        Assertion.assertThat("Hotel Order ID is generated", !hotelOrderId.isEmpty(), is(true));

        step("Create Flight Booking via API (Pending Approval)");
        String flightOrderId = flightApi.createOrderFlightB2BDomestic(
                travelerEmail, travelerPassword, flightOrigin, flightDestination,
                flightDepartureDate, searchType, adult, 0, infant, airlineName,
                employeeData, firstName, lastName
        );
        Assertion.assertThat("Flight Order ID is generated", !flightOrderId.isEmpty(), is(true));

        step("Login via cookie injection and open corporate dashboard");
        homePage.loginViaCookieAndOpenCorporateDashboard(adminEmail, adminPassword);
        Assertion.assertThat("Is user logged in", loginB2CInDWEB.checkIfLoggedIn(), is(true));
        loginB2CInDWEB.closeTutorialbanner();

        step("Navigate to Purchase List");
        purchaseListPage.goToPurchaseListPage();
        Thread.sleep(4000);

        step("Verify both Order IDs are present simultaneously on Purchase List");
        Assertion.assertThat("Hotel Order Card is present", purchaseListPage.isOrderCardPresent(hotelOrderId), is(true));
        Assertion.assertThat("Flight Order Card is present", purchaseListPage.isOrderCardPresent(flightOrderId), is(true));

        step("Expand Refund & Reschedule Policy for Hotel Card and Verify Details");
        purchaseListPage.clickRefundAndReschedulePolicyForSpecificCard(hotelOrderId);
        Assertion.assertThat("Hotel 'Room Type' header is visible in Policy Table", purchaseListPage.isRoomTypeHeaderVisibleInPolicyTab(hotelOrderId), is(true));
        Assertion.assertThat("Specific Hotel Name is visible inside Hotel Card", purchaseListPage.isSpecificTextVisibleInCard(hotelOrderId, hotelName), is(true));

        LocalDate parsedDate = LocalDate.parse(hotelStartDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String uiFormattedDate = parsedDate.format(DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.ENGLISH));
        Assertion.assertThat("Hotel Check-in Date is visible inside Policy Tab", purchaseListPage.isCheckInDateVisibleInPolicyTab(hotelOrderId, uiFormattedDate), is(true));

        step("Expand Refund & Reschedule Policy for Flight Card and Verify Details");
        purchaseListPage.clickRefundAndReschedulePolicyForSpecificCard(flightOrderId);
        Assertion.assertThat("Flight Route is visible inside Flight Card", purchaseListPage.isFlightRouteVisibleInCard(flightOrderId), is(true));
        Assertion.assertThat("Specific Airline Name is visible inside Flight Card", purchaseListPage.isSpecificTextVisibleInCard(flightOrderId, airlineName), is(true));

        step("Verifying Hotel refund and reschedule policy does not change to flight");
        Assertion.assertThat("Hotel 'Room Type' header is visible in Policy Table", purchaseListPage.isRoomTypeHeaderVisibleInPolicyTab(hotelOrderId), is(true));
        Assertion.assertThat("Specific Hotel Name is visible inside Hotel Card", purchaseListPage.isSpecificTextVisibleInCard(hotelOrderId, hotelName), is(true));

        step("Click Reject button to reject both orders");
        purchaseListPage.rejectSpecificOrder(hotelOrderId, "Automation Test Cleanup");
        purchaseListPage.rejectSpecificOrder(flightOrderId, "Automation Test Cleanup");

        step("Log out From Admin dashboard");
        homePage.logoutFromAdminDashboard();
    }

    @DataProvider(name = "verifyPolicyContentDoesNotBleedBetweenCards_DataProvider")
    public Object[][] verifyPolicyContentDoesNotBleedBetweenCards_DataProvider() {
        return getObjectParams("verifyPolicyContentDoesNotBleedBetweenCards");
    }

    @Author(names = {"Manan Kumar"})
    @Module(name = "Purchase List")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test(dataProvider = "verifyMultiCityOrderOnPendingApprovalList_DataProvider")
    public void verifyMultiCityOrderOnPendingApprovalList(Map<String, String> param) throws Exception {
        BaseTest.paramsThreadLocal.set(param);
        Session<WebDriver> session = sessionThreadLocal.get();

        HomePage<?> homePage = new HomePage<>(session);
        LoginB2CInDWEB<?> loginB2CInDWEB = new LoginB2CInDWEB<>(session);
        PurchaseListPage<?> purchaseListPage = new PurchaseListPage<>(session);
        FlightApi<?> flightApi = new FlightApi<>(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);

        String travelerEmail = param.get("username");
        List<String[]> routes = CoreFunctions.splitPairs(param.get("routes"), "\\|", "-");
        List<String> airlineCodes = CoreFunctions.expandToSize(param.get("airlineCodes"), "\\|", routes.size());
        List<String> departureDates = CoreFunctions.generateAscendingRandomLongDates(param.get("rangeAddDate"), param.get("gapRange"), routes.size());

        step("Create multi city flight order via API as traveler and submit it for approval");
        EmployeeData employeeData = corporateApi.getEmployeeData(travelerEmail);
        FlightApi.MultiCityOrder order = flightApi.createMultiCityOrderB2BPendingApproval(
                travelerEmail, param.get("password"), routes, departureDates, airlineCodes,
                Integer.parseInt(param.get("adult")), Integer.parseInt(param.get("child")),
                Integer.parseInt(param.get("infant")), param.get("cabinClass"), param.get("phoneNumber"), employeeData);
        Assertion.assertThat("Multi city order is created", order, notNullValue());

        step("Login to external dashboard as admin");
        homePage.loginViaCookieAndOpenCorporateDashboard(param.get("adminEmail"), param.get("adminPassword"));
        Assertion.assertThat("Is user logged in", loginB2CInDWEB.checkIfLoggedIn(), is(true));
        loginB2CInDWEB.closeTutorialbanner();

        step("Open Pending Approvals and search order " + order.orderId());
        purchaseListPage.goToPurchaseListPage();
        purchaseListPage.searchByOrderID(order.orderId());
        Assertion.assertThat("Multi city order is present on Pending Approvals", purchaseListPage.isOrderCardPresent(order.orderId()), is(true));

        step("Verify multi city flights are coming in the list");
        Assertion.assertThat("Multi city flights are listed with " + routes.size() + " flights", purchaseListPage.isMultiCitySummaryVisible(order.orderId(), routes.size()), is(true));

        step("Verify route and airline of each segment");
        for (int i = 0; i < routes.size(); i++) {
            String segment = "Segment " + (i + 1) + " " + routes.get(i)[0] + "-" + routes.get(i)[1];
            Assertion.assertThat(segment + " route is visible", purchaseListPage.isMultiCitySegmentRouteVisible(order.orderId(), i + 1, routes.get(i)[0], routes.get(i)[1]), is(true));
            Assertion.assertThat(segment + " airline is present", purchaseListPage.getMultiCitySegmentAirline(order.orderId(), i + 1), not(emptyOrNullString()));
        }
    }

    @DataProvider(name = "verifyMultiCityOrderOnPendingApprovalList_DataProvider")
    public Object[][] verifyMultiCityOrderOnPendingApprovalList_DataProvider() {
        return getObjectParams("verifyMultiCityOrderOnPendingApprovalList");
    }
}