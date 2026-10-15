package com.tiket.web.dashboard.test.hotel.supply.mmmodule;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.web.dashboard.page.hotel.supply.mmmodule.HotelLibraryPage;
import com.tiket.web.dashboard.page.hotel.supply.mmmodule.HotelMergePage;
import com.tiket.web.dashboard.page.hotel.supply.mmmodule.LoginPage;
import com.tiket.web.dashboard.page.hotel.supply.mmmodule.SidebarPage;
import org.testng.annotations.Test;

import java.util.Map;

import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;

public class HotelMergeUnmergeTest extends BaseTest {

    @Author(names = {"Raihan", "Abhishek Sharma"})
    @Module(name = "HotelMerge")
    @Tribe(name = "Supply")
    @Vertical(name = "Accommodation")
    @Test()
    public void testUnmergeProperty() {
        Map<String, String> param = getParam("testUnmergeProperty");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        LoginPage<?> loginPage = new LoginPage<>(session);
        HotelLibraryPage<?> hotelLibraryPage = new HotelLibraryPage<>(session);

        String vendorName = param.get("vendorName");
        String vendorExternalId = param.get("vendorExternalId");
        String vendorName2 = param.get("vendorName2");
        String vendorExternalId2 = param.get("vendorExternalId2");

        String username = param.get("username");
        String password = param.get("password");

        step("Open MM Module");
        loginPage.openMMModule(username, password);
        Assertion.assertThat("Hotel Library page is loaded", hotelLibraryPage.isHotelLibraryPageLoaded(), is(true));
        Assertion.assertThat("Search result is displayed", hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(), is(true));

        step("Now search by Vendor Id and verify");
        hotelLibraryPage.clickSearchByVendorId();
        hotelLibraryPage.selectVendorName(vendorName);
        hotelLibraryPage.inputVendorExternalId(vendorExternalId);
        hotelLibraryPage.clickSearch();

        String vendorDetails = hotelLibraryPage.getTableFirstValueByHeading("Vendor Status");
        Assertion.assertThat("Verify vendor name 1", vendorDetails, containsStringIgnoringCase(vendorName));
        Assertion.assertThat("Verify vendor externalId 1", vendorDetails, containsString(vendorExternalId));
        Assertion.assertThat("Verify vendor name 2", vendorDetails, containsStringIgnoringCase(vendorName2));
        Assertion.assertThat("Verify vendor externalId 2", vendorDetails, containsString(vendorExternalId2));

        step("Unmerge property to revert the test data");
        hotelLibraryPage.clickAction();
        hotelLibraryPage.clickUnmerge();
        Assertion.assertThat("Unmerge property modal is displayed", hotelLibraryPage.isUnmergePropertyModalAppeared(), is(true));
        if (hotelLibraryPage.isUnmergeAllPropertyRadioButtonNotSelected()) {
            hotelLibraryPage.selectUnmergeAllPropertyRadioButton();
        }
        hotelLibraryPage.clickContinueUnmerge();
        Assertion.assertThat("Unmerge property reason modal is displayed", hotelLibraryPage.isUnmergePropertyModalAppeared(), is(true));
        hotelLibraryPage.inputOtherUnmergeReason(CoreFunctions.generateRandomString(10));
        hotelLibraryPage.clickUnmergeSubmit();
        Assertion.assertThat("Merge properties is successful", hotelLibraryPage.isSnackbarAppeared(), is(true));

        step("Search hotel by vendor external id");
        hotelLibraryPage.selectVendorName(vendorName);
        hotelLibraryPage.inputVendorExternalId(vendorExternalId);
        hotelLibraryPage.clickSearch();

        String unmergedVendorDetails = hotelLibraryPage.getTableFirstValueByHeading("Vendor Status");
        Assertion.assertThat("Search result is displayed", hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(), is(true));
        Assertion.assertThat("Verify vendor name 1", unmergedVendorDetails, containsStringIgnoringCase(vendorName));
        Assertion.assertThat("Verify vendor externalId 1", unmergedVendorDetails, containsString(vendorExternalId));
        Assertion.assertThat("Verify not contains vendor name 2", unmergedVendorDetails, not(containsString(vendorName2)));
        Assertion.assertThat("Verify not contains vendor externalId 2", unmergedVendorDetails, not(containsString(vendorExternalId2)));
    }

    @Author(names = {"Raihan", "Abhishek Sharma", "Riya Bajpai"})
    @Module(name = "HotelMerge")
    @Tribe(name = "Supply")
    @Vertical(name = "Accommodation")
    @Test
    public void testMergePropertyDifferentVendor() {
        Map<String, String> param = getParam("testMergePropertyDifferentVendor");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPage<?> loginPage = new LoginPage<>(session);
        HotelLibraryPage<?> hotelLibraryPage = new HotelLibraryPage<>(session);
        SidebarPage<?> sidebarPage = new SidebarPage<>(session);
        HotelMergePage<?> hotelMergePage = new HotelMergePage<>(session);

        String country = param.get("country");
        String region = param.get("region");
        String city = param.get("city");
        String area = param.get("area");
        String vendorName = param.get("vendorName");
        String vendorName2 = param.get("vendorName2");
        String vendorExternalId = param.get("vendorExternalId");
        String vendorExternalId2 = param.get("vendorExternalId2");
        String propertyName = param.get("propertyName");

        String username = param.get("username");
        String password = param.get("password");

        step("Open MM Module");
        loginPage.openMMModule(username, password);
        Assertion.assertThat("Hotel Library page is loaded", hotelLibraryPage.isHotelLibraryPageLoaded(), is(true));
        Assertion.assertThat("Search result is displayed", hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(), is(true));

        hotelLibraryPage.ensurePropertiesNotMerged(vendorName, vendorExternalId, vendorExternalId2);

        boolean unmerged = false;
        try {
            step("Go to hotel merge page");
            sidebarPage.clickSidebarMenu(SidebarPage.SidebarMenu.HOTEL_MERGE);
            sidebarPage.clickSidebarSubmenu(SidebarPage.SidebarMenu.MANUAL_MERGE);
            Assertion.assertThat("Hotel merge page is displayed", hotelMergePage.isHotelMergePageDisplayed(), is(true));

            step("Search source hotel");
            hotelMergePage.selectCountry("SOURCE", country);
            hotelMergePage.selectRegion("SOURCE", region);
            hotelMergePage.selectCity("SOURCE", city);
            hotelMergePage.selectArea("SOURCE", area);
            hotelMergePage.inputKeyword("SOURCE", propertyName);
            hotelMergePage.clickSearch("SOURCE");
            Assertion.assertThat("Search result for source hotel is available", hotelMergePage.getSearchResultAvailable("SOURCE", propertyName), containsString(propertyName));
            hotelMergePage.clickSourceActionRadioButtonByVendorName(vendorName, vendorExternalId);

            step("Search target hotel");
            hotelMergePage.inputKeyword("TARGET", propertyName);
            hotelMergePage.selectCountry("TARGET", country);
            hotelMergePage.selectRegion("TARGET", region);
            hotelMergePage.selectCity("TARGET", city);
            hotelMergePage.selectArea("TARGET", area);
            hotelMergePage.clickSearch("TARGET");
            Assertion.assertThat("Search result for source hotel is available", hotelMergePage.getSearchResultAvailable("TARGET", propertyName), containsString(propertyName));
            hotelMergePage.clickTargetActionRadioButtonByVendorName(vendorName2, vendorExternalId2);

            step("Click Merge button");
            hotelMergePage.clickMergeButton();

            step("Verify and click confirmation modal");
            Assertion.assertThat("Merge confirmation modal is displayed", hotelMergePage.isMergeConfirmationModalAppeared(), is(true));
            hotelMergePage.confirmHotelMerge();
            Assertion.assertThat("Merge properties is successful (Snackbar)", hotelMergePage.isSuccessfulSnackbarDisplayed(), is(true));

            step("Go to Hotel Library Page");
            sidebarPage.clickSidebarMenu(SidebarPage.SidebarMenu.HOTEL_CORE);
            sidebarPage.clickSidebarSubmenu(SidebarPage.SidebarMenu.HOTEL_LIBRARY);
            Assertion.assertThat("Hotel Library page is loaded", hotelLibraryPage.isHotelLibraryPageLoaded(), is(true));
            Assertion.assertThat("Search result is displayed", hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(), is(true));

            step("Now search by Vendor Id and verify");
            hotelLibraryPage.clickSearchByVendorId();
            hotelLibraryPage.selectVendorName(vendorName);
            hotelLibraryPage.inputVendorExternalId(vendorExternalId);
            hotelLibraryPage.clickSearch();

            Assertion.assertThat("Search result is displayed", hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(), is(true));
            Assertion.assertThat("Verify Property Name", hotelLibraryPage.getTableFirstValueByHeading("Name"), containsStringIgnoringCase(propertyName));
            Assertion.assertThat("Verify Province/ Region", hotelLibraryPage.getTableFirstValueByHeading("Province"), containsStringIgnoringCase(region));
            Assertion.assertThat("Verify City", hotelLibraryPage.getTableFirstValueByHeading("City"), containsStringIgnoringCase(city));
            Assertion.assertThat("Verify Area", hotelLibraryPage.getTableFirstValueByHeading("Area"), containsStringIgnoringCase(area));
            Assertion.assertThat("Verify Updated By", hotelLibraryPage.getTableFirstValueByHeading("Updated By"), containsStringIgnoringCase(username));

            String vendorDetails = hotelLibraryPage.getTableFirstValueByHeading("Vendor Status");
            Assertion.assertThat("Verify vendor name 1", vendorDetails, containsStringIgnoringCase(vendorName));
            Assertion.assertThat("Verify vendor externalId 1", vendorDetails, containsString(vendorExternalId));
            Assertion.assertThat("Verify vendor name 2", vendorDetails, containsStringIgnoringCase(vendorName2));
            Assertion.assertThat("Verify vendor externalId 2", vendorDetails, containsString(vendorExternalId2));

            step("Unmerge property to revert the test data");
            // flag only reflects a confirmed unmerge: if the submit silently did nothing, the finally block
            // still runs its cleanup (which re-checks the data before unmerging)
            unmerged = hotelLibraryPage.unmergeFirstResult();
            Assertion.assertThat("Unmerge properties is successful (Snackbar)", unmerged, is(true));

            step("Search hotel by vendor external id");
            hotelLibraryPage.selectVendorName(vendorName);
            hotelLibraryPage.inputVendorExternalId(vendorExternalId);
            hotelLibraryPage.clickSearch();

            String unmergedVendorDetails = hotelLibraryPage.getTableFirstValueByHeading("Vendor Status");
            Assertion.assertThat("Search result is displayed", hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(), is(true));
            Assertion.assertThat("Verify vendor name 1", unmergedVendorDetails, containsStringIgnoringCase(vendorName));
            Assertion.assertThat("Verify vendor externalId 1", unmergedVendorDetails, containsString(vendorExternalId));
            Assertion.assertThat("Verify not contains vendor name 2", unmergedVendorDetails, not(containsString(vendorName2)));
            Assertion.assertThat("Verify not contains vendor externalId 2", unmergedVendorDetails, not(containsString(vendorExternalId2)));
        } finally {
            if (!unmerged) {
                hotelLibraryPage.cleanupMergedProperties(sidebarPage, vendorName, vendorExternalId, vendorExternalId2);
            }
        }
    }

    @Author(names = {"Raihan", "Abhishek Sharma", "Riya Bajpai"})
    @Module(name = "HotelMerge")
    @Tribe(name = "Supply")
    @Vertical(name = "Accommodation")
    @Test
    public void testMergePropertySameVendor() {
        Map<String, String> param = getParam("testMergePropertySameVendor");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginPage<?> loginPage = new LoginPage<>(session);
        HotelLibraryPage<?> hotelLibraryPage = new HotelLibraryPage<>(session);
        SidebarPage<?> sidebarPage = new SidebarPage<>(session);
        HotelMergePage<?> hotelMergePage = new HotelMergePage<>(session);

        String country = param.get("country");
        String region = param.get("region");
        String city = param.get("city");
        String area = param.get("area");
        String vendorName = param.get("vendorName");
        String vendorExternalId = param.get("vendorExternalId");
        String vendorExternalId2 = param.get("vendorExternalId2");
        String propertyName = param.get("propertyName");
        String propertyName2 = param.get("propertyName2");

        String username = param.get("username");
        String password = param.get("password");

        step("Open MM Module");
        loginPage.openMMModule(username, password);
        Assertion.assertThat("Hotel Library page is loaded", hotelLibraryPage.isHotelLibraryPageLoaded(), is(true));
        Assertion.assertThat("Search result is displayed", hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(), is(true));

        hotelLibraryPage.ensurePropertiesNotMerged(vendorName, vendorExternalId, vendorExternalId2);

        boolean unmerged = false;
        try {
            step("Go to hotel merge page");
            sidebarPage.clickSidebarMenu(SidebarPage.SidebarMenu.HOTEL_MERGE);
            sidebarPage.clickSidebarSubmenu(SidebarPage.SidebarMenu.MANUAL_MERGE);
            Assertion.assertThat("Hotel merge page is displayed", hotelMergePage.isHotelMergePageDisplayed(), is(true));

            step("Search source hotel");
            hotelMergePage.selectCountry("SOURCE", country);
            hotelMergePage.selectRegion("SOURCE", region);
            hotelMergePage.selectCity("SOURCE", city);
            hotelMergePage.selectArea("SOURCE", area);
            hotelMergePage.inputKeyword("SOURCE", propertyName);
            hotelMergePage.clickSearch("SOURCE");
            Assertion.assertThat("Search result for source hotel is available", hotelMergePage.getSearchResultAvailable("SOURCE", propertyName), containsString(propertyName));
            hotelMergePage.clickSourceActionRadioButtonByVendorName(vendorName, vendorExternalId);

            step("Search target hotel");
            hotelMergePage.inputKeyword("TARGET", propertyName2);
            hotelMergePage.selectCountry("TARGET", country);
            hotelMergePage.selectRegion("TARGET", region);
            hotelMergePage.selectCity("TARGET", city);
            hotelMergePage.selectArea("TARGET", area);
            hotelMergePage.clickSearch("TARGET");
            Assertion.assertThat("Search result for source hotel is available", hotelMergePage.getSearchResultAvailable("TARGET", propertyName2), containsString(propertyName2));
            hotelMergePage.clickTargetActionRadioButtonByVendorName(vendorName, vendorExternalId2);

            step("Click Merge button");
            hotelMergePage.clickMergeButton();

            step("Verify and click confirmation modal");
            Assertion.assertThat("Merge confirmation modal is displayed", hotelMergePage.isMergeConfirmationModalAppeared(), is(true));
            hotelMergePage.confirmHotelMerge();
            Assertion.assertThat("Merge properties is successful (Snackbar)", hotelMergePage.isSuccessfulSnackbarDisplayed(), is(true));

            step("Go to Hotel Library Page");
            sidebarPage.clickSidebarMenu(SidebarPage.SidebarMenu.HOTEL_CORE);
            sidebarPage.clickSidebarSubmenu(SidebarPage.SidebarMenu.HOTEL_LIBRARY);
            Assertion.assertThat("Hotel Library page is loaded", hotelLibraryPage.isHotelLibraryPageLoaded(), is(true));
            Assertion.assertThat("Search result is displayed", hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(), is(true));

            step("Now search by Vendor Id and verify");
            hotelLibraryPage.clickSearchByVendorId();
            hotelLibraryPage.selectVendorName(vendorName);
            hotelLibraryPage.inputVendorExternalId(vendorExternalId);
            hotelLibraryPage.clickSearch();

            Assertion.assertThat("Search result is displayed", hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(), is(true));
            Assertion.assertThat("Verify Property Name", hotelLibraryPage.getTableFirstValueByHeading("Name"), containsStringIgnoringCase(propertyName));
            Assertion.assertThat("Verify Province/ Region", hotelLibraryPage.getTableFirstValueByHeading("Province"), containsStringIgnoringCase(region));
            Assertion.assertThat("Verify City", hotelLibraryPage.getTableFirstValueByHeading("City"), containsStringIgnoringCase(city));
            Assertion.assertThat("Verify Area", hotelLibraryPage.getTableFirstValueByHeading("Area"), containsStringIgnoringCase(area));
            Assertion.assertThat("Verify Updated By", hotelLibraryPage.getTableFirstValueByHeading("Updated By"), containsStringIgnoringCase(username));

            String vendorDetails = hotelLibraryPage.getTableFirstValueByHeading("Vendor Status");
            Assertion.assertThat("Verify vendor name 1", vendorDetails, containsStringIgnoringCase(vendorName));
            Assertion.assertThat("Verify vendor externalId 1", vendorDetails, containsString(vendorExternalId));
            Assertion.assertThat("Verify vendor name 2", vendorDetails, containsStringIgnoringCase(vendorName));
            Assertion.assertThat("Verify vendor externalId 2", vendorDetails, containsString(vendorExternalId2));

            step("Unmerge property to revert the test data");
            // flag only reflects a confirmed unmerge: if the submit silently did nothing, the finally block
            // still runs its cleanup (which re-checks the data before unmerging)
            unmerged = hotelLibraryPage.unmergeFirstResult();
            Assertion.assertThat("Unmerge properties is successful", unmerged, is(true));

            step("Search hotel by vendor external id");
            hotelLibraryPage.selectVendorName(vendorName);
            hotelLibraryPage.inputVendorExternalId(vendorExternalId);
            hotelLibraryPage.clickSearch();

            String unmergedVendorDetails = hotelLibraryPage.getTableFirstValueByHeading("Vendor Status");
            Assertion.assertThat("Search result is displayed", hotelLibraryPage.isSearchResultHotelNameLabelDisplayed(), is(true));
            Assertion.assertThat("Verify vendor name 1", unmergedVendorDetails, containsStringIgnoringCase(vendorName));
            Assertion.assertThat("Verify vendor externalId 1", unmergedVendorDetails, containsString(vendorExternalId));
            Assertion.assertThat("Verify not contains vendor externalId 2", unmergedVendorDetails, not(containsString(vendorExternalId2)));
        } finally {
            if (!unmerged) {
                hotelLibraryPage.cleanupMergedProperties(sidebarPage, vendorName, vendorExternalId, vendorExternalId2);
            }
        }
    }
}
