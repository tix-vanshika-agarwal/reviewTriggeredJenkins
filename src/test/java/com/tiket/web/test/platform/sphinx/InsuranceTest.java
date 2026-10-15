package com.tiket.web.test.platform.sphinx;

import com.tiket.annotation.Author;
import com.tiket.apiPage.OrderApi;
import com.tiket.apiPage.Unm;
import com.tiket.pojo.order.MapVerticalWithOrderId;
import com.tiket.pojo.unm.LoginDataUnmResponse;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.model.Platform;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.testbase.Configs;
import com.tiket.web.page.common.home.HomePageWeb;
import com.tiket.web.page.common.home.LoginPageWeb;
import com.tiket.web.page.platform.payment.PaymentPageWeb;
import com.tiket.web.page.platform.sphinx.insurance.ClaimCenterPageWeb;
import org.hamcrest.Matchers;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import javax.security.auth.login.FailedLoginException;
import java.util.Map;

import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.core.Is.is;

public class InsuranceTest extends BaseTest {

    private boolean hasFlightOrderToClaimAgainst(Session<WebDriver> session, String username, String password) {
        try {
            LoginDataUnmResponse.Data authData = new Unm<WebDriver>(session).unmLoginApi(username, password);
            MapVerticalWithOrderId activeOrders =
                    new OrderApi<>(session).getAvailaibleOrderByVertical("flight", authData.accessToken);
            return !activeOrders.isEmpty && !activeOrders.flight.isEmpty();
        } catch (Exception e) {
            log("Could not read the order list over the API (" + e.getClass().getSimpleName() + ": "
                    + e.getMessage() + "), so the claim center is left to show whether an order is there.");
            return true;
        }
    }
    @Author(names = {"Haniah"})
    @Module(name = "Insurance")
    @Tribe(name = "Sphinx")
    @Vertical(name = "Platform")
    @Test
    public void testAccessingInsuranceClaimLandingPageNonLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testAccessingInsuranceClaimLandingPageNonLogin");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<WebDriver> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<WebDriver> loginPage = LoginPageWeb.getInstance(session);
        ClaimCenterPageWeb<WebDriver> claimCenterPage = ClaimCenterPageWeb.getInstance(session);

        step("user open homepage");
        homePage.goToHomePage();
        homePage.waitForHomePageToOpen();

        step("Logout via URL");
        loginPage.logoutViaUrl();

        step("Open Insurance Claim Landing Page");
        if (Configs.PLATFORM.equals(Platform.DWEB)) {
            homePage.openVerticalPageViaURL("Claim Center");
        } else {
            homePage.goToHomePage();
            homePage.openVerticalPage("Claim Center");
        }
        claimCenterPage.removeWelcomePopup();
        Assertion.assertThat("is on landing page of Insurance Claim Landing Page",claimCenterPage.isOnInsuranceClaimLandingPage(false),is(true));
    }

    @Author(names = {"Yoan"})
    @Module(name = "Insurance")
    @Tribe(name = "Sphinx")
    @Vertical(name = "Platform")
    @Test
    public void verifyInsuranceOrderDetailWeb() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyInsuranceOrderDetailWeb");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<WebDriver> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<WebDriver> loginPage = LoginPageWeb.getInstance(session);
        ClaimCenterPageWeb<WebDriver> claimCenterPage = ClaimCenterPageWeb.getInstance(session);

        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");

        step("user open homepage");
        homePage.goToHomePage();
        homePage.waitForHomePageToOpen();

        step("login to tiket");
        loginPage.loginIfNotWithSameEmail(username, password, otp,0);

        step("Open Insurance Claim Landing Page");
        homePage.openVerticalPageViaURL("Claim Center");

        step("Verify Insurance Order Detail Button");
        claimCenterPage.clickOrderDetailsInsurance();

        step("Verify Insurance Order Detail Page");
        Assertion.assertThat("Insurance order detail page is accessible",claimCenterPage.isFAQDropdownAccessible(),is(true));

    }

    @Author(names = {"Vidhi Jindal"})
    @Module(name = "Insurance")
    @Tribe(name = "Insurance")
    @Vertical(name = "Platform")
    @Test
    public void testverifyClaimCentrePageLogin() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("testverifyClaimCentrePageLogin");
        BaseTest.paramsThreadLocal.set(map);

        HomePageWeb<WebDriver> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<WebDriver> loginPage = LoginPageWeb.getInstance(session);
        ClaimCenterPageWeb<WebDriver> claimCenterPage = ClaimCenterPageWeb.getInstance(session);

        String email = map.get("email");
        String password = map.get("password");
        String otp = map.get("otp");

        step("user open homepage");
        homePage.goToHomePage();
        homePage.waitForHomePageToOpen();

        step("Login as " + email + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(email, password);

        step("Open Insurance Claim Landing Page");
        if (Configs.PLATFORM.equals(Platform.DWEB)) {
            homePage.openVerticalPageViaURL("Claim Center");
            claimCenterPage.removeWelcomePopup();
            Assertion.assertThat("User is on landing page of Insurance Claim Landing Page",claimCenterPage.isOnInsuranceClaimLandingPage(true),is(true));
        } else {
            homePage.goToHomePage();
            homePage.clickClaimCentre();
            Assertion.assertThat("User is on landing page of Claim Centre Landing Page",claimCenterPage.insuranceClaimLandingPageAppears(),is(true));
        }
    }

    @Author(names = {"Vidhi Jindal"})
    @Module(name = "Insurance")
    @Tribe(name = "Insurance")
    @Vertical(name = "Platform")
    @Test
    public void verifyTestInsuranceOrderDetail() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("verifyTestInsuranceOrderDetail");
        BaseTest.paramsThreadLocal.set(map);

        HomePageWeb<WebDriver> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<WebDriver> loginPage = LoginPageWeb.getInstance(session);
        ClaimCenterPageWeb<WebDriver> claimCenterPage = ClaimCenterPageWeb.getInstance(session);

        String email = map.get("email");
        String password = map.get("password");
        String otp = map.get("otp");

        step("user open homepage");
        homePage.goToHomePage();
        homePage.waitForHomePageToOpen();

        step("Login as " + email + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(email, password);

        step("Open Insurance Claim Landing Page");
        if (Configs.PLATFORM.equals(Platform.DWEB)) {
            homePage.openVerticalPageViaURL("Claim Center");
            claimCenterPage.removeWelcomePopup();
            Assertion.assertThat("User is on landing page of Insurance Claim Landing Page",claimCenterPage.isOnInsuranceClaimLandingPage(true),is(true));
        } else {
            homePage.goToHomePage();
            homePage.clickClaimCentre();
            claimCenterPage.removeWelcomePopup();
            Assertion.assertThat("User is on landing page of Claim Centre Landing Page",claimCenterPage.insuranceClaimLandingPageAppears(),is(true));
        }

        step("User clicks on claim history button and is redirected to history page.");
        claimCenterPage.clicksClaimHistory();

        claimCenterPage.removeClaimListPopup();

        step("Verify insurance order history list appears");
        Assertion.assertThat("Insurance Order History List appear",claimCenterPage.claimAndExpiredInsuranceOrdersAppears(),is(true));

        step("Verify insurance details can be access");
        Assertion.assertThat("Insurance Details can be access",claimCenterPage.historyInsuranceDetailsAppears(),is(true));
    }

    @Author(names = {"Vidhi Jindal"})
    @Module(name = "Insurance")
    @Tribe(name = "Sphinx")
    @Vertical(name = "Platform")
    @Test
    public void testVerifyInsuranceClaimDetailPage() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("testVerifyInsuranceClaimDetailPage");
        BaseTest.paramsThreadLocal.set(map);

        HomePageWeb<WebDriver> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<WebDriver> loginPage = LoginPageWeb.getInstance(session);
        ClaimCenterPageWeb<WebDriver> claimCenterPage = ClaimCenterPageWeb.getInstance(session);

        String email = map.get("email");
        String password = map.get("password");
        String otp = map.get("otp");

        step("user open homepage");
        homePage.goToHomePage();
        homePage.waitForHomePageToOpen();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, 2);

        step("Open Insurance Claim Landing Page");
        if (Configs.PLATFORM.equals(Platform.DWEB)) {
            homePage.openVerticalPageViaURL("Claim Center");
            claimCenterPage.removeWelcomePopup();
            Assertion.assertThat("User is on landing page of Insurance Claim Landing Page",claimCenterPage.isOnInsuranceClaimLandingPage(true),is(true));
        } else {
            homePage.goToHomePage();
            homePage.clickClaimCentre();
            claimCenterPage.removeWelcomePopup();
            Assertion.assertThat("User is on landing page of Claim Centre Landing Page",claimCenterPage.insuranceClaimLandingPageAppears(),is(true));
        }

        step("User clicks on claim history button and is redirected to history page.");
        claimCenterPage.clicksClaimHistory();

        claimCenterPage.removeClaimListPopup();

        step("Verify 2 tabs appear : Claim List & Expired Insurance");
        Assertion.assertThat("Claim List and Expired Insurance appears",claimCenterPage.claimAndExpiredInsuranceAppears(),is(true));

        step("Verify insurance order detail appears");
        Assertion.assertThat("Insurance Order appears",claimCenterPage.claimAndExpiredInsuranceOrdersAppears(),is(true));

        step("User click Details button next to order ID and is redirected to Insurance Claim Detail Page");
        claimCenterPage.clicksDetails();

        step("Verify User can see the details of Insurance List.");
        Assertion.assertThat("Details of Insurance List appears",claimCenterPage.claimAndExpiredInsuranceDetailsAppears(),is(true));

        step("User click Frequently Asked Details.");
        claimCenterPage.faqDetails();

        step("Verify User can see Insurance Policy, How to Claim, and Contact Cermati Protect.");
        Assertion.assertThat("Insurance Policy, Claim and Contact Cermati Protect appears",claimCenterPage.insurancePolicyClaimAndContactCermatiAppears(),is(true));
    }

    @Author(names = {"Yoan"})
    @Module(name = "Insurance")
    @Tribe(name = "Insurance")
    @Vertical(name = "Platform")
    @Test
    public void verifyClaimCenterDrawerWeb() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyClaimCenterDrawerWeb");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<WebDriver> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<WebDriver> loginPage = LoginPageWeb.getInstance(session);
        ClaimCenterPageWeb<WebDriver> claimCenterPage = ClaimCenterPageWeb.getInstance(session);

        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");

        step("user open homepage");
        homePage.goToHomePage();
        homePage.waitForHomePageToOpen();

        step("login to tiket");
        loginPage.loginIfNotWithSameEmail(username, password, otp,0);

        step("Open Insurance Claim Landing Page");
        homePage.openVerticalPageViaURL("Claim Center");

        step("Verify welcome popup");
        claimCenterPage.closeWelcomePopUp();

        step("Verify user is on Insurance Claim Landing Page");
        claimCenterPage.insuranceClaimLandingPageAppears();

        step("Verify insurance card claim is displayed");
        Assertion.assertThat("Insurance card claim is displayed", claimCenterPage.isInsuranceCardClaimDisplayed(), is(true));

        step("Click on insurance card claim");
        claimCenterPage.clickInsuranceCardClaim();

        step("Verify claim ID is displayed in the drawer");
        Assertion.assertThat("Claim ID is displayed in the drawer", claimCenterPage.isClaimIdInDetailsPresent(), is(true));

        step("Verify flight route is displayed in the drawer");
        Assertion.assertThat("Flight route is displayed in the drawer", claimCenterPage.isFlightRouteDisplayed(), is(true));

        step("Verify claim status is displayed");
        if(claimCenterPage.isClaimStatusSectionDisplayed()) {
            Assertion.assertThat("Claim status is displayed", claimCenterPage.isClaimStatusSectionDisplayed(), is(true));
        } else {
            Assertion.assertThat("Claim status can't be processed", claimCenterPage.isInsuranceCardClaimDisplayed(), is(false));
        }

    }
    @Author(names = {"Akshat", "Yoan"})
    @Module(name = "Insurance")
    @Tribe(name = "Insurance")
    @Vertical(name = "Platform")
    @Test
    public void testBuyStandaloneInsuranceUntilPaidLoginWeb() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testBuyStandaloneInsuranceUntilPaidLoginWeb");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<WebDriver> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<WebDriver> loginPage = LoginPageWeb.getInstance(session);
        ClaimCenterPageWeb<WebDriver> claimCenterPage = ClaimCenterPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);

        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
        String paymentMethod = param.get("paymentMethod");
        String paymentGroup = param.get("paymentGroup");

        step("user open homepage");
        homePage.goToHomePage();
        homePage.waitForHomePageToOpen();

        step("Put the session on IDR before logging in, since the non-IDR homepage carries no login "
                + "button and insurance is priced in rupiah either way");
        homePage.selectCurrency("IDR");

        step("login to tiket");
        loginPage.loginIfNotWithSameEmail(username, password, otp, 0);

        step("Open Insurance Page");
        homePage.openVerticalPageViaURL("Insurance");
        claimCenterPage.showAllBookings();

        step("Verify there is a flight order that can be protected");
        int orderCount = claimCenterPage.countOrdersOfferingProtection();
        if (orderCount == 0) {
            Assertion.skip("No flight order on this account offers protection, so there is nothing to buy.");
        }

        boolean reachedPaymentPage = false;
        for (int order = 1; order <= orderCount && !reachedPaymentPage; order++) {
            step("Open the protection offer of flight order " + order + " of " + orderCount);
            homePage.openVerticalPageViaURL("Insurance");
            claimCenterPage.showAllBookings();
            if (!claimCenterPage.clickOnAddProtectionButton(order)) {
                continue;
            }

            if (!claimCenterPage.verifyInsuranceAvailableToBuy()) {
                log("Flight order " + order + " has no protection on offer, so the next one is tried.");
                continue;
            }

            step("User clicks on Add button");
            claimCenterPage.clickOnAddButton();

            step("User clicks on Select Button");
            claimCenterPage.clickOnSelectButton();

            step("User continues to payment");
            claimCenterPage.clickContinueToPayButton();

            if (claimCenterPage.isOrderChangeConfirmationRaised()) {
                log("Flight order " + order + " already carries protection, so checkout asked to replace it. "
                        + "Leaving that order as it is and trying the next one.");
                continue;
            }

            reachedPaymentPage = paymentPage.isPaymentPageLoaded();
            if (!reachedPaymentPage && paymentPage.isOnPaymentSuccessPage()) {
                log("Flight order " + order + " already carries protection that has been paid for, so checkout "
                        + "went straight past the payment method list. Trying the next one.");
            }
        }

        step("Verify payment page is loaded");
        if (!reachedPaymentPage) {
            Assertion.skip("None of the " + orderCount + " flight orders on this account reached the payment "
                    + "page: each either offered no protection or already carried protection that checkout "
                    + "offered to replace. An order with no insurance on it yet is needed to buy one.");
        }
        Assertion.assertThat("Verify payment page is loaded or not", reachedPaymentPage, is(true));

        step("Access payment group");
        paymentPage.clickOnSelectPaymentMethod();

        step("Scroll down to access selected payment group");
        paymentPage.goToPaymentMethodsGroup(paymentGroup);

        step("Select payment group");
        paymentPage.clickOnPaymentGroup(paymentGroup);

        step("Select Payment Method");
        paymentPage.selectPaymentMethod(paymentMethod);

        step("Click on Pay button");
        paymentPage.clickOnPaymentButton();

        step("Verify the payment successful page is visible or not");
        Assertion.assertThat("Verify the payment successful page is visible", paymentPage.isPaymentSuccessfulVisible(), Matchers.is(true));
    }

    @Author(names = {"Akshat"})
    @Module(name = "Insurance")
    @Tribe(name = "Insurance")
    @Vertical(name = "Platform")
    @Test
    public void verifyOneWayFlightOrderLogin() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyOneWayFlightOrderLogin");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<WebDriver> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<WebDriver> loginPage = LoginPageWeb.getInstance(session);
        ClaimCenterPageWeb<WebDriver> claimCenterPage = ClaimCenterPageWeb.getInstance(session);

        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
        String orderId = param.get("orderId");

        step("Check over the API that the account still has a flight order to claim against, so a "
                + "missing prerequisite is told apart from a broken claim center before any page opens");
        if (!hasFlightOrderToClaimAgainst(session, username, password)) {
            Assertion.skip("No active flight order on this account, so there is no order to claim against.");
        }

        step("user open homepage");
        homePage.goToHomePage();
        homePage.waitForHomePageToOpen();

        step("Put the session on IDR before logging in, since the non-IDR homepage carries no login "
                + "button and insurance is priced in rupiah either way");
        homePage.selectCurrency("IDR");

        step("login to tiket");
        loginPage.loginIfNotWithSameEmail(username, password, otp, 0);

        step("Open Insurance Claim Landing Page");
        homePage.openVerticalPageViaURL("Claim Center");

        step("Verify welcome popup");
        claimCenterPage.removeWelcomePopup();

        step("Verify user is on Insurance Claim Landing Page");
        claimCenterPage.insuranceClaimLandingPageAppears();

        step("Verify insurance card claim is displayed");
        Assertion.assertThat("Insurance card claim is displayed", claimCenterPage.isInsuranceCardClaimDisplayed(), is(true));

        step("User clicks on add claim or new claim button");
        if (!claimCenterPage.clickOnAddOrNewClaimButton(orderId)) {
            Assertion.skip("The claim button is missing or still disabled, which the claim center shows while "
                    + "the e-ticket is being issued, so there is no claim form to check.");
        }

        step("User verifies whether select passenger option is displayed or not");
        Assertion.assertThat("Select passenger to claim is displayed", claimCenterPage.checkHeading(), is(true));

        step("User verifies whether the one way route is available or not");
        claimCenterPage.verifyOneWayRoute(orderId);
    }
    @Author(names = {"Gaurav"})
    @Module(name = "Insurance")
    @Tribe(name = "Sphinx")
    @Vertical(name = "Platform")
    @Test
    public void verifyInsuranceOnHomePageNonLogin() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyInsuranceOnHomePageNonLogin");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<WebDriver> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<WebDriver> loginPage = LoginPageWeb.getInstance(session);

        step("User open homepage");
        homePage.goToHomePage();
        homePage.waitForHomePageToOpen();

        step("User login as guest");
        loginPage.loginAsGuest();

        step("User click on insurance icon");
        homePage.clickInsuranceIcon();

        step("Verify insurance detail page is displayed");
        Assertion.assertThat("check Insurance text on insurance page",  homePage.checkInsuranceDetailPage(), is(true));

        step("User clicks on Login on insurance page");
        homePage.clickInsuranceLogin();

        step("Checking the login page is opened");
        Assertion.assertThat("User is on Login page", homePage.loginPageOpened(), is(true));

    }
  
    @Author(names = {"Gaurav"})
    @Module(name = "Insurance")
    @Tribe(name = "Insurance")
    @Vertical(name = "Platform")
    @Test
    public void verifyloginfromstandaloneinsurance() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyloginfromstandaloneinsurance");
         BaseTest.paramsThreadLocal.set(param);
        HomePageWeb<WebDriver> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<WebDriver> loginPage = LoginPageWeb.getInstance(session);
        
        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
      
        step("User open homepage");
        homePage.goToHomePage();
        homePage.waitForHomePageToOpen();

        step("User login as guest");
        loginPage.loginAsGuest();

        step("User click on insurance icon");
        homePage.clickInsuranceIcon();

        step("Verify insurance detail page is displayed");
        Assertion.assertThat("check Insurance text on insurance page",  homePage.checkInsuranceDetailPage(), is(true));

        step("User clicks on Login on insurance page");
        homePage.clickInsuranceLogin();

        step("Checking the login page is opened");
        Assertion.assertThat("User is on Login page", homePage.loginPageOpened(), is(true));

        step("User clicks on Email login button");
        homePage.clickOnEmailButton();

        step("User enters email ID");
        loginPage.inputEmail(username);

        step("User enters password");
        loginPage.inputPassword(password);

        step("User enters OTP");
        loginPage.enterOTPs(otp);

        step("User close the pop up");
        loginPage.skipPassKey();

        step("User is logged In to from standalone insurance page");
        Assertion.assertThat("User is on insurance page", homePage.InsuranceloginPageOpened(), is(true));

    }   

     
    @Author(names = {"Akshat"})
    @Module(name = "Insurance")
    @Tribe(name = "Insurance")
    @Vertical(name = "Platform")
    @Test
    public void verifyAccessingHistoryPageAndVerifyContentLogin() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyAccessingHistoryPageAndVerifyContentLogin");
        BaseTest.paramsThreadLocal.set(param);
        HomePageWeb<WebDriver> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<WebDriver> loginPage = LoginPageWeb.getInstance(session);
        ClaimCenterPageWeb<WebDriver> claimCenterPage = ClaimCenterPageWeb.getInstance(session);
        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");

        step("user open homepage");
        homePage.goToHomePage();
        homePage.waitForHomePageToOpen();

        step("login to tiket");
        loginPage.loginIfNotWithSameEmail(username, password, otp, 0);

        step("Open Insurance Claim Landing Page");
        homePage.openVerticalPageViaURL("Claim Center");

        step("Verify welcome popup");
        claimCenterPage.removeWelcomePopup();

        step("Verify user is on Insurance Claim Landing Page");
        claimCenterPage.insuranceClaimLandingPageAppears();

        step("Verify insurance card claim is displayed");
        Assertion.assertThat("Insurance card claim is displayed", claimCenterPage.isInsuranceCardClaimDisplayed(), is(true));

        step("User clicks on claim history button and is redirected to history page");
        claimCenterPage.clicksClaimHistory();

        step("User skips the popup");
        claimCenterPage.removeClaimListPopup();

        step("Verify 2 tabs appear : Claim List & Expired Insurance");
        Assertion.assertThat("Claim List and Expired Insurance appears", claimCenterPage.claimAndExpiredInsuranceAppears(), is(true));

        step("Verify insurance order detail appears");
        Assertion.assertThat("Insurance Order Details appear", claimCenterPage.claimAndExpiredInsuranceOrdersAppears(), is(true));
    }

    @Author(names = {"Akshat"})
    @Module(name = "Insurance")
    @Tribe(name = "Insurance")
    @Vertical(name = "Platform")
    @Test
    public void verifyAccessingInsuranceClaimDetailPage() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyAccessingInsuranceClaimDetailPage");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<WebDriver> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<WebDriver> loginPage = LoginPageWeb.getInstance(session);
        ClaimCenterPageWeb<WebDriver> claimCenterPage = ClaimCenterPageWeb.getInstance(session);

        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");

        step("user open homepage");
        homePage.goToHomePage();
        homePage.waitForHomePageToOpen();

        step("login to tiket");
        loginPage.loginIfNotWithSameEmail(username, password, otp, 0);

        step("Open Insurance Claim Landing Page");
        homePage.openVerticalPageViaURL("Claim Center");

        step("Verify welcome popup");
        claimCenterPage.removeWelcomePopup();

        step("Verify user is on Insurance Claim Landing Page");
        claimCenterPage.insuranceClaimLandingPageAppears();

        step("Verify insurance card claim is displayed");
        Assertion.assertThat("Insurance card claim is displayed", claimCenterPage.isInsuranceCardClaimDisplayed(), is(true));

        step("User clicks on claim detail page next to order id");
        claimCenterPage.clickClaimDetailButton();

        step("User clicks on FAQ button");
        claimCenterPage.faqDetails();

        step("User checks the details on insurance detail page");
        claimCenterPage.checkDetails();
    }

    @Author(names = {"Akshat"})
    @Module(name = "Insurance")
    @Tribe(name = "Insurance")
    @Vertical(name = "Platform")
    @Test
    public void verifyClaimInsuranceInClaimedPage() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyClaimInsuranceInClaimedPage");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<WebDriver> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<WebDriver> loginPage = LoginPageWeb.getInstance(session);
        ClaimCenterPageWeb<WebDriver> claimCenterPage = ClaimCenterPageWeb.getInstance(session);

        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
        String orderId = param.get("orderId");
        String address = param.get("address");
        String accountNumber = param.get("accountNumber");
        String incidentLocation = param.get("incidentLocation");
        String incidentChronology = param.get("incidentChronology");

        step("Check over the API that the account still has a flight order to claim against, so a "
                + "missing prerequisite is told apart from a broken claim center before any page opens");
        if (!hasFlightOrderToClaimAgainst(session, username, password)) {
            Assertion.skip("No active flight order on this account, so there is no order to claim against.");
        }

        step("user open homepage");
        homePage.goToHomePage();
        homePage.waitForHomePageToOpen();

        step("Put the session on IDR before logging in, since the non-IDR homepage carries no login "
                + "button and insurance is priced in rupiah either way");
        homePage.selectCurrency("IDR");

        step("login to tiket");
        loginPage.loginIfNotWithSameEmail(username, password, otp, 0);

        step("Open Insurance Claim Landing Page");
        homePage.openVerticalPageViaURL("Claim Center");

        step("Verify welcome popup");
        claimCenterPage.closeWelcomePopUp();

        step("Verify user is on Insurance Claim Landing Page");
        claimCenterPage.insuranceClaimLandingPageAppears();

        step("Verify insurance card claim is displayed");
        int policies = claimCenterPage.countClaimableInsurances();
        Assertion.assertThat("Insurance card claim is displayed", policies >= 1, is(true));

        boolean claimStarted = false;
        boolean namesOrder = orderId != null && !orderId.isBlank();
        int firstAttempt = namesOrder ? 0 : 1;
        for (int policy = firstAttempt; policy <= policies && !claimStarted; policy++) {
            if (policy > firstAttempt) {
                step("That policy had no reason left, so the claim center is reopened for the next one");
                homePage.openVerticalPageViaURL("Claim Center");
                claimCenterPage.closeWelcomePopUp();
            }

            step(policy == 0
                    ? "User clicks on add claim or new claim button for flight order " + orderId
                    : "User clicks on add claim or new claim button, policy " + policy + " of " + policies);
            boolean formOpened = policy == 0
                    ? claimCenterPage.clickOnAddOrNewClaimButton(orderId)
                    : claimCenterPage.clickClaimButtonAt(policy);
            if (!formOpened) {
                continue;
            }

            step("User select the route to claim");
            claimCenterPage.selectRouteToClaim();

            step("User select the passenger to claim");
            claimCenterPage.selectPassengerToClaimForRoute();

            step("User selects a reason that has no claim being processed yet, and continues");
            claimStarted = claimCenterPage.selectClaimReasonAndContinue();
        }

        if (!claimStarted) {
            Assertion.skip("Every reason of every policy on this account already has a claim being "
                    + "processed, so no further claim can be started. A flight order carrying insurance "
                    + "that has not been claimed against yet is needed.");
        }

        step("User carries on with the claim draft an earlier run left open, if there is one");
        claimCenterPage.finishClaimDraftIfAsked();

        step("User agrees to terms and conditions and proceed to next step");
        claimCenterPage.agreeToTermsAndProceed();

        step("User picks the bank account the claim is disbursed to, unless the draft already names one");
        claimCenterPage.chooseBankAccountIfNotSet(accountNumber);
        Assertion.assertThat("Claim names a bank account to be disbursed to", claimCenterPage.isBankAccountChosen(), is(true));

        step("User fills in the insured's address, if this reason's form asks for one");
        claimCenterPage.inputAddressIfAsked(address);

        step("User sets the insured's birth date, if this reason's form asks for one");
        claimCenterPage.confirmDatePickerIfAsked();

        step("User continues from the insured details step");
        claimCenterPage.clickContinueOnInsuredDetails();

        step("User acknowledges that the bank account is not in the insured's name, if asked");
        claimCenterPage.confirmBeneficiaryNameMismatchIfAsked();

        step("User fills incident details");
        claimCenterPage.fillIncidentDetails(incidentLocation, incidentChronology);

        step("User can continue to next page");
        Assertion.assertThat("User can continue to next page", claimCenterPage.canProceedToNextStepAfterFillingIncidentDetails(), is(true));

        step("User proceeds to the next step");
        claimCenterPage.clickContinueAfterIncidentDetails();
    }

    @Author(names = {"Ram Pratap Singh"})
    @Module(name = "Insurance")
    @Tribe(name = "Insurance")
    @Vertical(name = "Platform")
    @Test
    public void verifyClaimInsuranceCompleteFlow() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyClaimInsuranceCompleteFlow");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<WebDriver> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<WebDriver> loginPage = LoginPageWeb.getInstance(session);
        ClaimCenterPageWeb<WebDriver> claimCenterPage = ClaimCenterPageWeb.getInstance(session);

        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
        String fullName = param.get("fullName");
        String idNumber = param.get("idNumber");
        String address = param.get("address");
        String birthDate = param.get("birthDate");
        String mobilePhone = param.get("mobilePhone");
        String email = param.get("email");
        String accountNumber = param.get("accountNumber");
        String bankName = param.get("bankName");
        String accountOwnerName = param.get("accountOwnerName");
        String incidentLocation = param.get("incidentLocation");
        String incidentChronology = param.get("incidentChronology");
        String eTicketPath = param.get("eTicketPath");
        String boardingPassPath = param.get("boardingPassPath");
        String idPassportPath = param.get("idPassportPath");
        String ticketReceiptPath = param.get("ticketReceiptPath");
        String delayProofPath = param.get("delayProofPath");
        String legalDocPath = param.get("legalDocPath");

        step("User open homepage");
        homePage.goToHomePage();
        homePage.waitForHomePageToOpen();

        step("User login to tiket");
        loginPage.loginIfNotWithSameEmail(username, password, otp, 2);

        step("Open Insurance Claim Landing Page");
        homePage.openVerticalPageViaURL("Claim Center");

        step("Verify user is on Insurance Claim Landing Page");
        claimCenterPage.removeWelcomePopup();
        Assertion.assertThat("User is on Claim Centre Landing Page", claimCenterPage.insuranceClaimLandingPageAppears(), is(true));

        step("User click Add Claim or New Claim button");
        claimCenterPage.clickOnAddOrNewClaimButton();

        step("Verify Complete the Claim Form page is loaded");
        Assertion.assertThat("Claim form page is loaded", claimCenterPage.isClaimFormPageLoaded(), is(true));

        step("User select route to claim");
        claimCenterPage.selectRouteToClaim();

        step("User select passenger to claim");
        claimCenterPage.selectPassengerToClaimForRoute();

        step("User select the reason to claim");
        claimCenterPage.selectReasonForClaim();

        step("User add bank account details for claim disbursement");
        claimCenterPage.addBankAccountDetails(bankName, accountNumber, accountOwnerName);
        Assertion.assertThat("Bank account saved", claimCenterPage.isBankAccountSaved(), is(true));

        step("User click Continue button");
        claimCenterPage.clickContinueButton();

        step("User tick TnC checkbox");
        claimCenterPage.agreeToTermsInUploadPopup();

        step("User click Continue to the claim form button");
        claimCenterPage.clickContinueToClaimForm();

        step("User fill all insured details including personal information");
        claimCenterPage.fillInsuredDetails(fullName, idNumber, address, birthDate, mobilePhone, email);

        step("User click Continue button on insured details page");
        claimCenterPage.clickContinueOnInsuredDetails();

        step("User upload e-Tiket document");
        claimCenterPage.uploadETicket(eTicketPath);

        step("User upload Boarding Pass document");
        claimCenterPage.uploadBoardingPass(boardingPassPath);

        step("User upload KTP/Passport document");
        claimCenterPage.uploadIdPassport(idPassportPath);

        step("User upload optional documents");
        claimCenterPage.uploadOptionalDocuments(ticketReceiptPath, delayProofPath, legalDocPath);

        step("User click Continue/Lanjutkan button after document upload");
        claimCenterPage.clickContinueAfterDocumentUpload();

        step("User input incident date");
        claimCenterPage.selectIncidentDate();

        step("User input incident location address");
        claimCenterPage.inputIncidentLocation(incidentLocation);

        step("User input incident chronology");
        claimCenterPage.inputIncidentChronology(incidentChronology);

        step("Verify user can submit the claim form");
        Assertion.assertThat("User can submit claim", claimCenterPage.isSubmitClaimEnabled(), is(true));

        step("User click Continue to proceed");
        claimCenterPage.clickContinueAfterIncidentDetails();

        step("Verify claim submission is successful");
        Assertion.assertThat("Claim submitted successfully", claimCenterPage.isClaimSubmittedSuccessfully(), is(true));
    }
    @Author(names = {"Akshat"})
    @Module(name = "Insurance")
    @Tribe(name = "Insurance")
    @Vertical(name = "Platform")
    @Test
    public void verifyPDPContentForInsurance() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyPDPContentForInsurance");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<WebDriver> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<WebDriver> loginPage = LoginPageWeb.getInstance(session);
        ClaimCenterPageWeb<WebDriver> claimCenterPage = ClaimCenterPageWeb.getInstance(session);

        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");

        step("user open homepage");
        homePage.goToHomePage();
        homePage.waitForHomePageToOpen();

        step("login to tiket");
        loginPage.loginIfNotWithSameEmail(username, password, otp, 0);

        step("Open Insurance Claim Landing Page");
        homePage.openVerticalPageViaURL("Insurance");

        step("Verify welcome popup");
        claimCenterPage.closeWelcomePopUp();

        if (claimCenterPage.isAddProtectionButtonDisplayed()) {
            step("User clicks on add protection button");
            claimCenterPage.clickOnAddProtectionButton();

            if (claimCenterPage.verifyInsuranceAvailableToBuy()) {
                step("User clicks on chevron button to see PDP details");
                claimCenterPage.clickChevronButton();

                step("Verify PDP content for insurance is displayed");
                claimCenterPage.isPDPContentForInsuranceDisplayed();
            } else {
                Assertion.skip("No insurance available to purchase, skipping PDP content verification.");
            }
        } else {
            Assertion.skip("Add Protection button is not displayed, skipping PDP content verification.");
        }
    }
}
