package com.tiket.test.platform.sphinx.insurance;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.apiPage.FlightApi;
import com.tiket.apiPage.Unm;
import com.tiket.model.Environment;
import com.tiket.model.Session;
import com.tiket.page.flight.FlightPage;
import com.tiket.page.claimcenter.ClaimCenterPage;
import com.tiket.page.home.HomePage;
import com.tiket.page.insurance.InsurancePage;
import com.tiket.page.insurance.standalone.StandaloneSrp;
import com.tiket.page.login.LoginPage;
import com.tiket.page.order.orderdetail.OrderDetailPage;
import com.tiket.page.order.orderlist.OrderListPage;
import com.tiket.pojo.flight.FlightDynamicData;
import com.tiket.pojo.order.MapVerticalWithOrderId;
import com.tiket.pojo.unm.LoginDataUnmResponse;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.testbase.Configs;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Map;

import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.Matchers.either;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;

public class InsuranceTest extends BaseTest{
    @Author(names = {"Prerna"})
    @Module(name = "INSURANCE (New)")
    @Tribe(name = "Insurance")
    @Vertical(name = "Platform")
    @Test
    public void testAccessingInsuranceClaimLandingPageLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testAccessingInsuranceClaimLandingPageLogin");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        InsurancePage<WebDriver> insurancePage = InsurancePage.getInstance(session);
        ClaimCenterPage<WebDriver> claimCenterPage = ClaimCenterPage.getInstance(session);

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
        String language  = homePage.getLanguageApp();

        step("Open Insurance Claim Landing Page");
        homePage.openVerticalPageDeeplink("insurance", language, "");
        insurancePage.removeWelcomePopup();
        insurancePage.openClaimCenterPage();

        step("verify we are on claim center page");
        claimCenterPage.removeWelcomePopup();
        Assertion.assertThat("is on landing page of Insurance Claim Landing Page",claimCenterPage.isOnInsuranceClaimLandingPage(true),is(true));
    }

    @Author(names = {"Ashish"})
    @Module(name = "INSURANCE (New)")
    @Tribe(name = "Insurance")
    @Vertical(name = "Platform")
    @Test
    public void testAccessingInsuranceClaimLandingPageGuest() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testAccessingInsuranceClaimLandingPageGuest");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        InsurancePage<WebDriver> insurancePage = InsurancePage.getInstance(session);
        ClaimCenterPage<WebDriver> claimCenterPage = ClaimCenterPage.getInstance(session);

        String currency = param.get("currency");

        step("user open app");
        homePage.gotoHome();

        step("user login as guest");
        loginPage.loginAsGuest();

        step("Change currency");
        homePage.changeCurrency(currency,"");
        String language  = homePage.getLanguageApp();

        step("Open Insurance Claim Landing Page");
        homePage.openVerticalPageDeeplink("insurance", language, "");
        insurancePage.removeWelcomePopup();
        insurancePage.openClaimCenterPage();

        step("verify we are on claim center page");
        claimCenterPage.removeWelcomePopup();
        Assertion.assertThat("is on landing page of Insurance Claim Landing Page",claimCenterPage.isOnInsuranceClaimLandingPage(false),is(true));
    }

    @Author(names = {"Ashish","Shubham Bansal"})
    @Module(name = "standaloneInsurance")
    @Tribe(name = "Insurance")
    @Vertical(name = "Platform")
    @Test
    public void testAccessingStandalonePageViaYourOrderPageLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testAccessingStandalonePageViaYourOrderPageLogin");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FlightPage<WebDriver> flightPage = FlightPage.getInstance(session);
        FlightApi<WebDriver> flightApi = new FlightApi<>(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        StandaloneSrp<WebDriver> standaloneSrp = StandaloneSrp.getInstance(session);

        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");
        String airline = param.get("airline");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));

        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));

        String username = param.get("username");
        String password = param.get("password");
        String currency = param.get("currency");
        String firstName = param.get("firstName");
        String lastName = param.get("lastName");
        String phoneNumber = param.get("phoneNumber");
        String[] passengerNames = {firstName + " " + lastName};

        step("Get api access token");
        Unm<?> unm = new Unm<>(session);
        LoginDataUnmResponse.Data data = unm.unmLoginApi(username, password);
        String apiAccessToken = data.accessToken;

        step("check order present in order");
        MapVerticalWithOrderId.TitleAndOrderId activeOrderData = orderListPage.getLatestActiveFlightOrderWithEticketIssuedAndDepartureAfterTwoMonths(apiAccessToken);

        String bookedOrderID = "";
        if (activeOrderData == null) {
            step("Find dynamic flight data (origin/destination/date/airline)");
            FlightDynamicData flightDynamicData = flightApi.findDate(
                    username, password,
                    originAirport, destinationAirport, daysToDepartureDate,
                    adultPassengers, childPassengers, infantPassengers,
                    tripType, airline, "", "", "", ""
            );
            originAirport = flightDynamicData.originAirport;
            destinationAirport = flightDynamicData.destinationAirport;
            String departureDate = flightDynamicData.departureDate;
            airline = flightDynamicData.airlineDepart;

            if (Configs.ENVIRONMENT == Environment.PREPROD) {
                step("Validate all suppliers are mock-issued");
                flightApi.validateAllSuppliersAreMockIssuedByAirline(flightDynamicData.airlineCodeDepart, flightDynamicData.originAirport, flightDynamicData.destinationAirport);
            }

            step("complete flight booking via API");
            bookedOrderID = flightPage.createOrderFlightB2CDomestic(username, password, originAirport, destinationAirport, departureDate, tripType, adultPassengers, childPassengers, infantPassengers, airline, passengerNames, phoneNumber);
            Assertion.assertThat("Flight order created", bookedOrderID, not(""));
        } else {
            bookedOrderID = activeOrderData.orderId;
        }

        step("user open app");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink and change currency to " + currency);
        homePage.loginWithSpecificAccountViaDeeplink(username, password, currency);

        if (activeOrderData == null) {
            step("get booking code from API");
            String bookingCode = flightApi.getBookingCode(bookedOrderID, username);

            step("set Tiket Number on flight dashboard and ticket will be eticket issued");
            Boolean isTicketIssued = flightApi.flightEticketIssuedRequestMultipleGuest(bookedOrderID, bookingCode, username, Arrays.asList(passengerNames), 15);
            Assertion.assertThat("is Eticket Issued", isTicketIssued, is(true));
        }

        step("Find the issued order");
        orderListPage.openIssuedTicketViaDeeplink(bookedOrderID);
        Assertion.assertThat("OrderStatus is E-ticket issued", orderDetailPage.getETicketLabel(), either(is("Your e-ticket is here")).or(is("E-tiketmu di sini")));

        step("swipe to standalone section");
        boolean isSwiped = orderDetailPage.swipeToStandAloneSection();
        Assertion.assertThat("is swiped to standalone section", isSwiped, is(true));

        step("get price of standalone insurance");
        String insurancePrice = orderDetailPage.getPriceOnStandAloneSection();

        step("click add btn standalone section");
        orderDetailPage.clickAddOnStandAloneSection();

        step("verify we are redirected to standalone srp page");
        Assertion.assertThat("is on standalone srp page",standaloneSrp.isOnStandaloneSrpPage(),is(true));

        step("verify price detail on standalone srp page");
        standaloneSrp.openPriceDetail();
        Assertion.assertThat("is price detail correct",standaloneSrp.isPriceDetailCorrect(insurancePrice),is(true));
    }

    @Author(names = {"Ashish"})
    @Module(name = "INSURANCE (New)")
    @Tribe(name = "Insurance")
    @Vertical(name = "Platform")
    @Test
    public void testAccessingStandalonePageViaHomepageGuest() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testAccessingStandalonePageViaHomepageGuest");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        InsurancePage<WebDriver> insurancePage = InsurancePage.getInstance(session);
        String currency = param.get("currency");
        String username = param.get("username");
        String password = param.get("password");

        step("user open app");
        homePage.gotoHome();

        step("Logout via deeplink");
        homePage.logoutViaDeeplink();

        step("Change currency");
        homePage.changeCurrency(currency,"");
        sleep(20);

        step("Open Insurance Claim Landing Page");
        homePage.openVerticalPageDeeplink("insurance","",currency);
        insurancePage.removeWelcomePopup();
        Assertion.assertThat("is on landing page of Insurance Landing Page",insurancePage.isOnInsuranceLandingPage(false),is(true));

        step("click login btn");
        insurancePage.clickLoginBtn();
        loginPage.loginFromDifferentPages(username,password);

        step("verify we are on insurance landing page with my order header");
        insurancePage.removeWelcomePopup();
        Assertion.assertThat("is on landing page of Insurance Landing Page with my order header",insurancePage.isOnInsuranceLandingPage(true),is(true));

        step("check flight standalone order is displayed");
        Assertion.assertThat("is add btn on standalone insurance displayed",insurancePage.isStandAloneInsuranceAddBtnDisplayed(),is(true));
    }


}
