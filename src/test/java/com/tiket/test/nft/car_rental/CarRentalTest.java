package com.tiket.test.nft.car_rental;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Environment;
import com.tiket.model.Session;
import com.tiket.page.carrental.bookingpage.CarRentalBookingPage;
import com.tiket.page.carrental.reviewbookingpage.CarRentalReviewBookingPage;
import com.tiket.page.carrental.searchpage.SearchPageCarRental;
import com.tiket.page.carrental.searchresultpage.SearchResultPageCarRental;
import com.tiket.page.home.HomePage;
import com.tiket.page.login.LoginPage;
import com.tiket.page.order.orderdetail.OrderDetailPage;
import com.tiket.page.order.orderlist.OrderListPage;
import com.tiket.page.payment.PaymentPage;
import com.tiket.pojo.carrental.CarRentalDetail;
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

public class CarRentalTest extends BaseTest {

    @Author(names = {"Kartik","Ashish"})
    @Module(name = "CarCompleteBooking")
    @Tribe(name = "CarRental")
    @Vertical(name = "GroundTransport")
    @Test(dataProvider="testGuestCompleteBookingWithoutDriverAndExtraFacilitiesDataProvider")
    public void testGuestCompleteBookingWithoutDriverAndExtraFacilities(Map<String, String> params) {

        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(params);
        
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        SearchPageCarRental<WebDriver> carRentalSearchPage = SearchPageCarRental.getInstance(session);
        SearchResultPageCarRental<WebDriver> carRentalSearchResultPage = SearchResultPageCarRental.getInstance(session);
        CarRentalReviewBookingPage<WebDriver> carRentalReviewBookingPage = CarRentalReviewBookingPage.getInstance(session);
        CarRentalBookingPage<WebDriver> carRentalBookingPage = CarRentalBookingPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        LoginPage <WebDriver> loginPage = LoginPage.getInstance(session);

        String Location = params.get("location");
        String pickupLocation = params.get("pickupLocation");
        String email = params.get("email");
        String carRentalDuration = params.get("carRentalDuration");
        int duration = Integer.parseInt(carRentalDuration);
        String carRentalSpecialRequest = params.get("specialRequest");
        String dateRental = CoreFunctions.getCurrentLongDate();
        String phoneNumber = params.get("phoneNumber");
        String currency = params.get("currency");
        CarRentalDetail carRentalDetail = new CarRentalDetail();
        carRentalDetail.duration = duration;
        String driverType = "without_driver";

        step("Go To Home");
        homePage.gotoHome();

        step("Logout via deeplink");
        homePage.logoutViaDeeplink();

        if(Configs.ENVIRONMENT == Environment.PROD) {
            step("Change currency to 'IDR'");
            homePage.changeCurrency("IDR","");
        }

        step("Get App Language");
        String language = homePage.getLanguageApp();

        step("Open Car Landing Page");
        homePage.openVerticalPageDeeplink("Car Rentals", language, currency);
        carRentalSearchPage.allowLocationPermission();
        carRentalSearchPage.closeInformationPopUp();

        step("Select car rental type");
        carRentalSearchPage.chooseDriver(driverType);

        step("Select car rental date");
        carRentalSearchPage.selectDate(CoreFunctions.addToLongDate(dateRental,"10-20"));

        step("Input car rental location");
        carRentalSearchPage.chooseLocation(Location);

        step("get selected data landing page");
        carRentalDetail.landingPageData = carRentalSearchPage.getSelectedDataLandingPage();

        step("Search car rental");
        carRentalSearchPage.searchCar(carRentalDuration);

        step("choose car");
        carRentalDetail.srpData = carRentalSearchResultPage.chooseCar(duration);

        step("car rental detail assertion");
        Assertion.assertThat("check car rental review detail page",carRentalReviewBookingPage.checkCarRentalDetail(carRentalDetail),is(true));

        step("various section assertion");
        Assertion.assertThat("check rental section",carRentalReviewBookingPage.verifyVariousSectionOnThePage(driverType,carRentalDetail),is(true));

        step("verify price on review booking page");
        Assertion.assertThat("verify price detail section on review booking page",carRentalReviewBookingPage.verifyPriceDetailSection(carRentalDetail),is(true));

        step("continue to booking form from review page");
        carRentalReviewBookingPage.continueFromReviewDetailCar();

        step("pick up location");
        carRentalBookingPage.choosePickUpLocationAndToggleForDropOff(pickupLocation);

        step("add special request");
        carRentalBookingPage.addExtraFacilities(carRentalSpecialRequest);
        Assertion.assertThat("check extra facilities added", carRentalBookingPage.verifyExtraFacilities(), containsString(carRentalSpecialRequest));

        step("continue to complete booking");
        carRentalBookingPage.continueToCompleteBookingPage();

        step("verify special request booking summery");
        Assertion.assertThat("check special request on booking summery", carRentalBookingPage.getAddSpecialRequestBookingSummery(), containsString(carRentalSpecialRequest));

        step("verify driverType, pickup and drop-off time & package type(optional)");
        Assertion.assertThat("check driverType, pickup and drop-off time & package type(only for with driver)", carRentalBookingPage.verifyCarRentalDetail(carRentalDetail), is(true));
        
        step("input contact detail");
        carRentalBookingPage.fillGuestDetailOrder(email,phoneNumber);
        carRentalBookingPage.setContinueToPayment();

        step("get order id");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id { " + orderID+ " }",!orderID.isEmpty(),is(true));
    }

    @Author(names = {"Kartik"})
    @Module(name = "CarCompleteBooking")
    @Tribe(name = "CarRental")
    @Vertical(name = "GroundTransport")
    @Test(dataProvider="testGuestCompleteBookingWithoutDriverDataProvider")
    public void testGuestCompleteBookingWithoutDriver(Map<String, String> params) {

        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(params);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        SearchPageCarRental<WebDriver> carRentalSearchPage = SearchPageCarRental.getInstance(session);
        SearchResultPageCarRental<WebDriver> carRentalSearchResultPage = SearchResultPageCarRental.getInstance(session);
        CarRentalReviewBookingPage<WebDriver> carRentalReviewBookingPage = CarRentalReviewBookingPage.getInstance(session);
        CarRentalBookingPage<WebDriver> carRentalBookingPage = CarRentalBookingPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        LoginPage <WebDriver> loginPage = LoginPage.getInstance(session);

        String Location = params.get("location");
        String pickupLocation = params.get("pickupLocation");
        String email = params.get("email");
        String carRentalDuration = params.get("carRentalDuration");
        String carRentalPackage = params.get("carRentalPackage");
        String dateRental = CoreFunctions.getCurrentLongDate();
        String phoneNumber = params.get("phoneNumber");
        String currency = params.get("currency");

        step("Go To Home");
        homePage.gotoHome();

        step("User user app as guest");
        loginPage.loginAsGuest();

        if(Configs.ENVIRONMENT == Environment.PROD) {
            step("Change currency to 'IDR'");
            homePage.changeCurrency("IDR","");
        }

        step("Get App Language");
        String language = homePage.getLanguageApp();

        step("Open Car Landing Page");
        homePage.openVerticalPageDeeplink("Car Rentals", language, currency);
        carRentalSearchPage.allowLocationPermission();
        carRentalSearchPage.closeInformationPopUp();

        step("Select car rental type");
        carRentalSearchPage.chooseDriver("without_driver");

        step("Select car rental date");
        carRentalSearchPage.selectDate(CoreFunctions.addToLongDate(dateRental,"10-20"));

        step("Input car rental location");
        carRentalSearchPage.chooseLocation(Location);

        step("Searh car rental");
        carRentalSearchPage.searchCar(carRentalDuration);

        step("choose car");
        carRentalSearchResultPage.chooseCar();

        step("continue to booking form from review page");
        carRentalReviewBookingPage.continueFromReviewDetailCar();

        step("pick up location");
        carRentalBookingPage.choosePickUpLocation(pickupLocation);

        step("continue to complete booking");
        carRentalBookingPage.continueToCompleteBookingPage();

        step("input contact detail");
        carRentalBookingPage.fillGuestDetailOrder(email,phoneNumber);
        carRentalBookingPage.setContinueToPayment();

        step("get order id");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",!orderID.isEmpty(),is(true));
    }

    @Author(names = {"Kartik","Ashish"})
    @Module(name = "CarCompleteBooking")
    @Tribe(name = "CarRental")
    @Vertical(name = "GroundTransport")
    @Test(dataProvider="testGuestCompleteBookingWithDriverAndExtraFacilitiesDataProvider")
    public void testGuestCompleteBookingWithDriverAndExtraFacilities(Map<String, String> params) {

        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(params);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        SearchPageCarRental<WebDriver> carRentalSearchPage = SearchPageCarRental.getInstance(session);
        SearchResultPageCarRental<WebDriver> carRentalSearchResultPage = SearchResultPageCarRental.getInstance(session);
        CarRentalReviewBookingPage<WebDriver> carRentalReviewBookingPage = CarRentalReviewBookingPage.getInstance(session);
        CarRentalBookingPage<WebDriver> carRentalBookingPage = CarRentalBookingPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        LoginPage <WebDriver> loginPage = LoginPage.getInstance(session);

        String Location = params.get("location");
        String pickupLocation = params.get("pickupLocation");
        String email = params.get("email");
        String carRentalDuration = params.get("carRentalDuration");
        int duration = Integer.parseInt(carRentalDuration);
        String carRentalSpecialRequest = params.get("specialRequest");
        String carRentalPackage = params.get("carRentalPackage");
        String dateRental = CoreFunctions.getCurrentLongDate();
        String phoneNumber = params.get("phoneNumber");
        String currency = params.get("currency");
        String driverType = "with_driver";
        CarRentalDetail carRentalDetail = new CarRentalDetail();

        step("Go To Home");
        homePage.gotoHome();

        step("Logout via deeplink");
        homePage.logoutViaDeeplink();

        if(Configs.ENVIRONMENT == Environment.PROD) {
            step("Change currency to 'IDR'");
            homePage.changeCurrency("IDR","");
        }

        step("Get App Language");
        String language = homePage.getLanguageApp();

        step("Open Car Landing Page");
        homePage.openVerticalPageDeeplink("Car Rentals", language, currency);
        carRentalSearchPage.allowLocationPermission();
        carRentalSearchPage.closeInformationPopUp();

        step("Select car rental type");
        carRentalSearchPage.chooseDriver(driverType);

        step("Select car rental date");
        carRentalSearchPage.selectDate(CoreFunctions.addToLongDate(dateRental,"10-20"));

        step("Input car rental location");
        carRentalSearchPage.chooseLocation(Location);

        step("get selected data landing page");
        carRentalDetail.landingPageData = carRentalSearchPage.getSelectedDataLandingPage();

        step("Search car rental");
        carRentalSearchPage.searchCar(carRentalDuration);

        step("choose car");
        carRentalDetail.srpData = carRentalSearchResultPage.chooseCar(duration);

        step("car rental detail assertion");
        Assertion.assertThat("check car rental review detail page",carRentalReviewBookingPage.checkCarRentalDetail(carRentalDetail),is(true));

        step("various section assertion");
        Assertion.assertThat("check rental section",carRentalReviewBookingPage.verifyVariousSectionOnThePage(driverType,carRentalDetail),is(true));

        step("verify price on review booking page");
        Assertion.assertThat("verify price detail section on review booking page",carRentalReviewBookingPage.verifyPriceDetailSection(carRentalDetail),is(true));

        step("continue to booking form from review page");
        carRentalReviewBookingPage.continueFromReviewDetailCar();

        step("pick up location");
        carRentalBookingPage.choosePickUpLocationAndToggleForDropOff(pickupLocation);

        step("add special request");
        carRentalBookingPage.addExtraFacilities(carRentalSpecialRequest);
        Assertion.assertThat("check extra facilities added", carRentalBookingPage.verifyExtraFacilities(), containsString(carRentalSpecialRequest));

        step("continue to complete booking");
        carRentalBookingPage.continueToCompleteBookingPage();

        step("verify special request booking summery");
        Assertion.assertThat("check special request on booking summery", carRentalBookingPage.getAddSpecialRequestBookingSummery(), containsString(carRentalSpecialRequest));

        step("verify driverType, pickup and drop-off time & package type(optional)");
        Assertion.assertThat("check driverType, pickup and drop-off time & package type(only for with driver)", carRentalBookingPage.verifyCarRentalDetail(carRentalDetail), is(true));

        step("input contact detail");
        carRentalBookingPage.fillGuestDetailOrder(email,phoneNumber);
        carRentalBookingPage.setContinueToPayment();

        step("get order id");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id { " + orderID+ " }",!orderID.isEmpty(),is(true));
    }

    @Author(names = {"Budi"})
    @Module(name = "CarCompleteBooking")
    @Tribe(name = "CarRental")
    @Vertical(name = "GroundTransport")
    @Test(dataProvider="testGuestCompleteBookingWithDriverDataProvider")
    public void testGuestCompleteBookingWithDriver(Map<String, String> params) {

        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(params);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        SearchPageCarRental<WebDriver> carRentalSearchPage = SearchPageCarRental.getInstance(session);
        SearchResultPageCarRental<WebDriver> carRentalSearchResultPage = SearchResultPageCarRental.getInstance(session);
        CarRentalReviewBookingPage<WebDriver> carRentalReviewBookingPage = CarRentalReviewBookingPage.getInstance(session);
        CarRentalBookingPage<WebDriver> carRentalBookingPage = CarRentalBookingPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        LoginPage <WebDriver> loginPage = LoginPage.getInstance(session);



        String Location = params.get("location");
        String pickupLocation = params.get("pickupLocation");
        String email = params.get("email");
        String carRentalDuration = params.get("carRentalDuration");
        String carRentalPackage = params.get("carRentalPackage");
        String dateRental = CoreFunctions.getCurrentLongDate();
        String phoneNumber = params.get("phoneNumber");
        String currency = params.get("currency");

        step("Go To Home");
        homePage.gotoHome();

        step("User user app as guest");
        loginPage.loginAsGuest();

        if(Configs.ENVIRONMENT == Environment.PROD) {
            step("Change currency to 'IDR'");
            homePage.changeCurrency("IDR","");
        }

        step("Get App Language");
        String language = homePage.getLanguageApp();

        step("Open Car Landing Page");
        homePage.openVerticalPageDeeplink("Car Rentals", language, currency);
        carRentalSearchPage.allowLocationPermission();
        carRentalSearchPage.closeInformationPopUp();

        step("Select car rental type");
        carRentalSearchPage.chooseDriver("with_driver");

        step("Select car rental date");
        carRentalSearchPage.selectDate(CoreFunctions.addToLongDate(dateRental,"10-20"));

        step("Input car rental location");
        carRentalSearchPage.chooseLocation(Location);

        step("Searh car rental");
        carRentalSearchPage.searchCar(carRentalDuration);

        step("choose car");
        carRentalSearchResultPage.chooseCar();

        step("continue to booking form from review page");
        carRentalReviewBookingPage.continueFromReviewDetailCar();

        step("pick up location");
        carRentalBookingPage.choosePickUpLocation(pickupLocation);

        step("continue to complete booking");
        carRentalBookingPage.continueToCompleteBookingPage();

        step("input contact detail");
        carRentalBookingPage.fillGuestDetailOrder(email,phoneNumber);
        carRentalBookingPage.setContinueToPayment();

        step("get order id");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",!orderID.isEmpty(),is(true));
    }

    @Author(names = {"Kartik","Ashish"})
    @Module(name = "CarCompleteBooking")
    @Tribe(name = "CarRental")
    @Vertical(name = "GroundTransport")
    @Test(dataProvider="testLoginCompleteBookingWithDriverAndExtraFacilitiesDataProvider")
    public void testLoginCompleteBookingWithDriverAndExtraFacilities(Map<String,String> params){
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(params);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        SearchPageCarRental<WebDriver> carRentalSearchPage = SearchPageCarRental.getInstance(session);
        SearchResultPageCarRental<WebDriver> carRentalSearchResultPage = SearchResultPageCarRental.getInstance(session);
        CarRentalReviewBookingPage<WebDriver> carRentalReviewBookingPage = CarRentalReviewBookingPage.getInstance(session);
        CarRentalBookingPage<WebDriver> carRentalBookingPage = CarRentalBookingPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        LoginPage <WebDriver> loginPage = LoginPage.getInstance(session);


        String Location = params.get("location");
        String pickupLocation = params.get("pickupLocation");
        String carRentalDuration = params.get("carRentalDuration");
        String carRentalSpecialRequest = params.get("specialRequest");
        String loginMethod = params.get("loginType");
        String username = params.get("username");
        String password = params.get("password");
        String dateRental = CoreFunctions.getCurrentLongDate();
        String currency = params.get("currency");
        CarRentalDetail carRentalDetail = new CarRentalDetail();
        int duration = Integer.parseInt(carRentalDuration);
        String driverType = "with_driver";

        step("Go To Home");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink (if needed) and change currency to " + currency);
        homePage.loginIfLoggedOutAndChangeCurrencyViaDeeplink(username, password, currency);

        step("Get App Language");
        String language = homePage.getLanguageApp();

        step("Open Car Landing Page");
        homePage.openVerticalPageDeeplink("Car Rentals", language, currency);
        carRentalSearchPage.allowLocationPermission();
        carRentalSearchPage.closeInformationPopUp();

        step("Select car rental type");
        carRentalSearchPage.chooseDriver(driverType);

        step("Select car rental date");
        carRentalSearchPage.selectDate(CoreFunctions.addToLongDate(dateRental,"10-20"));

        step("Input car rental location");
        carRentalSearchPage.chooseLocation(Location);

        step("get selected data landing page");
        carRentalDetail.landingPageData = carRentalSearchPage.getSelectedDataLandingPage();

        step("Search car rental");
        carRentalSearchPage.searchCar(carRentalDuration);

        step("choose car");
        carRentalDetail.srpData = carRentalSearchResultPage.chooseCar(duration);

        step("car rental detail assertion");
        Assertion.assertThat("check car rental review detail page",carRentalReviewBookingPage.checkCarRentalDetail(carRentalDetail),is(true));

        step("various section assertion");
        Assertion.assertThat("check rental section",carRentalReviewBookingPage.verifyVariousSectionOnThePage(driverType,carRentalDetail),is(true));

        step("verify price on review booking page");
        Assertion.assertThat("verify price detail section on review booking page",carRentalReviewBookingPage.verifyPriceDetailSection(carRentalDetail),is(true));

        step("continue to booking form from review page");
        carRentalReviewBookingPage.continueFromReviewDetailCar();

        step("pick up location");
        carRentalBookingPage.choosePickUpLocationAndToggleForDropOff(pickupLocation);

        step("add special request");
        carRentalBookingPage.addExtraFacilities(carRentalSpecialRequest);
        Assertion.assertThat("check extra facilities added", carRentalBookingPage.verifyExtraFacilities(), containsString(carRentalSpecialRequest));

        step("continue to complete booking");
        carRentalBookingPage.continueToCompleteBookingPage();

        step("verify special request booking summery");
        Assertion.assertThat("check special request on booking summery", carRentalBookingPage.getAddSpecialRequestBookingSummery(), containsString(carRentalSpecialRequest));

        step("verify driverType, pickup and drop-off time & package type(optional)");
        Assertion.assertThat("check driverType, pickup and drop-off time & package type(only for with driver)", carRentalBookingPage.verifyCarRentalDetail(carRentalDetail), is(true));

        step("continue to payment page");
        carRentalBookingPage.setContinueToPayment();

        step("get order id");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id { " + orderID+ " }",!orderID.isEmpty(),is(true));
    }

    @Author(names = {"Budi","Zia"})
    @Module(name = "CarCompleteBooking")
    @Tribe(name = "CarRental")
    @Vertical(name = "GroundTransport")
    @Test(dataProvider="testLoginCompleteBookingWithDriverDataProvider")
    public void testLoginCompleteBookingWithDriver(Map<String,String> params){
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(params);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        SearchPageCarRental<WebDriver> carRentalSearchPage = SearchPageCarRental.getInstance(session);
        SearchResultPageCarRental<WebDriver> carRentalSearchResultPage = SearchResultPageCarRental.getInstance(session);
        CarRentalReviewBookingPage<WebDriver> carRentalReviewBookingPage = CarRentalReviewBookingPage.getInstance(session);
        CarRentalBookingPage<WebDriver> carRentalBookingPage = CarRentalBookingPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        LoginPage <WebDriver> loginPage = LoginPage.getInstance(session);


        String Location = params.get("location");
        String pickupLocation = params.get("pickupLocation");
        String carRentalDuration = params.get("carRentalDuration");
        String carRentalPackage = params.get("carRentalPackage");
        String loginMethod = params.get("loginType");
        String username = params.get("username");
        String password = params.get("password");
        String dateRental = CoreFunctions.getCurrentLongDate();
        String currency = params.get("currency");

        step("Go To Home");
        homePage.gotoHome();

        step("input credential");
        loginPage.loginWith(loginMethod, username, password);

        if(Configs.ENVIRONMENT == Environment.PROD) {
            step("Change currency to 'IDR'");
            homePage.changeCurrency("IDR",username);
        }

        step("Get App Language");
        String language = homePage.getLanguageApp();

        step("Open Car Landing Page");
        homePage.openVerticalPageDeeplink("Car Rentals", language, currency);
        carRentalSearchPage.allowLocationPermission();
        carRentalSearchPage.closeInformationPopUp();

        step("Select car rental type");
        carRentalSearchPage.chooseDriver("with_driver");

        step("Select car rental date");
        carRentalSearchPage.selectDate(CoreFunctions.addToLongDate(dateRental,"10-20"));

        step("Input car rental location");
        carRentalSearchPage.chooseLocation(Location);

        step("Searh car rental");
        carRentalSearchPage.searchCar(carRentalDuration);

        step("choose car");
        carRentalSearchResultPage.chooseCar();

        step("continue to booking form from review page");
        carRentalReviewBookingPage.continueFromReviewDetailCar();

        step("pick up location");
        carRentalBookingPage.choosePickUpLocation(pickupLocation);

        step("continue to complete booking");
        carRentalBookingPage.continueToCompleteBookingPage();
        carRentalBookingPage.setContinueToPayment();

        step("get order id");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",!orderID.isEmpty(),is(true));
    }

    @Author(names = {"Kartik","Ashish"})
    @Module(name = "CarCompleteBooking")
    @Tribe(name = "CarRental")
    @Vertical(name = "GroundTransport")
    @Test(dataProvider="testLoginCompleteBookingWithoutDriverAndExtraFacilitiesDataProvider")
    public void testLoginCompleteBookingWithoutDriverAndExtraFacilities(Map<String, String> params) {

        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(params);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        SearchPageCarRental<WebDriver> carRentalSearchPage = SearchPageCarRental.getInstance(session);
        SearchResultPageCarRental<WebDriver> carRentalSearchResultPage = SearchResultPageCarRental.getInstance(session);
        CarRentalReviewBookingPage<WebDriver> carRentalReviewBookingPage = CarRentalReviewBookingPage.getInstance(session);
        CarRentalBookingPage<WebDriver> carRentalBookingPage = CarRentalBookingPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);

        String Location = params.get("location");
        String pickupLocation = params.get("pickupLocation");
        String carRentalDuration = params.get("carRentalDuration");
        String carRentalSpecialRequest = params.get("specialRequest");
        String username = params.get("username");
        String password = params.get("password");
        String dateRental = CoreFunctions.getCurrentLongDate();
        String currency = params.get("currency");
        CarRentalDetail carRentalDetail = new CarRentalDetail();
        int duration = Integer.parseInt(carRentalDuration);
        String driverType = "without_driver";

        step("Go To Home");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink (if needed) and change currency to " + currency);
        homePage.loginWithSpecificAccountViaDeeplink(username, password, currency);

        step("Get App Language");
        String language = homePage.getLanguageApp();

        step("Open Car Landing Page");
        homePage.openVerticalPageDeeplink("Car Rentals", language, currency);
        carRentalSearchPage.allowLocationPermission();
        carRentalSearchPage.closeInformationPopUp();

        step("Select car rental type");
        carRentalSearchPage.chooseDriver(driverType);

        step("Select car rental date");
        carRentalSearchPage.selectDate(CoreFunctions.addToLongDate(dateRental,"10-20"));

        step("Input car rental location");
        carRentalSearchPage.chooseLocation(Location);

        step("get selected data landing page");
        carRentalDetail.landingPageData = carRentalSearchPage.getSelectedDataLandingPage();

        step("Search car rental");
        carRentalSearchPage.searchCar(carRentalDuration);

        step("choose car");
        carRentalDetail.srpData = carRentalSearchResultPage.chooseCar(duration);

        step("car rental detail assertion");
        Assertion.assertThat("check car rental review detail page",carRentalReviewBookingPage.checkCarRentalDetail(carRentalDetail),is(true));

        step("various section assertion");
        Assertion.assertThat("check rental section",carRentalReviewBookingPage.verifyVariousSectionOnThePage(driverType,carRentalDetail),is(true));

        step("verify price on review booking page");
        Assertion.assertThat("verify price detail section on review booking page",carRentalReviewBookingPage.verifyPriceDetailSection(carRentalDetail),is(true));

        step("continue to booking form from review page");
        carRentalReviewBookingPage.continueFromReviewDetailCar();

        step("pick up location");
        carRentalBookingPage.choosePickUpLocationAndToggleForDropOff(pickupLocation);

        step("add special request");
        carRentalBookingPage.addExtraFacilities(carRentalSpecialRequest);
        Assertion.assertThat("check extra facilities added", carRentalBookingPage.verifyExtraFacilities(), containsString(carRentalSpecialRequest));

        step("continue to complete booking");
        carRentalBookingPage.continueToCompleteBookingPage();

        step("verify special request booking summery");
        Assertion.assertThat("check special request on booking summery", carRentalBookingPage.getAddSpecialRequestBookingSummery(), containsString(carRentalSpecialRequest));

        step("verify driverType, pickup and drop-off time & package type(optional)");
        Assertion.assertThat("check driverType, pickup and drop-off time & package type(only for with driver)", carRentalBookingPage.verifyCarRentalDetail(carRentalDetail), is(true));

        step("continue to payment page");
        carRentalBookingPage.setContinueToPayment();

        step("get order id");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id { " + orderID+ " }",!orderID.isEmpty(),is(true));
    }

    @Author(names = {"Kartik"})
    @Module(name = "CarCompleteBooking")
    @Tribe(name = "CarRental")
    @Vertical(name = "GroundTransport")
    @Test(dataProvider="testLoginCompleteBookingWithoutDriverDataProvider")
    public void testLoginCompleteBookingWithoutDriver(Map<String, String> params) {

        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(params);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        SearchPageCarRental<WebDriver> carRentalSearchPage = SearchPageCarRental.getInstance(session);
        SearchResultPageCarRental<WebDriver> carRentalSearchResultPage = SearchResultPageCarRental.getInstance(session);
        CarRentalReviewBookingPage<WebDriver> carRentalReviewBookingPage = CarRentalReviewBookingPage.getInstance(session);
        CarRentalBookingPage<WebDriver> carRentalBookingPage = CarRentalBookingPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        LoginPage <WebDriver> loginPage = LoginPage.getInstance(session);


        String Location = params.get("location");
        String pickupLocation = params.get("pickupLocation");
        String carRentalDuration = params.get("carRentalDuration");
        String carRentalPackage = params.get("carRentalPackage");
        String loginMethod = params.get("loginType");
        String username = params.get("username");
        String password = params.get("password");
        String dateRental = CoreFunctions.getCurrentLongDate();
        String currency = params.get("currency");

        step("Go To Home");
        homePage.gotoHome();

        step("input credential");
        loginPage.loginWith(loginMethod, username, password);

        if(Configs.ENVIRONMENT == Environment.PROD) {
            step("Change currency to 'IDR'");
            homePage.changeCurrency("IDR",username);
        }

        step("Get App Language");
        String language = homePage.getLanguageApp();

        step("Open Car Landing Page");
        homePage.openVerticalPageDeeplink("Car Rentals", language, currency);
        carRentalSearchPage.allowLocationPermission();
        carRentalSearchPage.closeInformationPopUp();

        step("Select car rental type");
        carRentalSearchPage.chooseDriver("without_driver");

        step("Select car rental date");
        carRentalSearchPage.selectDate(CoreFunctions.addToLongDate(dateRental,"10-20"));

        step("Input car rental location");
        carRentalSearchPage.chooseLocation(Location);

        step("Search car rental");
        carRentalSearchPage.searchCar(carRentalDuration);

        step("choose car");
        carRentalSearchResultPage.chooseCar();

        step("continue to booking form from review page");
        carRentalReviewBookingPage.continueFromReviewDetailCar();

        step("pick up location");
        carRentalBookingPage.choosePickUpLocation(pickupLocation);

        step("continue to complete booking");
        carRentalBookingPage.continueToCompleteBookingPage();

        step("get order id");
        carRentalBookingPage.setContinueToPayment();
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",!orderID.isBlank(),is(true));
    }

    @Author(names = {"Prerna"})
    @Module(name = "CarRentalReviewBooking")
    @Tribe(name = "CarRental")
    @Vertical(name = "GroundTransport")
    @Test(dataProvider="testLoginCheckReviewBookingPageDataProvider")
    public void testLoginCheckReviewBookingPage(Map<String, String> params) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(params);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        SearchPageCarRental<WebDriver> carRentalSearchPage = SearchPageCarRental.getInstance(session);
        SearchResultPageCarRental<WebDriver> carRentalSearchResultPage = SearchResultPageCarRental.getInstance(session);
        CarRentalReviewBookingPage<WebDriver> carRentalReviewBookingPage = CarRentalReviewBookingPage.getInstance(session);
        LoginPage <WebDriver> loginPage = LoginPage.getInstance(session);

        String Location = params.get("location");
        String loginMethod = params.get("loginType");
        String username = params.get("username");
        String password = params.get("password");
        String carRentalDuration = params.get("carRentalDuration");
        String carRentalType = params.get("carRentalType");
        String dateRental = CoreFunctions.getCurrentLongDate();
        String currency = params.get("currency");

        step("Go To Home");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink (if needed) and change currency to " + currency);
        homePage.loginIfLoggedOutAndChangeCurrencyViaDeeplink(username, password, currency);

        step("Get App Language");
        String language = homePage.getLanguageApp();

        step("Open Car Landing Page");
        homePage.openVerticalPageDeeplink("Car Rentals", language, currency);
        carRentalSearchPage.allowLocationPermission();
        carRentalSearchPage.closeInformationPopUp();

        step("Select car rental type");
        carRentalSearchPage.chooseDriver(carRentalType);

        step("Select car rental date");
        carRentalSearchPage.selectDate(CoreFunctions.addToLongDate(dateRental,"10-20"));

        step("Input car rental location");
        carRentalSearchPage.chooseLocation(Location);

        step("Search car rental");
        carRentalSearchPage.searchCar(carRentalDuration);

        step("choose car");
        carRentalSearchResultPage.chooseCar();

        step("Assertions");
        Assertion.assertThat("all sections are there on review booking page", carRentalReviewBookingPage.verifyAllSectionsOnThePage(), is(true));
    }

    @Author(names = {"Prerna"})
    @Module(name = "CarRentalReviewBooking")
    @Tribe(name = "CarRental")
    @Vertical(name = "GroundTransport")
    @Test(dataProvider="testGuestCheckReviewBookingPageDataProvider")
    public void testGuestCheckReviewBookingPage(Map<String, String> params) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(params);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        SearchPageCarRental<WebDriver> carRentalSearchPage = SearchPageCarRental.getInstance(session);
        SearchResultPageCarRental<WebDriver> carRentalSearchResultPage = SearchResultPageCarRental.getInstance(session);
        CarRentalReviewBookingPage<WebDriver> carRentalReviewBookingPage = CarRentalReviewBookingPage.getInstance(session);
        LoginPage <WebDriver> loginPage = LoginPage.getInstance(session);

        String Location = params.get("location");
        String carRentalDuration = params.get("carRentalDuration");
        String carRentalType = params.get("carRentalType");
        String dateRental = CoreFunctions.getCurrentLongDate();
        String currency = params.get("currency");

        step("Go To Home");
        homePage.gotoHome();

        step("Logout via deeplink");
        homePage.logoutViaDeeplink();

        step("Get App Language");
        String language = homePage.getLanguageApp();

        step("Open Car Landing Page");
        homePage.openVerticalPageDeeplink("Car Rentals", language, currency);
        carRentalSearchPage.allowLocationPermission();
        carRentalSearchPage.closeInformationPopUp();

        step("Select car rental type");
        carRentalSearchPage.chooseDriver(carRentalType);

        step("Select car rental date");
        carRentalSearchPage.selectDate(CoreFunctions.addToLongDate(dateRental,"10-20"));

        step("Input car rental location");
        carRentalSearchPage.chooseLocation(Location);

        step("Search car rental");
        carRentalSearchPage.searchCar(carRentalDuration);

        step("choose car");
        carRentalSearchResultPage.chooseCar();

        step("Assertions");
        Assertion.assertThat("all sections are there on review booking page", carRentalReviewBookingPage.verifyAllSectionsOnThePage(), is(true));
    }
    @Author(names = {"Kartik"})
    @Module(name = "CarRentalReviewBooking")
    @Tribe(name = "CarRental")
    @Vertical(name = "GroundTransport")
    @Test(dataProvider="testCheckReviewBookingPageWithoutDriverDataProvider")
    public void testCheckReviewBookingPageWithoutDriver(Map<String, String> params) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(params);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        SearchPageCarRental<WebDriver> carRentalSearchPage = SearchPageCarRental.getInstance(session);
        SearchResultPageCarRental<WebDriver> carRentalSearchResultPage = SearchResultPageCarRental.getInstance(session);
        CarRentalReviewBookingPage<WebDriver> carRentalReviewBookingPage = CarRentalReviewBookingPage.getInstance(session);
        LoginPage <WebDriver> loginPage = LoginPage.getInstance(session);

        String Location = params.get("location");
        String carRentalDuration = params.get("carRentalDuration");
        String carRentalType = params.get("carRentalType");
        String dateRental = CoreFunctions.getCurrentLongDate();
        String currency = params.get("currency");

        step("Go To Home");
        homePage.gotoHome();

        step("Login as Guest");
        loginPage.loginAsGuest();

        step("Get App Language");
        String language = homePage.getLanguageApp();

        step("Open Car Landing Page");
        homePage.openVerticalPageDeeplink("Car Rentals", language, currency);
        carRentalSearchPage.allowLocationPermission();
        carRentalSearchPage.closeInformationPopUp();

        step("Select car rental type");
        carRentalSearchPage.chooseDriver(carRentalType);

        step("Select car rental date");
        carRentalSearchPage.selectDate(CoreFunctions.addToLongDate(dateRental,"10-20"));

        step("Input car rental location");
        carRentalSearchPage.chooseLocation(Location);

        step("Search car rental");
        carRentalSearchPage.searchCar(carRentalDuration);

        step("choose Car transmission type for self drive");
        carRentalSearchResultPage.chooseTransmissionType("Automatic/Otomatis");

        step("choose car");
        carRentalSearchResultPage.chooseCar();

        step("Assertions");
        Assertion.assertThat("all sections are there on review booking page", carRentalReviewBookingPage.verifyAllSectionsOnPageWithoutDriver(), is(true));
    }

    @Author(names = {"Kartik"})
    @Module(name = "CarRentalReviewBooking")
    @Tribe(name = "CarRental")
    @Vertical(name = "GroundTransport")
    @Test(dataProvider="testLoginCheckReviewBookingPageWithoutDriverDataProvider")
    public void testLoginCheckReviewBookingPageWithoutDriver(Map<String, String> params) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(params);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        SearchPageCarRental<WebDriver> carRentalSearchPage = SearchPageCarRental.getInstance(session);
        SearchResultPageCarRental<WebDriver> carRentalSearchResultPage = SearchResultPageCarRental.getInstance(session);
        CarRentalReviewBookingPage<WebDriver> carRentalReviewBookingPage = CarRentalReviewBookingPage.getInstance(session);
        LoginPage <WebDriver> loginPage = LoginPage.getInstance(session);

        String loginMethod = params.get("loginType");
        String username = params.get("username");
        String password = params.get("password");
        String Location = params.get("location");
        String carRentalDuration = params.get("carRentalDuration");
        String carRentalType = params.get("carRentalType");
        String dateRental = CoreFunctions.getCurrentLongDate();
        String currency = params.get("currency");

        step("Go To Home");
        homePage.gotoHome();

        step("input credential");
        loginPage.loginWith(loginMethod, username, password);

        step("Get App Language");
        String language = homePage.getLanguageApp();

        step("Open Car Landing Page");
        homePage.openVerticalPageDeeplink("Car Rentals", language, currency);
        carRentalSearchPage.allowLocationPermission();
        carRentalSearchPage.closeInformationPopUp();

        step("Select car rental type");
        carRentalSearchPage.chooseDriver(carRentalType);

        step("Select car rental date");
        carRentalSearchPage.selectDate(CoreFunctions.addToLongDate(dateRental,"10-20"));

        step("Input car rental location");
        carRentalSearchPage.chooseLocation(Location);

        step("Search car rental");
        carRentalSearchPage.searchCar(carRentalDuration);

        step("choose Car transmission type for self drive");
        carRentalSearchResultPage.chooseTransmissionType("Automatic/Otomatis");

        step("choose car");
        carRentalSearchResultPage.chooseCar();

        step("Assertions");
        Assertion.assertThat("all sections are there on review booking page", carRentalReviewBookingPage.verifyAllSectionsOnPageWithoutDriver(), is(true));
    }


    @Author(names = {"Kartik"})
    @Module(name = "CarRental ")
    @Tribe(name = "CarRental")
    @Vertical(name = "GroundTransport")
    @Test()
    public void testLoginCheckCoverageArea(){
        Map<String, String> map = getParam("testLoginCheckCoverageArea");
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(map);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        SearchPageCarRental<WebDriver> carRentalSearchPage = SearchPageCarRental.getInstance(session);
        SearchResultPageCarRental<WebDriver> carRentalSearchResultPage = SearchResultPageCarRental.getInstance(session);
        CarRentalReviewBookingPage<WebDriver> carRentalReviewBookingPage = CarRentalReviewBookingPage.getInstance(session);
        LoginPage <WebDriver> loginPage = LoginPage.getInstance(session);

        String Location = map.get("location");
        String carRentalDuration = map.get("carRentalDuration");
        String carRentalPackage = map.get("carRentalPackage");
        String loginMethod = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");
        String dateRental = CoreFunctions.getCurrentLongDate();
        String currency = map.get("currency");

        step("Go To Home");
        homePage.gotoHome();

        step("input credential");
        loginPage.loginWith(loginMethod, username, password);

        if(Environment.PROD == Configs.ENVIRONMENT) {
            step("Change currency to 'IDR'");
            homePage.changeCurrency("IDR",username);
        }

        step("Open Car Landing Page");
        homePage.openVerticalPageDeeplink("Car Rentals", "", currency);
        carRentalSearchPage.allowLocationPermission();
        carRentalSearchPage.closeInformationPopUp();

        step("Select car rental type");
        carRentalSearchPage.chooseDriver("with_driver");

        step("Select car rental date");
        carRentalSearchPage.selectDate(CoreFunctions.addToLongDate(dateRental,"10-20"));

        step("Input car rental location");
        carRentalSearchPage.chooseLocation(Location);

        step("Search car rental");
        carRentalSearchPage.searchCar(carRentalDuration);

        step("choose car");
        carRentalSearchResultPage.chooseCar();

        step("Verify car rental Area names");
        String rentalAreaName = carRentalReviewBookingPage.getCarRentalAreaName();
        carRentalReviewBookingPage.clickSeeRentalAreas();
        String rentalAreaNameTitle = carRentalReviewBookingPage.verifyCarRentalAreaName(rentalAreaName);

        Assertion.assertThat("Maps image is visible on booking form review page ",carRentalReviewBookingPage.isMapsImageVisible(),is(true));
        Assertion.assertThat("Location on both page is same as expected ",rentalAreaName,is(rentalAreaNameTitle));
        Assertion.assertThat("Verify list of area is visible to user",carRentalReviewBookingPage.verifyListOfArea(),is(true));
    }

    @Author(names = {"Kartik"})
    @Module(name = "CarRental")
    @Tribe(name = "CarRental")
    @Vertical(name = "GroundTransport")
    @Test()
    public void testGuestCheckCoverageArea(){
        Map<String, String> map = getParam("testGuestCheckCoverageArea");
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(map);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        SearchPageCarRental<WebDriver> carRentalSearchPage = SearchPageCarRental.getInstance(session);
        SearchResultPageCarRental<WebDriver> carRentalSearchResultPage = SearchResultPageCarRental.getInstance(session);
        CarRentalReviewBookingPage<WebDriver> carRentalReviewBookingPage = CarRentalReviewBookingPage.getInstance(session);
        LoginPage <WebDriver> loginPage = LoginPage.getInstance(session);

        String Location = map.get("location");
        String carRentalDuration = map.get("carRentalDuration");
        String carRentalPackage = map.get("carRentalPackage");
        String dateRental = CoreFunctions.getCurrentLongDate();
        String currency = map.get("currency");

        step("Go To Home");
        homePage.gotoHome();

        step("login as guest");
        loginPage.loginAsGuest();

        if(Environment.PROD == Configs.ENVIRONMENT) {
            step("Change currency to 'IDR'");
            homePage.changeCurrency("IDR", "");
        }

        step("Open Car Landing Page");
        homePage.openVerticalPageDeeplink("Car Rentals", "", currency);
        carRentalSearchPage.allowLocationPermission();
        carRentalSearchPage.closeInformationPopUp();

        step("Select car rental type");
        carRentalSearchPage.chooseDriver("with_driver");

        step("Select car rental date");
        carRentalSearchPage.selectDate(CoreFunctions.addToLongDate(dateRental,"10-20"));

        step("Input car rental location");
        carRentalSearchPage.chooseLocation(Location);

        step("Search car rental");
        carRentalSearchPage.searchCar(carRentalDuration);

        step("choose car");
        carRentalSearchResultPage.chooseCar();

        step("continue to booking form review page");
        String rentalAreaName = carRentalReviewBookingPage.getCarRentalAreaName();
        carRentalReviewBookingPage.clickSeeRentalAreas();
        String rentalAreaNameTitle = carRentalReviewBookingPage.verifyCarRentalAreaName(rentalAreaName);

        Assertion.assertThat("Maps image is visible on booking form review page ",carRentalReviewBookingPage.isMapsImageVisible(),is(true));
        Assertion.assertThat("Location on both page is same as expected ",rentalAreaName,is(rentalAreaNameTitle));
        Assertion.assertThat("Verify list of area is visible to user",carRentalReviewBookingPage.verifyListOfArea(),is(true));
    }
    @Author(names = {"kartik"})
    @Module(name = "CarRentalCheckDetailBookingForm")
    @Tribe(name = "CarRental")
    @Vertical(name = "GroundTransport")
    @Test(dataProvider="testLoginCheckDetailBookingFormWithDriverDataProvider")
    public void testLoginCheckDetailBookingFormWithDriver(Map<String, String> params) {

        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        SearchPageCarRental<WebDriver> carRentalSearchPage = SearchPageCarRental.getInstance(session);
        SearchResultPageCarRental<WebDriver> carRentalSearchResultPage = SearchResultPageCarRental.getInstance(session);
        CarRentalReviewBookingPage<WebDriver> carRentalReviewBookingPage = CarRentalReviewBookingPage.getInstance(session);
        CarRentalBookingPage<WebDriver> carRentalBookingPage = CarRentalBookingPage.getInstance(session);
        LoginPage <WebDriver> loginPage = LoginPage.getInstance(session);

        BaseTest.paramsThreadLocal.set(params);

        String loginMethod = params.get("loginType");
        String username = params.get("username");
        String password = params.get("password");

        String rentalTypes = params.get("carRentalTypes");
        //for selecting cars that contains special request
        String carName = params.get("carName");
        String vendorName = params.get("vendorName");
        String Location = params.get("location");
        String pickupLocation = params.get("pickupLocation");
        String dropOffLocation = params.get("dropOffLocation");
        String carRentalDuration = params.get("carRentalDuration");
        String carRentalPackage = params.get("carRentalPackage");
        String dateRental = CoreFunctions.getCurrentLongDate();
        String specialRequest = params.get("specialRequest");

        String currency = params.get("currency");

        step("Go To Home");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink (if needed) and change currency to " + currency);
        homePage.loginIfLoggedOutAndChangeCurrencyViaDeeplink(username, password, currency);

        step("Get App Language");
        String language = homePage.getLanguageApp();

        step("Open Car Landing Page");
        homePage.openVerticalPageDeeplink("Car Rentals", language, currency);
        carRentalSearchPage.allowLocationPermission();
        carRentalSearchPage.closeInformationPopUp();

        step("Select car rental type");
        carRentalSearchPage.chooseDriver(rentalTypes);

        step("Select car rental date");
        carRentalSearchPage.selectDate(CoreFunctions.addToLongDate(dateRental,"10-20"));

        step("Input car rental location");
        carRentalSearchPage.chooseLocation(Location);

        step("Search car rental");
        carRentalSearchPage.searchCar(carRentalDuration);

        step("choose car");
        carName = carRentalSearchResultPage.getFirstCarName();
        carRentalSearchResultPage.chooseCar();

        step("verification on review page");
        Assertion.assertThat("Detail of car rentals on review booking page", carRentalReviewBookingPage.detailCarRental(carName), is(true));
        Assertion.assertThat("Details package on review booking page", carRentalReviewBookingPage.detailAboutPackage(), is(true));
        Assertion.assertThat("is price displayed on review booking page", !carRentalReviewBookingPage.getPrice().isEmpty(), is(true));

        step("continue to booking form from review page");
        carRentalReviewBookingPage.continueFromReviewDetailCar();

        step("pick up location");
        carRentalBookingPage.choosePickUpLocation(pickupLocation);
        Assertion.assertThat("is pickup area displayed", carRentalBookingPage.verifyPickupArea(), is(true));

        step("drop off location");
        carRentalBookingPage.chooseDropOffLocation(dropOffLocation);
        Assertion.assertThat("is dropOff area displayed", carRentalBookingPage.verifyDropOffArea(), is(true));

        step("add special request");
        carRentalBookingPage.addSpecialRequestWifi(specialRequest);
        Assertion.assertThat("is special request added", carRentalBookingPage.verifySpecialRequestAdded(specialRequest), is(true));

        step("continue to complete booking");
        carRentalBookingPage.continueToCompleteBookingPage();

        step("Verify the Package All in");
        Assertion.assertThat("is package all in displayed", carRentalBookingPage.verifyPackageAllIn(), is(true));
    }

    @Author(names = {"Kartik"})
    @Module(name = "CarRentalCheckDetailBookingForm")
    @Tribe(name = "CarRental")
    @Vertical(name = "GroundTransport")
    @Test(dataProvider="testGuestCheckDetailBookingFormWithDriverDataProvider")
    public void testGuestCheckDetailBookingFormWithDriver(Map<String, String> params) {

        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        SearchPageCarRental<WebDriver> carRentalSearchPage = SearchPageCarRental.getInstance(session);
        SearchResultPageCarRental<WebDriver> carRentalSearchResultPage = SearchResultPageCarRental.getInstance(session);
        CarRentalReviewBookingPage<WebDriver> carRentalReviewBookingPage = CarRentalReviewBookingPage.getInstance(session);
        CarRentalBookingPage<WebDriver> carRentalBookingPage = CarRentalBookingPage.getInstance(session);
        LoginPage <WebDriver> loginPage = LoginPage.getInstance(session);

        BaseTest.paramsThreadLocal.set(params);

        String email = params.get("email");
        String phone = params.get("phoneNumber");

        String rentalTypes = params.get("carRentalTypes");
        //for selecting cars that contains special request
        String carName = params.get("carName");
        String vendorName = params.get("vendorName");
        String Location = params.get("location");
        String pickupLocation = params.get("pickupLocation");
        String dropOffLocation = params.get("dropOffLocation");
        String carRentalDuration = params.get("carRentalDuration");
        String carRentalPackage = params.get("carRentalPackage");
        String dateRental = CoreFunctions.getCurrentLongDate();
        String specialRequest = params.get("specialRequest");

        String currency = params.get("currency");

        step("Go To Home");
        homePage.gotoHome();

        step("Logout via deeplink");
        homePage.logoutViaDeeplink();

        step("Get App Language");
        String language = homePage.getLanguageApp();

        step("Open Car Landing Page");
        homePage.openVerticalPageDeeplink("Car Rentals", language, currency);
        carRentalSearchPage.allowLocationPermission();
        carRentalSearchPage.closeInformationPopUp();

        step("Select car rental type");
        carRentalSearchPage.chooseDriver(rentalTypes);

        step("Select car rental date");
        carRentalSearchPage.selectDate(CoreFunctions.addToLongDate(dateRental,"10-20"));

        step("Input car rental location");
        carRentalSearchPage.chooseLocation(Location);

        step("Search car rental");
        carRentalSearchPage.searchCar(carRentalDuration);

        step("choose car");
        carName = carRentalSearchResultPage.getFirstCarName();
        carRentalSearchResultPage.chooseCar();

        step("verification on review page");
        Assertion.assertThat("Detail of car rentals on review booking page", carRentalReviewBookingPage.detailCarRental(carName), is(true));
        Assertion.assertThat("Details package on review booking page", carRentalReviewBookingPage.detailAboutPackage(), is(true));
        Assertion.assertThat("is price displayed on review booking page", !carRentalReviewBookingPage.getPrice().isEmpty(), is(true));

        step("continue to booking form from review page");
        carRentalReviewBookingPage.continueFromReviewDetailCar();

        step("pick up location");
        carRentalBookingPage.choosePickUpLocation(pickupLocation);
        Assertion.assertThat("is pickup area displayed", carRentalBookingPage.verifyPickupArea(), is(true));

        step("drop off location");
        carRentalBookingPage.chooseDropOffLocation(dropOffLocation);
        Assertion.assertThat("is dropOff area displayed", carRentalBookingPage.verifyDropOffArea(), is(true));

        step("add special request");
        carRentalBookingPage.addSpecialRequestWifi(specialRequest);
        Assertion.assertThat("is special request added", carRentalBookingPage.verifySpecialRequestAdded(specialRequest), is(true));

        step("continue to complete booking");
        carRentalBookingPage.continueToCompleteBookingPage();

        step("Verify the Package All in");
        Assertion.assertThat("is package all in displayed", carRentalBookingPage.verifyPackageAllIn(), is(true));

        step("fill passenger guest");
        carRentalBookingPage.fillGuestDetailOrder(email,phone);
    }

    @Author(names = {"Akbar"})
    @Module(name = "Car")
    @Tribe(name = "CarRental")
    @Vertical(name = "GroundTransport")
    @Test
    public void testGuestCheckHowToRentAndVideoSection() {
        Map<String, String> params = getParam("testGuestCheckHowToRentAndVideoSection");
        BaseTest.paramsThreadLocal.set(params);

        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        SearchPageCarRental<WebDriver> searchPageCarRental = SearchPageCarRental.getInstance(session);
        LoginPage <WebDriver> loginPage = LoginPage.getInstance(session);

        step("Go To Home");
        homePage.gotoHome();

        step("Login with credentials");
        loginPage.loginAsGuest();

        if(Configs.ENVIRONMENT == Environment.PROD) {
            step("Change currency to 'IDR'");
            homePage.changeCurrency("IDR","");
        }

        step("Open Car Landing Page");
        homePage.openVerticalPageDeeplink("Car Rentals", "", "IDR");
        searchPageCarRental.closeInformationPopUp();

        step("Scroll to How To Rent Car info section and Click");
        searchPageCarRental.swipeToInfoSection();
        searchPageCarRental.clickInfoSection("HowToRent");

        step("Verify how to rent car page");
        Assertion.assertThat("Assert if the how to rent page is opened",searchPageCarRental.verifyHowToRentPage(Configs.ENVIRONMENT), is(true));

        step("Back to the landing page");
        searchPageCarRental.clickBackFromHowToRent();

        step("Scroll to How To Rent Car video section");
        searchPageCarRental.swipeToHowToRentVideo();

        step("Click the video");
        searchPageCarRental.clickToHowToRentVideo();

        step("Verify the video player playing the video");
        Assertion.assertThat("Assert if the video is played",searchPageCarRental.verifyHowToRentVideoPlayed(), is(true));
    }

    @Author(names = {"Akbar"})
    @Module(name = "Car")
    @Tribe(name = "CarRental")
    @Vertical(name = "GroundTransport")
    @Test
    public void testLoginCheckHowToRentAndVideoSection() {
        Map<String, String> params = getParam("testLoginCheckHowToRentAndVideoSection");
        BaseTest.paramsThreadLocal.set(params);

        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        SearchPageCarRental<WebDriver> searchPageCarRental = SearchPageCarRental.getInstance(session);
        LoginPage <WebDriver> loginPage = LoginPage.getInstance(session);

        String loginType = params.get("loginType");
        String username = params.get("username");
        String password = params.get("password");

        step("Go To Home");
        homePage.gotoHome();

        step("Login with credentials");
        loginPage.loginWith(loginType, username, password);

        if(Configs.ENVIRONMENT == Environment.PROD) {
            step("Change currency to 'IDR'");
            homePage.changeCurrency("IDR","");
        }

        step("Open Car Landing Page");
        homePage.openVerticalPageDeeplink("Car Rentals", "", "IDR");
        searchPageCarRental.closeInformationPopUp();

        step("Scroll to How To Rent Car info section and Click");
        searchPageCarRental.swipeToInfoSection();
        searchPageCarRental.clickInfoSection("HowToRent");

        step("Verify how to rent car page");
        Assertion.assertThat("Assert if the how to rent page is opened",searchPageCarRental.verifyHowToRentPage(Configs.ENVIRONMENT), is(true));

        step("Back to the landing page");
        searchPageCarRental.clickBackFromHowToRent();

        step("Scroll to How To Rent Car video section");
        searchPageCarRental.swipeToHowToRentVideo();

        step("Click the video");
        searchPageCarRental.clickToHowToRentVideo();

        step("Verify the video player playing the video");
        Assertion.assertThat("Assert if the video is played",searchPageCarRental.verifyHowToRentVideoPlayed(), is(true));
    }

    @Author(names = {"Yazid"})
    @Module(name = "CarRental")
    @Vertical(name = "GroundTransport")
    @Test
    public void testGuestCheckFilterCarListWithDriver() {
        Map<String, String> params = getParam("testGuestCheckFilterCarListWithDriver");
        BaseTest.paramsThreadLocal.set(params);
        String currency = params.get("currency");
        String dateRental = CoreFunctions.getCurrentLongDate();
        String Location = params.get("location");
        String carRentalDuration = params.get("carRentalDuration");

        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage <WebDriver> loginPage = LoginPage.getInstance(session);
        SearchPageCarRental<WebDriver> carRentalSearchPage = SearchPageCarRental.getInstance(session);
        SearchResultPageCarRental<WebDriver> carRentalSearchResultPage = SearchResultPageCarRental.getInstance(session);

        step("Go To Home");
        homePage.gotoHome();

        step("User app as guest");
        loginPage.loginAsGuest();

        step("Get App Language");
        String language = homePage.getLanguageApp();

        if(Configs.ENVIRONMENT == Environment.PROD) {
            step("Change currency to 'IDR'");
            homePage.changeCurrency("IDR","");
        }

        step("Open Car Landing Page");
        homePage.openVerticalPageDeeplink("Car Rentals", language, currency);
        carRentalSearchPage.allowLocationPermission();
        carRentalSearchPage.closeInformationPopUp();

        step("Select car rental type");
        carRentalSearchPage.chooseDriver("with_driver");

        step("Select car rental date");
        carRentalSearchPage.selectDate(CoreFunctions.addToLongDate(dateRental,"10-20"));

        step("Input car rental location");
        carRentalSearchPage.chooseLocation(Location);

        step("Search car rental");
        carRentalSearchPage.searchCar(carRentalDuration);

        step("Tap all-in package filter in SRP");
        String packageTitle;
        packageTitle = carRentalSearchResultPage.selectAllInTab();
        Assertion.assertThat("List car that provide all-in package are visible",
                carRentalSearchResultPage.carListContent() && carRentalSearchResultPage.packageBanner(packageTitle), is(true));

        step("Tap regular package filter in SRP");
        packageTitle = carRentalSearchResultPage.selectRegularTab();
        Assertion.assertThat("List car that provide regular package are visible",
                carRentalSearchResultPage.carListContent() && carRentalSearchResultPage.packageBanner(packageTitle), is(true));
    }

    @Author(names = {"Yazid"})
    @Module(name = "CarRental")
    @Vertical(name = "GroundTransport")
    @Test
    public void testLoginCheckFilterCarListWithDriver() {
        Map<String, String> params = getParam("testLoginCheckFilterCarListWithDriver");
        BaseTest.paramsThreadLocal.set(params);
        String loginMethod = params.get("loginType");
        String username = params.get("username");
        String password = params.get("password");
        String currency = params.get("currency");
        String dateRental = CoreFunctions.getCurrentLongDate();
        String Location = params.get("location");
        String carRentalDuration = params.get("carRentalDuration");

        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage <WebDriver> loginPage = LoginPage.getInstance(session);
        SearchPageCarRental<WebDriver> carRentalSearchPage = SearchPageCarRental.getInstance(session);
        SearchResultPageCarRental<WebDriver> carRentalSearchResultPage = SearchResultPageCarRental.getInstance(session);

        step("Go To Home");
        homePage.gotoHome();

        step("input credential");
        loginPage.loginWith(loginMethod, username, password);

        step("Get App Language");
        String language = homePage.getLanguageApp();

        step("Open Car Landing Page");
        homePage.openVerticalPageDeeplink("Car Rentals", language, currency);
        carRentalSearchPage.allowLocationPermission();
        carRentalSearchPage.closeInformationPopUp();

        step("Select car rental type");
        carRentalSearchPage.chooseDriver("with_driver");

        step("Select car rental date");
        carRentalSearchPage.selectDate(CoreFunctions.addToLongDate(dateRental,"10-20"));

        step("Input car rental location");
        carRentalSearchPage.chooseLocation(Location);

        step("Search car rental");
        carRentalSearchPage.searchCar(carRentalDuration);

        step("Tap all-in package filter in SRP");
        String packageTitle;
        packageTitle = carRentalSearchResultPage.selectAllInTab();
        Assertion.assertThat("List car that provide all-in package are visible",
                carRentalSearchResultPage.carListContent() && carRentalSearchResultPage.packageBanner(packageTitle), is(true));

        step("Tap regular package filter in SRP");
        packageTitle = carRentalSearchResultPage.selectRegularTab();
        Assertion.assertThat("List car that provide regular package are visible",
                carRentalSearchResultPage.carListContent() && carRentalSearchResultPage.packageBanner(packageTitle), is(true));
    }

    @Author(names = {"Yazid"})
    @Module(name = "CarRental")
    @Vertical(name = "GroundTransport")
    @Test
    public void testLoginCheckFilterCarListWithoutDriver() {
        Map<String, String> params = getParam("testLoginCheckFilterCarListWithoutDriver");
        BaseTest.paramsThreadLocal.set(params);
        String loginType = params.get("loginType");
        String username = params.get("username");
        String password = params.get("password");
        String currency = params.get("currency");
        String dateRental = CoreFunctions.getCurrentLongDate();
        String Location = params.get("location");
        String carRentalDuration = params.get("carRentalDuration");

        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage <WebDriver> loginPage = LoginPage.getInstance(session);
        SearchPageCarRental<WebDriver> carRentalSearchPage = SearchPageCarRental.getInstance(session);
        SearchResultPageCarRental<WebDriver> carRentalSearchResultPage = SearchResultPageCarRental.getInstance(session);

        step("Go To Home");
        homePage.gotoHome();

        step("input credential");
        loginPage.loginWith(loginType, username, password);

        step("Get App Language");
        String language = homePage.getLanguageApp();

        step("Open Car Landing Page");
        homePage.openVerticalPageDeeplink("Car Rentals", language, currency);
        carRentalSearchPage.allowLocationPermission();
        carRentalSearchPage.closeInformationPopUp();

        step("Select car rental type");
        carRentalSearchPage.chooseDriver("without_driver");

        step("Select car rental date");
        carRentalSearchPage.selectDate(CoreFunctions.addToLongDate(dateRental,"10-20"));

        step("Input car rental location");
        carRentalSearchPage.chooseLocation(Location);

        step("Search car rental");
        carRentalSearchPage.searchCar(carRentalDuration);

        step("choose car manual transmission");
        carRentalSearchResultPage.chooseTransmissionType("Manual/Manual");
        Assertion.assertThat("Manual transmission should be displayed", carRentalSearchResultPage.carListTransmissionType("Manual/Manual"), is(true));
        Assertion.assertThat("Automatic transmission should NOT be displayed", carRentalSearchResultPage.carListTransmissionType("Automatic/Otomatis"), is(false));

        step("choose car automatic transmission");
        carRentalSearchResultPage.chooseTransmissionType("Automatic/Otomatis");
        Assertion.assertThat("Automatic transmission should be displayed", carRentalSearchResultPage.carListTransmissionType("Automatic/Otomatis"), is(true));
        Assertion.assertThat("Manual transmission should NOT be displayed", carRentalSearchResultPage.carListTransmissionType("Manual/Manual"), is(false));
    }

    @Author(names = {"Yazid"})
    @Module(name = "CarRental")
    @Vertical(name = "GroundTransport")
    @Test
    public void testGuestCheckFilterCarListWithoutDriver() {
        Map<String, String> params = getParam("testGuestCheckFilterCarListWithoutDriver");
        BaseTest.paramsThreadLocal.set(params);
        String currency = params.get("currency");
        String dateRental = CoreFunctions.getCurrentLongDate();
        String Location = params.get("location");
        String carRentalDuration = params.get("carRentalDuration");

        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage <WebDriver> loginPage = LoginPage.getInstance(session);
        SearchPageCarRental<WebDriver> carRentalSearchPage = SearchPageCarRental.getInstance(session);
        SearchResultPageCarRental<WebDriver> carRentalSearchResultPage = SearchResultPageCarRental.getInstance(session);

        step("Go To Home");
        homePage.gotoHome();

        step("User app as guest");
        loginPage.loginAsGuest();

        if(Configs.ENVIRONMENT == Environment.PROD) {
            step("Change currency to 'IDR'");
            homePage.changeCurrency("IDR","");
        }

        step("Open Car Landing Page");
        homePage.openVerticalPageDeeplink("Car Rentals", "", currency);
        carRentalSearchPage.allowLocationPermission();
        carRentalSearchPage.closeInformationPopUp();

        step("Select car rental type");
        carRentalSearchPage.chooseDriver("without_driver");

        step("Select car rental date");
        carRentalSearchPage.selectDate(CoreFunctions.addToLongDate(dateRental,"10-20"));

        step("Input car rental location");
        carRentalSearchPage.chooseLocation(Location);

        step("Search car rental");
        carRentalSearchPage.searchCar(carRentalDuration);

        step("choose car manual transmission");
        carRentalSearchResultPage.chooseTransmissionType("Manual/Manual");
        Assertion.assertThat("Manual transmission should be displayed", carRentalSearchResultPage.carListTransmissionType("Manual/Manual"), is(true));
        Assertion.assertThat("Automatic transmission should NOT be displayed", carRentalSearchResultPage.carListTransmissionType("Automatic/Otomatis"), is(false));

        step("choose car automatic transmission");
        carRentalSearchResultPage.chooseTransmissionType("Automatic/Otomatis");
        Assertion.assertThat("Automatic transmission should be displayed", carRentalSearchResultPage.carListTransmissionType("Automatic/Otomatis"), is(true));
        Assertion.assertThat("Manual transmission should NOT be displayed", carRentalSearchResultPage.carListTransmissionType("Manual/Manual"), is(false));
    }

    @Author(names = {"Zia"})
    @Module(name = "EditLocation")
    @Tribe(name = "CarRental")
    @Vertical(name = "GroundTransport")
    @Test(dataProvider = "testGuestCheckEditLocationDataProvider")
    public void testGuestCheckEditLocation(Map<String, String> map){

        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(map);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        SearchPageCarRental<WebDriver> carRentalSearchPage = SearchPageCarRental.getInstance(session);
        SearchResultPageCarRental<WebDriver> carRentalSearchResultPage = SearchResultPageCarRental.getInstance(session);
        LoginPage <WebDriver> loginPage = LoginPage.getInstance(session);

        String location1 = map.get("location1");
        String location2 = map.get("location2");
        String location3 = map.get("location3");
        String duration1 = map.get("duration1");
        String duration2 = map.get("duration2");
        String currency = map.get("currency");
        String dateRental = CoreFunctions.getCurrentLongDate();
        String date1 = CoreFunctions.addToLongDate(dateRental,"10-20");
        String date2 = CoreFunctions.addToLongDate(date1,"10-20");

        step("Go To Home");
        homePage.gotoHome();

        step("Logout via deeplink");
        homePage.logoutViaDeeplink();

        if(Configs.ENVIRONMENT == Environment.PROD) {
            step("Change currency to 'IDR'");
            homePage.changeCurrency("IDR","");
        }

        step("Get App Language");
        String language = homePage.getLanguageApp();

        step("Open Car Landing Page");
        homePage.openVerticalPageDeeplink("Car Rentals", language, currency);
        carRentalSearchPage.allowLocationPermission();
        carRentalSearchPage.closeInformationPopUp();

        step("Select car rental type");
        carRentalSearchPage.chooseDriver("with_driver");

        step("Select car rental date");
        carRentalSearchPage.selectDate(date1);

        step("Input car rental location");
        carRentalSearchPage.chooseLocation(location1);

        step("Search car rental");
        carRentalSearchPage.searchCar(duration1);

        step("Assert SRP with driver");
        Assertion.assertThat("is in SRP with driver ",carRentalSearchResultPage.verifySRP(date1,location1,duration1,"With Driver"),is(true));

        step("Change the search for the with driver option");
        carRentalSearchResultPage.clickChangeSearch(location2,date2,duration2,"with-driver");

        step("Verify SRP again");
        Assertion.assertThat("is in SRP with driver ",carRentalSearchResultPage.verifySRP(date2,location2,duration2,"With Driver"),is(true));

        step("Change the search for the Self Drive option");
        carRentalSearchResultPage.clickChangeSearch(location3,date2,duration2,"self-drive");

        step("Verify SRP again");
        Assertion.assertThat("is in SRP without driver ",carRentalSearchResultPage.verifySRP(date2,location3,duration2,"Self drive"),is(true));
    }

    @Author(names = {"Zia"})
    @Module(name = "EditLocation")
    @Tribe(name = "CarRental")
    @Vertical(name = "GroundTransport")
    @Test(dataProvider = "testLoginCheckEditLocationDataProvider")
    public void testLoginCheckEditLocation(Map<String, String> map){

        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(map);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        SearchPageCarRental<WebDriver> carRentalSearchPage = SearchPageCarRental.getInstance(session);
        SearchResultPageCarRental<WebDriver> carRentalSearchResultPage = SearchResultPageCarRental.getInstance(session);
        LoginPage <WebDriver> loginPage = LoginPage.getInstance(session);

        String loginMethod = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");

        String location1 = map.get("location1");
        String location2 = map.get("location2");
        String location3 = map.get("location3");
        String duration1 = map.get("duration1");
        String duration2 = map.get("duration2");
        String currency = map.get("currency");
        String dateRental = CoreFunctions.getCurrentLongDate();
        String date1 = CoreFunctions.addToLongDate(dateRental,"10-20");
        String date2 = CoreFunctions.addToLongDate(date1,"10-20");

        step("Go To Home");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink (if needed) and change currency to " + currency);
        homePage.loginIfLoggedOutAndChangeCurrencyViaDeeplink(username, password, currency);

        step("Get App Language");
        String language = homePage.getLanguageApp();

        step("Open Car Landing Page");
        homePage.openVerticalPageDeeplink("Car Rentals", language, currency);
        carRentalSearchPage.allowLocationPermission();
        carRentalSearchPage.closeInformationPopUp();

        step("Select car rental type");
        carRentalSearchPage.chooseDriver("with_driver");

        step("Select car rental date");
        carRentalSearchPage.selectDate(date1);

        step("Input car rental location");
        carRentalSearchPage.chooseLocation(location1);

        step("Search car rental");
        carRentalSearchPage.searchCar(duration1);

        step("Assert SRP with driver");
        Assertion.assertThat("is in SRP with driver ",carRentalSearchResultPage.verifySRP(date1,location1,duration1,"With Driver"),is(true));

        step("Change the search for the with driver option");
        carRentalSearchResultPage.clickChangeSearch(location2,date2,duration2,"with-driver");

        step("Verify SRP again");
        Assertion.assertThat("is in SRP with driver ",carRentalSearchResultPage.verifySRP(date2,location2,duration2,"With Driver"),is(true));

        step("Change the search for the Self Drive option");
        carRentalSearchResultPage.clickChangeSearch(location3,date2,duration2,"self-drive");

        step("Verify SRP again");
        Assertion.assertThat("is in SRP without driver ",carRentalSearchResultPage.verifySRP(date2,location3,duration2,"Self drive"),is(true));
    }

    @Author(names = {"Ashish"})
    @Module(name = "CarRentalCheckDetailBookingForm")
    @Tribe(name = "CarRental")
    @Vertical(name = "GroundTransport")
    @Test(dataProvider="testLoginCheckDetailBookingFormWithoutDriverDataProvider")
    public void testLoginCheckDetailBookingFormWithoutDriver(Map<String, String> params) {

        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        SearchPageCarRental<WebDriver> carRentalSearchPage = SearchPageCarRental.getInstance(session);
        SearchResultPageCarRental<WebDriver> carRentalSearchResultPage = SearchResultPageCarRental.getInstance(session);
        CarRentalReviewBookingPage<WebDriver> carRentalReviewBookingPage = CarRentalReviewBookingPage.getInstance(session);
        CarRentalBookingPage<WebDriver> carRentalBookingPage = CarRentalBookingPage.getInstance(session);
        LoginPage <WebDriver> loginPage = LoginPage.getInstance(session);

        BaseTest.paramsThreadLocal.set(params);

        String loginMethod = params.get("loginType");
        String username = params.get("username");
        String password = params.get("password");

        String rentalTypes = params.get("carRentalTypes");
        //for selecting cars that contains special request
        String carName = params.get("carName");
        String vendorName = params.get("vendorName");
        String Location = params.get("location");
        String pickupLocation = params.get("pickupLocation");
        String dropOffLocation = params.get("dropOffLocation");
        String carRentalDuration = params.get("carRentalDuration");
        String carRentalPackage = params.get("carRentalPackage");
        String dateRental = CoreFunctions.getCurrentLongDate();
        String specialRequest = params.get("specialRequest");

        String currency = params.get("currency");

        step("Go To Home");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink (if needed) and change currency to " + currency);
        homePage.loginIfLoggedOutAndChangeCurrencyViaDeeplink(username, password, currency);

        step("Get App Language");
        String language = homePage.getLanguageApp();

        step("Open Car Landing Page");
        homePage.openVerticalPageDeeplink("Car Rentals", language, currency);
        carRentalSearchPage.allowLocationPermission();
        carRentalSearchPage.closeInformationPopUp();

        step("Select car rental type");
        carRentalSearchPage.chooseDriver(rentalTypes);

        step("Select car rental date");
        carRentalSearchPage.selectDate(CoreFunctions.addToLongDate(dateRental,"10-20"));

        step("Input car rental location");
        carRentalSearchPage.chooseLocation(Location);

        step("Search car rental");
        carRentalSearchPage.searchCar(carRentalDuration);

        step("choose car");
        carName = carRentalSearchResultPage.getFirstCarName();
        carRentalSearchResultPage.chooseCar();

        step("verification on review page");
        Assertion.assertThat("Detail of car rentals on review booking page", carRentalReviewBookingPage.detailCarRental(carName), is(true));
        Assertion.assertThat("is price displayed on review booking page", !carRentalReviewBookingPage.getPrice().isEmpty(), is(true));

        step("continue to booking form from review page");
        carRentalReviewBookingPage.continueFromReviewDetailCar();

        step("pick up location");
        carRentalBookingPage.choosePickUpLocation(pickupLocation);
        Assertion.assertThat("is pickup area displayed", carRentalBookingPage.verifyPickupArea(), is(true));

        step("drop off location");
        carRentalBookingPage.chooseDropOffLocation(dropOffLocation);
        Assertion.assertThat("is dropOff area displayed", carRentalBookingPage.verifyDropOffArea(), is(true));

        step("add special request");
        carRentalBookingPage.addSpecialRequestWifi(specialRequest);
        Assertion.assertThat("is special request added", carRentalBookingPage.verifySpecialRequestAdded(specialRequest), is(true));

        step("continue to complete booking");
        carRentalBookingPage.continueToCompleteBookingPage();
    }

    @Author(names = {"Kartik"})
    @Module(name = "CarRentalCheckDetailBookingForm")
    @Tribe(name = "CarRental")
    @Vertical(name = "GroundTransport")
    @Test(dataProvider="testGuestCheckDetailBookingFormWithoutDriverDataProvider")
    public void testGuestCheckDetailBookingFormWithoutDriver(Map<String, String> params) {

        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        SearchPageCarRental<WebDriver> carRentalSearchPage = SearchPageCarRental.getInstance(session);
        SearchResultPageCarRental<WebDriver> carRentalSearchResultPage = SearchResultPageCarRental.getInstance(session);
        CarRentalReviewBookingPage<WebDriver> carRentalReviewBookingPage = CarRentalReviewBookingPage.getInstance(session);
        CarRentalBookingPage<WebDriver> carRentalBookingPage = CarRentalBookingPage.getInstance(session);
        LoginPage <WebDriver> loginPage = LoginPage.getInstance(session);

        BaseTest.paramsThreadLocal.set(params);

        String loginMethod = params.get("loginType");
        String email = params.get("email");
        String phone = params.get("phoneNumber");

        String rentalTypes = params.get("carRentalTypes");
        //for selecting cars that contains special request
        String carName = params.get("carName");
        String vendorName = params.get("vendorName");
        String Location = params.get("location");
        String pickupLocation = params.get("pickupLocation");
        String dropOffLocation = params.get("dropOffLocation");
        String carRentalDuration = params.get("carRentalDuration");
        String carRentalPackage = params.get("carRentalPackage");
        String dateRental = CoreFunctions.getCurrentLongDate();
        String specialRequest = params.get("specialRequest");

        String currency = params.get("currency");
        step("Go To Home");
        homePage.gotoHome();

        step("Logout via deeplink");
        homePage.logoutViaDeeplink();

        step("Get App Language");
        String language = homePage.getLanguageApp();

        step("Open Car Landing Page");
        homePage.openVerticalPageDeeplink("Car Rentals", language, currency);
        carRentalSearchPage.allowLocationPermission();
        carRentalSearchPage.closeInformationPopUp();

        step("Select car rental type");
        carRentalSearchPage.chooseDriver(rentalTypes);

        step("Select car rental date");
        carRentalSearchPage.selectDate(CoreFunctions.addToLongDate(dateRental,"10-20"));

        step("Input car rental location");
        carRentalSearchPage.chooseLocation(Location);

        step("Search car rental");
        carRentalSearchPage.searchCar(carRentalDuration);

        step("choose car");
        carName = carRentalSearchResultPage.getFirstCarName();
        carRentalSearchResultPage.chooseCar();

        step("verification on review page");
        Assertion.assertThat("Detail of car rentals on review booking page", carRentalReviewBookingPage.detailCarRental(carName), is(true));
        Assertion.assertThat("is price displayed on review booking page", !carRentalReviewBookingPage.getPrice().isEmpty(), is(true));

        step("continue to booking form from review page");
        carRentalReviewBookingPage.continueFromReviewDetailCar();

        step("pick up location");
        carRentalBookingPage.choosePickUpLocation(pickupLocation);
        Assertion.assertThat("is pickup area displayed", carRentalBookingPage.verifyPickupArea(), is(true));

        step("drop off location");
        carRentalBookingPage.chooseDropOffLocation(dropOffLocation);
        Assertion.assertThat("is dropOff area displayed", carRentalBookingPage.verifyDropOffArea(), is(true));

        step("add special request");
        carRentalBookingPage.addSpecialRequestWifi(specialRequest);
        Assertion.assertThat("is special request added", carRentalBookingPage.verifySpecialRequestAdded(specialRequest), is(true));

        step("continue to complete booking");
        carRentalBookingPage.continueToCompleteBookingPage();

        step("fill passenger guest");
        carRentalBookingPage.fillGuestDetailOrder(email,phone);
    }

    @Author(names = {"Zia"})
    @Module(name = "CarCompleteBooking")
    @Tribe(name = "CarRental")
    @Vertical(name = "GroundTransport")
    @Test
    public void testBookCarRentalViaDeeplinkLogin(){
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> params = getParam("testBookCarRentalViaDeeplinkLogin");
        BaseTest.paramsThreadLocal.set(params);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        SearchPageCarRental<WebDriver> carRentalSearchPage = SearchPageCarRental.getInstance(session);
        SearchResultPageCarRental<WebDriver> carRentalSearchResultPage = SearchResultPageCarRental.getInstance(session);
        CarRentalReviewBookingPage<WebDriver> carRentalReviewBookingPage = CarRentalReviewBookingPage.getInstance(session);
        CarRentalBookingPage<WebDriver> carRentalBookingPage = CarRentalBookingPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        LoginPage <WebDriver> loginPage = LoginPage.getInstance(session);

        String Location = params.get("location");
        String pickupLocation = params.get("pickupLocation");
        String carRentalDuration = params.get("carRentalDuration");
        String carRentalPackage = params.get("carRentalPackage");
        String loginMethod = params.get("loginType");
        String username = params.get("username");
        String password = params.get("password");
        String dateRental = CoreFunctions.getCurrentLongDate();
        String currency = params.get("currency");

        step("Go To Home");
        homePage.gotoHome();

        step("input credential");
        loginPage.loginWith(loginMethod, username, password);

        step("Change currency");
        homePage.changeCurrency(currency,username);

        step("Open Car Landing Page");
        carRentalSearchPage.openViaDeeplink();
        carRentalSearchPage.allowLocationPermission();
        carRentalSearchPage.closeInformationPopUp();

        step("Select car rental type");
        carRentalSearchPage.chooseDriver("with_driver");

        step("Select car rental date");
        carRentalSearchPage.selectDate(CoreFunctions.addToLongDate(dateRental,"10-20"));

        step("Input car rental location");
        carRentalSearchPage.chooseLocation(Location);

        step("Search car rental");
        carRentalSearchPage.searchCar(carRentalDuration);

        step("Verify price car type");
        Assertion.assertThat("is car type price in IDR", carRentalSearchResultPage.getPriceCarType().contains("IDR"), is(true));

        step("choose car");
        carRentalSearchResultPage.chooseCarType();

        step("Verify price car vendor");
        Assertion.assertThat("is car vendor price in IDR", carRentalSearchResultPage.getPriceCarVendor().contains("IDR"), is(true));

        step("choose vendor");
        carRentalSearchResultPage.chooseCarVendor();

        step("Verify price in PDP page");
        Assertion.assertThat("is car PDP price in IDR", carRentalSearchResultPage.getPricePDP().contains("IDR"), is(true));

        step("continue to booking form from review page");
        carRentalReviewBookingPage.continueFromReviewDetailCar();

        step("Verify price in booking form page 1");
        carRentalBookingPage.searchChoosePickUpLocation(pickupLocation);
        Assertion.assertThat("is pickup location search price in IDR", carRentalBookingPage.getPricePickupSearch().contains("IDR"), is(true));
        carRentalBookingPage.confirmPickUpLocation();
        carRentalBookingPage.clickSameReturn();
        Assertion.assertThat("is pickup location price in IDR", carRentalBookingPage.getPricePickup().contains("IDR"), is(true));
        Assertion.assertThat("is return location price in IDR", carRentalBookingPage.getPriceDropOff().contains("IDR"), is(true));
        Assertion.assertThat("is coverage area price in IDR", carRentalBookingPage.getPriceArea().contains("IDR"), is(true));

        String priceAllIn = carRentalBookingPage.addAllInPackage(currency);
        Assertion.assertThat("is price all-in in IDR", priceAllIn.contains("IDR"), is(true));

        if (Configs.ENVIRONMENT == Environment.GK || Configs.ENVIRONMENT == Environment.PREPROD) {
            CarRentalDetail carRentalDetail = carRentalBookingPage.addSpecialRequest(currency);
            String priceSpecialRequestItem = carRentalDetail.extraFacilities1Price;
            Assertion.assertThat("is price special request item in IDR", priceSpecialRequestItem.contains("IDR"), is(true));
            String priceSpecialRequestTotal = carRentalDetail.extraFacilitiesTotalPrice;
            Assertion.assertThat("is price special request total in IDR", priceSpecialRequestTotal.contains("IDR"), is(true));
        }

        carRentalBookingPage.clickPriceDetail();
        Assertion.assertThat("is price detail in IDR", (carRentalBookingPage.getPriceDetail().stream().allMatch(price -> (price.contains("IDR") || price.equals("Termasuk") || price.equals("Included")))), is(true));
        carRentalBookingPage.closePriceDetail();
        Assertion.assertThat("is total price in IDR", carRentalBookingPage.getTotalPrice().contains("IDR"), is(true));

        step("continue to complete booking");
        carRentalBookingPage.continueToCompleteBookingPage();

        step("Verify price in booking form page 2");
        carRentalBookingPage.clickPriceDetail();
        Assertion.assertThat("is price detail in IDR", (carRentalBookingPage.getPriceDetail().stream().allMatch(price -> (price.contains("IDR") || price.equals("Termasuk") || price.equals("Included")))), is(true));
        carRentalBookingPage.closePriceDetail();
        Assertion.assertThat("is total price in IDR", carRentalBookingPage.getTotalPrice().contains("IDR"), is(true));

        carRentalBookingPage.setContinueToPayment();

        step("get order id");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",!orderID.isEmpty(),is(true));

        step("Verify price in payment page");
        paymentPage.openPriceDetail();
        Assertion.assertThat("is price detail payment page in IDR", (paymentPage.getPriceDetail().stream().allMatch(price -> (price.contains("IDR") || price.equals("Termasuk") || price.equals("Included")))), is(true));
        Assertion.assertThat("is price total payment page in IDR", (paymentPage.getTotalPrice().contains("IDR")), is(true));
    }

    @Author(names = {"Zia"})
    @Module(name = "CarCompleteBooking")
    @Tribe(name = "CarRental")
    @Vertical(name = "GroundTransport")
    @Test
    public void testBookCarRentalViaDeeplinkGuest(){
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> params = getParam("testBookCarRentalViaDeeplinkGuest");
        BaseTest.paramsThreadLocal.set(params);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        SearchPageCarRental<WebDriver> carRentalSearchPage = SearchPageCarRental.getInstance(session);
        SearchResultPageCarRental<WebDriver> carRentalSearchResultPage = SearchResultPageCarRental.getInstance(session);
        CarRentalReviewBookingPage<WebDriver> carRentalReviewBookingPage = CarRentalReviewBookingPage.getInstance(session);
        CarRentalBookingPage<WebDriver> carRentalBookingPage = CarRentalBookingPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        LoginPage <WebDriver> loginPage = LoginPage.getInstance(session);

        String Location = params.get("location");
        String pickupLocation = params.get("pickupLocation");
        String carRentalDuration = params.get("carRentalDuration");
        String carRentalPackage = params.get("carRentalPackage");
        String dateRental = CoreFunctions.getCurrentLongDate();
        String currency = params.get("currency");

        step("Go To Home");
        homePage.gotoHome();

        step("login As Guest");
        loginPage.loginAsGuest();

        step("Change currency to default IDR");
        homePage.changeCurrency("IDR","");

        step("Change currency");
        homePage.changeCurrency(currency,"");

        step("Open Car Landing Page");
        carRentalSearchPage.openViaDeeplink();
        carRentalSearchPage.allowLocationPermission();
        carRentalSearchPage.closeInformationPopUp();

        step("Select car rental type");
        carRentalSearchPage.chooseDriver("with_driver");

        step("Select car rental date");
        carRentalSearchPage.selectDate(CoreFunctions.addToLongDate(dateRental,"10-20"));

        step("Input car rental location");
        carRentalSearchPage.chooseLocation(Location);

        step("Search car rental");
        carRentalSearchPage.searchCar(carRentalDuration);

        step("Verify price car type");
        Assertion.assertThat("is car type price in IDR", carRentalSearchResultPage.getPriceCarType().contains("IDR"), is(true));

        step("choose car");
        carRentalSearchResultPage.chooseCarType();

        step("Verify price car vendor");
        Assertion.assertThat("is car vendor price in IDR", carRentalSearchResultPage.getPriceCarVendor().contains("IDR"), is(true));

        step("choose vendor");
        carRentalSearchResultPage.chooseCarVendor();

        step("Verify price in PDP page");
        Assertion.assertThat("is car PDP price in IDR", carRentalSearchResultPage.getPricePDP().contains("IDR"), is(true));

        step("continue to booking form from review page");
        carRentalReviewBookingPage.continueFromReviewDetailCar();

        step("Verify price in booking form page 1");
        carRentalBookingPage.searchChoosePickUpLocation(pickupLocation);
        Assertion.assertThat("is pickup location search price in IDR", carRentalBookingPage.getPricePickupSearch().contains("IDR"), is(true));
        carRentalBookingPage.confirmPickUpLocation();
        carRentalBookingPage.clickSameReturn();
        Assertion.assertThat("is pickup location price in IDR", carRentalBookingPage.getPricePickup().contains("IDR"), is(true));
        Assertion.assertThat("is return location price in IDR", carRentalBookingPage.getPriceDropOff().contains("IDR"), is(true));
        Assertion.assertThat("is coverage area price in IDR", carRentalBookingPage.getPriceArea().contains("IDR"), is(true));

        String priceAllIn = carRentalBookingPage.addAllInPackage(currency);
        Assertion.assertThat("is price all-in in IDR", priceAllIn.contains("IDR"), is(true));

        if (Configs.ENVIRONMENT == Environment.GK || Configs.ENVIRONMENT == Environment.PREPROD) {
            CarRentalDetail carRentalDetail = carRentalBookingPage.addSpecialRequest(currency);
            String priceSpecialRequestItem = carRentalDetail.extraFacilities1Price;
            Assertion.assertThat("is price special request item in IDR", priceSpecialRequestItem.contains("IDR"), is(true));
            String priceSpecialRequestTotal = carRentalDetail.extraFacilitiesTotalPrice;
            Assertion.assertThat("is price special request total in IDR", priceSpecialRequestTotal.contains("IDR"), is(true));
        }

        carRentalBookingPage.clickPriceDetail();
        Assertion.assertThat("is price detail in IDR", (carRentalBookingPage.getPriceDetail().stream().allMatch(price -> (price.contains("IDR") || price.equals("Termasuk") || price.equals("Included")))), is(true));
        carRentalBookingPage.closePriceDetail();
        Assertion.assertThat("is total price in IDR", carRentalBookingPage.getTotalPrice().contains("IDR"), is(true));

        step("continue to complete booking");
        carRentalBookingPage.continueToCompleteBookingPage();

        step("Fill contact detail");
        carRentalBookingPage.fillGuestDetailOrder(CoreFunctions.randomEmail(),CoreFunctions.randomPhone());

        step("Verify price in booking form page 2");
        carRentalBookingPage.clickPriceDetail();
        Assertion.assertThat("is price detail in IDR", (carRentalBookingPage.getPriceDetail().stream().allMatch(price -> (price.contains("IDR") || price.equals("Termasuk") || price.equals("Included")))), is(true));
        carRentalBookingPage.closePriceDetail();
        Assertion.assertThat("is total price in IDR", carRentalBookingPage.getTotalPrice().contains("IDR"), is(true));

        carRentalBookingPage.setContinueToPayment();

        step("get order id");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",!orderID.isEmpty(),is(true));

        step("Verify price in payment page");
        paymentPage.openPriceDetail();
        Assertion.assertThat("is price detail payment page in IDR", (paymentPage.getPriceDetail().stream().allMatch(price -> (price.contains("IDR") || price.equals("Termasuk") || price.equals("Included")))), is(true));
        Assertion.assertThat("is price total payment page in IDR", (paymentPage.getTotalPrice().contains("IDR")), is(true));
    }

    @Author(names = {"Ashish"})
    @Module(name = "homePageWithoutCarRentalsNonIDR")
    @Tribe(name = "CarRental")
    @Vertical(name = "GroundTransport")
    @Test
    public void testVerifyHomePageWithoutCarRentalsNonIDRLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testVerifyHomePageWithoutCarRentalsNonIDRLogin");
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

        step("change currency to default IDR");
        homePage.changeCurrency("IDR",username);

        step("Change currency");
        homePage.changeCurrency(currency,username);

        step("Find Car Rental Vertical");
        Assertion.assertThat("is car rental not displayed in non-IDR currency {"+currency+" }",homePage.checkVerticalPresence("Car Rentals"),is(false));

    }

    @Author(names = {"Ashish"})
    @Module(name = "homePageWithoutCarRentals")
    @Tribe(name = "CarRental")
    @Vertical(name = "GroundTransport")
    @Test
    public void testVerifyHomePageWithoutCarRentalsNonIDRGuest() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testVerifyHomePageWithoutCarRentalsNonIDRGuest");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);

        String currency = param.get("currency");

        step("user open app");
        homePage.gotoHome();

        step("user login as guest");
        loginPage.loginAsGuest();

        step("change currency to default IDR");
        homePage.changeCurrency("IDR","");

        step("Change currency");
        homePage.changeCurrency(currency,"");

        step("Find Car Rental Vertical");
        Assertion.assertThat("is car rental not displayed in non-IDR currency {"+currency+" }",homePage.checkVerticalPresence("Car Rentals"),is(false));

    }

    @Author(names = {"Ashish"})
    @Module(name = "PurchaseWithCC")
    @Tribe(name = "CarRental")
    @Vertical(name = "GroundTransport")
    @Test
    public void testPurchaseWithCCLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String,String> testProperties = getParam("testPurchaseWithCCLogin");
        BaseTest.paramsThreadLocal.set(testProperties);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        SearchPageCarRental<WebDriver> carRentalSearchPage = SearchPageCarRental.getInstance(session);
        SearchResultPageCarRental<WebDriver> carRentalSearchResultPage = SearchResultPageCarRental.getInstance(session);
        CarRentalReviewBookingPage<WebDriver> carRentalReviewBookingPage = CarRentalReviewBookingPage.getInstance(session);
        CarRentalBookingPage<WebDriver> carRentalBookingPage = CarRentalBookingPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        LoginPage <WebDriver> loginPage = LoginPage.getInstance(session);


        String Location = testProperties.get("location");
        String pickupLocation = testProperties.get("pickupLocation");
        String carRentalDuration = testProperties.get("carRentalDuration");
        String carRentalPackage = testProperties.get("carRentalPackage");
        String loginMethod = testProperties.get("loginType");
        String username = testProperties.get("username");
        String password = testProperties.get("password");
        String dateRental = CoreFunctions.getCurrentLongDate();
        String currency = testProperties.get("currency");
        String cardNumber = testProperties.get("cardNumber");
        String expiry = testProperties.get("expiry");
        String cvv = testProperties.get("cvv");
        String cardName = testProperties.get("cardName");
        String payment_method = testProperties.get("paymentMethod");

        step("Go To Home");
        homePage.gotoHome();

        step("input credential");
        loginPage.loginWith(loginMethod, username, password);

        if(Configs.ENVIRONMENT == Environment.PROD) {
            step("Change currency to 'IDR'");
            homePage.changeCurrency("IDR",username);
        }

        step("Get App Language");
        String language = homePage.getLanguageApp();

        step("Open Car Landing Page");
        homePage.openVerticalPageDeeplink("Car Rentals", language, currency);
        carRentalSearchPage.allowLocationPermission();
        carRentalSearchPage.closeInformationPopUp();

        step("Select car rental type");
        carRentalSearchPage.chooseDriver("with_driver");

        step("Select car rental date");
        carRentalSearchPage.selectDate(CoreFunctions.addToLongDate(dateRental,"30-40"));

        step("Input car rental location");
        carRentalSearchPage.chooseLocation(Location);

        step("Searh car rental");
        carRentalSearchPage.searchCar(carRentalDuration);

        step("choose car");
        carRentalSearchResultPage.chooseCar();

        step("continue to booking form from review page");
        carRentalReviewBookingPage.continueFromReviewDetailCar();

        step("pick up location");
        carRentalBookingPage.choosePickUpLocation(pickupLocation);

        step("continue to complete booking");
        carRentalBookingPage.continueToCompleteBookingPage();
        carRentalBookingPage.setContinueToPayment();

        step("get order id");
        String orderID = paymentPage.checkOrderID();
        String productName = paymentPage.getProductName();
        Assertion.assertThat("user get order id",!orderID.isEmpty(),is(true));

        step("user choose pay with cc");
        paymentPage.openPaymentMethod();
        paymentPage.selectPaymentMethod(payment_method);

        if(Configs.ENVIRONMENT == Environment.GK || Configs.ENVIRONMENT == Environment.PREPROD){
            step("user fill cc detail");
            paymentPage.fillCCDetailRealIssued(cardNumber, expiry, cvv, cardName);
            paymentPage.clickPayButtonCC();
            paymentPage.verifyCreditCard3ds();

            step("Open the order list and go to order detail");
            paymentPage.seeOrderList();
            orderListPage.openOrderDetail(productName);
            orderListPage.removePopUp();

            step("Assertion");
            Assertion.assertThat("order id is match",orderDetailPage.getFullOrderId(),containsString(orderID));
        }else{
            step("verify payment method is selected correctly");
            Assertion.assertThat("is payment method selected correctly",paymentPage.isCCSelectedAsPaymentMethod(),is(true));
        }
    }

    @Author(names = {"Ashish"})
    @Module(name = "PurchaseWithCC")
    @Tribe(name = "CarRental")
    @Vertical(name = "GroundTransport")
    @Test
    public void testPurchaseWithCCGuest() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String,String> testProperties = getParam("testPurchaseWithCCGuest");
        BaseTest.paramsThreadLocal.set(testProperties);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        SearchPageCarRental<WebDriver> carRentalSearchPage = SearchPageCarRental.getInstance(session);
        SearchResultPageCarRental<WebDriver> carRentalSearchResultPage = SearchResultPageCarRental.getInstance(session);
        CarRentalReviewBookingPage<WebDriver> carRentalReviewBookingPage = CarRentalReviewBookingPage.getInstance(session);
        CarRentalBookingPage<WebDriver> carRentalBookingPage = CarRentalBookingPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        LoginPage <WebDriver> loginPage = LoginPage.getInstance(session);

        String Location = testProperties.get("location");
        String pickupLocation = testProperties.get("pickupLocation");
        String email = testProperties.get("email");
        String carRentalDuration = testProperties.get("carRentalDuration");
        String carRentalPackage = testProperties.get("carRentalPackage");
        String dateRental = CoreFunctions.getCurrentLongDate();
        String phoneNumber = testProperties.get("phoneNumber");
        String currency = testProperties.get("currency");
        String cardNumber = testProperties.get("cardNumber");
        String expiry = testProperties.get("expiry");
        String cvv = testProperties.get("cvv");
        String cardName = testProperties.get("cardName");
        String payment_method = testProperties.get("paymentMethod");

        step("Go To Home");
        homePage.gotoHome();

        step("input credential");
        loginPage.loginAsGuest();

        if(Configs.ENVIRONMENT == Environment.PROD) {
            step("Change currency to 'IDR'");
            homePage.changeCurrency("IDR","");
        }

        step("Get App Language");
        String language = homePage.getLanguageApp();

        step("Open Car Landing Page");
        homePage.openVerticalPageDeeplink("Car Rentals", language, currency);
        carRentalSearchPage.allowLocationPermission();
        carRentalSearchPage.closeInformationPopUp();

        step("Select car rental type");
        carRentalSearchPage.chooseDriver("with_driver");

        step("Select car rental date");
        carRentalSearchPage.selectDate(CoreFunctions.addToLongDate(dateRental,"30-40"));

        step("Input car rental location");
        carRentalSearchPage.chooseLocation(Location);

        step("Searh car rental");
        carRentalSearchPage.searchCar(carRentalDuration);

        step("choose car");
        carRentalSearchResultPage.chooseCar();

        step("continue to booking form from review page");
        carRentalReviewBookingPage.continueFromReviewDetailCar();

        step("pick up location");
        carRentalBookingPage.choosePickUpLocation(pickupLocation);

        step("continue to complete booking");
        carRentalBookingPage.continueToCompleteBookingPage();

        step("input contact detail");
        carRentalBookingPage.fillGuestDetailOrder(email,phoneNumber);
        carRentalBookingPage.setContinueToPayment();

        step("get order id");
        String orderID = paymentPage.checkOrderID();
        String productName = paymentPage.getProductName();
        Assertion.assertThat("user get order id",!orderID.isEmpty(),is(true));

        step("user choose pay with cc");
        paymentPage.openPaymentMethod();
        paymentPage.selectPaymentMethod(payment_method);

        if(Configs.ENVIRONMENT == Environment.GK || Configs.ENVIRONMENT == Environment.PREPROD){
            step("user fill cc detail");
            paymentPage.fillCCDetailRealIssued(cardNumber, expiry, cvv, cardName);
            paymentPage.clickPayButtonCC();
            paymentPage.verifyCreditCard3ds();

            step("Open the order list and go to order detail");
            paymentPage.seeOrderList();
            orderListPage.openOrderDetail(productName);
            orderListPage.removePopUp();

            step("Assertion");
            Assertion.assertThat("order id is match",orderDetailPage.getFullOrderId(),containsString(orderID));
        }else{
            step("verify payment method is selected correctly");
            Assertion.assertThat("is payment method selected correctly",paymentPage.isCCSelectedAsPaymentMethod(),is(true));
        }
    }

    @Author(names = {"Kartik","Ashish"})
    @Module(name = "CompleteBooking")
    @Tribe(name = "CarRental")
    @Vertical(name = "GroundTransport")
    @Test
    public void testGuestCompleteBookingWithDriverAndFuelPackage() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String,String> params = getParam("testGuestCompleteBookingWithDriverAndFuelPackage");
        BaseTest.paramsThreadLocal.set(params);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        SearchPageCarRental<WebDriver> carRentalSearchPage = SearchPageCarRental.getInstance(session);
        SearchResultPageCarRental<WebDriver> carRentalSearchResultPage = SearchResultPageCarRental.getInstance(session);
        CarRentalReviewBookingPage<WebDriver> carRentalReviewBookingPage = CarRentalReviewBookingPage.getInstance(session);
        CarRentalBookingPage<WebDriver> carRentalBookingPage = CarRentalBookingPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        LoginPage <WebDriver> loginPage = LoginPage.getInstance(session);

        String Location = params.get("location");
        String username = params.get("username");
        String pickupLocation = params.get("pickupLocation");
        String dropOffLocation = params.get("dropOffLocation");
        String carRentalPackage = params.get("carRentalPackage");
        String carRentalDuration = params.get("carRentalDuration");
        String dateRental = CoreFunctions.getCurrentLongDate();
        String phoneNumber = params.get("phoneNumber");
        String currency = params.get("currency");
        int duration = Integer.parseInt(carRentalDuration);
        CarRentalDetail carRentalDetail = new CarRentalDetail();
        carRentalDetail.duration = duration;
        String driverType = "with_driver";

        step("Go To Home");
        homePage.gotoHome();

        step("Logout via deeplink");
        homePage.logoutViaDeeplink();

        if(Configs.ENVIRONMENT == Environment.PROD) {
            step("Change currency to 'IDR'");
            homePage.changeCurrency("IDR","");
        }

        step("Get App Language");
        String language = homePage.getLanguageApp();

        step("Open Car Landing Page");
        homePage.openVerticalPageDeeplink("Car Rentals", language, currency);
        carRentalSearchPage.allowLocationPermission();
        carRentalSearchPage.closeInformationPopUp();

        step("Select car rental type");
        carRentalSearchPage.chooseDriver(driverType);

        step("Select car rental date");
        carRentalSearchPage.selectDate(CoreFunctions.addToLongDate(dateRental,"10-15"));

        step("Input car rental location");
        carRentalSearchPage.chooseLocation(Location);

        step("get selected data landing page");
        carRentalDetail.landingPageData = carRentalSearchPage.getSelectedDataLandingPage();

        step("Search car rental");
        carRentalSearchPage.searchCar(carRentalDuration);

        step("choose car");
        carRentalDetail.srpData = carRentalSearchResultPage.chooseCar(duration);

        step("Select Your Rental Package: " + carRentalPackage);
        carRentalDetail.reviewPageData = carRentalReviewBookingPage.selectYourRentalPackage(carRentalPackage);

        step("car rental detail assertion");
        Assertion.assertThat("check car rental review detail page",carRentalReviewBookingPage.checkCarRentalDetail(carRentalDetail),is(true));

        step("various section assertion");
        Assertion.assertThat("check rental section",carRentalReviewBookingPage.verifyVariousSectionOnThePage(driverType,carRentalDetail),is(true));

        step("verify price on review booking page");
        Assertion.assertThat("verify price detail section on review booking page",carRentalReviewBookingPage.verifyPriceDetailSection(carRentalDetail),is(true));

        step("continue to booking form from review page");
        carRentalReviewBookingPage.continueFromReviewDetailCar();

        step("pick up location");
        carRentalBookingPage.choosePickUpLocation(pickupLocation);

        step("choose drop off location");
        carRentalBookingPage.chooseDropOffLocation(dropOffLocation);

        step("get pickup and drop off data");
        carRentalDetail.bookingPageData = carRentalBookingPage.getPickUpAndDropOffData();

        step("price detail assertion");
        Assertion.assertThat("is price detail correct", carRentalBookingPage.checkPriceDetail(carRentalDetail), is(true));

        step("continue to complete booking");
        carRentalBookingPage.continueToCompleteBookingPage();

        step("verify driverType, pickup and drop-off time & package type(optional)");
        Assertion.assertThat("check driverType, pickup and drop-off time & package type(only for with driver)", carRentalBookingPage.verifyCarRentalDetail(carRentalDetail), is(true));

        step("input contact detail");
        carRentalBookingPage.fillGuestDetailOrder(username,phoneNumber);
        carRentalBookingPage.setContinueToPayment();

        step("get order id");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id { " + orderID+ " }",!orderID.isEmpty(),is(true));
    }

    @Author(names = {"Kartik","Ashish"})
    @Module(name = "CarRental")
    @Tribe(name = "NFT")
    @Vertical(name = "GroundTransport")
    @Test
    public void CarRental_testCompleteBookingWithDriverAndFuelPackageLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String,String> params = getParam("CarRental_testCompleteBookingWithDriverAndFuelPackageLogin");
        BaseTest.paramsThreadLocal.set(params);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        SearchPageCarRental<WebDriver> carRentalSearchPage = SearchPageCarRental.getInstance(session);
        SearchResultPageCarRental<WebDriver> carRentalSearchResultPage = SearchResultPageCarRental.getInstance(session);
        CarRentalReviewBookingPage<WebDriver> carRentalReviewBookingPage = CarRentalReviewBookingPage.getInstance(session);
        CarRentalBookingPage<WebDriver> carRentalBookingPage = CarRentalBookingPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        LoginPage <WebDriver> loginPage = LoginPage.getInstance(session);

        String loginMethod = params.get("loginType");
        String username = params.get("username");
        String password = params.get("password");
        String Location = params.get("location");
        String pickupLocation = params.get("pickupLocation");
        String dropOffLocation = params.get("dropOffLocation");
        String carRentalPackage = params.get("carRentalPackage");
        String carRentalDuration = params.get("carRentalDuration");
        String dateRental = CoreFunctions.getCurrentLongDate();
        String currency = params.get("currency");
        CarRentalDetail carRentalDetail = new CarRentalDetail();
        int duration = Integer.parseInt(carRentalDuration);
        carRentalDetail.duration = duration;
        String driverType = "with_driver";

        step("Go To Home");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink (if needed) and change currency to " + currency);
        homePage.loginWithSpecificAccountViaDeeplink(username, password, currency);

        step("Get App Language");
        String language = homePage.getLanguageApp();

        step("Open Car Landing Page");
        homePage.openVerticalPageDeeplink("Car Rentals", language, currency);
        carRentalSearchPage.allowLocationPermission();
        carRentalSearchPage.closeInformationPopUp();

        step("Select car rental type");
        carRentalSearchPage.chooseDriver(driverType);

        step("Select car rental date");
        carRentalSearchPage.selectDate(CoreFunctions.addToLongDate(dateRental,"10-15"));

        step("Input car rental location");
        carRentalSearchPage.chooseLocation(Location);

        step("get selected data landing page");
        carRentalDetail.landingPageData = carRentalSearchPage.getSelectedDataLandingPage();

        step("Search car rental");
        carRentalSearchPage.searchCar(carRentalDuration);

        step("choose car");
        carRentalDetail.srpData = carRentalSearchResultPage.chooseCar(duration);

        if (Configs.ENVIRONMENT == Environment.PROD) {
            step("Select Your Rental Package: " + carRentalPackage + " as optional package");
        } else {
            step("Select Your Rental Package: " + carRentalPackage);
        }
        carRentalDetail.reviewPageData = carRentalReviewBookingPage.selectYourRentalPackage(carRentalPackage);

        step("car rental detail assertion");
        Assertion.assertThat("check car rental review detail page",carRentalReviewBookingPage.checkCarRentalDetail(carRentalDetail),is(true));

        step("various section assertion");
        Assertion.assertThat("check rental section",carRentalReviewBookingPage.verifyVariousSectionOnThePage(driverType,carRentalDetail),is(true));

        step("verify price on review booking page");
        Assertion.assertThat("verify price detail section on review booking page",carRentalReviewBookingPage.verifyPriceDetailSection(carRentalDetail),is(true));

        step("continue to booking form from review page");
        carRentalReviewBookingPage.continueFromReviewDetailCar();

        step("pick up location");
        carRentalBookingPage.choosePickUpLocation(pickupLocation);

        step("choose drop off location");
        carRentalBookingPage.chooseDropOffLocation(dropOffLocation);

        step("get pickup and drop off data");
        carRentalDetail.bookingPageData = carRentalBookingPage.getPickUpAndDropOffData();

        step("price detail assertion");
        Assertion.assertThat("is price detail correct", carRentalBookingPage.checkPriceDetail(carRentalDetail), is(true));

        step("continue to complete booking");
        carRentalBookingPage.continueToCompleteBookingPage();

        step("verify driverType, pickup and drop-off time & package type(optional)");
        Assertion.assertThat("check driverType, pickup and drop-off time & package type(only for with driver)", carRentalBookingPage.verifyCarRentalDetail(carRentalDetail), is(true));

        step("continue to payment page");
        carRentalBookingPage.setContinueToPayment();

        step("get order id");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id { " + orderID+ " }",!orderID.isEmpty(),is(true));
    }

    @Author(names = {"Kartik","Ashish"})
    @Module(name = "CompleteBooking")
    @Tribe(name = "CarRental")
    @Vertical(name = "GroundTransport")
    @Test
    public void testGuestCompleteBookingWithDriverAndAllInPackage() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String,String> params = getParam("testGuestCompleteBookingWithDriverAndAllInPackage");
        BaseTest.paramsThreadLocal.set(params);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        SearchPageCarRental<WebDriver> carRentalSearchPage = SearchPageCarRental.getInstance(session);
        SearchResultPageCarRental<WebDriver> carRentalSearchResultPage = SearchResultPageCarRental.getInstance(session);
        CarRentalReviewBookingPage<WebDriver> carRentalReviewBookingPage = CarRentalReviewBookingPage.getInstance(session);
        CarRentalBookingPage<WebDriver> carRentalBookingPage = CarRentalBookingPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        LoginPage <WebDriver> loginPage = LoginPage.getInstance(session);

        String username = params.get("username");
        String Location = params.get("location");
        String pickupLocation = params.get("pickupLocation");
        String dropOffLocation = params.get("dropOffLocation");
        String carRentalPackage = params.get("carRentalPackage"); //All-in Package
        String carRentalDuration = params.get("carRentalDuration");
        String dateRental = CoreFunctions.getCurrentLongDate();
        String phoneNumber = params.get("phoneNumber");
        String currency = params.get("currency");
        CarRentalDetail carRentalDetail = new CarRentalDetail();
        int duration = Integer.parseInt(carRentalDuration);
        carRentalDetail.duration = duration;
        String driverType = "with_driver";

        step("Go To Home");
        homePage.gotoHome();

        step("Logout via deeplink");
        homePage.logoutViaDeeplink();

        if(Configs.ENVIRONMENT == Environment.PROD) {
            step("Change currency to 'IDR'");
            homePage.changeCurrency("IDR","");
        }

        step("Get App Language");
        String language = homePage.getLanguageApp();

        step("Open Car Landing Page");
        homePage.openVerticalPageDeeplink("Car Rentals", language, currency);
        carRentalSearchPage.allowLocationPermission();
        carRentalSearchPage.closeInformationPopUp();

        step("Select car rental type");
        carRentalSearchPage.chooseDriver(driverType);

        step("Select car rental date");
        carRentalSearchPage.selectDate(CoreFunctions.addToLongDate(dateRental,"10-15"));

        step("Input car rental location");
        carRentalSearchPage.chooseLocation(Location);

        step("get selected data landing page");
        carRentalDetail.landingPageData = carRentalSearchPage.getSelectedDataLandingPage();

        step("Search car rental");
        carRentalSearchPage.searchCar(carRentalDuration);

        step("choose car");
        carRentalDetail.srpData = carRentalSearchResultPage.chooseCar(duration);

        step("Select Your Rental Package: " + carRentalPackage);
        carRentalDetail.reviewPageData = carRentalReviewBookingPage.selectYourRentalPackage(carRentalPackage);

        step("car rental detail assertion");
        Assertion.assertThat("check car rental review detail page",carRentalReviewBookingPage.checkCarRentalDetail(carRentalDetail),is(true));

        step("various section assertion");
        Assertion.assertThat("check rental section",carRentalReviewBookingPage.verifyVariousSectionOnThePage(driverType,carRentalDetail),is(true));

        step("verify price on review booking page");
        Assertion.assertThat("verify price detail section on review booking page",carRentalReviewBookingPage.verifyPriceDetailSection(carRentalDetail),is(true));

        step("continue to booking form from review page");
        carRentalReviewBookingPage.continueFromReviewDetailCar();

        step("pick up location");
        carRentalBookingPage.choosePickUpLocation(pickupLocation);

        step("choose drop off location");
        carRentalBookingPage.chooseDropOffLocation(dropOffLocation);

        step("get pickup and drop off data");
        carRentalDetail.bookingPageData = carRentalBookingPage.getPickUpAndDropOffData();

        step("price detail assertion");
        Assertion.assertThat("is price detail correct", carRentalBookingPage.checkPriceDetail(carRentalDetail), is(true));

        step("continue to complete booking");
        carRentalBookingPage.continueToCompleteBookingPage();

        step("verify driverType, pickup and drop-off time & package type(optional)");
        Assertion.assertThat("check driverType, pickup and drop-off time & package type(only for with driver)", carRentalBookingPage.verifyCarRentalDetail(carRentalDetail), is(true));

        step("input contact detail");
        carRentalBookingPage.fillGuestDetailOrder(username,phoneNumber);
        carRentalBookingPage.setContinueToPayment();

        step("get order id");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id { " + orderID+ " }",!orderID.isEmpty(),is(true));
    }

    @Author(names = {"Kartik","Ashish"})
    @Module(name = "CarRental")
    @Tribe(name = "NFT")
    @Vertical(name = "GroundTransport")
    @Test
    public void CarRental_testCompleteBookingWithDriverAndAllInPackageLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String,String> params = getParam("CarRental_testCompleteBookingWithDriverAndAllInPackageLogin");
        BaseTest.paramsThreadLocal.set(params);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        SearchPageCarRental<WebDriver> carRentalSearchPage = SearchPageCarRental.getInstance(session);
        SearchResultPageCarRental<WebDriver> carRentalSearchResultPage = SearchResultPageCarRental.getInstance(session);
        CarRentalReviewBookingPage<WebDriver> carRentalReviewBookingPage = CarRentalReviewBookingPage.getInstance(session);
        CarRentalBookingPage<WebDriver> carRentalBookingPage = CarRentalBookingPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        LoginPage <WebDriver> loginPage = LoginPage.getInstance(session);

        String loginMethod = params.get("loginType");
        String username = params.get("username");
        String password = params.get("password");
        String Location = params.get("location");
        String pickupLocation = params.get("pickupLocation");
        String dropOffLocation = params.get("dropOffLocation");
        String carRentalPackage = params.get("carRentalPackage");
        String carRentalDuration = params.get("carRentalDuration");
        String dateRental = CoreFunctions.getCurrentLongDate();
        String currency = params.get("currency");
        CarRentalDetail carRentalDetail = new CarRentalDetail();
        int duration = Integer.parseInt(carRentalDuration);
        carRentalDetail.duration = duration;
        String driverType = "with_driver";

        step("Go To Home");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink (if needed) and change currency to " + currency);
        homePage.loginIfLoggedOutAndChangeCurrencyViaDeeplink(username, password, currency);

        step("Get App Language");
        String language = homePage.getLanguageApp();

        step("Open Car Landing Page");
        homePage.openVerticalPageDeeplink("Car Rentals", language, currency);
        carRentalSearchPage.allowLocationPermission();
        carRentalSearchPage.closeInformationPopUp();

        step("Select car rental type");
        carRentalSearchPage.chooseDriver(driverType);

        step("Select car rental date");
        carRentalSearchPage.selectDate(CoreFunctions.addToLongDate(dateRental,"10-15"));

        step("Input car rental location");
        carRentalSearchPage.chooseLocation(Location);

        step("get selected data landing page");
        carRentalDetail.landingPageData = carRentalSearchPage.getSelectedDataLandingPage();

        step("Search car rental");
        carRentalSearchPage.searchCar(carRentalDuration);

        step("choose car");
        carRentalDetail.srpData = carRentalSearchResultPage.chooseCar(duration);

        step("Select Your Rental Package: " + carRentalPackage);
        carRentalDetail.reviewPageData = carRentalReviewBookingPage.selectYourRentalPackage(carRentalPackage);

        step("car rental detail assertion");
        Assertion.assertThat("check car rental review detail page",carRentalReviewBookingPage.checkCarRentalDetail(carRentalDetail),is(true));

        step("various section assertion");
        Assertion.assertThat("check rental section",carRentalReviewBookingPage.verifyVariousSectionOnThePage(driverType,carRentalDetail),is(true));

        step("verify price on review booking page");
        Assertion.assertThat("verify price detail section on review booking page",carRentalReviewBookingPage.verifyPriceDetailSection(carRentalDetail),is(true));

        step("continue to booking form from review page");
        carRentalReviewBookingPage.continueFromReviewDetailCar();

        step("pick up location");
        carRentalBookingPage.choosePickUpLocation(pickupLocation);

        step("choose drop off location");
        carRentalBookingPage.chooseDropOffLocation(dropOffLocation);

        step("get pickup and drop off data");
        carRentalDetail.bookingPageData = carRentalBookingPage.getPickUpAndDropOffData();

        step("price detail assertion");
        Assertion.assertThat("is price detail correct", carRentalBookingPage.checkPriceDetail(carRentalDetail), is(true));

        step("continue to complete booking");
        carRentalBookingPage.continueToCompleteBookingPage();

        step("verify driverType, pickup and drop-off time & package type(optional)");
        Assertion.assertThat("check driverType, pickup and drop-off time & package type(only for with driver)", carRentalBookingPage.verifyCarRentalDetail(carRentalDetail), is(true));

        step("continue to payment page");
        carRentalBookingPage.setContinueToPayment();

        step("get order id");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id { " + orderID+ " }",!orderID.isEmpty(),is(true));
    }

    @Author(names = {"Kartik","Ashish"})
    @Module(name = "CompleteBooking")
    @Tribe(name = "CarRental")
    @Vertical(name = "GroundTransport")
    @Test
    public void testCompleteBookingWithDriverAndCarOnlyPackage() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String,String> params = getParam("testCompleteBookingWithDriverAndCarOnlyPackage");
        BaseTest.paramsThreadLocal.set(params);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        SearchPageCarRental<WebDriver> carRentalSearchPage = SearchPageCarRental.getInstance(session);
        SearchResultPageCarRental<WebDriver> carRentalSearchResultPage = SearchResultPageCarRental.getInstance(session);
        CarRentalReviewBookingPage<WebDriver> carRentalReviewBookingPage = CarRentalReviewBookingPage.getInstance(session);
        CarRentalBookingPage<WebDriver> carRentalBookingPage = CarRentalBookingPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        LoginPage <WebDriver> loginPage = LoginPage.getInstance(session);

        String username = params.get("username");
        String Location = params.get("location");
        String pickupLocation = params.get("pickupLocation");
        String dropOffLocation = params.get("dropOffLocation");
        String carRentalPackage = params.get("carRentalPackage");
        String carRentalDuration = params.get("carRentalDuration");
        String dateRental = CoreFunctions.getCurrentLongDate();
        String phoneNumber = params.get("phoneNumber");
        String currency = params.get("currency");
        CarRentalDetail carRentalDetail = new CarRentalDetail();
        int duration = Integer.parseInt(carRentalDuration);
        carRentalDetail.duration = duration;
        String driverType = "with_driver";

        step("Go To Home");
        homePage.gotoHome();

        step("Logout via deeplink");
        homePage.logoutViaDeeplink();

        if(Configs.ENVIRONMENT == Environment.PROD) {
            step("Change currency to 'IDR'");
            homePage.changeCurrency("IDR","");
        }

        step("Get App Language");
        String language = homePage.getLanguageApp();

        step("Open Car Landing Page");
        homePage.openVerticalPageDeeplink("Car Rentals", language, currency);
        carRentalSearchPage.allowLocationPermission();
        carRentalSearchPage.closeInformationPopUp();

        step("Select car rental type");
        carRentalSearchPage.chooseDriver(driverType);

        step("Select car rental date");
        carRentalSearchPage.selectDate(CoreFunctions.addToLongDate(dateRental,"10-15"));

        step("Input car rental location");
        carRentalSearchPage.chooseLocation(Location);

        step("get selected data landing page");
        carRentalDetail.landingPageData = carRentalSearchPage.getSelectedDataLandingPage();

        step("Search car rental");
        carRentalSearchPage.searchCar(carRentalDuration);

        step("choose car");
        carRentalDetail.srpData = carRentalSearchResultPage.chooseCar(duration);

        step("Select Your Rental Package: " + carRentalPackage);
        carRentalDetail.reviewPageData = carRentalReviewBookingPage.selectYourRentalPackage(carRentalPackage);

        step("car rental detail assertion");
        Assertion.assertThat("check car rental review detail page",carRentalReviewBookingPage.checkCarRentalDetail(carRentalDetail),is(true));

        step("various section assertion");
        Assertion.assertThat("check rental section",carRentalReviewBookingPage.verifyVariousSectionOnThePage(driverType,carRentalDetail),is(true));

        step("verify price on review booking page");
        Assertion.assertThat("verify price detail section on review booking page",carRentalReviewBookingPage.verifyPriceDetailSection(carRentalDetail),is(true));

        step("continue to booking form from review page");
        carRentalReviewBookingPage.continueFromReviewDetailCar();

        step("pick up location");
        carRentalBookingPage.choosePickUpLocation(pickupLocation);

        step("choose drop off location");
        carRentalBookingPage.chooseDropOffLocation(dropOffLocation);

        step("get pickup and drop off data");
        carRentalDetail.bookingPageData = carRentalBookingPage.getPickUpAndDropOffData();

        step("price detail assertion");
        Assertion.assertThat("is price detail correct", carRentalBookingPage.checkPriceDetail(carRentalDetail), is(true));

        step("continue to complete booking");
        carRentalBookingPage.continueToCompleteBookingPage();

        step("verify driverType, pickup and drop-off time & package type(optional)");
        Assertion.assertThat("check driverType, pickup and drop-off time & package type(only for with driver)", carRentalBookingPage.verifyCarRentalDetail(carRentalDetail), is(true));

        step("input contact detail");
        carRentalBookingPage.fillGuestDetailOrder(username,phoneNumber);
        carRentalBookingPage.setContinueToPayment();

        step("get order id");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id { " + orderID+ " }",!orderID.isEmpty(),is(true));
    }

    @Author(names = {"Kartik","Ashish"})
    @Module(name = "CompleteBooking")
    @Tribe(name = "CarRental")
    @Vertical(name = "GroundTransport")
    @Test
    public void testCompleteBookingWithDriverAndCarOnlyPackageLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String,String> params = getParam("testCompleteBookingWithDriverAndCarOnlyPackageLogin");
        BaseTest.paramsThreadLocal.set(params);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        SearchPageCarRental<WebDriver> carRentalSearchPage = SearchPageCarRental.getInstance(session);
        SearchResultPageCarRental<WebDriver> carRentalSearchResultPage = SearchResultPageCarRental.getInstance(session);
        CarRentalReviewBookingPage<WebDriver> carRentalReviewBookingPage = CarRentalReviewBookingPage.getInstance(session);
        CarRentalBookingPage<WebDriver> carRentalBookingPage = CarRentalBookingPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        LoginPage <WebDriver> loginPage = LoginPage.getInstance(session);

        String loginMethod = params.get("loginType");
        String username = params.get("username");
        String password = params.get("password");
        String Location = params.get("location");
        String pickupLocation = params.get("pickupLocation");
        String dropOffLocation = params.get("dropOffLocation");
        String carRentalPackage = params.get("carRentalPackage");
        String carRentalDuration = params.get("carRentalDuration");
        String dateRental = CoreFunctions.getCurrentLongDate();
        String phoneNumber = params.get("phoneNumber");
        String currency = params.get("currency");
        CarRentalDetail carRentalDetail = new CarRentalDetail();
        int duration = Integer.parseInt(carRentalDuration);
        carRentalDetail.duration = duration;
        String driverType = "with_driver";

        step("Go To Home");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink (if needed) and change currency to " + currency);
        homePage.loginWithSpecificAccountViaDeeplink(username, password, currency);

        step("Get App Language");
        String language = homePage.getLanguageApp();

        step("Open Car Landing Page");
        homePage.openVerticalPageDeeplink("Car Rentals", language, currency);
        carRentalSearchPage.allowLocationPermission();
        carRentalSearchPage.closeInformationPopUp();

        step("Select car rental type");
        carRentalSearchPage.chooseDriver(driverType);

        step("Select car rental date");
        carRentalSearchPage.selectDate(CoreFunctions.addToLongDate(dateRental,"10-15"));

        step("Input car rental location");
        carRentalSearchPage.chooseLocation(Location);

        step("get selected data landing page");
        carRentalDetail.landingPageData = carRentalSearchPage.getSelectedDataLandingPage();

        step("Search car rental");
        carRentalSearchPage.searchCar(carRentalDuration);

        step("choose car");
        carRentalDetail.srpData = carRentalSearchResultPage.chooseCar(duration);

        step("Select Your Rental Package: " + carRentalPackage);
        carRentalDetail.reviewPageData = carRentalReviewBookingPage.selectYourRentalPackage(carRentalPackage);

        step("car rental detail assertion");
        Assertion.assertThat("check car rental review detail page",carRentalReviewBookingPage.checkCarRentalDetail(carRentalDetail),is(true));

        step("various section assertion");
        Assertion.assertThat("check rental section",carRentalReviewBookingPage.verifyVariousSectionOnThePage(driverType,carRentalDetail),is(true));

        step("verify price on review booking page");
        Assertion.assertThat("verify price detail section on review booking page",carRentalReviewBookingPage.verifyPriceDetailSection(carRentalDetail),is(true));

        step("continue to booking form from review page");
        carRentalReviewBookingPage.continueFromReviewDetailCar();

        step("pick up location");
        carRentalBookingPage.choosePickUpLocation(pickupLocation);

        step("choose drop off location");
        carRentalBookingPage.chooseDropOffLocation(dropOffLocation);

        step("get pickup and drop off data");
        carRentalDetail.bookingPageData = carRentalBookingPage.getPickUpAndDropOffData();

        step("price detail assertion");
        Assertion.assertThat("is price detail correct", carRentalBookingPage.checkPriceDetail(carRentalDetail), is(true));

        step("continue to complete booking");
        carRentalBookingPage.continueToCompleteBookingPage();

        step("verify driverType, pickup and drop-off time & package type(optional)");
        Assertion.assertThat("check driverType, pickup and drop-off time & package type(only for with driver)", carRentalBookingPage.verifyCarRentalDetail(carRentalDetail), is(true));

        step("continue to payment page");
        carRentalBookingPage.setContinueToPayment();

        step("get order id");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id { " + orderID+ " }",!orderID.isEmpty(),is(true));
    }

    @DataProvider(name = "testLoginCompleteBookingWithDriverDataProvider")
    public Object[][] testLoginCompleteBookingWithDriverDataProvider(){
        return getObjectParams("testLoginCompleteBookingWithDriver");
    }
    @DataProvider(name = "testLoginCompleteBookingWithoutDriverDataProvider")
    public Object[][] testLoginCompleteBookingWithoutDriverDataProvider() {
        return getObjectParams("testLoginCompleteBookingWithoutDriver");
    }

    @DataProvider(name = "testLoginCompleteBookingWithDriverAndExtraFacilitiesDataProvider")
    public Object[][] testLoginCompleteBookingWithDriverAndExtraFacilitiesDataProvider(){
        return getObjectParams("testLoginCompleteBookingWithDriverAndExtraFacilities");
    }
    @DataProvider(name = "testLoginCompleteBookingWithoutDriverAndExtraFacilitiesDataProvider")
    public Object[][] testLoginCompleteBookingWithoutDriverAndExtraFacilitiesDataProvider() {
        return getObjectParams("testLoginCompleteBookingWithoutDriverAndExtraFacilities");
    }

    @DataProvider(name = "testGuestCompleteBookingWithoutDriverDataProvider")
    public Object[][] testGuestCompleteBookingWithoutDriverDataProvider() {
        return getObjectParams("testGuestCompleteBookingWithoutDriver");
    }
    @DataProvider(name = "testGuestCompleteBookingWithoutDriverAndExtraFacilitiesDataProvider")
    public Object[][] testGuestCompleteBookingWithoutDriverAndExtraFacilitiesDataProvider() {
        return getObjectParams("testGuestCompleteBookingWithoutDriverAndExtraFacilities");
    }

    @DataProvider(name = "testGuestCompleteBookingWithDriverAndExtraFacilitiesDataProvider")
    public Object[][] testGuestCompleteBookingWithDriverAndExtraFacilitiesDataProvider() {
        return getObjectParams("testGuestCompleteBookingWithDriverAndExtraFacilities");
    }

    @DataProvider(name = "testGuestCompleteBookingWithDriverDataProvider")
    public Object[][] testGuestCompleteBookingWithDriverDataProvider() {
        return getObjectParams("testGuestCompleteBookingWithDriver");
    }

    @DataProvider(name = "testGuestCheckEditLocationDataProvider")
    public Object[][] testGuestCheckEditLocationDataProvider(){
        return getObjectParams("testGuestCheckEditLocation");
    }

    @DataProvider(name = "testLoginCheckEditLocationDataProvider")
    public Object[][] testLoginCheckEditLocationDataProvider() {
        return getObjectParams("testLoginCheckEditLocation");

    }

    @DataProvider(name = "testGuestCheckReviewBookingPageDataProvider")
    public Object[][] testGuestCheckReviewBookingPageDataProvider() {
        return getObjectParams("testGuestCheckReviewBookingPage");
    }

    @DataProvider(name = "testLoginCheckReviewBookingPageDataProvider")
    public Object[][] testLoginCheckReviewBookingPageDataProvider() {
        return getObjectParams("testLoginCheckReviewBookingPage");
    }

    @DataProvider(name = "testLoginCheckDetailBookingFormWithDriverDataProvider")
    public Object[][] testLoginCheckDetailBookingFormWithDriverDataProvider() {
        return getObjectParams("testLoginCheckDetailBookingFormWithDriver");
    }

    @DataProvider(name = "testGuestCheckDetailBookingFormWithDriverDataProvider")
    public Object[][] testGuestCheckDetailBookingFormWithDriverDataProvider() {
        return getObjectParams("testGuestCheckDetailBookingFormWithDriver");
    }

    @DataProvider(name = "testLoginCheckDetailBookingFormWithoutDriverDataProvider")
    public Object[][] testLoginCheckDetailBookingFormWithoutDriverDataProvider() {
        return getObjectParams("testLoginCheckDetailBookingFormWithoutDriver");
    }

    @DataProvider(name = "testGuestCheckDetailBookingFormWithoutDriverDataProvider")
    public Object[][] testGuestCheckDetailBookingFormWithoutDriverDataProvider() {
        return getObjectParams("testGuestCheckDetailBookingFormWithoutDriver");
    }

    @DataProvider(name = "testCheckReviewBookingPageWithoutDriverDataProvider")
    public Object[][] testCheckReviewBookingPageWithoutDriverDataProvider(){
        return getObjectParams("testCheckReviewBookingPageWithoutDriver");
    }

    @DataProvider(name = "testLoginCheckReviewBookingPageWithoutDriverDataProvider")
    public Object[][] testLoginCheckReviewBookingPageWithoutDriverDataProvider(){
        return getObjectParams("testLoginCheckReviewBookingPageWithoutDriver");
    }
}