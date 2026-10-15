package com.tiket.web.dashboard.test.hotel.supply.mmmodule;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.web.dashboard.page.hotel.supply.mmmodule.BulkActivateDeactivatePage;
import com.tiket.web.dashboard.page.hotel.supply.mmmodule.BulkResyncPage;
import com.tiket.web.dashboard.page.hotel.supply.mmmodule.HotelLibraryPage;
import com.tiket.web.dashboard.page.hotel.supply.mmmodule.LoginPage;
import com.tiket.web.dashboard.page.hotel.supply.mmmodule.SidebarPage;
import com.tiket.web.dashboard.page.hotel.supply.mmmodule.SyncPage;
import org.testng.annotations.Test;

import java.io.File;
import java.util.Map;

import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.core.Is.is;

public class SyncTest extends BaseTest {

    @Author(names = {"Raihan", "Bikash kumar jha"})
    @Module(name = "Sync")
    @Tribe(name = "Supply")
    @Vertical(name = "Accommodation")
    @Test
    public void testSyncLocationPolygon() {
        Map<String, String> param = getParam("testSyncLocationPolygon");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPage<?> loginPage = new LoginPage<>(session);
        HotelLibraryPage<?> hotelLibraryPage = new HotelLibraryPage<>(session);
        SidebarPage<?> sidebarPage = new SidebarPage<>(session);
        SyncPage<?> syncPage = new SyncPage<>(session);

        String searchType = param.get("searchType");
        String country = param.get("country");
        String region = param.get("region");

        String username = param.get("username");
        String password = param.get("password");

        step("Open MM Module");
        loginPage.openMMModule(username, password);
        Assertion.assertThat("Hotel Library page is loaded", hotelLibraryPage.isHotelLibraryPageLoaded(), is(true));
        Assertion.assertThat("Search result is displayed", hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(), is(true));

        step("Go to sync location polygon page");
        sidebarPage.clickSidebarMenu(SidebarPage.SidebarMenu.SYNC);
        sidebarPage.clickSidebarSubmenu(SidebarPage.SidebarMenu.LOCATION_POLYGON);
        Assertion.assertThat("Sync location polygon page is displayed", syncPage.isSyncLocationPolygonPageDisplayed(), is(true));

        step("Filter location and trigger sync");
        syncPage.selectLocationPolygonFilterSearchType(searchType);
        syncPage.selectLocationPolygonFilterCountry(country);
        syncPage.selectLocationPolygonFilterRegion(region);
        syncPage.clickLocationPolygonSyncButton();
        Assertion.assertThat("Successful snackbar is displayed", syncPage.isSuccessfulSnackbarDisplayed(), is(true));
    }

    @Author(names = {"Raihan"})
    @Module(name = "Sync")
    @Tribe(name = "Supply")
    @Vertical(name = "Accommodation")
    @Test
    public void testSyncHotelNearby() {
        Map<String, String> param = getParam("testSyncHotelNearby");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPage<?> loginPage = new LoginPage<>(session);
        HotelLibraryPage<?> hotelLibraryPage = new HotelLibraryPage<>(session);
        SidebarPage<?> sidebarPage = new SidebarPage<>(session);
        SyncPage<?> syncPage = new SyncPage<>(session);

        String searchType = param.get("searchType");
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

        step("Go to sync location polygon page");
        sidebarPage.clickSidebarMenu(SidebarPage.SidebarMenu.SYNC);
        sidebarPage.clickSidebarSubmenu(SidebarPage.SidebarMenu.NEARBY);
        Assertion.assertThat("Sync nearby page is displayed", syncPage.isSyncNearbyPageDisplayed(), is(true));

        step("Filter location and trigger sync");
        syncPage.selectNearbyFilterSearchType(searchType);
        syncPage.selectNearbyFilterCountry(country);
        syncPage.selectNearbyFilterRegion(region);
        syncPage.selectNearbyFilterCity(city);
        syncPage.selectNearbyFilterArea(area);
        syncPage.clickNearbySyncButton();
        Assertion.assertThat("Successful toast is displayed", syncPage.isSuccessfulToastDisplayed(), is(true));
    }

    @Author(names = {"Raihan"})
    @Module(name = "Sync")
    @Tribe(name = "Supply")
    @Vertical(name = "Accommodation")
    @Test
    public void testSyncWeightUpdate() {
        Map<String, String> param = getParam("testSyncWeightUpdate");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPage<?> loginPage = new LoginPage<>(session);
        HotelLibraryPage<?> hotelLibraryPage = new HotelLibraryPage<>(session);
        SidebarPage<?> sidebarPage = new SidebarPage<>(session);
        SyncPage<?> syncPage = new SyncPage<>(session);

        String weightType = param.get("weightType");
        String location = param.get("location");
        String weightValue = param.get("weightValue");

        String username = param.get("username");
        String password = param.get("password");

        step("Open MM Module");
        loginPage.openMMModule(username, password);
        Assertion.assertThat("Hotel Library page is loaded", hotelLibraryPage.isHotelLibraryPageLoaded(), is(true));
        Assertion.assertThat("Search result is displayed", hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(), is(true));

        step("Go to sync weight update page");
        sidebarPage.clickSidebarMenu(SidebarPage.SidebarMenu.SYNC);
        sidebarPage.clickSidebarSubmenu(SidebarPage.SidebarMenu.WEIGHT_UPDATE);
        Assertion.assertThat("Sync weight update page is displayed", syncPage.isSyncWeightUpdatePageDisplayed(), is(true));

        step("Filter weight type and trigger sync");
        syncPage.selectWeightUpdateType(weightType);
        syncPage.selectWeightUpdateLocation(location);
        syncPage.selectWeightUpdateValue(weightValue);
        syncPage.clickWeightUpdateSyncButton();
        Assertion.assertThat("Successful snackbar is displayed", syncPage.isSuccessfulSnackbarDisplayed(), is(true));
    }

    @Author(names = {"Raihan", "Bikash kumar jha"})
    @Module(name = "Sync")
    @Tribe(name = "Supply")
    @Vertical(name = "Accommodation")
    @Test
    public void testSyncHotelContent() {
        Map<String, String> param = getParam("testSyncHotelContent");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPage<?> loginPage = new LoginPage<>(session);
        HotelLibraryPage<?> hotelLibraryPage = new HotelLibraryPage<>(session);
        SidebarPage<?> sidebarPage = new SidebarPage<>(session);
        SyncPage<?> syncPage = new SyncPage<>(session);

        String vendorName = param.get("vendorName");
        String vendorHotelId = param.get("vendorHotelId");

        String username = param.get("username");
        String password = param.get("password");

        step("Open MM Module");
        loginPage.openMMModule(username, password);
        Assertion.assertThat("Hotel Library page is loaded", hotelLibraryPage.isHotelLibraryPageLoaded(), is(true));
        Assertion.assertThat("Search result is displayed", hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(), is(true));

        step("Go to sync weight update page");
        sidebarPage.clickSidebarMenu(SidebarPage.SidebarMenu.SYNC);
        sidebarPage.clickSidebarSubmenu(SidebarPage.SidebarMenu.HOTEL_CONTENT);

        step("Enter vendor and hotel id, then trigger sync");
        syncPage.selectHotelContentVendorName(vendorName);
        syncPage.enterHotelContentVendorHotelId(vendorHotelId);
        syncPage.clickHotelContentSyncButton();
        Assertion.assertThat("Successful snackbar is displayed", syncPage.isSuccessfulSnackbarDisplayed(), is(true));
        Assertion.assertThat("Sync hotel content page is displayed", syncPage.isSyncHotelContentPageDisplayed(), is(true));
    }

    @Author(names = {"Raihan","Bikash kumar jha"})
    @Module(name = "Sync")
    @Tribe(name = "Supply")
    @Vertical(name = "Accommodation")
    @Test
    public void testSyncMainPhoto() {
        Map<String, String> param = getParam("testSyncMainPhoto");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPage<?> loginPage = new LoginPage<>(session);
        HotelLibraryPage<?> hotelLibraryPage = new HotelLibraryPage<>(session);
        SidebarPage<?> sidebarPage = new SidebarPage<>(session);
        SyncPage<?> syncPage = new SyncPage<>(session);

        String locationType = param.get("locationType");
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

        step("Go to sync location polygon page");
        sidebarPage.clickSidebarMenu(SidebarPage.SidebarMenu.SYNC);
        sidebarPage.clickSidebarSubmenu(SidebarPage.SidebarMenu.MAIN_PHOTO);
        Assertion.assertThat("Sync main photo page is displayed", syncPage.isSyncMainPhotoPageDisplayed(), is(true));

        step("Filter location and trigger sync");
        syncPage.selectMainPhotoFilterLocationType(locationType);
        syncPage.selectMainPhotoFilterCountry(country);
        syncPage.selectMainPhotoFilterRegion(region);
        syncPage.selectMainPhotoFilterCity(city);
        syncPage.selectMainPhotoFilterArea(area);
        syncPage.clickMainPhotoSyncButton();
        Assertion.assertThat("Successful snackbar is displayed", syncPage.isSuccessfulSnackbarDisplayed(), is(true));
    }

    @Author(names = {"Raihan"})
    @Module(name = "Sync")
    @Tribe(name = "Supply")
    @Vertical(name = "Accommodation")
    @Test
    public void testSyncAurora() {
        Map<String, String> param = getParam("testSyncAurora");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPage<?> loginPage = new LoginPage<>(session);
        HotelLibraryPage<?> hotelLibraryPage = new HotelLibraryPage<>(session);
        SidebarPage<?> sidebarPage = new SidebarPage<>(session);
        SyncPage<?> syncPage = new SyncPage<>(session);

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

        step("Go to sync Aurora page");
        sidebarPage.clickSidebarMenu(SidebarPage.SidebarMenu.SYNC);
        sidebarPage.clickSidebarSubmenu(SidebarPage.SidebarMenu.AURORA);
        Assertion.assertThat("Sync Aurora page is displayed", syncPage.isSyncAuroraPageDisplayed(), is(true));

        step("Filter location and trigger sync");
        syncPage.selectAuroraFilterCountry(country);
        syncPage.selectAuroraFilterRegion(region);
        syncPage.selectAuroraFilterCity(city);
        syncPage.selectAuroraFilterArea(area);
        syncPage.clickAuroraSyncButton();
        Assertion.assertThat("Successful snackbar is displayed", syncPage.isSuccessfulSnackbarDisplayed(), is(true));
    }

    @Author(names = {"Raihan", "Bikash kumar jha"})
    @Module(name = "Sync")
    @Tribe(name = "Supply")
    @Vertical(name = "Accommodation")
    @Test
    public void testSyncNoOfHotelPerLocation() {
        Map<String, String> param = getParam("testSyncNoOfHotelPerLocation");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPage<?> loginPage = new LoginPage<>(session);
        HotelLibraryPage<?> hotelLibraryPage = new HotelLibraryPage<>(session);
        SidebarPage<?> sidebarPage = new SidebarPage<>(session);
        SyncPage<?> syncPage = new SyncPage<>(session);

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

        step("Go to sync no of hotels per location page");
        sidebarPage.clickSidebarMenu(SidebarPage.SidebarMenu.SYNC);
        sidebarPage.clickSidebarSubmenu(SidebarPage.SidebarMenu.NO_OF_HOTELS_PER_LOCATION);

        step("Filter location and trigger sync");
        syncPage.selectNoOfHotelPerLocationFilterCountry(country);
        syncPage.selectNoOfHotelPerLocationFilterRegion(region);
        syncPage.selectNoOfHotelPerLocationFilterCity(city);
        syncPage.selectNoOfHotelPerLocationFilterArea(area);
        syncPage.clickNoOfHotelPerLocationSyncButton();
        Assertion.assertThat("Successful snackbar is displayed", syncPage.isSuccessfulSnackbarDisplayed(), is(true));
        Assertion.assertThat("Sync no of hotels per location page is displayed", syncPage.isSyncNoOfHotelPerLocationPageDisplayed(), is(true));
    }

    @Author(names = {"Manan Kumar","Riya Bajpai"})
    @Module(name = "Sync")
    @Tribe(name = "Supply")
    @Vertical(name = "Accommodation")
    @Test
    public void testBulkResyncVerifyFormTypesAndTaskTabs() {
        Map<String, String> param = getParam("testBulkResyncVerifyFormTypesAndTaskTabs");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPage<?> loginPage = new LoginPage<>(session);
        HotelLibraryPage<?> hotelLibraryPage = new HotelLibraryPage<>(session);
        SidebarPage<?> sidebarPage = new SidebarPage<>(session);
        BulkResyncPage<?> bulkResyncPage = new BulkResyncPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String hotelId  = param.get("hotelId");
        String taskName = "BulkResync_" + CoreFunctions.randomName();

        step("Login as MM user and open MM Module");
        loginPage.openMMModule(username, password);
        Assertion.assertThat("Hotel Library page is loaded", hotelLibraryPage.isHotelLibraryPageLoaded(), is(true));

        step("Navigate to Sync");
        sidebarPage.clickSidebarMenu(SidebarPage.SidebarMenu.SYNC);

        step("Click Bulk Resync submenu");
        sidebarPage.clickSyncSubmenu("Bulk Resync");
        Assertion.assertThat("Bulk Resync page is displayed", bulkResyncPage.isBulkResyncPageDisplayed(), is(true));

        step("Click Add Task Non-Vendor API: Room");
        bulkResyncPage.clickAddTask();

        step("Fill task name, select type Non-Vendor API > Room and upload CSV");
        bulkResyncPage.selectType("Non-vendor API");
        bulkResyncPage.fillTaskName(taskName);
        bulkResyncPage.selectSubType("Room");
        bulkResyncPage.createAndUploadCsvFile(hotelId);

        step("Submit form and verify task is submitted");
        bulkResyncPage.clickSubmit();
        Assertion.assertThat("Verify user is able to submit bulk resync form with type Non-Vendor API: Room", bulkResyncPage.isSubmitSuccessToastDisplayed(), is(true));

        step("Verify Non-Vendor API: Room task is visible under In Progress tab");
        bulkResyncPage.clickInProgressTab();
        Assertion.assertThat("Task is present in In Progress tab", bulkResyncPage.isTaskVisibleInInProgressTab(), is(true));

        step("Verify Non-Vendor API: Room task is visible under Completed tab");
        bulkResyncPage.clickCompletedTab();
        Assertion.assertThat("Verify sync task is presented under Completed tab once the sync process is done",bulkResyncPage.isTaskVisibleInCompletedTab(), is(true));

        step("Retry sync task from Completed tab and verify it appears under In Progress tab");
        bulkResyncPage.clickInProgressTab();
        Assertion.assertThat("Verify sync task is presented under In Progress tab immediately after user retry sync task from Completed tab",bulkResyncPage.isTaskVisibleInInProgressTab(), is(true));

        step("Click Add Task Non-Vendor API: Property");
        bulkResyncPage.clickAddTask();

        step("Fill task name, select type Non-Vendor API > Property and upload CSV");
        bulkResyncPage.selectType("Non-vendor API");
        bulkResyncPage.fillTaskName(taskName);
        bulkResyncPage.selectSubType("Property");
        bulkResyncPage.createAndUploadCsvFile(hotelId);

        step("Submit form and verify task is submitted");
        bulkResyncPage.clickSubmit();
        Assertion.assertThat("Verify user is able to submit bulk resync form with type Non-Vendor API: Property",bulkResyncPage.isSubmitSuccessToastDisplayed(), is(true));

        step("Verify Non-Vendor API: Property task is visible under In Progress tab");
        bulkResyncPage.clickInProgressTab();
        Assertion.assertThat("Task is present in In Progress tab",bulkResyncPage.isTaskVisibleInInProgressTab(), is(true));

        step("Verify Non-Vendor API: Property task is visible under Completed tab");
        bulkResyncPage.clickCompletedTab();
        Assertion.assertThat("Verify sync task is presented under Completed tab once the sync process is done",bulkResyncPage.isTaskVisibleInCompletedTab(), is(true));

        step("Click Add Task Non-Vendor API: Property and Room");
        bulkResyncPage.clickAddTask();

        step("Fill task name, select type Non-Vendor API > Property and Room and upload CSV");
        bulkResyncPage.selectType("Non-vendor API");
        bulkResyncPage.fillTaskName(taskName);
        bulkResyncPage.selectSubType("Property and Room");
        bulkResyncPage.createAndUploadCsvFile(hotelId);

        step("Submit form and verify task is submitted");
        bulkResyncPage.clickSubmit();
        Assertion.assertThat("Verify user is able to submit bulk resync form with type Non-Vendor API: Property & Room",bulkResyncPage.isSubmitSuccessToastDisplayed(), is(true));

        step("Verify Non-Vendor API: Property and Room task is visible under In Progress tab");
        bulkResyncPage.clickInProgressTab();
        Assertion.assertThat("Task is present in In Progress tab", bulkResyncPage.isTaskVisibleInInProgressTab(), is(true));

        step("Verify Non-Vendor API: Property and Room task is visible under Completed tab");
        bulkResyncPage.clickCompletedTab();
        Assertion.assertThat("Verify sync task is presented under Completed tab once the sync process is done", bulkResyncPage.isTaskVisibleInCompletedTab(), is(true));

        step("Click Add Task Non-Vendor API: New Lacerta Model");
        bulkResyncPage.clickAddTask();

        step("Fill task name, select type Non-Vendor API > New Lacerta Model and upload CSV");
        bulkResyncPage.selectType("Non-vendor API");
        bulkResyncPage.fillTaskName(taskName);
        bulkResyncPage.selectSubType("New Lacerta Model");
        bulkResyncPage.createAndUploadCsvFile(hotelId);

        step("Submit form and verify task is submitted");
        bulkResyncPage.clickSubmit();
        Assertion.assertThat("Verify user is able to submit bulk resync form with type Non-Vendor API: New Lacerta Model",bulkResyncPage.isSubmitSuccessToastDisplayed(), is(true));

        step("Verify Non-Vendor API: New Lacerta Model task is visible under In Progress tab");
        bulkResyncPage.clickInProgressTab();
        Assertion.assertThat("Task is present in In Progress tab",bulkResyncPage.isTaskVisibleInInProgressTab(), is(true));

        step("Verify Non-Vendor API: New Lacerta Model task is visible under Completed tab");
        bulkResyncPage.clickCompletedTab();
        Assertion.assertThat("Verify sync task is presented under Completed tab once the sync process is done",bulkResyncPage.isTaskVisibleInCompletedTab(), is(true));

        step("Click Add Task Non-Vendor API: Cetus Room Grouping");
        bulkResyncPage.clickAddTask();

        step("Fill task name, select type Non-Vendor API > Cetus Room Grouping and upload CSV");
        bulkResyncPage.selectType("Non-vendor API");
        bulkResyncPage.fillTaskName(taskName);
        bulkResyncPage.selectSubType("Cetus Room Grouping");
        bulkResyncPage.createAndUploadCsvFile(hotelId);

        step("Submit form and verify task is submitted");
        bulkResyncPage.clickSubmit();
        Assertion.assertThat("Verify user is able to submit bulk resync form with type Non-Vendor API: Cetus Room Grouping",bulkResyncPage.isSubmitSuccessToastDisplayed(), is(true));

        step("Verify Non-Vendor API: Cetus Room Grouping task is visible under In Progress tab");
        bulkResyncPage.clickInProgressTab();
        Assertion.assertThat("Task is present in In Progress tab", bulkResyncPage.isTaskVisibleInInProgressTab(), is(true));

        step("Verify Non-Vendor API: Cetus Room Grouping task is visible under Completed tab");
        bulkResyncPage.clickCompletedTab();
        Assertion.assertThat("Verify sync task is presented under Completed tab once the sync process is done", bulkResyncPage.isTaskVisibleInCompletedTab(), is(true));

        step("Click Add Task Non-Vendor API: Master Room Group Re-evaluation");
        bulkResyncPage.clickAddTask();

        step("Fill task name, select type Non-Vendor API > Master Room Group Re-evaluation and upload CSV");
        bulkResyncPage.selectType("Non-vendor API");
        bulkResyncPage.fillTaskName(taskName);
        bulkResyncPage.selectSubType("Master Room Group Re-evaluation");
        bulkResyncPage.createAndUploadCsvFile(hotelId);

        step("Submit form and verify task is submitted");
        bulkResyncPage.clickSubmit();
        Assertion.assertThat("Verify user is able to submit bulk resync form with type Non-Vendor API: Master Room Group Re-evaluation",bulkResyncPage.isSubmitSuccessToastDisplayed(), is(true));

        step("Verify Non-Vendor API: Master Room Group Re-evaluation task is visible under In Progress tab");
        bulkResyncPage.clickInProgressTab();
        Assertion.assertThat("Task is present in In Progress tab",bulkResyncPage.isTaskVisibleInInProgressTab(), is(true));

        step("Verify Non-Vendor API: Master Room Group Re-evaluation task is visible under Completed tab");
        bulkResyncPage.clickCompletedTab();
        Assertion.assertThat("Verify sync task is presented under Completed tab once the sync process is done",bulkResyncPage.isTaskVisibleInCompletedTab(), is(true));

        step("Click Add Task Vendor API");
        bulkResyncPage.clickAddTask();

        step("Fill task name, select type Vendor API and upload CSV");
        bulkResyncPage.selectType("Vendor API");
        bulkResyncPage.fillVendorTaskName(taskName);
        bulkResyncPage.createAndUploadCsvFile(hotelId);

        step("Submit form and verify task is submitted");
        bulkResyncPage.clickSubmit();
        Assertion.assertThat("Verify user is able to submit bulk resync form with type Vendor API", bulkResyncPage.isSubmitSuccessToastDisplayed(), is(true));

        step("Verify Vendor API task is visible under In Progress tab");
        bulkResyncPage.clickInProgressTab();
        Assertion.assertThat("Task is present in In Progress tab", bulkResyncPage.isTaskVisibleInInProgressTab(), is(true));

        step("Verify Vendor API task is visible under Completed tab");
        bulkResyncPage.clickCompletedTab();
        Assertion.assertThat("Verify sync task is presented under Completed tab once the sync process is done", bulkResyncPage.isTaskVisibleInCompletedTab(), is(true));
    }

    @Author(names = {"Manan Kumar"})
    @Module(name = "Sync")
    @Tribe(name = "Supply")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyBulkActivateDeactivateAtHotelAndVendorLevel() {
        Map<String, String> param = getParam("verifyBulkActivateDeactivateAtHotelAndVendorLevel");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPage<?> loginPage = new LoginPage<>(session);
        HotelLibraryPage<?> hotelLibraryPage = new HotelLibraryPage<>(session);
        SidebarPage<?> sidebarPage = new SidebarPage<>(session);
        BulkActivateDeactivatePage<?> bulkActivateDeactivatePage = new BulkActivateDeactivatePage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String hotelLevel = param.get("hotelLevel");
        String vendorLevel = param.get("vendorLevel");
        String hotelLevelFilePath = param.get("hotelLevelFilePath");
        String vendorLevelFilePath = param.get("vendorLevelFilePath");
        String HotelDescription;

        step("Open MM Module");
        loginPage.openMMModule(username, password);
        Assertion.assertThat("Hotel Library page is loaded", hotelLibraryPage.isHotelLibraryPageLoaded(), is(true));

        step("Navigate to Sync");
        sidebarPage.clickSidebarMenu(SidebarPage.SidebarMenu.SYNC);

        step("Click Bulk Activate/Deactivate submenu");
        sidebarPage.clickSyncSubmenu("Bulk Active / Deactivate");
        Assertion.assertThat("Bulk Activate/Deactivate page is displayed", bulkActivateDeactivatePage.isBulkActivateDeactivatePageDisplayed(), is(true));

        HotelDescription = CoreFunctions.randomNameWithoutTest() + " " + CoreFunctions.randomNameWithoutTest();
        File hotelLevelCsvFile = new File(hotelLevelFilePath);
        File vendorLevelCsvFile = new File(vendorLevelFilePath);
        String hotelLevelCsvFileName = hotelLevelCsvFile.getName();
        String vendorLevelCsvFileName = vendorLevelCsvFile.getName();

        step("Click Add Task for Deactivate Hotel Level");
        bulkActivateDeactivatePage.clickAddTask();

        step("Select Deactivate chip");
        bulkActivateDeactivatePage.selectActionType("Deactivate");

        step("Select Hotel Level from dropdown");
        bulkActivateDeactivatePage.selectLevel(hotelLevel);

        step("Fill description");
        bulkActivateDeactivatePage.fillDescription(HotelDescription);

        step("Upload csv file");
        bulkActivateDeactivatePage.uploadCsvFile(hotelLevelCsvFile);
        Assertion.assertThat("Uploaded csv file is displayed", bulkActivateDeactivatePage.isCsvFileUploaded(hotelLevelCsvFileName), is(true));

        step("Click Submit");
        bulkActivateDeactivatePage.clickSubmit();
        Assertion.assertThat("[Sync][Bulk Activate/Deactivate] Verify user is able to submit bulk deactivate task for hotel level successfully", bulkActivateDeactivatePage.isActivityRowDisplayed(HotelDescription, hotelLevel, "Deactivate"), is(true));

        step("Click Add Task for Deactivate Vendor Level");
        bulkActivateDeactivatePage.clickAddTask();

        step("Select Deactivate chip");
        bulkActivateDeactivatePage.selectActionType("Deactivate");

        step("Select Vendor Level from dropdown");
        bulkActivateDeactivatePage.selectLevel(vendorLevel);

        step("Fill description");
        bulkActivateDeactivatePage.fillDescription(HotelDescription);

        step("Upload csv file");
        bulkActivateDeactivatePage.uploadCsvFile(vendorLevelCsvFile);
        Assertion.assertThat("Uploaded csv file is displayed", bulkActivateDeactivatePage.isCsvFileUploaded(vendorLevelCsvFileName), is(true));

        step("Click Submit");
        bulkActivateDeactivatePage.clickSubmit();
        Assertion.assertThat("[Sync][Bulk Activate/Deactivate] Verify user is able to submit bulk deactivate task for vendor level successfully", bulkActivateDeactivatePage.isActivityRowDisplayed(HotelDescription, vendorLevel, "Deactivate"), is(true));

        step("Click Add Task for Activate Hotel Level");
        bulkActivateDeactivatePage.clickAddTask();

        step("Select Activate chip");
        bulkActivateDeactivatePage.selectActionType("Activate");

        step("Select Hotel Level from dropdown");
        bulkActivateDeactivatePage.selectLevel(hotelLevel);

        step("Fill description");
        bulkActivateDeactivatePage.fillDescription(HotelDescription);

        step("Upload csv file");
        bulkActivateDeactivatePage.uploadCsvFile(hotelLevelCsvFile);
        Assertion.assertThat("Uploaded csv file is displayed", bulkActivateDeactivatePage.isCsvFileUploaded(hotelLevelCsvFileName), is(true));

        step("Click Submit");
        bulkActivateDeactivatePage.clickSubmit();
        Assertion.assertThat("[Sync][Bulk Activate/Deactivate] Verify user is able to submit bulk activate task for hotel level successfully", bulkActivateDeactivatePage.isActivityRowDisplayed(HotelDescription, hotelLevel, "Activate"), is(true));

        step("Click Add Task for Activate Vendor Level");
        bulkActivateDeactivatePage.clickAddTask();

        step("Select Activate chip");
        bulkActivateDeactivatePage.selectActionType("Activate");

        step("Select Vendor Level from dropdown");
        bulkActivateDeactivatePage.selectLevel(vendorLevel);

        step("Fill description");
        bulkActivateDeactivatePage.fillDescription(HotelDescription);

        step("Upload csv file");
        bulkActivateDeactivatePage.uploadCsvFile(vendorLevelCsvFile);
        Assertion.assertThat("Uploaded csv file is displayed", bulkActivateDeactivatePage.isCsvFileUploaded(vendorLevelCsvFileName), is(true));

        step("Click Submit");
        bulkActivateDeactivatePage.clickSubmit();
        Assertion.assertThat("[Sync][Bulk Activate/Deactivate] Verify user is able to submit bulk activate task for vendor level successfully", bulkActivateDeactivatePage.isActivityRowDisplayed(HotelDescription, vendorLevel, "Activate"), is(true));
    }
}