package com.tiket.test.prodmonitoring;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.model.Session;
import com.tiket.page.accommodation.hotel.bookingformpage.BookingFormPage;
import com.tiket.page.accommodation.hotel.detailpage.DetailPage;
import com.tiket.page.accommodation.hotel.landingpage.LandingPage;
import com.tiket.page.accommodation.hotel.roomlistpage.RoomListPage;
import com.tiket.page.accommodation.hotel.searchresultpage.SearchResultPage;
import com.tiket.page.home.HomePage;
import com.tiket.page.login.LoginPage;
import com.tiket.page.payment.PaymentPage;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import java.util.Map;

import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.Matchers.*;

public class AccommodationTest extends BaseTest {

    @Author(names = {"Ashish"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Hotel")
    @Module(name = "CreateBooking")
    @Test(priority = 1)
    public void completeBookingIdrLoginProdMonitoringHotel() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("completeBookingIdrLoginProdMonitoringHotel");
        BaseTest.paramsThreadLocal.set(map);

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);

        String location = map.get("location");
        String currency = map.get("currency");
        String dateType = map.get("date_type");
        String date = map.get("date");

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");

        step("Goto home");
        homePage.gotoHome();

        step("Login page");
        loginPage.loginWith(loginType,username,password);

        step("Home page");
        homePage.changeCurrency(currency,username);
        homePage.openVerticalPageDeeplink("Hotel", "", currency);

        step("landing page");
        landingPage.changeDateViaDeeplink(date,dateType,1,1);
        landingPage.searchStayLocation(location);
        landingPage.clickRoomAndGuestButton();
        landingPage.setTotalRoom(1);
        landingPage.setTotalGuest(1);
        landingPage.setTotalChildren(0);
        landingPage.clickDone();
        landingPage.clickSubmit();

        step("Search result page");
        searchResultPage.selectFirstHotelCard();

        step("Room detail page");
        Assertion.assertThat("Price in PDP Currency is "+currency, detailPage.getHotelPrice(), containsString(currency));
        detailPage.seeHotelRoom();

        step("room list page");
        roomListPage.clickBookRoom();
        roomListPage.reselectAnotherHotelIfFullyBooked();

        step("Booking page");
        bookingFormPage.fillGuestDetail(false);
        bookingFormPage.continueToPaymentPage();
        bookingFormPage.PriceChangesPopUp();
        bookingFormPage.closeRatePlanBottomSheet();

        step("Payment page");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id", orderID, allOf(
                not(nullValue()),
                not(equalTo(""))
        ));
    }
}
