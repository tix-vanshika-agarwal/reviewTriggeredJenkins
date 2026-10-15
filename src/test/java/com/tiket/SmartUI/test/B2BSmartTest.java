package com.tiket.SmartUI.test;

import com.tiket.SmartUI.SmartUtils;
import com.tiket.SmartUI.page.b2b.B2BSmartPage;
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
import com.tiket.testbase.BaseTest;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import java.util.Map;

import static com.tiket.testdata.TestData.getParam;

public class B2BSmartTest extends BaseTest {
    @Author(names = {"Naufal Hafiz"})
    @Module(name = "Room")
    @Tribe(name = "B2BCorp")
    @Vertical(name = "B2BCorp")
    @Test
    public void testB2BCorpSmartLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("testB2BCorpSmartLogin");
        BaseTest.paramsThreadLocal.set(map);
        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");
        String date_type = map.get("date_type");
        String date = map.get("date");
        String hotelName = map.get("hotelName");
        int totalRoom = Integer.parseInt(map.get("total_room"));
        int totalAdult = Integer.parseInt(map.get("total_adult"));
        String currency = map.get("currency");


        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);
        B2BSmartPage<WebDriver> b2BSmartPage = B2BSmartPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);
        String language = homePage.getLanguageApp();

        step("Open hotel landing page");
        homePage.openVerticalPageDeeplink("Hotel", language, currency);

        step("Search stay location");
        landingPage.searchStayLocation(hotelName);

        step("set stay date");
        landingPage.setStayDate(date_type, date);

        step("set total room and total guest");
        landingPage.clickRoomAndGuestButton();
        landingPage.setTotalRoom(totalRoom);
        landingPage.setTotalGuest(totalAdult);
        landingPage.clickDone();

        step("Submit search");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        step("Go to the room list");
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();

        step("Select room from the room list");
        roomListPage.getTheFirstHotelRoomPrice();
        roomListPage.clickBookRoom();

        step("Continue to Travel Plan page");
        bookingFormPage.continueToPaymentPage();

        step("Choose Travel Plan");
        b2BSmartPage.openTravelPlan();
        b2BSmartPage.waitForTravelPlanToBeOpened();
        SmartUtils.takeSmartScreenshot(session.driver(), "B2B_[Login] Verify travel plan bottomsheet can be opened");
        b2BSmartPage.chooseTravelPlan();
        SmartUtils.takeSmartScreenshot(session.driver(), "B2B_[Login] Verify travel plan can be chosen");

        step("Submit booking");
        bookingFormPage.continueToPaymentPage();
        bookingFormPage.PriceChangesPopUp();
        SmartUtils.takeSmartScreenshot(session.driver(), "B2B_[Login] Verify Payment page via corporate billing");
    }
}
