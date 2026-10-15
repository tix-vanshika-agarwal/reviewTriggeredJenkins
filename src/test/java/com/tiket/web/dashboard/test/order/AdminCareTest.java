package com.tiket.web.dashboard.test.order;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.apiPage.CorporateApi;
import com.tiket.appium.DriverTools;
import com.tiket.core.CoreFunctions;
import static com.tiket.core.CoreFunctions.generateRandomDate;
import static com.tiket.core.CoreFunctions.getRandomObject;
import static com.tiket.core.CoreFunctions.randomName;
import com.tiket.model.Session;
import com.tiket.page.order.orderlist.OrderListPage.OrderStatus;
import com.tiket.pojo.order.GeneralVerticalInternalOrderDetail;
import com.tiket.pojo.order.MapVerticalWithOrderId;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import static com.tiket.testdata.TestData.getParam;
import com.tiket.web.dWeb.page.account.AccountPage;
import com.tiket.web.dashboard.page.order.AdminCareDetailPage;
import com.tiket.web.dashboard.page.order.AdminCarePage;
import com.tiket.web.dashboard.page.order.orderCreationPage;
import com.tiket.web.page.order.OrderDetail.OrderDetailWeb;
import com.tiket.web.page.order.OrderList.OrderListWeb;

public class AdminCareTest extends BaseTest {

    @AfterMethod
    public void cleanUpAfterTest(ITestContext context, Method method) {
        Session<?> session = sessionThreadLocal.get();
        if (session == null || session.driver() == null) return;

        WebDriver webDriver = (WebDriver) session.driver();
        new DriverTools<>(webDriver).closeCurrentTab();
    }

    @Author(names = {"Jashan"})
    @Module(name = "Admin Care")
    @Tribe(name = "Order")
    @Vertical(name = "PROP")
    @Test
    public void searchByPhone() throws InterruptedException {
        Map<String, String> param = getParam("searchByPhone");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        AdminCarePage<?> adminCarePage = new AdminCarePage<>(session);
        AdminCareDetailPage<?> adminCareDetailPage = new AdminCareDetailPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String phone = param.get("phone");
        String month = param.get("month");
        String date = param.get("date");

        step("Maximize the window");
        adminCarePage.maximize();

        step("login into dashboard");
        adminCarePage.goToAdminCarePageViaUI(username, password);

        step("clicking dropdown in search by");
        adminCarePage.clickDropdownInSearchBy();

        step("clicking phonenumber in search by dropdown menu");
        adminCarePage.clickPhonenumber();

        step("enter phonenumber");
        adminCarePage.input(phone);

        step("click start date in date transaction range");
        adminCarePage.clickStartDateTransactionRange();

        step("select the start date in start date transaction range and click done");
        adminCarePage.selectDate(month, date);

        step("click search");
        adminCarePage.clickSearch();

        step("checking if the product is present or not");
        Assertion.assertThat("is order card displayed", adminCarePage.checkElement(), is(true));

        step("now clicking the order id to verify the phone number");
        adminCarePage.clickOrderId();

        step("checking phone number associated with the product");
        Assertion.assertThat("is phone number right", adminCareDetailPage.verifyPhoneNumber(phone), is(true));

    }

    @Author(names = {"Jashan"})
    @Module(name = "Admin Care")
    @Tribe(name = "Order")
    @Vertical(name = "PROP")
    @Test
    public void searchByCode() throws InterruptedException {
        Map<String, String> param = getParam("searchByCode");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        AdminCarePage<?> adminCarePage = new AdminCarePage<>(session);
        AdminCareDetailPage<?> adminCareDetailPage = new AdminCareDetailPage<>(session);
        AccountPage<?> accountPage = new AccountPage<>(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);

        String username = param.get("username");
        String password = param.get("password");
        String accountId = param.get("accountId");

        step("Get api access token");
        String apiAccessTokenBearer = "Bearer " + accountPage.getAccessTokenUNM(username, password);

        step("Fetch flight booking code from active order list");
        MapVerticalWithOrderId.TitleAndOrderId activeOrderData = orderListPage.findFirstOrderFilterByVertical(apiAccessTokenBearer, username, accountId, "flight", "IDR", false, false, OrderStatus.PROCESSING_ETICKET);
        String code = orderListPage.getBookingCode(activeOrderData.orderId);

        step("login into dashboard");
        adminCarePage.goToAdminCarePage(username, password);

        step("clicking dropdown in search by");
        adminCarePage.clickDropdownInSearchBy();

        step("clicking Booking Code in search by dropdown menu");
        adminCarePage.clickBookingCode();

        step("enter Booking Code ");
        adminCarePage.input(code);

        step("click search");
        adminCarePage.clickSearch();

        step("final step checking the product");
        Assertion.assertThat("is Order card displayed", adminCarePage.checkElement(), is(true));

        step("now clicking the order id to verify the Booking code");
        adminCarePage.clickOrderId();

        step("checking booking code associated with the product");
        Assertion.assertThat("is booking code right", adminCareDetailPage.verifyBookingCode(code), is(true));

    }

    @Author(names = {"Jashan"})
    @Module(name = "Admin Care")
    @Tribe(name = "Order")
    @Vertical(name = "PROP")
    @Test
    public void searchByPhoneWithoutTransaction() throws InterruptedException {
        Map<String, String> param = getParam("searchByPhoneWithoutTransaction");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        AdminCarePage<?> adminCarePage = new AdminCarePage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String phone = param.get("phone");

        step("Maximize the window");
        adminCarePage.maximize();

        step("login into dashboard");
        adminCarePage.goToAdminCarePage(username, password);

        step("clicking dropdown in search by");
        adminCarePage.clickDropdownInSearchBy();

        step("clicking phonenumber in search by dropdown menu");
        adminCarePage.clickPhonenumber();

        step("enter phonenumber");
        adminCarePage.input(phone);

        step("click search");
        adminCarePage.clickSearch();

        step("final step checking the product");
        Assertion.assertThat("is order card displayed?", adminCarePage.checkElement(), is(false));

    }

    @Author(names = {"Jashan"})
    @Module(name = "Admin Care")
    @Tribe(name = "Order")
    @Vertical(name = "PROP")
    @Test
    public void searchByAccountAndProductTypePositive() throws InterruptedException {
        Map<String, String> param = getParam("searchByAccountAndProductTypePositive");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        AdminCarePage<?> adminCarePage = new AdminCarePage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String account = param.get("account");
        String month = param.get("month");
        String date = param.get("date");
        String product = param.get("product");

        step("Maximize the window");
        adminCarePage.maximize();

        step("login into dashboard");
        adminCarePage.goToAdminCarePage(username, password);

        step("clicking dropdown in search by");
        adminCarePage.clickDropdownInSearchBy();

        step("clicking Account in search by dropdown menu");
        adminCarePage.clickAccount();

        step("enter Account");
        adminCarePage.input(account);

        step("clicking dropdown in product type");
        adminCarePage.clickDropdownInProductType();

        step("clicking Flight in Product type dropdown menu");
        adminCarePage.clickInProductType(product);


        step("click start date in date transaction range");
        adminCarePage.clickStartDateTransactionRange();

        step("select the start date in start date transaction range and click done");
        adminCarePage.selectDate(month, date);

        step("click search");
        adminCarePage.clickSearch();

        step("checking the product");
        Assertion.assertThat("is order card displayed?", adminCarePage.checkElement(), is(true));

        step("checking the product type");
        Assertion.assertThat("is product type as expected?", adminCarePage.verifyProductType(product), is(true));

    }

    @Author(names = {"Jashan"})
    @Module(name = "Admin Care")
    @Tribe(name = "Order")
    @Vertical(name = "PROP")
    @Test
    public void searchByAccountAndProductTypeNegative() throws InterruptedException {
        Map<String, String> param = getParam("searchByAccountAndProductTypeNegative");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        AdminCarePage<?> adminCarePage = new AdminCarePage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String account = param.get("account");
        String month = param.get("month");
        String date = param.get("date");
        String product = param.get("product");

        step("Maximize the window");
        adminCarePage.maximize();

        step("login into dashboard");
        adminCarePage.goToAdminCarePage(username, password);

        step("clicking dropdown in search by");
        adminCarePage.clickDropdownInSearchBy();

        step("clicking Account in search by dropdown menu");
        adminCarePage.clickAccount();

        step("enter Account");
        adminCarePage.input(account);

        step("clicking dropdown in product type");
        adminCarePage.clickDropdownInProductType();

        step("clicking Hotel in Product type dropdown menu");
        adminCarePage.clickInProductType(product);

        step("click start date in date transaction range");
        adminCarePage.clickStartDateTransactionRange();

        step("select the start date in start date transaction range and click done");
        adminCarePage.selectDate(month, date);

        step("click search");
        adminCarePage.clickSearch();

        step("final step checking the product");
        Assertion.assertThat("is order card displayed?", adminCarePage.checkElement(), is(false));

    }

    @Author(names = {"Jashan"})
    @Module(name = "Admin Care")
    @Tribe(name = "Order")
    @Vertical(name = "PROP")
    @Test
    public void searchByAccountAndETiketIssued() throws InterruptedException {
        Map<String, String> param = getParam("searchByAccountAndETiketIssued");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        AdminCarePage<?> adminCarePage = new AdminCarePage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String account = param.get("account");
        String month = param.get("month");
        String date = param.get("date");
        String status = param.get("status");

        step("Maximize the window");
        adminCarePage.maximize();

        step("login into dashboard");
        adminCarePage.goToAdminCarePage(username, password);

        step("clicking dropdown in search by");
        adminCarePage.clickDropdownInSearchBy();

        step("clicking Account in search by dropdown menu");
        adminCarePage.clickAccount();

        step("enter Account");
        adminCarePage.input(account);

        step("click start date in date transaction range");
        adminCarePage.clickStartDateTransactionRange();

        step("select the start date in start date transaction range and click done");
        adminCarePage.selectDate(month, date);

        step("clicking dropdown in Order Status");
        adminCarePage.clickDropdownInOrderStatus();

        step("clicking E-Ticket-issued in search by dropdown menu");
        adminCarePage.clickETicketIssued();


        step("click start date in date transaction range");
        adminCarePage.clickStartDateTransactionRange();

        step("select the start date in start date transaction range and click done");
        adminCarePage.selectDate(month, date);

        step("click search");
        adminCarePage.clickSearch();

        step("checking the product");
        Assertion.assertThat("is order card displayed?", adminCarePage.checkElement(), is(true));

        step("final step checking the order status should be ETicket issued");
        Assertion.assertThat("order status should be ETicket Issued", adminCarePage.verifyOrderStatus(status),
                is(true));

    }

    @Author(names = {"Jashan"})
    @Module(name = "Admin Care")
    @Tribe(name = "Order")
    @Vertical(name = "PROP")
    @Test
    public void searchByAccountAndProcessingETicket() throws InterruptedException {
        Map<String, String> param = getParam("searchByAccountAndProcessingETicket");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        AdminCarePage<?> adminCarePage = new AdminCarePage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String account = param.get("account");
        String month = param.get("month");
        String date = param.get("date");

        step("Maximize the window");
        adminCarePage.maximize();

        step("login into dashboard");
        adminCarePage.goToAdminCarePage(username, password);

        step("clicking dropdown in search by");
        adminCarePage.clickDropdownInSearchBy();

        step("clicking Account in search by dropdown menu");
        adminCarePage.clickAccount();

        step("enter Account");
        adminCarePage.input(account);

        step("clicking dropdown in Order Status");
        adminCarePage.clickDropdownInOrderStatus();

        step("clicking Processing_E-Ticket in search by dropdown menu");
        adminCarePage.clickProcessingETicket();

        step("click start date in date transaction range");
        adminCarePage.clickStartDateTransactionRange();

        step("select the start date in start date transaction range and click done");
        adminCarePage.selectDate(month, date);

        step("click search");
        adminCarePage.clickSearch();

        step("final step checking the product");
        Assertion.assertThat("is order card displayed?", adminCarePage.checkElement(), is(true));

    }

    @Author(names = {"Jashan"})
    @Module(name = "Admin Care")
    @Tribe(name = "Order")
    @Vertical(name = "PROP")
    @Test
    public void searchByAccountAndFilterDate() throws InterruptedException {
        Map<String, String> param = getParam("searchByAccountAndFilterDate");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        AdminCarePage<?> adminCarePage = new AdminCarePage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String account = param.get("account");
        String month = param.get("month");
        String date = param.get("date");

        step("Maximize the window");
        adminCarePage.maximize();

        step("login into dashboard");
        adminCarePage.goToAdminCarePage(username, password);

        step("clicking dropdown in search by");
        adminCarePage.clickDropdownInSearchBy();

        step("clicking Account in search by dropdown menu");
        adminCarePage.clickAccount();

        step("click start date in date transaction range");
        adminCarePage.clickStartDateTransactionRange();

        step("select the start date in start date transaction range and click done");
        adminCarePage.selectDate(month, date);

        step("enter Account");
        adminCarePage.input(account);

        step("click End date in date transaction range");
        adminCarePage.clickEndDateTransactionRange();

        step("select the End date in End date transaction range and click done");
        adminCarePage.selectDate(month, date);

        step("click search");
        adminCarePage.clickSearch();

        step("final step checking the product");
        Assertion.assertThat("is Order card displayed?", adminCarePage.checkElement(),
                is(true));

        step("Checking the date to be right");
        Assertion.assertThat("is Date correct?", adminCarePage.verifyDate(), is(notNullValue()));

    }

    @Author(names = {"NaufalF"})
    @Module(name = "AdminCare")
    @Tribe(name = "Order")
    @Vertical(name = "PROP")
    @Test
    public void adminCareSearchByOrderId() {

        Map<String, String> param = getParam("adminCareSearchByOrderId");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        AdminCarePage<?> adminCarePage = new AdminCarePage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String orderId = param.get("orderid");

        step("Login to Admin Care Dashboard");
        adminCarePage.goToAdminCarePage(username, password);
        Assertion.assertThat("logged in to Admin Care", adminCarePage.isAdminCareLoggedIn(), is(true));

        step("Search the Order Id");
        adminCarePage.selectFilterByName("Search By");
        adminCarePage.selectDropdownMenu("Order ID");
        adminCarePage.inputKeyword(orderId);
        adminCarePage.clickSearch();

        step("Validate Showing Order Id");
        Assertion.assertThat("Order id match", adminCarePage.getOrderListId(0), is(orderId));
    }

    @Author(names = {"NaufalF"})
    @Module(name = "AdminCare")
    @Tribe(name = "Order")
    @Vertical(name = "PROP")
    @Test
    public void adminCareSearchByAccountEmptyData() {
        Map<String, String> param = getParam("adminCareSearchByAccountEmptyData");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        AdminCarePage<?> adminCarePage = new AdminCarePage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String account = param.get("account");
        step("Login to Admin Care Dashboard");
        adminCarePage.goToAdminCarePage(username, password);
        Assertion.assertThat("logged in to Admin Care", adminCarePage.isAdminCareLoggedIn(), is(true));

        step("Search by Account");
        adminCarePage.selectFilterByName("Search By");
        adminCarePage.selectDropdownMenu("Account");
        adminCarePage.inputKeyword(account);
        adminCarePage.clickSearch();

        step("Validate Search Result is Empty");
        Assertion.assertThat("Showing Empty Data", adminCarePage.isEmptySearchResult(), is(true));
    }

    @Author(names = {"NaufalF"})
    @Module(name = "AdminCare")
    @Tribe(name = "Order")
    @Vertical(name = "PROP")
    @Test
    public void adminCareSearchByAccount() throws InterruptedException {
        Map<String, String> param = getParam("adminCareSearchByAccount");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        AdminCarePage<?> adminCarePage = new AdminCarePage<>(session);
        AdminCareDetailPage<?> adminCareDetailPage = new AdminCareDetailPage<>(session);
        String username = param.get("username");
        String password = param.get("password");
        String account = param.get("account");
        String month = param.get("month");
        String date = param.get("date");

        step("Login to Admin Care Dashboard");
        adminCarePage.goToAdminCarePage(username, password);
        Assertion.assertThat("logged in to Admin Care", adminCarePage.isAdminCareLoggedIn(), is(true));

        step("Search by Account");
        adminCarePage.selectFilterByName("Search By");
        adminCarePage.selectDropdownMenu("Account");
        adminCarePage.inputKeyword(account);

        step("click start date in date transaction range");
        adminCarePage.clickStartDateTransactionRange();

        step("select the start date in start date transaction range and click done");
        adminCarePage.selectDate(month, date);
        adminCarePage.clickSearch();

        step("Select First Order Appear");
        adminCarePage.selectOrderListId(0);

        step("Validate Account is Correct");
        Assertion.assertThat("User in Admin Care Detail Page", adminCareDetailPage.isUserInAdminCareDetailPage(),
                is(true));
        Assertion.assertThat("User Account is Correct", adminCareDetailPage.getUserInfoAccount(0), is(account));
    }

    @Author(names = {"Jashan"})
    @Module(name = "Admin Care")
    @Tribe(name = "Order")
    @Vertical(name = "PROP")
    @Test
    public void searchByAccountAndFilterDateNegtive() throws InterruptedException {
        Map<String, String> param = getParam("searchByAccountAndFilterDateNegtive");
        Session<?> session = BaseTest.sessionThreadLocal.get();
        AdminCarePage<?> adminCarePage = new AdminCarePage<>(session);
        String username = param.get("username");
        String password = param.get("password");
        String account = param.get("account");
        String smonth = param.get("smonth");
        String sdate = param.get("sdate");
        String emonth = param.get("emonth");
        String edate = param.get("edate");

        step("Maximize the window");
        adminCarePage.maximize();

        step("login into dashboard");
        adminCarePage.goToAdminCarePage(username, password);

        step("clicking dropdown in search by");
        adminCarePage.clickDropdownInSearchBy();

        step("clicking Account in search by dropdown menu");
        adminCarePage.clickAccount();

        step("click start date in date transaction range");
        adminCarePage.clickStartDateTransactionRange();

        step("select the start date in start date transaction range and click done");
        adminCarePage.selectDate(smonth, sdate);

        step("enter Account");
        adminCarePage.input(account);

        step("click End date in date transaction range");
        adminCarePage.clickEndDateTransactionRange();

        step("select the End date in End date transaction range and click done");
        adminCarePage.selectDate(emonth, edate);

        step("click search");
        adminCarePage.clickSearch();

        step("final step checking the product");
        Assertion.assertThat("product should not be displayed", adminCarePage.verifyDate(), is(notNullValue()));
    }

    @Author(names = {"Jashan"})
    @Module(name = "Admin Care")
    @Tribe(name = "Order")
    @Vertical(name = "PROP")
    @Test
    public void searchByAccountAndFilterDateSchedulePostive() throws InterruptedException {
        Map<String, String> param = getParam("searchByAccountAndFilterDateSchedulePostive");
        Session<?> session = BaseTest.sessionThreadLocal.get();
        AdminCarePage<?> adminCarePage = new AdminCarePage<>(session);
        String username = param.get("username");
        String password = param.get("password");
        String account = param.get("account");
        String traMonth = param.get("traMonth");
        String traDate = param.get("traDate");
        String traMonthEnd = param.get("traMonthEnd");
        String traDateEnd = param.get("traDateEnd");
        String secMonthStart = param.get("secMonthStart");
        String secDateStart = param.get("secDateStart");
        String secYearStart = param.get("secYearStart");
        String secMonthEnd = param.get("secMonthEnd");
        String secDateEnd = param.get("secDateEnd");
        String secYearEnd = param.get("secYearEnd");

        step("Maximize the window");
        adminCarePage.maximize();

        step("login into dashboard");
        adminCarePage.goToAdminCarePage(username, password);

        step("clicking dropdown in search by");
        adminCarePage.clickDropdownInSearchBy();

        step("clicking Account in search by dropdown menu");
        adminCarePage.clickAccount();

        step("enter Account");
        adminCarePage.input(account);

        step("click start date in date transaction range");
        adminCarePage.clickStartDateTransactionRange();

        step("select the start date in start date transaction range and click done");
        adminCarePage.selectDate(traMonth, traDate);

        step("click End date in date transaction range");
        adminCarePage.clickEndDateTransactionRange();

        step("select the End date in End date transaction range and click done");
        adminCarePage.selectDate(traMonthEnd, traDateEnd);

        step("click start date in date Schedule range");
        adminCarePage.clickStartDateScheduleRange();

        step("select the start date in start date Schedule range");
        adminCarePage.selectDateScheduleRange(secMonthStart, secYearStart, secDateStart);

        step("click End date in date Schedule range");
        adminCarePage.clickEndDateScheduleRange();

        step("select the end date in end date Schedule range");
        adminCarePage.selectDateScheduleRange(secMonthEnd, secYearEnd, secDateEnd);

        step("click search");
        adminCarePage.clickSearch();

        step("checking the product");
        Assertion.assertThat("is element displayed", adminCarePage.checkElement(), is(true));

        step("Checking the date to be right");
        Assertion.assertThat("is Date correct?", adminCarePage.verifyDate(), is(notNullValue()));
    }

    @Author(names = {"Jashan"})
    @Module(name = "Admin Care")
    @Tribe(name = "Order")
    @Vertical(name = "PROP")
    @Test
    public void searchByAccountAndFilterDateScheduleNegtive() throws InterruptedException {
        Map<String, String> param = getParam("searchByAccountAndFilterDateScheduleNegtive");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        AdminCarePage<?> adminCarePage = new AdminCarePage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String account = param.get("account");
        String traMonth = param.get("traMonth");
        String traDate = param.get("traDate");
        String traMonthEnd = param.get("traMonthEnd");
        String traDateEnd = param.get("traDateEnd");
        String secMonthStart = param.get("secMonthStart");
        String secDateStart = param.get("secDateStart");
        String secYearStart = param.get("secYearStart");
        String secMonthEnd = param.get("secMonthEnd");
        String secDateEnd = param.get("secDateEnd");
        String secYearEnd = param.get("secYearEnd");

        step("Maximize the window");
        adminCarePage.maximize();

        step("login into dashboard");
        adminCarePage.goToAdminCarePage(username, password);

        step("clicking dropdown in search by");
        adminCarePage.clickDropdownInSearchBy();

        step("clicking Account in search by dropdown menu");
        adminCarePage.clickAccount();

        step("enter Account");
        adminCarePage.input(account);

        step("click start date in date transaction range");
        adminCarePage.clickStartDateTransactionRange();

        step("select the start date in start date transaction range and click done");
        adminCarePage.selectDate(traMonth, traDate);

        step("click End date in date transaction range");
        adminCarePage.clickEndDateTransactionRange();

        step("select the End date in End date transaction range and click done");
        adminCarePage.selectDate(traMonthEnd, traDateEnd);

        step("click start date in date Schedule range");
        adminCarePage.clickStartDateScheduleRange();

        step("select the start date in start date Schedule range");
        adminCarePage.selectDateScheduleRange(secMonthStart, secYearStart, secDateStart);

        step("click End date in date Schedule range");
        adminCarePage.clickEndDateScheduleRange();

        step("select the end date in end date Schedule range");
        adminCarePage.selectDateScheduleRange(secMonthEnd, secYearEnd, secDateEnd);

        step("click search");
        adminCarePage.clickSearch();

        step("final step checking the product");
        Assertion.assertThat("is element displayed", adminCarePage.checkElement(), is(false));
    }

    @Author(names = {"Jashan"})
    @Module(name = "Admin Care")
    @Tribe(name = "Order")
    @Vertical(name = "PROP")
    @Test
    public void searchByOrderIdCheckActionColumn() throws InterruptedException {
        Map<String, String> param = getParam("searchByOrderIdCheckActionColumn");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        AdminCarePage<?> adminCarePage = new AdminCarePage<>(session);
        AdminCareDetailPage<?> adminCareDetailPage = new AdminCareDetailPage<>(session);
        AccountPage<?> accountPage = new AccountPage<>(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        orderCreationPage<?> orderCreationPage = new orderCreationPage<>(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String searchType = param.get("searchType");
        String item = param.get("item");
        String name = param.get("name");
        String email = param.get("email");
        String phone = param.get("phone");
        String orderId = param.get("orderId");

        String hotelName = param.get("hotelName");
        int adult = 1;
        int night = 1;
        int room = 1;
        String startDate = generateRandomDate(param.get("rangeAddDate"));

        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = getRandomObject(salutationList);

        step("login into dashboard");
        adminCarePage.goToAdminCarePage(username, password);

        step("clicking dropdown in search by");
        adminCarePage.clickDropdownInSearchBy();

        step("clicking OrderId in search by dropdown menu");
        adminCarePage.selectInSearchBy(searchType);

        step("enter Order ID");
        adminCarePage.input(orderId);

        step("click search");
        adminCarePage.clickSearch();

        step("checking if the product is present or not");
        Assertion.assertThat("is element displayed", adminCarePage.checkElement(), is(true));

        step("click more in action column");
        adminCarePage.clickMoreInActionColumn();

        step("select details in action column");
        adminCarePage.selectInMoreInActionColumn(item);

        step("verify account information such as name , email Id and phone number");
        Assertion.assertThat("is Name right", adminCareDetailPage.verifyName(name), is(true));
        Assertion.assertThat("is email ID right", adminCareDetailPage.verifyEmail(email), is(true));
        Assertion.assertThat("is phone number right", adminCareDetailPage.verifyPhoneNumber(phone), is(true));

        step("verify able to see issue status");
        Assertion.assertThat("Verify issue status", adminCareDetailPage.verifyIssueStatus(orderId), is(true));

        step("verify able to see action menu");
        Assertion.assertThat("Verify able to see action menu", adminCareDetailPage.verifySeeActionMenu(), is(true));
    }

    @Author(names = {"Jashan"})
    @Module(name = "Admin Care")
    @Tribe(name = "Order")
    @Vertical(name = "PROP")
    @Test
    public void verifyOrderDetailFlight() throws InterruptedException {
        Map<String, String> param = getParam("verifyOrderDetailFlight");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        AdminCarePage<?> adminCarePage = new AdminCarePage<>(session);
        AdminCareDetailPage<?> adminCareDetailPage = new AdminCareDetailPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String orderId = param.get("orderId");
        String searchType = param.get("searchType");
        String item = param.get("item");
        String name = param.get("name");
        String flightName = param.get("flightName");
        String orderDetailID = param.get("orderDetailID");
        String price = param.get("price");
        String pMethod = param.get("pMethod");

        step("Maximize the window");
        adminCarePage.maximize();

        step("login into dashboard");
        adminCarePage.goToAdminCarePage(username, password);

        step("clicking dropdown in search by");
        adminCarePage.clickDropdownInSearchBy();

        step("clicking OrderId in search by dropdown menu");
        adminCarePage.selectInSearchBy(searchType);

        step("enter Order ID");
        adminCarePage.input(orderId);

        step("click search");
        adminCarePage.clickSearch();

        step("checking if the product is present or not");
        Assertion.assertThat("is element displayed", adminCarePage.checkElement(), is(true));

        step("click more in action column");
        adminCarePage.clickMoreInActionColumn();

        step("select details in action column");
        adminCarePage.selectInMoreInActionColumn(item);

        step("check scheduled flight name");
        Assertion.assertThat("is flight name correct?", adminCareDetailPage.verifyScheduledFlight(flightName), is(true));
        Assertion.assertThat("is Order Detail ID correct?", adminCareDetailPage.verifyOrderDetailIDBus(orderDetailID), is(true));

        step("Go to passenger detail tab");
        adminCareDetailPage.clickPassengerDetailFlight();

        step("check name in passenger detail tab");
        Assertion.assertThat("is Name right", adminCareDetailPage.verifyNamePassengerDetail(name), is(true));

        step("Go to Transaction/Invoice tab");
        adminCareDetailPage.clickTransactionInvoiceTabFlight();

        step("check Transaction/Invioce tab");
        Assertion.assertThat("is price correct", adminCareDetailPage.verifyPriceDetailFlightHighEmphasis(price), is(true));
        Assertion.assertThat("is Payment method correct", adminCareDetailPage.verifyPaymentMethodFlight(pMethod), is(true));
    }

    @Author(names = {"Jashan"})
    @Module(name = "Admin Care")
    @Tribe(name = "Order")
    @Vertical(name = "PROP")
    @Test
    public void verifyOrderDetailHotel() throws InterruptedException, JSONException, ExecutionException {
        Map<String, String> param = getParam("verifyOrderDetailHotel");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        AdminCarePage<?> adminCarePage = new AdminCarePage<>(session);
        AdminCareDetailPage<?> adminCareDetailPage = new AdminCareDetailPage<>(session);
        AccountPage<?> accountPage = new AccountPage<>(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        orderCreationPage<?> orderCreationPage = new orderCreationPage<>(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String searchType = param.get("searchType");
        String item = param.get("item");
        String hotelName = param.get("hotelName");
        String orderId = param.get("orderId");
        String orderDetailID = param.get("orderDetailID");
        String price = param.get("price");
        String name = param.get("name");

        step("login into dashboard");
        adminCarePage.goToAdminCarePage(username, password);

        step("clicking dropdown in search by");
        adminCarePage.clickDropdownInSearchBy();

        step("clicking OrderId in search by dropdown menu");
        adminCarePage.selectInSearchBy(searchType);

        step("enter Order ID");
        adminCarePage.input(orderId);

        step("click search");
        adminCarePage.clickSearch();

        step("checking if the product is present or not");
        Assertion.assertThat("is element displayed", adminCarePage.checkElement(), is(true));

        step("click more in action column");
        adminCarePage.clickMoreInActionColumn();

        step("select details in action column");
        adminCarePage.selectInMoreInActionColumn(item);

        step("check scheduled Hotel name");
        Assertion.assertThat("is Hotel name correct?", adminCareDetailPage.verifyScheduledHotel(hotelName), is(true));
        Assertion.assertThat("is Order Detail ID correct?", adminCareDetailPage.verifyHotelOrderDetailID(orderDetailID), is(true));

        step("Go to guest / passenger detail tab");
        adminCareDetailPage.clickPassengerDetailHotel();

        step("check name in passenger detail tab");
        Assertion.assertThat("is Name right", adminCareDetailPage.verifyNamePassengerDetailHotel(name), is(true));

        step("Go to Transaction/Invoice tab");
        adminCareDetailPage.clickTransactionInvoiceTab();

        step("check Transaction/Invioce tab");
        Assertion.assertThat("is price correct", adminCareDetailPage.verifyPriceDetailHotel(price), is(true));
    }

    @Author(names = {"Fery"})
    @Module(name = "AdminCare")
    @Tribe(name = "Order")
    @Vertical(name = "PROP")
    @Test
    public void adminCarePriceDetailIdr() throws InterruptedException {
        Map<String, String> param = getParam("adminCarePriceDetailIdr");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        AdminCarePage<?> adminCarePage = new AdminCarePage<>(session);
        AdminCareDetailPage<?> adminCareDetailPage = new AdminCareDetailPage<>(session);
        AccountPage<?> accountPage = new AccountPage<>(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        orderCreationPage<?> orderCreationPage = new orderCreationPage<>(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String filterName = param.get("filterName");
        String dropdownName = param.get("dropdownName");
        String moreInActionItem = param.get("moreInActionItem");
        String currency = "IDR";

        String hotelName = param.get("hotelName");
        int adult = 1;
        int night = 1;
        int room = 1;
        String startDate = generateRandomDate(param.get("rangeAddDate"));

        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = getRandomObject(salutationList);

        step("Login to Admin Care Dashboard");
        adminCarePage.goToAdminCarePage(username, password);
        Assertion.assertThat("Logged in to Admin Care", adminCarePage.isAdminCareLoggedIn(), is(true));

        step("Get api access token");
        String apiAccessToken = accountPage.getAccessTokenUNM(username, password);
        String apiAccessTokenBearer = "Bearer " + apiAccessToken;
        String accountId = String.valueOf(corporateApi.getAccountId(apiAccessToken));

        step("Fetch hotel order with issued status on order list, else create order");
        MapVerticalWithOrderId.TitleAndOrderId activeOrderData = orderListPage.findFirstOrderFilterByVertical(apiAccessTokenBearer, username, accountId, "hotel", "IDR", false, false, OrderStatus.ETICKET_ISSUED);
        String orderId = orderListPage.getHotelBookedOrderId(orderCreationPage, activeOrderData, username, password, startDate, hotelName, adult, night, room, orderFullName, orderNameTitle, guestFullName, guestTitle);

        step("Search the Order Id");
        adminCarePage.selectFilterByName(filterName);
        adminCarePage.selectDropdownMenu(dropdownName);
        adminCarePage.inputKeyword(orderId);
        adminCarePage.clickSearch();

        step("Open order details");
        adminCarePage.clickMoreInActionColumn();
        adminCarePage.selectInMoreInActionColumn(moreInActionItem);
        Assertion.assertThat("User in Admin Care Detail Page", adminCareDetailPage.isUserInAdminCareDetailPage(), is(true));

        step("Checking the order ID to be right");
        Assertion.assertThat("User Order ID is Correct?", adminCareDetailPage.isOrderIdCorrect(orderId), is(true));

        step("Go to the price details modal");
        adminCareDetailPage.clickTransactionInvoiceTab();
        adminCareDetailPage.clickPriceDetailButton();
        adminCareDetailPage.clickExpandCollapsePriceDetail();

        step("Check the currency details");
        Assertion.assertThat("Currency is IDR", adminCareDetailPage.checkCurrencyPriceDetails(currency), is(true));
    }

    @Author(names = {"Fery"})
    @Module(name = "AdminCare")
    @Tribe(name = "Order")
    @Vertical(name = "PROP")
    @Test
    public void adminCarePriceDetailMultiCurrency() throws InterruptedException {
        Map<String, String> param = getParam("adminCarePriceDetailMultiCurrency");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        AdminCarePage<?> adminCarePage = new AdminCarePage<>(session);
        AdminCareDetailPage<?> adminCareDetailPage = new AdminCareDetailPage<>(session);
        AccountPage<?> accountPage = new AccountPage<>(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        orderCreationPage<?> orderCreationPage = new orderCreationPage<>(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String filterName = param.get("filterName");
        String dropdownName = param.get("dropdownName");
        String moreInActionItem = param.get("moreInActionItem");
        String currency = param.get("currency");

        String cardNumber = param.get("cardNumber");
        String expiry = param.get("expiry");
        String cvv = param.get("cvv");
        String cardName = param.get("cardName");

        String hotelName = param.get("hotelName");
        int adult = 1;
        int night = 1;
        int room = 1;
        String startDate = generateRandomDate(param.get("rangeAddDate"));

        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = getRandomObject(salutationList);

        step("Get api access token");
        String apiAccessToken = accountPage.getAccessTokenUNM(username, password);
        String apiAccessTokenBearer = "Bearer " + apiAccessToken;
        String accountId = String.valueOf(corporateApi.getAccountId(apiAccessToken));

        step("Fetch hotel order with issued status on order list, else create order");
        MapVerticalWithOrderId.TitleAndOrderId activeOrderData = orderListPage.findFirstOrderFilterByVertical(apiAccessTokenBearer, username, accountId, "hotel", currency, false, false, OrderStatus.ETICKET_ISSUED);
        String orderId = orderListPage.getHotelBookedOrderIdCard(orderCreationPage, activeOrderData, username, password, startDate, hotelName, adult, night, room,
                currency, orderFullName, orderNameTitle, guestFullName, guestTitle, cardNumber, expiry, cvv, cardName);

        step("Login to Admin Care Dashboard");
        adminCarePage.goToAdminCarePage(username, password);
        Assertion.assertThat("Logged in to Admin Care", adminCarePage.isAdminCareLoggedIn(), is(true));

        step("Search the Order Id");
        adminCarePage.selectFilterByName(filterName);
        adminCarePage.selectDropdownMenu(dropdownName);
        adminCarePage.inputKeyword(orderId);
        adminCarePage.clickSearch();

        step("Open order details");
        adminCarePage.clickMoreInActionColumn();
        adminCarePage.selectInMoreInActionColumn(moreInActionItem);
        Assertion.assertThat("User in Admin Care Detail Page", adminCareDetailPage.isUserInAdminCareDetailPage(), is(true));

        step("Checking the order ID to be right");
        Assertion.assertThat("User Order ID is Correct?", adminCareDetailPage.isOrderIdCorrect(orderId), is(true));

        step("Go to the price details modal");
        adminCareDetailPage.clickTransactionInvoiceTab();
        adminCareDetailPage.clickPriceDetailButton();
        adminCareDetailPage.clickExpandCollapsePriceDetail();

        step("Check the currency details");
        Assertion.assertThat("Currency is SGD", adminCareDetailPage.checkCurrencyPriceDetails(currency), is(true));
    }

    @Author(names = {"Fery"})
    @Module(name = "AdminCare")
    @Tribe(name = "Order")
    @Vertical(name = "PROP")
    @Test
    public void adminCareAddNote() {
        Map<String, String> param = getParam("adminCareAddNote");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        AdminCarePage<?> adminCarePage = new AdminCarePage<>(session);
        AdminCareDetailPage<?> adminCareDetailPage = new AdminCareDetailPage<>(session);
        AccountPage<?> accountPage = new AccountPage<>(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        orderCreationPage<?> orderCreationPage = new orderCreationPage<>(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String filterName = param.get("filterName");
        String dropdownName = param.get("dropdownName");
        String moreInActionItem = param.get("moreInActionItem");
        String noteText = param.get("noteText");

        String hotelName = param.get("hotelName");
        int adult = 1;
        int night = 1;
        int room = 1;
        String startDate = generateRandomDate(param.get("rangeAddDate"));

        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = getRandomObject(salutationList);

        step("Login to Admin Care Dashboard");
        adminCarePage.goToAdminCarePage(username, password);
        Assertion.assertThat("Logged in to Admin Care", adminCarePage.isAdminCareLoggedIn(), is(true));

        step("Get api access token");
        String apiAccessToken = accountPage.getAccessTokenUNM(username, password);
        String apiAccessTokenBearer = "Bearer " + apiAccessToken;
        String accountId = String.valueOf(corporateApi.getAccountId(apiAccessToken));

        step("Fetch hotel order with issued status on order list, else create order");
        MapVerticalWithOrderId.TitleAndOrderId activeOrderData = orderListPage.findFirstOrderFilterByVertical(apiAccessTokenBearer, username, accountId, "hotel", "IDR", false, false, OrderStatus.ETICKET_ISSUED);
        String orderId = orderListPage.getHotelBookedOrderId(orderCreationPage, activeOrderData, username, password, startDate, hotelName, adult, night, room, orderFullName, orderNameTitle, guestFullName, guestTitle);

        step("Search the Order Id");
        adminCarePage.selectFilterByName(filterName);
        adminCarePage.selectDropdownMenu(dropdownName);
        adminCarePage.inputKeyword(orderId);
        adminCarePage.clickSearch();

        step("Open order details");
        adminCarePage.clickMoreInActionColumn();
        adminCarePage.selectInMoreInActionColumn(moreInActionItem);
        Assertion.assertThat("User in Admin Care Detail Page", adminCareDetailPage.isUserInAdminCareDetailPage(), is(true));

        step("Checking the order ID to be right");
        Assertion.assertThat("User Order ID is Correct?", adminCareDetailPage.isOrderIdCorrect(orderId), is(true));

        step("Click Add Note Button");
        adminCareDetailPage.clickAddNoteButton();
        Assertion.assertThat("Add Note Modal Appeared?", adminCareDetailPage.isAddNoteModalAppear(), is(true));

        step("Select note category from the dropdown menu");
        adminCareDetailPage.selectAddNoteCategoryDropdown();

        step("Add some notes to the note field");
        adminCareDetailPage.addTextNote(noteText);

        step("Click submit button");
        adminCareDetailPage.clickSubmitNote();
        Assertion.assertThat("Add note success snackbar is displayed", adminCareDetailPage.isAddNoteSuccessSnackbarAppear(), is(true));

        step("Check log Latest Activities after adding notes");
        Assertion.assertThat("Log Latest Activities are Correct", adminCareDetailPage.validateLatestActivitiesDescription(noteText), is(true));
    }

    @Author(names = {"Jashan"})
    @Module(name = "Admin Care")
    @Tribe(name = "Order")
    @Vertical(name = "PROP")
    @Test
    public void verifyOrderDetailHomes() throws InterruptedException {
        Map<String, String> param = getParam("verifyOrderDetailHomes");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        AdminCarePage<?> adminCarePage = new AdminCarePage<>(session);
        AdminCareDetailPage<?> adminCareDetailPage = new AdminCareDetailPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String orderId = param.get("orderId");
        String searchType = param.get("searchType");
        String item = param.get("item");
        String name = param.get("name");
        String homeName = param.get("homeName");
        String orderDetailID = param.get("orderDetailID");
        String price = param.get("price");
        String pMethod = param.get("pMethod");

        step("Maximize the window");
        adminCarePage.maximize();

        step("login into dashboard");
        adminCarePage.goToAdminCarePage(username, password);

        step("clicking dropdown in search by");
        adminCarePage.clickDropdownInSearchBy();

        step("clicking OrderId in search by dropdown menu");
        adminCarePage.selectInSearchBy(searchType);

        step("enter Order ID");
        adminCarePage.input(orderId);

        step("click search");
        adminCarePage.clickSearch();

        step("checking if the product is present or not");
        Assertion.assertThat("is element displayed", adminCarePage.checkElement(), is(true));

        step("click more in action column");
        adminCarePage.clickMoreInActionColumn();

        step("select details in action column");
        adminCarePage.selectInMoreInActionColumn(item);

        step("check scheduled Home name");
        Assertion.assertThat("is Home name correct?", adminCareDetailPage.verifyScheduledHotel(homeName), is(true));
        Assertion.assertThat("is Order Detail ID correct?", adminCareDetailPage.verifyHotelOrderDetailID(orderDetailID), is(true));

        step("Go to passenger detail tab");
        adminCareDetailPage.clickGuestDetailsTabHomes();

        step("check name in passenger detail tab");
        Assertion.assertThat("is Name right", adminCareDetailPage.verifyNamePassengerDetailHotel(name), is(true));

        step("Go to Transaction/Invoice tab");
        adminCareDetailPage.clickTransactionInvoiceTab();

        step("check Transaction/Invioce tab");
        Assertion.assertThat("is price correct", adminCareDetailPage.verifyPriceDetail(price), is(true));
        Assertion.assertThat("is Payment method correct", adminCareDetailPage.verifyPaymentMethodHomes(pMethod), is(true));
    }

    @Author(names = {"Jashan"})
    @Module(name = "Admin Care")
    @Tribe(name = "Order")
    @Vertical(name = "PROP")
    @Test
    public void verifyOrderDetailTrain() throws InterruptedException {
        Map<String, String> param = getParam("verifyOrderDetailTrain");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        AdminCarePage<?> adminCarePage = new AdminCarePage<>(session);
        AdminCareDetailPage<?> adminCareDetailPage = new AdminCareDetailPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String orderId = param.get("orderId");
        String searchType = param.get("searchType");
        String item = param.get("item");
        String name = param.get("name");
        String TrainName = param.get("TrainName");
        String orderDetailID = param.get("orderDetailID");
        String price = param.get("price");
        String pMethod = param.get("pMethod");

        step("Maximize the window");
        adminCarePage.maximize();

        step("login into dashboard");
        adminCarePage.goToAdminCarePage(username, password);

        step("clicking dropdown in search by");
        adminCarePage.clickDropdownInSearchBy();

        step("clicking OrderId in search by dropdown menu");
        adminCarePage.selectInSearchBy(searchType);

        step("enter Order ID");
        adminCarePage.input(orderId);

        step("click search");
        adminCarePage.clickSearch();

        step("checking if the product is present or not");
        Assertion.assertThat("is element displayed", adminCarePage.checkElement(), is(true));

        step("click more in action column");
        adminCarePage.clickMoreInActionColumn();

        step("select details in action column");
        adminCarePage.selectInMoreInActionColumn(item);

        step("check scheduled Train name");
        Assertion.assertThat("is Train name correct?", adminCareDetailPage.verifyScheduledTrain(TrainName), is(true));
        Assertion.assertThat("is Order Detail ID correct?", adminCareDetailPage.verifyTrainOrderDetailID(orderDetailID), is(true));

        step("Go to Guest detail tab");
        adminCareDetailPage.clickPassengerDetail();

        step("check name in passenger detail tab");
        Assertion.assertThat("is Name right", adminCareDetailPage.verifyNamePassengerDetailTrain(name), is(true));

        step("Go to Transaction/Invoice tab");
        adminCareDetailPage.clickTransactionInvoiceTab();

        step("check Transaction/Invioce tab");
        Assertion.assertThat("is price correct", adminCareDetailPage.verifyPriceDetailFlight(price), is(true));
        Assertion.assertThat("is Payment method correct", adminCareDetailPage.verifyPaymentMethod(pMethod), is(true));
    }

    @Author(names = {"Jashan"})
    @Module(name = "Admin Care")
    @Tribe(name = "Order")
    @Vertical(name = "PROP")
    @Test
    public void verifyOrderDetailTTD() throws InterruptedException, JSONException, ExecutionException {
        Map<String, String> param = getParam("verifyOrderDetailTTD");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        AdminCarePage<?> adminCarePage = new AdminCarePage<>(session);
        AdminCareDetailPage<?> adminCareDetailPage = new AdminCareDetailPage<>(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);

        String usernameDashboard = param.get("usernameDashboard");
        String passwordDashboard = param.get("passwordDashboard");
        String searchType = param.get("searchType");
        String item = param.get("item");
        String orderId = param.get("orderId");
        Assertion.assertThat("orderId param must be provided", orderId, is(notNullValue()));

        step("Load order details from static orderId");
        JSONObject internalOrderJson = orderListPage.getInternalOrderData(orderId);
        GeneralVerticalInternalOrderDetail generalVerticalInternalOrderDetail = orderListPage.getVerticalInternalOrderDtata(internalOrderJson);
        String orderDetailID = orderListPage.getCorrectVerticalOrderDetailId(generalVerticalInternalOrderDetail);
        String price = generalVerticalInternalOrderDetail.data.price.toString();
        String currency = generalVerticalInternalOrderDetail.data.payment.get(0).paymentCurrency;
        String TTDName = generalVerticalInternalOrderDetail.data.orderDetails.get(0).orderName;
        price = CoreFunctions.formatNumberBasedOnLanguage(price, "en");
        price = currency + " " + price;
        String name = internalOrderJson.getJSONObject("data").getJSONArray("travelers").getJSONObject(0).getString("fullName");

        step("login into dashboard");
        adminCarePage.goToAdminCarePage(usernameDashboard, passwordDashboard);

        step("clicking dropdown in search by");
        adminCarePage.clickDropdownInSearchBy();

        step("clicking OrderId in search by dropdown menu");
        adminCarePage.selectInSearchBy(searchType);

        step("enter Order ID");
        adminCarePage.input(orderId);

        step("click search");
        adminCarePage.clickSearch();

        step("checking if the product is present or not");
        Assertion.assertThat("is element displayed", adminCarePage.checkElement(), is(true));

        step("click more in action column");
        adminCarePage.clickMoreInActionColumn();

        step("select details in action column");
        adminCarePage.selectInMoreInActionColumn(item);

        step("check Booked TTD name");
        String category = CoreFunctions.titleize(orderDetailPage.getVerticalDataObject(orderId).getJSONObject("detailEventConnectMs").getString("eventCategory").toLowerCase());
        Assertion.assertThat("is TTD name correct?", adminCareDetailPage.verifyScheduledTTD(category), containsString(TTDName));
        Assertion.assertThat("is Order Detail ID correct?", adminCareDetailPage.isOrderIdCorrect(orderDetailID), is(true));

        step("Go to visitor name tab");
        adminCareDetailPage.clickVisitorName();

        step("check name in passenger detail tab");
        Assertion.assertThat("is Name right", adminCareDetailPage.verifyVisitorNameTTD(name), is(true));

        step("Go to Transaction/Invoice tab");
        adminCareDetailPage.clickTransactionInvoiceTabFlight();

        step("check Transaction/Invioce tab");
        Assertion.assertThat("is price correct", adminCareDetailPage.verifyPriceDetailFlight(price), is(true));
    }

    @Author(names = {"Jashan"})
    @Module(name = "Admin Care")
    @Tribe(name = "Order")
    @Vertical(name = "PROP")
    @Test
    public void verifyOrderDetailCar() throws InterruptedException, JSONException, ExecutionException {
        Map<String, String> param = getParam("verifyOrderDetailCar");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        AdminCarePage<?> adminCarePage = new AdminCarePage<>(session);
        AdminCareDetailPage<?> adminCareDetailPage = new AdminCareDetailPage<>(session);
        AccountPage<?> accountPage = new AccountPage<>(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        orderCreationPage<?> orderCreationPage = new orderCreationPage<>(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String searchType = param.get("searchType");
        String item = param.get("item");
        String userConsumer = param.get("userConsumer");
        String passConsumer = param.get("passConsumer");
        String phoneNumber = param.get("phoneNumber");

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
        String adminPhone = param.get("adminPhone");

        step("Get api access token");
        String apiAccessToken = accountPage.getAccessTokenUNM(username, password);
        String apiAccessTokenBearer = "Bearer " + apiAccessToken;
        String accountId = String.valueOf(corporateApi.getAccountId(apiAccessToken));

        step("Fetch Car order with issued status on order list, else create order");
        MapVerticalWithOrderId.TitleAndOrderId activeOrderData = orderListPage.findFirstOrderFilterByVertical(apiAccessTokenBearer, username, accountId, "car", "IDR", false, false, OrderStatus.ETICKET_ISSUED);
        String orderId = "";
        String orderDetailID = "";
        String carName = "";
        String price = "";
        String currency = "";
        if (activeOrderData == null) {
            orderCreationPage.createIssuedOrderApiCar(userConsumer, phoneNumber, passConsumer, driverOption, location, startDate, pickupTime, type, fullName, nameTitle, adminName, adminPhone, rentalDuration);
            orderId = String.valueOf(orderCreationPage.getOrderId());
            GeneralVerticalInternalOrderDetail generalVerticalInternalOrderDetail = orderListPage.getVerticalInternalOrderDtata(orderListPage.getInternalOrderData(orderId));
            orderDetailID = orderListPage.getCorrectVerticalOrderDetailId(generalVerticalInternalOrderDetail);
            price = generalVerticalInternalOrderDetail.data.price.toString();
            currency = generalVerticalInternalOrderDetail.data.payment.get(0).paymentCurrency;
            carName = orderCreationPage.issuedOrderData("carName");
        } else {
            orderId = activeOrderData.orderId;
            orderDetailID = activeOrderData.orderDetailId;
            price = activeOrderData.totalPrice;
            carName = orderDetailPage.getVerticalDataObject(orderId).getString("fleetName");
            currency = activeOrderData.currency;
            fullName = orderListPage.getInternalOrderData(orderId).getJSONObject("data").getJSONArray("travelers").getJSONObject(0).getString("fullName");
        }
        price = CoreFunctions.formatNumberBasedOnLanguage(price, "en");
        price = currency + " " + price;

        step("login into dashboard");
        adminCarePage.goToAdminCarePage(username, password);

        step("clicking dropdown in search by");
        adminCarePage.clickDropdownInSearchBy();

        step("clicking OrderId in search by dropdown menu");
        adminCarePage.selectInSearchBy(searchType);

        step("enter Order ID");
        adminCarePage.input(orderId);

        step("click search");
        adminCarePage.clickSearch();

        step("checking if the product is present or not");
        Assertion.assertThat("is element displayed", adminCarePage.checkElement(), is(true));

        step("click more in action column");
        adminCarePage.clickMoreInActionColumn();

        step("select details in action column");
        adminCarePage.selectInMoreInActionColumn(item);

        step("check scheduled Car name");
        Assertion.assertThat("is Car name correct?", adminCareDetailPage.verifyScheduledCar(carName), is(true));
        Assertion.assertThat("is Order Detail ID correct?", adminCareDetailPage.verifyCarOrderDetailID(orderDetailID), is(true));

        step("Go to passenger detail tab");
        adminCareDetailPage.clickGuestDetailsTabHomes();

        step("check name in passenger detail tab");
        Assertion.assertThat("is Name right", adminCareDetailPage.verifyNamePassengerDetailCar(fullName), is(true));

        step("Go to Transaction/Invoice tab");
        adminCareDetailPage.clickTransactionInvoiceTabFlight();

        step("check Transaction/Invioce tab");
        Assertion.assertThat("is price correct", adminCareDetailPage.verifyPriceDetailFlight(price), is(true));
    }

    @Author(names = {"Jashan"})
    @Module(name = "Admin Care")
    @Tribe(name = "Order")
    @Vertical(name = "PROP")
    @Test
    public void verifyOrderDetailAT() throws InterruptedException, JSONException, ExecutionException {
        Map<String, String> param = getParam("verifyOrderDetailAT");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        AdminCarePage<?> adminCarePage = new AdminCarePage<>(session);
        AdminCareDetailPage<?> adminCareDetailPage = new AdminCareDetailPage<>(session);
        AccountPage<?> accountPage = new AccountPage<>(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        orderCreationPage<?> orderCreationPage = new orderCreationPage<>(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String searchType = param.get("searchType");
        String item = param.get("item");
        String adminName = param.get("adminName");
        String adminPhoneNumber = param.get("adminPhoneNumber");
        String origin = param.get("origin");
        String destination = param.get("destination");
        String type = param.get("type");
        String rangeAddDate = param.get("rangeAddDate");
        String pickupTime = param.get("pickupTime").replace('/', ':');
        int pax = Integer.parseInt(param.get("pax"));

        step("Get api access token");
        String apiAccessToken = accountPage.getAccessTokenUNM(username, password);
        String apiAccessTokenBearer = "Bearer " + apiAccessToken;
        String accountId = String.valueOf(corporateApi.getAccountId(apiAccessToken));

        step("Fetch AT order with issued status on order list, else create order");
        MapVerticalWithOrderId.TitleAndOrderId activeOrderData = orderListPage.findFirstOrderFilterByVertical(apiAccessTokenBearer, username, accountId, "airport_transfer", "IDR", false, false, OrderStatus.ETICKET_ISSUED);
        String orderId = orderListPage.getATBookedOrderId(
                orderCreationPage, activeOrderData, username, password, adminName, adminPhoneNumber,
                origin, destination, type, rangeAddDate, pickupTime, pax);

        step("login into dashboard");
        adminCarePage.goToAdminCarePage(username, password);

        step("clicking dropdown in search by");
        adminCarePage.clickDropdownInSearchBy();

        step("clicking OrderId in search by dropdown menu");
        adminCarePage.selectInSearchBy(searchType);

        step("enter Order ID");
        adminCarePage.input(orderId);

        step("click search");
        adminCarePage.clickSearch();

        step("checking if the product is present or not");
        Assertion.assertThat("is element displayed", adminCarePage.checkElement(), is(true));

        step("click more in action column");
        adminCarePage.clickMoreInActionColumn();

        step("select details in action column");
        adminCarePage.selectInMoreInActionColumn(item);

        step("check Car name");
        JSONObject internalOrderVerticalData = orderDetailPage.getVerticalDataObject(orderId);
        Assertion.assertThat("is Car name correct?", adminCareDetailPage.verifyScheduledCarAirportTransfer(internalOrderVerticalData.getJSONObject("fleet").getString("fleetName")), is(true));
        Assertion.assertThat("is Order Detail ID correct?", adminCareDetailPage.verifyCarOrderDetailID(String.valueOf(internalOrderVerticalData.getInt("orderDetailId"))), is(true));

        step("Go to passenger detail tab");
        adminCareDetailPage.clickGuestDetailsTabAirportTransfer();

        step("check name in passenger detail tab");
        JSONObject internalOrderData = orderDetailPage.getInternalOrderDetail(orderId);
        Assertion.assertThat("is Name right", adminCareDetailPage.verifyNamePassengerDetailAT(internalOrderData.getJSONObject("data").getJSONObject("contact").getString("contactName")), is(true));

        step("Go to Transaction/Invoice tab");
        adminCareDetailPage.clickTransactionInvoiceTab();

        step("check Transaction/Invioce tab");
        String price = String.valueOf(internalOrderData.getJSONObject("data").getLong("totalAmount"));
        price = CoreFunctions.formatNumberBasedOnLanguage(price, "en");
        price = internalOrderData.getJSONObject("data").getString("totalAmountCurrency") + " " + price;
        Assertion.assertThat("is price correct", adminCareDetailPage.verifyPriceDetailFlight(price), is(true));
    }

    @Author(names = {"Jashan"})
    @Module(name = "Admin Care")
    @Tribe(name = "Order")
    @Vertical(name = "PROP")
    @Test
    public void verifyOrderDetailBus() throws InterruptedException {
        Map<String, String> param = getParam("verifyOrderDetailBus");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        AdminCarePage<?> adminCarePage = new AdminCarePage<>(session);
        AdminCareDetailPage<?> adminCareDetailPage = new AdminCareDetailPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String orderId = param.get("orderId");
        String searchType = param.get("searchType");
        String item = param.get("item");
        String name = param.get("name");
        String busName = param.get("busName");
        String orderDetailID = param.get("orderDetailID");
        String price = param.get("price");
        String pMethod = param.get("pMethod");

        step("Maximize the window");
        adminCarePage.maximize();

        step("login into dashboard");
        adminCarePage.goToAdminCarePage(username, password);

        step("clicking dropdown in search by");
        adminCarePage.clickDropdownInSearchBy();

        step("clicking OrderId in search by dropdown menu");
        adminCarePage.selectInSearchBy(searchType);

        step("enter Order ID");
        adminCarePage.input(orderId);

        step("click search");
        adminCarePage.clickSearch();

        step("checking if the product is present or not");
        Assertion.assertThat("is element displayed", adminCarePage.checkElement(), is(true));

        step("click more in action column");
        adminCarePage.clickMoreInActionColumn();

        step("select details in action column");
        adminCarePage.selectInMoreInActionColumn(item);

        step("check scheduled Bus name");
        Assertion.assertThat("is Bus name correct?", adminCareDetailPage.verifyScheduledBus(busName), is(true));
        Assertion.assertThat("is Order Detail ID correct?", adminCareDetailPage.verifyOrderDetailIDBus(orderDetailID), is(true));

        step("Go to passenger detail tab");
        adminCareDetailPage.clickPassengerDetailTabBus();

        step("check name in passenger detail tab");
        Assertion.assertThat("is Name right", adminCareDetailPage.verifyNamePassengerDetailTrain(name), is(true));

        step("Go to Transaction/Invoice tab");
        adminCareDetailPage.clickTransactionInvoiceTab();

        step("check Transaction/Invioce tab");
        Assertion.assertThat("is price correct", adminCareDetailPage.verifyPriceDetail(price), is(true));
        Assertion.assertThat("is Payment method correct", adminCareDetailPage.verifyPaymentMethod(pMethod), is(true));
    }

    @Author(names = {"Jashan"})
    @Module(name = "Admin Care")
    @Tribe(name = "Order")
    @Vertical(name = "PROP")
    @Test
    public void verifyOrderDetailFerry() throws InterruptedException {
        Map<String, String> param = getParam("verifyOrderDetailFerry");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        AdminCarePage<?> adminCarePage = new AdminCarePage<>(session);
        AdminCareDetailPage<?> adminCareDetailPage = new AdminCareDetailPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String orderId = param.get("orderId");
        String searchType = param.get("searchType");
        String item = param.get("item");
        String name = param.get("name");
        String price = param.get("price");
        String pMethod = param.get("pMethod");

        step("Maximize the window");
        adminCarePage.maximize();

        step("login into dashboard");
        adminCarePage.goToAdminCarePage(username, password);

        step("clicking dropdown in search by");
        adminCarePage.clickDropdownInSearchBy();

        step("clicking OrderId in search by dropdown menu");
        adminCarePage.selectInSearchBy(searchType);

        step("enter Order ID");
        adminCarePage.input(orderId);

        step("click search");
        adminCarePage.clickSearch();

        step("checking if the product is present or not");
        Assertion.assertThat("is element displayed", adminCarePage.checkElement(), is(true));

        step("click more in action column");
        adminCarePage.clickMoreInActionColumn();

        step("select details in action column");
        adminCarePage.selectInMoreInActionColumn(item);

        step("Go to passenger detail tab");
        adminCareDetailPage.clickPassengerDetail();

        step("check name in passenger detail tab");
        Assertion.assertThat("is Name right", adminCareDetailPage.verifyNamePassengerDetailFerry(name), is(true));

        step("Go to Transaction/Invoice tab");
        adminCareDetailPage.clickTransactionInvoiceTab();

        step("check Transaction/Invioce tab");
        Assertion.assertThat("is price correct", adminCareDetailPage.verifyPriceDetail(price), is(true));
        Assertion.assertThat("is Payment method correct", adminCareDetailPage.verifyPaymentMethod(pMethod), is(true));
    }

    @Author(names = {"Jashan"})
    @Module(name = "Admin Care")
    @Tribe(name = "Order")
    @Vertical(name = "PROP")
    @Test
    public void verifyViewETicket() throws InterruptedException {
        Map<String, String> param = getParam("verifyViewETicket");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        AdminCarePage<?> adminCarePage = new AdminCarePage<>(session);
        AdminCareDetailPage<?> adminCareDetailPage = new AdminCareDetailPage<>(session);
        AccountPage<?> accountPage = new AccountPage<>(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);

        String username = param.get("username");
        String password = param.get("password");
        String searchType = param.get("searchType");
        String item = param.get("item");
        String url = param.get("url");

        step("Get api access token");
        String apiAccessToken = accountPage.getAccessTokenUNM(username, password);
        String apiAccessTokenBearer = "Bearer " + apiAccessToken;

        step("Fetch any issued order on order list (any vertical, skip partial hotel/flexi)");
        MapVerticalWithOrderId.TitleAndOrderId activeOrderData = orderListPage.findFirstOrderByStatus(
                apiAccessTokenBearer, "IDR", OrderStatus.ETICKET_ISSUED);
        if (activeOrderData == null || activeOrderData.orderId == null) {
            Assertion.skip("Issued order id not found");
        }
        String orderId = activeOrderData.orderId;

        step("login into dashboard");
        adminCarePage.goToAdminCarePage(username, password);

        step("clicking dropdown in search by");
        adminCarePage.clickDropdownInSearchBy();

        step("clicking OrderId in search by dropdown menu");
        adminCarePage.selectInSearchBy(searchType);

        step("enter Order ID");
        adminCarePage.input(orderId);

        step("click search");
        adminCarePage.clickSearch();

        step("checking if the product is present or not");
        Assertion.assertThat("is element displayed", adminCarePage.checkElement(), is(true));

        step("click more in action column");
        adminCarePage.clickMoreInActionColumn();

        step("select details in action column");
        adminCarePage.selectInMoreInActionColumn(item);

        step("Click and checking view E-ticket/voutcher button");
        Assertion.assertThat("is new tab opened and is url correct?", adminCareDetailPage.isNewTabOpened(url), is(true));
    }

    @Author(names = {"Jashan"})
    @Module(name = "Admin Care")
    @Tribe(name = "Order")
    @Vertical(name = "PROP")
    @Test
    public void verifyResendETicket() throws InterruptedException {
        Map<String, String> param = getParam("verifyResendETicket");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        AdminCarePage<?> adminCarePage = new AdminCarePage<>(session);
        AdminCareDetailPage<?> adminCareDetailPage = new AdminCareDetailPage<>(session);

        String searchType = param.get("searchType");
        String item = param.get("item");
        String message = param.get("message");
        String usernameDashboard = param.get("usernameDashboard");
        String passwordDashboard = param.get("passwordDashboard");
        String orderId = param.get("orderId");
        Assertion.assertThat("orderId param must be provided", orderId, is(notNullValue()));

        step("login into dashboard");
        adminCarePage.goToAdminCarePage(usernameDashboard, passwordDashboard);

        step("clicking dropdown in search by");
        adminCarePage.clickDropdownInSearchBy();

        step("clicking OrderId in search by dropdown menu");
        adminCarePage.selectInSearchBy(searchType);

        step("enter Order ID");
        adminCarePage.input(orderId);

        step("click search");
        adminCarePage.clickSearch();

        step("checking if the product is present or not");
        Assertion.assertThat("is order card displayed", adminCarePage.checkElement(), is(true));

        step("click more in action column");
        adminCarePage.clickMoreInActionColumn();

        step("select details in action column");
        adminCarePage.selectInMoreInActionColumn(item);

        step("Click resend voucher button and do all steps");
        adminCareDetailPage.clickResendVoucher();
        adminCareDetailPage.selectRecipientTypeDropdown();
        adminCareDetailPage.selectCustomerEmailInDropdown();
        adminCareDetailPage.selectChooseLanguageDropdown();
        adminCareDetailPage.selectEnglishInDropdown();
        adminCareDetailPage.clickResendVoucherSubmitButton();

        step("checking the toast message on top");
        Assertion.assertThat("is success message displayed", adminCareDetailPage.checkResendVoucherSuccessMessage(message), is(true));
    }

    @Author(names = {"Jashan"})
    @Module(name = "Admin Care")
    @Tribe(name = "Order")
    @Vertical(name = "PROP")
    @Test
    public void verifyResendETicketVendor() throws InterruptedException {
        Map<String, String> param = getParam("verifyResendETicketVendor");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        AdminCarePage<?> adminCarePage = new AdminCarePage<>(session);
        AdminCareDetailPage<?> adminCareDetailPage = new AdminCareDetailPage<>(session);

        String searchType = param.get("searchType");
        String item = param.get("item");
        String message = param.get("message");
        String usernameDashboard = param.get("usernameDashboard");
        String passwordDashboard = param.get("passwordDashboard");
        String orderId = param.get("orderId");
        Assertion.assertThat("orderId param must be provided", orderId, is(notNullValue()));

        step("login into dashboard");
        adminCarePage.goToAdminCarePage(usernameDashboard, passwordDashboard);

        step("clicking dropdown in search by");
        adminCarePage.clickDropdownInSearchBy();

        step("clicking OrderId in search by dropdown menu");
        adminCarePage.selectInSearchBy(searchType);

        step("enter Order ID");
        adminCarePage.input(orderId);

        step("click search");
        adminCarePage.clickSearch();

        step("checking if the product is present or not");
        Assertion.assertThat("is order card displayed", adminCarePage.checkElement(), is(true));

        step("click more in action column");
        adminCarePage.clickMoreInActionColumn();

        step("select details in action column");
        adminCarePage.selectInMoreInActionColumn(item);

        step("Click resend voucher button and do all steps");
        adminCareDetailPage.clickResendVoucher();
        adminCareDetailPage.selectRecipientTypeDropdown();
        adminCareDetailPage.selectVendorInDropdown();
        adminCareDetailPage.selectChooseLanguageDropdown();
        adminCareDetailPage.selectEnglishInDropdown();
        adminCareDetailPage.clickResendVoucherSubmitButton();

        step("checking the toast message on top");
        Assertion.assertThat("is success message displayed", adminCareDetailPage.checkResendVoucherSuccessMessage(message), is(true));
    }

    @Author(names = {"Jashan"})
    @Module(name = "Admin Care")
    @Tribe(name = "Order")
    @Vertical(name = "PROP")
    @Test
    public void verifyResendETicketOtherEmail() throws InterruptedException {
        Map<String, String> param = getParam("verifyResendETicketOtherEmail");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        AdminCarePage<?> adminCarePage = new AdminCarePage<>(session);
        AdminCareDetailPage<?> adminCareDetailPage = new AdminCareDetailPage<>(session);

        String searchType = param.get("searchType");
        String item = param.get("item");
        String message = param.get("message");
        String usernameDashboard = param.get("usernameDashboard");
        String passwordDashboard = param.get("passwordDashboard");
        String email = param.get("email");
        String orderId = param.get("orderId");
        Assertion.assertThat("orderId param must be provided", orderId, is(notNullValue()));

        step("login into dashboard");
        adminCarePage.goToAdminCarePage(usernameDashboard, passwordDashboard);

        step("clicking dropdown in search by");
        adminCarePage.clickDropdownInSearchBy();

        step("clicking OrderId in search by dropdown menu");
        adminCarePage.selectInSearchBy(searchType);

        step("enter Order ID");
        adminCarePage.input(orderId);

        step("click search");
        adminCarePage.clickSearch();

        step("checking if the product is present or not");
        Assertion.assertThat("is order card displayed", adminCarePage.checkElement(), is(true));

        step("click more in action column");
        adminCarePage.clickMoreInActionColumn();

        step("select details in action column");
        adminCarePage.selectInMoreInActionColumn(item);

        step("Click resend voucher button and do all steps");
        adminCareDetailPage.clickResendVoucher();
        adminCareDetailPage.selectRecipientTypeDropdown();
        adminCareDetailPage.selectOtherEmailInDropdown();
        adminCareDetailPage.selectChooseLanguageDropdown();
        adminCareDetailPage.selectEnglishInDropdown();
        adminCareDetailPage.sendEmailInEmailField(email);
        adminCareDetailPage.clickResendVoucherSubmitButton();

        step("checking the toast message on top");
        Assertion.assertThat("is success message displayed", adminCareDetailPage.checkResendVoucherSuccessMessage(message), is(true));
    }

    @Author(names = {"Jashan"})
    @Module(name = "Admin Care")
    @Tribe(name = "Order")
    @Vertical(name = "PROP")
    @Test
    public void verifyViewReceiptIDR() throws InterruptedException {
        Map<String, String> param = getParam("verifyViewReceiptIDR");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        AdminCarePage<?> adminCarePage = new AdminCarePage<>(session);
        AdminCareDetailPage<?> adminCareDetailPage = new AdminCareDetailPage<>(session);
        AccountPage<?> accountPage = new AccountPage<>(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        orderCreationPage<?> orderCreationPage = new orderCreationPage<>(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String searchType = param.get("searchType");
        String item = param.get("item");
        String url = param.get("url");

        String productUrlName = param.get("productUrlName");
        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = getRandomObject(salutationList);

        step("Get api access token");
        String apiAccessToken = accountPage.getAccessTokenUNM(username, password);
        String apiAccessTokenBearer = "Bearer " + apiAccessToken;
        String accountId = String.valueOf(corporateApi.getAccountId(apiAccessToken));

        step("Fetch TTD order with issued status on order list, else create order");
        MapVerticalWithOrderId.TitleAndOrderId activeOrderData = orderListPage.findFirstOrderFilterByVertical(apiAccessTokenBearer, username, accountId, "ttd", "IDR", false, false, OrderStatus.ETICKET_ISSUED);
        String orderId = orderListPage.getTTDBookedOrderId(orderCreationPage, activeOrderData, username, password, "IDR", productUrlName, false, orderFullName, orderNameTitle, guestFullName, username, guestTitle);

        step("login into dashboard");
        adminCarePage.goToAdminCarePage(username, password);

        step("clicking dropdown in search by");
        adminCarePage.clickDropdownInSearchBy();

        step("clicking OrderId in search by dropdown menu");
        adminCarePage.selectInSearchBy(searchType);

        step("enter Order ID");
        adminCarePage.input(orderId);

        step("click search");
        adminCarePage.clickSearch();

        step("checking if the product is present or not");
        Assertion.assertThat("is element displayed", adminCarePage.checkElement(), is(true));

        step("click more in action column");
        adminCarePage.clickMoreInActionColumn();

        step("select details in action column");
        adminCarePage.selectInMoreInActionColumn(item);

        step("Go to Transaction/Invoice tab");
        adminCareDetailPage.clickTransactionInvoiceTab();

        step("Click and checking View Receipt button");
        Assertion.assertThat("is new tab opened and is url correct?", adminCareDetailPage.isNewTabOpenedViewReceipt(url), is(true));
    }

    @Author(names = {"Jashan"})
    @Module(name = "Admin Care")
    @Tribe(name = "Order")
    @Vertical(name = "PROP")
    @Test
    public void verifyViewReceiptMultiCurrency() throws InterruptedException {
        Map<String, String> param = getParam("verifyViewReceiptMultiCurrency");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        AdminCarePage<?> adminCarePage = new AdminCarePage<>(session);
        AdminCareDetailPage<?> adminCareDetailPage = new AdminCareDetailPage<>(session);
        AccountPage<?> accountPage = new AccountPage<>(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        orderCreationPage<?> orderCreationPage = new orderCreationPage<>(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String searchType = param.get("searchType");
        String item = param.get("item");
        String url = param.get("url");

        String productUrlName = param.get("productUrlName");
        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = getRandomObject(salutationList);

        String cardNumber = param.get("cardNumber");
        String expiry = param.get("expiry");
        String cvv = param.get("cvv");
        String cardName = param.get("cardName");

        step("Get api access token");
        String apiAccessToken = accountPage.getAccessTokenUNM(username, password);
        String apiAccessTokenBearer = "Bearer " + apiAccessToken;
        String accountId = String.valueOf(corporateApi.getAccountId(apiAccessToken));

        step("Fetch TTD order with issued status on order list, else create order");
        MapVerticalWithOrderId.TitleAndOrderId activeOrderData = orderListPage.findFirstOrderFilterByVertical(apiAccessTokenBearer, username, accountId, "ttd", "SGD", false, false, OrderStatus.ETICKET_ISSUED);
        String orderId = orderListPage.getTodoBookedOrderId(orderCreationPage, activeOrderData, username, password, "SGD", productUrlName, false,
                orderFullName, orderNameTitle, guestFullName, username, guestTitle, cardNumber, expiry, cvv, cardName);

        step("login into dashboard");
        adminCarePage.goToAdminCarePage(username, password);

        step("clicking dropdown in search by");
        adminCarePage.clickDropdownInSearchBy();

        step("clicking OrderId in search by dropdown menu");
        adminCarePage.selectInSearchBy(searchType);

        step("enter Order ID");
        adminCarePage.input(orderId);

        step("click search");
        adminCarePage.clickSearch();

        step("checking if the product is present or not");
        Assertion.assertThat("is element displayed", adminCarePage.checkElement(), is(true));

        step("click more in action column");
        adminCarePage.clickMoreInActionColumn();

        step("select details in action column");
        adminCarePage.selectInMoreInActionColumn(item);

        step("Go to Transaction/Invoice tab");
        adminCareDetailPage.clickTransactionInvoiceTab();

        step("Click and checking View Receipt button");
        Assertion.assertThat("is new tab opened and is url correct?", adminCareDetailPage.isNewTabOpenedViewReceipt(url), is(true));
    }

    @Author(names = {"Jashan"})
    @Module(name = "Admin Care")
    @Tribe(name = "Order")
    @Vertical(name = "PROP")
    @Test
    public void verifyResendReceipt() throws InterruptedException {
        Map<String, String> param = getParam("verifyResendReceipt");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        AdminCarePage<?> adminCarePage = new AdminCarePage<>(session);
        AdminCareDetailPage<?> adminCareDetailPage = new AdminCareDetailPage<>(session);
        AccountPage<?> accountPage = new AccountPage<>(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);

        String username = param.get("username");
        String password = param.get("password");
        String searchType = param.get("searchType");
        String item = param.get("item");
        String message = param.get("message");

        step("Login to Admin Care Dashboard");
        adminCarePage.goToAdminCarePage(username, password);
        Assertion.assertThat("Logged in to Admin Care", adminCarePage.isAdminCareLoggedIn(), is(true));

        step("Get api access token");
        String apiAccessToken = accountPage.getAccessTokenUNM(username, password);
        String apiAccessTokenBearer = "Bearer " + apiAccessToken;

        step("Fetch any issued order on order list (any vertical, skip partial hotel/flexi)");
        MapVerticalWithOrderId.TitleAndOrderId activeOrderData = orderListPage.findFirstOrderByStatus(
                apiAccessTokenBearer, "IDR", OrderStatus.ETICKET_ISSUED);
        if (activeOrderData == null || activeOrderData.orderId == null) {
            Assertion.skip("Issued order id not found");
        }
        String orderId = activeOrderData.orderId;

        step("login into dashboard");
        adminCarePage.goToAdminCarePage(username, password);

        step("clicking dropdown in search by");
        adminCarePage.clickDropdownInSearchBy();

        step("clicking OrderId in search by dropdown menu");
        adminCarePage.selectInSearchBy(searchType);

        step("enter Order ID");
        adminCarePage.input(orderId);

        step("click search");
        adminCarePage.clickSearch();

        step("checking if the product is present or not");
        Assertion.assertThat("is element displayed", adminCarePage.checkElement(), is(true));

        step("click more in action column");
        adminCarePage.clickMoreInActionColumn();

        step("select details in action column");
        adminCarePage.selectInMoreInActionColumn(item);

        step("Go to Transaction/Invoice tab");
        adminCareDetailPage.clickTransactionInvoiceTab();

        step("Click resend Receipt button and do all steps");
        adminCareDetailPage.clickResendReceipt();
        adminCareDetailPage.selectRecipientTypeDropdown();
        adminCareDetailPage.selectCustomerEmailInDropdown();
        adminCareDetailPage.selectChooseLanguageDropdown();
        adminCareDetailPage.selectEnglishInDropdown();
        adminCareDetailPage.clickResendReceiptSubmit();

        step("checking the toast message on top");
        Assertion.assertThat("is success message displayed", adminCareDetailPage.checkSuccessMessage(message), is(true));
    }

    @Author(names = {"Jashan"})
    @Module(name = "Admin Care")
    @Tribe(name = "Order")
    @Vertical(name = "PROP")
    @Test
    public void verifyEditBooking() throws InterruptedException {
        Map<String, String> param = getParam("verifyEditBooking");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        AdminCarePage<?> adminCarePage = new AdminCarePage<>(session);
        AdminCareDetailPage<?> adminCareDetailPage = new AdminCareDetailPage<>(session);
        AccountPage<?> accountPage = new AccountPage<>(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        orderCreationPage<?> orderCreationPage = new orderCreationPage<>(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String loginUsername = param.get("loginUsername");
        String loginPassword = param.get("loginPassword");
        String searchType = param.get("searchType");
        String item = param.get("item");
        String url = param.get("url");

        String hotelName = param.get("hotelName");
        int adult = 1;
        int night = 1;
        int room = 1;
        String startDate = generateRandomDate(param.get("rangeAddDate"));

        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = getRandomObject(salutationList);

        step("Get api access token");
        String apiAccessToken = accountPage.getAccessTokenUNM(username, password);
        String apiAccessTokenBearer = "Bearer " + apiAccessToken;
        String accountId = String.valueOf(corporateApi.getAccountId(apiAccessToken));

        step("Fetch hotel order with issued status on order list, else create order");
        MapVerticalWithOrderId.TitleAndOrderId activeOrderData = orderListPage.findFirstOrderFilterByVertical(apiAccessTokenBearer, username, accountId, "hotel", "IDR", false, false, OrderStatus.ETICKET_ISSUED);
        String orderId = orderListPage.getHotelBookedOrderId(orderCreationPage, activeOrderData, username, password, startDate, hotelName, adult, night, room, orderFullName, orderNameTitle, guestFullName, guestTitle);

        step("login into dashboard");
        adminCarePage.goToAdminCarePage(loginUsername, loginPassword);

        step("clicking dropdown in search by");
        adminCarePage.clickDropdownInSearchBy();

        step("clicking OrderId in search by dropdown menu");
        adminCarePage.selectInSearchBy(searchType);

        step("enter Order ID");
        adminCarePage.input(orderId);

        step("click search");
        adminCarePage.clickSearch();

        step("checking if the product is present or not");
        Assertion.assertThat("is element displayed", adminCarePage.checkElement(), is(true));

        step("click more in action column");
        adminCarePage.clickMoreInActionColumn();

        step("select details in action column");
        adminCarePage.selectInMoreInActionColumn(item);

        step("Go to Guest Detail tab");
        adminCareDetailPage.clickGuestDetailsTabHomes();

        step("Click Edit Booking button");
        adminCareDetailPage.clickEditBooking();

        step("Select room to edit booking");
        adminCareDetailPage.clickRoomToEdit();

        step("Click submit");
        adminCareDetailPage.clickSubmitButton();

        step("Click submit and Check if hotel dashboard is opened and if Order id is present in URL");
        Assertion.assertThat("is new tab opened and is url correct?", adminCareDetailPage.isHotelDashboardOpened(), containsString(url));
    }

    @Author(names = {"Jashan"})
    @Module(name = "Admin Care")
    @Tribe(name = "Order")
    @Vertical(name = "PROP")
    @Test
    public void verifyLogHistory() throws InterruptedException {
        Map<String, String> param = getParam("verifyLogHistory");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        AdminCarePage<?> adminCarePage = new AdminCarePage<>(session);
        AdminCareDetailPage<?> adminCareDetailPage = new AdminCareDetailPage<>(session);
        AccountPage<?> accountPage = new AccountPage<>(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        orderCreationPage<?> orderCreationPage = new orderCreationPage<>(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String searchType = param.get("searchType");
        String item = param.get("item");
        String eTicketIssued = param.get("eTicketIssued");
        String processingETicket = param.get("processingETicket");
        String paid = param.get("paid");
        String waitingForPayment = param.get("waitingForPayment");
        String selectPayMethod = param.get("selectPayMethod");
        String status = param.get("status");

        String hotelName = param.get("hotelName");
        int adult = 1;
        int night = 1;
        int room = 1;
        String startDate = generateRandomDate(param.get("rangeAddDate"));

        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = getRandomObject(salutationList);

        step("Get api access token");
        String apiAccessToken = accountPage.getAccessTokenUNM(username, password);
        String apiAccessTokenBearer = "Bearer " + apiAccessToken;
        String accountId = String.valueOf(corporateApi.getAccountId(apiAccessToken));

        step("Fetch hotel order with issued status on order list, else create order");
        MapVerticalWithOrderId.TitleAndOrderId activeOrderData = orderListPage.findFirstOrderFilterByVertical(apiAccessTokenBearer, username, accountId, "hotel", "IDR", false, false, OrderStatus.ETICKET_ISSUED);
        String orderId = orderListPage.getHotelBookedOrderId(orderCreationPage, activeOrderData, username, password, startDate, hotelName, adult, night, room, orderFullName, orderNameTitle, guestFullName, guestTitle);

        step("login into dashboard");
        adminCarePage.goToAdminCarePage(username, password);

        step("clicking dropdown in search by");
        adminCarePage.clickDropdownInSearchBy();

        step("clicking OrderId in search by dropdown menu");
        adminCarePage.selectInSearchBy(searchType);

        step("enter Order ID");
        adminCarePage.input(orderId);

        step("click search");
        adminCarePage.clickSearch();

        step("checking if the product is present or not");
        Assertion.assertThat("is element displayed", adminCarePage.checkElement(), is(true));

        step("click more in action column");
        adminCarePage.clickMoreInActionColumn();

        step("select details in action column");
        adminCarePage.selectInMoreInActionColumn(item);

        step("Go to Transaction/Invoice tab");
        adminCareDetailPage.clickTransactionInvoiceTab();

        step("Click Log History button");
        adminCareDetailPage.clickLogHistory();

        step("checking all the requred information");
        Assertion.assertThat("is Log History status appeared", adminCareDetailPage.verifyStatus(), is(true));
    }

    @Author(names = {"Jashan"})
    @Module(name = "Admin Care")
    @Tribe(name = "Order")
    @Vertical(name = "PROP")
    @Test
    public void verifySeeCoverageAndValue() throws InterruptedException {
        Map<String, String> param = getParam("verifySeeCoverageAndValue");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        AdminCarePage<?> adminCarePage = new AdminCarePage<>(session);
        AdminCareDetailPage<?> adminCareDetailPage = new AdminCareDetailPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String orderId = param.get("orderId");
        String searchType = param.get("searchType");
        String item = param.get("item");
        String url = param.get("url");

        step("Maximize the window");
        adminCarePage.maximize();

        step("login into dashboard");
        adminCarePage.goToAdminCarePage(username, password);

        step("clicking dropdown in search by");
        adminCarePage.clickDropdownInSearchBy();

        step("clicking OrderId in search by dropdown menu");
        adminCarePage.selectInSearchBy(searchType);

        step("enter Order ID");
        adminCarePage.input(orderId);

        step("click search");
        adminCarePage.clickSearch();

        step("checking if the product is present or not");
        Assertion.assertThat("is element displayed", adminCarePage.checkElement(), is(true));

        step("click more in action column");
        adminCarePage.clickMoreInActionColumn();

        step("select details in action column");
        adminCarePage.selectInMoreInActionColumn(item);

        step("open insurance column");
        adminCareDetailPage.clickInsurance();

        step("Click More");
        adminCareDetailPage.clickMoreInInsuranceColunm();

        step("Click See Coverage & Value and Check if new tab is opened");
        Assertion.assertThat("is new tab opened and is url correct?", adminCareDetailPage.isNewTabOpenedSeeCoverageValue(url), is(true));
    }

    @Author(names = {"Jashan"})
    @Module(name = "Admin Care")
    @Tribe(name = "Order")
    @Vertical(name = "PROP")
    @Test
    public void verifySeeProtectionDetail() throws InterruptedException {
        Map<String, String> param = getParam("verifySeeProtectionDetail");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        AdminCarePage<?> adminCarePage = new AdminCarePage<>(session);
        AdminCareDetailPage<?> adminCareDetailPage = new AdminCareDetailPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String orderId = param.get("orderId");
        String searchType = param.get("searchType");
        String item = param.get("item");
        String url = param.get("url");

        step("Maximize the window");
        adminCarePage.maximize();

        step("login into dashboard");
        adminCarePage.goToAdminCarePage(username, password);

        step("clicking dropdown in search by");
        adminCarePage.clickDropdownInSearchBy();

        step("clicking OrderId in search by dropdown menu");
        adminCarePage.selectInSearchBy(searchType);

        step("enter Order ID");
        adminCarePage.input(orderId);

        step("click search");
        adminCarePage.clickSearch();

        step("checking if the product is present or not");
        Assertion.assertThat("is element displayed", adminCarePage.checkElement(), is(true));

        step("click more in action column");
        adminCarePage.clickMoreInActionColumn();

        step("select details in action column");
        adminCarePage.selectInMoreInActionColumn(item);

        step("open insurance column");
        adminCareDetailPage.clickInsurance();

        step("Click More");
        adminCareDetailPage.clickMoreInInsuranceColunm();

        step("Click See Protection Detail and Check if new tab is opened");
        Assertion.assertThat("is new tab opened and is url correct?", adminCareDetailPage.isNewTabOpenedSeeProtectionDetail(url), is(true));
    }

    @Author(names = {"Jashan"})
    @Module(name = "Admin Care")
    @Tribe(name = "Order")
    @Vertical(name = "PROP")
    @Test
    public void verifyResendPolicy() throws InterruptedException {
        Map<String, String> param = getParam("verifyResendPolicy");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        AdminCarePage<?> adminCarePage = new AdminCarePage<>(session);
        AdminCareDetailPage<?> adminCareDetailPage = new AdminCareDetailPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String orderId = param.get("orderId");
        String searchType = param.get("searchType");
        String item = param.get("item");
        String message = param.get("message");

        step("Maximize the window");
        adminCarePage.maximize();

        step("login into dashboard");
        adminCarePage.goToAdminCarePage(username, password);

        step("clicking dropdown in search by");
        adminCarePage.clickDropdownInSearchBy();

        step("clicking OrderId in search by dropdown menu");
        adminCarePage.selectInSearchBy(searchType);

        step("enter Order ID");
        adminCarePage.input(orderId);

        step("click search");
        adminCarePage.clickSearch();

        step("checking if the product is present or not");
        Assertion.assertThat("is element displayed", adminCarePage.checkElement(), is(true));

        step("click more in action column");
        adminCarePage.clickMoreInActionColumn();

        step("select details in action column");
        adminCarePage.selectInMoreInActionColumn(item);

        step("open insurance column");
        adminCareDetailPage.clickInsurance();

        step("Click More");
        adminCareDetailPage.clickMoreInInsuranceColunm();

        step("Click Resend Policy");
        adminCareDetailPage.clickResendPolicy();

        step("Enter Email");
        adminCareDetailPage.inputEmailInResendPolicy(username);

        step("Click resend Button");
        adminCareDetailPage.clickResendPolicyButton();

        step("checking the toast message on top");
        Assertion.assertThat("is success message displayed", adminCareDetailPage.checkSuccessMessage(message), is(true));
    }

    @Author(names = {"Jashan"})
    @Module(name = "Admin Care")
    @Tribe(name = "PRO-Order")
    @Vertical(name = "PROP")
    @Test
    public void searchByAccountAndFilterPayment() throws InterruptedException {
        Map<String, String> param = getParam("searchByAccountAndFilterPayment");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        AdminCarePage<?> adminCarePage = new AdminCarePage<>(session);
        AdminCareDetailPage<?> adminCareDetailPage = new AdminCareDetailPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String account = param.get("account");
        String traMonth = param.get("traMonth");
        String traDate = param.get("traDate");
        String traMonthEnd = param.get("traMonthEnd");
        String traDateEnd = param.get("traDateEnd");
        String method = param.get("method");
        String pMethod = param.get("pMethod");
        String item = param.get("item");

        step("Maximize the window");
        adminCarePage.maximize();

        step("login into dashboard");
        adminCarePage.goToAdminCarePage(username, password);

        step("clicking dropdown in search by");
        adminCarePage.clickDropdownInSearchBy();

        step("clicking Account in search by dropdown menu");
        adminCarePage.clickAccount();

        step("enter Account");
        adminCarePage.input(account);

        step("click start date in date transaction range");
        adminCarePage.clickStartDateTransactionRange();

        step("select the start date in start date transaction range and click done");
        adminCarePage.selectDate(traMonth, traDate);

        step("click End date in date transaction range");
        adminCarePage.clickEndDateTransactionRange();

        step("select the End date in End date transaction range and click done");
        adminCarePage.selectDate(traMonthEnd, traDateEnd);

        step("click Payment method dropdown");
        adminCarePage.clickPaymentMetodDropdown();

        step("click Sakuku method in payment");
        adminCarePage.selectPaymentMethodInDropdown(pMethod);

        step("click Payment method dropdown");
        adminCarePage.clickPaymentMetodDropdown();

        step("click search");
        adminCarePage.clickSearch();

        step("checking the product");
        Assertion.assertThat("is element displayed", adminCarePage.checkElement(), is(true));

        step("check Payment method");
        Assertion.assertThat("is Payment method Sakuku", adminCarePage.checkPaymentMetohd(method), is(true));
    }

    @Author(names = {"Jashan"})
    @Module(name = "Admin Care")
    @Tribe(name = "PRO-Order")
    @Vertical(name = "PROP")
    @Test
    public void searchByOrderIdCheckCopy() throws InterruptedException {
        Map<String, String> param = getParam("searchByOrderIdCheckCopy");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        AdminCarePage<?> adminCarePage = new AdminCarePage<>(session);
        AdminCareDetailPage<?> adminCareDetailPage = new AdminCareDetailPage<>(session);
        AccountPage<?> accountPage = new AccountPage<>(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        orderCreationPage<?> orderCreationPage = new orderCreationPage<>(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String searchType = param.get("searchType");
        String searchType2 = param.get("searchType2");
        String message = param.get("message");

        String hotelName = param.get("hotelName");
        int adult = 1;
        int night = 1;
        int room = 1;
        String startDate = generateRandomDate(param.get("rangeAddDate"));

        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = getRandomObject(salutationList);

        step("Get api access token");
        String apiAccessToken = accountPage.getAccessTokenUNM(username, password);
        String apiAccessTokenBearer = "Bearer " + apiAccessToken;
        String accountId = String.valueOf(corporateApi.getAccountId(apiAccessToken));

        step("Fetch hotel order with issued status on order list, else create order");
        MapVerticalWithOrderId.TitleAndOrderId activeOrderData = orderListPage.findFirstOrderFilterByVertical(apiAccessTokenBearer, username, accountId, "hotel", "IDR", false, false, OrderStatus.ETICKET_ISSUED);
        String orderId = orderListPage.getHotelBookedOrderId(orderCreationPage, activeOrderData, username, password, startDate, hotelName, adult, night, room, orderFullName, orderNameTitle, guestFullName, guestTitle);

        step("login into dashboard");
        adminCarePage.goToAdminCarePage(username, password);

        step("clicking dropdown in search by");
        adminCarePage.clickDropdownInSearchBy();

        step("clicking OrderId in search by dropdown menu");
        adminCarePage.selectInSearchBy(searchType);

        step("enter Order ID");
        adminCarePage.input(orderId);

        step("click search");
        adminCarePage.clickSearch();

        step("checking if the product is present or not");
        Assertion.assertThat("is order card displayed", adminCarePage.checkElement(), is(true));

        step("click more in action column");
        adminCarePage.clickMoreInActionColumn();

        step("select details in action column");
        sleep(1);
        adminCarePage.clickCopyOnOrderCard();

        step("Check the green success bar at the top");
        Assertion.assertThat("is Green Bar displayed on top?", adminCareDetailPage.checkSuccessMessage(message), is(true));

        step("clicking dropdown in search by");
        adminCarePage.clickDropdownInSearchBy();

        step("clicking Account in search by dropdown menu");
        adminCarePage.selectInSearchBy(searchType2);

        step("Paste-ing copied data");
        String copyData = adminCarePage.verifyCopiedData();

        step("click more in action column");
        adminCarePage.clickMoreInActionColumn();

        step("Go to order detail");
        adminCarePage.selectInMoreInActionColumn("Details");

        step("Check if copied data Correct");
        Assertion.assertThat("is copied data order id correct", copyData, containsString(orderId));
        String orderDetailId = adminCareDetailPage.getDataTableIssuedStatus("Order Details ID");
        Assertion.assertThat("is copied data order detail id correct", copyData, containsString(orderDetailId));
        JSONObject orderDataJson = orderListPage.getOrderDetailObjectByOrderDetailId(orderId, orderDetailId);
        Assertion.assertThat("is copied data hotel name correct", copyData, containsString(orderDataJson.getString("orderName")));
        Assertion.assertThat("is copied data hotel room correct", copyData, containsString(orderDataJson.getString("orderNameDetail")));
        Assertion.assertThat("is copied data room count correct", copyData, containsString(orderDataJson.getJSONObject("vertical").getInt("bookingRoom") + " Room"));
        String checkInDate = CoreFunctions.convertDateFormat(orderDataJson.getJSONObject("vertical").getString("bookingCheckinDate"), "yyyy-MM-dd", "dd MMM yy");
        Assertion.assertThat("is copied data check in date correct", copyData, containsString(checkInDate));
        String fullname = orderDataJson.getJSONObject("vertical").getJSONArray("travellers").getJSONObject(0).getString("fullName");
        Assertion.assertThat("is copied data traveller name correct", copyData.toLowerCase(), containsString(fullname.toLowerCase()));
    }

    @Author(names = {"Jashan"})
    @Module(name = "Admin Care")
    @Tribe(name = "PRO-Order")
    @Vertical(name = "PROP")
    @Test
    public void verifyManualIssued() throws InterruptedException {
        Map<String, String> param = getParam("verifyManualIssued");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> Session = BaseTest.sessionThreadLocal.get();
        AdminCarePage<?> adminCarePage = new AdminCarePage<>(Session);
        AdminCareDetailPage<?> adminCareDetailPage = new AdminCareDetailPage<>(Session);
        orderCreationPage<?> orderCreation = new orderCreationPage<>(Session);

        String username = param.get("username");
        String password = param.get("password");
        String userConsumer = param.get("userConsumer");
        String passConsumer = param.get("passConsumer");
        String startDate = generateRandomDate(param.get("rangeAddDate"));
        String hotelName = param.get("hotelName");
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));
        int room = Integer.parseInt(param.get("room"));
        String item = param.get("item");
        String message = param.get("message");
        String successMessage = param.get("successMessage");
        String eTicketIssuedStatus = param.get("eTicketIssuedStatus");
        String processingETicketStatus = param.get("processingETicketStatus");
        String cardNumber = param.get("cardNumber");
        String cardExpiredMonth = param.get("cardExpiredMonth");
        String cardExpiredYear = param.get("cardExpiredYear");
        String cvv = param.get("cvv");
        String cardName = param.get("cardName");

        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = salutationList[0];

        step("Create new hotel order");
        orderCreation.createWaitingForPaymentOrderUsingCCApi(
                userConsumer, passConsumer, startDate, hotelName, adult, night, room,
                orderFullName, orderNameTitle, guestFullName, guestTitle, cardNumber,
                cardExpiredMonth, cardExpiredYear, cvv, cardName, "IDR");
        String orderId = String.valueOf(orderCreation.getOrderId());

        step("login into dashboard");
        adminCarePage.goToAdminCarePage(username, password);

        step("Search order id " + orderId + " on admin care");
        adminCarePage.searchOrderId(orderId, 3);

        step("checking if the product is present or not");
        Assertion.assertThat("is order card displayed", adminCarePage.checkElement(), is(true));

        step("click more in action column");
        adminCarePage.clickMoreInActionColumn();

        step("select manual issued in action column");
        adminCarePage.selectManualIssued();

        step("click dropdown and select payment method");
        adminCarePage.clickChoosePaymentDropdown();
        adminCarePage.chooseCC();

        step("Enter message or reason");
        adminCarePage.clickAndSendInMessageBox(message);

        step("Click Submit");
        adminCareDetailPage.clickSubmitButton();

        step("click ok");
        adminCarePage.clickOk();

        step("checking the toast message on top");
        Assertion.assertThat("is success message displayed", adminCareDetailPage.checkSuccessMessage(successMessage), is(true));

        step("waiting for success message to disappear");
        adminCarePage.letMessageDisappear();

        step("Checking the status");
        Assertion.assertThat("is Status E-ticket issued?", adminCareDetailPage.checkStatus(eTicketIssuedStatus, processingETicketStatus), is(true));
    }
}