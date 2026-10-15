package com.tiket.SmartUI.test;

import com.tiket.SmartUI.SmartUtils;
import com.tiket.SmartUI.page.hotel.HotelSmartPage;
import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.model.Environment;
import com.tiket.model.Session;
import com.tiket.page.accommodation.hotel.landingpage.LandingPage;
import com.tiket.page.common.webview.WebViewPage;
import com.tiket.page.home.HomePage;
import com.tiket.page.login.LoginPage;

import com.tiket.testbase.BaseTest;
import com.tiket.testbase.Configs;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import java.util.Map;

import static com.tiket.testdata.TestData.getParam;

public class HotelSmartTest extends BaseTest {
    @Author(names = {"Naufal Hafiz"})
    @Module(name = "Room")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void testHotelSmartGuest() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String,String> params = getParam("testHotelSmartGuest");
        BaseTest.paramsThreadLocal.set(params);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        HotelSmartPage<WebDriver> hotelSmartPage = HotelSmartPage.getInstance(session);
        LoginPage <WebDriver> loginPage = LoginPage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);

        String currency = params.get("currency");
        String location = params.get("location");
        String date_type = params.get("date_type");
        String date = params.get("date");
        String newDate = params.get("newDate");

        step("Go To Home");
        homePage.gotoHome();

        step("User uses app as guest");
        loginPage.loginAsGuest();

        if(Configs.ENVIRONMENT == Environment.PROD) {
            step("Change currency to 'IDR'");
            homePage.changeCurrency(currency,"");
        }

        step("Get App Language");
        String language = homePage.getLanguageApp();

        step("open hotel page");
        homePage.openVerticalPageDeeplink("Hotel", language, currency);

        step("edit calendar on landing page to be one day");
        hotelSmartPage.setDate("ONE_DAY","LP");
        SmartUtils.takeSmartScreenshot(session.driver(), "Hotel_[Login] Verify User cant choose same day - LP");
        hotelSmartPage.closeCalendar();

        step("edit calendar on landing page to be 30 days");
        hotelSmartPage.setDate("MORE_THAN_30_DAYS","LP");
        SmartUtils.takeSmartScreenshot(session.driver(), "Hotel_[Login] Verify User cant choose more than 30 days - LP");
        hotelSmartPage.closeCalendar();

        step("search stay location: " + location);
        landingPage.searchStayLocation(location);

        step("set stay date");
        landingPage.setStayDate(date_type, date);

        step("submit search");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        step("edit calendar on SRP to be one day");
        hotelSmartPage.openSRPSearchForm();
        hotelSmartPage.setDate("ONE_DAY","SRP");
        SmartUtils.takeSmartScreenshot(session.driver(), "Hotel_[Login] Verify User cant choose same day - SRP");
        hotelSmartPage.closeCalendar();

        step("edit calendar on SRP to be 30 days");
        hotelSmartPage.setDate("MORE_THAN_30_DAYS","SRP");
        SmartUtils.takeSmartScreenshot(session.driver(), "Hotel_[Login] Verify User cant choose more than 30 days - SRP");
        hotelSmartPage.closeCalendar();
        hotelSmartPage.closeSearchBottomsheet();

        step("set stay date on SRP");
        hotelSmartPage.openSRPSearchForm();
        landingPage.setStayDate(date_type, newDate);
        SmartUtils.takeSmartScreenshot(session.driver(), "Hotel_[Guest] Verify User can modify date on SRP");

    }

    @Author(names = {"Naufal Hafiz"})
    @Module(name = "Room")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void testHotelSmartLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String,String> params = getParam("testHotelSmartLogin");
        BaseTest.paramsThreadLocal.set(params);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        HotelSmartPage<WebDriver> hotelSmartPage = HotelSmartPage.getInstance(session);
        LoginPage <WebDriver> loginPage = LoginPage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);

        String currency = params.get("currency");
        String location = params.get("location");
        String date_type = params.get("date_type");
        String date = params.get("date");
        String newDate = params.get("newDate");

        String loginMethod = params.get("loginType");
        String username = params.get("username");
        String password = params.get("password");

        step("Go To Home");
        homePage.gotoHome();

        step("input credential");
        loginPage.loginWith(loginMethod, username, password);

        if(Configs.ENVIRONMENT == Environment.PROD) {
            step("Change currency to 'IDR'");
            homePage.changeCurrency(currency,"");
        }

        step("Get App Language");
        String language = homePage.getLanguageApp();

        step("open hotel page");
        homePage.openVerticalPageDeeplink("Hotel", language, currency);

        step("edit calendar on landing page to be one day");
        hotelSmartPage.setDate("ONE_DAY","LP");
        SmartUtils.takeSmartScreenshot(session.driver(), "Hotel_[Login] Verify User cant choose same day - LP");
        hotelSmartPage.closeCalendar();

        step("edit calendar on landing page to be 30 days");
        hotelSmartPage.setDate("MORE_THAN_30_DAYS","LP");
        SmartUtils.takeSmartScreenshot(session.driver(), "Hotel_[Login] Verify User cant choose more than 30 days - LP");
        hotelSmartPage.closeCalendar();

        step("search stay location: " + location);
        landingPage.searchStayLocation(location);

        step("set stay date");
        landingPage.setStayDate(date_type, date);

        step("submit search");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        step("edit calendar on SRP to be one day");
        hotelSmartPage.openSRPSearchForm();
        hotelSmartPage.setDate("ONE_DAY","SRP");
        SmartUtils.takeSmartScreenshot(session.driver(), "Hotel_[Login] Verify User cant choose same day - SRP");
        hotelSmartPage.closeCalendar();

        step("edit calendar on SRP to be 30 days");
        hotelSmartPage.setDate("MORE_THAN_30_DAYS","SRP");
        SmartUtils.takeSmartScreenshot(session.driver(), "Hotel_[Login] Verify User cant choose more than 30 days - SRP");
        hotelSmartPage.closeCalendar();
        hotelSmartPage.closeSearchBottomsheet();

        step("set stay date on SRP");
        hotelSmartPage.openSRPSearchForm();
        landingPage.setStayDate(date_type, newDate);
        SmartUtils.takeSmartScreenshot(session.driver(), "Hotel_[Login] Verify User can modify date on SRP");
    }
}
