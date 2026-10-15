package com.tiket.test.flight;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.apiPage.FlightApi;
import com.tiket.apiPage.LoyaltyApi;
import com.tiket.apiPage.Unm;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Environment;
import com.tiket.model.Platform;
import com.tiket.model.Session;
import com.tiket.page.account.AccountPage;
import com.tiket.page.accountcenter.AccountCenterPage;
import com.tiket.page.corporate.login.CorporateLoginPage;
import com.tiket.page.flight.FlightPage;
import com.tiket.page.flight.bookingform.FlightBookingFormPage;
import com.tiket.page.flight.generaltravelinformationpage.FlightGeneralTravelInformationPage;
import com.tiket.page.flight.lpgPage.LpgPage;
import com.tiket.page.flight.pdp.FlightDescriptionPage;
import com.tiket.page.flight.prp.FlightProductReviewPage;
import com.tiket.page.flight.searchpage.FlightSearchPage;
import com.tiket.page.flight.searchresultpage.FlightSearchResultPage;
import com.tiket.page.home.HomePage;
import com.tiket.page.login.LoginPage;
import com.tiket.page.order.orderdetail.OrderDetailPage;
import com.tiket.page.order.orderlist.OrderListPage;
import com.tiket.page.payment.PaymentPage;
import com.tiket.pojo.flight.FlightDynamicData;
import com.tiket.pojo.flight.FlightMealsDetail;
import com.tiket.pojo.flight.PriceDetail;
import com.tiket.pojo.flight.PromoData;
import com.tiket.pojo.member.RegisterNewAccountData;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.testbase.Configs;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static com.tiket.testdata.TestData.getObjectParams;
import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;

public class FlightTest extends BaseTest {

    @Author(names = {"Ashish"})
    @Module(name = "FlightDomesticOneway")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test(dataProvider="testFlightDomesticOnewayDirectLoginDataProvider")
    public void testFlightDomesticOnewayDirectLogin(Map<String, String> param) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightProductReviewPage<WebDriver> flightProductReviewPage = FlightProductReviewPage.getInstance(session);
        FlightBookingFormPage<WebDriver> flightBookingFormPage = FlightBookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        FlightApi<?> flightApi = new FlightApi<>(session);

        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        String airline = param.get("airline");

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        int baggageAmount = Integer.parseInt(param.get("baggage"));
        String optionalLocation = param.get("optionalLocation");
        String optionalAirline = param.get("optionalAirline");
        String currency = param.get("currency");

        step("Find dynamic data");
        FlightDynamicData flightDynamicData = flightApi.findDate(
                username,password,
                originAirport,destinationAirport,daysToDepartureDate,
                adultPassengers,childPassengers,infantPassengers,
                tripType,airline,"baggage",optionalLocation,optionalAirline,currency
        );
        originAirport = flightDynamicData.originAirport;
        destinationAirport = flightDynamicData.destinationAirport;
        String departureDate = CoreFunctions.convertToLongDate(flightDynamicData.departureDate);
        airline = flightDynamicData.airlineDepart;

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink (if needed) and change currency to " + currency);
        homePage.loginIfLoggedOutAndChangeCurrencyViaDeeplink(username, password, currency);

        step("Open flights page");
        homePage.openVerticalPageDeeplink("Flights","",currency);
        flightSearchPage.closePopup();

        step("change date using deeplink");
        flightSearchPage.changeDateViaDeeplink(flightDynamicData, tripType);

        step("Toggle trip type { " + tripType + " }");
        flightSearchPage.toggleTriptype(tripType);

        step("Select origin airport");
        flightSearchPage.selectOrigin(originAirport);
        flightSearchPage.closePopup();

        step("Select destination airport");
        flightSearchPage.selectDestination(destinationAirport);

        step("Select date");
        flightSearchPage.selectDate(departureDate);

        step("Add passenger");
        flightSearchPage.addPassenger(adultPassengers,childPassengers,infantPassengers);

        step("Search flight");
        flightSearchPage.searchFlight();

        step("[STEP ADDED] Minimize app while on SRP, open Google Chrome and validate we are in Chrome, then reopen tiket.com");
        Assertion.assertThat("Google Chrome opened in foreground while app was minimized", flightSearchResultPage.minimizeOpenChromeAndReopenApp(), is(true));

        step("[STEP ADDED] Verify SRP is shown without any error bottomsheet after reopening the app");
        Assertion.assertThat("Search result page is shown after reopening the app", flightSearchResultPage.isOnSearchResultPage(), is(true));
        Assertion.assertThat("No error bottomsheet is shown after reopening the app", flightSearchResultPage.isErrorBottomSheetDisplayed(), is(false));

        step("Open filter");
        flightSearchResultPage.openFilter();

        step("Filter airline");
        flightSearchResultPage.filterAirline(airline);

        step("Save filter");
        flightSearchResultPage.saveFilter();

        step("Click flight detail chevron");
        flightSearchResultPage.selectFlight();

        step("Continue to booking form");
        flightProductReviewPage.continueToBookingForm();

        step("Skip full protection if visible");
        flightBookingFormPage.skipFullProtectionPrp();

        step("verify date format");
        flightBookingFormPage.verifyDateFormat(tripType);

        step("Fill passenger detail");
        flightBookingFormPage.fillPassengerDetail(adultPassengers,childPassengers,infantPassengers, "domestic",airline);

        step("Add baggage");
        baggageAmount = flightBookingFormPage.addBaggage();
        String baggagePrice = flightBookingFormPage.getBaggagePrice(baggageAmount);
        flightBookingFormPage.saveAddBaggage();

        step("Continue to second half booking form");
        flightBookingFormPage.continueToSecondHalf();

        step("Open price detail");
        flightBookingFormPage.openPriceDetail();

        step("Verify price detail");
        PriceDetail priceDetail = new PriceDetail();

        priceDetail.tripType=tripType;
        priceDetail.adult=adultPassengers;
        priceDetail.child=childPassengers;
        priceDetail.infant=infantPassengers;
        priceDetail.baggageDepartAmount=baggageAmount;
        priceDetail.baggageDepartPrice=baggagePrice;

        Assertion.assertThat("Is price breakdown correct", flightBookingFormPage.isPriceBreakdownCorrect(priceDetail,currency), is(true));

        step("Continue to payment");
        flightBookingFormPage.continueToPayment();

        step("Skip full protection");
        flightBookingFormPage.skipFullProtection();
        flightBookingFormPage.baggageAllowancePopup();

        step("Check order ID");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",!orderID.isBlank(),is(true));
        Assertion.assertThat("User is directed to payment page", paymentPage.isOnPaymentPage(), is(true));

        step("[STEP ADDED] Select payment method for Downpay, open Downpay option, verify bottomsheet, close it, then verify Pay X% upfront button amounts");
        if (Configs.ENVIRONMENT == Environment.PROD) {
            paymentPage.selectPaymentMethodFromTileOrDrawer("Credit Card");
        } else {
            paymentPage.selectSakukuPaymentMethod();
        }
        Assertion.assertThat("Downpay option banner is shown", paymentPage.isDownPayOptionDisplayed(), is(true));
        paymentPage.openDownPayOption();
        Assertion.assertThat("Downpay information bottomsheet is shown", paymentPage.isDownPayInfoBottomSheetDisplayed(), is(true));
        PaymentPage.DownPayBottomSheetAmounts downPaySheet = paymentPage.getDownPayBottomSheetAmounts();
        paymentPage.closeDownPayInfoBottomSheet();
        Assertion.assertThat("Pay X% upfront Downpay button is shown and enabled", paymentPage.isPayUpfrontDownPayButtonDisplayed(), is(true));

        int downPayPercent = paymentPage.getDownPayButtonPercent();
        double downPayButtonAmount = paymentPage.getDownPayButtonAmount();
        Assertion.assertThat("Downpay percent on button is > 0", downPayPercent, greaterThan(0));
        Assertion.assertThat("Downpay button amount is > 0", downPayButtonAmount, greaterThan(0.0));
        Assertion.assertThat("Downpay bottomsheet today amount is > 0", downPaySheet.todayAmount, greaterThan(0.0));
        Assertion.assertThat("Payment total price is > 0", downPaySheet.paymentTotal, greaterThan(0.0));
    }

    @Author(names = {"Ashish"})
    @Module(name = "FlightDomesticRoundTrip")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test(dataProvider="testFlightDomesticRoundTripLoginAddOnMealsDataProvider")
    public void testFlightDomesticRoundTripLoginAddOnMeals(Map<String, String> param) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightProductReviewPage<WebDriver> flightProductReviewPage = FlightProductReviewPage.getInstance(session);
        FlightBookingFormPage<WebDriver> flightBookingFormPage = FlightBookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        FlightApi<?> flightApi = new FlightApi<>(session);

        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int duration = Integer.parseInt(param.get("duration"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        String airline = param.get("airline");
        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String optionalLocation = param.get("optionalLocation");
        String optionalAirline = param.get("optionalAirline");

        String currency = param.get("currency");

        step("Find dynamic data");
        FlightDynamicData flightDynamicData = flightApi.findDateRT(
                username,password,
                originAirport,destinationAirport,daysToDepartureDate,duration,
                adultPassengers,childPassengers,infantPassengers,
                tripType,airline,airline,"","meal",optionalLocation,optionalAirline,currency
        );
        String departureDate = CoreFunctions.convertToLongDate(flightDynamicData.departureDate);
        String returnDate = CoreFunctions.convertToLongDate(flightDynamicData.returnDate);
        airline = flightDynamicData.airlineDepart;
        String airlineReturn = flightDynamicData.airlineReturn;
        originAirport = flightDynamicData.originAirport;
        destinationAirport = flightDynamicData.destinationAirport;

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink (if needed) and change currency to " + currency);
        homePage.loginIfLoggedOutAndChangeCurrencyViaDeeplink(username, password, currency);

        step("Open flights page");
        homePage.openVerticalPageDeeplink("Flights","",currency);
        flightSearchPage.closePopup();

        step("change date using deeplink");
        flightSearchPage.changeDateViaDeeplink(flightDynamicData, tripType);

        step("Toggle trip type { " + tripType + " }");
        flightSearchPage.toggleTriptype(tripType);

        step("Select origin airport");
        flightSearchPage.selectOrigin(originAirport);
        flightSearchPage.closePopup();

        step("Select destination airport");
        flightSearchPage.selectDestination(destinationAirport);

        step("Select date");
        flightSearchPage.selectDate(departureDate,returnDate);

        step("Add passenger");
        flightSearchPage.addPassenger(adultPassengers,childPassengers,infantPassengers);

        step("Search flight");
        flightSearchPage.searchFlight();

        step("Open filter");
        flightSearchResultPage.openFilter();

        step("Filter airline");
        flightSearchResultPage.filterAirline(airline);

        step("Save filter");
        flightSearchResultPage.saveFilter();

        step("Click flight detail chevron");
        flightSearchResultPage.selectFlight();

        step("Open filter");
        flightSearchResultPage.openFilter();

        step("Filter airline");
        flightSearchResultPage.filterAirline(airlineReturn);

        step("Save filter");
        flightSearchResultPage.saveFilter();

        step("Click flight detail chevron");
        flightSearchResultPage.selectFlight();

        step("Continue to booking form");
        flightProductReviewPage.continueToBookingForm(tripType);

        step("Skip full protection if visible");
        flightBookingFormPage.skipFullProtectionPrp();

        step("verify date format");
        flightBookingFormPage.verifyDateFormat(tripType);

        step("Fill passenger detail");
        flightBookingFormPage.fillPassengerDetail(adultPassengers,childPassengers,infantPassengers,"domestic",airline);

        step("Continue to second half booking form");
        flightBookingFormPage.continueToSecondHalf();

        step("[STEP ADDED] Add Meals add-on for return only, up to 2 meals per passenger per segment");
        FlightMealsDetail flightMealsDetail = flightBookingFormPage.addMealsReturnOnly(adultPassengers, childPassengers, currency, 2);
        Assertion.assertThat("Meals price is not 0", CoreFunctions.convertPrice(flightMealsDetail.mealPriceReturn), greaterThan(0.0));

        step("Open price detail via chevron on price section");
        flightBookingFormPage.openPriceDetail();

        step("Verify Price Detail page contains breakdown including total price, then close back to Booking Form");
        PriceDetail priceDetail = new PriceDetail();
        priceDetail.tripType = tripType;
        priceDetail.adult = adultPassengers;
        priceDetail.child = childPassengers;
        priceDetail.infant = infantPassengers;
        // Return-only meals appear as a single Meals row in price detail
        priceDetail.mealPrice = flightMealsDetail.mealPriceReturn;
        Assertion.assertThat("Is price breakdown correct", flightBookingFormPage.isPriceBreakdownCorrect(priceDetail, currency), is(true));

        step("Continue to payment");
        flightBookingFormPage.continueToPayment();

        step("Skip full protection");
        flightBookingFormPage.skipFullProtection();
        flightBookingFormPage.baggageAllowancePopup();

        step("[SUCCESS CRITERIA] User redirected to payment detail page and meals price is not 0");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id", !orderID.isBlank(), is(true));
        Assertion.assertThat("Meals price is not 0", CoreFunctions.convertPrice(flightMealsDetail.mealPriceReturn), greaterThan(0.0));

        step("Open price detail on payment page and verify meals price matches booking form");
        paymentPage.openPriceDetail();
        double bookingFormMealPrice = CoreFunctions.convertPrice(flightMealsDetail.mealPriceReturn);
        Assertion.assertThat("Meals price on payment price detail matches booking form",
                paymentPage.getMealPriceAmountFromPriceDetail(currency), closeTo(bookingFormMealPrice, 1.0));
        paymentPage.closePriceDetail();
    }

    @Author(names = {"Zia"})
    @Module(name = "flightInternationalSmartTrip")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test(dataProvider="testFlightInternationalSmartTripDirectGuestDataProvider")
    public void testFlightInternationalSmartTripDirectGuest(Map<String, String> param) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightProductReviewPage<WebDriver> flightProductReviewPage = FlightProductReviewPage.getInstance(session);
        FlightBookingFormPage<WebDriver> flightBookingFormPage = FlightBookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        FlightApi<?> flightApi = new FlightApi<>(session);

        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");
        int index = Integer.parseInt(param.get("index"));
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        int duration = Integer.parseInt(param.get("duration"));
        String optionalLocation = param.get("optionalLocation");
        String optionalAirline = param.get("optionalAirline");

        String email = param.get("email");
        String phoneNumber = param.get("phoneNumber");

        String airline = param.get("airline");
        String otherFilter = param.get("otherFilter");

        String currency = param.get("currency");

        step("Find dynamic data");
        FlightDynamicData flightDynamicData = flightApi.findDateRT(
                "","",
                originAirport,destinationAirport,daysToDepartureDate,duration,
                adultPassengers,childPassengers,infantPassengers,
                tripType,"","",otherFilter,otherFilter,optionalLocation,optionalAirline,currency
        );
        originAirport = flightDynamicData.originAirport;
        destinationAirport = flightDynamicData.destinationAirport;
        String departureDate = CoreFunctions.convertToLongDate(flightDynamicData.departureDate);
        String returnDate = CoreFunctions.convertToLongDate(flightDynamicData.returnDate);
        airline = flightDynamicData.airlineDepart;

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Logout via deeplink");
        homePage.logoutViaDeeplink();

        step("Change currency");
        homePage.changeCurrency(currency,"");

        step("Open flights page");
        homePage.openVerticalPageDeeplink("Flights","",currency);
        flightSearchPage.closePopup();

        step("change date using deeplink");
        flightSearchPage.changeDateViaDeeplink(flightDynamicData, tripType);

        step("Toggle trip type { " + tripType + " }");
        flightSearchPage.toggleTriptype(tripType);

        step("Select origin airport");
        flightSearchPage.selectOrigin(originAirport);
        flightSearchPage.closePopup();

        step("Select destination airport");
        flightSearchPage.selectDestination(destinationAirport);

        step("Select departure date");
        flightSearchPage.selectDate(departureDate,returnDate);

        step("Add passenger");
        flightSearchPage.addPassenger(adultPassengers,childPassengers,infantPassengers);

        step("Search flight");
        flightSearchPage.searchFlight();

        if(Configs.ENVIRONMENT == Environment.GK || Configs.ENVIRONMENT == Environment.PREPROD) {
            step("Open filter");
            flightSearchResultPage.openFilter();

            step("Filter airline");
            flightSearchResultPage.filterAirline(airline);

            step("Save filter");
            flightSearchResultPage.saveFilter();
        }else {
            step("filter round-trip deals");
            flightSearchResultPage.filterChips(otherFilter);
        }

        step("Click flight detail chevron");
        flightSearchResultPage.selectFlight();

        if(Configs.ENVIRONMENT == Environment.GK || Configs.ENVIRONMENT == Environment.PREPROD) {
            step("Open filter");
            flightSearchResultPage.openFilter();

            step("Filter airline");
            flightSearchResultPage.filterAirline(airline);

            step("Save filter");
            flightSearchResultPage.saveFilter();
        }else {
            step("filter round-trip deals");
            flightSearchResultPage.filterChips(otherFilter);
        }

        step("Click flight detail chevron");
        flightSearchResultPage.selectFlight();

        step("Continue to booking form");
        flightProductReviewPage.continueToBookingForm(tripType);

        step("Skip full protection if visible");
        flightBookingFormPage.skipFullProtectionPrp();

        step("verify date format");
        flightBookingFormPage.verifyDateFormat(tripType);

        step("Fill contact detail");
        flightBookingFormPage.fillContactDetail(phoneNumber,email,"guest",airline);

        step("Fill passenger detail");
        flightBookingFormPage.fillPassengerDetail(adultPassengers,childPassengers,infantPassengers,"international",airline);

        step("Continue to second half booking form");
        flightBookingFormPage.continueToSecondHalf();

        step("Add Extra Protection");
        ArrayList<String> extraProtection = flightBookingFormPage.addExtraProtection(index);

        step("Open price detail");
        flightBookingFormPage.openPriceDetail();

        step("Verify price detail");
        PriceDetail priceDetail = new PriceDetail();
        priceDetail.tripType = tripType;
        priceDetail.adult = adultPassengers;
        priceDetail.child = childPassengers;
        priceDetail.infant = infantPassengers;
        priceDetail.extraProtectionTitle=extraProtection.get(0) + " extra protection";
        priceDetail.extraProtectionPrice=extraProtection.get(1);

        Assertion.assertThat("Is price breakdown correct", flightBookingFormPage.isPriceBreakdownCorrect(priceDetail,currency), is(true));

        step("Continue to payment");
        flightBookingFormPage.continueToPayment();

        step("Skip full protection");
        flightBookingFormPage.skipFullProtection();
        flightBookingFormPage.baggageAllowancePopup();

        step("Check order ID");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",!orderID.isBlank(),is(true));
    }

    @Author(names = {"Ashish"})
    @Module(name = "flightInternationalSmartTrip")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test(dataProvider="testFlightInternationalSmartTripDirectLoginDataProvider")
    public void testFlightInternationalSmartTripDirectLogin(Map<String, String> param) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightProductReviewPage<WebDriver> flightProductReviewPage = FlightProductReviewPage.getInstance(session);
        FlightBookingFormPage<WebDriver> flightBookingFormPage = FlightBookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        FlightApi<?> flightApi = new FlightApi<>(session);

        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");
        String optionalLocation = param.get("optionalLocation");
        String optionalAirline = param.get("optionalAirline");
        int index = Integer.parseInt(param.get("index"));
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        int duration = Integer.parseInt(param.get("duration"));

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");

        String airline = param.get("airline");
        String otherFilter = param.get("otherFilter");

        String currency = param.get("currency");

        step("Find dynamic data");
        FlightDynamicData flightDynamicData = flightApi.findDateRT(
                username,password,
                originAirport,destinationAirport,daysToDepartureDate,duration,
                adultPassengers,childPassengers,infantPassengers,
                tripType,"","",otherFilter,otherFilter,optionalLocation,optionalAirline,currency
        );
        originAirport = flightDynamicData.originAirport;
        destinationAirport = flightDynamicData.destinationAirport;
        String departureDate = CoreFunctions.convertToLongDate(flightDynamicData.departureDate);
        String returnDate = CoreFunctions.convertToLongDate(flightDynamicData.returnDate);
        airline = flightDynamicData.airlineDepart;
        String airlineReturn = flightDynamicData.airlineReturn;

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink (if needed) and change currency to " + currency);
        homePage.loginIfLoggedOutAndChangeCurrencyViaDeeplink(username, password, currency);

        step("Open flights page");
        homePage.openVerticalPageDeeplink("Flights","",currency);
        flightSearchPage.closePopup();

        step("change date using deeplink");
        flightSearchPage.changeDateViaDeeplink(flightDynamicData, tripType);

        step("Toggle trip type { " + tripType + " }");
        flightSearchPage.toggleTriptype(tripType);

        step("Select origin airport");
        flightSearchPage.selectOrigin(originAirport);
        flightSearchPage.closePopup();

        step("Select destination airport");
        flightSearchPage.selectDestination(destinationAirport);

        step("Select departure date");
        flightSearchPage.selectDate(departureDate,returnDate);

        step("Add passenger");
        flightSearchPage.addPassenger(adultPassengers,childPassengers,infantPassengers);

        step("Search flight");
        flightSearchPage.searchFlight();
        if(Configs.ENVIRONMENT == Environment.GK || Configs.ENVIRONMENT == Environment.PREPROD) {
            step("Open filter");
            flightSearchResultPage.openFilter();

            step("Filter airline");
            flightSearchResultPage.filterAirline(airline);

            step("Save filter");
            flightSearchResultPage.saveFilter();
        }else {
            step("filter round-trip deals");
            flightSearchResultPage.filterChips(otherFilter);
        }

        step("Click flight detail chevron");
        flightSearchResultPage.selectFlight();

        if(Configs.ENVIRONMENT == Environment.GK || Configs.ENVIRONMENT == Environment.PREPROD) {
            step("Open filter");
            flightSearchResultPage.openFilter();

            step("Filter airline");
            flightSearchResultPage.filterAirline(airlineReturn);

            step("Save filter");
            flightSearchResultPage.saveFilter();
        }else {
            step("filter round-trip deals");
            flightSearchResultPage.filterChips(otherFilter);
        }

        step("Click flight detail chevron");
        flightSearchResultPage.selectFlight();

        step("Continue to booking form");
        flightProductReviewPage.continueToBookingForm(tripType);

        step("Skip full protection if visible");
        flightBookingFormPage.skipFullProtectionPrp();

        step("verify date format");
        flightBookingFormPage.verifyDateFormat(tripType);

        step("Fill passenger detail");
        flightBookingFormPage.fillPassengerDetail(adultPassengers,childPassengers,infantPassengers,"international",airline);

        step("Continue to second half booking form");
        flightBookingFormPage.continueToSecondHalf();

        step("Add Extra Protection");
        ArrayList<String> extraProtection = flightBookingFormPage.addExtraProtection(index);

        step("Open price detail");
        flightBookingFormPage.openPriceDetail();

        step("Verify price detail");
        PriceDetail priceDetail = new PriceDetail();
        priceDetail.tripType = tripType;
        priceDetail.adult = adultPassengers;
        priceDetail.child = childPassengers;
        priceDetail.infant = infantPassengers;
        priceDetail.extraProtectionTitle=extraProtection.get(0) + " extra protection";
        priceDetail.extraProtectionPrice=extraProtection.get(1);

        Assertion.assertThat("Is price breakdown correct", flightBookingFormPage.isPriceBreakdownCorrect(priceDetail,currency), is(true));

        step("Continue to payment");
        flightBookingFormPage.continueToPayment();

        step("Skip full protection");
        flightBookingFormPage.skipFullProtection();
        flightBookingFormPage.baggageAllowancePopup();

        step("Check order ID");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",!orderID.isBlank(),is(true));
    }

    @Author(names = {"Zia"})
    @Module(name = "flightInternationalRoundtrip")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test(dataProvider="testFlightInternationalRoundtrip2BookingCodeGuestDataProvider")
    public void testFlightInternationalRoundtrip2BookingCodeGuest(Map<String, String> param) {
        //obsolete
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightProductReviewPage<WebDriver> flightProductReviewPage = FlightProductReviewPage.getInstance(session);
        FlightBookingFormPage<WebDriver> flightBookingFormPage = FlightBookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        FlightApi<?> flightApi = new FlightApi<>(session);

        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");
        String optionalLocation = param.get("optionalLocation");
        String optionalAirline = param.get("optionalAirline");

        String email = param.get("email");
        String phoneNumber = param.get("phoneNumber");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        int duration = Integer.parseInt(param.get("duration"));
        String otherFilter = param.get("otherFilter");

        String airline = CoreFunctions.convertSquareBracketToRound(param.get("airline"));
        String airlineReturn = param.get("airlineReturn");

        String currency = param.get("currency");

        step("Find dynamic data");
        FlightDynamicData flightDynamicData = flightApi.findDateRT(
                "","",
                originAirport,destinationAirport,daysToDepartureDate,duration,
                adultPassengers,childPassengers,infantPassengers,
                tripType,airline,airlineReturn,otherFilter,otherFilter,"","",currency
        );
        originAirport = flightDynamicData.originAirport;
        destinationAirport = flightDynamicData.destinationAirport;
        String departureDate = CoreFunctions.convertToLongDate(flightDynamicData.departureDate);
        String returnDate = CoreFunctions.convertToLongDate(flightDynamicData.returnDate);
        airline = flightDynamicData.airlineDepart;

        step("Wait for app to launch");
        homePage.gotoHome();

        step("login as guest");
        loginPage.loginAsGuest();

        step("Change currency");
        homePage.changeCurrency(currency,"");

        step("Open flights page");
        homePage.openVerticalPageDeeplink("Flights","",currency);
        flightSearchPage.closePopup();

        step("change date using deeplink");
        flightSearchPage.changeDateViaDeeplink(flightDynamicData, tripType);

        step("Toggle trip type { " + tripType + " }");
        flightSearchPage.toggleTriptype(tripType);

        step("Select origin airport");
        flightSearchPage.selectOrigin(originAirport);
        flightSearchPage.closePopup();

        step("Select destination airport");
        flightSearchPage.selectDestination(destinationAirport);

        step("Select departure date");
        flightSearchPage.selectDate(departureDate,returnDate);

        step("Add passenger");
        flightSearchPage.addPassenger(adultPassengers,childPassengers,infantPassengers);

        step("Search flight");
        flightSearchPage.searchFlight();

        step("filter free reschedule deals");
        flightSearchResultPage.filterChips(otherFilter);

        step("see Schedule Airline with free reschedule");
        boolean isLabelDisplayedOnCard = flightSearchResultPage.checkLabelOnCardSRP(otherFilter);
        Assertion.assertThat("Is free reschedule label display on the card", isLabelDisplayedOnCard, is(true));

        step("Click flight detail chevron");
        flightSearchResultPage.selectFlight();

        step("filter free reschedule deals");
        flightSearchResultPage.filterChips(otherFilter);

        step("see Schedule Airline with free reschedule");
        boolean isLabelDisplayedOnCard2 = flightSearchResultPage.checkLabelOnCardSRP(otherFilter);
        Assertion.assertThat("Is free reschedule label display on the card", isLabelDisplayedOnCard2, is(true));

        step("Click flight detail chevron");
        flightSearchResultPage.selectFlight();

        step("Continue to booking form");
        flightProductReviewPage.continueToBookingForm(tripType);

        step("Skip full protection if visible");
        flightBookingFormPage.skipFullProtectionPrp();

        step("verify date format");
        flightBookingFormPage.verifyDateFormat(tripType);

        step("Fill contact detail");
        flightBookingFormPage.fillContactDetail(phoneNumber,email,"guest",airline);

        step("Fill passenger detail");
        flightBookingFormPage.fillPassengerDetail(adultPassengers,childPassengers,infantPassengers, "international",airline);

        step("Continue to second half booking form");
        flightBookingFormPage.continueToSecondHalf();

        step("Verify what you get detail");
        Assertion.assertThat("Is what you'll get correct", flightBookingFormPage.isWhatYouGetCorrect(otherFilter), is(true));

        step("Open price detail");
        flightBookingFormPage.openPriceDetail();

        step("Verify price detail");
        PriceDetail priceDetail = new PriceDetail();
        priceDetail.tripType = tripType;
        priceDetail.adult = adultPassengers;
        priceDetail.child = childPassengers;
        priceDetail.infant = infantPassengers;

        Assertion.assertThat("Is price breakdown correct", flightBookingFormPage.isPriceBreakdownCorrect(priceDetail,currency), is(true));

        step("Continue to payment");
        flightBookingFormPage.continueToPayment();

        step("Skip full protection");
        flightBookingFormPage.skipFullProtection();
        flightBookingFormPage.baggageAllowancePopup();

        step("Check order ID");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",!orderID.isBlank(),is(true));
    }

    @Author(names = {"Zia"})
    @Module(name = "flightInternationalRoundtrip")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test(dataProvider="testFlightInternationalRoundtrip2BookingCodeLoginDataProvider")
    public void testFlightInternationalRoundtrip2BookingCodeLogin(Map<String, String> param) {
        //obsolete
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightProductReviewPage<WebDriver> flightProductReviewPage = FlightProductReviewPage.getInstance(session);
        FlightBookingFormPage<WebDriver> flightBookingFormPage = FlightBookingFormPage.getInstance(session);
        FlightPage<WebDriver> flightPage = FlightPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        FlightApi<?> flightApi = new FlightApi<>(session);

        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        int duration = Integer.parseInt(param.get("duration"));
        String otherFilter = param.get("otherFilter");

        String airline = CoreFunctions.convertSquareBracketToRound(param.get("airline"));
        String airlineReturn = param.get("airlineReturn");

        String currency = param.get("currency");
        String optionalLocation = param.get("optionalLocation");
        String optionalAirline = param.get("optionalAirline");

        step("Find dynamic data");
        FlightDynamicData flightDynamicData = flightApi.findDateRT(
                username,password,
                originAirport,destinationAirport,daysToDepartureDate,duration,
                adultPassengers,childPassengers,infantPassengers,
                tripType,airline,airlineReturn,otherFilter,otherFilter,"[CGK=KUL]","[Singapore Airlines=Singapore Airlines]",currency
        );
        originAirport = flightDynamicData.originAirport;
        destinationAirport = flightDynamicData.destinationAirport;
        String departureDate = CoreFunctions.convertToLongDate(flightDynamicData.departureDate);
        String returnDate = CoreFunctions.convertToLongDate(flightDynamicData.returnDate);
        airline = flightDynamicData.airlineDepart;
        airlineReturn = flightDynamicData.airlineReturn;

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Change currency");
        homePage.changeCurrency(currency,"");

        step("Open flights page");
        homePage.openVerticalPageDeeplink("Flights","",currency);
        flightSearchPage.closePopup();

        step("change date using deeplink");
        flightSearchPage.changeDateViaDeeplink(flightDynamicData, tripType);

        step("Toggle trip type { " + tripType + " }");
        flightSearchPage.toggleTriptype(tripType);

        step("Select origin airport");
        flightSearchPage.selectOrigin(originAirport);
        flightSearchPage.closePopup();

        step("Select destination airport");
        flightSearchPage.selectDestination(destinationAirport);

        step("Select departure date");
        flightSearchPage.selectDate(departureDate,returnDate);

        step("Add passenger");
        flightSearchPage.addPassenger(adultPassengers,childPassengers,infantPassengers);

        step("Search flight");
        flightSearchPage.searchFlight();

        step("filter free reschedule deals");
        flightSearchResultPage.filterChips(otherFilter);

        step("see Schedule Airline with free reschedule");
        boolean isLabelDisplayedOnCard = flightSearchResultPage.checkLabelOnCardSRP(otherFilter);
        Assertion.assertThat("Is free reschedule label display on the card", isLabelDisplayedOnCard, is(true));

        step("Click flight detail chevron");
        flightSearchResultPage.selectFlight();

        step("filter free reschedule deals");
        flightSearchResultPage.filterChips(otherFilter);

        step("see Schedule Airline with free reschedule");
        boolean isLabelDisplayedOnCard2 = flightSearchResultPage.checkLabelOnCardSRP(otherFilter);
        Assertion.assertThat("Is free reschedule label display on the card", isLabelDisplayedOnCard2, is(true));

        step("Click flight detail chevron");
        flightSearchResultPage.selectFlight();

        step("Continue to booking form");
        flightProductReviewPage.continueToBookingForm(tripType);

        step("Skip full protection if visible");
        flightBookingFormPage.skipFullProtectionPrp();

        step("verify date format");
        flightBookingFormPage.verifyDateFormat(tripType);

        step("Fill passenger detail");
        flightBookingFormPage.fillPassengerDetail(adultPassengers,childPassengers,infantPassengers, "international",airline);

        step("Continue to second half booking form");
        flightBookingFormPage.continueToSecondHalf();

        step("Verify what you get detail");
        Assertion.assertThat("Is what you'll get correct", flightBookingFormPage.isWhatYouGetCorrect(otherFilter), is(true));

        step("Open price detail");
        flightBookingFormPage.openPriceDetail();

        step("Verify price detail");
        PriceDetail priceDetail = new PriceDetail();
        priceDetail.tripType = tripType;
        priceDetail.adult = adultPassengers;
        priceDetail.child = childPassengers;
        priceDetail.infant = infantPassengers;

        Assertion.assertThat("Is price breakdown correct", flightBookingFormPage.isPriceBreakdownCorrect(priceDetail,currency), is(true));

        step("Continue to payment");
        flightBookingFormPage.continueToPayment();

        step("Skip full protection");
        flightBookingFormPage.skipFullProtection();
        flightBookingFormPage.baggageAllowancePopup();

        step("Check order ID");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",!orderID.isBlank(),is(true));
    }

    @Author(names = {"Ashish"})
    @Module(name = "FlightInternationalOneWay")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test(dataProvider="testFlightInternationalOneWayLoginAddOnSeatDataProvider")
    public void testFlightInternationalOneWayLoginAddOnSeat(Map<String, String> param) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightProductReviewPage<WebDriver> flightProductReviewPage = FlightProductReviewPage.getInstance(session);
        FlightBookingFormPage<WebDriver> flightBookingFormPage = FlightBookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        FlightApi<?> flightApi = new FlightApi<>(session);

        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");
        String optionalLocation = param.get("optionalLocation");
        String optionalAirline = param.get("optionalAirline");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        String airline = param.get("airline");
        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");

        String currency = param.get("currency");

        PriceDetail priceDetail = new PriceDetail();
        priceDetail.tripType=tripType;
        priceDetail.adult=adultPassengers;
        priceDetail.child=childPassengers;
        priceDetail.infant=infantPassengers;

        step("Find dynamic data");
        FlightDynamicData flightDynamicData = flightApi.findDate(
                username,password,
                originAirport,destinationAirport,daysToDepartureDate,
                adultPassengers,childPassengers,infantPassengers,
                tripType,airline,"seat",optionalLocation,optionalAirline,currency
        );
        String departureDate =CoreFunctions.convertToLongDate(flightDynamicData.departureDate);
        airline = flightDynamicData.airlineDepart;
        originAirport = flightDynamicData.originAirport;
        destinationAirport = flightDynamicData.destinationAirport;

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink (if needed) and change currency to " + currency);
        homePage.loginIfLoggedOutAndChangeCurrencyViaDeeplink(username, password, currency);

        step("Open flights page");
        homePage.openVerticalPageDeeplink("Flights","",currency);
        flightSearchPage.closePopup();

        step("change date using deeplink");
        flightSearchPage.changeDateViaDeeplink(flightDynamicData, tripType);

        step("Toggle trip type { " + tripType + " }");
        flightSearchPage.toggleTriptype(tripType);

        step("Select origin airport");
        flightSearchPage.selectOrigin(originAirport);
        flightSearchPage.closePopup();

        step("Select destination airport");
        flightSearchPage.selectDestination(destinationAirport);

        step("Select date");
        flightSearchPage.selectDate(departureDate);

        step("Add passenger");
        flightSearchPage.addPassenger(adultPassengers,childPassengers,infantPassengers);

        step("Search flight");
        flightSearchPage.searchFlight();

        step("Open filter");
        flightSearchResultPage.openFilter();

        step("Filter airline");
        flightSearchResultPage.filterAirline(airline);

        step("Save filter");
        flightSearchResultPage.saveFilter();

        step("Click flight detail chevron");
        flightSearchResultPage.selectFlight();

        step("Continue to booking form");
        flightProductReviewPage.continueToBookingForm(tripType);

        step("Skip full protection if visible");
        flightBookingFormPage.skipFullProtectionPrp();

        step("verify date format");
        flightBookingFormPage.verifyDateFormat(tripType);

        step("Fill passenger detail");
        flightBookingFormPage.fillPassengerDetail(adultPassengers,childPassengers,infantPassengers,"international",airline);

        step("Continue to second half booking form");
        flightBookingFormPage.continueToSecondHalf();

        step("Add AddOnSeat");
        String seatPrice=flightBookingFormPage.addSeat(priceDetail,currency);

        step("Open price detail");
        flightBookingFormPage.openPriceDetail();

        step("Verify price detail");
        priceDetail.seatDepartPrice=seatPrice;
        Assertion.assertThat("Is price breakdown correct", flightBookingFormPage.isPriceBreakdownCorrect(priceDetail,currency), is(true));

        step("Continue to payment");
        flightBookingFormPage.continueToPayment();

        step("Skip full protection");
        flightBookingFormPage.skipFullProtection();
        flightBookingFormPage.baggageAllowancePopup();

        step("Check order ID");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",!orderID.isBlank(),is(true));
    }

    @Author(names = {"Ashish"})
    @Module(name = "FlightDomesticRoundTrip")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test(dataProvider="testFlightDomesticRoundTripGuestExtraProtectionsDataProvider")
    public void testFlightDomesticRoundTripGuestExtraProtections(Map<String, String> param) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightProductReviewPage<WebDriver> flightProductReviewPage = FlightProductReviewPage.getInstance(session);
        FlightBookingFormPage<WebDriver> flightBookingFormPage = FlightBookingFormPage.getInstance(session);
        FlightPage<WebDriver> flightPage = FlightPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        FlightApi<?> flightApi = new FlightApi<>(session);

        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");
        int index = Integer.parseInt(param.get("index"));
        String email = param.get("email");
        String phoneNumber = param.get("phoneNumber");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        int duration = Integer.parseInt(param.get("duration"));

        String airline = param.get("airline");
        String airlineReturn = param.get("airlineReturn");
        String optionalLocation = param.get("optionalLocation");
        String optionalAirline = param.get("optionalAirline");

        String currency = param.get("currency");

        step("Find dynamic data");
        FlightDynamicData flightDynamicData = flightApi.findDateRT(
                "","",
                originAirport,destinationAirport,daysToDepartureDate,duration,
                adultPassengers,childPassengers,infantPassengers,
                tripType,"","","","",optionalLocation,optionalAirline,currency
        );
        String departureDate =CoreFunctions.convertToLongDate (flightDynamicData.departureDate);
        String returnDate =CoreFunctions.convertToLongDate(flightDynamicData.returnDate);
        airline = flightDynamicData.airlineDepart;
        airlineReturn = flightDynamicData.airlineReturn;
        originAirport = flightDynamicData.originAirport;
        destinationAirport = flightDynamicData.destinationAirport;

        step("Wait for app to launch");
        homePage.gotoHome();

        step("login as guest");
        loginPage.loginAsGuest();

        step("Change currency");
        homePage.changeCurrency(currency,"");

        step("Open flights page");
        homePage.openVerticalPageDeeplink("Flights","",currency);
        flightSearchPage.closePopup();

        step("change date using deeplink");
        flightSearchPage.changeDateViaDeeplink(flightDynamicData, tripType);

        step("Toggle trip type { " + tripType + " }");
        flightSearchPage.toggleTriptype(tripType);

        step("Select origin airport");
        flightSearchPage.selectOrigin(originAirport);
        flightSearchPage.closePopup();

        step("Select destination airport");
        flightSearchPage.selectDestination(destinationAirport);

        step("Select departure date");
        flightSearchPage.selectDate(departureDate,returnDate);

        step("Add passenger");
        flightSearchPage.addPassenger(adultPassengers,childPassengers,infantPassengers);

        step("Search flight");
        flightSearchPage.searchFlight();

        step("Click flight detail chevron");
        flightSearchResultPage.selectFlight();

        step("Click flight return trip detail chevron");
        flightSearchResultPage.selectFlight();

        step("Continue to booking form");
        flightProductReviewPage.continueToBookingForm(tripType);

        step("Skip full protection if visible");
        flightBookingFormPage.skipFullProtectionPrp();

        step("verify date format");
        flightBookingFormPage.verifyDateFormat(tripType);

        step("Fill contact detail");
        flightBookingFormPage.fillContactDetail(phoneNumber,email,"guest",airline);

        step("Fill passenger detail");
        flightBookingFormPage.fillPassengerDetail(adultPassengers,childPassengers,infantPassengers,"domestic",airline);

        step("Continue to second half booking form");
        flightBookingFormPage.continueToSecondHalf();

        step("Add Extra Protection");
        ArrayList<String> extraProtection = flightBookingFormPage.addExtraProtection(index);

        step("Open price detail");
        flightBookingFormPage.openPriceDetail();

        step("Verify price detail");
        PriceDetail priceDetail = new PriceDetail();
        priceDetail.tripType = tripType;
        priceDetail.adult = adultPassengers;
        priceDetail.child = childPassengers;
        priceDetail.infant = infantPassengers;
        priceDetail.extraProtectionTitle=extraProtection.get(0) + " extra protection";
        priceDetail.extraProtectionPrice=extraProtection.get(1);

        Assertion.assertThat("Is price breakdown correct", flightBookingFormPage.isPriceBreakdownCorrect(priceDetail,currency), is(true));

        step("Continue to payment");
        flightBookingFormPage.continueToPayment();

        step("Skip full protection");
        flightBookingFormPage.skipFullProtection();
        flightBookingFormPage.baggageAllowancePopup();

        step("Check order ID");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",!orderID.isBlank(),is(true));
    }

    @Author(names = {"Budi","Zia"})
    @Module(name = "FlightBundlingOW")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test(dataProvider="testFlightGuestBundlingOwDataProvider")
    public void testFlightGuestBundlingOw(Map<String, String> param) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightApi<?> flightApi = new FlightApi<>(session);

        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");
        String currency = param.get("currency");
        String optionalLocation = param.get("optionalLocation");
        String optionalAirline = param.get("optionalAirline");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        String airline = param.get("airline");

        step("Find dynamic data");
        FlightDynamicData flightDynamicData = flightApi.findDate(
                "","",
                originAirport,destinationAirport,daysToDepartureDate,
                adultPassengers,childPassengers,infantPassengers,
                tripType,airline,"free airport transfer/free antigen test",optionalLocation,optionalAirline,currency
        );
        String departureDate = CoreFunctions.convertToLongDate (flightDynamicData.departureDate);
        airline = flightDynamicData.airlineDepart;

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Logout via deeplink");
        homePage.logoutViaDeeplink();

        step("Change currency");
        homePage.changeCurrency(currency,"");

        step("Open flights page");
        homePage.openVerticalPageDeeplink("Flights","","");
        flightSearchPage.closePopup();

        step("change date using deeplink");
        flightSearchPage.changeDateViaDeeplink(flightDynamicData, tripType);

        step("Toggle trip type { " + tripType + " }");
        flightSearchPage.toggleTriptype(tripType);

        step("Select origin airport");
        flightSearchPage.selectOrigin(originAirport);
        flightSearchPage.closePopup();

        step("Select destination airport");
        flightSearchPage.selectDestination(destinationAirport);

        step("Select departure date");
        flightSearchPage.selectDate(departureDate);

        step("Add passenger");
        flightSearchPage.addPassenger(adultPassengers, childPassengers, infantPassengers);

        step("Search flight");
        flightSearchPage.searchFlight();

        step("Open filter");
        flightSearchResultPage.openFilter();

        step("Filter airline");
        flightSearchResultPage.filterAirline(airline);

        step("Save filter");
        flightSearchResultPage.saveFilter();

        step("verify what you'll get");
        flightSearchResultPage.clickSeeDetailSrp(1);
        Assertion.assertThat("Verify free airport transfer label in detail",flightSearchResultPage.isFreeTransferDetailOneWay(),is(true));
    }

    @Author(names = {"Budi","Zia"})
    @Module(name = "FlightBundlingOW")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test(dataProvider="testFlightLoginBundlingOwDataProvider")
    public void testFlightLoginBundlingOw(Map<String, String> param) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightApi<?> flightApi = new FlightApi<>(session);

        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String currency = param.get("currency");
        String optionalLocation = param.get("optionalLocation");
        String optionalAirline = param.get("optionalAirline");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));

        String airline = param.get("airline");

        step("Find dynamic data");
        FlightDynamicData flightDynamicData = flightApi.findDate(
                username,password,
                originAirport,destinationAirport,daysToDepartureDate,
                adultPassengers,childPassengers,infantPassengers,
                tripType,airline,"",optionalLocation,optionalAirline,currency
        );
        String departureDate = CoreFunctions.convertToLongDate (flightDynamicData.departureDate);
        airline = flightDynamicData.airlineDepart;

        step("Wait for app to launch");
        homePage.gotoHome();

        step("login with email");
        loginPage.loginWith(loginType,username,password);

        step("Change currency");
        homePage.changeCurrency(currency,"");

        step("Open flights page");
        homePage.openVerticalPageDeeplink("Flights","","");
        flightSearchPage.closePopup();

        step("change date using deeplink");
        flightSearchPage.changeDateViaDeeplink(flightDynamicData, tripType);

        step("Toggle trip type { " + tripType + " }");
        flightSearchPage.toggleTriptype(tripType);

        step("Select origin airport");
        flightSearchPage.selectOrigin(originAirport);
        flightSearchPage.closePopup();

        step("Select destination airport");
        flightSearchPage.selectDestination(destinationAirport);

        step("Select departure date");
        flightSearchPage.selectDate(departureDate);

        step("Add passenger");
        flightSearchPage.addPassenger(adultPassengers, childPassengers, infantPassengers);

        step("Search flight");
        flightSearchPage.searchFlight();

        step("Open filter");
        flightSearchResultPage.openFilter();

        step("Filter airline");
        flightSearchResultPage.filterAirline(airline);

        step("Save filter");
        flightSearchResultPage.saveFilter();

        step("verify Verify bundling in search");
        flightSearchResultPage.clickSeeDetailSrp(1);
        Assertion.assertThat("Verify free airport transfer label in detail",flightSearchResultPage.isFreeTransferDetailOneWay(),is(true));
    }

    @Author(names = {"Budi","Zia", "Aditi"})
    @Module(name = "FlightBundlingRT")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test(dataProvider="testFlightGuestBundlingRTDataProvider")
    public void testFlightGuestBundlingRT(Map<String, String> param) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightProductReviewPage<WebDriver> flightProductReviewPage = FlightProductReviewPage.getInstance(session);
        FlightBookingFormPage<WebDriver> flightBookingFormPage = FlightBookingFormPage.getInstance(session);
        FlightApi<?> flightApi = new FlightApi<>(session);

        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");
        String currency = param.get("currency");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        int duration = Integer.parseInt(param.get("duration"));
        String phoneNumber = param.get("phoneNumber");
        String email = param.get("email");

        String airline = param.get("airline");
        String airlineReturn = param.get("airlineReturn");
        String optionalLocation = param.get("optionalLocation");
        String optionalAirline = param.get("optionalAirline");

        step("Find dynamic data");
        FlightDynamicData flightDynamicData = flightApi.findDateRT(
                "","",
                originAirport,destinationAirport,daysToDepartureDate,duration,
                adultPassengers,childPassengers,infantPassengers,
                tripType,airline,airlineReturn,"free airport transfer/free antigen test","free airport transfer/free antigen test",optionalLocation,optionalAirline,currency
        );
        String departureDate =CoreFunctions.convertToLongDate (flightDynamicData.departureDate);
        String returnDate =CoreFunctions.convertToLongDate(flightDynamicData.returnDate);
        airline = flightDynamicData.airlineDepart;
        airlineReturn = flightDynamicData.airlineReturn;

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Logout via deeplink");
        homePage.logoutViaDeeplink();

        step("Change currency");
        homePage.changeCurrency(currency,"");

        step("Open flights page");
        homePage.openVerticalPageDeeplink("Flights","","");
        flightSearchPage.closePopup();

        step("change date using deeplink");
        flightSearchPage.changeDateViaDeeplink(flightDynamicData, tripType);

        step("Toggle trip type { " + tripType + " }");
        flightSearchPage.toggleTriptype(tripType);

        step("Select origin airport");
        flightSearchPage.selectOrigin(originAirport);
        flightSearchPage.closePopup();

        step("Select destination airport");
        flightSearchPage.selectDestination(destinationAirport);

        step("Select departure date");
        flightSearchPage.selectDate(departureDate,returnDate);

        step("Add passenger");
        flightSearchPage.addPassenger(adultPassengers, childPassengers, infantPassengers);

        step("Search flight");
        flightSearchPage.searchFlight();

        step("Open filter");
        flightSearchResultPage.openFilter();

        step("Filter airline");
        flightSearchResultPage.filterAirline(airline);

        step("Save filter");
        flightSearchResultPage.saveFilter();

        step("verify Verify bundling in search for departure flight");
        flightSearchResultPage.clickSeeDetailSrp(1);
        Assertion.assertThat("Verify free airport transfer label in detail",flightSearchResultPage.isFreeTransferDetailOneWay(),is(true));

        step("Click flight detail chevron");
        flightSearchResultPage.selectFlight();

        step("Open filter");
        flightSearchResultPage.openFilter();

        step("Filter airline");
        flightSearchResultPage.filterAirline(airlineReturn);

        step("Save filter");
        flightSearchResultPage.saveFilter();

        step("Click see details");
        flightSearchResultPage.clickSeeDetailSrp(2);

        step("verify bundling in search for return flight");
        Assertion.assertThat("Verify free airport transfer label in detail",flightSearchResultPage.isFreeTransferDetail(),is(true));

        step("Click flight detail chevron");
        flightSearchResultPage.selectFlightSpecialCampaign(airlineReturn);

        step("Continue to booking form");
        flightProductReviewPage.continueToBookingForm(tripType);

        step("Skip full protection if visible");
        flightBookingFormPage.skipFullProtectionPrp();

        step("verify date format");
        flightBookingFormPage.verifyDateFormat(tripType);

        step("Fill contact detail");
        flightBookingFormPage.fillContactDetail(phoneNumber,email,"guest",airline);

        step("Fill passenger detail");
        flightBookingFormPage.fillPassengerDetail(adultPassengers,childPassengers,infantPassengers,"domestic",airline);

        step("Continue to second half booking form");
        flightBookingFormPage.continueToSecondHalf();

        step("Verify bundling in detail");
        Assertion.assertThat("Verify detail labels for departure flight", flightBookingFormPage.labelsOnBookingPageCorrect(), is(true));
    }

    @Author(names = {"Budi","Zia", "Aditi"})
    @Module(name = "FlightBundlingRT")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test(dataProvider="testFlightLoginBundlingRTDataProvider")
    public void testFlightLoginBundlingRT(Map<String, String> param) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightProductReviewPage<WebDriver> flightProductReviewPage = FlightProductReviewPage.getInstance(session);
        FlightBookingFormPage<WebDriver> flightBookingFormPage = FlightBookingFormPage.getInstance(session);
        FlightApi<?> flightApi = new FlightApi<>(session);

        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String currency = param.get("currency");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        int duration = Integer.parseInt(param.get("duration"));
        String phoneNumber = param.get("phoneNumber");

        String airline = param.get("airline");
        String airlineReturn = param.get("airlineReturn");
        String optionalLocation = param.get("optionalLocation");
        String optionalAirline = param.get("optionalAirline");

        step("Find dynamic data");
        FlightDynamicData flightDynamicData = flightApi.findDateRT(
                username,password,
                originAirport,destinationAirport,daysToDepartureDate,duration,
                adultPassengers,childPassengers,infantPassengers,
                tripType,airline,airlineReturn,"free airport transfer/free antigen test","free airport transfer/free antigen test",optionalLocation,optionalAirline,currency
        );
        String departureDate =CoreFunctions.convertToLongDate (flightDynamicData.departureDate);
        String returnDate =CoreFunctions.convertToLongDate(flightDynamicData.returnDate);
        airline = flightDynamicData.airlineDepart;
        airlineReturn = flightDynamicData.airlineReturn;

        step("Wait for app to launch");
        homePage.gotoHome();

        step("login with email");
        loginPage.loginWith(loginType,username,password);

        step("Change currency");
        homePage.changeCurrency(currency,"");

        step("Open flights page");
        homePage.openVerticalPageDeeplink("Flights","","");
        flightSearchPage.closePopup();

        step("change date using deeplink");
        flightSearchPage.changeDateViaDeeplink(flightDynamicData, tripType);

        step("Toggle trip type { " + tripType + " }");
        flightSearchPage.toggleTriptype(tripType);

        step("Select origin airport");
        flightSearchPage.selectOrigin(originAirport);
        flightSearchPage.closePopup();

        step("Select destination airport");
        flightSearchPage.selectDestination(destinationAirport);

        step("Select departure date");
        flightSearchPage.selectDate(departureDate,returnDate);

        step("Add passenger");
        flightSearchPage.addPassenger(adultPassengers, childPassengers, infantPassengers);

        step("Search flight");
        flightSearchPage.searchFlight();

        step("Open filter");
        flightSearchResultPage.openFilter();

        step("Filter airline");
        flightSearchResultPage.filterAirline(airline);

        step("Save filter");
        flightSearchResultPage.saveFilter();

        step("verify Verify bundling in search");
        flightSearchResultPage.clickSeeDetailSrp(1);
        Assertion.assertThat("Verify free airport transfer label in detail",flightSearchResultPage.isFreeTransferDetailOneWay(),is(true));

        step("Click flight detail chevron");
        flightSearchResultPage.selectFlight();

        step("Open filter");
        flightSearchResultPage.openFilter();

        step("Filter airline");
        flightSearchResultPage.filterAirline(airlineReturn);

        step("Save filter");
        flightSearchResultPage.saveFilter();

        step("verify Verify bundling in search");
        flightSearchResultPage.clickSeeDetailSrp(2);
        Assertion.assertThat("Verify free airport transfer label in detail",flightSearchResultPage.isFreeTransferDetail(),is(true));

        step("Click flight detail chevron");
        flightSearchResultPage.selectFlight();

        step("Continue to booking form");
        flightProductReviewPage.continueToBookingForm(tripType);

        step("Skip full protection if visible");
        flightBookingFormPage.skipFullProtectionPrp();

        step("verify date format");
        flightBookingFormPage.verifyDateFormat(tripType);

        step("Fill passenger detail");
        flightBookingFormPage.fillPassengerDetail(adultPassengers,childPassengers,infantPassengers,"domestic",airline);

        step("Continue to second half booking form");
        flightBookingFormPage.continueToSecondHalf();

        step("Verify bundling in detail");
        Assertion.assertThat("Verify detail labels for departure flight", flightBookingFormPage.labelsOnBookingPageCorrect(), is(true));
    }

    @Author(names = {"Ashish"})
    @Module(name = "FlightDomesticRoundTrip")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test(dataProvider="testFlightDomesticRoundTripLoginExtraProtectionsDataProvider")
    public void testFlightDomesticRoundTripLoginExtraProtections(Map<String, String> param) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightProductReviewPage<WebDriver> flightProductReviewPage = FlightProductReviewPage.getInstance(session);
        FlightBookingFormPage<WebDriver> flightBookingFormPage = FlightBookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        FlightApi<?> flightApi = new FlightApi<>(session);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        int index = Integer.parseInt(param.get("index"));
        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        int duration = Integer.parseInt(param.get("duration"));

        String airline = param.get("airline");
        String airlineReturn = param.get("airlineReturn");
        String optionalLocation = param.get("optionalLocation");
        String optionalAirline = param.get("optionalAirline");

        String currency = param.get("currency");

        step("Find dynamic data");
        FlightDynamicData flightDynamicData = flightApi.findDateRT(
                username,password,
                originAirport,destinationAirport,daysToDepartureDate,duration,
                adultPassengers,childPassengers,infantPassengers,
                tripType,"","","","",optionalLocation,optionalAirline,currency
        );
        String departureDate =CoreFunctions.convertToLongDate (flightDynamicData.departureDate);
        String returnDate =CoreFunctions.convertToLongDate(flightDynamicData.returnDate);
        airline = flightDynamicData.airlineDepart;
        airlineReturn = flightDynamicData.airlineReturn;
        originAirport = flightDynamicData.originAirport;
        destinationAirport = flightDynamicData.destinationAirport;

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink (if needed) and change currency to " + currency);
        homePage.loginIfLoggedOutAndChangeCurrencyViaDeeplink(username, password, currency);

        step("Open flights page");
        homePage.openVerticalPageDeeplink("Flights","",currency);
        flightSearchPage.closePopup();

        step("change date using deeplink");
        flightSearchPage.changeDateViaDeeplink(flightDynamicData, tripType);

        step("Toggle trip type { " + tripType + " }");
        flightSearchPage.toggleTriptype(tripType);

        step("Select origin airport");
        flightSearchPage.selectOrigin(originAirport);
        flightSearchPage.closePopup();

        step("Select destination airport");
        flightSearchPage.selectDestination(destinationAirport);

        step("Select departure date");
        flightSearchPage.selectDate(departureDate,returnDate);

        step("Add passenger");
        flightSearchPage.addPassenger(adultPassengers,childPassengers,infantPassengers);

        step("Search flight");
        flightSearchPage.searchFlight();

        step("Open filter");
        flightSearchResultPage.openFilter();

        step("Filter airline");
        flightSearchResultPage.filterAirline(airline);

        step("Save filter");
        flightSearchResultPage.saveFilter();

        step("Click flight detail chevron");
        flightSearchResultPage.selectFlight();

        step("Open filter");
        flightSearchResultPage.openFilter();

        step("Filter airline");
        flightSearchResultPage.filterAirline(airlineReturn);

        step("Save filter");
        flightSearchResultPage.saveFilter();

        step("Click flight detail chevron");
        flightSearchResultPage.selectFlight();

        step("Continue to booking form");
        flightProductReviewPage.continueToBookingForm(tripType);

        step("Skip full protection if visible");
        flightBookingFormPage.skipFullProtectionPrp();

        step("verify date format");
        flightBookingFormPage.verifyDateFormat(tripType);

        step("Fill passenger detail");
        flightBookingFormPage.fillPassengerDetail(adultPassengers,childPassengers,infantPassengers,"domestic",airline);

        step("Continue to second half booking form");
        flightBookingFormPage.continueToSecondHalf();

        step("Add Extra Protection");
        ArrayList<String> extraProtection = flightBookingFormPage.addExtraProtection(index);

        step("Open price detail");
        flightBookingFormPage.openPriceDetail();

        step("Verify price detail");
        PriceDetail priceDetail = new PriceDetail();
        priceDetail.tripType = tripType;
        priceDetail.adult = adultPassengers;
        priceDetail.child = childPassengers;
        priceDetail.infant = infantPassengers;
        priceDetail.extraProtectionTitle=extraProtection.get(0) + " extra protection";
        priceDetail.extraProtectionPrice=extraProtection.get(1);

        Assertion.assertThat("Is price breakdown correct", flightBookingFormPage.isPriceBreakdownCorrect(priceDetail,currency), is(true));

        step("Continue to payment");
        flightBookingFormPage.continueToPayment();

        step("Skip full protection");
        flightBookingFormPage.skipFullProtection();
        flightBookingFormPage.baggageAllowancePopup();

        step("Check order ID");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",!orderID.isBlank(),is(true));
    }

    @Author(names = {"Ashish"})
    @Module(name = "FlightInternationalOneWay")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test(dataProvider="testFlightInternationalOneWayGuestAddOnSeatDataProvider")
    public void testFlightInternationalOneWayGuestAddOnSeat(Map<String, String> param) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightProductReviewPage<WebDriver> flightProductReviewPage = FlightProductReviewPage.getInstance(session);
        FlightBookingFormPage<WebDriver> flightBookingFormPage = FlightBookingFormPage.getInstance(session);
        FlightPage<WebDriver> flightPage = FlightPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        FlightApi<?> flightApi = new FlightApi<>(session);

        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        String airline = param.get("airline");
        String email = param.get("email");
        String phoneNumber = param.get("phoneNumber");
        String optionalLocation = param.get("optionalLocation");
        String optionalAirline = param.get("optionalAirline");
        String currency = param.get("currency");

        PriceDetail priceDetail = new PriceDetail();
        priceDetail.tripType=tripType;
        priceDetail.adult=adultPassengers;
        priceDetail.child=childPassengers;
        priceDetail.infant=infantPassengers;

        step("Find dynamic data");
        FlightDynamicData flightDynamicData = flightApi.findDate(
                "","",
                originAirport,destinationAirport,daysToDepartureDate,
                adultPassengers,childPassengers,infantPassengers,
                tripType,"","seat",optionalLocation,optionalAirline,currency
        );
        String departureDate =CoreFunctions.convertToLongDate(flightDynamicData.departureDate);
        airline = flightDynamicData.airlineDepart;
        originAirport = flightDynamicData.originAirport;
        destinationAirport = flightDynamicData.destinationAirport;

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Logout via deeplink");
        homePage.logoutViaDeeplink();

        step("Change currency");
        homePage.changeCurrency(currency,"");

        step("Open flights page");
        homePage.openVerticalPageDeeplink("Flights","",currency);
        flightSearchPage.closePopup();

        step("change date using deeplink");
        flightSearchPage.changeDateViaDeeplink(flightDynamicData, tripType);

        step("Toggle trip type { " + tripType + " }");
        flightSearchPage.toggleTriptype(tripType);

        step("Select origin airport");
        flightSearchPage.selectOrigin(originAirport);
        flightSearchPage.closePopup();

        step("Select destination airport");
        flightSearchPage.selectDestination(destinationAirport);

        step("Select date");
        flightSearchPage.selectDate(departureDate);

        step("Add passenger");
        flightSearchPage.addPassenger(adultPassengers,childPassengers,infantPassengers);

        step("Search flight");
        flightSearchPage.searchFlight();

        step("Open filter");
        flightSearchResultPage.openFilter();

        step("Filter airline");
        flightSearchResultPage.filterAirline(airline);

        step("Save filter");
        flightSearchResultPage.saveFilter();

        step("Click flight detail chevron");
        flightSearchResultPage.selectFlight();

        step("Continue to booking form");
        flightProductReviewPage.continueToBookingForm(tripType);

        step("Skip full protection if visible");
        flightBookingFormPage.skipFullProtectionPrp();

        step("verify date format");
        flightBookingFormPage.verifyDateFormat(tripType);

        step("Fill contact detail");
        flightBookingFormPage.fillContactDetail(phoneNumber,email,"guest",airline);

        step("Fill passenger detail");
        flightBookingFormPage.fillPassengerDetail(adultPassengers,childPassengers,infantPassengers,"international",airline);

        step("Continue to second half booking form");
        flightBookingFormPage.continueToSecondHalf();

        step("Add AddOnSeat");
        String seatPrice=flightBookingFormPage.addSeat(priceDetail,currency);

        step("Open price detail");
        flightBookingFormPage.openPriceDetail();

        step("Verify price detail");
        priceDetail.seatDepartPrice=seatPrice;
        Assertion.assertThat("Is price breakdown correct", flightBookingFormPage.isPriceBreakdownCorrect(priceDetail,currency), is(true));

        step("Continue to payment");
        flightBookingFormPage.continueToPayment();

        step("Skip full protection");
        flightBookingFormPage.skipFullProtection();
        flightBookingFormPage.baggageAllowancePopup();

        step("Check order ID");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",!orderID.isBlank(),is(true));
    }

    @Author(names = {"Ashish"})
    @Module(name = "FlightDomesticRoundTrip")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test(dataProvider="testFlightDomesticRoundTripGuestAddOnMealsDataProvider")
    public void testFlightDomesticRoundTripGuestAddOnMeals(Map<String, String> param) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightProductReviewPage<WebDriver> flightProductReviewPage = FlightProductReviewPage.getInstance(session);
        FlightBookingFormPage<WebDriver> flightBookingFormPage = FlightBookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        FlightApi<?> flightApi = new FlightApi<>(session);

        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int duration = Integer.parseInt(param.get("duration"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        String airline = param.get("airline");
        String email = param.get("email");
        String phoneNumber = param.get("phoneNumber");
        String optionalLocation = param.get("optionalLocation");
        String optionalAirline = param.get("optionalAirline");
        String currency = param.get("currency");

        step("Find dynamic data");
        FlightDynamicData flightDynamicData = flightApi.findDateRT(
                "","",
                originAirport,destinationAirport,daysToDepartureDate,duration,
                adultPassengers,childPassengers,infantPassengers,
                tripType,airline,airline,"meal","meal",optionalLocation,optionalAirline,currency
        );
        String departureDate =CoreFunctions.convertToLongDate(flightDynamicData.departureDate);
        String returnDate =CoreFunctions.convertToLongDate(flightDynamicData.returnDate);
        airline = flightDynamicData.airlineDepart;
        originAirport = flightDynamicData.originAirport;
        destinationAirport = flightDynamicData.destinationAirport;

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Logout via deeplink");
        homePage.logoutViaDeeplink();

        step("Change currency");
        homePage.changeCurrency(currency,"");

        step("Open flights page");
        homePage.openVerticalPageDeeplink("Flights","",currency);
        flightSearchPage.closePopup();

        step("change date using deeplink");
        flightSearchPage.changeDateViaDeeplink(flightDynamicData, tripType);

        step("Toggle trip type { " + tripType + " }");
        flightSearchPage.toggleTriptype(tripType);

        step("Select origin airport");
        flightSearchPage.selectOrigin(originAirport);
        flightSearchPage.closePopup();

        step("Select destination airport");
        flightSearchPage.selectDestination(destinationAirport);

        step("Select date");
        flightSearchPage.selectDate(departureDate,returnDate);

        step("Add passenger");
        flightSearchPage.addPassenger(adultPassengers,childPassengers,infantPassengers);

        step("Search flight");
        flightSearchPage.searchFlight();

        step("Open filter");
        flightSearchResultPage.openFilter();

        step("Filter airline");
        flightSearchResultPage.filterAirline(airline);

        step("Save filter");
        flightSearchResultPage.saveFilter();

        step("Click flight detail chevron");
        flightSearchResultPage.selectFlight();

        step("Open filter");
        flightSearchResultPage.openFilter();

        step("Filter airline");
        flightSearchResultPage.filterAirline(airline);

        step("Save filter");
        flightSearchResultPage.saveFilter();

        step("Click flight detail chevron");
        flightSearchResultPage.selectFlight();

        step("Continue to booking form");
        flightProductReviewPage.continueToBookingForm(tripType);

        step("Skip full protection if visible");
        flightBookingFormPage.skipFullProtectionPrp();

        step("verify date format");
        flightBookingFormPage.verifyDateFormat(tripType);

        step("Fill contact detail");
        flightBookingFormPage.fillContactDetail(phoneNumber,email,"guest",airline);

        step("Fill passenger detail");
        flightBookingFormPage.fillPassengerDetail(adultPassengers,childPassengers,infantPassengers,"domestic",airline);

        step("Continue to second half booking form");
        flightBookingFormPage.continueToSecondHalf();

        step("Add AddOnMeals");
        String mealPrice=flightBookingFormPage.addMeals(adultPassengers,childPassengers,currency);

        step("Open price detail");
        flightBookingFormPage.openPriceDetail();

        step("Verify price detail");
        PriceDetail priceDetail = new PriceDetail();
        priceDetail.tripType=tripType;

        priceDetail.adult = adultPassengers;
        priceDetail.child = childPassengers;
        priceDetail.infant = infantPassengers;
        priceDetail.mealPrice=mealPrice;
        Assertion.assertThat("Is price breakdown correct", flightBookingFormPage.isPriceBreakdownCorrect(priceDetail,currency), is(true));

        step("Continue to payment");
        flightBookingFormPage.continueToPayment();

        step("Skip full protection");
        flightBookingFormPage.skipFullProtection();
        flightBookingFormPage.baggageAllowancePopup();

        step("Check order ID");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",!orderID.isBlank(),is(true));
    }

    @Author(names = {"Ashish"})
    @Module(name = "FlightDomesticOneway")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test(dataProvider="testFlightDomesticOnewayDirectGuestAddOnBaggageDataProvider")
    public void testFlightDomesticOnewayDirectGuestAddOnBaggage(Map<String, String> param) throws ExecutionException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightProductReviewPage<WebDriver> flightProductReviewPage = FlightProductReviewPage.getInstance(session);
        FlightBookingFormPage<WebDriver> flightBookingFormPage = FlightBookingFormPage.getInstance(session);
        FlightPage<WebDriver> flightPage = FlightPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        FlightApi<?> flightApi = new FlightApi<>(session);

        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");
        String optionalLocation = param.get("optionalLocation");
        String optionalAirline = param.get("optionalAirline");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        String airline = param.get("airline");
        String transit = param.get("transit");
        String email = param.get("email");
        String phoneNumber = param.get("phoneNumber");
        int baggageAmount = Integer.parseInt(param.get("baggage"));
        String currency = param.get("currency");
        String paymentMethod = param.getOrDefault("paymentMethod", "Sakuku");

        step("Precondition: set Don't keep activities");
        flightPage.enableDontKeepActivities();

        step("Find dynamic data");
        FlightDynamicData flightDynamicData = flightApi.findDate(
                "","",
                originAirport,destinationAirport,daysToDepartureDate,
                adultPassengers,childPassengers,infantPassengers,
                tripType,airline,"baggage",optionalLocation,optionalAirline,currency
        );
        String departureDate =CoreFunctions.convertToLongDate(flightDynamicData.departureDate);
        airline = flightDynamicData.airlineDepart;
        originAirport = flightDynamicData.originAirport;
        destinationAirport = flightDynamicData.destinationAirport;

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Logout via deeplink");
        homePage.logoutViaDeeplink();

        step("Change currency");
        homePage.changeCurrency(currency,"");

        step("Open flights page");
        homePage.openVerticalPageDeeplink("Flights","",currency);
        flightSearchPage.closePopup();

        step("change date using deeplink");
        flightSearchPage.changeDateViaDeeplink(flightDynamicData, tripType);

        step("Toggle trip type { " + tripType + " }");
        flightSearchPage.toggleTriptype(tripType);

        step("Select origin airport");
        flightSearchPage.selectOrigin(originAirport);
        flightSearchPage.closePopup();

        step("Select destination airport");
        flightSearchPage.selectDestination(destinationAirport);

        step("Select date");
        flightSearchPage.selectDate(departureDate);

        step("Add passenger");
        flightSearchPage.addPassenger(adultPassengers,childPassengers,infantPassengers);

        step("Search flight");
        flightSearchPage.searchFlight();
        flightSearchResultPage.waitForPageLoad();

        step("Tap promo entry on SRP (FAB or sticky promo banner Apply)");
        Assertion.assertThat("Promo entry is displayed on SRP", flightSearchResultPage.isFloatingIconDisplayed(), is(true));
        flightSearchResultPage.clickFloatingIcon();

        step("Verify promo applied / promo page and return to SRP");
        flightPage.floatingIconRedirection();

        step("Open filter");
        flightSearchResultPage.openFilter();

        step("Filter airline");
        flightSearchResultPage.filterAirline(airline);

        step("Save filter");
        flightSearchResultPage.saveFilter();

        if (transit != null && !transit.isBlank()) {
            step("Filter transit { " + transit + " }");
            flightSearchResultPage.waitForPageLoad();
            flightSearchResultPage.openTransitFilter();
            flightSearchResultPage.filterTransitType(transit);
            flightSearchResultPage.saveFilter();
        }

        step("Click flight detail chevron");
        flightSearchResultPage.selectFlight();

        step("Verify product review page details");
        Assertion.assertThat("Product review page is displayed with schedule selection", flightProductReviewPage.isProductReviewPageDisplayed(), is(true));

        step("Open and verify price details on PRP");
        Assertion.assertThat("PRP price breakdown is correct (Pay now / Total After Cashback)", flightProductReviewPage.verifyPriceBreakdownPRP(), is(true));

        step("Continue to booking form");
        flightProductReviewPage.continueToBookingForm(tripType);

        step("Skip full protection if visible");
        flightBookingFormPage.skipFullProtectionPrp();

        step("Verify booking form 1st half");
        Assertion.assertThat("Booking form 1st half is displayed", flightBookingFormPage.isBookingFormPageDisplayed(), is(true));

        step("verify date format");
        flightBookingFormPage.verifyDateFormat(tripType);

        step("Fill contact detail");
        flightBookingFormPage.fillContactDetail(phoneNumber,email,"guest",airline);

        step("Fill passenger detail");
        flightBookingFormPage.fillPassengerDetail(adultPassengers,childPassengers,infantPassengers, "domestic",airline);

        step("Add baggage");
        baggageAmount = flightBookingFormPage.addBaggage();
        String baggagePrice = flightBookingFormPage.getBaggagePrice(baggageAmount);
        flightBookingFormPage.saveAddBaggage();

        step("Continue to second half booking form");
        flightBookingFormPage.continueToSecondHalf();

        step("Open price detail");
        flightBookingFormPage.openPriceDetail();

        step("Verify price detail");
        PriceDetail priceDetail = new PriceDetail();

        priceDetail.tripType=tripType;
        priceDetail.adult=adultPassengers;
        priceDetail.child=childPassengers;
        priceDetail.infant=infantPassengers;
        priceDetail.baggageDepartAmount=baggageAmount;
        priceDetail.baggageDepartPrice=baggagePrice;

        Assertion.assertThat("Is price breakdown correct", flightBookingFormPage.isPriceBreakdownCorrect(priceDetail,currency), is(true));

        step("Continue to payment");
        flightBookingFormPage.continueToPayment();

        step("Skip full protection / baggage offer (No, thank you)");
        flightBookingFormPage.skipFullProtection();
        flightBookingFormPage.baggageAllowancePopup();

        step("Check order ID");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",!orderID.isBlank(),is(true));

        step("Choose payment method { " + paymentMethod + " }");
        paymentPage.openPaymentMethod();
        paymentPage.selectPaymentMethod(paymentMethod);

        step("Validate suppliers are mock issued by airline");
        boolean isDepartSuppliersMocked = flightApi.areAllSuppliersMockIssuedByAirline(flightDynamicData.airlineCodeDepart, flightDynamicData.originAirport, flightDynamicData.destinationAirport);

        step("Complete payment and go to my order when suppliers are mocked { isDepartSuppliersMocked: " + isDepartSuppliersMocked + " }");
        if (Configs.ENVIRONMENT != Environment.PROD && isDepartSuppliersMocked) {
            step("Submit payment");
            paymentPage.clickPayButton();

            step("Auto paid order via api (I have completed the payment)");
            paymentPage.postAutoPaidOrder();
            orderListPage.waitForPaymentSuccessful(orderID,15);

            step("Go to my order");
            paymentPage.seeOrderList(orderID);
            Assertion.assertThat("Order list / my order opened for order id", !orderID.isBlank(), is(true));
        }
    }

    @Author(names = {"Ashish"})
    @Module(name = "FlightRecentSearch")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test(dataProvider="testFlightRecentSearchLoginDataProvider")
    public void testFlightRecentSearchLogin(Map<String, String> param) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightProductReviewPage<WebDriver> flightProductReviewPage = FlightProductReviewPage.getInstance(session);
        FlightApi<?> flightApi = new FlightApi<>(session);

        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String optionalLocation = param.get("optionalLocation");
        String optionalAirline = param.get("optionalAirline");

        step("Find dynamic data");
        FlightDynamicData flightDynamicData = flightApi.findDate(
                username,password,
                originAirport,destinationAirport,daysToDepartureDate,
                1,0,0,
                tripType,"","",optionalLocation,optionalAirline,"IDR"
        );
        String departureDate =CoreFunctions.convertToLongDate(flightDynamicData.departureDate);
        originAirport = flightDynamicData.originAirport;
        destinationAirport = flightDynamicData.destinationAirport;

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType,username,password);

        step("Open flights page");
        homePage.openVerticalPageDeeplink("Flights","","");
        flightSearchPage.closePopup();

        step("Clear recent search");
        flightSearchPage.clearRecentSearch();

        step("change date using deeplink");
        flightSearchPage.changeDateViaDeeplink(flightDynamicData, tripType);

        step("Toggle trip type { " + tripType + " }");
        flightSearchPage.toggleTriptype(tripType);

        step("Select origin airport");
        flightSearchPage.selectOrigin(originAirport);
        flightSearchPage.closePopup();

        step("Select destination airport");
        flightSearchPage.selectDestination(destinationAirport);

        step("Select date");
        flightSearchPage.selectDate(departureDate);

        step("Search flight");
        flightSearchPage.searchFlight();

        step("Back to flights page");
        flightSearchResultPage.backToFlightPage();

        step("Click on recent search box");
        flightSearchPage.clickOnRecentSearch();

        step("search result page through recent search");
        boolean isOnSearchResultPage = flightSearchResultPage.isOnSearchResultPage();
        Assertion.assertThat("verify we are on search result page through recent search",isOnSearchResultPage,is(true));
    }

    @Author(names = {"Ashish"})
    @Module(name = "FlightRecentSearch")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test(dataProvider="testFlightRecentSearchGuestDataProvider")
    public void testFlightRecentSearchGuest(Map<String, String> param) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightProductReviewPage<WebDriver> flightProductReviewPage = FlightProductReviewPage.getInstance(session);
        FlightApi<?> flightApi = new FlightApi<>(session);

        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");
        String optionalLocation = param.get("optionalLocation");
        String optionalAirline = param.get("optionalAirline");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));

        step("Find dynamic data");
        FlightDynamicData flightDynamicData = flightApi.findDate(
                "","",
                originAirport,destinationAirport,daysToDepartureDate,
                1,0,0,
                tripType,"","",optionalLocation,optionalAirline,"IDR"
        );
        String departureDate =CoreFunctions.convertToLongDate(flightDynamicData.departureDate);
        originAirport = flightDynamicData.originAirport;
        destinationAirport = flightDynamicData.destinationAirport;

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Logout via deeplink");
        homePage.logoutViaDeeplink();

        step("Change currency");
        homePage.changeCurrency("IDR","");

        step("Open flights page");
        homePage.openVerticalPageDeeplink("Flights","","");
        flightSearchPage.closePopup();

        step("Clear recent search");
        flightSearchPage.clearRecentSearch();

        step("change date using deeplink");
        flightSearchPage.changeDateViaDeeplink(flightDynamicData, tripType);

        step("Toggle trip type { " + tripType + " }");
        flightSearchPage.toggleTriptype(tripType);

        step("Select origin airport");
        flightSearchPage.selectOrigin(originAirport);
        flightSearchPage.closePopup();

        step("Select destination airport");
        flightSearchPage.selectDestination(destinationAirport);

        step("Select date");
        flightSearchPage.selectDate(departureDate);

        step("Search flight");
        flightSearchPage.searchFlight();

        step("Back to flights page");
        flightSearchResultPage.backToFlightPage();

        step("Click on recent search box");
        flightSearchPage.clickOnRecentSearch();

        step("search result page through recent search");
        boolean isOnSearchResultPage = flightSearchResultPage.isOnSearchResultPage();
        Assertion.assertThat("verify we are on search result page through recent search",isOnSearchResultPage,is(true));
    }

    @Author(names = {"Zia"})
    @Module(name = "FlightLoyaltyPlatinum")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test(dataProvider="testFlightOneWayLoyaltyPlatinumLoginDataProvider")
    public void testFlightOneWayLoyaltyPlatinumLogin(Map<String, String> param) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightProductReviewPage<WebDriver> flightProductReviewPage = FlightProductReviewPage.getInstance(session);
        FlightBookingFormPage<WebDriver> flightBookingFormPage = FlightBookingFormPage.getInstance(session);
        FlightPage<WebDriver> flightPage = FlightPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        FlightApi<?> flightApi = new FlightApi<>(session);

        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        String airline = param.get("airline");

        String optionalLocation = param.get("optionalLocation");
        String optionalAirline = param.get("optionalAirline");

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");

        String currency = param.get("currency");

        step("Find dynamic data");
        FlightDynamicData flightDynamicData = flightApi.findDate(
                username,password,
                originAirport,destinationAirport,daysToDepartureDate,
                adultPassengers,childPassengers,infantPassengers,
                tripType,"","platinum",optionalLocation,optionalAirline,currency
        );
        String departureDate =CoreFunctions.convertToLongDate(flightDynamicData.departureDate);
        airline = flightDynamicData.airlineDepart;
        originAirport = flightDynamicData.originAirport;
        destinationAirport = flightDynamicData.destinationAirport;

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink and change currency to " + currency);
        homePage.loginWithSpecificAccountViaDeeplink(username, password, currency);

        step("Open flights page");
        homePage.openVerticalPageDeeplink("Flights","",currency);
        flightSearchPage.closePopup();

        step("change date using deeplink");
        flightSearchPage.changeDateViaDeeplink(flightDynamicData, tripType);

        step("Toggle trip type { " + tripType + " }");
        flightSearchPage.toggleTriptype(tripType);

        step("Select origin airport");
        flightSearchPage.selectOrigin(originAirport);
        flightSearchPage.closePopup();

        step("Select destination airport");
        flightSearchPage.selectDestination(destinationAirport);

        step("Select date");
        flightSearchPage.selectDate(departureDate);

        step("Add passenger");
        flightSearchPage.addPassenger(adultPassengers,childPassengers,infantPassengers);

        step("Search flight");
        flightSearchPage.searchFlight();

        step("Open filter");
        flightSearchResultPage.openFilter();

        step("Filter airline");
        flightSearchResultPage.filterAirline(airline);

        step("Save filter");
        flightSearchResultPage.saveFilter();

        step("Verify loyalty discount tag");
        Assertion.assertThat("Is loyalty discount tag showing", flightSearchResultPage.verifyDiscountTag(), is(true));

        step("Click flight detail chevron");
        flightSearchResultPage.selectFlightSpecialCampaign(airline);

        step("Continue to booking form");
        flightProductReviewPage.continueToBookingForm(tripType);

        step("Skip full protection if visible");
        flightBookingFormPage.skipFullProtectionPrp();

        step("verify date format");
        flightBookingFormPage.verifyDateFormat(tripType);

        step("Fill passenger detail");
        flightBookingFormPage.fillPassengerDetail(adultPassengers,childPassengers,infantPassengers, "domestic",airline);

        step("Continue to second half booking form");
        flightBookingFormPage.continueToSecondHalf();

        step("Verify loyalty detail");
        Assertion.assertThat("Is what you'll get correct", flightBookingFormPage.isWhatYouGetCorrect("Platinum"), is(true));

        step("Open price detail");
        flightBookingFormPage.openPriceDetail();

        step("Verify price detail");
        PriceDetail priceDetail = new PriceDetail();

        priceDetail.tripType=tripType;
        priceDetail.adult=adultPassengers;
        priceDetail.child=childPassengers;
        priceDetail.infant=infantPassengers;

        Assertion.assertThat("Is price breakdown correct", flightBookingFormPage.isPriceBreakdownCorrect(priceDetail,currency), is(true));

        step("Continue to payment");
        flightBookingFormPage.continueToPayment();

        step("Skip full protection");
        flightBookingFormPage.skipFullProtection();
        flightBookingFormPage.baggageAllowancePopup();

        step("Check order ID");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",!orderID.isBlank(),is(true));
    }

    @Author(names = {"Budi"})
    @Module(name = "FlightLoyaltyDiamond")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test(dataProvider="testFlightRTLoyaltyDiamondLoginDataProvider")
    public void testFlightRTLoyaltyDiamondLogin(Map<String, String> param) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightProductReviewPage<WebDriver> flightProductReviewPage = FlightProductReviewPage.getInstance(session);
        FlightBookingFormPage<WebDriver> flightBookingFormPage = FlightBookingFormPage.getInstance(session);
        FlightPage<WebDriver> flightPage = FlightPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        FlightApi<?> flightApi = new FlightApi<>(session);

        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");
        String destinationWithoutAirportCode = destinationAirport.split("/")[1];

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        int duration = Integer.parseInt(param.get("duration"));
        String airline = param.get("airline");
        String quickFilter = param.get("otherFilter");

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String optionalLocation = param.get("optionalLocation");
        String optionalAirline = param.get("optionalAirline");
        String passengerType = param.get("passengerType");
        String currency = param.get("currency");

        step("Find dynamic data");
        FlightDynamicData flightDynamicData = flightApi.findDateRT(
                username,password,
                originAirport,destinationWithoutAirportCode,daysToDepartureDate,duration,
                adultPassengers,childPassengers,infantPassengers,
                tripType,"", "","diamond", "diamond",optionalLocation, optionalAirline,currency
        );
        String departureDate = CoreFunctions.convertToLongDate(flightDynamicData.departureDate);
        String returnDate = CoreFunctions.convertToLongDate(flightDynamicData.returnDate);
        airline = flightDynamicData.airlineDepart;
        String returnAirline = flightDynamicData.airlineReturn;
        originAirport = flightDynamicData.originAirport;
        destinationAirport = flightDynamicData.destinationAirport;

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink and change currency to " + currency);
        homePage.loginWithSpecificAccountViaDeeplink(username, password, currency);

        step("Open flights page");
        homePage.openVerticalPageDeeplink("Flights","",currency);
        flightSearchPage.closePopup();

        step("change date using deeplink");
        flightSearchPage.changeDateViaDeeplink(flightDynamicData, tripType);

        step("Toggle trip type { " + tripType + " }");
        flightSearchPage.toggleTriptype(tripType);

        step("Select origin airport");
        flightSearchPage.selectOrigin(originAirport);
        flightSearchPage.closePopup();

        step("Select destination airport");
        flightSearchPage.selectDestination(destinationAirport);

        step("Select date");
        flightSearchPage.selectDate(departureDate,returnDate);

        step("Add passenger");
        flightSearchPage.addPassenger(adultPassengers,childPassengers,infantPassengers);

        step("Search flight");
        flightSearchPage.searchFlight();

        if(Configs.ENVIRONMENT == Environment.GK || Configs.ENVIRONMENT == Environment.PREPROD) {
            step("Open filter");
            flightSearchResultPage.openFilter();

            step("Filter airline");
            flightSearchResultPage.filterAirline(airline);

            step("Save filter");
            flightSearchResultPage.saveFilter();
        }else {
            step("filter elite diamond");
            flightSearchResultPage.filterChips(quickFilter);
        }

        step("Verify loyalty banner");
        //Assertion.assertThat("Is loyalty banner showing", flightSearchResultPage.verifyLoyaltyBanner("Diamond"), is(true)); -> reference: https://tiket.slack.com/archives/C012AEB4EKT/p1710324149562659?thread_ts=1710321998.756889&cid=C012AEB4EKT
        Assertion.assertThat("Is loyalty discount tag showing", flightSearchResultPage.verifyDiscountTag(), is(true));

        step("Click departure flight detail chevron");
        flightSearchResultPage.selectFlight();

        if(Configs.ENVIRONMENT == Environment.GK || Configs.ENVIRONMENT == Environment.PREPROD) {
            step("Open filter");
            flightSearchResultPage.openFilter();

            step("Filter airline");
            flightSearchResultPage.filterAirline(returnAirline);

            step("Save filter");
            flightSearchResultPage.saveFilter();
        }else {
            step("filter elite diamond");
            flightSearchResultPage.filterChips(quickFilter);
        }

        step("Click return flight detail chevron");
        flightSearchResultPage.selectFlight();

        step("Verify loyalty banner");
        //Assertion.assertThat("Is loyalty banner showing", flightSearchResultPage.verifyLoyaltyBanner("Diamond"), is(true)); -> reference: https://tiket.slack.com/archives/C012AEB4EKT/p1710324149562659?thread_ts=1710321998.756889&cid=C012AEB4EKT
        Assertion.assertThat("Is loyalty discount tag showing", flightSearchResultPage.verifyDiscountTag(), is(true));

        step("Continue to booking form");
        flightProductReviewPage.continueToBookingForm(tripType);

        step("Skip full protection if visible");
        flightBookingFormPage.skipFullProtectionPrp();

        step("verify date format");
        flightBookingFormPage.verifyDateFormat(tripType);

        step("Fill passenger detail");
        flightBookingFormPage.fillPassengerDetail(adultPassengers,childPassengers,infantPassengers,passengerType,airline);

        step("Continue to second half booking form");
        flightBookingFormPage.continueToSecondHalf();

        step("Verify loyalty detail");
        Assertion.assertThat("Is what you'll get correct", flightBookingFormPage.isWhatYouGetCorrect("Diamond"), is(true));

        step("Open price detail");
        flightBookingFormPage.openPriceDetail();

        step("Verify price detail roundtrip");
        PriceDetail priceDetail = new PriceDetail();

        priceDetail.tripType=tripType;
        priceDetail.adult=adultPassengers;
        priceDetail.child=childPassengers;
        priceDetail.infant=infantPassengers;

        Assertion.assertThat("Is price breakdown correct", flightBookingFormPage.isPriceBreakdownCorrect(priceDetail,currency), is(true));

        step("Continue to payment");
        flightBookingFormPage.continueToPayment();

        step("Skip full protection");
        flightBookingFormPage.skipFullProtection();
        flightBookingFormPage.baggageAllowancePopup();

        step("Check order ID");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",!orderID.isBlank(),is(true));
    }

    @Author(names = {"Ashish"})
    @Module(name = "FlightDomesticOneWayDiscountedPrice")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test(dataProvider="testFlightOneWayGuestWithDiscountedPriceDataProvider")
    public void testFlightOneWayGuestWithDiscountedPrice(Map<String, String> param) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightProductReviewPage<WebDriver> flightProductReviewPage = FlightProductReviewPage.getInstance(session);
        FlightBookingFormPage<WebDriver> flightBookingFormPage = FlightBookingFormPage.getInstance(session);
        FlightPage<WebDriver> flightPage = FlightPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        FlightApi<?> flightApi = new FlightApi<>(session);

        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        String airline = param.get("airline");
        String email = param.get("email");
        String phoneNumber = param.get("phoneNumber");
        String currency = param.get("currency");
        String optionalLocation = param.get("optionalLocation");
        String optionalAirline = param.get("optionalAirline");

        PriceDetail priceDetail = new PriceDetail();
        priceDetail.tripType=tripType;
        priceDetail.adult=adultPassengers;
        priceDetail.child=childPassengers;
        priceDetail.infant=infantPassengers;

        step("Find dynamic data");
        FlightDynamicData flightDynamicData = flightApi.findDate(
                "","",
                originAirport,destinationAirport,daysToDepartureDate,
                adultPassengers,childPassengers,infantPassengers,
                tripType,"","discounted price",optionalLocation,optionalAirline,currency
        );
        String departureDate =CoreFunctions.convertToLongDate(flightDynamicData.departureDate);
        airline = flightDynamicData.airlineDepart;
        originAirport = flightDynamicData.originAirport;
        destinationAirport = flightDynamicData.destinationAirport;

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Logout via deeplink");
        homePage.logoutViaDeeplink();

        step("Change currency");
        homePage.changeCurrency(currency,"");

        step("Open flights page");
        homePage.openVerticalPageDeeplink("Flights","",currency);
        flightSearchPage.closePopup();

        step("change date using deeplink");
        flightSearchPage.changeDateViaDeeplink(flightDynamicData, tripType);

        step("Toggle trip type { " + tripType + " }");
        flightSearchPage.toggleTriptype(tripType);

        step("Select origin airport");
        flightSearchPage.selectOrigin(originAirport);
        flightSearchPage.closePopup();

        step("Select destination airport");
        flightSearchPage.selectDestination(destinationAirport);

        step("Select date");
        flightSearchPage.selectDate(departureDate);

        step("Add passenger");
        flightSearchPage.addPassenger(adultPassengers,childPassengers,infantPassengers);

        step("Search flight");
        flightSearchPage.searchFlight();

        step("Open filter");
        flightSearchResultPage.openFilter();

        step("Filter airline");
        flightSearchResultPage.filterAirline(airline);

        step("Save filter");
        flightSearchResultPage.saveFilter();

        step("Schedule airline with Discounted Price");
        boolean isDiscountedAirline = flightSearchResultPage.isAirlineWithDiscountedPrice();
        Assertion.assertThat("Is schedule airline with Discounted Price", isDiscountedAirline, is(true));

        step("Click flight detail chevron");
        flightSearchResultPage.selectFlight();

        step("Continue to booking form");
        flightProductReviewPage.continueToBookingForm(tripType);

        step("Skip full protection if visible");
        flightBookingFormPage.skipFullProtectionPrp();

        step("verify date format");
        flightBookingFormPage.verifyDateFormat(tripType);

        step("Fill contact detail");
        flightBookingFormPage.fillContactDetail(phoneNumber,email,"guest",airline);

        step("Fill passenger detail");
        flightBookingFormPage.fillPassengerDetail(adultPassengers,childPassengers,infantPassengers,"domestic",airline);

        step("Continue to second half booking form");
        flightBookingFormPage.continueToSecondHalf();

        step("Open price detail");
        flightBookingFormPage.openPriceDetail();

        step("Verify price detail");
        boolean discountTagDisplayed = flightBookingFormPage.isDiscountTagDisplayed();
        Assertion.assertThat("Is discount tag present in booking page price detail", discountTagDisplayed, is(true));
        Assertion.assertThat("Is price breakdown correct", flightBookingFormPage.isPriceBreakdownCorrect(priceDetail,currency), is(true));

        step("Continue to payment");
        flightBookingFormPage.continueToPayment();

        step("Skip full protection");
        flightBookingFormPage.skipFullProtection();
        flightBookingFormPage.baggageAllowancePopup();

        step("Check order ID");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",!orderID.isBlank(),is(true));
    }

    @Author(names = {"Budi","Ashish"})
    @Module(name = "testFlightOneWayLoginWithFinalPrice")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test(dataProvider="testFlightOneWayLoginWithFinalPriceDataProvider")
    public void testFlightOneWayLoginWithFinalPrice(Map<String, String> param) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightProductReviewPage<WebDriver> flightProductReviewPage = FlightProductReviewPage.getInstance(session);
        FlightBookingFormPage<WebDriver> flightBookingFormPage = FlightBookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        FlightApi<?> flightApi = new FlightApi<>(session);

        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        String airline = param.get("airline");
        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String optionalLocation = param.get("optionalLocation");
        String optionalAirline = param.get("optionalAirline");

        String currency = param.get("currency");

        PriceDetail priceDetail = new PriceDetail();
        priceDetail.tripType=tripType;
        priceDetail.adult=adultPassengers;
        priceDetail.child=childPassengers;
        priceDetail.infant=infantPassengers;

        step("Find dynamic data");
        FlightDynamicData flightDynamicData= flightApi.findDate(
                username,password,
                originAirport,destinationAirport,daysToDepartureDate,
                adultPassengers,childPassengers,infantPassengers,
                tripType,airline,"",optionalLocation,optionalAirline,currency
        );
        String departureDate =CoreFunctions.convertToLongDate(flightDynamicData.departureDate);
        airline = flightDynamicData.airlineDepart;
        originAirport = flightDynamicData.originAirport;
        destinationAirport = flightDynamicData.destinationAirport;

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink and change currency to " + currency);
        homePage.loginWithSpecificAccountViaDeeplink(username, password, currency);

        step("Open flights page");
        homePage.openVerticalPageDeeplink("Flights","",currency);
        flightSearchPage.closePopup();

        step("change date using deeplink");
        flightSearchPage.changeDateViaDeeplink(flightDynamicData, tripType);

        step("Toggle trip type { " + tripType + " }");
        flightSearchPage.toggleTriptype(tripType);

        step("Select origin airport");
        flightSearchPage.selectOrigin(originAirport);
        flightSearchPage.closePopup();

        step("Select destination airport");
        flightSearchPage.selectDestination(destinationAirport);

        step("Select date");
        flightSearchPage.selectDate(departureDate);

        step("Add passenger");
        flightSearchPage.addPassenger(adultPassengers,childPassengers,infantPassengers);

        step("Search flight");
        flightSearchPage.searchFlight();

        step("Open filter");
        flightSearchResultPage.openFilter();

        step("Filter airline");
        flightSearchResultPage.filterAirline(airline);

        step("Save filter");
        flightSearchResultPage.saveFilter();

        step("Verify no adjustments on flight search result");
        Assertion.assertThat("Is flight adjustment showing", flightSearchResultPage.isAdjustmentPresentSearch(), is(false));

        step("Click flight detail chevron");
        flightSearchResultPage.selectFlight();

        step("Continue to booking form");
        flightProductReviewPage.continueToBookingForm(tripType);

        step("Skip full protection if visible");
        flightBookingFormPage.skipFullProtectionPrp();

        step("verify date format");
        flightBookingFormPage.verifyDateFormat(tripType);

        step("Fill passenger detail");
        flightBookingFormPage.fillPassengerDetail(adultPassengers,childPassengers,infantPassengers,"international",airline);

        step("Continue to second half booking form");
        flightBookingFormPage.continueToSecondHalf();

        step("remove extra protection if any");
        flightBookingFormPage.removeExtraProtection();

        step("Verify no adjustment on flight booking");
        Assertion.assertThat("Is flight adjustment showing", flightBookingFormPage.isAdjustmentPresentInBooking(), is(false));

        step("Open price detail");
        flightBookingFormPage.openPriceDetail();

        step("Verify no campaign or loyalty show");
        Assertion.assertThat("Is flight adjustment showing", flightBookingFormPage.isAdjustmentPresentInBooking(), is(false));
        Assertion.assertThat("Is ticket price the same as adult price", flightBookingFormPage.isTotalPriceWithoutDiscount(adultPassengers), is(true));

        step("Continue to payment");
        flightBookingFormPage.continueToPayment();

        step("Skip full protection");
        flightBookingFormPage.skipFullProtection();
        flightBookingFormPage.baggageAllowancePopup();

        step("Check order ID");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",!orderID.isBlank(),is(true));
    }

    @Author(names = {"Ashish"})
    @Module(name = "FlightDomesticOneWaySpecialCampaign")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test(dataProvider="testFlightOneWayLoginWithSpecialCampaignDataProvider")
    public void testFlightOneWayLoginWithSpecialCampaign(Map<String, String> param) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightProductReviewPage<WebDriver> flightProductReviewPage = FlightProductReviewPage.getInstance(session);
        FlightBookingFormPage<WebDriver> flightBookingFormPage = FlightBookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        FlightApi<?> flightApi = new FlightApi<>(session);

        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        String airline = param.get("airline");
        String label = param.get("label");

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String quickFilter = param.get("otherFilter");
        String currency = param.get("currency");
        String optionalLocation = param.get("optionalLocation");
        String optionalAirline = param.get("optionalAirline");

        PriceDetail priceDetail = new PriceDetail();
        priceDetail.tripType=tripType;
        priceDetail.adult=adultPassengers;
        priceDetail.child=childPassengers;
        priceDetail.infant=infantPassengers;

        step("Find dynamic data");
        FlightDynamicData flightDynamicData = flightApi.findDate(
                username,password,
                originAirport,destinationAirport,daysToDepartureDate,
                adultPassengers,childPassengers,infantPassengers,
                tripType,"","harga gledek",optionalLocation,optionalAirline,currency
        );
        String departureDate =CoreFunctions.convertToLongDate(flightDynamicData.departureDate);
        airline = flightDynamicData.airlineDepart;
        originAirport = flightDynamicData.originAirport;
        destinationAirport = flightDynamicData.destinationAirport;

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink (if needed) and change currency to " + currency);
        homePage.loginIfLoggedOutAndChangeCurrencyViaDeeplink(username, password, currency);

        step("Open flights page");
        homePage.openVerticalPageDeeplink("Flights","",currency);
        flightSearchPage.closePopup();

        step("change date using deeplink");
        flightSearchPage.changeDateViaDeeplink(flightDynamicData, tripType);

        step("Toggle trip type { " + tripType + " }");
        flightSearchPage.toggleTriptype(tripType);

        step("Select origin airport");
        flightSearchPage.selectOrigin(originAirport);
        flightSearchPage.closePopup();

        step("Select destination airport");
        flightSearchPage.selectDestination(destinationAirport);

        step("Select date");
        flightSearchPage.selectDate(departureDate);

        step("Add passenger");
        flightSearchPage.addPassenger(adultPassengers,childPassengers,infantPassengers);

        step("Search flight");
        flightSearchPage.searchFlight();

        if(Configs.ENVIRONMENT == Environment.GK || Configs.ENVIRONMENT == Environment.PREPROD) {
            step("Open filter");
            flightSearchResultPage.openFilter();

            step("Filter airline");
            flightSearchResultPage.filterAirline(airline);

            step("Save filter");
            flightSearchResultPage.saveFilter();
        }else {
            step("filter Harga Gledek Testing deals");
            flightSearchResultPage.filterChips(quickFilter);
        }

        step("see Schedule Airline with Special Campaign ("+label+") on Card");
        boolean isLabelDisplayedOnCard = flightSearchResultPage.checkLabelOnCardSRP(label);
        //boolean isLabelDiscountDisplay = flightSearchResultPage.checkLabelOnCardSRP("discount"); https://tiket.slack.com/archives/C012AEB4EKT/p1706257786715069
        Assertion.assertThat("Is special campaign label display on the card", isLabelDisplayedOnCard, is(true));
        //Assertion.assertThat("Is Discount label display on the card", isLabelDiscountDisplay, is(true)); https://tiket.slack.com/archives/C012AEB4EKT/p1706257786715069
        // For special-campaign fares the SRP card renders the savings delta as
        // "Save IDR <N>" (EN) / "Hemat IDR <N>" (ID), separate from the regular fare price.
        // This savings figure is what must equal the booking-form breakdown's
        // (Discount + Tix Points Cashback) sum during downstream verification.
        String discountedPrice = flightSearchResultPage.getSpecialCampaignSavingsPrice();

        step("Click flight detail chevron");
        flightSearchResultPage.selectFlight();

        step("Continue to booking form");
        flightProductReviewPage.continueToBookingForm(tripType);

        step("Skip full protection if visible");
        flightBookingFormPage.skipFullProtectionPrp();

        step("verify date format");
        flightBookingFormPage.verifyDateFormat(tripType);

        step("Fill passenger detail");
        flightBookingFormPage.fillPassengerDetail(adultPassengers,childPassengers,infantPassengers,"international",airline);

        step("Continue to second half booking form");
        flightBookingFormPage.continueToSecondHalf();

        step("Verify what you get detail");
        Assertion.assertThat("Is what you'll get correct", flightBookingFormPage.isWhatYouGetCorrect(label), is(true));

        step("Open price detail");
        flightBookingFormPage.openPriceDetail();

        step("Verify price detail");
        priceDetail.specialCampaignPrice = discountedPrice;
        boolean specialCampaignBreakdown = flightBookingFormPage.offerBreakdown(label,priceDetail);
        Assertion.assertThat("Is special campaign tag and price correct", specialCampaignBreakdown, is(true));
        Assertion.assertThat("Is price breakdown correct", flightBookingFormPage.isPriceBreakdownCorrect(priceDetail,currency), is(true));

        step("Continue to payment");
        flightBookingFormPage.continueToPayment();

        step("Skip full protection");
        flightBookingFormPage.skipFullProtection();
        flightBookingFormPage.baggageAllowancePopup();

        step("Check order ID");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",!orderID.isBlank(),is(true));
    }

    @Author(names = {"Zia"})
    @Module(name = "FlightNegativeCase")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test(dataProvider="testFlightLoginInternationalWithoutAdjustmentSRTDataProvider")
    public void testFlightLoginInternationalWithoutAdjustmentSRT(Map<String, String> param) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightApi<?> flightApi = new FlightApi<>(session);

        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate,daysToDepartureDate);
        int duration = Integer.parseInt(param.get("duration"));
        String returnDate = CoreFunctions.addToLongDate(departureDate,duration);
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        String otherFilter = param.get("otherFilter");
        String currency = param.get("currency");
        String optionalLocation = param.get("optionalLocation");
        String optionalAirline = param.get("optionalAirline");

        step("Find dynamic data");
        FlightDynamicData flightDynamicData = flightApi.findDateRT(
                username,password,
                originAirport,destinationAirport,daysToDepartureDate,duration,
                adultPassengers,childPassengers,infantPassengers,
                tripType,"","",otherFilter,otherFilter,optionalLocation,optionalAirline,currency
        );
        departureDate = CoreFunctions.convertToLongDate(flightDynamicData.departureDate);
        originAirport = flightDynamicData.originAirport;
        destinationAirport = flightDynamicData.destinationAirport;
        String airline = flightDynamicData.airlineDepart;

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink (if needed) and change currency to " + currency);
        homePage.loginIfLoggedOutAndChangeCurrencyViaDeeplink(username, password, currency);

        step("Open flights page");
        homePage.openVerticalPageDeeplink("Flights","", currency);
        flightSearchPage.closePopup();

        step("change date using deeplink");
        flightSearchPage.changeDateViaDeeplink(flightDynamicData, tripType);

        step("Toggle trip type { " + tripType + " }");
        flightSearchPage.toggleTriptype(tripType);

        step("Select origin airport");
        flightSearchPage.selectOrigin(originAirport);
        flightSearchPage.closePopup();

        step("Select destination airport");
        flightSearchPage.selectDestination(destinationAirport);

        step("Select date");
        flightSearchPage.selectDate(departureDate,returnDate);

        step("Add passenger");
        flightSearchPage.addPassenger(adultPassengers,childPassengers,infantPassengers);

        step("Search flight");
        flightSearchPage.searchFlight();

        if(Configs.ENVIRONMENT == Environment.GK || Configs.ENVIRONMENT == Environment.PREPROD) {
            step("Open filter");
            flightSearchResultPage.openFilter();

            step("Filter airline");
            flightSearchResultPage.filterAirline(airline);

            step("Save filter");
            flightSearchResultPage.saveFilter();
        }else {
            step("filter round-trip deals");
            flightSearchResultPage.filterChips(otherFilter);
        }

        step("Verify no adjustments on flight search result");
        Assertion.assertThat("Is flight adjustment showing", flightSearchResultPage.isAdjustmentPresentSearch(), is(false));

        step("open and verify price and policy(discount)");
        flightSearchResultPage.clickSeeDetailSrp(1);

        step("Verify no adjustments on flight search detail");
        Assertion.assertThat("Is flight adjustment showing", flightSearchResultPage.isAdjustmentPresentDetail(), is(false));

        step("Click flight detail chevron");
        flightSearchResultPage.selectFlight();

        step("Verify no adjustments on flight search result return");
        Assertion.assertThat("Is flight adjustment showing", flightSearchResultPage.isAdjustmentPresentSearch(), is(false));

        step("open and verify price and policy(discount)");
        flightSearchResultPage.clickSeeDetailSrp(2);

        step("Verify no adjustments on flight search detail return");
        Assertion.assertThat("Is flight adjustment showing", flightSearchResultPage.isAdjustmentPresentDetail(), is(false));
    }

    @Author(names = {"Zia"})
    @Module(name = "FlightLoginLoyaltyAndSpecialCampaign")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test(dataProvider="testFlightLoginLoyaltyAndSpecialCampaignDataProvider")
    public void testFlightLoginLoyaltyAndSpecialCampaign(Map<String, String> param) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightProductReviewPage<WebDriver> flightProductReviewPage = FlightProductReviewPage.getInstance(session);
        FlightPage<WebDriver> flightPage = FlightPage.getInstance(session);
        FlightBookingFormPage<WebDriver> flightBookingFormPage = FlightBookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        FlightApi<?> flightApi = new FlightApi<>(session);

        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        String airline = param.get("airline");
        String airlineReturn = param.get("airlineReturn");
        int duration = Integer.parseInt(param.get("duration"));
        String label = param.get("label");

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String optionalLocation = param.get("optionalLocation");
        String optionalAirline = param.get("optionalAirline");
        String currency = param.get("currency");

        PriceDetail priceDetail = new PriceDetail();
        priceDetail.tripType=tripType;
        priceDetail.adult=adultPassengers;
        priceDetail.child=childPassengers;
        priceDetail.infant=infantPassengers;

        step("Find dynamic data");
        FlightDynamicData flightDynamicData = flightApi.findDateRT(
                username,password,
                originAirport,destinationAirport,daysToDepartureDate,duration,
                adultPassengers,childPassengers,infantPassengers,
                tripType,"","","platinum","harga gledek",optionalLocation,optionalAirline,currency
        );
        String departureDate = CoreFunctions.convertToLongDate(flightDynamicData.departureDate);
        String returnDate = CoreFunctions.convertToLongDate(flightDynamicData.returnDate);
        airline = flightDynamicData.airlineDepart;
        airlineReturn = flightDynamicData.airlineReturn;
        originAirport = flightDynamicData.originAirport;
        destinationAirport = flightDynamicData.destinationAirport;

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink and change currency to " + currency);
        homePage.loginWithSpecificAccountViaDeeplink(username, password, currency);

        step("Open flights page");
        homePage.openVerticalPageDeeplink("Flights","",currency);
        flightSearchPage.closePopup();

        step("change date using deeplink");
        flightSearchPage.changeDateViaDeeplink(flightDynamicData, tripType);

        step("Toggle trip type { " + tripType + " }");
        flightSearchPage.toggleTriptype(tripType);

        step("Select origin airport");
        flightSearchPage.selectOrigin(originAirport);
        flightSearchPage.closePopup();

        step("Select destination airport");
        flightSearchPage.selectDestination(destinationAirport);

        step("Select date");
        flightSearchPage.selectDate(departureDate,returnDate);

        step("Add passenger");
        flightSearchPage.addPassenger(adultPassengers,childPassengers,infantPassengers);

        step("Search flight");
        flightSearchPage.searchFlight();

        step("Open filter");
        flightSearchResultPage.openFilter();

        step("Filter airline");
        flightSearchResultPage.filterAirline(airline);

        step("Save filter");
        flightSearchResultPage.saveFilter();

        step("Verify loyalty banner");
        Assertion.assertThat("Is loyalty discount tag showing", flightSearchResultPage.verifyDiscountTag(), is(true));

        step("Click flight detail chevron");
        flightSearchResultPage.selectFlightSpecialCampaign(airline);

        step("Open filter");
        flightSearchResultPage.openFilter();

        step("Filter airline");
        flightSearchResultPage.filterAirline(airlineReturn);

        step("Save filter");
        flightSearchResultPage.saveFilter();

        step("see Schedule Airline with Special Campaign ("+label+") on Card");
        boolean isLabelDisplayedOnCard = flightSearchResultPage.checkLabelOnCardSRP(label);
        Assertion.assertThat("Is special campaign label display on the card", isLabelDisplayedOnCard, is(true));

        step("Click flight detail chevron");
        flightSearchResultPage.selectFlightSpecialCampaign(airlineReturn);

        step("Verify loyalty discount price");
        flightProductReviewPage.clickDetailPRP();
//        flightSearchResultPage.clickPricePolicyTab();
        Assertion.assertThat("Is price sum correct", flightSearchResultPage.isPriceSumCorrect(), is(true));
        Assertion.assertThat("Is platinum discount showing on price detail", flightSearchResultPage.isWhatYouGetCorrect("Platinum"), is(true));

        step("Continue to booking form");
        flightProductReviewPage.continueToBookingForm(tripType);

        step("Skip full protection if visible");
        flightBookingFormPage.skipFullProtectionPrp();

        step("verify date format");
        flightBookingFormPage.verifyDateFormat(tripType);

        step("Fill passenger detail");
        flightBookingFormPage.fillPassengerDetail(adultPassengers,childPassengers,infantPassengers,"domestic",airline);

        step("Continue to second half booking form");
        flightBookingFormPage.continueToSecondHalf();

        step("remove insurance/extra protection");
        flightBookingFormPage.removeExtraProtectionAll();

//        step("Verify what you get detail");
//        Assertion.assertThat("Is what you'll get correct", flightBookingFormPage.isWhatYouGetCorrect(label), is(true));

        step("Verify loyalty detail");
        Assertion.assertThat("Is what you'll get correct", flightBookingFormPage.isWhatYouGetCorrect("Platinum"), is(true));

        step("Open price detail");
        flightBookingFormPage.openPriceDetail();

//        step("Verify special campaign discount");
//        boolean specialCampaignBreakdown = flightBookingFormPage.offerBreakdown(label,priceDetail);
//        Assertion.assertThat("Is special campaign tag and price correct", specialCampaignBreakdown, is(true));

        step("Verify price breakdown");
        Assertion.assertThat("Is price breakdown correct", flightBookingFormPage.isPriceBreakdownCorrect(priceDetail,currency), is(true));

        step("Continue to payment");
        flightBookingFormPage.continueToPayment();

        step("Skip full protection");
        flightBookingFormPage.skipFullProtection();
        flightBookingFormPage.baggageAllowancePopup();

        step("Check order ID");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",!orderID.isBlank(),is(true));
    }

    @Author(names = {"Ashish"})
    @Module(name = "FlightNegativeCase")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test(dataProvider="testFlightLoginDomesticWithoutAdjustmentRTSDataProvider")
    public void testFlightLoginDomesticWithoutAdjustmentRTS(Map<String, String> param) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightApi<?> flightApi = new FlightApi<>(session);

        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate,daysToDepartureDate);
        int duration = Integer.parseInt(param.get("duration"));
        String returnDate = CoreFunctions.addToLongDate(departureDate,duration);
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        String airline = param.get("airline");
        String optionalLocation = param.get("optionalLocation");
        String optionalAirline = param.get("optionalAirline");
        String currency = param.get("currency");

        step("Find dynamic data");
        FlightDynamicData flightDynamicData = flightApi.findDateRT(
                username,password,
                originAirport,destinationAirport,daysToDepartureDate,duration,
                adultPassengers,childPassengers,infantPassengers,
                tripType,"","","","",optionalLocation,optionalAirline,currency
        );
        departureDate =CoreFunctions.convertToLongDate(flightDynamicData.departureDate);
        returnDate = CoreFunctions.convertToLongDate(flightDynamicData.returnDate);
        airline = flightDynamicData.airlineDepart;
        originAirport = flightDynamicData.originAirport;
        destinationAirport = flightDynamicData.destinationAirport;

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink (if needed) and change currency to " + currency);
        homePage.loginIfLoggedOutAndChangeCurrencyViaDeeplink(username, password, currency);

        step("Open flights page");
        homePage.openVerticalPageDeeplink("Flights","",currency);
        flightSearchPage.closePopup();

        step("change date using deeplink");
        flightSearchPage.changeDateViaDeeplink(flightDynamicData, tripType);

        step("Toggle trip type { " + tripType + " }");
        flightSearchPage.toggleTriptype(tripType);

        step("Select origin airport");
        flightSearchPage.selectOrigin(originAirport);
        flightSearchPage.closePopup();

        step("Select destination airport");
        flightSearchPage.selectDestination(destinationAirport);

        step("Select date");
        flightSearchPage.selectDate(departureDate,returnDate);

        step("Add passenger");
        flightSearchPage.addPassenger(adultPassengers,childPassengers,infantPassengers);

        step("Search flight");
        flightSearchPage.searchFlight();

        step("Open filter");
        flightSearchResultPage.openFilter();

        step("Filter airline");
        flightSearchResultPage.filterAirline(airline);

        step("Save filter");
        flightSearchResultPage.saveFilter();

        step("Verify no adjustments on flight search result");
        Assertion.assertThat("Is flight adjustment showing", flightSearchResultPage.isAdjustmentPresentSearch(), is(false));

        step("Click flight detail chevron");
        flightSearchResultPage.selectFlight();

        step("open and verify price and policy(discount)");
        flightSearchResultPage.clickSeeDetailSrp(2);

        step("Verify no adjustments on flight search detail");
        Assertion.assertThat("Is flight adjustment showing", flightSearchResultPage.isAdjustmentPresentDetail(), is(false));

        step("Open filter");
        flightSearchResultPage.openFilter();

        step("Filter airline");
        flightSearchResultPage.filterAirline(airline);

        step("Save filter");
        flightSearchResultPage.saveFilter();

        step("Verify no adjustments on flight search result return");
        Assertion.assertThat("Is flight adjustment showing", flightSearchResultPage.isAdjustmentPresentSearch(), is(false));

    }

    @Author(names = {"Ashish"})
    @Module(name = "refund100pLogin")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test(dataProvider="testFlight100pRefundLoginDataProvider")
    public void testFlight100pRefundLogin(Map<String, String> param) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightProductReviewPage<WebDriver> flightProductReviewPage = FlightProductReviewPage.getInstance(session);
        FlightBookingFormPage<WebDriver> flightBookingFormPage = FlightBookingFormPage.getInstance(session);
        FlightPage<WebDriver> flightPage = FlightPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        FlightApi<?> flightApi = new FlightApi<>(session);

        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        int baggageAmount = Integer.parseInt(param.get("baggage"));
        String airline = param.get("airline");
        String label = param.get("label");
        int index = Integer.parseInt(param.get("index"));
        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String optionalLocation = param.get("optionalLocation");
        String optionalAirline = param.get("optionalAirline");
        String currency = param.get("currency");

        PriceDetail priceDetail = new PriceDetail();
        priceDetail.tripType=tripType;
        priceDetail.adult=adultPassengers;
        priceDetail.child=childPassengers;
        priceDetail.infant=infantPassengers;

        step("Find dynamic data");
        FlightDynamicData flightDynamicData = flightApi.findDate(
                username,password,
                originAirport,destinationAirport,daysToDepartureDate,
                adultPassengers,childPassengers,infantPassengers,
                tripType,airline,"100% refund/baggage",optionalLocation,optionalAirline,currency
        );
        String departureDate =CoreFunctions.convertToLongDate(flightDynamicData.departureDate);
        airline = flightDynamicData.airlineDepart;
        originAirport = flightDynamicData.originAirport;
        destinationAirport = flightDynamicData.destinationAirport;

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink (if needed) and change currency to " + currency);
        homePage.loginIfLoggedOutAndChangeCurrencyViaDeeplink(username, password, currency);

        step("Open flights page");
        homePage.openVerticalPageDeeplink("Flights","",currency);
        flightSearchPage.closePopup();

        step("change date using deeplink");
        flightSearchPage.changeDateViaDeeplink(flightDynamicData, tripType);

        step("Toggle trip type { " + tripType + " }");
        flightSearchPage.toggleTriptype(tripType);

        step("Select origin airport");
        flightSearchPage.selectOrigin(originAirport);
        flightSearchPage.closePopup();

        step("Select destination airport");
        flightSearchPage.selectDestination(destinationAirport);

        step("Select date");
        flightSearchPage.selectDate(departureDate);

        step("Add passenger");
        flightSearchPage.addPassenger(adultPassengers,childPassengers,infantPassengers);

        step("Search flight");
        flightSearchPage.searchFlight();

        step("filter 100% Refund");
        flightSearchResultPage.filterChips("100% Refund");

        step("check 100p attribute");
        boolean is100pAttributeDisplayed = flightSearchResultPage.is100pRefundDisplayed();
        Assertion.assertThat("Is 100p refund attribute displayed on srp", is100pAttributeDisplayed, is(true));

        step("Open filter");
        flightSearchResultPage.openFilter();

        step("Filter airline");
        flightSearchResultPage.filterAirline(airline);

        step("Save filter");
        flightSearchResultPage.saveFilter();

        step("Click flight detail chevron");
        flightSearchResultPage.selectFlight();

        step("swipe to 100p section");
        flightProductReviewPage.swipeToRegularSection();
        boolean is100pRefundOnPrpDisplayed = flightProductReviewPage.check100pRefundAttribute();
        Assertion.assertThat("Is 100p refund displayed on prp page", is100pRefundOnPrpDisplayed, is(true));

        step("continue to booking page using 100p");
        flightProductReviewPage.continueToBookingPageUsing100pSelectBtn();

        step("Skip full protection if visible");
        flightBookingFormPage.skipFullProtectionPrp();

        step("Fill passenger detail");
        flightBookingFormPage.fillPassengerDetail(adultPassengers,childPassengers,infantPassengers,"domestic",airline);

        step("add addons baggage");
        baggageAmount = flightBookingFormPage.addBaggage();
        String baggagePrice = flightBookingFormPage.getBaggagePrice(baggageAmount);
        flightBookingFormPage.saveAddBaggage();

        step("Continue to second half booking form");
        flightBookingFormPage.continueToSecondHalf();

        step("Add Extra Protection");
        ArrayList<String> extraProtection = flightBookingFormPage.addExtraProtection(index);

        step("Open price detail");
        flightBookingFormPage.openPriceDetail();

        step("Verify price detail");
        priceDetail.tripType=tripType;
        priceDetail.baggageDepartAmount=baggageAmount;
        priceDetail.baggageDepartPrice=baggagePrice;
        priceDetail.extraProtectionTitle = extraProtection.get(0) + " extra protection";
        priceDetail.extraProtectionPrice = extraProtection.get(1);
        boolean is100pRefundTextPresentBookingPage = flightBookingFormPage.offerBreakdown(label,priceDetail);
        Assertion.assertThat("Is 100 percent refund text displayed ", is100pRefundTextPresentBookingPage, is(true));
        Assertion.assertThat("Is price breakdown correct", flightBookingFormPage.isPriceBreakdownCorrect(priceDetail,currency), is(true));

        step("Continue to payment");
        flightBookingFormPage.continueToPayment();

        step("Skip full protection");
        flightBookingFormPage.baggageAllowancePopup();
        flightBookingFormPage.skipFullProtection();
        flightBookingFormPage.baggageAllowancePopup();

        step("Check order ID");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",!orderID.isBlank(),is(true));

        step("open price detail");
        paymentPage.openPriceDetail();
        boolean is100pRefundTextPresentPaymentPage = paymentPage.offerBreakdown();
        Assertion.assertThat("Is 100 percent refund text displayed ", is100pRefundTextPresentPaymentPage, is(true));
    }

    @Author(names = {"Ashish"})
    @Module(name = "refund100pGuest")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test(dataProvider="testFlight100pRefundGuestDataProvider")
    public void testFlight100pRefundGuest(Map<String, String> param) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightProductReviewPage<WebDriver> flightProductReviewPage = FlightProductReviewPage.getInstance(session);
        FlightPage<WebDriver> flightPage = FlightPage.getInstance(session);
        FlightApi<?> flightApi = new FlightApi<>(session);

        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        String airline = param.get("airline");
        String optionalLocation = param.get("optionalLocation");
        String optionalAirline = param.get("optionalAirline");
        String currency = param.get("currency");

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Find dynamic data");
        FlightDynamicData flightDynamicData = flightApi.findDate(
                "","",
                originAirport,destinationAirport,daysToDepartureDate,
                adultPassengers,childPassengers,infantPassengers,
                tripType,airline,"",optionalLocation,optionalAirline,currency
        );
        String departureDate =CoreFunctions.convertToLongDate(flightDynamicData.departureDate);
        originAirport = flightDynamicData.originAirport;
        destinationAirport = flightDynamicData.destinationAirport;

        step("Logout via deeplink");
        homePage.logoutViaDeeplink();

        step("Change currency");
        homePage.changeCurrency(currency,"");

        step("Open flights page");
        homePage.openVerticalPageDeeplink("Flights","",currency);
        flightSearchPage.closePopup();

        step("change date using deeplink");
        flightSearchPage.changeDateViaDeeplink(flightDynamicData, tripType);

        step("Toggle trip type { " + tripType + " }");
        flightSearchPage.toggleTriptype(tripType);

        step("Select origin airport");
        flightSearchPage.selectOrigin(originAirport);
        flightSearchPage.closePopup();

        step("Select destination airport");
        flightSearchPage.selectDestination(destinationAirport);

        step("Select date");
        flightSearchPage.selectDate(departureDate);

        step("Add passenger");
        flightSearchPage.addPassenger(adultPassengers,childPassengers,infantPassengers);

        step("Search flight");
        flightSearchPage.searchFlight();

        step("check 100p attribute");
        boolean is100pAttributeDisplayed = flightSearchResultPage.is100pRefundDisplayed();
        Assertion.assertThat("Is 100p refund attribute not displayed on srp", is100pAttributeDisplayed, is(false));

        step("Click flight detail chevron");
        flightSearchResultPage.selectFlight();

        step("swipe to 100p section");
        flightProductReviewPage.swipeToRegularSection();
        boolean is100pRefundOnPrpDisplayed = flightProductReviewPage.check100pRefundAttribute();
        Assertion.assertThat("Is 100p refund not displayed on prp page", is100pRefundOnPrpDisplayed, is(false));
    }

    @Author(names = {"Ashish"})
    @Module(name = "ValidateEmailOnBookingFormAfterChangeEmail")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test(priority=1)
    public void testValidateEmailOnBookingFormAfterChangeEmail() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testValidateEmailOnBookingFormAfterChangeEmail");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightProductReviewPage<WebDriver> flightProductReviewPage = FlightProductReviewPage.getInstance(session);
        FlightBookingFormPage<WebDriver> flightBookingFormPage = FlightBookingFormPage.getInstance(session);
        FlightPage<WebDriver> flightPage = FlightPage.getInstance(session);
        AccountCenterPage<WebDriver> accountCenterPage = AccountCenterPage.getInstance(session);
        Unm<WebDriver> unm = new Unm<>(session);
        FlightApi<?> flightApi = new FlightApi<>(session);

        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");
        String optionalLocation = param.get("optionalLocation");
        String optionalAirline = param.get("optionalAirline");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));

        step("Register new account");
        RegisterNewAccountData accountData = unm.registerNewAccountViaApiUNM();
        String email = accountData.email;
        String password = accountData.password;

        try {
            step("Find dynamic data");
            FlightDynamicData flightDynamicData = flightApi.findDate(
                    email, password,
                    originAirport, destinationAirport, daysToDepartureDate,
                    adultPassengers, childPassengers, infantPassengers,
                    tripType, "", "", optionalLocation, optionalAirline, "IDR"
            );
            String departureDate = CoreFunctions.convertToLongDate(flightDynamicData.departureDate);
            originAirport = flightDynamicData.originAirport;
            destinationAirport = flightDynamicData.destinationAirport;

            step("Wait for app to launch");
            homePage.gotoHome();

            step("Login as " + email);
            loginPage.loginWith("email", email, password);

            step("open account center");
            homePage.goToHomeDeeplink();
            homePage.openAccountCenter(email);

            step("change email");
            String newEmail = accountCenterPage.changeEmail(email);

            step("go to home");
            homePage.gotoHome();

            step("Open flights page");
            homePage.openVerticalPageDeeplink("Flights", "", "");

            step("change date using deeplink");
            flightSearchPage.changeDateViaDeeplink(flightDynamicData, tripType);

            step("Toggle trip type { " + tripType + " }");
            flightSearchPage.toggleTriptype(tripType);

            step("Select origin airport");
            flightSearchPage.selectOrigin(originAirport);
            flightSearchPage.closePopup();

            step("Select destination airport");
            flightSearchPage.selectDestination(destinationAirport);

            step("Select date");
            flightSearchPage.selectDate(departureDate);

            step("Add passenger");
            flightSearchPage.addPassenger(adultPassengers, childPassengers, infantPassengers);

            step("Search flight");
            flightSearchPage.searchFlight();

            step("Click flight detail chevron");
            flightSearchResultPage.selectFlight();

            step("Continue to booking form");
            flightProductReviewPage.continueToBookingForm(tripType);

            step("Skip full protection if visible");
            flightBookingFormPage.skipFullProtectionPrp();

            step("verify date format");
            flightBookingFormPage.verifyDateFormat(tripType);

            step("check changed email");
            boolean isEmailCorrect = flightBookingFormPage.verifyChangeEmail(newEmail);
            Assertion.assertThat("Is new email present", isEmailCorrect, is(true));
        }finally {
            step("log out");
            homePage.goToHomeDeeplink();
            loginPage.loginAsGuest();

            step("delete account");
            unm.deleteAccountViaApi(accountData.unmData);
        }
    }

    @Author(names = {"Ashish"})
    @Module(name = "FlightCheckMegaphoneIcon")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test(dataProvider="testFlightCheckMegaphoneIconLoginDataProvider")
    public void testFlightCheckMegaphoneIconLogin(Map<String, String> param) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightGeneralTravelInformationPage<WebDriver> flightGeneralTravelInformationPage = FlightGeneralTravelInformationPage.getInstance(session);
        FlightPage<WebDriver> flightPage = FlightPage.getInstance(session);

        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");
        String currency = param.get("currency");
        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Find dynamic data");
        flightPage.setDataOneWay(
                daysToDepartureDate,""
        );
        String departureDate =CoreFunctions.convertToLongDate (flightPage.getDate("departureDate"));

        step("login as guest");
        loginPage.loginWith(loginType,username,password);

        step("Change currency");
        homePage.changeCurrency(currency,username);


        step("Open flights page");
        homePage.openVerticalPageDeeplink("Flights","","");
        flightSearchPage.closePopup();

        step("change date using deeplink");
        flightSearchPage.changeDateViaDeeplink(daysToDepartureDate,tripType);

        step("Toggle trip type { " + tripType + " }");
        flightSearchPage.toggleTriptype(tripType);

        step("Select origin airport");
        flightSearchPage.selectOrigin(originAirport);
        flightSearchPage.closePopup();

        step("Select destination airport");
        flightSearchPage.selectDestination(destinationAirport);

        step("Select departure date");
        flightSearchPage.selectDate(departureDate);

        step("Search flight");
        flightSearchPage.searchFlight();

        step("check megaphone icon bottom of SRP and click");
        Assertion.assertThat("is megaphone icon displayed on the srp",flightSearchResultPage.isMegaphoneIconDisplayed(),is(true));
        flightSearchResultPage.clickMegaphoneIcon();

        step("verify on general travel information page");
        Assertion.assertThat("is general travel information page correct",flightGeneralTravelInformationPage.verifyGeneralTravelInformationPage(),is(true));
    }

    @Author(names = {"Ashish"})
    @Module(name = "FlightCheckYellowBannerBookingPage2")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test(dataProvider="testFlightCheckYellowBannerBookingPage2LoginDataProvider")
    public void testFlightCheckYellowBannerBookingPage2Login(Map<String, String> param) {
        //deprecated test case
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightPage<WebDriver> flightPage = FlightPage.getInstance(session);
        FlightProductReviewPage<WebDriver> flightProductReviewPage = FlightProductReviewPage.getInstance(session);
        FlightBookingFormPage<WebDriver> flightBookingFormPage = FlightBookingFormPage.getInstance(session);
        FlightGeneralTravelInformationPage<WebDriver> flightGeneralTravelInformationPage = FlightGeneralTravelInformationPage.getInstance(session);

        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");
        String currency = param.get("currency");
        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Find dynamic data");
        flightPage.setDataOneWay(
                daysToDepartureDate,""
        );
        String departureDate =CoreFunctions.convertToLongDate (flightPage.getDate("departureDate"));

        step("login as guest");
        loginPage.loginWith(loginType,username,password);

        step("Change currency");
        homePage.changeCurrency(currency,username);

        step("Open flights page");
        homePage.openVerticalPageDeeplink("Flights","","");
        flightSearchPage.closePopup();

        step("change date using deeplink");
        flightSearchPage.changeDateViaDeeplink(daysToDepartureDate,tripType);

        step("Toggle trip type { " + tripType + " }");
        flightSearchPage.toggleTriptype(tripType);

        step("Select origin airport");
        flightSearchPage.selectOrigin(originAirport);
        flightSearchPage.closePopup();

        step("Select destination airport");
        flightSearchPage.selectDestination(destinationAirport);

        step("Select departure date");
        flightSearchPage.selectDate(departureDate);

        step("Add passenger");
        flightSearchPage.addPassenger(adultPassengers,childPassengers,infantPassengers);

        step("Search flight");
        flightSearchPage.searchFlight();

        step("select first airline");
        flightSearchResultPage.selectFlight();

        step("move to booking page");
        flightProductReviewPage.continueToBookingForm(tripType);

        step("Skip full protection if visible");
        flightBookingFormPage.skipFullProtectionPrp();

        step("verify date format");
        flightBookingFormPage.verifyDateFormat(tripType);

        step("Fill passenger detail");
        flightBookingFormPage.fillPassengerDetail(adultPassengers,childPassengers,infantPassengers, "domestic","");

        step("move to 2nd half of booking page");
        flightBookingFormPage.continueToSecondHalf();

        step("find and verify yellow banner click");
        Assertion.assertThat("is yellow banner displayed on the booking page 2nd part",flightBookingFormPage.isYellowBannerBookingPage2Displayed(),is(true));
        flightBookingFormPage.clickYellowBannerBookingPage2();

        step("verify on general travel information page");
        Assertion.assertThat("is general travel information page correct",flightGeneralTravelInformationPage.verifyGeneralTravelInformationPage(),is(true));
    }

    @Author(names = {"Ashish"})
    @Module(name = "CheckNudgeOnSRP")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void testToCheckBestSellingNudgeOnSRPLoginRoundTrip() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testToCheckBestSellingNudgeOnSRPLoginRoundTrip");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightPage<WebDriver> flightPage = FlightPage.getInstance(session);

        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        int duration = Integer.parseInt(param.get("duration"));

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String airline = param.get("airline");
        String currency = param.get("currency");
        String enTextBestSelling = "High Demand, book now!";
        String idTextBestSelling = "Banyak dicari, pesan sekarang!";

        step("Wait for app to launch");
        homePage.gotoHome();

        step("set date");
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate =CoreFunctions.addToLongDate (currentDate,daysToDepartureDate);
        String returnDate =CoreFunctions.addToLongDate(departureDate,duration);

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Change currency");
        homePage.changeCurrency(currency,username);

        step("Open flights page");
        homePage.openVerticalPageDeeplink("Flights","",currency);
        flightSearchPage.closePopup();

        step("change date using deeplink");
        flightSearchPage.changeDateViaDeeplink(daysToDepartureDate,duration,tripType);

        step("Toggle trip type { " + tripType + " }");
        flightSearchPage.toggleTriptype(tripType);

        step("Select origin airport");
        flightSearchPage.selectOrigin(originAirport);
        flightSearchPage.closePopup();

        step("Select destination airport");
        flightSearchPage.selectDestination(destinationAirport);

        step("Select departure date");
        flightSearchPage.selectDate(departureDate,returnDate);

        step("Search flight");
        flightSearchPage.searchFlight();

        step("Open filter");
        flightSearchResultPage.openFilter();

        step("Filter airline");
        flightSearchResultPage.filterAirline(airline);

        step("Save filter");
        flightSearchResultPage.saveFilter();

        step("verify best selling nudge depart");
        Assertion.assertThat("is best selling nudge displayed",flightSearchResultPage.checkNudge(enTextBestSelling,idTextBestSelling),is(true));

        step("Click flight detail chevron");
        flightSearchResultPage.clickFlightWithNudge(enTextBestSelling,idTextBestSelling);

        step("Open filter");
        flightSearchResultPage.openFilter();

        step("Filter airline");
        flightSearchResultPage.filterAirline(airline);

        step("Save filter");
        flightSearchResultPage.saveFilter();

        step("verify best selling nudge return");
        Assertion.assertThat("is best selling nudge displayed",flightSearchResultPage.checkNudge(enTextBestSelling,idTextBestSelling),is(true));
    }

    @Author(names = {"Ashish"})
    @Module(name = "CheckNudgeOnSRP")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void testToCheckBestExperienceNudgeOnSRPLoginRoundTrip() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testToCheckBestExperienceNudgeOnSRPLoginRoundTrip");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightPage<WebDriver> flightPage = FlightPage.getInstance(session);

        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        int duration = Integer.parseInt(param.get("duration"));

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String airline = param.get("airline");
        String currency = param.get("currency");
        String enTextBestExperience = param.get("enBestExperience");
        String idTextBestExperience = param.get("idBestExperience");
        String optionalLocation = param.get("optionalLocation");
        String optionalAirline = param.get("optionalAirline");
        step("Wait for app to launch");
        homePage.gotoHome();

        step("set date");
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate =CoreFunctions.addToLongDate (currentDate,daysToDepartureDate);
        String returnDate =CoreFunctions.addToLongDate(departureDate,duration);

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Change currency");
        homePage.changeCurrency(currency,username);

        step("Open flights page");
        homePage.openVerticalPageDeeplink("Flights","",currency);
        flightSearchPage.closePopup();

        step("change date using deeplink");
        flightSearchPage.changeDateViaDeeplink(daysToDepartureDate,duration,tripType);

        step("Toggle trip type { " + tripType + " }");
        flightSearchPage.toggleTriptype(tripType);

        step("Select origin airport");
        flightSearchPage.selectOrigin(originAirport);
        flightSearchPage.closePopup();

        step("Select destination airport");
        flightSearchPage.selectDestination(destinationAirport);

        step("Select departure date");
        flightSearchPage.selectDate(departureDate,returnDate);

        step("Search flight");
        flightSearchPage.searchFlight();

        step("Open filter");
        flightSearchResultPage.openFilter();

        step("Filter airline");
        flightSearchResultPage.filterAirline(airline);

        step("Save filter");
        flightSearchResultPage.saveFilter();

        step("verify best experience nudge depart");
        Assertion.assertThat("is best experience nudge displayed",flightSearchResultPage.checkNudge(enTextBestExperience,idTextBestExperience),is(true));

        step("Click flight detail chevron");
        flightSearchResultPage.clickFlightWithNudge(enTextBestExperience,idTextBestExperience);

        step("Open filter");
        flightSearchResultPage.openFilter();

        step("Filter airline");
        flightSearchResultPage.filterAirline(airline);

        step("Save filter");
        flightSearchResultPage.saveFilter();

        step("verify best experience nudge depart");
        Assertion.assertThat("is best experience nudge displayed",flightSearchResultPage.checkNudge(enTextBestExperience,idTextBestExperience),is(true));
    }

    @Author(names = {"Ashish", "Aditi"})
    @Module(name = "CheckFloatingIconSRP")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void testCheckPromoInfoWithFloatingIconSRPLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testCheckPromoInfoWithFloatingIconSRPLogin");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightPage<WebDriver> flightPage = FlightPage.getInstance(session);
        FlightApi<?> flightApi = new FlightApi<>(session);

        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int duration = Integer.parseInt(param.get("duration"));
        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String currency = param.get("currency");
        String optionalLocation = param.get("optionalLocation");
        String optionalAirline = param.get("optionalAirline");
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Find dynamic data");
        FlightDynamicData flightDynamicData = flightApi.findDateRT(
                username,password,
                originAirport,destinationAirport,daysToDepartureDate,duration,
                adultPassengers,childPassengers,infantPassengers,
                tripType,"","","","",optionalLocation,optionalAirline,currency
        );
        String departureDate = CoreFunctions.convertToLongDate(flightDynamicData.departureDate);
        String returnDate = CoreFunctions.convertToLongDate(flightDynamicData.returnDate);
        originAirport = flightDynamicData.originAirport;
        destinationAirport = flightDynamicData.destinationAirport;

        step("Login as " + username + " via deeplink (if needed) and change currency to " + currency);
        homePage.loginIfLoggedOutAndChangeCurrencyViaDeeplink(username, password, currency);

        step("Open flights page");
        homePage.openVerticalPageDeeplink("Flights","",currency);
        flightSearchPage.closePopup();

        step("change date using deeplink");
        flightSearchPage.changeDateViaDeeplink(flightDynamicData, tripType);

        step("Toggle trip type { " + tripType + " }");
        flightSearchPage.toggleTriptype(tripType);

        step("Select origin airport");
        flightSearchPage.selectOrigin(originAirport);
        flightSearchPage.closePopup();

        step("Select destination airport");
        flightSearchPage.selectDestination(destinationAirport);

        step("Select departure date");
        flightSearchPage.selectDate(departureDate, returnDate);

        step("Search flight");
        flightSearchPage.searchFlight();

        step("check floating icon on srp and click");
        Assertion.assertThat("is floating icon displayed",flightSearchResultPage.isFloatingIconDisplayed(),is(true));
        flightSearchResultPage.clickFloatingIcon();

        step("verify redirection to different page");
        flightPage.floatingIconRedirection();

        step("Select flight");
        flightSearchResultPage.selectFlight();

        step("check floating icon on return trip srp and click");
        Assertion.assertThat("is floating icon displayed",flightSearchResultPage.isFloatingIconDisplayed(),is(true));
        flightSearchResultPage.clickFloatingIcon();

        step("verify redirection to different page");
        flightPage.floatingIconRedirection();
    }

    @Author(names = {"Ashish"})
    @Module(name = "LPGBanner")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void testCheckLPGBannerOnFlightLandingPageLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testCheckLPGBannerOnFlightLandingPageLogin");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        LpgPage<WebDriver> lpgPage = LpgPage.getInstance(session);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String currency = param.get("currency");

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Change currency");
        homePage.changeCurrency(currency,username);

        step("Open flights page");
        homePage.openVerticalPageDeeplink("Flights","",currency);
        flightSearchPage.closePopup();

        step("check lpg banner");
        Assertion.assertThat("is lpg banner displayed",flightSearchPage.checkLPGBanner(), is(true));
        flightSearchPage.clickLPGBanner();

        step("verify lpg banner");
        lpgPage.checkLPGBanner();
    }

    @Author(names = {"Ashish"})
    @Module(name = "yellowBannerOnPDP")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void testCheckGeneralTravelInformationPagePDPLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testCheckGeneralTravelInformationPagePDPLogin");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightDescriptionPage<WebDriver> flightDescriptionPage = FlightDescriptionPage.getInstance(session);
        FlightPage<WebDriver> flightPage = FlightPage.getInstance(session);
        FlightGeneralTravelInformationPage<WebDriver> flightGeneralTravelInformationPage = FlightGeneralTravelInformationPage.getInstance(session);

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int duration = Integer.parseInt(param.get("duration"));
        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");

        String currency = param.get("currency");

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Find dynamic data");
        flightPage.setDataRoundTrip(
                daysToDepartureDate,duration,"",""
        );
        String departureDate =CoreFunctions.convertToLongDate (flightPage.getDate("departureDate"));
        String returnDate =CoreFunctions.convertToLongDate(flightPage.getDate("returnDate"));

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Change currency");
        homePage.changeCurrency(currency,username);

        step("Open flights page");
        homePage.openVerticalPageDeeplink("Flights","",currency);
        flightSearchPage.closePopup();

        step("change date using deeplink");
        flightSearchPage.changeDateViaDeeplink(daysToDepartureDate,duration,tripType);

        step("Toggle trip type { " + tripType + " }");
        flightSearchPage.toggleTriptype(tripType);

        step("Select origin airport");
        flightSearchPage.selectOrigin(originAirport);
        flightSearchPage.closePopup();

        step("Select destination airport");
        flightSearchPage.selectDestination(destinationAirport);

        step("Select date");
        flightSearchPage.selectDate(departureDate,returnDate);

        step("Search flight");
        flightSearchPage.searchFlight();

        step("Click flight detail chevron");
        String route = flightSearchResultPage.getRouteText();
        flightSearchResultPage.selectFlight();

        step("yellow banner pdp");
        String yellowBannerText = flightDescriptionPage.checkYellowBannerPDP();
        Assertion.assertThat("is yellow banner pdp displayed",yellowBannerText,is(either(containsString("Important")).or(containsString("Penting nih"))));
        flightDescriptionPage.isContainsDestinationLocation(route,yellowBannerText);
        flightDescriptionPage.clickYellowBannerPDP();

        step("check redirected to General Travel Information page");
        Assertion.assertThat("is general travel information page correct",flightGeneralTravelInformationPage.verifyGeneralTravelInformationPage(),is(true));
        flightGeneralTravelInformationPage.clickBackBtn();

        step("Click flight detail chevron");
        String routeReturn = flightSearchResultPage.getRouteText();
        flightSearchResultPage.selectFlight();

        step("yellow banner pdp");
        String yellowBannerTextReturn = flightDescriptionPage.checkYellowBannerPDP();
        Assertion.assertThat("is yellow banner pdp displayed",yellowBannerTextReturn,is(either(containsString("Important")).or(containsString("Penting nih"))));
        flightDescriptionPage.isContainsDestinationLocation(routeReturn,yellowBannerTextReturn);
        flightDescriptionPage.clickYellowBannerPDP();

        step("check redirected to General Travel Information page");
        Assertion.assertThat("is general travel information page correct",flightGeneralTravelInformationPage.verifyGeneralTravelInformationPage(),is(true));flightGeneralTravelInformationPage.verifyGeneralTravelInformationPage();
    }

    @Author(names = {"Ashish"})
    @Module(name = "refund100pLogin")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void testFlight100pRefundRoundTripLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testFlight100pRefundRoundTripLogin");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightProductReviewPage<WebDriver> flightProductReviewPage = FlightProductReviewPage.getInstance(session);
        FlightBookingFormPage<WebDriver> flightBookingFormPage = FlightBookingFormPage.getInstance(session);
        FlightPage<WebDriver> flightPage = FlightPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        FlightApi<?> flightApi = new FlightApi<>(session);

        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int duration = Integer.parseInt(param.get("duration"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        int baggageAmount = Integer.parseInt(param.get("baggage"));
        String airline = param.get("airline");
        String label = param.get("label");
        int index = Integer.parseInt(param.get("index"));
        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String optionalLocation = param.get("optionalLocation");
        String optionalAirline = param.get("optionalAirline");
        String currency = param.get("currency");

        PriceDetail priceDetail = new PriceDetail();
        priceDetail.tripType=tripType;
        priceDetail.adult=adultPassengers;
        priceDetail.child=childPassengers;
        priceDetail.infant=infantPassengers;

        step("Find dynamic data");
        FlightDynamicData flightDynamicData = flightApi.findDateRT(
                username,password,
                originAirport,destinationAirport,daysToDepartureDate,duration,
                adultPassengers,childPassengers,infantPassengers,
                tripType,airline,airline,"baggage","baggage",optionalLocation,optionalAirline,currency
        );
        String departureDate =CoreFunctions.convertToLongDate(flightDynamicData.departureDate);
        String returnDate =CoreFunctions.convertToLongDate(flightDynamicData.returnDate);
        originAirport = flightDynamicData.originAirport;
        destinationAirport = flightDynamicData.destinationAirport;
        airline = flightDynamicData.airlineDepart;

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink (if needed) and change currency to " + currency);
        homePage.loginIfLoggedOutAndChangeCurrencyViaDeeplink(username, password, currency);

        step("Open flights page");
        homePage.openVerticalPageDeeplink("Flights","","");
        flightSearchPage.closePopup();

        step("change date using deeplink");
        flightSearchPage.changeDateViaDeeplink(flightDynamicData, tripType);

        step("Toggle trip type { " + tripType + " }");
        flightSearchPage.toggleTriptype(tripType);

        step("Select origin airport");
        flightSearchPage.selectOrigin(originAirport);
        flightSearchPage.closePopup();

        step("Select destination airport");
        flightSearchPage.selectDestination(destinationAirport);

        step("Select date");
        flightSearchPage.selectDate(departureDate,returnDate);

        step("Add passenger");
        flightSearchPage.addPassenger(adultPassengers,childPassengers,infantPassengers);

        step("Search flight");
        flightSearchPage.searchFlight();

        step("filter 100% Refund");
        flightSearchResultPage.filterChips(label);

        step("Open filter");
        flightSearchResultPage.openFilter();

        step("Filter airline");
        flightSearchResultPage.filterAirline(airline);

        step("Save filter");
        flightSearchResultPage.saveFilter();

        step("check 100p attribute");
        boolean is100pAttributeDisplayed = flightSearchResultPage.is100pRefundDisplayed();
        Assertion.assertThat("Is 100p refund attribute displayed on srp", is100pAttributeDisplayed, is(true));

        step("Click flight detail chevron");
        flightSearchResultPage.selectFlight();

        step("filter 100% Refund");
        flightSearchResultPage.filterChips(label);

        step("Open filter");
        flightSearchResultPage.openFilter();

        step("Filter airline");
        flightSearchResultPage.filterAirline(airline);

        step("Save filter");
        flightSearchResultPage.saveFilter();

        step("check 100p attribute");
        boolean is100pAttributeDisplayedReturn = flightSearchResultPage.is100pRefundDisplayed();
        Assertion.assertThat("Is 100p refund attribute displayed on srp", is100pAttributeDisplayedReturn, is(true));

        step("continue to booking page using 100p");
        flightSearchResultPage.selectFlight();
        flightProductReviewPage.continueToBookingPageUsing100pSelectBtn();

        step("Skip full protection if visible");
        flightBookingFormPage.skipFullProtectionPrp();

        step("verify date format");
        flightBookingFormPage.verifyDateFormat(tripType);

        step("Fill passenger detail");
        flightBookingFormPage.fillPassengerDetail(adultPassengers,childPassengers,infantPassengers,"domestic",airline);

        step("add addons baggage");
        baggageAmount = flightBookingFormPage.addBaggage();
        String baggagePrice = flightBookingFormPage.getBaggagePrice(baggageAmount);
        flightBookingFormPage.saveAddBaggage();

        step("Continue to second half booking form");
        flightBookingFormPage.continueToSecondHalf();

        step("Add Extra Protection");
        ArrayList<String> extraProtection = flightBookingFormPage.addExtraProtection(index);

        step("Open price detail");
        flightBookingFormPage.openPriceDetail();

        step("Verify price detail");
        priceDetail.tripType=tripType;
        priceDetail.baggageDepartAmount=baggageAmount;
        priceDetail.baggageDepartPrice=baggagePrice;
        priceDetail.extraProtectionTitle = extraProtection.get(0) + " extra protection";
        priceDetail.extraProtectionPrice = extraProtection.get(1);
        boolean is100pRefundTextPresentBookingPage = flightBookingFormPage.offerBreakdown(label,priceDetail);
        Assertion.assertThat("Is 100 percent refund text displayed ", is100pRefundTextPresentBookingPage, is(true));
        Assertion.assertThat("Is price breakdown correct", flightBookingFormPage.isPriceBreakdownCorrect(priceDetail,currency), is(true));

        step("Continue to payment");
        flightBookingFormPage.continueToPayment();

        step("Skip full protection");
        flightBookingFormPage.skipFullProtection();
        flightBookingFormPage.baggageAllowancePopup();

        step("Check order ID");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",!orderID.isBlank(),is(true));

        step("open price detail");
        paymentPage.openPriceDetail();
        boolean is100pRefundTextPresentPaymentPage = paymentPage.offerBreakdown();
        Assertion.assertThat("Is 100 percent refund text displayed ", is100pRefundTextPresentPaymentPage, is(true));
    }

    @Author(names = {"Ashish"})
    @Module(name = "ValidateSearchRoundTripSameDay")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void testValidateSearchRoundTripSameDayGuest() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testValidateSearchRoundTripSameDayGuest");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightPage<WebDriver> flightPage = FlightPage.getInstance(session);
        FlightApi<?> flightApi = new FlightApi<>(session);

        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String optionalLocation = param.get("optionalLocation");
        String optionalAirline = param.get("optionalAirline");
        String currency = param.get("currency");

        step("Find dynamic data");
        FlightDynamicData flightDynamicData = flightApi.findDateRT(
                "","",
                originAirport,destinationAirport,daysToDepartureDate,0,
                1,0,0,
                tripType,"","", "", "","","",currency
        );
        //return date should be same as depart date
        String departureDate = CoreFunctions.convertToLongDate(flightDynamicData.departureDate);

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Logout via deeplink");
        homePage.logoutViaDeeplink();

        step("Change currency");
        homePage.changeCurrency(currency,"");

        step("get language");
        String langCode = homePage.getLanguageApp();

        step("Open flights page");
        homePage.openVerticalPageDeeplink("Flights", langCode, currency);
        flightSearchPage.closePopup();

        step("Toggle trip type");
        flightSearchPage.toggleTriptype(tripType);

        step("Select origin airport");
        flightSearchPage.selectOrigin(originAirport);
        flightSearchPage.closePopup();

        step("Select destination airport");
        flightSearchPage.selectDestination(destinationAirport);

        step("Select departure date");
        flightSearchPage.selectDate(departureDate, departureDate);

        step("verify selected date");
        flightSearchPage.verifySelectedDate(departureDate,departureDate,langCode);

        step("Search flight");
        flightSearchPage.searchFlight();

        step("click on change btn srp and verify date");
        flightSearchResultPage.clickChangeBtn();
        flightSearchResultPage.verifySelectedDateSRP(departureDate,departureDate,langCode);
        flightSearchResultPage.clickSearchBtn();

        step("select first flight");
        flightSearchResultPage.selectFlight();

        step("click on change btn srp return");
        flightSearchResultPage.clickChangeBtn();

        step("verify selected date return");
        flightSearchResultPage.verifySelectedDateSRP(departureDate,departureDate,langCode);
    }

    @Author(names = {"Ashish"})
    @Module(name = "LPGBanner")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void testCheckLPGBannerOnSRPLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testCheckLPGBannerOnSRPLogin");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightPage<WebDriver> flightPage = FlightPage.getInstance(session);
        LpgPage<WebDriver> lpgPage = LpgPage.getInstance(session);
        FlightApi<?> flightApi = new FlightApi<>(session);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");

        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currency = param.get("currency");

        step("Find dynamic data");
        FlightDynamicData flightDynamicData = flightApi.findDate(
                username,password,
                originAirport,destinationAirport,daysToDepartureDate,
                1,0,0,
                tripType,"","", "","",currency
        );
        String departureDate = CoreFunctions.convertToLongDate(flightDynamicData.departureDate);

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Change currency");
        homePage.changeCurrency(currency,username);

        step("Open flights page");
        homePage.openVerticalPageDeeplink("Flights","",currency);
        flightSearchPage.closePopup();

        step("change date using deeplink");
        flightSearchPage.changeDateViaDeeplink(flightDynamicData, tripType);

        step("Toggle trip type { " + tripType + " }");
        flightSearchPage.toggleTriptype(tripType);

        step("Select origin airport");
        flightSearchPage.selectOrigin(originAirport);
        flightSearchPage.closePopup();

        step("Select destination airport");
        flightSearchPage.selectDestination(destinationAirport);

        step("Select departure date");
        flightSearchPage.selectDate(departureDate);

        step("Search flight");
        flightSearchPage.searchFlight();

        step("check lpg banner");
        Assertion.assertThat("is lpg banner displayed",flightSearchPage.checkLPGBanner(), is(true));
        flightSearchPage.clickLPGBanner();

        step("verify lpg banner");
        lpgPage.checkLPGBanner();
    }

    @Author(names = {"Ashish"})
    @Module(name = "ValidateSearchDateToday")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void testValidateSearchDateTodayGuest() {
        //android only case
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testValidateSearchDateTodayGuest");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);

        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");
        String currency = param.get("currency");

        //case state to select date today
        int daysToDepartureDate = 0;
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate =CoreFunctions.addToLongDate (currentDate,daysToDepartureDate);

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginAsGuest();

        step("Change currency");
        homePage.changeCurrency(currency,"");

        step("get language");
        String langCode = homePage.getLanguageApp();

        step("Open flights page");
        homePage.openVerticalPageDeeplink("Flights","",currency);
        flightSearchPage.closePopup();

        step("Toggle trip type");
        flightSearchPage.toggleTriptype(tripType);

        step("Select origin airport");
        flightSearchPage.selectOrigin(originAirport);
        flightSearchPage.closePopup();

        step("Select destination airport");
        flightSearchPage.selectDestination(destinationAirport);

        step("Select today");
        flightSearchPage.selectToday(departureDate);

        step("verify selected date");
        flightSearchPage.verifySelectedDate(departureDate,langCode);

        step("Search flight");
        flightSearchPage.searchFlight();

        step("is results shows flight availability for today date");
        flightSearchResultPage.isSearchDateToday(departureDate,langCode);
    }


    @Author(names = {"Ashish"})
    @Module(name = "SearchAlwaysOnTop")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void testCheckSearchAirportResultsAlwaysOnTopLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testCheckSearchAirportResultsAlwaysOnTopLogin");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);


        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");

        String originAirport = param.get("originAirport");

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink (if needed)");
        homePage.loginIfLoggedOutViaDeeplink(username, password);

        step("Open flights page");
        homePage.openVerticalPageDeeplink("Flights","","");
        flightSearchPage.closePopup();

        step("Select origin airport");
        String actualLocation = flightSearchPage.CheckSearchAirportResultsAlwaysOnTop(originAirport);
        Assertion.assertThat("is location shown : ",actualLocation.toLowerCase(),is(containsString(originAirport.toLowerCase())));
    }

    @Author(names = {"Ashish"})
    @Module(name = "QuietZoneSeat")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void testFlightInternationalOneWayMultiPaxAndAddOnQuietZoneSeat() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testFlightInternationalOneWayMultiPaxAndAddOnQuietZoneSeat");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightProductReviewPage<WebDriver> flightProductReviewPage = FlightProductReviewPage.getInstance(session);
        FlightBookingFormPage<WebDriver> flightBookingFormPage = FlightBookingFormPage.getInstance(session);
        FlightPage<WebDriver> flightPage = FlightPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        FlightApi<?> flightApi = new FlightApi<>(session);

        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");

        int days = Integer.parseInt(param.get("daysToDepartureDate"));
        int daysToDepartureDate = CoreFunctions.getDaysToNextMondayWednesdayOrFriday(days);
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        String airline = param.get("airline");
        String email = param.get("email");
        String phoneNumber = param.get("phoneNumber");
        String optionalLocation = param.get("optionalLocation");
        String optionalAirline = param.get("optionalAirline");
        String currency = param.get("currency");

        PriceDetail priceDetail = new PriceDetail();
        priceDetail.tripType=tripType;
        priceDetail.adult=adultPassengers;
        priceDetail.child=childPassengers;
        priceDetail.infant=infantPassengers;

        step("Find dynamic data");
        FlightDynamicData flightDynamicData = flightApi.findDate(
                "","",
                originAirport,destinationAirport,daysToDepartureDate,
                adultPassengers,childPassengers,infantPassengers,
                tripType,"","seat/quiet_zone","","",currency
        );
        String departureDate =CoreFunctions.convertToLongDate(flightDynamicData.departureDate);
        airline = flightDynamicData.airlineDepart;

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginAsGuest();

        step("Change currency");
        homePage.changeCurrency(currency,"");

        step("Open flights page");
        homePage.openVerticalPageDeeplink("Flights","",currency);
        flightSearchPage.closePopup();

        step("change date using deeplink");
        flightSearchPage.changeDateViaDeeplink(flightDynamicData, tripType);

        step("Toggle trip type { " + tripType + " }");
        flightSearchPage.toggleTriptype(tripType);

        step("Select origin airport");
        flightSearchPage.selectOrigin(originAirport);
        flightSearchPage.closePopup();

        step("Select destination airport");
        flightSearchPage.selectDestination(destinationAirport);

        step("Select date");
        flightSearchPage.selectDate(departureDate);

        step("Add passenger");
        flightSearchPage.addPassenger(adultPassengers,childPassengers,infantPassengers);

        step("Search flight");
        flightSearchPage.searchFlight();

        step("Open filter");
        flightSearchResultPage.openFilter();

        step("Filter airline");
        flightSearchResultPage.filterAirline(airline);

        step("Save filter");
        flightSearchResultPage.saveFilter();

        step("Click flight detail chevron");
        flightSearchResultPage.selectFlight();

        step("Continue to booking form");
        flightProductReviewPage.continueToBookingForm(tripType);

        step("Skip full protection if visible");
        flightBookingFormPage.skipFullProtectionPrp();

        step("verify date format");
        flightBookingFormPage.verifyDateFormat(tripType);

        step("Fill contact detail");
        flightBookingFormPage.fillContactDetail(phoneNumber,email,"guest",airline);

        step("Fill passenger detail");
        flightBookingFormPage.fillPassengerDetail(adultPassengers,childPassengers,infantPassengers,"international",airline);

        step("Continue to second half booking form");
        flightBookingFormPage.continueToSecondHalf();

        step("Add and Verify Quiet Zone Seat");
        flightBookingFormPage.clickAddOnSeat();
        flightBookingFormPage.clickChooseSeat();
        Assertion.assertThat("Is Quiet Zone banner displayed", flightBookingFormPage.isQuiteZoneBannerDisplayed(), is(true));
        String seatPrice = flightBookingFormPage.addQuietZoneSeat(priceDetail,currency);

        step("Open price detail");
        flightBookingFormPage.openPriceDetail();

        step("Verify price detail");
        priceDetail.seatDepartPrice=seatPrice;
        Assertion.assertThat("Is price breakdown correct", flightBookingFormPage.isPriceBreakdownCorrect(priceDetail,currency), is(true));

        step("Continue to payment");
        flightBookingFormPage.continueToPayment();

        step("Skip full protection");
        flightBookingFormPage.skipFullProtection();
        flightBookingFormPage.baggageAllowancePopup();

        step("Check order ID");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",!orderID.isBlank(),is(true));

        step("seat verification");
        paymentPage.openPriceDetail();
        paymentPage.isCorrectNoOfSeatSelected(priceDetail);
    }

    @Author(names = {"Ashish"})
    @Module(name = "PromoInfinity")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void testLoginFlightBestSuitedPromoInfinity() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testLoginFlightBestSuitedPromoInfinity");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightProductReviewPage<WebDriver> flightProductReviewPage = FlightProductReviewPage.getInstance(session);
        FlightBookingFormPage<WebDriver> flightBookingFormPage = FlightBookingFormPage.getInstance(session);
        FlightPage<WebDriver> flightPage = FlightPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        FlightApi<?> flightApi = new FlightApi<>(session);

        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");
        String optionalLocation = param.get("optionalLocation");
        String optionalAirline = param.get("optionalAirline");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        int index = Integer.parseInt(param.get("index"));

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");

        String currency = param.get("currency");

        PriceDetail priceDetail = new PriceDetail();
        priceDetail.tripType=tripType;
        priceDetail.adult=adultPassengers;
        priceDetail.child=childPassengers;
        priceDetail.infant=infantPassengers;

        step("Find dynamic data");
        FlightDynamicData flightDynamicData = flightApi.findDate(
                username,password,
                originAirport,destinationAirport,daysToDepartureDate,
                adultPassengers,childPassengers,infantPassengers,
                tripType,"","promo infinity",optionalLocation,optionalAirline,currency
        );
        String departureDate =CoreFunctions.convertToLongDate(flightDynamicData.departureDate);
        // Return date is only used for the round trip search that must hide the promo banner, so any valid date works
        String returnDate = CoreFunctions.addToLongDate(departureDate,3);
        originAirport = flightDynamicData.originAirport;
        destinationAirport = flightDynamicData.destinationAirport;

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType,username,password);

        step("Change currency");
        homePage.changeCurrency(currency,username);

        step("Open flights page");
        homePage.openVerticalPageDeeplink("Flights","",currency);
        flightSearchPage.closePopup();

        step("change date using deeplink");
        flightSearchPage.changeDateViaDeeplink(flightDynamicData, tripType);

        step("Toggle trip type { " + tripType + " }");
        flightSearchPage.toggleTriptype(tripType);

        step("Select origin airport");
        flightSearchPage.selectOrigin(originAirport);
        flightSearchPage.closePopup();

        step("Select destination airport");
        flightSearchPage.selectDestination(destinationAirport);

        step("Select date");
        flightSearchPage.selectDate(departureDate);

        step("Add passenger");
        flightSearchPage.addPassenger(adultPassengers,childPassengers,infantPassengers);

        step("Search flight");
        flightSearchPage.searchFlight();

        step("user is directed to search result page with flights");
        Assertion.assertThat("is user on search result page",flightSearchResultPage.isOnSearchResultPage(),is(true));

        step("promo banner's apply button is shown on one way srp");
        Assertion.assertThat("is promo banner apply button shown on one way srp",flightSearchResultPage.isPromoInfinityApplyButtonDisplayed(),is(true));

        step("Back to flight landing page and switch on round trip toggle");
        flightSearchResultPage.backToFlightPage();
        flightSearchPage.toggleTriptype("roundtrip");
        flightSearchPage.selectDate(departureDate,returnDate);

        step("Search round trip flight");
        flightSearchPage.searchFlight();

        step("promo banner's apply button is not shown on round trip srp");
        Assertion.assertThat("is promo banner apply button shown on round trip srp",flightSearchResultPage.isPromoInfinityApplyButtonDisplayed(),is(false));

        step("Back to flight landing page and switch off round trip toggle");
        flightSearchResultPage.backToFlightPage();
        flightSearchPage.toggleTriptype(tripType);

        step("Search flight { " + tripType + " } again");
        flightSearchPage.searchFlight();

        step("promo banner's apply button is shown on one way srp");
        Assertion.assertThat("is promo banner apply button shown on one way srp",flightSearchResultPage.isPromoInfinityApplyButtonDisplayed(),is(true));

        step("filter 100% Refund");
        flightSearchResultPage.filterChips("100% Refund");

        step("check 100p attribute");
        boolean is100pAttributeDisplayed =flightSearchResultPage.is100pRefundDisplayed();
        Assertion.assertThat("Is 100p refund attribute displayed on srp", is100pAttributeDisplayed, is(true));

        step("apply promo infinity");
        flightSearchResultPage.applyPromoInfinity();

        step("is promo infinity applied");
        Assertion.assertThat("is promo infinity applied",flightSearchResultPage.isPromoInfinityApplied(),is(true));

        step("Click flight detail chevron");
        flightSearchResultPage.selectFlight();

        step("verify promo infinity section");
        Assertion.assertThat("is promo infinity part displayed in prp",flightProductReviewPage.verifyPromoInfinitySection(),is(true));

        step("swipe to 100p section");
        flightProductReviewPage.swipeToRegularSection();

        step("continue to booking page using regular");
        flightProductReviewPage.continueToBookingForm(tripType);

        step("Skip full protection if visible");
        flightBookingFormPage.skipFullProtectionPrp();

        step("Fill passenger detail");
        flightBookingFormPage.fillPassengerDetail(adultPassengers,childPassengers,infantPassengers,"domestic","");

        step("Continue to second half booking form");
        flightBookingFormPage.continueToSecondHalf();

        step("verify promo infinity section");
        flightBookingFormPage.swipeToPromoInfinityPart();
        Assertion.assertThat("is promo infinity part displayed in booking page part 2",flightBookingFormPage.verifyPromoInfinitySection2ndPage(),is(true));

        step("check applied promo and get price and promo code");
        flightBookingFormPage.openPromoInfinity();
        flightBookingFormPage.removePromo();
        flightSearchResultPage.applyPromoInfinity();
        PromoData promoData = flightBookingFormPage.applyPromoInfinityRandomAndGetData(index);

        step("Open price detail");
        flightBookingFormPage.openPriceDetail();

        if(promoData.isSaveAmount){
            step("check promo code and save amount in price detail");
            Assertion.assertThat("is promo code and amount present in price detail page",flightBookingFormPage.isPromoCodeAndAmountDisplayedInPriceDetail(promoData),is(true));
        }
        step("Verify price detail");
        Assertion.assertThat("Is price breakdown correct", flightBookingFormPage.isPriceBreakdownCorrect(priceDetail,currency), is(true));

        step("Continue to payment");
        flightBookingFormPage.continueToPayment();

        step("Skip full protection");
        flightBookingFormPage.skipFullProtection();
        flightBookingFormPage.baggageAllowancePopup();

        step("Check order ID");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",!orderID.isBlank(),is(true));

        step("is promo infinity section displayed with promo code");
        Assertion.assertThat("is promo infinity section displayed with promo code",paymentPage.isPromoCodeAppliedShown(promoData),is(true));

        step("log out");
        homePage.gotoHome();
        loginPage.loginAsGuest();
    }

    @Author(names = {"Ashish"})
    @Module(name = "VIBooking")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void testFlightVIOneWayRouteLogin() throws InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testFlightVIOneWayRouteLogin");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightProductReviewPage<WebDriver> flightProductReviewPage = FlightProductReviewPage.getInstance(session);
        FlightBookingFormPage<WebDriver> flightBookingFormPage = FlightBookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        FlightApi<WebDriver> flightApi = new FlightApi<>(session);

        String tripType = param.get("tripType");
        String location1 = param.get("location1");
        String location1CityName = param.get("location1CityName");
        String location2 = param.get("location2");
        String location2CityName = param.get("location2CityName");
        String location3 = param.get("location3");
        String location3CityName = param.get("location3CityName");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate =CoreFunctions.addToLongDate(currentDate,daysToDepartureDate);

        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        String airline = param.get("airline");
        String transit = param.get("transit");
        String currency = param.get("currency");

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");

        PriceDetail priceDetail = new PriceDetail();
        priceDetail.tripType=tripType;
        priceDetail.adult=adultPassengers;
        priceDetail.child=childPassengers;
        priceDetail.infant=infantPassengers;

        step("Set the scheduler config to true");
        Assertion.assertThat("Config is set to true", flightApi.toggleScheduleIngestion(true), is(true));

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink (if needed) and change currency to " + currency);
        homePage.loginIfLoggedOutAndChangeCurrencyViaDeeplink(username, password, currency);

        step("Open flights page");
        homePage.openVerticalPageDeeplink("Flights","",currency);
        flightSearchPage.closePopup();

        step("Toggle trip type");
        flightSearchPage.toggleTriptype(tripType);

        step("change date using deeplink");
        flightSearchPage.changeDateViaDeeplink(daysToDepartureDate,tripType);

        step("Select date");
        flightSearchPage.selectDate(departureDate);

        step("Add passenger");
        flightSearchPage.addPassenger(adultPassengers,childPassengers,infantPassengers);

        //create cache of schedule each segments route
        if(Configs.PLATFORM == Platform.ANDROID){

            step("select location1 { " + location1 +" } and location2 { " + location2 +" }");
            flightSearchPage.selectOrigin(location1);
            flightSearchPage.closePopup();
            flightSearchPage.selectDestination(location2);

            step("Search flight");
            flightSearchPage.searchFlight();
            flightSearchResultPage.waitForPageLoad();
        }else {
            //using deeplink to set the date
            step("select location1 { " + location1 +" } and location2 { " + location2 +" }");
            flightSearchPage.deeplinkWithSelectedDateSrp(location1,location2,location1CityName,location2CityName,daysToDepartureDate,adultPassengers,childPassengers,infantPassengers);
            flightSearchResultPage.waitForPageLoad();
            flightSearchResultPage.changeTrip(tripType);
            flightSearchResultPage.waitForPageLoad();
        }

        step("Back to flights page");
        flightSearchResultPage.backToFlightPage();

        step("select location2 { " + location2 +" } and location3 { " + location3 +" }");
        flightSearchPage.selectOrigin(location2);
        flightSearchPage.closePopup();
        flightSearchPage.selectDestination(location3);

        step("Search flight");
        flightSearchPage.searchFlight();
        flightSearchResultPage.waitForPageLoad();

        step("Back to flights page");
        flightSearchResultPage.backToFlightPage();

        step("select location1 { " + location1 +" } and location3 { " + location3 +" }");
        flightSearchPage.selectOrigin(location1);
        flightSearchPage.closePopup();
        flightSearchPage.selectDestination(location3);

        step("Search flight");
        flightSearchPage.searchFlight();

        step("Use self transfer filter");
        flightSearchPage.selectSelfTransferFilter();

        step("verify airline");
        Assertion.assertThat("is VI airline present",flightSearchResultPage.isVI_AirlineDisplayed(),is(true));

        step("Click flight detail chevron");
        flightSearchResultPage.selectFlight();

        step("verify self-transfer indicator in prp");
        Assertion.assertThat("Self-transfer indicator in prp ",flightProductReviewPage.isCheckInAgainTextDisplayed(),is(true));

        step("Tap self-transfer banner and verify Self Transfer Protection included in bottom sheet");
        Assertion.assertThat("Self Transfer Protection included shown in PRP bottom sheet ",flightProductReviewPage.isConnectingFlightProtection(),is(true));

        step("Continue to booking form");
        flightProductReviewPage.continueToBookingForm(tripType);

        step("Skip full protection if visible");
        flightBookingFormPage.skipFullProtectionPrp();

        step("verify date format");
        flightBookingFormPage.verifyDateFormat(tripType);

        step("verify connecting flight protection nudge");
        Assertion.assertThat("is connecting flight protection nudge displayed",flightBookingFormPage.isConnectingFlightProtectionNudgeDisplayed(),is(true));

        step("Fill contact & passenger detail");
        flightBookingFormPage.fillPassengerDetail(adultPassengers,childPassengers,infantPassengers,"domestic",airline);

        step("Continue to second half booking form");
        flightBookingFormPage.continueToSecondHalf();

        step("Verify what you will get");
        Assertion.assertThat("Is what you'll get correct", flightBookingFormPage.isWhatYouGetCorrectVI(), is(true));
        Assertion.assertThat("Validate 'More Benefit' on 'Connecting Flight protection' details will showing Insurance Detail", flightBookingFormPage.verifyMoreBenefit(), is(true));

        step("verify yellow banner");
        Assertion.assertThat("Validate there will be warning message 'You chose connecting flights'",flightBookingFormPage.verifyWarningMassage(),is(true));

        step("Open price detail");
        flightBookingFormPage.openPriceDetail();

        step("Verify price detail");
        Assertion.assertThat("is connecting flight protection auto added",flightBookingFormPage.connectingFlightProtectionDisplayedOnPriceDetail(adultPassengers+childPassengers,currency),is(true));
        Assertion.assertThat("Is price breakdown correct", flightBookingFormPage.isPriceBreakdownCorrect(priceDetail,currency), is(true));

        step("Continue to payment");
        flightBookingFormPage.continueToPayment();

        step("Skip full protection");
        flightBookingFormPage.skipFullProtection();
        flightBookingFormPage.baggageAllowancePopup();

        step("Check order ID");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",!orderID.isBlank(),is(true));
    }

    @Author(names = {"Ashish"})
    @Module(name = "VISearch")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void testFlightVIOneWayRouteSearchLogin() throws InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testFlightVIOneWayRouteSearchLogin");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightPage<WebDriver> flightPage = FlightPage.getInstance(session);
        FlightApi flightApi = new FlightApi<>(session);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");

        String tripType = param.get("tripType");
        String location1 = param.get("location1");
        String location1CityName = param.get("location1CityName");
        String location2 = param.get("location2");
        String location2CityName = param.get("location2CityName");
        String location3 = param.get("location3");
        String location3CityName = param.get("location3CityName");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate =CoreFunctions.addToLongDate(currentDate,daysToDepartureDate);

        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        ArrayList<String> airlines = CoreFunctions.makeAirlineArrayList(param.get("airline"));
        String transit = param.get("transit");
        String currency = param.get("currency");

        step("Set the scheduler config to true");
        Assertion.assertThat("Config is set to true", flightApi.toggleScheduleIngestion(true), is(true));

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType,username,password);

        step("Change currency");
        homePage.changeCurrency(currency,username);

        step("Open flights page");
        homePage.openVerticalPageDeeplink("Flights","",currency);
        flightSearchPage.closePopup();

        step("Toggle trip type");
        flightSearchPage.toggleTriptype(tripType);

        step("Select date");
        flightSearchPage.selectDate(departureDate);

        step("Add passenger");
        flightSearchPage.addPassenger(adultPassengers,childPassengers,infantPassengers);

        //create cache of schedule each segments route
        if(Configs.PLATFORM == Platform.ANDROID){
            step("select location1 { " + location1 +" } and location2 { " + location2 +" }");
            flightSearchPage.selectOrigin(location1);
            flightSearchPage.closePopup();
            flightSearchPage.selectDestination(location2);

            step("Search flight");
            flightSearchPage.searchFlight();
            flightSearchResultPage.waitForPageLoad();
        }else {
            //using deeplink to set the date
            step("select location1 { " + location1 +" } and location2 { " + location2 +" }");
            flightSearchPage.deeplinkWithSelectedDateSrp(location1,location2,location1CityName,location2CityName,daysToDepartureDate,adultPassengers,childPassengers,infantPassengers);
            flightSearchResultPage.waitForPageLoad();
            flightSearchResultPage.changeTrip(tripType);
            flightSearchResultPage.waitForPageLoad();
        }

        step("Back to flights page");
        flightSearchResultPage.backToFlightPage();

        step("select location2 { " + location2 +" } and location3 { " + location3 +" }");
        flightSearchPage.selectOrigin(location2);
        flightSearchPage.closePopup();
        flightSearchPage.selectDestination(location3);

        step("Search flight");
        flightSearchPage.searchFlight();
        flightSearchResultPage.waitForPageLoad();

        step("Back to flights page");
        flightSearchResultPage.backToFlightPage();

        step("select location1 { " + location1 +" } and location3 { " + location3 +" }");
        flightSearchPage.selectOrigin(location1);
        flightSearchPage.closePopup();
        flightSearchPage.selectDestination(location3);

        step("Search flight");
        flightSearchPage.searchFlight();

        step("select { " + transit+ " }");
        flightSearchResultPage.waitForPageLoad();
        flightSearchResultPage.openTransitFilter();
        flightSearchResultPage.filterTransitType(transit);
        flightSearchResultPage.saveFilter();

        step("filter airline");
        flightSearchResultPage.openFilter();
        flightSearchResultPage.filterAirline(airlines);
        flightSearchResultPage.saveFilter();
        flightSearchResultPage.waitForPageLoad();

        step("verify airline");
        Assertion.assertThat("is VI airline present",flightSearchResultPage.checkAirlineVI(airlines),is(true));
    }

    @Author(names = {"Ashish"})
    @Module(name = "OtwSearch")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void testFlightSinglePaxDomesticSearchLoginOW() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testFlightSinglePaxDomesticSearchLoginOW");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightPage<WebDriver> flightPage = FlightPage.getInstance(session);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");

        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate =CoreFunctions.addToLongDate(currentDate,daysToDepartureDate);

        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        int numberOfSwipe = Integer.parseInt(param.get("numberOfSwipe"));
        ArrayList<String> airlines = CoreFunctions.makeAirlineArrayList(param.get("airline"));
        String currency = param.get("currency");

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType,username,password);

        step("Change currency: " + currency);
        homePage.changeCurrency(currency,username);

        step("Open flights page");
        homePage.openVerticalPageDeeplink("Flights","",currency);
        flightSearchPage.closePopup();

        step("change date using deeplink");
        flightSearchPage.changeDateViaDeeplink(daysToDepartureDate,tripType);

        step("Toggle trip type { " + tripType + " }");
        flightSearchPage.toggleTriptype(tripType);

        step("select originAirport { " + originAirport +" } and destinationAirport { " + destinationAirport +" }");
        flightSearchPage.selectOrigin(originAirport);
        flightSearchPage.closePopup();
        flightSearchPage.selectDestination(destinationAirport);

        step("Select date");
        flightSearchPage.selectDate(departureDate);

        step("Add passenger { adultPassengers: " + adultPassengers + " childPassengers: " + childPassengers + " infantPassengers: " + infantPassengers + " }");
        flightSearchPage.addPassenger(adultPassengers,childPassengers,infantPassengers);

        step("Search flight");
        flightSearchPage.searchFlight();

        step("filter airline and check if its showing up in srp " + airlines);
        Assertion.assertThat("is airlines " + airlines + " present on srp",flightSearchResultPage.checkAirlines(airlines,numberOfSwipe),is(true));
    }

    @Author(names = {"Ashish"})
    @Module(name = "OtwSearch")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void testFlightSinglePaxInternationalSearchLoginOW() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testFlightSinglePaxInternationalSearchLoginOW");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightPage<WebDriver> flightPage = FlightPage.getInstance(session);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");

        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate =CoreFunctions.addToLongDate(currentDate,daysToDepartureDate);

        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        int numberOfSwipe = Integer.parseInt(param.get("numberOfSwipe"));
        ArrayList<String> airlines = CoreFunctions.makeAirlineArrayList(param.get("airline"));
        String currency = param.get("currency");

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType,username,password);

        step("Change currency: " + currency);
        homePage.changeCurrency(currency,username);

        step("Open flights page");
        homePage.openVerticalPageDeeplink("Flights","",currency);
        flightSearchPage.closePopup();

        step("change date using deeplink");
        flightSearchPage.changeDateViaDeeplink(daysToDepartureDate,tripType);

        step("Toggle trip type { " + tripType + " }");
        flightSearchPage.toggleTriptype(tripType);

        step("select originAirport { " + originAirport +" } and destinationAirport { " + destinationAirport +" }");
        flightSearchPage.selectOrigin(originAirport);
        flightSearchPage.closePopup();
        flightSearchPage.selectDestination(destinationAirport);

        step("Select date");
        flightSearchPage.selectDate(departureDate);

        step("Add passenger { adultPassengers: " + adultPassengers + " childPassengers: " + childPassengers + " infantPassengers: " + infantPassengers + " }");
        flightSearchPage.addPassenger(adultPassengers,childPassengers,infantPassengers);

        step("Search flight");
        flightSearchPage.searchFlight();

        step("filter airline and check if its showing up in srp " + airlines);
        Assertion.assertThat("is airlines " + airlines + " present on srp",flightSearchResultPage.checkAirlines(airlines,numberOfSwipe),is(true));
    }

    @Author(names = {"Ashish"})
    @Module(name = "OtwSearch")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void testFlightMultiPaxDomesticSearchLoginRTSeparate() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testFlightMultiPaxDomesticSearchLoginRTSeparate");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightPage<WebDriver> flightPage = FlightPage.getInstance(session);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");

        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int duration = Integer.parseInt(param.get("duration"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate,daysToDepartureDate);
        String returnDate = CoreFunctions.addToLongDate(departureDate,duration);

        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        int numberOfSwipe = Integer.parseInt(param.get("numberOfSwipe"));
        ArrayList<String> airlines = CoreFunctions.makeAirlineArrayList(param.get("airline"));
        String currency = param.get("currency");

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType,username,password);

        step("Change currency: " + currency);
        homePage.changeCurrency(currency,username);

        step("Open flights page");
        homePage.openVerticalPageDeeplink("Flights","",currency);
        flightSearchPage.closePopup();

        step("change date using deeplink");
        flightSearchPage.changeDateViaDeeplink(daysToDepartureDate,duration,tripType);

        step("Toggle trip type { " + tripType + " }");
        flightSearchPage.toggleTriptype(tripType);

        step("select originAirport { " + originAirport +" } and destinationAirport { " + destinationAirport +" }");
        flightSearchPage.selectOrigin(originAirport);
        flightSearchPage.closePopup();
        flightSearchPage.selectDestination(destinationAirport);

        step("Select date");
        flightSearchPage.selectDate(departureDate,returnDate);

        step("Add passenger { adultPassengers: " + adultPassengers + " childPassengers: " + childPassengers + " infantPassengers: " + infantPassengers + " }");
        flightSearchPage.addPassenger(adultPassengers,childPassengers,infantPassengers);

        step("Search flight");
        flightSearchPage.searchFlight();

        step("filter airline and check if its showing up in srp " + airlines);
        Assertion.assertThat("is airlines " + airlines + " present on srp",flightSearchResultPage.checkAirlinesOnFilter(airlines,numberOfSwipe),is(true));

        String randomAirline = airlines.get(CoreFunctions.generateRandomInt(0,airlines.size()-1));
        step("filter random airline { " + randomAirline + " }");
        flightSearchResultPage.openFilter();
        flightSearchResultPage.filterAirline(randomAirline);
        flightSearchResultPage.saveFilter();
        flightSearchResultPage.waitForPageLoad();

        step("select first flight");
        flightSearchResultPage.selectFlight();

        step("Select ticket");
        flightSearchResultPage.waitForPageLoad();

        step("filter airline and check if its showing up in srp " + randomAirline);
        ArrayList<String> singleArray = new ArrayList<>();
        singleArray.add(randomAirline);
        Assertion.assertThat("is airlines " + airlines + " present on srp",flightSearchResultPage.checkAirlinesOnFilter(singleArray,numberOfSwipe),is(true));
    }

    @Author(names = {"Aditi"})
    @Module(name = "OtwSearch")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void testFlightRoundTripRouteSearchLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testFlightRoundTripRouteSearchLogin");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");

        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int duration = Integer.parseInt(param.get("duration"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate =CoreFunctions.addToLongDate(currentDate,daysToDepartureDate);
        String returnDate =CoreFunctions.addToLongDate(departureDate,duration);

        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        int numberOfSwipe = Integer.parseInt(param.get("numberOfSwipe"));
        ArrayList<String> airlines = CoreFunctions.makeAirlineArrayList(param.get("airline"));;
        String currency = param.get("currency");;
        String label = param.get("label");

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType,username,password);

        step("Change currency: " + currency);
        homePage.changeCurrency(currency,username);

        step("Open flights page");
        homePage.openVerticalPageDeeplink("Flights","",currency);
        flightSearchPage.closePopup();

        step("change date using deeplink");
        flightSearchPage.changeDateViaDeeplink(daysToDepartureDate,duration,tripType);

        step("Toggle trip type { " + tripType + " }");
        flightSearchPage.toggleTriptype(tripType);

        step("select originAirport { " + originAirport +" } and destinationAirport { " + destinationAirport +" }");
        flightSearchPage.selectOrigin(originAirport);
        flightSearchPage.closePopup();
        flightSearchPage.selectDestination(destinationAirport);

        step("Select date");
        flightSearchPage.selectDate(departureDate,returnDate);

        step("Add passenger { adultPassengers: " + adultPassengers + " childPassengers: " + childPassengers + " infantPassengers: " + infantPassengers + " }");
        flightSearchPage.addPassenger(adultPassengers,childPassengers,infantPassengers);

        step("Search flight");
        flightSearchPage.searchFlight();

        step("filter Round-trip deals");
        flightSearchResultPage.filterChips(label);

        step("filter airline and check if its showing up in srp " + airlines);
        Assertion.assertThat("is airlines " + airlines + " present on srp",flightSearchResultPage.checkAirlinesOnFilter(airlines,numberOfSwipe),is(true));

        String randomAirline = airlines.get(CoreFunctions.generateRandomInt(0,airlines.size()-1));
        step("filter random airline { " + randomAirline + " }");
        flightSearchResultPage.openFilter();
        flightSearchResultPage.filterAirline(randomAirline);
        flightSearchResultPage.saveFilter();
        flightSearchResultPage.waitForPageLoad();

        step("select first flight");
        flightSearchResultPage.selectFlight();

        step("Select ticket");
        flightSearchResultPage.waitForPageLoad();

        step("filter Round-trip deals");
        flightSearchResultPage.filterChips(label);

        step("filter airline and check if its showing up in srp " + randomAirline);
        ArrayList<String> singleArray = new ArrayList<>();
        singleArray.add(randomAirline);
        Assertion.assertThat("is airlines " + airlines + " present on srp",flightSearchResultPage.checkAirlinesOnFilter(singleArray,numberOfSwipe),is(true));

    }

    @Author(names = {"Ashish"})
    @Module(name = "onlineCheckIn")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void testFlightDomesticCheckInOnlineBoardingPassLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testFlightDomesticCheckInOnlineBoardingPassLogin");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightPage<WebDriver> flightPage = FlightPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        FlightApi<WebDriver> flightApi = new FlightApi<>(session);

        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));

        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        String airline = param.get("airline");
        String phoneNumber = param.get("phoneNumber");

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");

        step("Find dynamic data");
        FlightDynamicData flightDynamicData = flightApi.findDate(
                username,password,
                originAirport,destinationAirport,daysToDepartureDate,
                adultPassengers,childPassengers,infantPassengers,
                tripType,airline,"","","",""
        );
        originAirport = flightDynamicData.originAirport;
        destinationAirport = flightDynamicData.destinationAirport;
        // findDate returns departureDate in yyyy-MM-dd, which is exactly what
        // FlightSearchApi expects downstream — do NOT pass through convertToLongDate
        // (that yields "d MMMM yyyy" like "22 June 2026" and the search endpoint
        // returns HTTP 400 for that format, ultimately throwing "Flight not found").
        String departureDate = flightDynamicData.departureDate;
        airline = flightDynamicData.airlineDepart;

        if (Configs.ENVIRONMENT == Environment.PREPROD) {
            step("Validate all suppliers are mock-issued");
            flightApi.validateAllSuppliersAreMockIssuedByAirline(flightDynamicData.airlineCodeDepart, flightDynamicData.originAirport, flightDynamicData.destinationAirport);
        }

        step("complete flight booking via API");
        String[] passengerNames = {CoreFunctions.randomNameWithoutTest() + " Testing"};
        String orderID = flightPage.createOrderFlightB2CDomestic(username, password, originAirport, destinationAirport, departureDate, tripType, adultPassengers, childPassengers, infantPassengers, airline, passengerNames, phoneNumber);
        Assertion.assertThat("Flight order created", orderID, not(""));

        step("Goto home");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink");
        homePage.loginWithSpecificAccountViaDeeplinkOnly(username, password);

        step("get booking code from API");
        String bookingCode = flightApi.getBookingCode(orderID, username);

        step("set Tiket Number on flight dashboard and ticket will be eticket issued");
        Boolean isTicketIssued = flightApi.flightEticketIssuedRequest(orderID, bookingCode, username,passengerNames[0],15);
        Assertion.assertThat("is Eticket Issued", isTicketIssued, is(true));

        step("open order via deeplink");
        orderListPage.openIssuedTicketViaDeeplink(orderID);

        step("check order list page");
        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.getETicketLabel(), either(is("Your e-ticket is here")).or(is("E-tiketmu di sini")));

        step("Check-in online");
        String bookingCodeOrderDetailPage = orderDetailPage.getBookingCodeOrderDetail();
        orderDetailPage.clickCheckInButton();
        Assertion.assertThat("check booking code", orderDetailPage.getBookingCodeOnCheckInPage(), is(bookingCodeOrderDetailPage));
        orderDetailPage.clickContinueToOnlineCheckIn();
        Assertion.assertThat("redirected to some other page", orderDetailPage.isOnlineCheckInPage(), not(containsString("online-checkin")));
    }

    @Author(names = {"Aditi"})
    @Module(name = "FlightInternationalOneway")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test(dataProvider="testFlightInternationalOnewayPassportPopupDataProvider")
    public void testFlightInternationalOnewayPassportPopup(Map<String, String> param) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightProductReviewPage<WebDriver> flightProductReviewPage = FlightProductReviewPage.getInstance(session);
        FlightBookingFormPage<WebDriver> flightBookingFormPage = FlightBookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        FlightApi<?> flightApi = new FlightApi<>(session);

        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        String airline = param.get("airline");

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        int baggageAmount = Integer.parseInt(param.get("baggage"));
        String optionalLocation = param.get("optionalLocation");
        String optionalAirline = param.get("optionalAirline");
        String currency = param.get("currency");

        step("Find dynamic data");
        FlightDynamicData flightDynamicData = flightApi.findDate(
                username,password,
                originAirport,destinationAirport,daysToDepartureDate,
                adultPassengers,childPassengers,infantPassengers,
                tripType, "","baggage",optionalLocation,optionalAirline,currency
        );
        originAirport = flightDynamicData.originAirport;
        destinationAirport = flightDynamicData.destinationAirport;
        String departureDate = CoreFunctions.convertToLongDate(flightDynamicData.departureDate);
        airline = flightDynamicData.airlineDepart;

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink (if needed) and change currency to " + currency);
        homePage.loginIfLoggedOutAndChangeCurrencyViaDeeplink(username, password, currency);

        step("Open flights page");
        homePage.openVerticalPageDeeplink("Flights","",currency);
        flightSearchPage.closePopup();

        step("change date using deeplink");
        flightSearchPage.changeDateViaDeeplink(flightDynamicData, tripType);

        step("Toggle trip type { " + tripType + " }");
        flightSearchPage.toggleTriptype(tripType);

        step("Select origin airport");
        flightSearchPage.selectOrigin(originAirport);
        flightSearchPage.closePopup();

        step("Select destination airport");
        flightSearchPage.selectDestination(destinationAirport);

        step("Select date");
        flightSearchPage.selectDate(departureDate);

        step("Add passenger");
        flightSearchPage.addPassenger(adultPassengers,childPassengers,infantPassengers);

        step("Search flight");
        flightSearchPage.searchFlight();

        step("Open filter");
        flightSearchResultPage.openFilter();

        step("Filter airline");
        flightSearchResultPage.filterAirline(airline);

        step("Save filter");
        flightSearchResultPage.saveFilter();

        step("Click flight detail chevron");
        flightSearchResultPage.selectFlight();

        step("Continue to booking form");
        flightProductReviewPage.continueToBookingForm();

        step("Skip full protection if visible");
        flightBookingFormPage.skipFullProtectionPrp();

        step("verify date format");
        flightBookingFormPage.verifyDateFormat(tripType);

        step("Check that the passport banner is present on the passenger detail form");
        Assertion.assertThat("Passport banner is present on the passenger detail form", flightBookingFormPage.checkPassportBanner(), is(true));

        step("Fill passenger detail");
        flightBookingFormPage.fillPassengerDetail(adultPassengers,childPassengers,infantPassengers, "international",airline);

        step("Continue to second half booking form");
        flightBookingFormPage.continueToSecondHalf();

        step("Check if the passport popup appears");
        Assertion.assertThat("Passport popup is present on the booking page", flightBookingFormPage.checkPassportPopup(), is(true));

        step("Continue to payment");
        flightBookingFormPage.continueToSecondHalf();
        flightBookingFormPage.continueToPayment();

        step("Skip full protection");
        flightBookingFormPage.skipFullProtection();
        flightBookingFormPage.baggageAllowancePopup();

        step("Check order ID");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",!orderID.isBlank(),is(true));
    }

    @DataProvider(name="testFlightInternationalOnewayPassportPopupDataProvider")
    public Object[][] testFlightInternationalOnewayPassportPopupDataProvider(){
        return getObjectParams("testFlightInternationalOnewayPassportPopup");
    }

    @Author(names = {"Mridul"})
    @Module(name = "FlightBookingForm")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test(dataProvider = "testFlightGuestCachedPaxMissingIssuingFieldsBlockedDataProvider")
    public void testFlightGuestCachedPaxMissingIssuingFieldsBlocked(Map<String, String> param) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightProductReviewPage<WebDriver> flightProductReviewPage = FlightProductReviewPage.getInstance(session);
        FlightBookingFormPage<WebDriver> flightBookingFormPage = FlightBookingFormPage.getInstance(session);
        FlightApi<?> flightApi = new FlightApi<>(session);

        String tripType = param.get("tripType");
        String domesticOrigin = param.get("domesticOriginAirport");
        String domesticDestination = param.get("domesticDestinationAirport");
        String domesticAirline = param.get("domesticAirline");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");
        String airline = param.get("airline");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        String email = param.get("email");
        String phoneNumber = param.get("phoneNumber");
        String currency = param.get("currency");
        String optionalLocation = param.get("optionalLocation");
        String optionalAirline = param.get("optionalAirline");
        String intlOptionalLocation = param.get("intlOptionalLocation");
        String intlOptionalAirline = param.get("intlOptionalAirline");

        step("Clear Android app data/cache so no leftover passengers exist");
        homePage.clearAppDataAndRelaunch();

        step("Wait for app to launch (re-run onboarding after data clear)");
        homePage.gotoHome();

        step("Logout via deeplink");
        homePage.logoutViaDeeplink();

        step("Change currency");
        homePage.changeCurrency(currency, "");

        step("Find dynamic data for domestic seed booking (no issuing fields)");
        FlightDynamicData domesticData = flightApi.findDate(
                "", "",
                domesticOrigin, domesticDestination, daysToDepartureDate,
                adultPassengers, childPassengers, infantPassengers,
                tripType, domesticAirline, "", optionalLocation, optionalAirline, currency
        );
        String domesticDepartureDate = CoreFunctions.convertToLongDate(domesticData.departureDate);
        domesticAirline = domesticData.airlineDepart;
        domesticOrigin = domesticData.originAirport;
        domesticDestination = domesticData.destinationAirport;

        step("Open flights page");
        homePage.openVerticalPageDeeplink("Flights", "", currency);
        flightSearchPage.closePopup();

        step("change date using deeplink");
        flightSearchPage.changeDateViaDeeplink(domesticData, tripType);

        step("Toggle trip type { " + tripType + " }");
        flightSearchPage.toggleTriptype(tripType);

        step("Select origin airport");
        flightSearchPage.selectOrigin(domesticOrigin);
        flightSearchPage.closePopup();

        step("Select destination airport");
        flightSearchPage.selectDestination(domesticDestination);

        step("Select date");
        flightSearchPage.selectDate(domesticDepartureDate);

        step("Add passenger");
        flightSearchPage.addPassenger(adultPassengers, childPassengers, infantPassengers);

        step("Search domestic flight to seed passenger cache");
        flightSearchPage.searchFlight();

        step("Open filter");
        flightSearchResultPage.openFilter();

        step("Filter airline");
        flightSearchResultPage.filterAirline(domesticAirline);

        step("Save filter");
        flightSearchResultPage.saveFilter();

        step("Click flight detail chevron");
        flightSearchResultPage.selectFlight();

        step("Continue to booking form");
        flightProductReviewPage.continueToBookingForm(tripType);

        step("Skip full protection if visible");
        flightBookingFormPage.skipFullProtectionPrp();

        step("Fill contact detail as guest");
        flightBookingFormPage.fillContactDetail(phoneNumber, email, "guest", domesticAirline);

        step("Fill international passenger detail (no issuing date/country) to seed app cache");
        List<String> seededPassengers = flightBookingFormPage.fillPassengerDetail(
                adultPassengers, childPassengers, infantPassengers, "international", domesticAirline
        );
        Assertion.assertThat("passenger was filled for cache seed", seededPassengers.isEmpty(), is(false));
        String cachedPassengerName = seededPassengers.get(0);

        step("Continue to second half booking form");
        flightBookingFormPage.continueToSecondHalf();

        step("Try continue to payment — expect incomplete passenger validation");
        flightBookingFormPage.continueToPayment();

        step("Leave booking form and reopen flights for international search");
        homePage.openVerticalPageDeeplink("Flights", "", currency);
        flightSearchPage.closePopup();

        step("Find dynamic data for international MH route");
        FlightDynamicData intlData = flightApi.findDate(
                "", "",
                originAirport, destinationAirport, daysToDepartureDate,
                adultPassengers, childPassengers, infantPassengers,
                tripType, airline, "", intlOptionalLocation, intlOptionalAirline, currency
        );
        String intlDepartureDate = CoreFunctions.convertToLongDate(intlData.departureDate);
        airline = intlData.airlineDepart;
        originAirport = intlData.originAirport;
        destinationAirport = intlData.destinationAirport;

        step("change date using deeplink");
        flightSearchPage.changeDateViaDeeplink(intlData, tripType);

        step("Toggle trip type { " + tripType + " }");
        flightSearchPage.toggleTriptype(tripType);

        step("Select origin airport");
        flightSearchPage.selectOrigin(originAirport);
        flightSearchPage.closePopup();

        step("Select destination airport");
        flightSearchPage.selectDestination(destinationAirport);

        step("Select date");
        flightSearchPage.selectDate(intlDepartureDate);

        step("Add passenger");
        flightSearchPage.addPassenger(adultPassengers, childPassengers, infantPassengers);

        step("Search international flight CGK-KUL MH");
        flightSearchPage.searchFlight();

        step("Open filter");
        flightSearchResultPage.openFilter();

        step("Filter airline");
        flightSearchResultPage.filterAirline(airline);

        step("Save filter");
        flightSearchResultPage.saveFilter();

        step("Click flight detail chevron");
        flightSearchResultPage.selectFlight();

        step("Continue to booking form");
        flightProductReviewPage.continueToBookingForm(tripType);

        step("Skip full protection if visible");
        flightBookingFormPage.skipFullProtectionPrp();

        step("Fill contact detail as guest");
        flightBookingFormPage.fillContactDetail(phoneNumber, email, "guest", airline);

        step("Assert existing cached passenger is shown on booking form");
        Assertion.assertThat("Existing/cached passenger is shown on booking form",
                flightBookingFormPage.isExistingPassengerDisplayed(), is(true));

        step("Select cached passenger from previous domestic booking without completing issuing fields");
        flightBookingFormPage.selectCachedPassengerWithoutCompleting(cachedPassengerName);

        step("Tap Continue — expect red 'Select 1 adult' helper and stay on BF page 1/2");
        Assertion.assertThat(
                "User is blocked on BF page 1 with passenger selection validation (Select 1 adult)",
                flightBookingFormPage.isBlockedByPassengerSelectionValidation(),
                is(true)
        );

        step("Open passenger form and verify issuing date and issuing country are empty");
        Assertion.assertThat(
                "Issuing date and issuing country are empty on selected cached passenger",
                flightBookingFormPage.areIssuingDateAndCountryEmpty(),
                is(true)
        );
    }

    @DataProvider(name = "testFlightGuestCachedPaxMissingIssuingFieldsBlockedDataProvider")
    public Object[][] testFlightGuestCachedPaxMissingIssuingFieldsBlockedDataProvider() {
        return getObjectParams("testFlightGuestCachedPaxMissingIssuingFieldsBlocked");
    }

    @Author(names = {"Aditi"})
    @Module(name = "FlightInternationalOneway")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test(dataProvider="testFlightInternationalOnewayBaggageTouchPointDataProvider")
    public void testFlightInternationalOnewayBaggageTouchPoint(Map<String, String> param) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightProductReviewPage<WebDriver> flightProductReviewPage = FlightProductReviewPage.getInstance(session);
        FlightBookingFormPage<WebDriver> flightBookingFormPage = FlightBookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        FlightApi<?> flightApi = new FlightApi<>(session);

        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        String airline = param.get("airline");

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        int baggageAmount = Integer.parseInt(param.get("baggage"));
        String optionalLocation = param.get("optionalLocation");
        String optionalAirline = param.get("optionalAirline");
        String currency = param.get("currency");

        step("Find dynamic data");
        FlightDynamicData flightDynamicData = flightApi.findDate(
                username,password,
                originAirport,destinationAirport,daysToDepartureDate,
                adultPassengers,childPassengers,infantPassengers,
                tripType,airline,"baggage",optionalLocation,optionalAirline,currency
        );
        originAirport = flightDynamicData.originAirport;
        destinationAirport = flightDynamicData.destinationAirport;
        String departureDate = CoreFunctions.convertToLongDate(flightDynamicData.departureDate);
        airline = flightDynamicData.airlineDepart;

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink (if needed) and change currency to " + currency);
        homePage.loginIfLoggedOutAndChangeCurrencyViaDeeplink(username, password, currency);

        step("Open flights page");
        homePage.openVerticalPageDeeplink("Flights","",currency);
        flightSearchPage.closePopup();

        step("change date using deeplink");
        flightSearchPage.changeDateViaDeeplink(flightDynamicData, tripType);

        step("Toggle trip type { " + tripType + " }");
        flightSearchPage.toggleTriptype(tripType);

        step("Select origin airport");
        flightSearchPage.selectOrigin(originAirport);
        flightSearchPage.closePopup();

        step("Select destination airport");
        flightSearchPage.selectDestination(destinationAirport);

        step("Select date");
        flightSearchPage.selectDate(departureDate);

        step("Add passenger");
        flightSearchPage.addPassenger(adultPassengers,childPassengers,infantPassengers);

        step("Search flight");
        flightSearchPage.searchFlight();

        step("Open filter");
        flightSearchResultPage.openFilter();

        step("Filter airline");
        flightSearchResultPage.filterAirline(airline);

        step("Save filter");
        flightSearchResultPage.saveFilter();

        step("Click flight detail chevron");
        flightSearchResultPage.selectFlight();

        step("Continue to booking form");
        flightProductReviewPage.continueToBookingForm(tripType);

        step("Skip full protection if visible");
        flightBookingFormPage.skipFullProtectionPrp();

        step("verify date format");
        flightBookingFormPage.verifyDateFormat(tripType);

        step("Fill passenger detail");
        flightBookingFormPage.fillPassengerDetail(adultPassengers,childPassengers,infantPassengers, "international",airline);

        step("Check that the AddOn Baggage option is present");
        Assertion.assertThat("AddOn Baggage option is present", flightBookingFormPage.isAddOnPresent("Baggage", "up"), is(true));

        step("Continue to second half booking form");
        flightBookingFormPage.continueToSecondHalf();

        step("Click on checkPassenger");
        flightBookingFormPage.goBackToAddOnSection();

        step("Check that the AddOn Baggage option is present again");
        flightBookingFormPage.continueToSecondHalf();

        PriceDetail priceDetail = new PriceDetail();
        priceDetail.tripType=tripType;
        priceDetail.adult=adultPassengers;
        priceDetail.child=childPassengers;
        priceDetail.infant=infantPassengers;

        step("Open price detail");
        flightBookingFormPage.openPriceDetail();
        Assertion.assertThat("Is price breakdown correct", flightBookingFormPage.isPriceBreakdownCorrect(priceDetail,currency), is(true));

        step("Continue to payment");
        flightBookingFormPage.continueToPayment();

        step("Skip full protection");
        flightBookingFormPage.skipFullProtection();
        flightBookingFormPage.baggageAllowancePopup();

        step("Check order ID");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",!orderID.isBlank(),is(true));
    }

    @DataProvider(name="testFlightInternationalOnewayBaggageTouchPointDataProvider")
    public Object[][] testFlightInternationalOnewayBaggageTouchPointDataProvider(){
        return getObjectParams("testFlightInternationalOnewayBaggageTouchPoint");
    }

    @Author(names = {"Aditi"})
    @Module(name = "FlightDomesticOneway")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test(dataProvider="testFlightDomesticRefundAsAddOnPRPDataProvider")
    public void testFlightDomesticRefundAsAddOnPRP(Map<String, String> param) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightProductReviewPage<WebDriver> flightProductReviewPage = FlightProductReviewPage.getInstance(session);
        FlightBookingFormPage<WebDriver> flightBookingFormPage = FlightBookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        FlightApi<?> flightApi = new FlightApi<>(session);

        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        String airline = param.get("airline");

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        int baggageAmount = Integer.parseInt(param.get("baggage"));
        String optionalLocation = param.get("optionalLocation");
        String optionalAirline = param.get("optionalAirline");
        String currency = param.get("currency");

        step("Find dynamic data");
        FlightDynamicData flightDynamicData = flightApi.findDate(
                username,password,
                originAirport,destinationAirport,daysToDepartureDate,
                adultPassengers,childPassengers,infantPassengers,
                tripType,airline,"baggage",optionalLocation,optionalAirline,currency
        );
        originAirport = flightDynamicData.originAirport;
        destinationAirport = flightDynamicData.destinationAirport;
        String departureDate = CoreFunctions.convertToLongDate(flightDynamicData.departureDate);
        airline = flightDynamicData.airlineDepart;

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink (if needed) and change currency to " + currency);
        homePage.loginIfLoggedOutAndChangeCurrencyViaDeeplink(username, password, currency);
        homePage.openAccount();
        accountPage.selectLanguage("ENG");

        step("Open flights page");
        homePage.openVerticalPageDeeplink("Flights","",currency);
        flightSearchPage.closePopup();

        step("change date using deeplink");
        flightSearchPage.changeDateViaDeeplink(flightDynamicData, tripType);

        step("Toggle trip type { " + tripType + " }");
        flightSearchPage.toggleTriptype(tripType);

        step("Select origin airport");
        flightSearchPage.selectOrigin(originAirport);
        flightSearchPage.closePopup();

        step("Select destination airport");
        flightSearchPage.selectDestination(destinationAirport);

        step("Select date");
        flightSearchPage.selectDate(departureDate);

        step("Add passenger");
        flightSearchPage.addPassenger(adultPassengers,childPassengers,infantPassengers);

        step("Search flight");
        flightSearchPage.searchFlight();

        step("filter 100% Refund");
        flightSearchResultPage.filterChips("100% Refund");

        step("Click flight detail chevron");
        flightSearchResultPage.selectFlight();

        step("Continue to booking form");
        flightProductReviewPage.continueToBookingPageUsing100pSelectBtn();

        step("Skip full protection if visible");
        flightBookingFormPage.skipFullProtectionPrp();

        step("verify date format");
        flightBookingFormPage.verifyDateFormat(tripType);

        step("Fill passenger detail");
        flightBookingFormPage.fillPassengerDetail(adultPassengers,childPassengers,infantPassengers, "domestic", airline);

        step("Continue to second half booking form");
        flightBookingFormPage.continueToSecondHalf();

        step("Open price detail");
        flightBookingFormPage.openPriceDetail();

        step("Verify price detail");
        PriceDetail priceDetail = new PriceDetail();

        priceDetail.tripType=tripType;
        priceDetail.adult=adultPassengers;
        priceDetail.child=childPassengers;
        priceDetail.infant=infantPassengers;

        Assertion.assertThat("Is price breakdown correct", flightBookingFormPage.isPriceBreakdownCorrect(priceDetail,currency), is(true));

        step("Continue to payment");
        flightBookingFormPage.continueToPayment();

        step("Skip full protection");
        flightBookingFormPage.skipFullProtection();
        flightBookingFormPage.baggageAllowancePopup();

        step("Check order ID");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",!orderID.isBlank(),is(true));
    }

    @Author(names = {"Alden"})
    @Module(name = "SRP")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void testFlightOneWaySRPNudgesLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testFlightOneWaySRPNudgesLogin");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightApi<?> flightApi = new FlightApi<>(session);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");

        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");
        String currency = param.get("currency");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));

        step("Find dynamic data");
        FlightDynamicData flightDynamicData = flightApi.findDate(
                "","",
                originAirport,destinationAirport,daysToDepartureDate,
                adultPassengers,childPassengers,infantPassengers,
                tripType,"","","","",currency
        );
        String departureDate = CoreFunctions.convertToLongDate(flightDynamicData.departureDate);
        String airline = flightDynamicData.airlineDepart;
        originAirport = flightDynamicData.originAirport;
        destinationAirport = flightDynamicData.destinationAirport;

        step("Wait for app to launch");
        homePage.gotoHome();
        String language = homePage.getLanguageApp();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username,password);
        homePage.changeCurrency("IDR", username);

        step("Open flights page");
        homePage.openVerticalPageDeeplink("Flights","","");
        flightSearchPage.closePopup();

        step("Toggle trip type { " + tripType + " }");
        flightSearchPage.toggleTriptype(tripType);

        step("select originAirport { " + originAirport +" } and destinationAirport { " + destinationAirport +" }");
        flightSearchPage.selectOrigin(originAirport);
        flightSearchPage.closePopup();
        flightSearchPage.selectDestination(destinationAirport);

        step("Select date");
        flightSearchPage.selectDate(departureDate);

        step("Add passenger { adultPassengers: " + adultPassengers + " childPassengers: " + childPassengers + " infantPassengers: " + infantPassengers + " }");
        flightSearchPage.addPassenger(adultPassengers,childPassengers,infantPassengers);

        step("search flight");
        flightSearchPage.searchFlight();

        step("verify nudges appearance");
        Assertion.assertThat("Verify SRP nudges is shown", flightSearchResultPage.verifySRPNudges(language), is(true));

    }

    @Author(names = {"Mridul"})
    @Module(name = "FlightDomesticRoundTrip")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void testRoundTripDeferredCostSubsidy() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testRoundTripDeferredCostSubsidy");
        BaseTest.paramsThreadLocal.set(param);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightProductReviewPage<WebDriver> flightProductReviewPage = FlightProductReviewPage.getInstance(session);
        FlightBookingFormPage<WebDriver> flightBookingFormPage = FlightBookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        FlightApi<?> flightApi = new FlightApi<>(session);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String currency = param.get("currency");
        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int duration = Integer.parseInt(param.get("duration"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        String airline = param.get("airline");
        String optionalLocation = param.get("optionalLocation");
        String optionalAirline = param.get("optionalAirline");

        step("Find dynamic data");
        FlightDynamicData flightDynamicData = flightApi.findDateRT(
                "","",
                originAirport,destinationAirport,daysToDepartureDate,duration,
                adultPassengers,childPassengers,infantPassengers,
                tripType,airline,"","","",optionalLocation,optionalAirline,currency
        );
        String departureDate = CoreFunctions.convertToLongDate(flightDynamicData.departureDate);
        String returnDate = CoreFunctions.convertToLongDate(flightDynamicData.returnDate);
        airline = flightDynamicData.airlineDepart;
        String airlineReturn = flightDynamicData.airlineReturn;
        originAirport = flightDynamicData.originAirport;
        destinationAirport = flightDynamicData.destinationAirport;

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink (if needed) and change currency to " + currency);
        homePage.loginIfLoggedOutAndChangeCurrencyViaDeeplink(username, password, currency);

        step("Open flights page");
        homePage.openVerticalPageDeeplink("Flights","",currency);
        flightSearchPage.closePopup();

        step("change date using deeplink");
        flightSearchPage.changeDateViaDeeplink(flightDynamicData, tripType);

        step("Toggle trip type { " + tripType + " }");
        flightSearchPage.toggleTriptype(tripType);

        step("select originAirport { " + originAirport +" } and destinationAirport { " + destinationAirport +" }");
        flightSearchPage.selectOrigin(originAirport);
        flightSearchPage.selectDestination(destinationAirport);

        step("Select date");
        flightSearchPage.selectDate(departureDate,returnDate);

        step("Add passenger { adultPassengers: " + adultPassengers + " childPassengers: " + childPassengers + " infantPassengers: " + infantPassengers + " }");
        flightSearchPage.addPassenger(adultPassengers,childPassengers,infantPassengers);

        step("Search flight");
        flightSearchPage.searchFlight();

        step("Filter Airline " + airline);
        flightSearchResultPage.openFilter();
        flightSearchResultPage.filterAirline(airline);
        flightSearchResultPage.saveFilter();

        step("Check the After Cashback label is present on the departure flight");
        Assertion.assertThat("Check stamp label discount with animation is Present on departure flight", flightSearchResultPage.SaveLabel(), is(true));
        Assertion.assertThat("After Cashback label is present on departure flight", flightSearchResultPage.CheckCashBackLabel(), is(true));

        step("select first flight");
        flightSearchResultPage.selectFlight();

        step("Filter Airline " + airlineReturn);
        flightSearchResultPage.openFilter();
        flightSearchResultPage.filterAirline(airlineReturn);
        flightSearchResultPage.saveFilter();

        step("Check the After Cashback label is present on the return flight");
        Assertion.assertThat("Check stamp label discount with animation is Present on departure flight", flightSearchResultPage.SaveLabel(), is(true));
        Assertion.assertThat("After Cashback label is present on departure flight", flightSearchResultPage.CheckCashBackLabel(), is(true));

        step("select first flight");
        flightSearchResultPage.selectFlight();

        step("Check see details of Economy Section ");
        flightSearchResultPage.clickPricePolicyTab();
        Assertion.assertThat("Is Cashback Price Detail Correct ", flightSearchResultPage.isPriceSumCorrect(), is(true));

        step("Continue to booking form");
        flightSearchResultPage.selectTicketFromDetailsPage();

        step("Fill passenger detail");
        flightBookingFormPage.fillPassengerDetail(adultPassengers,childPassengers,infantPassengers, "domestic",airline);

        step("Continue to second half booking form");
        flightBookingFormPage.continueToSecondHalf();

        step("Check green banner under continue to payment button");
        Assertion.assertThat("Is green banner with deferred cost subsidy message displayed", flightBookingFormPage.isGreenBannerVisible(), is(true));

        step("Open price detail");
        flightBookingFormPage.openPriceDetail();

        step("Verify price detail");
        PriceDetail priceDetail = new PriceDetail();
        priceDetail.tripType=tripType;
        priceDetail.adult=adultPassengers;
        priceDetail.child=childPassengers;
        priceDetail.infant=infantPassengers;
        Assertion.assertThat("Is price breakdown correct", flightBookingFormPage.isPriceBreakdownCorrect(priceDetail,currency), is(true));
        flightBookingFormPage.openPriceDetail();
        Assertion.assertThat("Is Cashback Amount visible in price detail", flightBookingFormPage.isCashbackVisible(), is(true));

        step("Continue to payment");
        flightBookingFormPage.continueToPayment();

        step("Skip full protection");
        flightBookingFormPage.skipFullProtection();
        flightBookingFormPage.baggageAllowancePopup();

        step("check we are on payment page");
        Assertion.assertThat("Is payment page loaded", paymentPage.isInPaymentPage(), is(true));
    }

    @Author(names = {"Aditi","Mridul Gupta"})
    @Module(name = "RefundRescheduleInfo")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void testVerifyRefundRescheduleInfoInternational() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testVerifyRefundRescheduleInfoInternational");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightProductReviewPage<WebDriver> flightProductReviewPage = FlightProductReviewPage.getInstance(session);
        FlightBookingFormPage<WebDriver> flightBookingFormPage = FlightBookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        FlightApi<?> flightApi = new FlightApi<>(session);

        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        String airline = param.get("airline");

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String optionalLocation = param.get("optionalLocation");
        String optionalAirline = param.get("optionalAirline");
        String currency = param.get("currency");
        String phoneNumber = param.get("phoneNumber");

        step("Find dynamic data");
        FlightDynamicData flightDynamicData = flightApi.findDateRT(
                "","",
                originAirport,destinationAirport,daysToDepartureDate,2,
                adultPassengers,childPassengers,infantPassengers,
                tripType,"","","","",optionalLocation,optionalAirline,currency
        );
        String departureDate =CoreFunctions.convertToLongDate (flightDynamicData.departureDate);
        String returnDate =CoreFunctions.convertToLongDate(flightDynamicData.returnDate);
        airline = flightDynamicData.airlineDepart;
        String airlineReturn = flightDynamicData.airlineReturn;
        originAirport = flightDynamicData.originAirport;
        destinationAirport = flightDynamicData.destinationAirport;

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Logout via deeplink");
        homePage.logoutViaDeeplink();

        step("Change currency");
        homePage.changeCurrency(currency,"");

        step("Open flights page");
        homePage.openVerticalPageDeeplink("Flights","",currency);
        flightSearchPage.closePopup();

        step("change date using deeplink");
        flightSearchPage.changeDateViaDeeplink(flightDynamicData, tripType);

        step("Toggle trip type { " + tripType + " }");
        flightSearchPage.toggleTriptype(tripType);

        step("Select origin airport");
        flightSearchPage.selectOrigin(originAirport);
        flightSearchPage.closePopup();

        step("Select destination airport");
        flightSearchPage.selectDestination(destinationAirport);

        step("Select date");
        flightSearchPage.selectDate(departureDate, returnDate);

        step("Add passenger");
        flightSearchPage.addPassenger(adultPassengers,childPassengers,infantPassengers);

        step("Search flight");
        flightSearchPage.searchFlight();

        step("Click departure flight");
        flightSearchResultPage.selectFlight();

        step("Click return flight");
        flightSearchResultPage.selectFlight();

        boolean departPolicyRowVisible;
        boolean returnPolicyRowVisible;

        if (flightProductReviewPage.isMultiFarePRP()) {
            step("Multi-fare PRP detected — verify Refund/Reschedule info per leg and continue to booking form");
            Map<String, Boolean> rowVisibilityPerLeg =
                    flightProductReviewPage.verifyRefundRescheduleInfoMultiFareAndContinue(tripType);
            departPolicyRowVisible = Boolean.TRUE.equals(rowVisibilityPerLeg.get("depart"));
            returnPolicyRowVisible = Boolean.TRUE.equals(rowVisibilityPerLeg.get("return"));
        } else {
            step("Click see details on PRP");
            flightProductReviewPage.clickDetailPRP();

            step("Open Refund & Reschedule section (legacy combined tab if rendered, else no-op)");
            flightProductReviewPage.clickRefundReschedulePolicyTab();

            step("Open Refund tab");
            flightProductReviewPage.clickRefundTab();

            step("Switch to Depart leg under Refund tab");
            flightProductReviewPage.clickDepartChip();
            boolean departRefundRowVisible = flightProductReviewPage.isPolicyRowVisible();
            Assertion.assertThat("Refund policy info present for Depart leg",
                    flightProductReviewPage.isPolicyInfoVisible(), is(true));

            step("Switch to Return leg under Refund tab");
            flightProductReviewPage.clickReturnChip();
            boolean returnRefundRowVisible = flightProductReviewPage.isPolicyRowVisible();
            Assertion.assertThat("Refund policy info present for Return leg",
                    flightProductReviewPage.isPolicyInfoVisible(), is(true));

            step("Open Reschedule tab");
            flightProductReviewPage.clickRescheduleTab();

            step("Switch to Depart leg under Reschedule tab");
            flightProductReviewPage.clickDepartChip();
            boolean departRescheduleRowVisible = flightProductReviewPage.isPolicyRowVisible();
            Assertion.assertThat("Reschedule policy info present for Depart leg",
                    flightProductReviewPage.isPolicyInfoVisible(), is(true));

            step("Switch to Return leg under Reschedule tab");
            flightProductReviewPage.clickReturnChip();
            boolean returnRescheduleRowVisible = flightProductReviewPage.isPolicyRowVisible();
            Assertion.assertThat("Reschedule policy info present for Return leg",
                    flightProductReviewPage.isPolicyInfoVisible(), is(true));

            departPolicyRowVisible = departRefundRowVisible || departRescheduleRowVisible;
            returnPolicyRowVisible = returnRefundRowVisible || returnRescheduleRowVisible;

            step("Continue to booking form");
            flightProductReviewPage.continueToBookingForm(tripType);
        }

        step("Skip full protection if visible");
        flightBookingFormPage.skipFullProtectionPrp();

        step("Verify refund reschedule info banner for departure flight on booking form");
        Assertion.assertThat("Refund reschedule info present for departure flight", flightBookingFormPage.verifyDepartureInfoBookingPage(), is(true));
        Assertion.assertThat("Refund reschedule info present for return flight", flightBookingFormPage.verifyReturnInfoBookingPage(), is(true));

        step("verify date format");
        flightBookingFormPage.verifyDateFormat(tripType);

        step("Fill contact detail");
        flightBookingFormPage.fillContactDetail(phoneNumber,username,"guest",airline);

        step("Fill passenger detail");
        flightBookingFormPage.fillPassengerDetail(adultPassengers,childPassengers,infantPassengers, "international", airline);

        step("Continue to second half booking form");
        flightBookingFormPage.continueToSecondHalf();

        step("Continue to payment");
        flightBookingFormPage.continueToPayment();

        step("Skip full protection");
        flightBookingFormPage.skipFullProtection();
        flightBookingFormPage.baggageAllowancePopup();

        step("Check order ID");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",!orderID.isBlank(),is(true));

        if (departPolicyRowVisible) {
            step("Check refund reschedule info on payment page");
            Assertion.assertThat("Refund reschedule info present for departure flight", paymentPage.verifyDepartureInfo(), is(true));
        } else {
            log("Skipping payment-page departure refund/reschedule assertion - PRP had no policy row for Depart leg");
        }
        if (returnPolicyRowVisible) {
            step("Check refund reschedule info on payment page");
            Assertion.assertThat("Refund reschedule info present for return flight", paymentPage.verifyReturnInfo(), is(true));
        } else {
            log("Skipping payment-page return refund/reschedule assertion - PRP had no policy row for Return leg");
        }


    }

    @Author(names = {"Aditi","Mridul Gupta"})
    @Module(name = "RefundRescheduleInfo")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void testVerifyRefundRescheduleInfoDomestic() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testVerifyRefundRescheduleInfoDomestic");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightProductReviewPage<WebDriver> flightProductReviewPage = FlightProductReviewPage.getInstance(session);
        FlightBookingFormPage<WebDriver> flightBookingFormPage = FlightBookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        FlightApi<?> flightApi = new FlightApi<>(session);

        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        String airline = param.get("airline");

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String optionalLocation = param.get("optionalLocation");
        String optionalAirline = param.get("optionalAirline");
        String currency = param.get("currency");
        String phoneNumber = param.get("phoneNumber");

        step("Find dynamic data");
        FlightDynamicData flightDynamicData = flightApi.findDateRT(
                "","",
                originAirport,destinationAirport,daysToDepartureDate,2,
                adultPassengers,childPassengers,infantPassengers,
                tripType,airline,airline,"","",optionalLocation,optionalAirline,currency
        );
        String departureDate =CoreFunctions.convertToLongDate (flightDynamicData.departureDate);
        String returnDate =CoreFunctions.convertToLongDate(flightDynamicData.returnDate);
        airline = flightDynamicData.airlineDepart;
        String airlineReturn = flightDynamicData.airlineReturn;
        originAirport = flightDynamicData.originAirport;
        destinationAirport = flightDynamicData.destinationAirport;

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Logout via deeplink");
        homePage.logoutViaDeeplink();

        step("Change currency");
        homePage.changeCurrency(currency,"");

        step("Open flights page");
        homePage.openVerticalPageDeeplink("Flights","",currency);
        flightSearchPage.closePopup();

        step("change date using deeplink");
        flightSearchPage.changeDateViaDeeplink(flightDynamicData, tripType);

        step("Toggle trip type { " + tripType + " }");
        flightSearchPage.toggleTriptype(tripType);

        step("Select origin airport");
        flightSearchPage.selectOrigin(originAirport);
        flightSearchPage.closePopup();

        step("Select destination airport");
        flightSearchPage.selectDestination(destinationAirport);

        step("Select date");
        flightSearchPage.selectDate(departureDate, returnDate);

        step("Add passenger");
        flightSearchPage.addPassenger(adultPassengers,childPassengers,infantPassengers);

        step("Search flight");
        flightSearchPage.searchFlight();

        step("Filter Airline " + airline);
        flightSearchResultPage.openFilter();
        flightSearchResultPage.filterAirline(airline);
        flightSearchResultPage.saveFilter();

        step("Click departure flight");
        flightSearchResultPage.selectFlight();

        step("Filter Airline " + airline);
        flightSearchResultPage.openFilter();
        flightSearchResultPage.filterAirline(airline);
        flightSearchResultPage.saveFilter();

        step("Click return flight");
        flightSearchResultPage.selectFlight();

        // Track whether the populated policy row container was actually rendered on PRP for
        // each leg. We OR refund + reschedule tab results per leg. When the PRP shows only
        // the "information is not available" empty-state, the payment page will not render
        // refund/reschedule banners either — so we use these flags to gate the payment-page
        // assertions further down to avoid false negatives.
        boolean departPolicyRowVisible;
        boolean returnPolicyRowVisible;

        // PRP renders two distinct UIs:
        //  (a) Single-fare flow (no insurance) — one fare card with a "See details" link that
        //      opens a popup containing Refund/Reschedule tabs AND Depart/Return chips to
        //      switch legs inside the same popup.
        //  (b) Multi-fare flow (airline offers per-leg insurance bundles like "tiket 100% Refund DP")
        //      — a 2-step stepper "1. Select depart ticket — 2. Select return ticket" with multiple
        //      fare cards per step. Each fare card has its own "See details" popup that is per-leg
        //      (no chips). The user must Select a fare to advance through the stepper.
        if (flightProductReviewPage.isMultiFarePRP()) {
            step("Multi-fare PRP detected — verify Refund/Reschedule info per leg and continue to booking form");
            Map<String, Boolean> rowVisibilityPerLeg =
                    flightProductReviewPage.verifyRefundRescheduleInfoMultiFareAndContinue(tripType);
            departPolicyRowVisible = Boolean.TRUE.equals(rowVisibilityPerLeg.get("depart"));
            returnPolicyRowVisible = Boolean.TRUE.equals(rowVisibilityPerLeg.get("return"));
        } else {
            step("Click see details on PRP");
            flightProductReviewPage.clickDetailPRP();

            step("Open Refund & Reschedule section (legacy combined tab if rendered, else no-op)");
            flightProductReviewPage.clickRefundReschedulePolicyTab();

            step("Open Refund tab");
            flightProductReviewPage.clickRefundTab();

            // For each chip check below we peek isPolicyRowVisible() BEFORE isPolicyInfoVisible(),
            // since the latter closes the popup. We OR row visibility across Refund + Reschedule
            // tabs for each leg — the payment page is gated on whether real policy data (not the
            // "info not available" fallback) was rendered for that leg on the PRP.
            step("Switch to Depart leg under Refund tab");
            flightProductReviewPage.clickDepartChip();
            boolean departRefundRowVisible = flightProductReviewPage.isPolicyRowVisible();
            Assertion.assertThat("Refund policy info present for Depart leg",
                    flightProductReviewPage.isPolicyInfoVisible(), is(true));

            step("Switch to Return leg under Refund tab");
            flightProductReviewPage.clickReturnChip();
            boolean returnRefundRowVisible = flightProductReviewPage.isPolicyRowVisible();
            Assertion.assertThat("Refund policy info present for Return leg",
                    flightProductReviewPage.isPolicyInfoVisible(), is(true));

            step("Open Reschedule tab");
            flightProductReviewPage.clickRescheduleTab();

            step("Switch to Depart leg under Reschedule tab");
            flightProductReviewPage.clickDepartChip();
            boolean departRescheduleRowVisible = flightProductReviewPage.isPolicyRowVisible();
            Assertion.assertThat("Reschedule policy info present for Depart leg",
                    flightProductReviewPage.isPolicyInfoVisible(), is(true));

            step("Switch to Return leg under Reschedule tab");
            flightProductReviewPage.clickReturnChip();
            boolean returnRescheduleRowVisible = flightProductReviewPage.isPolicyRowVisible();
            Assertion.assertThat("Reschedule policy info present for Return leg",
                    flightProductReviewPage.isPolicyInfoVisible(), is(true));

            departPolicyRowVisible = departRefundRowVisible || departRescheduleRowVisible;
            returnPolicyRowVisible = returnRefundRowVisible || returnRescheduleRowVisible;

            step("Continue to booking form");
            flightProductReviewPage.continueToBookingForm(tripType);
        }

        step("Skip full protection if visible");
        flightBookingFormPage.skipFullProtectionPrp();

        step("Verify refund reschedule info banner for departure flight on booking form");
        Assertion.assertThat("Refund reschedule info present for departure flight", flightBookingFormPage.verifyDepartureInfoBookingPage(), is(true));
        Assertion.assertThat("Refund reschedule info present for return flight", flightBookingFormPage.verifyReturnInfoBookingPage(), is(true));

        step("verify date format");
        flightBookingFormPage.verifyDateFormat(tripType);

        step("Fill contact detail");
        flightBookingFormPage.fillContactDetail(phoneNumber,username,"guest",airline);

        step("Fill passenger detail");
        flightBookingFormPage.fillPassengerDetail(adultPassengers,childPassengers,infantPassengers, "domestic", airline);

        step("Continue to second half booking form");
        flightBookingFormPage.continueToSecondHalf();

        step("Continue to payment");
        flightBookingFormPage.continueToPayment();

        step("Skip full protection");
        flightBookingFormPage.skipFullProtection();
        flightBookingFormPage.baggageAllowancePopup();

        step("Check order ID");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",!orderID.isBlank(),is(true));

        if (departPolicyRowVisible) {
            step("Check refund reschedule info on payment page");
            Assertion.assertThat("Refund reschedule info present for departure flight", paymentPage.verifyDepartureInfo(), is(true));
        } else {
            log("Skipping payment-page departure refund/reschedule assertion - PRP had no policy row for Depart leg");
        }

        if (returnPolicyRowVisible) {
            step("Check refund reschedule info on payment page");
            Assertion.assertThat("Refund reschedule info present for return flight", paymentPage.verifyReturnInfo(), is(true));
        } else {
            log("Skipping payment-page return refund/reschedule assertion - PRP had no policy row for Return leg");
        }

    }

    @Author(names = {"Ashish"})
    @Module(name = "Flight")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test(dataProvider="testFlightDomesticOnewayDirectLoginBusinessCriticalDataProvider")
    public void FLG_testFlightDomesticOnewayDirectLoginBusinessCritical(Map<String, String> param) throws ExecutionException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightProductReviewPage<WebDriver> flightProductReviewPage = FlightProductReviewPage.getInstance(session);
        FlightBookingFormPage<WebDriver> flightBookingFormPage = FlightBookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        FlightApi<?> flightApi = new FlightApi<>(session);
        LoyaltyApi<?> loyaltyApi = new LoyaltyApi<>(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);

        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        String airline = param.get("airline");

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String optionalLocation = param.get("optionalLocation");
        String optionalAirline = param.get("optionalAirline");
        String currency = param.get("currency");
        String pdProcessType = param.get("pdProcessType");
        int pdRatio = Integer.parseInt(param.get("pdRatio"));

        step("Find dynamic data");
        FlightDynamicData flightDynamicData = flightApi.findDate(
                username,password,
                originAirport,destinationAirport,daysToDepartureDate,
                adultPassengers,childPassengers,infantPassengers,
                tripType,airline,"",optionalLocation,optionalAirline,currency
        );
        originAirport = flightDynamicData.originAirport;
        destinationAirport = flightDynamicData.destinationAirport;
        String departureDate = CoreFunctions.convertToLongDate(flightDynamicData.departureDate);
        airline = flightDynamicData.airlineDepart;

        step("Wait for app to launch");
        homePage.gotoHome();

        step("logout if already logged in");
        loginPage.loginAsGuest();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Change currency");
        homePage.changeCurrency(currency,username);

        step("get tiket points");
        String tiketPoints = homePage.getActiveTiketPointsInRewardsPage();

        //inject tiket point
        step("check if order can be book using half tiket points, else delete or inject point");
        tiketPoints = loyaltyApi.injectTiketPointsBasedOnCondition( 20000, pdProcessType, pdRatio, username,tiketPoints,"half");

        step("Open flights page");
        homePage.openVerticalPageDeeplink("Flights","",currency);
        flightSearchPage.closePopup();

        step("change date using deeplink");
        flightSearchPage.changeDateViaDeeplink(flightDynamicData, tripType);

        step("Toggle trip type { " + tripType + " }");
        flightSearchPage.toggleTriptype(tripType);

        step("Select origin airport");
        flightSearchPage.selectOrigin(originAirport);
        flightSearchPage.closePopup();

        step("Select destination airport");
        flightSearchPage.selectDestination(destinationAirport);

        step("Select date");
        flightSearchPage.selectDate(departureDate);

        step("Add passenger");
        flightSearchPage.addPassenger(adultPassengers,childPassengers,infantPassengers);

        step("Search flight");
        flightSearchPage.searchFlight();

        step("Open filter");
        flightSearchResultPage.openFilter();

        step("Filter airline");
        flightSearchResultPage.filterAirline(airline);

        step("Save filter");
        flightSearchResultPage.saveFilter();

        step("Click flight detail chevron");
        flightSearchResultPage.selectFlight();

        step("Continue to booking form");
        flightProductReviewPage.continueToBookingForm(tripType);

        step("Skip full protection if visible");
        flightBookingFormPage.skipFullProtectionPrp();

        step("verify date format");
        flightBookingFormPage.verifyDateFormat(tripType);

        step("Fill passenger detail");
        flightBookingFormPage.fillPassengerDetail(adultPassengers,childPassengers,infantPassengers, "domestic",airline);

        step("Continue to second half booking form");
        flightBookingFormPage.continueToSecondHalf();

        step("Open price detail");
        flightBookingFormPage.openPriceDetail();

        step("Verify price detail");
        PriceDetail priceDetail = new PriceDetail();

        priceDetail.tripType=tripType;
        priceDetail.adult=adultPassengers;
        priceDetail.child=childPassengers;
        priceDetail.infant=infantPassengers;
        Assertion.assertThat("Is price breakdown correct", flightBookingFormPage.isPriceBreakdownCorrect(priceDetail,currency), is(true));

        step("Continue to payment");
        flightBookingFormPage.continueToPayment();

        step("Skip full protection");
        flightBookingFormPage.skipFullProtection();
        flightBookingFormPage.baggageAllowancePopup();

        step("Check order ID");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",!orderID.isBlank(),is(true));
        double totalPrice = CoreFunctions.convertPriceSafely(paymentPage.getTotalPrice());

        step("choose SAKUKU payment method");
        paymentPage.openPaymentMethod();
        paymentPage.selectPaymentMethod("Sakuku");

        step("Toggle pay with loyalty");
        paymentPage.payWithTiketPoint();
        double priceAfterTiketPoint = CoreFunctions.convertPriceSafely(paymentPage.getTotalPrice());
        int tiketPointsToBeUsed = (int) ((totalPrice - priceAfterTiketPoint));
        Assertion.assertThat("Tiket points to be used is correct", tiketPointsToBeUsed, is(Integer.parseInt(tiketPoints)));

        step("Validate all suppliers are mock issued by airline, skipped if real issued supplier exist");
        boolean isDepartSuppliersMocked = flightApi.areAllSuppliersMockIssuedByAirline(flightDynamicData.airlineCodeDepart, flightDynamicData.originAirport, flightDynamicData.destinationAirport);

        step("only submit payment if all suppliers are mocked { isDepartSuppliersMocked: " + isDepartSuppliersMocked +" }");
        if(Configs.ENVIRONMENT != Environment.PROD && isDepartSuppliersMocked){
            step("Submit payment");
            paymentPage.clickPayButton();

            step("Auto paid order via api");
            paymentPage.postAutoPaidOrder();
            orderListPage.waitForPaymentSuccessful(orderID,15);

            step("Open order list page");
            paymentPage.seeOrderList(orderID);

            step("get deducted tiket points");
            String newTiketPoints = homePage.getActiveTiketPointsInRewardsPage();
            int tiketPointsActual = Integer.parseInt(newTiketPoints.replaceAll("[^0-9]", ""));

            step("Verify loyalty point is 0");
            Assertion.assertThat("Verify loyalty point value is 0",tiketPointsActual,is(0));

            step("check order status");
            orderListPage.openOrderDetailViaDeeplink(orderID);
            
            Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.getETicketLabel(), either(is("Your e-ticket is here")).or(is("E-tiketmu di sini")).or(is("Waiting for confirmation")).or(is("Menunggu konfirmasi")).or(is("Processing e-ticket")).or(is("Memproses e-tiket")));
        }
    }
    @DataProvider(name="testFlightDomesticOnewayDirectLoginBusinessCriticalDataProvider")
    public Object[][] testFlightDomesticOnewayDirectLoginBusinessCriticalDataProvider(){
        return getObjectParams("FLG_testFlightDomesticOnewayDirectLoginBusinessCritical");
    }

    @Author(names = {"Ashish"})
    @Module(name = "Flight")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test(dataProvider="testFlightDomesticOnewayTransitLoginBusinessCriticalDataProvider")
    public void FLG_testFlightDomesticOnewayTransitLoginBusinessCritical(Map<String, String> param) throws ExecutionException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightProductReviewPage<WebDriver> flightProductReviewPage = FlightProductReviewPage.getInstance(session);
        FlightBookingFormPage<WebDriver> flightBookingFormPage = FlightBookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        FlightApi<?> flightApi = new FlightApi<>(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);

        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        String airline = param.get("airline");

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        int baggageAmount = Integer.parseInt(param.get("baggage"));
        String currency = param.get("currency");
        String transit = param.get("transit");

        step("Find dynamic data");
        FlightDynamicData flightDynamicData = flightApi.findDate(
                username,password,
                originAirport,destinationAirport,daysToDepartureDate,
                adultPassengers,childPassengers,infantPassengers,
                tripType,airline,"baggage","","",currency
        );
        originAirport = flightDynamicData.originAirport;
        destinationAirport = flightDynamicData.destinationAirport;
        String departureDate = CoreFunctions.convertToLongDate(flightDynamicData.departureDate);
        airline = flightDynamicData.airlineDepart;

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Change currency");
        homePage.changeCurrency(currency,username);

        step("Open flights page");
        homePage.openVerticalPageDeeplink("Flights","",currency);
        flightSearchPage.closePopup();

        step("change date using deeplink");
        flightSearchPage.changeDateViaDeeplink(flightDynamicData, tripType);

        step("Toggle trip type { " + tripType + " }");
        flightSearchPage.toggleTriptype(tripType);

        step("Select origin airport");
        flightSearchPage.selectOrigin(originAirport);
        flightSearchPage.closePopup();

        step("Select destination airport");
        flightSearchPage.selectDestination(destinationAirport);

        step("Select date");
        flightSearchPage.selectDate(departureDate);

        step("Add passenger");
        flightSearchPage.addPassenger(adultPassengers,childPassengers,infantPassengers);

        step("Search flight");
        flightSearchPage.searchFlight();

        step("Open filter");
        flightSearchResultPage.openFilter();

        step("Filter airline");
        flightSearchResultPage.filterAirline(airline);

        step("Save filter");
        flightSearchResultPage.saveFilter();

        step("select { " + transit+ " }");
        flightSearchResultPage.waitForPageLoad();
        flightSearchResultPage.openTransitFilter();
        flightSearchResultPage.filterTransitType(transit);
        flightSearchResultPage.saveFilter();

        step("Click flight detail chevron");
        flightSearchResultPage.selectFlight();

        step("Continue to booking form");
        flightProductReviewPage.continueToBookingForm(tripType);

        step("Skip full protection if visible");
        flightBookingFormPage.skipFullProtectionPrp();

        step("verify date format");
        flightBookingFormPage.verifyDateFormat(tripType);

        step("Fill passenger detail");
        flightBookingFormPage.fillPassengerDetail(adultPassengers,childPassengers,infantPassengers, "domestic",airline);

        step("Add baggage");
        baggageAmount = flightBookingFormPage.addBaggage();
        String baggagePrice = flightBookingFormPage.getBaggagePrice(baggageAmount);
        flightBookingFormPage.saveAddBaggage();

        step("Continue to second half booking form");
        flightBookingFormPage.continueToSecondHalf();

        step("Open price detail");
        flightBookingFormPage.openPriceDetail();

        step("Verify price detail");
        PriceDetail priceDetail = new PriceDetail();

        priceDetail.tripType=tripType;
        priceDetail.adult=adultPassengers;
        priceDetail.child=childPassengers;
        priceDetail.infant=infantPassengers;
        priceDetail.baggageDepartAmount=baggageAmount;
        priceDetail.baggageDepartPrice=baggagePrice;

        Assertion.assertThat("Is price breakdown correct", flightBookingFormPage.isPriceBreakdownCorrect(priceDetail,currency), is(true));

        step("Continue to payment");
        flightBookingFormPage.continueToPayment();

        step("Skip full protection");
        flightBookingFormPage.skipFullProtection();
        flightBookingFormPage.baggageAllowancePopup();

        step("Check order ID");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",!orderID.isBlank(),is(true));

        step("choose ATM payment method");
        paymentPage.openPaymentMethod();
        paymentPage.selectPaymentMethod("ATM");

        step("Validate all suppliers are mock issued by airline, skipped if real issued supplier exist");
        boolean isDepartSuppliersMocked = flightApi.areAllSuppliersMockIssuedByAirline(flightDynamicData.airlineCodeDepart, flightDynamicData.originAirport, flightDynamicData.destinationAirport);

        step("only submit payment if all suppliers are mocked { isDepartSuppliersMocked: " + isDepartSuppliersMocked +" }");
        if(Configs.ENVIRONMENT != Environment.PROD && isDepartSuppliersMocked){
            step("Submit payment");
            paymentPage.clickPayButton();

            step("Auto paid order via api");
            paymentPage.postAutoPaidOrder();
            orderListPage.waitForPaymentSuccessful(orderID,15);

            step("Open order list page");
            paymentPage.seeOrderList(orderID);

            step("check order status");
            orderListPage.openOrderDetailViaDeeplink(orderID);
            
            Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.getETicketLabel(), either(is("Your e-ticket is here")).or(is("E-tiketmu di sini")).or(is("Waiting for confirmation")).or(is("Menunggu konfirmasi")).or(is("Processing e-ticket")).or(is("Memproses e-tiket")));
        }
    }
    @DataProvider(name="testFlightDomesticOnewayTransitLoginBusinessCriticalDataProvider")
    public Object[][] testFlightDomesticOnewayTransitLoginBusinessCriticalDataProvider(){
        return getObjectParams("FLG_testFlightDomesticOnewayTransitLoginBusinessCritical");
    }

    @Author(names = {"Ashish"})
    @Module(name = "Flight")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test(dataProvider="testFlightInternationalSmartTripDirectLoginBusinessCriticalDataProvider")
    public void FLG_testFlightInternationalSmartTripDirectLoginBusinessCritical(Map<String, String> param) throws ExecutionException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightProductReviewPage<WebDriver> flightProductReviewPage = FlightProductReviewPage.getInstance(session);
        FlightBookingFormPage<WebDriver> flightBookingFormPage = FlightBookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        FlightApi<?> flightApi = new FlightApi<>(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);

        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        int duration = Integer.parseInt(param.get("duration"));
        String airline = param.get("airlineDepart");
        String airlineReturn = param.get("airlineReturn");

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String otherFilter = param.get("otherFilter");

        String currency = param.get("currency");

        step("Find dynamic data for international smart trip, filter used: " + otherFilter);
        FlightDynamicData flightDynamicData = flightApi.findDateRT(
                username,password,
                originAirport,destinationAirport,daysToDepartureDate,duration,
                adultPassengers,childPassengers,infantPassengers,
                tripType,"","",otherFilter,otherFilter,"","",currency,true
        );
        originAirport = flightDynamicData.originAirport;
        destinationAirport = flightDynamicData.destinationAirport;
        String departureDate = CoreFunctions.convertToLongDate(flightDynamicData.departureDate);
        String returnDate = CoreFunctions.convertToLongDate(flightDynamicData.returnDate);
        airline = flightDynamicData.airlineDepart;
        airlineReturn = flightDynamicData.airlineReturn;
        int totalTransitDepart = Math.min(flightDynamicData.totalTransitDepart, 2);
        int totalTransitReturn = Math.min(flightDynamicData.totalTransitReturn, 2);

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Change currency");
        homePage.changeCurrency(currency,username);

        step("Open flights page");
        homePage.openVerticalPageDeeplink("Flights","",currency);
        flightSearchPage.closePopup();

        step("change date using deeplink");
        flightSearchPage.changeDateViaDeeplink(flightDynamicData, tripType);

        step("Toggle trip type { " + tripType + " }");
        flightSearchPage.toggleTriptype(tripType);

        step("Select origin airport");
        flightSearchPage.selectOrigin(originAirport);
        flightSearchPage.closePopup();

        step("Select destination airport");
        flightSearchPage.selectDestination(destinationAirport);

        step("Select departure date");
        flightSearchPage.selectDate(departureDate,returnDate);

        step("Add passenger");
        flightSearchPage.addPassenger(adultPassengers,childPassengers,infantPassengers);

        step("Search flight");
        flightSearchPage.searchFlight();

        step("Departure SRP - filter transit ("+totalTransitDepart+" transit)");
        flightSearchResultPage.waitForPageLoad();
        flightSearchResultPage.openTransitFilter();
        flightSearchResultPage.filterTransitType(totalTransitDepart+" transit");
        flightSearchResultPage.saveFilter();

        step("Departure SRP - filter round-trip deals chip ("+otherFilter+")");
        flightSearchResultPage.filterChips(otherFilter);

        step("Departure SRP - filter airline ("+airline+")");
        flightSearchResultPage.openFilter();
        flightSearchResultPage.filterAirline(airline);
        flightSearchResultPage.saveFilter();

        step("Departure SRP - select flight card");
        flightSearchResultPage.selectFlight();
        flightSearchResultPage.waitForPageLoad();

        step("Return SRP - filter transit ("+totalTransitReturn+" transit)");
        flightSearchResultPage.waitForPageLoad();
        flightSearchResultPage.openTransitFilter();
        flightSearchResultPage.filterTransitType(totalTransitReturn+" transit");
        flightSearchResultPage.saveFilter();

        step("Return SRP - filter round-trip deals chip ("+otherFilter+")");
        flightSearchResultPage.filterChips(otherFilter);

        step("Return SRP - filter airline ("+airlineReturn+")");
        flightSearchResultPage.openFilter();
        flightSearchResultPage.filterAirline(airlineReturn);
        flightSearchResultPage.saveFilter();

        step("Return SRP - select flight card");
        flightSearchResultPage.selectFlight();

        step("Continue to booking form");
        flightProductReviewPage.continueToBookingForm(tripType);

        step("Skip full protection if visible");
        flightBookingFormPage.skipFullProtectionPrp();

        step("verify date format");
        flightBookingFormPage.verifyDateFormat(tripType);

        step("Fill passenger detail");
        flightBookingFormPage.fillPassengerDetail(adultPassengers,childPassengers,infantPassengers,"international",airline);

        step("Continue to second half booking form");
        flightBookingFormPage.continueToSecondHalf();

        step("Open price detail");
        flightBookingFormPage.openPriceDetail();

        step("Verify price detail");
        PriceDetail priceDetail = new PriceDetail();
        priceDetail.tripType = tripType;
        priceDetail.adult = adultPassengers;
        priceDetail.child = childPassengers;
        priceDetail.infant = infantPassengers;
        Assertion.assertThat("Is price breakdown correct", flightBookingFormPage.isPriceBreakdownCorrect(priceDetail,currency), is(true));

        step("Continue to payment");
        flightBookingFormPage.continueToPayment();

        step("Skip full protection");
        flightBookingFormPage.skipFullProtection();
        flightBookingFormPage.baggageAllowancePopup();

        step("Check order ID");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",!orderID.isBlank(),is(true));

        step("choose ATM payment method");
        paymentPage.openPaymentMethod();
        paymentPage.selectPaymentMethod("ATM");

        step("Validate all suppliers are mock issued by airline, skipped if real issued supplier exist");
        boolean isDepartSuppliersMocked = flightApi.areAllSuppliersMockIssuedByAirline(flightDynamicData.airlineCodeDepart, flightDynamicData.originAirport, flightDynamicData.destinationAirport);
        boolean isReturnSuppliersMocked = flightApi.areAllSuppliersMockIssuedByAirline(flightDynamicData.airlineCodeReturn, flightDynamicData.destinationAirport, flightDynamicData.originAirport);

        step("only submit payment if all suppliers are mocked { isDepartSuppliersMocked : " + isDepartSuppliersMocked +" } isReturnSuppliersMocked : " + isReturnSuppliersMocked + " }" );
        if(Configs.ENVIRONMENT != Environment.PROD && isDepartSuppliersMocked && isReturnSuppliersMocked){
            step("Submit payment");
            paymentPage.clickPayButton();

            step("Auto paid order via api");
            paymentPage.postAutoPaidOrder();
            orderListPage.waitForPaymentSuccessful(orderID,15);

            step("Open order list page");
            paymentPage.seeOrderList(orderID);
            orderListPage.refreshTheOrderList(2);

            step("check order status");
            orderListPage.openOrderDetailViaDeeplink(orderID);
            
            Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.getETicketLabel(), either(is("Your e-ticket is here")).or(is("E-tiketmu di sini")).or(is("Waiting for confirmation")).or(is("Menunggu konfirmasi")).or(is("Processing e-ticket")).or(is("Memproses e-tiket")));
        }
    }
    @DataProvider(name="testFlightInternationalSmartTripDirectLoginBusinessCriticalDataProvider")
    public Object[][] testFlightInternationalSmartTripDirectLoginBusinessCriticalDataProvider(){
        return getObjectParams("FLG_testFlightInternationalSmartTripDirectLoginBusinessCritical");
    }

    @Author(names = {"Ashish"})
    @Module(name = "Flight")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test(dataProvider="testFlight100pRefundLoginBusinessCriticalDataProvider")
    public void FLG_testFlight100pRefundLoginBusinessCritical(Map<String, String> param) throws ExecutionException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightProductReviewPage<WebDriver> flightProductReviewPage = FlightProductReviewPage.getInstance(session);
        FlightBookingFormPage<WebDriver> flightBookingFormPage = FlightBookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        FlightApi<?> flightApi = new FlightApi<>(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);

        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        int baggageAmount = Integer.parseInt(param.get("baggage"));
        String airline = param.get("airline");
        String label = param.get("label");
        int index = Integer.parseInt(param.get("index"));
        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String optionalLocation = param.get("optionalLocation");
        String optionalAirline = param.get("optionalAirline");
        String currency = param.get("currency");

        PriceDetail priceDetail = new PriceDetail();
        priceDetail.tripType=tripType;
        priceDetail.adult=adultPassengers;
        priceDetail.child=childPassengers;
        priceDetail.infant=infantPassengers;

        step("Find dynamic data");
        FlightDynamicData flightDynamicData = flightApi.findDate(
                username,password,
                originAirport,destinationAirport,daysToDepartureDate,
                adultPassengers,childPassengers,infantPassengers,
                tripType,airline,"100% refund/baggage",optionalLocation,optionalAirline,currency
        );
        String departureDate =CoreFunctions.convertToLongDate(flightDynamicData.departureDate);
        airline = flightDynamicData.airlineDepart;
        originAirport = flightDynamicData.originAirport;
        destinationAirport = flightDynamicData.destinationAirport;

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType,username,password);

        step("Change currency");
        homePage.changeCurrency(currency,username);

        step("Open flights page");
        homePage.openVerticalPageDeeplink("Flights","",currency);
        flightSearchPage.closePopup();

        step("change date using deeplink");
        flightSearchPage.changeDateViaDeeplink(flightDynamicData, tripType);

        step("Toggle trip type { " + tripType + " }");
        flightSearchPage.toggleTriptype(tripType);

        step("Select origin airport");
        flightSearchPage.selectOrigin(originAirport);
        flightSearchPage.closePopup();

        step("Select destination airport");
        flightSearchPage.selectDestination(destinationAirport);

        step("Select date");
        flightSearchPage.selectDate(departureDate);

        step("Add passenger");
        flightSearchPage.addPassenger(adultPassengers,childPassengers,infantPassengers);

        step("Search flight");
        flightSearchPage.searchFlight();

        step("filter 100% Refund");
        flightSearchResultPage.filterChips("100% Refund");

        step("Open filter");
        flightSearchResultPage.openFilter();

        step("Filter airline");
        flightSearchResultPage.filterAirline(airline);

        step("Save filter");
        flightSearchResultPage.saveFilter();

        step("Click flight detail chevron");
        flightSearchResultPage.selectFlight();

        step("swipe to 100p section");
        flightProductReviewPage.swipeToRegularSection();
        boolean is100pRefundOnPrpDisplayed = flightProductReviewPage.check100pRefundAttribute();
        Assertion.assertThat("Is 100p refund displayed on prp page", is100pRefundOnPrpDisplayed, is(true));

        step("continue to booking page using 100p");
        flightProductReviewPage.continueToBookingPageUsing100pSelectBtn();

        step("Skip full protection if visible");
        flightBookingFormPage.skipFullProtectionPrp();

        step("Fill passenger detail");
        flightBookingFormPage.fillPassengerDetail(adultPassengers,childPassengers,infantPassengers,"domestic",airline);

        step("add addons baggage");
        baggageAmount = flightBookingFormPage.addBaggage();
        String baggagePrice = flightBookingFormPage.getBaggagePrice(baggageAmount);
        flightBookingFormPage.saveAddBaggage();

        step("Continue to second half booking form");
        flightBookingFormPage.continueToSecondHalf();

        step("Add Extra Protection");
        ArrayList<String> extraProtection = flightBookingFormPage.addExtraProtection(index);

        step("Open price detail");
        flightBookingFormPage.openPriceDetail();

        step("Verify price detail");
        priceDetail.tripType=tripType;
        priceDetail.baggageDepartAmount=baggageAmount;
        priceDetail.baggageDepartPrice=baggagePrice;
        priceDetail.extraProtectionTitle = extraProtection.get(0) + " extra protection";
        priceDetail.extraProtectionPrice = extraProtection.get(1);
        boolean is100pRefundTextPresentBookingPage = flightBookingFormPage.offerBreakdown(label,priceDetail);
        Assertion.assertThat("Is 100 percent refund text displayed ", is100pRefundTextPresentBookingPage, is(true));
        Assertion.assertThat("Is price breakdown correct", flightBookingFormPage.isPriceBreakdownCorrect(priceDetail,currency), is(true));

        step("Continue to payment");
        flightBookingFormPage.continueToPayment();

        step("Skip full protection");
        flightBookingFormPage.baggageAllowancePopup();
        flightBookingFormPage.skipFullProtection();
        flightBookingFormPage.baggageAllowancePopup();

        step("Check order ID");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",!orderID.isBlank(),is(true));

        step("open price detail");
        paymentPage.openPriceDetail();
        boolean is100pRefundTextPresentPaymentPage = paymentPage.offerBreakdown();
        Assertion.assertThat("Is 100 percent refund text displayed ", is100pRefundTextPresentPaymentPage, is(true));
        paymentPage.closePriceDetail();

        step("choose Sakuku payment method");
        paymentPage.openPaymentMethod();
        paymentPage.selectPaymentMethod("Sakuku");

        step("Validate all suppliers are mock issued by airline, skipped if real issued supplier exist");
        boolean isDepartSuppliersMocked = flightApi.areAllSuppliersMockIssuedByAirline(flightDynamicData.airlineCodeDepart, flightDynamicData.originAirport, flightDynamicData.destinationAirport);

        step("only submit payment if all suppliers are mocked { isDepartSuppliersMocked: " + isDepartSuppliersMocked +" }");
        if(Configs.ENVIRONMENT != Environment.PROD && isDepartSuppliersMocked){
            step("Submit payment");
            paymentPage.clickPayButton();

            step("Auto paid order via api");
            paymentPage.postAutoPaidOrder();
            orderListPage.waitForPaymentSuccessful(orderID,15);

            step("Open order list page");
            paymentPage.seeOrderList(orderID);
            orderListPage.refreshTheOrderList(2);

            step("check order status");
            orderListPage.openOrderDetailViaDeeplink(orderID);
            
            Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.getETicketLabel(), either(is("Your e-ticket is here")).or(is("E-tiketmu di sini")).or(is("Waiting for confirmation")).or(is("Menunggu konfirmasi")).or(is("Processing e-ticket")).or(is("Memproses e-tiket")));
        }
    }
    @DataProvider(name="testFlight100pRefundLoginBusinessCriticalDataProvider")
    public Object[][] testFlight100pRefundLoginBusinessCriticalDataProvider(){
        return getObjectParams("FLG_testFlight100pRefundLoginBusinessCritical");
    }

    @Author(names = {"Ashish"})
    @Module(name = "Flight")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test(dataProvider="testFlightSortByLowestPriceSRTGuestBusinessCriticalDataProvider")
    public void FLG_testFlightSortByLowestPriceSRTGuestBusinessCritical(Map<String, String> param) throws ExecutionException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightApi<?> flightApi = new FlightApi<>(session);

        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        int duration = Integer.parseInt(param.get("duration"));
        String otherFilter = param.get("otherFilter");
        String currency = param.get("currency");
        String sortByOption = "Lowest price";

        step("Find dynamic data for international smart trip, filter used: " + otherFilter);
        FlightDynamicData flightDynamicData = flightApi.findDateRT(
                "","",
                originAirport,destinationAirport,daysToDepartureDate,duration,
                adultPassengers,childPassengers,infantPassengers,
                tripType,"","",otherFilter,otherFilter,"","",currency,false
        );
        originAirport = flightDynamicData.originAirport;
        destinationAirport = flightDynamicData.destinationAirport;
        String departureDate = CoreFunctions.convertToLongDate(flightDynamicData.departureDate);
        String returnDate = CoreFunctions.convertToLongDate(flightDynamicData.returnDate);

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Logout if already logged in");
        loginPage.loginAsGuest();

        step("Change currency");
        homePage.changeCurrency(currency,"");

        step("Open flights page");
        homePage.openVerticalPageDeeplink("Flights","",currency);
        flightSearchPage.closePopup();

        step("change date using deeplink");
        flightSearchPage.changeDateViaDeeplink(flightDynamicData, tripType);

        step("Toggle trip type { " + tripType + " }");
        flightSearchPage.toggleTriptype(tripType);

        step("Select origin airport");
        flightSearchPage.selectOrigin(originAirport);
        flightSearchPage.closePopup();

        step("Select destination airport");
        flightSearchPage.selectDestination(destinationAirport);

        step("Select departure date");
        flightSearchPage.selectDate(departureDate,returnDate);

        step("Add passenger");
        flightSearchPage.addPassenger(adultPassengers,childPassengers,infantPassengers);

        step("Search flight");
        flightSearchPage.searchFlight();
        flightSearchResultPage.waitForPageLoad();

        step("On departure search result, choose to sort by cheapest price in advanced filter");
        flightSearchResultPage.sortByViaAdvancedFilter(sortByOption);

        step("get the price on date picker");
        double selectedDatePrice = flightSearchResultPage.getPriceOnDatePicker();

        step("get the price on flight card");
        double firstCardPrice = flightSearchResultPage.getFirstFlightCardPrice();
        Assertion.assertThat("Is the price on date picker and flight card the same selectedDatePickerPrice { "+selectedDatePrice + " } first card price after sortBy { " + firstCardPrice +" }" , selectedDatePrice, closeTo(firstCardPrice,0.01));

        step("filter round-trip deals with chips " + otherFilter);
        flightSearchResultPage.filterChips(otherFilter);

        step("Click flight detail chevron");
        flightSearchResultPage.selectFlight();
        flightSearchResultPage.waitForPageLoad();

        step("sort by lowest price");
        flightSearchResultPage.openSortFilter();
        flightSearchResultPage.sortBy(sortByOption);
        flightSearchResultPage.waitForPageLoad();

        step("verify selected sort by filter");
        flightSearchResultPage.verifySortSelected(sortByOption);

        step("get the price on date picker");
        double selectedDatePriceAfterSortReturn = flightSearchResultPage.getPriceOnDatePicker();

        step("get the price of first card");
        double firstCardPriceAfterSortReturn= flightSearchResultPage.getFirstFlightCardPrice();

        step("scroll to delta price desc");
        double getTheLastCardPriceBeforeDeltaDesc = flightSearchResultPage.getLastFlightCardPriceBeforeDeltaDesc();
        flightSearchResultPage.swipeToDeltaPriceDesc();
        double getTheFirstCardPriceAfterDeltaDesc = flightSearchResultPage.getFirstFlightCardPriceAfterDeltaDesc();
        //price of firstCardPriceAfterSortReturn <= getTheLastCardPriceBeforeDeltaDesc and selectedDatePriceAfterSortReturn ~= getTheFirstCardPriceAfterDeltaDesc
        Assertion.assertThat("Is the price on date picker and flight card the same selectedDatePickerPrice { "+selectedDatePriceAfterSortReturn + " } first card price after delta desc { " + getTheFirstCardPriceAfterDeltaDesc +" }" , selectedDatePriceAfterSortReturn, closeTo(getTheFirstCardPriceAfterDeltaDesc, 0.01));
        Assertion.assertThat("Is the price of first card after sort by lowest price less than last card price before delta desc " + firstCardPriceAfterSortReturn + " <= " + getTheLastCardPriceBeforeDeltaDesc, firstCardPriceAfterSortReturn, lessThanOrEqualTo(getTheLastCardPriceBeforeDeltaDesc));
    }

    @Author(names = {"Aditi"})
    @Module(name = "flightInternationalRoundtrip")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test(dataProvider = "testFlightInternationalRoundTripDealsGuestDataProvider")
    public void testFlightInternationalRoundTripDealsGuest(Map<String, String> param) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightProductReviewPage<WebDriver> flightProductReviewPage = FlightProductReviewPage.getInstance(session);
        FlightBookingFormPage<WebDriver> flightBookingFormPage = FlightBookingFormPage.getInstance(session);
        FlightApi<?> flightApi = new FlightApi<>(session);

        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        int duration = Integer.parseInt(param.get("duration"));
        String otherFilter = param.get("otherFilter");
        String currency = param.get("currency");

        step("Find dynamic international round-trip data with Round-trip Deals on departure");
        FlightDynamicData flightDynamicData = flightApi.findDateRT(
                "", "",
                originAirport, destinationAirport, daysToDepartureDate, duration,
                adultPassengers, childPassengers, infantPassengers,
                tripType, "", "", otherFilter, "", "", "", currency
        );
        originAirport = flightDynamicData.originAirport;
        destinationAirport = flightDynamicData.destinationAirport;
        String departureDate = CoreFunctions.convertToLongDate(flightDynamicData.departureDate);
        String returnDate = CoreFunctions.convertToLongDate(flightDynamicData.returnDate);

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Login as guest");
        loginPage.loginAsGuest();

        step("Change currency");
        homePage.changeCurrency(currency, "");

        step("Open flight menu from apps");
        homePage.openVerticalPageDeeplink("Flights", "", currency);
        flightSearchPage.closePopup();

        step("Change date using deeplink");
        flightSearchPage.changeDateViaDeeplink(flightDynamicData, tripType);

        step("Toggle trip type { " + tripType + " }");
        flightSearchPage.toggleTriptype(tripType);

        step("Select origin airport");
        flightSearchPage.selectOrigin(originAirport);
        flightSearchPage.closePopup();

        step("Select destination airport");
        flightSearchPage.selectDestination(destinationAirport);

        step("Select departure and return date");
        flightSearchPage.selectDate(departureDate, returnDate);

        step("Add passenger");
        flightSearchPage.addPassenger(adultPassengers, childPassengers, infantPassengers);

        step("Search international round-trip flight");
        flightSearchPage.searchFlight();
        flightSearchResultPage.waitForPageLoad();

        step("On departure, apply Round-trip Deals filter and select flight");
        flightSearchResultPage.filterChips(otherFilter);
        Assertion.assertThat("Departure flights are displayed after Round-trip Deals filter",
                flightSearchResultPage.isOnSearchResultPage(), is(true));
        flightSearchResultPage.selectFirstFlightWithRoundTripDealsLabel(otherFilter);

        step("On return, choose non Round-trip Deals flight under delta disclaimer");
        flightSearchResultPage.waitForPageLoad();
        flightSearchResultPage.selectFirstFlightWithoutRoundTripDealsAfterDeltaDesc();

        step("Verify PRP is displayed");
        Assertion.assertThat("Product review page should be displayed",
                flightProductReviewPage.isProductReviewPageDisplayed(), is(true));

        step("Continue to booking form from PRP");
        flightProductReviewPage.continueToBookingForm(tripType);
        flightBookingFormPage.skipFullProtectionPrp();

        step("Verify user is on booking form");
        Assertion.assertThat("Booking form page should be displayed",
                flightBookingFormPage.isBookingFormPageDisplayed(), is(true));
    }

    @DataProvider(name="testFlightSortByLowestPriceSRTGuestBusinessCriticalDataProvider")
    public Object[][] testFlightSortByLowestPriceSRTGuestBusinessCriticalDataProvider(){
        return getObjectParams("FLG_testFlightSortByLowestPriceSRTGuestBusinessCritical");
    }

    @Author(names = {"Ashish"})
    @Module(name = "Flight")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test(dataProvider="testCancelFlightLoginBusinessCriticalDataProvider")
    public void FLG_testCancelFlightLoginBusinessCritical(Map<String, String> param) throws ExecutionException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightApi<?> flightApi = new FlightApi<>(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);

        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String departDateApi = CoreFunctions.getRandomLongDateOnRange(daysToDepartureDate,60,"yyyy-MM-dd");
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        ArrayList<String> airlines = CoreFunctions.makeAirlineArrayList(param.get("airline"));

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String currency = param.get("currency");

        step("Create waiting payment order for Flight (using any " + airlines + ")");
        String orderId = flightApi.createWaitForPaymentFlightB2CDomesticFromAirlines(
                username, password,
                originAirport, destinationAirport,
                departDateApi, "ONE_WAY",
                adultPassengers, childPassengers, infantPassengers,
                airlines,
                "Preprod", "Automation"
        );

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Logout if already logged in");
        loginPage.loginAsGuest();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Change currency");
        homePage.changeCurrency(currency,username);

        step("open my order page");
        homePage.openMyOrderPage();

        step("open waiting payment order detail");
        orderListPage.openOrderDetailViaDeeplink(orderId);

        step("Cancel the order");
        orderDetailPage.cancelOrder(orderId);
        Assertion.assertThat("Order should be cancelled", orderDetailPage.getOrderStatus(orderId,8), either(is("You have canceled this order")).or(is("Kamu membatalkan pesanan ini")));
    }
    @DataProvider(name="testCancelFlightLoginBusinessCriticalDataProvider")
    public Object[][] testCancelFlightLoginBusinessCriticalDataProvider(){
        return getObjectParams("FLG_testCancelFlightLoginBusinessCritical");
    }

    @Author(names = {"Aditi"})
    @Module(name = "Flight")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void FLG_testFlightDomesticRoundTripLoginWithoutInsuranceBcc() throws ExecutionException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("FLG_testFlightDomesticRoundTripLoginWithoutInsuranceBcc");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightProductReviewPage<WebDriver> flightProductReviewPage = FlightProductReviewPage.getInstance(session);
        FlightBookingFormPage<WebDriver> flightBookingFormPage = FlightBookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        FlightApi<?> flightApi = new FlightApi<>(session);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        int duration = Integer.parseInt(param.get("duration"));

        String airline = param.get("airline");
        String airlineReturn = param.get("airlineReturn");
        String optionalLocation = param.get("optionalLocation");
        String optionalAirline = param.get("optionalAirline");

        String currency = param.get("currency");

        step("Find dynamic data");
        FlightDynamicData flightDynamicData = flightApi.findDateRT(
                username, password,
                originAirport, destinationAirport, daysToDepartureDate, duration,
                adultPassengers, childPassengers, infantPassengers,
                tripType, "", "", "", "", optionalLocation, optionalAirline, currency
        );
        String departureDate = CoreFunctions.convertToLongDate(flightDynamicData.departureDate);
        String returnDate = CoreFunctions.convertToLongDate(flightDynamicData.returnDate);
        airline = flightDynamicData.airlineDepart;
        airlineReturn = flightDynamicData.airlineReturn;
        originAirport = flightDynamicData.originAirport;
        destinationAirport = flightDynamicData.destinationAirport;

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Change currency");
        homePage.changeCurrency(currency, username);

        step("Open flights page");
        homePage.openVerticalPageDeeplink("Flights", "", currency);
        flightSearchPage.closePopup();

        step("change date using deeplink");
        flightSearchPage.changeDateViaDeeplink(flightDynamicData, tripType);

        step("Toggle trip type { " + tripType + " }");
        flightSearchPage.toggleTriptype(tripType);

        step("Select origin airport");
        flightSearchPage.selectOrigin(originAirport);
        flightSearchPage.closePopup();

        step("Select destination airport");
        flightSearchPage.selectDestination(destinationAirport);

        step("Select departure date");
        flightSearchPage.selectDate(departureDate, returnDate);

        step("Add passenger");
        flightSearchPage.addPassenger(adultPassengers, childPassengers, infantPassengers);

        step("Search flight");
        flightSearchPage.searchFlight();

        step("Open filter");
        flightSearchResultPage.openFilter();

        step("Filter airline");
        flightSearchResultPage.filterAirline(airline);

        step("Save filter");
        flightSearchResultPage.saveFilter();

        step("Click flight detail chevron");
        flightSearchResultPage.selectFlight();

        step("Open filter");
        flightSearchResultPage.openFilter();

        step("Filter airline");
        flightSearchResultPage.filterAirline(airlineReturn);

        step("Save filter");
        flightSearchResultPage.saveFilter();

        step("Click flight detail chevron");
        flightSearchResultPage.selectFlight();

        step("Continue to booking form");
        flightProductReviewPage.continueToBookingForm(tripType);

        step("Skip full protection if visible");
        flightBookingFormPage.skipFullProtectionPrp();

        step("verify date format");
        flightBookingFormPage.verifyDateFormat(tripType);

        step("Fill passenger detail");
        flightBookingFormPage.fillPassengerDetail(adultPassengers, childPassengers, infantPassengers, "domestic", airline);

        step("Continue to second half booking form");
        flightBookingFormPage.continueToSecondHalf();

        step("Remove insurance / extra protection");
        flightBookingFormPage.removeExtraProtectionAll();

        step("Open price detail");
        flightBookingFormPage.openPriceDetail();

        step("Verify price detail (no extra protection)");
        PriceDetail priceDetail = new PriceDetail();
        priceDetail.tripType = tripType;
        priceDetail.adult = adultPassengers;
        priceDetail.child = childPassengers;
        priceDetail.infant = infantPassengers;

        Assertion.assertThat("Is price breakdown correct", flightBookingFormPage.isPriceBreakdownCorrect(priceDetail, currency), is(true));

        step("Continue to payment");
        flightBookingFormPage.continueToPayment();

        step("Skip full protection");
        flightBookingFormPage.skipFullProtection();
        flightBookingFormPage.baggageAllowancePopup();

        step("Check order ID");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id", !orderID.isBlank(), is(true));

        step("Open payment method");
        paymentPage.openPaymentMethod();

        step("Select Sakuku and submit payment");
        paymentPage.selectPaymentMethod("Sakuku");

        step("Validate all suppliers are mock issued by airline, skipped if real issued supplier exist");
        boolean isDepartSuppliersMocked = flightApi.areAllSuppliersMockIssuedByAirline(flightDynamicData.airlineCodeDepart, flightDynamicData.originAirport, flightDynamicData.destinationAirport);
        boolean isReturnSuppliersMocked = flightApi.areAllSuppliersMockIssuedByAirline(flightDynamicData.airlineCodeReturn, flightDynamicData.destinationAirport, flightDynamicData.originAirport);

        step("only submit payment if all suppliers are mocked { isDepartSuppliersMocked : " + isDepartSuppliersMocked +" } isReturnSuppliersMocked : " + isReturnSuppliersMocked + " }" );
        if (Configs.ENVIRONMENT != Environment.PROD && isDepartSuppliersMocked && isReturnSuppliersMocked) {
            step("Submit payment");
            paymentPage.clickPayButton();

            step("Auto paid order via api");
            paymentPage.postAutoPaidOrder();
            orderListPage.waitForPaymentSuccessful(orderID,15);

            step("Open order list page");
            paymentPage.seeOrderList(orderID);
            orderListPage.refreshTheOrderList(2);

            step("check order status");
            orderListPage.openOrderDetailViaDeeplink(orderID);
            
            Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.getETicketLabel(), either(is("Your e-ticket is here")).or(is("E-tiketmu di sini")).or(is("Waiting for confirmation")).or(is("Menunggu konfirmasi")).or(is("Processing e-ticket")).or(is("Memproses e-tiket")));

        }

    }

    @Author(names = {"Aditi"})
    @Module(name = "Flight")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void FLG_testFlightInternationalOneWayLoginAddOnSeatBcc() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("FLG_testFlightInternationalOneWayLoginAddOnSeatBcc");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightProductReviewPage<WebDriver> flightProductReviewPage = FlightProductReviewPage.getInstance(session);
        FlightBookingFormPage<WebDriver> flightBookingFormPage = FlightBookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        FlightApi<?> flightApi = new FlightApi<>(session);

        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");
        String optionalLocation = param.get("optionalLocation");
        String optionalAirline = param.get("optionalAirline");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        String airline = param.get("airline");
        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String cardNumber = param.get("cardNumber");
        String expiry = param.get("expiry");
        String cvv = param.get("cvv");
        String cardName = param.get("cardName");
        String currency = param.get("currency");

        PriceDetail priceDetail = new PriceDetail();
        priceDetail.tripType=tripType;
        priceDetail.adult=adultPassengers;
        priceDetail.child=childPassengers;
        priceDetail.infant=infantPassengers;

        step("Find dynamic data");
        FlightDynamicData flightDynamicData = flightApi.findDate(
                username,password,
                originAirport,destinationAirport,daysToDepartureDate,
                adultPassengers,childPassengers,infantPassengers,
                tripType,airline,"seat",optionalLocation,optionalAirline,currency
        );
        String departureDate =CoreFunctions.convertToLongDate(flightDynamicData.departureDate);
        airline = flightDynamicData.airlineDepart;
        originAirport = flightDynamicData.originAirport;
        destinationAirport = flightDynamicData.destinationAirport;

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Change currency");
        homePage.changeCurrency(currency,"");

        step("Open flights page");
        homePage.openVerticalPageDeeplink("Flights","",currency);
        flightSearchPage.closePopup();

        step("change date using deeplink");
        flightSearchPage.changeDateViaDeeplink(flightDynamicData, tripType);

        step("Toggle trip type { " + tripType + " }");
        flightSearchPage.toggleTriptype(tripType);

        step("Select origin airport");
        flightSearchPage.selectOrigin(originAirport);
        flightSearchPage.closePopup();

        step("Select destination airport");
        flightSearchPage.selectDestination(destinationAirport);

        step("Select date");
        flightSearchPage.selectDate(departureDate);

        step("Add passenger");
        flightSearchPage.addPassenger(adultPassengers,childPassengers,infantPassengers);

        step("Search flight");
        flightSearchPage.searchFlight();

        step("Open filter");
        flightSearchResultPage.openFilter();

        step("Filter airline");
        flightSearchResultPage.filterAirline(airline);

        step("Save filter");
        flightSearchResultPage.saveFilter();

        step("Click flight detail chevron");
        flightSearchResultPage.selectFlight();

        step("Continue to booking form");
        flightProductReviewPage.continueToBookingForm(tripType);

        step("Skip full protection if visible");
        flightBookingFormPage.skipFullProtectionPrp();

        step("verify date format");
        flightBookingFormPage.verifyDateFormat(tripType);

        step("Fill passenger detail");
        flightBookingFormPage.fillPassengerDetail(adultPassengers,childPassengers,infantPassengers,"international",airline);

        step("Continue to second half booking form");
        flightBookingFormPage.continueToSecondHalf();

        step("Add AddOnSeat");
        String seatPrice=flightBookingFormPage.addSeat(priceDetail,currency);

        step("Remove insurance / extra protection");
        flightBookingFormPage.removeExtraProtectionAll();

        step("Open price detail");
        flightBookingFormPage.openPriceDetail();

        step("Verify price detail (no extra protection)");
        priceDetail = new PriceDetail();
        priceDetail.tripType = tripType;
        priceDetail.adult = adultPassengers;
        priceDetail.child = childPassengers;
        priceDetail.infant = infantPassengers;

        Assertion.assertThat("Is price breakdown correct", flightBookingFormPage.isPriceBreakdownCorrect(priceDetail, currency), is(true));

        step("Continue to payment");
        flightBookingFormPage.continueToPayment();

        step("Skip full protection");
        flightBookingFormPage.skipFullProtection();
        flightBookingFormPage.baggageAllowancePopup();

        step("Check order ID");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id", !orderID.isBlank(), is(true));

        step("User make payment");
        paymentPage.checkOrderID();
        paymentPage.selectPaymentMethodFromTileOrDrawer("Credit Card");
        paymentPage.fillCCDetailRealIssued(cardNumber, expiry, cvv, cardName);

        step("Validate all suppliers are mock issued by airline, skipped if real issued supplier exist");
        boolean isDepartSuppliersMocked = flightApi.areAllSuppliersMockIssuedByAirline(flightDynamicData.airlineCodeDepart, flightDynamicData.originAirport, flightDynamicData.destinationAirport);

        step("only submit payment if all suppliers are mocked { isDepartSuppliersMocked: " + isDepartSuppliersMocked +" }");
        if (Configs.ENVIRONMENT != Environment.PROD && isDepartSuppliersMocked) {
            step("Submit payment");
            paymentPage.clickPayButton();
            paymentPage.verifyCreditCard3ds();
            orderListPage.waitForPaymentSuccessful(orderID,10);

            step("Open order list page");
            paymentPage.seeOrderList(orderID);
            orderListPage.refreshTheOrderList(2);

            step("check order status");
            orderListPage.openOrderDetailViaDeeplink(orderID);
            
            Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.getETicketLabel(), either(is("Your e-ticket is here")).or(is("E-tiketmu di sini")).or(is("Waiting for confirmation")).or(is("Menunggu konfirmasi")).or(is("Processing e-ticket")).or(is("Memproses e-tiket")));

        }

    }

    @Author(names = {"Aditi"})
    @Module(name = "Flight")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void FLG_testFlightInternationalSmartTripDirectLoginBcc() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("FLG_testFlightInternationalSmartTripDirectLoginBcc");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightProductReviewPage<WebDriver> flightProductReviewPage = FlightProductReviewPage.getInstance(session);
        FlightBookingFormPage<WebDriver> flightBookingFormPage = FlightBookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        FlightApi<?> flightApi = new FlightApi<>(session);

        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");
        String optionalLocation = param.get("optionalLocation");
        String optionalAirline = param.get("optionalAirline");
        int index = Integer.parseInt(param.get("index"));
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        int duration = Integer.parseInt(param.get("duration"));

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");

        String airline = param.get("airline");
        String otherFilter = param.get("otherFilter");

        String currency = param.get("currency");

        step("Find dynamic data");
        FlightDynamicData flightDynamicData = flightApi.findDateRT(
                username,password,
                originAirport,destinationAirport,daysToDepartureDate,duration,
                adultPassengers,childPassengers,infantPassengers,
                tripType,"","",otherFilter,otherFilter,optionalLocation,optionalAirline,currency
        );
        originAirport = flightDynamicData.originAirport;
        destinationAirport = flightDynamicData.destinationAirport;
        String departureDate = CoreFunctions.convertToLongDate(flightDynamicData.departureDate);
        String returnDate = CoreFunctions.convertToLongDate(flightDynamicData.returnDate);
        airline = flightDynamicData.airlineDepart;
        String airlineReturn = flightDynamicData.airlineReturn;

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Change currency");
        homePage.changeCurrency(currency,username);

        step("Open flights page");
        homePage.openVerticalPageDeeplink("Flights","",currency);
        flightSearchPage.closePopup();

        step("change date using deeplink");
        flightSearchPage.changeDateViaDeeplink(flightDynamicData, tripType);

        step("Toggle trip type { " + tripType + " }");
        flightSearchPage.toggleTriptype(tripType);

        step("Select origin airport");
        flightSearchPage.selectOrigin(originAirport);
        flightSearchPage.closePopup();

        step("Select destination airport");
        flightSearchPage.selectDestination(destinationAirport);

        step("Select departure date");
        flightSearchPage.selectDate(departureDate,returnDate);

        step("Add passenger");
        flightSearchPage.addPassenger(adultPassengers,childPassengers,infantPassengers);

        step("Search flight");
        flightSearchPage.searchFlight();
        if(Configs.ENVIRONMENT == Environment.GK || Configs.ENVIRONMENT == Environment.PREPROD) {
            step("Open filter");
            flightSearchResultPage.openFilter();

            step("Filter airline");
            flightSearchResultPage.filterAirline(airline);

            step("Save filter");
            flightSearchResultPage.saveFilter();
        }else {
            step("filter round-trip deals");
            flightSearchResultPage.filterChips(otherFilter);
        }

        step("Click flight detail chevron");
        flightSearchResultPage.selectFlight();

        if(Configs.ENVIRONMENT == Environment.GK || Configs.ENVIRONMENT == Environment.PREPROD) {
            step("Open filter");
            flightSearchResultPage.openFilter();

            step("Filter airline");
            flightSearchResultPage.filterAirline(airlineReturn);

            step("Save filter");
            flightSearchResultPage.saveFilter();
        }else {
            step("filter round-trip deals");
            flightSearchResultPage.filterChips(otherFilter);
        }

        step("Click flight detail chevron");
        flightSearchResultPage.selectFlight();

        step("Continue to booking form");
        flightProductReviewPage.continueToBookingForm(tripType);

        step("Skip full protection if visible");
        flightBookingFormPage.skipFullProtectionPrp();

        step("verify date format");
        flightBookingFormPage.verifyDateFormat(tripType);

        step("Fill passenger detail");
        flightBookingFormPage.fillPassengerDetail(adultPassengers,childPassengers,infantPassengers,"international",airline);

        step("Continue to second half booking form");
        flightBookingFormPage.continueToSecondHalf();

        step("Remove insurance / extra protection");
        flightBookingFormPage.removeExtraProtectionAll();

        step("Open price detail");
        flightBookingFormPage.openPriceDetail();

        step("Verify price detail");
        PriceDetail priceDetail = new PriceDetail();
        priceDetail.tripType = tripType;
        priceDetail.adult = adultPassengers;
        priceDetail.child = childPassengers;
        priceDetail.infant = infantPassengers;

        Assertion.assertThat("Is price breakdown correct", flightBookingFormPage.isPriceBreakdownCorrect(priceDetail,currency), is(true));

        step("Continue to payment");
        flightBookingFormPage.continueToPayment();

        step("Skip full protection");
        flightBookingFormPage.skipFullProtection();
        flightBookingFormPage.baggageAllowancePopup();

        step("Check order ID");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id", !orderID.isBlank(), is(true));

        step("User make payment");
        paymentPage.checkOrderID();
        paymentPage.selectPaymentMethodFromTileOrDrawer("Sakuku");

        step("Validate all suppliers are mock issued by airline, skipped if real issued supplier exist");
        boolean isDepartSuppliersMocked = flightApi.areAllSuppliersMockIssuedByAirline(flightDynamicData.airlineCodeDepart, flightDynamicData.originAirport, flightDynamicData.destinationAirport);
        boolean isReturnSuppliersMocked = flightApi.areAllSuppliersMockIssuedByAirline(flightDynamicData.airlineCodeReturn, flightDynamicData.destinationAirport, flightDynamicData.originAirport);

        step("only submit payment if all suppliers are mocked { isDepartSuppliersMocked : " + isDepartSuppliersMocked +" } isReturnSuppliersMocked : " + isReturnSuppliersMocked + " }" );
        if (Configs.ENVIRONMENT != Environment.PROD && isDepartSuppliersMocked && isReturnSuppliersMocked) {
            step("Submit payment");
            paymentPage.clickPayButton();
            orderListPage.waitForPaymentSuccessful(orderID,10);

            step("Open order list page");
            paymentPage.seeOrderList(orderID);
            orderListPage.refreshTheOrderList(2);

            step("check order status");
            orderListPage.openOrderDetailViaDeeplink(orderID);
            
            Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.getETicketLabel(), either(is("Your e-ticket is here")).or(is("E-tiketmu di sini")).or(is("Waiting for confirmation")).or(is("Menunggu konfirmasi")).or(is("Processing e-ticket")).or(is("Memproses e-tiket")));

        }

    }

    @Author(names = {"Aditi"})
    @Module(name = "Flight")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void FLG_testFlightVIOneWayRouteLoginBcc() throws InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("FLG_testFlightVIOneWayRouteLoginBcc");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightProductReviewPage<WebDriver> flightProductReviewPage = FlightProductReviewPage.getInstance(session);
        FlightBookingFormPage<WebDriver> flightBookingFormPage = FlightBookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        FlightApi<WebDriver> flightApi = new FlightApi<>(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);

        String tripType = param.get("tripType");
        String location1 = param.get("location1");
        String location1CityName = param.get("location1CityName");
        String location2 = param.get("location2");
        String location2CityName = param.get("location2CityName");
        String location3 = param.get("location3");
        String location3CityName = param.get("location3CityName");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate =CoreFunctions.addToLongDate(currentDate,daysToDepartureDate);

        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        String airline = param.get("airline");
        String transit = param.get("transit");
        String currency = param.get("currency");

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");

        PriceDetail priceDetail = new PriceDetail();
        priceDetail.tripType=tripType;
        priceDetail.adult=adultPassengers;
        priceDetail.child=childPassengers;
        priceDetail.infant=infantPassengers;

        step("Find dynamic data (one-way streaming; airline directory from API response)");
        FlightDynamicData flightDynamicData = flightApi.findDynamicFlightSearchDataOneWayStreaming(
                username,
                password,
                location1,
                location3,
                daysToDepartureDate,
                adultPassengers,
                childPassengers,
                infantPassengers,
                tripType,
                "",
                currency
        );
        Map<String, String> streamingAirlineCodeToName = flightApi.getLastOneWayStreamingAirlineCodeToName();
        if (!streamingAirlineCodeToName.isEmpty() && flightDynamicData.airlineCodeDepart != null) {
            Assertion.assertThat(
                    "Streaming search payload includes the selected airline code",
                    streamingAirlineCodeToName.containsKey(flightDynamicData.airlineCodeDepart),
                    is(true));
        }
        departureDate =CoreFunctions.convertToLongDate(flightDynamicData.departureDate);
        location1 = flightDynamicData.originAirport;
        location3 = flightDynamicData.destinationAirport;

        step("Set the scheduler config to true");
        Assertion.assertThat("Config is set to true", flightApi.toggleScheduleIngestion(true), is(true));

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType,username,password);

        step("Change currency");
        homePage.changeCurrency(currency,username);

        step("Open flights page");
        homePage.openVerticalPageDeeplink("Flights","",currency);
        flightSearchPage.closePopup();

        step("Toggle trip type");
        flightSearchPage.toggleTriptype(tripType);

        step("change date using deeplink");
        flightSearchPage.changeDateViaDeeplink(flightDynamicData, tripType);

        step("Select date");
        flightSearchPage.selectDate(departureDate);

        step("Add passenger");
        flightSearchPage.addPassenger(adultPassengers,childPassengers,infantPassengers);

        //create cache of schedule each segments route
        if(Configs.PLATFORM == Platform.ANDROID){

            step("select location1 { " + location1 +" } and location2 { " + location2 +" }");
            flightSearchPage.selectOrigin(location1);
            flightSearchPage.closePopup();
            flightSearchPage.selectDestination(location2);

            step("Search flight");
            flightSearchPage.searchFlight();
            flightSearchResultPage.waitForPageLoad();
        }else {
            //using deeplink to set the date
            step("select location1 { " + location1 +" } and location2 { " + location2 +" }");
            flightSearchPage.deeplinkWithSelectedDateSrp(location1,location2,location1CityName,location2CityName,daysToDepartureDate,adultPassengers,childPassengers,infantPassengers);
            flightSearchResultPage.waitForPageLoad();
            flightSearchResultPage.changeTrip(tripType);
            flightSearchResultPage.waitForPageLoad();
        }

        step("Back to flights page");
        flightSearchResultPage.backToFlightPage();

        step("select location2 { " + location2 +" } and location3 { " + location3 +" }");
        flightSearchPage.selectOrigin(location2);
        flightSearchPage.closePopup();
        flightSearchPage.selectDestination(location3);

        step("Search flight");
        flightSearchPage.searchFlight();
        flightSearchResultPage.waitForPageLoad();

        step("Back to flights page");
        flightSearchResultPage.backToFlightPage();

        step("select location1 { " + location1 +" } and location3 { " + location3 +" }");
        flightSearchPage.selectOrigin(location1);
        flightSearchPage.closePopup();
        flightSearchPage.selectDestination(location3);

        step("Search flight");
        flightSearchPage.searchFlight();

        step("Use self transfer filter");
        flightSearchPage.selectSelfTransferFilter();

        step("verify airline");
        Assertion.assertThat("is VI airline present",flightSearchResultPage.isVI_AirlineDisplayed(),is(true));

        step("Read first SRP card VI airline names (split on +) and map them");
        List<String> firstCardViAirlineNames = flightSearchResultPage.getFirstFlightCardAirlineNamesSplitByPlus();

        String firstViAirlineCode = flightSearchResultPage
                .resolveAirlineCodeFromStreamingDirectory(streamingAirlineCodeToName, firstCardViAirlineNames.get(0))
                .orElse(null);
        String secondViAirlineCode = flightSearchResultPage
                .resolveAirlineCodeFromStreamingDirectory(streamingAirlineCodeToName, firstCardViAirlineNames.get(1))
                .orElse(null);

        step("Click flight detail chevron");
        flightSearchResultPage.selectFlight();

        step("Tap self-transfer banner and verify Self Transfer Protection included in bottom sheet");
        Assertion.assertThat("Self Transfer Protection included shown in PRP bottom sheet ",flightProductReviewPage.isConnectingFlightProtection(),is(true));

        step("Continue to booking form");
        flightProductReviewPage.continueToBookingForm(tripType);

        step("Skip full protection if visible");
        flightBookingFormPage.skipFullProtectionPrp();

        step("verify date format");
        flightBookingFormPage.verifyDateFormat(tripType);

        step("verify connecting flight protection nudge");
        Assertion.assertThat("is connecting flight protection nudge displayed",flightBookingFormPage.isConnectingFlightProtectionNudgeDisplayed(),is(true));

        step("Fill contact & passenger detail");
        flightBookingFormPage.fillPassengerDetail(adultPassengers,childPassengers,infantPassengers,"domestic",airline);

        step("Continue to second half booking form");
        flightBookingFormPage.continueToSecondHalf();

        step("Verify what you will get");
        Assertion.assertThat("Is what you'll get correct", flightBookingFormPage.isWhatYouGetCorrectVI(), is(true));
        Assertion.assertThat("Validate 'More Benefit' on 'Connecting Flight protection' details will showing Insurance Detail", flightBookingFormPage.verifyMoreBenefit(), is(true));

        step("verify yellow banner");
        Assertion.assertThat("Validate there will be warning message 'You chose connecting flights'",flightBookingFormPage.verifyWarningMassage(),is(true));

        step("Open price detail");
        flightBookingFormPage.openPriceDetail();

        step("Verify price detail");
        Assertion.assertThat("is connecting flight protection auto added",flightBookingFormPage.connectingFlightProtectionDisplayedOnPriceDetail(adultPassengers+childPassengers,currency),is(true));
        Assertion.assertThat("Is price breakdown correct", flightBookingFormPage.isPriceBreakdownCorrect(priceDetail,currency), is(true));

        step("Continue to payment");
        flightBookingFormPage.continueToPayment();

        step("Skip full protection");
        flightBookingFormPage.skipFullProtection();
        flightBookingFormPage.baggageAllowancePopup();

        step("Check order ID");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id", !orderID.isBlank(), is(true));

        step("User make payment");
        paymentPage.checkOrderID();
        paymentPage.selectPaymentMethodFromTileOrDrawer("Sakuku");

        step("Validate all suppliers are mock issued by airline, skipped if real issued supplier exist");
        boolean airline1 = flightApi.areAllSuppliersMockIssuedByAirline(firstViAirlineCode, flightDynamicData.originAirport, flightDynamicData.destinationAirport);
        boolean airline2 = flightApi.areAllSuppliersMockIssuedByAirline(secondViAirlineCode, flightDynamicData.originAirport, flightDynamicData.destinationAirport);
        boolean isDepartSuppliersMocked = airline1 && airline2;

        step("only submit payment if all suppliers are mocked { isDepartSuppliersMocked: " + isDepartSuppliersMocked +" }");
        if (Configs.ENVIRONMENT != Environment.PROD && isDepartSuppliersMocked) {
            step("Submit payment");
            paymentPage.clickPayButton();
            orderListPage.waitForPaymentSuccessful(orderID,10);

            step("Open order list page");
            paymentPage.seeOrderList(orderID);
            orderListPage.refreshTheOrderList(2);

            step("check order status");
            orderListPage.openOrderDetailViaDeeplink(orderID);
            
            Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.getETicketLabel(), either(is("Your e-ticket is here")).or(is("E-tiketmu di sini")).or(is("Waiting for confirmation")).or(is("Menunggu konfirmasi")).or(is("Processing e-ticket")).or(is("Memproses e-tiket")));

        }

    }

    @Author(names = {"Aditi"})
    @Module(name = "Flight")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void FLG_testFlightInternationalRTDifferentAirlineBcc() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("FLG_testFlightInternationalRTDifferentAirlineBcc");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightProductReviewPage<WebDriver> flightProductReviewPage = FlightProductReviewPage.getInstance(session);
        FlightBookingFormPage<WebDriver> flightBookingFormPage = FlightBookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        FlightApi<?> flightApi = new FlightApi<>(session);

        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");
        String optionalLocation = param.get("optionalLocation");
        String optionalAirline = param.get("optionalAirline");
        int index = Integer.parseInt(param.get("index"));
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        int duration = Integer.parseInt(param.get("duration"));

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");

        String airline = param.get("airline");
        String airlineReturn = param.get("airlineReturn");

        String currency = param.get("currency");

        step("Find dynamic data");
        FlightDynamicData flightDynamicData = flightApi.findDateRT(
                username,password,
                originAirport,destinationAirport,daysToDepartureDate,duration,
                adultPassengers,childPassengers,infantPassengers,
                tripType,airline,airlineReturn,"","","","",currency
        );
        originAirport = flightDynamicData.originAirport;
        destinationAirport = flightDynamicData.destinationAirport;
        String departureDate = CoreFunctions.convertToLongDate(flightDynamicData.departureDate);
        String returnDate = CoreFunctions.convertToLongDate(flightDynamicData.returnDate);
        airline = flightDynamicData.airlineDepart;
        airlineReturn = flightDynamicData.airlineReturn;

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Change currency");
        homePage.changeCurrency(currency,username);

        step("Open flights page");
        homePage.openVerticalPageDeeplink("Flights","",currency);
        flightSearchPage.closePopup();

        step("change date using deeplink");
        flightSearchPage.changeDateViaDeeplink(flightDynamicData, tripType);

        step("Toggle trip type { " + tripType + " }");
        flightSearchPage.toggleTriptype(tripType);

        step("Select origin airport");
        flightSearchPage.selectOrigin(originAirport);
        flightSearchPage.closePopup();

        step("Select destination airport");
        flightSearchPage.selectDestination(destinationAirport);

        step("Select departure date");
        flightSearchPage.selectDate(departureDate,returnDate);

        step("Add passenger");
        flightSearchPage.addPassenger(adultPassengers,childPassengers,infantPassengers);

        step("Search flight");
        flightSearchPage.searchFlight();
        if(Configs.ENVIRONMENT == Environment.GK || Configs.ENVIRONMENT == Environment.PREPROD) {
            step("Open filter");
            flightSearchResultPage.openFilter();

            step("Filter airline");
            flightSearchResultPage.filterAirline(airline);

            step("Save filter");
            flightSearchResultPage.saveFilter();
        }

        step("Click flight detail chevron");
        flightSearchResultPage.selectFlight();

        if(Configs.ENVIRONMENT == Environment.GK || Configs.ENVIRONMENT == Environment.PREPROD) {
            step("Open filter");
            flightSearchResultPage.openFilter();

            step("Filter airline");
            flightSearchResultPage.filterAirline(airlineReturn);

            step("Save filter");
            flightSearchResultPage.saveFilter();
        }

        step("Click flight detail chevron");
        flightSearchResultPage.selectFlight();

        step("Continue to booking form");
        flightProductReviewPage.continueToBookingForm(tripType);

        step("Skip full protection if visible");
        flightBookingFormPage.skipFullProtectionPrp();

        step("verify date format");
        flightBookingFormPage.verifyDateFormat(tripType);

        step("Fill passenger detail");
        flightBookingFormPage.fillPassengerDetail(adultPassengers,childPassengers,infantPassengers,"international",airline);

        step("Continue to second half booking form");
        flightBookingFormPage.continueToSecondHalf();

        step("Open price detail");
        flightBookingFormPage.openPriceDetail();

        step("Verify price detail");
        PriceDetail priceDetail = new PriceDetail();
        priceDetail.tripType = tripType;
        priceDetail.adult = adultPassengers;
        priceDetail.child = childPassengers;
        priceDetail.infant = infantPassengers;

        Assertion.assertThat("Is price breakdown correct", flightBookingFormPage.isPriceBreakdownCorrect(priceDetail,currency), is(true));

        step("Continue to payment");
        flightBookingFormPage.continueToPayment();

        step("Skip full protection");
        flightBookingFormPage.skipFullProtection();
        flightBookingFormPage.baggageAllowancePopup();

        step("Check order ID");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id", !orderID.isBlank(), is(true));

        step("User make payment");
        paymentPage.checkOrderID();
        paymentPage.selectPaymentMethodFromTileOrDrawer("Sakuku");

        step("Validate all suppliers are mock issued by airline, skipped if real issued supplier exist");
        boolean isDepartSuppliersMocked = flightApi.areAllSuppliersMockIssuedByAirline(flightDynamicData.airlineCodeDepart, flightDynamicData.originAirport, flightDynamicData.destinationAirport);
        boolean isReturnSuppliersMocked = flightApi.areAllSuppliersMockIssuedByAirline(flightDynamicData.airlineCodeReturn, flightDynamicData.destinationAirport, flightDynamicData.originAirport);

        step("only submit payment if all suppliers are mocked { isDepartSuppliersMocked : " + isDepartSuppliersMocked +" } isReturnSuppliersMocked : " + isReturnSuppliersMocked + " }" );
        if (Configs.ENVIRONMENT != Environment.PROD && isDepartSuppliersMocked && isReturnSuppliersMocked) {
            step("Submit payment");
            paymentPage.clickPayButton();
            orderListPage.waitForPaymentSuccessful(orderID,10);

            step("Open order list page");
            paymentPage.seeOrderList(orderID);
            orderListPage.refreshTheOrderList(2);

            step("check order status");
            orderListPage.openOrderDetailViaDeeplink(orderID);
            
            Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.getETicketLabel(), either(is("Your e-ticket is here")).or(is("E-tiketmu di sini")).or(is("Waiting for confirmation")).or(is("Menunggu konfirmasi")).or(is("Processing e-ticket")).or(is("Memproses e-tiket")));

        }

    }

    @Author(names = {"Aditi"})
    @Module(name = "Flight")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void FLG_testFlightFilterAirlineRTBcc() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("FLG_testFlightFilterAirlineRTBcc");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightProductReviewPage<WebDriver> flightProductReviewPage = FlightProductReviewPage.getInstance(session);
        FlightBookingFormPage<WebDriver> flightBookingFormPage = FlightBookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        FlightApi<?> flightApi = new FlightApi<>(session);

        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        int duration = Integer.parseInt(param.get("duration"));
        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String airline = param.get("airline");
        String airlineReturn = param.get("airlineReturn");
        String currency = param.get("currency");
        int maxFlightCardsAirlineFilterCheck = Integer.parseInt(param.get("index"));

        step("Find dynamic data");
        FlightDynamicData flightDynamicData = flightApi.findDateRT(
                "","",
                originAirport,destinationAirport,daysToDepartureDate,duration,
                adultPassengers,childPassengers,infantPassengers,
                tripType,airline,airlineReturn,"","","","",currency
        );
        originAirport = flightDynamicData.originAirport;
        destinationAirport = flightDynamicData.destinationAirport;
        String departureDate = CoreFunctions.convertToLongDate(flightDynamicData.departureDate);
        String returnDate = CoreFunctions.convertToLongDate(flightDynamicData.returnDate);

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginAsGuest();

        step("Change currency");
        homePage.changeCurrency(currency,username);

        step("Open flights page");
        homePage.openVerticalPageDeeplink("Flights","",currency);
        flightSearchPage.closePopup();

        step("change date using deeplink");
        flightSearchPage.changeDateViaDeeplink(flightDynamicData, tripType);

        step("Toggle trip type { " + tripType + " }");
        flightSearchPage.toggleTriptype(tripType);

        step("Select origin airport");
        flightSearchPage.selectOrigin(originAirport);
        flightSearchPage.closePopup();

        step("Select destination airport");
        flightSearchPage.selectDestination(destinationAirport);

        step("Select departure date");
        flightSearchPage.selectDate(departureDate,returnDate);

        step("Add passenger");
        flightSearchPage.addPassenger(adultPassengers,childPassengers,infantPassengers);

        step("Search flight");
        flightSearchPage.searchFlight();
        if(Configs.ENVIRONMENT == Environment.GK || Configs.ENVIRONMENT == Environment.PREPROD) {
            step("Open filter");
            flightSearchResultPage.openFilter();

            step("Filter airline");
            flightSearchResultPage.filterAirline(airline);

            step("Save filter");
            flightSearchResultPage.saveFilter();

            step("Verify all departure flight cards match airline filter");
            Assertion.assertThat("Every visible card airline matches departure airline filter",
                    flightSearchResultPage.verifyAllVisibleFlightCardsMatchAirlineFilter(airline, maxFlightCardsAirlineFilterCheck), is(true));
        }

        step("Click flight detail chevron");
        flightSearchResultPage.selectFlight();

        if(Configs.ENVIRONMENT == Environment.GK || Configs.ENVIRONMENT == Environment.PREPROD) {
            step("Open filter");
            flightSearchResultPage.openFilter();

            step("Filter airline");
            flightSearchResultPage.filterAirline(airlineReturn);

            step("Save filter");
            flightSearchResultPage.saveFilter();

            step("Verify all return flight cards match airline filter");
            Assertion.assertThat("Every visible card airline matches return airline filter",
                    flightSearchResultPage.verifyAllVisibleFlightCardsMatchAirlineFilter(airlineReturn, maxFlightCardsAirlineFilterCheck), is(true));
        }

    }

    @Author(names = {"Mridul"})
    @Module(name = "corporateCode")
    @Tribe(name = "Corporate")
    @Vertical(name = "B2B")
    @Test
    public void corporateDealsDirectFlightNoTransitInfoOnSrpCard() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("corporateDealsDirectFlightNoTransitInfoOnSrpCard");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        CorporateLoginPage<WebDriver> corporateLoginPage = CorporateLoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);

        String username = param.get("username");
        String password = param.get("password");
        String currency = param.get("currency");
        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");
        String airline = param.get("airline");
        String transit = param.get("transit");

        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Login with corporate account " + username);
        loginPage.loginAsGuest();
        corporateLoginPage.corporateLogin(username, password);

        step("Change currency");
        homePage.changeCurrency(currency, username);

        step("Open flights page");
        homePage.openVerticalPageDeeplink("Flights", "", currency);
        flightSearchPage.closePopup();

        step("change date using deeplink");
        flightSearchPage.changeDateViaDeeplink(daysToDepartureDate, tripType);

        step("Toggle trip type { " + tripType + " }");
        flightSearchPage.toggleTriptype(tripType);

        step("Select origin airport " + originAirport);
        flightSearchPage.selectOrigin(originAirport);
        flightSearchPage.closePopup();

        step("Select destination airport " + destinationAirport);
        flightSearchPage.selectDestination(destinationAirport);

        step("Select date");
        flightSearchPage.selectDate(departureDate);

        step("Add passenger");
        flightSearchPage.addPassenger(adultPassengers, childPassengers, infantPassengers);

        step("Search flight for route which provides corporate deals");
        flightSearchPage.searchFlight();
        flightSearchResultPage.waitForPageLoad();

        step("Filter by { " + transit + " }");
        flightSearchResultPage.openTransitFilter();
        flightSearchResultPage.resetFilter();
        flightSearchResultPage.filterTransitType(transit);
        flightSearchResultPage.saveFilter();
        flightSearchResultPage.waitForPageLoad();

        step("Filter airline with corporate deals { " + airline + " }");
        flightSearchResultPage.openFilter();
        flightSearchResultPage.resetFilter();
        flightSearchResultPage.filterAirline(airline);
        flightSearchResultPage.saveFilter();
        flightSearchResultPage.waitForPageLoad();

        step("Verify corporate deals benefit is present on the card");
        Assertion.assertThat("Corporate deals benefit on the card is present", flightSearchResultPage.gotoCorporateDealsBenefitCard(), is(true));

        step("Click see details on the direct corporate-deal flight card");
        flightSearchResultPage.clickSeeDetailSrp(1);

        step("Verify transit info is NOT shown on the SRP card for a direct flight");
        Assertion.assertThat("Transit info must be hidden on SRP card for a direct (no-transit) flight", flightSearchResultPage.isTransitInfoPresentOnSrpCardDetail(), is(false));
    }

    @Author(names = {"Ashish"})
    @Module(name = "SRP")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void testFlightVerifyUniqueSchedulesAfterAirlineFilter() {
        if (Configs.PLATFORM != Platform.ANDROID) {
            Assertion.skip("testFlightVerifyUniqueSchedulesAfterAirlineFilter is Android-only (iOS not implemented)");
        }

        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testFlightVerifyUniqueSchedulesAfterAirlineFilter");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightApi<?> flightApi = new FlightApi<>(session);

        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        String airline = param.get("airline");
        String currency = param.get("currency");

        String currentDate = CoreFunctions.getCurrentLongDate();

        step("Find dynamic data");
        FlightDynamicData flightDynamicData = flightApi.findDate(
                "","",
                originAirport,destinationAirport,daysToDepartureDate,
                adultPassengers,childPassengers,infantPassengers,
                tripType,airline,"","","",currency
        );
        originAirport = flightDynamicData.originAirport;
        destinationAirport = flightDynamicData.destinationAirport;
        String departureDate = CoreFunctions.convertToLongDate(flightDynamicData.departureDate);
        airline = flightDynamicData.airlineDepart;

        step("Wait for app to launch");
        homePage.gotoHome();

        step("set app as guest user");
        homePage.logoutViaDeeplink();

        step("Change currency: " + currency);
        homePage.changeCurrency(currency, "");

        step("Open flights page");
        homePage.openVerticalPageDeeplink("Flights", "", currency);
        flightSearchPage.closePopup();

        step("change date using deeplink");
        flightSearchPage.changeDateViaDeeplink(daysToDepartureDate, tripType);

        step("Toggle trip type { " + tripType + " }");
        flightSearchPage.toggleTriptype(tripType);

        step("Select origin airport " + originAirport);
        flightSearchPage.selectOrigin(originAirport);
        flightSearchPage.closePopup();

        step("Select destination airport " + destinationAirport);
        flightSearchPage.selectDestination(destinationAirport);

        step("Select date");
        flightSearchPage.selectDate(departureDate);

        step("Add passenger");
        flightSearchPage.addPassenger(adultPassengers, childPassengers, infantPassengers);

        step("Search flight " + originAirport + "-" + destinationAirport);
        flightSearchPage.searchFlight();
        flightSearchResultPage.waitForPageLoad();

        step("Open filter and filter airline by " + airline);
        flightSearchResultPage.openFilter();
        flightSearchResultPage.filterAirline(airline);
        int expectedCardCountFromFilter = flightSearchResultPage.getAirlineFilterOptionCount(airline);
        Assertion.assertThat("Airline filter label should expose a count like 'Airline (N)' for " + airline,
                expectedCardCountFromFilter, greaterThan(0));
        flightSearchResultPage.saveFilter();
        flightSearchResultPage.waitForPageLoad();

        step("Validate each flight card airline, unique schedules, and SRP card count == filter ("
                + expectedCardCountFromFilter + ")");
        Assertion.assertThat("All filtered flight schedules should be unique with airline " + airline
                        + " and SRP card count should match filter (" + expectedCardCountFromFilter + ")",
                flightSearchResultPage.areAllFlightSchedulesUnique(airline, expectedCardCountFromFilter), is(true));
    }

    @Author(names = {"Aditi"})
    @Module(name = "FlightDomesticRoundTrip")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test(dataProvider = "testFlightDomesticRoundTripLoginFreeMealSeatAddonsDataProvider")
    public void testFlightDomesticRoundTripLoginFreeMealSeatAddons(Map<String, String> param) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightProductReviewPage<WebDriver> flightProductReviewPage = FlightProductReviewPage.getInstance(session);
        FlightBookingFormPage<WebDriver> flightBookingFormPage = FlightBookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        FlightApi<?> flightApi = new FlightApi<>(session);

        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int duration = Integer.parseInt(param.get("duration"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        String airline = param.get("airline");
        String airlineReturn = param.get("airlineReturn");
        String username = param.get("username");
        String password = param.get("password");
        String optionalLocation = param.get("optionalLocation");
        String optionalAirline = param.get("optionalAirline");
        String currency = param.get("currency");
        String departFareCriteria = param.get("departFareCriteria");
        String returnFareCriteria = param.get("returnFareCriteria");

        PriceDetail priceDetail = new PriceDetail();
        priceDetail.tripType = tripType;
        priceDetail.adult = adultPassengers;
        priceDetail.child = childPassengers;
        priceDetail.infant = infantPassengers;

        step("Find dynamic data");
        FlightDynamicData flightDynamicData = flightApi.findDateRT(
                username, password,
                originAirport, destinationAirport, daysToDepartureDate, duration,
                adultPassengers, childPassengers, infantPassengers,
                tripType, airline, airlineReturn, "", "", "", "", currency
        );
        String departureDate = CoreFunctions.convertToLongDate(flightDynamicData.departureDate);
        String returnDate = CoreFunctions.convertToLongDate(flightDynamicData.returnDate);

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink and change currency to " + currency);
        homePage.loginIfLoggedOutAndChangeCurrencyViaDeeplink(username, password, currency);

        step("Open flights page");
        homePage.openVerticalPageDeeplink("Flights", "", currency);
        flightSearchPage.closePopup();

        step("change date using deeplink");
        flightSearchPage.changeDateViaDeeplink(flightDynamicData, tripType);

        step("Toggle trip type { " + tripType + " }");
        flightSearchPage.toggleTriptype(tripType);

        step("Select origin airport");
        flightSearchPage.selectOrigin(originAirport);
        flightSearchPage.closePopup();

        step("Select destination airport");
        flightSearchPage.selectDestination(destinationAirport);

        step("Select date");
        flightSearchPage.selectDate(departureDate, returnDate);

        step("Add passenger");
        flightSearchPage.addPassenger(adultPassengers, childPassengers, infantPassengers);

        step("Search flight");
        flightSearchPage.searchFlight();

        step("Open filter and filter depart airline " + airline);
        flightSearchResultPage.openFilter();
        flightSearchResultPage.filterAirline(airline);
        flightSearchResultPage.saveFilter();

        step("Select depart flight");
        flightSearchResultPage.selectFlight();

        step("Open filter and filter return airline " + airlineReturn);
        flightSearchResultPage.openFilter();
        flightSearchResultPage.filterAirline(airlineReturn);
        flightSearchResultPage.saveFilter();

        step("Select return flight");
        flightSearchResultPage.selectFlight();

        step("On PRP select regular fare for depart and fare with pre meal + seat for return");
        flightProductReviewPage.continueToBookingFormWithFareCriteria(tripType, departFareCriteria, returnFareCriteria);

        step("Skip full protection if visible");
        flightBookingFormPage.skipFullProtectionPrp();

        step("verify date format");
        flightBookingFormPage.verifyDateFormat(tripType);

        step("Fill passenger detail");
        flightBookingFormPage.fillPassengerDetail(adultPassengers, childPassengers, infantPassengers, "domestic", airline);

        step("Choose any addon for depart (baggage)");
        int baggageAmount = flightBookingFormPage.addBaggage();
        String baggagePrice = flightBookingFormPage.getBaggagePrice(baggageAmount);
        flightBookingFormPage.saveAddBaggage();
        Assertion.assertThat("Depart baggage addon price should be present", !baggagePrice.isBlank(), is(true));

        step("Continue to second half booking form (completes bundled free meal/seat screens if shown)");
        flightBookingFormPage.continueToSecondHalf();

        step("Select free meals from addons section");
        String freeMealTotal = flightBookingFormPage.selectFreeMealsFromAddons(adultPassengers, childPassengers);
        Assertion.assertThat("Free meal selected from addons", !freeMealTotal.isBlank(), is(true));

        step("Select free seats from addons section");
        String freeSeatTotal = flightBookingFormPage.selectFreeSeatsFromAddons(priceDetail);
        Assertion.assertThat("Free seat selected from addons", !freeSeatTotal.isBlank(), is(true));

        step("Continue to payment");
        flightBookingFormPage.continueToPayment();

        step("Skip full protection");
        flightBookingFormPage.skipFullProtection();
        flightBookingFormPage.baggageAllowancePopup();

        step("Payment page should show order id and departure addons (baggage) plus meal/seat");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id", !orderID.isBlank(), is(true));
        boolean[] addonLines = paymentPage.checkPriceDetailLineItemsDisplayed(
                new String[]{"baggage", "bagasi"},
                new String[]{"meal", "makanan"},
                new String[]{"seat", "kursi"});
        Assertion.assertThat("Departure baggage addon included on payment price detail", addonLines[0], is(true));
        Assertion.assertThat("Meal addon included on payment price detail", addonLines[1], is(true));
        Assertion.assertThat("Seat addon included on payment price detail", addonLines[2], is(true));

        step("Choose payment method");
        paymentPage.openPaymentMethod();
        paymentPage.selectPaymentMethod("Sakuku");

        step("Validate suppliers are mock issued by airline");
        boolean isDepartSuppliersMocked = flightApi.areAllSuppliersMockIssuedByAirline(
                flightDynamicData.airlineCodeDepart, flightDynamicData.originAirport, flightDynamicData.destinationAirport);
        boolean isReturnSuppliersMocked = flightApi.areAllSuppliersMockIssuedByAirline(
                flightDynamicData.airlineCodeReturn, flightDynamicData.destinationAirport, flightDynamicData.originAirport);

        step("Submit booking when all suppliers are mocked");
        if (Configs.ENVIRONMENT != Environment.PROD && isDepartSuppliersMocked && isReturnSuppliersMocked) {
            paymentPage.clickPayButton();
            orderListPage.waitForPaymentSuccessful(orderID, 15);
            paymentPage.seeOrderList(orderID);
            orderListPage.refreshTheOrderList(2);
            orderListPage.openOrderDetailViaDeeplink(orderID);
            
            Assertion.assertThat("e-Ticket on the order detail should be published",
                    orderDetailPage.getETicketLabel(),
                    either(is("Your e-ticket is here")).or(is("E-tiketmu di sini"))
                            .or(is("Waiting for confirmation")).or(is("Menunggu konfirmasi"))
                            .or(is("Processing e-ticket")).or(is("Memproses e-tiket")));
        }
    }

    @Author(names = {"Aditi"})
    @Module(name = "FlightDomesticOneway")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test(dataProvider = "testFlightDomesticOnewayLoginFreeMealSeatMaxAddonsDataProvider")
    public void testFlightDomesticOnewayLoginFreeMealSeatMaxAddons(Map<String, String> param) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightProductReviewPage<WebDriver> flightProductReviewPage = FlightProductReviewPage.getInstance(session);
        FlightBookingFormPage<WebDriver> flightBookingFormPage = FlightBookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        FlightApi<?> flightApi = new FlightApi<>(session);

        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        String airline = param.get("airline");
        String username = param.get("username");
        String password = param.get("password");
        String optionalLocation = param.get("optionalLocation");
        String optionalAirline = param.get("optionalAirline");
        String currency = param.get("currency");
        String fareCriteria = param.get("fareCriteria");

        PriceDetail priceDetail = new PriceDetail();
        priceDetail.tripType = tripType;
        priceDetail.adult = adultPassengers;
        priceDetail.child = childPassengers;
        priceDetail.infant = infantPassengers;

        step("Find dynamic data");
        FlightDynamicData flightDynamicData = flightApi.findDate(
                username, password,
                originAirport, destinationAirport, daysToDepartureDate,
                adultPassengers, childPassengers, infantPassengers,
                tripType, airline, "", optionalLocation, optionalAirline, currency
        );
        String departureDate = CoreFunctions.convertToLongDate(flightDynamicData.departureDate);

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink and change currency to " + currency);
        homePage.loginIfLoggedOutViaDeeplink(username, password);
        homePage.changeCurrency(currency, username);

        step("Open flights page");
        homePage.openVerticalPageDeeplink("Flights", "", currency);
        flightSearchPage.closePopup();

        step("change date using deeplink");
        flightSearchPage.changeDateViaDeeplink(flightDynamicData, tripType);

        step("Toggle trip type { " + tripType + " }");
        flightSearchPage.toggleTriptype(tripType);

        step("Select origin airport");
        flightSearchPage.selectOrigin(originAirport);
        flightSearchPage.closePopup();

        step("Select destination airport");
        flightSearchPage.selectDestination(destinationAirport);

        step("Select date");
        flightSearchPage.selectDate(departureDate);

        step("Add passenger");
        flightSearchPage.addPassenger(adultPassengers, childPassengers, infantPassengers);

        step("Search flight");
        flightSearchPage.searchFlight();

        step("Open filter and filter airline " + airline);
        flightSearchResultPage.openFilter();
        flightSearchResultPage.filterAirline(airline);
        flightSearchResultPage.saveFilter();

        step("Select flight");
        flightSearchResultPage.selectFlight();

        step("On PRP select fare card with pre meal and seat selection");
        flightProductReviewPage.continueToBookingFormWithFareCriteria(tripType, fareCriteria, null);

        step("Skip full protection if visible");
        flightBookingFormPage.skipFullProtectionPrp();

        step("verify date format");
        flightBookingFormPage.verifyDateFormat(tripType);

        step("Fill passenger detail");
        flightBookingFormPage.fillPassengerDetail(adultPassengers, childPassengers, infantPassengers, "domestic", airline);

        step("Continue to second half booking form");
        flightBookingFormPage.continueToSecondHalf();

        step("Add meals from addons until maximum — Add must be disabled after max");
        int mealsAdded = flightBookingFormPage.selectMealsUntilMaximumFromAddons(adultPassengers, childPassengers);
        Assertion.assertThat("At least one meal should be selected before max", mealsAdded > 0, is(true));

        step("Select free seat from addons section");
        String seatPrice = flightBookingFormPage.selectFreeSeatFromAddons(priceDetail);
        Assertion.assertThat("Free seat total should be present", !seatPrice.isBlank(), is(true));

        step("Continue to payment");
        flightBookingFormPage.continueToPayment();

        step("Skip full protection");
        flightBookingFormPage.skipFullProtection();
        flightBookingFormPage.baggageAllowancePopup();

        step("Payment page should show order id with meal and seat addons");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id", !orderID.isBlank(), is(true));
        boolean[] addonLines = paymentPage.checkPriceDetailLineItemsDisplayed(
                new String[]{"meal", "makanan"},
                new String[]{"seat", "kursi"});
        Assertion.assertThat("Meal addon included on payment price detail", addonLines[0], is(true));
        Assertion.assertThat("Seat addon included on payment price detail", addonLines[1], is(true));

        step("User make payment");
        paymentPage.selectPaymentMethodFromTileOrDrawer("Sakuku");

        step("Validate suppliers are mock issued by airline, skipped if real issued supplier exist");
        boolean isDepartSuppliersMocked = flightApi.areAllSuppliersMockIssuedByAirline(
                flightDynamicData.airlineCodeDepart, flightDynamicData.originAirport, flightDynamicData.destinationAirport);

        step("only submit payment if all suppliers are mocked { isDepartSuppliersMocked: " + isDepartSuppliersMocked + " }");
        if (Configs.ENVIRONMENT != Environment.PROD && isDepartSuppliersMocked) {
            step("Submit payment");
            paymentPage.clickPayButton();
            orderListPage.waitForPaymentSuccessful(orderID, 10);

            step("Open order list page");
            paymentPage.seeOrderList(orderID);
            orderListPage.refreshTheOrderList(2);

            step("check order status");
            orderListPage.openOrderDetailViaDeeplink(orderID);
            
            Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.getETicketLabel(),
                    either(is("Your e-ticket is here")).or(is("E-tiketmu di sini"))
                            .or(is("Waiting for confirmation")).or(is("Menunggu konfirmasi"))
                            .or(is("Processing e-ticket")).or(is("Memproses e-tiket")));
        }
    }

    @DataProvider(name="testFlightDomesticRefundAsAddOnPRPDataProvider")
    public Object[][] testFlightDomesticRefundAsAddOnPRPDataProvider(){
        return getObjectParams("testFlightDomesticRefundAsAddOnPRP");
    }


    @DataProvider(name="testFlightLoginInternationalWithoutAdjustmentSRTDataProvider")
    public Object[][] testFlightLoginInternationalWithoutAdjustmentSRTDataProvider(){
        return getObjectParams("testFlightLoginInternationalWithoutAdjustmentSRT");
    }

    @DataProvider(name="testFlightDomesticRoundTripGuestExtraProtectionsDataProvider")
    public Object[][] testFlightDomesticRoundTripGuestExtraProtectionsDataProvider(){
        return getObjectParams("testFlightDomesticRoundTripGuestExtraProtections");
    }

    @DataProvider(name="testFlightDomesticRoundTripLoginExtraProtectionsDataProvider")
    public Object[][] testFlightDomesticRoundTripLoginExtraProtectionsDataProvider(){
        return getObjectParams("testFlightDomesticRoundTripLoginExtraProtections");
    }

    @DataProvider(name = "testFlightInternationalRoundtrip2BookingCodeGuestDataProvider")
    public Object[][] testFlightInternationalRoundtrip2BookingCodeGuestDataProvider() {
        return getObjectParams("testFlightInternationalRoundtrip2BookingCodeGuest");
    }

    @DataProvider(name = "testFlightInternationalRoundtrip2BookingCodeLoginDataProvider")
    public Object[][] testFlightInternationalRoundtrip2BookingCodeLoginDataProvider() {
        return getObjectParams("testFlightInternationalRoundtrip2BookingCodeLogin");
    }

    @DataProvider(name = "testFlightInternationalRoundTripDealsGuestDataProvider")
    public Object[][] testFlightInternationalRoundTripDealsGuestDataProvider() {
        return getObjectParams("testFlightInternationalRoundTripDealsGuest");
    }

    @DataProvider(name = "testFlightInternationalSmartTripDirectGuestDataProvider")
    public Object[][] testFlightInternationalSmartTripDirectGuestDataProvider() {
        return getObjectParams("testFlightInternationalSmartTripDirectGuest");
    }

    @DataProvider(name = "testFlightInternationalSmartTripDirectLoginDataProvider")
    public Object[][] testFlightInternationalSmartTripDirectLoginDataProvider() {
        return getObjectParams("testFlightInternationalSmartTripDirectLogin");
    }

    @DataProvider(name="testFlightDomesticOnewayDirectLoginDataProvider")
    public Object[][] testFlightDomesticOnewayDirectLoginDataProvider(){
        return getObjectParams("testFlightDomesticOnewayDirectLogin");
    }

    @DataProvider(name="testFlightDomesticRoundTripLoginAddOnMealsDataProvider")
    public Object[][] testFlightDomesticRoundTripLoginAddOnMealsDataProvider(){
        return getObjectParams("testFlightDomesticRoundTripLoginAddOnMeals");
    }

    @DataProvider(name="testFlightInternationalOneWayLoginAddOnSeatDataProvider")
    public Object[][] testFlightInternationalOneWayLoginAddOnSeatDataProvider(){
        return getObjectParams("testFlightInternationalOneWayLoginAddOnSeat");
    }

    @DataProvider(name="testFlightGuestBundlingOwDataProvider")
    public Object[][] testFlightGuestBundlingOwDataProvider(){
        return getObjectParams("testFlightGuestBundlingOw");
    }
    @DataProvider(name="testFlightLoginBundlingOwDataProvider")
    public Object[][] testFlightLoginBundlingOwDataProvider(){
        return getObjectParams("testFlightLoginBundlingOw");
    }

    @DataProvider(name="testFlightGuestBundlingRTDataProvider")
    public Object[][] testFlightGuestBundlingRTDataProvider(){
        return getObjectParams("testFlightGuestBundlingRT");
    }
    @DataProvider(name="testFlightLoginBundlingRTDataProvider")
    public Object[][] testFlightLoginBundlingRTDataProvider(){
        return getObjectParams("testFlightLoginBundlingRT");
    }

    @DataProvider(name="testFlightInternationalOneWayGuestAddOnSeatDataProvider")
    public Object[][] testFlightInternationalOneWaGuestAddOnSeatDataProvider(){
        return getObjectParams("testFlightInternationalOneWayGuestAddOnSeat");
    }

    @DataProvider(name="testFlightDomesticRoundTripGuestAddOnMealsDataProvider")
    public Object[][] testFlightDomesticRoundTripGuestAddOnMealsDataProvider(){
        return getObjectParams("testFlightDomesticRoundTripGuestAddOnMeals");
    }

    @DataProvider(name="testFlightDomesticOnewayDirectGuestAddOnBaggageDataProvider")
    public Object[][] testFlightDomesticOnewayDirectGuestAddOnBaggageDataProvider(){
        return getObjectParams("testFlightDomesticOnewayDirectGuestAddOnBaggage");
    }

    @DataProvider(name="testFlightOneWayLoyaltyPlatinumLoginDataProvider")
    public Object[][] testFlightOneWayLoyaltyPlatinumLoginDataProvider() {
        return getObjectParams("testFlightOneWayLoyaltyPlatinumLogin");
    }

    @DataProvider(name="testFlightRTLoyaltyDiamondLoginDataProvider")
    public Object[][] testFlightRTLoyaltyDiamondLoginDataProvider() {
        return getObjectParams("testFlightRTLoyaltyDiamondLogin");
    }

    @DataProvider(name="testFlightRecentSearchLoginDataProvider")
    public Object[][] testFlightRecentSearchLoginDataProvider(){
        return getObjectParams("testFlightRecentSearchLogin");
    }

    @DataProvider(name="testFlightRecentSearchGuestDataProvider")
    public Object[][] testFlightRecentSearchGuestDataProvider(){
        return getObjectParams("testFlightRecentSearchGuest");
    }

    @DataProvider(name="testFlightOneWayLoginWithFinalPriceDataProvider")
    public Object[][] testFlightOneWayLoginWithFinalPriceDataProvider(){
        return getObjectParams("testFlightOneWayLoginWithFinalPrice");
    }

    @DataProvider(name="testFlightOneWayGuestWithDiscountedPriceDataProvider")
    public Object[][] testFlightOneWayGuestWithDiscountedPriceDataProvider(){
        return getObjectParams("testFlightOneWayGuestWithDiscountedPrice");
    }

    @DataProvider(name="testFlightOneWayLoginWithSpecialCampaignDataProvider")
    public Object[][] testFlightOneWayLoginWithSpecialCampaignDataProvider(){
        return getObjectParams("testFlightOneWayLoginWithSpecialCampaign");
    }

    @DataProvider(name="testFlightLoginLoyaltyAndSpecialCampaignDataProvider")
    public Object[][] testFlightLoginLoyaltyAndSpecialCampaignDataProvider(){
        return getObjectParams("testFlightLoginLoyaltyAndSpecialCampaign");
    }

    @DataProvider(name="testFlightLoginDomesticWithoutAdjustmentRTSDataProvider")
    public Object[][] testFlightLoginDomesticWithoutAdjustmentRTSDataProvider(){
        return getObjectParams("testFlightLoginDomesticWithoutAdjustmentRTS");
    }

    @DataProvider(name="testFlight100pRefundLoginDataProvider")
    public Object[][] testFlight100pRefundLoginDataProvider(){
        return getObjectParams("testFlight100pRefundLogin");
    }

    @DataProvider(name="testFlight100pRefundGuestDataProvider")
    public Object[][] testFlight100pRefundGuestDataProvider(){
        return getObjectParams("testFlight100pRefundGuest");
    }

    @DataProvider(name="testFlightCheckMegaphoneIconLoginDataProvider")
    public Object[][] testFlightCheckMegaphoneIconLoginDataProvider(){
        return getObjectParams("testFlightCheckMegaphoneIconLogin");
    }

    @DataProvider(name="testFlightCheckYellowBannerBookingPage2LoginDataProvider")
    public Object[][] testFlightCheckYellowBannerBookingPage2LoginDataProvider(){
        return getObjectParams("testFlightCheckYellowBannerBookingPage2Login");
    }

    @DataProvider(name = "testFlightDomesticRoundTripLoginFreeMealSeatAddonsDataProvider")
    public Object[][] testFlightDomesticRoundTripLoginFreeMealSeatAddonsDataProvider() {
        return getObjectParams("testFlightDomesticRoundTripLoginFreeMealSeatAddons");
    }

    @DataProvider(name = "testFlightDomesticOnewayLoginFreeMealSeatMaxAddonsDataProvider")
    public Object[][] testFlightDomesticOnewayLoginFreeMealSeatMaxAddonsDataProvider() {
        return getObjectParams("testFlightDomesticOnewayLoginFreeMealSeatMaxAddons");
    }
}
