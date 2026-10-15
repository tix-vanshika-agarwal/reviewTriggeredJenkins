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
import com.tiket.web.dWeb.page.accommodation.hotels.HotelDetailPage;
import com.tiket.web.dWeb.page.accommodation.hotels.HotelRoomListPage;
import com.tiket.web.dWeb.page.payment.PaymentPage;
import com.tiket.web.page.accommodation.hotels.BookingForm.HotelBookingFormPageWeb;
import com.tiket.web.page.accommodation.hotels.SRP.HotelSearchResultPage;
import com.tiket.web.page.accommodation.hotels.hotelLandingPage.HotelLandingPageWeb;
import com.tiket.web.page.common.calendar.CalendarPageWeb;
import com.tiket.web.page.common.home.HomePageWeb;
import com.tiket.web.page.common.home.LoginPageWeb;
import org.testng.annotations.Test;

import javax.security.auth.login.FailedLoginException;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;

public class AccommodationTest extends WebTest {

    @Author(names = "Harshit")
    @Module(name = "BookingPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void completeBookingIdrLoginProdMonitoringHotelWeb() throws FailedLoginException, InterruptedException, ExecutionException {
        Map<String, String> param = getParam("completeBookingIdrLoginProdMonitoringHotelWeb");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String location = param.get("location");
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String dateRange = param.get("dateRange");

        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, dateRange);
        String returnDate = CoreFunctions.addToLongDate(departureDate, 1);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        HotelDetailPage<?> detailPage = new HotelDetailPage<>(session);
        HotelSearchResultPage<?> searchResultPage = new HotelSearchResultPage<>(session);
        HotelRoomListPage<?> roomListPage=new HotelRoomListPage<>(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HotelBookingFormPageWeb<?> bookingFormPage = HotelBookingFormPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        CalendarPageWeb<?> calendarPage = CalendarPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("Login page");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("Home page");
        homePage.selectCurrency("IDR");
        homePage.openVerticalPageViaURL("Hotel");

        step("Landing Page");
        Assertion.assertThat("Landing Page is Opened", landingPage.isLandingPageVisible(), is(true));
        landingPage.searchStayLocation(location);
        landingPage.clickOnCalendarWidgetToOpen();
        calendarPage.selectDate(departureDate, returnDate);
        landingPage.clickSubmit();

        step("Search Result Page");
        searchResultPage.clickPopUp();
        String searchPageCurrency = homePage.getCurrentCurrency();
        Assertion.assertThat("Verify currency in  Search Result page is set to IDR", searchPageCurrency, is(containsString("IDR")));
        Assertion.assertThat("Srp is Loaded",searchResultPage.isHotelCardVisible(),is(true));
        searchResultPage.selectHotel();

        step("Product Description Page");
        detailPage.switchToNewTab();
        Assertion.assertThat("Verify if PDP is Opened", detailPage.isPdpLoaded(), is(true));
        roomListPage.scrollToRoomListSection();

        step("Room List Page");
        roomListPage.clickBookButton();

        step("Booking page");
        bookingFormPage.continueTo2NdPartThenPaymentPage();
        bookingFormPage.clickOnPopUp();

        step("Payment Page");
        String orderID = paymentPage.getOrderId();
        Assertion.assertThat("user get order id", orderID, allOf(
                not(nullValue()),
                not(equalTo(""))
        ));
    }
}
