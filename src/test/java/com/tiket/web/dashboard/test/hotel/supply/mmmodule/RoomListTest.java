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
import com.tiket.web.dashboard.page.hotel.supply.mmmodule.RoomListPage;
import com.tiket.web.dashboard.page.hotel.supply.mmmodule.SidebarPage;
import org.testng.annotations.Test;

import java.util.Map;

import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.core.Is.is;

public class RoomListTest extends BaseTest {

    @Author(names = {"Raihan"})
    @Module(name = "RoomList")
    @Tribe(name = "Supply")
    @Vertical(name = "Accommodation")
    @Test
    public void testRoomListEditRoomMasterDataSingleCategory() {
        Map<String, String> param = getParam("testRoomListEditRoomMasterDataSingleCategory");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPage<?> loginPage = new LoginPage<>(session);
        HotelLibraryPage<?> hotelLibraryPage = new HotelLibraryPage<>(session);
        SidebarPage<?> sidebarPage = new SidebarPage<>(session);
        RoomListPage<?> roomListPage = new RoomListPage<>(session);

        String country = param.get("country");
        String region = param.get("region");
        String city = param.get("city");
        String area = param.get("area");
        String hotelName = param.get("hotelName");
        String roomNameType = param.get("roomNameType");
        String roomName = param.get("roomName");
        String categoryType = param.get("categoryType");
        String categoryValue = param.get("categoryValue");

        String username = param.get("username");
        String password = param.get("password");

        step("Open MM Module");
        loginPage.openMMModule(username, password);
        Assertion.assertThat("Hotel Library page is loaded", hotelLibraryPage.isHotelLibraryPageLoaded(), is(true));
        Assertion.assertThat("Search result is displayed", hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(), is(true));

        step("Go to room configuration page");
        sidebarPage.clickSidebarMenu(SidebarPage.SidebarMenu.ROOM_MASTER_DATA);
        sidebarPage.clickSidebarSubmenu(SidebarPage.SidebarMenu.ROOM_LIST);
        Assertion.assertThat("Room list page is displayed", roomListPage.isRoomListTitleDisplayed(), is(true));

        step("Search room list");
        roomListPage.selectFilterCountry(country);
        roomListPage.selectFilterRegion(region);
        roomListPage.selectFilterCity(city);
        roomListPage.selectFilterArea(area);
        roomListPage.seeAdvancedFilter();
        roomListPage.inputFilterHotelName(hotelName);
        roomListPage.selectFilterRoomNameType(roomNameType);
        roomListPage.inputFilterRoomName(roomName);
        roomListPage.clickSearch();
        roomListPage.scrollToRoomListTable();
        Assertion.assertThat("Search result is available", roomListPage.isSearchResultAvailableByHotelName(hotelName) && roomListPage.isSearchResultAvailableByOriginalRoomName(roomName), is(true));

        step("Edit room master data");
        roomListPage.checkRoomListSearchResultByIndex(1);
        roomListPage.clickEditRoomMasterData();
        Assertion.assertThat("Edit room master data modal is appeared", roomListPage.isEditRoomMasterDataModalDisplayed(), is(true));
        roomListPage.selectCategoryByIndex(1, categoryType, categoryValue);
        roomListPage.submitEditRoomMasterData();
        Assertion.assertThat("Successful snackbar is appeared", roomListPage.isSuccessfulSnackbarDisplayed(), is(true));
        Assertion.assertThat(String.format("%s is updated to %s successfully", categoryType, categoryValue), roomListPage.isSearchResultAvailableByAttribute(categoryType, categoryValue), is(true));
    }

    @Author(names = {"Raihan"})
    @Module(name = "RoomList")
    @Tribe(name = "Supply")
    @Vertical(name = "Accommodation")
    @Test
    public void testRoomListSearchByLocationAndHotelName() {
        Map<String, String> param = getParam("testRoomListSearchByLocationAndHotelName");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPage<?> loginPage = new LoginPage<>(session);
        HotelLibraryPage<?> hotelLibraryPage = new HotelLibraryPage<>(session);
        SidebarPage<?> sidebarPage = new SidebarPage<>(session);
        RoomListPage<?> roomListPage = new RoomListPage<>(session);

        String country = param.get("country");
        String region = param.get("region");
        String city = param.get("city");
        String area = param.get("area");
        String hotelName = param.get("hotelName");

        String username = param.get("username");
        String password = param.get("password");

        step("Open MM Module");
        loginPage.openMMModule(username, password);
        Assertion.assertThat("Hotel Library page is loaded", hotelLibraryPage.isHotelLibraryPageLoaded(), is(true));
        Assertion.assertThat("Search result is displayed", hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(), is(true));

        step("Go to room configuration page");
        sidebarPage.clickSidebarMenu(SidebarPage.SidebarMenu.ROOM_MASTER_DATA);
        sidebarPage.clickSidebarSubmenu(SidebarPage.SidebarMenu.ROOM_LIST);
        Assertion.assertThat("Room list page is displayed", roomListPage.isRoomListTitleDisplayed(), is(true));

        step("Search room list");
        roomListPage.selectFilterCountry(country);
        roomListPage.selectFilterRegion(region);
        roomListPage.selectFilterCity(city);
        roomListPage.selectFilterArea(area);
        roomListPage.seeAdvancedFilter();
        roomListPage.scrollToRoomListTable();
        roomListPage.inputFilterHotelName(hotelName);
        roomListPage.clickSearch();
        roomListPage.scrollToRoomListTable();
        Assertion.assertThat("Search result is available", roomListPage.isSearchResultAvailableByHotelName(hotelName), is(true));
    }

    @Author(names = {"Raihan"})
    @Module(name = "RoomList")
    @Tribe(name = "Supply")
    @Vertical(name = "Accommodation")
    @Test
    public void testRoomListSearchByLocationRoomNameTypeAndRoomNameKeyword() {
        Map<String, String> param = getParam("testRoomListSearchByLocationRoomNameTypeAndRoomNameKeyword");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPage<?> loginPage = new LoginPage<>(session);
        HotelLibraryPage<?> hotelLibraryPage = new HotelLibraryPage<>(session);
        SidebarPage<?> sidebarPage = new SidebarPage<>(session);
        RoomListPage<?> roomListPage = new RoomListPage<>(session);

        String country = param.get("country");
        String region = param.get("region");
        String city = param.get("city");
        String area = param.get("area");
        String roomNameType = param.get("roomNameType");
        String roomName = param.get("roomName");

        String username = param.get("username");
        String password = param.get("password");

        step("Open MM Module");
        loginPage.openMMModule(username, password);
        Assertion.assertThat("Hotel Library page is loaded", hotelLibraryPage.isHotelLibraryPageLoaded(), is(true));
        Assertion.assertThat("Search result is displayed", hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(), is(true));

        step("Go to room configuration page");
        sidebarPage.clickSidebarMenu(SidebarPage.SidebarMenu.ROOM_MASTER_DATA);
        sidebarPage.clickSidebarSubmenu(SidebarPage.SidebarMenu.ROOM_LIST);
        Assertion.assertThat("Room list page is displayed", roomListPage.isRoomListTitleDisplayed(), is(true));

        step("Search room list");
        roomListPage.selectFilterCountry(country);
        roomListPage.selectFilterRegion(region);
        roomListPage.selectFilterCity(city);
        roomListPage.selectFilterArea(area);
        roomListPage.seeAdvancedFilter();
        roomListPage.scrollToRoomListTable();
        roomListPage.selectFilterRoomNameType(roomNameType);
        roomListPage.inputFilterRoomName(roomName);
        roomListPage.clickSearch();
        roomListPage.scrollToRoomListTable();
        Assertion.assertThat("Search result is available", roomListPage.isSearchResultAvailableByOriginalRoomName(roomName), is(true));
    }

    @Author(names = {"Raihan"})
    @Module(name = "RoomList")
    @Tribe(name = "Supply")
    @Vertical(name = "Accommodation")
    @Test
    public void testRoomListSearchByLocationSmokingPolicyAndPropertyType() {
        Map<String, String> param = getParam("testRoomListSearchByLocationSmokingPolicyAndPropertyType");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPage<?> loginPage = new LoginPage<>(session);
        HotelLibraryPage<?> hotelLibraryPage = new HotelLibraryPage<>(session);
        SidebarPage<?> sidebarPage = new SidebarPage<>(session);
        RoomListPage<?> roomListPage = new RoomListPage<>(session);

        String country = param.get("country");
        String region = param.get("region");
        String city = param.get("city");
        String area = param.get("area");
        String smokingPolicy = param.get("smokingPolicy");
        String propertyType = param.get("propertyType");

        String username = param.get("username");
        String password = param.get("password");

        step("Open MM Module");
        loginPage.openMMModule(username, password);
        Assertion.assertThat("Hotel Library page is loaded", hotelLibraryPage.isHotelLibraryPageLoaded(), is(true));
        Assertion.assertThat("Search result is displayed", hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(), is(true));

        step("Go to room configuration page");
        sidebarPage.clickSidebarMenu(SidebarPage.SidebarMenu.ROOM_MASTER_DATA);
        sidebarPage.clickSidebarSubmenu(SidebarPage.SidebarMenu.ROOM_LIST);
        Assertion.assertThat("Room list page is displayed", roomListPage.isRoomListTitleDisplayed(), is(true));

        step("Search room list");
        roomListPage.selectFilterCountry(country);
        roomListPage.selectFilterRegion(region);
        roomListPage.selectFilterCity(city);
        roomListPage.selectFilterArea(area);
        roomListPage.seeAdvancedFilter();
        roomListPage.selectFilterSmokingPolicy(smokingPolicy);
        roomListPage.selectFilterPropertyType(propertyType);
        roomListPage.clickSearch();
        Assertion.assertThat("Search result is available", roomListPage.isSearchResultAvailableByPropertyType(propertyType) && roomListPage.isSearchResultAvailableBySmokingPolicy(smokingPolicy), is(true));
    }

    @Author(names = {"Raihan"})
    @Module(name = "RoomList")
    @Tribe(name = "Supply")
    @Vertical(name = "Accommodation")
    @Test
    public void testRoomListSearchByLocationRoomTypeBedTypeAndNoOfRooms() {
        Map<String, String> param = getParam("testRoomListSearchByLocationRoomTypeBedTypeAndNoOfRooms");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPage<?> loginPage = new LoginPage<>(session);
        HotelLibraryPage<?> hotelLibraryPage = new HotelLibraryPage<>(session);
        SidebarPage<?> sidebarPage = new SidebarPage<>(session);
        RoomListPage<?> roomListPage = new RoomListPage<>(session);

        String country = param.get("country");
        String region = param.get("region");
        String city = param.get("city");
        String area = param.get("area");
        String roomType = param.get("roomType");
        String bedType = param.get("bedType");
        String noOfRooms = param.get("noOfRooms");

        String username = param.get("username");
        String password = param.get("password");

        step("Open MM Module");
        loginPage.openMMModule(username, password);
        Assertion.assertThat("Hotel Library page is loaded", hotelLibraryPage.isHotelLibraryPageLoaded(), is(true));
        Assertion.assertThat("Search result is displayed", hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(), is(true));

        step("Go to room configuration page");
        sidebarPage.clickSidebarMenu(SidebarPage.SidebarMenu.ROOM_MASTER_DATA);
        sidebarPage.clickSidebarSubmenu(SidebarPage.SidebarMenu.ROOM_LIST);
        Assertion.assertThat("Room list page is displayed", roomListPage.isRoomListTitleDisplayed(), is(true));

        step("Search room list");
        roomListPage.selectFilterCountry(country);
        roomListPage.selectFilterRegion(region);
        roomListPage.selectFilterCity(city);
        roomListPage.selectFilterArea(area);
        roomListPage.seeAdvancedFilter();
        roomListPage.selectFilterRoomType(roomType);
        roomListPage.selectFilterBedType(bedType);
        roomListPage.selectFilterNoOfRooms(noOfRooms);
        roomListPage.clickSearch();
        Assertion.assertThat("Search result is available", roomListPage.isSearchResultAvailableByRoomType(roomType) && roomListPage.isSearchResultAvailableByBedType(bedType) && roomListPage.isSearchResultAvailableByNoOfRooms(noOfRooms), is(true));
    }

    @Author(names = {"Raihan"})
    @Module(name = "RoomList")
    @Tribe(name = "Supply")
    @Vertical(name = "Accommodation")
    @Test
    public void testRoomListEditRoomMasterDataMultipleCategory() {
        Map<String, String> param = getParam("testRoomListEditRoomMasterDataMultipleCategory");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPage<?> loginPage = new LoginPage<>(session);
        HotelLibraryPage<?> hotelLibraryPage = new HotelLibraryPage<>(session);
        SidebarPage<?> sidebarPage = new SidebarPage<>(session);
        RoomListPage<?> roomListPage = new RoomListPage<>(session);

        String country = param.get("country");
        String region = param.get("region");
        String city = param.get("city");
        String area = param.get("area");
        String hotelName = param.get("hotelName");
        String roomNameType = param.get("roomNameType");
        String roomName = param.get("roomName");
        String categoryType = param.get("categoryType");
        String categoryValue = param.get("categoryValue");
        String categoryType2 = param.get("categoryType2");
        String categoryValue2 = param.get("categoryValue2");

        String username = param.get("username");
        String password = param.get("password");

        step("Open MM Module");
        loginPage.openMMModule(username, password);
        Assertion.assertThat("Hotel Library page is loaded", hotelLibraryPage.isHotelLibraryPageLoaded(), is(true));
        Assertion.assertThat("Search result is displayed", hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(), is(true));

        step("Go to room configuration page");
        sidebarPage.clickSidebarMenu(SidebarPage.SidebarMenu.ROOM_MASTER_DATA);
        sidebarPage.clickSidebarSubmenu(SidebarPage.SidebarMenu.ROOM_LIST);
        Assertion.assertThat("Room list page is displayed", roomListPage.isRoomListTitleDisplayed(), is(true));

        step("Search room list");
        roomListPage.selectFilterCountry(country);
        roomListPage.selectFilterRegion(region);
        roomListPage.selectFilterCity(city);
        roomListPage.selectFilterArea(area);
        roomListPage.seeAdvancedFilter();
        roomListPage.scrollToRoomListTable();
        roomListPage.inputFilterHotelName(hotelName);
        roomListPage.selectFilterRoomNameType(roomNameType);
        roomListPage.inputFilterRoomName(roomName);
        roomListPage.clickSearch();
        roomListPage.scrollToRoomListTable();
        Assertion.assertThat("Search result is available", roomListPage.isSearchResultAvailableByHotelName(hotelName) && roomListPage.isSearchResultAvailableByOriginalRoomName(roomName), is(true));

        step("Edit room master data");
        roomListPage.checkRoomListSearchResultByIndex(1);
        roomListPage.clickEditRoomMasterData();
        Assertion.assertThat("Edit room master data modal is appeared", roomListPage.isEditRoomMasterDataModalDisplayed(), is(true));
        roomListPage.selectCategoryByIndex(1, categoryType, categoryValue);
        roomListPage.clickAddAnotherCategory();
        roomListPage.selectCategoryByIndex(2, categoryType2, categoryValue2);
        roomListPage.submitEditRoomMasterData();
        Assertion.assertThat("Successful snackbar is appeared", roomListPage.isSuccessfulSnackbarDisplayed(), is(true));
        Assertion.assertThat(String.format("%s is updated to %s successfully", categoryType, categoryValue), roomListPage.isSearchResultAvailableByAttribute(categoryType, categoryValue), is(true));
        Assertion.assertThat(String.format("%s is updated to %s successfully", categoryType2, categoryValue2), roomListPage.isSearchResultAvailableByAttribute(categoryType2, categoryValue2), is(true));
    }

    @Author(names = {"Raihan"})
    @Module(name = "RoomList")
    @Tribe(name = "Supply")
    @Vertical(name = "Accommodation")
    @Test
    public void testRoomListExportSearchResultToCsv() {
        Map<String, String> param = getParam("testRoomListExportSearchResultToCsv");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPage<?> loginPage = new LoginPage<>(session);
        HotelLibraryPage<?> hotelLibraryPage = new HotelLibraryPage<>(session);
        SidebarPage<?> sidebarPage = new SidebarPage<>(session);
        RoomListPage<?> roomListPage = new RoomListPage<>(session);

        String country = param.get("country");
        String region = param.get("region");
        String city = param.get("city");
        String area = param.get("area");
        String hotelName = param.get("hotelName");
        String roomNameType = param.get("roomNameType");
        String roomName = param.get("roomName");

        String username = param.get("username");
        String password = param.get("password");

        step("Open MM Module");
        loginPage.openMMModule(username, password);
        Assertion.assertThat("Hotel Library page is loaded", hotelLibraryPage.isHotelLibraryPageLoaded(), is(true));
        Assertion.assertThat("Search result is displayed", hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(), is(true));

        step("Go to room configuration page");
        sidebarPage.clickSidebarMenu(SidebarPage.SidebarMenu.ROOM_MASTER_DATA);
        sidebarPage.clickSidebarSubmenu(SidebarPage.SidebarMenu.ROOM_LIST);
        Assertion.assertThat("Room list page is displayed", roomListPage.isRoomListTitleDisplayed(), is(true));

        step("Search room list");
        roomListPage.selectFilterCountry(country);
        roomListPage.selectFilterRegion(region);
        roomListPage.selectFilterCity(city);
        roomListPage.selectFilterArea(area);
        roomListPage.seeAdvancedFilter();
        roomListPage.scrollToRoomListTable();
        roomListPage.inputFilterHotelName(hotelName);
        roomListPage.selectFilterRoomNameType(roomNameType);
        roomListPage.inputFilterRoomName(roomName);
        roomListPage.clickSearch();
        roomListPage.scrollToRoomListTable();
        Assertion.assertThat("Search result is available", roomListPage.isSearchResultAvailableByHotelName(hotelName) && roomListPage.isSearchResultAvailableByOriginalRoomName(roomName), is(true));

        step("Export search result to csv file");
        roomListPage.clickExportToCsv();
        Assertion.assertThat("Successful snackbar is appeared", roomListPage.isSuccessfulSnackbarDisplayed(), is(true));
    }
}
