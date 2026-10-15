package com.tiket.web.test.nft.ferry;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Environment;
import com.tiket.model.Platform;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.testbase.Configs;
import com.tiket.web.WebTest;
import com.tiket.web.page.nft.ferry.BookingPage.FerryBookingPageWeb;
import com.tiket.web.page.nft.ferry.LandingPage.FerrySearchPageWeb;
import com.tiket.web.page.nft.ferry.SearchResultPage.FerrySearchResultPageWeb;
import com.tiket.web.page.order.OrderDetail.OrderDetailWeb;
import com.tiket.web.page.order.OrderList.OrderListWeb;
import com.tiket.web.dWeb.page.payment.PaymentPage;
import com.tiket.web.page.common.home.HomePageWeb;
import com.tiket.web.page.common.home.LoginPageWeb;
import org.testng.annotations.Test;

import javax.security.auth.login.FailedLoginException;
import java.util.Map;

import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.core.Is.is;

public class FerryTest extends WebTest {
    @Author(names = "Okta")
    @Module(name = "HomePageWeb")
    @Tribe(name = "Ferry")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyFerryIsDisplayedNonIDRGuestWeb() {
        Map<String, String> param = getParam("testVerifyFerryIsDisplayedNonIDRGuestWeb");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String currency = param.get("currency");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as guest");
        loginPage.loginAsGuest();

        step("User select " + currency + " currency");
        homePage.selectCurrency(currency);

        step("User is redirected back to home page");
        Assertion.assertThat("Verify ferry is displayed on home page", homePage.verifyVerticalIsDisplayed("Ferry"), is(true));
    }

    @Author(names = {"Okta", "Vipul Kumar"})
    @Module(name = "SRP")
    @Tribe(name = "Ferry")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyOpenImageOnFerrySRPGuest() {
        Map<String, String> param = getParam("testVerifyOpenImageOnFerrySRPGuest");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String departurePort = param.get("departurePort");
        String destinationPort = param.get("destinationPort");
        String departureDate = param.get("departureDate");
        String checkInTime = param.get("checkInTime");
        String ticketType = param.get("ticketType");
        int adultPassenger = Integer.parseInt(param.get("adultPassenger"));
        int childPassenger, infantPassenger, elderPassenger;
        childPassenger = infantPassenger = elderPassenger = 0;

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        FerrySearchPageWeb<?> ferrySearchPageWeb = FerrySearchPageWeb.getInstance(session);
        FerrySearchResultPageWeb<?> ferrySearchResultPageWeb = FerrySearchResultPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as guest");
        loginPage.loginAsGuest();

        step("User open ferry vertical");
        homePage.openVerticalPageViaURL("ferry");

        step("User click skip button");
        ferrySearchPageWeb.clickSkipButton();

        step("User select departure port");
        ferrySearchPageWeb.clickDeparturePortInput();
        ferrySearchPageWeb.selectPort(departurePort);

        step("User select destination port");
        ferrySearchPageWeb.clickDestinationPortInput();
        ferrySearchPageWeb.selectPort(destinationPort);

        step("User select date");
        ferrySearchPageWeb.clickDateInput();
        ferrySearchPageWeb.selectDate(departureDate);

        step("User select check-in time");
        ferrySearchPageWeb.clickCheckInTimeInput();
        ferrySearchPageWeb.selectCheckInTime(checkInTime);

        step("User select ticket type");
        ferrySearchPageWeb.clickTicketTypeInput();
        ferrySearchPageWeb.selectTicketType(ticketType);

        step("User select number of passengers");
        ferrySearchPageWeb.clickPassengerInput();
        ferrySearchPageWeb.setPassenger("adult", adultPassenger);
        ferrySearchPageWeb.setPassenger("child", childPassenger);
        ferrySearchPageWeb.setPassenger("infant", infantPassenger);
        ferrySearchPageWeb.setPassenger("elder", elderPassenger);
        ferrySearchPageWeb.savePassenger();

        step("User search ferry");
        ferrySearchPageWeb.searchFerry();

        step("Verify user is able to open image on each SRP card");
        ferrySearchResultPageWeb.verifyOpenImage();
    }

    @Author(names = "Okta")
    @Module(name = "SRP")
    @Tribe(name = "Ferry")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyPriceDetailOnFerrySRPGuestWeb() {
        Map<String, String> param = getParam("testVerifyPriceDetailOnFerrySRPGuestWeb");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String departurePort = param.get("departurePort");
        String destinationPort = param.get("destinationPort");
        String departureDate = param.get("departureDate");
        String checkInTime = param.get("checkInTime");
        String ticketType = param.get("ticketType");
        int adultPassenger = Integer.parseInt(param.get("adultPassenger"));
        int childPassenger, infantPassenger, elderPassenger;
        childPassenger = infantPassenger = elderPassenger = 0;

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        FerrySearchPageWeb<?> ferrySearchPageWeb = FerrySearchPageWeb.getInstance(session);
        FerrySearchResultPageWeb<?> ferrySearchResultPageWeb = FerrySearchResultPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as guest");
        loginPage.loginAsGuest();

        step("User select IDR currency");
        homePage.selectCurrency("IDR");

        step("User open ferry vertical");
        homePage.openVerticalPageViaURL("ferry");

        step("User click skip button");
        ferrySearchPageWeb.clickSkipButton();

        step("User select departure port");
        ferrySearchPageWeb.clickDeparturePortInput();
        ferrySearchPageWeb.selectPort(departurePort);

        step("User select destination port");
        ferrySearchPageWeb.clickDestinationPortInput();
        ferrySearchPageWeb.selectPort(destinationPort);

        step("User select date");
        ferrySearchPageWeb.clickDateInput();
        ferrySearchPageWeb.selectDate(departureDate);

        step("User select check-in time");
        ferrySearchPageWeb.clickCheckInTimeInput();
        ferrySearchPageWeb.selectCheckInTime(checkInTime);

        step("User select ticket type");
        ferrySearchPageWeb.clickTicketTypeInput();
        ferrySearchPageWeb.selectTicketType(ticketType);

        step("User select number of passengers");
        ferrySearchPageWeb.clickPassengerInput();
        ferrySearchPageWeb.setPassenger("adult", adultPassenger);
        ferrySearchPageWeb.setPassenger("child", childPassenger);
        ferrySearchPageWeb.setPassenger("infant", infantPassenger);
        ferrySearchPageWeb.setPassenger("elder", elderPassenger);
        ferrySearchPageWeb.savePassenger();

        step("User search ferry");
        ferrySearchPageWeb.searchFerry();

        step("Verify price details are displayed correctly on each SRP card");
        ferrySearchResultPageWeb.verifyPriceDetail();
    }

    @Author(names = {"Okta", "Clement"})
    @Module(name = "SRP")
    @Tribe(name = "Ferry")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyChangeSearchOnFerrySRPGuest() {
        Map<String, String> param = getParam("testVerifyChangeSearchOnFerrySRPGuest");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String departurePort = param.get("departurePort");
        String destinationPort = param.get("destinationPort");
        String departureDate = param.get("departureDate");
        String checkInTime = param.get("checkInTime");
        String ticketType = param.get("ticketType");
        int adultPassenger = Integer.parseInt(param.get("adultPassenger"));
        int childPassenger, infantPassenger, elderPassenger;
        childPassenger = infantPassenger = elderPassenger = 0;

        String newDeparturePort = param.get("newDeparturePort");
        String newDestinationPort = param.get("newDestinationPort");
        String newCheckInTime = param.get("newCheckInTime");
        String newDepartureDate = param.get("newDepartureDate");
        String newTicketType = param.get("newTicketType");
        int newAdultPassenger = Integer.parseInt(param.get("newAdultPassenger"));
        int newChildPassenger = Integer.parseInt(param.get("newChildPassenger"));
        int newTotalPassenger = newAdultPassenger + newChildPassenger;

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        FerrySearchPageWeb<?> ferrySearchPageWeb = FerrySearchPageWeb.getInstance(session);
        FerrySearchResultPageWeb<?> ferrySearchResultPageWeb = FerrySearchResultPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as guest");
        loginPage.loginAsGuest();

        step("User open ferry vertical");
        homePage.openVerticalPageViaURL("ferry");

        step("User click skip button");
        ferrySearchPageWeb.clickSkipButton();

        step("User select departure port");
        ferrySearchPageWeb.clickDeparturePortInput();
        ferrySearchPageWeb.selectPort(departurePort);

        step("User select destination port");
        ferrySearchPageWeb.clickDestinationPortInput();
        ferrySearchPageWeb.selectPort(destinationPort);

        step("User select date");
        ferrySearchPageWeb.clickDateInput();
        ferrySearchPageWeb.selectDate(departureDate);

        step("User select check-in time");
        ferrySearchPageWeb.clickCheckInTimeInput();
        ferrySearchPageWeb.selectCheckInTime(checkInTime);

        step("User select ticket type");
        ferrySearchPageWeb.clickTicketTypeInput();
        ferrySearchPageWeb.selectTicketType(ticketType);

        step("User select number of passengers");
        ferrySearchPageWeb.clickPassengerInput();
        ferrySearchPageWeb.setPassenger("adult", adultPassenger);
        ferrySearchPageWeb.setPassenger("child", childPassenger);
        ferrySearchPageWeb.setPassenger("infant", infantPassenger);
        ferrySearchPageWeb.setPassenger("elder", elderPassenger);
        ferrySearchPageWeb.savePassenger();

        step("User search ferry");
        ferrySearchPageWeb.searchFerry();

        if (Configs.PLATFORM != Platform.DWEB) {
            ferrySearchResultPageWeb.clickEditSearchButton();
        }

        step("User change departure port");
        ferrySearchResultPageWeb.changeDeparturePort();
        ferrySearchPageWeb.selectPort(newDeparturePort);

        step("User change destination port");
        ferrySearchResultPageWeb.changeDestinationPort();
        ferrySearchPageWeb.selectPort(newDestinationPort);

        step("User change check-in time");
        ferrySearchResultPageWeb.changeCheckInTime();
        ferrySearchPageWeb.selectCheckInTime(newCheckInTime);

        step("User change date time");
        ferrySearchResultPageWeb.changeDateTime();
        ferrySearchPageWeb.selectDate(newDepartureDate);

        step("User change ticket type");
        ferrySearchResultPageWeb.changeTicketType();
        ferrySearchPageWeb.selectTicketType(newTicketType);

        step("User change number of passengers");
        ferrySearchResultPageWeb.changePassenger();
        ferrySearchPageWeb.setPassenger("adult", newAdultPassenger);
        ferrySearchPageWeb.setPassenger("child", newChildPassenger);
        ferrySearchPageWeb.setPassenger("infant", infantPassenger);
        ferrySearchPageWeb.setPassenger("elder", elderPassenger);
        ferrySearchPageWeb.savePassenger();

        step("Verify user is able to change search on ferry SRP");
        ferrySearchResultPageWeb.changeSearchFerry();
        sleep(5);
        ferrySearchResultPageWeb.verifyChangeSearch(newDeparturePort, newDestinationPort, newCheckInTime, newTotalPassenger, newDepartureDate, newTicketType);
    }

    @Author(names = "Okta")
    @Module(name = "BookingForm")
    @Tribe(name = "Ferry")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyVehicleTnCOnFerryBookingFormGuestWeb() {
        Map<String, String> param = getParam("testVerifyVehicleTnCOnFerryBookingFormGuestWeb");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String departurePort = param.get("departurePort");
        String destinationPort = param.get("destinationPort");
        String departureDate = param.get("departureDate");
        String checkInTime = param.get("checkInTime");
        String ticketType = param.get("ticketType");
        int adultPassenger = Integer.parseInt(param.get("adultPassenger"));
        int childPassenger, infantPassenger, elderPassenger;
        childPassenger = infantPassenger = elderPassenger = 0;

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        FerrySearchPageWeb<?> ferrySearchPageWeb = FerrySearchPageWeb.getInstance(session);
        FerrySearchResultPageWeb<?> ferrySearchResultPageWeb = FerrySearchResultPageWeb.getInstance(session);
        FerryBookingPageWeb<?> ferryBookingPageWeb = FerryBookingPageWeb.getInstance(session);
      
        step("User open home page");
        homePage.goToHomePage();

        step("User open as guest");
        loginPage.loginAsGuest();

        step("User select IDR currency");
        homePage.selectCurrency("IDR");

        step("User open ferry vertical");
        homePage.openVerticalPageViaURL("ferry");

        step("User click skip button");
        ferrySearchPageWeb.clickSkipButton();

        step("User select departure port");
        ferrySearchPageWeb.clickDeparturePortInput();
        ferrySearchPageWeb.selectPort(departurePort);

        step("User select destination port");
        ferrySearchPageWeb.clickDestinationPortInput();
        ferrySearchPageWeb.selectPort(destinationPort);

        step("User select date");
        ferrySearchPageWeb.clickDateInput();
        ferrySearchPageWeb.selectDate(departureDate);

        step("User select check-in time");
        ferrySearchPageWeb.clickCheckInTimeInput();
        ferrySearchPageWeb.selectCheckInTime(checkInTime);

        step("User select ticket type");
        ferrySearchPageWeb.clickTicketTypeInput();
        ferrySearchPageWeb.selectTicketType(ticketType);

        step("User select number of passengers");
        ferrySearchPageWeb.clickPassengerInput();
        ferrySearchPageWeb.setPassenger("adult", adultPassenger);
        ferrySearchPageWeb.setPassenger("child", childPassenger);
        ferrySearchPageWeb.setPassenger("infant", infantPassenger);
        ferrySearchPageWeb.setPassenger("elder", elderPassenger);
        ferrySearchPageWeb.savePassenger();

        step("User search ferry");
        ferrySearchPageWeb.searchFerry();

        step("User select first SRP card");
        ferrySearchResultPageWeb.selectFirstSrpCard();

        step("User click terms and conditions link on booking form");
        ferryBookingPageWeb.clickTncPopUp();

        step("User close terms and conditions pop up on booking form");
        ferryBookingPageWeb.closeTncPopUp();
    }

    @Author(names = {"Okta", "Vipul Kumar"})
    @Module(name = "HomePageWeb")
    @Tribe(name = "Ferry")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyFerryIsDisplayedNonIDRLoginWeb() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testVerifyFerryIsDisplayedNonIDRLoginWeb");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String currency = param.get("currency");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(username, password, otp, retryCounter);

        step("User select " + currency + " currency");
        homePage.selectCurrency(currency);

        step("User is redirected back to home page");
        Assertion.assertThat("Verify ferry is displayed on home page", homePage.verifyVerticalIsDisplayed("Ferry"), is(true));
    }

    @Author(names = {"Okta", "Vipul Kumar", "Clement"})
    @Module(name = "SRP")
    @Tribe(name = "Ferry")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyOpenImageOnFerrySRPLogin() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testVerifyOpenImageOnFerrySRPLogin");
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
        String ticketType = param.get("ticketType");
        int adultPassenger = Integer.parseInt(param.get("adultPassenger"));
        int childPassenger, infantPassenger, elderPassenger;
        childPassenger = infantPassenger = elderPassenger = 0;

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        FerrySearchPageWeb<?> ferrySearchPageWeb = FerrySearchPageWeb.getInstance(session);
        FerrySearchResultPageWeb<?> ferrySearchResultPageWeb = FerrySearchResultPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(username, password, otp, retryCounter);

        step("User open ferry vertical");
        homePage.openVerticalPageViaURL("ferry");

        step("User click skip button");
        ferrySearchPageWeb.clickSkipButton();

        step("User select departure port");
        ferrySearchPageWeb.clickDeparturePortInput();
        ferrySearchPageWeb.selectPort(departurePort);

        step("User select destination port");
        ferrySearchPageWeb.clickDestinationPortInput();
        ferrySearchPageWeb.selectPort(destinationPort);

        step("User select date");
        ferrySearchPageWeb.clickDateInput();
        ferrySearchPageWeb.selectDate(departureDate);

        step("User select check-in time");
        ferrySearchPageWeb.clickCheckInTimeInput();
        ferrySearchPageWeb.selectCheckInTime(checkInTime);

        step("User select ticket type");
        ferrySearchPageWeb.clickTicketTypeInput();
        ferrySearchPageWeb.selectTicketType(ticketType);

        step("User select number of passengers");
        ferrySearchPageWeb.clickPassengerInput();
        ferrySearchPageWeb.setPassenger("adult", adultPassenger);
        ferrySearchPageWeb.setPassenger("child", childPassenger);
        ferrySearchPageWeb.setPassenger("infant", infantPassenger);
        ferrySearchPageWeb.setPassenger("elder", elderPassenger);
        ferrySearchPageWeb.savePassenger();

        step("User search ferry");
        ferrySearchPageWeb.searchFerry();

        step("Verify user is able to open image on each SRP card");
        ferrySearchResultPageWeb.verifyOpenImage();
    }

    @Author(names = "Okta")
    @Module(name = "SRP")
    @Tribe(name = "Ferry")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyPriceDetailOnFerrySRPLoginWeb() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testVerifyPriceDetailOnFerrySRPLoginWeb");
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
        String ticketType = param.get("ticketType");
        int adultPassenger = Integer.parseInt(param.get("adultPassenger"));
        int childPassenger, infantPassenger, elderPassenger;
        childPassenger = infantPassenger = elderPassenger = 0;

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        FerrySearchPageWeb<?> ferrySearchPageWeb = FerrySearchPageWeb.getInstance(session);
        FerrySearchResultPageWeb<?> ferrySearchResultPageWeb = FerrySearchResultPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(username, password, otp, retryCounter);

        step("User select IDR currency");
        homePage.selectCurrency("IDR");

        step("User open ferry vertical");
        homePage.openVerticalPageViaURL("ferry");

        step("User click skip button");
        ferrySearchPageWeb.clickSkipButton();

        step("User select departure port");
        ferrySearchPageWeb.clickDeparturePortInput();
        ferrySearchPageWeb.selectPort(departurePort);

        step("User select destination port");
        ferrySearchPageWeb.clickDestinationPortInput();
        ferrySearchPageWeb.selectPort(destinationPort);

        step("User select date");
        ferrySearchPageWeb.clickDateInput();
        ferrySearchPageWeb.selectDate(departureDate);

        step("User select check-in time");
        ferrySearchPageWeb.clickCheckInTimeInput();
        ferrySearchPageWeb.selectCheckInTime(checkInTime);

        step("User select ticket type");
        ferrySearchPageWeb.clickTicketTypeInput();
        ferrySearchPageWeb.selectTicketType(ticketType);

        step("User select number of passengers");
        ferrySearchPageWeb.clickPassengerInput();
        ferrySearchPageWeb.setPassenger("adult", adultPassenger);
        ferrySearchPageWeb.setPassenger("child", childPassenger);
        ferrySearchPageWeb.setPassenger("infant", infantPassenger);
        ferrySearchPageWeb.setPassenger("elder", elderPassenger);
        ferrySearchPageWeb.savePassenger();

        step("User search ferry");
        ferrySearchPageWeb.searchFerry();

        step("Verify price details are displayed correctly on each SRP card");
        ferrySearchResultPageWeb.verifyPriceDetail();
    }

    @Author(names = {"Okta", "Clement"})
    @Module(name = "SRP")
    @Tribe(name = "Ferry")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyChangeSearchOnFerrySRPLogin() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testVerifyChangeSearchOnFerrySRPLogin");
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
        String ticketType = param.get("ticketType");
        int adultPassenger = Integer.parseInt(param.get("adultPassenger"));
        int childPassenger, infantPassenger, elderPassenger;
        childPassenger = infantPassenger = elderPassenger = 0;

        String newDeparturePort = param.get("newDeparturePort");
        String newDestinationPort = param.get("newDestinationPort");
        String newCheckInTime = param.get("newCheckInTime");
        String newDepartureDate = param.get("newDepartureDate");
        String newTicketType = param.get("newTicketType");
        int newAdultPassenger = Integer.parseInt(param.get("newAdultPassenger"));
        int newChildPassenger = Integer.parseInt(param.get("newChildPassenger"));
        int newTotalPassenger = newAdultPassenger + newChildPassenger;

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        FerrySearchPageWeb<?> ferrySearchPageWeb = FerrySearchPageWeb.getInstance(session);
        FerrySearchResultPageWeb<?> ferrySearchResultPageWeb = FerrySearchResultPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(username, password, otp, retryCounter);

        step("User open ferry vertical");
        homePage.openVerticalPageViaURL("ferry");

        step("User click skip button");
        ferrySearchPageWeb.clickSkipButton();

        step("User select departure port");
        ferrySearchPageWeb.clickDeparturePortInput();
        ferrySearchPageWeb.selectPort(departurePort);

        step("User select destination port");
        ferrySearchPageWeb.clickDestinationPortInput();
        ferrySearchPageWeb.selectPort(destinationPort);

        step("User select date");
        ferrySearchPageWeb.clickDateInput();
        ferrySearchPageWeb.selectDate(departureDate);

        step("User select check-in time");
        ferrySearchPageWeb.clickCheckInTimeInput();
        ferrySearchPageWeb.selectCheckInTime(checkInTime);

        step("User select ticket type");
        ferrySearchPageWeb.clickTicketTypeInput();
        ferrySearchPageWeb.selectTicketType(ticketType);

        step("User select number of passengers");
        ferrySearchPageWeb.clickPassengerInput();
        ferrySearchPageWeb.setPassenger("adult", adultPassenger);
        ferrySearchPageWeb.setPassenger("child", childPassenger);
        ferrySearchPageWeb.setPassenger("infant", infantPassenger);
        ferrySearchPageWeb.setPassenger("elder", elderPassenger);
        ferrySearchPageWeb.savePassenger();

        step("User search ferry");
        ferrySearchPageWeb.searchFerry();

        if (Configs.PLATFORM != Platform.DWEB) {
            ferrySearchResultPageWeb.clickEditSearchButton();
        }

        step("User change departure port");
        ferrySearchResultPageWeb.changeDeparturePort();
        ferrySearchPageWeb.selectPort(newDeparturePort);

        step("User change destination port");
        ferrySearchResultPageWeb.changeDestinationPort();
        ferrySearchPageWeb.selectPort(newDestinationPort);

        step("User change check-in time");
        ferrySearchResultPageWeb.changeCheckInTime();
        ferrySearchPageWeb.selectCheckInTime(newCheckInTime);

        step("User change date time");
        ferrySearchResultPageWeb.changeDateTime();
        ferrySearchPageWeb.selectDate(newDepartureDate);

        step("User change ticket type");
        ferrySearchResultPageWeb.changeTicketType();
        ferrySearchPageWeb.selectTicketType(newTicketType);

        step("User change number of passengers");
        ferrySearchResultPageWeb.changePassenger();
        ferrySearchPageWeb.setPassenger("adult", newAdultPassenger);
        ferrySearchPageWeb.setPassenger("child", newChildPassenger);
        ferrySearchPageWeb.setPassenger("infant", infantPassenger);
        ferrySearchPageWeb.setPassenger("elder", elderPassenger);
        ferrySearchPageWeb.savePassenger();

        step("Verify user is able to change search on ferry SRP");
        ferrySearchResultPageWeb.changeSearchFerry();
        sleep(5);
        ferrySearchResultPageWeb.verifyChangeSearch(newDeparturePort, newDestinationPort, newCheckInTime, newTotalPassenger, newDepartureDate, newTicketType);
    }

    @Author(names = "Okta")
    @Module(name = "BookingForm")
    @Tribe(name = "Ferry")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyVehicleTnCOnFerryBookingFormLoginWeb() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testVerifyVehicleTnCOnFerryBookingFormLoginWeb");
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
        String ticketType = param.get("ticketType");
        int adultPassenger = Integer.parseInt(param.get("adultPassenger"));
        int childPassenger, infantPassenger, elderPassenger;
        childPassenger = infantPassenger = elderPassenger = 0;

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        FerrySearchPageWeb<?> ferrySearchPageWeb = FerrySearchPageWeb.getInstance(session);
        FerrySearchResultPageWeb<?> ferrySearchResultPageWeb = FerrySearchResultPageWeb.getInstance(session);
        FerryBookingPageWeb<?> ferryBookingPageWeb = FerryBookingPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(username, password, otp, retryCounter);

        step("User select IDR currency");
        homePage.selectCurrency("IDR");

        step("User open ferry vertical");
        homePage.openVerticalPageViaURL("ferry");

        step("User click skip button");
        ferrySearchPageWeb.clickSkipButton();

        step("User select departure port");
        ferrySearchPageWeb.clickDeparturePortInput();
        ferrySearchPageWeb.selectPort(departurePort);

        step("User select destination port");
        ferrySearchPageWeb.clickDestinationPortInput();
        ferrySearchPageWeb.selectPort(destinationPort);

        step("User select date");
        ferrySearchPageWeb.clickDateInput();
        ferrySearchPageWeb.selectDate(departureDate);

        step("User select check-in time");
        ferrySearchPageWeb.clickCheckInTimeInput();
        ferrySearchPageWeb.selectCheckInTime(checkInTime);

        step("User select ticket type");
        ferrySearchPageWeb.clickTicketTypeInput();
        ferrySearchPageWeb.selectTicketType(ticketType);

        step("User select number of passengers");
        ferrySearchPageWeb.clickPassengerInput();
        ferrySearchPageWeb.setPassenger("adult", adultPassenger);
        ferrySearchPageWeb.setPassenger("child", childPassenger);
        ferrySearchPageWeb.setPassenger("infant", infantPassenger);
        ferrySearchPageWeb.setPassenger("elder", elderPassenger);
        ferrySearchPageWeb.savePassenger();

        step("User search ferry");
        ferrySearchPageWeb.searchFerry();

        step("User select first SRP card");
        ferrySearchResultPageWeb.selectFirstSrpCard();

        step("User click terms and conditions link on booking form");
        ferryBookingPageWeb.clickTncPopUp();

        step("User close terms and conditions pop up on booking form");
        ferryBookingPageWeb.closeTncPopUp();
    }

    @Author(names = {"Okta", "Clement"})
    @Module(name = "CompleteBooking")
    @Tribe(name = "Ferry")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyFerryPriceWithCurrencyNonIDRGuest() {
        Map<String, String> param = getParam("testVerifyFerryPriceWithCurrencyNonIDRGuest");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String currency = param.get("currency");
        String departurePort = param.get("departurePort");
        String destinationPort = param.get("destinationPort");
        String departureDate = param.get("departureDate");
        String checkInTime = param.get("checkInTime");
        String ticketType = param.get("ticketType");

        int adultPassenger = Integer.parseInt(param.get("adultPassenger"));
        int childPassenger = Integer.parseInt(param.get("childPassenger"));
        int infantPassenger = Integer.parseInt(param.get("infantPassenger"));
        int elderPassenger = Integer.parseInt(param.get("elderPassenger"));

        String fullName = CoreFunctions.randomNameWithoutTest();
        String phoneNumber = "81" + CoreFunctions.randomInt(8);
        String email = CoreFunctions.randomEmail();
        String idNumberValid = param.get("idNumberValid");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        FerrySearchPageWeb<?> ferrySearchPageWeb = FerrySearchPageWeb.getInstance(session);
        FerrySearchResultPageWeb<?> ferrySearchResultPageWeb = FerrySearchResultPageWeb.getInstance(session);
        FerryBookingPageWeb<?> ferryBookingPageWeb = FerryBookingPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);

        step("User open home page");
        homePage.goToHomePage(); 

        step("User open as guest");
        loginPage.loginAsGuest();

        step("User select " + currency + " currency");
        homePage.selectCurrency(currency);

        step("User open ferry vertical via deeplink");
        homePage.openVerticalPageViaURL("ferry");

        step("User click skip button");
        ferrySearchPageWeb.clickSkipButton();
        if (Configs.PLATFORM == Platform.DWEB) {
            String searchPageCurrency = homePage.getCurrentCurrency();
            Assertion.assertThat("Verify currency in search page is set to IDR", searchPageCurrency.equals("IDR"), is(true));
        }

        step("User select departure port");
        ferrySearchPageWeb.clickDeparturePortInput();
        ferrySearchPageWeb.selectPort(departurePort);

        step("User select destination port");
        ferrySearchPageWeb.clickDestinationPortInput();
        ferrySearchPageWeb.selectPort(destinationPort);

        step("User select date");
        ferrySearchPageWeb.clickDateInput();
        ferrySearchPageWeb.selectDate(departureDate);

        step("User select check-in time");
        ferrySearchPageWeb.clickCheckInTimeInput();
        ferrySearchPageWeb.selectCheckInTime(checkInTime);

        step("User select ticket type");
        ferrySearchPageWeb.clickTicketTypeInput();
        ferrySearchPageWeb.selectTicketType(ticketType);

        step("User select number of passengers");
        ferrySearchPageWeb.clickPassengerInput();
        ferrySearchPageWeb.setPassenger("adult", adultPassenger);
        ferrySearchPageWeb.setPassenger("child", childPassenger);
        ferrySearchPageWeb.setPassenger("infant", infantPassenger);
        ferrySearchPageWeb.setPassenger("elder", elderPassenger);
        ferrySearchPageWeb.savePassenger();

        step("User search ferry");
        ferrySearchPageWeb.searchFerry();
        Assertion.assertThat("Verify prices in SRP card are in IDR", ferrySearchResultPageWeb.verifySrpPriceInIDR(), is(true));

        step("User select first SRP card");
        ferrySearchResultPageWeb.selectFirstSrpCard();
        Assertion.assertThat("Verify prices in booking form are in IDR", ferryBookingPageWeb.verifyBookingPriceInIDR(), is(true));

        step("User input contact detail");
        ferryBookingPageWeb.fillContactDetail(fullName, phoneNumber, email);

        step("User input passenger detail");
        ferryBookingPageWeb.fillPassengerDetail(adultPassenger, childPassenger, infantPassenger, elderPassenger, idNumberValid, "random");

        step("User check terms and conditions");
        ferryBookingPageWeb.clickTncCheckBox();

        step("User continue to payment page");
        ferryBookingPageWeb.continueToPaymentPage();

        step("User allow location");
        ferryBookingPageWeb.clickAllowLocation();

        step("Verify price in payment page");
        String orderID = paymentPage.getOrderId();
        Assertion.assertThat("Verify order ID is displayed in payment page", !orderID.isEmpty(), is(true));
        Assertion.assertThat("Verify price in payment page is in IDR", paymentPage.verifyTotalPaymentPriceInIDR(), is(true));
    }

    @Author(names = {"Okta", "Clement"})
    @Module(name = "CompleteBooking")
    @Tribe(name = "Ferry")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyFerryPriceWithCurrencyNonIDRLogin() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testVerifyFerryPriceWithCurrencyNonIDRLogin");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String currency = param.get("currency");

        String departurePort = param.get("departurePort");
        String destinationPort = param.get("destinationPort");
        String departureDate = param.get("departureDate");
        String checkInTime = param.get("checkInTime");
        String ticketType = param.get("ticketType");

        int adultPassenger = Integer.parseInt(param.get("adultPassenger"));
        int childPassenger = Integer.parseInt(param.get("childPassenger"));
        int infantPassenger = Integer.parseInt(param.get("infantPassenger"));
        int elderPassenger = Integer.parseInt(param.get("elderPassenger"));
        
        String idNumberValid = param.get("idNumberValid");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        FerrySearchPageWeb<?> ferrySearchPageWeb = FerrySearchPageWeb.getInstance(session);
        FerrySearchResultPageWeb<?> ferrySearchResultPageWeb = FerrySearchResultPageWeb.getInstance(session);
        FerryBookingPageWeb<?> ferryBookingPageWeb = FerryBookingPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(username, password, otp, retryCounter);

        step("User select " + currency + " currency");
        homePage.selectCurrency(currency);

        step("User open ferry vertical via deeplink");
        homePage.openVerticalPageViaURL("ferry");

        step("User click skip button");
        ferrySearchPageWeb.clickSkipButton();
        if (Configs.PLATFORM == Platform.DWEB) {
            String searchPageCurrency = homePage.getCurrentCurrency();
            Assertion.assertThat("Verify currency in search page is set to IDR", searchPageCurrency.equals("IDR"), is(true));
        }

        step("User select departure port");
        ferrySearchPageWeb.clickDeparturePortInput();
        ferrySearchPageWeb.selectPort(departurePort);

        step("User select destination port");
        ferrySearchPageWeb.clickDestinationPortInput();
        ferrySearchPageWeb.selectPort(destinationPort);

        step("User select date");
        ferrySearchPageWeb.clickDateInput();
        ferrySearchPageWeb.selectDate(departureDate);

        step("User select check-in time");
        ferrySearchPageWeb.clickCheckInTimeInput();
        ferrySearchPageWeb.selectCheckInTime(checkInTime);

        step("User select ticket type");
        ferrySearchPageWeb.clickTicketTypeInput();
        ferrySearchPageWeb.selectTicketType(ticketType);

        step("User select number of passengers");
        ferrySearchPageWeb.clickPassengerInput();
        ferrySearchPageWeb.setPassenger("adult", adultPassenger);
        ferrySearchPageWeb.setPassenger("child", childPassenger);
        ferrySearchPageWeb.setPassenger("infant", infantPassenger);
        ferrySearchPageWeb.setPassenger("elder", elderPassenger);
        ferrySearchPageWeb.savePassenger();

        step("User search ferry");
        ferrySearchPageWeb.searchFerry();
        Assertion.assertThat("Verify prices in SRP card are in IDR", ferrySearchResultPageWeb.verifySrpPriceInIDR(), is(true));

        step("User select first SRP card");
        ferrySearchResultPageWeb.selectFirstSrpCard();
        Assertion.assertThat("Verify prices in booking form are in IDR", ferryBookingPageWeb.verifyBookingPriceInIDR(), is(true));

        step("User input passenger detail");
        ferryBookingPageWeb.fillPassengerDetail(adultPassenger, childPassenger, infantPassenger, elderPassenger, idNumberValid, "random");

        step("User check terms and conditions");
        ferryBookingPageWeb.clickTncCheckBox();

        step("User continue to payment page");
        ferryBookingPageWeb.continueToPaymentPage();

        step("User allow location");
        ferryBookingPageWeb.clickAllowLocation();

        step("Verify price in payment page");
        String orderID = paymentPage.getOrderId();
        Assertion.assertThat("Verify order ID is displayed in payment page", !orderID.isEmpty(), is(true));
        Assertion.assertThat("Verify price in payment page is in IDR", paymentPage.verifyTotalPaymentPriceInIDR(), is(true));
    }

    @Author(names = {"Okta", "Clement"})
    @Module(name = "CompleteBooking")
    @Tribe(name = "Ferry")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyFerryBookingAdultsPassengerCarWithVaccineGuest() {
        Map<String, String> param = getParam("testVerifyFerryBookingAdultsPassengerCarWithVaccineGuest");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String departurePort = param.get("departurePort");
        String destinationPort = param.get("destinationPort");
        String departureDate = param.get("departureDate");
        String checkInTime = param.get("checkInTime");
        int adultPassenger = Integer.parseInt(param.get("adultPassenger"));
        int childPassenger, infantPassenger, elderPassenger;
        childPassenger = infantPassenger = elderPassenger = 0;

        String fullName = CoreFunctions.randomNameWithoutTest();
        String phoneNumber = "81" + CoreFunctions.randomInt(8);
        String email = CoreFunctions.randomEmail();
        String idNumberValid = param.get("idNumberValid");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        FerrySearchPageWeb<?> ferrySearchPageWeb = FerrySearchPageWeb.getInstance(session);
        FerrySearchResultPageWeb<?> ferrySearchResultPageWeb = FerrySearchResultPageWeb.getInstance(session);
        FerryBookingPageWeb<?> ferryBookingPageWeb = FerryBookingPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as guest");
        loginPage.loginAsGuest();

        step("User select IDR currency");
        homePage.selectCurrency("IDR");

        step("User open ferry vertical");
        homePage.openVerticalPageViaURL("ferry");

        step("User click skip button");
        ferrySearchPageWeb.clickSkipButton();

        step("User select departure port");
        ferrySearchPageWeb.clickDeparturePortInput();
        ferrySearchPageWeb.selectPort(departurePort);

        step("User select destination port");
        ferrySearchPageWeb.clickDestinationPortInput();
        ferrySearchPageWeb.selectPort(destinationPort);

        step("User select date");
        ferrySearchPageWeb.clickDateInput();
        ferrySearchPageWeb.selectDate(departureDate);

        step("User select check-in time");
        ferrySearchPageWeb.clickCheckInTimeInput();
        ferrySearchPageWeb.selectCheckInTime(checkInTime);

        step("User select ticket type");
        ferrySearchPageWeb.clickTicketTypeInput();
        ferrySearchPageWeb.selectTicketType("Passenger Car");

        step("User select number of passengers");
        ferrySearchPageWeb.clickPassengerInput();
        ferrySearchPageWeb.setPassenger("adult", adultPassenger);
        ferrySearchPageWeb.setPassenger("child", childPassenger);
        ferrySearchPageWeb.setPassenger("infant", infantPassenger);
        ferrySearchPageWeb.setPassenger("elder", elderPassenger);
        ferrySearchPageWeb.savePassenger();

        step("User search ferry");
        ferrySearchPageWeb.searchFerry();

        step("User select first SRP card");
        ferrySearchResultPageWeb.selectFirstSrpCard();

        step("User input contact detail");
        ferryBookingPageWeb.fillContactDetail(fullName, phoneNumber, email);

        step("User input passenger detail");
        ferryBookingPageWeb.fillPassengerDetail(adultPassenger, childPassenger, infantPassenger, elderPassenger, idNumberValid, "random");

        step("User check terms and conditions");
        ferryBookingPageWeb.clickTncCheckBox();

        step("User continue to payment page");
        ferryBookingPageWeb.continueToPaymentPage();

        step("User allow location");
        ferryBookingPageWeb.clickAllowLocation();

        step("Verify order ID is shown in payment page");
        String orderID = paymentPage.getOrderId();
        Assertion.assertThat("Verify order ID is displayed in payment page", !orderID.isEmpty(), is(true));
    }

    @Author(names = {"Okta", "Clement"})
    @Module(name = "CompleteBooking")
    @Tribe(name = "Ferry")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyFerryBookingAdultPedestrianWithVaccineLogin() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testVerifyFerryBookingAdultPedestrianWithVaccineLogin");
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

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(username, password, otp, retryCounter);

        step("User select IDR currency");
        homePage.selectCurrency("IDR");

        step("User open ferry vertical");
        homePage.openVerticalPageViaURL("ferry");

        step("User click skip button");
        ferrySearchPageWeb.clickSkipButton();

        step("User select departure port");
        ferrySearchPageWeb.clickDeparturePortInput();
        ferrySearchPageWeb.selectPort(departurePort);

        step("User select destination port");
        ferrySearchPageWeb.clickDestinationPortInput();
        ferrySearchPageWeb.selectPort(destinationPort);

        step("User select date");
        ferrySearchPageWeb.clickDateInput();
        ferrySearchPageWeb.selectDate(departureDate);

        step("User select check-in time");
        ferrySearchPageWeb.clickCheckInTimeInput();
        ferrySearchPageWeb.selectCheckInTime(checkInTime);

        step("User select ticket type");
        ferrySearchPageWeb.clickTicketTypeInput();
        ferrySearchPageWeb.selectTicketType("Pedestrian");

        step("User select number of passengers");
        ferrySearchPageWeb.clickPassengerInput();
        ferrySearchPageWeb.setPassenger("adult", adultPassenger);
        ferrySearchPageWeb.setPassenger("child", childPassenger);
        ferrySearchPageWeb.setPassenger("infant", infantPassenger);
        ferrySearchPageWeb.setPassenger("elder", elderPassenger);
        ferrySearchPageWeb.savePassenger();

        step("User search ferry");
        ferrySearchPageWeb.searchFerry();

        step("User select first SRP card");
        ferrySearchResultPageWeb.selectFirstSrpCard();

        step("User input passenger detail");
        ferryBookingPageWeb.fillPassengerDetail(adultPassenger, childPassenger, infantPassenger, elderPassenger, idNumberValid, "random");

        step("User check terms and conditions");
        ferryBookingPageWeb.clickTncCheckBox();

        step("User continue to payment page");
        ferryBookingPageWeb.continueToPaymentPage();

        step("User allow location");
        ferryBookingPageWeb.clickAllowLocation();

        step("Verify order ID is shown in payment page");
        String orderID = paymentPage.getOrderId();
        Assertion.assertThat("Verify order ID is displayed in payment page", !orderID.isEmpty(), is(true));
    }

    @Author(names = {"Okta", "Clement"})
    @Module(name = "CompleteBooking")
    @Tribe(name = "Ferry")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyFerryBookingAdultChildBicycleWithVaccineGuest() {
        Map<String, String> param = getParam("testVerifyFerryBookingAdultChildBicycleWithVaccineGuest");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String departurePort = param.get("departurePort");
        String destinationPort = param.get("destinationPort");
        String departureDate = param.get("departureDate");
        String checkInTime = param.get("checkInTime");
        int adultPassenger = Integer.parseInt(param.get("adultPassenger"));
        int childPassenger = Integer.parseInt(param.get("childPassenger"));
        int infantPassenger, elderPassenger;
        infantPassenger = elderPassenger = 0;

        String fullName = CoreFunctions.randomNameWithoutTest();
        String phoneNumber = "81" + CoreFunctions.randomInt(8);
        String email = CoreFunctions.randomEmail();
        String idNumberValid = param.get("idNumberValid");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        FerrySearchPageWeb<?> ferrySearchPageWeb = FerrySearchPageWeb.getInstance(session);
        FerrySearchResultPageWeb<?> ferrySearchResultPageWeb = FerrySearchResultPageWeb.getInstance(session);
        FerryBookingPageWeb<?> ferryBookingPageWeb = FerryBookingPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as guest");
        loginPage.loginAsGuest();

        step("User select IDR currency");
        homePage.selectCurrency("IDR");

        step("User open ferry vertical");
        homePage.openVerticalPageViaURL("ferry");

        step("User click skip button");
        ferrySearchPageWeb.clickSkipButton();

        step("User select departure port");
        ferrySearchPageWeb.clickDeparturePortInput();
        ferrySearchPageWeb.selectPort(departurePort);

        step("User select destination port");
        ferrySearchPageWeb.clickDestinationPortInput();
        ferrySearchPageWeb.selectPort(destinationPort);

        step("User select date");
        ferrySearchPageWeb.clickDateInput();
        ferrySearchPageWeb.selectDate(departureDate);

        step("User select check-in time");
        ferrySearchPageWeb.clickCheckInTimeInput();
        ferrySearchPageWeb.selectCheckInTime(checkInTime);

        step("User select ticket type");
        ferrySearchPageWeb.clickTicketTypeInput();
        ferrySearchPageWeb.selectTicketType("Bicycle");

        step("User select number of passengers");
        ferrySearchPageWeb.clickPassengerInput();
        ferrySearchPageWeb.setPassenger("adult", adultPassenger);
        ferrySearchPageWeb.setPassenger("child", childPassenger);
        ferrySearchPageWeb.setPassenger("infant", infantPassenger);
        ferrySearchPageWeb.setPassenger("elder", elderPassenger);
        ferrySearchPageWeb.savePassenger();

        step("User search ferry");
        ferrySearchPageWeb.searchFerry();

        step("User select first SRP card");
        ferrySearchResultPageWeb.selectFirstSrpCard();

        step("User input contact detail");
        ferryBookingPageWeb.fillContactDetail(fullName, phoneNumber, email);

        step("User input passenger detail");
        ferryBookingPageWeb.fillPassengerDetail(adultPassenger, childPassenger, infantPassenger, elderPassenger, idNumberValid, "random");

        step("User check terms and conditions");
        ferryBookingPageWeb.clickTncCheckBox();

        step("User continue to payment page");
        ferryBookingPageWeb.continueToPaymentPage();

        step("User allow location");
        ferryBookingPageWeb.clickAllowLocation();

        step("Verify order ID is shown in payment page");
        String orderID = paymentPage.getOrderId();
        Assertion.assertThat("Verify order ID is displayed in payment page", !orderID.isEmpty(), is(true));
    }

    @Author(names = {"Okta", "Clement"})
    @Module(name = "CompleteBooking")
    @Tribe(name = "Ferry")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyFerryBookingAdultChildBicycleWithVaccineLogin() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testVerifyFerryBookingAdultChildBicycleWithVaccineLogin");
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
        int childPassenger = Integer.parseInt(param.get("childPassenger"));
        int infantPassenger, elderPassenger;
        infantPassenger = elderPassenger = 0;

        String idNumberValid = param.get("idNumberValid");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        FerrySearchPageWeb<?> ferrySearchPageWeb = FerrySearchPageWeb.getInstance(session);
        FerrySearchResultPageWeb<?> ferrySearchResultPageWeb = FerrySearchResultPageWeb.getInstance(session);
        FerryBookingPageWeb<?> ferryBookingPageWeb = FerryBookingPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(username, password, otp, retryCounter);

        step("User select IDR currency");
        homePage.selectCurrency("IDR");

        step("User open ferry vertical");
        homePage.openVerticalPageViaURL("ferry");

        step("User click skip button");
        ferrySearchPageWeb.clickSkipButton();

        step("User select departure port");
        ferrySearchPageWeb.clickDeparturePortInput();
        ferrySearchPageWeb.selectPort(departurePort);

        step("User select destination port");
        ferrySearchPageWeb.clickDestinationPortInput();
        ferrySearchPageWeb.selectPort(destinationPort);

        step("User select date");
        ferrySearchPageWeb.clickDateInput();
        ferrySearchPageWeb.selectDate(departureDate);

        step("User select check-in time");
        ferrySearchPageWeb.clickCheckInTimeInput();
        ferrySearchPageWeb.selectCheckInTime(checkInTime);

        step("User select ticket type");
        ferrySearchPageWeb.clickTicketTypeInput();
        ferrySearchPageWeb.selectTicketType("Bicycle");

        step("User select number of passengers");
        ferrySearchPageWeb.clickPassengerInput();
        ferrySearchPageWeb.setPassenger("adult", adultPassenger);
        ferrySearchPageWeb.setPassenger("child", childPassenger);
        ferrySearchPageWeb.setPassenger("infant", infantPassenger);
        ferrySearchPageWeb.setPassenger("elder", elderPassenger);
        ferrySearchPageWeb.savePassenger();

        step("User search ferry");
        ferrySearchPageWeb.searchFerry();

        step("User select first SRP card");
        ferrySearchResultPageWeb.selectFirstSrpCard();

        step("User input passenger detail");
        ferryBookingPageWeb.fillPassengerDetail(adultPassenger, childPassenger, infantPassenger, elderPassenger, idNumberValid, "random");

        step("User check terms and conditions");
        ferryBookingPageWeb.clickTncCheckBox();

        step("User continue to payment page");
        ferryBookingPageWeb.continueToPaymentPage();

        step("User allow location");
        ferryBookingPageWeb.clickAllowLocation();

        step("Verify order ID is shown in payment page");
        String orderID = paymentPage.getOrderId();
        Assertion.assertThat("Verify order ID is displayed in payment page", !orderID.isEmpty(), is(true));
    }

    @Author(names = {"Okta", "Clement"})
    @Module(name = "CompleteBooking")
    @Tribe(name = "Ferry")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyFerryOrderDetailAdultInfantLogin() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testVerifyFerryOrderDetailAdultInfantLogin");
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
        String ticketType = param.get("ticketType");
        int adultPassenger = Integer.parseInt(param.get("adultPassenger"));
        int infantPassenger = Integer.parseInt(param.get("infantPassenger"));
        int childPassenger, elderPassenger;
        childPassenger = elderPassenger = 0;

        String idNumberValid = param.get("idNumberValid");

        String paymentMethod = param.get("paymentMethod");
        String fileName = param.get("fileName");
        String timeOut = param.get("timeOut");
        String paymentType = param.get("paymentType");
        int totalPassenger = adultPassenger + childPassenger + infantPassenger + elderPassenger;

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        FerrySearchPageWeb<?> ferrySearchPageWeb = FerrySearchPageWeb.getInstance(session);
        FerrySearchResultPageWeb<?> ferrySearchResultPageWeb = FerrySearchResultPageWeb.getInstance(session);
        FerryBookingPageWeb<?> ferryBookingPageWeb = FerryBookingPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(username, password, otp, retryCounter);

        step("User select IDR currency");
        homePage.selectCurrency("IDR");

        step("User open ferry vertical");
        homePage.openVerticalPageViaURL("ferry");

        step("User click skip button");
        ferrySearchPageWeb.clickSkipButton();

        step("User select departure port");
        ferrySearchPageWeb.clickDeparturePortInput();
        ferrySearchPageWeb.selectPort(departurePort);

        step("User select destination port");
        ferrySearchPageWeb.clickDestinationPortInput();
        ferrySearchPageWeb.selectPort(destinationPort);

        step("User select date");
        ferrySearchPageWeb.clickDateInput();
        ferrySearchPageWeb.selectDate(departureDate);

        step("User select check-in time");
        ferrySearchPageWeb.clickCheckInTimeInput();
        ferrySearchPageWeb.selectCheckInTime(checkInTime);

        step("User select ticket type");
        ferrySearchPageWeb.clickTicketTypeInput();
        ferrySearchPageWeb.selectTicketType(ticketType);

        step("User select number of passengers");
        ferrySearchPageWeb.clickPassengerInput();
        ferrySearchPageWeb.setPassenger("adult", adultPassenger);
        ferrySearchPageWeb.setPassenger("child", childPassenger);
        ferrySearchPageWeb.setPassenger("infant", infantPassenger);
        ferrySearchPageWeb.setPassenger("elder", elderPassenger);
        ferrySearchPageWeb.savePassenger();

        step("User search ferry");
        ferrySearchPageWeb.searchFerry();

        step("User select first SRP card");
        String ferryClass = ferrySearchResultPageWeb.getFerryClassFirstCard();
        ferrySearchResultPageWeb.selectFirstSrpCard();

        step("User input passenger detail");
        ferryBookingPageWeb.fillPassengerDetail(adultPassenger, childPassenger, infantPassenger, elderPassenger, idNumberValid, "random");

        step("User check terms and conditions");
        ferryBookingPageWeb.clickTncCheckBox();

        step("User continue to payment page");
        ferryBookingPageWeb.continueToPaymentPage();

        step("User allow location");
        ferryBookingPageWeb.clickAllowLocation();

        step("Get order ID");
        String orderID = paymentPage.getOrderId();
        Assertion.assertThat("Verify order ID is displayed in payment page", !orderID.isEmpty(), is(true));

        step("User select payment method");
        paymentPage.clickOnSelectPaymentMethod();
        
        if (Configs.PLATFORM == Platform.DWEB) {
            paymentPage.selectPaymentMethod(paymentMethod);
        }
        else {
            paymentPage.selectPaymentMethodMWeb(paymentMethod, paymentType);
        }

        step("User complete payment");
        paymentPage.clickOnPaymentButton();

        if (Configs.ENVIRONMENT == Environment.PROD) {
            step("User open order list page");
            paymentPage.clickSeeOrderListButton();

            step("User open order detail page");
            orderListPage.openOrderDetailPage(orderID);
        }

        else if (Configs.ENVIRONMENT == Environment.GK|| Configs.ENVIRONMENT == Environment.PREPROD) {
            boolean paymentSuccess = paymentPage.isPaymentSuccessfulVisible();

            step("User open order detail page");
            orderListPage.openActiveTicketViaURL(orderID);

            if (paymentSuccess) {
                step("User check issued booking");
                orderDetailPage.verifyIssuedETicket();
                orderDetailPage.verifyIssuedQRCode();
                orderDetailPage.verifyShareETicketAndReceiptButton(fileName, timeOut);
            }
        }

        step("Verify passenger detail in order detail page");
        orderDetailPage.verifyFerryOrderPassengerDetail(ferryClass, departurePort, destinationPort, totalPassenger);
    }

    @Author(names = {"Okta", "Clement"})
    @Module(name = "CompleteBooking")
    @Tribe(name = "Ferry")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyFerryOrderDetailAdultInfantGuest() {
        Map<String, String> param = getParam("testVerifyFerryOrderDetailAdultInfantGuest");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String departurePort = param.get("departurePort");
        String destinationPort = param.get("destinationPort");
        String departureDate = param.get("departureDate");
        String checkInTime = param.get("checkInTime");
        String ticketType = param.get("ticketType");
        int adultPassenger = Integer.parseInt(param.get("adultPassenger"));
        int infantPassenger = Integer.parseInt(param.get("infantPassenger"));
        int childPassenger, elderPassenger;
        childPassenger = elderPassenger = 0;

        String fullName = CoreFunctions.randomNameWithoutTest();
        String phoneNumber = "81" + CoreFunctions.randomInt(8);
        String email = CoreFunctions.randomEmail();
        String idNumberValid = param.get("idNumberValid");
        String paymentMethod = param.get("paymentMethod");
        String paymentType = param.get("paymentType");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        FerrySearchPageWeb<?> ferrySearchPageWeb = FerrySearchPageWeb.getInstance(session);
        FerrySearchResultPageWeb<?> ferrySearchResultPageWeb = FerrySearchResultPageWeb.getInstance(session);
        FerryBookingPageWeb<?> ferryBookingPageWeb = FerryBookingPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as guest");
        loginPage.loginAsGuest();

        step("User select IDR currency");
        homePage.selectCurrency("IDR");

        step("User open ferry vertical");
        homePage.openVerticalPageViaURL("ferry");

        step("User click skip button");
        ferrySearchPageWeb.clickSkipButton();

        step("User select departure port");
        ferrySearchPageWeb.clickDeparturePortInput();
        ferrySearchPageWeb.selectPort(departurePort);

        step("User select destination port");
        ferrySearchPageWeb.clickDestinationPortInput();
        ferrySearchPageWeb.selectPort(destinationPort);

        step("User select date");
        ferrySearchPageWeb.clickDateInput();
        ferrySearchPageWeb.selectDate(departureDate);

        step("User select check-in time");
        ferrySearchPageWeb.clickCheckInTimeInput();
        ferrySearchPageWeb.selectCheckInTime(checkInTime);

        step("User select ticket type");
        ferrySearchPageWeb.clickTicketTypeInput();
        ferrySearchPageWeb.selectTicketType(ticketType);

        step("User select number of passengers");
        ferrySearchPageWeb.clickPassengerInput();
        ferrySearchPageWeb.setPassenger("adult", adultPassenger);
        ferrySearchPageWeb.setPassenger("child", childPassenger);
        ferrySearchPageWeb.setPassenger("infant", infantPassenger);
        ferrySearchPageWeb.setPassenger("elder", elderPassenger);
        ferrySearchPageWeb.savePassenger();

        step("User search ferry");
        ferrySearchPageWeb.searchFerry();

        step("User select first SRP card");
        ferrySearchResultPageWeb.selectFirstSrpCard();

        step("User input contact detail");
        ferryBookingPageWeb.fillContactDetail(fullName, phoneNumber, email);

        step("User input passenger detail");
        ferryBookingPageWeb.fillPassengerDetail(adultPassenger, childPassenger, infantPassenger, elderPassenger, idNumberValid, "random");

        step("User check terms and conditions");
        ferryBookingPageWeb.clickTncCheckBox();

        step("User continue to payment page");
        ferryBookingPageWeb.continueToPaymentPage();

        step("User allow location");
        ferryBookingPageWeb.clickAllowLocation();

        step("User select payment method");
        paymentPage.clickOnSelectPaymentMethod();
        
        if (Configs.PLATFORM == Platform.DWEB) {
            paymentPage.selectPaymentMethod(paymentMethod);
        }
        else {
            paymentPage.selectPaymentMethodMWeb(paymentMethod, paymentType);
        }

        step("User complete payment");
        paymentPage.clickOnPaymentButton();

        step("User open order list page");
        paymentPage.clickSeeOrderListButton();

        Assertion.assertThat("Verify web page will redirect to login page for guest user", loginPage.isLoginPageOpened(), is(true));
    }

    @Author(names = {"Okta", "Clement"})
    @Module(name = "BookingForm")
    @Tribe(name = "Ferry")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyFerryRejectionNotificationSameIdNumberGuest() {
        Map<String, String> param = getParam("testVerifyFerryRejectionNotificationSameIdNumberGuest");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String departurePort = param.get("departurePort");
        String destinationPort = param.get("destinationPort");
        String departureDate = param.get("departureDate");
        String checkInTime = param.get("checkInTime");
        String ticketType = param.get("ticketType");
        int adultPassenger = Integer.parseInt(param.get("adultPassenger"));
        int childPassenger, infantPassenger, elderPassenger;
        childPassenger = infantPassenger = elderPassenger = 0;

        String fullName = CoreFunctions.randomNameWithoutTest();
        String phoneNumber = "81" + CoreFunctions.randomInt(8);
        String email = CoreFunctions.randomEmail();
        String idNumberValid = param.get("idNumberValid");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        FerrySearchPageWeb<?> ferrySearchPageWeb = FerrySearchPageWeb.getInstance(session);
        FerrySearchResultPageWeb<?> ferrySearchResultPageWeb = FerrySearchResultPageWeb.getInstance(session);
        FerryBookingPageWeb<?> ferryBookingPageWeb = FerryBookingPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as guest");
        loginPage.loginAsGuest();

        step("User open ferry vertical");
        homePage.openVerticalPageViaURL("ferry");

        step("User click skip button");
        ferrySearchPageWeb.clickSkipButton();

        step("User select departure port");
        ferrySearchPageWeb.clickDeparturePortInput();
        ferrySearchPageWeb.selectPort(departurePort);

        step("User select destination port");
        ferrySearchPageWeb.clickDestinationPortInput();
        ferrySearchPageWeb.selectPort(destinationPort);

        step("User select date");
        ferrySearchPageWeb.clickDateInput();
        ferrySearchPageWeb.selectDate(departureDate);

        step("User select check-in time");
        ferrySearchPageWeb.clickCheckInTimeInput();
        ferrySearchPageWeb.selectCheckInTime(checkInTime);

        step("User select ticket type");
        ferrySearchPageWeb.clickTicketTypeInput();
        ferrySearchPageWeb.selectTicketType(ticketType);

        step("User select number of passengers");
        ferrySearchPageWeb.clickPassengerInput();
        ferrySearchPageWeb.setPassenger("adult", adultPassenger);
        ferrySearchPageWeb.setPassenger("child", childPassenger);
        ferrySearchPageWeb.setPassenger("infant", infantPassenger);
        ferrySearchPageWeb.setPassenger("elder", elderPassenger);
        ferrySearchPageWeb.savePassenger();

        step("User search ferry");
        ferrySearchPageWeb.searchFerry();

        step("User select first SRP card");
        ferrySearchResultPageWeb.selectFirstSrpCard();

        step("User input contact detail");
        ferryBookingPageWeb.fillContactDetail(fullName, phoneNumber, email);

        step("User input passenger detail");
        ferryBookingPageWeb.fillPassengerDetail(adultPassenger, childPassenger, infantPassenger, elderPassenger, idNumberValid, "same");

        step("User check terms and conditions");
        ferryBookingPageWeb.clickTncCheckBox();

        step("User continue to payment page");
        ferryBookingPageWeb.continueToPaymentPage();
        Assertion.assertThat("Verify user is not directed to payment page and stays in booking page", ferryBookingPageWeb.isBookingPageOpened(), is(true));

        step("Verify alert is displayed for passenger with same ID number");
        ferryBookingPageWeb.verifyAlertSameIdNumber();
    }

    @Author(names = {"Okta", "Clement"})
    @Module(name = "BookingForm")
    @Tribe(name = "Ferry")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyFerryRejectionNotificationSameIdNumberLogin() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testVerifyFerryRejectionNotificationSameIdNumberLogin");
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
        String ticketType = param.get("ticketType");
        int adultPassenger = Integer.parseInt(param.get("adultPassenger"));
        int childPassenger, infantPassenger, elderPassenger;
        childPassenger = infantPassenger = elderPassenger = 0;

        String idNumberValid = param.get("idNumberValid");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        FerrySearchPageWeb<?> ferrySearchPageWeb = FerrySearchPageWeb.getInstance(session);
        FerrySearchResultPageWeb<?> ferrySearchResultPageWeb = FerrySearchResultPageWeb.getInstance(session);
        FerryBookingPageWeb<?> ferryBookingPageWeb = FerryBookingPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(username, password, otp, retryCounter);

        step("User open ferry vertical");
        homePage.openVerticalPageViaURL("ferry");

        step("User click skip button");
        ferrySearchPageWeb.clickSkipButton();

        step("User select departure port");
        ferrySearchPageWeb.clickDeparturePortInput();
        ferrySearchPageWeb.selectPort(departurePort);

        step("User select destination port");
        ferrySearchPageWeb.clickDestinationPortInput();
        ferrySearchPageWeb.selectPort(destinationPort);

        step("User select date");
        ferrySearchPageWeb.clickDateInput();
        ferrySearchPageWeb.selectDate(departureDate);

        step("User select check-in time");
        ferrySearchPageWeb.clickCheckInTimeInput();
        ferrySearchPageWeb.selectCheckInTime(checkInTime);

        step("User select ticket type");
        ferrySearchPageWeb.clickTicketTypeInput();
        ferrySearchPageWeb.selectTicketType(ticketType);

        step("User select number of passengers");
        ferrySearchPageWeb.clickPassengerInput();
        ferrySearchPageWeb.setPassenger("adult", adultPassenger);
        ferrySearchPageWeb.setPassenger("child", childPassenger);
        ferrySearchPageWeb.setPassenger("infant", infantPassenger);
        ferrySearchPageWeb.setPassenger("elder", elderPassenger);
        ferrySearchPageWeb.savePassenger();

        step("User search ferry");
        ferrySearchPageWeb.searchFerry();

        step("User select first SRP card");
        ferrySearchResultPageWeb.selectFirstSrpCard();

        step("User input passenger detail");
        ferryBookingPageWeb.fillPassengerDetail(adultPassenger, childPassenger, infantPassenger, elderPassenger, idNumberValid, "same");

        step("User check terms and conditions");
        ferryBookingPageWeb.clickTncCheckBox();

        step("User continue to payment page");
        ferryBookingPageWeb.continueToPaymentPage();
        Assertion.assertThat("Verify user is not directed to payment page and stays in booking page", ferryBookingPageWeb.isBookingPageOpened(), is(true));

        step("Verify alert is displayed for passenger with same ID number");
        ferryBookingPageWeb.verifyAlertSameIdNumber();
    }
}