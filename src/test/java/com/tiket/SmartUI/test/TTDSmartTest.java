package com.tiket.SmartUI.test;

import com.tiket.SmartUI.SmartUtils;
import com.tiket.SmartUI.page.ttd.TTDSmartPage;
import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.model.Session;
import com.tiket.page.common.webview.WebViewPage;
import com.tiket.page.home.HomePage;
import com.tiket.page.login.LoginPage;
import com.tiket.page.ttd.searchpage.TTDSearchPage;
import com.tiket.testbase.BaseTest;
import com.tiket.web.page.ttd.TtdBookingFormPage.TtdBookingFormPageWeb;
import com.tiket.web.page.ttd.TtdProductDetailPage.TtdProductDetailPageWeb;
import com.tiket.web.page.ttd.TtdSelectedPackageDetailPage.TtdSelectedPackageDetailPageWeb;
import com.tiket.web.smart.TTD.TTDSmartPageWeb;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import java.util.Map;

import static com.tiket.testdata.TestData.getParam;

public class TTDSmartTest extends BaseTest {
    @Author(names = {"Naufal Hafiz"})
    @Module(name = "TTD")
    @Tribe(name = "TTD")
    @Vertical(name = "TTD")
    @Test
    public void testTTDSmartGuest() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("testTTDSmartGuest");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDSmartPage<WebDriver> ttdSmartPage = TTDSmartPage.getInstance(session);
        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);
        TTDSmartPageWeb<?> ttdSmartPageWeb = TTDSmartPageWeb.getInstance(session);
        TtdSelectedPackageDetailPageWeb<?> ttdSelectedPackageDetailPageWeb = TtdSelectedPackageDetailPageWeb.getInstance(session);
        TtdBookingFormPageWeb<?> ttdBookingFormPageWeb = TtdBookingFormPageWeb.getInstance(session);

        String product = map.get("product");
        int ticketQty = Integer.parseInt(map.get("ticket_qty"));

        BaseTest.paramsThreadLocal.set(map);

        step("Go to home page");
        homePage.gotoHome();

        step("Make user as guest");
        loginPage.loginAsGuest();
        String language = homePage.getLanguageApp();

        step("Change currency to 'IDR'");
        homePage.changeCurrency("IDR", "");

        step("Open TTD Landing Page");
        homePage.openVerticalPageDeeplink("To Do", language, "IDR");

        step("Scroll to Product Card");
        ttdSmartPage.scrollToProductCardLandingPage();
        SmartUtils.takeSmartScreenshot(session.driver(), "TTD_[Guest] Verify Product Card");

        step("Search the product");
        ttdSmartPage.clickSearchBar();
        ttdSearchPage.inputSearchKeyword(product);

        step("select the activity");
        ttdSearchPage.selectRecommendedActivity(product);

        step("Get the product name");
        webViewPage.switchToWebViewUrl("to-do");
        ttdSmartPageWeb.clickShareButton();
        SmartUtils.takeSmartScreenshot(session.driver(), "TTD_[Guest] Verify app bar - Share button");
        ttdSmartPageWeb.closeBottomsheet();

        step("Select ticket");
        ttdProductDetailPageWeb.goToPackagesList();
        ttdSmartPageWeb.waitForTicketCard();
        SmartUtils.takeSmartScreenshot(session.driver(), "TTD_[Guest] Verify Bottom Navigation & Card View");
        ttdSmartPageWeb.chooseTicket();

        step("Change to tomorrow date");
        ttdSelectedPackageDetailPageWeb.chooseTomorrowDate();

        step("Set total ticket");
        ttdSelectedPackageDetailPageWeb.setTicketQty(ticketQty, 0);

        step("Submit package detail");
        ttdSelectedPackageDetailPageWeb.clickBookButton();
        webViewPage.switchToNative();

        step("Fill out contact details");
        webViewPage.switchToWebViewUrl("to-do");
        ttdBookingFormPageWeb.fillContactOrderDetail(false);
        ttdSmartPageWeb.waitForBottomsheetToBeDismissed();
        SmartUtils.takeSmartScreenshot(session.driver(), "TTD_[Guest] Verify group field - contact details");

        step("Fill out visitor details");
        ttdSmartPageWeb.fillVisitorDetailsData(false);
        SmartUtils.takeSmartScreenshot(session.driver(), "TTD_[Guest] Verify group field - visitor details");
        ttdSmartPageWeb.closeVisitorDetailsBottomsheet();
    }

    @Author(names = {"Naufal Hafiz"})
    @Module(name = "TTD")
    @Tribe(name = "TTD")
    @Vertical(name = "TTD")
    @Test
    public void testTTDSmartLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("testTTDSmartLogin");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDSmartPage<WebDriver> ttdSmartPage = TTDSmartPage.getInstance(session);
        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);
        TTDSmartPageWeb<?> ttdSmartPageWeb = TTDSmartPageWeb.getInstance(session);
        TtdSelectedPackageDetailPageWeb<?> ttdSelectedPackageDetailPageWeb = TtdSelectedPackageDetailPageWeb.getInstance(session);
        TtdBookingFormPageWeb<?> ttdBookingFormPageWeb = TtdBookingFormPageWeb.getInstance(session);

        String product = map.get("product");
        int ticketQty = Integer.parseInt(map.get("ticket_qty"));
        String loginMethod = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");

        BaseTest.paramsThreadLocal.set(map);

        step("Go to home page");
        homePage.gotoHome();

        step("input credential");
        loginPage.loginWith(loginMethod, username, password);
        String language = homePage.getLanguageApp();

        step("Change currency to 'IDR'");
        homePage.changeCurrency("IDR", "");

        step("Open TTD Landing Page");
        homePage.openVerticalPageDeeplink("To Do", language, "IDR");

        step("Scroll to Product Card");
        ttdSmartPage.scrollToProductCardLandingPage();
        SmartUtils.takeSmartScreenshot(session.driver(), "TTD_[Login] Verify Product Card");

        step("Search the product");
        ttdSmartPage.clickSearchBar();
        ttdSearchPage.inputSearchKeyword(product);

        step("select the activity");
        ttdSearchPage.selectRecommendedActivity(product);

        step("Get the product name");
        webViewPage.switchToWebViewUrl("to-do");
        ttdSmartPageWeb.clickShareButton();
        SmartUtils.takeSmartScreenshot(session.driver(), "TTD_[Login] Verify app bar - Share button");
        ttdSmartPageWeb.closeBottomsheet();

        step("Select ticket");
        ttdProductDetailPageWeb.goToPackagesList();
        ttdSmartPageWeb.waitForTicketCard();
        SmartUtils.takeSmartScreenshot(session.driver(), "TTD_[Login] Verify Bottom Navigation & Card View");
        ttdSmartPageWeb.chooseTicket();

        step("Change to tomorrow date");
        ttdSelectedPackageDetailPageWeb.chooseTomorrowDate();

        step("Set total ticket");
        ttdSelectedPackageDetailPageWeb.setTicketQty(ticketQty, 0);

        step("Submit package detail");
        ttdSelectedPackageDetailPageWeb.clickBookButton();
        webViewPage.switchToNative();

        step("Fill out contact details");
        webViewPage.switchToWebViewUrl("to-do");
        ttdBookingFormPageWeb.fillContactOrderDetail(true);
        ttdSmartPageWeb.waitForBottomsheetToBeDismissed();
        SmartUtils.takeSmartScreenshot(session.driver(), "TTD_[Login] Verify group field - contact details");

        step("Fill out visitor details");
        ttdSmartPageWeb.fillVisitorDetailsData(true);
        SmartUtils.takeSmartScreenshot(session.driver(), "TTD_[Login] Verify group field - visitor details");
        ttdSmartPageWeb.closeVisitorDetailsBottomsheet();
    }
}
