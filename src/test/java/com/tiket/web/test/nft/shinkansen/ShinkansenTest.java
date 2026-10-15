package com.tiket.web.test.nft.shinkansen;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.web.WebTest;
import com.tiket.web.page.common.calendar.CalendarPageWeb;
import com.tiket.web.page.common.home.HomePageWeb;
import com.tiket.web.page.common.home.LoginPageWeb;
import com.tiket.web.page.nft.shinkansen.booking_page.ShinkansenBookingPageWeb;
import com.tiket.web.page.nft.shinkansen.searchResult_page.ShinkansenSearchResultPageWeb;
import com.tiket.web.page.nft.shinkansen.search_page.ShinkansenSearchPageWeb;
import com.tiket.web.page.platform.payment.PaymentPageWeb;
import org.testng.annotations.Test;

import javax.security.auth.login.FailedLoginException;
import java.util.Map;

import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.core.Is.is;

public class ShinkansenTest extends WebTest {

    @Author(names = "Mridul Gupta")
    @Module(name = "CompleteBooking")
    @Tribe(name = "Shinkansen")
    @Vertical(name = "NFT")
    @Test
    public void testCompleteShinkansenBookingRoundTripForTwoAdultsAndOneInfantGuestWeb() {
        Map<String, String> param = getParam("testCompleteShinkansenBookingRoundTripForTwoAdultsAndOneInfantGuestWeb");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String currency = param.get("currency");
        String originStation = param.get("originStation");
        String destinationStation = param.get("destinationStation");
        String tripType = param.get("tripType");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int daysToReturnDate = Integer.parseInt(param.get("daysToReturnDate"));
        int adultCount = Integer.parseInt(param.get("adultCount"));
        int infantCount = Integer.parseInt(param.get("infantCount"));

        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);
        String returnDate = CoreFunctions.addToLongDate(departureDate, daysToReturnDate);

        String fullName = CoreFunctions.randomName()+" NLN";
        String phoneNumber = "62" + CoreFunctions.randomInt(8);
        String randomEmail = CoreFunctions.randomEmail();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        ShinkansenSearchPageWeb<?> shinkansenSearchPage = ShinkansenSearchPageWeb.getInstance(session);
        ShinkansenSearchResultPageWeb<?> shinkansenSearchResultPage = ShinkansenSearchResultPageWeb.getInstance(session);
        ShinkansenBookingPageWeb<?> shinkansenBookingPage = ShinkansenBookingPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);
        CalendarPageWeb<?> calendarPage = CalendarPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open home page as guest (non-login)");
        loginPage.loginAsGuest();

        step("User select " + currency + " currency");
        homePage.selectCurrency(currency);

        step("User open Train landing page via deeplink");
        homePage.openVerticalPageViaURL("Train");

        step("User skip pop-up if any");
        shinkansenSearchPage.clickSkipPopup();

        step("User tap on Shinkansen chip");
        shinkansenSearchPage.selectShinkansen();

        step("User select origin station: " + originStation);
        shinkansenSearchPage.selectOrigin(originStation);

        step("User select destination station: " + destinationStation);
        shinkansenSearchPage.selectDestination(destinationStation);

        step("User toggle " + tripType + " trip type");
        shinkansenSearchPage.toggleTripType(tripType);

        step("User select departure (next " + daysToDepartureDate + " days) and return (next " + daysToReturnDate + " days) dates");
        shinkansenSearchPage.clickOnCalendarWidgetToOpen();
        calendarPage.selectDate(departureDate, returnDate);

        step("User set passenger count to " + adultCount + " adult(s) and " + infantCount + " infant(s)");
        shinkansenSearchPage.selectPassenger(adultCount, infantCount);

        step("User saves the passengers");
        shinkansenSearchPage.savePassengers();

        step("User tap Search Shinkansen button");
        shinkansenSearchPage.searchShinkansen();

        step("User is on SRP");
        Assertion.assertThat("Verify SRP is displayed", shinkansenSearchResultPage.checkSRP(), is(true));

        step("User skips the filter option");
        shinkansenSearchResultPage.clickSkip();

        step("User select available Shinkansen fleet on SRP for depart trip");
        shinkansenSearchResultPage.clickFirstShinkansen();

        step("User select the Ticket type ");
        shinkansenSearchResultPage.selectTicketType();

        step("User select available Shinkansen fleet on SRP for return trip");
        shinkansenSearchResultPage.clickFirstShinkansen();

        step("User select the Ticket type ");
        shinkansenSearchResultPage.selectTicketType();

        step("User fill contact details (guest user)");
        shinkansenBookingPage.fillContactDetails(fullName, phoneNumber, randomEmail);

        step("User continue to payment page");
        shinkansenBookingPage.continueToPay();
        shinkansenBookingPage.goToPaymentPage();

        step("Verify user is redirected to payment page and Shinkansen orderId is generated");
        String orderID = paymentPage.getOrderId();
        Assertion.assertThat("Verify Shinkansen order ID is displayed in payment page", !orderID.isEmpty(), is(true));
    }

    @Author(names = "Mridul Gupta")
    @Module(name = "CompleteBooking")
    @Tribe(name = "Shinkansen")
    @Vertical(name = "NFT")
    @Test
    public void testCompleteShinkansenBookingOneWayForOneAdultLoginWeb()
            throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testCompleteShinkansenBookingOneWayForOneAdultLoginWeb");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String email = param.get("email");
        String password = param.get("password");
        String currency = param.get("currency");
        String originStation = param.get("originStation");
        String destinationStation = param.get("destinationStation");
        String tripType = param.get("tripType");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int daysToShiftDate = Integer.parseInt(param.get("daysToShiftDate"));
        int adultCount = Integer.parseInt(param.get("adultCount"));
        int infantCount = Integer.parseInt(param.get("infantCount"));

        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        ShinkansenSearchPageWeb<?> shinkansenSearchPage = ShinkansenSearchPageWeb.getInstance(session);
        ShinkansenSearchResultPageWeb<?> shinkansenSearchResultPage = ShinkansenSearchResultPageWeb.getInstance(session);
        ShinkansenBookingPageWeb<?> shinkansenBookingPage = ShinkansenBookingPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);
        CalendarPageWeb<?> calendarPage = CalendarPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open home page as logged in user via cookie");
        loginPage.loginWithSpecificAccountViaCookie(email, password);

        step("User select " + currency + " currency");
        homePage.selectCurrency(currency);

        step("User open Train landing page via deeplink");
        homePage.openVerticalPageViaURL("Train");

        step("User skip pop-up if any");
        shinkansenSearchPage.clickSkipPopup();

        step("User tap on Shinkansen chip");
        shinkansenSearchPage.selectShinkansen();

        step("User select origin station: " + originStation);
        shinkansenSearchPage.selectOrigin(originStation);

        step("User select destination station: " + destinationStation);
        shinkansenSearchPage.selectDestination(destinationStation);

        step("User select " + tripType + " trip type (round-trip toggle stays off)");
        shinkansenSearchPage.toggleTripType(tripType);

        step("User tap Search Shinkansen button");
        shinkansenSearchPage.searchShinkansen();

        step("User shifts the departure date +" + daysToShiftDate + " days by editing the current SRP URL");
        shinkansenSearchResultPage.shiftTravelDateByDays(daysToShiftDate);

        step("Verify no Shinkansen fleet is available for the far-out (+" + daysToShiftDate + " days) date");
        Assertion.assertThat("Verify first Shinkansen is NOT present for the far-out date", shinkansenSearchResultPage.checkShinkansen(), is(false));

        step("User navigates back to the search form (back twice: shifted SRP -> valid SRP -> search form)");
        shinkansenSearchResultPage.getBack();
        shinkansenSearchResultPage.getBack();

        step("User select " + tripType + " trip type (round-trip toggle stays off)");
        shinkansenSearchPage.toggleTripType(tripType);

        step("User set passenger count to " + adultCount + " adult(s)");
        shinkansenSearchPage.selectPassenger(adultCount, infantCount);

        step("User saves the passengers");
        shinkansenSearchPage.savePassengers();

        step("User select DoT (next " + daysToDepartureDate + " days)");
        shinkansenSearchPage.clickOnCalendarWidgetToOpen();
        calendarPage.selectDate(departureDate);

        step("User tap Search Shinkansen button");
        shinkansenSearchPage.searchShinkansen();

        step("User is on SRP");
        Assertion.assertThat("Verify SRP is displayed", shinkansenSearchResultPage.checkSRP(), is(true));

        step("User skips the filter option");
        shinkansenSearchResultPage.clickSkip();

        step("User select any available Shinkansen fleet on SRP");
        shinkansenSearchResultPage.clickFirstShinkansen();

        step("User select the Ticket type ");
        shinkansenSearchResultPage.selectTicketType();

        step("User continue to payment page");
        shinkansenBookingPage.continueToPay();
        shinkansenBookingPage.goToPaymentPage();

        step("Verify user is redirected to payment page and Shinkansen orderId is generated");
        String orderID = paymentPage.getOrderId();
        Assertion.assertThat("Verify Shinkansen order ID is displayed in payment page",
                !orderID.isEmpty(), is(true));
    }
}
