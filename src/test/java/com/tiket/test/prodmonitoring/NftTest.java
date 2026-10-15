package com.tiket.test.prodmonitoring;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.apiPage.FerryApi;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Platform;
import com.tiket.model.Session;
import com.tiket.page.bus.bookingform.BusBookingFormPage;
import com.tiket.page.bus.pdp.BuspdpPage;
import com.tiket.page.bus.search.BusSearchPage;
import com.tiket.page.bus.searchresultpage.BusSearchResultPage;
import com.tiket.page.carrental.bookingpage.CarRentalBookingPage;
import com.tiket.page.carrental.reviewbookingpage.CarRentalReviewBookingPage;
import com.tiket.page.carrental.searchpage.SearchPageCarRental;
import com.tiket.page.carrental.searchresultpage.SearchResultPageCarRental;
import com.tiket.page.cartransfer.bookingform.CarTransferBookingPage;
import com.tiket.page.cartransfer.searchpage.CarTransferSearchPage;
import com.tiket.page.cartransfer.searchresultpage.CarTransferSearchResultPage;
import com.tiket.page.common.nftPopup.NFTCommonPopup;
import com.tiket.page.common.webview.WebViewPage;
import com.tiket.page.ferry.FerryBookingPage.FerryBookingPage;
import com.tiket.page.ferry.FerrySearchPage.FerrySearchPage;
import com.tiket.page.ferry.FerrySearchresultPage.FerrySearchResultPage;
import com.tiket.page.home.HomePage;
import com.tiket.page.login.LoginPage;
import com.tiket.page.payment.PaymentPage;
import com.tiket.page.train.bookingform.TrainBookingPage;
import com.tiket.page.train.searchpage.TrainSearchPage;
import com.tiket.page.train.searchresultpage.TrainSearchResultPage;
import com.tiket.page.whoosh.bookingform.WhooshBookingPage;
import com.tiket.page.whoosh.searchpage.WhooshSearchPage;
import com.tiket.page.whoosh.searchresultpage.WhooshSearchResultPage;
import com.tiket.pojo.carrental.CarRentalDetail;
import com.tiket.pojo.whoosh.TrainAndWhooshGeneralData;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.testbase.Configs;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import java.util.Map;

import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;

public class NftTest extends BaseTest {

    @Author(names = {"Dendi","Zia", "Aditi"})
    @Module(name = "CompleteBooking")
    @Tribe(name = "AirportTransfer")
    @Vertical(name = "NFT")
    @Test
    public void testCTInLoginProdMonitoringAirportTransfer() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("testCTInLoginProdMonitoringAirportTransfer");
        BaseTest.paramsThreadLocal.set(map);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        CarTransferSearchPage<WebDriver> carTransferSearchPage = CarTransferSearchPage.getInstance(session);
        CarTransferSearchResultPage<WebDriver> carTransferSearchResultPage = CarTransferSearchResultPage.getInstance(session);
        CarTransferBookingPage<WebDriver> carTransferBookingPage = CarTransferBookingPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        NFTCommonPopup<WebDriver> nftCommonPopup = NFTCommonPopup.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);

        String airport = map.get("airport");
        String destination = map.get("destination");
        String currency = map.get("currency");
        String loginMethod = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");

        step("User open app");
        homePage.gotoHome();

        step("Login page");
        loginPage.loginWith(loginMethod, username, password);

        step("Home page");
        homePage.changeCurrency("IDR",username);
        homePage.openVerticalPageDeeplink("Car Transfers", "", currency);

        step("Landing page");
        nftCommonPopup.clickCloseButton();
        carTransferSearchPage.allowLocationAccess();
        webViewPage.switchToWebViewUrl("airport-transfer");
        carTransferSearchPage.closePopups();
        carTransferSearchPage.chooseTransferType("transfer_in");
        carTransferSearchPage.toggleForLaterOrder();
        carTransferSearchPage.choosePickups("transfer_in", airport, destination);
        carTransferSearchPage.searchCar();

        if(Configs.PLATFORM == Platform.IOS){
            webViewPage.switchToNative();
            carTransferSearchPage.allowLocationAccess();
            webViewPage.switchToWebViewUrl("airport-transfer");
        }

        step("Search result page");
        carTransferSearchResultPage.selectCar();
        carTransferSearchResultPage.selectVendor();

        step("Product review page");
        carTransferSearchResultPage.continueToBookingPage();

        step("Booking form page");
        carTransferBookingPage.continueToPayment();
        webViewPage.switchToNative();

        step("Payment page");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id", orderID, allOf(
                not(nullValue()),
                not(equalTo(""))
        ));
    }

    @Author(names = {"Zia"})
    @Module(name = "CompleteBooking")
    @Tribe(name = "Bus")
    @Vertical(name = "NFT")
    @Test
    public void testBookOnewayLoginProdMonitoringBus(){
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> testProperties = getParam("testBookOnewayLoginProdMonitoringBus");
        BaseTest.paramsThreadLocal.set(testProperties);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        BusSearchPage<WebDriver> busSearchPage = BusSearchPage.getInstance(session);
        BusSearchResultPage<WebDriver> busSearchResultPage = BusSearchResultPage.getInstance(session);
        BuspdpPage<WebDriver> buspdpPage = BuspdpPage.getInstance(session);
        BusBookingFormPage<WebDriver> busBookingFormPage = BusBookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        NFTCommonPopup<WebDriver> nftCommonPopup = NFTCommonPopup.getInstance(session);

        String loginMethod = testProperties.get("loginType");
        String username = testProperties.get("username");
        String password = testProperties.get("password");

        String tripType = testProperties.get("tripType");
        String originLocation = testProperties.get("originLocation");
        String destinationLocation = testProperties.get("destinationLocation");
        int daysToDeparture = Integer.parseInt(testProperties.get("daysToDeparture"));
        daysToDeparture = CoreFunctions.generateRandomInt(daysToDeparture, daysToDeparture + 10);
        String currentDate = CoreFunctions.getCurrentLongDate();
        String dateDeparture = CoreFunctions.addToLongDate(currentDate,daysToDeparture);
        int passenger = Integer.parseInt(testProperties.get("passenger"));

        step("User open app");
        homePage.gotoHome();

        step("Login page");
        loginPage.loginWith(loginMethod, username, password);

        step("Home page");
        homePage.changeCurrency("IDR",username);
        homePage.openVerticalPageDeeplink("Bus", "", "IDR");

        step("Landing page");
        nftCommonPopup.clickCloseButton();
        busSearchPage.skipCoachMark();
        busSearchPage.switchTripType(tripType);
        busSearchPage.inputOriginLocation(originLocation);
        busSearchPage.inputDestinationLocation(destinationLocation);
        busSearchPage.selectDate(dateDeparture);
        busSearchPage.selectPassenger(passenger);
        busSearchPage.clickSearchBus();

        step("Search result page");
        busSearchResultPage.skipCoachMark();
        busSearchResultPage.selectFirstBus();

        step("Product detail page");
        buspdpPage.openSeat();
        buspdpPage.clickSelectSeatButton();

        step("Booking page");
        busBookingFormPage.togglePassengerSameAsContact();
        busBookingFormPage.fillPassengerData(passenger);
        busBookingFormPage.continueToPaymentPage();

        step("Payment page");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id", orderID, allOf(
                not(nullValue()),
                not(equalTo(""))
        ));
    }

    @Author(names = {"Kartik"})
    @Module(name = "TrainCompleteBookingRoundtripInsurance")
    @Tribe(name = "Train")
    @Vertical(name = "NFT")
    @Test
    public void testCompleteBookingRoundTripLoginProdMonitoringTrain(){
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testCompleteBookingRoundTripLoginProdMonitoringTrain");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TrainSearchPage<WebDriver> trainSearchPage = TrainSearchPage.getInstance(session);
        TrainSearchResultPage<WebDriver> trainSearchResultPage = TrainSearchResultPage.getInstance(session);
        TrainBookingPage<WebDriver> trainBookingPage = TrainBookingPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        NFTCommonPopup<WebDriver> nftCommonPopup = NFTCommonPopup.getInstance(session);

        String tripType = param.get("tripType");
        String currency = param.get("currency");
        String originStation = param.get("originStation");
        String destinationStation = param.get("destinationStation");
        String dateRange = param.get("dateRange");

        int duration = Integer.parseInt(param.get("duration"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int infantPassengers = Integer.parseInt(param.get("infant"));

        TrainAndWhooshGeneralData trainAndWhooshGeneralData = new TrainAndWhooshGeneralData(originStation, destinationStation,adultPassengers,adultPassengers,infantPassengers,tripType,dateRange,duration);
        String departureDate = trainAndWhooshGeneralData.departureDateLong;
        String returnDate = trainAndWhooshGeneralData.returnDateLong;


        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Login page");
        loginPage.loginWith(loginType, username, password);

        step("Home page");
        homePage.changeCurrency("IDR",username);

        step("Open Train Landing Page");
        homePage.openVerticalPageDeeplink("Trains", "", currency);


        step("Landing page");
        nftCommonPopup.clickCloseButton();
        trainSearchPage.skipPopup();
        trainSearchPage.selectOrigin(originStation);
        trainSearchPage.selectDestination(destinationStation);
        trainSearchPage.toggleTripType(tripType);
        trainSearchPage.selectDate(departureDate,returnDate);
        trainSearchPage.addPassenger(adultPassengers,infantPassengers);
        trainSearchPage.searchTrain();

        step("Search result page");
        trainSearchPage.skipPopup();
        trainSearchResultPage.selectTrain();
        trainSearchResultPage.selectTrain();

        step("Booking page");
        trainBookingPage.fillPassengerDetail(adultPassengers,infantPassengers);
        trainBookingPage.continueTo2ndHalf();
        trainBookingPage.continueToPayment();

        step("Payment page");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id", orderID, allOf(
                not(nullValue()),
                not(equalTo(""))
        ));
    }

    @Author(names = {"Ashish"})
    @Module(name = "RoundTripInsurance")
    @Tribe(name = "Whoosh")
    @Vertical(name = "NFT")
    @Test
    public void testCompleteBookingRoundTripLoginProdMonitoringWhoosh(){
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testCompleteBookingRoundTripLoginProdMonitoringWhoosh");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        WhooshSearchPage<WebDriver> whooshSearchPage = WhooshSearchPage.getInstance(session);
        WhooshSearchResultPage<WebDriver> whooshSearchResultPage = WhooshSearchResultPage.getInstance(session);
        WhooshBookingPage<WebDriver> whooshBookingPage = WhooshBookingPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        NFTCommonPopup<WebDriver> nftCommonPopup = NFTCommonPopup.getInstance(session);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String tripType = param.get("tripType");
        String currency = param.get("currency");
        String originStation = param.get("originStation");
        String destinationStation = param.get("destinationStation");
        String dateRange = param.get("dateRange");

        int duration = Integer.parseInt(param.get("duration"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int infantPassengers = Integer.parseInt(param.get("infant"));

        TrainAndWhooshGeneralData trainAndWhooshGeneralData = new TrainAndWhooshGeneralData(originStation, destinationStation,adultPassengers,adultPassengers,infantPassengers,tripType,dateRange,duration);
        String departureDate = trainAndWhooshGeneralData.departureDateLong;
        String returnDate = trainAndWhooshGeneralData.returnDateLong;

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Login page");
        loginPage.loginWith(loginType, username, password);

        step("Home page");
        homePage.changeCurrency("IDR","");
        homePage.openVerticalPageDeeplink("Whoosh", "", currency);

        step("Landing page");
        nftCommonPopup.clickCloseButton();
        whooshSearchPage.skipPopup();
        whooshSearchPage.selectOrigin(originStation);
        whooshSearchPage.selectDestination(destinationStation);
        whooshSearchPage.selectRoundTripToggle(tripType);
        whooshSearchPage.selectDate(departureDate,returnDate);
        whooshSearchPage.addPassenger(adultPassengers,infantPassengers);
        whooshSearchPage.searchTrain();

        step("Search result page");
        whooshSearchPage.skipPopup();
        whooshSearchResultPage.selectWhoosh();

        step("Product detail page");
        whooshSearchResultPage.selectWhoosh();

        step("Booking page");
        whooshBookingPage.fillPassengerDetail(adultPassengers,infantPassengers);
        whooshBookingPage.continueTo2ndHalf();
        whooshBookingPage.continueToPayment();

        step("Payment page");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id", orderID, allOf(
                not(nullValue()),
                not(equalTo(""))
        ));
    }

    @Author(names = {"Kartik","Ashish"})
    @Module(name = "CarCompleteBooking")
    @Tribe(name = "CarRental")
    @Vertical(name = "NFT")
    @Test
    public void testLoginCompleteBookingWithDriverProdMonitoringCarRental(){
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> params = getParam("testLoginCompleteBookingWithDriverProdMonitoringCarRental");
        BaseTest.paramsThreadLocal.set(params);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        SearchPageCarRental<WebDriver> carRentalSearchPage = SearchPageCarRental.getInstance(session);
        SearchResultPageCarRental<WebDriver> carRentalSearchResultPage = SearchResultPageCarRental.getInstance(session);
        CarRentalReviewBookingPage<WebDriver> carRentalReviewBookingPage = CarRentalReviewBookingPage.getInstance(session);
        CarRentalBookingPage<WebDriver> carRentalBookingPage = CarRentalBookingPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        LoginPage <WebDriver> loginPage = LoginPage.getInstance(session);
        NFTCommonPopup<WebDriver> nftCommonPopup = NFTCommonPopup.getInstance(session);


        String Location = params.get("location");
        String pickupLocation = params.get("pickupLocation");
        String carRentalDuration = params.get("carRentalDuration");
        String loginMethod = params.get("loginType");
        String username = params.get("username");
        String password = params.get("password");
        String dateRental = CoreFunctions.getCurrentLongDate();
        String currency = params.get("currency");
        String dateRange = params.get("dateRange");
        CarRentalDetail carRentalDetail = new CarRentalDetail();
        int duration = Integer.parseInt(carRentalDuration);
        String driverType = "with_driver";

        step("Go To Home");
        homePage.gotoHome();

        step("Login page");
        loginPage.loginWith(loginMethod, username, password);

        step("Home page");
        homePage.changeCurrency("IDR",username);
        homePage.openVerticalPageDeeplink("Car Rentals", "", currency);

        step("Landing page");
        nftCommonPopup.clickCloseButton();
        carRentalSearchPage.allowLocationPermission();
        carRentalSearchPage.closeInformationPopUp();
        carRentalSearchPage.chooseDriver(driverType);
        carRentalSearchPage.selectDate(CoreFunctions.addToLongDate(dateRental,dateRange));
        carRentalSearchPage.chooseLocation(Location);
        carRentalDetail.landingPageData = carRentalSearchPage.getSelectedDataLandingPage();
        carRentalSearchPage.searchCar(carRentalDuration);

        step("Search result page");
        carRentalDetail.srpData = carRentalSearchResultPage.chooseCar(duration);

        step("Product review page");
        carRentalReviewBookingPage.continueFromReviewDetailCar();

        step("Booking page");
        carRentalBookingPage.choosePickUpLocationAndToggleForDropOff(pickupLocation);
        carRentalBookingPage.continueToCompleteBookingPage();
        Assertion.assertThat("check driverType, pickup and drop-off time & package type(only for with driver)", carRentalBookingPage.verifyCarRentalDetail(carRentalDetail), is(true));
        carRentalBookingPage.setContinueToPayment();

        step("Payment page");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id", orderID, allOf(
                not(nullValue()),
                not(equalTo(""))
        ));
    }

    @Author(names = {"Kartik"})
    @Module(name = "CompleteBooking")
    @Tribe(name = "Ferry")
    @Vertical(name = "NFT")
    @Test
    public void testCompleteBookingWithPedestrianProdMonitoringFerry() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testCompleteBookingWithPedestrianProdMonitoringFerry");

        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        FerryApi<?> ferryApi = new FerryApi<>(session);
        FerrySearchPage<WebDriver> ferrySearchPage = FerrySearchPage.getInstance(session);
        FerrySearchResultPage<WebDriver> ferrySearchResultPage = FerrySearchResultPage.getInstance(session);
        FerryBookingPage<WebDriver> ferryBookingPage = FerryBookingPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        NFTCommonPopup<WebDriver> nftCommonPopup = NFTCommonPopup.getInstance(session);

        String loginMethod = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String VaccineVerifiedID = param.get("VaccineVerifiedID");
        String originPort = param.get("originPort");
        String destinationPort = param.get("destinationPort");
        String ticketType = param.get("ticketType");
        String vaccineCheck = param.get("vaccineCheck");
        int adult = Integer.parseInt(param.get("adult"));
        int child = Integer.parseInt(param.get("child"));
        int infant = Integer.parseInt(param.get("infant"));
        int elder = Integer.parseInt(param.get("elder"));

        step("Get available date from API");
        String departureDate = ferryApi.getFerryAvailableDate(originPort, destinationPort, ticketType, adult, child, infant, elder);

        step("user open app");
        homePage.gotoHome();

        step("Login page");
        loginPage.loginWith(loginMethod, username, password);

        step("Home page");
        homePage.changeCurrency("IDR",username);
        homePage.openVerticalPageDeeplink("Ferry", "", "IDR");


        step("Landing page");
        nftCommonPopup.clickCloseButton();
        ferrySearchPage.skipCoachMark();
        ferrySearchPage.allowLocationPopup();
        ferrySearchPage.inputOrigin(originPort);
        ferrySearchPage.inputDestination(destinationPort);
        ferrySearchPage.openTicketQty();
        ferrySearchPage.setTicketQty(adult, child, infant, elder);
        ferrySearchPage.saveTicketQty();
        ferrySearchPage.selectDate(departureDate);
        ferrySearchPage.selectTicketType(ticketType);
        ferrySearchPage.submitSearch();

        step("Search result page");
        ferrySearchResultPage.skipCoachMark();
        ferrySearchResultPage.chooseFirstFleet();

        step("Booking page");
        ferryBookingPage.fillGuestsAndVaccineDetails(vaccineCheck, VaccineVerifiedID, adult, elder);
        ferryBookingPage.clickTncCheckBox();
    }
}
