package com.tiket.web.dashboard.test.hotel.supply.mmmodule;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.web.dashboard.page.hotel.supply.mmmodule.HotelLibraryPage;
import com.tiket.web.dashboard.page.hotel.supply.mmmodule.LoginPage;
import com.tiket.web.dashboard.page.hotel.supply.mmmodule.PostBookScorePage;
import com.tiket.web.dashboard.page.hotel.supply.mmmodule.SidebarPage;
import org.testng.annotations.Test;

import java.util.Map;

import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;

public class PostBookScoreTest extends BaseTest {

    @Author(names = {"Raihan"})
    @Module(name = "PostBookScore")
    @Tribe(name = "Supply")
    @Vertical(name = "Accommodation")
    @Test
    public void testReportPostBookScore() {
        Map<String, String> param = getParam("testReportPostBookScore");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPage<?> loginPage = new LoginPage<>(session);
        HotelLibraryPage<?> hotelLibraryPage = new HotelLibraryPage<>(session);
        SidebarPage<?> sidebarPage = new SidebarPage<>(session);
        PostBookScorePage<?> postBookScorePage = new PostBookScorePage<>(session);

        String vendorName = param.get("vendorName");
        String vendorId = param.get("vendorId");
        String hotelName = param.get("hotelName");
        String scoreStatus = param.get("scoreStatus");
        String reason = param.get("reason");
        String scoreStatus2 = param.get("scoreStatus2");
        String reason2 = param.get("reason2");

        String username = param.get("username");
        String password = param.get("password");

        step("Open MM Module");
        loginPage.openMMModule(username, password);
        Assertion.assertThat("Hotel Library page is loaded", hotelLibraryPage.isHotelLibraryPageLoaded(), is(true));
        Assertion.assertThat("Search result is displayed", hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(), is(true));

        step("Go to post-book score page");
        sidebarPage.clickSidebarMenu(SidebarPage.SidebarMenu.PROPERTY_SCORING);
        sidebarPage.clickSidebarSubmenu(SidebarPage.SidebarMenu.POST_BOOK_SCORE);
        Assertion.assertThat("Post-book score page is displayed", postBookScorePage.isPostBookScorePageDisplayed(), is(true));

        step("Search hotel by vendor name and id");
        postBookScorePage.clickSearchByVendorTab();
        postBookScorePage.selectFilterVendorName(vendorName);
        postBookScorePage.enterFilterVendorId(vendorId);
        postBookScorePage.clickSearch();
        Assertion.assertThat("Search result is available", postBookScorePage.isSearchResultDisplayedByHotelName(hotelName), is(true));

        step("Report hotel's score status");
        postBookScorePage.clickReportByHotelName(hotelName);
        Assertion.assertThat("Report score status modal is appeared", postBookScorePage.isAddNewReportModalDisplayed(), is(true));
        postBookScorePage.selectReportScoreStatusRadio(scoreStatus);
        postBookScorePage.enterReportScoreStatusReason(reason);
        postBookScorePage.submitReport();
        postBookScorePage.confirmSubmitReport();
        Assertion.assertThat("Successful snackbar is appeared", postBookScorePage.isSuccessfulSnackbarAppeared(), is(true));
        postBookScorePage.clickSearch();
        Assertion.assertThat("Score status has been changed successfully", postBookScorePage.isSearchResultScoreStatusDisplayedByHotelName(hotelName, scoreStatus), is(true));

        step("Revert hotel's score status");
        postBookScorePage.clickReportByHotelName(hotelName);
        Assertion.assertThat("Report score status modal is appeared", postBookScorePage.isAddNewReportModalDisplayed(), is(true));
        postBookScorePage.selectReportScoreStatusRadio(scoreStatus2);
        postBookScorePage.enterReportScoreStatusReason(reason2);
        postBookScorePage.submitReport();
        postBookScorePage.confirmSubmitReport();
        Assertion.assertThat("Successful snackbar is appeared", postBookScorePage.isSuccessfulSnackbarAppeared(), is(true));
        postBookScorePage.clickSearch();
        Assertion.assertThat("Score status has been changed successfully", postBookScorePage.isSearchResultScoreStatusDisplayedByHotelName(hotelName, scoreStatus2), is(true));
    }
    @Author(names = {"Venkat"})
    @Module(name = "PostBookScore")
    @Tribe(name = "Supply")
    @Vertical(name = "Accommodation")
    @Test
    public void testSearchPostBookScoreByAccommodationType() {
        Map<String, String> param = getParam("testSearchPostBookScoreByAccommodationType");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPage<?> loginPage = new LoginPage<>(session);
        HotelLibraryPage<?> hotelLibraryPage = new HotelLibraryPage<>(session);
        SidebarPage<?> sidebarPage = new SidebarPage<>(session);
        PostBookScorePage<?> postBookScorePage = new PostBookScorePage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String accommodationType = param.get("accommodationType");

        step("Open MM Module");
        loginPage.openMMModule(username, password);
        Assertion.assertThat("Hotel Library page is loaded", hotelLibraryPage.isHotelLibraryPageLoaded(), is(true));

        step("Go to post-book score page");
        sidebarPage.clickSidebarMenu(SidebarPage.SidebarMenu.PROPERTY_SCORING);
        sidebarPage.clickSidebarSubmenu(SidebarPage.SidebarMenu.POST_BOOK_SCORE);
        Assertion.assertThat("Post-book score page is displayed", postBookScorePage.isPostBookScorePageDisplayed(), is(true));

        step("Select accommodation type and search");
        postBookScorePage.selectAccommodationType(accommodationType);
        postBookScorePage.clickSearch();

        step("Verify search result");
        Assertion.assertThat("Check columnHeader name ", postBookScorePage.getColumnHeader(), containsString("Name"));
        Assertion.assertThat("Check columnHeader type ", postBookScorePage.getColumnAccomType(),containsString("Accommodation Type"));
        Assertion.assertThat("Check rowData name result ", postBookScorePage.getColumnResultHotel().isEmpty(), is(false));
        Assertion.assertThat("Check rowData type result ", postBookScorePage.getColumnResultAccomType(), containsString("Hotel"));
    }
    @Author(names = {"Venkat"})
    @Module(name = "PostBookScore")
    @Tribe(name = "Supply")
    @Vertical(name = "Accommodation")
    @Test
    public void testDownloadPostBookScoreCsv() {
        Map<String, String> param = getParam("testDownloadPostBookScoreCsv");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPage<?> loginPage = new LoginPage<>(session);
        HotelLibraryPage<?> hotelLibraryPage = new HotelLibraryPage<>(session);
        SidebarPage<?> sidebarPage = new SidebarPage<>(session);
        PostBookScorePage<?> postBookScorePage = new PostBookScorePage<>(session);

        String username = param.get("username");
        String password = param.get("password");

        step("Open MM Module");
        loginPage.openMMModule(username, password);
        Assertion.assertThat("Hotel Library page is loaded", hotelLibraryPage.isHotelLibraryPageLoaded(), is(true));

        step("Go to post-book score page");
        sidebarPage.clickSidebarMenu(SidebarPage.SidebarMenu.PROPERTY_SCORING);
        sidebarPage.clickSidebarSubmenu(SidebarPage.SidebarMenu.POST_BOOK_SCORE);
        Assertion.assertThat("Post-book score page is displayed", postBookScorePage.isPostBookScorePageDisplayed(), is(true));

        step("Download CSV and verify success snackbar");
        postBookScorePage.clickDownloadCsv();
        Assertion.assertThat("Successful snackbar is appeared",
                postBookScorePage.isSuccessfulSnackbarAppeared(), is(true));
    }
}
