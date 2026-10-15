package com.tiket.web.dashboard.test.hotel.supply.mmmodule;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.web.dashboard.page.hotel.supply.mmmodule.HotelArchivePage;
import com.tiket.web.dashboard.page.hotel.supply.mmmodule.HotelLibraryPage;
import com.tiket.web.dashboard.page.hotel.supply.mmmodule.LoginPage;
import com.tiket.web.dashboard.page.hotel.supply.mmmodule.SidebarPage;
import org.testng.annotations.Test;

import java.util.Map;

import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.core.Is.is;

public class HotelArchiveTest extends BaseTest {

    @Author(names = {"Raihan"})
    @Module(name = "HotelArchive")
    @Tribe(name = "Supply")
    @Vertical(name = "Accommodation")
    @Test
    public void testArchiveProperty() {
        Map<String, String> param = getParam("testArchiveProperty");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPage<?> loginPage = new LoginPage<>(session);
        HotelLibraryPage<?> hotelLibraryPage = new HotelLibraryPage<>(session);
        SidebarPage<?> sidebarPage = new SidebarPage<>(session);
        HotelArchivePage<?> hotelArchivePage = new HotelArchivePage<>(session);

        String vendorName = param.get("vendorName");
        String vendorExternalId = param.get("vendorExternalId");
        String country = param.get("country");
        String region = param.get("region");
        String city = param.get("city");
        String propertyName = param.get("propertyName");

        String username = param.get("username");
        String password = param.get("password");

        step("Open MM Module");
        loginPage.openMMModule(username, password);
        Assertion.assertThat("Hotel Library page is loaded", hotelLibraryPage.isHotelLibraryPageLoaded(), is(true));
        Assertion.assertThat("Search result is displayed", hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(), is(true));

        step("Change tab to search by vendor Id");
        hotelLibraryPage.clickSearchByVendorId();

        step(String.format("Select %s as vendor name", vendorName));
        hotelLibraryPage.selectVendorName(vendorName);
        Assertion.assertThat(String.format("Vendor name selected: %s", vendorName), hotelLibraryPage.isVendorNameSelected(vendorName), is(true));

        step(String.format("Input %s as vendor external ID", vendorExternalId));
        hotelLibraryPage.inputVendorExternalId(vendorExternalId);

        step("Click search button");
        hotelLibraryPage.clickSearch();
        Assertion.assertThat("Search result is displayed", hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(), is(true));
        Assertion.assertThat(String.format("%s is displayed in search result", propertyName), hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(propertyName), is(true));

        step("Click action and delete property button");
        hotelLibraryPage.clickAction();
        hotelLibraryPage.deleteProperty();
        Assertion.assertThat("Delete property is done", hotelLibraryPage.isSnackbarAppeared(), is(true));
        Assertion.assertThat("Property is not appeared on hotel library", hotelLibraryPage.isSearchResultNotFound(), is(true));

        step("Open Hotel Archive page");
        sidebarPage.clickSidebarSubmenu(SidebarPage.SidebarMenu.HOTEL_ARCHIVE);
        Assertion.assertThat("Hotel Archive page is displayed", hotelArchivePage.isHotelArchivePageDisplayed(), is(true));

        step("Search archived property");
        hotelArchivePage.searchAndSelectLocationByType("COUNTRY", country);
        hotelArchivePage.searchAndSelectLocationByType("REGION", region);
        hotelArchivePage.searchAndSelectLocationByType("CITY", city);
        hotelArchivePage.inputPropertyName(propertyName);
        hotelArchivePage.clickSearch();
        Assertion.assertThat("Property is found on hotel archive", hotelArchivePage.isSearchResultDisplayedByPropertyName(propertyName), is(true));
    }

    @Author(names = {"Raihan"})
    @Module(name = "HotelArchive")
    @Tribe(name = "Supply")
    @Vertical(name = "Accommodation")
    @Test
    public void testUnarchiveProperty() {
        Map<String, String> param = getParam("testUnarchiveProperty");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPage<?> loginPage = new LoginPage<>(session);
        HotelLibraryPage<?> hotelLibraryPage = new HotelLibraryPage<>(session);
        SidebarPage<?> sidebarPage = new SidebarPage<>(session);
        HotelArchivePage<?> hotelArchivePage = new HotelArchivePage<>(session);

        String vendorName = param.get("vendorName");
        String vendorExternalId = param.get("vendorExternalId");
        String country = param.get("country");
        String region = param.get("region");
        String city = param.get("city");
        String propertyName = param.get("propertyName");

        String username = param.get("username");
        String password = param.get("password");

        step("Open MM Module");
        loginPage.openMMModule(username, password);
        Assertion.assertThat("Hotel Library page is loaded", hotelLibraryPage.isHotelLibraryPageLoaded(), is(true));
        Assertion.assertThat("Search result is displayed", hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(), is(true));

        step("Open Hotel Archive page");
        sidebarPage.clickSidebarSubmenu(SidebarPage.SidebarMenu.HOTEL_ARCHIVE);
        Assertion.assertThat("Hotel Archive page is displayed", hotelArchivePage.isHotelArchivePageDisplayed(), is(true));

        step("Search archived property");
        hotelArchivePage.searchAndSelectLocationByType("COUNTRY", country);
        hotelArchivePage.searchAndSelectLocationByType("REGION", region);
        hotelArchivePage.searchAndSelectLocationByType("CITY", city);
        hotelArchivePage.inputPropertyName(propertyName);
        hotelArchivePage.clickSearch();
        Assertion.assertThat("Property is found on hotel archive", hotelArchivePage.isSearchResultDisplayedByPropertyName(propertyName), is(true));

        step("Restore property to hotel library");
        hotelArchivePage.restorePropertyToHotelLibrary();
        Assertion.assertThat("Property is not appeared on Hotel Archive", hotelArchivePage.isSearchResultNotFound(), is(true));

        step("Open Hotel Library page");
        sidebarPage.clickSidebarSubmenu(SidebarPage.SidebarMenu.HOTEL_LIBRARY);
        Assertion.assertThat("Hotel Library page is loaded", hotelLibraryPage.isHotelLibraryPageLoaded(), is(true));
        Assertion.assertThat("Search result is displayed", hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(), is(true));

        step("Change tab to search by vendor Id");
        hotelLibraryPage.clickSearchByVendorId();

        step(String.format("Select %s as vendor name", vendorName));
        hotelLibraryPage.selectVendorName(vendorName);
        Assertion.assertThat(String.format("Vendor name selected: %s", vendorName), hotelLibraryPage.isVendorNameSelected(vendorName), is(true));

        step(String.format("Input %s as vendor external ID", vendorExternalId));
        hotelLibraryPage.inputVendorExternalId(vendorExternalId);

        step("Click search button");
        hotelLibraryPage.clickSearch();
        Assertion.assertThat("Search result is displayed", hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(), is(true));
        Assertion.assertThat(String.format("%s is displayed in search result", propertyName), hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(propertyName), is(true));
    }
}
