package com.tiket.web.test.ttd;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.apiPage.Unm;
import com.tiket.model.FilterType;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Environment;
import com.tiket.model.Platform;
import com.tiket.model.Session;
import com.tiket.page.ttd.TTDPage;
import com.tiket.model.SortItem;
import com.tiket.pojo.member.RegisterNewAccountData;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.testbase.Configs;
import com.tiket.web.dashboard.page.member_dashboard.MemberLoginPage;
import com.tiket.web.page.order.OrderDetail.OrderDetailWeb;
import com.tiket.web.page.order.OrderList.OrderListWeb;
import com.tiket.web.page.platform.payment.PaymentPageWeb;
import com.tiket.web.page.ttd.TtdBookingFormPage.TtdBookingFormPageWeb;
import com.tiket.web.dWeb.page.payment.PaymentPage;
import com.tiket.web.page.ttd.CMS.TTDInventoryManagementPage;
import com.tiket.web.page.ttd.TtdPackageDetailPage.TtdPackageDetailPageWeb;
import com.tiket.web.page.ttd.TtdPdpInfo.TtdPdpInfoWeb;
import com.tiket.web.page.ttd.TtdProductDetailPage.TtdProductDetailPageMweb;
import com.tiket.web.page.ttd.TtdProductDetailPage.TtdProductDetailPageWeb;
import com.tiket.web.page.ttd.TtdSearchResultPage.TtdSearchResultPageWeb;
import com.tiket.web.page.common.home.HomePageWeb;
import com.tiket.web.page.common.home.LoginPageWeb;
import com.tiket.web.page.ttd.TtdLandingPage.TtdLandingPageWeb;
import com.tiket.web.page.ttd.TtdPartnerPage.TtdPartnerPageWeb;
import org.hamcrest.Matchers;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;


import javax.security.auth.login.FailedLoginException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ExecutionException;

import static com.tiket.core.CoreFunctions.splitText;
import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.Matchers.*;

public class TtdTest extends BaseTest {
    @Author(names = {"Annisa"})
    @Module(name = "Home Page")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void searchEventRandomPriority() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("searchEventRandomPriority");
        BaseTest.paramsThreadLocal.set(param);
        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        int ticketQty = Integer.parseInt(param.get("ticketQty"));
        String paymentGroup = param.get("paymentGroup");
        String paymentMethod = param.get("paymentMethod");
        int randomRank = Integer.parseInt(param.get("randomRank"));
        String packageName = param.get("package");


        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        TtdLandingPageWeb<?> ttdLandingPageWeb = TtdLandingPageWeb.getInstance(session);
        TtdSearchResultPageWeb<?> ttdSearchResultPageWeb = TtdSearchResultPageWeb.getInstance(session);
        TtdPdpInfoWeb<?> ttdPdpInfoWeb = TtdPdpInfoWeb.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);
        TtdBookingFormPageWeb<?> ttdBookingFormPageWeb = TtdBookingFormPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        OrderListWeb<?> orderListWeb = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailWeb = OrderDetailWeb.getInstance(session);

        step("Open home page");
        homePage.goToHomePage();

        step("Login as " + username + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(username, password);

        step("Select IDR as currency");
        homePage.selectCurrency("IDR");

        step("Go to TTD Landing Page");
        ttdLandingPageWeb.goToTtdLandingPage();

        step("Check rank of the popular event");
        ttdLandingPageWeb.openSearchInput();
        Assertion.assertThat("popular event section on search suggestion page with 10 events having count 1-10", ttdLandingPageWeb.checkPopularEventsRank(10), is(true));

        step("Choose random rank of the popular event");
        ttdLandingPageWeb.selectPopularEvents(randomRank);

        step("Click the 'Buy ticket now'");
        ttdPdpInfoWeb.selectPdpInfoButton();

        step("Select package randomly");
        ttdProductDetailPageWeb.chooseTicket(packageName);

        step("Submit the total ticket");
        ttdProductDetailPageWeb.setTicketQty(ticketQty, 0);
        ttdProductDetailPageWeb.clickBookButton();

        step("Completing booking form data");
        ttdBookingFormPageWeb.fillContactOrderDetail(true);
        ttdBookingFormPageWeb.fillGuestDetail();

        step("Continue to the payment page");
        ttdBookingFormPageWeb.continueToPaymentPage();
        ttdBookingFormPageWeb.selectSeatIfAvailable("random");

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
        Assertion.assertThat("Payment successful page is visible", paymentPage.isPaymentSuccessfulVisible(), is(true));

        step("Check issued on order detail page");
        orderListWeb.openActiveTicketViaURL(orderId);
        orderDetailWeb.skipCoachmark();
        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailWeb.verifyIssuedETicket(), is(true));

    }

    @Author(names = {"Annisa"})
    @Module(name = "PDP")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void newEventPdpSoldOut() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("newEventPdpSoldOut");
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
        TtdSearchResultPageWeb<?> ttdSearchResultPageWeb = TtdSearchResultPageWeb.getInstance(session);
        TtdPdpInfoWeb<?> ttdPdpInfoWeb = TtdPdpInfoWeb.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);
        TtdBookingFormPageWeb<?> ttdBookingFormPageWeb = TtdBookingFormPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);

        step("Open home page");
        homePage.goToHomePage();

        step("Open as logged in user");
        loginPage.loginIfNotWithSameEmail(username, password, otp, retryCounter);

        step("Go to TTD Landing Page");
        ttdLandingPageWeb.goToTtdLandingPage();

        step("Search and select product item");
        ttdLandingPageWeb.inputSearchKeyword(productName);
        ttdLandingPageWeb.selectRecommendedActivity(productName);

        step("Verify that the correct product has been opened");
        Assertion.assertThat("Is correct product opened", ttdPdpInfoWeb.isCorrectProductOpened(productName), is(true));

        step("Verify PDP info button label");
        String infoButtonLabel = ttdPdpInfoWeb.getInfoButtonLabel();

        step("Verify label is sold out or terjual habis");
        Assertion.assertThat("Button label is sold out or terjual habis", infoButtonLabel, anyOf(containsString("Sold out"),containsString("Terjual habis")));

    }

    @Author(names = {"Aditi", "Alden"})
    @Module(name = "Booking")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void checkReviewSectionForPartner() throws Exception {
        Map<String, String> param = getParam("checkReviewSectionForPartner");
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

        step("Select Attractions");
        Assertion.assertThat("Attraction page has been opened", ttdLandingPageWeb.isAttractionPageOpened(), is(true));

        step("Search and select product item");
        ttdProductDetailPageWeb.openPdpUrl(productName.toLowerCase().replace(' ', '-'));
        Assertion.assertThat("Product is selected", ttdProductDetailPageWeb.getProductName(), is(productName));

        step("Verify that the partner name is present");
        Assertion.assertThat("Partner name is present", ttdProductDetailPageWeb.isPartnerNamePresent(), is(true));

        step("Check the reviews section");
        Assertion.assertThat("Reviews are present", ttdProductDetailPageWeb.isReviewSectionPresent(), is(true));
    }

    @Author(names = {"Aditi, Yesnila"})
    @Module(name = "PDP")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void checkPartnerInPDP() throws Exception {
        Map<String, String> param = getParam("checkPartnerInPDP");
        BaseTest.paramsThreadLocal.set(param);

        String productName = param.get("productName");

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        TtdLandingPageWeb<?> ttdLandingPageWeb = TtdLandingPageWeb.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);

        step("Open home page");
        homePage.goToHomePage();

        step("Select IDR as currency");
        homePage.selectCurrency("IDR");

        step("Go to TTD Landing Page");
        homePage.openVerticalPageViaURL("events");

        step("Search and select product item");
        ttdLandingPageWeb.inputSearchKeyword(productName);
        ttdLandingPageWeb.selectRecommendedActivity(productName);

        step("Verify that the partner name is not present");
        Assertion.assertThat("Partner name is not present", ttdProductDetailPageWeb.isPartnerNamePresent(), is(false));
    }

    @Author(names = {"Aditi", "Yesnila"})
    @Module(name = "HomePage")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void checkFloatingButtonTTD() throws Exception {
        Map<String, String> param = getParam("checkFloatingButtonTTD");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        TtdLandingPageWeb<?> ttdLandingPageWeb = TtdLandingPageWeb.getInstance(session);
        TtdSearchResultPageWeb<?> ttdSearchResultPageWeb = TtdSearchResultPageWeb.getInstance(session);

        step("Open home page");
        homePage.goToHomePage();

        step("Select IDR as currency");
        homePage.selectCurrency("IDR");

        step("Go to TTD Landing Page");
        homePage.openVerticalPageViaURL("events");

        step("Swipe down and click the See All Activities button");
        ttdLandingPageWeb.clickSeeAllActivities();

        step("Verify that the green dot is not present on the filter icon");
        Assertion.assertThat("Green dot icon is not displayed", ttdSearchResultPageWeb.isGreenDotVisible(), is(false));

        step("Verify that the All locations text is displayed");
        Assertion.assertThat("All locations badge is present", ttdLandingPageWeb.checkAllLocationsBadge(), is(true));

        step("Check the parameters of the current url");
        String currentURL = ttdLandingPageWeb.getSeeAllHomeParameterURL();
        Assertion.assertThat("URL should contains some of parameters", currentURL, allOf(containsString("searchEntry=SEE_ALL_HOME_FAB"), containsString("utm_page=toDoHome"), containsString("?")));

        Assertion.assertThat("URL should contain exactly 2 parameters",
                currentURL.split("&").length, is(2));
    }

    @Author(names = {"Yazid", "Yesnila"})
    @Module(name = "BookingSupplier")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void bundlingSupplierBmg() throws FailedLoginException, InterruptedException, ExecutionException {
        Map<String, String> param = getParam("bundlingSupplierBmg");
        BaseTest.paramsThreadLocal.set(param);
        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String productName = param.get("productName");
        String supplierName = param.get("supplierName");
        int ticketQty = Integer.parseInt(param.get("ticketQty"));
        int bundleQty = Integer.parseInt(param.get("bundleQty"));
        boolean visitDateOdd = Boolean.parseBoolean(param.get("visitDateOdd"));
        String paymentGroup = param.get("paymentGroup");
        String paymentMethod = param.get("paymentMethod");

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        TtdLandingPageWeb<?> ttdLandingPageWeb = TtdLandingPageWeb.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);
        TtdBookingFormPageWeb<?> ttdBookingFormPageWeb = TtdBookingFormPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);

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

        step("Choose visit date with odd number: " + visitDateOdd);
        ttdProductDetailPageWeb.chooseVisitDate(visitDateOdd);

        step("Submit the total ticket");
        ttdProductDetailPageWeb.setTicketQty(ticketQty, 0);
        ttdProductDetailPageWeb.clickBookButton();

        step("Add bundle product");
        ttdBookingFormPageWeb.addBundleProduct(bundleQty, 0);
        Assertion.assertThat("Bundle product is added", ttdBookingFormPageWeb.isBundleProductAdded(), is(true));

        step("Completing booking form data");
        ttdBookingFormPageWeb.fillGuestDetailSupplier(supplierName);

        step("Continue to the payment page");
        ttdBookingFormPageWeb.continueToPaymentPage();

        step("check order id on payment page");
        paymentPage.getOrderId();

        step("User select payment method ");
        paymentPage.clickOnSelectPaymentMethod();
        paymentPage.clickOnSeeAll();
        paymentPage.goToPaymentMethodsGroup(paymentGroup);
        paymentPage.selectPaymentMethod(paymentMethod);

        step("User complete payment");
        paymentPage.clickOnPaymentButton();

        step("Verify the payment successful page is visible or not");
        Assertion.assertThat("Verify the payment successful page is visible", paymentPage.isPaymentSuccessfulVisible(), Matchers.is(true));
    }

    @Author(names = {"Manan Kumar", "Rizki Maulana"})
    @Module(name = "PDP")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void testOrderIssuedFromSupplierRecentlyViewedInTTDHome() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testOrderIssuedFromSupplierRecentlyViewedInTTDHome");
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

        step("Again Go to TTD Landing Page");
        ttdLandingPageWeb.goToTtdLandingPage();

        step("User Go to Recently Viewed section ");
        ttdLandingPageWeb.navigateRecentlyViewedSection();

        step("User Clicked on Recent Item that searched.");
        ttdLandingPageWeb.clickRecentItem();

        step("Verify that page is opened.");
        Assertion.assertThat("same page is opened",ttdLandingPageWeb.isRecentItemOpened(product), is(true));
    }

    @Author(names = {"Kartik", "Annisa"})
    @Module(name = "Booking")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void checkUserCanSelectPackageGroupFromPDP() throws Exception {
        Map<String, String> param = getParam("checkUserCanSelectPackageGroupFromPDP");
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

        step("Verify that the selected package group is displayed");
        ttdProductDetailPageWeb.validateGroupNavigation();
    }

    @Author(names = {"Aditi", "Annisa"})
    @Module(name = "SearchPage")
    @Tribe(name = "Search")
    @Vertical(name = "TTD")
    @Test
    public void checkShareLinkProduct() throws FailedLoginException, InterruptedException{
        Map<String, String> param = getParam("checkShareLinkProduct");
        BaseTest.paramsThreadLocal.set(param);
        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = 0;
        try {
            String retryCounterStr = param.get("retryCounter");
            if (retryCounterStr != null && !retryCounterStr.trim().isEmpty()) {
                retryCounter = Integer.parseInt(retryCounterStr.trim());
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid value for retryCounter: " + param.get("retryCounter"), e);
        }
        String productName = param.get("productName");

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

        step("Check if correct product opened");
        Assertion.assertThat("Selected product is opened", ttdProductDetailPageWeb.isCorrectProductOpened(productName), is(true));

        if (Configs.PLATFORM == Platform.DWEB){
            step("Click on the share link");
            String copiedURL = ttdProductDetailPageWeb.getCurrentURL();

            step("Open a different tab and paste the link in the new browser");
            ttdProductDetailPageWeb.pasteLinkInNewTab(copiedURL);

            step("Check if correct product opened");
            Assertion.assertThat("Selected product is opened", ttdProductDetailPageWeb.isCorrectProductOpened(productName), is(true));
        } else if (Configs.PLATFORM == Platform.IMWEB || Configs.PLATFORM == Platform.AMWEB){
            step("Click share button");
            ttdProductDetailPageMweb.clickShareButton();
        }
    }

    @Author(names = {"Alden"})
    @Module(name = "Refund")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void checkInventoryRefund100pNonLogin() {
        Map<String, String> param = getParam("checkInventoryRefund100pNonLogin");
        BaseTest.paramsThreadLocal.set(param);
        String popularDestination = param.get("popularDestination");

        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        TtdLandingPageWeb<?> ttdLandingPageWeb = TtdLandingPageWeb.getInstance(session);
        TtdSearchResultPageWeb<?> ttdSearchResultPageWeb = TtdSearchResultPageWeb.getInstance(session);

        step("Open home page");
        homePage.goToHomePage();

        step("Go to TTD Landing Page");
        ttdLandingPageWeb.goToTtdLandingPage();

        step("Choose location from popular destinations");
        ttdLandingPageWeb.openSearchInput();
        ttdLandingPageWeb.selectPopularDestinations(popularDestination);

        step("Redirect to the SRP with destination: " + popularDestination);
        Assertion.assertThat("SRP have product with 100% refund benefit", ttdSearchResultPageWeb.isRefund100PVisible(), is(true));
    }

    @Author(names = {"Prerna","Annisa"})
    @Module(name = "PDP")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void checkPackageBundlingRecommendation() throws Exception {
        Map<String, String> param = getParam("checkPackageBundlingRecommendation");
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

        step("Verify bundles recommendation on package detail");
        Assertion.assertThat("Package detail contains bundle recommendation section", ttdProductDetailPageWeb.verifyBundleRecommendation(), is(true));

        step("Verify Bundle Title");
        Assertion.assertThat("Bundle Title appears", ttdProductDetailPageWeb.verifyBundleTitle(), is(true));
    }

    @Author(names = {"Aditi"})
    @Module(name = "Booking")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void checkLPGLabelOnProductByDeeplink() throws Exception {
        Map<String, String> param = getParam("checkLPGLabelOnProductByDeeplink");
        BaseTest.paramsThreadLocal.set(param);

        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String productName = param.get("productName");
        String packageName = param.get("packageName");
        int quantity = Integer.parseInt(param.get("quantity"));
        int ticketIndex = Integer.parseInt(param.get("ticketIndex"));
        String paymentGroup = param.get("paymentGroup");
        String paymentMethod = param.get("paymentMethod");

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        TtdLandingPageWeb<?> ttdLandingPageWeb = TtdLandingPageWeb.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);
        TtdBookingFormPageWeb<?> ttdBookingFormPageWeb = TtdBookingFormPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);
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

        step("Verify that the LPG label is present on the PDP");
        Assertion.assertThat("Is LPG label present on the PDP", ttdProductDetailPageWeb.isLPGLabelPresentOnPDP(), is(true));

        step("Select the package");
        ttdProductDetailPageWeb.clickSeePackagesButton();
        ttdProductDetailPageWeb.chooseTicket(packageName);
        ttdProductDetailPageWeb.setTimeslot();
        ttdProductDetailPageWeb.setTicketQty(quantity, ticketIndex);
        ttdProductDetailPageWeb.clickBookButton();
        ttdBookingFormPageWeb.fillGuestDetail();

        step("Verify that the LPG label is present on the booking page");
        Assertion.assertThat("Is LPG label present on the booking page", ttdBookingFormPageWeb.isLPGLabelPresentOnBookingPage(), is(true));

        step("Continue to the payment page");
        ttdBookingFormPageWeb.continueToPaymentPage();

        Assertion.assertThat("Success redirected to payment page", paymentPage.isPaymentPageLoaded(), is(true));
        String orderId = paymentPage.getOrderId();

        step("Verify that the LPG label is present on the payment page");
        Assertion.assertThat("Is LPG label present on the payment page", paymentPage.isLPGLabelPresentOnPaymentPage(), is(true));

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
        Assertion.assertThat("e-Ticket on the order detail should be published and user has successfully booked LPG product", orderDetailPage.verifyIssuedETicket(), is(true));
    }

    @Author(names = {"Aditi","Annisa"})
    @Module(name = "Booking")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void checkLabelsOnPartnerPageLocationOn() throws Exception {
        Map<String, String> param = getParam("checkLabelsOnPartnerPageLocationOn");
        BaseTest.paramsThreadLocal.set(param);

        String productName = param.get("productName");
        String label1 = param.get("label1");
        String label2 = param.get("label2");

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

        step("Check that the Instant Pass/ tiket Flexi labels are present");
        Assertion.assertThat(label1 + "label is present", ttdProductDetailPageWeb.isLabelPresent(label1), is(true));
        Assertion.assertThat(label2 + "label is present", ttdProductDetailPageWeb.isLabelPresent(label2), is(true));
    }

    @Author(names = {"Rizki Maulana"})
    @Module(name = "Booking")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void checkPlatformFeeSortedByHighestLowest() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("checkPlatformFeeSortedByHighestLowest");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        TtdSearchResultPageWeb<?> ttdSearchResultPageWeb = TtdSearchResultPageWeb.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);
        TtdBookingFormPageWeb<?> ttdBookingFormPageWeb = TtdBookingFormPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);

        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String filter = param.get("filter");
        String ticket = param.get("ticket");
        String paymentGroup = param.get("paymentGroup");
        String paymentMethod = param.get("paymentMethod");

        step("Go to Home Page");
        homePage.goToHomePage();

        step("Open as logged in user");
        loginPage.loginIfNotWithSameEmail(username, password, otp, retryCounter);

        step("Go to TTD Search Result Page");
        ttdSearchResultPageWeb.openSrpUrl("EVENT");
        ttdSearchResultPageWeb.closeContinueOnWebPopup();

        step("Filter by Highest or Lowest");
        ttdSearchResultPageWeb.chooseFilter(filter, FilterType.FEATURES);
        ttdSearchResultPageWeb.clickTodayFilterButton();
        ttdSearchResultPageWeb.closeContinueOnWebPopup();

        step("Click the First Product");
        ttdSearchResultPageWeb.openFirstProduct();
        ttdSearchResultPageWeb.switchToNewTab();

        step("Book Product");
        ttdProductDetailPageWeb.clickBuyTicketButton();
        ttdProductDetailPageWeb.chooseTicket(ticket);
        ttdProductDetailPageWeb.setTicketQty(1, 0);
        ttdProductDetailPageWeb.clickBookButton();
        ttdBookingFormPageWeb.clickSameAsContactToggle();
        ttdBookingFormPageWeb.continueToPaymentPage();

        step("Submit Payment Method");
        paymentPage.clickTotalPriceChevron();
        Assertion.assertThat("Verify platform fee is shown in Payment Page", paymentPage.validateExistPlatformFee(), is(true));

        String orderId = paymentPage.getOrderId();
        paymentPage.clickOnSelectPaymentMethod();
        paymentPage.goToPaymentMethodsGroup(paymentGroup);
        paymentPage.selectPaymentMethod(paymentMethod);
        paymentPage.clickOnPaymentButton();
        Assertion.assertThat("Payment successful page is visible", paymentPage.isPaymentSuccessfulVisible(), is(true));

        step("Check issued on order detail page");
        orderListPage.openActiveTicketViaURL(orderId);
        orderDetailPage.skipCoachmark();
        Assertion.assertThat("Order successfully issued", orderDetailPage.verifyIssuedETicket(), is(true));

        step("Check platform fee on Order Detail Page");
        orderDetailPage.clickShowPriceDetailButton();
        Assertion.assertThat("Verify platform fee is there on Order Detail Page", orderDetailPage.isConvenienceFeeExist(), is(true));
    }

    @Author(names = {"Zia", "Rizki Maulana"})
    @Module(name = "Booking")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void checkProductSpecialPrice() throws Exception {
        Map<String, String> param = getParam("checkProductSpecialPrice");
        BaseTest.paramsThreadLocal.set(param);

        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");

        String productName = param.get("productName");
        String packageName = param.get("packageName");

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

        step("Open home page");
        homePage.goToHomePage();

        step("Open as logged in user");
        loginPage.loginIfNotWithSameEmail(username, password, otp, 1);

        step("Select IDR as currency");
        homePage.selectCurrency("IDR");

        step("Go to TTD Landing Page");
        String lang = homePage.getLanguage();
        ttdLandingPageWeb.goToTtdLandingPage();

        step("Search " + productName);
        ttdLandingPageWeb.inputSearchKeyword(productName);
        ttdLandingPageWeb.search();
        ttdLandingPageWeb.closeContinueOnWebPopup();

        step("Verify product in search result");
        List<String> productNames = ttdSearchResultPageWeb.getProductTitles();
        Assertion.assertThat("Product " + productName + " is in search result", productNames, hasItem(productName));

        step("Open product");
        ttdSearchResultPageWeb.chooseProductSrp(false, productName, 1);
        ttdSearchResultPageWeb.switchToNewTab();

        step("Open package with special price " + packageName);
        ttdProductDetailPageWeb.chooseTicket(packageName);

        step("Open package calendar");
        ttdProductDetailPageWeb.clickCheckAvailableDates();
        int date = ttdProductDetailPageWeb.getNearestSpecialPriceDate();
        String specialPrice = ttdProductDetailPageWeb.getSpecialPriceForDate(date);
        ttdProductDetailPageWeb.clickSpecialPriceForDate(date);
        Assertion.assertThat("Special price is displayed", specialPrice, is(not(emptyOrNullString())));

        step("Continue to booking form");
        ttdProductDetailPageWeb.setTicketQty(1, 0);
        ttdProductDetailPageWeb.clickBookButton();

        step("Completing booking form data");
        ttdBookingFormPageWeb.fillContactOrderDetail(true);
        ttdBookingFormPageWeb.fillGuestDetailWithPhoneNumber();

        step("Verify price in calendar is same as in booking form");
        String priceInBookingForm = ttdBookingFormPageWeb.getFinalPrice();
        Assertion.assertThat("Price in booking form is same as in calendar", CoreFunctions.convertPriceSafely(priceInBookingForm), is(CoreFunctions.convertToThousand(specialPrice)));

        step("Continue to payment page");
        ttdBookingFormPageWeb.continueToPaymentPage();

        Assertion.assertThat("Success redirected to payment page", paymentPage.isPaymentPageLoaded(), is(true));
        String orderId = paymentPage.getOrderId();

        step("Open all payment method");
        paymentPage.clickOnSelectPaymentMethod();

        step("Select the payment Method");
        String paymentGroup = lang.equals("id") ? "Pembayaran Instan" : "Instant Payment";
        paymentPage.goToPaymentMethodsGroup(paymentGroup);
        paymentPage.selectPaymentMethod("Sakuku");

        step("Submit the payment method");
        paymentPage.clickOnPaymentButton();
        Assertion.assertThat("Payment successful page is visible", paymentPage.isPaymentSuccessfulVisible(), is(true));

        step("Check issued on order detail page");
        orderListPage.openActiveTicketViaURL(orderId);
        orderDetailPage.skipCoachmark();
        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.verifyIssuedETicket(orderId), is(true));
    }

    @Author(names = {"Rizki Maulana"})
    @Module(name = "Booking")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void checkOrderWithValidityExpiredDate() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("checkOrderWithValidityExpiredDate");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        TtdLandingPageWeb<?> ttdLandingPageWeb = TtdLandingPageWeb.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);
        TtdPackageDetailPageWeb<?> ttdPackageDetailPageWeb = TtdPackageDetailPageWeb.getInstance(session);
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

        step("Submit booking");
        String todayDateId = LocalDate.now().format(DateTimeFormatter.ofPattern("d MMM yyyy", new Locale("id", "ID")));
        String todayDateEn = LocalDate.now().format(DateTimeFormatter.ofPattern("d MMM yyyy", new Locale("en", "EN")));

        String validityPeriodText = ttdProductDetailPageWeb.getValidityPeriodText();
        String validityStartDate = validityPeriodText.split(":")[1].split(" - ")[0];
        String validityEndDate = validityPeriodText.split(":")[1].split(" - ")[1];
        Assertion.assertThat("Verify validity start date is today", validityStartDate, anyOf(containsStringIgnoringCase(todayDateEn), containsStringIgnoringCase(todayDateId)));
        Assertion.assertThat("Verify validity end date is expired date", validityEndDate, containsStringIgnoringCase(validityExpiredDate));

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


    @Author(names = {"Rizki Maulana", "Annisa"})
    @Module(name = "Home Page")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void homePageLocationOff() throws InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("homePageLocationOff");
        BaseTest.paramsThreadLocal.set(map);

        TtdLandingPageWeb<WebDriver> ttdLandingPageWeb = TtdLandingPageWeb.getInstance(session);
        TtdSearchResultPageWeb<WebDriver> ttdSearchResultPageWeb = TtdSearchResultPageWeb.getInstance(session);

        step("Open TTD Landing Page");
        ttdLandingPageWeb.goToTtdLandingPage();

        step("Check location and turn on if it's off");
        if (ttdLandingPageWeb.isUserLocationOff()) {
            ttdLandingPageWeb.clickSeeAllActivities();
        }

        step("Assertions");
        Assertion.assertThat("Verify Location is not all location (user's location)", ttdSearchResultPageWeb.getSearchLocation(), is(not("All location")));
        Assertion.assertThat("Verify product is shown", ttdSearchResultPageWeb.checkCountResultExist(), is(true));
    }

    @Author(names = {"Ram, Yazid"})
    @Module(name = "SearchPage")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void checkGroupIcon() {
        Map<String, String> param = getParam("checkGroupIcon");
        BaseTest.paramsThreadLocal.set(param);
        String language = param.get("language");
        String separator = "-";
        String groupIconList = param.get("groupIconList");
        List<String> groupIcon = splitText(groupIconList, separator);
        String categoryPromoList = param.get("categoryPromoList");
        List<String> categoryPromo = splitText(categoryPromoList, separator);
        String categoryWithoutPromoList = param.get("categoryWithoutPromoList");
        List<String> categoryWithoutPromo = splitText(categoryWithoutPromoList, separator);
        String sectionTitleList = param.get("sectionTitleList");
        List<String> sectionTitle = (sectionTitleList != null) ? splitText(sectionTitleList, separator) : new ArrayList<>();
        if (sectionTitle.isEmpty()) {
            for (int i = 0; i < groupIcon.size(); i++) {
                sectionTitle.add("");
            }
        }

        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        TtdLandingPageWeb<?> ttdLandingPageWeb = TtdLandingPageWeb.getInstance(session);

        step("User on the home page as guest session");
        homePage.goToHomePage();
        loginPage.loginAsGuest();

        step("Select language: "+language);
        homePage.clickLanguageSelector();
        if (language.equalsIgnoreCase("english")) {
            homePage.selectEnglishLanguage();
        } else {
            homePage.selectBahasaLanguage();
        }

        step("Go to TTD Landing Page");
        ttdLandingPageWeb.goToTtdLandingPage();

        step("Check the group icon");
        int totalGroupIcon = ttdLandingPageWeb.getGroupIconText().size();
        Assertion.assertThat("Expected group icon count should match the number of chips", totalGroupIcon, is(groupIcon.size()));

        for (int i = 0; i < totalGroupIcon; i++) {
            Assertion.assertThat("Group icon should be visible as correct name", ttdLandingPageWeb.getGroupIconText().get(i), equalToIgnoringCase(groupIcon.get(i)));

            step("user is on "+groupIcon.get(i)+" group icon");
            ttdLandingPageWeb.clickGroupIcon(groupIcon.get(i));

            step("Open all "+groupIcon.get(i)+" group icon");
            String allIconName = ttdLandingPageWeb.openAllItemIcon();
            Assertion.assertThat("Opening the correct popup", ttdLandingPageWeb.checkOpeningPopup(allIconName, sectionTitle.get(i)), is(true));

            step("Choose category from the "+groupIcon.get(i)+" group");
            String selectedCategory = ttdLandingPageWeb.chooseCategoryIcon(categoryPromo.get(i), categoryWithoutPromo.get(i));
            Assertion.assertThat("After select category item, user should be redirected to the correct page", ttdLandingPageWeb.redirectionCategoryIcon(), hasItem(containsStringIgnoringCase(selectedCategory)));

            if (i < totalGroupIcon - 1) {
                step("Back to TTD Landing Page");
                ttdLandingPageWeb.goToTtdLandingPage();
            }
        }
    }

    @Author(names = {"Ram, Aditi"})
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
    public void verifyBookingQuantityRestrictionWithSrpPriceMatch() throws Exception {
        Map<String, String> param = getParam("verifyBookingQuantityRestrictionWithSrpPriceMatch");
        BaseTest.paramsThreadLocal.set(param);

        String productName = param.get("productName");
        String productSlug = param.get("productSlug");
        String packageName = param.get("packageName");
        int wrongQuantity = Integer.parseInt(param.get("wrongQuantity"));
        int rightQuantity = Integer.parseInt(param.get("rightQuantity"));
        int ticketIndex = Integer.parseInt(param.get("ticketIndex"));
        String expectedErrorMessage = param.get("expectedErrorMessage");

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        TtdLandingPageWeb<?> ttdLandingPageWeb = TtdLandingPageWeb.getInstance(session);
        TtdSearchResultPageWeb<?> ttdSearchResultPageWeb = TtdSearchResultPageWeb.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);
        TtdBookingFormPageWeb<?> ttdBookingFormPageWeb = TtdBookingFormPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        Unm<?> unm = new Unm<>(session);

        step("Register new account");
        RegisterNewAccountData accountData = unm.registerNewAccountViaApiUNM();
        String email = accountData.email;
        String password = accountData.password;

        try {
            step("Open home page");
            homePage.goToHomePage();

            step("Open as logged in user");
            loginPage.loginWithSpecificAccountViaCookie(email, password);

            step("Select IDR as currency");
            homePage.selectCurrency("IDR");

            step("Go to TTD Landing Page and search product on SRP");
            ttdLandingPageWeb.goToTtdLandingPage();
            ttdLandingPageWeb.inputSearchKeyword(productName);
            ttdLandingPageWeb.search();
            ttdLandingPageWeb.closeContinueOnWebPopup();

            step("Get minimum price from SRP");
            String srpMinPrice = ttdSearchResultPageWeb.getMinimumPriceByProductSlug(productSlug);

            step("Go to PDP");
            ttdProductDetailPageWeb.openPdpUrl(productSlug);
            Assertion.assertThat("Is correct product opened", ttdProductDetailPageWeb.isCorrectProductOpened(productName), is(true));

            step("Select the package");
            ttdProductDetailPageWeb.chooseTicket(packageName);
            ttdProductDetailPageWeb.setTicketQty(wrongQuantity, ticketIndex);

            step("Capture the date selected on PDP");
            String selectedPackageDate = ttdProductDetailPageWeb.getValidityPeriodText();
            Assertion.assertThat("Selected package date is displayed on PDP", selectedPackageDate, is(not(emptyOrNullString())));

            ttdProductDetailPageWeb.clickBookButton();

            step("Fill contact details and visitor details");
            ttdBookingFormPageWeb.fillContactOrderDetail(true);
            ttdBookingFormPageWeb.fillGuestDetailWithPhoneNumber();

            step("Verify minimum price in SRP is similar with price displayed in Booking page");
            int srpUnitPrice = TtdBookingFormPageWeb.convertPriceToInt(srpMinPrice);
            int bookingUnitPrice = TtdBookingFormPageWeb.convertPriceToInt(ttdBookingFormPageWeb.getPrice()) / wrongQuantity;
            Assertion.assertThat("Minimum price in SRP is similar with price displayed in Booking page", bookingUnitPrice, is(srpUnitPrice));

            step("Continue to payment page");
            ttdBookingFormPageWeb.continueToPaymentPage();

            step("Verify booking quantity restriction error message is displayed");
            Assertion.assertThat("Is error message displayed regarding the booking quantity?", ttdBookingFormPageWeb.isQuantityErrorMessageDisplayed(), is(true));
            Assertion.assertThat("Booking quantity restriction error title is correct",
                    ttdBookingFormPageWeb.getQuantityErrorMessage(),
                    anyOf(
                            containsString(expectedErrorMessage),
                            containsString("Sisa kuota tiketmu tinggal"),
                            containsString("only have 5 tickets left"),
                            containsString("Only 5 tickets left")));
            Assertion.assertThat("Booking quantity restriction error description is correct",
                    ttdBookingFormPageWeb.getQuantityErrorDescription(),
                    anyOf(
                            containsString("total 5 tickets"),
                            containsString("total 5 tiket"),
                            containsString("Kurangi jumlah tiket"),
                            containsString("Reduce the number of tickets")));

            step("Click CTA to go back to PDP and update ticket quantity");
            ttdBookingFormPageWeb.clickChangeQuantityButton();
            Assertion.assertThat("User is redirected to PDP after clicking change quantity CTA", ttdProductDetailPageWeb.isCorrectProductOpened(productName), is(true));

            ttdProductDetailPageWeb.setTicketQty(rightQuantity, ticketIndex, packageName);

            step("Check the date selected on PDP is same as the one selected initially");
            String selectedPackageDateAfterBack = ttdProductDetailPageWeb.getValidityPeriodText();
            Assertion.assertThat("Date selected on PDP after going back should be same as the one selected initially selectedPackageDateAfterBack: " + selectedPackageDateAfterBack + ", selectedPackageDate: " + selectedPackageDate,
                    selectedPackageDateAfterBack, equalToIgnoringCase(selectedPackageDate));

            ttdProductDetailPageWeb.clickBookButton();

            step("Verify the valid date on booking form is same as the package date selected on PDP ");
            String bookingFormValidDate = ttdBookingFormPageWeb.getValidDateText();
            Assertion.assertThat("Valid date on booking form should be same as the package date selected on PDP bookingFormValidDate: " + bookingFormValidDate + ", selectedPackageDate: " + selectedPackageDate,
                    ttdBookingFormPageWeb.compareValidityText(bookingFormValidDate, selectedPackageDate), is(true));

            step("Fill contact details and visitor details again");
            ttdBookingFormPageWeb.fillContactOrderDetail(true);
            ttdBookingFormPageWeb.fillGuestDetailWithPhoneNumber();
            ttdBookingFormPageWeb.continueToPaymentPage();

            Assertion.assertThat("Success redirected to payment page", paymentPage.isPaymentPageLoaded(), is(true));
            String orderId = paymentPage.getOrderId();
            Assertion.assertThat("Order ID is present", orderId, is(not(emptyOrNullString())));
        } finally {
            step("log out");
            homePage.goToHomePage();
            loginPage.loginAsGuest();

            step("delete account");
            unm.deleteAccountViaApi(accountData.unmData);
        }

    }

    @Author(names = {"Yazid", "Rizki Maulana"})
    @Module(name = "Booking")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void checkAttractionTopRating() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("checkAttractionTopRating");
        BaseTest.paramsThreadLocal.set(param);
        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        int ticketQty = Integer.parseInt(param.get("ticketQty"));
        String packageName = param.get("packageName");
        String paymentGroup = param.get("paymentGroup");
        String paymentMethod = param.get("paymentMethod");

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

        step("Open home page");
        homePage.goToHomePage();

        step("Open as logged in user");
        loginPage.loginIfNotWithSameEmail(username, password, otp, retryCounter);

        step("Select IDR as currency");
        homePage.selectCurrency("IDR");

        step("Go to TTD Landing Page");
        ttdLandingPageWeb.goToTtdLandingPage();

        step("Check rank of the popular attractions");
        ttdLandingPageWeb.openSearchInput();
        Assertion.assertThat("popular attractions section on search suggestion page with 10 attractions having count 1-10", ttdLandingPageWeb.checkPopularAttractionsRank(10), is(true));

        step("Click see all popular attraction");
        ttdLandingPageWeb.clickSeeAllPopularAttractions();
        ttdLandingPageWeb.closeContinueOnWebPopup();
        Assertion.assertThat("User will be redirected to the SRP page", ttdSearchResultPageWeb.getProductTitle(), is(not("")));
        Assertion.assertThat("Attractions should be a selected category on the SRP", ttdSearchResultPageWeb.getSelectedFilter(), either(equalToIgnoringCase("all attractions")).or(equalToIgnoringCase("semua atraksi")));

        step("Back to the TTD Landing Page");
        ttdLandingPageWeb.goToTtdLandingPage();
        ttdLandingPageWeb.closeContinueOnWebPopup();

        step("Choose 1st rank of the popular attraction");
        ttdLandingPageWeb.openSearchInput();
        ttdLandingPageWeb.selectPopularAttractions(1);

        step("Select package randomly");
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
        Assertion.assertThat("'Redeem your e-ticket' and 'See redemption status' button is shown and clickable", orderDetailPage.validateRedeemAndSeeRedemptionStatusButtonExistAndClickable(), is(true));
    }

    @Author(names = {"Aditi", "Manan Kumar"})
    @Module(name = "PDP")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void checkPackageConfigurationTTD() throws InterruptedException {
        Map<String, String> map = getParam("checkPackageConfigurationTTD");
        BaseTest.paramsThreadLocal.set(map);

        String product = map.get("product");

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        TtdLandingPageWeb<?> ttdLandingPageWeb = TtdLandingPageWeb.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);

        step("Open home page");
        homePage.goToHomePage();

        step("Open as logged in user");
        loginPage.loginAsGuest();

        step("Select IDR as currency");
        homePage.selectCurrency("IDR");

        step("Go to TTD Landing Page");
        ttdLandingPageWeb.goToTtdLandingPage();

        step("User navigates to the master product");
        ttdLandingPageWeb.openProductViaURL(product);

        step("Is the page displayed");
        Assertion.assertThat("Selected page is opened", ttdProductDetailPageWeb.isCorrectProductOpened(product), is(true));

        if (Configs.PLATFORM.equals(Platform.DWEB)) {
            step("Navigate to the Package section");
            Assertion.assertThat("All packages groups are present on separate container", ttdProductDetailPageWeb.verifyPackageSection(), is(true));
        } else {
            String option1 = map.get("option1");
            String option2 = map.get("option2");
            String option3 = map.get("option3");

            step("Verify product page is opened for mWeb");
            Assertion.assertThat("Product page is opened", ttdLandingPageWeb.isPartnerOpened(), is(true));

            step("User clicks and opens first package option: " + option1);
            ttdLandingPageWeb.openPackageOptions(option1);

            step("Verify " + option1 + " packages are displayed");
            Assertion.assertThat(option1 + " group is displayed", ttdLandingPageWeb.isPackageGroupDisplayed(option1), is(true));
            Assertion.assertThat("Packages are displayed for " + option1, ttdLandingPageWeb.arePackagesDisplayed(), is(true));

            step("User clicks and opens second package option: " + option2);
            ttdLandingPageWeb.openPackageOptions(option2);

            step("Verify " + option2 + " packages are displayed");
            Assertion.assertThat(option2 + " group is displayed", ttdLandingPageWeb.isPackageGroupDisplayed(option2), is(true));
            Assertion.assertThat("Packages are displayed for " + option2, ttdLandingPageWeb.arePackagesDisplayed(), is(true));

            step("User clicks and opens third package option: " + option3);
            ttdLandingPageWeb.openPackageOptions(option3);

            step("Verify " + option3 + " packages are displayed");
            Assertion.assertThat(option3 + " group is displayed", ttdLandingPageWeb.isPackageGroupDisplayed(option3), is(true));
            Assertion.assertThat("Packages are displayed for " + option3, ttdLandingPageWeb.arePackagesDisplayed(), is(true));
        }
    }

    @Author(names = {"Ram, Aditi"})
    @Module(name = "Booking")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void checkVisitDatePackageWithTimeSlot() throws Exception {
        Map<String, String> param = getParam("checkVisitDatePackageWithTimeSlot");
        BaseTest.paramsThreadLocal.set(param);

        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String productName = param.get("productName");
        String packageName = param.get("packageName");
        int quantity = Integer.parseInt(param.get("quantity"));
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

        step("Login as " + username + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(username, password);

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

        step("Select visit date and time slot");
        ttdProductDetailPageWeb.setTimeslot();

        step("set ticket quantity");
        ttdProductDetailPageWeb.setTicketQty(quantity, ticketIndex);

        step("Submit the booking");
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

    @Author(names = {"Ram,Yazid"})
    @Module(name = "Booking")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void checkSeatMapStandingInsurance() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("checkSeatMapStandingInsurance");
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

        step("Click buy ticket button");
        ttdProductDetailPageWeb.clickBuyTicketButton();

        step("Select package: " + packageName);
        Assertion.assertThat("\"Standing\" info should be appear on the PDP", ttdProductDetailPageWeb.getPackageBenefit(packageName), either(is(containsString("Standing"))).or(is(containsString("Berdiri"))));
        ttdProductDetailPageWeb.chooseTicket(packageName);

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

        step("Submit the payment method");
        paymentPage.clickOnPaymentButton();
        Assertion.assertThat("Payment successful page is visible", paymentPage.isPaymentSuccessfulVisible(), is(true));

        step("Check issued on order detail page");
        orderListPage.openActiveTicketViaURL(orderId);
        orderDetailPage.skipCoachmark();
        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.verifyIssuedETicket(), is(true));
    }

    @Author(names = {"Ram, Yazid"})
    @Module(name = "TTD")
    @Tribe(name = "TTD")
    @Vertical(name = "TTD")
    @Test
    public void TTD_checkSeatMapManualSeating() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("TTD_checkSeatMapManualSeating");
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
        ttdBookingFormPageWeb.fillContactOrderDetail(true);
        ttdBookingFormPageWeb.clickSameAsContactToggle();

        step("Select seat map");
        ttdBookingFormPageWeb.continueToPaymentPage();
        ttdBookingFormPageWeb.selectSeat("random");

        step("Redirected to the payment page");
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

    @Author(names = {"Ram, Aditi"})
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

    @Author(names = {"Ram Pratap Singh"})
    @Module(name = "Booking")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void claimedGVCheck() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("claimedGVCheck");
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
        ttdProductDetailPageWeb.chooseTicket(packageName);

        step("Choose next day of visit date");
        ttdProductDetailPageWeb.chooseVisitDateWithoutTab(0);

        step("Submit the total ticket");
        ttdProductDetailPageWeb.setTicketQty(1, 0);
        ttdProductDetailPageWeb.clickBookButton();

        step("Completing booking form data");
        ttdBookingFormPageWeb.fillContactOrderDetail(true);
        ttdBookingFormPageWeb.fillGuestDetailWithPhoneNumber();

        step("Click 'Claim' button");
        ttdBookingFormPageWeb.clickInfinityPromoApplyButton();

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

    @Author(names = {"Kartik","Manan Kumar"})
    @Module(name = "Booking")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void verifyBundlingForSameProductDifferentPackages() throws FailedLoginException, InterruptedException, IOException {
        Map<String, String> param = getParam("verifyBundlingForSameProductDifferentPackages");
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
        ttdBookingFormPageWeb.fillContactOrderDetail(true);

        step("Choose a different add on package for the same main product from add on section");
        String bundlingProduct = ttdBookingFormPageWeb.addBundlingItems(1,0);

        step("User fill guest details for both the packages.");
        if(Configs.PLATFORM.equals(Platform.AMWEB)){
            ttdBookingFormPageWeb.fillMultipleVisitorDetails();
        } else {
            ttdBookingFormPageWeb.fillGuestDetail();
            ttdBookingFormPageWeb.clickSameAsContactToggle(2);
        }

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

        step("Open e-ticket and verify the product name for both packages");
        orderListPage.waitForPaymentSuccessful(orderId,5);
        ArrayList<String> orderDetails = orderListPage.getOrderDetailIdAndHashId(orderId);
        ArrayList<String> bundlingPackageNames = new ArrayList<>(List.of(packageName, bundlingProduct));
        String orderHashId = orderDetails.get(orderDetails.size()-1);
        ArrayList<String> bundlingProductFromETicketIssued = new ArrayList<>();
        for(int i=0; i<orderDetails.size()-1; i++){
            String orderDetailId = orderDetails.get(i);
            orderListPage.openActiveTicketViaURL(orderId, orderHashId, orderDetailId);
            orderDetailPage.skipCoachmark();
            Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.verifyIssuedETicket(), is(true));


            orderDetailPage.clickEticketFromHeader();
            bundlingProductFromETicketIssued.add(orderDetailPage.verifyTextFromEVoucherDwebAndMweb(packageName, bundlingProduct));
        }
        step("Verify that both packages are issued with correct product name on eVoucher ");
        Assertion.assertThat("Both packages should be issued with correct product name on eVoucher", bundlingProductFromETicketIssued, containsInAnyOrder(bundlingPackageNames.toArray()));
    }

    @Author(names = {"Manan Kumar"})
    @Module(name = "Booking")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void verifySpecialPriceLowestPriceAndCampaignDisplayed() throws FailedLoginException, InterruptedException, IOException {
        Map<String, String> param = getParam("verifySpecialPriceLowestPriceAndCampaignDisplayed");
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

        step("Click on Check available dates");
        ttdProductDetailPageWeb.clickCheckAvailableDates();

        step("Select first special price (green) date and store the price");
        ttdProductDetailPageWeb.clickFirstSpecialPriceDateAndGetPrice();

        step("Verify special price date is selected");
        Assertion.assertThat("Special price date should be selected", ttdProductDetailPageWeb.isSpecialPriceDateSelected(), is(true));

        step("Submit the total ticket");
        ttdProductDetailPageWeb.setTicketQty(1, 0);
        ttdProductDetailPageWeb.clickBookButton();

        step("Completing booking form data");
        ttdBookingFormPageWeb.fillContactOrderDetail(true);

        step("User fill the Visitor details.");
        ttdBookingFormPageWeb.fillMultipleVisitorDetails();

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

        step("User verify that issued order is for package which contains Special Price, Lowest Price and Campaign");
        Assertion.assertThat("is correct order issued ? ",orderDetailPage.isCorrectOrderIssued(packageName), is(true));
    }

    @Author(names = {"Ram Pratap Singh"})
    @Module(name = "Booking")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void checkOrderIssuedWithMaxBookingLimit() throws Exception {
        Map<String, String> param = getParam("checkOrderIssuedWithMaxBookingLimit");
        BaseTest.paramsThreadLocal.set(param);

        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String productName = param.get("productName");
        String packageName = param.get("packageName");
        int maxQuantity = Integer.parseInt(param.get("maxQuantity"));
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
        ttdProductDetailPageWeb.setTicketQty(maxQuantity, 0);

        step("Verify max quantity message is displayed");
        Assertion.assertThat("Maximum quantity message is shown", ttdProductDetailPageWeb.isMaxQuantityMessageDisplayed(), is(true));

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

    @Author(names = {"Ram Pratap Singh"})
    @Module(name = "BookingSupplier")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void bundlingSupplierJTR() throws FailedLoginException, InterruptedException, ExecutionException {
        Map<String, String> param = getParam("bundlingSupplierJTR");
        BaseTest.paramsThreadLocal.set(param);
        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String supplierName = param.get("supplierName");
        int ticketQty = Integer.parseInt(param.get("ticketQty"));
        boolean visitDateOdd = Boolean.parseBoolean(param.get("visitDateOdd"));
        String paymentGroup = param.get("paymentGroup");
        String paymentMethod = param.get("paymentMethod");
        String product = param.get("product");

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        TtdLandingPageWeb<?> ttdLandingPageWeb = TtdLandingPageWeb.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);
        TtdBookingFormPageWeb<?> ttdBookingFormPageWeb = TtdBookingFormPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);

        step("Open home page");
        homePage.goToHomePage();

        step("Open as logged in user");
        loginPage.loginIfNotWithSameEmail(username, password, otp, retryCounter);

        step("Select IDR as currency");
        homePage.selectCurrency("IDR");

        step("Go to TTD Landing Page");
        ttdLandingPageWeb.goToTtdLandingPage();

        step("User navigates to the master product");
        ttdLandingPageWeb.openProductViaURL(product);

        step("Select package from supplier: " + supplierName);
        ttdProductDetailPageWeb.chooseTicketSupplier(supplierName);

        step("Choose visit date with odd number: " + visitDateOdd);
        ttdProductDetailPageWeb.chooseVisitDate(visitDateOdd);

        step("Submit the total ticket");
        ttdProductDetailPageWeb.setTicketQty(ticketQty, 0);
        ttdProductDetailPageWeb.clickBookButton();

        step("Completing booking form data");
        ttdBookingFormPageWeb.fillGuestDetailSupplier(supplierName);

        step("Continue to the payment page");
        ttdBookingFormPageWeb.continueToPaymentPage();

        step("check order id on payment page");
        paymentPage.getOrderId();

        step("User select payment method ");
        paymentPage.clickOnSelectPaymentMethod();
        paymentPage.clickOnSeeAll();
        paymentPage.goToPaymentMethodsGroup(paymentGroup);
        paymentPage.selectPaymentMethod(paymentMethod);

        step("User complete payment");
        paymentPage.clickOnPaymentButton();

        step("Verify the payment successful page is visible or not");
        Assertion.assertThat("Verify the payment successful page is visible", paymentPage.isPaymentSuccessfulVisible(), Matchers.is(true));
    }

    @Author(names = {"Ram Pratap Singh"})
    @Module(name = "Booking")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void bundlingSupplierGlobalTix() throws FailedLoginException, InterruptedException, ExecutionException, IOException {
        Map<String, String> param = getParam("bundlingSupplierGlobalTix");
        BaseTest.paramsThreadLocal.set(param);
        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String productName = param.get("productName");
        String supplierName = param.get("supplierName");
        int ticketQty = Integer.parseInt(param.get("ticketQty"));
        int bundleQty = Integer.parseInt(param.get("bundleQty"));
        boolean visitDateOdd = Boolean.parseBoolean(param.get("visitDateOdd"));
        String paymentGroup = param.get("paymentGroup");
        String paymentMethod = param.get("paymentMethod");

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        TtdLandingPageWeb<?> ttdLandingPageWeb = TtdLandingPageWeb.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);
        TtdBookingFormPageWeb<?> ttdBookingFormPageWeb = TtdBookingFormPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);
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

        step("User navigates to the master product");
        ttdLandingPageWeb.openProductViaURL(productName);

        step("Select package from supplier: " + supplierName);
        ttdProductDetailPageWeb.getProductName();
        ttdProductDetailPageWeb.chooseTicketSupplier(supplierName);

        step("Choose visit date with odd number: " + visitDateOdd);
        ttdProductDetailPageWeb.chooseVisitDate(visitDateOdd);

        step("Submit the total ticket");
        ttdProductDetailPageWeb.setTicketQty(ticketQty, 0);
        ttdProductDetailPageWeb.clickBookButton();

        step("Completing booking form data - Fill all mandatory fields");
        ttdBookingFormPageWeb.fillContactOrderDetail(true);

        step("Click on the add on section - Choose a different GlobalTIX product package from add on section");
        String bundlingProduct = ttdBookingFormPageWeb.addBundlingItems(bundleQty, 0);

        step("User fill guest details for GLOBALTIX supplier");
        ttdBookingFormPageWeb.fillGuestDetailSupplier(supplierName);

        step("Continue to the payment page");
        ttdBookingFormPageWeb.continueToPaymentPage();

        step("Check order id on payment page");
        String orderId = paymentPage.getOrderId();

        step("User select payment method");
        paymentPage.clickOnSelectPaymentMethod();
        paymentPage.clickOnSeeAll();
        paymentPage.goToPaymentMethodsGroup(paymentGroup);
        paymentPage.selectPaymentMethod(paymentMethod);

        step("User complete payment");
        paymentPage.clickOnPaymentButton();

        step("Verify the payment successful page is visible");
        Assertion.assertThat("Verify the payment successful page is visible", paymentPage.isPaymentSuccessfulVisible(), is(true));

        step("Payment success and able to go to MyOrder");
        orderListPage.openActiveTicketViaURL(orderId);
        orderDetailPage.skipCoachmark();

        step("Verify e-Ticket is issued on order detail page");
        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.verifyIssuedETicket(), is(true));

        step("Verify bundling product in E-Voucher");
        orderDetailPage.clickEticketFromHeader();
        Assertion.assertThat("Verify bundling text in E-Voucher", orderDetailPage.verifyTextFromEVoucher(bundlingProduct), is(true));
    }

    @Author(names = {"Yazid"})
    @Module(name = "LandingPage")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void checkLandingPagePromoSectionGuestWeb() {
        Map<String, String> map = getParam("checkLandingPagePromoSectionGuestWeb");
        BaseTest.paramsThreadLocal.set(map);

        String currency = map.get("currency");
        String product = map.get("product");

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        TtdLandingPageWeb<?> ttdLandingPageWeb = TtdLandingPageWeb.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);
        TtdPartnerPageWeb<?> ttdPartnerPageWeb = TtdPartnerPageWeb.getInstance(session);

        step("Go to home page");
        homePage.goToHomePage();

        step("Make user as guest");
        loginPage.loginAsGuest();

        step("Select currency");
        homePage.selectCurrency(currency);

        step("Go to TTD Landing Page");
        ttdLandingPageWeb.goToTtdLandingPage();
        Assertion.assertThat("Promo section should be displayed on the TTD landing page", ttdLandingPageWeb.verifyPromoSection(), is(true));

        step("Search the product");
        ttdLandingPageWeb.openSearchInput();
        ttdLandingPageWeb.inputSearchKeyword(product);
        ttdLandingPageWeb.selectRecommendedActivity(product);
        Assertion.assertThat("Promo section should be displayed on the TTD PDP", ttdProductDetailPageWeb.verifyPromoBenefitsSection(), is(true));

        step("Open the TTD Partner Page");
        ttdProductDetailPageWeb.clickProductPartner();
        Assertion.assertThat("Partner page should be displayed", ttdPartnerPageWeb.isPageModuleDisplayed(), is(true));
        step("Verify promo section on partner page");
        Assertion.assertThat("Promo section showing on partner page", ttdPartnerPageWeb.isPromoSectionDisplayed(), is(true));
    }

    @Author(names = {"Yazid"})
    @Module(name = "SearchSuggestion")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void checkPopularAttractionsGuestWeb() throws Exception {
        Map<String, String> map = getParam("checkPopularAttractionsGuestWeb");
        BaseTest.paramsThreadLocal.set(map);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        TtdLandingPageWeb<?> ttdLandingPageWeb = TtdLandingPageWeb.getInstance(session);
        TtdSearchResultPageWeb<?> ttdSearchResultPageWeb = TtdSearchResultPageWeb.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);
        TTDPage<?> ttdPage = new TTDPage<>(session);

        step("Go To Home");
        homePage.goToHomePage();

        step("Make user as guest");
        loginPage.loginAsGuest();

        step("Select IDR as currency");
        homePage.selectCurrency("IDR");

        String language = homePage.getLanguage();

        step("Open TTD Landing Page");
        ttdLandingPageWeb.goToTtdLandingPage();

        step("Open search input");
        ttdLandingPageWeb.openSearchInput();

        step("Check popular attractions section");
        ttdLandingPageWeb.isPopularAttractionPresent();
        Assertion.assertThat("Attractions should be have 1-10 of the rank", ttdLandingPageWeb.checkPopularAttractionsRank(10), is(true));

        if (Configs.ENVIRONMENT == Environment.GK || Configs.ENVIRONMENT == Environment.PREPROD) {
            step("Check product list order: popular attractions");
            List<String> productOrderFromUI = ttdLandingPageWeb.getPopularAttractionProductTitleOrder();

            step("Get product list from API: popular attractions");
            List<String> productOrderFromAPI = ttdPage.getProductTitleOrder("popular attractions", language, "", "");
            Assertion.assertThat("Product title order should be matched with API", productOrderFromUI, is(productOrderFromAPI));
        }

        step("Click see all button");
        ttdLandingPageWeb.clickSeeAllPopularAttractions();
        ttdLandingPageWeb.closeContinueOnWebPopup();
        Assertion.assertThat("See all button is redirecting to SRP with 'attraction' category", ttdSearchResultPageWeb.getSelectedFilter(), either(equalToIgnoringCase("all attractions")).or(equalToIgnoringCase("semua atraksi")));

        step("Back to the TTD Landing Page");
        ttdLandingPageWeb.goToTtdLandingPage();
        ttdLandingPageWeb.closeContinueOnWebPopup();

        step("Select any product");
        ttdLandingPageWeb.openSearchInput();
        String expectedProductName = ttdLandingPageWeb.selectAttractionProduct();
        Assertion.assertThat("User will be redirected to the correct PDP", ttdProductDetailPageWeb.getProductName(), is(expectedProductName));
    }

    @Author(names = {"Aditi"})
    @Module(name = "SearchSuggestion")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void checkTopKeywordsAndSuggestion() {
        Map<String, String> param = getParam("checkTopKeywordsAndSuggestion");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        TtdLandingPageWeb<?> ttdLandingPageWeb = TtdLandingPageWeb.getInstance(session);

        step("Open home page");
        homePage.goToHomePage();

        step("Open To Do section on landing page");
        ttdLandingPageWeb.goToTtdLandingPage();

        step("Click on search suggestion box");
        ttdLandingPageWeb.openSearchInput();

        step("Verify that some keywords are present");
        List<String> topKeywords = ttdLandingPageWeb.getTopKeywordList();
        Assertion.assertThat("Top keywords are present on search suggestion box", topKeywords, is(not(empty())));
        Assertion.assertThat("Top keywords should not be empty", topKeywords, everyItem(not(emptyOrNullString())));
    }
}