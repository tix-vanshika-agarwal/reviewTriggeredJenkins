package com.tiket.test.nft.car_transfer;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.model.Environment;
import com.tiket.model.Platform;
import com.tiket.model.Session;
import com.tiket.page.cartransfer.bookingform.CarTransferBookingPage;
import com.tiket.page.cartransfer.searchpage.CarTransferSearchPage;
import com.tiket.page.cartransfer.searchresultpage.CarTransferSearchResultPage;
import com.tiket.page.common.webview.WebViewPage;
import com.tiket.page.home.HomePage;
import com.tiket.page.login.LoginPage;
import com.tiket.page.order.orderdetail.OrderDetailPage;
import com.tiket.page.order.orderlist.OrderListPage;
import com.tiket.page.payment.PaymentPage;
import com.tiket.pojo.cartransfer.TripDetails;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.testbase.Configs;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Map;

import static com.tiket.testdata.TestData.getObjectParams;
import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.core.Is.is;

public class CarTransferTest extends BaseTest {

    @Author(names = {"Dendi", "Zia", "Aditi"})
    @Module(name = "AT")
    @Tribe(name = "NFT")
    @Vertical(name = "GroundTransport")
    @Test(dataProvider = "testCTOutLoginDataProvider")
    public void AT_testCTOutLogin(Map<String, String> testProperties) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(testProperties);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        CarTransferSearchPage<WebDriver> carTransferSearchPage = CarTransferSearchPage.getInstance(session);
        CarTransferSearchResultPage<WebDriver> carTransferSearchResultPage = CarTransferSearchResultPage.getInstance(session);
        CarTransferBookingPage<WebDriver> carTransferBookingPage = CarTransferBookingPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);

        String loginMethod = testProperties.get("loginType");
        String username = testProperties.get("username");
        String password = testProperties.get("password");
        String airport = testProperties.get("airport");
        String destination = testProperties.get("destination");
        String currency = testProperties.get("currency");

        step("User open app");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink (if needed) and change currency to " + currency);
        homePage.loginIfLoggedOutAndChangeCurrencyViaDeeplink(username, password, currency);

        step("Open Car Landing Page");
        homePage.openVerticalPageDeeplink("Car Transfers", "", currency);
        carTransferSearchPage.allowLocationAccess();
        webViewPage.switchToWebViewUrl("airport-transfer");
        carTransferSearchPage.closePopups();

        step("User choose car transfer out");
        carTransferSearchPage.chooseTransferType("transfer_out");

        step("select date 1-3 (Random)");
        carTransferSearchPage.toggleForLaterOrder();

        step("user input origin and destination");
        carTransferSearchPage.choosePickups("transfer_out", airport, destination);

        step("user search for car");
        carTransferSearchPage.searchCar();

        if(Configs.PLATFORM == Platform.IOS){
            webViewPage.switchToNative();
            carTransferSearchPage.allowLocationAccess();
            webViewPage.switchToWebViewUrl("airport-transfer");
        }

        step("user select car");
        carTransferSearchResultPage.selectCar();

        step("user select vendor");
        carTransferSearchResultPage.selectVendor();

        step("user see price detail in car detail page");
        String carType = carTransferSearchResultPage.getCarType();
        carTransferSearchResultPage.openPriceDetail();
        Assertion.assertThat("ORIGIN IS VALID", carTransferSearchResultPage.verifyOrigin(airport) , containsString(airport));
        Assertion.assertThat("DESTINATION IS VALID", carTransferSearchResultPage.verifyDestination(destination) , containsString(destination));
        Assertion.assertThat("PRICE OF CAR FARE IS PRESENT",carTransferSearchResultPage.isCarFarePresent(currency),is(true));
        String price = carTransferSearchResultPage.getPrice();
        carTransferSearchResultPage.closePriceDetail();

        step("user continue to booking page");
        carTransferSearchResultPage.continueToBookingPage();

        step("open price detail in booking page");
        carTransferBookingPage.openPriceDetail();

        step("user see price detail in booking page");
        Assertion.assertThat("ORIGIN IN BOOKING PAGE IS VALID", carTransferBookingPage.verifyOrigin(airport) , containsString(airport));
        Assertion.assertThat("DESTINATION IN BOOKING PAGE IS VALID", carTransferBookingPage.verifyDestination(destination) , containsString(destination));
        Assertion.assertThat("Price from SRP is the same with price from booking form", carTransferBookingPage.verifyPriceDetail(price,carType), is(true));
        carTransferBookingPage.closePriceDetail();

        step("user continue to payment list page");
        carTransferBookingPage.continueToPayment();
        webViewPage.switchToNative();

        step("user see order id in payment page");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",!orderID.isEmpty(),is(true));
    }

    @Author(names = {"Budi", "Aditi"})
    @Module(name = "CarTransfersCompleteBooking")
    @Tribe(name = "CarTransport")
    @Vertical(name = "GroundTransport")
    @Test(dataProvider = "testCTOutGuestDataProvider")
    public void testCTOutGuest(Map<String, String> testProperties) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(testProperties);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        CarTransferSearchPage<WebDriver> carTransferSearchPage = CarTransferSearchPage.getInstance(session);
        CarTransferSearchResultPage<WebDriver> carTransferSearchResultPage = CarTransferSearchResultPage.getInstance(session);
        CarTransferBookingPage<WebDriver> carTransferBookingPage = CarTransferBookingPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);

        String username = testProperties.get("username");
        String phoneNumber = testProperties.get("phoneNumber");
        String airport = testProperties.get("airport");
        String destination = testProperties.get("destination");
        String currency = testProperties.get("currency");

        step("User open app");
        homePage.gotoHome();

        step("Logout via deeplink");
        homePage.logoutViaDeeplink();

        if(Configs.ENVIRONMENT == Environment.PROD) {
            step("Change currency to 'IDR'");
            homePage.changeCurrency("IDR","");
        }

        step("Open Car Landing Page");
        homePage.openVerticalPageDeeplink("Car Transfers", "", currency);
        carTransferSearchPage.allowLocationAccess();
        webViewPage.switchToWebViewUrl("airport-transfer");
        carTransferSearchPage.closePopups();

        step("User choose car transfer out");
        carTransferSearchPage.chooseTransferType("transfer_out");

        step("select date 1-3 (Random)");
        carTransferSearchPage.toggleForLaterOrder();

        step("user input origin and destination");
        carTransferSearchPage.choosePickups("transfer_out", airport, destination);

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

        step("user see price detail in car detail page");
        String carType = carTransferSearchResultPage.getCarType();
        carTransferSearchResultPage.openPriceDetail();
        Assertion.assertThat("ORIGIN IS VALID", carTransferSearchResultPage.verifyOrigin(airport) , containsString(airport));
        Assertion.assertThat("DESTINATION IS VALID", carTransferSearchResultPage.verifyDestination(destination) , containsString(destination));
        Assertion.assertThat("PRICE OF CAR FARE IS PRESENT",carTransferSearchResultPage.isCarFarePresent(currency),is(true));
        String price = carTransferSearchResultPage.getPrice();
        carTransferSearchResultPage.closePriceDetail();

        step("user continue to booking page");
        carTransferSearchResultPage.continueToBookingPage();

        step("user fill contact detail in booking page");
        carTransferBookingPage.fillDetailOrder(username,phoneNumber);

        step("user see price detail in booking page");
        carTransferBookingPage.openPriceDetail();
        Assertion.assertThat("ORIGIN IN BOOKING PAGE IS VALID", carTransferBookingPage.verifyOrigin(airport) , containsString(airport));
        Assertion.assertThat("DESTINATION IN BOOKING PAGE IS VALID", carTransferBookingPage.verifyDestination(destination) , containsString(destination));
        Assertion.assertThat("Price from SRP is the same with price from booking form", carTransferBookingPage.verifyPriceDetail(price,carType), is(true));
        carTransferBookingPage.closePriceDetail();

        step("user continue to payment list page");
        carTransferBookingPage.continueToPayment();
        webViewPage.switchToNative();
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",!orderID.isEmpty(),is(true));
    }

    @Author(names = {"Dendi","Zia", "Aditi"})
    @Module(name = "AT")
    @Tribe(name = "NFT")
    @Vertical(name = "GroundTransport")
    @Test(dataProvider = "testCTInLoginDataProvider")
    public void AT_testCTInLogin(Map<String, String> testProperties) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(testProperties);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        CarTransferSearchPage<WebDriver> carTransferSearchPage = CarTransferSearchPage.getInstance(session);
        CarTransferSearchResultPage<WebDriver> carTransferSearchResultPage = CarTransferSearchResultPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        CarTransferBookingPage<WebDriver> carTransferBookingPage = CarTransferBookingPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);

        String airport = testProperties.get("airport");
        String destination = testProperties.get("destination");
        String currency = testProperties.get("currency");
        String username = testProperties.get("username");
        String password = testProperties.get("password");

        step("User open app");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink (if needed) and change currency to " + currency);
        homePage.loginIfLoggedOutAndChangeCurrencyViaDeeplink(username, password, currency);

        step("Open Car Landing Page");
        homePage.openVerticalPageDeeplink("Car Transfers", "", currency);
        carTransferSearchPage.allowLocationAccess();
       webViewPage.switchToWebViewUrl("airport-transfer");
        carTransferSearchPage.closePopups();

        step("User choose car transfer in");
        carTransferSearchPage.chooseTransferType("transfer_in");

        step("select date 1-3 (Random)");
        carTransferSearchPage.toggleForLaterOrder();

        step("user input origin and destination");
        carTransferSearchPage.choosePickups("transfer_in", airport, destination);

        step("user search for car");
        carTransferSearchPage.searchCar();

        if(Configs.PLATFORM == Platform.IOS){
            webViewPage.switchToNative();
            carTransferSearchPage.allowLocationAccess();
            webViewPage.switchToWebViewUrl("airport-transfer");
        }

        step("user select car");
        carTransferSearchResultPage.selectCar();

        step("user select vendor");
        carTransferSearchResultPage.selectVendor();

        step("user see price detail in car detail page");
        carTransferSearchResultPage.openPriceDetail();
        Assertion.assertThat("ORIGIN IS VALID", carTransferSearchResultPage.verifyOrigin(destination) , containsString(destination));
        Assertion.assertThat("DESTINATION IS VALID", carTransferSearchResultPage.verifyDestination(airport) , containsString(airport));
        carTransferSearchResultPage.closePriceDetail();

        step("user continue to booking page");
        carTransferSearchResultPage.continueToBookingPage();

        step("user continue to payment list page");
        carTransferBookingPage.continueToPayment();
        webViewPage.switchToNative();

        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",!orderID.isEmpty(),is(true));
    }

    @Author(names = {"Dendi","Zia", "Aditi"})
    @Module(name = "CarTransfersCompleteBooking")
    @Tribe(name = "CarTransport")
    @Vertical(name = "GroundTransport")
    @Test(dataProvider = "testCTInGuestDataProvider")
    public void testCTInGuest(Map<String, String> testProperties) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(testProperties);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        CarTransferSearchPage<WebDriver> carTransferSearchPage = CarTransferSearchPage.getInstance(session);
        CarTransferSearchResultPage<WebDriver> carTransferSearchResultPage = CarTransferSearchResultPage.getInstance(session);
        CarTransferBookingPage<WebDriver> carTransferBookingPage = CarTransferBookingPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);

        String airport = testProperties.get("airport");
        String destination = testProperties.get("destination");
        String email = testProperties.get("email");
        String phoneNumber = testProperties.get("phoneNumber");
        String currency = testProperties.get("currency");

        step("User open app");
        homePage.gotoHome();

        step("Logout via deeplink");
        homePage.logoutViaDeeplink();

        if(Configs.ENVIRONMENT == Environment.PROD) {
            step("Change currency to 'IDR'");
            homePage.changeCurrency("IDR","");
        }

        step("Open Car Landing Page");
        homePage.openVerticalPageDeeplink("Car Transfers", "", currency);
        carTransferSearchPage.allowLocationAccess();
       webViewPage.switchToWebViewUrl("airport-transfer");
        carTransferSearchPage.closePopups();

        step("User choose car transfer in");
        carTransferSearchPage.chooseTransferType("transfer_in");

        step("select date 1-3 (Random)");
        carTransferSearchPage.toggleForLaterOrder();

        step("user input origin and destination");
        carTransferSearchPage.choosePickups("transfer_in", airport, destination);

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

        step("user see price detail in car detail page");
        carTransferSearchResultPage.openPriceDetail();
        Assertion.assertThat("ORIGIN IS VALID", carTransferSearchResultPage.verifyOrigin(destination) , containsString(destination));
        Assertion.assertThat("DESTINATION IS VALID", carTransferSearchResultPage.verifyDestination(airport) , containsString(airport));
        carTransferSearchResultPage.closePriceDetail();

        step("user continue to booking page");
        carTransferSearchResultPage.continueToBookingPage();

        step("user fill contact detail in booking page");
        carTransferBookingPage.fillDetailOrder(email,phoneNumber);

        step("user continue to payment list page");
        carTransferBookingPage.continueToPayment();
        webViewPage.switchToNative();
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",!orderID.isEmpty(),is(true));
    }

    @Author(names = {"Zia", "Aditi"})
    @Module(name = "Destination")
    @Tribe(name = "CarTransfer")
    @Vertical(name = "GroundTransport")
    @Test(dataProvider = "testCheckDestinationGuestDataProvider")
    public void testCheckDestinationGuest(Map<String, String> testProperties) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(testProperties);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        CarTransferSearchPage<WebDriver> carTransferSearchPage = CarTransferSearchPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);

        String airport = testProperties.get("airport");
        String destination = testProperties.get("destination");
        String currency = testProperties.get("currency");

        step("User open app");
        homePage.gotoHome();

        step("Logout via deeplink");
        homePage.logoutViaDeeplink();

        if(Configs.ENVIRONMENT == Environment.PROD) {
            step("Change currency to 'IDR'");
            homePage.changeCurrency("IDR","");
        }

        step("Open Car Landing Page");
        homePage.openVerticalPageDeeplink("Car Transfers", "", currency);
        carTransferSearchPage.allowLocationAccess();
       webViewPage.switchToWebViewUrl("airport-transfer");
        carTransferSearchPage.closePopups();

        step("User choose car transfer out");
        carTransferSearchPage.chooseTransferType("transfer_out");

        step("user input origin and destination");
        carTransferSearchPage.choosePickups("transfer_out", airport, destination);

        step("verify destination");
        Assertion.assertThat("Destination correct",carTransferSearchPage.checkDestinationMatch(destination), containsString(destination));
    }

    @Author(names = {"Zia"})
    @Module(name = "Destination")
    @Tribe(name = "CarTransport")
    @Vertical(name = "GroundTransport")
    @Test(dataProvider = "testCheckDestinationLoginDataProvider")
    public void testCheckDestinationLogin(Map<String, String> testProperties) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(testProperties);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        CarTransferSearchPage<WebDriver> carTransferSearchPage = CarTransferSearchPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);

        String airport = testProperties.get("airport");
        String destination = testProperties.get("destination");
        String currency = testProperties.get("currency");
        String loginMethod = testProperties.get("loginType");
        String username = testProperties.get("username");
        String password = testProperties.get("password");

        step("User open app");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink (if needed) and change currency to " + currency);
        homePage.loginIfLoggedOutAndChangeCurrencyViaDeeplink(username, password, currency);

        step("Open Car Landing Page");
        homePage.openVerticalPageDeeplink("Car Transfers", "", currency);
        carTransferSearchPage.allowLocationAccess();
       webViewPage.switchToWebViewUrl("airport-transfer");
        carTransferSearchPage.closePopups();

        step("User choose car transfer out");
        carTransferSearchPage.chooseTransferType("transfer_out");

        step("user input origin and destination");
        carTransferSearchPage.choosePickups("transfer_out", airport, destination);

        step("verify destination");
        Assertion.assertThat("Destination correct",carTransferSearchPage.checkDestinationMatch(destination), containsString(destination));
    }


    @Author(names = {"Ashish","Zia", "Aditi"})
    @Module(name = "CarTransfersCheckRecentlySearched")
    @Tribe(name = "CarTransport")
    @Vertical(name = "GroundTransport")
    @Test(dataProvider = "testCarTransfersCheckRecentlySearchedLoginDataProvider")
    public void testCarTransfersCheckRecentlySearchedLogin(Map<String, String> testProperties) throws InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(testProperties);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        CarTransferSearchPage<WebDriver> carTransferSearchPage = CarTransferSearchPage.getInstance(session);
        CarTransferSearchResultPage<WebDriver> carTransferSearchResultPage = CarTransferSearchResultPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);

        String username = testProperties.get("username");
        String password = testProperties.get("password");
        String fullAirport = testProperties.get("fullAirport");
        String fullDestination = testProperties.get("fullDestination");
        String currency = testProperties.get("currency");

        step("User open app");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink (if needed) and change currency to " + currency);
        homePage.loginIfLoggedOutAndChangeCurrencyViaDeeplink(username, password, currency);

        step("Open Car Landing Page");
        homePage.openVerticalPageDeeplink("Car Transfers", "", currency);
        carTransferSearchPage.allowLocationAccess();
        webViewPage.switchToWebViewUrl("airport-transfer");
        carTransferSearchPage.closePopups();

        step("clear recent search");
        carTransferSearchPage.clearRecentSearch();

        step("User choose car transfer out");
        carTransferSearchPage.chooseTransferType("transfer_out");

        step("user input origin and destination");
        carTransferSearchPage.choosePickups("transfer_out", fullAirport, fullDestination);

        step("select date 1-3 (Random)");
        carTransferSearchPage.toggleForLaterOrder();

        step("user search for car");
        carTransferSearchPage.searchCar();

        if(Configs.PLATFORM == Platform.IOS){
            webViewPage.switchToNative();
            carTransferSearchPage.allowLocationAccess();
            webViewPage.switchToWebViewUrl("airport-transfer");
        }

        step("verify we are on SRP or product page");
        boolean areWeOnSRP1 = carTransferSearchResultPage.areWeOnSRP();
        Assertion.assertThat("are we on SRP or product page",areWeOnSRP1,is(true));

        step("back to search landing page");
        carTransferSearchResultPage.clickBackBtn();

        step("verify latest recent search inside the destination field");
        carTransferSearchPage.verifyRecentSearch("transfer_out", fullDestination);

        step("pick the recent search and search again to go to srp");
        carTransferSearchPage.clickRecentSearch();
        carTransferSearchPage.searchCar();

        step("verify we are on SRP or product page");
        boolean areWeOnSRP2 = carTransferSearchResultPage.areWeOnSRP();
        Assertion.assertThat("are we on SRP or product page",areWeOnSRP2,is(true));
    }

    @Author(names = {"Ashish","Zia", "Aditi"})
    @Module(name = "CarTransfersCheckRecentlySearched")
    @Tribe(name = "CarTransport")
    @Vertical(name = "GroundTransport")
    @Test(dataProvider = "testCarTransfersCheckRecentlySearchedGuestDataProvider")
    public void testCarTransfersCheckRecentlySearchedGuest(Map<String, String> testProperties) throws InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(testProperties);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        CarTransferSearchPage<WebDriver> carTransferSearchPage = CarTransferSearchPage.getInstance(session);
        CarTransferSearchResultPage<WebDriver> carTransferSearchResultPage = CarTransferSearchResultPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);

        String fullAirport = testProperties.get("fullAirport");
        String fullDestination = testProperties.get("fullDestination");
        String currency = testProperties.get("currency");

        step("User open app");
        homePage.gotoHome();

        step("Logout via deeplink");
        homePage.logoutViaDeeplink();

        step("Open Car Landing Page");
        homePage.openVerticalPageDeeplink("Car Transfers", "", currency);
        carTransferSearchPage.allowLocationAccess();
       webViewPage.switchToWebViewUrl("airport-transfer");
        carTransferSearchPage.closePopups();

        step("clear recent search");
        carTransferSearchPage.clearRecentSearch();

        step("User choose car transfer out");
        carTransferSearchPage.chooseTransferType("transfer_out");

        step("user input origin and destination");
        carTransferSearchPage.choosePickups("transfer_out", fullAirport, fullDestination);

        step("select date 1-3 (Random)");
        carTransferSearchPage.toggleForLaterOrder();

        step("user search for car");
        carTransferSearchPage.searchCar();

        if(Configs.PLATFORM == Platform.IOS){
            webViewPage.switchToNative();
            carTransferSearchPage.allowLocationAccess();
            webViewPage.switchToWebViewUrl("airport-transfer");
        }

        step("verify we are on SRP or product page");
        boolean areWeOnSRP1 = carTransferSearchResultPage.areWeOnSRP();
        Assertion.assertThat("are we on SRP or product page",areWeOnSRP1,is(true));

        step("back to search landing page");
        carTransferSearchResultPage.clickBackBtn();

        step("verify latest recent search inside the destination field");
        carTransferSearchPage.verifyRecentSearch("transfer_out", fullDestination);

        step("pick the recent search and search again to go to srp");
        carTransferSearchPage.clickRecentSearch();
        carTransferSearchPage.searchCar();

        step("verify we are on SRP or product page");
        boolean areWeOnSRP2 = carTransferSearchResultPage.areWeOnSRP();
        Assertion.assertThat("are we on SRP or product page",areWeOnSRP2,is(true));
    }

    @Author(names = {"Ashish", "Aditi"})
    @Module(name = "CheckAirport")
    @Tribe(name = "CarTransport")
    @Vertical(name = "GroundTransport")
    @Test(dataProvider = "testCheckAirportLoginDataProvider")
    public void testCheckAirportLogin(Map<String, String> testProperties) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(testProperties);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        CarTransferSearchPage<WebDriver> carTransferSearchPage = CarTransferSearchPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);

        String itaCode = testProperties.get("itaCode");
        String airportName = testProperties.get("airportName");
        String region = testProperties.get("region");
        String currency = testProperties.get("currency");
        String loginMethod = testProperties.get("loginType");
        String username = testProperties.get("username");
        String password = testProperties.get("password");

        step("User open app");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink (if needed) and change currency to " + currency);
        homePage.loginIfLoggedOutAndChangeCurrencyViaDeeplink(username, password, currency);

        step("Open Car Landing Page");
        homePage.openVerticalPageDeeplink("Car Transfers", "en", currency);
        carTransferSearchPage.allowLocationAccess();
       webViewPage.switchToWebViewUrl("airport-transfer");
        carTransferSearchPage.closePopups();

        step("User choose car transfer out");
        carTransferSearchPage.chooseTransferType("transfer_out");

        step("user click on select airport");
        carTransferSearchPage.clickAirportSearch();

        step("verify popular locations for airport");
        boolean isNearestLocationAvailableAirport = carTransferSearchPage.verifyNearestLocations();
        Assertion.assertThat("is nearest location of airport available",isNearestLocationAvailableAirport,is(true));

        step("verify airport");
        Assertion.assertThat("airport correct",carTransferSearchPage.checkAirport(itaCode, airportName, region),is(true));
    }

    @Author(names = {"Ashish", "Aditi"})
    @Module(name = "CheckAirport")
    @Tribe(name = "CarTransport")
    @Vertical(name = "GroundTransport")
    @Test(dataProvider = "testCheckAirportGuestDataProvider")
    public void testCheckAirportGuest(Map<String, String> testProperties) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(testProperties);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        CarTransferSearchPage<WebDriver> carTransferSearchPage = CarTransferSearchPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);

        String itaCode = testProperties.get("itaCode");
        String airportName = testProperties.get("airportName");
        String region = testProperties.get("region");
        String currency = testProperties.get("currency");

        step("User open app");
        homePage.gotoHome();

        step("Logout via deeplink");
        homePage.logoutViaDeeplink();

        if(Configs.ENVIRONMENT == Environment.PROD) {
            step("Change currency to 'IDR'");
            homePage.changeCurrency("IDR","");
        }

        step("Open Car Landing Page");
        homePage.openVerticalPageDeeplink("Car Transfers", "", currency);
        carTransferSearchPage.allowLocationAccess();
       webViewPage.switchToWebViewUrl("airport-transfer");
        carTransferSearchPage.closePopups();

        step("User choose car transfer out");
        carTransferSearchPage.chooseTransferType("transfer_out");

        step("user click on select airport");
        carTransferSearchPage.clickAirportSearch();

        step("verify popular locations for airport");
        boolean isPopularLocationAvailableAirport = carTransferSearchPage.verifyNearestLocations();
        Assertion.assertThat("is popular location of airport available",isPopularLocationAvailableAirport,is(true));

        step("verify airport");
        Assertion.assertThat("airport correct",carTransferSearchPage.checkAirport(itaCode, airportName, region),is(true));
    }

    @Author(names = {"Ashish"})
    @Module(name = "CheckTripDetailOnBookingForm")
    @Tribe(name = "CarTransport")
    @Vertical(name = "GroundTransport")
    @Test(dataProvider = "testCheckTripDetailOnBookingFormLoginDataProvider")
    public void testCheckTripDetailOnBookingFormLogin(Map<String, String> testProperties) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(testProperties);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        CarTransferSearchPage<WebDriver> carTransferSearchPage = CarTransferSearchPage.getInstance(session);
        CarTransferSearchResultPage<WebDriver> carTransferSearchResultPage = CarTransferSearchResultPage.getInstance(session);
        CarTransferBookingPage<WebDriver> carTransferBookingPage = CarTransferBookingPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);

        String loginMethod = testProperties.get("loginType");
        String username = testProperties.get("username");
        String password = testProperties.get("password");
        String airport = testProperties.get("airport");
        String destination = testProperties.get("destination");
        String currency = testProperties.get("currency");
        String transferType = testProperties.get("transferType");

        TripDetails tripDetails = new TripDetails();
        tripDetails.transferType = transferType;
        step("User open app");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink (if needed) and change currency to " + currency);
        homePage.loginIfLoggedOutAndChangeCurrencyViaDeeplink(username, password, currency);

        step("Open Car Landing Page");
        homePage.openVerticalPageDeeplink("Car Transfers", "", currency);
        carTransferSearchPage.allowLocationAccess();
       webViewPage.switchToWebViewUrl("airport-transfer");
        carTransferSearchPage.closePopups();

        step("User choose car transfer out");
        carTransferSearchPage.chooseTransferType(transferType);

        step("user input origin and destination");
        carTransferSearchPage.choosePickups(transferType, airport, destination);

        step("select date 1-3 (Random)");
        carTransferSearchPage.toggleForLaterOrder();

        step("get time search page");
        String time = carTransferSearchPage.getTimeSearchPage();

        step("user search for car");
        carTransferSearchPage.searchCar();

        if(Configs.PLATFORM == Platform.IOS){
            webViewPage.switchToNative();
            carTransferSearchPage.allowLocationAccess();
            webViewPage.switchToWebViewUrl("airport-transfer");
        }

        step("get date search page");
        String date = carTransferSearchResultPage.getDateSearchResult();

        step("user select car and vendor");
        carTransferSearchResultPage.selectCar();
        carTransferSearchResultPage.selectVendor();

        step("user see price detail in car detail page");
        String carType = carTransferSearchResultPage.getCarType();
        Assertion.assertThat("TIKET POINTS IS DISPLAYED",carTransferSearchResultPage.isTiketPointsPresent(),is(true));
        carTransferSearchResultPage.openPriceDetail();
        Assertion.assertThat("ORIGIN IS VALID", carTransferSearchResultPage.verifyOrigin(airport) , containsString(airport));
        Assertion.assertThat("DESTINATION IS VALID", carTransferSearchResultPage.verifyDestination(destination) , containsString(destination));
        Assertion.assertThat("PRICE OF CAR FARE IS PRESENT",carTransferSearchResultPage.isCarFarePresent(currency),is(true));
        Assertion.assertThat("TIKET POINTS IS DISPLAYED",carTransferSearchResultPage.isTiketPointsPresent(),is(true));
        String price = carTransferSearchResultPage.getPrice();
        Assertion.assertThat("IS TOTAL PRICE DISPLAYED",!price.isEmpty(),is(true));
        carTransferSearchResultPage.closePriceDetail();

        step("user continue to booking page");
        carTransferSearchResultPage.continueToBookingPage();

        step("user see trip detail on booking form");
        carTransferBookingPage.openPriceDetail();
        Assertion.assertThat("ORIGIN IN BOOKING PAGE IS VALID", carTransferBookingPage.verifyOrigin(airport) , containsString(airport));
        Assertion.assertThat("DESTINATION IN BOOKING PAGE IS VALID", carTransferBookingPage.verifyDestination(destination) , containsString(destination));
        Assertion.assertThat("Price from SRP is the same with price from booking form", carTransferBookingPage.verifyPriceDetail(price,carType), is(true));
        carTransferBookingPage.closePriceDetail();
        Assertion.assertThat("Date from SRP is the same with date from booking form", carTransferBookingPage.verifyDate(date), is(date));
        Assertion.assertThat("Time from SRP is the same with time from booking form", carTransferBookingPage.verifyTime(time), containsString(time));
    }

    @Author(names = {"Ashish", "Aditi"})
    @Module(name = "CheckTripDetailOnBookingForm")
    @Tribe(name = "CarTransport")
    @Vertical(name = "GroundTransport")
    @Test(dataProvider = "testCheckTripDetailOnBookingFormGuestDataProvider")
    public void testCheckTripDetailOnBookingFormGuest(Map<String, String> testProperties) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(testProperties);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        CarTransferSearchPage<WebDriver> carTransferSearchPage = CarTransferSearchPage.getInstance(session);
        CarTransferSearchResultPage<WebDriver> carTransferSearchResultPage = CarTransferSearchResultPage.getInstance(session);
        CarTransferBookingPage<WebDriver> carTransferBookingPage = CarTransferBookingPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);

        String airport = testProperties.get("airport");
        String destination = testProperties.get("destination");
        String currency = testProperties.get("currency");
        String transferType = testProperties.get("transferType");

        TripDetails tripDetails = new TripDetails();
        tripDetails.transferType = transferType;

        step("User open app");
        homePage.gotoHome();

        step("Logout via deeplink");
        homePage.logoutViaDeeplink();

        step("Open Car Landing Page");
        homePage.openVerticalPageDeeplink("Car Transfers", "", currency);
        carTransferSearchPage.allowLocationAccess();
       webViewPage.switchToWebViewUrl("airport-transfer");
        carTransferSearchPage.closePopups();

        step("User choose car transfer out");
        carTransferSearchPage.chooseTransferType(transferType);

        step("user input origin and destination");
        carTransferSearchPage.choosePickups(transferType, airport, destination);

        step("select date 1-3 (Random)");
        carTransferSearchPage.toggleForLaterOrder();

        step("get time search page");
        String time = carTransferSearchPage.getTimeSearchPage();

        step("user search for car");
        carTransferSearchPage.searchCar();

        if(Configs.PLATFORM == Platform.IOS){
            webViewPage.switchToNative();
            carTransferSearchPage.allowLocationAccess();
            webViewPage.switchToWebViewUrl("airport-transfer");
        }

        step("get date search page");
        String date = carTransferSearchResultPage.getDateSearchResult();

        step("user select car and vendor");
        carTransferSearchResultPage.selectCar();
        carTransferSearchResultPage.selectVendor();

        step("user see price detail in car detail page");
        String carType = carTransferSearchResultPage.getCarType();
        carTransferSearchResultPage.openPriceDetail();
        Assertion.assertThat("ORIGIN IS VALID", carTransferSearchResultPage.verifyOrigin(airport) , containsString(airport));
        Assertion.assertThat("DESTINATION IS VALID", carTransferSearchResultPage.verifyDestination(destination) , containsString(destination));
        Assertion.assertThat("PRICE OF CAR FARE IS PRESENT",carTransferSearchResultPage.isCarFarePresent(currency),is(true));
        String price = carTransferSearchResultPage.getPrice();
        Assertion.assertThat("IS TOTAL PRICE DISPLAYED",!price.isEmpty(),is(true));
        carTransferSearchResultPage.closePriceDetail();

        step("user continue to booking page");
        carTransferSearchResultPage.continueToBookingPage();

        step("user see trip detail on booking form");
        carTransferBookingPage.openPriceDetail();
        Assertion.assertThat("ORIGIN IN BOOKING PAGE IS VALID", carTransferBookingPage.verifyOrigin(airport) , containsString(airport));
        Assertion.assertThat("DESTINATION IN BOOKING PAGE IS VALID", carTransferBookingPage.verifyDestination(destination) , containsString(destination));
        Assertion.assertThat("Price from SRP is the same with price from booking form", carTransferBookingPage.verifyPriceDetail(price,carType), is(true));
        carTransferBookingPage.closePriceDetail();
        Assertion.assertThat("Date from SRP is the same with date from booking form", carTransferBookingPage.verifyDate(date), is(date));
        Assertion.assertThat("Time from SRP is the same with time from booking form", carTransferBookingPage.verifyTime(time), containsString(time));
    }

    @Author(names = {"Ashish","Zia", "Aditi"})
    @Module(name = "CheckPriceDetailOnBookingFormAT")
    @Tribe(name = "CarTransport")
    @Vertical(name = "GroundTransport")
    @Test(dataProvider = "testATCheckPriceDetailOnBookingFormGuestDataProvider")
    public void testATCheckPriceDetailOnBookingFormGuest(Map<String, String> testProperties) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(testProperties);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        CarTransferSearchPage<WebDriver> carTransferSearchPage = CarTransferSearchPage.getInstance(session);
        CarTransferSearchResultPage<WebDriver> carTransferSearchResultPage = CarTransferSearchResultPage.getInstance(session);
        CarTransferBookingPage<WebDriver> carTransferBookingPage = CarTransferBookingPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);

        String airport = testProperties.get("airport");
        String destination = testProperties.get("destination");
        String currency = testProperties.get("currency");
        String transferType = testProperties.get("transferType");
        
        step("User open app");
        homePage.gotoHome();

        step("User input credential");
        loginPage.loginAsGuest();

        if(Configs.ENVIRONMENT == Environment.PROD) {
            step("Change currency to 'IDR'");
            homePage.changeCurrency("IDR","");
        }

        step("Open Car Landing Page");
        homePage.openVerticalPageDeeplink("Car Transfers", "", currency);
        carTransferSearchPage.allowLocationAccess();
       webViewPage.switchToWebViewUrl("airport-transfer");
        carTransferSearchPage.closePopups();

        step("User choose car transfer out");
        carTransferSearchPage.chooseTransferType("transfer_out");

        step("user input origin and destination");
        carTransferSearchPage.choosePickups(transferType, airport, destination);

        step("user search for car");
        carTransferSearchPage.searchCar();

        if(Configs.PLATFORM == Platform.IOS){
            webViewPage.switchToNative();
            carTransferSearchPage.allowLocationAccess();
            webViewPage.switchToWebViewUrl("airport-transfer");
        }

        step("Select car");
        carTransferSearchResultPage.selectCar();

        step("Select vendor");
        carTransferSearchResultPage.selectVendor();

        step("user see price detail in car detail page");
        String carType = carTransferSearchResultPage.getCarType();
        carTransferSearchResultPage.openPriceDetail();
        Assertion.assertThat("PRICE OF CAR FARE IS PRESENT",carTransferSearchResultPage.isCarFarePresent(currency),is(true));
        carTransferSearchResultPage.closePriceDetail();
        Assertion.assertThat("TIKET POINTS DISPLAYED",carTransferSearchResultPage.isTiketPointsPresent(),is(true));
        String price = carTransferSearchResultPage.getPrice();
        Assertion.assertThat("IS TOTAL PRICE DISPLAYED",!price.isEmpty(),is(true));
        Assertion.assertThat("IS CAR TYPE DISPLAYED",!carType.isEmpty(),is(true));

        step("user continue to booking page");
        carTransferSearchResultPage.continueToBookingPage();

        step("user see price detail in booking page");
        carTransferBookingPage.openPriceDetail();
        Assertion.assertThat("ORIGIN IN BOOKING PAGE IS VALID", carTransferBookingPage.verifyOrigin(airport) , containsString(airport));
        Assertion.assertThat("DESTINATION IN BOOKING PAGE IS VALID", carTransferBookingPage.verifyDestination(destination) , containsString(destination));
        Assertion.assertThat("Price from SRP is the same with price from booking form", carTransferBookingPage.verifyPriceDetail(price,carType), is(true));
        carTransferBookingPage.closePriceDetail();
    }

    @Author(names = {"Ashish","Zia"})
    @Module(name = "CheckPriceDetailOnBookingFormAT")
    @Tribe(name = "CarTransport")
    @Vertical(name = "GroundTransport")
    @Test(dataProvider = "testATCheckPriceDetailOnBookingFormLoginDataProvider")
    public void testATCheckPriceDetailOnBookingFormLogin(Map<String, String> testProperties) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(testProperties);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        CarTransferSearchPage<WebDriver> carTransferSearchPage = CarTransferSearchPage.getInstance(session);
        CarTransferSearchResultPage<WebDriver> carTransferSearchResultPage = CarTransferSearchResultPage.getInstance(session);
        CarTransferBookingPage<WebDriver> carTransferBookingPage = CarTransferBookingPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);

        String loginMethod = testProperties.get("loginType");
        String username = testProperties.get("username");
        String password = testProperties.get("password");
        String airport = testProperties.get("airport");
        String destination = testProperties.get("destination");
        String currency = testProperties.get("currency");
        String transferType = testProperties.get("transferType");

        step("User open app");
        homePage.gotoHome();

        step("User input credential");
        loginPage.loginWith(loginMethod,username,password);

        step("Open Car Landing Page");
        homePage.openVerticalPageDeeplink("Car Transfers", "", currency);
        carTransferSearchPage.allowLocationAccess();
       webViewPage.switchToWebViewUrl("airport-transfer");
        carTransferSearchPage.closePopups();

        step("User choose car transfer out");
        carTransferSearchPage.chooseTransferType("transfer_out");

        step("user input origin and destination");
        carTransferSearchPage.choosePickups(transferType, airport, destination);

        step("user search for car");
        carTransferSearchPage.searchCar();

        if(Configs.PLATFORM == Platform.IOS){
            webViewPage.switchToNative();
            carTransferSearchPage.allowLocationAccess();
            webViewPage.switchToWebViewUrl("airport-transfer");
        }

        step("Select car");
        carTransferSearchResultPage.selectCar();

        step("Select vendor");
        carTransferSearchResultPage.selectVendor();

        step("user see price detail in car detail page");
        String carType = carTransferSearchResultPage.getCarType();
        carTransferSearchResultPage.openPriceDetail();
        Assertion.assertThat("PRICE OF CAR FARE IS PRESENT",carTransferSearchResultPage.isCarFarePresent(currency),is(true));
        carTransferSearchResultPage.closePriceDetail();
        Assertion.assertThat("TIKET POINTS DISPLAYED",carTransferSearchResultPage.isTiketPointsPresent(),is(true));
        String price = carTransferSearchResultPage.getPrice();
        Assertion.assertThat("IS TOTAL PRICE DISPLAYED",!price.isEmpty(),is(true));
        Assertion.assertThat("IS CAR TYPE DISPLAYED",!carType.isEmpty(),is(true));

        step("user continue to booking page");
        carTransferSearchResultPage.continueToBookingPage();

        step("user see price detail in booking page");
        carTransferBookingPage.openPriceDetail();
        Assertion.assertThat("ORIGIN IN BOOKING PAGE IS VALID", carTransferBookingPage.verifyOrigin(airport) , containsString(airport));
        Assertion.assertThat("DESTINATION IN BOOKING PAGE IS VALID", carTransferBookingPage.verifyDestination(destination) , containsString(destination));
        Assertion.assertThat("Price from SRP is the same with price from booking form", carTransferBookingPage.verifyPriceDetail(price,carType), is(true));
        carTransferBookingPage.closePriceDetail();
    }


    @Author(names = {"Dendi", "Aditi"})
    @Module(name = "payAtWithCC")
    @Tribe(name = "CarTransport")
    @Vertical(name = "GroundTransport")
    @Test
    public void testPayAtWithCCLogin(){
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String,String> testProperties = getParam("testPayAtWithCCLogin");
        BaseTest.paramsThreadLocal.set(testProperties);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        CarTransferSearchPage<WebDriver> carTransferSearchPage = CarTransferSearchPage.getInstance(session);
        CarTransferSearchResultPage<WebDriver> carTransferSearchResultPage = CarTransferSearchResultPage.getInstance(session);
        CarTransferBookingPage<WebDriver> carTransferBookingPage = CarTransferBookingPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);

        String loginMethod = testProperties.get("loginType");
        String username = testProperties.get("username");
        String password = testProperties.get("password");
        String airport = testProperties.get("airport");
        String destination = testProperties.get("destination");
        String currency = testProperties.get("currency");
        String transferType = testProperties.get("transferType");
        String cardNumber = testProperties.get("cardNumber");
        String expiry = testProperties.get("expiry");
        String cvv = testProperties.get("cvv");
        String cardName = testProperties.get("cardName");
        String payment_method = testProperties.get("paymentMethod");

        step("User open app");
        homePage.gotoHome();

        step("User input credential");
        loginPage.loginWith(loginMethod, username, password);
        Assertion.assertThat("Login success ", true, is(true));

        step("User open car transfer vertical page");
        homePage.openVerticalPageDeeplink("Car Transfers", "", currency);
        carTransferSearchPage.allowLocationAccess();
       webViewPage.switchToWebViewUrl("airport-transfer");
        carTransferSearchPage.closePopups();

        step("User choose car transfer out");
        carTransferSearchPage.chooseTransferType(transferType);

        step("user input origin and destination");
        carTransferSearchPage.choosePickups(transferType, airport, destination);

        step("user search for car");
        carTransferSearchPage.searchCar();

        if(Configs.PLATFORM == Platform.IOS){
            webViewPage.switchToNative();
            carTransferSearchPage.allowLocationAccess();
            webViewPage.switchToWebViewUrl("airport-transfer");
        }

        step("Select car");
        carTransferSearchResultPage.selectCar();

        step("Select vendor");
        carTransferSearchResultPage.selectVendor();

        step("Verify price in PDP is in IDR");
        carTransferSearchResultPage.verifyPricePdpInIDR();

        step("Continue to booking page");
        carTransferSearchResultPage.continueToBookingPage();

        step("Verify price in booking page");
        Assertion.assertThat("Verify price detail are all in IDR", carTransferBookingPage.verifyPriceDetailIsInIDR(), containsString("IDR"));
        
        step("user continue to payment list page");
        carTransferBookingPage.continueToPayment();
        webViewPage.switchToNative();
        String expectedOrderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",!expectedOrderID.isEmpty(),is(true));

        step("user choose pay with cc");
        paymentPage.openPaymentMethod();
        paymentPage.selectPaymentMethod(payment_method);

        step("user fill cc detail");
        paymentPage.fillCCDetailRealIssued(cardNumber, expiry, cvv, cardName);
        paymentPage.clickPayButtonCC();
        paymentPage.verifyCreditCard3ds();

        step("Open the order list and go to order detail");
        paymentPage.seeOrderList();
        orderListPage.openActiveTicketViaDeeplink(expectedOrderID);
        orderListPage.removePopUp();

        step("Assertion");
        Assertion.assertThat("order id is match",orderDetailPage.getFullOrderId(),containsString(expectedOrderID));
    }


    @Author(names = {"Dendi", "Aditi"})
    @Module(name = "payAtWithCC")
    @Tribe(name = "CarTransport")
    @Vertical(name = "GroundTransport")
    @Test
    public void testPayAtWithCCGuest(){
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String,String> testProperties = getParam("testPayAtWithCCGuest");
        BaseTest.paramsThreadLocal.set(testProperties);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        CarTransferSearchPage<WebDriver> carTransferSearchPage = CarTransferSearchPage.getInstance(session);
        CarTransferSearchResultPage<WebDriver> carTransferSearchResultPage = CarTransferSearchResultPage.getInstance(session);
        CarTransferBookingPage<WebDriver> carTransferBookingPage = CarTransferBookingPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);

        String airport = testProperties.get("airport");
        String destination = testProperties.get("destination");
        String currency = testProperties.get("currency");
        String transferType = testProperties.get("transferType");
        String cardNumber = testProperties.get("cardNumber");
        String expiry = testProperties.get("expiry");
        String cvv = testProperties.get("cvv");
        String cardName = testProperties.get("cardName");
        String payment_method = testProperties.get("paymentMethod");
        String email = testProperties.get("username");
        String phoneNumber =testProperties.get("phoneNumber");

        step("User open app");
        homePage.gotoHome();

        step("login as guest");
        loginPage.loginAsGuest();

        step("Open Car Landing Page");
        homePage.openVerticalPageDeeplink("Car Transfers", "", "IDR");
        carTransferSearchPage.allowLocationAccess();
       webViewPage.switchToWebViewUrl("airport-transfer");
        carTransferSearchPage.closePopups();

        step("User choose car transfer out");
        carTransferSearchPage.chooseTransferType("transfer_out");

        step("user input origin and destination");
        carTransferSearchPage.choosePickups(transferType, airport, destination);

        step("user search for car");
        carTransferSearchPage.searchCar();

        if(Configs.PLATFORM == Platform.IOS){
            webViewPage.switchToNative();
            carTransferSearchPage.allowLocationAccess();
            webViewPage.switchToWebViewUrl("airport-transfer");
        }

        step("Select car");
        carTransferSearchResultPage.selectCar();

        step("Select vendor");
        carTransferSearchResultPage.selectVendor();

        step("Verify price in PDP is in IDR");
        carTransferSearchResultPage.verifyPricePdpInIDR();

        step("Continue to booking page");
        carTransferSearchResultPage.continueToBookingPage();

        step("Verify price in booking page");
        Assertion.assertThat("Verify price detail are all in IDR", carTransferBookingPage.verifyPriceDetailIsInIDR(), containsString("IDR"));

        step("user continue to payment list page");
        carTransferBookingPage.fillDetailOrder(email,phoneNumber);

        step("user continue to payment list page");
        carTransferBookingPage.continueToPayment();
        webViewPage.switchToNative();
        String expectedOrderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",!expectedOrderID.isEmpty(),is(true));

        step("user choose pay with cc");
        paymentPage.openPaymentMethod();
        paymentPage.selectPaymentMethod(payment_method);

        step("user fill cc detail");
        paymentPage.fillCCDetailRealIssued(cardNumber, expiry, cvv, cardName);
        paymentPage.clickPayButtonCC();
        paymentPage.verifyCreditCard3ds();

        step("Open the order list and go to order detail");
        paymentPage.seeOrderList();
        orderListPage.openActiveTicketViaDeeplink(expectedOrderID);
        orderListPage.removePopUp();

        step("Assertion");
        Assertion.assertThat("order id is match",orderDetailPage.getFullOrderId(),containsString(expectedOrderID));
    }

    @Author(names = {"Dendi", "Aditi"})
    @Module(name = "homePageWithoutCarTransferNonIDR")
    @Tribe(name = "CarRental")
    @Vertical(name = "GroundTransport")
    @Test
    public void testVerifyHomePageWithoutCarTransferNonIDRLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testVerifyHomePageWithoutCarTransferNonIDRLogin");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);

        String loginMethod = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String currency = param.get("currency");

        step("user open app");
        homePage.gotoHome();

        step("user login using "+username+" account");
        loginPage.loginWith(loginMethod, username, password);

        step("Change currency");
        homePage.changeCurrency(currency,username);

        step("Find Car Rental Vertical");
        Assertion.assertThat("is car transfers displayed in non-IDR currency {"+currency+" }",homePage.checkVerticalPresence("Car Transfers"),is(true));
    }

    @Author(names = {"Dendi", "Aditi"})
    @Module(name = "homePageWithoutCarTransferNonIDR")
    @Tribe(name = "CarRental")
    @Vertical(name = "GroundTransport")
    @Test
    public void testVerifyHomePageWithoutCarTransferNonIDRGuest() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testVerifyHomePageWithoutCarTransferNonIDRGuest");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);

        String currency = param.get("currency");

        step("user open app");
        homePage.gotoHome();

        step("user open app as guest");
        loginPage.loginAsGuest();;

        step("Change currency");
        homePage.changeCurrency(currency,"");

        step("Find Car Rental Vertical");
        Assertion.assertThat("is car transfers displayed in non-IDR currency {"+currency+" }",homePage.checkVerticalPresence("Car Transfers"),is(true));
    }

    @Author(names = {"Dendi", "Kartik", "Aditi"})
    @Module(name = "homePageWithoutCarTransferNonIDR")
    @Tribe(name = "CarRental")
    @Vertical(name = "GroundTransport")
    @Test
    public void testLoginBookAtViaDeeplinkNonIDR(){
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String,String> testProperties = getParam("testLoginBookAtViaDeeplinkNonIDR");
        BaseTest.paramsThreadLocal.set(testProperties);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        CarTransferSearchPage<WebDriver> carTransferSearchPage = CarTransferSearchPage.getInstance(session);
        CarTransferSearchResultPage<WebDriver> carTransferSearchResultPage = CarTransferSearchResultPage.getInstance(session);
        CarTransferBookingPage<WebDriver> carTransferBookingPage = CarTransferBookingPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);

        String loginMethod = testProperties.get("loginType");
        String username = testProperties.get("username");
        String password = testProperties.get("password");
        String airport = testProperties.get("airport");
        String destination = testProperties.get("destination");
        String currency = testProperties.get("currency");
        String transferType = testProperties.get("transferType");

        step("User open app");
        homePage.gotoHome();

        step("User input credential");
        loginPage.loginWith(loginMethod, username, password);

        step("Change currency");
        homePage.changeCurrency(currency,username);

        step("User open car transfer vertical page via deeplink");
        homePage.openVerticalPageDeeplink("Car Transfers", "", currency);
        carTransferSearchPage.allowLocationAccess();
       webViewPage.switchToWebViewUrl("airport-transfer");
        carTransferSearchPage.closePopups();

        step("User choose car transfer out");
        carTransferSearchPage.chooseTransferType(transferType);

        step("user input origin and destination");
        carTransferSearchPage.choosePickups(transferType, airport, destination);

        step("user search for car");
        carTransferSearchPage.searchCar();

        if(Configs.PLATFORM == Platform.IOS){
            webViewPage.switchToNative();
            carTransferSearchPage.allowLocationAccess();
            webViewPage.switchToWebViewUrl("airport-transfer");
        }

        step("Verify car type in srp are all in IDR");
        Assertion.assertThat("Verify price in SRP are all in IDR", carTransferSearchResultPage.verifyCarsOptionPriceInIDR(), is(true));

        step("Select car");
        carTransferSearchResultPage.selectCar();

        step("Verify vendor prices are all in IDR");
        Assertion.assertThat("Verify vendor prices are all in IDR", carTransferSearchResultPage.verifyCarVendorPricesInIDR(), is(true));

        step("Select vendor");
        carTransferSearchResultPage.selectVendor();

        step("Verify price in PDP is in IDR");
        carTransferSearchResultPage.verifyPricePdpInIDR();

        step("Continue to booking page");
        carTransferSearchResultPage.continueToBookingPage();

        step("Verify price in booking page");
        Assertion.assertThat("Verify price detail are all in IDR", carTransferBookingPage.verifyPriceDetailIsInIDR(), containsString("IDR"));

        step("continue to payment page");
        carTransferBookingPage.continueToPayment();
        webViewPage.switchToNative();

        step("Continue to payment page");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",!orderID.isEmpty(),is(true));

        step("Verify price in payment page");
        paymentPage.openPriceDetail();
        Assertion.assertThat("is price detail payment page in IDR", (paymentPage.getPriceDetail().stream().allMatch(price -> (price.contains("IDR") || price.equals("Termasuk") || price.equals("Included")))), is(true));
        Assertion.assertThat("is price total payment page in IDR", (paymentPage.getTotalPrice().contains("IDR")), is(true));
    }

    @Author(names = {"Dendi","kartik", "Aditi"})
    @Module(name = "homePageWithoutCarTransferNonIDR")
    @Tribe(name = "CarRental")
    @Vertical(name = "GroundTransport")
    @Test
    public void testGuestBookAtViaDeeplinkNonIDR(){
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String,String> testProperties = getParam("testGuestBookAtViaDeeplinkNonIDR");
        BaseTest.paramsThreadLocal.set(testProperties);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        CarTransferSearchPage<WebDriver> carTransferSearchPage = CarTransferSearchPage.getInstance(session);
        CarTransferSearchResultPage<WebDriver> carTransferSearchResultPage = CarTransferSearchResultPage.getInstance(session);
        CarTransferBookingPage<WebDriver> carTransferBookingPage = CarTransferBookingPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);

        String airport = testProperties.get("airport");
        String destination = testProperties.get("destination");
        String currency = testProperties.get("currency");
        String transferType = testProperties.get("transferType");
        String username = testProperties.get("username");
        String phoneNumber = testProperties.get("phoneNumber");

        step("User open app");
        homePage.gotoHome();

        step("User input credential");
        loginPage.loginAsGuest();

        step("Change currency");
        homePage.changeCurrency(currency,"");

        step("User open car transfer vertical page via deeplink");
        homePage.openVerticalPageDeeplink("Car Transfers", "", currency);
        carTransferSearchPage.allowLocationAccess();
       webViewPage.switchToWebViewUrl("airport-transfer");
        carTransferSearchPage.closePopups();

        step("User choose car transfer out");
        carTransferSearchPage.chooseTransferType(transferType);

        step("user input origin and destination");
        carTransferSearchPage.choosePickups(transferType, airport, destination);

        step("user search for car");
        carTransferSearchPage.searchCar();

        if(Configs.PLATFORM == Platform.IOS){
            webViewPage.switchToNative();
            carTransferSearchPage.allowLocationAccess();
            webViewPage.switchToWebViewUrl("airport-transfer");
        }

        step("Verify car type in srp are all in IDR");
        Assertion.assertThat("Verify price in SRP are all in IDR", carTransferSearchResultPage.verifyCarsOptionPriceInIDR(), is(true));

        step("Select car");
        carTransferSearchResultPage.selectCar();

        step("Verify vendor prices are all in IDR");
        Assertion.assertThat("Verify vendor prices are all in IDR", carTransferSearchResultPage.verifyCarVendorPricesInIDR(), is(true));

        step("Select vendor");
        carTransferSearchResultPage.selectVendor();

        step("Verify price in PDP is in IDR");
        carTransferSearchResultPage.verifyPricePdpInIDR();

        step("Continue to booking page");
        carTransferSearchResultPage.continueToBookingPage();

        step("Verify price in booking page");
        Assertion.assertThat("Verify price detail are all in IDR", carTransferBookingPage.verifyPriceDetailIsInIDR(), containsString("IDR"));

        step("Fill booking data");
        carTransferBookingPage.fillDetailOrder(username,phoneNumber);

        step("continue to payment page");
        carTransferBookingPage.continueToPayment();
        webViewPage.switchToNative();

        step("Continue to payment page");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",!orderID.isEmpty(),is(true));

        step("Verify price in payment page");
        paymentPage.openPriceDetail();
        Assertion.assertThat("is price detail payment page in IDR", (paymentPage.getPriceDetail().stream().allMatch(price -> (price.contains("IDR") || price.equals("Termasuk") || price.equals("Included")))), is(true));
        Assertion.assertThat("is price total payment page in IDR", (paymentPage.getTotalPrice().contains("IDR")), is(true));
    }

    @DataProvider(name = "testCTInGuestDataProvider")
    public Object[][] testCTInGuestDataProvider() {
        return getObjectParams("testCTInGuest");
    }

    @DataProvider(name = "testCTInLoginDataProvider")
    public Object[][] testCTInLoginDataProvider() {
        return getObjectParams("AT_testCTInLogin");
    }

    @DataProvider(name = "testCTOutGuestDataProvider")
    public Object[][] testCTOutGuestDataProvider() {
        return getObjectParams("testCTOutGuest");
    }

    @DataProvider(name = "testCTOutLoginDataProvider")
    public Object[][] testCTOutLoginDataProvider() {
        return getObjectParams("AT_testCTOutLogin");
    }

    @DataProvider(name = "testCarTransfersCheckRecentlySearchedLoginDataProvider")
    public Object[][] testCarTransfersCheckRecentlySearchedLoginDataProvider() {
        return getObjectParams("testCarTransfersCheckRecentlySearchedLogin");
    }

    @DataProvider(name = "testCarTransfersCheckRecentlySearchedGuestDataProvider")
    public Object[][] testCarTransfersCheckRecentlySearchedGuestDataProvider() {
        return getObjectParams("testCarTransfersCheckRecentlySearchedGuest");
    }

    @DataProvider(name = "testCheckDestinationGuestDataProvider")
    public Object[][] testCheckDestinationGuestDataProvider() {
        return getObjectParams("testCheckDestinationGuest");
    }

    @DataProvider(name = "testCheckDestinationLoginDataProvider")
    public Object[][] testCheckDestinationLoginDataProvider() {
        return getObjectParams("testCheckDestinationLogin");
    }

    @DataProvider(name = "testCheckAirportLoginDataProvider")
    public Object[][] testCheckAirportLoginDataProvider() {
        return getObjectParams("testCheckAirportLogin");
    }

    @DataProvider(name = "testCheckAirportGuestDataProvider")
    public Object[][] testCheckAirportGuestDataProvider() {
        return getObjectParams("testCheckAirportGuest");
    }

    @DataProvider(name = "testCheckTripDetailOnBookingFormLoginDataProvider")
    public Object[][] testCheckTripDetailOnBookingFormLoginDataProvider() {
        return getObjectParams("testCheckTripDetailOnBookingFormLogin");
    }

    @DataProvider(name = "testCheckTripDetailOnBookingFormGuestDataProvider")
    public Object[][] testCheckTripDetailOnBookingFormGuestDataProvider() {
        return getObjectParams("testCheckTripDetailOnBookingFormGuest");
    }

    @DataProvider(name = "testATCheckPriceDetailOnBookingFormGuestDataProvider")
    public Object[][] testATCheckPriceDetailOnBookingFormGuestDataProvider() {
        return getObjectParams("testATCheckPriceDetailOnBookingFormGuest");
    }

    @DataProvider(name = "testATCheckPriceDetailOnBookingFormLoginDataProvider")
    public Object[][] testATCheckPriceDetailOnBookingFormLoginDataProvider() {
        return getObjectParams("testATCheckPriceDetailOnBookingFormLogin");
    }
}