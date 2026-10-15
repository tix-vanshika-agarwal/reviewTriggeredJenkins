package com.tiket.web.dashboard.test.hotel.extranet;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.web.dashboard.page.hotel.extranet.ManageRatesAndAvailabilityPage;
import com.tiket.web.dashboard.page.hotel.extranet.Navbar;
import com.tiket.web.dashboard.page.hotel.extranet.SideMenu;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Map;

import static com.tiket.testdata.TestData.getObjectParams;
import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.Matchers.is;

public class ManageRatesAndAvailabilityTest extends BaseTest {
    @Author(names = {"Fadhlan", "Vipul Kumar"})
    @Module(name = "ManageRatesAndAvailability")
    @Tribe(name = "Extranet")
    @Vertical(name = "Accommodation")
    @Test
    public void testVerifyUserAbleToUpdateSingleDate() {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        Navbar<?> navbar = new Navbar<>(session);
        ManageRatesAndAvailabilityPage<?> manageRatesAndAvailabilityPage = new ManageRatesAndAvailabilityPage<>(session);
        SideMenu<?> sideMenu = new SideMenu<>(session);

        AuthTest authTest = new AuthTest();
        
        Map<String, String> param = getParam("testVerifyUserAbleToUpdateSingleDate");
        BaseTest.paramsThreadLocal.set(param);
        String email = param.get("email");
        String password = param.get("password");
        String hotelName = param.get("hotelName");
        String allotment = param.get("allotment");
        String rate = param.get("rate");
        String ratePlanName = param.get("ratePlanName");
        boolean ratePlanStatus = Boolean.parseBoolean(param.get("ratePlanStatus"));
        String roomName = param.get("roomName");
        int startDateIndex = Integer.parseInt(param.get("startDateIndex"));

        step("Login to Extranet");
        authTest.ensureUserIsLoggedInWithCorrectCredentials(email, password, session);

        step("Change hotel");
        navbar.changeHotel(hotelName);

        step("Go to rate plan page");
        sideMenu.clickRatesAndAvailabilityMenu();
        sideMenu.clickManageRatesAndAvailabilityMenu();
        Assertion.assertThat("Manage Rates and Availability page is loaded", manageRatesAndAvailabilityPage.isManageRatesAndAvailabilityPageLoaded(), is(true));
        
        step("Choose a rate plan");
        manageRatesAndAvailabilityPage.selectRoom(roomName);
        manageRatesAndAvailabilityPage.selectRatePlanTypeByName(ratePlanName, ratePlanStatus);
        Assertion.assertThat("The right rate plan is chosen", manageRatesAndAvailabilityPage.isRatePlanSelected(ratePlanName), is(true));
        
        step("Click a single date");
        manageRatesAndAvailabilityPage.selectCalendarActiveDateByIndex(startDateIndex);
        manageRatesAndAvailabilityPage.skipRightDrawerCoachmark();
        Assertion.assertThat("The right drawer is loaded", manageRatesAndAvailabilityPage.isTheRightDrawerLoaded(), is(true));
        
        step("Fill out allotment and rates inputs");
        manageRatesAndAvailabilityPage.fillOutAllotmentInput(allotment);
        manageRatesAndAvailabilityPage.fillOutRatesInput(rate);
        
        step("Click submit button");
        manageRatesAndAvailabilityPage.clickSubmitButton();
        Assertion.assertThat("Successful snackbar is appeared", manageRatesAndAvailabilityPage.isSuccessToastAppeared(), is(true));
        Assertion.assertThat("The allotment is updated", manageRatesAndAvailabilityPage.isAllotmentUpdatedByIndex(startDateIndex, allotment), is(true));
        Assertion.assertThat("The rate is updated", manageRatesAndAvailabilityPage.isRateUpdatedByIndex(startDateIndex, Integer.parseInt(rate)), is(true));
    }

    @Author(names = {"Fadhlan", "Vipul Kumar"})
    @Module(name = "ManageRatesAndAvailability")
    @Tribe(name = "Extranet")
    @Vertical(name = "Accommodation")
    @Test
    public void testVerifyUserAbleToUpdateMultipleDate() {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        Navbar<?> navbar = new Navbar<>(session);
        ManageRatesAndAvailabilityPage<?> manageRatesAndAvailabilityPage = new ManageRatesAndAvailabilityPage<>(session);
        SideMenu<?> sideMenu = new SideMenu<>(session);

        AuthTest authTest = new AuthTest();

        Map<String, String> param = getParam("testVerifyUserAbleToUpdateMultipleDate");
        BaseTest.paramsThreadLocal.set(param);
        String email = param.get("email");
        String password = param.get("password");
        String hotelName = param.get("hotelName");
        String allotment = param.get("allotment");
        String rate = param.get("rate");
        String ratePlanName = param.get("ratePlanName");
        boolean ratePlanStatus = Boolean.parseBoolean(param.get("ratePlanStatus"));
        String roomName = param.get("roomName");
        int startDateIndex = Integer.parseInt(param.get("startDateIndex"));
        int endDateIndex = Integer.parseInt(param.get("endDateIndex"));

        step("Login to Extranet");
        authTest.ensureUserIsLoggedInWithCorrectCredentials(email, password, session);

        step("Change hotel");
        navbar.changeHotel(hotelName);

        step("Go to rate plan page");
        sideMenu.clickRatesAndAvailabilityMenu();
        sideMenu.clickManageRatesAndAvailabilityMenu();
        Assertion.assertThat("Manage Rates and Availability page is loaded", manageRatesAndAvailabilityPage.isManageRatesAndAvailabilityPageLoaded(), is(true));

        step("Choose a rate plan");
        manageRatesAndAvailabilityPage.selectRoom(roomName);
        manageRatesAndAvailabilityPage.selectRatePlanTypeByName(ratePlanName, ratePlanStatus);
        Assertion.assertThat("The right rate plan is chosen", manageRatesAndAvailabilityPage.isRatePlanSelected(ratePlanName), is(true));

        step("Click a single date");
        manageRatesAndAvailabilityPage.selectCalendarActiveDateByIndex(startDateIndex);
        manageRatesAndAvailabilityPage.skipRightDrawerCoachmark();
        Assertion.assertThat("The right drawer is loaded", manageRatesAndAvailabilityPage.isTheRightDrawerLoaded(), is(true));

        step("Fill out allotment and rates inputs");
        manageRatesAndAvailabilityPage.selectDatepickerByIndex("START_DATE", startDateIndex);
        manageRatesAndAvailabilityPage.selectDatepickerByIndex("END_DATE", endDateIndex-startDateIndex+1);
        manageRatesAndAvailabilityPage.fillOutAllotmentInput(allotment);
        manageRatesAndAvailabilityPage.fillOutRatesInput(rate);

        step("Click submit button");
        manageRatesAndAvailabilityPage.clickSubmitButton();
        Assertion.assertThat("Successful snackbar is appeared", manageRatesAndAvailabilityPage.isSuccessToastAppeared(), is(true));
        Assertion.assertThat("The allotment is updated", manageRatesAndAvailabilityPage.isAllotmentUpdatedByIndex(startDateIndex, allotment), is(true));
        while (startDateIndex <= endDateIndex) {
            Assertion.assertThat("The rate is updated", manageRatesAndAvailabilityPage.isRateUpdatedByIndex(startDateIndex, Integer.parseInt(rate)), is(true));
            startDateIndex++;
        }
    }

    @DataProvider(name = "testVerifyUserAbleToUpdateCloseOutStatus")
    public Object[][] testVerifyUserAbleToUpdateCloseOutStatusDataProvider() {
        return getObjectParams("testVerifyUserAbleToUpdateCloseOutStatus");
    }

    @Author(names = {"Fadhlan", "Vipul Kumar"})
    @Module(name = "ManageRatesAndAvailability")
    @Tribe(name = "Extranet")
    @Vertical(name = "Accommodation")
    @Test(dataProvider = "testVerifyUserAbleToUpdateCloseOutStatus", priority=1)
    public void testVerifyUserAbleToUpdateCloseOutStatus(Map<String, String> param) {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        Navbar<?> navbar = new Navbar<>(session);
        ManageRatesAndAvailabilityPage<?> manageRatesAndAvailabilityPage = new ManageRatesAndAvailabilityPage<>(session);
        SideMenu<?> sideMenu = new SideMenu<>(session);

        AuthTest authTest = new AuthTest();

        BaseTest.paramsThreadLocal.set(param);
        String email = param.get("email");
        String password = param.get("password");
        String hotelName = param.get("hotelName");
        String ratePlanName = param.get("ratePlanName");
        boolean ratePlanStatus = Boolean.parseBoolean(param.get("ratePlanStatus"));
        String roomName = param.get("roomName");
        String allotment = param.get("allotment");
        String rate = param.get("rate");
        int startDateIndex = Integer.parseInt(param.get("startDateIndex"));
        boolean closeOutStatus = Boolean.parseBoolean(param.get("closeOutStatus"));

        step("Login to Extranet");
        authTest.ensureUserIsLoggedInWithCorrectCredentials(email, password, session);

        step("Change hotel");
        navbar.changeHotel(hotelName);

        step("Go to rate plan page");
        sideMenu.clickRatesAndAvailabilityMenu();
        sideMenu.clickManageRatesAndAvailabilityMenu();
        Assertion.assertThat("Manage Rates and Availability page is loaded", manageRatesAndAvailabilityPage.isManageRatesAndAvailabilityPageLoaded(), is(true));

        step("Choose a rate plan");
        manageRatesAndAvailabilityPage.selectRoom(roomName);
        manageRatesAndAvailabilityPage.selectRatePlanTypeByName(ratePlanName, ratePlanStatus);
        Assertion.assertThat("The right rate plan is chosen", manageRatesAndAvailabilityPage.isRatePlanSelected(ratePlanName), is(true));

        step("Click a single date");
        manageRatesAndAvailabilityPage.selectCalendarActiveDateByIndex(startDateIndex);
        manageRatesAndAvailabilityPage.skipRightDrawerCoachmark();
        Assertion.assertThat("The right drawer is loaded", manageRatesAndAvailabilityPage.isTheRightDrawerLoaded(), is(true));

        step(String.format("Click close out all toggle button to be %s", closeOutStatus));
        manageRatesAndAvailabilityPage.clickCloseOutAllToggleButton(closeOutStatus, allotment, rate);

        step("Click submit button");
        manageRatesAndAvailabilityPage.clickSubmitButton();
        Assertion.assertThat("Successful snackbar is appeared", manageRatesAndAvailabilityPage.isSuccessToastAppeared(), is(true));
        Assertion.assertThat(String.format("The calendar's closed out status is %s", closeOutStatus), manageRatesAndAvailabilityPage.isCalendarCloseOutByIndex(startDateIndex), is(closeOutStatus));
    }
}
