package com.tiket.web.test.platform.eiffel.membership;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.web.WebTest;
import com.tiket.web.page.common.home.HomePageWeb;
import com.tiket.web.page.common.home.LoginPageWeb;
import com.tiket.web.page.platform.eiffel.membership.GuestBookingWeb;
import org.testng.annotations.Test;

import java.util.Map;

import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;

public class GuestBookingTest extends WebTest {
    @Author(names = "Yanuar")
    @Module(name = "GuestBooking")
    @Tribe(name = "Membership")
    @Vertical(name = "Platform")
    @Test
    public void testBookingProductsWithFreshGuestEmailWeb() throws Exception {
        Map<String, String> sheetData = getParam("testBookingProductsWithFreshGuestEmailWeb");
        BaseTest.paramsThreadLocal.set(sheetData);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        GuestBookingWeb<?> guestBooking = GuestBookingWeb.getInstance(session);

        guestBooking.setVerticals(sheetData.get("verticals").split("#"));
        guestBooking.setSheetData(sheetData);

        step("User open home page");
        homePage.goToHomePage();

        step("user open as guest");
        loginPage.loginAsGuest();

        step("User go to vertical page");
        guestBooking.goToVerticalPage();

        step("User continue booking products as guest until booking form");
        guestBooking.continueToBookingForm();

        step("User input contact details with fresh guest email and continue payment");
        guestBooking.inputContactDetails();

        step("After success payment, verify DB");
        Map<String, Object> dbData = guestBooking.getGuestEmailDataFromDB();

        Assertion.assertThat("Verify guest accountId is created", dbData.get("accountId"), is(notNullValue()));
        Assertion.assertThat("Verify guest email is saved in DB", String.valueOf(dbData.get("accountUsername")).toLowerCase(), is(sheetData.get("email")));
        Assertion.assertThat("Verify guest first name is correct", String.valueOf(dbData.get("accountFirstName")).toLowerCase(), is(sheetData.get("firstName")));
        Assertion.assertThat("Verify guest last name is correct", String.valueOf(dbData.get("accountLastName")).toLowerCase(), is(sheetData.get("lastName")));
        Assertion.assertThat("Verify guest must be true", dbData.get("guest"), is(true));
    }
}
