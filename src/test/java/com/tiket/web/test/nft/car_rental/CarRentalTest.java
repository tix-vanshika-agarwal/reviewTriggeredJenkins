package com.tiket.web.test.nft.car_rental;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Environment;
import com.tiket.model.Platform;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.testbase.Configs;
import com.tiket.web.page.nft.car_rental.bookingForm_Page.CarRentalBookingPageWeb;
import com.tiket.web.page.nft.car_rental.productDetail_page.CarRentalReviewBookingPageWeb;
import com.tiket.web.page.nft.car_rental.search_Page.CarRentalSearchPageWeb;
import com.tiket.web.page.nft.car_rental.searchResult_Page.CarRentalSearchResultPageWeb;
import com.tiket.web.page.order.OrderDetail.OrderDetailWeb;
import com.tiket.web.page.order.OrderList.OrderListWeb;
import com.tiket.web.dWeb.page.payment.PaymentPage;
import com.tiket.web.page.common.home.HomePageWeb;
import com.tiket.web.page.common.home.LoginPageWeb;
import org.testng.annotations.Test;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.either;
import static org.hamcrest.Matchers.equalToIgnoringCase;

import javax.security.auth.login.FailedLoginException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.core.Is.is;

public class CarRentalTest extends BaseTest {
    @Author(names = "Okta")
    @Module(name = "ReviewPage")
    @Tribe(name = "CarRental")
    @Vertical(name = "NFT")
    @Test
    public void testCheckRegularCarReviewPageWithDriverGuestWeb() {
        Map<String, String> param = getParam("testCheckRegularCarReviewPageWithDriverGuestWeb");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String driverType = param.get("driverType");
        String packageType = param.get("packageType");
        String location = param.get("location");
        String duration = param.get("duration");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        CarRentalSearchPageWeb<?> carRentalSearchPage = CarRentalSearchPageWeb.getInstance(session);
        CarRentalSearchResultPageWeb<?> carRentalSearchResultPage = CarRentalSearchResultPageWeb.getInstance(session);
        CarRentalReviewBookingPageWeb<?> carRentalReviewBookingPage = CarRentalReviewBookingPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as guest");
        loginPage.loginAsGuest();

        step("User select IDR currency");
        homePage.selectCurrency("IDR");

        step("User open car rental vertical");
        homePage.openVerticalPageViaURL("sewa-mobil");

        step("User click skip button");
        carRentalSearchPage.clickSkipButton();

        step("User select driver type");
        carRentalSearchPage.selectDriverType(driverType);

        step("User select rental location by recommendation");
        carRentalSearchPage.selectLocationByRecommendation(location);

        step("User select rental duration");
        carRentalSearchPage.selectDuration(duration);

        step("User search car rental");
        carRentalSearchPage.searchCarRental();

        step("User select first car rental");
        String carName = carRentalSearchResultPage.getFirstCarName();
        carRentalSearchResultPage.selectFirstCarRentalCard();

        step("Verify review page with driver is displayed");
        carRentalReviewBookingPage.verifyCarVendorDetails();
        carRentalReviewBookingPage.verifyCarName(carName);
        carRentalReviewBookingPage.verifyRentalArea(location);
        carRentalReviewBookingPage.verifyRentalTime();
        carRentalReviewBookingPage.verifyCarPackage(packageType);
    }

    @Author(names = {"Okta", "Clement"})
    @Module(name = "ReviewPage")
    @Tribe(name = "CarRental")
    @Vertical(name = "NFT")
    @Test
    public void testCheckRegularCarReviewPageWithDriverLogin() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testCheckRegularCarReviewPageWithDriverLogin");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));

        String driverType = param.get("driverType");
        String packageType = param.get("packageType");
        String location = param.get("location");
        String duration = param.get("duration");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        CarRentalSearchPageWeb<?> carRentalSearchPage = CarRentalSearchPageWeb.getInstance(session);
        CarRentalSearchResultPageWeb<?> carRentalSearchResultPage = CarRentalSearchResultPageWeb.getInstance(session);
        CarRentalReviewBookingPageWeb<?> carRentalReviewBookingPage = CarRentalReviewBookingPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("Login as " + username + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(username, password);

        step("User select IDR currency");
        homePage.selectCurrency("IDR");

        step("User open car rental vertical");
        homePage.openVerticalPageViaURL("sewa-mobil");

        step("User click skip button");
        carRentalSearchPage.clickSkipButton();

        step("User select driver type");
        carRentalSearchPage.selectDriverType(driverType);

        step("User select rental location by recommendation");
        carRentalSearchPage.selectLocationByRecommendation(location);

        step("User select rental duration");
        carRentalSearchPage.selectDuration(duration);

        step("User search car rental");
        carRentalSearchPage.searchCarRental();

        step("User select first car rental");
        String carName = carRentalSearchResultPage.getFirstCarName();
        carRentalSearchResultPage.selectFirstCarRentalCard();

        step("Verify review page with driver is displayed");
        carRentalReviewBookingPage.verifyCarVendorDetails();
        carRentalReviewBookingPage.verifyCarName(carName);
        carRentalReviewBookingPage.verifyRentalTime();
        carRentalReviewBookingPage.verifyCarPackage(packageType);
    }

    @Author(names = "Vipul Kumar")
    @Module(name = "HomePageWeb")
    @Tribe(name = "CarRental")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyCarRentalIsStillDisplayedNonIDRGuestWeb() {
        Map<String, String> param = getParam("testVerifyCarRentalIsStillDisplayedNonIDRGuestWeb");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String currency = param.get("currency");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("Verify the home page is opened");
        homePage.waitForHomePageToOpen();
        Assertion.assertThat("Verify the home page is opened", homePage.isHomePageOpened(), is(true));

        step("User open home page as guest");
        loginPage.loginAsGuest();

        step("User change to " + currency + " currency");
        homePage.selectCurrency(currency);

        step("User is redirected back to home page");
        Assertion.assertThat("Verify Car Rentals is still displayed on home page", homePage.verifyVerticalIsDisplayed("Car"), is(true));
    }

    @Author(names = "Clement")
    @Module(name = "HomePageWeb")
    @Tribe(name = "CarRental")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyCarRentalIsStillDisplayedNonIDRLoginWeb() throws FailedLoginException, InterruptedException{
        Map<String, String> param = getParam("testVerifyCarRentalIsStillDisplayedNonIDRLoginWeb");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String currency = param.get("currency");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("Verify the home page is opened");
        homePage.waitForHomePageToOpen();
        Assertion.assertThat("Verify the home page is opened", homePage.isHomePageOpened(), is(true));

        step("User change to " + currency + " currency");
        homePage.selectCurrency(currency);

        step("Verify current currency");
        Assertion.assertThat("Verify current currency", homePage.getCurrentCurrency(), is(currency));

        step("User is redirected back to home page");
        Assertion.assertThat("Verify Car Rentals is still displayed on home page", homePage.verifyVerticalIsDisplayed("Car"), is(true));
    }

    @Author(names = "Vipul Kumar")
    @Module(name = "HomePageWeb")
    @Tribe(name = "CarRental")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyCarRentalIsStillDisplayedNonIDRLogin() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testVerifyCarRentalIsStillDisplayedNonIDRLogin");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String currency = param.get("currency");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open home page as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("Verify user is logged in and home page is opened");
        homePage.waitForHomePageToOpen();
        Assertion.assertThat("Verify the home page is opened", homePage.isHomePageOpened(), is(true));
        Assertion.assertThat("Verify the user logged in", loginPage.checkIfLoggedIn(), is(true));

        step("User change to " + currency + " currency");
        homePage.selectCurrency(currency);

        step("User is redirected back to home page");
        Assertion.assertThat("Verify Car Rentals is still displayed on home page", homePage.verifyVerticalIsDisplayed("Car"), is(true));
    }

    @Author(names = {"Vipul Kumar", "Clement"})
    @Module(name = "CompleteBooking")
    @Tribe(name = "CarRental")
    @Vertical(name = "NFT")
    @Test
    public void carRentalCompleteBookingWithDriverWithExtraFacilitiesGuest() {
        Map<String, String> param = getParam("carRentalCompleteBookingWithDriverWithExtraFacilitiesGuest");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String currency = param.get("currency");
        String driverType = param.get("driverType");
        String location = param.get("location");
        String duration = param.get("duration");
        String type = param.get("type");
        String addLocation = param.get("addLocation");
        String addDestination = param.get("addDestination");
        String request = param.get("request");

        String fullName = CoreFunctions.randomName();
        String phoneNumber = "91" + CoreFunctions.randomInt(10);
        String email = CoreFunctions.randomEmail();
        String paymentMethod = param.get("paymentMethod");
        String paymentGroup = param.get("paymentGroup");
        String paymentType = param.get("paymentType");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        CarRentalSearchPageWeb<?> carRentalSearchPage = CarRentalSearchPageWeb.getInstance(session);
        CarRentalSearchResultPageWeb<?> carRentalSearchResultPage = CarRentalSearchResultPageWeb.getInstance(session);
        CarRentalReviewBookingPageWeb<?> carRentalReviewBookingPage = CarRentalReviewBookingPageWeb.getInstance(session);
        CarRentalBookingPageWeb<?> carRentalBookingPage = CarRentalBookingPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as guest");
        loginPage.loginAsGuest();

        step("User select IDR currency");
        homePage.selectCurrency(currency);

        step("User open car rental vertical");
        homePage.openVerticalPageViaURL("sewa-mobil");

        step("User click skip button");
        carRentalSearchPage.clickSkipButton();

        step("User select driver type");
        carRentalSearchPage.selectDriverType(driverType);

        step("User select rental location by recommendation");
        carRentalSearchPage.selectLocationByRecommendation(location);

        step("User select rental duration");
        carRentalSearchPage.selectDuration(duration);

        step("User search car rental");
        carRentalSearchPage.searchCarRental();

        step("User select first car rental");
        carRentalSearchResultPage.selectFirstCarRentalCard();

        step("User Choose Package Type");
        carRentalReviewBookingPage.selectPackageType(type);
        carRentalReviewBookingPage.getSelectedPackagePrice(type);
        carRentalReviewBookingPage.VerifyTravelAddOnPrice(type);
        String ToTalPriceOnPDP = carRentalReviewBookingPage.getTotalPrice();

        step("Proceed To booking page");
        carRentalReviewBookingPage.proceedToBookingPage();

        step("Add pickup & Drop off location");
        carRentalBookingPage.setPickUpLocation(addLocation);
        carRentalBookingPage.setSetDropOffLocation(addDestination);

        step("Add Special Request");
        carRentalBookingPage.addSpecialRequest(request);
        carRentalBookingPage.continueBookFormMWEB();

        step("Fill the details on Booking Page");
        carRentalBookingPage.fillContactDetail(fullName, phoneNumber, email);
        Assertion.assertThat("Verify total price on Booking Page is same as on PDP", carRentalBookingPage.getTotalPrice(), is(ToTalPriceOnPDP));

        step("Select Payment and proceed to Payment Page");
        carRentalBookingPage.selectPayment();

        String orderID = paymentPage.getOrderId();
        Assertion.assertThat("Verify order ID is displayed in payment page", !orderID.isEmpty(), is(true));

        if (Configs.ENVIRONMENT == Environment.PROD) {
            step("User select payment method ");
            paymentPage.clickOnSelectPaymentMethod();
            if (Configs.PLATFORM == Platform.DWEB) {
                paymentPage.goToPaymentMethodsGroup(paymentGroup);
                paymentPage.selectPaymentMethod(paymentMethod);
            } else {
                paymentPage.selectPaymentMethodMWeb(paymentMethod, paymentType);
            }

            step("User complete payment");
            paymentPage.clickOnPaymentButton();

            step("User open order list page");
            paymentPage.clickSeeOrderListButton();
        }

        if (Configs.ENVIRONMENT == Environment.GK || Configs.ENVIRONMENT == Environment.PREPROD) {
            step("User select payment method ");
            paymentPage.clickOnSelectPaymentMethod();
            if (Configs.PLATFORM == Platform.DWEB) {
                paymentPage.goToPaymentMethodsGroup(paymentGroup);
                paymentPage.selectPaymentMethod(paymentMethod);
            } else {
                paymentPage.selectPaymentMethodMWeb(paymentMethod, paymentType);
            }

            step("User complete payment");
            paymentPage.clickOnPaymentButton();

            step("User open order detail page");
            orderListPage.openActiveTicketViaURL(orderID);
        }

        Assertion.assertThat("Verify web page will redirect to login page for guest user", loginPage.isLoginPageOpened(), is(true));
    }

    @Author(names = {"Vipul Kumar", "Clement"})
    @Module(name = "CarRental")
    @Tribe(name = "NFT")
    @Vertical(name = "NFT")
    @Test
    public void CarRental_carRentalCompleteBookingWithDriverWithExtraFacilitiesLogin() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("CarRental_carRentalCompleteBookingWithDriverWithExtraFacilitiesLogin");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));

        String currency = param.get("currency");
        String driverType = param.get("driverType");
        String location = param.get("location");
        String duration = param.get("duration");
        String type = param.get("type");
        String addLocation = param.get("addLocation");
        String addDestination = param.get("addDestination");
        String request = param.get("request");
        String paymentMethod = param.get("paymentMethod");
        String paymentGroup = param.get("paymentGroup");
        String paymentType = param.get("paymentType");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        CarRentalSearchPageWeb<?> carRentalSearchPage = CarRentalSearchPageWeb.getInstance(session);
        CarRentalSearchResultPageWeb<?> carRentalSearchResultPage = CarRentalSearchResultPageWeb.getInstance(session);
        CarRentalReviewBookingPageWeb<?> carRentalReviewBookingPage = CarRentalReviewBookingPageWeb.getInstance(session);
        CarRentalBookingPageWeb<?> carRentalBookingPage = CarRentalBookingPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("Login as " + email + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(email, password);

        step("User select IDR currency");
        homePage.selectCurrency(currency);

        step("User open car rental vertical");
        homePage.openVerticalPageViaURL("sewa-mobil");

        step("User click skip button");
        carRentalSearchPage.clickSkipButton();

        step("User select driver type");
        carRentalSearchPage.selectDriverType(driverType);

        step("User select rental location by recommendation");
        carRentalSearchPage.selectLocationByRecommendation(location);

        step("User select rental duration");
        carRentalSearchPage.selectDuration(duration);

        step("User search car rental");
        carRentalSearchPage.searchCarRental();

        step("User select first car rental");
        carRentalSearchResultPage.selectFirstCarRentalCard();

        step("User Choose Package Type");
        carRentalReviewBookingPage.selectPackageType(type);
        carRentalReviewBookingPage.getSelectedPackagePrice(type);
        carRentalReviewBookingPage.VerifyTravelAddOnPrice(type);
        String ToTalPriceOnPDP = carRentalReviewBookingPage.getTotalPrice();

        step("Proceed To booking page");
        carRentalReviewBookingPage.proceedToBookingPage();

        step("Add pickup & Drop off location");
        String pickUpLocation = carRentalBookingPage.setPickUpLocation(addLocation);
        String dropOffLocation = carRentalBookingPage.setSetDropOffLocation(addDestination);

        step("Add Special Request");
        carRentalBookingPage.addSpecialRequest(request);
        carRentalBookingPage.continueBookFormMWEB();
        Assertion.assertThat("Verify total price on Booking Page is same as on PDP", carRentalBookingPage.getTotalPrice(), is(ToTalPriceOnPDP));

        step("Select Payment and proceed to Payment Page");
        carRentalBookingPage.selectPayment();

        String orderID = paymentPage.getOrderId();
        Assertion.assertThat("Verify order ID is displayed in payment page", !orderID.isEmpty(), is(true));

        if (Configs.ENVIRONMENT == Environment.GK || Configs.ENVIRONMENT == Environment.PREPROD) {
            step("User select payment method ");
            paymentPage.clickOnSelectPaymentMethod();
            if (Configs.PLATFORM == Platform.DWEB) {
                paymentPage.goToPaymentMethodsGroup(paymentGroup);
                paymentPage.selectPaymentMethod(paymentMethod);
            } else {
                paymentPage.selectPaymentMethodMWeb(paymentMethod, paymentType);
            }

            step("User complete payment");
            paymentPage.clickOnPaymentButton();
            boolean paymentSuccess = paymentPage.isPaymentSuccessfulVisible();

            step("User open order detail page");
            orderListPage.openActiveTicketViaURL(orderID);
            orderDetailPage.refreshTheWebpage();
            orderDetailPage.skipCoachmark();

            if (paymentSuccess) {
                step("User check e-tiket details");
                Assertion.assertThat("Verify Waiting for Confirmation is there or not", true, either(is(orderDetailPage.isWaitingForConfirmationAppeared())).or(is(orderDetailPage.verifyEticketManageOrder())));
                Assertion.assertThat("Verify Pickup Location is as same as on Booking Page", orderDetailPage.checkForCarRentalPickUpLocation(), is(pickUpLocation));
                Assertion.assertThat("Verify Drop-Off Location is as same as on Booking Page", orderDetailPage.checkForCarRentalDropOffLocation(), is(dropOffLocation));
            }
        }
    }

    @Author(names = {"Vipul Kumar", "Clement"})
    @Module(name = "CompleteBooking")
    @Tribe(name = "CarRental")
    @Vertical(name = "NFT")
    @Test
    public void carRentalCompleteBookingWithDriverWithoutExtraFacilitiesGuest() {
        Map<String, String> param = getParam("carRentalCompleteBookingWithDriverWithoutExtraFacilitiesGuest");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String currency = param.get("currency");
        String driverType = param.get("driverType");
        String location = param.get("location");
        String duration = param.get("duration");
        String type = param.get("type");
        String addLocation = param.get("addLocation");
        String addDestination = param.get("addDestination");

        String fullName = CoreFunctions.randomName();
        String phoneNumber = "91" + CoreFunctions.randomInt(10);
        String email = CoreFunctions.randomEmail();
        String paymentMethod = param.get("paymentMethod");
        String paymentGroup = param.get("paymentGroup");
        String paymentType = param.get("paymentType");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        CarRentalSearchPageWeb<?> carRentalSearchPage = CarRentalSearchPageWeb.getInstance(session);
        CarRentalSearchResultPageWeb<?> carRentalSearchResultPage = CarRentalSearchResultPageWeb.getInstance(session);
        CarRentalReviewBookingPageWeb<?> carRentalReviewBookingPage = CarRentalReviewBookingPageWeb.getInstance(session);
        CarRentalBookingPageWeb<?> carRentalBookingPage = CarRentalBookingPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as guest");
        loginPage.loginAsGuest();

        step("User select IDR currency");
        homePage.selectCurrency(currency);

        step("User open car rental vertical");
        homePage.openVerticalPageViaURL("sewa-mobil");

        step("User click skip button");
        carRentalSearchPage.clickSkipButton();

        step("User select driver type");
        carRentalSearchPage.selectDriverType(driverType);

        step("User select rental location by recommendation");
        carRentalSearchPage.selectLocationByRecommendation(location);

        step("User select rental duration");
        carRentalSearchPage.selectDuration(duration);

        step("User search car rental");
        carRentalSearchPage.searchCarRental();

        step("User select first car rental");
        carRentalSearchResultPage.selectFirstCarRentalCard();

        step("User Choose Package Type");
        carRentalReviewBookingPage.selectPackageType(type);
        carRentalReviewBookingPage.getSelectedPackagePrice(type);
        carRentalReviewBookingPage.VerifyTravelAddOnPrice(type);
        String ToTalPriceOnPDP = carRentalReviewBookingPage.getTotalPrice();

        step("Proceed To booking page");
        carRentalReviewBookingPage.proceedToBookingPage();

        step("Add pickup & Drop off location");
        carRentalBookingPage.setPickUpLocation(addLocation);
        carRentalBookingPage.setSetDropOffLocation(addDestination);

        step("Fill the details on Booking Page");
        carRentalBookingPage.continueBookFormMWEB();
        carRentalBookingPage.fillContactDetail(fullName, phoneNumber, email);
        Assertion.assertThat("Verify total price on Booking Page is same as on PDP", carRentalBookingPage.getTotalPrice(), is(ToTalPriceOnPDP));

        step("Select Payment and proceed to Payment Page");
        carRentalBookingPage.selectPayment();

        String orderID = paymentPage.getOrderId();
        Assertion.assertThat("Verify order ID is displayed in payment page", !orderID.isEmpty(), is(true));

        if (Configs.ENVIRONMENT == Environment.PROD) {
            step("User select payment method ");
            paymentPage.clickOnSelectPaymentMethod();
            if (Configs.PLATFORM == Platform.DWEB) {
                paymentPage.goToPaymentMethodsGroup(paymentGroup);
                paymentPage.selectPaymentMethod(paymentMethod);
            } else {
                paymentPage.selectPaymentMethodMWeb(paymentMethod, paymentType);
            }

            step("User complete payment");
            paymentPage.clickOnPaymentButton();

            step("User open order list page");
            paymentPage.clickSeeOrderListButton();
        }

        if (Configs.ENVIRONMENT == Environment.GK || Configs.ENVIRONMENT == Environment.PREPROD) {
            step("User select payment method ");
            paymentPage.clickOnSelectPaymentMethod();
            if (Configs.PLATFORM == Platform.DWEB) {
                paymentPage.goToPaymentMethodsGroup(paymentGroup);
                paymentPage.selectPaymentMethod(paymentMethod);
            } else {
                paymentPage.selectPaymentMethodMWeb(paymentMethod, paymentType);
            }

            step("User complete payment");
            paymentPage.clickOnPaymentButton();

            step("User open order detail page");
            orderListPage.openActiveTicketViaURL(orderID);
        }

        Assertion.assertThat("Verify web page will redirect to login page for guest user", loginPage.isLoginPageOpened(), is(true));
    }

    @Author(names = {"Vipul Kumar", "Clement"})
    @Module(name = "CompleteBooking")
    @Tribe(name = "CarRental")
    @Vertical(name = "NFT")
    @Test
    public void carRentalCompleteBookingWithDriverWithoutExtraFacilitiesLogin() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("carRentalCompleteBookingWithDriverWithoutExtraFacilitiesLogin");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));

        String currency = param.get("currency");
        String driverType = param.get("driverType");
        String location = param.get("location");
        String duration = param.get("duration");
        String type = param.get("type");
        String addLocation = param.get("addLocation");
        String addDestination = param.get("addDestination");
        String paymentMethod = param.get("paymentMethod");
        String paymentGroup = param.get("paymentGroup");
        String paymentType = param.get("paymentType");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        CarRentalSearchPageWeb<?> carRentalSearchPage = CarRentalSearchPageWeb.getInstance(session);
        CarRentalSearchResultPageWeb<?> carRentalSearchResultPage = CarRentalSearchResultPageWeb.getInstance(session);
        CarRentalReviewBookingPageWeb<?> carRentalReviewBookingPage = CarRentalReviewBookingPageWeb.getInstance(session);
        CarRentalBookingPageWeb<?> carRentalBookingPage = CarRentalBookingPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open home page as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User select IDR currency");
        homePage.selectCurrency(currency);

        step("User open car rental vertical");
        homePage.openVerticalPageViaURL("sewa-mobil");

        step("User click skip button");
        carRentalSearchPage.clickSkipButton();

        step("User select driver type");
        carRentalSearchPage.selectDriverType(driverType);

        step("User select rental location by recommendation");
        carRentalSearchPage.selectLocationByRecommendation(location);

        step("User select rental duration");
        carRentalSearchPage.selectDuration(duration);

        step("User search car rental");
        carRentalSearchPage.searchCarRental();

        step("User select first car rental");
        carRentalSearchResultPage.selectFirstCarRentalCard();

        step("User Choose Package Type");
        carRentalReviewBookingPage.selectPackageType(type);
        carRentalReviewBookingPage.getSelectedPackagePrice(type);
        carRentalReviewBookingPage.VerifyTravelAddOnPrice(type);
        String ToTalPriceOnPDP = carRentalReviewBookingPage.getTotalPrice();

        step("Proceed To booking page");
        carRentalReviewBookingPage.proceedToBookingPage();

        step("Add pickup & Drop off location");
        String pickUpLocation = carRentalBookingPage.setPickUpLocation(addLocation);
        String dropOffLocation = carRentalBookingPage.setSetDropOffLocation(addDestination);
        carRentalBookingPage.continueBookFormMWEB();
        Assertion.assertThat("Verify total price on Booking Page is same as on PDP", carRentalBookingPage.getTotalPrice(), is(ToTalPriceOnPDP));

        step("Select Payment and proceed to Payment Page");
        carRentalBookingPage.selectPayment();

        String orderID = paymentPage.getOrderId();
        Assertion.assertThat("Verify order ID is displayed in payment page", !orderID.isEmpty(), is(true));

        if (Configs.ENVIRONMENT == Environment.PROD) {
            step("User select payment method ");
            paymentPage.clickOnSelectPaymentMethod();
            if (Configs.PLATFORM == Platform.DWEB) {
                paymentPage.goToPaymentMethodsGroup(paymentGroup);
                paymentPage.selectPaymentMethod(paymentMethod);
            } else {
                paymentPage.selectPaymentMethodMWeb(paymentMethod, paymentType);
            }

            step("User complete payment");
            paymentPage.clickOnPaymentButton();

            step("User open order list page");
            paymentPage.clickSeeOrderListButton();
        }

        if (Configs.ENVIRONMENT == Environment.GK || Configs.ENVIRONMENT == Environment.PREPROD) {
            step("User select payment method ");
            paymentPage.clickOnSelectPaymentMethod();
            if (Configs.PLATFORM == Platform.DWEB) {
                paymentPage.goToPaymentMethodsGroup(paymentGroup);
                paymentPage.selectPaymentMethod(paymentMethod);
            } else {
                paymentPage.selectPaymentMethodMWeb(paymentMethod, paymentType);
            }

            step("User complete payment");
            paymentPage.clickOnPaymentButton();
            boolean paymentSuccess = paymentPage.isPaymentSuccessfulVisible();

            step("User open order detail page");
            orderListPage.openActiveTicketViaURL(orderID);
            orderDetailPage.refreshTheWebpage();
            orderDetailPage.skipCoachmark();

            if (paymentSuccess) {
                step("User check e-tiket details");
                Assertion.assertThat("Verify Waiting for Confirmation is there or not", true, either(is(orderDetailPage.isWaitingForConfirmationAppeared())).or(is(orderDetailPage.verifyEticketManageOrder())));
                Assertion.assertThat("Verify Pickup Location is as same as on Booking Page", orderDetailPage.checkForCarRentalPickUpLocation(), is(pickUpLocation));
                Assertion.assertThat("Verify Drop-Off Location is as same as on Booking Page", orderDetailPage.checkForCarRentalDropOffLocation(), is(dropOffLocation));
            }
        }
    }

    @Author(names = {"Vipul Kumar", "Clement"})
    @Module(name = "CompleteBooking")
    @Tribe(name = "CarRental")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyCarRentalPriceWithCurrencyNonIDRGuest() {
        Map<String, String> param = getParam("testVerifyCarRentalPriceWithCurrencyNonIDRGuest");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String currency = param.get("currency");
        String driverType = param.get("driverType");
        String location = param.get("location");
        String duration = param.get("duration");
        String type = param.get("type");
        String addLocation = param.get("addLocation");

        String fullName = CoreFunctions.randomName();
        String phoneNumber = "91" + CoreFunctions.randomInt(10);
        String email = CoreFunctions.randomEmail();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        CarRentalSearchPageWeb<?> carRentalSearchPage = CarRentalSearchPageWeb.getInstance(session);
        CarRentalSearchResultPageWeb<?> carRentalSearchResultPage = CarRentalSearchResultPageWeb.getInstance(session);
        CarRentalReviewBookingPageWeb<?> carRentalReviewBookingPage = CarRentalReviewBookingPageWeb.getInstance(session);
        CarRentalBookingPageWeb<?> carRentalBookingPage = CarRentalBookingPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as guest");
        loginPage.loginAsGuest();

        step("User select non-IDR currency");
        homePage.selectCurrency(currency);

        step("User open car rental vertical via deeplink");
        homePage.openVerticalPageViaURL("sewa-mobil");

        step("User click skip button");
        carRentalSearchPage.clickSkipButton();

        if (Configs.PLATFORM == Platform.DWEB) {
            step("Verify currency in landing search page is set to IDR");
            String searchPageCurrency = homePage.getCurrentCurrency();
            Assertion.assertThat("Verify currency in landing search page is set to IDR", searchPageCurrency.equals("IDR"), is(true));
        }

        step("User select driver type");
        carRentalSearchPage.selectDriverType(driverType);

        step("User select rental location by recommendation");
        carRentalSearchPage.selectLocationByRecommendation(location);

        step("User select rental duration");
        carRentalSearchPage.selectDuration(duration);

        step("User search car rental");
        carRentalSearchPage.searchCarRental();

        step("User select first car rental");
        carRentalSearchResultPage.selectFirstCarRentalCard();

        if (Configs.PLATFORM != Platform.DWEB) {
            step("Verify currency in search result page is set to IDR");
            String searchResultPageCurrency = carRentalSearchResultPage.getFirstCarRentalPrice();
            Assertion.assertThat("Verify currency in search result page is set to IDR", searchResultPageCurrency.contains("IDR"), is(true));
        }

        step("User Choose Package Type");
        carRentalReviewBookingPage.selectPackageType(type);
        carRentalReviewBookingPage.getSelectedPackagePrice(type);
        carRentalReviewBookingPage.VerifyTravelAddOnPrice(type);
        String ToTalPriceOnPDP = carRentalReviewBookingPage.getTotalPrice();

        step("Proceed To booking page");
        carRentalReviewBookingPage.proceedToBookingPage();

        step("Add pickup & Drop off location");
        carRentalBookingPage.setPickUpLocation(addLocation);
        carRentalBookingPage.clickSameAsPickupToggle();
        carRentalBookingPage.continueBookFormMWEB();

        step("Fill the details on Booking Page");
        carRentalBookingPage.fillContactDetail(fullName, phoneNumber, email);
        Assertion.assertThat("Verify total price on Booking Page is same as on PDP", carRentalBookingPage.getTotalPrice(), is(ToTalPriceOnPDP));

        step("Select Payment and proceed to Payment Page");
        carRentalBookingPage.selectPayment();

        String orderID = paymentPage.getOrderId();
        Assertion.assertThat("Verify order ID is displayed in payment page", !orderID.isEmpty(), is(true));
    }

    @Author(names = {"Vipul Kumar", "Clement"})
    @Module(name = "CompleteBooking")
    @Tribe(name = "CarRental")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyCarRentalPriceWithCurrencyNonIDRLogin() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testVerifyCarRentalPriceWithCurrencyNonIDRLogin");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));

        String currency = param.get("currency");
        String driverType = param.get("driverType");
        String location = param.get("location");
        String duration = param.get("duration");
        String type = param.get("type");
        String addLocation = param.get("addLocation");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        CarRentalSearchPageWeb<?> carRentalSearchPage = CarRentalSearchPageWeb.getInstance(session);
        CarRentalSearchResultPageWeb<?> carRentalSearchResultPage = CarRentalSearchResultPageWeb.getInstance(session);
        CarRentalReviewBookingPageWeb<?> carRentalReviewBookingPage = CarRentalReviewBookingPageWeb.getInstance(session);
        CarRentalBookingPageWeb<?> carRentalBookingPage = CarRentalBookingPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);

        step("User open home page");
        homePage.goToHomePage();

        step("Login as " + email + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(email, password);

        step("User select non-IDR currency");
        homePage.selectCurrency(currency);

        step("User open car rental vertical via deeplink");
        homePage.openVerticalPageViaURL("sewa-mobil");

        step("User click skip button");
        carRentalSearchPage.clickSkipButton();

        if (Configs.PLATFORM == Platform.DWEB) {
            step("Verify currency in landing search page is set to IDR");
            String searchPageCurrency = homePage.getCurrentCurrency();
            Assertion.assertThat("Verify currency in landing search page is set to IDR", searchPageCurrency.contains("IDR"), is(true));
        }

        step("User select driver type");
        carRentalSearchPage.selectDriverType(driverType);

        step("User select rental location by recommendation");
        carRentalSearchPage.selectLocationByRecommendation(location);

        step("User select rental duration");
        carRentalSearchPage.selectDuration(duration);

        step("User search car rental");
        carRentalSearchPage.searchCarRental();

        step("User select first car rental");
        carRentalSearchResultPage.selectFirstCarRentalCard();

        if (Configs.PLATFORM != Platform.DWEB) {
            step("Verify currency in search result page is set to IDR");
            String searchResultPageCurrency = carRentalSearchResultPage.getFirstCarRentalPrice();
            Assertion.assertThat("Verify currency in search result page is set to IDR", searchResultPageCurrency.contains("IDR"), is(true));
        }

        step("User Choose Package Type");
        carRentalReviewBookingPage.selectPackageType(type);
        carRentalReviewBookingPage.getSelectedPackagePrice(type);
        carRentalReviewBookingPage.VerifyTravelAddOnPrice(type);
        String ToTalPriceOnPDP = carRentalReviewBookingPage.getTotalPrice();

        step("Proceed To booking page");
        carRentalReviewBookingPage.proceedToBookingPage();

        step("Add pickup & Drop off location");
        carRentalBookingPage.setPickUpLocation(addLocation);
        carRentalBookingPage.clickSameAsPickupToggle();
        carRentalBookingPage.continueBookFormMWEB();
        Assertion.assertThat("Verify total price on Booking Page is same as on PDP", carRentalBookingPage.getTotalPrice(), is(ToTalPriceOnPDP));

        step("Select Payment and proceed to Payment Page");
        carRentalBookingPage.selectPayment();

        String orderID = paymentPage.getOrderId();
        Assertion.assertThat("Verify order ID is displayed in payment page", !orderID.isEmpty(), is(true));
    }

    @Author(names = "Hafizh")
    @Module(name = "ProductDetailPage")
    @Tribe(name = "CarRental")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyCoverageAreaPageGuestWeb() {
        Map<String, String> param = getParam("testVerifyCoverageAreaPageGuestWeb");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        CarRentalSearchPageWeb<?> carRentalSearchPage = CarRentalSearchPageWeb.getInstance(session);
        CarRentalSearchResultPageWeb<?> carRentalSearchResultPage = CarRentalSearchResultPageWeb.getInstance(session);
        CarRentalReviewBookingPageWeb<?> carRentalReviewBookingPage = CarRentalReviewBookingPageWeb.getInstance(session);

        String type = param.get("type");
        String location = param.get("location");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);
        String duration = param.get("duration");
        String carRentalPackage = param.get("carRentalPackage");
        String queryParam = param.get("queryParam");

        step("User open home page");
        homePage.goToHomePage();

        step("User open as guest");
        loginPage.loginAsGuest();

        step("User select IDR currency");
        homePage.selectCurrency("IDR");

        step("User open car rental vertical");
        homePage.openVerticalPageViaURL("sewa-mobil");

        step("User click skip button");
        carRentalSearchPage.clickSkipButton();

        step("User select driver type");
        carRentalSearchPage.selectDriverType(type);

        step("User select car rental location");
        carRentalSearchPage.selectCarRentalLocationBySearch(location);

        step("User select car rental dates");
        carRentalSearchPage.selectDate(departureDate);

        step("User select car rental duration");
        carRentalSearchPage.selectDuration(duration);

        step("User search car rental");
        carRentalSearchPage.searchCarRental();
        carRentalSearchResultPage.editTotalDaysQueryParam(queryParam, duration);

        step("User select first car card");
        carRentalSearchResultPage.selectFirstCarRentalCard();

        step("User navigate to rental area page");
        carRentalReviewBookingPage.navigateToRentalAreaPage(carRentalPackage);

        step("Verify that rental coverage area title is displayed");
        Assertion.assertThat("Rental area is displayed", carRentalReviewBookingPage.isRentalCoverageAreaTitleDisplayed(), is(true));

        step("Verify that map of coverage area is displayed");
        Assertion.assertThat("Map of coverage area is displayed", carRentalReviewBookingPage.isCoverageAreaMapDisplayed(), is(true));

        step("Verify that list of coverage areas is displayed");
        Assertion.assertThat("List of coverage areas is displayed", carRentalReviewBookingPage.isListOfCoverageAreaDisplayed(), is(true));
    }

    @Author(names = "Naufal Hafiz")
    @Module(name = "SearchResultPage")
    @Tribe(name = "CarRental")
    @Vertical(name = "NFT")
    @Test
    public void testCarRentalEditLocationOnSRPGuestWeb() throws ParseException {
        Map<String, String> param = getParam("testCarRentalEditLocationOnSRPGuestWeb");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String driverType = param.get("driverType");
        String location = param.get("location");
        String newLocation = param.get("newLocation");

        String currentDate = CoreFunctions.getCurrentLongDate();
        String newDate = CoreFunctions.addToLongDate(currentDate, 3);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        CarRentalSearchPageWeb<?> carRentalSearchPageWeb = CarRentalSearchPageWeb.getInstance(session);
        CarRentalSearchResultPageWeb<?> carRentalSearchResultPageWeb = CarRentalSearchResultPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as guest");
        loginPage.loginAsGuest();

        step("User open car rental vertical");
        homePage.openVerticalPageViaURL("sewa-mobil");

        step("User click skip button");
        carRentalSearchPageWeb.clickSkipButton();

        step("User select driver type");
        carRentalSearchPageWeb.selectDriverType(driverType);

        step("User select rental location by search");
        carRentalSearchPageWeb.selectCarRentalLocationBySearch(location);

        step("Extract form data");
        String locationValue = carRentalSearchPageWeb.getFormValue("location", "SP");
        String dateValue = carRentalSearchPageWeb.getFormValue("date", "SP");

        step("User search car rental");
        carRentalSearchPageWeb.searchCarRental();

        String srpDateValue = carRentalSearchPageWeb.getFormValue("date", "SRP");
        String normalizedSrpDateValue = srpDateValue.replaceFirst("^0+(\\d)", "$1");

        step("Assertion before edit - Location");
        Assertion.assertThat("Verify location on SRP is match on search page", carRentalSearchPageWeb.getFormValue("location", "SRP"), is(locationValue));
        step("Assertion before edit - Date");
        Assertion.assertThat("Verify date on SRP is match on search page", normalizedSrpDateValue, containsString(dateValue));
        step("Assertion before edit - Driver Type");
        Assertion.assertThat("Verify driver option is match on search page",
                carRentalSearchPageWeb.getFormValue("driver", "SRP"), either(is("With Driver")).or(is("Dengan Sopir")));

        step("First edit search result on SRP");
        carRentalSearchResultPageWeb.editSearchResult(newLocation, newDate);

        srpDateValue = carRentalSearchPageWeb.getFormValue("date", "SRP");
        srpDateValue = srpDateValue.replaceFirst("^0+(\\d)", "$1");

        step("Assertion after first edit - Location");
        Assertion.assertThat("Verify edited location is match after edit", carRentalSearchPageWeb.getFormValue("location", "SRP"), is(newLocation));
        step("Assertion after first edit - Date");
        Assertion.assertThat("Verify edited date is match after edit", srpDateValue,
            either(containsString(CoreFunctions.convertFullDateToShortDate(newDate).replaceFirst("^0+(\\d)", "$1")))
            .or(containsString(CoreFunctions.convertFullDateToShortDateIndonesian(newDate).replaceFirst("^0+(\\d)", "$1")))
        );
        step("Assertion after first  edit - Driver Type");
        Assertion.assertThat("Verify driver option after edit",
                carRentalSearchPageWeb.getFormValue("driver", "SRP"), either(is("With Driver")).or(is("Dengan Sopir")));

        step("Second edit search result on SRP");
        carRentalSearchResultPageWeb.changeDriverType("self-drive", location, newDate);

        step("Assertion after second edit - Location");
        Assertion.assertThat("Verify edited location is match after edit", carRentalSearchPageWeb.getFormValue("location", "SRP"), is(location));
        step("Assertion after second edit - Driver Type");
        Assertion.assertThat("Verify driver option after edit",
                carRentalSearchPageWeb.getFormValue("driver", "SRP"), either(is("Self Drive")).or(is("Lepas Kunci")));
    }

    @Author(names = "Okta")
    @Module(name = "SearchPage")
    @Tribe(name = "CarRental")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyCarRentalVideoFaqSectionGuestWeb() {
        Map<String, String> param = getParam("testVerifyCarRentalVideoFaqSectionGuestWeb");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String faqLink = param.get("faqLink");
        String howToRentLink = param.get("howToRentLink");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        CarRentalSearchPageWeb<?> carRentalSearchPage = CarRentalSearchPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open home page as guest");
        loginPage.loginAsGuest();

        step("User open car rental vertical");
        homePage.openVerticalPageViaURL("sewa-mobil");

        step("User click skip button");
        carRentalSearchPage.clickSkipButton();

        step("User click FAQ link");
        Assertion.assertThat("Verify FAQ page is displayed", carRentalSearchPage.clickFaqLink(), containsString(faqLink));

        step("User play video");
        Assertion.assertThat("Verify video is played", carRentalSearchPage.playVideo(), is(true));

        step("User click how to rent banner");
        Assertion.assertThat("Verify how to rent page is displayed", carRentalSearchPage.clickHowToRentBanner(), containsString(howToRentLink));
    }

    @Author(names = {"Vipul Kumar", "Clement"})
    @Module(name = "CompleteBooking")
    @Tribe(name = "CarRental")
    @Vertical(name = "NFT")
    @Test
    public void testCompleteBookingForSelfDriveGuestWeb() {
        Map<String, String> param = getParam("testCompleteBookingForSelfDriveGuestWeb");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String currency = param.get("currency");
        String driverType = param.get("driverType");
        String location = param.get("location");
        String duration = param.get("duration");
        String addLocation = param.get("addLocation");
        String addDestination = param.get("addDestination");
        String paymentMethod = param.get("paymentMethod");
        String paymentGroup = param.get("paymentGroup");
        String paymentType = param.get("paymentType");

        String fullName = CoreFunctions.randomName();
        String phoneNumber = "91" + CoreFunctions.randomInt(10);
        String email = CoreFunctions.randomEmail();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        CarRentalSearchPageWeb<?> carRentalSearchPage = CarRentalSearchPageWeb.getInstance(session);
        CarRentalSearchResultPageWeb<?> carRentalSearchResultPage = CarRentalSearchResultPageWeb.getInstance(session);
        CarRentalReviewBookingPageWeb<?> carRentalReviewBookingPage = CarRentalReviewBookingPageWeb.getInstance(session);
        CarRentalBookingPageWeb<?> carRentalBookingPage = CarRentalBookingPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as guest");
        loginPage.loginAsGuest();

        step("User select non-IDR currency");
        homePage.selectCurrency(currency);

        step("User open car rental vertical via deeplink");
        homePage.openVerticalPageViaURL("sewa-mobil");

        step("User click skip button");
        carRentalSearchPage.clickSkipButton();

        step("User select driver type");
        carRentalSearchPage.selectDriverType(driverType);

        step("User select rental location by recommendation");
        carRentalSearchPage.selectLocationByRecommendation(location);

        step("User select rental duration");
        carRentalSearchPage.selectDuration(duration);

        step("User search car rental");
        carRentalSearchPage.searchCarRental();

        step("User select first car rental");
        String carName = carRentalSearchResultPage.getFirstCarName();
        carRentalSearchResultPage.selectFirstCarRentalCard();

        step("User Go To PDP of Self Drive");
        Assertion.assertThat("Verify car details and name are displayed correctly on the review page", carRentalReviewBookingPage.verifyReviewPageIsDisplayed(carName), is(true));
        String ToTalPriceOnPDP = carRentalReviewBookingPage.getTotalPrice();

        step("Proceed To booking page");
        carRentalReviewBookingPage.proceedToBookingPage();

        step("Add pickup & Drop off location");
        carRentalBookingPage.setPickUpLocation(addLocation);
        carRentalBookingPage.setSetDropOffLocation(addDestination);

        step("Fill the details on Booking Page");
        carRentalBookingPage.continueBookFormMWEB();
        carRentalBookingPage.fillContactDetail(fullName, phoneNumber, email);
        Assertion.assertThat("Verify total price on Booking Page is same as on PDP", carRentalBookingPage.getTotalPrice(), is(ToTalPriceOnPDP));

        step("Select Payment and proceed to Payment Page");
        carRentalBookingPage.selectPayment();

        String orderID = paymentPage.getOrderId();
        Assertion.assertThat("Verify order ID is displayed in payment page", !orderID.isEmpty(), is(true));

        if (Configs.ENVIRONMENT == Environment.PROD) {
            step("User select payment method ");
            paymentPage.clickOnSelectPaymentMethod();
            if (Configs.PLATFORM == Platform.DWEB) {
                paymentPage.goToPaymentMethodsGroup(paymentGroup);
                paymentPage.selectPaymentMethod(paymentMethod);
            } else {
                paymentPage.selectPaymentMethodMWeb(paymentMethod, paymentType);
            }

            step("User complete payment");
            paymentPage.clickOnPaymentButton();

            step("User open order list page");
            paymentPage.clickSeeOrderListButton();
        }

        if (Configs.ENVIRONMENT == Environment.GK || Configs.ENVIRONMENT == Environment.PREPROD) {
            step("User select payment method ");
            paymentPage.clickOnSelectPaymentMethod();
            paymentPage.selectPaymentMethod(paymentMethod);

            step("User complete payment");
            paymentPage.clickOnPaymentButton();

            step("User open order detail page");
            orderListPage.openActiveTicketViaURL(orderID);
        }

        Assertion.assertThat("Verify web page will redirect to login page for guest user", loginPage.isLoginPageOpened(), is(true));
    }

    @Author(names = {"Vipul Kumar", "Clement"})
    @Module(name = "CompleteBooking")
    @Tribe(name = "CarRental")
    @Vertical(name = "NFT")
    @Test
    public void testCompleteBookingForSelfDriveLoginWeb() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testCompleteBookingForSelfDriveLoginWeb");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));

        String currency = param.get("currency");
        String driverType = param.get("driverType");
        String location = param.get("location");
        String duration = param.get("duration");
        String addLocation = param.get("addLocation");
        String addDestination = param.get("addDestination");
        String paymentMethod = param.get("paymentMethod");
        String paymentGroup = param.get("paymentGroup");
        String paymentType = param.get("paymentType");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        CarRentalSearchPageWeb<?> carRentalSearchPage = CarRentalSearchPageWeb.getInstance(session);
        CarRentalSearchResultPageWeb<?> carRentalSearchResultPage = CarRentalSearchResultPageWeb.getInstance(session);
        CarRentalReviewBookingPageWeb<?> carRentalReviewBookingPage = CarRentalReviewBookingPageWeb.getInstance(session);
        CarRentalBookingPageWeb<?> carRentalBookingPage = CarRentalBookingPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open home page as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User select non-IDR currency");
        homePage.selectCurrency(currency);

        step("User open car rental vertical via deeplink");
        homePage.openVerticalPageViaURL("sewa-mobil");

        step("User click skip button");
        carRentalSearchPage.clickSkipButton();

        step("User select driver type");
        carRentalSearchPage.selectDriverType(driverType);

        step("User select rental location by recommendation");
        carRentalSearchPage.selectLocationByRecommendation(location);

        step("User select rental duration");
        carRentalSearchPage.selectDuration(duration);

        step("User search car rental");
        carRentalSearchPage.searchCarRental();

        step("User select first car rental");
        String carName = carRentalSearchResultPage.getFirstCarName();
        carRentalSearchResultPage.selectFirstCarRentalCard();

        step("User Go To PDP of Self Drive");
        Assertion.assertThat("Verify car details and name are displayed correctly on the review page", carRentalReviewBookingPage.verifyReviewPageIsDisplayed(carName), is(true));
        String ToTalPriceOnPDP = carRentalReviewBookingPage.getTotalPrice();

        step("Proceed To booking page");
        carRentalReviewBookingPage.proceedToBookingPage();

        step("Add pickup & Drop off location");
        String pickUpLocation = carRentalBookingPage.setPickUpLocation(addLocation);
        String dropOffLocation = carRentalBookingPage.setSetDropOffLocation(addDestination);
        carRentalBookingPage.continueBookFormMWEB();
        Assertion.assertThat("Verify total price on Booking Page is same as on PDP", carRentalBookingPage.getTotalPrice(), is(ToTalPriceOnPDP));

        step("Select Payment and proceed to Payment Page");
        carRentalBookingPage.selectPayment();

        String orderID = paymentPage.getOrderId();
        Assertion.assertThat("Verify order ID is displayed in payment page", !orderID.isEmpty(), is(true));

        if (Configs.ENVIRONMENT == Environment.PROD) {
            step("User select payment method ");
            paymentPage.clickOnSelectPaymentMethod();
            if (Configs.PLATFORM == Platform.DWEB) {
                paymentPage.goToPaymentMethodsGroup(paymentGroup);
                paymentPage.selectPaymentMethod(paymentMethod);
            } else {
                paymentPage.selectPaymentMethodMWeb(paymentMethod, paymentType);
            }

            step("User complete payment");
            paymentPage.clickOnPaymentButton();

            step("User open order list page");
            paymentPage.clickSeeOrderListButton();
        }

        if (Configs.ENVIRONMENT == Environment.GK || Configs.ENVIRONMENT == Environment.PREPROD) {
            step("User select payment method ");
            paymentPage.clickOnSelectPaymentMethod();
            if (Configs.PLATFORM == Platform.DWEB) {
                paymentPage.goToPaymentMethodsGroup(paymentGroup);
                paymentPage.selectPaymentMethod(paymentMethod);
            } else {
                paymentPage.selectPaymentMethodMWeb(paymentMethod, paymentType);
            }

            step("User complete payment");
            paymentPage.clickOnPaymentButton();
            boolean paymentSuccess = paymentPage.isPaymentSuccessfulVisible();

            step("User open order detail page");
            orderListPage.openActiveTicketViaURL(orderID);
            orderDetailPage.refreshTheWebpage();
            orderDetailPage.skipCoachmark();

            if (paymentSuccess) {
                step("User check e-tiket details");
                Assertion.assertThat("Verify Waiting for Confirmation is there or not", true, either(is(orderDetailPage.isWaitingForConfirmationAppeared())).or(is(orderDetailPage.verifyEticketManageOrder())));
                Assertion.assertThat("Verify Pickup Location is as same as on Booking Page", orderDetailPage.checkForCarRentalPickUpLocation(), is(pickUpLocation));
                Assertion.assertThat("Verify Drop-Off Location is as same as on Booking Page", orderDetailPage.checkForCarRentalDropOffLocation(), is(dropOffLocation));
            }
        }
    }

    @Author(names = "Mridul Gupta")
    @Module(name = "CompleteBooking")
    @Tribe(name = "CarRental")
    @Vertical(name = "NFT")
    @Test
    public void testCompleteSelfDriveBookingWithAutomaticTransmissionLoginWeb() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testCompleteSelfDriveBookingWithAutomaticTransmissionLoginWeb");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String email = param.get("email");
        String password = param.get("password");

        String currency = param.get("currency");
        String driverType = param.get("driverType");
        String location = param.get("location");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String duration = param.get("duration");
        String transmission = param.get("transmission");
        String addLocation = param.get("addLocation");
        String addDestination = param.get("addDestination");

        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        CarRentalSearchPageWeb<?> carRentalSearchPage = CarRentalSearchPageWeb.getInstance(session);
        CarRentalSearchResultPageWeb<?> carRentalSearchResultPage = CarRentalSearchResultPageWeb.getInstance(session);
        CarRentalReviewBookingPageWeb<?> carRentalReviewBookingPage = CarRentalReviewBookingPageWeb.getInstance(session);
        CarRentalBookingPageWeb<?> carRentalBookingPage = CarRentalBookingPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open home page as logged in user");
        loginPage.loginWithSpecificAccountViaCookie(email, password);

        step("User select " + currency + " currency");
        homePage.selectCurrency(currency);

        step("User go to car rental landing page");
        homePage.openVerticalPageViaURL("sewa-mobil");

        step("User click skip button");
        carRentalSearchPage.clickSkipButton();

        step("User choose self drive option");
        carRentalSearchPage.selectDriverType(driverType);

        step("User select rental area");
        carRentalSearchPage.selectCarRentalLocationBySearch(location);

        step("User select DoT");
        carRentalSearchPage.selectDate(departureDate);

        step("User select rental duration");
        carRentalSearchPage.selectDuration(duration);

        step("User tap search button");
        carRentalSearchPage.searchCarRental();

        step("User click on filter and sort options");
        carRentalSearchResultPage.clickFilterBtn();

        step("User select Transmission filter");
        carRentalSearchResultPage.clickTransmissionFilter();

        step("User selects " + transmission + " transmission type");
        carRentalSearchResultPage.selectAMT();

        step("User saves the filter");
        carRentalSearchResultPage.clickSave();

        step("Verify transmission filter is applied");
        Assertion.assertThat("Verify filter chip is displayed on SRP", carRentalSearchResultPage.isElementDisplayingText(), is(true));
        Assertion.assertThat("Verify the SRP still has at least one fleet", carRentalSearchResultPage.checkList(), is(true));
        String[] transmissionTypes = transmission.split("/");
        Assertion.assertThat("Verify first fleet transmission matches " + transmission + " (accepts EN/ID variants)", carRentalSearchResultPage.getTransmissionType(), either(containsString(transmissionTypes[0].trim())).or(containsString(transmissionTypes[1].trim())));

        step("User select any available fleet on SRP");
        String carName = carRentalSearchResultPage.getFirstCarName();
        carRentalSearchResultPage.openFirstCarRentalCard();

        step("User select car rentals vendor");
        carRentalSearchResultPage.selectFirstCarVendorCard();

        step("Verify review page is displayed for selected fleet");
        Assertion.assertThat("Verify car details and name are displayed correctly on the review page", carRentalReviewBookingPage.verifyReviewPageIsDisplayed(carName), is(true));

        step("User tap continue and proceed to booking form");
        carRentalReviewBookingPage.proceedToBookingPage();

        step("User complete the booking form (pickup & drop-off)");
        carRentalBookingPage.setPickUpLocation(addLocation);
        carRentalBookingPage.setSetDropOffLocation(addDestination);
        carRentalBookingPage.continueBookFormMWEB();
        String totalPriceBookingForm = carRentalBookingPage.getTotalPrice();

        step("User tap continue payment button");
        carRentalBookingPage.selectPayment();

        step("Verify user is redirected to payment page and orderId is generated");
        String orderID = paymentPage.getOrderId();
        Assertion.assertThat("Verify order ID is displayed in payment page", !orderID.isEmpty(), is(true));
        Assertion.assertThat("Verify total price on Booking Page is same as on Payment Page", paymentPage.totalPricePaymentPage(), is(totalPriceBookingForm));
    }

    @Author(names = "Mridul Gupta")
    @Module(name = "CompleteBooking")
    @Tribe(name = "CarRental")
    @Vertical(name = "NFT")
    @Test
    public void testCompleteSelfDriveBookingWithManualTransmissionLoginWeb() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testCompleteSelfDriveBookingWithManualTransmissionLoginWeb");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String email = param.get("email");
        String password = param.get("password");

        String currency = param.get("currency");
        String driverType = param.get("driverType");
        String location = param.get("location");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String duration = param.get("duration");
        String transmission = param.get("transmission");
        String addLocation = param.get("addLocation");
        String addDestination = param.get("addDestination");

        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        CarRentalSearchPageWeb<?> carRentalSearchPage = CarRentalSearchPageWeb.getInstance(session);
        CarRentalSearchResultPageWeb<?> carRentalSearchResultPage = CarRentalSearchResultPageWeb.getInstance(session);
        CarRentalReviewBookingPageWeb<?> carRentalReviewBookingPage = CarRentalReviewBookingPageWeb.getInstance(session);
        CarRentalBookingPageWeb<?> carRentalBookingPage = CarRentalBookingPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open home page as logged in user via cookie");
        loginPage.loginWithSpecificAccountViaCookie(email, password);

        step("User select " + currency + " currency");
        homePage.selectCurrency(currency);

        step("User go to car rental landing page");
        homePage.openVerticalPageViaURL("sewa-mobil");

        step("User click skip button");
        carRentalSearchPage.clickSkipButton();

        step("User choose self drive option");
        carRentalSearchPage.selectDriverType(driverType);

        step("User select rental area");
        carRentalSearchPage.selectLocationByRecommendation(location);

        step("User select DoT");
        carRentalSearchPage.selectDate(departureDate);

        step("User select rental duration");
        carRentalSearchPage.selectDuration(duration);

        step("User tap search button");
        carRentalSearchPage.searchCarRental();

        step("User click on filter and sort options");
        carRentalSearchResultPage.clickFilterBtn();

        step("User select Transmission filter");
        carRentalSearchResultPage.clickTransmissionFilter();

        step("User selects " + transmission + " transmission type");
        carRentalSearchResultPage.selectManual();

        step("User saves the filter");
        carRentalSearchResultPage.clickSave();

        step("Verify transmission filter is applied");
        Assertion.assertThat("Verify filter chip is displayed on SRP", carRentalSearchResultPage.isElementDisplayingText(), is(true));
        Assertion.assertThat("Verify the SRP still has at least one fleet", carRentalSearchResultPage.checkList(), is(true));
        Assertion.assertThat("Verify first fleet transmission matches " + transmission + " (accepts EN/ID variants)", carRentalSearchResultPage.getTransmissionType(), equalToIgnoringCase(transmission));

        step("User select any available fleet on SRP");
        String carName = carRentalSearchResultPage.getFirstCarName();
        carRentalSearchResultPage.openFirstCarRentalCard();

        step("User select car rentals vendor");
        carRentalSearchResultPage.selectFirstCarVendorCard();

        step("Verify review page is displayed for selected fleet");
        Assertion.assertThat("Verify car details and name are displayed correctly on the review page", carRentalReviewBookingPage.verifyReviewPageIsDisplayed(carName), is(true));

        step("User tap continue and proceed to booking form");
        carRentalReviewBookingPage.proceedToBookingPage();

        step("User complete the booking form (pickup & drop-off)");
        carRentalBookingPage.setPickUpLocation(addLocation);
        carRentalBookingPage.setSetDropOffLocation(addDestination);
        carRentalBookingPage.continueBookFormMWEB();
        String totalPriceBookingForm = carRentalBookingPage.getTotalPrice();

        step("User tap continue payment button");
        carRentalBookingPage.selectPayment();

        step("Verify user is redirected to payment page and orderId is generated");
        String orderID = paymentPage.getOrderId();
        Assertion.assertThat("Verify order ID is displayed in payment page", !orderID.isEmpty(), is(true));
        Assertion.assertThat("Verify total price on Booking Page is same as on Payment Page", paymentPage.totalPricePaymentPage(), is(totalPriceBookingForm));
    }

    @Author(names = "Mridul Gupta")
    @Module(name = "CompleteBooking")
    @Tribe(name = "CarRental")
    @Vertical(name = "NFT")
    @Test
    public void testCompleteWithDriverBookingWithCarOnlyPackageLoginWeb() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testCompleteWithDriverBookingWithCarOnlyPackageLoginWeb");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String email = param.get("email");
        String password = param.get("password");

        String currency = param.get("currency");
        String driverType = param.get("driverType");
        String location = param.get("location");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String duration = param.get("duration");
        String packageType = param.get("packageType");
        String addLocation = param.get("addLocation");
        String addDestination = param.get("addDestination");

        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        CarRentalSearchPageWeb<?> carRentalSearchPage = CarRentalSearchPageWeb.getInstance(session);
        CarRentalSearchResultPageWeb<?> carRentalSearchResultPage = CarRentalSearchResultPageWeb.getInstance(session);
        CarRentalReviewBookingPageWeb<?> carRentalReviewBookingPage = CarRentalReviewBookingPageWeb.getInstance(session);
        CarRentalBookingPageWeb<?> carRentalBookingPage = CarRentalBookingPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open home page as logged in user via cookie");
        loginPage.loginWithSpecificAccountViaCookie(email, password);

        step("User select " + currency + " currency");
        homePage.selectCurrency(currency);

        step("User go to car rental landing page");
        homePage.openVerticalPageViaURL("sewa-mobil");

        step("User click skip button");
        carRentalSearchPage.clickSkipButton();

        step("User choose with driver option");
        carRentalSearchPage.selectDriverType(driverType);

        step("User select rental area");
        carRentalSearchPage.selectLocationByRecommendation(location);

        step("User select DoT");
        carRentalSearchPage.selectDate(departureDate);

        step("User select rental duration");
        carRentalSearchPage.selectDuration(duration);

        step("User tap search button");
        carRentalSearchPage.searchCarRental();

        step("User select any available fleet on SRP");
        String carName = carRentalSearchResultPage.getFirstCarName();
        carRentalSearchResultPage.openFirstCarRentalCard();

        step("User select car rentals vendor");
        carRentalSearchResultPage.selectFirstCarVendorCard();

        step("Verify review page is displayed for selected fleet");
        Assertion.assertThat("Verify car details and name are displayed correctly on the review page", carRentalReviewBookingPage.verifyReviewPageIsDisplayed(carName), is(true));

        step("User select " + packageType + " package on review page");
        carRentalReviewBookingPage.selectPackageType(packageType);
        String totalPriceOnPDP = carRentalReviewBookingPage.getTotalPrice();

        step("User tap continue and proceed to booking form");
        carRentalReviewBookingPage.proceedToBookingPage();

        step("User complete the booking form (pickup & drop-off)");
        carRentalBookingPage.setPickUpLocation(addLocation);
        carRentalBookingPage.setSetDropOffLocation(addDestination);
        carRentalBookingPage.continueBookFormMWEB();
        Assertion.assertThat("Verify total price on Booking Page is same as on PDP", carRentalBookingPage.getTotalPrice(), is(totalPriceOnPDP));

        step("User tap continue payment button");
        carRentalBookingPage.selectPayment();

        step("Verify user is redirected to payment page and orderId is generated");
        String orderID = paymentPage.getOrderId();
        Assertion.assertThat("Verify order ID is displayed in payment page", !orderID.isEmpty(), is(true));
    }

    @Author(names = "Mridul Gupta")
    @Module(name = "CompleteBooking")
    @Tribe(name = "CarRental")
    @Vertical(name = "NFT")
    @Test
    public void testCompleteWithDriverBookingWithFuelPackageGuestWeb() {
        Map<String, String> param = getParam("testCompleteWithDriverBookingWithFuelPackageGuestWeb");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String currency = param.get("currency");
        String driverType = param.get("driverType");
        String location = param.get("location");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String duration = param.get("duration");
        String packageType = param.get("packageType");
        String addLocation = param.get("addLocation");
        String addDestination = param.get("addDestination");

        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);

        String fullName = CoreFunctions.randomName();
        String phoneNumber = "91" + CoreFunctions.randomInt(10);
        String guestEmail = CoreFunctions.randomEmail();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        CarRentalSearchPageWeb<?> carRentalSearchPage = CarRentalSearchPageWeb.getInstance(session);
        CarRentalSearchResultPageWeb<?> carRentalSearchResultPage = CarRentalSearchResultPageWeb.getInstance(session);
        CarRentalReviewBookingPageWeb<?> carRentalReviewBookingPage = CarRentalReviewBookingPageWeb.getInstance(session);
        CarRentalBookingPageWeb<?> carRentalBookingPage = CarRentalBookingPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open home page as guest (non-login)");
        loginPage.loginAsGuest();

        step("User select " + currency + " currency");
        homePage.selectCurrency(currency);

        step("User go to car rental landing page");
        homePage.openVerticalPageViaURL("sewa-mobil");

        step("User click skip button");
        carRentalSearchPage.clickSkipButton();

        step("User choose with driver option");
        carRentalSearchPage.selectDriverType(driverType);

        step("User select rental area");
        carRentalSearchPage.selectLocationByRecommendation(location);

        step("User select DoT");
        carRentalSearchPage.selectDate(departureDate);

        step("User select rental duration");
        carRentalSearchPage.selectDuration(duration);

        step("User tap search button");
        carRentalSearchPage.searchCarRental();

        step("User select any available fleet on SRP");
        String carName = carRentalSearchResultPage.getFirstCarName();
        carRentalSearchResultPage.openFirstCarRentalCard();

        step("User select car rentals vendor");
        carRentalSearchResultPage.selectFirstCarVendorCard();

        step("Verify review page is displayed for selected fleet");
        Assertion.assertThat("Verify car details and name are displayed correctly on the review page", carRentalReviewBookingPage.verifyReviewPageIsDisplayed(carName), is(true));

        step("User select " + packageType + " package on review page");
        carRentalReviewBookingPage.selectPackageType(packageType);
        String totalPriceOnPDP = carRentalReviewBookingPage.getTotalPrice();

        step("User tap continue and proceed to booking form");
        carRentalReviewBookingPage.proceedToBookingPage();

        step("User complete the booking form (pickup & drop-off + contact details)");
        carRentalBookingPage.setPickUpLocation(addLocation);
        carRentalBookingPage.setSetDropOffLocation(addDestination);
        carRentalBookingPage.continueBookFormMWEB();
        carRentalBookingPage.fillContactDetail(fullName, phoneNumber, guestEmail);
        Assertion.assertThat("Verify total price on Booking Page is same as on PDP", carRentalBookingPage.getTotalPrice(), is(totalPriceOnPDP));

        step("User tap continue payment button");
        carRentalBookingPage.selectPayment();

        step("Verify user is redirected to payment page and orderId is generated");
        String orderID = paymentPage.getOrderId();
        Assertion.assertThat("Verify order ID is displayed in payment page", !orderID.isEmpty(), is(true));
    }

    @Author(names = "Mridul Gupta")
    @Module(name = "CompleteBooking")
    @Tribe(name = "CarRental")
    @Vertical(name = "NFT")
    @Test
    public void testCompleteWithDriverBookingWithAllInPackageLoginWeb() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testCompleteWithDriverBookingWithAllInPackageLoginWeb");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String email = param.get("email");
        String password = param.get("password");

        String currency = param.get("currency");
        String driverType = param.get("driverType");
        String location = param.get("location");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String duration = param.get("duration");
        String packageType = param.get("packageType");
        String addLocation = param.get("addLocation");
        String addDestination = param.get("addDestination");

        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        CarRentalSearchPageWeb<?> carRentalSearchPage = CarRentalSearchPageWeb.getInstance(session);
        CarRentalSearchResultPageWeb<?> carRentalSearchResultPage = CarRentalSearchResultPageWeb.getInstance(session);
        CarRentalReviewBookingPageWeb<?> carRentalReviewBookingPage = CarRentalReviewBookingPageWeb.getInstance(session);
        CarRentalBookingPageWeb<?> carRentalBookingPage = CarRentalBookingPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open home page as logged in user via cookie");
        loginPage.loginWithSpecificAccountViaCookie(email, password);

        step("User select " + currency + " currency");
        homePage.selectCurrency(currency);

        step("User go to car rental landing page");
        homePage.openVerticalPageViaURL("sewa-mobil");

        step("User click skip button");
        carRentalSearchPage.clickSkipButton();

        step("User choose with driver option");
        carRentalSearchPage.selectDriverType(driverType);

        step("User select rental area");
        carRentalSearchPage.selectLocationByRecommendation(location);

        step("User select DoT");
        carRentalSearchPage.selectDate(departureDate);

        step("User select rental duration (default 1 day)");
        carRentalSearchPage.selectDuration(duration);

        step("User tap search button");
        carRentalSearchPage.searchCarRental();

        step("User select any available fleet on SRP");
        String carName = carRentalSearchResultPage.getFirstCarName();
        carRentalSearchResultPage.openFirstCarRentalCard();

        step("User select car rentals vendor");
        carRentalSearchResultPage.selectFirstCarVendorCard();

        step("Verify review page is displayed for selected fleet");
        Assertion.assertThat("Verify car details and name are displayed correctly on the review page", carRentalReviewBookingPage.verifyReviewPageIsDisplayed(carName), is(true));

        step("User select " + packageType + " package on review page");
        carRentalReviewBookingPage.selectPackageType(packageType);
        String totalPriceOnPDP = carRentalReviewBookingPage.getTotalPrice();

        step("User tap continue and proceed to booking form");
        carRentalReviewBookingPage.proceedToBookingPage();

        step("User complete the booking form (pickup & drop-off)");
        carRentalBookingPage.setPickUpLocation(addLocation);
        carRentalBookingPage.setSetDropOffLocation(addDestination);
        carRentalBookingPage.continueBookFormMWEB();
        Assertion.assertThat("Verify total price on Booking Page is same as on PDP", carRentalBookingPage.getTotalPrice(), is(totalPriceOnPDP));

        step("User tap continue payment button");
        carRentalBookingPage.selectPayment();

        step("Verify user is redirected to payment page and orderId is generated");
        String orderID = paymentPage.getOrderId();
        Assertion.assertThat("Verify order ID is displayed in payment page", !orderID.isEmpty(), is(true));
    }

    @Author(names = {"Vipul Kumar", "Clement"})
    @Module(name = "CompleteBooking")
    @Tribe(name = "CarRental")
    @Vertical(name = "NFT")
    @Test
    public void testCompleteBookingForSelfDriveWithExtraFacilitiesGuestWeb() {
        Map<String, String> param = getParam("testCompleteBookingForSelfDriveWithExtraFacilitiesGuestWeb");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String currency = param.get("currency");
        String driverType = param.get("driverType");
        String location = param.get("location");
        String duration = param.get("duration");
        String addLocation = param.get("addLocation");
        String addDestination = param.get("addDestination");
        String request = param.get("request");
        String paymentMethod = param.get("paymentMethod");
        String paymentGroup = param.get("paymentGroup");
        String paymentType = param.get("paymentType");

        String fullName = CoreFunctions.randomName();
        String phoneNumber = "91" + CoreFunctions.randomInt(10);
        String email = CoreFunctions.randomEmail();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        CarRentalSearchPageWeb<?> carRentalSearchPage = CarRentalSearchPageWeb.getInstance(session);
        CarRentalSearchResultPageWeb<?> carRentalSearchResultPage = CarRentalSearchResultPageWeb.getInstance(session);
        CarRentalReviewBookingPageWeb<?> carRentalReviewBookingPage = CarRentalReviewBookingPageWeb.getInstance(session);
        CarRentalBookingPageWeb<?> carRentalBookingPage = CarRentalBookingPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as guest");
        loginPage.loginAsGuest();

        step("User select non-IDR currency");
        homePage.selectCurrency(currency);

        step("User open car rental vertical via deeplink");
        homePage.openVerticalPageViaURL("sewa-mobil");

        step("User click skip button");
        carRentalSearchPage.clickSkipButton();

        step("User select driver type");
        carRentalSearchPage.selectDriverType(driverType);

        step("User select rental location by recommendation");
        carRentalSearchPage.selectLocationByRecommendation(location);

        step("User select rental duration");
        carRentalSearchPage.selectDuration(duration);

        step("User search car rental");
        carRentalSearchPage.searchCarRental();

        step("User select first car rental");
        String carName = carRentalSearchResultPage.getFirstCarName();
        carRentalSearchResultPage.selectFirstCarRentalCard();

        step("User Go To PDP of Self Drive");
        Assertion.assertThat("Verify car details and name are displayed correctly on the review page", carRentalReviewBookingPage.verifyReviewPageIsDisplayed(carName), is(true));
        String ToTalPriceOnPDP = carRentalReviewBookingPage.getTotalPrice();

        step("Proceed To booking page");
        carRentalReviewBookingPage.proceedToBookingPage();

        step("Add pickup & Drop off location");
        carRentalBookingPage.setPickUpLocation(addLocation);
        carRentalBookingPage.setSetDropOffLocation(addDestination);

        step("Add Special Request");
        carRentalBookingPage.addSpecialRequest(request);
        carRentalBookingPage.continueBookFormMWEB();

        step("Fill the details on Booking Page");
        carRentalBookingPage.fillContactDetail(fullName, phoneNumber, email);
        Assertion.assertThat("Verify total price on Booking Page is same as on PDP", carRentalBookingPage.getTotalPrice(), is(ToTalPriceOnPDP));

        step("Select Payment and proceed to Payment Page");
        carRentalBookingPage.selectPayment();

        String orderID = paymentPage.getOrderId();
        Assertion.assertThat("Verify order ID is displayed in payment page", !orderID.isEmpty(), is(true));

        if (Configs.ENVIRONMENT == Environment.PROD) {
            step("User select payment method ");
            paymentPage.clickOnSelectPaymentMethod();
            if (Configs.PLATFORM == Platform.DWEB) {
                paymentPage.goToPaymentMethodsGroup(paymentGroup);
                paymentPage.selectPaymentMethod(paymentMethod);
            } else {
                paymentPage.selectPaymentMethodMWeb(paymentMethod, paymentType);
            }

            step("User complete payment");
            paymentPage.clickOnPaymentButton();

            step("User open order list page");
            paymentPage.clickSeeOrderListButton();
        }

        if (Configs.ENVIRONMENT == Environment.GK || Configs.ENVIRONMENT == Environment.PREPROD) {
            step("User select payment method ");
            paymentPage.clickOnSelectPaymentMethod();
            if (Configs.PLATFORM == Platform.DWEB) {
                paymentPage.goToPaymentMethodsGroup(paymentGroup);
                paymentPage.selectPaymentMethod(paymentMethod);
            } else {
                paymentPage.selectPaymentMethodMWeb(paymentMethod, paymentType);
            }

            step("User complete payment");
            paymentPage.clickOnPaymentButton();

            step("User open order detail page");
            orderListPage.openActiveTicketViaURL(orderID);
        }

        Assertion.assertThat("Verify web page will redirect to login page for guest user", loginPage.isLoginPageOpened(), is(true));
    }

    @Author(names = {"Vipul Kumar", "Clement"})
    @Module(name = "CarRental")
    @Tribe(name = "NFT")
    @Vertical(name = "NFT")
    @Test
    public void CarRental_testCompleteBookingForSelfDriveWithExtraFacilitiesLoginWeb() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("CarRental_testCompleteBookingForSelfDriveWithExtraFacilitiesLoginWeb");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));

        String currency = param.get("currency");
        String driverType = param.get("driverType");
        String location = param.get("location");
        String duration = param.get("duration");
        String addLocation = param.get("addLocation");
        String addDestination = param.get("addDestination");
        String request = param.get("request");
        String paymentMethod = param.get("paymentMethod");
        String paymentGroup = param.get("paymentGroup");
        String paymentType = param.get("paymentType");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        CarRentalSearchPageWeb<?> carRentalSearchPage = CarRentalSearchPageWeb.getInstance(session);
        CarRentalSearchResultPageWeb<?> carRentalSearchResultPage = CarRentalSearchResultPageWeb.getInstance(session);
        CarRentalReviewBookingPageWeb<?> carRentalReviewBookingPage = CarRentalReviewBookingPageWeb.getInstance(session);
        CarRentalBookingPageWeb<?> carRentalBookingPage = CarRentalBookingPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("Login as " + email + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(email, password);

        step("User select non-IDR currency");
        homePage.selectCurrency(currency);

        step("User open car rental vertical via deeplink");
        homePage.openVerticalPageViaURL("sewa-mobil");

        step("User click skip button");
        carRentalSearchPage.clickSkipButton();

        step("User select driver type");
        carRentalSearchPage.selectDriverType(driverType);

        step("User select rental location by recommendation");
        carRentalSearchPage.selectLocationByRecommendation(location);

        step("User select rental duration");
        carRentalSearchPage.selectDuration(duration);

        step("User search car rental");
        carRentalSearchPage.searchCarRental();

        step("User select first car rental");
        String carName = carRentalSearchResultPage.getFirstCarName();
        carRentalSearchResultPage.selectFirstCarRentalCard();

        step("User Go To PDP of Self Drive");
        Assertion.assertThat("Verify car details and name are displayed correctly on the review page", carRentalReviewBookingPage.verifyReviewPageIsDisplayed(carName), is(true));
        String ToTalPriceOnPDP = carRentalReviewBookingPage.getTotalPrice();

        step("Proceed To booking page");
        carRentalReviewBookingPage.proceedToBookingPage();

        step("Add pickup & Drop off location");
        String pickUpLocation = carRentalBookingPage.setPickUpLocation(addLocation);
        String dropOffLocation = carRentalBookingPage.setSetDropOffLocation(addDestination);

        step("Add Special Request");
        carRentalBookingPage.addSpecialRequest(request);
        carRentalBookingPage.continueBookFormMWEB();
        Assertion.assertThat("Verify total price on Booking Page is same as on PDP", carRentalBookingPage.getTotalPrice(), is(ToTalPriceOnPDP));

        step("Select Payment and proceed to Payment Page");
        carRentalBookingPage.selectPayment();

        String orderID = paymentPage.getOrderId();
        Assertion.assertThat("Verify order ID is displayed in payment page", !orderID.isEmpty(), is(true));

        if (Configs.ENVIRONMENT == Environment.PROD) {
            step("User select payment method ");
            paymentPage.clickOnSelectPaymentMethod();
            if (Configs.PLATFORM == Platform.DWEB) {
                paymentPage.goToPaymentMethodsGroup(paymentGroup);
                paymentPage.selectPaymentMethod(paymentMethod);
            } else {
                paymentPage.selectPaymentMethodMWeb(paymentMethod, paymentType);
            }

            step("User complete payment");
            paymentPage.clickOnPaymentButton();

            step("User open order list page");
            paymentPage.clickSeeOrderListButton();
        }

        if (Configs.ENVIRONMENT == Environment.GK || Configs.ENVIRONMENT == Environment.PREPROD) {
            step("User select payment method ");
            paymentPage.clickOnSelectPaymentMethod();
            if (Configs.PLATFORM == Platform.DWEB) {
                paymentPage.goToPaymentMethodsGroup(paymentGroup);
                paymentPage.selectPaymentMethod(paymentMethod);
            } else {
                paymentPage.selectPaymentMethodMWeb(paymentMethod, paymentType);
            }
            
            step("User complete payment");
            paymentPage.clickOnPaymentButton();
            boolean paymentSuccess = paymentPage.isPaymentSuccessfulVisible();

            step("User open order detail page");
            orderListPage.openActiveTicketViaURL(orderID);
            orderDetailPage.refreshTheWebpage();
            orderDetailPage.skipCoachmark();

            if (paymentSuccess) {
                step("User check e-tiket details");
                Assertion.assertThat("Verify Waiting for Confirmation is there or not", true, either(is(orderDetailPage.isWaitingForConfirmationAppeared())).or(is(orderDetailPage.verifyEticketManageOrder())));
                Assertion.assertThat("Verify Pickup Location is as same as on Booking Page", orderDetailPage.checkForCarRentalPickUpLocation(), is(pickUpLocation));
                Assertion.assertThat("Verify Drop-Off Location is as same as on Booking Page", orderDetailPage.checkForCarRentalDropOffLocation(), is(dropOffLocation));
            }
        }
    }

    @Author(names = "Vipul Kumar")
    @Module(name = "ProductDetailPage")
    @Tribe(name = "CarRental")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyDetailsOnPDPPageGuestWeb() {
        Map<String, String> param = getParam("testVerifyDetailsOnPDPPageGuestWeb");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String currency = param.get("currency");
        String driverType = param.get("driverType");
        String location = param.get("location");
        String duration = param.get("duration");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        CarRentalSearchPageWeb<?> carRentalSearchPage = CarRentalSearchPageWeb.getInstance(session);
        CarRentalSearchResultPageWeb<?> carRentalSearchResultPage = CarRentalSearchResultPageWeb.getInstance(session);
        CarRentalReviewBookingPageWeb<?> carRentalReviewBookingPage = CarRentalReviewBookingPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as guest");
        loginPage.loginAsGuest();

        step("User select non-IDR currency");
        homePage.selectCurrency(currency);

        step("User open car rental vertical via deeplink");
        homePage.openVerticalPageViaURL("sewa-mobil");

        step("User click skip button");
        carRentalSearchPage.clickSkipButton();

        step("User select driver type");
        carRentalSearchPage.selectDriverType(driverType);

        step("User select rental location by recommendation");
        carRentalSearchPage.selectLocationByRecommendation(location);

        step("User select rental duration");
        carRentalSearchPage.selectDuration(duration);

        step("User search car rental");
        carRentalSearchPage.searchCarRental();

        step("User select first car rental");
        String carName = carRentalSearchResultPage.getFirstCarName();
        carRentalSearchResultPage.selectFirstCarRentalCard();

        step("User Go To PDP of Self Drive");
        Assertion.assertThat("Verify car details and name are displayed correctly on the review page", carRentalReviewBookingPage.verifyReviewPageIsDisplayed(carName), is(true));
    }

    @Author(names = {"Vipul Kumar", "Clement Marly"})
    @Module(name = "ProductDetailPage")
    @Tribe(name = "CarRental")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyDetailsOnPDPPageLoginWeb() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testVerifyDetailsOnPDPPageLoginWeb");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));

        String currency = param.get("currency");
        String driverType = param.get("driverType");
        String location = param.get("location");
        String duration = param.get("duration");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        CarRentalSearchPageWeb<?> carRentalSearchPage = CarRentalSearchPageWeb.getInstance(session);
        CarRentalSearchResultPageWeb<?> carRentalSearchResultPage = CarRentalSearchResultPageWeb.getInstance(session);
        CarRentalReviewBookingPageWeb<?> carRentalReviewBookingPage = CarRentalReviewBookingPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open home page as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User select non-IDR currency");
        homePage.selectCurrency(currency);

        step("User open car rental vertical via deeplink");
        homePage.openVerticalPageViaURL("sewa-mobil");

        step("User click skip button");
        carRentalSearchPage.clickSkipButton();

        step("User select driver type");
        carRentalSearchPage.selectDriverType(driverType);

        step("User select rental location by recommendation");
        carRentalSearchPage.selectLocationByRecommendation(location);

        step("User select rental duration");
        carRentalSearchPage.selectDuration(duration);

        step("User search car rental");
        carRentalSearchPage.searchCarRental();

        step("User select first car rental");
        String carName = carRentalSearchResultPage.getFirstCarName();
        carRentalSearchResultPage.selectFirstCarRentalCard();

        step("User Go To PDP of Self Drive");
        Assertion.assertThat("Verify car details and name are displayed correctly on the review page", carRentalReviewBookingPage.verifyReviewPageIsDisplayed(carName), is(true));
    }

    @Author(names = "Vipul Kumar")
    @Module(name = "SearchPage")
    @Tribe(name = "CarRental")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyCarRentalVideoFaqSectionLoginWeb() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testVerifyCarRentalVideoFaqSectionLoginWeb");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String faqLink = param.get("faqLink");
        String howToRentLink = param.get("howToRentLink");
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        CarRentalSearchPageWeb<?> carRentalSearchPage = CarRentalSearchPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open home page as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User open car rental vertical");
        homePage.openVerticalPageViaURL("sewa-mobil");

        step("User click skip button");
        carRentalSearchPage.clickSkipButton();

        step("User click FAQ link");
        Assertion.assertThat("Verify FAQ page is displayed", carRentalSearchPage.clickFaqLink(), containsString(faqLink));

        step("User play video");
        Assertion.assertThat("Verify video is played", carRentalSearchPage.playVideo(), is(true));

        step("User click how to rent banner");
        Assertion.assertThat("Verify how to rent page is displayed", carRentalSearchPage.clickHowToRentBanner(), containsString(howToRentLink));
    }

    @Author(names = "Vipul Kumar")
    @Module(name = "ProductDetailPage")
    @Tribe(name = "CarRental")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyCoverageAreaPageLoginWeb() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testVerifyCoverageAreaPageLoginWeb");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        CarRentalSearchPageWeb<?> carRentalSearchPage = CarRentalSearchPageWeb.getInstance(session);
        CarRentalSearchResultPageWeb<?> carRentalSearchResultPage = CarRentalSearchResultPageWeb.getInstance(session);
        CarRentalReviewBookingPageWeb<?> carRentalReviewBookingPage = CarRentalReviewBookingPageWeb.getInstance(session);

        String type = param.get("type");
        String location = param.get("location");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);
        String duration = param.get("duration");
        String carRentalPackage = param.get("carRentalPackage");
        String queryParam = param.get("queryParam");

        step("User open home page");
        homePage.goToHomePage();

        step("User open home page as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User select IDR currency");
        homePage.selectCurrency("IDR");

        step("User open car rental vertical");
        homePage.openVerticalPageViaURL("sewa-mobil");

        step("User click skip button");
        carRentalSearchPage.clickSkipButton();

        step("User select driver type");
        carRentalSearchPage.selectDriverType(type);

        step("User select car rental location");
        carRentalSearchPage.selectCarRentalLocationBySearch(location);

        step("User select car rental dates");
        carRentalSearchPage.selectDate(departureDate);

        step("User select car rental duration");
        carRentalSearchPage.selectDuration(duration);

        step("User search car rental");
        carRentalSearchPage.searchCarRental();
        carRentalSearchResultPage.editTotalDaysQueryParam(queryParam, duration);

        step("User select first car card");
        carRentalSearchResultPage.selectFirstCarRentalCard();

        step("User navigate to rental area page");
        carRentalReviewBookingPage.navigateToRentalAreaPage(carRentalPackage);

        step("Verify that rental coverage area title is displayed");
        Assertion.assertThat("Rental area is displayed", carRentalReviewBookingPage.isRentalCoverageAreaTitleDisplayed(), is(true));

        step("Verify that map of coverage area is displayed");
        Assertion.assertThat("Map of coverage area is displayed", carRentalReviewBookingPage.isCoverageAreaMapDisplayed(), is(true));

        step("Verify that list of coverage areas is displayed");
        Assertion.assertThat("List of coverage areas is displayed", carRentalReviewBookingPage.isListOfCoverageAreaDisplayed(), is(true));
    }

    @Author(names = {"Naufal Hafiz", "Clement"})
    @Module(name = "ProductDetailPage")
    @Tribe(name = "CarRental")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyCheckDetailBookingFormWithoutDriverLoginWeb() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testVerifyCheckDetailBookingFormWithoutDriverLoginWeb");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        CarRentalSearchPageWeb<?> carRentalSearchPage = CarRentalSearchPageWeb.getInstance(session);
        CarRentalSearchResultPageWeb<?> carRentalSearchResultPage = CarRentalSearchResultPageWeb.getInstance(session);
        CarRentalReviewBookingPageWeb<?> carRentalReviewBookingPage = CarRentalReviewBookingPageWeb.getInstance(session);
        CarRentalBookingPageWeb<?> carRentalBookingPageWeb = CarRentalBookingPageWeb.getInstance(session);

        String type = param.get("type");
        String location = param.get("location");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);
        String duration = param.get("duration");
        String specialRequest = param.get("specialRequest");
        String pickup = param.get("pickup");
        String dropoff = param.get("dropoff");

        step("User open home page");
        homePage.goToHomePage();

        step("User open home page as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User open car rental vertical");
        homePage.openVerticalPageViaURL("sewa-mobil");

        step("User click skip button");
        carRentalSearchPage.clickSkipButton();

        step("User select driver type");
        carRentalSearchPage.selectDriverType(type);

        step("User select car rental location");
        carRentalSearchPage.selectCarRentalLocationBySearch(location);

        step("User select car rental dates");
        carRentalSearchPage.selectDate(departureDate);

        step("User select car rental duration");
        carRentalSearchPage.selectDuration(duration);

        step("User search car rental");
        carRentalSearchPage.searchCarRental();

        step("Extract needed data");
        carRentalSearchResultPage.openFirstCarRentalCard();
        String carModel = carRentalSearchResultPage.getCarModel();
        String vendorName = carRentalSearchResultPage.getVendorName();
        carRentalSearchResultPage.closeFirstCarRentalCard();

        step("User select car");
        carRentalSearchResultPage.selectFirstCarRentalCard();

        step("User navigate to booking page");
        carRentalReviewBookingPage.proceedToBookingPage();

        step("Add special request");
        carRentalBookingPageWeb.addSpecialRequest(specialRequest);

        step("Get special request");
        String specialRequestText = carRentalBookingPageWeb.getSpecialRequest();

        step("Verify special request");
        Assertion.assertThat("Special request should be the same", specialRequest, is(specialRequestText));

        step("Verify booking form text");
        Assertion.assertThat("Pickup & Dropoff Location should exists", carRentalBookingPageWeb.isBookingTextExists(), is(true));

        step("Set pickup & dropoff location");
        carRentalBookingPageWeb.setPickUpLocation(pickup);
        carRentalBookingPageWeb.setSetDropOffLocation(dropoff);

        step("Verify location on booking form");
        Assertion.assertThat("Verify pickup location", carRentalBookingPageWeb.getPickupLocation(), is(org.hamcrest.Matchers.notNullValue()));
        Assertion.assertThat("Verify dropoff location", carRentalBookingPageWeb.getDropoffLocation(), is(org.hamcrest.Matchers.notNullValue()));

        step("User proceed to booking form");
        carRentalBookingPageWeb.continueBookFormMWEB();

        step("Verify car model");
        Assertion.assertThat("Car model should be the same", carModel, is(carRentalBookingPageWeb.getCarModel()));

        step("Verify vendor name");
        Assertion.assertThat("Vendor name should be the same", vendorName, is(carRentalBookingPageWeb.getVendorName()));
        
        step("Verify driver type");
        if (type.equalsIgnoreCase("self-drive")) {
            type = "Self drive";
        } else {
            type = "With driver";
        }
        Assertion.assertThat("Verify driver type",
                carRentalBookingPageWeb.getDriverType(), is(type));
    }

    @Author(names = {"Naufal Hafiz", "Clement"})
    @Module(name = "ProductDetailPage")
    @Tribe(name = "CarRental")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyCheckDetailBookingFormWithoutDriverGuestWeb() {
        Map<String, String> param = getParam("testVerifyCheckDetailBookingFormWithoutDriverGuestWeb");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        CarRentalSearchPageWeb<?> carRentalSearchPage = CarRentalSearchPageWeb.getInstance(session);
        CarRentalSearchResultPageWeb<?> carRentalSearchResultPage = CarRentalSearchResultPageWeb.getInstance(session);
        CarRentalReviewBookingPageWeb<?> carRentalReviewBookingPage = CarRentalReviewBookingPageWeb.getInstance(session);
        CarRentalBookingPageWeb<?> carRentalBookingPageWeb = CarRentalBookingPageWeb.getInstance(session);

        String type = param.get("type");
        String location = param.get("location");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);
        String duration = param.get("duration");
        String specialRequest = param.get("specialRequest");
        String pickup = param.get("pickup");
        String dropoff = param.get("dropoff");

        step("User open home page");
        homePage.goToHomePage();

        step("User open home page as logged in user");
        loginPage.loginAsGuest();

        step("User open car rental vertical");
        homePage.openVerticalPageViaURL("sewa-mobil");

        step("User click skip button");
        carRentalSearchPage.clickSkipButton();

        step("User select driver type");
        carRentalSearchPage.selectDriverType(type);

        step("User select car rental location");
        carRentalSearchPage.selectCarRentalLocationBySearch(location);

        step("User select car rental dates");
        carRentalSearchPage.selectDate(departureDate);

        step("User select car rental duration");
        carRentalSearchPage.selectDuration(duration);

        step("User search car rental");
        carRentalSearchPage.searchCarRental();

        step("Extract needed data");
        carRentalSearchResultPage.openFirstCarRentalCard();
        String carModel = carRentalSearchResultPage.getCarModel();
        String vendorName = carRentalSearchResultPage.getVendorName();
        carRentalSearchResultPage.closeFirstCarRentalCard();

        step("User select car");
        carRentalSearchResultPage.selectFirstCarRentalCard();

        step("User navigate to booking page");
        carRentalReviewBookingPage.proceedToBookingPage();

        step("Add special request");
        carRentalBookingPageWeb.addSpecialRequest(specialRequest);

        step("Get special request");
        String specialRequestText = carRentalBookingPageWeb.getSpecialRequest();

        step("Verify special request");
        Assertion.assertThat("Special request should be the same", specialRequest, is(specialRequestText));

        step("Verify booking form text");
        Assertion.assertThat("Pickup & Dropoff Location should exists", carRentalBookingPageWeb.isBookingTextExists(), is(true));

        step("Set pickup & dropoff location");
        carRentalBookingPageWeb.setPickUpLocation(pickup);
        carRentalBookingPageWeb.setSetDropOffLocation(dropoff);

        step("Verify location on booking form");
        Assertion.assertThat("Verify pickup location", carRentalBookingPageWeb.getPickupLocation(), is(org.hamcrest.Matchers.notNullValue()));
        Assertion.assertThat("Verify dropoff location", carRentalBookingPageWeb.getDropoffLocation(), is(org.hamcrest.Matchers.notNullValue()));

        step("User proceed to booking form");
        carRentalBookingPageWeb.continueBookFormMWEB();

        step("Verify car model");
        Assertion.assertThat("Car model should be the same", carModel, is(carRentalBookingPageWeb.getCarModel()));

        step("Verify vendor name");
        Assertion.assertThat("Vendor name should be the same", vendorName, is(carRentalBookingPageWeb.getVendorName()));

        step("Verify driver type");
        if (type.equalsIgnoreCase("self-drive")) {
            type = "Self drive";
        } else {
            type = "With driver";
        }
        Assertion.assertThat("Verify driver type",
                carRentalBookingPageWeb.getDriverType(), is(type));
    }

    @Author(names = {"Akshat" , "Clement"})
    @Module(name = "SRP")
    @Tribe(name = "CarRental")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyFiltersPageWithoutDriverGuestWeb() {
        Map<String, String> param = getParam("testVerifyFiltersPageWithoutDriverGuestWeb");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePageWeb<?> homePageWeb = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        CarRentalSearchPageWeb<?> carRentalSearchPage = CarRentalSearchPageWeb.getInstance(session);
        CarRentalSearchResultPageWeb<?> carRentalSearchResultPage = CarRentalSearchResultPageWeb.getInstance(session);

        String driverType = param.get("driverType");
        String location = param.get("location");
        String duration = param.get("duration");

        step("User open home page");
        homePageWeb.goToHomePage();

        step("User open as guest");
        loginPage.loginAsGuest();

        step("User open car rental vertical");
        homePageWeb.openVerticalPageViaURL("sewa-mobil");

        step("User click skip button");
        carRentalSearchPage.clickSkipButton();

        step("User select driver type");
        carRentalSearchPage.selectDriverType(driverType);

        step("User select rental location by recommendation");
        carRentalSearchPage.selectLocationByRecommendation(location);

        step("User select rental duration");
        carRentalSearchPage.selectDuration(duration);

        step("User search car rental");
        carRentalSearchPage.searchCarRental();

        step("User click on filter and sort options");
        carRentalSearchResultPage.clickFilterBtn();

        step("User select Transmission filter");
        carRentalSearchResultPage.clickTransmissionFilter();

        step("User selects Automatic transmission type");
        carRentalSearchResultPage.selectAMT();

        step("User saves the filter");
        carRentalSearchResultPage.clickSave();

        step("Check whether filter appears or not");
        Assertion.assertThat("Verify filter appears or not", carRentalSearchResultPage.isElementDisplayingText(), is(true));

        step("Checks whether there is any car with those filters or not");
        Assertion.assertThat("Checking for the list of cars", carRentalSearchResultPage.checkList(), is(true));

        step("User verifies whether the filter is same or not");
        Assertion.assertThat("Transmission type is Automatic or not", carRentalSearchResultPage.getTransmissionType(), is("Automatic"));
    }

    @Author(names = {"Akshat", "Clement"})
    @Module(name = "SRP")
    @Tribe(name = "CarRental")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyFiltersPageWithoutDriverLoginWeb() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testVerifyFiltersPageWithoutDriverLoginWeb");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        CarRentalSearchPageWeb<?> carRentalSearchPage = CarRentalSearchPageWeb.getInstance(session);
        CarRentalSearchResultPageWeb<?> carRentalSearchResultPage = CarRentalSearchResultPageWeb.getInstance(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));

        String driverType = param.get("driverType");
        String location = param.get("location");
        String duration = param.get("duration");

        step("User open home page");
        homePage.goToHomePage();

        step("User open home page as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User open car rental vertical");
        homePage.openVerticalPageViaURL("sewa-mobil");

        step("User click skip button");
        carRentalSearchPage.clickSkipButton();

        step("User select driver type");
        carRentalSearchPage.selectDriverType(driverType);

        step("User select rental location by recommendation");
        carRentalSearchPage.selectLocationByRecommendation(location);

        step("User select rental duration");
        carRentalSearchPage.selectDuration(duration);

        step("User search car rental");
        carRentalSearchPage.searchCarRental();

        step("User click on filter and sort options");
        carRentalSearchResultPage.clickFilterBtn();

        step("User select Transmission filter");
        carRentalSearchResultPage.clickTransmissionFilter();

        step("User selects Automatic");
        carRentalSearchResultPage.selectAMT();

        step("User saves the filter");
        carRentalSearchResultPage.clickSave();

        step("Check whether filter appears or not");
        Assertion.assertThat("Verify filter appears or not", carRentalSearchResultPage.isElementDisplayingText(), is(true));

        step("Checks whether there is any car with those filters or not");
        Assertion.assertThat("Checking for the list of cars", carRentalSearchResultPage.checkList(), is(true));

        step("User verifies whether the filter is same or not");
        Assertion.assertThat("Same filter or not", carRentalSearchResultPage.getTransmissionType(), is("Automatic"));
    }

    @Author(names = {"Naufal Hafiz", "Clement"})
    @Module(name = "SearchResultPage")
    @Tribe(name = "CarRental")
    @Vertical(name = "NFT")
    @Test
    public void testCarRentalEditLocationOnSRPLoginWeb() throws ParseException, FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testCarRentalEditLocationOnSRPLoginWeb");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String driverType = param.get("driverType");
        String location = param.get("location");
        String newLocation = param.get("newLocation");
        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));

        String currentDate = CoreFunctions.getCurrentLongDate();
        String newDate = CoreFunctions.addToLongDate(currentDate, 3);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        CarRentalSearchPageWeb<?> carRentalSearchPageWeb = CarRentalSearchPageWeb.getInstance(session);
        CarRentalSearchResultPageWeb<?> carRentalSearchResultPageWeb = CarRentalSearchResultPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open home page as logged in user");
        loginPage.loginIfNotWithSameEmail(username, password, otp, retryCounter);

        step("User open car rental vertical");
        homePage.openVerticalPageViaURL("sewa-mobil");

        step("User click skip button");
        carRentalSearchPageWeb.clickSkipButton();

        step("User select driver type");
        carRentalSearchPageWeb.selectDriverType(driverType);

        step("User select rental location by search");
        carRentalSearchPageWeb.selectCarRentalLocationBySearch(location);

        step("Extract form data");
        String locationValue = carRentalSearchPageWeb.getFormValue("location", "SP");
        String dateValue = carRentalSearchPageWeb.getFormValue("date", "SP");

        step("User search car rental");
        carRentalSearchPageWeb.searchCarRental();

        String srpDateValue = carRentalSearchPageWeb.getFormValue("date", "SRP");
        String normalizedSrpDateValue = srpDateValue.replaceFirst("^0+(\\d)", "$1");

        step("Assertion before edit - Location");
        Assertion.assertThat("Verify location on SRP is match on search page", carRentalSearchPageWeb.getFormValue("location", "SRP"), is(locationValue));
        step("Assertion before edit - Date");
        Assertion.assertThat("Verify date on SRP is match on search page", normalizedSrpDateValue, containsString(dateValue));
        step("Assertion before edit - Driver Type");
        Assertion.assertThat("Verify driver option is match on search page",
                carRentalSearchPageWeb.getFormValue("driver", "SRP"), either(is("With Driver")).or(is("Dengan Sopir")));

        step("First edit search result on SRP");
        carRentalSearchResultPageWeb.editSearchResult(newLocation, newDate);

        srpDateValue = carRentalSearchPageWeb.getFormValue("date", "SRP");
        srpDateValue = srpDateValue.replaceFirst("^0+(\\d)", "$1");

        step("Assertion after first edit - Location");
        Assertion.assertThat("Verify edited location is match after edit", carRentalSearchPageWeb.getFormValue("location", "SRP"), is(newLocation));
        step("Assertion after first edit - Date");
        Assertion.assertThat("Verify edited date is match after edit", srpDateValue,
            either(containsString(CoreFunctions.convertFullDateToShortDate(newDate).replaceFirst("^0+(\\d)", "$1")))
            .or(containsString(CoreFunctions.convertFullDateToShortDateIndonesian(newDate).replaceFirst("^0+(\\d)", "$1")))
        );
        step("Assertion after first edit - Driver Type");
        Assertion.assertThat("Verify driver option after edit",
                carRentalSearchPageWeb.getFormValue("driver", "SRP"), either(is("With Driver")).or(is("Dengan Sopir")));

        step("Second edit search result on SRP");
        carRentalSearchResultPageWeb.changeDriverType("self-drive", location, newDate);

        step("Assertion after second edit - Location");
        Assertion.assertThat("Verify edited location is match after edit", carRentalSearchPageWeb.getFormValue("location", "SRP"), is(location));
        step("Assertion after second edit - Driver Type");
        Assertion.assertThat("Verify driver option after edit",
                carRentalSearchPageWeb.getFormValue("driver", "SRP"), either(is("Self Drive")).or(is("Lepas Kunci")));
    }

    @Author(names = "Akshat")
    @Module(name = "SRP")
    @Tribe(name = "CarRental")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyVendorFilterWithDriverGuestWeb() {
        Map<String, String> param = getParam("testVerifyVendorFilterWithDriverGuestWeb");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePageWeb<?> homePageWeb = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        CarRentalSearchPageWeb<?> carRentalSearchPage = CarRentalSearchPageWeb.getInstance(session);
        CarRentalSearchResultPageWeb<?> carRentalSearchResultPage = CarRentalSearchResultPageWeb.getInstance(session);

        String currency = param.get("currency");
        String driverType = param.get("driverType");
        String location = param.get("location");
        String duration = param.get("duration");

        step("User open home page");
        homePageWeb.goToHomePage();

        step("User open as guest");
        loginPage.loginAsGuest();

        step("User select IDR currency");
        homePageWeb.selectCurrency(currency);

        step("User open car rental vertical");
        homePageWeb.openVerticalPageViaURL("sewa-mobil");

        step("User click skip button");
        carRentalSearchPage.clickSkipButton();

        step("User select driver type");
        carRentalSearchPage.selectDriverType(driverType);

        step("User select rental location by recommendation");
        carRentalSearchPage.selectLocationByRecommendation(location);

        step("User select rental duration");
        carRentalSearchPage.selectDuration(duration);

        step("User search car rental");
        carRentalSearchPage.searchCarRental();

        step("User click on filter and sort options");
        carRentalSearchResultPage.clickFilterBtn();

        step("User select Vendors filter");
        carRentalSearchResultPage.clickVendorFilter();

        step("User selects vendor");
        String expected_vendor = carRentalSearchResultPage.selectVendor();

        step("User saves the filter");
        carRentalSearchResultPage.clickSave();

        step("Check whether filter appears or not");
        Assertion.assertThat("Verify filter appears or not", carRentalSearchResultPage.isElementDisplayingText(), is(true));

        step("Checks whether there is any car with those filters or not");
        Assertion.assertThat("Checking for the list of cars", carRentalSearchResultPage.checkList(), is(true));

        step("User click on first car with filter");
        carRentalSearchResultPage.selectFirstCarRentalCardForVendor();

        step("User verifies whether the filtered car is same as of selected vendor or not");
        Assertion.assertThat("Same filter or not", carRentalSearchResultPage.checkNameOfVendor(), is(expected_vendor));

    }

    @Author(names = "Akshat")
    @Module(name = "SRP")
    @Tribe(name = "CarRental")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyVendorFilterWithDriverLoginWeb() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testVerifyVendorFilterWithDriverLoginWeb");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePageWeb<?> homePageWeb = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        CarRentalSearchPageWeb<?> carRentalSearchPage = CarRentalSearchPageWeb.getInstance(session);
        CarRentalSearchResultPageWeb<?> carRentalSearchResultPage = CarRentalSearchResultPageWeb.getInstance(session);

        String currency = param.get("currency");
        String driverType = param.get("driverType");
        String location = param.get("location");
        String duration = param.get("duration");
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));

        step("User open home page");
        homePageWeb.goToHomePage();

        step("User open home page as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User select IDR currency");
        homePageWeb.selectCurrency(currency);

        step("User open car rental vertical");
        homePageWeb.openVerticalPageViaURL("sewa-mobil");

        step("User click skip button");
        carRentalSearchPage.clickSkipButton();

        step("User select driver type");
        carRentalSearchPage.selectDriverType(driverType);

        step("User select rental location by recommendation");
        carRentalSearchPage.selectLocationByRecommendation(location);

        step("User select rental duration");
        carRentalSearchPage.selectDuration(duration);

        step("User search car rental");
        carRentalSearchPage.searchCarRental();

        step("User click on filter and sort options");
        carRentalSearchResultPage.clickFilterBtn();

        step("User select Vendors filter");
        carRentalSearchResultPage.clickVendorFilter();

        step("User selects vendor");
        String expected_vendor = carRentalSearchResultPage.selectVendor();

        step("User saves the filter");
        carRentalSearchResultPage.clickSave();

        step("Check whether filter appears or not");
        Assertion.assertThat("Verify filter appears or not", carRentalSearchResultPage.isElementDisplayingText(), is(true));

        step("Checks whether there is any car with those filters or not");
        Assertion.assertThat("Checking for the list of cars", carRentalSearchResultPage.checkList(), is(true));

        step("User click on first car with filter");
        carRentalSearchResultPage.selectFirstCarRentalCardForVendor();

        step("User verifies whether the filtered car is same as of selected vendor or not");
        Assertion.assertThat("Same filter or not", carRentalSearchResultPage.checkNameOfVendor(), is(expected_vendor));
    }

    @Author(names = "Vipul Kumar")
    @Module(name = "ProductDetailPage")
    @Tribe(name = "CarRental")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyDetailsOnPDPPageWithDriverGuestWeb() {
        Map<String, String> param = getParam("testVerifyDetailsOnPDPPageWithDriverGuestWeb");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String currency = param.get("currency");
        String driverType = param.get("driverType");
        String location = param.get("location");
        String duration = param.get("duration");
        String carRentalPackage = param.get("carRentalPackage");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        CarRentalSearchPageWeb<?> carRentalSearchPage = CarRentalSearchPageWeb.getInstance(session);
        CarRentalSearchResultPageWeb<?> carRentalSearchResultPage = CarRentalSearchResultPageWeb.getInstance(session);
        CarRentalReviewBookingPageWeb<?> carRentalReviewBookingPage = CarRentalReviewBookingPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as guest");
        loginPage.loginAsGuest();

        step("User select non-IDR currency");
        homePage.selectCurrency(currency);

        step("User open car rental vertical via deeplink");
        homePage.openVerticalPageViaURL("sewa-mobil");

        step("User click skip button");
        carRentalSearchPage.clickSkipButton();

        step("User select driver type");
        carRentalSearchPage.selectDriverType(driverType);

        step("User select rental location by recommendation");
        carRentalSearchPage.selectLocationByRecommendation(location);

        step("User select rental duration");
        carRentalSearchPage.selectDuration(duration);

        step("User search car rental");
        carRentalSearchPage.searchCarRental();

        step("User select first car rental");
        String carName = carRentalSearchResultPage.getFirstCarName();
        carRentalSearchResultPage.selectFirstCarRentalCard();

        step("User Go To PDP of With Driver");
        Assertion.assertThat("Verify car details and name are displayed correctly on the review page", carRentalReviewBookingPage.verifyReviewPageIsDisplayed(carName), is(true));

        if (Configs.PLATFORM.equals(Platform.AMWEB) || Configs.PLATFORM.equals(Platform.IMWEB)) {
            step("User navigate to rental area page");
            carRentalReviewBookingPage.navigateToRentalAreaPage(carRentalPackage);
        }
        step("Verify that rental coverage area With location which you search for is displayed");
        Assertion.assertThat("Verify rental area section and location name are displayed correctly on the review page for With Driver", carRentalReviewBookingPage.verifyReviewPageIsDisplayedForWithDriver(location), is(true));
        Assertion.assertThat("Rental area is displayed", carRentalReviewBookingPage.isRentalCoverageAreaTitleDisplayed(), is(true));
        Assertion.assertThat("Map of coverage area is displayed", carRentalReviewBookingPage.isCoverageAreaMapDisplayed(), is(true));
        Assertion.assertThat("List of coverage areas is displayed", carRentalReviewBookingPage.isListOfCoverageAreaDisplayed(), is(true));
    }

    @Author(names = "Vipul Kumar")
    @Module(name = "ProductDetailPage")
    @Tribe(name = "CarRental")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyDetailsOnPDPPageWithDriverLoginWeb() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testVerifyDetailsOnPDPPageWithDriverLoginWeb");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));

        String currency = param.get("currency");
        String driverType = param.get("driverType");
        String location = param.get("location");
        String duration = param.get("duration");
        String carRentalPackage = param.get("carRentalPackage");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        CarRentalSearchPageWeb<?> carRentalSearchPage = CarRentalSearchPageWeb.getInstance(session);
        CarRentalSearchResultPageWeb<?> carRentalSearchResultPage = CarRentalSearchResultPageWeb.getInstance(session);
        CarRentalReviewBookingPageWeb<?> carRentalReviewBookingPage = CarRentalReviewBookingPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open home page as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User select non-IDR currency");
        homePage.selectCurrency(currency);

        step("User open car rental vertical via deeplink");
        homePage.openVerticalPageViaURL("sewa-mobil");

        step("User click skip button");
        carRentalSearchPage.clickSkipButton();

        step("User select driver type");
        carRentalSearchPage.selectDriverType(driverType);

        step("User select rental location by recommendation");
        carRentalSearchPage.selectLocationByRecommendation(location);

        step("User select rental duration");
        carRentalSearchPage.selectDuration(duration);

        step("User search car rental");
        carRentalSearchPage.searchCarRental();

        step("User select first car rental");
        String carName = carRentalSearchResultPage.getFirstCarName();
        carRentalSearchResultPage.selectFirstCarRentalCard();

        step("User Go To PDP of With Driver");
        Assertion.assertThat("Verify car details and name are displayed correctly on the review page", carRentalReviewBookingPage.verifyReviewPageIsDisplayed(carName), is(true));

        if (Configs.PLATFORM.equals(Platform.AMWEB) || Configs.PLATFORM.equals(Platform.IMWEB)) {
            step("User navigate to rental area page");
            carRentalReviewBookingPage.navigateToRentalAreaPage(carRentalPackage);
        }
        step("Verify that rental coverage area With location which you search for is displayed");
        Assertion.assertThat("Verify rental area section and location name are displayed correctly on the review page for With Driver", carRentalReviewBookingPage.verifyReviewPageIsDisplayedForWithDriver(location), is(true));
        Assertion.assertThat("Rental area is displayed", carRentalReviewBookingPage.isRentalCoverageAreaTitleDisplayed(), is(true));
        Assertion.assertThat("Map of coverage area is displayed", carRentalReviewBookingPage.isCoverageAreaMapDisplayed(), is(true));
        Assertion.assertThat("List of coverage areas is displayed", carRentalReviewBookingPage.isListOfCoverageAreaDisplayed(), is(true));
    }

    @Author(names = {"Akshat", "Clement"})
    @Module(name = "BookingPage")
    @Tribe(name = "CarRental")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyDetailsOnBookingPageWithDriverLoginWeb() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testVerifyDetailsOnBookingPageWithDriverLoginWeb");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));

        String driverType = param.get("driverType");
        String location = param.get("location");
        String duration = param.get("duration");
        String carRentalPackage = param.get("carRentalPackage");
        String changedPickUpLocation = param.get("changedPickUpLocation");
        String changedDropOffLocation = param.get("changedDropOffLocation");
        String request = param.get("request");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        CarRentalSearchPageWeb<?> carRentalSearchPage = CarRentalSearchPageWeb.getInstance(session);
        CarRentalSearchResultPageWeb<?> carRentalSearchResultPage = CarRentalSearchResultPageWeb.getInstance(session);
        CarRentalReviewBookingPageWeb<?> carRentalReviewBookingPage = CarRentalReviewBookingPageWeb.getInstance(session);
        CarRentalBookingPageWeb<?> carRentalBookingPageWeb = CarRentalBookingPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open home page as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User open car rental vertical via deeplink");
        homePage.openVerticalPageViaURL("sewa-mobil");

        step("User click skip button");
        carRentalSearchPage.clickSkipButton();

        step("User select driver type");
        carRentalSearchPage.selectDriverType(driverType);

        step("User select rental location by recommendation");
        carRentalSearchPage.selectCarRentalLocationBySearch(location);

        step("User select rental duration");
        carRentalSearchPage.selectDuration(duration);

        step("User search car rental");
        carRentalSearchPage.searchCarRental();

        if (Configs.PLATFORM.equals(Platform.AMWEB) || Configs.PLATFORM.equals(Platform.IMWEB)) {
            step("User re-select correct duration using url manipulation");
            carRentalSearchResultPage.selectDurationUsingDeeplink(duration);
        }

        step("User select first car rental");
        String carPriceOnSRP = carRentalSearchResultPage.getPriceOnSRP();
        String carName = carRentalSearchResultPage.getFirstCarName();
        carRentalSearchResultPage.selectFirstCarRentalCard();

        step("User Go To PDP of With Driver");
        Assertion.assertThat("Verify car details and name are displayed correctly on the review page", carRentalReviewBookingPage.verifyReviewPageIsDisplayed(carName), is(true));

        step("User selects the package type");
        carRentalReviewBookingPage.selectPackageType(carRentalPackage);

        step("User gets the price of the package");
        String packagePrice = carRentalReviewBookingPage.getSelectedPackagePrice(carRentalPackage);

        step("User proceeds to booking page");
        carRentalReviewBookingPage.proceedToBookingPage();

        step("User changes the pickup location and get the additional price");
        List<String> pickUpAddOns = carRentalBookingPageWeb.getPickUpAreaAndPrice(changedPickUpLocation);

        step("User verifies the pick-up area");
        Assertion.assertThat("Verifies pickUp area", carRentalBookingPageWeb.verifiesPickUpArea(), containsString(pickUpAddOns.get(1)));

        step("User changes the drop-off location and get the additional price");
        List<String> dropOffAddOns = carRentalBookingPageWeb.getDropOffAreaAndPrice(changedDropOffLocation);

        step("User verifies the drop-off area");
        Assertion.assertThat("Verifies drop-off area", carRentalBookingPageWeb.verifiesDropOffArea(), containsString(dropOffAddOns.get(1)));

        step("User adds some special requests");
        carRentalBookingPageWeb.addSpecialRequest(request);

        step("User clicks on price details button");
        carRentalBookingPageWeb.clickPriceDetails();

        step("User verifies all the details of booking");
        Assertion.assertThat("Verifies the price of car", carRentalBookingPageWeb.getPriceOfCar(duration), is(carPriceOnSRP));
        Assertion.assertThat("Verifies the package selected of car", carRentalBookingPageWeb.getPackagePrice(carRentalPackage, duration, packagePrice), is(true));
        Assertion.assertThat("Verifies the pick-up additional charges", pickUpAddOns.get(0), containsString(carRentalBookingPageWeb.checkPickUpAddOn(pickUpAddOns.get(1))));
        Assertion.assertThat("Verifies the drop-off additional charges", dropOffAddOns.get(0), containsString(carRentalBookingPageWeb.checkDropOffAddOn(dropOffAddOns.get(1))));
    }

    @Author(names = {"Akshat", "Clement"})
    @Module(name = "BookingPage")
    @Tribe(name = "CarRental")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyDetailsOnBookingPageWithDriverGuestUserWeb() {
        Map<String, String> param = getParam("testVerifyDetailsOnBookingPageWithDriverGuestUserWeb");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String driverType = param.get("driverType");
        String location = param.get("location");
        String duration = param.get("duration");
        String carRentalPackage = param.get("carRentalPackage");
        String changedPickUpLocation = param.get("changedPickUpLocation");
        String changedDropOffLocation = param.get("changedDropOffLocation");
        String request = param.get("request");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        CarRentalSearchPageWeb<?> carRentalSearchPage = CarRentalSearchPageWeb.getInstance(session);
        CarRentalSearchResultPageWeb<?> carRentalSearchResultPage = CarRentalSearchResultPageWeb.getInstance(session);
        CarRentalReviewBookingPageWeb<?> carRentalReviewBookingPage = CarRentalReviewBookingPageWeb.getInstance(session);
        CarRentalBookingPageWeb<?> carRentalBookingPageWeb = CarRentalBookingPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open home page as guest user");
        loginPage.loginAsGuest();

        step("User open car rental vertical via deeplink");
        homePage.openVerticalPageViaURL("sewa-mobil");

        step("User click skip button");
        carRentalSearchPage.clickSkipButton();

        step("User select driver type");
        carRentalSearchPage.selectDriverType(driverType);

        step("User select rental location by recommendation");
        carRentalSearchPage.selectLocationByRecommendation(location);

        step("User select rental duration");
        carRentalSearchPage.selectDuration(duration);

        step("User search car rental");
        carRentalSearchPage.searchCarRental();

        if ( Configs.PLATFORM.equals(Platform.AMWEB) || Configs.PLATFORM.equals(Platform.IMWEB)) {
            step("User re-select correct duration using url manipulation");
            carRentalSearchResultPage.selectDurationUsingDeeplink(duration);
        }

        step("User select first car rental");
        String carPriceOnSRP = carRentalSearchResultPage.getPriceOnSRP();
        String carName = carRentalSearchResultPage.getFirstCarName();
        carRentalSearchResultPage.selectFirstCarRentalCard();

        step("User Go To PDP of With Driver");
        Assertion.assertThat("Verify car details and name are displayed correctly on the review page", carRentalReviewBookingPage.verifyReviewPageIsDisplayed(carName), is(true));

        step("User selects the package type");
        carRentalReviewBookingPage.selectPackageType(carRentalPackage);

        step("User gets the price of the package");
        String packagePrice = carRentalReviewBookingPage.getSelectedPackagePrice(carRentalPackage);

        step("User proceeds to booking page");
        carRentalReviewBookingPage.proceedToBookingPage();

        step("User changes the pickup location and get the additional price");
        List<String> pickUpAddOns = carRentalBookingPageWeb.getPickUpAreaAndPrice(changedPickUpLocation);

        step("User verifies the pick-up area");
        Assertion.assertThat("Verifies pickUp area", carRentalBookingPageWeb.verifiesPickUpArea(), containsString(pickUpAddOns.get(1)));

        step("User changes the drop-off location and get the additional price");
        List<String> dropOffAddOns = carRentalBookingPageWeb.getDropOffAreaAndPrice(changedDropOffLocation);

        step("User verifies the drop-off area");
        Assertion.assertThat("Verifies drop-off area", carRentalBookingPageWeb.verifiesDropOffArea(), containsString(dropOffAddOns.get(1)));

        step("User adds some special requests");
        carRentalBookingPageWeb.addSpecialRequest(request);

        step("User clicks on price details button");
        carRentalBookingPageWeb.clickPriceDetails();

        step("User verifies all the details of booking");
        Assertion.assertThat("Verifies the price of car", carRentalBookingPageWeb.getPriceOfCar(duration), is(carPriceOnSRP));
        Assertion.assertThat("Verifies the package selected of car", carRentalBookingPageWeb.getPackagePrice(carRentalPackage, duration, packagePrice), is(true));
        Assertion.assertThat("Verifies the pick-up additional charges", pickUpAddOns.get(0), containsString(carRentalBookingPageWeb.checkPickUpAddOn(pickUpAddOns.get(1))));
        Assertion.assertThat("Verifies the drop-off additional charges", dropOffAddOns.get(0), containsString(carRentalBookingPageWeb.checkDropOffAddOn(dropOffAddOns.get(1))));
    }
}