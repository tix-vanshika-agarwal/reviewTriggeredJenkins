package com.tiket.test.nft.train;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Environment;
import com.tiket.model.Platform;
import com.tiket.model.Session;
import com.tiket.page.bus.searchresultpage.BusSearchResultPage;
import com.tiket.page.home.HomePage;
import com.tiket.page.login.LoginPage;
import com.tiket.page.order.orderdetail.OrderDetailPage;
import com.tiket.page.order.orderlist.OrderListPage;
import com.tiket.page.payment.PaymentPage;
import com.tiket.page.train.bookingform.TrainBookingPage;
import com.tiket.page.train.searchpage.TrainSearchPage;
import com.tiket.page.train.searchresultpage.TrainSearchResultPage;
import com.tiket.pojo.common.InsuranceGeneralData;
import com.tiket.pojo.train.TrainClass;
import com.tiket.pojo.train.TrainData;
import com.tiket.pojo.train.TrainPriceDetail;
import com.tiket.pojo.whoosh.TrainAndWhooshGeneralData;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.testbase.Configs;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Map;
import static com.tiket.testdata.TestData.getObjectParams;
import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;

public class TrainTest extends BaseTest {

    @Author(names = {"Kartik"})
    @Module(name = "TrainCompleteBookingRoundtrip")
    @Tribe(name = "Train")
    @Vertical(name = "GroundTransport")
    @Test(dataProvider="testTrainCompleteBookingRoundtripLoginDataProvider")
    public void testTrainCompleteBookingRoundtripLogin(Map<String, String> param) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TrainSearchPage<WebDriver> trainSearchPage = TrainSearchPage.getInstance(session);
        TrainSearchResultPage<WebDriver> trainSearchResultPage = TrainSearchResultPage.getInstance(session);
        TrainBookingPage<WebDriver> trainBookingPage = TrainBookingPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);

        String tripType = param.get("tripType");
        String currency = param.get("currency");
        String originStation = param.get("originStation");
        String destinationStation = param.get("destinationStation");
        
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int duration = Integer.parseInt(param.get("duration"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int infantPassengers = Integer.parseInt(param.get("infant"));

        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate,daysToDepartureDate);
        String returnDate = CoreFunctions.addToLongDate(departureDate, duration);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        if(Configs.ENVIRONMENT == Environment.PROD) {
            step("Change currency to 'IDR'");
            homePage.changeCurrency("IDR",username);
        }

        step("Get App Language");
        String language = homePage.getLanguageApp();

        step("Open Train Landing Page");
        homePage.openVerticalPageDeeplink("Trains", language, currency);

        step("Skip Popup");
        trainSearchPage.skipPopup();

        step("Select origin station");
        trainSearchPage.selectOrigin(originStation);

        step("Select destination station");
        trainSearchPage.selectDestination(destinationStation);

        step("Toggle roundtrip");
        trainSearchPage.toggleTripType(tripType);

        step("Select date");
        trainSearchPage.selectDate(departureDate,returnDate);

        step("Add passenger");
        trainSearchPage.addPassenger(adultPassengers,infantPassengers);

        step("Search train");
        trainSearchPage.searchTrain();

        step("Skip Popup");
        trainSearchPage.skipPopup();

        step("Select departure train");
        trainSearchResultPage.selectTrain();

        step("Select return train");
        trainSearchResultPage.selectTrain();

        step("Fill passenger detail");
        trainBookingPage.fillPassengerDetail(adultPassengers,infantPassengers);

        step("Continue to 2nd half");
        trainBookingPage.continueTo2ndHalf();

        step("Select seat");
        trainBookingPage.selectSeat(tripType,adultPassengers);

        step("Continue to payment");
        trainBookingPage.continueToPayment();

        step("Check order ID");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",!orderID.isBlank(),is(true));
    }

    @Author(names = {"Zia","Dendi"})
    @Module(name = "TrainCompleteBookingOneway")
    @Tribe(name = "Train")
    @Vertical(name = "GroundTransport")
    @Test(dataProvider="testTrainCompleteBookingOnewayLoginDataProvider")

    public void testTrainCompleteBookingOnewayLogin(Map<String, String> param){
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TrainSearchPage<WebDriver> trainSearchPage = TrainSearchPage.getInstance(session);
        TrainSearchResultPage<WebDriver> trainSearchResultPage = TrainSearchResultPage.getInstance(session);
        TrainBookingPage<WebDriver> trainBookingPage = TrainBookingPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);

        String tripType = param.get("tripType");
        String currency = param.get("currency");
        String originStation = param.get("originStation");
        String destinationStation = param.get("destinationStation");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int infantPassengers = Integer.parseInt(param.get("infant"));

        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate,daysToDepartureDate);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        if(Configs.ENVIRONMENT == Environment.PROD) {
            step("Change currency to 'IDR'");
            homePage.changeCurrency("IDR",username);
        }

        step("Get App Language");
        String language = homePage.getLanguageApp();

        step("Open Train Landing Page");
        homePage.openVerticalPageDeeplink("Trains", language, currency);

        step("Skip Popup");
        trainSearchPage.skipPopup();

        step("Select origin station");
        trainSearchPage.selectOrigin(originStation);

        step("Select destination station");
        trainSearchPage.selectDestination(destinationStation);

        step("Toggle roundtrip");
        trainSearchPage.toggleTripType(tripType);

        step("Select date");
        trainSearchPage.selectDate(departureDate);

        step("Add passenger");
        trainSearchPage.addPassenger(adultPassengers,infantPassengers);

        step("Search train");
        trainSearchPage.searchTrain();

        step("Skip Popup");
        trainSearchPage.skipPopup();

        //for ios the next two method regarding nudges is skip and it will just click any train like the other test case
        //TODO fix step after this task unblocked : https://borobudur.atlassian.net/browse/QAAUT-13542
        if(Configs.PLATFORM == Platform.ANDROID){
            step("Open price detail train that has nudges");
            trainSearchPage.openTrainDetailWithNudges();

            step("Verify price is present in price detail tab");
            Assertion.assertThat("Verify price is present in price detail tab", trainSearchPage.verifyTrainDetailPrice(), is(true));

            step("continue to booking page");
            trainSearchPage.clickChooseInTrainDetail();

            step("Verify first page stepper");
            Assertion.assertThat("Verify first page stepper is visible", trainBookingPage.verifyFirstPageStepper(), is(true));
        }else{
            step("user select train");
            trainSearchResultPage.selectTrain();
        }

        step("Fill passenger detail");
        trainBookingPage.fillPassengerDetail(adultPassengers,infantPassengers);

        step("Continue to 2nd half");
        trainBookingPage.continueTo2ndHalf();

        if(Configs.PLATFORM == Platform.ANDROID){
            step("Verify second page stepper");
            Assertion.assertThat("Verify stepper show user in second page", trainBookingPage.verifySecondPageStepper(), is(true));
        }

        step("Select seat");
        trainBookingPage.selectSeat(tripType,adultPassengers);

        step("Continue to payment");
        trainBookingPage.continueToPayment();

        step("Check order ID");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",!orderID.isBlank(),is(true));
    }

    @Author(names = {"Budi","Dendi"})
    @Module(name = "TrainCompleteBookingOnewayInsurance")
    @Tribe(name = "Train")
    @Vertical(name = "GroundTransport")
    @Test(dataProvider="testTrainCompleteBookingOnewayInsuranceLoginDataProvider")

    public void testTrainCompleteBookingOnewayInsuranceLogin(Map<String, String> param){
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TrainSearchPage<WebDriver> trainSearchPage = TrainSearchPage.getInstance(session);
        TrainSearchResultPage<WebDriver> trainSearchResultPage = TrainSearchResultPage.getInstance(session);
        TrainBookingPage<WebDriver> trainBookingPage = TrainBookingPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);

        String tripType = param.get("tripType");
        String currency = param.get("currency");
        String originStation = param.get("originStation");
        String destinationStation = param.get("destinationStation");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int infantPassengers = Integer.parseInt(param.get("infant"));

        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate,daysToDepartureDate);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        if(Configs.ENVIRONMENT == Environment.PROD) {
            step("Change currency to 'IDR'");
            homePage.changeCurrency("IDR",username);
        }

        step("Get App Language");
        String language = homePage.getLanguageApp();

        step("Open Train Landing Page");
        homePage.openVerticalPageDeeplink("Trains", language, currency);

        step("Skip Popup");
        trainSearchPage.skipPopup();

        step("Select origin station");
        trainSearchPage.selectOrigin(originStation);

        step("Select destination station");
        trainSearchPage.selectDestination(destinationStation);

        step("Toggle roundtrip");
        trainSearchPage.toggleTripType(tripType);

        step("Select date");
        trainSearchPage.selectDate(departureDate);

        step("Add passenger");
        trainSearchPage.addPassenger(adultPassengers,infantPassengers);

        step("Search train");
        trainSearchPage.searchTrain();

        step("Skip Popup");
        trainSearchPage.skipPopup();

        step("Select departure train");
        trainSearchResultPage.selectTrain();

        step("Fill passenger detail");
        trainBookingPage.fillPassengerDetail(adultPassengers,infantPassengers);

        step("Continue to 2nd half");
        trainBookingPage.continueTo2ndHalf();

        step("Select insurance");
        trainBookingPage.selectInsurance();

        step("Open price detail");
        trainBookingPage.openPriceDetail(tripType);

        Assertion.assertThat("Is insurance show", trainBookingPage.isInsuranceShow(), is(true));

        step("close price detail");
        trainBookingPage.closePriceDetail();

        step("Continue to payment");
        trainBookingPage.continueToPayment();

        step("Check order ID");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",!orderID.isBlank(),is(true));
    }

    @Author(names = {"Budi","Dendi"})
    @Module(name = "TrainCompleteBookingOnewayInsurance")
    @Tribe(name = "Train")
    @Vertical(name = "GroundTransport")
    @Test(dataProvider="testTrainCompleteBookingOnewayInsuranceGuestDataProvider")
    public void testTrainCompleteBookingOnewayInsuranceGuest(Map<String, String> param){
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TrainSearchPage<WebDriver> trainSearchPage = TrainSearchPage.getInstance(session);
        TrainSearchResultPage<WebDriver> trainSearchResultPage = TrainSearchResultPage.getInstance(session);
        TrainBookingPage<WebDriver> trainBookingPage = TrainBookingPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);

        String tripType = param.get("tripType");
        String currency = param.get("currency");
        String originStation = param.get("originStation");
        String destinationStation = param.get("destinationStation");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int infantPassengers = Integer.parseInt(param.get("infant"));

        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate,daysToDepartureDate);

        String email = param.get("username");

        step("Wait for app to launch");
        homePage.gotoHome();

        step("User user app as guest");
        loginPage.loginAsGuest();

        if(Configs.ENVIRONMENT == Environment.PROD) {
            step("Change currency to 'IDR'");
            homePage.changeCurrency("IDR","");
        }

        step("Get App Language");
        String language = homePage.getLanguageApp();

        step("Open Train Landing Page");
        homePage.openVerticalPageDeeplink("Trains", language, currency);

        step("Skip Popup");
        trainSearchPage.skipPopup();

        step("Select origin station");
        trainSearchPage.selectOrigin(originStation);

        step("Select destination station");
        trainSearchPage.selectDestination(destinationStation);

        step("Toggle roundtrip");
        trainSearchPage.toggleTripType(tripType);

        step("Select date");
        trainSearchPage.selectDate(departureDate);

        step("Add passenger");
        trainSearchPage.addPassenger(adultPassengers,infantPassengers);

        step("Search train");
        trainSearchPage.searchTrain();

        step("Skip Popup");
        trainSearchPage.skipPopup();

        step("Select departure train");
        trainSearchResultPage.selectTrain();

        step("Fill contact data");
        trainBookingPage.fillGuestDetail(email);

        step("Fill passenger detail");
        trainBookingPage.fillPassengerDetail(adultPassengers,infantPassengers);

        step("Continue to 2nd half");
        trainBookingPage.continueTo2ndHalf();

        step("Select insurance");
        trainBookingPage.selectInsurance();

        step("Open price detail");
        trainBookingPage.openPriceDetail(tripType);

        Assertion.assertThat("Is insurance show", trainBookingPage.isInsuranceShow(), is(true));

        step("close price detail");
        trainBookingPage.closePriceDetail();

        step("Continue to payment");
        trainBookingPage.continueToPayment();

        step("Check order ID");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",!orderID.isBlank(),is(true));
    }

    @Author(names = {"Kartik"})
    @Module(name = "Train")
    @Tribe(name = "NFT")
    @Vertical(name = "GroundTransport")
    @Test(dataProvider="testTrainCompleteBookingRoundtripInsuranceLoginDataProvider")

    public void Train_testTrainCompleteBookingRoundtripInsuranceLogin(Map<String, String> param){
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        TrainSearchPage<WebDriver> trainSearchPage = TrainSearchPage.getInstance(session);
        TrainSearchResultPage<WebDriver> trainSearchResultPage = TrainSearchResultPage.getInstance(session);
        TrainBookingPage<WebDriver> trainBookingPage = TrainBookingPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);

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

        String username = param.get("username");
        String password = param.get("password");

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink (if needed) and change currency to " + currency);
        homePage.loginIfLoggedOutAndChangeCurrencyViaDeeplink(username, password, currency);

        step("Get App Language");
        String language = homePage.getLanguageApp();

        step("Open Train Landing Page");
        homePage.openVerticalPageDeeplink("Trains", language, currency);

        step("Skip Popup");
        trainSearchPage.skipPopup();

        step("Select origin station");
        trainSearchPage.selectOrigin(originStation);

        step("Select destination station");
        trainSearchPage.selectDestination(destinationStation);

        step("Toggle roundtrip");
        trainSearchPage.toggleTripType(tripType);

        step("Select date");
        trainSearchPage.selectDate(departureDate,returnDate);

        step("Add passenger");
        trainSearchPage.addPassenger(adultPassengers,infantPassengers);

        step("Search train");
        trainSearchPage.searchTrain();

        step("Skip Popup");
        trainSearchPage.skipPopup();

        step("Select departure train");
        TrainSearchResultPage.TrainAndWhooshPrices trainAndWhooshDepartPrices = trainSearchResultPage.selectTrainAndGetPricePerPax();
        trainAndWhooshGeneralData.srpFirstCardPricePerPaxDepart = trainAndWhooshDepartPrices.pricePerPax();
        trainAndWhooshGeneralData.slashedPricePerPaxDepart = trainAndWhooshDepartPrices.slashedPrice().orElse(null);

        step("Select return train");
        TrainSearchResultPage.TrainAndWhooshPrices trainAndWhooshReturnPrices = trainSearchResultPage.selectTrainAndGetPricePerPax();
        trainAndWhooshGeneralData.srpFirstCardPricePerPaxReturn = trainAndWhooshReturnPrices.pricePerPax();
        trainAndWhooshGeneralData.slashedPricePerPaxReturn = trainAndWhooshReturnPrices.slashedPrice().orElse(null);

        step("Fill passenger detail");
        trainBookingPage.fillPassengerDetail(adultPassengers,infantPassengers);

        step("Continue to 2nd half");
        trainBookingPage.continueTo2ndHalf();

        step("Select insurance");
        trainAndWhooshGeneralData.insuranceGeneralData = trainBookingPage.selectInsurance();

        step("Open price detail");
        trainBookingPage.openPriceDetail(tripType);

        step("verify selected insurance title { " + trainAndWhooshGeneralData.insuranceGeneralData.insuranceTitle + " } with price: " + trainAndWhooshGeneralData.insuranceGeneralData.insurancePrice + " and adult price and infant price");
        Assertion.assertThat("is insurance selected displayed in price detail", trainBookingPage.verifyGeneralDataPriceDetail(trainAndWhooshGeneralData), is(true));

        step("verify price calculation");
        Assertion.assertThat("is calculation of prices are correct and match with total price",trainBookingPage.isPriceSumCorrect(),is(true));

        step("Continue to payment");
        trainBookingPage.continueToPayment();

        step("Check order ID");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",!orderID.isBlank(),is(true));
    }

    @Author(names = {"Kartik"})
    @Module(name = "TrainCompleteBookingRoundtripInsurance")
    @Tribe(name = "Train")
    @Vertical(name = "GroundTransport")
    @Test(dataProvider="testTrainCompleteBookingRoundtripInsuranceGuestDataProvider")

    public void testTrainCompleteBookingRoundtripInsuranceGuest(Map<String, String> param){
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TrainSearchPage<WebDriver> trainSearchPage = TrainSearchPage.getInstance(session);
        TrainSearchResultPage<WebDriver> trainSearchResultPage = TrainSearchResultPage.getInstance(session);
        TrainBookingPage<WebDriver> trainBookingPage = TrainBookingPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);

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

        String email = param.get("username");

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Logout via deeplink");
        homePage.logoutViaDeeplink();

        step("Change currency to 'IDR'");
        homePage.changeCurrency("IDR","");

        step("Get App Language");
        String language = homePage.getLanguageApp();

        step("Open Train Landing Page");
        homePage.openVerticalPageDeeplink("Trains", language, currency);

        step("Skip Popup");
        trainSearchPage.skipPopup();

        step("Select origin station");
        trainSearchPage.selectOrigin(originStation);

        step("Select destination station");
        trainSearchPage.selectDestination(destinationStation);

        step("Toggle roundtrip");
        trainSearchPage.toggleTripType(tripType);

        step("Select date");
        trainSearchPage.selectDate(departureDate,returnDate);

        step("Add passenger");
        trainSearchPage.addPassenger(adultPassengers,infantPassengers);

        step("Search train");
        trainSearchPage.searchTrain();

        step("Skip Popup");
        trainSearchPage.skipPopup();

        step("Select departure train");
        TrainSearchResultPage.TrainAndWhooshPrices trainAndWhooshDepartPrices = trainSearchResultPage.selectTrainAndGetPricePerPax();
        trainAndWhooshGeneralData.srpFirstCardPricePerPaxDepart = trainAndWhooshDepartPrices.pricePerPax();
        trainAndWhooshGeneralData.slashedPricePerPaxDepart = trainAndWhooshDepartPrices.slashedPrice().orElse(null);
        step("Select return train");
        TrainSearchResultPage.TrainAndWhooshPrices trainAndWhooshReturnPrices = trainSearchResultPage.selectTrainAndGetPricePerPax();
        trainAndWhooshGeneralData.srpFirstCardPricePerPaxReturn = trainAndWhooshReturnPrices.pricePerPax();
        trainAndWhooshGeneralData.slashedPricePerPaxReturn = trainAndWhooshReturnPrices.slashedPrice().orElse(null);

        step("Fill contact data");
        trainBookingPage.fillGuestDetail(email);

        step("Fill passenger detail");
        trainBookingPage.fillPassengerDetail(adultPassengers,infantPassengers);

        step("Continue to 2nd half");
        trainBookingPage.continueTo2ndHalf();

        step("Select insurance");
        trainAndWhooshGeneralData.insuranceGeneralData = trainBookingPage.selectInsurance();

        step("Open price detail");
        trainBookingPage.openPriceDetail(tripType);

        step("verify selected insurance title { " + trainAndWhooshGeneralData.insuranceGeneralData.insuranceTitle + " } with price: " + trainAndWhooshGeneralData.insuranceGeneralData.insurancePrice + " and adult price and infant price");
        Assertion.assertThat("is insurance selected displayed in price detail", trainBookingPage.verifyGeneralDataPriceDetail(trainAndWhooshGeneralData), is(true));

        step("verify price calculation");
        Assertion.assertThat("is calculation of prices are correct and match with total price",trainBookingPage.isPriceSumCorrect(),is(true));

        step("close price detail");
        trainBookingPage.closePriceDetail();

        step("Continue to payment");
        trainBookingPage.continueToPayment();

        step("Check order ID");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",!orderID.isBlank(),is(true));
    }

    @Author(names = {"Zia","Budi","Ashish","Dendi"})
    @Module(name = "TrainCompleteBookingRoundtrip")
    @Tribe(name = "Train")
    @Vertical(name = "GroundTransport")
    @Test(dataProvider="testTrainCompleteBookingRoundtripGuestDataProvider")

    public void testTrainCompleteBookingRoundtripGuest(Map<String, String> param){
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        TrainSearchPage<WebDriver> trainSearchPage = TrainSearchPage.getInstance(session);
        TrainSearchResultPage<WebDriver> trainSearchResultPage = TrainSearchResultPage.getInstance(session);
        TrainBookingPage<WebDriver> trainBookingPage = TrainBookingPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);


        String tripType = param.get("tripType");
        String currency = param.get("currency");
        String originStation = param.get("originStation");
        String destinationStation = param.get("destinationStation");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int duration = Integer.parseInt(param.get("duration"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int infantPassengers = Integer.parseInt(param.get("infant"));

        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate,daysToDepartureDate);
        String returnDate = CoreFunctions.addToLongDate(departureDate, duration);

        String email = param.get("username");

        step("Wait for app to launch");
        homePage.gotoHome();

        step("User user app as guest");
        loginPage.loginAsGuest();

        if(Configs.ENVIRONMENT == Environment.PROD) {
            step("Change currency to 'IDR'");
            homePage.changeCurrency("IDR","");
        }

        step("Get App Language");
        String language = homePage.getLanguageApp();

        step("Open Train Landing Page");
        homePage.openVerticalPageDeeplink("Trains", language, currency);

        step("Skip Popup");
        trainSearchPage.skipPopup();

        step("Select origin station");
        trainSearchPage.selectOrigin(originStation);

        step("Select destination station");
        trainSearchPage.selectDestination(destinationStation);

        step("Toggle roundtrip");
        trainSearchPage.toggleTripType(tripType);

        step("Select date");
        trainSearchPage.selectDate(departureDate,returnDate);

        step("Add passenger");
        trainSearchPage.addPassenger(adultPassengers,infantPassengers);

        step("Search train");
        trainSearchPage.searchTrain();

        step("Skip Popup");
        trainSearchPage.skipPopup();

        step("Select departure train");
        trainSearchResultPage.selectTrain();

        step("Select return train");
        trainSearchResultPage.selectTrain();

        step("Fill contact data");
        trainBookingPage.fillGuestDetail(email);

        step("Fill passenger detail");
        trainBookingPage.fillPassengerDetail(adultPassengers,infantPassengers);

        step("Continue to 2nd half");
        trainBookingPage.continueTo2ndHalf();

        step("Select seat");
        trainBookingPage.selectSeat(tripType,adultPassengers);

        step("Continue to payment");
        trainBookingPage.continueToPayment();

        step("Check order ID");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",!orderID.isBlank(),is(true));
    }

    @Author(names = {"kartik"})
    @Module(name = "TrainCompleteBookingRoundtrip")
    @Tribe(name = "Train")
    @Vertical(name = "GroundTransport")
    @Test(dataProvider="testTrainCompleteBookingOnewayGuestDataProvider")

    public void testTrainCompleteBookingOnewayGuest(Map<String, String> param){
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        TrainSearchPage<WebDriver> trainSearchPage = TrainSearchPage.getInstance(session);
        TrainSearchResultPage<WebDriver> trainSearchResultPage = TrainSearchResultPage.getInstance(session);
        TrainBookingPage<WebDriver> trainBookingPage = TrainBookingPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);

        String tripType = param.get("tripType");
        String currency = param.get("currency");
        String originStation = param.get("originStation");
        String destinationStation = param.get("destinationStation");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int infantPassengers = Integer.parseInt(param.get("infant"));

        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate,daysToDepartureDate);

        String email = param.get("username");

        step("Wait for app to launch");
        homePage.gotoHome();

        step("User user app as guest");
        loginPage.loginAsGuest();

        if(Configs.ENVIRONMENT == Environment.PROD) {
            step("Change currency to 'IDR'");
            homePage.changeCurrency("IDR","");
        }

        step("Get App Language");
        String language = homePage.getLanguageApp();

        step("Open Train Landing Page");
        homePage.openVerticalPageDeeplink("Trains", language, currency);

        step("Select origin station");
        trainSearchPage.skipPopup();
        trainSearchPage.selectOrigin(originStation);

        step("Select destination station");
        trainSearchPage.selectDestination(destinationStation);

        step("Toggle one way");
        trainSearchPage.toggleTripType(tripType);

        step("Select date");
        trainSearchPage.selectDate(departureDate);

        step("Add passenger");
        trainSearchPage.addPassenger(adultPassengers,infantPassengers);

        step("Search train");
        trainSearchPage.searchTrain();

        step("Skip Popup");
        trainSearchPage.skipPopup();

        //for ios the next two method regarding nudges is skip and it will just click any train like the other test case
        //TODO fix step after this task unblocked : https://borobudur.atlassian.net/browse/QAAUT-13542
        if(Configs.PLATFORM == Platform.ANDROID){
            step("Open price detail train that has nudges");
            trainSearchPage.openTrainDetailWithNudges();
    
            step("Verify price is present in price detail tab");
            Assertion.assertThat("Verify price is present in price detail tab", trainSearchPage.verifyTrainDetailPrice(), is(true));

            step("continue to booking page");
            trainSearchPage.clickChooseInTrainDetail();

            step("Verify first page stepper");
            Assertion.assertThat("Verify first page stepper is visible", trainBookingPage.verifyFirstPageStepper(), is(true));
        }else{
            step("user select train");
            trainSearchResultPage.selectTrain();
        }
        
        step("Fill contact data");
        trainBookingPage.fillGuestDetail(email);

        step("Fill passenger detail");
        trainBookingPage.fillPassengerDetail(adultPassengers,infantPassengers);

        step("Continue to 2nd half");
        trainBookingPage.continueTo2ndHalf();

        if(Configs.PLATFORM == Platform.ANDROID){
            step("Verify second page stepper");
            Assertion.assertThat("Verify second page stepper is visible", trainBookingPage.verifySecondPageStepper(), is(true));
        }

        step("Select seat");
        trainBookingPage.selectSeat(tripType,adultPassengers);

        step("Continue to payment");
        trainBookingPage.continueToPayment();

        step("Check order ID");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",!orderID.isBlank(),is(true));
    }

    @Author(names = {"Kartik"})
    @Module(name = "OrderAndPaymentDetails")
    @Tribe(name = "Train")
    @Vertical(name = "GroundTransport")
    @Test(dataProvider="testTrainCheckOrderDetailInMyOrderMenuLoginDataProvider")
    public void testTrainCheckOrderDetailInMyOrderMenuLogin(Map<String, String> param){
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TrainSearchPage<WebDriver> trainSearchPage = TrainSearchPage.getInstance(session);
        TrainSearchResultPage<WebDriver> trainSearchResultPage = TrainSearchResultPage.getInstance(session);
        TrainBookingPage<WebDriver> trainBookingPage = TrainBookingPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String tripType = param.get("tripType");
        String currency = param.get("currency");
        String originStation = param.get("originStation");
        String destinationStation = param.get("destinationStation");
        String dateRange = param.get("dateRange");

        int adultPassengers = Integer.parseInt(param.get("adult"));
        int infantPassengers = Integer.parseInt(param.get("infant"));

        String departureDate = CoreFunctions.addToLongRandomDate(dateRange);

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink (if needed) and change currency to " + currency);
        homePage.loginIfLoggedOutAndChangeCurrencyViaDeeplink(username, password, currency);

        step("Get App Language");
        String language = homePage.getLanguageApp();

        step("Open Train Landing Page");
        homePage.openVerticalPageDeeplink("Trains", language, currency);

        step("Skip Popup");
        trainSearchPage.skipPopup();

        step("Select origin station");
        trainSearchPage.selectOrigin(originStation);

        step("Select destination station");
        trainSearchPage.selectDestination(destinationStation);

        step("Toggle roundtrip");
        trainSearchPage.toggleTripType(tripType);

        step("Select date");
        trainSearchPage.selectDate(departureDate);

        step("Add passenger");
        trainSearchPage.addPassenger(adultPassengers,infantPassengers);

        step("Search train");
        trainSearchPage.searchTrain();

        step("Skip Popup");
        trainSearchPage.skipPopup();

        step("Select departure train");
        trainSearchResultPage.selectTrain();

        step("Fill passenger detail");
        trainBookingPage.fillPassengerDetail(adultPassengers,infantPassengers);

        step("Continue to 2nd half");
        trainBookingPage.continueTo2ndHalf();

        step("Continue to payment");
        trainBookingPage.continueToPayment();

        step("Check order ID");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",!orderID.isBlank(),is(true));

        step("Open payment method");
        paymentPage.openPaymentMethod();

        step("Select virtual account as payment method");
        paymentPage.selectPaymentMethod("BCA Virtual Account");

        step("get guest and train data from payment page");
        TrainData data = paymentPage.getTrainGuestInfoPaymentPage();
        data.orderId = orderID;

        step("get total price");
        String totalPrice = paymentPage.getTotalPriceFirstPage();

        step("Submit payment");
        paymentPage.clickPayButton();

        step("Go to the order list");
        paymentPage.seeOrderList();

        step("Open order");
        orderListPage.openOrderDetailWaitingForPayment(orderID);

        step("check train and passenger detail correct");
        Assertion.assertThat("Verify train detail", orderDetailPage.checkTrainDetails(data), is(true));
        orderDetailPage.checkPassengerDetailsTrain(data);

        step("verify total payment correct");
        Assertion.assertThat("is total payment from payment is equals to order menu",orderDetailPage.verifyTotalPaymentTrain(totalPrice),is(true));
    }

    @Author(names = {"Zia","Dendi"})
    @Module(name = "SearchFilter")
    @Tribe(name = "Train")
    @Vertical(name = "GroundTransport")
    @Test(dataProvider="testTrainFilterByClassGuestDataProvider")
    public void testTrainFilterByClassGuest(Map<String, String> param) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);
        TrainSearchPage<WebDriver> trainSearchPage = TrainSearchPage.getInstance(session);
        TrainSearchResultPage<WebDriver> trainSearchResultPage = TrainSearchResultPage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);

        String tripType = param.get("tripType");
        String currency = param.get("currency");
        String originStation = param.get("originStation");
        String destinationStation = param.get("destinationStation");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate,daysToDepartureDate);
        String trainClassName = param.get("trainClass");
        TrainClass trainClass = new TrainClass(trainClassName);

        step("Wait for app to launch");
        homePage.gotoHome();

        step("User user app as guest");
        loginPage.loginAsGuest();

        if(Configs.ENVIRONMENT == Environment.PROD) {
            step("Change currency to 'IDR'");
            homePage.changeCurrency("IDR","");
        }

        step("Get App Language");
        String language = homePage.getLanguageApp();

        step("Open Train Landing Page");
        homePage.openVerticalPageDeeplink("Trains", language, currency);

        step("Skip Popup");
        trainSearchPage.skipPopup();


        step("Select origin station");
        trainSearchPage.selectOrigin(originStation);

        step("Select destination station");
        trainSearchPage.selectDestination(destinationStation);

        step("Toggle one way");
        trainSearchPage.toggleTripType(tripType);

        step("Select date");
        trainSearchPage.selectDate(departureDate);

        step("Search train");
        trainSearchPage.searchTrain();

        step("Skip Popup");
        trainSearchPage.skipPopup();

        step("Open search filter");
        trainSearchPage.openFilter();

        step("Filter by class name");
        trainSearchPage.filterByClass(trainClass);

        step("Save filter");
        trainSearchPage.saveFilter();

        step("Assert filter by class name correct");
        Assertion.assertThat("verify class name after filter",trainSearchPage.verifyClass(trainClass),is(true));
    }

    @Author(names = {"Zia","Dendi"})
    @Module(name = "SearchFilter")
    @Tribe(name = "Train")
    @Vertical(name = "GroundTransport")
    @Test(dataProvider="testTrainFilterByClassLoginDataProvider")
    public void testTrainFilterByClassLogin(Map<String, String> param) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);
        TrainSearchPage<WebDriver> trainSearchPage = TrainSearchPage.getInstance(session);
        TrainSearchResultPage<WebDriver> trainSearchResultPage = TrainSearchResultPage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);

        String tripType = param.get("tripType");
        String currency = param.get("currency");
        String originStation = param.get("originStation");
        String destinationStation = param.get("destinationStation");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate,daysToDepartureDate);
        String trainClassName = param.get("trainClass");
        TrainClass trainClass = new TrainClass(trainClassName);
        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");

        step("Wait for app to launch");
        homePage.gotoHome();

        step("User login");
        loginPage.loginWith(loginType,username,password);

        step("Get App Language");
        String language = homePage.getLanguageApp();

        step("Open Train Landing Page");
        homePage.openVerticalPageDeeplink("Trains", language, currency);

        step("Skip Popup");
        trainSearchPage.skipPopup();


        step("Select origin station");
        trainSearchPage.selectOrigin(originStation);

        step("Select destination station");
        trainSearchPage.selectDestination(destinationStation);

        step("Toggle one way");
        trainSearchPage.toggleTripType(tripType);

        step("Select date");
        trainSearchPage.selectDate(departureDate);

        step("Search train");
        trainSearchPage.searchTrain();

        step("Skip Popup");
        trainSearchPage.skipPopup();

        step("Open search filter");
        trainSearchPage.openFilter();

        step("Filter by class name");
        trainSearchPage.filterByClass(trainClass);

        step("Save filter");
        trainSearchPage.saveFilter();

        step("Assert filter by class name correct");
        Assertion.assertThat("verify class name after filter",trainSearchPage.verifyClass(trainClass),is(true));
    }

    @Author(names = {"Zia","Dendi"})
    @Module(name = "RecentlySearched")
    @Tribe(name = "Train")
    @Vertical(name = "GroundTransport")
    @Test(dataProvider="testTrainCheckRecentlySearchedGuestDataProvider")
    public void testTrainCheckRecentlySearchedGuest(Map<String, String> param) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        TrainSearchPage<WebDriver> trainSearchPage = TrainSearchPage.getInstance(session);
        TrainSearchResultPage<WebDriver> trainSearchResultPage = TrainSearchResultPage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);

        String tripType = param.get("tripType");
        String currency = param.get("currency");
        String originStation = param.get("originStation");
        String destinationStation = param.get("destinationStation");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));

        TrainAndWhooshGeneralData trainAndWhooshGeneralData = new TrainAndWhooshGeneralData(originStation, destinationStation,1,1,0,tripType,daysToDepartureDate);
        String departureDate = trainAndWhooshGeneralData.departureDateLong;

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Logout via deeplink");
        homePage.logoutViaDeeplink();

        step("Change currency to 'IDR'");
        homePage.changeCurrency("IDR","");

        step("Open Train Landing Page");
        homePage.openVerticalPageDeeplink("Trains", "", currency);

        step("Skip Popup");
        trainSearchPage.skipPopup();

        step("Clear recently searched if present");
        trainSearchPage.clearRecentlySearch();

        step("Select origin station");
        trainSearchPage.selectOrigin(originStation);

        step("Select destination station");
        trainSearchPage.selectDestination(destinationStation);

        step("Toggle roundtrip");
        trainSearchPage.toggleTripType(tripType);

        step("Select date");
        trainSearchPage.selectDate(departureDate);

        step("Get train date");
        String trainDate = trainSearchPage.getSelectedDate(trainAndWhooshGeneralData);
        Assertion.assertThat("Selected date is correct", trainDate, anyOf(containsString(trainAndWhooshGeneralData.whooshDateFormatted_dd_MMM_En), containsString(trainAndWhooshGeneralData.whooshDateFormatted_dd_MMM_Id)));

        step("Search train");
        trainSearchPage.searchTrain();

        step("Skip Popup");
        trainSearchPage.skipPopup();

        step("Verify search result page");
        Assertion.assertThat("user is in search result",trainSearchResultPage.verifySearch(trainAndWhooshGeneralData),is(true));

        step("Back to search page");
        trainSearchResultPage.backToSearchPage();

        step("Verify recently search card is present");
        Assertion.assertThat("is recently search present",trainSearchPage.verifyRecentlySearch(trainAndWhooshGeneralData),is(true));

        step("Click recently searched to go to srp");
        trainSearchPage.clickRecentlySearch(trainAndWhooshGeneralData.whooshDateFormatted_dd_MMM_yy_En, trainAndWhooshGeneralData.whooshDateFormatted_dd_MMM_yy_Id);

        step("Verify correct search result displayed");
        Assertion.assertThat("user is in search result",trainSearchResultPage.verifySearch(trainAndWhooshGeneralData),is(true));
    }

    @Author(names = {"Zia","Dendi"})
    @Module(name = "Train")
    @Tribe(name = "NFT")
    @Vertical(name = "GroundTransport")
    @Test(dataProvider="testTrainCheckRecentlySearchedLoginDataProvider")
    public void Train_testTrainCheckRecentlySearchedLogin(Map<String, String> param) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        TrainSearchPage<WebDriver> trainSearchPage = TrainSearchPage.getInstance(session);
        TrainSearchResultPage<WebDriver> trainSearchResultPage = TrainSearchResultPage.getInstance(session);

        String tripType = param.get("tripType");
        String currency = param.get("currency");
        String originStation = param.get("originStation");
        String destinationStation = param.get("destinationStation");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));

        String username = param.get("username");
        String password = param.get("password");

        int adultPassengers = 1, infantPassengers = 0;

        TrainAndWhooshGeneralData trainAndWhooshGeneralData = new TrainAndWhooshGeneralData(originStation, destinationStation,adultPassengers + infantPassengers,adultPassengers,infantPassengers,tripType,daysToDepartureDate);
        String departureDate = trainAndWhooshGeneralData.departureDateLong;

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink (if needed) and change currency to " + currency);
        homePage.loginIfLoggedOutAndChangeCurrencyViaDeeplink(username, password, currency);

        step("Open Train Landing Page");
        homePage.openVerticalPageDeeplink("Trains", "", currency);

        step("Skip Popup");
        trainSearchPage.skipPopup();

        step("Clear recently searched if present");
        trainSearchPage.clearRecentlySearch();

        step("Select origin station");
        trainSearchPage.selectOrigin(originStation);

        step("Select destination station");
        trainSearchPage.selectDestination(destinationStation);

        step("Toggle roundtrip");
        trainSearchPage.toggleTripType(tripType);

        step("set passenger");
        trainSearchPage.addPassenger(adultPassengers, infantPassengers);

        step("Select date");
        trainSearchPage.selectDate(departureDate);

        step("Get train date");
        String trainDate = trainSearchPage.getSelectedDate(trainAndWhooshGeneralData);
        Assertion.assertThat("Selected date is correct", trainDate, anyOf(containsString(trainAndWhooshGeneralData.whooshDateFormatted_dd_MMM_En), containsString(trainAndWhooshGeneralData.whooshDateFormatted_dd_MMM_Id)));

        step("Search train");
        trainSearchPage.searchTrain();

        step("Skip Popup");
        trainSearchPage.skipPopup();

        step("Verify search result page");
        Assertion.assertThat("user is in search result",trainSearchResultPage.verifySearch(trainAndWhooshGeneralData),is(true));

        step("Back to search page");
        trainSearchResultPage.backToSearchPage();

        step("Verify recently search card is present");
        Assertion.assertThat("is recently search present",trainSearchPage.verifyRecentlySearch(trainAndWhooshGeneralData),is(true));

        step("Click recently searched to go to srp");
        trainSearchPage.clickRecentlySearch(trainAndWhooshGeneralData.whooshDateFormatted_dd_MMM_yy_En, trainAndWhooshGeneralData.whooshDateFormatted_dd_MMM_yy_Id);

        step("Verify correct search result displayed");
        Assertion.assertThat("user is in search result",trainSearchResultPage.verifySearch(trainAndWhooshGeneralData),is(true));
    }

    @Author(names = {"Ashish","Dendi"})
    @Module(name = "CheckPriceDetailInBookingPage")
    @Tribe(name = "Train")
    @Vertical(name = "GroundTransport")
    @Test(dataProvider="testTrainCheckPriceDetailInBookingPageLoginDataProvider")
    public void testTrainCheckPriceDetailInBookingPageLogin(Map<String, String> param){
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TrainSearchPage<WebDriver> trainSearchPage = TrainSearchPage.getInstance(session);
        TrainSearchResultPage<WebDriver> trainSearchResultPage = TrainSearchResultPage.getInstance(session);
        TrainBookingPage<WebDriver> trainBookingPage = TrainBookingPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);

        String tripType = param.get("tripType");
        String currency = param.get("currency");
        String originStation = param.get("originStation");
        String destinationStation = param.get("destinationStation");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int infantPassengers = Integer.parseInt(param.get("infant"));

        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate,daysToDepartureDate);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink (if needed) and change currency to " + currency);
        homePage.loginIfLoggedOutAndChangeCurrencyViaDeeplink(username, password, currency);

        step("Open Train Landing Page");
        homePage.openVerticalPageDeeplink("Trains", "", currency);

        step("Skip Popup");
        trainSearchPage.skipPopup();

        step("Select origin station");
        trainSearchPage.selectOrigin(originStation);

        step("Select destination station");
        trainSearchPage.selectDestination(destinationStation);

        step("Toggle roundtrip");
        trainSearchPage.toggleTripType(tripType);

        step("Select date");
        trainSearchPage.selectDate(departureDate);

        step("Add passenger");
        trainSearchPage.addPassenger(adultPassengers,infantPassengers);

        step("Search train");
        trainSearchPage.searchTrain();

        step("Skip Popup");
        trainSearchPage.skipPopup();

        step("Select departure train");
        trainSearchResultPage.selectTrain();

        step("Fill passenger detail");
        trainBookingPage.fillPassengerDetail(adultPassengers,infantPassengers);

        step("Continue to 2nd half");
        trainBookingPage.continueTo2ndHalf();

        step("Open price detail");
        trainBookingPage.openPriceDetail(tripType);

        step("Verify price detail");
        TrainPriceDetail priceDetail = new TrainPriceDetail();
        priceDetail.tripType = tripType;
        priceDetail.adultNumber = adultPassengers;
        priceDetail.infantNumber = infantPassengers;

        Assertion.assertThat("Is price breakdown correct", trainBookingPage.isPriceBreakdownCorrect(priceDetail), is(true));

        step("close price detail");
        trainBookingPage.closePriceDetail();

        step("Continue to payment");
        trainBookingPage.continueToPayment();

        step("Check order ID");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",!orderID.isBlank(),is(true));
    }

    @Author(names = {"Zia","Dendi"})
    @Module(name = "SearchFilter")
    @Tribe(name = "Train")
    @Vertical(name = "GroundTransport")
    @Test(dataProvider="testTrainFilterByNameGuestDataProvider")
    public void testTrainFilterByNameGuest(Map<String, String> param) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);
        TrainSearchPage<WebDriver> trainSearchPage = TrainSearchPage.getInstance(session);
        TrainSearchResultPage<WebDriver> trainSearchResultPage = TrainSearchResultPage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);

        String tripType = param.get("tripType");
        String currency = param.get("currency");
        String originStation = param.get("originStation");
        String destinationStation = param.get("destinationStation");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate,daysToDepartureDate);
        String trainName = param.get("trainName");

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Logout via deeplink");
        homePage.logoutViaDeeplink();

        if(Configs.ENVIRONMENT == Environment.PROD) {
            step("Change currency to 'IDR'");
            homePage.changeCurrency("IDR","");
        }

        step("Get App Language");
        String language = homePage.getLanguageApp();

        step("Open Train Landing Page");
        homePage.openVerticalPageDeeplink("Trains", language, currency);

        step("Select origin station");
        trainSearchPage.skipPopup();
        trainSearchPage.selectOrigin(originStation);

        step("Select destination station");
        trainSearchPage.selectDestination(destinationStation);

        step("Toggle one way");
        trainSearchPage.toggleTripType(tripType);

        step("Select date");
        trainSearchPage.selectDate(departureDate);

        step("Search train");
        trainSearchPage.searchTrain();

        step("Skip Popup");
        trainSearchPage.skipPopup();

        step("Open search filter");
        trainSearchPage.openFilter();

        step("Filter by train name");
        trainSearchPage.filterByName(trainName);

        step("Save filter");
        trainSearchPage.saveFilter();

        step("Assert filter by train name correct");
        Assertion.assertThat("verify train name after filter",trainSearchPage.verifyName(trainName),is(true));
    }

    @Author(names = {"Zia","Dendi"})
    @Module(name = "SearchFilter")
    @Tribe(name = "Train")
    @Vertical(name = "GroundTransport")
    @Test(dataProvider="testTrainFilterByNameLoginDataProvider")
    public void testTrainFilterByNameLogin(Map<String, String> param) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);
        TrainSearchPage<WebDriver> trainSearchPage = TrainSearchPage.getInstance(session);
        TrainSearchResultPage<WebDriver> trainSearchResultPage = TrainSearchResultPage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);

        String tripType = param.get("tripType");
        String currency = param.get("currency");
        String originStation = param.get("originStation");
        String destinationStation = param.get("destinationStation");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate,daysToDepartureDate);
        String trainName = param.get("trainName");

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink (if needed) and change currency to " + currency);
        homePage.loginIfLoggedOutAndChangeCurrencyViaDeeplink(username, password, currency);

        step("Get App Language");
        String language = homePage.getLanguageApp();

        step("Open Train Landing Page");
        homePage.openVerticalPageDeeplink("Trains", language, currency);
        step("Skip Popup");
        trainSearchPage.skipPopup();

        step("Select origin station");
        trainSearchPage.selectOrigin(originStation);

        step("Select destination station");
        trainSearchPage.selectDestination(destinationStation);

        step("Toggle one way");
        trainSearchPage.toggleTripType(tripType);

        step("Select date");
        trainSearchPage.selectDate(departureDate);

        step("Search train");
        trainSearchPage.searchTrain();

        step("Skip Popup");
        trainSearchPage.skipPopup();

        step("Open search filter");
        trainSearchPage.openFilter();

        step("Filter by train name");
        trainSearchPage.filterByName(trainName);

        step("Save filter");
        trainSearchPage.saveFilter();

        step("Assert filter by train name correct");
        Assertion.assertThat("verify class name after filter",trainSearchPage.verifyName(trainName),is(true));
    }

    @Author(names = {"Kartik"})
    @Module(name = "OrderAndPaymentDetails")
    @Tribe(name = "Train")
    @Vertical(name = "GroundTransport")
    @Test(dataProvider="testTrainCheckDetailPaymentPageLoginDataProvider")
    public void testTrainCheckDetailPaymentPageLogin(Map<String, String> param){
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TrainSearchPage<WebDriver> trainSearchPage = TrainSearchPage.getInstance(session);
        TrainSearchResultPage<WebDriver> trainSearchResultPage = TrainSearchResultPage.getInstance(session);
        TrainBookingPage<WebDriver> trainBookingPage = TrainBookingPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String tripType = param.get("tripType");
        String currency = param.get("currency");
        String originStation = param.get("originStation");
        String destinationStation = param.get("destinationStation");
        String dateRange = param.get("dateRange");

        int adultPassengers = Integer.parseInt(param.get("adult"));
        int infantPassengers = Integer.parseInt(param.get("infant"));

        TrainAndWhooshGeneralData trainAndWhooshGeneralData = new TrainAndWhooshGeneralData(originStation, destinationStation,adultPassengers,adultPassengers,infantPassengers,tripType,dateRange);
        String departureDate = trainAndWhooshGeneralData.departureDateLong;

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink (if needed) and change currency to " + currency);
        homePage.loginIfLoggedOutAndChangeCurrencyViaDeeplink(username, password, currency);

        step("Get App Language");
        String language = homePage.getLanguageApp();

        step("Open Train Landing Page");
        homePage.openVerticalPageDeeplink("Trains", language, currency);

        step("Skip Popup");
        trainSearchPage.skipPopup();

        step("Select origin station");
        trainAndWhooshGeneralData.originCityName = trainSearchPage.selectOriginAndGetCityName(originStation);

        step("Select destination station");
        trainAndWhooshGeneralData.destinationCityName = trainSearchPage.selectDestinationAndGetCityName(destinationStation);

        step("Toggle roundtrip");
        trainSearchPage.toggleTripType(tripType);

        step("Select date");
        trainSearchPage.selectDate(departureDate);

        step("Add passenger");
        trainSearchPage.addPassenger(adultPassengers,infantPassengers);

        step("Search train");
        trainSearchPage.searchTrain();

        step("Skip Popup");
        trainSearchPage.skipPopup();

        step("Select departure train");
        trainSearchResultPage.selectTrain();

        step("Fill passenger detail");
        trainBookingPage.fillPassengerDetail(adultPassengers,infantPassengers);

        step("Continue to 2nd half");
        trainBookingPage.continueTo2ndHalf();

        step("get details from the booking page");
        trainAndWhooshGeneralData.trainDataBookingPage = trainBookingPage.getDetailsTrainAndPassenger(language);

        step("get total payment");
        trainAndWhooshGeneralData.totalPaymentBookingPage = trainBookingPage.getTotalPayment();

        step("Open price detail");
        trainBookingPage.openPriceDetail(tripType);

        step("Verify price detail");
        TrainPriceDetail priceDetail = new TrainPriceDetail();
        priceDetail.tripType = tripType;
        priceDetail.adultNumber = adultPassengers;
        priceDetail.infantNumber = infantPassengers;

        Assertion.assertThat("Is price breakdown correct", trainBookingPage.isPriceBreakdownCorrect(priceDetail), is(true));

        step("close price detail");
        trainBookingPage.closePriceDetail();

        step("Continue to payment");
        trainBookingPage.continueToPayment();

        step("Check order ID");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",!orderID.isBlank(),is(true));

        step("get guest and train data from payment page");
        trainAndWhooshGeneralData.trainDataPaymentPage = paymentPage.getTrainGuestInfoPaymentPage();

        step("open price detail page");
        paymentPage.openPriceDetail();

        step("get depart and arrival station price detail");
        trainAndWhooshGeneralData.departAndArrivalStationPriceDetail = paymentPage.getDepartAndArrivalStationPriceDetail();

        step("get total price");
        trainAndWhooshGeneralData.totalPaymentPaymentPage = paymentPage.getPriceWithoutOtherFees();

        step("price breakdown");
        Assertion.assertThat("is price breakdown correct: ", paymentPage.priceBreakDown(), is(true));

        step("verify details");
        paymentPage.verifyDetailsTrainAndPassenger(trainAndWhooshGeneralData);
    }

    @Author(names = {"Ashish","Dendi"})
    @Module(name = "CheckPriceDetailInBookingPage")
    @Tribe(name = "Train")
    @Vertical(name = "GroundTransport")
    @Test(dataProvider="testTrainCheckPriceDetailInBookingPageGuestDataProvider")
    public void testTrainCheckPriceDetailInBookingPageGuest(Map<String, String> param){
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TrainSearchPage<WebDriver> trainSearchPage = TrainSearchPage.getInstance(session);
        TrainSearchResultPage<WebDriver> trainSearchResultPage = TrainSearchResultPage.getInstance(session);
        TrainBookingPage<WebDriver> trainBookingPage = TrainBookingPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);

        String tripType = param.get("tripType");
        String currency = param.get("currency");
        String originStation = param.get("originStation");
        String destinationStation = param.get("destinationStation");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int infantPassengers = Integer.parseInt(param.get("infant"));

        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate,daysToDepartureDate);
        String email = param.get("email");

        step("Wait for app to launch");
        homePage.gotoHome();

        step("User login");
        loginPage.loginAsGuest();

        if(Configs.ENVIRONMENT == Environment.PROD) {
            step("Change currency to 'IDR'");
            homePage.changeCurrency("IDR","");
        }

        step("Get App Language");
        String language = homePage.getLanguageApp();

        step("Open Train Landing Page");
        homePage.openVerticalPageDeeplink("Trains", language, currency);

        step("Skip Popup");
        trainSearchPage.skipPopup();

        step("Select origin station");
        trainSearchPage.selectOrigin(originStation);

        step("Select destination station");
        trainSearchPage.selectDestination(destinationStation);

        step("Toggle round trip");
        trainSearchPage.toggleTripType(tripType);

        step("Select date");
        trainSearchPage.selectDate(departureDate);

        step("Add passenger");
        trainSearchPage.addPassenger(adultPassengers,infantPassengers);

        step("Search train");
        trainSearchPage.searchTrain();

        step("Skip Popup");
        trainSearchPage.skipPopup();

        step("Select departure train");
        trainSearchResultPage.selectTrain();

        step("Fill contact data");
        trainBookingPage.fillGuestDetail(email);

        step("Fill passenger detail");
        trainBookingPage.fillPassengerDetail(adultPassengers,infantPassengers);

        step("Continue to 2nd half");
        trainBookingPage.continueTo2ndHalf();

        step("Open price detail");
        trainBookingPage.openPriceDetail(tripType);

        step("Verify price detail");
        TrainPriceDetail priceDetail = new TrainPriceDetail();
        priceDetail.tripType = tripType;
        priceDetail.adultNumber = adultPassengers;
        priceDetail.infantNumber = infantPassengers;

        Assertion.assertThat("Is price breakdown correct", trainBookingPage.isPriceBreakdownCorrect(priceDetail), is(true));

        step("close price detail");
        trainBookingPage.closePriceDetail();

        step("Continue to payment");
        trainBookingPage.continueToPayment();

        step("Check order ID");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",!orderID.isBlank(),is(true));
    }

    @Author(names = {"Kartik"})
    @Module(name = "OrderAndPaymentDetails")
    @Tribe(name = "Train")
    @Vertical(name = "GroundTransport")
    @Test(dataProvider="testTrainCheckDetailPaymentPageGuestDataProvider")
    public void testTrainCheckDetailPaymentPageGuest(Map<String, String> param) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TrainSearchPage<WebDriver> trainSearchPage = TrainSearchPage.getInstance(session);
        TrainBookingPage<WebDriver> trainBookingPage = TrainBookingPage.getInstance(session);
        TrainSearchResultPage<WebDriver> trainSearchResultPage = TrainSearchResultPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);

        String email = param.get("email");
        String tripType = param.get("tripType");
        String currency = param.get("currency");
        String originStation = param.get("originStation");
        String destinationStation = param.get("destinationStation");
        String dateRange = param.get("dateRange");

        int adultPassengers = Integer.parseInt(param.get("adult"));
        int infantPassengers = Integer.parseInt(param.get("infant"));

        TrainAndWhooshGeneralData trainAndWhooshGeneralData = new TrainAndWhooshGeneralData(originStation, destinationStation,adultPassengers,adultPassengers,infantPassengers,tripType,dateRange);
        String departureDate = trainAndWhooshGeneralData.departureDateLong;

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Logout via deeplink");
        homePage.logoutViaDeeplink();

        step("Change currency to 'IDR'");
        homePage.changeCurrency("IDR","");

        step("Get App Language");
        String language = homePage.getLanguageApp();

        step("Open Train Landing Page");
        homePage.openVerticalPageDeeplink("Trains", language, currency);

        step("Skip Popup");
        trainSearchPage.skipPopup();

        step("Select origin station");
        trainAndWhooshGeneralData.originCityName = trainSearchPage.selectOriginAndGetCityName(originStation);

        step("Select destination station");
        trainAndWhooshGeneralData.destinationCityName = trainSearchPage.selectDestinationAndGetCityName(destinationStation);

        step("Toggle roundtrip");
        trainSearchPage.toggleTripType(tripType);

        step("Select date");
        trainSearchPage.selectDate(departureDate);

        step("Add passenger");
        trainSearchPage.addPassenger(adultPassengers,infantPassengers);

        step("Search train");
        trainSearchPage.searchTrain();

        step("Skip Popup");
        trainSearchPage.skipPopup();

        step("Select departure train");
        trainSearchResultPage.selectTrain();

        step("Fill contact data");
        trainBookingPage.fillGuestDetail(email);

        step("Fill passenger detail");
        trainBookingPage.fillPassengerDetail(adultPassengers,infantPassengers);

        step("Continue to 2nd half");
        trainBookingPage.continueTo2ndHalf();

        step("get details from the booking page");
        trainAndWhooshGeneralData.trainDataBookingPage = trainBookingPage.getDetailsTrainAndPassenger(language);

        step("get total payment");
        trainAndWhooshGeneralData.totalPaymentBookingPage = trainBookingPage.getTotalPayment();

        step("Open price detail");
        trainBookingPage.openPriceDetail(tripType);

        step("Verify price detail");
        TrainPriceDetail priceDetail = new TrainPriceDetail();
        priceDetail.tripType = tripType;
        priceDetail.adultNumber = adultPassengers;
        priceDetail.infantNumber = infantPassengers;

        Assertion.assertThat("Is price breakdown correct", trainBookingPage.isPriceBreakdownCorrect(priceDetail), is(true));

        step("close price detail");
        trainBookingPage.closePriceDetail();

        step("Continue to payment");
        trainBookingPage.continueToPayment();

        step("Check order ID");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",!orderID.isBlank(),is(true));

        step("get guest and train data from payment page");
        trainAndWhooshGeneralData.trainDataPaymentPage = paymentPage.getTrainGuestInfoPaymentPage();

        step("open price detail page");
        paymentPage.openPriceDetail();

        step("get depart and arrival station price detail");
        trainAndWhooshGeneralData.departAndArrivalStationPriceDetail = paymentPage.getDepartAndArrivalStationPriceDetail();

        step("get total price");
        trainAndWhooshGeneralData.totalPaymentPaymentPage = paymentPage.getPriceWithoutOtherFees();

        step("price breakdown");
        Assertion.assertThat("is price breakdown correct: ", paymentPage.priceBreakDown(), is(true));

        step("verify details");
        paymentPage.verifyDetailsTrainAndPassenger(trainAndWhooshGeneralData);
    }

    @Author(names = {"kartik"})
    @Module(name = "OrderAndPaymentDetails")
    @Tribe(name = "Train")
    @Vertical(name = "GroundTransport")
    @Test(dataProvider="testTrainCheckOrderDetailInMyOrderMenuGuestDataProvider")
    public void testTrainCheckOrderDetailInMyOrderMenuGuest(Map<String, String> param){
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TrainSearchPage<WebDriver> trainSearchPage = TrainSearchPage.getInstance(session);
        TrainSearchResultPage<WebDriver> trainSearchResultPage = TrainSearchResultPage.getInstance(session);
        TrainBookingPage<WebDriver> trainBookingPage = TrainBookingPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);

        String email = param.get("email");
        String tripType = param.get("tripType");
        String currency = param.get("currency");
        String originStation = param.get("originStation");
        String destinationStation = param.get("destinationStation");
        String dateRange = param.get("dateRange");

        int adultPassengers = Integer.parseInt(param.get("adult"));
        int infantPassengers = Integer.parseInt(param.get("infant"));

        String departureDate = CoreFunctions.addToLongRandomDate(dateRange);

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Logout via deeplink");
        homePage.logoutViaDeeplink();

        step("Change currency to 'IDR'");
        homePage.changeCurrency("IDR","");

        step("Get App Language");
        String language = homePage.getLanguageApp();

        step("Open Train Landing Page");
        homePage.openVerticalPageDeeplink("Trains", language, currency);

        step("Skip Popup");
        trainSearchPage.skipPopup();


        step("Select origin station");
        trainSearchPage.selectOrigin(originStation);

        step("Select destination station");
        trainSearchPage.selectDestination(destinationStation);

        step("Toggle roundtrip");
        trainSearchPage.toggleTripType(tripType);

        step("Select date");
        trainSearchPage.selectDate(departureDate);

        step("Add passenger");
        trainSearchPage.addPassenger(adultPassengers,infantPassengers);

        step("Search train");
        trainSearchPage.searchTrain();

        step("Skip Popup");
        trainSearchPage.skipPopup();

        step("Select departure train");
        trainSearchResultPage.selectTrain();

        step("Fill contact data");
        trainBookingPage.fillGuestDetail(email);

        step("Fill passenger detail");
        trainBookingPage.fillPassengerDetail(adultPassengers,infantPassengers);

        step("Continue to 2nd half");
        trainBookingPage.continueTo2ndHalf();

        step("Continue to payment");
        trainBookingPage.continueToPayment();

        step("Check order ID");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",!orderID.isBlank(),is(true));

        step("Open payment method");
        paymentPage.openPaymentMethod();

        if(Configs.ENVIRONMENT == Environment.PROD){
            step("Select virtual account as payment method");
            paymentPage.selectPaymentMethod("BCA Virtual Account");
        }else {
            step("select sakaku as payment method");
            paymentPage.selectPaymentMethod("Sakuku");
        }

        step("get guest and train data from payment page");
        TrainData data = paymentPage.getTrainGuestInfoPaymentPage();
        data.orderId = orderID;

        step("get total price");
        String totalPrice = paymentPage.getTotalPriceFirstPage();

        step("Submit payment");
        paymentPage.clickPayButton();

        if(Configs.ENVIRONMENT == Environment.PROD){
            step("Open order");
            paymentPage.seeOrderList();
            orderListPage.openOrderDetailWaitingForPayment(orderID);
        }else {
            step("open issued order");
            Assertion.assertThat("Payment Successful and Thank You page is shown", paymentPage.isThankYouPageShown(), is(true));
            orderListPage.openIssuedTicketViaDeeplink(orderID);
            
            Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.getETicketLabel(), either(is("Your e-ticket is here")).or(is("E-tiketmu di sini")));
        }

        step("check train and passenger detail correct");
        orderDetailPage.checkTrainDetails(data);
        orderDetailPage.checkPassengerDetailsTrain(data);

        step("verify total payment correct");
        Assertion.assertThat("is total payment from payment is equals to order menu",orderDetailPage.verifyTotalPaymentTrain(totalPrice),is(true));
    }

    @Author(names = {"Zia"})
    @Module(name = "CheckIssuedOrder")
    @Tribe(name = "Train")
    @Vertical(name = "GroundTransport")
    @Test
    public void testTrainCheckIssuedOrderGuest() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testTrainCheckIssuedOrderGuest");
        BaseTest.paramsThreadLocal.set(param);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TrainSearchPage<WebDriver> trainSearchPage = TrainSearchPage.getInstance(session);
        TrainSearchResultPage<WebDriver> trainSearchResultPage = TrainSearchResultPage.getInstance(session);
        TrainBookingPage<WebDriver> trainBookingPage = TrainBookingPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);

        String tripType = param.get("tripType");
        String currency = param.get("currency");
        String originStation = param.get("originStation");
        String destinationStation = param.get("destinationStation");
        String cardNumber = param.get("cardNumber");
        String cvv = param.get("cvv");
        String expiry = param.get("expiry");
        String cardName = param.get("cardName");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int infantPassengers = Integer.parseInt(param.get("infant"));

        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate,daysToDepartureDate);

        String[] departureDateArray = departureDate.split(" ");
        String date = departureDateArray[0];

        String email = param.get("email");

        step("Wait for app to launch");
        homePage.gotoHome();

        step("User login as guest");
        loginPage.loginAsGuest();

        if(Configs.ENVIRONMENT == Environment.PROD) {
            step("Change currency to 'IDR'");
            homePage.changeCurrency("IDR","");
        }

        step("Get App Language");
        String language = homePage.getLanguageApp();

        step("Open Train Landing Page");
        homePage.openVerticalPageDeeplink("Trains", language, currency);

        step("Skip Popup");
        trainSearchPage.skipPopup();

        step("Select origin station");
        trainSearchPage.selectOrigin(originStation);

        step("Select destination station");
        trainSearchPage.selectDestination(destinationStation);

        step("Toggle oneway");
        trainSearchPage.toggleTripType(tripType);

        step("Select date");
        trainSearchPage.selectDate(departureDate);

        step("Add passenger");
        trainSearchPage.addPassenger(adultPassengers,infantPassengers);

        step("Search train");
        trainSearchPage.searchTrain();

        step("Skip Popup");
        trainSearchPage.skipPopup();

        step("Select departure train");
        trainSearchResultPage.selectTrain();

        step("Fill contact data");
        trainBookingPage.fillGuestDetail(email);

        step("Fill passenger detail");
        trainBookingPage.fillPassengerDetail(adultPassengers,infantPassengers);

        step("Get passenger data");
        String passengerIdNumber = trainBookingPage.getPassengerId();

        step("Continue to 2nd half");
        trainBookingPage.continueTo2ndHalf();

        step("get details from the booking page");
        TrainData dataFromBookingPage = trainBookingPage.getDetailsTrainAndPassenger(language);

        step("Continue to payment");
        trainBookingPage.continueToPayment();

        step("Check order ID");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",!orderID.isBlank(),is(true));

        step("Pay with CC");
        paymentPage.openPaymentMethod();
        paymentPage.selectPaymentMethod("Credit Card");
        paymentPage.fillCCDetailRealIssued(cardNumber, expiry, cvv, cardName);
        paymentPage.clickPayButtonCC();
        paymentPage.checkCcOtp();

        step("Go to the order list");
        paymentPage.seeOrderList();

        step("Open train order detail");
        String orderTitle = originStation + " - " + destinationStation;
        orderListPage.findActiveTicket(orderTitle, date, orderID, 10);

        step("Open e-Ticket");
        orderDetailPage.clickEticketFromHeader();

        step("e-Ticket file is opened and contain train ticket info");
        String seat = orderDetailPage.convertSeatCodeWithLang(dataFromBookingPage.seat,language);
        String actualPdfContent = orderDetailPage.getPDFContentFile();
        Assertion.assertThat("Order ID on the PDF should be equal on the App "+orderID, actualPdfContent.contains(orderID), is(true));
        Assertion.assertThat("Passenger name on the PDF should be equal on the App "+dataFromBookingPage.guestName, actualPdfContent.contains(dataFromBookingPage.guestName), is(true));
        Assertion.assertThat("Passenger ID on the PDF should be equal on the App "+passengerIdNumber, actualPdfContent.contains(passengerIdNumber), is(true));
        Assertion.assertThat("Seat on the PDF should be equal on the App "+seat, actualPdfContent.contains(seat), is(true));
    }

    @Author(names = {"Zia"})
    @Module(name = "CheckIssuedOrder")
    @Tribe(name = "Train")
    @Vertical(name = "GroundTransport")
    @Test
    public void testTrainCheckIssuedOrderLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testTrainCheckIssuedOrderLogin");
        BaseTest.paramsThreadLocal.set(param);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TrainSearchPage<WebDriver> trainSearchPage = TrainSearchPage.getInstance(session);
        TrainSearchResultPage<WebDriver> trainSearchResultPage = TrainSearchResultPage.getInstance(session);
        TrainBookingPage<WebDriver> trainBookingPage = TrainBookingPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);

        String tripType = param.get("tripType");
        String currency = param.get("currency");
        String originStation = param.get("originStation");
        String destinationStation = param.get("destinationStation");
        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");

        String cardNumber = param.get("cardNumber");
        String cvv = param.get("cvv");
        String expiry = param.get("expiry");
        String cardName = param.get("cardName");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int infantPassengers = Integer.parseInt(param.get("infant"));

        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate,daysToDepartureDate);

        String[] departureDateArray = departureDate.split(" ");
        String date = departureDateArray[0];

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        if(Configs.ENVIRONMENT == Environment.PROD) {
            step("Change currency to 'IDR'");
            homePage.changeCurrency("IDR",username);
        }

        step("Get App Language");
        String language = homePage.getLanguageApp();

        step("Open Train Landing Page");
        homePage.openVerticalPageDeeplink("Trains", language, currency);

        step("Skip Popup");
        trainSearchPage.skipPopup();

        step("Select origin station");
        trainSearchPage.selectOrigin(originStation);

        step("Select destination station");
        trainSearchPage.selectDestination(destinationStation);

        step("Toggle oneway");
        trainSearchPage.toggleTripType(tripType);

        step("Select date");
        trainSearchPage.selectDate(departureDate);

        step("Add passenger");
        trainSearchPage.addPassenger(adultPassengers,infantPassengers);

        step("Search train");
        trainSearchPage.searchTrain();

        step("Skip Popup");
        trainSearchPage.skipPopup();

        step("Select departure train");
        trainSearchResultPage.selectTrain();

        step("Fill passenger detail");
        trainBookingPage.fillPassengerDetail(adultPassengers,infantPassengers);

        step("Get passenger data");
        String passengerIdNumber = trainBookingPage.getPassengerId();

        step("Continue to 2nd half");
        trainBookingPage.continueTo2ndHalf();

        step("get details from the booking page");
        TrainData dataFromBookingPage = trainBookingPage.getDetailsTrainAndPassenger(language);

        step("Continue to payment");
        trainBookingPage.continueToPayment();

        step("Check order ID");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",!orderID.isBlank(),is(true));

        step("Pay with CC");
        paymentPage.openPaymentMethod();
        paymentPage.selectPaymentMethod("Credit Card");
        paymentPage.fillCCDetailRealIssued(cardNumber, expiry, cvv, cardName);
        paymentPage.clickPayButtonCC();
        paymentPage.checkCcOtp();

        step("Go to the order list");
        paymentPage.seeOrderList();

        step("Open train order detail");
        String orderTitle = originStation + " - " + destinationStation;
        orderListPage.findActiveTicket(orderTitle, date, orderID, 10);

        step("Open e-Ticket");
        orderDetailPage.clickEticketFromHeader();

        step("e-Ticket file is opened and contain train ticket info");

        String seat = orderDetailPage.convertSeatCodeWithLang(dataFromBookingPage.seat,language);
        String actualPdfContent = orderDetailPage.getPDFContentFile();
        Assertion.assertThat("Order ID on the PDF should be equal on the App "+orderID, actualPdfContent.contains(orderID), is(true));
        Assertion.assertThat("Passenger name on the PDF should be equal on the App "+dataFromBookingPage.guestName, actualPdfContent.contains(dataFromBookingPage.guestName), is(true));
        Assertion.assertThat("Passenger ID on the PDF should be equal on the App "+passengerIdNumber, actualPdfContent.contains(passengerIdNumber), is(true));
        Assertion.assertThat("Seat on the PDF should be equal on the App "+seat, actualPdfContent.contains(seat), is(true));
    }

    @Author(names = {"Zia"})
    @Module(name = "homePageWithoutTrain")
    @Tribe(name = "Train")
    @Vertical(name = "GroundTransport")
    @Test
    public void testVerifyHomePageWithoutTrainsNonIDRGuest() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testVerifyHomePageWithoutTrainsNonIDRGuest");
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

        step("Find Train Vertical");
        Assertion.assertThat("is Trains not displayed in non-IDR currency {"+currency+" }",homePage.checkVerticalPresence("Trains"),is(false));
    }

    @Author(names = {"Zia"})
    @Module(name = "homePageWithoutTrain")
    @Tribe(name = "Train")
    @Vertical(name = "GroundTransport")
    @Test
    public void testVerifyHomePageWithoutTrainsNonIDRLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testVerifyHomePageWithoutTrainsNonIDRLogin");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);

        String currency = param.get("currency");
        String loginMethod = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");

        step("user open app");
        homePage.gotoHome();

        step("User input credential");
        loginPage.loginWith(loginMethod, username, password);

        step("change currency to default IDR");
        homePage.changeCurrency("IDR",username);

        step("Change currency");
        homePage.changeCurrency(currency,username);

        step("Find Train Vertical");
        Assertion.assertThat("is Trains not displayed in non-IDR currency {"+currency+" }",homePage.checkVerticalPresence("Trains"),is(false));
    }

    @Author(names = {"Zia"})
    @Module(name = "TrainDeeplink")
    @Tribe(name = "Train")
    @Vertical(name = "GroundTransport")
    @Test
    public void testBookTrainViaDeeplinkLogin(){
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testBookTrainViaDeeplinkLogin");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TrainSearchPage<WebDriver> trainSearchPage = TrainSearchPage.getInstance(session);
        TrainSearchResultPage<WebDriver> trainSearchResultPage = TrainSearchResultPage.getInstance(session);
        TrainBookingPage<WebDriver> trainBookingPage = TrainBookingPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);

        String tripType = param.get("tripType");
        String currency = param.get("currency");
        String originStation = param.get("originStation");
        String destinationStation = param.get("destinationStation");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int infantPassengers = Integer.parseInt(param.get("infant"));

        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate,daysToDepartureDate);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Change currency to "+currency);
        homePage.changeCurrency(currency,username);

        step("Open train page via deeplink");
        trainSearchPage.openViaDeeplink();

        step("Skip Popup");
        trainSearchPage.skipPopup();


        step("Select origin station");
        trainSearchPage.selectOrigin(originStation);

        step("Select destination station");
        trainSearchPage.selectDestination(destinationStation);

        step("Toggle roundtrip");
        trainSearchPage.toggleTripType(tripType);

        step("Select date");
        trainSearchPage.selectDate(departureDate);

        step("Add passenger");
        trainSearchPage.addPassenger(adultPassengers,infantPassengers);

        step("Search train");
        trainSearchPage.searchTrain();

        step("Skip Popup");
        trainSearchPage.skipPopup();

        step("Verify train schedule prices");
        Assertion.assertThat("All ticket prices are in IDR", trainSearchPage.getPrices().stream().filter(price -> price.matches("IDR \\d{1,3}(,\\d{3})*"))
                        .allMatch(price -> price.contains("IDR")), is(true));

        step("Select departure train");
        trainSearchResultPage.selectTrain();

        step("Fill passenger detail");
        trainBookingPage.fillPassengerDetail(adultPassengers,infantPassengers);

        step("Continue to 2nd half");
        trainBookingPage.continueTo2ndHalf();

        step("Verify train insurance prices in IDR");
        Assertion.assertThat("is insurance prices in IDR", (trainBookingPage.getInsurancePrices().stream().allMatch(price -> (price.contains("IDR")))), is(true));

        step("Open price detail");
        trainBookingPage.openPriceDetail(tripType);

        step("Verify booking form detail prices in IDR");
        Assertion.assertThat("is price detail booking form page in IDR", (trainBookingPage.getPriceDetailPrices().stream().allMatch(price -> (price.contains("IDR") || price.equals("Termasuk") || price.equals("Included") || price.equals("Free") || price.equals("Gratis")))), is(true));
        trainBookingPage.closePriceDetail();

        step("Verify booking form total price in IDR");
        Assertion.assertThat("is total price booking form in IDR", (trainBookingPage.getTotalPayment().contains("IDR")), is(true));

        step("Continue to payment");
        trainBookingPage.continueToPayment();

        step("Check order ID");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",!orderID.isBlank(),is(true));

        step("Verify price in payment page");
        paymentPage.openPriceDetail();
        Assertion.assertThat("is price detail payment page in IDR", (paymentPage.getPriceDetail().stream().allMatch(price -> (price.contains("IDR") || price.equals("Termasuk") || price.equals("Included") || price.equals("Free") || price.equals("Gratis")))), is(true));
        Assertion.assertThat("is price total payment page in IDR", (paymentPage.getTotalPrice().contains("IDR")), is(true));
    }

    @Author(names = {"Zia"})
    @Module(name = "TrainDeeplink")
    @Tribe(name = "Train")
    @Vertical(name = "GroundTransport")
    @Test
    public void testBookTrainViaDeeplinkGuest(){
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testBookTrainViaDeeplinkGuest");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TrainSearchPage<WebDriver> trainSearchPage = TrainSearchPage.getInstance(session);
        TrainSearchResultPage<WebDriver> trainSearchResultPage = TrainSearchResultPage.getInstance(session);
        TrainBookingPage<WebDriver> trainBookingPage = TrainBookingPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);

        String tripType = param.get("tripType");
        String currency = param.get("currency");
        String originStation = param.get("originStation");
        String destinationStation = param.get("destinationStation");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int infantPassengers = Integer.parseInt(param.get("infant"));

        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate,daysToDepartureDate);

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginAsGuest();

        step("Change currency to "+currency);
        homePage.changeCurrency(currency,"");

        step("Open train page via deeplink");
        trainSearchPage.openViaDeeplink();

        step("Skip Popup");
        trainSearchPage.skipPopup();


        step("Select origin station");
        trainSearchPage.selectOrigin(originStation);

        step("Select destination station");
        trainSearchPage.selectDestination(destinationStation);

        step("Toggle roundtrip");
        trainSearchPage.toggleTripType(tripType);

        step("Select date");
        trainSearchPage.selectDate(departureDate);

        step("Add passenger");
        trainSearchPage.addPassenger(adultPassengers,infantPassengers);

        step("Search train");
        trainSearchPage.searchTrain();

        step("Skip Popup");
        trainSearchPage.skipPopup();

        step("Verify train schedule prices");
        Assertion.assertThat("All ticket prices are in IDR", trainSearchPage.getPrices().stream().filter(price -> price.matches("IDR \\d{1,3}(,\\d{3})*")) // Filter valid price entries
                .allMatch(price -> price.contains("IDR")), is(true));

        step("Select departure train");
        trainSearchResultPage.selectTrain();

        step("Fill contact data");
        trainBookingPage.fillGuestDetail(CoreFunctions.randomEmail());

        step("Fill passenger detail");
        trainBookingPage.fillPassengerDetail(adultPassengers,infantPassengers);

        step("Continue to 2nd half");
        trainBookingPage.continueTo2ndHalf();

        step("Verify train insurance prices in IDR");
        Assertion.assertThat("is insurance prices in IDR", (trainBookingPage.getInsurancePrices().stream().allMatch(price -> (price.contains("IDR")))), is(true));

        step("Open price detail");
        trainBookingPage.openPriceDetail(tripType);

        step("Verify booking form detail prices in IDR");
        Assertion.assertThat("is price detail booking form page in IDR", (trainBookingPage.getPriceDetailPrices().stream().allMatch(price -> (price.contains("IDR") || price.equals("Termasuk") || price.equals("Included") || price.equals("Free") || price.equals("Gratis")))), is(true));
        trainBookingPage.closePriceDetail();

        step("Verify booking form total price in IDR");
        Assertion.assertThat("is total price booking form in IDR", (trainBookingPage.getTotalPayment().contains("IDR")), is(true));

        step("Continue to payment");
        trainBookingPage.continueToPayment();

        step("Check order ID");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",!orderID.isBlank(),is(true));

        step("Verify price in payment page");
        paymentPage.openPriceDetail();
        Assertion.assertThat("is price detail payment page in IDR", (paymentPage.getPriceDetail().stream().allMatch(price -> (price.contains("IDR") || price.equals("Termasuk") || price.equals("Included") || price.equals("Free") || price.equals("Gratis")))), is(true));
        Assertion.assertThat("is price total payment page in IDR", (paymentPage.getTotalPrice().contains("IDR")), is(true));
    }

    @Author(names = {"Kartik"})
    @Module(name = "CrossSell")
    @Tribe(name = "Train")
    @Vertical(name = "GroundTransport")
    @Test
    public void testCrossSellToBusNoRouteScheduled() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testCrossSellToBusNoRouteScheduled");
        BaseTest.paramsThreadLocal.set(param);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TrainSearchPage<WebDriver> trainSearchPage = TrainSearchPage.getInstance(session);
        TrainSearchResultPage<WebDriver> trainSearchResultPage = TrainSearchResultPage.getInstance(session);
        BusSearchResultPage<WebDriver> busSearchResultPage = BusSearchResultPage.getInstance(session);

        String originStation = param.get("originStation");
        String destinationStation = param.get("destinationStation");
        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int adultPassengers = Integer.parseInt(param.get("adult"));

        TrainAndWhooshGeneralData trainAndWhooshGeneralData = new TrainAndWhooshGeneralData(originStation, destinationStation,adultPassengers,adultPassengers,0,"oneway",daysToDepartureDate);
        String departureDate = trainAndWhooshGeneralData.departureDateLong;

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink (if needed) and change currency to " + "IDR");
        homePage.loginIfLoggedOutAndChangeCurrencyViaDeeplink(username, password, "IDR");

        step("Get App Language");
        String language = homePage.getLanguageApp();

        step("Open Train Landing Page");
        homePage.openVerticalPageDeeplink("Trains", language, "IDR");

        step("Skip Popup");
        trainSearchPage.skipPopup();

        step("Select origin station");
        trainAndWhooshGeneralData.originCityName = trainSearchPage.selectOriginAndGetCityName(originStation);

        step("Select destination station");
        trainAndWhooshGeneralData.destinationCityName = trainSearchPage.selectDestinationAndGetCityName(destinationStation);

        step("Add passenger");
        trainSearchPage.addPassenger(adultPassengers,0);

        step("Select date");
        trainSearchPage.selectDate(departureDate);

        step("Search train");
        trainSearchPage.searchTrain();

        step("Skip Popup");
        trainSearchPage.skipPopup();

        step("Verify that SRP don't have any data");
        Boolean verifyData = trainSearchResultPage.verifySRPHaveNoResult();
        Assertion.assertThat("Verify that search result page don't have any data",verifyData,is(true));

        step("Click cross vertical bus button");
        trainSearchResultPage.clickCrossSell();
        Assertion.assertThat("Verify Header location on BUS search result page",busSearchResultPage.verifyOriginAndDestinationCrossSell(trainAndWhooshGeneralData),is(true));
    }

    @Author(names = {"Kartik"})
    @Module(name = "CrossSell")
    @Tribe(name = "Train")
    @Vertical(name = "GroundTransport")
    @Test
    public void testCrossSellToBusNoRouteScheduledGuest() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testCrossSellToBusNoRouteScheduledGuest");
        BaseTest.paramsThreadLocal.set(param);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TrainSearchPage<WebDriver> trainSearchPage = TrainSearchPage.getInstance(session);
        TrainSearchResultPage<WebDriver> trainSearchResultPage = TrainSearchResultPage.getInstance(session);
        BusSearchResultPage<WebDriver> busSearchResultPage = BusSearchResultPage.getInstance(session);

        String originStation = param.get("originStation");
        String destinationStation = param.get("destinationStation");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int adultPassengers = Integer.parseInt(param.get("adult"));

        TrainAndWhooshGeneralData trainAndWhooshGeneralData = new TrainAndWhooshGeneralData(originStation, destinationStation,adultPassengers,adultPassengers,0,"oneway",daysToDepartureDate);
        String departureDate = trainAndWhooshGeneralData.departureDateLong;

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Logout via deeplink");
        homePage.logoutViaDeeplink();

        if(Configs.ENVIRONMENT == Environment.PROD) {
            step("Change currency to 'IDR'");
            homePage.changeCurrency("IDR","");
        }

        step("Get App Language");
        String language = homePage.getLanguageApp();

        step("Open Train Landing Page");
        homePage.openVerticalPageDeeplink("Trains", language, "IDR");

        step("Skip Popup");
        trainSearchPage.skipPopup();

        step("Select origin station");
        trainAndWhooshGeneralData.originCityName = trainSearchPage.selectOriginAndGetCityName(originStation);

        step("Select destination station");
        trainAndWhooshGeneralData.destinationCityName = trainSearchPage.selectDestinationAndGetCityName(destinationStation);

        step("Select date");
        trainSearchPage.selectDate(departureDate);

        step("Add passenger");
        trainSearchPage.addPassenger(adultPassengers,0);

        step("Search train");
        trainSearchPage.searchTrain();

        step("Skip Popup");
        trainSearchPage.skipPopup();

        step("Verify that SRP don't have any data");
        Boolean verifyData = trainSearchResultPage.verifySRPHaveNoResult();
        Assertion.assertThat("Verify that search result page don't have any data",verifyData,is(true));

        step("Click cross vertical bus button");
        trainSearchResultPage.clickCrossSell();
        Assertion.assertThat("Verify Header location on BUS search result page",busSearchResultPage.verifyOriginAndDestinationCrossSell(trainAndWhooshGeneralData),is(true));
    }

    @Author(names = {"Kartik"})
    @Module(name = "CrossSell")
    @Tribe(name = "Train")
    @Vertical(name = "GroundTransport")
    @Test
    public void testCrossSellToBusPartialSoldOutSchedule() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testCrossSellToBusPartialSoldOutSchedule");
        BaseTest.paramsThreadLocal.set(param);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TrainSearchPage<WebDriver> trainSearchPage = TrainSearchPage.getInstance(session);
        TrainSearchResultPage<WebDriver> trainSearchResultPage = TrainSearchResultPage.getInstance(session);
        BusSearchResultPage<WebDriver> busSearchResultPage = BusSearchResultPage.getInstance(session);

        String originStation = param.get("originStation");
        String destinationStation = param.get("destinationStation");
        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        if(Configs.ENVIRONMENT == Environment.PROD) {
            step("Change currency to 'IDR'");
            homePage.changeCurrency("IDR",username);
        }

        step("Get App Language");
        String language = homePage.getLanguageApp();

        step("Open Train Landing Page");
        homePage.openVerticalPageDeeplink("Trains", language, "IDR");

        step("Skip Popup");
        trainSearchPage.skipPopup();

        step("Select origin station");
        trainSearchPage.selectOrigin(originStation);

        step("Select destination station");
        trainSearchPage.selectDestination(destinationStation);

        step("Select date");
        trainSearchPage.selectDate(departureDate);

        step("Add passenger");
        trainSearchPage.addPassenger(adultPassengers,0);

        step("Search train");
        trainSearchPage.searchTrain();

        step("Skip Popup");
        trainSearchPage.skipPopup();

        step("Verify that SRP have any data and cross sell to bus button");
        Boolean verifyData = trainSearchPage.VerifyAndClickPartialSellBanner();
        Assertion.assertThat("SRP should have data and display the option to book bus and shuttle.",verifyData,is(true));

        step("Click cross vertical bus button");
        Assertion.assertThat("Verify Header location on BUS search result page",busSearchResultPage.verifyOriginAndDestinationCrossSell(originStation,destinationStation),is(true));
    }

    @Author(names = {"Kartik"})
    @Module(name = "CrossSell")
    @Tribe(name = "Train")
    @Vertical(name = "GroundTransport")
    @Test
    public void testCrossSellToBusPartialSoldOutScheduleGuest() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testCrossSellToBusPartialSoldOutScheduleGuest");
        BaseTest.paramsThreadLocal.set(param);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TrainSearchPage<WebDriver> trainSearchPage = TrainSearchPage.getInstance(session);
        TrainSearchResultPage<WebDriver> trainSearchResultPage = TrainSearchResultPage.getInstance(session);
        BusSearchResultPage<WebDriver> busSearchResultPage = BusSearchResultPage.getInstance(session);

        String originStation = param.get("originStation");
        String destinationStation = param.get("destinationStation");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Login as guest");
        loginPage.loginAsGuest();

        if(Configs.ENVIRONMENT == Environment.PROD) {
            step("Change currency to 'IDR'");
            homePage.changeCurrency("IDR","");
        }

        step("Get App Language");
        String language = homePage.getLanguageApp();

        step("Open Train Landing Page");
        homePage.openVerticalPageDeeplink("Trains", language, "IDR");

        step("Skip Popup");
        trainSearchPage.skipPopup();

        step("Select origin station");
        trainSearchPage.selectOrigin(originStation);

        step("Select destination station");
        trainSearchPage.selectDestination(destinationStation);

        step("Select date");
        trainSearchPage.selectDate(departureDate);

        step("Add passenger");
        trainSearchPage.addPassenger(adultPassengers,0);

        step("Search train");
        trainSearchPage.searchTrain();

        step("Skip Popup");
        trainSearchPage.skipPopup();

        step("Verify that SRP have any data and cross sell to bus button");
        Boolean verifyData = trainSearchPage.VerifyAndClickPartialSellBanner();
        Assertion.assertThat("SRP should have data and display the option to book bus and shuttle.",verifyData,is(true));

        step("Verify cross vertical bus Path");
        Assertion.assertThat("Verify Header location on BUS search result page",busSearchResultPage.verifyOriginAndDestinationCrossSell(originStation,destinationStation),is(true));
    }

    @Author(names = {"kartik"})
    @Module(name = "CheckIssuedOrder")
    @Tribe(name = "Train")
    @Vertical(name = "GroundTransport")
    @Test
    public void testUserCheckInfoAvailabilityOnlineCheckIn() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testUserCheckInfoAvailabilityOnlineCheckIn");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        TrainSearchPage<WebDriver> trainSearchPage = TrainSearchPage.getInstance(session);
        TrainSearchResultPage<WebDriver> trainSearchResultPage = TrainSearchResultPage.getInstance(session);
        TrainBookingPage<WebDriver> trainBookingPage = TrainBookingPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);

        String tripType = param.get("tripType");
        String originCity = param.get("originCity");  // updated from originStation to originCity
        String destinationCity = param.get("destinationCity");  // updated from destinationStation to destinationCity
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);
        String username = param.get("username");
        String password = param.get("password");

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink (if needed) and change currency to " + "IDR");
        homePage.loginIfLoggedOutAndChangeCurrencyViaDeeplink(username, password, "IDR");

        step("Get App Language");
        String language = homePage.getLanguageApp();

        step("Open Train Landing Page");
        homePage.openVerticalPageDeeplink("Trains", language, "IDR");

        step("Skip Popup");
        trainSearchPage.skipPopup();

        step("Select origin station");
        trainSearchPage.selectOrigin(originCity);

        step("Select destination station");
        trainSearchPage.selectDestination(destinationCity);

        step("Toggle trip type");
        trainSearchPage.toggleTripType(tripType);

        step("Select date");
        trainSearchPage.selectDate(departureDate);

        step("Add passenger");
        trainSearchPage.addPassenger(adultPassengers,infantPassengers);

        step("Search train");
        trainSearchPage.searchTrain();

        step("Skip Popup");
        trainSearchPage.skipPopup();

        step("Select departure train");
        trainSearchResultPage.selectTrain();

        step("Fill passenger detail");
        trainBookingPage.fillPassengerDetail(adultPassengers,infantPassengers);

        step("Continue to payment");
        trainBookingPage.continueToPayment();

        step("Check order ID");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",!orderID.isBlank(),is(true));

        step("Open payment method");
        paymentPage.openPaymentMethod();

        step("Select Sakuku as payment method");
        paymentPage.selectPaymentMethod("Sakuku");

        step("Submit payment");
        paymentPage.clickPayButton();
        Assertion.assertThat("Payment Successful and Thank You page is shown", paymentPage.isThankYouPageShown(), is(true));

        step("Open order details page");
        orderListPage.openIssuedTicketViaDeeplink(orderID);
        orderDetailPage.getOrderID();

        step("verify on See e-boarding pass not shown");
        Assertion.assertThat("See button print e-boarding pass not shown",orderDetailPage.clickSeeBoardingPass(),is(false));
    }

    @Author(names = {"kartik"})
    @Module(name = "CheckIssuedOrder")
    @Tribe(name = "Train")
    @Vertical(name = "GroundTransport")
    @Test
    public void testUserCheckInfoAvailabilityOnlineCheckInGuest() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testUserCheckInfoAvailabilityOnlineCheckInGuest");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        TrainSearchPage<WebDriver> trainSearchPage = TrainSearchPage.getInstance(session);
        TrainSearchResultPage<WebDriver> trainSearchResultPage = TrainSearchResultPage.getInstance(session);
        TrainBookingPage<WebDriver> trainBookingPage = TrainBookingPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);

        String originCity = param.get("originCity");  // updated from originStation to originCity
        String destinationCity = param.get("destinationCity");  // updated from destinationStation to destinationCity
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);
        String username = param.get("username");

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Logout via deeplink");
        homePage.logoutViaDeeplink();

        if(Configs.ENVIRONMENT == Environment.PROD) {
            step("Change currency to 'IDR'");
            homePage.changeCurrency("IDR",username);
        }

        step("Get App Language");
        String language = homePage.getLanguageApp();

        step("Open Train Landing Page");
        homePage.openVerticalPageDeeplink("Trains", language, "IDR");

        step("Skip Popup");
        trainSearchPage.skipPopup();

        step("Select origin station");
        trainSearchPage.selectOrigin(originCity);

        step("Select destination station");
        trainSearchPage.selectDestination(destinationCity);

        step("Select date");
        trainSearchPage.selectDate(departureDate);

        step("Add passenger");
        trainSearchPage.addPassenger(adultPassengers,infantPassengers);

        step("Search train");
        trainSearchPage.searchTrain();

        step("Skip Popup");
        trainSearchPage.skipPopup();

        step("Select departure train");
        trainSearchResultPage.selectTrain();

        step("Fill contact data");
        trainBookingPage.fillGuestDetail(CoreFunctions.randomEmail());

        step("Fill passenger detail");
        trainBookingPage.fillPassengerDetail(adultPassengers,infantPassengers);

        step("Continue to payment page");
        trainBookingPage.continueToPayment();

        step("Check order ID");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",!orderID.isBlank(),is(true));

        step("Open payment method");
        paymentPage.openPaymentMethod();

        step("Select Sakuku as payment method");
        paymentPage.selectPaymentMethod("Sakuku");

        step("Submit payment");
        paymentPage.clickPayButton();
        Assertion.assertThat("Payment Successful and Thank You page is shown", paymentPage.isThankYouPageShown(), is(true));

        step("Open order details page");
        orderListPage.openIssuedTicketViaDeeplink(orderID);
        orderDetailPage.getOrderID();

        step("verify on See e-boarding pass not shown");
        Assertion.assertThat("See button print e-boarding pass not shown",orderDetailPage.clickSeeBoardingPass(),is(false));
    }

    @Author(names = {"kartik"})
    @Module(name = "onlineCheckIn")
    @Tribe(name = "Train")
    @Vertical(name = "GroundTransport")
    @Test
    public void testUserAbleToOnlineCheckIn() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testUserAbleToOnlineCheckIn");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TrainSearchPage<WebDriver> trainSearchPage = TrainSearchPage.getInstance(session);
        TrainSearchResultPage<WebDriver> trainSearchResultPage = TrainSearchResultPage.getInstance(session);
        TrainBookingPage<WebDriver> trainBookingPage = TrainBookingPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);

        String originCity = param.get("originCity");
        String destinationCity = param.get("destinationCity");
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String dateRange = param.get("dateRange");
        String tripType = param.get("tripType");

        String departureDate = CoreFunctions.addToLongRandomDate(dateRange);

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink (if needed) and change currency to " + "IDR");
        homePage.loginIfLoggedOutAndChangeCurrencyViaDeeplink(username, password, "IDR");

        step("Get App Language");
        String language = homePage.getLanguageApp();

        step("Open Train Landing Page");
        homePage.openVerticalPageDeeplink("Trains", language, "IDR");

        step("Skip Popup");
        trainSearchPage.skipPopup();

        step("Select origin station");
        trainSearchPage.selectOrigin(originCity);

        step("Select destination station");
        trainSearchPage.selectDestination(destinationCity);

        step("Toggle tripType { " + tripType +" }");
        trainSearchPage.toggleTripType(tripType);

        step("Select date");
        trainSearchPage.selectDate(departureDate);

        step("Add passenger");
        trainSearchPage.addPassenger(adultPassengers,infantPassengers);

        step("Search train");
        trainSearchPage.searchTrain();

        step("Skip Popup");
        trainSearchPage.skipPopup();

        step("Select departure train");
        trainSearchResultPage.selectTrain();

        step("Fill passenger detail");
        TrainData passengerNamesAndId = trainBookingPage.fillPassengerDetail(adultPassengers,infantPassengers);

        step("Continue to 2nd half");
        trainBookingPage.continueTo2ndHalf();
        TrainData dataFromBookingPage = trainBookingPage.getDetailsTrainAndPassenger(language);
        dataFromBookingPage.adult = adultPassengers;
        dataFromBookingPage.infant = infantPassengers;
        dataFromBookingPage.guestList = passengerNamesAndId.guestList;
        dataFromBookingPage.idNumberList = passengerNamesAndId.idNumberList;

        step("Continue to payment");
        trainBookingPage.continueToPayment();

        step("Check order ID");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",!orderID.isBlank(),is(true));

        step("Open payment method");
        paymentPage.openPaymentMethod();

        step("Select Sakuku as payment method");
        paymentPage.selectPaymentMethod("Sakuku");

        step("Submit payment");
        paymentPage.clickPayButton();
        Assertion.assertThat("Payment Successful and Thank You page is shown", paymentPage.isThankYouPageShown(), is(true));

        step("Open order details page");
        orderListPage.openIssuedTicketViaDeeplink(orderID);
        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.getETicketLabel().toLowerCase(), either(is("your e-ticket is here")).or(is("e-tiketmu di sini")));
    }

    @Author(names = {"Kartik"})
    @Module(name = "onlineCheckIn")
    @Tribe(name = "Train")
    @Vertical(name = "GroundTransport")
    @Test
    public void testUserAbleToOnlineCheckInGuest() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testUserAbleToOnlineCheckInGuest");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TrainSearchPage<WebDriver> trainSearchPage = TrainSearchPage.getInstance(session);
        TrainSearchResultPage<WebDriver> trainSearchResultPage = TrainSearchResultPage.getInstance(session);
        TrainBookingPage<WebDriver> trainBookingPage = TrainBookingPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);

        String originCity = param.get("originCity");
        String destinationCity = param.get("destinationCity");
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        String username = param.get("username");
        String dateRange = param.get("dateRange");
        String tripType = param.get("tripType");
        String departureDate = CoreFunctions.addToLongRandomDate(dateRange);

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Logout via deeplink");
        homePage.logoutViaDeeplink();

        if(Configs.ENVIRONMENT == Environment.PROD) {
            step("Change currency to 'IDR'");
            homePage.changeCurrency("IDR",username);
        }

        step("Get App Language");
        String language = homePage.getLanguageApp();

        step("Open Train Landing Page");
        homePage.openVerticalPageDeeplink("Trains", language, "IDR");

        step("Skip Popup");
        trainSearchPage.skipPopup();

        step("Select origin station");
        trainSearchPage.selectOrigin(originCity);

        step("Select destination station");
        trainSearchPage.selectDestination(destinationCity);

        step("Toggle tripType { " + tripType +" }");
        trainSearchPage.toggleTripType(tripType);

        step("Select date");
        trainSearchPage.selectDate(departureDate);

        step("Add passenger");
        trainSearchPage.addPassenger(adultPassengers,infantPassengers);

        step("Search train");
        trainSearchPage.searchTrain();

        step("Skip Popup");
        trainSearchPage.skipPopup();

        step("Select departure train");
        trainSearchResultPage.selectTrain();

        step("Fill contact data");
        trainBookingPage.fillGuestDetail(CoreFunctions.randomEmail());

        step("Fill passenger detail");
        TrainData passengerNamesAndId = trainBookingPage.fillPassengerDetail(adultPassengers,infantPassengers);

        step("Continue to 2nd half");
        trainBookingPage.continueTo2ndHalf();
        TrainData dataFromBookingPage = trainBookingPage.getDetailsTrainAndPassenger(language);
        dataFromBookingPage.adult = adultPassengers;
        dataFromBookingPage.infant = infantPassengers;
        dataFromBookingPage.guestList = passengerNamesAndId.guestList;
        dataFromBookingPage.idNumberList = passengerNamesAndId.idNumberList;

        step("Continue to payment");
        trainBookingPage.continueToPayment();

        step("Check order ID");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",!orderID.isBlank(),is(true));

        step("Open payment method");
        paymentPage.openPaymentMethod();

        step("Select Sakuku as payment method");
        paymentPage.selectPaymentMethod("Sakuku");

        step("Submit payment");
        paymentPage.clickPayButton();
        Assertion.assertThat("Payment Successful and Thank You page is shown", paymentPage.isThankYouPageShown(), is(true));

        step("Open order details page");
        orderListPage.openIssuedTicketViaDeeplink(orderID);
        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.getETicketLabel().toLowerCase(), either(is("your e-ticket is here")).or(is("e-tiketmu di sini")));
    }

    @DataProvider(name = "testTrainCompleteBookingRoundtripLoginDataProvider")
    public Object[][] testTrainCompleteBookingRoundtripLoginDataProvider() {
        return getObjectParams("testTrainCompleteBookingRoundtripLogin");
    }

    @DataProvider(name = "testTrainCompleteBookingRoundtripGuestDataProvider")
    public Object[][] testTrainCompleteBookingRoundtripGuestDataProvider() {
        return getObjectParams("testTrainCompleteBookingRoundtripGuest");
    }

    @DataProvider(name = "testTrainCompleteBookingOnewayLoginDataProvider")
    public Object[][] testTrainCompleteBookingOnewayLoginDataProvider() {
        return getObjectParams("testTrainCompleteBookingOnewayLogin");
    }

    @DataProvider(name = "testTrainCompleteBookingOnewayInsuranceLoginDataProvider")
    public Object[][] testTrainCompleteBookingOnewayInsuranceLoginDataProvider() {
        return getObjectParams("testTrainCompleteBookingOnewayInsuranceLogin");
    }

    @DataProvider(name = "testTrainCompleteBookingOnewayInsuranceGuestDataProvider")
    public Object[][] testTrainCompleteBookingOnewayInsuranceGuestDataProvider() {
        return getObjectParams("testTrainCompleteBookingOnewayInsuranceGuest");
    }

    @DataProvider(name = "testTrainCompleteBookingRoundtripInsuranceLoginDataProvider")
    public Object[][] testTrainCompleteBookingRoundtripInsuranceLoginDataProvider() {
        return getObjectParams("Train_testTrainCompleteBookingRoundtripInsuranceLogin");
    }

    @DataProvider(name = "testTrainCompleteBookingRoundtripInsuranceGuestDataProvider")
    public Object[][] testTrainCompleteBookingRoundtripInsuranceGuestDataProvider() {
        return getObjectParams("testTrainCompleteBookingRoundtripInsuranceGuest");
    }

    @DataProvider(name = "testTrainCompleteBookingOnewayGuestDataProvider")
    public Object[][] testTrainCompleteBookingOnewayGuestDataProvider() {
        return getObjectParams("testTrainCompleteBookingOnewayGuest");
    }

    @DataProvider(name = "testTrainCheckOrderDetailInMyOrderMenuLoginDataProvider")
    public Object[][] testTrainCheckOrderDetailInMyOrderMenuLoginDataProvider() {
        return getObjectParams("testTrainCheckOrderDetailInMyOrderMenuLogin");
    }

    @DataProvider(name = "testTrainCheckPriceDetailInBookingPageLoginDataProvider")
    public Object[][] testTrainCheckPriceDetailInBookingPageLoginDataProvider() {
        return getObjectParams("testTrainCheckPriceDetailInBookingPageLogin");
    }

    @DataProvider(name = "testTrainCheckRecentlySearchedLoginDataProvider")
    public Object[][] testTrainCheckRecentlySearchedLoginDataProvider() {
        return getObjectParams("Train_testTrainCheckRecentlySearchedLogin");
    }

    @DataProvider(name = "testTrainCheckRecentlySearchedGuestDataProvider")
    public Object[][] testTrainCheckRecentlySearchedGuestDataProvider() {
        return getObjectParams("testTrainCheckRecentlySearchedGuest");
    }

    @DataProvider(name = "testTrainFilterByClassLoginDataProvider")
    public Object[][] testTrainFilterByClassLoginDataProvider() {
        return getObjectParams("testTrainFilterByClassLogin");
    }

    @DataProvider(name = "testTrainFilterByClassGuestDataProvider")
    public Object[][] testTrainFilterByClassGuestDataProvider() {
        return getObjectParams("testTrainFilterByClassGuest");
    }

    @DataProvider(name = "testTrainFilterByNameGuestDataProvider")
    public Object[][] testTrainFilterByNameGuestDataProvider() {
        return getObjectParams("testTrainFilterByNameGuest");
    }

    @DataProvider(name = "testTrainFilterByNameLoginDataProvider")
    public Object[][] testTrainFilterByNameLoginDataProvider() {
        return getObjectParams("testTrainFilterByNameLogin");
    }

    @DataProvider(name = "testTrainCheckDetailPaymentPageLoginDataProvider")
    public Object[][] testTrainCheckDetailPaymentPageLoginDataProvider() {
        return getObjectParams("testTrainCheckDetailPaymentPageLogin");
    }

    @DataProvider(name = "testTrainCheckPriceDetailInBookingPageGuestDataProvider")
    public Object[][] testTrainCheckPriceDetailInBookingPageGuestDataProvider() {
        return getObjectParams("testTrainCheckPriceDetailInBookingPageGuest");
    }

    @DataProvider(name = "testTrainCheckDetailPaymentPageGuestDataProvider")
    public Object[][] testTrainCheckDetailPaymentPageGuestDataProvider() {
        return getObjectParams("testTrainCheckDetailPaymentPageGuest");
    }

    @DataProvider(name = "testTrainCheckOrderDetailInMyOrderMenuGuestDataProvider")
    public Object[][] testTrainCheckOrderDetailInMyOrderMenuGuestDataProvider() {
        return getObjectParams("testTrainCheckOrderDetailInMyOrderMenuGuest");
    }
}