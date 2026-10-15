package com.tiket.test.platform.sphinx.payment;

import com.tiket.apiPage.CorporateApi;
import com.tiket.apiPage.HotelApi;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.apiPage.CarTransferApi;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Environment;
import com.tiket.model.Platform;
import com.tiket.model.Session;
import com.tiket.page.accommodation.hotel.bookingformpage.BookingFormPage;
import com.tiket.page.accommodation.hotel.detailpage.DetailPage;
import com.tiket.page.accommodation.hotel.landingpage.LandingPage;
import com.tiket.page.accommodation.hotel.roomdetailpage.RoomDetailPage;
import com.tiket.page.accommodation.hotel.roomlistpage.RoomListPage;
import com.tiket.page.accommodation.hotel.searchresultpage.SearchResultPage;
import com.tiket.page.account.AccountPage;
import com.tiket.page.carrental.CarRentalPage;
import com.tiket.page.carrental.bookingpage.CarRentalBookingPage;
import com.tiket.page.carrental.reviewbookingpage.CarRentalReviewBookingPage;
import com.tiket.page.carrental.searchpage.SearchPageCarRental;
import com.tiket.page.carrental.searchresultpage.SearchResultPageCarRental;
import com.tiket.page.cartransfer.CarTransferPage;
import com.tiket.page.cartransfer.bookingform.CarTransferBookingPage;
import com.tiket.page.cartransfer.searchpage.CarTransferSearchPage;
import com.tiket.page.cartransfer.searchresultpage.CarTransferSearchResultPage;
import com.tiket.page.common.error_msg.ErrorMessagePage;
import com.tiket.page.common.webview.WebViewPage;
import com.tiket.page.corporate.login.CorporateLoginPage;
import com.tiket.page.home.HomePage;
import com.tiket.page.login.LoginPage;
import com.tiket.page.order.orderdetail.OrderDetailPage;
import com.tiket.page.order.orderlist.OrderListPage;
import com.tiket.page.payment.PaymentMethodPage;
import com.tiket.page.payment.PaymentPage;
import com.tiket.page.ttd.landingpage.TTDLandingPage;
import com.tiket.page.ttd.searchpage.TTDSearchPage;
import com.tiket.page.ttd.searchresultpage.TTDSearchResultPage;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.testbase.Configs;
import com.tiket.web.page.ttd.TtdBookingFormPage.TtdBookingFormPageWeb;
import com.tiket.web.page.ttd.TtdProductDetailPage.TtdProductDetailPageWeb;
import com.tiket.web.page.ttd.TtdSelectedPackageDetailPage.TtdSelectedPackageDetailPageWeb;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static com.tiket.core.CoreFunctions.*;
import static com.tiket.testdata.TestData.getObjectParams;
import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;

public class PaymentTest extends BaseTest {
    @Author(names = {"Ashish","Shubham Bansal"})
    @Module(name = "PaymentMethod")
    @Tribe(name = "Payment")
    @Vertical(name = "PRO")
    @Test(dataProvider = "testCheckOvoAsPaymentMethodGuestDataProvider")
    public void testCheckOvoAsPaymentMethodGuest(Map<String, String> param) {
        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        TTDSearchResultPage<WebDriver> ttdSearchResultPage = TTDSearchResultPage.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPage = TtdProductDetailPageWeb.getInstance(session);
        TtdSelectedPackageDetailPageWeb<?> ttdSelectedPackageDetailPage = TtdSelectedPackageDetailPageWeb.getInstance(session);
        TtdBookingFormPageWeb<?> ttdBookingFormPage = TtdBookingFormPageWeb.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);

        BaseTest.paramsThreadLocal.set(param);

        String email = param.get("email");
        String password = param.get("password");
        String paymentType = param.get("paymentType");
        String phoneNumber = param.get("phoneNumber");
        String ovoPin = param.get("ovoPin");
        String product = param.get("product");
        int ticketQty = Integer.parseInt(param.get("ticketQty"));

        step("User open app");
        homePage.gotoHome();

        step("Logout via deeplink");
        homePage.logoutViaDeeplink();

        step("Change the currency to IDR");
        homePage.changeCurrency("IDR", email);

        step("Open PDP page via deeplink");
        ttdSearchResultPage.openPdpDeeplink(product);
        webViewPage.switchToWebViewUrl("to-do");

        step("Select ticket");
        ttdProductDetailPage.goToPackagesList();
        ttdProductDetailPage.chooseTicketWithMorePackages(1);

        step("Select ticket quantity");
        ttdSelectedPackageDetailPage.setTicketQty(ticketQty, 0);

        step("Submit package detail");
        ttdSelectedPackageDetailPage.clickBookButton();
        webViewPage.switchToNative();

        step("Completing booking form data");
        webViewPage.switchToWebViewUrl("to-do");
        ttdBookingFormPage.fillContactOrderDetail(false, email, phoneNumber);
        ttdBookingFormPage.fillGuestDetail();

        step("Submit booking");
        ttdBookingFormPage.continueToPaymentPage();
        webViewPage.switchToNative();

        step("user continue to payment list page");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",!orderID.isEmpty(),is(true));

        step("Open payment method");
        paymentPage.openPaymentMethod();

        step("Select e-wallet as payment method");
        paymentPage.verifyInactivePaymentMethod(paymentType);

        step("login from payment page");
        paymentPage.openLoginPage();
        loginPage.loginFromDifferentPages(email,password);
        Assertion.assertThat("No login label shown", !paymentPage.isLoginContainerDisplayed(), is(true));

        step("Open payment method");
        paymentPage.openPaymentMethod();

        if (Configs.ENVIRONMENT == Environment.PROD) {
            Assertion.assertThat("ovo as payment is available ", paymentPage.verifyPaymentMethodExistence(paymentType), is(true));
        } else {
            step("Select payment method");
            paymentPage.selectPaymentMethod(paymentType);

            step("Assert ovo pay is chosen as payment method");
            Assertion.assertThat("ovo as payment is chosen ", paymentPage.isCorrectPaymentMethodSelected(paymentType), is(true));

            step("Submit payment");
            paymentPage.clickPayButton();

            step("Fill ovo pin");
            paymentPage.fillOvoPin(ovoPin);

            step("verify OVO Payment Successful");
            Assertion.assertThat("Verify payment is successful", paymentPage.isThankYouPageShown(), is(true));
        }
    }

    @Author(names = {"Okta"})
    @Module(name = "PaymentMethod")
    @Tribe(name = "Payment")
    @Vertical(name = "PRO")
    @Test(dataProvider = "testCheckPayLaterAsPaymentMethodLoginDataProvider")
    public void testCheckPayLaterAsPaymentMethodLogin(Map<String, String> param) {
        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        CarTransferSearchPage<WebDriver> carTransferSearchPage = CarTransferSearchPage.getInstance(session);
        CarTransferSearchResultPage<WebDriver> carTransferSearchResultPage = CarTransferSearchResultPage.getInstance(session);
        CarTransferBookingPage<WebDriver> carTransferBookingPage = CarTransferBookingPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);

        BaseTest.paramsThreadLocal.set(param);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");

        String airport = param.get("airport");
        String destination = param.get("destination");
        String paymentType = param.get("paymentType");

        step("Open app");
        homePage.gotoHome();

        step("Login as guest");
        loginPage.loginAsGuest();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Set currency to IDR");
        homePage.changeCurrency("IDR",username);

        step("Open car transfer vertical");
        homePage.openVerticalPageDeeplink("Car Transfers","","IDR");
        webViewPage.switchToWebViewUrl("airport-transfer");
        carTransferSearchPage.closePopups();

        step("Choose car transfer in");
        carTransferSearchPage.chooseTransferType("transfer_in");

        step("Input origin and destination");
        carTransferSearchPage.choosePickup(airport, destination);

        step("Search car");
        carTransferSearchPage.searchCar();

        if(Configs.PLATFORM == Platform.IOS){
            webViewPage.switchToNative();
            carTransferSearchPage.allowLocationAccess();
            webViewPage.switchToWebViewUrl("airport-transfer");
        }

        step("Select car and vendor");
        carTransferSearchResultPage.selectCarAndVendor();

        step("Continue to booking page");
        carTransferSearchResultPage.continueToBookingPage();

        step("Continue to payment page");
        carTransferBookingPage.continueToPayment();
        webViewPage.switchToNative();

        step("Open payment method");
        paymentPage.openPaymentMethod();

        step("Open No Card Installment pop up");
        paymentPage.selectPaymentMethod(paymentType);

        step("Get PayLater limit on pop up");
        String payLaterLimitPopup = paymentPage.getPayLaterLimitPopup();

        step("Select PayLater payment method");
        paymentPage.selectPayLater();

        step("Get PayLater limit on payment page");
        String payLaterLimitPaymentPage = paymentPage.getPayLaterLimitWebview();

        step("Submit payment");
        paymentPage.clickPayButton();

        step("Assert PayLater limit on pop up is the same on payment page");
        Assertion.assertThat("PayLater limit on pop up is the same on payment page ", payLaterLimitPopup, is(payLaterLimitPaymentPage));

        step("Assert OTP pop up is displayed after submit payment");
        Assertion.assertThat("OTP pop up is displayed after submit payment ", paymentPage.checkOtp(), is(true));
    }

    @Author(names = {"Soumya"})
    @Module(name = "PaymentMethod")
    @Tribe(name = "Payment")
    @Vertical(name = "PRO")
    @Test(dataProvider = "testCheckBTPLAsPaymentMethodLoginDataProvider")
    public void testCheckBTPLAsPaymentMethodLogin(Map<String, String> param) {
        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        CarTransferSearchPage<WebDriver> carTransferSearchPage = CarTransferSearchPage.getInstance(session);
        CarTransferSearchResultPage<WebDriver> carTransferSearchResultPage = CarTransferSearchResultPage.getInstance(session);
        CarTransferBookingPage<WebDriver> carTransferBookingPage = CarTransferBookingPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);

        BaseTest.paramsThreadLocal.set(param);

        String username = param.get("username");
        String password = param.get("password");

        String airport = param.get("airport");
        String destination = param.get("destination");

        String promoCode = param.get("promoCode");
        String cicilanPromoCode = param.get("cicilanPromoCode");

        step("Open app");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink and change currency to IDR");
        homePage.loginWithSpecificAccountViaDeeplink(username, password, "IDR");

        step("Open car transfer vertical");
        homePage.openVerticalPageDeeplink("Car Transfers","","IDR");
        webViewPage.switchToWebViewUrl("airport-transfer");
        carTransferSearchPage.closePopups();

        step("Allow location access if prompted");
        webViewPage.switchToNative();
        carTransferSearchPage.allowLocationAccess();
        webViewPage.switchToWebViewUrl("airport-transfer");

        step("Choose car transfer in");
        carTransferSearchPage.chooseTransferType("transfer_in");

        step("Input origin and destination");
        carTransferSearchPage.choosePickups("transfer_in", airport, destination);

        step("Search car");
        carTransferSearchPage.searchCar();

        if (Configs.PLATFORM == Platform.IOS) {
            webViewPage.switchToNative();
            carTransferSearchPage.allowLocationAccess();
            webViewPage.switchToWebViewUrl("airport-transfer");
        }

        step("Select car and vendor");
        carTransferSearchResultPage.selectCarAndVendor();

        step("Continue to booking page");
        carTransferSearchResultPage.continueToBookingPage();

        step("Continue to payment page");
        carTransferBookingPage.continueToPayment();
        webViewPage.switchToNative();

        step("Open payment method");
        paymentPage.openPaymentMethod();

        step("Select BTPL payment method from Payment group");
        paymentPage.selectPaymentMethod("BTPL");

        step("Assert BTPL is chosen as payment method");
        Assertion.assertThat("BTPL is chosen as payment method", paymentPage.isCorrectPaymentMethodSelected("Blibli Tiket PayLater"), is(true));

        step("Verify promo savings banner is NOT shown when no promo is applied");
        Assertion.assertThat("No promo savings banner is shown without a promo", paymentPage.isBtplPromoBannerNotShown(), is(true));

        step("Click on instalment (Cicilan) selection");
        paymentPage.openInstallmentSelection();

        step("Verify tenor options are shown on installment selection UI");
        Assertion.assertThat("Installment tenor options are shown", paymentPage.hasInstallmentTenorOptions(), is(true));

        step("Verify 'Bunga 0%' label is NOT shown without a 0%-interest promo (PLAT-56785 regression)");
        Assertion.assertThat("No tenor incorrectly shows 'Bunga 0%' label by default", paymentPage.verifyNoInstallmentTenorShowsZeroInterestLabel(), is(true));

        step("Select a tenor");
        paymentPage.selectInstallmentTenor(1);
        paymentPage.closeInstallmentSelection();

        step("Verify installment detail reflects the selected tenor");
        Assertion.assertThat("Installment detail is displayed after selecting tenor", paymentPage.getInstallmentDetail(), not(isEmptyOrNullString()));

        step("Apply BTPL promo");
        paymentPage.seeAllPromoCodes();
        paymentPage.applyPromoCode(promoCode);
        Assertion.assertThat("BTPL promo savings banner is shown after applying promo", paymentPage.isBtplPromoApplied(), is(true));

        step("Click on instalment and verify whole UI after applying promo");
        paymentPage.openInstallmentSelection();
        Assertion.assertThat("Installment tenor options are shown after promo applied", paymentPage.hasInstallmentTenorOptions(), is(true));
        Assertion.assertThat("No tenor incorrectly shows 'Bunga 0%' label with regular BTPL promo", paymentPage.verifyNoInstallmentTenorShowsZeroInterestLabel(), is(true));
        paymentPage.selectInstallmentTenor(1);
        paymentPage.closeInstallmentSelection();

        step("Remove BTPL promo");
        paymentPage.unApplyBtplPromoCode();
        Assertion.assertThat("No promo savings banner is shown after removing the promo", paymentPage.isBtplPromoBannerNotShown(), is(true));

        step("Apply BTPLCICILAN promo");
        paymentPage.seeAllPromoCodes();
        paymentPage.applyPromoCode(cicilanPromoCode);

        step("Click on instalment and verify only one tenor shows Interest 0% after applying BTPLCICILAN promo");
        paymentPage.openInstallmentSelection();
        Assertion.assertThat("Installment tenor options are shown with BTPLCICILAN promo", paymentPage.hasInstallmentTenorOptions(), is(true));
        Assertion.assertThat("Only the zero-fee tenor shows 'Bunga 0%' label with BTPLCICILAN promo", paymentPage.verifyExactlyOneInstallmentTenorShowsZeroInterestLabel(), is(true));
        paymentPage.selectZeroInterestInstallmentTenor();
        paymentPage.closeInstallmentSelection();

        step("Verify promo savings banner is shown after selecting the 0% tenor");
        Assertion.assertThat("BTPL promo savings banner is shown after selecting the 0% tenor with BTPLCICILAN promo", paymentPage.isBtplPromoApplied(), is(true));

        step("Click Pay now");
        paymentPage.clickPayButton();

        step("Allow camera access if OS permission dialog appears");
        paymentPage.allowCameraAccessIfPresent();

        step("Verify redirection to Indodana/BTPL page");
        Assertion.assertThat("User is redirected to Indodana/BTPL payment page", paymentPage.verifyBtplRedirect(), is(true));
    }

    @Author(names = {"Zia","Kartik"})
    @Module(name = "PaymentMethod")
    @Tribe(name = "Payment")
    @Vertical(name = "PRO")
    @Test(dataProvider = "testCheckCCAsPaymentMethodLoginDataProvider")
    public void testCheckCCAsPaymentMethodLogin(Map<String, String> map) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(map);
    
        String currency = map.get("currency");

        String loginMethod = map.get("loginType");
        String username = map.get("username");
        String phoneNumber = map.get("phoneNumber");
        String password = map.get("password");

        String driverOption = map.get("driverOption");
        String location = map.get("location");
        String startDate = generateRandomDate(map.get("rangeAddDate"));
        String type = map.get("type");

        String pickupTestData = map.get("pickupTime");
        StringBuilder pickup = new StringBuilder(pickupTestData);
        pickup.setCharAt(2, ':');
        String pickupTime = pickup.toString();

        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String fullName = randomName() + " Autorder";
        String nameTitle = getRandomObject(salutationList);

        String cardNumber = map.get("cardNumber");
        String expiry = map.get("expiry");
        String cvv = map.get("cvv");
        String cardName = map.get("cardName");

        int rentalDuration = Integer.parseInt(map.get("rentalDuration"));

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        CarRentalPage<WebDriver> carRentalPage = CarRentalPage.getInstance(session);

        step("Create car rental order with waiting payment order for 1 passenger and "+rentalDuration+" day via API");
        Boolean orderResult = carRentalPage.createProductTillPaymentApi(
                username, phoneNumber, password, driverOption, location,
                startDate, pickupTime, type, fullName, nameTitle,rentalDuration);
        Assertion.assertThat("Car rental booking via API is successful", orderResult, is(true));
        String issuedOrderID = carRentalPage.getOrderId();

        step("user open app");
        homePage.gotoHome();

        step("user login as guest");
        loginPage.loginAsGuest();

        step("user login using "+username+" account");
        loginPage.loginWith(loginMethod, username, password);

        step("open payment page");
        paymentPage.openWaitingPaymentPage(issuedOrderID);
        String orderId = paymentPage.checkOrderID();
        Assertion.assertThat("is open correct payment order",orderId.contains(issuedOrderID),is(true));

        step("Choose payment method and pay with credit card");
        paymentPage.openPaymentMethod();
        paymentPage.selectPaymentMethod("Credit Card");
        paymentPage.fillCardDetailPayment(cardNumber, expiry, cvv, cardName);

        step("verify credit card detail");
        Assertion.assertThat("Credit card service charge is correct ", paymentPage.verifyCCdetails() , is(true));
    }

    @DataProvider(name = "testCheckCCAsPaymentMethodLoginDataProvider")
    public Object[][] testCheckCCAsPaymentMethodLoginDataProvider() {
        return getObjectParams("testCheckCCAsPaymentMethodLogin");
    }

    @Author(names = {"Dendi"})
    @Module(name = "PaymentMethod")
    @Tribe(name = "Payment")
    @Vertical(name = "PRO")
    @Test(dataProvider = "testCCAsPaymentMethodWithoutInstallmentGuestDataProvider")
    public void testCCAsPaymentMethodWithoutInstallmentGuest(Map<String, String> map) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(map);

        String Location = map.get("location");
        String pickupLocation = map.get("pickupLocation");
        String carRentalDuration = map.get("carRentalDuration");
        String carRentalPackage = map.get("carRentalPackage");
        String dateRental = CoreFunctions.getCurrentLongDate();
        String username =  map.get("username");

        String cardNumber = map.get("cardNumber");
        String expiry = map.get("expiry");
        String cvv = map.get("cvv");
        String cardName = map.get("cardName");

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
         SearchPageCarRental<WebDriver> carRentalSearchPage = SearchPageCarRental.getInstance(session);
        SearchResultPageCarRental<WebDriver> carRentalSearchResultPage = SearchResultPageCarRental.getInstance(session);
        CarRentalReviewBookingPage<WebDriver> carRentalReviewBookingPage = CarRentalReviewBookingPage.getInstance(session);
        CarRentalBookingPage<WebDriver> carRentalBookingPage = CarRentalBookingPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginAsGuest();

        step("Change currency");
        homePage.changeCurrency("IDR","");

        step("Open Car Landing Page");
        homePage.openVerticalPageDeeplink("Car Rentals","","IDR");
        carRentalSearchPage.closeInformationPopUp();

        step("Select car rental type");
        carRentalSearchPage.chooseDriver("with_driver");

        step("Select car rental date");
        carRentalSearchPage.selectDate(CoreFunctions.addToLongDate(dateRental,30));

        step("Input car rental location");
        carRentalSearchPage.chooseLocation(Location);

        step("Searh car rental");
        carRentalSearchPage.searchCar();

        step("choose car");
        carRentalSearchResultPage.chooseCar();

        step("continue to booking form from review page");
        carRentalReviewBookingPage.continueFromReviewDetailCar();

        step("pick up location");
        carRentalBookingPage.choosePickUpLocation(pickupLocation);

        step("continue to complete booking");
        carRentalBookingPage.continueToCompleteBookingPage();

        step("input contact detail");
        carRentalBookingPage.fillGuestDetailOrder(username,"81"+CoreFunctions.randomInt(9));

        step("continue to payment");
        carRentalBookingPage.setContinueToPayment();

        step("get order id");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",!orderID.isEmpty(),is(true));

        step("Choose payment method and pay with credit card");
        paymentPage.openPaymentMethod();
        paymentPage.selectPaymentMethod("Credit Card");
        paymentPage.fillCardDetailPayment(cardNumber, expiry, cvv, cardName);

        step("Click pay button");
        paymentPage.clickPayButtonCC();
        paymentPage.verifyCreditCard3ds();
        Boolean isPaymentSuccessful = paymentPage.isPaymentSuccessful();

        step("Verify Step");
        Assertion.assertThat("Asserting if the payment is successful ", isPaymentSuccessful, is(true));
    }

    @DataProvider(name = "testCCAsPaymentMethodWithoutInstallmentGuestDataProvider")
    public Object[][] testCCAsPaymentMethodWithoutInstallmentGuestDataProvider() {
        return getObjectParams("testCCAsPaymentMethodWithoutInstallmentGuest");
    }

    @Author(names = {"Ashish"})
    @Module(name = "PaymentMethod")
    @Tribe(name = "Payment")
    @Vertical(name = "PRO")
    @Test(dataProvider = "testCheckTIXPointAsDiscountLoginDataProvider")
    public void testCheckTIXPointAsDiscountLogin(Map<String, String> param) {
        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        CarTransferSearchPage<WebDriver> carTransferSearchPage = CarTransferSearchPage.getInstance(session);
        CarTransferSearchResultPage<WebDriver> carTransferSearchResultPage = CarTransferSearchResultPage.getInstance(session);
        CarTransferBookingPage<WebDriver> carTransferBookingPage = CarTransferBookingPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);

        BaseTest.paramsThreadLocal.set(param);

        String airport = param.get("airport");
        String destination = param.get("destination");
        String username = param.get("username");
        String password = param.get("password");
        String paymentType = param.get("paymentType");
        String loginType = param.get("loginType");
        String applyDiscount = param.get("applyDiscount");

        step("User open app");
        homePage.gotoHome();

        step("Login as guest");
        loginPage.loginAsGuest();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Set currency to IDR");
        homePage.changeCurrency("IDR",username);

        step("User open car transfer vertical page");
        homePage.openVerticalPageDeeplink("Car Transfers","","");
        webViewPage.switchToWebViewUrl("airport-transfer");
        carTransferSearchPage.closePopups();

        step("User choose car transfer in");
        carTransferSearchPage.chooseTransferType("transfer_in");

        step("user input origin and destination");
        carTransferSearchPage.choosePickup(airport, destination);

        step("user search for car");
        carTransferSearchPage.searchCar();

        if(Configs.PLATFORM == Platform.IOS){
            webViewPage.switchToNative();
            carTransferSearchPage.allowLocationAccess();
            webViewPage.switchToWebViewUrl("airport-transfer");
        }

        step("user select car and vendor");
        carTransferSearchResultPage.selectCarAndVendor();

        step("user continue to booking page");
        carTransferSearchResultPage.continueToBookingPage();

        step("user continue to payment list page");
        carTransferBookingPage.continueToPayment();
        webViewPage.switchToNative();

        step("Open payment method");
        paymentPage.openPaymentMethod();

        step("Select e-wallet as payment method");
        paymentPage.selectPaymentMethod(paymentType);

        step("Get tiket point");
        String tiketPoint = paymentPage.getAndToogleTiketPoint();

        step("open price detail page");
        paymentPage.openPriceDetail();

        step("price breakdown");
        Assertion.assertThat("is price breakdown correct: ", paymentPage.priceBreakDown(tiketPoint, applyDiscount), is(true));

        step("get total price");
        String totalPrice = paymentPage.getTotalPricePayment();

        step("Submit payment");
        paymentPage.clickPayButton();

        step("Verify Total amount on 2nd page");
        Assertion.assertThat("total price on 1st page is equal total price on 2nd page: ", paymentPage.verifyTotalAmountSecondPage(totalPrice), is(true));
    }

    @DataProvider(name = "testCheckOvoAsPaymentMethodGuestDataProvider")
    public Object[][] testCheckOvoAsPaymentMethodGuestDataProvider() {
        return getObjectParams("testCheckOvoAsPaymentMethodGuest");
    }

    @DataProvider(name = "testCheckGopayAsPaymentMethodGuestDataProvider")
    public Object[][] testCheckGopayAsPaymentMethodGuestDataProvider() {
        return getObjectParams("testCheckGopayAsPaymentMethodGuest");
    }

    @DataProvider(name = "testCheckPayLaterAsPaymentMethodLoginDataProvider")
    public Object[][] testCheckPayLaterAsPaymentMethodLoginDataProvider() {
        return getObjectParams("testCheckPayLaterAsPaymentMethodLogin");
    }

    @DataProvider(name = "testCheckBTPLAsPaymentMethodLoginDataProvider")
    public Object[][] testCheckBTPLAsPaymentMethodLoginDataProvider() {
        return getObjectParams("testCheckBTPLAsPaymentMethodLogin");
    }

    @DataProvider(name = "testCheckTIXPointAsDiscountLoginDataProvider")
    public Object[][] testCheckTIXPointAsDiscountLoginDataProvider() {
        return getObjectParams("testCheckTIXPointAsDiscountLogin");
    }

    @Author(names = {"Kartik"})
    @Module(name = "PaymentMethod")
    @Tribe(name = "Payment")
    @Vertical(name = "PRO")
    @Test(dataProvider = "testCheckpaymentWithPromoLoginDataProvider")
    public void testPaymentWithPromoLogin(Map<String, String> param) {
        Session<WebDriver> session = sessionThreadLocal.get();

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        CarTransferSearchPage<WebDriver> carTransferSearchPage = CarTransferSearchPage.getInstance(session);
        CarTransferSearchResultPage<WebDriver> carTransferSearchResultPage = CarTransferSearchResultPage.getInstance(session);
        CarTransferBookingPage<WebDriver> carTransferBookingPage = CarTransferBookingPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);

        BaseTest.paramsThreadLocal.set(param);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String airport = param.get("airport");
        String destination = param.get("destination");
        String promoCode = param.get("promoCode");
        String applyDiscount = param.get("applyDiscount");
        String paymentType = param.get("paymentType");

        step("Open app");
        homePage.gotoHome();

        step("Login as guest");
        loginPage.loginAsGuest();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Set currency to IDR");
        homePage.changeCurrency("IDR",username);

        step("Open car transfer vertical");
        homePage.openVerticalPageDeeplink("Car Transfers","","IDR");
        webViewPage.switchToWebViewUrl("airport-transfer");
        carTransferSearchPage.closePopups();

        step("Choose car transfer in");
        carTransferSearchPage.chooseTransferType("transfer_in");

        step("Input origin and destination");
        carTransferSearchPage.choosePickup(airport, destination);

        step("Search car");
        carTransferSearchPage.searchCar();

        if(Configs.PLATFORM == Platform.IOS){
            webViewPage.switchToNative();
            carTransferSearchPage.allowLocationAccess();
            webViewPage.switchToWebViewUrl("airport-transfer");
        }

        step("Select car and vendor");
        carTransferSearchResultPage.selectCarAndVendor();

        step("Continue to booking page");
        carTransferSearchResultPage.continueToBookingPage();

        step("Continue to payment page");
        carTransferBookingPage.continueToPayment();
        webViewPage.switchToNative();

        step("Open payment method");
        paymentPage.openPaymentMethod();

        step("Select e-wallet as payment method");
        paymentPage.selectPaymentMethod(paymentType);

        step("User Apply promocode ");
        paymentPage.seeAllPromoCodes();
        paymentPage.applyPromoCode(promoCode);
        String appliedPromoCode = paymentPage.getPromo();
        String promoAmount = paymentPage.getPriceFromPromo();

        step("open price detail page");
        paymentPage.openPriceDetail();

        step("price breakdown");
        boolean isBreakdown = paymentPage.priceBreakDown(promoAmount, applyDiscount);

        step("get total price");
        String totalPrice = paymentPage.getTotalPricePayment();

        step("Submit payment");
        paymentPage.clickPayButton();

        step("Verify Total amount on 2nd page");
        Assertion.assertThat("Compare Promocode and applied promocode on paymentPage  ", appliedPromoCode.equals(promoCode), is(true));
        Assertion.assertThat("is price breakdown correct: ", isBreakdown, is(true));
        Assertion.assertThat("total price on 1st page is equal total price on 2nd page: ", paymentPage.verifyTotalAmountSecondPage(totalPrice), is(true));
    }

    @DataProvider(name = "testCheckpaymentWithPromoLoginDataProvider")
    public Object[][] testCheckpaymentWithPromoLoginDataProvider() {
        return getObjectParams("testPaymentWithPromoLogin");
    }

    @Author(names = {"Budi"})
    @Module(name = "PaymentMethod")
    @Tribe(name = "Payment")
    @Vertical(name = "PRO")
    @Test(dataProvider = "testCheckBCAVAAsPaymentMethodLoginDataProvider")
    public void testCheckBCAVAAsPaymentMethodLogin(Map<String, String> param) {
        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        CarTransferSearchPage<WebDriver> carTransferSearchPage = CarTransferSearchPage.getInstance(session);
        CarTransferSearchResultPage<WebDriver> carTransferSearchResultPage = CarTransferSearchResultPage.getInstance(session);
        CarTransferBookingPage<WebDriver> carTransferBookingPage = CarTransferBookingPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);

        BaseTest.paramsThreadLocal.set(param);

        String airport = param.get("airport");
        String destination = param.get("destination");
        String paymentType = param.get("paymentType");
        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");

        step("User open app");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("User open car transfer vertical page");
        homePage.openVerticalPageDeeplink("Car Transfers","","");
        webViewPage.switchToWebViewUrl("airport-transfer");
        carTransferSearchPage.closePopups();

        step("User choose car transfer in");
        carTransferSearchPage.chooseTransferType("transfer_in");

        step("user input origin and destination");
        carTransferSearchPage.choosePickup(airport, destination);

        step("user search for car");
        carTransferSearchPage.searchCar();

        if(Configs.PLATFORM == Platform.IOS){
            webViewPage.switchToNative();
            carTransferSearchPage.allowLocationAccess();
            webViewPage.switchToWebViewUrl("airport-transfer");
        }

        step("user select car and vendor");
        carTransferSearchResultPage.selectCarAndVendor();

        step("user continue to booking page");
        carTransferSearchResultPage.continueToBookingPage();

        step("user continue to payment list page");
        carTransferBookingPage.continueToPayment();
        webViewPage.switchToNative();

        step("Open payment method");
        paymentPage.openPaymentMethod();

        step("Select Virtual Account as payment method");
        paymentPage.selectPaymentMethod(paymentType);

        step("Assert BCA Virtual Account is chosen as payment method");
        Assertion.assertThat("BCA Virtual Account as payment is chosen ", paymentPage.isCorrectPaymentMethodSelected(paymentType), is(true));

        step("open price detail page");
        paymentPage.openPriceDetail();

        step("get total price");
        String totalPrice = paymentPage.getTotalPricePayment();

        paymentPage.closePriceDetail();

        step("Submit payment");
        paymentPage.clickPayButton();

        step("Assert BCA Virtual Account is chosen as payment method");
        Assertion.assertThat("BCA Virtual Account as payment is chosen ", paymentPage.isCorrectPaymentMethodChosen(paymentType), is(true));
        Assertion.assertThat("total price on 1st page is equal total price on 2nd page: ", paymentPage.verifyTotalAmountSecondPage(totalPrice), is(true));
        Assertion.assertThat("Number va is visible", paymentPage.isVaNumberShow(), is(true));
    }

    @DataProvider(name = "testCheckBCAVAAsPaymentMethodLoginDataProvider")
    public Object[][] testCheckBCAVAAsPaymentMethodLoginDataProvider() {
        return getObjectParams("testCheckBCAVAAsPaymentMethodLogin");
    }

    @Author(names = {"Budi"})
    @Module(name = "PaymentMethod")
    @Tribe(name = "Payment")
    @Vertical(name = "PRO")
    @Test(dataProvider = "testCheckOVOAsPaymentMethodLoginDataProvider")
    public void testCheckOVOAsPaymentMethodLogin(Map<String, String> param) {
        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        CarTransferSearchPage<WebDriver> carTransferSearchPage = CarTransferSearchPage.getInstance(session);
        CarTransferSearchResultPage<WebDriver> carTransferSearchResultPage = CarTransferSearchResultPage.getInstance(session);
        CarTransferBookingPage<WebDriver> carTransferBookingPage = CarTransferBookingPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);

        BaseTest.paramsThreadLocal.set(param);

        String airport = param.get("airport");
        String destination = param.get("destination");
        String paymentType = param.get("paymentType");
        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");

        step("User open app");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("User open car transfer vertical page");
        homePage.openVerticalPageDeeplink("Car Transfers","","");
        webViewPage.switchToWebViewUrl("airport-transfer");
        carTransferSearchPage.closePopups();

        step("User choose car transfer in");
        carTransferSearchPage.chooseTransferType("transfer_in");

        step("user input origin and destination");
        carTransferSearchPage.choosePickup(airport, destination);

        step("user search for car");
        carTransferSearchPage.searchCar();

        if(Configs.PLATFORM == Platform.IOS){
            webViewPage.switchToNative();
            carTransferSearchPage.allowLocationAccess();
            webViewPage.switchToWebViewUrl("airport-transfer");
        }

        step("user select car and vendor");
        carTransferSearchResultPage.selectCarAndVendor();

        step("user continue to booking page");
        carTransferSearchResultPage.continueToBookingPage();

        step("user continue to payment list page");
        carTransferBookingPage.continueToPayment();
        webViewPage.switchToNative();

        step("Open payment method");
        paymentPage.openPaymentMethod();

        step("Select OVO as payment method");
        paymentPage.selectPaymentMethod(paymentType);

        step("Add ovo account");
        paymentPage.addOVOAccount();
        Assertion.assertThat("Linkage ovo is visible", paymentPage.verifyLinkageOvo(), is(true));
    }

    @DataProvider(name = "testCheckOVOAsPaymentMethodLoginDataProvider")
    public Object[][] testCheckOVOAsPaymentMethodLoginDataProvider() {
        return getObjectParams("testCheckOVOAsPaymentMethodLogin");
    }

    @Author(names = {"Naufal"})
    @Module(name = "PaymentMethod")
    @Tribe(name = "Payment")
    @Vertical(name = "PRO")
    @Test
    public void checkCreditCardPaymentLogin() {
        Map<String, String> map = getParam("checkCreditCardPaymentLogin");
        BaseTest.paramsThreadLocal.set(map);

        Session<WebDriver> session = sessionThreadLocal.get();

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");

        String location = map.get("location");
        String payment_method = map.get("paymentMethod");

        String cardNumber = map.get("cardNumber");
        String expiry = map.get("expiry");
        String cvv = map.get("cvv");
        String cardName = map.get("cardName");

        step("Goto home");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Open hotel landing page");
        homePage.openVerticalPageDeeplink("Hotel","","");

        step("Search stay location");
        landingPage.searchStayLocation(location);

        step("Submit search");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        step("Go to the room list");
        String hotelName = detailPage.getHotelNameDetail();
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();

        step("Select room from the room list");
        roomListPage.clickBookRoom();

        step("Submit booking");
        bookingFormPage.continueToPaymentPage();
        bookingFormPage.PriceChangesPopUp();

        step("Choose payment method and pay with credit card");
        String expectedOrderId = paymentPage.checkOrderID();
        paymentPage.openPaymentMethod();
        paymentPage.selectPaymentMethod(payment_method);
        paymentPage.fillCCDetailRealIssued(cardNumber, expiry, cvv, cardName);

        paymentPage.clickPayButton();
        paymentPage.verifyCreditCard3ds();

        step("Open the order list and go to order detail");
        paymentPage.seeOrderList();
        orderListPage.openOrderDetail(hotelName);
        orderListPage.removePopUp();

        step("Assertion");
        Assertion.assertThat("order id is match", expectedOrderId.split(" ")[3], is(orderDetailPage.getOrderID()));
    }

    @Author(names = {"Kartik"})
    @Module(name = "PaymentMethod")
    @Tribe(name = "Payment")
    @Vertical(name = "PRO")
    @Test(dataProvider = "testCheckGiftVoucherPaymentMethodDataProvider")
    public void testCheckGiftVoucherPaymentMethod(Map<String, String> param) {
        Session<WebDriver> session = sessionThreadLocal.get();

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        CarTransferSearchPage<WebDriver> carTransferSearchPage = CarTransferSearchPage.getInstance(session);
        CarTransferSearchResultPage<WebDriver> carTransferSearchResultPage = CarTransferSearchResultPage.getInstance(session);
        CarTransferBookingPage<WebDriver> carTransferBookingPage = CarTransferBookingPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);

        BaseTest.paramsThreadLocal.set(param);

        String airport = param.get("airport");
        String destination = param.get("destination");
        String email = param.get("email");
        String phoneNumber = param.get("phoneNumber");
        String promoCode = param.get("promoCode");
        String applyDiscount = param.get("applyDiscount");
        String paymentType = param.get("paymentType");
        String cardNumber = param.get("cardNumber");
        String expiry = param.get("expiry");
        String cvv = param.get("cvv");
        String cardName = param.get("cardName");

        step("Open app");
        homePage.gotoHome();

        step("User login as guest");
        loginPage.loginAsGuest();

        step("Set currency to IDR");
        homePage.changeCurrency("IDR","");

        step("User open car transfer vertical page");
        homePage.openVerticalPageDeeplink("Car Transfers","","IDR");
        webViewPage.switchToWebViewUrl("airport-transfer");
        carTransferSearchPage.closePopups();

        step("User choose car transfer in");
        carTransferSearchPage.chooseTransferType("transfer_in");

        step("user input origin and destination");
        carTransferSearchPage.choosePickup(airport,destination );

        step("user search for car");
        carTransferSearchPage.searchCar();

        if(Configs.PLATFORM == Platform.IOS){
            webViewPage.switchToNative();
            carTransferSearchPage.allowLocationAccess();
            webViewPage.switchToWebViewUrl("airport-transfer");
        }

        step("user select car and vendor");
        carTransferSearchResultPage.selectCarAndVendor();

        step("user continue to booking page");
        carTransferSearchResultPage.continueToBookingPage();

        step("user fill contact detail in booking page");
        carTransferBookingPage.fillDetailOrder(email,phoneNumber);

        step("Continue to payment page");
        carTransferBookingPage.continueToPayment();
        webViewPage.switchToNative();

        step("User Apply Gift Voucher");
        paymentPage.seeAllPromoCodes();
        paymentPage.applyPromoCode(promoCode);
        String promoAmount = paymentPage.getPriceFromPromo();

        step("Open payment method");
        paymentPage.openPaymentMethod();

        step("Select Credit Card as payment method");
        paymentPage.selectPaymentMethod(paymentType);

        step("open price detail page");
        paymentPage.openPriceDetail();
        boolean priceBreakDownCorrect =paymentPage.priceBreakDown(promoAmount, "Gift voucher");
        paymentPage.clickPayButton();

        step("price breakdown");
        Assertion.assertThat("is price breakdown correct: ", priceBreakDownCorrect, is(true));
        Assertion.assertThat("Payment Success with Gift Voucher ", paymentPage.isThankYouPageShown(), is(true));
    }

    @Author(names = {"Kartik"})
    @Module(name = "PaymentMethod")
    @Tribe(name = "Payment")
    @Vertical(name = "PRO")
    @Test
    public void checkBCABankTransfer() {
        Map<String, String> map = getParam("checkBCABankTransfer");
        BaseTest.paramsThreadLocal.set(map);
        Session<WebDriver> session = sessionThreadLocal.get();

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");
        String location = map.get("location");
        String date_type = map.get("date_type");
        String date = map.get("date");

        step("Goto home");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);
        String language = homePage.getLanguageApp();


        step("Open hotel landing page");
        homePage.openVerticalPageDeeplink("Hotel",language,"");

        step("Search stay location");
        landingPage.searchStayLocation(location);

        step("set stay date");
        landingPage.setStayDate(date_type, date);

        step("Submit search");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        step("Go to the room list");
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();

        step("Select room from the room list");
        roomListPage.clickBookRoom();

        step("Submit booking");
        bookingFormPage.continueToPaymentPage();
        bookingFormPage.PriceChangesPopUp();

        step("Open payment method");
        paymentPage.openPaymentMethod();

        step("Select Kredivo as payment method");
        paymentPage.selectPaymentMethod("BCA Transfer");

        step("open price detail page");
        paymentPage.openPriceDetail();
        boolean priceBreakDown = paymentPage.priceBreakDown();
        boolean uniqueCodeVisible = paymentPage.checkUniqueCodePriceDetail();

        step("User click on pay button");
        paymentPage.clickPayButton();
        Assertion.assertThat("Is Account number clipboard shown",  paymentPage.checkAccountNumberSnackbar(), is(true));
        Assertion.assertThat("Is price text clipboard number shown",  paymentPage.checkPriceNumberSnackbar(), is(true));
        Assertion.assertThat("Is Price BreakDown Correct", priceBreakDown, is(true));
        Assertion.assertThat("is Unique code visible inside price Details",uniqueCodeVisible, is(true));
    }

    @Author(names = {"Kartik"})
    @Module(name = "PaymentMethod")
    @Tribe(name = "Payment")
    @Vertical(name = "PRO")
    @Test
    public void checkDebitCardPaymentLogin() {
        Map<String, String> map = getParam("checkDebitCardPaymentLogin");
        BaseTest.paramsThreadLocal.set(map);
        Session<WebDriver> session = sessionThreadLocal.get();

        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);


        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");

        String location = map.get("location");
        String paymentType = map.get("paymentType");
        String cardNumber = map.get("cardNumber");
        String expiry = map.get("expiry");
        String cvv = map.get("cvv");
        String cardName = map.get("cardName");

        step("Goto home");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Set currency to IDR");
        homePage.changeCurrency("IDR",username);

        step("Open hotel landing page");
        homePage.openVerticalPageDeeplink("Hotel","","IDR");

        step("Search stay location");
        landingPage.searchStayLocation(location);

        step("Submit search");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        step("Go to the room list");
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();

        step("Select room from the room list");
        roomListPage.clickBookRoom();

        step("Submit booking");
        bookingFormPage.continueToPaymentPage();
        bookingFormPage.PriceChangesPopUp();

        step("open price detail page");
        paymentPage.openPriceDetail();
        boolean priceBreakDown =  paymentPage.priceBreakDown();

        step("Open payment method");
        paymentPage.openPaymentMethod();
        paymentPage.selectPaymentMethod(paymentType);
        paymentPage.fillCCDetailRealIssued(cardNumber, expiry, cvv, cardName);

        step("Pay using Debit card");
        paymentPage.clickPayButton();
        paymentPage.verifyCreditCard3ds();
        Assertion.assertThat("Payment Success with Debit card ", paymentPage.isThankYouPageShown(), is(true));
        Assertion.assertThat("Is Price BreakDown Correct", priceBreakDown, is(true));
    }
    @DataProvider(name = "testCheckGiftVoucherPaymentMethodDataProvider")
    public Object[][] testCheckGiftVoucherPaymentMethodDataProvider() {
        return getObjectParams("testCheckGiftVoucherPaymentMethod");
    }

    @Author(names = {"Akbar"})
    @Module(name = "MultiCurrency")
    @Tribe(name = "Payment")
    @Vertical(name = "PRO")
    @Test
    public void testMCCheckCCPaymentSuccessGuest() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testMCCheckCCPaymentSuccessGuest");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);

        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);
        TtdSelectedPackageDetailPageWeb<?> ttdSelectedPackageDetailPageWeb = TtdSelectedPackageDetailPageWeb.getInstance(session);
        TtdBookingFormPageWeb<?> ttdBookingFormPageWeb = TtdBookingFormPageWeb.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);

        String product = param.get("product");
        String currency = param.get("currency");
        String cardNumber = param.get("cardNumber");
        String expiry = param.get("expiry");
        String cvv = param.get("cvv");
        String cardName = param.get("cardName");

        BaseTest.paramsThreadLocal.set(param);

        step("Go to home page");
        homePage.gotoHome();

        step("Make user as guest");
        loginPage.loginAsGuest();

        step("Set currency to "+ currency);
        homePage.changeCurrency(currency,"");

        step("Open TTD landing page");
        homePage.openVerticalPageDeeplink("To Do","",currency);

        step("Search the product");
        ttdLandingPage.clickSearchBar();
        ttdSearchPage.inputSearchKeyword(product);

        step("select the activity");
        ttdSearchPage.selectRecommendedActivity(product);

        step("Select ticket");
        webViewPage.switchToWebViewUrl("to-do");
        ttdProductDetailPageWeb.goToPackagesList();
        ttdProductDetailPageWeb.chooseTicket(1);

        step("Set total ticket");
        ttdSelectedPackageDetailPageWeb.fillAvailableSection();
        ttdSelectedPackageDetailPageWeb.setTicketQty(1, 0);

        step("Submit package detail");
        ttdSelectedPackageDetailPageWeb.clickBookButton();
        webViewPage.switchToNative();

        step("Completing booking form data");
        webViewPage.switchToWebViewUrl("to-do");
        ttdBookingFormPageWeb.fillContactOrderDetail(false);
        ttdBookingFormPageWeb.fillGuestDetail();

        step("Submit booking");
        ttdBookingFormPageWeb.continueToPaymentPage();
        ttdBookingFormPageWeb.checkPriceNotifChange();

        step("Open payment method");
        paymentPage.openPriceDetail();
        Boolean isPriceCorrect = paymentPage.priceBreakDown();

        step("Fill CC Info");
        paymentPage.fillCardDetailPayment(cardNumber, expiry, cvv, cardName);

        step("Click pay button");
        paymentPage.clickPayButton();
        paymentPage.verifyCreditCard3ds();
        Boolean isPaymentSuccessful = paymentPage.isPaymentSuccessful();

        step("Verify Step");
        Assertion.assertThat("Asserting if the payment is successful ", isPaymentSuccessful, is(true));
        Assertion.assertThat("Asserting if the price detail is correct ", isPriceCorrect, is(true));
    }

    @Author(names = {"Akbar"})
    @Module(name = "MultiCurrency")
    @Tribe(name = "Payment")
    @Vertical(name = "PRO")
    @Test
    public void testMCCheckDebitCardPaymentSuccessGuest() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testMCCheckDebitCardPaymentSuccessGuest");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);

        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);
        TtdSelectedPackageDetailPageWeb<?> ttdSelectedPackageDetailPageWeb = TtdSelectedPackageDetailPageWeb.getInstance(session);
        TtdBookingFormPageWeb<?> ttdBookingFormPageWeb = TtdBookingFormPageWeb.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);

        String product = param.get("product");
        String currency = param.get("currency");
        String cardNumber = param.get("cardNumber");
        String expiry = param.get("expiry");
        String cvv = param.get("cvv");
        String cardName = param.get("cardName");

        BaseTest.paramsThreadLocal.set(param);

        step("Go to home page");
        homePage.gotoHome();

        step("Make user as guest");
        loginPage.loginAsGuest();

        step("Set currency to "+ currency);
        homePage.changeCurrency(currency,"");

        step("Open TTD landing page");
        homePage.openVerticalPageDeeplink("To Do","",currency);

        step("Search the product");
        ttdLandingPage.clickSearchBar();
        ttdSearchPage.inputSearchKeyword(product);

        step("select the activity");
        ttdSearchPage.selectRecommendedActivity(product);

        step("Select ticket");
        webViewPage.switchToWebViewUrl("to-do");
        ttdProductDetailPageWeb.goToPackagesList();
        ttdProductDetailPageWeb.chooseTicket(1);

        step("Set total ticket");
        ttdSelectedPackageDetailPageWeb.fillAvailableSection();
        ttdSelectedPackageDetailPageWeb.setTicketQty(1, 0);

        step("Submit package detail");
        ttdSelectedPackageDetailPageWeb.clickBookButton();
        webViewPage.switchToNative();

        step("Completing booking form data");
        webViewPage.switchToWebViewUrl("to-do");
        ttdBookingFormPageWeb.fillContactOrderDetail(false);
        ttdBookingFormPageWeb.fillGuestDetail();

        step("Submit booking");
        ttdBookingFormPageWeb.continueToPaymentPage();
        ttdBookingFormPageWeb.checkPriceNotifChange();
        webViewPage.switchToNative();

        step("Open payment method");
        paymentPage.openPriceDetail();
        Boolean isPriceCorrect = paymentPage.priceBreakDown();

        step("Fill Debit Card Info");
        paymentPage.fillCardDetailPayment(cardNumber, expiry, cvv, cardName);

        step("Click pay button");
        paymentPage.clickPayButton();
        Boolean isPaymentSuccessful = paymentPage.isPaymentSuccessful();

        step("Verify Step");
        Assertion.assertThat("Asserting if the price detail is correct ", isPriceCorrect, is(true));
        Assertion.assertThat("Asserting if the payment is successful ", isPaymentSuccessful, is(true));
    }

    @Author(names = {"Akbar"})
    @Module(name = "MultiCurrency")
    @Tribe(name = "Payment")
    @Vertical(name = "PRO")
    @Test
    public void testMCCheckDebitCardPaymentSuccessLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testMCCheckDebitCardPaymentSuccessLogin");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);

        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);
        TtdSelectedPackageDetailPageWeb<?> ttdSelectedPackageDetailPageWeb = TtdSelectedPackageDetailPageWeb.getInstance(session);
        TtdBookingFormPageWeb<?> ttdBookingFormPageWeb = TtdBookingFormPageWeb.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");

        String product = param.get("product");
        String currency = param.get("currency");
        String cardNumber = param.get("cardNumber");
        String expiry = param.get("expiry");
        String cvv = param.get("cvv");
        String cardName = param.get("cardName");

        BaseTest.paramsThreadLocal.set(param);

        step("Go to home page");
        homePage.gotoHome();

        step("Login with credential");
        loginPage.loginAsGuest();
        loginPage.loginWith(loginType, username, password);

        step("Set currency to "+ currency);
        homePage.changeCurrency(currency,username);

        step("Open TTD landing page");
        homePage.openVerticalPageDeeplink("To Do","",currency);

        step("Search the product");
        ttdLandingPage.clickSearchBar();
        ttdSearchPage.inputSearchKeyword(product);

        step("select the activity");
        ttdSearchPage.selectRecommendedActivity(product);

        step("Select ticket");
        webViewPage.switchToWebViewUrl("to-do");
        ttdProductDetailPageWeb.goToPackagesList();
        ttdProductDetailPageWeb.chooseTicket(1);

        step("Set total ticket");
        ttdSelectedPackageDetailPageWeb.fillAvailableSection();
        ttdSelectedPackageDetailPageWeb.setTicketQty(1, 0);

        step("Submit package detail");
        ttdSelectedPackageDetailPageWeb.clickBookButton();
        webViewPage.switchToNative();

        step("Completing booking form data");
        webViewPage.switchToWebViewUrl("to-do");
        ttdBookingFormPageWeb.fillContactOrderDetail(true);
        ttdBookingFormPageWeb.fillGuestDetail();

        step("Submit booking");
        ttdBookingFormPageWeb.continueToPaymentPage();
        ttdBookingFormPageWeb.checkPriceNotifChange();
        webViewPage.switchToNative();

        step("Open payment method");
        paymentPage.openPriceDetail();
        Boolean isPriceCorrect = paymentPage.priceBreakDown();

        step("Fill Debit Card Info");
        paymentPage.fillCardDetailPayment(cardNumber, expiry, cvv, cardName);

        step("Click pay button");
        paymentPage.clickPayButton();
        Boolean isPaymentSuccessful = paymentPage.isPaymentSuccessful();

        step("Verify Step");
        Assertion.assertThat("Asserting if the price detail is correct ", isPriceCorrect, is(true));
        Assertion.assertThat("Asserting if the payment is successful ", isPaymentSuccessful, is(true));
    }

    @Author(names = {"Kartik"})
    @Module(name = "PaymentMethod")
    @Tribe(name = "Payment")
    @Vertical(name = "PRO")
    @Test
    public void checkKredivoPaymentLogin() {
        Map<String, String> map = getParam("checkKredivoPaymentLogin");
        BaseTest.paramsThreadLocal.set(map);
        Session<WebDriver> session = sessionThreadLocal.get();

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);
        TtdSelectedPackageDetailPageWeb<?> ttdSelectedPackageDetailPageWeb = TtdSelectedPackageDetailPageWeb.getInstance(session);
        TtdBookingFormPageWeb<?> ttdBookingFormPageWeb = TtdBookingFormPageWeb.getInstance(session);

        String product = map.get("product");
        int ticketQty = Integer.parseInt(map.get("ticket_qty"));
        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");

        step("Go To Home");
        homePage.gotoHome();

        step("User input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Open TTD landing page");
        homePage.openVerticalPageDeeplink("To Do","","");

        step("Click search bar on TTD landing page");
        ttdLandingPage.clickSearchBar();

        step("Search the product '"+product+"' and select");
        ttdSearchPage.inputSearchKeyword(product);
        ttdSearchPage.selectRecommendedActivity(product);

        step("Select ticket");
        webViewPage.switchToWebViewUrl("to-do");
        ttdProductDetailPageWeb.chooseTicket();

        step("Set total ticket");
        ttdSelectedPackageDetailPageWeb.setTicketQty(ticketQty);

        step("Submit package detail");
        ttdSelectedPackageDetailPageWeb.clickBookButton();
        webViewPage.switchToNative();

        step("Completing booking form data");
        webViewPage.switchToWebViewUrl("to-do");
        ttdBookingFormPageWeb.fillContactOrderDetail(true);
        ttdBookingFormPageWeb.fillGuestDetail();

        step("Submit booking");
        ttdBookingFormPageWeb.continueToPaymentPage();
        webViewPage.switchToNative();

        step("Open payment method");
        paymentPage.openPaymentMethod();

        step("Select Kredivo as payment method");
        paymentPage.selectPaymentMethod("No Card Installment");

        step("open price detail page");
        paymentPage.openPriceDetail();

        Assertion.assertThat("Is Price BreakDown Correct", paymentPage.priceBreakDown(), is(true));
        paymentPage.clickPayButton();
        Assertion.assertThat("Verify Kredivo redirect", paymentPage.verifyKredivoRedirect(), is(true));
    }

    @Author(names = {"Akbar"})
    @Module(name = "PaymentMethod")
    @Tribe(name = "Payment")
    @Vertical(name = "PRO")
    @Test
    public void testCheckBlipayAsPaymentMethodGuest() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testCheckBlipayAsPaymentMethodGuest");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);

        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);
        TtdSelectedPackageDetailPageWeb<?> ttdSelectedPackageDetailPageWeb = TtdSelectedPackageDetailPageWeb.getInstance(session);
        TtdBookingFormPageWeb<?> ttdBookingFormPageWeb = TtdBookingFormPageWeb.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);

        String username = param.get("username");
        String password = param.get("password");
        String product = param.get("product");

        BaseTest.paramsThreadLocal.set(param);

        step("Go to home page");
        homePage.gotoHome();

        step("Login as guest");
        loginPage.loginAsGuest();

        step("Set currency to 'IDR'");
        homePage.changeCurrency("IDR","");

        step("Open TTD landing page");
        homePage.openVerticalPageDeeplink("To Do","","IDR");

        step("Search the product");
        ttdLandingPage.clickSearchBar();
        ttdSearchPage.inputSearchKeyword(product);

        step("select the activity");
        ttdSearchPage.selectRecommendedActivity(product);

        step("Select ticket");
        webViewPage.switchToWebViewUrl("to-do");
        ttdProductDetailPageWeb.goToPackagesList();
        ttdProductDetailPageWeb.chooseTicket(1);

        step("Set total ticket");
        ttdSelectedPackageDetailPageWeb.fillAvailableSection();
        ttdSelectedPackageDetailPageWeb.setTicketQty(1, 0);

        step("Submit package detail");
        ttdSelectedPackageDetailPageWeb.clickBookButton();
        webViewPage.switchToNative();

        step("Completing booking form data");
        webViewPage.switchToWebViewUrl("to-do");
        ttdBookingFormPageWeb.fillCustomContactOrderDetail("random",CoreFunctions.randomPhone(),CoreFunctions.randomEmail());
        ttdBookingFormPageWeb.fillGuestDetail();

        step("Submit booking");
        ttdBookingFormPageWeb.continueToPaymentPage();
        ttdBookingFormPageWeb.checkPriceNotifChange();
        webViewPage.switchToNative();

        step("Open payment method 'BlibliPay'");
        paymentPage.openPaymentMethod();
        paymentPage.selectPaymentMethod("blipay");

        step("Login to a user that already activated BlibliPay");
        loginPage.loginFromDifferentPages(username, password);
        boolean isLogin = !paymentPage.isLoginContainerDisplayed();

        step("Open payment method 'BlibliPay'");
        paymentPage.openPaymentMethod();
        paymentPage.openPaymentGroup("e-wallet");
        boolean isBlibliPayBalanceDisplayedOnPaymentGroup = paymentPage.isBlibliPayBalanceValid("payment-group");
        paymentPage.closePaymentGroup();
        paymentPage.selectPaymentMethod("blipay");
        boolean isBlibliPayBalanceDisplayedOnPaymentLanding = paymentPage.isBlibliPayBalanceValid("payment-landing");

        step("Verify Step");
        Assertion.assertThat("User changed to login user ", isLogin, is(true));
        Assertion.assertThat("Blipay balance is displayed on payment method page", isBlibliPayBalanceDisplayedOnPaymentGroup, is(true));
        Assertion.assertThat("Blipay balance is displayed on payment landing page", isBlibliPayBalanceDisplayedOnPaymentLanding, is(true));
    }

    @Author(names = {"Akbar"})
    @Module(name = "MultiCurrency")
    @Tribe(name = "Payment")
    @Vertical(name = "PRO")
    @Test
    public void testMCCheckCCPaymentSuccessWithPromoLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testMCCheckCCPaymentSuccessWithPromoLogin");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);

        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);
        TtdSelectedPackageDetailPageWeb<?> ttdSelectedPackageDetailPageWeb = TtdSelectedPackageDetailPageWeb.getInstance(session);
        TtdBookingFormPageWeb<?> ttdBookingFormPageWeb = TtdBookingFormPageWeb.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");

        String product = param.get("product");
        String currency = param.get("currency");
        int ticketQty = Integer.parseInt(param.get("ticket_qty"));

        String cardNumber = param.get("cardNumber");
        String expiry = param.get("expiry");
        String cvv = param.get("cvv");
        String cardName = param.get("cardName");

        BaseTest.paramsThreadLocal.set(param);

        step("Go to home page");
        homePage.gotoHome();

        step("Make user as guest");
        loginPage.loginWith(loginType, username, password);

        step("Set currency to "+ currency);
        homePage.changeCurrency(currency,username);

        step("Open TTD landing page");
        homePage.openVerticalPageDeeplink("To Do","","");

        step("Search the product");
        ttdLandingPage.clickSearchBar();
        ttdSearchPage.inputSearchKeyword(product);

        step("select the activity");
        ttdSearchPage.selectRecommendedActivity(product);

        step("Select ticket");
        webViewPage.switchToWebViewUrl("to-do");
        ttdProductDetailPageWeb.goToPackagesList();
        ttdProductDetailPageWeb.chooseTicket(1);

        step("Set total ticket");
        ttdSelectedPackageDetailPageWeb.setTicketQty(ticketQty, 0);

        step("Submit package detail");
        ttdSelectedPackageDetailPageWeb.clickBookButton();
        webViewPage.switchToNative();

        step("Completing booking form data");
        webViewPage.switchToWebViewUrl("to-do");
        ttdBookingFormPageWeb.fillContactOrderDetail(true);
        ttdBookingFormPageWeb.fillGuestDetail();

        step("Submit booking");
        ttdBookingFormPageWeb.continueToPaymentPage();
        ttdBookingFormPageWeb.checkPriceNotifChange();
        webViewPage.switchToNative();

        step("Open payment method");
        paymentPage.openPriceDetail();
        Boolean isPriceCorrect1 = paymentPage.priceBreakDown();

        step("Fill CC Info");
        paymentPage.fillCardDetailPayment(cardNumber, expiry, cvv, cardName);

        step("Apply promo");
        paymentPage.seeAllPromoCodes();
        paymentPage.applyPromoSuggestion();

        step("Verify price detail after promo applied");
        paymentPage.openPriceDetail();
        Boolean isPriceCorrect2 = paymentPage.priceBreakDown();

        step("Click pay button");
        paymentPage.clickPayButton();
        Boolean isPaymentSuccessful = paymentPage.isPaymentSuccessful();

        step("Verify Step");
        Assertion.assertThat("Asserting if the price detail before applying promo is correct ", isPriceCorrect1, is(true));
        Assertion.assertThat("Asserting if the price detail after applying promo is correct ", isPriceCorrect2, is(true));
        Assertion.assertThat("Asserting if the payment is successful ", isPaymentSuccessful, is(true));
    }

    @Author(names = {"Prerna"})
    @Module(name = "Promo")
    @Tribe(name = "Payment")
    @Vertical(name = "PRO")
    @Test
    public void verifyValidationErrorWithPromoCodeLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();

        Map<String, String> map = getParam("verifyValidationErrorWithPromoCodeLogin");
        BaseTest.paramsThreadLocal.set(map);

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        CarTransferPage<WebDriver> carTransferPage = CarTransferPage.getInstance(session);
        CarTransferSearchPage<WebDriver> carTransferSearchPage = CarTransferSearchPage.getInstance(session);
        CarTransferSearchResultPage<WebDriver> carTransferSearchResultPage = CarTransferSearchResultPage.getInstance(session);
        CarTransferBookingPage<WebDriver> carTransferBookingPage = CarTransferBookingPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        CarTransferApi<?> carTransferApi = new CarTransferApi<>(session);

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");
        String promoCode = map.get("promoCode");
        String promoCodeError = map.get("promoError");
        String origin = map.get("origin");
        String destination = map.get("destination");
        String type = map.get("type");
        String pickupTime = map.get("pickupTime").replace("/", ":");
        int pax = Integer.parseInt(map.get("pax"));
        String rangeAddDate = map.get("rangeAddDate");
        String issuedOrderID = "";

        if (Configs.ENVIRONMENT == Environment.GK || Configs.ENVIRONMENT == Environment.PREPROD) {
            step("Create AT waiting payment order for 1 passenger and 1 day via API");
            issuedOrderID = carTransferApi.createWaitingPaymentOrderApi(username, password, origin, destination, type, rangeAddDate, pickupTime, pax);
            Assertion.assertThat("Car transfer booking via API is successful", issuedOrderID, not(blankOrNullString()));
        }

        step("Goto home");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        if (Configs.ENVIRONMENT == Environment.PROD) {
            step("Open car transfer vertical page");
            homePage.openVerticalPageDeeplink("Car Transfers", "", "IDR");
            carTransferSearchPage.allowLocationAccess();
            webViewPage.switchToWebViewUrl("airport-transfer");
            carTransferSearchPage.closePopups();

            step("Select Date");
            carTransferSearchPage.selectDate(5);

            step("user input origin and destination");
            carTransferSearchPage.choosePickups("transfer_out",origin, destination);

            step("user search for car");
            carTransferSearchPage.searchCar();

            if(Configs.PLATFORM == Platform.IOS){
                webViewPage.switchToNative();
                carTransferSearchPage.allowLocationAccess();
                webViewPage.switchToWebViewUrl("airport-transfer");
            }

            step("user select car and vendor");
            carTransferSearchResultPage.selectCar();
            carTransferSearchResultPage.selectVendor();

            step("user continue to booking page");
            carTransferSearchResultPage.continueToBookingPage();

            step("continue to payment page");
            carTransferBookingPage.continueToPayment();
            webViewPage.switchToNative();
        }

        if (Configs.ENVIRONMENT == Environment.GK || Configs.ENVIRONMENT == Environment.PREPROD) {
            step("Open waiting payment order");
            paymentPage.openWaitingPaymentPage(issuedOrderID);
        }

        step("Wait for payment page load completely");
        paymentPage.waitForPaymentPageToLoad(10);

        step("Apply promo code");
        paymentPage.seeAllPromoCodes();
        paymentPage.applyPromoCode(promoCode);

        step("Check Error Message");
        boolean promoReasonCorrect = paymentPage.isErrorMessageCorrect(promoCodeError.split("/")[0] , promoCodeError.split("/")[1]);

        step("Click Terms and conditions");
        paymentPage.clickTermAndConditionPromo();

        Assertion.assertThat("Promo reason valid", promoReasonCorrect, is(true));
        Assertion.assertThat("Promo Bottom Sheet Available", paymentPage.isPromoTncBottomSheetShown(), is(true));
    }

    @Author(names = {"Prerna", "Aditi"})
    @Module(name = "Promo")
    @Tribe(name = "Payment")
    @Vertical(name = "PRO")
    @Test
    public void verifyAvbErrorTenorNotValidLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();

        Map<String, String> map = getParam("verifyAvbErrorTenorNotValidLogin");
        BaseTest.paramsThreadLocal.set(map);

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");
        String location = map.get("location");
        String promoCode = map.get("promoCode");
        String cardNumber = map.get("cardNumber");
        String expiry = map.get("expiry");
        String cvv = map.get("cvv");
        String cardName = map.get("cardName");

        step("Goto home");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);
        homePage.changeCurrency("IDR","");

        step("Open hotel landing page");
        homePage.openVerticalPageDeeplink("Hotel", "", "IDR");

        step("Search stay location");
        landingPage.searchStayLocation(location);

        step("Submit search");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        step("Go to the room list");
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();

        step("Select room from the room list");
        roomListPage.selectRoomByPriceMore(1000000);

        step("Submit Booking");
        bookingFormPage.continueToPaymentPage();
        bookingFormPage.PriceChangesPopUp();

        step("Choose payment method");
        paymentPage.checkOrderID();
        paymentPage.openPaymentMethod();
        paymentPage.selectPaymentMethod("Credit Card");

        step("Fill CC Info");
        paymentPage.fillCCDetailRealIssued(cardNumber, expiry, cvv, cardName);

        step("set installments for 3 months");
        paymentPage.setInstallments(2);

        step("Apply promo");
        paymentPage.seeAllPromoCodes();
        paymentPage.applyPromoCode(promoCode);

        step("Change Installments to 6 months");
        paymentPage.setInstallments(3);

        Assertion.assertThat("Is Error Popup Shown",paymentPage.isAVBErrorAppears(), is(true));

        step("Click see all promo button");
        paymentPage.clickSeeAllPromoAvBError();

        Assertion.assertThat("User is on promo list page ", paymentPage.isOnPromoSuggestionList(), is(true));
    }


    @Author(names = {"Prerna"})
    @Module(name = "Promo")
    @Tribe(name = "Payment")
    @Vertical(name = "Promo")
    @Test
    public void testApplyUnApplyGvAndPromo() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testApplyUnApplyGvAndPromo");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String location = param.get("location");
        String promoCode = param.get("promoCode").trim();

        String currency = param.get("currency");

        step("Go to home page");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink and change currency to " + currency);
        homePage.loginWithSpecificAccountViaDeeplink(username, password, currency);

        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);
        step("Open hotel landing page");
        homePage.openVerticalPageDeeplink("Hotel", "", "IDR");

        step("Search stay location");
        landingPage.searchStayLocation(location);

        step("Submit search");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        step("Go to the room list");
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();

        step("Select room from the room list");
        roomListPage.getTheFirstHotelRoomPrice();
        roomListPage.clickBookRoom();

        step("Submit Booking");
        bookingFormPage.continueToPaymentPage();
        bookingFormPage.PriceChangesPopUp();

        step("Get original total price");
        paymentPage.waitForPaymentPageToLoad(10);
        String totalPrice = paymentPage.getTotalPrice();

        step("Open voucher and promo");
        paymentPage.seeAllPromoCodes();

        step("Add voucher promo from suggestion list");
        paymentPage.applyPromoCode(promoCode);

        step("Check voucher promo code applied");
        Assertion.assertThat("Verify discount icon present", paymentPage.isDiscountIconPresent(), is(true));

        step("Check for voucher promo strikeout/crossed price");
        String crossedPriceBefore = paymentPage.getCrossedPriceBefore();
        Assertion.assertThat("Verify strikeout price is equal to original price", crossedPriceBefore, equalTo(totalPrice));

        step("cancel voucher promo");
        paymentPage.unApplyPromoCode();

        step("Check promo has been unapply");
        Assertion.assertThat("Verify promo has been unapply", paymentPage.verifyPromoUnApply(), is(true));

        step("Open voucher and promo");
        paymentPage.seeAllPromoCodes();

        step("Add voucher promo from suggestion list");
        paymentPage.applyPromoCode(promoCode);

        step("Check gift voucher code applied");
        Assertion.assertThat("Verify discount icon present", paymentPage.isDiscountIconPresent(), is(true));

        step("Check for gift voucher strikeout/crossed price");
        crossedPriceBefore = paymentPage.getCrossedPriceBefore();
        Assertion.assertThat("Verify strikeout price is equal to original price", crossedPriceBefore, equalTo(totalPrice));

        step("cancel gift voucher");
        paymentPage.unApplyPromoCode();

        step("Check gift vocher has been unapply");
        Assertion.assertThat("Verify promo has been unapply", paymentPage.verifyPromoUnApply(), is(true));
    }

    @Author(names = {"Prerna"})
    @Module(name = "Promo")
    @Tribe(name = "Payment")
    @Vertical(name = "Promo")
    @Test
    public void testVerifyPaidUsingPromoCashback() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testVerifyPaidUsingPromoCashback");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String hotelName = param.get("hotelName");

        String payment_method = param.get("paymentMethod");

        String cardNumber = param.get("cardNumber");
        String expiry = param.get("expiry");
        String cvv = param.get("cvv");
        String cardName = param.get("cardName");

        String promoCode = param.get("promoCode");

        String startDate = generateRandomDate(param.get("rangeAddDate"));
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));
        int room = Integer.parseInt(param.get("room"));
        String currency = param.get("currency");

        String[] salutationList = { "Mr", "Mrs", "Ms" };
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = salutationList[0];

        step("Create waiting payment order");
        String orderId = "";
        if(Configs.ENVIRONMENT.equals(Environment.GK) || Configs.ENVIRONMENT.equals(Environment.PREPROD)){
            HotelApi<?> hotelApi = new HotelApi<>(session);
            step("Create a hotel order with waiting payment status");
            orderId = hotelApi.createWaitingPaymentOrderApi(
                    username, password, startDate, hotelName, adult, night, room,
                    orderFullName, orderNameTitle, guestFullName, guestTitle,
                    currency,"id");
        }

        step("Go to home page");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink and change currency to " + currency);
        homePage.loginWithSpecificAccountViaDeeplink(username, password, currency);

        if(Configs.ENVIRONMENT.equals(Environment.PROD)){
            LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
            RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
            DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
            BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);
            step("Open hotel landing page");
            homePage.openVerticalPageDeeplink("Hotel", "", "IDR");

            step("Search stay location");
            landingPage.searchStayLocation(hotelName);

            step("set total room and total guest");
            landingPage.clickRoomAndGuestButton();
            landingPage.setTotalRoom(1);
            landingPage.setTotalGuest(1);
            landingPage.setTotalChildren(0);
            landingPage.clickDone();

            step("Submit search");
            landingPage.clickSubmit();
            landingPage.fillNightCheckInForm();

            step("Go to the room list");
            detailPage.seeHotelRoom();
            detailPage.RemovePopUp();

            step("Select room from the room list");
            roomListPage.getTheFirstHotelRoomPrice();
            roomListPage.clickBookRoom();

            step("Submit Booking");
            bookingFormPage.continueToPaymentPage();
            bookingFormPage.PriceChangesPopUp();
        }
        else{
            OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
            step("Open my order");
            homePage.openMyOrderPage();

            step("Open payment page");
            orderListPage.openOrderCompletePayment(orderId);
        }

        step("Wait payment page to get loaded");
        String paymentPageOrderId = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",paymentPageOrderId.isBlank(),is(false));

        step("Open payment method");
        paymentPage.openPaymentMethod();

        step("Select virtual account as payment method");
        paymentPage.selectPaymentMethod(payment_method);

        if(Configs.ENVIRONMENT.equals(Environment.GK) || Configs.ENVIRONMENT.equals(Environment.PREPROD)){
            step("Input and submit CC information");
            paymentPage.fillCCDetailRealIssued(cardNumber, expiry, cvv, cardName);
        }

        step("Open voucher and promo");
        paymentPage.seeAllPromoCodes();
        paymentPage.applyPromoCode(promoCode);
        String appliedPromoCode = paymentPage.getAppliedPromoCode();
        String appliedPromoText = paymentPage.getPromoText();
        Assertion.assertThat("Verify promo or voucher applied", appliedPromoCode, equalTo(promoCode));
        Assertion.assertThat("Verify promo text", appliedPromoText, is(either(containsString("points after payment")).or(containsString("poin setelah pembayaran"))));
        Assertion.assertThat("Verify discount icon present", paymentPage.isDiscountIconPresent(), is(true));

        if(Configs.ENVIRONMENT.equals(Environment.GK) || Configs.ENVIRONMENT.equals(Environment.PREPROD)){
            step("Submit the payment method");
            paymentPage.clickPayButtonCC();
            paymentPage.verifyCreditCard3ds();

            step("Assert if the payment is a success");
            Assertion.assertThat("Payment Successful and Thank You page is shown", paymentPage.isThankYouPageShown(), is(true));
        }else {
            step("cancel gift voucher");
            paymentPage.unApplyPromoCode();

            step("Check gift vocher has been unapply");
            Assertion.assertThat("Verify promo has been unapply", paymentPage.verifyPromoUnApply(), is(true));
        }
    }

    @Author(names = {"Ashish"})
    @Module(name = "Promo")
    @Tribe(name = "Payment")
    @Vertical(name = "PRO")
    @Test
    public void testVerifyPaidPromoForB2BPayment() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testVerifyPaidPromoForB2BPayment");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        CorporateLoginPage<WebDriver> corporateLoginPage = CorporateLoginPage.getInstance(session);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String adminEmail = param.get("adminEmail");
        String adminPassword = param.get("adminPassword");
        String hotelName = param.get("hotelName");

        String promoCode = param.get("promoCode");

        String startDate = generateRandomDate(param.get("rangeAddDate"));
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));
        int room = Integer.parseInt(param.get("room"));
        String currency = param.get("currency");

        String[] salutationList = { "Mr", "Mrs", "Ms" };
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = salutationList[0];

        String orderId = "";
        if(Configs.ENVIRONMENT.equals(Environment.GK) || Configs.ENVIRONMENT.equals(Environment.PREPROD)){
            HotelApi<?> hotelApi = new HotelApi<>(session);
            step("Create new hotel issued order");
            String orderID = hotelApi.createWaitForPaymentApiB2B(
                    username, password, startDate, hotelName, adult, night, room,"IDR",
                    orderFullName, orderNameTitle, guestFullName, guestTitle);
            Assertion.assertThat("Hotel Booking via API is successful for B2B account", !orderID.isEmpty(), is(true));
        }

        step("Goto home");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink and change currency to " + currency);
        homePage.loginWithSpecificAccountViaDeeplink(username, password, currency);

        if(Configs.ENVIRONMENT.equals(Environment.PROD)){
            LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
            RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
            DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
            BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);

            step("Open hotel landing page");
            homePage.openVerticalPage("Hotel");

            step("Search stay location");
            landingPage.searchStayLocation(hotelName);

            step("set total room and total guest");
            landingPage.clickRoomAndGuestButton();
            landingPage.setTotalRoom(1);
            landingPage.setTotalGuest(1);
            landingPage.setTotalChildren(0);
            landingPage.clickDone();

            step("Submit search");
            landingPage.clickSubmit();
            landingPage.fillNightCheckInForm();

            step("Go to the room list");
            detailPage.seeHotelRoom();
            detailPage.RemovePopUp();

            step("Select room from the room list");
            roomListPage.getTheFirstHotelRoomPrice();
            roomListPage.clickBookRoom();

            step("Submit Booking");
            bookingFormPage.continueToPaymentPage();
            bookingFormPage.PriceChangesPopUp();
        }
        else{
            OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
            step("Open my order");
            homePage.openMyOrderPage();

            step("Open payment page");
            orderListPage.openOrderCompletePayment(orderId);
        }

        step("Wait payment page to get loaded");
        String paymentPageOrderId = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",!paymentPageOrderId.isBlank(),is(true));

        step("Get original total price");
        String totalPrice = paymentPage.getTotalPrice();

        step("Open voucher and promo");
        paymentPage.seeAllPromoCodes();
        paymentPage.applyPromoCode(promoCode);

        step("check promo applied");
        String appliedPromoCode = paymentPage.getAppliedPromoCode();
        String appliedPromoText = paymentPage.getPromoText();
        double selectedVoucherPromoDiscount = CoreFunctions.convertPriceSafely(appliedPromoText);
        Assertion.assertThat("Verify promo or voucher applied", appliedPromoCode, equalTo(promoCode));
        Assertion.assertThat("Verify promo text", appliedPromoText, is(either(containsString("Yay! You save")).or(containsString("Hore! Kamu hemat"))));
        Assertion.assertThat("Verify discount icon present", paymentPage.isDiscountIconPresent(), is(true));

        step("Check for voucher promo strikeout/crossed price");
        String crossedPriceBefore = paymentPage.getCrossedPriceBefore();
        String newPrice = paymentPage.getTotalPrice();
        String discountPriceBelowTotalPrice = paymentPage.getDiscountAmmount();
        Assertion.assertThat("Verify strikeout price is equal to original price", crossedPriceBefore, equalTo(totalPrice));
        Assertion.assertThat("Verify total price is equal to original total price - promo discount", CoreFunctions.convertPriceSafely(newPrice), equalTo((CoreFunctions.convertPriceSafely(totalPrice)-selectedVoucherPromoDiscount)));
        Assertion.assertThat("Verify discount price below total price", CoreFunctions.convertPriceSafely(discountPriceBelowTotalPrice), equalTo(selectedVoucherPromoDiscount));

        if(Configs.ENVIRONMENT.equals(Environment.GK) || Configs.ENVIRONMENT.equals(Environment.PREPROD)){

            step("Click pay with corporate billing");
            paymentPage.clickPayButtonWithoutWaitForPreviousBookingPopup();

            step("verify successfully placed order popup from corporate");
            Assertion.assertThat("get successfully placed order popup",paymentPage.isCorporateSuccessfullyPlacedOrderPopup(),is(true));

            step("Approve booking by approver account for Order ID: " + paymentPageOrderId);
            Boolean bookingStatus = corporateApi.approveBookingB2BApi(adminEmail, adminPassword, paymentPageOrderId);
            Assertion.assertThat("Booking approved by B2B dashboard", bookingStatus, is(true));

            step("verify successful payment popup from corporate");
            Assertion.assertThat("get successfully placed order popup",paymentPage.isCorporatePaymentSuccessPopupDisplayed(),is(true));
        }else {
            step("cancel gift voucher");
            paymentPage.unApplyPromoCode();

            step("Check gift vocher has been unapply");
            Assertion.assertThat("Verify promo has been unapply", paymentPage.verifyPromoUnApply(), is(true));
        }
    }

    @Author(names = {"Dendi", "Aditi"})
    @Module(name = "Promo")
    @Tribe(name = "Payment")
    @Vertical(name = "PRO")
    @Test
    public void verifyPsv2Flow(){
        Map<String,String> param = getParam("verifyPsv2Flow");
        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);

        BaseTest.paramsThreadLocal.set(param);

        String email = param.get("email");
        String promoCode = param.get("promoCode");
        String password = param.get("password");
        String location = param.get("location");

        step("User open app");
        homePage.gotoHome();

        step("Logout via deeplink");
        homePage.logoutViaDeeplink();

        step("Get language app");
        String language = homePage.getLanguageApp();
        String nudgesName = language.equalsIgnoreCase("id") ? param.get("nudgesNameId") : param.get("nudgesNameEn");

        step("Change currency to IDR");
        homePage.changeCurrency("IDR",email);

        step("Open hotel landing page");
        homePage.openVerticalPageDeeplink("Hotel", "", "");

        step("Search stay location");
        landingPage.searchStayLocation(location);

        step("Submit search");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        step("Go to the room list");
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();

        step("Select room from the room list");
        roomListPage.getTheFirstHotelRoomPrice();
        roomListPage.clickBookRoom();

        step("Completing booking form data");
        bookingFormPage.fillContactOrderDetail(email);
        bookingFormPage.fillGuestDetail(false);

        step("Submit booking");
        bookingFormPage.continueToPaymentPage();
        bookingFormPage.PriceChangesPopUp();

        step("Get original total price");
        paymentPage.waitForPaymentPageToLoad(10);
        String totalPrice = paymentPage.getTotalPrice();

        step("Open voucher and promo");
        paymentPage.seeAllPromoCodes();

        step("Scroll to promo "+promoCode+" in eligible promo list");
        paymentPage.scrollToPromoCardInPromoList(promoCode);

        step("Verify need login nudges shown");
        Assertion.assertThat("Verify need login nudges label in promo card present", paymentPage.checkNudgesLabelInPromoCardIsShown(nudgesName), is(true));

        step("Apply promo code");
        paymentPage.clickPromoCardInPromoList(promoCode);

        step("Check login pop up appear");
        Assertion.assertThat("Verify login chalange pop up appear after applying promo", paymentPage.isLoginChallengePopupDisplayed(), is(true));

        step("Open login page and login");
        if (Configs.PLATFORM == Platform.IOS)
            paymentPage.clickLoginFromNativePopup();
        else
            paymentPage.clickLoginFromPopup();

        step("Login with email and password");
        loginPage.loginFromDifferentPages(email, password);

        step("check promo applied");
        paymentPage.waitForPaymentPageToLoad(10);
        paymentPage.checkOrderID();
        String appliedPromoCode = paymentPage.getAppliedPromoCode();
        String appliedPromoText = paymentPage.getPromoText();
        double selectedVoucherPromoDiscount = CoreFunctions.removeText(appliedPromoText);
        Assertion.assertThat("Verify promo or voucher applied", appliedPromoCode, equalTo(promoCode));
        Assertion.assertThat("Verify promo text", appliedPromoText, is(either(containsString("Yay! You saved IDR")).or(containsString("Hore! Kamu hemat IDR"))));
        Assertion.assertThat("Verify discount icon present", paymentPage.isDiscountIconPresent(), is(true));

        step("Check for voucher promo strikeout/crossed price");
        String crossedPriceBefore = paymentPage.getCrossedPriceBefore();
        String newPrice = paymentPage.getTotalPrice();
        String discountPriceBelowTotalPrice = paymentPage.getDiscountAmmount();
        Assertion.assertThat("Verify strikeout price is equal to original price", crossedPriceBefore, equalTo(totalPrice));
        Assertion.assertThat("Verify total price is equal to original total price - promo discount", CoreFunctions.convertPriceSafely(newPrice), equalTo((CoreFunctions.convertPriceSafely(totalPrice)-selectedVoucherPromoDiscount)));
        Assertion.assertThat("Verify discount price below total price", CoreFunctions.convertPriceSafely(discountPriceBelowTotalPrice), equalTo(selectedVoucherPromoDiscount));
    }

    @Author(names = {"Prerna", "Aditi"})
    @Module(name = "Promo")
    @Tribe(name = "Payment")
    @Vertical(name = "PRO")
    @Test
    public void verifyPsv3Flow(){
        Map<String,String> param = getParam("verifyPsv3Flow");
        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        CarTransferPage<WebDriver> carTransferPage = CarTransferPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        CarTransferSearchPage<WebDriver> carTransferSearchPage = CarTransferSearchPage.getInstance(session);
        CarTransferSearchResultPage<WebDriver> carTransferSearchResultPage = CarTransferSearchResultPage.getInstance(session);
        CarTransferBookingPage<WebDriver> carTransferBookingPage = CarTransferBookingPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        CarTransferApi<?> carTransferApi = new CarTransferApi<>(session);

        BaseTest.paramsThreadLocal.set(param);

        String origin = param.get("origin");
        String destination = param.get("destination");
        String email = param.get("email");
        String phoneNumber = param.get("phoneNumber");
        String promoCode = param.get("promoCode");
        String password = param.get("password");
        String loginType = param.get("loginType");
        String type = param.get("type");
        String pickupTime = param.get("pickupTime").replace("/", ":");
        int pax = Integer.parseInt(param.get("pax"));
        String rangeAddDate = param.get("rangeAddDate");
        String fleetName = param.get("fleetName");
        int airportTransferFee = Integer.parseInt(param.get("ATFee"));

        step("User open app");
        homePage.gotoHome();

        step("Login as " + email + " via deeplink and change currency to IDR");
        homePage.loginWithSpecificAccountViaDeeplink(email, password, "IDR");

        if(Configs.ENVIRONMENT.equals(Environment.GK) || Configs.ENVIRONMENT.equals(Environment.PREPROD)){
            step("Create car transfer waiting payment order for 1 passenger and 1 day via API");
            String issuedOrderID = carTransferApi.createWaitingPaymentOrderApi(email,password,origin,destination,type,rangeAddDate,pickupTime,pax, Optional.of(fleetName));
            Assertion.assertThat("Car transfer booking via API is successful", issuedOrderID, not(blankOrNullString()));
            String grandTotal = String.valueOf(carTransferApi.getGrandTotal());

            step("Open my order page");
            homePage.openMyOrderPage();

            step("Open payment page for order id "+issuedOrderID);
            paymentPage.openWaitingPaymentPage(issuedOrderID);
        }else{
            step("Change currency");
            homePage.changeCurrency("IDR","");

            step("User open car transfer vertical page");
            homePage.openVerticalPageDeeplink("Airport Transfers","","");
            webViewPage.switchToWebViewUrl("airport-transfer");
            carTransferSearchPage.closePopups();

            step("User choose car transfer in");
            carTransferSearchPage.chooseTransferType("transfer_in");

            step("user input origin and destination");
            carTransferSearchPage.choosePickup(origin, destination);

            step("user search for car");
            carTransferSearchPage.searchCar();

            if(Configs.PLATFORM == Platform.IOS){
                webViewPage.switchToNative();
                carTransferSearchPage.allowLocationAccess();
                webViewPage.switchToWebViewUrl("airport-transfer");
            }

            step("user select car and vendor");
            carTransferSearchResultPage.selectCarAndVendor();

            step("user continue to booking page");
            carTransferSearchResultPage.continueToBookingPage();

            step("user fill contact detail in booking page");
            carTransferBookingPage.fillDetailOrder(email, phoneNumber);

            step("user continue to payment list page");
            carTransferBookingPage.continueToPayment();
            webViewPage.switchToNative();
        }


        step("Get original total price");
        String totalPrice = paymentPage.getTotalPrice();

        step("Open voucher and promo");
        paymentPage.seeAllPromoCodes();

        step("Scroll to promo "+promoCode+" in eligible promo list");
        paymentPage.scrollToPromoCardInPromoList(promoCode);

        step("Change method for ineligible promo");
        paymentPage.clickChangeMethodForInEligiblePromo(promoCode);

        step("Verify psv3 popup");
        Assertion.assertThat("verify psv3 pop up is shown", paymentPage.verifyPsv3Shown(), is(true));

        step("Change payment method from psv3");
        paymentPage.clickPsv3ChangePaymentMethod();

        step("check promo applied");
        String appliedPromoCode = paymentPage.getAppliedPromoCode();
        String appliedPromoText = paymentPage.getPromoText();
        double selectedVoucherPromoDiscount = CoreFunctions.convertPriceSafely(appliedPromoText);
        Assertion.assertThat("Verify promo or voucher applied", appliedPromoCode, equalTo(promoCode));
        Assertion.assertThat("Verify promo text", appliedPromoText, is(either(containsString("Yay! You save")).or(containsString("Hore! Kamu hemat"))));
        Assertion.assertThat("Verify discount icon present", paymentPage.isDiscountIconPresent(), is(true));

        step("Check for voucher promo strikeout/crossed price");
        String crossedPriceBefore = paymentPage.getCrossedPriceBefore();
        String newPrice = paymentPage.getTotalPrice();
        String discountPriceBelowTotalPrice = paymentPage.getDiscountAmmount();
        Assertion.assertThat("Verify strikeout price is equal to (original price + AT fee)", CoreFunctions.convertPriceSafely(crossedPriceBefore), equalTo(CoreFunctions.convertPriceSafely(totalPrice)+airportTransferFee));
        Assertion.assertThat("Verify total price is equal to original total price - promo discount + AT Fee", CoreFunctions.convertPriceSafely(newPrice), equalTo((CoreFunctions.convertPriceSafely(totalPrice)-selectedVoucherPromoDiscount+airportTransferFee)));
        Assertion.assertThat("Verify discount price below total price", CoreFunctions.convertPriceSafely(discountPriceBelowTotalPrice), equalTo(selectedVoucherPromoDiscount));
    }

    @Author(names = {"Zia"})
    @Module(name = "Payment")
    @Tribe(name = "Payment")
    @Vertical(name = "PRO")
    @Test
    public void testSeeAllPaymentMethods() {
        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        PaymentMethodPage<?> paymentMethodPage = new PaymentMethodPage<>(session);

        Map<String, String> param = getParam("testSeeAllPaymentMethods");
        BaseTest.paramsThreadLocal.set(param);
        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");

        step("User goto home");
        homePage.gotoHome();

        step("User open app as login user");
        loginPage.loginWith(loginType, username, password);
        homePage.changeCurrency("IDR",username);

        step("User open account page");
        homePage.openAccount();

        step("Click see all payment methods");
        accountPage.clickSeeAllPayment();

        step("Verify payment methods shown");
        List<String> paymentMethods = paymentMethodPage.getPaymentMethods();
        Assertion.assertThat("Payment methods exist", paymentMethods, is(not(empty())));
    }

    @Author(names = {"Kartik"})
    @Module(name = "Promo")
    @Tribe(name = "Payment")
    @Vertical(name = "PRO")
    @Test
    public void verifyAvbErrorPaymentMethodNotValidLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();

        Map<String, String> map = getParam("verifyAvbErrorPaymentMethodNotValidLogin");
        BaseTest.paramsThreadLocal.set(map);

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        RoomDetailPage<WebDriver> roomDetailPage = RoomDetailPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");
        String location = map.get("location");
        String promoCode = map.get("promoCode");


       step("Goto home");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        homePage.changeCurrency("IDR",username);

        step("Open hotel landing page");
        homePage.openVerticalPageDeeplink("Hotel", "", "IDR");

        step("Search stay location");
        landingPage.searchStayLocation(location);

        step("set total room and total guest");
        landingPage.clickRoomAndGuestButton();
        landingPage.setTotalRoom(1);
        landingPage.setTotalGuest(1);
        landingPage.setTotalChildren(0);
        landingPage.clickDone();

        step("Submit search");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        step("Go to the room list");
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();

        step("Select room from the room list");
        roomListPage.selectRoomByPriceMore(1000000);

        step("Submit Booking");
        bookingFormPage.continueToPaymentPage();
        bookingFormPage.PriceChangesPopUp();

        step("Open voucher and promo");
        paymentPage.seeAllPromoCodes();
        paymentPage.applyPromoCode("TESTAUTDAILYOTWVA");

        step("Choose payment method Acc to Promo");
        paymentPage.clickPsv3ChangePaymentMethod();

        step("Open payment method");
        paymentPage.openPaymentMethod();

        step("Select cc as payment method");
        paymentPage.selectPaymentMethod("Credit Card");
        Assertion.assertThat("Is Error Popup Shown",paymentPage.isAVBErrorAppears(), is(true));

        step("Click see all promo button");
        paymentPage.clickSeeAllPromoAvBError();
        paymentPage.seeAllPromoCodes();
        Assertion.assertThat("User is on promo list page ", paymentPage.isOnPromoSuggestionList(), is(true));

    }

    @Author(names = {"Areeb"})
    @Module(name = "PaymentMethod")
    @Tribe(name = "Payment")
    @Vertical(name = "Sphinx")
    @Test
    public void verifyDifferentErrorMessageForNonIndonesianPayLater() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("verifyDifferentErrorMessageForNonIndonesianPayLater");
        BaseTest.paramsThreadLocal.set(map);

        String username = map.get("username");
        String password = map.get("password");
        String tier = "Tier";
        Set<String> titles = Set.of("Unable to load content", "Gagal memuat konten");
        String englishContent = "We encounter a technical issue. Please try again in a few moments.";
        String indonesianContent = "Kami sedang mengalami gangguan teknis. Mohon coba lagi dalam beberapa saat.";

        HomePage<?> homePage = HomePage.getInstance(session);
        ErrorMessagePage<?> errorMessagePage = ErrorMessagePage.getInstance(session);

        step("User goto home");
        homePage.gotoHome();

        step("Login and change currency to IDR");
        homePage.loginIfLoggedOutAndChangeCurrencyViaDeeplink(username, password, "IDR");
        homePage.refreshHomePage(1);

        step("Verify IDR login details show tier, PayLater, and BlibliPay");
        Assertion.assertThat(
                "IDR login details should show loyalty tier, PayLater, and BlibliPay",
                homePage.areIdrLoginDetailsShown(tier), is(true));

        step("Click PayLater");
        homePage.clickPayLaterOnFinanceBar();

        step("Verify PayLater error message is shown");
        Assertion.assertThat("PayLater error message should be shown for non-Indonesian users",
                errorMessagePage.isErrorDisplayed(), is(true));
        String messageContent = errorMessagePage.getMessageContent();
        Assertion.assertThat("PayLater error message title should be correct",
                titles, hasItem(errorMessagePage.getMessageTitle()));
        Assertion.assertThat("PayLater error message content should be correct",
                messageContent,
                anyOf(startsWith(englishContent), startsWith(indonesianContent)));

        step("Click error message button");
        Assertion.assertThat("PayLater error message button should be displayed",
                errorMessagePage.isButtonDisplayed(), is(true));
        errorMessagePage.clickButton();
        Assertion.assertThat("PayLater error message should appear again",
                errorMessagePage.isErrorDisplayed(), is(true));
    }
}