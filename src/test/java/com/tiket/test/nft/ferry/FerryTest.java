package com.tiket.test.nft.ferry;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.apiPage.FerryApi;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Environment;
import com.tiket.model.Session;
import com.tiket.page.ferry.FerryBookingPage.FerryBookingPage;
import com.tiket.page.ferry.FerryPage;
import com.tiket.page.ferry.FerrySearchPage.FerrySearchPage;
import com.tiket.page.ferry.FerrySearchresultPage.FerrySearchResultPage;
import com.tiket.page.home.HomePage;
import com.tiket.page.login.LoginPage;
import com.tiket.page.order.orderdetail.OrderDetailPage;
import com.tiket.page.order.orderlist.OrderListPage;
import com.tiket.page.payment.PaymentPage;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.testbase.Configs;
import org.hamcrest.core.Every;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.in;
import static org.hamcrest.core.Is.is;

public class FerryTest extends BaseTest {

    @Author(names = {"Kartik"})
    @Module(name = "HomePageWeb")
    @Tribe(name = "Ferry")
    @Vertical(name = "GroundTransport")
    @Test
    public void testVerifyHomePageWithoutFerryNonIDRLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testVerifyHomePageWithoutFerryNonIDRLogin");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);

        String loginMethod = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String currency = param.get("currency");

        step("user open app");
        homePage.gotoHome();

        step("user login using " + username + " account");
        loginPage.loginWith(loginMethod, username, password);

        step("Change currency");
        homePage.changeCurrency(currency,username);

        step("Find Ferry Vertical");
        Assertion.assertThat("is Ferry not displayed in non-IDR currency {" + currency + "}",
                homePage.checkVerticalPresence("Ferry"), is(true));
    }

    @Author(names = {"Kartik"})
    @Module(name = "HomePageWeb")
    @Tribe(name = "Ferry")
    @Vertical(name = "GroundTransport")
    @Test
    public void testVerifyHomePageWithoutFerryNonIDRGuest() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testVerifyHomePageWithoutFerryNonIDRGuest");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);

        String currency = param.get("currency");

        step("user open app");
        homePage.gotoHome();

        step("Login as guest");
        loginPage.loginAsGuest();

        step("Change currency");
        homePage.changeCurrency(currency,"");

        step("Find Ferry Vertical");
        Assertion.assertThat("is Ferry not displayed in non-IDR currency {" + currency + "}",
                homePage.checkVerticalPresence("Ferry"), is(true));
    }

    @Author(names = {"Dendi"})
    @Module(name = "FerryViaDeeplink")
    @Tribe(name = "Ferry")
    @Vertical(name = "GroundTransport")
    @Test
    public void testLoginBookFerryViaDeeplink() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testLoginBookFerryViaDeeplink");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        FerryApi<?> ferryApi = new FerryApi<>(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FerrySearchPage<WebDriver> ferrySearchPage = FerrySearchPage.getInstance(session);
        FerrySearchResultPage<WebDriver> ferrySearchResultPage = FerrySearchResultPage.getInstance(session);
        FerryBookingPage<WebDriver> ferryBookingPage = FerryBookingPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);

        String loginMethod = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String currency = param.get("currency");
        String originPort = param.get("originPort");
        String destinationPort = param.get("destinationPort");
        String ticketType = param.get("ticketType");
        int adult = Integer.parseInt(param.get("adult"));
        int child = 0, infant = 0, elder = 0;

        step("Get available date from API");
        String departureDate = ferryApi.getFerryAvailableDate(originPort, destinationPort, ticketType, adult, 0,0,0);

        step("user open app");
        homePage.gotoHome();

        step("user login using " + username + " account");
        loginPage.loginWith(loginMethod, username, password);

        step("change currency to default IDR");
        homePage.changeCurrency(currency,username);

        step("Open Ferry via deeplink");
        homePage.openVerticalPageDeeplink("ferry", "", "");

        step("Skip coachmark");
        ferrySearchPage.skipCoachMark();
        ferrySearchPage.allowLocationPopup();

        step("input origin port");
        ferrySearchPage.inputOrigin(originPort);

        step("input destionation port");
        ferrySearchPage.inputDestination(destinationPort);

        step("Input ticket type");
        ferrySearchPage.selectTicketType(ticketType);

        step("User input pax qty");
        ferrySearchPage.openTicketQty();
        ferrySearchPage.resetTicketQty();
        ferrySearchPage.setTicketQty("adult", adult);
        ferrySearchPage.saveTicketQty();

        step("Departure date");
        ferrySearchPage.selectDate(departureDate);

        step("submit search");
        ferrySearchPage.submitSearch();

        step("Check if ferry is available");
        boolean isFerryAvailable = ferrySearchResultPage.isFerryCardDisplayed();
        if (isFerryAvailable){
            ferrySearchResultPage.clickChangeButton();

            step("Enter new departure date");
            departureDate = param.get("daysToDepart");
            ferrySearchPage.selectDate(departureDate);

            step("Again submit search");
            ferrySearchPage.submitSearch();

            isFerryAvailable = ferrySearchResultPage.isFerryCardDisplayed();
        }
        Assertion.assertThat("Ferry card is displayed", isFerryAvailable, is(false));

        step("Skip information coachmark in srp");
        ferrySearchResultPage.skipCoachMark();

        step("search ferry schedule");
        Assertion.assertThat("Verify price in srp", ferrySearchResultPage.verifyPriceInSrpIsInIDR(), is(true));

        step("Check one fleet price detail");
        Assertion.assertThat("Verify price detail", ferrySearchResultPage.verifyFirstFleetPriceDetailIDR(), is(true));

        step("Check booking form price detail");
        ferrySearchResultPage.chooseFirstFleet();

        step("Input passenger detail");
        ferryBookingPage.fillGuestsAndVaccineDetails("true", String.valueOf(CoreFunctions.generateRandomInt(16)), 1, 0);

        step("Verify total price is in IDR");
        Assertion.assertThat("Verify total price is in IDR", ferryBookingPage.verifTotalPriceIsInIdr(), is(true));

        step("Fill plate number if required");
        ferryBookingPage.fillLicensePlateNumber();

        step("Verify price detail");
        Assertion.assertThat("Verify price detail is indr", ferryBookingPage.verifyPriceDetailIsInIDR(), is(true));

        step("Check term and condition");
        ferryBookingPage.clickTncCheckBox();

        step("continue to payment page");
        ferryBookingPage.submitBooking();
        ferryBookingPage.setLocationService();

        step("verify order id is received");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id", !orderID.isEmpty(), is(true));

        step("Verify price in payment page");
        paymentPage.openPriceDetail();

        Assertion.assertThat("is price detail payment page in IDR", paymentPage.getPriceDetail().stream().filter(item -> item.contains("IDR")).collect(Collectors.toList()),
                Every.everyItem(containsString("IDR")));
        Assertion.assertThat("is price total payment page in IDR",paymentPage.getTotalPrice(), containsString("IDR"));
    }

    @Author(names = {"Dendi"})
    @Module(name = "FerryViaDeeplink")
    @Tribe(name = "Ferry")
    @Vertical(name = "GroundTransport")
    @Test
    public void testGuestBookFerryViaDeeplink() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testGuestBookFerryViaDeeplink");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        FerryApi<?> ferryApi = new FerryApi<>(session);
        FerrySearchPage<WebDriver> ferrySearchPage = FerrySearchPage.getInstance(session);
        FerrySearchResultPage<WebDriver> ferrySearchResultPage = FerrySearchResultPage.getInstance(session);
        FerryBookingPage<WebDriver> ferryBookingPage = FerryBookingPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);

        String username = param.get("username");
        String fullName = CoreFunctions.randomName();
        String phoneNumber = param.get("phoneNumber");
        String currency = param.get("currency");
        String originPort = param.get("originPort");
        String destinationPort = param.get("destinationPort");
        String ticketType = param.get("ticketType");
        String VaccineVerifiedID = param.get("VaccineVerifiedID");
        int adult = Integer.parseInt(param.get("adult"));
        int child = 0, infant = 0, elder = 0;

        step("Get available date from API");
        String departureDate = ferryApi.getFerryAvailableDate(originPort, destinationPort, ticketType, adult, 0,0,0);

        step("user open app");
        homePage.gotoHome();

        step("user login using as guest account");
        loginPage.loginAsGuest();

        step("change currency to default IDR");
        homePage.changeCurrency(currency,"");

        step("Open Ferry via deeplink");
        homePage.openVerticalPageDeeplink("ferry", "", "");

        step("Skip coachmark");
        ferrySearchPage.skipCoachMark();
        ferrySearchPage.allowLocationPopup();

        step("input origin port");
        ferrySearchPage.inputOrigin(originPort);

        step("input destination port");
        ferrySearchPage.inputDestination(destinationPort);

        step("Input ticket type");
        ferrySearchPage.selectTicketType(ticketType);

        step("User input pax qty");
        ferrySearchPage.openTicketQty();
        ferrySearchPage.resetTicketQty();
        ferrySearchPage.setTicketQty("adult", adult);
        ferrySearchPage.saveTicketQty();

        step("Departure date");
        ferrySearchPage.selectDate(departureDate);

        step("submit search");
        ferrySearchPage.submitSearch();

        step("Check if ferry is available");
        boolean isFerryAvailable = ferrySearchResultPage.isFerryCardDisplayed();
        Assertion.assertThat("Ferry card is displayed", isFerryAvailable, is(false));

        step("Skip information coachmark in srp");
        ferrySearchResultPage.skipCoachMark();

        step("search ferry schedule");
        Assertion.assertThat("Verify price in srp", ferrySearchResultPage.verifyPriceInSrpIsInIDR(), is(true));

        step("Check one fleet price detail");
        Assertion.assertThat("Verify price detail", ferrySearchResultPage.verifyFirstFleetPriceDetailIDR(), is(true));

        step("Check booking form price detail");
        ferrySearchResultPage.chooseFirstFleet();

        step("Input contact detail");
        ferryBookingPage.fillGuestContactDetail(username, fullName, phoneNumber);

        step("Input passenger detail");
        ferryBookingPage.fillGuestsAndVaccineDetails("true", String.valueOf(CoreFunctions.generateRandomInt(16)), 1, 0);

        step("Verify total price is in IDR");
        Assertion.assertThat("Verify total price is in IDR", ferryBookingPage.verifTotalPriceIsInIdr(), is(true));

        step("Fill plate number if required");
        ferryBookingPage.fillLicensePlateNumber();

        step("Verify price detail");
        Assertion.assertThat("Verify price detail is in IDR", ferryBookingPage.verifyPriceDetailIsInIDR(), is(true));

        step("Check term and condition");
        ferryBookingPage.clickTncCheckBox();

        step("continue to payment page");
        ferryBookingPage.submitBooking();
        ferryBookingPage.setLocationService();

        step("verify order id is received");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id", !orderID.isEmpty(), is(true));

        step("Verify price in payment page");
        paymentPage.openPriceDetail();

        Assertion.assertThat("is price detail payment page in IDR", paymentPage.getPriceDetail().stream().filter(item -> item.contains("IDR")).collect(Collectors.toList()),
                Every.everyItem(containsString("IDR")));
        Assertion.assertThat("is price total payment page in IDR", paymentPage.getTotalPrice(), containsString("IDR"));
        Assertion.assertThat("is price detail payment page in IDR", paymentPage.getPriceDetail().stream().filter(item -> item.contains("IDR")).collect(Collectors.toList()),
                Every.everyItem(containsString("IDR")));
    }

    @Author(names = {"Akbar"})
    @Module(name = "SRP")
    @Tribe(name = "Ferry")
    @Vertical(name = "GroundTransport")
    @Test
    public void testSeePriceDetailEachFerrySRPCardGuest() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testSeePriceDetailEachFerrySRPCardGuest");

        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        FerryApi<?> ferryApi = new FerryApi<>(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FerrySearchPage<WebDriver> ferrySearchPage = FerrySearchPage.getInstance(session);
        FerrySearchResultPage<WebDriver> ferrySearchResultPage = FerrySearchResultPage.getInstance(session);

        String origin = param.get("origin");
        String destination = param.get("destination");
        String ticketType = param.get("ticketType");
        int adult = Integer.parseInt(param.get("adult"));
        int child = Integer.parseInt(param.get("child"));
        int infant = Integer.parseInt(param.get("infant"));
        int elder = Integer.parseInt(param.get("elder"));

        step("Get available date from API");
        String departureDate = ferryApi.getFerryAvailableDate(origin, destination, ticketType, adult, child,0,0);

        step("user open app");
        homePage.gotoHome();

        step("Login as guest");
        loginPage.loginAsGuest();

        if(Configs.ENVIRONMENT == Environment.PROD) {
            step("Change currency to 'IDR'");
            homePage.changeCurrency("IDR","");
        }

        step("Get App Language");
        String language = homePage.getLanguageApp();

        step("Open Ferry Landing Page");
        homePage.openVerticalPageDeeplink("Ferry", language, "IDR");

        step("User select depart port and arrival port");
        ferrySearchPage.skipCoachMark();
        ferrySearchPage.allowLocationPopup();
        ferrySearchPage.inputOrigin(origin);
        ferrySearchPage.inputDestination(destination);

        step("User set travel date & time");
        ferrySearchPage.selectDate(departureDate);

        step("User select ticket type");
        ferrySearchPage.selectTicketType(ticketType);

        step("User input pax qty");
        ferrySearchPage.openTicketQty();
        ferrySearchPage.setTicketQty("adult", adult);
        ferrySearchPage.setTicketQty("child", child);
        ferrySearchPage.setTicketQty("infant", infant);
        ferrySearchPage.setTicketQty("elder", elder);
        ferrySearchPage.saveTicketQty();

        step("User tap on search ferry button");
        ferrySearchPage.submitSearch();

        step("Check if ferry is available");
        boolean isFerryAvailable = ferrySearchResultPage.isFerryCardDisplayed();
        Assertion.assertThat("Ferry card is displayed", isFerryAvailable, is(false));
        ferrySearchResultPage.skipCoachMark();

        step("Verify step");
        Assertion.assertThat("Each card on ferry SRP is showing correct bottomsheet price detail and class name", ferrySearchResultPage.verifyAllFerryCardPriceDetailBottomSheet(), is(true));
    }

    @Author(names = {"Akbar"})
    @Module(name = "SRP")
    @Tribe(name = "Ferry")
    @Vertical(name = "GroundTransport")
    @Test
    public void testSeePriceDetailEachFerrySRPCardLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testSeePriceDetailEachFerrySRPCardLogin");

        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        FerryApi<?> ferryApi = new FerryApi<>(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FerrySearchPage<WebDriver> ferrySearchPage = FerrySearchPage.getInstance(session);
        FerrySearchResultPage<WebDriver> ferrySearchResultPage = FerrySearchResultPage.getInstance(session);

        String loginMethod = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");

        String origin = param.get("origin");
        String destination = param.get("destination");
        String ticketType = param.get("ticketType");
        int adult = Integer.parseInt(param.get("adult"));
        int child = Integer.parseInt(param.get("child"));
        int infant = Integer.parseInt(param.get("infant"));
        int elder = Integer.parseInt(param.get("elder"));

        step("Get available date from API");
        String departureDate = ferryApi.getFerryAvailableDate(origin, destination, ticketType, adult, child, infant, elder);

        step("user open app");
        homePage.gotoHome();

        step("user login using " + username + " account");
        loginPage.loginWith(loginMethod, username, password);

        if(Configs.ENVIRONMENT == Environment.PROD) {
            step("Change currency to 'IDR'");
            homePage.changeCurrency("IDR",username);
        }

        step("Get App Language");
        String language = homePage.getLanguageApp();

        step("Open Ferry Landing Page");
        homePage.openVerticalPageDeeplink("Ferry", language, "IDR");

        step("User select depart port and arrival port");
        ferrySearchPage.skipCoachMark();
        ferrySearchPage.allowLocationPopup();
        ferrySearchPage.inputOrigin(origin);
        ferrySearchPage.inputDestination(destination);

        step("User set travel date & time");
        ferrySearchPage.selectDate(departureDate);

        step("User select ticket type");
        ferrySearchPage.selectTicketType(ticketType);

        step("User input pax qty");
        ferrySearchPage.openTicketQty();
        ferrySearchPage.setTicketQty("adult", adult);
        ferrySearchPage.setTicketQty("child", child);
        ferrySearchPage.setTicketQty("infant", infant);
        ferrySearchPage.setTicketQty("elder", elder);
        ferrySearchPage.saveTicketQty();

        step("User tap on search ferry button");
        ferrySearchPage.submitSearch();

        step("Check if ferry is available");
        boolean isFerryAvailable = ferrySearchResultPage.isFerryCardDisplayed();
        Assertion.assertThat("Ferry card is displayed", isFerryAvailable, is(false));
        ferrySearchResultPage.skipCoachMark();

        step("Verify step");
        Assertion.assertThat("Each card on ferry SRP is showing correct bottomsheet price detail and class name", ferrySearchResultPage.verifyAllFerryCardPriceDetailBottomSheet(), is(true));
    }

    @Author(names = {"Kartik"})
    @Module(name = "SRP")
    @Tribe(name = "Ferry")
    @Vertical(name = "GroundTransport")
    @Test
    public void testUserOpenImageOnFerrySRPLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testUserOpenImageOnFerrySRPLogin");

        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        FerryApi<?> ferryApi = new FerryApi<>(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FerrySearchPage<WebDriver> ferrySearchPage = FerrySearchPage.getInstance(session);
        FerrySearchResultPage<WebDriver> ferrySearchResultPage = FerrySearchResultPage.getInstance(session);

        String loginMethod = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");

        String origin = param.get("origin");
        String destination = param.get("destination");
        String ticketType = param.get("ticketType");
        int adult = Integer.parseInt(param.get("adult"));
        int child = 0, infant = 0, elder = 0;

        step("Get available date from API");
        String departureDate = ferryApi.getFerryAvailableDate(origin, destination, ticketType, adult, 0,0,0);

        step("user open app");
        homePage.gotoHome();

        step("user login using " + username + " account");
        loginPage.loginWith(loginMethod, username, password);

        if(Configs.ENVIRONMENT == Environment.PROD) {
            step("Change currency to 'IDR'");
            homePage.changeCurrency("IDR",username);
        }

        step("Get App Language");
        String language = homePage.getLanguageApp();

        step("Open Ferry Landing Page");
        homePage.openVerticalPageDeeplink("Ferry", language, "IDR");

        step("User select depart port and arrival port");
        ferrySearchPage.skipCoachMark();
        ferrySearchPage.allowLocationPopup();
        ferrySearchPage.inputOrigin(origin);
        ferrySearchPage.inputDestination(destination);

        step("User set travel date & time");
        ferrySearchPage.selectDate(departureDate);

        step("User select ticket type");
        ferrySearchPage.selectTicketType(ticketType);

        step("User input pax qty");
        ferrySearchPage.openTicketQty();
        ferrySearchPage.resetTicketQty();
        ferrySearchPage.setTicketQty("adult", adult);
        ferrySearchPage.saveTicketQty();

        step("User tap on search ferry button");
        ferrySearchPage.submitSearch();

        step("Check if ferry is available");
        boolean isFerryAvailable = ferrySearchResultPage.isFerryCardDisplayed();
        Assertion.assertThat("Ferry card is displayed", isFerryAvailable, is(false));
        ferrySearchResultPage.skipCoachMark();

        step("User tap on Image ferry Card on SRP");
        ferrySearchResultPage.ClickFerryImageSRP();

        step("Verify step");
        Assertion.assertThat("User redirect to image page and can access image contents", ferrySearchResultPage.verifyImageContents(), is(true));
    }

    @Author(names = {"Kartik"})
    @Module(name = "SRP")
    @Tribe(name = "Ferry")
    @Vertical(name = "GroundTransport")
    @Test
    public void testUserOpenImageOnFerrySRPGuest() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testUserOpenImageOnFerrySRPGuest");

        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        FerryApi<?> ferryApi = new FerryApi<>(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FerrySearchPage<WebDriver> ferrySearchPage = FerrySearchPage.getInstance(session);
        FerrySearchResultPage<WebDriver> ferrySearchResultPage = FerrySearchResultPage.getInstance(session);

        String origin = param.get("origin");
        String destination = param.get("destination");
        String ticketType = param.get("ticketType");
        int adult = Integer.parseInt(param.get("adult"));
        int child = 0, infant = 0, elder = 0;

        step("Get available date from API");
        String departureDate = ferryApi.getFerryAvailableDate(origin, destination, ticketType, adult, 0,0,0);

        step("user open app");
        homePage.gotoHome();

        step("Login as guest");
        loginPage.loginAsGuest();

        if(Configs.ENVIRONMENT == Environment.PROD) {
            step("Change currency to 'IDR'");
            homePage.changeCurrency("IDR","");
        }

        step("Get App Language");
        String language = homePage.getLanguageApp();

        step("Open Ferry Landing Page");
        homePage.openVerticalPageDeeplink("Ferry", language, "IDR");

        step("User select depart port and arrival port");
        ferrySearchPage.skipCoachMark();
        ferrySearchPage.allowLocationPopup();
        ferrySearchPage.inputOrigin(origin);
        ferrySearchPage.inputDestination(destination);

        step("User set travel date & time");
        ferrySearchPage.selectDate(departureDate);

        step("User select ticket type");
        ferrySearchPage.selectTicketType(ticketType);

        step("User input pax qty");
        ferrySearchPage.openTicketQty();
        ferrySearchPage.resetTicketQty();
        ferrySearchPage.setTicketQty("adult", adult);
        ferrySearchPage.saveTicketQty();

        step("User tap on search ferry button");
        ferrySearchPage.submitSearch();

        step("Check if ferry is available");
        boolean isFerryAvailable = ferrySearchResultPage.isFerryCardDisplayed();
        Assertion.assertThat("Ferry card is displayed", isFerryAvailable, is(false));
        ferrySearchResultPage.skipCoachMark();

        step("User tap on Image ferry Card on SRP");
        ferrySearchResultPage.ClickFerryImageSRP();

        step("Verify step");
        Assertion.assertThat("User redirect to image page and can access image contents", ferrySearchResultPage.verifyImageContents(), is(true));
    }

    @Author(names = {"Kartik"})
    @Module(name = "SRP")
    @Tribe(name = "Ferry")
    @Vertical(name = "GroundTransport")
    @Test
    public void testSeeFerryVehicleTNConBookingForm() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testSeeFerryVehicleTNConBookingForm");

        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        FerryApi<?> ferryApi = new FerryApi<>(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FerrySearchPage<WebDriver> ferrySearchPage = FerrySearchPage.getInstance(session);
        FerrySearchResultPage<WebDriver> ferrySearchResultPage = FerrySearchResultPage.getInstance(session);
        FerryBookingPage<WebDriver> ferryBookingPage = FerryBookingPage.getInstance(session);

        String origin = param.get("origin");
        String destination = param.get("destination");
        String ticketType = param.get("ticketType");
        int adult = Integer.parseInt(param.get("adult"));
        int child = 0;
        int infant = 0;
        int elder = 0;

        step("Get available date from API");
        String departureDate = ferryApi.getFerryAvailableDate(origin, destination, ticketType, adult, child,infant,elder);

        step("user open app");
        homePage.gotoHome();

        step("Login as guest");
        loginPage.loginAsGuest();

        if(Configs.ENVIRONMENT == Environment.PROD) {
            step("Change currency to 'IDR'");
            homePage.changeCurrency("IDR","");
        }

        step("Get App Language");
        String language = homePage.getLanguageApp();

        step("Open Ferry Landing Page");
        homePage.openVerticalPageDeeplink("Ferry", language, "IDR");

        step("input origin port");
        ferrySearchPage.skipCoachMark();
        ferrySearchPage.allowLocationPopup();
        ferrySearchPage.inputOrigin(origin);

        step("input destination port");
        ferrySearchPage.inputDestination(destination);

        step("User set travel date & time");
        ferrySearchPage.selectDate(departureDate);

        step("Input ticket type");
        ferrySearchPage.selectTicketType(ticketType);

        step("submit search");
        ferrySearchPage.submitSearch();

        step("Check if ferry is available");
        boolean isFerryAvailable = ferrySearchResultPage.isFerryCardDisplayed();
        Assertion.assertThat("Ferry card is displayed", isFerryAvailable, is(false));

        step("Skip information coachMark in srp");
        ferrySearchResultPage.skipCoachMark();

        step("Select ferry form SRP");
        ferrySearchResultPage.chooseFirstFleet();

        step("Check term and condition Contents");
        Assertion.assertThat("User is able to see ferry vehicle TNC contents on booking form ", ferryBookingPage.VerifyTermAndConditionPage(), is(true));
    }

    @Author(names = {"Kartik"})
    @Module(name = "CompleteBooking")
    @Tribe(name = "Ferry")
    @Vertical(name = "GroundTransport")
    @Test
    public void testCompleteBookingFerryWithPedestrianAndVerifiedVaccine() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testCompleteBookingFerryWithPedestrianAndVerifiedVaccine");

        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        FerryApi<?> ferryApi = new FerryApi<>(session);
        FerrySearchPage<WebDriver> ferrySearchPage = FerrySearchPage.getInstance(session);
        FerrySearchResultPage<WebDriver> ferrySearchResultPage = FerrySearchResultPage.getInstance(session);
        FerryBookingPage<WebDriver> ferryBookingPage = FerryBookingPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);

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

        step("user login using " + username + " account");
        loginPage.loginWith(loginMethod, username, password);

        if(Configs.ENVIRONMENT == Environment.PROD) {
            step("Change currency to 'IDR'");
            homePage.changeCurrency("IDR",username);
        }

        step("Get App Language");
        String language = homePage.getLanguageApp();

        step("Open Ferry Landing Page");
        homePage.openVerticalPageDeeplink("Ferry", language, "IDR");

        step("Skip coachmark");
        ferrySearchPage.skipCoachMark();
        ferrySearchPage.allowLocationPopup();

        step("input origin port");
        ferrySearchPage.inputOrigin(originPort);

        step("input destination port");
        ferrySearchPage.inputDestination(destinationPort);

        step("User input pax qty");
        ferrySearchPage.openTicketQty();
        ferrySearchPage.setTicketQty(adult, child, infant, elder);
        ferrySearchPage.saveTicketQty();

        step("User set travel date & time");
        ferrySearchPage.selectDate(departureDate);

        step("Input ticket type");
        ferrySearchPage.selectTicketType(ticketType);

        step("submit search");
        ferrySearchPage.submitSearch();

        step("Check if ferry is available");
        boolean isFerryAvailable = ferrySearchResultPage.isFerryCardDisplayed();
        Assertion.assertThat("Ferry card is displayed", isFerryAvailable, is(false));

        step("Skip information coachmark in srp");
        ferrySearchResultPage.skipCoachMark();

        step("Check booking form price detail");
        ferrySearchResultPage.chooseFirstFleet();

        step("fill vaccine verified Passenger detail");
        ferryBookingPage.fillGuestsAndVaccineDetails(vaccineCheck, VaccineVerifiedID, adult, elder);
        ferryBookingPage.fillGuestData(adult, child, infant, elder);

        step("fill and verify vaccination Details");
        ferryBookingPage.verifyVaccinatedStatus(vaccineCheck);

        step("Check term and condition");
        ferryBookingPage.clickTncCheckBox();

        if (Configs.ENVIRONMENT != Environment.PROD) {
            step("continue to payment page");
            ferryBookingPage.submitBooking();
            ferryBookingPage.setLocationService();

            step("verify order id is received");
            String orderID = paymentPage.checkOrderID();
            Assertion.assertThat("user get order id", !orderID.isEmpty(), is(true));
        }
    }

    @Author(names = {"Kartik"})
    @Module(name = "CompleteBooking")
    @Tribe(name = "Ferry")
    @Vertical(name = "GroundTransport")
    @Test
    public void CompletedBookingFerryWithPassengerCarAndVaccineTests() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("CompletedBookingFerryWithPassengerCarAndVaccineTests");

        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        FerryApi<?> ferryApi = new FerryApi<>(session);
        FerrySearchPage<WebDriver> ferrySearchPage = FerrySearchPage.getInstance(session);
        FerrySearchResultPage<WebDriver> ferrySearchResultPage = FerrySearchResultPage.getInstance(session);
        FerryBookingPage<WebDriver> ferryBookingPage = FerryBookingPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);

        String username = param.get("username");
        String VaccineVerifiedID = param.get("VaccineVerifiedID");
        String originPort = param.get("originPort");
        String destinationPort = param.get("destinationPort");
        String ticketType = param.get("ticketType");
        String vaccineCheck = param.get("vaccineCheck");
        int adult = Integer.parseInt(param.get("adult"));
        int child = Integer.parseInt(param.get("child"));
        int infant = Integer.parseInt(param.get("infant"));
        int elder = Integer.parseInt(param.get("elder"));
        String phoneNumber = param.get("phoneNumber");
        String fullName = CoreFunctions.randomName();

        step("Get available date from API");
        String departureDate = ferryApi.getFerryAvailableDate(originPort, destinationPort, ticketType, adult, child,infant,elder);

        step("user open app");
        homePage.gotoHome();

        step("user login guest");
        loginPage.loginAsGuest();

        if(Configs.ENVIRONMENT == Environment.PROD) {
            step("Change currency to 'IDR'");
            homePage.changeCurrency("IDR","");
        }

        step("Get App Language");
        String language = homePage.getLanguageApp();

        step("Open Ferry Landing Page");
        homePage.openVerticalPageDeeplink("Ferry", language, "IDR");

        step("Skip coachmark");
        ferrySearchPage.skipCoachMark();
        ferrySearchPage.allowLocationPopup();

        step("input origin port");
        ferrySearchPage.inputOrigin(originPort);

        step("input destination port");
        ferrySearchPage.inputDestination(destinationPort);

        step("User input pax qty");
        ferrySearchPage.openTicketQty();
        ferrySearchPage.setTicketQty(adult, child, infant, elder);
        ferrySearchPage.saveTicketQty();

        step("Input ticket type");
        ferrySearchPage.selectTicketType(ticketType);

        step("Departure date");
        ferrySearchPage.selectDate(departureDate);

        step("submit search");
        ferrySearchPage.submitSearch();

        step("Check if ferry is available");
        boolean isFerryAvailable = ferrySearchResultPage.isFerryCardDisplayed();
        Assertion.assertThat("Ferry card is displayed", isFerryAvailable, is(false));

        step("Skip information coachmark in srp");
        ferrySearchResultPage.skipCoachMark();

        step("select ferry from srp");
        ferrySearchResultPage.chooseFirstFleet();

        step("Input contact detail");
        ferryBookingPage.fillGuestContactDetail(username, fullName, phoneNumber);

        step("Input passenger detail");
        ferryBookingPage.fillGuestsAndVaccineDetails(vaccineCheck, VaccineVerifiedID, adult, elder);

        step("fill and verify vaccination Details");
        ferryBookingPage.verifyVaccinatedStatus(vaccineCheck);

        step("Fill plate number if required");
        ferryBookingPage.fillLicensePlateNumber();

        step("Check term and condition");
        ferryBookingPage.clickTncCheckBox();

        if(Configs.ENVIRONMENT != Environment.PROD) {
            step("continue to payment page");
            ferryBookingPage.submitBooking();
            ferryBookingPage.setLocationService();

            step("verify order id is received");
            String orderID = paymentPage.checkOrderID();
            Assertion.assertThat("user get order id", !orderID.isEmpty(), is(true));
        }
    }

    @Author(names = {"Kartik"})
    @Module(name = "CompleteBooking")
    @Tribe(name = "Ferry")
    @Vertical(name = "GroundTransport")
    @Test
    public void CompletedBookingFerryWithPickUpTruckAndVaccineTests() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("CompletedBookingFerryWithPickUpTruckAndVaccineTests");

        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        FerryApi<?> ferryApi = new FerryApi<>(session);
        FerrySearchPage<WebDriver> ferrySearchPage = FerrySearchPage.getInstance(session);
        FerrySearchResultPage<WebDriver> ferrySearchResultPage = FerrySearchResultPage.getInstance(session);
        FerryBookingPage<WebDriver> ferryBookingPage = FerryBookingPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);

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
        String daysToDepart = ferryApi.getFerryAvailableDate(originPort, destinationPort, ticketType, adult, child,infant,elder);

        step("user open app");
        homePage.gotoHome();

        step("user login using " + username + " account");
        loginPage.loginWith(loginMethod, username, password);

        if(Configs.ENVIRONMENT == Environment.PROD) {
            step("Change currency to 'IDR'");
            homePage.changeCurrency("IDR",username);
        }

        step("Get App Language");
        String language = homePage.getLanguageApp();

        step("Open Ferry Landing Page");
        homePage.openVerticalPageDeeplink("Ferry", language, "IDR");

        step("Skip coachmark");
        ferrySearchPage.skipCoachMark();
        ferrySearchPage.allowLocationPopup();

        step("input origin port");
        ferrySearchPage.inputOrigin(originPort);

        step("input destination port");
        ferrySearchPage.inputDestination(destinationPort);

        step("User input pax qty");
        ferrySearchPage.openTicketQty();
        ferrySearchPage.setTicketQty(adult, child, infant, elder);
        ferrySearchPage.saveTicketQty();

        step("Input ticket type");
        ferrySearchPage.selectTicketType(ticketType);

        step("Departure date");
        ferrySearchPage.selectDate(daysToDepart);

        step("submit search");
        ferrySearchPage.submitSearch();

        step("Check if ferry is available");
        boolean isFerryAvailable = ferrySearchResultPage.isFerryCardDisplayed();
        Assertion.assertThat("Ferry card is displayed", isFerryAvailable, is(false));

        step("Skip information coachmark in srp");
        ferrySearchResultPage.skipCoachMark();

        step("Check booking form price detail");
        ferrySearchResultPage.chooseFirstFleet();

        step("Input passenger detail");
        ferryBookingPage.fillGuestsAndVaccineDetails(vaccineCheck, VaccineVerifiedID, adult, elder);

        step("input child guest detail");
        ferryBookingPage.fillGuestData(adult, child, infant, elder);

        step("Fill plate number if required");
        ferryBookingPage.fillLicensePlateNumber();

        step("Check term and condition");
        ferryBookingPage.clickTncCheckBox();

        if(Configs.ENVIRONMENT != Environment.PROD) {
            step("continue to payment page");
            ferryBookingPage.submitBooking();
            ferryBookingPage.setLocationService();

            step("verify order id is received");
            String orderID = paymentPage.checkOrderID();
            Assertion.assertThat("user get order id", !orderID.isEmpty(), is(true));
        }
    }

    @Author(names = {"Kartik"})
    @Module(name = "CompleteBooking")
    @Tribe(name = "Ferry")
    @Vertical(name = "GroundTransport")
    @Test
    public void CompletedBookingFerryWithBicycleAndVaccineTests() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("CompletedBookingFerryWithBicycleAndVaccineTests");

        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        FerryApi<?> ferryApi = new FerryApi<>(session);
        FerrySearchPage<WebDriver> ferrySearchPage = FerrySearchPage.getInstance(session);
        FerrySearchResultPage<WebDriver> ferrySearchResultPage = FerrySearchResultPage.getInstance(session);
        FerryBookingPage<WebDriver> ferryBookingPage = FerryBookingPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);

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

        step("user login using " + username + " account");
        loginPage.loginWith(loginMethod, username, password);

        if(Configs.ENVIRONMENT == Environment.PROD) {
            step("Change currency to 'IDR'");
            homePage.changeCurrency("IDR",username);
        }

        step("Get App Language");
        String language = homePage.getLanguageApp();

        step("Open Ferry Landing Page");
        homePage.openVerticalPageDeeplink("Ferry", language, "IDR");

        step("Skip coachmark");
        ferrySearchPage.skipCoachMark();
        ferrySearchPage.allowLocationPopup();

        step("input origin port");
        ferrySearchPage.inputOrigin(originPort);

        step("input destination port");
        ferrySearchPage.inputDestination(destinationPort);

        step("User input pax qty");
        ferrySearchPage.openTicketQty();
        ferrySearchPage.setTicketQty(adult, child, infant, elder);
        ferrySearchPage.saveTicketQty();

        step("Input ticket type");
        ferrySearchPage.selectTicketType(ticketType);

        step("Departure date");
        ferrySearchPage.selectDate(departureDate);

        step("submit search");
        ferrySearchPage.submitSearch();

        step("Check if ferry is available");
        boolean isFerryAvailable = ferrySearchResultPage.isFerryCardDisplayed();
        Assertion.assertThat("Ferry card is displayed", isFerryAvailable, is(false));

        step("Skip information coachmark in srp");
        ferrySearchResultPage.skipCoachMark();

        step("Check booking form price detail");
        ferrySearchResultPage.chooseFirstFleet();

        step("Input passenger detail");
        ferryBookingPage.fillGuestsAndVaccineDetails(vaccineCheck, VaccineVerifiedID, adult, elder);

        step("input child guest detail");
        ferryBookingPage.fillGuestData(adult, child, infant, elder);

        step("Check term and condition");
        ferryBookingPage.clickTncCheckBox();

        step("continue to payment page");
        ferryBookingPage.submitBooking();
        ferryBookingPage.setLocationService();

        step("verify order id is received");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id", !orderID.isEmpty(), is(true));
    }

    @Author(names = {"Kartik"})
    @Module(name = "SRP")
    @Tribe(name = "Ferry")
    @Vertical(name = "GroundTransport")
    @Test
    public void testUserChangeSearchDataOnFerrySRPGuest() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testUserChangeSearchDataOnFerrySRPGuest");

        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        FerryApi<?> ferryApi = new FerryApi<>(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FerrySearchPage<WebDriver> ferrySearchPage = FerrySearchPage.getInstance(session);
        FerrySearchResultPage<WebDriver> ferrySearchResultPage = FerrySearchResultPage.getInstance(session);

        String origin = param.get("origin");
        String destination = param.get("destination");
        String ticketType = param.get("ticketType");

        String originNew = param.get("originNew");
        String destinationNew = param.get("destinationNew");
        String ticketTypeNew = param.get("ticketTypeNew");

        int adult = Integer.parseInt(param.get("adult"));
        int child = Integer.parseInt(param.get("child"));
        int infant = Integer.parseInt(param.get("infant"));
        int elder = Integer.parseInt(param.get("elder"));

        int  adultNew = Integer.parseInt(param.get("adultNew"));
        int childNew = Integer.parseInt(param.get("childNew"));
        int infantNew = Integer.parseInt(param.get("infantNew"));
        int elderNew = Integer.parseInt(param.get("elderNew"));

        step("Get available date from API");
        String departureDate = ferryApi.getFerryAvailableDate(origin, destination, ticketType, adult, child,infant,elder);

        step("user open app");
        homePage.gotoHome();

        step("Login as guest");
        loginPage.loginAsGuest();

        if(Configs.ENVIRONMENT == Environment.PROD) {
            step("Change currency to 'IDR'");
            homePage.changeCurrency("IDR","");
        }

        step("Get App Language");
        String language = homePage.getLanguageApp();

        step("Open Ferry Landing Page");
        homePage.openVerticalPageDeeplink("Ferry", language, "IDR");

        step("User select depart port and arrival port");
        ferrySearchPage.skipCoachMark();
        ferrySearchPage.allowLocationPopup();

        ferrySearchPage.inputOrigin(origin);
        ferrySearchPage.inputDestination(destination);

        step("User select ticket type");
        ferrySearchPage.selectTicketType(ticketType);

        step("User input adult: " + adult + " infant: " + infant +" child: " + child + " elder: " + elder + " pax qty");
        ferrySearchPage.openTicketQty();
        ferrySearchPage.setTicketQty(adult, child, infant, elder);
        ferrySearchPage.saveTicketQty();

        step("User tap on search ferry button");
        ferrySearchPage.submitSearch();
        ferrySearchResultPage.skipCoachMark();

        step("User tap on Change button on SRP");
        ferrySearchResultPage.clickChangeButton();

        step("User change search input data on SRP");
        ferrySearchPage.inputOrigin(originNew);
        ferrySearchPage.inputDestination(destinationNew);

        step("Departure date");
        ferrySearchPage.selectDate(departureDate);

        step("User Change ticket type on SRP");
        ferrySearchPage.selectTicketType(ticketTypeNew);

        step("user select adult: " + adultNew + " infant: " + infantNew +" child: " + childNew + " elder: " + elderNew + " pax qty");
        ferrySearchPage.openTicketQty();
        ferrySearchPage.setTicketQty(adultNew, childNew, infantNew, elderNew);
        ferrySearchPage.saveTicketQty();
        ferrySearchResultPage.clickSearchFerryButton();

        step("Verify SRP header Content");
        boolean result = ferrySearchResultPage.verifySRPHeaderContent(originNew, destinationNew, ticketTypeNew);
        Assertion.assertThat("SRP header content matches with new testdata", result, is(true));
    }

    @Author(names = {"Kartik"})
    @Module(name = "SRP")
    @Tribe(name = "Ferry")
    @Vertical(name = "GroundTransport")
    @Test
    public void testUserChangeSearchDataOnFerrySRP() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testUserChangeSearchDataOnFerrySRP");

        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        FerryApi<?> ferryApi = new FerryApi<>(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FerrySearchPage<WebDriver> ferrySearchPage = FerrySearchPage.getInstance(session);
        FerrySearchResultPage<WebDriver> ferrySearchResultPage = FerrySearchResultPage.getInstance(session);

        String origin = param.get("origin");
        String destination = param.get("destination");
        String ticketType = param.get("ticketType");
        String loginMethod = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String originNew = param.get("originNew");
        String destinationNew = param.get("destinationNew");
        String ticketTypeNew = param.get("ticketTypeNew");

        int adult = Integer.parseInt(param.get("adult"));
        int child = Integer.parseInt(param.get("child"));
        int infant = Integer.parseInt(param.get("infant"));
        int elder = Integer.parseInt(param.get("elder"));

        int adultNew = Integer.parseInt(param.get("adultNew"));
        int infantNew = Integer.parseInt(param.get("infantNew"));
        int childNew = Integer.parseInt(param.get("childNew"));
        int elderNew = Integer.parseInt(param.get("elderNew"));

        step("Get available date from API");
        String departureDate = ferryApi.getFerryAvailableDate(origin, destination, ticketType, adult, child,infant,elder);

        step("user open app");
        homePage.gotoHome();

        step("user login using " + username + " account");
        loginPage.loginWith(loginMethod, username, password);

        if(Configs.ENVIRONMENT == Environment.PROD) {
            step("Change currency to 'IDR'");
            homePage.changeCurrency("IDR",username);
        }

        step("Get App Language");
        String language = homePage.getLanguageApp();

        step("Open Ferry Landing Page");
        homePage.openVerticalPageDeeplink("Ferry", language, "IDR");

        step("User select depart port and arrival port");
        ferrySearchPage.skipCoachMark();
        ferrySearchPage.allowLocationPopup();
        ferrySearchPage.inputOrigin(origin);
        ferrySearchPage.inputDestination(destination);

        step("User select ticket type");
        ferrySearchPage.selectTicketType(ticketType);

        step("User input " + adult + " pax qty");
        ferrySearchPage.openTicketQty();
        ferrySearchPage.setTicketQty(adult, child, infant, elder);
        ferrySearchPage.saveTicketQty();

        step("User tap on search ferry button");
        ferrySearchPage.submitSearch();
        ferrySearchResultPage.skipCoachMark();

        step("User tap on Change button on SRP");
        ferrySearchResultPage.clickChangeButton();

        step("User change search input data on SRP");
        ferrySearchPage.inputOrigin(originNew);
        ferrySearchPage.inputDestination(destinationNew);

        step("User Change ticket type on SRP");
        ferrySearchPage.selectTicketType(ticketTypeNew);

        step("Departure date");
        ferrySearchPage.selectDate(departureDate);

        step("User Change pax qty on SRP");
        ferrySearchPage.openTicketQty();
        ferrySearchPage.setTicketQty(adultNew, childNew, infantNew, elderNew);
        ferrySearchPage.saveTicketQty();
        ferrySearchResultPage.clickSearchFerryButton();

        step("Verify SRP header Content");
        boolean result = ferrySearchResultPage.verifySRPHeaderContent(originNew, destinationNew, ticketTypeNew);
        Assertion.assertThat("SRP header content matches with new testdata", result, is(true));
    }

    @Author(names = {"Kartik"})
    @Module(name = "SRP")
    @Tribe(name = "Ferry")
    @Vertical(name = "GroundTransport")
    @Test
    public void testSeeFerryVehicleTNConBookingFormLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testSeeFerryVehicleTNConBookingFormLogin");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        FerryApi<?> ferryApi = new FerryApi<>(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FerrySearchPage<WebDriver> ferrySearchPage = FerrySearchPage.getInstance(session);
        FerrySearchResultPage<WebDriver> ferrySearchResultPage = FerrySearchResultPage.getInstance(session);
        FerryBookingPage<WebDriver> ferryBookingPage = FerryBookingPage.getInstance(session);

        String loginMethod = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String origin = param.get("origin");
        String destination = param.get("destination");
        String ticketType = param.get("ticketType");
        int adult = 1, child = 0, infant = 0, elder = 0;

        step("Get available date from API");
        String departureDate = ferryApi.getFerryAvailableDate(origin, destination, ticketType, adult, child,infant,elder);

        step("user open app");
        homePage.gotoHome();

        step("user login using " + username + " account");
        loginPage.loginWith(loginMethod, username, password);

        if(Configs.ENVIRONMENT == Environment.PROD) {
            step("Change currency to 'IDR'");
            homePage.changeCurrency("IDR",username);
        }

        step("Get App Language");
        String language = homePage.getLanguageApp();

        step("Open Ferry Landing Page");
        homePage.openVerticalPageDeeplink("Ferry", language, "IDR");

        step("input origin port");
        ferrySearchPage.skipCoachMark();
        ferrySearchPage.allowLocationPopup();
        ferrySearchPage.inputOrigin(origin);

        step("input destination port");
        ferrySearchPage.inputDestination(destination);

        step("Input ticket type");
        ferrySearchPage.selectTicketType(ticketType);

        step("User set travel date & time");
        ferrySearchPage.selectDate(departureDate);

        step("submit search");
        ferrySearchPage.submitSearch();

        step("Check if ferry is available");
        boolean isFerryAvailable = ferrySearchResultPage.isFerryCardDisplayed();
        Assertion.assertThat("Ferry card is displayed", isFerryAvailable, is(false));

        step("Skip information coachMark in srp");
        ferrySearchResultPage.skipCoachMark();

        step("Select ferry form SRP");
        ferrySearchResultPage.chooseFirstFleet();

        step("Check term and condition Contents");
        Assertion.assertThat("User is able to see ferry vehicle TNC contents on booking form ", ferryBookingPage.VerifyTermAndConditionPage(), is(true));
    }

    @Author(names = {"Kartik"})
    @Module(name = "CompleteBooking")
    @Tribe(name = "Ferry")
    @Vertical(name = "GroundTransport")
    @Test
    public void verifyRejectionNotificationSameIDBookingForm() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyRejectionNotificationSameIDBookingForm");

        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        FerryApi<?> ferryApi = new FerryApi<>(session);
        FerrySearchPage<WebDriver> ferrySearchPage = FerrySearchPage.getInstance(session);
        FerrySearchResultPage<WebDriver> ferrySearchResultPage = FerrySearchResultPage.getInstance(session);
        FerryBookingPage<WebDriver> ferryBookingPage = FerryBookingPage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);

        String loginMethod = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String originPort = param.get("origin");
        String destinationPort = param.get("destination");
        String ticketType = param.get("ticketType");
        String vaccineCheck = param.get("vaccineCheck");
        int adult = Integer.parseInt(param.get("adult"));
        int child = 0, infant = 0, elder =0;

        step("Get available date from API");
        String departureDate = ferryApi.getFerryAvailableDate(originPort, destinationPort, ticketType, adult, child,infant,elder);

        step("user open app");
        homePage.gotoHome();

        step("user login using " + username + " account");
        loginPage.loginWith(loginMethod, username, password);

        if(Configs.ENVIRONMENT == Environment.PROD) {
            step("Change currency to 'IDR'");
            homePage.changeCurrency("IDR",username);
        }

        step("Get App Language");
        String language = homePage.getLanguageApp();

        step("Open Ferry Landing Page");
        homePage.openVerticalPageDeeplink("Ferry", language, "IDR");

        step("Skip coachmark");
        ferrySearchPage.skipCoachMark();
        ferrySearchPage.allowLocationPopup();

        step("input origin port");
        ferrySearchPage.inputOrigin(originPort);

        step("input destination port");
        ferrySearchPage.inputDestination(destinationPort);

        step("User input pax qty");
        ferrySearchPage.openTicketQty();
        ferrySearchPage.setTicketQty(adult, child, infant, elder);
        ferrySearchPage.saveTicketQty();

        step("Input ticket type");
        ferrySearchPage.selectTicketType(ticketType);

        step("Departure date");
        ferrySearchPage.selectDate(departureDate);

        step("submit search");
        ferrySearchPage.submitSearch();

        step("Check if ferry is available");
        boolean isFerryAvailable = ferrySearchResultPage.isFerryCardDisplayed();
        Assertion.assertThat("Ferry card is displayed", isFerryAvailable, is(false));

        step("Skip information coachmark in srp");
        ferrySearchResultPage.skipCoachMark();

        step("Check booking form price detail");
        ferrySearchResultPage.chooseFirstFleet();

        step("Input Adult guest detail with same ID and verify user failed to continue");
        Assertion.assertThat("Rejection notification for same id with other passenger is displayed.", ferryBookingPage.VerifyGuestDetailSameId(adult, vaccineCheck), is(true));

        step("Check term and condition");
        ferryBookingPage.clickTncCheckBox();

        step("continue to payment page");
        ferryBookingPage.submitBooking();
        ferryBookingPage.setLocationService();

        step("verify order id is received");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id", !orderID.isEmpty(), is(true));
    }

    @Author(names = {"Kartik"})
    @Module(name = "CompleteBooking")
    @Tribe(name = "Ferry")
    @Vertical(name = "GroundTransport")
    @Test
    public void verifyRejectionNotificationSameIDBookingFormGuest() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyRejectionNotificationSameIDBookingFormGuest");

        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        FerryApi<?> ferryApi = new FerryApi<>(session);
        FerrySearchPage<WebDriver> ferrySearchPage = FerrySearchPage.getInstance(session);
        FerrySearchResultPage<WebDriver> ferrySearchResultPage = FerrySearchResultPage.getInstance(session);
        FerryBookingPage<WebDriver> ferryBookingPage = FerryBookingPage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);


        String username = param.get("username");
        String phoneNumber = CoreFunctions.randomPhone();
        String fullName = CoreFunctions.randomName();
        String originPort = param.get("origin");
        String destinationPort = param.get("destination");
        String ticketType = param.get("ticketType");
        String vaccineCheck = param.get("vaccineCheck");
        int adult = Integer.parseInt(param.get("adult"));

        step("Get available date from API");
        String departureDate = ferryApi.getFerryAvailableDate(originPort, destinationPort, ticketType, adult, 0,0,0);

        step("user open app");
        homePage.gotoHome();

        step("user login as guest");
        loginPage.loginAsGuest();

        if(Configs.ENVIRONMENT == Environment.PROD) {
            step("Change currency to 'IDR'");
            homePage.changeCurrency("IDR","");
        }

        step("Get App Language");
        String language = homePage.getLanguageApp();

        step("Open Ferry Landing Page");
        homePage.openVerticalPageDeeplink("Ferry", language, "IDR");

        step("Skip coachmark");
        ferrySearchPage.skipCoachMark();
        ferrySearchPage.allowLocationPopup();

        step("input origin port");
        ferrySearchPage.inputOrigin(originPort);

        step("input destination port");
        ferrySearchPage.inputDestination(destinationPort);

        step("User input pax qty");
        ferrySearchPage.openTicketQty();
        ferrySearchPage.resetTicketQty();
        ferrySearchPage.setTicketQty("adult", adult);
        ferrySearchPage.saveTicketQty();

        step("Input ticket type");
        ferrySearchPage.selectTicketType(ticketType);

        step("Departure date");
        ferrySearchPage.selectDate(departureDate);

        step("submit search");
        ferrySearchPage.submitSearch();

        step("Check if ferry is available");
        boolean isFerryAvailable = ferrySearchResultPage.isFerryCardDisplayed();
        Assertion.assertThat("Ferry card is displayed", isFerryAvailable, is(false));

        step("Skip information coachmark in srp");
        ferrySearchResultPage.skipCoachMark();

        step("Check booking form price detail");
        ferrySearchResultPage.chooseFirstFleet();

        step("Input contact detail");
        ferryBookingPage.fillGuestContactDetail(username, fullName, phoneNumber);

        step("Input Adult guest detail with same ID and verify user failed to continue");
        Assertion.assertThat("Rejection notification for same id with other passenger is displayed.", ferryBookingPage.VerifyGuestDetailSameId(adult, vaccineCheck), is(true));

        step("Check term and condition");
        ferryBookingPage.clickTncCheckBox();

        step("continue to payment page");
        ferryBookingPage.submitBooking();
        ferryBookingPage.setLocationService();

        step("verify order id is received");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id", !orderID.isEmpty(), is(true));
    }

    @Author(names = {"Kartik"})
    @Module(name = "SRP")
    @Tribe(name = "Ferry")
    @Vertical(name = "GroundTransport")
    @Test
    public void checkFerryDetailOrder() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("checkFerryDetailOrder");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        FerryApi<?> ferryApi = new FerryApi<>(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FerrySearchPage<WebDriver> ferrySearchPage = FerrySearchPage.getInstance(session);
        FerrySearchResultPage<WebDriver> ferrySearchResultPage = FerrySearchResultPage.getInstance(session);
        FerryBookingPage<WebDriver> ferryBookingPage = FerryBookingPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);

        String loginMethod = param.get("loginType");
        String email = param.get("username");
        String password = param.get("password");
        String origin = param.get("origin");
        String destination = param.get("destination");
        String vehicleType = param.get("vehicleType");

        String cardNumber = param.get("cardNumber");
        String expiry = param.get("expiry");
        String cvv = param.get("cvv");
        String cardName = CoreFunctions.randomName();

        int adult = Integer.parseInt(param.get("adult"));
        int child = Integer.parseInt(param.get("child"));
        int infant = Integer.parseInt(param.get("infant"));
        int elder = Integer.parseInt(param.get("elder"));

        step("Get available date from API");
        String departureDate = ferryApi.getFerryAvailableDate(origin, destination, vehicleType, adult, child,infant,elder);

        step("user open app");
        homePage.gotoHome();

        step("user login using " + email + " account");
        loginPage.loginWith(loginMethod, email, password);

        step("Change currency to 'IDR'");
        homePage.changeCurrency("IDR",email);

        step("Get App Language");
        String language = homePage.getLanguageApp();

        step("Open Ferry Landing Page");
        homePage.openVerticalPageDeeplink("Ferry", language, "IDR");

        step("Skip coachmark");
        ferrySearchPage.skipCoachMark();
        ferrySearchPage.allowLocationPopup();

        step("input origin port");
        ferrySearchPage.inputOrigin(origin);

        step("input destination port");
        ferrySearchPage.inputDestination(destination);

        step("User input pax qty");
        ferrySearchPage.openTicketQty();
        ferrySearchPage.setTicketQty(adult, child, infant, elder);
        ferrySearchPage.saveTicketQty();

        step("Input ticket type");
        ferrySearchPage.selectTicketType(vehicleType);

        step("Departure date");
        ferrySearchPage.selectDate(departureDate);

        step("submit search");
        ferrySearchPage.submitSearch();

        step("Check if ferry is available");
        boolean isFerryAvailable = ferrySearchResultPage.isFerryCardDisplayed();
        Assertion.assertThat("Ferry card is displayed", isFerryAvailable, is(false));

        step("Skip information coachmark in srp");
        ferrySearchResultPage.skipCoachMark();

        step("Check booking form price detail");
        ferrySearchResultPage.chooseFirstFleet();

        step("input guest detail");
        ferryBookingPage.fillGuestData(adult,child , infant, elder);

        step("Get booking detail from booking page");
        String bookingRoute = String.valueOf(ferryBookingPage.getFerryRouteBookingPage());
        String bookingDate = String.valueOf(ferryBookingPage.getFerryCheckInDateBookingPage());
        List<String> bookingPassengerName = ferryBookingPage.getFerryPassengerNamesBookingPage();

        step("Fill plate number if required");
        ferryBookingPage.fillLicensePlateNumber();

        step("Check term and condition");
        ferryBookingPage.clickTncCheckBox();

        step("continue to payment page");
        ferryBookingPage.submitBooking();
        ferryBookingPage.setLocationService();

        step("verify order id is received");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id", !orderID.isEmpty(), is(true));

        if (Configs.ENVIRONMENT == Environment.PROD) {

            step("User Make payment With BCA VA in Prod");
            paymentPage.openPaymentMethod();
            paymentPage.selectPaymentMethod("BCA Virtual Account");
            paymentPage.clickPayButton();
        }

        if (Configs.ENVIRONMENT == Environment.GK || Configs.ENVIRONMENT == Environment.PREPROD) {
            step("User Make payment With CC");
            paymentPage.openPaymentMethod();
            paymentPage.selectPaymentMethod("Credit Card");
            paymentPage.fillCCDetailRealIssued(cardNumber, expiry, cvv, cardName);
            paymentPage.clickPayButtonCC();
            paymentPage.checkCcOtp();
        }

        if (Configs.ENVIRONMENT == Environment.GK || Configs.ENVIRONMENT == Environment.PREPROD) {
            step("Open recent order via deeplink");
            orderListPage.waitForOrderToBeInIssuedState(orderID,10);
            orderListPage.openActiveTicketViaDeeplink(orderID);
        }

        if (Configs.ENVIRONMENT == Environment.PROD) {
            step("Go to the order list");
            paymentPage.seeOrderList();
            step("Open Order waiting for payment");
            orderListPage.openOrderDetailWaitingForPayment(orderID);
        }


        if (Configs.ENVIRONMENT == Environment.GK || Configs.ENVIRONMENT == Environment.PREPROD) {
            Assertion.assertThat("User is able to see ferry View e-voucher button on Order detail page", orderDetailPage.verifyIssuedViewEticket(language), is(true));
            Assertion.assertThat("User is able to see ferry booking barcode on Order detail page", orderDetailPage.verifyIssuedBarCode(), is(true));
            Assertion.assertThat("User is able to see ferry Share  receipt button Order detail page", orderDetailPage.verifyIssuedShareEticket(), is(true));
            Assertion.assertThat("Receipt button is working", orderDetailPage.isReceiptButtonWork("IDR"), is(true));
        }

        step("Open the order detail");
        Assertion.assertThat("User is able to see ferry check-in date  on order details page", orderDetailPage.verifyFerryCheckinDate(bookingDate), is(true));
        Assertion.assertThat("User is able to see ferry route  on order details page", orderDetailPage.verifyFerryRoute(bookingRoute), is(true));
        Assertion.assertThat("User is able to see ferry passenger names on order details page", orderDetailPage.verifyFerryPassengerNames(bookingPassengerName), is(true));
    }

    @Author(names = {"Kartik"})
    @Module(name = "Order")
    @Tribe(name = "Ferry")
    @Vertical(name = "GroundTransport")
    @Test
    public void checkFerryDetailOrderGuest() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("checkFerryDetailOrderGuest");
        BaseTest.paramsThreadLocal.set(param);

        FerryPage<WebDriver> ferryPage = FerryPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        FerryApi<?> ferryApi = new FerryApi<>(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        FerrySearchPage<WebDriver> ferrySearchPage = FerrySearchPage.getInstance(session);
        FerrySearchResultPage<WebDriver> ferrySearchResultPage = FerrySearchResultPage.getInstance(session);
        FerryBookingPage<WebDriver> ferryBookingPage = FerryBookingPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);

        String email = param.get("username");
        String origin = param.get("origin");
        String destination = param.get("destination");
        String vehicleType = param.get("vehicleType");
        int adult = Integer.parseInt(param.get("adult"));
        int child = Integer.parseInt(param.get("child"));
        int infant = Integer.parseInt(param.get("infant"));
        int elder = Integer.parseInt(param.get("elder"));
        String cardNumber = param.get("cardNumber");
        String expiry = param.get("expiry");
        String cvv = param.get("cvv");
        String cardName = CoreFunctions.randomName();
        String passengerName = CoreFunctions.randomName();
        String phoneNumber = CoreFunctions.randomPhone();

        step("Get available date from API");
        String departureDate = ferryApi.getFerryAvailableDate(origin, destination, vehicleType, adult, child,infant,elder);

        step("user open app");
        homePage.gotoHome();

        step("user continue as guest");
        loginPage.loginAsGuest();

        if(Configs.ENVIRONMENT == Environment.PROD) {
            step("Change currency to 'IDR'");
            homePage.changeCurrency("IDR","");
        }

        step("Get App Language");
        String language = homePage.getLanguageApp();

        step("Open Ferry Landing Page");
        homePage.openVerticalPageDeeplink("Ferry", language, "IDR");

        step("Skip coachmark");
        ferrySearchPage.allowLocationPopup();
        ferrySearchPage.skipCoachMark();
        ferrySearchPage.allowLocationPopup();

        step("input origin port");
        ferrySearchPage.inputOrigin(origin);

        step("input destination port");
        ferrySearchPage.inputDestination(destination);

        step("User input pax qty");
        ferrySearchPage.openTicketQty();
        ferrySearchPage.setTicketQty(adult, child, infant, elder);
        ferrySearchPage.saveTicketQty();

        step("Input ticket type");
        ferrySearchPage.selectTicketType(vehicleType);

        step("Departure date");
        ferrySearchPage.selectDate(departureDate);

        step("submit search");
        ferrySearchPage.submitSearch();

        step("Check if ferry is available");
        boolean isFerryAvailable = ferrySearchResultPage.isFerryCardDisplayed();
        Assertion.assertThat("Ferry card is displayed", isFerryAvailable, is(false));

        step("Skip information coachmark in srp");
        ferrySearchResultPage.skipCoachMark();

        step("Check booking form price detail");
        ferrySearchResultPage.chooseFirstFleet();

        step("Input passenger detail");
        ferryBookingPage.fillGuestContactDetail(email, passengerName, phoneNumber);

        step("input  guest detail");
        ferryBookingPage.fillGuestData(adult,child , infant, elder);

        step("Get booking detail from booking page");
        String bookingRoute = String.valueOf(ferryBookingPage.getFerryRouteBookingPage());
        String bookingDate = String.valueOf(ferryBookingPage.getFerryCheckInDateBookingPage());
        List<String> bookingPassengerName = ferryBookingPage.getFerryPassengerNamesBookingPage();

        step("Fill plate number if required");
        ferryBookingPage.fillLicensePlateNumber();

        step("Check term and condition");
        ferryBookingPage.clickTncCheckBox();

        step("continue to payment page");
        ferryBookingPage.submitBooking();
        ferryBookingPage.setLocationService();

        step("verify order id is received");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id", !orderID.isEmpty(), is(true));

        if (Configs.ENVIRONMENT == Environment.PROD) {
            step("User Make payment With BCA VA in Prod");
            paymentPage.openPaymentMethod();
            paymentPage.selectPaymentMethod("BCA Virtual Account");
            paymentPage.clickPayButton();
        }

        if (Configs.ENVIRONMENT == Environment.GK || Configs.ENVIRONMENT == Environment.PREPROD) {
            step("User Make payment With CC");
            paymentPage.openPaymentMethod();
            paymentPage.selectPaymentMethod("Credit Card");
            paymentPage.fillCCDetailRealIssued(cardNumber, expiry, cvv, cardName);
            paymentPage.clickPayButtonCC();
            paymentPage.checkCcOtp();
        }

        step("Go to the order list");
        paymentPage.seeOrderList();

        if (Configs.ENVIRONMENT == Environment.GK || Configs.ENVIRONMENT == Environment.PREPROD) {
            step("Open recent order via deeplink");
            orderListPage.openActiveTicketViaDeeplink(orderID);
        }

        if (Configs.ENVIRONMENT == Environment.PROD) {
            step("Open Order waiting for payment");
            orderListPage.openOrderDetailWaitingForPayment(orderID);
        }

        if (Configs.ENVIRONMENT == Environment.GK || Configs.ENVIRONMENT == Environment.PREPROD) {
            Assertion.assertThat("User is able to see ferry View e-voucher button on Order detail page", orderDetailPage.verifyIssuedViewEticket(language), is(true));
            Assertion.assertThat("User is able to see ferry booking barcode on Order detail page", orderDetailPage.verifyIssuedBarCode(), is(true));
            Assertion.assertThat("User is able to see ferry Share  receipt button Order detail page", orderDetailPage.verifyIssuedShareEticket(), is(true));
            Assertion.assertThat("Receipt button is working", orderDetailPage.isReceiptButtonWork("IDR"), is(true));
        }

        step("Open the order detail");
        Assertion.assertThat("User is able to see ferry check-in date  on order details page", orderDetailPage.verifyFerryCheckinDate(bookingDate), is(true));
        Assertion.assertThat("User is able to see ferry route  on order details page", orderDetailPage.verifyFerryRoute(bookingRoute), is(true));
        Assertion.assertThat("User is able to see ferry passenger names on order details page", orderDetailPage.verifyFerryPassengerNames(bookingPassengerName), is(true));
    }

    @Author(names = {"Kartik"})
    @Module(name = "CompleteBooking")
    @Tribe(name = "Ferry")
    @Vertical(name = "GroundTransport")
    @Test
    public void CompleteBookingFerryWithBicycleAndElderPaxVaccine() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("CompleteBookingFerryWithBicycleAndElderPaxVaccine");

        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        FerryApi<?> ferryApi = new FerryApi<>(session);
        FerrySearchPage<WebDriver> ferrySearchPage = FerrySearchPage.getInstance(session);
        FerrySearchResultPage<WebDriver> ferrySearchResultPage = FerrySearchResultPage.getInstance(session);
        FerryBookingPage<WebDriver> ferryBookingPage = FerryBookingPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);


        String username = param.get("username");
        String VaccineVerifiedID = param.get("VaccineVerifiedID");
        String originPort = param.get("originPort");
        String destinationPort = param.get("destinationPort");
        String ticketType = param.get("ticketType");
        String vaccineCheck = param.get("vaccineCheck");
        int adult = Integer.parseInt(param.get("adult"));
        int child = Integer.parseInt(param.get("child"));
        int infant = Integer.parseInt(param.get("infant"));
        int elder = Integer.parseInt(param.get("elder"));
        String phoneNumber =String.valueOf(CoreFunctions.generateRandomInt(10));
        String fullName = CoreFunctions.randomName();

        step("Get available date from API");
        String departureDate = ferryApi.getFerryAvailableDate(originPort, destinationPort, ticketType, adult, child,infant,elder);

        step("user open app");
        homePage.gotoHome();

        step("user login Guest");
        loginPage.loginAsGuest();

        if(Configs.ENVIRONMENT == Environment.PROD) {
            step("Change currency to 'IDR'");
            homePage.changeCurrency("IDR","");
        }

        step("Get App Language");
        String language = homePage.getLanguageApp();

        step("Open Ferry Landing Page");
        homePage.openVerticalPageDeeplink("Ferry", language, "IDR");

        step("Skip coachmark");
        ferrySearchPage.skipCoachMark();
        ferrySearchPage.allowLocationPopup();

        step("input origin port");
        ferrySearchPage.inputOrigin(originPort);

        step("input destination port");
        ferrySearchPage.inputDestination(destinationPort);

        step("User input pax qty");
        ferrySearchPage.openTicketQty();
        ferrySearchPage.setTicketQty(adult, child, infant, elder);
        ferrySearchPage.saveTicketQty();

        step("Input ticket type");
        ferrySearchPage.selectTicketType(ticketType);

        step("Departure date");
        ferrySearchPage.selectDate(departureDate);

        step("submit search");
        ferrySearchPage.submitSearch();

        step("Check if ferry is available");
        boolean isFerryAvailable = ferrySearchResultPage.isFerryCardDisplayed();
        Assertion.assertThat("Ferry card is displayed", isFerryAvailable, is(false));

        step("Skip information coachmark in srp");
        ferrySearchResultPage.skipCoachMark();

        step("Check booking form price detail");
        ferrySearchResultPage.chooseFirstFleet();

        step("Input contact detail");
        ferryBookingPage.fillGuestContactDetail(username, fullName, phoneNumber);

        step("Input passenger detail");
        ferryBookingPage.fillGuestsAndVaccineDetails(vaccineCheck, VaccineVerifiedID, adult, elder);

        step("Check term and condition");
        ferryBookingPage.clickTncCheckBox();

        if(Configs.ENVIRONMENT != Environment.PROD) {
            step("continue to payment page");
            ferryBookingPage.submitBooking();
            ferryBookingPage.setLocationService();

            step("verify order id is received");
            String orderID = paymentPage.checkOrderID();
            Assertion.assertThat("user get order id", !orderID.isEmpty(), is(true));
        }
    }
}