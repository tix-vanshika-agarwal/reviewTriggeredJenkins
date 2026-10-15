package com.tiket.web.test.platform.eiffel.loyalty;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.web.WebTest;
import com.tiket.web.page.common.home.HomePageWeb;
import com.tiket.web.page.common.home.LoginPageWeb;
import com.tiket.web.page.platform.eiffel.loyalty.LoyaltyPointPageWeb;
import com.tiket.web.page.platform.eiffel.membership.AccountPageWeb;
import com.tiket.web.page.platform.payment.PaymentPageWeb;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import javax.security.auth.login.FailedLoginException;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static com.tiket.core.CoreFunctions.*;
import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.blankOrNullString;

public class LoyaltyFullPointsTest extends WebTest {
    @Author(names = {"Yanuar"})
    @Module(name = "FullPoints")
    @Tribe(name = "Loyalty")
    @Vertical(name = "Platform")
    @Test
    public void testLoyaltyPayFullPointsFlightsWeb() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> sheetData = getParam("testLoyaltyPayFullPointsFlightsWeb");
        BaseTest.paramsThreadLocal.set(sheetData);
        HomePageWeb<?> homePageWeb = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPageWeb = LoginPageWeb.getInstance(session);
        LoyaltyPointPageWeb<?> loyaltyPointPageWeb = LoyaltyPointPageWeb.getInstance(session);
        AccountPageWeb<?> accountPageWeb = AccountPageWeb.getInstance(session);
        loyaltyPointPageWeb.setSheetData(sheetData);

        String email = sheetData.get("email");
        String password = sheetData.get("password");
        String otp = sheetData.get("otp");
        String origin = sheetData.get("origin");
        String destination = sheetData.get("destination");
        int departureDateFromNow = Integer.parseInt(sheetData.get("departureDateFromNow"));
        String airline = sheetData.get("airline");

        step("Given user open home page");
        homePageWeb.goToHomePage();

        step("And user login");
        loginPageWeb.loginIfNotWithSameEmail(email,
                password,
                otp,
                1);

        step("When user book a flight until payment page");
        String curr = accountPageWeb.getCurrentCurrency();
        if(!curr.equals("IDR")) {
            accountPageWeb.changeCurrency("IDR");
        }
        homePageWeb.openVerticalPageViaURL("flights");
        loyaltyPointPageWeb.bookFlight(origin, destination, departureDateFromNow, airline, "full");

        step("And user choose payment method using full tix points");

        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);
        paymentPage.isPaymentPageLoaded();

        int currentPoints = loyaltyPointPageWeb.getCurrentLoyaltyPoints();
        int pricePayment = Integer.parseInt(paymentPage.getFinalPrice().split("[,.]")[0]);
        String orderId = paymentPage.getOrderId();

        loyaltyPointPageWeb.payWithTixPoints(currentPoints, pricePayment);

        step("Then user should successfully book a flight and tix points should be deducted");
        loyaltyPointPageWeb.verifyPaymentAndLoyaltyPointsHistory(orderId, pricePayment);
    }

    @Author(names = {"Yanuar"})
    @Module(name = "FullPoints")
    @Tribe(name = "Loyalty")
    @Vertical(name = "Platform")
    @Test
    public void testLoyaltyPayFullPointsHotelsWeb() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> sheetData = getParam("testLoyaltyPayFullPointsHotelsWeb");
        BaseTest.paramsThreadLocal.set(sheetData);
        HomePageWeb<?> homePageWeb = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPageWeb = LoginPageWeb.getInstance(session);
        LoyaltyPointPageWeb<?> loyaltyPointPageWeb = LoyaltyPointPageWeb.getInstance(session);
        AccountPageWeb<?> accountPageWeb = AccountPageWeb.getInstance(session);
        loyaltyPointPageWeb.setSheetData(sheetData);

        String email = sheetData.get("email");
        String password = sheetData.get("password");
        String otp = sheetData.get("otp");
        String hotelName = sheetData.get("hotelName");
        String startDate = generateRandomDate(sheetData.get("rangeAddDate"));
        int adult = Integer.parseInt(sheetData.get("adult"));
        int room = Integer.parseInt(sheetData.get("room"));

        step("Create Hotel Booking via API");
        Map<String, String> orderData = loyaltyPointPageWeb.bookHotelApi(
                email, password, startDate, hotelName, adult, 1, room,
                "IDR","id");
        log("orderData: " + orderData);
        Assertion.assertThat("Verify order via API is successful", orderData.toString(), not(blankOrNullString()));

        String orderId = orderData.get("orderId");
        int totalPrice = Integer.parseInt(orderData.get("totalPrice"));
        String orderHash = orderData.get("orderHash");

        step("Given user open home page");
        homePageWeb.goToHomePage();

        step("And user login");
        loginPageWeb.loginIfNotWithSameEmail(email,
                password,
                otp,
                1);
        String curr = accountPageWeb.getCurrentCurrency();
        if(!curr.equals("IDR")) {
            accountPageWeb.changeCurrency("IDR");
        }

        step("When user go to payment page for the Hotel order");
        loyaltyPointPageWeb.loyaltyPointsChecker(totalPrice, "full");
        loyaltyPointPageWeb.goToPaymentPage(orderId, orderHash);

        step("And user pay using full tix points");
        int currentPoints = loyaltyPointPageWeb.getCurrentLoyaltyPoints();
        loyaltyPointPageWeb.payWithTixPoints(currentPoints, totalPrice);

        step("Then user should successfully book a Hotel and tix points should be deducted");
        loyaltyPointPageWeb.verifyPaymentAndLoyaltyPointsHistory(orderId, totalPrice);
    }

    @Author(names = {"Yanuar"})
    @Module(name = "FullPoints")
    @Tribe(name = "Loyalty")
    @Vertical(name = "Platform")
    @Test
    public void testLoyaltyPayFullPointsCarRentalWeb() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> sheetData = getParam("testLoyaltyPayFullPointsCarRentalWeb");
        BaseTest.paramsThreadLocal.set(sheetData);
        HomePageWeb<?> homePageWeb = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPageWeb = LoginPageWeb.getInstance(session);
        LoyaltyPointPageWeb<?> loyaltyPointPageWeb = LoyaltyPointPageWeb.getInstance(session);
        AccountPageWeb<?> accountPageWeb = AccountPageWeb.getInstance(session);
        loyaltyPointPageWeb.setSheetData(sheetData);

        String email = sheetData.get("email");
        String password = sheetData.get("password");
        String otp = sheetData.get("otp");

        String driverOption = sheetData.get("driverOption");
        String location = sheetData.get("location");
        String startDate = generateRandomDate(sheetData.get("rangeAddDate"));

        String pickupTestData = sheetData.get("pickupTime");
        StringBuilder pickup = new StringBuilder(pickupTestData);
        pickup.setCharAt(2, ':');
        String pickupTime = pickup.toString();

        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String fullName = randomName() + " Autorder";
        String nameTitle = getRandomObject(salutationList);

        int rentalDuration = Integer.parseInt(sheetData.get("rentalDuration"));

        step("Create Car Rental Booking via API");
        Map<String, String> orderData = loyaltyPointPageWeb.bookCarRental(email, randomPhone(), password, driverOption, location,
                startDate, pickupTime, fullName, nameTitle, rentalDuration);
        Assertion.assertThat("Verify order via API is successful", orderData.toString(), not(blankOrNullString()));

        String orderId = orderData.get("orderId");
        int totalPrice = Integer.parseInt(orderData.get("totalPrice"));
        String orderHash = orderData.get("orderHash");

        step("Given user open home page");
        homePageWeb.goToHomePage();

        step("And user login");
        loginPageWeb.loginIfNotWithSameEmail(email,
                password,
                otp,
                1);

        String curr = accountPageWeb.getCurrentCurrency();
        if(!curr.equals("IDR")) {
            accountPageWeb.changeCurrency("IDR");
        }

        step("When user go to payment page for the Car Rental order");
        loyaltyPointPageWeb.loyaltyPointsChecker(totalPrice, "full");
        loyaltyPointPageWeb.goToPaymentPage(orderId, orderHash);

        step("And user pay using full tix points");
        int currentPoints = loyaltyPointPageWeb.getCurrentLoyaltyPoints();
        loyaltyPointPageWeb.payWithTixPoints(currentPoints, totalPrice);

        step("Then user should successfully book a Car Rental and tix points should be deducted");
        loyaltyPointPageWeb.verifyPaymentAndLoyaltyPointsHistory(orderId, totalPrice);
    }

    @Author(names = {"Yanuar"})
    @Module(name = "FullPoints")
    @Tribe(name = "Loyalty")
    @Vertical(name = "Platform")
    @Test
    public void testLoyaltyPayFullPointsTtdWeb() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> sheetData = getParam("testLoyaltyPayFullPointsTtdWeb");
        BaseTest.paramsThreadLocal.set(sheetData);
        HomePageWeb<?> homePageWeb = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPageWeb = LoginPageWeb.getInstance(session);
        LoyaltyPointPageWeb<?> loyaltyPointPageWeb = LoyaltyPointPageWeb.getInstance(session);
        AccountPageWeb<?> accountPageWeb = AccountPageWeb.getInstance(session);
        loyaltyPointPageWeb.setSheetData(sheetData);

        String email = sheetData.get("email");
        String password = sheetData.get("password");
        String otp = sheetData.get("otp");
        int dateRange = Integer.parseInt(sheetData.get("dateRange"));
        int monthRange = Integer.parseInt(sheetData.get("monthRange"));
        String productUrl = sheetData.get("productUrl");
        String productPackages = sheetData.get("productPackages");
        String guestFullName = randomName() + " " + randomName();
        String guestTitle = "Mr";

        step("Prerequisite: User create a TTD order with API");
        Map<String, String> orderResult = loyaltyPointPageWeb.bookTTD(
                email,password,
                productUrl,
                productPackages,
                guestFullName,email,guestTitle,dateRange,monthRange,false);
        Assertion.assertThat("TTD booking via API is successful", orderResult.toString(), not(blankOrNullString()));
        String orderId = orderResult.get("orderId");
        int totalPrice = Integer.parseInt(orderResult.get("totalPrice"));
        String orderHash = orderResult.get("orderHash");

        step("Given user open home page");
        homePageWeb.goToHomePage();

        step("And user login");
        loginPageWeb.loginIfNotWithSameEmail(email,
                password,
                otp,
                1);
        String curr = accountPageWeb.getCurrentCurrency();
        if(!curr.equals("IDR")) {
            accountPageWeb.changeCurrency("IDR");
        }

        step("When user go to payment page for the TTD order");
        loyaltyPointPageWeb.loyaltyPointsChecker(totalPrice, "full");
        loyaltyPointPageWeb.goToPaymentPage(orderId, orderHash);

        step("And user pay using full tix points");
        int currentPoints = loyaltyPointPageWeb.getCurrentLoyaltyPoints();
        loyaltyPointPageWeb.payWithTixPoints(currentPoints, totalPrice);

        step("Then user should successfully book a TTD and tix points should be deducted");
        loyaltyPointPageWeb.verifyPaymentAndLoyaltyPointsHistory(orderId, totalPrice);
    }

    @Author(names = {"Yanuar"})
    @Module(name = "FullPoints")
    @Tribe(name = "Loyalty")
    @Vertical(name = "Platform")
    @Test
    public void testLoyaltyPayFullPointsTrainWeb() throws FailedLoginException, InterruptedException, ExecutionException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> sheetData = getParam("testLoyaltyPayFullPointsTrainWeb");
        BaseTest.paramsThreadLocal.set(sheetData);
        HomePageWeb<?> homePageWeb = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPageWeb = LoginPageWeb.getInstance(session);
        LoyaltyPointPageWeb<?> loyaltyPointPageWeb = LoyaltyPointPageWeb.getInstance(session);
        AccountPageWeb<?> accountPageWeb = AccountPageWeb.getInstance(session);

        String email = sheetData.get("email");
        String password = sheetData.get("password");
        String otp = sheetData.get("otp");
        String name = randomName();
        String phoneNumber = "8991" + randomInt(6);
        String idNumber = String.valueOf(randomInt(16));
        String originCity = sheetData.get("originCity");
        String destinationCity = sheetData.get("destinationCity");
        String tripType = sheetData.get("tripType");
        int adult = Integer.parseInt(sheetData.get("adult"));
        int infant = Integer.parseInt(sheetData.get("infant"));
        String date = generateRandomDate(sheetData.get("rangeAddDate"));
        date = date.replace("-","");

        step("Prerequisite: User create a train order with API");
        Map<String, String> orderResult = loyaltyPointPageWeb.bookTrain(
                email, password, name, phoneNumber, idNumber, originCity, destinationCity, date, tripType, adult, infant);
        Assertion.assertThat("Train booking via API is successful", orderResult.toString(), not(blankOrNullString()));

        String orderId = orderResult.get("orderId");
        int totalPrice = Integer.parseInt(orderResult.get("orderPrice"));
        String orderHash = orderResult.get("orderHash");

        step("Given user open home page");
        homePageWeb.goToHomePage();

        step("And user login");
        loginPageWeb.loginIfNotWithSameEmail(email,
                password,
                otp,
                1);

        String curr = accountPageWeb.getCurrentCurrency();
        if(!curr.equals("IDR")) {
            accountPageWeb.changeCurrency("IDR");
        }

        step("When user go to payment page for train order");
        loyaltyPointPageWeb.loyaltyPointsChecker(totalPrice, "full");
        loyaltyPointPageWeb.goToPaymentPage(orderId, orderHash);

        step("And user pay using full tix points");
        int currentPoints = loyaltyPointPageWeb.getCurrentLoyaltyPoints();
        loyaltyPointPageWeb.payWithTixPoints(currentPoints, totalPrice);

        step("Then user should successfully book a train and tix points should be deducted");
        loyaltyPointPageWeb.verifyPaymentAndLoyaltyPointsHistory(orderId, totalPrice);
    }

    @Author(names = {"Yanuar"})
    @Module(name = "FullPoints")
    @Tribe(name = "Loyalty")
    @Vertical(name = "Platform")
    @Test
    public void testLoyaltyPayFullPointsBusWeb() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> sheetData = getParam("testLoyaltyPayFullPointsBusWeb");
        BaseTest.paramsThreadLocal.set(sheetData);
        HomePageWeb<?> homePageWeb = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPageWeb = LoginPageWeb.getInstance(session);
        LoyaltyPointPageWeb<?> loyaltyPointPageWeb = LoyaltyPointPageWeb.getInstance(session);
        AccountPageWeb<?> accountPageWeb = AccountPageWeb.getInstance(session);

        String email = sheetData.get("email");
        String password = sheetData.get("password");
        String otp = sheetData.get("otp");
        String originId = sheetData.get("originId");
        String destinationId = sheetData.get("destinationId");
        String departureDate = generateRandomDate(sheetData.get("rangeAddDate"));
        String profileId = sheetData.get("profileId");

        step("Prerequisite: User create a bus order with API");
        Map<String, String> busData = loyaltyPointPageWeb.bookBus(email, password, originId, destinationId, departureDate, profileId);
        int totalPrice = Integer.parseInt(busData.get("totalPrice"));
        String orderId = busData.get("orderId");
        String orderHash = busData.get("orderHash");

        Assertion.assertThat("Order ID should not be empty", orderId, not(blankOrNullString()));
        Assertion.assertThat("Order Hash should not be empty", orderHash, not(blankOrNullString()));

        step("Given user open home page");
        homePageWeb.goToHomePage();

        step("And user login");
        loginPageWeb.loginIfNotWithSameEmail(email,
                password,
                otp,
                1);

        String curr = accountPageWeb.getCurrentCurrency();
        if(!curr.equals("IDR")) {
            accountPageWeb.changeCurrency("IDR");
        }

        step("When user go to payment page for train order");
        loyaltyPointPageWeb.loyaltyPointsChecker(totalPrice, "full");
        loyaltyPointPageWeb.goToPaymentPage(orderId, orderHash);

        step("And user pay using full tix points");
        int currentPoints = loyaltyPointPageWeb.getCurrentLoyaltyPoints();
        loyaltyPointPageWeb.payWithTixPoints(currentPoints, totalPrice);
        int pricePayment = Integer.parseInt(loyaltyPointPageWeb.getPriceFromPaymentPage().replaceAll("\\D", ""));
        log("pricePayment: " + pricePayment);
        if(pricePayment != totalPrice) {
            totalPrice = pricePayment;
        }

        step("Then user should successfully book a train and tix points should be deducted");
        loyaltyPointPageWeb.verifyPaymentAndLoyaltyPointsHistory(orderId, totalPrice);
    }

    @Author(names = {"Yanuar"})
    @Module(name = "FullPoints")
    @Tribe(name = "Loyalty")
    @Vertical(name = "Platform")
    @Test
    public void testLoyaltyPayFullPointsFerryWeb() throws FailedLoginException, InterruptedException, ExecutionException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> sheetData = getParam("testLoyaltyPayFullPointsFerryWeb");
        BaseTest.paramsThreadLocal.set(sheetData);
        HomePageWeb<?> homePageWeb = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPageWeb = LoginPageWeb.getInstance(session);
        LoyaltyPointPageWeb<?> loyaltyPointPageWeb = LoyaltyPointPageWeb.getInstance(session);
        AccountPageWeb<?> accountPageWeb = AccountPageWeb.getInstance(session);

        String email = sheetData.get("email");
        String password = sheetData.get("password");
        String otp = sheetData.get("otp");
        String origin = sheetData.get("origin");
        String destination = sheetData.get("destination");
        String vehicleType = sheetData.get("vehicleType");
        int adult = Integer.parseInt(sheetData.get("adult"));
        int child = Integer.parseInt(sheetData.get("child"));
        int infant = Integer.parseInt(sheetData.get("infant"));
        int elderly = Integer.parseInt(sheetData.get("elderly"));
        String departureDate = CoreFunctions.getEndOfCurrentMonth("yyyy-MM-dd");
        String departureTime = sheetData.get("departureTime").replace("-", ":");

        step("Prerequisite: User create a Ferry order with API");
        Map<String, String> ferryData = loyaltyPointPageWeb.bookFerryApi(email, password, origin, destination,
                departureDate, departureTime, vehicleType, adult, child, infant, elderly);
        int totalPrice = Integer.parseInt(ferryData.get("totalPrice"));
        String orderId = ferryData.get("orderId");
        String orderHash = ferryData.get("orderHash");

        Assertion.assertThat("Order ferry is successful", ferryData.toString(), not(blankOrNullString()));

        step("Given user open home page");
        homePageWeb.goToHomePage();

        step("And user login");
        loginPageWeb.loginIfNotWithSameEmail(email,
                password,
                otp,
                1);

        String curr = accountPageWeb.getCurrentCurrency();
        if(!curr.equals("IDR")) {
            accountPageWeb.changeCurrency("IDR");
        }

        step("When user go to payment page for ferry order");
        loyaltyPointPageWeb.loyaltyPointsChecker(totalPrice, "full");
        loyaltyPointPageWeb.goToPaymentPage(orderId, orderHash);

        step("And user pay using full tix points");
        int currentPoints = loyaltyPointPageWeb.getCurrentLoyaltyPoints();
        loyaltyPointPageWeb.payWithTixPoints(currentPoints, totalPrice);
        int pricePayment = Integer.parseInt(loyaltyPointPageWeb.getPriceFromPaymentPage().replaceAll("\\D", ""));
        log("pricePayment: " + pricePayment);
        if(pricePayment != totalPrice) {
            totalPrice = pricePayment;
        }

        step("Then user should successfully book a ferry and tix points should be deducted");
        loyaltyPointPageWeb.verifyPaymentAndLoyaltyPointsHistory(orderId, totalPrice);
    }
}
