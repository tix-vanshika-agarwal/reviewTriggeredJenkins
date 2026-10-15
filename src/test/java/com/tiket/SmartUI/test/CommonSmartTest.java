package com.tiket.SmartUI.test;

import com.tiket.SmartUI.SmartUtils;
import com.tiket.SmartUI.page.common.CommonSmartPage;
import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Session;
import com.tiket.page.accommodation.hotel.detailpage.DetailPage;
import com.tiket.page.accommodation.hotel.landingpage.LandingPage;
import com.tiket.page.accommodation.hotel.roomlistpage.RoomListPage;
import com.tiket.page.carrental.bookingpage.CarRentalBookingPage;
import com.tiket.page.carrental.reviewbookingpage.CarRentalReviewBookingPage;
import com.tiket.page.carrental.searchpage.SearchPageCarRental;
import com.tiket.page.carrental.searchresultpage.SearchResultPageCarRental;
import com.tiket.page.common.webview.WebViewPage;
import com.tiket.page.home.HomePage;
import com.tiket.page.login.LoginPage;
import com.tiket.pojo.carrental.CarRentalDetail;
import com.tiket.testbase.BaseTest;
import com.tiket.web.smart.Common.CommonSmartPageWeb;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import java.util.Map;

import static com.tiket.testdata.TestData.getParam;

public class CommonSmartTest extends BaseTest {
    @Author(names = {"Naufal Hafiz"})
    @Module(name = "Common")
    @Tribe(name = "Common")
    @Vertical(name = "Common")
    @Test
    public void testCommonSmartGuest() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("testCommonSmartGuest");
        BaseTest.paramsThreadLocal.set(map);

        String date_type = map.get("date_type");
        String date = map.get("date");
        String hotelName = map.get("hotelName");
        int totalRoom = Integer.parseInt(map.get("total_room"));
        int totalAdult = Integer.parseInt(map.get("total_adult"));
        String currency = map.get("currency");
        String location = map.get("location");

        String pickupLocation = map.get("pickupLocation");
        String carRentalDuration = map.get("carRentalDuration");
        int duration = Integer.parseInt(carRentalDuration);
        String dateRental = CoreFunctions.getCurrentLongDate();


        CarRentalDetail carRentalDetail = new CarRentalDetail();
        carRentalDetail.duration = duration;
        String driverType = "without_driver";


        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        SearchPageCarRental<WebDriver> carRentalSearchPage = SearchPageCarRental.getInstance(session);
        SearchResultPageCarRental<WebDriver> carRentalSearchResultPage = SearchResultPageCarRental.getInstance(session);
        CarRentalReviewBookingPage<WebDriver> carRentalReviewBookingPage = CarRentalReviewBookingPage.getInstance(session);
        CarRentalBookingPage<WebDriver> carRentalBookingPage = CarRentalBookingPage.getInstance(session);
        CommonSmartPage<WebDriver> commonSmartPage = CommonSmartPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginAsGuest();

        step("Get App Language");
        String language = homePage.getLanguageApp();

        //Hotel
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
        commonSmartPage.waitForHotelLoginBanner();
        SmartUtils.takeSmartScreenshot(session.driver(), "Common_[Guest] Verify Login form on Hotel Booking Form");

        //Car Rental
        step("Open Car Landing Page");
        homePage.openVerticalPageDeeplink("Car Rentals", language, currency);
        carRentalSearchPage.allowLocationPermission();
        carRentalSearchPage.closeInformationPopUp();

        step("Select car rental type");
        carRentalSearchPage.chooseDriver(driverType);

        step("Select car rental date");
        carRentalSearchPage.selectDate(CoreFunctions.addToLongDate(dateRental,"10-20"));

        step("Input car rental location");
        carRentalSearchPage.chooseLocation(location);

        step("get selected data landing page");
        carRentalDetail.landingPageData = carRentalSearchPage.getSelectedDataLandingPage();

        step("Search car rental");
        carRentalSearchPage.searchCar(carRentalDuration);

        step("choose car");
        carRentalSearchResultPage.chooseCar(duration);

        step("continue to booking form from review page");
        carRentalReviewBookingPage.continueFromReviewDetailCar();

        step("pick up location");
        carRentalBookingPage.choosePickUpLocationAndToggleForDropOff(pickupLocation);

        step("continue to complete booking");
        carRentalBookingPage.continueToCompleteBookingPage();
        SmartUtils.takeSmartScreenshot(session.driver(), "Common_[Guest] Verify Login form on Car Rental Booking Form");

    }

    @Author(names = {"Naufal Hafiz"})
    @Module(name = "Common")
    @Tribe(name = "Common")
    @Vertical(name = "Common")
    @Test
    public void testCommonSmartLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("testCommonSmartLogin");
        BaseTest.paramsThreadLocal.set(map);

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");


        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        CommonSmartPage<WebDriver> commonSmartPage = CommonSmartPage.getInstance(session);
        CommonSmartPageWeb<WebDriver> commonSmartPageWeb = CommonSmartPageWeb.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        homePage.closePromoPopup();
        step("Open Voucher Box");
        commonSmartPage.openVoucherBox();

        webViewPage.switchToWebViewUrl("voucher-box");
        commonSmartPageWeb.waitForVoucherBox();
        SmartUtils.takeSmartScreenshot(session.driver(), "Common_[Login] Verify User can access voucher box");
    }
}
