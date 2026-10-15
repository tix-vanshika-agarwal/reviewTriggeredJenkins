package com.tiket.test.prodmonitoring;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Session;
import com.tiket.page.common.webview.WebViewPage;
import com.tiket.page.home.HomePage;
import com.tiket.page.login.LoginPage;
import com.tiket.page.order.orderdetail.OrderDetailPage;
import com.tiket.page.order.orderlist.OrderListPage;
import com.tiket.page.payment.PaymentPage;
import com.tiket.page.ttd.landingpage.TTDLandingPage;
import com.tiket.page.ttd.searchpage.TTDSearchPage;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.web.page.common.calendar.CalendarPageWeb;
import com.tiket.web.page.ttd.TtdBookingFormPage.TtdBookingFormPageWeb;
import com.tiket.web.page.ttd.TtdProductDetailPage.TtdProductDetailPageWeb;
import com.tiket.web.page.ttd.TtdSelectedPackageDetailPage.TtdSelectedPackageDetailPageWeb;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import java.util.Map;

import static com.tiket.core.CoreFunctions.getOrderId;
import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;

public class TtdTest extends BaseTest {
    @Author(names = {"Kartik", "Akbar"})
    @Module(name = "CompleteBooking")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void completeBookingTimeSlotLoginProdMonitoringTTD() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("completeBookingTimeSlotLoginProdMonitoringTTD");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);
        TtdSelectedPackageDetailPageWeb<?> ttdSelectedPackageDetailPageWeb = TtdSelectedPackageDetailPageWeb.getInstance(session);
        TtdBookingFormPageWeb<?> ttdBookingFormPageWeb = TtdBookingFormPageWeb.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");
        int ticketQty = Integer.parseInt(map.get("ticket_qty"));
        int packageNumber = Integer.parseInt(map.get("packageNumber"));

        String product = map.get("product");
        BaseTest.paramsThreadLocal.set(map);

        step("Go to home page");
        homePage.gotoHome();

        step("Login page");
        loginPage.loginWith(loginType, username, password);

        step("Home page");
        homePage.changeCurrency("IDR", username);
        homePage.openVerticalPageDeeplink("To Do", "", "IDR");

        step("Landing page");
        ttdLandingPage.clickSearchBar();
        ttdSearchPage.inputSearchKeyword(product);

        step("Search page");
        ttdSearchPage.selectRecommendedActivity(product);

        step("Product detail page");
        webViewPage.switchToWebViewUrl("to-do");
        ttdProductDetailPageWeb.goToPackagesList();
        ttdProductDetailPageWeb.openCalendar();
        ttdProductDetailPageWeb.selectDateCalendarPopup(CoreFunctions.generateRandomInt(5,10));
        ttdProductDetailPageWeb.chooseTicket(packageNumber);
        ttdSelectedPackageDetailPageWeb.setTicketQty(ticketQty);
        Assertion.assertThat("Verify tiket point is displayed", ttdSelectedPackageDetailPageWeb.isTiketPointDisplayed(), is(true));
        ttdSelectedPackageDetailPageWeb.clickBookButton();
        webViewPage.switchToNative();

        step("Booking form page");
        webViewPage.switchToWebViewUrl("to-do");
        ttdBookingFormPageWeb.fillContactOrderDetail(true);
        ttdBookingFormPageWeb.fillGuestDetail();
        ttdBookingFormPageWeb.continueToPaymentPage();
        ttdBookingFormPageWeb.checkPriceNotifChange();
        webViewPage.switchToNative();

        step("Payment page");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id", orderID, allOf(
                not(nullValue()),
                not(equalTo(""))
        ));
    }
}
