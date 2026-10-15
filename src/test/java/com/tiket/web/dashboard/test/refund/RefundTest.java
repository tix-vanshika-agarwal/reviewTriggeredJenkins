package com.tiket.web.dashboard.test.refund;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.apiPage.CorporateApi;
import com.tiket.apiPage.MemberApi;
import com.tiket.apiPage.PaymentApi;
import com.tiket.apiPage.WhooshApi;
import com.tiket.core.CoreFunctions;
import com.tiket.dto.RefundRequestDTO;
import com.tiket.model.Session;
import com.tiket.page.order.orderlist.OrderListPage.OrderStatus;
import com.tiket.pojo.auth.ProfileDetailResponse;
import com.tiket.pojo.order.GeneralVerticalInternalOrderDetail;
import com.tiket.pojo.order.MapVerticalWithOrderId;
import com.tiket.pojo.payment.InstallmentDetailResult;
import com.tiket.pojo.payment.PaymentCorePromoApplyResponse;
import com.tiket.pojo.payment.PaymentCorePromoSuggestionResponse;
import com.tiket.pojo.paymentDweb.BCAVASnapInquiryResponse;
import com.tiket.pojo.paymentDweb.BCAVASnapPaymentResponse;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.web.dWeb.page.account.AccountPage;
import com.tiket.web.page.order.OrderDetail.OrderDetailWeb;
import com.tiket.web.page.order.OrderList.OrderListWeb;
import com.tiket.web.dashboard.page.b2b_external.EmployeeListPage;
import com.tiket.web.dashboard.page.b2b_external.PurchaseListPage;
import com.tiket.web.dashboard.page.order.AdminCareDetailPage;
import com.tiket.web.dashboard.page.order.AdminCarePage;
import com.tiket.web.dashboard.page.order.orderCreationPage;
import com.tiket.web.dashboard.page.payment.PaymentDashboardPage;
import com.tiket.web.dashboard.page.payment.PaymentLinkConfig;
import com.tiket.web.dashboard.page.payment.PaymentLinkConsumerPage;
import com.tiket.web.dashboard.page.payment.PaymentLinkCreatePage;
import com.tiket.web.dashboard.page.payment.PaymentLinkManagementPage;
import com.tiket.web.dashboard.page.payment.InstallmentDurationDetail;
import com.tiket.web.dashboard.page.payment.PaymentInstallmentPage;
import com.tiket.web.dashboard.page.refund.*;
import org.json.JSONObject;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ExecutionException;

import static com.tiket.core.CoreFunctions.*;
import static com.tiket.testdata.TestData.getObjectParams;
import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.blankOrNullString;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.containsStringIgnoringCase;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;

public class RefundTest extends BaseTest {

    @Author(names = {"NaufalF"})
    @Module(name = "Refund List")
    @Tribe(name = "Refund")
    @Vertical(name = "PROP")
    @Test
    public void testRefundListTab() {
        Map<String, String> param = getParam("testRefundListTab");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        RefundListPage<?> refundListPage = new RefundListPage<>(session);
        AdminCarePage<?> adminCarePage = new AdminCarePage<>(session);

        String username = param.get("username");
        String password = param.get("password");

        step("Login to Admin Care");
        adminCarePage.goToAdminCarePageViaUI(username, password);

        step("Go To Refund List Page");
        refundListPage.goToRefundList();

        step("Verify Refund List Tab");
        Assertion.assertThat("Refund List Tab is Correct", refundListPage.verifyRefundListTab(), is(true));
    }

    @Author(names = {"NaufalF"})
    @Module(name = "Refund List")
    @Tribe(name = "Refund")
    @Vertical(name = "PROP")
    @Test
    public void testRefundListFilterRequestDate() {
        Map<String, String> param = getParam("testRefundListFilterRequestDate");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        RefundListPage<?> refundListPage = new RefundListPage<>(session);
        AdminCarePage<?> adminCarePage = new AdminCarePage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String dateType = param.get("dateType");
        int monthDuration = Integer.parseInt(param.get("monthDuration"));
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy");
        String startDate = today.minusMonths(monthDuration).format(formatter);
        String endDate = today.format(formatter);

        step("Login to Admin Care");
        adminCarePage.goToAdminCarePage(username, password);

        step("Go to Refund List Page");
        refundListPage.goToRefundList();

        step("Go to random refund list tab");
        refundListPage.goToRefundListTab();

        step("Filter using request date");
        refundListPage.selectRequestStartDate(startDate, monthDuration);

        refundListPage.clickSearchButton();

        step("Verify filtered refund data");
        Assertion.assertThat("Verify filtered data request date", refundListPage.verifyFilterDate(startDate, endDate, dateType), is(true));
    }

    @Author(names = {"NaufalF"})
    @Module(name = "Refund List")
    @Tribe(name = "Refund")
    @Vertical(name = "PROP")
    @Test
    public void testRefundListFilterDepartureDate() {
        Map<String, String> param = getParam("testRefundListFilterDepartureDate");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        RefundListPage<?> refundListPage = new RefundListPage<>(session);
        AdminCarePage<?> adminCarePage = new AdminCarePage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        int monthDuration = Integer.parseInt(param.get("monthDuration"));
        String dateType = param.get("dateType");
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy");
        String depStartDate = today.minusMonths(monthDuration).format(formatter);
        String depEndDate = today.plusMonths(monthDuration).format(formatter);

        step("Login to Admin Care");
        adminCarePage.goToAdminCarePage(username, password);

        step("Go to Refund List Page");
        refundListPage.goToRefundList();

        step("Go to random refund list tab");
        refundListPage.goToRefundListTab();

        step("Click filter button");
        refundListPage.clickFilterButton();

        step("Filter using departure date from " + depStartDate + " to " + depEndDate);
        refundListPage.setDepartDate(depStartDate, depEndDate, monthDuration);
        refundListPage.clickApplyButton();

        step("Verify filtered refund data");
        Assertion.assertThat("Verify filtered data departure date", refundListPage.verifyFilterDate(depStartDate, depEndDate, dateType), is(true));
    }

    @Author(names = {"NaufalF"})
    @Module(name = "Refund List")
    @Tribe(name = "Refund")
    @Vertical(name = "PROP")
    @Test
    public void testRefundListFilterStatus() {
        Map<String, String> param = getParam("testRefundListFilterStatus");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        RefundListPage<?> refundListPage = new RefundListPage<>(session);
        AdminCarePage<?> adminCarePage = new AdminCarePage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String status = param.get("status");
        int monthDuration = Integer.parseInt(param.get("monthDuration"));
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy");
        String startDate = today.minusMonths(monthDuration).format(formatter);

        step("Login to Admin Care");
        adminCarePage.goToAdminCarePage(username, password);

        step("Go to Refund List Page");
        refundListPage.goToRefundList();

        step("Go to random refund list tab");
        String vertical = refundListPage.goToRefundListTab();

        step("Filter using request date: " + startDate);
        refundListPage.selectRequestStartDate(startDate, monthDuration);
        refundListPage.clickSearchButton();

        step("Click filter button");
        refundListPage.clickFilterButton();

        step("Select filter by refund status");
        refundListPage.selectFilterStatus(status);
        refundListPage.clickApplyButton();

        step("Verify filtered refund data");
        Assertion.assertThat("Verify filtered data refund status", refundListPage.verifyFilterRefundStatus(status, vertical), is(true));
    }

    @Author(names = {"NaufalF"})
    @Module(name = "Refund List")
    @Tribe(name = "Refund")
    @Vertical(name = "PROP")
    @Test
    public void testRefundListFilterProgramName() {
        Map<String, String> param = getParam("testRefundListFilterProgramName");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        RefundListPage<?> refundListPage = new RefundListPage<>(session);
        AdminCarePage<?> adminCarePage = new AdminCarePage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String programNameFlight = param.get("programNameFlight");
        String programNameTodo = param.get("programNameTodo");
        int monthDuration = Integer.parseInt(param.get("monthDuration"));
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy");
        String startDate = today.minusMonths(monthDuration).format(formatter);

        step("Login to Admin Care");
        adminCarePage.goToAdminCarePage(username, password);

        step("Go to Refund List Page");
        refundListPage.goToRefundList();

        step("Go to Native Flights refund list tab");
        refundListPage.goToRefundListTab("Native Flights");

        step("Filter using request date: " + startDate);
        refundListPage.selectRequestStartDate(startDate, monthDuration);
        refundListPage.clickSearchButton();

        step("Filter by program name: 100p refund");
        refundListPage.clickFilterButton();
        refundListPage.selectFilterProgramName(programNameFlight);
        refundListPage.clickApplyButton();
        Assertion.assertThat("Verify filtered data refund program name", refundListPage.verifyFilterProgramName(programNameFlight), is(true));

        step("Go to To Do refund list tab");
        refundListPage.goToRefundListTab("To Do");

        step("Filter using request date");
        refundListPage.selectRequestStartDate(startDate, monthDuration);
        refundListPage.clickSearchButton();

        step("Filter by program name: TTD Cancellation");
        refundListPage.clickFilterButton();
        refundListPage.selectFilterProgramName(programNameTodo);
        refundListPage.clickApplyButton();
        Assertion.assertThat("Verify filtered data refund program name", refundListPage.verifyFilterProgramName(programNameTodo), is(true));
    }

    @Author(names = {"NaufalF"})
    @Module(name = "Refund List")
    @Tribe(name = "Refund")
    @Vertical(name = "PROP")
    @Test(dataProvider = "testRefundListSearchByOrderIdDataProvider")
    public void testRefundListSearchByOrderId(Map<String, String> param) throws Exception {
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        RefundListPage<?> refundListPage = new RefundListPage<>(session);
        AdminCarePage<?> adminCarePage = new AdminCarePage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String tabName = param.get("tabName");
        String vertical = param.get("vertical");
        String vendor = param.get("vendor");

        step("Get Order ID that have Refund Data from DB");
        String orderId = refundListPage.getOrderIdFromRefundDB(vertical, vendor);

        step("Login to Admin Care");
        adminCarePage.goToAdminCarePage(username, password);

        step("Go to Refund List page");
        adminCarePage.goToRefundPage();
        refundListPage.goToRefundList();

        step("Go to Tab " + tabName + " and verify");
        refundListPage.goToRefundListTab(tabName);
        refundListPage.searchRefundByOrderId(orderId);

        step("Verify Order Id shown");
        Assertion.assertThat("Verify refund request id shown", refundListPage.verifyFilterOrderId(orderId), is(true));
    }

    @DataProvider(name = "testRefundListSearchByOrderIdDataProvider")
    public Object[][] testRefundListSearchByOrderIdDataProvider() {
        return getObjectParams("testRefundListSearchByOrderId");
    }

    @Author(names = {"NaufalF"})
    @Module(name = "Refund List")
    @Tribe(name = "Refund")
    @Vertical(name = "PROP")
    @Test(dataProvider = "testRefundListSearchByOrderDetail")
    public void testRefundListSearchByOrderDetail(Map<String, String> param) throws InterruptedException {
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        RefundListPage<?> refundListPage = new RefundListPage<>(session);
        AdminCarePage<?> adminCarePage = new AdminCarePage<>(session);
        AdminCareDetailPage<?> adminCareDetailPage = new AdminCareDetailPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String tabName = param.get("tabName");
        String month = param.get("month");
        String date = param.get("date");
        String vertical = param.get("vertical");
        String searchBy = param.get("searchBy");

        step("Login to Admin Care");
        adminCarePage.goToAdminCarePage(username, password);

        step("Filter by product type");
        adminCarePage.clickDropdownInProductType();
        adminCarePage.clickInProductType(vertical);
        adminCarePage.clickDropdownInProductType();

        step("Filter by order status: Processing refund or Refunded");
        adminCarePage.clickDropdownInOrderStatus();
        adminCarePage.selectDropdownMenu("Refunded");
        adminCarePage.selectDropdownMenu("Processing refund");
        adminCarePage.clickDropdownInOrderStatus();

        step("Set filter start transaction date");
        adminCarePage.clickSeeMoreFilter();
        adminCarePage.clickStartDateTransactionRange();
        adminCarePage.selectDate(month, date);
        adminCarePage.clickSearch();

        step("Get order detail id from admin care detail page");
        adminCarePage.selectOrderListId(0);
        String orderDetailId = adminCareDetailPage.getOrderDetailByVertical(vertical);

        step("Go to Refund List page");
        adminCareDetailPage.goToRefundPage();
        refundListPage.goToRefundList();

        step("Go to Tab " + tabName + " and verify");
        refundListPage.goToRefundListTab(tabName);
        refundListPage.searchUsingFilterSearchBy(searchBy, orderDetailId);
        boolean isShownOrderDetail = refundListPage.verifyOrderPair(orderDetailId, searchBy);
        boolean isShownOnGDSFlight = false;

        if (vertical.equals("Flight")) {
            step("Go to Tab GDS Flight and verify");
            refundListPage.goToRefundListTab("GDS Flights");
            refundListPage.searchUsingFilterSearchBy(searchBy, orderDetailId);
            isShownOnGDSFlight = refundListPage.verifyOrderPair(orderDetailId, searchBy);
        }

        step("Verify Order Detail ID shown");
        Assertion.assertThat("Verify order detail id shown", (isShownOrderDetail || isShownOnGDSFlight), is(true));
    }

    @DataProvider(name = "testRefundListSearchByOrderDetail")
    public Object[][] testRefundListSearchByOrderDetail() {
        return getObjectParams("testRefundListSearchByOrderDetail");
    }

    @Author(names = {"Fery", "NaufalF"})
    @Module(name = "RefundForm")
    @Tribe(name = "Refund")
    @Vertical(name = "PROP")
    @Test
    public void testFullRefundUniqueCode() {
        Map<String, String> param = getParam("testFullRefundUniqueCode");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        AdminCarePage<?> adminCarePage = new AdminCarePage<>(session);
        AdminCareDetailPage<?> adminCareDetailPage = new AdminCareDetailPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String orderId = param.get("orderId");
        String filterName = param.get("filterName");
        String dropdownName = param.get("dropdownName");
        String moreInActionItem = param.get("moreInActionItem");
        String refundMethod = param.get("refundMethod");
        String reason = param.get("reason");

        step("Login to Admin Care Dashboard");
        adminCarePage.goToAdminCarePage(username, password);

        step("Search the Order Id");
        adminCarePage.selectFilterByName(filterName);
        adminCarePage.selectDropdownMenu(dropdownName);
        adminCarePage.inputKeyword(orderId);
        adminCarePage.clickSearch();

        step("Open order details");
        adminCarePage.clickMoreInActionColumn();
        adminCarePage.selectInMoreInActionColumn(moreInActionItem);

        step("Go to the refund cancel tab");
        adminCareDetailPage.clickRefundCancelTab();

        step("Click E-Refund by CS Button");
        adminCareDetailPage.clickERefundByCsButton();

        step("Click one of the refund reason");
        adminCareDetailPage.selectRefundReasonByName(reason);

        step("Open the collapsible estimation details section");
        if (Objects.equals(refundMethod, "Bank Transfer")) {
            adminCareDetailPage.scrollToRefundMethod();
            adminCareDetailPage.clickSpecialCaseCheckBox();
        }

        adminCareDetailPage.clickSeeDetailsEstimationAmount();
        adminCareDetailPage.scrollToRefundMethod();

        step("Verify refund method is " + refundMethod + " and unique code is present");
        Assertion.assertThat("Unique code is included in the refund estimation", adminCareDetailPage.isElementInFeesChargedPresent("Unique Code"), is(true));
        Assertion.assertThat("Estimated Refund method is " + refundMethod + " and correct amount", Integer.parseInt(adminCareDetailPage.getEstimatedRefundAmountPerMethod(refundMethod)), greaterThan(0));
        Assertion.assertThat("Refund method is " + refundMethod, adminCareDetailPage.getRefundMethodsRefundForm(), hasItem(refundMethod));
    }

    @Author(names = {"Fery"})
    @Module(name = "RefundForm")
    @Tribe(name = "Refund")
    @Vertical(name = "PROP")
    @Test(dataProvider = "testPartialRefundUniqueCodeDataProvider")
    public void testPartialRefundUniqueCode(Map<String, String> param) {
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        AdminCarePage<?> adminCarePage = new AdminCarePage<>(session);
        AdminCareDetailPage<?> adminCareDetailPage = new AdminCareDetailPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String orderId = param.get("orderId");
        String filterName = param.get("filterName");
        String dropdownName = param.get("dropdownName");
        String moreInActionItem = param.get("moreInActionItem");
        String refundMethod = param.get("refundMethod");
        String isUniqueCode = param.get("isUniqueCode");
        int reason = Integer.parseInt(param.get("reason"));

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

        step("Go to the refund cancel tab");
        adminCareDetailPage.clickRefundCancelTab();

        step("Click E-Refund by CS Button");
        adminCareDetailPage.clickERefundByCsButton();

        step("Click one of the refund reason");
        adminCareDetailPage.selectRefundReasonValue(reason);

        step("Select partial passengers");
        adminCareDetailPage.selectPax(1);

        step("Open the collapsible estimation details section");
        if (Objects.equals(refundMethod, "Bank Transfer")) {
            adminCareDetailPage.scrollToRefundMethod();
            adminCareDetailPage.clickSpecialCaseCheckBox();
        }

        adminCareDetailPage.clickSeeDetailsEstimationAmount();
        adminCareDetailPage.scrollToRefundMethod();

        step("Verify refund method is " + refundMethod + " and presence of unique code");
        Assertion.assertThat("Unique code included in the refund estimation?", adminCareDetailPage.isUniqueCodeCalculated(), is(Boolean.parseBoolean(isUniqueCode)));
        Assertion.assertThat("Refund method is " + refundMethod + " in the refund estimation", adminCareDetailPage.isEstimatedRefundMethodCorrect(refundMethod), is(true));
        Assertion.assertThat("Refund method is " + refundMethod, adminCareDetailPage.isRefundMethodCorrect(refundMethod), is(true));
    }

    @DataProvider(name = "testPartialRefundUniqueCodeDataProvider")
    public Object[][] testPartialRefundUniqueCodeDataProvider() {
        return getObjectParams("testPartialRefundUniqueCode");
    }

    @Author(names = {"Fery", "NaufalF"})
    @Module(name = "RefundForm")
    @Tribe(name = "Refund")
    @Vertical(name = "PROP")
    @Test(dataProvider = "testFullRefundPaylaterDataProvider")
    public void testFullRefundPaylater(Map<String, String> param) {
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        AdminCarePage<?> adminCarePage = new AdminCarePage<>(session);
        AdminCareDetailPage<?> adminCareDetailPage = new AdminCareDetailPage<>(session);
        AccountPage<?> accountPage = new AccountPage<>(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String userConsumer = param.get("userConsumer");
        String passConsumer = param.get("passConsumer");
        String filterName = param.get("filterName");
        String dropdownName = param.get("dropdownName");
        String moreInActionItem = param.get("moreInActionItem");
        String refundMethod = param.get("refundMethod");
        int reasonIndex = Integer.parseInt(param.get("reason"));
        String paylaterName = param.get("paylaterName");

        step("Get api access token");
        String apiAccessToken = "Bearer " + accountPage.getAccessTokenUNM(userConsumer, passConsumer);
        String accountId = String.valueOf(corporateApi.getAccountId(apiAccessToken.split(" ", 2)[1]));

        step("Check order present in order list");
        MapVerticalWithOrderId.TitleAndOrderId activeOrderData = orderListPage.findRefundableOrderByRefundMethod(apiAccessToken, userConsumer, accountId, true, paylaterName.toLowerCase());
        if (activeOrderData == null) {
            Assertion.skip("can't find any order id with refund method: " + paylaterName.toLowerCase());
        }

        step("Login to Admin Care Dashboard");
        adminCarePage.goToAdminCarePage(username, password);

        step("Search the Order Id");
        adminCarePage.selectFilterByName(filterName);
        adminCarePage.selectDropdownMenu(dropdownName);
        adminCarePage.inputKeyword(activeOrderData.orderId);
        adminCarePage.clickSearch();

        step("Open order details");
        adminCarePage.clickMoreInActionColumn();
        adminCarePage.selectInMoreInActionColumn(moreInActionItem);

        step("Go to the refund cancel tab");
        adminCareDetailPage.clickRefundCancelTab();

        step("Click E-Refund by CS Button");
        adminCareDetailPage.clickERefundByCsButton();

        step("Click one of the refund reason");
        adminCareDetailPage.selectRefundReasonValue(reasonIndex);

        step("Open the collapsible estimation details section");
        adminCareDetailPage.clickSeeDetailsEstimationAmount();
        adminCareDetailPage.scrollToRefundMethod();

        step("Verify refund method is " + paylaterName);
        String breakdownMethod = paylaterName.equals("Pay_later") ? refundMethod : paylaterName;
        Assertion.assertThat("Refund method is " + breakdownMethod + " in the refund estimation", adminCareDetailPage.isEstimatedRefundMethodCorrect(breakdownMethod), is(true));
        Assertion.assertThat("Refund method is " + refundMethod, adminCareDetailPage.isRefundMethodCorrect(refundMethod), is(true));
        Assertion.assertThat("PayLater Name is " + paylaterName, adminCareDetailPage.isPaylaterNameCorrect(paylaterName), is(true));
    }

    @DataProvider(name = "testFullRefundPaylaterDataProvider")
    public Object[][] testFullRefundPaylaterDataProvider() {
        return getObjectParams("testFullRefundPaylater");
    }

    @Author(names = {"NaufalF"})
    @Module(name = "Refund List")
    @Tribe(name = "Refund")
    @Vertical(name = "PROP")
    @Test(dataProvider = "testSearchByRequestId")
    public void testSearchByRequestId(Map<String, String> param) throws Exception {
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        RefundListPage<?> refundListPage = new RefundListPage<>(session);
        AdminCarePage<?> adminCarePage = new AdminCarePage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String tabName = param.get("tabName");
        String vertical = param.get("vertical");
        String vendor = param.get("vendor");

        step("Get Refund Request ID Data from DB");
        String refundId = refundListPage.getRequestIdFromDB(vertical, vendor);

        step("Login to Admin Care");
        adminCarePage.goToAdminCarePage(username, password);

        step("Go To Refund List Page");
        adminCarePage.goToRefundPage();
        refundListPage.goToRefundList();

        step("Go to Tab " + tabName + " and verify");
        refundListPage.goToRefundListTab(tabName);
        refundListPage.searchRefundByRefundId(refundId);

        step("Verify Refund Request ID shown");
        Assertion.assertThat("Verify refund request id shown", refundListPage.verifyFilterRequestId(refundId), is(true));
    }

    @DataProvider(name = "testSearchByRequestId")
    public Object[][] testSearchByRequestId() {
        return getObjectParams("testSearchByRequestId");
    }

    @Author(names = {"Fery", "NaufalF"})
    @Module(name = "RefundForm")
    @Tribe(name = "Refund")
    @Vertical(name = "PROP")
    @Test
    public void testFullRefundInstallmentServiceFee() {
        Map<String, String> param = getParam("testFullRefundInstallmentServiceFee");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        AdminCarePage<?> adminCarePage = new AdminCarePage<>(session);
        AdminCareDetailPage<?> adminCareDetailPage = new AdminCareDetailPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String orderId = param.get("orderId");
        String filterName = param.get("filterName");
        String dropdownName = param.get("dropdownName");
        String moreInActionItem = param.get("moreInActionItem");
        String refundMethod = param.get("refundMethod");
        String reason = param.get("reason");

        step("Login to Admin Care Dashboard");
        adminCarePage.goToAdminCarePage(username, password);

        step("Search the Order Id");
        adminCarePage.selectFilterByName(filterName);
        adminCarePage.selectDropdownMenu(dropdownName);
        adminCarePage.inputKeyword(orderId);
        adminCarePage.clickSearch();

        step("Open order details");
        adminCarePage.clickMoreInActionColumn();
        adminCarePage.selectInMoreInActionColumn(moreInActionItem);

        step("Go to the refund cancel tab");
        adminCareDetailPage.clickRefundCancelTab();

        step("Click E-Refund by CS Button");
        adminCareDetailPage.clickERefundByCsButton();

        step("Click one of the refund reason");
        adminCareDetailPage.selectRefundReasonByName(reason);

        step("Open the collapsible estimation details section");
        adminCareDetailPage.clickSeeDetailsEstimationAmount();
        adminCareDetailPage.scrollToRefundMethod();

        step("Verify refund method is " + refundMethod);
        Assertion.assertThat("Installment fee/Service Charge should not included in e-refund", adminCareDetailPage.isElementInFeesChargedPresent("Service Charge"), is(false));
        Assertion.assertThat("Installment fee/Service fee should not included in e-refund", adminCareDetailPage.isElementInFeesChargedPresent("Transaction Fee"), is(false));
        Assertion.assertThat("Estimated Refund method is " + refundMethod + " and correct amount", Integer.parseInt(adminCareDetailPage.getEstimatedRefundAmountPerMethod(refundMethod)), greaterThan(0));
        Assertion.assertThat("Refund method is " + refundMethod, adminCareDetailPage.getRefundMethodsRefundForm(), hasItem(refundMethod));
    }

    @Author(names = {"Fery"})
    @Module(name = "RefundForm")
    @Tribe(name = "Refund")
    @Vertical(name = "PROP")
    @Test(dataProvider = "testPartialRefundHalfPromoGVDataProvider")
    public void testPartialRefundHalfPromoGV(Map<String, String> param) {
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        AdminCarePage<?> adminCarePage = new AdminCarePage<>(session);
        AdminCareDetailPage<?> adminCareDetailPage = new AdminCareDetailPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String orderId = param.get("orderId");
        String filterName = param.get("filterName");
        String dropdownName = param.get("dropdownName");
        String moreInActionItem = param.get("moreInActionItem");
        String refundMethod = param.get("refundMethod");
        String currency = param.get("currency");
        int reason = Integer.parseInt(param.get("reason"));

        step("Login to Admin Care Dashboard");
        adminCarePage.goToAdminCarePage(username, password);

        step("Search the Order Id");
        adminCarePage.selectFilterByName(filterName);
        adminCarePage.selectDropdownMenu(dropdownName);
        adminCarePage.inputKeyword(orderId);
        adminCarePage.clickSearch();

        step("Open order details");
        adminCarePage.clickMoreInActionColumn();
        adminCarePage.selectInMoreInActionColumn(moreInActionItem);

        step("Get discount type & value");
        adminCareDetailPage.clickTransactionInvoiceTab();
        String discountType = adminCareDetailPage.getDiscountType();
        int discountValue = adminCareDetailPage.getDiscountValue();

        step("Go to the refund cancel tab");
        adminCareDetailPage.clickRefundCancelTab();

        step("Click E-Refund by CS Button");
        adminCareDetailPage.clickERefundByCsButton();

        step("Click one of the refund reason");
        adminCareDetailPage.selectRefundReasonValue(reason);

        step("Select first two passengers and the reason");
        adminCareDetailPage.selectPax(1);

        step("Open the collapsible estimation details section");
        if (Objects.equals(refundMethod, "Bank Transfer")) {
            adminCareDetailPage.scrollToRefundMethod();
            adminCareDetailPage.clickSpecialCaseCheckBox();
        }

        adminCareDetailPage.clickSeeDetailsEstimationAmount();
        adminCareDetailPage.scrollToRefundMethod();

        step("Verify refund method is " + refundMethod + " deduction is prorated");
        int deduction = adminCareDetailPage.getRefundDiscountDeduction(discountType);
        Assertion.assertThat("Discount amount is pro rated: " + deduction, deduction, is(discountValue / 2));
        Assertion.assertThat("Currency is IDR", adminCareDetailPage.checkCurrencyRefundEstimation(currency), is(true));
        Assertion.assertThat("Refund method is " + refundMethod + " in the refund estimation", adminCareDetailPage.isEstimatedRefundMethodCorrect(refundMethod), is(true));
        Assertion.assertThat("Refund method is " + refundMethod, adminCareDetailPage.isRefundMethodCorrect(refundMethod), is(true));
    }

    @DataProvider(name = "testPartialRefundHalfPromoGVDataProvider")
    public Object[][] testPartialRefundHalfPromoGVDataProvider() {
        return getObjectParams("testPartialRefundHalfPromoGV");
    }

    @Author(names = {"Fery"})
    @Module(name = "RefundForm")
    @Tribe(name = "Refund")
    @Vertical(name = "PROP")
    @Test(dataProvider = "testPartialRefundHalfTiketPointsProvider")
    public void testPartialRefundHalfTiketPoints(Map<String, String> param) {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        AdminCarePage<?> adminCarePage = new AdminCarePage<>(session);
        AdminCareDetailPage<?> adminCareDetailPage = new AdminCareDetailPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String orderId = param.get("orderId");
        String filterName = param.get("filterName");
        String dropdownName = param.get("dropdownName");
        String moreInActionItem = param.get("moreInActionItem");
        String refundMethod = param.get("refundMethod");
        int reason = Integer.parseInt(param.get("reason"));

        step("Login to Admin Care Dashboard");
        adminCarePage.goToAdminCarePage(username, password);

        step("Search the Order Id");
        adminCarePage.selectFilterByName(filterName);
        adminCarePage.selectDropdownMenu(dropdownName);
        adminCarePage.inputKeyword(orderId);
        adminCarePage.clickSearch();

        step("Open order details");
        adminCarePage.clickMoreInActionColumn();
        adminCarePage.selectInMoreInActionColumn(moreInActionItem);

        step("Get discount type & value");
        adminCareDetailPage.clickTransactionInvoiceTab();
        String discountType = adminCareDetailPage.getDiscountType();
        int discountValue = adminCareDetailPage.getDiscountValue();

        step("Go to the refund cancel tab");
        adminCareDetailPage.clickRefundCancelTab();
        int refundHistoryTixPoints = adminCareDetailPage.getRefundHistoryTixPoint();

        step("Click E-Refund by CS Button");
        adminCareDetailPage.clickERefundByCsButton();

        step("Click one of the refund reason");
        adminCareDetailPage.selectRefundReasonValue(reason);

        step("Select first two passengers and the reason");
        adminCareDetailPage.selectPax(1);

        step("Open the collapsible estimation details section");
        if (Objects.equals(refundMethod, "Bank Transfer")) {
            adminCareDetailPage.scrollToRefundMethod();
            adminCareDetailPage.clickSpecialCaseCheckBox();
        }

        adminCareDetailPage.clickSeeDetailsEstimationAmount();
        adminCareDetailPage.scrollToRefundMethod();

        step("Verify refund method and calculation is correct");
        Assertion.assertThat("Refund method & calculation is correct",
                adminCareDetailPage.isRefundMethodCalculationPartialTiketPointsCorrect(
                        refundMethod,
                        discountType,
                        discountValue,
                        refundHistoryTixPoints
                ), is(true));
    }

    @DataProvider(name = "testPartialRefundHalfTiketPointsProvider")
    public Object[][] testPartialRefundHalfTiketPointsProvider() {
        return getObjectParams("testPartialRefundHalfTiketPoints");
    }

    @Author(names = {"Fery", "Naufal"})
    @Module(name = "RefundForm")
    @Tribe(name = "Refund")
    @Vertical(name = "PROP")
    @Test(dataProvider = "testCantSubmitRefundProvider")
    public void testCantSubmitRefund(Map<String, String> param) {
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        AdminCarePage<?> adminCarePage = new AdminCarePage<>(session);
        AdminCareDetailPage<?> adminCareDetailPage = new AdminCareDetailPage<>(session);
        AccountPage<?> accountPage = new AccountPage<>(session);
        orderCreationPage<?> orderCreationPage = new orderCreationPage<>(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String userConsumer = param.get("userConsumer");
        String passConsumer = param.get("passConsumer");
        String filterName = param.get("filterName");
        String dropdownName = param.get("dropdownName");
        String moreInActionItem = param.get("moreInActionItem");
        int reason = Integer.parseInt(param.get("reason"));

        String hotelName = param.get("hotelName");
        String startDate = generateRandomDate(param.get("rangeAddDate"));
        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = getRandomObject(salutationList);

        step("Get api access token");
        String apiAccessToken = "Bearer " + accountPage.getAccessTokenUNM(userConsumer, passConsumer);
        String accountId = String.valueOf(corporateApi.getAccountId(apiAccessToken.split(" ", 2)[1]));

        step("Check order present in order list");
        MapVerticalWithOrderId.TitleAndOrderId activeOrderData = orderListPage.findRefundableOrderByRefundMethod(apiAccessToken, userConsumer, accountId, true, "bank_transfer");

        step("Fetch Hotel order with issued status, else create order");
        String bookedOrderID = orderListPage.getHotelBookedOrderId(orderCreationPage, activeOrderData, userConsumer, passConsumer, startDate, hotelName, 1, 1, 1, orderFullName, orderNameTitle, guestFullName, guestTitle);

        step("Login to Admin Care Dashboard");
        adminCarePage.goToAdminCarePage(username, password);

        step("Search the Order Id");
        adminCarePage.selectFilterByName(filterName);
        adminCarePage.selectDropdownMenu(dropdownName);
        adminCarePage.inputKeyword(bookedOrderID);
        adminCarePage.clickSearch();

        step("Open order details");
        adminCarePage.clickMoreInActionColumn();
        adminCarePage.selectInMoreInActionColumn(moreInActionItem);

        step("Go to the refund cancel tab");
        adminCareDetailPage.clickRefundCancelTab();

        step("Click E-Refund by CS Button");
        adminCareDetailPage.clickERefundByCsButton();

        step("Click one of the refund reason");
        adminCareDetailPage.selectRefundReasonValue(reason);

        step("Check if refund request button is enabled");
        Assertion.assertThat("Refund request button is disabled", adminCareDetailPage.isRequestRefundButtonEnabled(), is(false));
    }

    @DataProvider(name = "testCantSubmitRefundProvider")
    public Object[][] testCantSubmitRefundProvider() {
        return getObjectParams("testCantSubmitRefund");
    }

    @Author(names = {"Fery"})
    @Module(name = "RefundForm")
    @Tribe(name = "Refund")
    @Vertical(name = "PROP")
    @Test
    public void inputNewBankTransferAccount() {
        Map<String, String> param = getParam("inputNewBankTransferAccount");
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
        String emailUser = param.get("emailUser");
        String passUser = param.get("passUser");
        String filterName = param.get("filterName");
        String dropdownName = param.get("dropdownName");
        String moreInActionItem = param.get("moreInActionItem");
        String bankName = param.get("bankName");
        String accountNumber = param.get("accountNumber");
        int reason = Integer.parseInt(param.get("reason"));

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
        String apiAccessToken = "Bearer " + accountPage.getAccessTokenUNM(emailUser, passUser);
        String accountId = String.valueOf(corporateApi.getAccountId(apiAccessToken.split(" ", 2)[1]));

        step("Fetch order with refund method blipay on order list, else create order");
        MapVerticalWithOrderId.TitleAndOrderId activeOrderData = orderListPage.findRefundableOrderByRefundMethod(apiAccessToken, emailUser, accountId, true, "bank_transfer");
        String orderId = orderListPage.getHotelBookedOrderId(orderCreationPage, activeOrderData, emailUser, passUser, startDate, hotelName, adult, night, room, orderFullName, orderNameTitle, guestFullName, guestTitle);

        step("Login to Admin Care Dashboard");
        adminCarePage.goToAdminCarePage(username, password);

        step("Search the Order Id");
        adminCarePage.selectFilterByName(filterName);
        adminCarePage.selectDropdownMenu(dropdownName);
        adminCarePage.inputKeyword(orderId);
        adminCarePage.clickSearch();

        step("Open order details");
        adminCarePage.clickMoreInActionColumn();
        adminCarePage.selectInMoreInActionColumn(moreInActionItem);

        step("Go to the refund cancel tab");
        adminCareDetailPage.clickRefundCancelTab();

        step("Click E-Refund by CS Button");
        adminCareDetailPage.clickERefundByCsButton();

        step("Click one of the refund reason");
        adminCareDetailPage.selectRefundReasonValue(reason);

        step("Input new bank details");
        adminCareDetailPage.scrollToRefundMethod();
        adminCareDetailPage.clickSpecialCaseCheckBox();
        adminCareDetailPage.selectBankName(bankName);
        adminCareDetailPage.inputAccountNumber(accountNumber);

        step("Check if refund request button is enabled");
        Assertion.assertThat("Bank Account Name appear", adminCareDetailPage.getBankAccountHolderName(), not(emptyOrNullString()));
        Assertion.assertThat("Refund request button is enabled", adminCareDetailPage.isRequestRefundButtonEnabled(), is(true));
    }

    @Author(names = {"Fery", "NaufalF"})
    @Module(name = "RefundForm")
    @Tribe(name = "Refund")
    @Vertical(name = "PROP")
    @Test
    public void testInputSavedBankTransferAccount() {
        Map<String, String> param = getParam("testInputSavedBankTransferAccount");
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
        String emailUser = param.get("emailUser");
        String passUser = param.get("passUser");
        String filterName = param.get("filterName");
        String dropdownName = param.get("dropdownName");
        String moreInActionItem = param.get("moreInActionItem");
        int reason = Integer.parseInt(param.get("reason"));

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
        String apiAccessToken = "Bearer " + accountPage.getAccessTokenUNM(emailUser, passUser);
        String accountId = String.valueOf(corporateApi.getAccountId(apiAccessToken.split(" ", 2)[1]));

        step("Fetch order with refund method blipay on order list, else create order");
        MapVerticalWithOrderId.TitleAndOrderId activeOrderData = orderListPage.findRefundableOrderByRefundMethod(apiAccessToken, emailUser, accountId, true, "bank_transfer");
        String orderId = orderListPage.getHotelBookedOrderId(orderCreationPage, activeOrderData, emailUser, passUser, startDate, hotelName, adult, night, room, orderFullName, orderNameTitle, guestFullName, guestTitle);

        step("Login to Admin Care Dashboard");
        adminCarePage.goToAdminCarePage(username, password);

        step("Search the Order Id");
        adminCarePage.selectFilterByName(filterName);
        adminCarePage.selectDropdownMenu(dropdownName);
        adminCarePage.inputKeyword(orderId);
        adminCarePage.clickSearch();

        step("Open order details");
        adminCarePage.clickMoreInActionColumn();
        adminCarePage.selectInMoreInActionColumn(moreInActionItem);

        step("Go to the refund cancel tab");
        adminCareDetailPage.clickRefundCancelTab();

        step("Click E-Refund by CS Button");
        adminCareDetailPage.clickERefundByCsButton();

        step("Choose room for refund");
        adminCareDetailPage.selectRoomAndConfirmRefundPopup();

        step("Click one of the refund reason");
        adminCareDetailPage.selectRefundReasonValue(reason);

        step("Click saved bank account and pick one");
        adminCareDetailPage.scrollToRefundMethod();
        adminCareDetailPage.selectRefundMethodOption("Bank Transfer");
        adminCareDetailPage.clickSmartPayButton();
        adminCareDetailPage.clickFirstSavedAccount();
        adminCareDetailPage.clickSaveButton();

        step("Check if refund request button is enabled");
        Assertion.assertThat("Refund request button is enabled", adminCareDetailPage.isRequestRefundButtonEnabled(), is(true));
    }

    @Author(names = {"NaufalF"})
    @Module(name = "Refund List")
    @Tribe(name = "Refund")
    @Vertical(name = "PROP")
    @Test
    public void testDownloadCSVReport() {
        Map<String, String> param = getParam("testDownloadCSVReport");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        RefundListPage<?> refundListPage = new RefundListPage<>(session);
        AdminCarePage<?> adminCarePage = new AdminCarePage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String tabName = param.get("tabName");
        String timeOutMiliSec = param.get("timeOutMiliSec");

        step("Login to Admin Care");
        adminCarePage.goToAdminCarePage(username, password);

        step("Go To Refund List Page and a Tab");
        refundListPage.goToRefundList();
        refundListPage.goToRefundListTab(tabName);

        step("Click Export CSV for vertical " + tabName);
        refundListPage.clickExportCSV();

        step("Check Downloaded File");
        Assertion.assertThat("Verify downloaded file", refundListPage.verifyDownloadedExportCSV(tabName, timeOutMiliSec), is(true));
    }

    @Author(names = {"NaufalF"})
    @Module(name = "RefundForm")
    @Tribe(name = "Refund")
    @Vertical(name = "PROP")
    @Test
    public void testSubmitRefundFormHotel() throws Exception {
        Map<String, String> param = getParam("testSubmitRefundFormHotel");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        RefundListPage<?> refundListPage = new RefundListPage<>(session);
        AdminCarePage<?> adminCarePage = new AdminCarePage<>(session);
        AdminCareDetailPage<?> adminCareDetailPage = new AdminCareDetailPage<>(session);
        orderCreationPage<?> orderCreation = new orderCreationPage<>(session);
        AccountPage<?> accountPage = new AccountPage<>(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String userConsumer = param.get("userConsumer");
        String passConsumer = param.get("passConsumer");
        String startDate = generateRandomDate(param.get("rangeAddDate"));
        String hotelName = param.get("hotelName");
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));
        int room = Integer.parseInt(param.get("room"));
        int maxRetry = Integer.parseInt(param.get("maxRetry"));
        String note = param.get("note");
        int reason = Integer.parseInt(param.get("reason"));

        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = salutationList[0];
        String expectedCurr = param.get("expectedCurr");

        step("Get api access token");
        String apiAccessToken = "Bearer " + accountPage.getAccessTokenUNM(userConsumer, passConsumer);
        String accountId = String.valueOf(corporateApi.getAccountId(apiAccessToken.split(" ", 2)[1]));

        step("Fetch refundable hotel order, else create order");
        MapVerticalWithOrderId.TitleAndOrderId activeOrderData = orderListPage.findFirstOrderFilterByVertical(apiAccessToken, userConsumer, accountId, "hotel", expectedCurr, true, false, OrderStatus.ETICKET_ISSUED);
        String issuedOrderID = orderListPage.getHotelBookedOrderId(orderCreation, activeOrderData, userConsumer, passConsumer, startDate, hotelName, adult, night, room, orderFullName, orderNameTitle, guestFullName, guestTitle);
        GeneralVerticalInternalOrderDetail orderData = orderListPage.getVerticalInternalOrderDtata(orderListPage.getInternalOrderData(issuedOrderID));
        String orderDetailId = orderListPage.getCorrectVerticalOrderDetailId(orderData);
        hotelName = orderData.data.orderDetails.stream().filter(orderDetail -> orderDetailId.equalsIgnoreCase(orderDetail.orderDetailId)).findFirst().get().orderName;
        JSONObject verticalData = orderDetailPage.getVerticalDataObject(issuedOrderID);
        adult = verticalData.getInt("bookingAdult");
        room = verticalData.getInt("bookingRoom");

        step("Login to Admin Care");
        adminCarePage.goToAdminCarePage(username, password);

        step("Search and Open the Order Id");
        adminCarePage.searchOrderId(issuedOrderID, maxRetry);
        adminCarePage.selectOrderListId(0);

        step("Open Refund Form dialog");
        adminCareDetailPage.clickRefundCancelTab();
        adminCareDetailPage.clickERefundByCsButton();
        adminCareDetailPage.selectRoomAndConfirmRefundPopup();

        step("Select Reason Refund");
        adminCareDetailPage.selectRefundReasonValue(reason);
        adminCareDetailPage.clickSeeDetailsEstimationAmount();

        step("Verify Estimation amount and method");
        String totalRefundAmount = adminCareDetailPage.getTotalRefundAmount();
        int breakdownPayment = adminCareDetailPage.getFRPriceBreakdown("Payment");
        int breakdownFee = adminCareDetailPage.getFRPriceBreakdown("Fees Charged");
        String breakdownEstimated = expectedCurr + " " + CoreFunctions.formatNumberBasedOnLanguage(String.valueOf(adminCareDetailPage.getFRPriceBreakdown("Estimated Refund")), "en");
        String totalBreakdown = expectedCurr + " " + CoreFunctions.formatNumberBasedOnLanguage(String.valueOf(breakdownPayment - breakdownFee), "en");
        Assertion.assertThat("Total Refund Amount is Correct", totalRefundAmount, is(totalBreakdown));
        Assertion.assertThat("Refund Method Amount is Correct", totalRefundAmount, is(breakdownEstimated));

        step("Open Terms and Condition");
        Assertion.assertThat("is TNC opened correctly", adminCareDetailPage.openTNCRefundForm(false), is(true));

        step("Submit Refund Form");
        adminCareDetailPage.submitRefundForm(note);
        Assertion.assertThat("is Success Submit", adminCareDetailPage.verifyFlashSuccessSubmitRefund(), is(true));

        step("Verify Submitted Refund Form on Refund List");
        adminCareDetailPage.closeChooseRoomPopupIfPresent();
        adminCareDetailPage.waitForRefundFormToDisappear();
        adminCareDetailPage.goToRefundPage();
        refundListPage.goToRefundList();
        refundListPage.goToRefundListTab("Hotel");
        refundListPage.searchUsingFilterSearchBy("Order ID", issuedOrderID);
        Assertion.assertThat("is Refund Type Correct", refundListPage.verifyRefundTypeList("erefund"), is(true));

        step("Verify Data in DB");
        boolean isDBDataCorrect = refundListPage.verifyRefundRequestOnDB(issuedOrderID, "refund_form");
        Assertion.assertThat("is DB Data Correct", isDBDataCorrect, is(true));
    }

    @Author(names = {"GibranD"})
    @Module(name = "Refund List")
    @Tribe(name = "Refund")
    @Vertical(name = "PROP")
    @Test(dataProvider = "testVerifyRefundTypeErefundDataProvider")
    public void testVerifyRefundTypeErefund(Map<String, String> param) throws Exception {
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        RefundListPage<?> refundListPage = new RefundListPage<>(session);
        AdminCarePage<?> adminCarePage = new AdminCarePage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String orderId = param.get("orderId");
        String tabName = param.get("tabName");

        step("Login to Admin Care");
        adminCarePage.goToAdminCarePage(username, password);

        step("Go to Refund List Page");
        refundListPage.goToRefundList();

        step("Go to refund list " + tabName + " and Search Data");
        refundListPage.goToRefundListTab(tabName);
        refundListPage.searchRefundByOrderId(orderId);

        step("Verify the Data");
        Assertion.assertThat("is Refund Type Correct", refundListPage.verifyRefundTypeList("erefund"), is(true));
        boolean isDBDataCorrect = refundListPage.verifyRefundRequestOnDB(orderId, "erefund");
        Assertion.assertThat("is DB Data Correct", isDBDataCorrect, is(true));
    }

    @DataProvider(name = "testVerifyRefundTypeErefundDataProvider")
    public Object[][] testVerifyRefundTypeErefunDataProvider() {
        return getObjectParams("testVerifyRefundTypeErefund");
    }

    @Author(names = {"Fery"})
    @Module(name = "RefundForm")
    @Tribe(name = "Refund")
    @Vertical(name = "PROP")
    @Test(dataProvider = "testFullRefundNonCcDcDataProvider")
    public void testFullRefundNonCcDc(Map<String, String> param) throws InterruptedException {
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        AdminCarePage<?> adminCarePage = new AdminCarePage<>(session);
        AdminCareDetailPage<?> adminCareDetailPage = new AdminCareDetailPage<>(session);
        orderCreationPage<?> orderCreationPage = new orderCreationPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String moreInActionItem = param.get("moreInActionItem");
        String userConsumer = param.get("userConsumer");
        String passConsumer = param.get("passConsumer");
        String startDate = generateRandomDate(param.get("rangeAddDate"));
        String hotelName = param.get("hotelName");
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));
        int room = Integer.parseInt(param.get("room"));
        String refundMethod = param.get("refundMethod");
        int reason = Integer.parseInt(param.get("reason"));

        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = salutationList[0];

        step("Create new hotel issued order");
        orderCreationPage.createHotelSakukuIssuedOrderApi(userConsumer, passConsumer, startDate, hotelName,
                adult, night, room, orderFullName, orderNameTitle, guestFullName, guestTitle, "IDR");

        step("Get data from the issued order");
        String issuedOrderID = String.valueOf(orderCreationPage.getOrderId());

        step("Login to Admin Care Dashboard");
        adminCarePage.goToAdminCarePage(username, password);

        step("Search the Order Id");
        adminCarePage.searchOrderId(issuedOrderID, 5);

        step("Open order details");
        adminCarePage.clickMoreInActionColumn();
        adminCarePage.selectInMoreInActionColumn(moreInActionItem);

        step("Go to the refund cancel tab");
        adminCareDetailPage.clickRefundCancelTab();

        step("Click E-Refund by CS Button");
        adminCareDetailPage.clickERefundByCsButton();

        step("Click one of the refund reason");
        adminCareDetailPage.selectRefundReasonValue(reason);

        step("Open the collapsible estimation details section");
        if (Objects.equals(refundMethod, "Bank Transfer")) {
            adminCareDetailPage.scrollToRefundMethod();
            adminCareDetailPage.clickSpecialCaseCheckBox();
        }

        adminCareDetailPage.clickSeeDetailsEstimationAmount();
        adminCareDetailPage.scrollToRefundMethod();

        step("Verify refund method is " + refundMethod);
        Assertion.assertThat("Refund method is " + refundMethod + " in the refund estimation", adminCareDetailPage.isEstimatedRefundMethodCorrect(refundMethod), is(true));
        Assertion.assertThat("Refund method is " + refundMethod, adminCareDetailPage.isRefundMethodCorrect(refundMethod), is(true));
    }

    @DataProvider(name = "testFullRefundNonCcDcDataProvider")
    public Object[][] testFullRefundNonCcDcDataProvider() {
        return getObjectParams("testFullRefundNonCcDc");
    }

    @Author(names = {"Fery"})
    @Module(name = "RefundForm")
    @Tribe(name = "Refund")
    @Vertical(name = "PROP")
    @Test(dataProvider = "testFullRefundCcDcDataProvider")
    public void testFullRefundCcDc(Map<String, String> param) {
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        AdminCarePage<?> adminCarePage = new AdminCarePage<>(session);
        AdminCareDetailPage<?> adminCareDetailPage = new AdminCareDetailPage<>(session);
        orderCreationPage<?> orderCreationPage = new orderCreationPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String moreInActionItem = param.get("moreInActionItem");
        String userConsumer = param.get("userConsumer");
        String passConsumer = param.get("passConsumer");
        String startDate = generateRandomDate(param.get("rangeAddDate"));
        String hotelName = param.get("hotelName");
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));
        int room = Integer.parseInt(param.get("room"));
        String refundMethod = param.get("refundMethod");
        int reason = Integer.parseInt(param.get("reason"));

        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = salutationList[0];

        String currency = param.get("currency");
        String cardNumber = param.get("cardNumber");
        String expiry = param.get("expiry");
        String cvv = param.get("cvv");
        String cardName = param.get("cardName");

        step("Create new hotel issued order");
        orderCreationPage.createHotelCreditCardIssuedOrderApi(
                userConsumer, passConsumer, startDate, hotelName, adult, night, room, currency,
                orderFullName, orderNameTitle, guestFullName, guestTitle,
                cardNumber, expiry, cvv, cardName);

        step("Get data from the issued order");
        String issuedOrderID = String.valueOf(orderCreationPage.getOrderId());

        step("Login to Admin Care Dashboard");
        adminCarePage.goToAdminCarePage(username, password);

        step("Search the Order Id");
        adminCarePage.searchOrderId(issuedOrderID, 5);

        step("Open order details");
        adminCarePage.clickMoreInActionColumn();
        adminCarePage.selectInMoreInActionColumn(moreInActionItem);

        step("Go to the refund cancel tab");
        adminCareDetailPage.clickRefundCancelTab();

        step("Click E-Refund by CS Button");
        adminCareDetailPage.clickERefundByCsButton();

        step("Click one of the refund reason");
        adminCareDetailPage.selectRefundReasonValue(reason);

        step("Open the collapsible estimation details section");
        if (Objects.equals(refundMethod, "Bank Transfer")) {
            adminCareDetailPage.scrollToRefundMethod();
            adminCareDetailPage.clickSpecialCaseCheckBox();
        }

        adminCareDetailPage.clickSeeDetailsEstimationAmount();
        adminCareDetailPage.scrollToRefundMethod();

        step("Verify refund method is " + refundMethod);
        Assertion.assertThat("Refund method is " + refundMethod + " in the refund estimation", adminCareDetailPage.isEstimatedRefundMethodCorrect(refundMethod), is(true));
        Assertion.assertThat("Currency is " + currency, adminCareDetailPage.checkCurrencyRefundEstimation(currency), is(true));
        Assertion.assertThat("Refund method is " + refundMethod, adminCareDetailPage.isRefundMethodCorrect(refundMethod), is(true));
    }

    @DataProvider(name = "testFullRefundCcDcDataProvider")
    public Object[][] testFullRefundCcDcDataProvider() {
        return getObjectParams("testFullRefundCcDc");
    }

    @Author(names = {"Fery"})
    @Module(name = "RefundForm")
    @Tribe(name = "Refund")
    @Vertical(name = "PROP")
    @Test
    public void testFullRefundFullTixPoints() throws InterruptedException {
        Map<String, String> param = getParam("testFullRefundFullTixPoints");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        AdminCarePage<?> adminCarePage = new AdminCarePage<>(session);
        AdminCareDetailPage<?> adminCareDetailPage = new AdminCareDetailPage<>(session);
        orderCreationPage<?> orderCreationPage = new orderCreationPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String moreInActionItem = param.get("moreInActionItem");
        String userConsumer = param.get("userConsumer");
        String passConsumer = param.get("passConsumer");
        String startDate = generateRandomDate(param.get("rangeAddDate"));
        String hotelName = param.get("hotelName");
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));
        int room = Integer.parseInt(param.get("room"));
        String refundMethod = param.get("refundMethod");
        int reason = Integer.parseInt(param.get("reason"));
        int accountId = Integer.parseInt(param.get("accountId"));

        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = salutationList[0];

        step("Create new hotel issued order");
        orderCreationPage.createHotelFullTiketPointsIssuedOrderApi(userConsumer, passConsumer, startDate, hotelName, adult, night, room, orderFullName, orderNameTitle, guestFullName, guestTitle, accountId, "IDR");

        step("Get data from the issued order");
        String issuedOrderID = String.valueOf(orderCreationPage.getOrderId());

        step("Login to Admin Care Dashboard");
        adminCarePage.goToAdminCarePage(username, password);

        step("Search the Order Id");
        adminCarePage.searchOrderId(issuedOrderID, 5);

        step("Open order details");
        adminCarePage.clickMoreInActionColumn();
        adminCarePage.selectInMoreInActionColumn(moreInActionItem);

        step("Go to the refund cancel tab");
        adminCareDetailPage.clickRefundCancelTab();

        step("Click E-Refund by CS Button");
        adminCareDetailPage.clickERefundByCsButton();

        step("Click one of the refund reason");
        adminCareDetailPage.selectRefundReasonValue(reason);

        step("Open the collapsible estimation details section");
        if (Objects.equals(refundMethod, "Bank Transfer")) {
            adminCareDetailPage.scrollToRefundMethod();
            adminCareDetailPage.clickSpecialCaseCheckBox();
        }

        adminCareDetailPage.clickSeeDetailsEstimationAmount();
        adminCareDetailPage.scrollToRefundMethod();

        step("Verify refund method is " + refundMethod + " and unique code is present");
        Assertion.assertThat("Unique code is included in the refund estimation", adminCareDetailPage.isUniqueCodeCalculated(), is(false));
        Assertion.assertThat("Refund method is " + refundMethod + " in the refund estimation", adminCareDetailPage.isEstimatedRefundMethodCorrect(refundMethod), is(true));
        Assertion.assertThat("Refund method is " + refundMethod, adminCareDetailPage.isRefundMethodCorrect(refundMethod), is(true));
    }

    @Author(names = {"Fery"})
    @Module(name = "RefundForm")
    @Tribe(name = "Refund")
    @Vertical(name = "PROP")
    @Test
    public void testFullRefundHalfTixPoints()
            throws InterruptedException, ExecutionException {
        Map<String, String> param = getParam("testFullRefundHalfTixPoints");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        AdminCarePage<?> adminCarePage = new AdminCarePage<>(session);
        AdminCareDetailPage<?> adminCareDetailPage = new AdminCareDetailPage<>(session);
        orderCreationPage<?> orderCreationPage = new orderCreationPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String moreInActionItem = param.get("moreInActionItem");
        String userConsumer = param.get("userConsumer");
        String passConsumer = param.get("passConsumer");
        String startDate = generateRandomDate(param.get("rangeAddDate"));
        String hotelName = param.get("hotelName");
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));
        int room = Integer.parseInt(param.get("room"));
        ArrayList<String> refundMethods = new ArrayList<>(
                Arrays.asList(param.get("refundMethods").substring(1, param.get("refundMethods").length() - 1).split(","))
        );
        int pdAccountId = Integer.parseInt(param.get("pdAccountId"));
        int pdPoint = Integer.parseInt(param.get("pdPoint"));
        String pdProcessType = param.get("pdProcessType");
        int pdRatio = Integer.parseInt(param.get("pdRatio"));
        int pointsThreshold = Integer.parseInt(param.get("pointsThreshold"));
        int reason = Integer.parseInt(param.get("reason"));

        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = salutationList[0];

        step("Create new hotel issued order");
        orderCreationPage.createWaitingPaymentOrderHotel(userConsumer, passConsumer, startDate, hotelName,
                adult, night, room, orderFullName, orderNameTitle, guestFullName, guestTitle, "IDR");
        orderCreationPage.injectTiketPointWithThreshold(pdAccountId, pdProcessType, pdRatio, userConsumer, passConsumer, pointsThreshold);
        orderCreationPage.applyTiketPointsV2(orderCreationPage.getAccessToken(), orderCreationPage.getOrderId(), "TIXHOTEL");
        orderCreationPage.payWaitingPaymentHotelOrderApi();

        step("Get data from the issued order");
        String issuedOrderID = String.valueOf(orderCreationPage.getOrderId());

        step("Login to Admin Care Dashboard");
        adminCarePage.goToAdminCarePage(username, password);

        step("Search the Order Id");
        adminCarePage.searchOrderId(issuedOrderID, 5);

        step("Open order details");
        adminCarePage.clickMoreInActionColumn();
        adminCarePage.selectInMoreInActionColumn(moreInActionItem);

        step("Go to the refund cancel tab");
        adminCareDetailPage.clickRefundCancelTab();

        step("Click E-Refund by CS Button");
        adminCareDetailPage.clickERefundByCsButton();

        step("Click one of the refund reason");
        adminCareDetailPage.selectRefundReasonValue(reason);

        step("Open the collapsible estimation details section");
        if (refundMethods.contains("Bank Transfer")) {
            adminCareDetailPage.scrollToRefundMethod();
            adminCareDetailPage.clickSpecialCaseCheckBox();
        }

        adminCareDetailPage.clickSeeDetailsEstimationAmount();
        adminCareDetailPage.scrollToRefundMethod();

        Assertion.assertThat("Refund method are " + refundMethods + " in the refund estimation", adminCareDetailPage.isEstimatedRefundMethodCorrect(refundMethods), is(true));
        Assertion.assertThat("Refund method are " + refundMethods, adminCareDetailPage.isRefundMethodCorrect(refundMethods), is(true));
    }

    @Author(names = {"Fery"})
    @Module(name = "RefundForm")
    @Tribe(name = "Refund")
    @Vertical(name = "PROP")
    @Test(dataProvider = "testFullRefundPromoGVDataProvider")
    public void testFullRefundPromoGV(Map<String, String> param) throws ExecutionException, InterruptedException {
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        AdminCarePage<?> adminCarePage = new AdminCarePage<>(session);
        AdminCareDetailPage<?> adminCareDetailPage = new AdminCareDetailPage<>(session);
        orderCreationPage<?> orderCreationPage = new orderCreationPage<>(session);
        PaymentApi<?> paymentApi = new PaymentApi<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String moreInActionItem = param.get("moreInActionItem");
        String userConsumer = param.get("userConsumer");
        String passConsumer = param.get("passConsumer");
        String startDate = generateRandomDate(param.get("rangeAddDate"));
        String hotelName = param.get("hotelName");
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));
        int room = Integer.parseInt(param.get("room"));
        String refundMethod = param.get("refundMethod");
        int reason = Integer.parseInt(param.get("reason"));
        boolean isFullGV = Boolean.parseBoolean(param.get("isFullGV"));
        String currency = param.get("currency");

        String idempotentKey = orderCreationPage.generateIdempotentKey();
        String apiKey = param.get("apiKey");
        int discountAmount = Integer.parseInt(param.get("discountAmount"));
        int discountPercentage = Integer.parseInt(param.get("discountPercentage"));
        int maximumVoucherAmount = Integer.parseInt(param.get("maximumVoucherAmount"));
        int minimumTransactionAmount = Integer.parseInt(param.get("minimumTransactionAmount"));
        int cashbackAmount = Integer.parseInt(param.get("cashbackAmount"));
        int maximumVoucherCashbackAmount = Integer.parseInt(param.get("maximumVoucherCashbackAmount"));
        String discountScheme = param.get("discountScheme");
        String giftVoucherAdjustmentCode = param.getOrDefault("giftVoucherAdjustmentCode", "");
        List<String> userEmails = Arrays.asList(param.get("userEmails"));
        System.out.println("userEmails refundtest -> " + userEmails);
        List<String> validityRestriction = Arrays.asList(param.get("validityRestriction"));

        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = salutationList[0];

        step("Create new hotel issued order");
        orderCreationPage.createWaitingPaymentOrderHotel(userConsumer, passConsumer, startDate, hotelName, adult, night, room,
                orderFullName, orderNameTitle, guestFullName, guestTitle, "IDR");
        String gvCode = orderCreationPage.createGvApi(username, idempotentKey, apiKey, discountAmount, cashbackAmount, maximumVoucherCashbackAmount, discountPercentage, maximumVoucherAmount, minimumTransactionAmount, "IDR", startDate, discountScheme, giftVoucherAdjustmentCode, userEmails, validityRestriction).data.giftVoucherCode;
        String productType = orderCreationPage.getProductType();
        PaymentCorePromoApplyResponse response = paymentApi.applyPromoGv(orderCreationPage.getAccessToken(), String.valueOf(orderCreationPage.getOrderId()), orderCreationPage.getOrderHash(), gvCode, "IDR", "", productType, orderCreationPage.getGrandTotal().longValue());
        if (!response.code.equalsIgnoreCase("SUCCESS")) Assertion.skip("Failed to apply GV, skipping subsequent steps");
        orderCreationPage.payWaitingPaymentHotelOrderApi();

        step("Get data from the issued order");
        String issuedOrderID = String.valueOf(orderCreationPage.getOrderId());

        step("Login to Admin Care Dashboard");
        adminCarePage.goToAdminCarePage(username, password);

        step("Search the Order Id");
        adminCarePage.searchOrderId(issuedOrderID, 5);

        step("Open order details");
        adminCarePage.clickMoreInActionColumn();
        adminCarePage.selectInMoreInActionColumn(moreInActionItem);

        step("Get discount type & value");
        adminCareDetailPage.clickTransactionInvoiceTab();
        String discountType = adminCareDetailPage.getDiscountType();
        int discountValue = adminCareDetailPage.getDiscountValue();

        step("Go to the refund cancel tab");
        adminCareDetailPage.clickRefundCancelTab();

        step("Click E-Refund by CS Button");
        adminCareDetailPage.clickERefundByCsButton();

        if (isFullGV) {
            step("Verify order not refundable");
            Assertion.assertThat("is refund not refundable", adminCareDetailPage.isRefundFormShown(), is(false));
        } else {
            step("Click one of the refund reason");
            adminCareDetailPage.selectRefundReasonValue(reason);

            step("Open the collapsible estimation details section");
            if (refundMethod.contains("Bank Transfer")) {
                adminCareDetailPage.scrollToRefundMethod();
                adminCareDetailPage.clickSpecialCaseCheckBox();
            }

            adminCareDetailPage.clickSeeDetailsEstimationAmount();
            int deduction = adminCareDetailPage.getRefundDiscountDeduction(discountType);
            adminCareDetailPage.scrollToRefundMethod();
            Assertion.assertThat("Refund method are " + refundMethod + " in the refund estimation", adminCareDetailPage.isEstimatedRefundMethodCorrect(refundMethod), is(true));
            Assertion.assertThat("Refund method are " + refundMethod, adminCareDetailPage.isRefundMethodCorrect(refundMethod), is(true));
            Assertion.assertThat("Discount amount is deducted: " + deduction, deduction, is(discountValue));
        }
    }

    @DataProvider(name = "testFullRefundPromoGVDataProvider")
    public Object[][] testFullRefundPromoGVDataProvider() {
        return getObjectParams("testFullRefundPromoGV");
    }

    @Author(names = {"NaufalF"})
    @Module(name = "RefundForm")
    @Tribe(name = "Refund")
    @Vertical(name = "PROP")
    @Test(dataProvider = "testRefundFormTTDIDR")
    public void testSubmitRefundFormTodoIDR(Map<String, String> param) throws Exception {
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        RefundListPage<?> refundListPage = new RefundListPage<>(session);
        AdminCarePage<?> adminCarePage = new AdminCarePage<>(session);
        AdminCareDetailPage<?> adminCareDetailPage = new AdminCareDetailPage<>(session);
        orderCreationPage<?> orderCreation = new orderCreationPage<>(session);
        AccountPage<?> accountPage = new AccountPage<>(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String userConsumer = param.get("userConsumer");
        String passConsumer = param.get("passConsumer");
        String productUrlName = param.get("productUrlName");
        String note = param.get("note");
        int maxRetry = Integer.parseInt(param.get("maxRetry"));
        boolean refundable = true;
        String currency = "IDR";
        int reason = Integer.parseInt(param.get("reason"));

        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String contactFullName = randomName() + " autorder";
        String contactSalutation = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestEmailAddress = randomEmail();
        String guestSalutation = salutationList[0];

        step("Get api access token");
        String apiAccessToken = "Bearer " + accountPage.getAccessTokenUNM(userConsumer, passConsumer);
        String accountId = String.valueOf(corporateApi.getAccountId(apiAccessToken.split(" ", 2)[1]));

        step("Fetch refundable TTD order, else create order");
        MapVerticalWithOrderId.TitleAndOrderId activeOrderData = orderListPage.findFirstOrderFilterByVertical(apiAccessToken, userConsumer, accountId, "ttd", currency, true, false, OrderStatus.ETICKET_ISSUED);
        String issuedOrderID = orderListPage.getTTDBookedOrderId(orderCreation, activeOrderData, userConsumer, passConsumer, currency, productUrlName, refundable, contactFullName, contactSalutation, guestFullName, guestEmailAddress, guestSalutation);

        step("Login to Admin Care");
        adminCarePage.goToAdminCarePage(username, password);

        step("Search and Open the Order Id");
        adminCarePage.searchOrderId(issuedOrderID, maxRetry);
        adminCarePage.selectOrderListId(0);

        step("Open Refund Form dialog");
        adminCareDetailPage.clickRefundCancelTab();
        adminCareDetailPage.clickAlternateERefundByCsButton();

        step("Select Reason Refund");
        adminCareDetailPage.selectRefundReasonValue(reason);
        adminCareDetailPage.clickSeeDetailsEstimationAmount();

        step("Verify Estimation amount and method");
        String totalRefundAmount = adminCareDetailPage.getTotalRefundAmount();
        int breakdownPayment = adminCareDetailPage.getFRPriceBreakdown("Payment");
        int breakdownFee = adminCareDetailPage.getFRPriceBreakdown("Fees Charged");
        String breakdownEstimated = currency + " " + CoreFunctions.formatNumberBasedOnLanguage(String.valueOf(adminCareDetailPage.getFRPriceBreakdown("Estimated Refund")), "en");
        String totalBreakdown = currency + " " + CoreFunctions.formatNumberBasedOnLanguage(String.valueOf(breakdownPayment - breakdownFee), "en");
        Assertion.assertThat("Total Refund Amount is Correct", totalRefundAmount, is(totalBreakdown));
        Assertion.assertThat("Refund Method Amount is Correct", totalRefundAmount, is(breakdownEstimated));

        step("Open Terms and Condition");
        Assertion.assertThat("is TNC opened correctly", adminCareDetailPage.openTNCRefundForm(false), is(true));

        step("Submit Refund Form");
        adminCareDetailPage.submitRefundForm(note);
        Assertion.assertThat("is Success Submit", adminCareDetailPage.verifyFlashSuccessSubmitRefund(), is(true));

        step("Verify Submitted Refund Form on Refund List");
        adminCareDetailPage.closeChooseRoomPopupIfPresent();
        adminCareDetailPage.waitForRefundFormToDisappear();
        adminCareDetailPage.goToRefundPage();
        refundListPage.goToRefundList();
        refundListPage.goToRefundListTab("To Do");
        refundListPage.searchUsingFilterSearchBy("Order ID", issuedOrderID);
        Assertion.assertThat("is Refund Type Correct", refundListPage.verifyRefundTypeList("erefund"), is(true));

        step("Verify Data in DB");
        boolean isDBDataCorrect = refundListPage.verifyRefundRequestOnDB(issuedOrderID, "refund_form");
        Assertion.assertThat("is DB Data Correct", isDBDataCorrect, is(true));
    }

    @DataProvider(name = "testRefundFormTTDIDR")
    public Object[][] testRefundFormTTDIDR() {
        return getObjectParams("testSubmitRefundFormTodoIDR");
    }

    @Author(names = {"NaufalF"})
    @Module(name = "RefundForm")
    @Tribe(name = "Refund")
    @Vertical(name = "PROP")
    @Test
    public void testSubmitRefundFormCar() throws Exception {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        Map<String, String> param = getParam("testSubmitRefundFormCar");

        RefundListPage<?> refundListPage = new RefundListPage<>(session);
        AdminCarePage<?> adminCarePage = new AdminCarePage<>(session);
        AdminCareDetailPage<?> adminCareDetailPage = new AdminCareDetailPage<>(session);
        orderCreationPage<?> orderCreationPage = new orderCreationPage<>(session);
        AccountPage<?> accountPage = new AccountPage<>(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String userConsumer = param.get("userConsumer");
        String passConsumer = param.get("passConsumer");
        String phoneNumber = param.get("phoneNumber");
        String note = param.get("note");
        int maxRetryWait = Integer.parseInt(param.get("maxRetry"));
        int reason = Integer.parseInt(param.get("reason"));
        String currency = param.get("currency");

        String driverOption = param.get("driverOption");
        String location = param.get("location");
        String startDate = generateRandomDate(param.get("rangeAddDate"));
        String type = param.get("type");
        int rentalDuration = Integer.parseInt(param.get("rentalDuration"));
        int carCount = 1;

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
        String apiAccessToken = "Bearer " + accountPage.getAccessTokenUNM(userConsumer, passConsumer);
        String accountId = String.valueOf(corporateApi.getAccountId(apiAccessToken.split(" ", 2)[1]));

        step("Fetch refundable car rental order, else create order");
        MapVerticalWithOrderId.TitleAndOrderId activeOrderData = orderListPage.findFirstOrderFilterByVertical(apiAccessToken, userConsumer, accountId, "car", currency, true, false, OrderStatus.ETICKET_ISSUED);
        String issuedOrderID = orderListPage.getCarRentalBookedOrderId(orderCreationPage, activeOrderData, userConsumer, phoneNumber, passConsumer, driverOption, location, startDate, pickupTime, type, fullName, nameTitle, adminName, adminPhone, rentalDuration);
        JSONObject verticalData = orderDetailPage.getVerticalDataObject(issuedOrderID);
        String carName = verticalData.getString("fleetName");
        String carRentalLocation = verticalData.getString("regionalName");
        pickupTime = verticalData.getString("pickupTime");
        rentalDuration = verticalData.getInt("duration");

        step("Login to Admin Care");
        adminCarePage.goToAdminCarePage(username, password);

        step("Search and Open the Order Id");
        adminCarePage.searchOrderId(issuedOrderID, maxRetryWait);
        adminCarePage.selectOrderListId(0);
        String carStartDate = adminCareDetailPage.getCarStartDate();
        String carVendor = adminCareDetailPage.getCarStartDate();

        step("Open Refund Form dialog");
        adminCareDetailPage.clickRefundCancelTab();
        adminCareDetailPage.clickERefundByCsButton();
        boolean orderReviewBool = adminCareDetailPage.verifyOrderReviewRefundFormCar(carName, carVendor, carRentalLocation, carStartDate, pickupTime, carCount, rentalDuration, maxRetryWait);
        Assertion.assertThat("e-Refund Form Order Review is correct", orderReviewBool, is(true));

        step("Select Reason Refund");
        adminCareDetailPage.selectRefundReasonValue(reason);
        adminCareDetailPage.clickSeeDetailsEstimationAmount();

        step("Verify Estimation amount and method");
        ArrayList<String> refundMethods = adminCareDetailPage.getRefundMethodsRefundForm();
        String totalRefundAmount = adminCareDetailPage.getTotalRefundAmount();
        int breakdownPayment = adminCareDetailPage.getFRPriceBreakdown("Payment");
        int breakdownFee = adminCareDetailPage.getFRPriceBreakdown("Fees Charged");
        String breakdownEstimated = currency + " " + CoreFunctions.formatNumberBasedOnLanguage(String.valueOf(adminCareDetailPage.getFRPriceBreakdown("Estimated Refund")), "en");
        String totalBreakdown = currency + " " + CoreFunctions.formatNumberBasedOnLanguage(String.valueOf(breakdownPayment - breakdownFee), "en");
        Assertion.assertThat("Total Refund Amount is Correct", totalRefundAmount, is(totalBreakdown));
        Assertion.assertThat("Refund Method Amount is Correct", totalRefundAmount, is(breakdownEstimated));

        step("Open Terms and Condition");
        Assertion.assertThat("is TNC opened correctly", adminCareDetailPage.openTNCRefundForm(false), is(true));

        step("Submit Refund Form");
        adminCareDetailPage.submitRefundForm(note);
        Assertion.assertThat("is Success Submit", adminCareDetailPage.verifyFlashSuccessSubmitRefund(), is(true));

        step("Verify Submitted Refund Form on Refund List");
        adminCareDetailPage.goToRefundPage();
        refundListPage.goToRefundList();
        refundListPage.goToRefundListTab("Car Rental");
        refundListPage.searchUsingFilterSearchBy("Order ID", issuedOrderID);
        Assertion.assertThat("is Submitted Refund Form Shown", refundListPage.verifyOrderPair(issuedOrderID, "Order ID"), is(true));
        boolean verifyRefundDetail = refundListPage.verifyDetailsCarList(carName, carVendor, carStartDate, pickupTime, carCount, rentalDuration);
        Assertion.assertThat("is Refund Order Details correct", verifyRefundDetail, is(true));
        Assertion.assertThat("is Refund Type Correct", refundListPage.verifyRefundTypeList("erefund"), is(true));
        Assertion.assertThat("is total refund correct", totalRefundAmount, is(refundListPage.getTotalEstimationRefundDetail()));
        Assertion.assertThat("is refund breakdown payment correct", refundListPage.getRefundDetailPriceBreakdownItem("Payment"), is(breakdownPayment));
        Assertion.assertThat("is refund breakdown fee correct", refundListPage.getRefundDetailPriceBreakdownItem("Fees Charged"), is(breakdownFee));
        Assertion.assertThat("Refund methods correct", refundListPage.getRefundMethodsDetailRefund(), is(refundMethods));
        Assertion.assertThat("is Refund Description Correct", refundListPage.getRefundDescription(), is(note));

        step("Verify Data in DB");
        boolean isDBDataCorrect = refundListPage.verifyRefundRequestOnDB(issuedOrderID, "refund_form");
        Assertion.assertThat("is DB Data Correct", isDBDataCorrect, is(true));
    }

    @Author(names = {"NaufalF"})
    @Module(name = "RefundForm")
    @Tribe(name = "Refund")
    @Vertical(name = "PROP")
    @Test(dataProvider = "testRefundFormFlightOneWayAndRoundtrip")
    public void testRefundFormFlightOneWayAndRoundtrip(Map<String, String> param) {
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        AdminCarePage<?> adminCarePage = new AdminCarePage<>(session);
        AdminCareDetailPage<?> adminCareDetailPage = new AdminCareDetailPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String refundMethod = param.get("refundMethod");
        String orderId = param.get("orderId");
        boolean isOneHundredRefund = Boolean.parseBoolean(param.get("isOneHundredRefund"));
        int bankFee = Integer.parseInt(param.get("bankFee"));
        String currency = param.get("currency");
        String trip = param.get("trip");
        int reasonNum = Integer.parseInt(param.get("reasonNum"));

        String oneHundredRefund = isOneHundredRefund ? " 100p Refund" : "";

        step("Login to Admin Care");
        adminCarePage.goToAdminCarePage(username, password);

        step("Open Order Id for Flight Trip " + trip);
        adminCarePage.searchOrderId(orderId, 5);
        adminCarePage.selectOrderListId(0);
        adminCareDetailPage.selectTripType(trip);

        step("Open Refund Form dialog");
        adminCareDetailPage.clickRefundCancelTab();
        adminCareDetailPage.clickAlternateERefundByCsButton();

        step("Select Refund Reason and All Pax");
        adminCareDetailPage.selectRefundReasonValue(reasonNum);
        adminCareDetailPage.selectAllPaxAndReason(true);

        step("Open the collapsible estimation details section");
        adminCareDetailPage.scrollToRefundMethod();
        adminCareDetailPage.clickSeeDetailsEstimationAmount();
        if (Objects.equals(refundMethod, "Bank Transfer")) {
            adminCareDetailPage.clickSpecialCaseCheckBox();
        }

        step("Verify Estimation amount and method" + oneHundredRefund);
        BigInteger totalRefundAmount = new BigInteger(adminCareDetailPage.getTotalRefundAmount().replaceAll("\\D", ""));
        Assertion.assertThat("Refund Method is correct", adminCareDetailPage.isRefundMethodCorrect(refundMethod), is(true));
        Assertion.assertThat("Refund Method Amount is Correct", adminCareDetailPage.verifyRefundAmountPerMethod(totalRefundAmount, currency, refundMethod), is(true));

        step("Verify Refund Deduction" + oneHundredRefund);
        if (isOneHundredRefund) {
            Assertion.assertThat("no Ticket Fee Deduction", adminCareDetailPage.getTicketFee(), is(0));
        }
        if (Objects.equals(refundMethod, "Bank Transfer")) {
            Assertion.assertThat("is Bank Fee correct", adminCareDetailPage.verifyBankFee(bankFee, isOneHundredRefund), is(true));
        }

        step("Open Terms and Condition");
        Assertion.assertThat("is TNC opened correctly", adminCareDetailPage.openTNCRefundForm(false), is(true));
    }

    @DataProvider(name = "testRefundFormFlightOneWayAndRoundtrip")
    public Object[][] testRefundFormFlightOneWayAndRoundtrip() {
        return getObjectParams("testRefundFormFlightOneWayAndRoundtrip");
    }

    @Author(names = {"NaufalF"})
    @Module(name = "RefundForm")
    @Tribe(name = "Refund")
    @Vertical(name = "PROP")
    @Test(dataProvider = "testRefundFormFlightSmarttrip")
    public void testRefundFormFlightSmarttrip(Map<String, String> param) {
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        AdminCarePage<?> adminCarePage = new AdminCarePage<>(session);
        AdminCareDetailPage<?> adminCareDetailPage = new AdminCareDetailPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String refundMethod = param.get("refundMethod");
        String orderId = param.get("orderId");
        boolean isOneHundredRefund = Boolean.parseBoolean(param.get("isOneHundredRefund"));
        boolean isSmartTrip = true;
        int bankFee = Integer.parseInt(param.get("bankFee"));
        String currency = param.get("currency");
        boolean isFullPax = Boolean.parseBoolean(param.get("isFullPax"));
        int paxNum = Integer.parseInt(param.get("paxNum"));
        int reasonNum = Integer.parseInt(param.get("reasonNum"));

        String oneHundredRefund = isOneHundredRefund ? " 100p Refund" : "";
        String paxes = isFullPax ? "Full Pax" : "Partial Pax";

        step("Login to Admin Care");
        adminCarePage.goToAdminCarePage(username, password);

        step("Open Order Id for Flight Smarttrip");
        adminCarePage.searchOrderId(orderId, 5);
        adminCarePage.selectOrderListId(0);
        adminCareDetailPage.selectTripType("Depart");
        String orderDetailIdDepart = adminCareDetailPage.getDataTableIssuedStatus("Order Detail ID");
        String airlineNameDepart = adminCareDetailPage.getAirlineName();
        String aiportDepartCodeDepart = adminCareDetailPage.getFlightAirpotCode("Depart");
        String airportArrivalCodeDepart = adminCareDetailPage.getFlightAirpotCode("Arrival");
        String flightDepartureDateDepart = adminCareDetailPage.getFlightDateOrTime("Departure", "date");
        String flightDepartureTimeDepart = adminCareDetailPage.getFlightDateOrTime("Departure", "time");

        adminCareDetailPage.selectTripType("Return");
        String orderDetailIdReturn = adminCareDetailPage.getDataTableIssuedStatus("Order Detail ID");
        String airlineNameReturn = adminCareDetailPage.getAirlineName();
        String aiportDepartCodeReturn = adminCareDetailPage.getFlightAirpotCode("Depart");
        String airportArrivalCodeReturn = adminCareDetailPage.getFlightAirpotCode("Arrival");
        String flightDepartureDateReturn = adminCareDetailPage.getFlightDateOrTime("Departure", "date");
        String flightDepartureTimeReturn = adminCareDetailPage.getFlightDateOrTime("Departure", "time");

        step("Open Refund Form dialog");
        adminCareDetailPage.clickRefundCancelTab();
        adminCareDetailPage.clickERefundByCsButton();

        step("Verify Order Review Section Depart");
        boolean orderReviewBoolDepart = adminCareDetailPage.verifyOrderReviewRefundFormFlight(orderDetailIdDepart, airlineNameDepart,
                aiportDepartCodeDepart, airportArrivalCodeDepart, flightDepartureDateDepart, flightDepartureTimeDepart, isOneHundredRefund, isSmartTrip);
        Assertion.assertThat("Order Review Flight Depart is correct", orderReviewBoolDepart, is(true));

        step("Verify Order Review Section Return");
        boolean orderReviewBoolReturn = adminCareDetailPage.verifyOrderReviewRefundFormFlight(orderDetailIdReturn, airlineNameReturn,
                aiportDepartCodeReturn, airportArrivalCodeReturn, flightDepartureDateReturn, flightDepartureTimeReturn, isOneHundredRefund, isSmartTrip);
        Assertion.assertThat("Order Review Flight Depart is correct", orderReviewBoolReturn, is(true));

        step("Select Refund Reason and " + paxes);
        adminCareDetailPage.selectRefundReasonValue(reasonNum);
        if (isFullPax) {
            adminCareDetailPage.selectAllPaxAndReason(true);
        } else {
            adminCareDetailPage.selectPaxAndSpecificReason(paxNum);
        }

        step("Open the collapsible estimation details section");
        adminCareDetailPage.scrollToRefundMethod();
        adminCareDetailPage.clickSeeDetailsEstimationAmount();
        if (Objects.equals(refundMethod, "Bank Transfer")) {
            adminCareDetailPage.clickSpecialCaseCheckBox();
        }

        step("Verify Estimation amount and method" + oneHundredRefund);
        BigInteger totalRefundAmount = new BigInteger(adminCareDetailPage.getTotalRefundAmount().replaceAll("\\D", ""));
        Assertion.assertThat("Refund Method is correct", adminCareDetailPage.isRefundMethodCorrect(refundMethod), is(true));
        Assertion.assertThat("Refund Method Amount is Correct", adminCareDetailPage.verifyRefundAmountPerMethod(totalRefundAmount, currency, refundMethod), is(true));

        step("Verify Refund Deduction" + oneHundredRefund);
        if (isOneHundredRefund) {
            Assertion.assertThat("no Ticket Fee Deduction", adminCareDetailPage.getTicketFee(), is(0));
        }
        if (Objects.equals(refundMethod, "Bank Transfer")) {
            Assertion.assertThat("is Bank Fee correct", adminCareDetailPage.verifyBankFee(bankFee, isOneHundredRefund), is(true));
        }

        step("Open Terms and Condition");
        Assertion.assertThat("is TNC opened correctly", adminCareDetailPage.openTNCRefundForm(false), is(true));
    }

    @DataProvider(name = "testRefundFormFlightSmarttrip")
    public Object[][] testRefundFormFlightSmarttrip() {
        return getObjectParams("testRefundFormFlightSmarttrip");
    }

    @Author(names = {"NaufalF"})
    @Module(name = "Refund List")
    @Tribe(name = "Refund")
    @Vertical(name = "PROP")
    @Test(dataProvider = "testActionButtonRefundDetail")
    public void testActionButtonRefundDetail(Map<String, String> param) {
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        RefundCreation<?> refundCreation = new RefundCreation<>(session);
        RefundListPage<?> refundListPage = new RefundListPage<>(session);
        AdminCarePage<?> adminCarePage = new AdminCarePage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String vertical = param.get("vertical");
        String action = param.get("action");
        String refundAmount = param.get("refundAmount");
        boolean isForceToBankTransfer = Boolean.parseBoolean(param.get("isForceBank"));
        long orderDetailId = Long.parseLong(param.get("orderDetailId"));
        long orderId = Long.parseLong(param.get("orderId"));
        String reasonId = param.get("reasonId");
        String bankAccountName = param.get("bankName");
        String bankAccountNumber = param.get("bankNumber");
        String bankCodeTiketIdentifier = param.get("bankCode");
        String remark = param.get("remark");

        List<String> items = new ArrayList<>();

        step("Create Refund Middleware Form via API");
        String requestId = refundCreation.createFormMiddlewareApi(refundAmount, isForceToBankTransfer, items,
                orderDetailId, orderId, reasonId, bankAccountName, bankAccountNumber, bankCodeTiketIdentifier, remark, username);
        Assertion.assertThat("request Id not empty", requestId, not(blankOrNullString()));

        step("Login to Admin Care");
        adminCarePage.goToAdminCarePage(username, password);

        step("Go To Refund List page on Tab " + vertical);
        refundListPage.goToRefundList();
        refundListPage.goToRefundListTab(vertical);

        step("Open the Refund Request");
        refundListPage.searchUsingFilterSearchBy("Request ID", requestId);
        Assertion.assertThat("Verify Refund Status is open", refundListPage.verifyRefundOpenState(vertical), is(true));

        step("Receive the Refund Request");
        refundListPage.clickRefundActionButton("RECEIVED", "Receive");

        step("Reload Refund List");
        refundListPage.goToRefundList();
        refundListPage.goToRefundListTab(vertical);
        refundListPage.searchUsingFilterSearchBy("Request ID", requestId);

        step("Verify Refund Status is Reviewed by tiket.com");
        Assertion.assertThat("is Refund Status Reviewed by tiket.com", refundListPage.verifyRefundReceivedState(vertical), is(true));

        if (action.equals("reject")) {
            step("Reject the Refund Request");
            refundListPage.clickRefundActionButton(action, "Reject");

            step("Reload Refund List");
            refundListPage.goToRefundList();
            refundListPage.goToRefundListTab(vertical);
            refundListPage.searchUsingFilterSearchBy("Request ID", requestId);

            step("Verify Refund Status is Rejected");
            Assertion.assertThat("is Refund Status Rejected", refundListPage.verifyRefundRejectedState(vertical), is(true));
        } else if (action.equals("approved")) {
            step("Approve the Refund Request");
            refundListPage.clickRefundActionButton(action, "Approve");

            step("Reload Refund List");
            adminCarePage.goToAdminCarePage(username, password);
            refundListPage.goToRefundList();
            refundListPage.goToRefundListTab(vertical);
            refundListPage.searchUsingFilterSearchBy("Request ID", requestId);

            step("Verify Refund Status is Verified by tiket.com");
            Assertion.assertThat("is Refund Status Verified by tiket.com", refundListPage.verifyRefundApprovedState(vertical), is(true));
        }
    }

    @DataProvider(name = "testActionButtonRefundDetail")
    public Object[][] testActionButtonRefundDetail() {
        return getObjectParams("testActionButtonRefundDetail");
    }

    @Author(names = {"NaufalF"})
    @Module(name = "Refund List")
    @Tribe(name = "Refund")
    @Vertical(name = "PROP")
    @Test
    public void testRefund100pReschedule() {
        Map<String, String> param = getParam("testRefund100pReschedule");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        RefundListPage<?> refundListPage = new RefundListPage<>(session);
        AdminCarePage<?> adminCarePage = new AdminCarePage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String programName = param.get("programName");
        int startDay = Integer.parseInt(param.get("startDay"));
        String startMonthYear = param.get("startMonthYear");

        step("Login to Admin Care");
        adminCarePage.goToAdminCarePage(username, password);

        step("Go to Refund List Page");
        refundListPage.goToRefundList();

        step("Go to refund list tab Native Flights");
        refundListPage.goToRefundListTab("Native Flights");

        step("Filter using request date");
        refundListPage.selectTodayDate("end");
        refundListPage.clickSearchButton();

        step("Filter by program name: 100p Reschedule");
        refundListPage.clickFilterButton();
        refundListPage.selectFilterProgramName(programName);
        refundListPage.clickApplyButton();
        Assertion.assertThat("Verify filtered data refund program name", refundListPage.verifyFilterProgramName(programName), is(true));
    }

    @Author(names = {"NaufalF"})
    @Module(name = "Refund List")
    @Tribe(name = "Refund")
    @Vertical(name = "PROP")
    @Test(dataProvider = "testVerifyCreatedRefundFormDataProvider")
    public void testVerifyCreatedRefundForm(Map<String, String> param) throws Exception {
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        RefundListPage<?> refundListPage = new RefundListPage<>(session);
        AdminCarePage<?> adminCarePage = new AdminCarePage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String orderId = param.get("orderId");
        String tabName = param.get("tabName");

        step("Login to Admin Care");
        adminCarePage.goToAdminCarePage(username, password);

        step("Go to Refund List Page");
        refundListPage.goToRefundList();

        step("Go to refund list " + tabName + " and Search Data");
        refundListPage.goToRefundListTab(tabName);
        refundListPage.searchRefundByOrderId(orderId);

        step("Verify the Data");
        Assertion.assertThat("is Refund Type Correct", refundListPage.verifyRefundTypeList("erefund"), is(true));
        boolean isDBDataCorrect = refundListPage.verifyRefundRequestOnDB(orderId, "refund_form");
        Assertion.assertThat("is DB Data Correct", isDBDataCorrect, is(true));
    }

    @DataProvider(name = "testVerifyCreatedRefundFormDataProvider")
    public Object[][] testVerifyCreatedRefundFormDataProvider() {
        return getObjectParams("testVerifyCreatedRefundForm");
    }

    @Author(names = {"GibranD"})
    @Module(name = "Refund List")
    @Tribe(name = "Refund")
    @Vertical(name = "PROP")
    @Test
    public void testVerifyRefundCurrencyIDR() throws Exception {
        Map<String, String> param = getParam("testVerifyRefundCurrencyIDR");

        Session<?> session = BaseTest.sessionThreadLocal.get();
        RefundListPage<?> refundListPage = new RefundListPage<>(session);
        AdminCarePage<?> adminCarePage = new AdminCarePage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String orderId = param.get("orderId");
        String searchBy = param.get("searchBy");
        String tabName = param.get("tabName");
        String currency = param.get("currency");

        step("Login to Admin Care");
        adminCarePage.goToAdminCarePage(username, password);

        step("Go to Refund List page");
        adminCarePage.goToRefundPage();
        refundListPage.goToRefundList();

        step("Go to Tab Hotel and verify");
        refundListPage.goToRefundListTab(tabName);
        refundListPage.searchRefundByOrderId(orderId);

        step("Verify currency type in refund list");
        Assertion.assertThat("Currency is correct", refundListPage.verifyCurrencyType(currency), is(true));

        step("Verify currency type in refund detail");
        Assertion.assertThat("Currency in refund detail is correct", refundListPage.verifyCurrencyInRefundDetail(currency), is(true));
        Assertion.assertThat("Currencytype in see details refund detail is correct", refundListPage.verifyCurrencyInRefundEstimation(currency), is(true));
    }

    @Author(names = {"GibranD"})
    @Module(name = "Refund List")
    @Tribe(name = "Refund")
    @Vertical(name = "PROP")
    @Test
    public void testVerifyRefundCurrencyNonIDR() throws Exception {
        Map<String, String> param = getParam("testVerifyRefundCurrencyNonIDR");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        RefundListPage<?> refundListPage = new RefundListPage<>(session);
        AdminCarePage<?> adminCarePage = new AdminCarePage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String orderId = param.get("orderId");
        String searchBy = param.get("searchBy");
        String tabName = param.get("tabName");
        String currency = param.get("currency");

        step("Login to Admin Care");
        adminCarePage.goToAdminCarePage(username, password);

        step("Go to Refund List page");
        adminCarePage.goToRefundPage();
        refundListPage.goToRefundList();

        step("Go to Tab Hotel and verify");
        refundListPage.goToRefundListTab(tabName);
        refundListPage.searchRefundByOrderId(orderId);

        step("Verify currency type in refund list");
        Assertion.assertThat("Currency is correct", refundListPage.verifyCurrencyType(currency), is(true));

        step("Verify currency exchange rate in refund list");
        Assertion.assertThat("Exchange rate type is correct", refundListPage.verifyExchangeRate(currency), is(true));

        step("Verify currency type in refund detail");
        Assertion.assertThat("Currency in refund detail is correct", refundListPage.verifyCurrencyInRefundDetail(currency), is(true));
        Assertion.assertThat("Currency rate type in see details refund detail is correct", refundListPage.verifyCurrencyInRefundEstimation(currency), is(true));
    }

    @Author(names = {"NaufalF"})
    @Module(name = "RefundForm")
    @Tribe(name = "Refund")
    @Vertical(name = "PROP")
    @Test(dataProvider = "testRefundPaidEwalletDataProvider")
    public void testRefundPaidEwallet(Map<String, String> param) {
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        AdminCarePage<?> adminCarePage = new AdminCarePage<>(session);
        AdminCareDetailPage<?> adminCareDetailPage = new AdminCareDetailPage<>(session);
        AccountPage<?> accountPage = new AccountPage<>(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        MemberApi<?> memberApi = new MemberApi<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String userConsumer = param.get("userConsumer");
        String passConsumer = param.get("passConsumer");
        String paymentSource = param.get("paymentSource");
        String refundMethod = param.get("refundMethod");
        String refundMethodNoSpace = refundMethod.replaceAll("\\s", "");
        String currency = param.get("currency");
        int reasonIndex = Integer.parseInt(param.get("reason"));

        step("Get api access token");
        String apiAccessToken = "Bearer " + accountPage.getAccessTokenUNM(userConsumer, passConsumer);
        ProfileDetailResponse resProfile = memberApi.getProfileData(apiAccessToken);
        String accountId = String.valueOf(resProfile.data.accountId);
        String userPN = String.valueOf(resProfile.data.phoneCode) + String.valueOf(resProfile.data.phoneNumber);

        step("Fetch refundable car rental order, else create order");
        MapVerticalWithOrderId.TitleAndOrderId activeOrder = orderListPage.findFirstOrderFilterByPaymentSource(apiAccessToken, userConsumer, accountId, paymentSource, currency, true, false, OrderStatus.ETICKET_ISSUED);
        if (activeOrder == null) {
            Assertion.skip("can't find order id with payment source: " + paymentSource.toLowerCase());
        }

        step("Login to Admin Care");
        adminCarePage.goToAdminCarePage(username, password);

        step("Search and Open the Order Id");
        adminCarePage.searchOrderId(activeOrder.orderId, 5);
        adminCarePage.clickMoreInActionColumn();
        adminCarePage.selectInMoreInActionColumn("Details");

        step("Click E-Refund by CS Button");
        adminCareDetailPage.clickRefundCancelTab();

        step("Open Refund Form Dialog");
        adminCareDetailPage.clickERefundByCsButton();

        step("Click one of the refund reason");
        adminCareDetailPage.selectRefundReasonValue(reasonIndex);

        step("Open the collapsible estimation details section");
        adminCareDetailPage.clickSeeDetailsEstimationAmount();
        adminCareDetailPage.scrollToRefundMethod();

        step("Verify Refund Method " + refundMethod + " and Phone Number");
        String totalRefundAmount = adminCareDetailPage.getTotalRefundAmount().replaceAll("[^\\d-]", "");
        Assertion.assertThat("Refund Method Per Amount is Correct", adminCareDetailPage.getEstimatedRefundAmountPerMethod(refundMethod), lessThanOrEqualTo(totalRefundAmount));
        Assertion.assertThat("Refund method is correct", adminCareDetailPage.getRefundMethodsRefundForm(), hasItem(refundMethodNoSpace));
        if (refundMethod.equals("BlibliPay")) {
            Assertion.assertThat("is " + refundMethod + " Phone Number Correct", adminCareDetailPage.getBliblipayPhoneNumber(), is(userPN));
        }
    }

    @DataProvider(name = "testRefundPaidEwalletDataProvider")
    public Object[][] testRefundPaidEwalletDataProvider() {
        return getObjectParams("testRefundPaidEwallet");
    }

    @Author(names = {"NaufalF"})
    @Module(name = "RefundForm")
    @Tribe(name = "Refund")
    @Vertical(name = "PROP")
    @Test
    public void testRefundPaidCorpBilling() {
        Map<String, String> param = getParam("testRefundPaidCorpBilling");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        AdminCarePage<?> adminCarePage = new AdminCarePage<>(session);
        AdminCareDetailPage<?> adminCareDetailPage = new AdminCareDetailPage<>(session);
        PurchaseListPage<?> purchaseListPage = new PurchaseListPage<>(session);
        EmployeeListPage<?> employeeListPage = new EmployeeListPage<>(session);
        AccountPage<?> accountPage = new AccountPage<>(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        orderCreationPage<?> orderCreationPage = new orderCreationPage<>(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String userTraveler = param.get("userTraveler");
        String passTraveler = param.get("passTraveler");
        String userApprover = param.get("userApprover");
        String passApprover = param.get("passApprover");
        String refundMethod = param.get("refundMethod");
        String currency = param.get("currency");
        int bankFee = Integer.parseInt(param.get("bankFee"));
        int reasonIndex = Integer.parseInt(param.get("reason"));
        int sleep = Integer.parseInt(param.get("sleep"));

        String startDate = generateRandomDate(param.get("rangeAddDate"));
        String query = param.get("hotelName");
        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = getRandomObject(salutationList);

        step("Get api access token");
        String apiAccessToken = "Bearer " + accountPage.getAccessTokenUNM(userTraveler, passTraveler);
        String accountId = String.valueOf(corporateApi.getAccountId(apiAccessToken.split(" ", 2)[1]));

        step("Check order present in order list");
        String orderId = "";
        MapVerticalWithOrderId.TitleAndOrderId activeOrderData = orderListPage.findFirstOrderFilterByVertical(apiAccessToken, userTraveler, accountId, "hotel", currency, true, false, OrderStatus.ETICKET_ISSUED);
        if (activeOrderData == null) {
            step("Create new B2B Corp hotel issued order");
            orderCreationPage.createIssuedHotelOrderApiB2BCorp(userTraveler, passTraveler, startDate, query, 1, 1, 1, orderFullName, orderNameTitle, guestFullName, guestTitle, employeeListPage.getEmployeeData(userTraveler), false, currency);
            orderId = String.valueOf(orderCreationPage.getOrderId());
            sleep(sleep);

            step("Approve booking by approver account for Order ID: " + orderId);
            Boolean bookingStatus = purchaseListPage.approveBookingB2BApi(userApprover, passApprover, orderId);
            Assertion.assertThat("Booking approved by B2B approver", bookingStatus, is(true));
        } else {
            orderId = activeOrderData.orderId;
        }

        step("Login to Admin Care");
        adminCarePage.goToAdminCarePage(username, password);

        step("Open Corporate Order Id");
        adminCarePage.searchOrderId(orderId, 5);
        adminCarePage.clickMoreInActionColumn();
        adminCarePage.selectInMoreInActionColumn("Details");

        step("Open Refund Form Dialog");
        adminCareDetailPage.clickRefundCancelTab();
        adminCareDetailPage.clickERefundByCsButton();

        step("Click one of the refund reason");
        adminCareDetailPage.selectRefundReasonValue(reasonIndex);

        step("Open the collapsible estimation details section");
        adminCareDetailPage.clickSeeDetailsEstimationAmount();
        adminCareDetailPage.scrollToRefundMethod();

        step("Verify Refund Method and Amount");
        String totalRefundAmount = adminCareDetailPage.getTotalRefundAmount().replaceAll("[^\\d-]", "");
        Assertion.assertThat("Refund Method Per Amount is Correct", adminCareDetailPage.getEstimatedRefundAmountPerMethod(refundMethod.toLowerCase()), is(totalRefundAmount));
        Assertion.assertThat("Refund method is correct", adminCareDetailPage.getRefundMethodsRefundForm(), hasItem(refundMethod));
        Assertion.assertThat("is Bank Fee correct", adminCareDetailPage.getBankFee(), is(bankFee));
    }

    @Author(names = {"NaufalF"})
    @Module(name = "RefundForm")
    @Tribe(name = "Refund")
    @Vertical(name = "PROP")
    @Test
    public void testRefundPaidDebitCard() {
        Map<String, String> param = getParam("testRefundPaidDebitCard");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        AdminCarePage<?> adminCarePage = new AdminCarePage<>(session);
        AdminCareDetailPage<?> adminCareDetailPage = new AdminCareDetailPage<>(session);
        orderCreationPage<?> orderCreationPage = new orderCreationPage<>(session);
        AccountPage<?> accountPage = new AccountPage<>(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String userConsumer = param.get("userConsumer");
        String passConsumer = param.get("passConsumer");
        String currency = param.get("currency");
        String refundMethod = param.get("refundMethod");
        String refundMethodTwo = param.get("refundMethodTwo");
        int reasonIndex = Integer.parseInt(param.get("reason"));

        String productUrlName = param.get("productUrlName");
        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String contactFullName = randomName() + " autorder";
        String contactSalutation = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestEmailAddress = randomEmail();
        String guestSalutation = salutationList[0];

        String cardNumber = param.get("cardNumber");
        String expiry = param.get("expiry");
        String cvv = param.get("cvv");
        String cardName = param.get("cardName");

        step("Get api access token");
        String apiAccessToken = "Bearer " + accountPage.getAccessTokenUNM(userConsumer, passConsumer);
        String accountId = String.valueOf(corporateApi.getAccountId(apiAccessToken.split(" ", 2)[1]));

        step("Fetch refundable car rental order, else create order");
        MapVerticalWithOrderId.TitleAndOrderId activeOrderData = orderListPage.findFirstOrderFilterByPaymentSource(apiAccessToken, userConsumer, accountId, "debit_online", currency, true, false, OrderStatus.ETICKET_ISSUED);
        String issuedOrderID = orderListPage.getTodoBookedOrderId(orderCreationPage, activeOrderData, userConsumer, passConsumer, currency, productUrlName, true, contactFullName, contactSalutation, guestFullName, guestEmailAddress, guestSalutation, cardNumber, expiry, cvv, cardName);

        step("Login to Admin Care");
        adminCarePage.goToAdminCarePage(username, password);

        step("Search and Open Order Id");
        adminCarePage.searchOrderId(issuedOrderID, 5);
        adminCarePage.selectOrderListId(0);

        step("Get the user Phone Number");
        String userPN = adminCareDetailPage.getUserInfoAccount(1);

        step("Open Refund Form Dialog");
        adminCareDetailPage.clickRefundCancelTab();
        adminCareDetailPage.clickERefundByCsButton();

        step("Click one of the refund reason");
        adminCareDetailPage.selectRefundReasonValue(reasonIndex);

        step("Open the collapsible estimation details section");
        adminCareDetailPage.clickSeeDetailsEstimationAmount();
        adminCareDetailPage.scrollToRefundMethod();

        step("Verify Refund Method " + refundMethod + " and Phone Number");
        Assertion.assertThat("Refund method is " + refundMethod + " in the refund estimation", adminCareDetailPage.isEstimatedRefundMethodCorrect(refundMethod), is(true));
        Assertion.assertThat("Refund method is correct", adminCareDetailPage.isRefundMethodCorrect(refundMethod), is(true));
        Assertion.assertThat("is " + refundMethod + " Phone Number Correct", adminCareDetailPage.getBliblipayPhoneNumber(), is(userPN));

        step("Change Refund Method");
        adminCareDetailPage.clickSpecialCaseCheckBox();

        step("Verify Refund Method " + refundMethodTwo);
        Assertion.assertThat("Refund method is " + refundMethodTwo + " in the refund estimation", adminCareDetailPage.isEstimatedRefundMethodCorrect(refundMethodTwo), is(true));
        Assertion.assertThat("Refund method is correct", adminCareDetailPage.isRefundMethodCorrect(refundMethodTwo), is(true));
    }

    @Author(names = {"NaufalF"})
    @Module(name = "RefundForm")
    @Tribe(name = "Refund")
    @Vertical(name = "PROP")
    @Test
    public void testMultiRefundMethod() {
        Map<String, String> param = getParam("testMultiRefundMethod");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        AdminCarePage<?> adminCarePage = new AdminCarePage<>(session);
        AdminCareDetailPage<?> adminCareDetailPage = new AdminCareDetailPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String orderId = param.get("orderId");
        ArrayList<String> refundMethods = new ArrayList<>(
                Arrays.asList(param.get("refundMethods").substring(1, param.get("refundMethods").length() - 1).split("-"))
        );
        int reasonIndex = Integer.parseInt(param.get("reason"));

        step("Login to Admin Care");
        adminCarePage.goToAdminCarePage(username, password);

        step("Search and Open Order Id");
        adminCarePage.searchOrderId(orderId, 5);
        adminCarePage.selectOrderListId(0);

        step("Get the user Phone Number");
        String userPN = adminCareDetailPage.getUserInfoAccount(1);

        step("Open Refund Form Dialog");
        adminCareDetailPage.clickRefundCancelTab();
        adminCareDetailPage.clickERefundByCsButton();

        step("Click one of the refund reason");
        adminCareDetailPage.selectRefundReasonValue(reasonIndex);

        step("Open the collapsible estimation details section");
        adminCareDetailPage.clickSeeDetailsEstimationAmount();
        adminCareDetailPage.scrollToRefundMethod();

        step("Verify Refund Method are " + refundMethods);
        Assertion.assertThat("Refund method are " + refundMethods + " in the refund estimation", adminCareDetailPage.isEstimatedRefundMethodCorrect(refundMethods), is(true));
        Assertion.assertThat("Refund method are " + refundMethods, adminCareDetailPage.isRefundMethodCorrect(refundMethods), is(true));
    }

    @Author(names = {"NaufalF"})
    @Module(name = "RefundList")
    @Tribe(name = "Refund")
    @Vertical(name = "PROP")
    @Test
    public void testCreateRefundBulkWithoutDeeplink() {
        Map<String, String> param = getParam("testCreateRefundBulkWithoutDeeplink");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        AdminCarePage<?> adminCarePage = new AdminCarePage<>(session);
        RefundBulkPage<?> refundBulkPage = new RefundBulkPage<>(session);
        RefundListPage<?> refundListPage = new RefundListPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String orderId = param.get("orderId");
        String orderDetailId = param.get("orderDetailId");
        String amount = param.get("amount");
        String reasonId = param.get("reasonId");
        String remark = param.get("remark");
        String expiryHour = param.get("expiryHour");
        String refundMethod = param.get("refundMethod");
        String currency = "IDR";

        step("Login to Admin Care");
        adminCarePage.goToAdminCarePage(username, password);

        step("Go To Bulk Refund Upload Page");
        adminCarePage.goToRefundPage();
        refundBulkPage.goToRefundBulk();

        step("Generate Refund Bulk CSV");
        String expiry = refundBulkPage.setExpiryTime(expiryHour);
        String filePath = refundBulkPage.generateCSVRefundBulk(orderId, orderDetailId, amount, reasonId, expiry, remark);

        step("Upload CSV File");
        refundBulkPage.uploadNewBatch();
        refundBulkPage.uploadCSVRefundBulk(filePath);
        refundBulkPage.waitForLoader("Uploading your file...", 10);
        Assertion.assertThat("is Flash Success appear", refundBulkPage.verifyFlashSuccess("Successfully uploaded a csv file"), is(true));
        Assertion.assertThat("is File Uploaded", refundBulkPage.verifyFileUploaded(filePath), is(true));
        Assertion.assertThat("is Ready to Validate page", refundBulkPage.verifyRefundBulkEmpty(false), is(true));
        refundBulkPage.deleteCSVFileOnMachine(filePath);

        step("Validate Refund Bulk");
        refundBulkPage.validateRefundBulk();
        Assertion.assertThat("is Table Summary Correct", refundBulkPage.verifySummaryValidateSucces(refundMethod, amount, currency, "1"), is(true));
        Assertion.assertThat("is Tab Data Error Empty", refundBulkPage.validateTabDataErrorEmpty(), is(true));
        Assertion.assertThat("is Data Ready to Execute Correct", refundBulkPage.validateTabDataReady(orderId, orderDetailId, currency, amount, reasonId, expiry, remark, refundMethod), is(true));

        step("Submit Refund Bulk");
        refundBulkPage.submitRefundBulk();
        Assertion.assertThat("verify Flash Success appear", refundBulkPage.verifyFlashSuccess("Your bulk upload is complete."), is(true));
        Assertion.assertThat("verify Table Summary", refundBulkPage.verifySummaryValidateSucces(refundMethod, amount, currency, "1"), is(true));
        Assertion.assertThat("verify Success Create Bulk Refund", refundBulkPage.verifySuccessExecuteBulk("1"), is(true));
        refundBulkPage.clickExportData();
        Assertion.assertThat("verify Download Success Data", refundBulkPage.verifyDownloadExportCSV(expiry, "5000"), is(true));

        step("Verify Created Refund on Refund List");
        String query = "SELECT * FROM tix_refund.refund_bulk_preview rbp WHERE rbp.order_id = '" + orderId + "'";
        String requestId = refundBulkPage.getRequestIdFromDB(query);
        refundListPage.goToRefundList();
        refundListPage.goToRefundListTab("Hotel");
        refundListPage.searchUsingFilterSearchBy("Request ID", requestId);
        BigInteger refundAmount = new BigInteger(amount);
        Assertion.assertThat("verify Refund Method and Amount", refundListPage.verifyTotalAmountAndMethodList(refundAmount, currency, refundMethod), is(true));
        Assertion.assertThat("verify Refund Status Approved", refundListPage.verifyRefundApprovedState("Hotel"), is(true));
    }

    @Author(names = {"NaufalF"})
    @Module(name = "RefundTransaction")
    @Tribe(name = "Refund")
    @Vertical(name = "PROP")
    @Test
    public void testVerifyOpenRefundTransaction() {
        Map<String, String> param = getParam("testVerifyOpenRefundTransaction");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        AdminCarePage<?> adminCarePage = new AdminCarePage<>(session);
        RefundTransactionPage<?> refundTransactionPage = new RefundTransactionPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String dataPage = param.get("dataPage");

        ArrayList<String> tabNames = new ArrayList<>(
                Arrays.asList(param.get("tabNames").split("-"))
        );
        String defaultRow = param.get("defaultRow");

        step("Login to Admin Care");
        adminCarePage.goToAdminCarePage(username, password);

        step("go To Refund Transaction page");
        adminCarePage.goToRefundPage();
        refundTransactionPage.goToRefundTransaction();

        for (String tab : tabNames) {
            step("Go To Refund Transaction tab " + tab);
            refundTransactionPage.goToRefundTransactionTab(tab);

            step("Set Total Data Displayed per Page");
            refundTransactionPage.setTotalDataPerPage(dataPage);

            step("Verify " + dataPage + " Data Displayed per Page");
            Assertion.assertThat("Total displayed data per page is correct", refundTransactionPage.getTotalDisplayedData(), is(dataPage));

            step("Reset all Filter");
            refundTransactionPage.clickButton("Clear Search");
            Assertion.assertThat("Total displayed data per page is default " + defaultRow, refundTransactionPage.getTotalDisplayedData(), is(defaultRow));
        }
    }

    @Author(names = {"NaufalF"})
    @Module(name = "RefundTransaction")
    @Tribe(name = "Refund")
    @Vertical(name = "PROP")
    @Test
    public void testVerifyFilterRefundTransaction() {
        Map<String, String> param = getParam("testVerifyFilterRefundTransaction");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        AdminCarePage<?> adminCarePage = new AdminCarePage<>(session);
        RefundTransactionPage<?> refundTransactionPage = new RefundTransactionPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        int monthDuration = Integer.parseInt(param.get("monthDuration"));
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy");
        String startDate = today.minusMonths(monthDuration).format(formatter);
        String endDate = today.format(formatter);
        ArrayList<String> inputFilters = new ArrayList<>(
                Arrays.asList(param.get("inputFilters").split("-"))
        );
        ArrayList<String> dropdownFilters = new ArrayList<>(
                Arrays.asList(param.get("dropdownFilters").split("-"))
        );
        ArrayList<String> dropdownFilterKeys = new ArrayList<>(
                Arrays.asList(param.get("dropdownFilterKeys").split("-"))
        );

        step("Login to Admin Care");
        adminCarePage.goToAdminCarePage(username, password);

        step("go To Refund Transaction page");
        adminCarePage.goToRefundPage();
        refundTransactionPage.goToRefundTransaction();

        step("Get Required Filter Data from DB");
        String query = "SELECT * FROM tix_refund.refund_request rr WHERE rr.last_status = 'on_process_cs' ORDER BY id DESC LIMIT 1";
        RefundRequestDTO result = refundTransactionPage.getDataRefundDB(query).get(0);

        for (String filter : inputFilters) {
            step("Set Filter " + filter);
            String data = refundTransactionPage.getFilterDataFromDTO(result, filter);
            refundTransactionPage.clickButton("Filter(s)");
            refundTransactionPage.inputFilter(filter, data);
            refundTransactionPage.clickButton("Apply");

            step("Verify Filter " + filter);
            Assertion.assertThat("Displayed data is correct", refundTransactionPage.verifyRowValueOfColumn(filter, data), is(true));
            refundTransactionPage.clickButton("Filter(s)");
            refundTransactionPage.clickButton("Reset");
        }

        for (int i = 0; i < dropdownFilters.size(); i++) {
            String filter = dropdownFilters.get(i);
            String key = dropdownFilterKeys.get(i);

            step("Set Filter " + filter);

            refundTransactionPage.clickButton("Filter(s)");
            String option = refundTransactionPage.selectFilterDropdownByKey(filter, key);
            refundTransactionPage.clickButton("Apply");

            step("Verify Filter " + filter);
            Assertion.assertThat("Displayed data is correct", refundTransactionPage.verifyRowValueOfColumn(filter, option), is(true));
            refundTransactionPage.clickButton("Filter(s)");
            refundTransactionPage.clickButton("Reset");
        }

        step("Set Filter Date");
        refundTransactionPage.setFilterDate(startDate, endDate, monthDuration);
        refundTransactionPage.clickButton("Search");

        step("Verify Filter Date");
        Assertion.assertThat("Verify filtered data request date", refundTransactionPage.verifyFilterDate(startDate, endDate), is(true));
    }

    @Author(names = {"NaufalF"})
    @Module(name = "RefundTransaction")
    @Tribe(name = "Refund")
    @Vertical(name = "PROP")
    @Test(dataProvider = "testFinanceDoActionDataProvider")
    public void testFinanceDoAction(Map<String, String> param) {
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        AdminCarePage<?> adminCarePage = new AdminCarePage<>(session);
        RefundTransactionPage<?> refundTransactionPage = new RefundTransactionPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        ArrayList<String> tabNames = new ArrayList<>(Arrays.asList(param.get("tabNames").split("-")));
        ArrayList<String> actions = new ArrayList<>(Arrays.asList(param.get("actions").split("-")));
        String description = param.get("description");
        boolean actionPerMethod = Boolean.parseBoolean(param.get("actionPerMethod"));
        boolean actionBulk = Boolean.parseBoolean(param.get("actionBulk"));
        String dataCount = param.get("dataCount");
        if (actionBulk) dataCount = String.valueOf(CoreFunctions.generateRandomInt(5, 50));
        String actionDesc = actionPerMethod ? "per Refund Method" : "per Refund Request";

        step("Login to Admin Care");
        adminCarePage.goToAdminCarePage(username, password);

        step("go To Refund Transaction page");
        adminCarePage.goToRefundPage();
        refundTransactionPage.goToRefundTransaction();

        step("Go To Refund Transaction tab Rejected");
        refundTransactionPage.goToRefundTransactionTab("Rejected");

        step("Verify Action Buttons not shown on tab Rejected");
        Assertion.assertThat("Action Buttons Not Shown", refundTransactionPage.verifyActionButtonsNotShown(), is(true));
        Assertion.assertThat("Select all row disabled", refundTransactionPage.verifySelectAllRowDisabled(), is(true));

        for (String tab : tabNames) {
            step("Go To Refund Transaction tab " + tab);
            refundTransactionPage.goToRefundTransactionTab(tab);

            step("Verify do Action " + actionDesc);
            for (String action : actions) {
                refundTransactionPage.setTotalDataPerPage(dataCount);
                refundTransactionPage.clickButton("Search");
                if (actionBulk) {
                    refundTransactionPage.selectAllRow();
                    refundTransactionPage.clickActionButtonBulk(action);
                } else {
                    refundTransactionPage.clickActionButton(action, actionPerMethod);
                }
                Assertion.assertThat("do Action " + action, refundTransactionPage.verifyDoAction(action, description, refundTransactionPage.getTotalDisplayedData()), is(true));
                refundTransactionPage.clickButton("No");
                if (!actionBulk || !actionPerMethod) refundTransactionPage.goToRefundTransactionTab(tab);
            }
        }
    }

    @DataProvider(name = "testFinanceDoActionDataProvider")
    public Object[][] testFinanceDoActionDataProvider() {
        return getObjectParams("testFinanceDoAction");
    }

    @Author(names = {"NaufalF"})
    @Module(name = "RefundTransaction")
    @Tribe(name = "Refund")
    @Vertical(name = "PROP")
    @Test
    public void testVerifyOpenRefundReport() {
        Map<String, String> param = getParam("testVerifyOpenRefundReport");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        AdminCarePage<?> adminCarePage = new AdminCarePage<>(session);
        RefundReportPage<?> refundReportPage = new RefundReportPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String title = param.get("title");
        String desc = param.get("desc");
        String url = param.get("url");

        step("Login to Admin Care");
        adminCarePage.goToAdminCarePage(username, password);

        step("go To Refund Report page");
        adminCarePage.goToRefundPage();
        refundReportPage.goToRefundReport();

        step("Verify Refund Report initial page");
        Assertion.assertThat("is Filter panel shown", refundReportPage.isPanelFilterShown(), is(true));
        Assertion.assertThat("is T-Man Image Data Not Found Shown", refundReportPage.imageDataNotFoundShown(), is(true));
        Assertion.assertThat("is Title Empty Data correct", refundReportPage.isTitleEmptyDataShown(title), is(true));
        Assertion.assertThat("is Description Empty Data correct", refundReportPage.isDescEmptyDataShown(desc), is(true));
        refundReportPage.clickButton("See Refund Transaction");
        Assertion.assertThat("is Button directed to Transaction Page", refundReportPage.getCurrentURL(), containsString(url));
    }

    @Author(names = {"NaufalF"})
    @Module(name = "RefundTransaction")
    @Tribe(name = "Refund")
    @Vertical(name = "PROP")
    @Test
    public void testVerifyFilterRefundReport() {
        Map<String, String> param = getParam("testVerifyFilterRefundReport");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        AdminCarePage<?> adminCarePage = new AdminCarePage<>(session);
        RefundReportPage<?> refundReportPage = new RefundReportPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String[] inputFilters = param.get("inputFilters").split("-");
        String[] dropdownFilters = param.get("dropdownFilters").split("-");
        String message = param.get("message");

        step("Login to Admin Care");
        adminCarePage.goToAdminCarePage(username, password);

        step("go To Refund Report page");
        adminCarePage.goToRefundPage();
        refundReportPage.goToRefundReport();

        step("Select Filter Finance Submission Date");
        String startDate = CoreFunctions.getRandomDateD_MMMM_YYYY(1);
        String todayDate = CoreFunctions.getTodayDateD_MMMM_YYYY();
        refundReportPage.clickFilterDate("Finance Submission Date");
        refundReportPage.selectDate(startDate, todayDate);
        refundReportPage.clickButton("Search");

        step("Verify Filter Finance Submission Date");
        Assertion.assertThat("Data shown", refundReportPage.imageDataNotFoundShown(), is(false));
        Assertion.assertThat("Filter Refund Request Date disabled", refundReportPage.isOtherFilterDisabled("Refund Request Date"), is(true));
        refundReportPage.clickButton("Reset");

        step("Select Filter Refund Request Date");
        startDate = CoreFunctions.getRandomDateD_MMMM_YYYY(1);
        refundReportPage.clickFilterDate("Refund Request Date");
        refundReportPage.selectDate(startDate, todayDate);
        refundReportPage.clickButton("Search");

        step("Verify Filter Refund Request Date");
        Assertion.assertThat("End Filter Request Date is Correct", refundReportPage.verifyDataRequestDate(false, todayDate), is(true));
        Assertion.assertThat("Start Filter Request Date is Correct", refundReportPage.verifyDataRequestDate(true, startDate), is(true));
        refundReportPage.clickButton("Reset");

        step("Get filter data from DB");
        String query = "SELECT * FROM tix_refund.refund_request rr WHERE last_status IN ('on_process_cs', 'on_process_finance', 'rejected', 'failed', 'refunded') ORDER BY rr.id DESC LIMIT 1";
        RefundRequestDTO result = refundReportPage.getRefundDataFromDB(query).get(0);

        for (String filter : inputFilters) {
            step("Input data to filter -> " + filter);
            String data = refundReportPage.getFilterDataFromDTO(result, filter);
            refundReportPage.inputDataOnFilter(filter, data);
            refundReportPage.clickButton("Search");

            step("Verify report data filter " + filter);
            Assertion.assertThat("is Refund Report Data correct", refundReportPage.verifyRowValueOfColumn(filter, data), is(true));
            refundReportPage.clickButton("Reset");
        }

        for (String filter : dropdownFilters) {
            step("Select dropdown from filter -> " + filter);
            refundReportPage.clickFilterDropdown(filter);
            String option;
            if (filter.equals("Refund Type")) {
                option = result.getRequest_type();
                refundReportPage.selectFilterDropdownByOption(refundReportPage.refundType.get(option));
            } else {
                option = refundReportPage.getRandomOptionFromDropdownFilter();
                refundReportPage.selectFilterDropdownByOption(option);
            }
            refundReportPage.clickButton("Search");
            Assertion.assertThat("Error filter date must be filled", refundReportPage.isErrorNotFilled(message), is(true));

            step("Select Filter Refund Request Date");
            startDate = CoreFunctions.getRandomDateD_MMMM_YYYY(6);
            refundReportPage.clickFilterDate("Refund Request Date");
            refundReportPage.selectDate(startDate, todayDate);
            refundReportPage.clickButton("Search");

            step("Verify report data filter -> " + filter);
            Assertion.assertThat("is Refund Report Data correct", refundReportPage.verifyRowValueOfColumn(filter, option), is(true));
            refundReportPage.clickButton("Reset");
        }
    }

    @Author(names = {"NaufalF", "Hafizh"})
    @Module(name = "RefundTransaction")
    @Tribe(name = "Refund")
    @Vertical(name = "PROP")
    @Test
    public void testDownloadExcelReport() {
        Map<String, String> param = getParam("testDownloadExcelReport");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        AdminCarePage<?> adminCarePage = new AdminCarePage<>(session);
        RefundReportPage<?> refundReportPage = new RefundReportPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String timeOutMiliSec = param.get("timeOutMiliSec");

        step("Login to Admin Care");
        adminCarePage.goToAdminCarePage(username, password);

        step("Go To Refund List Page and a Tab");
        adminCarePage.goToRefundPage();
        refundReportPage.goToRefundReport();

        step("Set Filter Finance Submission Date");
        String startDate = CoreFunctions.getRandomDateD_MMMM_YYYY(0);
        String todayDate = CoreFunctions.getTodayDateD_MMMM_YYYY();
        refundReportPage.clickFilterDate("Finance Submission Date");
        refundReportPage.selectDateDirectlyFromCalendar(startDate, todayDate);
        refundReportPage.clickButton("Search");

        step("Click Download to Excel");
        refundReportPage.clickDownloadExcel();

        step("Check Downloaded File");
        String fileName = refundReportPage.excelFilenameBuilderDateD_MMMM_YYYY(startDate, todayDate);
        Assertion.assertThat("Verify downloaded file", refundReportPage.verifyDownloadedExcel(fileName, timeOutMiliSec), is(true));
    }

    @Author(names = {"NaufalF"})
    @Module(name = "RefundTransaction")
    @Tribe(name = "Refund")
    @Vertical(name = "PROP")
    @Test
    public void testUploadManualChecking() {
        Map<String, String> param = getParam("testUploadManualChecking");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        AdminCarePage<?> adminCarePage = new AdminCarePage<>(session);
        RefundReportPage<?> refundReportPage = new RefundReportPage<>(session);

        String username = param.get("username");
        String password = param.get("password");

        step("Login to Admin Care");
        adminCarePage.goToAdminCarePage(username, password);

        step("Go To Refund List Page and a Tab");
        adminCarePage.goToRefundPage();
        refundReportPage.goToRefundReport();

        step("Get data for manual checking");
        String query = "SELECT * FROM tix_refund.refund_request rr WHERE rr.manual_checking = 0 AND rr.last_status IN ('on_process_cs', 'on_process_finance', 'rejected', 'failed', 'refunded') ORDER BY rr.id DESC LIMIT 1";
        RefundRequestDTO result = refundReportPage.getRefundDataFromDB(query).get(0);
        String requestId = String.valueOf(result.getId());
        String orderId = String.valueOf(result.getReference_order_id());
        String orderDetailId = String.valueOf(result.getReference_order_detail_id());

        step("Generate CSV file upload manual checking");
        String filePath = refundReportPage.generateCSVUploadManualCheck(requestId, orderId, orderDetailId);

        step("Upload file to refund report");
        refundReportPage.clickButton("Upload File");
        refundReportPage.uploadFileManualCheck(filePath);
        refundReportPage.clickButton("Submit");

        step("Verify Flash Success Message");
        Assertion.assertThat("is correct Flash Success message", refundReportPage.verifyFlashUploadCSV("1"), is(true));

        step("Verify data manual checking is Valid");
        refundReportPage.inputDataOnFilter("Refund Request ID", requestId);
        refundReportPage.clickButton("Search");
        Assertion.assertThat("is Refund Report Data correct", refundReportPage.verifyRowValueOfColumn("Manual Checking", "Valid"), is(true));

        step("Delete the CSV file");
        CoreFunctions.deleteFile(filePath);
    }

    @Author(names = {"NaufalF"})
    @Module(name = "RefundTransaction")
    @Tribe(name = "Refund")
    @Vertical(name = "PROP")
    @Test(dataProvider = "testDisburseMethodCardDataProvider")
    public void testDisburseMethodCard(Map<String, String> param) {
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        AdminCarePage<?> adminCarePage = new AdminCarePage<>(session);
        RefundTransactionPage<?> refundTransactionPage = new RefundTransactionPage<>(session);
        RefundListPage<?> refundListPage = new RefundListPage<>(session);
        RefundReportPage<?> refundReportPage = new RefundReportPage<>(session);
        orderCreationPage<?> orderCreation = new orderCreationPage<>(session);
        RefundCreation<?> refundCreation = new RefundCreation<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String userConsumer = param.get("userConsumer");
        String passConsumer = param.get("passConsumer");
        String startDate = generateRandomDate(param.get("rangeAddDate"));
        String hotelName = param.get("hotelName");
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));
        int room = Integer.parseInt(param.get("room"));
        String reasonId = param.get("reasonId");
        String remark = param.get("remark");
        String flash = param.get("flash");
        int retry = Integer.parseInt(param.get("retry"));
        String status = param.get("status");

        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = salutationList[0];

        String refundMethod = param.get("refundMethod");
        String currency = param.get("currency");
        String cardNumber = param.get("cardNumber");
        String expiry = param.get("expiry");
        String cvv = param.get("cvv");
        String cardName = param.get("cardName");

        step("Create Hotel order paid using CC " + currency);
        orderCreation.createHotelCreditCardIssuedOrderApi(userConsumer, passConsumer, startDate, hotelName, adult,
                night, room, currency, orderFullName, orderNameTitle, guestFullName, guestTitle, cardNumber, expiry, cvv, cardName);

        step("Login to Admin Care");
        adminCarePage.goToAdminCarePage(username, password);

        step("Create SFR from created order");
        boolean isForceBank = false;
        List<String> items = new ArrayList<>();
        String requestId = refundCreation.createFormMiddlewareApi(String.valueOf(orderCreation.getGrandTotal()), isForceBank, items,
                orderCreation.getOrderDetailId(), orderCreation.getOrderId(), reasonId,
                "", "", "", remark, username);
        Assertion.assertThat("request Id not empty", requestId, not(blankOrNullString()));

        step("Go to Refund List tab Hotel");
        refundListPage.goToRefundList();
        refundListPage.goToRefundListTab("Hotel");

        step("Hit Automation Process to approve refund request");
        String responseCode = refundCreation.automationProcess("APPROVED", Long.parseLong(requestId));

        if (responseCode.isEmpty()) {
            step("Reload Refund List");
            adminCarePage.goToAdminCarePage(username, password);
            refundListPage.goToRefundList();
            refundListPage.goToRefundListTab("Hotel");

            step("Open the Refund Request");
            refundListPage.searchUsingFilterSearchBy("Request ID", requestId);
            boolean isApproved = refundListPage.verifyRefundApprovedState("Hotel");
            if (!isApproved) {
                step("Receive the Refund Request");
                refundListPage.clickRefundActionButton("received", "Receive");
                refundListPage.verifyRefundReceivedState("Hotel");

                step("Approve the Refund Request");
                refundListPage.clickRefundActionButton("approved", "Approve");
            }
        }

        step("Go to Refund Transaction");
        refundTransactionPage.goToRefundTransaction();

        step("Set Filter Refund Request ID");
        refundTransactionPage.clickButton("Filter(s)");
        refundTransactionPage.inputFilter("Refund Request ID", requestId);
        refundTransactionPage.clickButton("Apply");

        step("Submit Disburse this data: " + requestId);
        refundTransactionPage.clickActionButton("Submit", false);
        refundTransactionPage.clickButton("Yes");
        Assertion.assertThat("is Flash message correct", refundTransactionPage.verifyFlashAction(flash), is(true));

        step("Verify Refund status is Refunded");
        Assertion.assertThat("is Refund status " + status, refundReportPage.verifyDisburseStatus(requestId, retry, status), is(true));
        Assertion.assertThat("is Refund Method " + refundMethod, refundReportPage.verifyRowValueOfColumn("Refund Method", refundMethod), is(true));
    }

    @DataProvider(name = "testDisburseMethodCardDataProvider")
    public Object[][] testDisburseMethodCardDataProvider() {
        return getObjectParams("testDisburseMethodCard");
    }

    @Author(names = {"NaufalF"})
    @Module(name = "RefundTransaction")
    @Tribe(name = "Refund")
    @Vertical(name = "PROP")
    @Test(dataProvider = "testDisburseBlipayTopupBankDataProvider")
    public void testDisburseBlipayTopupBank(Map<String, String> param) {
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        AdminCarePage<?> adminCarePage = new AdminCarePage<>(session);
        RefundTransactionPage<?> refundTransactionPage = new RefundTransactionPage<>(session);
        RefundListPage<?> refundListPage = new RefundListPage<>(session);
        RefundReportPage<?> refundReportPage = new RefundReportPage<>(session);
        orderCreationPage<?> orderCreation = new orderCreationPage<>(session);
        RefundCreation<?> refundCreation = new RefundCreation<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String userConsumer = param.get("userConsumer");
        String passConsumer = param.get("passConsumer");
        String productUrlName = param.get("productUrlName");
        String note = param.get("note");
        String reasonId = param.get("reasonId");
        String flash = param.get("flash");
        int retry = Integer.parseInt(param.get("retry"));
        boolean refundable = true;
        String status = param.get("status");

        String currency = "IDR";
        boolean isForceBank = Boolean.parseBoolean(param.get("isForceBank"));
        String bankAccountName = param.get("bankName");
        String bankAccountNumber = param.get("bankNumber");
        String bankCodeTiketIdentifier = param.get("bankCode");
        String refundMethod = param.get("refundMethod");

        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String contactFullName = randomName() + " autorder";
        String contactSalutation = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestEmailAddress = randomEmail();
        String guestSalutation = salutationList[0];

        step("Create TTD order paid using Non CC/Paylater/Blipay");
        orderCreation.createIssuedOrderApiTodo(userConsumer, passConsumer, productUrlName, refundable, currency,
                contactFullName, contactSalutation, guestFullName, guestEmailAddress, guestSalutation);

        step("Login to Admin Care");
        adminCarePage.goToAdminCarePage(username, password);

        step("Create SFR from created order");
        List<String> items = new ArrayList<>();
        String requestId = refundCreation.createFormMiddlewareApi(String.valueOf(orderCreation.getGrandTotal()), isForceBank,
                items, orderCreation.getOrderDetailId(), orderCreation.getOrderId(), reasonId,
                bankAccountName, bankAccountNumber, bankCodeTiketIdentifier, note, username);
        Assertion.assertThat("request Id not empty", requestId, not(blankOrNullString()));

        step("Go to Refund List tab To Do");
        refundListPage.goToRefundList();
        refundListPage.goToRefundListTab("To Do");

        step("Hit Automation Process to approve refund request");
        String responseCode = refundCreation.automationProcess("APPROVED", Long.parseLong(requestId));

        if (responseCode.isEmpty()) {
            step("Reload Refund List");
            adminCarePage.goToAdminCarePage(username, password);
            refundListPage.goToRefundList();
            refundListPage.goToRefundListTab("To Do");

            step("Open the Refund Request");
            refundListPage.searchUsingFilterSearchBy("Request ID", requestId);
            boolean isApproved = refundListPage.verifyRefundApprovedState("To Do");
            if (!isApproved) {
                step("Receive the Refund Request");
                refundListPage.clickRefundActionButton("received", "Receive");
                refundListPage.verifyRefundReceivedState("To Do");

                step("Approve the Refund Request");
                refundListPage.clickRefundActionButton("approved", "Approve");
            }
        }

        step("Go to Refund Transaction");
        refundTransactionPage.goToRefundTransaction();

        step("Set Filter Refund Request ID");
        refundTransactionPage.clickButton("Filter(s)");
        refundTransactionPage.inputFilter("Refund Request ID", requestId);
        refundTransactionPage.clickButton("Apply");

        step("Submit Disburse this data: " + requestId);
        refundTransactionPage.clickActionButton("Submit", false);
        refundTransactionPage.clickButton("Yes");
        Assertion.assertThat("is Flash message correct", refundTransactionPage.verifyFlashAction(flash), is(true));

        step("Verify Refund status is Refunded");
        Assertion.assertThat("is Refund status " + status, refundReportPage.verifyDisburseStatus(requestId, retry, status), is(true));
        Assertion.assertThat("is Refund Method " + refundMethod, refundReportPage.verifyRowValueOfColumn("Refund Method", refundMethod), is(true));
    }

    @DataProvider(name = "testDisburseBlipayTopupBankDataProvider")
    public Object[][] testDisburseBlipayTopupBankDataProvider() {
        return getObjectParams("testDisburseBlipayTopupBank");
    }

    @Author(names = {"NaufalF"})
    @Module(name = "RefundTransaction")
    @Tribe(name = "Refund")
    @Vertical(name = "PROP")
    @Test(dataProvider = "testDisburseTiketPointDataProvider")
    public void testDisburseTiketPoint(Map<String, String> param) throws ExecutionException, InterruptedException {
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        AdminCarePage<?> adminCarePage = new AdminCarePage<>(session);
        RefundTransactionPage<?> refundTransactionPage = new RefundTransactionPage<>(session);
        RefundListPage<?> refundListPage = new RefundListPage<>(session);
        RefundReportPage<?> refundReportPage = new RefundReportPage<>(session);
        orderCreationPage<?> orderCreation = new orderCreationPage<>(session);
        RefundCreation<?> refundCreation = new RefundCreation<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String userConsumer = param.get("userConsumer");
        String passConsumer = param.get("passConsumer");
        String startDate = generateRandomDate(param.get("rangeAddDate"));
        String hotelName = param.get("hotelName");
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));
        int room = Integer.parseInt(param.get("room"));
        String reasonId = param.get("reasonId");
        String note = param.get("note");
        String flash = param.get("flash");
        int retry = Integer.parseInt(param.get("retry"));
        String status = param.get("status");
        String[] refundMethods = param.get("refundMethod").split("-");

        int pdAccountId = Integer.parseInt(param.get("pdAccountId"));
        int pointsThreshold = Integer.parseInt(param.get("pointsThreshold"));
        String pdProcessType = param.get("pdProcessType");
        int pdRatio = Integer.parseInt(param.get("pdRatio"));

        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = salutationList[0];

        if (refundMethods.length > 1) {
            step("Create new hotel order paid using Sakuku + Points");
            orderCreation.createWaitingPaymentOrderHotel(userConsumer, passConsumer, startDate, hotelName,
                    adult, night, room, orderFullName, orderNameTitle, guestFullName, guestTitle, "IDR");
            orderCreation.injectTiketPointWithThreshold(pdAccountId, pdProcessType, pdRatio, userConsumer, passConsumer, pointsThreshold);
            orderCreation.applyTiketPointsV2(orderCreation.getAccessToken(), orderCreation.getOrderId(), "TIXHOTEL");
            orderCreation.payWaitingPaymentHotelOrderApi();
        } else {
            step("Create new hotel order paid using Points");
            orderCreation.createHotelFullTiketPointsIssuedOrderApi(userConsumer, passConsumer, startDate, hotelName,
                    adult, night, room, orderFullName, orderNameTitle, guestFullName, guestTitle, pdAccountId, "IDR");
        }

        step("Login to Admin Care");
        adminCarePage.goToAdminCarePage(username, password);

        step("Create SFR from created order");
        List<String> items = new ArrayList<>();
        String requestId = refundCreation.createFormMiddlewareApi(String.valueOf(orderCreation.getGrandTotal()), false,
                items, orderCreation.getOrderDetailId(), orderCreation.getOrderId(), reasonId,
                "", "", "", note, username);
        Assertion.assertThat("request Id not empty", requestId, not(blankOrNullString()));

        step("Go to Refund List tab Hotel");
        refundListPage.goToRefundList();
        refundListPage.goToRefundListTab("Hotel");

        step("Hit Automation Process to approve refund request");
        String responseCode = refundCreation.automationProcess("APPROVED", Long.parseLong(requestId));

        if (responseCode.isEmpty()) {
            step("Reload Refund List");
            adminCarePage.goToAdminCarePage(username, password);
            refundListPage.goToRefundList();
            refundListPage.goToRefundListTab("Hotel");

            step("Open the Refund Request");
            refundListPage.searchUsingFilterSearchBy("Request ID", requestId);
            boolean isApproved = refundListPage.verifyRefundApprovedState("Hotel");
            if (!isApproved) {
                step("Receive the Refund Request");
                refundListPage.clickRefundActionButton("received", "Receive");
                refundListPage.verifyRefundReceivedState("Hotel");

                step("Approve the Refund Request");
                refundListPage.clickRefundActionButton("approved", "Approve");
            }
        }

        step("Go to Refund Transaction");
        refundTransactionPage.goToRefundTransaction();

        step("Set Filter Refund Request ID");
        refundTransactionPage.clickButton("Filter(s)");
        refundTransactionPage.inputFilter("Refund Request ID", requestId);
        refundTransactionPage.clickButton("Apply");

        step("Submit Disburse this data: " + requestId);
        if (refundMethods.length > 1) {
            refundTransactionPage.clickActionButton("Force Submit", false);
        } else {
            refundTransactionPage.clickActionButton("Submit", false);
        }
        refundTransactionPage.clickButton("Yes");
        Assertion.assertThat("is Flash message correct", refundTransactionPage.verifyFlashAction(flash), is(true));

        step("Verify Refund status is Refunded");
        Assertion.assertThat("is Refund status " + status, refundReportPage.verifyDisburseStatus(requestId, retry, status), is(true));
        Assertion.assertThat("is Refund Method Correct", refundReportPage.verifyMultiRefundMethods(refundMethods), is(true));
    }

    @DataProvider(name = "testDisburseTiketPointDataProvider")
    public Object[][] testDisburseTiketPointDataProvider() {
        return getObjectParams("testDisburseTiketPoint");
    }

    @Author(names = {"NaufalF"})
    @Module(name = "RefundForm")
    @Tribe(name = "Refund")
    @Vertical(name = "PROP")
    @Test(dataProvider = "testSpecialRefundFormPerOrderDetailDataProvider")
    public void testSpecialRefundFormPerOrderDetail(Map<String, String> param) {
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        AdminCarePage<?> adminCarePage = new AdminCarePage<>(session);
        AdminCareDetailPage<?> adminCareDetailPage = new AdminCareDetailPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String refundMethod = param.get("refundMethod");
        String orderId = param.get("orderId");
        String currency = param.get("currency");
        String reason = param.get("reason");
        int paxNum = Integer.parseInt(param.get("paxNum"));
        boolean isFullPax = Boolean.parseBoolean(param.get("isFullPax"));
        String[] trip = {"Depart", "Return"};
        int maxAmountDepart = Integer.parseInt(param.get("maxAmountDepart"));
        int maxAmountReturn = Integer.parseInt(param.get("maxAmountReturn"));
        int[] amount = {maxAmountDepart, maxAmountReturn};
        int promoDepart = Integer.parseInt(param.get("promoDepart"));
        int promoReturn = Integer.parseInt(param.get("promoReturn"));
        int[] promo = {promoDepart, promoReturn};
        int refundFee = Integer.parseInt(param.get("refundFee"));
        String errorAmountBig = param.get("errorAmountBig");

        String paxes = isFullPax ? "Full Pax" : "Partial Pax";
        boolean isSmartTrip = false;
        boolean isOneHundredRefund = false;

        for (int i = 0; i < 2; i++) {
            step("Login to Admin Care");
            adminCarePage.goToAdminCarePage(username, password);

            step("Search and Open Order Id");
            adminCarePage.searchOrderId(orderId, 5);
            adminCarePage.selectOrderListId(0);

            step("select Flight Trip " + trip[i]);
            adminCareDetailPage.selectTripType(trip[i]);
            String orderDetailId = adminCareDetailPage.getDataTableIssuedStatus("Order Detail ID");
            String airlineName = adminCareDetailPage.getAirlineName();
            String aiportDepartCode = adminCareDetailPage.getFlightAirpotCode("Depart");
            String airportArrivalCode = adminCareDetailPage.getFlightAirpotCode("Arrival");
            String flightDepartureDate = adminCareDetailPage.getFlightDateOrTime("Departure", "date");
            String flightDepartureTime = adminCareDetailPage.getFlightDateOrTime("Departure", "time");

            step("Open Special Refund Form dialog");
            adminCareDetailPage.clickRefundCancelTab();
            adminCareDetailPage.clickSpecialRefundButton();
            boolean orderReviewBool = adminCareDetailPage.verifyOrderReviewRefundFormFlight(orderDetailId, airlineName, aiportDepartCode,
                    airportArrivalCode, flightDepartureDate, flightDepartureTime, isOneHundredRefund, isSmartTrip);
            Assertion.assertThat("Refund Form Order Review is correct", orderReviewBool, is(true));

            step("Select Refund Reason and " + paxes);
            adminCareDetailPage.selectRefundReasonByName(reason);
            if (isFullPax) {
                adminCareDetailPage.selectAllPaxAndReason(false);
            } else {
                adminCareDetailPage.selectPax(paxNum);
            }

            step("Input Above Maximum Refund Amount");
            adminCareDetailPage.inputEstimationTotalRefund(amount[i] + 1);

            step("Verify got error max amount");
            Assertion.assertThat("is Flash Message correct", adminCareDetailPage.showFlashMessage(false), containsString(errorAmountBig));
            Assertion.assertThat("can't submit Special Refund Form", adminCareDetailPage.isRequestRefundButtonEnabled(), is(false));

            step("Input Correct Refund Amount");
            adminCareDetailPage.inputEstimationTotalRefund(amount[i]);

            step("Open the collapsible estimation details section");
            adminCareDetailPage.scrollToRefundMethod();
            adminCareDetailPage.clickSeeDetailsEstimationAmount();

            step("Verify Estimation amount and method");
            long amountCalculation = amount[i] - promo[i] - refundFee;
            BigInteger refundAmount = BigInteger.valueOf(amountCalculation);
            BigInteger totalRefundAmount = new BigInteger(adminCareDetailPage.getTotalRefundAmount().replaceAll("\\D", ""));
            Assertion.assertThat("Total Refund Amount is correct", totalRefundAmount, is(refundAmount));
            Assertion.assertThat("Refund Method is correct", adminCareDetailPage.isRefundMethodCorrect(refundMethod), is(true));
            Assertion.assertThat("Refund Method Amount is Correct", adminCareDetailPage.verifyRefundAmountPerMethod(refundAmount, currency, refundMethod), is(true));

            step("Verify Refund Fee Deduction");
            Assertion.assertThat("is Refund Fee Correct", adminCareDetailPage.getSFRPriceBreakdown("Refund fee"), is(refundFee));

            step("Verify Promo Prorate Deduction");
            Assertion.assertThat("Promo amount is correct", adminCareDetailPage.getSFRPriceBreakdown("Gift Voucher"), is(promo[i]));
        }

        step("Open Terms and Condition");
        Assertion.assertThat("is TNC opened correctly", adminCareDetailPage.openTNCRefundForm(true), is(true));
        Assertion.assertThat("is Request Refund Button Enable", adminCareDetailPage.isRequestRefundButtonEnabled(), is(true));
    }

    @DataProvider(name = "testSpecialRefundFormPerOrderDetailDataProvider")
    public Object[][] testSpecialRefundFormPerOrderDetailDataProvider() {
        return getObjectParams("testSpecialRefundFormPerOrderDetail");
    }

    @Author(names = {"Hafizh"})
    @Module(name = "RefundRules")
    @Tribe(name = "Refund")
    @Vertical(name = "PROP")
    @Test
    public void testRefundRulesViewLogs() {
        Map<String, String> param = getParam("testRefundRulesViewLogs");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        RefundRulesPage<?> refundRulesPage = new RefundRulesPage<>(session);
        AdminCarePage<?> adminCarePage = new AdminCarePage<>(session);

        String username = param.get("username");
        String password = param.get("password");

        step("Login to Admin Care");
        adminCarePage.goToAdminCarePage(username, password);

        step("Go To Refund Rules Page");
        adminCarePage.goToRefundPage();
        refundRulesPage.goToRefundRulesTab();

        step("Verify User is On Refund Rules Page");
        Assertion.assertThat("Refund Rules Page is Opened", refundRulesPage.isOnRefundRulesTab(), is(true));

        step("User Open Refund Rules See Log");
        refundRulesPage.clickRefundRulesAction();
        refundRulesPage.clickRefundRulesSeeLog();

        step("Verify Refund Rules See Log is Opened");
        Assertion.assertThat("Refund Rules See Log is Opened", refundRulesPage.isRefundSeeLogOpened(), is(true));
    }

    @Author(names = {"Hafizh"})
    @Module(name = "RefundImportantInfo")
    @Tribe(name = "Refund")
    @Vertical(name = "PROP")
    @Test
    public void testRefundImportantInfoViewLogs() {
        Map<String, String> param = getParam("testRefundImportantInfoViewLogs");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        RefundImportantInfoPage<?> refundImportantInfoPage = new RefundImportantInfoPage<>(session);
        AdminCarePage<?> adminCarePage = new AdminCarePage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String paymentMethod = param.get("paymentMethod");

        step("Login to Admin Care");
        adminCarePage.goToAdminCarePage(username, password);

        step("Go To Refund Important Info Page");
        adminCarePage.goToRefundPage();
        refundImportantInfoPage.goToRefundImportantInfoTab();

        step("Verify User is On Refund Important Info Page");
        Assertion.assertThat("Refund important info page is opened", refundImportantInfoPage.isOnRefundImportantInfoTab(), is(true));

        step("Filter List for Payment Method According to Test Data");
        refundImportantInfoPage.clickButtonPaymentMethodFilter();
        refundImportantInfoPage.selectPaymentMethodInFilter(paymentMethod);

        step("Verify That Selected Payment Method is Available");
        Assertion.assertThat("Payment method is available", refundImportantInfoPage.isResultForPaymentMethodAvailable(paymentMethod), is(true));

        step("Open Refund Important Info Selected Payment Method Log");
        refundImportantInfoPage.clickButtonAction();
        refundImportantInfoPage.clickButtonSeeLog();

        step("Verify That Selected Payment Method Log is Opened");
        Assertion.assertThat("Log of selected payment method is opened", refundImportantInfoPage.isModalSeeLogOpened(), is(true));
    }

    @Author(names = {"Daffa"})
    @Module(name = "RefundImportantInfo")
    @Tribe(name = "Refund")
    @Vertical(name = "PROP")
    @Test
    public void testRefundImportantInfoFilterByPaymentMethod() {
        Map<String, String> param = getParam("testRefundImportantInfoFilterByPaymentMethod");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        RefundImportantInfoPage<?> refundImportantInfoPage = new RefundImportantInfoPage<>(session);
        AdminCarePage<?> adminCarePage = new AdminCarePage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String paymentMethod = param.get("paymentMethod");

        step("Login to Admin Care");
        adminCarePage.goToAdminCarePage(username, password);

        step("Go To Refund Important Info Page");
        adminCarePage.goToRefundPage();
        refundImportantInfoPage.goToRefundImportantInfoTab();

        step("Verify User is On Refund Important Info Page");
        Assertion.assertThat("Refund important info page is opened", refundImportantInfoPage.isOnRefundImportantInfoTab(), is(true));

        step("Search Filter by Payment Method According to Test Data");
        refundImportantInfoPage.clickButtonPaymentMethodFilter();
        refundImportantInfoPage.selectPaymentMethodInFilter(paymentMethod);

        step("Verify That Result for Selected Payment Method is Available");
        Assertion.assertThat("Payment method is available", refundImportantInfoPage.isResultForPaymentMethodAvailable(paymentMethod), is(true));

        step("Verify That All Result Rows are Filtered by Selected Payment Method");
        Assertion.assertThat("All result rows are for selected payment method", refundImportantInfoPage.isAllResultsForPaymentMethod(paymentMethod), is(true));
    }

    @Author(names = {"Daffa"})
    @Module(name = "RefundImportantInfo")
    @Tribe(name = "Refund")
    @Vertical(name = "PROP")
    @Test
    public void testRefundImportantInfoViewDetails() {
        Map<String, String> param = getParam("testRefundImportantInfoViewDetails");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        RefundImportantInfoPage<?> refundImportantInfoPage = new RefundImportantInfoPage<>(session);
        AdminCarePage<?> adminCarePage = new AdminCarePage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String paymentMethod = param.get("paymentMethod");

        step("Login to Admin Care");
        adminCarePage.goToAdminCarePage(username, password);

        step("Go To Refund Important Info Page");
        adminCarePage.goToRefundPage();
        refundImportantInfoPage.goToRefundImportantInfoTab();

        step("Verify User is On Refund Important Info Page");
        Assertion.assertThat("Refund important info page is opened", refundImportantInfoPage.isOnRefundImportantInfoTab(), is(true));

        step("Filter List for Payment Method According to Test Data");
        refundImportantInfoPage.clickButtonPaymentMethodFilter();
        refundImportantInfoPage.selectPaymentMethodInFilter(paymentMethod);

        step("Verify That Selected Payment Method is Available");
        Assertion.assertThat("Payment method is available", refundImportantInfoPage.isResultForPaymentMethodAvailable(paymentMethod), is(true));

        step("Open Refund Important Info Selected Payment Method Details");
        refundImportantInfoPage.clickButtonDetails(paymentMethod);

        step("Verify That Selected Payment Method Details is Opened");
        Assertion.assertThat("Details of selected payment method is opened", refundImportantInfoPage.isModalDetailsOpened(), is(true));
    }

    @Author(names = {"Hafizh"})
    @Module(name = "RefundSLA")
    @Tribe(name = "Refund")
    @Vertical(name = "PROP")
    @Test
    public void testRefundSlaViewLogs() {
        Map<String, String> param = getParam("testRefundSlaViewLogs");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        RefundSlaPage<?> refundSlaPage = new RefundSlaPage<>(session);
        AdminCarePage<?> adminCarePage = new AdminCarePage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String partnerName = param.get("partnerName");

        step("Login to Admin Care");
        adminCarePage.goToAdminCarePage(username, password);

        step("Go To Refund SLA Page");
        adminCarePage.goToRefundPage();
        refundSlaPage.goToRefundSlaTab();

        step("Verify User is On Refund SLA Page");
        Assertion.assertThat("Refund SLA page is opened", refundSlaPage.isOnRefundSlaTab(), is(true));

        step("Filter List for Partner Name According to Test Data");
        refundSlaPage.clickButtonPartnerNameFilter();
        refundSlaPage.selectPartnerNameInFilter(partnerName);

        step("Verify That Selected Partner is Available");
        Assertion.assertThat("Partner name is available", refundSlaPage.isResultForPartnerNameAvailable(partnerName), is(true));

        step("Open Refund SLA Selected Partner Log");
        refundSlaPage.clickButtonAction();
        refundSlaPage.clickButtonSeeLog();

        step("Verify That Selected Partner Log is Opened");
        Assertion.assertThat("Log of selected partner is opened", refundSlaPage.isModalSeeLogOpened(), is(true));
    }

    @Author(names = {"Daffa"})
    @Module(name = "RefundSLA")
    @Tribe(name = "Refund")
    @Vertical(name = "PROP")
    @Test
    public void testRefundSlaFilterByPartnerName() {
        Map<String, String> param = getParam("testRefundSlaFilterByPartnerName");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        RefundSlaPage<?> refundSlaPage = new RefundSlaPage<>(session);
        AdminCarePage<?> adminCarePage = new AdminCarePage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String partnerName = param.get("partnerName");

        step("Login to Admin Care");
        adminCarePage.goToAdminCarePage(username, password);

        step("Go To Refund SLA Page");
        adminCarePage.goToRefundPage();
        refundSlaPage.goToRefundSlaTab();

        step("Verify User is On Refund SLA Page");
        Assertion.assertThat("Refund SLA page is opened", refundSlaPage.isOnRefundSlaTab(), is(true));

        step("Search Filter by Partner Name According to Test Data");
        refundSlaPage.clickButtonPartnerNameFilter();
        refundSlaPage.selectPartnerNameInFilter(partnerName);

        step("Verify That Selected Partner Name is Shown in Filter");
        Assertion.assertThat("Partner name is selected in filter", refundSlaPage.isPartnerNameSelectedInFilter(partnerName), is(true));

        step("Verify That Result for Selected Partner Name is Available");
        Assertion.assertThat("Partner name is available", refundSlaPage.isResultForPartnerNameAvailable(partnerName), is(true));

        step("Verify That All Result Rows are Filtered by Selected Partner Name");
        Assertion.assertThat("All result rows are for selected partner name", refundSlaPage.isAllResultsForPartnerName(partnerName), is(true));
    }

    @Author(names = {"Daffa"})
    @Module(name = "RefundImportantInfo")
    @Tribe(name = "Refund")
    @Vertical(name = "PROP")
    @Test
    public void testRefundImportantInfoUpdateStatusToActive() {
        Map<String, String> param = getParam("testRefundImportantInfoUpdateStatusToActive");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        RefundImportantInfoPage<?> refundImportantInfoPage = new RefundImportantInfoPage<>(session);
        AdminCarePage<?> adminCarePage = new AdminCarePage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String paymentMethod = param.get("paymentMethod");

        step("Login to Admin Care");
        adminCarePage.goToAdminCarePage(username, password);

        step("Go To Refund Important Info Page");
        adminCarePage.goToRefundPage();
        refundImportantInfoPage.goToRefundImportantInfoTab();

        step("Verify User is On Refund Important Info Page");
        Assertion.assertThat("Refund important info page is opened", refundImportantInfoPage.isOnRefundImportantInfoTab(), is(true));

        step("Filter List for Payment Method According to Test Data");
        refundImportantInfoPage.clickButtonPaymentMethodFilter();
        refundImportantInfoPage.selectPaymentMethodInFilter(paymentMethod);

        step("Verify That Selected Payment Method is Available");
        Assertion.assertThat("Payment method is available", refundImportantInfoPage.isResultForPaymentMethodAvailable(paymentMethod), is(true));

        step("Verify Initial Status of Selected Payment Method is Inactive");
        Assertion.assertThat("Status of selected payment method is inactive", refundImportantInfoPage.isStatusInactive(paymentMethod), is(true));

        step("Update Status of Selected Payment Method to Active");
        refundImportantInfoPage.clickStatusToggle(paymentMethod);

        step("Verify Status of Selected Payment Method is Updated to Active");
        Assertion.assertThat("Status of selected payment method is active", refundImportantInfoPage.isStatusActive(paymentMethod), is(true));

        step("Revert Status of Selected Payment Method Back to Inactive");
        refundImportantInfoPage.clickStatusToggle(paymentMethod);
        Assertion.assertThat("Status of selected payment method is reverted to inactive", refundImportantInfoPage.isStatusInactive(paymentMethod), is(true));
    }

    @Author(names = {"Abhishek Sharma"})
    @Module(name = "RefundMethodList")
    @Tribe(name = "Refund")
    @Vertical(name = "PROP")
    @Test
    public void testRefundMethodListFilterReset() {
        Map<String, String> param = getParam("testRefundMethodListFilterReset");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        AdminCarePage<?> adminCarePage = new AdminCarePage<>(session);
        RefundMethodListPage<?> refundMethodListPage = new RefundMethodListPage<>(session);

        String username = param.get("username");
        String password = param.get("password");

        step("Login to Admin Care");
        adminCarePage.goToAdminCarePage(username, password);

        step("Go To Refund Method List Page");
        adminCarePage.goToRefundPage();
        refundMethodListPage.goToRefundMethodListPage();
        Assertion.assertThat("Verify Refund Method List page is opened", refundMethodListPage.isOnRefundMethodListPage(), is(true));

        step("Input First Refund Method Identifier");
        int originalDataSize = refundMethodListPage.getTableValuesCount();
        String firstRefundMethodIdentifier = refundMethodListPage.getTableFirstValueByHeading("Refund Method Identifier");
        int searchedDataSize = refundMethodListPage.getTableValuesCount("Refund Method Identifier", firstRefundMethodIdentifier);
        refundMethodListPage.setFilterInputFieldValue(firstRefundMethodIdentifier);

        step("Click Search button");
        Assertion.assertThat("Verify search button is clickable", refundMethodListPage.isSearchButtonClickable(), is(true));
        refundMethodListPage.clickSearchButton();
        Assertion.assertThat("Verify after only searched is visible", refundMethodListPage.getTableValuesCount(), is(searchedDataSize));

        step("Click Reset Filter Button");
        Assertion.assertThat("Verify Reset Filter button is clickable", refundMethodListPage.isResetFilterButtonClickable(), is(true));
        refundMethodListPage.clickResetFilterButton();

        step("Verify Reset Data");
        Assertion.assertThat("Verify After Reset Data count is same as original data count", refundMethodListPage.getTableValuesCount(), is(originalDataSize));
        Assertion.assertThat("Verify Search Input box value is reset", refundMethodListPage.getFilterInputFieldValue(), equalTo(""));

    }

    @Author(names = {"Abhishek Sharma"})
    @Module(name = "ManualRefund")
    @Tribe(name = "Refund")
    @Vertical(name = "PROP")
    @Test
    public void testManualRefundFilterByRefundStatus() {
        Map<String, String> param = getParam("testManualRefundFilterByRefundStatus");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        AdminCarePage<?> adminCarePage = new AdminCarePage<>(session);
        ManualRefundPage<?> manualRefundPage = new ManualRefundPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String filterRefundStatus = param.get("filterRefundStatus");
        String refundCompletedMessage = param.get("refundCompletedMessage");

        step("Login to Admin Care");
        adminCarePage.goToAdminCarePage(username, password);

        step("Go To Manual Refund Page");
        adminCarePage.goToRefundPage();
        manualRefundPage.goToManualRefundPage();
        Assertion.assertThat("Verify Manual Refund page is opened", manualRefundPage.isOnManualRefundPage(), is(true));

        step("Select Filter: " + filterRefundStatus);
        manualRefundPage.selectFilterRefundStatus(filterRefundStatus);
        Assertion.assertThat("Verify selected filter appears", manualRefundPage.isFilterRefundStatusSelected(filterRefundStatus), is(true));

        step("Click Search button");
        manualRefundPage.clickSearchButton();

        step("Verify Data in Search Result");
        List<String> allRowsRefundStatus = manualRefundPage.getAllTableRowsData("Refund Status");
        Assertion.assertThat("Verify search result has at least one row", allRowsRefundStatus, is(not(empty())));
        for (int i = 0; i < allRowsRefundStatus.size(); i++) {
            Assertion.assertThat("Verify Refund status for %d element".formatted(i + 1), allRowsRefundStatus.get(i), containsStringIgnoringCase(refundCompletedMessage));
        }
    }

    @Author(names = {"Pankaj Singh Kushwaha"})
    @Module(name = "ManualRefund")
    @Tribe(name = "Refund")
    @Vertical(name = "PROP")
    @Test
    public void verifyCSFilterByRefundRequestID() {
        Map<String, String> param = getParam("verifyCSFilterByRefundRequestID");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        AdminCarePage<?> adminCarePage = new AdminCarePage<>(session);
        ManualRefundPage<?> manualRefundPage = new ManualRefundPage<>(session);

        String username = param.get("username");
        String password = param.get("password");

        step("Login to Admin Care");
        adminCarePage.goToAdminCarePage(username, password);

        step("Go To Manual Refund Page");
        adminCarePage.goToRefundPage();
        manualRefundPage.goToManualRefundPage();
        Assertion.assertThat("Verify Manual Refund page is opened", manualRefundPage.isOnManualRefundPage(), is(true));

        step("Get the refundId of the first row");
        String refundId = manualRefundPage.getFirstRowRefundRequestId();

        step("Enter the Refund Request ID as " + refundId);
        manualRefundPage.enterRefundRequestId(refundId);

        step("Click Search button");
        manualRefundPage.clickSearchButton();

        step("Verify the filter works and displays the correct Refund Request ID");
        manualRefundPage.verifyFilteredRefundRequest(refundId);
    }

    @Author(names = {"Pankaj Singh Kushwaha"})
    @Module(name = "ManualRefund")
    @Tribe(name = "Refund")
    @Vertical(name = "PROP")
    @Test
    public void verifyCSFilterByVerticalOrderType() {
        Map<String, String> param = getParam("verifyCSFilterByVerticalOrderType");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        AdminCarePage<?> adminCarePage = new AdminCarePage<>(session);
        ManualRefundPage<?> manualRefundPage = new ManualRefundPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String verticalType = param.get("verticalType");

        step("Login to Admin Care");
        adminCarePage.goToAdminCarePage(username, password);

        step("Go To Manual Refund Page");
        adminCarePage.goToRefundPage();
        manualRefundPage.goToManualRefundPage();
        Assertion.assertThat("Verify Manual Refund page is opened", manualRefundPage.isOnManualRefundPage(), is(true));

        step("Step: Select the order type from the Vertical dropdown as " + verticalType);
        manualRefundPage.selectOrderType(verticalType);

        step("Step: Verify the dropdown displays = " + verticalType);
        manualRefundPage.verifyOrderTypeSelected(verticalType);

        step("Click Search button");
        manualRefundPage.clickSearchButton();

        step("Verify Search Result is Based on Vertical Type =" + verticalType);
        List<String> allRowsVertical = manualRefundPage.getAllTableRowsData("Vertical");
        Assertion.assertThat("Verify search result has at least one row", allRowsVertical, is(not(empty())));
        for (int i = 0; i < allRowsVertical.size(); i++) {
            Assertion.assertThat("Verify Vertical Type for %d element".formatted(i + 1), allRowsVertical.get(i), containsStringIgnoringCase(verticalType));
        }
    }

    @Author(names = {"Aditi"})
    @Module(name = "PaymentLink")
    @Tribe(name = "Payment")
    @Vertical(name = "PROP")
    @Test
    public void testPaymentLinkCreatePayAndVerifyUsageLimit() {
        Map<String, String> param = getParam("testPaymentLinkCreatePayAndVerifyUsageLimit");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        PaymentDashboardPage<?> paymentDashboardPage = new PaymentDashboardPage<>(session);
        PaymentLinkManagementPage<?> paymentLinkManagementPage = new PaymentLinkManagementPage<>(session);
        PaymentLinkCreatePage<?> paymentLinkCreatePage = new PaymentLinkCreatePage<>(session);
        PaymentLinkConsumerPage<?> paymentLinkConsumerPage = new PaymentLinkConsumerPage<>(session);
        PaymentApi<?> paymentApi = new PaymentApi<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String paymentMethod = param.get("paymentMethod");
        String paymentGroup = param.get("paymentGroup");
        String paymentMethodConsumer = param.get("paymentMethodConsumer");
        String productTitleEn = param.get("productTitleEn");
        String productDescriptionEn = param.get("productDescriptionEn");
        String productTitleId = param.get("productTitleId");
        String productDescriptionId = param.get("productDescriptionId");
        String itemNameEn = param.get("itemNameEn");
        String itemNameId = param.get("itemNameId");
        long unitPrice = Long.parseLong(param.get("unitPrice"));
        int quantity = Integer.parseInt(param.get("quantity"));
        int maxNumberOfTimes = Integer.parseInt(param.get("maxNumberOfTimes"));

        LocalDateTime expiryTime = LocalDateTime.now().plusDays(1).withSecond(0).withNano(0);
        LocalDateTime bufferExpiryTime = expiryTime.plusDays(1);

        PaymentLinkConfig paymentLinkConfig = new PaymentLinkConfig();
        paymentLinkConfig.expiryTime = expiryTime;
        paymentLinkConfig.bufferExpiryTime = bufferExpiryTime;
        paymentLinkConfig.currency = "IDR";
        paymentLinkConfig.clientId = "4";
        paymentLinkConfig.maxNumberOfTimes = maxNumberOfTimes;
        paymentLinkConfig.productTitleEn = productTitleEn;
        paymentLinkConfig.productDescriptionEn = productDescriptionEn;
        paymentLinkConfig.productTitleId = productTitleId;
        paymentLinkConfig.productDescriptionId = productDescriptionId;
        paymentLinkConfig.itemNameEn = itemNameEn;
        paymentLinkConfig.itemNameId = itemNameId;
        paymentLinkConfig.unitPrice = unitPrice;
        paymentLinkConfig.quantity = quantity;
        paymentLinkConfig.paymentMethod = paymentMethod;
        paymentLinkConfig.paymentGroup = paymentGroup;
        paymentLinkConfig.paymentMethodConsumer = paymentMethodConsumer;

        step("Login to Admin Panel");
        paymentDashboardPage.goToAdminCarePage(username, password);
        paymentDashboardPage.openPaymentsPage();

        step("Navigate to Payment Link -> Link Management");
        paymentLinkManagementPage.navigateToLinkManagement();
        Assertion.assertThat("Link Management page is opened", paymentLinkManagementPage.isOnLinkManagementPage(), is(true));

        step("Click Create New Link");
        paymentLinkManagementPage.clickCreateNewLink();

        step("Configure and create payment link");
        paymentLinkCreatePage.createPaymentLink(paymentLinkConfig);
        String clientId = paymentLinkConfig.clientId;
        String createdPaymentLinkUrl = paymentLinkCreatePage.captureGeneratedPaymentLinkUrl();
        Assertion.assertThat("Payment link is created successfully", paymentLinkManagementPage.isPaymentLinkCreatedSuccessfully(), is(true));

        step("Return to Link Management and search for the newly created payment link");
        paymentLinkManagementPage.navigateToLinkManagement();
        paymentLinkManagementPage.searchPaymentLinkByClientId(clientId);
        Assertion.assertThat("Payment link is displayed in Link Management",
                paymentLinkManagementPage.isPaymentLinkUrlDisplayedInList(createdPaymentLinkUrl), is(true));
        Assertion.assertThat("Created payment link URL is shown in listing",
                paymentLinkManagementPage.getFirstRowPaymentLinkUrl(), containsString(createdPaymentLinkUrl.trim()));
        Assertion.assertThat("Client ID is correct", paymentLinkManagementPage.getFirstRowClientId(), containsString(clientId));
        Assertion.assertThat("Link usage shows 0 / 1 before payment", paymentLinkManagementPage.getFirstRowLinkUsage(), containsString("0 / 1"));

        step("Open generated payment link in a new browser tab and verify payment page details");
        paymentLinkConsumerPage.rememberAdminWindow();
        paymentLinkConsumerPage.openPaymentLinkInNewTab(createdPaymentLinkUrl);
        Assertion.assertThat("Payment page loads successfully", paymentLinkConsumerPage.isPaymentPageLoaded(), is(true));
        Assertion.assertThat("Product title is displayed correctly",
                paymentLinkConsumerPage.isProductTitleDisplayed(productTitleEn, productTitleId), is(true));
        Assertion.assertThat("Product description is displayed correctly",
                paymentLinkConsumerPage.isProductDescriptionDisplayed(productDescriptionEn, productDescriptionId), is(true));
        Assertion.assertThat("Base amount is displayed before selecting payment method",
                paymentLinkConsumerPage.getDisplayedTotalAmount(), is(unitPrice * quantity));
        Assertion.assertThat("Countdown timer is visible", paymentLinkConsumerPage.isCountdownTimerVisible(), is(true));
        Assertion.assertThat("Complete before section is visible", paymentLinkConsumerPage.isCompleteBeforeSectionVisible(), is(true));
        Assertion.assertThat("Payment method is available on page",
                paymentLinkConsumerPage.isPaymentMethodDisplayed(paymentMethodConsumer), is(true));

        step("Select payment method and verify updated total amount");
        paymentLinkConsumerPage.selectPaymentMethod(paymentMethodConsumer);
        Assertion.assertThat("Pay Now button is enabled after selecting payment method",
                paymentLinkConsumerPage.isPayNowButtonEnabled(), is(true));
        long totalWithTransactionFee = paymentLinkConsumerPage.getDisplayedTotalAmount();
        Assertion.assertThat("Total amount is updated after selecting payment method",
                totalWithTransactionFee, greaterThan(unitPrice * quantity));

        step("Open price detail and verify transaction fee breakdown");
        paymentLinkConsumerPage.openPriceDetail();
        Assertion.assertThat("Price detail is opened", paymentLinkConsumerPage.isPriceDetailOpened(), is(true));
        Assertion.assertThat("Transaction fee is displayed in price detail",
                paymentLinkConsumerPage.isTransactionFeeDisplayed(), is(true));
        Assertion.assertThat("Base amount plus transaction fee equals total",
                paymentLinkConsumerPage.verifyBaseAmountPlusTransactionFeeEqualsTotal(unitPrice * quantity), is(true));
        Assertion.assertThat("Price detail total matches displayed total",
                paymentLinkConsumerPage.getPriceDetailTotalAmount(), is(totalWithTransactionFee));
        paymentLinkConsumerPage.closePriceDetail();

        step("Pay and verify checkout page total");
        paymentLinkConsumerPage.clickPayNow();
        Assertion.assertThat("Checkout page total amount is correct",
                paymentLinkConsumerPage.getCheckoutPageTotalAmount(), is(totalWithTransactionFee));

        step("Get BCA Snap access token via API");
        String orderId = paymentLinkConsumerPage.getOrderId();
        String bcaSnapAccessToken = paymentApi.getBCAVASnapAccessToken();
        Assertion.assertThat("BCA Snap access token is retrieved", bcaSnapAccessToken, not(blankOrNullString()));

        step("Hit BCA Snap VA inquiry API");
        BCAVASnapInquiryResponse bcaSnapInquiryResponse = paymentApi.hitBCAVASnapInquiry(bcaSnapAccessToken, orderId);
        Assertion.assertThat("BCA Snap inquiry is successful", bcaSnapInquiryResponse.responseCode, is("2002400"));
        Assertion.assertThat("BCA Snap inquiry status is success",
                bcaSnapInquiryResponse.virtualAccountData.inquiryStatus, is("00"));

        step("Hit BCA Snap VA payment API");
        BCAVASnapPaymentResponse bcaSnapPaymentResponse = paymentApi.hitBCAVASnapPayment(
                bcaSnapAccessToken, orderId, totalWithTransactionFee, bcaSnapInquiryResponse);
        Assertion.assertThat("BCA Snap payment is successful",
                bcaSnapPaymentResponse.responseMessage.toLowerCase(), containsString("success"));

        step("Verify payment is completed successfully on consumer page");
        paymentLinkConsumerPage.reloadPaymentPage();
        Assertion.assertThat("Payment is completed successfully",
                paymentLinkConsumerPage.isPaymentCompletedSuccessfully(), is(true));
        paymentLinkConsumerPage.closeCurrentTabAndReturnToAdmin();

        step("Verify Link Usage is updated to 1 / 1 after payment");
        paymentLinkManagementPage.navigateToLinkManagement();
        paymentLinkManagementPage.searchPaymentLinkByClientId(clientId);
        Assertion.assertThat("Link usage shows 1 / 1 after payment", paymentLinkManagementPage.getFirstRowLinkUsage(), containsString("1 / 1"));

        step("Open the same payment link again and verify usage limit error");
        paymentLinkConsumerPage.openPaymentLinkInNewTab(createdPaymentLinkUrl);
        paymentLinkConsumerPage.selectPaymentMethod(paymentMethodConsumer);
        paymentLinkConsumerPage.clickPayNow();
        Assertion.assertThat("Limit reached message is displayed", paymentLinkConsumerPage.isLimitReachedMessageDisplayed(), is(true));
        paymentLinkConsumerPage.closeCurrentTabAndReturnToAdmin();

        step("Open payment link configuration from listing and verify latest details");
        paymentLinkManagementPage.searchPaymentLinkByClientId(clientId);
        paymentLinkManagementPage.openFirstRowConfiguration();
        Assertion.assertThat("Configuration page shows link usage 1 / 1", paymentLinkManagementPage.getConfigurationPageLinkUsage(), containsString("1 / 1"));
        Assertion.assertThat("Product details remain unchanged on configuration page", paymentLinkManagementPage.getConfigurationPageProductTitle(), anyOf(containsString(productTitleEn), containsString(productTitleId)));
        Assertion.assertThat("Payment method remains unchanged on configuration page", paymentLinkManagementPage.getConfigurationPagePaymentMethod(), containsString(paymentMethod));
        Assertion.assertThat("Link URL remains unchanged on configuration page", paymentLinkManagementPage.getConfigurationPageLinkUrl().trim(), is(createdPaymentLinkUrl.trim()));

        step("Verify Copy Link functionality respects usage limit");
        paymentLinkManagementPage.navigateToLinkManagement();
        paymentLinkManagementPage.searchPaymentLinkByClientId(clientId);
        String copiedUrl = paymentLinkManagementPage.clickCopyLinkOnFirstRow();
        Assertion.assertThat("Copied URL matches generated payment link", copiedUrl.trim(), is(createdPaymentLinkUrl.trim()));
        paymentLinkConsumerPage.openPaymentLinkInNewTab(copiedUrl);
        paymentLinkConsumerPage.selectPaymentMethod(paymentMethodConsumer);
        paymentLinkConsumerPage.clickPayNow();
        Assertion.assertThat("Limit reached message is displayed", paymentLinkConsumerPage.isLimitReachedMessageDisplayed(), is(true));
        paymentLinkConsumerPage.closeCurrentTabAndReturnToAdmin();
    }

    @Author(names = {"Aditi"})
    @Module(name = "CardPaymentConfiguration")
    @Tribe(name = "Payment")
    @Vertical(name = "PROP")
    @Test
    public void testInstallmentConfigurationLifecycle() throws InterruptedException, FileNotFoundException {
        Map<String, String> param = getParam("testInstallmentConfigurationLifecycle");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        PaymentDashboardPage<?> paymentDashboardPage = new PaymentDashboardPage<>(session);
        PaymentInstallmentPage<?> installmentPage = new PaymentInstallmentPage<>(session);
        PaymentApi<?> paymentApi = new PaymentApi<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String cardType = param.get("cardType");
        String recipientEmail = param.get("recipientEmail");
        String emailAction = param.get("emailAction");
        String acquirer = param.get("acquirer");
        String promoStatusActive = param.get("promoStatusActive");
        String promoStatusInactive = param.get("promoStatusInactive");
        String config = param.get("config");
        String database = param.get("database");
        String collectionName = param.get("collectionName");
        int maxIter = 10;

        LocalDate localDate = LocalDate.now();
        LocalDate endDate = localDate.plusDays(7);
        LocalDate updatedEndDate = localDate.plusDays(14);
        String uniqueSuffix = CoreFunctions.randomName(6);
        String bankName = CoreFunctions.generateRandomString(7) + localDate + uniqueSuffix;
        String cardName = CoreFunctions.generateRandomString(5) + localDate + uniqueSuffix;
        String promoTitle = CoreFunctions.generateRandomString(5) + localDate + uniqueSuffix;
        String updatedPromoTitle = promoTitle + "Updated";
        String inactiveMessageId = param.get("inactiveMessageId");
        String inactiveMessageEn = param.get("inactiveMessageEn");
        String updatedInactiveMessageId = inactiveMessageId + " Updated";
        String updatedInactiveMessageEn = inactiveMessageEn + " Updated";

        String duration1 = param.get("duration1");
        String duration1TotalInterest = param.get("duration1TotalInterest");
        String duration1Finance = param.get("duration1Finance");
        String duration1Bank = param.get("duration1Bank");
        String duration1MinPayment = param.get("duration1MinPayment");
        String duration1Reconcile = param.get("duration1Reconcile");

        String duration2 = param.get("duration2");
        String duration2TotalInterest = param.get("duration2TotalInterest");
        String duration2Finance = param.get("duration2Finance");
        String duration2Bank = param.get("duration2Bank");
        String duration2MinPayment = param.get("duration2MinPayment");
        String duration2Reconcile = param.get("duration2Reconcile");

        String duration3 = param.get("duration3");
        String duration3TotalInterest = param.get("duration3TotalInterest");
        String duration3Finance = param.get("duration3Finance");
        String duration3Bank = param.get("duration3Bank");
        String duration3MinPayment = param.get("duration3MinPayment");
        String duration3Reconcile = param.get("duration3Reconcile");

        String updatedDurationFinance = param.get("updatedDurationFinance");
        String updatedDurationBank = param.get("updatedDurationBank");
        String updatedDurationMinPayment = param.get("updatedDurationMinPayment");
        String updatedDurationReconcile = param.get("updatedDurationReconcile");
        String updatedDurationTotalInterest = param.get("updatedDurationTotalInterest");

        String userConsumer = param.get("userConsumer");
        String passConsumer = param.get("passConsumer");
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

        orderCreationPage<?> orderCreationPage = new orderCreationPage<>(session);

        step("Create waiting hotel order for installment detail API");
        orderCreationPage.createWaitingPaymentOrderHotel(userConsumer, passConsumer, startDate, hotelName,
                adult, night, room, orderFullName, orderNameTitle, guestFullName, guestTitle, "IDR");
        long referenceId = orderCreationPage.getOrderId();
        String orderHash = orderCreationPage.getOrderHash();
        String bookingAccessToken = orderCreationPage.getAccessToken();
        Assertion.assertThat("Waiting hotel order is created", referenceId, greaterThan(0L));
        Assertion.assertThat("Order hash is available", orderHash, not(blankOrNullString()));
        Assertion.assertThat("Booking access token is available", bookingAccessToken, not(blankOrNullString()));

        step("Login to Admin Panel");
        paymentDashboardPage.goToAdminCarePage(username, password);

        step("Navigate to Card Payment Configuration -> Installment Configuration");
        installmentPage.openPaymentInstallmentPage();
        Assertion.assertThat("Installment configuration page is opened", installmentPage.isPaymentInstallmentPageOpened(), is(true));

        step("Fetch bank name, card name, and promo name from first row");
        installmentPage.waitForInstallmentListLoaded();
        String existingBankName = installmentPage.getFirstRowBankName();
        String existingCardName = installmentPage.getFirstRowCardName();
        String existingPromoName = installmentPage.getFirstRowPromoTitle();
        String notedBin = installmentPage.getFirstRowBin();
        Assertion.assertThat("First row bank name is available", existingBankName, not(blankOrNullString()));
        Assertion.assertThat("First row card name is available", existingCardName, not(blankOrNullString()));
        Assertion.assertThat("First row promo name is available", existingPromoName, not(blankOrNullString()));
        Assertion.assertThat("First row BIN is available", notedBin, not(blankOrNullString()));

        step("Search existing installment by Bank Name and verify");
        installmentPage.searchInstallment("Bank Name", existingBankName);
        Assertion.assertThat("Existing installment is found by bank name", installmentPage.rowValueContains("0", "Bank Name", existingBankName), is(true));

        step("Search existing installment by Card Name and verify");
        installmentPage.resetInstallmentFilter();
        installmentPage.searchInstallment("Card Name", existingCardName);
        Assertion.assertThat("Existing installment is found by card name", installmentPage.rowValueContains("0", "Card Name", existingCardName), is(true));

        step("Search existing installment by Promo Name and verify");
        installmentPage.resetInstallmentFilter();
        installmentPage.searchInstallment("Promo Name", existingPromoName);
        Assertion.assertThat("Existing installment is found by promo name", installmentPage.rowValueContains("0", "Promo Title", existingPromoName), is(true));

        step("Create new installment configuration");
        installmentPage.clickOnCreateInstallment();
        installmentPage.setBankName(bankName);
        installmentPage.setCardName(cardName);
        String resolvedCardType = installmentPage.resolveCardType(cardType);
        installmentPage.selectCardType(cardType);
        List<String> selectedBins = installmentPage.selectFirstTwoBinsInModal();
        String primaryBin = selectedBins.get(0);
        installmentPage.selectRecipientEmail(recipientEmail);
        installmentPage.selectEmailAction(emailAction);
        installmentPage.setPromoTitle(promoTitle);
        installmentPage.selectDateOnCalendar(true, String.valueOf(localDate.getDayOfMonth()), CoreFunctions.getMonthName(localDate.getMonthValue(), "en"), String.valueOf(localDate.getYear()), maxIter);
        installmentPage.selectDateOnCalendar(false, String.valueOf(endDate.getDayOfMonth()), CoreFunctions.getMonthName(endDate.getMonthValue(), "en"), String.valueOf(endDate.getYear()), maxIter);
        installmentPage.selectPromoStatus(promoStatusActive);
        installmentPage.setInactiveMessageId(inactiveMessageId);
        installmentPage.setInactiveMessageEn(inactiveMessageEn);

        installmentPage.clickAddDuration();
        installmentPage.configureDuration(duration1, duration1TotalInterest, duration1Finance, duration1Bank, duration1MinPayment, duration1Reconcile);
        installmentPage.clickAddDuration();
        installmentPage.configureDuration(duration2, duration2TotalInterest, duration2Finance, duration2Bank, duration2MinPayment, duration2Reconcile);
        installmentPage.clickAddDuration();
        installmentPage.configureDuration(duration3, duration3TotalInterest, duration3Finance, duration3Bank, duration3MinPayment, duration3Reconcile);
        List<InstallmentDurationDetail> configuredDurations = installmentPage.getConfiguredDurationDetails();
        installmentPage.saveInstallment("Submit");

        step("Verify installment configuration is created successfully");
        installmentPage.openPaymentInstallmentPage();
        installmentPage.resetInstallmentFilter();
        installmentPage.searchInstallment("Promo Name", promoTitle);
        Assertion.assertThat("New installment appears in listing by promo name", installmentPage.rowValueContains("0", "Promo Title", promoTitle), is(true));
        Assertion.assertThat("Bank name is correct in listing", installmentPage.rowValueContains("0", "Bank Name", bankName), is(true));
        Assertion.assertThat("BIN is correct in listing", installmentPage.getNormalizedRowValue("0", "BIN"), anyOf(containsString(selectedBins.get(0))));
        Assertion.assertThat("Card type is correct in listing", installmentPage.rowValueContainsIgnoringCase("0", "Card Type", resolvedCardType), is(true));
        Assertion.assertThat("Card name is correct in listing", installmentPage.rowValueContains("0", "Card Name", cardName), is(true));
        Assertion.assertThat("Promo status is active in listing", installmentPage.rowValueContainsIgnoringCase("0", "Promo Status", promoStatusActive), is(true));

        step("Expand installment configuration and verify duration rows");
        installmentPage.expandFirstRow();
        Assertion.assertThat("Month details match configured duration values", installmentPage.monthDetailsMatchConfiguredValues(configuredDurations), is(true));

        step("Verify installment detail API for newly created installment");
        Map<Integer, Double> expectedChargePercentages = paymentApi.buildExpectedChargePercentages(
                duration1, duration1Finance, duration1Bank,
                duration2, duration2Finance, duration2Bank,
                duration3, duration3Finance, duration3Bank);
        InstallmentDetailResult createInstallmentDetailResult = paymentApi.hitInstallmentDetail(
                referenceId, primaryBin, bookingAccessToken, orderHash);
        paymentApi.assertInstallmentDetailResponse(createInstallmentDetailResult, "IDR", expectedChargePercentages);

        step("Edit installment configuration");
        installmentPage.clickEdit();
        installmentPage.setPromoTitle(updatedPromoTitle);
        updatedPromoTitle = promoTitle + updatedPromoTitle;
        installmentPage.selectEndDateOnEditCalendar(String.valueOf(updatedEndDate.getDayOfMonth()),
                CoreFunctions.getMonthName(updatedEndDate.getMonthValue(), "en"), String.valueOf(updatedEndDate.getYear()), maxIter);
        installmentPage.selectPromoStatus(promoStatusActive);
        installmentPage.setInactiveMessageId(updatedInactiveMessageId);
        installmentPage.setInactiveMessageEn(updatedInactiveMessageEn);
        installmentPage.setTotalInterest(updatedDurationTotalInterest);
        installmentPage.setForFinanceValue(updatedDurationFinance);
        installmentPage.setForBankValue(updatedDurationBank);
        installmentPage.setMinimumPayment(updatedDurationMinPayment);
        installmentPage.setReconcileRate(updatedDurationReconcile);
        List<InstallmentDurationDetail> expectedUpdatedDurations = List.of(
                new InstallmentDurationDetail(duration1, duration1TotalInterest, duration1Finance, duration1Bank, duration1MinPayment, duration1Reconcile),
                new InstallmentDurationDetail(duration2, duration2TotalInterest, duration2Finance, duration2Bank, duration2MinPayment, duration2Reconcile),
                new InstallmentDurationDetail(duration3, updatedDurationTotalInterest, updatedDurationFinance, updatedDurationBank, updatedDurationMinPayment, updatedDurationReconcile));
        installmentPage.saveInstallment("Save Changes");

        step("Verify updated installment in listing");
        installmentPage.searchInstallment("Promo Name", updatedPromoTitle);
        Assertion.assertThat("Updated promo title is displayed", installmentPage.rowValueContains("0", "Promo Title", updatedPromoTitle), is(true));
        installmentPage.expandFirstRow();
        Assertion.assertThat("Month details match updated duration values", installmentPage.monthDetailsMatchConfiguredValues(expectedUpdatedDurations), is(true));

        step("Verify installment detail API after update");
        Map<Integer, Double> updatedChargePercentages = paymentApi.buildExpectedChargePercentages(
                duration1, duration1Finance, duration1Bank,
                duration2, duration2Finance, duration2Bank,
                duration3, updatedDurationFinance, updatedDurationBank);
        InstallmentDetailResult updateInstallmentDetailResult = paymentApi.hitInstallmentDetail(
                referenceId, primaryBin, bookingAccessToken, orderHash);
        paymentApi.assertInstallmentDetailResponse(updateInstallmentDetailResult, "IDR", updatedChargePercentages);

        step("Verify configuration log for create and update actions");
        installmentPage.clickEdit();
        installmentPage.scrollToConfigurationLog();
        Assertion.assertThat("Configuration log captures latest update activity", installmentPage.getLatestConfigurationLogValue("Activity"), containsStringIgnoringCase("update"));
        Assertion.assertThat("Configuration log captures old value for latest update", installmentPage.getLatestConfigurationLogValue("Old Value"), not(blankOrNullString()));
        Assertion.assertThat("Configuration log captures new value for latest update", installmentPage.getLatestConfigurationLogValue("New Value"), not(blankOrNullString()));
        Assertion.assertThat("Configuration log captures time for latest update", installmentPage.getLatestConfigurationLogValue("Time"), not(blankOrNullString()));
        Assertion.assertThat("Configuration log captures create activity on last row", installmentPage.getCreateConfigurationLogValue("Activity"), containsStringIgnoringCase("create"));
        Assertion.assertThat("Configuration log captures new value for create", installmentPage.getCreateConfigurationLogValue("New Value"), not(blankOrNullString()));
        Assertion.assertThat("Configuration log captures time for create", installmentPage.getCreateConfigurationLogValue("Time"), not(blankOrNullString()));

        step("Deactivate installment configuration and verify listing and installment detail API behavior");
        installmentPage.openPaymentInstallmentPage();
        installmentPage.searchInstallment("Promo Name", updatedPromoTitle);
        installmentPage.clickEdit();
        installmentPage.selectPromoStatus(promoStatusInactive);
        installmentPage.saveInstallment("Save Changes");
        installmentPage.searchInstallment("Promo Name", updatedPromoTitle);
        Assertion.assertThat("Listing displays inactive promo status", installmentPage.rowValueContainsIgnoringCase("0", "Promo Status", promoStatusInactive), is(true));
        InstallmentDetailResult inactiveInstallmentDetailResult = paymentApi.hitInstallmentDetailUntilInactive(
                referenceId, primaryBin, bookingAccessToken, orderHash);
        paymentApi.assertInactiveInstallmentDetailResponse(inactiveInstallmentDetailResult);

        step("Verify configuration log after deactivate");
        installmentPage.clickEdit();
        installmentPage.scrollToConfigurationLog();
        Assertion.assertThat("Latest configuration log activity is update after deactivate",
                installmentPage.getLatestConfigurationLogValue("Activity"), containsStringIgnoringCase("update"));
        Assertion.assertThat("Latest configuration log reflects inactive promo status",
                installmentPage.getLatestConfigurationLogValue("New Value"), containsString("\"promoStatus\":false"));
        Assertion.assertThat("Create entry remains on last configuration log row",
                installmentPage.getCreateConfigurationLogValue("Activity"), containsStringIgnoringCase("create"));

        step("Reactivate installment configuration and verify listing and installment detail API");
        installmentPage.openPaymentInstallmentPage();
        installmentPage.resetInstallmentFilter();
        installmentPage.searchInstallment("Promo Name", updatedPromoTitle);
        installmentPage.clickEdit();
        installmentPage.selectPromoStatus(promoStatusActive);
        installmentPage.saveInstallment("Save Changes");

        installmentPage.searchInstallment("Promo Name", updatedPromoTitle);
        Assertion.assertThat("Listing displays active promo status", installmentPage.rowValueContainsIgnoringCase("0", "Promo Status", promoStatusActive), is(true));
        InstallmentDetailResult activeInstallmentDetailResult = paymentApi.hitInstallmentDetailUntilActive(
                referenceId, primaryBin, bookingAccessToken, orderHash);
        paymentApi.assertInactiveToActiveInstallmentDetailResponse(activeInstallmentDetailResult);

        step("Verify configuration log after reactivate");
        installmentPage.clickEdit();
        installmentPage.scrollToConfigurationLog();
        Assertion.assertThat("Latest configuration log activity is update after reactivate",
                installmentPage.getLatestConfigurationLogValue("Activity"), containsStringIgnoringCase("update"));
        Assertion.assertThat("Latest configuration log reflects active promo status",
                installmentPage.getLatestConfigurationLogValue("New Value"), containsString("\"promoStatus\":true"));
        Assertion.assertThat("Create entry remains on last configuration log row",
                installmentPage.getCreateConfigurationLogValue("Activity"), containsStringIgnoringCase("create"));
    }

    @Author(names = {"Pankaj Singh Kushwaha"})
    @Module(name = "ManualRefund")
    @Tribe(name = "Refund")
    @Vertical(name = "PROP")
    @Test
    public void VerifyCSExportTheCSVFile() throws Exception {
        Map<String, String> param = getParam("VerifyCSExportTheCSVFile");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        AdminCarePage<?> adminCarePage = new AdminCarePage<>(session);
        ManualRefundPage<?> manualRefundPage = new ManualRefundPage<>(session);

        String username = param.get("username");
        String password = param.get("password");

        step("Login to Admin Care");
        adminCarePage.goToAdminCarePage(username, password);

        step("Go To Manual Refund Page");
        adminCarePage.goToRefundPage();
        manualRefundPage.goToManualRefundPage();
        Assertion.assertThat("Verify Manual Refund page is opened", manualRefundPage.isOnManualRefundPage(), is(true));

        step("Search and pick the first order's Order Id and Request Date");
        manualRefundPage.clickSearchButton();
        String orderId = manualRefundPage.getFirstRowOrderId();
        LocalDate requestDate = manualRefundPage.getFirstRowRequestDate();

        step("Filter by Order Id " + orderId + " and its request date " + requestDate);
        manualRefundPage.filterByOrderIdAndDateRange(orderId, requestDate, requestDate);
        manualRefundPage.clickSearchButton();
        Assertion.assertThat("Verify results are displayed for the filter", manualRefundPage.isSearchResultsDisplayed(), is(true));

        String refundRequestId = manualRefundPage.getFirstRowRefundRequestId();

        step("Export to CSV and verify the downloaded file name");
        File csvFile = manualRefundPage.exportToCsv();
        try {
            Assertion.assertThat("Verify downloaded file name is ManualRefund.csv", csvFile.getName(), is(ManualRefundPage.EXPORTED_CSV_NAME));

            step("Verify the details in the downloaded CSV");
            manualRefundPage.verifyExportedCsvDetails(csvFile, orderId, refundRequestId);
        } finally {
            step("Delete the downloaded file");
            manualRefundPage.deleteDownloadedFile(csvFile);
        }
    }

    @Author(names = {"Suryansh Shukla"})
    @Module(name = "Refund")
    @Tribe(name = "Sphinx")
    @Vertical(name = "Platform")
    @Test
    public void verifyUserClickDetails() throws InterruptedException, FileNotFoundException {
        Map<String, String> param = getParam("verifyUserClickDetails");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        PaymentDashboardPage<?> paymentDashboardPage = new PaymentDashboardPage<>(session);
        RefundRulesPage<?> RefundRulesPage = new RefundRulesPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String configRulesUrl = param.get("configRulesUrl");
        String ruleName = param.get("ruleName");

        step("Login to Admin Panel");
        paymentDashboardPage.goToAdminCarePage(username, password);

        step("Go To the URL");
        paymentDashboardPage.goToURL(configRulesUrl);

        step("Search rule name");
        RefundRulesPage.inputSearchKeyword(ruleName);

        step("Hit the search button");
        RefundRulesPage.clickSearchButton();

        step("Click Details in rule");
        RefundRulesPage.clickDetailsByRuleName(ruleName);

        step("Verify Refund Rules Details is Opened");
        Assertion.assertThat("Details can be opened", RefundRulesPage.isRuleDetailsDisplayed(), is(true));

        step("Verify Rule Name in details modal matches the searched rule: " + ruleName);
        String actualRuleName = RefundRulesPage.getRuleNameValueInDetails();
        Assertion.assertThat("Rule Name in details modal matches searched rule", actualRuleName, containsString(ruleName));
    }
    @Author(names = { "Suryansh Shukla" })
    @Module(name = "Refund")
    @Tribe(name = "Sphinx")
    @Vertical(name = "Platform")
    @Test
    public void verifyFilterSpecialRefundReasonByVertical() throws InterruptedException, FileNotFoundException {
        Map<String, String> param = getParam("verifyFilterSpecialRefundReasonByVertical");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        PaymentDashboardPage<?> paymentDashboardPage = new PaymentDashboardPage<>(session);
        RefundRulesPage<?> refundRulesPage = new RefundRulesPage<>(session);
        AdminCareDetailPage<?> adminCareDetailPage = new AdminCareDetailPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String targetUrl = param.get("targetUrl");
        String verticalName = param.get("verticalName");

        step("Login to Admin Care");
        paymentDashboardPage.goToAdminCarePage(username, password);

        step("Go to Special Refund Reason page");
        paymentDashboardPage.goToURL(targetUrl);

        step("Filter using vertical name: " + verticalName);
        adminCareDetailPage.selectVerticalFilter(verticalName);

        step("Hit the search button");
        refundRulesPage.clickSearchButton();

        step("Verify filtered data displays vertical: " + verticalName);
        String actualVertical = adminCareDetailPage.getFirstRowVerticalValue();
        Assertion.assertThat("Filter is working normally", !actualVertical.isEmpty(), equalTo(true));
        Assertion.assertThat("Vertical column matches selected filter", actualVertical, equalToIgnoringCase(verticalName));
    }

    @Author(names = { "Pankaj Singh Kushwaha" })
    @Module(name = "RefundForm")
    @Tribe(name = "Refund")
    @Vertical(name = "PROP")
    @Test
    public void testRefundCalculationPromoGVBreakdownRoundTripItemized() throws ExecutionException, InterruptedException {
        Map<String, String> param = getParam("testRefundCalculationPromoGVBreakdownRoundTripItemized");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        AdminCarePage<?> adminCarePage = new AdminCarePage<>(session);
        AdminCareDetailPage<?> adminCareDetailPage = new AdminCareDetailPage<>(session);
        WhooshApi<?> whooshApi = new WhooshApi<>(session);
        PaymentApi<?> paymentApi = new PaymentApi<>(session);

        String adminUsername = param.get("adminUsername");
        String adminPassword = param.get("adminPassword");
        int adult = Integer.parseInt(param.get("adultCount"));
        int infant = Integer.parseInt(param.get("infantCount"));
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int duration = Integer.parseInt(param.get("duration"));

        step("Create Whoosh round trip with " + adult + " adult + " + infant + " infant");
        String departureDate = CoreFunctions.addToDateYYYY_MM_DD(CoreFunctions.getCurrentDateYYYY_MM_DD(), daysToDepartureDate);
        String returnDate = CoreFunctions.addToDateYYYY_MM_DD(departureDate, duration);
        AdminCareDetailPage.RefundPassengers passengers = adminCareDetailPage.generateRefundPassengers(adult, infant);
        WhooshApi.Order order = whooshApi.createOrderWhoosh(param.get("username"), param.get("password"), param.get("origin"),
                param.get("destination"), departureDate, returnDate, param.get("phone"), passengers.adultNames(), passengers.infantNames());
        if (order == null) Assertion.skip("No Whoosh journey with enough seats on " + departureDate + " / " + returnDate + ". Skipping the subsequent steps.");

        step("Get the price before promo, pick a promo from the suggestion, apply it and pay the order with Sakuku");
        String accessToken = whooshApi.accessToken;
        long priceBeforePromo = paymentApi.getPriceBeforePromo(accessToken, order.orderId(), order.orderHash());
        PaymentCorePromoSuggestionResponse.Promo promo = paymentApi.choosePromoFromSuggestion(accessToken, order.orderId(), order.orderHash(), "IDR", "TRAIN", priceBeforePromo);
        if (promo == null) Assertion.skip("No promo suggested for the order, skipping subsequent steps");
        PaymentCorePromoApplyResponse promoResponse = paymentApi.applyPromoGv(accessToken, order.orderId(), order.orderHash(), promo.code, "IDR", "", "TRAIN", priceBeforePromo, CoreFunctions.generateDeviceId());
        Assertion.assertThat("Promo " + promo.code + " is applied", promoResponse.code, is("SUCCESS"));
        String referenceId = paymentApi.submitSakukuPayment(accessToken, Long.parseLong(order.orderId()), order.orderHash(), true);
        String paidPrice = String.valueOf(Math.round(paymentApi.grandTotal));
        CoreFunctions.log("Promo " + promo.code + " chosen (discount " + promo.amount + "), price before promo " + priceBeforePromo + " -> paid " + paidPrice);
        Assertion.assertThat("Promo is reflected in the Sakuku grand total", paymentApi.grandTotal, lessThan((double) priceBeforePromo));
        Assertion.assertThat("Sakuku payment is successful", paymentApi.paidCheckerStatus(120, referenceId == null || referenceId.isBlank() ? order.orderId() : referenceId), equalToIgnoringCase("SUCCESS"));

        step("Login to Admin Care");
        adminCarePage.goToAdminCarePage(adminUsername, adminPassword);

        step("Search and Open Order Id");
        adminCarePage.searchOrderId(order.orderId(), 5);
        adminCarePage.selectOrderListId(0);

        step("Go to Transaction/Invoice tab and verify the paid price after promo");
        adminCareDetailPage.clickTransactionInvoiceTab();
        Assertion.assertThat("is price after promo correct", adminCareDetailPage.verifyPriceDetailFlight(paidPrice), is(true));

        step("Capture the price of each pax per journey and the promo deduction from Price Details");
        adminCareDetailPage.clickPriceDetailButton();
        Map<String, Map<String, Long>> pricePerJourney = adminCareDetailPage.getPriceDetailsPerJourney();
        Map<String, Long> deductions = adminCareDetailPage.getPriceDeductions();
        adminCareDetailPage.closePriceDetails();
        CoreFunctions.log("Price per pax per journey -> " + pricePerJourney + ", deductions -> " + deductions);
        Assertion.assertThat("Promo deduction is the applied discount", deductions.get("Promo code"), is(Math.round(promo.amount)));
        Assertion.assertThat("Price details has a fare for both journeys of the round trip", pricePerJourney.size(), is(2));
        long fareTotal = adminCareDetailPage.getFareTotal(pricePerJourney);
        CoreFunctions.log("Fare total " + fareTotal + ", so fees on the order are " + (priceBeforePromo - fareTotal));
        Assertion.assertThat("Fare total is within the price before promo", fareTotal, lessThanOrEqualTo(priceBeforePromo));

        step("Open E-Refund by CS from the Refund/Cancel tab");
        adminCareDetailPage.clickRefundCancelTab();
        adminCareDetailPage.clickAlternateERefundByCsButton();

        step("Pick the refund reason");
        adminCareDetailPage.selectRefundReason(param.getOrDefault("refundReason", "Personal Reason"));

        Map<String, Long> departFares = pricePerJourney.values().iterator().next();
        adminCareDetailPage.verifyPromoDeductionPerSelectedPassengers(passengers, departFares, fareTotal, deductions.get("Promo code"));
    }


    @Author(names = {"Vaishnavi Mishra"})
    @Module(name = "SpecialRejectReason")
    @Tribe(name = "Refund")
    @Vertical(name = "PROP")
    @Test
    public void createSpecialRejectReasonTest() throws Exception{
        Map<String, String> param = getParam("createSpecialRejectReasonTest");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        AdminCarePage<?> adminCarePage = new AdminCarePage<>(session);
        RefundSpecialRejectReasonPage<?> specialRejectReasonPage = new RefundSpecialRejectReasonPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String engReason=param.get("engReason");
        String indoReason=param.get("indoReason");

        step("Login to Admin Care");
        adminCarePage.goToAdminCarePage(username, password);

        step("Go to Special Reject Reason Configuration page");
        adminCarePage.goToRefundPage();
        specialRejectReasonPage.goToSpecialRejectReasonPage();
        Assertion.assertThat("Verify Special Rejcet Reason page is opened", specialRejectReasonPage.isOnSpecialRejectReasonPage(), is(true));

        step("Click Create new Reason");
        specialRejectReasonPage.clickCreateNewReason();

        step("Put EN rejection reason");
        specialRejectReasonPage.inputEnglishReason(engReason);

        step("Put ID rejection reason");
        specialRejectReasonPage.inputIndonesianReason(indoReason);

        step("Put vertical");
        specialRejectReasonPage.selectVertical("Payment");

        step("Click Submit Special Refund");
        specialRejectReasonPage.clickSubmitSpecialRefund();

        step("Navigate to last page and verify created reason exists in the last row");
        boolean isCreated = specialRejectReasonPage.isReasonInLastRow(engReason);
        Assertion.assertThat("Verify created reason exists in last row", isCreated, is(true));

        step("Delete created reason to keep test idempotent");
        specialRejectReasonPage.deleteReasonByText(engReason);

    }
}

