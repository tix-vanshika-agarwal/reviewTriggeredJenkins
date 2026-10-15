package com.tiket.web.dashboard.test.ttd;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.apiPage.TTDSupplierSyncApi;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.web.WebTest;
import com.tiket.web.dWeb.page.payment.PaymentPage;
import com.tiket.web.dashboard.page.member_dashboard.MemberLoginPage;
import com.tiket.web.dashboard.page.ttd.CMS.CMSInventoryManagementProductListPage.CMSInventoryManagementProductListPageWeb;
import com.tiket.web.dashboard.page.ttd.CMS.CMSInventoryManagementThingsToDoListPage.CMSInventoryManagementThingsToDoListPageWeb;
import com.tiket.web.dashboard.page.ttd.CMS.CMSLoginPage.CMSLoginPageWeb;
import com.tiket.web.dashboard.page.ttd.CMS.CMSProductManagementEditProductPage.CMSProductManagementEditProductPageWeb;
import com.tiket.web.dashboard.page.ttd.CMS.CMSProductManagementSchedulePackagePage.CMSProductManagementSchedulePackagePageWeb;
import com.tiket.web.dashboard.page.ttd.CMS.CMSProductManagementSchedulePackageSeatMapPage.CMSProductManagementSchedulePackageSeatMapPageWeb;
import com.tiket.web.dashboard.page.ttd.CMS.CMSProductSettingPage.CMSProductSettingSeatMapWeb;
import com.tiket.web.dashboard.page.ttd.CMS.CMSProductSettingPartner.CMSProductSettingPartnerWeb;
import com.tiket.web.dashboard.page.ttd.CMS.CMSProductSettingSeatMapFormPage.CMSProductSettingSeatMapFormPageWeb;
import com.tiket.web.dashboard.page.ttd.CMS.CMSProductSettingSeatMapPage.CMSProductSettingSeatMapPageWeb;
import com.tiket.web.dashboard.page.ttd.CMS.CMSProgressLogPage.CMSProgressLogWeb;
import com.tiket.web.dashboard.page.ttd.CMS.CMSPromoAndLoyaltyPromotionPage.CMSPromoAndLoyaltyPromotionPageWeb;
import com.tiket.web.dashboard.page.ttd.CMS.CMSSupplierConfigToDoListPage.CMSSupplierConfigToDoListPageWeb;
import com.tiket.web.dashboard.page.ttd.CMS.CMSPromoAndLoyaltyCampaignPage.CMSPromoAndLoyaltyCampaignPageWeb;
import com.tiket.web.dashboard.page.ttd.CMS.CMSThingsToDoReviewPage.CMSThingsToDoReviewPageWeb;
import com.tiket.web.dashboard.page.ttd.CMS.CMSPromoAndLoyaltyBundlePage.CMSPromoAndLoyaltyBundlePageWeb;
import com.tiket.web.dashboard.page.ttd.Ignis.IgnisManageOrderPage.IgnisManageOrderPageWeb;
import com.tiket.web.dashboard.page.ttd.Ignis.IgnisManageTicketDetailsPage.IgnisManageTicketDetailsPageWeb;
import com.tiket.web.dashboard.page.ttd.Ignis.IgnisPartnerBookingFormPage.IgnisPartnerBookingFormPageWeb;
import com.tiket.web.dashboard.page.ttd.Ignis.IgnisPartnerBookingPage.IgnisPartnerBookingPageWeb;
import com.tiket.web.dashboard.page.ttd.Ignis.IgnisProgressLogPage.IgnisProgressLogPageWeb;
import com.tiket.web.dashboard.page.ttd.Ignis.IgnisRedeemVoucherPage.IgnisRedeemVoucherPageWeb;
import com.tiket.web.dashboard.page.ttd.Ignis.IgnisRefundConfigPage.IgnisRefundConfigPageWeb;
import com.tiket.web.dashboard.page.ttd.Ignis.IgnisSelfRedemptionPage.IgnisSelfRedemptionPageWeb;
import com.tiket.web.dashboard.page.ttd.Ignis.IgnisThingsToDoEditPage.IgnisThingsToDoEditPageWeb;
import com.tiket.web.dashboard.page.ttd.Ignis.IgnisTicketingManagementSystemPage.IgnisTicketingManagementSystemPageWeb;
import com.tiket.web.dashboard.page.ttd.Ignis.IgnisUpdateThingsToDoListPage.IgnisUpdateThingsToDoListPageWeb;
import com.tiket.web.dashboard.page.ttd.Ignis.LoginPage.IgnisLoginPageWeb;
import com.tiket.web.dashboard.page.ttd.Ignis.OrderReportPage.IgnisOrderReportPageWeb;
import com.tiket.web.dashboard.page.ttd.Ignis.PartnerPage.IgnisPartnerPageWeb;
import com.tiket.web.dashboard.page.ttd.Ignis.SelectPartnerPage.IgnisSelectPartnerPageWeb;
import com.tiket.web.dashboard.page.ttd.Ignis.ThingsToDoListPage.IgnisThingsToDoListPageWeb;
import com.tiket.web.page.common.home.HomePageWeb;
import com.tiket.web.page.common.home.LoginPageWeb;
import com.tiket.web.page.order.OrderDetail.OrderDetailWeb;
import com.tiket.web.page.order.OrderList.OrderListWeb;
import com.tiket.web.page.ttd.CMS.TTDInventoryManagementPage;
import com.tiket.web.page.ttd.TtdBookingFormPage.TtdBookingFormPageWeb;
import com.tiket.web.page.ttd.TtdLandingPage.TtdLandingPageWeb;
import com.tiket.web.page.ttd.TtdProductDetailPage.TtdProductDetailPageWeb;
import com.tiket.web.page.ttd.TtdSearchResultPage.TtdSearchResultPageWeb;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import javax.security.auth.login.FailedLoginException;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import static com.tiket.testdata.TestData.getObjectParams;
import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;

public class TtdTest extends WebTest {

    @Author(names = {"Akbar"})
    @Module(name = "LatestOrder")
    @Tribe(name = "Ignis")
    @Vertical(name = "TTD")
    @Test
    public void testLatestOrderSeeAllOrder() {
        Map<String, String> param = getParam("testLatestOrderSeeAllOrder");
        BaseTest.paramsThreadLocal.set(param);

        String username = param.get("username");
        String password = param.get("password");

        String partnerName = param.get("partnerName");
        String productName = param.get("productName");
        String quickFilter = param.get("quickFilter");

        Session<?> session = BaseTest.sessionThreadLocal.get();
        IgnisLoginPageWeb<?> ignisLoginPageWeb = IgnisLoginPageWeb.getInstance(session);
        IgnisOrderReportPageWeb<?> ignisOrderReportPageWeb = IgnisOrderReportPageWeb.getInstance(session);
        IgnisSelectPartnerPageWeb<?> ignisSelectPartnerPageWeb = IgnisSelectPartnerPageWeb.getInstance(session);
        IgnisPartnerPageWeb<?> ignisPartnerPageWeb = IgnisPartnerPageWeb.getInstance(session);

        step("Login as a user");
        ignisLoginPageWeb.loginAsSpecificUser(username, password);

        step("Select Partner");
        ignisSelectPartnerPageWeb.selectPartner(partnerName);

        step("Select product and apply quick filter");
        ignisPartnerPageWeb.selectLatestOrderProduct(productName);
        ignisPartnerPageWeb.applyLatestOrderQuickFilter(quickFilter);

        step("Click See All Orders on Latest Order widget on Homepage");
        ignisPartnerPageWeb.clickLatestOrderSeeAllOrder();

        step("Check latest order result");
        ignisOrderReportPageWeb.clickApplyFilter();
        int totalOrderCountText = ignisOrderReportPageWeb.getTotalOrderCountText();
        int totalOrderCountTableRow = ignisOrderReportPageWeb.getTotalOrderCountTableRow();

        step("Assertions");
        Assertion.assertThat("Total order and Table row in sync", totalOrderCountText, greaterThanOrEqualTo(totalOrderCountTableRow));
        Assertion.assertThat("Verify all table row is having valid date according to filter", ignisOrderReportPageWeb.verifyTransactionDateWithFilterDate(), is(true));
    }

    @Author(names = {"Akbar"})
    @Module(name = "LatestOrder")
    @Tribe(name = "Ignis")
    @Vertical(name = "TTD")
    @Test(dataProvider = "testLatestOrderQuickFilterDataProvider")
    public void testLatestOrderQuickFilter(Map<String, String> param) {
        BaseTest.paramsThreadLocal.set(param);

        String username = param.get("username");
        String password = param.get("password");

        String partnerName = param.get("partnerName");
        String productName = param.get("productName");
        String quickFilter = param.get("quickFilter");

        Session<?> session = BaseTest.sessionThreadLocal.get();
        IgnisLoginPageWeb<?> ignisLoginPageWeb = IgnisLoginPageWeb.getInstance(session);
        IgnisSelectPartnerPageWeb<?> ignisSelectPartnerPageWeb = IgnisSelectPartnerPageWeb.getInstance(session);
        IgnisPartnerPageWeb<?> ignisPartnerPageWeb = IgnisPartnerPageWeb.getInstance(session);

        step("Login as a user");
        ignisLoginPageWeb.loginAsSpecificUser(username, password);

        step("Select Partner");
        ignisSelectPartnerPageWeb.selectPartner(partnerName);

        step("Select product and apply quick filter");
        ignisPartnerPageWeb.selectLatestOrderProduct(productName);
        ignisPartnerPageWeb.applyLatestOrderQuickFilter(quickFilter);
        int totalTicketsCountText = ignisPartnerPageWeb.getLatestOrderTotalTicketsText();
        int totalTicketsCountTableRow = ignisPartnerPageWeb.getLatestOrderTotalTicketsTableRow();

        step("Assertions");
        Assertion.assertThat("Total tickets and Table row in sync", totalTicketsCountText, greaterThanOrEqualTo(totalTicketsCountTableRow));
        Assertion.assertThat("Verify all table row is having valid date according to filter", ignisPartnerPageWeb.verifyTransactionDateWithQuickFilter(quickFilter), is(true));
    }

    @DataProvider(name = "testLatestOrderQuickFilterDataProvider")
    public Object[][] testLatestOrderQuickFilterDataProvider() {
        return getObjectParams("testLatestOrderQuickFilter");
    }

    @Author(names = {"Akbar"})
    @Module(name = "UpcomingVisitor")
    @Tribe(name = "Ignis")
    @Vertical(name = "TTD")
    @Test
    public void testUpcomingVisitorSeeAllOrder() {
        Map<String, String> param = getParam("testUpcomingVisitorSeeAllOrder");
        BaseTest.paramsThreadLocal.set(param);

        String username = param.get("username");
        String password = param.get("password");

        String partnerName = param.get("partnerName");
        String productName = param.get("productName");
        String quickFilter = param.get("quickFilter");

        Session<?> session = BaseTest.sessionThreadLocal.get();
        IgnisLoginPageWeb<?> ignisLoginPageWeb = IgnisLoginPageWeb.getInstance(session);
        IgnisOrderReportPageWeb<?> ignisOrderReportPageWeb = IgnisOrderReportPageWeb.getInstance(session);
        IgnisSelectPartnerPageWeb<?> ignisSelectPartnerPageWeb = IgnisSelectPartnerPageWeb.getInstance(session);
        IgnisPartnerPageWeb<?> ignisPartnerPageWeb = IgnisPartnerPageWeb.getInstance(session);

        step("Login as a user");
        ignisLoginPageWeb.loginAsSpecificUser(username, password);

        step("Select Partner");
        ignisSelectPartnerPageWeb.selectPartner(partnerName);

        step("Select product and apply quick filter");
        ignisPartnerPageWeb.selectUpcomingVisitorProduct(productName);
        ignisPartnerPageWeb.applyUpcomingVisitorQuickFilter(quickFilter);

        step("Click See All Orders on Latest Order widget on Homepage");
        ignisPartnerPageWeb.clickUpcomingVisitorSeeAllOrder();

        step("Check latest order result");
        ignisOrderReportPageWeb.clickApplyFilter();
        int totalOrderCountText = ignisOrderReportPageWeb.getTotalOrderCountText();
        int totalOrderCountTableRow = ignisOrderReportPageWeb.getTotalOrderCountTableRow();

        step("Assertions");
        Assertion.assertThat("Total order and Table row in sync", totalOrderCountText, greaterThanOrEqualTo(totalOrderCountTableRow));
        Assertion.assertThat("Verify all table row is having valid date according to filter", ignisOrderReportPageWeb.verifyVisitDateWithFilterDate(), is(true));
    }

    @Author(names = {"Akbar"})
    @Module(name = "LatestOrder")
    @Tribe(name = "Ignis")
    @Vertical(name = "TTD")
    @Test(dataProvider = "testUpcomingVisitorQuickFilterDataProvider")
    public void testUpcomingVisitorQuickFilter(Map<String, String> param) {
        BaseTest.paramsThreadLocal.set(param);

        String username = param.get("username");
        String password = param.get("password");

        String partnerName = param.get("partnerName");
        String productName = param.get("productName");
        String quickFilter = param.get("quickFilter");

        Session<?> session = BaseTest.sessionThreadLocal.get();
        IgnisLoginPageWeb<?> ignisLoginPageWeb = IgnisLoginPageWeb.getInstance(session);
        IgnisSelectPartnerPageWeb<?> ignisSelectPartnerPageWeb = IgnisSelectPartnerPageWeb.getInstance(session);
        IgnisPartnerPageWeb<?> ignisPartnerPageWeb = IgnisPartnerPageWeb.getInstance(session);

        step("Login as a user");
        ignisLoginPageWeb.loginAsSpecificUser(username, password);

        step("Select Partner");
        ignisSelectPartnerPageWeb.selectPartner(partnerName);

        step("Select product and apply quick filter");
        ignisPartnerPageWeb.selectUpcomingVisitorProduct(productName);
        ignisPartnerPageWeb.applyUpcomingVisitorQuickFilter(quickFilter);
        int totalTicketsCountText = ignisPartnerPageWeb.getUpcomingVisitorTotalTicketsText();
        int totalTicketsCountTableRow = ignisPartnerPageWeb.getUpcomingVisitorTotalTicketsTableRow();

        step("Assertions");
        Assertion.assertThat("Total tickets and Table row in sync", totalTicketsCountText, greaterThanOrEqualTo(totalTicketsCountTableRow));
        Assertion.assertThat("Verify all table row is having valid date according to filter", ignisPartnerPageWeb.verifyVisitDateWithQuickFilter(quickFilter), is(true));
    }

    @DataProvider(name = "testUpcomingVisitorQuickFilterDataProvider")
    public Object[][] testUpcomingVisitorQuickFilterDataProvider() {
        return getObjectParams("testUpcomingVisitorQuickFilter");
    }

    @Author(names = {"Akbar"})
    @Module(name = "ThingsToDoList")
    @Tribe(name = "Ignis")
    @Vertical(name = "TTD")
    @Test
    public void testIgnisSeeAllThingsToDo() {
        Map<String, String> param = getParam("testIgnisSeeAllThingsToDo");
        BaseTest.paramsThreadLocal.set(param);

        String username = param.get("username");
        String password = param.get("password");

        String partnerName = param.get("partnerName");

        Session<?> session = BaseTest.sessionThreadLocal.get();
        IgnisLoginPageWeb<?> ignisLoginPageWeb = IgnisLoginPageWeb.getInstance(session);
        IgnisSelectPartnerPageWeb<?> ignisSelectPartnerPageWeb = IgnisSelectPartnerPageWeb.getInstance(session);
        IgnisPartnerPageWeb<?> ignisPartnerPageWeb = IgnisPartnerPageWeb.getInstance(session);
        IgnisThingsToDoListPageWeb<?> ignisThingsToDoListPageWeb = IgnisThingsToDoListPageWeb.getInstance(session);

        step("Login as a user");
        ignisLoginPageWeb.loginAsSpecificUser(username, password);

        step("Select Partner");
        ignisSelectPartnerPageWeb.selectPartner(partnerName);

        step("Extract Data to Compare");
        int totalLiveProductIgnisHome = ignisPartnerPageWeb.getLiveProductCount();

        step("Click 'See All Thing To Do'");
        ignisPartnerPageWeb.clickSeeAllThingToDo();

        step("Extract Data to Compare");
        int totalLiveProductListPage = ignisThingsToDoListPageWeb.getLiveProductCount();

        step("Assertions");
        Assertion.assertThat("The total count of live product is the same between ignis partner homepage and on things to do list page", totalLiveProductIgnisHome, is(totalLiveProductListPage));
    }

    @Author(names = {"Akbar"})
    @Module(name = "RedeemVoucher")
    @Tribe(name = "Ignis")
    @Vertical(name = "TTD")
    @Test
    public void testRedeemTicketByOrderId() throws Exception {
        Map<String, String> param = getParam("testRedeemTicketByOrderId");
        BaseTest.paramsThreadLocal.set(param);

        String b2cUsername = param.get("b2cUsername");
        String b2cPassword = param.get("b2cPassword");
        String otp = param.get("otp");

        String productName = param.get("productName");
        String packageName = param.get("packageName");

        String ignisUsername = param.get("ignisUsername");
        String ignisPassword = param.get("ignisPassword");

        String partnerName = param.get("partnerName");

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        TtdLandingPageWeb<?> ttdLandingPageWeb = TtdLandingPageWeb.getInstance(session);
        TtdSearchResultPageWeb<?> ttdSearchResultPageWeb = TtdSearchResultPageWeb.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);
        TtdBookingFormPageWeb<?> ttdBookingFormPageWeb = TtdBookingFormPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);

        IgnisLoginPageWeb<?> ignisLoginPageWeb = IgnisLoginPageWeb.getInstance(session);
        IgnisSelectPartnerPageWeb<?> ignisSelectPartnerPageWeb = IgnisSelectPartnerPageWeb.getInstance(session);
        IgnisPartnerPageWeb<?> ignisPartnerPageWeb = IgnisPartnerPageWeb.getInstance(session);
        IgnisRedeemVoucherPageWeb<?> ignisRedeemVoucherPage = IgnisRedeemVoucherPageWeb.getInstance(session);

        step("Open home page");
        homePage.goToHomePage();

        step("Open as logged in user");
        loginPage.loginIfNotWithSameEmail(b2cUsername, b2cPassword, otp, 1);

        step("Select IDR as currency");
        homePage.selectCurrency("IDR");

        step("Go to TTD Landing Page");
        String lang = homePage.getLanguage();
        ttdLandingPageWeb.goToTtdLandingPage();

        step("Search " + productName);
        ttdLandingPageWeb.inputSearchKeyword(productName);
        ttdLandingPageWeb.search();
        String nextWeekend = CoreFunctions.getNearestNextWeekend();

        step("Filter date to next weekend " + nextWeekend);
        int date = Integer.parseInt(nextWeekend.split(" ")[0]);
        ttdSearchResultPageWeb.filterDate(date);

        step("Verify product in search result");
        List<String> productNames = ttdSearchResultPageWeb.getProductTitles();
        Assertion.assertThat("Product " + productName + " is in search result", productNames, hasItem(productName));

        step("Open product");
        ttdSearchResultPageWeb.chooseProductSrp(false, productName, 1);
        ttdSearchResultPageWeb.switchToNewTab();

        step("Open package " + packageName);
        ttdProductDetailPageWeb.chooseTicket(packageName);

        step("Continue to booking form");
        ttdProductDetailPageWeb.setTicketQty(1, 0);
        ttdProductDetailPageWeb.clickBookButton();

        step("Completing booking form data");
        ttdBookingFormPageWeb.fillContactOrderDetail(true);
        ttdBookingFormPageWeb.fillGuestDetail();

        step("Continue to payment page");
        ttdBookingFormPageWeb.continueToPaymentPage();

        Assertion.assertThat("Success redirected to payment page", paymentPage.isPaymentPageLoaded(), is(true));
        String orderId = paymentPage.getOrderId();

        step("Open all payment method");
        paymentPage.clickOnSelectPaymentMethod();

        step("Select the payment Method");
        paymentPage.selectPaymentMethod("Sakuku");

        step("Submit the payment method");
        paymentPage.clickOnPaymentButton();
        Assertion.assertThat("Payment successful page is visible", paymentPage.isPaymentSuccessfulVisible(), is(true));

        step("Go to Ignis dashboard and login");
        ignisLoginPageWeb.loginAsSpecificUser(ignisUsername, ignisPassword);

        step("Select Partner");
        ignisSelectPartnerPageWeb.selectPartner(partnerName);

        step("Enter order id to redeem input form");
        ignisPartnerPageWeb.inputRedeemCode(orderId);
        ignisPartnerPageWeb.submitRedeemCode();

        step("Redeem Voucher for that specific id");
        String orderIdIgnis = ignisRedeemVoucherPage.getOrderId();
        Assertion.assertThat("Order ID is matched from b2c side to ignis", orderIdIgnis, is(orderId));
        ignisRedeemVoucherPage.clickRedeemVoucherButton();

        step("Assertions");
        Assertion.assertThat("Success snackbar is visible", ignisRedeemVoucherPage.isSuccessSnackBarVisible(), is(true));
        Assertion.assertThat("Success notification banner page is visible", ignisRedeemVoucherPage.isSuccessNotifBannerVisible(), is(true));
        Assertion.assertThat("Success badge is visible", ignisRedeemVoucherPage.isSuccessBadgeVisible(), is(true));
    }

    @Author(names = {"Akbar"})
    @Module(name = "RedeemVoucher")
    @Tribe(name = "Ignis")
    @Vertical(name = "TTD")
    @Test
    public void testRedeemTicketByTicketId() throws Exception {
        Map<String, String> param = getParam("testRedeemTicketByTicketId");
        BaseTest.paramsThreadLocal.set(param);

        String b2cUsername = param.get("b2cUsername");
        String b2cPassword = param.get("b2cPassword");
        String otp = param.get("otp");

        String productName = param.get("productName");
        String packageName = param.get("packageName");

        String ignisUsername = param.get("ignisUsername");
        String ignisPassword = param.get("ignisPassword");

        String partnerName = param.get("partnerName");

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        TtdLandingPageWeb<?> ttdLandingPageWeb = TtdLandingPageWeb.getInstance(session);
        TtdSearchResultPageWeb<?> ttdSearchResultPageWeb = TtdSearchResultPageWeb.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);
        TtdBookingFormPageWeb<?> ttdBookingFormPageWeb = TtdBookingFormPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);

        IgnisLoginPageWeb<?> ignisLoginPageWeb = IgnisLoginPageWeb.getInstance(session);
        IgnisSelectPartnerPageWeb<?> ignisSelectPartnerPageWeb = IgnisSelectPartnerPageWeb.getInstance(session);
        IgnisPartnerPageWeb<?> ignisPartnerPageWeb = IgnisPartnerPageWeb.getInstance(session);
        IgnisRedeemVoucherPageWeb<?> ignisRedeemVoucherPage = IgnisRedeemVoucherPageWeb.getInstance(session);

        step("Open home page");
        homePage.goToHomePage();

        step("Open as logged in user");
        loginPage.loginIfNotWithSameEmail(b2cUsername, b2cPassword, otp, 1);

        step("Select IDR as currency");
        homePage.selectCurrency("IDR");

        step("Go to TTD Landing Page");
        String lang = homePage.getLanguage();
        ttdLandingPageWeb.goToTtdLandingPage();

        step("Search " + productName);
        ttdLandingPageWeb.inputSearchKeyword(productName);
        ttdLandingPageWeb.search();
        String nextWeekend = CoreFunctions.getNearestNextWeekend();

        step("Filter date to next weekend " + nextWeekend);
        int date = Integer.parseInt(nextWeekend.split(" ")[0]);
        ttdSearchResultPageWeb.filterDate(date);

        step("Verify product in search result");
        List<String> productNames = ttdSearchResultPageWeb.getProductTitles();
        Assertion.assertThat("Product " + productName + " is in search result", productNames, hasItem(productName));

        step("Open product");
        ttdSearchResultPageWeb.chooseProductSrp(false, productName, 1);
        ttdSearchResultPageWeb.switchToNewTab();

        step("Open package " + packageName);
        ttdProductDetailPageWeb.chooseTicket(packageName);

        step("Continue to booking form");
        ttdProductDetailPageWeb.setTicketQty(1, 0);
        ttdProductDetailPageWeb.clickBookButton();

        step("Completing booking form data");
        ttdBookingFormPageWeb.fillContactOrderDetail(true);
        ttdBookingFormPageWeb.fillGuestDetail();

        step("Continue to payment page");
        ttdBookingFormPageWeb.continueToPaymentPage();

        Assertion.assertThat("Success redirected to payment page", paymentPage.isPaymentPageLoaded(), is(true));
        String orderId = paymentPage.getOrderId();

        step("Open all payment method");
        paymentPage.clickOnSelectPaymentMethod();

        step("Select the payment Method");
        paymentPage.selectPaymentMethod("Sakuku");

        step("Submit the payment method");
        paymentPage.clickOnPaymentButton();
        Assertion.assertThat("Payment successful page is visible", paymentPage.isPaymentSuccessfulVisible(), is(true));

        step("Go to order detail page");
        orderListPage.openActiveTicketViaURL(orderId);
        orderDetailPage.skipCoachmark();

        step("Open redemption status");
        orderDetailPage.openRedemptionStatus();
        String ticketId = orderDetailPage.getTicketId(1);

        step("Go to Ignis dashboard and login");
        ignisLoginPageWeb.loginAsSpecificUser(ignisUsername, ignisPassword);

        step("Select Partner");
        ignisSelectPartnerPageWeb.selectPartner(partnerName);

        step("Enter order id to redeem input form");
        ignisPartnerPageWeb.inputRedeemCode(ticketId);

        step("Redeem Voucher for that specific id");
        String orderIdIgnis = ignisRedeemVoucherPage.getOrderId();
        String ticketIdIgnis = ignisRedeemVoucherPage.getTicketId();
        Assertion.assertThat("Order ID is matched from b2c side to ignis", orderIdIgnis, is(orderId));
        Assertion.assertThat("Ticket ID is matched from b2c side to ignis", ticketIdIgnis, is(ticketId));
        ignisRedeemVoucherPage.clickRedeemVoucherButton();

        step("Assertions");
        Assertion.assertThat("Success snackbar is visible", ignisRedeemVoucherPage.isSuccessSnackBarVisible(), is(true));
        Assertion.assertThat("Success notification banner page is visible", ignisRedeemVoucherPage.isSuccessNotifBannerVisible(), is(true));
        Assertion.assertThat("Success badge is visible", ignisRedeemVoucherPage.isSuccessBadgeVisible(), is(true));
    }

    @Author(names = {"Akbar"})
    @Module(name = "RedeemVoucher")
    @Tribe(name = "Ignis")
    @Vertical(name = "TTD")
    @Test
    public void testRedeemRedeemedTicket() throws Exception {
        Map<String, String> param = getParam("testRedeemRedeemedTicket");
        BaseTest.paramsThreadLocal.set(param);

        String b2cUsername = param.get("b2cUsername");
        String b2cPassword = param.get("b2cPassword");
        String otp = param.get("otp");

        String productName = param.get("productName");
        String packageName = param.get("packageName");

        String ignisUsername = param.get("ignisUsername");
        String ignisPassword = param.get("ignisPassword");

        String partnerName = param.get("partnerName");

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        TtdLandingPageWeb<?> ttdLandingPageWeb = TtdLandingPageWeb.getInstance(session);
        TtdSearchResultPageWeb<?> ttdSearchResultPageWeb = TtdSearchResultPageWeb.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);
        TtdBookingFormPageWeb<?> ttdBookingFormPageWeb = TtdBookingFormPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);

        IgnisLoginPageWeb<?> ignisLoginPageWeb = IgnisLoginPageWeb.getInstance(session);
        IgnisSelectPartnerPageWeb<?> ignisSelectPartnerPageWeb = IgnisSelectPartnerPageWeb.getInstance(session);
        IgnisPartnerPageWeb<?> ignisPartnerPageWeb = IgnisPartnerPageWeb.getInstance(session);
        IgnisRedeemVoucherPageWeb<?> ignisRedeemVoucherPage = IgnisRedeemVoucherPageWeb.getInstance(session);

        step("Open home page");
        homePage.goToHomePage();

        step("Open as logged in user");
        loginPage.loginIfNotWithSameEmail(b2cUsername, b2cPassword, otp, 1);

        step("Select IDR as currency");
        homePage.selectCurrency("IDR");

        step("Go to TTD Landing Page");
        String lang = homePage.getLanguage();
        ttdLandingPageWeb.goToTtdLandingPage();

        step("Search " + productName);
        ttdLandingPageWeb.inputSearchKeyword(productName);
        ttdLandingPageWeb.search();
        String nextWeekend = CoreFunctions.getNearestNextWeekend();

        step("Filter date to next weekend " + nextWeekend);
        int date = Integer.parseInt(nextWeekend.split(" ")[0]);
        ttdSearchResultPageWeb.filterDate(date);

        step("Verify product in search result");
        List<String> productNames = ttdSearchResultPageWeb.getProductTitles();
        Assertion.assertThat("Product " + productName + " is in search result", productNames, hasItem(productName));

        step("Open product");
        ttdSearchResultPageWeb.chooseProductSrp(false, productName, 1);
        ttdSearchResultPageWeb.switchToNewTab();

        step("Open package " + packageName);
        ttdProductDetailPageWeb.chooseTicket(packageName);

        step("Continue to booking form");
        ttdProductDetailPageWeb.setTicketQty(1, 0);
        ttdProductDetailPageWeb.clickBookButton();

        step("Completing booking form data");
        ttdBookingFormPageWeb.fillContactOrderDetail(true);
        ttdBookingFormPageWeb.fillGuestDetail();

        step("Continue to payment page");
        ttdBookingFormPageWeb.continueToPaymentPage();

        Assertion.assertThat("Success redirected to payment page", paymentPage.isPaymentPageLoaded(), is(true));
        String orderId = paymentPage.getOrderId();

        step("Open all payment method");
        paymentPage.clickOnSelectPaymentMethod();

        step("Select the payment Method");
        paymentPage.selectPaymentMethod("Sakuku");

        step("Submit the payment method");
        paymentPage.clickOnPaymentButton();
        Assertion.assertThat("Payment successful page is visible", paymentPage.isPaymentSuccessfulVisible(), is(true));

        step("Go to order detail page");
        orderListPage.openActiveTicketViaURL(orderId);
        orderDetailPage.skipCoachmark();

        step("Open redemption status");
        orderDetailPage.openRedemptionStatus();
        String ticketId = orderDetailPage.getTicketId(1);

        step("Go to Ignis dashboard and login");
        ignisLoginPageWeb.loginAsSpecificUser(ignisUsername, ignisPassword);

        step("Select Partner");
        ignisSelectPartnerPageWeb.selectPartner(partnerName);

        step("Enter order id to redeem input form");
        ignisPartnerPageWeb.inputRedeemCode(ticketId);

        step("Redeem Voucher for that specific id");
        String orderIdIgnis = ignisRedeemVoucherPage.getOrderId();
        String ticketIdIgnis = ignisRedeemVoucherPage.getTicketId();
        Assertion.assertThat("Order ID is matched from b2c side to ignis", orderIdIgnis, is(orderId));
        Assertion.assertThat("Ticket ID is matched from b2c side to ignis", ticketIdIgnis, is(ticketId));
        ignisRedeemVoucherPage.clickRedeemVoucherButton();

        Assertion.assertThat("Success redeemed badge is visible", ignisRedeemVoucherPage.isSuccessBadgeVisible(), is(true));

        step("Go back to Ignis partner home page");
        ignisPartnerPageWeb.goToPage();

        step("Input the same ticketId on redeem section");
        ignisPartnerPageWeb.inputRedeemCode(ticketId);

        step("Assertions");
        Assertion.assertThat("Redeemed badge is visible", ignisRedeemVoucherPage.isSuccessBadgeVisible(), is(true));
    }

    @Author(names = {"Akbar"})
    @Module(name = "RedeemVoucher")
    @Tribe(name = "Ignis")
    @Vertical(name = "TTD")
    @Test
    public void testRedeemExpiredTicket() {
        Map<String, String> param = getParam("testRedeemExpiredTicket");
        BaseTest.paramsThreadLocal.set(param);
        String ignisUsername = param.get("ignisUsername");
        String ignisPassword = param.get("ignisPassword");

        String partnerName = param.get("partnerName");
        String orderId = param.get("orderId");

        Session<?> session = BaseTest.sessionThreadLocal.get();
        IgnisLoginPageWeb<?> ignisLoginPageWeb = IgnisLoginPageWeb.getInstance(session);
        IgnisSelectPartnerPageWeb<?> ignisSelectPartnerPageWeb = IgnisSelectPartnerPageWeb.getInstance(session);
        IgnisPartnerPageWeb<?> ignisPartnerPageWeb = IgnisPartnerPageWeb.getInstance(session);
        IgnisRedeemVoucherPageWeb<?> ignisRedeemVoucherPage = IgnisRedeemVoucherPageWeb.getInstance(session);

        step("Go to Ignis dashboard and login");
        ignisLoginPageWeb.loginAsSpecificUser(ignisUsername, ignisPassword);

        step("Select Partner");
        ignisSelectPartnerPageWeb.selectPartner(partnerName);

        step("Enter order id to redeem input form");
        ignisPartnerPageWeb.inputRedeemCode(orderId);
        ignisPartnerPageWeb.submitRedeemCode();

        step("Redeem the voucher");
        String orderIdIgnis = ignisRedeemVoucherPage.getOrderId();
        Assertion.assertThat("Order ID is matched from b2c side to ignis", orderIdIgnis, is(orderId));
        ignisRedeemVoucherPage.clickRedeemVoucherButton();

        step("Assertions");
        Assertion.assertThat("Fail to redeem snackbar is visible", ignisRedeemVoucherPage.isFailSnackBarVisible(), is(true));
        Assertion.assertThat("Fail to redeem notification banner is visible", ignisRedeemVoucherPage.isFailNotifBannerVisible(), is(true));
    }

    @Author(names = {"Akbar"})
    @Module(name = "ThingsToDoList")
    @Tribe(name = "Ignis")
    @Vertical(name = "TTD")
    @Test
    public void testIgnisChangeProductLiveStatus() {
        Map<String, String> param = getParam("testIgnisChangeProductLiveStatus");
        BaseTest.paramsThreadLocal.set(param);
        String username = param.get("username");
        String password = param.get("password");

        String partnerName = param.get("partnerName");
        String productName = param.get("productName");

        Session<?> session = BaseTest.sessionThreadLocal.get();
        IgnisLoginPageWeb<?> ignisLoginPageWeb = IgnisLoginPageWeb.getInstance(session);
        IgnisSelectPartnerPageWeb<?> ignisSelectPartnerPageWeb = IgnisSelectPartnerPageWeb.getInstance(session);
        IgnisPartnerPageWeb<?> ignisPartnerPageWeb = IgnisPartnerPageWeb.getInstance(session);
        IgnisThingsToDoListPageWeb<?> ignisThingsToDoListPageWeb = IgnisThingsToDoListPageWeb.getInstance(session);

        step("Go to Ignis dashboard and login");
        ignisLoginPageWeb.loginAsSpecificUser(username, password);

        step("Select Partner");
        ignisSelectPartnerPageWeb.selectPartner(partnerName);

        step("Go to things to do list page");
        ignisPartnerPageWeb.clickSeeAllThingToDo();
        ignisThingsToDoListPageWeb.searchProduct(productName);
        String firstProductName = ignisThingsToDoListPageWeb.getProductName(1);

        step("Change the first Live product to `Hidden` status and check on the same category");
        ignisThingsToDoListPageWeb.setProductStatus(firstProductName, "hidden/hidden");
        ignisThingsToDoListPageWeb.goToTabChip("hidden/hidden");
        ignisThingsToDoListPageWeb.searchProduct(firstProductName);
        boolean isVisibleOnHiddenTab = ignisThingsToDoListPageWeb.isProductExist(firstProductName);

        step("Change it again to `Inactive` status and check on the same category");
        ignisThingsToDoListPageWeb.setProductStatus(firstProductName, "inactive/tidak aktif");
        ignisThingsToDoListPageWeb.goToTabChip("inactive/tidak aktif");
        ignisThingsToDoListPageWeb.searchProduct(firstProductName);
        boolean isVisibleOnInactiveTab = ignisThingsToDoListPageWeb.isProductExist(firstProductName);

        step("Change it again to `Live` status and check on the same category");
        ignisThingsToDoListPageWeb.setProductStatus(firstProductName, "live/live");
        ignisThingsToDoListPageWeb.goToTabChip("live/live");
        ignisThingsToDoListPageWeb.searchProduct(firstProductName);
        boolean isVisibleOnLiveTab = ignisThingsToDoListPageWeb.isProductExist(firstProductName);

        step("Assertions");
        Assertion.assertThat("Product is visible on the `Hidden` tab", isVisibleOnHiddenTab, is(true));
        Assertion.assertThat("Product is visible on the `Inactive` tab", isVisibleOnInactiveTab, is(true));
        Assertion.assertThat("Product is visible on the `Live` tab", isVisibleOnLiveTab, is(true));
    }

    @Author(names = {"Akbar"})
    @Module(name = "ThingsToDoList")
    @Tribe(name = "Ignis")
    @Vertical(name = "TTD")
    @Test
    public void testIgnisChangePackageStatus() {
        Map<String, String> param = getParam("testIgnisChangePackageStatus");
        BaseTest.paramsThreadLocal.set(param);
        String username = param.get("username");
        String password = param.get("password");

        String partnerName = param.get("partnerName");
        String productName = param.get("productName");
        String packageName = param.get("packageName");

        Session<?> session = BaseTest.sessionThreadLocal.get();
        IgnisLoginPageWeb<?> ignisLoginPageWeb = IgnisLoginPageWeb.getInstance(session);
        IgnisSelectPartnerPageWeb<?> ignisSelectPartnerPageWeb = IgnisSelectPartnerPageWeb.getInstance(session);
        IgnisPartnerPageWeb<?> ignisPartnerPageWeb = IgnisPartnerPageWeb.getInstance(session);
        IgnisThingsToDoListPageWeb<?> ignisThingsToDoListPageWeb = IgnisThingsToDoListPageWeb.getInstance(session);

        step("Go to Ignis dashboard and login");
        ignisLoginPageWeb.loginAsSpecificUser(username, password);

        step("Select Partner");
        ignisSelectPartnerPageWeb.selectPartner(partnerName);

        step("Go to things to do list page");
        ignisPartnerPageWeb.clickSeeAllThingToDo();
        ignisThingsToDoListPageWeb.searchProduct(productName);

        step("Open the package section");
        ignisThingsToDoListPageWeb.openProductPackageSection(productName);

        step("Change the package to 'soldout'");
        ignisThingsToDoListPageWeb.setPackageSoldout(packageName, true);
        boolean isPackageSoldout = ignisThingsToDoListPageWeb.isPackageSoldoutBadgeVisible(packageName);
        ignisThingsToDoListPageWeb.setPackageSoldout(packageName, false);

        step("Change the package to 'inactive'");
        ignisThingsToDoListPageWeb.setPackageInactive(packageName, true);
        boolean isPackageInactive = ignisThingsToDoListPageWeb.isPackageInactiveBadgeVisible(packageName);
        ignisThingsToDoListPageWeb.setPackageInactive(packageName, false);

        step("Assertions");
        Assertion.assertThat("Package soldout badge is visible", isPackageSoldout, is(true));
        Assertion.assertThat("Package inactive badge is visible", isPackageInactive, is(true));
    }

    @Author(names = {"Akbar"})
    @Module(name = "ThingsToDoList")
    @Tribe(name = "Ignis")
    @Vertical(name = "TTD")
    @Test
    public void testIgnisSeeProgressLog() {
        Map<String, String> param = getParam("testIgnisSeeProgressLog");
        BaseTest.paramsThreadLocal.set(param);
        String username = param.get("username");
        String password = param.get("password");

        String partnerName = param.get("partnerName");

        Session<?> session = BaseTest.sessionThreadLocal.get();
        IgnisLoginPageWeb<?> ignisLoginPageWeb = IgnisLoginPageWeb.getInstance(session);
        IgnisSelectPartnerPageWeb<?> ignisSelectPartnerPageWeb = IgnisSelectPartnerPageWeb.getInstance(session);
        IgnisPartnerPageWeb<?> ignisPartnerPageWeb = IgnisPartnerPageWeb.getInstance(session);
        IgnisThingsToDoListPageWeb<?> ignisThingsToDoListPageWeb = IgnisThingsToDoListPageWeb.getInstance(session);
        IgnisProgressLogPageWeb<?> ignisProgressLogPageWeb = IgnisProgressLogPageWeb.getInstance(session);

        step("Go to Ignis dashboard and login");
        ignisLoginPageWeb.loginAsSpecificUser(username, password);

        step("Select Partner");
        ignisSelectPartnerPageWeb.selectPartner(partnerName);

        step("Go to things to do list page");
        ignisPartnerPageWeb.clickSeeAllThingToDo();

        step("Click 'advance setting' button");
        ignisThingsToDoListPageWeb.clickAdvanceConfigButton();

        step("Click 'see progress log' option");
        ignisThingsToDoListPageWeb.clickAdvanceConfigOption("lihat progress log/see progress log");

        step("Assertions");
        Assertion.assertThat("Redirected to progress log page", ignisProgressLogPageWeb.isInPage(), is(true));
    }

    @Author(names = {"Akbar"})
    @Module(name = "ThingsToDoList")
    @Tribe(name = "Ignis")
    @Vertical(name = "TTD")
    @Test
    public void testIgnisManageTicketDetails() {
        Map<String, String> param = getParam("testIgnisManageTicketDetails");
        BaseTest.paramsThreadLocal.set(param);
        String username = param.get("username");
        String password = param.get("password");

        String partnerName = param.get("partnerName");
        String productName = param.get("productName");
        String packageName = param.get("packageName");

        Session<?> session = BaseTest.sessionThreadLocal.get();
        IgnisLoginPageWeb<?> ignisLoginPageWeb = IgnisLoginPageWeb.getInstance(session);
        IgnisSelectPartnerPageWeb<?> ignisSelectPartnerPageWeb = IgnisSelectPartnerPageWeb.getInstance(session);
        IgnisPartnerPageWeb<?> ignisPartnerPageWeb = IgnisPartnerPageWeb.getInstance(session);
        IgnisThingsToDoListPageWeb<?> ignisThingsToDoListPageWeb = IgnisThingsToDoListPageWeb.getInstance(session);
        IgnisManageTicketDetailsPageWeb<?> ignisManageTicketDetailsPageWeb = IgnisManageTicketDetailsPageWeb.getInstance(session);

        step("Go to Ignis dashboard and login");
        ignisLoginPageWeb.loginAsSpecificUser(username, password);

        step("Select Partner");
        ignisSelectPartnerPageWeb.selectPartner(partnerName);

        step("Go to things to do list page");
        ignisPartnerPageWeb.clickSeeAllThingToDo();

        step("Click 'manage ticket details' button");
        ignisThingsToDoListPageWeb.clickManageTicketDetails();

        step("Search product and choose a package");
        ignisManageTicketDetailsPageWeb.inputSearchBar(productName);
        ignisManageTicketDetailsPageWeb.clickProduct(productName);
        ignisManageTicketDetailsPageWeb.clickPackage(packageName);

        step("Click next until save");
        ignisManageTicketDetailsPageWeb.clickNextAndSave();
        boolean isSnackBarVisible = ignisManageTicketDetailsPageWeb.isSnackBarVisible();
        boolean isActiveTabLast = ignisManageTicketDetailsPageWeb.isActiveTabLast();

        step("Assertions");
        Assertion.assertThat("Snackbar is visible after the user clicked save button", isSnackBarVisible, is(true));
        Assertion.assertThat("The active tab is on the last tab after the user clicked save button", isActiveTabLast, is(true));
    }

    @Author(names = {"Akbar"})
    @Module(name = "OrderReportDetails")
    @Tribe(name = "Ignis")
    @Vertical(name = "TTD")
    @Test
    public void testIgnisCheckQN() throws IOException {
        Map<String, String> param = getParam("testIgnisCheckQN");
        BaseTest.paramsThreadLocal.set(param);
        String username = param.get("username");
        String password = param.get("password");

        String partnerName = param.get("partnerName");
        String orderId = param.get("orderId");

        Session<?> session = BaseTest.sessionThreadLocal.get();
        IgnisLoginPageWeb<?> ignisLoginPageWeb = IgnisLoginPageWeb.getInstance(session);
        IgnisSelectPartnerPageWeb<?> ignisSelectPartnerPageWeb = IgnisSelectPartnerPageWeb.getInstance(session);
        IgnisPartnerPageWeb<?> ignisPartnerPageWeb = IgnisPartnerPageWeb.getInstance(session);
        IgnisOrderReportPageWeb<?> ignisOrderReportPageWeb = IgnisOrderReportPageWeb.getInstance(session);
        IgnisRedeemVoucherPageWeb<?> ignisRedeemVoucherPageWeb = IgnisRedeemVoucherPageWeb.getInstance(session);
        IgnisProgressLogPageWeb<?> ignisProgressLogPageWeb = IgnisProgressLogPageWeb.getInstance(session);

        step("Go to Ignis dashboard and login");
        ignisLoginPageWeb.loginAsSpecificUser(username, password);

        step("Select Partner");
        ignisSelectPartnerPageWeb.selectPartner(partnerName);

        step("Go to order report details");
        ignisOrderReportPageWeb.goToPage();
        ignisOrderReportPageWeb.setSearchByOrder();

        step("Search order id from testdata and extract data");
        ignisOrderReportPageWeb.inputOrderId(orderId);
        ignisOrderReportPageWeb.clickRowByOrderId(orderId);
        String queueNumber1 = ignisOrderReportPageWeb.getQueueNumber();
        ignisOrderReportPageWeb.closeDetailPopup();

        step("Download exported report");
        ignisOrderReportPageWeb.clickOrderConfigButton();
        ignisOrderReportPageWeb.clickOrderConfigOptions("export/ekspor");
        ignisOrderReportPageWeb.chooseExportBy("ticketid");
        ignisOrderReportPageWeb.clickPopupPrimaryButton();
        ignisProgressLogPageWeb.clickDownloadButtonRow(1);

        step("Extract data from downloaded report");
        String queueNumber2 = ignisOrderReportPageWeb.getQueueNumberDownloadedFileByOrderId(orderId);

        step("Go to partner home page");
        ignisPartnerPageWeb.goToPage();

        step("Input order id on redeem input form");
        ignisPartnerPageWeb.inputRedeemCode(orderId);
        ignisPartnerPageWeb.submitRedeemCode();

        step("Extract data");
        String queueNumber3 = ignisRedeemVoucherPageWeb.getQueueNumber();

        step("Assertions");
        Assertion.assertThat("Queue Number is matched from order detail page and exported file", queueNumber1, is(queueNumber2));
        Assertion.assertThat("Queue Number is matched from exported file and redeem voucher page", queueNumber2, is(queueNumber3));
    }

    @Author(names = {"Akbar"})
    @Module(name = "OrderReportDetails")
    @Tribe(name = "Ignis")
    @Vertical(name = "TTD")
    @Test
    public void testIgnisRefundInformation() {
        Map<String, String> param = getParam("testIgnisRefundInformation");
        BaseTest.paramsThreadLocal.set(param);
        String username = param.get("username");
        String password = param.get("password");

        String partnerName = param.get("partnerName");
        String orderId = param.get("orderId");

        Session<?> session = BaseTest.sessionThreadLocal.get();
        IgnisLoginPageWeb<?> ignisLoginPageWeb = IgnisLoginPageWeb.getInstance(session);
        IgnisSelectPartnerPageWeb<?> ignisSelectPartnerPageWeb = IgnisSelectPartnerPageWeb.getInstance(session);
        IgnisOrderReportPageWeb<?> ignisOrderReportPageWeb = IgnisOrderReportPageWeb.getInstance(session);

        step("Go to Ignis dashboard and login");
        ignisLoginPageWeb.loginAsSpecificUser(username, password);

        step("Select Partner");
        ignisSelectPartnerPageWeb.selectPartner(partnerName);

        step("Go to order report details");
        ignisOrderReportPageWeb.goToPage();
        ignisOrderReportPageWeb.setSearchByOrder();

        step("Search order id from testdata and extract data");
        ignisOrderReportPageWeb.inputOrderId(orderId);
        ignisOrderReportPageWeb.clickRowByOrderId(orderId);

        step("Extract data");
        String isRefundOptionVisible = ignisOrderReportPageWeb.isRefundOptionVisible();
        String isRefundStatusVisible = ignisOrderReportPageWeb.isRefundStatusVisible();

        step("Assertions");
        Assertion.assertThat("Refund Option is displayed on order detail popup", isRefundOptionVisible, is(notNullValue()));
        Assertion.assertThat("Refund Status is displayed on order detail popup", isRefundStatusVisible, is(notNullValue()));
    }

    @Author(names = {"Akbar"})
    @Module(name = "BulkUpdate")
    @Tribe(name = "Ignis")
    @Vertical(name = "TTD")
    @Test
    public void testIgnisBulkUpdatePrice() {
        Map<String, String> param = getParam("testIgnisBulkUpdatePrice");
        BaseTest.paramsThreadLocal.set(param);
        String username = param.get("username");
        String password = param.get("password");

        String partnerName = param.get("partnerName");
        String productName = param.get("productName");

        String editOfflinePrice = param.get("editOfflinePrice");
        String editOfferedPrice = param.get("editOfferedPrice");

        Session<?> session = BaseTest.sessionThreadLocal.get();
        IgnisLoginPageWeb<?> ignisLoginPageWeb = IgnisLoginPageWeb.getInstance(session);
        IgnisSelectPartnerPageWeb<?> ignisSelectPartnerPageWeb = IgnisSelectPartnerPageWeb.getInstance(session);
        IgnisPartnerPageWeb<?> ignisPartnerPageWeb = IgnisPartnerPageWeb.getInstance(session);
        IgnisThingsToDoListPageWeb<?> ignisThingsToDoListPageWeb = IgnisThingsToDoListPageWeb.getInstance(session);
        IgnisProgressLogPageWeb<?> ignisProgressLogPageWeb = IgnisProgressLogPageWeb.getInstance(session);
        IgnisUpdateThingsToDoListPageWeb<?> ignisUpdateThingsToDoListPageWeb = IgnisUpdateThingsToDoListPageWeb.getInstance(session);

        step("Go to Ignis dashboard and login");
        ignisLoginPageWeb.loginAsSpecificUser(username, password);

        step("Select Partner");
        ignisSelectPartnerPageWeb.selectPartner(partnerName);

        step("Go to things to do list page");
        ignisPartnerPageWeb.clickSeeAllThingToDo();

        step("Click 'advance setting' button");
        ignisThingsToDoListPageWeb.clickAdvanceConfigButton();

        step("Click 'bulk update settings' option");
        ignisThingsToDoListPageWeb.clickAdvanceConfigOption("update sekaligus/bulk update settings");

        step("Click 'update price' option");
        ignisThingsToDoListPageWeb.chooseBulkUpdateType("update harga/pricing update");
        ignisThingsToDoListPageWeb.clickBulkUpdatePopupPrimaryButton();

        step("Select product and package");
        ignisUpdateThingsToDoListPageWeb.inputProductSearchBar(productName);
        ignisUpdateThingsToDoListPageWeb.selectProduct(productName);
        ignisUpdateThingsToDoListPageWeb.selectAllPackage();
        ignisUpdateThingsToDoListPageWeb.clickPrimaryButton();

        step("Expand package and edit the details");
        ignisUpdateThingsToDoListPageWeb.expandAllProductPackages();
        ignisUpdateThingsToDoListPageWeb.updateAllPackages(editOfflinePrice, editOfferedPrice);

        step("Submit the edit");
        ignisUpdateThingsToDoListPageWeb.clickPrimaryButton();
        ignisUpdateThingsToDoListPageWeb.clickPrimaryButton();
        ignisUpdateThingsToDoListPageWeb.clickPopupPrimaryButton();
        ignisUpdateThingsToDoListPageWeb.clickPopupPrimaryButton();

        step("Extract data");
        boolean isPriceUpdateLogValid = ignisProgressLogPageWeb.verifyFirstRow("harga/pricing", productName);

        step("Assertions");
        Assertion.assertThat("Verify the update log is valid", isPriceUpdateLogValid, is(true));
    }

    @Author(names = {"Akbar"})
    @Module(name = "BulkUpdate")
    @Tribe(name = "Ignis")
    @Vertical(name = "TTD")
    @Test
    public void testIgnisBulkUpdateSellingAndPublishDate() {
        Map<String, String> param = getParam("testIgnisBulkUpdateSellingAndPublishDate");
        BaseTest.paramsThreadLocal.set(param);
        String username = param.get("username");
        String password = param.get("password");

        String partnerName = param.get("partnerName");
        String productName = param.get("productName");

        boolean editSellingDateAnytime = Boolean.parseBoolean(param.get("editSellingDateAnytime"));
        String targetRangeSellingDateAndTime = param.get("targetRangeSellingDateAndTime");
        boolean editPublishDateAnytime = Boolean.parseBoolean(param.get("editPublishDateAnytime"));
        String targetRangePublishDateAndTime = param.get("targetRangePublishDateAndTime");

        Session<?> session = BaseTest.sessionThreadLocal.get();
        IgnisLoginPageWeb<?> ignisLoginPageWeb = IgnisLoginPageWeb.getInstance(session);
        IgnisSelectPartnerPageWeb<?> ignisSelectPartnerPageWeb = IgnisSelectPartnerPageWeb.getInstance(session);
        IgnisPartnerPageWeb<?> ignisPartnerPageWeb = IgnisPartnerPageWeb.getInstance(session);
        IgnisThingsToDoListPageWeb<?> ignisThingsToDoListPageWeb = IgnisThingsToDoListPageWeb.getInstance(session);
        IgnisProgressLogPageWeb<?> ignisProgressLogPageWeb = IgnisProgressLogPageWeb.getInstance(session);
        IgnisUpdateThingsToDoListPageWeb<?> ignisUpdateThingsToDoListPageWeb = IgnisUpdateThingsToDoListPageWeb.getInstance(session);

        step("Go to Ignis dashboard and login");
        ignisLoginPageWeb.loginAsSpecificUser(username, password);

        step("Select Partner");
        ignisSelectPartnerPageWeb.selectPartner(partnerName);

        step("Go to things to do list page");
        ignisPartnerPageWeb.clickSeeAllThingToDo();

        step("Click 'advance setting' button");
        ignisThingsToDoListPageWeb.clickAdvanceConfigButton();

        step("Click 'bulk update settings' option");
        ignisThingsToDoListPageWeb.clickAdvanceConfigOption("update sekaligus/bulk update settings");

        step("Click 'update price' option");
        ignisThingsToDoListPageWeb.chooseBulkUpdateType("update tanggal penjualan dan ditampilkan/selling and publish date update");
        ignisThingsToDoListPageWeb.clickBulkUpdatePopupPrimaryButton();

        step("Select product and package");
        ignisUpdateThingsToDoListPageWeb.inputProductSearchBar(productName);
        ignisUpdateThingsToDoListPageWeb.selectProduct(productName);
        ignisUpdateThingsToDoListPageWeb.selectAllPackage();
        ignisUpdateThingsToDoListPageWeb.clickPrimaryButton();

        step("Edit the details");
        ignisUpdateThingsToDoListPageWeb.chooseSellingDate(editSellingDateAnytime, targetRangeSellingDateAndTime);
        ignisUpdateThingsToDoListPageWeb.choosePublishDate(editPublishDateAnytime, targetRangePublishDateAndTime);

        step("Submit the edit");
        ignisUpdateThingsToDoListPageWeb.clickPrimaryButton();
        ignisUpdateThingsToDoListPageWeb.clickPrimaryButton();
        ignisUpdateThingsToDoListPageWeb.clickPopupPrimaryButton();
        ignisUpdateThingsToDoListPageWeb.clickPopupPrimaryButton();

        step("Extract data");
        boolean isSellingAndPublishDateUpdateLogValid = ignisProgressLogPageWeb.verifyFirstRow("tanggal penjualan dan ditampilkan/selling and publish date", productName);

        step("Assertions");
        Assertion.assertThat("Verify the update log is valid", isSellingAndPublishDateUpdateLogValid, is(true));
    }

    @Author(names = {"Akbar"})
    @Module(name = "BulkUpdate")
    @Tribe(name = "Ignis")
    @Vertical(name = "TTD")
    @Test
    public void testIgnisBulkUpdateScheduleAndQuota() {
        Map<String, String> param = getParam("testIgnisBulkUpdateScheduleAndQuota");
        BaseTest.paramsThreadLocal.set(param);
        String username = param.get("username");
        String password = param.get("password");

        String partnerName = param.get("partnerName");
        String productName = param.get("productName");

        String targetDay = param.get("targetDay");

        Session<?> session = BaseTest.sessionThreadLocal.get();
        IgnisLoginPageWeb<?> ignisLoginPageWeb = IgnisLoginPageWeb.getInstance(session);
        IgnisSelectPartnerPageWeb<?> ignisSelectPartnerPageWeb = IgnisSelectPartnerPageWeb.getInstance(session);
        IgnisPartnerPageWeb<?> ignisPartnerPageWeb = IgnisPartnerPageWeb.getInstance(session);
        IgnisThingsToDoListPageWeb<?> ignisThingsToDoListPageWeb = IgnisThingsToDoListPageWeb.getInstance(session);
        IgnisProgressLogPageWeb<?> ignisProgressLogPageWeb = IgnisProgressLogPageWeb.getInstance(session);
        IgnisUpdateThingsToDoListPageWeb<?> ignisUpdateThingsToDoListPageWeb = IgnisUpdateThingsToDoListPageWeb.getInstance(session);

        step("Go to Ignis dashboard and login");
        ignisLoginPageWeb.loginAsSpecificUser(username, password);

        step("Select Partner");
        ignisSelectPartnerPageWeb.selectPartner(partnerName);

        step("Go to things to do list page");
        ignisPartnerPageWeb.clickSeeAllThingToDo();

        step("Click 'advance setting' button");
        ignisThingsToDoListPageWeb.clickAdvanceConfigButton();

        step("Click 'bulk update settings' option");
        ignisThingsToDoListPageWeb.clickAdvanceConfigOption("update sekaligus/bulk update settings");

        step("Click 'update price' option");
        ignisThingsToDoListPageWeb.chooseBulkUpdateType("update jadwal dan kuota/schedule and quota update");
        ignisThingsToDoListPageWeb.clickBulkUpdatePopupPrimaryButton();

        step("Select product and package");
        ignisUpdateThingsToDoListPageWeb.inputProductSearchBar(productName);
        ignisUpdateThingsToDoListPageWeb.selectProduct(productName);
        ignisUpdateThingsToDoListPageWeb.selectAllPackage();
        ignisUpdateThingsToDoListPageWeb.clickPrimaryButton();

        step("Edit the details");
        ignisUpdateThingsToDoListPageWeb.updateOpenDay(targetDay);

        step("Submit the edit");
        ignisUpdateThingsToDoListPageWeb.clickPrimaryButton();
        ignisUpdateThingsToDoListPageWeb.clickPopupPrimaryButton();
        ignisUpdateThingsToDoListPageWeb.clickPopupPrimaryButton();

        step("Extract data");
        boolean isSellingAndPublishDateUpdateLogValid = ignisProgressLogPageWeb.verifyFirstRow("jadwal dan kuota/schedule and quota", productName);

        step("Assertions");
        Assertion.assertThat("Verify the update log is valid", isSellingAndPublishDateUpdateLogValid, is(true));
    }

    @Author(names = {"Akbar"})
    @Module(name = "PartnerBooking")
    @Tribe(name = "Ignis")
    @Vertical(name = "TTD")
    @Test
    public void testIgnisCheckQNPartnerBookingAndB2C() throws Exception {
        Map<String, String> param = getParam("testIgnisCheckQNPartnerBookingAndB2C");
        BaseTest.paramsThreadLocal.set(param);

        String b2cUsername = param.get("b2cUsername");
        String b2cPassword = param.get("b2cPassword");
        String otp = param.get("otp");

        String productName = param.get("productName");
        String packageName = param.get("packageName");
        int ticketQty = Integer.parseInt(param.get("ticketQty"));
        String rangeBookDate = param.get("rangeBookDate");

        String ignisUsername = param.get("ignisUsername");
        String ignisPassword = param.get("ignisPassword");

        String partnerName = param.get("partnerName");

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        TtdLandingPageWeb<?> ttdLandingPageWeb = TtdLandingPageWeb.getInstance(session);
        TtdSearchResultPageWeb<?> ttdSearchResultPageWeb = TtdSearchResultPageWeb.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);
        TtdBookingFormPageWeb<?> ttdBookingFormPageWeb = TtdBookingFormPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);

        IgnisLoginPageWeb<?> ignisLoginPageWeb = IgnisLoginPageWeb.getInstance(session);
        IgnisSelectPartnerPageWeb<?> ignisSelectPartnerPageWeb = IgnisSelectPartnerPageWeb.getInstance(session);
        IgnisPartnerPageWeb<?> ignisPartnerPageWeb = IgnisPartnerPageWeb.getInstance(session);
        IgnisRedeemVoucherPageWeb<?> ignisRedeemVoucherPage = IgnisRedeemVoucherPageWeb.getInstance(session);
        IgnisPartnerBookingPageWeb<?> ignisPartnerBookingPageWeb = IgnisPartnerBookingPageWeb.getInstance(session);
        IgnisPartnerBookingFormPageWeb<?> ignisPartnerBookingFormPageWeb = IgnisPartnerBookingFormPageWeb.getInstance(session);

        step("Open home page");
        homePage.goToHomePage();

        step("Open as logged in user");
        loginPage.loginIfNotWithSameEmail(b2cUsername, b2cPassword, otp, 1);

        step("Select IDR as currency");
        homePage.selectCurrency("IDR");

        step("Go to TTD Landing Page");
        String lang = homePage.getLanguage();
        ttdLandingPageWeb.goToTtdLandingPage();

        step("Search " + productName);
        ttdLandingPageWeb.inputSearchKeyword(productName);
        ttdLandingPageWeb.search();
        String nextWeekend = CoreFunctions.getNearestNextWeekend();

        step("Filter date to next weekend " + nextWeekend);
        int date = Integer.parseInt(nextWeekend.split(" ")[0]);
        ttdSearchResultPageWeb.filterDate(date);

        step("Verify product in search result");
        List<String> productNames = ttdSearchResultPageWeb.getProductTitles();
        Assertion.assertThat("Product " + productName + " is in search result", productNames, hasItem(productName));

        step("Open product");
        ttdSearchResultPageWeb.chooseProductSrp(false, productName, 1);
        ttdSearchResultPageWeb.switchToNewTab();

        step("Open package " + packageName);
        ttdProductDetailPageWeb.chooseTicket(packageName);

        step("Continue to booking form");
        ttdProductDetailPageWeb.setTicketQty(ticketQty, 0);
        ttdProductDetailPageWeb.clickBookButton();

        step("Completing booking form data");
        ttdBookingFormPageWeb.fillContactOrderDetail(true);
        ttdBookingFormPageWeb.fillGuestDetail();

        step("Continue to payment page");
        ttdBookingFormPageWeb.continueToPaymentPage();

        Assertion.assertThat("Success redirected to payment page", paymentPage.isPaymentPageLoaded(), is(true));
        String orderId = paymentPage.getOrderId();

        step("Open all payment method");
        paymentPage.clickOnSelectPaymentMethod();

        step("Select the payment Method");
        paymentPage.selectPaymentMethod("Sakuku");

        step("Submit the payment method");
        paymentPage.clickOnPaymentButton();
        Assertion.assertThat("Payment successful page is visible", paymentPage.isPaymentSuccessfulVisible(), is(true));

        step("Go to Ignis dashboard and login");
        ignisLoginPageWeb.loginAsSpecificUser(ignisUsername, ignisPassword);

        step("Select Partner");
        ignisSelectPartnerPageWeb.selectPartner(partnerName);

        step("Enter order id to redeem input form");
        ignisPartnerPageWeb.inputRedeemCode(orderId);
        ignisPartnerPageWeb.submitRedeemCode();

        step("Extract data");
        int QNFromB2C = Integer.parseInt(ignisRedeemVoucherPage.getQueueNumber());

        step("Do partner booking for the same product and package");
        ignisPartnerBookingPageWeb.goToPage();
        ignisPartnerBookingPageWeb.createNewPartnerBooking(productName, packageName, ticketQty, rangeBookDate);

        step("Fill and submit the booking form");
        ignisPartnerBookingFormPageWeb.fillBookingForm(param);
        ignisPartnerBookingFormPageWeb.choosePaymentMethod("internal");
        ignisPartnerBookingFormPageWeb.submitBook();

        step("Check order details or the newly created order using partner booking");
        ignisPartnerBookingPageWeb.chooseProductFilter(productName);
        ignisPartnerBookingPageWeb.applyFilter();
        ignisPartnerBookingPageWeb.openOrderDataByIndex(1);
        int QNFromPartnerBooking = ignisPartnerBookingPageWeb.getQueueNumber();

        step("Assertions");
        Assertion.assertThat("Queue Number is created orderly and not overlap", QNFromPartnerBooking, greaterThanOrEqualTo(QNFromB2C));
    }

    @Author(names = {"Akbar"})
    @Module(name = "OrderReportDetails")
    @Tribe(name = "Ignis")
    @Vertical(name = "TTD")
    @Test
    public void testIgnisExportedOrderReportNotLimitedBy100() throws IOException {
        Map<String, String> param = getParam("testIgnisExportedOrderReportNotLimitedBy100");
        BaseTest.paramsThreadLocal.set(param);
        String username = param.get("username");
        String password = param.get("password");

        String partnerName = param.get("partnerName");
        String productName = param.get("productName");
        String dateRange = param.get("dateRange");

        Session<?> session = BaseTest.sessionThreadLocal.get();
        IgnisLoginPageWeb<?> ignisLoginPageWeb = IgnisLoginPageWeb.getInstance(session);
        IgnisSelectPartnerPageWeb<?> ignisSelectPartnerPageWeb = IgnisSelectPartnerPageWeb.getInstance(session);
        IgnisOrderReportPageWeb<?> ignisOrderReportPageWeb = IgnisOrderReportPageWeb.getInstance(session);
        IgnisProgressLogPageWeb<?> ignisProgressLogPageWeb = IgnisProgressLogPageWeb.getInstance(session);

        step("Go to Ignis dashboard and login");
        ignisLoginPageWeb.loginAsSpecificUser(username, password);

        step("Select Partner");
        ignisSelectPartnerPageWeb.selectPartner(partnerName);

        step("Go to order report details");
        ignisOrderReportPageWeb.goToPage();

        step("Set filter to cover maximize result count");
        ignisOrderReportPageWeb.setProductFilter(productName);
        ignisOrderReportPageWeb.setDateRangeFilter(dateRange);
        ignisOrderReportPageWeb.clickApplyFilter();

        step("Download exported report");
        ignisOrderReportPageWeb.clickOrderConfigButton();
        ignisOrderReportPageWeb.clickOrderConfigOptions("export/ekspor");
        ignisOrderReportPageWeb.chooseExportBy("ticketid");
        ignisOrderReportPageWeb.clickPopupPrimaryButton();
        ignisProgressLogPageWeb.clickDownloadButtonRow(1);

        step("Extract data from downloaded report");
        int rowSize = ignisOrderReportPageWeb.getRowSizeOfExportedFile();

        step("Assertions");
        Assertion.assertThat("File is containing more than 100 rows exclude header", rowSize, greaterThanOrEqualTo(100));
    }

    @Author(names = {"Akbar"})
    @Module(name = "OrderReportDetails")
    @Tribe(name = "Ignis")
    @Vertical(name = "TTD")
    @Test
    public void testIgnisSubsidyFilter() {
        Map<String, String> param = getParam("testIgnisSubsidyFilter");
        BaseTest.paramsThreadLocal.set(param);
        String username = param.get("username");
        String password = param.get("password");

        String partnerName = param.get("partnerName");
        String productName = param.get("productName");

        String subsidyFilter = param.get("subsidyFilter");
        String dateRange = param.get("dateRange");

        Session<?> session = BaseTest.sessionThreadLocal.get();
        IgnisLoginPageWeb<?> ignisLoginPageWeb = IgnisLoginPageWeb.getInstance(session);
        IgnisSelectPartnerPageWeb<?> ignisSelectPartnerPageWeb = IgnisSelectPartnerPageWeb.getInstance(session);
        IgnisOrderReportPageWeb<?> ignisOrderReportPageWeb = IgnisOrderReportPageWeb.getInstance(session);

        step("Go to Ignis dashboard and login");
        ignisLoginPageWeb.loginAsSpecificUser(username, password);

        step("Select Partner");
        ignisSelectPartnerPageWeb.selectPartner(partnerName);

        step("Go to order report details");
        ignisOrderReportPageWeb.goToPage();

        step("Set filter to maximize the data reach");
        ignisOrderReportPageWeb.setProductFilter(productName);
        ignisOrderReportPageWeb.setSubsidyFilter(subsidyFilter);
        ignisOrderReportPageWeb.setDateRangeFilter(dateRange);
        ignisOrderReportPageWeb.clickApplyFilter();

        step("Open the first row order detail");
        ignisOrderReportPageWeb.clickRowByIndex(1);

        step("Check subsidy attributes");
        boolean isSubsisdyAttributeVisible = ignisOrderReportPageWeb.isSubsidyAttributeVisible();

        step("Assertions");
        Assertion.assertThat("Subsidy attribute is showing on order detail section", isSubsisdyAttributeVisible, is(true));
    }

    @Author(names = {"Akbar"})
    @Module(name = "OrderReportDetails")
    @Tribe(name = "Ignis")
    @Vertical(name = "TTD")
    @Test
    public void testIgnisCheckOrderReportWithOrWithoutQuestionnaire() {
        Map<String, String> param = getParam("testIgnisCheckOrderReportWithOrWithoutQuestionnaire");
        BaseTest.paramsThreadLocal.set(param);
        String username = param.get("username");
        String password = param.get("password");

        String partnerName = param.get("partnerName");

        String orderIdWithQuestionnaire = param.get("orderIdWithQuestionnaire");
        String orderIdWithoutQuestionnaire = param.get("orderIdWithoutQuestionnaire");

        Session<?> session = BaseTest.sessionThreadLocal.get();
        IgnisLoginPageWeb<?> ignisLoginPageWeb = IgnisLoginPageWeb.getInstance(session);
        IgnisSelectPartnerPageWeb<?> ignisSelectPartnerPageWeb = IgnisSelectPartnerPageWeb.getInstance(session);
        IgnisOrderReportPageWeb<?> ignisOrderReportPageWeb = IgnisOrderReportPageWeb.getInstance(session);

        step("Go to Ignis dashboard and login");
        ignisLoginPageWeb.loginAsSpecificUser(username, password);

        step("Select Partner");
        ignisSelectPartnerPageWeb.selectPartner(partnerName);

        step("Go to order report details");
        ignisOrderReportPageWeb.goToPage();

        step("Search and extract data for order with questionnaire");
        ignisOrderReportPageWeb.setSearchByOrder();
        ignisOrderReportPageWeb.inputOrderId(orderIdWithQuestionnaire);
        ignisOrderReportPageWeb.clickRowByIndex(1);
        boolean isQuestionnaireVisible1 = ignisOrderReportPageWeb.isQuestionnaireSectionVisible();
        ignisOrderReportPageWeb.closeDetailPopup();

        step("Search and extract data for order without questionnaire");
        ignisOrderReportPageWeb.inputOrderId(orderIdWithoutQuestionnaire);
        ignisOrderReportPageWeb.clickRowByIndex(1);
        boolean isQuestionnaireVisible2 = ignisOrderReportPageWeb.isQuestionnaireSectionVisible();

        step("Assertions");
        Assertion.assertThat("Questionnaire section is visible on 1st order", isQuestionnaireVisible1, is(true));
        Assertion.assertThat("Questionnaire section is not visible on 2nd order", isQuestionnaireVisible2, is(false));
    }

    @Author(names = {"Akbar"})
    @Module(name = "ThingsToDoList")
    @Tribe(name = "Ignis")
    @Vertical(name = "TTD")
    @Test
    public void testIgnisCheckClonePackage() {
        Map<String, String> param = getParam("testIgnisCheckClonePackage");
        BaseTest.paramsThreadLocal.set(param);
        String username = param.get("username");
        String password = param.get("password");

        String partnerName = param.get("partnerName");
        String productName = param.get("productName");
        String packageName = param.get("packageName");

        Session<?> session = BaseTest.sessionThreadLocal.get();
        IgnisLoginPageWeb<?> ignisLoginPageWeb = IgnisLoginPageWeb.getInstance(session);
        IgnisSelectPartnerPageWeb<?> ignisSelectPartnerPageWeb = IgnisSelectPartnerPageWeb.getInstance(session);
        IgnisPartnerPageWeb<?> ignisPartnerPageWeb = IgnisPartnerPageWeb.getInstance(session);
        IgnisThingsToDoListPageWeb<?> ignisThingsToDoListPageWeb = IgnisThingsToDoListPageWeb.getInstance(session);
        IgnisProgressLogPageWeb<?> ignisProgressLogPageWeb = IgnisProgressLogPageWeb.getInstance(session);

        step("Go to Ignis dashboard and login");
        ignisLoginPageWeb.loginAsSpecificUser(username, password);

        step("Select Partner");
        ignisSelectPartnerPageWeb.selectPartner(partnerName);

        step("Go to things to do list page");
        ignisPartnerPageWeb.clickSeeAllThingToDo();

        step("Search for a product and expand the section");
        ignisThingsToDoListPageWeb.searchProduct(productName);
        ignisThingsToDoListPageWeb.openProductPackageSection(productName);
        ignisThingsToDoListPageWeb.clickPackageOption(packageName, "clone/clone");

        step("Clone the package to the same product");
        ignisThingsToDoListPageWeb.clonePackageAddThingsToDo(productName);
        ignisThingsToDoListPageWeb.clonePackagePreviewClickSubmit();

        step("Check the progress log");
        boolean isProgressLogValid = ignisProgressLogPageWeb.verifyFirstRow("clone/clone", productName);

        step("Assertions");
        Assertion.assertThat("Progress log is valid", isProgressLogValid, is(true));
    }

    @Author(names = {"Akbar"})
    @Module(name = "RedeemVoucher")
    @Tribe(name = "Ignis")
    @Vertical(name = "TTD")
    @Test
    public void testRedeemRefundedTicket() {
        Map<String, String> param = getParam("testRedeemRefundedTicket");
        BaseTest.paramsThreadLocal.set(param);

        String username = param.get("username");
        String password = param.get("password");

        String partnerName = param.get("partnerName");
        String orderId = param.get("orderId");

        Session<?> session = BaseTest.sessionThreadLocal.get();

        IgnisLoginPageWeb<?> ignisLoginPageWeb = IgnisLoginPageWeb.getInstance(session);
        IgnisSelectPartnerPageWeb<?> ignisSelectPartnerPageWeb = IgnisSelectPartnerPageWeb.getInstance(session);
        IgnisPartnerPageWeb<?> ignisPartnerPageWeb = IgnisPartnerPageWeb.getInstance(session);
        IgnisRedeemVoucherPageWeb<?> ignisRedeemVoucherPage = IgnisRedeemVoucherPageWeb.getInstance(session);

        step("Go to Ignis dashboard and login");
        ignisLoginPageWeb.loginAsSpecificUser(username, password);

        step("Select Partner");
        ignisSelectPartnerPageWeb.selectPartner(partnerName);

        step("Input the orderId on redeem section");
        ignisPartnerPageWeb.inputRedeemCode(orderId);
        ignisPartnerPageWeb.submitRedeemCode();

        step("Click redeem button");
        ignisRedeemVoucherPage.clickRedeemVoucherButton();

        step("Assertions");
        Assertion.assertThat("Refunded notif bar is visible", ignisRedeemVoucherPage.isFailNotifBannerVisible(), is(true));
        Assertion.assertThat("Refunded snackbar is visible", ignisRedeemVoucherPage.isFailSnackBarVisible(), is(true));
    }

    @Author(names = {"Akbar"})
    @Module(name = "ThingsToDoList")
    @Tribe(name = "Ignis")
    @Vertical(name = "TTD")
    @Test
    public void testIgnisEditProductUntilSubmitReview() {
        Map<String, String> param = getParam("testIgnisEditProductUntilSubmitReview");
        BaseTest.paramsThreadLocal.set(param);

        String ignisUsername = param.get("ignisUsername");
        String ignisPassword = param.get("ignisPassword");

        String partnerName = param.get("partnerName");
        String productName = param.get("productName");
        String updateDescription = param.get("updateDescription");

        String cmsUsername = param.get("cmsUsername");
        String cmsPassword = param.get("cmsPassword");

        Session<?> session = BaseTest.sessionThreadLocal.get();

        IgnisLoginPageWeb<?> ignisLoginPageWeb = IgnisLoginPageWeb.getInstance(session);
        IgnisSelectPartnerPageWeb<?> ignisSelectPartnerPageWeb = IgnisSelectPartnerPageWeb.getInstance(session);
        IgnisPartnerPageWeb<?> ignisPartnerPageWeb = IgnisPartnerPageWeb.getInstance(session);
        IgnisThingsToDoListPageWeb<?> ignisThingsToDoListPageWeb = IgnisThingsToDoListPageWeb.getInstance(session);
        IgnisThingsToDoEditPageWeb<?> ignisThingsToDoEditPageWeb = IgnisThingsToDoEditPageWeb.getInstance(session);

        CMSLoginPageWeb<?> cmsLoginPageWeb = CMSLoginPageWeb.getInstance(session);
        CMSInventoryManagementThingsToDoListPageWeb<?> cmsInventoryManagementThingsToDoListPageWeb = CMSInventoryManagementThingsToDoListPageWeb.getInstance(session);
        CMSThingsToDoReviewPageWeb<?> cmsThingsToDoListReviewPageWeb = CMSThingsToDoReviewPageWeb.getInstance(session);

        step("Go to Ignis dashboard and login");
        ignisLoginPageWeb.loginAsSpecificUser(ignisUsername, ignisPassword);

        step("Select Partner");
        ignisSelectPartnerPageWeb.selectPartner(partnerName);

        step("Go to things to do list page");
        ignisPartnerPageWeb.clickSeeAllThingToDo();

        step("Search for the product and click edit button");
        ignisThingsToDoListPageWeb.searchProduct(productName);
        ignisThingsToDoListPageWeb.clickEditButton(productName);

        step("Click continue until description and edit the value");
        ignisThingsToDoEditPageWeb.goToTab("deskripsi/description");
        ignisThingsToDoEditPageWeb.editProductDescription(updateDescription);

        step("Click continue until submit");
        ignisThingsToDoEditPageWeb.clickContinueUntilSubmit();
        ignisThingsToDoEditPageWeb.clickSubmitButton();

        step("Check status of the product should be 'in review'");
        ignisThingsToDoEditPageWeb.backToThingsToDoPage();
        ignisThingsToDoListPageWeb.goToTab("things to do dalam proses/things to do in approval");
        ignisThingsToDoListPageWeb.searchProduct(productName);
        boolean isProductStatusInReview = ignisThingsToDoListPageWeb.isProductStatusInReview(productName);

        step("Post-Test : Logout from ignis and go to CMS login page");
        ignisLoginPageWeb.logoutIgnis();
        cmsLoginPageWeb.loginAsSpecificUserViaUI(cmsUsername, cmsPassword);

        step("Post-Test : Go to things to do page on CMS");
        cmsInventoryManagementThingsToDoListPageWeb.goToPage();
        cmsInventoryManagementThingsToDoListPageWeb.inputSearchBar(productName);
        cmsInventoryManagementThingsToDoListPageWeb.pressSearchButton();
        cmsInventoryManagementThingsToDoListPageWeb.goToTab(2);

        step("Post-Test : Start review and approve it");
        cmsInventoryManagementThingsToDoListPageWeb.startProductReview(productName);
        cmsThingsToDoListReviewPageWeb.continueUntilSubmit();
        cmsThingsToDoListReviewPageWeb.approveAndSubmitReview();

        step("Post-Test : logout CMS");
        cmsLoginPageWeb.logoutCMS();

        step("Assertions");
        Assertion.assertThat("Product status is in review", isProductStatusInReview, is(true));
    }

    @Author(names = {"Akbar"})
    @Module(name = "ThingsToDoList")
    @Tribe(name = "Ignis")
    @Vertical(name = "TTD")
    @Test
    public void testIgnisEditPackageUntilSubmitReview() {
        Map<String, String> param = getParam("testIgnisEditPackageUntilSubmitReview");
        BaseTest.paramsThreadLocal.set(param);

        String ignisUsername = param.get("ignisUsername");
        String ignisPassword = param.get("ignisPassword");

        String partnerName = param.get("partnerName");
        String productName = param.get("productName");
        String packageName = param.get("packageName");
        String updateAdditionalInfo = param.get("updateAdditionalInfo");

        String cmsUsername = param.get("cmsUsername");
        String cmsPassword = param.get("cmsPassword");

        Session<?> session = BaseTest.sessionThreadLocal.get();

        IgnisLoginPageWeb<?> ignisLoginPageWeb = IgnisLoginPageWeb.getInstance(session);
        IgnisSelectPartnerPageWeb<?> ignisSelectPartnerPageWeb = IgnisSelectPartnerPageWeb.getInstance(session);
        IgnisPartnerPageWeb<?> ignisPartnerPageWeb = IgnisPartnerPageWeb.getInstance(session);
        IgnisThingsToDoListPageWeb<?> ignisThingsToDoListPageWeb = IgnisThingsToDoListPageWeb.getInstance(session);
        IgnisThingsToDoEditPageWeb<?> ignisThingsToDoEditPageWeb = IgnisThingsToDoEditPageWeb.getInstance(session);

        CMSLoginPageWeb<?> cmsLoginPageWeb = CMSLoginPageWeb.getInstance(session);
        CMSInventoryManagementThingsToDoListPageWeb<?> cmsInventoryManagementThingsToDoListPageWeb = CMSInventoryManagementThingsToDoListPageWeb.getInstance(session);
        CMSThingsToDoReviewPageWeb<?> cmsThingsToDoListReviewPageWeb = CMSThingsToDoReviewPageWeb.getInstance(session);

        step("Go to Ignis dashboard and login");
        ignisLoginPageWeb.loginAsSpecificUser(ignisUsername, ignisPassword);

        step("Select Partner");
        ignisSelectPartnerPageWeb.selectPartner(partnerName);

        step("Go to things to do list page");
        ignisPartnerPageWeb.clickSeeAllThingToDo();

        step("Search for the product and click edit button");
        ignisThingsToDoListPageWeb.searchProduct(productName);
        ignisThingsToDoListPageWeb.openProductPackageSection(productName);
        ignisThingsToDoListPageWeb.clickEditButton(productName, packageName);

        step("Click continue until description and edit the value");
        ignisThingsToDoEditPageWeb.editPackageAdditionalInfo(updateAdditionalInfo);

        step("Click continue until submit");
        ignisThingsToDoEditPageWeb.clickContinueUntilSubmit();
        ignisThingsToDoEditPageWeb.clickSubmitButton();

        step("Back to things to do page");
        ignisThingsToDoEditPageWeb.backToThingsToDoPage();

        step("Login to CMS");
        ignisLoginPageWeb.logoutIgnis();
        cmsLoginPageWeb.loginAsSpecificUser(cmsUsername, cmsPassword);

        step("Go to things to do page on CMS");
        cmsInventoryManagementThingsToDoListPageWeb.goToPage();
        cmsInventoryManagementThingsToDoListPageWeb.inputSearchBar(packageName);
        cmsInventoryManagementThingsToDoListPageWeb.pressSearchButton();
        cmsInventoryManagementThingsToDoListPageWeb.goToTab(3);

        step("Post-Test : Start review and approve it");
        boolean isPackageStatusNeedReview = cmsInventoryManagementThingsToDoListPageWeb.isStatusNeedReview(packageName);
        cmsInventoryManagementThingsToDoListPageWeb.startPackageReview(packageName);
        cmsThingsToDoListReviewPageWeb.continueUntilSubmit();
        cmsThingsToDoListReviewPageWeb.approveAndSubmitReview();

        step("Post-Test : logout CMS");
        cmsLoginPageWeb.logoutCMS();

        step("Assertions");
        Assertion.assertThat("Package status is need review on CMS", isPackageStatusNeedReview, is(true));
    }

    @Author(names = {"Akbar"})
    @Module(name = "ThingsToDoList")
    @Tribe(name = "Ignis")
    @Vertical(name = "TTD")
    @Test
    public void testIgnisCheckApprovedProductFromCMS() {
        Map<String, String> param = getParam("testIgnisCheckApprovedProductFromCMS");
        BaseTest.paramsThreadLocal.set(param);

        String ignisUsername = param.get("ignisUsername");
        String ignisPassword = param.get("ignisPassword");

        String partnerName = param.get("partnerName");
        String productName = param.get("productName");
        String updateDescription = param.get("updateDescription");

        String cmsUsername = param.get("cmsUsername");
        String cmsPassword = param.get("cmsPassword");

        Session<?> session = BaseTest.sessionThreadLocal.get();

        IgnisLoginPageWeb<?> ignisLoginPageWeb = IgnisLoginPageWeb.getInstance(session);
        IgnisSelectPartnerPageWeb<?> ignisSelectPartnerPageWeb = IgnisSelectPartnerPageWeb.getInstance(session);
        IgnisPartnerPageWeb<?> ignisPartnerPageWeb = IgnisPartnerPageWeb.getInstance(session);
        IgnisThingsToDoListPageWeb<?> ignisThingsToDoListPageWeb = IgnisThingsToDoListPageWeb.getInstance(session);
        IgnisThingsToDoEditPageWeb<?> ignisThingsToDoEditPageWeb = IgnisThingsToDoEditPageWeb.getInstance(session);

        CMSLoginPageWeb<?> cmsLoginPageWeb = CMSLoginPageWeb.getInstance(session);
        CMSInventoryManagementThingsToDoListPageWeb<?> cmsInventoryManagementThingsToDoListPageWeb = CMSInventoryManagementThingsToDoListPageWeb.getInstance(session);
        CMSThingsToDoReviewPageWeb<?> cmsThingsToDoListReviewPageWeb = CMSThingsToDoReviewPageWeb.getInstance(session);

        step("Pre-Test : Go to Ignis dashboard and login");
        ignisLoginPageWeb.loginAsSpecificUser(ignisUsername, ignisPassword);

        step("Pre-Test : Select Partner");
        ignisSelectPartnerPageWeb.selectPartner(partnerName);

        step("Pre-Test : Go to things to do list page");
        ignisPartnerPageWeb.clickSeeAllThingToDo();

        step("Pre-Test : Search for the product and click edit button");
        ignisThingsToDoListPageWeb.searchProduct(productName);
        ignisThingsToDoListPageWeb.clickEditButton(productName);

        step("Pre-Test : Click continue until description and edit the value");
        ignisThingsToDoEditPageWeb.goToTab("deskripsi/description");
        updateDescription = ignisThingsToDoEditPageWeb.editProductDescription(updateDescription);

        step("Pre-Test : Click continue until submit");
        ignisThingsToDoEditPageWeb.clickContinueUntilSubmit();
        ignisThingsToDoEditPageWeb.clickSubmitButton();
        ignisThingsToDoEditPageWeb.backToThingsToDoPage();

        step("Logout from ignis and go to CMS login page");
        ignisLoginPageWeb.logoutIgnis();
        cmsLoginPageWeb.loginAsSpecificUser(cmsUsername, cmsPassword);

        step("Go to things to do page on CMS");
        cmsInventoryManagementThingsToDoListPageWeb.goToPage();
        cmsInventoryManagementThingsToDoListPageWeb.inputSearchBar(productName);
        cmsInventoryManagementThingsToDoListPageWeb.pressSearchButton();
        cmsInventoryManagementThingsToDoListPageWeb.goToTab(2);

        step("Start review and approve it");
        cmsInventoryManagementThingsToDoListPageWeb.startProductReview(productName);
        cmsThingsToDoListReviewPageWeb.continueUntilSubmit();
        cmsThingsToDoListReviewPageWeb.approveAndSubmitReview();

        step("logout CMS");
        cmsLoginPageWeb.logoutCMS();

        step("Go to Ignis dashboard and login");
        ignisLoginPageWeb.loginAsSpecificUser(ignisUsername, ignisPassword);

        step("Select Partner");
        ignisSelectPartnerPageWeb.selectPartner(partnerName);

        step("Go to things to do list page");
        ignisPartnerPageWeb.clickSeeAllThingToDo();

        step("Search for the product and click edit button");
        ignisThingsToDoListPageWeb.searchProduct(productName);
        ignisThingsToDoListPageWeb.clickProduct(productName);
        String displayedDescription = ignisThingsToDoListPageWeb.getProductDescription();

        step("Assertions");
        Assertion.assertThat("Product description is updated", displayedDescription, is(updateDescription));
    }

    @Author(names = {"Akbar"})
    @Module(name = "ThingsToDoList")
    @Tribe(name = "Ignis")
    @Vertical(name = "TTD")
    @Test
    public void testIgnisCheckApprovedPackageFromCMS() {
        Map<String, String> param = getParam("testIgnisCheckApprovedPackageFromCMS");
        BaseTest.paramsThreadLocal.set(param);

        String ignisUsername = param.get("ignisUsername");
        String ignisPassword = param.get("ignisPassword");

        String partnerName = param.get("partnerName");
        String productName = param.get("productName");
        String packageName = param.get("packageName");
        String updateAdditionalInfo = param.get("updateAdditionalInfo");

        String cmsUsername = param.get("cmsUsername");
        String cmsPassword = param.get("cmsPassword");

        Session<?> session = BaseTest.sessionThreadLocal.get();

        IgnisLoginPageWeb<?> ignisLoginPageWeb = IgnisLoginPageWeb.getInstance(session);
        IgnisSelectPartnerPageWeb<?> ignisSelectPartnerPageWeb = IgnisSelectPartnerPageWeb.getInstance(session);
        IgnisPartnerPageWeb<?> ignisPartnerPageWeb = IgnisPartnerPageWeb.getInstance(session);
        IgnisThingsToDoListPageWeb<?> ignisThingsToDoListPageWeb = IgnisThingsToDoListPageWeb.getInstance(session);
        IgnisThingsToDoEditPageWeb<?> ignisThingsToDoEditPageWeb = IgnisThingsToDoEditPageWeb.getInstance(session);

        CMSLoginPageWeb<?> cmsLoginPageWeb = CMSLoginPageWeb.getInstance(session);
        CMSInventoryManagementThingsToDoListPageWeb<?> cmsInventoryManagementThingsToDoListPageWeb = CMSInventoryManagementThingsToDoListPageWeb.getInstance(session);
        CMSThingsToDoReviewPageWeb<?> cmsThingsToDoListReviewPageWeb = CMSThingsToDoReviewPageWeb.getInstance(session);

        step("Pre-Test : Go to Ignis dashboard and login");
        ignisLoginPageWeb.loginAsSpecificUser(ignisUsername, ignisPassword);

        step("Pre-Test : Select Partner");
        ignisSelectPartnerPageWeb.selectPartner(partnerName);

        step("Pre-Test : Go to things to do list page");
        ignisPartnerPageWeb.clickSeeAllThingToDo();

        step("Pre-Test : Search for the product and click edit button");
        ignisThingsToDoListPageWeb.searchProduct(productName);
        ignisThingsToDoListPageWeb.openProductPackageSection(productName);
        ignisThingsToDoListPageWeb.clickEditButton(productName, packageName);

        step("Pre-Test : Click continue until description and edit the value");
        updateAdditionalInfo = ignisThingsToDoEditPageWeb.editPackageAdditionalInfo(updateAdditionalInfo);

        step("Pre-Test : Click continue until submit");
        ignisThingsToDoEditPageWeb.clickContinueUntilSubmit();
        ignisThingsToDoEditPageWeb.clickSubmitButton();

        step("Pre-Test : Back to things to do page");
        ignisThingsToDoEditPageWeb.backToThingsToDoPage();

        step("Login to CMS");
        ignisLoginPageWeb.logoutIgnis();
        cmsLoginPageWeb.loginAsSpecificUser(cmsUsername, cmsPassword);

        step("Go to things to do page on CMS");
        cmsInventoryManagementThingsToDoListPageWeb.goToPage();
        cmsInventoryManagementThingsToDoListPageWeb.inputSearchBar(packageName);
        cmsInventoryManagementThingsToDoListPageWeb.pressSearchButton();
        cmsInventoryManagementThingsToDoListPageWeb.goToTab(3);

        step("Start review and approve it");
        cmsInventoryManagementThingsToDoListPageWeb.startPackageReview(packageName);
        cmsThingsToDoListReviewPageWeb.continueUntilSubmit();
        cmsThingsToDoListReviewPageWeb.approveAndSubmitReview();

        step("logout CMS");
        cmsLoginPageWeb.logoutCMS();

        step("Go to Ignis dashboard and login");
        ignisLoginPageWeb.loginAsSpecificUser(ignisUsername, ignisPassword);

        step("Select Partner");
        ignisSelectPartnerPageWeb.selectPartner(partnerName);

        step("Go to things to do list page");
        ignisPartnerPageWeb.clickSeeAllThingToDo();

        step("Search for the product and click edit button");
        ignisThingsToDoListPageWeb.searchProduct(productName);
        ignisThingsToDoListPageWeb.openProductPackageSection(productName);
        ignisThingsToDoListPageWeb.clickPackageTitle(packageName);
        String displayedAdditionalInfo = ignisThingsToDoListPageWeb.getPackageAdditionalInfo();

        step("Assertions");
        Assertion.assertThat("Product description is updated", displayedAdditionalInfo, is(updateAdditionalInfo));
    }

    @Author(names = {"Ram Pratap Singh", "Annisa"})
    @Module(name = "ProductList")
    @Tribe(name = "CMS")
    @Vertical(name = "TTD")
    @Test
    public void testUpdatePackageInInventoryManagement() {
        Map<String, String> param = getParam("testUpdatePackageInInventoryManagement");
        BaseTest.paramsThreadLocal.set(param);

        String cmsUsername = param.get("cmsUsername");
        String cmsPassword = param.get("cmsPassword");
        String description = param.get("description");
        String productName = param.get("productName");
        String productURL = param.get("productURL");

        Session<?> session = BaseTest.sessionThreadLocal.get();

        CMSLoginPageWeb<?> cmsLoginPageWeb = CMSLoginPageWeb.getInstance(session);
        CMSInventoryManagementProductListPageWeb<?> cmsInventoryManagementProductListPageWeb = CMSInventoryManagementProductListPageWeb.getInstance(session);

        step("Go to CMS login page and login");
        cmsLoginPageWeb.loginAsSpecificUser(cmsUsername, cmsPassword);

        step("Go to Product List page");
        cmsInventoryManagementProductListPageWeb.goToPage();

        step("Search for the product");
        cmsInventoryManagementProductListPageWeb.inputSearchBar(productName);
        cmsInventoryManagementProductListPageWeb.pressSearchButton();

        step("Go to Package section");
        cmsInventoryManagementProductListPageWeb.goToPackage();

        step("Edit the package");
        String textToCheck = cmsInventoryManagementProductListPageWeb.editPackage(description);

        step("Go to B2C and check the package");
        cmsInventoryManagementProductListPageWeb.goToProductInB2C(productURL);
        boolean isPackageVisible = cmsInventoryManagementProductListPageWeb.checkUpdateinB2C(textToCheck);

        step("The user verifies that the package is updated in B2C");
        Assertion.assertThat("Package is updated in B2C", isPackageVisible, is(true));
    }

    @Author(names = {"Ram Pratap Singh", "Annisa"})
    @Module(name = "ProductList")
    @Tribe(name = "CMS")
    @Vertical(name = "TTD")
    @Test
    public void testSingleClonePackageInInventoryManagement() {
        Map<String, String> param = getParam("testSingleClonePackageInInventoryManagement");
        BaseTest.paramsThreadLocal.set(param);

        String cmsUsername = param.get("cmsUsername");
        String cmsPassword = param.get("cmsPassword");
        String productName = param.get("productName");

        Session<?> session = BaseTest.sessionThreadLocal.get();

        CMSLoginPageWeb<?> cmsLoginPageWeb = CMSLoginPageWeb.getInstance(session);
        CMSInventoryManagementProductListPageWeb<?> cmsInventoryManagementProductListPageWeb = CMSInventoryManagementProductListPageWeb.getInstance(session);

        step("Go to CMS login page and login");
        cmsLoginPageWeb.loginAsSpecificUser(cmsUsername, cmsPassword);

        step("Go to Product List page");
        cmsInventoryManagementProductListPageWeb.goToPage();

        step("Search for the product");
        cmsInventoryManagementProductListPageWeb.inputSearchBar(productName);
        cmsInventoryManagementProductListPageWeb.pressSearchButton();

        step("Go to Package section");
        cmsInventoryManagementProductListPageWeb.goToPackage();

        step("The user clones the package");
        String packageName = cmsInventoryManagementProductListPageWeb.cloneProduct();

        step("The user verifies that the package is cloned");
        boolean isPackageCloned = cmsInventoryManagementProductListPageWeb.isPackageCloned(packageName);
        Assertion.assertThat("Package is cloned successfully", isPackageCloned, is(true));
    }

    @Author(names = {"Akbar"})
    @Module(name = "PartnerBooking")
    @Tribe(name = "Ignis")
    @Vertical(name = "TTD")
    @Test
    public void testIgnisCheckPartnerBookingExceed25() {
        Map<String, String> param = getParam("testIgnisCheckPartnerBookingExceed25");
        BaseTest.paramsThreadLocal.set(param);

        String username = param.get("username");
        String password = param.get("password");

        String productName = param.get("productName");
        String packageName = param.get("packageName");
        int ticketQty = Integer.parseInt(param.get("ticketQty"));
        String rangeBookDate = param.get("rangeBookDate");

        String partnerName = param.get("partnerName");

        Session<?> session = BaseTest.sessionThreadLocal.get();

        IgnisLoginPageWeb<?> ignisLoginPageWeb = IgnisLoginPageWeb.getInstance(session);
        IgnisSelectPartnerPageWeb<?> ignisSelectPartnerPageWeb = IgnisSelectPartnerPageWeb.getInstance(session);
        IgnisPartnerBookingPageWeb<?> ignisPartnerBookingPageWeb = IgnisPartnerBookingPageWeb.getInstance(session);
        IgnisPartnerBookingFormPageWeb<?> ignisPartnerBookingFormPageWeb = IgnisPartnerBookingFormPageWeb.getInstance(session);

        step("Go to Ignis dashboard and login");
        ignisLoginPageWeb.loginAsSpecificUser(username, password);

        step("Select Partner");
        ignisSelectPartnerPageWeb.selectPartner(partnerName);

        step("Choose product and package for partner booking");
        ignisPartnerBookingPageWeb.goToPage();
        ignisPartnerBookingPageWeb.createNewPartnerBooking(productName, packageName, ticketQty, rangeBookDate);

        step("Fill and submit the booking form");
        Map<String, Map<String, String>> contactAndGuestData = ignisPartnerBookingFormPageWeb.fillBookingForm(param);
        ignisPartnerBookingFormPageWeb.choosePaymentMethod("internal");
        ignisPartnerBookingFormPageWeb.submitBook();

        step("Check order details or the newly created order using partner booking");
        ignisPartnerBookingPageWeb.waitAndRefresh(5);
        ignisPartnerBookingPageWeb.chooseProductFilter(productName);
        ignisPartnerBookingPageWeb.applyFilter();
        ignisPartnerBookingPageWeb.openOrderDataByIndex(1);

        step("Data extraction");
        String contactFullName = contactAndGuestData.get("contact").get("firstName") + " " + contactAndGuestData.get("contact").get("lastName");
        String displayedName = ignisPartnerBookingPageWeb.getOrderContactName();
        Assertion.assertThat("Order is the correct one", displayedName, containsString(contactFullName));
        int displayedBookedQty = ignisPartnerBookingPageWeb.getOrderTicketQty();
        boolean isOrderIssued = ignisPartnerBookingPageWeb.isOrderIssued();

        step("Assertions");
        Assertion.assertThat("Qty is matched", displayedBookedQty, is(ticketQty));
        Assertion.assertThat("Qty is above 25", displayedBookedQty, greaterThanOrEqualTo(26));
        Assertion.assertThat("Order is issued", isOrderIssued, is(true));
    }

    @Author(names = {"Akbar"})
    @Module(name = "PartnerBooking")
    @Tribe(name = "Ignis")
    @Vertical(name = "TTD")
    @Test
    public void testIgnisCheckPurchaseDateAndExpirationDatePackage() {
        Map<String, String> param = getParam("testIgnisCheckPurchaseDateAndExpirationDatePackage");
        BaseTest.paramsThreadLocal.set(param);

        String username = param.get("username");
        String password = param.get("password");

        String productName = param.get("productName");
        String purchaseDatePackageName = param.get("purchaseDatePackageName");
        String expiredDatePackageName = param.get("expiredDatePackageName");

        String partnerName = param.get("partnerName");

        Session<?> session = BaseTest.sessionThreadLocal.get();

        IgnisLoginPageWeb<?> ignisLoginPageWeb = IgnisLoginPageWeb.getInstance(session);
        IgnisSelectPartnerPageWeb<?> ignisSelectPartnerPageWeb = IgnisSelectPartnerPageWeb.getInstance(session);
        IgnisPartnerBookingPageWeb<?> ignisPartnerBookingPageWeb = IgnisPartnerBookingPageWeb.getInstance(session);

        step("Go to Ignis dashboard and login");
        ignisLoginPageWeb.loginAsSpecificUser(username, password);

        step("Select Partner");
        ignisSelectPartnerPageWeb.selectPartner(partnerName);

        step("Go to partner booking page");
        ignisPartnerBookingPageWeb.goToPage();

        step("Choose product and package for partner booking");
        ignisPartnerBookingPageWeb.clickCreateBookingButton();
        ignisPartnerBookingPageWeb.inputProductSearchBar(productName);
        ignisPartnerBookingPageWeb.chooseProduct(productName);

        step("Open package with purchase date");
        ignisPartnerBookingPageWeb.inputPackageSearchBar(purchaseDatePackageName);
        ignisPartnerBookingPageWeb.choosePackage(purchaseDatePackageName);
        boolean isTodayDateExist1 = ignisPartnerBookingPageWeb.isTodayDatePickerAvailable();

        step("Open package with expiration date");
        ignisPartnerBookingPageWeb.inputPackageSearchBar(expiredDatePackageName);
        ignisPartnerBookingPageWeb.choosePackage(expiredDatePackageName);
        boolean isTodayDateExist2 = ignisPartnerBookingPageWeb.isTodayDatePickerAvailable();

        step("Assertions");
        Assertion.assertThat("Today date is available for purchase date package", isTodayDateExist1, is(true));
        Assertion.assertThat("Today date is available for expiration date package", isTodayDateExist2, is(true));
    }

    @Author(names = {"Akbar"})
    @Module(name = "TicketingManagementSystem")
    @Tribe(name = "Ignis")
    @Vertical(name = "TTD")
    @Test
    public void testIgnisCheckGenerateBarcodeTMS() throws IOException {
        Map<String, String> param = getParam("testIgnisCheckGenerateBarcodeTMS");
        BaseTest.paramsThreadLocal.set(param);

        String username = param.get("username");
        String password = param.get("password");

        String partnerName = param.get("partnerName");
        String eventName = param.get("eventName");
        String categoryName = param.get("categoryName") + "-" + System.currentTimeMillis();
        String quantity = param.get("quantity");

        Session<?> session = BaseTest.sessionThreadLocal.get();

        IgnisLoginPageWeb<?> ignisLoginPageWeb = IgnisLoginPageWeb.getInstance(session);
        IgnisSelectPartnerPageWeb<?> ignisSelectPartnerPageWeb = IgnisSelectPartnerPageWeb.getInstance(session);
        IgnisTicketingManagementSystemPageWeb<?> ignisTicketingManagementSystemPageWeb = IgnisTicketingManagementSystemPageWeb.getInstance(session);
        IgnisProgressLogPageWeb<?> ignisProgressLogPageWeb = IgnisProgressLogPageWeb.getInstance(session);

        step("Go to Ignis dashboard and login");
        ignisLoginPageWeb.loginAsSpecificUser(username, password);

        step("Select Partner");
        ignisSelectPartnerPageWeb.selectPartner(partnerName);

        step("Go to TMS page");
        ignisTicketingManagementSystemPageWeb.goToPage();

        step("Choose Event '" + eventName + "'");
        ignisTicketingManagementSystemPageWeb.chooseEvent(eventName);

        step("Generate Barcode");
        ignisTicketingManagementSystemPageWeb.clickGenerateBarcodeButton();

        step("Input Categories Name and Quantity");
        ignisTicketingManagementSystemPageWeb.addCategory(categoryName, quantity);
        ignisTicketingManagementSystemPageWeb.clickGenerateButtonPopup();

        step("Check Progress Log");
        boolean progressLogCheckResult = ignisProgressLogPageWeb.verifyTMSProgressLog("Create barcode", categoryName, quantity);

        step("Download and Check The Downloaded Report");
        ignisProgressLogPageWeb.downloadFirstRowReport();
        boolean barcodeCheckResult = ignisTicketingManagementSystemPageWeb.checkNewlyDownloadedBarcodeGenerated(categoryName, quantity);

        step("Assertion");
        Assertion.assertThat("Progress log is valid", progressLogCheckResult, is(true));
        Assertion.assertThat("Barcode report is valid", barcodeCheckResult, is(true));
    }

    @Author(names = {"Rizki"})
    @Module(name = "ManageOrderReport")
    @Tribe(name = "Ignis")
    @Vertical(name = "TTD")
    @Test
    public void testIgnisExtendVisitDateMultipleOrderId() {
        Map<String, String> param = getParam("testIgnisExtendVisitDateMultipleOrderId");
        BaseTest.paramsThreadLocal.set(param);

        String username = param.get("username");
        String password = param.get("password");

        String partnerName = param.get("partnerName");
        String productName = param.get("productName");
        String packageName = param.get("packageName");
        String updateDays = param.get("updateDays");
        String updateAmount = param.get("updateAmount");

        Session<?> session = BaseTest.sessionThreadLocal.get();
        IgnisLoginPageWeb<?> ignisLoginPageWeb = IgnisLoginPageWeb.getInstance(session);
        IgnisSelectPartnerPageWeb<?> ignisSelectPartnerPageWeb = IgnisSelectPartnerPageWeb.getInstance(session);
        IgnisOrderReportPageWeb<?> ignisOrderReportPageWeb = IgnisOrderReportPageWeb.getInstance(session);
        IgnisManageOrderPageWeb<?> ignisManageOrderVisitDatePageWeb = IgnisManageOrderPageWeb.getInstance(session);

        step("Login to Ignis");
        ignisLoginPageWeb.loginAsSpecificUser(username, password);

        step("Select any partners");
        ignisSelectPartnerPageWeb.selectPartner(partnerName);

        step("Go to Order Report page");
        ignisOrderReportPageWeb.goToPage();

        step("Click Order Configurations, select Manage Order");
        ignisOrderReportPageWeb.clickOrderConfigButton();
        ignisOrderReportPageWeb.clickOrderConfigOptions("manage order/atur order");

        step("On the popup, select Update Visit Date radio button");
        ignisOrderReportPageWeb.selectUpdateOptionManageOrderPopup("update visit date");

        step("Click Continue");
        ignisOrderReportPageWeb.clickContinueOnManageOrderPopup();

        step("On Manage Order page, select product and packages");
        ignisManageOrderVisitDatePageWeb.setTTDFilter(productName);
        ignisManageOrderVisitDatePageWeb.setPackageFilter(packageName);

        step("Click Search");
        ignisManageOrderVisitDatePageWeb.clickSearchButton();

        step("Select any order ids to be extended");
        List<String> updatedOrderIds = ignisManageOrderVisitDatePageWeb.selectRandomRow(Integer.parseInt(updateAmount));

        step("Click Manage Order");
        ignisManageOrderVisitDatePageWeb.clickManageOrderButton();

        step("Select New Date and Timeslot (if any)");
        ignisManageOrderVisitDatePageWeb.inputNewVisitDate(Integer.parseInt(updateDays));
        ignisManageOrderVisitDatePageWeb.inputNewTimeVisit();
        ignisManageOrderVisitDatePageWeb.clickOkUpdateOrderPopUp();

        step("Click OK");
        String newDateTime = ignisManageOrderVisitDatePageWeb.getNewUpdatedDate();
        ignisManageOrderVisitDatePageWeb.clickOkPreviewPopUp();

        step("Click Apply");
        ignisManageOrderVisitDatePageWeb.clickOkApplyPopUp();

        step("Check whether the extended orders can still be redeemed");
        ignisOrderReportPageWeb.goToPage();
        ignisOrderReportPageWeb.setSearchByOrder();
        for (String id : updatedOrderIds) {
            ignisOrderReportPageWeb.inputOrderId(id);
            Assertion.assertThat(String.format("Order ID %s has been extended successfully", id), ignisOrderReportPageWeb.verifyVisitDateWithDateString(newDateTime), is(true));
        }
    }

    @Author(names = {"Akbar"})
    @Module(name = "ThingsToDoList")
    @Tribe(name = "Ignis")
    @Vertical(name = "TTD")
    @Test
    public void testIgnisGenerateQRCodeSelfRedemption() {
        Map<String, String> param = getParam("testIgnisGenerateQRCodeSelfRedemption");
        BaseTest.paramsThreadLocal.set(param);

        String username = param.get("username");
        String password = param.get("password");

        String partnerName = param.get("partnerName");
        String productName = param.get("productName");

        Session<?> session = BaseTest.sessionThreadLocal.get();

        IgnisLoginPageWeb<?> ignisLoginPageWeb = IgnisLoginPageWeb.getInstance(session);
        IgnisSelectPartnerPageWeb<?> ignisSelectPartnerPageWeb = IgnisSelectPartnerPageWeb.getInstance(session);
        IgnisPartnerPageWeb<?> ignisPartnerPageWeb = IgnisPartnerPageWeb.getInstance(session);
        IgnisThingsToDoListPageWeb<?> ignisThingsToDoListPageWeb = IgnisThingsToDoListPageWeb.getInstance(session);
        IgnisSelfRedemptionPageWeb<?> ignisSelfRedemptionPageWeb = IgnisSelfRedemptionPageWeb.getInstance(session);

        step("Go to Ignis dashboard and login");
        ignisLoginPageWeb.loginAsSpecificUser(username, password);

        step("Select Partner");
        ignisSelectPartnerPageWeb.selectPartner(partnerName);

        step("Go to things to do list page");
        ignisPartnerPageWeb.clickSeeAllThingToDo();

        step("Search product and click three dots button");
        ignisThingsToDoListPageWeb.searchProduct(productName);
        ignisThingsToDoListPageWeb.clickProductOption(productName, "self redemption/-");

        step("Check the QR is loaded or not");
        boolean QRCheckResult = ignisSelfRedemptionPageWeb.isQRVisible();

        step("Assertion");
        Assertion.assertThat("QR for self-redemption is visible", QRCheckResult, is(true));
    }

    @Author(names = {"Akbar"})
    @Module(name = "RefundConfiguration")
    @Tribe(name = "Ignis")
    @Vertical(name = "TTD")
    @Test
    public void testIgnisSetOrderAsRefundableEventCancelledReason() throws Exception {
        Map<String, String> param = getParam("testIgnisSetOrderAsRefundableEventCancelledReason");
        BaseTest.paramsThreadLocal.set(param);

        String b2cUsername = param.get("b2cUsername");
        String b2cPassword = param.get("b2cPassword");
        String otp = param.get("otp");

        String productName = param.get("productName");
        String packageName = param.get("packageName");

        String ignisUsername = param.get("ignisUsername");
        String ignisPassword = param.get("ignisPassword");

        String partnerName = param.get("partnerName");

        String refundReason = param.get("refundReason");
        String refundDeadlineDateRange = param.get("refundDeadlineDateRange");
        String refundDeadlineTime = param.get("refundDeadlineTime");
        String refundPercentage = param.get("refundPercentage");

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        TtdLandingPageWeb<?> ttdLandingPageWeb = TtdLandingPageWeb.getInstance(session);
        TtdSearchResultPageWeb<?> ttdSearchResultPageWeb = TtdSearchResultPageWeb.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);
        TtdBookingFormPageWeb<?> ttdBookingFormPageWeb = TtdBookingFormPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);

        IgnisLoginPageWeb<?> ignisLoginPageWeb = IgnisLoginPageWeb.getInstance(session);
        IgnisSelectPartnerPageWeb<?> ignisSelectPartnerPageWeb = IgnisSelectPartnerPageWeb.getInstance(session);
        IgnisOrderReportPageWeb<?> ignisOrderReportPageWeb = IgnisOrderReportPageWeb.getInstance(session);
        IgnisRefundConfigPageWeb<?> ignisRefundConfigurationPageWeb = IgnisRefundConfigPageWeb.getInstance(session);
        IgnisProgressLogPageWeb<?> ignisProgressLogPageWeb = IgnisProgressLogPageWeb.getInstance(session);

        step("Open home page");
        homePage.goToHomePage();

        step("Open as logged in user");
        loginPage.loginIfNotWithSameEmail(b2cUsername, b2cPassword, otp, 1);

        step("Select IDR as currency");
        homePage.selectCurrency("IDR");

        step("Go to TTD Landing Page");
        ttdLandingPageWeb.goToTtdLandingPage();

        step("Search " + productName);
        ttdLandingPageWeb.inputSearchKeyword(productName);
        ttdLandingPageWeb.search();
        String nextWeekend = CoreFunctions.getNearestNextWeekend();

        step("Filter date to next weekend " + nextWeekend);
        int date = Integer.parseInt(nextWeekend.split(" ")[0]);
        ttdSearchResultPageWeb.filterDate(date);

        step("Verify product in search result");
        List<String> productNames = ttdSearchResultPageWeb.getProductTitles();
        Assertion.assertThat("Product " + productName + " is in search result", productNames, hasItem(productName));

        step("Open product");
        ttdSearchResultPageWeb.chooseProductSrp(false, productName, 1);
        ttdSearchResultPageWeb.switchToNewTab();

        step("Open package " + packageName);
        ttdProductDetailPageWeb.chooseTicket(packageName);

        step("Continue to booking form");
        ttdProductDetailPageWeb.setTicketQty(1, 0);
        ttdProductDetailPageWeb.clickBookButton();

        step("Completing booking form data");
        ttdBookingFormPageWeb.fillContactOrderDetail(true);
        ttdBookingFormPageWeb.fillGuestDetail();

        step("Continue to payment page");
        ttdBookingFormPageWeb.continueToPaymentPage();

        Assertion.assertThat("Success redirected to payment page", paymentPage.isPaymentPageLoaded(), is(true));
        String orderId = paymentPage.getOrderId();

        step("Open all payment method");
        paymentPage.clickOnSelectPaymentMethod();

        step("Select the payment Method");
        paymentPage.selectPaymentMethod("Sakuku");

        step("Submit the payment method");
        paymentPage.clickOnPaymentButton();
        Assertion.assertThat("Payment successful page is visible", paymentPage.isPaymentSuccessfulVisible(), is(true));

        step("Go to Ignis dashboard and login");
        ignisLoginPageWeb.loginAsSpecificUser(ignisUsername, ignisPassword);

        step("Select Partner");
        ignisSelectPartnerPageWeb.selectPartner(partnerName);

        step("Get order detail id from newly created b2c order");
        ignisOrderReportPageWeb.goToPage();
        ignisOrderReportPageWeb.setSearchByOrder();
        ignisOrderReportPageWeb.inputOrderId(orderId);
        String orderDetailId = ignisOrderReportPageWeb.getOrderDetailId(orderId);

        step("Request to change the order as refundable");
        ignisOrderReportPageWeb.clickOrderConfigButton();
        ignisOrderReportPageWeb.clickOrderConfigOptions("Pengaturan Refund/Refund Configuration");

        step("Create and upload new CSV file");
        ignisRefundConfigurationPageWeb.createAndUploadCSV(orderDetailId);

        step("Fill all form for the request");
        ignisRefundConfigurationPageWeb.chooseRefundReason(refundReason);
        ignisRefundConfigurationPageWeb.chooseRefundType("Event dibatalkan/Event Canceled");
        ignisRefundConfigurationPageWeb.fillRefundSubmissionDeadline(refundDeadlineDateRange, refundDeadlineTime);
        ignisRefundConfigurationPageWeb.fillRefundPercentage(refundPercentage);

        step("Submit request");
        ignisRefundConfigurationPageWeb.submitRequest();
        ignisRefundConfigurationPageWeb.checkProgressLog();

        step("Check the progress log");
        boolean isProgressLogValid = ignisProgressLogPageWeb.verifyFirstRow("Set as Refundable/Atur Sebagai Refundable", orderDetailId);

        step("Check the actual refundable status for the same order id");
        ignisOrderReportPageWeb.goToPage();
        ignisOrderReportPageWeb.setSearchByOrder();
        ignisOrderReportPageWeb.inputOrderId(orderId);
        ignisOrderReportPageWeb.clickRowByIndex(1);
        boolean isOrderRefundable = ignisOrderReportPageWeb.isOrderRefundable();

        step("Assertion");
        Assertion.assertThat("Progress Log is valid", isProgressLogValid, is(true));
        Assertion.assertThat("Order is refundable", isOrderRefundable, is(true));
    }

    @Author(names = {"Akbar"})
    @Module(name = "RefundConfiguration")
    @Tribe(name = "Ignis")
    @Vertical(name = "TTD")
    @Test
    public void testIgnisSetOrderAsRefundableRescheduledReason() throws Exception {
        Map<String, String> param = getParam("testIgnisSetOrderAsRefundableRescheduledReason");
        BaseTest.paramsThreadLocal.set(param);

        String b2cUsername = param.get("b2cUsername");
        String b2cPassword = param.get("b2cPassword");
        String otp = param.get("otp");

        String productName = param.get("productName");
        String packageName = param.get("packageName");

        String ignisUsername = param.get("ignisUsername");
        String ignisPassword = param.get("ignisPassword");

        String partnerName = param.get("partnerName");

        String refundReason = param.get("refundReason");
        String refundDeadlineDateRange = param.get("refundDeadlineDateRange");
        String refundDeadlineTime = param.get("refundDeadlineTime");
        String refundPercentage = param.get("refundPercentage");

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        TtdLandingPageWeb<?> ttdLandingPageWeb = TtdLandingPageWeb.getInstance(session);
        TtdSearchResultPageWeb<?> ttdSearchResultPageWeb = TtdSearchResultPageWeb.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);
        TtdBookingFormPageWeb<?> ttdBookingFormPageWeb = TtdBookingFormPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);

        IgnisLoginPageWeb<?> ignisLoginPageWeb = IgnisLoginPageWeb.getInstance(session);
        IgnisSelectPartnerPageWeb<?> ignisSelectPartnerPageWeb = IgnisSelectPartnerPageWeb.getInstance(session);
        IgnisOrderReportPageWeb<?> ignisOrderReportPageWeb = IgnisOrderReportPageWeb.getInstance(session);
        IgnisRefundConfigPageWeb<?> ignisRefundConfigurationPageWeb = IgnisRefundConfigPageWeb.getInstance(session);
        IgnisProgressLogPageWeb<?> ignisProgressLogPageWeb = IgnisProgressLogPageWeb.getInstance(session);

        step("Open home page");
        homePage.goToHomePage();

        step("Open as logged in user");
        loginPage.loginIfNotWithSameEmail(b2cUsername, b2cPassword, otp, 1);

        step("Select IDR as currency");
        homePage.selectCurrency("IDR");

        step("Go to TTD Landing Page");
        ttdLandingPageWeb.goToTtdLandingPage();

        step("Search " + productName);
        ttdLandingPageWeb.inputSearchKeyword(productName);
        ttdLandingPageWeb.search();
        String nextWeekend = CoreFunctions.getNearestNextWeekend();

        step("Filter date to next weekend " + nextWeekend);
        int date = Integer.parseInt(nextWeekend.split(" ")[0]);
        ttdSearchResultPageWeb.filterDate(date);

        step("Verify product in search result");
        List<String> productNames = ttdSearchResultPageWeb.getProductTitles();
        Assertion.assertThat("Product " + productName + " is in search result", productNames, hasItem(productName));

        step("Open product");
        ttdSearchResultPageWeb.chooseProductSrp(false, productName, 1);
        ttdSearchResultPageWeb.switchToNewTab();

        step("Open package " + packageName);
        ttdProductDetailPageWeb.chooseTicket(packageName);

        step("Continue to booking form");
        ttdProductDetailPageWeb.setTicketQty(1, 0);
        ttdProductDetailPageWeb.clickBookButton();

        step("Completing booking form data");
        ttdBookingFormPageWeb.fillContactOrderDetail(true);
        ttdBookingFormPageWeb.fillGuestDetail();

        step("Continue to payment page");
        ttdBookingFormPageWeb.continueToPaymentPage();

        Assertion.assertThat("Success redirected to payment page", paymentPage.isPaymentPageLoaded(), is(true));
        String orderId = paymentPage.getOrderId();

        step("Open all payment method");
        paymentPage.clickOnSelectPaymentMethod();

        step("Select the payment Method");
        paymentPage.selectPaymentMethod("Sakuku");

        step("Submit the payment method");
        paymentPage.clickOnPaymentButton();
        Assertion.assertThat("Payment successful page is visible", paymentPage.isPaymentSuccessfulVisible(), is(true));

        step("Go to Ignis dashboard and login");
        ignisLoginPageWeb.loginAsSpecificUser(ignisUsername, ignisPassword);

        step("Select Partner");
        ignisSelectPartnerPageWeb.selectPartner(partnerName);

        step("Get order detail id from newly created b2c order");
        ignisOrderReportPageWeb.goToPage();
        ignisOrderReportPageWeb.setSearchByOrder();
        ignisOrderReportPageWeb.inputOrderId(orderId);
        String orderDetailId = ignisOrderReportPageWeb.getOrderDetailId(orderId);

        step("Request to change the order as refundable");
        ignisOrderReportPageWeb.clickOrderConfigButton();
        ignisOrderReportPageWeb.clickOrderConfigOptions("Pengaturan Refund/Refund Configuration");

        step("Create and upload new CSV file");
        ignisRefundConfigurationPageWeb.createAndUploadCSV(orderDetailId);

        step("Fill all form for the request");
        ignisRefundConfigurationPageWeb.chooseRefundReason(refundReason);
        ignisRefundConfigurationPageWeb.chooseRefundType("Reschedule/Reschedule");
        ignisRefundConfigurationPageWeb.fillRefundSubmissionDeadline(refundDeadlineDateRange, refundDeadlineTime);
        ignisRefundConfigurationPageWeb.fillExtendValidity(refundDeadlineDateRange, refundDeadlineTime);
        ignisRefundConfigurationPageWeb.fillRefundPercentage(refundPercentage);

        step("Submit request");
        ignisRefundConfigurationPageWeb.submitRequest();
        ignisRefundConfigurationPageWeb.checkProgressLog();

        step("Check the progress log");
        boolean isProgressLogValid = ignisProgressLogPageWeb.verifyFirstRow("Set as Refundable/Atur Sebagai Refundable", orderDetailId);

        step("Check the actual refundable status for the same order id");
        ignisOrderReportPageWeb.goToPage();
        ignisOrderReportPageWeb.setSearchByOrder();
        ignisOrderReportPageWeb.inputOrderId(orderId);
        ignisOrderReportPageWeb.clickRowByIndex(1);
        boolean isOrderRefundable = ignisOrderReportPageWeb.isOrderRefundable();

        step("Assertion");
        Assertion.assertThat("Progress Log is valid", isProgressLogValid, is(true));
        Assertion.assertThat("Order is refundable", isOrderRefundable, is(true));
    }

    @Author(names = {"Rizki"})
    @Tribe(name = "Ignis")
    @Module(name = "OrderReport")
    @Vertical(name = "TTD")
    @Test
    public void testIgnisFilterOrderReportByVisitDate() {
        Map<String, String> param = getParam("testIgnisFilterOrderReportByVisitDate");
        BaseTest.paramsThreadLocal.set(param);

        String username = param.get("username");
        String password = param.get("password");

        String partnerName = param.get("partnerName");
        String productName = param.get("productName");

        Session<?> session = BaseTest.sessionThreadLocal.get();
        IgnisLoginPageWeb<?> ignisLoginPageWeb = IgnisLoginPageWeb.getInstance(session);
        IgnisSelectPartnerPageWeb<?> ignisSelectPartnerPageWeb = IgnisSelectPartnerPageWeb.getInstance(session);
        IgnisOrderReportPageWeb<?> ignisOrderReportPageWeb = IgnisOrderReportPageWeb.getInstance(session);

        step("Login to Ignis");
        ignisLoginPageWeb.loginAsSpecificUser(username, password);

        step("Select any partners");
        ignisSelectPartnerPageWeb.selectPartner(partnerName);

        step("Go to Order Report page");
        ignisOrderReportPageWeb.goToPage();

        step("Select product");
        ignisOrderReportPageWeb.setProductFilter(productName);

        step("Set the filter to By Visit Date");
        ignisOrderReportPageWeb.selectFilterBy("visit");

        step("Click Apply Button");
        ignisOrderReportPageWeb.clickApplyFilter();

        step("Assertions");
        Assertion.assertThat("The data shown on the order report list should be based on the visit date", ignisOrderReportPageWeb.verifyVisitDateWithFilterDate(), is(true));
    }

    @Author(names = {"Rizki"})
    @Tribe(name = "Ignis")
    @Module(name = "OrderReport")
    @Vertical(name = "TTD")
    @Test
    public void testIgnisFilterOrderReportBySettlementDate() {
        Map<String, String> param = getParam("testIgnisFilterOrderReportBySettlementDate");
        BaseTest.paramsThreadLocal.set(param);

        String username = param.get("username");
        String password = param.get("password");

        String partnerName = param.get("partnerName");
        String productName = param.get("productName");

        Session<?> session = BaseTest.sessionThreadLocal.get();
        IgnisLoginPageWeb<?> ignisLoginPageWeb = IgnisLoginPageWeb.getInstance(session);
        IgnisSelectPartnerPageWeb<?> ignisSelectPartnerPageWeb = IgnisSelectPartnerPageWeb.getInstance(session);
        IgnisOrderReportPageWeb<?> ignisOrderReportPageWeb = IgnisOrderReportPageWeb.getInstance(session);

        step("Login to Ignis");
        ignisLoginPageWeb.loginAsSpecificUser(username, password);

        step("Select any partners");
        ignisSelectPartnerPageWeb.selectPartner(partnerName);

        step("Go to Order Report page");
        ignisOrderReportPageWeb.goToPage();

        step("Select product");
        ignisOrderReportPageWeb.setProductFilter(productName);

        step("Set the filter to By Settlement Date");
        ignisOrderReportPageWeb.selectFilterBy("settlement");

        step("Click Apply Button");
        ignisOrderReportPageWeb.clickApplyFilter();

        step("Assertions");
        Assertion.assertThat("The data shown on the order report list should be based on the due date", ignisOrderReportPageWeb.verifySettlementDateWithFilterDate(), is(true));
    }

    @Author(names = {"Akbar"})
    @Module(name = "ThingsToDoList")
    @Tribe(name = "Ignis")
    @Vertical(name = "TTD")
    @Test
    public void testIgnisEditPackagePricingConfig() {
        Map<String, String> param = getParam("testIgnisEditPackagePricingConfig");
        BaseTest.paramsThreadLocal.set(param);
        String username = param.get("username");
        String password = param.get("password");

        String partnerName = param.get("partnerName");
        String productName = param.get("productName");
        String packageName = param.get("packageName");

        String updateOnSiteTicket = param.get("updateOnSiteTicket");
        String updateAmountPaidByCustomer = param.get("updateAmountPaidByCustomer");
        String randomAmountAdded = param.get("randomAmountAdded");

        Session<?> session = BaseTest.sessionThreadLocal.get();
        IgnisLoginPageWeb<?> ignisLoginPageWeb = IgnisLoginPageWeb.getInstance(session);
        IgnisSelectPartnerPageWeb<?> ignisSelectPartnerPageWeb = IgnisSelectPartnerPageWeb.getInstance(session);
        IgnisPartnerPageWeb<?> ignisPartnerPageWeb = IgnisPartnerPageWeb.getInstance(session);
        IgnisThingsToDoListPageWeb<?> ignisThingsToDoListPageWeb = IgnisThingsToDoListPageWeb.getInstance(session);

        step("Go to Ignis dashboard and login");
        ignisLoginPageWeb.loginAsSpecificUser(username, password);

        step("Select Partner");
        ignisSelectPartnerPageWeb.selectPartner(partnerName);

        step("Go to things to do list page");
        ignisPartnerPageWeb.clickSeeAllThingToDo();

        step("Search for a product and expand the section");
        ignisThingsToDoListPageWeb.searchProduct(productName);
        ignisThingsToDoListPageWeb.openProductPackageSection(productName);
        ignisThingsToDoListPageWeb.clickPackageOption(packageName, "pengaturan harga/pricing configuration");

        step("Clone the package to the same product");
        String finalUpdateOnSiteTicketPrice = ignisThingsToDoListPageWeb.editOnSiteTicketPrice(updateOnSiteTicket, randomAmountAdded);
        String finalUpdateAmountPaidByCustomer = ignisThingsToDoListPageWeb.editAmountPaidByCustomer(updateAmountPaidByCustomer, randomAmountAdded);
        ignisThingsToDoListPageWeb.submitEditPricingConfiguration();

        step("Extract data for assertion");
        boolean isSuccessSnackBarVisible = ignisThingsToDoListPageWeb.isSuccessSnackBarVisible();
        ignisThingsToDoListPageWeb.clickPackageOption(packageName, "pengaturan harga/pricing configuration");
        String displayedOnSiteTicketPrice = ignisThingsToDoListPageWeb.getOnSiteTicketPrice();
        String displayedAmountPaidByCustomer = ignisThingsToDoListPageWeb.getAmountPaidByCustomer();

        step("Assertions");
        Assertion.assertThat("Success snackbar is visible after clicking submit edit", isSuccessSnackBarVisible, is(true));
        Assertion.assertThat("Displayed on site ticket price is updated", displayedOnSiteTicketPrice, is(finalUpdateOnSiteTicketPrice));
        Assertion.assertThat("Displayed amount paid by customer is updated", displayedAmountPaidByCustomer, is(finalUpdateAmountPaidByCustomer));
    }

    @Author(names = {"Akbar", "Annisa"})
    @Module(name = "ProductSetting")
    @Tribe(name = "CMS")
    @Vertical(name = "TTD")
    @Test
    public void testCMSCreateSeatMap() {
        Map<String, String> param = getParam("testCMSCreateSeatMap");
        BaseTest.paramsThreadLocal.set(param);
        String username = param.get("username");
        String password = param.get("password");

        String seatMapName = param.get("seatMapName") + "_" + System.currentTimeMillis();
        String categoryName = param.get("categoryName");
        String rowHeightAmount = param.get("rowHeightAmount");
        String columnWidthAmount = param.get("columnWidthAmount");

        Session<?> session = BaseTest.sessionThreadLocal.get();
        CMSLoginPageWeb<?> cmsLoginPageWeb = CMSLoginPageWeb.getInstance(session);
        CMSProductSettingSeatMapPageWeb<?> cmsProductSettingSeatMapPageWeb = CMSProductSettingSeatMapPageWeb.getInstance(session);
        CMSProductSettingSeatMapFormPageWeb<?> cmsProductSettingSeatMapFormPageWeb = CMSProductSettingSeatMapFormPageWeb.getInstance(session);

        step("Go to CMS dashboard and login");
        cmsLoginPageWeb.loginAsSpecificUser(username, password);

        step("Go to product setting > seat map page and click add seat map");
        cmsProductSettingSeatMapPageWeb.goToPage();
        cmsProductSettingSeatMapPageWeb.clickCreateButton();

        step("Input seat map name and click add category");
        cmsProductSettingSeatMapFormPageWeb.inputSeatMapName(seatMapName);
        cmsProductSettingSeatMapFormPageWeb.clickAddCategory();

        step("Input category name");
        cmsProductSettingSeatMapFormPageWeb.inputCategoryName(categoryName);
        cmsProductSettingSeatMapFormPageWeb.clickAddSectionButtonPopup();

        step("Setup the height and width");
        cmsProductSettingSeatMapFormPageWeb.inputRowHeight(rowHeightAmount);
        cmsProductSettingSeatMapFormPageWeb.inputColumnWidth(columnWidthAmount);
        cmsProductSettingSeatMapFormPageWeb.clickRefreshGrid();

        step("Submit");
        cmsProductSettingSeatMapFormPageWeb.clickSubmitButton();

        step("Extract Data");
        boolean isSuccessNotifBarVisible = cmsProductSettingSeatMapPageWeb.isSuccessNotifBarVisible();
        boolean isNewlyCreatedSeatMapExist = cmsProductSettingSeatMapPageWeb.isNewlyCreatedSeatMapExist(seatMapName);

        step("Post-test : Delete the newly created seat map");
        cmsProductSettingSeatMapPageWeb.deleteSeatMap(seatMapName);

        step("Assertions");
        Assertion.assertThat("Success notif bar is visible after clicking submit", isSuccessNotifBarVisible, is(true));
        Assertion.assertThat("Newly created seat map is visible", isNewlyCreatedSeatMapExist, is(true));
    }

    @Author(names = {"Ram Pratap Singh", "Annisa"})
    @Module(name = "ProductList")
    @Tribe(name = "CMS")
    @Vertical(name = "TTD")
    @Test
    public void testUserCanCreateScheduleForPackage() {
        Map<String, String> param = getParam("testUserCanCreateScheduleForPackage");
        BaseTest.paramsThreadLocal.set(param);

        String cmsUsername = param.get("cmsUsername");
        String cmsPassword = param.get("cmsPassword");
        String productName = param.get("productName");

        Session<?> session = BaseTest.sessionThreadLocal.get();

        CMSLoginPageWeb<?> cmsLoginPageWeb = CMSLoginPageWeb.getInstance(session);
        CMSInventoryManagementProductListPageWeb<?> cmsInventoryManagementProductListPageWeb = CMSInventoryManagementProductListPageWeb.getInstance(session);

        step("Go to CMS login page and login");
        cmsLoginPageWeb.loginAsSpecificUser(cmsUsername, cmsPassword);

        step("Go to Product List page");
        cmsInventoryManagementProductListPageWeb.goToPage();

        step("Search for the product");
        cmsInventoryManagementProductListPageWeb.inputSearchBar(productName);
        cmsInventoryManagementProductListPageWeb.pressSearchButton();

        step("User goes to Package section");
        cmsInventoryManagementProductListPageWeb.goToPackage();

        step("User goes to Package schedule section");
        cmsInventoryManagementProductListPageWeb.goToPackageSchedule();

        step("User adds a schedule for the package");
        cmsInventoryManagementProductListPageWeb.addSchedule();

        step("User verifies that the schedule is added");
        boolean isScheduleAdded = cmsInventoryManagementProductListPageWeb.isScheduleAdded();
        Assertion.assertThat("Schedule is added successfully", isScheduleAdded, is(true));

        step("User deletes the schedule");
        cmsInventoryManagementProductListPageWeb.deleteSchedule();
    }

    @Author(names = {"Ram Pratap Singh", "Annisa"})
    @Module(name = "ProductList")
    @Tribe(name = "CMS")
    @Vertical(name = "TTD")
    @Test
    public void testUserCanDeleteScheduleForPackage() {
        Map<String, String> param = getParam("testUserCanDeleteScheduleForPackage");
        BaseTest.paramsThreadLocal.set(param);

        String cmsUsername = param.get("cmsUsername");
        String cmsPassword = param.get("cmsPassword");
        String productName = param.get("productName");

        Session<?> session = BaseTest.sessionThreadLocal.get();

        CMSLoginPageWeb<?> cmsLoginPageWeb = CMSLoginPageWeb.getInstance(session);
        CMSInventoryManagementProductListPageWeb<?> cmsInventoryManagementProductListPageWeb = CMSInventoryManagementProductListPageWeb.getInstance(session);

        step("Go to CMS login page and login");
        cmsLoginPageWeb.loginAsSpecificUser(cmsUsername, cmsPassword);

        step("Go to Product List page");
        cmsInventoryManagementProductListPageWeb.goToPage();

        step("Search for the product");
        cmsInventoryManagementProductListPageWeb.inputSearchBar(productName);
        cmsInventoryManagementProductListPageWeb.pressSearchButton();

        step("User goes to Package section");
        cmsInventoryManagementProductListPageWeb.goToPackage();

        step("User goes to Package schedule section");
        cmsInventoryManagementProductListPageWeb.goToPackageSchedule();

        step("User adds a schedule for the package");
        cmsInventoryManagementProductListPageWeb.addSchedule();

        step("User verifies that the schedule is added");
        boolean isScheduleAdded = cmsInventoryManagementProductListPageWeb.isScheduleAdded();
        Assertion.assertThat("Schedule is added successfully", isScheduleAdded, is(true));

        step("User deletes the schedule");
        cmsInventoryManagementProductListPageWeb.deleteSchedule();

        step("User verifies that the schedule is deleted");
        boolean isScheduleDeleted = cmsInventoryManagementProductListPageWeb.isScheduleDeleted();
        Assertion.assertThat("Schedule is deleted successfully", isScheduleDeleted, is(true));
    }

    @Author(names = {"Ram Pratap Singh"})
    @Module(name = "ProductList")
    @Tribe(name = "CMS")
    @Vertical(name = "TTD")
    @Test
    public void testUserCanSpecifyQuotaForProductInInventoryManagement() {
        Map<String, String> param = getParam("testUserCanSpecifyQuotaForProductInInventoryManagement");
        BaseTest.paramsThreadLocal.set(param);

        String cmsUsername = param.get("cmsUsername");
        String cmsPassword = param.get("cmsPassword");
        String productName = param.get("productName");
        String quota = param.get("quota");

        Session<?> session = BaseTest.sessionThreadLocal.get();

        CMSLoginPageWeb<?> cmsLoginPageWeb = CMSLoginPageWeb.getInstance(session);
        CMSInventoryManagementProductListPageWeb<?> cmsInventoryManagementProductListPageWeb = CMSInventoryManagementProductListPageWeb.getInstance(session);

        step("Go to CMS login page and login");
        cmsLoginPageWeb.loginAsSpecificUser(cmsUsername, cmsPassword);

        step("Go to Product List page");
        cmsInventoryManagementProductListPageWeb.goToPage();

        step("Search for the product");
        cmsInventoryManagementProductListPageWeb.inputSearchBar(productName);
        cmsInventoryManagementProductListPageWeb.pressSearchButton();

        step("User goes to Package section");
        cmsInventoryManagementProductListPageWeb.goToPackage();

        step("User clicks on edit package button");
        cmsInventoryManagementProductListPageWeb.editPackageButton();

        step("User goes to Package quota section");
        cmsInventoryManagementProductListPageWeb.enterQuota(quota);

        step("User goes to Package section");
        cmsInventoryManagementProductListPageWeb.goToPackage();

        step("User clicks on edit package button");
        cmsInventoryManagementProductListPageWeb.editPackageButton();

        step("User verifies that the quota is set");
        boolean isQuotaSet = cmsInventoryManagementProductListPageWeb.isQuotaUpdated(quota);
        Assertion.assertThat("Quota is set successfully", isQuotaSet, is(true));
    }

    @Author(names = {"Rizki Maulana"})
    @Module(name = "OrderReport")
    @Tribe(name = "Ignis")
    @Vertical(name = "TTD")
    @Test
    public void testIgnisExportTicketReportXLSWithFilters() throws IOException {
        Map<String, String> param = getParam("testIgnisExportTicketReportXLSWithFilters");
        BaseTest.paramsThreadLocal.set(param);

        String username = param.get("username");
        String password = param.get("password");

        String partnerName = param.get("partnerName");
        String productName = param.get("productName");
        String packageName = param.get("packageName");
        String orderConfigOption = param.get("orderConfigOption");
        String exportOption = param.get("exportOption");
        String filterBy = param.get("filterBy");
        String startDate = param.get("startDate");
        String endDate = param.get("endDate");

        Session<?> session = BaseTest.sessionThreadLocal.get();

        IgnisLoginPageWeb<?> ignisLoginPageWeb = IgnisLoginPageWeb.getInstance(session);
        IgnisSelectPartnerPageWeb<?> ignisSelectPartnerPageWeb = IgnisSelectPartnerPageWeb.getInstance(session);
        IgnisOrderReportPageWeb<?> ignisOrderReportPageWeb = IgnisOrderReportPageWeb.getInstance(session);
        IgnisProgressLogPageWeb<?> ignisProgressLogPageWeb = IgnisProgressLogPageWeb.getInstance(session);

        step("Login to Ignis");
        ignisLoginPageWeb.loginAsSpecificUser(username, password);

        step("Select any Partners");
        ignisSelectPartnerPageWeb.selectPartner(partnerName);

        step("Go to Order Report page (set the radio buttons and field filters)");
        ignisOrderReportPageWeb.goToPage();
        ignisOrderReportPageWeb.setProductFilter(productName);
        ignisOrderReportPageWeb.setPackageFilter(packageName);
        ignisOrderReportPageWeb.selectFilterBy(filterBy);
        ignisOrderReportPageWeb.setDateRangeFilterWithDateRange(startDate, endDate);

        step("Click Apply button");
        ignisOrderReportPageWeb.clickApplyFilter();

        if (!ignisOrderReportPageWeb.checkIfTableResultIsEmpty()) {
            step("Click Order Configurations, select Export to .XLS");
            ignisOrderReportPageWeb.clickOrderConfigButton();
            ignisOrderReportPageWeb.clickOrderConfigOptions(orderConfigOption);

            step("On the popup, select Order ID radio button");
            ignisOrderReportPageWeb.selectExportRadioButton(exportOption);

            step("Click Export Now");
            ignisOrderReportPageWeb.clickExportNowButton();

            step("The exported order report by order id should be based on the applied filters");
            ignisProgressLogPageWeb.downloadFirstRowReport();
            Assertion.assertThat("Verify Product Filter", ignisProgressLogPageWeb.verifyExactValueForExportedXSL(productName, 2), is(true));
            Assertion.assertThat("Verify Package Filter", ignisProgressLogPageWeb.verifyExactValueForExportedXSL(packageName, 3), is(true));
        }
    }

    @Author(names = {"Rizki Maulana"})
    @Module(name = "OrderReport")
    @Tribe(name = "Ignis")
    @Vertical(name = "TTD")
    @Test
    public void testIgnisExportOrderReportXLSWithFilters() throws IOException {
        Map<String, String> param = getParam("testIgnisExportOrderReportXLSWithFilters");
        BaseTest.paramsThreadLocal.set(param);

        String username = param.get("username");
        String password = param.get("password");

        String partnerName = param.get("partnerName");
        String productName = param.get("productName");
        String packageName = param.get("packageName");
        String orderConfigOption = param.get("orderConfigOption");
        String exportOption = param.get("exportOption");
        String filterBy = param.get("filterBy");
        String startDate = param.get("startDate");
        String endDate = param.get("endDate");

        Session<?> session = BaseTest.sessionThreadLocal.get();

        IgnisLoginPageWeb<?> ignisLoginPageWeb = IgnisLoginPageWeb.getInstance(session);
        IgnisSelectPartnerPageWeb<?> ignisSelectPartnerPageWeb = IgnisSelectPartnerPageWeb.getInstance(session);
        IgnisOrderReportPageWeb<?> ignisOrderReportPageWeb = IgnisOrderReportPageWeb.getInstance(session);
        IgnisProgressLogPageWeb<?> ignisProgressLogPageWeb = IgnisProgressLogPageWeb.getInstance(session);

        step("Login to Ignis");
        ignisLoginPageWeb.loginAsSpecificUser(username, password);

        step("Select any Partners");
        ignisSelectPartnerPageWeb.selectPartner(partnerName);

        step("Go to Order Report page (set the radio buttons and field filters)");
        ignisOrderReportPageWeb.goToPage();
        ignisOrderReportPageWeb.setProductFilter(productName);
        ignisOrderReportPageWeb.setPackageFilter(packageName);
        ignisOrderReportPageWeb.selectFilterBy(filterBy);
        ignisOrderReportPageWeb.setDateRangeFilterWithDateRange(startDate, endDate);

        step("Click Apply button");
        ignisOrderReportPageWeb.clickApplyFilter();

        if (!ignisOrderReportPageWeb.checkIfTableResultIsEmpty()) {
            step("Click Order Configurations, select Export to .XLS");
            ignisOrderReportPageWeb.clickOrderConfigButton();
            ignisOrderReportPageWeb.clickOrderConfigOptions(orderConfigOption);

            step("On the popup, select Order ID radio button");
            ignisOrderReportPageWeb.selectExportRadioButton(exportOption);

            step("Click Export Now");
            ignisOrderReportPageWeb.clickExportNowButton();

            step("The exported order report by order id should be based on the applied filters");
            ignisProgressLogPageWeb.downloadFirstRowReport();
            Assertion.assertThat("Verify Product Filter", ignisProgressLogPageWeb.verifyExactValueForExportedXSL(productName, 9), is(true));
            Assertion.assertThat("Verify Package Filter", ignisProgressLogPageWeb.verifyExactValueForExportedXSL(packageName, 10), is(true));
            Assertion.assertThat("Verify Date Range Filter", ignisProgressLogPageWeb.verifyDateRangeForExportedXSL(startDate, endDate, filterBy), is(true));
        }
    }

    @Author(names = {"Akbar"})
    @Module(name = "InventoryManagement")
    @Tribe(name = "CMS")
    @Vertical(name = "TTD")
    @Test
    public void testCMSCheckScheduleWithSeatMap() {
        Map<String, String> param = getParam("testCMSCheckScheduleWithSeatMap");
        BaseTest.paramsThreadLocal.set(param);
        String username = param.get("username");
        String password = param.get("password");

        String productId = param.get("productId");
        String packageName = param.get("packageName");

        Session<?> session = BaseTest.sessionThreadLocal.get();
        CMSLoginPageWeb<?> cmsLoginPageWeb = CMSLoginPageWeb.getInstance(session);
        CMSProductManagementEditProductPageWeb<?> cmsProductManagementEditProductPageWeb = CMSProductManagementEditProductPageWeb.getInstance(session);
        CMSProductManagementSchedulePackagePageWeb<?> cmsProductManagementSchedulePackagePageWeb = CMSProductManagementSchedulePackagePageWeb.getInstance(session);
        CMSProductManagementSchedulePackageSeatMapPageWeb<?> cmsProductManagementSchedulePackageSeatMapPageWeb = CMSProductManagementSchedulePackageSeatMapPageWeb.getInstance(session);

        step("Go to CMS dashboard and login");
        cmsLoginPageWeb.loginAsSpecificUser(username, password);

        step("Go to edit product page based on productId given on testdata");
        cmsProductManagementEditProductPageWeb.goToPage(productId);

        step("Click schedule on specific package");
        cmsProductManagementEditProductPageWeb.clickPackageSchedule(packageName);

        step("Click seat map button on the first schedule");
        cmsProductManagementSchedulePackagePageWeb.changeWindowPointing();
        cmsProductManagementSchedulePackagePageWeb.clickSeatMapByRowIndex(1);

        step("Check if seatmap configuration is loaded or not");
        boolean isSeatMapConfigLoaded = cmsProductManagementSchedulePackageSeatMapPageWeb.isSeatMapConfigLoaded();

        step("Assertions");
        Assertion.assertThat("Seat map config is loaded inside the page", isSeatMapConfigLoaded, is(true));
    }

    @Author(names = {"Akbar"})
    @Module(name = "InventoryManagement")
    @Tribe(name = "CMS")
    @Vertical(name = "TTD")
    @Test
    public void testCMSCheckSeatConfigAfterBooking() {
        Map<String, String> param = getParam("testCMSCheckSeatConfigAfterBooking");
        BaseTest.paramsThreadLocal.set(param);
        String ignisUsername = param.get("ignisUsername");
        String ignisPassword = param.get("ignisPassword");

        String cmsUsername = param.get("cmsUsername");
        String cmsPassword = param.get("cmsPassword");

        String partnerName = param.get("partnerName");
        String productName = param.get("productName");
        String productId = param.get("productId");
        String packageName = param.get("packageName");

        int ticketQty = Integer.parseInt(param.get("ticketQty"));
        String rangeBookDate = param.get("rangeBookDate");

        Session<?> session = BaseTest.sessionThreadLocal.get();

        IgnisLoginPageWeb<?> ignisLoginPageWeb = IgnisLoginPageWeb.getInstance(session);
        IgnisSelectPartnerPageWeb<?> ignisSelectPartnerPageWeb = IgnisSelectPartnerPageWeb.getInstance(session);
        IgnisPartnerBookingPageWeb<?> ignisPartnerBookingPageWeb = IgnisPartnerBookingPageWeb.getInstance(session);
        IgnisPartnerBookingFormPageWeb<?> ignisPartnerBookingFormPageWeb = IgnisPartnerBookingFormPageWeb.getInstance(session);

        CMSLoginPageWeb<?> cmsLoginPageWeb = CMSLoginPageWeb.getInstance(session);
        CMSProductManagementEditProductPageWeb<?> cmsProductManagementEditProductPageWeb = CMSProductManagementEditProductPageWeb.getInstance(session);
        CMSProductManagementSchedulePackagePageWeb<?> cmsProductManagementSchedulePackagePageWeb = CMSProductManagementSchedulePackagePageWeb.getInstance(session);
        CMSProductManagementSchedulePackageSeatMapPageWeb<?> cmsProductManagementSchedulePackageSeatMapPageWeb = CMSProductManagementSchedulePackageSeatMapPageWeb.getInstance(session);

        step("Go to Ignis dashboard and login");
        ignisLoginPageWeb.loginAsSpecificUser(ignisUsername, ignisPassword);

        step("Select Partner");
        ignisSelectPartnerPageWeb.selectPartner(partnerName);

        step("Do partner booking for the same product and package");
        ignisPartnerBookingPageWeb.goToPage();
        Map<String, String> bookingData = ignisPartnerBookingPageWeb.createNewPartnerBooking(productName, packageName, ticketQty, rangeBookDate);

        step("Fill and submit the booking form");
        Map<String, Map<String, String>> bookingFormData = ignisPartnerBookingFormPageWeb.fillBookingForm(param);
        ignisPartnerBookingFormPageWeb.choosePaymentMethod("internal");
        ignisPartnerBookingFormPageWeb.submitBook();

        step("Go to CMS dashboard and login");
        ignisLoginPageWeb.logoutIgnis();
        cmsLoginPageWeb.loginAsSpecificUser(cmsUsername, cmsPassword);

        step("Go to edit product page based on productId given on testdata");
        cmsProductManagementEditProductPageWeb.goToPage(productId);

        step("Click schedule on specific package");
        cmsProductManagementEditProductPageWeb.clickPackageSchedule(packageName);

        step("Click seat map button on the first schedule");
        cmsProductManagementSchedulePackagePageWeb.changeWindowPointing();
        cmsProductManagementSchedulePackagePageWeb.clickSeatMapByScheduleDate(bookingData.get("date"));

        step("Check if seatmap configuration is loaded or not");
        boolean isSeatMapBooked = cmsProductManagementSchedulePackageSeatMapPageWeb.isSeatMapBooked(bookingFormData.get("seatMap"));

        step("Assertions");
        Assertion.assertThat("Seat map status is booked", isSeatMapBooked, is(true));
    }

    @Author(names = {"Rizki Maulana"})
    @Module(name = "PartnerBooking")
    @Tribe(name = "CMS")
    @Vertical(name = "TTD")
    @Test
    public void testCMSAtLeastOnePartnerWithSettlementEmail() {
        Map<String, String> param = getParam("testCMSAtLeastOnePartnerWithSettlementEmail");
        BaseTest.paramsThreadLocal.set(param);

        String cmsUsername = param.get("cmsUsername");
        String cmsPassword = param.get("cmsPassword");
        String partnerName = param.get("partnerName");

        Session<?> session = BaseTest.sessionThreadLocal.get();

        CMSLoginPageWeb<?> cmsLoginPageWeb = CMSLoginPageWeb.getInstance(session);
        CMSProductSettingPartnerWeb<?> cmsProductSettingPartnerWeb = CMSProductSettingPartnerWeb.getInstance(session);

        step("Login to CMS");
        cmsLoginPageWeb.loginAsSpecificUser(cmsUsername, cmsPassword);

        step("User on CMS choose menu Product Setting > Partner");
        cmsProductSettingPartnerWeb.goToPage();

        step("Select the partner name");
        cmsProductSettingPartnerWeb.setPartnerNameFilter(partnerName);

        step("Go to Edit partner page");
        cmsProductSettingPartnerWeb.clickEditButtonFromRandomTableRow();

        step("Scroll until Partner Config section");
        cmsProductSettingPartnerWeb.scrollUntilPartnerConfigSection();

        step("Ensure that Email success pop up come");
        Assertion.assertThat("Ensure that Email success pop up come", cmsProductSettingPartnerWeb.emailSuccessPop(),is(true)); ;
    }

    @Author(names = {"Akbar"})
    @Module(name = "PartnerBooking")
    @Tribe(name = "Ignis")
    @Vertical(name = "TTD")
    @Test
    public void testCMSPartnerBookingMultiguestSeatmap() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testCMSPartnerBookingMultiguestSeatmap");
        BaseTest.paramsThreadLocal.set(param);
        String ignisUsername = param.get("ignisUsername");
        String ignisPassword = param.get("ignisPassword");

        String partnerName = param.get("partnerName");
        String productName = param.get("productName");
        String packageName = param.get("packageName");

        int ticketQty = Integer.parseInt(param.get("ticketQty"));
        String rangeBookDate = param.get("rangeBookDate");

        String b2cUsername = param.get("b2cUsername");
        String b2cPassword = param.get("b2cPassword");
        String otp = param.get("otp");

        Assertion.assertThat("b2cEmail should be match with contactEmail", param.get("contactEmail"), is(b2cUsername));

        Session<?> session = BaseTest.sessionThreadLocal.get();

        IgnisLoginPageWeb<?> ignisLoginPageWeb = IgnisLoginPageWeb.getInstance(session);
        IgnisSelectPartnerPageWeb<?> ignisSelectPartnerPageWeb = IgnisSelectPartnerPageWeb.getInstance(session);
        IgnisPartnerBookingPageWeb<?> ignisPartnerBookingPageWeb = IgnisPartnerBookingPageWeb.getInstance(session);
        IgnisPartnerBookingFormPageWeb<?> ignisPartnerBookingFormPageWeb = IgnisPartnerBookingFormPageWeb.getInstance(session);

        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        OrderListWeb<?> orderListWeb = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailWeb = OrderDetailWeb.getInstance(session);

        step("Go to Ignis dashboard and login");
        ignisLoginPageWeb.loginAsSpecificUser(ignisUsername, ignisPassword);

        step("Select Partner");
        ignisSelectPartnerPageWeb.selectPartner(partnerName);

        step("Do partner booking for the same product and package");
        ignisPartnerBookingPageWeb.goToPage();
        ignisPartnerBookingPageWeb.createNewPartnerBooking(productName, packageName, ticketQty, rangeBookDate);

        step("Fill and submit the booking form");
        Map<String, Map<String, String>> bookingFormData = ignisPartnerBookingFormPageWeb.fillBookingForm(param);
        ignisPartnerBookingFormPageWeb.choosePaymentMethod("link");
        ignisPartnerBookingFormPageWeb.submitBook();

        step("Check order id for the newly created order using partner booking");
        ignisPartnerBookingPageWeb.waitAndRefresh(5);
        ignisPartnerBookingPageWeb.chooseProductFilter(productName);
        ignisPartnerBookingPageWeb.applyFilter();
        String orderId = ignisPartnerBookingPageWeb.getOrderIdByIndex(1);

        step("Go to B2C and login");
        ignisLoginPageWeb.logoutIgnis();
        homePage.goToHomePage();

        step("Open as logged in user");
        loginPage.loginIfNotWithSameEmail(b2cUsername, b2cPassword, otp, 1);

        step("Go to orderDetail page");
        homePage.goToMyOrderPage();
        orderListWeb.openOrderDetailPage(orderId);
        orderDetailWeb.completePaymentFromOrderGroupPaymentPage();

        step("Complete payment");
        Assertion.assertThat("Success redirected to payment page", paymentPage.isPaymentPageLoaded(), is(true));
        String orderIdPayment = paymentPage.getOrderId();
        Assertion.assertThat("Order ID is matched", orderIdPayment, is(orderId));

        step("Open all payment method");
        String paymentPagePrice = paymentPage.getFinalPrice();
        paymentPage.clickOnSelectPaymentMethod();

        step("Select the payment Method");
        paymentPage.selectPaymentMethod("Sakuku");

        step("Submit the payment method");
        paymentPage.clickOnPaymentButton();
        Assertion.assertThat("Payment successful page is visible", paymentPage.isPaymentSuccessfulVisible(), is(true));

        step("Logout from b2c");
        homePage.goToHomePage();
        loginPage.logoutFromDweb();

        step("Go to Ignis dashboard and login");
        ignisLoginPageWeb.loginAsSpecificUser(ignisUsername, ignisPassword);

        step("Select Partner");
        ignisSelectPartnerPageWeb.selectPartner(partnerName);

        step("Check order details or the newly created order using partner booking");
        ignisPartnerBookingPageWeb.goToPage();
        ignisPartnerBookingPageWeb.waitAndRefresh(5);
        ignisPartnerBookingPageWeb.chooseProductFilter(productName);
        ignisPartnerBookingPageWeb.applyFilter();
        ignisPartnerBookingPageWeb.openOrderDataByIndex(1);

        step("Data extraction");
        String ignisPrice = ignisPartnerBookingPageWeb.getTotalPaymentText().replaceAll("\\D", "");
        boolean isOrderIssued = ignisPartnerBookingPageWeb.isOrderIssued();
        boolean multiguestChecking = ignisPartnerBookingPageWeb.checkOrderDetailMultiguest(ticketQty, bookingFormData.get("guest"));
        boolean seatmapChecking = ignisPartnerBookingPageWeb.checkOrderDetailSeatMap(ticketQty, bookingFormData.get("seatMap"));

        step("Assertion");
        Assertion.assertThat("Price from ignis is matched with b2c final price", paymentPagePrice, is(ignisPrice));
        Assertion.assertThat("Order is issued", isOrderIssued, is(true));
        Assertion.assertThat("Multiguest check is passed", multiguestChecking, is(true));
        Assertion.assertThat("Seatmap is matched", seatmapChecking, is(true));
    }

    @Author(names = {"Akbar"})
    @Module(name = "ManageOrderReport")
    @Tribe(name = "Ignis")
    @Vertical(name = "TTD")
    @Test
    public void testIgnisExtendValidityDate() {
        Map<String, String> param = getParam("testIgnisExtendValidityDate");
        BaseTest.paramsThreadLocal.set(param);

        String username = param.get("username");
        String password = param.get("password");

        String partnerName = param.get("partnerName");
        String orderDetailId = param.get("orderDetailId");
        String dateExtendRange = param.get("dateExtendRange");

        Session<?> session = BaseTest.sessionThreadLocal.get();
        IgnisLoginPageWeb<?> ignisLoginPageWeb = IgnisLoginPageWeb.getInstance(session);
        IgnisSelectPartnerPageWeb<?> ignisSelectPartnerPageWeb = IgnisSelectPartnerPageWeb.getInstance(session);
        IgnisOrderReportPageWeb<?> ignisOrderReportPageWeb = IgnisOrderReportPageWeb.getInstance(session);
        IgnisManageOrderPageWeb<?> ignisManageOrderPageWeb = IgnisManageOrderPageWeb.getInstance(session);
        IgnisProgressLogPageWeb<?> ignisProgressLogPageWeb = IgnisProgressLogPageWeb.getInstance(session);

        step("Login to Ignis");
        ignisLoginPageWeb.loginAsSpecificUser(username, password);

        step("Select any partners");
        ignisSelectPartnerPageWeb.selectPartner(partnerName);

        step("Go to Order Report page");
        ignisOrderReportPageWeb.goToPage();

        step("Click Order Configurations, select Manage Order");
        ignisOrderReportPageWeb.clickOrderConfigButton();
        ignisOrderReportPageWeb.clickOrderConfigOptions("manage order/atur order");

        step("On the popup, select Update Visit Date radio button");
        ignisOrderReportPageWeb.selectUpdateOptionManageOrderPopup("update validity");

        step("Click Continue");
        ignisOrderReportPageWeb.clickContinueOnManageOrderPopup();

        step("On Manage Order page, select product and packages");
        ignisManageOrderPageWeb.setOrderDetailIdFilter(orderDetailId);

        step("Click Search");
        ignisManageOrderPageWeb.clickSearchButton();

        step("Select any order ids to be extended");
        ignisManageOrderPageWeb.checkTableRow(1);

        step("Click Manage Order");
        ignisManageOrderPageWeb.clickManageOrderButton();

        step("Select New Date and Timeslot (if any)");
        ignisManageOrderPageWeb.inputNewValidityDate(dateExtendRange);
        ignisManageOrderPageWeb.clickOkUpdateOrderPopUp();

        step("Click OK");
        String newDateTime = ignisManageOrderPageWeb.getNewUpdatedDate();
        ignisManageOrderPageWeb.clickOkPreviewPopUp();

        step("Click Apply");
        ignisManageOrderPageWeb.clickOkApplyPopUp();

        step("Check Progress Log");
        ignisManageOrderPageWeb.clickGoToProgressLog();
        boolean progressLogCheckResult = ignisProgressLogPageWeb.verifyFirstRow("Masa Berlaku Order/Order Validity", orderDetailId);

        step("Check whether the extended orders can still be redeemed");
        ignisOrderReportPageWeb.goToPage();
        ignisOrderReportPageWeb.setSearchByOrder();
        ignisOrderReportPageWeb.inputOrderId(orderDetailId);

        step("Assertion");
        Assertion.assertThat("Progress log is valid", progressLogCheckResult, is(true));
        Assertion.assertThat(String.format("Order Detail ID %s has been extended successfully", orderDetailId), ignisOrderReportPageWeb.getExtendedValidityDate(orderDetailId), is(newDateTime));
    }

    @Author(names = {"Akbar"})
    @Module(name = "PartnerBooking")
    @Tribe(name = "Ignis")
    @Vertical(name = "TTD")
    @Test
    public void testCMSPartnerBookingNonMultiguestQuestionnaire() {
        Map<String, String> param = getParam("testCMSPartnerBookingNonMultiguestQuestionnaire");
        BaseTest.paramsThreadLocal.set(param);
        String username = param.get("username");
        String password = param.get("password");

        String partnerName = param.get("partnerName");
        String productName = param.get("productName");
        String packageName = param.get("packageName");

        int ticketQty = Integer.parseInt(param.get("ticketQty"));
        String rangeBookDate = param.get("rangeBookDate");

        Session<?> session = BaseTest.sessionThreadLocal.get();

        IgnisLoginPageWeb<?> ignisLoginPageWeb = IgnisLoginPageWeb.getInstance(session);
        IgnisSelectPartnerPageWeb<?> ignisSelectPartnerPageWeb = IgnisSelectPartnerPageWeb.getInstance(session);
        IgnisPartnerBookingPageWeb<?> ignisPartnerBookingPageWeb = IgnisPartnerBookingPageWeb.getInstance(session);
        IgnisPartnerBookingFormPageWeb<?> ignisPartnerBookingFormPageWeb = IgnisPartnerBookingFormPageWeb.getInstance(session);

        step("Go to Ignis dashboard and login");
        ignisLoginPageWeb.loginAsSpecificUser(username, password);

        step("Select Partner");
        ignisSelectPartnerPageWeb.selectPartner(partnerName);

        step("Do partner booking for the same product and package");
        ignisPartnerBookingPageWeb.goToPage();
        ignisPartnerBookingPageWeb.createNewPartnerBooking(productName, packageName, ticketQty, rangeBookDate);

        step("Fill and submit the booking form");
        Map<String, Map<String, String>> bookingFormData = ignisPartnerBookingFormPageWeb.fillBookingForm(param);
        ignisPartnerBookingFormPageWeb.choosePaymentMethod("internal");
        String totalPaymentPartnerBookingForm = ignisPartnerBookingFormPageWeb.getTotalPayment().replaceAll("\\D", "");
        ignisPartnerBookingFormPageWeb.submitBook();

        step("Check order detail for the newly created order using partner booking");
        ignisPartnerBookingPageWeb.waitAndRefresh(5);
        ignisPartnerBookingPageWeb.chooseProductFilter(productName);
        ignisPartnerBookingPageWeb.applyFilter();
        ignisPartnerBookingPageWeb.openOrderDataByIndex(1);

        step("Data extraction");
        String ignisPrice = ignisPartnerBookingPageWeb.getTotalPaymentText().replaceAll("\\D", "");
        boolean isOrderIssued = ignisPartnerBookingPageWeb.isOrderIssued();
        boolean nonMultiguestChecking = ignisPartnerBookingPageWeb.checkOrderDetailNonMultiguest(bookingFormData.get("guest"));
        boolean questionnaireChecking = ignisPartnerBookingPageWeb.checkOrderDetailQuestionnaire(bookingFormData.get("questionnaire"));

        step("Assertion");
        Assertion.assertThat("Price from booking form and order detail is matched", ignisPrice, is(totalPaymentPartnerBookingForm));
        Assertion.assertThat("Order is issued", isOrderIssued, is(true));
        Assertion.assertThat("NonMultiguest check is passed", nonMultiguestChecking, is(true));
        Assertion.assertThat("Questionnaire check is passed", questionnaireChecking, is(true));
    }

    @Author(names = {"Manan Kumar"})
    @Module(name = "InventoryManagement")
    @Tribe(name = "CMS")
    @Vertical(name = "TTD")
    @Test
    public void testUserCanAddNewDateAndVenueToProductSeatMap() {
        Map<String, String> param = getParam("testUserCanAddNewDateAndVenueToProductSeatMap");
        BaseTest.paramsThreadLocal.set(param);

        String cmsUsername = param.get("cmsUsername");
        String cmsPassword = param.get("cmsPassword");
        String productName = param.get("productName");
        String venue = param.get("venue");

        Session<?> session = BaseTest.sessionThreadLocal.get();

        CMSLoginPageWeb<?> cmsLoginPageWeb = CMSLoginPageWeb.getInstance(session);
        CMSInventoryManagementProductListPageWeb<?> cmsInventoryManagementProductListPageWeb = CMSInventoryManagementProductListPageWeb.getInstance(session);

        step("Go to CMS login page and login");
        cmsLoginPageWeb.loginAsSpecificUser(cmsUsername, cmsPassword);

        step("Go to Product List page");
        cmsInventoryManagementProductListPageWeb.goToPage();

        step("Search for the product");
        cmsInventoryManagementProductListPageWeb.inputSearchBar(productName);
        cmsInventoryManagementProductListPageWeb.pressSearchButton();

        step("User Selects Edit Product");
        cmsInventoryManagementProductListPageWeb.clickEditProductButton();

        step("User clicks on ADD new date button");
        cmsInventoryManagementProductListPageWeb.clickAddNewDate();

        step("User add New Date and Venue");
        cmsInventoryManagementProductListPageWeb.addVenue(venue);
        cmsInventoryManagementProductListPageWeb.selectEventDate();

        step("User verifies the snackbar message is displayed");
        boolean isVenueAdded = cmsInventoryManagementProductListPageWeb.isVenueAdded();
        Assertion.assertThat("Venue added successfully", isVenueAdded, is(true));

    }

    @Author(names = {"Rizki Maulana", "Annisa"})
    @Module(name = "InventoryManagement")
    @Tribe(name = "CMS")
    @Vertical(name = "TTD")
    @Test
    public void testCMSPricingConfigurationReflectInPackage() {
        Map<String, String> param = getParam("testCMSPricingConfigurationReflectInPackage");
        BaseTest.paramsThreadLocal.set(param);

        String username = param.get("username");
        String password = param.get("password");
        String productName = param.get("productName");

        Session<?> session = BaseTest.sessionThreadLocal.get();

        CMSLoginPageWeb<?> cmsLoginPageWeb = CMSLoginPageWeb.getInstance(session);
        CMSInventoryManagementProductListPageWeb<?> cmsInventoryManagementProductListPageWeb = CMSInventoryManagementProductListPageWeb.getInstance(session);

        step("Login in TTD CMS");
        cmsLoginPageWeb.loginAsSpecificUser(username, password);

        step("Go to Inventory-Management");
        cmsInventoryManagementProductListPageWeb.goToPage();

        step("Search for product Pricing Annisa Commission");
        cmsInventoryManagementProductListPageWeb.inputSearchBar(productName);
        cmsInventoryManagementProductListPageWeb.pressSearchButton();

        step("Click on edit product and check Pricing Configuration > commission based");
        cmsInventoryManagementProductListPageWeb.clickEditProductButton();
        boolean isCommissionBased = cmsInventoryManagementProductListPageWeb.verifyProductIsCommissionBased();
        Assertion.assertThat("Check if the Product is Commission Based", isCommissionBased, is(true));

        step("Choose any package and check > Check price tier and check Commission Percentage and commission should have value");
        cmsInventoryManagementProductListPageWeb.clickRandomPackageEditButton();
        boolean isPackageCommissionBased = cmsInventoryManagementProductListPageWeb.verifyPackageIsCommissionBased();
        Assertion.assertThat("Check if the Product is Commission Based", isPackageCommissionBased, is(true));
    }

    @Author(names = {"Nishtha"})
    @Module(name = "ProductList")
    @Tribe(name = "CMS")
    @Vertical(name = "TTD")
    @Test
    public void testAssignScheduleOnSeatMapSetting() {
        Map<String, String> param = getParam("testAssignScheduleOnSeatMapSetting");
        BaseTest.paramsThreadLocal.set(param);

        String cmsUsername = param.get("cmsUsername");
        String cmsPassword = param.get("cmsPassword");
        String productName = param.get("productName");

        Session<?> session = BaseTest.sessionThreadLocal.get();

        CMSLoginPageWeb<?> cmsLoginPageWeb = CMSLoginPageWeb.getInstance(session);
        CMSInventoryManagementProductListPageWeb<?> cmsInventoryManagementProductListPageWeb = CMSInventoryManagementProductListPageWeb.getInstance(session);

        step("Go to CMS login page and login");
        cmsLoginPageWeb.loginAsSpecificUser(cmsUsername, cmsPassword);

        step("Go to Product List page");
        cmsInventoryManagementProductListPageWeb.goToPage();

        step("Search for the product");
        cmsInventoryManagementProductListPageWeb.inputSearchBar(productName);
        cmsInventoryManagementProductListPageWeb.pressSearchButton();

        step("User goes to Package section");
        cmsInventoryManagementProductListPageWeb.goToPackage();

        step("User clicks on schedule package button");
        cmsInventoryManagementProductListPageWeb.clickManualSeatingScheduleButton();

        step("User clicks on seat map setting button");
        cmsInventoryManagementProductListPageWeb.clickSeatMapButton();

        step("User verifies that all seats are assigned.");
        boolean areAllSeatsAssigned = cmsInventoryManagementProductListPageWeb.checkAllSeatsAreAssigned();
        Assertion.assertThat("Verify all seats are assigned.", areAllSeatsAssigned, is(true));
    }

    @Author(names = {"Nishtha"})
    @Module(name = "ProductList")
    @Tribe(name = "CMS")
    @Vertical(name = "TTD")
    @Test
    public void testSeeScheduleSummaryOnSeatMapSetting() {
        Map<String, String> param = getParam("testSeeScheduleSummaryOnSeatMapSetting");
        BaseTest.paramsThreadLocal.set(param);

        String cmsUsername = param.get("cmsUsername");
        String cmsPassword = param.get("cmsPassword");
        String productName = param.get("productName");

        Session<?> session = BaseTest.sessionThreadLocal.get();

        CMSLoginPageWeb<?> cmsLoginPageWeb = CMSLoginPageWeb.getInstance(session);
        CMSInventoryManagementProductListPageWeb<?> cmsInventoryManagementProductListPageWeb = CMSInventoryManagementProductListPageWeb.getInstance(session);

        step("Go to CMS login page and login");
        cmsLoginPageWeb.loginAsSpecificUser(cmsUsername, cmsPassword);

        step("Go to Product List page");
        cmsInventoryManagementProductListPageWeb.goToPage();

        step("Search for the product");
        cmsInventoryManagementProductListPageWeb.inputSearchBar(productName);
        cmsInventoryManagementProductListPageWeb.pressSearchButton();

        step("User goes to Package section");
        cmsInventoryManagementProductListPageWeb.goToPackage();

        step("User clicks on schedule package button");
        cmsInventoryManagementProductListPageWeb.clickManualSeatingScheduleButton();

        step("User clicks on seat map setting button");
        cmsInventoryManagementProductListPageWeb.clickSeatMapButton();

        step("Verify User can see schedule summary on seat map setting page");
        boolean seatMapSummaryDisplayed = cmsInventoryManagementProductListPageWeb.checkSeatMapScheduleSummaryIsDisplayed();
        Assertion.assertThat("User can see schedule summary on seat map setting page", seatMapSummaryDisplayed, is(true));
    }

    @Author(names = {"Nishtha"})
    @Module(name = "ProductList")
    @Tribe(name = "CMS")
    @Vertical(name = "TTD")
    @Test
    public void testBlockSeatForPartnerInSeatMapSetting() {
        Map<String, String> param = getParam("testBlockSeatForPartnerInSeatMapSetting");
        BaseTest.paramsThreadLocal.set(param);

        String cmsUsername = param.get("cmsUsername");
        String cmsPassword = param.get("cmsPassword");
        String productName = param.get("productName");

        Session<?> session = BaseTest.sessionThreadLocal.get();

        CMSLoginPageWeb<?> cmsLoginPageWeb = CMSLoginPageWeb.getInstance(session);
        CMSInventoryManagementProductListPageWeb<?> cmsInventoryManagementProductListPageWeb = CMSInventoryManagementProductListPageWeb.getInstance(session);

        step("Go to CMS login page and login");
        cmsLoginPageWeb.loginAsSpecificUser(cmsUsername, cmsPassword);

        step("Go to Product List page");
        cmsInventoryManagementProductListPageWeb.goToPage();

        step("Search for the product");
        cmsInventoryManagementProductListPageWeb.inputSearchBar(productName);
        cmsInventoryManagementProductListPageWeb.pressSearchButton();

        step("User goes to Package section");
        cmsInventoryManagementProductListPageWeb.goToPackage();

        step("User search for schedule package");
        String packageName = param.get("packageName");
        cmsInventoryManagementProductListPageWeb.clickSearchInput(packageName);

        step("User clicks on schedule package button");
        cmsInventoryManagementProductListPageWeb.clickOnScheduleBlockedPartner();

        step("User clicks on seat map setting button");
        cmsInventoryManagementProductListPageWeb.clickSeatMapButton();

        step("User blocks a seat for a partner");
        cmsInventoryManagementProductListPageWeb.findFirstUnblockedSeat();
        cmsInventoryManagementProductListPageWeb.clickOnFirstUnblockedSeat();

        step("User saves the blocked seat");
        cmsInventoryManagementProductListPageWeb.clickBlockButton();
        cmsInventoryManagementProductListPageWeb.clickBlockForPartnerRadio();
        cmsInventoryManagementProductListPageWeb.clickSaveBlockForPartnerButton();
        int blockedSeatCountBefore = cmsInventoryManagementProductListPageWeb.getBlockedForPartnerValueBefore();
        cmsInventoryManagementProductListPageWeb.clickAssignToScheduleButton();
        cmsInventoryManagementProductListPageWeb.clickSaveButton();

        step("User clicks on seat map setting button");
        cmsInventoryManagementProductListPageWeb.clickSeatMapButton();

        step("Verify User can see the blocked seat for the partner");
        Assertion.assertThat("User navigate again to the seatmap schedule summary and checks if blocked for partner count increased",
                cmsInventoryManagementProductListPageWeb.isSeatBlockedForPartner(blockedSeatCountBefore), is(true));

    }

    @Author(names = {"Nishtha"})
    @Module(name = "ProductList")
    @Tribe(name = "CMS")
    @Vertical(name = "TTD")
    @Test
    public void testBlockSeatForAllPackageInSeatMapSetting() {
        Map<String, String> param = getParam("testBlockSeatForAllPackageInSeatMapSetting");
        BaseTest.paramsThreadLocal.set(param);

        String cmsUsername = param.get("cmsUsername");
        String cmsPassword = param.get("cmsPassword");
        String productName = param.get("productName");

        Session<?> session = BaseTest.sessionThreadLocal.get();

        CMSLoginPageWeb<?> cmsLoginPageWeb = CMSLoginPageWeb.getInstance(session);
        CMSInventoryManagementProductListPageWeb<?> cmsInventoryManagementProductListPageWeb = CMSInventoryManagementProductListPageWeb.getInstance(session);

        step("Go to CMS login page and login");
        cmsLoginPageWeb.loginAsSpecificUser(cmsUsername, cmsPassword);

        step("Go to Product List page");
        cmsInventoryManagementProductListPageWeb.goToPage();

        step("Search for the product");
        cmsInventoryManagementProductListPageWeb.inputSearchBar(productName);
        cmsInventoryManagementProductListPageWeb.pressSearchButton();

        step("User goes to Package section");
        cmsInventoryManagementProductListPageWeb.goToPackage();

        step("User search for schedule package");
        String packageName = param.get("packageName");
        cmsInventoryManagementProductListPageWeb.clickSearchInput(packageName);

        step("User clicks on schedule package button");
        cmsInventoryManagementProductListPageWeb.clickOnScheduleBlockedPartner();

        step("User clicks on seat map setting button");
        cmsInventoryManagementProductListPageWeb.clickSeatMapButton();

        step("User blocks a seat for a partner");
        cmsInventoryManagementProductListPageWeb.findFirstUnblockedSeat();
        cmsInventoryManagementProductListPageWeb.clickOnFirstUnblockedSeat();

        step("User saves the blocked seat");
        cmsInventoryManagementProductListPageWeb.clickBlockButton();
        cmsInventoryManagementProductListPageWeb.clickBlockForAllPackagesRadio();
        cmsInventoryManagementProductListPageWeb.clickSaveBlockForPartnerButton();
        int blockedSeatCountBefore = cmsInventoryManagementProductListPageWeb.getBlockedForAllPackagesValueBefore();
        cmsInventoryManagementProductListPageWeb.clickAssignToScheduleButton();
        cmsInventoryManagementProductListPageWeb.clickSaveButton();

        step("User clicks on seat map setting button");
        cmsInventoryManagementProductListPageWeb.clickSeatMapButton();

        step("Verify User can see the blocked seat for all package");
        Assertion.assertThat("User navigate again to the seatmap schedule summary and checks if blocked for all package count increased",
                cmsInventoryManagementProductListPageWeb.isSeatBlockedForAllPackages(blockedSeatCountBefore), is(true));
    }

    @Author(names = {"Ram Pratap Singh"})
    @Module(name = "ManageOrderReport")
    @Tribe(name = "Ignis")
    @Vertical(name = "TTD")
    @Test
    public void testIgnisExtendVisitDateMultipleOrSingleOrderId() {
        Map<String, String> param = getParam("testIgnisExtendVisitDateMultipleOrSingleOrderId");
        BaseTest.paramsThreadLocal.set(param);

        String username = param.get("username");
        String password = param.get("password");

        String partnerName = param.get("partnerName");
        String productName = param.get("productName");
        String packageName = param.get("packageName");
        String updateDays = param.get("updateDays");
        String updateAmount = param.get("updateAmount");

        Session<?> session = BaseTest.sessionThreadLocal.get();
        IgnisLoginPageWeb<?> ignisLoginPageWeb = IgnisLoginPageWeb.getInstance(session);
        IgnisSelectPartnerPageWeb<?> ignisSelectPartnerPageWeb = IgnisSelectPartnerPageWeb.getInstance(session);
        IgnisOrderReportPageWeb<?> ignisOrderReportPageWeb = IgnisOrderReportPageWeb.getInstance(session);
        IgnisManageOrderPageWeb<?> ignisManageOrderVisitDatePageWeb = IgnisManageOrderPageWeb.getInstance(session);

        step("Login to Ignis");
        ignisLoginPageWeb.loginAsSpecificUser(username, password);

        step("Select any partners");
        ignisSelectPartnerPageWeb.selectPartner(partnerName);

        step("Go to Order Report page");
        ignisOrderReportPageWeb.goToPage();

        step("Click Order Configurations, select Manage Order");
        ignisOrderReportPageWeb.clickOrderConfigButton();
        ignisOrderReportPageWeb.clickOrderConfigOptions("manage order/atur order");

        step("On the popup, select Update Visit Date radio button");
        ignisOrderReportPageWeb.selectUpdateOptionManageOrderPopup("update visit date");

        step("Click Continue");
        ignisOrderReportPageWeb.clickContinueOnManageOrderPopup();

        step("On Manage Order page, select product and packages");
        ignisManageOrderVisitDatePageWeb.setTTDFilter(productName);
        ignisManageOrderVisitDatePageWeb.setPackageFilter(packageName);

        step("Click Search");
        ignisManageOrderVisitDatePageWeb.clickSearchButton();

        step("Select any order ids to be extended");
        List<String> updatedOrderIds = ignisManageOrderVisitDatePageWeb.selectRandomRow(Integer.parseInt(updateAmount));

        step("Click Manage Order");
        ignisManageOrderVisitDatePageWeb.clickManageOrderButton();

        step("Select New Date and Timeslot (if any)");
        ignisManageOrderVisitDatePageWeb.inputNewVisitDate(Integer.parseInt(updateDays));
        ignisManageOrderVisitDatePageWeb.inputNewTimeVisit();
        ignisManageOrderVisitDatePageWeb.clickOkUpdateOrderPopUp();

        step("Click OK");
        String newDateTime = ignisManageOrderVisitDatePageWeb.getNewUpdatedDate();
        ignisManageOrderVisitDatePageWeb.clickOkPreviewPopUp();

        step("Click Apply");
        ignisManageOrderVisitDatePageWeb.clickOkApplyPopUp();

        step("Check whether the extended orders can still be redeemed");
        ignisOrderReportPageWeb.goToPage();
        ignisOrderReportPageWeb.setSearchByOrder();
        for (String id : updatedOrderIds) {
            ignisOrderReportPageWeb.inputOrderId(id);
            Assertion.assertThat(String.format("Order ID %s has been extended successfully", id), ignisOrderReportPageWeb.verifyVisitDateWithDateString(newDateTime), is(true));
        }
    }

    @Author(names = {"Rizki Maulana"})
    @Module(name = "InventoryManagement")
    @Tribe(name = "CMS")
    @Vertical(name = "TTD")
    @Test
    public void testCMSPricingConfigurationReflectInPackageMarkUpBased() {
        Map<String, String> param = getParam("testCMSPricingConfigurationReflectInPackageMarkUpBased");
        BaseTest.paramsThreadLocal.set(param);

        String username = param.get("username");
        String password = param.get("password");
        String productName = param.get("productName");

        Session<?> session = BaseTest.sessionThreadLocal.get();

        CMSLoginPageWeb<?> cmsLoginPageWeb = CMSLoginPageWeb.getInstance(session);
        CMSInventoryManagementProductListPageWeb<?> cmsInventoryManagementProductListPageWeb = CMSInventoryManagementProductListPageWeb.getInstance(session);

        step("Login in TTD CMS");
        cmsLoginPageWeb.loginAsSpecificUser(username, password);

        step("Go to Inventory-Management");
        cmsInventoryManagementProductListPageWeb.goToPage();

        step("Search for product Preprod Annisa Pricing Markup");
        cmsInventoryManagementProductListPageWeb.inputSearchBar(productName);
        cmsInventoryManagementProductListPageWeb.pressSearchButton();

        step("Click on edit product and check Pricing Configuration > Markup based");
        cmsInventoryManagementProductListPageWeb.clickEditProductButton();
        boolean isMarkupBased = cmsInventoryManagementProductListPageWeb.verifyProductIsMarkupBased();
        Assertion.assertThat("Check if the Product is Mark Up Based", isMarkupBased, is(true));
        String markupType = cmsInventoryManagementProductListPageWeb.getMarkupType();

        step("Choose any package and check > Check price tier and check markup Percentage and markup amount should have value");
        cmsInventoryManagementProductListPageWeb.clickRandomPackageEditButton();
        boolean isPackageMarkupBased = cmsInventoryManagementProductListPageWeb.verifyPackageIsMarkupBased(markupType);
        Assertion.assertThat("Check if the Package is Mark Up Based", isPackageMarkupBased, is(true));

        step("Log out");
        cmsLoginPageWeb.logoutCMS();
    }

    @Author(names = {"Nishtha"})
    @Module(name = "SupplierConfig")
    @Tribe(name = "CMS")
    @Vertical(name = "TTD")
    @Test
    public void testCMSSupplierConfigRWSMapping() {
        Map<String, String> param = getParam("testCMSSupplierConfigRWSMapping");
        BaseTest.paramsThreadLocal.set(param);

        String cmsUsername = param.get("cmsUsername");
        String cmsPassword = param.get("cmsPassword");

        Session<?> session = BaseTest.sessionThreadLocal.get();

        CMSLoginPageWeb<?> cmsLoginPageWeb = CMSLoginPageWeb.getInstance(session);
        CMSSupplierConfigToDoListPageWeb<?> cmsSupplierConfigToDoListPageWeb = CMSSupplierConfigToDoListPageWeb.getInstance(session);

        step("Login to CMS");
        cmsLoginPageWeb.loginAsSpecificUser(cmsUsername, cmsPassword);

        step("Go to RWS Mapping on CMS");
        cmsSupplierConfigToDoListPageWeb.goToRWSMappingPage();

        step("Click action button.");
        cmsSupplierConfigToDoListPageWeb.clickActionButton();

        step("Click edit button.");
        cmsSupplierConfigToDoListPageWeb.clickEditButton();

        step("Click on toggle button to update the toggle.");
        cmsSupplierConfigToDoListPageWeb.updateToggleButton();

        step("Click on Apply button.");
        cmsSupplierConfigToDoListPageWeb.clickOnApplyButton();

        step("Assertion");
        Assertion.assertThat("Verify that the offer update alert is displayed.", cmsSupplierConfigToDoListPageWeb.isOfferUpdateAlertDisplayed(), is(true));

        step("logout CMS");
        cmsLoginPageWeb.logoutCMS();
    }

    @Author(names = {"Nishtha"})
    @Module(name = "ProductSettings")
    @Tribe(name = "CMS")
    @Vertical(name = "TTD")
    @Test
    public void testCMSProductSettingsSeatMapExport() {
        Map<String, String> param = getParam("testCMSProductSettingsSeatMapExport");
        BaseTest.paramsThreadLocal.set(param);

        String cmsUsername = param.get("cmsUsername");
        String cmsPassword = param.get("cmsPassword");

        Session<?> session = BaseTest.sessionThreadLocal.get();

        CMSLoginPageWeb<?> cmsLoginPageWeb = CMSLoginPageWeb.getInstance(session);
        CMSProductSettingSeatMapWeb<?> cmsProductSettingSeatMapWeb = CMSProductSettingSeatMapWeb.getInstance(session);
        CMSProgressLogWeb<?> cmsProgressLogWeb = CMSProgressLogWeb.getInstance(session);

        step("Login to CMS");
        cmsLoginPageWeb.loginAsSpecificUser(cmsUsername, cmsPassword);

        step("Go to seat map page on CMS");
        cmsProductSettingSeatMapWeb.goToSeatMapPage();

        step("Click action button.");
        cmsProductSettingSeatMapWeb.clickActionButton();

        step("Click edit button.");
        cmsProductSettingSeatMapWeb.clickEditButton();

        step("Click on Export to xlsx button.");
        cmsProductSettingSeatMapWeb.clickOnExportButton();

        step("Click on Submit button.");
        cmsProductSettingSeatMapWeb.clickOnSubmitButton();

        step("Go to progress log page on CMS");
        cmsProgressLogWeb.goToProgressLogPage();

        step("Assertion");
        Assertion.assertThat("Verify that seatmap export is completed.", cmsProgressLogWeb.isSeatMapExportCompleted(), is(true));

        step("logout CMS");
        cmsLoginPageWeb.logoutCMS();
    }

    @Author(names = {"Nishtha"})
    @Module(name = "ProductSettings")
    @Tribe(name = "CMS")
    @Vertical(name = "TTD")
    @Test
    public void testSupplierProductMapping() {
        Map<String, String> param = getParam("testSupplierProductMapping");
        BaseTest.paramsThreadLocal.set(param);

        String cmsUsername = param.get("cmsUsername");
        String cmsPassword = param.get("cmsPassword");

        Session<?> session = BaseTest.sessionThreadLocal.get();

        CMSLoginPageWeb<?> cmsLoginPageWeb = CMSLoginPageWeb.getInstance(session);
        CMSSupplierConfigToDoListPageWeb<?> cmsSupplierConfigToDoListPageWeb = CMSSupplierConfigToDoListPageWeb.getInstance(session);

        step("Login to CMS");
        cmsLoginPageWeb.loginAsSpecificUser(cmsUsername, cmsPassword);

        step("Go to seat map page on CMS");
        cmsSupplierConfigToDoListPageWeb.goToSupplierProductPage();

        step("Click on Search Supplier and select supplier.");
        cmsSupplierConfigToDoListPageWeb.selectSupplierFilter();

        step("Assertion");
        Assertion.assertThat("Verify that same supplier exists as passed ", cmsSupplierConfigToDoListPageWeb.areAllSuppliersAncol(), is(true));

        step("logout CMS");
        cmsLoginPageWeb.logoutCMS();
    }

    @Author(names = {"Ram Pratap Singh"})
    @Module(name = "ProductList")
    @Tribe(name = "CMS")
    @Vertical(name = "TTD")
    @Test
    public void testAddManualSeating() {
        Map<String, String> param = getParam("testAddManualSeating");
        BaseTest.paramsThreadLocal.set(param);

        String cmsUsername = param.get("cmsUsername");
        String cmsPassword = param.get("cmsPassword");
        String productName = param.get("productName");
        String name = param.get("name");
        String value = param.get("value");
        String description = param.get("description");
        String min = param.get("min");
        String max = param.get("max");
        Session<?> session = BaseTest.sessionThreadLocal.get();

        CMSLoginPageWeb<?> cmsLoginPageWeb = CMSLoginPageWeb.getInstance(session);
        CMSInventoryManagementProductListPageWeb<?> cmsInventoryManagementProductListPageWeb = CMSInventoryManagementProductListPageWeb.getInstance(session);

        step("Go to CMS login page and login");
        cmsLoginPageWeb.loginAsSpecificUser(cmsUsername, cmsPassword);

        step("Go to Product List page");
        cmsInventoryManagementProductListPageWeb.goToPage();

        step("Search for the product");
        cmsInventoryManagementProductListPageWeb.inputSearchBar(productName);
        cmsInventoryManagementProductListPageWeb.pressSearchButton();
        cmsInventoryManagementProductListPageWeb.clickEditProductButton();

        step("Go to Add Package section");
        cmsInventoryManagementProductListPageWeb.addPackageButton();

        step("Fill the general details of the package");
        cmsInventoryManagementProductListPageWeb.fillPackageDetails(name, value);

        step("User selects Manual Seat");
        cmsInventoryManagementProductListPageWeb.selectSeatTypeDropdown();
        cmsInventoryManagementProductListPageWeb.selectSeatSelectionAvailable();

        step("User fills the minimum and maximum pax");
        cmsInventoryManagementProductListPageWeb.fillMinimumAndMaximumPax(min, max, description, value);

        step("User fills the quota");
        cmsInventoryManagementProductListPageWeb.enterQuota(value);

        step("The user verifies the package has been added successfully");
        cmsInventoryManagementProductListPageWeb.clickSearchInput(name);
        Assertion.assertThat("Package added successfully", cmsInventoryManagementProductListPageWeb.isFirstRowPackageNameDisplayed(name), is(true));
    }

    @Author(names = {"Ram Pratap Singh"})
    @Module(name = "ProductList")
    @Tribe(name = "CMS")
    @Vertical(name = "TTD")
    @Test
    public void testAddAutomaticSeating() {
        Map<String, String> param = getParam("testAddAutomaticSeating");
        BaseTest.paramsThreadLocal.set(param);
        String cmsUsername = param.get("cmsUsername");
        String cmsPassword = param.get("cmsPassword");
        String productName = param.get("productName");
        String name = param.get("name");
        String value = param.get("value");
        String description = param.get("description");
        String min = param.get("min");
        String max = param.get("max");
        Session<?> session = BaseTest.sessionThreadLocal.get();

        CMSLoginPageWeb<?> cmsLoginPageWeb = CMSLoginPageWeb.getInstance(session);
        CMSInventoryManagementProductListPageWeb<?> cmsInventoryManagementProductListPageWeb = CMSInventoryManagementProductListPageWeb.getInstance(session);

        step("Go to CMS login page and login");
        cmsLoginPageWeb.loginAsSpecificUser(cmsUsername, cmsPassword);

        step("Go to Product List page");
        cmsInventoryManagementProductListPageWeb.goToPage();

        step("Search for the product");
        cmsInventoryManagementProductListPageWeb.inputSearchBar(productName);
        cmsInventoryManagementProductListPageWeb.pressSearchButton();
        cmsInventoryManagementProductListPageWeb.clickEditProductButton();

        step("Go to Add Package section");
        cmsInventoryManagementProductListPageWeb.addPackageButton();

        step("Fill the general details of the package");
        cmsInventoryManagementProductListPageWeb.fillPackageDetails(name, value);

        step("User selects Automatic Seat");
        cmsInventoryManagementProductListPageWeb.selectSeatTypeDropdown();
        cmsInventoryManagementProductListPageWeb.selectSeatSelectedAutomatically();

        step("User fills the minimum and maximum pax");
        cmsInventoryManagementProductListPageWeb.fillMinimumAndMaximumPax(min, max, description, value);

        step("User clicks on Submit button");
        cmsInventoryManagementProductListPageWeb.clickSubmitButton();

        step("The user verifies the package has been added successfully");
        cmsInventoryManagementProductListPageWeb.clickSearchInput(name);
        Assertion.assertThat("Package added successfully", cmsInventoryManagementProductListPageWeb.isFirstRowPackageNameDisplayed(name), is(true));
    }

    @Author(names = {"Ram Pratap Singh"})
    @Module(name = "ProductList")
    @Tribe(name = "CMS")
    @Vertical(name = "TTD")
    @Test
    public void testFilterProductByPrimaryCategoryInInventoryManagement() {
        Map<String, String> param = getParam("testFilterProductByPrimaryCategoryInInventoryManagement");
        BaseTest.paramsThreadLocal.set(param);
        String cmsUsername = param.get("cmsUsername");
        String cmsPassword = param.get("cmsPassword");
        String header = param.get("header");
        String value = param.get("value");
        Session<?> session = BaseTest.sessionThreadLocal.get();

        CMSLoginPageWeb<?> cmsLoginPageWeb = CMSLoginPageWeb.getInstance(session);
        CMSInventoryManagementProductListPageWeb<?> cmsInventoryManagementProductListPageWeb = CMSInventoryManagementProductListPageWeb.getInstance(session);

        step("Go to CMS login page and login");
        cmsLoginPageWeb.loginAsSpecificUser(cmsUsername, cmsPassword);

        step("Go to Product List page");
        cmsInventoryManagementProductListPageWeb.goToPage();

        step("Search for the primary category");
        cmsInventoryManagementProductListPageWeb.selectPrimaryCategory(value);

        step("User verifies that the primary category is selected");
        boolean isPrimaryCategorySelected = cmsInventoryManagementProductListPageWeb.verifyCategoryValues(header, value);
        Assertion.assertThat("Primary category is selected", isPrimaryCategorySelected, is(true));

        step("The user logs out from CMS");
        cmsLoginPageWeb.logoutCMS();
    }

    @Author(names = {"Ram Pratap Singh"})
    @Module(name = "ProductList")
    @Tribe(name = "CMS")
    @Vertical(name = "TTD")
    @Test
    public void testFilterProductByCountryInInventoryManagement() {
        Map<String, String> param = getParam("testFilterProductByCountryInInventoryManagement");
        BaseTest.paramsThreadLocal.set(param);
        String cmsUsername = param.get("cmsUsername");
        String cmsPassword = param.get("cmsPassword");
        String country = param.get("country");
        String header = param.get("header");
        Session<?> session = BaseTest.sessionThreadLocal.get();

        CMSLoginPageWeb<?> cmsLoginPageWeb = CMSLoginPageWeb.getInstance(session);
        CMSInventoryManagementProductListPageWeb<?> cmsInventoryManagementProductListPageWeb = CMSInventoryManagementProductListPageWeb.getInstance(session);

        step("Go to CMS login page and login");
        cmsLoginPageWeb.loginAsSpecificUser(cmsUsername, cmsPassword);

        step("Go to Product List page");
        cmsInventoryManagementProductListPageWeb.goToPage();

        step("Search for the Country");
        cmsInventoryManagementProductListPageWeb.searchByCountry(country);

        step("User verifies that the Country is selected");
        boolean isPrimaryCategorySelected = cmsInventoryManagementProductListPageWeb.verifyCategoryValues(header, country);
        Assertion.assertThat("Country is selected", isPrimaryCategorySelected, is(true));

        step("The user logs out from CMS");
        cmsLoginPageWeb.logoutCMS();
    }

    @Author(names = {"Ram Pratap Singh"})
    @Module(name = "ProductList")
    @Tribe(name = "CMS")
    @Vertical(name = "TTD")
    @Test
    public void testFilterProductByPartnerInInventoryManagement() {
        Map<String, String> param = getParam("testFilterProductByPartnerInInventoryManagement");
        BaseTest.paramsThreadLocal.set(param);
        String cmsUsername = param.get("cmsUsername");
        String cmsPassword = param.get("cmsPassword");
        String partner = param.get("partner");
        Session<?> session = BaseTest.sessionThreadLocal.get();

        CMSLoginPageWeb<?> cmsLoginPageWeb = CMSLoginPageWeb.getInstance(session);
        CMSInventoryManagementProductListPageWeb<?> cmsInventoryManagementProductListPageWeb = CMSInventoryManagementProductListPageWeb.getInstance(session);

        step("Go to CMS login page and login");
        cmsLoginPageWeb.loginAsSpecificUser(cmsUsername, cmsPassword);

        step("Go to Product List page");
        cmsInventoryManagementProductListPageWeb.goToPage();

        step("Search for the Partner");
        cmsInventoryManagementProductListPageWeb.searchByPartner(partner);

        step("User verifies that the Partner is selected");
        cmsInventoryManagementProductListPageWeb.clickEditProductButton();
        boolean isPartnerSelected = cmsInventoryManagementProductListPageWeb.verifySelectedPartner(partner);
        Assertion.assertThat("Partner is selected", isPartnerSelected, is(true));

        step("The user logs out from CMS");
        cmsLoginPageWeb.logoutCMS();
    }

    @Author(names = {"Ram Pratap Singh"})
    @Module(name = "ProductList")
    @Tribe(name = "CMS")
    @Vertical(name = "TTD")
    @Test
    public void testFilterProductBySupplierAndCategoryInInventoryManagement() {
        Map<String, String> param = getParam("testFilterProductBySupplierAndCategoryInInventoryManagement");
        BaseTest.paramsThreadLocal.set(param);
        String cmsUsername = param.get("cmsUsername");
        String cmsPassword = param.get("cmsPassword");
        String category = param.get("category");
        String supplier = param.get("supplier");
        String categoryHeader = param.get("categoryHeader");
        String supplierHeader = param.get("supplierHeader");

        Session<?> session = BaseTest.sessionThreadLocal.get();

        CMSLoginPageWeb<?> cmsLoginPageWeb = CMSLoginPageWeb.getInstance(session);
        CMSInventoryManagementProductListPageWeb<?> cmsInventoryManagementProductListPageWeb = CMSInventoryManagementProductListPageWeb.getInstance(session);

        step("Go to CMS login page and login");
        cmsLoginPageWeb.loginAsSpecificUser(cmsUsername, cmsPassword);

        step("Go to Product List page");
        cmsInventoryManagementProductListPageWeb.goToPage();

        step("Search for the category");
        cmsInventoryManagementProductListPageWeb.selectPrimaryCategory(category);

        step("Search for the supplier");
        cmsInventoryManagementProductListPageWeb.searchBySupplier(supplier);

        step("User verifies that the supplier and category is selected");
        boolean isPrimaryCategorySelected = cmsInventoryManagementProductListPageWeb.verifyCategoryValues(categoryHeader, category);
        boolean isCategorySelected = cmsInventoryManagementProductListPageWeb.verifyCategoryValues(supplierHeader, supplier);

        Assertion.assertThat("supplier is selected", isPrimaryCategorySelected && isCategorySelected, is(true));

        step("The user logs out from CMS");
        cmsLoginPageWeb.logoutCMS();
    }

    @Author(names = {"Ram Pratap Singh"})
    @Module(name = "ProductList")
    @Tribe(name = "CMS")
    @Vertical(name = "TTD")
    @Test
    public void testFilterProductBySupplierAndCountryInInventoryManagement() {
        Map<String, String> param = getParam("testFilterProductBySupplierAndCountryInInventoryManagement");
        BaseTest.paramsThreadLocal.set(param);
        String cmsUsername = param.get("cmsUsername");
        String cmsPassword = param.get("cmsPassword");
        String country = param.get("country");
        String supplier = param.get("supplier");
        String supplierHeader = param.get("supplierHeader");

        Session<?> session = BaseTest.sessionThreadLocal.get();

        CMSLoginPageWeb<?> cmsLoginPageWeb = CMSLoginPageWeb.getInstance(session);
        CMSInventoryManagementProductListPageWeb<?> cmsInventoryManagementProductListPageWeb = CMSInventoryManagementProductListPageWeb.getInstance(session);

        step("Go to CMS login page and login");
        cmsLoginPageWeb.loginAsSpecificUser(cmsUsername, cmsPassword);

        step("Go to Product List page");
        cmsInventoryManagementProductListPageWeb.goToPage();

        step("Search for the country");
        cmsInventoryManagementProductListPageWeb.searchByCountry(country);

        step("Search for the supplier");
        cmsInventoryManagementProductListPageWeb.searchBySupplier(supplier);

        step("User verifies that the supplier is selected");
        boolean isSupplierSelected = cmsInventoryManagementProductListPageWeb.verifyCategoryValues(supplierHeader, supplier);
        Assertion.assertThat("Supplier is selected", isSupplierSelected, is(true));

        step("The user logs out from CMS");
        cmsLoginPageWeb.logoutCMS();
    }

    @Author(names = {"Ram Pratap Singh"})
    @Module(name = "ProductList")
    @Tribe(name = "CMS")
    @Vertical(name = "TTD")
    @Test
    public void testFilterProductBySupplierAndStatusInInventoryManagement() {
        Map<String, String> param = getParam("testFilterProductBySupplierAndStatusInInventoryManagement");
        BaseTest.paramsThreadLocal.set(param);
        String cmsUsername = param.get("cmsUsername");
        String cmsPassword = param.get("cmsPassword");
        String status = param.get("status");
        String supplier = param.get("supplier");
        String statusHeader = param.get("statusHeader");
        String supplierHeader = param.get("supplierHeader");

        Session<?> session = BaseTest.sessionThreadLocal.get();

        CMSLoginPageWeb<?> cmsLoginPageWeb = CMSLoginPageWeb.getInstance(session);
        CMSInventoryManagementProductListPageWeb<?> cmsInventoryManagementProductListPageWeb = CMSInventoryManagementProductListPageWeb.getInstance(session);

        step("Go to CMS login page and login");
        cmsLoginPageWeb.loginAsSpecificUser(cmsUsername, cmsPassword);

        step("Go to Product List page");
        cmsInventoryManagementProductListPageWeb.goToPage();

        step("Select the status");
        cmsInventoryManagementProductListPageWeb.selectStatus();

        step("Search for the supplier");
        cmsInventoryManagementProductListPageWeb.searchBySupplier(supplier);

        step("User verifies that the supplier is selected");
        boolean isSupplierSelected = cmsInventoryManagementProductListPageWeb.verifyCategoryValues(supplierHeader, supplier);
        boolean isStatusSelected = cmsInventoryManagementProductListPageWeb.verifyCategoryValues(statusHeader, status);

        Assertion.assertThat("Status is selected", isStatusSelected, is(true));
        Assertion.assertThat("Supplier is selected", isSupplierSelected, is(true));

        step("The user logs out from CMS");
        cmsLoginPageWeb.logoutCMS();
    }

    @Author(names = {"Ram Pratap Singh"})
    @Module(name = "ProductList")
    @Tribe(name = "CMS")
    @Vertical(name = "TTD")
    @Test
    public void testCMSEditProductInInventoryManagement() {
        Map<String, String> param = getParam("testCMSEditProductInInventoryManagement");
        BaseTest.paramsThreadLocal.set(param);
        String cmsUsername = param.get("cmsUsername");
        String cmsPassword = param.get("cmsPassword");
        String productName = param.get("productName");
        String productURL = param.get("productURL");

        Session<?> session = BaseTest.sessionThreadLocal.get();

        CMSLoginPageWeb<?> cmsLoginPageWeb = CMSLoginPageWeb.getInstance(session);
        CMSInventoryManagementProductListPageWeb<?> cmsInventoryManagementProductListPageWeb = CMSInventoryManagementProductListPageWeb.getInstance(session);
        CMSProductManagementEditProductPageWeb<?> cmsProductManagementEditProductPageWeb = CMSProductManagementEditProductPageWeb.getInstance(session);

        step("Go to CMS login page and login");
        cmsLoginPageWeb.loginAsSpecificUser(cmsUsername, cmsPassword);

        step("Go to Product List page");
        cmsInventoryManagementProductListPageWeb.goToPage();

        step("Search for the product");
        cmsInventoryManagementProductListPageWeb.inputSearchBar(productName);
        cmsInventoryManagementProductListPageWeb.pressSearchButton();

        step("The user clicks on Edit Product button");
        cmsInventoryManagementProductListPageWeb.clickEditProductButton();

        step("The user goes to Edit Product page and edits the product details");
        String description = cmsProductManagementEditProductPageWeb.editDescription();

        step("The user clicks on Save and Close button");
        cmsProductManagementEditProductPageWeb.clickSaveAndCloseButton();

        step("The user navigates to the product URL in B2C");
        cmsInventoryManagementProductListPageWeb.goToProductInB2C(productURL);

        step("The user verifies that the product description is updated successfully");
        boolean isDescriptionUpdated = cmsProductManagementEditProductPageWeb.verifyIsDescriptionUpdated(description);
        Assertion.assertThat("The product description is updated successfully", isDescriptionUpdated, is(true));

        step("The user logs out from CMS");
        cmsLoginPageWeb.logoutCMS();
    }

    @Author(names = {"Nishtha"})
    @Module(name = "ProductSettings")
    @Tribe(name = "CMS")
    @Vertical(name = "TTD")
    @Test
    public void testEditSeatMap() {
        Map<String, String> param = getParam("testEditSeatMap");
        BaseTest.paramsThreadLocal.set(param);

        String cmsUsername = param.get("cmsUsername");
        String cmsPassword = param.get("cmsPassword");

        Session<?> session = BaseTest.sessionThreadLocal.get();

        CMSLoginPageWeb<?> cmsLoginPageWeb = CMSLoginPageWeb.getInstance(session);
        CMSProductSettingSeatMapWeb<?> cmsProductSettingSeatMapWeb = CMSProductSettingSeatMapWeb.getInstance(session);

        step("Login to CMS");
        cmsLoginPageWeb.loginAsSpecificUser(cmsUsername, cmsPassword);

        step("Go to seat map page on CMS");
        cmsProductSettingSeatMapWeb.goToSeatMapPage();

        step("Search input.");
        String seatMapName = param.get("seatMapName");
        cmsProductSettingSeatMapWeb.searchSeatmapInput(seatMapName);

        step("Click search button.");
        cmsProductSettingSeatMapWeb.clickSearchButton();

        step("Click action button.");
        cmsProductSettingSeatMapWeb.clickActionButton();

        step("Click edit button.");
        cmsProductSettingSeatMapWeb.clickEditButton();

        step("User edit the column size");
        cmsProductSettingSeatMapWeb.updateColumnSize();

        step("Click on upgrade grid button.");
        cmsProductSettingSeatMapWeb.clickOnUpgradeGridButton();

        step("Click on Submit button.");
        cmsProductSettingSeatMapWeb.clickOnSubmitButton();

        step("Assertion : Success Alert displayed");
        Assertion.assertThat("Verify that the user has successfully updated the seatmap", cmsProductSettingSeatMapWeb.isSuccessAlertDisplayed(), is(true));

        step("Search input.");
        cmsProductSettingSeatMapWeb.searchSeatmapInput(seatMapName);

        step("Click search button.");
        cmsProductSettingSeatMapWeb.clickSearchButton();

        step("Click action button.");
        cmsProductSettingSeatMapWeb.clickActionButton();

        step("Click edit button.");
        cmsProductSettingSeatMapWeb.clickEditButton();

        step("Assertion : Verify Total Grid Value");
        Assertion.assertThat("Verify Total Grid Value", cmsProductSettingSeatMapWeb.checkTotalGridValue(), is(true));

        step("logout CMS");
        cmsLoginPageWeb.logoutCMS();
    }

    @Author(names = {"Manan Kumar", "Annisa"})
    @Module(name = "Promo&Loyalty")
    @Tribe(name = "CMS")
    @Vertical(name = "TTD")
    @Test
    public void testUserCanAddTiketAndPartnerSubsidy() {
        Map<String, String> param = getParam("testUserCanAddTiketAndPartnerSubsidy");
        BaseTest.paramsThreadLocal.set(param);

        String cmsUsername = param.get("cmsUsername");
        String cmsPassword = param.get("cmsPassword");
        String packageName = param.get("packageName");
        String productName = param.get("productName");
        String Package = param.get("package");
        String subsidy = param.get("subsidy");

        Session<?> session = BaseTest.sessionThreadLocal.get();

        CMSLoginPageWeb<?> cmsLoginPageWeb = CMSLoginPageWeb.getInstance(session);
        TTDInventoryManagementPage<?> ttdInventoryManagementPage = new TTDInventoryManagementPage<>(session);
        CMSPromoAndLoyaltyCampaignPageWeb<?> cmsPromoAndLoyaltyCampaignPageWeb = CMSPromoAndLoyaltyCampaignPageWeb.getInstance(session);

        step("Go to CMS login page and login");
        cmsLoginPageWeb.loginAsSpecificUser(cmsUsername, cmsPassword);

        step("Go to Campaign page");
        ttdInventoryManagementPage.gotoToDoAdminPage();
        ttdInventoryManagementPage.selectCampaignMenu();

        step("User clicks on Actions Button");
        cmsPromoAndLoyaltyCampaignPageWeb.clickActionsButton();

        step("User clicks on Edit Action");
        cmsPromoAndLoyaltyCampaignPageWeb.clickEditAction();

        step("User Navigates to Include Things To Do");
        cmsPromoAndLoyaltyCampaignPageWeb.goToIncludeThingsToDo();

        step("User clicks on Add Things To Do Button");
        cmsPromoAndLoyaltyCampaignPageWeb.clickAddThingsToDoButton();

        step("User searches for the package");
        cmsPromoAndLoyaltyCampaignPageWeb.searchPackage(packageName);

        step("User Selects the package");
        cmsPromoAndLoyaltyCampaignPageWeb.selectPackage(productName, Package);

        step("User inputs Subsidy for Package");
        cmsPromoAndLoyaltyCampaignPageWeb.enterSubsidy(subsidy);

        step("User Verifies the Subsidy is Added for Tiket and Partner");
        boolean subsidyAdded = cmsPromoAndLoyaltyCampaignPageWeb.isSubsidyAdded(subsidy);
        Assertion.assertThat("Subsidy is added successfully", subsidyAdded, is(true));
    }

    @Author(names = {"Manan Kumar", "Annisa"})
    @Module(name = "Promo&Loyalty")
    @Tribe(name = "CMS")
    @Vertical(name = "TTD")
    @Test
    public void testUserCanAddCampaign() {
        Map<String, String> param = getParam("testUserCanAddCampaign");
        BaseTest.paramsThreadLocal.set(param);

        String cmsUsername = param.get("cmsUsername");
        String cmsPassword = param.get("cmsPassword");
        String quota = param.get("quota");
        String campaignName = param.get("campaignName");
        String label = param.get("label");
        String time = param.get("time");
        String priority = param.get("priority");
        String picName = param.get("picName");
        String picMail = param.get("picMail");
        String picNumber = param.get("picNumber");
        String packageName = param.get("packageName");
        String subsidy = param.get("subsidy");

        Session<?> session = BaseTest.sessionThreadLocal.get();

        CMSLoginPageWeb<?> cmsLoginPageWeb = CMSLoginPageWeb.getInstance(session);
        TTDInventoryManagementPage<?> ttdInventoryManagementPage = new TTDInventoryManagementPage<>(session);
        CMSPromoAndLoyaltyCampaignPageWeb<?> cmsPromoAndLoyaltyCampaignPageWeb = CMSPromoAndLoyaltyCampaignPageWeb.getInstance(session);

        step("Go to CMS login page and login");
        cmsLoginPageWeb.loginAsSpecificUser(cmsUsername, cmsPassword);

        step("The user clicks on Campaign menu");
        ttdInventoryManagementPage.gotoToDoAdminPage();
        ttdInventoryManagementPage.selectCampaignMenu();

        step("User clicks on Add Campaign Button");
        cmsPromoAndLoyaltyCampaignPageWeb.clicksAddCampaignButton();

        step("User enters Campaign Name and Quota.");
        cmsPromoAndLoyaltyCampaignPageWeb.enterCampaignNameAndQuota(quota, campaignName);

        step("User enters Campaign Label.");
        cmsPromoAndLoyaltyCampaignPageWeb.enterCampaignLabel(label);

        step("User enters Campaign date & Time.");
        cmsPromoAndLoyaltyCampaignPageWeb.selectDateAndTime(time);

        step("User Selects Campaign Status.");
        cmsPromoAndLoyaltyCampaignPageWeb.selectCampaignStatus();

        step("User selects Campaign Priority and Platform.");
        cmsPromoAndLoyaltyCampaignPageWeb.selectPriorityAndPlatform(priority);

        step("User Add PIC details.");
        cmsPromoAndLoyaltyCampaignPageWeb.enterPICDetails(picName, picMail, picNumber);

        step("User Upload Campaign Image.");
        cmsPromoAndLoyaltyCampaignPageWeb.uploadCampaignIcon();

        step("User clicks on Save Campaign Button");
        cmsPromoAndLoyaltyCampaignPageWeb.clickSaveAndContinueButton();

        step("User clicks on Add Things To Do Button");
        cmsPromoAndLoyaltyCampaignPageWeb.clickAddThingsToDoButton();

        step("User searches for the package");
        cmsPromoAndLoyaltyCampaignPageWeb.searchPackage(packageName);

        step("User Selects the package");
        cmsPromoAndLoyaltyCampaignPageWeb.selectSeatingPackage();

        step("User inputs Subsidy for Package");
        cmsPromoAndLoyaltyCampaignPageWeb.enterSubsidy(subsidy);

        step("User clicks on ADD TTD campaign button");
        cmsPromoAndLoyaltyCampaignPageWeb.clickAddTTDButton();

        step("User opens TTD page.");
        cmsPromoAndLoyaltyCampaignPageWeb.openB2CPageInNewTab();

        step("User verifies that the campaign is added successfully");
        boolean isCampaignAdded = cmsPromoAndLoyaltyCampaignPageWeb.isCampaignAdded(label);
        Assertion.assertThat("Campaign is added successfully", isCampaignAdded, is(true));
    }

    @Author(names = {"Ankit Raj", "Annisa"})
    @Module(name = "Inventory Management")
    @Tribe(name = "CMS")
    @Vertical(name = "TTD")
    @Test
    public void testVerifyProgressLogXlsxExportSeatMapping() {
        Map<String, String> param = getParam("testVerifyProgressLogXlsxExportSeatMapping");
        BaseTest.paramsThreadLocal.set(param);

        String cmsUsername = param.get("cmsUsername");
        String cmsPassword = param.get("cmsPassword");
        String productName = param.get("productName");
        String typeToVerify = param.get("typeToVerify");
        String status = param.get("status");

        Session<?> session = BaseTest.sessionThreadLocal.get();

        CMSLoginPageWeb<?> cmsLoginPageWeb = CMSLoginPageWeb.getInstance(session);
        CMSInventoryManagementProductListPageWeb<?> cmsInventoryManagementProductListPageWeb = CMSInventoryManagementProductListPageWeb.getInstance(session);
        CMSProgressLogWeb<?> cmsProgressLogWeb = CMSProgressLogWeb.getInstance(session);

        step("Login to CMS");
        cmsLoginPageWeb.loginAsSpecificUser(cmsUsername, cmsPassword);

        step("Go to seat map page on CMS");
        cmsInventoryManagementProductListPageWeb.goToPage();

        step("Search for seating_automation_copy");
        cmsInventoryManagementProductListPageWeb.inputSearchBar(productName);
        cmsInventoryManagementProductListPageWeb.pressSearchButton();

        step("Click on edit button");
        cmsInventoryManagementProductListPageWeb.clickEditProductButton();

        step("Click on schedule button");
        cmsInventoryManagementProductListPageWeb.clickScheduleManualProductButton();

        step("Click on seat map");
        cmsInventoryManagementProductListPageWeb.clickOnSeatMapIcon();

        step("Click on Export XLSX");
        cmsInventoryManagementProductListPageWeb.clickOnExportXLSXButton();

        step("Go to progress log page on CMS");
        cmsProgressLogWeb.goToProgressLogPage();

        step("Verify seat map export completed");
        cmsProgressLogWeb.isSeatMapExportCompleted();

        step("Verify the type of content");
        boolean isTypeCorrect = cmsProgressLogWeb.verifyTheContent(typeToVerify);
        Assertion.assertThat("Package is of correct type", isTypeCorrect, is(true));

        step("Verify the status");
        boolean isStatusCorrect = cmsProgressLogWeb.verifyStatus(status);
        Assertion.assertThat("Package is of correct status", isStatusCorrect, is(true));

        step("Verify download availability");
        boolean isDownloadPresent = cmsProgressLogWeb.verifyDownload();
        Assertion.assertThat("Package is downloadable", isDownloadPresent, is(true));

        step("Verify time in last 2 minutes");
        boolean verifyTime = cmsProgressLogWeb.verifyTimestampWithinLast2Minutes();
        Assertion.assertThat("Package is within last 2 minutes", verifyTime, is(true));

        step("Click on first download");
        cmsProgressLogWeb.clickOnDownload();

        step("Logout");
        cmsLoginPageWeb.logoutCMS();
    }

    @Author(names = {"Nishtha", "Annisa"})
    @Module(name = "ProductList")
    @Tribe(name = "CMS")
    @Vertical(name = "TTD")
    @Test
    public void testCMSCreatePackageInInventoryManagement() {
        Map<String, String> param = getParam("testCMSCreatePackageInInventoryManagement");
        BaseTest.paramsThreadLocal.set(param);

        String cmsUsername = param.get("cmsUsername");
        String cmsPassword = param.get("cmsPassword");
        String productName = param.get("productName");
        String name = "aut_" + CoreFunctions.randomName();
        String value = param.get("value");
        String description = param.get("description");
        String min = param.get("min");
        String max = param.get("max");
        String productURL = param.get("productURL");
        Session<?> session = BaseTest.sessionThreadLocal.get();

        CMSLoginPageWeb<?> cmsLoginPageWeb = CMSLoginPageWeb.getInstance(session);
        CMSInventoryManagementProductListPageWeb<?> cmsInventoryManagementProductListPageWeb = CMSInventoryManagementProductListPageWeb.getInstance(session);

        step("Go to CMS login page and login");
        cmsLoginPageWeb.loginAsSpecificUser(cmsUsername, cmsPassword);

        step("Go to Product List page");
        cmsInventoryManagementProductListPageWeb.goToPage();

        step("Search for the product");
        cmsInventoryManagementProductListPageWeb.inputSearchBar(productName);
        cmsInventoryManagementProductListPageWeb.pressSearchButton();
        cmsInventoryManagementProductListPageWeb.clickEditProductButton();

        step("Go to Add Package section");
        cmsInventoryManagementProductListPageWeb.addPackageButton();

        step("Fill the general details of the package");
        cmsInventoryManagementProductListPageWeb.fillPackageDetails(name, value);

        step("User fills the minimum and maximum pax");
        cmsInventoryManagementProductListPageWeb.fillMinimumAndMaximumPax(min, max, description, value);

        step("User fills the quota");
        cmsInventoryManagementProductListPageWeb.enterQuota(value);

        step("The user verifies the package has been added successfully");
        cmsInventoryManagementProductListPageWeb.clickSearchInput(name);
        Assertion.assertThat("Package added successfully", cmsInventoryManagementProductListPageWeb.isFirstRowPackageNameDisplayed(name), is(true));

        step("Go to B2C and check the package");
        cmsInventoryManagementProductListPageWeb.goToProductInB2C(productURL);

        step("Click on show all packages button");
        cmsInventoryManagementProductListPageWeb.clickShowAllPackagesButton();

        step("The user verifies the package has been added successfully and is displayed on B2C");
        Assertion.assertThat("Package added successfully", cmsInventoryManagementProductListPageWeb.isPackageDisplayed(name), is(true));

        step("Log out");
        cmsLoginPageWeb.logoutCMS();
    }

    @Author(names = {"Nishtha", "Annisa"})
    @Module(name = "ProductList")
    @Tribe(name = "CMS")
    @Vertical(name = "TTD")
    @Test
    public void testCMSDeletePackageInInventoryManagement() {
        Map<String, String> param = getParam("testCMSDeletePackageInInventoryManagement");
        BaseTest.paramsThreadLocal.set(param);

        String cmsUsername = param.get("cmsUsername");
        String cmsPassword = param.get("cmsPassword");
        String productName = param.get("productName");
        String name = "aut_" + CoreFunctions.randomName();
        String value = param.get("value");
        String description = param.get("description");
        String min = param.get("min");
        String max = param.get("max");
        String productURL = param.get("productURL");
        Session<?> session = BaseTest.sessionThreadLocal.get();

        CMSLoginPageWeb<?> cmsLoginPageWeb = CMSLoginPageWeb.getInstance(session);
        CMSInventoryManagementProductListPageWeb<?> cmsInventoryManagementProductListPageWeb = CMSInventoryManagementProductListPageWeb.getInstance(session);

        step("Go to CMS login page and login");
        cmsLoginPageWeb.loginAsSpecificUser(cmsUsername, cmsPassword);

        step("Go to Product List page");
        cmsInventoryManagementProductListPageWeb.goToPage();

        step("Search for the product");
        cmsInventoryManagementProductListPageWeb.inputSearchBar(productName);
        cmsInventoryManagementProductListPageWeb.pressSearchButton();
        cmsInventoryManagementProductListPageWeb.clickEditProductButton();

        step("Go to Add Package section");
        cmsInventoryManagementProductListPageWeb.addPackageButton();

        step("Fill the general details of the package");
        cmsInventoryManagementProductListPageWeb.fillPackageDetails(name, value);

        step("User fills the minimum and maximum pax");
        cmsInventoryManagementProductListPageWeb.fillMinimumAndMaximumPax(min, max, description, value);

        step("User fills the quota");
        cmsInventoryManagementProductListPageWeb.enterQuota(value);

        step("The user verifies the package has been added successfully");
        cmsInventoryManagementProductListPageWeb.clickSearchInput(name);
        Assertion.assertThat("Package added successfully", cmsInventoryManagementProductListPageWeb.isFirstRowPackageNameDisplayed(name), is(true));

        step("User deletes the package");
        cmsInventoryManagementProductListPageWeb.clickDeletePackageButton();

        step("The user verifies the package has been deleted successfully");
        cmsInventoryManagementProductListPageWeb.clickSearchInput(name);
        Assertion.assertThat("Package deleted successfully", cmsInventoryManagementProductListPageWeb.isFirstRowPackageNameDisplayed(name), is(false));

        step("Go to B2C and check the package");
        cmsInventoryManagementProductListPageWeb.goToProductInB2C(productURL);

        step("Click on show all packages button");
        cmsInventoryManagementProductListPageWeb.clickShowAllPackagesButton();

        step("The user verifies the package has been deleted successfully and verified on B2C");
        Assertion.assertThat("Package deleted successfully", cmsInventoryManagementProductListPageWeb.isPackageDisplayed(name), is(false));
        step("Log out");
        cmsLoginPageWeb.logoutCMS();
    }

    @Author(names = {"Nishtha", "Annisa"})
    @Module(name = "ProductList")
    @Tribe(name = "CMS")
    @Vertical(name = "TTD")
    @Test
    public void testCMSCreateProductInInventoryManagement() {
        Map<String, String> param = getParam("testCMSCreateProductInInventoryManagement");
        BaseTest.paramsThreadLocal.set(param);

        String cmsUsername = param.get("cmsUsername");
        String cmsPassword = param.get("cmsPassword");
        String productName = "autproduct" + CoreFunctions.randomName();
        String name = "aut_" + CoreFunctions.randomName();
        String location = param.get("location");
        String value = param.get("value");
        String description = param.get("description");
        String min = param.get("min");
        String max = param.get("max");
        Session<?> session = BaseTest.sessionThreadLocal.get();

        CMSLoginPageWeb<?> cmsLoginPageWeb = CMSLoginPageWeb.getInstance(session);
        CMSInventoryManagementProductListPageWeb<?> cmsInventoryManagementProductListPageWeb = CMSInventoryManagementProductListPageWeb.getInstance(session);

        step("Go to CMS login page and login");
        cmsLoginPageWeb.loginAsSpecificUser(cmsUsername, cmsPassword);

        step("Go to Product List page");
        cmsInventoryManagementProductListPageWeb.goToPage();
        cmsInventoryManagementProductListPageWeb.refreshPage();

        step("Click on Add/Update Product");
        cmsInventoryManagementProductListPageWeb.clickAddProductButton();

        step("Fill new product details");
        cmsInventoryManagementProductListPageWeb.fillNewProductDetails(productName, location);
        String commissionPercentage = param.get("commissionPercentage");
        cmsInventoryManagementProductListPageWeb.fillProductConfigurationDetails(productName, commissionPercentage);

        step("Go to Add Package section");
        cmsInventoryManagementProductListPageWeb.addPackageButton();

        step("Fill the general details of the package");
        sleep(5);
        cmsInventoryManagementProductListPageWeb.fillPackageDetails(name, value);

        step("User fills the minimum and maximum pax");
        cmsInventoryManagementProductListPageWeb.fillMinimumAndMaximumPax(min, max, description, value);

        step("User fills the quota");
        cmsInventoryManagementProductListPageWeb.enterQuota(value);

        step("Click on save button");
        cmsInventoryManagementProductListPageWeb.clickSaveButton();

        step("Go to Product List page");
        cmsInventoryManagementProductListPageWeb.goToPage();
        cmsInventoryManagementProductListPageWeb.refreshPage();

        step("Search for the product");
        cmsInventoryManagementProductListPageWeb.inputSearchBar(productName);
        cmsInventoryManagementProductListPageWeb.pressSearchButton();
        Assertion.assertThat("Product added successfully", cmsInventoryManagementProductListPageWeb.isFirstRowProductNameDisplayed(productName), is(true));

        step("Go to B2C and check the partner and package");
        cmsInventoryManagementProductListPageWeb.goToProductInB2C(productName);

        step("The user verifies the package has been added successfully and is displayed on B2C");
        Assertion.assertThat("Package added successfully on b2c", cmsInventoryManagementProductListPageWeb.isPackageDisplayed(name), is(true));

        step("Log out");
        cmsLoginPageWeb.logoutCMS();
    }

    @Author(names = {"Nishtha"})
    @Module(name = "Promo&Loyalty")
    @Tribe(name = "CMS")
    @Vertical(name = "TTD")
    @Test
    public void testEditCampaign() {
        Map<String, String> param = getParam("testEditCampaign");
        BaseTest.paramsThreadLocal.set(param);

        String cmsUsername = param.get("cmsUsername");
        String cmsPassword = param.get("cmsPassword");

        Session<?> session = BaseTest.sessionThreadLocal.get();

        CMSLoginPageWeb<?> cmsLoginPageWeb = CMSLoginPageWeb.getInstance(session);
        CMSPromoAndLoyaltyCampaignPageWeb<?> cmsPromoAndLoyaltyCampaignPageWeb = CMSPromoAndLoyaltyCampaignPageWeb.getInstance(session);

        step("Go to CMS login page and login");
        cmsLoginPageWeb.loginAsSpecificUser(cmsUsername, cmsPassword);

        step("Go to Campaign page");
        cmsPromoAndLoyaltyCampaignPageWeb.goToPage();
        cmsPromoAndLoyaltyCampaignPageWeb.goToPromoAndLoyaltyPage();

        step("Search campaign Name");
        String campaignName = param.get("campaignName");
        cmsPromoAndLoyaltyCampaignPageWeb.searchCampaign(campaignName);

        step("User clicks on Actions Button");
        cmsPromoAndLoyaltyCampaignPageWeb.clickActionsButton();

        step("User clicks on Edit Action");
        cmsPromoAndLoyaltyCampaignPageWeb.clickEditAction();

        step("Update campaign details");
        String campaignDescription = "aut_" + CoreFunctions.randomName();
        cmsPromoAndLoyaltyCampaignPageWeb.updateCampaignDescription(campaignDescription);

        step("Click on Save Button");
        cmsPromoAndLoyaltyCampaignPageWeb.clickSaveButton();

        step("Validate campaign description reflects in B2C API");
        String productUrl = param.get("productUrl");
        boolean isDescriptionValidated = cmsPromoAndLoyaltyCampaignPageWeb.validateCampaignDescriptionInB2CAPI(productUrl, campaignDescription);

        step("Assert that the campaign description is reflected in the B2C API response");
        Assertion.assertThat("Campaign description should be reflected in B2C API response",
                isDescriptionValidated, is(true));

        step("Logout from CMS");
        cmsLoginPageWeb.logoutCMS();
    }

    @Author(names = {"Akbar"})
    @Module(name = "InventoryManagement")
    @Tribe(name = "CMS")
    @Vertical(name = "TTD")
    @Test
    public void testCMSAddPackageScheduleSeatMap () {
        Map<String, String> param = getParam("testCMSAddPackageScheduleSeatMap");

        BaseTest.paramsThreadLocal.set(param);

        String username = param.get("username");
        String password = param.get("password");

        String productId = param.get("productId");
        String packageName = param.get("packageName");
        String date = CoreFunctions.generateRandomDate(param.get("dateRangeAdd"));
        String seatMapName = param.get("seatMapName");

        Session<?> session = BaseTest.sessionThreadLocal.get();

        CMSLoginPageWeb<?> cmsLoginPageWeb = CMSLoginPageWeb.getInstance(session);
        CMSProductManagementEditProductPageWeb<?> cmsProductManagementEditProductPageWeb = CMSProductManagementEditProductPageWeb.getInstance(session);
        CMSProductManagementSchedulePackagePageWeb<?> cmsProductManagementSchedulePackagePageWeb = CMSProductManagementSchedulePackagePageWeb.getInstance(session);

        step("Go to CMS login page and login");
        cmsLoginPageWeb.loginAsSpecificUser(username, password);

        step("Go to Product Edit page");
        cmsProductManagementEditProductPageWeb.goToPage(productId);

        step("Add seatmap schedule");
        cmsProductManagementEditProductPageWeb.addSeatMapSchedule(seatMapName, date);
        cmsProductManagementEditProductPageWeb.clickPackageSchedule(packageName);

        step("Create new seatmap using same date with created seatmap schedule");
        cmsProductManagementSchedulePackagePageWeb.clickUpdateSchedule();
        cmsProductManagementSchedulePackagePageWeb.clickAddSchedule();

        step("Input date and submit");
        cmsProductManagementSchedulePackagePageWeb.changeDateInputTypeNewSchedule("individual");
        cmsProductManagementSchedulePackagePageWeb.selectIndividualDateNewSchedule(date);
        cmsProductManagementSchedulePackagePageWeb.clickSubmitButtonNewSchedule();

        step("Extract data");
        boolean isSuccessSnackBarVisible = cmsProductManagementSchedulePackagePageWeb.isSuccessNotifBarVisible();
        boolean isScheduleWithSeatMapCreated = cmsProductManagementSchedulePackagePageWeb.isScheduleWithSeatMapCreated(date);

        step("Delete schedule");
        cmsProductManagementSchedulePackagePageWeb.deleteSchedule(date);
        boolean isDeleteSuccessSnackBarVisible = cmsProductManagementSchedulePackagePageWeb.isSuccessNotifBarVisible();

        step("Logout from CMS");
        cmsLoginPageWeb.logoutCMS();

        step("Assertion schedule");
        Assertion.assertThat("Success snackbar is showing after creating new schedule", isSuccessSnackBarVisible, is(true));
        Assertion.assertThat("Seatmap schedule is detected", isScheduleWithSeatMapCreated, is(true));
        Assertion.assertThat("Seatmap schedule is deleted", isDeleteSuccessSnackBarVisible, is(true));
    }

    @Author(names = {"Manan Kumar"})
    @Module(name = "InventoryManagement")
    @Tribe(name = "CMS")
    @Vertical(name = "TTD")
    @Test
    public void testUserCanSyncSuppliers () {
        Map<String, String> param = getParam("testUserCanSyncSuppliers");
        BaseTest.paramsThreadLocal.set(param);

        String cmsUsername = param.get("cmsUsername");
        String cmsPassword = param.get("cmsPassword");
        String supplierProductName = param.get("supplierProductName");
        String productName = param.get("productName");
        String supplierID = param.get("supplierID");

        Session<?> session = BaseTest.sessionThreadLocal.get();

        CMSLoginPageWeb<?> cmsLoginPageWeb = CMSLoginPageWeb.getInstance(session);
        CMSInventoryManagementProductListPageWeb<?> cmsInventoryManagementProductListPageWeb = CMSInventoryManagementProductListPageWeb.getInstance(session);

        step("Go to CMS login page and login");
        cmsLoginPageWeb.loginAsSpecificUser(cmsUsername, cmsPassword);

        step("Go to Product List page");
        cmsInventoryManagementProductListPageWeb.goToPage();

        step("User search for the Supplier Product");
        cmsInventoryManagementProductListPageWeb.enterSupplierProduct(supplierProductName);

        step("User Enter Product Name ");
        cmsInventoryManagementProductListPageWeb.inputSearchBar(productName);
        cmsInventoryManagementProductListPageWeb.pressSearchButton();

        step("User Click on Update Supplier Product");
        cmsInventoryManagementProductListPageWeb.clickUpdateSupplierProduct();

        step("User verifies that the Supplier Product is updated successfully");
        boolean isProductUpdated = cmsInventoryManagementProductListPageWeb.isSupplierProductUpdated();
        Assertion.assertThat("Supplier Product is updated successfully", isProductUpdated, is(true));

        step("Click OK on the popup");
        cmsInventoryManagementProductListPageWeb.clickOkOnSuccessPopup();

        step("User navigate to Edit Product List page");
        cmsInventoryManagementProductListPageWeb.goToEditProduct();

        step("Verify successfully updated");
        Assertion.assertThat("Verify successfully updated",cmsInventoryManagementProductListPageWeb.isProductUpdated() , is(true));

         }

    @Author(names = {"Annisa"})
    @Module(name = "Campaign")
    @Tribe(name = "Supply")
    @Vertical(name = "TTD")
    @Test
    public void checkFilterCampaignByName(){
        Map<String, String> param = getParam("checkFilterCampaignByName");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        TTDInventoryManagementPage<?> ttdInventoryManagementPage = new TTDInventoryManagementPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String campaignName = param.get("campaignName");

        step("The user logs in to the Admin dashboard");
        memberLoginPage.gotoMemberLoginPage();
        memberLoginPage.adminLogin(username, password);
        memberLoginPage.isAdminLogin();

        step("The user navigates to TTD Admin page");
        ttdInventoryManagementPage.gotoToDoAdminPage();

        step("The user clicks on Campaign menu");
        ttdInventoryManagementPage.selectCampaignMenu();

        step("Input keyword for search campaign");
        ttdInventoryManagementPage.inputCampaignName(campaignName);

        step("Click search button");
        ttdInventoryManagementPage.clickSearchCampaign();

        step("Verify campaign name contain the inputted keyword");
        String campaignResult = ttdInventoryManagementPage.getCampaignName();
        Assertion.assertThat("First campaign name contain keyword", campaignResult, anyOf(containsStringIgnoringCase(campaignName)));
    }

    @Author(names = {"Annisa"})
    @Module(name = "Campaign")
    @Tribe(name = "Supply")
    @Vertical(name = "TTD")
    @Test
    public void checkFilterCampaignByStatus() {
        Map<String, String> param = getParam("checkFilterCampaignByStatus");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        TTDInventoryManagementPage<?> ttdInventoryManagementPage = new TTDInventoryManagementPage<>(session);
        String username = param.get("username");
        String password = param.get("password");
        String statusIndexStr = param.get("statusIndex");
        if (statusIndexStr == null) {
            throw new IllegalArgumentException("Missing 'statusIndex' parameter for checkFilterCampaignByStatus test");
        }
        final int statusIndex;
        try {
            statusIndex = Integer.parseInt(statusIndexStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid 'statusIndex' parameter value: " + statusIndexStr, e);
        }

        step("The user logs in to the Admin dashboard");
        memberLoginPage.adminLoginViaCookie(username, password);
        memberLoginPage.isAdminLogin();

        step("The user navigates to TTD Admin page");
        ttdInventoryManagementPage.gotoToDoAdminPage();

        step("The user clicks on Campaign menu");
        ttdInventoryManagementPage.selectCampaignMenu();

        step("Select the status option dropdown");
        ttdInventoryManagementPage.chooseCampaignStatus(statusIndex);

        step("Click search button");
        ttdInventoryManagementPage.clickSearchCampaign();

        int totalAllCampaign = ttdInventoryManagementPage.countAllCampaign();
        int totalAllActiveCampaign = ttdInventoryManagementPage.countAllActiveCampaign();

        step("Verify when campaign status result is active");
        Assertion.assertThat("Verify all sorted campaign has same total with campaign before getting sorted",
                totalAllCampaign, equalTo(totalAllActiveCampaign));
    }

    @Author(names = {"Annisa"})
    @Module(name = "Campaign")
    @Tribe(name = "Supply")
    @Vertical(name = "TTD")
    @Test
    public void checkRetrieveCampaignList(){
        Map<String, String> param = getParam("checkRetrieveCampaignList");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        TTDInventoryManagementPage<?> ttdInventoryManagementPage = new TTDInventoryManagementPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String campaignName = param.get("campaignName");

        step("The user logs in to the Admin dashboard");
        memberLoginPage.adminLoginViaCookie(username, password);
        memberLoginPage.isAdminLogin();

        step("The user navigates to TTD Admin page");
        ttdInventoryManagementPage.gotoToDoAdminPage();

        step("The user clicks on Campaign menu");
        ttdInventoryManagementPage.selectCampaignMenu();

        step("Verify campaign list are retrieved on the page");
        Assertion.assertThat("First campaign name matches expected test data",
                ttdInventoryManagementPage.checkCampaignResultName(campaignName), is(true));
    }

    @Author(names = {"Manan Kumar", "Annisa"})
    @Module(name = "InventoryList")
    @Tribe(name = "Supply")
    @Vertical(name = "TTD")
    @Test
    public void testSearchTTDByProductName() {
        Map<String, String> param = getParam("testSearchTTDByProductName");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        TTDInventoryManagementPage<?> ttdInventoryMangementPage = new TTDInventoryManagementPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String productName = param.get("productName");
        String productUrl = param.get("productUrl");
        String supplierId = param.get("supplierId");
        String productID = param.get("productID");

        step("The user logs in to the Admin dashboard.");
        memberLoginPage.adminLoginViaCookie(username, password);
        memberLoginPage.isAdminLogin();

        step("The user navigates to TTD Admin page.");
        ttdInventoryMangementPage.gotoToDoAdminPage();

        step("The user clicks on Product List.");
        ttdInventoryMangementPage.selectProductList();

        step("The user enters the product name.");
        ttdInventoryMangementPage.enterProductNameInSearch(productName);

        step("The user clicks on Search button.");
        ttdInventoryMangementPage.clickSearchButton();

        step("The user verifies the first row of the result contains the correct product.");
        Assertion.assertThat("Product name is visible in the first row",
                ttdInventoryMangementPage.isFirstRowProductVisible(
                        "productName",productName, productUrl,productID,supplierId), is(true));
    }

    @Author(names = {"Manan Kumar", "Annisa"})
    @Module(name = "InventoryList")
    @Tribe(name = "Supply")
    @Vertical(name = "TTD")
    @Test
    public void testSearchTTDByProductURL() {
        Map<String, String> param = getParam("testSearchTTDByProductURL");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        TTDInventoryManagementPage<?> ttdInventoryMangementPage = new TTDInventoryManagementPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String productName = param.get("productName");
        String productUrl = param.get("productUrl");
        String supplierId = param.get("supplierId");
        String productID = param.get("productID");


        step("The user logs in to the Admin dashboard.");
        memberLoginPage.adminLoginViaCookie(username, password);
        memberLoginPage.isAdminLogin();

        step("The user navigates to TTD Admin page.");
        ttdInventoryMangementPage.gotoToDoAdminPage();

        step("The user clicks on Product List.");
        ttdInventoryMangementPage.selectProductList();

        step("The user selects the Full URL.");
        ttdInventoryMangementPage.enterFullURL(productUrl);

        step("The user clicks on Search button.");
        ttdInventoryMangementPage.clickSearchButton();

        step("The user verifies the first row of the result contains the correct product.");
        Assertion.assertThat("Product name is visible in the first row",
                ttdInventoryMangementPage.isFirstRowProductVisible(
                        "productUrl",productName, productUrl,productID,supplierId), is(true));
    }

    @Author(names = {"Manan Kumar", "Annisa"})
    @Module(name = "InventoryList")
    @Tribe(name = "Supply")
    @Vertical(name = "TTD")
    @Test
    public void testSearchTTDBySupplierProductID() {
        Map<String, String> param = getParam("testSearchTTDBySupplierProductID");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        TTDInventoryManagementPage<?> ttdInventoryMangementPage = new TTDInventoryManagementPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String productName = param.get("productName");
        String productUrl = param.get("productUrl");
        String supplierId = param.get("supplierId");
        String productID = param.get("productID");

        step("The user logs in to the Admin dashboard.");
        memberLoginPage.adminLoginViaCookie(username, password);
        memberLoginPage.isAdminLogin();

        step("The user navigates to TTD Admin page.");
        ttdInventoryMangementPage.gotoToDoAdminPage();

        step("The user clicks on Product List.");
        ttdInventoryMangementPage.selectProductList();

        step("The user enters the Supplier Product ID.");
        ttdInventoryMangementPage.enterSupplierProductIdInSearch(supplierId);

        step("The user selects the Supplier Product ID.");
        ttdInventoryMangementPage.enterSupplierProductID();

        step("The user clicks on Search button.");
        ttdInventoryMangementPage.clickSearchButton();

        step("The user verifies the first row of the result contains the correct product.");
        Assertion.assertThat("Supplier Product ID is visible in the first row",
                ttdInventoryMangementPage.isFirstRowProductVisible(
                        "supplierId",productName,productUrl,productID,supplierId), is(true));
    }

    @Author(names = {"Manan Kumar", "Annisa"})
    @Module(name = "InventoryList")
    @Tribe(name = "Supply")
    @Vertical(name = "TTD")
    @Test
    public void testSearchTTDByProductID() {
        Map<String, String> param = getParam("testSearchTTDByProductID");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        TTDInventoryManagementPage<?> ttdInventoryMangementPage = new TTDInventoryManagementPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String productName = param.get("productName");
        String productUrl = param.get("productUrl");
        String supplierId = param.get("supplierId");
        String productID = param.get("productID");

        step("The user logs in to the Admin dashboard.");
        memberLoginPage.adminLoginViaCookie(username, password);
        memberLoginPage.isAdminLogin();

        step("The user navigates to TTD Admin page.");
        ttdInventoryMangementPage.gotoToDoAdminPage();

        step("The user clicks on Product List.");
        ttdInventoryMangementPage.selectProductList();

        step("The user enters the Product ID.");
        ttdInventoryMangementPage.enterSupplierProductIdInSearch(productID);

        step("The user selects the Product ID.");
        ttdInventoryMangementPage.productID();

        step("The user clicks on Search button.");
        ttdInventoryMangementPage.clickSearchButton();

        step("The user verifies the first row of the result contains the correct product.");
        Assertion.assertThat("Supplier Product ID is visible in the first row",
                ttdInventoryMangementPage.isFirstRowProductVisible(
                        "productID",productName,productUrl,productID,supplierId), is(true));
    }

    @Author(names = {"Nasha"})
    @Module(name = "ProductList")
    @Tribe(name = "CMS")
    @Vertical(name = "TTD")
    @Test
    public void testCheckWhenUserSelectTimeTypeSession() {
        Map<String, String> param = getParam("testCheckWhenUserSelectTimeTypeSession");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        String cmsUsername = param.get("cmsUsername");
        String cmsPassword = param.get("cmsPassword");
        String productName = param.get("productName");
        String name = "aut_schedule_session_" + CoreFunctions.randomName();
        String value = param.get("value");
        String description = param.get("description");
        String min = param.get("min");
        String max = param.get("max");
        
        String quota = param.get("quota");
        String hours = param.get("hours");
        String minutes = param.get("minutes");
        String titleEn = param.get("titleEn");
        String titleId = param.get("titleId");
        String detailsEn = param.get("detailsEn");
        String detailsId = param.get("detailsId");

        CMSLoginPageWeb<?> cmsLoginPageWeb = CMSLoginPageWeb.getInstance(session);
        CMSInventoryManagementProductListPageWeb<?> cmsInventoryManagementProductListPageWeb = CMSInventoryManagementProductListPageWeb.getInstance(session);

        step("Go to CMS login page and login");
        cmsLoginPageWeb.loginAsSpecificUser(cmsUsername, cmsPassword);

        step("Go to Product List page");
        cmsInventoryManagementProductListPageWeb.goToPage();

        step("Search for the product");
        cmsInventoryManagementProductListPageWeb.inputSearchBar(productName);
        cmsInventoryManagementProductListPageWeb.pressSearchButton();
        cmsInventoryManagementProductListPageWeb.clickEditProductButton();

        step("Go to Add Package section");
        cmsInventoryManagementProductListPageWeb.addPackageButton();

        step("Fill the general details of the package");
        cmsInventoryManagementProductListPageWeb.fillPackageDetails(name, value);

        step("User fills the minimum and maximum pax");
        cmsInventoryManagementProductListPageWeb.fillMinimumAndMaximumPax(min, max, description, value);

        step("User fills the quota");
        cmsInventoryManagementProductListPageWeb.enterQuota(value);

        step("User goes to Package schedule section");
        cmsInventoryManagementProductListPageWeb.clickSearchInput(name);
        cmsInventoryManagementProductListPageWeb.goToPackageSchedule();
        
        step("User adds a schedule for the package with session");
        cmsInventoryManagementProductListPageWeb.addScheduleSession(quota, hours, minutes, titleEn, titleId, detailsEn, detailsId);

        step("User verifies that the schedule with session is added");
        boolean isScheduleAdded = cmsInventoryManagementProductListPageWeb.isScheduleAdded();
        Assertion.assertThat("Schedule with session is added successfully", isScheduleAdded, is(true));

        step("[Post Condition] User deletes the schedule");
        cmsInventoryManagementProductListPageWeb.deleteSchedule();

        step("[Post Condition] User goes back to the package section to delete the package");
        cmsInventoryManagementProductListPageWeb.switchToEditProductWindow();

        step("[Post Condition] User deletes the package");
        cmsInventoryManagementProductListPageWeb.clickDeletePackageButton();

        step("Log out");
        cmsLoginPageWeb.logoutCMS();
    }

    @Author(names = { "Nasha" })
    @Module(name = "Promo&Loyalty")
    @Tribe(name = "CMS")
    @Vertical(name = "TTD")
    @Test
    public void verifyTheCreateBundleFlow() {
        Map<String, String> param = getParam("verifyTheCreateBundleFlow");
        BaseTest.paramsThreadLocal.set(param);

        String username = param.get("username");
        String password = param.get("password");
        String productName = param.get("productName");
        String packageNameMain = param.get("packageNameMain");
        String packageNameAddon = param.get("packageNameAddon");
        String productId = param.get("productId");
        String tiketSubsidy = param.get("tiketSubsidy");
        String partnerSubsidy = param.get("partnerSubsidy");

        Session<?> session = BaseTest.sessionThreadLocal.get();
        CMSLoginPageWeb<?> cmsLoginPageWeb = CMSLoginPageWeb.getInstance(session);
        CMSPromoAndLoyaltyBundlePageWeb<?> cmsPromoAndLoyaltyBundlePageWeb = CMSPromoAndLoyaltyBundlePageWeb.getInstance(session);

        step("Login as a user");
        cmsLoginPageWeb.loginAsSpecificUser(username, password);

        step("Go to Promo and Loyalty page");
        cmsPromoAndLoyaltyBundlePageWeb.goToPage();
        cmsPromoAndLoyaltyBundlePageWeb.clickPromoAndLoyaltySidebar();

        step("Click bundle");
        cmsPromoAndLoyaltyBundlePageWeb.goToBundlePage();

        step("Click create new bundle");
        cmsPromoAndLoyaltyBundlePageWeb.clickCreateNewBundle();

        step("Fill general details for bundle");
        String bundleName = "aut_bundle_" + CoreFunctions.randomName();
        cmsPromoAndLoyaltyBundlePageWeb.fillBundleGeneralDetails(bundleName);

        step("Add Main Product configuration");
        cmsPromoAndLoyaltyBundlePageWeb.addMainProductConfiguration(productName, packageNameMain);

        step("Add Add-ons Product configuration");
        cmsPromoAndLoyaltyBundlePageWeb.addAddonsProductConfiguration(productName, packageNameAddon);
        cmsPromoAndLoyaltyBundlePageWeb.inputTiketSubsidy(tiketSubsidy);
        cmsPromoAndLoyaltyBundlePageWeb.inputPartnerSubsidy(partnerSubsidy);

        step("Submit Bundle");
        cmsPromoAndLoyaltyBundlePageWeb.submitBundle();

        step("New bundle should be created & displayed on the screen Bundle Promotion List");
        Assertion.assertThat("Bundle is created successfully", cmsPromoAndLoyaltyBundlePageWeb.isBundleCreated(productName, packageNameMain), is(true));

        step("Clean up the created bundle");
        cmsPromoAndLoyaltyBundlePageWeb.deleteBundle(productId, packageNameMain);

        step("Logout from CMS");
        cmsLoginPageWeb.logoutCMS();
    }

    @Author(names = {"Prachi Upadhyay"})
    @Module(name = "Promotion")
    @Tribe(name = "TTD-Merchant")
    @Vertical(name = "TTD")
    @Test
    public void testVerifyBulkCreatePromotion() throws Exception {
        Map<String, String> param = getParam("testVerifyBulkCreatePromotion");
        BaseTest.paramsThreadLocal.set(param);

        String cmsUsername = param.get("cmsUsername");
        String cmsPassword = param.get("cmsPassword");
        String promotionType = param.get("promotionType");
        String promotionType2 = param.get("promotionType2");
        String targetSelection = param.get("targetSelection");
        String targetCountries = param.get("targetCountries");
        String startTime = param.get("startTime");
        String endTime = param.get("endTime");
        String downloadedFileName = param.get("downloadedFileName");
        int daysToStartDate = ThreadLocalRandom.current().nextInt(10, 20);
        int daysToEndDate = ThreadLocalRandom.current().nextInt(20, 30);
        String currentDate = CoreFunctions.getCurrentLongDate();
        String startDate = CoreFunctions.addToLongDate(currentDate,daysToStartDate);
        String endDate = CoreFunctions.addToLongDate(currentDate,daysToEndDate);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        CMSLoginPageWeb<?> cmsLoginPageWeb = CMSLoginPageWeb.getInstance(session);
        TTDInventoryManagementPage<?> ttdInventoryManagementPage = new TTDInventoryManagementPage<>(session);

        CMSPromoAndLoyaltyPromotionPageWeb<?> cmsPromotionPageWeb = CMSPromoAndLoyaltyPromotionPageWeb.getInstance(session);

        step("Login in TTD CMS");
        cmsLoginPageWeb.loginAsSpecificUser(cmsUsername, cmsPassword);

        step("Navigate to Promotion page");
        ttdInventoryManagementPage.gotoToDoAdminPage();
        cmsPromotionPageWeb.goToPromotionMenu();

        step("Click Bulk Create");
        cmsPromotionPageWeb.clickBulkCreateButton();

        step("Precondition: Select product and Download format file");
        File formatFile = cmsPromotionPageWeb.downloadFormatFile(downloadedFileName);

        try {
            step("Precondition: Complete the file by filling the required column in the sheet");
            cmsPromotionPageWeb.fillPromotionFormatFile(formatFile, param);


            // 1. FLOW FOR MULTICHANNEL
            step("Switch to Upload File Tab for Multichannel");
            cmsPromotionPageWeb.goToUploadTab();
            step("Upload file");
            cmsPromotionPageWeb.uploadPromotionFile(formatFile.getAbsolutePath());
            step("Select Promotion Type and Targeted Channel (Multichannel)");
            cmsPromotionPageWeb.selectPromotionTypeAndTarget(promotionType, targetSelection);
            step("Fill in Dates and Times");
            cmsPromotionPageWeb.fillBookingPeriodDatesAndTimes(startDate,startTime,endDate, endTime);
            step("Click Continue button");
            cmsPromotionPageWeb.clickContinueButton();
            step("Click Yes, Save Promotions (Multichannel)");
            cmsPromotionPageWeb.clickYesSavePromotionsButton();

            step("Verify Multichannel Promotion is successfully created");
            Assertion.assertThat("Verify success snackbar/alert is displayed", cmsPromotionPageWeb.isSuccessAlertDisplayed(), is(true));

            cmsPromotionPageWeb.waitForSnackbarToDisappear();

            // 2. FLOW FOR GEOFENCING
            step("Click Bulk Create again for Geofencing");
            cmsPromotionPageWeb.clickBulkCreateButton(); // Re-open the modal
            step("Switch to Upload File Tab for Geofencing");
            cmsPromotionPageWeb.goToUploadTab();
            step("Upload file again");
            cmsPromotionPageWeb.uploadPromotionFile(formatFile.getAbsolutePath());
            step("Select Promotion Type and Targeted Country (Geofencing)");
            cmsPromotionPageWeb.selectPromotionTypeAndTargetGeofencing(promotionType2, targetCountries);
            step("Fill in Dates and Times");
            cmsPromotionPageWeb.fillBookingPeriodDatesAndTimes(startDate,startTime,endDate, endTime);
            step("Click Continue button");
            cmsPromotionPageWeb.clickContinueButton();
            step("Click Yes, Save Promotions (Geofencing)");
            cmsPromotionPageWeb.clickYesSavePromotionsButton();

            step("Verify Geofencing Promotion is successfully created");
            Assertion.assertThat("Verify success snackbar/alert is displayed", cmsPromotionPageWeb.isSuccessAlertDisplayed(), is(true));

        } finally {
            step("Postcondition: Clean up the downloaded file");
            if (formatFile != null && formatFile.exists()) {
                CoreFunctions.deleteFile(formatFile.getAbsolutePath());
                System.out.println("File deleted successfully.");
            }
        }

        step("Logout from CMS");
        cmsLoginPageWeb.logoutCMS();
    }

    @Author(names = {"Prachi Upadhyay"})
    @Module(name = "Promotion")
    @Tribe(name = "TTD-Merchant")
    @Vertical(name = "TTD")
    @Test
    public void testVerifyEarlybookCreateUpdateDelete() throws Exception {
        Map<String, String> param = getParam("testVerifyEarlybookCreateUpdateDelete");
        BaseTest.paramsThreadLocal.set(param);

        String cmsUsername = param.get("cmsUsername");
        String cmsPassword = param.get("cmsPassword");
        String promotionType = param.get("promotionType");
        String promotionType2 = param.get("promotionType2");
        String targetSelection = param.get("targetSelection");
        String targetCountries = param.get("targetCountries");
        String startTime = param.get("startTime");
        String endTime = param.get("endTime");
        String downloadedFileName = param.get("downloadedFileName");
        int daysToStartDate = ThreadLocalRandom.current().nextInt(10, 20);
        int daysToEndDate = ThreadLocalRandom.current().nextInt(20, 30);
        String currentDate = CoreFunctions.getCurrentLongDate();
        String startDate = CoreFunctions.addToLongDate(currentDate,daysToStartDate);
        String endDate = CoreFunctions.addToLongDate(currentDate,daysToEndDate);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        CMSLoginPageWeb<?> cmsLoginPageWeb = CMSLoginPageWeb.getInstance(session);
        TTDInventoryManagementPage<?> ttdInventoryManagementPage = new TTDInventoryManagementPage<>(session);

        CMSPromoAndLoyaltyPromotionPageWeb<?> cmsPromotionPageWeb = CMSPromoAndLoyaltyPromotionPageWeb.getInstance(session);

        step("Login in TTD CMS");
        cmsLoginPageWeb.loginAsSpecificUser(cmsUsername, cmsPassword);

        step("Navigate to Promotion page");
        ttdInventoryManagementPage.gotoToDoAdminPage();
        cmsPromotionPageWeb.goToPromotionMenu();

        step("Click Bulk Create");
        cmsPromotionPageWeb.clickBulkCreateButton();

        step("Precondition: Select product and Download format file");
        File formatFile = cmsPromotionPageWeb.downloadFormatFile(downloadedFileName);

        try {
            step("Precondition: Complete the file by filling the required column in the sheet");
            cmsPromotionPageWeb.fillPromotionFormatFile(formatFile, param);


            // 1. FLOW FOR MULTICHANNEL
            step("Switch to Upload File Tab for Multichannel");
            cmsPromotionPageWeb.goToUploadTab();
            step("Upload file");
            cmsPromotionPageWeb.uploadPromotionFile(formatFile.getAbsolutePath());
            step("Select Promotion Type and Targeted Channel (Multichannel)");
            cmsPromotionPageWeb.selectPromotionTypeAndTarget(promotionType, targetSelection);
            step("Fill in Dates and Times");
            cmsPromotionPageWeb.fillBookingPeriodDatesAndTimes(startDate,startTime,endDate, endTime);
            step("Click Continue button");
            cmsPromotionPageWeb.clickContinueButton();
            step("Click Yes, Save Promotions (Multichannel)");
            cmsPromotionPageWeb.clickYesSavePromotionsButton();

            step("Verify Multichannel Promotion is successfully created");
            Assertion.assertThat("Verify success snackbar/alert is displayed", cmsPromotionPageWeb.isSuccessAlertDisplayed(), is(true));

            cmsPromotionPageWeb.waitForSnackbarToDisappear();

            // 2. FLOW FOR GEOFENCING
            step("Click Bulk Create again for Geofencing");
            cmsPromotionPageWeb.clickBulkCreateButton(); // Re-open the modal
            step("Switch to Upload File Tab for Geofencing");
            cmsPromotionPageWeb.goToUploadTab();
            step("Upload file again");
            cmsPromotionPageWeb.uploadPromotionFile(formatFile.getAbsolutePath());
            step("Select Promotion Type and Targeted Country (Geofencing)");
            cmsPromotionPageWeb.selectPromotionTypeAndTargetGeofencing(promotionType2, targetCountries);
            step("Fill in Dates and Times");
            cmsPromotionPageWeb.fillBookingPeriodDatesAndTimes(startDate,startTime,endDate, endTime);
            step("Click Continue button");
            cmsPromotionPageWeb.clickContinueButton();
            step("Click Yes, Save Promotions (Geofencing)");
            cmsPromotionPageWeb.clickYesSavePromotionsButton();

            step("Verify Geofencing Promotion is successfully created");
            Assertion.assertThat("Verify success snackbar/alert is displayed", cmsPromotionPageWeb.isSuccessAlertDisplayed(), is(true));

        } finally {
            step("Postcondition: Clean up the downloaded file");
            if (formatFile != null && formatFile.exists()) {
                CoreFunctions.deleteFile(formatFile.getAbsolutePath());
                System.out.println("File deleted successfully.");
            }
        }

        step("Logout from CMS");
        cmsLoginPageWeb.logoutCMS();
    }

    @Author(names = {"Prachi Upadhyay"})
    @Module(name = "Promotion")
    @Tribe(name = "TTD-Merchant")
    @Vertical(name = "TTD")
    @Test
    public void testVerifyGeofencingCreateUpdateDelete() throws Exception {
        Map<String, String> param = getParam("testVerifyGeofencingCreateUpdateDelete");
        BaseTest.paramsThreadLocal.set(param);

        String cmsUsername = param.get("cmsUsername");
        String cmsPassword = param.get("cmsPassword");
        String promotionType = param.get("promotionType");
        String targetCountries = param.get("targetCountries");
        String startTime = param.get("startTime");
        String endTime = param.get("endTime");
        String partnerSubsidy = param.get("partnerSubsidy");
        String partnerSubsidy2 = param.get("partnerSubsidy2");

        // Generating dynamic dates
        int daysToStartDate = ThreadLocalRandom.current().nextInt(10, 20);
        int daysToEndDate = ThreadLocalRandom.current().nextInt(20, 30);
        String currentDate = CoreFunctions.getCurrentLongDate();
        String startDate = CoreFunctions.addToLongDate(currentDate, daysToStartDate);
        String endDate = CoreFunctions.addToLongDate(currentDate, daysToEndDate);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        CMSLoginPageWeb<?> cmsLoginPageWeb = CMSLoginPageWeb.getInstance(session);
        TTDInventoryManagementPage<?> ttdInventoryManagementPage = new TTDInventoryManagementPage<>(session);
        CMSPromoAndLoyaltyPromotionPageWeb<?> cmsPromotionPageWeb = CMSPromoAndLoyaltyPromotionPageWeb.getInstance(session);

        step("Login in TTD CMS");
        cmsLoginPageWeb.loginAsSpecificUser(cmsUsername, cmsPassword);

        step("Navigate to Promotion page");
        ttdInventoryManagementPage.gotoToDoAdminPage();
        cmsPromotionPageWeb.goToPromotionMenu();

        step("Click Create New and Select Promotion Type");
        cmsPromotionPageWeb.clickCreateNewPromotionOption(promotionType);

        // ==========================================
        // CREATE FLOW
        // ==========================================
        step("Select active product, active package, and click Add Things To Do");
        String activeProductName = cmsPromotionPageWeb.searchAndSelectActiveProductAndPackage();

        step("Select Targeted Country");
        cmsPromotionPageWeb.selectGeofencingTargetCountries(targetCountries);

        step("Fill in Start and End date and time of Booking Period");
        cmsPromotionPageWeb.fillBookingPeriodDatesAndTimes(startDate, startTime, endDate, endTime);

        step("Fill in Partner Subsidy");
        cmsPromotionPageWeb.fillGeofencingPartnerSubsidy(partnerSubsidy);

        step("Click Save - Save Promotion");
        cmsPromotionPageWeb.submitAndSavePromotion();

        step("Verify newly created promotion was successful");
        Assertion.assertThat("Verify success snackbar/alert is displayed", cmsPromotionPageWeb.isSuccessAlertDisplayed(), is(true));
        cmsPromotionPageWeb.waitForSnackbarToDisappear();

        // ==========================================
        // EDIT FLOW
        // ==========================================
        step("Navigate to Geofencing Tab and Search by product name");
        cmsPromotionPageWeb.clickGeofencingTab();
        cmsPromotionPageWeb.searchPromotionByName(activeProductName);

        step("Click Edit option, Edit Partner Subsidy, and Save");
        cmsPromotionPageWeb.clickEditOnFirstRow();

        cmsPromotionPageWeb.fillGeofencingPartnerSubsidy(partnerSubsidy2);
        cmsPromotionPageWeb.submitAndSavePromotion();

        step("Verify promotion update was successful");
        Assertion.assertThat("Verify edit success snackbar is displayed", cmsPromotionPageWeb.isSuccessAlertDisplayed(), is(true));
        cmsPromotionPageWeb.waitForSnackbarToDisappear();

        // ==========================================
        // DELETE FLOW
        // ==========================================
        step("Click Delete from the option and Verify");
        cmsPromotionPageWeb.searchPromotionByName(activeProductName);
        cmsPromotionPageWeb.clickDeleteOnFirstRowAndConfirm();

        Assertion.assertThat("Verify delete success snackbar is displayed", cmsPromotionPageWeb.isSuccessAlertDisplayed(), is(true));
    }

    @Author(names = {"Prachi Upadhyay"})
    @Module(name = "Promotion")
    @Tribe(name = "TTD-Merchant")
    @Vertical(name = "TTD")
    @Test
    public void testVerifyMultichannelCreateUpdateDelete() throws Exception {
        Map<String, String> param = getParam("testVerifyMultichannelCreateUpdateDelete");
        BaseTest.paramsThreadLocal.set(param);

        String cmsUsername = param.get("cmsUsername");
        String cmsPassword = param.get("cmsPassword");
        String promotionType = param.get("promotionType"); // Should be "Multichannel" in sheet
        String startTime = param.get("startTime");
        String endTime = param.get("endTime");

        // Create & Edit flow variables
        String partnerSubsidy = param.get("partnerSubsidy");
        String partnerSubsidy2 = param.get("partnerSubsidy2");

        // Generating dynamic dates
        int daysToStartDate = ThreadLocalRandom.current().nextInt(10, 20);
        int daysToEndDate = ThreadLocalRandom.current().nextInt(20, 30);
        String currentDate = CoreFunctions.getCurrentLongDate();
        String startDate = CoreFunctions.addToLongDate(currentDate, daysToStartDate);
        String endDate = CoreFunctions.addToLongDate(currentDate, daysToEndDate);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        CMSLoginPageWeb<?> cmsLoginPageWeb = CMSLoginPageWeb.getInstance(session);
        TTDInventoryManagementPage<?> ttdInventoryManagementPage = new TTDInventoryManagementPage<>(session);
        CMSPromoAndLoyaltyPromotionPageWeb<?> cmsPromotionPageWeb = CMSPromoAndLoyaltyPromotionPageWeb.getInstance(session);

        step("Login in TTD CMS");
        cmsLoginPageWeb.loginAsSpecificUser(cmsUsername, cmsPassword);

        step("Navigate to Promotion page");
        ttdInventoryManagementPage.gotoToDoAdminPage();
        cmsPromotionPageWeb.goToPromotionMenu();

        step("Click Create New and Select Promotion Type");
        cmsPromotionPageWeb.clickCreateNewPromotionOption(promotionType);

        // ==========================================
        // CREATE FLOW
        // ==========================================
        step("Select active product, active package, and click Add Things To Do");
        String activeProductName = cmsPromotionPageWeb.searchAndSelectActiveProductAndPackage();

        step("Fill in Start and End date and time of Booking Period");
        cmsPromotionPageWeb.fillBookingPeriodDatesAndTimes(startDate, startTime, endDate, endTime);

        step("Fill in Partner Subsidy");
        cmsPromotionPageWeb.fillMultichannelPartnerSubsidy(partnerSubsidy);

        step("Click Save - Save Promotion");
        cmsPromotionPageWeb.submitAndSavePromotion();

        step("Verify newly created promotion was successful");
        Assertion.assertThat("Verify success snackbar/alert is displayed", cmsPromotionPageWeb.isSuccessAlertDisplayed(), is(true));
        cmsPromotionPageWeb.waitForSnackbarToDisappear();

        // ==========================================
        // EDIT FLOW
        // ==========================================
        step("Navigate to Multichannel Tab and Search by product name");
        cmsPromotionPageWeb.clickMultichannelTab();
        cmsPromotionPageWeb.searchPromotionByName(activeProductName);

        step("Click Edit option, Edit Partner Subsidy, and Save");
        cmsPromotionPageWeb.clickEditOnFirstRow();

        cmsPromotionPageWeb.fillMultichannelPartnerSubsidy(partnerSubsidy2);
        cmsPromotionPageWeb.submitAndSavePromotion();

        step("Verify promotion update was successful");
        Assertion.assertThat("Verify edit success snackbar is displayed", cmsPromotionPageWeb.isSuccessAlertDisplayed(), is(true));
        cmsPromotionPageWeb.waitForSnackbarToDisappear();

        // ==========================================
        // DELETE FLOW
        // ==========================================
        step("Click Delete from the option and Verify");
        cmsPromotionPageWeb.searchPromotionByName(activeProductName);
        cmsPromotionPageWeb.clickDeleteOnFirstRowAndConfirm();

        Assertion.assertThat("Verify delete success snackbar is displayed", cmsPromotionPageWeb.isSuccessAlertDisplayed(), is(true));
    }

}