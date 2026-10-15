package com.tiket.web.dashboard.test.hotel.extranet;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.web.dashboard.page.hotel.extranet.BulkUpdatePage;
import com.tiket.web.dashboard.page.hotel.extranet.ManageRatesAndAvailabilityPage;
import com.tiket.web.dashboard.page.hotel.extranet.Navbar;
import com.tiket.web.dashboard.page.hotel.extranet.SideMenu;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Map;

import static com.tiket.testdata.TestData.getObjectParams;
import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.Matchers.is;

public class BulkUpdateTest extends BaseTest {
    @Author(names = {"Fadhlan", "Vipul Kumar"})
    @Module(name = "BulkUpdate")
    @Tribe(name = "Extranet")
    @Vertical(name = "Accommodation")
    @Test
    public void testVerifyUserAbleToBulkUpdateRatesForSingleDate() {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        Navbar<?> navbar = new Navbar<>(session);
        BulkUpdatePage<?> bulkUpdatePage = new BulkUpdatePage<>(session);
        SideMenu<?> sideMenu = new SideMenu<>(session);
        ManageRatesAndAvailabilityPage<?> manageRatesAndAvailabilityPage = new ManageRatesAndAvailabilityPage<>(session);

        AuthTest authTest = new AuthTest();

        Map<String, String> param = getParam("testVerifyUserAbleToBulkUpdateRatesForSingleDate");
        BaseTest.paramsThreadLocal.set(param);
        String email = param.get("email");
        String password = param.get("password");
        String hotelName = param.get("hotelName");
        int startDateIndex = Integer.parseInt(param.get("startDateIndex"));
        int endDateIndex = Integer.parseInt(param.get("endDateIndex"));
        String roomName = param.get("roomName");
        String ratePlanName = param.get("ratePlanName");
        String rate = param.get("rate");

        step("Login to Extranet");
        authTest.ensureUserIsLoggedInWithCorrectCredentials(email, password, session);

        step("Change hotel");
        navbar.changeHotel(hotelName);

        step("Go to bulk update page");
        sideMenu.clickRatesAndAvailabilityMenu();
        sideMenu.clickBulkUpdateMenu();
        Assertion.assertThat("Bulk update page is loaded", bulkUpdatePage.isBulkUpdatePageLoaded(), is(true));

        step("Select single date");
        bulkUpdatePage.selectDateByIndex(startDateIndex, endDateIndex);

        step("Go to price tab section");
        bulkUpdatePage.clickPriceTab();

        step("Fill out all allotment inputs");
        bulkUpdatePage.selectRoomName(roomName);
        bulkUpdatePage.enterRateByRoomNameAndRatePlan(roomName, ratePlanName, rate);

        step("Click save changes");
        bulkUpdatePage.clickSaveChangesButton();
        bulkUpdatePage.clickSaveChangesConfirmationButton();
        Assertion.assertThat("The changes are saved", bulkUpdatePage.isUpdatePreviewModalAppeared(), is(true));

        step("Close the update preview modal");
        bulkUpdatePage.clickUpdatePreviewModalOKButton();

        step("Check allotment on rates and availability");
        sideMenu.clickManageRatesAndAvailabilityMenu();
        Assertion.assertThat("Manage Rates and Availability page is loaded", manageRatesAndAvailabilityPage.isManageRatesAndAvailabilityPageLoaded(), is(true));
        manageRatesAndAvailabilityPage.selectRoom(roomName);
        Assertion.assertThat("The rate is updated", manageRatesAndAvailabilityPage.isRateUpdatedByIndex(startDateIndex, Integer.parseInt(rate)), is(true));
    }

    @Author(names = {"Fadhlan", "Vipul Kumar"})
    @Module(name = "BulkUpdate")
    @Tribe(name = "Extranet")
    @Vertical(name = "Accommodation")
    @Test
    public void testVerifyUserAbleToBulkUpdateAllotmentsForSingleDate() {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        Navbar<?> navbar = new Navbar<>(session);
        BulkUpdatePage<?> bulkUpdatePage = new BulkUpdatePage<>(session);
        SideMenu<?> sideMenu = new SideMenu<>(session);
        ManageRatesAndAvailabilityPage<?> manageRatesAndAvailabilityPage = new ManageRatesAndAvailabilityPage<>(session);

        AuthTest authTest = new AuthTest();

        Map<String, String> param = getParam("testVerifyUserAbleToBulkUpdateAllotmentsForSingleDate");
        BaseTest.paramsThreadLocal.set(param);
        String email = param.get("email");
        String password = param.get("password");
        String hotelName = param.get("hotelName");
        int startDateIndex = Integer.parseInt(param.get("startDateIndex"));
        int endDateIndex = Integer.parseInt(param.get("endDateIndex"));
        String roomName = param.get("roomName");
        String ratePlanName = param.get("ratePlanName");
        String allotment = param.get("allotment");

        step("Login to Extranet");
        authTest.ensureUserIsLoggedInWithCorrectCredentials(email, password, session);

        step("Change hotel");
        navbar.changeHotel(hotelName);

        step("Go to bulk update page");
        sideMenu.clickRatesAndAvailabilityMenu();
        sideMenu.clickBulkUpdateMenu();
        Assertion.assertThat("Bulk update page is loaded", bulkUpdatePage.isBulkUpdatePageLoaded(), is(true));

        step("Select single date");
        bulkUpdatePage.selectDateByIndex(startDateIndex, endDateIndex);

        step("Go to allotment tab section");
        bulkUpdatePage.clickAllotmentTab();

        step("Fill out all allotment inputs");
        bulkUpdatePage.selectRoomName(roomName);
        bulkUpdatePage.enterAllotmentByRoomNameAndRatePlan(roomName, ratePlanName, allotment);

        step("Click save changes");
        bulkUpdatePage.clickSaveChangesButton();
        bulkUpdatePage.clickSaveChangesConfirmationButton();
        Assertion.assertThat("The changes are saved", bulkUpdatePage.isUpdatePreviewModalAppeared(), is(true));

        step("Close the update preview modal");
        bulkUpdatePage.clickUpdatePreviewModalOKButton();

        step("Check allotment on rates and availability");
        sideMenu.clickManageRatesAndAvailabilityMenu();
        Assertion.assertThat("Manage Rates and Availability page is loaded", manageRatesAndAvailabilityPage.isManageRatesAndAvailabilityPageLoaded(), is(true));
        manageRatesAndAvailabilityPage.selectRoom(roomName);
        Assertion.assertThat("The allotment is updated", manageRatesAndAvailabilityPage.isAllotmentUpdatedByIndex(startDateIndex, allotment), is(true));
    }

    @DataProvider(name = "testVerifyUserAbleToBulkUpdateAvailabilityForSingleDateDataProvider")
    public Object[][] testVerifyUserAbleToBulkUpdateAvailabilityForSingleDateDataProvider() {
        return getObjectParams("testVerifyUserAbleToBulkUpdateAvailabilityForSingleDate");
    }

    @Author(names = {"Fadhlan", "Vipul Kumar"})
    @Module(name = "BulkUpdate")
    @Tribe(name = "Extranet")
    @Vertical(name = "Accommodation")
    @Test(dataProvider = "testVerifyUserAbleToBulkUpdateAvailabilityForSingleDateDataProvider", priority = 1)
    public void testVerifyUserAbleToBulkUpdateAvailabilityForSingleDate(Map<String, String> param) {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        Navbar<?> navbar = new Navbar<>(session);
        BulkUpdatePage<?> bulkUpdatePage = new BulkUpdatePage<>(session);
        SideMenu<?> sideMenu = new SideMenu<>(session);
        ManageRatesAndAvailabilityPage<?> manageRatesAndAvailabilityPage = new ManageRatesAndAvailabilityPage<>(session);

        AuthTest authTest = new AuthTest();

        BaseTest.paramsThreadLocal.set(param);
        String email = param.get("email");
        String password = param.get("password");
        String hotelName = param.get("hotelName");
        int startDateIndex = Integer.parseInt(param.get("startDateIndex"));
        int endDateIndex = Integer.parseInt(param.get("endDateIndex"));
        String roomName = param.get("roomName");
        String ratePlanName = param.get("ratePlanName");
        boolean closeOutStatus = Boolean.parseBoolean(param.get("closeOutStatus"));

        step("Login to Extranet");
        authTest.ensureUserIsLoggedInWithCorrectCredentials(email, password, session);

        step("Change hotel");
        navbar.changeHotel(hotelName);

        step("Go to bulk update page");
        sideMenu.clickRatesAndAvailabilityMenu();
        sideMenu.clickBulkUpdateMenu();
        Assertion.assertThat("Bulk update page is loaded", bulkUpdatePage.isBulkUpdatePageLoaded(), is(true));

        step("Select single date");
        bulkUpdatePage.selectDateByIndex(startDateIndex, endDateIndex);

        step("Go to close out tab section");
        bulkUpdatePage.clickCloseOutTab();

        step("Select close out status");
        bulkUpdatePage.selectRoomName(roomName);
        bulkUpdatePage.selectCloseOutStatusByRoomName(roomName, closeOutStatus);

        step("Click save changes");
        bulkUpdatePage.clickSaveChangesButton();
        bulkUpdatePage.clickSaveChangesConfirmationButton();
        Assertion.assertThat("The changes are saved", bulkUpdatePage.isUpdatePreviewModalAppeared(), is(true));

        step("Close the update preview modal");
        bulkUpdatePage.clickUpdatePreviewModalOKButton();

        step("Check allotment on rates and availability");
        sideMenu.clickManageRatesAndAvailabilityMenu();
        Assertion.assertThat("Manage Rates and Availability page is loaded", manageRatesAndAvailabilityPage.isManageRatesAndAvailabilityPageLoaded(), is(true));
        manageRatesAndAvailabilityPage.selectRoom(roomName);
        Assertion.assertThat(String.format("The calendar's closed out status is %s", closeOutStatus), manageRatesAndAvailabilityPage.isCalendarCloseOutByIndex(startDateIndex), is(closeOutStatus));
    }

    @Author(names = {"Ketan Singh", "Vipul Kumar"})
    @Module(name = "BulkUpdate")
    @Tribe(name = "Extranet")
    @Vertical(name = "Accommodation")
    @Test
    public void testVerifyUserAbleToBulkUpdateAllotmentsForMultipleDates() {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        Navbar<?> navbar = new Navbar<>(session);
        BulkUpdatePage<?> bulkUpdatePage = new BulkUpdatePage<>(session);
        SideMenu<?> sideMenu = new SideMenu<>(session);
        ManageRatesAndAvailabilityPage<?> manageRatesAndAvailabilityPage = new ManageRatesAndAvailabilityPage<>(session);

        AuthTest authTest = new AuthTest();

        Map<String, String> param = getParam("testVerifyUserAbleToBulkUpdateAllotmentsForMultipleDates");
        BaseTest.paramsThreadLocal.set(param);
        String email = param.get("email");
        String password = param.get("password");
        String hotelName = param.get("hotelName");
        int startDateIndex = Integer.parseInt(param.get("startDateIndex"));
        int endDateIndex = Integer.parseInt(param.get("endDateIndex"));
        String roomName = param.get("roomName");
        String ratePlanName = param.get("ratePlanName");
        String allotment = param.get("allotment");

        step("Login to Extranet");
        authTest.ensureUserIsLoggedInWithCorrectCredentials(email, password, session);

        step("Change hotel");
        navbar.changeHotel(hotelName);

        step("Go to bulk update page");
        sideMenu.clickRatesAndAvailabilityMenu();
        sideMenu.clickBulkUpdateMenu();
        Assertion.assertThat("Bulk update page is loaded", bulkUpdatePage.isBulkUpdatePageLoaded(), is(true));

        step("Select Multiple date");
        bulkUpdatePage.selectDateByIndex(startDateIndex, endDateIndex);

        step("Go to allotment tab section");
        bulkUpdatePage.clickAllotmentTab();

        step("Fill out all allotment inputs");
        bulkUpdatePage.selectRoomName(roomName);
        bulkUpdatePage.enterAllotmentByRoomNameAndRatePlan(roomName, ratePlanName, allotment);

        step("Click save changes");
        bulkUpdatePage.clickSaveChangesButton();
        bulkUpdatePage.clickSaveChangesConfirmationButton();
        Assertion.assertThat("The changes are saved", bulkUpdatePage.isUpdatePreviewModalAppeared(), is(true));

        step("Close the update preview modal");
        bulkUpdatePage.clickUpdatePreviewModalOKButton();

        step("Check allotment on rates and availability");
        sideMenu.clickManageRatesAndAvailabilityMenu();
        Assertion.assertThat("Manage Rates and Availability page is loaded", manageRatesAndAvailabilityPage.isManageRatesAndAvailabilityPageLoaded(), is(true));
        manageRatesAndAvailabilityPage.selectRoom(roomName);
        Assertion.assertThat("The allotment is updated", manageRatesAndAvailabilityPage.isAllotmentUpdatedByIndex(startDateIndex, allotment), is(true));
    }
}
