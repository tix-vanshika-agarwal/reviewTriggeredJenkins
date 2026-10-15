package com.tiket.web.dashboard.test.hotel.supply.mmmodule;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.web.dashboard.page.hotel.supply.mmmodule.DailyMergePage;
import com.tiket.web.dashboard.page.hotel.supply.mmmodule.HotelLibraryPage;
import com.tiket.web.dashboard.page.hotel.supply.mmmodule.LoginPage;
import com.tiket.web.dashboard.page.hotel.supply.mmmodule.SidebarPage;
import org.testng.annotations.Test;

import java.util.Map;

import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.core.Is.is;

public class DailyMergeTest extends BaseTest {
    @Author(names = {"Raihan"})
    @Module(name = "DailyMerge")
    @Tribe(name = "Supply")
    @Vertical(name = "Accommodation")
    @Test
    public void testDailyMergeSearchByCountry() {
        Map<String, String> param = getParam("testDailyMergeSearchByCountry");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPage<?> loginPage = new LoginPage<>(session);
        HotelLibraryPage<?> hotelLibraryPage = new HotelLibraryPage<>(session);
        SidebarPage<?> sidebarPage = new SidebarPage<>(session);
        DailyMergePage<?> dailyMergePage = new DailyMergePage<>(session);

        String country = param.get("country");

        String username = param.get("username");
        String password = param.get("password");

        step("Open MM Module");
        loginPage.openMMModule(username, password);
        Assertion.assertThat("Hotel Library page is loaded", hotelLibraryPage.isHotelLibraryPageLoaded(), is(true));
        Assertion.assertThat("Search result is displayed", hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(), is(true));

        step("Go to daily merge page");
        sidebarPage.clickSidebarMenu(SidebarPage.SidebarMenu.HOTEL_MERGE);
        sidebarPage.clickSidebarSubmenu(SidebarPage.SidebarMenu.DAILY_MERGE);
        Assertion.assertThat("Daily merge page is displayed", dailyMergePage.isDailyMergePageDisplayed(), is(true));

        step("Search daily hotel merge list by country");
        dailyMergePage.selectFilterCountry(country);
        dailyMergePage.clickSearch();
        dailyMergePage.assertSearchResultNewHotelDisplayedBySearchCriteria("COUNTRY", country);
    }

    @Author(names = {"Raihan"})
    @Module(name = "DailyMerge")
    @Tribe(name = "Supply")
    @Vertical(name = "Accommodation")
    @Test
    public void testDailyMergeSearchByRegion() {
        Map<String, String> param = getParam("testDailyMergeSearchByRegion");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPage<?> loginPage = new LoginPage<>(session);
        HotelLibraryPage<?> hotelLibraryPage = new HotelLibraryPage<>(session);
        SidebarPage<?> sidebarPage = new SidebarPage<>(session);
        DailyMergePage<?> dailyMergePage = new DailyMergePage<>(session);

        String country = param.get("country");
        String region = param.get("region");

        String username = param.get("username");
        String password = param.get("password");

        step("Open MM Module");
        loginPage.openMMModule(username, password);
        Assertion.assertThat("Hotel Library page is loaded", hotelLibraryPage.isHotelLibraryPageLoaded(), is(true));
        Assertion.assertThat("Search result is displayed", hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(), is(true));

        step("Go to daily merge page");
        sidebarPage.clickSidebarMenu(SidebarPage.SidebarMenu.HOTEL_MERGE);
        sidebarPage.clickSidebarSubmenu(SidebarPage.SidebarMenu.DAILY_MERGE);
        Assertion.assertThat("Daily merge page is displayed", dailyMergePage.isDailyMergePageDisplayed(), is(true));

        step("Search daily hotel merge list by region");
        dailyMergePage.selectFilterCountry(country);
        dailyMergePage.selectFilterRegion(region);
        dailyMergePage.clickSearch();
        dailyMergePage.assertSearchResultNewHotelDisplayedBySearchCriteria("REGION", region);
    }

    @Author(names = {"Raihan"})
    @Module(name = "DailyMerge")
    @Tribe(name = "Supply")
    @Vertical(name = "Accommodation")
    @Test
    public void testDailyMergeSearchByCity() {
        Map<String, String> param = getParam("testDailyMergeSearchByCity");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPage<?> loginPage = new LoginPage<>(session);
        HotelLibraryPage<?> hotelLibraryPage = new HotelLibraryPage<>(session);
        SidebarPage<?> sidebarPage = new SidebarPage<>(session);
        DailyMergePage<?> dailyMergePage = new DailyMergePage<>(session);

        String country = param.get("country");
        String region = param.get("region");
        String city = param.get("city");

        String username = param.get("username");
        String password = param.get("password");

        step("Open MM Module");
        loginPage.openMMModule(username, password);
        Assertion.assertThat("Hotel Library page is loaded", hotelLibraryPage.isHotelLibraryPageLoaded(), is(true));
        Assertion.assertThat("Search result is displayed", hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(), is(true));

        step("Go to daily merge page");
        sidebarPage.clickSidebarMenu(SidebarPage.SidebarMenu.HOTEL_MERGE);
        sidebarPage.clickSidebarSubmenu(SidebarPage.SidebarMenu.DAILY_MERGE);
        Assertion.assertThat("Daily merge page is displayed", dailyMergePage.isDailyMergePageDisplayed(), is(true));

        step("Search daily hotel merge list by city");
        dailyMergePage.selectFilterCountry(country);
        dailyMergePage.selectFilterRegion(region);
        dailyMergePage.selectFilterCity(city);
        dailyMergePage.clickSearch();
        dailyMergePage.assertSearchResultNewHotelDisplayedBySearchCriteria("CITY", city);
    }

    @Author(names = {"Raihan"})
    @Module(name = "DailyMerge")
    @Tribe(name = "Supply")
    @Vertical(name = "Accommodation")
    @Test
    public void testDailyMergeSearchByArea() {
        Map<String, String> param = getParam("testDailyMergeSearchByArea");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPage<?> loginPage = new LoginPage<>(session);
        HotelLibraryPage<?> hotelLibraryPage = new HotelLibraryPage<>(session);
        SidebarPage<?> sidebarPage = new SidebarPage<>(session);
        DailyMergePage<?> dailyMergePage = new DailyMergePage<>(session);

        String country = param.get("country");
        String region = param.get("region");
        String city = param.get("city");
        String area = param.get("area");

        String username = param.get("username");
        String password = param.get("password");

        step("Open MM Module");
        loginPage.openMMModule(username, password);
        Assertion.assertThat("Hotel Library page is loaded", hotelLibraryPage.isHotelLibraryPageLoaded(), is(true));
        Assertion.assertThat("Search result is displayed", hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(), is(true));

        step("Go to daily merge page");
        sidebarPage.clickSidebarMenu(SidebarPage.SidebarMenu.HOTEL_MERGE);
        sidebarPage.clickSidebarSubmenu(SidebarPage.SidebarMenu.DAILY_MERGE);
        Assertion.assertThat("Daily merge page is displayed", dailyMergePage.isDailyMergePageDisplayed(), is(true));

        step("Search daily hotel merge list by city");
        dailyMergePage.selectFilterCountry(country);
        dailyMergePage.selectFilterRegion(region);
        dailyMergePage.selectFilterCity(city);
        dailyMergePage.selectFilterArea(area);
        dailyMergePage.clickSearch();
        dailyMergePage.assertSearchResultNewHotelDisplayedBySearchCriteria("AREA", area);
    }

    @Author(names = {"Raihan"})
    @Module(name = "DailyMerge")
    @Tribe(name = "Supply")
    @Vertical(name = "Accommodation")
    @Test
    public void testDailyMergeSearchByStatus() {
        Map<String, String> param = getParam("testDailyMergeSearchByStatus");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPage<?> loginPage = new LoginPage<>(session);
        HotelLibraryPage<?> hotelLibraryPage = new HotelLibraryPage<>(session);
        SidebarPage<?> sidebarPage = new SidebarPage<>(session);
        DailyMergePage<?> dailyMergePage = new DailyMergePage<>(session);

        String country = param.get("country");
        String region = param.get("region");
        String status = param.get("status");
        String status2 = param.get("status2");

        String username = param.get("username");
        String password = param.get("password");

        step("Open MM Module");
        loginPage.openMMModule(username, password);
        Assertion.assertThat("Hotel Library page is loaded", hotelLibraryPage.isHotelLibraryPageLoaded(), is(true));
        Assertion.assertThat("Search result is displayed", hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(), is(true));

        step("Go to daily merge page");
        sidebarPage.clickSidebarMenu(SidebarPage.SidebarMenu.HOTEL_MERGE);
        sidebarPage.clickSidebarSubmenu(SidebarPage.SidebarMenu.DAILY_MERGE);
        Assertion.assertThat("Daily merge page is displayed", dailyMergePage.isDailyMergePageDisplayed(), is(true));

        step(String.format("Search daily hotel merge list by status %s", status));
        dailyMergePage.selectFilterCountry(country);
        dailyMergePage.selectFilterRegion(region);
        dailyMergePage.selectFilterStatus(status);
        dailyMergePage.clickSearch();
        Assertion.assertThat(String.format("Search result by status %s is available", status), dailyMergePage.isSearchResultDisplayedByStatus(status), is(true));

        step(String.format("Search daily hotel merge list by status %s", status2));
        dailyMergePage.selectFilterStatus(status2);
        dailyMergePage.clickSearch();
        Assertion.assertThat(String.format("Search result by status %s is available", status2), dailyMergePage.isSearchResultDisplayedByStatus(status2), is(true));
    }

    @Author(names = {"Raihan"})
    @Module(name = "DailyMerge")
    @Tribe(name = "Supply")
    @Vertical(name = "Accommodation")
    @Test
    public void testDailyMergeSearchByReviewed() {
        Map<String, String> param = getParam("testDailyMergeSearchByReviewed");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPage<?> loginPage = new LoginPage<>(session);
        HotelLibraryPage<?> hotelLibraryPage = new HotelLibraryPage<>(session);
        SidebarPage<?> sidebarPage = new SidebarPage<>(session);
        DailyMergePage<?> dailyMergePage = new DailyMergePage<>(session);

        String country = param.get("country");
        String region = param.get("region");
        boolean reviewed = Boolean.parseBoolean(param.get("reviewed"));
        boolean reviewed2 = Boolean.parseBoolean(param.get("reviewed2"));

        String username = param.get("username");
        String password = param.get("password");

        step("Open MM Module");
        loginPage.openMMModule(username, password);
        Assertion.assertThat("Hotel Library page is loaded", hotelLibraryPage.isHotelLibraryPageLoaded(), is(true));
        Assertion.assertThat("Search result is displayed", hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(), is(true));

        step("Go to daily merge page");
        sidebarPage.clickSidebarMenu(SidebarPage.SidebarMenu.HOTEL_MERGE);
        sidebarPage.clickSidebarSubmenu(SidebarPage.SidebarMenu.DAILY_MERGE);
        Assertion.assertThat("Daily merge page is displayed", dailyMergePage.isDailyMergePageDisplayed(), is(true));

        step(String.format("Search daily hotel merge list by reviewed status %s", reviewed));
        dailyMergePage.selectFilterCountry(country);
        dailyMergePage.selectFilterRegion(region);
        dailyMergePage.selectFilterReviewed(reviewed);
        dailyMergePage.clickSearch();
        Assertion.assertThat(String.format("Search result by reviewed status %s is available", reviewed), dailyMergePage.isSearchResultDisplayedByReviewed(reviewed), is(true));

        step(String.format("Search daily hotel merge list by reviewed status %s", reviewed2));
        dailyMergePage.selectFilterReviewed(reviewed2);
        dailyMergePage.clickSearch();
        Assertion.assertThat(String.format("Search result by reviewed status %s is available", reviewed2), dailyMergePage.isSearchResultDisplayedByReviewed(reviewed2), is(true));
    }

    @Author(names = {"Raihan"})
    @Module(name = "DailyMerge")
    @Tribe(name = "Supply")
    @Vertical(name = "Accommodation")
    @Test
    public void testDailyMergeSearchByKeyword() {
        Map<String, String> param = getParam("testDailyMergeSearchByKeyword");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPage<?> loginPage = new LoginPage<>(session);
        HotelLibraryPage<?> hotelLibraryPage = new HotelLibraryPage<>(session);
        SidebarPage<?> sidebarPage = new SidebarPage<>(session);
        DailyMergePage<?> dailyMergePage = new DailyMergePage<>(session);

        String country = param.get("country");
        String region = param.get("region");
        String keyword = param.get("keyword");

        String username = param.get("username");
        String password = param.get("password");

        step("Open MM Module");
        loginPage.openMMModule(username, password);
        Assertion.assertThat("Hotel Library page is loaded", hotelLibraryPage.isHotelLibraryPageLoaded(), is(true));
        Assertion.assertThat("Search result is displayed", hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(), is(true));

        step("Go to daily merge page");
        sidebarPage.clickSidebarMenu(SidebarPage.SidebarMenu.HOTEL_MERGE);
        sidebarPage.clickSidebarSubmenu(SidebarPage.SidebarMenu.DAILY_MERGE);
        Assertion.assertThat("Daily merge page is displayed", dailyMergePage.isDailyMergePageDisplayed(), is(true));

        step("Search daily hotel merge list by keyword");
        dailyMergePage.selectFilterCountry(country);
        dailyMergePage.selectFilterRegion(region);
        dailyMergePage.enterKeyword(keyword);
        dailyMergePage.clickSearch();
        dailyMergePage.assertSearchResultNewHotelDisplayedBySearchCriteria("HOTEL_NAME", keyword);
    }
}
