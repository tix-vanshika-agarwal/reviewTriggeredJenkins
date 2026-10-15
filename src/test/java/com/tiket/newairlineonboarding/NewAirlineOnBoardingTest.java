package com.tiket.newairlineonboarding;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Session;
import com.tiket.page.flight.FlightPage;
import com.tiket.page.flight.bookingform.FlightBookingFormPage;
import com.tiket.page.flight.pdp.FlightDescriptionPage;
import com.tiket.page.flight.prp.FlightProductReviewPage;
import com.tiket.page.flight.searchpage.FlightSearchPage;
import com.tiket.page.flight.searchresultpage.FlightSearchResultPage;
import com.tiket.page.home.HomePage;
import com.tiket.page.login.LoginPage;
import com.tiket.page.order.orderdetail.OrderDetailPage;
import com.tiket.page.order.orderlist.OrderListPage;
import com.tiket.page.payment.PaymentPage;
import com.tiket.pojo.flight.FlightBaggageDetail;
import com.tiket.pojo.flight.FlightMealsDetail;
import com.tiket.pojo.flight.FlightSeatDetails;
import com.tiket.pojo.flight.PriceDetail;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import java.util.Map;

import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.Matchers.either;
import static org.hamcrest.core.Is.is;

public class NewAirlineOnBoardingTest extends BaseTest{
    @Author(names = {"Ashish"})
    @Module(name = "PrimeBooking")
    @Tribe(name = "NewAirlineOnBoarding")
    @Vertical(name = "Flight")
    @Test
    public void testLoginBookingFlightOneWayDirect() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testLoginBookingFlightOneWayDirect");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightProductReviewPage<WebDriver> flightProductReviewPage = FlightProductReviewPage.getInstance(session);
        FlightBookingFormPage<WebDriver> flightBookingFormPage = FlightBookingFormPage.getInstance(session);
        FlightPage<WebDriver> flightPage = FlightPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);

        String tripType = param.get("tripType");
        String transit = param.get("transit");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");
        String flightRoute = param.get("flightRoute");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        String airline = param.get("airline");

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");

        String currency = param.get("currency");

        step("Find dynamic data");
        flightPage.setDataOneWay(
                daysToDepartureDate,""
        );
        String departureDate =CoreFunctions.convertToLongDate (flightPage.getDate("departureDate"));

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Change currency");
        homePage.changeCurrency(currency,username);

        step("Open flights page");
        homePage.openVerticalPage("Flights");
        flightSearchPage.closePopup();

        step("Toggle trip type");
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

        step("Change date flight");
        flightSearchResultPage.changeDate(departureDate);

        step("Open & set airline filter");
        flightSearchResultPage.openFilter();
        flightSearchResultPage.filterAirline(airline);
        flightSearchResultPage.saveFilter();

        step("open and set transit filter");
        flightSearchResultPage.openTransitFilter();
        flightSearchResultPage.filterTransitType(transit);
        flightSearchResultPage.saveFilter();

        step("take route detail");
        String route = flightSearchResultPage.getRouteText();

        step("select first airline");
        flightSearchResultPage.selectFlight();

        step("verify airline logo, route and price breakdown correct on prp");
        Assertion.assertThat("is airline logo correct",flightProductReviewPage.isAirlineLogoDisplayedPRP(),is(true));
        Assertion.assertThat("is airline route correct",flightProductReviewPage.isAirlineRouteCorrectPRP(route),is(true));
        Assertion.assertThat("is price breakdown correct",flightProductReviewPage.verifyPriceBreakdownPRP(),is(true));

        step("move to booking page");
        flightProductReviewPage.continueToBookingForm(tripType);

        step("verify date format");
        flightBookingFormPage.verifyDateFormat(tripType);

        step("Fill passenger detail");
        flightBookingFormPage.fillPassengerDetail(adultPassengers,childPassengers,infantPassengers, flightRoute,airline);

        step("move to 2nd half of booking page");
        flightBookingFormPage.continueToSecondHalf();

        step("remove insurance/extra protection");
        flightBookingFormPage.removeExtraProtectionAll();

        step("open price detail");
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

        step("Check order ID");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",!orderID.isBlank(),is(true));

        step("open and verify price detail payment page");
        paymentPage.openPriceDetail();
        Assertion.assertThat("Is price breakdown correct on payment page", paymentPage.isPriceBreakdownCorrect(), is(true));
        paymentPage.closePriceDetail();

        step("Open payment method for order id: "+orderID);
        paymentPage.openPaymentMethod();

        step("Select ATM as payment method");
        paymentPage.selectPaymentMethod("ATM");

        step("Submit payment");
        paymentPage.clickPayButton();

        step("Go to the order list");
        paymentPage.seeOrderList();

        step("check order status");
        Assertion.assertThat("Order is Waiting for payment and E-ticket not issued",  orderListPage.verifyWaitingForPayment(orderID), is(true));
    }

    @Author(names = {"Ashish"})
    @Module(name = "PrimeBooking")
    @Tribe(name = "NewAirlineOnBoarding")
    @Vertical(name = "Flight")
    @Test
    public void testLoginBookingFlightRoundTripDirect() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testLoginBookingFlightRoundTripDirect");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightDescriptionPage<WebDriver> flightDescriptionPage = FlightDescriptionPage.getInstance(session);
        FlightProductReviewPage<WebDriver> flightProductReviewPage = FlightProductReviewPage.getInstance(session);
        FlightBookingFormPage<WebDriver> flightBookingFormPage = FlightBookingFormPage.getInstance(session);
        FlightPage<WebDriver> flightPage = FlightPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);

        String tripType = param.get("tripType");
        String transit = param.get("transit");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");
        String flightRoute = param.get("flightRoute");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int duration = Integer.parseInt(param.get("duration"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        String airline = param.get("airline");

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");

        String currency = param.get("currency");

        step("Find dynamic data");
        flightPage.setDataRoundTrip(
                daysToDepartureDate,duration,airline,airline
        );
        String departureDate = CoreFunctions.convertToLongDate (flightPage.getDate("departureDate"));
        String returnDate = CoreFunctions.convertToLongDate (flightPage.getDate("returnDate"));

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Change currency");
        homePage.changeCurrency(currency,username);

        step("Open flights page");
        homePage.openVerticalPage("Flights");
        flightSearchPage.closePopup();

        step("Toggle trip type");
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

        step("Change date flight");
        flightSearchResultPage.changeDate(departureDate);

        step("Open & set airline filter");
        flightSearchResultPage.openFilter();
        flightSearchResultPage.filterAirline(airline);
        flightSearchResultPage.saveFilter();

        step("open and set transit filter");
        flightSearchResultPage.openTransitFilter();
        flightSearchResultPage.filterTransitType(transit);
        flightSearchResultPage.saveFilter();

        step("get route detail depart");
        String routeDepart = flightSearchResultPage.getRouteText();

        step("select first airline");
        flightSearchResultPage.selectFlight();

        step("verify logo, route and price breakdown on pdp depart");
        flightSearchResultPage.clickPricePolicyTab();
        Assertion.assertThat("is airline logo displayed", flightDescriptionPage.isAirlineLogoDisplayedPDP(),is(true));
        Assertion.assertThat("is route correct",flightDescriptionPage.isAirlineRouteCorrectPDP(routeDepart),is(true));
        Assertion.assertThat("is price breakdown correct",flightSearchResultPage.isPriceSumCorrect(),is(true));

        step("Select ticket");
        flightSearchResultPage.selectTicket();

        step("Change date flight return flight");
        flightSearchResultPage.changeDate(returnDate);

        step("Open & set airline filter");
        flightSearchResultPage.openFilter();
        flightSearchResultPage.filterAirline(airline);
        flightSearchResultPage.saveFilter();

        step("open and set transit filter");
        flightSearchResultPage.openTransitFilter();
        flightSearchResultPage.filterTransitType(transit);
        flightSearchResultPage.saveFilter();

        step("get route detail return");
        String routeReturn = flightSearchResultPage.getRouteText();

        step("select first airline");
        flightSearchResultPage.selectFlight();

        step("verify logo, route and price breakdown on pdp depart");
        flightSearchResultPage.clickPricePolicyTab();
        Assertion.assertThat("is airline logo displayed", flightDescriptionPage.isAirlineLogoDisplayedPDP(),is(true));
        Assertion.assertThat("is route correct",flightDescriptionPage.isAirlineRouteCorrectPDP(routeReturn),is(true));
        Assertion.assertThat("is price breakdown correct",flightSearchResultPage.isPriceSumCorrect(),is(true));

        step("Select ticket");
        flightSearchResultPage.selectTicket();

        step("verify airline logo, route and price breakdown correct on prp");
        Assertion.assertThat("is airline logo correct",flightProductReviewPage.isAirlineLogoDisplayedPRP(),is(true));
        Assertion.assertThat("is airline route correct",flightProductReviewPage.isAirlineRouteCorrectPRP(routeDepart),is(true));
        Assertion.assertThat("is price breakdown correct",flightProductReviewPage.verifyPriceBreakdownPRP(),is(true));

        step("move to booking page");
        flightProductReviewPage.continueToBookingForm(tripType);

        step("verify date format");
        flightBookingFormPage.verifyDateFormat(tripType);

        step("Fill passenger detail");
        flightBookingFormPage.fillPassengerDetail(adultPassengers,childPassengers,infantPassengers, flightRoute,airline);

        step("move to 2nd half of booking page");
        flightBookingFormPage.continueToSecondHalf();

        step("remove insurance/extra protection");
        flightBookingFormPage.removeExtraProtectionAll();

        step("open price detail");
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

        step("Check order ID");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",!orderID.isBlank(),is(true));

        step("open and verify price detail payment page");
        paymentPage.openPriceDetail();
        Assertion.assertThat("Is price breakdown correct on payment page", paymentPage.isPriceBreakdownCorrect(), is(true));
        paymentPage.closePriceDetail();

        step("Open payment method for order id: "+orderID);
        paymentPage.openPaymentMethod();

        step("Select ATM as payment method");
        paymentPage.selectPaymentMethod("ATM");

        step("Submit payment");
        paymentPage.clickPayButton();

        step("Go to the order list");
        paymentPage.seeOrderList();

        step("check order status");
        Assertion.assertThat("Order is Waiting for payment and E-ticket not issued",  orderListPage.verifyWaitingForPayment(orderID), is(true));
    }

    @Author(names = {"Ashish"})
    @Module(name = "PrimeBooking")
    @Tribe(name = "NewAirlineOnBoarding")
    @Vertical(name = "Flight")
    @Test
    public void testLoginBookingFlightRoundTripTransit() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testLoginBookingFlightRoundTripTransit");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightDescriptionPage<WebDriver> flightDescriptionPage = FlightDescriptionPage.getInstance(session);
        FlightProductReviewPage<WebDriver> flightProductReviewPage = FlightProductReviewPage.getInstance(session);
        FlightBookingFormPage<WebDriver> flightBookingFormPage = FlightBookingFormPage.getInstance(session);
        FlightPage<WebDriver> flightPage = FlightPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);

        String tripType = param.get("tripType");
        String transit = param.get("transit");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");
        String flightRoute = param.get("flightRoute");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int duration = Integer.parseInt(param.get("duration"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        String airline = param.get("airline");

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");

        String currency = param.get("currency");

        step("Find dynamic data");
        flightPage.setDataRoundTrip(
                daysToDepartureDate,duration,airline,airline
        );
        String departureDate = CoreFunctions.convertToLongDate (flightPage.getDate("departureDate"));
        String returnDate = CoreFunctions.convertToLongDate (flightPage.getDate("returnDate"));

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Change currency");
        homePage.changeCurrency(currency,username);

        step("Open flights page");
        homePage.openVerticalPage("Flights");
        flightSearchPage.closePopup();

        step("Toggle trip type");
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

        step("Change date flight");
        flightSearchResultPage.changeDate(departureDate);

        step("Open & set airline filter");
        flightSearchResultPage.openFilter();
        flightSearchResultPage.filterAirline(airline);
        flightSearchResultPage.saveFilter();

        step("open and set transit filter");
        flightSearchResultPage.openTransitFilter();
        flightSearchResultPage.filterTransitType(transit);
        flightSearchResultPage.saveFilter();

        step("get route detail depart");
        String routeDepart = flightSearchResultPage.getRouteText();

        step("select first airline");
        flightSearchResultPage.selectFlight();

        step("verify logo, route and price breakdown on pdp depart");
        flightSearchResultPage.clickPricePolicyTab();
        Assertion.assertThat("is airline logo displayed", flightDescriptionPage.isAirlineLogoDisplayedPDP(),is(true));
        Assertion.assertThat("is route correct",flightDescriptionPage.isAirlineRouteCorrectPDP(routeDepart),is(true));
        Assertion.assertThat("is price breakdown correct",flightSearchResultPage.isPriceSumCorrect(),is(true));

        step("Select ticket");
        flightSearchResultPage.selectTicket();

        step("Change date flight return flight");
        flightSearchResultPage.changeDate(returnDate);

        step("Open & set airline filter");
        flightSearchResultPage.openFilter();
        flightSearchResultPage.filterAirline(airline);
        flightSearchResultPage.saveFilter();

        step("open and set transit filter");
        flightSearchResultPage.openTransitFilter();
        flightSearchResultPage.filterTransitType(transit);
        flightSearchResultPage.saveFilter();

        step("get route detail return");
        String routeReturn = flightSearchResultPage.getRouteText();

        step("select first airline");
        flightSearchResultPage.selectFlight();

        step("verify logo, route and price breakdown on pdp depart");
        flightSearchResultPage.clickPricePolicyTab();
        Assertion.assertThat("is airline logo displayed", flightDescriptionPage.isAirlineLogoDisplayedPDP(),is(true));
        Assertion.assertThat("is route correct",flightDescriptionPage.isAirlineRouteCorrectPDP(routeReturn),is(true));
        Assertion.assertThat("is price breakdown correct",flightSearchResultPage.isPriceSumCorrect(),is(true));

        step("Select ticket");
        flightSearchResultPage.selectTicket();

        step("verify airline logo, route and price breakdown correct on prp");
        Assertion.assertThat("is airline logo correct",flightProductReviewPage.isAirlineLogoDisplayedPRP(),is(true));
        Assertion.assertThat("is airline route correct",flightProductReviewPage.isAirlineRouteCorrectPRP(routeDepart),is(true));
        Assertion.assertThat("is price breakdown correct",flightProductReviewPage.verifyPriceBreakdownPRP(),is(true));

        step("move to booking page");
        flightProductReviewPage.continueToBookingForm(tripType);

        step("verify date format");
        flightBookingFormPage.verifyDateFormat(tripType);

        step("Fill passenger detail");
        flightBookingFormPage.fillPassengerDetail(adultPassengers,childPassengers,infantPassengers, flightRoute,airline);

        step("move to 2nd half of booking page");
        flightBookingFormPage.continueToSecondHalf();

        step("remove insurance/extra protection");
        flightBookingFormPage.removeExtraProtectionAll();

        step("open price detail");
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

        step("Check order ID");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",!orderID.isBlank(),is(true));

        step("open and verify price detail payment page");
        paymentPage.openPriceDetail();
        Assertion.assertThat("Is price breakdown correct on payment page", paymentPage.isPriceBreakdownCorrect(), is(true));
        paymentPage.closePriceDetail();

        step("Open payment method for order id: "+orderID);
        paymentPage.openPaymentMethod();

        step("Select ATM as payment method");
        paymentPage.selectPaymentMethod("ATM");

        step("Submit payment");
        paymentPage.clickPayButton();

        step("Go to the order list");
        paymentPage.seeOrderList();

        step("check order status");
        Assertion.assertThat("Order is Waiting for payment and E-ticket not issued",  orderListPage.verifyWaitingForPayment(orderID), is(true));
    }

    @Author(names = {"Ashish"})
    @Module(name = "PrimeBooking")
    @Tribe(name = "NewAirlineOnBoarding")
    @Vertical(name = "Flight")
    @Test
    public void testLoginBookingFlightRoundTripStopOver() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testLoginBookingFlightRoundTripStopOver");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightDescriptionPage<WebDriver> flightDescriptionPage = FlightDescriptionPage.getInstance(session);
        FlightProductReviewPage<WebDriver> flightProductReviewPage = FlightProductReviewPage.getInstance(session);
        FlightBookingFormPage<WebDriver> flightBookingFormPage = FlightBookingFormPage.getInstance(session);
        FlightPage<WebDriver> flightPage = FlightPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);

        String tripType = param.get("tripType");
        String transit = param.get("transit");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");
        String flightRoute = param.get("flightRoute");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int duration = Integer.parseInt(param.get("duration"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        String airline = param.get("airline");

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");

        String currency = param.get("currency");

        step("Find dynamic data");
        flightPage.setDataRoundTrip(
                daysToDepartureDate,duration,airline,airline
        );
        String departureDate = CoreFunctions.convertToLongDate (flightPage.getDate("departureDate"));
        String returnDate = CoreFunctions.convertToLongDate (flightPage.getDate("returnDate"));

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Change currency");
        homePage.changeCurrency(currency,username);

        step("Open flights page");
        homePage.openVerticalPage("Flights");
        flightSearchPage.closePopup();

        step("Toggle trip type");
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

        step("Change date flight");
        flightSearchResultPage.changeDate(departureDate);

        step("Open & set airline filter");
        flightSearchResultPage.openFilter();
        flightSearchResultPage.filterAirline(airline);
        flightSearchResultPage.saveFilter();

        step("open and set transit filter");
        flightSearchResultPage.openTransitFilter();
        flightSearchResultPage.filterTransitType(transit);
        flightSearchResultPage.saveFilter();

        step("get route detail depart");
        String routeDepart = flightSearchResultPage.getRouteText();

        step("select stop over airline");
        flightSearchResultPage.selectFlightStopOver();

        step("verify stop over on pdp");
        Assertion.assertThat("is stopover flight",flightDescriptionPage.verifyStopOver(),is(true));

        step("verify logo, route and price breakdown on pdp depart");
        flightSearchResultPage.clickPricePolicyTab();
        Assertion.assertThat("is airline logo displayed", flightDescriptionPage.isAirlineLogoDisplayedPDP(),is(true));
        Assertion.assertThat("is route correct",flightDescriptionPage.isAirlineRouteCorrectPDP(routeDepart),is(true));
        Assertion.assertThat("is price breakdown correct",flightSearchResultPage.isPriceSumCorrect(),is(true));

        step("Select ticket");
        flightSearchResultPage.selectTicket();

        step("Change date flight return flight");
        flightSearchResultPage.changeDate(returnDate);

        step("Open & set airline filter");
        flightSearchResultPage.openFilter();
        flightSearchResultPage.filterAirline(airline);
        flightSearchResultPage.saveFilter();

        step("open and set transit filter");
        flightSearchResultPage.openTransitFilter();
        flightSearchResultPage.filterTransitType(transit);
        flightSearchResultPage.saveFilter();

        step("get route detail return");
        String routeReturn = flightSearchResultPage.getRouteText();

        step("select stop over airline");
        flightSearchResultPage.selectFlightStopOver();

        step("verify stop over on pdp");
        Assertion.assertThat("is stopover flight",flightDescriptionPage.verifyStopOver(),is(true));

        step("verify logo, route and price breakdown on pdp depart");
        flightSearchResultPage.clickPricePolicyTab();
        Assertion.assertThat("is airline logo displayed", flightDescriptionPage.isAirlineLogoDisplayedPDP(),is(true));
        Assertion.assertThat("is route correct",flightDescriptionPage.isAirlineRouteCorrectPDP(routeReturn),is(true));
        Assertion.assertThat("is price breakdown correct",flightSearchResultPage.isPriceSumCorrect(),is(true));

        step("Select ticket");
        flightSearchResultPage.selectTicket();

        step("verify airline logo, route and price breakdown correct on prp");
        Assertion.assertThat("is airline logo correct",flightProductReviewPage.isAirlineLogoDisplayedPRP(),is(true));
        Assertion.assertThat("is airline route correct",flightProductReviewPage.isAirlineRouteCorrectPRP(routeDepart),is(true));
        Assertion.assertThat("is price breakdown correct",flightProductReviewPage.verifyPriceBreakdownPRP(),is(true));

        step("move to booking page");
        flightProductReviewPage.continueToBookingForm(tripType);

        step("verify date format");
        flightBookingFormPage.verifyDateFormat(tripType);

        step("Fill passenger detail");
        flightBookingFormPage.fillPassengerDetail(adultPassengers,childPassengers,infantPassengers, flightRoute,airline);

        step("move to 2nd half of booking page");
        flightBookingFormPage.continueToSecondHalf();

        step("remove insurance/extra protection");
        flightBookingFormPage.removeExtraProtectionAll();

        step("open price detail");
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

        step("Check order ID");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",!orderID.isBlank(),is(true));

        step("open and verify price detail payment page");
        paymentPage.openPriceDetail();
        Assertion.assertThat("Is price breakdown correct on payment page", paymentPage.isPriceBreakdownCorrect(), is(true));
        paymentPage.closePriceDetail();

        step("Open payment method for order id: "+orderID);
        paymentPage.openPaymentMethod();

        step("Select ATM as payment method");
        paymentPage.selectPaymentMethod("ATM");

        step("Submit payment");
        paymentPage.clickPayButton();

        step("Go to the order list");
        paymentPage.seeOrderList();

        step("check order status");
        Assertion.assertThat("Order is Waiting for payment and E-ticket not issued",  orderListPage.verifyWaitingForPayment(orderID), is(true));
    }

    @Author(names = {"Ashish"})
    @Module(name = "Ancillaries")
    @Tribe(name = "NewAirlineOnBoarding")
    @Vertical(name = "Flight")
    @Test
    public void testLoginBookingFlightOneWayDirectAddOnBaggage() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testLoginBookingFlightOneWayDirectAddOnBaggage");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightProductReviewPage<WebDriver> flightProductReviewPage = FlightProductReviewPage.getInstance(session);
        FlightBookingFormPage<WebDriver> flightBookingFormPage = FlightBookingFormPage.getInstance(session);
        FlightPage<WebDriver> flightPage = FlightPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);

        String tripType = param.get("tripType");
        String transit = param.get("transit");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");
        String flightRoute = param.get("flightRoute");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        int baggageAmount = Integer.parseInt(param.get("baggage"));
        String airline = param.get("airline");

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String currency = param.get("currency");

        PriceDetail priceDetail = new PriceDetail();
        priceDetail.tripType = tripType;
        priceDetail.adult = adultPassengers;
        priceDetail.child = childPassengers;
        priceDetail.infant = infantPassengers;

        step("Find dynamic data");
        flightPage.setDataOneWay(
                daysToDepartureDate,""
        );
        String departureDate =CoreFunctions.convertToLongDate (flightPage.getDate("departureDate"));

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Change currency");
        homePage.changeCurrency(currency,username);

        step("Open flights page");
        homePage.openVerticalPage("Flights");
        flightSearchPage.closePopup();

        step("Toggle trip type");
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

        step("Change date flight");
        flightSearchResultPage.changeDate(departureDate);

        step("Open & set airline filter");
        flightSearchResultPage.openFilter();
        flightSearchResultPage.filterAirline(airline);
        flightSearchResultPage.saveFilter();

        step("open and set transit filter");
        flightSearchResultPage.openTransitFilter();
        flightSearchResultPage.filterTransitType(transit);
        flightSearchResultPage.saveFilter();

        step("take route detail");
        String route = flightSearchResultPage.getRouteText();

        step("select first airline");
        flightSearchResultPage.selectFlight();

        step("verify airline logo, route and price breakdown correct on prp");
        Assertion.assertThat("is airline logo correct",flightProductReviewPage.isAirlineLogoDisplayedPRP(),is(true));
        Assertion.assertThat("is airline route correct",flightProductReviewPage.isAirlineRouteCorrectPRP(route),is(true));
        Assertion.assertThat("is price breakdown correct",flightProductReviewPage.verifyPriceBreakdownPRP(),is(true));

        step("move to booking page");
        flightProductReviewPage.continueToBookingForm(tripType);

        step("verify date format");
        flightBookingFormPage.verifyDateFormat(tripType);

        step("Fill passenger detail");
        flightBookingFormPage.fillPassengerDetail(adultPassengers,childPassengers,infantPassengers, flightRoute,airline);

        step("move to 2nd half of booking page");
        flightBookingFormPage.continueToSecondHalf();

        step("Add baggage");
        baggageAmount = flightBookingFormPage.addBaggage(priceDetail);
        String baggagePrice = flightBookingFormPage.getBaggagePrice(baggageAmount);
        int finalBaggageAmount = flightBookingFormPage.getBaggageAmount(baggageAmount,priceDetail);
        flightBookingFormPage.saveAddBaggage();

        step("remove insurance/extra protection");
        flightBookingFormPage.removeExtraProtectionAll();

        step("open price detail");
        flightBookingFormPage.openPriceDetail();

        step("Verify price detail");
        priceDetail.baggageDepartAmount=finalBaggageAmount;
        priceDetail.baggageDepartPrice=baggagePrice;
        Assertion.assertThat("Is price breakdown correct", flightBookingFormPage.isPriceBreakdownCorrect(priceDetail,currency), is(true));

        step("Continue to payment");
        flightBookingFormPage.continueToPayment();

        step("Skip full protection");
        flightBookingFormPage.skipFullProtection();

        step("Check order ID");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",!orderID.isBlank(),is(true));

        step("open and verify price detail payment page");
        paymentPage.openPriceDetail();
        Assertion.assertThat("Is price breakdown correct on payment page", paymentPage.isPriceBreakdownCorrect(), is(true));
        paymentPage.closePriceDetail();

        step("Open payment method for order id: "+orderID);
        paymentPage.openPaymentMethod();

        step("Select ATM as payment method");
        paymentPage.selectPaymentMethod("ATM");

        step("Submit payment");
        paymentPage.clickPayButton();

        step("Go to the order list");
        paymentPage.seeOrderList();

        step("check order status");
        Assertion.assertThat("Order is Waiting for payment and E-ticket not issued",  orderListPage.verifyWaitingForPayment(orderID), is(true));
    }

    @Author(names = {"Ashish"})
    @Module(name = "Ancillaries")
    @Tribe(name = "NewAirlineOnBoarding")
    @Vertical(name = "Flight")
    @Test
    public void testLoginBookingFlightRoundTripDirectAddOnBaggage() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testLoginBookingFlightRoundTripDirectAddOnBaggage");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightDescriptionPage<WebDriver> flightDescriptionPage = FlightDescriptionPage.getInstance(session);
        FlightProductReviewPage<WebDriver> flightProductReviewPage = FlightProductReviewPage.getInstance(session);
        FlightBookingFormPage<WebDriver> flightBookingFormPage = FlightBookingFormPage.getInstance(session);
        FlightPage<WebDriver> flightPage = FlightPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);

        String tripType = param.get("tripType");
        String transit = param.get("transit");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");
        String flightRoute = param.get("flightRoute");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int duration = Integer.parseInt(param.get("duration"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        int baggageAmount = Integer.parseInt(param.get("baggage"));
        String airline = param.get("airline");

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");

        String currency = param.get("currency");

        PriceDetail priceDetail = new PriceDetail();
        priceDetail.tripType = tripType;
        priceDetail.adult = adultPassengers;
        priceDetail.child = childPassengers;
        priceDetail.infant = infantPassengers;

        step("Find dynamic data");
        flightPage.setDataRoundTrip(
                daysToDepartureDate,duration,airline,airline
        );
        String departureDate = CoreFunctions.convertToLongDate (flightPage.getDate("departureDate"));
        String returnDate = CoreFunctions.convertToLongDate (flightPage.getDate("returnDate"));

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Change currency");
        homePage.changeCurrency(currency,username);

        step("Open flights page");
        homePage.openVerticalPage("Flights");
        flightSearchPage.closePopup();

        step("Toggle trip type");
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

        step("Change date flight");
        flightSearchResultPage.changeDate(departureDate);

        step("Open & set airline filter");
        flightSearchResultPage.openFilter();
        flightSearchResultPage.filterAirline(airline);
        flightSearchResultPage.saveFilter();

        step("open and set transit filter");
        flightSearchResultPage.openTransitFilter();
        flightSearchResultPage.filterTransitType(transit);
        flightSearchResultPage.saveFilter();

        step("get route detail depart");
        String routeDepart = flightSearchResultPage.getRouteText();

        step("select first airline");
        flightSearchResultPage.selectFlight();

        step("verify logo, route and price breakdown on pdp depart");
        flightSearchResultPage.clickPricePolicyTab();
        Assertion.assertThat("is airline logo displayed", flightDescriptionPage.isAirlineLogoDisplayedPDP(),is(true));
        Assertion.assertThat("is route correct",flightDescriptionPage.isAirlineRouteCorrectPDP(routeDepart),is(true));
        Assertion.assertThat("is price breakdown correct",flightSearchResultPage.isPriceSumCorrect(),is(true));

        step("Select ticket");
        flightSearchResultPage.selectTicket();

        step("Change date flight return flight");
        flightSearchResultPage.changeDate(returnDate);

        step("Open & set airline filter");
        flightSearchResultPage.openFilter();
        flightSearchResultPage.filterAirline(airline);
        flightSearchResultPage.saveFilter();

        step("open and set transit filter");
        flightSearchResultPage.openTransitFilter();
        flightSearchResultPage.filterTransitType(transit);
        flightSearchResultPage.saveFilter();

        step("get route detail return");
        String routeReturn = flightSearchResultPage.getRouteText();

        step("select first airline");
        flightSearchResultPage.selectFlight();

        step("verify logo, route and price breakdown on pdp depart");
        flightSearchResultPage.clickPricePolicyTab();
        Assertion.assertThat("is airline logo displayed", flightDescriptionPage.isAirlineLogoDisplayedPDP(),is(true));
        Assertion.assertThat("is route correct",flightDescriptionPage.isAirlineRouteCorrectPDP(routeReturn),is(true));
        Assertion.assertThat("is price breakdown correct",flightSearchResultPage.isPriceSumCorrect(),is(true));

        step("Select ticket");
        flightSearchResultPage.selectTicket();

        step("verify airline logo, route and price breakdown correct on prp");
        Assertion.assertThat("is airline logo correct",flightProductReviewPage.isAirlineLogoDisplayedPRP(),is(true));
        Assertion.assertThat("is airline route correct",flightProductReviewPage.isAirlineRouteCorrectPRP(routeDepart),is(true));
        Assertion.assertThat("is price breakdown correct",flightProductReviewPage.verifyPriceBreakdownPRP(),is(true));

        step("move to booking page");
        flightProductReviewPage.continueToBookingForm(tripType);

        step("verify date format");
        flightBookingFormPage.verifyDateFormat(tripType);

        step("Fill passenger detail");
        flightBookingFormPage.fillPassengerDetail(adultPassengers,childPassengers,infantPassengers, flightRoute,airline);

        step("move to 2nd half of booking page");
        flightBookingFormPage.continueToSecondHalf();

        step("Add baggage");
        FlightBaggageDetail flightBaggageDetail = flightBookingFormPage.addBaggageRoundTrip(baggageAmount,priceDetail,currency);

        step("remove insurance/extra protection");
        flightBookingFormPage.removeExtraProtectionAll();

        step("open price detail");
        flightBookingFormPage.openPriceDetail();

        step("Verify price detail");
        priceDetail.flightBaggageDetail = flightBaggageDetail;
        Assertion.assertThat("Is price breakdown correct", flightBookingFormPage.isPriceBreakdownCorrect(priceDetail,currency), is(true));

        step("Continue to payment");
        flightBookingFormPage.continueToPayment();

        step("Skip full protection");
        flightBookingFormPage.skipFullProtection();

        step("Check order ID");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",!orderID.isBlank(),is(true));

        step("open and verify price detail payment page");
        paymentPage.openPriceDetail();
        Assertion.assertThat("Is price breakdown correct on payment page", paymentPage.isPriceBreakdownCorrect(), is(true));
        paymentPage.closePriceDetail();

        step("Open payment method for order id: "+orderID);
        paymentPage.openPaymentMethod();

        step("Select ATM as payment method");
        paymentPage.selectPaymentMethod("ATM");

        step("Submit payment");
        paymentPage.clickPayButton();

        step("Go to the order list");
        paymentPage.seeOrderList();

        step("check order status");
        Assertion.assertThat("Order is Waiting for payment and E-ticket not issued",  orderListPage.verifyWaitingForPayment(orderID), is(true));
    }

    @Author(names = {"Ashish"})
    @Module(name = "Ancillaries")
    @Tribe(name = "NewAirlineOnBoarding")
    @Vertical(name = "Flight")
    @Test
    public void testLoginBookingFlightRoundTripTransitAddOnBaggage() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testLoginBookingFlightRoundTripTransitAddOnBaggage");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightDescriptionPage<WebDriver> flightDescriptionPage = FlightDescriptionPage.getInstance(session);
        FlightProductReviewPage<WebDriver> flightProductReviewPage = FlightProductReviewPage.getInstance(session);
        FlightBookingFormPage<WebDriver> flightBookingFormPage = FlightBookingFormPage.getInstance(session);
        FlightPage<WebDriver> flightPage = FlightPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);

        String tripType = param.get("tripType");
        String transit = param.get("transit");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");
        String flightRoute = param.get("flightRoute");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int duration = Integer.parseInt(param.get("duration"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        int baggageAmount = Integer.parseInt(param.get("baggage"));
        String airline = param.get("airline");

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");

        String currency = param.get("currency");

        PriceDetail priceDetail = new PriceDetail();
        priceDetail.tripType = tripType;
        priceDetail.adult = adultPassengers;
        priceDetail.child = childPassengers;
        priceDetail.infant = infantPassengers;

        step("Find dynamic data");
        flightPage.setDataRoundTrip(
                daysToDepartureDate,duration,airline,airline
        );
        String departureDate = CoreFunctions.convertToLongDate (flightPage.getDate("departureDate"));
        String returnDate = CoreFunctions.convertToLongDate (flightPage.getDate("returnDate"));

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Change currency");
        homePage.changeCurrency(currency,username);

        step("Open flights page");
        homePage.openVerticalPage("Flights");
        flightSearchPage.closePopup();

        step("Toggle trip type");
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

        step("Change date flight");
        flightSearchResultPage.changeDate(departureDate);

        step("Open & set airline filter");
        flightSearchResultPage.openFilter();
        flightSearchResultPage.filterAirline(airline);
        flightSearchResultPage.saveFilter();

        step("open and set transit filter");
        flightSearchResultPage.openTransitFilter();
        flightSearchResultPage.filterTransitType(transit);
        flightSearchResultPage.saveFilter();

        step("get route detail depart");
        String routeDepart = flightSearchResultPage.getRouteText();

        step("select first airline");
        flightSearchResultPage.selectFlight();

        step("verify logo, route and price breakdown on pdp depart");
        flightSearchResultPage.clickPricePolicyTab();
        Assertion.assertThat("is airline logo displayed", flightDescriptionPage.isAirlineLogoDisplayedPDP(),is(true));
        Assertion.assertThat("is route correct",flightDescriptionPage.isAirlineRouteCorrectPDP(routeDepart),is(true));
        Assertion.assertThat("is price breakdown correct",flightSearchResultPage.isPriceSumCorrect(),is(true));

        step("Select ticket");
        flightSearchResultPage.selectTicket();

        step("Change date flight return flight");
        flightSearchResultPage.changeDate(returnDate);

        step("Open & set airline filter");
        flightSearchResultPage.openFilter();
        flightSearchResultPage.filterAirline(airline);
        flightSearchResultPage.saveFilter();

        step("open and set transit filter");
        flightSearchResultPage.openTransitFilter();
        flightSearchResultPage.filterTransitType(transit);
        flightSearchResultPage.saveFilter();

        step("get route detail return");
        String routeReturn = flightSearchResultPage.getRouteText();

        step("select first airline");
        flightSearchResultPage.selectFlight();

        step("verify logo, route and price breakdown on pdp depart");
        flightSearchResultPage.clickPricePolicyTab();
        Assertion.assertThat("is airline logo displayed", flightDescriptionPage.isAirlineLogoDisplayedPDP(),is(true));
        Assertion.assertThat("is route correct",flightDescriptionPage.isAirlineRouteCorrectPDP(routeReturn),is(true));
        Assertion.assertThat("is price breakdown correct",flightSearchResultPage.isPriceSumCorrect(),is(true));

        step("Select ticket");
        flightSearchResultPage.selectTicket();

        step("verify airline logo, route and price breakdown correct on prp");
        Assertion.assertThat("is airline logo correct",flightProductReviewPage.isAirlineLogoDisplayedPRP(),is(true));
        Assertion.assertThat("is airline route correct",flightProductReviewPage.isAirlineRouteCorrectPRP(routeDepart),is(true));
        Assertion.assertThat("is price breakdown correct",flightProductReviewPage.verifyPriceBreakdownPRP(),is(true));

        step("move to booking page");
        flightProductReviewPage.continueToBookingForm(tripType);

        step("verify date format");
        flightBookingFormPage.verifyDateFormat(tripType);

        step("Fill passenger detail");
        flightBookingFormPage.fillPassengerDetail(adultPassengers,childPassengers,infantPassengers, flightRoute,airline);

        step("move to 2nd half of booking page");
        flightBookingFormPage.continueToSecondHalf();

        step("Add baggage");
        FlightBaggageDetail flightBaggageDetail = flightBookingFormPage.addBaggageRoundTrip(baggageAmount,priceDetail,currency);

        step("remove insurance/extra protection");
        flightBookingFormPage.removeExtraProtectionAll();

        step("open price detail");
        flightBookingFormPage.openPriceDetail();

        step("Verify price detail");
        priceDetail.flightBaggageDetail = flightBaggageDetail;
        Assertion.assertThat("Is price breakdown correct", flightBookingFormPage.isPriceBreakdownCorrect(priceDetail,currency), is(true));

        step("Continue to payment");
        flightBookingFormPage.continueToPayment();

        step("Skip full protection");
        flightBookingFormPage.skipFullProtection();

        step("Check order ID");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",!orderID.isBlank(),is(true));

        step("open and verify price detail payment page");
        paymentPage.openPriceDetail();
        Assertion.assertThat("Is price breakdown correct on payment page", paymentPage.isPriceBreakdownCorrect(), is(true));
        paymentPage.closePriceDetail();

        step("Open payment method for order id: "+orderID);
        paymentPage.openPaymentMethod();

        step("Select ATM as payment method");
        paymentPage.selectPaymentMethod("ATM");

        step("Submit payment");
        paymentPage.clickPayButton();

        step("Go to the order list");
        paymentPage.seeOrderList();

        step("check order status");
        Assertion.assertThat("Order is Waiting for payment and E-ticket not issued",  orderListPage.verifyWaitingForPayment(orderID), is(true));
    }

    @Author(names = {"Ashish"})
    @Module(name = "Ancillaries")
    @Tribe(name = "NewAirlineOnBoarding")
    @Vertical(name = "Flight")
    @Test
    public void testLoginBookingFlightRoundTripStopOverAddOnBaggage() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testLoginBookingFlightRoundTripStopOverAddOnBaggage");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightDescriptionPage<WebDriver> flightDescriptionPage = FlightDescriptionPage.getInstance(session);
        FlightProductReviewPage<WebDriver> flightProductReviewPage = FlightProductReviewPage.getInstance(session);
        FlightBookingFormPage<WebDriver> flightBookingFormPage = FlightBookingFormPage.getInstance(session);
        FlightPage<WebDriver> flightPage = FlightPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);

        String tripType = param.get("tripType");
        String transit = param.get("transit");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");
        String flightRoute = param.get("flightRoute");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int duration = Integer.parseInt(param.get("duration"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        int baggageAmount = Integer.parseInt(param.get("baggage"));
        String airline = param.get("airline");

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");

        String currency = param.get("currency");

        PriceDetail priceDetail = new PriceDetail();
        priceDetail.tripType = tripType;
        priceDetail.adult = adultPassengers;
        priceDetail.child = childPassengers;
        priceDetail.infant = infantPassengers;

        step("Find dynamic data");
        flightPage.setDataRoundTrip(
                daysToDepartureDate,duration,airline,airline
        );
        String departureDate = CoreFunctions.convertToLongDate (flightPage.getDate("departureDate"));
        String returnDate = CoreFunctions.convertToLongDate (flightPage.getDate("returnDate"));

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Change currency");
        homePage.changeCurrency(currency,username);

        step("Open flights page");
        homePage.openVerticalPage("Flights");
        flightSearchPage.closePopup();

        step("Toggle trip type");
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

        step("Change date flight");
        flightSearchResultPage.changeDate(departureDate);

        step("Open & set airline filter");
        flightSearchResultPage.openFilter();
        flightSearchResultPage.filterAirline(airline);
        flightSearchResultPage.saveFilter();

        step("open and set transit filter");
        flightSearchResultPage.openTransitFilter();
        flightSearchResultPage.filterTransitType(transit);
        flightSearchResultPage.saveFilter();

        step("get route detail depart");
        String routeDepart = flightSearchResultPage.getRouteText();

        step("select stop over airline");
        flightSearchResultPage.selectFlightStopOver();

        step("verify stop over on pdp");
        Assertion.assertThat("is stopover flight",flightDescriptionPage.verifyStopOver(),is(true));

        step("verify logo, route and price breakdown on pdp depart");
        flightSearchResultPage.clickPricePolicyTab();
        Assertion.assertThat("is airline logo displayed", flightDescriptionPage.isAirlineLogoDisplayedPDP(),is(true));
        Assertion.assertThat("is route correct",flightDescriptionPage.isAirlineRouteCorrectPDP(routeDepart),is(true));
        Assertion.assertThat("is price breakdown correct",flightSearchResultPage.isPriceSumCorrect(),is(true));

        step("Select ticket");
        flightSearchResultPage.selectTicket();

        step("Change date flight return flight");
        flightSearchResultPage.changeDate(returnDate);

        step("Open & set airline filter");
        flightSearchResultPage.openFilter();
        flightSearchResultPage.filterAirline(airline);
        flightSearchResultPage.saveFilter();

        step("open and set transit filter");
        flightSearchResultPage.openTransitFilter();
        flightSearchResultPage.filterTransitType(transit);
        flightSearchResultPage.saveFilter();

        step("get route detail return");
        String routeReturn = flightSearchResultPage.getRouteText();

        step("select stop over airline");
        flightSearchResultPage.selectFlightStopOver();

        step("verify stop over on pdp");
        Assertion.assertThat("is stopover flight",flightDescriptionPage.verifyStopOver(),is(true));

        step("verify logo, route and price breakdown on pdp depart");
        flightSearchResultPage.clickPricePolicyTab();
        Assertion.assertThat("is airline logo displayed", flightDescriptionPage.isAirlineLogoDisplayedPDP(),is(true));
        Assertion.assertThat("is route correct",flightDescriptionPage.isAirlineRouteCorrectPDP(routeReturn),is(true));
        Assertion.assertThat("is price breakdown correct",flightSearchResultPage.isPriceSumCorrect(),is(true));

        step("Select ticket");
        flightSearchResultPage.selectTicket();

        step("verify airline logo, route and price breakdown correct on prp");
        Assertion.assertThat("is airline logo correct",flightProductReviewPage.isAirlineLogoDisplayedPRP(),is(true));
        Assertion.assertThat("is airline route correct",flightProductReviewPage.isAirlineRouteCorrectPRP(routeDepart),is(true));
        Assertion.assertThat("is price breakdown correct",flightProductReviewPage.verifyPriceBreakdownPRP(),is(true));

        step("move to booking page");
        flightProductReviewPage.continueToBookingForm(tripType);

        step("verify date format");
        flightBookingFormPage.verifyDateFormat(tripType);

        step("Fill passenger detail");
        flightBookingFormPage.fillPassengerDetail(adultPassengers,childPassengers,infantPassengers, flightRoute,airline);

        step("move to 2nd half of booking page");
        flightBookingFormPage.continueToSecondHalf();

        step("Add baggage");
        FlightBaggageDetail flightBaggageDetail = flightBookingFormPage.addBaggageRoundTrip(baggageAmount,priceDetail,currency);

        step("remove insurance/extra protection");
        flightBookingFormPage.removeExtraProtectionAll();

        step("open price detail");
        flightBookingFormPage.openPriceDetail();

        step("Verify price detail");
        priceDetail.flightBaggageDetail = flightBaggageDetail;
        Assertion.assertThat("Is price breakdown correct", flightBookingFormPage.isPriceBreakdownCorrect(priceDetail,currency), is(true));

        step("Continue to payment");
        flightBookingFormPage.continueToPayment();

        step("Skip full protection");
        flightBookingFormPage.skipFullProtection();

        step("Check order ID");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",!orderID.isBlank(),is(true));

        step("open and verify price detail payment page");
        paymentPage.openPriceDetail();
        Assertion.assertThat("Is price breakdown correct on payment page", paymentPage.isPriceBreakdownCorrect(), is(true));
        paymentPage.closePriceDetail();

        step("Open payment method for order id: "+orderID);
        paymentPage.openPaymentMethod();

        step("Select ATM as payment method");
        paymentPage.selectPaymentMethod("ATM");

        step("Submit payment");
        paymentPage.clickPayButton();

        step("Go to the order list");
        paymentPage.seeOrderList();

        step("check order status");
        Assertion.assertThat("Order is Waiting for payment and E-ticket not issued",  orderListPage.verifyWaitingForPayment(orderID), is(true));
    }

    @Author(names = {"Ashish"})
    @Module(name = "Ancillaries")
    @Tribe(name = "NewAirlineOnBoarding")
    @Vertical(name = "Flight")
    @Test
    public void testLoginBookingFlightOneWayDirectAddOnMeals() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testLoginBookingFlightOneWayDirectAddOnMeals");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightProductReviewPage<WebDriver> flightProductReviewPage = FlightProductReviewPage.getInstance(session);
        FlightBookingFormPage<WebDriver> flightBookingFormPage = FlightBookingFormPage.getInstance(session);
        FlightPage<WebDriver> flightPage = FlightPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);

        String tripType = param.get("tripType");
        String transit = param.get("transit");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");
        String flightRoute = param.get("flightRoute");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        String airline = param.get("airline");

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");

        String currency = param.get("currency");

        step("Find dynamic data");
        flightPage.setDataOneWay(
                daysToDepartureDate,""
        );
        String departureDate =CoreFunctions.convertToLongDate (flightPage.getDate("departureDate"));

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Change currency");
        homePage.changeCurrency(currency,username);

        step("Open flights page");
        homePage.openVerticalPage("Flights");
        flightSearchPage.closePopup();

        step("Toggle trip type");
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

        step("Change date flight");
        flightSearchResultPage.changeDate(departureDate);

        step("Open & set airline filter");
        flightSearchResultPage.openFilter();
        flightSearchResultPage.filterAirline(airline);
        flightSearchResultPage.saveFilter();

        step("open and set transit filter");
        flightSearchResultPage.openTransitFilter();
        flightSearchResultPage.filterTransitType(transit);
        flightSearchResultPage.saveFilter();

        step("take route detail");
        String route = flightSearchResultPage.getRouteText();

        step("select first airline");
        flightSearchResultPage.selectFlight();

        step("verify airline logo, route and price breakdown correct on prp");
        Assertion.assertThat("is airline logo correct",flightProductReviewPage.isAirlineLogoDisplayedPRP(),is(true));
        Assertion.assertThat("is airline route correct",flightProductReviewPage.isAirlineRouteCorrectPRP(route),is(true));
        Assertion.assertThat("is price breakdown correct",flightProductReviewPage.verifyPriceBreakdownPRP(),is(true));

        step("move to booking page");
        flightProductReviewPage.continueToBookingForm(tripType);

        step("verify date format");
        flightBookingFormPage.verifyDateFormat(tripType);

        step("Fill passenger detail");
        flightBookingFormPage.fillPassengerDetail(adultPassengers,childPassengers,infantPassengers, flightRoute,airline);

        step("move to 2nd half of booking page");
        flightBookingFormPage.continueToSecondHalf();

        step("Add AddOnMeals");
        String mealPrice=flightBookingFormPage.addMeals(adultPassengers,childPassengers,currency);

        step("remove insurance/extra protection");
        flightBookingFormPage.removeExtraProtectionAll();

        step("open price detail");
        flightBookingFormPage.openPriceDetail();

        step("Verify price detail");
        PriceDetail priceDetail = new PriceDetail();
        priceDetail.tripType = tripType;
        priceDetail.adult = adultPassengers;
        priceDetail.child = childPassengers;
        priceDetail.infant = infantPassengers;
        priceDetail.mealPrice=mealPrice;
        Assertion.assertThat("Is price breakdown correct", flightBookingFormPage.isPriceBreakdownCorrect(priceDetail,currency), is(true));

        step("Continue to payment");
        flightBookingFormPage.continueToPayment();

        step("Skip full protection");
        flightBookingFormPage.skipFullProtection();

        step("Check order ID");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",!orderID.isBlank(),is(true));

        step("open and verify price detail payment page");
        paymentPage.openPriceDetail();
        Assertion.assertThat("Is price breakdown correct on payment page", paymentPage.isPriceBreakdownCorrect(), is(true));
        paymentPage.closePriceDetail();

        step("Open payment method for order id: "+orderID);
        paymentPage.openPaymentMethod();

        step("Select ATM as payment method");
        paymentPage.selectPaymentMethod("ATM");

        step("Submit payment");
        paymentPage.clickPayButton();

        step("Go to the order list");
        paymentPage.seeOrderList();

        step("check order status");
        Assertion.assertThat("Order is Waiting for payment and E-ticket not issued",  orderListPage.verifyWaitingForPayment(orderID), is(true));
    }

    @Author(names = {"Ashish"})
    @Module(name = "Ancillaries")
    @Tribe(name = "NewAirlineOnBoarding")
    @Vertical(name = "Flight")
    @Test
    public void testLoginBookingFlightRoundTripDirectAddOnMeals() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testLoginBookingFlightRoundTripDirectAddOnMeals");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightDescriptionPage<WebDriver> flightDescriptionPage = FlightDescriptionPage.getInstance(session);
        FlightProductReviewPage<WebDriver> flightProductReviewPage = FlightProductReviewPage.getInstance(session);
        FlightBookingFormPage<WebDriver> flightBookingFormPage = FlightBookingFormPage.getInstance(session);
        FlightPage<WebDriver> flightPage = FlightPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);

        String tripType = param.get("tripType");
        String transit = param.get("transit");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");
        String flightRoute = param.get("flightRoute");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int duration = Integer.parseInt(param.get("duration"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        String airline = param.get("airline");

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");

        String currency = param.get("currency");

        step("Find dynamic data");
        flightPage.setDataRoundTrip(
                daysToDepartureDate,duration,airline,airline
        );
        String departureDate = CoreFunctions.convertToLongDate (flightPage.getDate("departureDate"));
        String returnDate = CoreFunctions.convertToLongDate (flightPage.getDate("returnDate"));

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Change currency");
        homePage.changeCurrency(currency,username);

        step("Open flights page");
        homePage.openVerticalPage("Flights");
        flightSearchPage.closePopup();

        step("Toggle trip type");
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

        step("Change date flight");
        flightSearchResultPage.changeDate(departureDate);

        step("Open & set airline filter");
        flightSearchResultPage.openFilter();
        flightSearchResultPage.filterAirline(airline);
        flightSearchResultPage.saveFilter();

        step("open and set transit filter");
        flightSearchResultPage.openTransitFilter();
        flightSearchResultPage.filterTransitType(transit);
        flightSearchResultPage.saveFilter();

        step("get route detail depart");
        String routeDepart = flightSearchResultPage.getRouteText();

        step("select first airline");
        flightSearchResultPage.selectFlight();

        step("verify logo, route and price breakdown on pdp depart");
        flightSearchResultPage.clickPricePolicyTab();
        Assertion.assertThat("is airline logo displayed", flightDescriptionPage.isAirlineLogoDisplayedPDP(),is(true));
        Assertion.assertThat("is route correct",flightDescriptionPage.isAirlineRouteCorrectPDP(routeDepart),is(true));
        Assertion.assertThat("is price breakdown correct",flightSearchResultPage.isPriceSumCorrect(),is(true));

        step("Select ticket");
        flightSearchResultPage.selectTicket();

        step("Change date flight return flight");
        flightSearchResultPage.changeDate(returnDate);

        step("Open & set airline filter");
        flightSearchResultPage.openFilter();
        flightSearchResultPage.filterAirline(airline);
        flightSearchResultPage.saveFilter();

        step("open and set transit filter");
        flightSearchResultPage.openTransitFilter();
        flightSearchResultPage.filterTransitType(transit);
        flightSearchResultPage.saveFilter();

        step("get route detail return");
        String routeReturn = flightSearchResultPage.getRouteText();

        step("select first airline");
        flightSearchResultPage.selectFlight();

        step("verify logo, route and price breakdown on pdp depart");
        flightSearchResultPage.clickPricePolicyTab();
        Assertion.assertThat("is airline logo displayed", flightDescriptionPage.isAirlineLogoDisplayedPDP(),is(true));
        Assertion.assertThat("is route correct",flightDescriptionPage.isAirlineRouteCorrectPDP(routeReturn),is(true));
        Assertion.assertThat("is price breakdown correct",flightSearchResultPage.isPriceSumCorrect(),is(true));

        step("Select ticket");
        flightSearchResultPage.selectTicket();

        step("verify airline logo, route and price breakdown correct on prp");
        Assertion.assertThat("is airline logo correct",flightProductReviewPage.isAirlineLogoDisplayedPRP(),is(true));
        Assertion.assertThat("is airline route correct",flightProductReviewPage.isAirlineRouteCorrectPRP(routeDepart),is(true));
        Assertion.assertThat("is price breakdown correct",flightProductReviewPage.verifyPriceBreakdownPRP(),is(true));

        step("move to booking page");
        flightProductReviewPage.continueToBookingForm(tripType);

        step("verify date format");
        flightBookingFormPage.verifyDateFormat(tripType);

        step("Fill passenger detail");
        flightBookingFormPage.fillPassengerDetail(adultPassengers,childPassengers,infantPassengers, flightRoute,airline);

        step("move to 2nd half of booking page");
        flightBookingFormPage.continueToSecondHalf();

        step("Add AddOnMeals");
        FlightMealsDetail flightMealsDetail = flightBookingFormPage.addMealsRoundTrip(adultPassengers,childPassengers,currency);

        step("remove insurance/extra protection");
        flightBookingFormPage.removeExtraProtectionAll();

        step("open price detail");
        flightBookingFormPage.openPriceDetail();

        step("Verify price detail");
        PriceDetail priceDetail = new PriceDetail();
        priceDetail.tripType = tripType;
        priceDetail.adult = adultPassengers;
        priceDetail.child = childPassengers;
        priceDetail.infant = infantPassengers;
        priceDetail.flightMealsDetail = flightMealsDetail;
        Assertion.assertThat("Is price breakdown correct", flightBookingFormPage.isPriceBreakdownCorrect(priceDetail,currency), is(true));

        step("Continue to payment");
        flightBookingFormPage.continueToPayment();

        step("Skip full protection");
        flightBookingFormPage.skipFullProtection();

        step("Check order ID");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",!orderID.isBlank(),is(true));

        step("open and verify price detail payment page");
        paymentPage.openPriceDetail();
        Assertion.assertThat("Is price breakdown correct on payment page", paymentPage.isPriceBreakdownCorrect(), is(true));
        paymentPage.closePriceDetail();

        step("Open payment method for order id: "+orderID);
        paymentPage.openPaymentMethod();

        step("Select ATM as payment method");
        paymentPage.selectPaymentMethod("ATM");

        step("Submit payment");
        paymentPage.clickPayButton();

        step("Go to the order list");
        paymentPage.seeOrderList();

        step("check order status");
        Assertion.assertThat("Order is Waiting for payment and E-ticket not issued",  orderListPage.verifyWaitingForPayment(orderID), is(true));
    }

    @Author(names = {"Ashish"})
    @Module(name = "Ancillaries")
    @Tribe(name = "NewAirlineOnBoarding")
    @Vertical(name = "Flight")
    @Test
    public void testLoginBookingFlightRoundTripTransitAddOnMeals() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testLoginBookingFlightRoundTripTransitAddOnMeals");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightDescriptionPage<WebDriver> flightDescriptionPage = FlightDescriptionPage.getInstance(session);
        FlightProductReviewPage<WebDriver> flightProductReviewPage = FlightProductReviewPage.getInstance(session);
        FlightBookingFormPage<WebDriver> flightBookingFormPage = FlightBookingFormPage.getInstance(session);
        FlightPage<WebDriver> flightPage = FlightPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);

        String tripType = param.get("tripType");
        String transit = param.get("transit");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");
        String flightRoute = param.get("flightRoute");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int duration = Integer.parseInt(param.get("duration"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        String airline = param.get("airline");

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");

        String currency = param.get("currency");

        step("Find dynamic data");
        flightPage.setDataRoundTrip(
                daysToDepartureDate,duration,airline,airline
        );
        String departureDate = CoreFunctions.convertToLongDate (flightPage.getDate("departureDate"));
        String returnDate = CoreFunctions.convertToLongDate (flightPage.getDate("returnDate"));

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Change currency");
        homePage.changeCurrency(currency,username);

        step("Open flights page");
        homePage.openVerticalPage("Flights");
        flightSearchPage.closePopup();

        step("Toggle trip type");
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

        step("Change date flight");
        flightSearchResultPage.changeDate(departureDate);

        step("Open & set airline filter");
        flightSearchResultPage.openFilter();
        flightSearchResultPage.filterAirline(airline);
        flightSearchResultPage.saveFilter();

        step("open and set transit filter");
        flightSearchResultPage.openTransitFilter();
        flightSearchResultPage.filterTransitType(transit);
        flightSearchResultPage.saveFilter();

        step("get route detail depart");
        String routeDepart = flightSearchResultPage.getRouteText();

        step("select first airline");
        flightSearchResultPage.selectFlight();

        step("verify logo, route and price breakdown on pdp depart");
        flightSearchResultPage.clickPricePolicyTab();
        Assertion.assertThat("is airline logo displayed", flightDescriptionPage.isAirlineLogoDisplayedPDP(),is(true));
        Assertion.assertThat("is route correct",flightDescriptionPage.isAirlineRouteCorrectPDP(routeDepart),is(true));
        Assertion.assertThat("is price breakdown correct",flightSearchResultPage.isPriceSumCorrect(),is(true));

        step("Select ticket");
        flightSearchResultPage.selectTicket();

        step("Change date flight return flight");
        flightSearchResultPage.changeDate(returnDate);

        step("Open & set airline filter");
        flightSearchResultPage.openFilter();
        flightSearchResultPage.filterAirline(airline);
        flightSearchResultPage.saveFilter();

        step("open and set transit filter");
        flightSearchResultPage.openTransitFilter();
        flightSearchResultPage.filterTransitType(transit);
        flightSearchResultPage.saveFilter();

        step("get route detail return");
        String routeReturn = flightSearchResultPage.getRouteText();

        step("select first airline");
        flightSearchResultPage.selectFlight();

        step("verify logo, route and price breakdown on pdp depart");
        flightSearchResultPage.clickPricePolicyTab();
        Assertion.assertThat("is airline logo displayed", flightDescriptionPage.isAirlineLogoDisplayedPDP(),is(true));
        Assertion.assertThat("is route correct",flightDescriptionPage.isAirlineRouteCorrectPDP(routeReturn),is(true));
        Assertion.assertThat("is price breakdown correct",flightSearchResultPage.isPriceSumCorrect(),is(true));

        step("Select ticket");
        flightSearchResultPage.selectTicket();

        step("verify airline logo, route and price breakdown correct on prp");
        Assertion.assertThat("is airline logo correct",flightProductReviewPage.isAirlineLogoDisplayedPRP(),is(true));
        Assertion.assertThat("is airline route correct",flightProductReviewPage.isAirlineRouteCorrectPRP(routeDepart),is(true));
        Assertion.assertThat("is price breakdown correct",flightProductReviewPage.verifyPriceBreakdownPRP(),is(true));

        step("move to booking page");
        flightProductReviewPage.continueToBookingForm(tripType);

        step("verify date format");
        flightBookingFormPage.verifyDateFormat(tripType);

        step("Fill passenger detail");
        flightBookingFormPage.fillPassengerDetail(adultPassengers,childPassengers,infantPassengers, flightRoute,airline);

        step("move to 2nd half of booking page");
        flightBookingFormPage.continueToSecondHalf();

        step("Add AddOnMeals");
        FlightMealsDetail flightMealsDetail = flightBookingFormPage.addMealsRoundTrip(adultPassengers,childPassengers,currency);

        step("remove insurance/extra protection");
        flightBookingFormPage.removeExtraProtectionAll();

        step("open price detail");
        flightBookingFormPage.openPriceDetail();

        step("Verify price detail");
        PriceDetail priceDetail = new PriceDetail();
        priceDetail.tripType = tripType;
        priceDetail.adult = adultPassengers;
        priceDetail.child = childPassengers;
        priceDetail.infant = infantPassengers;
        priceDetail.flightMealsDetail = flightMealsDetail;
        Assertion.assertThat("Is price breakdown correct", flightBookingFormPage.isPriceBreakdownCorrect(priceDetail,currency), is(true));

        step("Continue to payment");
        flightBookingFormPage.continueToPayment();

        step("Skip full protection");
        flightBookingFormPage.skipFullProtection();

        step("Check order ID");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",!orderID.isBlank(),is(true));

        step("open and verify price detail payment page");
        paymentPage.openPriceDetail();
        Assertion.assertThat("Is price breakdown correct on payment page", paymentPage.isPriceBreakdownCorrect(), is(true));
        paymentPage.closePriceDetail();

        step("Open payment method for order id: "+orderID);
        paymentPage.openPaymentMethod();

        step("Select ATM as payment method");
        paymentPage.selectPaymentMethod("ATM");

        step("Submit payment");
        paymentPage.clickPayButton();

        step("Go to the order list");
        paymentPage.seeOrderList();

        step("check order status");
        Assertion.assertThat("Order is Waiting for payment and E-ticket not issued",  orderListPage.verifyWaitingForPayment(orderID), is(true));
    }

    @Author(names = {"Ashish"})
    @Module(name = "Ancillaries")
    @Tribe(name = "NewAirlineOnBoarding")
    @Vertical(name = "Flight")
    @Test
    public void testLoginBookingFlightRoundTripStopOverAddOnMeals() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testLoginBookingFlightRoundTripStopOverAddOnMeals");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightDescriptionPage<WebDriver> flightDescriptionPage = FlightDescriptionPage.getInstance(session);
        FlightProductReviewPage<WebDriver> flightProductReviewPage = FlightProductReviewPage.getInstance(session);
        FlightBookingFormPage<WebDriver> flightBookingFormPage = FlightBookingFormPage.getInstance(session);
        FlightPage<WebDriver> flightPage = FlightPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);

        String tripType = param.get("tripType");
        String transit = param.get("transit");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");
        String flightRoute = param.get("flightRoute");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int duration = Integer.parseInt(param.get("duration"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        String airline = param.get("airline");

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");

        String currency = param.get("currency");

        step("Find dynamic data");
        flightPage.setDataRoundTrip(
                daysToDepartureDate,duration,airline,airline
        );
        String departureDate = CoreFunctions.convertToLongDate (flightPage.getDate("departureDate"));
        String returnDate = CoreFunctions.convertToLongDate (flightPage.getDate("returnDate"));

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Change currency");
        homePage.changeCurrency(currency,username);

        step("Open flights page");
        homePage.openVerticalPage("Flights");
        flightSearchPage.closePopup();

        step("Toggle trip type");
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

        step("Change date flight");
        flightSearchResultPage.changeDate(departureDate);

        step("Open & set airline filter");
        flightSearchResultPage.openFilter();
        flightSearchResultPage.filterAirline(airline);
        flightSearchResultPage.saveFilter();

        step("open and set transit filter");
        flightSearchResultPage.openTransitFilter();
        flightSearchResultPage.filterTransitType(transit);
        flightSearchResultPage.saveFilter();

        step("get route detail depart");
        String routeDepart = flightSearchResultPage.getRouteText();

        step("select stop over airline");
        flightSearchResultPage.selectFlightStopOver();

        step("verify stop over on pdp");
        Assertion.assertThat("is stopover flight",flightDescriptionPage.verifyStopOver(),is(true));

        step("verify logo, route and price breakdown on pdp depart");
        flightSearchResultPage.clickPricePolicyTab();
        Assertion.assertThat("is airline logo displayed", flightDescriptionPage.isAirlineLogoDisplayedPDP(),is(true));
        Assertion.assertThat("is route correct",flightDescriptionPage.isAirlineRouteCorrectPDP(routeDepart),is(true));
        Assertion.assertThat("is price breakdown correct",flightSearchResultPage.isPriceSumCorrect(),is(true));

        step("Select ticket");
        flightSearchResultPage.selectTicket();

        step("Change date flight return flight");
        flightSearchResultPage.changeDate(returnDate);

        step("Open & set airline filter");
        flightSearchResultPage.openFilter();
        flightSearchResultPage.filterAirline(airline);
        flightSearchResultPage.saveFilter();

        step("open and set transit filter");
        flightSearchResultPage.openTransitFilter();
        flightSearchResultPage.filterTransitType(transit);
        flightSearchResultPage.saveFilter();

        step("get route detail return");
        String routeReturn = flightSearchResultPage.getRouteText();

        step("select stop over airline");
        flightSearchResultPage.selectFlightStopOver();

        step("verify stop over on pdp");
        Assertion.assertThat("is stopover flight",flightDescriptionPage.verifyStopOver(),is(true));

        step("verify logo, route and price breakdown on pdp depart");
        flightSearchResultPage.clickPricePolicyTab();
        Assertion.assertThat("is airline logo displayed", flightDescriptionPage.isAirlineLogoDisplayedPDP(),is(true));
        Assertion.assertThat("is route correct",flightDescriptionPage.isAirlineRouteCorrectPDP(routeReturn),is(true));
        Assertion.assertThat("is price breakdown correct",flightSearchResultPage.isPriceSumCorrect(),is(true));

        step("Select ticket");
        flightSearchResultPage.selectTicket();

        step("verify airline logo, route and price breakdown correct on prp");
        Assertion.assertThat("is airline logo correct",flightProductReviewPage.isAirlineLogoDisplayedPRP(),is(true));
        Assertion.assertThat("is airline route correct",flightProductReviewPage.isAirlineRouteCorrectPRP(routeDepart),is(true));
        Assertion.assertThat("is price breakdown correct",flightProductReviewPage.verifyPriceBreakdownPRP(),is(true));

        step("move to booking page");
        flightProductReviewPage.continueToBookingForm(tripType);

        step("verify date format");
        flightBookingFormPage.verifyDateFormat(tripType);

        step("Fill passenger detail");
        flightBookingFormPage.fillPassengerDetail(adultPassengers,childPassengers,infantPassengers, flightRoute,airline);

        step("move to 2nd half of booking page");
        flightBookingFormPage.continueToSecondHalf();

        step("Add AddOnMeals");
        FlightMealsDetail flightMealsDetail = flightBookingFormPage.addMealsRoundTrip(adultPassengers,childPassengers,currency);

        step("remove insurance/extra protection");
        flightBookingFormPage.removeExtraProtectionAll();

        step("open price detail");
        flightBookingFormPage.openPriceDetail();

        step("Verify price detail");
        PriceDetail priceDetail = new PriceDetail();
        priceDetail.tripType = tripType;
        priceDetail.adult = adultPassengers;
        priceDetail.child = childPassengers;
        priceDetail.infant = infantPassengers;
        priceDetail.flightMealsDetail = flightMealsDetail;
        Assertion.assertThat("Is price breakdown correct", flightBookingFormPage.isPriceBreakdownCorrect(priceDetail,currency), is(true));

        step("Continue to payment");
        flightBookingFormPage.continueToPayment();

        step("Skip full protection");
        flightBookingFormPage.skipFullProtection();

        step("Check order ID");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",!orderID.isBlank(),is(true));

        step("open and verify price detail payment page");
        paymentPage.openPriceDetail();
        Assertion.assertThat("Is price breakdown correct on payment page", paymentPage.isPriceBreakdownCorrect(), is(true));
        paymentPage.closePriceDetail();

        step("Open payment method for order id: "+orderID);
        paymentPage.openPaymentMethod();

        step("Select ATM as payment method");
        paymentPage.selectPaymentMethod("ATM");

        step("Submit payment");
        paymentPage.clickPayButton();

        step("Go to the order list");
        paymentPage.seeOrderList();

        step("check order status");
        Assertion.assertThat("Order is Waiting for payment and E-ticket not issued",  orderListPage.verifyWaitingForPayment(orderID), is(true));
    }

    @Author(names = {"Ashish"})
    @Module(name = "Ancillaries")
    @Tribe(name = "NewAirlineOnBoarding")
    @Vertical(name = "Flight")
    @Test
    public void testLoginBookingFlightOneWayDirectAddOnSeat() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testLoginBookingFlightOneWayDirectAddOnSeat");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightProductReviewPage<WebDriver> flightProductReviewPage = FlightProductReviewPage.getInstance(session);
        FlightBookingFormPage<WebDriver> flightBookingFormPage = FlightBookingFormPage.getInstance(session);
        FlightPage<WebDriver> flightPage = FlightPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);

        String tripType = param.get("tripType");
        String transit = param.get("transit");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");
        String flightRoute = param.get("flightRoute");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        String airline = param.get("airline");

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");

        String currency = param.get("currency");

        step("Find dynamic data");
        flightPage.setDataOneWay(
                daysToDepartureDate,""
        );
        String departureDate =CoreFunctions.convertToLongDate (flightPage.getDate("departureDate"));

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Change currency");
        homePage.changeCurrency(currency,username);

        step("Open flights page");
        homePage.openVerticalPage("Flights");
        flightSearchPage.closePopup();

        step("Toggle trip type");
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

        step("Change date flight");
        flightSearchResultPage.changeDate(departureDate);

        step("Open & set airline filter");
        flightSearchResultPage.openFilter();
        flightSearchResultPage.filterAirline(airline);
        flightSearchResultPage.saveFilter();

        step("open and set transit filter");
        flightSearchResultPage.openTransitFilter();
        flightSearchResultPage.filterTransitType(transit);
        flightSearchResultPage.saveFilter();

        step("take route detail");
        String route = flightSearchResultPage.getRouteText();

        step("select first airline");
        flightSearchResultPage.selectFlight();

        step("verify airline logo, route and price breakdown correct on prp");
        Assertion.assertThat("is airline logo correct",flightProductReviewPage.isAirlineLogoDisplayedPRP(),is(true));
        Assertion.assertThat("is airline route correct",flightProductReviewPage.isAirlineRouteCorrectPRP(route),is(true));
        Assertion.assertThat("is price breakdown correct",flightProductReviewPage.verifyPriceBreakdownPRP(),is(true));

        step("move to booking page");
        flightProductReviewPage.continueToBookingForm(tripType);

        step("verify date format");
        flightBookingFormPage.verifyDateFormat(tripType);

        step("Fill passenger detail");
        flightBookingFormPage.fillPassengerDetail(adultPassengers,childPassengers,infantPassengers, flightRoute,airline);

        step("move to 2nd half of booking page");
        flightBookingFormPage.continueToSecondHalf();

        step("Add AddOnSeat");
        PriceDetail priceDetail = new PriceDetail();
        priceDetail.tripType=tripType;
        priceDetail.adult = adultPassengers;
        priceDetail.child = childPassengers;
        priceDetail.infant = infantPassengers;
        String seatPrice=flightBookingFormPage.addSeat(priceDetail,currency);

        step("remove insurance/extra protection");
        flightBookingFormPage.removeExtraProtectionAll();

        step("open price detail");
        flightBookingFormPage.openPriceDetail();

        step("Verify price detail");
        priceDetail.seatDepartPrice=seatPrice;
        Assertion.assertThat("Is price breakdown correct", flightBookingFormPage.isPriceBreakdownCorrect(priceDetail,currency), is(true));

        step("Continue to payment");
        flightBookingFormPage.continueToPayment();

        step("Skip full protection");
        flightBookingFormPage.skipFullProtection();

        step("Check order ID");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",!orderID.isBlank(),is(true));

        step("open and verify price detail payment page");
        paymentPage.openPriceDetail();
        Assertion.assertThat("Is price breakdown correct on payment page", paymentPage.isPriceBreakdownCorrect(), is(true));
        paymentPage.closePriceDetail();

        step("Open payment method for order id: "+orderID);
        paymentPage.openPaymentMethod();

        step("Select ATM as payment method");
        paymentPage.selectPaymentMethod("ATM");

        step("Submit payment");
        paymentPage.clickPayButton();

        step("Go to the order list");
        paymentPage.seeOrderList();

        step("check order status");
        Assertion.assertThat("Order is Waiting for payment and E-ticket not issued",  orderListPage.verifyWaitingForPayment(orderID), is(true));
    }

    @Author(names = {"Ashish"})
    @Module(name = "Ancillaries")
    @Tribe(name = "NewAirlineOnBoarding")
    @Vertical(name = "Flight")
    @Test
    public void testLoginBookingFlightRoundTripDirectAddOnSeat() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testLoginBookingFlightRoundTripDirectAddOnSeat");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightDescriptionPage<WebDriver> flightDescriptionPage = FlightDescriptionPage.getInstance(session);
        FlightProductReviewPage<WebDriver> flightProductReviewPage = FlightProductReviewPage.getInstance(session);
        FlightBookingFormPage<WebDriver> flightBookingFormPage = FlightBookingFormPage.getInstance(session);
        FlightPage<WebDriver> flightPage = FlightPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);

        String tripType = param.get("tripType");
        String transit = param.get("transit");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");
        String flightRoute = param.get("flightRoute");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int duration = Integer.parseInt(param.get("duration"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        String airline = param.get("airline");

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");

        String currency = param.get("currency");

        step("Find dynamic data");
        flightPage.setDataRoundTrip(
                daysToDepartureDate,duration,airline,airline
        );
        String departureDate = CoreFunctions.convertToLongDate (flightPage.getDate("departureDate"));
        String returnDate = CoreFunctions.convertToLongDate (flightPage.getDate("returnDate"));

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Change currency");
        homePage.changeCurrency(currency,username);

        step("Open flights page");
        homePage.openVerticalPage("Flights");
        flightSearchPage.closePopup();

        step("Toggle trip type");
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

        step("Change date flight");
        flightSearchResultPage.changeDate(departureDate);

        step("Open & set airline filter");
        flightSearchResultPage.openFilter();
        flightSearchResultPage.filterAirline(airline);
        flightSearchResultPage.saveFilter();

        step("open and set transit filter");
        flightSearchResultPage.openTransitFilter();
        flightSearchResultPage.filterTransitType(transit);
        flightSearchResultPage.saveFilter();

        step("get route detail depart");
        String routeDepart = flightSearchResultPage.getRouteText();

        step("select first airline");
        flightSearchResultPage.selectFlight();

        step("verify logo, route and price breakdown on pdp depart");
        flightSearchResultPage.clickPricePolicyTab();
        Assertion.assertThat("is airline logo displayed", flightDescriptionPage.isAirlineLogoDisplayedPDP(),is(true));
        Assertion.assertThat("is route correct",flightDescriptionPage.isAirlineRouteCorrectPDP(routeDepart),is(true));
        Assertion.assertThat("is price breakdown correct",flightSearchResultPage.isPriceSumCorrect(),is(true));

        step("Select ticket");
        flightSearchResultPage.selectTicket();

        step("Change date flight return flight");
        flightSearchResultPage.changeDate(returnDate);

        step("Open & set airline filter");
        flightSearchResultPage.openFilter();
        flightSearchResultPage.filterAirline(airline);
        flightSearchResultPage.saveFilter();

        step("open and set transit filter");
        flightSearchResultPage.openTransitFilter();
        flightSearchResultPage.filterTransitType(transit);
        flightSearchResultPage.saveFilter();

        step("get route detail return");
        String routeReturn = flightSearchResultPage.getRouteText();

        step("select first airline");
        flightSearchResultPage.selectFlight();

        step("verify logo, route and price breakdown on pdp depart");
        flightSearchResultPage.clickPricePolicyTab();
        Assertion.assertThat("is airline logo displayed", flightDescriptionPage.isAirlineLogoDisplayedPDP(),is(true));
        Assertion.assertThat("is route correct",flightDescriptionPage.isAirlineRouteCorrectPDP(routeReturn),is(true));
        Assertion.assertThat("is price breakdown correct",flightSearchResultPage.isPriceSumCorrect(),is(true));

        step("Select ticket");
        flightSearchResultPage.selectTicket();

        step("verify airline logo, route and price breakdown correct on prp");
        Assertion.assertThat("is airline logo correct",flightProductReviewPage.isAirlineLogoDisplayedPRP(),is(true));
        Assertion.assertThat("is airline route correct",flightProductReviewPage.isAirlineRouteCorrectPRP(routeDepart),is(true));
        Assertion.assertThat("is price breakdown correct",flightProductReviewPage.verifyPriceBreakdownPRP(),is(true));

        step("move to booking page");
        flightProductReviewPage.continueToBookingForm(tripType);

        step("verify date format");
        flightBookingFormPage.verifyDateFormat(tripType);

        step("Fill passenger detail");
        flightBookingFormPage.fillPassengerDetail(adultPassengers,childPassengers,infantPassengers, flightRoute,airline);

        step("move to 2nd half of booking page");
        flightBookingFormPage.continueToSecondHalf();

        step("Add AddOnSeat");
        PriceDetail priceDetail = new PriceDetail();
        priceDetail.tripType=tripType;
        priceDetail.adult = adultPassengers;
        priceDetail.child = childPassengers;
        priceDetail.infant = infantPassengers;
        priceDetail.flightSeatDetails = flightBookingFormPage.addSeatRoundTrip(priceDetail,currency);

        step("remove insurance/extra protection");
        flightBookingFormPage.removeExtraProtectionAll();

        step("open price detail");
        flightBookingFormPage.openPriceDetail();

        step("Verify price detail");
        Assertion.assertThat("Is price breakdown correct", flightBookingFormPage.isPriceBreakdownCorrect(priceDetail,currency), is(true));

        step("Continue to payment");
        flightBookingFormPage.continueToPayment();

        step("Skip full protection");
        flightBookingFormPage.skipFullProtection();

        step("Check order ID");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",!orderID.isBlank(),is(true));

        step("open and verify price detail payment page");
        paymentPage.openPriceDetail();
        Assertion.assertThat("Is price breakdown correct on payment page", paymentPage.isPriceBreakdownCorrect(), is(true));
        paymentPage.closePriceDetail();

        step("Open payment method for order id: "+orderID);
        paymentPage.openPaymentMethod();

        step("Select ATM as payment method");
        paymentPage.selectPaymentMethod("ATM");

        step("Submit payment");
        paymentPage.clickPayButton();

        step("Go to the order list");
        paymentPage.seeOrderList();

        step("check order status");
        Assertion.assertThat("Order is Waiting for payment and E-ticket not issued",  orderListPage.verifyWaitingForPayment(orderID), is(true));
    }

    @Author(names = {"Ashish"})
    @Module(name = "Ancillaries")
    @Tribe(name = "NewAirlineOnBoarding")
    @Vertical(name = "Flight")
    @Test
    public void testLoginBookingFlightRoundTripTransitAddOnSeat() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testLoginBookingFlightRoundTripTransitAddOnSeat");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightDescriptionPage<WebDriver> flightDescriptionPage = FlightDescriptionPage.getInstance(session);
        FlightProductReviewPage<WebDriver> flightProductReviewPage = FlightProductReviewPage.getInstance(session);
        FlightBookingFormPage<WebDriver> flightBookingFormPage = FlightBookingFormPage.getInstance(session);
        FlightPage<WebDriver> flightPage = FlightPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);

        String tripType = param.get("tripType");
        String transit = param.get("transit");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");
        String flightRoute = param.get("flightRoute");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int duration = Integer.parseInt(param.get("duration"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        String airline = param.get("airline");

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");

        String currency = param.get("currency");

        step("Find dynamic data");
        flightPage.setDataRoundTrip(
                daysToDepartureDate,duration,airline,airline
        );
        String departureDate = CoreFunctions.convertToLongDate (flightPage.getDate("departureDate"));
        String returnDate = CoreFunctions.convertToLongDate (flightPage.getDate("returnDate"));

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Change currency");
        homePage.changeCurrency(currency,username);

        step("Open flights page");
        homePage.openVerticalPage("Flights");
        flightSearchPage.closePopup();

        step("Toggle trip type");
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

        step("Change date flight");
        flightSearchResultPage.changeDate(departureDate);

        step("Open & set airline filter");
        flightSearchResultPage.openFilter();
        flightSearchResultPage.filterAirline(airline);
        flightSearchResultPage.saveFilter();

        step("open and set transit filter");
        flightSearchResultPage.openTransitFilter();
        flightSearchResultPage.filterTransitType(transit);
        flightSearchResultPage.saveFilter();

        step("get route detail depart");
        String routeDepart = flightSearchResultPage.getRouteText();

        step("select first airline");
        flightSearchResultPage.selectFlight();

        step("verify logo, route and price breakdown on pdp depart");
        flightSearchResultPage.clickPricePolicyTab();
        Assertion.assertThat("is airline logo displayed", flightDescriptionPage.isAirlineLogoDisplayedPDP(),is(true));
        Assertion.assertThat("is route correct",flightDescriptionPage.isAirlineRouteCorrectPDP(routeDepart),is(true));
        Assertion.assertThat("is price breakdown correct",flightSearchResultPage.isPriceSumCorrect(),is(true));

        step("Select ticket");
        flightSearchResultPage.selectTicket();

        step("Change date flight return flight");
        flightSearchResultPage.changeDate(returnDate);

        step("Open & set airline filter");
        flightSearchResultPage.openFilter();
        flightSearchResultPage.filterAirline(airline);
        flightSearchResultPage.saveFilter();

        step("open and set transit filter");
        flightSearchResultPage.openTransitFilter();
        flightSearchResultPage.filterTransitType(transit);
        flightSearchResultPage.saveFilter();

        step("get route detail return");
        String routeReturn = flightSearchResultPage.getRouteText();

        step("select first airline");
        flightSearchResultPage.selectFlight();

        step("verify logo, route and price breakdown on pdp depart");
        flightSearchResultPage.clickPricePolicyTab();
        Assertion.assertThat("is airline logo displayed", flightDescriptionPage.isAirlineLogoDisplayedPDP(),is(true));
        Assertion.assertThat("is route correct",flightDescriptionPage.isAirlineRouteCorrectPDP(routeReturn),is(true));
        Assertion.assertThat("is price breakdown correct",flightSearchResultPage.isPriceSumCorrect(),is(true));

        step("Select ticket");
        flightSearchResultPage.selectTicket();

        step("verify airline logo, route and price breakdown correct on prp");
        Assertion.assertThat("is airline logo correct",flightProductReviewPage.isAirlineLogoDisplayedPRP(),is(true));
        Assertion.assertThat("is airline route correct",flightProductReviewPage.isAirlineRouteCorrectPRP(routeDepart),is(true));
        Assertion.assertThat("is price breakdown correct",flightProductReviewPage.verifyPriceBreakdownPRP(),is(true));

        step("move to booking page");
        flightProductReviewPage.continueToBookingForm(tripType);

        step("verify date format");
        flightBookingFormPage.verifyDateFormat(tripType);

        step("Fill passenger detail");
        flightBookingFormPage.fillPassengerDetail(adultPassengers,childPassengers,infantPassengers, flightRoute,airline);

        step("move to 2nd half of booking page");
        flightBookingFormPage.continueToSecondHalf();

        step("Add AddOnSeat");
        PriceDetail priceDetail = new PriceDetail();
        priceDetail.tripType=tripType;
        priceDetail.adult = adultPassengers;
        priceDetail.child = childPassengers;
        priceDetail.infant = infantPassengers;
        priceDetail.flightSeatDetails = flightBookingFormPage.addSeatRoundTrip(priceDetail,currency);

        step("remove insurance/extra protection");
        flightBookingFormPage.removeExtraProtectionAll();

        step("open price detail");
        flightBookingFormPage.openPriceDetail();

        step("Verify price detail");
        Assertion.assertThat("Is price breakdown correct", flightBookingFormPage.isPriceBreakdownCorrect(priceDetail,currency), is(true));

        step("Continue to payment");
        flightBookingFormPage.continueToPayment();

        step("Skip full protection");
        flightBookingFormPage.skipFullProtection();

        step("Check order ID");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",!orderID.isBlank(),is(true));

        step("open and verify price detail payment page");
        paymentPage.openPriceDetail();
        Assertion.assertThat("Is price breakdown correct on payment page", paymentPage.isPriceBreakdownCorrect(), is(true));
        paymentPage.closePriceDetail();

        step("Open payment method for order id: "+orderID);
        paymentPage.openPaymentMethod();

        step("Select ATM as payment method");
        paymentPage.selectPaymentMethod("ATM");

        step("Submit payment");
        paymentPage.clickPayButton();

        step("Go to the order list");
        paymentPage.seeOrderList();

        step("check order status");
        Assertion.assertThat("Order is Waiting for payment and E-ticket not issued",  orderListPage.verifyWaitingForPayment(orderID), is(true));
    }

    @Author(names = {"Ashish"})
    @Module(name = "Ancillaries")
    @Tribe(name = "NewAirlineOnBoarding")
    @Vertical(name = "Flight")
    @Test
    public void testLoginBookingFlightRoundTripStopOverAddOnSeat() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testLoginBookingFlightRoundTripStopOverAddOnSeat");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightDescriptionPage<WebDriver> flightDescriptionPage = FlightDescriptionPage.getInstance(session);
        FlightProductReviewPage<WebDriver> flightProductReviewPage = FlightProductReviewPage.getInstance(session);
        FlightBookingFormPage<WebDriver> flightBookingFormPage = FlightBookingFormPage.getInstance(session);
        FlightPage<WebDriver> flightPage = FlightPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);

        String tripType = param.get("tripType");
        String transit = param.get("transit");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");
        String flightRoute = param.get("flightRoute");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int duration = Integer.parseInt(param.get("duration"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        String airline = param.get("airline");

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");

        String currency = param.get("currency");

        step("Find dynamic data");
        flightPage.setDataRoundTrip(
                daysToDepartureDate,duration,airline,airline
        );
        String departureDate = CoreFunctions.convertToLongDate (flightPage.getDate("departureDate"));
        String returnDate = CoreFunctions.convertToLongDate (flightPage.getDate("returnDate"));

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Change currency");
        homePage.changeCurrency(currency,username);

        step("Open flights page");
        homePage.openVerticalPage("Flights");
        flightSearchPage.closePopup();

        step("Toggle trip type");
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

        step("Change date flight");
        flightSearchResultPage.changeDate(departureDate);

        step("Open & set airline filter");
        flightSearchResultPage.openFilter();
        flightSearchResultPage.filterAirline(airline);
        flightSearchResultPage.saveFilter();

        step("open and set transit filter");
        flightSearchResultPage.openTransitFilter();
        flightSearchResultPage.filterTransitType(transit);
        flightSearchResultPage.saveFilter();

        step("get route detail depart");
        String routeDepart = flightSearchResultPage.getRouteText();

        step("select stop over airline");
        flightSearchResultPage.selectFlightStopOver();

        step("verify stop over on pdp");
        Assertion.assertThat("is stopover flight",flightDescriptionPage.verifyStopOver(),is(true));

        step("verify logo, route and price breakdown on pdp depart");
        flightSearchResultPage.clickPricePolicyTab();
        Assertion.assertThat("is airline logo displayed", flightDescriptionPage.isAirlineLogoDisplayedPDP(),is(true));
        Assertion.assertThat("is route correct",flightDescriptionPage.isAirlineRouteCorrectPDP(routeDepart),is(true));
        Assertion.assertThat("is price breakdown correct",flightSearchResultPage.isPriceSumCorrect(),is(true));

        step("Select ticket");
        flightSearchResultPage.selectTicket();

        step("Change date flight return flight");
        flightSearchResultPage.changeDate(returnDate);

        step("Open & set airline filter");
        flightSearchResultPage.openFilter();
        flightSearchResultPage.filterAirline(airline);
        flightSearchResultPage.saveFilter();

        step("open and set transit filter");
        flightSearchResultPage.openTransitFilter();
        flightSearchResultPage.filterTransitType(transit);
        flightSearchResultPage.saveFilter();

        step("get route detail return");
        String routeReturn = flightSearchResultPage.getRouteText();

        step("select stop over airline");
        flightSearchResultPage.selectFlightStopOver();

        step("verify stop over on pdp");
        Assertion.assertThat("is stopover flight",flightDescriptionPage.verifyStopOver(),is(true));

        step("verify logo, route and price breakdown on pdp depart");
        flightSearchResultPage.clickPricePolicyTab();
        Assertion.assertThat("is airline logo displayed", flightDescriptionPage.isAirlineLogoDisplayedPDP(),is(true));
        Assertion.assertThat("is route correct",flightDescriptionPage.isAirlineRouteCorrectPDP(routeReturn),is(true));
        Assertion.assertThat("is price breakdown correct",flightSearchResultPage.isPriceSumCorrect(),is(true));

        step("Select ticket");
        flightSearchResultPage.selectTicket();

        step("verify airline logo, route and price breakdown correct on prp");
        Assertion.assertThat("is airline logo correct",flightProductReviewPage.isAirlineLogoDisplayedPRP(),is(true));
        Assertion.assertThat("is airline route correct",flightProductReviewPage.isAirlineRouteCorrectPRP(routeDepart),is(true));
        Assertion.assertThat("is price breakdown correct",flightProductReviewPage.verifyPriceBreakdownPRP(),is(true));

        step("move to booking page");
        flightProductReviewPage.continueToBookingForm(tripType);

        step("verify date format");
        flightBookingFormPage.verifyDateFormat(tripType);

        step("Fill passenger detail");
        flightBookingFormPage.fillPassengerDetail(adultPassengers,childPassengers,infantPassengers, flightRoute,airline);

        step("move to 2nd half of booking page");
        flightBookingFormPage.continueToSecondHalf();

        step("Add AddOnSeat");
        PriceDetail priceDetail = new PriceDetail();
        priceDetail.tripType=tripType;
        priceDetail.adult = adultPassengers;
        priceDetail.child = childPassengers;
        priceDetail.infant = infantPassengers;
        priceDetail.flightSeatDetails = flightBookingFormPage.addSeatRoundTrip(priceDetail,currency);

        step("remove insurance/extra protection");
        flightBookingFormPage.removeExtraProtectionAll();

        step("open price detail");
        flightBookingFormPage.openPriceDetail();

        step("Verify price detail");
        Assertion.assertThat("Is price breakdown correct", flightBookingFormPage.isPriceBreakdownCorrect(priceDetail,currency), is(true));

        step("Continue to payment");
        flightBookingFormPage.continueToPayment();

        step("Skip full protection");
        flightBookingFormPage.skipFullProtection();

        step("Check order ID");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",!orderID.isBlank(),is(true));

        step("open and verify price detail payment page");
        paymentPage.openPriceDetail();
        Assertion.assertThat("Is price breakdown correct on payment page", paymentPage.isPriceBreakdownCorrect(), is(true));
        paymentPage.closePriceDetail();

        step("Open payment method for order id: "+orderID);
        paymentPage.openPaymentMethod();

        step("Select ATM as payment method");
        paymentPage.selectPaymentMethod("ATM");

        step("Submit payment");
        paymentPage.clickPayButton();

        step("Go to the order list");
        paymentPage.seeOrderList();

        step("check order status");
        Assertion.assertThat("Order is Waiting for payment and E-ticket not issued",  orderListPage.verifyWaitingForPayment(orderID), is(true));
    }

    @Author(names = {"Ashish"})
    @Module(name = "PaidOrderPrimeBooking")
    @Tribe(name = "NewAirlineOnBoarding")
    @Vertical(name = "Flight")
    @Test
    public void testLoginBookingFlightOneWayDirectIssueETicket() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testLoginBookingFlightOneWayDirectIssueETicket");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightProductReviewPage<WebDriver> flightProductReviewPage = FlightProductReviewPage.getInstance(session);
        FlightBookingFormPage<WebDriver> flightBookingFormPage = FlightBookingFormPage.getInstance(session);
        FlightPage<WebDriver> flightPage = FlightPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);

        String tripType = param.get("tripType");
        String transit = param.get("transit");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");
        String flightRoute = param.get("flightRoute");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        String airline = param.get("airline");

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");

        String currency = param.get("currency");

        step("Find dynamic data");
        flightPage.setDataOneWay(
                daysToDepartureDate,""
        );
        String departureDate =CoreFunctions.convertToLongDate (flightPage.getDate("departureDate"));

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Change currency");
        homePage.changeCurrency(currency,username);

        step("Open flights page");
        homePage.openVerticalPage("Flights");
        flightSearchPage.closePopup();

        step("Toggle trip type");
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

        step("Change date flight");
        flightSearchResultPage.changeDate(departureDate);

        step("Open & set airline filter");
        flightSearchResultPage.openFilter();
        flightSearchResultPage.filterAirline(airline);
        flightSearchResultPage.saveFilter();

        step("open and set transit filter");
        flightSearchResultPage.openTransitFilter();
        flightSearchResultPage.filterTransitType(transit);
        flightSearchResultPage.saveFilter();

        step("take route detail");
        String route = flightSearchResultPage.getRouteText();

        step("select first airline");
        flightSearchResultPage.selectFlight();

        step("verify airline logo, route and price breakdown correct on prp");
        Assertion.assertThat("is airline logo correct",flightProductReviewPage.isAirlineLogoDisplayedPRP(),is(true));
        Assertion.assertThat("is airline route correct",flightProductReviewPage.isAirlineRouteCorrectPRP(route),is(true));
        Assertion.assertThat("is price breakdown correct",flightProductReviewPage.verifyPriceBreakdownPRP(),is(true));

        step("move to booking page");
        flightProductReviewPage.continueToBookingForm(tripType);

        step("verify date format");
        flightBookingFormPage.verifyDateFormat(tripType);

        step("Fill passenger detail");
        flightBookingFormPage.fillPassengerDetail(adultPassengers,childPassengers,infantPassengers, flightRoute,airline);

        step("move to 2nd half of booking page");
        flightBookingFormPage.continueToSecondHalf();

        step("remove insurance/extra protection");
        flightBookingFormPage.removeExtraProtectionAll();

        step("open price detail");
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

        step("Check order ID");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",!orderID.isBlank(),is(true));

        step("open and verify price detail payment page");
        paymentPage.openPriceDetail();
        Assertion.assertThat("Is price breakdown correct on payment page", paymentPage.isPriceBreakdownCorrect(), is(true));
        paymentPage.closePriceDetail();

        step("Open payment method for order id: "+orderID);
        paymentPage.openPaymentMethod();

        step("Select ATM as payment method");
        paymentPage.selectPaymentMethod("ATM");

        step("Submit payment");
        paymentPage.clickPayButton();

        step("Go to the order list");
        sleep(50);
        paymentPage.seeOrderList();

        step("open order and check order status");
        boolean orderEticketStatus = orderListPage.getOrderStatus(orderID,username,password);
        Assertion.assertThat("Order Status is eticket issue",orderEticketStatus,is(true));

        step("Find the issued order");
        orderListPage.openActiveTicketViaDeeplink(orderID);
        
        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.getETicketLabel(), either(is("Your e-ticket is here")).or(is("E-tiketmu di sini")));
    }

    @Author(names = {"Ashish"})
    @Module(name = "PaidOrderPrimeBooking")
    @Tribe(name = "NewAirlineOnBoarding")
    @Vertical(name = "Flight")
    @Test
    public void testLoginBookingFlightRoundTripDirectIssueETicket() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testLoginBookingFlightRoundTripDirectIssueETicket");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightDescriptionPage<WebDriver> flightDescriptionPage = FlightDescriptionPage.getInstance(session);
        FlightProductReviewPage<WebDriver> flightProductReviewPage = FlightProductReviewPage.getInstance(session);
        FlightBookingFormPage<WebDriver> flightBookingFormPage = FlightBookingFormPage.getInstance(session);
        FlightPage<WebDriver> flightPage = FlightPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);

        String tripType = param.get("tripType");
        String transit = param.get("transit");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");
        String flightRoute = param.get("flightRoute");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int duration = Integer.parseInt(param.get("duration"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        String airline = param.get("airline");

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");

        String currency = param.get("currency");

        step("Find dynamic data");
        flightPage.setDataRoundTrip(
                daysToDepartureDate,duration,airline,airline
        );
        String departureDate = CoreFunctions.convertToLongDate (flightPage.getDate("departureDate"));
        String returnDate = CoreFunctions.convertToLongDate (flightPage.getDate("returnDate"));

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Change currency");
        homePage.changeCurrency(currency,username);

        step("Open flights page");
        homePage.openVerticalPage("Flights");
        flightSearchPage.closePopup();

        step("Toggle trip type");
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

        step("Change date flight");
        flightSearchResultPage.changeDate(departureDate);

        step("Open & set airline filter");
        flightSearchResultPage.openFilter();
        flightSearchResultPage.filterAirline(airline);
        flightSearchResultPage.saveFilter();

        step("open and set transit filter");
        flightSearchResultPage.openTransitFilter();
        flightSearchResultPage.filterTransitType(transit);
        flightSearchResultPage.saveFilter();

        step("get route detail depart");
        String routeDepart = flightSearchResultPage.getRouteText();

        step("select first airline");
        flightSearchResultPage.selectFlight();

        step("verify logo, route and price breakdown on pdp depart");
        flightSearchResultPage.clickPricePolicyTab();
        Assertion.assertThat("is airline logo displayed", flightDescriptionPage.isAirlineLogoDisplayedPDP(),is(true));
        Assertion.assertThat("is route correct",flightDescriptionPage.isAirlineRouteCorrectPDP(routeDepart),is(true));
        Assertion.assertThat("is price breakdown correct",flightSearchResultPage.isPriceSumCorrect(),is(true));

        step("Select ticket");
        flightSearchResultPage.selectTicket();

        step("Change date flight return flight");
        flightSearchResultPage.changeDate(returnDate);

        step("Open & set airline filter");
        flightSearchResultPage.openFilter();
        flightSearchResultPage.filterAirline(airline);
        flightSearchResultPage.saveFilter();

        step("open and set transit filter");
        flightSearchResultPage.openTransitFilter();
        flightSearchResultPage.filterTransitType(transit);
        flightSearchResultPage.saveFilter();

        step("get route detail return");
        String routeReturn = flightSearchResultPage.getRouteText();

        step("select first airline");
        flightSearchResultPage.selectFlight();

        step("verify logo, route and price breakdown on pdp depart");
        flightSearchResultPage.clickPricePolicyTab();
        Assertion.assertThat("is airline logo displayed", flightDescriptionPage.isAirlineLogoDisplayedPDP(),is(true));
        Assertion.assertThat("is route correct",flightDescriptionPage.isAirlineRouteCorrectPDP(routeReturn),is(true));
        Assertion.assertThat("is price breakdown correct",flightSearchResultPage.isPriceSumCorrect(),is(true));

        step("Select ticket");
        flightSearchResultPage.selectTicket();

        step("verify airline logo, route and price breakdown correct on prp");
        Assertion.assertThat("is airline logo correct",flightProductReviewPage.isAirlineLogoDisplayedPRP(),is(true));
        Assertion.assertThat("is airline route correct",flightProductReviewPage.isAirlineRouteCorrectPRP(routeDepart),is(true));
        Assertion.assertThat("is price breakdown correct",flightProductReviewPage.verifyPriceBreakdownPRP(),is(true));

        step("move to booking page");
        flightProductReviewPage.continueToBookingForm(tripType);

        step("verify date format");
        flightBookingFormPage.verifyDateFormat(tripType);

        step("Fill passenger detail");
        flightBookingFormPage.fillPassengerDetail(adultPassengers,childPassengers,infantPassengers, flightRoute,airline);

        step("move to 2nd half of booking page");
        flightBookingFormPage.continueToSecondHalf();

        step("remove insurance/extra protection");
        flightBookingFormPage.removeExtraProtectionAll();

        step("open price detail");
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

        step("Check order ID");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",!orderID.isBlank(),is(true));

        step("open and verify price detail payment page");
        paymentPage.openPriceDetail();
        Assertion.assertThat("Is price breakdown correct on payment page", paymentPage.isPriceBreakdownCorrect(), is(true));
        paymentPage.closePriceDetail();

        step("Open payment method for order id: "+orderID);
        paymentPage.openPaymentMethod();

        step("Select ATM as payment method");
        paymentPage.selectPaymentMethod("ATM");

        step("Submit payment");
        paymentPage.clickPayButton();

        step("Go to the order list");
        sleep(50);
        paymentPage.seeOrderList();

        step("open order and check order status");
        boolean orderEticketStatus = orderListPage.getOrderStatus(orderID,username,password);
        Assertion.assertThat("Order Status is eticket issue",orderEticketStatus,is(true));

        step("Find the issued order");
        orderListPage.openActiveTicketViaDeeplink(orderID);
        
        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.getETicketLabel(), either(is("Your e-ticket is here")).or(is("E-tiketmu di sini")));
    }

    @Author(names = {"Ashish"})
    @Module(name = "PaidOrderPrimeBooking")
    @Tribe(name = "NewAirlineOnBoarding")
    @Vertical(name = "Flight")
    @Test
    public void testLoginBookingFlightRoundTripTransitIssueETicket() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testLoginBookingFlightRoundTripTransitIssueETicket");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightDescriptionPage<WebDriver> flightDescriptionPage = FlightDescriptionPage.getInstance(session);
        FlightProductReviewPage<WebDriver> flightProductReviewPage = FlightProductReviewPage.getInstance(session);
        FlightBookingFormPage<WebDriver> flightBookingFormPage = FlightBookingFormPage.getInstance(session);
        FlightPage<WebDriver> flightPage = FlightPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);

        String tripType = param.get("tripType");
        String transit = param.get("transit");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");
        String flightRoute = param.get("flightRoute");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int duration = Integer.parseInt(param.get("duration"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        String airline = param.get("airline");

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");

        String currency = param.get("currency");

        step("Find dynamic data");
        flightPage.setDataRoundTrip(
                daysToDepartureDate,duration,airline,airline
        );
        String departureDate = CoreFunctions.convertToLongDate (flightPage.getDate("departureDate"));
        String returnDate = CoreFunctions.convertToLongDate (flightPage.getDate("returnDate"));

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Change currency");
        homePage.changeCurrency(currency,username);

        step("Open flights page");
        homePage.openVerticalPage("Flights");
        flightSearchPage.closePopup();

        step("Toggle trip type");
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

        step("Change date flight");
        flightSearchResultPage.changeDate(departureDate);

        step("Open & set airline filter");
        flightSearchResultPage.openFilter();
        flightSearchResultPage.filterAirline(airline);
        flightSearchResultPage.saveFilter();

        step("open and set transit filter");
        flightSearchResultPage.openTransitFilter();
        flightSearchResultPage.filterTransitType(transit);
        flightSearchResultPage.saveFilter();

        step("get route detail depart");
        String routeDepart = flightSearchResultPage.getRouteText();

        step("select first airline");
        flightSearchResultPage.selectFlight();

        step("verify logo, route and price breakdown on pdp depart");
        flightSearchResultPage.clickPricePolicyTab();
        Assertion.assertThat("is airline logo displayed", flightDescriptionPage.isAirlineLogoDisplayedPDP(),is(true));
        Assertion.assertThat("is route correct",flightDescriptionPage.isAirlineRouteCorrectPDP(routeDepart),is(true));
        Assertion.assertThat("is price breakdown correct",flightSearchResultPage.isPriceSumCorrect(),is(true));

        step("Select ticket");
        flightSearchResultPage.selectTicket();

        step("Change date flight return flight");
        flightSearchResultPage.changeDate(returnDate);

        step("Open & set airline filter");
        flightSearchResultPage.openFilter();
        flightSearchResultPage.filterAirline(airline);
        flightSearchResultPage.saveFilter();

        step("open and set transit filter");
        flightSearchResultPage.openTransitFilter();
        flightSearchResultPage.filterTransitType(transit);
        flightSearchResultPage.saveFilter();

        step("get route detail return");
        String routeReturn = flightSearchResultPage.getRouteText();

        step("select first airline");
        flightSearchResultPage.selectFlight();

        step("verify logo, route and price breakdown on pdp depart");
        flightSearchResultPage.clickPricePolicyTab();
        Assertion.assertThat("is airline logo displayed", flightDescriptionPage.isAirlineLogoDisplayedPDP(),is(true));
        Assertion.assertThat("is route correct",flightDescriptionPage.isAirlineRouteCorrectPDP(routeReturn),is(true));
        Assertion.assertThat("is price breakdown correct",flightSearchResultPage.isPriceSumCorrect(),is(true));

        step("Select ticket");
        flightSearchResultPage.selectTicket();

        step("verify airline logo, route and price breakdown correct on prp");
        Assertion.assertThat("is airline logo correct",flightProductReviewPage.isAirlineLogoDisplayedPRP(),is(true));
        Assertion.assertThat("is airline route correct",flightProductReviewPage.isAirlineRouteCorrectPRP(routeDepart),is(true));
        Assertion.assertThat("is price breakdown correct",flightProductReviewPage.verifyPriceBreakdownPRP(),is(true));

        step("move to booking page");
        flightProductReviewPage.continueToBookingForm(tripType);

        step("verify date format");
        flightBookingFormPage.verifyDateFormat(tripType);

        step("Fill passenger detail");
        flightBookingFormPage.fillPassengerDetail(adultPassengers,childPassengers,infantPassengers, flightRoute,airline);

        step("move to 2nd half of booking page");
        flightBookingFormPage.continueToSecondHalf();

        step("remove insurance/extra protection");
        flightBookingFormPage.removeExtraProtectionAll();

        step("open price detail");
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

        step("Check order ID");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",!orderID.isBlank(),is(true));

        step("open and verify price detail payment page");
        paymentPage.openPriceDetail();
        Assertion.assertThat("Is price breakdown correct on payment page", paymentPage.isPriceBreakdownCorrect(), is(true));
        paymentPage.closePriceDetail();

        step("Open payment method for order id: "+orderID);
        paymentPage.openPaymentMethod();

        step("Select ATM as payment method");
        paymentPage.selectPaymentMethod("ATM");

        step("Submit payment");
        paymentPage.clickPayButton();

        step("Go to the order list");
        sleep(50);
        paymentPage.seeOrderList();

        step("open order and check order status");
        boolean orderEticketStatus = orderListPage.getOrderStatus(orderID,username,password);
        Assertion.assertThat("Order Status is eticket issue",orderEticketStatus,is(true));

        step("Find the issued order");
        orderListPage.openActiveTicketViaDeeplink(orderID);
        
        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.getETicketLabel(), either(is("Your e-ticket is here")).or(is("E-tiketmu di sini")));
    }

    @Author(names = {"Ashish"})
    @Module(name = "PaidOrderPrimeBooking")
    @Tribe(name = "NewAirlineOnBoarding")
    @Vertical(name = "Flight")
    @Test
    public void testLoginBookingFlightRoundTripStopOverIssueETicket() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testLoginBookingFlightRoundTripStopOverIssueETicket");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightDescriptionPage<WebDriver> flightDescriptionPage = FlightDescriptionPage.getInstance(session);
        FlightProductReviewPage<WebDriver> flightProductReviewPage = FlightProductReviewPage.getInstance(session);
        FlightBookingFormPage<WebDriver> flightBookingFormPage = FlightBookingFormPage.getInstance(session);
        FlightPage<WebDriver> flightPage = FlightPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);

        String tripType = param.get("tripType");
        String transit = param.get("transit");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");
        String flightRoute = param.get("flightRoute");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int duration = Integer.parseInt(param.get("duration"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        String airline = param.get("airline");

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");

        String currency = param.get("currency");

        step("Find dynamic data");
        flightPage.setDataRoundTrip(
                daysToDepartureDate,duration,airline,airline
        );
        String departureDate = CoreFunctions.convertToLongDate (flightPage.getDate("departureDate"));
        String returnDate = CoreFunctions.convertToLongDate (flightPage.getDate("returnDate"));

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Change currency");
        homePage.changeCurrency(currency,username);

        step("Open flights page");
        homePage.openVerticalPage("Flights");
        flightSearchPage.closePopup();

        step("Toggle trip type");
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

        step("Change date flight");
        flightSearchResultPage.changeDate(departureDate);

        step("Open & set airline filter");
        flightSearchResultPage.openFilter();
        flightSearchResultPage.filterAirline(airline);
        flightSearchResultPage.saveFilter();

        step("open and set transit filter");
        flightSearchResultPage.openTransitFilter();
        flightSearchResultPage.filterTransitType(transit);
        flightSearchResultPage.saveFilter();

        step("get route detail depart");
        String routeDepart = flightSearchResultPage.getRouteText();

        step("select stop over airline");
        flightSearchResultPage.selectFlightStopOver();

        step("verify stop over on pdp");
        Assertion.assertThat("is stopover flight",flightDescriptionPage.verifyStopOver(),is(true));

        step("verify logo, route and price breakdown on pdp depart");
        flightSearchResultPage.clickPricePolicyTab();
        Assertion.assertThat("is airline logo displayed", flightDescriptionPage.isAirlineLogoDisplayedPDP(),is(true));
        Assertion.assertThat("is route correct",flightDescriptionPage.isAirlineRouteCorrectPDP(routeDepart),is(true));
        Assertion.assertThat("is price breakdown correct",flightSearchResultPage.isPriceSumCorrect(),is(true));

        step("Select ticket");
        flightSearchResultPage.selectTicket();

        step("Change date flight return flight");
        flightSearchResultPage.changeDate(returnDate);

        step("Open & set airline filter");
        flightSearchResultPage.openFilter();
        flightSearchResultPage.filterAirline(airline);
        flightSearchResultPage.saveFilter();

        step("open and set transit filter");
        flightSearchResultPage.openTransitFilter();
        flightSearchResultPage.filterTransitType(transit);
        flightSearchResultPage.saveFilter();

        step("get route detail return");
        String routeReturn = flightSearchResultPage.getRouteText();

        step("select stop over airline");
        flightSearchResultPage.selectFlightStopOver();

        step("verify stop over on pdp");
        Assertion.assertThat("is stopover flight",flightDescriptionPage.verifyStopOver(),is(true));

        step("verify logo, route and price breakdown on pdp depart");
        flightSearchResultPage.clickPricePolicyTab();
        Assertion.assertThat("is airline logo displayed", flightDescriptionPage.isAirlineLogoDisplayedPDP(),is(true));
        Assertion.assertThat("is route correct",flightDescriptionPage.isAirlineRouteCorrectPDP(routeReturn),is(true));
        Assertion.assertThat("is price breakdown correct",flightSearchResultPage.isPriceSumCorrect(),is(true));

        step("Select ticket");
        flightSearchResultPage.selectTicket();

        step("verify airline logo, route and price breakdown correct on prp");
        Assertion.assertThat("is airline logo correct",flightProductReviewPage.isAirlineLogoDisplayedPRP(),is(true));
        Assertion.assertThat("is airline route correct",flightProductReviewPage.isAirlineRouteCorrectPRP(routeDepart),is(true));
        Assertion.assertThat("is price breakdown correct",flightProductReviewPage.verifyPriceBreakdownPRP(),is(true));

        step("move to booking page");
        flightProductReviewPage.continueToBookingForm(tripType);

        step("verify date format");
        flightBookingFormPage.verifyDateFormat(tripType);

        step("Fill passenger detail");
        flightBookingFormPage.fillPassengerDetail(adultPassengers,childPassengers,infantPassengers, flightRoute,airline);

        step("move to 2nd half of booking page");
        flightBookingFormPage.continueToSecondHalf();

        step("remove insurance/extra protection");
        flightBookingFormPage.removeExtraProtectionAll();

        step("open price detail");
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

        step("Check order ID");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",!orderID.isBlank(),is(true));

        step("open and verify price detail payment page");
        paymentPage.openPriceDetail();
        Assertion.assertThat("Is price breakdown correct on payment page", paymentPage.isPriceBreakdownCorrect(), is(true));
        paymentPage.closePriceDetail();

        step("Open payment method for order id: "+orderID);
        paymentPage.openPaymentMethod();

        step("Select ATM as payment method");
        paymentPage.selectPaymentMethod("ATM");

        step("Submit payment");
        paymentPage.clickPayButton();

        step("Go to the order list");
        sleep(50);
        paymentPage.seeOrderList();

        step("open order and check order status");
        boolean orderEticketStatus = orderListPage.getOrderStatus(orderID,username,password);
        Assertion.assertThat("Order Status is eticket issue",orderEticketStatus,is(true));

        step("Find the issued order");
        orderListPage.openActiveTicketViaDeeplink(orderID);
        
        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.getETicketLabel(), either(is("Your e-ticket is here")).or(is("E-tiketmu di sini")));
    }

    @Author(names = {"Ashish"})
    @Module(name = "PaidOrderAncillaries")
    @Tribe(name = "NewAirlineOnBoarding")
    @Vertical(name = "Flight")
    @Test
    public void testLoginBookingFlightOneWayDirectAddOnBaggageIssueETicket() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testLoginBookingFlightOneWayDirectAddOnBaggageIssueETicket");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightProductReviewPage<WebDriver> flightProductReviewPage = FlightProductReviewPage.getInstance(session);
        FlightBookingFormPage<WebDriver> flightBookingFormPage = FlightBookingFormPage.getInstance(session);
        FlightPage<WebDriver> flightPage = FlightPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);

        String tripType = param.get("tripType");
        String transit = param.get("transit");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");
        String flightRoute = param.get("flightRoute");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        int baggageAmount = Integer.parseInt(param.get("baggage"));
        String airline = param.get("airline");

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String currency = param.get("currency");

        PriceDetail priceDetail = new PriceDetail();
        priceDetail.tripType = tripType;
        priceDetail.adult = adultPassengers;
        priceDetail.child = childPassengers;
        priceDetail.infant = infantPassengers;

        step("Find dynamic data");
        flightPage.setDataOneWay(
                daysToDepartureDate,""
        );
        String departureDate =CoreFunctions.convertToLongDate (flightPage.getDate("departureDate"));

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Change currency");
        homePage.changeCurrency(currency,username);

        step("Open flights page");
        homePage.openVerticalPage("Flights");
        flightSearchPage.closePopup();

        step("Toggle trip type");
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

        step("Change date flight");
        flightSearchResultPage.changeDate(departureDate);

        step("Open & set airline filter");
        flightSearchResultPage.openFilter();
        flightSearchResultPage.filterAirline(airline);
        flightSearchResultPage.saveFilter();

        step("open and set transit filter");
        flightSearchResultPage.openTransitFilter();
        flightSearchResultPage.filterTransitType(transit);
        flightSearchResultPage.saveFilter();

        step("take route detail");
        String route = flightSearchResultPage.getRouteText();

        step("select first airline");
        flightSearchResultPage.selectFlight();

        step("verify airline logo, route and price breakdown correct on prp");
        Assertion.assertThat("is airline logo correct",flightProductReviewPage.isAirlineLogoDisplayedPRP(),is(true));
        Assertion.assertThat("is airline route correct",flightProductReviewPage.isAirlineRouteCorrectPRP(route),is(true));
        Assertion.assertThat("is price breakdown correct",flightProductReviewPage.verifyPriceBreakdownPRP(),is(true));

        step("move to booking page");
        flightProductReviewPage.continueToBookingForm(tripType);

        step("verify date format");
        flightBookingFormPage.verifyDateFormat(tripType);

        step("Fill passenger detail");
        flightBookingFormPage.fillPassengerDetail(adultPassengers,childPassengers,infantPassengers, flightRoute,airline);

        step("move to 2nd half of booking page");
        flightBookingFormPage.continueToSecondHalf();

        step("Add baggage");
        baggageAmount = flightBookingFormPage.addBaggage(priceDetail);
        String baggagePrice = flightBookingFormPage.getBaggagePrice(baggageAmount);
        int finalBaggageAmount = flightBookingFormPage.getBaggageAmount(baggageAmount,priceDetail);
        flightBookingFormPage.saveAddBaggage();

        step("remove insurance/extra protection");
        flightBookingFormPage.removeExtraProtectionAll();

        step("open price detail");
        flightBookingFormPage.openPriceDetail();

        step("Verify price detail");
        priceDetail.baggageDepartAmount=finalBaggageAmount;
        priceDetail.baggageDepartPrice=baggagePrice;
        Assertion.assertThat("Is price breakdown correct", flightBookingFormPage.isPriceBreakdownCorrect(priceDetail,currency), is(true));

        step("Continue to payment");
        flightBookingFormPage.continueToPayment();

        step("Skip full protection");
        flightBookingFormPage.skipFullProtection();

        step("Check order ID");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",!orderID.isBlank(),is(true));

        step("open and verify price detail payment page");
        paymentPage.openPriceDetail();
        Assertion.assertThat("Is price breakdown correct on payment page", paymentPage.isPriceBreakdownCorrect(), is(true));
        paymentPage.closePriceDetail();

        step("Open payment method for order id: "+orderID);
        paymentPage.openPaymentMethod();

        step("Select ATM as payment method");
        paymentPage.selectPaymentMethod("ATM");

        step("Submit payment");
        paymentPage.clickPayButton();

        step("Go to the order list");
        sleep(50);
        paymentPage.seeOrderList();

        step("open order and check order status");
        boolean orderEticketStatus = orderListPage.getOrderStatus(orderID,username,password);
        Assertion.assertThat("Order Status is eticket issue",orderEticketStatus,is(true));

        step("Find the issued order");
        orderListPage.openActiveTicketViaDeeplink(orderID);
        
        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.getETicketLabel(), either(is("Your e-ticket is here")).or(is("E-tiketmu di sini")));
    }

    @Author(names = {"Ashish"})
    @Module(name = "PaidOrderAncillaries")
    @Tribe(name = "NewAirlineOnBoarding")
    @Vertical(name = "Flight")
    @Test
    public void testLoginBookingFlightRoundTripDirectAddOnBaggageIssueETicket() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testLoginBookingFlightRoundTripDirectAddOnBaggageIssueETicket");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightDescriptionPage<WebDriver> flightDescriptionPage = FlightDescriptionPage.getInstance(session);
        FlightProductReviewPage<WebDriver> flightProductReviewPage = FlightProductReviewPage.getInstance(session);
        FlightBookingFormPage<WebDriver> flightBookingFormPage = FlightBookingFormPage.getInstance(session);
        FlightPage<WebDriver> flightPage = FlightPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);

        String tripType = param.get("tripType");
        String transit = param.get("transit");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");
        String flightRoute = param.get("flightRoute");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int duration = Integer.parseInt(param.get("duration"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        int baggageAmount = Integer.parseInt(param.get("baggage"));
        String airline = param.get("airline");

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");

        String currency = param.get("currency");

        PriceDetail priceDetail = new PriceDetail();
        priceDetail.tripType = tripType;
        priceDetail.adult = adultPassengers;
        priceDetail.child = childPassengers;
        priceDetail.infant = infantPassengers;

        step("Find dynamic data");
        flightPage.setDataRoundTrip(
                daysToDepartureDate,duration,airline,airline
        );
        String departureDate = CoreFunctions.convertToLongDate (flightPage.getDate("departureDate"));
        String returnDate = CoreFunctions.convertToLongDate (flightPage.getDate("returnDate"));

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Change currency");
        homePage.changeCurrency(currency,username);

        step("Open flights page");
        homePage.openVerticalPage("Flights");
        flightSearchPage.closePopup();

        step("Toggle trip type");
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

        step("Change date flight");
        flightSearchResultPage.changeDate(departureDate);

        step("Open & set airline filter");
        flightSearchResultPage.openFilter();
        flightSearchResultPage.filterAirline(airline);
        flightSearchResultPage.saveFilter();

        step("open and set transit filter");
        flightSearchResultPage.openTransitFilter();
        flightSearchResultPage.filterTransitType(transit);
        flightSearchResultPage.saveFilter();

        step("get route detail depart");
        String routeDepart = flightSearchResultPage.getRouteText();

        step("select first airline");
        flightSearchResultPage.selectFlight();

        step("verify logo, route and price breakdown on pdp depart");
        flightSearchResultPage.clickPricePolicyTab();
        Assertion.assertThat("is airline logo displayed", flightDescriptionPage.isAirlineLogoDisplayedPDP(),is(true));
        Assertion.assertThat("is route correct",flightDescriptionPage.isAirlineRouteCorrectPDP(routeDepart),is(true));
        Assertion.assertThat("is price breakdown correct",flightSearchResultPage.isPriceSumCorrect(),is(true));

        step("Select ticket");
        flightSearchResultPage.selectTicket();

        step("Change date flight return flight");
        flightSearchResultPage.changeDate(returnDate);

        step("Open & set airline filter");
        flightSearchResultPage.openFilter();
        flightSearchResultPage.filterAirline(airline);
        flightSearchResultPage.saveFilter();

        step("open and set transit filter");
        flightSearchResultPage.openTransitFilter();
        flightSearchResultPage.filterTransitType(transit);
        flightSearchResultPage.saveFilter();

        step("get route detail return");
        String routeReturn = flightSearchResultPage.getRouteText();

        step("select first airline");
        flightSearchResultPage.selectFlight();

        step("verify logo, route and price breakdown on pdp depart");
        flightSearchResultPage.clickPricePolicyTab();
        Assertion.assertThat("is airline logo displayed", flightDescriptionPage.isAirlineLogoDisplayedPDP(),is(true));
        Assertion.assertThat("is route correct",flightDescriptionPage.isAirlineRouteCorrectPDP(routeReturn),is(true));
        Assertion.assertThat("is price breakdown correct",flightSearchResultPage.isPriceSumCorrect(),is(true));

        step("Select ticket");
        flightSearchResultPage.selectTicket();

        step("verify airline logo, route and price breakdown correct on prp");
        Assertion.assertThat("is airline logo correct",flightProductReviewPage.isAirlineLogoDisplayedPRP(),is(true));
        Assertion.assertThat("is airline route correct",flightProductReviewPage.isAirlineRouteCorrectPRP(routeDepart),is(true));
        Assertion.assertThat("is price breakdown correct",flightProductReviewPage.verifyPriceBreakdownPRP(),is(true));

        step("move to booking page");
        flightProductReviewPage.continueToBookingForm(tripType);

        step("verify date format");
        flightBookingFormPage.verifyDateFormat(tripType);

        step("Fill passenger detail");
        flightBookingFormPage.fillPassengerDetail(adultPassengers,childPassengers,infantPassengers, flightRoute,airline);

        step("move to 2nd half of booking page");
        flightBookingFormPage.continueToSecondHalf();

        step("Add baggage");
        FlightBaggageDetail flightBaggageDetail = flightBookingFormPage.addBaggageRoundTrip(baggageAmount,priceDetail,currency);

        step("remove insurance/extra protection");
        flightBookingFormPage.removeExtraProtectionAll();

        step("open price detail");
        flightBookingFormPage.openPriceDetail();

        step("Verify price detail");
        priceDetail.flightBaggageDetail = flightBaggageDetail;
        Assertion.assertThat("Is price breakdown correct", flightBookingFormPage.isPriceBreakdownCorrect(priceDetail,currency), is(true));

        step("Continue to payment");
        flightBookingFormPage.continueToPayment();

        step("Skip full protection");
        flightBookingFormPage.skipFullProtection();

        step("Check order ID");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",!orderID.isBlank(),is(true));

        step("open and verify price detail payment page");
        paymentPage.openPriceDetail();
        Assertion.assertThat("Is price breakdown correct on payment page", paymentPage.isPriceBreakdownCorrect(), is(true));
        paymentPage.closePriceDetail();

        step("Open payment method for order id: "+orderID);
        paymentPage.openPaymentMethod();

        step("Select ATM as payment method");
        paymentPage.selectPaymentMethod("ATM");

        step("Submit payment");
        paymentPage.clickPayButton();

        step("Go to the order list");
        sleep(50);
        paymentPage.seeOrderList();

        step("open order and check order status");
        boolean orderEticketStatus = orderListPage.getOrderStatus(orderID,username,password);
        Assertion.assertThat("Order Status is eticket issue",orderEticketStatus,is(true));

        step("Find the issued order");
        orderListPage.openActiveTicketViaDeeplink(orderID);
        
        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.getETicketLabel(), either(is("Your e-ticket is here")).or(is("E-tiketmu di sini")));
    }

    @Author(names = {"Ashish"})
    @Module(name = "PaidOrderAncillaries")
    @Tribe(name = "NewAirlineOnBoarding")
    @Vertical(name = "Flight")
    @Test
    public void testLoginBookingFlightRoundTripTransitAddOnBaggageIssueETicket() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testLoginBookingFlightRoundTripTransitAddOnBaggageIssueETicket");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightDescriptionPage<WebDriver> flightDescriptionPage = FlightDescriptionPage.getInstance(session);
        FlightProductReviewPage<WebDriver> flightProductReviewPage = FlightProductReviewPage.getInstance(session);
        FlightBookingFormPage<WebDriver> flightBookingFormPage = FlightBookingFormPage.getInstance(session);
        FlightPage<WebDriver> flightPage = FlightPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);

        String tripType = param.get("tripType");
        String transit = param.get("transit");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");
        String flightRoute = param.get("flightRoute");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int duration = Integer.parseInt(param.get("duration"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        int baggageAmount = Integer.parseInt(param.get("baggage"));
        String airline = param.get("airline");

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");

        String currency = param.get("currency");

        PriceDetail priceDetail = new PriceDetail();
        priceDetail.tripType = tripType;
        priceDetail.adult = adultPassengers;
        priceDetail.child = childPassengers;
        priceDetail.infant = infantPassengers;

        step("Find dynamic data");
        flightPage.setDataRoundTrip(
                daysToDepartureDate,duration,airline,airline
        );
        String departureDate = CoreFunctions.convertToLongDate (flightPage.getDate("departureDate"));
        String returnDate = CoreFunctions.convertToLongDate (flightPage.getDate("returnDate"));

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Change currency");
        homePage.changeCurrency(currency,username);

        step("Open flights page");
        homePage.openVerticalPage("Flights");
        flightSearchPage.closePopup();

        step("Toggle trip type");
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

        step("Change date flight");
        flightSearchResultPage.changeDate(departureDate);

        step("Open & set airline filter");
        flightSearchResultPage.openFilter();
        flightSearchResultPage.filterAirline(airline);
        flightSearchResultPage.saveFilter();

        step("open and set transit filter");
        flightSearchResultPage.openTransitFilter();
        flightSearchResultPage.filterTransitType(transit);
        flightSearchResultPage.saveFilter();

        step("get route detail depart");
        String routeDepart = flightSearchResultPage.getRouteText();

        step("select first airline");
        flightSearchResultPage.selectFlight();

        step("verify logo, route and price breakdown on pdp depart");
        flightSearchResultPage.clickPricePolicyTab();
        Assertion.assertThat("is airline logo displayed", flightDescriptionPage.isAirlineLogoDisplayedPDP(),is(true));
        Assertion.assertThat("is route correct",flightDescriptionPage.isAirlineRouteCorrectPDP(routeDepart),is(true));
        Assertion.assertThat("is price breakdown correct",flightSearchResultPage.isPriceSumCorrect(),is(true));

        step("Select ticket");
        flightSearchResultPage.selectTicket();

        step("Change date flight return flight");
        flightSearchResultPage.changeDate(returnDate);

        step("Open & set airline filter");
        flightSearchResultPage.openFilter();
        flightSearchResultPage.filterAirline(airline);
        flightSearchResultPage.saveFilter();

        step("open and set transit filter");
        flightSearchResultPage.openTransitFilter();
        flightSearchResultPage.filterTransitType(transit);
        flightSearchResultPage.saveFilter();

        step("get route detail return");
        String routeReturn = flightSearchResultPage.getRouteText();

        step("select first airline");
        flightSearchResultPage.selectFlight();

        step("verify logo, route and price breakdown on pdp depart");
        flightSearchResultPage.clickPricePolicyTab();
        Assertion.assertThat("is airline logo displayed", flightDescriptionPage.isAirlineLogoDisplayedPDP(),is(true));
        Assertion.assertThat("is route correct",flightDescriptionPage.isAirlineRouteCorrectPDP(routeReturn),is(true));
        Assertion.assertThat("is price breakdown correct",flightSearchResultPage.isPriceSumCorrect(),is(true));

        step("Select ticket");
        flightSearchResultPage.selectTicket();

        step("verify airline logo, route and price breakdown correct on prp");
        Assertion.assertThat("is airline logo correct",flightProductReviewPage.isAirlineLogoDisplayedPRP(),is(true));
        Assertion.assertThat("is airline route correct",flightProductReviewPage.isAirlineRouteCorrectPRP(routeDepart),is(true));
        Assertion.assertThat("is price breakdown correct",flightProductReviewPage.verifyPriceBreakdownPRP(),is(true));

        step("move to booking page");
        flightProductReviewPage.continueToBookingForm(tripType);

        step("verify date format");
        flightBookingFormPage.verifyDateFormat(tripType);

        step("Fill passenger detail");
        flightBookingFormPage.fillPassengerDetail(adultPassengers,childPassengers,infantPassengers, flightRoute,airline);

        step("move to 2nd half of booking page");
        flightBookingFormPage.continueToSecondHalf();

        step("Add baggage");
        FlightBaggageDetail flightBaggageDetail = flightBookingFormPage.addBaggageRoundTrip(baggageAmount,priceDetail,currency);

        step("remove insurance/extra protection");
        flightBookingFormPage.removeExtraProtectionAll();

        step("open price detail");
        flightBookingFormPage.openPriceDetail();

        step("Verify price detail");
        priceDetail.flightBaggageDetail = flightBaggageDetail;
        Assertion.assertThat("Is price breakdown correct", flightBookingFormPage.isPriceBreakdownCorrect(priceDetail,currency), is(true));

        step("Continue to payment");
        flightBookingFormPage.continueToPayment();

        step("Skip full protection");
        flightBookingFormPage.skipFullProtection();

        step("Check order ID");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",!orderID.isBlank(),is(true));

        step("open and verify price detail payment page");
        paymentPage.openPriceDetail();
        Assertion.assertThat("Is price breakdown correct on payment page", paymentPage.isPriceBreakdownCorrect(), is(true));
        paymentPage.closePriceDetail();

        step("Open payment method for order id: "+orderID);
        paymentPage.openPaymentMethod();

        step("Select ATM as payment method");
        paymentPage.selectPaymentMethod("ATM");

        step("Submit payment");
        paymentPage.clickPayButton();

        step("Go to the order list");
        sleep(50);
        paymentPage.seeOrderList();

        step("open order and check order status");
        boolean orderEticketStatus = orderListPage.getOrderStatus(orderID,username,password);
        Assertion.assertThat("Order Status is eticket issue",orderEticketStatus,is(true));

        step("Find the issued order");
        orderListPage.openActiveTicketViaDeeplink(orderID);
        
        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.getETicketLabel(), either(is("Your e-ticket is here")).or(is("E-tiketmu di sini")));
    }

    @Author(names = {"Ashish"})
    @Module(name = "PaidOrderAncillaries")
    @Tribe(name = "NewAirlineOnBoarding")
    @Vertical(name = "Flight")
    @Test
    public void testLoginBookingFlightRoundTripStopOverAddOnBaggageIssueETicket() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testLoginBookingFlightRoundTripStopOverAddOnBaggageIssueETicket");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightDescriptionPage<WebDriver> flightDescriptionPage = FlightDescriptionPage.getInstance(session);
        FlightProductReviewPage<WebDriver> flightProductReviewPage = FlightProductReviewPage.getInstance(session);
        FlightBookingFormPage<WebDriver> flightBookingFormPage = FlightBookingFormPage.getInstance(session);
        FlightPage<WebDriver> flightPage = FlightPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);

        String tripType = param.get("tripType");
        String transit = param.get("transit");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");
        String flightRoute = param.get("flightRoute");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int duration = Integer.parseInt(param.get("duration"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        int baggageAmount = Integer.parseInt(param.get("baggage"));
        String airline = param.get("airline");

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");

        String currency = param.get("currency");

        PriceDetail priceDetail = new PriceDetail();
        priceDetail.tripType = tripType;
        priceDetail.adult = adultPassengers;
        priceDetail.child = childPassengers;
        priceDetail.infant = infantPassengers;

        step("Find dynamic data");
        flightPage.setDataRoundTrip(
                daysToDepartureDate,duration,airline,airline
        );
        String departureDate = CoreFunctions.convertToLongDate (flightPage.getDate("departureDate"));
        String returnDate = CoreFunctions.convertToLongDate (flightPage.getDate("returnDate"));

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Change currency");
        homePage.changeCurrency(currency,username);

        step("Open flights page");
        homePage.openVerticalPage("Flights");
        flightSearchPage.closePopup();

        step("Toggle trip type");
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

        step("Change date flight");
        flightSearchResultPage.changeDate(departureDate);

        step("Open & set airline filter");
        flightSearchResultPage.openFilter();
        flightSearchResultPage.filterAirline(airline);
        flightSearchResultPage.saveFilter();

        step("open and set transit filter");
        flightSearchResultPage.openTransitFilter();
        flightSearchResultPage.filterTransitType(transit);
        flightSearchResultPage.saveFilter();

        step("get route detail depart");
        String routeDepart = flightSearchResultPage.getRouteText();

        step("select stop over airline");
        flightSearchResultPage.selectFlightStopOver();

        step("verify stop over on pdp");
        Assertion.assertThat("is stopover flight",flightDescriptionPage.verifyStopOver(),is(true));

        step("verify logo, route and price breakdown on pdp depart");
        flightSearchResultPage.clickPricePolicyTab();
        Assertion.assertThat("is airline logo displayed", flightDescriptionPage.isAirlineLogoDisplayedPDP(),is(true));
        Assertion.assertThat("is route correct",flightDescriptionPage.isAirlineRouteCorrectPDP(routeDepart),is(true));
        Assertion.assertThat("is price breakdown correct",flightSearchResultPage.isPriceSumCorrect(),is(true));

        step("Select ticket");
        flightSearchResultPage.selectTicket();

        step("Change date flight return flight");
        flightSearchResultPage.changeDate(returnDate);

        step("Open & set airline filter");
        flightSearchResultPage.openFilter();
        flightSearchResultPage.filterAirline(airline);
        flightSearchResultPage.saveFilter();

        step("open and set transit filter");
        flightSearchResultPage.openTransitFilter();
        flightSearchResultPage.filterTransitType(transit);
        flightSearchResultPage.saveFilter();

        step("get route detail return");
        String routeReturn = flightSearchResultPage.getRouteText();

        step("select stop over airline");
        flightSearchResultPage.selectFlightStopOver();

        step("verify stop over on pdp");
        Assertion.assertThat("is stopover flight",flightDescriptionPage.verifyStopOver(),is(true));

        step("verify logo, route and price breakdown on pdp depart");
        flightSearchResultPage.clickPricePolicyTab();
        Assertion.assertThat("is airline logo displayed", flightDescriptionPage.isAirlineLogoDisplayedPDP(),is(true));
        Assertion.assertThat("is route correct",flightDescriptionPage.isAirlineRouteCorrectPDP(routeReturn),is(true));
        Assertion.assertThat("is price breakdown correct",flightSearchResultPage.isPriceSumCorrect(),is(true));

        step("Select ticket");
        flightSearchResultPage.selectTicket();

        step("verify airline logo, route and price breakdown correct on prp");
        Assertion.assertThat("is airline logo correct",flightProductReviewPage.isAirlineLogoDisplayedPRP(),is(true));
        Assertion.assertThat("is airline route correct",flightProductReviewPage.isAirlineRouteCorrectPRP(routeDepart),is(true));
        Assertion.assertThat("is price breakdown correct",flightProductReviewPage.verifyPriceBreakdownPRP(),is(true));

        step("move to booking page");
        flightProductReviewPage.continueToBookingForm(tripType);

        step("verify date format");
        flightBookingFormPage.verifyDateFormat(tripType);

        step("Fill passenger detail");
        flightBookingFormPage.fillPassengerDetail(adultPassengers,childPassengers,infantPassengers, flightRoute,airline);

        step("move to 2nd half of booking page");
        flightBookingFormPage.continueToSecondHalf();

        step("Add baggage");
        FlightBaggageDetail flightBaggageDetail = flightBookingFormPage.addBaggageRoundTrip(baggageAmount,priceDetail,currency);

        step("remove insurance/extra protection");
        flightBookingFormPage.removeExtraProtectionAll();

        step("open price detail");
        flightBookingFormPage.openPriceDetail();

        step("Verify price detail");
        priceDetail.flightBaggageDetail = flightBaggageDetail;
        Assertion.assertThat("Is price breakdown correct", flightBookingFormPage.isPriceBreakdownCorrect(priceDetail,currency), is(true));

        step("Continue to payment");
        flightBookingFormPage.continueToPayment();

        step("Skip full protection");
        flightBookingFormPage.skipFullProtection();

        step("Check order ID");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",!orderID.isBlank(),is(true));

        step("open and verify price detail payment page");
        paymentPage.openPriceDetail();
        Assertion.assertThat("Is price breakdown correct on payment page", paymentPage.isPriceBreakdownCorrect(), is(true));
        paymentPage.closePriceDetail();

        step("Open payment method for order id: "+orderID);
        paymentPage.openPaymentMethod();

        step("Select ATM as payment method");
        paymentPage.selectPaymentMethod("ATM");

        step("Submit payment");
        paymentPage.clickPayButton();

        step("Go to the order list");
        sleep(50);
        paymentPage.seeOrderList();

        step("open order and check order status");
        boolean orderEticketStatus = orderListPage.getOrderStatus(orderID,username,password);
        Assertion.assertThat("Order Status is eticket issue",orderEticketStatus,is(true));

        step("Find the issued order");
        orderListPage.openActiveTicketViaDeeplink(orderID);
        
        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.getETicketLabel(), either(is("Your e-ticket is here")).or(is("E-tiketmu di sini")));
    }

    @Author(names = {"Ashish"})
    @Module(name = "PaidOrderAncillaries")
    @Tribe(name = "NewAirlineOnBoarding")
    @Vertical(name = "Flight")
    @Test
    public void testLoginBookingFlightOneWayDirectAddOnMealsIssueETicket() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testLoginBookingFlightOneWayDirectAddOnMealsIssueETicket");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightProductReviewPage<WebDriver> flightProductReviewPage = FlightProductReviewPage.getInstance(session);
        FlightBookingFormPage<WebDriver> flightBookingFormPage = FlightBookingFormPage.getInstance(session);
        FlightPage<WebDriver> flightPage = FlightPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);

        String tripType = param.get("tripType");
        String transit = param.get("transit");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");
        String flightRoute = param.get("flightRoute");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        String airline = param.get("airline");

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");

        String currency = param.get("currency");

        step("Find dynamic data");
        flightPage.setDataOneWay(
                daysToDepartureDate,""
        );
        String departureDate =CoreFunctions.convertToLongDate (flightPage.getDate("departureDate"));

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Change currency");
        homePage.changeCurrency(currency,username);

        step("Open flights page");
        homePage.openVerticalPage("Flights");
        flightSearchPage.closePopup();

        step("Toggle trip type");
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

        step("Change date flight");
        flightSearchResultPage.changeDate(departureDate);

        step("Open & set airline filter");
        flightSearchResultPage.openFilter();
        flightSearchResultPage.filterAirline(airline);
        flightSearchResultPage.saveFilter();

        step("open and set transit filter");
        flightSearchResultPage.openTransitFilter();
        flightSearchResultPage.filterTransitType(transit);
        flightSearchResultPage.saveFilter();

        step("take route detail");
        String route = flightSearchResultPage.getRouteText();

        step("select first airline");
        flightSearchResultPage.selectFlight();

        step("verify airline logo, route and price breakdown correct on prp");
        Assertion.assertThat("is airline logo correct",flightProductReviewPage.isAirlineLogoDisplayedPRP(),is(true));
        Assertion.assertThat("is airline route correct",flightProductReviewPage.isAirlineRouteCorrectPRP(route),is(true));
        Assertion.assertThat("is price breakdown correct",flightProductReviewPage.verifyPriceBreakdownPRP(),is(true));

        step("move to booking page");
        flightProductReviewPage.continueToBookingForm(tripType);

        step("verify date format");
        flightBookingFormPage.verifyDateFormat(tripType);

        step("Fill passenger detail");
        flightBookingFormPage.fillPassengerDetail(adultPassengers,childPassengers,infantPassengers, flightRoute,airline);

        step("move to 2nd half of booking page");
        flightBookingFormPage.continueToSecondHalf();

        step("Add AddOnMeals");
        String mealPrice=flightBookingFormPage.addMeals(adultPassengers,childPassengers,currency);

        step("remove insurance/extra protection");
        flightBookingFormPage.removeExtraProtectionAll();

        step("open price detail");
        flightBookingFormPage.openPriceDetail();

        step("Verify price detail");
        PriceDetail priceDetail = new PriceDetail();
        priceDetail.tripType = tripType;
        priceDetail.adult = adultPassengers;
        priceDetail.child = childPassengers;
        priceDetail.infant = infantPassengers;
        priceDetail.mealPrice=mealPrice;
        Assertion.assertThat("Is price breakdown correct", flightBookingFormPage.isPriceBreakdownCorrect(priceDetail,currency), is(true));

        step("Continue to payment");
        flightBookingFormPage.continueToPayment();

        step("Skip full protection");
        flightBookingFormPage.skipFullProtection();

        step("Check order ID");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",!orderID.isBlank(),is(true));

        step("open and verify price detail payment page");
        paymentPage.openPriceDetail();
        Assertion.assertThat("Is price breakdown correct on payment page", paymentPage.isPriceBreakdownCorrect(), is(true));
        paymentPage.closePriceDetail();

        step("Open payment method for order id: "+orderID);
        paymentPage.openPaymentMethod();

        step("Select ATM as payment method");
        paymentPage.selectPaymentMethod("ATM");

        step("Submit payment");
        paymentPage.clickPayButton();

        step("Go to the order list");
        sleep(50);
        paymentPage.seeOrderList();

        step("open order and check order status");
        boolean orderEticketStatus = orderListPage.getOrderStatus(orderID,username,password);
        Assertion.assertThat("Order Status is eticket issue",orderEticketStatus,is(true));

        step("Find the issued order");
        orderListPage.openActiveTicketViaDeeplink(orderID);
        
        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.getETicketLabel(), either(is("Your e-ticket is here")).or(is("E-tiketmu di sini")));
    }

    @Author(names = {"Ashish"})
    @Module(name = "PaidOrderAncillaries")
    @Tribe(name = "NewAirlineOnBoarding")
    @Vertical(name = "Flight")
    @Test
    public void testLoginBookingFlightRoundTripDirectAddOnMealsIssueETicket() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testLoginBookingFlightRoundTripDirectAddOnMealsIssueETicket");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightDescriptionPage<WebDriver> flightDescriptionPage = FlightDescriptionPage.getInstance(session);
        FlightProductReviewPage<WebDriver> flightProductReviewPage = FlightProductReviewPage.getInstance(session);
        FlightBookingFormPage<WebDriver> flightBookingFormPage = FlightBookingFormPage.getInstance(session);
        FlightPage<WebDriver> flightPage = FlightPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);

        String tripType = param.get("tripType");
        String transit = param.get("transit");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");
        String flightRoute = param.get("flightRoute");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int duration = Integer.parseInt(param.get("duration"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        String airline = param.get("airline");

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");

        String currency = param.get("currency");

        step("Find dynamic data");
        flightPage.setDataRoundTrip(
                daysToDepartureDate,duration,airline,airline
        );
        String departureDate = CoreFunctions.convertToLongDate (flightPage.getDate("departureDate"));
        String returnDate = CoreFunctions.convertToLongDate (flightPage.getDate("returnDate"));

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Change currency");
        homePage.changeCurrency(currency,username);

        step("Open flights page");
        homePage.openVerticalPage("Flights");
        flightSearchPage.closePopup();

        step("Toggle trip type");
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

        step("Change date flight");
        flightSearchResultPage.changeDate(departureDate);

        step("Open & set airline filter");
        flightSearchResultPage.openFilter();
        flightSearchResultPage.filterAirline(airline);
        flightSearchResultPage.saveFilter();

        step("open and set transit filter");
        flightSearchResultPage.openTransitFilter();
        flightSearchResultPage.filterTransitType(transit);
        flightSearchResultPage.saveFilter();

        step("get route detail depart");
        String routeDepart = flightSearchResultPage.getRouteText();

        step("select first airline");
        flightSearchResultPage.selectFlight();

        step("verify logo, route and price breakdown on pdp depart");
        flightSearchResultPage.clickPricePolicyTab();
        Assertion.assertThat("is airline logo displayed", flightDescriptionPage.isAirlineLogoDisplayedPDP(),is(true));
        Assertion.assertThat("is route correct",flightDescriptionPage.isAirlineRouteCorrectPDP(routeDepart),is(true));
        Assertion.assertThat("is price breakdown correct",flightSearchResultPage.isPriceSumCorrect(),is(true));

        step("Select ticket");
        flightSearchResultPage.selectTicket();

        step("Change date flight return flight");
        flightSearchResultPage.changeDate(returnDate);

        step("Open & set airline filter");
        flightSearchResultPage.openFilter();
        flightSearchResultPage.filterAirline(airline);
        flightSearchResultPage.saveFilter();

        step("open and set transit filter");
        flightSearchResultPage.openTransitFilter();
        flightSearchResultPage.filterTransitType(transit);
        flightSearchResultPage.saveFilter();

        step("get route detail return");
        String routeReturn = flightSearchResultPage.getRouteText();

        step("select first airline");
        flightSearchResultPage.selectFlight();

        step("verify logo, route and price breakdown on pdp depart");
        flightSearchResultPage.clickPricePolicyTab();
        Assertion.assertThat("is airline logo displayed", flightDescriptionPage.isAirlineLogoDisplayedPDP(),is(true));
        Assertion.assertThat("is route correct",flightDescriptionPage.isAirlineRouteCorrectPDP(routeReturn),is(true));
        Assertion.assertThat("is price breakdown correct",flightSearchResultPage.isPriceSumCorrect(),is(true));

        step("Select ticket");
        flightSearchResultPage.selectTicket();

        step("verify airline logo, route and price breakdown correct on prp");
        Assertion.assertThat("is airline logo correct",flightProductReviewPage.isAirlineLogoDisplayedPRP(),is(true));
        Assertion.assertThat("is airline route correct",flightProductReviewPage.isAirlineRouteCorrectPRP(routeDepart),is(true));
        Assertion.assertThat("is price breakdown correct",flightProductReviewPage.verifyPriceBreakdownPRP(),is(true));

        step("move to booking page");
        flightProductReviewPage.continueToBookingForm(tripType);

        step("verify date format");
        flightBookingFormPage.verifyDateFormat(tripType);

        step("Fill passenger detail");
        flightBookingFormPage.fillPassengerDetail(adultPassengers,childPassengers,infantPassengers, flightRoute,airline);

        step("move to 2nd half of booking page");
        flightBookingFormPage.continueToSecondHalf();

        step("Add AddOnMeals");
        FlightMealsDetail flightMealsDetail = flightBookingFormPage.addMealsRoundTrip(adultPassengers,childPassengers,currency);

        step("remove insurance/extra protection");
        flightBookingFormPage.removeExtraProtectionAll();

        step("open price detail");
        flightBookingFormPage.openPriceDetail();

        step("Verify price detail");
        PriceDetail priceDetail = new PriceDetail();
        priceDetail.tripType = tripType;
        priceDetail.adult = adultPassengers;
        priceDetail.child = childPassengers;
        priceDetail.infant = infantPassengers;
        priceDetail.flightMealsDetail = flightMealsDetail;
        Assertion.assertThat("Is price breakdown correct", flightBookingFormPage.isPriceBreakdownCorrect(priceDetail,currency), is(true));

        step("Continue to payment");
        flightBookingFormPage.continueToPayment();

        step("Skip full protection");
        flightBookingFormPage.skipFullProtection();

        step("Check order ID");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",!orderID.isBlank(),is(true));

        step("open and verify price detail payment page");
        paymentPage.openPriceDetail();
        Assertion.assertThat("Is price breakdown correct on payment page", paymentPage.isPriceBreakdownCorrect(), is(true));
        paymentPage.closePriceDetail();


        step("Open payment method for order id: "+orderID);
        paymentPage.openPaymentMethod();

        step("Select ATM as payment method");
        paymentPage.selectPaymentMethod("ATM");

        step("Submit payment");
        paymentPage.clickPayButton();

        step("Go to the order list");
        sleep(50);
        paymentPage.seeOrderList();

        step("open order and check order status");
        boolean orderEticketStatus = orderListPage.getOrderStatus(orderID,username,password);
        Assertion.assertThat("Order Status is eticket issue",orderEticketStatus,is(true));

        step("Find the issued order");
        orderListPage.openActiveTicketViaDeeplink(orderID);
        
        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.getETicketLabel(), either(is("Your e-ticket is here")).or(is("E-tiketmu di sini")));
    }

    @Author(names = {"Ashish"})
    @Module(name = "PaidOrderAncillaries")
    @Tribe(name = "NewAirlineOnBoarding")
    @Vertical(name = "Flight")
    @Test
    public void testLoginBookingFlightRoundTripTransitAddOnMealsIssueETicket() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testLoginBookingFlightRoundTripTransitAddOnMealsIssueETicket");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightDescriptionPage<WebDriver> flightDescriptionPage = FlightDescriptionPage.getInstance(session);
        FlightProductReviewPage<WebDriver> flightProductReviewPage = FlightProductReviewPage.getInstance(session);
        FlightBookingFormPage<WebDriver> flightBookingFormPage = FlightBookingFormPage.getInstance(session);
        FlightPage<WebDriver> flightPage = FlightPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);

        String tripType = param.get("tripType");
        String transit = param.get("transit");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");
        String flightRoute = param.get("flightRoute");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int duration = Integer.parseInt(param.get("duration"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        String airline = param.get("airline");

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");

        String currency = param.get("currency");

        step("Find dynamic data");
        flightPage.setDataRoundTrip(
                daysToDepartureDate,duration,airline,airline
        );
        String departureDate = CoreFunctions.convertToLongDate (flightPage.getDate("departureDate"));
        String returnDate = CoreFunctions.convertToLongDate (flightPage.getDate("returnDate"));

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Change currency");
        homePage.changeCurrency(currency,username);

        step("Open flights page");
        homePage.openVerticalPage("Flights");
        flightSearchPage.closePopup();

        step("Toggle trip type");
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

        step("Change date flight");
        flightSearchResultPage.changeDate(departureDate);

        step("Open & set airline filter");
        flightSearchResultPage.openFilter();
        flightSearchResultPage.filterAirline(airline);
        flightSearchResultPage.saveFilter();

        step("open and set transit filter");
        flightSearchResultPage.openTransitFilter();
        flightSearchResultPage.filterTransitType(transit);
        flightSearchResultPage.saveFilter();

        step("get route detail depart");
        String routeDepart = flightSearchResultPage.getRouteText();

        step("select first airline");
        flightSearchResultPage.selectFlight();

        step("verify logo, route and price breakdown on pdp depart");
        flightSearchResultPage.clickPricePolicyTab();
        Assertion.assertThat("is airline logo displayed", flightDescriptionPage.isAirlineLogoDisplayedPDP(),is(true));
        Assertion.assertThat("is route correct",flightDescriptionPage.isAirlineRouteCorrectPDP(routeDepart),is(true));
        Assertion.assertThat("is price breakdown correct",flightSearchResultPage.isPriceSumCorrect(),is(true));

        step("Select ticket");
        flightSearchResultPage.selectTicket();

        step("Change date flight return flight");
        flightSearchResultPage.changeDate(returnDate);

        step("Open & set airline filter");
        flightSearchResultPage.openFilter();
        flightSearchResultPage.filterAirline(airline);
        flightSearchResultPage.saveFilter();

        step("open and set transit filter");
        flightSearchResultPage.openTransitFilter();
        flightSearchResultPage.filterTransitType(transit);
        flightSearchResultPage.saveFilter();

        step("get route detail return");
        String routeReturn = flightSearchResultPage.getRouteText();

        step("select first airline");
        flightSearchResultPage.selectFlight();

        step("verify logo, route and price breakdown on pdp depart");
        flightSearchResultPage.clickPricePolicyTab();
        Assertion.assertThat("is airline logo displayed", flightDescriptionPage.isAirlineLogoDisplayedPDP(),is(true));
        Assertion.assertThat("is route correct",flightDescriptionPage.isAirlineRouteCorrectPDP(routeReturn),is(true));
        Assertion.assertThat("is price breakdown correct",flightSearchResultPage.isPriceSumCorrect(),is(true));

        step("Select ticket");
        flightSearchResultPage.selectTicket();

        step("verify airline logo, route and price breakdown correct on prp");
        Assertion.assertThat("is airline logo correct",flightProductReviewPage.isAirlineLogoDisplayedPRP(),is(true));
        Assertion.assertThat("is airline route correct",flightProductReviewPage.isAirlineRouteCorrectPRP(routeDepart),is(true));
        Assertion.assertThat("is price breakdown correct",flightProductReviewPage.verifyPriceBreakdownPRP(),is(true));

        step("move to booking page");
        flightProductReviewPage.continueToBookingForm(tripType);

        step("verify date format");
        flightBookingFormPage.verifyDateFormat(tripType);

        step("Fill passenger detail");
        flightBookingFormPage.fillPassengerDetail(adultPassengers,childPassengers,infantPassengers, flightRoute,airline);

        step("move to 2nd half of booking page");
        flightBookingFormPage.continueToSecondHalf();

        step("Add AddOnMeals");
        FlightMealsDetail flightMealsDetail = flightBookingFormPage.addMealsRoundTrip(adultPassengers,childPassengers,currency);

        step("remove insurance/extra protection");
        flightBookingFormPage.removeExtraProtectionAll();

        step("open price detail");
        flightBookingFormPage.openPriceDetail();

        step("Verify price detail");
        PriceDetail priceDetail = new PriceDetail();
        priceDetail.tripType = tripType;
        priceDetail.adult = adultPassengers;
        priceDetail.child = childPassengers;
        priceDetail.infant = infantPassengers;
        priceDetail.flightMealsDetail = flightMealsDetail;
        Assertion.assertThat("Is price breakdown correct", flightBookingFormPage.isPriceBreakdownCorrect(priceDetail,currency), is(true));

        step("Continue to payment");
        flightBookingFormPage.continueToPayment();

        step("Skip full protection");
        flightBookingFormPage.skipFullProtection();

        step("Check order ID");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",!orderID.isBlank(),is(true));

        step("open and verify price detail payment page");
        paymentPage.openPriceDetail();
        Assertion.assertThat("Is price breakdown correct on payment page", paymentPage.isPriceBreakdownCorrect(), is(true));
        paymentPage.closePriceDetail();

        step("Open payment method for order id: "+orderID);
        paymentPage.openPaymentMethod();

        step("Select ATM as payment method");
        paymentPage.selectPaymentMethod("ATM");

        step("Submit payment");
        paymentPage.clickPayButton();

        step("Go to the order list");
        sleep(50);
        paymentPage.seeOrderList();

        step("open order and check order status");
        boolean orderEticketStatus = orderListPage.getOrderStatus(orderID,username,password);
        Assertion.assertThat("Order Status is eticket issue",orderEticketStatus,is(true));

        step("Find the issued order");
        orderListPage.openActiveTicketViaDeeplink(orderID);
        
        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.getETicketLabel(), either(is("Your e-ticket is here")).or(is("E-tiketmu di sini")));
    }

    @Author(names = {"Ashish"})
    @Module(name = "PaidOrderAncillaries")
    @Tribe(name = "NewAirlineOnBoarding")
    @Vertical(name = "Flight")
    @Test
    public void testLoginBookingFlightRoundTripStopOverAddOnMealsIssueETicket() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testLoginBookingFlightRoundTripStopOverAddOnMealsIssueETicket");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightDescriptionPage<WebDriver> flightDescriptionPage = FlightDescriptionPage.getInstance(session);
        FlightProductReviewPage<WebDriver> flightProductReviewPage = FlightProductReviewPage.getInstance(session);
        FlightBookingFormPage<WebDriver> flightBookingFormPage = FlightBookingFormPage.getInstance(session);
        FlightPage<WebDriver> flightPage = FlightPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);

        String tripType = param.get("tripType");
        String transit = param.get("transit");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");
        String flightRoute = param.get("flightRoute");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int duration = Integer.parseInt(param.get("duration"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        String airline = param.get("airline");

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");

        String currency = param.get("currency");

        step("Find dynamic data");
        flightPage.setDataRoundTrip(
                daysToDepartureDate,duration,airline,airline
        );
        String departureDate = CoreFunctions.convertToLongDate (flightPage.getDate("departureDate"));
        String returnDate = CoreFunctions.convertToLongDate (flightPage.getDate("returnDate"));

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Change currency");
        homePage.changeCurrency(currency,username);

        step("Open flights page");
        homePage.openVerticalPage("Flights");
        flightSearchPage.closePopup();

        step("Toggle trip type");
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

        step("Change date flight");
        flightSearchResultPage.changeDate(departureDate);

        step("Open & set airline filter");
        flightSearchResultPage.openFilter();
        flightSearchResultPage.filterAirline(airline);
        flightSearchResultPage.saveFilter();

        step("open and set transit filter");
        flightSearchResultPage.openTransitFilter();
        flightSearchResultPage.filterTransitType(transit);
        flightSearchResultPage.saveFilter();

        step("get route detail depart");
        String routeDepart = flightSearchResultPage.getRouteText();

        step("select stop over airline");
        flightSearchResultPage.selectFlightStopOver();

        step("verify stop over on pdp");
        Assertion.assertThat("is stopover flight",flightDescriptionPage.verifyStopOver(),is(true));

        step("verify logo, route and price breakdown on pdp depart");
        flightSearchResultPage.clickPricePolicyTab();
        Assertion.assertThat("is airline logo displayed", flightDescriptionPage.isAirlineLogoDisplayedPDP(),is(true));
        Assertion.assertThat("is route correct",flightDescriptionPage.isAirlineRouteCorrectPDP(routeDepart),is(true));
        Assertion.assertThat("is price breakdown correct",flightSearchResultPage.isPriceSumCorrect(),is(true));

        step("Select ticket");
        flightSearchResultPage.selectTicket();

        step("Change date flight return flight");
        flightSearchResultPage.changeDate(returnDate);

        step("Open & set airline filter");
        flightSearchResultPage.openFilter();
        flightSearchResultPage.filterAirline(airline);
        flightSearchResultPage.saveFilter();

        step("open and set transit filter");
        flightSearchResultPage.openTransitFilter();
        flightSearchResultPage.filterTransitType(transit);
        flightSearchResultPage.saveFilter();

        step("get route detail return");
        String routeReturn = flightSearchResultPage.getRouteText();

        step("select stop over airline");
        flightSearchResultPage.selectFlightStopOver();

        step("verify stop over on pdp");
        Assertion.assertThat("is stopover flight",flightDescriptionPage.verifyStopOver(),is(true));

        step("verify logo, route and price breakdown on pdp depart");
        flightSearchResultPage.clickPricePolicyTab();
        Assertion.assertThat("is airline logo displayed", flightDescriptionPage.isAirlineLogoDisplayedPDP(),is(true));
        Assertion.assertThat("is route correct",flightDescriptionPage.isAirlineRouteCorrectPDP(routeReturn),is(true));
        Assertion.assertThat("is price breakdown correct",flightSearchResultPage.isPriceSumCorrect(),is(true));

        step("Select ticket");
        flightSearchResultPage.selectTicket();

        step("verify airline logo, route and price breakdown correct on prp");
        Assertion.assertThat("is airline logo correct",flightProductReviewPage.isAirlineLogoDisplayedPRP(),is(true));
        Assertion.assertThat("is airline route correct",flightProductReviewPage.isAirlineRouteCorrectPRP(routeDepart),is(true));
        Assertion.assertThat("is price breakdown correct",flightProductReviewPage.verifyPriceBreakdownPRP(),is(true));

        step("move to booking page");
        flightProductReviewPage.continueToBookingForm(tripType);

        step("verify date format");
        flightBookingFormPage.verifyDateFormat(tripType);

        step("Fill passenger detail");
        flightBookingFormPage.fillPassengerDetail(adultPassengers,childPassengers,infantPassengers, flightRoute,airline);

        step("move to 2nd half of booking page");
        flightBookingFormPage.continueToSecondHalf();

        step("Add AddOnMeals");
        FlightMealsDetail flightMealsDetail = flightBookingFormPage.addMealsRoundTrip(adultPassengers,childPassengers,currency);

        step("remove insurance/extra protection");
        flightBookingFormPage.removeExtraProtectionAll();

        step("open price detail");
        flightBookingFormPage.openPriceDetail();

        step("Verify price detail");
        PriceDetail priceDetail = new PriceDetail();
        priceDetail.tripType = tripType;
        priceDetail.adult = adultPassengers;
        priceDetail.child = childPassengers;
        priceDetail.infant = infantPassengers;
        priceDetail.flightMealsDetail = flightMealsDetail;
        Assertion.assertThat("Is price breakdown correct", flightBookingFormPage.isPriceBreakdownCorrect(priceDetail,currency), is(true));

        step("Continue to payment");
        flightBookingFormPage.continueToPayment();

        step("Skip full protection");
        flightBookingFormPage.skipFullProtection();

        step("Check order ID");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",!orderID.isBlank(),is(true));

        step("open and verify price detail payment page");
        paymentPage.openPriceDetail();
        Assertion.assertThat("Is price breakdown correct on payment page", paymentPage.isPriceBreakdownCorrect(), is(true));
        paymentPage.closePriceDetail();

        step("Open payment method for order id: "+orderID);
        paymentPage.openPaymentMethod();

        step("Select ATM as payment method");
        paymentPage.selectPaymentMethod("ATM");

        step("Submit payment");
        paymentPage.clickPayButton();

        step("Go to the order list");
        sleep(50);
        paymentPage.seeOrderList();

        step("open order and check order status");
        boolean orderEticketStatus = orderListPage.getOrderStatus(orderID,username,password);
        Assertion.assertThat("Order Status is eticket issue",orderEticketStatus,is(true));

        step("Find the issued order");
        orderListPage.openActiveTicketViaDeeplink(orderID);
        
        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.getETicketLabel(), either(is("Your e-ticket is here")).or(is("E-tiketmu di sini")));
    }

    @Author(names = {"Ashish"})
    @Module(name = "PaidOrderAncillaries")
    @Tribe(name = "NewAirlineOnBoarding")
    @Vertical(name = "Flight")
    @Test
    public void testLoginBookingFlightOneWayDirectAddOnSeatIssueETicket() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testLoginBookingFlightOneWayDirectAddOnSeatIssueETicket");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightProductReviewPage<WebDriver> flightProductReviewPage = FlightProductReviewPage.getInstance(session);
        FlightBookingFormPage<WebDriver> flightBookingFormPage = FlightBookingFormPage.getInstance(session);
        FlightPage<WebDriver> flightPage = FlightPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);

        String tripType = param.get("tripType");
        String transit = param.get("transit");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");
        String flightRoute = param.get("flightRoute");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        String airline = param.get("airline");

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");

        String currency = param.get("currency");

        step("Find dynamic data");
        flightPage.setDataOneWay(
                daysToDepartureDate,""
        );
        String departureDate =CoreFunctions.convertToLongDate (flightPage.getDate("departureDate"));

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Change currency");
        homePage.changeCurrency(currency,username);

        step("Open flights page");
        homePage.openVerticalPage("Flights");
        flightSearchPage.closePopup();

        step("Toggle trip type");
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

        step("Change date flight");
        flightSearchResultPage.changeDate(departureDate);

        step("Open & set airline filter");
        flightSearchResultPage.openFilter();
        flightSearchResultPage.filterAirline(airline);
        flightSearchResultPage.saveFilter();

        step("open and set transit filter");
        flightSearchResultPage.openTransitFilter();
        flightSearchResultPage.filterTransitType(transit);
        flightSearchResultPage.saveFilter();

        step("take route detail");
        String route = flightSearchResultPage.getRouteText();

        step("select first airline");
        flightSearchResultPage.selectFlight();

        step("verify airline logo, route and price breakdown correct on prp");
        Assertion.assertThat("is airline logo correct",flightProductReviewPage.isAirlineLogoDisplayedPRP(),is(true));
        Assertion.assertThat("is airline route correct",flightProductReviewPage.isAirlineRouteCorrectPRP(route),is(true));
        Assertion.assertThat("is price breakdown correct",flightProductReviewPage.verifyPriceBreakdownPRP(),is(true));

        step("move to booking page");
        flightProductReviewPage.continueToBookingForm(tripType);

        step("verify date format");
        flightBookingFormPage.verifyDateFormat(tripType);

        step("Fill passenger detail");
        flightBookingFormPage.fillPassengerDetail(adultPassengers,childPassengers,infantPassengers, flightRoute,airline);

        step("move to 2nd half of booking page");
        flightBookingFormPage.continueToSecondHalf();

        step("Add AddOnSeat");
        PriceDetail priceDetail = new PriceDetail();
        priceDetail.tripType=tripType;
        priceDetail.adult = adultPassengers;
        priceDetail.child = childPassengers;
        priceDetail.infant = infantPassengers;
        String seatPrice=flightBookingFormPage.addSeat(priceDetail,currency);

        step("remove insurance/extra protection");
        flightBookingFormPage.removeExtraProtectionAll();

        step("open price detail");
        flightBookingFormPage.openPriceDetail();

        step("Verify price detail");
        priceDetail.seatDepartPrice=seatPrice;
        Assertion.assertThat("Is price breakdown correct", flightBookingFormPage.isPriceBreakdownCorrect(priceDetail,currency), is(true));

        step("Continue to payment");
        flightBookingFormPage.continueToPayment();

        step("Skip full protection");
        flightBookingFormPage.skipFullProtection();

        step("Check order ID");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",!orderID.isBlank(),is(true));

        step("open and verify price detail payment page");
        paymentPage.openPriceDetail();
        Assertion.assertThat("Is price breakdown correct on payment page", paymentPage.isPriceBreakdownCorrect(), is(true));
        paymentPage.closePriceDetail();

        step("Open payment method for order id: "+orderID);
        paymentPage.openPaymentMethod();

        step("Select ATM as payment method");
        paymentPage.selectPaymentMethod("ATM");

        step("Submit payment");
        paymentPage.clickPayButton();

        step("Go to the order list");
        sleep(50);
        paymentPage.seeOrderList();

        step("open order and check order status");
        boolean orderEticketStatus = orderListPage.getOrderStatus(orderID,username,password);
        Assertion.assertThat("Order Status is eticket issue",orderEticketStatus,is(true));

        step("Find the issued order");
        orderListPage.openActiveTicketViaDeeplink(orderID);
        
        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.getETicketLabel(), either(is("Your e-ticket is here")).or(is("E-tiketmu di sini")));
    }

    @Author(names = {"Ashish"})
    @Module(name = "PaidOrderAncillaries")
    @Tribe(name = "NewAirlineOnBoarding")
    @Vertical(name = "Flight")
    @Test
    public void testLoginBookingFlightRoundTripDirectAddOnSeatIssueETicket() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testLoginBookingFlightRoundTripDirectAddOnSeatIssueETicket");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightDescriptionPage<WebDriver> flightDescriptionPage = FlightDescriptionPage.getInstance(session);
        FlightProductReviewPage<WebDriver> flightProductReviewPage = FlightProductReviewPage.getInstance(session);
        FlightBookingFormPage<WebDriver> flightBookingFormPage = FlightBookingFormPage.getInstance(session);
        FlightPage<WebDriver> flightPage = FlightPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);

        String tripType = param.get("tripType");
        String transit = param.get("transit");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");
        String flightRoute = param.get("flightRoute");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int duration = Integer.parseInt(param.get("duration"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        String airline = param.get("airline");

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");

        String currency = param.get("currency");

        step("Find dynamic data");
        flightPage.setDataRoundTrip(
                daysToDepartureDate,duration,airline,airline
        );
        String departureDate = CoreFunctions.convertToLongDate (flightPage.getDate("departureDate"));
        String returnDate = CoreFunctions.convertToLongDate (flightPage.getDate("returnDate"));

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Change currency");
        homePage.changeCurrency(currency,username);

        step("Open flights page");
        homePage.openVerticalPage("Flights");
        flightSearchPage.closePopup();

        step("Toggle trip type");
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

        step("Change date flight");
        flightSearchResultPage.changeDate(departureDate);

        step("Open & set airline filter");
        flightSearchResultPage.openFilter();
        flightSearchResultPage.filterAirline(airline);
        flightSearchResultPage.saveFilter();

        step("open and set transit filter");
        flightSearchResultPage.openTransitFilter();
        flightSearchResultPage.filterTransitType(transit);
        flightSearchResultPage.saveFilter();

        step("get route detail depart");
        String routeDepart = flightSearchResultPage.getRouteText();

        step("select first airline");
        flightSearchResultPage.selectFlight();

        step("verify logo, route and price breakdown on pdp depart");
        flightSearchResultPage.clickPricePolicyTab();
        Assertion.assertThat("is airline logo displayed", flightDescriptionPage.isAirlineLogoDisplayedPDP(),is(true));
        Assertion.assertThat("is route correct",flightDescriptionPage.isAirlineRouteCorrectPDP(routeDepart),is(true));
        Assertion.assertThat("is price breakdown correct",flightSearchResultPage.isPriceSumCorrect(),is(true));

        step("Select ticket");
        flightSearchResultPage.selectTicket();

        step("Change date flight return flight");
        flightSearchResultPage.changeDate(returnDate);

        step("Open & set airline filter");
        flightSearchResultPage.openFilter();
        flightSearchResultPage.filterAirline(airline);
        flightSearchResultPage.saveFilter();

        step("open and set transit filter");
        flightSearchResultPage.openTransitFilter();
        flightSearchResultPage.filterTransitType(transit);
        flightSearchResultPage.saveFilter();

        step("get route detail return");
        String routeReturn = flightSearchResultPage.getRouteText();

        step("select first airline");
        flightSearchResultPage.selectFlight();

        step("verify logo, route and price breakdown on pdp depart");
        flightSearchResultPage.clickPricePolicyTab();
        Assertion.assertThat("is airline logo displayed", flightDescriptionPage.isAirlineLogoDisplayedPDP(),is(true));
        Assertion.assertThat("is route correct",flightDescriptionPage.isAirlineRouteCorrectPDP(routeReturn),is(true));
        Assertion.assertThat("is price breakdown correct",flightSearchResultPage.isPriceSumCorrect(),is(true));

        step("Select ticket");
        flightSearchResultPage.selectTicket();

        step("verify airline logo, route and price breakdown correct on prp");
        Assertion.assertThat("is airline logo correct",flightProductReviewPage.isAirlineLogoDisplayedPRP(),is(true));
        Assertion.assertThat("is airline route correct",flightProductReviewPage.isAirlineRouteCorrectPRP(routeDepart),is(true));
        Assertion.assertThat("is price breakdown correct",flightProductReviewPage.verifyPriceBreakdownPRP(),is(true));

        step("move to booking page");
        flightProductReviewPage.continueToBookingForm(tripType);

        step("verify date format");
        flightBookingFormPage.verifyDateFormat(tripType);

        step("Fill passenger detail");
        flightBookingFormPage.fillPassengerDetail(adultPassengers,childPassengers,infantPassengers, flightRoute,airline);

        step("move to 2nd half of booking page");
        flightBookingFormPage.continueToSecondHalf();

        step("Add AddOnSeat");
        PriceDetail priceDetail = new PriceDetail();
        priceDetail.tripType=tripType;
        priceDetail.adult = adultPassengers;
        priceDetail.child = childPassengers;
        priceDetail.infant = infantPassengers;
        FlightSeatDetails flightSeatDetails = flightBookingFormPage.addSeatRoundTrip(priceDetail,currency);

        step("remove insurance/extra protection");
        flightBookingFormPage.removeExtraProtectionAll();

        step("open price detail");
        flightBookingFormPage.openPriceDetail();

        step("Verify price detail");
        priceDetail.flightSeatDetails = flightSeatDetails;
        Assertion.assertThat("Is price breakdown correct", flightBookingFormPage.isPriceBreakdownCorrect(priceDetail,currency), is(true));

        step("Continue to payment");
        flightBookingFormPage.continueToPayment();

        step("Skip full protection");
        flightBookingFormPage.skipFullProtection();

        step("Check order ID");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",!orderID.isBlank(),is(true));

        step("open and verify price detail payment page");
        paymentPage.openPriceDetail();
        Assertion.assertThat("Is price breakdown correct on payment page", paymentPage.isPriceBreakdownCorrect(), is(true));
        paymentPage.closePriceDetail();

        step("Open payment method for order id: "+orderID);
        paymentPage.openPaymentMethod();

        step("Select ATM as payment method");
        paymentPage.selectPaymentMethod("ATM");

        step("Submit payment");
        paymentPage.clickPayButton();

        step("Go to the order list");
        sleep(50);
        paymentPage.seeOrderList();

        step("open order and check order status");
        boolean orderEticketStatus = orderListPage.getOrderStatus(orderID,username,password);
        Assertion.assertThat("Order Status is eticket issue",orderEticketStatus,is(true));

        step("Find the issued order");
        orderListPage.openActiveTicketViaDeeplink(orderID);
        
        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.getETicketLabel(), either(is("Your e-ticket is here")).or(is("E-tiketmu di sini")));
    }

    @Author(names = {"Ashish"})
    @Module(name = "PaidOrderAncillaries")
    @Tribe(name = "NewAirlineOnBoarding")
    @Vertical(name = "Flight")
    @Test
    public void testLoginBookingFlightRoundTripTransitAddOnSeatIssueETicket() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testLoginBookingFlightRoundTripTransitAddOnSeatIssueETicket");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightDescriptionPage<WebDriver> flightDescriptionPage = FlightDescriptionPage.getInstance(session);
        FlightProductReviewPage<WebDriver> flightProductReviewPage = FlightProductReviewPage.getInstance(session);
        FlightBookingFormPage<WebDriver> flightBookingFormPage = FlightBookingFormPage.getInstance(session);
        FlightPage<WebDriver> flightPage = FlightPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);

        String tripType = param.get("tripType");
        String transit = param.get("transit");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");
        String flightRoute = param.get("flightRoute");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int duration = Integer.parseInt(param.get("duration"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        String airline = param.get("airline");

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");

        String currency = param.get("currency");

        step("Find dynamic data");
        flightPage.setDataRoundTrip(
                daysToDepartureDate,duration,airline,airline
        );
        String departureDate = CoreFunctions.convertToLongDate (flightPage.getDate("departureDate"));
        String returnDate = CoreFunctions.convertToLongDate (flightPage.getDate("returnDate"));

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Change currency");
        homePage.changeCurrency(currency,username);

        step("Open flights page");
        homePage.openVerticalPage("Flights");
        flightSearchPage.closePopup();

        step("Toggle trip type");
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

        step("Change date flight");
        flightSearchResultPage.changeDate(departureDate);

        step("Open & set airline filter");
        flightSearchResultPage.openFilter();
        flightSearchResultPage.filterAirline(airline);
        flightSearchResultPage.saveFilter();

        step("open and set transit filter");
        flightSearchResultPage.openTransitFilter();
        flightSearchResultPage.filterTransitType(transit);
        flightSearchResultPage.saveFilter();

        step("get route detail depart");
        String routeDepart = flightSearchResultPage.getRouteText();

        step("select first airline");
        flightSearchResultPage.selectFlight();

        step("verify logo, route and price breakdown on pdp depart");
        flightSearchResultPage.clickPricePolicyTab();
        Assertion.assertThat("is airline logo displayed", flightDescriptionPage.isAirlineLogoDisplayedPDP(),is(true));
        Assertion.assertThat("is route correct",flightDescriptionPage.isAirlineRouteCorrectPDP(routeDepart),is(true));
        Assertion.assertThat("is price breakdown correct",flightSearchResultPage.isPriceSumCorrect(),is(true));

        step("Select ticket");
        flightSearchResultPage.selectTicket();

        step("Change date flight return flight");
        flightSearchResultPage.changeDate(returnDate);

        step("Open & set airline filter");
        flightSearchResultPage.openFilter();
        flightSearchResultPage.filterAirline(airline);
        flightSearchResultPage.saveFilter();

        step("open and set transit filter");
        flightSearchResultPage.openTransitFilter();
        flightSearchResultPage.filterTransitType(transit);
        flightSearchResultPage.saveFilter();

        step("get route detail return");
        String routeReturn = flightSearchResultPage.getRouteText();

        step("select first airline");
        flightSearchResultPage.selectFlight();

        step("verify logo, route and price breakdown on pdp depart");
        flightSearchResultPage.clickPricePolicyTab();
        Assertion.assertThat("is airline logo displayed", flightDescriptionPage.isAirlineLogoDisplayedPDP(),is(true));
        Assertion.assertThat("is route correct",flightDescriptionPage.isAirlineRouteCorrectPDP(routeReturn),is(true));
        Assertion.assertThat("is price breakdown correct",flightSearchResultPage.isPriceSumCorrect(),is(true));

        step("Select ticket");
        flightSearchResultPage.selectTicket();

        step("verify airline logo, route and price breakdown correct on prp");
        Assertion.assertThat("is airline logo correct",flightProductReviewPage.isAirlineLogoDisplayedPRP(),is(true));
        Assertion.assertThat("is airline route correct",flightProductReviewPage.isAirlineRouteCorrectPRP(routeDepart),is(true));
        Assertion.assertThat("is price breakdown correct",flightProductReviewPage.verifyPriceBreakdownPRP(),is(true));

        step("move to booking page");
        flightProductReviewPage.continueToBookingForm(tripType);

        step("verify date format");
        flightBookingFormPage.verifyDateFormat(tripType);

        step("Fill passenger detail");
        flightBookingFormPage.fillPassengerDetail(adultPassengers,childPassengers,infantPassengers, flightRoute,airline);

        step("move to 2nd half of booking page");
        flightBookingFormPage.continueToSecondHalf();

        step("Add AddOnSeat");
        PriceDetail priceDetail = new PriceDetail();
        priceDetail.tripType=tripType;
        priceDetail.adult = adultPassengers;
        priceDetail.child = childPassengers;
        priceDetail.infant = infantPassengers;
        FlightSeatDetails flightSeatDetails = flightBookingFormPage.addSeatRoundTrip(priceDetail,currency);

        step("remove insurance/extra protection");
        flightBookingFormPage.removeExtraProtectionAll();

        step("open price detail");
        flightBookingFormPage.openPriceDetail();

        step("Verify price detail");
        priceDetail.flightSeatDetails = flightSeatDetails;
        Assertion.assertThat("Is price breakdown correct", flightBookingFormPage.isPriceBreakdownCorrect(priceDetail,currency), is(true));

        step("Continue to payment");
        flightBookingFormPage.continueToPayment();

        step("Skip full protection");
        flightBookingFormPage.skipFullProtection();

        step("Check order ID");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",!orderID.isBlank(),is(true));

        step("open and verify price detail payment page");
        paymentPage.openPriceDetail();
        Assertion.assertThat("Is price breakdown correct on payment page", paymentPage.isPriceBreakdownCorrect(), is(true));
        paymentPage.closePriceDetail();

        step("Open payment method for order id: "+orderID);
        paymentPage.openPaymentMethod();

        step("Select ATM as payment method");
        paymentPage.selectPaymentMethod("ATM");

        step("Submit payment");
        paymentPage.clickPayButton();

        step("Go to the order list");
        sleep(50);
        paymentPage.seeOrderList();

        step("open order and check order status");
        boolean orderEticketStatus = orderListPage.getOrderStatus(orderID,username,password);
        Assertion.assertThat("Order Status is eticket issue",orderEticketStatus,is(true));

        step("Find the issued order");
        orderListPage.openActiveTicketViaDeeplink(orderID);
        
        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.getETicketLabel(), either(is("Your e-ticket is here")).or(is("E-tiketmu di sini")));
    }

    @Author(names = {"Ashish"})
    @Module(name = "PaidOrderAncillaries")
    @Tribe(name = "NewAirlineOnBoarding")
    @Vertical(name = "Flight")
    @Test
    public void testLoginBookingFlightRoundTripStopOverAddOnSeatIssueETicket() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testLoginBookingFlightRoundTripStopOverAddOnSeatIssueETicket");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightSearchPage<WebDriver> flightSearchPage = FlightSearchPage.getInstance(session);
        FlightSearchResultPage<WebDriver> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        FlightDescriptionPage<WebDriver> flightDescriptionPage = FlightDescriptionPage.getInstance(session);
        FlightProductReviewPage<WebDriver> flightProductReviewPage = FlightProductReviewPage.getInstance(session);
        FlightBookingFormPage<WebDriver> flightBookingFormPage = FlightBookingFormPage.getInstance(session);
        FlightPage<WebDriver> flightPage = FlightPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);

        String tripType = param.get("tripType");
        String transit = param.get("transit");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");
        String flightRoute = param.get("flightRoute");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int duration = Integer.parseInt(param.get("duration"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        String airline = param.get("airline");

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");

        String currency = param.get("currency");

        step("Find dynamic data");
        flightPage.setDataRoundTrip(
                daysToDepartureDate,duration,airline,airline
        );
        String departureDate = CoreFunctions.convertToLongDate (flightPage.getDate("departureDate"));
        String returnDate = CoreFunctions.convertToLongDate (flightPage.getDate("returnDate"));

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Change currency");
        homePage.changeCurrency(currency,username);

        step("Open flights page");
        homePage.openVerticalPage("Flights");
        flightSearchPage.closePopup();

        step("Toggle trip type");
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

        step("Change date flight");
        flightSearchResultPage.changeDate(departureDate);

        step("Open & set airline filter");
        flightSearchResultPage.openFilter();
        flightSearchResultPage.filterAirline(airline);
        flightSearchResultPage.saveFilter();

        step("open and set transit filter");
        flightSearchResultPage.openTransitFilter();
        flightSearchResultPage.filterTransitType(transit);
        flightSearchResultPage.saveFilter();

        step("get route detail depart");
        String routeDepart = flightSearchResultPage.getRouteText();

        step("select stop over airline");
        flightSearchResultPage.selectFlightStopOver();

        step("verify stop over on pdp");
        Assertion.assertThat("is stopover flight",flightDescriptionPage.verifyStopOver(),is(true));

        step("verify logo, route and price breakdown on pdp depart");
        flightSearchResultPage.clickPricePolicyTab();
        Assertion.assertThat("is airline logo displayed", flightDescriptionPage.isAirlineLogoDisplayedPDP(),is(true));
        Assertion.assertThat("is route correct",flightDescriptionPage.isAirlineRouteCorrectPDP(routeDepart),is(true));
        Assertion.assertThat("is price breakdown correct",flightSearchResultPage.isPriceSumCorrect(),is(true));

        step("Select ticket");
        flightSearchResultPage.selectTicket();

        step("Change date flight return flight");
        flightSearchResultPage.changeDate(returnDate);

        step("Open & set airline filter");
        flightSearchResultPage.openFilter();
        flightSearchResultPage.filterAirline(airline);
        flightSearchResultPage.saveFilter();

        step("open and set transit filter");
        flightSearchResultPage.openTransitFilter();
        flightSearchResultPage.filterTransitType(transit);
        flightSearchResultPage.saveFilter();

        step("get route detail return");
        String routeReturn = flightSearchResultPage.getRouteText();

        step("select stop over airline");
        flightSearchResultPage.selectFlightStopOver();

        step("verify stop over on pdp");
        Assertion.assertThat("is stopover flight",flightDescriptionPage.verifyStopOver(),is(true));

        step("verify logo, route and price breakdown on pdp depart");
        flightSearchResultPage.clickPricePolicyTab();
        Assertion.assertThat("is airline logo displayed", flightDescriptionPage.isAirlineLogoDisplayedPDP(),is(true));
        Assertion.assertThat("is route correct",flightDescriptionPage.isAirlineRouteCorrectPDP(routeReturn),is(true));
        Assertion.assertThat("is price breakdown correct",flightSearchResultPage.isPriceSumCorrect(),is(true));

        step("Select ticket");
        flightSearchResultPage.selectTicket();

        step("verify airline logo, route and price breakdown correct on prp");
        Assertion.assertThat("is airline logo correct",flightProductReviewPage.isAirlineLogoDisplayedPRP(),is(true));
        Assertion.assertThat("is airline route correct",flightProductReviewPage.isAirlineRouteCorrectPRP(routeDepart),is(true));
        Assertion.assertThat("is price breakdown correct",flightProductReviewPage.verifyPriceBreakdownPRP(),is(true));

        step("move to booking page");
        flightProductReviewPage.continueToBookingForm(tripType);

        step("verify date format");
        flightBookingFormPage.verifyDateFormat(tripType);

        step("Fill passenger detail");
        flightBookingFormPage.fillPassengerDetail(adultPassengers,childPassengers,infantPassengers, flightRoute,airline);

        step("move to 2nd half of booking page");
        flightBookingFormPage.continueToSecondHalf();

        step("Add AddOnSeat");
        PriceDetail priceDetail = new PriceDetail();
        priceDetail.tripType=tripType;
        priceDetail.adult = adultPassengers;
        priceDetail.child = childPassengers;
        priceDetail.infant = infantPassengers;
        FlightSeatDetails flightSeatDetails = flightBookingFormPage.addSeatRoundTrip(priceDetail,currency);

        step("remove insurance/extra protection");
        flightBookingFormPage.removeExtraProtectionAll();

        step("open price detail");
        flightBookingFormPage.openPriceDetail();

        step("Verify price detail");
        priceDetail.flightSeatDetails = flightSeatDetails;
        Assertion.assertThat("Is price breakdown correct", flightBookingFormPage.isPriceBreakdownCorrect(priceDetail,currency), is(true));

        step("Continue to payment");
        flightBookingFormPage.continueToPayment();

        step("Skip full protection");
        flightBookingFormPage.skipFullProtection();

        step("Check order ID");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",!orderID.isBlank(),is(true));

        step("open and verify price detail payment page");
        paymentPage.openPriceDetail();
        Assertion.assertThat("Is price breakdown correct on payment page", paymentPage.isPriceBreakdownCorrect(), is(true));
        paymentPage.closePriceDetail();

        step("Open payment method for order id: "+orderID);
        paymentPage.openPaymentMethod();

        step("Select ATM as payment method");
        paymentPage.selectPaymentMethod("ATM");

        step("Submit payment");
        paymentPage.clickPayButton();

        step("Go to the order list");
        sleep(50);
        paymentPage.seeOrderList();

        step("open order and check order status");
        boolean orderEticketStatus = orderListPage.getOrderStatus(orderID,username,password);
        Assertion.assertThat("Order Status is eticket issue",orderEticketStatus,is(true));

        step("Find the issued order");
        orderListPage.openActiveTicketViaDeeplink(orderID);
        
        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.getETicketLabel(), either(is("Your e-ticket is here")).or(is("E-tiketmu di sini")));
    }
}
