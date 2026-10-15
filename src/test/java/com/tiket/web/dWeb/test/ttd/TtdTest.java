package com.tiket.web.dWeb.test.ttd;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.apiPage.TTDApi;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Environment;
import com.tiket.model.FilterType;
import com.tiket.model.Session;
import com.tiket.model.SortItem;
import com.tiket.page.ttd.TTDPage;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.testbase.Configs;
import com.tiket.web.WebTest;
import com.tiket.web.dWeb.page.account.InboxPage;
import com.tiket.web.page.order.OrderDetail.OrderDetailWeb;
import com.tiket.web.page.order.OrderList.OrderListWeb;
import com.tiket.web.dWeb.page.payment.PaymentPage;
import com.tiket.web.page.platform.payment.PaymentPageWeb;
import com.tiket.web.page.platform.sphinx.refund.RefundPageWeb;
import com.tiket.web.page.ttd.TtdBookingFormPage.TtdBookingFormPageWeb;
import com.tiket.web.page.ttd.TtdLandingPage.TtdLandingPageWeb;
import com.tiket.web.page.ttd.TtdProductDetailPage.TtdProductDetailPageWeb;
import com.tiket.web.page.ttd.TtdSearchResultPage.TtdSearchResultPageWeb;
import com.tiket.web.dashboard.page.platform.discovery.DashboardPages;
import com.tiket.web.page.common.home.HomePageWeb;
import com.tiket.web.page.common.home.LoginPageWeb;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import javax.security.auth.login.FailedLoginException;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.tiket.core.CoreFunctions.removeDiscount;
import static com.tiket.core.CoreFunctions.splitText;
import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;

public class TtdTest extends BaseTest {

    //OTW test case
    @Author(names = {"Yazid"})
    @Module(name = "SRP")
    @Tribe(name = "TTD")
    @Vertical(name = "TTD")
    @Test
    public void checkNearestCampaignWeb() throws FailedLoginException, InterruptedException {
        Map<String, String> map = getParam("checkNearestCampaignWeb");
        BaseTest.paramsThreadLocal.set(map);
        String username = map.get("username");
        String password = map.get("password");
        String otp = map.get("otp");
        int retryCounter = Integer.parseInt(map.get("retryCounter"));
        String productCategoryCodes = map.get("productCategoryCodes");
        String location = map.get("location");
        String locationType = map.get("locationType");
        String campaignName = map.get("campaignName");
        String campaignNameCard = map.get("campaignNameCard");

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        TtdSearchResultPageWeb<?> ttdSearchResultPageWeb = TtdSearchResultPageWeb.getInstance(session);

        step("User on the home page with login session");
        homePage.goToHomePage();
        loginPage.loginIfNotWithSameEmail(username, password, otp, retryCounter);

        step("User is on SRP");
        ttdSearchResultPageWeb.openSrpUrl(productCategoryCodes);

        step("Change the location: " + location);
        ttdSearchResultPageWeb.clickLocation();
        ttdSearchResultPageWeb.inputLocationSearchBar(location);
        ttdSearchResultPageWeb.clickLocationSearchResult(location, locationType);
        Assertion.assertThat("SRP location should be : " + location, ttdSearchResultPageWeb.getSearchLocation().toLowerCase(), is(location.toLowerCase()));

        step("Choose the campaign name: "+campaignName+" from the filter");
        ttdSearchResultPageWeb.chooseFilter(campaignName, FilterType.PROMO);

        step("Check SRP in nearest location user: "+location+" with Category and sorted by Campaign: "+campaignName);
        if (!campaignNameCard.isEmpty()) campaignName = campaignNameCard;
        Assertion.assertThat("Show SRP products list with the selected Campaign and user's nearest location", ttdSearchResultPageWeb.checkLocationCampaign(location, campaignName), is(true));
    }

    //OTW test case
    @Author(names = {"Yazid"})
    @Module(name = "SearchSuggestion")
    @Tribe(name = "TTD")
    @Vertical(name = "TTD")
    @Test
    public void checkPopularDestinationLoginWeb() throws FailedLoginException, InterruptedException {
        Map<String, String> map = getParam("checkPopularDestinationLoginWeb");
        BaseTest.paramsThreadLocal.set(map);
        String username = map.get("username");
        String password = map.get("password");
        String otp = map.get("otp");
        int retryCounter = Integer.parseInt(map.get("retryCounter"));

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        TtdLandingPageWeb<?> ttdLandingPageWeb = TtdLandingPageWeb.getInstance(session);
        TtdSearchResultPageWeb<?> ttdSearchResultPageWeb = TtdSearchResultPageWeb.getInstance(session);

        step("User on the home page with login session");
        homePage.goToHomePage();
        loginPage.loginIfNotWithSameEmail(username, password, otp, retryCounter);

        step("Go to TTD Landing Page");
        ttdLandingPageWeb.goToTtdLandingPage();

        step("Click Search Bar");
        ttdLandingPageWeb.openSearchInput();

        step("Choose one of the popular destination: Randomly");
        String clickedPopularDestination = ttdLandingPageWeb.selectPopularDestinations(String.valueOf(SortItem.RANDOM));
        Assertion.assertThat("Location on the SRP should be equal with the selected destination", ttdSearchResultPageWeb.getSearchLocation(), is(clickedPopularDestination));

        step("Back to the landing page");
        ttdLandingPageWeb.goToTtdLandingPage();

        step("Click see all button of popular destination");
        ttdLandingPageWeb.openSearchInput();
        ttdLandingPageWeb.clickSeeAllPopularDestination();
        Assertion.assertThat("Destination page is opened", ttdLandingPageWeb.verifyDestinationListPage(), is(true));
    }

    //OTW test case
    @Author(names = {"Yazid"})
    @Module(name = "Login")
    @Tribe(name = "TTD")
    @Vertical(name = "TTD")
    @Test
    public void checkLoginBannerWeb() {
        Map<String, String> map = getParam("checkLoginBannerWeb");
        BaseTest.paramsThreadLocal.set(map);
        String username = map.get("username");
        String password = map.get("password");
        String otp = map.get("otp");
        String productCategoryCodes = map.get("productCategoryCodes");
        String slugProductNamePdp = map.get("slugProductNamePdp");
        String expectedProductNamePdp = map.get("expectedProductNamePdp");
        String slugProductNameBooking = map.get("slugProductNameBooking");
        String expectedProductNameBooking = map.get("expectedProductNameBooking");

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        TtdSearchResultPageWeb<?> ttdSearchResultPageWeb = TtdSearchResultPageWeb.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);
        TtdBookingFormPageWeb<?> ttdBookingFormPageWeb = TtdBookingFormPageWeb.getInstance(session);

        step("User on the home page as guest session");
        homePage.goToHomePage();
        loginPage.loginAsGuest();

        step("User is on SRP");
        ttdSearchResultPageWeb.openSrpUrl(productCategoryCodes);

        step("User should be able to click login from SRP and will be directed to the login page");
        ttdSearchResultPageWeb.clickLoginBanner();

        step("User input credential on login page");
        loginPage.loginStepsWithoutRetry(username, password, otp);

        step("When user successfully logged in, user should be directed back to the previous state before login: SRP");
        Assertion.assertThat("User back to the SRP", ttdSearchResultPageWeb.getProductTitle(), is(not(emptyOrNullString())));
        Assertion.assertThat("Login banner on SRP is not there", ttdSearchResultPageWeb.isLoginBannerDisplayed(), is(false));

        step("User logged out");
        loginPage.logoutFromDweb();

        step("User is on PDP");
        ttdProductDetailPageWeb.openPdpUrl(slugProductNamePdp);
        ttdProductDetailPageWeb.getProductName();

        step("User should be able to click login from PDP and will be directed to the login page");
        ttdProductDetailPageWeb.openLoginFromRequiredLogin();

        step("User input credential on login page");
        loginPage.loginStepsWithoutRetry(username, password, otp);

        step("When user successfully logged in, user should be directed back to the previous state before login: PDP");
        Assertion.assertThat("User back to the PDP", ttdProductDetailPageWeb.getProductName().toLowerCase(), is(expectedProductNamePdp.toLowerCase()));
        Assertion.assertThat("Login banner on PDP is not there", ttdProductDetailPageWeb.isLoginOverlayDisplayed(), is(false));

        step("User logged out");
        loginPage.logoutFromDweb();

        step("User is on Booking form");
        ttdProductDetailPageWeb.openPdpUrl(slugProductNameBooking);
        ttdProductDetailPageWeb.getProductName();
        ttdProductDetailPageWeb.chooseTicket(String.valueOf(SortItem.RANDOM));
        ttdProductDetailPageWeb.setTicketQty(1, 0);
        ttdProductDetailPageWeb.clickBookButton();

        step("User should be able to click login from Booking Form and will be directed to the login page");
        ttdBookingFormPageWeb.clickLoginNow();

        step("User input credential on login page");
        loginPage.loginStepsWithoutRetry(username, password, otp);

        step("When user successfully logged in, user should be directed back to the previous state before login: Booking Form");
        Assertion.assertThat("User back to the Booking Form", ttdBookingFormPageWeb.getProductTitle().toLowerCase(), is(expectedProductNameBooking.toLowerCase()));
        Assertion.assertThat("Login banner on Booking Form is not there", ttdBookingFormPageWeb.isLoginNowButtonDisplayed(), is(false));
    }

    //OTW test case
    @Author(names = {"Yazid"})
    @Module(name = "SearchSuggestion")
    @Tribe(name = "TTD")
    @Vertical(name = "TTD")
    @Test
    public void checkTopKeywordRefresh() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("checkTopKeywordRefresh");
        BaseTest.paramsThreadLocal.set(param);
        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
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

        step("Click Search Bar");
        ttdLandingPageWeb.openSearchInput();

        step("Get Keyword list before clicking refresh button");
        List<String> listBeforeRefresh = ttdLandingPageWeb.getTopKeywordList();

        step("click refresh button");
        ttdLandingPageWeb.clickRefreshTopKeyword();

        step("Get Keyword list after clicking refresh button");
        List<String> listAfterRefresh = ttdLandingPageWeb.getTopKeywordList();
        Assertion.assertThat("Top Keyword changes after clicking Refresh button", listAfterRefresh, is(not(listBeforeRefresh)));
    }

    //OTW test case
    @Author(names = {"Yazid"})
    @Module(name = "Campaign")
    @Tribe(name = "TTD")
    @Vertical(name = "TTD")
    @Test
    public void checkRegularCampaignWeb() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("checkRegularCampaignWeb");
        BaseTest.paramsThreadLocal.set(param);
        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String sectionTitleHome = param.get("sectionTitleHome");
        String productNameHome = param.get("productNameHome");
        String campaignLabelHome = param.get("campaignLabelHome");
        String sectionTitleTtd = param.get("sectionTitleTtd");
        String productNameTtd = param.get("productNameTtd");
        String campaignLabelTtd = param.get("campaignLabelTtd");
        String productCategoryCodes = param.get("productCategoryCodes");
        String destinationName = param.get("destinationName");
        String destinationType = param.get("destinationType");
        String productNameSrp = param.get("productNameSrp");
        String campaignLabelSrp = param.get("campaignLabelSrp");
        int totalScrollSrp = Integer.parseInt(param.get("totalScrollSrp"));
        String packageName = param.get("packageName");

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        TtdLandingPageWeb<?> ttdLandingPageWeb = TtdLandingPageWeb.getInstance(session);
        TtdSearchResultPageWeb<?> ttdSearchResultPageWeb = TtdSearchResultPageWeb.getInstance(session);
        DashboardPages<?> dashboardPages = new DashboardPages<>(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);
        TtdBookingFormPageWeb<?> ttdBookingFormPageWeb = TtdBookingFormPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);

        step("User on the home page with login session");
        homePage.goToHomePage();
        loginPage.loginIfNotWithSameEmail(username, password, otp, retryCounter);

        step("User scroll to the \""+sectionTitleHome+"\" & select \""+productNameHome+"\" product");
        homePage.goToSection(sectionTitleHome);
        String campaignLabel = homePage.getCampaignProduct(productNameHome);
        Assertion.assertThat("Campaign label should be visible on the Home Page", campaignLabel.toLowerCase(), is(campaignLabelHome.toLowerCase()));

        step("User is on TTD home and scroll to \""+sectionTitleTtd+"\" section & select \""+productNameTtd+"\" product");
        ttdLandingPageWeb.goToTtdLandingPage();
        homePage.goToSection(sectionTitleTtd);
        campaignLabel = homePage.getCampaignProduct(productNameTtd);
        Assertion.assertThat("Campaign label should be visible on the TTD Landing Page", campaignLabel.toLowerCase(), is(campaignLabelTtd.toLowerCase()));

        step("User is on SRP with location as \""+destinationName+"\" & select \""+productNameSrp+"\" product");
        ttdSearchResultPageWeb.openSrpUrl(productCategoryCodes);
        ttdSearchResultPageWeb.clickLocation();
        ttdSearchResultPageWeb.inputLocationSearchBar(destinationName);
        ttdSearchResultPageWeb.clickLocationSearchResult(destinationName, destinationType);
        campaignLabel = ttdSearchResultPageWeb.findCampaignProduct(productNameSrp, totalScrollSrp);
        Assertion.assertThat("Campaign label should be visible on the SRP", campaignLabel.toLowerCase(), is(campaignLabelSrp.toLowerCase()));

        step("User is on PDP \""+productNameSrp+"\"");
        ttdSearchResultPageWeb.chooseProductSrp(false, productNameSrp, totalScrollSrp);
        dashboardPages.handleNewWindow();
        campaignLabel = ttdProductDetailPageWeb.getChildCampaign();
        Assertion.assertThat("Campaign label should be visible on the PDP", campaignLabel.toLowerCase(), is(campaignLabelSrp.toLowerCase()));

        step("Create booking on \""+campaignLabel+"\" campaign package: " + packageName);
        ttdProductDetailPageWeb.chooseTicketCampaign(packageName, campaignLabel, false);
        ttdProductDetailPageWeb.setTicketQty(1, 0);
        ttdProductDetailPageWeb.clickBookButton();
        ttdBookingFormPageWeb.fillContactOrderDetail(true);
        ttdBookingFormPageWeb.fillGuestDetail();
        String priceDiscount = ttdBookingFormPageWeb.getDiscountPrice();
        Assertion.assertThat("Price discount is visible on the Booking Form Page price details", priceDiscount, is(containsString("-")));

        step("Successfully booked product with the vertical campaign with the deal amount");
        ttdBookingFormPageWeb.continueToPaymentPage();
        Assertion.assertThat("Success redirected to payment page", paymentPage.isPaymentPageLoaded(), is(true));
        Assertion.assertThat("Order ID should be generated", paymentPage.getOrderId(), is(not(emptyOrNullString())));
        String actualDiscountPrice = paymentPage.getDiscountPrice();
        Assertion.assertThat("Price discount is visible on the Payment Page price details", actualDiscountPrice, is(containsString("-")));
        Assertion.assertThat("Price discount should be equal with the booking form page", removeDiscount(actualDiscountPrice), is(removeDiscount(priceDiscount)));
    }

    @Author(names = {"Yazid"})
    @Module(name = "SearchPage")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void verifyRecentSearch() {
        Map<String, String> param = getParam("verifyRecentSearch");
        BaseTest.paramsThreadLocal.set(param);
        String searchKeyword = param.get("searchKeyword");

        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        TtdLandingPageWeb<?> ttdLandingPageWeb = TtdLandingPageWeb.getInstance(session);

        step("Logout via URL");
        homePage.goToHomePage();
        loginPage.logoutViaUrl();

        step("Go to TTD Landing Page");
        ttdLandingPageWeb.goToTtdLandingPage();

        step("Search and select item");
        ttdLandingPageWeb.inputSearchKeyword(searchKeyword);
        ttdLandingPageWeb.selectRecommendedActivity(searchKeyword);

        step("Redirected to the correct page");
        Assertion.assertThat("After select recommended item, user should be redirected to the correct page", ttdLandingPageWeb.redirectionSearchInput(), hasItem(containsStringIgnoringCase(searchKeyword)));

        step("Back to TTD Landing Page");
        ttdLandingPageWeb.goToTtdLandingPage();

        step("Check the recent search section");
        ttdLandingPageWeb.openSearchInput();
        Assertion.assertThat("Recent search should be visible", ttdLandingPageWeb.getRecentSearch(), equalToIgnoringCase(searchKeyword));
    }


    @Author(names = {"Yazid"})
    @Module(name = "BookingSupplier")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void bundlingSupplierKlook() throws FailedLoginException, InterruptedException, ExecutionException {
        Map<String, String> param = getParam("bundlingSupplierKlook");
        BaseTest.paramsThreadLocal.set(param);
        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String productName = param.get("productName");
        String supplierName = param.get("supplierName");
        boolean visitDateOdd = Boolean.parseBoolean(param.get("visitDateOdd"));
        int ticketQty = Integer.parseInt(param.get("ticketQty"));
        int bundleQty = Integer.parseInt(param.get("bundleQty"));
        String paymentGroup = param.get("paymentGroup");
        String paymentMethod = param.get("paymentMethod");
        String config = param.get("config");
        String database = param.get("database");
        String collectionName = param.get("collectionName");
        String code = param.get("code");

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        TtdLandingPageWeb<?> ttdLandingPageWeb = TtdLandingPageWeb.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);
        TtdBookingFormPageWeb<?> ttdBookingFormPageWeb = TtdBookingFormPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        TTDApi<?> ttdApi = new TTDApi<>(session);
        TTDPage<?> ttdPage = new TTDPage<>(session);

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

        step("Select package from supplier: " + supplierName);
        ttdProductDetailPageWeb.getProductName();
        ttdProductDetailPageWeb.chooseTicketSupplier(supplierName);
        ttdProductDetailPageWeb.getProductName();
        ttdProductDetailPageWeb.chooseTicketSupplier(supplierName);
        Assertion.assertThat("Verify that the supplierID is S12 ", ttdPage.getSupplierId("klook", "en", productName, code), is(true));

        step("Choose visit date with odd number: " + visitDateOdd);
        String selectedDate = ttdProductDetailPageWeb.chooseVisitDate(visitDateOdd);

        step("Submit the total ticket");
        ttdProductDetailPageWeb.setTicketQty(ticketQty, 0);
        ttdProductDetailPageWeb.clickBookButton();

        step("Add bundle product");
        ttdBookingFormPageWeb.addBundleProduct(bundleQty, 0, selectedDate);
        Assertion.assertThat("Bundle product is added", ttdBookingFormPageWeb.isBundleProductAdded(), is(true));

        step("Completing booking form data");
        ttdBookingFormPageWeb.fillContactOrderDetail(true);
        ttdBookingFormPageWeb.fillGuestDetailSupplier(supplierName);

        step("Continue to the payment page");
        ttdBookingFormPageWeb.continueToPaymentPage();
        ttdBookingFormPageWeb.checkPriceChange();
        Assertion.assertThat("Success redirected to payment page", paymentPage.isPaymentPageLoaded(), is(true));
        String orderId = paymentPage.getOrderId();

        step("Open all payment method");
        paymentPage.clickOnSelectPaymentMethod();

        step("Select the payment Method");
        paymentPage.goToPaymentMethodsGroup(paymentGroup);
        paymentPage.selectPaymentMethod(paymentMethod);

        step("Submit the payment method");
        paymentPage.clickOnPaymentButton();

        if (Configs.ENVIRONMENT == Environment.GK || Configs.ENVIRONMENT == Environment.PREPROD) {
            Assertion.assertThat("Payment successful page is visible", paymentPage.isPaymentSuccessfulVisible(), is(true));

            step("Submit issued order from the vendor side");
            String query = "{\"coreOrderId\":\"" + orderId + "\"}";
            String supplierOrderId = orderDetailPage.getSupplierOrderIdFromDb(config, database, collectionName, query);
            String messageResult = ttdApi.publishVoucherSupplier(orderId, supplierOrderId);
            Assertion.assertThat("Publish voucher from supplier", messageResult, equalToIgnoringCase("success"));

            step("Check issued on order detail page");
            orderListPage.openActiveTicketViaURL(orderId);
            orderDetailPage.skipCoachmark();
            Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.verifyIssuedETicket(orderId), is(true));

        } else {
            step("User open order list page");
            paymentPage.clickSeeOrderListButton();

            step("User open order detail page");
            orderListPage.openOrderDetailPage(orderId);

            Assertion.assertThat("orderID should be visible and should be same as from payment page", orderDetailPage.getOrderId(), is(orderId));
        }
    }

    @Author(names = {"Yazid"})
    @Module(name = "Booking")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void checkAutoSeatRefundable() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("checkAutoSeatRefundable");
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

        step("Login as " + username + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(username, password);

        step("Select IDR as currency");
        homePage.selectCurrency("IDR");

        step("Go to TTD Landing Page");
        ttdLandingPageWeb.goToTtdLandingPage();

        step("Search and select product item");
        ttdLandingPageWeb.inputSearchKeyword(productName);
        ttdLandingPageWeb.selectRecommendedActivity(productName);

        step("Select package: " + packageName);
        ttdProductDetailPageWeb.chooseTicket(packageName);

        step("Submit the total ticket");
        ttdProductDetailPageWeb.setTicketQty(1, 0);
        ttdProductDetailPageWeb.clickBookButton();

        step("Completing booking form data");
        Assertion.assertThat("User will see Refund label in package section on the Booking Form page", ttdBookingFormPageWeb.isRefundInfoDisplayed(), is(true));
        ttdBookingFormPageWeb.fillContactOrderDetail(true);
        ttdBookingFormPageWeb.fillGuestDetail();

        step("Continue to the payment page");
        ttdBookingFormPageWeb.continueToPaymentPage();
        Assertion.assertThat("Success redirected to payment page", paymentPage.isPaymentPageLoaded(), is(true));
        String orderId = paymentPage.getOrderId();

        step("Open all payment method");
        paymentPage.clickOnSelectPaymentMethod();

        step("Select the payment Method");
        paymentPage.selectPaymentMethod(paymentMethod);

        step("Submit the payment method");
        paymentPage.clickOnPaymentButton();
        Assertion.assertThat("Payment successful page is visible", paymentPage.isPaymentSuccessfulVisible(), is(true));

        step("Check issued on order detail page");
        orderListPage.openActiveTicketViaURL(orderId);
        orderDetailPage.skipCoachmark();
        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.verifyIssuedETicket(), is(true));
    }

    @Author(names = {"Yazid"})
    @Module(name = "Booking")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void checkClaimedGV() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("checkClaimedGV");
        BaseTest.paramsThreadLocal.set(param);
        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String productSlug = param.get("productSlug");
        String packageName = param.get("packageName");
        String gvTitleSection = param.get("gvTitleSection");
        String paymentGroup = param.get("paymentGroup");
        String paymentMethod = param.get("paymentMethod");
        String gvInboxTitle = param.get("gvInboxTitle");

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);
        TtdBookingFormPageWeb<?> ttdBookingFormPageWeb = TtdBookingFormPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        InboxPage<?> inboxPage = new InboxPage<>(session);

        step("Open home page");
        homePage.goToHomePage();

        step("Open as logged in user");
        loginPage.loginIfNotWithSameEmail(username, password, otp, retryCounter);

        step("Select IDR as currency");
        homePage.selectCurrency("IDR");

        step("User is on PDP");
        ttdProductDetailPageWeb.openPdpUrl(productSlug);

        step("Select package: " + packageName);
        ttdProductDetailPageWeb.chooseTicket(packageName);

        step("Choose next day of visit date");
        ttdProductDetailPageWeb.chooseVisitDate(0);

        step("Submit the total ticket");
        ttdProductDetailPageWeb.setTicketQty(1, 0);
        ttdProductDetailPageWeb.clickBookButton();

        step("Completing booking form data");
        ttdBookingFormPageWeb.fillContactOrderDetail(true);
        ttdBookingFormPageWeb.fillGuestDetail();

        step("Scroll until GV section");
        String actualGVTitleSection = ttdBookingFormPageWeb.getGVTitleSection();
        Assertion.assertThat("User will see GV section with title: " + gvTitleSection, actualGVTitleSection, either(is(gvTitleSection.split("/")[0])).or(is(gvTitleSection.split("/")[1])));

        step("Click 'Claim' button");
        ttdBookingFormPageWeb.clickClaimGVButton();

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

        step("Click inbox icon");
        homePage.openInbox();

        step("Show claimed GV inside the inbox");
        Assertion.assertThat("Claimed GV should be visible inside the inbox on the first index", inboxPage.getInboxTitle(0), either(is(containsString(gvInboxTitle.split("/")[0]))).or(is(containsString(gvInboxTitle.split("/")[1]))) );
    }

    @Author(names = {"Yazid", "Aditi"})
    @Module(name = "Booking")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void checkQuotaFullyBooked() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("checkQuotaFullyBooked");
        BaseTest.paramsThreadLocal.set(param);
        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String productSlug = param.get("productSlug");
        String packageName = param.get("packageName");
        String paymentGroup = param.get("paymentGroup");
        String paymentMethod = param.get("paymentMethod");

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
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

        step("User is on PDP");
        ttdProductDetailPageWeb.openPdpUrl(productSlug);

        step("Select package: " + packageName);
        ttdProductDetailPageWeb.chooseTicketCampaign(packageName, "buyxgety", true);
        Assertion.assertThat("User will not see any option to apply buy X get Y promo", ttdProductDetailPageWeb.isBuyXGetYSectionDisplayed(), is(false));

        step("Choose next day of visit date");
        ttdProductDetailPageWeb.chooseVisitDate(0);

        step("Submit the total ticket");
        ttdProductDetailPageWeb.setTicketQty(1, 0);
        ttdProductDetailPageWeb.clickBookButton();

        step("Completing booking form data");
        ttdBookingFormPageWeb.fillContactOrderDetail(true);
        ttdBookingFormPageWeb.fillGuestDetail();

        step("Check the price breakdown");
        int finalPrice = ttdBookingFormPageWeb.convertPriceToInt(ttdBookingFormPageWeb.getFinalPrice());
        Assertion.assertThat("Price break-down is correct", ttdBookingFormPageWeb.checkPriceBreakdown(), is(finalPrice));

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

        if (Configs.ENVIRONMENT == Environment.GK || Configs.ENVIRONMENT == Environment.PREPROD) {
            Assertion.assertThat("Payment successful page is visible", paymentPage.isPaymentSuccessfulVisible(), is(true));

            step("Check issued on order detail page");
            orderListPage.openActiveTicketViaURL(orderId);
            orderDetailPage.skipCoachmark();
            Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.verifyIssuedETicket(), is(true));
        } else {
            step("User open order list page");
            paymentPage.clickSeeOrderListButton();

            step("User open order detail page");
            orderListPage.openOrderDetailPage(orderId);

            Assertion.assertThat("orderID should be visible and should be same as from payment page", orderDetailPage.getOrderId(), is(orderId));
        }
    }

    @Author(names = {"Yazid", "Rizki Maulana"})
    @Module(name = "BookingPopularDestinations")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void checkPopularDestinations() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("checkPopularDestinations");
        BaseTest.paramsThreadLocal.set(param);
        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String productName = param.get("productName");
        String packageName = param.get("packageName");
        int totalScrollSrp = Integer.parseInt(param.get("totalScrollSrp"));
        int ticketQty = Integer.parseInt(param.get("ticketQty"));
        String paymentGroup = param.get("paymentGroup");
        String paymentMethod = param.get("paymentMethod");

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        TtdLandingPageWeb<?> ttdLandingPageWeb = TtdLandingPageWeb.getInstance(session);
        TtdSearchResultPageWeb<?> ttdSearchResultPageWeb = TtdSearchResultPageWeb.getInstance(session);
        DashboardPages<?> dashboardPages = new DashboardPages<>(session);
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

        step("Check total of the popular destinations");
        ttdLandingPageWeb.openSearchInput();
        Assertion.assertThat("Popular destinations should have 10 items", ttdLandingPageWeb.getPopularDestinationsAmount(), is(10));

        step("Choose the destination that have lots of activities");
        String selectedDestinations = ttdLandingPageWeb.selectPopularDestinationsWithLotsActivity();
        Assertion.assertThat("Redirect to the SRP with the selected destinations", selectedDestinations , containsStringIgnoringCase(ttdSearchResultPageWeb.getSearchLocation()));
        ttdSearchResultPageWeb.closeContinueOnWebPopup();

        step("Choose activity from the SRP");
        ttdSearchResultPageWeb.chooseProductSrp(false, productName, totalScrollSrp);

        step("Select package by name");
        dashboardPages.handleNewWindow();
        ttdProductDetailPageWeb.chooseTicket(packageName);

        step("Submit the total ticket");
        ttdProductDetailPageWeb.setTicketQty(ticketQty, 0);
        ttdProductDetailPageWeb.clickBookButton();

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

    @Author(names = {"Yazid"})
    @Module(name = "TTD")
    @Tribe(name = "TTD")
    @Vertical(name = "TTD")
    @Test
    public void TTD_checkInventoryRefund100p() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("TTD_checkInventoryRefund100p");
        BaseTest.paramsThreadLocal.set(param);
        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String popularDestination = param.get("popularDestination");
        int totalScrollSrp = Integer.parseInt(param.get("totalScrollSrp"));
        String productWithRefund = param.get("productWithRefund");
        int ticketQty = Integer.parseInt(param.get("ticketQty"));
        String paymentMethod = param.get("paymentMethod");

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        TtdLandingPageWeb<?> ttdLandingPageWeb = TtdLandingPageWeb.getInstance(session);
        TtdSearchResultPageWeb<?> ttdSearchResultPageWeb = TtdSearchResultPageWeb.getInstance(session);
        DashboardPages<?> dashboardPages = new DashboardPages<>(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);
        TtdBookingFormPageWeb<?> ttdBookingFormPageWeb = TtdBookingFormPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        RefundPageWeb<?> refundPage = RefundPageWeb.getInstance(session);

        step("Open home page");
        homePage.goToHomePage();

        step("Open as logged in user");
        loginPage.loginWithSpecificAccountViaCookie(username, password);

        step("Select IDR as currency");
        homePage.selectCurrency("IDR");

        step("Go to TTD Landing Page");
        ttdLandingPageWeb.goToTtdLandingPage();

        step("Choose location from popular destinations");
        ttdLandingPageWeb.openSearchInput();
        ttdLandingPageWeb.selectPopularDestinations(popularDestination);

        step("Redirect to the SRP with destination: " + popularDestination);
        Assertion.assertThat("SRP have product with 100% refund benefit", ttdSearchResultPageWeb.isRefund100PVisible(), is(true));

        step("Choose product with 100% refund benefit: " + productWithRefund);
        ttdSearchResultPageWeb.chooseProductSrp(true, productWithRefund, totalScrollSrp);

        step("Redirect to the PDP");
        dashboardPages.handleNewWindow();
        Assertion.assertThat("PDP have product with 100% refund benefit", ttdProductDetailPageWeb.isRefund100PVisible(), is(true));

        step("Choose package with 100% refund benefit");
        ttdProductDetailPageWeb.chooseTicketWithRefund();

        step("Submit the total ticket");
        ttdProductDetailPageWeb.setTicketQty(ticketQty, 0);
        ttdProductDetailPageWeb.clickBookButton();

        step("Redirect to the booking form page");
        Assertion.assertThat("Booking form page have section with 100% refund benefit", ttdProductDetailPageWeb.isRefund100PVisible(), is(true));
        Assertion.assertThat("What You Will Get of refund section is displayed", ttdBookingFormPageWeb.isRefundInfoDisplayed(), is(true));

        step("Completing booking form data");
        ttdBookingFormPageWeb.fillContactOrderDetail(true);
        ttdBookingFormPageWeb.clickSameAsContactToggle();

        step("Continue to the payment page");
        ttdBookingFormPageWeb.continueToPaymentPage();
        Assertion.assertThat("Success redirected to payment page", paymentPage.isPaymentPageLoaded(), is(true));
        String orderId = paymentPage.getOrderId();

        step("Open all payment method");
        paymentPage.clickOnSelectPaymentMethod();

        step("Select the payment Method");
        paymentPage.selectPaymentMethod(paymentMethod);

        step("Submit the payment method");
        paymentPage.clickOnPaymentButton();
        Assertion.assertThat("Payment successful page is visible", paymentPage.isPaymentSuccessfulVisible(), is(true));

        step("Check issued on order detail page");
        orderListPage.openActiveTicketViaURL(orderId);
        orderDetailPage.skipCoachmark();
        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.verifyIssuedETicket(orderId), is(true));

        step("Open refund page");
        orderDetailPage.clickRefundButton();
        refundPage.waitForContentLoaderDisappear();

        step("Select random refund reason");
        refundPage.selectRandomRefundReason();
        refundPage.waitForEstimationAmountAppear("30");

        step("Continue to next refund page");
        refundPage.clickButton("Continue", "Lanjutkan");
        refundPage.waitRefundSecondPageLoaded();

        step("Submit refund request");
        refundPage.clickChecklistTnc();
        refundPage.clickButton("Submit", "Ajukan");
        refundPage.clickButton("Process Refund", "Proses Refund");
        refundPage.waitForSubmitRefundLoader();
        Assertion.assertThat("Refund submission is done from Order Page", refundPage.getSuccessRefundFigureContent(), is(either(containsString("Success submit the request")).or(containsString("Pengajuan refund berhasil"))));
    }

    @Author(names = {"Prerna","Manan Kumar"})
    @Module(name = "TTD")
    @Tribe(name = "TTD")
    @Vertical(name = "TTD")
    @Test
    public void TTD_checkTopKeyword()  {
        Map<String, String> param = getParam("TTD_checkTopKeyword");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        TtdLandingPageWeb<?> ttdLandingPageWeb = TtdLandingPageWeb.getInstance(session);

        step("Open home page");
        homePage.goToHomePage();

        step("Go to TTD Landing Page");
        ttdLandingPageWeb.goToTtdLandingPage();

        step("Open search page");
        ttdLandingPageWeb.openSearchInput();

        step("Click Random Keyword");
        String keyword = ttdLandingPageWeb.clickRandomChipKeyword();

        step(keyword+ " is entered in search bar");
        Assertion.assertThat("selected keyword is entered in search bar", ttdLandingPageWeb.getSearchBarInputValue(), is(keyword));

        step("Verify search result related to "+ keyword);
        Assertion.assertThat("Search Result is related to " + keyword, ttdLandingPageWeb.verifySearchResultRelatedToSearchKeyword(), is(true) );
    }

    @Author(names = {"Kartik"})
    @Module(name = "Booking")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void CheckOrderIssuedForUnEligibleLoyalty() throws Exception {
        Map<String, String> param = getParam("CheckOrderIssuedForUnEligibleLoyalty");
        BaseTest.paramsThreadLocal.set(param);

        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String productName = param.get("productName");

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        TtdLandingPageWeb<?> ttdLandingPageWeb = TtdLandingPageWeb.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);

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

        step("Select package");
        ttdProductDetailPageWeb.clickBuyTicketButton();

        step("Verify that the selected package group is displayed");
        ttdProductDetailPageWeb.verifyLoyaltyNotEligible();
    }

    @Author(names = {"Kartik"})
    @Module(name = "Booking")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void verifyDetailsVouchersAndClaimButton() throws Exception {
        Map<String, String> param = getParam("verifyDetailsVouchersAndClaimButton");
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
        TtdBookingFormPageWeb<?> ttdBookingFormPageWeb = TtdBookingFormPageWeb.getInstance(session);
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

        step("Select package: ");
        ttdProductDetailPageWeb.chooseTicket(packageName);

        step("Submit the total ticket");
        ttdProductDetailPageWeb.setTicketQty(1, 0);
        ttdProductDetailPageWeb.clickBookButton();

        step("Modal (pop up UI) will appear showing the Voucher Details");
        ttdBookingFormPageWeb.clickClaimVoucher();
        ttdBookingFormPageWeb.verifyClaimVoucherPopUp();
    }

    @Author(names = {"Prerna"})
    @Module(name = "Booking")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void checkIssuedOrderWithExpiryDate() throws FailedLoginException, InterruptedException, IOException {
        Map<String, String> param = getParam("checkIssuedOrderWithExpiryDate");
        BaseTest.paramsThreadLocal.set(param);
        String productName = param.get("productName");
        String packageName = param.get("packageName");
        String paymentGroup = param.get("paymentGroup");
        String paymentMethod = param.get("paymentMethod");
        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String cardNumber = param.get("cardNumber");
        String cardMonth = param.get("cardMonth");
        String cvv = param.get("cvv");
        String cardHolderName = param.get("cardHolderName");
        String TwoCTwoP = param.get("TwoCTwoP");
        String cardOtp = param.get("cardOtp");

        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        TtdLandingPageWeb<?> ttdLandingPageWeb = TtdLandingPageWeb.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);
        TtdBookingFormPageWeb<?> ttdBookingFormPageWeb = TtdBookingFormPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        PaymentPage<?> landingPage = new PaymentPage<>(session);

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

        step("Select package: " + packageName);
        ttdProductDetailPageWeb.chooseTicket(packageName);

        step("Get validity period text");
        String validityPeriod = ttdProductDetailPageWeb.getValidityPeriodText();

        step("Submit the total ticket");
        ttdProductDetailPageWeb.setTicketQty(1, 0);
        ttdProductDetailPageWeb.clickBookButton();

        Assertion.assertThat("Validity date is there on Booking form", ttdBookingFormPageWeb.verifyValidityText(validityPeriod) ,is(true));

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

        step("Input the card details");
        landingPage.inputCardNumber(cardNumber);
        landingPage.inputCardMonth(cardMonth);
        landingPage.inputCardCvv(cvv);
        landingPage.inputCardHolderName(cardHolderName);

        step("Submit the payment method");
        paymentPage.clickOnPaymentButton();

        if(TwoCTwoP.equals("true")){
            step("Verify 2c2p page is loaded");
            landingPage.is2c2pConfirmationPageLoaded();

            step("Input otp on 2c2p page");
            landingPage.inputOtpOn2C2PPage(cardOtp);

            step("Click on submit button");
            landingPage.clickOnSubmitButton();
        }

        Assertion.assertThat("Payment successful page is visible", paymentPage.isPaymentSuccessfulVisible(), is(true));

        step("Check issued on order detail page");
        orderListPage.openActiveTicketViaURL(orderId);

        step("Get PDF Content");
        orderDetailPage.clickEticketFromHeader();
        Assertion.assertThat("Verify validity text in E-Voucher", orderDetailPage.verifyValidityTextFromEVoucher(validityPeriod), is(true));
    }

    @Author(names = {"Prerna"})
    @Module(name = "Booking")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void checkIssuedOrderHavingSession() throws FailedLoginException, InterruptedException, IOException {
        Map<String, String> param = getParam("checkIssuedOrderHavingSession");
        BaseTest.paramsThreadLocal.set(param);
        String productName = param.get("productName");
        String packageName = param.get("packageName");
        String sessionName = param.get("sessionName");
        String paymentGroup = param.get("paymentGroup");
        String paymentMethod = param.get("paymentMethod");
        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String cardNumber = param.get("cardNumber");
        String cardMonth = param.get("cardMonth");
        String cvv = null;
        try {
            cvv = param.get("cvv");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        String cardHolderName = param.get("cardHolderName");
        String TwoCTwoP = param.get("TwoCTwoP");
        String cardOtp = param.get("cardOtp");

        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        TtdLandingPageWeb<?> ttdLandingPageWeb = TtdLandingPageWeb.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);
        TtdBookingFormPageWeb<?> ttdBookingFormPageWeb = TtdBookingFormPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        PaymentPage<?> landingPage = new PaymentPage<>(session);

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

        step("Select package: " + packageName);
        ttdProductDetailPageWeb.chooseTicket(packageName);

        step("Select session " +sessionName );
        ttdProductDetailPageWeb.selectSession(sessionName);

        step("Submit the total ticket");
        ttdProductDetailPageWeb.setTicketQty(1, 0);
        ttdProductDetailPageWeb.clickBookButton();

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

        step("Input the card details");
        landingPage.inputCardNumber(cardNumber);
        landingPage.inputCardMonth(cardMonth);
        landingPage.inputCardCvv(cvv);
        landingPage.inputCardHolderName(cardHolderName);

        step("Submit the payment method");
        paymentPage.clickOnPaymentButton();

        if(TwoCTwoP.equals("true")){
            step("Verify 2c2p page is loaded");
            landingPage.is2c2pConfirmationPageLoaded();

            step("Input otp on 2c2p page");
            landingPage.inputOtpOn2C2PPage(cardOtp);

            step("Click on submit button");
            landingPage.clickOnSubmitButton();
        }

        Assertion.assertThat("Payment successful page is visible", paymentPage.isPaymentSuccessfulVisible(), is(true));

        step("Check issued on order detail page");
        orderListPage.openActiveTicketViaURL(orderId);

        step("Get PDF Content");
        orderDetailPage.clickEticketFromHeader();
        Assertion.assertThat("Verify session in E-Voucher", orderDetailPage.verifyTextFromEVoucher(sessionName), is(true));
    }

    @Author(names = {"Aditi"})
    @Module(name = "SearchResult")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void checkSRPFilterFeature()  {
        Map<String, String> param = getParam("checkSRPFilterFeature");
        BaseTest.paramsThreadLocal.set(param);

        String filter1 = param.get("filter1");
        String filter2 = param.get("filter2");

        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        TtdLandingPageWeb<?> ttdLandingPageWeb = TtdLandingPageWeb.getInstance(session);
        TtdSearchResultPageWeb<?> ttdSearchResultPageWeb = TtdSearchResultPageWeb.getInstance(session);

        step("Open the custom search page link");
        ttdSearchResultPageWeb.openSearchPageCustom();

        step("Select the filters");
        ttdSearchResultPageWeb.chooseFilter(filter1, FilterType.FEATURES);
        ttdSearchResultPageWeb.chooseFilter(filter2, FilterType.FEATURES);

        step("Check that the filters have been applied");
        Assertion.assertThat("Green dot is visible", ttdSearchResultPageWeb.isGreenDotVisible(), is(true) );

        step("Check whether the updated products are according to the filter applied");
        boolean checkFilter1 = ttdSearchResultPageWeb.isFilterApplied(filter1);
        boolean checkFilter2 = ttdSearchResultPageWeb.isFilterApplied(filter2);

        Assertion.assertThat("Both selected filters should be applied to the displayed products", checkFilter1 && checkFilter2, is(true));
    }

    @Author(names = {"Prerna"})
    @Module(name = "PDP")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void verifyCampaignOnChildIsSameAsMaster() throws Exception {
        Map<String, String> param = getParam("verifyCampaignOnChildIsSameAsMaster");
        BaseTest.paramsThreadLocal.set(param);

        String productName = param.get("productName");

        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        TtdLandingPageWeb<?> ttdLandingPageWeb = TtdLandingPageWeb.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);

        step("Open home page");
        homePage.goToHomePage();

        step("Go to TTD Landing Page");
        ttdLandingPageWeb.goToTtdLandingPage();

        step("Search and select product item");
        ttdLandingPageWeb.inputSearchKeyword(productName);
        ttdLandingPageWeb.selectRecommendedActivity(productName);

        step("get master product campaign");
        String masterCampaign = ttdProductDetailPageWeb.getMasterCampaign();

        step("get child campaign");
        String childCampaign = ttdProductDetailPageWeb.getChildCampaign();

        Assertion.assertThat("CHILD campaign is same as MASTER campaign", masterCampaign, is(childCampaign));
    }

    @Author(names = {"kartik"})
    @Module(name = "NewActivities")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void checkNewActivitiesSection() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("checkNewActivitiesSection");
        BaseTest.paramsThreadLocal.set(param);
        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = 2;

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        TtdLandingPageWeb<?> ttdLandingPageWeb = TtdLandingPageWeb.getInstance(session);
        TtdSearchResultPageWeb<?> ttdSearchResultPageWeb = TtdSearchResultPageWeb.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);

        step("Open home page");
        homePage.goToHomePage();

        step("Open as logged in user");
        loginPage.loginIfNotWithSameEmail(username, password, otp, retryCounter);

        step("Select IDR as currency");
        homePage.selectCurrency("IDR");

        step("Go to TTD Landing Page");
        ttdLandingPageWeb.goToTtdLandingPage();

        step("Check total of the New Activities ");
        ttdLandingPageWeb.openSearchInput();
        Assertion.assertThat("New Activities should have 10 items", ttdLandingPageWeb.getNewActivitesAmount(), is(10));

        step("Choose the destination that have lots of activities");
        String selectedProduct = ttdLandingPageWeb.selectActivityFromNewActivitySection();

        step("Is on pdp page?");
        Assertion.assertThat("Selected product PDP is opened", ttdProductDetailPageWeb.isCorrectProductOpened(selectedProduct), is(true));

        step("user click See All, user should be directed to SRP all location with filter Newly Added");
        ttdLandingPageWeb.openSearchInput();
        ttdLandingPageWeb.clickSeeAllNewActivites();

        step("Verify that the All locations text is displayed");
        Assertion.assertThat("All locations badge is present", ttdSearchResultPageWeb.checkAllLocationsBadgeSRP(), is(true));
        Assertion.assertThat("Green dot icon is Displayed on FILTER", ttdSearchResultPageWeb.isGreenDotVisible(), is(true));
    }

    @Author(names = {"kartik"})
    @Module(name = "PopularDestinations")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void checkPopularDestinationSeeAllBtn() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("checkPopularDestinationSeeAllBtn");
        BaseTest.paramsThreadLocal.set(param);
        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        TtdLandingPageWeb<?> ttdLandingPageWeb = TtdLandingPageWeb.getInstance(session);
        TtdSearchResultPageWeb<?> ttdSearchResultPageWeb = TtdSearchResultPageWeb.getInstance(session);

        step("Open home page");
        homePage.goToHomePage();

        step("Open as logged in user");
        loginPage.loginIfNotWithSameEmail(username, password, otp, retryCounter);

        step("Select IDR as currency");
        homePage.selectCurrency("IDR");

        step("Go to TTD Landing Page");
        ttdLandingPageWeb.goToTtdLandingPage();

        step("Click see all button");
        ttdLandingPageWeb.openSearchInput();
        ttdLandingPageWeb.clickSeeAllPopularDestination();

        step("Verify Step 1");
        Assertion.assertThat("Assert if redirected to destination list page", ttdLandingPageWeb.verifyDestinationListPage(), is(true));

        step("Check total of the popular destinations");
        ttdLandingPageWeb.goToTtdLandingPage();
        ttdLandingPageWeb.openSearchInput();

        Assertion.assertThat("Popular destinations should have 10 items", ttdLandingPageWeb.getPopularDestinationsAmount(), is(10));

        step("Choose the destination that have lots of activities");
        String selectedDestinations = ttdLandingPageWeb.selectPopularDestinationsWithLotsActivity();
        Assertion.assertThat("Redirect to the SRP with the selected destinations", ttdSearchResultPageWeb.getSearchLocation(), is(selectedDestinations));
    }

    @Author(names = {"Kartik"})
    @Module(name = "Booking")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void completeBookingMasterDataWeb() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("completeBookingMasterDataWeb");
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
        TtdBookingFormPageWeb<?> ttdBookingFormPageWeb = TtdBookingFormPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);

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

        step("Select package: " + packageName);
        ttdProductDetailPageWeb.chooseTicket(packageName);

        step("Submit the total ticket");
        ttdProductDetailPageWeb.setTicketQty(1, 0);
        ttdProductDetailPageWeb.clickBookButton();

        step("Completing booking form data");
        ttdBookingFormPageWeb.fillContactOrderDetail(true);
        ttdBookingFormPageWeb.fillGuestDetail();

        step("Continue to payment page");
        ttdBookingFormPageWeb.continueToPaymentPage();

        step("Redirected to the payment page");
        Assertion.assertThat("Success redirected to payment page", paymentPage.getOrderId(), is(notNullValue()));
    }

    //OTW Testcase
    @Author(names = {"Kartik"})
    @Module(name = "SearchSuggestion")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void searchAreaLoginWeb() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("searchAreaLoginWeb");
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        TtdLandingPageWeb<?> ttdLandingPageWeb = TtdLandingPageWeb.getInstance(session);
        TtdSearchResultPageWeb<?> ttdSearchResultPageWeb = TtdSearchResultPageWeb.getInstance(session);

        String username = map.get("username");
        String password = map.get("password");
        String otp = map.get("otp");
        int retryCounter = Integer.parseInt(map.get("retryCounter"));
        String area = map.get("area");

        BaseTest.paramsThreadLocal.set(map);
        step("Open home page");
        homePage.goToHomePage();

        step("Open as logged in user");
        loginPage.loginIfNotWithSameEmail(username, password, otp, retryCounter);

        step("Select IDR as currency");
        homePage.selectCurrency("IDR");

        step("Go to TTD Landing Page");
        ttdLandingPageWeb.goToTtdLandingPage();

        step("Search the area: " + area);
        ttdLandingPageWeb.inputSearchKeyword(area);
        ttdLandingPageWeb.selectRecommendedActivity(area);

        step("Select first recommended area");
        Assertion.assertThat("Show destination result by the destination name", ttdSearchResultPageWeb.getSearchLocation(), is(area));
    }

    @Author(names = {"Aditi"})
    @Module(name = "Search")
    @Tribe(name = "SearchPage")
    @Vertical(name = "TTD")
    @Test
    public void checkPopularEventSection() throws Exception {
        Map<String, String> param = getParam("checkPopularEventSection");
        BaseTest.paramsThreadLocal.set(param);

        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        TtdLandingPageWeb<?> ttdLandingPageWeb = TtdLandingPageWeb.getInstance(session);
        TtdSearchResultPageWeb<?> ttdSearchResultPageWeb = TtdSearchResultPageWeb.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);

        step("Open home page");
        homePage.goToHomePage();

        step("Open as logged in user");
        loginPage.loginIfNotWithSameEmail(username, password, otp, retryCounter);

        step("Select IDR as currency");
        homePage.selectCurrency("IDR");

        step("Go to TTD Landing Page");
        ttdLandingPageWeb.goToTtdLandingPage();

        step("Click on the Search field");
        ttdLandingPageWeb.openSearchInput();

        step("Check that the popular events section is present on the search page");
        ttdLandingPageWeb.isPopularEventsPresent();
        Assertion.assertThat("Events should be have 1-10 of the rank", ttdLandingPageWeb.checkEventsToDoRank(), is(10));

        step("Select any product");
        String expectedProductName = ttdLandingPageWeb.selectEventProduct();
        Assertion.assertThat("Selected product PDP is opened", ttdProductDetailPageWeb.isCorrectProductOpened(expectedProductName), is(true));
        ttdProductDetailPageWeb.clickBack();

        step("Click see all events button");
        ttdLandingPageWeb.goToTtdLandingPage();
        ttdLandingPageWeb.openSearchInput();
        ttdLandingPageWeb.clickSeeAllEventsButton();
        Assertion.assertThat("Events should be the selected category on the SRP", ttdSearchResultPageWeb.getSelectedEventFilter(), either(equalToIgnoringCase("all events")).or(equalToIgnoringCase("semua event")));
    }

    @Author(names = {"Aditi"})
    @Module(name = "Search")
    @Tribe(name = "SearchPage")
    @Vertical(name = "TTD")
    @Test
    public void checkPopularAttractionSection() throws Exception {
        Map<String, String> param = getParam("checkPopularAttractionSection");
        BaseTest.paramsThreadLocal.set(param);

        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        TtdLandingPageWeb<?> ttdLandingPageWeb = TtdLandingPageWeb.getInstance(session);
        TtdSearchResultPageWeb<?> ttdSearchResultPageWeb = TtdSearchResultPageWeb.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);

        step("Open home page");
        homePage.goToHomePage();

        step("Open as logged in user");
        loginPage.loginIfNotWithSameEmail(username, password, otp, retryCounter);

        step("Select IDR as currency");
        homePage.selectCurrency("IDR");

        step("Go to TTD Landing Page");
        ttdLandingPageWeb.goToTtdLandingPage();

        step("Click on the Search field");
        ttdLandingPageWeb.openSearchInput();

        step("Check that the popular attraction section is present on the search page");
        ttdLandingPageWeb.isPopularAttractionPresent();
        Assertion.assertThat("Attractions should be have 1-10 of the rank", ttdLandingPageWeb.checkAttractionsToDoRank(), is(10));

        step("Select any product");
        String expectedProductName = ttdLandingPageWeb.selectAttractionProduct();
        Assertion.assertThat("Selected product PDP is opened", ttdProductDetailPageWeb.isCorrectProductOpened(expectedProductName), is(true));
        ttdProductDetailPageWeb.clickBack();

        step("Click see all attractions button");
        ttdLandingPageWeb.goToTtdLandingPage();
        ttdLandingPageWeb.openSearchInput();
        ttdLandingPageWeb.clickSeeAllAttractionsButton();
        Assertion.assertThat("Attractions should be the selected category on the SRP", ttdSearchResultPageWeb.getSelectedFilter(), either(equalToIgnoringCase("all attractions")).or(equalToIgnoringCase("semua atraksi")));
    }

    @Author(names = {"Aditi"})
    @Module(name = "Booking")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void checkOrderIssuedWithBookingQuantityRestricted() throws Exception {
        Map<String, String> param = getParam("checkOrderIssuedWithBookingQuantityRestricted");
        BaseTest.paramsThreadLocal.set(param);

        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String productName = param.get("productName");
        String packageName = param.get("packageName");
        int wrongQuantity = Integer.parseInt(param.get("wrongQuantity"));
        int rightQuantity = Integer.parseInt(param.get("rightQuantity"));
        int ticketIndex = Integer.parseInt(param.get("ticketIndex"));
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

        step("Verify that the correct product has been opened");
        Assertion.assertThat("Is correct product opened", ttdProductDetailPageWeb.isCorrectProductOpened(productName), is(true));

        step("Select the package");
        ttdProductDetailPageWeb.clickSeePackagesButton();
        ttdProductDetailPageWeb.chooseTicket(packageName);
        ttdProductDetailPageWeb.setTicketQty(wrongQuantity, 0);
        ttdProductDetailPageWeb.clickBookButton();
        ttdBookingFormPageWeb.fillGuestDetail();
        ttdBookingFormPageWeb.continueToPaymentPage();

        step("Error message will be displayed");
        Assertion.assertThat("Is error message displayed regarding the booking quantity?", ttdBookingFormPageWeb.isQuantityErrorMessageDisplayed(), is(true));

        step("Change ticket quantity");
        ttdBookingFormPageWeb.clickChangeQuantityButton();

        step("Verify that the correct product has been opened");
        ttdLandingPageWeb.goToTtdLandingPage();
        ttdLandingPageWeb.inputSearchKeyword(productName);
        ttdLandingPageWeb.selectRecommendedActivity(productName);
        Assertion.assertThat("Is correct product opened", ttdProductDetailPageWeb.isCorrectProductOpened(productName), is(true));

        step("Make booking using valid quantity");
        ttdProductDetailPageWeb.chooseTicket(packageName);
        ttdProductDetailPageWeb.setTicketQty(rightQuantity, ticketIndex);
        ttdProductDetailPageWeb.clickBookButton();
        ttdBookingFormPageWeb.fillGuestDetail();
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

    @Author(names = {"Aditi"})
    @Module(name = "Booking")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void checkProductPriceCalendarAndEligibleLoyalty() throws Exception {
        Map<String, String> param = getParam("checkProductPriceCalendarAndEligibleLoyalty");
        BaseTest.paramsThreadLocal.set(param);

        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String productName = param.get("productName");
        String packageName = param.get("packageName");
        int quantity = Integer.parseInt(param.get("quantity"));
        int ticketIndex = Integer.parseInt(param.get("ticketIndex"));
        int visitDateIndex = Integer.parseInt(param.get("visitDateIndex"));
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

        step("Select IDR as currency");
        homePage.selectCurrency("IDR");

        step("Go to TTD Landing Page");
        ttdLandingPageWeb.goToTtdLandingPage();

        step("Search and select product item");
        ttdLandingPageWeb.inputSearchKeyword(productName);
        ttdLandingPageWeb.selectRecommendedActivity(productName);

        step("User should be prompted to login first");
        Assertion.assertThat("Is user prompted to login?", ttdProductDetailPageWeb.isLoginMessageDisplayed(), is(true));

        step("User login now");
        loginPage.loginIfNotWithSameEmail(username, password, otp, retryCounter);

        step("Verify that the correct product has been opened");
        homePage.goToHomePage();
        homePage.selectCurrency("IDR");
        ttdLandingPageWeb.goToTtdLandingPage();
        ttdLandingPageWeb.inputSearchKeyword(productName);
        ttdLandingPageWeb.selectRecommendedActivity(productName);
        Assertion.assertThat("Is correct product opened", ttdProductDetailPageWeb.isCorrectProductOpened(productName), is(true));

        step("Choose visit date randomly");
        ttdProductDetailPageWeb.chooseVisitDateLoyaltyMember(visitDateIndex, packageName);

        step("Select ticket button now enabled when successfully login as Diamond member tier");
        ttdProductDetailPageWeb.chooseTicket(packageName);

        step("Check that price on weekend (saturday and sunday) are cheaper than weekdays");
        Assertion.assertThat("Is the price on weekend (saturday and sunday) are cheaper than weekdays?", ttdProductDetailPageWeb.compareWeekdayAndWeekendPrice(), is(true));

        step("Choose a visit date");
        ttdProductDetailPageWeb.chooseVisitDate(visitDateIndex);

        step("Add number of people");
        ttdProductDetailPageWeb.setTicketQty(quantity, ticketIndex);
        ttdProductDetailPageWeb.clickBookButton();
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

    @Author(names = {"Aditi"})
    @Module(name = "Booking")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void checkPartnerPageNearestLocationOff() throws Exception {
        Map<String, String> param = getParam("checkPartnerPageNearestLocationOff");
        BaseTest.paramsThreadLocal.set(param);

        String productName = param.get("productName");

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        TtdLandingPageWeb<?> ttdLandingPageWeb = TtdLandingPageWeb.getInstance(session);
        TtdSearchResultPageWeb<?> ttdSearchResultPageWeb = TtdSearchResultPageWeb.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);

        step("Open home page");
        homePage.goToHomePage();

        step("Select IDR as currency");
        homePage.selectCurrency("IDR");

        step("Go to TTD Landing Page");
        ttdLandingPageWeb.goToTtdLandingPage();

        step("Check if the user location is OFF");
        Assertion.assertThat("User location is OFF", ttdLandingPageWeb.isUserLocationOff(), is(true));

        step("Open the partner page");
        ttdLandingPageWeb.openPartnerPage(productName);

        step("Store the first location shown on the page");
        String locationOffFirstLocation = ttdProductDetailPageWeb.getLocationFromLocator();
        String locationDistanceShouldBeOff = ttdProductDetailPageWeb.getNearestLocationDistance();


        step("Click on the Nearest button");
        ttdProductDetailPageWeb.clickNearestLocationButton();

        step("Store the first location shown on the Nearest page");
        String nearestFirstLocation = ttdProductDetailPageWeb.getLocationFromLocator();
        String locationDistanceShouldBeOn = ttdProductDetailPageWeb.getNearestLocationDistance();

        if(locationOffFirstLocation.equals(nearestFirstLocation)){
            Assertion.assertThat("Distance should not be shown when location is off",
                    locationDistanceShouldBeOff, nullValue());
            Assertion.assertThat("Distance should be shown when location is on",
                    locationDistanceShouldBeOn, is(notNullValue()));
            Assertion.assertThat("Locations are the same",
                    locationOffFirstLocation, is(nearestFirstLocation));
            Assertion.assertThat("distance should contain m or km",
                    locationDistanceShouldBeOn, matchesPattern(".*\\d+(\\.\\d+)?\\s?(m|km).*"));
        } else {
            step("Verify that nearest location should be different from the default location");
            Assertion.assertThat("Locations are different", locationOffFirstLocation, not(nearestFirstLocation));
        }
    }

    @Author(names = {"Aditi"})
    @Module(name = "SearchResult")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void checkBreadCrumbsDestination() throws InterruptedException {
        Map<String, String> param = getParam("checkBreadCrumbsDestination");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        TtdLandingPageWeb<?> ttdLandingPageWeb = TtdLandingPageWeb.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);

        String productName = param.get("productName");
        int level = Integer.parseInt(param.get("level"));

        step("Open home page");
        homePage.goToHomePage();

        step("Go to TTD Landing Page");
        ttdLandingPageWeb.goToTtdLandingPage();

        step("Search and select product item");
        ttdLandingPageWeb.inputSearchKeyword(productName);
        ttdLandingPageWeb.selectRecommendedActivity(productName);

        step("Verify that the product page opened");
        Assertion.assertThat("Product page is opened", ttdProductDetailPageWeb.isCorrectProductOpened(productName), is(true));

        step("Check that the breadcrumb navbar is present");
        Assertion.assertThat("Breadcrumb navbar is present", ttdProductDetailPageWeb.isBreadcrumbNavbarPresent(), is(true));

        step("Check that the user is able to navigate to the different pages with the help of the breadcrumb");
        ttdProductDetailPageWeb.clickBreadcrumbNavbar(level);
        ttdProductDetailPageWeb.goBackToThePreviousPage();

        step("Verify that the user has been navigated back to the product page");
        Assertion.assertThat("Product page is opened", ttdProductDetailPageWeb.isCorrectProductOpened(productName), is(true));
    }

    @Author(names = {"Aditi"})
    @Module(name = "TTD")
    @Tribe(name = "TTD")
    @Vertical(name = "TTD")
    @Test
    public void TTD_checkCompleteBookingNonIDRviaCard() throws InterruptedException, FailedLoginException {
        Map<String, String> param = getParam("TTD_checkCompleteBookingNonIDRviaCard");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        TtdLandingPageWeb<?> ttdLandingPageWeb = TtdLandingPageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);
        TtdBookingFormPageWeb<?> ttdBookingFormPageWeb = TtdBookingFormPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        DashboardPages<?> dashboardPages = new DashboardPages<>(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);


        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String currency = param.get("currency");
        String productName = param.get("productName");
        String cardNumber = param.get("cardNumber");
        String cardMonth = param.get("cardMonth");
        String cardCvv = param.get("cardCvv");
        String cardHolderName = param.get("cardHolderName");

        step("Open home page");
        homePage.goToHomePage();

        step("Open as logged in user");
        loginPage.loginAsGuest();
        loginPage.loginIntoWeb(username, password,otp,retryCounter);

        step("Change currency to " + currency);
        homePage.selectCurrency(currency);

        step("Go to TTD Landing Page");
        ttdLandingPageWeb.goToTtdLandingPage();

        step("Search and select product item");
        ttdLandingPageWeb.inputSearchKeyword(productName);
        ttdLandingPageWeb.selectRecommendedActivity(productName);

        step("Verify that the product page opened");
        dashboardPages.handleNewWindow();
        Assertion.assertThat("PDP have product with 100% refund benefit", ttdProductDetailPageWeb.isRefund100PVisible(), is(true));

        step("Choose package with 100% refund benefit");
        ttdProductDetailPageWeb.chooseTicketWithRefund();

        step("Submit the total ticket");
        ttdProductDetailPageWeb.setTicketQty(1, 0);
        ttdProductDetailPageWeb.clickBookButton();

        step("Completing booking form data");
        ttdBookingFormPageWeb.fillContactOrderDetail(true);
        ttdBookingFormPageWeb.clickSameAsContactToggle();

        step("Continue to the payment page");
        ttdBookingFormPageWeb.continueToPaymentPage();
        Assertion.assertThat("Success redirected to payment page", paymentPage.isPaymentPageLoaded(), is(true));
        String orderId = paymentPage.getOrderId();

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

        step("Verify order ID on order list page matches with the order ID from payment successful page");
        orderListPage.openActiveTicketViaURL(orderId);
        Assertion.assertThat("Order ID on order list page should match with the order ID from payment successful page", orderDetailPage.getOrderId(), is(orderId));
    }

    @Author(names = {"Mridul"})
    @Module(name = "SearchSuggestion")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void checkRecentlySearchedWeb() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("checkRecentlySearchedWeb");
        BaseTest.paramsThreadLocal.set(param);
        String searchKeyword = param.get("searchKeyword");
        String username = param.get("username");
        String password = param.get("password");

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        TtdLandingPageWeb<?> ttdLandingPageWeb = TtdLandingPageWeb.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);

        step("Open home page with login session");
        homePage.goToHomePage();
        loginPage.loginWithSpecificAccountViaCookie(username, password);

        step("Open To Do section on home page");
        ttdLandingPageWeb.goToTtdLandingPage();

        step("Click on search suggestion");
        ttdLandingPageWeb.openSearchInput();

        step("Search keyword: " + searchKeyword);
        ttdLandingPageWeb.inputSearchKeyword(searchKeyword);

        step("Click any product from the suggestion box");
        ttdLandingPageWeb.selectFirstProductSuggestion();

        step("Product should redirect to the PDP page");
        Assertion.assertThat("User should be redirected to the PDP page", ttdProductDetailPageWeb.isProductDetailPageDisplayed(), is(true));

        step("Come back to the To Do landing page");
        ttdLandingPageWeb.goToTtdLandingPage();

        step("Click on search suggestion again");
        ttdLandingPageWeb.openSearchInput();

        step("Recent search should be displayed once search is initiated and contains: " + searchKeyword);
        Assertion.assertThat("Recent search section should be displayed", ttdLandingPageWeb.isRecentSearchDisplayed(), is(true));
        Assertion.assertThat("Recent search should contain the searched keyword", ttdLandingPageWeb.getRecentSearch(), containsStringIgnoringCase(searchKeyword));

        step("Clear recent search for next run");
        ttdLandingPageWeb.clickClearRecentSearch();
        Assertion.assertThat("Recent search should be cleared", ttdLandingPageWeb.isRecentSearchCleared(), is(true));
    }

    @Author(names = {"Mridul"})
    @Module(name = "SearchSuggestion")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void checkRecentlyViewedWeb() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("checkRecentlyViewedWeb");
        BaseTest.paramsThreadLocal.set(param);
        String username = param.get("username");
        String password = param.get("password");
        String searchKeyword = param.get("searchKeyword");

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        TtdLandingPageWeb<?> ttdLandingPageWeb = TtdLandingPageWeb.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);

        step("Open home page and login with the account");
        homePage.goToHomePage();
        loginPage.loginWithSpecificAccountViaCookie(username, password);

        step("Open To Do section on home page");
        ttdLandingPageWeb.goToTtdLandingPage();

        step("Click on search suggestion box");
        ttdLandingPageWeb.openSearchInput();

        step("Search keyword: " + searchKeyword);
        ttdLandingPageWeb.inputSearchKeyword(searchKeyword);

        step("Click on the product and arrive on the PDP page");
        ttdLandingPageWeb.selectRecommendedActivity(searchKeyword);
        Assertion.assertThat("User should be redirected to the PDP page", ttdProductDetailPageWeb.isProductDetailPageDisplayed(), is(true));
        String viewedProduct = ttdProductDetailPageWeb.getProductTitle();
        Assertion.assertThat("Viewed product title should contain the searched keyword", viewedProduct, containsStringIgnoringCase(searchKeyword));

        step("Go back to the To Do landing page");
        ttdLandingPageWeb.goToTtdLandingPage();

        step("Check Recently Viewed Section on Landing page");
        Assertion.assertThat("Recently Viewed section should be displayed on the landing page", ttdLandingPageWeb.isRecentlyViewedSectionDisplayed(viewedProduct), is(true));

        step("Observe recently viewed section - same product should be displayed: " + viewedProduct);
        Assertion.assertThat("Same product should be displayed in the recently viewed section", ttdLandingPageWeb.isProductInRecentlyViewedInSearchPopUp(viewedProduct), is(true));
    }
}