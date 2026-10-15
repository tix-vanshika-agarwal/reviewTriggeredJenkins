package com.tiket.web.dashboard.test.pricing.smartrebooking;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.web.dashboard.page.member_dashboard.MemberLoginPage;
import com.tiket.web.dashboard.page.member_dashboard.MemberPage;
import com.tiket.web.dashboard.page.member_dashboard.PricingDashboardHome;
import com.tiket.web.dashboard.page.pricing.MemberLoginPagePricing.MemberLoginPagePricingPage;
import com.tiket.web.dashboard.page.pricing.SubsidyBudgeting.SubsidyBudgetingPage;
import com.tiket.web.dashboard.page.pricing.smartrebooking.SmartRebookingPage;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.core.Is.is;

public class SmartRebookingTest extends BaseTest {

    @Author(names = {"Nadil"})
    @Module(name = "SmartRebooking")
    @Tribe(name = "Poseidon")
    @Vertical(name = "Pricing")
    @Test
    public void testAccessSmartRebookingPage() {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        Map<String, String> param = getParam("testAccessSmartRebookingPage");
        BaseTest.paramsThreadLocal.set(param);

        MemberLoginPagePricingPage memberLoginHelper = new MemberLoginPagePricingPage();
        SmartRebookingPage<?> smartRebookingPage = new SmartRebookingPage<>(session);

        memberLoginHelper.ensurePricingDashboardIsLoaded(param);

        step("Access Smart Rebooking Page");
        smartRebookingPage.clickSmartRebookingSidebarButton();
        Assertion.assertThat("Smart Rebooking Page is loaded", smartRebookingPage.isSmartRebookingPageLoaded(), is(true));
    }

    @Author(names = {"Nadil"})
    @Module(name = "SmartRebooking")
    @Tribe(name = "Poseidon")
    @Vertical(name = "Pricing")
    @Test
    public void testFilterByOrderID() {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        Map<String, String> param = getParam("testFilterByOrderID");
        BaseTest.paramsThreadLocal.set(param);

        MemberLoginPagePricingPage memberLoginHelper = new MemberLoginPagePricingPage();
        SmartRebookingPage<?> smartRebookingPage = new SmartRebookingPage<>(session);

        memberLoginHelper.ensurePricingDashboardIsLoaded(param);

        step("Access Smart Rebooking Page");
        smartRebookingPage.clickSmartRebookingSidebarButton();
        Assertion.assertThat("Smart Rebooking Page is loaded", smartRebookingPage.isSmartRebookingPageLoaded(), is(true));

        step("Input Order ID");
        List<String> orderIds = List.of(param.get("order_id_1"), param.get("order_id_2"), param.get("order_id_3"));
        smartRebookingPage.inputOrderId(orderIds);

        step("Search for Order ID");
        smartRebookingPage.clickSmartRebookingSearchButton();
        Assertion.assertThat("Order ID exist", smartRebookingPage.isOrderIDExist(orderIds), is(true));
    }

    @Author(names = {"Nadil"})
    @Module(name = "SmartRebooking")
    @Tribe(name = "Poseidon")
    @Vertical(name = "Pricing")
    @Test
    public void testFilterByOrderDetailID() {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        Map<String, String> param = getParam("testFilterByOrderDetailID");
        BaseTest.paramsThreadLocal.set(param);

        MemberLoginPagePricingPage memberLoginHelper = new MemberLoginPagePricingPage();
        SmartRebookingPage<?> smartRebookingPage = new SmartRebookingPage<>(session);

        memberLoginHelper.ensurePricingDashboardIsLoaded(param);

        step("Access Smart Rebooking Page");
        smartRebookingPage.clickSmartRebookingSidebarButton();
        Assertion.assertThat("Smart Rebooking Page is loaded", smartRebookingPage.isSmartRebookingPageLoaded(), is(true));

        step("Input Order Detail ID");
        List<String> orderDetailIds = List.of(param.get("order_detail_id_1"), param.get("order_detail_id_2"), param.get("order_detail_id_3"));
        smartRebookingPage.inputOrderDetailId(orderDetailIds);

        step("Search for Order Detail ID");
        smartRebookingPage.clickSmartRebookingSearchButton();
        Assertion.assertThat("Order Detail ID exist", smartRebookingPage.isOrderDetailIDExist(orderDetailIds), is(true));
    }

    @Author(names = {"Nadil"})
    @Module(name = "SmartRebooking")
    @Tribe(name = "Poseidon")
    @Vertical(name = "Pricing")
    @Test
    public void testFilterByItineraryID() {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        Map<String, String> param = getParam("testFilterByItineraryID");
        BaseTest.paramsThreadLocal.set(param);

        MemberLoginPagePricingPage memberLoginHelper = new MemberLoginPagePricingPage();
        SmartRebookingPage<?> smartRebookingPage = new SmartRebookingPage<>(session);

        memberLoginHelper.ensurePricingDashboardIsLoaded(param);

        step("Access Smart Rebooking Page");
        smartRebookingPage.clickSmartRebookingSidebarButton();
        Assertion.assertThat("Smart Rebooking Page is loaded", smartRebookingPage.isSmartRebookingPageLoaded(), is(true));

        step("Input Itinerary ID");
        List<String> itineraryIds = List.of(param.get("itinerary_id_1"), param.get("itinerary_id_2"), param.get("itinerary_id_3"));
        smartRebookingPage.inputItinerarylId(itineraryIds);

        step("Search for Itinerary ID");
        smartRebookingPage.clickSmartRebookingSearchButton();
        Assertion.assertThat("Itinerary ID exist", smartRebookingPage.isItineraryIDExist(itineraryIds), is(true));
    }

    @Author(names = {"Nadil"})
    @Module(name = "SmartRebooking")
    @Tribe(name = "Poseidon")
    @Vertical(name = "Pricing")
    @Test
    public void testFilterByRoomSupplierName() {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        Map<String, String> param = getParam("testFilterByRoomSupplierName");
        BaseTest.paramsThreadLocal.set(param);

        MemberLoginPagePricingPage memberLoginHelper = new MemberLoginPagePricingPage();
        SmartRebookingPage<?> smartRebookingPage = new SmartRebookingPage<>(session);

        memberLoginHelper.ensurePricingDashboardIsLoaded(param);

        step("Access Smart Rebooking Page");
        smartRebookingPage.clickSmartRebookingSidebarButton();
        Assertion.assertThat("Smart Rebooking Page is loaded", smartRebookingPage.isSmartRebookingPageLoaded(), is(true));

        step("Input Room Supplier Name");
        String roomSupplierName = param.get("room_supplier_name");
        smartRebookingPage.inputRoomSupplierName(roomSupplierName);

        step("Search for Room Supplier Name");
        smartRebookingPage.clickSmartRebookingSearchButton();
        Assertion.assertThat("Room Supplier Name name exist", smartRebookingPage.isRoomSupplierNameExist(roomSupplierName), is(true));
    }

    @Author(names = {"Nadil"})
    @Module(name = "SmartRebooking")
    @Tribe(name = "Poseidon")
    @Vertical(name = "Pricing")
    @Test
    public void testSortByOrderStatus() {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        Map<String, String> param = getParam("testSortByOrderStatus");
        BaseTest.paramsThreadLocal.set(param);

        MemberLoginPagePricingPage memberLoginHelper = new MemberLoginPagePricingPage();
        SmartRebookingPage<?> smartRebookingPage = new SmartRebookingPage<>(session);

        memberLoginHelper.ensurePricingDashboardIsLoaded(param);

        step("Access Smart Rebooking Page");
        smartRebookingPage.clickSmartRebookingSidebarButton();
        Assertion.assertThat("Smart Rebooking Page is loaded", smartRebookingPage.isSmartRebookingPageLoaded(), is(true));

        step("Search Order Status `E-Ticket Issued`");
        smartRebookingPage.clickSmartRebookingResetFilterButton();
        smartRebookingPage.inputOrderStatusAll(); // set to inactive
        smartRebookingPage.inputOrderStatusEticketIssued();
        smartRebookingPage.clickSmartRebookingSearchButton();

        step("Search Order Status `Processing Refund`");
        smartRebookingPage.clickSmartRebookingResetFilterButton();
        smartRebookingPage.inputOrderStatusAll(); // set to inactive
        smartRebookingPage.inputOrderStatusProcessingRefund();
        smartRebookingPage.clickSmartRebookingSearchButton();

        step("Search Order Status `Refunded`");
        smartRebookingPage.clickSmartRebookingResetFilterButton();
        smartRebookingPage.inputOrderStatusAll(); // set to inactive
        smartRebookingPage.inputOrderStatusRefunded();
        smartRebookingPage.clickSmartRebookingSearchButton();

        step("Search Order Status `Rescheduled`");
        smartRebookingPage.clickSmartRebookingResetFilterButton();
        smartRebookingPage.inputOrderStatusAll(); // set to inactive
        smartRebookingPage.inputOrderStatusRescheduled();
        smartRebookingPage.clickSmartRebookingSearchButton();

        step("Search Order Status `Rebooked`");
        smartRebookingPage.clickSmartRebookingResetFilterButton();
        smartRebookingPage.inputOrderStatusAll(); // set to inactive
        smartRebookingPage.inputOrderStatusRebooked();
        smartRebookingPage.clickSmartRebookingSearchButton();

        step("Search Order Status `Inactive`");
        smartRebookingPage.clickSmartRebookingResetFilterButton();
        smartRebookingPage.inputOrderStatusAll(); // set to inactive
        smartRebookingPage.inputOrderStatusInactive();
        smartRebookingPage.clickSmartRebookingSearchButton();
    }

    @Author(names = {"Nadil"})
    @Module(name = "SmartRebooking")
    @Tribe(name = "Poseidon")
    @Vertical(name = "Pricing")
    @Test
    public void testSortByAllocation() {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        Map<String, String> param = getParam("testSortByAllocation");
        BaseTest.paramsThreadLocal.set(param);

        MemberLoginPagePricingPage memberLoginHelper = new MemberLoginPagePricingPage();
        SmartRebookingPage<?> smartRebookingPage = new SmartRebookingPage<>(session);

        memberLoginHelper.ensurePricingDashboardIsLoaded(param);

        step("Access Smart Rebooking Page");
        smartRebookingPage.clickSmartRebookingSidebarButton();
        Assertion.assertThat("Smart Rebooking Page is loaded", smartRebookingPage.isSmartRebookingPageLoaded(), is(true));

        step("Search Allocation `Tiket`");
        smartRebookingPage.clickSmartRebookingResetFilterButton();
        smartRebookingPage.inputAllocationAll(); // set to inactive
        smartRebookingPage.inputAllocationTiket();
        smartRebookingPage.clickSmartRebookingSearchButton();

        step("Search Allocation `Elbows`");
        smartRebookingPage.clickSmartRebookingResetFilterButton();
        smartRebookingPage.inputAllocationAll(); // set to inactive
        smartRebookingPage.inputAllocationElbows();
        smartRebookingPage.clickSmartRebookingSearchButton();

        step("Search Allocation `Unknown`");
        smartRebookingPage.clickSmartRebookingResetFilterButton();
        smartRebookingPage.inputAllocationAll(); // set to inactive
        smartRebookingPage.inputAllocationUnknown();
        smartRebookingPage.clickSmartRebookingSearchButton();
    }

    @Author(names = {"Nadil"})
    @Module(name = "SmartRebooking")
    @Tribe(name = "Poseidon")
    @Vertical(name = "Pricing")
    @Test
    public void testSortByOrderCreatedDate() {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        Map<String, String> param = getParam("testSortByOrderCreatedDate");
        BaseTest.paramsThreadLocal.set(param);

        MemberLoginPagePricingPage memberLoginHelper = new MemberLoginPagePricingPage();
        SmartRebookingPage<?> smartRebookingPage = new SmartRebookingPage<>(session);

        memberLoginHelper.ensurePricingDashboardIsLoaded(param);

        step("Access Smart Rebooking Page");
        smartRebookingPage.clickSmartRebookingSidebarButton();
        Assertion.assertThat("Smart Rebooking Page is loaded", smartRebookingPage.isSmartRebookingPageLoaded(), is(true));

        smartRebookingPage.clickPaginationRowsPerPageX("4"); // Show 50 rows

        step("Sort Order Created Date");
        smartRebookingPage.clickSmartRebookingResetFilterButton();
        smartRebookingPage.sortByOrderCreatedDate();
        smartRebookingPage.sortByOrderCreatedDate();

        step("Sort Order Created Descending");
        smartRebookingPage.clickSmartRebookingResetFilterButton();
        smartRebookingPage.sortByOrderCreatedDate();
        Assertion.assertThat("Order Created Date is sorted descending", smartRebookingPage.isOrderCreatedDateSortedDescending(), is(true));

        step("Sort Order Created Ascending");
        smartRebookingPage.sortByOrderCreatedDate();
        Assertion.assertThat("Order Created Date is sorted ascending", smartRebookingPage.isOrderCreatedDateSortedAscending(), is(true));
    }

    @Author(names = {"Nadil"})
    @Module(name = "SmartRebooking")
    @Tribe(name = "Poseidon")
    @Vertical(name = "Pricing")
    @Test
    public void testPagination() {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        Map<String, String> param = getParam("testPagination");
        BaseTest.paramsThreadLocal.set(param);

        MemberLoginPagePricingPage memberLoginHelper = new MemberLoginPagePricingPage();
        SmartRebookingPage<?> smartRebookingPage = new SmartRebookingPage<>(session);

        memberLoginHelper.ensurePricingDashboardIsLoaded(param);

        step("Access Smart Rebooking Page");
        smartRebookingPage.clickSmartRebookingSidebarButton();
        Assertion.assertThat("Smart Rebooking Page is loaded", smartRebookingPage.isSmartRebookingPageLoaded(), is(true));

        step("Pagination Next Page");
        smartRebookingPage.scrollOnceAndClickNextPage(2);

        step("Pagination Previous Page");
        smartRebookingPage.scrollOnceAndClickPreviousPage(2);

        step("Pagination Max Page");
        String MAX_PAGE = smartRebookingPage.getMaxPage();
        smartRebookingPage.clickPaginationMaxPage(MAX_PAGE);

        step("Pagination Min Page");
        String MIN_PAGE = smartRebookingPage.getMinPage();
        smartRebookingPage.clickPaginationMinPage(MIN_PAGE);

        step("Pagination 20 Rows");
        smartRebookingPage.clickPaginationRowsPerPageX("1");
        Assertion.assertThat("Page 20 Rows is loaded", smartRebookingPage.isPaginationRowsPerPageX("20"), is(true));

        step("Pagination 30 Rows");
        smartRebookingPage.clickPaginationRowsPerPageX("2");
        Assertion.assertThat("Page 30 Rows is loaded", smartRebookingPage.isPaginationRowsPerPageX("30"), is(true));

        step("Pagination 40 Rows");
        smartRebookingPage.clickPaginationRowsPerPageX("3");
        Assertion.assertThat("Page 40 Rows is loaded", smartRebookingPage.isPaginationRowsPerPageX("40"), is(true));

        step("Pagination 50 Rows");
        smartRebookingPage.clickPaginationRowsPerPageX("4");
        Assertion.assertThat("Page 50 Rows is loaded", smartRebookingPage.isPaginationRowsPerPageX("50"), is(true));

        step("Pagination 10 Rows");
        smartRebookingPage.clickPaginationRowsPerPageX("0");
        Assertion.assertThat("Page 10 Rows is loaded", smartRebookingPage.isPaginationRowsPerPageX("10"), is(true));
    }

    @Author(names = {"Nadil"})
    @Module(name = "SmartRebooking")
    @Tribe(name = "Poseidon")
    @Vertical(name = "Pricing")
    @Test
    public void testNoDataMatched() {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        Map<String, String> param = getParam("testNoDataMatched");
        BaseTest.paramsThreadLocal.set(param);

        MemberLoginPagePricingPage memberLoginHelper = new MemberLoginPagePricingPage();
        SmartRebookingPage<?> smartRebookingPage = new SmartRebookingPage<>(session);

        memberLoginHelper.ensurePricingDashboardIsLoaded(param);

        step("Access Smart Rebooking Page");
        smartRebookingPage.clickSmartRebookingSidebarButton();
        Assertion.assertThat("Smart Rebooking Page is loaded", smartRebookingPage.isSmartRebookingPageLoaded(), is(true));

        step("Input RandomOrder ID");
        smartRebookingPage.inputRandomOrderId();
        smartRebookingPage.clickSmartRebookingSearchButton();
        Assertion.assertThat("No Data Found", smartRebookingPage.isNoDataFound(), is(true));

        step("Click Reset Filter Button");
        smartRebookingPage.clickNoDataFoundResetFilterButton();
    }

    @Author(names = {"Nadil"})
    @Module(name = "SmartRebooking")
    @Tribe(name = "Poseidon")
    @Vertical(name = "Pricing")
    @Test
    public void testTableExists() {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        Map<String, String> param = getParam("testTableExists");
        BaseTest.paramsThreadLocal.set(param);

        MemberLoginPagePricingPage memberLoginHelper = new MemberLoginPagePricingPage();
        SmartRebookingPage<?> smartRebookingPage = new SmartRebookingPage<>(session);

        memberLoginHelper.ensurePricingDashboardIsLoaded(param);

        step("Access Smart Rebooking Page");
        smartRebookingPage.clickSmartRebookingSidebarButton();
        Assertion.assertThat("Smart Rebooking Page is loaded", smartRebookingPage.isSmartRebookingPageLoaded(), is(true));

        step("Check `No` Table Exists");
        Assertion.assertThat("Table `No` Exists", smartRebookingPage.isTableExists("No"), is(true));

        step("Check `Order ID` Table Exists");
        Assertion.assertThat("Table `Order ID` Exists", smartRebookingPage.isTableExists("Order ID"), is(true));

        step("Check `Order Detail ID` Table Exists");
        Assertion.assertThat("Table `Order Detail ID` Exists", smartRebookingPage.isTableExists("Order Detail ID"), is(true));

        step("Check `Extra Margin` Table Exists");
        Assertion.assertThat("Table `Extra Margin` Exists", smartRebookingPage.isTableExists("Extra Margin"), is(true));

        step("Check `Cummulative Extra Margin` Table Exists");
        Assertion.assertThat("Table `Cummulative Extra Margin` Exists", smartRebookingPage.isTableExists("Cummulative Extra Margin"), is(true));

        step("Check `Itinerary ID` Table Exists");
        Assertion.assertThat("Table `Itinerary ID` Exists", smartRebookingPage.isTableExists("Itinerary ID"), is(true));

        step("Check `Hotel Name` Table Exists");
        Assertion.assertThat("Table `Hotel Name` Exists", smartRebookingPage.isTableExists("Hotel Name"), is(true));

        step("Check `Room Supplier Name` Table Exists");
        Assertion.assertThat("Table `Room Supplier Name` Exists", smartRebookingPage.isTableExists("Room Supplier Name"), is(true));

        step("Check `Check-in Date` Table Exists");
        Assertion.assertThat("Table `Check-in Date` Exists", smartRebookingPage.isTableExists("Check-in Date"), is(true));

        step("Check `Check-out Date` Table Exists");
        Assertion.assertThat("Table `Check-out Date` Exists", smartRebookingPage.isTableExists("Check-out Date"), is(true));

        step("Check `Action` Table Exists");
        Assertion.assertThat("Table `Action` Exists", smartRebookingPage.isTableExists("Action"), is(true));

        step("Check `Order Status` Table Exists");
        Assertion.assertThat("Table `Order Status` Exists", smartRebookingPage.isTableSpanExists("Order Status"), is(true));

        step("Check `Allocation` Table Exists");
        Assertion.assertThat("Table `Allocation` Exists", smartRebookingPage.isTableSpanExists("Allocation"), is(true));

        step("Check `Order Created Date` Table Exists");
        Assertion.assertThat("Table `Order Created Date` Exists", smartRebookingPage.isTableSpanExists("Order Created Date"), is(true));
    }

    @Author(names = {"Nadil"})
    @Module(name = "SmartRebooking")
    @Tribe(name = "Poseidon")
    @Vertical(name = "Pricing")
    @Test
    public void testActionFields() {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        Map<String, String> param = getParam("testActionFields");
        BaseTest.paramsThreadLocal.set(param);

        MemberLoginPagePricingPage memberLoginHelper = new MemberLoginPagePricingPage();
        SmartRebookingPage<?> smartRebookingPage = new SmartRebookingPage<>(session);

        memberLoginHelper.ensurePricingDashboardIsLoaded(param);

        step("Access Smart Rebooking Page");
        smartRebookingPage.clickSmartRebookingSidebarButton();
        Assertion.assertThat("Smart Rebooking Page is loaded", smartRebookingPage.isSmartRebookingPageLoaded(), is(true));

        step("Click `More` Button");
        smartRebookingPage.clickActionMoreButton();
        Assertion.assertThat("Action Activity Log Button Exists", smartRebookingPage.isActionActityLogButtonExists(), is(true));
        Assertion.assertThat("Action Price Drop Detail Button Exists", smartRebookingPage.isActionPriceDropDetailButtonExists(), is(true));

        step("`Activity Log` Modal");
        smartRebookingPage.clickActionActivityLogButton();
        Assertion.assertThat("Activity Log Modal Exists", smartRebookingPage.isActionActivityLogTitleExists(), is(true));
        smartRebookingPage.clickActionModalCloseButton();

        step("`Price Drop Detail` Modal");
        smartRebookingPage.clickActionMoreButton();
        smartRebookingPage.clickActionPriceDropDetailButton();
        Assertion.assertThat("Price Drop Detail Modal Exists", smartRebookingPage.isActionPriceDropDetailTitleExists(), is(true));
        smartRebookingPage.clickActionModalCloseButton();
    }

    @Author(names = {"Nadil"})
    @Module(name = "SmartRebooking")
    @Tribe(name = "Poseidon")
    @Vertical(name = "Pricing")
    @Test
    public void testResetFilterButton() {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        Map<String, String> param = getParam("testResetFilterButton");
        BaseTest.paramsThreadLocal.set(param);

        MemberLoginPagePricingPage memberLoginHelper = new MemberLoginPagePricingPage();
        SmartRebookingPage<?> smartRebookingPage = new SmartRebookingPage<>(session);

        memberLoginHelper.ensurePricingDashboardIsLoaded(param);

        step("Access Smart Rebooking Page");
        smartRebookingPage.clickSmartRebookingSidebarButton();
        Assertion.assertThat("Smart Rebooking Page is loaded", smartRebookingPage.isSmartRebookingPageLoaded(), is(true));

        step("Input Random Order ID");
        smartRebookingPage.inputRandomOrderId();
        smartRebookingPage.clickSmartRebookingSearchButton();

        step("Click Reset Filter Button");
        smartRebookingPage.clickSmartRebookingResetFilterButton();
        Assertion.assertThat("Reset Filter Button is clicked", smartRebookingPage.isSmartRebookingPageLoaded(), is(true));
    }

    @Author(names = {"Muksal"})
    @Module(name = "SmartRebooking")
    @Tribe(name = "Pricing")
    @Vertical(name = "Pricing")
    @Test
    public void testVerifyFilterSortAndActions() {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        Map<String, String> param = getParam("testVerifyFilterSortAndActions");
        BaseTest.paramsThreadLocal.set(param);

        MemberLoginPagePricingPage memberLoginHelper = new MemberLoginPagePricingPage();
        SmartRebookingPage<?> smartRebookingPage = new SmartRebookingPage<>(session);

        memberLoginHelper.ensurePricingDashboardIsLoaded(param);
        smartRebookingPage.maximize();

        step("Access Smart Rebooking Page");
        smartRebookingPage.clickSmartRebookingSidebarButton();
        Assertion.assertThat("Smart Rebooking Page is loaded", smartRebookingPage.isSmartRebookingPageLoaded(), is(true));

        // ── POSITIVE: Filter by Order ID ────────────────────────────────────
        step("[Filter - OrderID] Input valid Order ID(s) and search");
        List<String> orderIds = Stream.of(param.get("order_id_1"), param.get("order_id_2"), param.get("order_id_3"))
                .filter(id -> id != null && !id.isBlank()).collect(Collectors.toList());
        smartRebookingPage.inputOrderId(orderIds);
        smartRebookingPage.clickSmartRebookingSearchButton();
        Assertion.assertThat("Records matching Order ID(s) are displayed", smartRebookingPage.isOrderIDExist(orderIds), is(true));

        // ── POSITIVE: Filter by Order Detail ID ─────────────────────────────
        step("[Filter - OrderDetailID] Input valid Order Detail ID(s) and search");
        List<String> orderDetailIds = Stream.of(param.get("order_detail_id_1"), param.get("order_detail_id_2"), param.get("order_detail_id_3"))
                .filter(id -> id != null && !id.isBlank()).collect(Collectors.toList());
        smartRebookingPage.inputOrderDetailId(orderDetailIds);
        smartRebookingPage.clickSmartRebookingSearchButton();
        Assertion.assertThat("Records matching Order Detail ID(s) are displayed", smartRebookingPage.isOrderDetailIDExist(orderDetailIds), is(true));

        // ── POSITIVE: Filter by Itinerary ID ────────────────────────────────
        step("[Filter - ItineraryID] Input valid Itinerary ID(s) and search");
        List<String> itineraryIds = Stream.of(param.get("itinerary_id_1"), param.get("itinerary_id_2"), param.get("itinerary_id_3"))
                .filter(id -> id != null && !id.isBlank()).collect(Collectors.toList());
        smartRebookingPage.inputItinerarylId(itineraryIds);
        smartRebookingPage.clickSmartRebookingSearchButton();
        Assertion.assertThat("Records matching Itinerary ID(s) are displayed", smartRebookingPage.isItineraryIDExist(itineraryIds), is(true));

        // ── POSITIVE: Filter by Room Supplier Name ───────────────────────────
        step("[Filter - RoomSupplierName] Input valid Room Supplier Name and search");
        String roomSupplierName = param.get("room_supplier_name");
        smartRebookingPage.inputRoomSupplierName(roomSupplierName);
        smartRebookingPage.clickSmartRebookingSearchButton();
        Assertion.assertThat("Records matching Room Supplier Name are displayed", smartRebookingPage.isRoomSupplierNameExist(roomSupplierName), is(true));

        // ── POSITIVE: Sort by Order Status ──────────────────────────────────
        step("[Sort - OrderStatus] Filter by Eticket Issued and verify records are displayed");
        smartRebookingPage.inputOrderStatusAll();
        smartRebookingPage.inputOrderStatusEticketIssued();
        smartRebookingPage.clickSmartRebookingSearchButton();
        smartRebookingPage.clickSmartRebookingResetFilterButton();

        step("[Sort - OrderStatus] Filter by Processing Refund and verify records are displayed");
        smartRebookingPage.inputOrderStatusAll();
        smartRebookingPage.inputOrderStatusProcessingRefund();
        smartRebookingPage.clickSmartRebookingSearchButton();
        smartRebookingPage.clickSmartRebookingResetFilterButton();

        step("[Sort - OrderStatus] Filter by Refunded and verify records are displayed");
        smartRebookingPage.inputOrderStatusAll();
        smartRebookingPage.inputOrderStatusRefunded();
        smartRebookingPage.clickSmartRebookingSearchButton();
        smartRebookingPage.clickSmartRebookingResetFilterButton();

        // ── POSITIVE: Sort by Allocation ────────────────────────────────────
        step("[Sort - Allocation] Filter by Tiket and verify records are displayed");
        smartRebookingPage.inputAllocationAll();
        smartRebookingPage.inputAllocationTiket();
        smartRebookingPage.clickSmartRebookingSearchButton();
        smartRebookingPage.clickSmartRebookingResetFilterButton();

        step("[Sort - Allocation] Filter by Elbows and verify records are displayed");
        smartRebookingPage.inputAllocationAll();
        smartRebookingPage.inputAllocationElbows();
        smartRebookingPage.clickSmartRebookingSearchButton();
        smartRebookingPage.clickSmartRebookingResetFilterButton();

        // ── NEGATIVE: No results for non-existent / invalid data ────────────
        step("[Search - Negative] Input non-existent data and verify 'No results found' message");
        smartRebookingPage.inputRandomOrderId();
        smartRebookingPage.clickSmartRebookingSearchButton();
        Assertion.assertThat("'No results found' message is displayed", smartRebookingPage.isNoDataFound(), is(true));

        step("[Search - Negative] Reset filter to restore the page");
        smartRebookingPage.clickSmartRebookingResetFilterButton();
        Assertion.assertThat("Page is restored after reset", smartRebookingPage.isSmartRebookingPageLoaded(), is(true));

        step("Logout");
        smartRebookingPage.LogOut();
    }

    @Author(names = {"Nadil"})
    @Module(name = "SmartRebooking")
    @Tribe(name = "Poseidon")
    @Vertical(name = "Pricing")
    @Test
    public void testUserHasNoAccess() {
        Map<String, String> param = getParam("testUserHasNoAccess");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        MemberPage<?> memberPage = new MemberPage<>(session);
        PricingDashboardHome<?> pricingDashboardHome = new PricingDashboardHome<>(session);
        SmartRebookingPage<?> smartRebookingPage = new SmartRebookingPage<>(session);

        if (pricingDashboardHome.isPricingDashboardLoaded()) {
            step("Logout user");
            smartRebookingPage.LogOut();
        }

        if (!pricingDashboardHome.isPricingDashboardLoaded()) {
            String username = param.get("username");
            String password = param.get("password");

            step("Go to Member Login Page");
            memberLoginPage.goToPage("/admin/member/v2/login");

            step("Input CREDENTIALS AND LOGIN");
            memberLoginPage.adminLogin(username, password);

            step("Verify Member Page is Loaded or not");
            Assertion.assertThat("Member page is loaded", memberPage.isMemberDashboardLoaded(), is(true));

            step("Click to open Pricing Dashboard");
            memberPage.goToPricingDashboard(username, password);
            Assertion.assertThat("Pricing Dashboard is loaded", pricingDashboardHome.isPricingDashboardLoaded(), is(true));
        }

        step("User has no Smart Rebooking Page Access");
        smartRebookingPage.clickSmartRebookingSidebarButton();
        Assertion.assertThat("Smart Rebooking Page is not loaded", smartRebookingPage.isSmartRebookingPageLoaded(), is(false));
    }
}