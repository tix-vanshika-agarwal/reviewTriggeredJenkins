package com.tiket.SmartUI.test;

import com.tiket.SmartUI.SmartUtils;
import com.tiket.SmartUI.page.car_rental.CarRentalSmartPage;
import com.tiket.SmartUI.page.platform.PlatformSmartPage;
import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Session;

import com.tiket.page.carrental.bookingpage.CarRentalBookingPage;
import com.tiket.page.carrental.reviewbookingpage.CarRentalReviewBookingPage;
import com.tiket.page.carrental.searchpage.SearchPageCarRental;
import com.tiket.page.carrental.searchresultpage.SearchResultPageCarRental;
import com.tiket.page.home.HomePage;
import com.tiket.page.login.LoginPage;

import com.tiket.pojo.carrental.CarRentalDetail;
import com.tiket.testbase.BaseTest;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import java.util.Map;

import static com.tiket.testdata.TestData.getParam;

public class PlatformSmartTest extends BaseTest {
    @Author(names = {"Vipul Kumar"})
    @Module(name = "PlatformVisualSmart")
    @Tribe(name = "Platform")
    @Vertical(name = "Core")
    @Test
    public void testPlatformSmartGuest() {
        Map<String, String> testProperties = getParam("testPlatformSmartGuest");
        BaseTest.paramsThreadLocal.set(testProperties);
        Session<WebDriver> session = sessionThreadLocal.get();

        String currency = testProperties.get("currency");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        PlatformSmartPage<WebDriver> platformSmartPage = PlatformSmartPage.getInstance(session);

        step("Wait for app to launch");
        homePage.gotoHome();

        step("Open app as guest");
        loginPage.loginAsGuest();

        step("Change currency");
        homePage.changeCurrency(currency,"");
        SmartUtils.takeSmartScreenshot(session.driver(), "Platform_[Guest] Verify single app bar is working fine to the user");

        step("Open global search");
        homePage.clickGlobalSearch();
        SmartUtils.takeSmartScreenshot(session.driver(), "Platform_[Guest] Verify single app bar is working fine for global search");

        step("Go Back to homepage again");
        homePage.gotoHome();

        step("Click Promo Button");
        homePage.clickPromoChip();
        SmartUtils.takeSmartScreenshot(session.driver(), "Platform_[Guest] Verify single app bar is working fine for promo button");

        step("Go Back to homepage again");
        homePage.gotoHome();

        step("Verify Location Banner Section");
        platformSmartPage.verifyLocationBanner();
        SmartUtils.takeSmartScreenshot(session.driver(), "Platform_[Guest] Verify info banner with proper functionality");
    }
    
}
