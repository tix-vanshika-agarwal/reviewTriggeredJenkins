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
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import javax.security.auth.login.FailedLoginException;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static com.tiket.core.CoreFunctions.generateRandomDate;
import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.Matchers.blankOrNullString;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.not;

public class LoyaltyHalfPointsTest extends WebTest {
    @Author(names = { "Yanuar" })
    @Module(name = "HalfPoints")
    @Tribe(name = "Loyalty")
    @Vertical(name = "Platform")
    @Test
    public void testLoyaltyPayHalfPointsHotelsWeb() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> sheetData = getParam("testLoyaltyPayHalfPointsHotelsWeb");
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
        Map<String, String> orderData = loyaltyPointPageWeb.bookHotelApi(email, password, startDate, hotelName, adult, 1, room, "IDR", "id");
        log("orderData: " + orderData);
        Assertion.assertThat("Verify order via API is successful", orderData.toString(), not(blankOrNullString()));

        String orderId = orderData.get("orderId");
        int totalPrice = Integer.parseInt(orderData.get("totalPrice"));
        String orderHash = orderData.get("orderHash");

        step("Given user open home page");
        homePageWeb.goToHomePage();

        step("Login as " + email + " via session injection");
        loginPageWeb.loginWithSpecificAccountViaCookie(email, password);
        String curr = accountPageWeb.getCurrentCurrency();
        if (!curr.equals("IDR")) {
            accountPageWeb.changeCurrency("IDR");
        }

        step("When user go to payment page for the Hotel order");
        loyaltyPointPageWeb.loyaltyPointsChecker(totalPrice, "half");
        loyaltyPointPageWeb.goToPaymentPage(orderId, orderHash);

        step("And user pay using half tix points");
        int currentPoints = loyaltyPointPageWeb.getCurrentLoyaltyPoints();
        loyaltyPointPageWeb.payWithTixPoints(currentPoints, totalPrice);

        step("Then user should successfully book a Hotel and tix points should be deducted");
        loyaltyPointPageWeb.verifyPaymentAndLoyaltyPointsHistory(orderId, totalPrice);
    }

    @Author(names = { "Yanuar" })
    @Module(name = "HalfPoints")
    @Tribe(name = "Loyalty")
    @Vertical(name = "Platform")
    @Test
    public void testLoyaltyPayHalfPointsFerryWeb() throws FailedLoginException, InterruptedException, ExecutionException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> sheetData = getParam("testLoyaltyPayHalfPointsFerryWeb");
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
        Map<String, String> ferryData = loyaltyPointPageWeb.bookFerryApi(email, password, origin, destination, departureDate, departureTime, vehicleType, adult, child, infant, elderly);
        int totalPrice = Integer.parseInt(ferryData.get("totalPrice"));
        String orderId = ferryData.get("orderId");
        String orderHash = ferryData.get("orderHash");

        Assertion.assertThat("Order ferry is successful", ferryData.toString(), not(blankOrNullString()));

        step("Given user open home page");
        homePageWeb.goToHomePage();

        step("And user login");
        loginPageWeb.loginIfNotWithSameEmail(email, password, otp, 1);

        String curr = accountPageWeb.getCurrentCurrency();
        if (!curr.equals("IDR")) {
            accountPageWeb.changeCurrency("IDR");
        }

        step("When user go to payment page for ferry order");
        loyaltyPointPageWeb.loyaltyPointsChecker(totalPrice, "half");
        loyaltyPointPageWeb.goToPaymentPage(orderId, orderHash);

        step("And user pay using half tix points");
        int currentPoints = loyaltyPointPageWeb.getCurrentLoyaltyPoints();
        loyaltyPointPageWeb.payWithTixPoints(currentPoints, totalPrice);
        int pricePayment = Integer.parseInt(loyaltyPointPageWeb.getPriceFromPaymentPage().replaceAll("\\D", ""));
        log("pricePayment: " + pricePayment);
        if (pricePayment != totalPrice) {
            totalPrice = pricePayment;
        }

        step("Then user should successfully book a ferry and tix points should be deducted");
        loyaltyPointPageWeb.verifyPaymentAndLoyaltyPointsHistory(orderId, totalPrice);
    }

    @Author(names = {"Mridul"})
    @Module(name = "HalfPoints")
    @Tribe(name = "Loyalty")
    @Vertical(name = "Platform")
    @Test
    public void testLoyaltyCappingPointsTtdWeb() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> sheetData = getParam("testLoyaltyCappingPointsTtdWeb");
        BaseTest.paramsThreadLocal.set(sheetData);
        HomePageWeb<?> homePageWeb = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPageWeb = LoginPageWeb.getInstance(session);
        LoyaltyPointPageWeb<?> loyaltyPointPageWeb = LoyaltyPointPageWeb.getInstance(session);
        AccountPageWeb<?> accountPageWeb = AccountPageWeb.getInstance(session);
        loyaltyPointPageWeb.setSheetData(sheetData);

        String email = sheetData.get("email");
        String password = sheetData.get("password");
        String productUrl = sheetData.get("productUrl");
        String productPackages = sheetData.get("productPackages");
        int dateRange = Integer.parseInt(sheetData.getOrDefault("dateRange", "0"));
        int monthRange = Integer.parseInt(sheetData.getOrDefault("monthRange", "1"));
        int pointsCapPercentage = Integer.parseInt(sheetData.getOrDefault("pointsCapPercentage", "50"));
        String paymentMethodGroup = sheetData.getOrDefault("paymentMethodGroup", "Instant Payment");
        String paymentMethod = sheetData.getOrDefault("paymentMethod", "Sakuku");
        String guestFullName = CoreFunctions.randomName() + " " + CoreFunctions.randomName();
        String guestTitle = "Mr";

        step("Given user creates a Todo order via API for product " + productUrl);
        Map<String, String> orderData = loyaltyPointPageWeb.bookTTD(
                email, password,
                productUrl, productPackages,
                guestFullName, email, guestTitle,
                dateRange, monthRange, false);
        Assertion.assertThat("Todo booking via API returned an order id",
                orderData.get("orderId"), not(blankOrNullString()));

        String orderId = orderData.get("orderId");
        int totalPrice = Integer.parseInt(orderData.get("totalPrice"));
        String orderHash = orderData.get("orderHash");

        step("And user open home page");
        homePageWeb.goToHomePage();

        step("And user login on dWeb using " + email);
        loginPageWeb.loginWithSpecificAccountViaCookie(email, password);

        String curr = accountPageWeb.getCurrentCurrency();
        if (!curr.equals("IDR")) {
            accountPageWeb.changeCurrency("IDR");
        }

        step("And user has tiket points higher than total Todo price");
        loyaltyPointPageWeb.ensurePointsExceedPrice(totalPrice);
        int currentPoints = loyaltyPointPageWeb.getCurrentLoyaltyPoints();
        Assertion.assertThat("User tiket points should be higher than total Todo price",
                currentPoints, greaterThan(totalPrice));

        step("When user go to payment page for the Todo order");
        loyaltyPointPageWeb.goToPaymentPage(orderId, orderHash);

        step("Then user should not be able to use full tiket points on payment page");
        step("And only " + pointsCapPercentage + "% of the price can be paid by tiket points");
        step("And user should finish payment with capped points plus another payment method");
        loyaltyPointPageWeb.payWithTixPointsCapped(totalPrice, pointsCapPercentage, paymentMethodGroup, paymentMethod);

        step("Verify Todo order is paid and capped tiket points are deducted");
        int pricePayment = Integer.parseInt(loyaltyPointPageWeb.getPriceFromPaymentPage().replaceAll("\\D", ""));
        if (pricePayment != totalPrice) {
            totalPrice = pricePayment;
        }
        loyaltyPointPageWeb.verifyPaymentAndLoyaltyPointsHistory(orderId, totalPrice);
    }

    @Author(names = {"Mridul"})
    @Module(name = "HalfPoints")
    @Tribe(name = "Loyalty")
    @Vertical(name = "Platform")
    @Test
    public void testLoyaltyCappingPointsTrainWeb() throws FailedLoginException, InterruptedException, ExecutionException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> sheetData = getParam("testLoyaltyCappingPointsTrainWeb");
        BaseTest.paramsThreadLocal.set(sheetData);
        HomePageWeb<?> homePageWeb = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPageWeb = LoginPageWeb.getInstance(session);
        LoyaltyPointPageWeb<?> loyaltyPointPageWeb = LoyaltyPointPageWeb.getInstance(session);
        AccountPageWeb<?> accountPageWeb = AccountPageWeb.getInstance(session);
        loyaltyPointPageWeb.setSheetData(sheetData);

        String email = sheetData.get("email");
        String password = sheetData.get("password");
        String originStation = sheetData.getOrDefault("originStation", "BD");
        String destinationStation = sheetData.getOrDefault("destinationStation", "GMR");
        String tripType = sheetData.getOrDefault("tripType", "oneway");
        int adultCount = Integer.parseInt(sheetData.getOrDefault("adultCount", "1"));
        int infantCount = Integer.parseInt(sheetData.getOrDefault("infantCount", "0"));
        int pointsCapPercentage = Integer.parseInt(sheetData.getOrDefault("pointsCapPercentage", "50"));
        String paymentMethodGroup = sheetData.getOrDefault("paymentMethodGroup", "Instant Payment");
        String paymentMethod = sheetData.getOrDefault("paymentMethod", "Sakuku");

        step("Given user create a Train order via API from " + originStation + " to " + destinationStation);
        String passengerName = CoreFunctions.randomName();
        String passengerPhone = "8991" + CoreFunctions.randomInt(6);
        String passengerId = String.valueOf(CoreFunctions.randomInt(16));
        String departureDate = generateRandomDate("3-14").replace("-", "");
        Map<String, String> orderData = loyaltyPointPageWeb.bookTrain(
                email, password, passengerName, passengerPhone, passengerId,
                originStation, destinationStation, departureDate, tripType, adultCount, infantCount);
        Assertion.assertThat("Train booking via API returned an order id",
                orderData.get("orderId"), not(blankOrNullString()));

        String orderId = orderData.get("orderId");
        int totalPrice = Integer.parseInt(orderData.get("orderPrice"));
        String orderHash = orderData.get("orderHash");

        step("And user open home page");
        homePageWeb.goToHomePage();

        step("And user login on dWeb using " + email);
        loginPageWeb.loginWithSpecificAccountViaCookie(email, password);

        String curr = accountPageWeb.getCurrentCurrency();
        if (!curr.equals("IDR")) {
            accountPageWeb.changeCurrency("IDR");
        }

        step("And user has tiket points higher than total Train price");
        loyaltyPointPageWeb.ensurePointsExceedPrice(totalPrice);
        int currentPoints = loyaltyPointPageWeb.getCurrentLoyaltyPoints();
        Assertion.assertThat("User tiket points should be higher than total Train price",
                currentPoints, greaterThan(totalPrice));

        step("When user go to payment page for the Train order");
        loyaltyPointPageWeb.goToPaymentPage(orderId, orderHash);

        step("Then user should not be able to use full tiket points on payment page");
        step("And only " + pointsCapPercentage + "% of the price can be paid by tiket points");
        step("And user should finish payment with capped points plus another payment method");
        loyaltyPointPageWeb.payWithTixPointsCapped(totalPrice, pointsCapPercentage, paymentMethodGroup, paymentMethod);

        step("Verify Train order is paid and capped tiket points are deducted");
        int pricePayment = Integer.parseInt(loyaltyPointPageWeb.getPriceFromPaymentPage().replaceAll("\\D", ""));
        if (pricePayment != totalPrice) {
            totalPrice = pricePayment;
        }
        loyaltyPointPageWeb.verifyPaymentAndLoyaltyPointsHistory(orderId, totalPrice);
    }
}
