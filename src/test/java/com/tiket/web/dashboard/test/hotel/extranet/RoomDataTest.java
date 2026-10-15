package com.tiket.web.dashboard.test.hotel.extranet;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.web.dashboard.page.hotel.extranet.Navbar;
import com.tiket.web.dashboard.page.hotel.extranet.RoomDataPage;
import com.tiket.web.dashboard.page.hotel.extranet.SideMenu;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Map;

import static com.tiket.testdata.TestData.getObjectParams;
import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.Matchers.is;

public class RoomDataTest extends BaseTest {
    @Author(names = {"Fadhlan", "Vipul Kumar"})
    @Module(name = "RoomData")
    @Tribe(name = "Extranet")
    @Vertical(name = "Accommodation")
    @Test
    public void testVerifyUserAbleToAddNewRoomOnRoomDataMenu() {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        Navbar<?> navbar = new Navbar<>(session);
        RoomDataPage<?> roomDataPage = new RoomDataPage<>(session);
        SideMenu<?> sideMenu = new SideMenu<>(session);

        AuthTest authTest = new AuthTest();
        
        Map<String, String> param = getParam("testVerifyUserAbleToAddNewRoomOnRoomDataMenu");
        BaseTest.paramsThreadLocal.set(param);
        String email = param.get("email");
        String password = param.get("password");
        String hotelName = param.get("hotelName");
        String roomName = param.get("roomName") + " " + System.currentTimeMillis();
        String desc = param.get("desc");
        String avgRoomSize = param.get("avgRoomSize");
        String roomType = param.get("roomType").replace('$', ' ');
        String sellPrice = param.get("sellPrice");

        step("Login to Extranet");
        authTest.ensureUserIsLoggedInWithCorrectCredentials(email, password, session);

        step("Change hotel");
        navbar.changeHotel(hotelName);

        step("Go to room data page");
        sideMenu.clickPropertyMenu();
        sideMenu.clickRoomDataMenu();
        Assertion.assertThat("Room data page is loaded", roomDataPage.isRoomDataPageLoaded(), is(true));

        step("Click add new room button");
        roomDataPage.clickAddNewRoomButton();
        Assertion.assertThat("Create room data page is loaded", roomDataPage.isCreateRoomDataPageLoaded(), is(true));
        
        step("Fill in the form");
        roomDataPage.fillRoomDetailForm(roomName, desc, avgRoomSize, roomType, sellPrice);
        roomDataPage.fillBedConfigurationForm();
        roomDataPage.fillRoomPhotosForm();
        roomDataPage.clickAddRoom();
        Assertion.assertThat("Success toast is displayed", roomDataPage.isSuccessToastAppeared(), is(true));
        Assertion.assertThat("The new room is created", roomDataPage.isRoomAppeared(roomName), is(true));

        step("Revert test data");
        roomDataPage.deleteRoom(roomName);
        Assertion.assertThat("Success toast is displayed", roomDataPage.isSuccessToastAppeared(), is(true));
        roomDataPage.refreshPage();
        Assertion.assertThat("The room is deleted", roomDataPage.isRoomAppeared(roomName), is(false));
    }

    @Author(names = {"Fadhlan", "Vipul Kumar"})
    @Module(name = "RoomData")
    @Tribe(name = "Extranet")
    @Vertical(name = "Accommodation")
    @Test
    public void testVerifyUserAbleToEditRoomOnRoomDataMenu() {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        Navbar<?> navbar = new Navbar<>(session);
        RoomDataPage<?> roomDataPage = new RoomDataPage<>(session);
        SideMenu<?> sideMenu = new SideMenu<>(session);

        AuthTest authTest = new AuthTest();
        
        Map<String, String> param = getParam("testVerifyUserAbleToEditRoomOnRoomDataMenu");
        BaseTest.paramsThreadLocal.set(param);
        String email = param.get("email");
        String password = param.get("password");
        String hotelName = param.get("hotelName");
        String oldRoomName = param.get("oldRoomName").replace('$', ' ');
        String newRoomName = param.get("newRoomName").replace('$', ' ');

        step("Login to Extranet");
        authTest.ensureUserIsLoggedInWithCorrectCredentials(email, password, session);

        step("Change hotel");
        navbar.changeHotel(hotelName);

        step("Go to room data page");
        sideMenu.clickPropertyMenu();
        sideMenu.clickRoomDataMenu();
        Assertion.assertThat("Room data page is loaded", roomDataPage.isRoomDataPageLoaded(), is(true));

        step("Click edit icon");
        roomDataPage.clickEditButtonByRoomName(oldRoomName);
        Assertion.assertThat("Edit room data page is loaded", roomDataPage.isCreateRoomDataPageLoaded(), is(true));

        step("Edit room name and submit");
        roomDataPage.fillOriginalRoomNameInput(newRoomName);
        roomDataPage.clickAddRoom();
        Assertion.assertThat("Success toast is displayed", roomDataPage.isSuccessToastAppeared(), is(true));
        roomDataPage.searchRoomName(newRoomName);
        roomDataPage.clickSearch();
        Assertion.assertThat("The room is edited", roomDataPage.isRoomAppeared(newRoomName), is(true));

        step("Revert test data");
        roomDataPage.clickEditButtonByRoomName(newRoomName);
        Assertion.assertThat("Edit room data page is loaded", roomDataPage.isCreateRoomDataPageLoaded(), is(true));
        roomDataPage.fillOriginalRoomNameInput(oldRoomName);
        roomDataPage.clickAddRoom();
        roomDataPage.searchRoomName(oldRoomName);
        roomDataPage.clickSearch();
        Assertion.assertThat("The room is edited", roomDataPage.isRoomAppeared(oldRoomName), is(true));
    }
    
    @DataProvider(name = "testVerifyUserAbleToToggleRoomStatusOnRoomDataMenu")
    public Object[][] testVerifyUserAbleToToggleRoomStatusOnRoomDataMenuDataProvider() {
        return getObjectParams("testVerifyUserAbleToToggleRoomStatusOnRoomDataMenu");
    }

    @Author(names = {"Fadhlan", "Vipul Kumar"})
    @Module(name = "RoomData")
    @Tribe(name = "Extranet")
    @Vertical(name = "Accommodation")
    @Test(dataProvider = "testVerifyUserAbleToToggleRoomStatusOnRoomDataMenu", dependsOnMethods = {"testVerifyUserAbleToEditRoomOnRoomDataMenu"})
    public void testVerifyUserAbleToToggleRoomStatusOnRoomDataMenu(Map<String, String> map) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(map);
        Navbar<?> navbar = new Navbar<>(session);
        RoomDataPage<?> roomDataPage = new RoomDataPage<>(session);
        SideMenu<?> sideMenu = new SideMenu<>(session);

        AuthTest authTest = new AuthTest();
        
        String email = map.get("email");
        String password = map.get("password");
        String hotelName = map.get("hotelName");
        String roomName = map.get("roomName");
        boolean isToggleOn = Boolean.parseBoolean(map.get("isToggleOn"));

        step("Login to Extranet");
        authTest.ensureUserIsLoggedInWithCorrectCredentials(email, password, session);

        step("Change hotel");
        navbar.changeHotel(hotelName);

        step("Go to room data page");
        sideMenu.clickPropertyMenu();
        sideMenu.clickRoomDataMenu();
        Assertion.assertThat("Room data page is loaded", roomDataPage.isRoomDataPageLoaded(), is(true));

        roomDataPage.searchRoomName(roomName);
        roomDataPage.clickSearch();
        Assertion.assertThat("The room is appeared", roomDataPage.isRoomAppeared(roomName), is(true));

        step("Click status toggle button");
        roomDataPage.switchRoomStatusToggle(roomName, isToggleOn);
        roomDataPage.confirmActivateRoom();
        Assertion.assertThat(String.format("The room status is changed to %b", isToggleOn), roomDataPage.isRoomActive(roomName), is(isToggleOn));
    }
}
