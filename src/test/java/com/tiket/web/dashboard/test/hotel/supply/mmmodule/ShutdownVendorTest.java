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
import com.tiket.web.dashboard.page.hotel.supply.mmmodule.ShutdownVendorPage;
import com.tiket.web.dashboard.page.hotel.supply.mmmodule.SidebarPage;
import org.testng.annotations.Test;

import java.util.Map;

import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.core.Is.is;

public class ShutdownVendorTest extends BaseTest {

    @Author(names = {"Raihan"})
    @Module(name = "ShutdownVendor")
    @Tribe(name = "Supply")
    @Vertical(name = "Accommodation")
    @Test
    public void testActivateVendor() {
        Map<String, String> param = getParam("testActivateVendor");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPage<?> loginPage = new LoginPage<>(session);
        HotelLibraryPage<?> hotelLibraryPage = new HotelLibraryPage<>(session);
        SidebarPage<?> sidebarPage = new SidebarPage<>(session);
        ShutdownVendorPage<?> shutdownVendorPage = new ShutdownVendorPage<>(session);

        String vendorName = param.get("vendorName");
        String username = param.get("username");
        String password = param.get("password");

        step("Open MM Module");
        loginPage.openMMModule(username, password);
        Assertion.assertThat("Hotel Library page is loaded", hotelLibraryPage.isHotelLibraryPageLoaded(), is(true));
        Assertion.assertThat("Search result is displayed", hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(), is(true));

        step("Go to shutdown vendor page");
        sidebarPage.clickSidebarMenu(SidebarPage.SidebarMenu.SETTINGS);
        sidebarPage.clickSidebarSubmenu(SidebarPage.SidebarMenu.SHUTDOWN_VENDOR);
        Assertion.assertThat("Shutdown vendor page is displayed", shutdownVendorPage.isShutdownVendorPageDisplayed(), is(true));

        step("Turn on vendor");
        shutdownVendorPage.turnVendorToggle(vendorName, true);
        Assertion.assertThat("Vendor is active", shutdownVendorPage.isVendorToggleOn(vendorName), is(true));
    }

    @Author(names = {"Raihan"})
    @Module(name = "ShutdownVendor")
    @Tribe(name = "Supply")
    @Vertical(name = "Accommodation")
    @Test()
    public void testDeactivateVendor() {
        Map<String, String> param = getParam("testDeactivateVendor");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPage<?> loginPage = new LoginPage<>(session);
        HotelLibraryPage<?> hotelLibraryPage = new HotelLibraryPage<>(session);
        SidebarPage<?> sidebarPage = new SidebarPage<>(session);
        ShutdownVendorPage<?> shutdownVendorPage = new ShutdownVendorPage<>(session);

        String vendorName = param.get("vendorName");
        String username = param.get("username");
        String password = param.get("password");

        step("Open MM Module");
        loginPage.openMMModule(username, password);
        Assertion.assertThat("Hotel Library page is loaded", hotelLibraryPage.isHotelLibraryPageLoaded(), is(true));
        Assertion.assertThat("Search result is displayed", hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(), is(true));

        step("Go to shutdown vendor page");
        sidebarPage.clickSidebarMenu(SidebarPage.SidebarMenu.SETTINGS);
        sidebarPage.clickSidebarSubmenu(SidebarPage.SidebarMenu.SHUTDOWN_VENDOR);
        Assertion.assertThat("Shutdown vendor page is displayed", shutdownVendorPage.isShutdownVendorPageDisplayed(), is(true));

        step("Turn off vendor");
        shutdownVendorPage.turnVendorToggle(vendorName, false);
        Assertion.assertThat("Vendor is inactive", shutdownVendorPage.isVendorToggleOn(vendorName), is(false));
    }

    @Author(names = {"Raihan"})
    @Module(name = "ShutdownVendor")
    @Tribe(name = "Supply")
    @Vertical(name = "Accommodation")
    @Test()
    public void testEditVendorConfig() {
        Map<String, String> param = getParam("testEditVendorConfig");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPage<?> loginPage = new LoginPage<>(session);
        HotelLibraryPage<?> hotelLibraryPage = new HotelLibraryPage<>(session);
        SidebarPage<?> sidebarPage = new SidebarPage<>(session);
        ShutdownVendorPage<?> shutdownVendorPage = new ShutdownVendorPage<>(session);

        String vendorName = param.get("vendorName");
        int startDate = Integer.parseInt(param.get("startDate"));
        int endDate = Integer.parseInt(param.get("endDate"));

        String username = param.get("username");
        String password = param.get("password");

        step("Open MM Module");
        loginPage.openMMModule(username, password);
        Assertion.assertThat("Hotel Library page is loaded", hotelLibraryPage.isHotelLibraryPageLoaded(), is(true));
        Assertion.assertThat("Search result is displayed", hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(), is(true));

        step("Go to shutdown vendor page");
        sidebarPage.clickSidebarMenu(SidebarPage.SidebarMenu.SETTINGS);
        sidebarPage.clickSidebarSubmenu(SidebarPage.SidebarMenu.SHUTDOWN_VENDOR);
        Assertion.assertThat("Shutdown vendor page is displayed", shutdownVendorPage.isShutdownVendorPageDisplayed(), is(true));

        step("Edit vendor config");
        shutdownVendorPage.clickEditVendorConfig(vendorName);
        Assertion.assertThat("Edit vendor config modal is appeared", shutdownVendorPage.isEditVendorConfigModalDisplayed(vendorName), is(true));
        shutdownVendorPage.selectDateByIndex(startDate, endDate);
        shutdownVendorPage.submitEditVendorConfig();
        Assertion.assertThat("Successful snackbar is appeared", shutdownVendorPage.isSuccessfulSnackbarAppeared(), is(true));
    }
}
