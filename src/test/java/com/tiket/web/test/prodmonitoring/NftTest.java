package com.tiket.web.test.prodmonitoring;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.web.WebTest;
import com.tiket.web.dWeb.page.payment.PaymentPage;
import com.tiket.web.page.common.calendar.CalendarPageWeb;
import com.tiket.web.page.common.home.HomePageWeb;
import com.tiket.web.page.common.home.LoginPageWeb;
import com.tiket.web.page.nft.airport_transfer.booking_page.AirportTransferBookingPageWeb;
import com.tiket.web.page.nft.airport_transfer.landing_page.AirportTransferLandingPageWeb;
import com.tiket.web.page.nft.airport_transfer.pdp.AirportTransferPDPWeb;
import com.tiket.web.page.nft.airport_transfer.srp.AirportTransferSRPWeb;
import com.tiket.web.page.nft.bus.bookingForm_Page.BusBookingFormPageWeb;
import com.tiket.web.page.nft.bus.productDetail_Page.BusPdpPageWeb;
import com.tiket.web.page.nft.bus.searchResult_Page.BusSearchResultPageWeb;
import com.tiket.web.page.nft.bus.search_Page.BusSearchPageWeb;
import com.tiket.web.page.nft.car_rental.bookingForm_Page.CarRentalBookingPageWeb;
import com.tiket.web.page.nft.car_rental.productDetail_page.CarRentalReviewBookingPageWeb;
import com.tiket.web.page.nft.car_rental.searchResult_Page.CarRentalSearchResultPageWeb;
import com.tiket.web.page.nft.car_rental.search_Page.CarRentalSearchPageWeb;
import com.tiket.web.page.nft.ferry.BookingPage.FerryBookingPageWeb;
import com.tiket.web.page.nft.ferry.LandingPage.FerrySearchPageWeb;
import com.tiket.web.page.nft.ferry.SearchResultPage.FerrySearchResultPageWeb;
import com.tiket.web.page.nft.train.bookingForm_Page.TrainBookingFormWeb;
import com.tiket.web.page.nft.train.searchResult_Page.TrainSearchResultPageWeb;
import com.tiket.web.page.nft.train.search_Page.TrainSearchPageWeb;
import com.tiket.web.page.nft.whoosh.booking_page.WhooshBookingPageWeb;
import com.tiket.web.page.nft.whoosh.searchResult_page.WhooshSearchResultPageWeb;
import com.tiket.web.page.nft.whoosh.search_page.WhooshSearchPageWeb;
import com.tiket.web.page.platform.payment.PaymentPageWeb;
import org.testng.annotations.Test;

import javax.security.auth.login.FailedLoginException;
import java.util.Map;

import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;

public class NftTest extends WebTest {

    @Author(names = "Vipul Kumar")
    @Module(name = "CompleteBooking")
    @Tribe(name = "AirportTransfer")
    @Vertical(name = "NFT")
    @Test
    public void testCTInLoginProdMonitoringAirportTransferWeb() throws InterruptedException, FailedLoginException {
        Map<String, String> param = getParam("testCTInLoginProdMonitoringAirportTransferWeb");
        BaseTest.paramsThreadLocal.set(param);

        String location = param.get("location");
        String destination = param.get("destination");
        String currency = param.get("currency");
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String type = param.get("type");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);
        String selectTime = param.get("selectTime");
        String passengerCount = param.get("passengerCount");

        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        AirportTransferLandingPageWeb<?> airportTransferLandingPageWeb = AirportTransferLandingPageWeb.getInstance(session);
        AirportTransferSRPWeb<?> airportTransferSearchPageWeb = AirportTransferSRPWeb.getInstance(session);
        AirportTransferPDPWeb<?> airportTransferProductPageWeb = AirportTransferPDPWeb.getInstance(session);
        AirportTransferBookingPageWeb<?> airportTransferBookingPageWeb = AirportTransferBookingPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("Login page");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("Home page");
        homePage.selectCurrency(currency);
        homePage.openVerticalPageViaURL("airport-transfer");

        step("Landing page");
        airportTransferLandingPageWeb.closePopup();
        airportTransferLandingPageWeb.selectTransferType(type);
        airportTransferLandingPageWeb.fillTransferFields(type,destination,location);
        airportTransferLandingPageWeb.selectDate(departureDate);
        airportTransferLandingPageWeb.selectPassenger(passengerCount);
        airportTransferLandingPageWeb.seeCarListOnSRP();

        step("Search Result Page");
        Assertion.assertThat("Verify the car rental card is displayed or not", airportTransferSearchPageWeb.checkCarRentalCard(), is(true));
        airportTransferSearchPageWeb.verifySRPPriceInIDR();
        airportTransferSearchPageWeb.selectFirstSRPCard();

        step("Product Detail Page");
        airportTransferProductPageWeb.proceedToBookingPage();

        step("Booking Page");
        airportTransferBookingPageWeb.goToPaymentPage();

        step("Payment Page");
        String orderID = paymentPage.getOrderId();
        Assertion.assertThat("user get order id", orderID, allOf(
                not(nullValue()),
                not(equalTo(""))
        ));
    }

    @Author(names = {"Okta", "Clement"})
    @Module(name = "CompleteBooking")
    @Tribe(name = "Bus")
    @Vertical(name = "NFT")
    @Test
    public void testBookOnewayLoginProdMonitoringBusWeb() throws InterruptedException, FailedLoginException {
        Map<String, String> param = getParam("testBookOnewayLoginProdMonitoringBusWeb");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));

        String origin = param.get("origin");
        String destination = param.get("destination");
        int daysToDeparture = Integer.parseInt(param.get("daysToDeparture"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDeparture);
        int totalPassenger = 1;

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        BusSearchPageWeb<?> busSearchPage = BusSearchPageWeb.getInstance(session);
        BusSearchResultPageWeb<?> busSearchResultPage = BusSearchResultPageWeb.getInstance(session);
        BusPdpPageWeb<?> busPdpPage = BusPdpPageWeb.getInstance(session);
        BusBookingFormPageWeb<?> busBookingFormPage = BusBookingFormPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);

        step("User open home page");
        homePage.goToHomePage();

        step("Login page");
        loginPage.loginIfNotWithSameEmail(username, password, otp, retryCounter);

        step("Home page");
        homePage.openVerticalPageViaURL("bus-travel");

        step("Landing page");
        busSearchPage.clickSkipButton();
        busSearchPage.selectType("oneway");
        busSearchPage.clickOriginLocationInput();
        busSearchPage.selectLocation(origin);
        busSearchPage.clickDestinationLocationInput();
        busSearchPage.selectLocation(destination);
        busSearchPage.selectDate(departureDate);
        busSearchPage.clickPassengerInput();
        busSearchPage.setPassenger(totalPassenger);
        busSearchPage.searchBus();

        step("Search Result Page");
        busSearchResultPage.selectFirstSrpCard();

        step("Product Detail Page");
        busPdpPage.selectSeat();

        step("Booking Page");
        busBookingFormPage.fillPassengerDetail(totalPassenger);
        busBookingFormPage.clickTncCheckBox();
        busBookingFormPage.continueToPaymentPage();

        step("Payment Page");
        String orderID = paymentPage.getOrderId();
        Assertion.assertThat("user get order id", orderID, allOf(
                not(nullValue()),
                not(equalTo(""))
        ));
    }

    @Author(names = {"Vipul Kumar", "Clement"})
    @Module(name = "CompleteBooking")
    @Tribe(name = "Train")
    @Vertical(name = "NFT")
    @Test
    public void testCompleteBookingRoundTripLoginProdMonitoringTrainWeb() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testCompleteBookingRoundTripLoginProdMonitoringTrainWeb");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String originStation = param.get("originStation");
        String destinationStation = param.get("destinationStation");
        String tripType = param.get("tripType");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        // Adding randomness to departure date
        daysToDepartureDate = CoreFunctions.generateRandomInt(daysToDepartureDate, daysToDepartureDate + 10 );
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);
        int daysToReturnDate = Integer.parseInt(param.get("daysToReturnDate"));
        String returnDate = CoreFunctions.addToLongDate(departureDate, daysToReturnDate);
        int adultCount = Integer.parseInt(param.get("adultCount"));
        int infantCount = Integer.parseInt(param.get("infantCount"));
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        TrainSearchPageWeb<?> trainSearchPage = TrainSearchPageWeb.getInstance(session);
        TrainSearchResultPageWeb<?> trainSearchResultPage = TrainSearchResultPageWeb.getInstance(session);
        TrainBookingFormWeb<?> trainBookingFormWeb = TrainBookingFormWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);
        CalendarPageWeb<?> calendarPage = CalendarPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("Login page");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("Home page");
        homePage.openVerticalPageViaURL("Train");

        step("Landing page");
        trainSearchPage.clickSkipPopup();
        trainSearchPage.selectOrigin(originStation);
        trainSearchPage.selectDestination(destinationStation);
        trainSearchPage.toggleTripType(tripType);
        trainSearchPage.clickOnCalendarWidgetToOpen();
        calendarPage.selectDate(departureDate, returnDate);
        trainSearchPage.selectPassenger(adultCount,infantCount);
        trainSearchPage.savePassengers();
        trainSearchPage.searchTrain();

        step("Search Result Page");
        trainSearchPage.clickSkipPopup();
        Assertion.assertThat("Train price in SRP is in IDR", trainSearchResultPage.isSrpPriceShownIdr(), is(true));
        trainSearchResultPage.selectTrain();
        trainSearchResultPage.checkReturn();
        trainSearchResultPage.selectTrain();

        step("Booking Page");
        trainBookingFormWeb.fillPassengerDetailAdult(adultCount);
        trainBookingFormWeb.fillPassengerDetailInfant(adultCount, infantCount);
        trainBookingFormWeb.continueToBookingForm2();
        trainBookingFormWeb.continueToPay();
        trainBookingFormWeb.goToPaymentPage();

        step("Payment Page");
        String orderID = paymentPage.getOrderId();
        Assertion.assertThat("user get order id", orderID, allOf(
                not(nullValue()),
                not(equalTo(""))
        ));
    }

    @Author(names = {"Akshat", "Vipul Kumar"})
    @Module(name = "CompleteBooking")
    @Tribe(name = "Whoosh")
    @Vertical(name = "NFT")
    @Test
    public void testCompleteBookingRoundTripLoginProdMonitoringWhooshWeb() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testCompleteBookingRoundTripLoginProdMonitoringWhooshWeb");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String currency = param.get("currency");
        String originStation = param.get("originStation");
        String destinationStation = param.get("destinationStation");
        String tripType = param.get("tripType");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);
        int daysToReturnDate = Integer.parseInt(param.get("daysToReturnDate"));
        String returnDate = CoreFunctions.addToLongDate(departureDate, daysToReturnDate);
        int adultCount = Integer.parseInt(param.get("adultCount"));
        int infantCount = Integer.parseInt(param.get("infantCount"));

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        WhooshSearchPageWeb<?> whooshSearchPageWeb = WhooshSearchPageWeb.getInstance(session);
        WhooshSearchResultPageWeb<?> whooshSearchResultPageWeb = WhooshSearchResultPageWeb.getInstance(session);
        WhooshBookingPageWeb<?> whooshBookingPageWeb = WhooshBookingPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("Login page");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("Home page");
        homePage.selectCurrency(currency);
        homePage.openVerticalPageViaURL("Whoosh");

        step("Landing page");
        whooshSearchPageWeb.clickSkipPopup();
        whooshSearchPageWeb.selectOrigin(originStation);
        whooshSearchPageWeb.selectDestination(destinationStation);
        whooshSearchPageWeb.toggleTripType(tripType);
        whooshSearchPageWeb.selectDate(departureDate, returnDate);
        whooshSearchPageWeb.selectPassenger(adultCount, infantCount);
        whooshSearchPageWeb.savePassengers();
        whooshSearchPageWeb.searchWhoosh();

        step("Search Result Page");
        Assertion.assertThat("Verify if the user is on SRP or not", whooshSearchResultPageWeb.checkSRP(), is(true));
        whooshSearchResultPageWeb.clickSkip();
        whooshSearchResultPageWeb.clickFirstWhoosh();
        Assertion.assertThat("Verifies whether user can select return whoosh or not", whooshSearchResultPageWeb.checkReturn(), is(true));
        whooshSearchResultPageWeb.clickFirstWhoosh();

        step("Booking Page");
        whooshBookingPageWeb.fillPassengerDetailAdult(adultCount);
        whooshBookingPageWeb.fillPassengerDetailInfant(adultCount, infantCount);
        whooshBookingPageWeb.clickContinue();
        whooshBookingPageWeb.continueToPay();
        whooshBookingPageWeb.goToPaymentPage();

        step("Payment Page");
        String orderID = paymentPage.getOrderId();
        Assertion.assertThat("user get order id", orderID, allOf(
                not(nullValue()),
                not(equalTo(""))
        ));
    }

    @Author(names = {"Vipul Kumar", "Clement"})
    @Module(name = "CompleteBooking")
    @Tribe(name = "CarRental")
    @Vertical(name = "NFT")
    @Test
    public void testLoginCompleteBookingWithDriverProdMonitoringCarRentalWeb() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testLoginCompleteBookingWithDriverProdMonitoringCarRentalWeb");
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
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
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

        step("Login page");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("Home page");
        homePage.selectCurrency(currency);
        homePage.openVerticalPageViaURL("sewa-mobil");

        step("Landing page");
        carRentalSearchPage.clickSkipButton();
        carRentalSearchPage.selectDriverType(driverType);
        carRentalSearchPage.selectLocationByRecommendation(location);
        carRentalSearchPage.selectDate(departureDate);
        carRentalSearchPage.selectDuration(duration);
        carRentalSearchPage.searchCarRental();

        step("Search Result Page");
        carRentalSearchResultPage.selectFirstCarRentalCard();

        step("Product review Page");
        carRentalReviewBookingPage.selectPackageType(type);
        carRentalReviewBookingPage.getSelectedPackagePrice(type);
        carRentalReviewBookingPage.VerifyTravelAddOnPrice(type);
        carRentalReviewBookingPage.proceedToBookingPage();

        step("Booking Page");
        carRentalBookingPage.setPickUpLocation(addLocation);
        carRentalBookingPage.setSetDropOffLocation(addDestination);
        carRentalBookingPage.selectPayment();

        step("Payment Page");
        String orderID = paymentPage.getOrderId();
        Assertion.assertThat("user get order id", orderID, allOf(
                not(nullValue()),
                not(equalTo(""))
        ));
    }

    @Author(names = {"Okta", "Clement"})
    @Module(name = "CompleteBooking")
    @Tribe(name = "Ferry")
    @Vertical(name = "NFT")
    @Test
    public void testCompleteBookingWithPedestrianProdMonitoringFerryWeb() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testCompleteBookingWithPedestrianProdMonitoringFerryWeb");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));

        String departurePort = param.get("departurePort");
        String destinationPort = param.get("destinationPort");
        String departureDate = param.get("departureDate");
        String checkInTime = param.get("checkInTime");
        int adultPassenger = Integer.parseInt(param.get("adultPassenger"));
        int childPassenger, infantPassenger, elderPassenger;
        childPassenger = infantPassenger = elderPassenger = 0;

        String idNumberValid = param.get("idNumberValid");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        FerrySearchPageWeb<?> ferrySearchPageWeb = FerrySearchPageWeb.getInstance(session);
        FerrySearchResultPageWeb<?> ferrySearchResultPageWeb = FerrySearchResultPageWeb.getInstance(session);
        FerryBookingPageWeb<?> ferryBookingPageWeb = FerryBookingPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);

        step("User open home page");
        homePage.goToHomePage();

        step("Login page");
        loginPage.loginIfNotWithSameEmail(username, password, otp, retryCounter);

        step("Home page");
        homePage.selectCurrency("IDR");
        homePage.openVerticalPageViaURL("ferry");

        step("Landing page");
        ferrySearchPageWeb.clickSkipButton();
        ferrySearchPageWeb.clickDeparturePortInput();
        ferrySearchPageWeb.selectPort(departurePort);
        ferrySearchPageWeb.clickDestinationPortInput();
        ferrySearchPageWeb.selectPort(destinationPort);
        ferrySearchPageWeb.clickDateInput();
        ferrySearchPageWeb.selectDate(departureDate);
        ferrySearchPageWeb.clickCheckInTimeInput();
        ferrySearchPageWeb.selectCheckInTime(checkInTime);
        ferrySearchPageWeb.clickTicketTypeInput();
        ferrySearchPageWeb.selectTicketType("Pedestrian");
        ferrySearchPageWeb.clickPassengerInput();
        ferrySearchPageWeb.setPassenger("adult", adultPassenger);
        ferrySearchPageWeb.setPassenger("child", childPassenger);
        ferrySearchPageWeb.setPassenger("infant", infantPassenger);
        ferrySearchPageWeb.setPassenger("elder", elderPassenger);
        ferrySearchPageWeb.savePassenger();
        ferrySearchPageWeb.searchFerry();

        step("Search Result Page");
        ferrySearchResultPageWeb.selectFirstSrpCard();

        step("Booking Page");
        ferryBookingPageWeb.fillPassengerDetail(adultPassenger, childPassenger, infantPassenger, elderPassenger, idNumberValid, "random");
        ferryBookingPageWeb.clickTncCheckBox();
    }
}
