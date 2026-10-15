package com.tiket.web.mWeb.test.ttd;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.model.FilterType;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.web.WebTest;
import com.tiket.web.dWeb.page.payment.PaymentPage;
import com.tiket.web.page.common.home.HomePageWeb;
import com.tiket.web.page.common.home.LoginPageWeb;
import com.tiket.web.page.order.OrderDetail.OrderDetailWeb;
import com.tiket.web.page.order.OrderList.OrderListWeb;
import com.tiket.web.page.ttd.TtdBookingFormPage.TtdBookingFormPageWeb;
import com.tiket.web.page.ttd.TtdLandingPage.TtdLandingPageMweb;
import com.tiket.web.page.ttd.TtdLandingPage.TtdLandingPageWeb;
import com.tiket.web.page.ttd.TtdPackageDetailPage.TtdPackageDetailPageWeb;
import com.tiket.web.page.ttd.TtdProductDetailPage.TtdProductDetailPageMweb;
import com.tiket.web.page.ttd.TtdProductDetailPage.TtdProductDetailPageWeb;
import com.tiket.web.page.ttd.TtdSearchResultPage.TtdSearchResultPageWeb;

import com.tiket.web.page.ttd.TtdSelectedPackageDetailPage.TtdSelectedPackageDetailPageWeb;
import org.testng.annotations.Test;

import javax.security.auth.login.FailedLoginException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;

public class TtdTest extends WebTest {

    @Author(names = {"Alden"})
    @Module(name = "SearchPage")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void checkTopKeywordsCanBeClicked() {
        Map<String, String> param = getParam("checkTopKeywordsCanBeClicked");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        TtdLandingPageWeb<?> ttdLandingPageWeb = TtdLandingPageWeb.getInstance(session);

        step("Open home page");
        homePage.goToHomePage();

        step("Go to TTD Landing Page");
        ttdLandingPageWeb.goToTtdLandingPage();

        step("Click top search bar on landing page");
        ttdLandingPageWeb.clickSearch();

        step("Assert that top keywords can be clicked");
        Assertion.assertThat("Top Keywords can be clicked", ttdLandingPageWeb.checkClickTopKeyword(), is(true));
    }

    @Author(names = {"Alden"})
    @Module(name = "Home Page")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void searchEventRandomPriorityNonLogin() {
        Map<String, String> param = getParam("searchEventRandomPriorityNonLogin");
        BaseTest.paramsThreadLocal.set(param);
        int randomRank = CoreFunctions.generateRandomInt(1, 10);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        TtdLandingPageWeb<?> ttdLandingPageWeb = TtdLandingPageWeb.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);

        step("Open home page");
        homePage.goToHomePage();

        step("Go to TTD Landing Page");
        ttdLandingPageWeb.goToTtdLandingPage();

        step("Check rank of the popular event");
        ttdLandingPageWeb.openSearchInput();
        Assertion.assertThat("popular event section on search suggestion page with 10 events having count 1-10", ttdLandingPageWeb.checkPopularEventsRank(10), is(true));

        step("Choose random rank of the popular event");
        String productNameSearchPage = ttdLandingPageWeb.selectPopularEvents(randomRank);

        step("Verify product name");
        String productNamePDP = ttdProductDetailPageWeb.getProductName();
        Assertion.assertThat("Product name is the same", productNamePDP, is(productNameSearchPage));
    }


    @Author(names = {"Alden"})
    @Module(name = "SearchResult")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void checkSRPFeatureFilter() {
        Map<String, String> param = getParam("checkSRPFeatureFilter");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        TtdLandingPageWeb<?> ttdLandingPageWeb = TtdLandingPageWeb.getInstance(session);
        TtdSearchResultPageWeb<?> ttdSearchResultPageWeb = TtdSearchResultPageWeb.getInstance(session);

        String category = param.get("category");

        step("Open home page");
        homePage.goToHomePage();

        step("change language");
        homePage.selectEnglishLanguage();

        step("Go to TTD Landing Page");
        ttdLandingPageWeb.goToTtdLandingPage();

        step("Go To SRP");
        ttdLandingPageWeb.chooseCategory(false, category);
        ttdLandingPageWeb.clickDownloadApp();
        ttdSearchResultPageWeb.resetFilter();

        step("get available filters");
        List<String> availableFilters = ttdSearchResultPageWeb.getFilterByCategory("Features");

        for (String featureFilter : availableFilters) {
            step("hide popup if visible");
            ttdLandingPageWeb.clickDownloadApp();

            step("apply feature filter");
            ttdSearchResultPageWeb.chooseFilter(featureFilter, FilterType.FEATURES);

            step("verify filter is applied");
            Assertion.assertThat("filter " + featureFilter + " is applied", ttdSearchResultPageWeb.getSelectedFilterInProductCard(featureFilter).size(), greaterThan(0));

            step("reset filter");
            ttdSearchResultPageWeb.resetFilter();
        }
    }

    @Author(names = {"Manan Kumar"})
    @Module(name = "PDP")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void testVerifyVoucherDetailsAndClaimButton() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testVerifyVoucherDetailsAndClaimButton");
        BaseTest.paramsThreadLocal.set(param);

        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
        String product = param.get("product");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));

        Session<?> session = BaseTest.sessionThreadLocal.get();

        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        TtdLandingPageWeb<?> ttdLandingPageWeb = TtdLandingPageWeb.getInstance(session);

        step("Open home page");
        homePage.goToHomePage();

        step("Open as logged in user");
        loginPage.loginIfNotWithSameEmail(username, password, otp, retryCounter);

        step("Go to TTD Landing Page");
        ttdLandingPageWeb.goToTtdLandingPage();

        step("Search for product.");
        ttdLandingPageWeb.searchProduct(product);

        step("User Open product in PDP");
        ttdLandingPageWeb.openSpecialPackages();

        step("User Set Ticket Quantity.");
        ttdLandingPageWeb.setTicketQuantity();

        step("User Clicks on Book Button");
        ttdLandingPageWeb.clickBookButton();

        step("Verify user claims voucher.");
        Assertion.assertThat("Verify POPUP is Appeared.", ttdLandingPageWeb.verifyPOPUp(), is(true));
    }

    @Author(names = {"Manan Kumar"})
    @Module(name = "SearchResult")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void testFirstCheckPackageAutomaticSeating() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testFirstCheckPackageAutomaticSeating");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        TtdLandingPageWeb<?> ttdLandingPageWeb = TtdLandingPageWeb.getInstance(session);
        TtdLandingPageMweb<?> ttdLandingPageMweb = (TtdLandingPageMweb<?>) TtdLandingPageWeb.getInstance(session);

        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
        String product = param.get("product");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));

        step("Open home page");
        homePage.goToHomePage();

        step("Open as logged in user");
        loginPage.loginIfNotWithSameEmail(username, password, otp, retryCounter);

        step("Go to TTD Landing Page");
        ttdLandingPageWeb.goToTtdLandingPage();

        step("User search for the product.");
        ttdLandingPageMweb.inputSearchKeyword(product);

        step("User Open Product");
        ttdLandingPageWeb.openProduct();

        step("User Select and open booking form");
        ttdLandingPageWeb.clickSelectAndBook();

        step("Verify Refund label present");
        Assertion.assertThat("Refund label present", ttdLandingPageWeb.isRefundLabelPresent(), is(true));

        step("Verify select label present");
        Assertion.assertThat("Check select labels present.",ttdLandingPageWeb.isSelectLabelPresent(), is(true));

        step("User Fill details for booking");
        ttdLandingPageWeb.detailAutoFill();

        step("User completes the booking");
        ttdLandingPageWeb.completeBooking();
    }

    @Author(names = {"Annisa"})
    @Module(name = "PDP")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void checkOrderUneligibleLoyalty() throws Exception {
        Map<String, String> param = getParam("checkOrderUneligibleLoyalty");
        BaseTest.paramsThreadLocal.set(param);
        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String productName = param.get("productName");
        String packageName = param.get("packageName");

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        TtdLandingPageWeb<?> ttdLandingPageWeb = TtdLandingPageWeb.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);
        TtdProductDetailPageMweb<?> ttdProductDetailPageMweb = new TtdProductDetailPageMweb(session);

        step("Open home page");
        homePage.goToHomePage();

        step("Open as logged in user");
        loginPage.loginIfNotWithSameEmail(username, password, otp, retryCounter);

        step("Go to TTD Landing Page");
        ttdLandingPageWeb.goToTtdLandingPage();

        step("Search and select product item");
        ttdLandingPageWeb.inputSearchKeyword(productName);
        ttdLandingPageWeb.selectRecommendedActivity(productName);

        step("Click details on Selected Ticket");
        ttdProductDetailPageWeb.openDetailPackage(packageName, 3);

        step("Check whether user's tier is eligible to purchase package");
        ttdProductDetailPageMweb.checkEligiblePackage(packageName, 3);
    }

    @Author(names = {"Manan Kumar"})
    @Module(name = "SearchResult")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void verifySearchAndRecentKeywords() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("verifySearchAndRecentKeywords");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        TtdLandingPageWeb<?> ttdLandingPageWeb = TtdLandingPageWeb.getInstance(session);
        TtdLandingPageMweb<?> ttdLandingPageMweb = (TtdLandingPageMweb<?>) TtdLandingPageWeb.getInstance(session);

        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
        String product = param.get("product");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));

        step("Open home page");
        homePage.goToHomePage();

        step("Open as logged in user");
        loginPage.loginIfNotWithSameEmail(username, password, otp, retryCounter);

        step("Go to TTD Landing Page");
        ttdLandingPageWeb.goToTtdLandingPage();

        step("User search for the product.");
        ttdLandingPageMweb.inputSearchKeyword(product);

        step("User Open Product");
        ttdLandingPageWeb.openProduct();

        step("User Again go to TTD landing page");
        ttdLandingPageWeb.goToTtdLandingPage();

        step("Verify product in  Recent Search list");
        Assertion.assertThat("Is product present.", ttdLandingPageWeb.verifyProductInRecentSearch(product), is(true));
    }

    @Author(names = {"Alden"})
    @Module(name = "PDP")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void checkBreadcrumbAreaPDP() throws Exception {
        Map<String, String> param = getParam("checkBreadcrumbAreaPDP");
        BaseTest.paramsThreadLocal.set(param);

        String productName = param.get("productName");
        String area = param.get("area");

        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        TtdLandingPageWeb<?> ttdLandingPageWeb = TtdLandingPageWeb.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);
        TtdSearchResultPageWeb<?> ttdSearchResultPageWeb = TtdSearchResultPageWeb.getInstance(session);

        step("Open home page");
        homePage.goToHomePage();

        step("Go to TTD Landing Page");
        ttdLandingPageWeb.goToTtdLandingPage();

        step("Search and select product item");
        ttdProductDetailPageWeb.openPdpUrl(productName.toLowerCase().replace(' ', '-'));
        Assertion.assertThat("Product is selected", ttdProductDetailPageWeb.getProductName(), is(productName));

        step("Click area breadcrumb");
        ttdProductDetailPageWeb.clickAreaBreadcrumb(area);
        Assertion.assertThat("Search result page for area is opened", ttdSearchResultPageWeb.getSearchLocation(), is(area));
    }

    @Author(names = {"Manan Kumar"})
    @Module(name = "SearchResult")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void testChildCampaignVisibleInMaster() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testChildCampaignVisibleInMaster");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        TtdLandingPageWeb<?> ttdLandingPageWeb = TtdLandingPageWeb.getInstance(session);

        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
        String product = param.get("product");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));

        step("Open home page");
        homePage.goToHomePage();

        step("Open as logged in user");
        loginPage.loginIfNotWithSameEmail(username, password, otp, retryCounter);

        step("Go to TTD Landing Page");
        ttdLandingPageWeb.goToTtdLandingPage();

        step("User navigate to the master product");
        ttdLandingPageWeb.openProductViaURL(product);

        step("User verify campaign label is available for master product");
        Assertion.assertThat("Verify campaign label", ttdLandingPageWeb.isCampaignPresent(), is(true));
    }

    @Author(names = {"Annisa"})
    @Module(name = "PDP")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void checkBxgyQuotaRunsOut() throws Exception {
        Map<String, String> param = getParam("checkBxgyQuotaRunsOut");
        BaseTest.paramsThreadLocal.set(param);
        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String productName = param.get("productName");
        String packageName = param.get("packageName");
        int quantity = Integer.parseInt(param.get("quantity"));
        int ticketIndex = Integer.parseInt(param.get("ticketIndex"));

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        TtdLandingPageWeb<?> ttdLandingPageWeb = TtdLandingPageWeb.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);
        TtdProductDetailPageMweb<?> ttdProductDetailPageMweb = new TtdProductDetailPageMweb(session);

        step("Open home page");
        homePage.goToHomePage();

        step("Open as logged in user");
        loginPage.loginIfNotWithSameEmail(username, password, otp, retryCounter);

        step("Go to TTD Landing Page");
        ttdLandingPageWeb.goToTtdLandingPage();

        step("Search and select product item");
        ttdLandingPageWeb.inputSearchKeyword(productName);
        ttdLandingPageWeb.selectRecommendedActivity(productName);

        step("Click details on Selected Ticket");
        ttdProductDetailPageWeb.openDetailPackage(packageName, 3);

        step("Check package has bXgY promo that is runs out");
        ttdProductDetailPageMweb.verifyBxGyNoQuota(packageName);

        step("Select the package");
        ttdProductDetailPageWeb.clickSeePackagesButton();
        ttdProductDetailPageWeb.chooseTicket(packageName);
        ttdProductDetailPageWeb.setTicketQty(quantity, ticketIndex);
        ttdProductDetailPageWeb.clickBookButton();
    }

    @Author(names = {"Manan Kumar"})
    @Module(name = "SearchResult")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void testCreateOrderWithSessionPackage() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testCreateOrderWithSessionPackage");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        TtdLandingPageWeb<?> ttdLandingPageWeb = TtdLandingPageWeb.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);

        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
        String product = param.get("product");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));

        step("Open home page");
        homePage.goToHomePage();

        step("Open as logged in user");
        loginPage.loginIfNotWithSameEmail(username, password, otp, retryCounter);

        step("Go to TTD Landing Page");
        ttdLandingPageWeb.goToTtdLandingPage();

        step("User navigates to the product");
        ttdLandingPageWeb.openProductViaURL(product);

        step("Verify that the correct product has been opened");
        Assertion.assertThat("Is correct product opened", ttdProductDetailPageWeb.isCorrectProductOpened(product), is(true));

        step("User opens Package Session");
        ttdLandingPageWeb.clickSelectSessionBTN();

        step("User Fills session details");
        String sessionDetails = ttdLandingPageWeb.selectSessionDetails();

        step("User set ticket Quantity");
        ttdLandingPageWeb.setTicketQuantity();

        step("User clicks on book button");
        ttdLandingPageWeb.clickBookButton();

        step("Verify session details on booking page");
        Assertion.assertThat("Session details should match on booking page", ttdLandingPageWeb.verifySessionDetailsOnBookingPage(sessionDetails), is(true));
    }

    @Author(names = {"Manan Kumar"})
    @Module(name = "SearchResult")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void testPartnerPageNearestLocationOff() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testPartnerPageNearestLocationOff");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        TtdLandingPageWeb<?> ttdLandingPageWeb = TtdLandingPageWeb.getInstance(session);

        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
        String product = param.get("product");
        String partnerName = param.get("partnerName");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));

        step("Open home page");
        homePage.goToHomePage();

        step("Open as logged in user");
        loginPage.loginIfNotWithSameEmail(username, password, otp, retryCounter);

        step("Go to TTD Landing Page");
        ttdLandingPageWeb.goToTtdLandingPage();

        step("User navigates to the master product");
        ttdLandingPageWeb.openProductViaURL(product);

        step("User navigates to the partner page");
        ttdLandingPageWeb.goToPartner(partnerName);

        step("User clicks on Nearest button");
        ttdLandingPageWeb.clickNearestButton();

        step("User disables the location access");
        ttdLandingPageWeb.disableLocationAccess();

        step("User verifies that GPS popup is visible");
        Assertion.assertThat("Verify GPS popup is present", ttdLandingPageWeb.enableGPSPopUPVisible(), is(true));

        step("User clicks on Okay button");
        ttdLandingPageWeb.clickOkayButton();

        step("User verifies that after disabling location access, the page returns to the Most-Popular (default) state");
        Assertion.assertThat("User is at default state", ttdLandingPageWeb.isDefaultPageDisplayed(), is(true));
    }

    @Author(names = {"Ram Pratap Singh"})
    @Module(name = "Booking")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void checkOrderWithValidityExpiredDateScheduleTomorrow() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("checkOrderWithValidityExpiredDateScheduleTomorrow");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        TtdLandingPageWeb<?> ttdLandingPageWeb = TtdLandingPageWeb.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);
        TtdPackageDetailPageWeb<?> ttdPackageDetailPageWeb = TtdPackageDetailPageWeb.getInstance(session);
        TtdSelectedPackageDetailPageWeb<?> ttdSelectedPackageDetailPageWeb = TtdSelectedPackageDetailPageWeb.getInstance(session);
        TtdBookingFormPageWeb<?> ttdBookingFormPageWeb = TtdBookingFormPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);

        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String product = param.get("product");
        String paymentGroup = param.get("paymentGroup");
        String paymentMethod = param.get("paymentMethod");

        step("Go to Home Page");
        homePage.goToHomePage();

        step("Open as logged in user");
        loginPage.loginIfNotWithSameEmail(username, password, otp, retryCounter);

        step("Go to TTD Landing Page");
        ttdLandingPageWeb.goToTtdLandingPage();

        step("Search Product");
        ttdLandingPageWeb.inputSearchKeyword(product);
        ttdLandingPageWeb.selectRecommendedActivity(product);

        step("Book Package with label 'Valid until [Expired Date]'");
        ttdProductDetailPageWeb.clickSeeAllPackagesButton();

        step("See Package detail");
        ttdProductDetailPageWeb.openTicketDetailWithExpiredDate();
        String validDayTextBefore = ttdPackageDetailPageWeb.getValidDayText();
        Assertion.assertThat("Valid day text is there on Package Detail Page", validDayTextBefore, notNullValue());
        List<String> validDaySplit = Arrays.stream(validDayTextBefore.split(" ")).toList();
        String validityExpiredDate = validDaySplit.get(validDaySplit.size() - 3) + " " + validDaySplit.get(validDaySplit.size() - 2) + " " + validDaySplit.get(validDaySplit.size() - 1);
        ttdPackageDetailPageWeb.clickSelectTicketFromDetail();

        step("Select tomorrow's schedule");
        String selectedDate = ttdSelectedPackageDetailPageWeb.chooseTomorrowDate();
        Assertion.assertThat("Tomorrow's schedule is available", selectedDate, is(notNullValue()));

        step("Submit booking");
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        String tomorrowDateId = tomorrow.format(DateTimeFormatter.ofPattern("d MMM yyyy", new Locale("id", "ID")));
        String tomorrowDateEn = tomorrow.format(DateTimeFormatter.ofPattern("d MMM yyyy", new Locale("en", "EN")));

        String validityPeriodText = ttdProductDetailPageWeb.getValidityPeriodText();
        Assertion.assertThat("Verify validity start date is tomorrow", validityPeriodText, anyOf(containsStringIgnoringCase(tomorrowDateEn), containsStringIgnoringCase(tomorrowDateId)));
        Assertion.assertThat("Verify validity end date is expired date", validityPeriodText, containsStringIgnoringCase(validityExpiredDate));

        ttdProductDetailPageWeb.setTicketQty(1, 0);
        ttdProductDetailPageWeb.clickBookButton();

        ttdBookingFormPageWeb.fillGuestDetail();
        String validDayTextBeforeBookingForm = ttdBookingFormPageWeb.getValidDateText();
        Assertion.assertThat("Valid day text is there on Booking Form Page", validDayTextBeforeBookingForm, notNullValue());

        ttdBookingFormPageWeb.continueToPaymentPage();
        String orderId = paymentPage.getOrderId();

        step("Submit payment method");
        paymentPage.clickOnSelectPaymentMethod();
        paymentPage.goToPaymentMethodsGroup(paymentGroup);
        paymentPage.selectPaymentMethod(paymentMethod);
        paymentPage.clickOnPaymentButton();
        Assertion.assertThat("Payment successful page is visible", paymentPage.isPaymentSuccessfulVisible(), is(true));

        step("Check issued on order detail page");
        orderListPage.openActiveTicketViaURL(orderId);
        orderDetailPage.skipCoachmark();
        Assertion.assertThat("Order successfully issued", orderDetailPage.verifyIssuedETicket(), is(true));
    }

    @Author(names = {"Manan Kumar"})
    @Module(name = "Booking")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void checkSeatMapOpenSeating() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("checkSeatMapOpenSeating");
        BaseTest.paramsThreadLocal.set(param);
        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String productName = param.get("productName");
        String packageName = param.get("packageName");
        String paymentGroup = param.get("paymentGroup");
        String paymentMethod = param.get("paymentMethod");

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        TtdLandingPageWeb<?> ttdLandingPageWeb = TtdLandingPageWeb.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);
        TtdBookingFormPageWeb<?> ttdBookingFormPageWeb = TtdBookingFormPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);

        step("Open home page");
        homePage.goToHomePage();

        step("Open as logged in user");
        loginPage.loginIfNotWithSameEmail(username, password, otp, retryCounter);

        step("Select IDR as currency");
        homePage.selectCurrency("IDR");

        step("Go to TTD Landing Page");
        ttdLandingPageWeb.goToTtdLandingPage();

        step("Search and select product item");
        ttdLandingPageWeb.inputSearchKeyword(productName);
        ttdLandingPageWeb.selectRecommendedActivity(productName);

        step("User click on Buy Ticket ");
        ttdProductDetailPageWeb.clickBuyTicket();

        step("Select package: " + packageName);
        ttdProductDetailPageWeb.selectPackage(packageName);

        step("Verify Free seating is available for selected Package.");
        Assertion.assertThat("Free seating label is present", ttdProductDetailPageWeb.isFreeSeatingDisplayed(), is(true));

        step("Submit the total ticket");
        ttdProductDetailPageWeb.setTicketQty(1, 0);
        ttdProductDetailPageWeb.clickBookButton();

        step("Verify Free seating is available on booking page.");
        Assertion.assertThat("Free seating label is present on booking page", ttdBookingFormPageWeb.isFreeSeatingDisplayedOnBookingPage(), is(true));

        step("Completing booking form data");
        ttdBookingFormPageWeb.fillContactOrderDetail(true);
        ttdBookingFormPageWeb.fillGuestDetail();

        step("Continue to the payment page");
        ttdBookingFormPageWeb.continueToPaymentPage();
        Assertion.assertThat("Success redirected to payment page", paymentPage.isPaymentPageLoaded(), is(true));
        String orderId = paymentPage.getOrderId();

        step("Open all payment method");
        paymentPage.clickOnSelectPaymentMethod();

        step("Select the payment Method");
        paymentPage.goToPaymentMethodsGroup(paymentGroup);
        paymentPage.selectPaymentMethod(paymentMethod);

        step("Submit the payment method");
        paymentPage.clickOnPaymentButton();
        Assertion.assertThat("Payment successful page is visible", paymentPage.isPaymentSuccessfulVisible(), is(true));

        step("Check issued on order detail page");
        orderListPage.openActiveTicketViaURL(orderId);
        orderDetailPage.skipCoachmark();
        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.verifyIssuedETicket(), is(true));
    }
}
