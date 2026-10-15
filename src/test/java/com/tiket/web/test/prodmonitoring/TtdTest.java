package com.tiket.web.test.prodmonitoring;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.web.WebTest;
import com.tiket.web.dWeb.page.payment.PaymentPage;
import com.tiket.web.page.common.calendar.CalendarPageWeb;
import com.tiket.web.page.common.home.HomePageWeb;
import com.tiket.web.page.common.home.LoginPageWeb;
import com.tiket.web.page.ttd.TtdBookingFormPage.TtdBookingFormPageWeb;
import com.tiket.web.page.ttd.TtdLandingPage.TtdLandingPageWeb;
import com.tiket.web.page.ttd.TtdProductDetailPage.TtdProductDetailPageWeb;
import org.testng.annotations.Test;

import java.util.Map;

import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;

public class TtdTest extends WebTest {

    @Author(names = {"Aditi"})
    @Module(name = "Booking")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void completeBookingTimeSlotLoginProdMonitoringTTDWeb() throws Exception {
        Map<String, String> param = getParam("completeBookingTimeSlotLoginProdMonitoringTTDWeb");
        BaseTest.paramsThreadLocal.set(param);

        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String productName = param.get("productName");
        String packageName = param.get("packageName").replaceAll("@",":");
        int quantity = Integer.parseInt(param.get("quantity"));
        int ticketIndex = Integer.parseInt(param.get("ticketIndex"));

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        TtdLandingPageWeb<?> ttdLandingPageWeb = TtdLandingPageWeb.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);
        TtdBookingFormPageWeb<?> ttdBookingFormPageWeb = TtdBookingFormPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        CalendarPageWeb<?> calendarPage = CalendarPageWeb.getInstance(session);

        step("Open home page");
        homePage.goToHomePage();

        step("Login page");
        loginPage.loginIfNotWithSameEmail(username, password, otp, retryCounter);

        step("Home page");
        homePage.selectCurrency("IDR");
        ttdLandingPageWeb.goToTtdLandingPage();

        step("Landing page");
        ttdLandingPageWeb.inputSearchKeyword(productName);
        ttdLandingPageWeb.selectRecommendedActivity(productName);

        step("Product detail page");
        Assertion.assertThat("Is correct product opened", ttdProductDetailPageWeb.isCorrectProductOpened(productName), is(true));
        ttdProductDetailPageWeb.clickSeePackagesButton();
        ttdProductDetailPageWeb.openCalender();
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, CoreFunctions.generateRandomInt(5,12));
        calendarPage.selectDate(departureDate);
        ttdProductDetailPageWeb.chooseTicket(packageName);
        ttdProductDetailPageWeb.setTimeslotOptional();
        ttdProductDetailPageWeb.setTicketQty(quantity, ticketIndex);
        ttdProductDetailPageWeb.clickBookButton();

        step("Booking page");
        ttdBookingFormPageWeb.toggleSameAsContactDetails();

        step("Payment page");
        ttdBookingFormPageWeb.continueToPaymentPage();
        ttdBookingFormPageWeb.checkPriceChange();
        Assertion.assertThat("Success redirected to payment page", paymentPage.isPaymentPageLoaded(), is(true));
        String orderId = paymentPage.getOrderId();
        Assertion.assertThat("user get order id", orderId, allOf(
                not(nullValue()),
                not(equalTo(""))
        ));
    }
}
