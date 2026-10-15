package com.tiket.web.dashboard.test.hotel.supply.mmmodule;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.web.dashboard.page.common.LoginMemberAdmin;
import com.tiket.web.dashboard.page.hotel.supply.mmmodule.HotelLibraryPage;
import com.tiket.web.dashboard.page.hotel.supply.mmmodule.LoginPage;
import com.tiket.web.dashboard.page.hotel.supply.mmmodule.RoomConfigurationPage;
import com.tiket.web.dashboard.page.hotel.supply.mmmodule.SidebarPage;
import static org.hamcrest.Matchers.containsString;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.core.Is.is;

public class RoomConfigurationTest extends BaseTest {

    @Author(names = {"Raihan"})
    @Module(name = "RoomConfiguration")
    @Tribe(name = "Supply")
    @Vertical(name = "Accommodation")
    @Test
    public void testCreateCustomRoomNameByChainAndBrand() {
        Map<String, String> param = getParam("testCreateCustomRoomNameByChainAndBrand");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPage<?> loginPage = new LoginPage<>(session);
        HotelLibraryPage<?> hotelLibraryPage = new HotelLibraryPage<>(session);
        SidebarPage<?> sidebarPage = new SidebarPage<>(session);
        RoomConfigurationPage<?> roomConfigurationPage = new RoomConfigurationPage<>(session);

        String chain = param.get("chain");
        String keyword = param.get("keyword");
        String enCustomRoomName = param.get("enCustomRoomName");
        String idCustomRoomName = param.get("idCustomRoomName");

        String username = param.get("username");
        String password = param.get("password");

        step("Open MM Module");
        loginPage.openMMModule(username, password);
        Assertion.assertThat("Hotel Library page is loaded", hotelLibraryPage.isHotelLibraryPageLoaded(), is(true));
        Assertion.assertThat("Search result is displayed", hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(), is(true));

        step("Go to room configuration page");
        sidebarPage.clickSidebarMenu(SidebarPage.SidebarMenu.ROOM_MASTER_DATA);
        sidebarPage.clickSidebarSubmenu(SidebarPage.SidebarMenu.ROOM_CONFIGURATION);
        Assertion.assertThat("Room configuration page is displayed", roomConfigurationPage.isRoomConfigurationPageDisplayed() && roomConfigurationPage.isCustomRoomNameListDisplayed(), is(true));

        step("Add new custom room name based on hotel chain and brand search");
        roomConfigurationPage.clickAddCustomRoomName();
        Assertion.assertThat("Add custom room name form is displayed", roomConfigurationPage.isAddCustomRoomNameFormDisplayed(), is(true));
        roomConfigurationPage.searchAndSelectHotelChain(chain);
        roomConfigurationPage.inputKeyword(keyword);
        roomConfigurationPage.clickSearchRoom();
        roomConfigurationPage.inputCustomRoomName(enCustomRoomName, idCustomRoomName);
        roomConfigurationPage.clickSubmitAddNewCustomRoomName();
        Assertion.assertThat("Successful snackbar is appeared", roomConfigurationPage.isSuccessfulSnackbarAppeared(), is(true));

        step("Verify newly added custom room name is presented on custom room name list");
        sidebarPage.clickSidebarSubmenu(SidebarPage.SidebarMenu.ROOM_CONFIGURATION);
        Assertion.assertThat("Room configuration page is displayed", roomConfigurationPage.isRoomConfigurationPageDisplayed() && roomConfigurationPage.isCustomRoomNameListDisplayed(), is(true));
        roomConfigurationPage.inputCustomRoomNameFilter(enCustomRoomName);
        roomConfigurationPage.clickSearchCustomRoomName();
        Assertion.assertThat(String.format("Custom room name is found. EN: %s, ID: %s", enCustomRoomName, idCustomRoomName), roomConfigurationPage.isCustomRoomNameSearchResultAppeared(1, enCustomRoomName, idCustomRoomName), is(true));
     }

    @Author(names = {"Raihan"})
    @Module(name = "RoomConfiguration")
    @Tribe(name = "Supply")
    @Vertical(name = "Accommodation")
    @Test
    public void testCreateCustomRoomNameByHotelName() {
        Map<String, String> param = getParam("testCreateCustomRoomNameByHotelName");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPage<?> loginPage = new LoginPage<>(session);
        HotelLibraryPage<?> hotelLibraryPage = new HotelLibraryPage<>(session);
        SidebarPage<?> sidebarPage = new SidebarPage<>(session);
        RoomConfigurationPage<?> roomConfigurationPage = new RoomConfigurationPage<>(session);

        String country = param.get("country");
        String region = param.get("region");
        String city = param.get("city");
        String area = param.get("area");
        String hotelName = param.get("hotelName");
        String keyword = param.get("keyword");
        String enCustomRoomName = param.get("enCustomRoomName");
        String idCustomRoomName = param.get("idCustomRoomName");
        int index = Integer.parseInt(param.get("index"));

        String username = param.get("username");
        String password = param.get("password");

        step("Open MM Module");
        loginPage.openMMModule(username, password);
        Assertion.assertThat("Hotel Library page is loaded", hotelLibraryPage.isHotelLibraryPageLoaded(), is(true));
        Assertion.assertThat("Search result is displayed", hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(), is(true));

        step("Go to room configuration page");
        sidebarPage.clickSidebarMenu(SidebarPage.SidebarMenu.ROOM_MASTER_DATA);
        sidebarPage.clickSidebarSubmenu(SidebarPage.SidebarMenu.ROOM_CONFIGURATION);
        Assertion.assertThat("Room configuration page is displayed", roomConfigurationPage.isRoomConfigurationPageDisplayed() && roomConfigurationPage.isCustomRoomNameListDisplayed(), is(true));

        step("Add new custom room name based on hotel chain and brand search");
        roomConfigurationPage.clickAddCustomRoomName();
        Assertion.assertThat("Add custom room name form is displayed", roomConfigurationPage.isAddCustomRoomNameFormDisplayed(), is(true));
        roomConfigurationPage.clickHotelNameTab();
        roomConfigurationPage.searchAndSelectCountry(country);
        roomConfigurationPage.searchAndSelectRegion(region);
        roomConfigurationPage.searchAndSelectCity(city);
        roomConfigurationPage.searchAndSelectArea(area);
        roomConfigurationPage.searchAndSelectHotelName(hotelName);
        roomConfigurationPage.inputKeyword(keyword);
        roomConfigurationPage.clickSearchRoom();
        roomConfigurationPage.inputCustomRoomName(enCustomRoomName, idCustomRoomName);
        roomConfigurationPage.clickSubmitAddNewCustomRoomName();
        Assertion.assertThat("Successful snackbar is appeared", roomConfigurationPage.isSuccessfulSnackbarAppeared(), is(true));

        step("Verify newly added custom room name is presented on custom room name list");
        sidebarPage.clickSidebarSubmenu(SidebarPage.SidebarMenu.ROOM_CONFIGURATION);
        Assertion.assertThat("Room configuration page is displayed", roomConfigurationPage.isRoomConfigurationPageDisplayed() && roomConfigurationPage.isCustomRoomNameListDisplayed(), is(true));
        roomConfigurationPage.inputCustomRoomNameFilter(enCustomRoomName);
        roomConfigurationPage.clickSearchCustomRoomName();
        Assertion.assertThat(String.format("Custom room name is found. EN: %s, ID: %s", enCustomRoomName, idCustomRoomName), roomConfigurationPage.isCustomRoomNameSearchResultAppeared(1, enCustomRoomName, idCustomRoomName), is(true));

        step("Remove custom room name to revert the condition");
        roomConfigurationPage.clickSettingCustomRoomName(index);
        roomConfigurationPage.clickDeleteCustomRoomName(index);
        Assertion.assertThat("Remove custom room name modal is appeared", roomConfigurationPage.isDeleteCustomRoomNameConfirmModalDisplayed(), is(true));
        roomConfigurationPage.clickSubmitDeleteCustomRoomName();
        Assertion.assertThat("Successful snackbar is appeared", roomConfigurationPage.isSuccessfulSnackbarAppeared(), is(true));
        roomConfigurationPage.inputCustomRoomNameFilter(enCustomRoomName);
        roomConfigurationPage.clickSearchCustomRoomName();
        Assertion.assertThat("Custom room name not found", roomConfigurationPage.isCustomRoomNameSearchResultNotFound(), is(true));
    }

    @Author(names = {"Raihan"})
    @Module(name = "RoomConfiguration")
    @Tribe(name = "Supply")
    @Vertical(name = "Accommodation")
    @Test(dependsOnMethods = {"testCreateCustomRoomNameByChainAndBrand"})
    public void testUpdateCustomRoomName() {
        Map<String, String> param = getParam("testUpdateCustomRoomName");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPage<?> loginPage = new LoginPage<>(session);
        HotelLibraryPage<?> hotelLibraryPage = new HotelLibraryPage<>(session);
        SidebarPage<?> sidebarPage = new SidebarPage<>(session);
        RoomConfigurationPage<?> roomConfigurationPage = new RoomConfigurationPage<>(session);

        String existingEnCustomRoomName = param.get("existingEnCustomRoomName");
        String existingIdCustomRoomName = param.get("existingIdCustomRoomName");
        String enCustomRoomName = param.get("enCustomRoomName");
        String idCustomRoomName = param.get("idCustomRoomName");
        int index = Integer.parseInt(param.get("index"));

        String username = param.get("username");
        String password = param.get("password");

        step("Open MM Module");
        loginPage.openMMModule(username, password);
        Assertion.assertThat("Hotel Library page is loaded", hotelLibraryPage.isHotelLibraryPageLoaded(), is(true));
        Assertion.assertThat("Search result is displayed", hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(), is(true));

        step("Go to room configuration page");
        sidebarPage.clickSidebarMenu(SidebarPage.SidebarMenu.ROOM_MASTER_DATA);
        sidebarPage.clickSidebarSubmenu(SidebarPage.SidebarMenu.ROOM_CONFIGURATION);
        Assertion.assertThat("Room configuration page is displayed", roomConfigurationPage.isRoomConfigurationPageDisplayed() && roomConfigurationPage.isCustomRoomNameListDisplayed(), is(true));

        step("Search custom room name");
        roomConfigurationPage.inputCustomRoomNameFilter(existingEnCustomRoomName);
        roomConfigurationPage.clickSearchCustomRoomName();
        Assertion.assertThat(String.format("Custom room name is found. EN: %s, ID: %s", existingEnCustomRoomName, existingIdCustomRoomName), roomConfigurationPage.isCustomRoomNameSearchResultAppeared(index, existingEnCustomRoomName, existingIdCustomRoomName), is(true));
        String currentEnCustomRoomName = roomConfigurationPage.getEnCustomRoomNameLabel(index);
        String currentIdCustomRoomName = roomConfigurationPage.getIdCustomRoomNameLabel(index);

        step("Update custom room name");
        roomConfigurationPage.clickSettingCustomRoomName(index);
        roomConfigurationPage.clickViewCustomRoomName(index);
        Assertion.assertThat("Update custom room name form is displayed", roomConfigurationPage.isUpdateCustomRoomNameFormDisplayed() && roomConfigurationPage.isUpdateCustomRoomNameLabelDisplayedCorrectly(currentEnCustomRoomName, currentIdCustomRoomName), is(true));
        roomConfigurationPage.clickEditCustomRoomName();
        roomConfigurationPage.inputCustomRoomName(enCustomRoomName, idCustomRoomName);
        roomConfigurationPage.clickSubmitUpdateCustomRoomName();
        Assertion.assertThat("Successful snackbar is appeared", roomConfigurationPage.isSuccessfulSnackbarAppeared(), is(true));
        Assertion.assertThat("Custom room name is updated successfully", roomConfigurationPage.isUpdateCustomRoomNameLabelDisplayedCorrectly(enCustomRoomName, idCustomRoomName), is(true));
    }

    @Author(names = {"Raihan"})
    @Module(name = "RoomConfiguration")
    @Tribe(name = "Supply")
    @Vertical(name = "Accommodation")
    @Test(dependsOnMethods = {"testUpdateCustomRoomName"})
    public void testRemoveCustomRoomName() {
        Map<String, String> param = getParam("testRemoveCustomRoomName");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPage<?> loginPage = new LoginPage<>(session);
        HotelLibraryPage<?> hotelLibraryPage = new HotelLibraryPage<>(session);
        SidebarPage<?> sidebarPage = new SidebarPage<>(session);
        RoomConfigurationPage<?> roomConfigurationPage = new RoomConfigurationPage<>(session);

        String existingEnCustomRoomName = param.get("existingEnCustomRoomName");
        String existingIdCustomRoomName = param.get("existingIdCustomRoomName");
        int index = Integer.parseInt(param.get("index"));

        String username = param.get("username");
        String password = param.get("password");

        step("Open MM Module");
        loginPage.openMMModule(username, password);
        Assertion.assertThat("Hotel Library page is loaded", hotelLibraryPage.isHotelLibraryPageLoaded(), is(true));
        Assertion.assertThat("Search result is displayed", hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(), is(true));

        step("Go to room configuration page");
        sidebarPage.clickSidebarMenu(SidebarPage.SidebarMenu.ROOM_MASTER_DATA);
        sidebarPage.clickSidebarSubmenu(SidebarPage.SidebarMenu.ROOM_CONFIGURATION);
        Assertion.assertThat("Room configuration page is displayed", roomConfigurationPage.isRoomConfigurationPageDisplayed() && roomConfigurationPage.isCustomRoomNameListDisplayed(), is(true));

        step("Search custom room name");
        roomConfigurationPage.inputCustomRoomNameFilter(existingEnCustomRoomName);
        roomConfigurationPage.clickSearchCustomRoomName();
        Assertion.assertThat(String.format("Custom room name is found. EN: %s, ID: %s", existingEnCustomRoomName, existingIdCustomRoomName), roomConfigurationPage.isCustomRoomNameSearchResultAppeared(index, existingEnCustomRoomName, existingIdCustomRoomName), is(true));

        step("Remove custom room name");
        roomConfigurationPage.clickSettingCustomRoomName(index);
        roomConfigurationPage.clickDeleteCustomRoomName(index);
        Assertion.assertThat("Remove custom room name modal is appeared", roomConfigurationPage.isDeleteCustomRoomNameConfirmModalDisplayed(), is(true));
        roomConfigurationPage.clickSubmitDeleteCustomRoomName();
        Assertion.assertThat("Successful snackbar is appeared", roomConfigurationPage.isSuccessfulSnackbarAppeared(), is(true));
        roomConfigurationPage.inputCustomRoomNameFilter(existingEnCustomRoomName);
        roomConfigurationPage.clickSearchCustomRoomName();
        Assertion.assertThat("Custom room name not found", roomConfigurationPage.isCustomRoomNameSearchResultNotFound(), is(true));
    }
    @Author(names = {"Venkat"})
    @Module(name = "RoomConfiguration")
    @Tribe(name = "Supply")
    @Vertical(name = "Accommodation")
    @Test
    public void testRoomConfiguration() {
        Map<String, String> param = getParam("testRoomConfiguration");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<>(session);
        RoomConfigurationPage<?> roomConfigurationPage = new RoomConfigurationPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String url = param.get("url");

        step("Login via cookie injection");
        loginMemberAdmin.loginViaCookie(username, password);

        step("Open Room Configuration");
        loginMemberAdmin.goToDashboard(url);

        Assertion.assertThat("Room configuration page is opened ", roomConfigurationPage.isRoomConfigurationPageOpened(), is(true));

        String resEng= CoreFunctions.generateRandomString(5).toUpperCase();
        String resID=CoreFunctions.generateRandomString(5).toUpperCase();

        step("Create a new configuration");
        roomConfigurationPage.createNewConfiguration(resEng,resID);
        Assertion.assertThat("Verify Success creation Snackbar is displayed",roomConfigurationPage.getTextFromSnackbar(),containsString("Success to create Room restriction."));

        step("Search the configuration");
        roomConfigurationPage.searchRestriction(resEng);

        step("Verify the filtered results");
        Assertion.assertThat("Verify column headers are correct",roomConfigurationPage.verifyColumnHeaders(),is(true));

        String date= LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));

        Assertion.assertThat("Verify En name is correct",roomConfigurationPage.getRestrictionEnName(),is(resEng));
        Assertion.assertThat("Verify Id name is correct",roomConfigurationPage.getRestrictionIdName(),is(resID));
        Assertion.assertThat("Verify Type name is correct",roomConfigurationPage.getRestrictionType(),is("Nationality"));
        Assertion.assertThat("Verify lastUpdated date is correct",roomConfigurationPage.getLastUpdated(),containsString(date));
        Assertion.assertThat("Verify Updated by name is Correct",roomConfigurationPage.getUpdatedBy(),is(username));

        step("Edit the Restriction");
        roomConfigurationPage.editRoomConfiguration(resEng,resID);
        Assertion.assertThat("Verify Edited  Snackbar is displayed",roomConfigurationPage.getTextFromSnackbar(),containsString("You've successfully updated Room Restriction."));

        step("Search the edit configuration");
        roomConfigurationPage.searchRestriction("Edited"+resEng);

        step("Verify the filtered results");

        Assertion.assertThat("Verify En name is correct",roomConfigurationPage.getRestrictionEnName(),is("Edited"+resEng));
        Assertion.assertThat("Verify Id name is correct",roomConfigurationPage.getRestrictionIdName(),is("Edited"+resID));
        Assertion.assertThat("Verify Type name is correct",roomConfigurationPage.getRestrictionType(),is("Nationality"));
        Assertion.assertThat("Verify lastUpdated date is correct",roomConfigurationPage.getLastUpdated(), containsString(date));
        Assertion.assertThat("Verify Updated by name is correct",roomConfigurationPage.getUpdatedBy(),is(username));


        step("Delete the Configuration");
        roomConfigurationPage.deleteRoomRestriction();
        Assertion.assertThat("Verify Deleted  Snackbar is displayed",roomConfigurationPage.getTextFromSnackbar(),containsString("You've successfully deleted Edited"+resEng));

        step("Search the Deleted configuration");
        roomConfigurationPage.searchRestriction("Edited"+resEng);

        step("Verify no data should be shown once deleted");
        Assertion.assertThat("Verify no data should be shown once deleted",roomConfigurationPage.verifyNoData(),is(true));

    }
}
